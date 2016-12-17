package com.logiware.reports;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.bean.CustomerBean;
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
 * @author Lakshmi Narayanan
 */
public class StatementConfigurationReportCreator extends ReportFormatMethods implements java.io.Serializable {
private static final Logger log = Logger.getLogger(StatementConfigurationReportCreator.class);
    private static final long serialVersionUID = 8207838899240153928L;
    private Document document = null;
    private PdfWriter pdfWriter = null;
    protected PdfTemplate totalNoOfPages = null;
    protected BaseFont helv = null;
    private String contextPath = null;

    public StatementConfigurationReportCreator() {
    }

    public StatementConfigurationReportCreator(String contextPath) {
        this.contextPath = contextPath;
    }

    private void init(String fileName, String contextPath) throws Exception {
        document = new Document(PageSize.A4.rotate());
        document.setMargins(10, 10, 20, 60);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        pdfWriter.setPageEvent(new StatementConfigurationReportCreator(contextPath));
        document.open();
        pdfWriter.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter));
    }

    private PdfPTable createHeaderTable(String contextPath) throws Exception {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{15, 10, 5, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,7});
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image image = Image.getInstance(contextPath + imagePath);
        image.scalePercent(75);
        PdfPCell logoCell = new PdfPCell(image);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_TOP);
        logoCell.setColspan(15);
        headerTable.addCell(logoCell);
        PdfPCell emptyCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.NO_BORDER);
        emptyCell.setColspan(15);
        headerTable.addCell(emptyCell);
        headerTable.addCell(emptyCell);
        String title = "Statement Configuration Report";
        PdfPCell titleCell = makeCell(title, Element.ALIGN_CENTER, Element.ALIGN_TOP, headingFont1, Rectangle.NO_BORDER, Color.LIGHT_GRAY);
        titleCell.setColspan(15);
        headerTable.addCell(titleCell);
        String date = "Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy");
        PdfPCell dateCell = makeCell(date, Element.ALIGN_RIGHT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER);
        dateCell.setColspan(15);
        headerTable.addCell(dateCell);
        headerTable.addCell(makeCell("Customer Name", Element.ALIGN_LEFT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("Customer Number", Element.ALIGN_LEFT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("Type", Element.ALIGN_LEFT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("Collector", Element.ALIGN_LEFT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("Statement Type", Element.ALIGN_LEFT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("Statement Frequency", Element.ALIGN_LEFT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("Credit Balance", Element.ALIGN_LEFT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("Credit Invoice", Element.ALIGN_LEFT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("Email", Element.ALIGN_LEFT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("Fax", Element.ALIGN_LEFT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("Current", Element.ALIGN_RIGHT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("31-60 Days", Element.ALIGN_RIGHT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("61-90 Days", Element.ALIGN_RIGHT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("91+ Days", Element.ALIGN_RIGHT, blackBoldFont2, Rectangle.BOTTOM));
        headerTable.addCell(makeCell("Total", Element.ALIGN_RIGHT, blackBoldFont2, Rectangle.BOTTOM));
        PdfPCell listHeadingCell = makeCell("", Element.ALIGN_RIGHT, Element.ALIGN_TOP, blackBoldFont2, Rectangle.NO_BORDER);
        listHeadingCell.addElement(headerTable);
        headerTable.addCell(listHeadingCell);
        return headerTable;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        totalNoOfPages = writer.getDirectContent().createTemplate(100, 100);
        totalNoOfPages.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        try {
            helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("onOpenDocument failed on " + new Date(),e);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            document.add(createHeaderTable(contextPath));
        } catch (Exception e) {
            log.info("onStartPage failed on " + new Date(),e);
        }
    }

    private void writeContents(List<CustomerBean> accounts) throws Exception {
        PdfPTable contentTable = new PdfPTable(15);
        contentTable.setWidthPercentage(100);
        contentTable.setWidths(new float[]{15, 10, 5, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,7});
        for (CustomerBean account : accounts) {
            contentTable.addCell(makeCell(account.getCustomerName(), Element.ALIGN_LEFT, smallBlackFont, Rectangle.BOTTOM));
            contentTable.addCell(makeCell(account.getCustomerNumber(), Element.ALIGN_LEFT, smallBlackFont, Rectangle.BOTTOM));
            contentTable.addCell(makeCell(account.getAccountType(), Element.ALIGN_LEFT, smallBlackFont, Rectangle.BOTTOM));
            contentTable.addCell(makeCell(account.getCollector(), Element.ALIGN_LEFT, smallBlackFont, Rectangle.BOTTOM));
            contentTable.addCell(makeCell(account.getStatementType(), Element.ALIGN_LEFT, smallBlackFont, Rectangle.BOTTOM));
            contentTable.addCell(makeCell(account.getStatementFrequency(), Element.ALIGN_LEFT, smallBlackFont, Rectangle.BOTTOM));
            contentTable.addCell(makeCell(account.getCreditBalance(), Element.ALIGN_LEFT, smallBlackFont, Rectangle.BOTTOM));
            contentTable.addCell(makeCell(account.getCreditInvoice(), Element.ALIGN_LEFT, smallBlackFont, Rectangle.BOTTOM));
            contentTable.addCell(makeCell(account.getEmail(), Element.ALIGN_LEFT, smallBlackFont, Rectangle.BOTTOM));
            contentTable.addCell(makeCell(account.getFax(), Element.ALIGN_LEFT, smallBlackFont, Rectangle.BOTTOM));
            if (account.getCurrent().startsWith("-")) {
                contentTable.addCell(makeCell("(" + account.getCurrent().replace("-", "") + ")", Element.ALIGN_RIGHT, smallRedFont, Rectangle.BOTTOM));
            } else {
                contentTable.addCell(makeCell(account.getCurrent(), Element.ALIGN_RIGHT, smallBlackFont, Rectangle.BOTTOM));
            }
            if (account.getThirtyOneToSixtyDays().startsWith("-")) {
                contentTable.addCell(makeCell("(" + account.getThirtyOneToSixtyDays().replace("-", "") + ")", Element.ALIGN_RIGHT, smallRedFont, Rectangle.BOTTOM));
            } else {
                contentTable.addCell(makeCell(account.getThirtyOneToSixtyDays(), Element.ALIGN_RIGHT, smallBlackFont, Rectangle.BOTTOM));
            }
            if (account.getSixtyOneToNintyDays().startsWith("-")) {
                contentTable.addCell(makeCell("(" + account.getSixtyOneToNintyDays().replace("-", "") + ")", Element.ALIGN_RIGHT, smallRedFont, Rectangle.BOTTOM));
            } else {
                contentTable.addCell(makeCell(account.getSixtyOneToNintyDays(), Element.ALIGN_RIGHT, smallBlackFont, Rectangle.BOTTOM));
            }
            if (account.getGreaterThanNintyDays().startsWith("-")) {
                contentTable.addCell(makeCell("(" + account.getGreaterThanNintyDays().replace("-", "") + ")", Element.ALIGN_RIGHT, smallRedFont, Rectangle.BOTTOM));
            } else {
                contentTable.addCell(makeCell(account.getGreaterThanNintyDays(), Element.ALIGN_RIGHT, smallBlackFont, Rectangle.BOTTOM));
            }
            if (account.getTotal().startsWith("-")) {
                contentTable.addCell(makeCell("(" + account.getTotal().replace("-", "") + ")", Element.ALIGN_RIGHT, smallRedFont, Rectangle.BOTTOM));
            } else {
                contentTable.addCell(makeCell(account.getTotal(), Element.ALIGN_RIGHT, smallBlackFont, Rectangle.BOTTOM));
            }
        }
        document.add(contentTable);
    }

    //This is for Footer and Page X Of Y
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            PdfPTable lineTable = makeTable(1);
            lineTable.setWidthPercentage(100);
            lineTable.setTotalWidth(820);
            lineTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, blackBoldFont, Rectangle.BOTTOM));
            lineTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());

            //This is for Page X Of Y
            BaseFont bfHelvNormal = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            String text = "Page " + writer.getPageNumber() + " of ";
            cb.beginText();
            cb.setFontAndSize(bfHelvNormal, 8);
            cb.setTextMatrix(document.right() / 2, document.bottomMargin() - 20);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(totalNoOfPages, document.right() / 2 + helv.getWidthPoint(text, 8) + 1, document.bottomMargin() - 20);
            cb.restoreState();
        } catch (Exception e) {
            log.info("onEndPage failed on " + new Date(),e);
        }
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        BaseFont bfHelvNormal = null;
        try {
            bfHelvNormal = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            totalNoOfPages.beginText();
            totalNoOfPages.setFontAndSize(bfHelvNormal, 8);//helv
            totalNoOfPages.setTextMatrix(0, 0);
            totalNoOfPages.showText(String.valueOf(document.getPageNumber() - 1));
            totalNoOfPages.endText();
        } catch (Exception e) {
            log.info("onCloseDocument failed on " + new Date(),e);
        }
    }

    public void destroy() {
        document.close();
    }

    public String createReport(String contextPath, List<CustomerBean> accounts) throws Exception {
        String fileName = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/CustomerStatement/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(fileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName += "StatementConfigurationReport.pdf";
        init(fileName, contextPath);
        writeContents(accounts);
        destroy();
        return fileName;
    }
}
