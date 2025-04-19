package com.yupi.soj.judge;

import com.yupi.soj.judge.strategy.DefaultJudgeStrategy;
import com.yupi.soj.judge.strategy.JavaJudgeStrategy;
import com.yupi.soj.judge.strategy.JudgeContext;
import com.yupi.soj.judge.strategy.JudgeStrategy;
import com.yupi.soj.judge.codesandbox.model.JudgeInfo;
import com.yupi.soj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {
    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
