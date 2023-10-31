package com.homestay.homestay.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.common.Res;
import com.homestay.homestay.config.aop.SysLog;
import com.homestay.homestay.entity.HOrder;
import com.homestay.homestay.entity.HRoom;
import com.homestay.homestay.service.HOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-06 16:32:19
 */

@RestController
@RequestMapping("/h/order")
public class HOrderController {
    @Resource
    private HOrderService hOrderService;

    /**
     * @return 所有订单列表
     */
    @GetMapping()
    public Res<?> getOrderPage(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "") String search,
                               @RequestParam(defaultValue = "") String searchType) {
        if (pageSize > 1000) {
            return Res.error("-1", "太大了");
        }
        // search：默认username
        Page<HOrder> page = hOrderService.getOrderPage(pageNum, pageSize, search, searchType);
        return Res.success(page);
    }

    /**
     * @return 获取预约中的page
     */
    @GetMapping("/getSubscribePage")
    public Res<?> getSubscribePage(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(defaultValue = "") String search,
                                   @RequestParam(defaultValue = "") String searchType) {
        if (pageSize > 1000) {
            return Res.error("-1", "太大了");
        }
        // search：默认username
        Page<HOrder> page = hOrderService.getSubscribePage(pageNum, pageSize, search, searchType);
        return Res.success(page);
    }

    /**
     * @return 获取入住中的page
     */
    @GetMapping("/getCheckPage")
    public Res<?> getCheckPage(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "") String search,
                               @RequestParam(defaultValue = "") String searchType) {
        if (pageSize > 1000) {
            return Res.error("-1", "太大了");
        }
        // search：默认username
        Page<HOrder> page = hOrderService.getCheckPage(pageNum, pageSize, search, searchType);
        return Res.success(page);
    }

    /**
     * 新增操作
     */
    @PostMapping
    public Res<?> addHandel(@RequestBody HOrder hOrder) {
        boolean b = hOrderService.addOne(hOrder);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "添加失败");
        }
    }

    /**
     * 取消预订操作
     */
    @DeleteMapping("/{id}")
    public Res<?> deleteHandle(@PathVariable Long id) {
        boolean b = hOrderService.CancelOrder(id);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "取消失败");
        }
    }

    /**
     * 修改、办理入住
     */
    @PutMapping
    public Res<?> update(@RequestBody HOrder hOrder) {
        boolean b = hOrderService.updateOrder(hOrder);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "更新失败");
        }
    }

    /**
     * 支付订单
     */
    @PostMapping("/pay")
    public Res<?> payHandle(@RequestBody Long orderId) {
        boolean b = hOrderService.payHandle(orderId);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "支付失败");
        }
    }

    /**
     * 续住操作
     */
    @PostMapping("/plus")
    public Res<?> orderPlus(@RequestParam Long orderId, @RequestParam Integer days) {
        boolean b = hOrderService.orderPlus(orderId, days);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "支付失败");
        }

    }

    /**
     * 退房操作
     */
    @PostMapping("/checkout")
    public Res<?> checkout(@RequestBody HOrder hOrder) {
        if (hOrder.getPayState() == 0) {
            return Res.error("-1", "订单未支付");
        }
        boolean b = hOrderService.checkout(hOrder);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "退房失败");
        }
    }

    /**
     * 图表月视图，当前年中每月数据
     */
    @GetMapping("/getMonthData")
    public Res<?> getMonthData() {
        Map<String, Object> monthData = hOrderService.getMonthData();
        return Res.success(monthData);
    }

    /**
     * 图表周视图，近一个月4周数据
     */
    @GetMapping("/getWeekData")
    public Res<?> getWeekData() {
        Map<String, Object> data = hOrderService.getWeekData();
        return Res.success(data);
    }

    /**
     * 删除操作
     */
    @SysLog("order delete")
    @DeleteMapping("/del/{id}")
    public Res<?> deleteHOrder(@PathVariable Long id) {
        boolean b = hOrderService.deleteHOrder(id);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "删除失败");
        }
    }

    /**
     * 查询当前登录用户所有订单
     */
    @GetMapping("/getUserOrders")
    public Res<?> getUserOrders() {
        if (!StpUtil.isLogin()) {
            return Res.error("-1", "未登录！");
        }
        Page<HOrder> b = hOrderService.getUserOrders();
        if (ObjectUtil.isNotEmpty(b)) {
            return Res.success(b);
        } else {
            return Res.error("-1", "获取失败");
        }
    }

    /**
     * 评价订单
     */
    @PostMapping("/comment")
    public Res<?> comment(@RequestParam Long id,@RequestParam Integer rate,@RequestParam String comment) {
        boolean b= hOrderService.commentHandle(id,rate,comment);
        if (b) {
            return Res.success();
        } else {
            return Res.error("-1", "评价失败");
        }
    }

    /**
     * 根据roomId获取有效评价
     */
    @GetMapping("/getComment")
    public Res<?> getComment(@RequestParam Integer roomId,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        if (pageSize <0 || pageSize > 100){return Res.error("-1","数量超出范围（0-100）");}
        Page<HOrder> list= hOrderService.getComment(pageNum,pageSize,roomId);
        if (ObjectUtil.isNotEmpty(list)){
            return Res.success(list);
        }else {
            return Res.error("-1","获取失败");
        }
    }
}

