package com.yupi.soj.judge.codesandbox.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.soj.common.ErrorCode;
import com.yupi.soj.exception.BusinessException;
import com.yupi.soj.judge.codesandbox.CodeSandbox;
import com.yupi.soj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.soj.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱
 */
public class RemoteCodeSandbox implements CodeSandbox {

    private static final String AUTH_REQUEST_HEADER = "auth";//通过约定好的字符串判断，请求方是否是被许可的

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://192.168.159.128:8081/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseBody = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseBody)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "execute remoteSandbox error, message = " + responseBody);
        }
        return JSONUtil.toBean(responseBody,ExecuteCodeResponse.class);
    }
}
