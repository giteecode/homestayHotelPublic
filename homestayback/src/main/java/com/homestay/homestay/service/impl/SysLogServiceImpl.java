package com.homestay.homestay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.homestay.entity.SysLogEntity;
import com.homestay.homestay.mapper.SysLogMapper;
import com.homestay.homestay.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-24 15:22:30
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLogEntity> implements SysLogService {


    @Override
    public void saveSysLog(SysLogEntity entity) {
        this.save(entity);
    }

    /**
     * 获取当前登录用户的登录日志
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page<SysLogEntity> getLoginLog(Integer pageNum, Integer pageSize) {
        Object loginId = StpUtil.getLoginIdDefaultNull();
//        Object loginId = "10001";
        if (loginId != null){
            LambdaQueryWrapper<SysLogEntity> wrapper = Wrappers.<SysLogEntity>lambdaQuery();
            wrapper.eq(SysLogEntity::getOperatorId,Long.valueOf(loginId.toString())).and(w -> w.eq(SysLogEntity::getUri,"/sys/user/login"));
//            List<SysLogEntity> list = this.list(wrapper);
//            list.sort(Comparator.comparing(SysLogEntity::getId).reversed()); //倒排序
            Page<SysLogEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper.orderByDesc(SysLogEntity::getId));
            return page;
        }else {
            return null;
        }
    }

    /**
     * 获取前端当前登录用户的登录日志
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page<SysLogEntity> frontLoginLog(Integer pageNum, Integer pageSize) {
        Object loginId = StpUtil.getLoginIdDefaultNull();
        if (loginId != null){
            LambdaQueryWrapper<SysLogEntity> wrapper = Wrappers.<SysLogEntity>lambdaQuery();
            wrapper.eq(SysLogEntity::getOperatorId,Long.valueOf(loginId.toString())).and(w -> w.eq(SysLogEntity::getUri,"/h/user/login"));
            Page<SysLogEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper.orderByDesc(SysLogEntity::getId));
            return page;
        }else {
            return null;
        }
    }


}
