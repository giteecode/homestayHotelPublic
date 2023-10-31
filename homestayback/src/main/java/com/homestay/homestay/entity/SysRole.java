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
 * @since 2022-11-02 21:23:23
 */
@Data
@TableName("sys_role")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色状态
     */
    @TableField("`status`")
    private Long status;

    /**
     * 角色名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 角色权限字符串
     */
    @TableField("`code`")
    private String code;

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

    /**
     * 当前角色具有的菜单访问权限集合,这个弃用，用ids更为便捷
     */
    @TableField(exist = false)
    private List<SysMenu> roleMenus;

    /**
     * 用于接收/返回角色权限菜单列表，表明当前角色有哪些菜单可以访问
     * 返回角色权限Ids
     */
    @TableField(exist = false)
    private List roleHasMenuIds = new ArrayList<>();
}
