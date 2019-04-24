package com.mall.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class FTPUtil {

    //    static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);
    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftuUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        log.info("对不对:{}", PropertiesUtil.getProperty("ftp.server.http.prefix"));
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftuUser, ftpPass);
        log.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("img", fileList);
        log.info("开始连接ftp服务器，结束连接，上传结果:{}", result);
        return result;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fileInputStream = null;
        //连接ftp服务器
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                //切换目录到img
                boolean flag = ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.getRemoteAddress();
                log.info("flag:{}", flag);
                log.info("getRemoteAddress:{}", ftpClient.getRemoteAddress());

                log.info("remotePath:{}", ftpClient.printWorkingDirectory());
                //设置缓存大小
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                //一定要设置二进制文件类型
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //非常重要，设置
                ftpClient.enterLocalPassiveMode();
                //对传入的文件遍历并且储存，通常只有一个
                for (File fileItem : fileList) {
                    fileInputStream = new FileInputStream(fileItem);
                    log.info("进来了:{}", fileItem.getName());
                    ftpClient.storeFile(fileItem.getName(), fileInputStream);
                    log.info("出来了:{}", fileItem.getName());
                }
            } catch (IOException e) {
                log.error("上传文件异常", e);
                uploaded = false;
                e.printStackTrace();
            } finally {
                if (fileInputStream != null)
                    fileInputStream.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    private boolean connectServer(String ip, int port, String user, String pwd) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            log.error("连接ftp服务器异常", e);
            e.printStackTrace();
        }
        return isSuccess;
    }
}
