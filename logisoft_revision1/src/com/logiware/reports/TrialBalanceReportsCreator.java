package com.logiware.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.bean.ReportBean;
import com.logiware.hibernate.dao.TrialBalanceDAO;
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
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Naryanan
 */
public class TrialBalanceReportsCreator extends BaseReportCreator {

    private static final Logger log = Logger.getLogger(TrialBalanceReportsCreator.class);
    private String startAccount;
    private String endAccount;
    private String year;
    private String period;
    private boolean ecuReport;

    public TrialBalanceReportsCreator() {
    }

    public TrialBalanceReportsCreator(String startAccount, String endAccount, String year, String period, boolean ecuReport, String contextPath) {
        this.startAccount = startAccount;
        this.endAccount = endAccount;
        this.year = year;
        this.period = period;
        this.ecuReport = ecuReport;
        this.contextPath = contextPath;
    }

    private void init(String fileName) throws Exception {
        document = new Document(PageSize.A4);
        document.setMargins(5, 5, 5, 30);
        writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        writer.setUserunit(1f);
        writer.setPageEvent(new TrialBalanceReportsCreator(startAccount, endAccount, year, period, ecuReport, contextPath));
        document.open();
        writer.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 1f), writer));
    }

    private void writeHeader() throws Exception {
        headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image image = Image.getInstance(contextPath + imagePath);
        image.scalePercent(75);
        headerTable.addCell(createImageCell(image));
        headerTable.addCell(createEmptyCell(Rectangle.BOTTOM));
        String title = "Trial Balance Report";
        PdfPCell titleCell = createCell(title, headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOTTOM, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
        PdfPTable filtersTable = new PdfPTable(4);
        filtersTable.setWidthPercentage(100);
        filtersTable.setWidths(new float[]{8, 15, 7, 70});
        if (CommonUtils.isNotEmpty(startAccount) && CommonUtils.isNotEmpty(endAccount)) {
            filtersTable.setWidths(new float[]{12, 15, 12, 61});
            filtersTable.addCell(createTextCell("Start Account : ", blackBoldFont8, Rectangle.NO_BORDER));
            filtersTable.addCell(createTextCell(startAccount, blackNormalFont7, Rectangle.NO_BORDER));
            filtersTable.addCell(createTextCell("End Account : ", blackBoldFont8, Rectangle.NO_BORDER));
            filtersTable.addCell(createTextCell(endAccount, blackNormalFont7, Rectangle.NO_BORDER));
        }
        filtersTable.addCell(createTextCell("Year : ", blackBoldFont8, Rectangle.NO_BORDER));
        filtersTable.addCell(createTextCell(year, blackNormalFont7, Rectangle.NO_BORDER));
        filtersTable.addCell(createTextCell("Period : ", blackBoldFont8, Rectangle.NO_BORDER));
        String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        filtersTable.addCell(createTextCell(month[Integer.parseInt(period) - 1], blackNormalFont7, Rectangle.NO_BORDER));
        PdfPCell filtersCell = createEmptyCell(Rectangle.BOTTOM);
        filtersCell.addElement(filtersTable);
        headerTable.addCell(filtersCell);
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        pageTemplate = writer.getDirectContent().createTemplate(20, 10);
        pageTemplate.setBoundingBox(new Rectangle(-20, -20, 20, 50));
        try {
            helvFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("onOpenDocument failed on " + new Date(), e);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            writeHeader();
            document.add(headerTable);
        } catch (Exception e) {
            log.info("onOpenDocument failed on " + new Date(), e);
        }
    }

    private void writeContent() throws Exception {
        List<ReportBean> trialBalances = new TrialBalanceDAO().getTrialBalances(startAccount, endAccount, year, period, ecuReport);
        contentTable = new PdfPTable(4);
        contentTable.setWidthPercentage(100);
        if (ecuReport) {
            contentTable.setWidths(new float[]{45, 20, 17, 18});
            contentTable.addCell(createHeaderCell("ECU Account", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
            contentTable.addCell(createHeaderCell("Account Type", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        } else {
            contentTable.setWidths(new float[]{20, 45, 18, 18});
            contentTable.addCell(createHeaderCell("Account", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
            contentTable.addCell(createHeaderCell("Description", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        }
        contentTable.addCell(createHeaderCell("Debit", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Credit", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
        contentTable.setHeaderRows(1);
        double totalDebit = 0d;
        double totalCredit = 0d;
        if (ecuReport) {
            for (ReportBean trialBalance : trialBalances) {
                contentTable.addCell(createTextCell(trialBalance.getReportCategory(), blackNormalFont7, Rectangle.BOTTOM));
                contentTable.addCell(createTextCell(trialBalance.getAccountType(), blackNormalFont7, Rectangle.BOTTOM));
                contentTable.addCell(createAmountCell(trialBalance.getDebit(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
                contentTable.addCell(createAmountCell(trialBalance.getCredit(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
                totalDebit += Double.parseDouble(trialBalance.getDebit().replace(",", ""));
                totalCredit += Double.parseDouble(trialBalance.getCredit().replace(",", ""));
            }
        } else {
            for (ReportBean trialBalance : trialBalances) {
                contentTable.addCell(createTextCell(trialBalance.getAccount(), blackNormalFont7, Rectangle.BOTTOM));
                contentTable.addCell(createTextCell(trialBalance.getDescription(), blackNormalFont7, Rectangle.BOTTOM));
                contentTable.addCell(createAmountCell(trialBalance.getDebit(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
                contentTable.addCell(createAmountCell(trialBalance.getCredit(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
                totalDebit += Double.parseDouble(trialBalance.getDebit().replace(",", ""));
                totalCredit += Double.parseDouble(trialBalance.getCredit().replace(",", ""));
            }
        }
        PdfPCell grandTotalCell = createCell("Total", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, LAVENDAR);
        grandTotalCell.setColspan(2);
        contentTable.addCell(grandTotalCell);
        contentTable.addCell(createAmountCell(totalDebit, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(totalCredit, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
        document.add(contentTable);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        String text = "Page " + writer.getPageNumber() + " of ";
        float textBase = document.bottom() - 20;
        float textSize = helvFont.getWidthPoint(text, 12);
        cb.beginText();
        cb.setFontAndSize(helvFont, 12);
        cb.setTextMatrix((document.right() / 2) - (textSize / 2), textBase);
        cb.showText(text);
        cb.endText();
        cb.addTemplate(pageTemplate, (document.right() / 2) + (textSize / 2), textBase);
        cb.restoreState();
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        pageTemplate.beginText();
        pageTemplate.setFontAndSize(helvFont, 12);
        pageTemplate.setTextMatrix(0, 0);
        pageTemplate.showText(String.valueOf(writer.getPageNumber() - 1));
        pageTemplate.endText();
    }

    private void exit() {
        document.close();
    }

    public String create() throws Exception {
        StringBuilder fileName = new StringBuilder();
        fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/TrialBalance/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append("TrialBalance_").append(year).append("_").append(period).append(".pdf");
        init(fileName.toString());
        writeContent();
        exit();
        return fileName.toString();
    }
}
