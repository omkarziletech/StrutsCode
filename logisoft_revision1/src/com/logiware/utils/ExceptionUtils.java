package com.logiware.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ExceptionUtils {

    /**
     * getStackTrace - get full stack trace from throwable
     * @param throwable
     * @return 
     */
    public static String getStackTrace(Throwable throwable) throws Exception {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        String stackTrace = writer.toString();
        writer.close();
        printWriter.close();
        return stackTrace;
    }
}
