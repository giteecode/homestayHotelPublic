package com.homestay.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.homestay.entity.SysMenu;
import com.homestay.homestay.entity.SysRoleMenu;
import com.homestay.homestay.mapper.SysRoleMenuMapper;
import com.homestay.homestay.service.SysRoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-02 16:45:21
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public int addUpdateRoleByMenuId(List MenuHasRoleIds, Long menuId) {
        int res = 0;
        LambdaQueryWrapper<SysRoleMenu> wrapper = Wrappers.<SysRoleMenu>lambdaQuery();
        //新增菜单时选择的对应角色权限
        if(!MenuHasRoleIds.isEmpty()){
            //判断新增or修改操作
            wrapper.eq(SysRoleMenu::getMenuId,menuId);
            List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(wrapper);
            if (sysRoleMenus.isEmpty()){
                //为空，新增操作
                //取出前端传回的roleIds,在sys_role_menu表中插入数据
                for (Object roleId : MenuHasRoleIds) {
                    SysRoleMenu sysRoleMenu = new SysRoleMenu();
                    sysRoleMenu.setRoleId(Long.valueOf(String.valueOf(roleId)));
                    sysRoleMenu.setMenuId(menuId);
//                System.out.println("addOneSysMenu中新增的MenuHasRoleIds："+MenuHasRoleIds);
                    res += sysRoleMenuMapper.insert(sysRoleMenu);
                }
            }else {
                //不为空，更新操作,先根据menuid删除，再重新添加
//                System.out.println("更新role_menu");
                wrapper.eq(SysRoleMenu::getMenuId,menuId);
                sysRoleMenuMapper.delete(wrapper);
                addUpdateRoleByMenuId(MenuHasRoleIds,menuId);
            }

        }
        return res;
    }

    @Override
    public int addUpdateRoleByRoleId(Long roleId, List<SysMenu> menus) {
        int res = 0;
        LambdaQueryWrapper<SysRoleMenu> wrapper = Wrappers.<SysRoleMenu>lambdaQuery();
        if (!menus.isEmpty()){
            //判断新增or修改操作
            List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(wrapper.eq(SysRoleMenu::getRoleId, roleId));
            if (sysRoleMenus.isEmpty()){
                // 查询不到则为新增操作
                for (SysMenu menu : menus) {
                    //如果选中的是子菜单，其应有父级菜单,这里帮忙插入一下
                    if (menu.getParentId() != 0){
                        boolean b = this.checkRoleMenu(menu.getParentId(),roleId);
                        if (b==false){//如果没有父级id的记录，那么这里插入一下
                            SysRoleMenu sysRoleMenu = new SysRoleMenu();
                            sysRoleMenu.setRoleId(roleId);
                            sysRoleMenu.setMenuId(menu.getParentId());
                            res += sysRoleMenuMapper.insert(sysRoleMenu);

                        }
                    }
                    SysRoleMenu sysRoleMenu = new SysRoleMenu();
                    sysRoleMenu.setRoleId(roleId);
                    sysRoleMenu.setMenuId(menu.getMenuId());
                    res += sysRoleMenuMapper.insert(sysRoleMenu);
                }
            }else {
                // 不为空，视为更新操作
                sysRoleMenuMapper.delete(wrapper.eq(SysRoleMenu::getRoleId,roleId));
                addUpdateRoleByRoleId(roleId,menus);
            }
        }
        return res;
    }

    @Override
    public int deleteByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = Wrappers.<SysRoleMenu>lambdaQuery();
        wrapper.eq(SysRoleMenu::getRoleId,roleId);
        int delete = sysRoleMenuMapper.delete(wrapper);
        return delete;
    }

    /**
     * 检查rolemenu是否已存在
     * @param parentId
     * @param roleId
     * @return true：已存在，false：不存在
     */
    public boolean checkRoleMenu(Long parentId,Long roleId){
        LambdaQueryWrapper<SysRoleMenu> wrapper = Wrappers.<SysRoleMenu>lambdaQuery();
        wrapper.eq(SysRoleMenu::getMenuId,parentId);
        List<SysRoleMenu> list = this.list(wrapper);
        if (list.isEmpty()){
            return false;
        }else {
            for (SysRoleMenu sysRoleMenu : list) {
                if (sysRoleMenu.getRoleId() == roleId){
                    return true;
                }else {
                    continue;
                }
            }
        }
       return false;
    }
}
