package com.homestay.homestay.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.common.Res;
import com.homestay.homestay.config.aop.SysLog;
import com.homestay.homestay.entity.HUser;
import com.homestay.homestay.service.HUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;


/**
 * <p>
 * 前端控制器，前台用户
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-03 14:51:43
 */

@RestController
@RequestMapping("/h/user")
public class HUserController {
    @Resource
    private HUserService hUserService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @SysLog("hUser login")
    public Res<?> login(@RequestBody HUser hUser) {

        HUser user = hUserService.getOne(Wrappers.<HUser>lambdaQuery().eq(HUser::getUsername, hUser.getUsername()).eq(HUser::getPassword, hUser.getPassword()));
        if (user == null) {
            return Res.error("-1", "用户名或密码错误");
        }
        if (user.getStatus() == 1) {
            return Res.error("-1", "账户被封禁，请联系管理员");
        }

        Map<String, Object> resultMap = hUserService.doLogin(user);
        return Res.success(resultMap);
    }

    /**
     * 状态切换控制
     */
    @PutMapping("/status")
    public Res<String> statusHandle(@RequestBody HUser hUser) {
        boolean b = hUserService.statusHandle(hUser);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "切换失败");
        }
    }

    /**
     * 检查用户名是否重复
     *
     * @param username
     * @return
     */
    @GetMapping("/checkUserName")
    public Res<String> checkUserName(@RequestParam String username) {
        boolean b = hUserService.checkUserName(username);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "用户名重复");
        }
    }

    /**
     * 获取个人信息
     */
    @GetMapping("/getUserInfo")
    public Res<?> getUserInfo() {
        HUser userInfo = hUserService.getUserInfo();
        if (userInfo != null) {
            return Res.success(userInfo);
        } else {
            return Res.error("-1", "未登录");
        }
    }

    /**
     * 新增用户
     */
    @PostMapping
    public Res<?> addHUser(@RequestBody HUser hUser) {
        boolean checkUserName = hUserService.checkUserName(hUser.getUsername());
        if (checkUserName == false) {
            return Res.error("-1", "用户名重复");
        }
        boolean checkUserPhone = hUserService.checkUserPhone(hUser.getPhone());
        if (checkUserPhone == false){
            return Res.error("-1", "该手机号已注册！！");
        }
        boolean b = hUserService.addOneHUser(hUser);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "添加失败");
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Res<?> deleteUser(@PathVariable Long id) {
        boolean b = hUserService.removeById(id);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "删除失败");
        }
    }

    /**
     * 修改操作
     */
    @PutMapping
    public Res<String> update(@RequestBody HUser hUser) {
        hUser.setUpdateTime(new Date());
        boolean b = hUserService.updateById(hUser);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "更新失败");
        }
    }

    /**
     * 更新个人信息
     */
    @PutMapping("/updateInfo")
    public Res<String> updateInfo(@RequestBody HUser hUser) {
        boolean b = hUserService.updateHUserInfo(hUser);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "更新失败");
        }
    }

    /**
     * @param pageNum
     * @param pageSize
     * @param search
     * @return 前台用户列表
     */
    @GetMapping()
    public Res<?> getAdmin(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "") String search) {
        if (pageSize > 1000) {
            return Res.error("-1", "太大了");
        }
        Page<HUser> page = hUserService.getHUserPage(pageNum, pageSize, search);
        return Res.success(page);
    }

    /**
     * 获取注册数据
     */
    @GetMapping("/registerData")
    public Res<?> registerData() {
        Map<String, Object> resultMap = hUserService.getRegisterData();
        return Res.success(resultMap);
    }

    /**
     * 检查注册手机号是否重复
     */
    @GetMapping("/checkUserPhone")
    public Res<?> checkUserPhone(@RequestParam String phone) {
        boolean b = hUserService.checkUserPhone(phone);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "更新失败");
        }
    }

    /**
     * 重置密码
     * @param phone
     * @param username
     * @return
     */
    @PostMapping("/resetPassword")
    public Res<?> resetPassword(@RequestParam String phone,@RequestParam String username) {

        HUser user = hUserService.resetPassword(phone,username);
        if (user != null) {
            return Res.success(user.getPassword());
        } else {
            return Res.error("-1", "重置失败，手机号和用户名不匹配");
        }
    }
}

