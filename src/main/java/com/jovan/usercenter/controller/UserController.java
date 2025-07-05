package com.jovan.usercenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jovan.usercenter.model.domain.User;
import com.jovan.usercenter.model.request.UserLoginRequest;
import com.jovan.usercenter.model.request.UserRegisterRequest;
import com.jovan.usercenter.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.jovan.usercenter.constant.UserConstant.ADMIN_USER;
import static com.jovan.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author jovan
 */
@RestController
@RequestMapping("/user")

public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    public List<User> searchUserList(String userAccount, HttpServletRequest request) {
        //仅管理员可查询
        if (hasNoPermission(request)) {
            return new ArrayList<>();
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (userAccount == null) {
            userAccount = "";
        }

        queryWrapper.like("user_account", userAccount);
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());

    }

    @PostMapping("/delete")
    public boolean userDelete(@RequestBody long userId, HttpServletRequest request) {
        //仅管理员可删除
        if (hasNoPermission(request)) {
            return false;
        }
        if (userId <= 0) {
            return false;
        }
        return userService.removeById(userId);
    }

    /**
     * 检查管理员权限
     *
     * @param request 请求
     * @return 是否无权限
     */
    private boolean hasNoPermission(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObject;

        return user == null || user.getRole() != ADMIN_USER;
    }

}
