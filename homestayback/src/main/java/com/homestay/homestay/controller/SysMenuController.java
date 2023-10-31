package com.homestay.homestay.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.homestay.homestay.common.Res;
import com.homestay.homestay.entity.SysMenu;
import com.homestay.homestay.service.SysMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-02 16:47:47
 */

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {
    @Resource
    private SysMenuService sysMenuService;
    /**
     * @return 获取所有菜单树
     */
    @GetMapping
    public Res<?> findMenuList() {
        if (StpUtil.hasRole("admin")) { //是否为admin权限
            List<SysMenu> menuList = sysMenuService.getAllTreeMenu();
            return Res.success(menuList);
        } else {
            return Res.error("-1", "无权限");
        }
//
//        List<SysMenu> menuList = sysMenuService.getAllTreeMenu();
//        return Res.success(menuList);
    }

    /**
     * 新增菜单
     * */
    @PostMapping
    public Res<?> addMenu(@RequestBody SysMenu sysMenu) {
        int i = sysMenuService.addOneSysMenu(sysMenu);
        if (i >= 1){
            return Res.success();
        }else {
            return Res.error("-1","未知错误");
        }

    }

    /**
     * 修改操作
     */
    @PutMapping
    public Res<String> updateMenu(@RequestBody SysMenu sysMenu){
        sysMenu.setUpdateTime(new Date());
        boolean b = sysMenuService.updateOneSysMenu(sysMenu);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","更新失败");
        }
    }
    /**
     * 删除操作
     */
    @DeleteMapping("/{id}")
    public Res<?> deleteMenu(@PathVariable Long id) {
        boolean b = sysMenuService.deleteOneSysMenu(id);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","删除失败,删除目录菜单之前需要先删除完其子菜单");
        }
    }

    /**
     * 菜单操作测试方法，仅供测试使用
     * */
    @PostMapping("/test")
    public Res<?> menuTest(@RequestBody SysMenu sysMenu) {
        int i = sysMenuService.addOneSysMenu(sysMenu);
        if (i >= 1){
            return Res.success();
        }else {
            return Res.error("-1","未知错误");
        }

    }

    /**
     * 状态切换控制
     */
    @PutMapping("/status")
    public Res<String> statusHandle(@RequestBody SysMenu sysMenu){
        boolean b = sysMenuService.statusHandle(sysMenu);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","切换失败");
        }
    }

    /**
     * @return 获取所有目录菜单
     */
    @GetMapping("/getParents")
    public Res<?> getParents() {
        if (StpUtil.hasRole("admin")) { //是否为admin权限
            List<SysMenu> list = sysMenuService.getParents();
            return Res.success(list);
        } else {
            return Res.error("-1", "无权限");
        }
    }


}

