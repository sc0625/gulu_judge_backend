package com.yupi.soj.model.vo;

import cn.hutool.json.JSONUtil;
import com.yupi.soj.judge.codesandbox.model.JudgeInfo;
import com.yupi.soj.model.entity.QuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交封装类
 */
@Data
public class QuestionSubmitVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题状态（0-待判题、1-判题中、2-成功、3-失败）
     */
    private Integer status;

    /**
     * 判题信息（json对象）
     */
    private JudgeInfo judgeInfo;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 包装类转对象
     *
     * @param questionSubmitVO
     * @return
     */
    public static QuestionSubmit voToObj(QuestionSubmitVO questionSubmitVO) {
        if (questionSubmitVO == null) {
            return null;
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVO, questionSubmit);//将能复制的属性进行拷贝
        JudgeInfo judgeInfo = questionSubmitVO.getJudgeInfo();
        if(judgeInfo!=null){
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        }
        return questionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return
     */
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);
        JudgeInfo judgeInfo = JSONUtil.toBean(questionSubmit.getJudgeInfo(), JudgeInfo.class);
        questionSubmitVO.setJudgeInfo(judgeInfo);
        return questionSubmitVO;
    }

    private static final long serialVersionUID = 1L;
}