package com.homestay.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.homestay.entity.SysMenu;
import com.homestay.homestay.entity.SysRole;
import com.homestay.homestay.entity.SysUser;
import com.homestay.homestay.mapper.SysRoleMapper;
import com.homestay.homestay.mapper.SysUserMapper;
import com.homestay.homestay.service.SysMenuService;
import com.homestay.homestay.service.SysRoleMenuService;
import com.homestay.homestay.service.SysRoleService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-02 21:23:23
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    @Lazy
    private SysRoleMenuService sysRoleMenuService;

    //    @Resource
//    private SysRoleMapper sysRoleMapper;
    @Override
    public List<SysRole> findRoleAndMenus() {
        List<SysRole> sysRoles = this.list();
        if (!sysRoles.isEmpty()) {
            for (SysRole sysRole : sysRoles) {
//                sysRole.setRoleMenus(sysMenuService.listSysMenuByRoleId(sysRole.getId()));
                List<SysMenu> sysMenus = sysMenuService.listSysMenuByRoleId(sysRole.getId());
                if (!sysMenus.isEmpty()){
                    //角色含有的菜单ids
                    List<Object> sysMenuIds = new ArrayList<>();
                    for (SysMenu sysMenu : sysMenus) {
                        if (sysMenu.getParentId() == 0){
                            // 这里因为前端Tree接收到父id会默认全选，所以不返回给前端parentId为0的
                            continue;
                        }
                        sysMenuIds.add(sysMenu.getMenuId());
                    }
                    sysRole.setRoleHasMenuIds(sysMenuIds);
                }
            }
        }

        return sysRoles;
    }

    @Override
    public boolean deleteOneRole(Long roleId) {
        List<SysUser> sysUsers = sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getRole, roleId));
        if (!sysUsers.isEmpty()) {
            System.out.println("该权限下有用户，需先清空该权限下的用户");
            return false;
        }
        // 删除角色
        boolean b = this.removeById(roleId);
        // 删除角色权限菜单
        sysRoleMenuService.deleteByRoleId(roleId);
        return b;
    }

    @Override
    public boolean addOneRole(SysRole sysRole) {
        // 新增一个role
        sysRole.setCreateTime(new Date());
        boolean save = this.save(sysRole);
        // 添加其授权菜单
        if (sysRole.getRoleMenus() != null && !sysRole.getRoleMenus().isEmpty()) {
            sysRoleMenuService.addUpdateRoleByRoleId(sysRole.getId(), sysRole.getRoleMenus());
        }
        return save;
    }

    @Override
    public boolean updateOneRole(SysRole sysRole) {
        sysRole.setUpdateTime(new Date());
        // 更新一个role
        boolean b = this.updateById(sysRole);
        // 更新其授权菜单
        if (sysRole.getRoleMenus() != null && !sysRole.getRoleMenus().isEmpty()) {
            sysRoleMenuService.addUpdateRoleByRoleId(sysRole.getId(), sysRole.getRoleMenus());
        }
        return b;
    }

    @Override
    public boolean statusHandle(SysRole sysRole) {
        if (sysRole.getStatus() == 0) {
            sysRole.setStatus(1L);
        } else {
            sysRole.setStatus(0L);
        }
        boolean b = this.updateById(sysRole);
        return b;
    }

    /**
     * 根据roleid查询该权限是否启用
     *
     * @param roleId
     * @return
     */
    @Override
    public boolean checkStatus(Integer roleId) {
        LambdaQueryWrapper<SysRole> wrapper = Wrappers.<SysRole>lambdaQuery().eq(SysRole::getId, roleId);
//        System.out.println(roleId);
        SysRole sysRole = this.getOne(wrapper);
        if (sysRole.getStatus() == 0){
            return true;
        }else {
            return false;
        }
    }
}
