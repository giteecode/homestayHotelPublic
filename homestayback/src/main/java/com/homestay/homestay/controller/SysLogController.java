package com.homestay.homestay.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.common.Res;
import com.homestay.homestay.entity.SysLogEntity;
import com.homestay.homestay.service.SysLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 张喜龙
 * @Date: 2022/11/24/17:30
 * @Description:
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController {
    @Resource
    private SysLogService sysLogService;
    /**
     * 分页查询，返回数据
     */
    @GetMapping
    public Res<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "100") Integer pageSize,
                           @RequestParam(defaultValue = "") String search) {
        if (pageSize > 1000){
            return Res.error("-1","太大了");
        }
        LambdaQueryWrapper<SysLogEntity> wapper = Wrappers.<SysLogEntity>lambdaQuery();
//        当serch不是空的时候再进行模糊查询,否则全部查询
        if (StrUtil.isNotBlank(search)) {
            wapper.eq(SysLogEntity::getOperatorId, search);
        }
        Page<SysLogEntity> adminPage = sysLogService.page(new Page<>(pageNum, pageSize), wapper.orderByDesc(SysLogEntity::getId));
        return Res.success(adminPage);
    }

    /**
     * 根据Ids批量删除log
     * */
    @PostMapping("/deleteBatch")
    public Res<?> deleteBatch(@RequestBody List<Integer> ids) {
        boolean b = sysLogService.removeByIds(ids);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","批量删除失败");
        }
    }
    @DeleteMapping("/{id}")
    public Res<?> deleteById(@PathVariable Long id) {
        boolean b = sysLogService.removeById(id);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","删除失败");
        }
    }

    /**
     * 获取当前用户登录日志
     */
    @GetMapping("/getLoginInfo")
    public Res<?> getLoginTimes(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize){
        Page<SysLogEntity> loginLog = sysLogService.getLoginLog(pageNum,pageSize);
        if(loginLog != null){
            return Res.success(loginLog);
        }else {
            return Res.error("-1","获取登录日志失败");
        }
    }

    /**
     * 获取前台当前用户登录日志
     */
    @GetMapping("/frontLoginLog")
    public Res<?> frontLoginLog(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize){
        Page<SysLogEntity> loginLog = sysLogService.frontLoginLog(pageNum,pageSize);
        if(loginLog != null){
            return Res.success(loginLog);
        }else {
            return Res.error("-1","获取登录日志失败");
        }
    }
}
