package com.yupi.soj.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.soj.common.ErrorCode;
import com.yupi.soj.constant.CommonConstant;
import com.yupi.soj.exception.BusinessException;
import com.yupi.soj.judge.JudgeService;
import com.yupi.soj.model.dto.question.QuestionQueryRequest;
import com.yupi.soj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.soj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.soj.model.entity.Question;
import com.yupi.soj.model.entity.QuestionSubmit;
import com.yupi.soj.model.entity.QuestionSubmit;
import com.yupi.soj.model.entity.User;
import com.yupi.soj.model.enums.QuestionSubmitLanguageEnum;
import com.yupi.soj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.soj.model.vo.QuestionSubmitVO;
import com.yupi.soj.model.vo.QuestionVO;
import com.yupi.soj.model.vo.UserVO;
import com.yupi.soj.service.QuestionService;
import com.yupi.soj.service.QuestionSubmitService;
import com.yupi.soj.service.QuestionSubmitService;
import com.yupi.soj.mapper.QuestionSubmitMapper;
import com.yupi.soj.service.UserService;
import com.yupi.soj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author sc
 * @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
 * @createDate 2024-07-27 15:17:49
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private JudgeService judgeService;


    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // todo 阅读 校验编程语言是否合法 通过自定义方法获取对应的枚举
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);//尝试获取这个枚举值
        if (languageEnum == null) {//如果没找到，说明语言有问题
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        long userId = loginUser.getId();
        // 每次创建一个新的提交记录
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        // todo 调用判题服务，对存入数据库的提交记录进行判题
        long questionSubmitId=questionSubmit.getId();
        CompletableFuture.runAsync(()->{
            judgeService.doJudge(questionSubmitId);
        });
        return questionSubmit.getId();
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询，得到mybatis框架支持的QueryWrapper类）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        // todo 下面这两个可能适用于排序的东西，现在先不管，照着用就行
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        // todo 知识点 isNotBlack用于字符串会判断是否为null，如果为null再判断是否为空，只有二者都不满足才返回true
        // todo isNotEmpty只判断对象是否为空
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);//将questionSubmit转化为VO对象，脱敏，删除不想给前端的信息
        //额外脱敏，仅本人和管理员能看见自己提交的代码
        long userId = loginUser.getId();
        //如果当前登录的用户跟题目提交用户不是同一个并且当前用户不是管理员
        if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);//把代码设置为空
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser)).collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
}




