package com.scp.api.exception;

import com.scp.api.model.ResultCode;
import com.scp.api.model.ResultVo;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: XingPc
 * @Description: 全局异常处理
 * @Date: 2020/4/20 22:33
 * @Version: 1.0
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVo<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        return new ResultVo<>(ResultCode.ARGS_VALIDATE_FAILED,exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(ApiException.class)
    public ResultVo<String> apiExceptionHandler(ApiException exception) {
        return new ResultVo<>(ResultCode.FAILED,exception.getMsg());
    }

}
