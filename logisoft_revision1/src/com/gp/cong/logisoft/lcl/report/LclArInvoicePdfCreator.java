package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.BlUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.lcl.comparator.ArRedInvoiceChargesComparator;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.hibernate.dao.ArRedInvoiceChargesDAO;
import com.logiware.hibernate.domain.ArRedInvoice;
import com.logiware.hibernate.domain.ArRedInvoiceCharges;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfGState;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

public class LclArInvoicePdfCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(LclArInvoicePdfCreator.class);
    private LclUtils lclUtils = new LclUtils();
    private BlUtils blUtils = new BlUtils();
    FclBlBC fclBlBC = new FclBlBC();
    Document document = null;
    PdfWriter pdfWriter = null;
    protected PdfTemplate total;
    protected BaseFont helv;
    private String contextPath = null;
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    GenericCode genericCode = new GenericCode();
    ArRedInvoice arRedInvoice = null;
    private StringBuilder eciShipmentFileNo = new StringBuilder();
    private String subHouseBl = new String();
    private String imagePath ="";
    private String companyName ="";
    public LclArInvoicePdfCreator() {
    }

    public LclArInvoicePdfCreator(String contextPath, ArRedInvoice arRedInvoice, String imagePath, String companyName) {
        this.contextPath = contextPath;
        this.arRedInvoice = arRedInvoice;
        this.imagePath = imagePath;
        this.companyName = companyName;
    }

    public void initialize(String fileName, String contextPath, ArRedInvoice arRedInvoice) throws FileNotFoundException,
            DocumentException,
            Exception {
        this.setBrand(arRedInvoice);
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        pdfWriter.setPageEvent(new LclArInvoicePdfCreator(contextPath, arRedInvoice,imagePath,companyName));
        HeaderFooter footer = new HeaderFooter(
                new Phrase("Thank you for choosing " + this.companyName + " for all your shipping needs", new Font(Font.HELVETICA, 10, Font.ITALIC, Color.BLACK)), false);
        footer.setBorder(Rectangle.NO_BORDER);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.setFooter(footer);
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
            log.info("onOpenDocument failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        total.beginText();
        total.setFontAndSize(helv, 7);
        total.setTextMatrix(0, 0);
        total.showText(String.valueOf(writer.getPageNumber() - 1));
        total.endText();
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        onStartArRedInvoicePage(writer, document);
    }

    public void onStartArRedInvoicePage(PdfWriter writer, Document document) {
        try {
            SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
            String companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
            String companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
            String companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");
            PdfPCell cell = new PdfPCell();
            PdfPTable headingMainTable = new PdfPTable(1);
            headingMainTable.setWidthPercentage(100);
            PdfPTable headingTable = new PdfPTable(1);
            headingTable.setWidths(new float[]{100});
            PdfPTable imgTable = new PdfPTable(1);
            imgTable.setWidthPercentage(100);
            Image img = Image.getInstance(contextPath + this.imagePath);
            img.scalePercent(75);
            PdfPCell logoCell = new PdfPCell(img);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            logoCell.setVerticalAlignment(Element.ALIGN_LEFT);
            logoCell.setPaddingLeft(+27);
            imgTable.addCell(logoCell);
            PdfPTable addrTable = new PdfPTable(1);
            addrTable.setWidthPercentage(100);
            PdfPTable invoiceFacturaTable = new PdfPTable(3);
            invoiceFacturaTable.setWidthPercentage(100);
            invoiceFacturaTable.setWidths(new float[]{40, 20, 40});
            StringBuilder stringBuilder = new StringBuilder();
            addrTable.addCell(makeCellCenterNoBorderFclBL("MAILING ADDRESS: " + (CommonUtils.isNotEmpty(companyAddress) ? companyAddress.toUpperCase() : "")));
            stringBuilder.append("TEL: ");
            stringBuilder.append(CommonUtils.isNotEmpty(companyPhone) ? companyPhone : "").append(" / ");
            stringBuilder.append("FAX: ");
            stringBuilder.append(CommonUtils.isNotEmpty(companyFax) ? companyFax : "");
            addrTable.addCell(makeCellCenterNoBorderFclBL(stringBuilder.toString()));
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));
            invoiceFacturaTable.addCell(makeCellLeftNoBorderFclBL(""));
            cell = makeCell("INVOICE", Element.ALIGN_CENTER, new Font(Font.HELVETICA, 12, Font.BOLD, Color.RED), 0.06f);
            invoiceFacturaTable.addCell(cell);
            invoiceFacturaTable.addCell(makeCellLeftNoBorderFclBL(""));
            cell = new PdfPCell();
            cell.addElement(invoiceFacturaTable);
            cell.setBorder(0);
            addrTable.addCell(cell);
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));

            cell = new PdfPCell();
            cell.addElement(imgTable);
            cell.setBorder(0);
            cell.setPaddingLeft(+150);
            headingMainTable.addCell(cell);
//            headingTable.addCell(cell);
            cell = new PdfPCell();
            cell.addElement(addrTable);
            cell.setBorder(0);
            headingTable.addCell(cell);
            cell = makeCellLeftNoBorderFclBL("");
            cell.setBorderWidthRight(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthBottom(0.0f);
            cell.addElement(headingTable);
            headingMainTable.addCell(cell);
            document.add(headingMainTable);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public void createBody(ArRedInvoice arRedInvoice, String contextPath, User loginUser,
            List<ArRedInvoiceCharges> arRedInvoiceChargesList, Map<String, List<ImportsManifestBean>> fileMap, boolean sessionFlag)
            throws DocumentException, MalformedURLException, IOException, Exception {
        StringBuilder originValues = new StringBuilder();
        StringBuilder destinationValues = new StringBuilder();

        // tabel for company details and invoice heading
        PdfPCell cell = new PdfPCell();
        LclUnitSsDAO lclUnitSSDAO = new LclUnitSsDAO();
        CustAddress custAddress = new CustAddressDAO().findByAccountNo(arRedInvoice.getCustomerNumber());
        PdfPTable mainTable = makeTable(2);
        mainTable.setWidthPercentage(100);
        PdfPTable clientPTable = new PdfPTable(2);
        clientPTable.setWidthPercentage(101);
        clientPTable.setWidths(new float[]{10, 90});
        clientPTable.setKeepTogether(true);
        cell = makeCell("TO:", Element.ALIGN_LEFT, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell(arRedInvoice.getCustomerName(), Element.ALIGN_LEFT, blackFontForFclAr, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFontSize8, 0);
        cell.setBorderWidthRight(0.06f);
        clientPTable.addCell(cell);
        StringBuilder stringBuilder = new StringBuilder("");
        if (null != custAddress) {
            stringBuilder.append(custAddress.getAddress1()).append("\n");
            stringBuilder.append("\n");
            stringBuilder.append(custAddress.getCity1()).append(", ");
            stringBuilder.append(custAddress.getState()).append(" ").append(custAddress.getZip());
        }
        cell = makeCell(stringBuilder.toString(), Element.ALIGN_LEFT, blackFontForFclAr, 0);
        cell.setBorderWidthRight(0.06f);
        cell.setMinimumHeight(30);
        clientPTable.addCell(cell);
        cell = makeCell("ATTN", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell(CommonUtils.isNotEmpty(arRedInvoice.getContactName()) ? arRedInvoice.getContactName().toUpperCase() : "", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell.addElement(clientPTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.0f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.0f);
        cell.setBorderWidthBottom(0.0f);
        mainTable.addCell(cell);

        LclBl lclBl = null;
        LclBooking lclBooking = null;
        LclUnitSs lclUnitSs = null;
        LclSsDetail lclSsDetail = null;
        String unitSsId = null;
        boolean voyageImpFlag = false;
        boolean drImpFlag = false;
        boolean drExpFlag = false;
        boolean voyageExpFlag = false;
        boolean isBl = false;
        boolean isBooking = false;
        String unitSsHeader = null;
        LclSsHeader lclSsHeader = null;
        String unitNo = "";
        String drNumber = "";
        String lclSsHeaderId = "";
        String refNo = "";

        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        if ("LCLI DR".equalsIgnoreCase(arRedInvoice.getScreenName())) {
            unitSsId = lclFileNumberDAO.getUnitSsId(Long.parseLong(arRedInvoice.getFileNo()));
            drImpFlag = true;
        } else if ("IMP VOYAGE".equalsIgnoreCase(arRedInvoice.getScreenName()) || sessionFlag) {
            unitSsId = arRedInvoice.getFileNo();
            voyageImpFlag = true;
        } else if ("LCLE DR".equalsIgnoreCase(arRedInvoice.getScreenName())) {
            unitSsId = lclFileNumberDAO.getUnitSsId(Long.parseLong(arRedInvoice.getFileNo()));
            drExpFlag = true;
        } else if ("EXP VOYAGE".equalsIgnoreCase(arRedInvoice.getScreenName()) || sessionFlag) {
            unitSsHeader = arRedInvoice.getFileNo();
            voyageExpFlag = true;
        }
        if (CommonUtils.isNotEmpty(unitSsId)) {
            lclSsHeader = new LclSsHeaderDAO().findById(Long.parseLong(unitSsId));
            lclUnitSs = lclUnitSSDAO.findById(Long.parseLong(unitSsId));
            lclSsDetail = lclUnitSs.getLclSsHeader().getVesselSsDetail();
            unitNo = lclUnitSs.getLclUnit().getUnitNo();
        }
        if (CommonUtils.isNotEmpty(unitSsHeader)) {// only for ExportVoyage
            lclSsHeader = new LclSsHeaderDAO().findById(Long.parseLong(unitSsHeader));
            lclSsHeaderId = String.valueOf(lclSsHeader.getId());
            lclUnitSs = new LclUnitSsDAO().getByProperty("lclSsHeader", lclSsHeader);
            lclSsDetail = new LclSsDetailDAO().getByProperty("lclSsHeader", lclSsHeader);
            unitNo = new LclUnitSsDAO().getUnitNameCount(String.valueOf(lclSsHeader.getId()));
        }

        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        LCLBlDAO lCLBlDAO = new LCLBlDAO();

        String ssVoyage = "";//vesselName
        String exportReference = "";//ssVoy

        lclBl = lCLBlDAO.getByProperty("lclFileNumber.id", Long.parseLong(arRedInvoice.getFileNo()));

        if (lclBl != null && drExpFlag) {
            isBl = true;
            if (unitSsId == null) {//Booked for Voyage in BL screen
                lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", Long.parseLong(arRedInvoice.getFileNo()));
                LclSsHeader bookedHeader = lclBooking.getBookedSsHeaderId();
                if (bookedHeader != null && null != bookedHeader.getVesselSsDetail()) {
                    ssVoyage = null != bookedHeader.getVesselSsDetail().getSpReferenceName() ? bookedHeader.getVesselSsDetail().getSpReferenceName() : "";
                    exportReference = null != bookedHeader.getVesselSsDetail().getSpReferenceNo() ? bookedHeader.getVesselSsDetail().getSpReferenceNo() : "";
                }
            }
        } else if (drExpFlag) {
            lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", Long.parseLong(arRedInvoice.getFileNo()));
            if (CommonUtils.isNotEmpty(unitSsId) && drExpFlag) {//Picked on Voyage
                isBooking = true;
            } else if (isBl == false && isBooking == false && lclBooking != null) {//Booked for Voyage
                LclSsHeader bookedHeader = lclBooking.getBookedSsHeaderId();
                if (bookedHeader != null && null != bookedHeader.getVesselSsDetail()) {
                    ssVoyage = null != bookedHeader.getVesselSsDetail().getSpReferenceName() ? bookedHeader.getVesselSsDetail().getSpReferenceName() : "";
                    exportReference = null != bookedHeader.getVesselSsDetail().getSpReferenceNo() ? bookedHeader.getVesselSsDetail().getSpReferenceNo() : "";
                }
                isBooking = true;
            }
        }

        if (drImpFlag) {
            lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", Long.parseLong(arRedInvoice.getFileNo()));
        }

        if ((voyageImpFlag || sessionFlag) && lclUnitSs != null && lclUnitSs.getLclSsHeader() != null) {
            String[] drNumbers = lclUnitSSDAO.getTotalBkgInUnits(lclUnitSs.getId().toString());
            drNumber = drNumbers[1];
            originValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclUnitSs.getLclSsHeader().getOrigin()));
            destinationValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclUnitSs.getLclSsHeader().getDestination()));
            eciShipmentFileNo.append(lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum()).append("-");
            eciShipmentFileNo.append(lclUnitSs.getLclSsHeader().getOrigin().getUnLocationCode()).append("-");
            eciShipmentFileNo.append(lclUnitSs.getLclSsHeader().getDestination().getUnLocationCode()).append("-");
            eciShipmentFileNo.append(lclUnitSs.getLclSsHeader().getScheduleNo());
        } else if (drImpFlag && lclBooking != null && lclBooking.getLclFileNumber() != null) {
            drNumber = "IMP-" + lclBooking.getLclFileNumber().getFileNumber();
            originValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclBooking.getPortOfLoading()));
            destinationValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclBooking.getPortOfDestination()));
            eciShipmentFileNo.append(lclBooking.getTerminal().getTrmnum()).append("-").append(lclBooking.getPortOfLoading().getUnLocationCode()).append("-");
            eciShipmentFileNo.append(lclBooking.getPortOfDestination().getUnLocationCode()).append("-");
            if (lclUnitSs != null) {
                eciShipmentFileNo.append(lclUnitSs.getLclSsHeader().getScheduleNo());
            } else {
                eciShipmentFileNo.append(lclBooking.getLclFileNumber().getFileNumber());
            }
        }

        if (drExpFlag && lclBl != null && lclBl.getLclFileNumber() != null) {
            drNumber = lclBl.getLclFileNumber().getFileNumber();
            originValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclBl.getPortOfOrigin()));
            destinationValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclBl.getPortOfDestination()));
            eciShipmentFileNo.append("ECCI-").append((lclBl.getPortOfOrigin().getUnLocationCode().substring(2))).append("-").append(lclBl.getPortOfDestination().getUnLocationCode()).append("-");
            eciShipmentFileNo.append(lclBl.getLclFileNumber().getFileNumber());
            refNo = new LclRemarksDAO().getLclRemarksByTypeSQL(String.valueOf(lclBl.getLclFileNumber().getId()), LclCommonConstant.REMARKS_TYPE_EXPORT_REF);
        } else if (CommonUtils.isNotEmpty(unitSsId) && drExpFlag && isBooking) {
            drNumber = lclBooking.getLclFileNumber().getFileNumber();
            originValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclBooking.getPortOfOrigin()));
            destinationValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclBooking.getPortOfDestination()));
            eciShipmentFileNo.append((lclBooking.getPortOfOrigin().getUnLocationCode().substring(2))).append("-").append(drNumber);
        } else if (drExpFlag && lclBooking != null && lclBooking.getLclFileNumber() != null) {
            drNumber = lclBooking.getLclFileNumber().getFileNumber();
            originValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclBooking.getPortOfOrigin()));
            destinationValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclBooking.getPortOfDestination()));
            eciShipmentFileNo.append((lclBooking.getPortOfOrigin().getUnLocationCode().substring(2))).append("-").append(drNumber);
        } else if ((voyageExpFlag) && lclUnitSs != null && lclUnitSs.getLclSsHeader() != null) {
            String[] drNumbers = lclUnitSSDAO.getTotalBookingCount(lclSsHeaderId);
            drNumber = drNumbers[1];
            originValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclUnitSs.getLclSsHeader().getOrigin()));
            destinationValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclUnitSs.getLclSsHeader().getDestination()));
            eciShipmentFileNo.append(lclUnitSs.getLclSsHeader().getOrigin().getUnLocationCode()).append("-");
            eciShipmentFileNo.append(lclUnitSs.getLclSsHeader().getDestination().getUnLocationCode()).append("-");
            eciShipmentFileNo.append(lclUnitSs.getLclSsHeader().getScheduleNo());
        }

        List lineItemList = null, fileList = null;
        if (CommonUtils.isNotEmpty(arRedInvoice.getId()) && arRedInvoice.getId() > 0) {
            lineItemList = new ArRedInvoiceChargesDAO().findByProperty("arRedInvoiceId", arRedInvoice.getId());
            Collections.sort(lineItemList, new ArRedInvoiceChargesComparator());
            fileMap = new LinkedHashMap();
            List<ImportsManifestBean> manifestListFromMap = null;
            String[] fileNumberArray = null;
            for (Object object : lineItemList) {
                ArRedInvoiceCharges arRedInvoiceCharges = (ArRedInvoiceCharges) object;
                if (voyageImpFlag && CommonUtils.isNotEmpty(arRedInvoiceCharges.getLclDrNumber())) {
                    fileNumberArray = arRedInvoiceCharges.getLclDrNumber().split(",");
                } else if (drImpFlag && CommonUtils.isNotEmpty(arRedInvoiceCharges.getBlDrNumber())) {
                    fileNumberArray = arRedInvoiceCharges.getBlDrNumber().split(",");
                }
                if (fileNumberArray != null) {
                    Set<String> fileNumberSet = new HashSet<String>(Arrays.asList(fileNumberArray));
                    for (String fileNumber : fileNumberSet) {
                        LclFileNumber lclFileNumber = new LclFileNumberDAO().getByProperty("fileNumber", fileNumber);
                        LclBookingImport lclBookingImport = lclFileNumber.getLclBookingImport();
                        subHouseBl = null != lclBookingImport ? lclBookingImport.getSubHouseBl() : "";
                        StringBuilder concatFd = new StringBuilder();
                        concatFd.append("(").append(lclFileNumber.getLclBooking().getFinalDestination().getUnLocationName());
                        if (lclFileNumber.getLclBooking().getFinalDestination().getStateId() != null && lclFileNumber.getLclBooking().getFinalDestination().getStateId().getCode() != null) {
                            concatFd.append(",").append(lclFileNumber.getLclBooking().getFinalDestination().getStateId().getCode());
                        } else if (lclFileNumber.getLclBooking().getFinalDestination().getCountryId() != null && lclFileNumber.getLclBooking().getFinalDestination().getCountryId().getCodedesc() != null) {
                            concatFd.append(",").append(lclFileNumber.getLclBooking().getFinalDestination().getCountryId().getCodedesc());
                        }
                        concatFd.append(")");
                        List charges = new LclCostChargeDAO().getAgentInvoiceChargesAmount(lclFileNumber.getId(), arRedInvoiceCharges.getChargeCode(), arRedInvoice.getInvoiceNumber());
                        Iterator it = charges.iterator();
                        while (it.hasNext()) {
                            BigDecimal charge = (BigDecimal) it.next();
                            LclBookingPad lclBookingPad = !lclFileNumber.getLclBookingPadList().isEmpty() ? lclFileNumber.getLclBookingPadList().get(0) : null;
                            if (!fileMap.containsKey("IMP-" + fileNumber)) {
                                manifestListFromMap = new ArrayList();
                            } else {
                                manifestListFromMap = (List) fileMap.get("IMP-" + fileNumber);
                            }
                            ImportsManifestBean impManifestBean = new ImportsManifestBean();
                            impManifestBean.setChargeCode(arRedInvoiceCharges.getChargeCode());
                            if (arRedInvoiceCharges.getChargeCode().equalsIgnoreCase("DOORDEL") && lclBookingPad != null && CommonUtils.isNotEmpty(lclBookingPad.getPickUpCity())) {
                                impManifestBean.setChargeCode(arRedInvoiceCharges.getChargeCode() + " ( " + lclBookingPad.getPickUpCity() + " )");
                            }
                            impManifestBean.setFileNo(arRedInvoiceCharges.getLclDrNumber());
                            impManifestBean.setSubHouseBl(subHouseBl);
                            impManifestBean.setFinalDestination(concatFd.toString());
                            if (!CommonUtils.isEmpty(charge)) {
                                impManifestBean.setTotalCharges(charge.doubleValue());
                            } else {
                                impManifestBean.setTotalCharges(arRedInvoiceCharges.getAmount());
                            }
                            manifestListFromMap.add(impManifestBean);
                            fileMap.put("IMP-" + fileNumber, manifestListFromMap);
                        }
                    }
                } else {
                    if (!fileMap.containsKey(arRedInvoiceCharges.getBlDrNumber())) {
                        fileList = new ArrayList();
                    } else {
                        fileList = (List) fileMap.get(arRedInvoiceCharges.getBlDrNumber());
                    }
                    ImportsManifestBean impManifestBean = new ImportsManifestBean();
                    impManifestBean.setChargeCode(arRedInvoiceCharges.getChargeCode());
                    impManifestBean.setTotalCharges(arRedInvoiceCharges.getAmount());
                    if (lclUnitSs != null) {
                        impManifestBean.setFinalDestination(lclUnitSs.getLclUnit().getUnitType().getDescription());
                    }
                    fileList.add(impManifestBean);
                    fileMap.put(arRedInvoiceCharges.getBlDrNumber(), fileList);
                }
            }
        } else {
            lineItemList = arRedInvoiceChargesList;
            Collections.sort(lineItemList, new ArRedInvoiceChargesComparator());
        }

        Paragraph p = null;
        PdfPTable invoiceMainTable = new PdfPTable(1);
        invoiceMainTable.setWidthPercentage(101.5f);
        invoiceMainTable.setKeepTogether(true);
        PdfPTable invoicePTable1 = new PdfPTable(4);
        invoicePTable1.setWidths(new float[]{10, 25, 20, 45});
        invoicePTable1.setWidthPercentage(101);
        invoicePTable1.addCell(makeCellLeftNoBorderFclBL(""));
        cell = makeCell("INVOICE NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);
        cell = makeCell("DATE", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);
        cell = makeCell("REFERENCE NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);
        invoicePTable1.addCell(makeCellLeftNoBorderFclBL(""));
        cell = makeCell(arRedInvoice.getInvoiceNumber(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);
        SimpleDateFormat simpDate = new SimpleDateFormat("dd-MMM-yyyy");

//        String exportReference = "";
        Date etaDate;
        String dateString = "";
        if (lclSsDetail != null) {
            if (voyageExpFlag) {
//                lclSsDetail = new LclSsDetailDAO().getByProperty("lclSsHeader", lclSsHeader);
                if (lclSsDetail.getSpReferenceNo() != null) {
                    exportReference = lclSsDetail.getSpReferenceNo();
                }
                if (lclSsDetail.getSpReferenceName() != null) {
                    ssVoyage = lclSsDetail.getSpReferenceName();
                }
            } else {
//                lclSsDetail = lclUnitSs.getLclSsHeader().getVesselSsDetail();                
                if (lclSsDetail.getSpReferenceNo() != null) {
                    exportReference = lclSsDetail.getSpReferenceNo();
                }
                if (lclSsDetail.getSpReferenceName() != null) {
                    ssVoyage = lclSsDetail.getSpReferenceName();
                }
            }
            if (lclSsDetail.getSta() != null) {
                etaDate = lclSsDetail.getSta();
            }
        }

        if (voyageExpFlag || drExpFlag) {
            if (arRedInvoice.getPostedDate() != null) {
                dateString = simpDate.format(arRedInvoice.getPostedDate());
            }
        } else if (arRedInvoice.getDate() != null) {
            dateString = simpDate.format(arRedInvoice.getDate());
        }

        cell = makeCell(dateString, Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);
        if (isBl) {
            cell = makeCell("" + refNo, Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthBottom(0.06f);
            invoicePTable1.addCell(cell);
        } else {
            String fileNumber = drImpFlag ? drNumber : "";
            cell = makeCell("" + fileNumber, Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthBottom(0.06f);
            invoicePTable1.addCell(cell);
        }
        cell = makeCellLeftNoBorderFclBL("");
        cell.setColspan(4);
        invoicePTable1.addCell(cell);
        cell = new PdfPCell();
        cell.addElement(invoicePTable1);
        cell.setBorder(0);
        invoiceMainTable.addCell(cell);

        PdfPTable invoicePTable2 = new PdfPTable(3);
        invoicePTable2.setWidths(new float[]{10, 40, 50});
        invoicePTable2.setWidthPercentage(101);

        invoicePTable2.addCell(makeCellLeftNoBorderFclBL(""));
        cell = makeCell("BILL TO ACCT NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable2.addCell(cell);
        cell = makeCell(arRedInvoice.getCustomerNumber(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable2.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(invoicePTable2);
        cell.setBorder(0);
        invoiceMainTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(invoiceMainTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.0f);
        cell.setBorderWidthTop(0.0f);
        cell.setBorderWidthBottom(0.0f);
        mainTable.addCell(cell);

        PdfPTable othersTable = makeTable(4);
        othersTable.setWidthPercentage(100f);
        othersTable.setWidths(new float[]{25, 25, 25, 25});
        cell = makeCell("CONTAINER NO.", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("ORIGIN", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("DESTINATION", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        othersTable.addCell(cell);
        othersTable = fillMarksAndVoyageContinerInformation(othersTable, unitNo, originValues.toString(), destinationValues.toString());
        cell = makeCell("ECI SHIPMENT FILE NUMBER", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("COMMENTS", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("" + eciShipmentFileNo.toString(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("" + arRedInvoice.getNotes().toUpperCase(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        othersTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        othersTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(othersTable);
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);

        cell = makeCell("DESCRIPTION / DETAILS", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setColspan(4);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setColspan(4);
        PdfPCell pcell = null;
        PdfPTable ptable = new PdfPTable(4);
        ptable.setWidthPercentage(100f);
        ptable.setWidths(new float[]{0.08f, 0.93f, 0.09f, 5.28f});

        String masterBl = "";
//        String ssVoyage = "";
        String containerNo = "";
        if (lclUnitSs != null && !drExpFlag && !voyageExpFlag) {
            masterBl = lclUnitSs.getLclSsHeader().getLclUnitSsManifestList().get(0).getMasterbl().toUpperCase();
            containerNo = lclUnitSs.getLclUnit().getUnitNo().toUpperCase() + "  ( " + lclUnitSs.getLclUnit().getUnitType().getDescription().toUpperCase() + " )";
        }
        if (lclSsDetail != null && !drExpFlag && !voyageExpFlag) {
            if (CommonUtils.isNotEmpty(lclSsDetail.getSpReferenceName())) {
                ssVoyage = lclSsDetail.getSpReferenceName().toUpperCase();
            }
        }

        //DockReceipts
        if (arRedInvoice.getPrintOnDr() || drImpFlag) {
            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            ptable.addCell(pcell);
            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "Dock Receipts", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_RIGHT);
            pcell.addElement(p);
            ptable.addCell(pcell);
            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "  : ", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_LEFT);
            pcell.addElement(p);
            ptable.addCell(pcell);

            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "" + drNumber, blackBoldFontSize6);
            p.setAlignment(Element.ALIGN_LEFT);
            pcell.addElement(p);
            ptable.addCell(pcell);
        }
        //Origin
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "Origin", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "" + originValues.toString(), blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);
        //Destination
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "Destination", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "" + destinationValues.toString(), blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);
        //MasterBL
        if (!drExpFlag && !voyageExpFlag) {
            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            ptable.addCell(pcell);
            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "Master BL#", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_RIGHT);
            pcell.addElement(p);
            ptable.addCell(pcell);

            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "  : ", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_LEFT);
            pcell.addElement(p);
            ptable.addCell(pcell);

            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "" + masterBl, blackBoldFontSize6);
            p.setAlignment(Element.ALIGN_LEFT);
            pcell.addElement(p);
            ptable.addCell(pcell);
        }
        //Destination
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "Vessel", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "" + ssVoyage, blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);
        //S.S. Voyage
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "S.S. Voyage", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "" + exportReference, blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);
        //ContainerNo       
        if (drExpFlag || voyageExpFlag && unitNo != null) {
            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            ptable.addCell(pcell);
            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "Unit", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_RIGHT);
            pcell.addElement(p);
            ptable.addCell(pcell);

            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "  : ", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_LEFT);
            pcell.addElement(p);
            ptable.addCell(pcell);

            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "" + unitNo, blackBoldFontSize6);
            p.setAlignment(Element.ALIGN_LEFT);
            pcell.addElement(p);
            ptable.addCell(pcell);
        } else {
            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            ptable.addCell(pcell);
            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "Unit", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_RIGHT);
            pcell.addElement(p);
            ptable.addCell(pcell);

            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "  : ", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_LEFT);
            pcell.addElement(p);
            ptable.addCell(pcell);

            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "" + containerNo, blackBoldFontSize6);
            p.setAlignment(Element.ALIGN_LEFT);
            pcell.addElement(p);
            ptable.addCell(pcell);
        }
        if (drImpFlag) {
            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            ptable.addCell(pcell);
            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "Sub-House BL", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_RIGHT);
            pcell.addElement(p);
            ptable.addCell(pcell);

            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            p = new Paragraph(9f, "  : ", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_LEFT);
            pcell.addElement(p);
            ptable.addCell(pcell);

            pcell = new PdfPCell();
            pcell.setBorder(0);
            pcell.setPadding(0f);
            if (CommonUtils.isNotEmpty(subHouseBl)) {
                p = new Paragraph(9f, "" + subHouseBl, blackBoldFontSize6);
            } else {
                p = new Paragraph(9f, "", blackBoldFontSize6);
            }
            p.setAlignment(Element.ALIGN_LEFT);
            pcell.addElement(p);
            ptable.addCell(pcell);
        }
        cell.addElement(ptable);
        mainTable.addCell(cell);
        //dr level charge
        if (voyageImpFlag == true) {
            if (fileMap != null && fileMap.size() > 0) {
                for (String key : fileMap.keySet()) {
                    List<ImportsManifestBean> drChargesList = (List) fileMap.get(key);
                    Paragraph p1 = null;
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    cell.setBorderWidthLeft(0.06f);
                    cell.setBorderWidthRight(0.06f);
                    cell.setColspan(4);
                    PdfPTable ntable = new PdfPTable(3);
                    PdfPCell ncell = null;
                    ntable.setWidthPercentage(100f);
                    ntable.setWidths(new float[]{1f, 1f, 2f});
                    if (drChargesList != null && !drChargesList.isEmpty()) {
                        ncell = new PdfPCell();
                        ncell.setBorder(0);
                        p1 = new Paragraph(5f, "" + key, blackFontForFclAr);
                        p1.setAlignment(Element.ALIGN_LEFT);
                        ncell.addElement(p1);
                        ntable.addCell(ncell);

                        ncell = new PdfPCell();
                        ncell.setBorder(0);
                        ncell.setPaddingLeft(-55f);
                        for (Object object : drChargesList) {
                            ImportsManifestBean importsManifestBean = (ImportsManifestBean) object;
                            if (importsManifestBean.getSubHouseBl() != null) {
                                p1 = new Paragraph(5f, "" + importsManifestBean.getSubHouseBl(), blackFontForFclAr);
                            } else {
                                p1 = new Paragraph(5f, "", blackFontForFclAr);
                            }
                            break;
                        }
                        p1.setAlignment(Element.ALIGN_LEFT);
                        ncell.addElement(p1);
                        ntable.addCell(ncell);

                        ncell = new PdfPCell();
                        ncell.setBorder(0);
                        ncell.setPaddingLeft(-35f);
                        for (Object object : drChargesList) {
                            ImportsManifestBean importsManifestBean = (ImportsManifestBean) object;
                            p1 = new Paragraph(5f, "" + importsManifestBean.getFinalDestination(), blackFontForFclAr);
                            break;
                        }
                        p1.setAlignment(Element.ALIGN_LEFT);
                        ncell.addElement(p1);
                        ntable.addCell(ncell);

                        int count = 0;
                        Double totalAmount = 0.00;
                        for (Object object : drChargesList) {
                            ImportsManifestBean importsManifestBean = (ImportsManifestBean) object;
                            if (CommonUtils.isNotEmpty(arRedInvoice.getId()) && arRedInvoice.getId() > 0) {
                                totalAmount = importsManifestBean.getTotalCharges();
                            } else {
                                totalAmount = importsManifestBean.getTotalCharges();
                            }
                            count++;
                            ncell = new PdfPCell();
                            ncell.setBorder(0);
                            if (count == 1) {
                                if (CommonUtils.isNotEmpty(importsManifestBean.getFileNo())) {
                                    p1 = new Paragraph(5f, "", blackBoldFontSize6);
                                } else {
                                    p1 = new Paragraph(5f, "", blackBoldFontSize6);
                                }
                            } else {
                                p1 = new Paragraph(5f, "", blackBoldFontSize6);
                            }
                            ncell.addElement(p1);
                            ntable.addCell(ncell);

                            ncell = new PdfPCell();
                            ncell.setBorder(0);
                            String chargeCodeDesc = "";
                            chargeCodeDesc = genericCodeDAO.getGenericCodeDesc(importsManifestBean.getChargeCode());
                            if (CommonUtils.isNotEmpty(chargeCodeDesc)) {
                                p1 = new Paragraph(5f, "" + chargeCodeDesc, blackBoldFontSize6);
                            } else {
                                p1 = new Paragraph(5f, "" + importsManifestBean.getChargeCode(), blackBoldFontSize6);
                            }
                            ncell.addElement(p1);
                            ntable.addCell(ncell);

                            ncell = new PdfPCell();
                            ncell.setBorder(0);
                            p1 = new Paragraph(5f, "$" + NumberUtils.convertToTwoDecimal(totalAmount), blackBoldFontSize6);
                            ncell.addElement(p1);
                            ntable.addCell(ncell);
                        }
                    }
                    cell.addElement(ntable);
                    mainTable.addCell(cell);
                }
            }
        }
        PdfPTable chargesTable = makeTable(4);
        chargesTable.setWidthPercentage(100.5f);
        chargesTable.setWidths(new float[]{45, 35, 5, 15});
        cell = makeCell("CHARGES", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setColspan(4);
        chargesTable.addCell(cell);
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        String code = "";
        double totalCharges = 0.00;
        double lateFee = 0.00;
        double payAmount = 0.00;
        int chargeCount = 0;
        if (lineItemList != null && !lineItemList.isEmpty()) {
            for (Object object : lineItemList) {
                chargeCount++;
                String codeDesc = "";
                ArRedInvoiceCharges arRedInvoiceCharges = (ArRedInvoiceCharges) object;
                code = CommonUtils.isNotEmpty(arRedInvoiceCharges.getChargeCode()) ? arRedInvoiceCharges.getChargeCode() : "";
                codeDesc = genericCodeDAO.getGenericCodeDesc(code);
                cell = new PdfPCell();
                cell.setBorder(0);
                if (CommonUtils.isNotEmpty(arRedInvoiceCharges.getDescription())) {
                    String desc = arRedInvoiceCharges.getDescription().toUpperCase();
                    if (desc.length() > 40) {
                        desc = desc.substring(0, 40);
                    }
                    p = new Paragraph(6f, "" + desc, blackFontForFclBl);
                } else {
                    p = new Paragraph(6f, "", blackFontForFclBl);
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                chargesTable.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                if (CommonUtils.isNotEmpty(codeDesc)) {
                    p = new Paragraph(6f, "" + codeDesc, blackFontForFclBl);
                } else {
                    p = new Paragraph(6f, "" + code, blackFontForFclBl);
                }
                p.setAlignment(Element.ALIGN_RIGHT);
                cell.addElement(p);
                chargesTable.addCell(cell);
                cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);
                if (chargeCount == 1) {
                    cell.setBorderWidthTop(0.0f);
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                } else {
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                }
                chargesTable.addCell(cell);
                cell = makeCell(number.format(arRedInvoiceCharges.getAmount()), Element.ALIGN_RIGHT, blackFontForFclBl, Rectangle.BOX);
                if (chargeCount == 1) {
                    cell.setBorderWidth(0.0f);
                } else {
                    cell.setBorderWidthLeft(0.0f);
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                }
                chargesTable.addCell(cell);
                totalCharges += arRedInvoiceCharges.getAmount() != null ? arRedInvoiceCharges.getAmount() : 0.00;
            }
        }
        for (int i = 0; i < (14 - chargeCount); i++) {
            chargesTable.addCell(makeCellLeftNoBorderFclBL(""));
            chargesTable.addCell(makeCellRightNoBorderFclBL(""));
            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            chargesTable.addCell(cell);
            cell = makeCell("", Element.ALIGN_RIGHT, blackFontForFclBl, 0.06f);
            cell.setBorderWidthLeft(0.0f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            cell.setMinimumHeight(10f);
            chargesTable.addCell(cell);
        }
        cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell("INVOICE TOTAL ($-USD)", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell(number.format(totalCharges), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setColspan(4);
        chargesTable.addCell(cell);
        chargesTable.setKeepTogether(true);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(chargesTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);

        payAmount = totalCharges;
        String acctNumber = checkPayment(arRedInvoice);
        boolean lateFeeFlag = false;
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        TradingPartner tradingPartner = null;
        String lateFeeDate = "";
        PdfPTable paidTable = makeTable(5);
        paidTable.setWidthPercentage(100.5f);
        paidTable.setWidths(new float[]{45, 25, 10, 5, 15});
        if (CommonFunctions.isNotNull(acctNumber) && !acctNumber.equals("") && !acctNumber.equals("noCredit")) {
            tradingPartner = tradingPartnerBC.findTradingPartnerById(acctNumber);
            if (CommonFunctions.isNotNullOrNotEmpty(tradingPartner.getAccounting())) {
                for (Iterator accountingList = tradingPartner.getAccounting().iterator(); accountingList.hasNext();) {
                    CustomerAccounting customerAccounting = (CustomerAccounting) accountingList.next();
                    if (null != customerAccounting.getLclApplyLateFee() && customerAccounting.getLclApplyLateFee().equals("on")) {
                        if (arRedInvoice.getDueDate() != null) {
                            String dt = "";
                            int creditTerm = Integer.parseInt(customerAccounting.getCreditRate().getCode());
                            Calendar c = Calendar.getInstance();
                            c.setTime(arRedInvoice.getDueDate());
                            c.add(Calendar.DATE, creditTerm);
                            dt = simpDate.format(c.getTime());
                            lateFeeDate = dt;
                            lateFeeFlag = true;
                        }
                    }
                    break;
                }
            }
        }

        if (lateFeeFlag) {
            lateFee = totalCharges * 0.015; // 1.5percent calculate
            payAmount = totalCharges + lateFee;
            cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0);
            paidTable.addCell(cell);
            cell = makeCell("LATE FEE IF NOT PAID BY - ", Element.ALIGN_LEFT, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthRight(0.06f);
            paidTable.addCell(cell);
            cell = makeCell(lateFeeDate, Element.ALIGN_LEFT, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.06f);
            paidTable.addCell(cell);
            cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthLeft(0.06f);
            paidTable.addCell(cell);
            cell = makeCell(number.format(lateFee), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.06f);
            paidTable.addCell(cell);
        }
        cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 1);
        paidTable.addCell(cell);
        cell = makeCell("PLEASE PAY THIS AMOUNT ", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setPaddingLeft(-12f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setColspan(2);
        paidTable.addCell(cell);
        cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        paidTable.addCell(cell);
        cell = makeCell(number.format(payAmount), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        paidTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setColspan(5);
        paidTable.addCell(cell);
        //paidTable.setKeepTogether(true);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(paidTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);

        String paymentStatment = "";
        paymentStatment = checkPayment(arRedInvoice);
        PdfPTable commandTable = new PdfPTable(1);
        commandTable.setWidthPercentage(100);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        if (lateFeeFlag == false) {//if(paymentStatment.equals("noCredit") || lateFeeFlag == false){
            cell = makeCell("THIS INVOICE IS DUE UPON RECEIPT", Element.ALIGN_CENTER, new Font(Font.HELVETICA, 10, Font.BOLDITALIC, Color.BLACK), Rectangle.NO_BORDER);
            commandTable.addCell(cell);
        } else {
            cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
            commandTable.addCell(cell);
        }
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(commandTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setExtraParagraphSpace(10f);
        mainTable.addCell(cell);

        PdfPTable bankDetailsTable = new PdfPTable(3);
        bankDetailsTable.setWidthPercentage(100.5f);
        bankDetailsTable.setWidths(new float[]{25, 50, 25});
        PdfPTable bankDetails = new PdfPTable(1);
        bankDetails.setWidthPercentage(100f);

        //
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        String eftBank = systemRulesDAO.getSystemRulesByCode("EFTBank");
        String eftBankAddress = systemRulesDAO.getSystemRulesByCode("EFTBankAddress");
        String eftAcctName = systemRulesDAO.getSystemRulesByCode("EFTAcctName");
        String eftAccountNo = systemRulesDAO.getSystemRulesByCode("EFTAccountNo");
        String eftABANo = systemRulesDAO.getSystemRulesByCode("EFTABANo");

        bankDetailsTable.setKeepTogether(true);
        bankDetailsTable.addCell(makeCellLeftNoBorder(""));
        StringBuilder bankHead = new StringBuilder();
        bankHead.append(" PAYMENTS VIA WIRE TRANSFER SHOULD BE SENT ");
        cell = makeCell(bankHead.toString(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        bankDetailsTable.addCell(cell);
        bankDetailsTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0));

        bankDetailsTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        cell = makeCell("AS FOLLOWS: ", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        bankDetailsTable.addCell(cell);
        bankDetailsTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("BANK: " + eftBank, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell(eftBankAddress, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("ACCT NAME: " + eftAcctName, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("Acct no.: " + eftAccountNo, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("ABA Routing no.: " + eftABANo, Element.ALIGN_CENTER, blackBoldFontSize6, 0));

        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        //  cell.setBorderWidthBottom(0.06f);
        bankDetailsTable.addCell(cell);
        cell = new PdfPCell();
        cell.addElement(bankDetails);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        //  cell.setBorderWidthBottom(0.06f);
        bankDetailsTable.addCell(cell);

        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        // cell.setBorderWidthBottom(0.06f);
        bankDetailsTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.addElement(bankDetailsTable);
        mainTable.addCell(cell);

        document.add(mainTable);

//                List disclaimerList = new GenericCodeDAO().getAllCommentCodesForInvoiceReports();
//                if(CommonUtils.isNotEmpty(disclaimerList)){
//                    PdfPTable disclaimerTable = makeTable(1);
//                    disclaimerTable.setWidthPercentage(100);
//                    cell=makeCellLeftNoBorder("");
//                    cell.setBorderWidthLeft(0.6f);
//                    cell.setBorderWidthRight(0.6f);
//                    disclaimerTable.addCell(cell);
//                    for (int i = 0; i < disclaimerList.size(); i++) {
//                        cell=makeCellLeftNoBorder("-" + String.valueOf(disclaimerList.get(i)));
//                        cell.setBorderWidthLeft(0.6f);
//                        cell.setBorderWidthRight(0.6f);
//                        disclaimerTable.addCell(cell);
//                    }
//                    cell=makeCellLeftNoBorder("");
//                    cell.setBorderWidthLeft(0.6f);
//                    cell.setBorderWidthRight(0.6f);
//                    cell.setBorderWidthBottom(0.6f);
//                    disclaimerTable.addCell(cell);
//                    document.add(disclaimerTable);
//                }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            //---------------
            //this for print page number at the bottom in the format x of y
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            String invoiceNO = " Invoice# " + (CommonUtils.isNotEmpty(arRedInvoice.getInvoiceNumber()) ? arRedInvoice.getInvoiceNumber() : "");
            String text = "Page " + writer.getPageNumber() + " of ";
            float textBase = document.bottom() - (document.bottomMargin() - 30);
            //float textBase = document.bottom() - 20;
            float textSize = helv.getWidthPoint(text, 12);
            cb.beginText();
            cb.setFontAndSize(helv, 7);
            cb.setTextMatrix(document.left(), textBase);
            cb.showText(invoiceNO);
            cb.setTextMatrix(document.left() + 280, textBase);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(total, document.left() + 260 + textSize, textBase);
            cb.restoreState();
            BaseFont helv;
            PdfGState gstate;
            String waterMark = "";
            if (arRedInvoice != null && arRedInvoice.getStatus() != null && !arRedInvoice.getStatus().equalsIgnoreCase("AR")) {
                waterMark = "DRAFT";
            } else {
                waterMark = "";
            }
            gstate = new PdfGState();
            gstate.setFillOpacity(0.10f);
            gstate.setStrokeOpacity(0.3f);
            PdfContentByte contentunder = writer.getDirectContent();
            contentunder.saveState();
            contentunder.setGState(gstate);
            contentunder.beginText();
            try {
                helv = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI,
                        BaseFont.NOT_EMBEDDED);
            } catch (Exception e) {
                log.info("onEndPage failed on " + new Date(), e);
                throw new ExceptionConverter(e);
            }
            contentunder.setFontAndSize(helv, 150);
            contentunder.showTextAligned(Element.ALIGN_CENTER, waterMark,
                    document.getPageSize().getWidth() / 2, document.getPageSize().getHeight() / 2, 45);
            contentunder.endText();
            contentunder.restoreState();
        } catch (Exception e) {
            log.info("onEndPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    public void destroy() {
        document.close();
    }

    /**
     * @param arInvoice
     * @param fileName
     * @param contextPath
     * @return
     */
    public String createReport(ArRedInvoice arRedInvoice, String fileName, String contextPath, User loginUser,
            List<ArRedInvoiceCharges> arRedInvoiceChargesList, Map<String, List<ImportsManifestBean>> fileMap, boolean sessionFlag) throws Exception {
        this.initialize(fileName, contextPath, arRedInvoice);
        this.createBody(arRedInvoice, contextPath, loginUser, arRedInvoiceChargesList, fileMap, sessionFlag);
        this.destroy();
        return "fileName";
    }

    public PdfPTable fillMarksAndContinerInformation(PdfPTable particularsTable, LclBooking lclBooking, LclBl lclBl, String importFlag)
            throws Exception {
        PdfPCell cell = null;
        NumberFormat numberFormat = new DecimalFormat("###,###,##0.000");
        FclBl bl = null;
        Set<FclBlContainer> containerSet = null;
        HashMap hashMap = new HashMap();
        List TempList = new ArrayList();
        List shortedContainerList = new ArrayList();
        List containerList = new ArrayList();
        if (containerSet != null) {
            for (Iterator iter = containerSet.iterator(); iter.hasNext();) {
                FclBlContainer fclBlCont = (FclBlContainer) iter.next();
                if (!"D".equalsIgnoreCase(fclBlCont.getDisabledFlag())) {
                    hashMap.put(fclBlCont.getTrailerNoId(), fclBlCont);
                    TempList.add(fclBlCont.getTrailerNoId());
                }
            }
        }
        Collections.sort(TempList);
        int containerSize = 0;
        for (int i = 0; i < TempList.size(); i++) {
            FclBlContainer fclBlCont = (FclBlContainer) hashMap.get(TempList.get(i));
            containerList.add(fclBlCont);
        }
        if (containerList != null && containerList.size() > 0) {
            shortedContainerList = OrderContainerList(containerList);
            containerSize = shortedContainerList.size();
        }
        int count = 0;
        if ("I".equalsIgnoreCase(importFlag)) {
            for (Iterator iter = shortedContainerList.iterator(); iter.hasNext();) {
                FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
                List<FclBlMarks> fclMarksList = new ArrayList();
                FclBlContainerDAO fclBlContainerDAO = new FclBlContainerDAO();
                fclMarksList = fclBlContainerDAO.getPakagesDetails(fclBlContainer.getTrailerNoId());
                if (fclMarksList != null && !fclMarksList.isEmpty()) {
                    fclBlContainer.setFclMarksList(fclMarksList);
                    List arrayList = fclBlContainer.getFclMarksList();
                    for (Iterator iterator1 = arrayList.iterator(); iterator1.hasNext();) {
                        count++;
                        FclBlMarks fclBlmarks = (FclBlMarks) iterator1.next();
                        StringBuilder stcAndPackages = new StringBuilder();
                        if (null != bl.getTotalContainers() && bl.getTotalContainers().equalsIgnoreCase("Yes")) {
                            stcAndPackages.append(null != fclBlmarks.getNoOfPkgs() && fclBlmarks.getNoOfPkgs() != 0 ? fclBlmarks.getNoOfPkgs() : "");
                            stcAndPackages.append(" ");
                            stcAndPackages.append(null != fclBlmarks.getUom() ? fclBlmarks.getUom() : "");
                        }
                        cell = makeCellLeftNoBorderFontSize6(stcAndPackages.toString());
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBorderWidthRight(0.6f);
                        particularsTable.addCell(cell);
                        double netWeightLBS = fclBlmarks.getNetweightLbs() != null ? fclBlmarks.getNetweightLbs() : 0.00;
                        if (netWeightLBS != 0.00) {
                            if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                cell = makeCellLeftNoBorderFontSize6(CommonUtils.removeTrailingZeros("" + netWeightLBS) + " LBS");
                            } else {
                                cell = makeCellLeftNoBorderFontSize6((numberFormat.format(netWeightLBS).toString()) + " LBS");
                            }
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setBorderWidthRight(0.6f);
                            cell.setNoWrap(true);

                        } else {
                            cell = makeCellLeftNoBorderFclBL("");
                            cell.setBorderWidthRight(0.06f);
                        }
                        particularsTable.addCell(cell);
                        double measureCFT = fclBlmarks.getMeasureCft() != null ? fclBlmarks.getMeasureCft() : 0.00;
                        StringBuilder cft = new StringBuilder();
                        if (measureCFT != 0.00) {
                            if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                cell = makeCellLeftNoBorderFontSize6(CommonUtils.removeTrailingZeros("" + measureCFT) + " CFT");
                            } else {
                                cell = makeCellLeftNoBorderFontSize6((numberFormat.format(measureCFT).toString()) + " CFT");
                            }
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setBorderWidthRight(0.6f);
                            cell.setNoWrap(true);
                        } else {
                            cell = makeCellLeftNoBorderFclBL("");
                            cell.setBorderWidthRight(0.6f);
                        }
                        particularsTable.addCell(cell);
                        if (count == 1) {
                            cell = makeCellLeftNoBorderFontSize6(bl.getTerminal());
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setBorderWidthRight(0.06f);
                            particularsTable.addCell(cell);
                            cell = makeCellLeftNoBorderFontSize6(bl.getFinalDestination());
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            particularsTable.addCell(cell);
                        } else {
                            cell = makeCellLeftNoBorderFontSize6("");
                            cell.setBorderWidthRight(0.06f);
                            particularsTable.addCell(cell);
                            particularsTable.addCell(makeCellLeftNoBorderFontSize6(""));
                        }
                    }
                }
            }
            if (count == 0) {
                cell = makeCellLeftNoBorderFontSize6("");
                cell.setBorderWidthRight(0.06f);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6("");
                cell.setBorderWidthRight(0.06f);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6("");
                cell.setBorderWidthRight(0.06f);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6(bl.getTerminal());
                cell.setBorderWidthRight(0.06f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6(bl.getFinalDestination());
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                particularsTable.addCell(cell);
            }
        } else {
            String bookingNo = "";
            StringBuilder stringBuilder = new StringBuilder();
            for (Iterator iter = shortedContainerList.iterator(); iter.hasNext();) {
                FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
                if (null != fclBlContainer.getTrailerNo() && !"".equals(fclBlContainer.getTrailerNo())) {
                    count++;
                    stringBuilder.append(fclBlContainer.getTrailerNo());
                    stringBuilder.append(",");
                }
            }
            cell = makeCellLeftNoBorderFontSize6(stringBuilder.toString());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidthRight(0.6f);
            particularsTable.addCell(cell);

            if (bl != null && null != bl.getBookingNo() && !"".equals(bl.getBookingNo())) {
                bookingNo = bl.getBookingNo();
            } else {
                bookingNo = "";
            }
            cell = makeCellLeftNoBorderFontSize6(bookingNo.toString());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidthRight(0.6f);
            particularsTable.addCell(cell);

            if (count > 0) {
                cell = makeCellLeftNoBorderFontSize6(bl.getTerminal());
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorderWidthRight(0.6f);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6(bl.getFinalDestination());
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                particularsTable.addCell(cell);
            } else {
                cell = makeCellLeftNoBorderFontSize6("");
                cell.setBorderWidthRight(0.6f);
                particularsTable.addCell(cell);
                particularsTable.addCell(makeCellLeftNoBorderFontSize6(""));
            }
            if (count == 0) {
                cell = makeCellLeftNoBorderFontSize6("");
                cell.setBorderWidthRight(0.6f);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6("");
                cell.setBorderWidthRight(0.6f);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6(lclUtils.getBlConcatenatedPortOfOrigin(lclBl));
                cell.setBorderWidthRight(0.6f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6(blUtils.getBLConcatenatedFinalDestination(lclBl));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                particularsTable.addCell(cell);
            }
        }
        return particularsTable;
    }

    public PdfPTable fillMarksAndVoyageContinerInformation(PdfPTable particularsTable, String unitNumber, String originValues, String destinationValues)
            throws Exception {
        PdfPCell cell = null;
        cell = makeCellLeftNoBorderFontSize6(unitNumber);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.06f);
        particularsTable.addCell(cell);

        cell = makeCellLeftNoBorderFontSize6("");
        cell.setBorderWidthRight(0.06f);
        particularsTable.addCell(cell);
        cell = makeCellLeftNoBorderFontSize6(originValues.toString().toUpperCase());
        cell.setBorderWidthRight(0.06f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        particularsTable.addCell(cell);
        cell = makeCellLeftNoBorderFontSize6(destinationValues.toString().toUpperCase());
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        particularsTable.addCell(cell);
        return particularsTable;
    }

    public PdfPTable fillBookingInformation(PdfPTable particularsTable, LclBooking lclBooking, MessageResources messageResources, String importFlag)
            throws DocumentException {
        PdfPCell cell = null;
        int count = 0;
        if ("I".equalsIgnoreCase(importFlag)) {
            cell = makeCellLeftNoBorderFontSize6("");
            cell.setBorderWidthRight(0.06f);
            particularsTable.addCell(cell);

            cell = makeCellLeftNoBorderFclBL("");
            cell.setBorderWidthRight(0.06f);
            particularsTable.addCell(cell);

            cell = makeCellLeftNoBorderFclBL("");
            cell.setBorderWidthRight(0.06f);
            particularsTable.addCell(cell);

        } else {
            String bookingNo = "";
            cell = makeCellLeftNoBorderFontSize6("");
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidthRight(0.06f);
            particularsTable.addCell(cell);

            if (null != lclBooking.getLclFileNumber().getFileNumber() && !"".equals(lclBooking.getLclFileNumber().getFileNumber())) {
                bookingNo = lclBooking.getLclFileNumber().getFileNumber();
            } else {
                bookingNo = "";
            }
            cell = makeCellLeftNoBorderFontSize6(bookingNo.toString());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidthRight(0.06f);
            particularsTable.addCell(cell);

        }
        cell = makeCellLeftNoBorderFontSize6(null != lclUtils.getConcatenatedPortOfOrigin(lclBooking) ? lclUtils.getConcatenatedPortOfOrigin(lclBooking) : "");
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.06f);
        particularsTable.addCell(cell);

        cell = makeCellLeftNoBorderFontSize6(null != lclUtils.getConcatenatedFinalDestination(lclBooking) ? lclUtils.getConcatenatedFinalDestination(lclBooking) : "");
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        particularsTable.addCell(cell);
        return particularsTable;
    }

    public List OrderContainerList(List containerList) throws Exception {
        List OrderedContainerList = new ArrayList();
        List<String> legentSize = new ArrayList();
        legentSize = new GenericCodeDAO().getUnitCostTypeListInOrder();
        for (Iterator it = legentSize.iterator(); it.hasNext();) {
            String legentName = (String) it.next();
            for (Iterator iter = containerList.iterator(); iter.hasNext();) {
                FclBlContainer fclBlContainer = (FclBlContainer) iter.next();
                String fcllegentName = fclBlContainer.getSizeLegend().getCodedesc();
                if (legentName.equals(fcllegentName)) {
                    OrderedContainerList.add(fclBlContainer);
                }
            }
        }
        return OrderedContainerList;
    }

    public String checkPayment(ArRedInvoice arRedInvoice) throws Exception {
        String returnString = "noCredit";
//        if (bl.getBillToCode() != null && bl.getBillToCode().equals("F")) {
//            customerNumber = bl.getForwardAgentNo();
//        } else if (bl.getBillToCode() != null && bl.getBillToCode().equals("S")) {
//            customerNumber = bl.getShipperNo();
//        } else if (bl.getBillToCode() != null && bl.getBillToCode().equals("T")) {
//            customerNumber = bl.getBillTrdPrty();
//        } else if (bl.getBillToCode() != null && bl.getBillToCode().equals("A")) {
//            customerNumber = bl.getAgentNo();
//        }
        if (arRedInvoice.getCustomerNumber() != null && !arRedInvoice.getCustomerNumber().isEmpty()) {
            FclBlBC blBC = new FclBlBC();
            String creditDetail = blBC.validateCustomer(arRedInvoice.getCustomerNumber(), "");
            if (creditDetail != null && !creditDetail.equals("")) {
                String[] chargecode = creditDetail.split("==");
                String crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                if (crditWarning.equals("In Good Standing ") || crditWarning.equals("Credit Hold ")) {
                    returnString = arRedInvoice.getCustomerNumber();
                } else {
                    returnString = "noCredit";
                }
            }
        }
        return returnString;
    }
    private void setBrand(ArRedInvoice arRedInvoice) throws Exception {
        String brand = "", property ="";
        if ("LCLI DR".equalsIgnoreCase(arRedInvoice.getScreenName())) {
            brand = new LclFileNumberDAO().getBusinessUnit(arRedInvoice.getFileNo());
        } else if ("IMP VOYAGE".equalsIgnoreCase(arRedInvoice.getScreenName())) {
            brand = new TradingPartnerDAO().getBusinessUnit(new LclUnitSsDAO().getUnitOriginAcct(arRedInvoice.getFileNo()));
        }
        if (CommonUtils.isNotEmpty(brand)) {
            property = brand.equalsIgnoreCase("ECI") ? "application.Econo.companyname"
                            : brand.equalsIgnoreCase("OTI") ? "application.OTI.companyname" : "application.ECU.companyname";
            this.companyName = LoadLogisoftProperties.getProperty(property);
            this.imagePath = "ECI,OTI".contains(brand) ? LoadLogisoftProperties.getProperty("application.image.econo.logo") : LoadLogisoftProperties.getProperty("application.image.logo");
        } else {
            this.companyName = null != LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName")
                    ? LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName").toUpperCase() : "OTI cargo Inc";
            this.imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        }
    }
}
