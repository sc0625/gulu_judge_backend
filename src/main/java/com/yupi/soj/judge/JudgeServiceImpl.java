package com.yupi.soj.judge;

import cn.hutool.json.JSONUtil;
import com.yupi.soj.common.ErrorCode;
import com.yupi.soj.exception.BusinessException;
import com.yupi.soj.judge.codesandbox.CodeSandbox;
import com.yupi.soj.judge.codesandbox.CodeSandboxFactory;
import com.yupi.soj.judge.codesandbox.CodeSandboxProxy;
import com.yupi.soj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.soj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yupi.soj.judge.strategy.JudgeContext;
import com.yupi.soj.model.dto.question.JudgeCase;
import com.yupi.soj.judge.codesandbox.model.JudgeInfo;
import com.yupi.soj.model.entity.Question;
import com.yupi.soj.model.entity.QuestionSubmit;
import com.yupi.soj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.soj.service.QuestionService;
import com.yupi.soj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Value("${codesandbox.type:example}")
    private String type;
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Resource
    private QuestionService questionService;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(long id) {
        //1)先通过id来获取题目提交信息，如果信息存在再进行下一步
        QuestionSubmit questionSubmit = questionSubmitService.getById(id);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        //判断题目是否存在，如果存在再进行下一步
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        //2)判断当前题目提交是否是等待中（未判题状态），避免用户反复提交，多次重复判题
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        //3)更改题目提交信息状态为“判题中”
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(id);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        //4)调用代码沙箱
        // 使用工厂创建集中返回对象，并使用代理
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        //获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        //todo 这段代码后面理解一下，什么意思，具体怎么实现
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        //todo 这个是通过构造器模式来生成一个请求参数对象，可以通过.字段名()的方式来给属性赋值，想使用这种方式需要使用@Builder注解
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
        //5)根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setQuestion(question);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestionSubmit(questionSubmit);
        //6)判断答案和执行条件是否符合预期
        JudgeInfo judgeInfo=judgeManager.doJudge(judgeContext);
        //修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(id);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionId);
        return questionSubmitResult;
    }
}
