package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.reports.dto.SearchCustomerSampleDTO;
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

public class AllCollectorsReportPdfCreator {

    Font blackBoldFont = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
    Font headingFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont1 = new Font(Font.BOLD, 14, 0, Color.BLACK);
    Font blackBoldHeadingFont = new Font(Font.HELVETICA, 7, Font.BOLD, Color.BLACK);
    Font blackFont = new Font(Font.HELVETICA, 9, 0, Color.BLACK);
    float[] twoColumnDefinitionSize = {50F, 50F};
    float[] threeColumnDefinitionSize = {33.33F, 33.33F, 33.33F};
    float[] fourColumnDefinitionSize = {26F, 29F, 15F, 30F};
    Document document = null;
    PdfWriter pdfWriter = null;

    public void initialize(String fileName, String contextPath) throws DocumentException, MalformedURLException, IOException {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        PdfAction action = PdfAction.gotoLocalPage(1, new
        PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
        pdfWriter.setOpenAction(action);
    }

    public void createBody(List<SearchCustomerSampleDTO> searchCollectorList, String contextPath) throws DocumentException, MalformedURLException, IOException, Exception {
        //start of image
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image img = Image.getInstance(contextPath + imagePath);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        img.scalePercent(50);
        PdfPCell celL = new PdfPCell();
        celL.addElement(new Chunk(img, 200, -22));
        celL.setBorder(0);
        celL.setHorizontalAlignment(Element.ALIGN_CENTER);
        celL.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(celL);
        document.add(table);
        //image
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
        //Heading
        String heading = "Aging Report For All Collectors";
        Phrase headingPhrase = new Phrase(heading, headingFont1);
        PdfPCell headingCell = new PdfPCell(headingPhrase);
        headingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headingCell.setVerticalAlignment(Element.ALIGN_TOP);
        //  headingCell.setPadding(100);
        headingCell.setPaddingTop(-2);
        headingCell.setPaddingBottom(2);
        headingCell.setBorder(0);
        headingCell.setBackgroundColor(Color.LIGHT_GRAY);
        headingCell.setColspan(2);
        bookTable.addCell(headingCell);
        document.add(bookTable);
        //Date
        PdfPTable DateCell = makeTable(1);
        DateCell.setWidthPercentage(100);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
        PdfPCell Emptycell1 = makeCellRightNoBorder(simpleDateFormat.format(new Date()));
        DateCell.addCell(Emptycell1);
        document.add(DateCell);

        //empty
        PdfPTable Empty = makeTable(1);
        PdfPCell Emptycell = makeCellRightNoBorder("");
        Empty.addCell(Emptycell);

        PdfPTable emptytable = makeTable(6);
        PdfPCell emptycell = makeCellCenterNoBorder("");
        Empty.addCell(emptycell);
        //THIS IS FOR FIELD HEADINGS
        PdfPTable agingTable = new PdfPTable(2);
        agingTable.setWidthPercentage(100);
        agingTable.setWidths(new float[]{50, 49});
        SearchCustomerSampleDTO searchCustSampleDTO = new SearchCustomerSampleDTO();
        searchCustSampleDTO = searchCollectorList.get(0);

        //THIS IS FOR FIRST ROW
        agingTable.addCell(makeCellleftNoBorder(ReportConstants.CUSTOMERNO + "  " + searchCustSampleDTO.getAcctNo()));
        agingTable.addCell(makeCellleftNoBorder(ReportConstants.CUSTOMERNAME + "  " + searchCustSampleDTO.getAcctName()));
        agingTable.addCell(makeCellleftNoBorder(""));
        document.add(agingTable);
        document.add(Empty);
        //THIS IS FOR TABLE HEADER
        int i = 0;
        int size = searchCollectorList.size();
        PdfPTable agingFielsTableHeading = new PdfPTable(5);
        agingFielsTableHeading.setWidthPercentage(100);
        agingFielsTableHeading.setWidths(new float[]{20, 20, 15, 15, 15});
        agingFielsTableHeading.addCell(makeCellleftwithBorder(ReportConstants.INVOICENO));
        agingFielsTableHeading.addCell(makeCellleftwithBorder(ReportConstants.BILLOFLADING));
        agingFielsTableHeading.addCell(makeCellleftwithBorder(ReportConstants.INVOICEDATE));
        agingFielsTableHeading.addCell(makeCellCenterForDoubleHeading(ReportConstants.AGING));
        agingFielsTableHeading.addCell(makeCellCenterForDoubleHeading(ReportConstants.BALANCE));
        document.add(agingFielsTableHeading);

        //THIS IS ITERATOR FOR FIELD LIST
        PdfPTable agingFielsTable = new PdfPTable(5);
        agingFielsTable.setWidthPercentage(100);
        agingFielsTable.setWidths(new float[]{20, 20, 15, 15, 15});
        String AcctName = "";
        if (searchCollectorList.get(i) != null) {
            while (size > 0) {
                //THIS IS FOR FIELD LIST ITERATOR
                searchCustSampleDTO = searchCollectorList.get(i);

                if (!AcctName.equals(searchCustSampleDTO.getAcctName()) && !AcctName.equals("")) {
                    document.add(agingFielsTable);
                    agingFielsTable.deleteBodyRows();
                    document.newPage();
                    document.add(table);
                    document.add(bookTable);
                    document.add(DateCell);

                    //Adding for Account Name and Number
                    PdfPTable agingTable1 = new PdfPTable(2);
                    agingTable1.setWidthPercentage(100);
                    agingTable1.setWidths(new float[]{50, 49});
                    searchCustSampleDTO = searchCollectorList.get(i);
                    //THIS IS FOR FIRST ROW
                    agingTable1.addCell(makeCellleftNoBorder(ReportConstants.CUSTOMERNO + "  " + searchCustSampleDTO.getAcctNo()));
                    agingTable1.addCell(makeCellleftNoBorder(ReportConstants.CUSTOMERNAME + "  " + searchCustSampleDTO.getAcctName()));
                    agingTable1.addCell(makeCellleftNoBorder(""));

                    document.add(agingTable1);
                    document.add(Empty);
                    document.add(agingFielsTableHeading);
                }
                AcctName = searchCustSampleDTO.getAcctName();
                agingFielsTable.addCell(makeCellleftNoBorder(searchCustSampleDTO.getInvoiceNum()));
                agingFielsTable.addCell(makeCellleftNoBorder(searchCustSampleDTO.getBillofladingno()));
                agingFielsTable.addCell(makeCellleftNoBorder(searchCustSampleDTO.getInvoiceDate()));
                agingFielsTable.addCell(makeCellCenterForDouble(searchCustSampleDTO.getAging()));
                agingFielsTable.addCell(makeCellCenterForDouble(searchCustSampleDTO.getBalance()));
                i++;
                size--;
                if (size == 0) {
                    document.add(agingFielsTable);
                }
            }
        }
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

    private PdfPCell makeCellLeftNoBorder(String text) {
        Phrase phrase = new Phrase(text, blackBoldFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.0f);
        return cell;
    }

    private PdfPCell makeCellRightNoBorder(String text) {
        Phrase phrase = new Phrase(text, blackBoldFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
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
        //cell.height('2');
        cell.setBorderWidthLeft(0f);
        return cell;
    }

    private PdfPCell makeCellCenterNoBorder(String text) {
        PdfPCell cell = makeCellCenter(text);
        cell.setBorderWidthLeft(0f);
        return cell;
    }
    //THIS IS FOR LEFT BORDER WITH HEADING FONT

    private PdfPCell makeCellleftwithBorder(String text) {
        Phrase phrase = new Phrase(text, headingFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.0f);
        cell.setBorderWidthBottom(0.1f);
        return cell;
    }

    private PdfPCell makeCellCenterForDoubleHeading(String text) {
        Phrase phrase = new Phrase(text, headingFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
        cell.setBorderWidthRight(0.0f);
        cell.setBorderWidthBottom(0.1f);
        return cell;
    }

    private PdfPCell makeCellCenterForDouble(String text) {
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
        return cell;
    }

    private PdfPCell makeCellCenterForDouble(Double value) {
        String value1 = String.valueOf(value);
        Phrase phrase = new Phrase(value1, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
        cell.setBorderWidthRight(0.1f);
        cell.setBorderWidthBottom(0.1f);
        return cell;
    }

    public String createReport(List<SearchCustomerSampleDTO> searchCollectorList, String fileName, String contextPath)throws Exception {
            this.initialize(fileName, contextPath);
            this.createBody(searchCollectorList, contextPath);
            this.destroy();
        return "fileName";
    }
}
