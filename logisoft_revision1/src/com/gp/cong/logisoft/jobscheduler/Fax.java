package com.gp.cong.logisoft.jobscheduler;

import com.gp.cong.struts.LoadLogisoftProperties;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class Fax {

    private static final Logger log = Logger.getLogger(Fax.class);

    public static String sendFax(String reportFile, String fileLocation, String tagFile) throws Exception {
        Process process = null;
        Runtime runTime = null;
        InputStream inputStream = null;
        InputStream errorStream = null;
        String commandOutput = null;
        String errorOutput = null;
        int c = 0;
        try {
            runTime = Runtime.getRuntime();
            //copy fax file
            String[] cmdarray = new String[]{"cp", reportFile, LoadLogisoftProperties.getProperty("vsifax_path")};
            process = runTime.exec(cmdarray);
            log.info(cmdarray[0] + " " + cmdarray[1] + " " + cmdarray[2]);
            inputStream = process.getInputStream();
            errorStream = process.getErrorStream();
            commandOutput = "";
            c = 0;
            while ((c = inputStream.read()) != -1) {
                commandOutput = commandOutput + (char) c;
            }
            log.info("CommandOutput: " + commandOutput);
            inputStream.close();
            errorOutput = "";
            while ((c = errorStream.read()) != -1) {
                errorOutput = errorOutput + (char) c;
            }
            log.info("ErrorOutput: " + errorOutput);
            errorStream.close();
            closeStream(process);
            //copy tag file
            process = runTime.exec("cp " + fileLocation + "" + tagFile + " " + LoadLogisoftProperties.getProperty("vsifax_path"));
            log.info("cp " + fileLocation + "" + tagFile + " " + LoadLogisoftProperties.getProperty("vsifax_path"));
            inputStream = process.getInputStream();
            errorStream = process.getErrorStream();
            commandOutput = "";
            c = 0;
            while ((c = inputStream.read()) != -1) {
                commandOutput = commandOutput + (char) c;
            }
            log.info("CommandOutput: " + commandOutput);
            inputStream.close();
            errorOutput = "";
            while ((c = errorStream.read()) != -1) {
                errorOutput = errorOutput + (char) c;
            }
            log.info("ErrorOutput: " + errorOutput);
            errorStream.close();
            closeStream(process);
            //send fax
            //delay for some time
            for (int i = 0; i <= 30; i++) {
            }
            process = runTime.exec(LoadLogisoftProperties.getProperty("vsifax_bin") + " " + LoadLogisoftProperties.getProperty("faxfile_path") + "/" + tagFile);
            log.info(LoadLogisoftProperties.getProperty("vsifax_bin") + " " + LoadLogisoftProperties.getProperty("faxfile_path") + "/" + tagFile);
            inputStream = process.getInputStream();
            errorStream = process.getErrorStream();
            commandOutput = "";
            c = 0;
            while ((c = inputStream.read()) != -1) {
                commandOutput = commandOutput + (char) c;
            }
            log.info("CommandOutput: " + commandOutput);
            inputStream.close();
            errorOutput = "";
            while ((c = errorStream.read()) != -1) {
                errorOutput = errorOutput + (char) c;
            }
            log.info("ErrorOutput: " + errorOutput);
            errorStream.close();
            closeStream(process);
            log.info("Available Processors: " + runTime.availableProcessors());
            return !errorOutput.trim().isEmpty() ? "error" : commandOutput;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("sendFax failed on " + new Date(), e);
            return "error";
        } finally {
            try {
                if (null != process) {
                    if (null != process.getInputStream()) {
                        process.getInputStream().close();
                    }

                    if (null != process.getOutputStream()) {
                        process.getOutputStream().close();
                    }

                    if (null != process.getErrorStream()) {
                        process.getErrorStream().close();
                    }
                    process.destroy();
                }
            } catch (Exception ex) {
                log.info("sendFax failed on " + new Date(), ex);
            }
        }
    }

    private static void closeStream(Process p) {
        try {
            if (null != p) {
                if (null != p.getInputStream()) {
                    p.getInputStream().close();
                }

                if (null != p.getOutputStream()) {
                    p.getOutputStream().close();
                }

                if (null != p.getErrorStream()) {
                    p.getErrorStream().close();
                }
            }
        } catch (Exception ex) {
            log.info("closeStream failed on " + new Date(), ex);
        }
    }

    public static void createTagFile(String fileContent, String fileName) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(fileName));
            out.write(fileContent);
            out.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.info("createTagFile failed on " + new Date(), e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (Exception ex) {
                    log.info("createTagFile failed on " + new Date(), ex);
                }
            }
        }
    }

    public static String checkStatus(String responseCode) throws Exception {
        Process process = null;
        Runtime runTime = null;
        InputStream inputStream = null;
        InputStream errorStream = null;
        try {
            runTime = Runtime.getRuntime();
            process = runTime.exec(LoadLogisoftProperties.getProperty("vsifax_status") + " " + responseCode);
            log.info(LoadLogisoftProperties.getProperty("vsifax_status") + " " + responseCode);
            inputStream = process.getInputStream();
            errorStream = process.getErrorStream();
            String statusOutput = IOUtils.toString(inputStream);
            if (null != statusOutput && statusOutput.contains("Result : done")) {
                statusOutput = "Completed";
            } else {
                statusOutput = "Failed";
            }
            log.info("StatusOutput: " + statusOutput);
            IOUtils.closeQuietly(inputStream);
            String errorOutput = IOUtils.toString(errorStream);
            log.info("ErrorOutput: " + statusOutput);
            IOUtils.closeQuietly(errorStream);
            closeStream(process);
            log.info("Available Processors: " + runTime.availableProcessors());
            return !errorOutput.trim().isEmpty() ? "Failed" : statusOutput;
        } catch (IOException e) {
            log.info("checkStatus failed on " + new Date(), e);
            return "Failed";
        } finally {
            try {
                if (null != process) {
                    if (null != process.getInputStream()) {
                        process.getInputStream().close();
                    }

                    if (null != process.getOutputStream()) {
                        process.getOutputStream().close();
                    }

                    if (null != process.getErrorStream()) {
                        process.getErrorStream().close();
                    }
                    process.destroy();
                }
            } catch (Exception ex) {
                log.info("Error in Fax.java in checkStatus method", ex);
            }
        }
    }
}
