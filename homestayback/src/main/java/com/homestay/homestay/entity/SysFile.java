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
 * @since 2022-11-26 09:34:37
 */
@Data
@TableName("sys_file")
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 上传文件的UUID
     */
    @TableField("`uuid`")
    private String uuid;

    /**
     * 服务器存储的文件名
     */
    @TableField("`name`")
    private String name;

    /**
     * 文件种类
     */
    @TableField("`type`")
    private String type;

    /**
     * 文件地址
     */
    @TableField("`path`")
    private String path;

    /**
     * 文件大小，单位：B
     */
    @TableField("`size`")
    private Long size;

    /**
     * 格式化文件大小
     */
    @TableField("`format_size`")
    private String formatSize;

    /**
     * 上传时间
     */
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 上传者的user_id
     */
    @TableField("`user_id`")
    private Integer userId;

    /**
     * 上传者的用户名
     */
    @TableField("`username`")
    private String username;

}
