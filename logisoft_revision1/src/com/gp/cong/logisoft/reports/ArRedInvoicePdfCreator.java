package com.gp.cong.logisoft.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlBC;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

public class ArRedInvoicePdfCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(ArRedInvoicePdfCreator.class);

    FclBlBC fclBlBC = new FclBlBC();
    Document document = null;
    PdfWriter pdfWriter = null;
    protected PdfTemplate total;
    protected BaseFont helv;
    private String contextPath = null;
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    GenericCode genericCode = new GenericCode();
    ArRedInvoice arRedInvoice = null;

    public ArRedInvoicePdfCreator() {
    }

    public ArRedInvoicePdfCreator(String contextPath, ArRedInvoice arRedInvoice) {
        this.contextPath = contextPath;
        this.arRedInvoice = arRedInvoice;
    }

    public void initialize(String fileName, String contextPath, ArRedInvoice arRedInvoice) throws FileNotFoundException,
            DocumentException,
            Exception {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        pdfWriter.setPageEvent(new ArRedInvoicePdfCreator(contextPath, arRedInvoice));

        BookingFcl bookingFcl = null;
        Quotation quotation = null;
        FclBl fclBl = null;
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        fclBl = new FclBlDAO().getOriginalBl(arRedInvoice.getFileNo());
        quotation = new QuotationDAO().getFileNoObject(arRedInvoice.getFileNo());
        bookingFcl = new BookingFclDAO().findbyFileNo(arRedInvoice.getFileNo());
        String brand = "";

        if (null != fclBl && null != fclBl.getBrand()) {
            brand = fclBl.getBrand();
        } else if (null != bookingFcl && null != bookingFcl.getBrand()) {
            brand = bookingFcl.getBrand();
        } else if (null != quotation && null != quotation.getBrand()) {
            brand = quotation.getBrand();
        }
        String companyName = "";
        if (brand.equals("Econo") && ("03").equals(companyCode)) {
            companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
        } else if (brand.equals("OTI") && ("02").equals(companyCode)) {
            companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
        } else if (brand.equalsIgnoreCase("Ecu Worldwide")) {
            companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
        }
//        String companyName = null != LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName")
//                ? LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName").toUpperCase() : "OTI cargo Inc";
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
            String path = LoadLogisoftProperties.getProperty("application.image.logo");
            SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
            String companyName = systemRulesDAO.getSystemRulesByCode("CompanyName");
            String companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
            String companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
            String companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");
            String companyEmail = systemRulesDAO.getSystemRulesByCode("Email");
            BookingFcl bookingFcl = null;
            Quotation quotation = null;
            FclBl fclBl = null;
            String econoPath = LoadLogisoftProperties.getProperty("application.image.econo.logo");
            String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
            String fileNo = null != arRedInvoice.getFileNo() ? arRedInvoice.getFileNo() : "";
            fclBl = new FclBlDAO().getOriginalBl(fileNo);
            quotation = new QuotationDAO().getFileNoObject(fileNo);
            bookingFcl = new BookingFclDAO().findbyFileNo(fileNo);
            String brand = "";
            if (null != fclBl && null != fclBl.getBrand()) {
                brand = fclBl.getBrand();
            } else if (null != bookingFcl && null != bookingFcl.getBrand()) {
                brand = bookingFcl.getBrand();
            } else if (null != quotation && null != quotation.getBrand()) {
                brand = quotation.getBrand();
            }

            PdfPCell cell = new PdfPCell();
            PdfPCell celL = new PdfPCell();
            PdfPTable headingMainTable = new PdfPTable(1);
            headingMainTable.setWidthPercentage(100);
            PdfPTable headingTable = new PdfPTable(1);
            headingTable.setWidths(new float[]{100});
            if (path.equalsIgnoreCase("/img/companyLogo/EconocaribePrintLogo.gif")) {
//                    Image img = Image.getInstance(contextPath + path);
//                    table.setWidthPercentage(100);
//	            img.scalePercent(60);
////	            img.scaleAbsoluteWidth(556);
//	            celL.addElement(new Chunk(img, -2, -25));
//	            celL.setBorder(0);
//	            celL.setHorizontalAlignment(Element.ALIGN_LEFT);
//	            celL.setVerticalAlignment(Element.ALIGN_CENTER);
//	            table.addCell(celL);
//	            DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
//	            Date currentDate = new Date();
//	            cell = makeCellRightNoBorderWhiteFont("");
////	            cell = makeCellRightNoBorderWhiteFont("Date Printed : " + df7.format(currentDate));
//	            cell.setPaddingTop(20f);
            } else {

                PdfPTable imgTable = new PdfPTable(1);
                imgTable.setWidthPercentage(100);
                Image img = null;
                if (brand.equals("Econo") && ("03").equals(companyCode)) {
                    img = Image.getInstance(contextPath + econoPath);
                } else if (brand.equals("OTI") && ("02").equals(companyCode)) {
                    img = Image.getInstance(contextPath + econoPath);
                } else if (brand.equalsIgnoreCase("Ecu Worldwide")) {
                    img = Image.getInstance(contextPath + path);
                } else if (("03").equals(companyCode)) {
                    img = Image.getInstance(contextPath + path);
                } else if (("02").equals(companyCode)) {
                    img = Image.getInstance(contextPath + path);
                }
                img.scalePercent(60);
                PdfPCell logoCell = new PdfPCell(img);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                logoCell.setVerticalAlignment(Element.ALIGN_LEFT);
                logoCell.setPaddingLeft(+50);
//	            celL.addElement(new Chunk(img, 0, -40));
//	            celL.setBorder(0);
//	            celL.setHorizontalAlignment(Element.ALIGN_LEFT);
//	            celL.setVerticalAlignment(Element.ALIGN_LEFT);
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
                cell = makeCell("INVOICE", Element.ALIGN_CENTER, new Font(Font.HELVETICA, 12, Font.BOLD, Color.RED), Rectangle.BOX);
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
//                celL.setBorder(0);                
//                headingTable.addCell(celL);
                cell = new PdfPCell();
                cell.addElement(addrTable);
                cell.setBorder(0);
                headingTable.addCell(cell);
                cell = makeCellLeftNoBorderFclBL("");
                cell.setBorderWidthRight(0.6f);
                cell.setBorderWidthLeft(0.6f);
                cell.setBorderWidthTop(0.6f);
                cell.setBorderWidthBottom(0.0f);
                cell.addElement(headingTable);
                headingMainTable.addCell(cell);
            }
            document.add(headingMainTable);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public void createBody(ArRedInvoice arRedInvoice, String contextPath, MessageResources messageResource, User loginUser)
            throws DocumentException, MalformedURLException, IOException, Exception {

        // tabel for company details and invoice heading
        PdfPCell cell = new PdfPCell();
        CustAddress custAddress = new CustAddressDAO().findByAccountNo(arRedInvoice.getCustomerNumber());
        PdfPTable mainTable = makeTable(2);
        mainTable.setWidthPercentage(100);
        mainTable.setKeepTogether(true);
        PdfPTable clientPTable = new PdfPTable(2);
        clientPTable.setWidthPercentage(101);
        clientPTable.setWidths(new float[]{10, 90});
        clientPTable.setKeepTogether(true);
        cell = makeCell("TO:", Element.ALIGN_LEFT, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        clientPTable.addCell(cell);
        cell = makeCell(arRedInvoice.getCustomerName(), Element.ALIGN_LEFT, blackFontForFclAr, 0);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        clientPTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFontSize8, 0);
        cell.setBorderWidthRight(0.6f);
        clientPTable.addCell(cell);
        StringBuilder stringBuilder = new StringBuilder("");
        if (null != custAddress) {
            stringBuilder.append(custAddress.getAddress1()).append("\n");
            stringBuilder.append("\n");
            stringBuilder.append(custAddress.getCity1()).append(", ");
            stringBuilder.append(custAddress.getState()).append(" ").append(custAddress.getZip());
        }
        cell = makeCell(stringBuilder.toString(), Element.ALIGN_LEFT, blackFontForFclAr, 0);
        cell.setBorderWidthRight(0.6f);
        cell.setMinimumHeight(30);
        clientPTable.addCell(cell);
        cell = makeCell("ATTN", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        clientPTable.addCell(cell);
        cell = makeCell(CommonUtils.isNotEmpty(arRedInvoice.getContactName()) ? arRedInvoice.getContactName().toUpperCase() : "", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        clientPTable.addCell(cell);
        cell.addElement(clientPTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.0f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.0f);
        cell.setBorderWidthBottom(0.0f);
        mainTable.addCell(cell);
        String fileNo = CommonUtils.isNotEmpty(arRedInvoice.getScreenName()) ? arRedInvoice.getFileNo() : "";
        List lineItemList = new ArRedInvoiceChargesDAO().getCharges(arRedInvoice.getId());
        FclBl fclBl = null;
        boolean isBl = true;
        boolean isBooking = true;
        boolean isQuotation = true;
        boolean isBlImport = false;
        boolean isBookingImport = false;
        boolean isQuotationImport = false;
        BookingFcl bookingFcl = null;
        Quotation quotation = null;
        String importFlag = "";
        fileNo = "";
        fclBl = new FclBlDAO().getOriginalBl(arRedInvoice.getFileNo());
        if (null == fclBl) {
            bookingFcl = new BookingFclDAO().getFileNoObject(arRedInvoice.getFileNo());
            isBl = false;
            if (null == bookingFcl) {
                quotation = new QuotationDAO().getFileNoObject(arRedInvoice.getFileNo());
                isBooking = false;
            }
        }
        if (isBl && null != fclBl) {
            if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                isBlImport = true;
            } else {
                isBlImport = false;
            }
        } else if (isBooking && null != bookingFcl) {
            if ("I".equalsIgnoreCase(bookingFcl.getImportFlag())) {
                isBookingImport = true;
            } else {
                isBookingImport = false;
            }
        } else if (isQuotation && null != quotation && null != quotation.getFileType()) {
            if ("I".equalsIgnoreCase(quotation.getFileType())) {
                isQuotationImport = true;
            } else {
                isQuotationImport = false;
            }
        }
        if (isBl && null != fclBl && null != fclBl.getFileNo()) {
            fileNo = fclBl.getFileNo();
        } else if (isBooking && null != bookingFcl && null != bookingFcl.getFileNo()) {
            fileNo = bookingFcl.getFileNo();
        } else if (isQuotation && null != quotation && null != quotation.getFileNo()) {
            fileNo = quotation.getFileNo();
        }
        PdfPTable invoiceMainTable = new PdfPTable(1);
        invoiceMainTable.setWidthPercentage(101.5f);
        invoiceMainTable.setKeepTogether(true);
        PdfPTable invoicePTable1 = new PdfPTable(4);
        invoicePTable1.setWidths(new float[]{10, 25, 20, 45});
        invoicePTable1.setWidthPercentage(101);
        invoicePTable1.addCell(makeCellLeftNoBorderFclBL(""));
        cell = makeCell("INVOICE NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        invoicePTable1.addCell(cell);
        cell = makeCell("DATE", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        invoicePTable1.addCell(cell);
        cell = makeCell("REFERENCE NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        invoicePTable1.addCell(cell);
        invoicePTable1.addCell(makeCellLeftNoBorderFclBL(""));
        cell = makeCell(arRedInvoice.getInvoiceNumber(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        invoicePTable1.addCell(cell);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = arRedInvoice.getDate();
        String dateString = "";
        if (date != null) {
            dateString = sdf.format(date);
        }
//            Date date = fclBl.getBolDate();
//            String dateString = "";
//            if (date != null) {
//                dateString = sdf.format(date);
//            }
        cell = makeCell(dateString, Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        invoicePTable1.addCell(cell);
//            String fileNo =(String) messageResource.getMessage("fileNumberPrefix")+fclBl.getFileNo().toString();
        String exportReference = null != fclBl && CommonUtils.isNotEmpty(fclBl.getImportOrginBlno()) ? fclBl.getImportOrginBlno() : "";
        int length = exportReference.length();
        if (!exportReference.equals("") && length > 0) {
            if (exportReference.contains("\n")) {
                exportReference = exportReference.substring(0, exportReference.indexOf("\n"));
            } else {
                exportReference = exportReference.substring(0, length);
            }
        }
        if (isBl && null != fclBl) {
            if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                cell = makeCell(exportReference, Element.ALIGN_CENTER, blackFontForFclBl, 0);
            } else {
                if (isBl) {
                    cell = makeCell(null != fclBl.getBookingNo() ? fclBl.getBookingNo() : "", Element.ALIGN_CENTER, blackFontForFclBl, 0);
                }
            }
        } else if (isBooking && null != bookingFcl) {
            if ("I".equalsIgnoreCase(bookingFcl.getImportFlag())) {
                cell = makeCell(exportReference, Element.ALIGN_CENTER, blackFontForFclBl, 0);
            } else {
                if (isBooking) {
                    cell = makeCell(null != bookingFcl.getSSBookingNo() ? bookingFcl.getSSBookingNo() : "", Element.ALIGN_CENTER, blackFontForFclBl, 0);
                }
            }
        } else if (isQuotation && null != quotation && null != quotation.getFileType()) {
            if ("I".equalsIgnoreCase(quotation.getFileType())) {
                cell = makeCell(exportReference, Element.ALIGN_CENTER, blackFontForFclBl, 0);
            } else {
                if (isQuotation) {
                    cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
                }
            }
        } else {
            if (isBl) {
                cell = makeCell(null != fclBl.getBookingNo() ? fclBl.getBookingNo() : "", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            } else if (isBooking) {
                cell = makeCell(null != bookingFcl.getSSBookingNo() ? bookingFcl.getSSBookingNo() : "", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            } else if (isQuotation) {
                cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            }
        }
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
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
        if (isBlImport || isBookingImport || isQuotationImport) {
            invoicePTable2.addCell(makeCellLeftNoBorderFclBL(""));
            cell = makeCell("BILL TO ACCT NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthBottom(0.6f);
            invoicePTable2.addCell(cell);
            cell = makeCell("ECI Ref.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthBottom(0.6f);
            invoicePTable2.addCell(cell);
            invoicePTable2.addCell(makeCellLeftNoBorderFclBL(""));
            cell = makeCell(arRedInvoice.getCustomerNumber(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthBottom(0.6f);
            invoicePTable2.addCell(cell);
            cell = makeCell("04-" + fileNo, Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthBottom(0.6f);
            invoicePTable2.addCell(cell);

        } else {
            invoicePTable2.addCell(makeCellLeftNoBorderFclBL(""));
            cell = makeCell("BILL TO ACCT NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthBottom(0.6f);
            invoicePTable2.addCell(cell);
            cell = makeCell(arRedInvoice.getCustomerNumber(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthBottom(0.6f);
            invoicePTable2.addCell(cell);
        }
        cell = new PdfPCell();
        cell.addElement(invoicePTable2);
        cell.setBorder(0);
        invoiceMainTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(invoiceMainTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthLeft(0.0f);
        cell.setBorderWidthTop(0.0f);
        cell.setBorderWidthBottom(0.0f);
        mainTable.addCell(cell);
        //
        if (isBlImport || isBookingImport || isQuotationImport) {
            PdfPTable othersTable = makeTable(5);
            othersTable.setWidthPercentage(100.5f);
            othersTable.setWidths(new float[]{10, 20, 20, 25, 25});
            importFlag = "I";
            cell = makeCell("PIECES", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthBottom(0.6f);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthRight(0.6f);
            othersTable.addCell(cell);
            cell = makeCell("WEIGHT", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthBottom(0.6f);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthRight(0.6f);
            othersTable.addCell(cell);
            cell = makeCell("MEASUREMENTS", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthBottom(0.6f);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthRight(0.6f);
            othersTable.addCell(cell);
            cell = makeCell("ORIGIN", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthBottom(0.6f);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthRight(0.6f);
            othersTable.addCell(cell);
            cell = makeCell("DESTINATION", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthBottom(0.6f);
            cell.setBorderWidthTop(0.6f);
            othersTable.addCell(cell);
            if (isBl) {
                othersTable = fillMarksAndContinerInformation(othersTable, fclBl, messageResource, importFlag);
            } else if (isBooking) {
                othersTable = fillBookingInformation(othersTable, bookingFcl, messageResource, importFlag);
            }
            cell = makeCell("SHIPPER / FORWARDER", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setColspan(3);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthBottom(0.6f);
            othersTable.addCell(cell);
            cell = makeCell("CONSIGNEE", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setColspan(2);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthBottom(0.6f);
            othersTable.addCell(cell);
            StringBuilder shipperAddr = new StringBuilder();
            if (isBl) {
                shipperAddr.append(CommonUtils.isNotEmpty(fclBl.getShipperName()) ? fclBl.getShipperName() : "");
                if ((null != fclBl.getShipperName() && !fclBl.getShipperName().equals(""))
                        && (null != fclBl.getForwardingAgentName() && !fclBl.getForwardingAgentName().equals(""))) {
                    shipperAddr.append(" / ");
                }
                shipperAddr.append(CommonUtils.isNotEmpty(fclBl.getForwardingAgentName()) ? fclBl.getForwardingAgentName() : "");
            } else if (isBooking) {
                shipperAddr.append(CommonUtils.isNotEmpty(bookingFcl.getShipper()) ? bookingFcl.getShipper() : "");
                if ((null != bookingFcl.getShipper() && !bookingFcl.getShipper().equals(""))
                        && (null != bookingFcl.getForward() && !bookingFcl.getForward().equals(""))) {
                    shipperAddr.append(" / ");
                }
                shipperAddr.append(CommonUtils.isNotEmpty(bookingFcl.getForward()) ? bookingFcl.getForward() : "");
            }
            cell = makeCell(shipperAddr.toString(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            cell.setColspan(3);
            cell.setBorderWidthRight(0.6f);
            othersTable.addCell(cell);
            if (isBl) {
                cell = makeCell(CommonUtils.isNotEmpty(fclBl.getConsigneeName()) ? fclBl.getConsigneeName() : "", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            } else if (isBooking) {
                cell = makeCell(CommonUtils.isNotEmpty(bookingFcl.getConsignee()) ? bookingFcl.getConsignee() : "", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            } else {
                cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            }

            cell.setColspan(2);
            othersTable.addCell(cell);
            cell = makeCell("DESCRIPTION / DETAILS", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthBottom(0.6f);
            cell.setColspan(5);
            othersTable.addCell(cell);
            cell = makeCellLeftNoBorderFclBL(CommonUtils.isNotEmpty(arRedInvoice.getNotes()) ? arRedInvoice.getNotes() : "");
            cell.setColspan(5);
            cell.setMinimumHeight(50);
            othersTable.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.addElement(othersTable);
            cell.setBorder(0);
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthLeft(0.6f);
            mainTable.addCell(cell);
        } else {
            PdfPTable othersTable = makeTable(4);
            othersTable.setWidthPercentage(100.5f);
            othersTable.setWidths(new float[]{25, 25, 25, 25});
            cell = makeCell("CONTAINER NO.", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthBottom(0.6f);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthRight(0.6f);
            othersTable.addCell(cell);
            cell = makeCell("BOOKING NO.", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthBottom(0.6f);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthRight(0.6f);
            othersTable.addCell(cell);
            cell = makeCell("ORIGIN", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthBottom(0.6f);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthRight(0.6f);
            othersTable.addCell(cell);
            cell = makeCell("DESTINATION", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthBottom(0.6f);
            cell.setBorderWidthTop(0.6f);
            othersTable.addCell(cell);
            if (isBl) {
                othersTable = fillMarksAndContinerInformation(othersTable, fclBl, messageResource, importFlag);
            } else if (isBooking) {
                othersTable = fillBookingInformation(othersTable, bookingFcl, messageResource, importFlag);
            }
            cell = makeCell("SHIPPER / FORWARDER", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setColspan(2);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthBottom(0.6f);
            othersTable.addCell(cell);
            cell = makeCell("CONSIGNEE", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setColspan(2);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthBottom(0.6f);
            othersTable.addCell(cell);
            StringBuilder shipperAddr = new StringBuilder();
            if (isBl) {
                shipperAddr.append(CommonUtils.isNotEmpty(fclBl.getShipperName()) ? fclBl.getShipperName() : "");
                if ((null != fclBl.getShipperName() && !fclBl.getShipperName().equals(""))
                        && (null != fclBl.getForwardingAgentName() && !fclBl.getForwardingAgentName().equals(""))) {
                    shipperAddr.append(" / ");
                }
                shipperAddr.append(CommonUtils.isNotEmpty(fclBl.getForwardingAgentName()) ? fclBl.getForwardingAgentName() : "");
            } else if (isBooking) {
                shipperAddr.append(CommonUtils.isNotEmpty(bookingFcl.getShipper()) ? bookingFcl.getShipper() : "");
                if ((null != bookingFcl.getShipper() && !bookingFcl.getShipper().equals(""))
                        && (null != bookingFcl.getForward() && !bookingFcl.getForward().equals(""))) {
                    shipperAddr.append(" / ");
                }
                shipperAddr.append(CommonUtils.isNotEmpty(bookingFcl.getForward()) ? bookingFcl.getForward() : "");
            }
            cell = makeCell(shipperAddr.toString(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            cell.setColspan(2);
            cell.setBorderWidthRight(0.6f);
            othersTable.addCell(cell);
            if (isBl) {
                cell = makeCell(CommonUtils.isNotEmpty(fclBl.getConsigneeName()) ? fclBl.getConsigneeName() : "", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            } else if (isBooking) {
                cell = makeCell(CommonUtils.isNotEmpty(bookingFcl.getConsignee()) ? bookingFcl.getConsignee() : "", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            } else {
                cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            }
            cell.setColspan(2);
            othersTable.addCell(cell);
            cell = makeCell("ECI SHIPMENT FILE NUMBER: ", Element.ALIGN_LEFT, blackBoldFontSize6, 0);
            cell.setBorderWidthTop(0.6f);
            othersTable.addCell(cell);
            cell = makeCell(CommonUtils.isNotEmpty(fileNo) ? "04-" + fileNo : arRedInvoice.getFileNo(), Element.ALIGN_LEFT, blackBoldFontSize6, 0);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthRight(0.6f);
            othersTable.addCell(cell);
            cell = makeCell("", Element.ALIGN_CENTER, headingFont, 0);
            cell.setColspan(2);
            othersTable.addCell(cell);
            cell = makeCell("DESCRIPTION / DETAILS", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthBottom(0.6f);
            cell.setColspan(4);
            othersTable.addCell(cell);
            cell = makeCellLeftNoBorderFclBL(CommonUtils.isNotEmpty(arRedInvoice.getNotes()) ? arRedInvoice.getNotes() : "");
            cell.setColspan(4);
            cell.setMinimumHeight(50);
            othersTable.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.addElement(othersTable);
            cell.setBorder(0);
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthLeft(0.6f);
            mainTable.addCell(cell);
        }
        PdfPTable chargesTable = makeTable(4);
        chargesTable.setWidthPercentage(100.5f);
        chargesTable.setWidths(new float[]{45, 35, 5, 15});
        cell = makeCell("CHARGES", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setColspan(4);
        chargesTable.addCell(cell);
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        String code = "";
        double totalCharges = 0.00;
        double lateFee = 0.00;
        double payAmount = 0.00;
        int chargeCount = 0;

        if (!lineItemList.isEmpty()) {
            for (Object object : lineItemList) {
                chargeCount++;
                String codeDesc = "";
                ArRedInvoiceCharges arRedInvoiceCharges = (ArRedInvoiceCharges) object;
                code = CommonUtils.isNotEmpty(arRedInvoiceCharges.getChargeCode()) ? arRedInvoiceCharges.getChargeCode() : "";
                codeDesc = genericCodeDAO.getGenericCodeDesc(code);
                chargesTable.addCell(makeCellLeftNoBorderFclBL(CommonUtils.isNotEmpty(codeDesc) ? codeDesc : ""));
                chargesTable.addCell(makeCellLeftNoBorderFclBL(arRedInvoiceCharges.getDescription()));
                cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, Rectangle.BOX);
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
            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, Rectangle.BOX);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            chargesTable.addCell(cell);
            cell = makeCell("", Element.ALIGN_RIGHT, blackFontForFclBl, Rectangle.BOX);
            cell.setBorderWidthLeft(0.0f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            cell.setMinimumHeight(10f);
            chargesTable.addCell(cell);
        }
        cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.6f);
        chargesTable.addCell(cell);
        cell = makeCell("INVOICE TOTAL", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        chargesTable.addCell(cell);
        cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        chargesTable.addCell(cell);
        cell = makeCell(number.format(totalCharges), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        chargesTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setColspan(4);
        chargesTable.addCell(cell);
        chargesTable.setKeepTogether(true);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(chargesTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthLeft(0.6f);
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
                if (CommonFunctions.isNotNullOrNotEmpty(tradingPartner.getAccounting())) {
                    for (CustomerAccounting customerAccounting : tradingPartner.getAccounting()) {
                        if (null != customerAccounting.getFclApplyLateFee() && customerAccounting.getFclApplyLateFee().equals("on")) {
                            if (arRedInvoice.getDueDate() != null) {
                                String dt = "";
                                int creditTerm = Integer.parseInt(customerAccounting.getCreditRate().getCode());
                                Calendar c = Calendar.getInstance();
                                c.setTime(arRedInvoice.getDueDate());
                                c.add(Calendar.DATE, creditTerm);
                                dt = sdf.format(c.getTime());
                                lateFeeDate = dt;
                                lateFeeFlag = true;
                            }
                        }
                        break;
                    }
                }
            }
        }

        if (lateFeeFlag) {
            lateFee = totalCharges * 0.015; // 1.5percent calculate
            payAmount = totalCharges + lateFee;
            cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0);
            paidTable.addCell(cell);
            cell = makeCell("LATE FEE IF NOT PAID BY - ", Element.ALIGN_LEFT, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthRight(0.6f);
            paidTable.addCell(cell);
            cell = makeCell(lateFeeDate, Element.ALIGN_LEFT, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.6f);
            paidTable.addCell(cell);
            cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthLeft(0.6f);
            paidTable.addCell(cell);
            cell = makeCell(number.format(lateFee), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.6f);
            paidTable.addCell(cell);
        }
        cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        paidTable.addCell(cell);
        cell = makeCell("PLEASE PAY THIS AMOUNT ", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        paidTable.addCell(cell);
        cell = makeCell("----->", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthTop(0.6f);
        paidTable.addCell(cell);
        cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        paidTable.addCell(cell);
        cell = makeCell(number.format(payAmount), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthTop(0.6f);
        paidTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setColspan(5);
        paidTable.addCell(cell);
        paidTable.setKeepTogether(true);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(paidTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthLeft(0.6f);
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
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthLeft(0.6f);
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
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthTop(0.6f);
        bankDetailsTable.addCell(cell);
        bankDetailsTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0));

        bankDetailsTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        cell = makeCell("AS FOLLOWS: ", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        bankDetailsTable.addCell(cell);
        bankDetailsTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("BANK: " + eftBank, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell(eftBankAddress, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("ACCT NAME: " + eftAcctName, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("Acct no.: " + eftAccountNo, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("ABA Routing no.: " + eftABANo, Element.ALIGN_CENTER, blackBoldFontSize6, 0));

        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setBorderWidthBottom(0.6f);
        bankDetailsTable.addCell(cell);
        cell = new PdfPCell();
        cell.addElement(bankDetails);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        bankDetailsTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setBorderWidthBottom(0.6f);
        bankDetailsTable.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(bankDetailsTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthLeft(0.6f);
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
    public String createReport(ArRedInvoice arRedInvoice, String fileName,
            String contextPath, MessageResources messageResource, User loginUser) throws Exception {
        this.initialize(fileName, contextPath, arRedInvoice);
        this.createBody(arRedInvoice, contextPath, messageResource, loginUser);
        this.destroy();
        return "fileName";
    }

    public String getTerms(String term) {
        String ssl = CommonUtils.isNotEmpty(term) ? term.substring(term.indexOf("-") + 1, term.length()) : "";
        if (!ssl.equals("")) {
            return ssl;
        }
        return "";
    }

    public PdfPTable fillMarksAndContinerInformation(PdfPTable particularsTable, FclBl bl, MessageResources messageResources, String importFlag)
            throws Exception {
        PdfPCell cell = null;
        NumberFormat numberFormat = new DecimalFormat("###,###,##0.000");
        Set<FclBlContainer> containerSet = bl.getFclcontainer();
        HashMap hashMap = new HashMap();
        List TempList = new ArrayList();
        List shortedContainerList = new ArrayList();
        List containerList = new ArrayList();
        for (Iterator iter = containerSet.iterator(); iter.hasNext();) {
            FclBlContainer fclBlCont = (FclBlContainer) iter.next();
            if (!"D".equalsIgnoreCase(fclBlCont.getDisabledFlag())) {
                hashMap.put(fclBlCont.getTrailerNoId(), fclBlCont);
                TempList.add(fclBlCont.getTrailerNoId());
            }
        }
        Collections.sort(TempList);
        int containerSize = 0;
        for (int i = 0; i < TempList.size(); i++) {
            FclBlContainer fclBlCont = (FclBlContainer) hashMap.get(TempList.get(i));
            containerList.add(fclBlCont);
        }
        if (containerList != null) {
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
                            cell.setBorderWidthRight(0.6f);
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
                    }
                }
            }
            if (count == 0) {
                cell = makeCellLeftNoBorderFontSize6("");
                cell.setBorderWidthRight(0.6f);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6("");
                cell.setBorderWidthRight(0.6f);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6("");
                cell.setBorderWidthRight(0.6f);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6(bl.getTerminal());
                cell.setBorderWidthRight(0.6f);
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

            if (null != bl.getBookingNo() && !"".equals(bl.getBookingNo())) {
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
                cell = makeCellLeftNoBorderFontSize6(bl.getTerminal());
                cell.setBorderWidthRight(0.6f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6(bl.getFinalDestination());
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                particularsTable.addCell(cell);
            }
        }
        return particularsTable;
    }

    public PdfPTable fillBookingInformation(PdfPTable particularsTable, BookingFcl bookingFcl, MessageResources messageResources, String importFlag)
            throws DocumentException {
        PdfPCell cell = null;
        int count = 0;
        if ("I".equalsIgnoreCase(importFlag)) {
            cell = makeCellLeftNoBorderFontSize6("");
            cell.setBorderWidthRight(0.6f);
            particularsTable.addCell(cell);

            cell = makeCellLeftNoBorderFclBL("");
            cell.setBorderWidthRight(0.6f);
            particularsTable.addCell(cell);

            cell = makeCellLeftNoBorderFclBL("");
            cell.setBorderWidthRight(0.6f);
            particularsTable.addCell(cell);

        } else {
            String bookingNo = "";
            cell = makeCellLeftNoBorderFontSize6("");
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidthRight(0.6f);
            particularsTable.addCell(cell);

            if (null != bookingFcl.getSSBookingNo() && !"".equals(bookingFcl.getSSBookingNo())) {
                bookingNo = bookingFcl.getSSBookingNo();
            } else {
                bookingNo = "";
            }
            cell = makeCellLeftNoBorderFontSize6(bookingNo.toString());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidthRight(0.6f);
            particularsTable.addCell(cell);

        }
        cell = makeCellLeftNoBorderFontSize6(null != bookingFcl.getOriginTerminal() ? bookingFcl.getOriginTerminal() : "");
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.6f);
        particularsTable.addCell(cell);

        cell = makeCellLeftNoBorderFontSize6(null != bookingFcl.getPortofDischarge() ? bookingFcl.getPortofDischarge() : "");
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
        if (arRedInvoice.getCustomerNumber() != null && !arRedInvoice.getCustomerNumber().isEmpty()) {
            FclBlBC blBC = new FclBlBC();
            String fileType = arRedInvoice.getFileType();
            if (null != fileType && (fileType.equalsIgnoreCase("I") || fileType.equalsIgnoreCase("true") || fileType.equalsIgnoreCase("LCLI"))) {
                fileType = "I";
            } else {
                fileType = "E";
            }
            String creditDetail = blBC.validateCustomer(arRedInvoice.getCustomerNumber(), fileType);
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
}
