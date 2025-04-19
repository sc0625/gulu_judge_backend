package com.yupi.soj.judge;

import com.yupi.soj.model.entity.QuestionSubmit;
import com.yupi.soj.model.vo.QuestionSubmitVO;

public interface JudgeService {

    /**
     * 判题
     * @param id
     * @return
     */
    QuestionSubmit doJudge(long id);

}
