package com.gp.cong.logisoft.jobscheduler;

import com.gp.cong.common.CommonUtils;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.print.PrintService;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPrintPage;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.print.DocFlavor;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;

import org.apache.log4j.Logger;

public class Print {

    private static final Logger log = Logger.getLogger(Print.class);

    public static Boolean printFile(String printerName, String file, Integer printCopy, String printerTray) throws IOException {
        File f = null;
        FileInputStream fis = null;
        FileChannel fc = null;
        try {
            f = new File(file);
            fis = new FileInputStream(f);
            fc = fis.getChannel();
            ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            PDFFile pdfFile = new PDFFile(bb); // Create PDF Print Page
            PDFPrintPage pages = new PDFPrintPage(pdfFile);
            boolean flag = false;
            // Create Print Job
            PrinterJob pjob = PrinterJob.getPrinterJob();
            PrintService[] services = PrinterJob.lookupPrintServices();
            PrintService printService = null;
            for (PrintService service : services) {
                if (null != printerName && printerName.equals(service.getName())) {
                    printService = service;
                    break;
                }
            }
            if (null == printService && null != services && services.length > 0) {
                printService = services[0];
            }
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            if (null != printService) {
                if (null != printerTray && !"".equalsIgnoreCase(printerTray)) {
                    String[] printTrayId = printerTray.split(":");
                    DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
                    Map<Integer, Media> trayMap = new HashMap<Integer, Media>();
                    Object mediaTrayObj = printService.getSupportedAttributeValues(Media.class, flavor, null);
                    if (mediaTrayObj != null && mediaTrayObj.getClass().isArray()) {
                        for (Media media : (Media[]) mediaTrayObj) {
                            // we collect the MediaTray av.ailable
                            if (media instanceof MediaTray) {
                                if (media != null
                                        && Integer.parseInt(printTrayId[0].toString()) == media.getValue()) {
                                    trayMap.put(media.getValue(), media);
                                    break;
                                }
                            }
                        }
                        MediaTray selectedTray = (MediaTray) trayMap.get(Integer.valueOf(printTrayId[0]));
                        if (null != selectedTray) {
                            System.out.println("Selected Tray is Avaliable" + selectedTray);
                            attributes.add(selectedTray);
                        } else {
                            System.out.println("Selected Tray is not Avaliable" + selectedTray);
                        }
                    }
                }
                pjob.setPrintService(printService);
                pjob.setCopies(printCopy);
                PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
                //If needed unblock this
                Paper paper = new Paper();
                paper.setImageableArea(30.0D, 10.0D, paper.getWidth() * 2, paper.getHeight());
                pf.setPaper(paper);
                pjob.setJobName(f.getName());
                Book book = new Book();
                book.append(pages, pf, pdfFile.getNumPages());
                pjob.setPageable(book);
                if (null != attributes && !attributes.isEmpty()) {
                    pjob.print(attributes);
                } else {
                    pjob.print();
                }
                flag = true;
            }
            return flag;
        } catch (PrinterException ex) {
            log.info("printFile failed on " + new Date(), ex);
            return false;
        } catch (IOException ex) {
            log.info("printFile failed on " + new Date(), ex);
            return false;
        } catch (Exception ex) {
            log.info("printFile failed on " + new Date(), ex);
            return false;
        } finally {
            //release the objects
            f = null;
            if (null != fis) {
                fis.close();
            }
            if (null != fc) {
                fc.close();
            }

        }
    }

    public static Boolean printCheck(String printerName, String file) throws IOException {
        File f = null;
        FileInputStream fis = null;
        FileChannel fc = null;
        try {
            f = new File(file);
            fis = new FileInputStream(f);
            fc = fis.getChannel();
            ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            PDFFile pdfFile = new PDFFile(bb); // Create PDF Print Page
            PDFPrintPage pages = new PDFPrintPage(pdfFile);
            boolean flag = false;
            // Create Print Job
            PrinterJob pjob = PrinterJob.getPrinterJob();
            PrintService[] services = PrinterJob.lookupPrintServices();
            PrintService printService = null;
            for (PrintService service : services) {
                if (null != printerName && printerName.equals(service.getName())) {
                    printService = service;
                    break;
                }
            }
            if (null == printService && null != services && services.length > 0) {
                printService = services[0];
            }
            if (null != printService) {
                pjob.setPrintService(printService);
                pjob.setCopies(1);
                PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
                Paper paper = new Paper();
                paper.setImageableArea(30.0D, 0.0D, paper.getWidth() * 2, paper.getHeight());
                pf.setPaper(paper);
                pjob.setJobName(f.getName());
                Book book = new Book();
                book.append(pages, pf, pdfFile.getNumPages());
                pjob.setPageable(book);
                pjob.print();
                flag = true;
            }
            return flag;
        } catch (PrinterException ex) {
            log.info("printCheck failed on " + new Date(), ex);
            return false;
        } catch (IOException ex) {
            log.info("printCheck failed on " + new Date(), ex);
            return false;
        } catch (Exception ex) {
            log.info("printCheck failed on " + new Date(), ex);
            return false;
        } finally {
            //release the objects
            f = null;
            if (null != fis) {
                fis.close();
            }
            if (null != fc) {
                fc.close();
            }

        }
    }

    public static String labelPrint(String file, String printerName, Integer printCopy) {
        Process process = null;
        Runtime runTime = null;
        InputStream inputStream = null;
        InputStream errorStream = null;
        String commandOutput = null;
        String errorOutput = null;
        int c = 0;
        printCopy = printCopy == null || printCopy == 0 ? 1 : printCopy;
        try {
            runTime = Runtime.getRuntime();
            //copy fax file
            process = runTime.exec("lp -d " + printerName + " -n " + printCopy + " -o raw " + file);
            log.info("lp -d " + printerName + " -n " + printCopy + " -o raw " + file);
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
            //delay for some time
            for (int i = 0; i <= 30; i++) {
            }
            return !errorOutput.trim().isEmpty() ? "error" : commandOutput;
        } catch (IOException e) {
            log.info("Label Print failed on " + new Date(), e);
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
                log.info("Label Print failed on " + new Date(), ex);
            }
        }
    }

    public static String printInSelectedTray(String file, String printerName, Integer printCopy, String printerTray) {
        Process process = null;
        Runtime runTime = null;
        InputStream inputStream = null;
        InputStream errorStream = null;
        String commandOutput = null;
        String errorOutput = null;
        boolean trayRequired = false;
        int c = 0;
        printCopy = printCopy == null || printCopy == 0 ? 1 : printCopy;
        try {
            String[] printTrayId = null;
            if(CommonUtils.isNotEmpty(printerTray)){
                printTrayId = printerTray.split(":");
                trayRequired = true;
            }
            runTime = Runtime.getRuntime();
            //CUPS Command to print on selected Tray
            String command = null;
            if(trayRequired){
                command = "lp -d " + printerName + " -n " + printCopy + " -o media=" + printTrayId[1].trim() + " -o fit-to-page " + file;
                log.info(command);
                process = runTime.exec(command);
            }else{
                command = "lp -d " + printerName + " -n " + printCopy + " -o fit-to-page " + file;
                log.info(command);
                process = runTime.exec(command);
            }
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
            //delay for some time
            for (int i = 0; i <= 30; i++) {
            }
            return !errorOutput.trim().isEmpty() ? "error" : commandOutput;
        } catch (IOException e) {
            log.info("Print on Selected Tray failed on " + new Date(), e);
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
                log.info("Print on Selected Tray failed " + new Date(), ex);
            }
        }
    }
}
