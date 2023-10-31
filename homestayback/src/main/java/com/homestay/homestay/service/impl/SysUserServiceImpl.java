package com.homestay.homestay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.homestay.entity.SysMenu;
import com.homestay.homestay.entity.SysUser;
import com.homestay.homestay.mapper.SysUserMapper;
import com.homestay.homestay.service.SysMenuService;
import com.homestay.homestay.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-02 16:33:10
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    public List<SysMenu> getUserTreeMenu(Integer roleId) {
        // 根据用户id获取所有的角色信息
//        List<SysRole> roleList = sysRoleService.list(new QueryWrapper<SysRole>().inSql("id", "SELECT role_id FROM sys_user_role WHERE user_id=" + currentUser.getId()));
        // 遍历所有的角色，获取所有菜单权限 而且不重复
//        Set<SysMenu> menuSet = new HashSet<>();
//        for (SysRole sysRole : roleList) {
//            List<SysMenu> sysMenuList = sysMenuService.list(new QueryWrapper<SysMenu>().inSql("id", "SELECT menu_id FROM sys_role_menu WHERE role_id=" + sysRole.getId()));
//            for (SysMenu sysMenu : sysMenuList) {
//                menuSet.add(sysMenu);
//            }
//        }
//
//        currentUser.setRoles(roleList.stream().map(SysRole::getName).collect(Collectors.joining(",")));
//        List<SysMenu> sysMenuList = new ArrayList<>(menuSet);

//        用set防止重复
        Set<SysMenu> menuSet = new HashSet<>();
        //子查询，获取sysMenu
        List<SysMenu> sysMenuList = sysMenuService.list(new QueryWrapper<SysMenu>().inSql("menu_id", "SELECT menu_id FROM sys_role_menu WHERE role_id=" + roleId));
        for (SysMenu sysMenu : sysMenuList) {
//            System.out.println(sysMenu.getMenuName());
            //只添加启用状态的菜单
            if (sysMenu.getStatus() == 0){
                menuSet.add(sysMenu);
            }else {
                continue;
            }
        }
        sysMenuList = new ArrayList<>(menuSet);
        // 排序
        sysMenuList.sort(Comparator.comparing(SysMenu::getOrderNum));
        // 转菜单树
        List<SysMenu> menuList = sysMenuService.buildTreeMenu(sysMenuList);
        return menuList;
    }

    @Override
    public Map<String, Object> doLogin(SysUser user) {
//        更新最后登录时间
        user.setLoginDate(new Date());
        updateById(user);
//        sa-token认证、鉴权
        StpUtil.login(user.getId());
        StpUtil.getPermissionList(user.getId());
        StpUtil.getRoleList(user.getId());
        Map<String, Object> resultMap = new HashMap<>();
        SysUser resUser = user;
        resUser.setPassword(SecureUtil.md5(resUser.getPassword())); //返回的密码进行加密
        resultMap.put("user", resUser);
//        获取用户对应的菜单树
        List<SysMenu> menuList = getUserTreeMenu(user.getRole());
        resultMap.put("menuList", menuList);
        resultMap.put("tokenInfo", StpUtil.getTokenInfo());
        return resultMap;
    }

    @Override
    public boolean statusHandle(SysUser sysUser) {
        if (sysUser.getStatus() == 0){
            sysUser.setStatus(1);
        }else {
            sysUser.setStatus(0);
        }
        int i = sysUserMapper.updateById(sysUser);
        if (i > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean checkUserName(String username) {
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.<SysUser>lambdaQuery();
        wrapper.eq(SysUser::getUsername,username);
        Long count = sysUserMapper.selectCount(wrapper);
        if (count != 0){
            return false;
        }else {
            return true;
        }

    }

    @Override
    public boolean addOneSysUser(SysUser sysUser) {
        // 先检查用户名是否重复
        boolean b = checkUserName(sysUser.getUsername());
        if (!b){
            return false;
        }

        if (sysUser.getPassword() == null) {
            sysUser.setPassword("123456");
        }

        sysUser.setCreateTime(new Date());
        int i = sysUserMapper.insert(sysUser);
        if (i > 0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取当前登录的用户信息
     */
    @Override
    public SysUser getUserInfo() {
        // 根据token取出当前用户id
        Object getLoginId = StpUtil.getLoginIdDefaultNull();
        if (getLoginId != null){
            Integer loginId = Convert.toInt(getLoginId);
            SysUser user = this.getById(loginId);
//            user.setPassword(SecureUtil.md5(user.getPassword()));
            return user;
        }else {
            return null;
        }
    }

    /**
     * 更新个人信息
     *
     * @param sysUser
     * @return
     */
    @Override
    public boolean updateSysUserInfo(SysUser sysUser) {
        // 根据token取出当前用户id
        Object getLoginId = StpUtil.getLoginIdDefaultNull();
        if (getLoginId != null){
            Integer loginId = Convert.toInt(getLoginId);
            sysUser.setId(loginId);
            sysUser.setUpdateTime(new Date());
            boolean b = this.updateById(sysUser);
            return b;
        }else {
            return false;
        }
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return 分页获取管理员列表
     */
    @Override
    public Page<SysUser> getAdminPage(Integer pageNum, Integer pageSize ,String search) {
        LambdaQueryWrapper<SysUser> wapper = Wrappers.<SysUser>lambdaQuery();
//        当serch不是空的时候再进行模糊查询,否则全部查询
        if (StrUtil.isNotBlank(search)) {
            wapper.like(SysUser::getUsername, search);
        }
        wapper.eq(SysUser::getRole,1);
        Page<SysUser> sysUserPage = sysUserMapper.selectPage(new Page<>(pageNum, pageSize), wapper);
        return sysUserPage;
    }

    /**
     * @param pageNum
     * @param pageSize
     * @return 分页获取员工列表
     */
    @Override
    public Page<SysUser> getStaffPage(Integer pageNum, Integer pageSize ,String search) {
        LambdaQueryWrapper<SysUser> wapper = Wrappers.<SysUser>lambdaQuery();
//        当serch不是空的时候再进行模糊查询,否则全部查询
        if (StrUtil.isNotBlank(search)) {
            wapper.like(SysUser::getUsername, search);
        }
        wapper.eq(SysUser::getRole,2);
        Page<SysUser> sysUserPage = sysUserMapper.selectPage(new Page<>(pageNum, pageSize), wapper);
        return sysUserPage;
    }


}
