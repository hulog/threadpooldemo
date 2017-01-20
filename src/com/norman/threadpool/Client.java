package com.norman.threadpool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 客户端实体类.
 *
 * @author norman
 * @version 1.0
 */
public class Client {
    /**
     * 发送文件.
     *
     * @param file 要进行传输的文件
     */
    public static void send(File file) {
        String  defaultServerIp = "192.168.199.101";
        int  defaultServerPort = 12345;
        send(defaultServerIp, defaultServerPort, file);
        return;
    }

    /**
     * 开始发送文件.
     *
     * @param ip   ip address
     * @param port port
     * @param file 文件对象
     */
    public static void send(String ip, int port, File file) {
        if (!file.exists()) {
            System.out.println("文件不存在");
            return;
        }
        if (port < 0 || port > 65535) {
            System.out.println("端口号错误");
            return;
        }

        System.out.println("文件>>>>"
                + file.getName()
                + ",正在上传......");
        Socket socket = null;
        BufferedReader in = null;
        OutputStream out = null;
        try {
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = socket.getOutputStream();
            FileInputStream fis = new FileInputStream(file);

            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = fis.read(buf)) != -1) {
                out.write(buf, 0, len);
                out.flush();
            }
            fis.close();
            socket.shutdownOutput();

            //接受服务器传回的消息
            String temp;
            long reply = 0;
            while ((temp = in.readLine()) != null) {
                try {
                    reply = Long.parseLong(temp);
                } catch (Exception ex) {
                    System.out.println("数字解析出错");
                }
                System.out.println("客户端收到服务器响应: "
                            + temp
                            + " ,文件上传成功率: "
                            + reply / file.length() / 1.0 * 100
                            + " % ");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
