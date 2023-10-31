package com.homestay.homestay.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestay.homestay.common.Res;
import com.homestay.homestay.entity.SysFile;
import com.homestay.homestay.service.SysFileService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-26 09:34:37
 */

@RestController
@RequestMapping("/sys/file")
public class SysFileController {
    @Resource
    private SysFileService sysFileService;
    /**
     * 分页获取文件列表
     */
    @GetMapping("/pageFiles")
    public Res<?> getLoginTimes(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "100") Integer pageSize,
                                @RequestParam(defaultValue = "") String search){
        Page<SysFile> sysFileList = sysFileService.getSysFileList(pageNum,pageSize,search);
        if(sysFileList != null){
            return Res.success(sysFileList);
        }else {
            return Res.error("-1","获取files失败");
        }
    }
    /**
     * 根据imgIds批量删除img
     * */
    @PostMapping("/deleteBatch")
    public Res<?> deleteBatch(@RequestBody List<Long> ids) {
        int i = sysFileService.delByFileIds(ids);
        if (i>0){
            return Res.success();
        }else {
            return Res.error("-1","批量删除失败");
        }
    }
    @DeleteMapping("/{id}")
    public Res<?> deleteById(@PathVariable Long id) {
        boolean b = sysFileService.delByFileId(id);
        if (b){
            return Res.success();
        }else {
            return Res.error("-1","删除失败");
        }
    }
    /**
     * 清理冗余
     * 不再使用！！！
     */
    @GetMapping("/clear")
    public Res<?> clearFiles(){
        sysFileService.clearUnUseFiles();
        return Res.success();
    }
}

