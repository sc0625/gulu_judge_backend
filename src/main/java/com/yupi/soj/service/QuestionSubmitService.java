package com.yupi.soj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.soj.model.dto.question.QuestionQueryRequest;
import com.yupi.soj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.soj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.soj.model.entity.Question;
import com.yupi.soj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.soj.model.entity.User;
import com.yupi.soj.model.vo.QuestionSubmitVO;
import com.yupi.soj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author sc
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2024-07-27 15:17:49
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
