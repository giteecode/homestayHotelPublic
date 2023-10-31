package com.homestay.homestay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.homestay.homestay.entity.SysFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-26 09:34:37
 */
public interface SysFileService extends IService<SysFile> {

    /**
     * 文件上传成功做的处理
     * @param file
     * @param uuid
     */
    void doUploadSuccess(MultipartFile file,String uuid,String filePath);

    /**
     * 分页获取文件列表
     * @return
     */
    Page<SysFile> getSysFileList(Integer pageNum, Integer pageSize,String search);

    /**
     * 清理files（即：不在数据库中记录的files）
     * 不再使用！！！
     */
    int clearUnUseFiles();

    /**
     * 根据id删除file，并在对应的目录删除
     * 同时删除roomImg表中的记录
     * @param fileId
     * @return
     */
    boolean delByFileId(Long fileId);

    /**
     * 根据file_ids,进行批量删除
     * @param ids
     * @return
     */
    int delByFileIds(List<Long> ids);
}
