/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.domain.FclDoorDelivery;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclDoorDeliveryDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author User
 */
public class DeliveryOrderPdfCreator {

    private static final Logger log = Logger.getLogger(DeliveryOrderPdfCreator.class);
    Font blackFontForAR = FontFactory.getFont("Arial", 8.5f, Font.BOLD);
    Font blackFont = FontFactory.getFont("Arial", 8f, Font.NORMAL);
    Font blackBoldFontheading = FontFactory.getFont("Arial", 15f, Font.BOLD);
    Document document = null;
    PdfWriter pdfWriter = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private int DESC_SIZE = 0;

    public void initialize(String fileName, FclBl bl) throws FileNotFoundException, DocumentException, Exception {
        document = new Document(PageSize.A4);
        document.setMargins(4, 4, 4, 4);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
    }

    public void createBody(FclBl bl, String fileName, String contextPath, MessageResources messageResources) throws MalformedURLException, IOException, DocumentException, Exception {
        String deliveryDate = "";
        String freeDate = "";
        String localDeliveryOrTransferBy = "";
        String deliveryTo = "";
        String notify = "";
        String billing = "";
        String po = "";
        String referenceNumbers = "";
        String deliveryContact = "";
        String deliveryEmail ="";
        String deliveryPhone = "";
        String deliveryFax = "";

        FclDoorDeliveryDAO fclDoorDeliveryDAO = new FclDoorDeliveryDAO();
        FclDoorDelivery fclDoorDelivery = fclDoorDeliveryDAO.getFclDoorDeliveryByBol(bl.getBol());
        if (fclDoorDelivery != null) {
            if (fclDoorDelivery.getDeliveryDate() != null) {
                deliveryDate = "" + DateUtils.formatStringDateToAppFormat(fclDoorDelivery.getDeliveryDate());
            }
            if (fclDoorDelivery.getFreeDate() != null) {
                freeDate = "" + DateUtils.formatStringDateToAppFormatMMM(fclDoorDelivery.getFreeDate());
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getLocalDeliveryOrTransferBy())) {
                localDeliveryOrTransferBy = fclDoorDelivery.getLocalDeliveryOrTransferBy().toUpperCase();
            }
            if (fclDoorDelivery.getDeliveryTo() != null && fclDoorDelivery.getDeliveryTo() != "") {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(fclDoorDelivery.getDeliveryTo()).append("\n");
                stringBuilder.append(CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryToAcctNo()) ? fclDoorDelivery.getDeliveryToAcctNo(): "").append("\n");
                stringBuilder.append(CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryAddress()) ? fclDoorDelivery.getDeliveryAddress(): "").append("\n");
                stringBuilder.append(CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryCity()) ? fclDoorDelivery.getDeliveryCity(): "").append("  ");
                stringBuilder.append(CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryState()) ? fclDoorDelivery.getDeliveryState(): "").append("  ");
                stringBuilder.append(CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryZip()) ? fclDoorDelivery.getDeliveryZip(): "");
                deliveryTo = stringBuilder.toString().toUpperCase();
            }
             if(CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryContactName())){ 
                 deliveryContact = fclDoorDelivery.getDeliveryContactName().toUpperCase();
             }
             if(CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryEmail())){ 
                 deliveryEmail = fclDoorDelivery.getDeliveryEmail();
             }
             if(CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryPhone())){ 
                 deliveryPhone = fclDoorDelivery.getDeliveryPhone();
             }
             if(CommonUtils.isNotEmpty(fclDoorDelivery.getDeliveryFax())){ 
                 deliveryFax = fclDoorDelivery.getDeliveryFax();
             }
            if (bl.getNotifyPartyName() != null && bl.getNotifyPartyName() != "") {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(bl.getNotifyPartyName());
                stringBuilder.append("\n");
                stringBuilder.append(CommonUtils.isNotEmpty(bl.getNotifyParty()) ? bl.getNotifyParty(): "").append("\n");
                stringBuilder.append(CommonUtils.isNotEmpty(bl.getStreamshipNotifyParty()) ? bl.getStreamshipNotifyParty(): "");
                notify = stringBuilder.toString();
            }
            if (fclDoorDelivery.getBilling() != null && fclDoorDelivery.getBilling().equalsIgnoreCase("P")) {
                billing = "PRE-PAID";
            } else if (fclDoorDelivery.getBilling() != null && fclDoorDelivery.getBilling().equalsIgnoreCase("C")) {
                billing = "COLLECT";
            } else if (fclDoorDelivery.getBilling() != null && fclDoorDelivery.getBilling().equalsIgnoreCase("T")) {
                billing = "3rd PARTY";
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getPo())) {
                po = fclDoorDelivery.getPo();
            }
            if (CommonUtils.isNotEmpty(fclDoorDelivery.getReferenceNumbers())) {
                referenceNumbers = fclDoorDelivery.getReferenceNumbers();
            }
        }
        document.add(lineTable(bl, deliveryDate, contextPath));
        document.add(emptyTable());
        document.add(tablebodyOne(bl, freeDate, localDeliveryOrTransferBy, po, referenceNumbers, contextPath));
        document.add(emptyTable());
        document.add(tablebodyTwo(bl, deliveryTo, deliveryContact, deliveryEmail, deliveryPhone,deliveryFax, notify, contextPath));
        document.add(emptyTable());
        document.add(tablebodyThree(bl, fclDoorDelivery, messageResources, contextPath));
        document.add(emptyTable());
        document.add(tablebodyFour(bl, billing, contextPath));
    }

    public PdfPTable lineTable(FclBl bl, String deliveryDate, String realPath) throws Exception {

        PdfPCell cell = new PdfPCell();
        PdfPCell celL = new PdfPCell();
        String path = LoadLogisoftProperties.getProperty("application.image.logo");
        String econoPath = LoadLogisoftProperties.getProperty("application.image.econo.logo");
        String companyCode= new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        Paragraph p = null;
        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setBorder(0);
        if (null != bl && bl.getBrand().equalsIgnoreCase("Econo") && ("03").equals(companyCode)) {
            Image img = Image.getInstance(realPath + econoPath);
            img.setAlignment(Element.ALIGN_CENTER);
            img.scalePercent(60);
            img.setAlignment(Element.ALIGN_TOP);
            cell.addElement(img);
        } else if (null != bl && bl.getBrand().equalsIgnoreCase("OTI") && ("02").equals(companyCode)) {
            Image img = Image.getInstance(realPath + econoPath);
            img.setAlignment(Element.ALIGN_CENTER);
            img.scalePercent(60);
            img.setAlignment(Element.ALIGN_TOP);
            cell.addElement(img);
        } else if (null != bl && bl.getBrand().equalsIgnoreCase("Ecu Worldwide")) {
            Image img = Image.getInstance(realPath + path);
            img.setAlignment(Element.ALIGN_CENTER);
            img.scalePercent(60);
            img.setAlignment(Element.ALIGN_TOP);
            cell.addElement(img);
        }
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setBorder(0);
        p = new Paragraph("DELIVERY ORDER", blackBoldFontheading);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.setBorder(0);
        p = new Paragraph("DATE PRINTED : " + DateUtils.formatStringDateToAppFormatMMM(new Date()), blackFontForAR);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(8f, "CONSIGNEE NAME", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(8f, "DATE:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(8f, "REF:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CommonUtils.isNotEmpty(bl.getConsigneeName()) ? bl.getConsigneeName() : "");
        stringBuilder.append("\n");
        stringBuilder.append(CommonUtils.isNotEmpty(bl.getConsigneeAddress()) ? bl.getConsigneeAddress() : "");

        cell = new PdfPCell();
        cell.setRowspan(2);
        cell.setBorderWidthTop(0f);
        p = new Paragraph(8f, stringBuilder.toString(), blackFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        p = new Paragraph(8f, deliveryDate, blackFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        p = new Paragraph(8f, "04-" + bl.getFileNo(), blackFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        p = new Paragraph(8f, "THE MERCHANDISE DESCRIBED BELOW WILL BE ENTERED AND FORWARDED AS FOLLOWS:", blackFontForAR);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    public PdfPTable tablebodyOne(FclBl bl, String freeDate, String localDeliveryOrTransferBy, String po, String referenceNumbers, String realPath) throws Exception {

        PdfPTable table = new PdfPTable(4);
        table.setWidths(new float[]{3f, 2f, 2f, 4f});
        table.setWidthPercentage(100);

        PdfPCell cell = null;
        Paragraph p = null;

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(8f, "IMPORTING CAREER:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        cell.setColspan(2);
        p = new Paragraph(8f, "LOCATION:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(9f, "FROM PORT OF ORIGIN AIRPORT:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        p = new Paragraph(8f, CommonUtils.isNotEmpty(bl.getSslineName()) ? bl.getSslineName() : "", blackFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        cell.setColspan(2);
        p = new Paragraph(8f, CommonUtils.isNotEmpty(bl.getOnwardInlandRouting()) ? bl.getOnwardInlandRouting() : "", blackFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        p = new Paragraph(8f, CommonUtils.isNotEmpty(bl.getTerminal()) ? bl.getTerminal() : "", blackFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(8f, "BILL OR AWB NO: ", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(8f, "ARRIVAL DATE:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(9f, "FREE TIME EXP:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(9f, "LOCAL DELIVERY OR TRANSFER BY:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        p = new Paragraph(8f, CommonUtils.isNotEmpty(bl.getNewMasterBL()) ? bl.getNewMasterBL() : "", blackFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        if (bl.getEtaFd() != null) {
            p = new Paragraph(8f, "" + DateUtils.formatStringDateToAppFormatMMM(bl.getEtaFd()), blackFont);
        } else {
            p = new Paragraph(8f, "", blackFont);
        }
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        p = new Paragraph(8f, freeDate, blackFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        p = new Paragraph(8f, localDeliveryOrTransferBy, blackFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(8f, "PO#:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        cell.setColspan(2);
        p = new Paragraph(8f, "HAWB NO:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(8f, "REFERENCE NUMBERS:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        p = new Paragraph(8f, po , blackFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        cell.setColspan(2);
        p = new Paragraph(8f, CommonUtils.isNotEmpty(bl.getImportAMSHouseBl()) ? bl.getImportAMSHouseBl() : "", blackFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        p = new Paragraph(8f, referenceNumbers, blackFont);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    public PdfPTable tablebodyTwo(FclBl bl, String deliveryTo, String deliveryContact, String deliveryEmail, String deliveryPhone, String deliveryFax, String notify, String realPath) throws Exception {
        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[]{2f, 2f, 2f, 2f, 2f});
        table.setWidthPercentage(100);
        PdfPCell cell = null;
        Paragraph p = null;

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(8f, "FOR DELIVERY TO:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(8f, "NOTIFY:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorderWidthTop(0f);
        p = new Paragraph(8f, deliveryTo, blackFont);
        cell.addElement(p);
        p = new Paragraph(8f, "Contact Name: "+deliveryContact, blackFont);
        cell.addElement(p);
        p = new Paragraph(8f, "Phone: "+deliveryPhone, blackFont);
        cell.addElement(p);
        p = new Paragraph(8f, "Fax: "+deliveryFax, blackFont);
        cell.addElement(p);
        p = new Paragraph(8f, "Email: "+deliveryEmail, blackFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorderWidthTop(0f);
        cell.setBorderWidthTop(0f);
        p = new Paragraph(8f, notify, blackFont);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    public PdfPTable tablebodyThree(FclBl bl, FclDoorDelivery fclDoorDelivery, MessageResources messageResources, String realPath) throws Exception {
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new float[]{2f, 1f, 5f, 2f});
        table.setWidthPercentage(100);

        PdfPCell cell = null;
        Paragraph p = null;

        cell = new PdfPCell();
        p = new Paragraph(8f, "MARKS AND NUMBERS", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        p = new Paragraph(8f, "PIECES", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        p = new Paragraph(8f, "DESCRIPTION OF PACKAGES AND GOODS", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        p = new Paragraph(8f, "GROSS WEIGHT LBS", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        FclBlContainerDAO fclBlContainerDAO = new FclBlContainerDAO();
        Set<FclBlContainer> containerSet = bl.getFclcontainer();
        for (Iterator iter = containerSet.iterator(); iter.hasNext();) {
            FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
            StringBuilder tempSizeLegened = new StringBuilder();
            String sizeLegend = fclBlContainer.getSizeLegend() != null ? (fclBlContainer.getSizeLegend().
                    getCodedesc() != null ? fclBlContainer.getSizeLegend().getCodedesc() : "") : "";
            int index = sizeLegend.indexOf("=");
            if (index != -1) {
                tempSizeLegened.append("1X");
                String tempSize = sizeLegend.substring(index + 1, sizeLegend.length());
                if (tempSize.equalsIgnoreCase(messageResources.getMessage("container40HC"))) {
                    tempSize = "40" + "'" + "HC";
                } else if (tempSize.equalsIgnoreCase(messageResources.getMessage("container40NOR"))) {
                    tempSize = "40" + "'" + "NOR";
                } else {
                    tempSize = tempSize + "'";
                }
                tempSizeLegened.append(tempSize);
            } else {
                tempSizeLegened.append("");
            }

            StringBuilder marksNumber = new StringBuilder();
            if (null != bl.getPrintContainersOnBL() && bl.getPrintContainersOnBL().equalsIgnoreCase("Yes")) {
                marksNumber.append(tempSizeLegened);
                marksNumber.append("\n");
                marksNumber.append(fclBlContainer.getTrailerNo() != null ? fclBlContainer.getTrailerNo() : "");
                marksNumber.append("\n");
                marksNumber.append("SEAL: ");
                marksNumber.append(" " + fclBlContainer.getSealNo() != null ? fclBlContainer.getSealNo() : "");
            }
//            HelperClass helperClass = new HelperClass();
//
//            List marksList = helperClass.splitDescrption(helperClass.wrapAddress(fclBlContainer.getMarks()), DESC_SIZE + 1);
//            if (!marksList.isEmpty()) {
//                marksNumber.append("\n");
//                marksNumber.append(marksList.get(0).toString());
//                marksList.remove(0);
//            }

            List<FclBlMarks> fclMarksList = fclBlContainerDAO.getPakagesDetails(fclBlContainer.getTrailerNoId());
            if (fclMarksList != null && !fclMarksList.isEmpty()) {

                cell = new PdfPCell();
                cell.setBorderWidthBottom(0f);
                cell.setBorderWidthTop(0f);
                p = new Paragraph(8f, marksNumber.toString(), blackFont);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorderWidthBottom(0f);
                cell.setBorderWidthTop(0f);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorderWidthBottom(0f);
                cell.setBorderWidthTop(0f);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorderWidthBottom(0f);
                cell.setBorderWidthTop(0f);
                table.addCell(cell);

                for (FclBlMarks fclBlmarks : fclMarksList) {

                    cell = new PdfPCell();
                    cell.setBorderWidthBottom(0f);
                    cell.setBorderWidthTop(0f);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorderWidthBottom(0f);
                    cell.setBorderWidthTop(0f);
                    p = new Paragraph(8f, "" + fclBlmarks.getNoOfPkgs(), blackFont);
                    cell.addElement(p);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorderWidthBottom(0f);
                    cell.setBorderWidthTop(0f);
                    p = new Paragraph(8f, fclBlmarks.getDescPckgs(), blackFont);
                    cell.addElement(p);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorderWidthBottom(0f);
                    cell.setBorderWidthTop(0f);
                    p = new Paragraph(8f, "" + fclBlmarks.getNetweightLbs(), blackFont);
                    cell.addElement(p);
                    table.addCell(cell);
                }
            }
        }

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        table.addCell(cell);

        return table;
    }

    public PdfPTable tablebodyFour(FclBl bl, String billing, String realPath) throws Exception {
        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[]{3f, 3f, 1f, 2f, 1f});
        table.setWidthPercentage(100);

        PdfPCell cell = null;
        Paragraph p = null;

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthBottom(0f);
        p = new Paragraph(8f, "PRE-PAID/COLLECT/3rd PARTY", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        p = new Paragraph(8f, "Received in Good Order By:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setFixedHeight(25f);
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0f);
        p = new Paragraph(8f, billing, blackFont);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        p = new Paragraph(8f, "", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorderWidthTop(0f);
        cell.setBorderWidthLeft(0f);
        cell.setBorderWidthRight(0f);
        p = new Paragraph(8f, "Signature:", blackFontForAR);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    public PdfPTable emptyTable() throws Exception {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();

        cell = new PdfPCell();
        cell.setFixedHeight(20f);
        cell.setColspan(4);
        cell.setBorder(0);
        table.addCell(cell);
        return table;
    }

    public void destroy() {
        document.close();
    }

    public String createDeliveryOrderReport(FclBl bl, String fileName, String contextPath, MessageResources messageResources) throws Exception {
        try {
            this.initialize(fileName, bl);
            this.createBody(bl, fileName, contextPath, messageResources);
            this.destroy();
        } catch (Exception e) {
            log.info("createDeliveryOrderReport failed on" + new Date(), e);
            throw new ExceptionConverter(e);
        }
        return "fileName";
    }
}
