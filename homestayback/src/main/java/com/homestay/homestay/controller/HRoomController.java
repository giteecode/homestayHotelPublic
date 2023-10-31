package com.homestay.homestay.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.common.Res;
import com.homestay.homestay.entity.HRoom;
import com.homestay.homestay.entity.HRoomSearch;
import com.homestay.homestay.service.HRoomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;


/**
 * <p>
 * 房间表 前端控制器
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-02 17:18:01
 */

@RestController
@RequestMapping("/h/room")
public class HRoomController {
    @Resource
    private HRoomService hRoomService;
    /**
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @return 民宿房间列表 ，包含未上架状态
     */
    @GetMapping()
    public Res<?> getRoomPage(@RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize,
                           @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") String searchType){
        if (pageSize > 1000){
            return Res.error("-1","太大了");
        }
        // search：默认民宿类型
        Page<HRoom> page = hRoomService.getRoomPage(pageNum, pageSize, search, searchType);
        return Res.success(page);
    }
    /**
     * 仅获取空闲状态房间,并且为上架状态
     */
    @GetMapping("/getFreeRoom")
    public Res<?> getFreeRoom(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") String searchType){
        Page<HRoom> page = hRoomService.getFreeRoom(pageNum, pageSize, search, searchType);
        if (ObjectUtil.isNotEmpty(page)){
            return Res.success(page);
        }else {
            return Res.error("-1","获取失败");
        }
    }

    /**
     * 前台分页查询，类型搜索、时间条件搜索，仅返回上架状态
     */
    @GetMapping("/searchPage")
    public Res<?> getFreeRoom(HRoomSearch hRoomSearch){
        Page<HRoom> page = hRoomService.searchPage(hRoomSearch);
        if (ObjectUtil.isNotEmpty(page)){
            return Res.success(page);
        }else {
            return Res.error("-1","获取失败");
        }
    }
    /**
     * 根据roomId返回房间详情，以及对应的图片
     *
     */
    @GetMapping("/detail")
    public Res<?> getDetail(@RequestParam Integer id){
        Map<String, Object> resultMap = hRoomService.getDetail(id);
        if (ObjectUtil.isNotEmpty(resultMap)){
            return Res.success(resultMap);
        }else {
            return Res.error("-1","获取失败");
        }
    }
    /**
     * 新增操作
     * */
    @PostMapping
    public Res<?> addHRoom(@RequestBody HRoom hRoom) {
        boolean b = hRoomService.addOneHRoom(hRoom);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","添加失败");
        }
    }

    /**
     * 删除操作
     */
    @DeleteMapping("/{id}")
    public Res<?> deleteHRoom(@PathVariable Long id) {
        boolean b = hRoomService.removeById(id);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","删除失败");
        }
    }

    /**
     * 修改操作
     */
    @PutMapping
    public Res<?> update(@RequestBody HRoom hRoom){
        hRoom.setUpdateTime(new Date());
        boolean b = hRoomService.updateById(hRoom);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","更新失败");
        }
    }

    /**
     * 状态切换控制
     */
    @PutMapping("/status")
    public Res<?> statusHandle(@RequestBody HRoom hRoom){
        boolean b = hRoomService.statusHandle(hRoom);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","切换失败");
        }
    }



    /**
     * 根据roomId返回对象
     */
    @GetMapping("/getById")
    public Res<?> getById(@RequestParam Integer roomId){
        HRoom room = hRoomService.getById(roomId);
        if (room != null){
            return Res.success(room);
        }else {
            return Res.error("-1","获取失败");
        }
    }

    /**
     * 获取房间状态数据，用于构建EChart
     * @return
     */
    @GetMapping("/roomData")
    public Res<?> registerData(){
        Map<String, Object> resultMap = hRoomService.getRoomData();
        return Res.success(resultMap);
    }

    /**
     * 获取首页轮播图
     */
    @GetMapping("/getBanner")
    public Res<?> getBanner(@RequestParam(defaultValue = "6") Integer num){
        if (num <0 || num>10){return Res.error("-1","数量超出范围（0-10）");}
        Page<HRoom> list = hRoomService.getBanner(num);
        return Res.success(list);
    }

    /**
     * 根据民宿名称分页获取room
     */
    @GetMapping("/byName")
    public Res<?> getByName(@RequestParam String name,
                            @RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize){
        if (pageSize <0 || pageSize > 100){return Res.error("-1","数量超出范围（0-100）");}
        Page<HRoom> list = hRoomService.getByName(pageNum,pageSize,name);
        if (ObjectUtil.isNotEmpty(list)){
            return Res.success(list);
        }else {
            return Res.error("-1","获取失败");
        }
    }


}

