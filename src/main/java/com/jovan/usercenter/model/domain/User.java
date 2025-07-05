package com.jovan.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    /**
     * 用户ID，主键自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 头像URL
     */
    @TableField(value = "avatar_url")
    private String avatarUrl;

    /**
     * 性别：0-未知，1-男，2-女
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 账号，唯一标识
     */
    @TableField(value = "user_account")
    private String userAccount;

    /**
     * 密码
     */
    @TableField(value = "user_password")
    private String userPassword;

    /**
     * 电话号码
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱地址
     */
    @TableField(value = "email")
    private String email;

    /**
     * 0-正常，1-禁用
     */
    @TableField(value = "user_status")
    private Integer userStatus;

    /**
     * 0-普通用户，1-管理员
     */
    @TableField(value = "role")
    private Integer role;

    /**
     * 创建时间，数据插入时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间，数据更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableLogic
    @TableField(value = "is_delete")
    private Integer isDelete;
}