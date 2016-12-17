package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.BlUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclOptions;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.lowagie.text.Chunk;

public class LclSSMasterFromHouseBLPdfCreator extends LclReportFormatMethods implements ConstantsInterface, LclCommonConstant {

    private static final Logger log = Logger.getLogger(LclSSMasterFromHouseBLPdfCreator.class);
    private LclBl lclbl;
    private LclBooking lclBooking;
    private BlUtils blUtils = new BlUtils();
    private LclUtils lclUtils = new LclUtils();
    private String polValues = "";
    private String podValues = "";
    private int commodity_count = 0;
    private String sailDate = "", termsType1 = "";
    List<LclBlPiece> lclBlPiecesList = null;
    LclUnitSs lclUnitSs = null;
    private String ladenSailDateRemarks = "", engmet = "";
    private String fdOverride = "N", hblPolOverRideKey = "N", miniKeyValue = "N", impKeyValue = "N", metKeyValue = "N", hblPierOverRideKey = "N",
            printPierPol = "Y", ladenSailDateOptKey = "N", printTermsTypeKey = "ROUTING", aesKeyValue = "N", ncmKeyValue = "N", hsKeyValue = "N", printHazBeforeKeyValue = "N";
    String exportRefRemarks = "";
    String routingInsRemarks = "";
    
    public void SetLclBLPdfCreator(LclBl lclbl, LclBooking lclBooking) throws Exception {
        if (CommonFunctions.isNotNull(lclbl)) {
            List<LclOptions> optionsList = lclbl.getLclFileNumber().getLclOptionsList();
            for (LclOptions options : optionsList) {
                if ("PIER".equalsIgnoreCase(options.getOptionValue())) {
                    printPierPol = options.getOptionKey();
                }
                if ("HBLPOL".equalsIgnoreCase(options.getOptionValue())) {
                    hblPolOverRideKey = options.getOptionKey();
                }
                if ("HBLPIER".equalsIgnoreCase(options.getOptionValue())) {
                    hblPierOverRideKey = options.getOptionKey();
                }
                if ("DELIVERY".equalsIgnoreCase(options.getOptionValue())) {
                    fdOverride = options.getOptionKey();
                }
                if ("AES".equalsIgnoreCase(options.getOptionValue())) {
                    aesKeyValue = options.getOptionKey();
                }
                if ("IMP".equalsIgnoreCase(options.getOptionValue())) {
                    impKeyValue = options.getOptionKey();
                }
                if ("MET".equalsIgnoreCase(options.getOptionValue())) {
                    metKeyValue = options.getOptionKey();
                }
                if ("MINI".equalsIgnoreCase(options.getOptionValue())) {
                    miniKeyValue = options.getOptionKey();
                }
                if ("PRINTTERMSTYPE".equalsIgnoreCase(options.getOptionValue())) {
                    printTermsTypeKey = options.getOptionKey();
                }
                if ("LADENSAILDATE".equalsIgnoreCase(options.getOptionValue())) {
                    ladenSailDateOptKey = options.getOptionKey();
                }
                if ("NCM".equalsIgnoreCase(options.getOptionValue())) {
                    ncmKeyValue = options.getOptionKey();
                }
                if ("HS".equalsIgnoreCase(options.getOptionValue())) {
                    hsKeyValue = options.getOptionKey();
                }
                if ("PRINTHAZBEFORE".equalsIgnoreCase(options.getOptionValue())) {
                    printHazBeforeKeyValue = options.getOptionKey();
                }
            }
            lclBlPiecesList = lclbl.getLclFileNumber().getLclBlPieceList();
            engmet = new PortsDAO().getEngmet(lclbl.getFinalDestination() != null
                    ? lclbl.getFinalDestination().getUnLocationCode() : lclbl.getPortOfDestination().getUnLocationCode());
            String printTermsValueBodyBl = null != lclbl.getTermsType1() ? lclbl.getTermsType1() : "";
            if (!"".equalsIgnoreCase(printTermsValueBodyBl)) {
                termsType1 = "ER".equalsIgnoreCase(printTermsValueBodyBl) ? "\nExpress Release"
                        : "OR".equalsIgnoreCase(printTermsValueBodyBl) ? "\nOriginals Required"
                                : "ORD".equalsIgnoreCase(printTermsValueBodyBl) ? "\nOriginals Released At Destination"
                                        : "DER".equalsIgnoreCase(printTermsValueBodyBl) ? "\nDo Not Express Release"
                                                : "MBL".equalsIgnoreCase(printTermsValueBodyBl) ? "\nMEMO HOUSE BILL OF LADING " : "";
            }


            if (CommonFunctions.isNotNull(lclbl.getPortOfLoading())) {
                polValues = lclbl.getPortOfLoading().getUnLocationName();
            }
            if (CommonFunctions.isNotNull(lclbl.getPortOfLoading()) && CommonFunctions.isNotNull(lclbl.getPortOfLoading().getStateId())
                    && CommonFunctions.isNotNull(lclbl.getPortOfLoading().getStateId().getCode())) {
                polValues += "," + lclbl.getPortOfLoading().getStateId().getCode();
            }
        }
        if (CommonFunctions.isNotNull(lclbl)) {
            if (CommonFunctions.isNotNull(lclbl.getPortOfDestination()) && CommonFunctions.isNotNull(lclbl.getPortOfDestination().getUnLocationName())) {
                podValues = lclbl.getPortOfDestination().getUnLocationName();
            }
            if (CommonFunctions.isNotNull(lclbl.getPortOfDestination()) && CommonFunctions.isNotNull(lclbl.getPortOfDestination().getCountryId())
                    && CommonFunctions.isNotNull(lclbl.getPortOfDestination().getCountryId().getCodedesc())) {
                podValues += ", " + lclbl.getPortOfDestination().getCountryId().getCodedesc();
            }
        }
        String[] remarkTypes = {REMARKS_TYPE_ROUTING_INSTRU, REMARKS_TYPE_EXPORT_REF};
        List remarks = new LclRemarksDAO().getRemarksByTypes(lclbl.getFileNumberId(), remarkTypes);
        for (Object row : remarks) {
            Object[] col = (Object[]) row;
            if (col[1].toString().equalsIgnoreCase("Export Reference")) {
                exportRefRemarks = col[0].toString();
            }
            if (col[1].toString().equalsIgnoreCase("Routing Instruction")) {
                routingInsRemarks = col[0].toString();
            }
        }
    }

    public void createPdfReport(String realPath, String outputFileName,
            String documentName, String fileId, String unitSsId, String printRadio, Boolean ratedFlag) throws Exception {
        init(outputFileName, documentName, fileId, printRadio);
    }

    private void init(String outputFileName, String documentName, String fileId,
            String printRadio) throws Exception {
        document = new Document(PageSize.A4, 18, 3, 30, 200);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        LclSSMasterFromHouseBLPdfCreator.PageCreation event = new LclSSMasterFromHouseBLPdfCreator.PageCreation();
        pdfWriter.setPageEvent(event);
        lclbl = new LCLBlDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
        new LCLBlDAO().getCurrentSession().evict(lclbl);
        lclBooking = lclbl.getLclFileNumber().getLclBooking();
        SetLclBLPdfCreator(lclbl, lclBooking);
        document.open();
        for (LclBlPiece lclBlPiece : lclBlPiecesList) {
            commodity_count++;
            document.add(commodityTab(lclBlPiece));
        }
        document.close();
    }

    public PdfPTable headerSection() throws Exception {
        Paragraph p = null;
        table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(-0.5f);
        p = new Paragraph(2f, "ECU WORLDWIDE", totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingAfter(5f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(2f, "PAGE " + pdfWriter.getPageNumber(), totalFontQuote);
        p.setAlignment(Element.ALIGN_RIGHT);
        p.setSpacingAfter(5f);
        cell.setPaddingRight(30f);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    private PdfPTable setTermsType() throws Exception {
        String collectAmount = new LCLBlDAO().getCollectAmount(lclbl.getFileNumberId());
        Paragraph p = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        if (CommonUtils.isNotEmpty(collectAmount)) {
            p = new Paragraph(7f, "*********************************************", totalFontQuote);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(2f);
            cell.addElement(p);
            p = new Paragraph(7f, "please collect $" + collectAmount.toUpperCase() + " on our behalf", totalFontQuote);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(5f);
            cell.addElement(p);
            p = new Paragraph(7f, "*********************************************", totalFontQuote);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(20f);
            cell.addElement(p);
        }
        if (CommonUtils.isNotEmpty(termsType1)) {
            p = new Paragraph(7f, "*****" + termsType1.replaceAll("\n", "").toUpperCase() + "*****", totalFontQuote);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(10f);
            cell.addElement(p);
        }
        p = new Paragraph(7f, "*****FREIGHT PREPAID*****", totalFontQuote);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable commodityTab(LclBlPiece lclBlPiece) throws DocumentException, Exception {
        LclReportUtils reportUtils = new LclReportUtils();
        LclConsolidateDAO consolidateDAO = new LclConsolidateDAO();
        List<Long> conoslidatelist = consolidateDAO.getConsolidatesFiles(lclbl.getFileNumberId());
        String conoslidateFileNumber = consolidateDAO.getConsolidatesFileNumbers(String.valueOf(lclbl.getFileNumberId()));
        conoslidatelist.add(lclbl.getFileNumberId());
        Lcl3pRefNoDAO _3pRefNoDAO = new Lcl3pRefNoDAO();
        StringBuilder inbondAppend = new StringBuilder();//Inbond
        inbondAppend.append(reportUtils.appendInbond(conoslidatelist, new LclInbondsDAO()));
        StringBuilder ncmAppend = new StringBuilder();//NCM
        ncmAppend.append(reportUtils.appendNcm(conoslidatelist, _3pRefNoDAO));
        StringBuilder hsAppend = new StringBuilder();//HS
        hsAppend.append(reportUtils.appendHsCode(conoslidatelist, new LclBookingHsCodeDAO()));
        StringBuilder aesAppend = new StringBuilder();//AES
        aesAppend.append(reportUtils.appendAes(conoslidatelist, _3pRefNoDAO));
        StringBuilder cpAppend = new StringBuilder();//CP
        cpAppend.append(reportUtils.appendCP(conoslidatelist, _3pRefNoDAO));
        StringBuilder invNoAppend = new StringBuilder();//INVOICENO
        invNoAppend.append(reportUtils.appendInvoiceNo(conoslidateFileNumber, lclbl.getLclFileNumber().getFileNumber()));
        LclHazmatDAO hazmatDAO = new LclHazmatDAO();
        StringBuilder hazmatValues = new StringBuilder();
        hazmatValues.append(reportUtils.appendHazmatForFreightInvoice(conoslidatelist, hazmatDAO));//Hazmat List
        Paragraph p = null;
        table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 0.75f, 4.74f, 1.047f, 1.047f});
        cell = new PdfPCell();
        cell.setBorder(0);

        if (lclBlPiece.getMarkNoDesc() != null && !lclBlPiece.getMarkNoDesc().equals("")) {
            p = new Paragraph(7f, "" + lclBlPiece.getMarkNoDesc().toUpperCase(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(10f);
            cell.addElement(p);
        }
        if (CommonUtils.isNotEmpty(inbondAppend)) {
            p = new Paragraph(7f, "INBOND:" + inbondAppend.toString(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
        }
        if ("N".equalsIgnoreCase(ncmKeyValue) && CommonUtils.isNotEmpty(ncmAppend)) {
            p = new Paragraph(7f, "" + ncmAppend.toString(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(2f);
            cell.addElement(p);
        }
        if ("N".equalsIgnoreCase(aesKeyValue) && CommonUtils.isNotEmpty(aesAppend)) {
            p = new Paragraph(7f, "" + aesAppend.toString(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(2f);
            cell.addElement(p);
        }

        if ("N".equalsIgnoreCase(hsKeyValue) && CommonUtils.isNotEmpty(hsAppend)) {
            p = new Paragraph(7f, "" + hsAppend.toString(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(2f);
            cell.addElement(p);
        }

        table.addCell(cell);

        //no of pkgs
        cell = new PdfPCell();
        cell.setBorder(0);
        if ("Y".equalsIgnoreCase(miniKeyValue)) {
            List<LclBookingPiece> lclBookingPieceList = new LclBookingPieceDAO().getConsolidatePieceCount(conoslidatelist);
            for (LclBookingPiece bkgPiece : lclBookingPieceList) {
                String packageDesc = null != bkgPiece.getActualPackageType() ? bkgPiece.getActualPackageType().getAbbr01()
                        : bkgPiece.getPackageType().getAbbr01();
                Integer pieceCount = bkgPiece.getActualPieceCount() != null ? bkgPiece.getActualPieceCount()
                        : bkgPiece.getBookedPieceCount();
                p = new Paragraph(7f, "" + pieceCount + " " + packageDesc, totalFontQuote);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
            }
        } else {
            if (lclBlPiece != null && lclBlPiece.getActualPieceCount() != null && lclBlPiece.getPackageType().getAbbr01() != null) {
                p = new Paragraph(7f, "" + lclBlPiece.getActualPieceCount() + " " + lclBlPiece.getPackageType().getAbbr01(),
                        totalFontQuote);
                p.setAlignment(Element.ALIGN_CENTER);
                // p.setSpacingAfter(spaceSize);
                cell.addElement(p);
            }
        }
        //desc
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        // print option "Y" Print Haz info first before Descriptions
        if (CommonUtils.isNotEmpty(hazmatValues) && "Y".equalsIgnoreCase(printHazBeforeKeyValue)) {
            String hazmat = hazmatValues.toString().replace("WEIGHT", "WT");
            p = new Paragraph(7f, "" + hazmat.toUpperCase(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(10f);
            cell.addElement(p);
        }
        String commodityDesc = null != lclBlPiece.getPieceDesc()
                ? lclBlPiece.getPieceDesc().toUpperCase() : "";
        p = new Paragraph(7f, "" + commodityDesc
                + ("BODYBL".equalsIgnoreCase(ladenSailDateOptKey) ? ladenSailDateRemarks : "")
                + ("BODYBL".equalsIgnoreCase(printTermsTypeKey) ? termsType1 : ""), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingAfter(10f);
        cell.addElement(p);
        if (CommonUtils.isNotEmpty(hazmatValues) && "N".equalsIgnoreCase(printHazBeforeKeyValue)) {
            String hazmat = hazmatValues.toString().replace("WEIGHT", "WT");
            p = new Paragraph(7f, "" + hazmat.toUpperCase(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(2f);
            cell.addElement(p);
        }
        if ("Y".equalsIgnoreCase(ncmKeyValue) && CommonUtils.isNotEmpty(ncmAppend)) {
            p = new Paragraph(7f, "" + ncmAppend.toString(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(2f);
            cell.addElement(p);
        }
        if ("Y".equalsIgnoreCase(aesKeyValue) && CommonUtils.isNotEmpty(aesAppend)) {
            p = new Paragraph(7f, "" + aesAppend.toString(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
        }
        if ("Y".equalsIgnoreCase(hsKeyValue) && CommonUtils.isNotEmpty(hsAppend)) {
            p = new Paragraph(7f, "" + hsAppend.toString(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(2f);
            cell.addElement(p);
        }
        PdfPTable nestedTable = new PdfPTable(2);
        nestedTable.setWidthPercentage(100f);
        PdfPCell inner_Cell1 = new PdfPCell();
        inner_Cell1.setBorder(0);

        p = new Paragraph(7f, "" + invNoAppend.toString(), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        inner_Cell1.addElement(p);

        nestedTable.addCell(inner_Cell1);
        PdfPCell inner_Cell2 = new PdfPCell();
        inner_Cell2.setBorder(0);

        p = new Paragraph(7f, "" + cpAppend.toString(), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        inner_Cell2.addElement(p);
        nestedTable.addCell(inner_Cell2);
        cell.addElement(nestedTable);
        table.addCell(cell);
        setCaribbenAndNonCaribben(lclBlPiece, impKeyValue, metKeyValue, engmet.equalsIgnoreCase("M"));
        if (commodity_count == lclBlPiecesList.size()) {
            table.setExtendLastRow(true);
        }
        return table;
    }

    public void setCaribbenAndNonCaribben(LclBlPiece lclBlPiece, String impKeyValue,
            String metKeyValue, boolean iscaribben) throws Exception {
        String AWImp = "0.000", AVImp = "0.000";
        String AWMet = "0.000", AVMet = "0.000";
        if (lclBlPiece.getActualWeightImperial() != null) {
            AWImp = "Y".equalsIgnoreCase(impKeyValue)
                    ? NumberUtils.convertToThreeDecimalhash(lclBlPiece.getActualWeightImperial().doubleValue())
                    : lclBlPiece.getActualWeightImperial().setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        }
        if (lclBlPiece.getActualVolumeImperial() != null) {
            AVImp = "Y".equalsIgnoreCase(impKeyValue)
                    ? NumberUtils.convertToThreeDecimalhash(lclBlPiece.getActualVolumeImperial().doubleValue())
                    : lclBlPiece.getActualVolumeImperial().setScale(0, BigDecimal.ROUND_HALF_UP).toString();
        }
        if (lclBlPiece.getActualWeightMetric() != null) {
            AWMet = NumberUtils.convertToThreeDecimalhash(lclBlPiece.getActualWeightMetric().doubleValue());
        }
        if (lclBlPiece.getActualVolumeMetric() != null) {
            AVMet = NumberUtils.convertToThreeDecimalhash(lclBlPiece.getActualVolumeMetric().doubleValue());
        }
        Paragraph p = null;
        cell = new PdfPCell();
        cell.setBorder(0);

        PdfPTable inner_Table = new PdfPTable(1);
        inner_Table.setWidthPercentage(100f);
        PdfPCell inner_Cell = null;

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        if ("Y".equalsIgnoreCase(metKeyValue)) {
            p = new Paragraph(6f, "LBS", blackBoldFont65);
        } else {
            p = new Paragraph(6f, !iscaribben ? "LBS" : "KGS", blackBoldFont65);
        }
        p.setAlignment(Element.ALIGN_CENTER);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        if ("Y".equalsIgnoreCase(metKeyValue)) {
            p = new Paragraph(7f, AWImp, totalFontQuote);
        } else {
            p = new Paragraph(7f, !iscaribben ? AWImp : AWMet, totalFontQuote);
        }
        p.setAlignment(Element.ALIGN_CENTER);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        inner_Cell.setPaddingBottom(18f);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        if ("Y".equalsIgnoreCase(metKeyValue)) {
            p = new Paragraph(6f, "KGS", blackBoldFont65);
        } else {
            p = new Paragraph(6f, !iscaribben ? "KGS" : "", blackBoldFont65);
        }
        p.setAlignment(Element.ALIGN_CENTER);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        p = new Paragraph(7f, !"Y".equalsIgnoreCase(metKeyValue) && iscaribben ? "" : AWMet, totalFontQuote);
        p.setAlignment(Element.ALIGN_CENTER);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);
        if (commodity_count != lclBlPiecesList.size()) {
            inner_Cell = new PdfPCell();
            inner_Cell.setBorder(0);
            inner_Cell.setPaddingBottom(16f);
            inner_Table.addCell(inner_Cell);
        }
        cell.addElement(inner_Table);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        inner_Table = new PdfPTable(1);
        inner_Table.setWidthPercentage(100f);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        if ("Y".equalsIgnoreCase(metKeyValue)) {
            p = new Paragraph(6f, "CU.FT.", blackBoldFont65);
        } else {
            p = new Paragraph(6f, !iscaribben ? "CU.FT." : "CBM", blackBoldFont65);
        }
        p.setAlignment(Element.ALIGN_CENTER);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        if ("Y".equalsIgnoreCase(metKeyValue)) {
            p = new Paragraph(7f, AVImp, totalFontQuote);
        } else {
            p = new Paragraph(7f, !iscaribben ? AVImp : AVMet, totalFontQuote);
        }
        p.setAlignment(Element.ALIGN_CENTER);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        inner_Cell.setPaddingBottom(18f);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        if ("Y".equalsIgnoreCase(metKeyValue)) {
            p = new Paragraph(6f, "CBM", blackBoldFont65);
        } else {
            p = new Paragraph(6f, !iscaribben ? "CBM" : "", blackBoldFont65);
        }
        p.setAlignment(Element.ALIGN_CENTER);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        p = new Paragraph(7f, !"Y".equalsIgnoreCase(metKeyValue) && iscaribben ? "" : AVMet, totalFontQuote);
        p.setAlignment(Element.ALIGN_CENTER);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);
        if (commodity_count != lclBlPiecesList.size()) {
            inner_Cell = new PdfPCell();
            inner_Cell.setBorder(0);
            inner_Cell.setPaddingBottom(16f);
            inner_Table.addCell(inner_Cell);
        }
        cell.addElement(inner_Table);
        table.addCell(cell);
    }

    public PdfPTable bodySection() throws Exception {
        ExportVoyageSearchModel pickedDetails = new LclUnitSsDAO().getPickedVoyageByVessel(lclBooking.getFileNumberId(), "E");
        LclSsDetail bookedOrPickedVoy = null;
        Paragraph p = null;
        StringBuilder carrierName = new StringBuilder();
        StringBuilder pierValues = new StringBuilder();
        if (pickedDetails != null && CommonUtils.isNotEmpty(pickedDetails.getUnitSsId())) {
            lclUnitSs = new LclUnitSsDAO().findById(Long.parseLong(pickedDetails.getUnitSsId()));
            bookedOrPickedVoy = lclUnitSs.getLclSsHeader().getVesselSsDetail();
        } else {
            bookedOrPickedVoy = null != lclbl.getBookedSsHeaderId() ? lclbl.getBookedSsHeaderId().getVesselSsDetail() : null;
        }

        if (bookedOrPickedVoy != null) {
            ladenSailDateRemarks = "\nLaden On Board:" + sailDate;
            if (CommonFunctions.isNotNull(bookedOrPickedVoy.getSpReferenceName())) {
                carrierName.append(bookedOrPickedVoy.getSpReferenceName()).append("  ");
            }
            if (CommonUtils.isNotEmpty(bookedOrPickedVoy.getTransMode())) {
                carrierName.append(bookedOrPickedVoy.getTransMode()).append(". ");
            }
            if (CommonUtils.isNotEmpty(bookedOrPickedVoy.getSpReferenceNo())) {
                carrierName.append(bookedOrPickedVoy.getSpReferenceNo());
            }
            pierValues.append(bookedOrPickedVoy.getDeparture().getUnLocationName());
            if (null != bookedOrPickedVoy.getDeparture().getStateId()) {
                pierValues.append("/").append(bookedOrPickedVoy.getDeparture().getStateId().getCode());
            }
        }

        /* Pier Logic */
        String pierValue = "N".equalsIgnoreCase(printPierPol)
                ? ("N".equalsIgnoreCase(printPierPol) && !"N".equalsIgnoreCase(hblPierOverRideKey))
                        ? hblPierOverRideKey
                        : pierValues.toString() : "";
        String pol = "";
        if ("Y".equalsIgnoreCase(printPierPol)) {
            if ("N".equalsIgnoreCase(hblPierOverRideKey)) {
                pol = pierValues.toString();
            } else {
                pol = hblPierOverRideKey;
            }
        } else {
            pol = !"N".equalsIgnoreCase(hblPolOverRideKey) ? hblPolOverRideKey
                    : polValues;
        }

        table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{5.5f, 4.5f});
        PdfPCell bCell = null;

        bCell = new PdfPCell();
        bCell.setBorder(0);
        bCell.setPadding(0f);
        PdfPTable bTable = new PdfPTable(2);
        bTable.setWidths(new float[]{5f, 4f});
        bTable.setWidthPercentage(100f);

        PdfPCell taCell = new PdfPCell();
        taCell.setBorder(0);
        taCell.setColspan(2);
        taCell.setPadding(0f);
        taCell.setPaddingLeft(-0.5f);
        Chunk chunk = new Chunk();
        chunk.append("2401 N.W. 69TH STREET ").append("\n").append("MIAMI, FL 33147");
        p = new Paragraph(11f, "" + chunk.getContent(), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingAfter(10f);
        taCell.addElement(p);
        bTable.addCell(taCell);

//        PdfPCell shCell = new PdfPCell();
//        shCell.setBorder(0);
//        shCell.setColspan(2);
//        shCell.setPadding(0f);
//        shCell.setPaddingLeft(-0.5f);
//        BigInteger ssMasterContactId = BigInteger.ZERO;
//        LclSsContact ssMasterShippingContact = null;
//        String ssMasterContactDetails = "";
//        if ((null != lclUnitSs && null != lclUnitSs.getLclSsHeader()) && null != lclUnitSs.getSpBookingNo()) {
//            ssMasterContactId = new LclSsHeaderDAO().getSSmasterContactId(lclUnitSs.getLclSsHeader().getId(), lclUnitSs.getSpBookingNo());
//            ssMasterShippingContact = new LclSsContactDAO().findById(ssMasterContactId.longValue());
//            ssMasterContactDetails = lclUtils.getConcatenatedSSmasterAccountDetails(ssMasterShippingContact);
//        }
//        if (CommonUtils.isNotEmpty(ssMasterContactDetails)) {
//            p = new Paragraph(11f, "" + ssMasterContactDetails.toUpperCase().trim(), totalFontQuote);
//            p.setSpacingAfter(15f);
//            p.setAlignment(Element.ALIGN_LEFT);
//        } else {
//            p = new Paragraph(8f, "", totalFontQuote);
//        }
//        shCell.addElement(p);
//        bTable.addCell(shCell);
        //consignee
        PdfPCell cvCell = new PdfPCell();
        cvCell.setBorder(0);
        cvCell.setColspan(2);
        cvCell.setPadding(0f);
        cvCell.setPaddingLeft(-0.5f);
        String consDetails = lclUtils.getConcatenatedAccountDetails(lclbl.getConsContact());
        if (CommonUtils.isNotEmpty(consDetails)) {
            p = new Paragraph(11f, "" + consDetails.toUpperCase().trim(), totalFontQuote);
            p.setSpacingAfter(10f);
            p.setAlignment(Element.ALIGN_LEFT);
        } else {
            p = new Paragraph(8f, "", totalFontQuote);
        }
        cvCell.addElement(p);
        bTable.addCell(cvCell);

        //Notify
        PdfPCell nvCell = new PdfPCell();
        nvCell.setBorder(0);
        nvCell.setColspan(2);
        nvCell.setPadding(0f);
        nvCell.setPaddingLeft(-0.5f);
        String notyDetails = lclUtils.getConcatenatedAccountDetails(lclbl.getNotyContact());
        if (CommonUtils.isNotEmpty(notyDetails)) {
            p = new Paragraph(11f, "" + notyDetails.toUpperCase().trim(), totalFontQuote);
            p.setSpacingAfter(10f);
            p.setAlignment(Element.ALIGN_LEFT);
        } else {
            p = new Paragraph(8f, "", totalFontQuote);
        }
        nvCell.addElement(p);
        bTable.addCell(nvCell);

        //place of receipt values
        PdfPCell prvCell = new PdfPCell();
        prvCell.setBorder(0);
        if (lclBooking.getPooPickup() && !lclBooking.getLclFileNumber().getLclBookingPadList().isEmpty()) {
            LclBookingPad lclBookingPad = lclBooking.getLclFileNumber().getLclBookingPadList().get(0);
            String pickUp_city = lclBookingPad.getPickUpCity().replaceAll("/", ",");
            pickUp_city = pickUp_city.substring(pickUp_city.indexOf("-") + 1, pickUp_city.length());
            p = new Paragraph(7f, "" + pickUp_city.toUpperCase(), totalFontQuote);
        } else {
            StringBuilder placeofReceipt = new StringBuilder();
            if (null != lclbl.getPortOfOrigin()) {
                placeofReceipt.append(lclbl.getPortOfOrigin().getUnLocationName());
                if (CommonFunctions.isNotNull(lclbl.getPortOfOrigin().getStateId())
                        && CommonFunctions.isNotNull(lclbl.getPortOfOrigin().getStateId().getCode())) {
                    placeofReceipt.append(",").append(lclbl.getPortOfOrigin().getStateId().getCode());
                }
            }
            p = new Paragraph(7f, "" + placeofReceipt.toString().toUpperCase(), totalFontQuote);
        }
        p.setSpacingAfter(5f);
        p.setAlignment(Element.ALIGN_LEFT);
        prvCell.addElement(p);
        bTable.addCell(prvCell);
        //pier values

        PdfPCell piervCell = new PdfPCell();
        piervCell.setBorder(0);
        p = new Paragraph(7f, "" + pierValue, totalFontQuote);
        p.setSpacingAfter(5f);
        p.setAlignment(Element.ALIGN_LEFT);
        piervCell.addElement(p);
        bTable.addCell(piervCell);

        //print carrier values
        PdfPCell exvCell = new PdfPCell();
        exvCell.setBorder(0);
        exvCell.setFixedHeight(15f);
        p = new Paragraph(7f, "" + carrierName.toString().toUpperCase(), totalFontQuote);
        p.setSpacingAfter(5f);
        p.setAlignment(Element.ALIGN_LEFT);
        exvCell.addElement(p);
        bTable.addCell(exvCell);
        //port of loading values

        PdfPCell portvCell = new PdfPCell();
        portvCell.setBorder(0);
        p = new Paragraph(7f, "" + pol.toUpperCase(), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        portvCell.addElement(p);
        bTable.addCell(portvCell);

        //sea values
        PdfPCell seavCell = new PdfPCell();
        seavCell.setBorder(0);
        seavCell.setFixedHeight(25f);
        p = new Paragraph(7f, "" + podValues.toUpperCase(), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        seavCell.addElement(p);
        bTable.addCell(seavCell);
        //fina deliver  values
        String finalDest = !fdOverride.equalsIgnoreCase("N") ? fdOverride : blUtils.getBLConcatenatedFinalDestination(lclbl);
        PdfPCell finalvCell = new PdfPCell();
        finalvCell.setBorder(0);
        p = new Paragraph(7f, !finalDest.equalsIgnoreCase(podValues) ? "" + finalDest.toUpperCase() : "", totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        finalvCell.addElement(p);
        bTable.addCell(finalvCell);

        bCell.addElement(bTable);
        table.addCell(bCell);
//2column

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        PdfPTable pTable = new PdfPTable(1);
        pTable.setWidthPercentage(100f);
        String consolidateNumber = new LclConsolidateDAO().getConsolidatesFileNumbers(lclbl.getLclFileNumber().getId().toString());
        consolidateNumber = null == consolidateNumber ? lclbl.getLclFileNumber().getFileNumber() : lclbl.getLclFileNumber().getFileNumber() + "," + consolidateNumber;

        PdfPCell eCell = new PdfPCell();
        eCell.setBorder(0);
        eCell.setPaddingLeft(10f);
        p = new Paragraph(7f, "ECUWORLDWIDE REF: " + consolidateNumber.toUpperCase(), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingAfter(35f);
        eCell.addElement(p);
        pTable.addCell(eCell);

        if (CommonUtils.isNotEmpty(lclBooking.getShipReference())) {
            PdfPCell sCell = new PdfPCell();
            sCell.setBorder(0);
            sCell.setPaddingLeft(10f);
            p = new Paragraph(7f, "SHIPPER REFERENCE: " + lclBooking.getShipReference().toUpperCase(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(10f);
            sCell.addElement(p);
            pTable.addCell(sCell);
        }

        if (CommonUtils.isNotEmpty(lclBooking.getFwdReference())) {
            PdfPCell fCell = new PdfPCell();
            fCell.setBorder(0);
            fCell.setPaddingLeft(10f);
            p = new Paragraph(7f, "FORWARDER REFERENCE: " + lclBooking.getFwdReference().toUpperCase(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(10f);
            fCell.addElement(p);
            pTable.addCell(fCell);
        }

        PdfPCell blCell = new PdfPCell();
        blCell.setBorder(0);
        blCell.setPaddingLeft(10f);
        p = new Paragraph(7f, "BILL OF LADING REFERENCE: " + lclbl.getLclFileNumber().getFileNumber().toUpperCase(), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);

        if (CommonUtils.isNotEmpty(termsType1)) {
            blCell.addElement(p);
            pTable.addCell(blCell);
            PdfPCell tlCell = new PdfPCell();
            tlCell.setBorder(0);
            tlCell.setPaddingLeft(10f);
            p = new Paragraph(11f, termsType1.replaceAll("\n", "").toUpperCase() + " BILL OF LADING", totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(10f);
            tlCell.addElement(p);
            pTable.addCell(tlCell);
        } else {
            p.setSpacingAfter(10f);
            blCell.addElement(p);
            pTable.addCell(blCell);
        }
        exportRefRemarks = exportRefRemarks
                + ("EXPORT".equalsIgnoreCase(ladenSailDateOptKey) ? ladenSailDateRemarks : "")
                + ("EXPORT".equalsIgnoreCase(printTermsTypeKey) ? termsType1 : "");
        if (exportRefRemarks != null && !exportRefRemarks.equals("")) {
            PdfPCell ehCell = new PdfPCell();
            ehCell.setBorder(0);
            ehCell.setPaddingLeft(10f);
            p = new Paragraph(7f, "EXPORT REFERENCE: ", totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            ehCell.addElement(p);
            pTable.addCell(ehCell);

            PdfPCell erCell = new PdfPCell();
            erCell.setBorder(0);
            erCell.setPaddingLeft(10f);
            p = new Paragraph(11f, exportRefRemarks, totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(10f);
            erCell.addElement(p);
            pTable.addCell(erCell);
        }
        //forwarding avlues
        PdfPCell fvCell = new PdfPCell();
        fvCell.setBorder(0);
        fvCell.setPaddingLeft(10f);
        if (CommonFunctions.isNotNull(lclbl.getFwdAcct())
                && !lclbl.getFwdAcct().getAccountName().equalsIgnoreCase("NO FRT. FORWARDER ASSIGNED")) {
            boolean forwarderAcctFlag = new LCLBlDAO().getFreightForwardAcctStatus(lclbl.getFwdAcct().getAccountno());
            String fwdDetails = lclUtils.getConcatenatedAccountDetails(lclbl.getFwdContact());
            if (CommonUtils.isNotEmpty(fwdDetails) && !forwarderAcctFlag) {
                p = new Paragraph(11f, "" + fwdDetails.toUpperCase().trim(), totalFontQuote);
                p.setSpacingAfter(10f);
                p.setAlignment(Element.ALIGN_LEFT);
            } else {
                p = new Paragraph(8f, "", totalFontQuote);
                fvCell.setPadding(25f);
            }
            fvCell.addElement(p);
            pTable.addCell(fvCell);
        } else {
            p = new Paragraph(8f, "", totalFontQuote);
            pTable.addCell(fvCell);
        }
        routingInsRemarks = routingInsRemarks
                + ("ROUTING".equalsIgnoreCase(ladenSailDateOptKey) ? ladenSailDateRemarks : "")
                + ("ROUTING".equalsIgnoreCase(printTermsTypeKey) ? termsType1 : "");
        if (routingInsRemarks != null && !routingInsRemarks.equals("")) {
            PdfPCell drihCell = new PdfPCell();
            drihCell.setBorder(0);
            drihCell.setPaddingLeft(10f);
            p = new Paragraph(7f, "DOMESTIC ROUTING/EXPORT INSTRUCTIONS: ", totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            drihCell.addElement(p);
            pTable.addCell(drihCell);

            PdfPCell driCell = new PdfPCell();
            driCell.setBorder(0);
            driCell.setPaddingLeft(10f);
            p = new Paragraph(11f, routingInsRemarks, totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(10f);
            driCell.addElement(p);
            pTable.addCell(driCell);
        }
        cell.addElement(pTable);
        table.addCell(cell);
        return table;
    }

    class PageCreation extends PdfPageEventHelper {

        PdfTemplate total;

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                PdfPTable termsTypeTable = setTermsType();
                termsTypeTable.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin());
                termsTypeTable.writeSelectedRows(0, 20, 15, 175, writer.getDirectContent());
            } catch (Exception ex) {
                log.info("Exception on class LclSSMasterFromHouseBLPdfCreator in method onEndPage" + new Date(), ex);
            }
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(100, 100);
            total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
            try {
            } catch (Exception e) {
                log.info("onOpenDocument failed on " + new Date(), e);
            }
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                lclbl = LclSSMasterFromHouseBLPdfCreator.this.lclbl;
                PdfPTable headerTable = new PdfPTable(1);
                headerTable.setWidthPercentage(100f);
                PdfPCell headerCell = new PdfPCell();
                headerCell.setBorder(0);
                headerCell.setFixedHeight(12f);
                headerCell.addElement(headerSection());
                headerTable.addCell(headerCell);

                headerCell = new PdfPCell();
                headerCell.setFixedHeight(315f);
                headerCell.setBorder(0);
                headerCell.addElement(bodySection());
                headerTable.addCell(headerCell);
                document.add(headerTable);

            } catch (Exception ex) {
                log.info("Exception on class LclSSMasterFromHouseBLPdfCreator in method onStartPage" + new Date(), ex);
            }
        }
    }

}
