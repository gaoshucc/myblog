package com.funnycode.myblog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author gaoshucc
 * @create 2018-12-09 16:47
 */
@Controller
@RequestMapping("/file")
public class FileUploadController {
    @Value("${web.upload-path}")
    public String FILE_PATH;

    @RequestMapping("/uploadNotePic")
    @ResponseBody
    public JSON uploadNotePic(@RequestParam(value = "editormd-image-file")MultipartFile notePic){
        Logger logger = LoggerFactory.getLogger(FileUploadController.class);
        JSONObject result = new JSONObject();
        if(notePic.isEmpty()){
            result.put("success", 0);
            result.put("message", "上传异常");
        }
        String filename = notePic.getOriginalFilename();
        File file = new File(FILE_PATH + filename);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }
        try {
            logger.info("接受到图片了" + FILE_PATH);
            notePic.transferTo(file);
            result.put("success", 1);
            result.put("message", "上传成功");
            //记得要加上File.separator，这样查找图片时才不会加上上一层的路径，而是从根目录寻找
            result.put("url", File.separator + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
            result.put("success", 0);
            result.put("message", "上传异常");
        }
        return result;
    }
}
