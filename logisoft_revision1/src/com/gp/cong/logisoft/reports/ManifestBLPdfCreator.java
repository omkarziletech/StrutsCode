package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts.util.MessageResources;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.fcl.HazmatBC;
import com.gp.cong.logisoft.bc.print.PrintConfigBC;
import com.gp.cong.logisoft.domain.FclInbondDetails;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.FclInbondDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclAESDetails;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.util.Date;

import org.apache.log4j.Logger;

public class ManifestBLPdfCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(ManifestBLPdfCreator.class);
    Document document = null;
    PdfWriter pdfWriter = null;
    protected PdfTemplate total;
    private String manifestRev = "";
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    public static double chargesTotal = 0.00;
    protected BaseFont helv;
    public static MessageResources messageResources = null;

    public ManifestBLPdfCreator() {
    }

    public ManifestBLPdfCreator(FclBl bl) throws Exception {
        FclBl fclbl = new FclBlDAO().getOriginalBl(bl.getFileNo());
        if (bl.getBolId() != null && bl.getBolId().indexOf("=") != -1) {
            if (CommonFunctions.isNotNull(fclbl.getCorrectionCount()) && "Yes".equalsIgnoreCase(fclbl.getPrintRev())) {
                if (CommonFunctions.isNotNull(fclbl.getManifestRev())) {
                    int count = fclbl.getCorrectionCount() + Integer.parseInt(fclbl.getManifestRev());
                    manifestRev = "" + count;
                } else {
                    manifestRev = "" + fclbl.getCorrectionCount();
                }
            }
        } else {
            if (CommonFunctions.isNotNull(fclbl.getManifestRev()) && "Yes".equalsIgnoreCase(fclbl.getPrintRev())) {
                manifestRev = fclbl.getManifestRev();
            }
        }
    }

    public void initialize(String fileName, FclBl bl) throws FileNotFoundException,
            DocumentException,
            Exception {
        document = new Document(PageSize.LEGAL.rotate());
        document.setMargins(4, 4, 4, 4);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(
                fileName));
        pdfWriter.setPageEvent(new ManifestBLPdfCreator(bl));
        String footerList = "Page No";
        String totalPages = "";
        Phrase headingPhrase = new Phrase(footerList, headingFont);
        Phrase headingPhrase1 = new Phrase(totalPages, headingFont);
        HeaderFooter footer = new HeaderFooter(headingPhrase, headingPhrase1);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.open();
    }

    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(100, 100);
        total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        try {
            helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
                    BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public void createBody(String contextPath, FclBl bl, List commentsList,
            String simpleRequest, MessageResources messageResources, Set<FclBlCharges> fclChargesSet)
            throws MalformedURLException, IOException, DocumentException, Exception {
        PdfPCell cell;
        //heading table
        BaseFont palationRomanBase = BaseFont.createFont(contextPath + "/ttf/Palatino-Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font palatinoRomanSmallFont = new Font(palationRomanBase, 8, Font.NORMAL, Color.BLACK);
        Font palatinoRomanBigFont = new Font(palationRomanBase, 12, Font.NORMAL, Color.BLACK);
        Font palatinoRomanMediumFontBold = new Font(palationRomanBase, 12, Font.BOLD, Color.BLACK);
        Font palatinoRomanSmallFontBold = new Font(palationRomanBase, 10, Font.BOLD, Color.BLACK);
        Font palatinoRomanBigFontBold = new Font(palationRomanBase, 14, Font.BOLD, Color.BLACK);
        Font helveticaBigFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);
        Font helveticaSmallFont = new Font(Font.HELVETICA, 6.5f, Font.NORMAL, Color.BLACK);
        Font helvSmallFont = new Font(Font.HELVETICA, 7, Font.NORMAL, Color.BLACK);
        PdfPTable headingTable = makeTable(3);
        headingTable.setWidthPercentage(100);
        cell = makeCellLeftNoBorderBold("");
        cell.setColspan(3);
        headingTable.addCell(cell);
        cell = makeCellLeftNoBorderBold("");
        cell.setColspan(3);
        headingTable.addCell(cell);
        cell = makeCellLeftNoBorderBold("");
        cell.setColspan(3);
        headingTable.addCell(cell);
        if (bl.getBolId() != null && bl.getBolId().indexOf("=") != -1) {
            cell = makeCellCenterNoBorderPalatinoFclBl("CORRECTED", palatinoRomanBigFont);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            headingTable.addCell(cell);
            cell = makeCellLeftNoBorderBold("");
            cell.setColspan(3);
            headingTable.addCell(cell);
        }

        //headingTable.setWidthPercentage(100);
////        String companyName = null != LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName")
////                ? LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName").toUpperCase() : "";
        String companyName = "";
        SystemRulesDAO rulesDAO = new SystemRulesDAO();
        String address = rulesDAO.getSystemRulesByCode("CompanyAddress");
        String companyCode= new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        if (bl.getBrand().equals("Econo") && ("03").equals(companyCode)) {
           companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
        } else if (bl.getBrand().equals("OTI") && ("02").equals(companyCode)) {
           companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
        } else if (bl.getBrand().equalsIgnoreCase("Ecu Worldwide")) {
           companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
        }
        //top table
        PdfPTable topTable = makeTable(5);
        topTable.setWidthPercentage(100);
        topTable.setWidths(new float[]{11, 11, 18, 30, 30});
        topTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell = makeCellLeftPalatinoWithBorderFclBl("TRAILER NO.", palatinoRomanSmallFontBold);
        cell.setBorderWidthBottom(0);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        topTable.addCell(cell);
        cell = makeCellLeftPalatinoWithBorderFclBl("SEAL", palatinoRomanSmallFontBold);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthLeft(0);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        topTable.addCell(cell);
        cell = makeCellLeftNoBorderBold("");
        topTable.addCell(cell);
        cell = makeCellLeftNoBorderPalatinoFclBl(companyName, palatinoRomanBigFontBold);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        topTable.addCell(cell);
        cell = makeCellLeftNoBorderPalatinoFclBl("CARGO", palatinoRomanBigFontBold);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        topTable.addCell(cell);

        cell = makeCellLeftPalatinoWithBorderFclBl("SEE LIST BELOW", helveticaBigFont);
        cell.setBorderWidthBottom(0);
        topTable.addCell(cell);
        cell = makeCellLeftPalatinoWithBorderFclBl("SEE LIST BELOW", helveticaBigFont);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthLeft(0);
        topTable.addCell(cell);
        cell = makeCellLeftNoBorderBold("");
        topTable.addCell(cell);
        cell = makeCellLeftPalatinoWithBorderFclBl(address, palatinoRomanMediumFontBold);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        topTable.addCell(cell);
        cell = makeCellLeftNoBorderPalatinoFclBl("MANIFEST", palatinoRomanBigFontBold);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        topTable.addCell(cell);

        //details table
        PdfPTable detailsTable = makeTable(7);
        detailsTable.setWidthPercentage(100);
        detailsTable.setWidths(new float[]{27, 9, 16, 9, 16, 16, 7});
        cell = makeCellLeftPalatinoWithBorderFclBl("SAILING ON VESSEL/VOYAGE", palatinoRomanSmallFontBold);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        detailsTable.addCell(cell);
        cell = makeCellLeftPalatinoWithBorderFclBl("FLAG", palatinoRomanSmallFontBold);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        detailsTable.addCell(cell);
        cell = makeCellLeftPalatinoWithBorderFclBl("DATE", palatinoRomanSmallFontBold);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        detailsTable.addCell(cell);
        cell = makeCellLeftWithBorderFclBl("");
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        detailsTable.addCell(cell);
        cell = makeCellLeftPalatinoWithBorderFclBl("PORT OF LOADING", palatinoRomanSmallFontBold);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        detailsTable.addCell(cell);
        cell = makeCellLeftPalatinoWithBorderFclBl("PORT OF DISCHARGE", palatinoRomanSmallFontBold);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        detailsTable.addCell(cell);
        cell = makeCellLeftPalatinoWithBorderFclBl("PG NO.", palatinoRomanSmallFontBold);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0.6f);
        detailsTable.addCell(cell);
        StringBuilder vesselVoyage = new StringBuilder();
        vesselVoyage.append(bl.getVessel() != null ? (bl.getVessel().getCodedesc() != null ? bl.getVessel().getCodedesc() : "") : "");
        vesselVoyage.append(" ");
        vesselVoyage.append("V.");
        vesselVoyage.append(bl.getVoyages() != null ? bl.getVoyages() : "");
        cell = makeCellLeftNoBorderPalatinoFclBl(vesselVoyage.toString(), helveticaBigFont);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        detailsTable.addCell(cell);
        cell = makeCellLeftNoBorderPalatinoFclBl("", helveticaBigFont);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        detailsTable.addCell(cell);
        String etd = bl.getSailDate() != null ? sdf.format(bl.getSailDate()).toString() : "";
        cell = makeCellLeftNoBorderPalatinoFclBl(etd, helveticaBigFont);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        detailsTable.addCell(cell);
        cell = makeCellLeftNoBorderPalatinoFclBl("", helveticaBigFont);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        detailsTable.addCell(cell);

        //----checking of presence of Door Origin------
        String pol = "";
        if (null != bl.getDoorOfOrigin() && !bl.getDoorOfOrigin().equals("")) {
            pol = removeUnlocCode(bl.getDoorOfOrigin());
        } else if (null != bl.getPortOfLoading() && !bl.getPortOfLoading().equals("")) {
            pol = removeUnlocCode(bl.getPortOfLoading());
        }

        cell = makeCellLeftNoBorderPalatinoFclBl(pol, helveticaBigFont);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        detailsTable.addCell(cell);

        //----CHECKING OF PRESENCE OF DOOR DESTINATION------
        String pod = "";
        if (null != bl.getDoorOfDestination() && !bl.getDoorOfDestination().equals("")) {
            pod = removeUnlocCodeAppendCountryName(bl.getDoorOfDestination());
        } else if (null != bl.getPortofDischarge() && !bl.getPortofDischarge().equals("")) {
            pod = removeUnlocCodeAppendCountryName(bl.getPortofDischarge());
        }
        cell = makeCellLeftNoBorderPalatinoFclBl(pod, helveticaBigFont);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        detailsTable.addCell(cell);
        cell = makeCellLeftNoBorderPalatinoFclBl(String.valueOf(document.getPageNumber() + 1), helveticaBigFont);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0.6f);
        detailsTable.addCell(cell);

        //bottom table
        PdfPTable bottomTable = makeTable(10);
        bottomTable.setWidthPercentage(100);
        bottomTable.setWidths(new float[]{7f, 14f, 14f, 14f, 7, 6f, 19, 6, 6, 6});
        cell = makeCell("B/L", Element.ALIGN_CENTER, palatinoRomanSmallFontBold);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        bottomTable.addCell(cell);
        cell = makeCell("SHIPPER", Element.ALIGN_CENTER, palatinoRomanSmallFontBold);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        bottomTable.addCell(cell);
        cell = makeCell("CONSIGNEE", Element.ALIGN_CENTER, palatinoRomanSmallFontBold);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        bottomTable.addCell(cell);
        cell = makeCell("NOTIFY", Element.ALIGN_CENTER, palatinoRomanSmallFontBold);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        bottomTable.addCell(cell);
        cell = makeCell("MARKS", Element.ALIGN_CENTER, palatinoRomanSmallFontBold);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        bottomTable.addCell(cell);
        cell = makeCell("PACKAGES", Element.ALIGN_CENTER, palatinoRomanSmallFontBold);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        bottomTable.addCell(cell);
        cell = makeCell("COMMODITY", Element.ALIGN_CENTER, palatinoRomanSmallFontBold);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        bottomTable.addCell(cell);
        cell = makeCell("VOLUME", Element.ALIGN_CENTER, palatinoRomanSmallFontBold);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        bottomTable.addCell(cell);
        cell = makeCell("WT.", Element.ALIGN_CENTER, palatinoRomanSmallFontBold);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0);
        bottomTable.addCell(cell);
        cell = makeCell("O/F", Element.ALIGN_CENTER, palatinoRomanSmallFontBold);
        cell.setBorderWidthBottom(0);
        cell.setBorderWidthRight(0.6f);
        bottomTable.addCell(cell);
        //adding values
        cell = makeCellLeftPalatinoWithLeftTopBorder(getBolNo(bl.getBolId()), helveticaSmallFont);
        bottomTable.addCell(cell);

        StringBuilder shipperDetails = new StringBuilder();
        shipperDetails.append(bl.getShipperName() != null ? bl.getShipperName() : "");
        shipperDetails.append("\n");
        shipperDetails.append(bl.getShipperAddress());
        cell = makeCellLeftPalatinoWithLeftTopBorder(shipperDetails.toString(), helveticaSmallFont);
        bottomTable.addCell(cell);

        StringBuilder consigneeDetails = new StringBuilder();
        consigneeDetails.append(bl.getConsigneeName() != null ? bl.getConsigneeName() : "");
        consigneeDetails.append("\n");
        consigneeDetails.append(bl.getConsigneeAddress());
        cell = makeCellLeftPalatinoWithLeftTopBorder(consigneeDetails.toString(), helveticaSmallFont);
        bottomTable.addCell(cell);

        StringBuilder notifyPartyDetails = new StringBuilder();
        notifyPartyDetails.append(bl.getNotifyPartyName() != null ? bl.getNotifyPartyName() : "");
        notifyPartyDetails.append("\n");
        notifyPartyDetails.append(bl.getStreamshipNotifyParty());
        cell = makeCellLeftPalatinoWithLeftTopBorder(notifyPartyDetails.toString(), palatinoRomanSmallFont);
        bottomTable.addCell(cell);

        double total = 0.00;
        List rateList = new ArrayList();
        List chargeListTemp = new ArrayList();
        for (Iterator iter = fclChargesSet.iterator(); iter.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
            rateList.add(fclBlCharges);
        }
        FclBlBC fclBlBC = new FclBlBC();
        boolean importFlag = "I".equalsIgnoreCase(bl.getImportFlag());
        if ("I".equalsIgnoreCase(bl.getImportFlag())) {
            chargeListTemp = fclBlBC.consolidateRates(fclBlBC.getSortedList(rateList), messageResources,importFlag);
        } else {
            chargeListTemp = new PrintConfigBC().getRatesForFclPrint(fclBlBC.getSortedList(rateList), messageResources);
        }
        List chargeList = fclBlBC.getCorrectedList(chargeListTemp);
        for (Iterator iter = chargeList.iterator(); iter.hasNext();) {
            FclBlCharges fclBlCharges = (FclBlCharges) iter.next();
            total = total + (fclBlCharges.getAmount() != null ? fclBlCharges.getAmount() : 0.00);
        }
        NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
        NumberFormat numberFormat1 = new DecimalFormat("###,###,##0.000");
        StringBuilder totalString = new StringBuilder();
        totalString.append("$");
        totalString.append(numberFormat.format(total).toString());
        totalString.append(" ");
        if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("P-Prepaid") || bl.getHouseBl().equalsIgnoreCase("P"))) {
            totalString.append("PPD");
        } else if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("C-Collect") || bl.getHouseBl().equalsIgnoreCase("C"))) {
            totalString.append("COL");
        } else if (bl.getHouseBl() != null && (bl.getHouseBl().equalsIgnoreCase("B-Both") || bl.getHouseBl().equalsIgnoreCase("B"))) {
            totalString.append("BOTH");
        } else {
            totalString.append("");
        }
        Set<FclBlContainer> containerSet = bl.getFclcontainer();
        HashMap hashMap = new HashMap();
        List TempList = new ArrayList();
        List containerList = new ArrayList();
        for (Iterator iter = containerSet.iterator(); iter.hasNext();) {
            FclBlContainer fclBlCont = (FclBlContainer) iter.next();
            if (!"D".equalsIgnoreCase(fclBlCont.getDisabledFlag())) {
                hashMap.put(fclBlCont.getTrailerNoId(), fclBlCont);
                TempList.add(fclBlCont.getTrailerNoId());
            }
        }
        Collections.sort(TempList);
        for (int i = 0; i < TempList.size(); i++) {
            FclBlContainer fclBlCont = (FclBlContainer) hashMap.get(TempList.get(i));
            containerList.add(fclBlCont);
        }
        boolean secondTime = false;
        List<String> marksList = new ArrayList<String>();
        for (Iterator iter = containerList.iterator(); iter.hasNext();) {
            if (secondTime == true) {
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
            }
            FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
            List<String> hazmatList = new ArrayList<String>();
            if (CommonUtils.isNotEmpty(fclBlContainer.getTrailerNo())) {
                List hazmatMaterialList = fclBlBC.getHazmatForBlPrint(fclBlContainer.getTrailerNoId(), FclBlConstants.FCLBL);
                hazmatList = new HazmatBC().getHazmatDetails(hazmatMaterialList);
            }
            StringBuilder marksNumber = new StringBuilder();
            marksNumber.append(fclBlContainer.getTrailerNo() != null ? fclBlContainer.getTrailerNo() : "");
            marksNumber.append(fclBlContainer.getTrailerNo() != null ? (fclBlContainer.getTrailerNo() != null ? " " : "") : "");
            marksNumber.append(fclBlContainer.getTrailerNo() != null ? (fclBlContainer.getTrailerNo() != null ? "SEAL # " : "") : "");
            marksNumber.append(fclBlContainer.getSealNo() != null ? fclBlContainer.getSealNo() : "");
            marksList.add(marksNumber.toString());

            if (secondTime == true) {
                cell = makeCellLeftWithLeftBorderFcLBl(fclBlContainer.getMarks() != null ? fclBlContainer.getMarks() : "", helveticaSmallFont);
            } else {
                cell = makeCellLeftPalatinoWithLeftTopBorder(fclBlContainer.getMarks() != null ? fclBlContainer.getMarks() : "", helveticaSmallFont);
            }
            bottomTable.addCell(cell);

            String sizeLegend = fclBlContainer.getSizeLegend() != null ? (fclBlContainer.getSizeLegend().
                    getCodedesc() != null ? fclBlContainer.getSizeLegend().getCodedesc() : "") : "";
            StringBuilder tempSizeLegened = new StringBuilder();
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
            if (CommonUtils.isNotEmpty(fclBlContainer.getSpecialEquipment())) {
                tempSizeLegened.append("\n");
                tempSizeLegened.append(fclBlContainer.getSpecialEquipment());
            }
            if (secondTime == true) {
                if (null != bl.getNoOfPackages() && bl.getNoOfPackages().equalsIgnoreCase("Yes")) {
                    cell = makeCellLeftWithLeftBorderFcLBl(tempSizeLegened.toString(), helveticaSmallFont);
                } else {
                    cell = makeCellLeftWithLeftBorderFcLBl("", helveticaSmallFont);
                }
            } else {
                if (null != bl.getNoOfPackages() && bl.getNoOfPackages().equalsIgnoreCase("Yes")) {
                    cell = makeCellLeftPalatinoWithLeftTopBorder(tempSizeLegened.toString(), helveticaSmallFont);
                } else {
                    cell = makeCellLeftPalatinoWithLeftTopBorder("", helveticaSmallFont);
                }
            }

            bottomTable.addCell(cell);
            List<FclBlMarks> fclMarksList = new ArrayList();
            FclBlContainerDAO fclBlContainerDAO = new FclBlContainerDAO();
            fclMarksList = fclBlContainerDAO.getPakagesDetails(fclBlContainer.getTrailerNoId());
            fclBlContainer.setFclMarksList(fclMarksList);
            List arrayList = fclBlContainer.getFclMarksList();
            boolean set = false;
            if (arrayList.size() == 0) {
                if (secondTime == true) {
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                    cell = makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont);
                    cell.setBorderWidthRight(0.6f);
                    bottomTable.addCell(cell);

                } else {
                    bottomTable.addCell(makeCellLeftPalatinoWithLeftTopBorder("", palatinoRomanSmallFont));
                    bottomTable.addCell(makeCellLeftPalatinoWithLeftTopBorder("", palatinoRomanSmallFont));
                    bottomTable.addCell(makeCellLeftPalatinoWithLeftTopBorder("", palatinoRomanSmallFont));
                    if (null != bl.getRatedManifest() && bl.getRatedManifest().equalsIgnoreCase("Yes")) {
                        cell = makeCellLeftPalatinoWithLeftTopBorder(totalString.toString(), helveticaSmallFont);
                    } else {
                        cell = makeCellLeftPalatinoWithLeftTopBorder("", helveticaSmallFont);
                    }
                    cell.setBorderWidthRight(0.6f);
                    bottomTable.addCell(cell);
                }

            } else {
                for (Iterator iterator1 = arrayList.iterator(); iterator1.hasNext();) {
                    FclBlMarks fclBlmarks = (FclBlMarks) iterator1.next();
                    if (set == false) {
                        PdfPTable cftTable = makeTable(2);
                        cftTable.setWidths(new float[]{60, 40});
                        cftTable.setWidthPercentage(100);
                        double measureCFT = fclBlmarks.getMeasureCft() != null ? fclBlmarks.getMeasureCft()
                                : 0.00;
                        StringBuilder cft = new StringBuilder();
                        double measureCbm = fclBlmarks.getMeasureCbm() != null ? fclBlmarks.getMeasureCbm()
                                : 0.00;
                        if (measureCbm != 0.00) {
                            if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                cell = makeCellLeftNoBorderPalatinoFclBl(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureCbm)), helveticaSmallFont);
                            } else {
                                cell = makeCellLeftNoBorderPalatinoFclBl(numberFormat.format(measureCbm).toString(), helveticaSmallFont);
                            }
                            cell.setNoWrap(true);
                            cftTable.addCell(cell);
                            cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("CBM", helveticaSmallFont));


                        } else {
                            cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                            cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                        }
                        if (measureCFT != 0.00) {
                            if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                cell = makeCellLeftNoBorderPalatinoFclBl(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureCFT)), helveticaSmallFont);
                            } else {
                                cell = makeCellLeftNoBorderPalatinoFclBl(numberFormat.format(measureCFT).toString(), helveticaSmallFont);
                            }
                            cell.setNoWrap(true);
                            cftTable.addCell(cell);
                            cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("CFT", helveticaSmallFont));

                        } else {
                            cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                            cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                        }
                        if (secondTime == true) {
                            StringBuilder stcAndPackages = new StringBuilder();
                            if (null != bl.getTotalContainers() && bl.getTotalContainers().equalsIgnoreCase("Yes")) {
                                stcAndPackages.append("STC: ");
                                stcAndPackages.append(null != fclBlmarks.getNoOfPkgs() && fclBlmarks.getNoOfPkgs() != 0 ? fclBlmarks.getNoOfPkgs() : "");
                                stcAndPackages.append(" ");
                                stcAndPackages.append(null != fclBlmarks.getUom() ? fclBlmarks.getUom() : "");
                                stcAndPackages.append("\n");

                            }
                            stcAndPackages.append(fclBlmarks.getDescPckgs() != null ? fclBlmarks.getDescPckgs() : "");
                            if (CommonUtils.isNotEmpty(hazmatList)) {
                                for (int i = 0; i < hazmatList.size(); i++) {
                                    StringBuilder hazmatBuild = new StringBuilder();
                                    stcAndPackages.append("\n");
                                    hazmatBuild.append("\n");
                                    hazmatBuild.append(CommonUtils.splitString((String) hazmatList.get(i), 30));
                                    stcAndPackages.append(hazmatBuild.toString());
                                }
                            }
                            bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl(stcAndPackages.toString(), helveticaSmallFont));
                            PdfPCell cftCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, helveticaSmallFont, Rectangle.LEFT);
                            cftCell.addElement(cftTable);
                            bottomTable.addCell(cftCell);
                        } else {
                            StringBuilder stcAndPackages = new StringBuilder();
                            if (null != bl.getTotalContainers() && bl.getTotalContainers().equalsIgnoreCase("Yes")) {
                                stcAndPackages.append("STC: ");
                                stcAndPackages.append(null != fclBlmarks.getNoOfPkgs() && fclBlmarks.getNoOfPkgs() != 0 ? fclBlmarks.getNoOfPkgs() : "");
                                stcAndPackages.append(" ");
                                stcAndPackages.append(null != fclBlmarks.getUom() ? fclBlmarks.getUom() : "");
                                stcAndPackages.append("\n");
                            }
                            stcAndPackages.append(fclBlmarks.getDescPckgs() != null ? fclBlmarks.getDescPckgs() : "");
                            if (CommonUtils.isNotEmpty(hazmatList)) {
                                for (int i = 0; i < hazmatList.size(); i++) {
                                    StringBuilder hazmatBuild = new StringBuilder();
                                    stcAndPackages.append("\n");
                                    hazmatBuild.append("\n");
                                    hazmatBuild.append(CommonUtils.splitString((String) hazmatList.get(i), 30));
                                    stcAndPackages.append(hazmatBuild.toString());
                                }
                            }
                            bottomTable.addCell(makeCellLeftPalatinoWithLeftTopBorder(stcAndPackages.toString(), helveticaSmallFont));
                            //bottomTable.addCell(makeCellLeftPalatinoWithLeftTopBorder(cft.toString(),helveticaSmallFont));
                            PdfPCell cftCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, helveticaSmallFont, 0.6f, 0.0f, 0.6f, 0.0f, Color.white);
                            cftCell.addElement(cftTable);
                            bottomTable.addCell(cftCell);
                        }

                        double netWeightLBS = fclBlmarks.getNetweightLbs() != null ? fclBlmarks.getNetweightLbs()
                                : 0.00;
                        StringBuilder lbs = new StringBuilder();
                        PdfPTable lbsTable = makeTable(2);
                        lbsTable.setWidths(new float[]{60, 40});
                        lbsTable.setWidthPercentage(100);

                        double measureKgs = fclBlmarks.getNetweightKgs() != null ? fclBlmarks.getNetweightKgs() : 0.00;
                        if (measureKgs != 0.00) {
                            if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                cell = makeCellLeftNoBorderPalatinoFclBl(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureKgs)), helveticaSmallFont);
                            } else {
                                cell = makeCellLeftNoBorderPalatinoFclBl(numberFormat.format(measureKgs).toString(), helveticaSmallFont);
                            }
                            cell.setNoWrap(true);
                            lbsTable.addCell(cell);
                            lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("KGS", helveticaSmallFont));
                        } else {
                            lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                            lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                        }
                        if (netWeightLBS != 0.00) {
                            if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                cell = makeCellLeftNoBorderPalatinoFclBl(CommonUtils.trimTrailingZeros("" + numberFormat.format(netWeightLBS)), helveticaSmallFont);
                            } else {
                                cell = makeCellLeftNoBorderPalatinoFclBl(numberFormat.format(netWeightLBS).toString(), helveticaSmallFont);
                            }
                            cell.setNoWrap(true);
                            lbsTable.addCell(cell);
                            lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("LBS", helveticaSmallFont));
                        } else {
                            lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                            lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                        }
                        if (secondTime == true) {
                            PdfPCell lbsCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, helveticaSmallFont, Rectangle.LEFT);
                            lbsCell.addElement(lbsTable);
                            bottomTable.addCell(lbsCell);
                            cell = makeCellLeftWithLeftBorderFcLBl("", helveticaSmallFont);
                            cell.setBorderWidthRight(0.6f);
                            bottomTable.addCell(cell);
                        } else {
                            PdfPCell lbsCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, helveticaSmallFont, 0.6f, 0.0f, 0.6f, 0.0f, Color.white);
                            lbsCell.addElement(lbsTable);
                            bottomTable.addCell(lbsCell);
                            if (null != bl.getRatedManifest() && bl.getRatedManifest().equalsIgnoreCase("Yes")) {
                                cell = makeCellLeftPalatinoWithLeftTopBorder(totalString.toString(), helveticaSmallFont);
                            } else {
                                cell = makeCellLeftPalatinoWithLeftTopBorder("", helveticaSmallFont);
                            }
                            cell.setBorderWidthRight(0.6f);
                            bottomTable.addCell(cell);
                        }

                        set = true;
                    } else {
                        bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                        bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                        bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                        bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                        bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                        bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                        StringBuilder stcAndPackages = new StringBuilder();
                        if (null != bl.getTotalContainers() && bl.getTotalContainers().equalsIgnoreCase("Yes")) {
                            stcAndPackages.append("STC: ");
                            stcAndPackages.append(null != fclBlmarks.getNoOfPkgs() ? fclBlmarks.getNoOfPkgs() : "");
                            stcAndPackages.append(" ");
                            stcAndPackages.append(null != fclBlmarks.getUom() ? fclBlmarks.getUom() : "");
                            stcAndPackages.append("\n");
                        }
                        stcAndPackages.append(fclBlmarks.getDescPckgs() != null ? fclBlmarks.getDescPckgs() : "");
                        if (CommonUtils.isNotEmpty(hazmatList)) {
                            for (int i = 0; i < hazmatList.size(); i++) {
                                StringBuilder hazmatBuild = new StringBuilder();
                                stcAndPackages.append("\n");
                                hazmatBuild.append("\n");
                                hazmatBuild.append(CommonUtils.splitString((String) hazmatList.get(i), 30));
                                stcAndPackages.append(hazmatBuild.toString());
                            }
                        }
                        bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl(stcAndPackages.toString(), helveticaSmallFont));
                        PdfPTable cftTable = makeTable(2);
                        cftTable.setWidths(new float[]{60, 40});
                        cftTable.setWidthPercentage(100);
                        double measureCFT = fclBlmarks.getMeasureCft() != null ? fclBlmarks.getMeasureCft()
                                : 0.00;
                        StringBuilder cft = new StringBuilder();
                        double measureCbm = fclBlmarks.getMeasureCbm() != null ? fclBlmarks.getMeasureCbm()
                                : 0.00;
                        if (measureCbm != 0.00) {
                            if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                cell = makeCellLeftNoBorderPalatinoFclBl(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureCbm)), helveticaSmallFont);
                            } else {
                                cell = makeCellLeftNoBorderPalatinoFclBl(numberFormat.format(measureCbm).toString(), helveticaSmallFont);
                            }
                            cell.setNoWrap(true);
                            cftTable.addCell(cell);
                            cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("CBM", helveticaSmallFont));
                        } else {
                            cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                            cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                        }
                        if (measureCFT != 0.00) {
                            if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                cell = makeCellLeftNoBorderPalatinoFclBl(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureCFT)), helveticaSmallFont);
                            } else {
                                cell = makeCellLeftNoBorderPalatinoFclBl(numberFormat.format(measureCFT).toString(), helveticaSmallFont);
                            }
                            cell.setNoWrap(true);
                            cftTable.addCell(cell);
                            cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("CFT", helveticaSmallFont));
                        } else {
                            cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                            cftTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                        }
                        PdfPCell cftCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, helveticaSmallFont, Rectangle.LEFT);
                        cftCell.addElement(cftTable);
                        bottomTable.addCell(cftCell);

                        PdfPTable lbsTable = makeTable(2);
                        lbsTable.setWidths(new float[]{60, 40});
                        lbsTable.setWidthPercentage(100);
                        double netWeightLBS = fclBlmarks.getNetweightLbs() != null ? fclBlmarks.getNetweightLbs()
                                : 0.00;
                        StringBuilder lbs = new StringBuilder();
                        double measureKgs = fclBlmarks.getNetweightKgs() != null ? fclBlmarks.getNetweightKgs() : 0.00;
                        if (measureKgs != 0.00) {
                            if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                cell = makeCellLeftNoBorderPalatinoFclBl(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureKgs)), helveticaSmallFont);
                            } else {
                                cell = makeCellLeftNoBorderPalatinoFclBl(numberFormat.format(measureKgs).toString(), helveticaSmallFont);
                            }
                            cell.setNoWrap(true);
                            lbsTable.addCell(cell);
                            lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("KGS", helveticaSmallFont));

                        } else {
                            lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                            lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                        }
                        if (netWeightLBS != 0.00) {
                            if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                cell = makeCellLeftNoBorderPalatinoFclBl(CommonUtils.trimTrailingZeros("" + numberFormat.format(netWeightLBS)), helveticaSmallFont);
                            } else {
                                cell = makeCellLeftNoBorderPalatinoFclBl(numberFormat.format(netWeightLBS).toString(), helveticaSmallFont);
                            }
                            cell.setNoWrap(true);
                            lbsTable.addCell(cell);
                            lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("LBS", helveticaSmallFont));
                        } else {
                            lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                            lbsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", helveticaSmallFont));
                        }
                        PdfPCell lbsCell = makeCell("", Element.ALIGN_LEFT, Element.ALIGN_TOP, helveticaSmallFont, Rectangle.LEFT);
                        lbsCell.addElement(lbsTable);
                        bottomTable.addCell(lbsCell);
                        cell = makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont);
                        cell.setBorderWidthRight(0.6f);
                        bottomTable.addCell(cell);
                    }
                }
            }
            secondTime = true;
        }
        List aesList = new FclInbondDetailsDAO().findAesdetails("fileNo", bl.getFileNo());
        if (!aesList.isEmpty()) {
            //StringBuilder aes = new StringBuilder();
            int count = 0;
            boolean printAes = false;
            StringBuilder aes = new StringBuilder("");
            for (Iterator iterator = aesList.iterator(); iterator.hasNext();) {
                count++;
                FclAESDetails aesDet = (FclAESDetails) iterator.next();
                if (CommonUtils.isNotEmpty(aesDet.getAesDetails())) {
                    if (count == 1) {
                        aes.append("AES ITN: " + aesDet.getAesDetails());
                        printAes = false;
                    } else {
                        aes.append(",AES ITN: " + aesDet.getAesDetails());
                        count = 0;
                        printAes = true;
                    }
                }
                if (printAes) {
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl(aes.toString(), helveticaSmallFont));
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                    cell = makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont);
                    cell.setBorderWidthRight(0.6f);
                    bottomTable.addCell(cell);
                    aes = new StringBuilder("");
                }
            }
            if (count == 1) {
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl(aes.toString(), helveticaSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                cell = makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont);
                cell.setBorderWidthRight(0.6f);
                bottomTable.addCell(cell);
            }

        }
        if (null != bl.getFclInbondDetails()) {
            String heading = "INBOND: ";
            for (Iterator iterator = bl.getFclInbondDetails().iterator(); iterator.hasNext();) {
                FclInbondDetails inbondDet = (FclInbondDetails) iterator.next();
                StringBuilder inbondDetailsBuilder = new StringBuilder();
                inbondDetailsBuilder.append((CommonFunctions.isNotNull(inbondDet.getInbondType())) ? (heading + inbondDet.getInbondType() + " " + inbondDet.getInbondNumber()) : (heading + inbondDet.getInbondNumber()));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl(inbondDetailsBuilder.toString(), helveticaSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                cell = makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont);
                cell.setBorderWidthRight(0.6f);
                bottomTable.addCell(cell);
            }
        }
        //Adding Trailers here
        if (null != bl.getPrintContainersOnBL() && bl.getPrintContainersOnBL().equalsIgnoreCase("Yes")) {
            bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
            cell = makeCellleftwithWordsUnderLinedSmallFont("TRAILERS");
            cell.setBorderWidthLeft(0.6f);
            bottomTable.addCell(cell);
            bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
            bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
            bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
            bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
            bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
            bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
            bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
            cell = makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont);
            cell.setBorderWidthRight(0.6f);
            bottomTable.addCell(cell);
            for (Iterator iter = marksList.iterator(); iter.hasNext();) {
                String element = (String) iter.next();
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                if (null != bl.getPrintContainersOnBL() && bl.getPrintContainersOnBL().equalsIgnoreCase("Yes")) {
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl(element, helveticaSmallFont));
                } else {
                    bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                }
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                bottomTable.addCell(makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont));
                cell = makeCellLeftWithLeftBorderFcLBl("", palatinoRomanSmallFont);
                cell.setBorderWidthRight(0.6f);
                bottomTable.addCell(cell);
            }
            cell = makeCellLeftPalatinoWithLeftTopBorder("", palatinoRomanSmallFont);
            cell.setColspan(10);
            cell.setBorderWidthLeft(0.0f);
            bottomTable.addCell(cell);
        }

        document.add(headingTable);
        document.add(topTable);
        document.add(detailsTable);
        document.add(bottomTable);
    }

    public PdfPTable getAddressTable(FclBl bl, Font palatinoRomanBigFont) throws Exception {
        PdfPCell cell;
        PdfPTable headingTable = makeTable(1);
        headingTable.setWidthPercentage(100);
        String companyName = null != LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName")
                ? LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName").toUpperCase() : "";
        SystemRulesDAO rulesDAO = new SystemRulesDAO();
        String address = rulesDAO.getSystemRulesByCode("CompanyAddress");
        cell = makeCellleftNoBorderForHeadingFclBL(companyName);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headingTable.addCell(cell);
        cell = makeCellRightNoBorder(address);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headingTable.addCell(cell);

        if (bl.getBolId() != null && bl.getBolId().indexOf("=") != -1) {
            cell = makeCellCenterNoBorderPalatinoFclBl("CORRECTED", palatinoRomanBigFont);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headingTable.addCell(cell);
        }
        return headingTable;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        try {
            //this for print page number at the bottom in the format x of y
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            String text = "Page " + writer.getPageNumber() + " of ";
            float textBase = document.bottom() - (document.bottomMargin() - 20);
            //float textBase = document.bottom() - 20;Odml
            float textSize = helv.getWidthPoint(text, 12);
            cb.beginText();
            cb.setFontAndSize(helv, 7);
            cb.setTextMatrix(document.left() + 480, textBase);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(total, document.left() + 460 + textSize, textBase);
            cb.restoreState();

        } catch (Exception e) {
            log.info("onEndPage failed on " + new Date(),e);
            throw new ExceptionConverter(e);
        }
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        total.beginText();
        total.setFontAndSize(helv, 7);
        total.setTextMatrix(0, 0);
        if (CommonFunctions.isNotNull(manifestRev)) {
            total.showText(String.valueOf(writer.getPageNumber() - 1) + "        (REV: " + manifestRev + ")");
        } else {
            total.showText(String.valueOf(writer.getPageNumber() - 1));
        }
        total.endText();
    }

    public void destroy() {
        document.close();
    }

    public String getAddress(String accoutNo) throws Exception {
        StringBuilder address = new StringBuilder();
        if (null != accoutNo && !"".equalsIgnoreCase(accoutNo)) {
            CustAddressDAO customerDAO = new CustAddressDAO();
            CustAddress custAddress = (CustAddress) customerDAO.findByAccountNo(null, accoutNo, null, null).get(0);
            address.append((null != custAddress.getCoName() && !custAddress.getCoName().equals("")) ? custAddress.getCoName() : "");
            address.append("\n");
            address.append((null != custAddress.getAddress1() && !custAddress.getAddress1().equals("")) ? custAddress.getAddress1() : "");
            address.append("\n");
            address.append((null != custAddress.getCity1() && !custAddress.getCity1().equals("")) ? custAddress.getCity1() + "," : "");
            address.append((null != custAddress.getState() && !custAddress.getState().equals("")) ? custAddress.getState() + "," : "");
            address.append((null != custAddress.getZip() && !custAddress.getZip().equals("")) ? custAddress.getZip() : "");
            address.append(".");
        }
        return address.toString();
    }

    public String getBolNo(String bolId) throws Exception {
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        String BolNo = "";
        if (null != bolId && !bolId.equalsIgnoreCase("")) {
            if (-1 != bolId.indexOf("=")) {
                BolNo = bolId.substring(0, bolId.indexOf("="));
            } else {
                BolNo = bolId;
            }
        }
        if ("02".equals(companyCode)) {
            BolNo = (String) messageResources.getMessage("defaultAgent") + "-" + BolNo;
        } else {
            BolNo = (String) messageResources.getMessage("defaultAgentforECCI") + "-" + BolNo;
        }
        return BolNo;
    }

    public String removeUnlocCode(String port) {
        String portName = "";
        if (null != port) {
            if (port.lastIndexOf("(") != -1) {
                portName = port.substring(0, port.lastIndexOf("("));
            } else {
                portName = port;
            }
            int length = portName.length();
            if (CommonUtils.isNotEmpty(portName) && portName.charAt(length - 1) == '/') {
                portName = portName.substring(0, length - 1);
            }
        }
        return portName;
    }

    public String removeUnlocCodeAppendCountryName(String port) throws Exception {
        String portName = "";
        if (null != port) {
            if (port.lastIndexOf("(") != -1 && port.lastIndexOf(")") != -1) {
                String unLocCode = "";
                unLocCode = port.substring(port.lastIndexOf("(") + 1, port.lastIndexOf(")"));
                UnLocationDAO locationDAO = new UnLocationDAO();
                UnLocation location = locationDAO.getUnlocation(unLocCode);
                if (-1 != port.indexOf("/")) {
                    portName = port.substring(0, port.indexOf("/")) + "/" + ((null != location.getCountryId()) ? location.getCountryId().getCodedesc() : "");
                } else {
                    portName = port + location.getUnLocationName();
                }
            }
        }
        return portName;
    }

    public String createBillOfLaddingReport(FclBl bl, String fileName,
            String contextPath, MessageResources messageResources,
            List commentsList, User user, String simpleRequest) throws Exception{
        try {
            FclBl fclbl = new FclBlDAO().getOriginalBl(bl.getFileNo());
            Set<FclBlCharges> fclChargesSet = bl.getFclcharge();
            ManifestBLPdfCreator.messageResources = messageResources;
            this.initialize(fileName, bl);
            this.createBody(contextPath, fclbl, commentsList, simpleRequest,
                    messageResources, fclChargesSet);
            this.destroy();
        } catch (Exception e) {
            log.info("createBillOfLaddingReport failed on " + new Date(),e);
            throw new ExceptionConverter(e);
        }
        return "fileName";
    }
}
