package com.logiware.common.job;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.bc.fcl.EdiTrackingBC;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import com.logiware.datamigration.FilenameFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 *
 * @author Lakshmi Narayanan
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CtsEdiJob implements org.quartz.Job {

    private static final Logger log = Logger.getLogger(CtsEdiJob.class);

    public static void run() throws Exception {
        UserDAO userDAO = new UserDAO();
        EdiTrackingBC ediTrackingBC = new EdiTrackingBC();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        String date = DateUtils.formatDate(new Date(), "MMddyyyyHHmmss");
        String todayDate = DateUtils.formatDate(new Date(), "yyyy/MM/dd");
        String inboundPath = LoadLogisoftProperties.getProperty("edi.xml");
        String archivePath = LoadLogisoftProperties.getProperty("edi.archive") + todayDate;
        String unprocessedPath = LoadLogisoftProperties.getProperty("edi.unprocessed") + todayDate;
        try {
            User user = userDAO.findUserName(CommonConstants.CTS_USER_SYSTEM);
            User systemUser = userDAO.findUserName(CommonConstants.CTS_USER_SYSTEM);
            
            File inboundDir = new File(inboundPath);
            File archiveDir = new File(archivePath);
            if (!archiveDir.exists()) {
                archiveDir.mkdirs();
            }
            File unprocessedDir = new File(unprocessedPath);
            if (!unprocessedDir.exists()) {
                unprocessedDir.mkdirs();
            }
            File[] files = inboundDir.listFiles(new FilenameFilter("xml"));
            List<String> elements = new ArrayList<String>();
            elements.add("TransmissionID");
            elements.add("TransmissionDate");
            elements.add("TransmissionTime");
            elements.add("SenderID");
            elements.add("BookingNumber");
            elements.add("Comment");
            elements.add("Code");
            elements.add("Description");
            elements.add("ProNumber");
            elements.add("POD");
            elements.add("Date");
            elements.add("Time");
            elements.add("Signature");
            StringBuilder remarks = new StringBuilder();
            String elementName = null;
            boolean isUnprocessed = false;
            StringBuilder bkgNo = new StringBuilder();
            for (File file : files) {
                XMLInputFactory factory = XMLInputFactory.newInstance();
                XMLEventReader reader = null;
                InputStream is = null;
                String doorDeliveryStatus = "", PODSigned = "", PODDateTime = "", PODDate = "", ProNumber = "", status ="",doorDelivery ="";
                try {
                    is = new FileInputStream(file);
                    reader = factory.createXMLEventReader(is);
                    while (reader.hasNext()) {
                        XMLEvent event = reader.nextEvent();
                        if (event.isStartElement()) {
                            if (elements.contains(event.asStartElement().getName().toString())) {
                                elementName = event.asStartElement().getName().toString();
                            }
                        } else if (event.isCharacters()) {
                            if (elements.contains(elementName)) {
                                remarks.append(event.asCharacters().getData()).append(" ");
                                if ("BookingNumber".equalsIgnoreCase(elementName)) {
                                    bkgNo.append(event.asCharacters().getData());
                                }
                                // update pickup details from xml data

                                if ("Code".equalsIgnoreCase(elementName)) {
                                    if ("3".equalsIgnoreCase(event.asCharacters().getData()) || "4".equalsIgnoreCase(event.asCharacters().getData())) {
                                        doorDeliveryStatus = "O";
                                        doorDelivery ="Out For Delivery";
                                        status ="N";
                                    } else if ("5".equalsIgnoreCase(event.asCharacters().getData())) {
                                        doorDeliveryStatus = "D";
                                        doorDelivery ="Delivered";
                                    }
                                }

                                if ("ProNumber".equalsIgnoreCase(elementName)) {
                                    ProNumber = event.asCharacters().getData();
                                }

                                if ("Date".equalsIgnoreCase(elementName)) {
                                    PODDate = event.asCharacters().getData();
                                }
                                if ("Time".equalsIgnoreCase(elementName)) {
                                    PODDateTime = PODDate+" "+ event.asCharacters().getData();
                                }
                                if ("Signature".equalsIgnoreCase(elementName)) {
                                    PODSigned = event.asCharacters().getData();
                                }
                            }
                        } else if (event.isEndElement()) {
                            if (elements.contains(event.asEndElement().getName().toString())) {
                                elementName = null;
                            }
                        }
                    }
                    String fileNo = bkgNo.toString().contains("-") ? StringUtils.substringAfter(bkgNo.toString(), "-") : bkgNo.toString();
                    Long fileNumberId = lclFileNumberDAO.getFileIdByFileNumber(fileNo);
                    if (NumberUtils.isNotZero(fileNumberId)) {
                        remarks.setLength(remarks.toString().lastIndexOf(" ") - 1);
                        lclRemarksDAO.insertLclRemarks(fileNumberId, "CTS_EDI", remarks.toString(), user.getUserId());
                        ediTrackingBC.setEdiLogCts(file.getName(), date, "success", "No Error", "C", "997", fileNo, "", "", null);
                        ediTrackingBC.setPickupDetails(fileNumberId, doorDeliveryStatus, ProNumber, PODSigned, PODDateTime, systemUser,status,doorDelivery);
                        File archiveFile = new File(archiveDir, file.getName());
                        if (archiveFile.exists()) {
                            archiveFile.delete();
                        }
                        file.renameTo(archiveFile);
                        isUnprocessed = false;
                    } else {
                        isUnprocessed = true;
                    }
                } catch (Exception e) {
                    log.info("Processing File Name : " + file.getName() + " failed on " + new Date(), e);
                    isUnprocessed = true;
                } finally {
                    if (null != reader) {
                        reader.close();
                    }
                    if (null != is) {
                        is.close();
                    }
                }
                if (isUnprocessed) {
                    try {
                        ediTrackingBC.setEdiLogCts(file.getName(), date, "failure", " Error", "C", "997", StringUtils.substring(bkgNo.toString(), 0, 20), "", "", null);
                    } catch (Exception e) {
                        log.info("Saving Unprocessed File Name : " + file.getName() + " failed on " + new Date(), e);
                    }
                    File unprocessedFile = new File(unprocessedDir, file.getName());
                    if (unprocessedFile.exists()) {
                        unprocessedFile.delete();
                    }
                    file.renameTo(unprocessedFile);
                }
                bkgNo.setLength(0);
                remarks.setLength(0);
            }
            if (null == archiveDir.list() || archiveDir.list().length <= 0) {
                archiveDir.delete();
            }
            if (null == unprocessedDir.list() || unprocessedDir.list().length <= 0) {
                unprocessedDir.delete();
            }
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO dao = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("CTS EDI Job started on " + new Date());
            transaction = dao.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = dao.findByClassName(CtsEdiJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("CTS EDI Job ended on " + new Date());
        } catch (Exception e) {
            log.info("CTS EDI Job failed on " + new Date(), e);
            try {
                Thread.sleep(5000);
                log.info("CTS EDI Job restarted on " + new Date());
                if (null == transaction || !transaction.isActive()) {
                    transaction = dao.getCurrentSession().getTransaction();
                    transaction.begin();
                } else {
                    transaction = dao.getCurrentSession().getTransaction();
                }
                Job job = dao.findByClassName(CtsEdiJob.class.getCanonicalName());
                job.setStartTime(new Date());
                run();
                job.setEndTime(new Date());
                transaction.commit();
                log.info("CTS EDI Job ended on " + new Date());
            } catch (Exception ex) {
                log.info("CTS EDI Job failed again on " + new Date(), ex);
                if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            } finally {
                HibernateSessionFactory.closeSession();
            }
        }
    }
}
