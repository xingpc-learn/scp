package com.scp.api.service;

import com.scp.api.model.User;
import org.springframework.stereotype.Service;

/**
 * @Author: XingPc
 * @Description: 用户服务层
 * @Date: 2020/4/20 21:46
 * @Version: 1.0
 */
@Service
public class UserService {

    public String addUser(User user) {
        return "success";
    }

}
