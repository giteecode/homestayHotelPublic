package com.homestay.homestay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.homestay.entity.SysMenu;
import com.homestay.homestay.entity.SysRoleMenu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-02 16:45:21
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {
    /**
     * 新增或修改菜单权限时对该表进行数据修改
     * @param MenuHasRoleIds
     * @param menuId
     * @return
     */
    int addUpdateRoleByMenuId(List MenuHasRoleIds,Long menuId);

    /**
     * 根据roleId，menus新增或者插入
     * @param roleId
     * @param menus
     * @return
     */
    int addUpdateRoleByRoleId(Long roleId, List<SysMenu> menus);

    /**
     * 根据权限角色id删除其授权菜单列表
     * @param roleId
     * @return
     */
    int deleteByRoleId(Long roleId);
}
