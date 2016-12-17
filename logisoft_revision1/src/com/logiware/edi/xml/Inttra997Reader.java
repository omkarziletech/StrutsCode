package com.logiware.edi.xml;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.edi.util.LogFileWriter;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Balaji.E
 */
public class Inttra997Reader {

    private static final String osName = osName();

    public void BookingAckReader() throws Exception {
        StringBuilder logBuilder = new StringBuilder();
        Properties prop = new Properties();
        prop.load(Inttra997Reader.class.getResourceAsStream(CommonConstants.EDIPROPERTIES));
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        String dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd").concat("/");
        String archivePath = osName.contains("linux") ? "linuxBookingInttra997Archive" : "inttraBooking997Archive";
        String sourcePath = osName.contains("linux") ? "linuxBookingInttra997Directory" : "inttraBooking997Directory";
        String unprocessedPath = osName.contains("linux") ? "linuxBookingInttra997Unprocessed" : "inttraBooking997Unprocessed";
        File unProcessed = new File(prop.getProperty(unprocessedPath));
        File sourceFile = new File(prop.getProperty(sourcePath));
        File archive = new File(prop.getProperty(archivePath).concat(dateFolder));
        if (!archive.exists()) {
            archive.mkdirs();
        }
        if (!unProcessed.exists()) {
            unProcessed.mkdir();
        }
        for (File file : sourceFile.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".xml")) {
                try {
                    JAXBContext jAXBContext = JAXBContext.newInstance(Inttra997Data.class);
                    Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
                    Inttra997Data ediBookingAck = (Inttra997Data) unmarshaller.unmarshal(file);
                    logFileEdiDAO.saveBooking997(ediBookingAck, file.getName());
                    //move the files to Archive
                    if (file.exists()) {
                    file.renameTo(new File(archive.getPath().concat(File.separator).concat(file.getName())));
                    file.deleteOnExit();
                    }
                } catch (Exception e) {
                    //move the files to Un Proccessed
                    if (file.exists()) {
                    file.renameTo(new File(unProcessed.getPath().concat(File.separator).concat(file.getName())));
                    file.deleteOnExit();
                    }
                    logBuilder.append("Error In Reading Files ").append(file.getName()).append(" ").append(e.toString());
                    new LogFileWriter().doAppend(logBuilder.toString(), file.getName(), "I", osName, "booking997");
                    logBuilder.setLength(0);
                }
            }
        }
    }

    private static String osName() {
        return System.getProperty("os.name").toLowerCase();
    }

}
      