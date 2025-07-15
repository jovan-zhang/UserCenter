package com.jovan.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jovan.usercenter.common.ErrorCode;
import com.jovan.usercenter.exception.BusinessException;
import com.jovan.usercenter.model.request.UserUpdateRequest;
import com.jovan.usercenter.service.UserService;
import com.jovan.usercenter.model.domain.User;

import com.jovan.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.jovan.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author jovan
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2025-07-03 20:35:49
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "账号或密码不得为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "账号长度不得小于四位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "密码长度不得小于8位");
        }

        // 账户不能包含特殊字符
        String validPattern = "^[a-zA-Z0-9_]+$";
        if (!userAccount.matches(validPattern)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "账号不能包含特殊字符");
        }

        //密码和校验密码需相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "两次密码输入不一致");
        }

        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);

        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "账号名不得重复");
        }

        //对密码进行加密
        String hashedPassword = BCrypt.hashpw(userPassword, BCrypt.gensalt());

        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(hashedPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入数据失败");
        }
        //返回用户id
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户名或密码为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户名不得小于四位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "密码不得小于八位");
        }

        // 账户不能包含特殊字符
        String validPattern = "^[a-zA-Z0-9_]+$";
        if (!userAccount.matches(validPattern)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户名不得包含特殊字符");
        }

        //校验用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = this.count(queryWrapper);
        if (count == 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户名不存在");
        }

        //校验密码是否正确
        User user = this.getOne(queryWrapper);
        String hashedPassword = user.getUserPassword();
        if (!BCrypt.checkpw(userPassword, hashedPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户名或密码错误");
        }

        //用户脱敏
        User safetyUser = getSafetyUser(user);

        // 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param user 原用户信息
     * @return 脱敏用户信息
     */

    @Override
    public User getSafetyUser(User user) {

        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setNickname(user.getNickname());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setRole(user.getRole());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());

        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "HTTP请求为空");
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public User userUpdate(UserUpdateRequest userUpdateRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userUpdateRequest.getId());

        User existingUser = this.getOne(queryWrapper);
        if (existingUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户不存在");
        }
        // 更新用户信息
        existingUser.setNickname(userUpdateRequest.getNickName());
        existingUser.setAvatarUrl(userUpdateRequest.getAvatarUrl());
        existingUser.setGender(userUpdateRequest.getGender());
        existingUser.setPhone(userUpdateRequest.getPhone());
        existingUser.setEmail(userUpdateRequest.getEmail());
        boolean updateResult = this.updateById(existingUser);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新用户信息失败");
        }
        // 返回更新后的用户信息
        return getSafetyUser(existingUser);

    }
}




