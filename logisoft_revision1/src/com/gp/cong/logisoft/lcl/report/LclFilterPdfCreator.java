package com.gp.cong.logisoft.lcl.report;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

public class LclFilterPdfCreator extends LclReportFormatMethods {

    protected BaseFont helv;

    public LclFilterPdfCreator() {
    }

    public void createPdf(String realPath, String outputFileName) throws Exception {
        document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(15, 15, 10, 10);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        document.open();
        document.add(heading());
        document.add(emptyTable());
        document.add(onStartPage());
        document.add(emptyTable());
        document.add(contentTable());
        document.add(emptyTable());
        document.add(bodyTable());
        document.add(emptyTable());
        document.add(contentTable1());
        document.add(emptyTable());
        document.add(contentTable2());
        document.add(emptyTable());
        document.add(contentTable3());
        document.add(emptyTable());
        document.add(contentTable6());
        document.add(emptyTable());
        document.add(contentTable4());
        document.add(emptyTable());
        document.add(contentTable5());
        document.add(emptyTable());
        document.close();
    }

    public PdfPTable onStartPage() throws Exception {
        Paragraph hp = null;
        table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        hp = new Paragraph("Help Screen for Inventory Filter Options:", headingBoldFont);
        cell.addElement(hp);
        table.addCell(cell);
        return table;
    }

    public PdfPTable contentTable() throws Exception {
        Paragraph hp = null;
        Paragraph p = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        hp = new Paragraph("Inventory All :", boxRedFont);
        p = new Paragraph("This option will show both LCL bookings received and also not yet received. " + "\n"
                + "Example:  For Direct Sailings, input a POL and a POD (i.e. Miami to Kingston) " + "\n"
                + "Example:  For Inland voyages, input a POO (like Chicago), or both a POO and a POL (like Chicago to Miami). " + "\n"
                + "Note:  If a POO is filled in,  then the Current location of any cargo received must match the POO.", blackBoldFont);

        cell.addElement(hp);
        cell.addElement(p);
        table.addCell(cell);
        return table;

    }

    public PdfPTable emptyTable() {
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setFixedHeight(10f);
        table.addCell(cell);
        return table;
    }

    public PdfPTable bodyTable() {
        Paragraph hp = null;
        Paragraph p = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        hp = new Paragraph("Inventory Booked:", boxRedFont);
        p = new Paragraph("Same as Inventory All, except this will only show bookings that are not yet received ", blackBoldFont);
        cell.addElement(hp);
        cell.addElement(p);
        table.addCell(cell);
        return table;

    }

    public PdfPTable contentTable1() {
        Paragraph hp = null;
        Paragraph p = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        hp = new Paragraph("Inventory Received :", boxRedFont);
        p = new Paragraph("Same as Inventory All, except this will only show bookings that have been received.", blackBoldFont);
        cell.addElement(hp);
        cell.addElement(p);
        table.addCell(cell);
        return table;

    }

    public PdfPTable contentTable2() {
        Paragraph hp = null;
        Paragraph p = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        hp = new Paragraph("Inventory In Transit :", boxRedFont);
        p = new Paragraph("This will only show items where their current location is In Transit. " + "\n"
                + "Example:   Input a POO and a POL (like Chicago to Miami), and you will see all items currently en route." + "\n"
                + "Example:   Input just a POL, and you will see all items en route to the POL (like Miami, or New York)." + "\n"
                + "Example:   Input a POL and POD, (like Miami to Kingston)  and you will see all items en route to the POL, but only for the POD desired.", blackBoldFont);

        cell.addElement(hp);
        cell.addElement(p);
        table.addCell(cell);
        return table;

    }

    public PdfPTable contentTable3() {
        Paragraph hp = null;
        Paragraph p = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        hp = new Paragraph("Inventory at POL :", boxRedFont);
        p = new Paragraph("This will show cargo received, and the current location of the cargo matches the POL." + "\n"
                + "Example:  Input a POL (like Miami or New York), and the results will show all cargo located at the POL. " + "\n"
                + "Example:  Input a POL and a POD, (like Miami to Kingston), and you will see all items at the POL (Miami), going to the POD (Kingston)." + "\n"
                + "Example:  Input a POO, POL, and POD – (like Chicago – Miami – Kingston), this will show all bookings that are in Miami, that originated in Chicago, and are destined for Kingston"
                + "", blackBoldFont);
        cell.addElement(hp);
        cell.addElement(p);
        table.addCell(cell);
        return table;

    }

    public PdfPTable contentTable4() {
        Paragraph hp = null;
        Paragraph p = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        hp = new Paragraph("BL Pool :", boxRedFont);
        p = new Paragraph("Show all BLs that have been started, but not yet posted.  The user can then choose to see by owner, with a “Me”  option.", blackBoldFont);
        cell.addElement(hp);
        cell.addElement(p);
        table.addCell(cell);
        return table;

    }

    public PdfPTable contentTable5() {
        Paragraph hp = null;
        Paragraph p = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        hp = new Paragraph("Loaded with No B/L :", boxRedFont);
        p = new Paragraph("This view shows all shipments that have already been loaded, but a B/L has not yet been posted against the shipment.", blackBoldFont);
        cell.addElement(hp);
        cell.addElement(p);
        table.addCell(cell);
        return table;

    }

    public PdfPTable contentTable6() {
        Paragraph hp = null;
        Paragraph p = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        hp = new Paragraph("Additional Filter Options", boxRedFont);
        cell.addElement(hp);
        table.addCell(cell);
        return table;

    }

    public PdfPTable heading() {
        Font blackBoldFont = new Font(FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.RED);
        Paragraph p = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(2f, "Help Screen for Inventory Filter Options: ", blackBoldFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }
}
