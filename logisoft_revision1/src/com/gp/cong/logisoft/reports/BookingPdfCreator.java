package com.gp.cong.logisoft.reports;

import com.gp.cong.common.CommonUtils;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.MessageResources;

import com.gp.cong.logisoft.bc.fcl.CustAddressBC;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.bc.fcl.QuotationConstants;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.reports.dto.SearchBookingReportDTO;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
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
import com.gp.cong.logisoft.bc.fcl.BookingDwrBC;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.lowagie.text.Paragraph;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class BookingPdfCreator extends ReportFormatMethods {

    Document document = null;
    PdfWriter pdfWriter = null;
    protected PdfTemplate total;
    private String OUT_OF_GAUGE = "OUT OF GAUGE";
    protected BaseFont helv;
    Font blackBoldFont = new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK);
    Font headingFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont1 = new Font(Font.BOLD, 18, 0, Color.BLACK);
    Font blackFont = new Font(Font.HELVETICA, 9, 0, Color.BLACK);
    Font italicFont = new Font(Font.ITALIC, 8, Font.NORMAL, Color.BLACK);
    private MessageResources messageResources = null;
    private SearchBookingReportDTO searchBookingReportDTO = null;
    private String simpleRequest = null;
    private String documentName = null;
    private static final Logger log = Logger.getLogger(BookingPdfCreator.class);

    public BookingPdfCreator() {
    }

    public BookingPdfCreator(SearchBookingReportDTO searchBookingDTO, String simpleReq, MessageResources messageResource, String docName) {
        messageResources = messageResource;
        searchBookingReportDTO = searchBookingDTO;
        simpleRequest = simpleReq;
        documentName = docName;
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
        pdfWriter.setPageEvent(new BookingPdfCreator(searchBookingReportDTO, simpleRequest, messageResources, documentName));
        document.open();
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
//                img.scaleAbsoluteWidth(200);
//                celL.addElement(new Chunk(img, 180, -20));
                img.setAlignment(Element.ALIGN_CENTER);
                celL.addElement(img);
                celL.setBorder(0);
                celL.setHorizontalAlignment(Element.ALIGN_CENTER);
                celL.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(celL);
                DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
                Date currentDate = new Date();
                cell = makeCellRightNoBorder("Date Printed : " + df7.format(currentDate));
                cell.setPaddingTop(10f);
                table.addCell(cell);
            } else if (brand.equals("OTI") && ("02").equals(companyCode)) {

                Image img = Image.getInstance(searchBookingReportDTO.getContextPath() + econoPath);
                table.setWidthPercentage(100);
                img.scalePercent(60);
//                img.scaleAbsoluteWidth(200);
//                celL.addElement(new Chunk(img, 180, -20));
                img.setAlignment(Element.ALIGN_CENTER);
                celL.addElement(img);
                celL.setBorder(0);
                celL.setHorizontalAlignment(Element.ALIGN_CENTER);
                celL.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(celL);
                DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
                Date currentDate = new Date();
                cell = makeCellRightNoBorder("Date Printed : " + df7.format(currentDate));
                cell.setPaddingTop(10f);
                table.addCell(cell);
            } else if (brand.equalsIgnoreCase("Ecu Worldwide")) {

                Image img = Image.getInstance(searchBookingReportDTO.getContextPath() + path);
                table.setWidthPercentage(100);
                img.scalePercent(60);
//                img.scaleAbsoluteWidth(200);
//                celL.addElement(new Chunk(img, 180, -20));
                img.setAlignment(Element.ALIGN_CENTER);
                celL.addElement(img);
                celL.setBorder(0);
                celL.setHorizontalAlignment(Element.ALIGN_CENTER);
                celL.setVerticalAlignment(Element.ALIGN_CENTER);
                table.addCell(celL);
                DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
                Date currentDate = new Date();
                cell = makeCellRightNoBorder("Date Printed : " + df7.format(currentDate));
                cell.setPaddingTop(10f);
                table.addCell(cell);
            }

            PdfPTable heading = makeTable(1);
            heading.setWidthPercentage(100);
            if (!"Pickup Order".equalsIgnoreCase(documentName)) {
                heading.addCell(makeCellCenterForDoubleHeading("FCL Booking Confirmation " + messageResources.getMessage("fileNumberPrefix") + String.valueOf(bookingFcl.getFileNo())));
            } else {
                heading.addCell(makeCellCenterForDoubleHeading("FCL Pickup Order " + messageResources.getMessage("fileNumberPrefix") + String.valueOf(bookingFcl.getFileNo())));
            }
            document.add(table);
            document.add(heading);
        } catch (Exception e) {
            log.info("onStartPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    public void createBody(SearchBookingReportDTO searchBookingReportDTO, String simpleRequest,
            MessageResources messageResources, String printFromBl, String documentName,
            String fromEmailAddress, String fromName, HttpServletRequest request)
            throws DocumentException, MalformedURLException, IOException, Exception {
        NumberFormat numformat = new DecimalFormat("##,###,##0.00");
        PdfPCell cell = new PdfPCell();
        PdfPTable mainTable = makeTable(2);
        mainTable.setWidthPercentage(100);

        PdfPTable emptyTable = makeTable(1);
        cell = makeCellleftNoBorder("");
        emptyTable.addCell(cell);
        BookingFcl bookingFcl = searchBookingReportDTO.getBookingflFcl();
        FclBl fclBl = null;
        if (CommonUtils.isNotEmpty(printFromBl)) {
            fclBl = new FclBlDAO().getFileNoObject("" + bookingFcl.getFileNo());
        }
        String from = null;
        String company = null;
        String contactName = "";
        String attention = null;
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
        //remarks
        String originCode = null;
        String propRemarks = null;
        if (bookingFcl.getOriginTerminal() != null && !bookingFcl.getOriginTerminal().equalsIgnoreCase("")) {
            if (bookingFcl.getOriginTerminal().lastIndexOf("(") != -1) {
                originCode = bookingFcl.getOriginTerminal().substring(bookingFcl.getOriginTerminal().lastIndexOf("(") + 1,
                        bookingFcl.getOriginTerminal().lastIndexOf(")"));
            }
        }
        if (bookingFcl.getZip() != null && !bookingFcl.getZip().trim().equals("")) {
            propRemarks = new UnLocationDAO().getpropertyRemarks(originCode);
        }
        // table for booking details

        //client table
        PdfPTable clientTable = makeTable(2);
        clientTable.setWidths(new float[]{27, 73});
        clientTable.setWidthPercentage(100);
        if ("Pickup Order".equalsIgnoreCase(documentName)) {
            cell = makeCellleftNoBorderWithBoldFont("Spotting Information");
            cell.setColspan(3);
            clientTable.addCell(cell);
            clientTable.addCell(makeCellleftNoBorderWithBoldFont("Spot Date"));
            if (CommonUtils.isNotEmpty(bookingFcl.getTimeCheckBox()) && bookingFcl.getTimeCheckBox().equals("on")) {
                clientTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getPositioningDate()));
            } else {
                clientTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getPositioningDate()));
            }
            clientTable.addCell(makeCellleftNoBorderWithBoldFont("Contact"));
            clientTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getLoadcontact()));
            clientTable.addCell(makeCellleftNoBorderWithBoldFont("Phone"));
            clientTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getLoadphone()));
            clientTable.addCell(makeCellleftNoBorderWithBoldFont("Name"));
            clientTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getSpottingAccountName()));
            clientTable.addCell(makeCellleftNoBorderWithBoldFont("Address"));
            StringBuilder spottingAddress = new StringBuilder("");
            if (CommonUtils.isNotEmpty(bookingFcl.getAddressForExpPositioning())) {
                spottingAddress.append(bookingFcl.getAddressForExpPositioning());
                if (CommonUtils.isNotEmpty(bookingFcl.getSpotAddrCity())) {
                    spottingAddress.append(", ");
                    spottingAddress.append(bookingFcl.getSpotAddrCity());
                }
                if (CommonUtils.isNotEmpty(bookingFcl.getSpotAddrState())) {
                    spottingAddress.append(", ");
                    spottingAddress.append(bookingFcl.getSpotAddrState());
                }
                if (CommonUtils.isNotEmpty(bookingFcl.getSpotAddrZip())) {
                    spottingAddress.append(", ");
                    spottingAddress.append(bookingFcl.getSpotAddrZip());
                }
            }
            clientTable.addCell(makeCellLeftNoBorderValue(spottingAddress.toString()));
            clientTable.addCell(makeCellleftNoBorderWithBoldFont("Remarks"));
            clientTable.addCell(makeCellLeftNoBorderValue((null != bookingFcl.getLoadRemarks()) ? bookingFcl.getLoadRemarks().toUpperCase() : ""));
        } else {
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
        }
        cell = makeCellleftNoBorder("");
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.addElement(clientTable);
        mainTable.addCell(cell);
        //booking number table
        PdfPTable booking = makeTable(1);
        booking.setWidthPercentage(101);

        PdfPTable bookingNo = makeTable(4);
        bookingNo.setWidths(new float[]{24, 28, 23, 25});
        bookingNo.setWidthPercentage(100);

        bookingNo.addCell(makeCellleftNoBorderWithBoldFontWithGreen("SSL BKG #"));
        cell = makeCellLeftNoBorderValueGreen(bookingFcl.getSSBookingNo());
        cell.setColspan(3);
        bookingNo.addCell(cell);
        bookingNo.addCell(makeCellleftNoBorderWithBoldFont("Booking date"));
        cell = makeCellLeftForDateNoBorderWithColonInDateMonthYearFormat(bookingFcl.getBookingDate());
        cell.setColspan(3);
        bookingNo.addCell(cell);
        bookingNo.addCell(makeCellleftNoBorderWithBoldFont("Booked By"));
        UserDAO userDao = new UserDAO();
        User user = userDao.findUserName(bookingFcl.getBookedBy() != null ? bookingFcl.getBookedBy() : "");
        if (user != null) {
            if (CommonUtils.isNotEmpty(fromEmailAddress)) {
                String terminalNo = bookingFcl.getIssuingTerminal().substring(bookingFcl.getIssuingTerminal().lastIndexOf("-") + 1);
                RefTerminal refTerminal = new RefTerminalDAO().getTerminal(terminalNo);
                cell = makeCellLeftNoBorderValue(fromName);
                cell.setColspan(3);
                bookingNo.addCell(cell);
                bookingNo.addCell(makeCellleftNoBorderWithBoldFont("Email"));
                cell = makeCellLeftNoBorderValue(fromEmailAddress);
                cell.setColspan(3);
                bookingNo.addCell(cell);
                bookingNo.addCell(makeCellleftNoBorderWithBoldFont("Phone"));
                cell = makeCellLeftNoBorderValue(null != refTerminal.getPhnnum1() ? refTerminal.getPhnnum1() : "");
                cell.setColspan(3);
                bookingNo.addCell(cell);
                bookingNo.addCell(makeCellleftNoBorderWithBoldFont("Fax"));
                cell = makeCellLeftNoBorderValue(null != refTerminal.getFaxnum1() ? refTerminal.getFaxnum1() : "");
                cell.setColspan(3);
                bookingNo.addCell(cell);
                cell = makeCellleftNoBorderWithBoldFont("");
                cell.addElement(bookingNo);
                cell.setBorderWidthBottom(0.6f);
                booking.addCell(cell);
            } else {
                String bookedBy1 = user.getFirstName() != null ? user.getFirstName() : "";
                String bookedBy2 = user.getLastName() != null ? user.getLastName() : "";
                String bookedBy = bookedBy1 + " " + bookedBy2;
                cell = makeCellLeftNoBorderValue(bookedBy);
                cell.setColspan(3);
                bookingNo.addCell(cell);
                bookingNo.addCell(makeCellleftNoBorderWithBoldFont("Email"));
                cell = makeCellLeftNoBorderValue(user.getEmail() != null ? user.getEmail() : "");
                cell.setColspan(3);
                bookingNo.addCell(cell);
                bookingNo.addCell(makeCellleftNoBorderWithBoldFont("Phone"));
                cell = makeCellLeftNoBorderValue(user.getTelephone() != null ? user.getTelephone() : "");
                cell.setColspan(3);
                bookingNo.addCell(cell);
                bookingNo.addCell(makeCellleftNoBorderWithBoldFont("Fax"));
                cell = makeCellLeftNoBorderValue(user.getFax() != null ? user.getFax() : "");
                cell.setColspan(3);
                bookingNo.addCell(cell);
                cell = makeCellleftNoBorderWithBoldFont("");
                cell.addElement(bookingNo);
                cell.setBorderWidthBottom(0.6f);
                booking.addCell(cell);
            }
        }
//       session.removeAttribute("valueFromTerminal");
//       session.removeAttribute("phoneFromTerminal");
//       session.removeAttribute("faxFromTerminal");
//       session.removeAttribute("nameTerminal");
        //forwarder name and address table
        PdfPTable forwarderTable = makeTable(2);
        forwarderTable.setWidths(new float[]{35, 65});
        forwarderTable.setWidthPercentage(100);
        forwarderTable.addCell(makeCellleftNoBorderWithBoldFont("Forwarder Name"));
        if (null != bookingFcl.getForward() && !bookingFcl.getForward().trim().equalsIgnoreCase("NO FF ASSIGNED".trim())
                && !bookingFcl.getForward().trim().equalsIgnoreCase("NO FF ASSIGNED / B/L PROVIDED".trim())
                && !bookingFcl.getForward().trim().equalsIgnoreCase("NO FRT. FORWARDER ASSIGNED".trim())) {
            forwarderTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getForward()));
        } else {
            forwarderTable.addCell(makeCellLeftNoBorderValue(""));

        }
        forwarderTable.addCell(makeCellleftNoBorderWithBoldFont("Consignee Name"));
        forwarderTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getConsignee()));
        booking.addCell(forwarderTable);
        cell = makeCellleftNoBorderWithBoldFont("");
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.addElement(booking);

        mainTable.addCell(cell);

        //goods description table
        PdfPTable goodsDescTable = makeTable(1);
        goodsDescTable.setWidthPercentage(100);
        cell = makeCellCenterNoBorderWithBoldFont("Goods Description");
        cell.setPaddingTop(-2);
        goodsDescTable.addCell(cell);
        goodsDescTable.addCell(makeCellleftNoBorder(bookingFcl.getGoodsDescription()));
        cell = makeCellleftNoBorderWithBoldFont("");
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.addElement(goodsDescTable);
        mainTable.addCell(cell);

        //booking remarks table
        PdfPTable remarksTable = makeTable(1);
        remarksTable.setWidthPercentage(100);
        cell = makeCellCenterNoBorderWithBoldFont("Booking Remarks");
        cell.setPaddingTop(-2);
        remarksTable.addCell(cell);
        remarksTable.addCell(makeCellleftNoBorder(bookingFcl.getRemarks()));
        cell = makeCellleftNoBorderWithBoldFont("");
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.0f);
        cell.addElement(remarksTable);
        mainTable.addCell(cell);

        //trade route table
        PdfPTable tradeRouteTable = makeTable(2);
        tradeRouteTable.setWidthPercentage(100);
        tradeRouteTable.setWidths(new float[]{30, 70});
        if (!bookingFcl.getDoorOrigin().equals("") && !bookingFcl.getDoorOrigin().equalsIgnoreCase("null")
                && bookingFcl.getDoorOrigin() != null) {
            String zip = bookingFcl.getZip();
            if (null != zip && zip.indexOf("-") > -1) {
                zip = zip.substring(0, zip.indexOf("-"));
            }
            if (bookingFcl.getRampCheck() != null && bookingFcl.getRampCheck().equalsIgnoreCase("on") && null != bookingFcl.getDoorOrigin()) {
                tradeRouteTable.addCell(makeCellleftNoBorderWithBoldFont("Ramp Origin"));
                cell = makeCellLeftNoBorderValue(String.valueOf(bookingFcl.getDoorOrigin()));
            } else {
                tradeRouteTable.addCell(makeCellleftNoBorderWithBoldFont("Door Origin"));
                if (null != bookingFcl.getDoorOrigin() && bookingFcl.getDoorOrigin().length() > 20) {
                    cell = makeCellLeftNoBorderValue(String.valueOf(bookingFcl.getDoorOrigin()) + "    " + "Zip" + ": " + zip);
                } else {
                    cell = makeCellLeftNoBorderValue(String.valueOf(bookingFcl.getDoorOrigin()) + "    " + "Origin Zip" + ": " + zip);
                }
            }
            cell.setNoWrap(true);
            tradeRouteTable.addCell(cell);
        }
        if (bookingFcl.getOriginTerminal() != null && !bookingFcl.getOriginTerminal().equals("")
                && !bookingFcl.getOriginTerminal().equalsIgnoreCase("null")) {
            tradeRouteTable.addCell(makeCellleftNoBorderWithBoldFont("Origin"));
            tradeRouteTable.addCell(makeCellLeftNoBorderValue(getCityStateName(bookingFcl.getOriginTerminal())));
        }

        if (bookingFcl.getPortofOrgin() != null && bookingFcl.getRampCity() != null && bookingFcl.getRampCity().replace("/ ", "/").trim().equals(bookingFcl.getPortofOrgin().
                replace("/ ", "/").trim())) {
            if (!bookingFcl.getPortofOrgin().equalsIgnoreCase("null") && !bookingFcl.getPortofOrgin().equalsIgnoreCase("")) {
                tradeRouteTable.addCell(makeCellleftNoBorderWithBoldFont("POL"));
                tradeRouteTable.addCell(makeCellLeftNoBorderValue(getCityStateName(bookingFcl.getPortofOrgin())));
            }
        } else {
            if (null != bookingFcl.getRampCity() && !bookingFcl.getRampCity().equalsIgnoreCase("") && !bookingFcl.getRampCity().equalsIgnoreCase("null")) {
                tradeRouteTable.addCell(makeCellleftNoBorderWithBoldFont("Ramp City"));
                tradeRouteTable.addCell(makeCellLeftNoBorderValue(getCityStateName(bookingFcl.getRampCity())));
            }
            if (!bookingFcl.getPortofOrgin().equalsIgnoreCase("") && !bookingFcl.getPortofOrgin().equalsIgnoreCase("null")) {
                tradeRouteTable.addCell(makeCellleftNoBorderWithBoldFont("POL"));
                tradeRouteTable.addCell(makeCellLeftNoBorderValue(getCityStateName(bookingFcl.getPortofOrgin())));
            }
        }

        if (bookingFcl.getPortofDischarge() != null && bookingFcl.getDestination() != null && bookingFcl.getDestination().replace("/ ", "/").trim().equalsIgnoreCase(bookingFcl.getPortofDischarge().replace("/ ", "/").trim())) {
            if (!bookingFcl.getDestination().equalsIgnoreCase("") && !bookingFcl.getDestination().equalsIgnoreCase("null")) {
                tradeRouteTable.addCell(makeCellleftNoBorderWithBoldFont("POD"));
                tradeRouteTable.addCell(makeCellLeftNoBorderValue(getCityStateName(bookingFcl.getDestination())));
            }
        } else {
            if (!bookingFcl.getDestination().equalsIgnoreCase("") && !bookingFcl.getDestination().equalsIgnoreCase("null")) {
                tradeRouteTable.addCell(makeCellleftNoBorderWithBoldFont("POD"));
                tradeRouteTable.addCell(makeCellLeftNoBorderValue(getCityStateName(bookingFcl.getDestination())));
            }
            if (!bookingFcl.getPortofDischarge().equalsIgnoreCase("") && !bookingFcl.getPortofDischarge().equalsIgnoreCase("null")) {
                tradeRouteTable.addCell(makeCellleftNoBorderWithBoldFontWithGreen("Destination"));
                tradeRouteTable.addCell(makeCellLeftNoBorderValueGreen(getCityStateName(bookingFcl.getPortofDischarge())));
            }
        }
        if (!bookingFcl.getDoorDestination().equals("") && !bookingFcl.getDoorDestination().equalsIgnoreCase("null")) {
            if (null != bookingFcl.getOnCarriage() && "on".equalsIgnoreCase(bookingFcl.getOnCarriage())) {
                tradeRouteTable.addCell(makeCellleftNoBorderWithBoldFont("Final Destination"));
            } else {
                tradeRouteTable.addCell(makeCellleftNoBorderWithBoldFont("Door Destination"));
            }
            tradeRouteTable.addCell(makeCellLeftNoBorderValue(getCityStateName(String.valueOf(bookingFcl.getDoorDestination()))));
        }
        tradeRouteTable.addCell(makeCellleftNoBorderWithBoldFont("Region Remarks"));
        tradeRouteTable.addCell(makeCellLeftNoBorderValueRedFont(searchBookingReportDTO.getRegionRemarks()));

        cell = makeCellleftNoBorderWithBoldFont("");
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.addElement(tradeRouteTable);
        mainTable.addCell(cell);

        //dateTable
        PdfPTable DateTable = makeTable(2);
        DateTable.setWidthPercentage(100);
        DateTable.setWidths(new float[]{30, 70});
        DateTable.addCell(makeCellleftNoBorderWithBoldFont("VGM Cut Off"));
        if (null != bookingFcl.getVgmCuttOff()) {
            DateTable.addCell(makeCellLeftForDateNoBorderWithColonInDateMonthYearFormatWithTimeRedFont(bookingFcl.getVgmCuttOff()));
        } else {
            DateTable.addCell(makeCellLeftNoBorderValueForFCLVGM(""));

        }
        DateTable.addCell(makeCellleftNoBorderWithBoldFont("Doc Cutoff"));
        if (CommonUtils.isNotEmpty(printFromBl) && null != fclBl) {
            DateTable.addCell(makeCellLeftForDateNoBorderWithColonInDateMonthYearFormatWithTimeRedFont(fclBl.getDocCutOff()));
        } else {
            DateTable.addCell(makeCellLeftForDateNoBorderWithColonInDateMonthYearFormatWithTimeRedFont(bookingFcl.getDocCutOff()));
        }
        DateTable.addCell(makeCellleftNoBorderWithBoldFont("Container Cutoff"));
        if (CommonUtils.isNotEmpty(printFromBl) && null != fclBl) {
            DateTable.addCell(makeCellLeftForDateNoBorderWithColonInDateMonthYearFormatWithTimeRedFont(fclBl.getPortCutOff()));
        } else {
            DateTable.addCell(makeCellLeftForDateNoBorderWithColonInDateMonthYearFormatWithTimeRedFont(bookingFcl.getPortCutOff()));
        }
        DateTable.addCell(makeCellleftNoBorderWithBoldFont("ETD"));
        if (CommonUtils.isNotEmpty(printFromBl) && null != fclBl) {
            DateTable.addCell(makeCellLeftForDateNoBorderWithColonInDateMonthYearFormat(fclBl.getSailDate()));
        } else {
            DateTable.addCell(makeCellLeftForDateNoBorderWithColonInDateMonthYearFormat(bookingFcl.getEtd()));
        }
        DateTable.addCell(makeCellleftNoBorderWithBoldFont("ETA"));
        if (CommonUtils.isNotEmpty(printFromBl) && null != fclBl) {
            DateTable.addCell(makeCellLeftForDateNoBorderWithColonInDateMonthYearFormat(fclBl.getEta()));
        } else {
            DateTable.addCell(makeCellLeftForDateNoBorderWithColonInDateMonthYearFormat(bookingFcl.getEta()));
        }
        cell = makeCellleftNoBorderWithBoldFont("");
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.addElement(DateTable);

        mainTable.addCell(cell);
        //FOR PORTS REMARKS
        PdfPTable portsRemarksTable1 = makeTable(2);
        portsRemarksTable1.setWidths(new float[]{17, 83});
        portsRemarksTable1.setWidthPercentage(100);
        cell = makeCellleftNoBorderWithBoldFont("Port Remarks/GRI:");
        portsRemarksTable1.addCell(cell);
        StringBuilder stringBuilder = new StringBuilder();
        String GriRemark = CommonUtils.isNotEmpty(getGRIRemarkks(bookingFcl)) ? getGRIRemarkks(bookingFcl) : "";
        if (bookingFcl.getDestRemarks() != null && !bookingFcl.getDestRemarks().equals("")) {
            stringBuilder.append(bookingFcl.getDestRemarks());
            if (bookingFcl.getRatesRemarks() != null && !bookingFcl.getRatesRemarks().equals("")) {
                stringBuilder.append("\n");
                stringBuilder.append(bookingFcl.getRatesRemarks());
            }
        } else {
            stringBuilder.append(bookingFcl.getRatesRemarks());
        }
        if (CommonUtils.isNotEmpty(GriRemark)) {
            stringBuilder.append("\n");
            stringBuilder.append(GriRemark);
        }
        if (CommonUtils.isNotEmpty(propRemarks)) {
            stringBuilder.append("\n");
            stringBuilder.append("\n");
            stringBuilder.append(propRemarks);
        }
        cell = makeCellLeftValueRedFont(stringBuilder.toString());
        cell.setBorderWidthRight(0.0f);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        portsRemarksTable1.addCell(cell);
        cell = makeCellLeftWithRightLeftBorder("");
        cell.setBorderWidthBottom(0.6f);
        cell.setColspan(2);
        cell.addElement(portsRemarksTable1);
        if (simpleRequest.equals("simpleRequest")) {
        } else {
            mainTable.addCell(cell);
        }

        //carrierTable
        PdfPTable carrierTable = makeTable(2);
        carrierTable.setWidths(new float[]{15, 85});
        carrierTable.setWidthPercentage(100);
        String sslName[] = bookingFcl.getSslname().split("//");
        if (bookingFcl.getCarrierPrint() != null && bookingFcl.getCarrierPrint().equals("on")) {
            carrierTable.addCell(makeCellleftNoBorderWithBoldFont("Carrier"));
            carrierTable.addCell(makeCellLeftNoBorderValue(sslName[0]));
        }
        carrierTable.addCell(makeCellleftNoBorderWithBoldFont("Vessel"));
        if (null == bookingFcl.getVesselNameCheck()) {
            if (CommonUtils.isNotEmpty(printFromBl) && null != fclBl) {
                carrierTable.addCell(makeCellLeftNoBorderValue(null != fclBl.getVessel() && null != fclBl.getVessel().getCodedesc() ? fclBl.getVessel().getCodedesc() : ""));
            } else {
                carrierTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getVessel()));
            }
        } else {
            carrierTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getManualVesselName()));
        }
        carrierTable.addCell(makeCellleftNoBorderWithBoldFont("Voyage"));
        if (CommonUtils.isNotEmpty(printFromBl) && null != fclBl && CommonUtils.isNotEmpty(fclBl.getVoyages())) {
            cell = makeCellLeftNoBorderValue(fclBl.getVoyages());
        } else {
            cell = makeCellLeftNoBorderValue(bookingFcl.getVoyageCarrier());
        }
        carrierTable.addCell(cell);
        cell = makeCellleftNoBorderWithBoldFont("");
        cell.addElement(carrierTable);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);

        mainTable.addCell(cell);
        //trucker table
        PdfPTable truckerTable = makeTable(2);
        truckerTable.setWidths(new float[]{33, 67});
        truckerTable.setWidthPercentage(100);

        truckerTable.addCell(makeCellleftNoBorderWithBoldFont("Trucker Name"));
        truckerTable.addCell(makeCellLeftNoBorderValue(bookingFcl.getName()));
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
                                if (!isFirst) {
                                    bkgParagraph1.add(new Phrase(" \n"));
                                }
                                if (tempArray[1].equalsIgnoreCase(messageResources.getMessage("container40HC"))) {
                                    container = bookingfclUnits.getNumbers() + " X " + "40" + "'" + "HC";
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
                                    container = bookingfclUnits.getNumbers() + " X " + "40" + "'" + "NOR";
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
                                    container = bookingfclUnits.getNumbers() + " X " + tempArray[1] + "'";
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
            cell = makeCellleftNoBorderWithBoldFont("Container Summary :");
            cell.setNoWrap(true);
            truckerTable.addCell(cell);
//                    truckerTable.addCell(makeCellLeftNoBorderValue(containerSummary.toString()));
            truckerTable.addCell(bkgParagraph1);
        }
        //truckerTable.addCell(makeCellleftNoBorderWithBoldFont("Trucker Address"));
        StringBuilder truckerAddress = new StringBuilder();
        truckerAddress.append(bookingFcl.getAddress() != null ? bookingFcl.getAddress() : "");
        truckerAddress.append(", ");
        truckerAddress.append(bookingFcl.getTruckerCity() != null ? bookingFcl.getTruckerCity() : "");
        truckerAddress.append(", ");
        truckerAddress.append(bookingFcl.getTruckerState() != null ? bookingFcl.getTruckerState() : "");
        truckerAddress.append(", ");
        truckerAddress.append(bookingFcl.getTruckerZip() != null ? bookingFcl.getTruckerZip() : "");
        truckerAddress.append(".");
        //truckerTable.addCell(makeCellLeftNoBorderValue(truckerAddress.toString()));
        cell = makeCellleftNoBorderWithBoldFont("");
        cell.addElement(truckerTable);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);

        mainTable.addCell(cell);
        //equipment pick up return table
        PdfPTable pickUpTable = null;
        if ("Pickup Order".equalsIgnoreCase(documentName)) {
            pickUpTable = makeTable(2);
            pickUpTable.setWidths(new float[]{50, 50});
        } else {
            pickUpTable = makeTable(3);
            pickUpTable.setWidths(new float[]{31, 38, 31});
        }
        pickUpTable.setWidthPercentage(100);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        PdfPTable pickUpTable1 = makeTable(3);
        pickUpTable1.setWidths(new float[]{29, 3, 68});
        pickUpTable1.setWidthPercentage(100);
        cell = makeCellleftNoBorderWithSmallFont("Equipment Pick up");
        cell.setColspan(3);
        pickUpTable1.addCell(cell);
        pickUpTable1.addCell(makeCellleftNoBorderWithSmallFont(""));
        pickUpTable1.addCell(makeCellleftNoBorderWithSmallFont(""));
        pickUpTable1.addCell(makeCellleftNoBorderWithSmallFont(""));
        pickUpTable1.addCell(makeCellleftNoBorderWithSmallFont("Earliest Date"));
        pickUpTable1.addCell(makeCellleftNoBorderBoldFont(":"));
        pickUpTable1.addCell(makeCellLeftForDateNoBorderWithoutColon(bookingFcl.getEarliestPickUpDate()));
        pickUpTable1.addCell(makeCellleftNoBorderWithSmallFont("Address"));
        pickUpTable1.addCell(makeCellleftNoBorderBoldFont(":"));
        pickUpTable1.addCell(makeCellLeftNoBorderValueSmallFontWithoutColon(bookingFcl.getEmptypickupaddress()));
        pickUpTable1.addCell(makeCellleftNoBorderWithSmallFont("Remarks"));
        pickUpTable1.addCell(makeCellleftNoBorderBoldFont(":"));
        pickUpTable1.addCell(makeCellLeftNoBorderValueSmallFontWithoutColon(bookingFcl.getPickUpRemarks()));
        cell = makeCellleftNoBorder1("");
        cell.addElement(pickUpTable1);
        cell.setPadding(0);
        cell.setBorderWidthRight(0.6f);
        pickUpTable.addCell(cell);

        if (!"Pickup Order".equalsIgnoreCase(documentName)) {
            PdfPTable pickUpTable2 = makeTable(3);
            pickUpTable2.setWidthPercentage(100);
            pickUpTable2.setWidths(new float[]{19, 3, 77});
            cell = makeCellleftNoBorderWithSmallFont("Spotting Information");
            cell.setColspan(3);
            pickUpTable2.addCell(cell);
            pickUpTable2.addCell(makeCellleftNoBorderWithSmallFont(""));
            pickUpTable2.addCell(makeCellleftNoBorderWithSmallFont(""));
            pickUpTable2.addCell(makeCellleftNoBorderWithSmallFont(""));
            pickUpTable2.addCell(makeCellleftNoBorderWithSmallFont("Spot Date"));
            pickUpTable2.addCell(makeCellleftNoBorderBoldFont(":"));
            if (CommonUtils.isNotEmpty(bookingFcl.getTimeCheckBox()) && bookingFcl.getTimeCheckBox().equals("on")) {
                pickUpTable2.addCell(makeCellLeftForDateNoBorderWithoutColon(bookingFcl.getPositioningDate()));
            } else {
                pickUpTable2.addCell(makeCellLeftForDateNoBorderInDateMonthYearFormatWithTime(bookingFcl.getPositioningDate()));
            }
            pickUpTable2.addCell(makeCellleftNoBorderWithSmallFont("Contact"));
            pickUpTable2.addCell(makeCellleftNoBorderBoldFont(":"));
            pickUpTable2.addCell(makeCellLeftNoBorderValueSmallFontWithoutColon(bookingFcl.getLoadcontact()));
            pickUpTable2.addCell(makeCellleftNoBorderWithSmallFont("Phone"));
            pickUpTable2.addCell(makeCellleftNoBorderBoldFont(":"));
            pickUpTable2.addCell(makeCellLeftNoBorderValueSmallFontWithoutColon(bookingFcl.getLoadphone()));
            pickUpTable2.addCell(makeCellleftNoBorderWithSmallFont("Name"));
            pickUpTable2.addCell(makeCellleftNoBorderBoldFont(":"));
            pickUpTable2.addCell(makeCellLeftNoBorderValueSmallFontWithoutColon(bookingFcl.getSpottingAccountName()));
            pickUpTable2.addCell(makeCellleftNoBorderWithSmallFont("Address"));
            pickUpTable2.addCell(makeCellleftNoBorderBoldFont(":"));
            StringBuilder spottingAddress = new StringBuilder("");
            if (CommonUtils.isNotEmpty(bookingFcl.getAddressForExpPositioning())) {
                spottingAddress.append(bookingFcl.getAddressForExpPositioning());
                if (CommonUtils.isNotEmpty(bookingFcl.getSpotAddrCity())) {
                    spottingAddress.append(", ");
                    spottingAddress.append(bookingFcl.getSpotAddrCity());
                }
                if (CommonUtils.isNotEmpty(bookingFcl.getSpotAddrState())) {
                    spottingAddress.append(", ");
                    spottingAddress.append(bookingFcl.getSpotAddrState());
                }
                if (CommonUtils.isNotEmpty(bookingFcl.getSpotAddrZip())) {
                    spottingAddress.append(", ");
                    spottingAddress.append(bookingFcl.getSpotAddrZip());
                }
            }
            pickUpTable2.addCell(makeCellLeftNoBorderValueSmallFontWithoutColon(spottingAddress.toString()));
            pickUpTable2.addCell(makeCellleftNoBorderWithSmallFont("Remarks"));
            pickUpTable2.addCell(makeCellleftNoBorderBoldFont(":"));
            pickUpTable2.addCell(makeCellLeftNoBorderValueSmallFontWithoutColon((null != bookingFcl.getLoadRemarks()) ? bookingFcl.getLoadRemarks().toUpperCase() : ""));
            cell = makeCellleftNoBorder1("");
            cell.addElement(pickUpTable2);
            cell.setPadding(0);
            cell.setBorderWidthRight(0.6f);
            pickUpTable.addCell(cell);
        }

        PdfPTable pickUpTable3 = makeTable(3);
        pickUpTable3.setWidthPercentage(100);
        pickUpTable3.setWidths(new float[]{22, 3, 75});
        cell = makeCellleftNoBorderWithSmallFont("Equipment Return");
        cell.setColspan(3);
        pickUpTable3.addCell(cell);
        pickUpTable3.addCell(makeCellleftNoBorderWithSmallFont(""));
        pickUpTable3.addCell(makeCellleftNoBorderWithSmallFont(""));
        pickUpTable3.addCell(makeCellleftNoBorderWithSmallFont(""));
        //pickUpTable3.addCell(makeCellleftNoBorderWithSmallFont("Return Date"));
        //pickUpTable3.addCell(makeCellleftNoBorderBoldFont(":"));
        //pickUpTable3.addCell(makeCellLeftForDateNoBorderWithoutColon(bookingFcl.getLoadDate()));
        pickUpTable3.addCell(makeCellleftNoBorderWithSmallFont("Address"));
        pickUpTable3.addCell(makeCellleftNoBorderBoldFont(":"));
        pickUpTable3.addCell(makeCellLeftNoBorderValueSmallFontWithoutColon(bookingFcl.getAddessForExpDelivery()));
        pickUpTable3.addCell(makeCellleftNoBorderWithSmallFont("Remarks"));
        pickUpTable3.addCell(makeCellleftNoBorderBoldFont(":"));
        pickUpTable3.addCell(makeCellLeftNoBorderValueSmallFontWithoutColon(bookingFcl.getReturnRemarks()));
        cell = makeCellleftNoBorder1("");
        cell.addElement(pickUpTable3);
        cell.setPadding(0);
        pickUpTable.addCell(cell);

        cell = makeCellleftNoBorder1("");
        cell.setColspan(2);
        cell.addElement(pickUpTable);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setPadding(0);
        mainTable.addCell(cell);
        //hazmat table-------
        PdfPTable hazmatDetails = makeTable(6);
        hazmatDetails.setWidthPercentage(100);

        if (bookingFcl.getHazmat() != null && bookingFcl.getHazmat().equalsIgnoreCase("Y")) {

            HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();
            List hazMatList1 = new ArrayList();
            hazMatList1 = hazmatMaterialDAO.findbydoctypeid1(QuotationConstants.BOOKING, bookingFcl.getBookingId());
            if (hazMatList1.size() != 0) {
                cell = makeCellLeftNoBorder("HAZMAT Details");
                cell.setColspan(6);
                hazmatDetails.addCell(cell);

                hazmatDetails.addCell(makeCellleftwithUnderLineSmallFont("UN Number"));
                hazmatDetails.addCell(makeCellleftwithUnderLineSmallFont("Proper Shipping Name"));
                hazmatDetails.addCell(makeCellleftwithUnderLineSmallFont("Technical Name"));
                hazmatDetails.addCell(makeCellleftwithUnderLineSmallFont("IMO Class Code(Primary)"));
                hazmatDetails.addCell(makeCellleftwithUnderLineSmallFont("PKG Group"));
                hazmatDetails.addCell(makeCellleftwithUnderLineSmallFont("Gross Weight"));

                for (Iterator iter = hazMatList1.iterator(); iter.hasNext();) {
                    HazmatMaterial hazmatMaterial = (HazmatMaterial) iter.next();
                    String pckGroup = (null == hazmatMaterial.getPackingGroupCode() || hazmatMaterial.getPackingGroupCode().equalsIgnoreCase("0")) ? " " : hazmatMaterial.getPackingGroupCode();
                    String grossWeight = (null != hazmatMaterial.getGrossWeight()) ? numformat.format(hazmatMaterial.getGrossWeight()) + "  " : "0.00";
                    grossWeight += (null != hazmatMaterial.getGrossWeightUMO()) ? hazmatMaterial.getGrossWeightUMO() : " ";
                    hazmatDetails.addCell(makeCellLeftNoBorderBold(hazmatMaterial.getUnNumber()));
                    hazmatDetails.addCell(makeCellLeftNoBorderBold(hazmatMaterial.getPropShipingNumber()));
                    hazmatDetails.addCell(makeCellLeftNoBorderBold(hazmatMaterial.getTechnicalName()));
                    hazmatDetails.addCell(makeCellLeftNoBorderBold(hazmatMaterial.getImoClssCode()));
                    hazmatDetails.addCell(makeCellLeftNoBorderBold(pckGroup));
                    hazmatDetails.addCell(makeCellLeftNoBorderBold(grossWeight));
                }

                cell = makeCellLeftNoBorder("");
                cell.setPaddingTop(10f);
                cell.setColspan(6);
                hazmatDetails.addCell(cell);

                cell = new PdfPCell();
                cell.setBorderWidthBottom(0.6f);
                cell.setBorderWidthTop(0.0f);
                cell.setBorderWidthLeft(0.0f);
                cell.setBorderWidthRight(0.0f);
                cell.setPaddingTop(10f);
                cell.setColspan(6);
                hazmatDetails.addCell(cell);
            }
        }
        cell = makeCellleftNoBorder1("");
        cell.setColspan(2);
        cell.addElement(hazmatDetails);
        if (!"Pickup Order".equalsIgnoreCase(documentName)) {
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthLeft(0.6f);
        }
        //cell.setBorderWidthBottom(0.6f);
        mainTable.addCell(cell);
        //mainTable.setKeepTogether(true);

        //rates table
        //charges heading
        if (!simpleRequest.equals("simpleRequest") && !"Pickup Order".equalsIgnoreCase(documentName)) {
            PdfPTable chargesMainTable = makeTable(1);
            chargesMainTable.setWidthPercentage(101.05f);
            PdfPTable ChargeHeading = makeTable(6);
            ChargeHeading.setWidthPercentage(100);
            ChargeHeading.setWidths(new float[]{20, 23, 20, 5, 16, 16});
            cell = makeCellleftNoBorderWithBoldFont("RateConfirmation");
            cell.setColspan(6);
            ChargeHeading.addCell(cell);

            ChargeHeading.addCell(makeCellleftwithUnderLine("Container"));
            ChargeHeading.addCell(makeCellleftwithUnderLine(""));
            ChargeHeading.addCell(makeCellleftwithUnderLine("Charge"));
            cell = makeCellleftwithUnderLine("Currency");
            cell.setNoWrap(true);
            ChargeHeading.addCell(cell);
            ChargeHeading.addCell(makeCellRightwithUnderLine("Rate Per Unit"));
            ChargeHeading.addCell(makeCellRightwithUnderLine("Amount"));
            chargesMainTable.addCell(ChargeHeading);
            //rates confirmation
            PdfPTable chargesTable = getRatesTable(searchBookingReportDTO, messageResources, bookingFcl);
            chargesMainTable.addCell(chargesTable);
            cell = makeCellleftNoBorder1("");
            cell.setColspan(2);
            cell.addElement(chargesMainTable);
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthBottom(0.6f);
            mainTable.addCell(cell);
        }
        // comodity

        PdfPTable comodity = makeTable(2);
        comodity.setWidthPercentage(100);
        comodity.setWidths(new float[]{15, 85});

        comodity.addCell(makeCellleftNoBorderWithBoldFont("Commodity"));
        comodity.addCell(makeCellLeftNoBorderValue(bookingFcl.getComdesc()));
        // Remarks
        PdfPTable remarks = makeTable(1);
        remarks.setWidthPercentage(100);

        remarks.addCell(makeCellleftNoBorderWithBoldFont("Remarks"));
        remarks.addCell(makeCellleftNoBorderWithBoldFont(bookingFcl.getRemarks()));

        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        List disclaimerList = genericCodeDAO.getAllCommentCodesForBookingReports();
        PdfPTable disclaimerTable = makeTable(1);
        disclaimerTable.setWidthPercentage(100);
        User userDetails = getPleaseContactDetails(bookingFcl.getBookedBy() != null ? bookingFcl.getBookedBy() : "");
        if (!"Pickup Order".equalsIgnoreCase(documentName)) {
            PdfPTable contactUsTable = makeTable(1);
            contactUsTable.setWidthPercentage(100);
            StringBuilder buffer = new StringBuilder("");
            buffer.append("Please Send Inquiries and Docs to ");
            QuotationBC quotationBC = new QuotationBC();
//	      Type of Load="F",IssuingTerminal number ,Pod. Passing to RETDD table and fetching Email id.
            String emailFromRetddTable = quotationBC.getEmailToDisplayINReport(QuotationConstants.TYPEOFLOAD, StringFormatter.getIssuingTerminal(bookingFcl.getIssuingTerminal()),
                    StringFormatter.orgDestStringFormatter(bookingFcl.getPortofDischarge()));
            if (emailFromRetddTable != null) {
                buffer.append(emailFromRetddTable);
            } else {
                buffer.append(userDetails.getEmail() != null ? userDetails.getEmail() : "");
                buffer.append(", ");
                buffer.append(userDetails.getFirstName() != null ? userDetails.getFirstName() : "");
                buffer.append(" ");
                buffer.append(userDetails.getLastName() != null ? userDetails.getLastName() : "");
                buffer.append(", at ");
                buffer.append("Phone-");
                buffer.append(userDetails.getTelephone() != null ? userDetails.getTelephone() : "");
                if (userDetails.getTelephone() != null && !userDetails.getTelephone().equalsIgnoreCase("")) {
                    buffer.append(", ");
                }
                buffer.append("Fax-");
                buffer.append(userDetails.getFax() != null ? userDetails.getFax() : "");
                if (userDetails.getFax() != null && !userDetails.getFax().equalsIgnoreCase("")) {
                    buffer.append(", ");
                }
                buffer.append(userDetails.getAddress1() != null ? userDetails.getAddress1() : "");
                if (userDetails.getAddress1() != null && !userDetails.getAddress1().equalsIgnoreCase("")) {
                    buffer.append(", ");
                }
                buffer.append(userDetails.getTerminal() != null
                        ? (userDetails.getTerminal().getTerminalLocation() != null ? userDetails.getTerminal().getTerminalLocation()
                        : "") : "");
                if (userDetails.getTerminal() != null && userDetails.getTerminal().getTerminalLocation() != null
                        && !userDetails.getTerminal().getTerminalLocation().equalsIgnoreCase("")) {
                    buffer.append(", ");
                }
                buffer.append(userDetails.getCity() != null ? userDetails.getCity() : "");
                if (userDetails.getCity() != null && !userDetails.getCity().equalsIgnoreCase("")) {
                    buffer.append(", ");
                }
                buffer.append(userDetails.getZipCode() != null ? userDetails.getZipCode() : "");
                if (userDetails.getZipCode() != null && !userDetails.getZipCode().equalsIgnoreCase("")) {
                    buffer.append(", ");
                }
            }

            buffer.append(".");
            contactUsTable.addCell(makeCellLeftNoBorderBold(buffer.toString()));
            cell = makeCellleftNoBorder("");
            cell.setBorderWidthBottom(0.6f);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthRight(0.6f);
            cell.setBorderWidthBottom(0.6f);
            cell.addElement(contactUsTable);
            disclaimerTable.addCell(cell);
        }

        PdfPTable referenceTable = makeTable(3);
        referenceTable.setWidthPercentage(30);
        referenceTable.setWidths(new float[]{6.7f, 3, 20.3f});
        referenceTable.addCell(makeCellLeftNoBorder("Please reference our File Number "));
        referenceTable.addCell(makeCellCenterForRefFileNo(messageResources.getMessage("fileNumberPrefix").toString() + String.valueOf(bookingFcl.getFileNo())));
        referenceTable.addCell(makeCellLeftNoBorder(" on all Documents"));
        disclaimerTable.addCell(makeCellleftNoBorderWithBoldFont(""));
        disclaimerTable.addCell(referenceTable);
//	        disclaimerTable.addCell(makeCellLeftNoBorder("Please reference our File Number "+messageResources.getMessage("fileNumberPrefix").toString()+String.valueOf(bookingFcl.getFileNo()+" on all Documents")));
        disclaimerTable.addCell(makeCellleftNoBorderWithBoldFont(""));
        if (!"Pickup Order".equalsIgnoreCase(documentName)) {
            disclaimerTable.addCell(makeCellLeftNoBorder("Note:"));
            BookingDwrBC bookingDwrBC = new BookingDwrBC();
            String destin = "";
            String checkRegion = "false";
            if (bookingFcl.getPortofDischarge() != null && !"".equals(bookingFcl.getPortofDischarge())) {
                destin = bookingFcl.getPortofDischarge();
                checkRegion = bookingDwrBC.checkForTheRegion(destin);
            }
            if (checkRegion != null && "true".equals(checkRegion)) {
                if (bookingFcl.getDeductFFcomm() != null && "Y".equals(bookingFcl.getDeductFFcomm())) {
                    for (int i = 0; i < disclaimerList.size(); i++) {
                        if (i == 0) {
                            disclaimerTable.addCell(makeCellLeftWithNoBorderColored(String.valueOf(disclaimerList.get(i))));
                        } else {
                            disclaimerTable.addCell(makeCellLeftNoBorder("-" + String.valueOf(disclaimerList.get(i))));
                        }
                    }
                } else {
                    for (int i = 1; i < disclaimerList.size(); i++) {
                        disclaimerTable.addCell(makeCellLeftNoBorder("-" + String.valueOf(disclaimerList.get(i))));
                    }
                }
            } else {
                for (int i = 0; i < disclaimerList.size(); i++) {
                    if (i == 0) {
                        disclaimerTable.addCell(makeCellLeftWithNoBorderColored(String.valueOf(disclaimerList.get(i))));
                    } else {
                        disclaimerTable.addCell(makeCellLeftNoBorder("-" + String.valueOf(disclaimerList.get(i))));
                    }
                }
            }
        }

        document.add(mainTable);
        document.add(disclaimerTable);
    }

    public void destroy() {
        document.close();
    }

    private User getPleaseContactDetails(String loginName) throws Exception {
        UserDAO userDAO = new UserDAO();
        return userDAO.findUserName(loginName);
    }

    public PdfPTable getRatesTable(SearchBookingReportDTO searchBookingReportDTO,
            MessageResources messageResources, BookingFcl bookingFcl) throws DocumentException {
        PdfPCell cell;

        //Bundle In to OFR calculation
        List<BookingfclUnits> otherUnitsList = searchBookingReportDTO.getOtherChargesList();
        Double ofrTotal = 0.00;
        Double ofrContainerTotal = 0.0;
        Double amt = 0.00;
        String specialEqu = "";
        String unitType = "";
        int count = 0;
        boolean spotRate = bookingFcl.getSpotRate().equals("Y");
        if (CommonUtils.isNotEmpty(bookingFcl.getSpecialEqpmtSelectBox()) && !bookingFcl.getSpecialEqpmtSelectBox().equals("0") && bookingFcl.getSpecialequipment().equals("Y")) {
            specialEqu = bookingFcl.getSpecialEqpmtSelectBox();
        }

        for (BookingfclUnits otherUnits : otherUnitsList) {
            if (null != otherUnits.getPrint() && "on".equalsIgnoreCase(otherUnits.getPrint())) {
                if (null != otherUnits.getNewFlag() && ("new".equals(otherUnits.getNewFlag()))) {
                    amt = (null != otherUnits.getMarkUp() ? Double.parseDouble(String.valueOf(otherUnits.getMarkUp()).replaceAll(",", "")) : 0.00);
                } else {
                    amt = (null != otherUnits.getAmount() ? Double.parseDouble(String.valueOf(otherUnits.getAmount()).replaceAll(",", "")) : 0.00)
                            + (null != otherUnits.getMarkUp() ? Double.parseDouble(String.valueOf(otherUnits.getMarkUp()).replaceAll(",", "")) : 0.00);
                }
                ofrTotal = ofrTotal + amt;
                ofrContainerTotal = ofrContainerTotal + amt;
            }
        }
        List<BookingfclUnits> bookingUnitsList = searchBookingReportDTO.getChargesList();
        String tempUnit = "";
        String currentUnit = "";
        int k = 0;
        int l = 0;
        Map<String, Double> bundleMap = new HashMap<String, Double>();
        //double[] OFR = new double[bookingUnitsList.size() + 10];
        //double[] OFRPERCONTAINER = new double[bookingUnitsList.size() + 10];
        boolean found = false;

        int noOfContainer;
        // initially assign first unit type, to compare the unit type
        if (!bookingUnitsList.isEmpty()) {
            BookingfclUnits firstUnit = bookingUnitsList.get(0);
            if (null == firstUnit.getSpecialEquipmentUnit()) {
                firstUnit.setSpecialEquipmentUnit("");
            }
            if (null == firstUnit.getStandardCharge()) {
                firstUnit.setStandardCharge("");
            }
            tempUnit = firstUnit.getUnitType().getCodedesc() + "-" + firstUnit.getSpecialEquipmentUnit() + "-" + firstUnit.getStandardCharge();
        }
        for (BookingfclUnits bookingUnits : bookingUnitsList) {
            if (null == bookingUnits.getSpecialEquipmentUnit()) {
                bookingUnits.setSpecialEquipmentUnit("");
            }
            if (null == bookingUnits.getStandardCharge()) {
                bookingUnits.setStandardCharge("");
            }
            currentUnit = bookingUnits.getUnitType().getCodedesc() + "-" + bookingUnits.getSpecialEquipmentUnit() + "-" + bookingUnits.getStandardCharge();
            if (tempUnit.equalsIgnoreCase(currentUnit)) {
                if (null != bookingUnits.getPrint() && "on".equalsIgnoreCase(bookingUnits.getPrint()) && null != bookingUnits.getApproveBl()
                        && "yes".equalsIgnoreCase(bookingUnits.getApproveBl()) && !bookingUnits.getChargeCodeDesc().equalsIgnoreCase("OCNFRT")) {
                    found = true;
                    noOfContainer = bookingUnits.getNumbers() != null ? Integer.parseInt(bookingUnits.getNumbers()) : 1;
                    if (null != bookingUnits.getNewFlag() && ("new".equals(bookingUnits.getNewFlag()))) {
                        ofrTotal += ((bookingUnits.getMarkUp() + bookingUnits.getAdjustment()) * noOfContainer);
                        ofrContainerTotal += (bookingUnits.getMarkUp() + bookingUnits.getAdjustment());
                    } else {
                        if (spotRate) {
                            ofrTotal += ((bookingUnits.getAmount() + bookingUnits.getSpotRateMarkUp() + bookingUnits.getAdjustment()) * noOfContainer);
                            ofrContainerTotal += (bookingUnits.getAmount() + bookingUnits.getSpotRateMarkUp() + bookingUnits.getAdjustment());
                        } else {
                            ofrTotal += ((bookingUnits.getAmount() + bookingUnits.getMarkUp() + bookingUnits.getAdjustment()) * noOfContainer);
                            ofrContainerTotal += (bookingUnits.getAmount() + bookingUnits.getMarkUp() + bookingUnits.getAdjustment());
                        }
                    }
                }
            } else {
                if (found == true) {
                    bundleMap.put(tempUnit, ofrContainerTotal);
                    ofrTotal = 0.0;
                    ofrContainerTotal = 0.0;
                    found = false;
                }
                tempUnit = bookingUnits.getUnitType().getCodedesc() + "-" + bookingUnits.getSpecialEquipmentUnit() + "-" + bookingUnits.getStandardCharge();
                if (null != bookingUnits.getPrint() && "on".equalsIgnoreCase(bookingUnits.getPrint()) && null != bookingUnits.getApproveBl()
                        && "yes".equalsIgnoreCase(bookingUnits.getApproveBl()) && !bookingUnits.getChargeCodeDesc().equalsIgnoreCase("OCNFRT")) {
                    noOfContainer = bookingUnits.getNumbers() != null ? Integer.parseInt(bookingUnits.getNumbers()) : 1;
                    if (bookingUnits.getNewFlag() != null && ("new".equals(bookingUnits.getNewFlag()))) {
                        ofrTotal = ofrTotal + ((bookingUnits.getMarkUp() + bookingUnits.getAdjustment()) * noOfContainer);
                        ofrContainerTotal = ofrContainerTotal + (bookingUnits.getMarkUp() + bookingUnits.getAdjustment());
                    } else {
                        if (spotRate) {
                            ofrTotal = ofrTotal + ((bookingUnits.getAmount() + bookingUnits.getSpotRateMarkUp() + bookingUnits.getAdjustment()) * noOfContainer);
                            ofrContainerTotal = ofrContainerTotal + (bookingUnits.getAmount() + bookingUnits.getSpotRateMarkUp() + bookingUnits.getAdjustment());
                        } else {
                            ofrTotal = ofrTotal + ((bookingUnits.getAmount() + bookingUnits.getMarkUp() + bookingUnits.getAdjustment()) * noOfContainer);
                            ofrContainerTotal = ofrContainerTotal + (bookingUnits.getAmount() + bookingUnits.getMarkUp() + bookingUnits.getAdjustment());
                        }
                    }
                }
            }
        }
        bundleMap.put(tempUnit, ofrContainerTotal);
        NumberFormat numformat = new DecimalFormat("##,###,##0.00");
        PdfPTable chargesTable = makeTable(6);
        chargesTable.setWidthPercentage(100);
        chargesTable.setWidths(new float[]{20, 23, 20, 5, 16, 16});

        List chargesList = searchBookingReportDTO.getChargesList();
        BookingfclUnits book = new BookingfclUnits();
        String temp = "";
        double subTotal = 0.00;
        double grandTotal = 0.00;
        int u = 0;
        int y = 0;
        int j = 0;
        int number;
        String tempSize = "";
        String tempSize1 = "";
        boolean printGage = false;
        boolean spclEquipt = true;
        boolean multiContainer;

        boolean hasGaugeComment = false;
        String comment = "";
        Paragraph bkgParagraph1 = new Paragraph();
        String outOfGage = "";
        Double bundleAmt;
        if (chargesList != null && !chargesList.isEmpty()) {
            if (chargesList.get(0) != null) {
                book = (BookingfclUnits) chargesList.get(0);
                if (null == book.getSpecialEquipmentUnit()) {
                    book.setSpecialEquipmentUnit("");
                }
                if (null == book.getStandardCharge()) {
                    book.setStandardCharge("");
                }
                temp = book.getUnitType().getCodedesc() + "-" + book.getSpecialEquipmentUnit() + "-" + book.getStandardCharge();
                if (book.getApproveBl() != null && book.getApproveBl().equalsIgnoreCase("Yes")) {
                    if (book.getPrint() != null && !book.getPrint().equalsIgnoreCase("on")) {
                        String newTemp = book.getUnitType().getCodedesc();
                        String tempArray[] = newTemp.split("=");
                        String container = "";
                        if (tempArray[1].equalsIgnoreCase(messageResources.getMessage("container40HC"))) {
                            container = book.getNumbers() + " X " + "40" + "'" + "HC";
                        } else if (tempArray[1].equalsIgnoreCase(messageResources.getMessage("container40NOR"))) {
                            container = book.getNumbers() + " X " + "40" + "'" + "NOR";
                        } else {
                            container = book.getNumbers() + " X " + tempArray[1] + "'";
                        }
                        bkgParagraph1.add(new Chunk(container, blackFontBold));
                        cell = makeCell(bkgParagraph1, Element.ALIGN_LEFT);
                        cell.setLeading(0.7f, 0.7f);
                        chargesTable.addCell(cell);
                        bkgParagraph1 = new Paragraph();
                        if ("Y".equalsIgnoreCase(book.getOutOfGauge())) {
                            if (CommonUtils.isNotEmpty(book.getOutOfGaugeComment())) {
                                hasGaugeComment = true;
                                comment = book.getOutOfGaugeComment();
                            } else {
                                comment = "";
                            }
                            printGage = true;
                        }
                        if (CommonUtils.isNotEmpty(book.getSpecialEquipmentUnit())) {
                            bkgParagraph1.add(new Chunk(book.getSpecialEquipment(), GreenFont8));

                        }
                        j++;
                        tempSize = tempArray[1];
                        //chargesTable.addCell(makeCellleftNoBorderBoldFont(container));
                        cell = makeCell(bkgParagraph1, Element.ALIGN_LEFT);
                        cell.setLeading(0.7f, 0.7f);
                        cell.setNoWrap(true);
                        cell.setBorderWidthLeft(0.0F);
                        chargesTable.addCell(cell);
                        chargesTable.addCell(makeCellleftNoBorderBoldFont(book.getChgCode()));
                        chargesTable.addCell(makeCellleftNoBorderBoldFont(book.getCurrency()));
                        number = book.getNumbers() != null ? Integer.parseInt(book.getNumbers()) : 1;

                        if (book.getChargeCodeDesc().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))) {
                            bundleAmt = null != bundleMap.get(temp) ? bundleMap.get(temp) : 0.00;
                            if ("new".equals(book.getNewFlag())) {
                                chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                subTotal = subTotal + (((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                            } else  {
                                if (spotRate) {
                             //       chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + book.getSpotRateMarkUp()) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00)) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format(((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                    subTotal = subTotal + ((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                                } else {
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00)) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format(((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                    subTotal = subTotal + ((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                                }
                            }

                            u++;
                            y++;
                        } else if (book.getChargeCodeDesc().equalsIgnoreCase(messageResources.getMessage("oceanfreightImpcharge"))) {
                            bundleAmt = null != bundleMap.get(temp) ? bundleMap.get(temp) : 0.00;
                            if ("new".equals(book.getNewFlag())) {
                                chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                subTotal = subTotal + (((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                            } else {
                                if (spotRate) {
                                   // chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + book.getSpotRateMarkUp()) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                     chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00)) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format(((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                    subTotal = subTotal + ((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                                } else {
                                  //  chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + book.getMarkUp()) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00)) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format(((book.getAmount() +(book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                    subTotal = subTotal + ((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                                }
                            }

                            u++;
                            y++;
                        } else if (book.getSellRate() != null) {
                            if (book.getNewFlag() != null && ("new".equals(book.getNewFlag()))) {
                                chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format(((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number)));
                                subTotal = subTotal + (((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number);
                            } else {
                                if (spotRate) {
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() +  (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00)) + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number)));
                                    subTotal = subTotal + ((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number);
                                } else {
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00)) + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number)));
                                    subTotal = subTotal + ((book.getAmount() +(book.getMarkUp() != null ? book.getMarkUp(): 0.00)+ book.getAdjustment()) * number);
                                }
                            }

                        }
                    }
                    count++;
                }

            }
        }

        // boolean printGage = true;
        boolean printGrandTotal = false;
        if (chargesList != null && chargesList.size() >= 1) {
            for (int i = 1; i < chargesList.size(); i++) {
                book = (BookingfclUnits) chargesList.get(i);
                if (null == book.getSpecialEquipmentUnit()) {
                    book.setSpecialEquipmentUnit("");
                }
                if (null == book.getStandardCharge()) {
                    book.setStandardCharge("");
                }
                unitType = book.getUnitType().getCodedesc();
                currentUnit = book.getUnitType().getCodedesc() + "-" + book.getSpecialEquipmentUnit() + "-" + book.getStandardCharge();
                if (temp.equalsIgnoreCase(currentUnit)) {
                    if (book.getApproveBl() != null && book.getApproveBl().equalsIgnoreCase("Yes")) {
                        if (book.getPrint() != null && !book.getPrint().equalsIgnoreCase("on")) {
                            String newTemp = book.getUnitType().getCodedesc();
                            String tempArray[] = newTemp.split("=");
                            String container = "";
                            j++;
                            if (tempArray[1].equalsIgnoreCase(messageResources.getMessage("container40HC"))) {
                                container = book.getNumbers() + " X " + "40" + "'" + "HC";
                            } else if (tempArray[1].equalsIgnoreCase(messageResources.getMessage("container40NOR"))) {
                                container = book.getNumbers() + " X " + "40" + "'" + "NOR";
                            } else {
                                container = book.getNumbers() + " X " + tempArray[1] + "'";
                            }
                            tempSize = tempArray[1];
                            if (printGage) {
                                bkgParagraph1 = new Paragraph();
                                bkgParagraph1.add(new Chunk(OUT_OF_GAUGE, GreenFont8));
                                printGage = false;
                                cell = makeCell(bkgParagraph1, Element.ALIGN_LEFT);
                                cell.setLeading(0.7f, 0.7f);
                                cell.setBorderWidthLeft(0.0F);
                                chargesTable.addCell(cell);
                            } else if (hasGaugeComment) {
                                hasGaugeComment = false;
                                chargesTable.addCell(makeCellLeftNoBorder(comment));
                            } else {
                                chargesTable.addCell(makeCellleftNoBorderBoldFont(""));
                            }
                            chargesTable.addCell(makeCellleftNoBorderBoldFont(""));
                            chargesTable.addCell(makeCellleftNoBorderBoldFont(book.getChgCode()));
                            chargesTable.addCell(makeCellleftNoBorderBoldFont(book.getCurrency()));
                            number = book.getNumbers() != null ? Integer.parseInt(book.getNumbers()) : 1;
                            if (book.getChargeCodeDesc().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge")) && (!"new".equalsIgnoreCase(book.getNewFlag()))) {
                                bundleAmt = null != bundleMap.get(currentUnit) ? bundleMap.get(currentUnit) : 0.00;
                                if (spotRate) {
                                    chargesTable.addCell(makeCellRightNoBorder(numformat.format((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00)))));
                                } else {
                                    chargesTable.addCell(makeCellRightNoBorder(numformat.format((book.getAmount() +(book.getMarkUp() != null ? book.getMarkUp(): 0.00) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00)))));
                                }
                                y++;
                                if (spotRate) {
                                    chargesTable.addCell(makeCellRightNoBorder(numformat.format(((book.getAmount() +(book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                    chargesTable.addCell(makeCellRightNoBorder(numformat.format(((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                } else {
                                    subTotal = subTotal + ((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                                    chargesTable.addCell(makeCellRightNoBorder(numformat.format(((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                }

                                u++;
                            } else if (book.getChargeCodeDesc().equalsIgnoreCase(messageResources.getMessage("oceanfreightImpcharge")) && (!"new".equalsIgnoreCase(book.getNewFlag()))) {
                                bundleAmt = null != bundleMap.get(currentUnit) ? bundleMap.get(currentUnit) : 0.00;
                                if (spotRate) {
                                    chargesTable.addCell(makeCellRightNoBorder(numformat.format((book.getAmount() +(book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00)))));
                                } else {
                                    chargesTable.addCell(makeCellRightNoBorder(numformat.format((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00)))));
                                }
                                y++;
                                if (spotRate) {
                                    chargesTable.addCell(makeCellRightNoBorder(numformat.format(((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                    chargesTable.addCell(makeCellRightNoBorder(numformat.format(((book.getAmount() +(book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                } else {
                                    subTotal = subTotal + ((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                                    chargesTable.addCell(makeCellRightNoBorder(numformat.format(((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                }

                                u++;
                            } else if (book.getSellRate() != null) {
                                if (book.getNewFlag() != null && ("new".equals(book.getNewFlag()))) {
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format(((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + (book.getAdjustment() != null ? book.getAdjustment() : 0.00)))));
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format(((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number)));
                                    subTotal = subTotal + (((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number);
                                } else {
                                    if (spotRate) {
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() +(book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + (book.getAdjustment() != null ? book.getAdjustment() : 0.00)))));
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number)));
                                        subTotal = subTotal + ((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number);
                                    } else {
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + (book.getAdjustment() != null ? book.getAdjustment() : 0.00)))));
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number)));
                                        subTotal = subTotal + ((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number);
                                    }
                                }

                            }
                        }
                    }
                } else {
                    if (book.getApproveBl() != null && book.getApproveBl().equalsIgnoreCase("Yes")) {
                        if (printGage) {
                            bkgParagraph1 = new Paragraph();
                            bkgParagraph1.add(new Chunk(OUT_OF_GAUGE, GreenFont8));
                            printGage = false;
                            cell = makeCell(bkgParagraph1, Element.ALIGN_LEFT);
                            cell.setLeading(0.7f, 0.7f);
                            cell.setBorderWidthLeft(0.0F);
                            chargesTable.addCell(cell);
                            chargesTable.addCell(makeCellLeftNoBorder(""));
                            chargesTable.addCell(makeCellLeftNoBorder(""));
                            chargesTable.addCell(makeCellLeftNoBorder(""));
                            chargesTable.addCell(makeCellLeftNoBorder(""));
                            chargesTable.addCell(makeCellLeftNoBorder(""));
                        }
                        if (hasGaugeComment) {
                            hasGaugeComment = false;
                            chargesTable.addCell(makeCellLeftNoBorder(comment));
                            chargesTable.addCell(makeCellLeftNoBorder(""));
                            chargesTable.addCell(makeCellLeftNoBorder(""));
                            chargesTable.addCell(makeCellLeftNoBorder(""));
                            chargesTable.addCell(makeCellLeftNoBorder(""));
                            chargesTable.addCell(makeCellLeftNoBorder(""));
                        }
                        if (!chargesList.isEmpty()) {
                            grandTotal = grandTotal + subTotal;
                            if (subTotal != 0d) {
                                if (tempSize.equalsIgnoreCase(messageResources.getMessage("container40HC"))) {
                                    tempSize = "40" + "'" + "HC";
                                } else if (tempSize.equalsIgnoreCase(messageResources.getMessage("container40NOR"))) {
                                    tempSize = "40" + "'" + "NOR";
                                } else {
                                    tempSize = tempSize + "'";
                                }
                                if (j > 0) {
                                    cell = makeCellRightWithBoldFont("Total for size " + tempSize + "......................"
                                            + "...................................." + numformat.format(subTotal) + "(USD)");
                                    cell.setColspan(6);
                                    chargesTable.addCell(cell);
                                }
                            }
                            j = 0;
                            subTotal = 0.0;
                        }

                        if (book.getPrint() != null && !book.getPrint().equalsIgnoreCase("on")) {
                            spclEquipt = true;
                            cell = makeCellRightWithBoldFont("");
                            cell.setBorderWidthBottom(0.1f);
                            cell.setBorderWidthLeft(0f);
                            cell.setBorderWidthRight(0f);
                            cell.setColspan(7);
                            cell.setBorderColorBottom(Color.LIGHT_GRAY);
                            chargesTable.addCell(cell);
                            String newTemp = book.getUnitType().getCodedesc();
                            String tempArray[] = newTemp.split("=");
                            String container = "";
                            j++;
                            if (tempArray[1].equalsIgnoreCase(messageResources.getMessage("container40HC"))) {
                                container = book.getNumbers() + " X " + "40" + "'" + "HC";
                            } else if (tempArray[1].equalsIgnoreCase(messageResources.getMessage("container40NOR"))) {
                                container = book.getNumbers() + " X " + "40" + "'" + "NOR";
                            } else {
                                container = book.getNumbers() + " X " + tempArray[1] + "'";
                            }
                            Paragraph bkgParagraph2 = new Paragraph();
                            bkgParagraph2.add(new Chunk(container, blackFontBold));
                            cell = makeCell(bkgParagraph2, Element.ALIGN_LEFT);
                            cell.setLeading(0.7f, 0.7f);
                            chargesTable.addCell(cell);

                            bkgParagraph1 = new Paragraph();
                            if ("Y".equalsIgnoreCase(book.getOutOfGauge())) {
                                if (CommonUtils.isNotEmpty(book.getOutOfGaugeComment())) {
                                    hasGaugeComment = true;
                                    comment = book.getOutOfGaugeComment();
                                }
                                printGage = true;
                            }
                            if (CommonUtils.isNotEmpty(book.getSpecialEquipmentUnit())) {
                                bkgParagraph1.add(new Chunk(book.getSpecialEquipment(), GreenFont8));
                                spclEquipt = false;
                            }
                            tempSize = tempArray[1];
                            //chargesTable.addCell(makeCellleftNoBorderBoldFont(container));
                            cell = makeCell(bkgParagraph1, Element.ALIGN_LEFT);
                            cell.setLeading(0.7f, 0.7f);
                            cell.setNoWrap(true);
                            cell.setBorderWidthLeft(0.0F);
                            chargesTable.addCell(cell);
                            tempSize = tempArray[1];
                            tempSize1 = tempArray[1];
                            //printGrandTotal=true;
                            //chargesTable.addCell(makeCellleftNoBorderBoldFont(container));

                            count++;
                            chargesTable.addCell(makeCellleftNoBorderBoldFont(book.getChgCode()));
                            chargesTable.addCell(makeCellleftNoBorderBoldFont(book.getCurrency()));
                            number = book.getNumbers() != null ? Integer.parseInt(book.getNumbers()) : 1;
                            bundleAmt = null != bundleMap.get(currentUnit) ? bundleMap.get(currentUnit) : 0.00;
                            if (book.getChargeCodeDesc().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))) {
                                if ("new".equals(book.getNewFlag())) {
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                    subTotal = subTotal + (((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                                } else {
                                    if (spotRate) {
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00)) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format(((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                        subTotal = subTotal + ((book.getAmount() +(book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                                    } else {
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00)) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format(((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                        subTotal = subTotal + ((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                                    }
                                }
                                y++;
                                u++;
                            } else if (book.getChargeCodeDesc().equalsIgnoreCase(messageResources.getMessage("oceanfreightImpcharge"))) {
                                if ("new".equals(book.getNewFlag())) {
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                    subTotal = subTotal + (((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                                } else {
                                    if (spotRate) {
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00)) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format(((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                        subTotal = subTotal + ((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                                    } else {
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00)) + bundleAmt + (book.getAdjustment() != null ? book.getAdjustment() : 0.00))));
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format(((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number))));
                                        subTotal = subTotal + ((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number) + (bundleAmt * number);
                                    }
                                }
                                y++;
                                u++;
                            } else if (book.getSellRate() != null) {
                                if (book.getNewFlag() != null && ("new".equals(book.getNewFlag()))) {
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format(((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + (book.getAdjustment() != null ? book.getAdjustment() : 0.00)))));
                                    chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format(((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number)));
                                    subTotal = subTotal + (((book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number);
                                } else {
                                    if (spotRate) {
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + (book.getAdjustment() != null ? book.getAdjustment() : 0.00)))));
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number)));
                                        subTotal = subTotal + ((book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00) + book.getAdjustment()) * number);
                                    } else {
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + (book.getAdjustment() != null ? book.getAdjustment() : 0.00)))));
                                        chargesTable.addCell(makeCellRightNoBorderBoldfont(numformat.format((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number)));
                                        subTotal = subTotal + ((book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00) + book.getAdjustment()) * number);
                                    }
                                }

                            }

                        }
                    }
                    temp = book.getUnitType().getCodedesc() + "-" + book.getSpecialEquipmentUnit() + "-" + book.getStandardCharge();

                }
            }
            if (printGage) {
                bkgParagraph1 = new Paragraph();
                bkgParagraph1.add(new Chunk(OUT_OF_GAUGE, GreenFont8));
                printGage = false;
                cell = makeCell(bkgParagraph1, Element.ALIGN_LEFT);
                cell.setLeading(0.7f, 0.7f);
                cell.setBorderWidthLeft(0.0F);
                chargesTable.addCell(cell);
                chargesTable.addCell(makeCellLeftNoBorder(""));
                chargesTable.addCell(makeCellLeftNoBorder(""));
                chargesTable.addCell(makeCellLeftNoBorder(""));
                chargesTable.addCell(makeCellLeftNoBorder(""));
                chargesTable.addCell(makeCellLeftNoBorder(""));
            }
            if (hasGaugeComment) {
                hasGaugeComment = false;
                chargesTable.addCell(makeCellLeftNoBorder(comment));
                chargesTable.addCell(makeCellLeftNoBorder(""));
                chargesTable.addCell(makeCellLeftNoBorder(""));
                chargesTable.addCell(makeCellLeftNoBorder(""));
                chargesTable.addCell(makeCellLeftNoBorder(""));
                chargesTable.addCell(makeCellLeftNoBorder(""));
            }
        }

        if (chargesList != null && !chargesList.isEmpty()) {
            grandTotal = grandTotal + subTotal;
            if (subTotal != 0d) {
                if (tempSize.equalsIgnoreCase(messageResources.getMessage("container40HC"))) {
                    tempSize = "40" + "'" + "HC";
                } else if (tempSize.equalsIgnoreCase(messageResources.getMessage("container40NOR"))) {
                    tempSize = "40" + "'" + "NOR";
                } else {
                    tempSize = tempSize + "'";
                }
                if (j > 0) {
                    cell = makeCellRightWithBoldFont("Total for size " + tempSize + "......................"
                            + "...................................." + numformat.format(subTotal) + "(USD)");
                    cell.setColspan(6);
                    chargesTable.addCell(cell);
                    j = 0;
                }

            }
            subTotal = 0.0;
        }

        //othercharges list
        double otherSubTotal = 0.00;
        String specequ = "";
        int otherCharges = 0;
        boolean printOtherTotal = false;
        boolean printGage1 = true;
        List otherChargesList = searchBookingReportDTO.getOtherChargesList();
        boolean printOtherGrandTotal = false;
        for (int i = 0; i < otherChargesList.size(); i++) {
            book = (BookingfclUnits) otherChargesList.get(i);
            if (book.getPrint() == null || !book.getPrint().equalsIgnoreCase("on")) {
                printOtherTotal = true;
                printOtherGrandTotal = true;
                if (i == 0) {
                    Paragraph bkgParagraph3 = new Paragraph();
                    specequ = "Per BL Charges";
                    bkgParagraph3.add(new Chunk(specequ, blackFontBold));
                    if (CommonUtils.isNotEmpty(specialEqu)) {
                        if ((unitType.equals("20") || unitType.equals("40"))) {
                            bkgParagraph3.add((new Chunk("       ")));
                        } else {
                            bkgParagraph3.add((new Chunk("   ")));
                        }
                        bkgParagraph3.add(new Chunk(specialEqu, GreenFont8));
                    } else if (!CommonUtils.isNotEmpty(specialEqu) && CommonUtils.isNotEmpty(bookingFcl.getOutofgage()) && bookingFcl.getOutofgage().equals("Y") && printGage1) {
                        if ((unitType.equals("20") || unitType.equals("40"))) {
                            bkgParagraph3.add((new Chunk("       ")));
                        } else {
                            bkgParagraph3.add((new Chunk("   ")));
                        }
                        bkgParagraph3.add(new Chunk(OUT_OF_GAUGE, GreenFont8));
                        printGage1 = false;
                    }
                    cell = makeCellRightWithBoldFont("");
                    cell.setBorderWidthBottom(0.1f);
                    cell.setBorderWidthLeft(0f);
                    cell.setBorderWidthRight(0f);
                    cell.setColspan(6);
                    cell.setBorderColorBottom(Color.LIGHT_GRAY);
                    chargesTable.addCell(cell);
                    cell = makeCell(bkgParagraph3, Element.ALIGN_LEFT);
                    cell.setLeading(0.7f, 0.7f);
                    cell.setNoWrap(true);
                    chargesTable.addCell(cell);
                    chargesTable.addCell(makeCellleftNoBorderBoldFont(""));
                    chargesTable.addCell(makeCellleftNoBorderBoldFont(book.getChgCode()));
                    chargesTable.addCell(makeCellleftNoBorderBoldFont(book.getCurrency()));
                    chargesTable.addCell(makeCellleftNoBorderBoldFont(""));
                    if (book.getNewFlag() != null && ("new".equals(book.getNewFlag()))) {
                        chargesTable.addCell(makeCellRightNoBorderBoldFont(numformat.format((book.getMarkUp() != null ? book.getMarkUp(): 0.00))));
                        otherSubTotal = otherSubTotal + (book.getMarkUp() != null ? book.getMarkUp(): 0.00);
                    } else {
                        if (spotRate) {
                            chargesTable.addCell(makeCellRightNoBorderBoldFont(numformat.format(book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00))));
                            otherSubTotal = otherSubTotal + book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00);
                        } else {
                            if (book.getAmount() != null) {
                                chargesTable.addCell(makeCellRightNoBorderBoldFont(numformat.format(book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00))));
                                otherSubTotal = otherSubTotal + book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00);
                            }
                        }
                    }
                } else {
                    if (CommonUtils.isNotEmpty(bookingFcl.getOutofgage()) && bookingFcl.getOutofgage().equals("Y") && printGage1) {
                        chargesTable.addCell(makeCellLeftWithNoBorderColoredGreen("                             " + OUT_OF_GAUGE));
                        printGage1 = false;
                    } else {
                        chargesTable.addCell(makeCellleftNoBorderBoldFont(""));
                    }
                    chargesTable.addCell(makeCellleftNoBorderBoldFont(""));
                    chargesTable.addCell(makeCellleftNoBorderBoldFont(book.getChgCode()));
                    chargesTable.addCell(makeCellleftNoBorderBoldFont(book.getCurrency()));
                    chargesTable.addCell(makeCellleftNoBorderBoldFont(""));
                    if (book.getNewFlag() != null && ("new".equals(book.getNewFlag()))) {
                        chargesTable.addCell(makeCellRightNoBorderBoldFont(numformat.format((book.getMarkUp() != null ? book.getMarkUp(): 0.00))));
                        otherSubTotal = otherSubTotal + (book.getMarkUp() != null ? book.getMarkUp(): 0.00);
                    } else if (book.getSellRate() != null) {
                        if (spotRate) {
                            chargesTable.addCell(makeCellRightNoBorderBoldFont(numformat.format(book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00))));
                            otherSubTotal = otherSubTotal + book.getAmount() + (book.getSpotRateMarkUp() != null ? book.getSpotRateMarkUp() : 0.00);
                        } else {
                            chargesTable.addCell(makeCellRightNoBorderBoldFont(numformat.format(book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00))));
                            otherSubTotal = otherSubTotal + book.getAmount() + (book.getMarkUp() != null ? book.getMarkUp(): 0.00);
                        }

                    }
                }
            }
        }
        if (otherChargesList.size() > 0 && printOtherTotal == true) {
            grandTotal = grandTotal + otherSubTotal;
            cell = makeCellRightWithBoldFont("Total............................................"
                    + "...................................." + numformat.format(otherSubTotal) + "(USD)");
            cell.setColspan(6);
            chargesTable.addCell(cell);
        } else {
            grandTotal = grandTotal + otherSubTotal;
        }
        boolean printGrandTotal1 = false;
        if (chargesList != null) {
            for (int n = 0; n < chargesList.size(); n++) {
                book = (BookingfclUnits) chargesList.get(n);
                if (book.getUnitType().getCodedesc() != null) {
                    if (book.getApproveBl() != null && book.getApproveBl().equalsIgnoreCase("Yes")) {
                        if (book.getPrint() != null && !book.getPrint().equalsIgnoreCase("on")) {
                            String newTemp = book.getUnitType().getCodedesc();
                            printGrandTotal1 = true;
                            for (int m = 0; m < chargesList.size(); m++) {
                                book = (BookingfclUnits) chargesList.get(m);
                                if (book.getUnitType().getCodedesc() != null) {
                                    if (book.getApproveBl() != null && book.getApproveBl().equalsIgnoreCase("Yes")) {
                                        if (book.getPrint() != null && !book.getPrint().equalsIgnoreCase("on")) {
                                            String newTemp2 = book.getUnitType().getCodedesc();
                                            if ((!"".equals(newTemp) && newTemp != null) && (!"".equals(newTemp2) && newTemp2 != null) && (newTemp != newTemp2)) {
                                                printGrandTotal = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (printGrandTotal || (printGrandTotal1 && printOtherGrandTotal)) {
            String freightPrepaid = "";
            if (null != bookingFcl.getPrepaidCollect() && bookingFcl.getPrepaidCollect().equalsIgnoreCase("P")) {
                freightPrepaid = "Freight Prepaid";
            } else if (null != bookingFcl.getPrepaidCollect() && bookingFcl.getPrepaidCollect().equalsIgnoreCase("C")) {
                freightPrepaid = "Freight Collect";
            }
            cell = makeCellRightWithBoldFont("");
            cell.setBorderWidthBottom(0.1f);
            cell.setBorderWidthLeft(0f);
            cell.setBorderWidthRight(0f);
            cell.setColspan(6);
            cell.setBorderColorBottom(Color.LIGHT_GRAY);
            chargesTable.addCell(cell);
            cell = makeCellleftBold(freightPrepaid);
            chargesTable.addCell(cell);
            cell = makeCellRightWithBoldFont("Grand Total......................................."
                    + ".............................." + numformat.format(grandTotal) + "(USD)");
            cell.setColspan(5);
            chargesTable.addCell(cell);
        } else {
            String freightPrepaid = "";
            if (null != bookingFcl.getPrepaidCollect() && bookingFcl.getPrepaidCollect().equalsIgnoreCase("P")) {
                freightPrepaid = "Freight Prepaid";
            } else if (null != bookingFcl.getPrepaidCollect() && bookingFcl.getPrepaidCollect().equalsIgnoreCase("C")) {
                freightPrepaid = "Freight Collect";
            }
            cell = makeCellleftBold(freightPrepaid);
            cell.setColspan(6);
            chargesTable.addCell(cell);
        }
        return chargesTable;

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

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {

            //---------------
            //this for print page number at the bottom in the format x of y
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            String text = "Page " + writer.getPageNumber() + " of ";
            float textBase = document.bottom() - (document.bottomMargin() - 10);
            //float textBase = document.bottom() - 20;
            float textSize = helv.getWidthPoint(text, 12);
            cb.beginText();
            cb.setFontAndSize(helv, 7);
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

    public String createReport(SearchBookingReportDTO searchBookingReportDTO, String simpleRequest,
            MessageResources messageResources, String printFromBl, String documentName,
            String fromEmailAddress, String fromName, HttpServletRequest request) throws Exception {
        try {
            this.initialize(searchBookingReportDTO, simpleRequest, messageResources, documentName);
            this.createBody(searchBookingReportDTO, simpleRequest, messageResources,
                    printFromBl, documentName, fromEmailAddress, fromName, request);
            this.destroy();
        } catch (Exception e) {
            log.info("createReport failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
        return searchBookingReportDTO.getFileName();

    }

    private String getSpecialEqup(GenericCode gc) {
        String codeDescripition = "";
        codeDescripition = gc.getCodedesc() != null ? gc.getCodedesc() : gc.getCodedesc();
        if (CommonUtils.isNotEmpty(codeDescripition)) {
            if (codeDescripition.equals("OT")) {
                codeDescripition = "OPEN TOP";
            } else if (codeDescripition.equals("FR")) {
                codeDescripition = "FLAT RACK";
            }
        }
        return codeDescripition;
    }

    private String getGRIRemarkks(BookingFcl bookingFcl) throws Exception {
        String unlocationCode = "";
        String Destination = "";
        String GRIRemarks = "";
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        if (bookingFcl.getPortofDischarge() != null) {
            Destination = bookingFcl.getPortofDischarge();
            int j = Destination.indexOf("/");
            if (j != -1) {
                String a[] = Destination.split("/");
                Destination = a[0];
                if (bookingFcl.getPortofDischarge().lastIndexOf("(") != -1) {
                    unlocationCode = bookingFcl.getPortofDischarge().substring(bookingFcl.getPortofDischarge().lastIndexOf("(") + 1,
                            bookingFcl.getPortofDischarge().lastIndexOf(")"));
                } else {
                    unlocationCode = a[1];
                }
            }
            GRIRemarks = unLocationDAO.getDestinationGRIRemarks(Destination, bookingFcl.getSSLine(), unlocationCode);
        }
        return GRIRemarks;
    }
}
