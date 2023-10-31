package com.homestay.homestay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.entity.HUser;
import com.baomidou.mybatisplus.extension.service.IService;


import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 前台用户服务类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-03 14:51:43
 */
public interface HUserService extends IService<HUser> {

    /**
     * 登录成功的处理
     */
    Map<String, Object> doLogin(HUser hUser);

    /**
     * 启用/禁用状态转换
     *
     * @return
     */
    boolean statusHandle(HUser hUser);

    /**
     * 检查用户名是否重复
     *
     * @param username
     * @return true:不存在，false：已存在
     */
    boolean checkUserName(String username);

    /**
     * 获取当前登录的用户信息
     */
    HUser getUserInfo();

    /**
     * 新增一个用户
     * @param HUser
     * @return
     */
    boolean addOneHUser(HUser hUser);

    /**
     * 更新个人信息
     * @param HUser
     * @return
     */
    boolean updateHUserInfo(HUser hUser);

    /**
     *
     * @param pageNum
     * @param pageSize
     * @return 分页获取前台用户列表
     */
    Page<HUser> getHUserPage(Integer pageNum, Integer pageSize , String search);

    /**
     * 获取注册数据
     */
    Map<String, Object> getRegisterData();

    /**
     * 根据用户手机号进行总消费金额的更新
     * @param phone 用户手机号
     * @param plusTotal 新订单总消费
     * @return
     */
    boolean updateTotal(String phone, BigDecimal plusTotal);

    /**
     * 检查手机号是否重复
     * @param phone
     * @return true:不存在，false：已存在
     */
    boolean checkUserPhone(String phone);

    /**
     * 重置密码
     * @param phone
     * @param username
     * @return
     */
    HUser resetPassword(String phone, String username);
}
