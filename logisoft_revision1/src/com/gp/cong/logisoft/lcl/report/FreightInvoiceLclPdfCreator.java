/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.beans.BookingChargesBean;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.ImportPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.TerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingSegregationDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.accounting.model.CompanyModel;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author NambuRajasekar
 */
public class FreightInvoiceLclPdfCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(FreightInvoiceLclPdfCreator.class);
    private String unitSsId;
    private String realPath;
    protected BaseFont helv;
    Document document = null;
    PdfWriter pdfWriter = null;
    protected PdfTemplate total;
    private LclUtils lclUtils = new LclUtils();
    private CompanyModel company;
    private String companyName="";
    private String webSite="";
    private String fileNumberId;
    
    public String getUnitSsId() {
        return unitSsId;
    }

    public void setUnitSsId(String unitSsId) {
        this.unitSsId = unitSsId;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }
    
    public FreightInvoiceLclPdfCreator() {
    }

    public FreightInvoiceLclPdfCreator(String realPath, String fileId) {
        this.realPath = realPath;
        this.fileNumberId=fileId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public CompanyModel getCompany() {
        return company;
    }

    public void setCompany(CompanyModel company) {
        this.company = company;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(String fileNumberId) {
        this.fileNumberId = fileNumberId;
    }
   
    
    public void createPdf(String realPath, String unitSsId, String fileId, String fileNumber, String outputFileName, String documentName, String voyNotiemailId, User loginUser) throws Exception {
        init(outputFileName, fileId);
        createImportFreightPdf(realPath, unitSsId, fileId, fileNumber, outputFileName, documentName, voyNotiemailId, loginUser);
        document.close();

    }

    public void init(String outputFileName, String fileId) throws FileNotFoundException, DocumentException, Exception {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        pdfWriter.setPageEvent(new FreightInvoiceLclPdfCreator(realPath,fileId));
        company = new SystemRulesDAO().getCompanyDetails();
        this.setBrand(fileId);
        HeaderFooter footer = new HeaderFooter(new Phrase(companyName, new Font(Font.HELVETICA, 10, Font.ITALIC, Color.BLACK)), false);        
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
            Image img =null;
            String logoImage = "";
            String brand =this.setBrand(fileNumberId);
            if (CommonUtils.isNotEmpty(brand)) {
                if ("ECI".equalsIgnoreCase(brand)) {
                    logoImage = LoadLogisoftProperties.getProperty("application.image.econo.logo");
                    img = Image.getInstance(realPath + logoImage);
                    img.scalePercent(75);
                } else if ("OTI".equalsIgnoreCase(brand)) {
                    logoImage = LoadLogisoftProperties.getProperty("application.image.econo.logo");
                    img = Image.getInstance(realPath + logoImage);
                    img.scalePercent(45);
                } else {
                    logoImage = LoadLogisoftProperties.getProperty("application.image.logo");
                    img = Image.getInstance(realPath + logoImage);
                    img.scalePercent(45);
                }
            } 
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

    public void createImportFreightPdf(String realPath, String unitSsId, String fileId, String fileNumber, String outputFileName, String documentName, String voyNotiemailId, User loginUser) throws Exception {
        ImportPortConfigurationDAO importPortConfigurationDAO = new ImportPortConfigurationDAO();
        LclUnitSsDispoDAO lclUnitSsDispoDAO = new LclUnitSsDispoDAO();
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        String trmname = "";
        String trmAddress = "";
        String trmZip = "";
        String customerPo = "";
        String unitNo = "";
        String masterBl = "";
        StringBuilder originValues = new StringBuilder();
        StringBuilder destinationValues = new StringBuilder();
        String subHouseBl = "";
        String amsHouseBl = "";
        String shipName = "";
        String consName = "";
        String notyName = "";
        String forwName = "";
        String billToParty = "";
        String billToPartyAc = "";
        Date pickUpDate = null;
        Date vesselEtd = null;
        String billToPartyAcctName ="";
        StringBuilder consAddress = new StringBuilder();

        LclBooking lclBooking = new LCLBookingDAO().findById(Long.valueOf(fileId));
        shipName = null != lclBooking.getShipAcct() ? lclBooking.getShipContact().getCompanyName() : "";
        consName = null != lclBooking.getConsAcct() ? lclBooking.getConsContact().getCompanyName() : "";
        notyName = null != lclBooking.getNotyAcct() ? lclBooking.getNotyContact().getCompanyName() : "";
        forwName = null != lclBooking.getFwdAcct() ? lclBooking.getFwdAcct().getAccountName() : "";

        billToParty = null != lclBooking.getBillToParty() ? lclBooking.getBillToParty() : "";
        if (billToParty.equalsIgnoreCase("C")) {
            billToPartyAc = null != lclBooking.getConsAcct() ? lclBooking.getConsAcct().getAccountno() : "";
            billToPartyAcctName =  null != lclBooking.getConsAcct() ? lclBooking.getConsContact().getCompanyName() : "";
        } else if (billToParty.equalsIgnoreCase("A")) {
            billToPartyAc = null != lclBooking.getSupAcct() ? lclBooking.getSupAcct().getAccountno() : "";
            billToPartyAcctName =  null != lclBooking.getSupAcct() ? lclBooking.getSupAcct().getAccountName() : "";
        } else if (billToParty.equalsIgnoreCase("N")) {
            billToPartyAc = null != lclBooking.getNotyAcct() ? lclBooking.getNotyAcct().getAccountno() : "";
            billToPartyAcctName =  null != lclBooking.getNotyAcct() ? lclBooking.getNotyContact().getCompanyName() : "";
        } else if (billToParty.equalsIgnoreCase("T")) {
            billToPartyAc = null != lclBooking.getThirdPartyAcct() ? lclBooking.getThirdPartyAcct().getAccountno() : "";
            billToPartyAcctName = null != lclBooking.getThirdPartyAcct() ? lclBooking.getThirdPartyAcct().getAccountName() : "";
        }

        originValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclBooking.getPortOfLoading()));
        destinationValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclBooking.getPortOfDestination()));
        if (lclBooking.getTerminal() != null) {
            RefTerminal terminal = new TerminalDAO().findByTerminalNo(String.valueOf(lclBooking.getTerminal().getTrmnum()));
            trmname = null != terminal ? terminal.getTerminalLocation() : "";
            if(trmname.equalsIgnoreCase("IMPRTS LOS ANGELES")){
                trmname = "Los Angeles";
            }
            trmAddress = null != terminal ? terminal.getAddres1() : "";
            trmZip = null != terminal ? terminal.getZipcde() : "";
        }
        customerPo = new Lcl3pRefNoDAO().getCustomerPo(fileId);
        CustAddress custAddress = new CustAddressDAO().findByAccountNo(billToPartyAc);
        if (custAddress != null) {
            consAddress.append(billToPartyAcctName).append("\n");
            consAddress.append(custAddress.getAddress1()).append("\n");
            consAddress.append(custAddress.getCity1()).append(" ").append(custAddress.getState()).append(" ").append(custAddress.getZip());
        }
        List<LclBookingPiece> lclBookingPiece = lclBookingPieceDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileId));
        LclFileNumber lclFileNumber = new LclFileNumberDAO().getByProperty("id", Long.parseLong(fileId));
        if (lclBookingPiece != null && !lclBookingPiece.isEmpty() && CommonUtils.isNotEmpty(lclBookingPiece.get(0).getLclBookingPieceUnitList())) {
            unitNo = lclBookingPiece.get(0).getLclBookingPieceUnitList().get(0).getLclUnitSs().getLclUnit().getUnitNo();
            masterBl = lclBookingPiece.get(0).getLclBookingPieceUnitList().get(0).getLclUnitSs().getLclUnit().getLclUnitSsManifestList().get(0).getMasterbl();
            vesselEtd = lclBookingPiece.get(0).getLclBookingPieceUnitList().get(0).getLclUnitSs().getLclSsHeader().getLclSsDetailList().get(0).getSta();
        }
        Boolean isSegregationFlag = new LclBookingSegregationDao().isCheckedSegregationDr(Long.parseLong(fileId));
        if (isSegregationFlag) {
            amsHouseBl = new LclBookingImportAmsDAO().getAmsNo(fileId);
        } else {
            amsHouseBl = new LclBookingImportAmsDAO().getAmsNoGroup(fileId);
        }
        if (lclFileNumber.getLclBookingImport() != null) {
            subHouseBl = lclFileNumber.getLclBookingImport().getSubHouseBl();
            pickUpDate = lclFileNumber.getLclBookingImport().getPickupDateTime();
        }
        PdfPCell cell = new PdfPCell();
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
        cell = makeCell("" + billToPartyAc, Element.ALIGN_LEFT, blackFontForFclAr, 0);
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

        cell = makeCell("" + consAddress.toString(), Element.ALIGN_LEFT, blackFontForFclAr, 0);
        cell.setColspan(2);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        clientPTable.addCell(cell);

        //INVOICE NO
        cell = makeCell(fileNumber, Element.ALIGN_CENTER, blackFontForFclBl, 0);
        clientPTable.addCell(cell);

        //DATE
        String acctNumber = checkPayment(billToPartyAc);
        if (!acctNumber.equals("noCredit")) {
            if (CommonFunctions.isNotNull(acctNumber) && !acctNumber.equals("") && pickUpDate != null) {
                cell = makeCell(DateUtils.formatStringDateToAppFormatMMM(pickUpDate), Element.ALIGN_CENTER, blackFontForFclBl, 0);
            } else if (pickUpDate == null && vesselEtd != null) {
                cell = makeCell(DateUtils.formatStringDateToAppFormatMMM(vesselEtd), Element.ALIGN_CENTER, blackFontForFclBl, 0);
            } else {
                cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            }
        } else if (acctNumber.equals("noCredit") && vesselEtd != null) {
            cell = makeCell(DateUtils.formatStringDateToAppFormatMMM(vesselEtd), Element.ALIGN_CENTER, blackFontForFclBl, 0);
        } else {
            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        }
        cell.setBorderWidthLeft(0.06f);
        clientPTable.addCell(cell);

        //BILLING TM
        cell = makeCell(trmname, Element.ALIGN_CENTER, blackFontForFclBl, 0);
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
        cell = makeCell(customerPo, Element.ALIGN_CENTER, blackFontForFclBl, 0);
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

        PdfPTable othersTable = makeTable(4);
        othersTable.setWidthPercentage(100f);
        othersTable.setWidths(new float[]{25, 25, 25, 25});
        cell = makeCell("CONTAINER NO.", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("ECI SHIPMENT FILE NO.", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
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
        //CONTAINER NO
        cell = makeCell("" + unitNo, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        //ECI SHIPMENT FILE NO
        cell = makeCell("IMP-" + fileNumber, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        //ORIGIN
        cell = makeCell("" + originValues, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        //DESTINATION
        cell = makeCell("" + destinationValues, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);

        cell = makeCell("MBL / AWB NUMBER", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("AMS HOUSE BL", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("SUB HOUSE BL", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        othersTable.addCell(cell);
        //MBL / AWB NUMBER
        cell = makeCell("" + masterBl, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        //amsHouseBl
        cell = makeCell(amsHouseBl, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        //subHouseBl
        cell = makeCell(subHouseBl, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);

        cell = makeCell("SHIPPER", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("FORWARDER", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        othersTable.addCell(cell);
        //SHIPPER
        cell = makeCell(shipName, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        //FORWARDER
        cell = makeCell(forwName, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);

        cell = makeCell("CONSIGNEE", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        othersTable.addCell(cell);

        cell = makeCell("NOTIFY PARTY", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        othersTable.addCell(cell);
        //CONSIGNEE
        cell = makeCell(consName, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        //NOTIFY PARTY
        cell = makeCell(notyName, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setMinimumHeight(20f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(othersTable);
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);

        Font boldHeadingFon = FontFactory.getFont("Arial", 7f, Font.BOLD);
        Paragraph p = null;

        PdfPTable othersTable1 = makeTable(5);
        othersTable1.setWidthPercentage(100f);
        othersTable1.setWidths(new float[]{2f, 1f, 4f, 1.3f, 1.3f});
        //marks     
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        p = new Paragraph(7f, "MARKS AND NUMBERS", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        othersTable1.addCell(cell);
        //no of pkgs
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "NO.OF.PKGS", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        othersTable1.addCell(cell);
        //desc
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "DESCRIPTION OF PACKAGES AND GOODS", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        othersTable1.addCell(cell);
        //grossweight
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "GROSS WEIGHT", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        othersTable1.addCell(cell);
        //measure
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "MEASURE", boldHeadingFon);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(Color.decode("#c5d9f1"));
        cell.addElement(p);
        othersTable1.addCell(cell);

//        List<LclBookingPiece> lclBookingPiecesList = null;
//        lclBookingPiecesList = lclBookingPieceDAO.findByProperty("lclFileNumber.id", Long.parseLong(fileId));
        if (lclBookingPiece != null && lclBookingPiece.size() > 0) {
            for (LclBookingPiece lclBookingPieces : lclBookingPiece) {
                //MARKS AND NUMBERS
                cell = new PdfPCell();
                cell.setBorder(0);
                if (lclBookingPieces != null && lclBookingPieces.getMarkNoDesc() != null && !lclBookingPieces.getMarkNoDesc().equals("")) {
                    p = new Paragraph(7f, "" + lclBookingPieces.getMarkNoDesc().toUpperCase(), blackNormalCourierFont8f);
                } else {
                    p = new Paragraph(7f, "", blackNormalCourierFont8f);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);
                //NO.OF.PKGS
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                if (lclBookingPieces != null && lclBookingPieces.getBookedPieceCount() != null && lclBookingPieces.getPackageType().getAbbr01() != null) {
                    p = new Paragraph(7f, "" + lclBookingPieces.getBookedPieceCount() + " " + lclBookingPieces.getPackageType().getAbbr01(), blackNormalCourierFont8f);
                } else {
                    p = new Paragraph(7f, "", blackNormalCourierFont8f);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);
                //DESCRIPTION OF PACKAGES AND GOODS
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                if (lclBookingPieces != null && lclBookingPieces.getPieceDesc() != null && !lclBookingPieces.getPieceDesc().equals("")) {
                    p = new Paragraph(7f, "" + lclBookingPieces.getPieceDesc().toUpperCase(), blackNormalCourierFont8f);
                } else {
                    p = new Paragraph(7f, "", blackNormalCourierFont8f);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);

                //grossweight
                cell = new PdfPCell();

                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                if (lclBookingPieces != null && lclBookingPieces.getBookedWeightMetric() != null) {
                    p = new Paragraph(7f, "" + lclBookingPieces.getBookedWeightMetric() + " KGS", blackNormalCourierFont8f);
                } else {
                    p = new Paragraph(7f, "", blackNormalCourierFont8f);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);
                //measure
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                if (lclBookingPieces != null && lclBookingPieces.getBookedVolumeMetric() != null) {
                    p = new Paragraph(7f, "" + lclBookingPieces.getBookedVolumeMetric() + " CBM", blackNormalCourierFont8f);
                } else {
                    p = new Paragraph(7f, "", blackNormalCourierFont8f);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);
                //2nd cell
                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                p = new Paragraph(7f, "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);

                cell.addElement(p);
                othersTable1.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                p = new Paragraph(7f, "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                p = new Paragraph(7f, "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                p = new Paragraph(7f, "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);

                //3rd cell
                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                p = new Paragraph(7f, "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                p = new Paragraph(7f, "", blackNormalCourierFont8f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                if (lclBookingPieces != null && lclBookingPieces.getBookedWeightImperial() != null) {
                    p = new Paragraph(7f, "" + lclBookingPieces.getBookedWeightImperial() + " LBS", blackNormalCourierFont8f);
                } else {
                    p = new Paragraph(7f, "", blackNormalCourierFont8f);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                if (lclBookingPieces != null && lclBookingPieces.getBookedVolumeImperial() != null) {
                    p = new Paragraph(7f, "" + lclBookingPieces.getBookedVolumeImperial() + " CFT", blackNormalCourierFont8f);
                } else {
                    p = new Paragraph(7f, "", blackNormalCourierFont8f);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                othersTable1.addCell(cell);
            }

        }

        cell = new PdfPCell();
        cell.setColspan(5);
        cell.addElement(othersTable1);
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        mainTable.addCell(cell);

        PdfPTable chargesTable = makeTable(4);
        chargesTable.setWidthPercentage(100.5f);
        chargesTable.setWidths(new float[]{45, 35, 5, 15});
        cell = makeCell("DESCRIPTION", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setColspan(2);
        chargesTable.addCell(cell);
        cell = makeCell("CHARGES", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        chargesTable.addCell(cell);

        NumberFormat number = new DecimalFormat("###,###,##0.00");
        String code = "";
//        double totalCharges = 0.00;
        double lateFee = 0.00;
        double payAmount = 0.00;
        int chargeCount = 0;
        double total = 0.00;
        String[] billToPartyA;
        billToPartyA = new String[]{"C","N","T"};
        List<String> billtoPartyList = Arrays.asList(billToPartyA);
        List<BookingChargesBean> lclBookingAcList = null;
        lclBookingAcList = new LclCostChargeDAO().findBybookingAcId(fileId,billtoPartyList);
        for (int j = 0; j < lclBookingAcList.size(); j++) {
            chargeCount++;
            BookingChargesBean lclBookingAc = (BookingChargesBean) lclBookingAcList.get(j);

            String codeDesc = "";

            code = CommonUtils.isNotEmpty(lclBookingAc.getChargeCode()) ? lclBookingAc.getChargeCode() : "";
            codeDesc = new GenericCodeDAO().getGenericCodeDesc(code);

            if (CommonUtils.isNotEmpty(lclBookingAc.getChargeCode())) {
//                    String desc = lclBookingAc.getChargeCode().toUpperCase();

                cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl);
                cell.setBorderWidthRight(0.0f);
            }

            if (chargeCount == 1) {
                cell.setBorderWidthTop(0.0f);
                cell.setBorderWidthRight(0.0f);
                cell.setBorderWidthLeft(0.0f);
                cell.setBorderWidthBottom(0.0f);
            } else {
                cell.setBorderWidthRight(0.0f);
                cell.setBorderWidthLeft(0.0f);
                cell.setBorderWidthBottom(0.0f);
            }
            chargesTable.addCell(cell);

            if (CommonUtils.isNotEmpty(codeDesc)) {
                cell = makeCell("" + codeDesc, Element.ALIGN_LEFT, blackFontForFclBl, 0.06f);
                cell.setBorderWidthLeft(0.0f);
            } else {
                cell = makeCell("" + code, Element.ALIGN_LEFT, blackFontForFclBl, 0.06f);
                cell.setBorderWidthLeft(0.0f);
            }
            if (chargeCount == 1) {
                cell.setBorderWidthTop(0.0f);
                cell.setBorderWidthRight(0.0f);
                cell.setBorderWidthBottom(0.0f);
            } else {
                cell.setBorderWidthRight(0.0f);
                cell.setBorderWidthBottom(0.0f);
            }

            chargesTable.addCell(cell);

            cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);//3
            if (chargeCount == 1) {
                cell.setBorderWidthTop(0.0f);
                cell.setBorderWidthRight(0.0f);
                cell.setBorderWidthBottom(0.0f);
            } else {
                cell.setBorderWidthRight(0.0f);
                cell.setBorderWidthBottom(0.0f);
            }
            chargesTable.addCell(cell);
            cell = makeCell(number.format(lclBookingAc.getTotalAmt().doubleValue()), Element.ALIGN_RIGHT, blackFontForFclBl, Rectangle.BOX);//4
            if (chargeCount == 1) {
                cell.setBorderWidth(0.0f);
            } else {
                cell.setBorderWidthLeft(0.0f);
                cell.setBorderWidthRight(0.0f);
                cell.setBorderWidthBottom(0.0f);
            }
            chargesTable.addCell(cell);
            total = total + lclBookingAc.getTotalAmt().doubleValue();

        }

        for (int i = 0; i < (14 - chargeCount); i++) {
//            chargesTable.addCell(makeCellLeftNoBorderFclBL(""));
//            chargesTable.addCell(makeCellRightNoBorderFclBL(""));
            cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0.06f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            cell.setBorderWidthLeft(0.0f);
            chargesTable.addCell(cell);

            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            cell.setBorderWidthLeft(0.0f);
            chargesTable.addCell(cell);
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
        cell = makeCell("INVOICE TOTAL", Element.ALIGN_CENTER, blackFontForFclBl, 0);
//        cell.setPaddingLeft(-15f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell(number.format(total), Element.ALIGN_RIGHT, blackFontForFclBl, 0);//4
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
//        
        payAmount = total;
//        String acctNumber = checkPayment(billToPartyAc);
        boolean lateFeeFlag = false;
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        TradingPartner tradingPartner = null;
//        SimpleDateFormat simpDate = new SimpleDateFormat("dd-MMM-yyyy");
        PdfPTable paidTable = makeTable(6);
        paidTable.setWidthPercentage(100.5f);
        paidTable.setWidths(new float[]{30, 15, 25, 10, 5, 15});
        if (CommonFunctions.isNotNull(acctNumber) && !acctNumber.equals("") && !acctNumber.equals("noCredit")) {
            tradingPartner = tradingPartnerBC.findTradingPartnerById(acctNumber);
            if (CommonFunctions.isNotNullOrNotEmpty(tradingPartner.getAccounting())) {
                for (Iterator accountingList = tradingPartner.getAccounting().iterator(); accountingList.hasNext();) {
                    CustomerAccounting customerAccounting = (CustomerAccounting) accountingList.next();
                    if (null != customerAccounting.getLclApplyLateFee() && customerAccounting.getLclApplyLateFee().equals("on")) {
                        lateFeeFlag = true;
                    }
                    break;
                }
            }
        }

        cell = makeCell("ARRIVAL DATE", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        paidTable.addCell(cell);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String outDate = "";
        String crtDate = "";

        if (vesselEtd != null) { //vessel arrivalDate         
            outDate = sdf.format(vesselEtd);
        }
        cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        paidTable.addCell(cell);
        CustomerAccounting customerAccounting = new CustomerAccountingDAO().findByProperty("accountNo", billToPartyAc);

        if (customerAccounting != null && (customerAccounting.getCreditRate() != null && (CommonUtils.isNotEmpty(outDate)) && !acctNumber.equals("noCredit"))) {
            Calendar c = Calendar.getInstance();
//            c.setTime(new Date(outDate)); //  Removed Deprecated Warning
            c.setTime(sdf.parse(outDate)); //   Now use previous date.
            if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 7 Days")) {
                c.add(Calendar.DATE, 7);
                crtDate = sdf.format(c.getTime());// Adding 7 days
            } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 15 Days")) {
                c.add(Calendar.DATE, 15);
                crtDate = sdf.format(c.getTime());// Adding 15 days
            } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("NET 21 DAYS")) {
                c.add(Calendar.DATE, 21);
                crtDate = sdf.format(c.getTime());// Adding 21 days
            } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 30 Days")) {
                c.add(Calendar.DATE, 30);
                crtDate = sdf.format(c.getTime());// Adding 30 days
            } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 45 Days")) {
                c.add(Calendar.DATE, 45);
                crtDate = sdf.format(c.getTime());// Adding 45 days
            } else if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Net 60 Days")) {
                c.add(Calendar.DATE, 60);
                crtDate = sdf.format(c.getTime());// Adding 60 days
            }
        }

        if (lateFeeFlag) {
            lateFee = total * 0.015; // 1.5percent calculate
            payAmount = total + lateFee;
            cell = makeCell("LATE FEE IF NOT PAID BY - " + crtDate, Element.ALIGN_LEFT, blackFontForFclBl, 0);
            cell.setColspan(2);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthLeft(0.06f);
            paidTable.addCell(cell);

            cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthLeft(0.06f);
            paidTable.addCell(cell);
            cell = makeCell(number.format(lateFee), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.06f);
            paidTable.addCell(cell);

            cell = makeCell(outDate, Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setMinimumHeight(10f);
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthRight(0.06f);
            paidTable.addCell(cell);

            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            paidTable.addCell(cell);

            cell = makeCell("PAY THIS AMOUNT IF NOT PAID BY DUE DATE", Element.ALIGN_LEFT, blackFontForFclBl, 0);
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
        } else {
//            String dueDate = "";
//            if(arRedInvoice.getDueDate() != null){
//            SimpleDateFormat sdfa = new SimpleDateFormat("dd-MMM-yyyy");
//            dueDate = sdfa.format(arRedInvoice.getDueDate());
//        }

//            cell = makeCell(!"".equals(dueDate) ? "PAY THIS AMOUNT IF NOT PAID BY - " + dueDate : "PLEASE PAY THIS AMOUNT",Element.ALIGN_LEFT, blackFontForFclBl, 0);
            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setColspan(4);
            paidTable.addCell(cell);

            cell = makeCell(outDate, Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setMinimumHeight(10f);
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthRight(0.06f);
            paidTable.addCell(cell);

            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            paidTable.addCell(cell);

            cell = makeCell("PLEASE PAY THIS AMOUNT - " + crtDate, Element.ALIGN_LEFT, blackFontForFclBl, 0);
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
            cell = makeCell(number.format(total), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthTop(0.06f);
            paidTable.addCell(cell);
        }
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

        ///end of description & charges
//        String paymentStatment = "";
//        paymentStatment = checkPayment(arRedInvoice);
        PdfPTable commandTable = new PdfPTable(1);
        commandTable.setWidthPercentage(100);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);

        if (customerAccounting != null && (customerAccounting.getCreditRate() != null && (CommonUtils.isNotEmpty(outDate)))) {
            if ((customerAccounting.getCreditRate().getCodedesc()).equalsIgnoreCase("Due Upon Receipt")) {
                cell = makeCell("INVOICE IS PAYABLE UPON RECEIPT", Element.ALIGN_CENTER, blackFontForFclBl, 0, Color.decode("#FFFF00"));
                commandTable.addCell(cell);
            } else {
                cell = makeCell("INVOICE PAYABLE ON OR BEFORE  " + (crtDate), Element.ALIGN_CENTER, new Font(Font.HELVETICA, 10, Font.BOLDITALIC, Color.BLACK), Rectangle.NO_BORDER);
                commandTable.addCell(cell);
            }
        } else {
            cell = makeCell("INVOICE IS PAYABLE UPON RECEIPT", Element.ALIGN_CENTER, blackFontForFclBl, 0, Color.decode("#FFFF00"));
            commandTable.addCell(cell);
        }
//        if (lateFeeFlag == false) {//if(paymentStatment.equals("noCredit") || lateFeeFlag == false){
//            cell = makeCell("INVOICE IS PAYABLE UPON RECEIPT OR INVOICE PAYABLE WITHIN 30 DAYS FROM DEPARTURE/ARRIVAL DATE", Element.ALIGN_LEFT, blackFontForFclBl, 0, Color.decode("#FFFF00"));
//            commandTable.addCell(cell);
//        } else {
//            cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
//            commandTable.addCell(cell);
//        }
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

        //
//        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
//        String eftBank = systemRulesDAO.getSystemRulesByCode("EFTBank");
//        String eftBankAddress = systemRulesDAO.getSystemRulesByCode("EFTBankAddress");
//        String eftABANo = systemRulesDAO.getSystemRulesByCode("EFTABANo");
//        String eftAcctName = systemRulesDAO.getSystemRulesByCode("EFTAcctName");
//        String eftAccountNo = systemRulesDAO.getSystemRulesByCode("EFTAccountNo");
//        CompanyModel company = systemRulesDAO.getCompanyDetails();
        
        //PAYMENT METHODS
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

// Via Check Details
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
                p = new Paragraph(10f, "2401 N.W. 69TH STREET", boldHeadingFon);
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                p = new Paragraph(10f, "Miami, FL  33147", boldHeadingFon);
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                bankDetailsTable.addCell(cell);
            } else {
                p = new Paragraph(10f, "" + trmAddress, boldHeadingFon);
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                p = new Paragraph(10f, "" + trmname + " " + trmZip, boldHeadingFon);
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                bankDetailsTable.addCell(cell);
            }
        }

        //Via ACH or Wire Transfer Details
        
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBorderWidthRight(0.06f);
            p = new Paragraph(10f, "Bank: " + company.getBankName(), boldHeadingFon);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
            p = new Paragraph(10f, company.getBankAddress(), boldHeadingFon);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
            p = new Paragraph(10f, "ABA: " + company.getBankAbaNumber(), boldHeadingFon);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
            p = new Paragraph(10f, "ACCT: " +companyName, boldHeadingFon);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
            p = new Paragraph(10f, "ACCOUNT NO: " + company.getBankAccountNumber(), boldHeadingFon);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
            bankDetailsTable.addCell(cell);
       
        //Credit Card Payments Details
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

    public String checkPayment(String billToPartyAc) throws Exception {
        String returnString = "noCredit";
        String crditWarning ="";
        FclBlBC blBC = new FclBlBC();
        String creditDetail = blBC.validateCustomer(billToPartyAc, "");
        if (creditDetail != null && !creditDetail.equals("")) {
            String[] chargecode = creditDetail.split("==");
            if(!chargecode[0].equals("")) {
            crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
            }
            if (crditWarning.equals("In Good Standing ") || crditWarning.equals("Credit Hold ")) {
                returnString = billToPartyAc;
            } else {
                returnString = "noCredit";
            }
        }
        return returnString;
    }
    
    public String setBrand(String fileId) throws Exception {
        String brand = new LclFileNumberDAO().getBusinessUnit(fileId);
        if ("ECI".equalsIgnoreCase(brand)) {
            webSite = LoadLogisoftProperties.getProperty("application.Econo.website");
            companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
        } else if ("OTI".equalsIgnoreCase(brand)) {
            webSite = LoadLogisoftProperties.getProperty("application.OTI.website");
            companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
        } else {
            webSite = LoadLogisoftProperties.getProperty("application.ECU.website");
            companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
        }
        return brand;
    }

}
