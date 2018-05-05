# 后端API接口文档
## 其他
### /login[POST]
* 传递登录动作的相关参数
* 请求格式
{  
name: 用户名,
key: 密码,
type: [requester|worker|admin]用户身份
}
* 响应格式
{
result: [success|failed|error][登录成功|用户状态不正常|登录失败],
~id: 用户id,
~name: 用户名,
~type: [requester|worker|admin]用户身份
}

## 发起者部分
### /requester[POST]
* 新增一个发起者的信息
* 请求格式
{
name: 用户名,
brith: [yyyy-MM-dd]出生日期,
sex: [ 0 | 1 ][ 男 | 女 ]性别,
email: 邮箱,
occupation: 职务,
company: 所属公司,
}
* 响应格式
status 200 添加成功
status 500 添加失败
  * 错误原因
此用户名已存在
用户信息校验出错

### /requester/{id}[GET]
* 获取一位发起者的基本信息
* 响应格式
{
id: 用户标识,
name: 用户名,
brith: [yyyy-MM-dd]出生日期,
sex: [ 0 | 1 ][ 男 | 女 ]性别,
email: 邮箱,
occupation: 职务,
company: 所属公司,
state: 用户状态
}
 * 错误原因
此id不存在

### /requester/{id}[PUT]
* 更新一位发起者的基本信息
* 响应格式
同上
