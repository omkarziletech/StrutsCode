/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.reports;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.hibernate.dao.ArBatchDAO;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
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
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
/**
 *
 * @author Administrator
 */
public class NSInvoiceReportCreator extends ReportFormatMethods implements ConstantsInterface {
private static final Logger log = Logger.getLogger(NSInvoiceReportCreator.class);
    private Document document;
    private PdfWriter pdfWriter;
    private PdfTemplate totalNoOfPages;
    private BaseFont helveticaBold;
    private String contextPath;
    private String batchId;
    private String companyName = null;
    private String companyAddress = null;
    private String companyPhone = null;
    private String companyFax = null;
    private String companyEmail = null;

    public NSInvoiceReportCreator() throws Exception {
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        this.companyName = systemRulesDAO.getSystemRulesByCode("CompanyName");
        this.companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
        this.companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
        this.companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");
        this.companyEmail = systemRulesDAO.getSystemRulesByCode("Email");
    }

    public NSInvoiceReportCreator(String contextPath, String batchId) throws Exception {
        this();
        this.contextPath = contextPath;
        this.batchId = batchId;
    }

    public void init(String fileName, String contextPath, String batchId) throws Exception {
            document = new Document(PageSize.A4);
            document.setMargins(10, 10, 20, 40);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            pdfWriter.setPageEvent(new NSInvoiceReportCreator(contextPath, batchId));
            document.open();
            PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
            pdfWriter.setOpenAction(action);
    }

    public void onOpenDocument(PdfWriter pdfWriter, Document document) {
        //log.info("On Open ArBatchReport Document");
        try {
            totalNoOfPages = pdfWriter.getDirectContent().createTemplate(20, 10);
            totalNoOfPages.setBoundingBox(new Rectangle(-10, -10, 20, 50));
            helveticaBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("On Open NSInvoice Document failed on " + new Date(),e);
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
        //Company Name in header
        headerTable.addCell(makeCell(companyName, Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER));
        //Company address in header
        StringBuilder address = new StringBuilder();
        address.append(companyAddress).append(". PHONE: ").append(companyPhone).append(". FAX: ").append(companyFax);
        headerTable.addCell(makeCell(address.toString(), Element.ALIGN_CENTER, blackFont, Rectangle.NO_BORDER));
        return headerTable;
    }

    public void onStartPage(PdfWriter writer, Document document) {
        try {
            document.add(createHeader());
        } catch (Exception e) {
            log.info("On Start Page NSInvoiceReport Document on " + new Date(),e);
        }
    }

    public void writeContents(String batchId) throws Exception {
        double receivableTotal = 0d, payableTotal = 0d;
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        Object customerReference = arBatchDAO.getCustomerRef(batchId);
        List<Object[]> receivables = arBatchDAO.getReceivables(batchId);
        List<Object[]> payables = arBatchDAO.getPayables(batchId);

        int receivableCount = receivables.size();
        int payableCount = payables.size();
        int rowCount = receivableCount > payableCount ? receivableCount : payableCount;


        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            //For Receivables
            if (rowIndex < receivableCount) {
                Object[] receivable = receivables.get(rowIndex);
                String amount = receivable[1].toString();
                receivableTotal += Double.parseDouble(amount.replace(",", ""));
            }
            //For Payables
            if (rowIndex < payableCount) {
                Object[] payable = payables.get(rowIndex);
                String amount = payable[1].toString();
                payableTotal += Double.parseDouble(amount.replace(",", ""));
            }
        }
        //For Net Amount
        double netTotal = receivableTotal + payableTotal;
        String netFavour = netTotal >= 0 ? "INVOICE" : "CREDIT NOTE";

        PdfPTable contentTable = new PdfPTable(5);
        contentTable.setWidthPercentage(100);
        contentTable.setTotalWidth(new float[]{36, 13, 2, 36, 13});
        //For Customer Reference
        PdfPTable customerTable = new PdfPTable(4);
        customerTable.setWidthPercentage(100);
        customerTable.setTotalWidth(new float[]{58, 12, 10, 20});
        customerTable.addCell(makeCell("TO ", Element.ALIGN_CENTER, headingFontSize8, Rectangle.BOX, Color.LIGHT_GRAY));
        customerTable.addCell(makeCell(netFavour, Element.ALIGN_CENTER, headingFontSize8, Rectangle.BOX, Color.LIGHT_GRAY));
        customerTable.addCell(makeCell("DATE", Element.ALIGN_CENTER, headingFontSize8, Rectangle.BOX, Color.LIGHT_GRAY));
        customerTable.addCell(makeCell("YOUR REF NO.", Element.ALIGN_CENTER, headingFontSize8, Rectangle.BOX, Color.LIGHT_GRAY));
        if (null != customerReference) {
            Object ref[] = (Object[]) customerReference;
            StringBuilder toAddress = new StringBuilder();
            toAddress.append(ref[0].toString());//Customer Name
            toAddress.append("(").append(ref[1].toString()).append(")");//Customer Number
            toAddress.append(ref[2].toString());//Customer Address
            customerTable.addCell(makeCell(toAddress.toString(), Element.ALIGN_LEFT, textFontForPayment, Rectangle.BOX));
            customerTable.addCell(makeCell(ref[3].toString(), Element.ALIGN_CENTER, textFontForPayment, Rectangle.BOX));//Invoice Number
            String date = DateUtils.formatDate(DateUtils.parseDate(ref[4].toString(), "yyyy-MM-dd"), "MM/dd/yyyy");
            customerTable.addCell(makeCell(date, Element.ALIGN_CENTER, textFontForPayment, Rectangle.BOX));//Deposite Date
            customerTable.addCell(makeCell(null != ref[5] && !ref[5].toString().equals("") ? ref[5].toString() : "From NettSettlement " + batchId, Element.ALIGN_CENTER, textFontForPayment, Rectangle.BOX));//Notes
        }
        PdfPCell customerCell = makeCell("", Element.ALIGN_CENTER, smallBlackFont, Rectangle.NO_BORDER);
        customerCell.addElement(customerTable);
        customerCell.setColspan(5);
        contentTable.addCell(customerCell);
        PdfPCell emptyCell = makeCell("", Element.ALIGN_CENTER, smallBlackFont, Rectangle.NO_BORDER);
        emptyCell.setColspan(5);
        contentTable.addCell(emptyCell);//For Empty row

        netFavour = netFavour.equalsIgnoreCase("CREDIT NOTE") ? NET_AMOUNT_YOUR_FAVOUR : NET_AMOUNT_OUR_FAVOUR;
        Color lightyellow = Color.decode("#FFCC99");
        PdfPCell netFavourCell = makeCell(netFavour, Element.ALIGN_RIGHT, headingFontSize8, Rectangle.BOX);
        netFavourCell.setColspan(4);
        netFavourCell.setBackgroundColor(lightyellow);
        contentTable.addCell(netFavourCell);
        
        String netAmount = NumberUtils.formatNumber(netTotal, "###,###,##0.00");
        if (netAmount.contains("-")) {
            PdfPCell netAmountCell = makeCell("USD (" + netAmount.replace("-", "") + ")", Element.ALIGN_RIGHT, redFont, Rectangle.BOX);
            netAmountCell.setBackgroundColor(lightyellow);
            contentTable.addCell(netAmountCell);
        } else {
            PdfPCell netAmountCell = makeCell("USD "+netAmount, Element.ALIGN_RIGHT, blackFont, Rectangle.BOX);
            netAmountCell.setBackgroundColor(lightyellow);
            contentTable.addCell(netAmountCell);
        }
        contentTable.addCell(emptyCell);//For Empty row
        receivableTotal = 0d;
        payableTotal = 0d;

        PdfPCell recCell = makeCell(ECONOCARIBE_RECEIVABLES, Element.ALIGN_LEFT, headingFontSize8, Rectangle.BOX, Color.LIGHT_GRAY);//For Receivables
        recCell.setColspan(2);
        contentTable.addCell(recCell);
        contentTable.addCell(makeCell("", Element.ALIGN_CENTER, smallBlackFont, Rectangle.NO_BORDER));//For Empty column
        PdfPCell payCell = makeCell(ECONOCARIBE_PAYABLES, Element.ALIGN_LEFT, headingFontSize8, Rectangle.BOX, Color.LIGHT_GRAY);//For Payables
        payCell.setColspan(2);
        contentTable.addCell(payCell);

        contentTable.addCell(makeCell(ECONOCARIBE_RECEIVABLES_INVOICE, Element.ALIGN_CENTER, headingFontSize8, Rectangle.BOX, Color.LIGHT_GRAY));
        contentTable.addCell(makeCell(RECEIVABLES_AMOUNT, Element.ALIGN_CENTER, headingFontSize8, Rectangle.BOX, Color.LIGHT_GRAY));
        contentTable.addCell(makeCell("", Element.ALIGN_CENTER, smallBlackFont, Rectangle.NO_BORDER));//For Empty column
        contentTable.addCell(makeCell(ECONOCARIBE_PAYABLES_INVOICE, Element.ALIGN_CENTER, headingFontSize8, Rectangle.BOX, Color.LIGHT_GRAY));
        contentTable.addCell(makeCell(PAYABLES_AMOUNT, Element.ALIGN_CENTER, headingFontSize8, Rectangle.BOX, Color.LIGHT_GRAY));

        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            //For Receivables
            if (rowIndex < receivableCount) {
                Object[] receivable = receivables.get(rowIndex);
                contentTable.addCell(makeCell(null != receivable[0] ? receivable[0].toString() : "", Element.ALIGN_LEFT, blackFont, Rectangle.BOX));
                String amount = receivable[1].toString();
                if (amount.contains("-")) {
                    contentTable.addCell(makeCell("(" + amount.replace("-", "") + ")", Element.ALIGN_RIGHT, redFont, Rectangle.BOX));
                } else {
                    contentTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, blackFont, Rectangle.BOX));
                }
                receivableTotal += Double.parseDouble(amount.replace(",", ""));
            } else {
                PdfPCell cell = makeCell("", Element.ALIGN_CENTER, blackBoldHeadingFont, Rectangle.BOX);
                contentTable.addCell(cell);
                contentTable.addCell(cell);
            }
            //For Empty column
            contentTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldHeadingFont, Rectangle.NO_BORDER));
            //For Payables
            if (rowIndex < payableCount) {
                Object[] payable = payables.get(rowIndex);
                contentTable.addCell(makeCell(null != payable[0] ? payable[0].toString() : "", Element.ALIGN_LEFT, blackFont, Rectangle.BOX));
                String amount = payable[1].toString();
                if (amount.contains("-")) {
                    contentTable.addCell(makeCell("(" + amount.replace("-", "") + ")", Element.ALIGN_RIGHT, redFont, Rectangle.BOX));
                } else {
                    contentTable.addCell(makeCell(amount, Element.ALIGN_RIGHT, blackFont, Rectangle.BOX));
                }
                payableTotal += Double.parseDouble(amount.replace(",", ""));
            } else {
                PdfPCell cell = makeCell("", Element.ALIGN_CENTER, blackBoldHeadingFont, Rectangle.BOX);
                contentTable.addCell(cell);
                contentTable.addCell(cell);
            }
        }
        contentTable.addCell(makeCell("Total", Element.ALIGN_RIGHT, headingFontSize8, Rectangle.BOX));
        String receivableAmount = NumberUtils.formatNumber(receivableTotal, "###,###,##0.00");
        if (receivableAmount.contains("-")) {
            contentTable.addCell(makeCell("(" + receivableAmount.replace("-", "") + ")", Element.ALIGN_RIGHT, redFont, Rectangle.BOX));
        } else {
            contentTable.addCell(makeCell(receivableAmount, Element.ALIGN_RIGHT, blackFont, Rectangle.BOX));
        }
        contentTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldHeadingFont, Rectangle.NO_BORDER));
        contentTable.addCell(makeCell("Total", Element.ALIGN_RIGHT, headingFontSize8, Rectangle.BOX));
        String payableAmount = NumberUtils.formatNumber(payableTotal, "###,###,##0.00");
        if (payableAmount.contains("-")) {
            contentTable.addCell(makeCell("(" + payableAmount.replace("-", "") + ")", Element.ALIGN_RIGHT, redFont, Rectangle.BOX));
        } else {
            contentTable.addCell(makeCell(payableAmount, Element.ALIGN_RIGHT, blackFont, Rectangle.BOX));
        }
        contentTable.addCell(emptyCell);//For Empty row
        document.add(contentTable);
    }

    public void onEndPage(PdfWriter writer, Document document) {
        try {
            //log.info("In onEndPage, page = " + document.getPageNumber());
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            BaseFont bfHelvNormal = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            String lineOnBottom = "_________________________________________________________________________________________________________"
                    + "_________________________";
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
            log.info("On Start Page NSInvoice Document on " + new Date(),e);
        }
    }

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

    public String createReport(String contextPath, String batchId) throws Exception {
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
        fileName.append("/ArBatch/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append("NET_SETT_").append(batchId).append(".pdf");
        init(fileName.toString(), contextPath, batchId);
        writeContents(batchId);
        exit();
        return fileName.toString();
    }
}
