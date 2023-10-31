package com.homestay.homestay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.common.Res;
import com.homestay.homestay.entity.HRoom;
import com.homestay.homestay.entity.HRoomImg;
import com.homestay.homestay.service.HRoomImgService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-04 18:13:33
 */

@RestController
@RequestMapping("/h/room/img")
public class HRoomImgController {
    @Resource
    private HRoomImgService hRoomImgService;

    @GetMapping()
    public Res<?> getRoomPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "100") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        if (pageSize > 1000){
            return Res.error("-1","太大了");
        }
        //搜索roomId
        Page<HRoomImg> page = hRoomImgService.getPage(pageNum, pageSize, search);
        return Res.success(page);
    }
    /**
     * 新增操作
     * */
    @PostMapping
    public Res<?> addRoomImg(@RequestBody HRoomImg hRoomImg) {
        boolean b = hRoomImgService.addOne(hRoomImg);
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
    public Res<?> deleteRoomImg(@PathVariable Integer id) {
        boolean b = hRoomImgService.delRoomImg(id);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","删除失败");
        }
    }
    /**
     * 删除操作 ,根据name删除
     */
    @DeleteMapping("/delByName/{name}")
    public Res<?> delByName(@PathVariable String name) {
        boolean b = hRoomImgService.delByName(name);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","删除失败");
        }
    }

    /**
     * 根据Ids批量删除img
     * */
    @PostMapping("/deleteBatch")
    public Res<?> deleteBatch(@RequestBody List<Integer> ids) {
        int i = hRoomImgService.delByIds(ids);
        if (i>0){
            return Res.success();
        }else {
            return Res.error("-1","批量删除失败");
        }
    }
    /**
     * 修改操作
     */
    @PutMapping
    public Res<String> update(@RequestBody HRoomImg hRoomImg){
        boolean b = hRoomImgService.updateById(hRoomImg);
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
    public Res<?> statusHandle(@RequestBody HRoomImg hRoomImg){
        boolean b = hRoomImgService.statusHandle(hRoomImg);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","切换失败");
        }
    }

    /**
     * 根据roomId获取对应的图片列表
     */
    @GetMapping("/imgByRoomId")
    public Res<?> imgByRoomId(@RequestParam Integer roomId){
        List<HRoomImg> list = hRoomImgService.getImgByRoomId(roomId);
        if (list.isEmpty()){
            return Res.error("-1","获取失败");

        }else {
            return Res.success(list);
        }
    }
}

