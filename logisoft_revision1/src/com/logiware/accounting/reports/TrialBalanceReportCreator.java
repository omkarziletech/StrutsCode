package com.logiware.accounting.reports;

import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.FiscalPeriodDAO;
import com.logiware.accounting.model.AccountModel;
import com.logiware.reports.BaseReportCreator;
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
public class TrialBalanceReportCreator extends BaseReportCreator {

    private static final Logger log = Logger.getLogger(TrialBalanceReportCreator.class);
    private String startPeriod;
    private String endPeriod;
    private boolean ecuFormat;

    public TrialBalanceReportCreator() {
    }

    public TrialBalanceReportCreator(String startPeriod, String endPeriod, boolean ecuFormat, String contextPath) {
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.ecuFormat = ecuFormat;
        this.contextPath = contextPath;
    }

    private void init(String fileName) throws Exception {
        document = new Document(PageSize.A4);
        document.setMargins(5, 5, 5, 30);
        writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        writer.setUserunit(1f);
        writer.setPageEvent(new TrialBalanceReportCreator(startPeriod, endPeriod, ecuFormat, contextPath));
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
        filtersTable.setWidths(new float[]{12, 15, 12, 61});
        filtersTable.addCell(createTextCell("Starting Period : ", blackBoldFont8, Rectangle.NO_BORDER));
        filtersTable.addCell(createTextCell(startPeriod, blackNormalFont7, Rectangle.NO_BORDER));
        filtersTable.addCell(createTextCell("Ending Period : ", blackBoldFont8, Rectangle.NO_BORDER));
        filtersTable.addCell(createTextCell(endPeriod, blackNormalFont7, Rectangle.NO_BORDER));
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
        contentTable = new PdfPTable(4);
        contentTable.setWidthPercentage(100);
        if (ecuFormat) {
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
        List<AccountModel> accounts = new FiscalPeriodDAO().getTrialBalances(startPeriod, endPeriod, ecuFormat);
        int rowCount = 0;
        double totalDebit = 0d;
        double totalCredit = 0d;
        AccountModel profit = accounts.get(accounts.size() - 1);
        accounts.remove(accounts.size() - 1);
        for (AccountModel account : accounts) {
            contentTable.addCell(createTextCell(account.getAccount(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(account.getDescription(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(account.getDebit(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createAmountCell(account.getCredit(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
            totalDebit += Double.parseDouble(account.getDebit().replace(",", ""));
            totalCredit += Double.parseDouble(account.getCredit().replace(",", ""));
        }

        PdfPCell grandTotalCell = createCell("Total", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, LAVENDAR);
        grandTotalCell.setColspan(2);
        contentTable.addCell(grandTotalCell);
        contentTable.addCell(createAmountCell(totalDebit, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
        contentTable.addCell(createAmountCell(totalCredit, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));

        PdfPCell profitCell = createCell(profit.getDescription(), redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, LIGHT_ASH);
        profitCell.setColspan(2);
        contentTable.addCell(profitCell);
        contentTable.addCell(createAmountCell(profit.getDebit(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createAmountCell(profit.getCredit(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LIGHT_ASH));
        contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LIGHT_ASH));
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
        fileName.append("TrialBalance_").append(startPeriod).append("_").append(endPeriod).append(".pdf");
        init(fileName.toString());
        writeContent();
        exit();
        return fileName.toString();
    }
}
