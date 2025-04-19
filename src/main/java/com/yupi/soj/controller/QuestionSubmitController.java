package com.yupi.soj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.soj.annotation.AuthCheck;
import com.yupi.soj.common.BaseResponse;
import com.yupi.soj.common.ErrorCode;
import com.yupi.soj.common.ResultUtils;
import com.yupi.soj.constant.UserConstant;
import com.yupi.soj.exception.BusinessException;
import com.yupi.soj.model.dto.question.QuestionQueryRequest;
import com.yupi.soj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.soj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.soj.model.entity.Question;
import com.yupi.soj.model.entity.QuestionSubmit;
import com.yupi.soj.model.entity.User;
import com.yupi.soj.model.vo.QuestionSubmitVO;
import com.yupi.soj.service.QuestionSubmitService;
import com.yupi.soj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
//@RequestMapping("/question_submit")
@Slf4j
@Deprecated
public class QuestionSubmitController {

}
