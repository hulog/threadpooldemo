package com.norman.threadpool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO服务端源码__伪异步I/O
 *
 * @author norman
 * @version 1.0
 */

public final class Server {
    //默认监听的端口号
    private static int DEFAULT_PORT = 12345;
    //单例的ServerSocket
    private static ServerSocket serverSocket;
    //线程池 懒汉式的单例
    private static ExecutorService executorService = Executors.newFixedThreadPool(32);
    private static int count = 0;

    //根据传入参数设置监听端口，如果没有参数调用以下方法并使用默认值
    public static void start() throws IOException {
        //使用默认值
        start(DEFAULT_PORT);
    }

    //这个方法不会被大量并发访问，不太需要考虑效率，直接进行方法同步就行了
    private static synchronized  void start(int port) throws IOException {
        if (serverSocket != null) {
            return;
        }
        try {
            //通过构造函数创建ServerSocket
            //如果端口合法且空闲，服务端就监听成功
            serverSocket = new ServerSocket(port);
            System.out.println("服务器已启动，端口号：" + port);
            Socket socket;
            //通过无线循环监听客户端连接
            //如果没有客户端接入，将阻塞在accept操作上。
            while (true) {
                socket = serverSocket.accept();
                //当有新的客户端接入时，会执行下面的代码
                //然后创建一个新的线程处理这条Socket链路
                System.out.println("服务器已接收到第 " + ++count + " 个任务......");
                executorService.execute(new ServerFileHandler(socket, count));
            }
        } finally {
            //一些必要的清理工作
            if (serverSocket != null) {
                System.out.println("服务器已关闭。");
                serverSocket.close();
                serverSocket = null;
            }
        }
    }
}
