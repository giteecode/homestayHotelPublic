package com.homestay.homestay.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 前台用户实体类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-03 14:51:43
 */
@Data
@TableName("h_user")
public class HUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户登录用户名
     */
    @TableField("`username`")
    private String username;

    /**
     * 用户登录密码
     */
    @TableField("`password`")
    private String password;

    /**
     * 用户头像
     */
    @TableField("`avatar`")
    private String avatar;

    /**
     * 用户真实姓名
     */
    @TableField("`real_name`")
    private String realName;

    /**
     * 用户身份证号码
     */
    @TableField("`id_card`")
    private String idCard;

    /**
     * 用户手机号
     */
    @TableField("`phone`")
    private String phone;

    /**
     * 用户性别
     */
    @TableField("`gender`")
    private String gender;

    /**
     * 注册时间
     */
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 最后一次更新时间
     */
    @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 最后一次登陆时间
     */
    @TableField("`login_time`")
    private Date loginTime;

    /**
     * 备注
     */
    @TableField("`remark`")
    private String remark;

    /**
     * 用户总消费金额
     */
    @TableField("`total`")
    private BigDecimal total;

    /**
     * 帐号状态（0正常 1停用）
     */
    @TableField("`status`")
    private Integer status;

}
