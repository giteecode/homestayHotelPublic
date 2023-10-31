package com.homestay.homestay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-02 16:45:21
 */

@TableName("sys_role_menu")
@Data
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色菜单主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */
    @TableField("`role_id`")
    private Long roleId;

    /**
     * 菜单ID
     */
    @TableField("`menu_id`")
    private Long menuId;


}
