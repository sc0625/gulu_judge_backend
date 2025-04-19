package com.yupi.soj.judge.codesandbox;

import com.yupi.soj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.yupi.soj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.yupi.soj.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实例），这里用的是多例模式（每次返回一个新的实例）
 * 如果确定代码沙箱不会出现线程安全问题，可复用，那么可以尝试使用单例模式
 */
public class CodeSandboxFactory {
    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();//如果不匹配返回默认
        }
    }
}
