-- auto-generated definition
create table user
(
    id            bigint unsigned auto_increment comment '用户ID，主键自增'
        primary key,
    nickname      varchar(50)                        null comment '昵称',
    avatar_url    varchar(1024)                      null comment '头像URL',
    gender        tinyint                            null comment '性别：0-未知，1-男，2-女',
    user_account  varchar(50)                        null comment '账号，唯一标识',
    user_password varchar(100)                       not null comment '密码',
    phone         varchar(20)                        null comment '电话号码',
    email         varchar(100)                       null comment '邮箱地址',
    user_status   int                                null comment '0-正常，1-禁用',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间，数据插入时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间，数据更新时间',
    is_delete     tinyint  default 0                 null comment '逻辑删除标志：0-未删除，1-已删除',
    role          int      default 0                 not null comment '用户角色 0-普通用户 1-管理员',
    constraint user_account
        unique (user_account)
)
    comment '用户表' charset = utf8mb4;



