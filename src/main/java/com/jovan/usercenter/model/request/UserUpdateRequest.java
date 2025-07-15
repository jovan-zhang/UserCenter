package com.jovan.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String NickName;

    private String avatarUrl;

    private Integer gender;

    private String phone;

    private String email;

}
