package com.homestay.homestay.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * <p>
 * 房间表
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-02 17:18:01
 */
@Data
@TableName("h_room")
public class HRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 房间状态：0启用(default)，1禁用
     */
    @TableField("`status`")
    private Integer status;

    /**
     * 民宿名字
     */
    @TableField("`name`")
    private String name;

    /**
     * 所在城市
     */
    @TableField("`city`")
    private String city;

    /**
     * 配套设施
     */
    @TableField("`device`")
    private String device;

    /**
     * 详细地址
     */
    @TableField("`address`")
    private String address;

    /**
     * 房间号
     */
    @TableField("`code`")
    private String code;

    /**
     * 分类
     */
    @TableField("`category`")
    private String category;

    /**
     * 房间价格
     */
    @TableField("`price`")
    private BigDecimal price;

    /**
     * 房间使用状态，空闲/预定/入住
     */
    @TableField("`state`")
    private String state;

    /**
     * banner图地址
     */
    @TableField("`banner`")
    private String banner;

    /**
     * 床位数
     */
    @TableField("`seat`")
    private Integer seat;

    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 备注
     */
    @TableField("`remark`")
    private String remark;

    /**
     * 权重，排序规则
     */
    @TableField("`weight`")
    private Integer weight;
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
     *  该房间服务结束日期
     */
    @TableField(value = "`end_time`", fill = FieldFill.INSERT_UPDATE)
    private Date endTime;
    /**
     * 当房间状态不是空闲状态时，返回该房间预订/入住的开始时间
     */
    @TableField(exist = false)
    private Date startTime;
    /**
     * 当房间状态不是空闲状态时，返回该房间预订/入住的结束时间
     */
//    @TableField(exist = false)
//    private Date endTime;

}
