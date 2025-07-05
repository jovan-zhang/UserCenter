package com.jovan.usercenter.service;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import com.jovan.usercenter.model.domain.User;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户测试
 *
 * @author jovan
 */

@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;



    @Test
    void testUserRegisterPasswordTooShort() {
        String userAccount = "jovan";
        String userPassword = "123";
        String checkPassword = "123";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertThat(result).isEqualTo(-1);
    }

    @Test
    void testUserRegisterAccountTooShort() {
        String userAccount = "jo";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertThat(result).isEqualTo(-1);
    }

    @Test
    void testUserRegisterPasswordMismatch() {
        String userAccount = "jovan";
        String userPassword = "12345678";
        String checkPassword = "87654321";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertThat(result).isEqualTo(-1);
    }

    @Test
    void testUserRegisterWithSpecialCharacters() {
        String userAccount = "jovan@#$";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertThat(result).isEqualTo(-1);
    }

    @Test
    void testUserRegisterWithEmptyAccount() {
        String userAccount = "";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertThat(result).isEqualTo(-1);
    }

    @Test
    void testUserRegisterWithNullParameters() {
        long result = userService.userRegister(null, null, null);
        Assertions.assertThat(result).isEqualTo(-1);
    }




}