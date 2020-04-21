package com.scp.api.model;

import lombok.Data;
import lombok.Getter;

/**
 * @Author: XingPc
 * @Description: 统一响应体类
 * @Date: 2020/4/21 8:45
 * @Version: 1.0
 */
@Getter
public class ResultVo<T> {

    private int code;

    private String msg;

    private T data;

    public ResultVo(T data) {
        this(ResultCode.SUCCESS,data);
    }

    public ResultVo(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.data = data;
    }

}
