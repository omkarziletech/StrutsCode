package com.logiware.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.reports.*;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.Transaction;
import com.logiware.bean.ArBatchBean;
import java.io.FileOutputStream;
import java.util.List;


import com.logiware.bean.PaymentBean;
import com.logiware.bean.PaymentCheckBean;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.File;
import java.util.Date;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

public class ArBatchReportCreator extends ReportFormatMethods implements ConstantsInterface {

    private Logger log = Logger.getLogger(ArBatchReportCreator.class);
    private Document document;
    private PdfWriter pdfWriter;
    private PdfTemplate totalNoOfPages;
    private BaseFont helveticaBold;
    private String contextPath;
    private ArBatchBean arBatchBean;

    public ArBatchReportCreator() {
    }

    public ArBatchReportCreator(String contextPath, ArBatchBean arBatchBean) {
        this.contextPath = contextPath;
        this.arBatchBean = arBatchBean;
    }

    public void init(String fileName, String contextPath, ArBatchBean arBatchBean) throws Exception {
        try {
            document = new Document(PageSize.A4.rotate());
            document.setMargins(10, 10, 20, 80);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            pdfWriter.setPageEvent(new ArBatchReportCreator(contextPath, arBatchBean));
            document.open();
            PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
            pdfWriter.setOpenAction(action);
        } catch (Exception e) {
            log.info("ArBatchReport not initialized",e);
        }
    }

    @Override
    public void onOpenDocument(PdfWriter pdfWriter, Document document) {
        log.info("On Open ArBatchReport Document");
        try {
            totalNoOfPages = pdfWriter.getDirectContent().createTemplate(20, 10);
            totalNoOfPages.setBoundingBox(new Rectangle(-10, -10, 20, 50));
            helveticaBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("On Open ArBatchReport Document failed :- ",e);
        }
    }

    private PdfPTable createHeader() throws Exception {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image image = Image.getInstance(contextPath + imagePath);
        image.scalePercent(75);
        PdfPCell logoCell = new PdfPCell(image);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_TOP);
        headerTable.addCell(logoCell);
        String date = "Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy hh:mm:ss a");
        PdfPCell dateCell = makeCell(date, Element.ALIGN_RIGHT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER);
        headerTable.addCell(dateCell);
        String title = "AR Batch Report";
        PdfPCell titleCell = makeCell(title, Element.ALIGN_CENTER, Element.ALIGN_TOP, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
        PdfPTable batchDetailsTable = new PdfPTable(6);
        batchDetailsTable.setWidthPercentage(100);
        batchDetailsTable.setTotalWidth(new float[]{15, 20, 15, 20, 15, 20});
        batchDetailsTable.addCell(makeCell("Batch Id :   ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        batchDetailsTable.addCell(makeCell(arBatchBean.getBatchId(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFont, Rectangle.NO_BORDER));
        batchDetailsTable.addCell(makeCell("Batch Type :   ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        String batchType = "CASH";
        if (CommonUtils.isEqualIgnoreCase(arBatchBean.getBatchType(), AccountingConstants.AR_NET_SETT_BATCH)) {
            batchType = "NET SETTLEMENT";
        }
        batchDetailsTable.addCell(makeCell(batchType, Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFont, Rectangle.NO_BORDER));
        batchDetailsTable.addCell(makeCell("GL Account :   ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        batchDetailsTable.addCell(makeCell(arBatchBean.getGlAccount(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFont, Rectangle.NO_BORDER));
        batchDetailsTable.addCell(makeCell("Batch Amount :   ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        batchDetailsTable.addCell(makeCell(arBatchBean.getTotalAmount(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFont, Rectangle.NO_BORDER));
        batchDetailsTable.addCell(makeCell("Deposit Date :   ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        batchDetailsTable.addCell(makeCell(arBatchBean.getDepositDate(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFont, Rectangle.NO_BORDER));
        batchDetailsTable.addCell(makeCell("Batch Date :   ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        batchDetailsTable.addCell(makeCell(arBatchBean.getDate(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFont, Rectangle.NO_BORDER));
        batchDetailsTable.addCell(makeCell("Batch Notes :  ", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER));
        batchDetailsTable.addCell(makeCell(arBatchBean.getNotes(), Element.ALIGN_LEFT, Element.ALIGN_TOP, blackFont, Rectangle.NO_BORDER));
        batchDetailsTable.addCell(makeCellleftNoBorder(""));
        batchDetailsTable.addCell(makeCellleftNoBorder(""));
        batchDetailsTable.addCell(makeCellleftNoBorder(""));
        batchDetailsTable.addCell(makeCellleftNoBorder(""));
        PdfPCell batchDetailsCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, headingFontForAR, Rectangle.BOTTOM);
        batchDetailsCell.addElement(batchDetailsTable);
        headerTable.addCell(batchDetailsCell);
        return headerTable;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            document.add(createHeader());
        } catch (Exception e) {
            log.info("On Start Page ArBatchReport Document failed :- ",e);
        }
    }

    public void writeContents(ArBatchBean arBatch, TreeMap<PaymentCheckBean, List<PaymentBean>> paymentChecks) throws Exception {
        if (CommonUtils.isNotEmpty(paymentChecks)) {
            Font redBoldFont15 = new Font(Font.HELVETICA, 15, Font.BOLD, Color.RED);
            Color lavendar = Color.decode("#EBDDE2");
            Color darkAsh = Color.decode("#C3FDB8");
            Color lightAsh = Color.decode("#DCDCDC");
            PdfPTable contentsTable = new PdfPTable(10);
            contentsTable.setWidthPercentage(100);
            contentsTable.setTotalWidth(new float[]{10, 20, 10, 10, 4, 10, 10, 10, 10, 6});
            //Column Header
            contentsTable.addCell(makeCell("Customer #", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
            contentsTable.addCell(makeCell("Customer Name", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
            contentsTable.addCell(makeCell("Check#", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
            contentsTable.addCell(makeCell("Check Amount", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
            contentsTable.addCell(makeCell("Type", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
            contentsTable.addCell(makeCell("Invoice/BL", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
            contentsTable.addCell(makeCell("Applied Amount", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
            contentsTable.addCell(makeCell("GL Account", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
            contentsTable.addCell(makeCell("Adjust Amount", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
            contentsTable.addCell(makeCell("Out of Balance", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX, Color.LIGHT_GRAY));
            double grandCheckTotal = 0d;
            double grandAppliedTotal = 0d;
            double grandAdjustTotal = 0d;
            for (PaymentCheckBean check : paymentChecks.keySet()) {
                //Check Details
                contentsTable.addCell(makeCell(check.getCustomerNumber(),
                        Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lavendar));
                String checkCustomer = StringUtils.left(check.getCustomerName(), 30);
                contentsTable.addCell(makeCell(checkCustomer, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lavendar));
                contentsTable.addCell(makeCell(check.getCheckNumber(),
                        Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lavendar));
                if (check.getCheckAmount().contains("-")) {
                    String amount = "(" + check.getCheckAmount().replace("-", "") + ")";
                    contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont2, Rectangle.BOX, lavendar));
                } else {
                    contentsTable.addCell(makeCell(check.getCheckAmount(),
                            Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lavendar));
                }
                contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lavendar));
                contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lavendar));
                if (check.getAppliedAmount().contains("-")) {
                    String amount = "(" + check.getAppliedAmount().replace("-", "") + ")";
                    contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont2, Rectangle.BOX, lavendar));
                } else {
                    contentsTable.addCell(makeCell(check.getAppliedAmount(),
                            Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lavendar));
                }
                contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lavendar));
                contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lavendar));
                if (arBatch.getBatchType().equals(AccountingConstants.AR_CASH_BATCH) && check.isOutOfBalance()) {
                    contentsTable.addCell(makeCell("*", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, redBoldFont15, Rectangle.BOX, lavendar));
                } else {
                    contentsTable.addCell(makeCell("", Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, redBoldFont15, Rectangle.BOX, lavendar));
                }
                grandCheckTotal += Double.parseDouble(check.getCheckAmount().replace(",", ""));
                List<PaymentBean> payments = paymentChecks.get(check);
                if (CommonUtils.isNotEmpty(payments)) {
                    double appliedTotal = 0d;
                    double adjustTotal = 0d;
                    for (PaymentBean payment : payments) {
                        //Check Payment Details
                        contentsTable.addCell(makeCell(payment.getCustomerNumber(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        String customerName = StringUtils.left(payment.getCustomerName(), 30);
                        contentsTable.addCell(makeCell(customerName, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell(payment.getTransactionType(), Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell(payment.getInvoiceOrBl(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        appliedTotal += Double.parseDouble(payment.getPaidAmount().replace(",", ""));
                        if (payment.getPaidAmount().contains("-")) {
                            String amount = "(" + payment.getPaidAmount().replace("-", "") + ")";
                            contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.BOX));
                        } else {
                            contentsTable.addCell(makeCell(payment.getPaidAmount(), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        }
                        contentsTable.addCell(makeCell(payment.getGlAccount(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        if (CommonUtils.isNotEmpty(payment.getAdjustAmount())) {
                            adjustTotal += Double.parseDouble(payment.getAdjustAmount().replace(",", ""));
                        }
                        if (payment.getAdjustAmount().contains("-")) {
                            String amount = "(" + payment.getAdjustAmount().replace("-", "") + ")";
                            contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.BOX));
                        } else {
                            contentsTable.addCell(makeCell(payment.getAdjustAmount(), Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        }
                        contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, redBoldFont15, Rectangle.BOX));
                    }
                    //Check Total
                    PdfPCell totalCell = makeCell("Check Total", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lightAsh);
                    totalCell.setColspan(3);
                    contentsTable.addCell(totalCell);
                    if (check.getCheckAmount().contains("-")) {
                        String amount = "(" + check.getCheckAmount().replace("-", "") + ")";
                        contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont2, Rectangle.BOX, lightAsh));
                    } else {
                        contentsTable.addCell(makeCell(check.getCheckAmount(),
                                Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lightAsh));
                    }
                    contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lightAsh));
                    contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lightAsh));
                    if (appliedTotal < 0) {
                        String amount = NumberUtils.formatNumber(-appliedTotal, "(###,###,##0.00)");
                        contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont2, Rectangle.BOX, lightAsh));
                    } else {
                        String amount = NumberUtils.formatNumber(appliedTotal, "###,###,##0.00");
                        contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lightAsh));
                    }
                    grandAppliedTotal += appliedTotal;
                    contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lightAsh));
                    if (adjustTotal < 0) {
                        String amount = NumberUtils.formatNumber(-adjustTotal, "(###,###,##0.00)");
                        contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont2, Rectangle.BOX, lightAsh));
                    } else {
                        String amount = NumberUtils.formatNumber(adjustTotal, "###,###,##0.00");
                        contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lightAsh));
                    }
                    grandAdjustTotal += adjustTotal;
                    contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, lightAsh));
                }
            }
            //Batch total
            PdfPCell totalCell = makeCell("Grand Total", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, darkAsh);
            totalCell.setColspan(3);
            contentsTable.addCell(totalCell);
            if (grandCheckTotal < 0) {
                String amount = NumberUtils.formatNumber(-grandCheckTotal, "(###,###,##0.00)");
                contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont2, Rectangle.BOX, darkAsh));
            } else {
                String amount = NumberUtils.formatNumber(grandCheckTotal, "###,###,##0.00");
                contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, darkAsh));
            }
            contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, darkAsh));
            contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, darkAsh));
            if (grandAppliedTotal < 0) {
                String amount = NumberUtils.formatNumber(-grandAppliedTotal, "(###,###,##0.00)");
                contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont2, Rectangle.BOX, darkAsh));
            } else {
                String amount = NumberUtils.formatNumber(grandAppliedTotal, "###,###,##0.00");
                contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, darkAsh));
            }
            contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, darkAsh));
            if (grandAdjustTotal < 0) {
                String amount = NumberUtils.formatNumber(-grandAdjustTotal, "(###,###,##0.00)");
                contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redBoldFont2, Rectangle.BOX, darkAsh));
            } else {
                String amount = NumberUtils.formatNumber(grandAdjustTotal, "###,###,##0.00");
                contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, darkAsh));
            }
            contentsTable.addCell(makeCell("", Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX, darkAsh));
            if (arBatch.getBatchType().equals(AccountingConstants.AR_NET_SETT_BATCH)) {
                //Net Settlement invoice Details
                PdfPCell netSettInvoiceCell = makeCell("NET SETTLEMENT",
                        Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, headingFont, Rectangle.BOX, Color.LIGHT_GRAY);
                netSettInvoiceCell.setColspan(10);
                contentsTable.addCell(netSettInvoiceCell);
                AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
                String invoiceNumber = AccountingConstants.SUBLEDGER_CODE_NETSETT + arBatch.getBatchId();
                List<Transaction> netSettInvoices = transactionDAO.findByProperty("invoiceNumber", invoiceNumber);
                if (CommonUtils.isNotEmpty(netSettInvoices) && CommonUtils.isEqualIgnoreCase(arBatch.getStatus(), STATUS_CLOSED)) {
                    for (Transaction netSettInvoice : netSettInvoices) {
                        contentsTable.addCell(makeCell(netSettInvoice.getCustNo(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        String customerName = StringUtils.left(netSettInvoice.getCustName(), 30);
                        contentsTable.addCell(makeCell(customerName, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE,
                                Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell(invoiceNumber, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        if (netSettInvoice.getTransactionAmt() < 0) {
                            String amount = NumberUtils.formatNumber(-netSettInvoice.getTransactionAmt(), "(###,###,##0.00)");
                            contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.BOX));
                        } else {
                            String amount = NumberUtils.formatNumber(netSettInvoice.getTransactionAmt(), "###,###,##0.00");
                            contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX));
                        }
                        contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, redBoldFont15, Rectangle.BOX));
                    }
                } else {
                    for (PaymentCheckBean check : paymentChecks.keySet()) {
                        contentsTable.addCell(makeCell(check.getCustomerNumber(), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        String customerName = StringUtils.left(check.getCustomerName(), 30);
                        contentsTable.addCell(makeCell(customerName, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE,
                                Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell(invoiceNumber, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        if (check.getAppliedAmount().contains("-")) {
                            String amount = "(" + check.getAppliedAmount().replace("-", "") + ")";
                            contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, redFont, Rectangle.BOX));
                        } else {
                            String amount = check.getAppliedAmount();
                            contentsTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE, blackBoldFont2, Rectangle.BOX));
                        }
                        contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, blackFont, Rectangle.BOX));
                        contentsTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, redBoldFont15, Rectangle.BOX));
                    }
                }
            }
            document.add(contentsTable);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            log.info("In onEndPage, page = " + document.getPageNumber());
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            BaseFont bfHelvNormal = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            String lineOnBottom = "________________________________________________________________________________________________________"
                    + "_________________________________________________________________________________";
            cb.beginText();
            cb.setFontAndSize(bfHelvNormal, 8);
            cb.setTextMatrix(document.left(), document.bottomMargin());
            cb.showText(lineOnBottom);
            cb.endText();

            //This is for Page X Of Y
            String text = "Page " + writer.getPageNumber() + " of ";
            float textBase = document.bottomMargin() - 20;
            float textSize = helveticaBold.getWidthPoint(text, 12);
            cb.beginText();
            cb.setFontAndSize(helveticaBold, 12);
            cb.setTextMatrix(document.right() / 2, textBase);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(totalNoOfPages, document.right() / 2 + textSize, textBase);
            cb.restoreState();
        } catch (Exception e) {
            log.info("On End Page of ArBatchReport failed :- ",e);
        }
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        totalNoOfPages.beginText();
        totalNoOfPages.setFontAndSize(helveticaBold, 12);
        totalNoOfPages.setTextMatrix(0, 0);
        totalNoOfPages.showText(String.valueOf(writer.getPageNumber() - 1));
        totalNoOfPages.endText();
    }

    public void exit() {
        document.close();
    }

    public String createReport(String contextPath, ArBatchBean arBatchBean, TreeMap<PaymentCheckBean, List<PaymentBean>> paymentChecks) throws Exception {
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
        fileName.append("/Documents/ArBatch/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append(arBatchBean.getBatchId()).append(".pdf");
        init(fileName.toString(), contextPath, arBatchBean);
        writeContents(arBatchBean, paymentChecks);
        exit();
        return fileName.toString();
    }
}
