package com.yupi.soj.judge.codesandbox.impl;

import com.yupi.soj.judge.codesandbox.CodeSandbox;
import com.yupi.soj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.soj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yupi.soj.judge.codesandbox.model.JudgeInfo;
import com.yupi.soj.model.enums.QuestionSubmitJudgeInfoEnum;
import com.yupi.soj.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 示例代码沙箱（仅为了跑通业务流程）
 */
@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(QuestionSubmitJudgeInfoEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}

