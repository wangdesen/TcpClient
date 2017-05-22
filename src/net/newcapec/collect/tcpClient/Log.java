package net.newcapec.collect.tcpClient;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by ff on 2017/5/15.
 */
public class Log implements Runnable {
    private final int maxFileSize = 6 * 1024 * 1024;
    private String logDir = "";
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS ");
    private final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
    private Queue<String> logQueue = new LinkedList<String>();
    private boolean isRun = false;

    //单态
    private static Log instance = null;
    private static final Object obj = new Object();

    public static Log getInstance() {
        if (null == instance) {
            synchronized (obj) {
                if (null == instance) {
                    instance = new Log();
                }
            }
        }
        return instance;
    }

    private Log() {
    }

    /**
     * 初始化日志路径
     *
     * @param path
     */
    public void start(String path) {
        if (path == null) {
            logDir = System.getProperty("user.dir");
        } else
            logDir = path;
        if (!logDir.endsWith(File.separator)) {
            logDir += File.separator;
        }

        isRun = true;
        Thread logThread = new Thread(this);
        logThread.setDaemon(true);
        logThread.setName("日志线程");
        logThread.start();
    }


    /**
     * 写日志
     *
     * @param message
     */
    public void write(String message) {
        try {
            logQueue.offer(formatter.format(new Date()) + message+"\r\n");
        } catch (Exception e) {
            System.out.print(e.getMessage() + MyException.getStackTrace(e));
        }
    }

    @Override
    public void run() {
        String message;
        while (isRun) {
            try {
                while ((message = logQueue.poll())!=null) {
                    File file = new File(logDir);
                    if (!file.exists()) {
                        file.mkdirs();
                    } else {
                        file = new File(logDir + "log.txt");
                        if (file.length() > maxFileSize) {
                            String newFileName = logDir + formatter1.format(new Date()) + ".bak";

                            file.renameTo(new File(newFileName));
                        }
                    }

                    try (FileWriter writer = new FileWriter(logDir + "log.txt", true)) {
                        writer.write(message);
                        System.out.println(message);
                    } catch (Exception e) {
                        System.out.print(e.getMessage() + MyException.getStackTrace(e));
                    }
                }
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.print(e.getMessage() + MyException.getStackTrace(e));
            }
        }
    }

}
