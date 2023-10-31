package com.homestay.homestay.config.satoken;/**
 * @author:  张喜龙
 * @date:  2022/11/1 16:38
 * @version: 1.0
 * @Description: 
 */

import cn.dev33.satoken.stp.StpInterface;
import com.homestay.homestay.service.SaTokenService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private SaTokenService saTokenService;
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        List<String> list = new ArrayList<String>();
//        发现一个问题，当用ID作为参数时，第一次生成时传进来的loginId是Integer类型，当鉴权查询时传进来的loginId是String类型
        if (loginId instanceof Integer) {
            Integer userId = (Integer) loginId;
            list = saTokenService.getPermissionStr(userId);
        } else if (loginId instanceof String) {
            Integer userId = Integer.valueOf((String) loginId);
            list = saTokenService.getPermissionStr(userId);
        }
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
//        List<String> list = new ArrayList<String>();
//        list.add("101");
//        list.add("user.add");
//        list.add("user.update");
//        list.add("user.get");
//        // list.add("user.delete");
//        list.add("art.*");
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {

        List<String> list = new ArrayList<String>();
        if (loginId instanceof Integer) {
            Integer userId = (Integer) loginId;
            list = saTokenService.getRoleList(userId);
        } else if (loginId instanceof String) {
            Integer userId = Integer.valueOf((String) loginId);
            list = saTokenService.getRoleList(userId);
        }

        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询角色
//        List<String> list = new ArrayList<String>();
//        list.add("admin");
//        list.add("super-admin");
        return list;
    }

}
