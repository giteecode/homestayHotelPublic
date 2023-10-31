package com.homestay.homestay.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-02 16:47:47
 */

@TableName("sys_menu")
@Data
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单主键ID
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    /**
     * 菜单状态
     */
    @TableField("`status`")
    private Long status;

    /**
     * 菜单名称
     */
    @TableField("`menu_name`")
    private String menuName;

    /**
     * 菜单图标
     */
    @TableField("`menu_icon`")
    private String menuIcon;

    /**
     * 父菜单ID
     */
    @TableField("`parent_id`")
    private Long parentId;

    /**
     * 显示顺序
     */
    @TableField("`order_num`")
    private Integer orderNum;

    /**
     * 路由地址
     */
    @TableField("`menu_url`")
    private String menuUrl;

    /**
     * 组件路径
     */
    @TableField("`component`")
    private String component;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @TableField("`menu_type`")
    private String menuType;

    /**
     * 权限标识
     */
    @TableField("`perms`")
    private String perms;

    /**
     * 创建时间
     */
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("`remark`")
    private String remark;


    //以下是数据库中不存在的字段
    /**
     * 子菜单
     * */
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();

    /**
     * 用于接收前端传回的角色权限列表，表明当前菜单有哪些角色可以访问(由于使用Ids比较便捷，故此处信息丢弃)
     * 返回角色权限详细信息（已抛弃）
     */
    @TableField(exist = false)
    private List<SysRole> menuHasRoles = new ArrayList<>();

    /**
     * 用于接收前端传回的角色权限列表，表明当前菜单有哪些角色可以访问
     * 返回角色权限Ids
     */
    @TableField(exist = false)
    private List menuHasRoleIds = new ArrayList<>();
}
