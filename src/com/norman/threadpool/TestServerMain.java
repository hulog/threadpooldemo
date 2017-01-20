package com.norman.threadpool;

import java.io.IOException;

/**
 * 测试方法
 *
 * @author norman
 * @version 1.0
 */
public class TestServerMain {
    /**
     * 测试主方法.
     */
    public static void main(String[] args) throws InterruptedException {
        // 运行服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Server.start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
}