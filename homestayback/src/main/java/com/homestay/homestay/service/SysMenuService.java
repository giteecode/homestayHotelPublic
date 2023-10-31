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
 * @since 2022-11-02 16:47:47
 */
public interface SysMenuService extends IService<SysMenu> {
    /**
     *
     * @param
     * @return 传入查询出的菜单列表，构建菜单树
     */
    List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList);

    /**
     * 获取所有菜单，用于菜单管理
     * @return 所有菜单并构建菜单树。
     */
    List<SysMenu> getAllTreeMenu();

    /**
     * 新增菜单；对order_num进行处理,设置排序
     * @param sysMenu
     * @return new SysMenu
     */
    int addOneSysMenu(SysMenu sysMenu);

    /**
     * 删除菜单时的处理
     * @param id
     * @return
     */
    boolean deleteOneSysMenu(Long id);

    /**
     * 更新菜单信息
     * @param sysMenu
     * @return
     */
    boolean updateOneSysMenu(SysMenu sysMenu);

    /**
     * 启用/禁用状态转换
     * @param menuID
     * @param status
     * @return
     */
    boolean statusHandle(SysMenu sysMenu);

    /**
     *
     * @param menuId
     * @return 返回拥有当前菜单访问权限的角色列表
     */
    List<SysRoleMenu> getSysRoleMenus(Long menuId);

    /**
     *
     * @return 父级菜单，用于选择子菜单的父级
     */
    List<SysMenu> getParents();

    /**
     * 根据role_id返回菜单列表
     * @param roleId
     * @return
     */
    List<SysMenu> listSysMenuByRoleId(Long roleId);
}
