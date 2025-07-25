# 统一用户管理平台

> 该项目仅部署后端，提供常用接口供演示

## 常用接口

### 查找所有用户
浏览器访问:http://117.72.168.65/api/user/search ，返回用户未登录即为成功
或者使用curl命令，POST方法建议使用其他工具
```shell
curl http://117.72.165.85/api/user/search
```
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
