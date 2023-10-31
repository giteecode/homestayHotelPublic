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
 * @since 2022-11-24 15:29:25
 */
@Data
@TableName("sys_log")
public class SysLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("`operation`")
    private String operation;

    @TableField("`method`")
    private String method;

    @TableField("`uri`")
    private String uri;

    @TableField("`ip`")
    private String ip;

    @TableField("`params`")
    private String params;

    @TableField("`operator_id`")
    private Long operatorId;

    @TableField("`operator_name`")
    private String operatorName;

    @TableField("`use_time`")
    private Double useTime;

    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 访问状态（0成功 1失败）
     */
    @TableField("`status`")
    private Integer status;
}
