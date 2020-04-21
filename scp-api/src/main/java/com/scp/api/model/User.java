package com.scp.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author: XingPc
 * @Description: 用户实体
 * @Date: 2020/4/20 21:16
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
public class User {

    @NotNull(message = "用户id不能为空！")
    private long id;

    @NotNull(message = "账户名不能为空！")
    @Size(min = 6, max = 11, message = "账户必须是6-11位！")
    private String account;

    @NotNull(message = "用户密码不能为空！")
    @Size(min = 6, max = 11, message = "密码必须是6-11位！")
    private String password;

    @NotNull(message = "用户email不能为空！")
    @Email(message = "邮箱格式不正确！")
    private String email;

}
