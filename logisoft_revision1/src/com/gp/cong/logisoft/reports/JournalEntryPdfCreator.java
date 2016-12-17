package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.domain.LineItem;
import com.gp.cvst.logisoft.hibernate.dao.JournalEntryDAO;
import com.gp.cvst.logisoft.hibernate.dao.LineItemDAO;
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

public class JournalEntryPdfCreator {

    Document document = null;
    Font blackBoldFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont1 = new Font(Font.BOLD, 12, 0, Color.BLACK);
    Font blackFont = new Font(Font.HELVETICA, 9, 0, Color.BLACK);

    public void initialize(String fileName) throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4.rotate());
        document.setMargins(10, 10, 10, 10);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        PdfAction action = PdfAction.gotoLocalPage(1, new
        PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
        pdfWriter.setOpenAction(action);
    }

    public void createBody(Batch batch, String contextPath) throws DocumentException, MalformedURLException, IOException, Exception {
        //THIS IS FOR IMAGE SET
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image img = Image.getInstance(contextPath + imagePath);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        img.scalePercent(50);
        PdfPCell celL = new PdfPCell();
        celL.addElement(new Chunk(img, 320, -22));
        celL.setBorder(0);
        celL.setHorizontalAlignment(Element.ALIGN_CENTER);
        celL.setVerticalAlignment(Element.ALIGN_CENTER);

        table.addCell(celL);

        PdfPTable emptyTable = makeTable(1);
        emptyTable.addCell(makeCellCenter(""));
        emptyTable.addCell(makeCellCenter(""));
        emptyTable.addCell(makeCellCenter(""));
        emptyTable.addCell(makeCellCenter(""));
        //THIS IS FOR BATCH DETAILS
        PdfPTable captionTable = new PdfPTable(1);
        captionTable.setWidthPercentage(100);
        PdfPCell cell;

        String heading = ReportConstants.BATCHDETAILS;
        Phrase headingPhrase = new Phrase(heading, headingFont1);
        PdfPCell headingCell = new PdfPCell(headingPhrase);
        headingCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headingCell.setVerticalAlignment(Element.ALIGN_TOP);
        headingCell.setBorderWidth(0.1f);
        headingCell.setBorderWidthBottom(0.0f);
        headingCell.setPaddingTop(-2);
        headingCell.setPaddingBottom(2);
        headingCell.setBackgroundColor(Color.LIGHT_GRAY);
        headingCell.setColspan(2);
        captionTable.addCell(headingCell);

        PdfPTable journalEntryFormTable = makeTable(6);
        journalEntryFormTable.setWidths(new float[]{14, 23, 12, 20, 8, 13});
        journalEntryFormTable.setWidthPercentage(100);

        journalEntryFormTable.setWidthPercentage(100);
        cell = makeCellLeft(ReportConstants.BATCHNO);
        cell.setBorderWidthLeft(0.1f);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        journalEntryFormTable.addCell(cell);
        journalEntryFormTable.addCell(makeCellLeft(batch.getBatchId().toString()));
        journalEntryFormTable.addCell(makeCellLeft(ReportConstants.DESCRIPTION));
        journalEntryFormTable.addCell(makeCellLeft(batch.getBatchDesc()));
        journalEntryFormTable.addCell(makeCellLeft(""));
        journalEntryFormTable.addCell(makeCellLeftRightBorder(""));
        cell = makeCellLeft(ReportConstants.DEBITTOTAL);
        cell.setBorderWidthLeft(0.1f);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        journalEntryFormTable.addCell(cell);
        journalEntryFormTable.addCell(makeCellLeftForDouble(batch.getTotalDebit()));
        journalEntryFormTable.addCell(makeCellLeft(ReportConstants.CREDITTOTAL));
        journalEntryFormTable.addCell(makeCellLeftForDouble(batch.getTotalCredit()));
        journalEntryFormTable.addCell(makeCellLeft(ReportConstants.STATUS));
        journalEntryFormTable.addCell(makeCellLeftRightBorder(batch.getStatus()));

        PdfPTable mainTable = new PdfPTable(1);
        mainTable.setWidthPercentage(100);

        document.add(table);
        document.add(emptyTable);
        document.add(emptyTable);
        document.add(captionTable);
        document.add(journalEntryFormTable);
        document.add(emptyTable);
        document.add(emptyTable);
        JournalEntryDAO journalEntryDAO = new JournalEntryDAO();
        List journalEntryList = journalEntryDAO.findByProperty("batchId", batch.getBatchId());

        for (Iterator iter = (Iterator) journalEntryList.iterator(); iter.hasNext();) {
            //THIS IS FOR JOURNALENTRY
            PdfPTable journalEntryFormTableDetailsHeading = makeTable(9);
            journalEntryFormTableDetailsHeading.setWidthPercentage(100);
            journalEntryFormTableDetailsHeading.setWidths(new float[]{8, 23, 5, 7, 8, 18, 10, 10, 11});
            JournalEntry journalEntry = (JournalEntry) iter.next();
            String journalEntryString = ReportConstants.JOURNALENTRYID + journalEntry.getJournalEntryId();
            Phrase phrase = new Phrase(journalEntryString, headingFont1);
            cell = new PdfPCell(phrase);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(9);
            cell.setBorderWidthBottom(0.0f);
            cell.setBorderWidth(0.1f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            journalEntryFormTableDetailsHeading.addCell(cell);

            cell = makeCellLeftBottomBorder(ReportConstants.ENTRYNO);
            cell.setBorderWidthLeft(0.1f);
            journalEntryFormTableDetailsHeading.addCell(cell);
            journalEntryFormTableDetailsHeading.addCell(makeCellLeftBottomBorder(ReportConstants.DESCRIPTION));
            journalEntryFormTableDetailsHeading.addCell(makeCellLeftBottomBorder(ReportConstants.PERIOD));
            journalEntryFormTableDetailsHeading.addCell(makeCellLeftBottomBorder(ReportConstants.DATE));
            journalEntryFormTableDetailsHeading.addCell(makeCellLeftBottomBorder(ReportConstants.SOURCECODE));
            journalEntryFormTableDetailsHeading.addCell(makeCellLeftBottomBorder(ReportConstants.SOURCEDESCRIPTION));
            journalEntryFormTableDetailsHeading.addCell(makeCellLeftBottomBorder(ReportConstants.DEBIT));
            journalEntryFormTableDetailsHeading.addCell(makeCellLeftBottomBorder(ReportConstants.CREDIT));
            cell = makeCellLeftBottomBorder(ReportConstants.MEMO);
            cell.setBorderWidthRight(0.2f);
            journalEntryFormTableDetailsHeading.addCell(cell);
            cell = makeCellLeft(journalEntry.getJournalEntryId());
            cell.setBorderWidthLeft(0.1f);
            cell.setPaddingTop(5);
            cell.setPaddingBottom(5);
            journalEntryFormTableDetailsHeading.addCell(cell);
            journalEntryFormTableDetailsHeading.addCell(makeCellLeft(journalEntry.getJournalEntryDesc()));
            journalEntryFormTableDetailsHeading.addCell(makeCellLeft(journalEntry.getPeriod()));
            journalEntryFormTableDetailsHeading.addCell(makeCellLeftForDate(journalEntry.getJeDate()));
            if (journalEntry.getSourceCode() != null && journalEntry.getSourceCode().getCode() != null) {
                journalEntryFormTableDetailsHeading.addCell(makeCellLeft(journalEntry.getSourceCode().getCode()));
            } else {
                journalEntryFormTableDetailsHeading.addCell(makeCellLeft(""));
            }
            journalEntryFormTableDetailsHeading.addCell(makeCellLeft(journalEntry.getSourceCodeDesc()));
            journalEntryFormTableDetailsHeading.addCell(makeCellLeftForDouble(journalEntry.getDebit()));
            journalEntryFormTableDetailsHeading.addCell(makeCellLeftForDouble(journalEntry.getCredit()));
            journalEntryFormTableDetailsHeading.addCell(makeCellLeftRightBorder(journalEntry.getMemo()));

            cell = makeCellLeft("");
            cell.setColspan(9);
            cell.setBorderWidthBottom(0.0f);
            journalEntryFormTableDetailsHeading.addCell(cell);
            journalEntryFormTableDetailsHeading.addCell(cell);
            journalEntryFormTableDetailsHeading.addCell(cell);







            /*
            PdfPTable journalEntryTable= new PdfPTable(6);
            journalEntryTable.setWidths(new float[]{7,23,12,22,12,24});

            JournalEntry journalEntry=(JournalEntry)iter.next();
            String journalEntryString = ReportConstants.JOURNALENTRYID+journalEntry.getJournalEntryId();
            Phrase phrase = new Phrase(journalEntryString, headingFont1);
            cell=new PdfPCell(phrase);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(6);
            cell.setBorderWidthBottom(0.0f);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            journalEntryTable.addCell(cell);

            cell = makeCellLeft(ReportConstants.ENTRYNO);
            cell.setBorderWidthLeft(0.1f);cell.setPaddingTop(5);cell.setPaddingBottom(5);
            journalEntryTable.addCell(cell);
            journalEntryTable.addCell(makeCellLeft(journalEntry.getJournalEntryId()));
            journalEntryTable.addCell(makeCellLeft(ReportConstants.DESCRIPTION));
            journalEntryTable.addCell(makeCellLeft(journalEntry.getJournalEntryDesc()));
            journalEntryTable.addCell(makeCellLeft(ReportConstants.PERIOD));
            journalEntryTable.addCell(makeCellLeftRightBorder(journalEntry.getPeriod()));
            cell = makeCellLeft(ReportConstants.DATE);
            cell.setBorderWidthLeft(0.1f);cell.setPaddingTop(5);cell.setPaddingBottom(5);
            journalEntryTable.addCell(cell);
            journalEntryTable.addCell(makeCellLeftForDate(journalEntry.getJeDate()));
            journalEntryTable.addCell(makeCellLeft(ReportConstants.SOURCECODE));
            if(journalEntry.getSourceCode()!=null && journalEntry.getSourceCode().getCode()!=null)
            journalEntryTable.addCell(makeCellLeft(journalEntry.getSourceCode().getCode()));
            else
            journalEntryTable.addCell(makeCellLeft(""));
            journalEntryTable.addCell(makeCellLeft(ReportConstants.SOURCEDESCRIPTION));
            journalEntryTable.addCell(makeCellLeftRightBorder(journalEntry.getSourceCodeDesc()));
            cell = makeCellLeft(ReportConstants.DEBIT);
            cell.setBorderWidthLeft(0.1f);cell.setPaddingTop(5);cell.setPaddingBottom(5);
            journalEntryTable.addCell(cell);
            journalEntryTable.addCell(makeCellLeftForDouble(journalEntry.getDebit()));
            journalEntryTable.addCell(makeCellLeft(ReportConstants.CREDIT));
            journalEntryTable.addCell(makeCellLeftForDouble(journalEntry.getCredit()));
            journalEntryTable.addCell(makeCellLeft(ReportConstants.MEMO));
            journalEntryTable.addCell(makeCellLeftRightBorder(journalEntry.getMemo()));*/
            //THIS IS FOR LIST OF LINE ITEMS
            PdfPTable lineItemsTable = new PdfPTable(8);
            lineItemsTable.setWidths(new float[]{8, 9, 9, 21, 11, 14, 23, 5});
            lineItemsTable.setWidthPercentage(100);

            cell = makeCellLeftBottomBorder(ReportConstants.LISTOFLINEITEMS);
            cell.setColspan(8);
            lineItemsTable.addCell(cell);
            cell = makeCellLeftBottomBorder(ReportConstants.ACCTNO);
            lineItemsTable.addCell(cell);
            cell = makeCellLeftBottomBorder(ReportConstants.DEBITAMT);
            lineItemsTable.addCell(cell);
            cell = makeCellLeftBottomBorder(ReportConstants.CREDITAMT);
            lineItemsTable.addCell(cell);
            cell = makeCellLeftBottomBorder(ReportConstants.ACCTDESC);
            lineItemsTable.addCell(cell);
            cell = makeCellLeftBottomBorder(ReportConstants.ITEMNO);
            lineItemsTable.addCell(cell);
            cell = makeCellLeftBottomBorder(ReportConstants.REFERENCE);
            lineItemsTable.addCell(cell);
            cell = makeCellLeftBottomBorder(ReportConstants.DESC);
            lineItemsTable.addCell(cell);
            cell = makeCellLeftBottomBorder(ReportConstants.CURRENCY);
            lineItemsTable.addCell(cell);

            LineItemDAO lineItemDAO = new LineItemDAO();
            List lineItemList = lineItemDAO.findByProperty("journalEntryId", journalEntry.getJournalEntryId());
            for (Iterator iter1 = (Iterator) lineItemList.iterator(); iter1.hasNext();) {
                LineItem lineItem = (LineItem) iter1.next();
                cell = makeCellLeft(lineItem.getAccount());
                cell.setBorderWidthLeft(0.0f);
                cell.setPaddingTop(5);
                cell.setPaddingBottom(5);
                lineItemsTable.addCell(cell);
                if (lineItem != null && lineItem.getDebit() != null) {
                    cell = makeCellLeftForDouble(lineItem.getDebit());
                }else{
                    cell = makeCellLeft("");
                }

                cell.setBorderWidthLeft(0.0f);
                cell.setPaddingTop(5);
                cell.setPaddingBottom(5);
                lineItemsTable.addCell(cell);
                if (lineItem != null && lineItem.getCredit() != null) {
                    cell = makeCellLeftForDouble(lineItem.getCredit());
                }else{
                    cell = makeCellLeft("");
                }

                cell.setBorderWidthLeft(0.0f);
                cell.setPaddingTop(5);
                cell.setPaddingBottom(5);
                lineItemsTable.addCell(cell);
                if (lineItem != null && lineItem.getAccountDesc() != null) {
                    cell = makeCellLeft(lineItem.getAccountDesc());
                }else{
                    cell = makeCellLeft("");
                }

                cell.setBorderWidthLeft(0.0f);
                cell.setPaddingTop(5);
                cell.setPaddingBottom(5);
                lineItemsTable.addCell(cell);
                if (lineItem != null && lineItem.getLineItemId() != null) {
                    cell = makeCellLeft(lineItem.getLineItemId());
                }else{
                    cell = makeCellLeft("");
                }

                cell.setBorderWidthLeft(0.0f);
                cell.setPaddingTop(5);
                cell.setPaddingBottom(5);
                lineItemsTable.addCell(cell);
                if (lineItem != null && lineItem.getReference() != null) {
                    cell = makeCellLeft(lineItem.getReference());
                }else{
                    cell = makeCellLeft("");
                }
                cell.setBorderWidthLeft(0.0f);
                cell.setPaddingTop(5);
                cell.setPaddingBottom(5);
                lineItemsTable.addCell(cell);
                if (lineItem != null && lineItem.getReferenceDesc() != null) {
                    cell = makeCellLeft(lineItem.getReferenceDesc());
                }else{
                    cell = makeCellLeft("");
                }
                cell.setBorderWidthLeft(0.0f);
                cell.setPaddingTop(5);
                cell.setPaddingBottom(5);
                lineItemsTable.addCell(cell);
                if (lineItem != null && lineItem.getCurrency() != null) {
                    cell = makeCellLeft(lineItem.getCurrency());
                }
                cell.setBorderWidthLeft(0.0f);
                cell.setPaddingTop(5);
                cell.setPaddingBottom(5);
                lineItemsTable.addCell(cell);
            }
            if (journalEntry.getLineItemSet().size() > 0) {
                cell = new PdfPCell(lineItemsTable);
                cell.setColspan(9);
                cell.setPadding(9);
                journalEntryFormTableDetailsHeading.addCell(cell);
            } else {
                cell = makeCellLeftBottomBorder(ReportConstants.COMMENT);
                cell.setColspan(9);
                cell.setBorderWidthLeft(0.1f);
                cell.setBorderWidthRight(0.1f);
                cell.setPadding(9);
                journalEntryFormTableDetailsHeading.addCell(cell);
            }
            cell = makeCellLeft("");
            cell.setColspan(9);
            cell.setBorder(0);
            cell.setPadding(20);
            journalEntryFormTableDetailsHeading.addCell(cell);

            document.add(journalEntryFormTableDetailsHeading);
        //document.newPage();
        }
    }

    public void destroy() {
        document.close();
    }

    private PdfPCell makeCellCenterNoBorder(String text) {
        PdfPCell cell = makeCellCenter(text);
        cell.setBorderWidthLeft(0f);
        return cell;
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

    private PdfPCell makeCellCenter3(String text) {
        Phrase phrase = new Phrase(text, headingFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setBorderWidthRight(0.1f);


        cell.setBorderWidthBottom(0.1f);
        return cell;
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

    private PdfPCell makeCellLeftBottomBorder(String text) {


        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setBorderWidthBottom(0.1f);
        cell.setBorderWidthRight(0.0f);
        return cell;
    }

    private PdfPCell makeCellLeftForDate(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String text = sdf.format(date);
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

    private PdfPCell makeCellLeftForDouble(double value) {
        NumberFormat numformat = new DecimalFormat("##,###,##0.00");
        String text = numformat.format(value);
        Phrase phrase = new Phrase(text, blackFont);
        PdfPCell cell = makeCell(phrase, Element.ALIGN_JUSTIFIED);
        cell.setBorderWidthBottom(0.1f);
        cell.setBorderWidthRight(0.0f);

        return cell;
    }

    /*private PdfPCell makeCellLeftForDouble(Double value){
    String value1=String.valueOf(value);
    Phrase phrase = new Phrase(value1, blackFont);
    PdfPCell cell = makeCell(phrase,Element.ALIGN_RIGHT);
    cell.setBorderWidthRight(0.5f);
    cell.setBackgroundColor(Color.LIGHT_GRAY);
    return cell;
    }*/
    public String createReport(Batch batch, String fileName, String contextPath) throws Exception {
            this.initialize(fileName);
            this.createBody(batch, contextPath);
            this.destroy();
        return "fileName";
    }
}
