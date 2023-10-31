package com.homestay.homestay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.homestay.entity.SysMenu;
import com.homestay.homestay.entity.SysRole;
import com.homestay.homestay.entity.SysRoleMenu;
import com.homestay.homestay.mapper.SysMenuMapper;
import com.homestay.homestay.mapper.SysRoleMapper;
import com.homestay.homestay.mapper.SysRoleMenuMapper;
import com.homestay.homestay.service.SysMenuService;
import com.homestay.homestay.service.SysRoleMenuService;
import com.homestay.homestay.service.SysRoleService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-02 16:47:47
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
//    @Resource
//    private SysMenuService sysMenuService;
    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    @Lazy
    private SysRoleService sysRoleService;
    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Resource
    private SysRoleMenuService sysRoleMenuService;
    @Override
    public List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList) {
//        先排序
        sysMenuList.sort(Comparator.comparing(SysMenu::getOrderNum));
        List<SysMenu> resultMenuList=new ArrayList<>();
        for(SysMenu sysMenu:sysMenuList){
            // 寻找子节点
            for(SysMenu e:sysMenuList){
                if(e.getParentId()==sysMenu.getMenuId()){
                    sysMenu.getChildren().add(e);
                }
            }

            if(sysMenu.getParentId()==0L){
                resultMenuList.add(sysMenu);
            }
        }

        return resultMenuList;
    }

    @Override
    public List<SysMenu> getAllTreeMenu() {
        LambdaQueryWrapper<SysMenu> wapper = Wrappers.<SysMenu>lambdaQuery();
        Set<SysMenu> menuSet = new HashSet<>();
        List<SysMenu> sysMenuList = sysMenuMapper.selectList(wapper);

        for (SysMenu sysMenu : sysMenuList) {
            // 根据当前菜单ID查询哪些角色含有访问权限
            List<SysRoleMenu> sysRoleMenus = getSysRoleMenus(sysMenu.getMenuId());
            //权限角色详细信息,(由于使用Ids比较便捷，故此处信息丢弃)
//            List<SysRole> sysRoles = new ArrayList<>();
            //权限角色ids
            Set<Object> roleIdsSet = new HashSet<>();

            if (!sysRoleMenus.isEmpty()){
                for (SysRoleMenu sysRoleMenu : sysRoleMenus) {
                    roleIdsSet.add(sysRoleMenu.getRoleId());
//                    System.out.println("当前菜单："+sysMenu.getMenuName()+",其权限id："+sysRoleMenu.getRoleId());
//                    SysRole sysRole = sysRoleMapper.selectById(sysRoleMenu.getRoleId());
//                    if (sysRole != null){
////                        System.out.println("我是sysRole："+sysRole.getName());
//                        sysRoles.add(sysRole);
//                    }
                }
            }
//            sysMenu.setMenuHasRoles(sysRoles);
            List<Object> sysRolesIds = new ArrayList<>(roleIdsSet);
            sysMenu.setMenuHasRoleIds(sysRolesIds);
            //获取权限角色ids
//            LambdaQueryWrapper<SysRoleMenu> wapper2 = Wrappers.<SysRoleMenu>lambdaQuery();
//            wapper2.eq(SysRoleMenu::getMenuId,sysMenu.getMenuId());
//            List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(wapper2);
//            if (!sysRoleMenus.isEmpty()){
//                sysMenu.setMenuHasRoleIds();
//            }
//            wapper2.inSql(SysRoleMenu::getRoleId,"select role_id from sys_role_menu where menu_id="+sysMenu.getMenuId());
//            List<Object> objects = sysRoleMenuMapper.selectObjs(wapper2);
//            sysMenu.setMenuHasRoleIds(objects);

            menuSet.add(sysMenu);
        }
        sysMenuList = new ArrayList<>(menuSet);
        // 排序
        sysMenuList.sort(Comparator.comparing(SysMenu::getOrderNum));
        // 转菜单树
        List<SysMenu> menuList = buildTreeMenu(sysMenuList);
        return menuList;
    }

    @Override
    public int addOneSysMenu(SysMenu sysMenu) {
        LambdaQueryWrapper<SysMenu> wapper = Wrappers.<SysMenu>lambdaQuery();
        if (sysMenu.getParentId() == 0 || "M".equals(sysMenu.getMenuType())){
            //目录
//            System.out.println("我进来了，我是目录！！！！");
            sysMenu.setParentId(0L);
            sysMenu.setMenuType("M");
        }
//        else {
//            //非目录,
//            System.out.println("我进来了,我不是目录");
//            sysMenu.setMenuType("C");
//        }
        //根据parent_id查询条数，设置菜单默认排序为当前条数+1
        wapper.eq(SysMenu::getParentId,sysMenu.getParentId());
        Long count = sysMenuMapper.selectCount(wapper);
        sysMenu.setOrderNum(Math.toIntExact(count + 1));


        sysMenu.setCreateTime(new Date());
        sysMenu.setUpdateTime(new Date());
        int insert = sysMenuMapper.insert(sysMenu);
//        System.out.println("我是插入后的menuId："+sysMenu.getMenuId());
        //加入权限列表
        sysRoleMenuService.addUpdateRoleByMenuId(sysMenu.getMenuHasRoleIds(),sysMenu.getMenuId());
        return insert;
    }

    @Override
    public boolean deleteOneSysMenu(Long id) {
        SysMenu sysMenu = sysMenuMapper.selectById(id);
        //如果parent_id为0，即视为菜单目录，需先删除完其子菜单才可进行删除菜单目录操作
        if (sysMenu.getParentId() == 0){
            LambdaQueryWrapper<SysMenu> wapper = Wrappers.<SysMenu>lambdaQuery();
            wapper.eq(SysMenu::getParentId,id); //查询有多少个子菜单的parent_id是这个需要删除的id
            List<SysMenu> sysMenus = sysMenuMapper.selectList(wapper);
//            System.out.println("sysMenus.isEmpty():"+sysMenus.isEmpty());
//            System.out.println("sysMenus.size():"+sysMenus.size());
            if (sysMenus.isEmpty()){
//                System.out.println("没有子菜单，可以删除");
                sysMenuMapper.deleteById(id);
                sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getMenuId,id));
                return true;
            }else {
//                System.out.println("有子菜单，需要先删除完子菜单再删除菜单目录");
                return false;
            }
        }
        else {
//            System.out.println("我不是目录菜单，可以删除");
            sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getMenuId,id));
            sysMenuMapper.deleteById(id);
            return true;
        }
    }

    @Override
    public boolean updateOneSysMenu(SysMenu sysMenu) {
        int i = sysMenuMapper.updateById(sysMenu);
        //更新权限
        sysRoleMenuService.addUpdateRoleByMenuId(sysMenu.getMenuHasRoleIds(), sysMenu.getMenuId());
        if (i>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean statusHandle(SysMenu sysMenu) {

        if (sysMenu.getStatus() == 0){
            sysMenu.setStatus(1L);
        }else {
            sysMenu.setStatus(0L);
        }
        int i = sysMenuMapper.updateById(sysMenu);
        if (i > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<SysRoleMenu> getSysRoleMenus(Long menuId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = Wrappers.<SysRoleMenu>lambdaQuery();
        wrapper.eq(SysRoleMenu::getMenuId,menuId);
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(wrapper);
//        List<SysRoleMenu> sysRoleMenus = Collections.synchronizedList(sysRoleMenuMapper.selectList(wrapper));
        // 拿到菜单权限id，去判断sysRole权限id是否为0，即是否为启用状态，非启用状态则不显示;使用list.remove会报错，采用下面这种方式遍历
        Iterator<SysRoleMenu> iterator = sysRoleMenus.iterator();
        while (iterator.hasNext()){
            SysRoleMenu sysRoleMenu = iterator.next();
            SysRole sysRole = sysRoleService.getById(sysRoleMenu.getRoleId());
            if (sysRole.getStatus() != 0){
//                sysRoleMenus.remove(sysRoleMenu);
                iterator.remove();
            }else {
                continue;
            }
        }

        return sysRoleMenus;
    }

    @Override
    public List<SysMenu> getParents() {
        LambdaQueryWrapper<SysMenu> wapper = Wrappers.<SysMenu>lambdaQuery();
        wapper.eq(SysMenu::getParentId,0);
        List<SysMenu> parents = sysMenuMapper.selectList(wapper);
        SysMenu gen = new SysMenu();
        gen.setMenuId(0L);
        gen.setParentId(0L);
        gen.setMenuName("根目录");
        parents.add(gen);
        parents.sort(Comparator.comparing(SysMenu::getMenuId));
        return parents;
    }
    @Override
    public List<SysMenu> listSysMenuByRoleId(Long roleId) {
        List<SysMenu> sysMenus = new ArrayList<>();
        // 先根据role_id获取menu_id
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuWrapper = Wrappers.<SysRoleMenu>lambdaQuery();
        sysRoleMenuWrapper.eq(SysRoleMenu::getRoleId,roleId);
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(sysRoleMenuWrapper);
        if (!sysRoleMenus.isEmpty()){

            for (SysRoleMenu sysRoleMenu : sysRoleMenus) {
                SysMenu byId = sysMenuMapper.selectById(sysRoleMenu.getMenuId());

                sysMenus.add(byId);
            }
        }

//        List<SysMenu> treeMenu = buildTreeMenu(sysMenus);
//        for (SysMenu menu : treeMenu) {
//            System.out.println("==============================");
//            System.out.println(menu.getMenuId());
//        }
        return sysMenus;
    }
}
