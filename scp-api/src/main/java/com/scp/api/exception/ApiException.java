package com.scp.api.exception;

import lombok.Getter;

/**
 * @Author: XingPc
 * @Description: 自定义异常类
 * @Date: 2020/4/21 8:31
 * @Version: 1.0
 */
@Getter
public class ApiException extends RuntimeException {

    private int code;

    private String msg;

    public ApiException() {
        this(1001,"接口调用异常");
    }

    public ApiException(String msg) {
        this(1001,msg);
    }

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
