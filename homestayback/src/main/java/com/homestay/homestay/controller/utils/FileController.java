package com.homestay.homestay.controller.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.homestay.homestay.common.Res;
import com.homestay.homestay.service.SysFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 张喜龙
 * @Date: 2022/06/26/10:17
 * @Description: 可做工具类，复制到新项目中记得修改端口，ip和文件地址！！
 */
@RestController
@RequestMapping("/files")
public class FileController {
    @Resource
    private SysFileService sysFileService;
//    获取服务器端口
    @Value("${server.port}")
    private String port;

    @Value("${file.ip}")
    private String ip;
//private static final String ip = "172.31.103.131";

//    @Value("${server.port}")
//    private String port;
//
//    @Value("${file.ip}")
//    private String ip;

    /**
     * 文件上传接口
     */
    @CrossOrigin
    @PostMapping("/upload")
    public Res<?> upload(@RequestPart(value = "file") MultipartFile file) throws IOException {  //MultipartFile前面可加@RequestPart("设置的KEY"),required = false非必须传参
        //接收文件的文件名
        String originalFilename = file.getOriginalFilename();
        //定义唯一标识.uuid
        String flag = IdUtil.fastSimpleUUID();
        //获取当前路径并且拼接文件名
//        System.out.println(System.getProperty("user.dir"));
        String rootFilePath = System.getProperty("user.dir") + "/src/main/resources/files/" + flag + "_" + originalFilename;
        //利用hutool工具类直接些写入文件到本地
        FileUtil.writeBytes(file.getBytes(), rootFilePath);
        String filePath = "http://" + ip + ":" + port + "/files/" + flag;
        // 文件上传成功的操作，可以不加
        sysFileService.doUploadSuccess(file,flag,filePath);
        return Res.success(filePath);  // 返回结果 url
    }
    /**
     * 富文本文件上传接口
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/editor/upload")
    public JSON editorUpload(@RequestPart(value = "file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();  // 获取源文件的名称
        // 定义文件的唯一标识（前缀）
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = System.getProperty("user.dir") + "/src/main/resources/files/" + flag + "_" + originalFilename;;  // 获取上传的路径
        File rootFile = new File(rootFilePath);
        if (!rootFile.getParentFile().exists()) {
            rootFile.getParentFile().mkdirs();
        }
        FileUtil.writeBytes(file.getBytes(), rootFilePath);  // 把文件写入到上传的路径
        String url = "http://" + ip + ":" + port + "/files/" + flag;
        JSONObject json = new JSONObject();
        json.set("errno", 0);
        JSONArray arr = new JSONArray();
        JSONObject data = new JSONObject();
        arr.add(data);
        data.set("url", url);
        json.set("data", arr);
        return json;  // 返回结果 url
    }
    /**
     * 文件下载接口*/
    @GetMapping("/{flag}")
    public void getFiles(HttpServletResponse response,@PathVariable String flag){
        OutputStream os; //新建一个输出流对象
        String basePath = System.getProperty("user.dir") + "/src/main/resources/files/";
        //获取根路径所有文件名
        List<String> fileNames = FileUtil.listFileNames(basePath);
        //找到跟参数一致的文件
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(basePath + fileName);  // 通过文件的路径读取文件字节流
                os = response.getOutputStream();   // 通过输出流返回文件
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }



}
