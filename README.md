# 后端API接口文档

### 约定
* ~表示此项可选，根据实际情况决定  
* *表示此项为列表中的一项
* [|]表示此项能取的值
* 500状态码返回时附带出错信息
* GET为param，POST为JSON
* 返回一律使用JSON

## 登录部分
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
type: [requester|worker|admin]用户身份  
}  
登录成功时返回用户id与用户名

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
200，500  

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

### /requester/{id}[PUT]
* 更新一位发起者的基本信息
* 请求格式  
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
* 响应格式  
200，500   

### /requester/check[POST]
* 审核一位发起者的信息
* 请求格式  
{  
id: 发起者的id,  
isChecked: [true | false]是否审核通过  
}  
* 响应格式  
200，500

### /requester[GET]
* 获取符合条件的requester对象  
* 请求格式  
state: [all | checked | unchecked][所有 | 通过审核 | 未通过审核]  
* 响应格式  
{  
*{  
id: 用户标识,  
name: 用户名,  
brith: [yyyy-MM-dd]出生日期,  
sex: [ 0 | 1 ][ 男 | 女 ]性别,  
email: 邮箱,  
occupation: 职务,  
company: 所属公司,  
state: 用户状态  
}  
}

### /requester/{id}/setKey[POST]
* 修改发起者密码
* 请求格式  
{  
key: 密码,  
}  
* 响应格式  
200，500  

### /requester/statics[GET]
* 获取与发起者相关的统计数据
* 响应格式  
{  
//TODO 待定  
}  