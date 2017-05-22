package net.newcapec.collect.tcpClient;

import java.io.PrintWriter;
import java.io.StringWriter;
/**
 * Created by ff on 2017/5/15.
 */
public class MyException {
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            t.printStackTrace(pw);
            return String.format("异常描述：%s,异常堆栈：%s",t.getMessage(),sw.toString());
        }
    }
}
