package com.scp.api.controller;

import com.scp.api.model.ResultVo;
import com.scp.api.model.User;
import com.scp.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author: XingPc
 * @Description: 用户控制层
 * @Date: 2020/4/20 21:44
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/addUser")
    public String addUser(@RequestBody @Valid User user) {
        return userService.addUser(user);
    }

    @GetMapping("/getUser")
    public User getUser() {
        User user = new User(1L,"hostsho","123456","183726@163.com");
        return user;
    }

}
