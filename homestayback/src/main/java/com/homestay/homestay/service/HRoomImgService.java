package com.homestay.homestay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.entity.HRoomImg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-04 18:13:33
 */
public interface HRoomImgService extends IService<HRoomImg> {

    /**
     * 分页获取roomImg
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    Page<HRoomImg> getPage(Integer pageNum, Integer pageSize, String search);

    /**
     * 添加一个图片记录
     * @param hRoomImg
     * @return
     */
    boolean addOne(HRoomImg hRoomImg);

    /**
     * 单个民宿图片启用/禁用
     * @param hRoomImg
     * @return
     */
    boolean statusHandle(HRoomImg hRoomImg);

    /**
     * 根据roomId获取对应的展示图片列表
     * @param roomId
     * @return
     */
    List<HRoomImg> getImgByRoomId(Integer roomId);

    /**
     * 根据roomImg id删除图片，并清理文件
     * @param id
     * @return
     */
    boolean delRoomImg(Integer id);

    /**
     * 根据文件名删除img
     * @param name
     * @return
     */
    boolean delByName(String name);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int delByIds(List<Integer> ids);
}
