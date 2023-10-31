package com.homestay.homestay.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * 
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-02 16:33:10
 */

@TableName("sys_user")
@Data
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限id
     */
    @TableField("`role`")
    private Integer role;

    /**
     * 帐号状态（0正常 1停用）
     */
    @TableField("`status`")
    private Integer status;

    /**
     * 用户名
     */
    @TableField("`username`")
    private String username;

    /**
     * 密码
     */
    @TableField("`password`")
    private String password;

    /**
     * 用户真实姓名
     */
    @TableField("`real_name`")
    private String realName;

    /**
     * 身份证号码
     */
    @TableField("`id_card`")
    private String idCard;

    /**
     * 性别
     */
    @TableField("`gender`")
    private String gender;

    /**
     * 地址
     */
    @TableField("`address`")
    private String address;

    /**
     * 用户头像
     */
    @TableField("`avatar`")
    private String avatar;

    /**
     * 用户邮箱
     */
    @TableField("`email`")
    private String email;

    /**
     * 手机号码
     */
    @TableField("`phonenumber`")
    private String phonenumber;

    /**
     * 最后登录时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:ss", timezone = "GMT+8")
    @TableField("`login_date`")
    private Date loginDate;


    /**
     * 创建时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:ss", timezone = "GMT+8")
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:ss", timezone = "GMT+8")
    @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("`remark`")
    private String remark;


}
