package com.homestay.homestay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.homestay.entity.SysLogEntity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-24 15:22:30
 */
public interface SysLogService extends IService<SysLogEntity> {
    /**
     * 保存日志
     * @param entity
     */
    void saveSysLog(SysLogEntity entity);

    /**
     * 获取当前登录用户的登录日志
     * @return
     */
    Page<SysLogEntity> getLoginLog(Integer pageNum, Integer pageSize);

    /**
     * 获取前端当前登录用户的登录日志
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<SysLogEntity> frontLoginLog(Integer pageNum, Integer pageSize);
}
