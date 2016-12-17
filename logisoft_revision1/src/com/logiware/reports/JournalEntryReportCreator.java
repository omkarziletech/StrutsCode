package com.logiware.reports;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.domain.LineItem;
import com.logiware.utils.LineItemComparator;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.TreeSet;

import org.apache.log4j.Logger;

public class JournalEntryReportCreator extends ReportFormatMethods
  implements ConstantsInterface
{
  private Logger log = Logger.getLogger(JournalEntryReportCreator.class);
  private Document document;
  private PdfWriter pdfWriter;
  private PdfTemplate totalNoOfPages;
  private BaseFont helveticaBold;
  private String contextPath;
  private JournalEntry journalEntry;

  public JournalEntryReportCreator()
  {
  }

  public JournalEntryReportCreator(String contextPath, JournalEntry journalEntry)
  {
    this.contextPath = contextPath;
    this.journalEntry = journalEntry;
  }

  public void init(String fileName, String contextPath, JournalEntry journalEntry) throws Exception {
    try {
      this.document = new Document(PageSize.A4);
      this.document.setMargins(10.0F, 10.0F, 20.0F, 40.0F);
      this.pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(fileName));
      this.pdfWriter.setPageEvent(new JournalEntryReportCreator(contextPath, journalEntry));
      this.document.open();
      PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(0, -1.0F, -1.0F, 1.0F), this.pdfWriter);
      this.pdfWriter.setOpenAction(action);
    } catch (Exception e) {
      this.log.info(e);
    }
  }

  public void onOpenDocument(PdfWriter pdfWriter, Document document)
  {
    try {
      this.totalNoOfPages = pdfWriter.getDirectContent().createTemplate(20.0F, 10.0F);
      this.totalNoOfPages.setBoundingBox(new Rectangle(-10.0F, -10.0F, 20.0F, 50.0F));
      this.helveticaBold = BaseFont.createFont("Helvetica-Bold", "Cp1252", false);
    } catch (Exception e) {
      this.log.info(e);
    }
  }

  private PdfPTable createHeader() throws Exception {
    String date = DateUtils.formatDate(new Date(), "MM/dd/yyyy");
    String debit = NumberUtils.formatNumber(this.journalEntry.getDebit(), "#,###,###,##0.00");
    String credit = NumberUtils.formatNumber(this.journalEntry.getCredit(), "#,###,###,##0.00");
    PdfPTable headerTable = new PdfPTable(1);
    headerTable.setWidthPercentage(100.0F);
    String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
    Image image = Image.getInstance(this.contextPath + imagePath);
    image.scalePercent(75.0F);
    PdfPCell logoCell = new PdfPCell(image);
    logoCell.setBorder(0);
    logoCell.setHorizontalAlignment(1);
    logoCell.setVerticalAlignment(4);
    headerTable.addCell(logoCell);
    String title = "Journal Entry Report";
    PdfPCell titleCell = makeCell(title, 1, 4, this.headingFont1, 0, Color.LIGHT_GRAY);
    headerTable.addCell(titleCell);
    PdfPTable journalEntryTable = new PdfPTable(6);
    journalEntryTable.setWidthPercentage(100.0F);
    journalEntryTable.setTotalWidth(new float[] { 15.0F, 20.0F, 15.0F, 20.0F, 15.0F, 20.0F });
    journalEntryTable.addCell(makeCell("Journal Entry:", 0, 4, this.blackBoldFont2, 0));
    journalEntryTable.addCell(makeCell(this.journalEntry.getJournalEntryId(), 0, 4, this.blackFont, 0));
    journalEntryTable.addCell(makeCell("Description:", 0, 4, this.blackBoldFont2, 0));
    journalEntryTable.addCell(makeCell(this.journalEntry.getJournalEntryDesc(), 0, 4, this.blackFont, 0));
    journalEntryTable.addCell(makeCell("", 0, 4, this.blackBoldFont2, 0));
    journalEntryTable.addCell(makeCell("", 0, 4, this.blackBoldFont2, 0));
    journalEntryTable.addCell(makeCell("Period:", 0, 4, this.blackBoldFont2, 0));
    journalEntryTable.addCell(makeCell(this.journalEntry.getPeriod(), 0, 4, this.blackFont, 0));
    journalEntryTable.addCell(makeCell("Subledger:", 0, 4, this.blackBoldFont2, 0));
    journalEntryTable.addCell(makeCell(this.journalEntry.getSourceCode().getCode(), 0, 4, this.blackFont, 0));
    journalEntryTable.addCell(makeCell("Memo:", 0, 4, this.blackBoldFont2, 0));
    journalEntryTable.addCell(makeCell(this.journalEntry.getMemo(), 0, 4, this.blackFont, 0));
    journalEntryTable.addCell(makeCell("Debit:", 0, 4, this.blackBoldFont2, 0));
    journalEntryTable.addCell(makeCell(debit, 0, 4, this.blackFont, 0));
    journalEntryTable.addCell(makeCell("Credit:", 0, 4, this.blackBoldFont2, 0));
    journalEntryTable.addCell(makeCell(credit, 0, 4, this.blackFont, 0));
    journalEntryTable.addCell(makeCell("Created Date:", 0, 4, this.blackBoldFont2, 0));
    journalEntryTable.addCell(makeCell(date, 0, 4, this.blackFont, 0));
    PdfPCell journalEntryCell = makeCell("", 0, 4, this.blackBoldFont2, 0);
    journalEntryCell.addElement(journalEntryTable);
    headerTable.addCell(journalEntryCell);
    return headerTable;
  }

  public void onStartPage(PdfWriter writer, Document document)
  {
    try {
      document.add(createHeader());
      PdfPTable lineItemTable = new PdfPTable(5);
      lineItemTable.setWidthPercentage(100.0F);
      lineItemTable.setTotalWidth(new float[] { 20.0F, 20.0F, 20.0F, 20.0F, 20.0F });
      lineItemTable.addCell(makeCell("Line Item Number", 1, this.headingFont, 15, Color.LIGHT_GRAY));
      lineItemTable.addCell(makeCell("GL Account", 1, this.headingFont, 15, Color.LIGHT_GRAY));
      lineItemTable.addCell(makeCell("Debit", 1, this.headingFont, 15, Color.LIGHT_GRAY));
      lineItemTable.addCell(makeCell("Credit", 1, this.headingFont, 15, Color.LIGHT_GRAY));
      lineItemTable.addCell(makeCell("Currency", 1, this.headingFont, 15, Color.LIGHT_GRAY));
      document.add(lineItemTable);
    } catch (Exception e) {
      this.log.info(e);
    }
  }

  public void writeContents() throws Exception {
    if ((null != this.journalEntry.getLineItemSet()) && (!this.journalEntry.getLineItemSet().isEmpty())) {
      TreeSet<LineItem> lineItems = new TreeSet(new LineItemComparator());
      lineItems.addAll(this.journalEntry.getLineItemSet());
      PdfPTable lineItemTable = new PdfPTable(5);
      lineItemTable.setWidthPercentage(100.0F);
      lineItemTable.setTotalWidth(new float[] { 20.0F, 20.0F, 20.0F, 20.0F, 20.0F });
      for (LineItem lineItem : lineItems) {
        String debit = NumberUtils.formatNumber(lineItem.getDebit(), "#,###,###,##0.00");
        String credit = NumberUtils.formatNumber(lineItem.getCredit(), "#,###,###,##0.00");
        lineItemTable.addCell(makeCell(lineItem.getLineItemId(), 1, this.blackFont, 15, Color.WHITE));
        lineItemTable.addCell(makeCell(lineItem.getAccount(), 1, this.blackFont, 15, Color.WHITE));
        lineItemTable.addCell(makeCell(debit, 2, this.blackFont, 15, Color.WHITE));
        lineItemTable.addCell(makeCell(credit, 2, this.blackFont, 15, Color.WHITE));
        lineItemTable.addCell(makeCell(lineItem.getCurrency(), 1, this.blackFont, 15, Color.WHITE));
      }
      this.document.add(lineItemTable);
    }
  }

  public void onEndPage(PdfWriter writer, Document document)
  {
    try {
      PdfContentByte cb = writer.getDirectContent();
      cb.saveState();
      BaseFont bfHelvNormal = BaseFont.createFont("Helvetica", "Cp1252", false);
      String lineOnBottom = "_________________________________________________________________________________________________________________________________";

      cb.beginText();
      cb.setFontAndSize(bfHelvNormal, 8.0F);
      cb.setTextMatrix(document.left(), document.bottomMargin());
      cb.showText(lineOnBottom);
      cb.endText();
      String text = "Page " + writer.getPageNumber() + " of ";
      float textBase = document.bottomMargin() - 20.0F;
      float textSize = this.helveticaBold.getWidthPoint(text, 12.0F);
      cb.beginText();
      cb.setFontAndSize(this.helveticaBold, 12.0F);
      cb.setTextMatrix(document.right() / 2.0F - 10.0F, textBase);
      cb.showText(text);
      cb.endText();
      cb.addTemplate(this.totalNoOfPages, document.right() / 2.0F - 10.0F + textSize, textBase);
      cb.restoreState();
    } catch (Exception e) {
      this.log.info(e);
    }
  }

  public void onCloseDocument(PdfWriter writer, Document document)
  {
    this.totalNoOfPages.beginText();
    this.totalNoOfPages.setFontAndSize(this.helveticaBold, 12.0F);
    this.totalNoOfPages.setTextMatrix(0.0F, 0.0F);
    this.totalNoOfPages.showText(String.valueOf(writer.getPageNumber() - 1));
    this.totalNoOfPages.endText();
  }

  public void exit() {
    this.document.close();
  }

  public String createReport(String contextPath, JournalEntry journalEntry) throws Exception {
    StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
    fileName.append("/Documents/JournalEntry/");
    fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
    File file = new File(fileName.toString());
    if (!file.exists()) {
      file.mkdirs();
    }
    fileName.append("Journal_Entry_").append(journalEntry.getJournalEntryId()).append(".pdf");
    init(fileName.toString(), contextPath, journalEntry);
    this.journalEntry = journalEntry;
    writeContents();
    exit();
    return fileName.toString();
  }
}