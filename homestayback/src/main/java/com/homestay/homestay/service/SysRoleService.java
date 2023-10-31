package com.homestay.homestay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.homestay.entity.SysRole;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-02 21:23:23
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     *
     * @return 权限角色及其可访问的菜单列表
     */
    List<SysRole> findRoleAndMenus();

    /**
     * 删除一个权限角色
     * @param roleId
     * @return
     */
    boolean deleteOneRole(Long roleId);


    boolean addOneRole(SysRole sysRole);

    boolean updateOneRole(SysRole sysRole);

    /**
     * 启用/禁用状态转换
     * @return
     */
    boolean statusHandle(SysRole sysRole);

    /**
     * 根据roleid查询该权限是否启用
     * @param roleId
     * @return
     */
    boolean checkStatus(Integer roleId);
}
