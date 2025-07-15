package com.jovan.usercenter.service;

import com.jovan.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jovan.usercenter.model.request.UserUpdateRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jovan
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2025-07-03 20:35:49
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 返回新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      请求
     * @return 返回用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param user 原用户
     * @return 脱敏用户
     */
    User getSafetyUser(User user);

    /**
     * 用户注销
     *
     * @return 1-成功， -1失败
     */
    int userLogout(HttpServletRequest request);

    User userUpdate(UserUpdateRequest userUpdateRequest);
}


