package com.mall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {

    static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);
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
        logger.info("对不对:{}", PropertiesUtil.getProperty("ftp.server.http.prefix"));
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftuUser, ftpPass);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("img", fileList);
        logger.info("开始连接ftp服务器，结束连接，上传结果:{}", result);
        return result;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fileInputStream = null;
        //连接ftp服务器
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                boolean flag = ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.getRemoteAddress();
                logger.info("flag:{}", flag);
                logger.info("getRemoteAddress:{}",ftpClient.getRemoteAddress());

                logger.info("remotePath:{}", ftpClient.printWorkingDirectory());

                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList) {
                    fileInputStream = new FileInputStream(fileItem);
                    logger.info("进来了:{}", fileItem.getName());
                    ftpClient.storeFile(fileItem.getName(), fileInputStream);
                    logger.info("出来了:{}", fileItem.getName());
                }
            } catch (IOException e) {
                logger.error("上传文件异常", e);
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
            logger.error("连接ftp服务器异常", e);
            e.printStackTrace();
        }
        return isSuccess;
    }

    public static String getFtpIp() {
        return ftpIp;
    }

    public static void setFtpIp(String ftpIp) {
        FTPUtil.ftpIp = ftpIp;
    }

    public static String getFtuUser() {
        return ftuUser;
    }

    public static void setFtuUser(String ftuUser) {
        FTPUtil.ftuUser = ftuUser;
    }

    public static String getFtpPass() {
        return ftpPass;
    }

    public static void setFtpPass(String ftpPass) {
        FTPUtil.ftpPass = ftpPass;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
