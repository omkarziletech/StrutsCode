package com.logiware.accounting.reports;

import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.FiscalPeriodDAO;
import com.logiware.accounting.model.AccountModel;
import com.logiware.reports.BaseReportCreator;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class IncomeStatementCreator extends BaseReportCreator {

    private static final Logger log = Logger.getLogger(IncomeStatementCreator.class);
    private String startPeriod;
    private String endPeriod;
    private Integer budgetSet;

    public IncomeStatementCreator() {
    }

    public IncomeStatementCreator(String startPeriod, String endPeriod, Integer budgetSet, String contextPath) {
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.budgetSet = budgetSet;
        this.contextPath = contextPath;
    }

    private void init(String fileName) throws Exception {
        document = new Document(PageSize.A4.rotate());
        document.setMargins(5, 5, 5, 30);
        writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        writer.setUserunit(1f);
        writer.setPageEvent(new IncomeStatementCreator(startPeriod, endPeriod, budgetSet, contextPath));
        document.open();
        writer.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 1f), writer));
    }

    private void writeHeader() throws Exception {
        headerTable = new PdfPTable(2);
        headerTable.setWidths(new float[]{40, 60});
        headerTable.setWidthPercentage(100);
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image image = Image.getInstance(contextPath + imagePath);
        image.scalePercent(75);
        PdfPCell logoCell = createImageCell(image);
        logoCell.setColspan(2);
        headerTable.addCell(logoCell);
        PdfPCell titleCell = createCell("Statement of Earnings", headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOX, Color.LIGHT_GRAY);
        titleCell.setColspan(2);
        headerTable.addCell(titleCell);
        headerTable.addCell(createCell("Period : " + startPeriod + " - " + endPeriod, headerBoldFont11, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
        headerTable.addCell(createCell("Budget Set : " + budgetSet, headerBoldFont11, Element.ALIGN_LEFT, Rectangle.NO_BORDER));
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        pageTemplate = writer.getDirectContent().createTemplate(20, 10);
        pageTemplate.setBoundingBox(new Rectangle(-20, -20, 20, 50));
        try {
            helvFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            log.info("onOpenDocument failed on " + new Date(), e);
        } catch (IOException e) {
            log.info("onOpenDocument failed on " + new Date(), e);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            writeHeader();
            document.add(headerTable);
        } catch (Exception e) {
            log.info("onStartPage failed on " + new Date(), e);
        }
    }

    private void writeContent() throws Exception {
        contentTable = new PdfPTable(8);
        contentTable.setWidths(new float[]{30, 10, 10, 10, 10, 10, 10, 10});
        contentTable.setWidthPercentage(100);
        contentTable.addCell(createHeaderCell("Account", blackBoldFont10, Element.ALIGN_LEFT, Rectangle.BOX));
        contentTable.addCell(createHeaderCell("Current Period", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.BOX));
        contentTable.addCell(createHeaderCell("Current Selected Period", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.BOX));
        contentTable.addCell(createHeaderCell("Current Year (YTD)", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.BOX));
        contentTable.addCell(createHeaderCell("Prior Year to date", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.BOX));
        contentTable.addCell(createHeaderCell("Prior Year (Totals)", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.BOX));
        contentTable.addCell(createHeaderCell("Budget YTD", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.BOX));
        contentTable.addCell(createHeaderCell("Annual Budget", blackBoldFont10, Element.ALIGN_RIGHT, Rectangle.BOX));
        PdfPCell lineCell = createEmptyCell(Rectangle.BOX);
        lineCell.setFixedHeight(1f);
        lineCell.setColspan(8);
        contentTable.addCell(lineCell);
        contentTable.setHeaderRows(2);
        contentTable.setFooterRows(1);
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        List<AccountModel> salesRevenue = fiscalPeriodDAO.getIncomeStatement("Income Group 1", startPeriod, endPeriod, budgetSet);
        List<AccountModel> costOfSales = fiscalPeriodDAO.getIncomeStatement("Expense Group1", startPeriod, endPeriod, budgetSet);
        List<AccountModel> operatingExpenses = fiscalPeriodDAO.getIncomeStatement("Expense Group3", startPeriod, endPeriod, budgetSet);
        List<AccountModel> otherIncomeAndExpenses = fiscalPeriodDAO.getIncomeStatement("Income Group2", startPeriod, endPeriod, budgetSet);
        otherIncomeAndExpenses.addAll(fiscalPeriodDAO.getIncomeStatement("Expense Group4", startPeriod, endPeriod, budgetSet));
        Long cp1 = 0l;
        Long csp1 = 0l;
        Long cy1 = 0l;
        Long pyp1 = 0l;
        Long py1 = 0l;
        Long bp1 = 0l;
        Long by1 = 0l;
        PdfPCell salesRevenueTitleCell = createTextCell("Sales Revenue", blackBoldFont9, Rectangle.BOX, LIGHT_ASH);
        salesRevenueTitleCell.setColspan(8);
        contentTable.addCell(salesRevenueTitleCell);
        for (AccountModel account : salesRevenue) {
            contentTable.addCell(createTextCell(account.getAccount(), blackBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getCurrentPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getCurrentSelectedPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getCurrentYear(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getPriorYearPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getPriorYear(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getBudgetPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getBudgetYear(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            cp1 += Long.parseLong(account.getCurrentPeriod().replace(",", ""));
            csp1 += Long.parseLong(account.getCurrentSelectedPeriod().replace(",", ""));
            cy1 += Long.parseLong(account.getCurrentYear().replace(",", ""));
            pyp1 += Long.parseLong(account.getPriorYearPeriod().replace(",", ""));
            py1 += Long.parseLong(account.getPriorYear().replace(",", ""));
            bp1 += Long.parseLong(account.getBudgetPeriod().replace(",", ""));
            by1 += Long.parseLong(account.getBudgetYear().replace(",", ""));
        }
        contentTable.addCell(createCell("Total Sales Revenue", blackBoldFont9, Element.ALIGN_LEFT, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(cp1, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(csp1, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(cy1, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(pyp1, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(py1, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(bp1, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(by1, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        Long cp2 = 0l;
        Long csp2 = 0l;
        Long cy2 = 0l;
        Long pyp2 = 0l;
        Long py2 = 0l;
        Long bp2 = 0l;
        Long by2 = 0l;
        PdfPCell costOfSalesTitleCell = createTextCell("Cost Of Sales", blackBoldFont9, Rectangle.BOX, LIGHT_ASH);
        costOfSalesTitleCell.setColspan(8);
        contentTable.addCell(costOfSalesTitleCell);
        for (AccountModel account : costOfSales) {
            contentTable.addCell(createTextCell(account.getAccount(), blackBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getCurrentPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getCurrentSelectedPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getCurrentYear(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getPriorYearPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getPriorYear(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getBudgetPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getBudgetYear(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            cp2 += Long.parseLong(account.getCurrentPeriod().replace(",", ""));
            csp2 += Long.parseLong(account.getCurrentSelectedPeriod().replace(",", ""));
            cy2 += Long.parseLong(account.getCurrentYear().replace(",", ""));
            pyp2 += Long.parseLong(account.getPriorYearPeriod().replace(",", ""));
            py2 += Long.parseLong(account.getPriorYear().replace(",", ""));
            bp2 += Long.parseLong(account.getBudgetPeriod().replace(",", ""));
            by2 += Long.parseLong(account.getBudgetYear().replace(",", ""));
        }
        contentTable.addCell(createCell("Total Cost Of Sales", blackBoldFont9, Element.ALIGN_LEFT, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(cp2, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(csp2, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(cy2, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(pyp2, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(py2, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(bp2, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(by2, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createCell("Gross Profit(Loss)", blackBoldFont9, Element.ALIGN_LEFT, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(cp1 - cp2, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(csp1 - csp2, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(cy1 - cy2, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(pyp1 - pyp2, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(py1 - py2, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(bp1 - bp2, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(by1 - by2, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        Long cp3 = 0l;
        Long csp3 = 0l;
        Long cy3 = 0l;
        Long pyp3 = 0l;
        Long py3 = 0l;
        Long bp3 = 0l;
        Long by3 = 0l;
        PdfPCell operatingExpensesTitleCell = createTextCell("Operating Expenses", blackBoldFont9, Rectangle.BOX, LIGHT_ASH);
        operatingExpensesTitleCell.setColspan(8);
        contentTable.addCell(operatingExpensesTitleCell);
        for (AccountModel account : operatingExpenses) {
            contentTable.addCell(createTextCell(account.getAccount(), blackBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getCurrentPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getCurrentSelectedPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getCurrentYear(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getPriorYearPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getPriorYear(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getBudgetPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getBudgetYear(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            cp3 += Long.parseLong(account.getCurrentPeriod().replace(",", ""));
            csp3 += Long.parseLong(account.getCurrentSelectedPeriod().replace(",", ""));
            cy3 += Long.parseLong(account.getCurrentYear().replace(",", ""));
            pyp3 += Long.parseLong(account.getPriorYearPeriod().replace(",", ""));
            py3 += Long.parseLong(account.getPriorYear().replace(",", ""));
            bp3 += Long.parseLong(account.getBudgetPeriod().replace(",", ""));
            by3 += Long.parseLong(account.getBudgetYear().replace(",", ""));
        }
        contentTable.addCell(createCell("Total Operating Expenses", blackBoldFont9, Element.ALIGN_LEFT, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(cp3, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(csp3, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(cy3, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(pyp3, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(py3, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(bp3, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(by3, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createCell("Earnings (Loss) from Operations", blackBoldFont9, Element.ALIGN_LEFT, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(cp1 - cp2 - cp3, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(csp1 - csp2 - csp3, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(cy1 - cy2 - cy3, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(pyp1 - pyp2 - pyp3, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(py1 - py2 - py3, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(bp1 - bp2 - bp3, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(by1 - by2 - by3, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        Long cp4 = 0l;
        Long csp4 = 0l;
        Long cy4 = 0l;
        Long pyp4 = 0l;
        Long py4 = 0l;
        Long bp4 = 0l;
        Long by4 = 0l;
        PdfPCell otherIncomeAndExpensesTitleCell = createTextCell("Other Income and Expenses", blackBoldFont9, Rectangle.BOX, LIGHT_ASH);
        otherIncomeAndExpensesTitleCell.setColspan(8);
        contentTable.addCell(otherIncomeAndExpensesTitleCell);
        for (AccountModel account : otherIncomeAndExpenses) {
            contentTable.addCell(createTextCell(account.getAccount(), blackBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getCurrentPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getCurrentSelectedPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getCurrentYear(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getPriorYearPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getPriorYear(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getBudgetPeriod(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            contentTable.addCell(createAmountCell(account.getBudgetYear(), blackBoldFont9, redBoldFont9, Rectangle.BOX));
            cp4 += Long.parseLong(account.getCurrentPeriod().replace(",", ""));
            csp4 += Long.parseLong(account.getCurrentSelectedPeriod().replace(",", ""));
            cy4 += Long.parseLong(account.getCurrentYear().replace(",", ""));
            pyp4 += Long.parseLong(account.getPriorYearPeriod().replace(",", ""));
            py4 += Long.parseLong(account.getPriorYear().replace(",", ""));
            bp4 += Long.parseLong(account.getBudgetPeriod().replace(",", ""));
            by4 += Long.parseLong(account.getBudgetYear().replace(",", ""));
        }
        contentTable.addCell(createCell("Total Other Income and Expenses", blackBoldFont9, Element.ALIGN_LEFT, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(cp4, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(csp4, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(cy4, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(pyp4, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(py4, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(bp4, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createAmountCell(by4, blackBoldFont9, redBoldFont9, Rectangle.BOX, LAVENDAR));
        contentTable.addCell(createCell("Net Earnings (Loss)", blackBoldFont9, Element.ALIGN_LEFT, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(cp1 - cp2 - cp3 - cp4, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(csp1 - csp2 - csp3 - csp4, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(cy1 - cy2 - cy3 - cy4, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(pyp1 - pyp2 - pyp3 - pyp4, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(py1 - py2 - py3 - py4, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(bp1 - bp2 - bp3 - bp4, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
        contentTable.addCell(createAmountCell(by1 - by2 - by3 - by4, blackBoldFont9, redBoldFont9, Rectangle.BOX, TEAL));
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
        fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/IncomeStatement/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append("IncomeStatement_").append(startPeriod).append("_").append(endPeriod).append(".pdf");
        init(fileName.toString());
        writeContent();
        exit();
        return fileName.toString();
    }
}
