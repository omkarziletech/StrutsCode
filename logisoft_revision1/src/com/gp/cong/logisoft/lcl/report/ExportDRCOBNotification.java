/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Mei
 */
public class ExportDRCOBNotification extends LclReportFormatMethods {

    private LclBl lclbl;
    private LclBooking lclBooking;
    private Date podEta = null;
    private Date etaFD = null;
    private Date sailDate = null;
    private String portofSailing = null;
    private String sealOut = "";
    private String carrierName = null;
    private String unitNumber = "";
    private StringBuilder vesselDetails = new StringBuilder();
    private StringBuilder voyageDetails = new StringBuilder();
    private String cityCode = "";

    public ExportDRCOBNotification(LclBl lclbl, LclBooking lclBooking) throws Exception {
        this.lclbl = lclbl;
        this.lclBooking = lclBooking;
        ExportVoyageSearchModel pickedDetails = new LclUnitSsDAO().getPickedVoyageByVessel(lclBooking.getFileNumberId(), "E");
        LclUnitSs lclUnitSs = null;
        if (pickedDetails != null && CommonUtils.isNotEmpty(pickedDetails.getUnitSsId())) {
            lclUnitSs = new LclUnitSsDAO().findById(Long.parseLong(pickedDetails.getUnitSsId()));
        }
        if (lclUnitSs != null) {
            LclSsDetail lclSsDetail = lclUnitSs.getLclSsHeader().getVesselSsDetail();
            if (CommonFunctions.isNotNull(lclSsDetail.getStd())) {
                sailDate = lclSsDetail.getStd();
            }
            if (CommonFunctions.isNotNull(lclSsDetail.getSta())) {
                podEta = lclSsDetail.getSta();
            }
            if (CommonFunctions.isNotNull(lclSsDetail.getSta())) {
                etaFD = lclSsDetail.getSta();
            }
            if (CommonFunctions.isNotNull(lclUnitSs.getSUHeadingNote())) {
                sealOut = lclUnitSs.getSUHeadingNote();
            }
            if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceName())) {
                vesselDetails.append(lclSsDetail.getSpReferenceName()).append("  ").append("- ");
            }
            if (CommonFunctions.isNotNull(lclSsDetail.getTransMode())) {
                vesselDetails.append(lclSsDetail.getTransMode()).append(".");
            }
            if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceNo())) {
                vesselDetails.append(lclSsDetail.getSpReferenceNo());
            }
            if (null != lclSsDetail.getSpAcctNo()
                    && CommonFunctions.isNotNull(lclSsDetail.getSpAcctNo().getAccountName())) {
                carrierName = lclSsDetail.getSpAcctNo().getAccountName();
            }
            if (null != lclSsDetail.getDeparture()) {
                portofSailing = lclSsDetail.getDeparture().getUnLocationName();
                if (null != lclSsDetail.getDeparture().getStateId()
                        && CommonUtils.isNotEmpty(lclSsDetail.getDeparture().getStateId().getCode())) {
                    portofSailing += ", " + lclSsDetail.getDeparture().getStateId().getCode();
                }
            }
            unitNumber = lclUnitSs.getLclUnit().getUnitNo();
            LclSsHeader lclSsHeader = lclUnitSs.getLclSsHeader();
            cityCode = lclSsHeader.getOrigin().getUnLocationCode().substring(2);
            voyageDetails.append(cityCode);
            if (CommonFunctions.isNotNull(lclSsHeader.getDestination())) {
                voyageDetails.append("-").append(lclSsHeader.getDestination().getUnLocationCode());
            }
            if (CommonFunctions.isNotNull(lclSsHeader.getScheduleNo())) {
                voyageDetails.append("-").append(lclSsHeader.getScheduleNo());
            }
        }

    }

    public void createReport(String realPath, String outputFileName, HttpServletRequest request) throws Exception {
        document = new Document(PageSize.A4, 20, 20, 10, 30);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        document.open();
        document.add(headerTable(request));
        document.add(consigneeDetails(request));
        document.add(sailDetailsTable());
        document.add(unitTable());
        document.add(footerTable());
        document.close();
    }

    public PdfPTable headerTable(HttpServletRequest request) throws Exception {
        
        String mnemonicCode = lclBooking.getLclFileNumber().getBusinessUnit();
        String path = LoadLogisoftProperties.getProperty(mnemonicCode.equalsIgnoreCase("ECU") ? "application.image.logo"
                : "application.image.econo.logo");
        String companyWebsite = LoadLogisoftProperties.getProperty(mnemonicCode.equalsIgnoreCase("ECU") ? "application.ECU.website"
                : mnemonicCode.equalsIgnoreCase("OTI") ? "application.OTI.website" : "application.Econo.website");
        
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        String realPath = request.getSession().getServletContext().getRealPath("/");
        Image img = Image.getInstance(realPath + path);
        img.scalePercent(80);
        img.scaleAbsoluteWidth(200);
        img.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(img);
        table.addCell(cell);

        table.addCell(makeCellNoBorderFontalign("(305)693-5133 1-866-326-6648", 2f, Element.ALIGN_CENTER, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign("" + companyWebsite, 2f, Element.ALIGN_CENTER, 0, blackNormalCourierFont12f));
        //empty cell
        table.addCell(createEmptyCell(0, 4, 0));

        table.addCell(makeCellNoBorderFontalign("L C L  C a r g o  Confirmed On-Board Notification", 2f, Element.ALIGN_CENTER, 0, blackNormalCourierFont18f));
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        table.addCell(cell);

        return table;
    }

    public PdfPTable consigneeDetails(HttpServletRequest request) throws DocumentException {
        String consigneeName = "";
        if (CommonFunctions.isNotNull(lclbl.getConsContact())
                && CommonFunctions.isNotNull(lclbl.getConsContact().getCompanyName())) {
            consigneeName = lclbl.getConsContact().getCompanyName();
        }

        User user = (User) request.getSession().getAttribute("loginuser");
        String userEmail = CommonUtils.isNotEmpty(user.getEmail()) ? user.getEmail() : "";
        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1f, 3.4f, 2.5f});
        //name
        table.addCell(makeCellNoBorderFontalign("FROM:", 2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));

        table.addCell(makeCellNoBorderFontalign("" + userEmail, 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign("REF.# " + cityCode + "-" + lclBooking.getLclFileNumber().getFileNumber(), 2f, Element.ALIGN_LEFT, 0, blackboldCourierFont11f));
        //email
        table.addCell(makeCellNoBorderFontalign("", -2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign("", -2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign("VOYAGE# " + voyageDetails.toString().toUpperCase(), -2f, Element.ALIGN_LEFT, 0, blackboldCourierFont11f));
        //empty cell
        table.addCell(createEmptyCell(0, 2, 3));
        //carrier
        table.addCell(makeCellNoBorderFontalign("CARRIER:", 2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));
        if (CommonFunctions.isNotNull(carrierName)) {
            table.addCell(makeCellNoBorderFontalign("" + carrierName.toUpperCase(), 2f, Element.ALIGN_LEFT, 2, blackNormalCourierFont12f));
        } else {
            table.addCell(makeCellNoBorderFontalign("", 2f, Element.ALIGN_LEFT, 2, blackNormalCourierFont12f));
        }
        //vessel
        table.addCell(makeCellNoBorderFontalign("VESSEL:", -2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));
        if (CommonFunctions.isNotNull(vesselDetails)) {
            table.addCell(makeCellNoBorderFontalign("" + vesselDetails.toString().toUpperCase(), -2f, Element.ALIGN_LEFT, 2, blackNormalCourierFont12f));
        } else {
            table.addCell(makeCellNoBorderFontalign("", -2f, Element.ALIGN_LEFT, 2, blackNormalCourierFont12f));
        }
        //consignee
        table.addCell(makeCellNoBorderFontalign("CONSIGNEE:", -2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign("" + consigneeName, -2f, Element.ALIGN_LEFT, 2, blackNormalCourierFont12f));
        //empty cell
        table.addCell(createEmptyCell(0, 2, 3));
        //borderLine
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBorderWidthBottom(0.6f);
        table.addCell(cell);
        return table;
    }

    public PdfPTable sailDetailsTable() throws DocumentException, Exception {
        String portofDischarge = "";
        if (null != lclBooking.getPortOfDestination()) {
            portofDischarge = lclBooking.getPortOfDestination().getUnLocationName();
        }
        String fd = "";
        if (null != lclBooking.getFinalDestination()) {
            fd = lclBooking.getFinalDestination().getUnLocationName();
        }
        table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.7f, 2.6f, 1.5f, 1.1f});
        //Forwader name
//        table.addCell(makeCellNoBorderFontalign("FRT FORWARDER REF:", 2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));
//
//        String fwdRef = "";
//        if (CommonFunctions.isNotNull(lclbl.getFwdAcct())) {
//            if (CommonFunctions.isNotNull(lclbl.getFwdAcct().getGeneralInfo())) {
//                if (CommonFunctions.isNotNull(lclbl.getFwdAcct().getGeneralInfo())) {
//                    fwdRef = lclbl.getFwdAcct().getGeneralInfo().getFwFmcNo();
//                }
//            }
//        }
//        table.addCell(makeCellNoBorderFontalign("" + fwdRef, 2f, Element.ALIGN_LEFT, 3, blackNormalCourierFont12f));
        //empty cell
        table.addCell(createEmptyCell(0, 2, 4));
        //carrier
//        table.addCell(makeCellNoBorderFontalign("P/O#:", 2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));
//        table.addCell(makeCellNoBorderFontalign("", 2f, Element.ALIGN_LEFT, 3, blackNormalCourierFont12f));
        //portsailing
        table.addCell(makeCellNoBorderFontalign("PORT OF SAILING:", -2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));
        if (CommonFunctions.isNotNull(portofSailing)) {
            table.addCell(makeCellNoBorderFontalign("" + portofSailing.toUpperCase(), -2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        } else {
            table.addCell(makeCellNoBorderFontalign("", -2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        }
        table.addCell(makeCellNoBorderFontalign("Sailing Date:", -2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));
        if (CommonFunctions.isNotNull(sailDate)) {
            table.addCell(makeCellNoBorderFontalign("" + DateUtils.formatStringDateToAppFormatMMM(sailDate), -2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        } else {
            table.addCell(makeCellNoBorderFontalign("", -2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        }
        //portdischarge
        table.addCell(makeCellNoBorderFontalign("PORT OF DISCHARGE:", -2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign("" + portofDischarge, -2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign("ETA POD Date:", -2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));
        if (CommonFunctions.isNotNull(podEta)) {
            table.addCell(makeCellNoBorderFontalign("" + DateUtils.formatStringDateToAppFormatMMM(podEta), -2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        } else {
            table.addCell(makeCellNoBorderFontalign("", -2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        }
        //portdischarge
        table.addCell(makeCellNoBorderFontalign("FINAL DESTINATION:", -2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign("" + fd, -2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign("ETA Final Date:", -2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont12f));
        if (CommonFunctions.isNotNull(etaFD)) {
            table.addCell(makeCellNoBorderFontalign("" + DateUtils.formatStringDateToAppFormatMMM(etaFD), -2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        } else {
            table.addCell(makeCellNoBorderFontalign("", -2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        }
        //empty cell
        table.addCell(createEmptyCell(0, 6, 4));
        //borderLine
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthBottom(0.6f);
        table.addCell(cell);
        return table;
    }

    public PdfPTable unitTable() throws DocumentException {
        table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.3f, 0.2f, 1.2f, 3.5f});
        //Unit details
        table.addCell(makeCellNoBorderFontalign("Unit", 2f, Element.ALIGN_CENTER, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign("", 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign("Seal#", 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign("", 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        //borderLine
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-2f);
        cell.setBorderWidthBottom(0.6f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-2f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-2f);
        cell.setBorderWidthBottom(0.6f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-2f);
        table.addCell(cell);
        //units Values
        if (CommonFunctions.isNotNull(unitNumber)) {
            table.addCell(makeCellNoBorderFontalign("" + unitNumber.toString(), 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        } else {
            table.addCell(makeCellNoBorderFontalign("", 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        }
        table.addCell(makeCellNoBorderFontalign("", 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign(sealOut, 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFontalign("", 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont12f));
        //empty cell
        table.addCell(createEmptyCell(0, 6, 4));
        return table;
    }

    public PdfPTable footerTable() throws DocumentException {
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a zzz ");
        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 2f, 2f});
        //date
        table.addCell(makeCellNoBorderFontalign("Date " + DateUtils.parseDateToString(todayDate), 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont9f));
        table.addCell(makeCellNoBorderFontalign("End of Report", 2f, Element.ALIGN_CENTER, 0, blackNormalCourierFont9f));
        table.addCell(makeCellNoBorderFontalign("Time   " + sdf.format(todayDate), 2f, Element.ALIGN_RIGHT, 0, blackNormalCourierFont9f));
        return table;
    }
}
