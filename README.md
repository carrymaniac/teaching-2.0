## README
这是开发分支-JWT分支
主要是对原本的Token进行了更新，加入了标准的JWT Token

##区别

原本的Token：
- 使用简单的MD5算法生成一串唯一的串
- 登录时 将串以（key：串 value：用户ID）放在Redis中 同时将串放在Cookie中
- 校验时，从Cookie中拿到Token 使用Token去Redis中获取用户ID 其中有一步失败表示用户校验失败，跳到登录页进行登录
缺陷：无法完成单点登录（Redis不同Token对应同一个用户ID）

新JWT Token：
- 使用标准的JWT规则生成Token
- 登录时 将Token生成并返回给前端 前端在之后的请求头中带上 同时在Redis中存放
- 校验时 从Request头中取出Token 解析Token获取用户ID 生成Redis中的Key去取出Token 两个Token比较，若相同则注入用户信息
优点：完成了单点登录的情况，避免使用了Cookie 



## 目前问题
- 暂时没有解决签名用的密匙生成问题
- 未测试