/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.reports;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.ArAccountNotesReportForm;
import com.logiware.bean.ReportBean;
import com.logiware.hibernate.dao.ArReportsDAO;
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
 * @author logiware
 */
public class ArAccountNotesReportCreator extends BaseReportCreator implements ConstantsInterface {
    private static final Logger log = Logger.getLogger(ArAccountNotesReportCreator.class);
    private ArAccountNotesReportForm arAccountNotesReportForm;

    public ArAccountNotesReportCreator() {
    }

    public ArAccountNotesReportCreator(ArAccountNotesReportForm arAccountNotesReportForm, String contextPath) {
        this.arAccountNotesReportForm = arAccountNotesReportForm;
        this.contextPath = contextPath;
    }

    private void init(String fileName) throws Exception {
        document = new Document(PageSize.A4.rotate());
        document.setMargins(5, 5, 5, 30);
        writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        writer.setUserunit(1f);
        writer.setPageEvent(new ArAccountNotesReportCreator(arAccountNotesReportForm, contextPath));
        document.open();
        writer.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 1f), writer));
    }

    private void writeArAccountNotesHeader() {
        String title = "AR Account Notes Report";
        PdfPCell titleCell = createCell(title, headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOTTOM, Color.LIGHT_GRAY);
        headerTable.addCell(titleCell);
    }

    private void writeHeader() throws Exception {
        headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        Image image = Image.getInstance(contextPath + imagePath);
        image.scalePercent(75);
        headerTable.addCell(createImageCell(image));
        headerTable.addCell(createEmptyCell(Rectangle.BOTTOM));
        writeArAccountNotesHeader();
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        pageTemplate = writer.getDirectContent().createTemplate(20, 10);
        pageTemplate.setBoundingBox(new Rectangle(-20, -20, 20, 50));
        try {
            helvFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("onOpenDocument failed :- ",e);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            writeHeader();
            document.add(headerTable);
        } catch (Exception e) {
            log.info("onStartPage failed :- ",e);
        }
    }

    private void writeContent() throws Exception {
        List<ReportBean> arAccountNotesList = new ArReportsDAO().getArAccountNotesList(arAccountNotesReportForm);
        contentTable = new PdfPTable(6);
        contentTable.setWidthPercentage(100);
        contentTable.setWidths(new float[]{20, 10, 40, 10, 10, 10});
        contentTable.addCell(createHeaderCell("Customer Name", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Customer Number", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Notes Description", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Followup Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Created Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.addCell(createHeaderCell("Created By", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
        contentTable.setHeaderRows(1);
        for (ReportBean reportBean : arAccountNotesList) {
            contentTable.addCell(createTextCell(reportBean.getCustomerName(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(reportBean.getCustomerNumber(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(reportBean.getNotesDescription(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(reportBean.getFollowupDate(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(reportBean.getCreatedDate(), blackNormalFont7, Rectangle.BOTTOM));
            contentTable.addCell(createTextCell(reportBean.getCreatedBy(), blackNormalFont7, Rectangle.BOTTOM));
        }
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

    public String createReport() throws Exception {
        StringBuilder fileName = new StringBuilder();
        fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ArReports/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append(arAccountNotesReportForm.getReportType()).append(".pdf");
        init(fileName.toString());
        writeContent();
        exit();
        return fileName.toString();
    }
}
