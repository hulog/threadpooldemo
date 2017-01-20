package com.norman.threadpool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerFileHandler implements Runnable {
    private final int count;
    private Socket socket;
    private static final String PATH_TO_FILE = "";

    public ServerFileHandler(Socket socket, int count) {
        this.count = count;
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream in = null;
        PrintWriter out = null;
        try {
            in = socket.getInputStream();
            out = new PrintWriter(socket.getOutputStream(), true);

            File dstFile = new File("/home/norman/temp/No." + count + ".iso");
            FileOutputStream fos = new FileOutputStream(dstFile);

            byte[] buf = new byte[4096];
            int len = 0;
            long size = 0L;

            long start = System.currentTimeMillis();
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);
                fos.flush();
                size += len;
            }
            out.println(size);
            out.flush();
            long time = System.currentTimeMillis() - start;
            double vv = size / 1024.0 / 1024 / (time / 1000.0);
            System.out.println("服务器已接受第 "
                    + count
                    + " 个任务对应的文件,\t"
                    + "耗时 "
                    + time / 1000.0
                    + "s,\t 上传速率 "
                    + vv
                    + " MB/s");
            fos.close();
            in.close();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
