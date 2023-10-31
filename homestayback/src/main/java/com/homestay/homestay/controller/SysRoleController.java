package com.homestay.homestay.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.homestay.homestay.common.Res;
import com.homestay.homestay.entity.SysRole;
import com.homestay.homestay.service.SysRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-02 21:23:23
 */

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;

    /**
     * @return 获取权限角色列表
     */
    @GetMapping
    public Res<?> findRoleList() {

        LambdaQueryWrapper<SysRole> wrapper = Wrappers.<SysRole>lambdaQuery().eq(SysRole::getStatus, 0);
        List<SysRole> list = sysRoleService.list(wrapper);
        return Res.success(list);

    }

    /**
     * @return 获取权限角色列表和该权限的菜单
     */
    @GetMapping("/roleAndMenus")
    public Res<?> findRoleAndMenus() {
//        if (StpUtil.hasRole("admin")) { //是否为admin权限
//            List<SysRole> list = sysRoleService.findRoleAndMenus();
//            return Res.success(list);
//        } else {
//            return Res.error("-1", "无权限");
//        }
        List<SysRole> list = sysRoleService.findRoleAndMenus();
        return Res.success(list);
    }

    /**
     * 新增角色
     */
    @PostMapping
    public Res<?> addMenu(@RequestBody SysRole sysRole) {
        boolean b = sysRoleService.addOneRole(sysRole);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "添加失败");
        }
//        System.out.println(sysRole);
//        return Res.success(sysRole);
    }

    /**
     * 修改角色
     */
    @PutMapping
    public Res<String> updateMenu(@RequestBody SysRole sysRole) {

        boolean b = sysRoleService.updateOneRole(sysRole);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "更新失败");
        }
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Res<?> deleteMenu(@PathVariable Long id) {
        boolean b = sysRoleService.deleteOneRole(id);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "删除失败,删除角色之前需要先清空其下用户");
        }
    }

    /**
     * 状态切换控制
     */
    @PutMapping("/status")
    public Res<String> statusHandle(@RequestBody SysRole sysRole) {
        boolean b = sysRoleService.statusHandle(sysRole);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "切换失败");
        }
    }
}

