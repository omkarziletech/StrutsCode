package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class CheckPrintingPdfCreator {

    Font blackBoldFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont1 = new Font(Font.BOLD, 18, 0, Color.BLACK);
    Font blackBoldHeadingFont = new Font(Font.HELVETICA, 7, Font.BOLD, Color.BLACK);
    Font blackFont = new Font(Font.HELVETICA, 9, 0, Color.BLACK);
    float[] twoColumnDefinitionSize = {50F, 50F};
    float[] threeColumnDefinitionSize = {33.33F, 33.33F, 33.33F};
    float[] fourColumnDefinitionSize = {26F, 29F, 15F, 30F};
    Document document = null;

    public void initialize() throws FileNotFoundException, DocumentException {

        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        PdfWriter.getInstance(document, new FileOutputStream("D:/CheckPrintingReport.pdf"));
        document.open();
    }

    public void createBody() throws DocumentException, MalformedURLException, IOException {

        // Heading table
        PdfPTable headingTable = new PdfPTable(3);
        headingTable.setWidthPercentage(100);
        headingTable.setWidths(new float[]{33, 33, 33});

        Image img = Image.getInstance("logo.jpg");
        img.scalePercent(25);
        PdfPCell pcell = new PdfPCell();
        pcell.addElement(new Chunk(img, 0, -18));
        pcell.setBorder(0);
        pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pcell.setVerticalAlignment(Element.ALIGN_LEFT);
        headingTable.addCell(pcell);
        headingTable.addCell(makeCellCenterNoBorder("REGIONS BANK"));
        headingTable.addCell(makeCellCenterNoBorder("177846"));


        //Empty table
        PdfPTable Empty = makeTable(1);
        PdfPCell Emptycell = makeCellCenterNoBorder("");
        Empty.addCell(Emptycell);


        //Check Details table
        PdfPTable checkDetailsTable = new PdfPTable(3);
        checkDetailsTable.setWidthPercentage(100);
        checkDetailsTable.setWidths(new float[]{33, 33, 33});

        checkDetailsTable.addCell(makeCellCenterNoBorder(" "));
        checkDetailsTable.addCell(makeCellCenterNoBorder(""));
        checkDetailsTable.addCell(makeCellCenterNoBorder("CHECK NO :"));
        checkDetailsTable.addCell(makeCellCenterNoBorder(""));
        checkDetailsTable.addCell(makeCellCenterNoBorder(""));
        checkDetailsTable.addCell(makeCellCenterNoBorder("DATE          :"));
        checkDetailsTable.addCell(makeCellCenterNoBorder(""));
        checkDetailsTable.addCell(makeCellCenterNoBorder(""));
        checkDetailsTable.addCell(makeCellCenterNoBorder("DOLLARS   :"));


        //Amount Table
        PdfPTable amountTable = makeTable(1);
        amountTable.setWidthPercentage(100);

        amountTable.addCell(makeCellleftNoBorder("AMOUNT IN WORDS        :" + " FOURTEEN THOUSAND THREE HUNDRED EUGHTY ONE DOLLARS@% CENTS"));


        //Pay Order Table
        PdfPTable payOrderTable = makeTable(1);
        payOrderTable.setWidthPercentage(100);

        payOrderTable.addCell(makeCellleftNoBorder("PAY TO THE ORDER OF :"));


        //Vendor Table
        PdfPTable vendorTable = makeTable(3);
        vendorTable.setWidthPercentage(100);
        vendorTable.setWidths(new float[]{53, 33, 33});

        vendorTable.addCell(makeCellleftNoBorder("Vendor :" + "(00600)EVERGREEN AMERICAN CORP"));
        vendorTable.addCell(makeCellCenterNoBorder("Check # :" + "177847"));
        vendorTable.addCell(makeCellCenterNoBorder("23/3/2009"));


        //InvoiceDetails Table
        PdfPTable invoiceDetailsTable = makeTable(7);
        invoiceDetailsTable.setWidthPercentage(100);
        invoiceDetailsTable.setWidths(new float[]{12, 12, 12, 12, 12, 12, 28});

        invoiceDetailsTable.addCell(makeCellCenter3("Invoice Number"));
        invoiceDetailsTable.addCell(makeCellCenter3("Invoice Date"));
        invoiceDetailsTable.addCell(makeCellCenter3("Amount Paid"));
        invoiceDetailsTable.addCell(makeCellCenter3("Invoice Number"));
        invoiceDetailsTable.addCell(makeCellCenter3("Invoice Date"));
        invoiceDetailsTable.addCell(makeCellCenter3("Amount Paid"));
        invoiceDetailsTable.addCell(makeCellCenter3(""));

        for (int i = 0; i < 6; i++) {
            invoiceDetailsTable.addCell(makeCellCenter1("0.0"));
            invoiceDetailsTable.addCell(makeCellCenter1("0.0"));
            invoiceDetailsTable.addCell(makeCellCenter1("0.0"));
        }


        //Invoice And GL Details Table
        PdfPTable invoiceGLTable = makeTable(12);
        invoiceGLTable.setWidthPercentage(100);
        invoiceGLTable.setWidths(new float[]{8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8});

        invoiceGLTable.addCell(makeCellCenter3("Invoice Number"));
        invoiceGLTable.addCell(makeCellCenter3("Invoice Date"));
        invoiceGLTable.addCell(makeCellCenter3("Amount Paid"));
        invoiceGLTable.addCell(makeCellCenter3("GL#"));
        invoiceGLTable.addCell(makeCellCenter3("Invoice Number"));
        invoiceGLTable.addCell(makeCellCenter3("Invoice Date"));
        invoiceGLTable.addCell(makeCellCenter3("Amount Paid"));
        invoiceGLTable.addCell(makeCellCenter3("GL#"));
        invoiceGLTable.addCell(makeCellCenter3("Invoice Number"));
        invoiceGLTable.addCell(makeCellCenter3("Invoice Date"));
        invoiceGLTable.addCell(makeCellCenter3("Amount Paid"));
        invoiceGLTable.addCell(makeCellCenter3("GL#"));

        for (int i = 0; i < 6; i++) {
            invoiceGLTable.addCell(makeCellCenter1("0.0"));
            invoiceGLTable.addCell(makeCellCenter1("0.0"));
            invoiceGLTable.addCell(makeCellCenter1("0.0"));
        }

        document.add(Empty);
        document.add(Empty);
        document.add(headingTable);
        document.add(Empty);
        document.add(Empty);
        document.add(Empty);
        document.add(Empty);
        document.add(Empty);
        document.add(Empty);
        document.add(checkDetailsTable);
        document.add(Empty);
        document.add(amountTable);
        document.add(Empty);
        document.add(payOrderTable);
        document.add(Empty);
        document.add(Empty);
        document.add(Empty);
        document.add(Empty);
        document.add(vendorTable);
        document.add(invoiceDetailsTable);
        document.add(Empty);
        document.add(Empty);
        document.add(Empty);
        document.add(Empty);
        document.add(Empty);
        document.add(Empty);
        document.add(vendorTable);
        document.add(invoiceGLTable);
    }

    public void destroy() {
        document.close();
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

    private PdfPCell makeTableCell(PdfPTable table) {

        PdfPCell cell = new PdfPCell(table);
        cell.setBorderWidth(0f);
        cell.setBorderWidthBottom(0.5f);
        cell.setPadding(0f);
        return cell;
    }

    private PdfPCell makeCellCenter4(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setBorderWidthRight(0.1f);
        //cell.setBorderWidthLeft(1.0f);
        //cell.setBorderWidthTop(1.0f);

        cell.setBorderWidthBottom(0.1f);
        return cell;
    }

    private PdfPCell makeCellLeft(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.5f);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        return cell;
    }

    private PdfPCell makeCellLeftForDouble(Double value) {
        String value1 = String.valueOf(value);
        Phrase phrase = new Phrase(value1, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
        cell.setBorderWidthRight(0.5f);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        return cell;
    }

    private PdfPCell makeCellLeftNoBorder(String text) {
        Phrase phrase = new Phrase(text, blackBoldFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.0f);
        return cell;
    }

    private PdfPCell makeCellCenter1(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.1f);
        cell.setBorderWidthBottom(0.1f);
        //cell.setBorderWidthLeft(1.0f);
        //cell.setBorderWidthTop(1.0f);
        return cell;
    }

    private PdfPCell makeCellRight(String text) {
        Phrase phrase = new Phrase(text, blackBoldFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
        cell.setBorderWidthRight(0.1f);
        cell.setBorderWidthBottom(0.1f);
        return cell;
    }

    private PdfPCell makeCellCenter(String text) {
        Phrase phrase = new Phrase(text, blackBoldFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
        cell.setBorderWidthLeft(0.5f);
        return cell;
    }

    private PdfPCell makeCellleftNoBorder(String text) {
        Phrase phrase = new Phrase(text, blackBoldFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBorderWidthLeft(0f);
        return cell;
    }

    private PdfPCell makeCellCenterNoBorder(String text) {
        PdfPCell cell = makeCellCenter(text);
        cell.setBorderWidthLeft(0f);
        return cell;
    }

    private PdfPCell makeCellRightNoBorder(String text) {
        PdfPCell cell = makeCellRight(text);
        cell.setBorderWidthLeft(0f);
        return cell;
    }

    private PdfPCell makeCellCenter3(String text) {
        Phrase phrase = new Phrase(text, headingFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setBorderWidthRight(0.1f);
        cell.setBorderWidthBottom(0.1f);
        return cell;
    }

    private PdfPCell makeCellCenterForDouble(Double value) {
        String value1 = String.valueOf(value);
        Phrase phrase = new Phrase(value1, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
        //cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setBorderWidthRight(0.1f);
        //cell.setBorderWidthLeft(1.0f);
        //cell.setBorderWidthTop(1.0f);

        cell.setBorderWidthBottom(0.1f);
        return cell;
    }

    /*public String createReport(List<TrialReportDTO> trialReportDTOList,String fileName,String contextPath){
    try {
    this.initialize(fileName);
    this.createBody(trialReportDTOList,contextPath);
    this.destroy();
    } catch (Exception e) {
    e.printStackTrace();
    }
    return "fileName";
    }*/
    public static void main(String[] args)throws Exception {
            CheckPrintingPdfCreator checkPrinting = new CheckPrintingPdfCreator();
            checkPrinting.initialize();
            checkPrinting.createBody();
            checkPrinting.destroy();
    }
}
