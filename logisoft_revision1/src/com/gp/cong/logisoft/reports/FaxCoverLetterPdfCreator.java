package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.gp.cong.logisoft.bc.print.PrintConfigBC;
import com.gp.cong.logisoft.struts.form.PrintConfigForm;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class FaxCoverLetterPdfCreator extends ReportFormatMethods {

    Document document = null;
    PdfWriter pdfWriter = null;

    private void initialize(String fileName) throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4.rotate());
        document.setMargins(40, 40, 30, 30);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
    }

    private void createBody(String toFaxNumber,PrintConfigForm printConfigForm, String realPath) throws DocumentException, IOException, IOException, Exception {
        PrintConfigBC printConfigBC = new PrintConfigBC();
        // table for company details and logo
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image img = Image.getInstance(realPath + imagePath);
        PdfPTable imagetable = new PdfPTable(1);
        imagetable.setWidthPercentage(70);
        PdfPCell companyNameCell = makeCellCenterWithBoldFont(new SystemRulesDAO().getSystemRulesByCode("CompanyName"));
        companyNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        companyNameCell.setVerticalAlignment(Element.ALIGN_CENTER);
        imagetable.addCell(companyNameCell);
        img.scalePercent(17);
        PdfPCell imageCell = new PdfPCell();
        imageCell.addElement(new Chunk(img, 0, -22));
        imageCell.setBorder(0);
        imageCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        imageCell.setVerticalAlignment(Element.ALIGN_LEFT);
        imagetable.addCell(imageCell);
        document.add(imagetable);

        PdfPTable bookTable = new PdfPTable(2);
        bookTable.setWidthPercentage(70);
        bookTable.setWidths(new float[]{35, 35});
        bookTable.getDefaultCell().setPadding(0);
        bookTable.getDefaultCell().setBorderWidth(0.5f);
        bookTable.getDefaultCell().setBorderWidthLeft(0.0f);
        bookTable.getDefaultCell().setBorderWidthRight(0.0f);

        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        bookTable.addCell(makeCellCenterNoBorder(""));
        //Heading
        String heading = "For Your Information";
        Phrase headingPhrase = new Phrase(heading, headingFont1);
        PdfPCell headingCell = new PdfPCell(headingPhrase);
        headingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headingCell.setVerticalAlignment(Element.ALIGN_TOP);
        headingCell.setPaddingTop(-2);
        headingCell.setPaddingBottom(2);
        headingCell.setBorder(0);
        headingCell.setBackgroundColor(Color.LIGHT_GRAY);
        headingCell.setColspan(2);
        bookTable.addCell(headingCell);
        document.add(bookTable);

        //empty
        PdfPTable emptyTable = makeTable(2);
        PdfPCell emptyCell = makeCellCenterNoBorder("");
        emptyCell.setColspan(2);
        emptyTable.addCell(emptyCell);
        document.add(emptyTable);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(70);
        float colWidth = 70 / 2;
        table.setWidths(new float[]{10f, colWidth});
        PdfPCell lineCell = new PdfPCell();
        lineCell.setBorder(1);
        lineCell.setColspan(2);
        table.addCell(lineCell);
        table.addCell(makeCellleftNoBorderWithBoldFont4("To:"));
        table.addCell(makeCellleftNoBorder(printConfigForm.getToName()));
        table.addCell(makeCellleftNoBorderWithBoldFont4("Fax Number:"));
        table.addCell(makeCellleftNoBorder(toFaxNumber));
        table.addCell(emptyCell);
        table.addCell(emptyCell);
        table.addCell(emptyCell);
        table.addCell(makeCellleftNoBorderWithBoldFont4("From:"));
        table.addCell(makeCellleftNoBorder(printConfigForm.getFromName()));
        table.addCell(makeCellleftNoBorderWithBoldFont4("Fax Number:"));
        table.addCell(makeCellleftNoBorder(printConfigForm.getFromFaxNumber()));
        table.addCell(makeCellleftNoBorderWithBoldFont4("Home Phone:"));
        table.addCell(makeCellleftNoBorder(printConfigForm.getHomePhone()));
        table.addCell(makeCellleftNoBorderWithBoldFont4("Business Phone:"));
        table.addCell(makeCellleftNoBorder(printConfigForm.getBusinessPhone()));
        table.addCell(emptyCell);
        table.addCell(emptyCell);
        table.addCell(emptyCell);
        PdfPCell dateCell = makeCellleftNoBorder(simpleDateFormat.format(new Date()));
        table.addCell(makeCellleftNoBorderWithBoldFont4("Date & Time:"));
        table.addCell(dateCell);
        table.addCell(makeCellleftNoBorderWithBoldFont4("Pages Sent:"));
        table.addCell(makeCellleftNoBorder(""));
        table.addCell(makeCellleftNoBorderWithBoldFont4("Subject:"));
        table.addCell(makeCellleftNoBorder(printConfigForm.getSubject()));
        table.addCell(emptyCell);
        table.addCell(emptyCell);
        table.addCell(lineCell);
        document.add(table);
        PdfPTable messageTable = new PdfPTable(1);
        messageTable.setWidthPercentage(70);
        messageTable.addCell(makeCellleftNoBorder(printConfigForm.getMessage()));
        messageTable.addCell(lineCell);
        document.add(messageTable);
    }

    private void destroy() {
        document.close();
    }

    public String createFaxCoverLetter(String toFaxNumber,PrintConfigForm printConfigForm, String fileName, String realPath)throws Exception {
            this.initialize(fileName);
            this.createBody(toFaxNumber,printConfigForm, realPath);
            this.destroy();
            return fileName;
    }
}
