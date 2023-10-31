package com.homestay.homestay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.entity.HUser;

import com.homestay.homestay.mapper.HUserMapper;
import com.homestay.homestay.service.HUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-03 14:51:43
 */
@Service
public class HUserServiceImpl extends ServiceImpl<HUserMapper, HUser> implements HUserService {

    /**
     * 登录成功的处理
     */
    @Override
    public Map<String, Object> doLogin(HUser hUser) {
        //        更新最后登录时间
        hUser.setLoginTime(new Date());
        updateById(hUser);
//        sa-token认证、鉴权
        StpUtil.login(hUser.getId());
        Map<String, Object> resultMap = new HashMap<>();
        HUser resUser = hUser;
        resUser.setPassword(SecureUtil.md5(resUser.getPassword())); //返回的密码进行加密
        resultMap.put("user", resUser);

        resultMap.put("tokenInfo", StpUtil.getTokenInfo());
        return resultMap;
    }

    /**
     * 启用/禁用状态转换
     *
     * @param hUser
     * @return
     */
    @Override
    public boolean statusHandle(HUser hUser) {
        if (hUser.getStatus() == 0) {
            hUser.setStatus(1);
        } else {
            hUser.setStatus(0);
        }
        boolean i = this.updateById(hUser);
        return i;
    }

    /**
     * 检查用户名是否重复
     *
     * @param username
     * @return
     */
    @Override
    public boolean checkUserName(String username) {
        LambdaQueryWrapper<HUser> wrapper = Wrappers.<HUser>lambdaQuery();
        wrapper.eq(HUser::getUsername, username);
        HUser one = this.getOne(wrapper);
        if (one != null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取当前登录的用户信息
     */
    @Override
    public HUser getUserInfo() {
        // 根据token取出当前用户id
        Object getLoginId = StpUtil.getLoginIdDefaultNull();
        if (getLoginId != null) {
            Integer loginId = Convert.toInt(getLoginId);
            HUser user = this.getById(loginId);
            return user;
        } else {
            return null;
        }
    }

    /**
     * 新增一个用户
     *
     * @param hUser
     * @return
     */
    @Override
    public boolean addOneHUser(HUser hUser) {
        // 先检查用户名是否重复
        boolean b = checkUserName(hUser.getUsername());
        if (!b) {
            return false;
        }

        if (hUser.getPassword() == null) {
            hUser.setPassword("123456");
        }

        hUser.setCreateTime(new Date());
        boolean save = this.save(hUser);
        return save;
    }

    /**
     * 更新个人信息
     *
     * @param hUser@return
     */
    @Override
    public boolean updateHUserInfo(HUser hUser) {
        // 根据token取出当前用户id
        Object getLoginId = StpUtil.getLoginIdDefaultNull();
        if (getLoginId != null) {
            Integer loginId = Convert.toInt(getLoginId);
            hUser.setId(loginId);
            hUser.setUpdateTime(new Date());
            boolean b = this.updateById(hUser);
            return b;
        } else {
            return false;
        }
    }

    /**
     * @param pageNum
     * @param pageSize
     * @param search
     * @return 分页获取前台用户列表
     */
    @Override
    public Page<HUser> getHUserPage(Integer pageNum, Integer pageSize, String search) {
        LambdaQueryWrapper<HUser> wapper = Wrappers.<HUser>lambdaQuery();
//        当serch不是空的时候再进行模糊查询,否则全部查询
        if (StrUtil.isNotBlank(search)) {
            wapper.like(HUser::getUsername, search);
        }
        Page<HUser> page = this.page(new Page<>(pageNum, pageSize), wapper);
        return page;
    }

    /**
     * 获取注册数据
     */
    @Override
    public Map<String, Object> getRegisterData() {
        Map<String, Object> resultMap = new HashMap<>();
        // 获取本年度数据
        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime yearStart = now.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
        LocalDateTime yearEnd = now.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
        LambdaQueryWrapper<HUser> wrapper = Wrappers.<HUser>lambdaQuery();
        wrapper.ge(HUser::getCreateTime, yearStart);
        wrapper.le(HUser::getCreateTime, yearEnd);
//        List<HUser> yearList = this.list(wrapper);
        long count = this.count(wrapper);
        resultMap.put("year", count);
//        System.out.println("当前年的开始时间:" + yearStart.format(fmt));
//        System.out.println("当前年的结束时间:" + yearEnd.format(fmt));
        // 获取本月数据
        LambdaQueryWrapper<HUser> wrapper2 = Wrappers.<HUser>lambdaQuery();
        LocalDateTime monthStart = now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        LocalDateTime monthEnd = now.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        wrapper2.ge(HUser::getCreateTime, monthStart);
        wrapper2.le(HUser::getCreateTime, monthEnd);
//        List<HUser> monthList = this.list(wrapper2);
        long count2 = this.count(wrapper2);
        resultMap.put("month", count2);
        // 获取本周数据
        LambdaQueryWrapper<HUser> wrapper3 = Wrappers.<HUser>lambdaQuery();
        int dayOfWeek = now.getDayOfWeek().getValue();
        LocalDateTime weekStart = now.minusDays(dayOfWeek - 1).with(LocalTime.MIN);
        LocalDateTime weekEnd = now.plusDays(7 - dayOfWeek).with(LocalTime.MAX);
        wrapper3.ge(HUser::getCreateTime, weekStart);
        wrapper3.le(HUser::getCreateTime, weekEnd);
//        List<HUser> weekList = this.list(wrapper3);
        long count3 = this.count(wrapper3);
        resultMap.put("week", count3);
        return resultMap;
    }

    /**
     * 根据用户手机号进行总消费金额的更新
     *
     * @param phone     用户手机号
     * @param plusTotal 新订单总消费
     * @return
     */
    @Override
    public boolean updateTotal(String phone, BigDecimal plusTotal) {
        LambdaQueryWrapper<HUser> wrapper = Wrappers.<HUser>lambdaQuery();
        wrapper.eq(HUser::getPhone, phone);
        HUser user = this.getOne(wrapper);
        if (user == null) {
            return false;
        }
        user.setTotal(user.getTotal().add(plusTotal));
        if (user.getTotal() == null) {
            user.setTotal(Convert.toBigDecimal(0));
        }
        boolean b = this.updateById(user);
        return b;
    }

    /**
     * 检查手机号是否重复
     *
     * @param phone
     * @return
     */
    @Override
    public boolean checkUserPhone(String phone) {
        LambdaQueryWrapper<HUser> wrapper = Wrappers.<HUser>lambdaQuery();
        wrapper.eq(HUser::getPhone, phone);
        List<HUser> list = this.list(wrapper);
        if (list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 重置密码
     *
     * @param phone
     * @param username
     * @return
     */
    @Override
    public HUser resetPassword(String phone, String username) {
        LambdaQueryWrapper<HUser> wrapper = Wrappers.<HUser>lambdaQuery();
        wrapper.eq(HUser::getPhone,phone);
        HUser user = this.getOne(wrapper);
        if (user.getUsername().equals(username)){
            // 匹配成功，重置密码
            String s = RandomUtil.randomString(10);
            user.setPassword(s);
            this.updateById(user);
            return user;
        }else {
            return null;
        }
    }


}
