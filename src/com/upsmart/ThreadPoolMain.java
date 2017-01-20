package com.upsmart;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by upsmart on 16-12-6.
 */
public class ThreadPoolMain {

    /**
     * 测试主入口.
     */
    public static void main(String[] args) {

        // BlockingQueue queue = new LinkedBlockingDeque();
        BlockingQueue queue = new ArrayBlockingQueue(3);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 6, 100, TimeUnit.SECONDS, queue,
                // new ThreadPoolExecutor.DiscardPolicy()
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable handler, ThreadPoolExecutor executor) {
                        System.out.println(Thread.currentThread().getName() + " 太多了，在我这呢...");
                    }
                }
        );

        for (int i = 0; i < 8; i++) {
            doTest(executor, i + 1);
        }
    }

    private static void doTest(ThreadPoolExecutor ex, final int num) {

        ex.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 打印中...====>对应第" + num + "个");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
