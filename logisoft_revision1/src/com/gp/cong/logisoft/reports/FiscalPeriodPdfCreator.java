package com.gp.cong.logisoft.reports;

import com.gp.cong.struts.LoadLogisoftProperties;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.gp.cvst.logisoft.reports.dto.fiscalPeriodDTO;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class FiscalPeriodPdfCreator extends ReportFormatMethods {

    Document document = null;
    PdfWriter pdfWriter = null;

    public void initialize(fiscalPeriodDTO periodDTO) throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4);
        document.setMargins(20, 20, 10, 10);
        String fileName = periodDTO.getFileName();
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
        pdfWriter.setOpenAction(action);
    }

    public void createBody(fiscalPeriodDTO periodDTO) throws DocumentException, MalformedURLException, IOException, Exception {
        String contextPath = periodDTO.getRealPath();
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image img = Image.getInstance(contextPath + imagePath);
        //GENERATING IMAGE
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        img.scalePercent(50);
        PdfPCell celL = new PdfPCell();
        celL.addElement(new Chunk(img, 217, -22));
        celL.setBorder(0);
        celL.setHorizontalAlignment(Element.ALIGN_CENTER);
        celL.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(celL);
        PdfPCell cell;
        PdfPTable bookTable = new PdfPTable(2);
        bookTable.setWidthPercentage(100);
        bookTable.setWidths(new float[]{50, 50});
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

        PdfPTable emptyRow = makeTable(1);
        PdfPCell emptyCell = makeCellRightNoBorder("");
        emptyRow.addCell(emptyCell);

        //HEADING WITH BACKGROUND COLOR
        String heading = "FISCAL PERIOD FOR THE YEAR " + periodDTO.getYear();
        Phrase headingPhrase = new Phrase(heading, headingFont);
        PdfPCell headingCell = new PdfPCell(headingPhrase);
        headingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headingCell.setVerticalAlignment(Element.ALIGN_TOP);
        headingCell.setPaddingTop(2);
        headingCell.setPaddingBottom(2);
        headingCell.setBorder(0);
        headingCell.setBackgroundColor(Color.LIGHT_GRAY);
        headingCell.setColspan(2);
        bookTable.addCell(headingCell);

        PdfPTable dateTable = new PdfPTable(1);
        dateTable.setWidthPercentage(100);
        dateTable.addCell(makeCellRightNoBorder(simpleDateFormat.format(new Date())));

        PdfPTable fiscalPeriodTable = new PdfPTable(4);
        fiscalPeriodTable.setWidthPercentage(100);
        fiscalPeriodTable.setWidths(new float[]{25, 25, 25, 25});
        fiscalPeriodTable.addCell(makeCellleftWithColor("Period"));
        fiscalPeriodTable.addCell(makeCellleftWithColor("Starting Date"));
        fiscalPeriodTable.addCell(makeCellleftWithColor("Ending Date"));
        fiscalPeriodTable.addCell(makeCellleftWithColor("Status"));

        PdfPTable fiscalPeriodTableValues = new PdfPTable(4);
        fiscalPeriodTableValues.setWidthPercentage(100);
        fiscalPeriodTableValues.setWidths(new float[]{25, 25, 25, 25});
        if (periodDTO.getPeriodList() != null && !periodDTO.getPeriodList().isEmpty()) {
            List periodList = periodDTO.getPeriodList();
            int count = 0;
            int size = periodList.size();
            while (size > 0) {
                fiscalPeriodDTO fisPeriodDTO = new fiscalPeriodDTO();
                fisPeriodDTO = (fiscalPeriodDTO) periodList.get(count);
                fiscalPeriodTableValues.addCell(makeCellleftNoBorder(fisPeriodDTO.getPeriod()));
                fiscalPeriodTableValues.addCell(makeCellleftNoBorder(fisPeriodDTO.getStartingdate()));
                fiscalPeriodTableValues.addCell(makeCellleftNoBorder(fisPeriodDTO.getEndingdate()));
                fiscalPeriodTableValues.addCell(makeCellleftNoBorder(fisPeriodDTO.getStatus()));
                count++;
                size--;
            }
        }
        document.add(table);
        document.add(bookTable);
        document.add(emptyRow);
        document.add(dateTable);
        document.add(emptyRow);
        document.add(fiscalPeriodTable);
        document.add(fiscalPeriodTableValues);

    }

    public void destroy() {
        document.close();
    }

    public String createReport(fiscalPeriodDTO periodDTO)throws Exception{
            this.initialize(periodDTO);
            this.createBody(periodDTO);
            this.destroy();
        return "fileName";
    }
}
