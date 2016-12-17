/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.ExportQuoteUtils;
import com.gp.cong.lcl.common.constant.ExportUnitQueryUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclExportsVoyageNotificationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.lcl.report.LclReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclExportNotiFicationForm;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.logiware.common.dao.PropertyDAO;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 *
 * @author aravindhan.v
 */
public class LclExportVoyageGeneralNotificationPdf implements LclCommonConstant, LclReportConstants {

    Paragraph p = null;
    PdfPCell cell = null;
    PdfPTable table = null;
    String companyCode = "";
    private Date ETA_FD = null;

    public void createPdf(String realPath, String outputFileName,
            Long notificationId, String companyName, String contact,
            LclFileNumber fileNumber) throws DocumentException, IOException, Exception {
        LclExportNotiFicationForm lclExportNotiFicationForm = new LclExportsVoyageNotificationDAO().getNotificationDetail(notificationId);
        LclSsDetail lclSsDetail = null;
        String pod = "", finalDest = "";
        String voyageHeading = "Voyage Change Notification";
        if (lclExportNotiFicationForm != null) {
            voyageHeading = "preview".equalsIgnoreCase(lclExportNotiFicationForm.getNoticeStatus()) ? "Voyage General Notification" : voyageHeading;
            lclSsDetail = new LclSsDetailDAO().findByTransMode(lclExportNotiFicationForm.getHeaderId(), "V");
            if (lclSsDetail != null) {
                pod = new LclUtils().getConcatenatedOriginByUnlocation(lclSsDetail.getArrival());
                finalDest = new LclUtils().getConcatenatedOriginByUnlocation(lclSsDetail.getLclSsHeader().getDestination());
            }
        }
        companyCode = new SystemRulesDAO().getSystemRules("CompanyCode");
        String path = LoadLogisoftProperties.getProperty(companyCode.equalsIgnoreCase("03") ? "application.image.logo"
                : "application.image.econo.logo");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        document.open();
        document.add(imageBlock(realPath, path));
        document.add(headerPage(voyageHeading));
        document.add(informationBlock(companyName, pod, finalDest, contact));
        document.add(changesBlock(lclSsDetail, lclExportNotiFicationForm));
        document.add(containerBlock(companyName, finalDest, fileNumber, lclExportNotiFicationForm));
        document.add(reasonBlock(lclExportNotiFicationForm));
        document.add(footerBlock());
        document.close();
    }

    public PdfPTable imageBlock(String realPath, String path) throws IOException, BadElementException, DocumentException {
        Font fontArialBold = FontFactory.getFont("Courier", 18f, Font.NORMAL);
        table = new PdfPTable(1);
        table.setWidths(new float[]{5.9f});
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        Image img = Image.getInstance(realPath + path);
        img.scalePercent(90);
        img.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(img);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(12f, "    (866) 326-6648", fontArialBold);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        String companyWebsite = new PropertyDAO()
                .getProperty(companyCode.equalsIgnoreCase("03")
                                ? "application.ECU.website" : "application.OTI.website");
        p = new Paragraph(28f, "   " + companyWebsite, fontArialBold);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable headerPage(String voyageHeading) throws DocumentException {
        table = new PdfPTable(1);
        table.setWidths(new float[]{6.9f});
        table.setWidthPercentage(100f);
        Font fontArialBold = FontFactory.getFont("Courier", 25f, Font.BOLD);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(32f, "" + voyageHeading, fontArialBold);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(1);
        p = new Paragraph(25f, " ", fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable informationBlock(String companyName, String portofDischarge,
            String finalDestination, String contact) throws IOException, BadElementException, DocumentException {
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        Font fontArialNormal = FontFactory.getFont("Courier", 12f, Font.NORMAL);
        Font fontArialBold = FontFactory.getFont("Courier", 12f, Font.BOLD);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(5f, "To Name : " + contact.substring(contact.indexOf("$") + 1, contact.indexOf(":")), fontArialNormal);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        String Company = contact.substring(contact.indexOf("#") + 1, contact.indexOf("$"));
        p = new Paragraph(25f, !"".equalsIgnoreCase(Company) ? "Company : " + Company : "Company : " + companyName, fontArialNormal);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(25f, "Voyage Port of Discharge (POD) : " + portofDischarge, fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(9f, "Shipment Final Destination (FD) : " + finalDestination, fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPCell allocateCell(String values, boolean border) {
        Font fontArialBold = FontFactory.getFont("Courier", 8f, Font.NORMAL);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(9f, values, fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        if (border) {
            cell.setBorderWidthRight(0.6f);
        }
        cell.addElement(p);
        return cell;
    }

    public PdfPTable changesBlock(LclSsDetail lclSsDetail,
            LclExportNotiFicationForm lclExportNotiFicationForm)
            throws IOException, BadElementException, DocumentException, Exception {
        table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        Font fontArialNormal = FontFactory.getFont("Courier", 12f, Font.NORMAL);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(9f, "Voyage Information Was : ", fontArialNormal);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        Date changedDatetime = lclExportNotiFicationForm.getEnterDateTime() != null ? lclExportNotiFicationForm.getEnterDateTime() : new Date();
        p = new Paragraph(9f, "Changes As Of:  " + DateUtils.formatDate(changedDatetime, "MM/dd/yyyy"), fontArialNormal);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthBottom(0.6f);
        table.addCell(cell);
        if (lclSsDetail != null) {
            String stateValue = lclSsDetail.getDeparture().getStateId() != null ? lclSsDetail.getDeparture().getStateId().getCode() : "";
            String vessel = "", pier = "", portLrd = "", sail_date = "", eta_date = "", ss_line = "", ss_voyage = "";
            if (!"preview".equalsIgnoreCase(lclExportNotiFicationForm.getNoticeStatus())) {
                vessel = lclExportNotiFicationForm.getVessel() != null ? lclExportNotiFicationForm.getVessel() : "";
                portLrd = lclExportNotiFicationForm.getPortLrd() != null && !"0".equalsIgnoreCase(lclExportNotiFicationForm.getPortLrd())
                        ? lclExportNotiFicationForm.getPortLrd() : "";
                sail_date = lclExportNotiFicationForm.getSailDate() != null ? DateUtils.formatDate(lclExportNotiFicationForm.getSailDate(), "MM/dd/yyyy") : "";
                eta_date = lclExportNotiFicationForm.getEtaDate() != null ? DateUtils.formatDate(lclExportNotiFicationForm.getEtaDate(), "MM/dd/yyyy") : "";
                ss_line = lclExportNotiFicationForm.getSsLine() != null ? lclExportNotiFicationForm.getSsLine() : "";
                ss_voyage = lclExportNotiFicationForm.getSsVoyage() != null ? lclExportNotiFicationForm.getSsVoyage() : "";
                pier = lclExportNotiFicationForm.getPier() != null ? lclExportNotiFicationForm.getPier().substring(0, lclExportNotiFicationForm.getPier().indexOf("(")) : "";
                pier = pier.replace("/", ", ");
            } else {
                vessel = CommonUtils.isNotEmpty(lclSsDetail.getSpReferenceName()) ? lclSsDetail.getSpReferenceName() : "";
                portLrd = CommonUtils.isNotEmpty(lclSsDetail.getRelayLrdOverride()) ? lclSsDetail.getRelayLrdOverride().toString() : "";
                sail_date = lclSsDetail.getStd() != null ? DateUtils.formatDate(lclSsDetail.getStd(), "MM/dd/yyyy") : "";
                eta_date = lclSsDetail.getSta() != null ? DateUtils.formatDate(lclSsDetail.getSta(), "MM/dd/yyyy") : "";
                ss_line = lclSsDetail.getSpAcctNo() != null ? lclSsDetail.getSpAcctNo().getAccountName() : "";
                ss_voyage = lclSsDetail.getSpReferenceNo() != null ? lclSsDetail.getSpReferenceNo() : "";
                pier = CommonUtils.isNotEmpty(lclExportNotiFicationForm.getPier()) ? lclExportNotiFicationForm.getPier().substring(0, lclExportNotiFicationForm.getPier().indexOf("(")) : "";
                pier = lclSsDetail.getDeparture().getUnLocationName() + "," + stateValue;
            }

            table.addCell(allocateCell("Voy#...:" + lclSsDetail.getLclSsHeader().getScheduleNo(), true));
            table.addCell(allocateCell("Voy#...:" + lclSsDetail.getLclSsHeader().getScheduleNo(), false));

            table.addCell(allocateCell("Vessel Name..:" + vessel, true));
            table.addCell(allocateCell("Vessel Name..:" + lclSsDetail.getSpReferenceName(), false));

            table.addCell(allocateCell("Pier.........:" + pier, true));
            String changedCity = lclSsDetail.getDeparture().getUnLocationName() + ", " + stateValue;
            table.addCell(allocateCell("Pier.........:" + changedCity, false));
            String lrdOverride = null != lclSsDetail.getRelayLrdOverride() ? lclSsDetail.getRelayLrdOverride().toString() : "";
            table.addCell(allocateCell("Port LRD ....:" + portLrd, true));
            table.addCell(allocateCell("Port LRD ....:" + lrdOverride, false));

            table.addCell(allocateCell("Sail Date....:" + sail_date, true));
            table.addCell(allocateCell("Sail Date....:" + DateUtils.formatDate(lclSsDetail.getStd(), "MM/dd/yyyy"), false));

            table.addCell(allocateCell("ETA POD Date :" + eta_date, true));
            table.addCell(allocateCell("ETA POD Date :" + DateUtils.formatDate(lclSsDetail.getSta(), "MM/dd/yyyy"), false));
            ETA_FD = lclSsDetail.getSta();
            table.addCell(allocateCell("Line Name....:" + ss_line, true));
            table.addCell(allocateCell("Line Name....:" + lclSsDetail.getSpAcctNo().getAccountName(), false));

            table.addCell(allocateCell("SS Voyage#...:" + ss_voyage, true));
            table.addCell(allocateCell("SS Voyage#...:" + lclSsDetail.getSpReferenceNo(), false));

            table.addCell(allocateCell(" ", true));
            table.addCell(allocateCell(" ", false));
        }
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthTop(0.6f);
        table.addCell(cell);

        return table;
    }

    public PdfPTable containerBlock(String companyName, String portofDischarge,
            LclFileNumber fileNumber, LclExportNotiFicationForm lclExportNotiFicationForm)
            throws IOException, BadElementException, DocumentException, Exception {
        table = new PdfPTable(4);
        table.setWidths(new float[]{2.5f, 3f, 1f, 5f});
        table.setWidthPercentage(100f);
        Font fontArialBold = FontFactory.getFont("Courier", 10f, Font.BOLD);
        Font bigArialBold = FontFactory.getFont("Courier", 15f, Font.BOLD);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(9f, "Container# ", fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(9f, "B/L# ", fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(9f, "D/R# ", fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(9f, "ETA-FINAL DESTINATION", bigArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        String containerNo = new LclExportsVoyageNotificationDAO()
                .getAllContainerNoFormVoyage(lclExportNotiFicationForm.getHeaderId().toString());
        p = new Paragraph(9f, "" + containerNo.replace(",", "\n"), fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        String blNumber = "";
        if (fileNumber.getState().equalsIgnoreCase("BL")) {
            if (null == fileNumber.getLclBl()) {
                Long consolidateId = new LCLBlDAO().findConsolidateBl(fileNumber.getId());
                LclBl lclbl = new LCLBlDAO().findById(consolidateId);
                fileNumber.setLclBl(lclbl);
            }
            if (null != fileNumber.getLclBl()) {
                String origin = fileNumber.getLclBl().getPortOfOrigin().getUnLocationCode();
                String dest = null != fileNumber.getLclBl().getFinalDestination()
                        ? fileNumber.getLclBl().getFinalDestination()
                        .getUnLocationCode() : fileNumber.getLclBl()
                        .getPortOfDestination().getUnLocationCode();
                blNumber = "ECCI-" + origin.substring(0, 3) + "-"
                        + dest.substring(2, dest.length()) + "-" + fileNumber.getFileNumber();
            }
        } else {
            blNumber = "";
        }
        p = new Paragraph(9f, "" + blNumber, fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(9f, "" + fileNumber.getFileNumber(), fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        boolean isDrPicked = new ExportUnitQueryUtils().isPickedFile(fileNumber.getId());
        if (isDrPicked) {
            p = new Paragraph(9f, DateUtils.formatDate(ETA_FD, "MM/dd/yyyy") + " " + portofDischarge, fontArialBold);
        } else {
            String eta_date = DateUtils.formatDate(fileNumber.getLclBooking().getFdEta(), "MM/dd/yyyy");
            eta_date = eta_date != null ? eta_date : "";
            p = new Paragraph(9f, eta_date + " " + portofDischarge, fontArialBold);
        }
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Paragraph(90f, " ", fontArialBold);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(2);
        cell.setColspan(4);
        table.addCell(cell);

        return table;
    }

    public PdfPTable reasonBlock(LclExportNotiFicationForm lclExportNotiFicationForm)
            throws IOException, BadElementException, DocumentException, Exception {
        User user = new UserDAO().findById(lclExportNotiFicationForm.getUserId() != null
                ? lclExportNotiFicationForm.getUserId().intValue() : 0);
        String userName = user != null ? user.getFirstName() : "";
        table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        Font fontArialBold = FontFactory.getFont("Courier", 10f, Font.BOLD);
        Font fontArialNormal = FontFactory.getFont("Courier", 10f, Font.NORMAL);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        p = new Paragraph(9f, "Reason:", fontArialBold);
        cell.addElement(p);
        table.addCell(cell);
        String voyageChangeReason = CommonUtils.isNotEmpty(lclExportNotiFicationForm.getVoyageReason())
                ? lclExportNotiFicationForm.getVoyageReason().toUpperCase() : "";
        String voyageComment = CommonUtils.isNotEmpty(lclExportNotiFicationForm.getVoyageComment())
                ? lclExportNotiFicationForm.getVoyageComment().toUpperCase() : "";
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        p = new Paragraph(9f, "" + voyageChangeReason + "\n" + voyageComment, fontArialNormal);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(20f, "Changes Made By:" + userName, fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(20f, "Contact#", fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(7f, null != user ? user.getTelephone() : "", fontArialNormal);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        String notes = "";
        if ("preview".equalsIgnoreCase(lclExportNotiFicationForm.getNoticeStatus())) {
            notes = lclExportNotiFicationForm.getRemarks() != null
                    ? lclExportNotiFicationForm.getRemarks().toUpperCase() : "";
        }
        p = new Paragraph(7f, "Notes:", fontArialBold);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        p = new Paragraph(7f, " " + notes, fontArialNormal);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        p = new Paragraph(10f, " ", fontArialNormal);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(1);
        cell.setColspan(2);
        table.addCell(cell);
        return table;
    }

    public PdfPTable footerBlock() throws IOException, BadElementException, DocumentException, Exception {
        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        Font fontArialNormal = FontFactory.getFont("Courier", 10f, Font.NORMAL);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(9f, "Date:" + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), fontArialNormal);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);

        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(9f, "End Of Report", fontArialNormal);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(9f, "Time :" + new Date().getHours() + ":"
                + new Date().getMinutes(), fontArialNormal);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }
}
