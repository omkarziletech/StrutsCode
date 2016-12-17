/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclExportsVoyageNotificationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.logiware.common.utils.RegexUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author aravindhan.v
 */
public class LclExportVoyageNotificationPdfCreator implements LclCommonConstant, LclReportConstants {

    Paragraph p = null;
    PdfPCell cell = null;
    PdfPTable table = null;
    private String email = "", fax = "";

    public void createPdf(String realPath, String outputFileName, String fileIds, Long notificationId) throws DocumentException, IOException, Exception {
        String companyCode = new SystemRulesDAO().getSystemRules("CompanyCode");
        String path = LoadLogisoftProperties.getProperty(companyCode.equalsIgnoreCase("03") ? "application.image.logo"
                : "application.image.econo.logo");
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        document.open();
        document.add(topTable());
        document.add(imageBlock(realPath, path));
        document.add(emptyRow());
        document.add(headerPage());
        document.add(emptyRow());
        document.add(titlePage());
        document.add(contentPage(fileIds, notificationId));
        document.close();
    }

    public PdfPTable topTable() throws Exception {
        Font colorfont = FontFactory.getFont("Arial", 14f, Font.NORMAL, new BaseColor(00, 00, 255));
        Font fontArialBold = FontFactory.getFont("Arial", 14f, Font.NORMAL);
        table = new PdfPTable(new float[]{6f, 1f});
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(8f, "DATE:", fontArialBold);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(8f, "" + DateUtils.formatStringDateToAppFormatMMM(new Date()), colorfont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable imageBlock(String realPath, String path) throws IOException, BadElementException, DocumentException {
        table = new PdfPTable(1);
        table.setWidths(new float[]{5.9f});
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        Image img = Image.getInstance(realPath + path);
        img.scalePercent(120);
        img.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(img);
        table.addCell(cell);
        return table;
    }

    public PdfPTable headerPage() {
        table = new PdfPTable(new float[]{1});
        table.setWidthPercentage(100f);
        Font fontArialBold = FontFactory.getFont("Arial", 20f, Font.BOLD, new BaseColor(00, 102, 00));
        cell = new PdfPCell();
        p = new Paragraph(10f, "Voyage Notification  Report", fontArialBold);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setBorder(0);
        table.addCell(cell);
        return table;
    }

    public PdfPTable emptyRow() {
        table = new PdfPTable(new float[]{1});
        table.setWidthPercentage(100f);
        Font fontArialBold = FontFactory.getFont("Arial", 14f, Font.BOLD);
        cell = new PdfPCell();
        cell.setPadding(12f);
        p = new Paragraph(2f, "", fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setBorder(0);
        table.addCell(cell);
        return table;
    }

    public PdfPCell cell(String values) {
        Font fontArialBold = FontFactory.getFont("Arial", 10f, Font.BOLD);
        cell = new PdfPCell();
        cell.setPadding(12f);
        p = new Paragraph(2f, values, fontArialBold);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        return cell;
    }

    public PdfPTable titlePage() {
        table = new PdfPTable(new float[]{0.7f, 0.7f, 4, 4});
        table.setWidthPercentage(100f);
        table.addCell(cell("S:No"));
        table.addCell(cell("F:No"));
        table.addCell(cell("Email"));
        table.addCell(cell("Fax"));
        return table;
    }

    public PdfPTable contentPage(String fileIds, Long notificationId) throws Exception {
        table = new PdfPTable(new float[]{0.7f, 0.7f, 4, 4});
        table.setWidthPercentage(100f);
        Font fontArialBold = FontFactory.getFont("Arial", 10f, Font.NORMAL);
        Font blackContentBoldFont8 = FontFactory.getFont("Arial", 10f, Font.NORMAL, BaseColor.BLUE);
        if (CommonUtils.isNotEmpty(fileIds)) {
            int index = 0;
            for (String fileId : fileIds.split(",")) {
                index++;
                String contacts = new LclExportsVoyageNotificationDAO().getContactEmailAndFaxListByCodeI(Long.parseLong(fileId), EMAIL_TYPE_E1, FAX_TYPE_F1, notificationId);
                String fileNumber = new LclFileNumberDAO().getFileNumberByFileId(fileId);
                if (contacts != null) {
                    email = "";
                    fax = "";
                    for (String contact : contacts.split(",")) {
                        String content = contact.substring(contact.indexOf(":") + 1, contact.length());
                        if (RegexUtil.isEmail(content)) {
                            email += contact.substring(1) + ":";
                        } else if (RegexUtil.isFax(content)) {
                            fax += contact.substring(1) + ":";
                        }
                    }
                }

                cell = new PdfPCell();
                p = new Paragraph(13f, Integer.toString(index), fontArialBold);
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                p = new Paragraph(13f, fileNumber, fontArialBold);
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                p = new Paragraph();
                String emailContent[] = email.split(":");
                for (int i = 0; i < emailContent.length; i++) {
                    if (RegexUtil.isEmail(emailContent[i])) {
                        p.add(new Paragraph(9f, emailContent[i], blackContentBoldFont8));
                    } else {
                        p.add(new Chunk("" + emailContent[i].replace("$", " ") + ": ", fontArialBold));
                    }
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                p = new Paragraph();
                String faxContent[] = fax.split(":");
                for (int i = 0; i < faxContent.length; i++) {
                    if (RegexUtil.isFax(faxContent[i])) {
                        p.add(new Paragraph(9f, faxContent[i], blackContentBoldFont8));
                    } else {
                        p.add(new Chunk("" + faxContent[i].replace("$", " ") + ": ", fontArialBold));
                    }
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);
            }
        }
        return table;
    }
}
