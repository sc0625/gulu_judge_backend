package com.yupi.soj.judge.codesandbox;

import com.yupi.soj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.soj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 仅仅返回执行结果，不做任何判断
 */
public interface CodeSandbox {
    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
