package com.homestay.homestay.entity;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: 张喜龙
 * @date: 2022/12/15 19:24
 * @version: 1.0
 * @Description: 这是room搜索模型，方便接受前端传回的查询类型，只有entity层
 */
@Data
public class HRoomSearch {
    /**
     * 页码
     */
    private Integer pageNum = 1;
    /**
     * 每页数量
     */
    private Integer pageSize = 10;
    /**
     * 搜索内容
     */
    private String search = "";
    /**
     * 搜索类型
     */
    private String searchType = "";
    /**
     * 开始时间
     */
//    String dateStr = "2099-01-1 01:02:03";
//    private Date startTime = DateUtil.parse(dateStr);
    private String startTime = "";

    /**
     * 结束时间
     */
//    private Date endTime= DateUtil.parse(dateStr);
    private String endTime= "";

}
