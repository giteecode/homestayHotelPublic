package com.homestay.homestay.service.impl;

import com.homestay.homestay.entity.SysUser;
import com.homestay.homestay.service.SaTokenService;
import com.homestay.homestay.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 张喜龙
 * @date: 2022/11/1 17:44
 * @version: 1.0
 * @Description:
 */
@Service
public class SaTokenServiceImpl implements SaTokenService {
    @Resource
    private SysUserService sysUserService;
    @Override
    public List getPermissionStr(Integer userId){
        List<String> list = new ArrayList<String>();
        SysUser sysUser = sysUserService.getById(userId);
//        System.err.println(sysUser.getRole());
        if (sysUser.getRole() == 1){
            list.add("admin.add");
            list.add("admin.update");
            list.add("admin.get");
            list.add("admin.delete");
        }else {
            list.add("user.add");
            list.add("user.update");
            list.add("user.get");
            list.add("user.delete");
        }

        return list;
    }

    @Override
    public List getRoleList(Integer userId) {
        List<String> list = new ArrayList<String>();
        SysUser sysUser = sysUserService.getById(userId);
//        System.err.println(sysUser.getRole());
        if (sysUser.getRole() == 1){
           list.add("admin");
        }else {
            list.add("user");
        }
        return list;
    }


}
