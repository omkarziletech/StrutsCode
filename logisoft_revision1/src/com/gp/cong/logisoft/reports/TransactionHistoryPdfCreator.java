package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import com.gp.cong.logisoft.reports.dto.TransactionHistoryReportDTO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.ChartOfAccountBean;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class TransactionHistoryPdfCreator {

    Document document = null;
    Font blackBoldFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont1 = new Font(Font.BOLD, 12, 0, Color.BLACK);
    Font blackFont = new Font(Font.HELVETICA, 9, 0, Color.BLACK);

    public void initialize(String fileName) throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
        pdfWriter.setOpenAction(action);
    }

    public void createBody(TransactionHistoryReportDTO transactionHistoryVO, String contextPath) throws DocumentException, MalformedURLException, IOException, Exception {
        PdfPCell cell;
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image img = Image.getInstance(contextPath + imagePath);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        img.scalePercent(50);
        PdfPCell celL = new PdfPCell();
        celL.addElement(new Chunk(img, 200, -30));
        celL.setBorder(0);
        celL.setHorizontalAlignment(Element.ALIGN_CENTER);
        celL.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(celL);

        PdfPTable emptyTable = new PdfPTable(1);
        emptyTable.addCell(makeCellCenter(""));
        emptyTable.addCell(makeCellCenter(""));
        emptyTable.addCell(makeCellCenter(""));
        emptyTable.addCell(makeCellCenter(""));

        PdfPTable transactionHistoryDetTable = makeTable(6);
        transactionHistoryDetTable.setWidthPercentage(100);
        cell = makeCellLeftBottomBorder("Transaction History Details");
        cell.setBorderWidthLeft(0.1f);
        cell.setBorderWidthTop(0.1f);
        cell.setBorderWidthRight(0.1f);
        cell.setBorderWidthBottom(0.1f);
        cell.setColspan(6);
        transactionHistoryDetTable.addCell(cell);
        cell = makeCellLeft("Starting Account :");
        cell.setBorderWidthLeft(0.1f);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        transactionHistoryDetTable.addCell(cell);
        transactionHistoryDetTable.addCell(makeCellLeft(transactionHistoryVO.getStartAccount()));
        transactionHistoryDetTable.addCell(makeCellLeft("Description :"));
        transactionHistoryDetTable.addCell(makeCellLeft(transactionHistoryVO.getDescription()));
        transactionHistoryDetTable.addCell(makeCellLeft("Period From :"));
        transactionHistoryDetTable.addCell(makeCellLeftRightBorder(transactionHistoryVO.getFromPeriod()));
        cell = makeCellLeft("Ending Account :");
        cell.setBorderWidthLeft(0.1f);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        transactionHistoryDetTable.addCell(cell);
        transactionHistoryDetTable.addCell(makeCellLeft(transactionHistoryVO.getEndAccount()));
        transactionHistoryDetTable.addCell(makeCellLeft("Source Code :"));
        transactionHistoryDetTable.addCell(makeCellLeft(transactionHistoryVO.getSourceCode()));
        transactionHistoryDetTable.addCell(makeCellLeft("Period To :"));
        transactionHistoryDetTable.addCell(makeCellLeftRightBorder(transactionHistoryVO.getToPeriod()));

        PdfPTable transactionListTable = makeTable(9);
        transactionListTable.setWidthPercentage(100);
        transactionListTable.setWidths(new float[]{9, 7, 9, 10, 9, 30, 8, 8, 10});
        cell = makeCellLeftBottomBorder("List of Transaction History");
        cell.setBorderWidthLeft(0.1f);
        cell.setBorderWidthTop(0.1f);
        cell.setBorderWidthRight(0.1f);
        cell.setBorderWidthBottom(0.1f);
        cell.setColspan(9);
        transactionListTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Account");
        cell.setBorderWidthLeft(0.1f);
        cell.setBorderWidthTop(0.1f);
        cell.setBorderWidthRight(0.0f);
        cell.setBorderWidthBottom(0.1f);
        transactionListTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Period");
        transactionListTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Date");
        transactionListTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Source Code");
        transactionListTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Reference");
        transactionListTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Description");
        transactionListTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Debit");
        transactionListTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Credit");
        transactionListTable.addCell(cell);
        cell = makeCellLeftBottomBorder("Balance");
        cell.setBorderWidthLeft(0.0f);
        cell.setBorderWidthTop(0.1f);
        cell.setBorderWidthRight(0.1f);
        cell.setBorderWidthBottom(0.1f);
        transactionListTable.addCell(cell);
        if (null != transactionHistoryVO.getTransactionList()) {
            for (Iterator iter = (Iterator) transactionHistoryVO.getTransactionList().iterator(); iter.hasNext();) {
                ChartOfAccountBean chartOfAccountBean = (ChartOfAccountBean) iter.next();
                cell = makeCellLeft(chartOfAccountBean.getAcct());
                cell.setBorderWidthLeft(0.1f);
                transactionListTable.addCell(cell);
                transactionListTable.addCell(makeCellLeft(chartOfAccountBean.getPeriod()));
                transactionListTable.addCell(makeCellLeft(chartOfAccountBean.getDate()));
                transactionListTable.addCell(makeCellLeft(chartOfAccountBean.getSourcecode()));
                transactionListTable.addCell(makeCellLeft(chartOfAccountBean.getReference()));
                transactionListTable.addCell(makeCellLeft(chartOfAccountBean.getDescription()));
                transactionListTable.addCell(makeCellRightNoBorder(chartOfAccountBean.getDebit()));
                cell = makeCellRightNoBorder(chartOfAccountBean.getCredit());
                cell.setBorderWidthLeft(0.0f);
                cell.setBorderWidthRight(0.0f);
                transactionListTable.addCell(cell);
                cell = makeCellRightNoBorder(chartOfAccountBean.getBalance());
                cell.setBorderWidthLeft(0.0f);
                cell.setBorderWidthRight(0.1f);
                transactionListTable.addCell(cell);
            }
        }
        document.add(table);
        document.add(emptyTable);
        document.add(emptyTable);
        document.add(emptyTable);
        document.add(transactionHistoryDetTable);
        document.add(emptyTable);
        document.add(emptyTable);
        document.add(transactionListTable);
    }

    public void destroy() {
        document.close();
    }

    private PdfPCell makeCellCenter(String text) {
        Phrase phrase = new Phrase(text, blackBoldFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
        cell.setBorderWidthLeft(0.0f);
        return cell;
    }

    private PdfPTable makeTable(int rows) {
        PdfPTable table = new PdfPTable(rows);
        table.getDefaultCell().setBorder(0);
        table.getDefaultCell().setBorderWidth(0);
        return table;
    }

    private PdfPCell makeCell(Phrase phrase, int alignment) {
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(0);
        return cell;
    }

    private PdfPCell makeCellLeftRightBorder(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBorderWidthBottom(0.1f);
        cell.setBorderWidthRight(0.1f);
        return cell;
    }

    private PdfPCell makeCellRightNoBorder(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
        cell.setBorderWidthBottom(0.1f);
        return cell;
    }

    private PdfPCell makeCellLeftBottomBorder(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setBorderWidthBottom(0.1f);
        cell.setBorderWidthRight(0.0f);
        return cell;
    }

    private PdfPCell makeCellLeftNumberFormat(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBorderWidthBottom(0.1f);
        cell.setBorderWidthRight(0.0f);
        return cell;
    }

    private PdfPCell makeCellLeft(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBorderWidthBottom(0.1f);
        cell.setBorderWidthRight(0.0f);
        return cell;
    }

    public String createReport(TransactionHistoryReportDTO transactionHistoryVO, String fileName, String contextPath) throws Exception {
        this.initialize(fileName);
        this.createBody(transactionHistoryVO, contextPath);
        this.destroy();
        return fileName;
    }
}
