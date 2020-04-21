package com.scp.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: XingPc
 * @Description: 响应码枚举类
 * @Date: 2020/4/21 9:14
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum  ResultCode {

    SUCCESS(1000,"操作成功！"),

    FAILED(1001,"响应失败！"),

    ARGS_VALIDATE_FAILED(1002,"参数校验失败！"),

    ERROR(5000,"系统位置错误！");

    private int code;

    private String msg;

}
