package com.norman.threadpool;

import java.io.File;

/**
 * 开启客户端
 *
 * @author norman
 * @version 1.0
 */
public final class TestClientMain {


    /**
     * 模拟客户端数量.
     */
    private static final int N_THREADS = 10;

    /**
     * 开启N_THREADS个客户端.
     *
     * @throws InterruptedException 中断异常.
     */
    public static void main(String[] args) throws InterruptedException {

        //文件路径
        final String pathToFile = "/home/norman/ubuntu-14.04.5-desktop-amd64.iso";

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < N_THREADS; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Client.send(new File(pathToFile));
                        }
                    }).start();
                }
            }
        }).start();
    }
}
