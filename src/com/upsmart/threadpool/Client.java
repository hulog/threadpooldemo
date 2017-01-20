package com.upsmart.threadpool;

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
     * 默认服务器端口号.
     */
    private static int DEFAULT_SERVER_PORT = 12345;
    /**
     * 默认服务器ip地址.
     */
    private static String DEFAULT_SERVER_IP = "192.168.199.101";

    /**
     * 发送文件.
     *
     * @param file 要进行传输的文件
     */
    public static void send(File file) {
        send(DEFAULT_SERVER_IP, DEFAULT_SERVER_PORT, file);
        return;
    }

    /**
     * 开始发送文件.
     *
     * @param ip   ip address
     * @param port port
     * @param file 文件类
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

            String temp;
            while ((temp = in.readLine()) != null) {
                try {
                    long reply = Long.parseLong(temp);
                    System.out.println("客户端收到服务器响应: "
                            + temp
                            + " ,文件上传成功率: "
                            + reply / file.length() / 1.0 * 100
                            + " % ");
                } catch (Exception ex) {
                    System.out.println("数字解析出错");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
