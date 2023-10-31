package com.homestay.homestay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.entity.HRoom;
import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.homestay.entity.HRoomSearch;
import com.homestay.homestay.entity.HUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 房间表 服务类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-02 17:18:01
 */
public interface HRoomService extends IService<HRoom> {

    /**
     * 分页获取room列表
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    Page<HRoom> getRoomPage(Integer pageNum, Integer pageSize, String search, String searchType);

    /**
     * 新增一个room
     * @param hRoom
     * @return
     */
    boolean addOneHRoom(HRoom hRoom);


    /**
     * 启用/禁用状态转换
     *
     * @return
     */
    boolean statusHandle(HRoom hRoom);

    /**
     * 获取空闲状态房间，并且为上架状态
     * @param pageNum
     * @param pageSize
     * @param search
     * @param searchType
     * @return
     */
    Page<HRoom> getFreeRoom(Integer pageNum, Integer pageSize, String search, String searchType);


    /**
     * 获取房间状态数据，用于构建EChart
     * @return
     */
    Map<String, Object> getRoomData();

    /**
     * 获取首页轮播图
     * @param num 获取数量
     * @return
     */
    Page<HRoom> getBanner(Integer num);

    /**
     * 前台分页查询，类型搜索、时间条件搜索，仅返回上架状态
     */
    Page<HRoom> searchPage(HRoomSearch hRoomSearch);

    /**
     * 根据roomId返回房间详情，以及对应的图片
     * @param id
     * @return
     */
    Map<String, Object> getDetail(Integer id);


    /**
     * 根据民宿名称分页获取room
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    Page<HRoom> getByName(Integer pageNum, Integer pageSize, String name);

    /**
     * 评分操作后对该room进行加权操作
     * @param roomId
     * @param rate
     * @return
     */
    boolean plusWeight(Integer roomId, Integer rate);
}
