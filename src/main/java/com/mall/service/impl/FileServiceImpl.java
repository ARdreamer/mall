package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.mall.service.IFileService;
import com.mall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        //获取扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件，文件名为:{},上传的路径为:{},新文件名为:{}", fileName, path, uploadFileName);
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
            logger.info("目录创建了");

        }
        File targetFile = new File(path, uploadFileName);
        try {
            //上传成功了
            file.transferTo(targetFile);
            logger.info("FileServiceImpl上传");
            //已经上传到FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //删除了
            logger.info("FileServiceImpl删除");
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }
        System.err.println("targetFile.getName(:)" + targetFile.getName());
        return targetFile.getName();
    }
}
