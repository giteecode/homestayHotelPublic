package com.homestay.homestay.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.entity.HRoom;
import com.homestay.homestay.entity.HRoomImg;
import com.homestay.homestay.entity.HRoomSearch;
import com.homestay.homestay.mapper.HRoomMapper;
import com.homestay.homestay.service.HRoomImgService;
import com.homestay.homestay.service.HRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 房间表 服务实现类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-02 17:18:01
 */
@Service
public class HRoomServiceImpl extends ServiceImpl<HRoomMapper, HRoom> implements HRoomService {
    @Resource
    private HRoomImgService hRoomImgService;
    /**
     * 公共类型搜索方法
     * @param search
     * @param searchType
     * @return LambdaQueryWrapper<HRoom>
     */
    public LambdaQueryWrapper<HRoom> typeSearch(String search, String searchType){
        LambdaQueryWrapper<HRoom> wrapper = Wrappers.<HRoom>lambdaQuery();
//        当serch不是空的时候再进行模糊查询,否则全部查询
        if (StrUtil.isNotBlank(search)) {
//            wrapper.like(HRoom::getCategory, search);
            // 当String searchType不为空时则表示按搜索类型搜索
            if (StrUtil.isNotBlank(searchType)){
                if (searchType.equals("name")){
                    wrapper.like(HRoom::getName,search);
                }else if(searchType.equals("category")){
                    wrapper.like(HRoom::getCategory, search);
                }else if(searchType.equals("device")){
                    wrapper.like(HRoom::getDevice, search);
                }else if(searchType.equals("city")){
                    wrapper.like(HRoom::getCity, search);
                }else {
                    wrapper.like(HRoom::getCategory, search);
                }
            }else {
                wrapper.like(HRoom::getCategory, search);
            }
        }else {
            return wrapper;
        }
        return wrapper;
    }

    /**
     * 分页获取room列表
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    @Override
    public Page<HRoom> getRoomPage(Integer pageNum, Integer pageSize, String search, String searchType) {
//        LambdaQueryWrapper<HRoom> wrapper = Wrappers.<HRoom>lambdaQuery();
////        当serch不是空的时候再进行模糊查询,否则全部查询
//        if (StrUtil.isNotBlank(search)) {
////            wrapper.like(HRoom::getCategory, search);
//            // 当String searchType不为空时则表示按搜索类型搜索
//            if (StrUtil.isNotBlank(searchType)){
//                if (searchType.equals("name")){
//                    wrapper.like(HRoom::getName,search);
//                }else if(searchType.equals("category")){
//                    wrapper.like(HRoom::getCategory, search);
//                }else if(searchType.equals("device")){
//                    wrapper.like(HRoom::getDevice, search);
//                }else if(searchType.equals("city")){
//                    wrapper.like(HRoom::getCity, search);
//                }else {
//                    wrapper.like(HRoom::getCategory, search);
//                }
//            }else {
//                wrapper.like(HRoom::getCategory, search);
//            }
//        }
        LambdaQueryWrapper<HRoom> wrapper = typeSearch(search, searchType);


        Page<HRoom> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        return page;
    }

    /**
     * 新增一个room
     *
     * @param hRoom
     * @return
     */
    @Override
    public boolean addOneHRoom(HRoom hRoom) {
        hRoom.setCreateTime(new Date());
        boolean save = this.save(hRoom);
        return save;
    }

    /**
     * 启用/禁用状态转换
     *
     * @param hRoom
     * @return
     */
    @Override
    public boolean statusHandle(HRoom hRoom) {
        if (hRoom.getStatus() == 0) {
            hRoom.setStatus(1);
        } else {
            hRoom.setStatus(0);
        }
        boolean i = this.updateById(hRoom);
        return i;
    }

    /**
     * 获取空闲状态房间
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @param searchType
     * @return
     */
    @Override
    public Page<HRoom> getFreeRoom(Integer pageNum, Integer pageSize, String search, String searchType) {
        LambdaQueryWrapper<HRoom> wrapper = typeSearch(search, searchType);
        wrapper.eq(HRoom::getStatus,0); //只获取上架状态的房间
        wrapper.eq(HRoom::getState,"空闲");
        Page<HRoom> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        return page;
    }

    /**
     * 获取房间状态数据，用于构建EChart
     * @return
     */
    @Override
    public Map<String, Object> getRoomData() {
        Map<String, Object> resultMap = new HashMap<>();
        String types[] = {"空闲","预订","入住"};
        for (String type : types) {
            LambdaQueryWrapper<HRoom> wrapper = Wrappers.<HRoom>lambdaQuery();
            wrapper.eq(HRoom::getState,type);
            long count = this.count(wrapper);
            resultMap.put(type,count);
        }
        return resultMap;
    }

    /**
     * 获取首页轮播图
     *
     * @param num 获取数量
     * @return
     */
    @Override
    public Page<HRoom> getBanner(Integer num) {
        LambdaQueryWrapper<HRoom> wrapper = Wrappers.<HRoom>lambdaQuery();
        wrapper.eq(HRoom::getStatus,0); //上架状态
        Page<HRoom> page = this.page(new Page<>(0, num), wrapper.orderByDesc(HRoom::getWeight));
        return page;
    }

    /**
     * 前台分页查询，类型搜索、时间条件搜索，仅返回上架状态
     *
     * @param hRoomSearch
     */
    @Override
    public Page<HRoom> searchPage(HRoomSearch hRoomSearch) {
        LambdaQueryWrapper<HRoom> wrapper = typeSearch(hRoomSearch.getSearch(), hRoomSearch.getSearchType());
        wrapper.eq(HRoom::getStatus,0); //上架状态
//        wrapper.eq(HRoom::getState,"空闲").or().eq(HRoom::getEndTime, DateUtil.parse(hRoomSearch.getEndTime()));
        // 用户搜索的起始日期应大于该房间的服务截止日期,即查询：房间服务截止日期<=搜索条件中的开始日期
//        wrapper.le(HRoom::getEndTime, DateUtil.parse(hRoomSearch.getStartTime()));
        if (StrUtil.isNotBlank(hRoomSearch.getStartTime()) && StrUtil.isBlank(hRoomSearch.getSearch())){
//            wrapper.eq(HRoom::getState,"空闲").or().le(HRoom::getEndTime, DateUtil.parse(hRoomSearch.getStartTime()));
//            wrapper.le(HRoom::getEndTime, DateUtil.parse(hRoomSearch.getStartTime())).or().eq(HRoom::getState,"空闲");
            wrapper.and(i -> i.le(HRoom::getEndTime, DateUtil.parse(hRoomSearch.getStartTime()))).or().eq(HRoom::getState,"空闲");

        }
        Page<HRoom> page = this.page(new Page<>(hRoomSearch.getPageNum(), hRoomSearch.getPageSize()), wrapper.orderByDesc(HRoom::getWeight));
        return page;
    }

    /**
     * 根据roomId返回房间详情，以及对应的图片
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getDetail(Integer id) {
        Map<String, Object> map = new HashMap<>();
        HRoom room = this.getById(id);
        if (room != null){
            map.put("room",room);
            List<HRoomImg> img = hRoomImgService.getImgByRoomId(room.getId());
            if (!img.isEmpty()){
                map.put("img",img);
            }
        }
        return map;
    }

    /**
     * 根据民宿名称分页获取room
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page<HRoom> getByName(Integer pageNum, Integer pageSize, String name) {
        LambdaQueryWrapper<HRoom> wrapper = Wrappers.<HRoom>lambdaQuery();
        wrapper.eq(HRoom::getName,name);
        Page<HRoom> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        return page;
    }

    /**
     * 评分操作后对该room进行加权操作
     *
     * @param roomId
     * @param rate
     * @return
     */
    @Override
    public boolean plusWeight(Integer roomId, Integer rate) {
        HRoom room = this.getById(roomId);
        if (room != null){
            room.setWeight(room.getWeight() + rate);
            boolean b = this.updateById(room);
            return b;
        }else {
            return false;

        }
    }




}
