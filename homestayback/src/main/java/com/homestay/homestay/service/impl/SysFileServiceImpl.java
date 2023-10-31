package com.homestay.homestay.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homestay.homestay.entity.HRoomImg;
import com.homestay.homestay.entity.SysFile;
import com.homestay.homestay.entity.SysUser;
import com.homestay.homestay.mapper.SysFileMapper;
import com.homestay.homestay.service.HRoomImgService;
import com.homestay.homestay.service.SysFileService;
import com.homestay.homestay.service.SysUserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangxilong
 * @since 2022-11-26 09:34:37
 */
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {
    @Resource
    private SysUserService userService;
    @Resource
    @Lazy
    private HRoomImgService hRoomImgService;
    @Override
    public void doUploadSuccess(MultipartFile file, String uuid, String filePath) {
        SysFile sysFile = new SysFile();
        if (file.getSize() > 0) {
            sysFile.setUuid(uuid);
            sysFile.setName(uuid + "_" + file.getOriginalFilename());
            sysFile.setType(file.getContentType());
            sysFile.setPath(filePath);
            sysFile.setSize(file.getSize());
            sysFile.setFormatSize(readableFileSize(file.getSize()));
            sysFile.setCreateTime(new Date());
            //设置操作者id
            Object loginId = StpUtil.getLoginIdDefaultNull();
//            System.out.println("login-id:"+loginId);
            if (loginId != null) {
                sysFile.setUserId(Convert.toInt(loginId));
                SysUser user = userService.getById(Convert.toInt(loginId));
                if (user != null){
                    sysFile.setUsername(user.getUsername());
                }
            } else {
                sysFile.setUsername("未知,未获取到userId");
            }
            this.save(sysFile);
        }
    }

    /**
     * 分页获取文件列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page<SysFile> getSysFileList(Integer pageNum, Integer pageSize,String search) {
        LambdaQueryWrapper<SysFile> wrapper = Wrappers.<SysFile>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(SysFile::getUserId, search);
        }
        Page<SysFile> page = this.page(new Page<>(pageNum, pageSize), wrapper.orderByDesc(SysFile::getId));
        return page;
    }

    /**
     * 清理files（即：不在数据库中记录的files）
     * 丢弃，不再开发使用
     */
    @Override
    public int clearUnUseFiles() {
        int res = 0;
        String filePath = System.getProperty("user.dir") + "/src/main/resources/files/";
        //递归遍历目录以及子目录中的所有文件 如果提供file为文件，直接返回过滤结果
        System.out.println(FileUtil.loopFiles(filePath));
        // 删除文件路径拼接
        String delName = filePath+"1d766441cd284d09a921a6de1810db59_562dec2c1ad1f316c9e4aad6eb58c623.png";
        FileUtil.del(delName);
        //获取目录下所有文件
//        try (Stream<Path> paths = Files.walk(Paths.get(filePath))){
//            List<Path> fileNames = paths
//                    .filter(Files::isRegularFile)
//                    .collect(Collectors.toList());
//            if (!fileNames.isEmpty()){
//                for (Path fileName : fileNames) {
//                    System.out.println(fileName);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return 0;
    }

    /**
     * 根据id删除file，并在对应的目录删除
     * 同时删除roomImg表中的记录
     * @param fileId
     * @return
     */
    @Override
    public boolean delByFileId(Long fileId) {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/files/";
        // 先获取文件名
        SysFile sysFile = this.getById(fileId);
        String delPath = filePath+sysFile.getName();
        // 先删除数据库中的记录，再删除文件
        boolean b = this.removeById(fileId);
        // 同时删除roomImg表中的记录
        LambdaQueryWrapper<HRoomImg> wrapper = Wrappers.<HRoomImg>lambdaQuery();
        wrapper.eq(HRoomImg::getFileId,fileId);
        hRoomImgService.remove(wrapper);
        if (b){
            FileUtil.del(delPath);
        }
        return b;
    }

    @Override
    public int delByFileIds(List<Long> ids) {
        int res = 0;
        if (ids.isEmpty()){
            return 0;
        }else {
            for (Long id : ids) {
                boolean b = delByFileId(id);
                if (b){
                    res ++;
                }
            }
        }
        return res;
    }


    /**
     * 文件大小智能转换
     * 会将文件大小转换为最大满足单位
     *
     * @param size（文件大小，单位为B）
     * @return
     */
    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,###.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 文件大小智能转换
     * 会将文件大小转换为当前单位到最大满足单位（满足GB: 包含B,KB,MB,GB）
     *
     * @param size（文件大小，单位为B）
     * @return
     */
    public static Map<String, BigDecimal> readableFileSizeMap(long size) {
        Map<String, BigDecimal> map = new HashMap<>();
        if (size <= 0) {
            return map;
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        for (int i = 0; i <= digitGroups; i++) {
            map.put(units[i], new BigDecimal(size / Math.pow(1024, i)));
        }
        return map;
    }


}
