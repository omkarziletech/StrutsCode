package com.gp.cong.logisoft.edi.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.DateUtils;
import java.util.Date;

public class LogFileWriter {

    public Properties prop = new Properties();

    /**
     * This is a public static method which appends values into a file with message
     *
     *@param messageToAppend - The type of the parameter is String.
     *
     *@return - it's return type is void
     */
    public void doAppend(String messageToAppend, String fileName, String company, String osName, String messageType)
            throws Exception {
        prop.load(getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES));
        String directory = "";
        directory = logFileDirectory(company, osName, messageType);
        File file = new File(directory);
        if (!file.exists()) {
            file.mkdirs();
        }
        BufferedWriter out = new BufferedWriter(new FileWriter(directory + fileName, true));
        out.write(messageToAppend);
        out.write("\n");
        out.close();
    }

    public String logFileDirectory(String company, String osName, String messageType) throws Exception {
        String directory;
        String dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd") .concat("/") ;
        if (messageType.equals("304")) {
            if (osName.contains("linux")) {
                if (company.equals("G")) {
                    directory = prop.getProperty("linuxGtnexusLogFileOut");
                } else {
                    directory = prop.getProperty("linuxInttraLogFileOut");
                }
            } else {
                if (company.equals("G")) {
                    directory = prop.getProperty("gtnexusLogFileOut");
                } else {
                    directory = prop.getProperty("inttraLogFileOut");
                }
            }
        } else if (messageType.equals("997")) {
            if (osName.contains("linux")) {
                if (company.equals("G")) {
                    directory = prop.getProperty("linuxGtnexus997LogFile") + dateFolder;
                } else {
                    directory = prop.getProperty("linuxInttra997LogFile") + dateFolder;
                }
            } else {
                if (company.equals("G")) {
                    directory = prop.getProperty("gtnexus997LogFile") + dateFolder;
                } else {
                    directory = prop.getProperty("inttra997LogFile") + dateFolder;
                }
            }
        } else if (messageType.equals("997Booking")) {
            if (osName.contains("linux")) {
                    directory = prop.getProperty("linuxBookingInttra997LogFile") + dateFolder;
            } else {
                    directory = prop.getProperty("inttraBooking997LogFile") + dateFolder;
            }
        }else if (messageType.equals("300")) {
            if (osName.contains("linux")) {
                    directory = prop.getProperty("linuxInttra300LogFileOut") + dateFolder;
            } else {
                    directory = prop.getProperty("inttra300LogFileOut") + dateFolder;
            }
        }else if (messageType.equals("301")) {
            if (osName.contains("linux")) {
                    directory = prop.getProperty("linuxInttra301LogFile") + dateFolder;
            } else {
                    directory = prop.getProperty("inttra301LogFile") + dateFolder;
            }
        }  else {
            if (osName.contains("linux")) {
                if (company.equals("G")) {
                    directory = prop.getProperty("linuxGtnexus315LogFile") + dateFolder;
                } else {
                    directory = prop.getProperty("linuxInttra315LogFile") + dateFolder;
                }
            } else {
                if (company.equals("G")) {
                    directory = prop.getProperty("gtnexus315LogFile") + dateFolder;
                } else {
                    directory = prop.getProperty("inttra315LogFile") + dateFolder;
                }
            }
        }
        return directory;
    }
}
