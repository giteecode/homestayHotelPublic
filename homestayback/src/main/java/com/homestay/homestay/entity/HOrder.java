package com.homestay.homestay.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * <p>
 * 订单模块
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-06 16:32:19
 */
@Data
@TableName("h_order")
public class HOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "`user_id`")
    private Integer userId;

    /**
     * 用户名
     */
    @TableField("`username`")
    private String username;

    /**
     * 用户身份证号码
     */
    @TableField("`id_card`")
    private String idCard;

    /**
     * 用户电话号码
     */
    @TableField("`phone`")
    private String phone;

    /**
     * 入住日期
     */
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("`start_date`")
    private Date startDate;

    /**
     * 离开日期
     */
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("`end_date`")
    private Date endDate;

    /**
     * 入住人数
     */
    @TableField("`amount`")
    private Integer amount;

    /**
     * 选择的房间ID
     */
    @TableField("`room_id`")
    private Integer roomId;

    /**
     * 房间号
     */
    @TableField("`room_code`")
    private String roomCode;

    /**
     * 用户添加的备注
     */
    @TableField("`user_info`")
    private String userInfo;

    /**
     * 用户添加的备注
     */
    @TableField("`user_remark`")
    private String userRemark;

    /**
     * 预定状态：预订/入住/取消/完成/删除
     */
    @TableField("`state`")
    private String state;

    /**
     * 订单总价
     */
    @TableField("`total`")
    private BigDecimal total;

    /**
     * 备注
     */
    @TableField("`remark`")
    private String remark;

    /**
     * 支付状态：0:未支付/1:已支付
     */
    @TableField("`pay_state`")
    private Integer payState;

    /**
     * 订单状态：0：正常，1：完成，2：关闭，3：删除
     */
    @TableField("`status`")
    private Integer status;

    /**
     * 创建时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:ss", timezone = "GMT+8")
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 最后更新时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:ss", timezone = "GMT+8")
    @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 入住天数
     */
    @TableField("`days`")
    private Integer days;

    /**
     * 评分
     */
    @TableField("`rate`")
    private Integer rate;

    /**
     * 评价
     */
    @TableField("`comment`")
    private String comment;
}
