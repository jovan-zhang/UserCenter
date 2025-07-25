# 统一用户管理平台

> 该项目仅部署后端，提供常用接口供演示

部署地址：117.72.168.65/api

## 常用接口

### 登录

POST http://117.72.168.65/api/user/login
Content-Type: application/json
```json
{
"userAccount": "admin",
"userPassword": "12345678"
}
```
### 注册

POST http://117.72.168.65/api/user/register
Content-Type: application/json
```json
{
"userAccount": "testUser1",
"userPassword": "12345678",
"checkPassword": "12345678"
}
```

### 查找所有用户
GET http://117.72.168.65/api/user/search
