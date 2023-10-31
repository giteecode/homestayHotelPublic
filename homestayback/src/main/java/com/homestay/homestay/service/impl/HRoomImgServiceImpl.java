package com.homestay.homestay.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.entity.HRoom;
import com.homestay.homestay.entity.HRoomImg;
import com.homestay.homestay.entity.SysFile;
import com.homestay.homestay.mapper.HRoomImgMapper;
import com.homestay.homestay.service.HRoomImgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.homestay.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-12-04 18:13:33
 */
@Service
public class HRoomImgServiceImpl extends ServiceImpl<HRoomImgMapper, HRoomImg> implements HRoomImgService {
    @Autowired
    private SysFileService sysFileService;

    /**
     * 分页获取roomImg
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    @Override
    public Page<HRoomImg> getPage(Integer pageNum, Integer pageSize, String search) {
        LambdaQueryWrapper<HRoomImg> wrapper = Wrappers.<HRoomImg>lambdaQuery();
//        当serch不是空的时候再进行模糊查询,否则全部查询
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(HRoomImg::getRoomId,search);
        }
        Page<HRoomImg> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        return page;
    }

    /**
     * 添加一个图片记录
     *
     * @param hRoomImg
     * @return
     */
    @Override
    public boolean addOne(HRoomImg hRoomImg) {
        hRoomImg.setCreateTime(new Date());
        // 获取fileId
        if (StrUtil.isNotBlank(hRoomImg.getUrl())){
            LambdaQueryWrapper<SysFile> wrapper = Wrappers.<SysFile>lambdaQuery();
            wrapper.eq(SysFile::getPath,hRoomImg.getUrl());
            SysFile sysFile = sysFileService.getOne(wrapper);
            if (sysFile != null){
                hRoomImg.setFileId(sysFile.getId());
                hRoomImg.setName(sysFile.getName());
            }
        }
        boolean save = this.save(hRoomImg);
        return save;
    }

    /**
     * 单个民宿图片启用/禁用
     *
     * @param hRoomImg
     * @return
     */
    @Override
    public boolean statusHandle(HRoomImg hRoomImg) {
        if (hRoomImg.getStatus() == 0) {
            hRoomImg.setStatus(1);
        } else {
            hRoomImg.setStatus(0);
        }
        boolean i = this.updateById(hRoomImg);
        return i;
    }

    /**
     * 根据roomId获取对应的展示图片列表
     *
     * @param roomId
     * @return
     */
    @Override
    public List<HRoomImg> getImgByRoomId(Integer roomId) {
        LambdaQueryWrapper<HRoomImg> wrapper = Wrappers.<HRoomImg>lambdaQuery();
        wrapper.eq(HRoomImg::getRoomId,roomId);
        List<HRoomImg> list = this.list(wrapper);
        return list;
    }

    /**
     * 根据roomImg id删除图片，并清理文件
     *
     * @param id
     * @return
     */
    @Override
    public boolean delRoomImg(Integer id) {
        HRoomImg byId = this.getById(id);
        if (byId != null){
            boolean b = sysFileService.delByFileId(byId.getFileId());
            return b;
        }else {
            return false;
        }
    }

    /**
     * 根据文件名删除img
     *
     * @param name
     * @return
     */
    @Override
    public boolean delByName(String name) {
        LambdaQueryWrapper<HRoomImg> wrapper = Wrappers.<HRoomImg>lambdaQuery();
        wrapper.eq(HRoomImg::getName,name);
        HRoomImg hRoomImg = this.getOne(wrapper);
        if (hRoomImg != null){
            boolean b = delRoomImg(hRoomImg.getId());
            return b;
        }else {
            return false;
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public int delByIds(List<Integer> ids) {
        int res = 0;
        if (ids.isEmpty()){
            return 0;
        }else {
            for (Integer id : ids) {
                boolean b = delRoomImg(id);
                if (b){
                    res ++;
                }
            }
        }
        return res;
    }
}
