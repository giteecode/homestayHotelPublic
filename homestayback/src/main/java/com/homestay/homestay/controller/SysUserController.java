package com.homestay.homestay.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.common.Res;
import com.homestay.homestay.config.aop.SysLog;
import com.homestay.homestay.entity.SysUser;
import com.homestay.homestay.service.SysRoleService;
import com.homestay.homestay.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-02 16:33:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
//    @Resource
//    private SysMenuService sysMenuService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;
    /**
     * 用户登录
     */
    @PostMapping("/login")
    @SysLog("user login")
    public Res<?> admin(@RequestBody SysUser sysUser) {

        SysUser user = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, sysUser.getUsername()).eq(SysUser::getPassword, sysUser.getPassword()));
        if (user == null) {
            return Res.error("-1", "用户名或密码错误");
        }
        if (user.getStatus() == 1) {
            return Res.error("-1", "账户被封禁，请联系管理员");
        }
        if (!sysRoleService.checkStatus(user.getRole())){
            return Res.error("-1", "账号权限角色未启用，请联系管理员");
        }
        Map<String, Object> resultMap = sysUserService.doLogin(user);
//        StpUtil.login(user.getId());
//        StpUtil.getPermissionList(user.getId());
//        StpUtil.getRoleList(user.getId());
//        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("user", user);
//        List<SysMenu> menuList = sysUserService.getTreeMenu(user.getRole());
//        resultMap.put("menuList", menuList);
//        resultMap.put("tokenInfo", StpUtil.getTokenInfo());

        System.out.println("我是login角色权限校验：" + StpUtil.getRoleList());
        return Res.success(resultMap);
    }

    /**
     * @return 动态获取用户菜单
     */
//    @GetMapping
//    public Res<?> findMenuList() {
//        if (StpUtil.isLogin()) { //是否登录
//            List<SysMenu> menuList = sysUserService.getTreeMenu(1);
//            return Res.success(menuList);
//        } else {
//            return Res.error("-1", "未登录");
//        }
//    }

    /**
     * 新增用户
     * */
    @PostMapping
    public Res<?> addAdmin(@RequestBody SysUser sysUser) {
        boolean b = sysUserService.addOneSysUser(sysUser);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","添加失败");
        }
    }

    /**
     * 修改操作
     */
    @PutMapping
    public Res<String> update(@RequestBody SysUser sysUser){
        sysUser.setUpdateTime(new Date());
        boolean b = sysUserService.updateById(sysUser);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","更新失败");
        }
    }
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Res<?> deleteUser(@PathVariable Long id) {
        boolean b = sysUserService.removeById(id);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","删除失败");
        }
    }

    /**
     * 分页查询，返回所有用户数据
     */
    @GetMapping()
    public Res<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
//        System.out.println(pageNum+"========"+pageSize+"========"+search);
        if (pageSize > 1000){
            return Res.error("-1","太大了");
        }
        LambdaQueryWrapper<SysUser> wapper = Wrappers.<SysUser>lambdaQuery();
//        当serch不是空的时候再进行模糊查询,否则全部查询
        if (StrUtil.isNotBlank(search)) {
            wapper.like(SysUser::getUsername, search);
        }
        Page<SysUser> adminPage = sysUserService.page(new Page<>(pageNum, pageSize), wapper);
//        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.<User>lambdaQuery().like(User::getNickName, search));
        return Res.success(adminPage);
    }

    /**
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @return 管理员列表
     */
    @GetMapping("/getAdmin")
    public Res<?> getAdmin(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "") String search){
        if (pageSize > 1000){
            return Res.error("-1","太大了");
        }
        Page<SysUser> adminPage = sysUserService.getAdminPage(pageNum, pageSize, search);
        return Res.success(adminPage);
    }

    /**
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @return 员工列表
     */
    @GetMapping("/getStaff")
    public Res<?> getStaff(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "") String search){
        if (pageSize > 1000){
            return Res.error("-1","太大了");
        }
        Page<SysUser> staffPage = sysUserService.getStaffPage(pageNum, pageSize, search);
        return Res.success(staffPage);
    }



    /**
     * 通过ID查询用户
     */
    @GetMapping("/{id}")
    public Res<?> getUserById(@PathVariable Long id) {
        return Res.success(sysUserService.getById(id));
    }

    /**
     * 根据创建时间段查询用户
     */
    @GetMapping("/getByTime")
    public Res<?> getByTime() {
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.<SysUser>lambdaQuery();
        Calendar calendar=Calendar.getInstance();
        //年月日 也可以具体到时分秒如calendar.set(2015, 10, 12,11,32,52);  !!!注意月份从0开始，到11月
        calendar.set(2022, 10, 01);
        Date date=calendar.getTime();//date就是你需要的时间
//        SysUser sysUser = new SysUser();
//        sysUser.setCreateTime(date);
//        System.out.println(sysUser.getCreateTime());
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
		String format = dateFormat.format(date);
        System.err.println(format);

        //结束时间
        Calendar endCal = Calendar.getInstance();
        endCal.set(2022,10,5,23,59,59);
        Date endTime=endCal.getTime();
//        wrapper.ge(SysUser::getCreateTime,sysUser.getCreateTime());
        wrapper.ge(SysUser::getCreateTime,format);
        wrapper.le(SysUser::getCreateTime,endTime);
//        wrapper.eq(SysUser::getUsername,"admin");
//        SysUser user2 = sysUserService.getOne(wrapper);
        List<SysUser> list = sysUserService.list(wrapper);
        if (list.isEmpty()){
            return Res.error("-1","无结果");
        }else {
            return Res.success(list);
        }
    }

    /**
     * 状态切换控制
     */
    @PutMapping("/status")
    public Res<String> statusHandle(@RequestBody SysUser sysUser){
        boolean b = sysUserService.statusHandle(sysUser);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","切换失败");
        }
    }

    /**
     * 检查用户名是否重复
     * @param username
     * @return
     */
    @GetMapping("/checkUserName")
    public Res<String> checkUserName(@RequestParam String username){
        boolean b = sysUserService.checkUserName(username);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","用户名重复");
        }
    }

    /**
     * 获取个人信息
     */
    @GetMapping("/getUserInfo")
    public Res<?> getUserInfo(){
        SysUser userInfo = sysUserService.getUserInfo();
        if(userInfo != null){
            return Res.success(userInfo);
        }else {
            return Res.error("-1","未登录");
        }
    }
    /**
     * 更新个人信息
     */
    @PutMapping("/updateInfo")
    public Res<String> updateInfo(@RequestBody SysUser sysUser){
        boolean b = sysUserService.updateSysUserInfo(sysUser);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","更新失败");
        }
    }
}

