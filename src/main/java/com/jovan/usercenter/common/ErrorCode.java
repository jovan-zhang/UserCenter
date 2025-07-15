package com.jovan.usercenter.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    PARAM_ERROR(40000, "请求参数错误", "请求参数错误"),
    NULL_ERROR(40001, "数据为空", "数据为空"),
    NO_AUTH(40100, "权限不足", "没有权限"),
    SYSTEM_ERROR(50000, "系统内部异常", "系统内部异常"),
    NOT_LOGIN(40101, "未登录", "用户未登录");

    private final int code;

    private final String message;

    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

}
