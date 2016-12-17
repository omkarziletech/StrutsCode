package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import com.gp.cong.logisoft.bc.print.PrintReportsConstants;
import com.gp.cong.logisoft.domain.FclInbondDetails;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
import com.gp.cong.logisoft.hibernate.dao.FclInbondDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclAESDetails;
import com.gp.cvst.logisoft.domain.FclBl;
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
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;
import java.util.Date;

import org.apache.log4j.Logger;

public class SteamShipBLPdfCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(SteamShipBLPdfCreator.class);
    Document document = null;
    FclBlBC fclBlBC = new FclBlBC();
    private String manifestRev = "";
    PdfWriter pdfWriter = null;
    protected PdfTemplate total;
    protected BaseFont helv;
    private static String MB100 = "";
    private static String MB101 = "";

    public SteamShipBLPdfCreator() {
    }

    public SteamShipBLPdfCreator(FclBl bl) throws Exception {
        FclBl fclBl = new FclBlDAO().getOriginalBl(bl.getFileNo());
        if (bl.getBolId() != null && bl.getBolId().indexOf("=") != -1) {
            if (CommonFunctions.isNotNull(fclBl.getCorrectionCount()) && "Yes".equalsIgnoreCase(fclBl.getPrintRev())) {
                if (CommonFunctions.isNotNull(fclBl.getManifestRev())) {
                    int count = fclBl.getCorrectionCount() + Integer.parseInt(fclBl.getManifestRev());
                    manifestRev = "" + count;
                } else {
                    manifestRev = "" + fclBl.getCorrectionCount();
                }
            }
        } else {
            if (CommonFunctions.isNotNull(fclBl.getManifestRev()) && "Yes".equalsIgnoreCase(fclBl.getPrintRev())) {
                manifestRev = fclBl.getManifestRev();
            }
        }
    }

    public void initialize(String fileName, FclBl bl) throws FileNotFoundException,
            DocumentException,
            Exception {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 30, 30);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(
                fileName));
        String footerList = "Page No";
        String totalPages = "";
        Phrase headingPhrase = new Phrase(footerList, headingFont);
        Phrase headingPhrase1 = new Phrase(totalPages, headingFont);
        HeaderFooter footer = new HeaderFooter(headingPhrase, headingPhrase1);
        footer.setAlignment(Element.ALIGN_CENTER);
        pdfWriter.setPageEvent(new SteamShipBLPdfCreator(bl));
        document.open();
    }

    @Override
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

    public void createbody(FclBl bl, String realPath,
            MessageResources messageResources, User user, List commentList)
            throws MalformedURIException, IOException, DocumentException, Exception {

        Font courierFontMedium = new Font(Font.COURIER, 9, Font.NORMAL, Color.BLACK);
        Font courierFontLargeBold = new Font(Font.COURIER, 11, Font.BOLD, Color.BLACK);
        String company = LoadLogisoftProperties.getProperty("application.fclBl.print.companyName");
        Font helvSmallFont = new Font(helv, 7, Font.NORMAL, Color.BLACK);
        // Details Table
        PdfPCell cell;
        PdfPTable detailsTable = makeTable(2);
        detailsTable.setWidthPercentage(100);

//		if(bl.getBolId()!=null && bl.getBolId().indexOf("=")!=-1){
//		        cell = makeCellForMaster("CORRECTED",Element.ALIGN_CENTER,courierFontLargeBold);
//		        cell.setColspan(2);
//		        detailsTable.addCell(cell);
//	    }
        PdfPTable leftTable = makeTable(3);
        leftTable.setWidths(new float[]{16, 44, 30});
        cell = makeCellForMaster("CARRIER:", Element.ALIGN_LEFT, courierFontMedium);
        cell.setColspan(1);
        leftTable.addCell(cell);
        cell = makeCellForMaster((bl.getSslineName() != null ? bl.getSslineName() : ""), Element.ALIGN_LEFT, courierFontMedium);
        cell.setColspan(2);
        leftTable.addCell(cell);
        cell = makeCellleftNoBorder("");
        cell.setColspan(3);
        leftTable.addCell(cell);

        cell = makeCellForMaster(bl.getHouseShipperName() != null ? bl.getHouseShipperName() : "", Element.ALIGN_LEFT, courierFontMedium);
        cell.setColspan(2);
        leftTable.addCell(cell);
        cell = makeCellLeftNoBorder("");
        cell.setColspan(1);
        leftTable.addCell(cell);

        cell = makeCellForMaster(bl.getHouseShipperAddress(), Element.ALIGN_LEFT, courierFontMedium);
        cell.setColspan(2);
        leftTable.addCell(cell);
        cell = makeCellLeftNoBorder("");
        cell.setColspan(1);
        leftTable.addCell(cell);

        cell = makeCellleftNoBorder("");
        cell.setColspan(3);
        leftTable.addCell(cell);
        leftTable.addCell(cell);
        leftTable.addCell(cell);

        cell = makeCellForMaster(bl.getHouseConsigneeName() != null ? bl.getHouseConsigneeName() : "", Element.ALIGN_LEFT, courierFontMedium);
        cell.setColspan(2);
        leftTable.addCell(cell);
        cell = makeCellLeftNoBorder("");
        cell.setColspan(1);
        leftTable.addCell(cell);

        cell = makeCellForMaster(bl.getHouseConsigneeAddress(), Element.ALIGN_LEFT, courierFontMedium);
        cell.setColspan(2);
        leftTable.addCell(cell);
        cell = makeCellLeftNoBorder("");
        cell.setColspan(1);
        leftTable.addCell(cell);

        cell = makeCellleftNoBorder("");
        cell.setColspan(3);
        leftTable.addCell(cell);
        leftTable.addCell(cell);
        leftTable.addCell(cell);

        if (bl.getHouseNotifyPartyName() != null && !bl.getHouseNotifyPartyName().equalsIgnoreCase("")) {
            StringBuilder notify = new StringBuilder();
            notify.append(bl.getHouseNotifyPartyName() != null ? bl.getHouseNotifyPartyName() : "");
            notify.append("\n");
            notify.append(bl.getHouseNotifyParty());
            cell = makeCellForMaster(notify.toString(), Element.ALIGN_LEFT, courierFontMedium);
            cell.setColspan(2);
            leftTable.addCell(cell);
            cell = makeCellLeftNoBorder("");
            cell.setColspan(1);
            leftTable.addCell(cell);
            cell = makeCellleftNoBorder("");
            cell.setColspan(3);
            leftTable.addCell(cell);
            leftTable.addCell(cell);
            leftTable.addCell(cell);
        }
//                else if(bl.getHouseConsignee()!=null && !bl.getHouseConsignee().equalsIgnoreCase("")){
//			StringBuilder notify = new StringBuilder();
//			notify.append("SAME AS CONSIGNEE");
//			CustAddressDAO addressDAO=new CustAddressDAO();
//			List custAddList =addressDAO.findByAccountNo(null, bl.getHouseConsignee(), null, null);
//			if(custAddList!=null && custAddList.size()>0){
//				CustAddress custAddress =(CustAddress) custAddList.get(0);
//				notify.append("\n");notify.append("");notify.append("\n");
//				notify.append("TEL.");
//				notify.append(custAddress.getPhone()!=null?custAddress.getPhone():"");
//				notify.append("\n");
//				notify.append("FAX.");
//				notify.append(custAddress.getFax()!=null?custAddress.getFax():"");
//                                cell=makeCellForMaster(notify.toString(),Element.ALIGN_LEFT,courierFontMedium);
//                                cell.setColspan(2);
//                                leftTable.addCell(cell);
//                                cell=makeCellLeftNoBorder("");
//                                cell.setColspan(1);
//				leftTable.addCell(cell);
//			}else{
//				cell = makeCellleftNoBorder("");
//                                cell.setColspan(3);
//                                leftTable.addCell(cell);
//			}
//		}
        cell = makeCellleftNoBorder("");
        cell.setColspan(3);
        leftTable.addCell(cell);
        PdfPCell subcell = makeCellleftNoBorder("");
        PdfPTable subTable = makeTable(2);
        subTable.setWidthPercentage(100);
        subTable.addCell(makeCellForMaster(removeUnlocCode(fclBlBC.getDoorOriginForMasterBlPrint(bl)), Element.ALIGN_LEFT, courierFontMedium));
        subTable.addCell(makeCellForMaster("", Element.ALIGN_LEFT, courierFontMedium));
        cell = makeCellForMaster("\n", Element.ALIGN_LEFT, courierFontMedium);
        cell.setColspan(2);
        subTable.addCell(cell);
        cell = makeCellForMaster("", Element.ALIGN_LEFT, courierFontMedium);
        cell.setColspan(2);
        subTable.addCell(cell);
        StringBuilder vessel = new StringBuilder();
        vessel.append(bl.getVessel() != null ? bl.getVessel().getCodedesc() : "");
        vessel.append("  ");
        vessel.append("V.");
        vessel.append(bl.getVoyages() != null ? bl.getVoyages().toString() : "");
        subTable.addCell(makeCellForMaster(vessel.toString(), Element.ALIGN_LEFT, courierFontMedium));
        subTable.addCell(makeCellForMaster(removeUnlocCode(bl.getPortOfLoading() != null ? bl.getPortOfLoading() : ""), Element.ALIGN_LEFT, courierFontMedium));
        cell = makeCellForMaster("\n", Element.ALIGN_LEFT, courierFontMedium);
        cell.setColspan(2);
        subTable.addCell(cell);
        cell = makeCellForMaster("", Element.ALIGN_LEFT, courierFontMedium);
        cell.setColspan(2);
        subTable.addCell(cell);
        subTable.addCell(makeCellForMaster(removeUnlocCodeAppendCountryName(bl.getPortofDischarge() != null ? bl.getPortofDischarge() : ""), Element.ALIGN_LEFT, courierFontMedium));
        String doorOfDest = "";
        if (null != bl.getDoorOfDestination() && !"".equalsIgnoreCase(bl.getDoorOfDestination())) {
            doorOfDest = bl.getDoorOfDestination();
        } else {
            doorOfDest = bl.getFinalDestination() != null ? bl.getFinalDestination() : "";
        }
        subTable.addCell(makeCellForMaster(removeUnlocCodeAppendCountryName(fclBlBC.getFinalDeliveryForBlPrint(bl, "master")), Element.ALIGN_LEFT, courierFontMedium));


        subcell.addElement(subTable);
        subcell.setColspan(3);
        leftTable.addCell(subcell);

        detailsTable.addCell(leftTable);

        PdfPTable rightTable = makeTable(1);
        StringBuilder builder = new StringBuilder();
        builder.append("BOOKING # ");
        builder.append(bl.getBookingNo() != null ? bl.getBookingNo() : "");
        rightTable.addCell(makeCellForMaster(builder.toString(), Element.ALIGN_LEFT, courierFontLargeBold));
        rightTable.addCell(makeCellForMaster("", Element.ALIGN_LEFT, courierFontLargeBold));
        StringBuilder builder1 = new StringBuilder();
//        String reference = LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic");
//        builder1.append(reference);
        builder1.append(" REF.#");
        String fileNoPrefix = messageResources.getMessage("fileNumberPrefix");
        String defaultAgent = messageResources.getMessage("defaultAgent");
        String exportReferenceMorelength = "";
        //builder1.append(defaultAgent);
        builder1.append(" ");
        builder1.append(fileNoPrefix);
        builder1.append(bl.getFileNo() != null ? bl.getFileNo() : "");
        rightTable.addCell(makeCellForMaster(builder1.toString(), Element.ALIGN_LEFT, courierFontLargeBold));
        exportReferenceMorelength = null != bl.getExportReference() && !"".equals(bl.getExportReference()) ? bl.getExportReference() : "";
        rightTable.addCell(makeCellForMaster(exportReferenceMorelength.length() > 250 ? exportReferenceMorelength.substring(0,250) : exportReferenceMorelength, Element.ALIGN_LEFT, courierFontMedium));
        cell = makeCellForMaster("", Element.ALIGN_LEFT, courierFontMedium);
        for (int i = 0; i < 15; i++) {
            rightTable.addCell(cell);
        }
        rightTable.addCell(makeCellForMaster(null != bl.getRoutedByAgentCountry() && !"".equals(bl.getRoutedByAgentCountry()) ? bl.getRoutedByAgentCountry() : "", Element.ALIGN_LEFT, courierFontMedium));
        cell = makeCellForMaster("", Element.ALIGN_LEFT, courierFontMedium);
        for (int i = 0; i < 5; i++) {
            rightTable.addCell(cell);
        }
        if (commentList != null) {
            String faxNo = "";
            String email = "";
//                    if(CommonUtils.isNotEmpty(user.getFax())){
//                        faxNo = user.getFax();
//                    }else{
//                        faxNo = new SystemRulesDAO().getSystemRulesByCode("CompanyFax");
//                    }
            if (CommonUtils.isNotEmpty(user.getEmail())) {
                email = user.getEmail();
            } else {
                email = new SystemRulesDAO().getSystemRulesByCode("Email");
            }
            rightTable.addCell(makeCellForMaster(bl.getClauseDescription() != null ? bl.getClauseDescription() : "", Element.ALIGN_LEFT, courierFontMedium));
            rightTable.addCell(makeCellForMaster("", Element.ALIGN_LEFT, courierFontMedium));
            StringBuilder contractComment = new StringBuilder();
            //contractComment.append(commentList.get(18)!=null?commentList.get(18).toString().replace("${FAX}", faxNo):"");
            contractComment.append(MB100 != null ? MB100.replace("${EMAIL}", email) : "");
            //contractComment.append(commentList.get(23)!=null?commentList.get(23).toString():"");
            if (bl.getServiceContractNo() == null || bl.getServiceContractNo().equals("Yes")) {
                contractComment.append("\n");
                contractComment.append(MB101 != null ? MB101 : "");
                contractComment.append(" ");
                String ssLineNo = bl.getSslineNo();
                if (ssLineNo != null && !ssLineNo.equalsIgnoreCase("")) {
                    TradingPartnerDAO partnerDAO = new TradingPartnerDAO();
                    String eciAcct = new EdiDAO().getSsLine(ssLineNo);
                    contractComment.append(new EdiDAO().getScacOrContract(eciAcct, "FCL_contract_number"));
                    //                            contractComment.append(partnerDAO.getContractNo(ssLineNo));
                }
            }
            rightTable.addCell(makeCellForMaster(contractComment.toString(), Element.ALIGN_LEFT, courierFontMedium));
        }


        detailsTable.addCell(rightTable);
        cell = makeCellleftNoBorder("");
        cell.setColspan(2);
        detailsTable.addCell(cell);
        detailsTable.addCell(cell);
        PdfPTable marksContainerTable = fillMarksAndContinerInformation(bl, messageResources);
        cell = makeCellleftNoBorder("");
        cell.setColspan(5);
        marksContainerTable.addCell(cell);
        cell = makeCellleftNoBorder("");
        cell.setColspan(5);
        marksContainerTable.addCell(cell);
        cell = makeCellleftNoBorder("");
        cell.setColspan(5);
        marksContainerTable.addCell(cell);
        String destChargesBl = "";
        if (bl.getStreamShipBl() != null && (bl.getStreamShipBl().equalsIgnoreCase("P-Prepaid") || bl.getStreamShipBl().equalsIgnoreCase("P"))) {
            cell = makeCellForMaster("** ALL FREIGHT PREPAID **", Element.ALIGN_CENTER, courierFontMedium);
        } else if (bl.getStreamShipBl() != null && (bl.getStreamShipBl().equalsIgnoreCase("C-Collect") || bl.getStreamShipBl().equalsIgnoreCase("C"))) {
            cell = makeCellForMaster("** ALL FREIGHT COLLECT **", Element.ALIGN_CENTER, courierFontMedium);
        } else {
            cell = makeCellForMaster("** ALL FREIGHT **", Element.ALIGN_CENTER, courierFontMedium);
        }
        if (bl.getDestinationChargesPreCol() != null && (bl.getDestinationChargesPreCol().equalsIgnoreCase("P-Prepaid")
                || bl.getDestinationChargesPreCol().equalsIgnoreCase("P"))) {
            destChargesBl = "Prepaid";
        } else if (bl.getDestinationChargesPreCol() != null && (bl.getDestinationChargesPreCol().equalsIgnoreCase("C-Collect")
                || bl.getDestinationChargesPreCol().equalsIgnoreCase("C"))) {
            destChargesBl = "Collect";
        }
        cell.setColspan(5);
        marksContainerTable.addCell(cell);
        cell = makeCellForMaster("All Destination Charges " + destChargesBl, Element.ALIGN_CENTER, courierFontMedium);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(5);
        marksContainerTable.addCell(cell);
        cell = makeCellCenterNoBorder("");
        cell.setColspan(5);
        marksContainerTable.addCell(cell);
        cell = makeCellLeftNoBorder(removeUnlocCode(doorOfDest));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(5);
        marksContainerTable.addCell(cell);
        cell = makeCellCenterNoBorder("");
        cell.setColspan(5);
        marksContainerTable.addCell(cell);
        // Adding Tables To Document
        document.add(detailsTable);
        document.add(marksContainerTable);

    }

    public PdfPTable fillMarksAndContinerInformation(FclBl bl, MessageResources messageResources)
            throws DocumentException, Exception {
        PdfPCell cell;
        Font courierFontSmall = new Font(Font.COURIER, 8, Font.NORMAL, Color.BLACK);
        Font courierFontSmallBold = new Font(Font.COURIER, 8, Font.BOLD, Color.BLACK);
        Font courierFontMediumBold = new Font(Font.COURIER, 9, Font.BOLD, Color.BLACK);
        Font courierFontLargeBold = new Font(Font.COURIER, 11, Font.BOLD, Color.BLACK);
        PdfPTable particularsTable = makeTable(5);
        particularsTable.setWidths(new float[]{18, 8, 45, 14, 14});
        particularsTable.setWidthPercentage(100);

        NumberFormat numberFormat = new DecimalFormat("###,###,##0.000");
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
        for (Iterator iter = containerList.iterator(); iter.hasNext();) {
            boolean set = false;
            FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
            List<String> hazmatList = new ArrayList<String>();
            if (CommonUtils.isNotEmpty(fclBlContainer.getTrailerNo())) {
                List hazmatMaterialList = fclBlBC.getHazmatForBlPrint(fclBlContainer.getTrailerNoId(), FclBlConstants.FCLBL);
                hazmatList = new HazmatBC().getHazmatDetails(hazmatMaterialList);
            }
            StringBuilder marksNumber = new StringBuilder("");
            if (null != bl.getPrintContainersOnBL() && bl.getPrintContainersOnBL().equalsIgnoreCase("Yes")) {
                marksNumber.append(fclBlContainer.getTrailerNo() != null ? fclBlContainer.getTrailerNo() : "");
                marksNumber.append("\n");
                marksNumber.append("SEAL: ");
                marksNumber.append(fclBlContainer.getSealNo() != null ? fclBlContainer.getSealNo() : "");
            }
            if (fclBlContainer.getMarks() != null && !fclBlContainer.getMarks().equals("")) {
                marksNumber.append("\n");
                marksNumber.append(fclBlContainer.getMarks());
            }
            particularsTable.addCell(makeCellForMaster(marksNumber.toString(), Element.ALIGN_LEFT, courierFontLargeBold));
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
            if (null != bl.getNoOfPackages() && bl.getNoOfPackages().equalsIgnoreCase("Yes")) {
                particularsTable.addCell(makeCellForMaster(tempSizeLegened.toString(), Element.ALIGN_LEFT, courierFontSmallBold));
            } else {
                particularsTable.addCell(makeCellForMaster("", Element.ALIGN_LEFT, courierFontSmallBold));
            }
            List<FclBlMarks> fclMarksList = new ArrayList();
            FclBlContainerDAO fclBlContainerDAO = new FclBlContainerDAO();
            fclMarksList = fclBlContainerDAO.getPakagesDetails(fclBlContainer.getTrailerNoId());
            if (fclBlContainer.getFclBlMarks() != null) {
//                Iterator iterator = fclBlContainer.getFclBlMarks().iterator();
//                while (iterator.hasNext()) {
//                    FclBlMarks fclBlMarks = (FclBlMarks) iterator.next();
//                    fclMarksList.add(fclBlMarks);
//                }
                fclBlContainer.setFclMarksList(fclMarksList);
                List arrayList = fclBlContainer.getFclMarksList();
                if (arrayList.size() == 0) {
                    particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                    particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                    particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                } else {
                    for (Iterator iterator1 = arrayList.iterator(); iterator1.hasNext();) {
                        FclBlMarks fclBlmarks = (FclBlMarks) iterator1.next();
                        if (set == false) {
                            StringBuilder stcAndPackages = new StringBuilder();
                            if (null != bl.getTotalContainers() && bl.getTotalContainers().equalsIgnoreCase("Yes")) {
                                stcAndPackages.append("STC: ");
                                stcAndPackages.append(null != fclBlmarks.getNoOfPkgs() && fclBlmarks.getNoOfPkgs() != 0 ? fclBlmarks.getNoOfPkgs() : "");
                                stcAndPackages.append(" ");
                                stcAndPackages.append(null != fclBlmarks.getUom() ? fclBlmarks.getUom() : "");
                                stcAndPackages.append("\n");
                            }
                            if (fclBlmarks.getCopyDescription() != null && "Y".equals(fclBlmarks.getCopyDescription())) {
                                stcAndPackages.append(fclBlmarks.getDescPckgs() != null ? fclBlmarks.getDescPckgs() : "");
                            } else {
                                stcAndPackages.append(fclBlmarks.getDescForMasterBl() != null ? fclBlmarks.getDescForMasterBl() : "");
                            }
                            if (CommonUtils.isNotEmpty(hazmatList)) {
                                for (int i = 0; i < hazmatList.size(); i++) {
                                    StringBuilder hazmatBuild = new StringBuilder();
                                    stcAndPackages.append("\n");
                                    hazmatBuild.append("\n");
                                    hazmatBuild.append(CommonUtils.splitString((String) hazmatList.get(i), 50));
                                    stcAndPackages.append(hazmatBuild.toString());
                                }
                            }
                            particularsTable.addCell(makeCellForMaster(stcAndPackages.toString(), Element.ALIGN_LEFT, courierFontSmall));
                            PdfPTable ratesSubTable = makeTable(2);
                            ratesSubTable.setWidths(new float[]{60, 40});
                            ratesSubTable.setWidthPercentage(100);
                            double netWeightLBS = fclBlmarks.getNetweightLbs() != null ? fclBlmarks.getNetweightLbs() : 0.00;
                            double measureKGS = fclBlmarks.getNetweightKgs() != null ? fclBlmarks.getNetweightKgs() : 0.00;
                            if (measureKGS != 0.00) {
                                if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                    cell = makeCellForMaster(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureKGS)), Element.ALIGN_RIGHT, courierFontLargeBold);
                                } else {
                                    cell = makeCellForMaster(numberFormat.format(measureKGS).toString(), Element.ALIGN_RIGHT, courierFontLargeBold);
                                }
                                cell.setNoWrap(true);
                                ratesSubTable.addCell(cell);
                                ratesSubTable.addCell(makeCellForMaster(" KGS", Element.ALIGN_LEFT, courierFontLargeBold));
                            } else {
                                cell = makeCellLeftNoBorder("");
                                cell.setColspan(2);
                                ratesSubTable.addCell(cell);
                            }
                            if (netWeightLBS != 0.00) {
                                if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                    cell = makeCellForMaster(CommonUtils.trimTrailingZeros("" + numberFormat.format(netWeightLBS)), Element.ALIGN_RIGHT, courierFontLargeBold);
                                } else {
                                    cell = makeCellForMaster(numberFormat.format(netWeightLBS).toString(), Element.ALIGN_RIGHT, courierFontLargeBold);
                                }
                                cell.setNoWrap(true);
                                ratesSubTable.addCell(cell);
                                ratesSubTable.addCell(makeCellForMaster(" LBS", Element.ALIGN_LEFT, courierFontLargeBold));
                            } else {
                                cell = makeCellLeftNoBorder("");
                                cell.setColspan(2);
                                ratesSubTable.addCell(cell);
                            }
                            particularsTable.addCell(ratesSubTable);
                            PdfPTable ratesSubTable1 = makeTable(2);
                            ratesSubTable1.setWidths(new float[]{60, 40});
                            ratesSubTable1.setWidthPercentage(100);
                            double measureCFT = fclBlmarks.getMeasureCft() != null ? fclBlmarks.getMeasureCft()
                                    : 0.00;
                            StringBuilder cft = new StringBuilder();
                            double measureCbm = fclBlmarks.getMeasureCbm() != null ? fclBlmarks.getMeasureCbm()
                                    : 0.00;
                            if (measureCbm != 0.00) {
                                if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                    cell = makeCellForMaster(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureCbm)), Element.ALIGN_RIGHT, courierFontLargeBold);
                                } else {
                                    cell = makeCellForMaster(numberFormat.format(measureCbm).toString(), Element.ALIGN_RIGHT, courierFontLargeBold);
                                }
                                cell.setNoWrap(true);
                                ratesSubTable1.addCell(cell);
                                ratesSubTable1.addCell(makeCellForMaster(" CBM", Element.ALIGN_LEFT, courierFontLargeBold));
                            } else {
                                cell = makeCellLeftNoBorder("");
                                cell.setColspan(2);
                                ratesSubTable.addCell(cell);
                            }
                            if (measureCFT != 0.00) {
                                if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                    cell = makeCellForMaster(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureCFT)), Element.ALIGN_RIGHT, courierFontLargeBold);
                                } else {
                                    cell = makeCellForMaster(numberFormat.format(measureCFT).toString(), Element.ALIGN_RIGHT, courierFontLargeBold);
                                }
                                cell.setNoWrap(true);
                                ratesSubTable1.addCell(cell);
                                ratesSubTable1.addCell(makeCellForMaster(" CFT", Element.ALIGN_LEFT, courierFontLargeBold));
                            } else {
                                cell = makeCellLeftNoBorder("");
                                cell.setColspan(2);
                                ratesSubTable.addCell(cell);
                            }
                            particularsTable.addCell(ratesSubTable1);
                            set = true;
                        } else {
                            particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                            particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                            StringBuilder stcAndPackages = new StringBuilder();
                            if (null != bl.getTotalContainers() && bl.getTotalContainers().equalsIgnoreCase("Yes")) {
                                stcAndPackages.append("STC: ");
                                stcAndPackages.append(null != fclBlmarks.getNoOfPkgs() && fclBlmarks.getNoOfPkgs() != 0 ? fclBlmarks.getNoOfPkgs() : "");
                                stcAndPackages.append(" ");
                                stcAndPackages.append(null != fclBlmarks.getUom() ? fclBlmarks.getUom() : "");
                                stcAndPackages.append("\n");
                            }
                            if (fclBlmarks.getCopyDescription() != null && "Y".equals(fclBlmarks.getCopyDescription())) {
                                stcAndPackages.append(fclBlmarks.getDescPckgs() != null ? fclBlmarks.getDescPckgs() : "");
                            } else {
                                stcAndPackages.append(fclBlmarks.getDescForMasterBl() != null ? fclBlmarks.getDescForMasterBl() : "");
                            }
                            if (CommonUtils.isNotEmpty(hazmatList)) {
                                for (int i = 0; i < hazmatList.size(); i++) {
                                    StringBuilder hazmatBuild = new StringBuilder();
                                    hazmatBuild.append("\n");
                                    hazmatBuild.append(CommonUtils.splitString((String) hazmatList.get(i), 50));
                                    stcAndPackages.append(hazmatBuild.toString());
                                }
                            }
                            particularsTable.addCell(makeCellForMaster(stcAndPackages.toString(), Element.ALIGN_LEFT, courierFontSmall));
                            PdfPTable ratesSubTable = makeTable(2);
                            ratesSubTable.setWidths(new float[]{60, 40});
                            ratesSubTable.setWidthPercentage(100);
                            double netWeightLBS = fclBlmarks.getNetweightLbs() != null ? fclBlmarks.getNetweightLbs() : 0.00;
                            double measureKGS = fclBlmarks.getNetweightKgs() != null ? fclBlmarks.getNetweightKgs() : 0.00;
                            if (measureKGS != 0.00) {
                                if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                    cell = makeCellForMaster(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureKGS)), Element.ALIGN_RIGHT, courierFontLargeBold);
                                } else {
                                    cell = makeCellForMaster(numberFormat.format(measureKGS).toString(), Element.ALIGN_RIGHT, courierFontLargeBold);
                                }
                                cell.setNoWrap(true);
                                ratesSubTable.addCell(cell);
                                ratesSubTable.addCell(makeCellForMaster(" KGS", Element.ALIGN_LEFT, courierFontLargeBold));
                            } else {
                                cell = makeCellLeftNoBorder("");
                                cell.setColspan(2);
                                ratesSubTable.addCell(cell);
                            }
                            if (netWeightLBS != 0.00) {
                                if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                    cell = makeCellForMaster(CommonUtils.trimTrailingZeros("" + numberFormat.format(netWeightLBS)), Element.ALIGN_RIGHT, courierFontLargeBold);
                                } else {
                                    cell = makeCellForMaster(numberFormat.format(netWeightLBS).toString(), Element.ALIGN_RIGHT, courierFontLargeBold);
                                }
                                cell.setNoWrap(true);
                                ratesSubTable.addCell(cell);
                                ratesSubTable.addCell(makeCellForMaster(" LBS", Element.ALIGN_LEFT, courierFontLargeBold));
                            } else {
                                cell = makeCellLeftNoBorder("");
                                cell.setColspan(2);
                                ratesSubTable.addCell(cell);
                            }
                            particularsTable.addCell(ratesSubTable);
                            PdfPTable ratesSubTable1 = makeTable(2);
                            ratesSubTable1.setWidths(new float[]{60, 40});
                            ratesSubTable1.setWidthPercentage(100);
                            double measureCFT = fclBlmarks.getMeasureCft() != null ? fclBlmarks.getMeasureCft()
                                    : 0.00;
                            StringBuilder cft = new StringBuilder();
                            double measureCbm = fclBlmarks.getMeasureCbm() != null ? fclBlmarks.getMeasureCbm()
                                    : 0.00;
                            if (measureCbm != 0.00) {
                                if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                    cell = makeCellForMaster(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureCbm)), Element.ALIGN_RIGHT, courierFontLargeBold);
                                } else {
                                    cell = makeCellForMaster(numberFormat.format(measureCbm).toString(), Element.ALIGN_RIGHT, courierFontLargeBold);
                                }
                                cell.setNoWrap(true);
                                ratesSubTable1.addCell(cell);
                                ratesSubTable1.addCell(makeCellForMaster(" CBM", Element.ALIGN_LEFT, courierFontLargeBold));
                            } else {
                                cell = makeCellLeftNoBorder("");
                                cell.setColspan(2);
                                ratesSubTable.addCell(cell);
                            }
                            if (measureCFT != 0.00) {
                                if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                    cell = makeCellForMaster(CommonUtils.trimTrailingZeros("" + numberFormat.format(measureCFT)), Element.ALIGN_RIGHT, courierFontLargeBold);
                                } else {
                                    cell = makeCellForMaster(numberFormat.format(measureCFT).toString(), Element.ALIGN_RIGHT, courierFontLargeBold);
                                }
                                cell.setNoWrap(true);
                                ratesSubTable1.addCell(cell);
                                ratesSubTable1.addCell(makeCellForMaster(" CFT", Element.ALIGN_LEFT, courierFontLargeBold));
                            } else {
                                cell = makeCellLeftNoBorder("");
                                cell.setColspan(2);
                                ratesSubTable.addCell(cell);
                            }
                            particularsTable.addCell(ratesSubTable1);
                        }
                    }
                }
            } else {
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
            }
        }
        PdfPCell aesHeading = makeCellForMaster("", Element.ALIGN_LEFT, courierFontSmall);
        particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
        particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
        particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("\n", courierFontSmallBold));
        particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
        particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
        List aesList = new FclInbondDetailsDAO().findAesdetails("fileNo", bl.getFileNo());
        if (!aesList.isEmpty()) {
            particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
            particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
            particularsTable.addCell(aesHeading);
            particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
            particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
        }
        if (!aesList.isEmpty()) {
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
                } else if (CommonUtils.isNotEmpty(aesDet.getException())) {
                    aes.append("AES: " + aesDet.getException());
                    count = 0;
                    printAes = true;
                }
                if (printAes) {
                    particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                    particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                    PdfPCell aesDeta = makeCellLeftNoBorderPalatinoFclBl(aes.toString(), courierFontSmallBold);
                    aesDeta.setNoWrap(true);
                    particularsTable.addCell(aesDeta);
                    particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                    particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                    aes = new StringBuilder("");
                }
            }
            if (count == 1) {
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                PdfPCell aesDeta = makeCellLeftNoBorderPalatinoFclBl(aes.toString(), courierFontSmallBold);
                particularsTable.addCell(aesDeta);
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
            }

        }
        if (null != bl.getFclInbondDetails()) {
            String heading = "INBOND: ";
            for (Iterator iterator = bl.getFclInbondDetails().iterator(); iterator.hasNext();) {
                FclInbondDetails inbondDet = (FclInbondDetails) iterator.next();
                StringBuilder inbondDetailsBuilder = new StringBuilder();
                inbondDetailsBuilder.append((CommonFunctions.isNotNull(inbondDet.getInbondType())) ? (heading + inbondDet.getInbondType() + " " + inbondDet.getInbondNumber()) : (heading + inbondDet.getInbondNumber()));
                PdfPCell inbondCell = makeCellForMaster(inbondDetailsBuilder.toString(), Element.ALIGN_LEFT, courierFontSmall);
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                particularsTable.addCell(inbondCell);
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
                particularsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", courierFontSmallBold));
            }
        }
        return particularsTable;
    }

    public void onEndPage(PdfWriter writer, Document document) {

        try {
            //this for print page number at the bottom in the format x of y
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            String text = "Page " + writer.getPageNumber() + " of ";
            float textBase = document.bottom() - (document.bottomMargin() - 20);
            //float textBase = document.bottom() - 20;
            float textSize = helv.getWidthPoint(text, 12);
            cb.beginText();
            cb.setFontAndSize(helv, 7);
            cb.setTextMatrix(document.left() + 280, textBase);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(total, document.left() + 260 + textSize, textBase);
            cb.restoreState();


            ///this for the water mark..........................
            BaseFont helv;
            PdfGState gstate;
            Font hellv;
            String waterMark = "";
            waterMark = PrintReportsConstants.STEAMSHIP_MASTER_BL;
            try {
                helv = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI,
                        BaseFont.NOT_EMBEDDED);

            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
            gstate = new PdfGState();
            gstate.setFillOpacity(0.10f);
            gstate.setStrokeOpacity(0.3f);
            PdfContentByte contentunder = writer.getDirectContentUnder();
            contentunder.saveState();
            contentunder.setGState(gstate);
            contentunder.beginText();
            contentunder.setFontAndSize(helv, 50);
            contentunder.showTextAligned(Element.ALIGN_CENTER, waterMark,
                    document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 45);
            contentunder.endText();
            contentunder.restoreState();
        } catch (Exception e) {
            log.info("onEndPage Failed-" + new Date(),e);
        }
    }

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

    public String getBolNo(String bolId) {
        String BolNo = "";
        if (null != bolId && !bolId.equalsIgnoreCase("")) {
            if (-1 != bolId.indexOf("=")) {
                BolNo = bolId.substring(0, bolId.indexOf("="));
            } else {
                BolNo = bolId;
            }

        }
        return BolNo;
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
            } else {
                portName = port;
            }
        }
        return portName;
    }

    public String createSteamShipBlReport(FclBl bl, String fileName,
            String realPath, MessageResources messageResources, User user, List commentList) {
        try {
            FclBl fclbl = new FclBlDAO().getOriginalBl(bl.getFileNo());
            this.initialize(fileName, bl);
            this.printCommands();
            this.createbody(fclbl, realPath, messageResources, user, commentList);
            this.destroy();
        } catch (Exception e) {
            log.info("Exception Caught in SteamShipBLPdfCreator-" + new Date(),e);
        }
        return "fileName";
    }

    public void printCommands() throws Exception {
        Iterator iter = new GenericCodeDAO().getCommentsListForStreamShipReport();
        while (iter.hasNext()) {
            Object[] row = (Object[]) iter.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null) {
                if ("MB100".equals(code)) {
                    MB100 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("MB101".equals(code)) {
                    MB101 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                }
            }
        }
    }
}
