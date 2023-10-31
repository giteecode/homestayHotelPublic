package com.homestay.homestay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.entity.HOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-06 16:32:19
 */
public interface HOrderService extends IService<HOrder> {

    /**
     * 分页倒序获取订单列表，可指定搜索类型
     * @param pageNum
     * @param pageSize
     * @param search
     * @param searchType
     * @return
     */
    Page<HOrder> getOrderPage(Integer pageNum, Integer pageSize, String search, String searchType);

    /**
     * 新增一个订单
     * @param hOrder
     * @return
     */
    boolean addOne(HOrder hOrder);


    /**
     *  取消预订的操作
     * @param id
     * @return
     */
    boolean CancelOrder(Long id);

    /**
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @param searchType
     * @return 获取预约中的page
     */
    Page<HOrder> getSubscribePage(Integer pageNum, Integer pageSize, String search, String searchType);

    /**
     *  修改预订、办理入住
     * @param hOrder
     * @return
     */
    boolean updateOrder(HOrder hOrder);

    /**
     * 支付订单
     * @param orderId
     * @return
     */
    boolean payHandle(Long orderId);

    /**
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @param searchType
     * @return 获取入住中的page
     */
    Page<HOrder> getCheckPage(Integer pageNum, Integer pageSize, String search, String searchType);

    /**
     *  续住操作
     * @param orderId
     * @param days
     * @return
     */
    boolean orderPlus(Long orderId, Integer days);

    /**
     * 退房操作
     * @param hOrder
     * @return
     */
    boolean checkout(HOrder hOrder);

    /**
     * 获取财务统计数据
     */
    Map<String, Object> getMoneyData();

    /**
     * 图表月视图，当前年中每月数据
     */
    Map<String, Object> getMonthData();

    /**
     * 图表周视图，近一个月4周数据
     */
    Map<String, Object> getWeekData();

    /**
     * 删除订单（仅设置状态，并非真实删除）
     * @param id
     * @return
     */
    boolean deleteHOrder(Long id);

    /**
     * 获取当前登录用户所有订单
     * @return
     */
    Page<HOrder> getUserOrders();

    /**
     * 评价订单
     * @param id
     * @param rate
     * @param comment
     * @return
     */
    boolean commentHandle(Long id, Integer rate, String comment);

    /**
     * 根据roomId获取有效评价
     * @param pageNum
     * @param pageSize
     * @param roomId
     * @return
     */
    Page<HOrder> getComment(Integer pageNum, Integer pageSize, Integer roomId);
}
