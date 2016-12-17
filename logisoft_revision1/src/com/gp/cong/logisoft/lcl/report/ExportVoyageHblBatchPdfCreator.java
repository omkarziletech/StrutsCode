package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import static com.gp.cong.common.ConstantsInterface.INSURANCE_CHARGE_CODE;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.BlUtils;
import com.gp.cong.lcl.common.constant.ExportUnitQueryUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclOptions;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.List;
import org.apache.log4j.Logger;
import com.gp.cvst.logisoft.struts.form.lcl.ExportVoyageHblBatchForm;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.hibernate.dao.AgencyInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.reports.ConvertNumberToWords;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Image;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class ExportVoyageHblBatchPdfCreator extends LclReportFormatMethods implements ConstantsInterface, LclCommonConstant {

    private static final Logger log = Logger.getLogger(LclBLPdfCreator.class);
    private LclBl lclbl;
    private LclBooking lclBooking;
    private BlUtils blUtils = new BlUtils();
    private LclUtils lclUtils = new LclUtils();
    private String polValues = "";
    private String podValues = "";
    private String billingType = "";
    private String billType = "";
    private String unitNumber = "";
    private String voyageNumber = "";
    private String etaDate = "";
    private String sealOut = "";
    private String headerprintdocumentName = "";
    private String footerprintdocumentName = "";
    private String ipeHotCodeComments = "";
    private String printRadio = "";
    private String topFileId = "";
    private String bottomFileId = "";
    private String ruleName = "";
    private boolean isFirst_page = true;
    private int page_count = 0;
    private int commodity_count = 0;
    private String exportBilltoParty = "";
    private String topBilltoParty = "";
    private String sailDate = "", sailDateFormat = "", termsType1 = "";
    List<LclBlPiece> lclBlPiecesList = new ArrayList<>();
    private double total_ar_amount = 0.0;
    private double total_ar_col_amount = 0.0;
    private double total_ar_ppd_amount = 0.0;
    private String headerTypeName = "", footerTypeName = "";
    ExportVoyageHblBatchForm exportbatchForm;
    private User user;
    private String realPath;
    private final Set<String> ppdBillToSet = new HashSet<String>();
    private String ladenSailDateRemarks = "", engmet = "", arBillToParty = "";
    private String watermarkLabel = "N", fdOverride = "N", hblPolOverRideKey = "N",
            ladenSailDateOptKey = "N", printTermsTypeKey = "ROUTING", printPierPol = "Y",
            hblPierOverRideKey = "N", printHazBeforeKeyValue = "N", altAgentKey = "N",
            altAgentValue = "", ncmKeyValue = "N", aesKeyValue = "N", hsKeyValue = "N",
            arrivalDateKeyValue = "Y", miniKeyValue = "N", impKeyValue = "N", metKeyValue = "N",
            receiveKeyValue = "N", portKey = "N", portKeyValue = "", printPpdBlBothKey = "N", printBlInsuranceKey = "Y";
    private boolean documentOpenFlag = false;
    private String OCNFRT_Total = "";
    private String[] agencyInfo = new String[3];

    public void setLclBLPdfCreator(String documentName, LclBl lclbl,
            LclBooking lclBooking) throws Exception {
        if (CommonFunctions.isNotNull(lclbl)) {
            List<LclOptions> optionsList = lclbl.getLclFileNumber().getLclOptionsList();
            for (LclOptions options : optionsList) {
                if (!"ALERT".equalsIgnoreCase(options.getOptionValue())
                        && !"CORRECTEDBL".equalsIgnoreCase(options.getOptionValue())) {
                    if ("LADENSAILDATE".equalsIgnoreCase(options.getOptionValue())) {
                        ladenSailDateOptKey = options.getOptionKey();
                    }
                    if ("PROOF".equalsIgnoreCase(options.getOptionValue())) {
                        ExportVoyageHblBatchPdfCreator.this.watermarkLabel = options.getOptionKey();
                    }
                    if ("PRINTTERMSTYPE".equalsIgnoreCase(options.getOptionValue())) {
                        printTermsTypeKey = options.getOptionKey();
                    }
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
                    if ("RECEIVE".equalsIgnoreCase(options.getOptionValue())) {
                        receiveKeyValue = options.getOptionKey();
                    }
                    if ("NCM".equalsIgnoreCase(options.getOptionValue())) {
                        ncmKeyValue = options.getOptionKey();
                    }
                    if ("AES".equalsIgnoreCase(options.getOptionValue())) {
                        aesKeyValue = options.getOptionKey();
                    }
                    if ("HS".equalsIgnoreCase(options.getOptionValue())) {
                        hsKeyValue = options.getOptionKey();
                    }
                    if ("MINI".equalsIgnoreCase(options.getOptionValue())) {
                        miniKeyValue = options.getOptionKey();
                    }
                    if ("IMP".equalsIgnoreCase(options.getOptionValue())) {
                        impKeyValue = options.getOptionKey();
                    }
                    if ("MET".equalsIgnoreCase(options.getOptionValue())) {
                        metKeyValue = options.getOptionKey();
                    }
                    if ("FP".equalsIgnoreCase(options.getOptionValue())) {
                        altAgentKey = options.getOptionKey();
                    }
                    if ("FPFEILD".equalsIgnoreCase(options.getOptionValue())) {
                        altAgentValue = options.getOptionKey();
                    }
                    if ("PORT".equalsIgnoreCase(options.getOptionValue())) {
                        portKey = options.getOptionKey();
                    }
                    if ("PORTFIELD".equalsIgnoreCase(options.getOptionValue())) {
                        portKeyValue = options.getOptionKey();
                    }
                    if ("ARRIVALDATE".equalsIgnoreCase(options.getOptionValue())) {
                        arrivalDateKeyValue = options.getOptionKey();
                    }
                    if ("PRINTHAZBEFORE".equalsIgnoreCase(options.getOptionValue())) {
                        printHazBeforeKeyValue = options.getOptionKey();
                    }
                    if ("PRINTPPDBLBOTH".equalsIgnoreCase(options.getOptionValue())) {
                        printPpdBlBothKey = options.getOptionKey();
                    }
                    if ("INSURANCE".equalsIgnoreCase(options.getOptionValue())) {
                        printBlInsuranceKey = options.getOptionKey();
                    }
                }
            }
            engmet = new PortsDAO().getEngmet(lclbl.getFinalDestination() != null
                    ? lclbl.getFinalDestination().getUnLocationCode() : lclbl.getPortOfDestination().getUnLocationCode());
            String printTermsValueBodyBl = null != lclbl.getTermsType1() ? lclbl.getTermsType1() : "";
            if (!"".equalsIgnoreCase(printTermsValueBodyBl)) {
                termsType1 = "ER".equalsIgnoreCase(printTermsValueBodyBl) ? "\nExpress Release"
                        : "OR".equalsIgnoreCase(printTermsValueBodyBl) ? "\nOriginals Required"
                                : "ORD".equalsIgnoreCase(printTermsValueBodyBl) ? "\nOriginals Released At Destination"
                                        : "DER".equalsIgnoreCase(printTermsValueBodyBl) ? "\nDo Not Express Release"
                                                : "MBL".equalsIgnoreCase(printTermsValueBodyBl) ? "\nMEMO HOUSE BILL OF LADING" : "";
            }

            lclBlPiecesList = lclbl.getLclFileNumber().getLclBlPieceList();
            if (CommonFunctions.isNotNull(lclbl.getPortOfLoading())) {
                polValues = lclbl.getPortOfLoading().getUnLocationName();
            }
            if (CommonFunctions.isNotNull(lclbl.getPortOfLoading()) && CommonFunctions.isNotNull(lclbl.getPortOfLoading().getStateId())
                    && CommonFunctions.isNotNull(lclbl.getPortOfLoading().getStateId().getCode())) {
                polValues += "," + lclbl.getPortOfLoading().getStateId().getCode();
            }
        }
        Integer PodId = 0;
        String FreightAcctNo = null;
        boolean freightFlag = false;
        if (lclBooking.getBookingType().equals("T")) {
            PodId = lclbl.getLclFileNumber().getLclBookingImport().getForeignPortOfDischarge().getId();
            FreightAcctNo = lclbl.getLclFileNumber().getLclBookingImport().getExportAgentAcctNo() != null
                    ? lclbl.getLclFileNumber().getLclBookingImport().getExportAgentAcctNo().getAccountno() : null;
        } else if (lclBooking.getAgentAcct() != null) {
            PodId = lclBooking.getPortOfDestination().getId();
            FreightAcctNo = lclBooking.getAgentAcct().getAccountno();
        }
        if (CommonUtils.isNotEmpty(FreightAcctNo)) {
            agencyInfo = new AgencyInfoDAO().getAgentPickAcctNo(PodId, FreightAcctNo);
            if (agencyInfo != null && CommonUtils.isNotEmpty(agencyInfo[1])) {
                freightFlag = true;
                UnLocation unLocation = new UnLocationDAO().getUnlocation(StringUtils.substring(agencyInfo[1], agencyInfo[1].length() - 6).replace(")", ""));
                if (unLocation != null && CommonFunctions.isNotNull(unLocation.getUnLocationName())) {
                    podValues = unLocation.getUnLocationName();
                }
                if (unLocation != null && CommonFunctions.isNotNull(unLocation.getCountryId())
                        && CommonFunctions.isNotNull(unLocation.getCountryId().getCodedesc())) {
                    podValues += ", " + unLocation.getCountryId().getCodedesc();
                }
            }
        }
        if (CommonFunctions.isNotNull(lclbl.getPortOfDestination()) && !freightFlag) {
            if (CommonFunctions.isNotNull(lclbl.getPortOfDestination().getUnLocationName())) {
                podValues = lclbl.getPortOfDestination().getUnLocationName();
            }
            if (CommonFunctions.isNotNull(lclbl.getPortOfDestination().getCountryId())
                    && CommonFunctions.isNotNull(lclbl.getPortOfDestination().getCountryId().getCodedesc())) {
                podValues += ", " + lclbl.getPortOfDestination().getCountryId().getCodedesc();
            }
        }
        ruleName = lclbl.getLclFileNumber().getBusinessUnit();
        boolean ipeHotCodeFlag = new LclBookingHotCodeDAO().isHotCodeValidate(lclBooking.getFileNumberId(), "IPE");
        if (ipeHotCodeFlag) {
            Iterator bookingCommentsIterator = new GenericCodeDAO().getLclPrintComments(39, "ipeclass");
            while (bookingCommentsIterator.hasNext()) {
                Object[] row = (Object[]) bookingCommentsIterator.next();
                ipeHotCodeComments = (String) row[1];
            }
        }

    }

    class Watermark extends PdfPageEventHelper {

        Font FONT = new Font(Font.FontFamily.HELVETICA, 100f, Font.BOLD, new GrayColor(0.75f));

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                PdfPTable commityTable = addingCommodityValues();
                PdfPTable footerTable = commodityValues();
                commityTable.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin());
                commityTable.writeSelectedRows(0, 20, 15, 355, writer.getDirectContent());

                footerTable.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin());
                footerTable.writeSelectedRows(0, 20, 15, 245, writer.getDirectContent());
            } catch (Exception ex) {
                log.info("Exception on class LclBLPdfCreator in method onEndPage" + new Date(), ex);
            }
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                ExportVoyageHblBatchPdfCreator.this.lclbl = new LCLBlDAO().getByProperty("lclFileNumber.id",
                        Long.parseLong(ExportVoyageHblBatchPdfCreator.this.topFileId));
                new LCLBlDAO().getCurrentSession().evict(ExportVoyageHblBatchPdfCreator.this.lclbl);
                ExportVoyageHblBatchPdfCreator.this.lclBooking = lclbl.getLclFileNumber().getLclBooking();
                new LCLBookingDAO().getCurrentSession().evict(ExportVoyageHblBatchPdfCreator.this.lclBooking);
                setLclBLPdfCreator(headerprintdocumentName, ExportVoyageHblBatchPdfCreator.this.lclbl,
                        ExportVoyageHblBatchPdfCreator.this.lclBooking);
                lclbl = ExportVoyageHblBatchPdfCreator.this.lclbl;
                String headerDocumentName = ExportVoyageHblBatchPdfCreator.this.headerprintdocumentName;
                if (headerDocumentName.equalsIgnoreCase("Bill Of Lading")
                        && "Y".equalsIgnoreCase(ExportVoyageHblBatchPdfCreator.this.watermarkLabel)) {
                    ColumnText.showTextAligned(writer.getDirectContentUnder(),
                            Element.ALIGN_CENTER, new Phrase("PROOF", FONT),
                            document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 50);
                } else {
                    if (headerDocumentName.contains("FreightInvoice")) {
                        ColumnText.showTextAligned(writer.getDirectContentUnder(),
                                Element.ALIGN_CENTER, new Phrase("Freight Invoice", FONT),
                                document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 50);
                    } else {
                        if (lclbl.getPostedByUser() == null) {
                            ColumnText.showTextAligned(writer.getDirectContentUnder(),
                                    Element.ALIGN_CENTER, new Phrase("EDIT", FONT),
                                    document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 50);
                        }
                    }
                }
                PdfPTable headerTable = new PdfPTable(1);
                headerTable.setWidthPercentage(100f);
                PdfPCell headerCell = new PdfPCell();
                headerCell.setBorder(0);
                headerCell.setFixedHeight(10f);
                headerCell.addElement(headerSection());
                headerTable.addCell(headerCell);

                headerCell = new PdfPCell();
                headerCell.setFixedHeight(315f);
                headerCell.setBorder(0);
                headerCell.addElement(bodySection());
                headerTable.addCell(headerCell);

                headerCell = new PdfPCell();
                headerCell.setBorder(2);
                headerCell.addElement(commodityHeadingSection());
                headerTable.addCell(headerCell);
                document.add(headerTable);

            } catch (Exception ex) {
                log.info("Exception on class LclBLPdfCreator in method onStartPage" + new Date(), ex);
            }
        }
    }

    public void creareReport(String outputFileName, ExportVoyageHblBatchForm batchForm,
            Long fileId, String blType, User user, String realPath) throws Exception {
        this.exportbatchForm = batchForm;
        this.user = user;
        this.realPath = realPath;
        document = new Document(PageSize.A4, 18, 3, 30, 355);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        ExportVoyageHblBatchPdfCreator.Watermark event = new ExportVoyageHblBatchPdfCreator.Watermark();
        pdfWriter.setPageEvent(event);
        this.topFileId = fileId.toString();
        this.bottomFileId = fileId.toString();
        this.headerTypeName = blType;
        this.footerTypeName = blType;
        this.headerprintdocumentName = !blType.contains("FreightInvoice") ? "Bill Of Lading" : blType;
        this.footerprintdocumentName = !blType.contains("FreightInvoice") ? "Bill Of Lading" : blType;
        total_ar_amount = 0.0;
        total_ar_col_amount = 0.0;
        total_ar_ppd_amount = 0.0;
        this.page_count = 0;
        commodity_count = 0;
        isFirst_page = true;
        if (!blType.equalsIgnoreCase("FreightInvoice")) {
            document.open();
            for (LclBlPiece lclBlPiece : lclBlPiecesList) {
                commodity_count++;
                document.add(commodityTab(lclBlPiece));
            }
            document.close();
        } else {
            documentOpenFlag = false;
            List<Long> actualDrList = new ArrayList<>();
            actualDrList.add(fileId);
            printFrieghtInvoice(actualDrList, "FreightInvoice");
            if (documentOpenFlag) {
                document.close();
            }
        }
    }

    public String createEmailReport(String outputFileName, ExportVoyageHblBatchForm batchForm,
            User user, String realPath, String emailName) throws Exception {
        this.exportbatchForm = batchForm;
        this.user = user;
        this.realPath = realPath;
        document = new Document(PageSize.A4, 18, 3, 30, 355);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        ExportVoyageHblBatchPdfCreator.Watermark event = new ExportVoyageHblBatchPdfCreator.Watermark();
        pdfWriter.setPageEvent(event);
        List<Long> actualDrList = new ArrayList<Long>();
        if (CommonUtils.isNotEmpty(batchForm.getFileNumberId())) {
            actualDrList.add(Long.parseLong(batchForm.getFileNumberId()));
        } else {
            actualDrList = new ExportUnitQueryUtils().getAllPickedCargoBkg(Long.valueOf(batchForm.getHeaderId()), Long.valueOf(batchForm.getUnitSSId()));
        }
        documentOpenFlag = false;
        if (emailName.equalsIgnoreCase("nonNegotiable")) {
            printNonNegotiable(actualDrList, "nonNegotiable");
        } else if (emailName.equalsIgnoreCase("FreightInvoice")) {
            exportbatchForm.setBillOfLading("RATED");
            printFrieghtInvoice(actualDrList, "FreightInvoice");
        } else if (emailName.equalsIgnoreCase("FreightInvoiceCollect")) {
            exportbatchForm.setBillOfLading("RATED");
            printFrieghtInvoiceCollect(actualDrList, "FreightInvoiceCollect");
        }
        if (documentOpenFlag) {
            document.close();
        } else {
            outputFileName = null;
        }
        return outputFileName;
    }

    public void setCommonPrintCommentForEmail(Long fileId, String documentType, boolean setDocumentFlag) throws Exception {

        this.page_count = 0;
        total_ar_amount = 0.0;
        total_ar_col_amount = 0.0;
        total_ar_ppd_amount = 0.0;
        this.topFileId = fileId.toString();
        this.headerTypeName = documentType;
        this.headerprintdocumentName = !documentType.contains("FreightInvoice") ? "Bill Of Lading" : documentType;

        if (!documentOpenFlag) {
            document.open();
            documentOpenFlag = true;
            this.bottomFileId = fileId.toString();
            this.footerTypeName = documentType;
            this.footerprintdocumentName = !documentType.contains("FreightInvoice") ? "Bill Of Lading" : documentType;

        } else {
            document.newPage();
            this.bottomFileId = fileId.toString();
            this.footerTypeName = documentType;
            this.footerprintdocumentName = !documentType.contains("FreightInvoice") ? "Bill Of Lading" : documentType;
        }
        commodity_count = 0;
        isFirst_page = true;
        if (documentOpenFlag) {
            for (LclBlPiece lclBlPiece : lclBlPiecesList) {
                commodity_count++;
                document.add(commodityTab(lclBlPiece));
            }
        }
    }

    public void printFrieghtInvoice(List<Long> actualDrList, String doumentType) throws Exception {
        for (int i = 0; i < actualDrList.size(); i++) {
            String billToParties = new ExportUnitQueryUtils().getPickedDrBillToParty(actualDrList.get(i));
            if (CommonUtils.isNotEmpty(billToParties)) {
                for (String billtoParty : billToParties.split(",")) {
                    billtoParty = billtoParty.equalsIgnoreCase("") ? "F" : billtoParty;
                    this.topBilltoParty = billtoParty;
                    this.setCommonPrintCommentForEmail(actualDrList.get(i), doumentType, i == 0);
                    this.exportBilltoParty = billtoParty;
                }
            }
        }
    }

    public void printFrieghtInvoiceCollect(List<Long> actualDrList, String doumentType) throws Exception {
        Iterator drList = actualDrList.iterator();
        boolean containCollectCharge = true;
        while (drList.hasNext()) {
            containCollectCharge = new ExportUnitQueryUtils().isDrContainCollectCharge((Long) drList.next());
            if (!containCollectCharge) {
                drList.remove();
            }
        }
        if (CommonUtils.isNotEmpty(actualDrList)) {
            for (int i = 0; i < actualDrList.size(); i++) {
                this.setCommonPrintCommentForEmail(actualDrList.get(i), doumentType, i == 0);
            }
        }
    }

    public void printNonNegotiable(List<Long> actualDrList, String doumentType) throws Exception {
        for (int i = 0; i < actualDrList.size(); i++) {
            this.setCommonPrintCommentForEmail(actualDrList.get(i), doumentType, i == 0);
        }
    }

    public PdfPTable headerSection() throws Exception {
        Paragraph p = null;
        String companyName = "";
        Font blackArialFont20 = FontFactory.getFont("Arial", 20f, Font.NORMAL);
        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        String heading_Alert = headerTypeName.equalsIgnoreCase("original") ? "ORIGINAL"
                : headerTypeName.equalsIgnoreCase("nonNegotiable") ? "NON-NEGOTIABLE"
                        : headerTypeName.equalsIgnoreCase("signedNonNegotiable") ? "SIGNED NON-NEGOTIABLE"
                                : headerTypeName.equalsIgnoreCase("unsignedOriginal") ? "ORIGINAL" : "";

        companyName = LoadLogisoftProperties.getProperty(ruleName.equalsIgnoreCase("ECU")
                ? "application.ECU.companyname" : ruleName.equalsIgnoreCase("ECI")
                        ? "application.Econo.companyname" : ruleName.equalsIgnoreCase("OTI")
                                ? "application.OTI.companyname" : "application.Econo.companyname");
        p = new Paragraph(2f, companyName.toUpperCase(), blackArialFont20);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(2f, heading_Alert, blackBoldFont14);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(2f, headerprintdocumentName.equalsIgnoreCase("Bill Of Lading") ? "BILL OF LADING" : "FREIGHT INVOICE", blackArialFont20);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);
        //empty space
        table.addCell(createEmptyCell(0, 1f, 3));
        return table;
    }

    public PdfPTable bodySection() throws Exception {
        String exportRefRemarks = "";
        String routingInsRemarks = "";
        String InsStatement = "";
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
        String bill_type = lclbl.getBillingType();
        billingType = bill_type.equalsIgnoreCase("P") ? "** FREIGHT PREPAID **"
                : bill_type.equalsIgnoreCase("C") ? "** FREIGHT COLLECT **" : bill_type.equalsIgnoreCase("B") && "Y".equalsIgnoreCase(printPpdBlBothKey)
                                ? "** FREIGHT PREPAID **" : "** FREIGHT COLLECT **";
        billType = bill_type.equalsIgnoreCase("P") ? "PPD" : bill_type.equalsIgnoreCase("C") ? "COL" : "BOTH";
        exportBilltoParty = headerprintdocumentName.equalsIgnoreCase("FreightInvoice")
                ? this.topBilltoParty : headerprintdocumentName.equalsIgnoreCase("FreightInvoiceCollect")
                        ? "A" : lclbl.getBillToParty();
        if (headerprintdocumentName.contains("FreightInvoice")) {
            billingType = "";
            String billToAcctName = "";
            if (CommonFunctions.isNotNull(exportBilltoParty)) {
                if (exportBilltoParty.equalsIgnoreCase("S") && lclbl.getShipAcct() != null) {
                    billToAcctName = lclbl.getShipAcct().getAccountName();
                } else if (exportBilltoParty.equalsIgnoreCase("F") && lclbl.getFwdAcct() != null) {
                    billToAcctName = lclbl.getFwdAcct().getAccountName();
                } else if (exportBilltoParty.equalsIgnoreCase("A") && lclbl.getAgentAcct() != null) {
                    if (lclBooking.getAgentAcct() != null) {
                        billToAcctName = lclBooking.getAgentAcct().getAccountName();
                    } else {
                        billToAcctName = lclbl.getAgentAcct().getAccountName();
                    }
                } else if (exportBilltoParty.equalsIgnoreCase("T") && lclbl.getThirdPartyAcct() != null) {
                    billToAcctName = lclbl.getThirdPartyAcct().getAccountName();
                }
                if (exportBilltoParty.equalsIgnoreCase("F") && lclbl.getFwdAcct() != null) {//NOT valid forward account
                    boolean isAcct = new LCLBlDAO().getFreightForwardAcctStatus(lclbl.getFwdAcct().getAccountno());
                    if (isAcct) {
                        billToAcctName = "";
                    }
                }
                billingType = bill_type.equalsIgnoreCase("P") ? "** PREPAID Freight Payable by: **"
                        : bill_type.equalsIgnoreCase("C") ? "** COLLECT Freight Payable by: **" : "** COLLECT Freight Payable by:**";
                billingType = billingType + " " + billToAcctName;
            }
        }
        ExportVoyageSearchModel pickedDetails = new LclUnitSsDAO().getPickedVoyageByVessel(lclBooking.getFileNumberId(), "E");
        LclSsDetail bookedOrPickedVoy = null;
        if (pickedDetails != null && CommonUtils.isNotEmpty(pickedDetails.getUnitSsId())) {
            LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(Long.parseLong(pickedDetails.getUnitSsId()));
            bookedOrPickedVoy = lclUnitSs.getLclSsHeader().getVesselSsDetail();
            unitNumber = lclUnitSs.getLclUnit().getUnitNo();
            voyageNumber = lclUnitSs.getLclSsHeader().getScheduleNo();
            if (lclUnitSs.getCobDatetime() != null && lclUnitSs.getCob()) {
                String today = DateUtils.formatDate(new Date(), "dd-MMM-yyyy HH:mm:ss");
                String past_today = DateUtils.formatDate(lclUnitSs.getCobDatetime(), "dd-MMM-yyyy HH:mm:ss");
                Date confirm_on_Date = today.compareTo(past_today) >= 0 ? lclUnitSs.getCobDatetime() : null;
                etaDate = null != confirm_on_Date ? DateUtils.formatDate(confirm_on_Date, "dd-MMM-yyyy HH:mm:ss") : "";
            }
            sealOut = CommonUtils.isNotEmpty(lclUnitSs.getSUHeadingNote()) ? lclUnitSs.getSUHeadingNote().toUpperCase() : "";
        }
        StringBuilder carrierName = new StringBuilder();
        StringBuilder pierValues = new StringBuilder();
        if (bookedOrPickedVoy != null) {
            sailDate = DateUtils.formatStringDateToAppFormatMMM(bookedOrPickedVoy.getStd());
            sailDateFormat = DateUtils.formatDateToMMMMDDYYYY(bookedOrPickedVoy.getStd());
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
        /* POL Logic */
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
        Boolean checkBlInsurance = new LclBlAcDAO().checkBlInsurance(lclbl.getFileNumberId(), INSURANCE_CHARGE_CODE);
        if (checkBlInsurance && "Y".equalsIgnoreCase(printBlInsuranceKey)) {
            InsStatement = LoadLogisoftProperties.getProperty("InsuranceChargeComment");
        }
        Paragraph p = null;
        table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{5.3f, 4.7f});
        PdfPCell bCell = null;

        bCell = new PdfPCell();
        bCell.setBorder(0);
        bCell.setPadding(0f);
        bCell.setBorderWidthBottom(0.06f);
        bCell.setBorderWidthTop(0.06f);
        bCell.setBorderWidthRight(0.06f);
        PdfPTable bTable = new PdfPTable(2);
        bTable.setWidths(new float[]{5f, 4f});
        bTable.setWidthPercentage(100f);

        bTable.addCell(makeCellLeftTopNoBorderFont("SHIPPER/EXPORTER", -0.5f, 0.8f, blackBoldFont65));

        PdfPCell shCell = new PdfPCell();
        shCell.setBorder(0);
        shCell.setColspan(2);
        shCell.setPadding(0f);
        shCell.setPaddingLeft(4f);
        shCell.setBorderWidthBottom(0.06f);
        shCell.setFixedHeight(65f);
        String shipperDetails = lclUtils.getConcatenatedAccountDetails(lclbl.getShipContact());
        if (CommonUtils.isNotEmpty(shipperDetails)) {
            p = new Paragraph(11f, "" + shipperDetails.toUpperCase(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            p.setSpacingAfter(15f);
        } else {
            p = new Paragraph(8f, "", totalFontQuote);
            shCell.setPadding(35f);
        }
        shCell.addElement(p);
        bTable.addCell(shCell);

        //consignee
        bTable.addCell(makeCellLeftTopNoBorderFont("CONSIGNEE", -0.5f, 0.8f, blackBoldFont65));
        //consignee Values
        PdfPCell cvCell = new PdfPCell();
        cvCell.setBorder(0);
        cvCell.setColspan(2);
        cvCell.setPadding(0f);
        cvCell.setPaddingLeft(4f);
        cvCell.setBorderWidthBottom(0.06f);
        cvCell.setFixedHeight(65f);
        String consDetails = lclUtils.getConcatenatedAccountDetails(lclbl.getConsContact());
        if (CommonUtils.isNotEmpty(consDetails)) {
            p = new Paragraph(11f, "" + consDetails.toUpperCase(), totalFontQuote);
            p.setSpacingAfter(15f);
            p.setAlignment(Element.ALIGN_LEFT);
        } else {
            p = new Paragraph(8f, "", totalFontQuote);
            cvCell.setPadding(30f);
        }
        cvCell.addElement(p);
        bTable.addCell(cvCell);

        //Notify
        bTable.addCell(makeCellLeftTopNoBorderFont("NOTIFY PARTY", -0.5f, 0.8f, blackBoldFont65));
        //consignee Values
        PdfPCell nvCell = new PdfPCell();
        nvCell.setBorder(0);
        nvCell.setColspan(2);
        nvCell.setPadding(0f);
        nvCell.setPaddingLeft(4f);
        nvCell.setBorderWidthBottom(0.06f);
        nvCell.setFixedHeight(70f);
        String notyDetails = lclUtils.getConcatenatedAccountDetails(lclbl.getNotyContact());
        if (CommonUtils.isNotEmpty(notyDetails)) {
            p = new Paragraph(11f, "" + notyDetails.toUpperCase(), totalFontQuote);
            p.setSpacingAfter(25f);
            p.setAlignment(Element.ALIGN_LEFT);
        } else {
            p = new Paragraph(8f, "", totalFontQuote);
            nvCell.setPadding(40f);
        }
        nvCell.addElement(p);
        bTable.addCell(nvCell);

        //place of receipt
        PdfPCell prCell = new PdfPCell();
        prCell.setBorder(0);
        prCell.setPaddingLeft(-0.9f);
        prCell.setPaddingTop(-0.2f);
        p = new Paragraph(7f, "PLACE OF RECEIPT", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        prCell.addElement(p);
        bTable.addCell(prCell);

        PdfPCell pierCell = new PdfPCell();
        pierCell.setBorder(0);
        pierCell.setBorderWidthLeft(0.06f);
        pierCell.setPaddingTop(-0.2f);
        p = new Paragraph(7f, "PIER", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        pierCell.addElement(p);
        bTable.addCell(pierCell);
        //place of receipt values
        PdfPCell prvCell = new PdfPCell();
        prvCell.setBorder(0);
        prvCell.setBorderWidthBottom(0.06f);
        if (lclBooking.getPooPickup()) {
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
        p.setSpacingBefore(5f);
        p.setAlignment(Element.ALIGN_LEFT);
        prvCell.addElement(p);
        bTable.addCell(prvCell);
        //pier values

        PdfPCell piervCell = new PdfPCell();
        piervCell.setBorder(0);
        piervCell.setBorderWidthLeft(0.06f);
        piervCell.setBorderWidthBottom(0.06f);
        p = new Paragraph(7f, "" + pierValue, totalFontQuote);
        p.setSpacingAfter(5f);
        p.setSpacingBefore(5f);
        p.setAlignment(Element.ALIGN_LEFT);
        piervCell.addElement(p);
        bTable.addCell(piervCell);

        //exporting carrier
        PdfPCell ecCell = new PdfPCell();
        ecCell.setBorder(0);
        ecCell.setPaddingLeft(-0.5f);
        ecCell.setPaddingTop(-0.2f);
        p = new Paragraph(7f, "EXPORTING CARRIER (Vessel) (Flag)", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        ecCell.addElement(p);
        bTable.addCell(ecCell);
        //portof loading
        PdfPCell portCell = new PdfPCell();
        portCell.setBorder(0);
        portCell.setPaddingTop(-0.2f);
        portCell.setBorderWidthLeft(0.06f);
        p = new Paragraph(7f, "PORT OF LOADING", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        portCell.addElement(p);
        bTable.addCell(portCell);
        //exporting values
        PdfPCell exvCell = new PdfPCell();
        exvCell.setBorder(0);
        exvCell.setFixedHeight(15f);
        exvCell.setBorderWidthBottom(0.06f);
        p = new Paragraph(7f, "" + carrierName.toString().toUpperCase(), totalFontQuote);
        p.setSpacingAfter(5f);
        p.setSpacingBefore(5f);
        p.setAlignment(Element.ALIGN_LEFT);
        exvCell.addElement(p);
        bTable.addCell(exvCell);
        //port of loading values

        PdfPCell portvCell = new PdfPCell();
        portvCell.setBorder(0);
        portvCell.setBorderWidthLeft(0.06f);
        portvCell.setBorderWidthBottom(0.06f);
        p = new Paragraph(7f, "" + pol.toUpperCase(), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        portvCell.addElement(p);
        bTable.addCell(portvCell);
        //sea port of discharge
        PdfPCell seaCell = new PdfPCell();
        seaCell.setBorder(0);
        seaCell.setPaddingLeft(-0.5f);
        seaCell.setPaddingTop(-0.2f);
        p = new Paragraph(7f, "SEA PORT OF DISCHARGE", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        seaCell.addElement(p);
        bTable.addCell(seaCell);
        //finalde
        PdfPCell finalCell = new PdfPCell();
        finalCell.setBorder(0);
        finalCell.setBorderWidthLeft(0.06f);
        finalCell.setPaddingTop(-0.2f);
        p = new Paragraph(7f, "FINAL DELIVERY TO", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        finalCell.addElement(p);
        bTable.addCell(finalCell);
        //sea values
        PdfPCell seavCell = new PdfPCell();
        seavCell.setBorder(0);
        seavCell.setFixedHeight(25f);
        //   seavCell.setBorderWidthBottom(0.06f);
        p = new Paragraph(7f, "" + podValues.toUpperCase(), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        seavCell.addElement(p);
        bTable.addCell(seavCell);
        //fina deliver  values

        String finalDest = !fdOverride.equalsIgnoreCase("N") ? fdOverride
                : (agencyInfo != null && CommonUtils.isNotEmpty(agencyInfo[2])) ? agencyInfo[2] : blUtils.getBLConcatenatedFinalDestination(lclbl);
        PdfPCell finalvCell = new PdfPCell();
        finalvCell.setBorder(0);
        finalvCell.setBorderWidthLeft(0.06f);
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
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        PdfPTable pTable = new PdfPTable(2);
        pTable.setWidths(new float[]{4f, 2f});
        pTable.setWidthPercentage(100f);
        PdfPCell nCell = null;

        nCell = new PdfPCell();
        nCell.setBorder(0);
        nCell.setPaddingTop(0.2f);
        p = new Paragraph(7f, "DOCUMENT NO", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        nCell.addElement(p);
        pTable.addCell(nCell);

        nCell = new PdfPCell();
        nCell.setBorder(0);
        nCell.setPaddingTop(0.2f);
        p = new Paragraph(7f, "PAGE : " + ++page_count, blackBoldFont65);
        p.setAlignment(Element.ALIGN_RIGHT);
        nCell.addElement(p);
        pTable.addCell(nCell);

        String consolidateNumber = new LclConsolidateDAO().getConsolidatesFileNumbers(lclbl.getLclFileNumber().getId().toString());
        consolidateNumber = null == consolidateNumber ? lclbl.getLclFileNumber().getFileNumber()
                : lclbl.getLclFileNumber().getFileNumber() + "," + consolidateNumber;

        PdfPCell dvCell = new PdfPCell();
        dvCell.setBorder(0);
        dvCell.setColspan(2);
        dvCell.setBorderWidthBottom(0.06f);
        dvCell.setPaddingLeft(8f);
        dvCell.setPaddingBottom(5f);
        dvCell.setPaddingTop(2f);
        p = new Paragraph(8f, "" + consolidateNumber.toUpperCase(), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        dvCell.addElement(p);
        pTable.addCell(dvCell);

        PdfPCell eCell = new PdfPCell();
        eCell.setBorder(0);
        eCell.setPaddingTop(0.2f);
        eCell.setColspan(2);
        p = new Paragraph(7f, "EXPORT REFERENCE", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        eCell.addElement(p);
        pTable.addCell(eCell);

        PdfPCell evCell = new PdfPCell();
        evCell.setBorder(0);
        evCell.setColspan(2);
        evCell.setBorderWidthBottom(0.06f);
        evCell.setPaddingLeft(8f);
        evCell.setFixedHeight(35f);
        exportRefRemarks = exportRefRemarks
                + ("EXPORT".equalsIgnoreCase(ladenSailDateOptKey) ? ladenSailDateRemarks : "")
                + ("EXPORT".equalsIgnoreCase(printTermsTypeKey) ? termsType1 : "");
        if (exportRefRemarks != null && !exportRefRemarks.equals("")) {
            p = new Paragraph(10f, "" + exportRefRemarks, totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
        } else {
            p = new Paragraph(8f, "", contentBLNormalFont);
        }
        evCell.addElement(p);
        pTable.addCell(evCell);

        PdfPCell fCell = new PdfPCell();
        fCell.setBorder(0);
        fCell.setColspan(2);
        fCell.setPaddingTop(0.2f);
        p = new Paragraph(7f, "FORWARDING AGENT-REFERENCES", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        fCell.addElement(p);
        pTable.addCell(fCell);
        //forwarding avlues
        PdfPCell fvCell = new PdfPCell();
        fvCell.setBorder(0);
        fvCell.setColspan(2);
        fvCell.setPaddingLeft(3f);
        fvCell.setPaddingTop(2f);
        fvCell.setBorderWidthBottom(0.06f);
        fvCell.setFixedHeight(65f);
        if (CommonFunctions.isNotNull(lclbl.getFwdAcct())
                && !lclbl.getFwdAcct().getAccountName().equalsIgnoreCase("NO FRT. FORWARDER ASSIGNED")) {
            boolean forwarderAcctFlag = new LCLBlDAO().getFreightForwardAcctStatus(lclbl.getFwdAcct().getAccountno());
            String fwdDetails = lclUtils.getConcatenatedAccountDetails(lclbl.getFwdContact());
            if (CommonUtils.isNotEmpty(fwdDetails) && !forwarderAcctFlag) {
                p = new Paragraph(11f, "" + fwdDetails.toUpperCase(), totalFontQuote);
                p.setSpacingAfter(15f);
                p.setAlignment(Element.ALIGN_LEFT);
            } else {
                p = new Paragraph(8f, "", totalFontQuote);
                fvCell.setPadding(25f);
            }
            fvCell.addElement(p);
            pTable.addCell(fvCell);
        } else {
            p = new Paragraph(8f, "", totalFontQuote);
            fvCell.setBorderWidthBottom(0.06f);
            pTable.addCell(fvCell);
        }

        //pointoforigin
        PdfPCell pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setColspan(2);
        pCell.setPaddingTop(0.2f);
        p = new Paragraph(7f, "POINT AND COUNTRY OF ORIGIN", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        pCell.addElement(p);
        pTable.addCell(pCell);
        //point avlues
        PdfPCell pvCell = new PdfPCell();
        pvCell.setBorder(0);
        pvCell.setColspan(2);
        pvCell.setPaddingLeft(8f);
        pvCell.setPaddingBottom(5f);
        pvCell.setPaddingTop(2f);
        pvCell.setFixedHeight(20f);
        pvCell.setBorderWidthBottom(0.06f);
        if (lclbl.getPointOfOrigin() != null) {
            p = new Paragraph(9f, "" + lclbl.getPointOfOrigin(), totalFontQuote);
            pvCell.addElement(p);
        }
        p.setAlignment(Element.ALIGN_LEFT);
        pTable.addCell(pvCell);

        //domes
        PdfPCell doCell = new PdfPCell();
        doCell.setBorder(0);
        doCell.setColspan(2);
        doCell.setPaddingTop(0.2f);
        p = new Paragraph(7f, "DOMESTIC ROUTING/EXPORT INSTRUCTIONS", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        doCell.addElement(p);
        pTable.addCell(doCell);
        //point avlues
        PdfPCell dovCell = new PdfPCell();
        dovCell.setBorder(0);
        dovCell.setColspan(2);
        dovCell.setPaddingLeft(8f);
        dovCell.setBorderWidthBottom(0.06f);
        dovCell.setFixedHeight(45f);
        routingInsRemarks = routingInsRemarks
                + ("ROUTING".equalsIgnoreCase(ladenSailDateOptKey) ? ladenSailDateRemarks : "")
                + ("ROUTING".equalsIgnoreCase(printTermsTypeKey) ? termsType1 : "");
        if (routingInsRemarks != null && !"".equalsIgnoreCase(routingInsRemarks)) {
            p = new Paragraph(10f, "" + routingInsRemarks, totalFontQuote);
            p.setSpacingAfter(10f);
            p.setAlignment(Element.ALIGN_LEFT);
        } else {
            p = new Paragraph(8f, "", contentBLNormalFont);
            dovCell.setPadding(15f);
        }
        dovCell.addElement(p);
        pTable.addCell(dovCell);

        //addito
        PdfPCell adCell = new PdfPCell();
        adCell.setBorder(0);
        adCell.setColspan(2);
        adCell.setPaddingTop(0.2f);
        p = new Paragraph(7f, "ADDITIONAL DOCUMENT NUMBERS", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        adCell.addElement(p);
        pTable.addCell(adCell);
        //add values
        PdfPCell advCell = new PdfPCell();
        advCell.setBorder(0);
        advCell.setColspan(2);
        advCell.setPadding(10f);
        p = new Paragraph(7f, InsStatement, totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        advCell.addElement(p);
        pTable.addCell(advCell);
        cell.addElement(pTable);
        table.addCell(cell);
        return table;
    }

    public PdfPTable commodityHeadingSection() throws DocumentException, Exception {
        Paragraph p = null;
        cell = null;
        table = new PdfPTable(6);
        table.setWidthPercentage(101f);
        table.setWidths(new float[]{0f, 2f, 0.74f, 4.72f, 1.048f, 1.048f});

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(6);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(2f, "PARTICULARS FURNISHED BY SHIPPER", blackBoldFont65);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        //marks
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(6f, "MARKS AND NUMBERS", blackBoldFont65);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //no of pkgs
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(6f, "NO.OF.PKGS", blackBoldFont65);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //desc
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(6f, "DESCRIPTION OF PACKAGES AND GOODS", blackBoldFont65);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //grossweight
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(6f, "GROSS WEIGHT", blackBoldFont65);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //measure
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(6f, "MEASURE", blackBoldFont65);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable addingCommodityValues() throws DocumentException, ParseException, Exception {
        Font font6 = new Font(Font.FontFamily.COURIER, 6f, Font.BOLD);
        Font blackContentNormalFont8 = FontFactory.getFont("Arial", 8f, Font.NORMAL);
        Paragraph p = null;

        table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2.74f, 1f, 6.33f, 1.40f, 1.40f});

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, " " + ipeHotCodeComments, blackContentNormalFont8);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setSpacingAfter(2f);
        cell.addElement(p);
        p = new Paragraph(7f, "" + billingType, totalFontQuote);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(2f);
        cell.addElement(p);
        if ("Y".equalsIgnoreCase(receiveKeyValue)) {
            p = new Paragraph(7f, "*** PLEASE NOTE THAT PAYMENT MUST BE RECEIVED PRIOR TO  ISSUING EXPRESS RELEASE / ORIGINAL HBL ***",
                    blackContentNormalFont8);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
        }
        p = new Paragraph(7f, "I HEREBY DECLARE THAT THE ABOVE NAMED MATERIALS ARE PROPERLY CLASSIFIED," + "\n"
                + "DESCRIBED,PACKAGED, MARKED, AND LABELED, AND ARE IN PROPER CONDITION FOR" + "\n"
                + "TRANSPORTATION ACCORDING TO THE APPLICABLE REGULATIONS OF THE DEPARTMENT" + "\n"
                + " OF TRANSPORTATION AND IMO.", font6);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        cell.setBorderWidthLeft(0.6f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(5);
        p = new Paragraph(7f, " Declared value per Package if value is More Than $500 Per Package USD___________________________",
                blackContentNormalFont8);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(5f);
        cell.addElement(p);

        p = new Paragraph(7f, "These Commodities, Technology Or Software Were Exported From the United States in Accordance with the Export "
                + "Administration Regulations.", blackContentNormalFont8);
        p.setSpacingBefore(5f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);

        p = new Paragraph(7f, "Diversion Contrary To U.S. Law Prohibited.", blackContentNormalFont8);
        p.setSpacingBefore(3f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable commodityValues() throws DocumentException, ParseException, Exception {
        Paragraph p = null;
        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{3f, 2f, 3.2f});
        cell = new PdfPCell();
        cell.setBorder(2);
        cell.setColspan(2);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0f);
        if (isFirst_page) {
            cell.addElement(appendChargesAndCommodity());
            isFirst_page = false;
        } else {
            cell.setFixedHeight(100f);
        }
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.6f);
        cell.addElement(appendDescComments());
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        if (footerprintdocumentName.contains("Bill Of Lading")) {
            String blFileNumber = new LCLBlDAO().getExportBlNumbering(this.bottomFileId);
            p = new Paragraph(16f, "B/L# " + blFileNumber, blackNormalFont13);
        } else {
            p = new Paragraph(16f, "INVOICE#  " + lclbl.getLclFileNumber().getFileNumber().toUpperCase(), blackNormalFont13);
        }
        cell.setPaddingTop(-6f);
        cell.addElement(p);
        table.addCell(cell);
        String companyLogo = LoadLogisoftProperties.getProperty("ECU".equalsIgnoreCase(ruleName) ? "application.image.logo"
                : "application.image.econo.logo");
        cell = new PdfPCell();
        cell.setBorder(0);
        Image img = Image.getInstance(realPath + companyLogo);
        img.scalePercent(10);
        img.scaleAbsoluteWidth(80f);
        img.scaleAbsoluteHeight(13f);
        cell.addElement(img);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

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
        if ("N".equalsIgnoreCase(metKeyValue) && lclBlPiece.getActualWeightMetric() != null) {
            AWMet = NumberUtils.convertToThreeDecimalhash(lclBlPiece.getActualWeightMetric().doubleValue());
        }
        if ("N".equalsIgnoreCase(metKeyValue) && lclBlPiece.getActualVolumeMetric() != null) {
            AVMet = NumberUtils.convertToThreeDecimalhash(lclBlPiece.getActualVolumeMetric().doubleValue());
        }
        Paragraph p = null;
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);

        PdfPTable inner_Table = new PdfPTable(1);
        inner_Table.setWidthPercentage(100f);
        PdfPCell inner_Cell = null;

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        inner_Cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(6f, iscaribben ? "LBS" : "KGS", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        p = new Paragraph(7f, iscaribben ? AWImp : AWMet, totalFontQuote);
        p.setAlignment(Element.ALIGN_CENTER);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        inner_Cell.setPaddingBottom(18f);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        if ("N".equalsIgnoreCase(metKeyValue)) {
            inner_Cell.setBorderWidthBottom(0.6f);
            p = new Paragraph(6f, iscaribben ? "KGS" : "LBS", blackBoldFont65);
        } else {
            AWMet = "";
            AWImp = "";
            p = new Paragraph(6f, "", blackBoldFont65);
        }
        p.setAlignment(Element.ALIGN_LEFT);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        p = new Paragraph(7f, iscaribben ? AWMet : AWImp, totalFontQuote);
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
        cell.setBorderWidthLeft(0.6f);
        inner_Table = new PdfPTable(1);
        inner_Table.setWidthPercentage(100f);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        inner_Cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(6f, iscaribben ? "CU.FT." : "CBM", blackBoldFont65);
        p.setAlignment(Element.ALIGN_LEFT);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        p = new Paragraph(7f, iscaribben ? AVImp : AVMet, totalFontQuote);
        p.setAlignment(Element.ALIGN_CENTER);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        inner_Cell.setPaddingBottom(18f);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        if ("N".equalsIgnoreCase(metKeyValue)) {
            inner_Cell.setBorderWidthBottom(0.6f);
            p = new Paragraph(6f, iscaribben ? "CBM" : "CU.FT.", blackBoldFont65);
        } else {
            AVMet = "";
            AVImp = "";
            p = new Paragraph(6f, "", blackBoldFont65);
        }
        p.setAlignment(Element.ALIGN_LEFT);
        inner_Cell.addElement(p);
        inner_Table.addCell(inner_Cell);

        inner_Cell = new PdfPCell();
        inner_Cell.setBorder(0);
        p = new Paragraph(7f, iscaribben ? AVMet : AVImp, totalFontQuote);
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

    public PdfPTable commodityTab(LclBlPiece lclBlPiece) throws DocumentException, Exception {
        LclReportUtils reportUtils = new LclReportUtils();
        LclConsolidateDAO consolidateDAO = new LclConsolidateDAO();
        List<Long> conoslidatelist = consolidateDAO.getConsolidatesFiles(lclbl.getFileNumberId());
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
        Paragraph p = null;
        table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 0.75f, 4.74f, 1.047f, 1.047f});
        LclHazmatDAO hazmatDAO = new LclHazmatDAO();
        StringBuilder hazmatValues = new StringBuilder();
        if (commodity_count == 1) {
            hazmatValues.append(reportUtils.appendHazmatForFreightInvoice(conoslidatelist, hazmatDAO));//Hazmat List
        }
        cell = new PdfPCell();
        cell.setBorder(0);
        if (lclBlPiece.getMarkNoDesc() != null && !lclBlPiece.getMarkNoDesc().equals("")) {
            p = new Paragraph(7f, "" + lclBlPiece.getMarkNoDesc().toUpperCase(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
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
            cell.addElement(p);
        }
        if ("N".equalsIgnoreCase(aesKeyValue) && CommonUtils.isNotEmpty(aesAppend)) {
            p = new Paragraph(7f, "" + aesAppend.toString(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
        }
        if ("N".equalsIgnoreCase(hsKeyValue) && CommonUtils.isNotEmpty(hsAppend)) {
            p = new Paragraph(7f, "" + hsAppend.toString(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
        }
        if ("MARKS".equalsIgnoreCase(printTermsTypeKey)) {
            p = new Paragraph(7f, "" + termsType1, totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
        }
        table.addCell(cell);

        //no of pkgs
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        if (headerprintdocumentName.equalsIgnoreCase("Bill Of Lading") && "Y".equalsIgnoreCase(miniKeyValue)) {
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
                cell.addElement(p);
            }
        }
        table.addCell(cell);

        //desc
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
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
        p = new Paragraph(6.7f, "" + commodityDesc
                + ("BODYBL".equalsIgnoreCase(ladenSailDateOptKey) ? ladenSailDateRemarks : "")
                + ("BODYBL".equalsIgnoreCase(printTermsTypeKey) ? termsType1 : ""), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);

        if (CommonUtils.isNotEmpty(hazmatValues) && "N".equalsIgnoreCase(printHazBeforeKeyValue)) {
            String hazmat = hazmatValues.toString().replace("WEIGHT", "WT");
            p = new Paragraph(7f, "" + hazmat.toUpperCase(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
        }
        if ("Y".equalsIgnoreCase(ncmKeyValue) && CommonUtils.isNotEmpty(ncmAppend)) {
            p = new Paragraph(7f, "" + ncmAppend.toString(), totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
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
            cell.addElement(p);
        }
        table.addCell(cell);

        setCaribbenAndNonCaribben(lclBlPiece, impKeyValue, metKeyValue, engmet.equalsIgnoreCase("M"));
        if (commodity_count == lclBlPiecesList.size()) {
            table.setExtendLastRow(true);
        }
        return table;
    }

    public PdfPTable appendChargesAndCommodity() throws Exception {
        LclBlAcDAO lclBlAcDAO = new LclBlAcDAO();
        lclBlPiecesList = lclbl.getLclFileNumber().getLclBlPieceList();
        List<LclBlAc> chargeList = lclBlAcDAO.getLclCostByFileNumberAsc(lclbl.getFileNumberId());
        PdfPTable chargeTable = new PdfPTable(6);
        PdfPCell chargeCell = null;
        chargeTable.setWidths(new float[]{3.8f, 1.5f, .8f, 3.8f, 1.5f, .8f});
        chargeTable.setWidthPercentage(100f);
        Paragraph p = null;
        boolean flag = true;
        List<LinkedHashMap<String, PdfPCell>> listChargeMap = null;
        LinkedHashMap<String, PdfPCell> chargeMap = null;
        this.total_ar_amount = 0.00;
        this.total_ar_col_amount = 0.0;
        this.total_ar_ppd_amount = 0.0;
        if ("BOTH".equalsIgnoreCase(billType)) {
            listChargeMap = !exportbatchForm.getBillOfLading().equalsIgnoreCase("UNRATED")
                    ? this.getTotalChargesList(chargeList, lclBlPiecesList) : null;
        } else {
            chargeMap = !exportbatchForm.getBillOfLading().equalsIgnoreCase("UNRATED")
                    ? this.getTotalCharges(chargeList, lclBlPiecesList) : null;
        }

        LclBlAc blAC = lclBlAcDAO.manualChargeValidate(lclbl.getFileNumberId(), "OCNFRT", false);
        if (lclBlPiecesList != null && lclBlPiecesList.size() > 0 && blAC != null
                && !exportbatchForm.getBillOfLading().equalsIgnoreCase("UNRATED") && flag) {
            BigDecimal CFT = BigDecimal.ZERO, LBS = BigDecimal.ZERO;
            LclBlPiece lclBlPiece = (LclBlPiece) lclBlPiecesList.get(0);
            if (blAC.getRatePerUnitUom() != null) {
                CFT = blAC.getRatePerUnitUom().equalsIgnoreCase("FRV") ? blAC.getRatePerVolumeUnit() : BigDecimal.ZERO;
                LBS = blAC.getRatePerUnitUom().equalsIgnoreCase("FRW") ? blAC.getRatePerWeightUnit() : BigDecimal.ZERO;
            }
            if (CFT != BigDecimal.ZERO || LBS != BigDecimal.ZERO) {
                StringBuilder cbmValues = new StringBuilder();
                if (CFT != BigDecimal.ZERO && lclBlPiece.getActualVolumeImperial() != null) {
                    cbmValues.append(NumberUtils.convertToThreeDecimalhash(lclBlPiece.getActualVolumeImperial().doubleValue()));
                }
                if (LBS != BigDecimal.ZERO && lclBlPiece.getActualWeightImperial() != null) {
                    cbmValues.append(NumberUtils.convertToThreeDecimalhash(lclBlPiece.getActualWeightImperial().doubleValue()));
                }
                if (null != blAC.getArAmount() && blAC.getArAmount().toString().equalsIgnoreCase(OCNFRT_Total)) {
                    if (CFT == BigDecimal.ZERO) {
                        cbmValues.append(" LBS @ ").append(LBS).append(" PER 100 LBS @ ").append(blAC.getArAmount());
                    } else {
                        cbmValues.append(" CFT @ ").append(CFT).append(" PER CFT @").append(blAC.getArAmount());
                    }
                    chargeCell = new PdfPCell();
                    chargeCell.setBorder(0);
                    chargeCell.setColspan(6);
                    chargeTable.addCell(chargeCell);

                    chargeCell = new PdfPCell();
                    chargeCell.setBorder(0);
                    chargeCell.setColspan(6);
                    p = new Paragraph(2f, "" + cbmValues.toString().toUpperCase(), totalFontQuote);
                    p.add(new Paragraph(2f, null != lclBlPiece && null != lclBlPiece.getCommodityType()
                            ? "   Commodity# " + lclBlPiece.getCommodityType().getCode() : "   Commodity#", totalFontQuote));
                    p.setAlignment(Element.ALIGN_LEFT);
                    chargeCell.addElement(p);
                    chargeTable.addCell(chargeCell);
                }
            }
        }
        this.OCNFRT_Total = "";
        LinkedHashMap<String, PdfPCell> left_chargeMap = new LinkedHashMap<String, PdfPCell>();
        LinkedHashMap<String, PdfPCell> right_chargeMap = new LinkedHashMap<String, PdfPCell>();
        if ("BOTH".equalsIgnoreCase(billType) && listChargeMap != null && !listChargeMap.isEmpty()) {
            if (listChargeMap.size() > 1) {
                if (listChargeMap.get(0).size() > 6 || listChargeMap.get(1).size() > 6) {
                    chargeMap = new LinkedHashMap<String, PdfPCell>();
                    chargeMap.putAll(listChargeMap.get(0));
                    chargeMap.putAll(listChargeMap.get(1));
                    int count = 0, size = chargeMap.size() / 2 <= 6 ? 6 : chargeMap.size() / 2;
                    for (String key : chargeMap.keySet()) {
                        if (count++ < size) {
                            left_chargeMap.put(key, chargeMap.get(key));
                        } else {
                            right_chargeMap.put(key, chargeMap.get(key));
                        }
                    }
                } else {
                    left_chargeMap.putAll(listChargeMap.get(0));
                    right_chargeMap.putAll(listChargeMap.get(1));
                }
            } else {
                int count = 0, size = listChargeMap.get(0).size() / 2 <= 6 ? 6 : listChargeMap.get(0).size() / 2;
                for (String key : listChargeMap.get(0).keySet()) {
                    if (count++ < size) {
                        left_chargeMap.put(key, listChargeMap.get(0).get(key));
                    } else {
                        right_chargeMap.put(key, listChargeMap.get(0).get(key));
                    }
                }
            }
        } else if (chargeMap != null && !chargeMap.isEmpty()) {
            int count = 0, size = chargeMap.size() / 2 <= 6 ? 6 : chargeMap.size() / 2;
            for (String key : chargeMap.keySet()) {
                if (count++ < size) {
                    left_chargeMap.put(key, chargeMap.get(key));
                } else {
                    right_chargeMap.put(key, chargeMap.get(key));
                }
            }
        }

        if (!left_chargeMap.isEmpty()) {
            String chargeDesc = null;
            PdfPTable innner_chargeTable = new PdfPTable(2);
            innner_chargeTable.setWidthPercentage(100f);
            PdfPCell inner_chargeCell = null;

            chargeCell = new PdfPCell();
            chargeCell.setBorder(0);
            chargeCell.setColspan(3);
            chargeCell.setBorderWidthRight(0.6f);
            chargeCell.setPadding(0);
            innner_chargeTable = new PdfPTable(2);
            innner_chargeTable.setWidths(new float[]{5f, 3f});
            if (!left_chargeMap.isEmpty()) {
                for (String key : left_chargeMap.keySet()) {
                    inner_chargeCell = new PdfPCell();
                    inner_chargeCell.setBorder(0);
                    inner_chargeCell.setPaddingLeft(-15);
                    chargeDesc = key.substring(key.indexOf("#") + 1, key.indexOf("$"));
                    inner_chargeCell.addElement(new Paragraph(7f, "" + chargeDesc, totalFontQuote));
                    innner_chargeTable.addCell(inner_chargeCell);

                    inner_chargeCell = new PdfPCell();
                    inner_chargeCell.setBorder(0);
                    inner_chargeCell = left_chargeMap.get(key);
                    innner_chargeTable.addCell(inner_chargeCell);
                }
            }
            chargeCell.addElement(innner_chargeTable);
            chargeTable.addCell(chargeCell);

            chargeCell = new PdfPCell();
            chargeCell.setBorder(0);
            chargeCell.setColspan(3);
            chargeCell.setPadding(0);
            innner_chargeTable = new PdfPTable(2);
            innner_chargeTable.setWidths(new float[]{5f, 3f});
            if (!left_chargeMap.isEmpty()) {
                for (String key : right_chargeMap.keySet()) {
                    inner_chargeCell = new PdfPCell();
                    inner_chargeCell.setBorder(0);
                    inner_chargeCell.setPaddingLeft(-15);
                    chargeDesc = key.substring(key.indexOf("#") + 1, key.indexOf("$"));
                    inner_chargeCell.addElement(new Paragraph(7f, "" + chargeDesc, totalFontQuote));
                    innner_chargeTable.addCell(inner_chargeCell);

                    inner_chargeCell = new PdfPCell();
                    inner_chargeCell.setBorder(0);
                    inner_chargeCell = right_chargeMap.get(key);
                    innner_chargeTable.addCell(inner_chargeCell);
                }
            }
            chargeCell.addElement(innner_chargeTable);
            chargeTable.addCell(chargeCell);
        } else {
            chargeCell = new PdfPCell();
            chargeCell.setBorder(0);
            chargeCell.setColspan(6);
            chargeCell.setFixedHeight(90);
            chargeTable.addCell(chargeCell);
            this.total_ar_amount = 0.00;
            this.total_ar_col_amount = 0.0;
            this.total_ar_ppd_amount = 0.0;
        }
        String acctNo = "";
        String billToParty = "";
        if (CommonUtils.isNotEmpty(this.exportBilltoParty)) {
            if (this.exportBilltoParty.equalsIgnoreCase("T")) {
                billToParty = "THIRD PARTY";
                acctNo = null != lclbl.getThirdPartyAcct() ? lclbl.getThirdPartyAcct().getAccountno() : acctNo;
            } else if (this.exportBilltoParty.equalsIgnoreCase("S")) {
                acctNo = null != lclbl.getShipAcct() ? lclbl.getShipAcct().getAccountno() : acctNo;
                billToParty = "SHIPPER";
            } else if (this.exportBilltoParty.equalsIgnoreCase("F")) {
                billToParty = "FORWARDER";
                acctNo = null != lclbl.getFwdAcct() ? lclbl.getFwdAcct().getAccountno() : acctNo;
            } else if (this.exportBilltoParty.equalsIgnoreCase("A")) {
                billToParty = "AGENT";
                if (lclBooking.getBookingType().equals("T") && lclbl.getLclFileNumber().getLclBookingImport().getExportAgentAcctNo() != null) {
                    acctNo = lclbl.getLclFileNumber().getLclBookingImport().getExportAgentAcctNo().getAccountno();
                } else if (lclBooking.getAgentAcct() != null) {
                    acctNo = lclBooking.getAgentAcct().getAccountno(); // Freight Acct
                } else {
                    acctNo = null != lclbl.getAgentAcct() ? lclbl.getAgentAcct().getAccountno() : acctNo;
                }
            }
        }
        if ("BOTH".equalsIgnoreCase(billType)) {
            if (this.total_ar_ppd_amount != 0.00 || this.total_ar_col_amount != 0.00) {
                if (this.total_ar_ppd_amount != 0.00) {
                    if (!"FreightInvoice".equalsIgnoreCase(this.footerTypeName)) {
                        if (CommonFunctions.isNotNullOrNotEmpty(ppdBillToSet)) {
                            for (String billTo : ppdBillToSet) {
                                arBillToParty = billTo;
                                ppdBillToSet.clear();
                                break;
                            }
                            if (arBillToParty.equalsIgnoreCase("T")) {
                                billToParty = "THIRD PARTY";
                                acctNo = null != lclbl.getThirdPartyAcct() ? lclbl.getThirdPartyAcct().getAccountno() : acctNo;
                            } else if (arBillToParty.equalsIgnoreCase("S")) {
                                acctNo = null != lclbl.getShipAcct() ? lclbl.getShipAcct().getAccountno() : acctNo;
                                billToParty = "SHIPPER";
                            } else if (arBillToParty.equalsIgnoreCase("F")) {
                                billToParty = "FORWARDER";
                                acctNo = null != lclbl.getFwdAcct() ? lclbl.getFwdAcct().getAccountno() : acctNo;
                            }
                        } else {
                            acctNo = null;
                        }
                    }
                    chargeCell = new PdfPCell();
                    chargeCell.setBorder(0);
                    chargeCell.setColspan(2);
                    p = new Paragraph(7f, "T O T A L (USA)", totalFontQuote);
                    p.setAlignment(Element.ALIGN_LEFT);
                    chargeCell.addElement(p);
                    chargeTable.addCell(chargeCell);

                    chargeCell = new PdfPCell();
                    chargeCell.setColspan(4);
                    chargeCell.setBorder(0);
                    p = new Paragraph(7f, "$" + NumberUtils.convertToTwoDecimal(this.total_ar_ppd_amount) + " PPD " + billToParty + "-" + acctNo, totalFontQuote);
                    p.setAlignment(Element.ALIGN_LEFT);
                    chargeCell.addElement(p);
                    chargeTable.addCell(chargeCell);
                }

                if (this.total_ar_col_amount != 0.00) {
                    String colAcctNo = "";
                    if (lclBooking.getBookingType().equals("T") && lclbl.getLclFileNumber().getLclBookingImport().getExportAgentAcctNo() != null) {
                        colAcctNo = lclbl.getLclFileNumber().getLclBookingImport().getExportAgentAcctNo().getAccountno();
                    } else if (lclBooking.getAgentAcct() != null) {
                        colAcctNo = lclBooking.getAgentAcct().getAccountno();
                    } else if (lclbl.getAgentAcct() != null) {
                        colAcctNo = lclbl.getAgentAcct().getAccountno();
                    }
                    chargeCell = new PdfPCell();
                    chargeCell.setBorder(0);
                    chargeCell.setColspan(2);
                    if (this.total_ar_ppd_amount == 0.00) {
                        p = new Paragraph(7f, "T O T A L (USA)", totalFontQuote);
                    } else {
                        p = new Paragraph(7f, "", totalFontQuote);
                    }
                    p.setAlignment(Element.ALIGN_LEFT);
                    chargeCell.addElement(p);
                    chargeTable.addCell(chargeCell);

                    chargeCell = new PdfPCell();
                    chargeCell.setColspan(4);
                    chargeCell.setBorder(0);
                    p = new Paragraph(7f, "$" + NumberUtils.convertToTwoDecimal(this.total_ar_col_amount) + " COL AGENT-" + colAcctNo, totalFontQuote);
                    p.setAlignment(Element.ALIGN_LEFT);
                    chargeCell.addElement(p);
                    chargeTable.addCell(chargeCell);
                }

                NumberFormat numberFormat = new DecimalFormat("###,###,##0.000");
                if (this.total_ar_ppd_amount != 0.00) {
                    String totalString1 = numberFormat.format(this.total_ar_ppd_amount).replaceAll(",", "");
                    int indexdot = totalString1.indexOf(".");
                    String beforeDecimal = totalString1.substring(0, indexdot);
                    String afterDecimal = totalString1.substring(indexdot + 1, totalString1.length());
                    chargeCell = new PdfPCell();
                    chargeCell.setColspan(6);
                    chargeCell.setBorder(0);
                    p = new Paragraph(7f, "" + ConvertNumberToWords.convert(Integer.parseInt(beforeDecimal))
                            + " DOLLARS AND " + StringUtils.removeEnd(afterDecimal, "0") + " CENTS", totalFontQuote);
                    chargeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    chargeCell.addElement(p);
                    chargeTable.addCell(chargeCell);
                }
                if (this.total_ar_col_amount != 0.00) {
                    String totalString1 = numberFormat.format(this.total_ar_col_amount).replaceAll(",", "");
                    int indexdot = totalString1.indexOf(".");
                    String beforeDecimal = totalString1.substring(0, indexdot);
                    String afterDecimal = totalString1.substring(indexdot + 1, totalString1.length());
                    chargeCell = new PdfPCell();
                    chargeCell.setColspan(6);
                    chargeCell.setBorder(0);
                    p = new Paragraph(7f, "" + ConvertNumberToWords.convert(Integer.parseInt(beforeDecimal))
                            + " DOLLARS AND " + StringUtils.removeEnd(afterDecimal, "0") + " CENTS", totalFontQuote);
                    chargeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    chargeCell.addElement(p);
                    chargeTable.addCell(chargeCell);
                }
            }
        } else if (this.total_ar_amount != 0.00) {
            chargeCell = new PdfPCell();
            chargeCell.setBorder(0);
            chargeCell.setColspan(2);
            chargeCell.setPaddingTop(8f);
            p = new Paragraph(7f, "T O T A L (USA)", totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            chargeCell.addElement(p);
            chargeTable.addCell(chargeCell);

            chargeCell = new PdfPCell();
            chargeCell.setColspan(4);
            chargeCell.setBorder(0);
            chargeCell.setPaddingTop(8f);
            p = new Paragraph(7f, "$" + NumberUtils.convertToTwoDecimal(this.total_ar_amount) + " " + billType + " " + billToParty + "-" + acctNo, totalFontQuote);
            p.setAlignment(Element.ALIGN_LEFT);
            chargeCell.addElement(p);
            chargeTable.addCell(chargeCell);

            NumberFormat numberFormat = new DecimalFormat("###,###,##0.000");
            String totalString1 = numberFormat.format(this.total_ar_amount).replaceAll(",", "");
            int indexdot = totalString1.indexOf(".");
            String beforeDecimal = totalString1.substring(0, indexdot);
            String afterDecimal = totalString1.substring(indexdot + 1, totalString1.length());
            chargeCell = new PdfPCell();
            chargeCell.setColspan(6);
            chargeCell.setBorder(0);
            p = new Paragraph(7f, "" + ConvertNumberToWords.convert(Integer.parseInt(beforeDecimal))
                    + " DOLLARS AND " + StringUtils.removeEnd(afterDecimal, "0") + " CENTS", totalFontQuote);
            chargeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            chargeCell.addElement(p);
            chargeTable.addCell(chargeCell);
        }

        chargeCell = new PdfPCell();
        chargeCell.setBorder(0);
        chargeCell.setColspan(4);
        p = new Paragraph(5f, "" + sailDateFormat, totalFontQuote);
        chargeCell.addElement(p);
        chargeTable.addCell(chargeCell);

        chargeCell = new PdfPCell();
        chargeCell.setColspan(5);
        chargeCell.setBorder(0);
        chargeTable.addCell(chargeCell);

        chargeCell = new PdfPCell();
        chargeCell.setColspan(3);
        chargeCell.setBorder(0);
        chargeCell.setRowspan(3);
        String fdPodValue = null;
        if (agencyInfo != null && CommonUtils.isNotEmpty(agencyInfo[2])) {
            fdPodValue = agencyInfo[2];
        } else if (CommonFunctions.isNotNull(lclbl.getFinalDestination()) && CommonFunctions.isNotNull(lclbl.getFinalDestination().getCountryId())
                && CommonFunctions.isNotNull(lclbl.getFinalDestination().getCountryId().getCodedesc())) {
            fdPodValue = lclbl.getFinalDestination().getUnLocationName() + "," + lclbl.getFinalDestination().getCountryId().getCodedesc();
        }
        fdPodValue = fdPodValue != null ? fdPodValue.toUpperCase() : podValues.toUpperCase();
        if (CommonUtils.isNotEmpty(lclbl.getSequenceNumber())) {
            fdPodValue = fdPodValue + "\n\r  Seq# " + lclbl.getSequenceNumber();
        }
        p = new Paragraph(7f, fdPodValue, totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        chargeCell.addElement(p);
        chargeTable.addCell(chargeCell);

        chargeCell = new PdfPCell();
        chargeCell.setBorder(0);
        chargeCell.setColspan(3);
        p = new Paragraph(5f, "UNIT# " + unitNumber, headingblackBoldFont);
        p.setAlignment(Element.ALIGN_LEFT);
        chargeCell.addElement(p);
        chargeTable.addCell(chargeCell);

        chargeCell = new PdfPCell();
        chargeCell.setBorder(0);
        chargeCell.setColspan(3);
        chargeCell.setPaddingTop(2f);
        p = new Paragraph(5f, "SEAL# " + sealOut, headingblackBoldFont);
        p.setAlignment(Element.ALIGN_LEFT);
        chargeCell.addElement(p);
        chargeTable.addCell(chargeCell);

        chargeCell = new PdfPCell();
        chargeCell.setBorder(0);
        chargeCell.setColspan(3);
        chargeCell.setPaddingTop(2f);
        p = new Paragraph(5f, "CONTROL-VOY# " + voyageNumber, totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        chargeCell.addElement(p);
        chargeTable.addCell(chargeCell);

        String emailId = "";
        StringBuilder agentDetails = new StringBuilder();
        //Agent Details
        String agentAcctNo = "";
        if (!footerprintdocumentName.contains("Bill Of Lading")
                && CommonUtils.isNotEmpty(acctNo)) {
            agentAcctNo = acctNo;
        } else if ("Y".equalsIgnoreCase(altAgentKey)) {
            agentAcctNo = CommonUtils.isNotEmpty(altAgentValue) ? altAgentValue : "";
        } else if (lclbl.getAgentAcct() != null) {
            agentAcctNo = (agencyInfo != null && CommonUtils.isNotEmpty(agencyInfo[0])) ? agencyInfo[0] : lclbl.getAgentAcct().getAccountno();
        }
        if (CommonUtils.isNotEmpty(agentAcctNo)) {
            CustAddress custAddress = new CustAddressDAO().findPrimeContact(agentAcctNo);
            if (CommonFunctions.isNotNull(custAddress)) {
                if (CommonFunctions.isNotNull(custAddress.getAcctName())) {
                    agentDetails.append(custAddress.getAcctName()).append("  ");
                }
                if (CommonFunctions.isNotNull(custAddress.getPhone())) {
                    agentDetails.append("Phone: ").append(custAddress.getPhone()).append("\n");
                } else {
                    agentDetails.append("\n");
                }
                if (CommonFunctions.isNotNull(custAddress.getCoName())) {
                    agentDetails.append(custAddress.getCoName()).append("\n");
                }
                if (CommonFunctions.isNotNull(custAddress.getAddress1())) {
                    agentDetails.append(custAddress.getAddress1().replace(", ", "\n")).append("\n");
                }
                if (CommonFunctions.isNotNull(custAddress.getCity1())) {
                    agentDetails.append(custAddress.getCity1());
                }
                if (CommonFunctions.isNotNull(custAddress.getState())) {
                    agentDetails.append("  ").append(custAddress.getState());
                }
                if (CommonFunctions.isNotNull(custAddress.getEmail1())) {
                    emailId = custAddress.getEmail1();
                }
            }
        }
        BigDecimal PrintInvoiceValue = null;

        if (lclbl.getPortOfDestination() != null && !this.footerTypeName.equalsIgnoreCase("FreightInvoice")
                && !this.footerTypeName.equalsIgnoreCase("FreightInvoiceCollect")) {
            boolean schnum = new LCLPortConfigurationDAO().getSchnumValue(lclbl.getPortOfDestination().getId());
            if (schnum) {
                BigDecimal printInvoice = lclbl.getInvoiceValue();
                if (!CommonUtils.isEmpty(printInvoice)) {
                    PrintInvoiceValue = printInvoice;

                }
            }

        }

        chargeCell = new PdfPCell();
        chargeCell.setBorder(2);
        chargeCell.setColspan(6);
        chargeCell.setPadding(0f);
        PdfPTable agent_Contact_Table = new PdfPTable(3);
        agent_Contact_Table.setWidthPercentage(100f);
        String freightContactMsg = "To Pick Up Freight Please Contact:";
        if (this.footerprintdocumentName.contains("FreightInvoice")) {
            agent_Contact_Table.setWidths(new float[]{1.5f, 2.5f, 1.8f});
            freightContactMsg = "Freight Payable By";
        } else {
            agent_Contact_Table.setWidths(new float[]{2.3f, 1.7f, 1.8f});
        }
        PdfPCell agent_Contact_cell = new PdfPCell();
        agent_Contact_cell.setBorder(0);
        agent_Contact_cell.setBorderWidthTop(1f);
        agent_Contact_cell.setBorderWidthLeft(1f);
        agent_Contact_cell.setBorderWidthBottom(0.06f);
        agent_Contact_cell.setBorderWidthRight(1f);
        p = new Paragraph(7f, "" + freightContactMsg, blackContentNormalFont);
        p.setAlignment(Element.ALIGN_LEFT);
        agent_Contact_cell.addElement(p);
        agent_Contact_Table.addCell(agent_Contact_cell);

        agent_Contact_cell = new PdfPCell();
        agent_Contact_cell.setBorder(0);
        agent_Contact_cell.setColspan(2);
        agent_Contact_cell.setBorderWidthTop(1f);
        agent_Contact_cell.setPaddingBottom(2f);
        p = new Paragraph(7f, "Email: " + emailId.toLowerCase(), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        agent_Contact_cell.addElement(p);
        agent_Contact_Table.addCell(agent_Contact_cell);

        agent_Contact_cell = new PdfPCell();
        agent_Contact_cell.setColspan(2);
        agent_Contact_cell.setBorder(0);
        agent_Contact_cell.setBorderWidthLeft(1f);
        p = new Paragraph(7f, "" + agentDetails.toString(), totalFontQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        agent_Contact_cell.addElement(p);
        agent_Contact_Table.addCell(agent_Contact_cell);

        agent_Contact_cell = new PdfPCell();
        agent_Contact_cell.setBorder(0);
        agent_Contact_cell.setPaddingTop(27f);
        p = new Paragraph(7f, "" + agentAcctNo, totalFontQuote);
        p.setAlignment(Element.ALIGN_RIGHT);
        agent_Contact_cell.addElement(p);
        agent_Contact_Table.addCell(agent_Contact_cell);

        agent_Contact_cell = new PdfPCell();
        agent_Contact_cell.setBorder(0);
        agent_Contact_cell.setColspan(3);
        agent_Contact_cell.setBorderWidthLeft(1f);
        StringBuilder builder = new StringBuilder();
        builder.append(PrintInvoiceValue != null ? "Value of Goods:USD $" + PrintInvoiceValue : "");
        p = new Paragraph(5f, "" + builder.toString(), totalFontQuote);
        p.setAlignment(Element.ALIGN_RIGHT);
        agent_Contact_cell.addElement(p);
        agent_Contact_Table.addCell(agent_Contact_cell);
        chargeCell.addElement(agent_Contact_Table);
        chargeTable.addCell(chargeCell);

        return chargeTable;
    }

    public PdfPTable appendDescComments() throws Exception {
        PdfPTable descTable = new PdfPTable(5);
        descTable.setWidthPercentage(100f);
        descTable.setWidths(new float[]{0.1f, 0.5f, 4f, 0.5f, 0.01f});
        PdfPCell descCell = null;
        Paragraph p = null;
        descCell = new PdfPCell();
        descCell.setBorder(0);
        descCell.setColspan(5);
        if (footerprintdocumentName.contains("Bill Of Lading")) {
            String comments = LoadLogisoftProperties.getProperty(ruleName.equalsIgnoreCase("ECU") ? "ECU.tariffterms"
                    : ruleName.equalsIgnoreCase("OTI") ? "OTI.tariffterms" : "Econo.tariffterms");
            p = new Paragraph(8f, comments + ""
                    + "\n" + "IN WITNESS WHERE OF THE CARRIER BY ITS AGENT HAS SIGNED................. 3(THREE) BILLS OF LADING, ALL OF THE SAME TENOR AND DATE, "
                    + "ONE OF WHICH BEING ACCOMPLISHED, THE OTHERS TO STAND VOID.\nPLEASE SEE OUR WEBSITE FOR TERMS AND CONDITIONS.", contentNormalFont);
            p.setSpacingAfter(10f);
            p.setAlignment(Element.ALIGN_LEFT);
            descCell.addElement(p);
            if (etaDate != null && !etaDate.equalsIgnoreCase("") && "Y".equalsIgnoreCase(arrivalDateKeyValue)) {
                p = new Paragraph(9f, "Arrival Date: " + etaDate, headingblackBoldFont);
                p.setAlignment(Element.ALIGN_CENTER);
                descCell.addElement(p);
            }
            descCell.setFixedHeight(100f);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setColspan(5);
            descCell.setFixedHeight(5f);
            descCell.setBorder(0);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(2);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setBorderWidthBottom(0.06f);
            String carrierName = LoadLogisoftProperties.getProperty(ruleName.equalsIgnoreCase("ECU")
                    ? "ECU.carrier" : ruleName.equalsIgnoreCase("OTI") ? "OTI.carrier" : "Econo.carrier");
            p = new Paragraph(12f, "BY " + carrierName.toUpperCase(), fontCompNormalSub);
            p.setAlignment(Element.ALIGN_LEFT);
            descCell.addElement(p);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(2);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setFixedHeight(15f);
            descCell.setColspan(2);
            descCell.setBorder(0);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setBorderWidthBottom(0.06f);
            byte[] image = user.getSignatureImage();
            if (null != image && (footerTypeName.equalsIgnoreCase("Original") || footerTypeName.equalsIgnoreCase("SignedNonNegotiable"))) {
                Image img = Image.getInstance(image);
                img.scalePercent(2);
                img.scaleAbsoluteWidth(40f);
                img.scaleAbsoluteHeight(8f);
                descCell.addElement(img);
            } else {
                p = new Paragraph(7f, "", blackBoldFontSize8);
                p.setAlignment(Element.ALIGN_LEFT);
                descCell.addElement(p);
            }
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(2);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(2);
            descCell.setFixedHeight(15f);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setBorderWidthBottom(0.06f);
            p = new Paragraph(12f, "" + polValues.toUpperCase(), fontCompNormalSub);
            p.setAlignment(Element.ALIGN_CENTER);
            descCell.addElement(p);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(2);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(2);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            if ("N".equalsIgnoreCase(portKey)) {
                p = new Paragraph(7f, "PORT", fontCompNormalSub);
            } else {
                p = new Paragraph(7f, "PORT" + " " + portKeyValue.toUpperCase(), fontCompNormalSub);
            }
            p.setAlignment(Element.ALIGN_CENTER);
            descCell.addElement(p);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(2);
            descTable.addCell(descCell);
        } else {
            Font blackArialFont10 = FontFactory.getFont("Arial", 10f, Font.NORMAL);
            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(5);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(5);
            p = new Paragraph(7f, "* * T H I S  I S  Y O U R  I N V O I C E * *", blackArialFont10);
            p.setAlignment(Element.ALIGN_CENTER);
            descCell.addElement(p);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(5);
            descCell.setPadding(5f);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(1);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(4);
            p = new Paragraph(7f, "Please Remit Payment With Copy Of Bill To:", blackArialFont10);
            p.setAlignment(Element.ALIGN_LEFT);
            descCell.addElement(p);
            descTable.addCell(descCell);
            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setPadding(3f);
            descCell.setColspan(5);
            descTable.addCell(descCell);
            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(1);
            descTable.addCell(descCell);
            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(4);
            p = new Paragraph(7f, !"OTI".equalsIgnoreCase(ruleName) ? "ECONOCARIBE CONSOLIDATORS, INC." : "O.T.I CARGO INC.", blackArialFont10);
            p.setAlignment(Element.ALIGN_LEFT);
            descCell.addElement(p);
            descTable.addCell(descCell);
            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(1);
            descTable.addCell(descCell);
            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(4);
            p = new Paragraph(7f, "2401 N.W. 69th Street, Miami, FL 33147", blackArialFont10);
            p.setAlignment(Element.ALIGN_LEFT);
            descCell.addElement(p);
            descTable.addCell(descCell);
            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(1);
            descTable.addCell(descCell);
            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(4);
            p = new Paragraph(7f, "Phone: (800) 275-3405 / (305) 693-5133", blackArialFont10);
            p.setAlignment(Element.ALIGN_LEFT);
            descCell.addElement(p);
            descTable.addCell(descCell);
            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(1);
            descTable.addCell(descCell);
            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(4);
            p = new Paragraph(7f, "Fax: (305) 694-3133 (Accounts Receivable)", blackArialFont10);
            p.setAlignment(Element.ALIGN_LEFT);
            descCell.addElement(p);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setPadding(10f);
            descCell.setColspan(5);
            descTable.addCell(descCell);

            descCell.setBorder(0);
            descCell.setColspan(5);
            p = new Paragraph(7f, "Payment due within 30 days of Sailing Date.", blackArialFont10);
            p.setAlignment(Element.ALIGN_LEFT);
            descCell.addElement(p);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setPadding(10f);
            descCell.setColspan(5);
            descTable.addCell(descCell);

            descCell = new PdfPCell();
            descCell.setBorder(0);
            descCell.setColspan(5);
            p = new Paragraph(7f, "" + sailDateFormat, totalFontQuote);
            descCell.addElement(p);
            descTable.addCell(descCell);
        }
        return descTable;
    }

    public LinkedHashMap<String, PdfPCell> getTotalCharges(List<LclBlAc> chargeList,
            List<LclBlPiece> lclBlPiecesList) throws Exception {
        List formattedChargesList = null;
        LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
        Long lngFileId = new Long(this.topFileId);
        String chargeCode = "";
        LinkedHashMap<String, PdfPCell> chargeMap = new LinkedHashMap<String, PdfPCell>();
        if (chargeList != null && chargeList.size() > 0 && lclBlPiecesList != null) {
            if (!engmet.trim().equalsIgnoreCase("")) {
                int approvedCorrectionCount = lclCorrectionDAO.getApprovedCorrectionCountByFileId(lngFileId);
                boolean isAdjustment = false;
                if (approvedCorrectionCount == 0) {
                    isAdjustment = true;
                }
                if (lclBlPiecesList.size() == 1) {
                    formattedChargesList = blUtils.getFormattedLabelChargesForBl(lclBlPiecesList, chargeList, engmet, null, isAdjustment);
                } else {
                    formattedChargesList = blUtils.getRolledUpChargesForBl(lclBlPiecesList, chargeList, engmet, null, isAdjustment);
                }
            }
            Double minchg = 0.0;
            Double bundleMinchg = 0.0;
            Double flatRateMinimum = 0.0;
            Double barrelBundlechg = 0.0;
            Double barrelAmount = 0.0;
            boolean is_OFBARR_Bundle = false;

            if (formattedChargesList != null && !formattedChargesList.isEmpty()) {
                PdfPCell chargeCell = null;
                Paragraph p = null;
                for (int i = 0; i < formattedChargesList.size(); i++) {
                    LclBlAc lclBlAc = (LclBlAc) formattedChargesList.get(i);
                    if (null != lclBlAc.getRolledupCharges()) {
                        minchg = lclBlAc.getRolledupCharges().doubleValue();
                    } else if (!CommonUtils.isEmpty(lclBlAc.getArAmount())) {
                        minchg = lclBlAc.getArAmount().doubleValue();
                    }

                    if (lclBlAc.getBundleIntoOf()) {
                        if ("TTBARR".equalsIgnoreCase(lclBlAc.getArglMapping().getChargeCode())) {
                            if (null != lclBlAc.getRolledupCharges()) {
                                barrelBundlechg += lclBlAc.getRolledupCharges().doubleValue();
                            } else if (!CommonUtils.isEmpty(lclBlAc.getArAmount())) {
                                barrelBundlechg += lclBlAc.getArAmount().doubleValue();
                            }
                        } else {
                            if ("OFBARR".equalsIgnoreCase(lclBlAc.getArglMapping().getChargeCode())) {
                                is_OFBARR_Bundle = true;
                            }
                            if (null != lclBlAc.getRolledupCharges()) {
                                bundleMinchg += lclBlAc.getRolledupCharges().doubleValue();
                            } else if (!CommonUtils.isEmpty(lclBlAc.getArAmount())) {
                                bundleMinchg += lclBlAc.getArAmount().doubleValue();
                            }
                        }
                    }
                }
                for (int k = 0; k < formattedChargesList.size(); k++) {
                    LclBlAc lclBlAc = (LclBlAc) formattedChargesList.get(k);
                    if (lclBlAc.getArglMapping().getChargeCode().equals(CommonConstants.OFR_CHARGECODE)) {
                        if (null != lclBlAc.getRolledupCharges()) {
                            flatRateMinimum = lclBlAc.getRolledupCharges().doubleValue() + bundleMinchg;
                        } else if (lclBlAc.getArAmount() != null) {
                            flatRateMinimum = lclBlAc.getArAmount().doubleValue() + bundleMinchg;
                        }
                    } else if (!is_OFBARR_Bundle && lclBlAc.getArglMapping().getChargeCode().equalsIgnoreCase("OFBARR")) {
                        if (null != lclBlAc.getRolledupCharges()) {
                            barrelAmount = lclBlAc.getRolledupCharges().doubleValue() + barrelBundlechg;
                        } else if (lclBlAc.getArAmount() != null) {
                            barrelAmount = lclBlAc.getArAmount().doubleValue() + barrelBundlechg;
                        }
                    }
                }
                Iterator sList = formattedChargesList.iterator();
                while (sList.hasNext()) {
                    LclBlAc lclBlAc = (LclBlAc) sList.next();
                    if (!exportbatchForm.getBillOfLading().equalsIgnoreCase("RATED")) {
                        String code = lclBlAc.getArglMapping().getChargeCode().toUpperCase();
                        if (exportbatchForm.getBillOfLading().equalsIgnoreCase("BUNKER") && !code.contains("BKRSUR")
                                && !code.contains("OCNFRT")) {
                            sList.remove();
                        } else if (exportbatchForm.getBillOfLading().equalsIgnoreCase("OCNBLPC")
                                && !code.contains("OCNFRT") && !code.contains("BLPC")) {
                            sList.remove();
                        } else if (exportbatchForm.getBillOfLading().equalsIgnoreCase("OCNFRT") && !code.contains("OCNFRT")) {
                            sList.remove();
                        } else if (exportbatchForm.getBillOfLading().equalsIgnoreCase("COLLECT")
                                && !lclBlAc.getArBillToParty().equalsIgnoreCase("A")) {
                            sList.remove();
                        } else if (exportbatchForm.getBillOfLading().equalsIgnoreCase("PREPAID")
                                && lclBlAc.getArBillToParty().equalsIgnoreCase("A")) {
                            sList.remove();
                        } else if (!lclBlAc.getPrintOnBl()) {
                            sList.remove();
                        }
                    } else if (this.footerTypeName.equalsIgnoreCase("FreightInvoiceCollect")
                            && !lclBlAc.getArBillToParty().equalsIgnoreCase("A")) {
                        sList.remove();
                    } else if (this.footerTypeName.equalsIgnoreCase("FreightInvoice")
                            && !lclBlAc.getArBillToParty().equalsIgnoreCase(this.exportBilltoParty)) {
                        sList.remove();
                    } else if ((!this.footerTypeName.contains("FreightInvoice")
                            && !lclBlAc.getPrintOnBl()) || lclBlAc.getBundleIntoOf()) {
                        sList.remove();
                    }
                }
                for (int i = 0; i < formattedChargesList.size(); i++) {
                    LclBlAc lclBlAc = (LclBlAc) formattedChargesList.get(i);
                    String chargeDesc = null;
                    String lbl = "";
                    if (lclBlAc.getArglMapping() != null && CommonUtils.isNotEmpty(lclBlAc.getArglMapping().getChargeCode())) {
                        chargeCode = lclBlAc.getArglMapping().getChargeCode();
                        chargeDesc = CommonUtils.isNotEmpty(lclBlAc.getArglMapping().getChargeDescriptions())
                                ? lclBlAc.getArglMapping().getChargeDescriptions() : lclBlAc.getArglMapping().getChargeCode();
                    }
                    String ar_amountLabel = "";
                    if (lclBlAc.getArAmount() != null) {
                        if (lclBlAc.getArglMapping().getChargeCode().equals(CommonConstants.OFR_CHARGECODE)) {
                            flatRateMinimum += is_OFBARR_Bundle ? barrelBundlechg : 0;
                            this.total_ar_amount += flatRateMinimum;
                            ar_amountLabel = NumberUtils.convertToTwoDecimal(flatRateMinimum);
                            if (!lclBlAc.isManualEntry()) {
                                OCNFRT_Total = NumberUtils.convertToTwoDecimal(flatRateMinimum);
                            }
                        } else if (!is_OFBARR_Bundle && lclBlAc.getArglMapping().getChargeCode().equalsIgnoreCase("OFBARR")) {
                            this.total_ar_amount += barrelAmount;
                            ar_amountLabel = NumberUtils.convertToTwoDecimal(barrelAmount);
                        } else {
                            if (null != lclBlAc.getRolledupCharges()) {
                                this.total_ar_amount += lclBlAc.getRolledupCharges().doubleValue();
                                ar_amountLabel = lclBlAc.getRolledupCharges().toString();
                            } else if (!CommonUtils.isEmpty(lclBlAc.getArAmount())) {
                                this.total_ar_amount += lclBlAc.getArAmount().doubleValue();
                                ar_amountLabel = lclBlAc.getArAmount().toString();
                            }
                        }
                    }
                    chargeCell = new PdfPCell();
                    chargeCell.setBorder(0);
                    if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("W") || lclBlAc.getRatePerUnitUom().equalsIgnoreCase("FRW")) {
                        lbl = "WT";
                        chargeCell.setPaddingRight(-14);
                    } else if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("V") || lclBlAc.getRatePerUnitUom().equalsIgnoreCase("FRV")) {
                        lbl = "MEA";
                        chargeCell.setPaddingRight(-14);
                    } else if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("M") || lclBlAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                        lbl = "MIN";
                        chargeCell.setPaddingRight(-14);
                    } else {
                        chargeCell.setPaddingRight(7);
                    }
                    chargeCell.setPaddingLeft(-5);
                    p = new Paragraph(7f, ar_amountLabel + " " + lbl, totalFontQuote);
                    p.setAlignment(Element.ALIGN_RIGHT);
                    chargeCell.addElement(p);
                    chargeMap.put(chargeCode + "#" + chargeDesc + "$" + lclBlAc.getId(), chargeCell);
                }
            }
        }
        return chargeMap;
    }

    public List<LinkedHashMap<String, PdfPCell>> getTotalChargesList(List<LclBlAc> chargeList,
            List<LclBlPiece> lclBlPiecesList) throws Exception {
        List<LinkedHashMap<String, PdfPCell>> l = new ArrayList<LinkedHashMap<String, PdfPCell>>();
        List formattedChargesList = null;
        LCLCorrectionDAO lclCorrectionDAO = new LCLCorrectionDAO();
        Long lngFileId = Long.parseLong(this.topFileId);
        String chargeCode = "";
        LinkedHashMap<String, PdfPCell> ppdChargeMap = new LinkedHashMap<String, PdfPCell>();
        LinkedHashMap<String, PdfPCell> colChargeMap = new LinkedHashMap<String, PdfPCell>();
        LinkedHashMap<String, PdfPCell> chargeMap = new LinkedHashMap<String, PdfPCell>();
        Double minchg = 0.0;
        Double bundleMinchg = 0.0;
        Double flatRateMinimum = 0.0;
        Double barrelBundlechg = 0.0;
        Double barrelAmount = 0.0;
        boolean is_OFBARR_Bundle = false;
        PdfPCell chargeCell = null;
        Paragraph p = null;
        if (chargeList != null && chargeList.size() > 0 && lclBlPiecesList != null) {
            if (!engmet.trim().equalsIgnoreCase("")) {
                int approvedCorrectionCount = lclCorrectionDAO.getApprovedCorrectionCountByFileId(lngFileId);
                boolean isAdjustment = false;
                if (approvedCorrectionCount == 0) {
                    isAdjustment = true;
                }
                if (lclBlPiecesList.size() == 1) {
                    formattedChargesList = blUtils.getFormattedLabelChargesForBl(lclBlPiecesList, chargeList, engmet, null, isAdjustment);
                } else {
                    formattedChargesList = blUtils.getRolledUpChargesForBl(lclBlPiecesList, chargeList, engmet, null, isAdjustment);
                }
            }
            if (formattedChargesList != null && !formattedChargesList.isEmpty()) {
                for (int i = 0; i < formattedChargesList.size(); i++) {
                    LclBlAc lclBlAc = (LclBlAc) formattedChargesList.get(i);
                    if (null != lclBlAc.getRolledupCharges()) {
                        minchg = lclBlAc.getRolledupCharges().doubleValue();
                    } else if (!CommonUtils.isEmpty(lclBlAc.getArAmount())) {
                        minchg = lclBlAc.getArAmount().doubleValue();
                    }

                    if (lclBlAc.getBundleIntoOf()) {
                        if ("TTBARR".equalsIgnoreCase(lclBlAc.getArglMapping().getChargeCode())) {
                            if (null != lclBlAc.getRolledupCharges()) {
                                barrelBundlechg += lclBlAc.getRolledupCharges().doubleValue();
                            } else if (!CommonUtils.isEmpty(lclBlAc.getArAmount())) {
                                barrelBundlechg += lclBlAc.getArAmount().doubleValue();
                            }
                        } else {
                            if ("OFBARR".equalsIgnoreCase(lclBlAc.getArglMapping().getChargeCode())) {
                                is_OFBARR_Bundle = true;
                            }
                            if (null != lclBlAc.getRolledupCharges()) {
                                bundleMinchg += lclBlAc.getRolledupCharges().doubleValue();
                            } else if (!CommonUtils.isEmpty(lclBlAc.getArAmount())) {
                                bundleMinchg += lclBlAc.getArAmount().doubleValue();
                            }
                        }
                    }
                }
                for (int k = 0; k < formattedChargesList.size(); k++) {
                    LclBlAc lclBlAc = (LclBlAc) formattedChargesList.get(k);
                    if (lclBlAc.getArglMapping().getChargeCode().equals(CommonConstants.OFR_CHARGECODE)) {
                        if (null != lclBlAc.getRolledupCharges()) {
                            flatRateMinimum = lclBlAc.getRolledupCharges().doubleValue() + bundleMinchg;
                        } else if (lclBlAc != null && lclBlAc.getArAmount() != null) {
                            flatRateMinimum = lclBlAc.getArAmount().doubleValue() + bundleMinchg;
                        }
                    } else if (!is_OFBARR_Bundle && lclBlAc.getArglMapping().getChargeCode().equalsIgnoreCase("OFBARR")) {
                        if (null != lclBlAc.getRolledupCharges()) {
                            barrelAmount = lclBlAc.getRolledupCharges().doubleValue() + barrelBundlechg;
                        } else if (lclBlAc != null && lclBlAc.getArAmount() != null) {
                            barrelAmount = lclBlAc.getArAmount().doubleValue() + barrelBundlechg;
                        }
                    }
                }
                Iterator sList = formattedChargesList.iterator();
                while (sList.hasNext()) {
                    LclBlAc lclBlAc = (LclBlAc) sList.next();
                    if (!exportbatchForm.getBillOfLading().equalsIgnoreCase("RATED")) {
                        String code = lclBlAc.getArglMapping().getChargeCode().toUpperCase();
                        if (exportbatchForm.getBillOfLading().equalsIgnoreCase("BUNKER") && !code.contains("BKRSUR")
                                && !code.contains("OCNFRT")) {
                            sList.remove();
                        } else if (exportbatchForm.getBillOfLading().equalsIgnoreCase("OCNBLPC")
                                && !code.contains("OCNFRT") && !code.contains("BLPC")) {
                            sList.remove();
                        } else if (exportbatchForm.getBillOfLading().equalsIgnoreCase("OCNFRT") && !code.contains("OCNFRT")) {
                            sList.remove();
                        } else if (exportbatchForm.getBillOfLading().equalsIgnoreCase("COLLECT")
                                && !lclBlAc.getArBillToParty().equalsIgnoreCase("A")) {
                            sList.remove();
                        } else if (exportbatchForm.getBillOfLading().equalsIgnoreCase("PREPAID")
                                && lclBlAc.getArBillToParty().equalsIgnoreCase("A")) {
                            sList.remove();
                        } else if (!lclBlAc.getPrintOnBl()) {
                            sList.remove();
                        }
                    } else if (this.footerTypeName.equalsIgnoreCase("FreightInvoiceCollect")
                            && !lclBlAc.getArBillToParty().equalsIgnoreCase("A")) {
                        sList.remove();
                    } else if (this.footerTypeName.equalsIgnoreCase("FreightInvoice")
                            && !lclBlAc.getArBillToParty().equalsIgnoreCase(this.exportBilltoParty)) {
                        sList.remove();
                    } else if ((!this.footerTypeName.contains("FreightInvoice")
                            && !lclBlAc.getPrintOnBl()) || lclBlAc.getBundleIntoOf()) {
                        sList.remove();
                    }
                }
                for (int i = 0; i < formattedChargesList.size(); i++) {
                    LclBlAc lclBlAc = (LclBlAc) formattedChargesList.get(i);
                    String chargeDesc = null;
                    String lbl = "";
                    if (lclBlAc.getArglMapping() != null && CommonUtils.isNotEmpty(lclBlAc.getArglMapping().getChargeCode())) {
                        chargeCode = lclBlAc.getArglMapping().getChargeCode();
                        chargeDesc = CommonUtils.isNotEmpty(lclBlAc.getArglMapping().getChargeDescriptions())
                                ? lclBlAc.getArglMapping().getChargeDescriptions() : lclBlAc.getArglMapping().getChargeCode();
                    }
                    String ar_amountLabel = "";
                    if (lclBlAc.getArAmount() != null) {
                        if (lclBlAc.getArglMapping().getChargeCode().equals(CommonConstants.OFR_CHARGECODE)) {
                            flatRateMinimum += is_OFBARR_Bundle ? barrelBundlechg : 0;
                            if ("A".equalsIgnoreCase(lclBlAc.getArBillToParty())) {
                                this.total_ar_col_amount += flatRateMinimum;
                            } else {
                                this.total_ar_ppd_amount += flatRateMinimum;
                            }
                            ar_amountLabel = NumberUtils.convertToTwoDecimal(flatRateMinimum);
                            if (!lclBlAc.isManualEntry()) {
                                OCNFRT_Total = NumberUtils.convertToTwoDecimal(flatRateMinimum);
                            }
                        } else if (!is_OFBARR_Bundle && lclBlAc.getArglMapping().getChargeCode().equalsIgnoreCase("OFBARR")) {
                            if ("A".equalsIgnoreCase(lclBlAc.getArBillToParty())) {
                                this.total_ar_col_amount += barrelAmount;
                            } else {
                                this.total_ar_ppd_amount += barrelAmount;
                            }
                            ar_amountLabel = NumberUtils.convertToTwoDecimal(barrelAmount);
                        } else {
                            if (null != lclBlAc.getRolledupCharges()) {
                                if ("A".equalsIgnoreCase(lclBlAc.getArBillToParty())) {
                                    this.total_ar_col_amount += lclBlAc.getRolledupCharges().doubleValue();
                                } else {
                                    this.total_ar_ppd_amount += lclBlAc.getRolledupCharges().doubleValue();
                                }
                                ar_amountLabel = lclBlAc.getRolledupCharges().toString();
                            } else if (!CommonUtils.isEmpty(lclBlAc.getArAmount())) {
                                if ("A".equalsIgnoreCase(lclBlAc.getArBillToParty())) {
                                    this.total_ar_col_amount += lclBlAc.getArAmount().doubleValue();
                                } else {
                                    this.total_ar_ppd_amount += lclBlAc.getArAmount().doubleValue();
                                }
                                ar_amountLabel = lclBlAc.getArAmount().toString();
                            }
                        }
                    }
                    chargeCell = new PdfPCell();
                    chargeCell.setBorder(0);
                    if (this.footerprintdocumentName.contains("Freight")) {
                        if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("W") || lclBlAc.getRatePerUnitUom().equalsIgnoreCase("FRW")) {
                            lbl = "WT";
                            chargeCell.setPaddingRight(-14);
                        } else if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("V") || lclBlAc.getRatePerUnitUom().equalsIgnoreCase("FRV")) {
                            lbl = "MEA";
                            chargeCell.setPaddingRight(-14);
                        } else if (lclBlAc.getRatePerUnitUom().equalsIgnoreCase("M") || lclBlAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                            lbl = "MIN";
                            chargeCell.setPaddingRight(-14);
                        } else {
                            chargeCell.setPaddingRight(7);
                        }
                    } else {
                        lbl = "A".equalsIgnoreCase(lclBlAc.getArBillToParty()) ? "COL" : "PPD";
                        chargeCell.setPaddingRight(-14);
                    }
                    chargeCell.setPaddingLeft(-5);
                    p = new Paragraph(7f, ar_amountLabel + " " + lbl, totalFontQuote);
                    p.setAlignment(Element.ALIGN_RIGHT);
                    chargeCell.addElement(p);
                    if ("A".equalsIgnoreCase(lclBlAc.getArBillToParty())) {
                        colChargeMap.put(chargeCode + "#" + chargeDesc + "$" + lclBlAc.getId(), chargeCell);
                    } else {
                        ppdBillToSet.add(lclBlAc.getArBillToParty());
                        ppdChargeMap.put(chargeCode + "#" + chargeDesc + "$" + lclBlAc.getId(), chargeCell);
                    }

                }
                if (!ppdChargeMap.isEmpty() && !colChargeMap.isEmpty()) {
                    l.add(0, ppdChargeMap);
                    l.add(1, colChargeMap);
                } else if (!ppdChargeMap.isEmpty()) {
                    l.add(0, ppdChargeMap);
                } else if (!colChargeMap.isEmpty()) {
                    l.add(0, colChargeMap);
                }

            }
        }
        return l;
    }
}
