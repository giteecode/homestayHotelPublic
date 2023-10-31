package com.homestay.homestay.config.aop;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.homestay.homestay.entity.HUser;
import com.homestay.homestay.entity.SysLogEntity;
import com.homestay.homestay.entity.SysUser;
import com.homestay.homestay.service.HUserService;
import com.homestay.homestay.service.SysLogService;
import com.homestay.homestay.service.SysUserService;
import com.homestay.homestay.utils.WebUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;

@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private HUserService hUserService;
    private static Logger logger = LoggerFactory.getLogger(SysLogAspect.class);

    @Around("@annotation(sysLog)")
    public Object around(ProceedingJoinPoint joinPoint, SysLog sysLog) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = joinPoint.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        SysLogEntity sysLogEntity = new SysLogEntity();
        if(sysLog != null){
            //注解上的描述
            sysLogEntity.setOperation(sysLog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        sysLogEntity.setMethod(className + "." + methodName + "()");
        //请求uri
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        sysLogEntity.setUri(request.getRequestURI());

        Map<String, String> headers = WebUtils.getHeaders(request);
        logger.info("headers: {}",headers);

        //请求的参数
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);
        sysLogEntity.setParams(params);

        //设置IP地址
        sysLogEntity.setIp(WebUtils.getRealIp(request));
        //设置操作者id和username
        Object loginId = StpUtil.getLoginIdDefaultNull();
        if (loginId != null){
            sysLogEntity.setOperatorId(Long.valueOf(loginId.toString()));
            //操作者username
            SysUser sysUser = sysUserService.getById(Long.valueOf(loginId.toString()));
            if (sysUser != null){
                sysLogEntity.setOperatorName(sysUser.getUsername());
            }else { //如果根据这个id查不到后台用户则向前台用户数据库中查询
                HUser hUser = hUserService.getById(Long.valueOf(loginId.toString()));
                if (hUser != null){
                    sysLogEntity.setOperatorName(hUser.getUsername());
                }
            }
        }else {
            sysLogEntity.setStatus(1); //获取不到用户信息则视为失败
        }
        //耗时
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(time));
        BigDecimal useTime = bigDecimal.divide(new BigDecimal("1000"), 3, RoundingMode.HALF_UP);
        sysLogEntity.setUseTime(useTime.doubleValue());
        //插入时间
        sysLogEntity.setCreateTime(new Date());
        //保存系统日志
        sysLogService.save(sysLogEntity);
        return result;
    }

}
