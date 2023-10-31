## 基于Java+Springboot+Vue的民宿酒店预订管理系统(源码+数据库)107

## 一、系统介绍
本系统前后端分离

本系统分为用户、前台、管理员三种角色(角色菜单可以自行分配)

- 前台：
登录、注册、民宿浏览、民宿评价、民宿酒店下单预订、密码修改、个人信息修改。

- 管理后台：
房间统计，用户管理，个人信息修改，角色管理，菜单管理，日志管理，文件管理，民宿预订入住管理，续住退房管理，订单管理、财务统计。

## 二、所用技术
后端技术栈：
- Springboot
- SpringMvc
- mybatisplus
- mysql

前端技术栈：
- Vue3
- elementui
- vue-router
- axios

## 三、环境介绍
基础环境 :IDEA/eclipse, JDK 1.8, Mysql5.7及以上, Node.js(14.21), Maven3.6, Vscode

所有项目以及源代码本人均调试运行无问题 可支持远程调试运行

## 四、页面截图
### 1、前台页面
![contents](./picture/picture1.png)
![contents](./picture/picture2.png)
![contents](./picture/picture3.png)
![contents](./picture/picture4.png)
![contents](./picture/picture5.png)
![contents](./picture/picture6.png)
![contents](./picture/picture7.png)

### 2、管理员页面
![contents](./picture/picture8.png)
![contents](./picture/picture9.png)
![contents](./picture/picture10.png)
![contents](./picture/picture11.png)
![contents](./picture/picture12.png)
![contents](./picture/picture13.png)
![contents](./picture/picture14.png)
![contents](./picture/picture15.png)
![contents](./picture/picture16.png)
![contents](./picture/picture17.png)
![contents](./picture/picture18.png)
![contents](./picture/picture19.png)
![contents](./picture/picture20.png)
![contents](./picture/picture21.png)
![contents](./picture/picture22.png)
![contents](./picture/picture23.png)
![contents](./picture/picture24.png)
![contents](./picture/picture25.png)
![contents](./picture/picture26.png)
![contents](./picture/picture27.png)
![contents](./picture/picture28.png)

## 五、浏览地址
- 后台访问路径：http://localhost:8090/index
  user/123456

- 后台访问路径：http://localhost:8090/dashboard
  admin/admin

## 六、安装教程

1. 使用Navicat或者其它工具，在mysql中创建对应名称的数据库，并执行项目的sql

2. 使用IDEA/Eclipse导入homestayback项目，导入时，若为maven项目请选择maven; 等待依赖下载完成

3. 修改resources目录下面application.properties里面的数据库配置

4. com/homestay/homestay/HomestayApplication.java启动后端项目

5. vscode或idea打开homestayfront项目

6. 在编译器中打开terminal，执行npm install 依赖下载完成后执行 npm run serve,执行成功后会显示前台访问地址

