package com.homestay.homestay.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.homestay.homestay.common.Res;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 张喜龙
 * @date: 2022/10/12 17:22
 * @version: 1.0
 * @Description:
 */
@RestController
@RequestMapping("/sa/")
public class SaTokenTest {
    @RequestMapping("doLogin")
    public SaResult doLogin(String username, String password,String validCode) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        System.out.println(username+":"+password);
        if("zhang".equals(username) && "123456".equals(password)) {
            StpUtil.login(10001);
            return SaResult.ok("成功");
        }
        return SaResult.error("错误");
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/sa/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    // 查询 Token 信息  ---- http://localhost:8081/acc/tokenInfo
    @RequestMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    // 测试注销  ---- http://localhost:8081/acc/logout
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

    @SaCheckRole("user")
    @RequestMapping("/permission")
    public Res<?> permission() {

        return Res.success("权限角色校验：user");
    }
    @SaCheckPermission("admin.add")
    @RequestMapping("/add")
    public Res<?> add(Exception e) {
        if (e instanceof NotPermissionException){
            return Res.error("-1","暂无权限哦");
        }
        return Res.success("权限列表校验admin.add");
    }

    @GetMapping("/jianquanApi")
    public Res<?> jianquanApi() {
       if (StpUtil.hasRole("super-admin")){
           return Res.success("super-admin权限");
       }else if (StpUtil.hasRole("admin")){
           return Res.success("admin权限");
       }else {
           return Res.success("没有任何的权限");
       }
    }
}
