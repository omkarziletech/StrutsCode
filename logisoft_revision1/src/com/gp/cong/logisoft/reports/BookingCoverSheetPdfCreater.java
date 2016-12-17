/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.fcl.CustAddressBC;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.reports.dto.SearchBookingReportDTO;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author Anshu
 */
public class BookingCoverSheetPdfCreater extends ReportFormatMethods {

    private final char checked = '\u00FE';
    private final char unchecked = '\u00A8';
    Document document = null;
    PdfWriter pdfWriter = null;
    protected PdfTemplate total;
    private String OUT_OF_GAUGE = "OUT OF GAUGE";
    protected BaseFont helv;
    Font blackBoldFont = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
    Font headingFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont1 = new Font(Font.BOLD, 21, 0, Color.BLACK);
    Font headingFont2 = new Font(Font.BOLD, 14, 0, Color.BLACK);
    Font redBoldFont = new Font(1, 14.0F, 1, Color.RED);
    Font blackFont = new Font(Font.HELVETICA, 9, 0, Color.BLACK);
    Font italicFont = new Font(Font.ITALIC, 8, Font.NORMAL, Color.BLACK);
    Font fontforYandN = new Font(1, 14.0F, 0, Color.BLACK);
    private MessageResources messageResources = null;
    private SearchBookingReportDTO searchBookingReportDTO = null;
    private String simpleRequest = null;
    private String documentName = null;
    private static final Logger log = Logger.getLogger(BookingCoverSheetPdfCreater.class);

    public BookingCoverSheetPdfCreater() {
    }

    public BookingCoverSheetPdfCreater(SearchBookingReportDTO searchBookingDTO, String simpleReq, MessageResources messageResource, String docName) {
        this.messageResources = messageResource;
        this.searchBookingReportDTO = searchBookingDTO;
        this.simpleRequest = simpleReq;
        this.documentName = docName;
    }

    public String createReport(SearchBookingReportDTO searchBookingReportDTO, String simpleRequest, MessageResources messageResources, String printFromBl, String documentName) throws Exception {
        try {
            this.initialize(searchBookingReportDTO, simpleRequest, messageResources, documentName);
            this.createBody(searchBookingReportDTO, simpleRequest, messageResources, printFromBl, documentName);
            this.destroy();
        } catch (Exception e) {
            log.info("create FCL Booking Cover Note failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
        return searchBookingReportDTO.getFileName();
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

    public void initialize(SearchBookingReportDTO searchBookingReportDTO, String simpleRequest, MessageResources messageResources, String documentName) throws FileNotFoundException,
            DocumentException {
        document = new Document(PageSize.A4);
        document.setMargins(15, 15, 10, 10);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(searchBookingReportDTO.getFileName()));
        pdfWriter.setPageEvent(new BookingCoverSheetPdfCreater(searchBookingReportDTO, simpleRequest, messageResources, documentName));
        document.open();
    }

    public void destroy() {
        document.close();
        pdfWriter.flush();
        pdfWriter.close();
    }

    public void createBody(SearchBookingReportDTO searchBookingReportDTO, String simpleRequest, MessageResources messageResources, String printFromBl, String documentName)
            throws DocumentException, MalformedURLException, IOException, Exception {
        NumberFormat numformat = new DecimalFormat("##,###,##0.00");
        PdfPCell cell = new PdfPCell();
        Paragraph paragraph;
        PdfPTable table;
        Phrase phrase;

        PdfPTable mainTable = makeTable(2);
        mainTable.setWidthPercentage(100);

        BookingFcl bookingFcl = searchBookingReportDTO.getBookingflFcl();
        FclBl fclBl = null;
        if (CommonUtils.isNotEmpty(printFromBl)) {
            fclBl = new FclBlDAO().getFileNoObject(bookingFcl.getFileNo());
        }
        String company = null;
        String contactName = "";
        String email = null;
        String phone = null;
        String fax = null;
        String address = null;
        QuotationDAO quotationDAO = new QuotationDAO();
        CustAddressBC custAddressBC = new CustAddressBC();
        Quotation quotation = quotationDAO.getFileNoObject(bookingFcl.getFileNo());
        if (null != quotation) {
            contactName = quotation.getContactname();
        }
        if (bookingFcl.getShippercheck() != null && bookingFcl.getShippercheck().equals("on")) {
            //from=bookingFcl.getShipper();
            company = bookingFcl.getShipper();
            email = bookingFcl.getShipperEmail();
            phone = bookingFcl.getShipperPhone();
            fax = bookingFcl.getShipperFax();
            address = bookingFcl.getAddressforShipper();
        } else if (bookingFcl.getForwardercheck() != null && bookingFcl.getForwardercheck().equals("on")) {
            company = bookingFcl.getForward();
            email = bookingFcl.getForwarderEmail();
            phone = bookingFcl.getForwarderPhone();
            fax = bookingFcl.getForwarderFax();
            address = bookingFcl.getAddressforForwarder();
        } else if (bookingFcl.getConsigneecheck() != null && bookingFcl.getConsigneecheck().equals("on")) {
            company = bookingFcl.getConsignee();
            email = bookingFcl.getConsingeeEmail();
            phone = bookingFcl.getConsingeePhone();
            fax = bookingFcl.getConsigneeFax();
            address = bookingFcl.getAddressforConsingee();
        } else {
            if (null != quotation) {
                company = quotation.getClientname();
                email = quotation.getEmail1();
                phone = quotation.getPhone();
                fax = quotation.getFax();
                CustAddress custAddress = custAddressBC.getClientAddress(quotation.getClientnumber());
                if (null != custAddress) {
                    address = custAddress.getAddress1();
                }
            }
        }

        PdfPTable headerTable = makeTable(3);
        headerTable.setWidths(new float[]{20, 50, 30});
        headerTable.setWidthPercentage(100);

        PdfPTable headerTable1 = makeTable(1);
        headerTable1.setWidths(new float[]{100});
        headerTable1.setWidthPercentage(100);

        PdfPCell headingCell1 = makeCell(new Phrase(messageResources.getMessage("fileNumberPrefix") + String.valueOf(bookingFcl.getFileNo()), headingFont1), Element.ALIGN_CENTER);
        headingCell1.setBackgroundColor(Color.LIGHT_GRAY);
        headingCell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerTable1.addCell(headingCell1);
        cell = makeCellleftNoBorder("");
        cell.addElement(headerTable1);
        headerTable.addCell(headerTable1);

        PdfPTable headerTable2 = makeTable(2);
        headerTable2.setWidths(new float[]{38, 62});
        headerTable2.setWidthPercentage(100);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
        SimpleDateFormat sdfForddMMyyyy = new SimpleDateFormat("dd-MMM-yyyy");
        String colon = ": ";
        String docCut = "";
        String carrierDocCut = "";
        String etd = "";
        String eta = "";
        if (bookingFcl.getDocCutOff() != null) {
            docCut = sdf.format(bookingFcl.getDocCutOff());
        }
        if (bookingFcl.getCarrierDocCut() != null) {
            carrierDocCut = sdf.format(bookingFcl.getCarrierDocCut());
        }
        if (bookingFcl.getEtd() != null) {
            etd = sdfForddMMyyyy.format(bookingFcl.getEtd());
        }
        if (bookingFcl.getEta() != null) {
            eta = sdfForddMMyyyy.format(bookingFcl.getEta());
        }

        headerTable2.addCell(makeCell(new Phrase("Doc Cutoff", headingFont2), Element.ALIGN_LEFT));
        headerTable2.addCell(makeCell(new Phrase(colon + docCut, redBoldFont), Element.ALIGN_LEFT));
        headerTable2.addCell(makeCell(new Phrase("Carrier Doc Cut", headingFont2), Element.ALIGN_LEFT));
        headerTable2.addCell(makeCell(new Phrase(colon + carrierDocCut, redBoldFont), Element.ALIGN_LEFT));

        cell = makeCellleftNoBorder("");
        cell.addElement(headerTable2);
        headerTable.addCell(headerTable2);

        PdfPTable headerTable3 = makeTable(2);
        headerTable3.setWidths(new float[]{20, 80});
        headerTable3.setWidthPercentage(100);

        headerTable3.addCell(makeCell(new Phrase("ETD", headingFont2), Element.ALIGN_LEFT));
        headerTable3.addCell(makeCell(new Phrase(colon + etd, headingFont2), Element.ALIGN_LEFT));
        headerTable3.addCell(makeCell(new Phrase("ETA", headingFont2), Element.ALIGN_LEFT));
        headerTable3.addCell(makeCell(new Phrase(colon + eta, headingFont2), Element.ALIGN_LEFT));

        cell = makeCellleftNoBorder("");
        cell.addElement(headerTable3);
        headerTable.addCell(headerTable3);

        document.add(headerTable);

        PdfPTable pTable = new PdfPTable(2);
        pTable.setWidthPercentage(100);

        PdfPTable clientTable = makeTable(2);
        clientTable.setWidths(new float[]{27, 73});
        clientTable.setWidthPercentage(100);

        clientTable.addCell(makeCellleftNoBorderWithBoldFont("Client Name"));
        clientTable.addCell(makeCellLeftNoBorderValue(company));
        clientTable.addCell(makeCellleftNoBorderWithBoldFont("Client Address"));
        clientTable.addCell(makeCellLeftNoBorderValue(address));
        clientTable.addCell(makeCellleftNoBorderWithBoldFont("Contact Name"));
        clientTable.addCell(makeCellLeftNoBorderValue(contactName));
        cell = makeCellleftNoBorderWithBoldFont("Contact Number");
        cell.setNoWrap(true);
        clientTable.addCell(cell);
        clientTable.addCell(makeCellLeftNoBorderValue(phone));
        clientTable.addCell(makeCellleftNoBorderWithBoldFont("Contact Fax"));
        clientTable.addCell(makeCellLeftNoBorderValue(fax));
        clientTable.addCell(makeCellleftNoBorderWithBoldFont("Contact Email"));
        clientTable.addCell(makeCellLeftNoBorderValue(email));

        cell = makeCellleftNoBorder("");
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.addElement(clientTable);
        pTable.addCell(cell);

        PdfPTable carrierTable = makeTable(2);
        carrierTable.setWidths(new float[]{26, 74});
        carrierTable.setWidthPercentage(100);
        String sslName[] = bookingFcl.getSslname().split("//");

        carrierTable.addCell(makeCellleftNoBorderWithBoldFont("Carrier"));
        carrierTable.addCell(makeCellLeftNoBorderValue(sslName.length > 1 ? sslName[0] : ""));
        carrierTable.addCell(makeCellleftNoBorderWithBoldFont("Vessel"));
        carrierTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getVessel()));
        carrierTable.addCell(makeCellleftNoBorderWithBoldFont("Voyage"));
        carrierTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getVoyageCarrier()));
        carrierTable.addCell(makeCellleftNoBorderWithBoldFont("SSL Booking#"));
        carrierTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getSSBookingNo()));
        carrierTable.addCell(makeCellleftNoBorderWithBoldFont("POO"));
        carrierTable.addCell(makeCellLeftNoBorderValue(getCityStateName(bookingFcl.getOriginTerminal())));
        carrierTable.addCell(makeCellleftNoBorderWithBoldFont("POL"));
        carrierTable.addCell(makeCellLeftNoBorderValue(getCityStateName(bookingFcl.getPortofOrgin())));
        carrierTable.addCell(makeCellleftNoBorderWithBoldFont("POD"));
        carrierTable.addCell(makeCellLeftNoBorderValue(getCityStateName(bookingFcl.getDestination())));
        carrierTable.addCell(makeCellleftNoBorderWithBoldFont("FD"));
        carrierTable.addCell(makeCellLeftNoBorderValue(getCityStateName(bookingFcl.getPortofDischarge())));
        carrierTable.addCell(makeCellleftNoBorderWithBoldFont("Containers"));
        carrierTable.addCell(makeCellLeftNoBorderValue(this.getContainers(simpleRequest, searchBookingReportDTO, messageResources).getContent()));
//        PdfPCell cell1 = makeCell(this.getContainers(simpleRequest, searchBookingReportDTO, messageResources), 1);
//        cell1.setColspan(2);
//        cell1.setBorderWidthLeft(0.0F);
//        carrierTable.addCell(cell1);

        cell = makeCellleftNoBorder("");
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.addElement(carrierTable);

        pTable.addCell(cell);

        document.add(pTable);

        PdfPTable hazardsMainTable = makeTable(1);
        hazardsMainTable.setWidths(new float[]{100});
        hazardsMainTable.setWidthPercentage(100);

        PdfPTable hazardsTable = makeTable(3);
        hazardsTable.setWidths(new float[]{70, 15, 15});
        hazardsTable.setWidthPercentage(100);

        Image yes = Image.getInstance(searchBookingReportDTO.getContextPath() + "/images/icons/y.png");
        yes.scalePercent(60);
        yes.scaleAbsoluteWidth(18);
        PdfPCell yesCell = new PdfPCell();
        yesCell.setBorder(0);
        yesCell.addElement(new Chunk(yes, 32, -0));

        Image no = Image.getInstance(searchBookingReportDTO.getContextPath() + "/images/icons/n.png");
        no.scalePercent(60);
        no.scaleAbsoluteWidth(18);
        PdfPCell noCell = new PdfPCell();
        noCell.setBorder(0);
        noCell.addElement(new Chunk(no, 32, -0));

        hazardsTable.addCell(makeCellleftNoBorderWithBoldFont("HAZARDOUS"));
        if (bookingFcl.getHazmat() != null && "Y".equalsIgnoreCase(bookingFcl.getHazmat())) {
            hazardsTable.addCell(yesCell);
            hazardsTable.addCell(makeCell(new Phrase("N", fontforYandN), 1));
        } else {
            hazardsTable.addCell(makeCell(new Phrase("Y", fontforYandN), 1));
            hazardsTable.addCell(noCell);
        }
        hazardsTable.addCell(makeCellleftNoBorderWithBoldFont("AES"));
        hazardsTable.addCell(makeCell(new Phrase("Y", fontforYandN), Element.ALIGN_CENTER));
        hazardsTable.addCell(makeCell(new Phrase("N", fontforYandN), Element.ALIGN_CENTER));
        hazardsTable.addCell(makeCellleftNoBorderWithBoldFont("DIRECT CONSIGMENT"));
        if (bookingFcl.getDirectConsignmntCheck() != null && "on".equalsIgnoreCase(bookingFcl.getDirectConsignmntCheck())) {
            hazardsTable.addCell(yesCell);
            hazardsTable.addCell(makeCell(new Phrase("N", fontforYandN), 1));
        } else {
            hazardsTable.addCell(makeCell(new Phrase("Y", fontforYandN), 1));
            hazardsTable.addCell(noCell);
        }
        hazardsTable.addCell(makeCellleftNoBorderWithBoldFont("AFR"));
        hazardsTable.addCell(makeCell(new Phrase("Y", fontforYandN), Element.ALIGN_CENTER));
        hazardsTable.addCell(makeCell(new Phrase("N", fontforYandN), Element.ALIGN_CENTER));

        cell = makeCellleftNoBorder("");
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.addElement(hazardsTable);

        hazardsMainTable.addCell(cell);
        document.add(hazardsMainTable);

        PdfPTable docsMainTable = makeTable(2);
        docsMainTable.setWidths(new float[]{30, 70});
        docsMainTable.setWidthPercentage(100);

        PdfPTable docsTable = makeTable(2);
        docsTable.setWidths(new float[]{50, 50});
        docsTable.setWidthPercentage(100);

        Image img = Image.getInstance(searchBookingReportDTO.getContextPath() + "/images/icons/uncheckedBox.png");
        img.scalePercent(50);
        img.scaleAbsoluteWidth(18);
        PdfPCell pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.addElement(new Chunk(img, 25, -0));

//        Image checkedimg = Image.getInstance(searchBookingReportDTO.getContextPath() + "/images/icons/check.png");
//        checkedimg.scalePercent(60);
//        checkedimg.scaleAbsoluteWidth(18);
//        PdfPCell checkedCell = new PdfPCell();
//        checkedCell.setBorder(0);
//        checkedCell.addElement(new Chunk(checkedimg, 25, -0));
        docsTable.addCell(makeCellleftNoBorderWithBoldFont("Docs"));
        docsTable.addCell(pCell);

        docsTable.addCell(makeCellNoBorders("       "));
        docsTable.addCell(makeCellNoBorders("       "));

        docsTable.addCell(makeCellleftNoBorderWithBoldFont("COB"));
        docsTable.addCell(pCell);

        docsTable.addCell(makeCellNoBorders("       "));
        docsTable.addCell(makeCellNoBorders("       "));

        docsTable.addCell(makeCellleftNoBorderWithBoldFont("MBL"));
        docsTable.addCell(pCell);

        docsTable.addCell(makeCellNoBorders("       "));
        docsTable.addCell(makeCellNoBorders("       "));

        docsTable.addCell(makeCellleftNoBorderWithBoldFont("Manifisted"));
        docsTable.addCell(pCell);

        cell = makeCellleftNoBorder("");
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.addElement(docsTable);
        docsMainTable.addCell(cell);

        PdfPTable noteTable = makeTable(1);
        noteTable.setWidths(new float[]{100});
        noteTable.setWidthPercentage(100);
        noteTable.addCell(makeCellCenterNoBorder("Notes"));
        noteTable.addCell(makeCellNoBorders("       "));
        noteTable.addCell(makeCellleftwithUnderLine("            "));
        noteTable.addCell(makeCellNoBorders("       "));
        noteTable.addCell(makeCellleftwithUnderLine("            "));
        noteTable.addCell(makeCellNoBorders("       "));
        noteTable.addCell(makeCellleftwithUnderLine("            "));
        noteTable.addCell(makeCellNoBorders("       "));
        noteTable.addCell(makeCellleftwithUnderLine("            "));
        noteTable.addCell(makeCellNoBorders("       "));

        cell = makeCellleftNoBorder("");
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.addElement(noteTable);
        docsMainTable.addCell(cell);
        document.add(docsMainTable);

    }

    public void onStartPage(PdfWriter writer, Document document) {
        try {

            PdfPCell cell = new PdfPCell();
            PdfPCell celL = new PdfPCell();
            PdfPTable table = new PdfPTable(1);
            FclBl fclBl = null;
            String brand = "";
            String path = LoadLogisoftProperties.getProperty("application.image.logo");
            String econoPath = LoadLogisoftProperties.getProperty("application.image.econo.logo");
            String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
            BookingFcl bookingFcl = searchBookingReportDTO.getBookingflFcl();
            fclBl = new FclBlDAO().getOriginalBl(bookingFcl.getFileNo());
            if (null != fclBl && null != fclBl.getBrand()) {
                brand = fclBl.getBrand();
            } else if (null != bookingFcl && null != bookingFcl.getBrand()) {
                brand = bookingFcl.getBrand();
            }
            if (brand.equals("Econo") && ("03").equals(companyCode)) {
                Image img = Image.getInstance(searchBookingReportDTO.getContextPath() + econoPath);
                table.setWidthPercentage(100);
                img.scalePercent(60);
                //img.scaleAbsoluteWidth(200);
                celL.addElement(new Chunk(img, 180, -20));
                celL.setBorder(0);
                celL.setHorizontalAlignment(Element.ALIGN_CENTER);
                celL.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(celL);
//            DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
//            Date currentDate = new Date();
//            cell = makeCellRightNoBorder("Date Printed : " + df7.format(currentDate));
                cell = makeCellRightNoBorder("  ");
                cell.setPaddingTop(10f);
                table.addCell(cell);
            } else if (brand.equals("OTI") && ("02").equals(companyCode)) {
                Image img = Image.getInstance(searchBookingReportDTO.getContextPath() + econoPath);
                table.setWidthPercentage(100);
                img.scalePercent(60);
                //img.scaleAbsoluteWidth(200);
                celL.addElement(new Chunk(img, 180, -20));
                celL.setBorder(0);
                celL.setHorizontalAlignment(Element.ALIGN_CENTER);
                celL.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(celL);
//            DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
//            Date currentDate = new Date();
//            cell = makeCellRightNoBorder("Date Printed : " + df7.format(currentDate));
                cell = makeCellRightNoBorder("  ");
                cell.setPaddingTop(10f);
                table.addCell(cell);
            } else if (brand.equalsIgnoreCase("Ecu Worldwide")) {
                Image img = Image.getInstance(searchBookingReportDTO.getContextPath() + path);
                table.setWidthPercentage(100);
                img.scalePercent(60);
                //img.scaleAbsoluteWidth(200);
                celL.addElement(new Chunk(img, 180, -20));
                celL.setBorder(0);
                celL.setHorizontalAlignment(Element.ALIGN_CENTER);
                celL.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(celL);
//            DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
//            Date currentDate = new Date();
//            cell = makeCellRightNoBorder("Date Printed : " + df7.format(currentDate));
                cell = makeCellRightNoBorder("  ");
                cell.setPaddingTop(10f);
                table.addCell(cell);
            }

            // table for heading
//            PdfPTable heading = makeTable(1);
//            heading.setWidthPercentage(100);            
//             heading.addCell(makeCellCenterForDoubleHeading("FCL Booking Confirmation " + messageResources.getMessage("fileNumberPrefix") + String.valueOf(bookingFcl.getFileNo())));
//            
            document.add(table);
//            document.add(heading);
        } catch (Exception e) {
            log.info("onStartPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {

            //---------------
            //this for print page number at the bottom in the format x of y
//            PdfContentByte cb = writer.getDirectContent();
//            cb.saveState();
//            String text = "Page " + writer.getPageNumber() + " of ";
//            float textBase = document.bottom() - (document.bottomMargin() - 10);
//            //float textBase = document.bottom() - 20;
//            float textSize = helv.getWidthPoint(text, 12);
//            cb.beginText();
//            cb.setFontAndSize(helv, 7);
//            cb.setTextMatrix(document.left() + 280, textBase);
//            cb.showText(text);
//            cb.endText();
//            cb.addTemplate(total, document.left() + 260 + textSize, textBase);
//            cb.restoreState();
        } catch (Exception e) {
            log.info("onEndPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    public Paragraph getContainers(String simpleRequest, SearchBookingReportDTO searchBookingReportDTO, MessageResources messageResources) {
        if (simpleRequest.equals("simpleRequest")) {
            Paragraph bkgParagraph1 = new Paragraph();
            List chargesList = searchBookingReportDTO.getChargesList();
            BookingfclUnits bookingfclUnits = new BookingfclUnits();
            String container = "";
            Map<String, String> summaryMap = new HashMap<String, String>();
            if (chargesList != null && chargesList.size() > 0) {
                boolean isFirst = true;
                for (int i = 0; i < chargesList.size(); i++) {
                    bookingfclUnits = (BookingfclUnits) chargesList.get(i);
                    if (bookingfclUnits.getApproveBl() != null && bookingfclUnits.getApproveBl().equalsIgnoreCase("Yes")) {
                        if (bookingfclUnits.getPrint() != null && !bookingfclUnits.getPrint().equalsIgnoreCase("on")) {
                            String temp = bookingfclUnits.getUnitType().getCodedesc().toString() + "-" + bookingfclUnits.getSpecialEquipmentUnit() + "-" + bookingfclUnits.getStandardCharge();
                            String newTemp = bookingfclUnits.getUnitType().getCodedesc();
                            if (null == summaryMap.get(temp)) {
                                String tempArray[] = newTemp.split("=");
//                                if (!isFirst) {
//                                    bkgParagraph1.add(new Phrase(" \n"));
//                                }
                                if (tempArray[1].equalsIgnoreCase(messageResources.getMessage("container40HC"))) {
                                    container = bookingfclUnits.getNumbers() + " X " + "40" + "'" + "HC ";
                                    bkgParagraph1.add(new Chunk(container, blackBoldFont2));
                                    if ("Y".equalsIgnoreCase(bookingfclUnits.getOutOfGauge())) {
                                        bkgParagraph1.add(new Phrase(", "));
                                        bkgParagraph1.add(new Chunk(OUT_OF_GAUGE, GreenFont8));
                                    }
                                    if (null != bookingfclUnits.getSpecialEquipment() && !bookingfclUnits.getSpecialEquipment().equals("")) {
                                        bkgParagraph1.add(new Phrase(", "));
                                        bkgParagraph1.add(new Chunk(bookingfclUnits.getSpecialEquipment(), GreenFont8));
                                    }
                                    isFirst = false;
                                } else if (tempArray[1].equalsIgnoreCase(messageResources.getMessage("container40NOR"))) {
                                    container = bookingfclUnits.getNumbers() + " X " + "40" + "'" + "NOR ";
                                    bkgParagraph1.add(new Chunk(container, blackBoldFont2));
                                    if ("Y".equalsIgnoreCase(bookingfclUnits.getOutOfGauge())) {
                                        bkgParagraph1.add(new Phrase(", "));
                                        bkgParagraph1.add(new Chunk(OUT_OF_GAUGE, GreenFont8));
                                    }
                                    if (null != bookingfclUnits.getSpecialEquipment() && !bookingfclUnits.getSpecialEquipment().equals("")) {
                                        bkgParagraph1.add(new Phrase(", "));
                                        bkgParagraph1.add(new Chunk(bookingfclUnits.getSpecialEquipment(), GreenFont8));
                                    }
                                    isFirst = false;
                                } else {
                                    container = bookingfclUnits.getNumbers() + " X " + tempArray[1] + "' ";
                                    bkgParagraph1.add(new Chunk(container, blackBoldFont2));
                                    if ("Y".equalsIgnoreCase(bookingfclUnits.getOutOfGauge())) {
                                        bkgParagraph1.add(new Phrase(", "));
                                        bkgParagraph1.add(new Chunk(OUT_OF_GAUGE, GreenFont8));
                                    }
                                    if (null != bookingfclUnits.getSpecialEquipment() && !bookingfclUnits.getSpecialEquipment().equals("")) {
                                        bkgParagraph1.add(new Phrase(", "));
                                        bkgParagraph1.add(new Chunk(bookingfclUnits.getSpecialEquipment(), GreenFont8));
                                    }
                                    isFirst = false;
                                }
                            }
                            summaryMap.put(temp, temp);
                        }
                    }
                }
            }
            return (bkgParagraph1);
        }
        return new Paragraph();
    }

    public String getCityStateName(String name) throws Exception {
        String finalName = "";
        if (null != name && !name.equalsIgnoreCase("")) {
            String subName = "";
            subName = StringFormatter.orgDestStringFormatter(name);
            name = StringFormatter.getTerminalFromInputStringr(name);
            if (subName.startsWith("US")) {
                UnLocationDAO unLocationDAO = new UnLocationDAO();
                UnLocation unlocation = unLocationDAO.getUnlocation(subName);
                if (unlocation != null && unlocation.getStateId() != null && unlocation.getStateId().getCode() != null) {
                    finalName = name + "/" + unlocation.getStateId().getCode();
                } else {
                    finalName = name;
                }
            } else {
                UnLocationDAO unLocationDAO = new UnLocationDAO();
                UnLocation unlocation = unLocationDAO.getUnlocation(subName);
                if (unlocation != null && unlocation.getCountryId() != null && unlocation.getCountryId().getCodedesc() != null) {
                    finalName = name + "/" + unlocation.getCountryId().getCodedesc();
                } else {
                    finalName = name;
                }
            }
        }
        return finalName;
    }
}
