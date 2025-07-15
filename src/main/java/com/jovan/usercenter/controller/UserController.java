package com.jovan.usercenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jovan.usercenter.common.BaseResponse;
import com.jovan.usercenter.common.ErrorCode;
import com.jovan.usercenter.common.ResultUtils;
import com.jovan.usercenter.exception.BusinessException;
import com.jovan.usercenter.model.domain.User;
import com.jovan.usercenter.model.request.UserLoginRequest;
import com.jovan.usercenter.model.request.UserRegisterRequest;
import com.jovan.usercenter.model.request.UserUpdateRequest;
import com.jovan.usercenter.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");
        }
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");
        }
        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUserList(String userAccount, HttpServletRequest request) {
        //仅管理员可查询
        if (hasNoPermission(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "查询权限不足");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (userAccount == null) {
            userAccount = "";
        }

        queryWrapper.like("user_account", userAccount);
        List<User> userList = userService.list(queryWrapper);
        //用户脱敏
        List<User> result = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(result);

    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> userDelete(@RequestBody long userId, HttpServletRequest request) {
        //仅管理员可删除
        if (hasNoPermission(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "删除权限不足");
        }
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "删除的用户id小于1");
        }
        boolean result = userService.removeById(userId);
        return ResultUtils.success(result);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }


    @PostMapping("/update")
    public BaseResponse<User> userUpdate(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        //仅管理员或自己可更新
        if (hasNoPermission(request) && !Objects.equals(userUpdateRequest.getId(), getLoginUser(request).getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "更新权限不足");
        }
        if (userUpdateRequest == null || userUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "更新的用户id小于1");
        }
        User updateUser = userService.userUpdate(userUpdateRequest);
        if (updateUser == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新用户失败");
        }
        return ResultUtils.success(updateUser);
    }

    /**
     * 检查管理员权限
     *
     * @param request 请求
     * @return 是否无权限
     */
    private boolean hasNoPermission(HttpServletRequest request) {
        User user = getLoginUser(request);

        return user.getRole() != ADMIN_USER;
    }

    /**
     * 获取登录态用户
     *
     * @param request 请求
     * @return 登录态用户
     */
    private User getLoginUser(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObject == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        return (User) userObject;
    }
}
