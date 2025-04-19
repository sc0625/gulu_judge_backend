package com.yupi.soj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目提交编程语言枚举
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public enum QuestionSubmitJudgeInfoEnum {

    ACCEPTED("ACCEPTED", "ACCEPTED"),
    WRONG_ANSWER("WRONG_ANSWER", "WRONG_ANSWER"),
    COMPILE_ERROR("COMPILE_ERROR", "COMPILE_ERROR"),
    MEMORY_LIMIT_EXCEEDED("MEMORY_LIMIT_EXCEEDED", "MEMORY_LIMIT_EXCEEDED"),
    TIME_LIMIT_EXCEEDED("TIME_LIMIT_EXCEEDED", "TIME_LIMIT_EXCEEDED"),
    PRESENTATION_ERROR("PRESENTATION_ERROR", "PRESENTATION_ERROR"),
    WAITING("WAITING", "WAITING"),
    OUTPUT_LIMIT_EXCEEDED("OUTPUT_LIMIT_EXCEEDED", "OUTPUT_LIMIT_EXCEEDED"),
    DANGEROUS_OPERATION("DANGEROUS_OPERATION", "DANGEROUS_OPERATION"),
    RUNTIME_ERROR("RUNTIME_ERROR", "RUNTIME_ERROR"),
    SYSTEM_ERROR("SYSTEM_ERROR", "SYSTEM_ERROR");


    private final String text;

    private final String value;

    QuestionSubmitJudgeInfoEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static QuestionSubmitJudgeInfoEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitJudgeInfoEnum anEnum : QuestionSubmitJudgeInfoEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
