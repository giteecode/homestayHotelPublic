package com.homestay.homestay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.entity.HOrder;
import com.homestay.homestay.entity.HRoom;
import com.homestay.homestay.entity.HUser;
import com.homestay.homestay.mapper.HOrderMapper;
import com.homestay.homestay.service.HOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.homestay.service.HRoomService;
import com.homestay.homestay.service.HUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-06 16:32:19
 */
@Service
public class HOrderServiceImpl extends ServiceImpl<HOrderMapper, HOrder> implements HOrderService {
    @Resource
    private HRoomService hRoomService;
    @Resource
    private HUserService hUserService;

    // 条件查询构造器
    public LambdaQueryWrapper<HOrder> typeSearch(String search, String searchType) {
        LambdaQueryWrapper<HOrder> wrapper = Wrappers.<HOrder>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            if (StrUtil.isNotBlank(searchType)) {
                if (searchType.equals("username")) {
                    wrapper.like(HOrder::getUsername, search);
                } else if (searchType.equals("idCard")) {
                    wrapper.like(HOrder::getIdCard, search);
                } else if (searchType.equals("phone")) {
                    wrapper.like(HOrder::getPhone, search);
                } else if (searchType.equals("roomId")) {
                    wrapper.like(HOrder::getRoomId, search);
                } else if (searchType.equals("roomCode")) {
                    wrapper.like(HOrder::getRoomCode, search);
                } else {
                    wrapper.like(HOrder::getUsername, search);
                }
            } else {
                wrapper.like(HOrder::getUsername, search);
            }
        }
        return wrapper;
    }

    /**
     * 分页倒序获取订单列表，可指定搜索类型
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @param searchType
     * @return
     */
    @Override
    public Page<HOrder> getOrderPage(Integer pageNum, Integer pageSize, String search, String searchType) {
//        LambdaQueryWrapper<HOrder> wrapper = Wrappers.<HOrder>lambdaQuery();
//        if (StrUtil.isNotBlank(search)){
//            if (StrUtil.isNotBlank(searchType)){
//                if (searchType.equals("username")){
//                    wrapper.like(HOrder::getUsername,search);
//                }else if(searchType.equals("idCard")){
//                    wrapper.like(HOrder::getIdCard,search);
//                }else if(searchType.equals("phone")){
//                    wrapper.like(HOrder::getPhone,search);
//                }else if(searchType.equals("roomId")){
//                    wrapper.like(HOrder::getRoomId,search);
//                }else if(searchType.equals("roomCode")){
//                    wrapper.like(HOrder::getRoomCode,search);
//                }else {
//                    wrapper.like(HOrder::getUsername,search);
//                }
//            }else {
//                wrapper.like(HOrder::getUsername,search);
//            }
//        }
        LambdaQueryWrapper<HOrder> wrapper = typeSearch(search, searchType);
        Page<HOrder> page = this.page(new Page<>(pageNum, pageSize), wrapper.orderByDesc(HOrder::getId));
        return page;
    }

    /**
     * 新增一个订单
     *
     * @param hOrder
     * @return
     */
    @Override
    public boolean addOne(HOrder hOrder) {
        hOrder.setCreateTime(new Date());
        Object userId = StpUtil.getLoginIdDefaultNull();
        if (userId != null) {
            hOrder.setUserId(Convert.toInt(userId));
        } else {
            hOrder.setUserId(0);
        }

        HRoom hRoom = hRoomService.getById(hOrder.getRoomId());
        hRoom.setState(hOrder.getState());
        hRoom.setEndTime(hOrder.getEndDate());
        hRoomService.updateById(hRoom);
        boolean save = this.save(hOrder);
        return save;
    }

    /**
     * 取消预订的操作
     *
     * @param id
     * @return
     */
    @Override
    public boolean CancelOrder(Long id) {
        HOrder order = this.getById(id);
        if (order != null) {
            order.setStatus(2); //关闭订单，并非删除
            order.setState("取消");
            boolean b = this.updateById(order);
            updateRoomEndTime(order.getRoomId());
            return b;
        } else {
            return false;
        }
    }

    /**
     * 订单取消预订时，查看该订单中房间room是否有其他预约订单，有则把房间最终服务时间改为其余订单的截止时间
     */
    public boolean updateRoomEndTime(Integer roomId) {
        LambdaQueryWrapper<HOrder> wrapper = Wrappers.<HOrder>lambdaQuery();
        wrapper.eq(HOrder::getRoomId, roomId);
        wrapper.eq(HOrder::getState, "预订");
        List<HOrder> orders = this.list(wrapper.orderByDesc(HOrder::getId));
        HRoom room = hRoomService.getById(roomId);
        if (room == null) {
            return false;
        }
        if (!orders.isEmpty()) {
            room.setState("预订");
            room.setEndTime(orders.stream().findFirst().get().getEndDate());
        } else {
            room.setState("空闲");
            room.setEndTime(DateUtil.parse("2022-01-1 01:02:03"));
        }
        boolean b = hRoomService.updateById(room);
        return b;
    }

    /**
     * @param pageNum
     * @param pageSize
     * @param search
     * @param searchType
     * @return 获取预约中的page
     */
    @Override
    public Page<HOrder> getSubscribePage(Integer pageNum, Integer pageSize, String search, String searchType) {
        LambdaQueryWrapper<HOrder> wrapper = typeSearch(search, searchType);
        wrapper.eq(HOrder::getState, "预订");
        Page<HOrder> page = this.page(new Page<>(pageNum, pageSize), wrapper.orderByDesc(HOrder::getId));
        return page;
    }

    /**
     * 修改预订、办理入住
     *
     * @param hOrder
     * @return
     */
    @Override
    public boolean updateOrder(HOrder hOrder) {
        HRoom room = hRoomService.getById(hOrder.getRoomId());
        if (hOrder.getState().equals("入住")) {
            room.setState("入住");
//            room.setEndTime(hOrder.getEndDate());
            hRoomService.updateById(room);
        } else { //非入住，即为更新操作
            hOrder.setUpdateTime(new Date());
            room.setEndTime(hOrder.getEndDate());
            hRoomService.updateById(room);
        }
        boolean b = this.updateById(hOrder);
        return b;
    }

    /**
     * 支付订单
     *
     * @param orderId
     * @return
     */
    @Override
    public boolean payHandle(Long orderId) {
        HOrder byId = this.getById(orderId);
        byId.setPayState(1);
        boolean b = this.updateById(byId);
        return b;
    }

    /**
     * @param pageNum
     * @param pageSize
     * @param search
     * @param searchType
     * @return 获取入住中的page
     */
    @Override
    public Page<HOrder> getCheckPage(Integer pageNum, Integer pageSize, String search, String searchType) {
        LambdaQueryWrapper<HOrder> wrapper = typeSearch(search, searchType);
        wrapper.eq(HOrder::getState, "入住").eq(HOrder::getStatus, 0);
        Page<HOrder> page = this.page(new Page<>(pageNum, pageSize), wrapper.orderByDesc(HOrder::getId));
        return page;
    }

    /**
     * 续住操作
     *
     * @param orderId
     * @param days
     * @return
     */
    @Override
    public boolean orderPlus(Long orderId, Integer days) {
        HOrder order = this.getById(orderId);
        if (order != null) {
            HRoom room = hRoomService.getById(order.getRoomId());
            order.setDays(order.getDays() + days);
            order.setPayState(0);
            order.setTotal(order.getTotal().add(room.getPrice().multiply(BigDecimal.valueOf(days))));
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(order.getEndDate());
            // 把日期往后增加一天,整数  往后推,负数往前移动
            calendar.add(Calendar.DATE, days);
            order.setEndDate(calendar.getTime());
            room.setEndTime(order.getEndDate());
            hRoomService.updateById(room);
            boolean b = this.updateById(order);
            return b;
        } else {
            return false;

        }
    }

    /**
     * 退房操作
     *
     * @param hOrder
     * @return
     */
    @Override
    public boolean checkout(HOrder hOrder) {
        hOrder.setState("完成");
        hOrder.setStatus(1);
        //更改房间服务截止日期
        updateRoomEndTime(hOrder.getRoomId());
        boolean b = this.updateById(hOrder);
        // 更新该订单用户总消费
        hUserService.updateTotal(hOrder.getPhone(), hOrder.getTotal());
        return b;
    }

    /**
     * 获取财务统计数据
     */
    @Override
    public Map<String, Object> getMoneyData() {
        // 获取当月数据
        // 获取上月数据
        // 获取本周数据
        // 获取上周数据
        return null;
    }

    /**
     * 图表月视图，当前年中每月数据
     */
    @Override
    public Map<String, Object> getMonthData() {
        Map<String, Object> map = new HashMap<>();
        List xAxis = new ArrayList();
        List seriesData = new ArrayList();
        // 1.先获取当前月
        LocalDateTime now = LocalDateTime.now(); // 获取当前时间
        int nowMonth = now.getMonthValue();
        for (int i = 0; i < nowMonth; i++) {
            // 2.月份递减
            LocalDateTime newTime = now.minusMonths(i);
            // 3.获取对应月份起始时间
            LocalDateTime monthStart = newTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
            LocalDateTime monthEnd = newTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
            LambdaQueryWrapper<HOrder> wrapper = Wrappers.<HOrder>lambdaQuery();
            // 4.构建条件查询
            wrapper.ge(HOrder::getCreateTime, monthStart);
            wrapper.le(HOrder::getCreateTime, monthEnd);
            wrapper.eq(HOrder::getStatus, 1);
            List<HOrder> orders = this.list(wrapper);
            xAxis.add(newTime.getMonthValue() + "月");
            if (orders.isEmpty()) {
                seriesData.add(0);
            } else {
                BigDecimal total = new BigDecimal(0);
                for (HOrder order : orders) {
                    total = total.add(order.getTotal());
                }
                seriesData.add(total);
            }
        }
        map.put("xAxis", xAxis);
        map.put("seriesData", seriesData);
        return map;
    }

    /**
     * 图表周视图，近一个月4周数据
     */
    @Override
    public Map<String, Object> getWeekData() {
        Map<String, Object> map = new HashMap<>();
        List xAxis = new ArrayList();
        List seriesData = new ArrayList();
        LocalDateTime now = LocalDateTime.now(); // 获取当前时间
        for (int i = 0; i < 4; i++) {
            // 1.先减去i天，实现查询前一周数据
            LocalDateTime newTime = now.minusDays(7 * i);
            // 2.获取周起始日期
            int dayOfWeek = newTime.getDayOfWeek().getValue();
            LocalDateTime weekStart = newTime.minusDays(dayOfWeek - 1).with(LocalTime.MIN);
            LocalDateTime weekEnd = newTime.plusDays(7 - dayOfWeek).with(LocalTime.MAX);
            // 3.构建条件查询
            LambdaQueryWrapper<HOrder> wrapper = Wrappers.<HOrder>lambdaQuery();
            wrapper.ge(HOrder::getCreateTime, weekStart);
            wrapper.le(HOrder::getCreateTime, weekEnd);
            wrapper.eq(HOrder::getStatus, 1);
            // 4.处理数据
            xAxis.add("第" + LocalDateTimeUtil.weekOfYear(newTime) + "周");
            List<HOrder> orders = this.list(wrapper);
            if (orders.isEmpty()) {
                seriesData.add(0);
            } else {
                BigDecimal total = new BigDecimal(0);
                for (HOrder order : orders) {
                    total = total.add(order.getTotal());
                }
                seriesData.add(total);
            }
        }
        map.put("xAxis", xAxis);
        map.put("seriesData", seriesData);
        return map;
    }

    /**
     * 删除订单（仅设置状态，并非真实删除）
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteHOrder(Long id) {
        HOrder order = this.getOne(Wrappers.<HOrder>lambdaQuery().eq(HOrder::getId, id));
        order.setState("删除");
        order.setRate(0); //删除订单后评价应该消失
        order.setStatus(3);
        boolean b = this.updateById(order);
        return b;
    }

    /**
     * 获取当前登录用户所有订单
     *
     * @return
     */
    @Override
    public Page<HOrder> getUserOrders() {
        LambdaQueryWrapper<HOrder> wrapper = Wrappers.<HOrder>lambdaQuery();
        // 根据token取出当前用户id
        Object getLoginId = StpUtil.getLoginIdDefaultNull();
        if (getLoginId != null) {
            Integer loginId = Convert.toInt(getLoginId);
            wrapper.eq(HOrder::getUserId, loginId);
            Page<HOrder> page = this.page(new Page<>(1, 200), wrapper.orderByDesc(HOrder::getId));
            return page;
        } else {
            return null;
        }
    }

    /**
     * 评价订单
     *
     * @param id
     * @param rate
     * @param comment
     * @return
     */
    @Override
    public boolean commentHandle(Long id, Integer rate, String comment) {
        HOrder order = this.getById(id);
        if (order != null){
            order.setRate(rate);
            order.setComment(comment);
            boolean b = this.updateById(order);
            // 对相应的room进行加权
            hRoomService.plusWeight(order.getRoomId(),rate);
            return b;
        }else {
            return false;
        }
    }

    /**
     * 根据roomId获取有效评价
     *
     * @param pageNum
     * @param pageSize
     * @param roomId
     * @return
     */
    @Override
    public Page<HOrder> getComment(Integer pageNum, Integer pageSize, Integer roomId) {
        LambdaQueryWrapper<HOrder> wrapper = Wrappers.<HOrder>lambdaQuery();
        wrapper.eq(HOrder::getRoomId,roomId);
        wrapper.gt(HOrder::getRate,0); //分数大于0才算有效评价，以过滤未评价订单
        // 只返回想要的字段
        wrapper.select(HOrder::getId,HOrder::getUsername,HOrder::getRate,HOrder::getComment);
        Page<HOrder> page = this.page(new Page<>(pageNum, pageSize), wrapper);

        if (page.getRecords().isEmpty()){
            return null;
        }else {
            List<HOrder> newRecords = page.getRecords();
            for (HOrder order : newRecords) {
               // 脱敏
                order.setUsername(DesensitizedUtil.chineseName(order.getUsername()));
            }
            page.setRecords(newRecords);

        }

        return page;
    }
}
