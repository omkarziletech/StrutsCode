package com.logiware.common.job;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.jobscheduler.Print;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import javax.print.PrintService;
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
public class PrintJob implements org.quartz.Job, ConstantsInterface {

    private static final Logger log = Logger.getLogger(PrintJob.class);

    public void run() throws Exception {
        try {
            EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
            LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
            PortsDAO portsDAO = new PortsDAO();
            List<EmailSchedulerVO> pendingPrints = emailschedulerDAO.getPendingPrints();
            String commonPrinter = LoadLogisoftProperties.getProperty("printrName");
            String labelPrinter = LoadLogisoftProperties.getProperty("labelPrinterName");
            for (EmailSchedulerVO print : pendingPrints) {
                try {
                    if (CommonUtils.isEqualIgnoreCase(print.getType(), CONTACT_MODE_PRINT)) {
                        String printer = CommonUtils.isNotEmpty(print.getPrinterName()) ? print.getPrinterName() : commonPrinter;
                        //If tray is selected then use CUPS command to print,since java print API has issue with Medai Tray
                        if("Barrel D/R".equalsIgnoreCase(print.getName()) || CommonUtils.isNotEmpty(print.getPrinterTray())){
                            String responseCode = Print.printInSelectedTray(print.getFileLocation(),printer, print.getPrintCopy(), print.getPrinterTray());
                            if (null == responseCode || responseCode.trim().equals("error")) {
                                emailschedulerDAO.updateStatus(print.getId(), "Failed", responseCode);
                                log.info("Print on Selected Tray Job failed " + responseCode);
                            } else {
                                emailschedulerDAO.updateStatus(print.getId(), "Completed", responseCode);
                                log.info("Print on Selected Tray Job Success " + responseCode);
                            }
                        }else{
                            PrintService[] services = PrinterJob.lookupPrintServices();
                            PrintService printService = null;
                            for (PrintService service : services) {
                                if (CommonUtils.isEqualIgnoreCase(printer, service.getName())) {
                                    printService = service;
                                    break;
                                }
                            }
                            if (null == printService) {
                                emailschedulerDAO.updateStatus(print, "InvalidPrinter");
                            } else if (CommonUtils.isEqualIgnoreCase(print.getModuleName(), "APPAYMENT")) {
                                if (Print.printCheck(printer, print.getFileLocation())) {
                                    emailschedulerDAO.updateStatus(print, "Completed");
                                } else {
                                    emailschedulerDAO.updateStatus(print, "PrintError");
                                }
                            } else if (Print.printFile(printer, print.getFileLocation(), print.getPrintCopy(), print.getPrinterTray())) {
                                emailschedulerDAO.updateStatus(print, "Completed");
                            } else {
                                emailschedulerDAO.updateStatus(print, "PrintError");
                            }
                        }
                    } else if (CommonUtils.isEqualIgnoreCase(print.getType(), CONTACT_MODE_LABEL_PRINT)) {
                        Long fileId = print.getModuleReferenceId();
                        String[] file = lclFileNumberDAO.getLabelPrintDataByFileNumber(fileId);
                        if (null != file) {
                            String companyName = LoadLogisoftProperties.getProperty("ECU".equalsIgnoreCase(file[8]) ? "application.ECU.companyname"
                                    : "OTI".equalsIgnoreCase(file[8]) ? "application.OTI.companyname" : "application.Econo.companyname");
                            String companyWebsite = LoadLogisoftProperties.getProperty(file[8].equalsIgnoreCase("ECU") ? "application.ECU.website"
                                    : file[8].equalsIgnoreCase("OTI") ? "application.OTI.website" : "application.Econo.website");
                            companyWebsite = "http://" + companyWebsite;
                            String company = "ECU".equalsIgnoreCase(file[8]) ? "ECU WorldWide"
                                    : "OTI".equalsIgnoreCase(file[8]) ? "OTI Cargo,Inc" : "Econocaribe";

                            StringBuilder labelBuilder = new StringBuilder();
                            labelBuilder.append("^XA");
                            labelBuilder.append("^PR5");
                            labelBuilder.append("^FO740,100^A0R,40,43^FD").append(companyName).append("^FS");
                            labelBuilder.append("^FO740,720^A0R,25,31^FD").append(companyWebsite).append("^FS");
                            labelBuilder.append("^FO730,50^GB0,1090,3^FS");
                            if (!file[0].startsWith("US") || "T".equalsIgnoreCase(file[3])) {
                                if (CommonUtils.isNotEmpty(file[9])) {
                                    labelBuilder.append("^FO450,60^A0R,175,60^FD").append(StringUtils.left(file[9], 25)).append("^FS"); //CFCL Acct Contain
                                } else if (file[1].length() <= 15) {
                                    labelBuilder.append("^FO540,60^A0R,175,100^FD").append(file[1]).append("^FS"); //Destination
                                } else {
                                    labelBuilder.append("^FO540,60^A0R,175,80^FD").append(StringUtils.left(file[1], 20)).append("^FS"); //Destination
                                }
                                if (CommonUtils.isEmpty(file[9])) {
                                    if (file[2].length() <= 15) {
                                        labelBuilder.append("^FO385,60^A0R,175,100^FD").append(file[2]).append("^FS"); //Country
                                    } else {
                                        labelBuilder.append("^FO385,60^A0R,175,80^FD").append(StringUtils.left(file[2], 20)).append("^FS"); //Country
                                    }
                                }
                            } else {
                                if (CommonUtils.isNotEmpty(file[9])) {
                                    labelBuilder.append("^FO540,60^A0R,175,60^FD").append(file[9]).append("^FS"); //CFCL Acct Contain
                                } else if (file[1].length() <= 15) {
                                    labelBuilder.append("^FO540,60^A0R,175,100^FD").append(file[1]).append(", ").append(file[2]).append("^FS"); //Destination, State
                                } else {
                                    labelBuilder.append("^FO540,60^A0R,175,75^FD").append(StringUtils.left(file[1], 20)).append(", ").append(file[2]).append("^FS"); //Destination, State
                                }
                            }
                            String laneValue = "";
                            if (!file[3].equalsIgnoreCase("I")) {
                                String eciPortCode = lclFileNumberDAO.getUnCodeForLabelPrint(Long.parseLong(file[7]));
                                if (null != eciPortCode && !"".equalsIgnoreCase(eciPortCode)) {
                                    laneValue = portsDAO.getLaneValue(eciPortCode);
                                }
                            }
                            labelBuilder.append("^FO580,972^A0R,140,99^FD").append(laneValue).append("^FS"); //Lane Value
                            labelBuilder.append("^FO390,935^BQN,2,10^FDQA,").append(file[5]).append("^FS,"); //QR Code and File Number
                            //  labelBuilder.append("^FO370,935^GB240,220,10^FS");
                            //labelBuilder.append("^FO330,940^A0R,240,180^FD").append(file[4]).append("^FS"); //Terminal
                            labelBuilder.append("^FO10,60^A0R,375,275^FD").append(file[5]).append("^FS"); //File Number
                            labelBuilder.append("^FO285,940^A0R,90,78^FD").append(file[4]).append("^FS"); //Poo Unlocode
                            if (file[6].length() <= 3) {
                                labelBuilder.append("^FO60,955^A0R,240,180^FD").append(file[6]).append("^FS"); //Piece Count
                            } else if (file[6].length() == 4) {
                                labelBuilder.append("^FO60,940^A0R,180,140^FD").append(file[6]).append("^FS"); //Piece Count
                            } else {
                                labelBuilder.append("^FO60,940^A0R,160,120^FD").append(file[6]).append("^FS"); //Piece Count
                            }
                            labelBuilder.append("^FO20,126^ADR,36,10^FDThank you for shipping with ").append(company).append("^FS");
                            labelBuilder.append("^FO20,610^ADR,36,10^FD - Gracias Por Embarcar Con ").append(company).append("^FS");
                            labelBuilder.append("^PQ001");
                            labelBuilder.append("^XZ");
                            String printer = CommonUtils.isNotEmpty(print.getPrinterName()) ? print.getPrinterName() : labelPrinter;
                            PrintService[] services = PrinterJob.lookupPrintServices();
                            PrintService printService = null;
                            for (PrintService service : services) {
                                if (CommonUtils.isEqualIgnoreCase(printer, service.getName())) {
                                    printService = service;
                                    break;
                                }
                            }
                            if (null == printService) {
                                emailschedulerDAO.updateStatus(print, "InvalidPrinter");
                            } else {
                                File temp = File.createTempFile(print.getModuleId(), ".txt");
                                FileOutputStream fis = null;
                                try {
                                    fis = new FileOutputStream(temp);
                                    fis.write(labelBuilder.toString().getBytes());
                                    fis.flush();
                                    String responseCode = Print.labelPrint(temp.getAbsolutePath(), printer, print.getPrintCopy());
                                    if (null == responseCode || responseCode.trim().equals("error")) {
                                        emailschedulerDAO.updateStatus(print.getId(), "Failed", responseCode);
                                        log.info("Label Print Job failed " + responseCode);
                                    } else {
                                        emailschedulerDAO.updateStatus(print.getId(), "Completed", responseCode);
                                        log.info("Label Print Job Success " + responseCode);
                                    }
                                } catch (FileNotFoundException e) {
                                    log.info("Label Print Job failed ", e);
                                    emailschedulerDAO.updateStatus(print, "File Not Found");
                                } finally {
                                    if (null != fis) {
                                        fis.close();
                                    }
                                    temp.deleteOnExit();
                                }
                            }
                        } else {
                            emailschedulerDAO.updateStatus(print, "File Not Found");
                        }
                    }
                } catch (Exception e) {
                    log.info(print.getType() + " Job failed ", e);
                    emailschedulerDAO.updateStatus(print, "Error");
                }
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
            log.info("Print Job started on " + new Date());
            transaction = new EmailschedulerDAO().getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();

            }
            Job job = dao.findByClassName(PrintJob.class
                    .getCanonicalName());
            job.setStartTime(
                    new Date());
            run();

            job.setEndTime(
                    new Date());
            transaction.commit();

            log.info(
                    "Print Job ended on " + new Date());
        } catch (Exception e) {
            log.info("Print Job failed on " + new Date(), e);
            if (null != transaction && transaction.isActive() && dao.getCurrentSession().isConnected() && dao.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
