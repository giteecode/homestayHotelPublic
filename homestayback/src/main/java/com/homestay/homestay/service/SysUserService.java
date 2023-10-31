package com.homestay.homestay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.homestay.entity.SysMenu;
import com.homestay.homestay.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-02 16:33:10
 */
public interface SysUserService extends IService<SysUser> {
    /**
     *
     * @param roleId
     * @return 根据权限ID获取菜单树
     */
    List<SysMenu> getUserTreeMenu(Integer roleId);

    /**
     * 登录成功的处理
     * @param sysUser
     * @return
     */
    Map<String, Object> doLogin(SysUser sysUser);


    /**
     * 启用/禁用状态转换
     * @return
     */
    boolean statusHandle(SysUser sysUser);

    /**
     * 检查用户名是否重复
     * @param username
     * @return
     */
    boolean checkUserName(String username);

    /**
     * 新增一个用户
     * @param sysUser
     * @return
     */
    boolean addOneSysUser(SysUser sysUser);

    /**
     * 获取当前登录的用户信息
     */
    SysUser getUserInfo();

    /**
     * 更新个人信息
     * @param sysUser
     * @return
     */
    boolean updateSysUserInfo(SysUser sysUser);

    /**
     *
     * @param pageNum
     * @param pageSize
     * @return 分页获取管理员列表
     */
    Page<SysUser> getAdminPage(Integer pageNum, Integer pageSize ,String search);

    /**
     *
     * @param pageNum
     * @param pageSize
     * @return 分页获取员工列表
     */
    Page<SysUser> getStaffPage(Integer pageNum, Integer pageSize ,String search);
}
