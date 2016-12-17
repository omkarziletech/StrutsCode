package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.BlUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrectionCharge;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.accounting.model.CompanyModel;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LclExportNewFormatFreightInvoice extends ReportFormatMethods {

    private String fileNumberId;
    private String realPath;
    private Document document = null;
    private PdfWriter pdfWriter = null;
    protected BaseFont helv;
    private PdfPTable table = null;
    private PdfPCell cell = null;
    private Paragraph paragraph = null;
    private String companyAddress, companyPhone, companyFax;
    private LclBl lclbl;
    private CompanyModel company;
    private String companyName = "", webSite = "";
    private TradingPartner tradingPartner = null;
    private String documentBillTo;

    public String getDocumentBillTo() {
        return documentBillTo;
    }

    public void setDocumentBillTo(String documentBillTo) {
        this.documentBillTo = documentBillTo;
    }

    public LclExportNewFormatFreightInvoice() throws Exception {
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
        companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
        companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");
        company = new SystemRulesDAO().getCompanyDetails();
    }

    public void createReport(String realPath, String outputFileName, String documentName, String fileId) throws Exception {
        this.realPath = realPath;
        this.fileNumberId = fileId;
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        lclbl = new LCLBlDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
        new LCLBlDAO().getCurrentSession().evict(lclbl);
        document.open();
        headerPage();
        customerTable();
        document.close();
    }

    public void headerPage() throws Exception {
        PdfPCell cell = new PdfPCell();
        PdfPTable headingMainTable = new PdfPTable(1);
        headingMainTable.setWidthPercentage(100);
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.setWidths(new float[]{100});
        PdfPTable imgTable = new PdfPTable(1);
        imgTable.setWidthPercentage(100);

        String brand = new LclFileNumberDAO().getBusinessUnit(this.fileNumberId);
        webSite = LoadLogisoftProperties.getProperty("ECI".equalsIgnoreCase(brand)
                ? "application.Econo.website" : "OTI".equalsIgnoreCase(brand) ? "application.OTI.website" : "application.ECU.website");
        companyName = LoadLogisoftProperties.getProperty("ECI".equalsIgnoreCase(brand)
                ? "application.Econo.companyname" : "OTI".equalsIgnoreCase(brand) ? "application.OTI.companyname" : "application.ECU.companyname");

        String logoImage = LoadLogisoftProperties.getProperty("ECU".equalsIgnoreCase(brand)
                ? "application.image.logo" : "application.image.econo.logo");

        Image img = Image.getInstance(realPath + logoImage);
        img.scalePercent(75);
        PdfPCell logoCell = new PdfPCell(img);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_LEFT);
        logoCell.setVerticalAlignment(com.lowagie.text.Element.ALIGN_LEFT);
        logoCell.setPaddingLeft(+27);
        imgTable.addCell(logoCell);
        PdfPTable addrTable = new PdfPTable(1);
        addrTable.setWidthPercentage(100);
        PdfPTable addressTable = new PdfPTable(3);
        addressTable.setWidthPercentage(100);
        addressTable.setWidths(new float[]{40, 20, 40});
        StringBuilder stringBuilder = new StringBuilder();
        addrTable.addCell(makeCellCenterNoBorderFclBL("MAILING ADDRESS: " + (CommonUtils.isNotEmpty(companyAddress)
                ? companyAddress.toUpperCase() : "")));
        stringBuilder.append("TEL: ");
        stringBuilder.append(CommonUtils.isNotEmpty(companyPhone) ? companyPhone : "").append(" / ");
        stringBuilder.append("FAX: ");
        stringBuilder.append(CommonUtils.isNotEmpty(companyFax) ? companyFax : "");
        addrTable.addCell(makeCellCenterNoBorderFclBL(stringBuilder.toString()));
        addrTable.addCell(makeCellLeftNoBorderFclBL(""));
        addrTable.addCell(makeCellLeftNoBorderFclBL(""));
        addressTable.addCell(makeCellLeftNoBorderFclBL(""));
        cell = makeCell("INVOICE", Element.ALIGN_CENTER, new Font(Font.HELVETICA, 12, Font.BOLD, Color.RED), 0.06f);
        addressTable.addCell(cell);
        addressTable.addCell(makeCellLeftNoBorderFclBL(""));
        cell = new PdfPCell();
        cell.addElement(addressTable);
        cell.setBorder(0);
        addrTable.addCell(cell);
        addrTable.addCell(makeCellLeftNoBorderFclBL(""));
        addrTable.addCell(makeCellLeftNoBorderFclBL(""));

        cell = new PdfPCell();
        cell.addElement(imgTable);
        cell.setBorder(0);
        cell.setPaddingLeft(+150);
        headingMainTable.addCell(cell);
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
    }

    public void customerTable() throws Exception {

        String billToAcct = "", billToPartyAcctName = "";
        StringBuilder billToPartyAddress = new StringBuilder();
        StringBuilder originValues = new StringBuilder();
        StringBuilder destinationValues = new StringBuilder();
        LclUtils lclUtils = new LclUtils();
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();

        String terminalname = "", terminalAddress = "", terminalZip = "", unitNo = "", masterBl = "";

        Date vesselEtd = new Date();

        String shipperName = null != lclbl.getShipAcct() ? lclbl.getShipContact().getCompanyName() : "";
        String consigneeName = null != lclbl.getConsAcct() ? lclbl.getConsContact().getCompanyName() : "";
        String notifyName = null != lclbl.getNotyAcct() ? lclbl.getNotyContact().getCompanyName() : "";
        String forwarderName = null != lclbl.getFwdAcct() ? lclbl.getFwdContact().getCompanyName() : "";

        String billToParty = null != this.getDocumentBillTo() ? this.getDocumentBillTo() : lclbl.getBillToParty();
        if (billToParty.equalsIgnoreCase("F")) {
            billToAcct = null != lclbl.getFwdAcct() ? lclbl.getFwdAcct().getAccountno() : "";
            billToPartyAcctName = null != lclbl.getFwdAcct() ? lclbl.getFwdAcct().getAccountName() : "";
        } else if (billToParty.equalsIgnoreCase("A")) {
            billToAcct = null != lclbl.getAgentAcct() ? lclbl.getAgentAcct().getAccountno() : "";
            billToPartyAcctName = null != lclbl.getAgentAcct() ? lclbl.getAgentAcct().getAccountName() : "";
        } else if (billToParty.equalsIgnoreCase("S")) {
            billToAcct = null != lclbl.getShipAcct() ? lclbl.getShipAcct().getAccountno() : "";
            billToPartyAcctName = null != lclbl.getShipAcct() ? lclbl.getShipAcct().getAccountName() : "";
        } else if (billToParty.equalsIgnoreCase("T")) {
            billToAcct = null != lclbl.getThirdPartyAcct() ? lclbl.getThirdPartyAcct().getAccountno() : "";
            billToPartyAcctName = null != lclbl.getThirdPartyAcct() ? lclbl.getThirdPartyAcct().getAccountName() : "";
        }

        CustAddress custAddress = new CustAddressDAO().findByAccountNo(billToAcct);
        CustomerAccounting customerAccounting = new CustomerAccountingDAO().findByProperty("accountNo", billToAcct);
        if (custAddress != null) {
            billToPartyAddress.append(billToPartyAcctName).append("\n");
            billToPartyAddress.append(custAddress.getAddress1()).append("\n");
            billToPartyAddress.append(custAddress.getCity1()).append(" ").append(custAddress.getState()).append(" ").append(custAddress.getZip());
        }

        originValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclbl.getPortOfLoading()));
        destinationValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclbl.getPortOfDestination()));
        if (lclbl.getTerminal() != null) {
            terminalname = null != lclbl.getTerminal() ? lclbl.getTerminal().getTerminalLocation() : "";
            terminalAddress = null != lclbl.getTerminal() ? lclbl.getTerminal().getAddres1() : "";
            terminalZip = null != lclbl.getTerminal() ? lclbl.getTerminal().getZipcde() : "";
        }

        LclFileNumber lclFileNumber = lclbl.getLclFileNumber();
        List<LclBookingPiece> lclBookingPieceList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", lclFileNumber.getId());
        if (lclBookingPieceList != null && !lclBookingPieceList.isEmpty()) {
            LclUnitSs lclUnitSs = null;
            for (LclBookingPiece lclBookingPiece : lclBookingPieceList) {
                for (LclBookingPieceUnit pieceUnit : lclBookingPiece.getLclBookingPieceUnitList()) {
                    if (CommonUtils.in(pieceUnit.getLclUnitSs().getLclSsHeader().getServiceType(), "E", "C")) {
                        lclUnitSs = pieceUnit.getLclUnitSs();
                        break;
                    }
                }
            }
            unitNo = null != lclUnitSs ? lclUnitSs.getLclUnit().getUnitNo() : "";
            masterBl = null != lclUnitSs ? lclUnitSs.getSpBookingNo() : "";
            vesselEtd = null != lclUnitSs ? lclUnitSs.getLclSsHeader().getLclSsDetailList().get(0).getStd() : new Date();
        }
        String shipmentFileNumber = new LCLBlDAO().getExportBlNumbering(lclFileNumber.getId().toString());

        String subHouseBl = "";
        String amsHouseBl = new LclBookingImportAmsDAO().getAmsNoGroup(lclFileNumber.getId().toString());

        if (lclFileNumber.getLclBookingImport() != null) {
            subHouseBl = lclFileNumber.getLclBookingImport().getSubHouseBl();
        }

        PdfPTable mainTable = makeTable(2);
        mainTable.setWidthPercentage(100f);
        PdfPTable clientPTable = new PdfPTable(5);
        clientPTable.setWidthPercentage(100f);
        clientPTable.setWidths(new float[]{25, 25, 17, 11, 22});
        clientPTable.setKeepTogether(true);
        cell = makeCell("BILL TO ACCOUNT NO.", Element.ALIGN_LEFT, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell("" + billToAcct, Element.ALIGN_LEFT, blackFontForFclAr, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        clientPTable.addCell(cell);

        cell = makeCell("INVOICE NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell("DATE", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell("BILLING TM", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);

        cell = makeCell("" + billToPartyAddress, Element.ALIGN_LEFT, blackFontForFclAr, 0);
        cell.setColspan(2);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        clientPTable.addCell(cell);

        cell = makeCell("" + lclbl.getLclFileNumber().getFileNumber(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
        clientPTable.addCell(cell);

        if (vesselEtd != null) {
            cell = makeCell(DateUtils.formatStringDateToAppFormatMMM(vesselEtd), Element.ALIGN_CENTER, blackFontForFclBl, 0);
        } else {
            cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        }
        cell.setBorderWidthLeft(0.06f);
        clientPTable.addCell(cell);

        //BILLING TM
        cell = makeCell("" + terminalname, Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.06f);
        clientPTable.addCell(cell);

        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        clientPTable.addCell(cell);

        cell = makeCell("CUSTOMER REF NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setColspan(3);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);

        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        clientPTable.addCell(cell);
        //CUSTOMER REF NO.
        cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setColspan(3);
        cell.setMinimumHeight(15f);
        cell.setBorderWidthLeft(0.06f);
        clientPTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(clientPTable);
        cell.setColspan(5);
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);

        PdfPTable containerTable = makeTable(4);
        containerTable.setWidthPercentage(100f);
        containerTable.setWidths(new float[]{25, 25, 25, 25});
        cell = makeCell("CONTAINER NO.", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);
        cell = makeCell("ECI SHIPMENT FILE NO.", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);
        cell = makeCell("ORIGIN", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);
        cell = makeCell("DESTINATION", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        containerTable.addCell(cell);
        //CONTAINER NO
        cell = makeCell("" + unitNo, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);
        //ECI SHIPMENT FILE NO
        cell = makeCell("" + shipmentFileNumber, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);
        //ORIGIN
        cell = makeCell("" + originValues, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);
        //DESTINATION
        cell = makeCell("" + destinationValues, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);

        cell = makeCell("MBL / AWB NUMBER", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        containerTable.addCell(cell);
        cell = makeCell("AMS HOUSE BL", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        containerTable.addCell(cell);
        cell = makeCell("SUB HOUSE BL", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        containerTable.addCell(cell);
        //MBL / AWB NUMBER
        cell = makeCell(null != masterBl ? masterBl : "", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);
        //amsHouseBl
        cell = makeCell(null != amsHouseBl ? amsHouseBl : "", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);
        //subHouseBl
        cell = makeCell(null != subHouseBl ? subHouseBl : "", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);

        cell = makeCell("SHIPPER", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        containerTable.addCell(cell);
        cell = makeCell("FORWARDER", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        containerTable.addCell(cell);
        //SHIPPER
        cell = makeCell("" + shipperName, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);
        //FORWARDER
        cell = makeCell("" + forwarderName, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);

        cell = makeCell("CONSIGNEE", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        containerTable.addCell(cell);

        cell = makeCell("NOTIFY PARTY", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        containerTable.addCell(cell);
        //CONSIGNEE
        cell = makeCell("" + consigneeName, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);
        //NOTIFY PARTY
        cell = makeCell("" + notifyName, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        containerTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(containerTable);
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);

        Font boldHeadingFon = FontFactory.getFont("Arial", 7f, Font.BOLD);
        Paragraph p = null;

        PdfPTable chargeTableHeading = makeTable(5);
        chargeTableHeading.setWidthPercentage(100f);
        chargeTableHeading.setWidths(new float[]{2f, 1f, 4f, 1.3f, 1.3f});
        //marks     
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        p = new Paragraph(7f, "MARKS AND NUMBERS", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        chargeTableHeading.addCell(cell);
        //no of pkgs
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "NO.OF.PKGS", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        chargeTableHeading.addCell(cell);
        //desc
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "DESCRIPTION OF PACKAGES AND GOODS", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        chargeTableHeading.addCell(cell);
        //grossweight
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "GROSS WEIGHT", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        chargeTableHeading.addCell(cell);
        //measure
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "MEASURE", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        chargeTableHeading.addCell(cell);

        if (CommonUtils.isNotEmpty(lclbl.getLclFileNumber().getLclBlPieceList())) {
            for (LclBlPiece blPiece : lclbl.getLclFileNumber().getLclBlPieceList()) {
                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, CommonUtils.isNotEmpty(blPiece.getMarkNoDesc())
                        ? blPiece.getMarkNoDesc().toUpperCase() : "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                chargeTableHeading.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, CommonUtils.isNotEmpty(blPiece.getActualPieceCount())
                        ? blPiece.getActualPieceCount().toString() : "", blackNormalCourierFont8f);
                p.add(null != blPiece.getPackageType() ? blPiece.getPackageType().getAbbr01() : "");
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                chargeTableHeading.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, CommonUtils.isNotEmpty(blPiece.getActualPieceCount())
                        ? blPiece.getPieceDesc().toUpperCase() : "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                chargeTableHeading.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, !CommonUtils.isEmpty(blPiece.getActualWeightMetric())
                        ? (blPiece.getActualWeightMetric() + "KGS") : "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                chargeTableHeading.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, !CommonUtils.isEmpty(blPiece.getActualVolumeMetric())
                        ? (blPiece.getActualVolumeMetric() + "CBM") : "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                chargeTableHeading.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setColspan(5);
                p = new Paragraph(7f, "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                chargeTableHeading.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setColspan(3);
                p = new Paragraph(7f, "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                chargeTableHeading.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, !CommonUtils.isEmpty(blPiece.getActualWeightImperial())
                        ? (blPiece.getActualWeightImperial() + "LBS") : "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                chargeTableHeading.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, !CommonUtils.isEmpty(blPiece.getActualVolumeImperial())
                        ? (blPiece.getActualVolumeImperial() + "CFT") : "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                chargeTableHeading.addCell(cell);

            }
        }

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.addElement(chargeTableHeading);
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        mainTable.addCell(cell);

        PdfPTable chargesListTable = makeTable(4);
        chargesListTable.setWidthPercentage(100.5f);
        chargesListTable.setWidths(new float[]{45, 35, 5, 15});
        cell = makeCell("DESCRIPTION", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setColspan(2);
        chargesListTable.addCell(cell);
        cell = makeCell("CHARGES", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        chargesListTable.addCell(cell);

        NumberFormat number = new DecimalFormat("###,###,##0.00");

        Map<String, BigDecimal> chargeList = getChargeList(lclbl.getFileNumberId());
        int count = 14;
        double total = 0.00;
        for (Map.Entry key : chargeList.entrySet()) {
            count--;
            String[] separateCharge = key.getKey().toString().split("#");

            cell = makeCell(separateCharge[0], Element.ALIGN_RIGHT, blackFontForFclBl, 0.06f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            cell.setBorderWidthLeft(0.0f);
            cell.setColspan(2);
            chargesListTable.addCell(cell);

            cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            chargesListTable.addCell(cell);

            BigDecimal amount = null != key.getValue() ? new BigDecimal(key.getValue().toString()) : BigDecimal.ZERO;
            cell = makeCell("" + amount, Element.ALIGN_RIGHT, blackFontForFclBl, 0.06f);
            cell.setBorderWidthLeft(0.0f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            cell.setMinimumHeight(10f);
            chargesListTable.addCell(cell);
            total = total + amount.doubleValue();
        }
        for (int i = 0; i < count; i++) {
            cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0.06f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            cell.setBorderWidthLeft(0.0f);
            cell.setColspan(2);
            chargesListTable.addCell(cell);

            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            chargesListTable.addCell(cell);

            cell = makeCell("", Element.ALIGN_RIGHT, blackFontForFclBl, 0.06f);
            cell.setBorderWidthLeft(0.0f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            cell.setMinimumHeight(10f);
            chargesListTable.addCell(cell);
        }
        cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        chargesListTable.addCell(cell);
        cell = makeCell("INVOICE TOTAL", Element.ALIGN_CENTER, blackFontForFclBl, 0);
//        cell.setPaddingLeft(-15f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        chargesListTable.addCell(cell);
        cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        chargesListTable.addCell(cell);
        cell = makeCell(number.format(total), Element.ALIGN_RIGHT, blackFontForFclBl, 0);//4
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        chargesListTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setColspan(4);
        chargesListTable.addCell(cell);
        chargesListTable.setKeepTogether(true);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(chargesListTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);
        String creditSatus = this.getAcctCreditStatus(billToAcct);
        boolean lateFeeFlag = false;
        if (CommonUtils.isNotEmpty(creditSatus) && !creditSatus.equals("") && !creditSatus.equals("noCredit")) {
            if (CommonUtils.isNotEmpty(tradingPartner.getAccounting())) {
                for (CustomerAccounting custAccounting : tradingPartner.getAccounting()) {
                    if (null != custAccounting.getLclApplyLateFee() && custAccounting.getLclApplyLateFee().equals("on")) {
                        lateFeeFlag = true;
                    }
                    break;
                }
            }
        }
        PdfPTable paymentTable = makeTable(6);
        paymentTable.setWidthPercentage(100.5f);
        paymentTable.setWidths(new float[]{30, 15, 25, 10, 5, 15});
        cell = makeCell("SAILING DATE", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        paymentTable.addCell(cell);

        cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        paymentTable.addCell(cell);

        String currentDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        if (customerAccounting != null && customerAccounting.getCreditRate() != null
                && null != vesselEtd) {
            Calendar c = Calendar.getInstance();
            c.setTime(vesselEtd);
            String custAcctCodeDesc = customerAccounting.getCreditRate().getCodedesc();
            int dateLimit = custAcctCodeDesc.equalsIgnoreCase("Net 7 Days") ? 7
                    : custAcctCodeDesc.equalsIgnoreCase("Net 15 Days") ? 15
                            : custAcctCodeDesc.equalsIgnoreCase("Net 21 Days") ? 21
                                    : custAcctCodeDesc.equalsIgnoreCase("Net 30 Days") ? 30
                                            : custAcctCodeDesc.equalsIgnoreCase("Net 45 Days") ? 45
                                                    : custAcctCodeDesc.equalsIgnoreCase("Net 60 Days") ? 60 : 0;
            c.add(Calendar.DATE, dateLimit);
            currentDate = sdf.format(c.getTime());
        }

        if (lateFeeFlag) {
            double payAmount = total;
            double lateFee = total * 0.015;
            payAmount = total + lateFee;

            cell = makeCell("LATE FEE IF NOT PAID BY - " + currentDate, Element.ALIGN_LEFT, blackFontForFclBl, 0);
            cell.setColspan(2);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthLeft(0.06f);
            paymentTable.addCell(cell);

            cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthLeft(0.06f);
            paymentTable.addCell(cell);
            cell = makeCell(number.format(lateFee), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.06f);
            paymentTable.addCell(cell);

            cell = makeCell("" + sdf.format(vesselEtd), Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setMinimumHeight(10f);
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthRight(0.06f);
            paymentTable.addCell(cell);

            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            paymentTable.addCell(cell);

            cell = makeCell("PAY THIS AMOUNT IF NOT PAID BY DUE DATE", Element.ALIGN_LEFT, blackFontForFclBl, 0);
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthTop(0.06f);
            cell.setColspan(2);
            paymentTable.addCell(cell);
            cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthTop(0.06f);
            paymentTable.addCell(cell);
            cell = makeCell(number.format(payAmount), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthTop(0.06f);
            paymentTable.addCell(cell);

        } else {
            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setColspan(4);
            paymentTable.addCell(cell);

            cell = makeCell("" + sdf.format(vesselEtd), Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setMinimumHeight(10f);
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthRight(0.06f);
            paymentTable.addCell(cell);

            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            paymentTable.addCell(cell);

            cell = makeCell("PLEASE PAY THIS AMOUNT - " + "", Element.ALIGN_LEFT, blackFontForFclBl, 0);
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthTop(0.06f);
            cell.setColspan(2);
            paymentTable.addCell(cell);
            cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthTop(0.06f);
            paymentTable.addCell(cell);
            cell = makeCell(number.format(total), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthTop(0.06f);
            paymentTable.addCell(cell);
        }
        cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setColspan(5);
        paymentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(paymentTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);

        PdfPTable commandTable = new PdfPTable(1);
        commandTable.setWidthPercentage(100);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);

        cell = makeCell("INVOICE IS PAYABLE UPON RECEIPT", Element.ALIGN_CENTER, blackFontForFclBl, 0, Color.decode("#FFFF00"));
        commandTable.addCell(cell);

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

        PdfPTable bankDetailsTable = makeTable(4);
        bankDetailsTable.setWidthPercentage(100f);
        bankDetailsTable.setWidths(new float[]{3f, 1f, 4f, 2.6f});
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthTop(0.6f);
        p = new Paragraph(7f, "PAYMENT METHODS", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        bankDetailsTable.addCell(cell);
        // Empty

        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(3);
        cell.setBorderWidthBottom(0.06f);
        bankDetailsTable.addCell(cell);

        //Via Check
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "Via Check", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        bankDetailsTable.addCell(cell);
        //Via ACH or Wire Transfer
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "Via ACH or Wire Transfer", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        bankDetailsTable.addCell(cell);
        //Credit Card Payments
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "Credit Card Payments", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        bankDetailsTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setColspan(2);
        p = new Paragraph(10f, "PLEASE REMIT PAYMENT TO", boldHeadingFon);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);

        p = new Paragraph(10f, companyName, boldHeadingFon);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);

        String creditStatusDomain = "";

        if (null != customerAccounting && null != customerAccounting.getCreditStatus()) {//no credit
            creditStatusDomain = customerAccounting.getCreditStatus().getCodedesc();
            if (!("No Credit").equalsIgnoreCase(creditStatusDomain)) {
                terminalAddress = "2401 N.W. 69TH STREET";
                terminalZip = "33147";
                terminalname = "Miami, FL";
            }
        }
        p = new Paragraph(10f, "" + terminalAddress, boldHeadingFon);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "" + terminalname + "" + terminalZip, boldHeadingFon);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        bankDetailsTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(10f, "Bank: ", boldHeadingFon);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "" + company.getBankAddress(), boldHeadingFon);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "ABA: " + company.getBankAbaNumber(), boldHeadingFon);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "ACCT: " + companyName, boldHeadingFon);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "ACCOUNT NO: " + company.getBankAccountNumber(), boldHeadingFon);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        bankDetailsTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(10f, "If paying via Credit card", boldHeadingFon);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, " Please go to:", boldHeadingFon);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, webSite, fileNoFont);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        bankDetailsTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(4);
        cell.addElement(bankDetailsTable);
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        mainTable.addCell(cell);

        document.add(mainTable);
    }

    public String getAcctCreditStatus(String acctNo) throws Exception {
        tradingPartner = new TradingPartnerDAO().findById(acctNo);
        if (null != tradingPartner && null != tradingPartner.getAccounting()) {
            for (CustomerAccounting custAccounting : tradingPartner.getAccounting()) {
                if (null != custAccounting.getCreditStatus() && null != custAccounting.getCreditStatus().getCodedesc()) {
                    return custAccounting.getCreditStatus().getCodedesc();
                }
                if (null != custAccounting.getOverLimit()
                        && !custAccounting.getOverLimit().equalsIgnoreCase("off")) {
                    return "Over Limit";
                }
            }
        }
        return "";
    }

    public Map<String, BigDecimal> getChargeList(Long fileId) throws Exception {
        Map<String, BigDecimal> chargeMap = new LinkedHashMap<String, BigDecimal>();
        int approvedCorrectionCount = new LCLCorrectionDAO().getApprovedCorrectionCountByFileId(fileId);
        String chargeCode = "", chargeDesc = "", blueCode = "";
        if (approvedCorrectionCount != 0) {
            Object correctionId = new LCLCorrectionDAO().getLastApprovedFieldsByFileId(fileId, "id");
            List<LclCorrectionCharge> correctedChargeList = new LCLCorrectionChargeDAO()
                    .findByProperty("lclCorrection.id", Long.parseLong(correctionId.toString()));
            Iterator iteratorList = correctedChargeList.iterator();
            while (iteratorList.hasNext()) {
                LclCorrectionCharge charge = (LclCorrectionCharge) iteratorList.next();
                if (CommonUtils.isNotEmpty(this.getDocumentBillTo())
                        && !this.getDocumentBillTo().equalsIgnoreCase(charge.getBillToParty())) {
                    iteratorList.remove();
                }
            }
            Collections.reverse(correctedChargeList);
            for (LclCorrectionCharge charge : correctedChargeList) {
                chargeCode = charge.getGlMapping().getChargeCode();
                chargeDesc = CommonUtils.isNotEmpty(charge.getGlMapping().getChargeDescriptions())
                        ? charge.getGlMapping().getChargeDescriptions() : charge.getGlMapping().getChargeCode();
                blueCode = charge.getGlMapping().getBlueScreenChargeCode();

                if (CommonUtils.isNotEmpty(chargeDesc)) {
                    chargeMap.put(chargeDesc + "#" + blueCode, charge.getNewAmount());
                } else {
                    chargeMap.put(chargeCode + "#" + blueCode, charge.getNewAmount());
                }
            }
        } else {
            List<LclBlAc> blAcList = lclbl.getLclFileNumber().getLclBlAcList();
            String eciUnLocCode = lclbl.getFinalDestination() != null
                    ? lclbl.getFinalDestination().getUnLocationCode()
                    : lclbl.getPortOfDestination().getUnLocationCode();
            Ports port = new PortsDAO().getPorts(eciUnLocCode);
            if (lclbl.getLclFileNumber().getLclBlPieceList().size() > 1) {
                blAcList = new BlUtils().getRolledUpChargesForBl(lclbl.getLclFileNumber().getLclBlPieceList(), blAcList, "E", null, true);
            }

            Iterator iteratorList = blAcList.iterator();
            while (iteratorList.hasNext()) {
                LclBlAc charge = (LclBlAc) iteratorList.next();
                if (CommonUtils.isNotEmpty(this.getDocumentBillTo())
                        && !this.getDocumentBillTo().equalsIgnoreCase(charge.getArBillToParty())) {
                    iteratorList.remove();
                }
            }

            for (LclBlAc charge : blAcList) {
                chargeCode = charge.getArglMapping().getChargeCode();
                chargeDesc = CommonUtils.isNotEmpty(charge.getArglMapping().getChargeDescriptions())
                        ? charge.getArglMapping().getChargeDescriptions() : charge.getArglMapping().getChargeCode();
                blueCode = charge.getArglMapping().getBlueScreenChargeCode();
                BigDecimal arAmount = null != charge.getRolledupCharges() ? charge.getRolledupCharges() : charge.getArAmount();
                if (CommonUtils.isNotEmpty(chargeDesc)) {
                    chargeMap.put(chargeDesc + "#" + blueCode, arAmount);
                } else {
                    chargeMap.put(chargeCode + "#" + blueCode, arAmount);
                }
            }
        }
        return chargeMap;
    }

}
