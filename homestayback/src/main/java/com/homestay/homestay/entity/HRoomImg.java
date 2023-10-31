package com.homestay.homestay.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-04 18:13:33
 */
@Data
@TableName("h_room_img")
public class HRoomImg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客房图片id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属房间id
     */
    @TableField("`room_id`")
    private Integer roomId;

    /**
     * 文件id
     */
    @TableField("`file_id`")
    private Long fileId;

    /**
     * 文件名
     */
    @TableField("`name`")
    private String name;

    /**
     * 文件路径
     */
    @TableField("`url`")
    private String url;

    /**
     * 上传时间
     */
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 状态：0启用，1禁用
     */
    @TableField("`status`")
    private Integer status;


}
