package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.beans.LCLCorrectionChargeBean;
import com.gp.cong.logisoft.beans.LCLCorrectionNoticeBean;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
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
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class LclCorrectionDebitCreditPdfCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(LclCorrectionDebitCreditPdfCreator.class);
    FclBlBC fclBlBC = new FclBlBC();
    Document document = null;
    PdfWriter pdfWriter = null;
    protected PdfTemplate total;
    protected BaseFont helv;
    private String contextPath;
    private LCLCorrectionNoticeBean lclCorrectionNoticeBean;
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    GenericCode genericCode = new GenericCode();
    private String imagePath="";

    public LclCorrectionDebitCreditPdfCreator() {
    }

    public LclCorrectionDebitCreditPdfCreator(String contextPath, LCLCorrectionNoticeBean lclCorrectionNoticeBean) {
        this.contextPath = contextPath;
        this.lclCorrectionNoticeBean = lclCorrectionNoticeBean;
    }

    public void initialize(String fileName, String contextPath, LCLCorrectionNoticeBean lclCorrectionNoticeBean, Long fileId) throws FileNotFoundException,
            DocumentException,
            Exception {
        String brand= new LclFileNumberDAO().getBusinessUnit(String.valueOf(fileId));
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        pdfWriter.setPageEvent(new LclCorrectionDebitCreditPdfCreator(contextPath, lclCorrectionNoticeBean));
        String companyName = "ECI".equalsIgnoreCase(brand) ? LoadLogisoftProperties.getProperty("application.Econo.companyname") : "OTI".equalsIgnoreCase(brand) ? LoadLogisoftProperties.getProperty("application.OTI.companyname") : LoadLogisoftProperties.getProperty("application.ECU.companyname");
        this.imagePath = "ECI".equalsIgnoreCase(brand) ? LoadLogisoftProperties.getProperty("application.image.econo.logo") : "OTI".equalsIgnoreCase(brand) ? LoadLogisoftProperties.getProperty("application.image.econo.logo") : LoadLogisoftProperties.getProperty("application.image.logo");
        HeaderFooter footer = new HeaderFooter(
                new Phrase("Thank you for choosing " + companyName + " for all your shipping needs", new Font(Font.HELVETICA, 10, Font.ITALIC, Color.BLACK)), false);
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
            log.info("onOpenDocument failed on " + new Date() + " for " + e);
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
    }

    public void createBody(LCLCorrectionNoticeBean lclCorrectionNoticeBean, String contextPath, HttpServletRequest request, Long fileId,
                           String selectedMenu)
            throws DocumentException, MalformedURLException, IOException, Exception {
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        String companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
        String companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
        String companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");
        String fileType = new LCLCorrectionDAO().getFileType(lclCorrectionNoticeBean.getFileNo());
        PdfPCell cell = new PdfPCell();
        PdfPTable headingMainTable = new PdfPTable(1);
        headingMainTable.setWidthPercentage(100);
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.setWidths(new float[]{100});
        String headerLabel = "INVOICE";
        if (lclCorrectionNoticeBean.getDebitOrCreditNote().contains("CREDIT")) {
            headerLabel = "CREDIT NOTE";
        }
        PdfPTable imgTable = new PdfPTable(1);
        imgTable.setWidthPercentage(100);
        Image img = Image.getInstance(contextPath + imagePath);
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
        cell = makeCell(headerLabel, Element.ALIGN_CENTER, new Font(Font.HELVETICA, 12, Font.BOLD, Color.RED), 0.06f);
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
        // tabel for company details and invoice heading
        PdfPTable mainTable = makeTable(2);
        mainTable.setWidthPercentage(100);
        mainTable.setKeepTogether(true);
        PdfPTable clientPTable = new PdfPTable(2);
        clientPTable.setWidthPercentage(101);
        clientPTable.setWidths(new float[]{10, 90});
        clientPTable.setKeepTogether(true);
        cell = makeCell("TO:", Element.ALIGN_LEFT, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell(lclCorrectionNoticeBean.getCustomer(), Element.ALIGN_LEFT, blackFontForFclAr, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFontSize8, 0);
        cell.setBorderWidthRight(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell(lclCorrectionNoticeBean.getBillToPartyAddress(), Element.ALIGN_LEFT, blackFontForFclAr, 0);
        cell.setBorderWidthRight(0.06f);
        cell.setMinimumHeight(30);
        clientPTable.addCell(cell);
        cell = makeCell("ATTN", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell(CommonUtils.isNotEmpty(lclCorrectionNoticeBean.getAttn()) ? lclCorrectionNoticeBean.getAttn().toUpperCase() : "", Element.ALIGN_LEFT, blackFontForFclBl, 0);
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
        PdfPTable invoiceMainTable = new PdfPTable(1);
        invoiceMainTable.setWidthPercentage(101.5f);
        invoiceMainTable.setKeepTogether(true);
        PdfPTable invoicePTable1 = new PdfPTable(4);
        invoicePTable1.setWidths(new float[]{40, 75, 40, 55});
        invoicePTable1.setWidthPercentage(101);
        invoicePTable1.addCell(makeCellLeftNoBorderFclBL(""));
        cell = makeCell(headerLabel + " NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
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
        cell = makeCell(lclCorrectionNoticeBean.getBlNo() + "CN" + lclCorrectionNoticeBean.getCorrectionNo(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);
        SimpleDateFormat simpDate;
        simpDate = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = lclCorrectionNoticeBean.getCorrectionDate();
        String dateString = "";
        if (date != null) {
            dateString = simpDate.format(date);
        }
        cell = makeCell(dateString, Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);
        cell = makeCell(lclCorrectionNoticeBean.getVoyageNumber(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable1.addCell(cell);
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
        cell = makeCell("ECI Ref.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable2.addCell(cell);
        invoicePTable2.addCell(makeCellLeftNoBorderFclBL(""));
        cell = makeCell(lclCorrectionNoticeBean.getCustomerAcctNo(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        invoicePTable2.addCell(cell);
        cell = makeCell("" + lclCorrectionNoticeBean.getBlNo(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
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
        othersTable = fillMarksAndVoyageContinerInformation(othersTable, lclCorrectionNoticeBean);
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
        cell = makeCell(lclCorrectionNoticeBean.getEciShipmentFileNo(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell(lclCorrectionNoticeBean.getComments(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        othersTable.addCell(cell);
        StringBuilder shipperAddr = new StringBuilder();
        cell = makeCell(shipperAddr.toString(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
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

        List<LCLCorrectionChargeBean> lclCorrectionChargesList = (List) request.getAttribute("lclCorrectionChargesList");
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
        Paragraph p = null;
        if (lclCorrectionChargesList != null && !lclCorrectionChargesList.isEmpty()) {
            for (LCLCorrectionChargeBean lclCorrectionChargeBean : lclCorrectionChargesList) {
                chargeCount++;
                String codeDesc = "";
                code = CommonUtils.isNotEmpty(lclCorrectionChargeBean.getChargeCode()) ? lclCorrectionChargeBean.getChargeCode() : "";
                codeDesc = genericCodeDAO.getGenericCodeDesc(code);
                chargesTable.addCell(makeCellLeftNoBorderFclBL(""));
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
                cell = makeCell("USD", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);
                if (chargeCount == 1) {
                    cell.setBorderWidthTop(0.0f);
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                } else {
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                }
                chargesTable.addCell(cell);
                if (lclCorrectionChargeBean.getDifferenceAmount() != null) {
                    if (selectedMenu.equalsIgnoreCase("Imports")) {
                        totalCharges += lclCorrectionChargeBean.getDifferenceAmount().doubleValue();
                        cell = makeCell(lclCorrectionChargeBean.getDifferenceAmount().toString(), Element.ALIGN_RIGHT, blackFontForFclBl, Rectangle.BOX);
                        } else {
                        totalCharges += lclCorrectionChargeBean.getDifferenceAmount().doubleValue();
                        cell = makeCell(lclCorrectionChargeBean.getDifferenceAmount().toString(), Element.ALIGN_RIGHT, blackFontForFclBl, Rectangle.BOX);
                    }

                } else {
                    cell = makeCell("", Element.ALIGN_RIGHT, blackFontForFclBl, Rectangle.BOX);
                }
                if (chargeCount == 1) {
                    cell.setBorderWidth(0.0f);
                } else {
                    cell.setBorderWidthLeft(0.0f);
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                }
                chargesTable.addCell(cell);
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
        cell = makeCell(headerLabel + " TOTAL ", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell("USD", Element.ALIGN_CENTER, blackFontForFclBl, 0);
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
        String acctNumber = checkPayment(lclCorrectionNoticeBean.getCustomerAcctNo(),fileType);
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
                        if (lclCorrectionNoticeBean.getSailDate() != null) {
                            String dt = "";
                            int creditTerm = Integer.parseInt(customerAccounting.getCreditRate().getCode());
                            Calendar c = Calendar.getInstance();
                            c.setTime(lclCorrectionNoticeBean.getSailDate());
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
            cell = makeCell("USD", Element.ALIGN_CENTER, blackFontForFclBl, 0);
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
        cell = makeCell("USD", Element.ALIGN_CENTER, blackFontForFclBl, 0);
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
        paymentStatment = checkPayment(lclCorrectionNoticeBean.getCustomerAcctNo(),fileType);
        PdfPTable commandTable = new PdfPTable(1);
        commandTable.setWidthPercentage(100);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        if (lateFeeFlag == false) {//if(paymentStatment.equals("noCredit") || lateFeeFlag == false){
            cell = makeCell("THIS " + headerLabel + " IS DUE UPON RECEIPT", Element.ALIGN_CENTER, new Font(Font.HELVETICA, 10, Font.BOLDITALIC, Color.BLACK), Rectangle.NO_BORDER);
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
        bankDetailsTable.addCell(cell);
        cell = new PdfPCell();
        cell.addElement(bankDetails);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        bankDetailsTable.addCell(cell);

        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
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
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            String headerLabel = "INVOICE";
            if (lclCorrectionNoticeBean.getDebitOrCreditNote().contains("CREDIT")) {
                headerLabel = "CREDIT NOTE";
            }
            String correctionNo = headerLabel + "# " + lclCorrectionNoticeBean.getBlNo() + "CN" + lclCorrectionNoticeBean.getCorrectionNo();
            //---------------
            //this for print page number at the bottom in the format x of y
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            String text = "Page " + writer.getPageNumber() + " of ";
            float textBase = document.bottom() - (document.bottomMargin() - 30);
            float textSize = helv.getWidthPoint(text, 12);
            cb.beginText();
            cb.setFontAndSize(helv, 7);
            cb.setTextMatrix(document.left(), textBase);
            cb.showText(correctionNo);
            cb.setTextMatrix(document.left() + 280, textBase);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(total, document.left() + 260 + textSize, textBase);
            cb.restoreState();
        } catch (Exception e) {
            log.info("onEndPage failed on " + new Date() + " for " + e);
            throw new ExceptionConverter(e);
        }
    }

    public void destroy() {
        document.close();
    }

    public String createReport(LCLCorrectionNoticeBean lclCorrectionNoticeBean, String fileName, String contextPath,
            HttpServletRequest request, Long fileId,String selectedMenu) throws Exception {
        this.initialize(fileName, contextPath, lclCorrectionNoticeBean, fileId);
        this.createBody(lclCorrectionNoticeBean, contextPath, request, fileId,selectedMenu);
        this.destroy();
        return fileName;
    }

    public PdfPTable fillMarksAndVoyageContinerInformation(PdfPTable particularsTable, LCLCorrectionNoticeBean lclCorrectionNoticeBean)
            throws Exception {
        PdfPCell cell = null;
        cell = makeCellLeftNoBorderFontSize6(StringUtils.substringBeforeLast(lclCorrectionNoticeBean.getUnitNo(), ","));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.06f);
        particularsTable.addCell(cell);
        cell = makeCellLeftNoBorderFontSize6("");
        cell.setBorderWidthRight(0.06f);
        particularsTable.addCell(cell);
        cell = makeCellLeftNoBorderFontSize6(lclCorrectionNoticeBean.getOrigin().toUpperCase());
        cell.setBorderWidthRight(0.06f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        particularsTable.addCell(cell);
        cell = makeCellLeftNoBorderFontSize6(lclCorrectionNoticeBean.getDestination().toUpperCase());
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        particularsTable.addCell(cell);
        return particularsTable;
    }

    public String checkPayment(String acctNo,String fileType) throws Exception {
        String returnString = "noCredit";
        if (CommonUtils.isNotEmpty(acctNo)) {
            FclBlBC blBC = new FclBlBC();
            String creditDetail = blBC.validateCustomer(acctNo,fileType);
            if (creditDetail != null && !creditDetail.equals("")) {
                String[] chargecode = creditDetail.split("==");
                String crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                if (crditWarning.equals("In Good Standing ") || crditWarning.equals("Credit Hold ")) {
                    returnString = acctNo;
                } else {
                    returnString = "noCredit";
                }
            }
        }
        return returnString;
    }
}
