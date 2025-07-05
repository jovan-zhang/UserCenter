package com.jovan.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        // 账户不能包含特殊字符
        String validPattern = "^[a-zA-Z0-9_]+$";
        if (!userAccount.matches(validPattern)) {
            return -1;
        }

        //密码和校验密码需相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);

        long count = this.count(queryWrapper);
        if (count > 0) {
            return -1;
        }

        //对密码进行加密
        String hashedPassword = BCrypt.hashpw(userPassword, BCrypt.gensalt());

        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(hashedPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            // todo 修改为自定义异常
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }

        // 账户不能包含特殊字符
        String validPattern = "^[a-zA-Z0-9_]+$";
        if (!userAccount.matches(validPattern)) {
            return null;
        }

        //校验用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = this.count(queryWrapper);
        if (count == 0) {
            return null;
        }

        //校验密码是否正确
        User user = this.getOne(queryWrapper);
        String hashedPassword = user.getUserPassword();
        if (!BCrypt.checkpw(userPassword, hashedPassword)) {
            log.info("Wrong password!");
            return null;
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
}




