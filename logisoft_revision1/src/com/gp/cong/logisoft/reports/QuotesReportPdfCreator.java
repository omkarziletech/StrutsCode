package com.gp.cong.logisoft.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.bc.fcl.QuotationConstants;
import com.gp.cong.logisoft.bc.fcl.QuoteDwrBC;
import com.gp.cong.logisoft.beans.CostBean;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.ChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.gp.cvst.logisoft.hibernate.dao.RetAddDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.reports.dto.QuotationDTO;
import com.lowagie.text.Chunk;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

/**
 * @author Pradeep on:06 Feb 2009
 *
 */
public class QuotesReportPdfCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(QuotesReportPdfCreator.class);
    // QuotesReportPdfCreator quotesReportPdfCreator=new
    // QuotesReportPdfCreator();
    Document document = new Document(PageSize.A4);
    PdfWriter pdfWriter = null;
    Element element = null;
    Quotation quote = null;
    protected PdfTemplate total;
    protected BaseFont helv;
    private static final HashMap<String, Quotation> hashMap = new HashMap<String, Quotation>();
    private static String contextPath1 = "";
    Font italicFont = new Font(Font.ITALIC, 8, Font.ITALIC, Color.BLACK);
    public static MessageResources messageResource = null;
    private static final String OUT_OF_GAUGE = "OUT OF GAUGE";
    HttpServletRequest request;
    FclBl fclBl = null;
    BookingFcl bookingFcl = null;

    public QuotesReportPdfCreator(HttpServletRequest request) {
        this.request = request;
    }

    public void initialize(String fileName, HttpServletRequest request, Quotation quotation) throws FileNotFoundException, DocumentException, Exception {
        quote = getQuote();
        document.setMargins(4, 4, 4, 4);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        pdfWriter.setPageEvent(new QuotesReportPdfCreator(request));
//        String companyName = null != LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName")
//                ? LoadLogisoftProperties.getProperty("application.fclBl.print.companyFullName").toUpperCase() : "OTI cargo Inc";
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        BookingFclDAO bookingFclDao = new BookingFclDAO();
        String fileno = null != quote.getFileNo() ? quote.getFileNo() : "";
        bookingFcl = bookingFclDao.findbyFileNo(fileno);
        fclBl = new FclBlDAO().getOriginalBl(fileno);
        String brand = "";
        if (null != fclBl && null != fclBl.getBrand()) {
            brand = fclBl.getBrand();
        } else if (null != bookingFcl && null != bookingFcl.getBrand()) {
            brand = bookingFcl.getBrand();
        } else if (null != quote && null != quote.getBrand()) {
            brand = quote.getBrand();
        }

        if (brand.equals("Econo") && ("03").equals(companyCode)) {
            String companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
            HeaderFooter footer = new HeaderFooter(
                    new Phrase("Thank you for choosing " + companyName + " for all your shipping needs", new Font(Font.HELVETICA, 10, Font.ITALIC, Color.BLACK)), false);
            footer.setBorder(Rectangle.NO_BORDER);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.setFooter(footer);
        } else if (brand.equals("OTI") && ("02").equals(companyCode)) {
            String companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
            HeaderFooter footer = new HeaderFooter(
                    new Phrase("Thank you for choosing " + companyName + " for all your shipping needs", new Font(Font.HELVETICA, 10, Font.ITALIC, Color.BLACK)), false);
            footer.setBorder(Rectangle.NO_BORDER);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.setFooter(footer);
        } else if (brand.equalsIgnoreCase("Ecu Worldwide")) {
            String companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
            HeaderFooter footer = new HeaderFooter(
                    new Phrase("Thank you for choosing " + companyName + " for all your shipping needs", new Font(Font.HELVETICA, 10, Font.ITALIC, Color.BLACK)), false);
            footer.setBorder(Rectangle.NO_BORDER);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.setFooter(footer);
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        if (null != user) {
            if (hashMap.get(user.getLoginName()) == null) {
                hashMap.put(user.getLoginName(), getQuote());
            } else {
                hashMap.put(user.getLoginName(), getQuote());
            }
        }
        document.open();
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        try {
            total = writer.getDirectContent().createTemplate(100, 100);
            total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
            helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
                    BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("QuotesReportPdfCreator onOpenDocument failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        try {
            total.beginText();
            total.setFontAndSize(helv, 7);
            total.setTextMatrix(0, 0);
            total.showText(String.valueOf(writer.getPageNumber() - 1));
            total.endText();
        } catch (Exception e) {
            log.info("QuotesReportPdfCreator onCloseDocument failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {

            onStartQuotationPage(writer, document);
        } catch (Exception e) {
            log.info("QuotesReportPdfCreator onStartPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    public void onStartQuotationPage(PdfWriter writer, Document document) throws Exception {
        String path = LoadLogisoftProperties.getProperty("application.image.logo");
        String econoPath = LoadLogisoftProperties.getProperty("application.image.econo.logo");
        String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
        PdfPCell cell = new PdfPCell();
        PdfPCell celL = new PdfPCell();
        PdfPTable table = new PdfPTable(1);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        quote = hashMap.get(user.getLoginName());
        String fileno = null != quote.getFileNo() ? quote.getFileNo() : "";
        bookingFcl = new BookingFclDAO().findBookingByFileNo(fileno);
        fclBl = new FclBlDAO().getOriginalBl(fileno);
        String brand = "";
        if (null != fclBl && null != fclBl.getBrand()) {
            brand = fclBl.getBrand();
        } else if (null != bookingFcl && null != bookingFcl.getBrand()) {
            brand = bookingFcl.getBrand();
        } else if (null != quote && null != quote.getBrand()) {
            brand = quote.getBrand();
        }
        if (brand.equals("Econo") && ("03").equals(companyCode)) {
            Image img = Image.getInstance(contextPath1 + econoPath);
            table.setWidthPercentage(100);
            img.scalePercent(60);
//            img.scaleAbsoluteWidth(200);--large image scaling
//            celL.addElement(new Chunk(img, 180, -25));
            img.setAlignment(Element.ALIGN_CENTER);
            celL.addElement(img);
            celL.setBorder(0);
            celL.setHorizontalAlignment(Element.ALIGN_CENTER);
            celL.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(celL);
            DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            Date currentDate = new Date();
            cell = makeCellRightNoBorder("Date Printed : " + df7.format(currentDate));
            cell.setPaddingTop(10f);
        } else if (brand.equals("OTI") && ("02").equals(companyCode)) {
            Image img = Image.getInstance(contextPath1 + econoPath);
            table.setWidthPercentage(100);
            img.scalePercent(60);
//            img.scaleAbsoluteWidth(200);
//            celL.addElement(new Chunk(img, 180, -25));
            img.setAlignment(Element.ALIGN_CENTER);
            celL.addElement(img);
            celL.setBorder(0);
            celL.setHorizontalAlignment(Element.ALIGN_CENTER);
            celL.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(celL);
            DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            Date currentDate = new Date();
            cell = makeCellRightNoBorder("Date Printed : " + df7.format(currentDate));
            cell.setPaddingTop(10f);
        } else if (brand.equalsIgnoreCase("Ecu Worldwide")) {
            Image img = Image.getInstance(contextPath1 + path);
            table.setWidthPercentage(100);
            img.scalePercent(60);
//            img.scaleAbsoluteWidth(200);
//            celL.addElement(new Chunk(img, 180, -25));
            img.setAlignment(Element.ALIGN_CENTER);
            celL.addElement(img);
            celL.setBorder(0);
            celL.setHorizontalAlignment(Element.ALIGN_CENTER);
            celL.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(celL);
            DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            Date currentDate = new Date();
            cell = makeCellRightNoBorder("Date Printed : " + df7.format(currentDate));
            cell.setPaddingTop(10f);
        }
//        if (path.equalsIgnoreCase("/img/companyLogo/EconocaribePrintLogo.gif")) {
//            Image img = Image.getInstance(contextPath1 + path);
//            table.setWidthPercentage(100);
//            img.scalePercent(60);
//            img.scaleAbsoluteWidth(556);
//            celL.addElement(new Chunk(img, -2, -25));
//            celL.setBorder(0);
//            celL.setHorizontalAlignment(Element.ALIGN_LEFT);
//            celL.setVerticalAlignment(Element.ALIGN_CENTER);
//            table.addCell(celL);
//            DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
//            Date currentDate = new Date();
//            cell = makeCellRightNoBorderWhiteFont("Date Printed : " + df7.format(currentDate));
//            cell.setPaddingTop(10f);
//        } else {
//            Image img = Image.getInstance(contextPath1 + path);
//            table.setWidthPercentage(100);
//            img.scalePercent(60);
//            img.scaleAbsoluteWidth(200);
//            celL.addElement(new Chunk(img, 180, -25));
//            celL.setBorder(0);
//            celL.setHorizontalAlignment(Element.ALIGN_CENTER);
//            celL.setVerticalAlignment(Element.ALIGN_CENTER);
//            table.addCell(celL);
//            DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
//            Date currentDate = new Date();
//            cell = makeCellRightNoBorder("Date Printed : " + df7.format(currentDate));
//            cell.setPaddingTop(10f);
//        }
        table.addCell(cell);
        //for goods descritption and remarks
        PdfPTable bookTable = new PdfPTable(3);
        bookTable.setWidthPercentage(100);
        bookTable.setWidths(new float[]{33, 47, 20});
        bookTable.getDefaultCell().setPadding(0);
        bookTable.getDefaultCell().setBorderWidth(0.5f);
        bookTable.getDefaultCell().setBorderWidthLeft(0.0f);
        bookTable.getDefaultCell().setBorderWidthRight(0.0f);

        cell = makeCellLeftNoBorder("");
        cell.setColspan(3);
        bookTable.addCell(cell);
        // HEADING WITH BACKGROUND COLOR

        DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        DateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("loginuser");
//        quote = hashMap.get(user.getLoginName());
        String quoteDate = "Date Created : " + "" + df7.format(quote.getQuoteDate());
        Phrase headingPhrase1 = new Phrase(quoteDate, blackFontForAR);
        PdfPCell headingCell1 = new PdfPCell(headingPhrase1);
        headingCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        headingCell1.setVerticalAlignment(Element.ALIGN_CENTER);
        headingCell1.setPaddingTop(2);
        headingCell1.setPaddingBottom(0);
        headingCell1.setBorder(0);
        headingCell1.setBackgroundColor(Color.LIGHT_GRAY);
        bookTable.addCell(headingCell1);

        String heading = "FCL Rate Quote" + " 04-" + String.valueOf(quote.getFileNo());
        Phrase headingPhrase = new Phrase(heading, headingFont1);
        PdfPCell headingCell = new PdfPCell(headingPhrase);
        headingCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headingCell.setVerticalAlignment(Element.ALIGN_TOP);
        headingCell.setPaddingTop(-2);
        headingCell.setPaddingBottom(2);
        headingCell.setBorder(0);

        headingCell.setBackgroundColor(Color.LIGHT_GRAY);
        bookTable.addCell(headingCell);

        String heading2 = "";
        Phrase headingPhrase2 = new Phrase(heading2, headingFont1);
        PdfPCell headingCell2 = new PdfPCell(headingPhrase2);
        headingCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        headingCell2.setVerticalAlignment(Element.ALIGN_TOP);
        headingCell2.setPaddingTop(-2);
        headingCell2.setPaddingBottom(2);
        headingCell2.setBorder(0);
        headingCell2.setBackgroundColor(Color.LIGHT_GRAY);
        bookTable.addCell(headingCell2);
        document.add(table);
        document.add(bookTable);
    }

    public void createBody(List<QuotationDTO> QtFieldsList, List<CostBean> otherChargesLIst, String comments,
            String ratechangeAlert, String disclaimer, Quotation quotation,
            String grandTotal, MessageResources messageResources,
            String fromEmailAddress, String fromName, HttpServletRequest request) throws Exception {
        quote = quotation;
        NumberFormat numformat = new DecimalFormat("##,###,##0.00");
        PdfPCell cell = null;
        QuotationBC quotationBC = new QuotationBC();
        boolean spotRate = quotation.getSpotRate().equals("Y");
        if ("Y".equalsIgnoreCase(quotation.getChangeIssuingTerminal())) {
            User quoteBy = new UserDAO().findUserName(quotation.getQuoteBy());
            if (null != quoteBy.getTerminal() && null != quoteBy.getTerminal().getTerminalLocation() && !quotation.getIssuingTerminal().contains(quoteBy.getTerminal().getTerminalLocation())) {
                String terminal = quotation.getIssuingTerminal().substring(quotation.getIssuingTerminal().indexOf("-") + 1);
                if (null != terminal) {
                    RefTerminal refTerminal = new RefTerminalDAO().findById(terminal);
                    if (null != refTerminal) {
                        quote.setUserEmail(new RetAddDAO().getRetAddEmail(terminal, refTerminal.getGovSchCode()));
                        quote.setUserFax(refTerminal.getFaxnum1());
                        quote.setUserPhone(refTerminal.getPhnnum1());
                    }

                }
            }
        }

        //remarks
        String originCode = null;
        String propRemarks = null;
        String orginRemarks = "";
        if (quote.getOrigin_terminal() != null && !quote.getOrigin_terminal().equalsIgnoreCase("")) {
            if (quote.getOrigin_terminal().lastIndexOf("(") != -1) {
                originCode = quote.getOrigin_terminal().substring(quote.getOrigin_terminal().lastIndexOf("(") + 1,
                        quote.getOrigin_terminal().lastIndexOf(")"));
            }
        }
        if (quote.getZip() != null && !quote.getZip().trim().equals("")) {
            propRemarks = new UnLocationDAO().getpropertyRemarks(originCode);
        }

        // table for quote details
        PdfPTable quoteDetails = makeTable(2);
        quoteDetails.setWidthPercentage(100);

        quoteDetails.setWidths(new float[]{49f, 51f});

        PdfPTable toEmailTable = makeTable(2);
        toEmailTable.setWidthPercentage(100);
        toEmailTable.setTotalWidth(100f);
        toEmailTable.setWidths(new float[]{9.7f, 90.3f});

        cell = makeCellLeftNoBorder("To");
        cell.setPaddingLeft(1.5f);
        toEmailTable.addCell(cell);

        cell = makeCellLeftNoBorderBold(": " + quote.getContactname());
        toEmailTable.addCell(cell);

        cell = makeCellLeftNoBorder("Email");
        cell.setPaddingLeft(1.5f);
        toEmailTable.addCell(cell);
        cell = makeCellLeftNoBorderBold(": " + quote.getEmail1());
        toEmailTable.addCell(cell);

        cell = makeCellleftwithLeftBorder("");
        cell.setBorderWidthRight(0.6f);
        cell.setColspan(2);
        cell.addElement(toEmailTable);
        quoteDetails.addCell(cell);

        PdfPTable quoteDetails1 = makeTable(2);
        quoteDetails1.setWidthPercentage(101);
        quoteDetails1.setWidths(new float[]{20, 80});

        quoteDetails1.addCell(makeCellLeftNoBorder("Phone"));
        cell = makeCellLeftNoBorderBold(": " + quote.getPhone());
        quoteDetails1.addCell(cell);

        cell = makeCellLeftNoBorder("Fax");
        quoteDetails1.addCell(cell);
        cell = makeCellLeftNoBorderBold(": " + quote.getFax());
        quoteDetails1.addCell(cell);

        cell = makeCellLeftNoBorder("Company");
        cell.setBorderWidthBottom(0.6f);
        quoteDetails1.addCell(cell);
        cell = makeCellLeftNoBorderBold(": " + quote.getClientname());
        cell.setBorderWidthBottom(0.6f);
        quoteDetails1.addCell(cell);

        if (CommonUtils.isNotEmpty(fromEmailAddress)) {
            quoteDetails1.addCell(makeCellLeftNoBorder("From"));
            cell = makeCellLeftNoBorderBold(": " + fromName);
            quoteDetails1.addCell(cell);

            quoteDetails1.addCell(makeCellLeftNoBorder("Phone"));
            cell = makeCellLeftNoBorderBold(": " + quote.getUserPhone());
            quoteDetails1.addCell(cell);

            quoteDetails1.addCell(makeCellLeftNoBorder("Fax"));
            cell = makeCellLeftNoBorderBold(": " + quote.getUserFax());
            quoteDetails1.addCell(cell);

            quoteDetails1.addCell(makeCellLeftNoBorder("Email"));
            cell = makeCellLeftNoBorderBold(": " + fromEmailAddress);
            quoteDetails1.addCell(cell);
        } else {
            quoteDetails1.addCell(makeCellLeftNoBorder("From"));
            cell = makeCellLeftNoBorderBold(": " + quote.getFrom());
            quoteDetails1.addCell(cell);

            quoteDetails1.addCell(makeCellLeftNoBorder("Phone"));
            cell = makeCellLeftNoBorderBold(": " + quote.getUserPhone());
            quoteDetails1.addCell(cell);

            quoteDetails1.addCell(makeCellLeftNoBorder("Fax"));
            cell = makeCellLeftNoBorderBold(": " + quote.getUserFax());
            quoteDetails1.addCell(cell);

            quoteDetails1.addCell(makeCellLeftNoBorder("Email"));
            cell = makeCellLeftNoBorderBold(": " + quote.getUserEmail());
            quoteDetails1.addCell(cell);
        }
        PdfPTable quoteDetails2 = makeTable(2);
        quoteDetails2.setWidthPercentage(100);
        quoteDetails2.setWidths(new float[]{20, 80});
//       session.removeAttribute("valueFromTerminal");
//       session.removeAttribute("phoneFromTerminal");
//       session.removeAttribute("faxFromTerminal");
//      session.removeAttribute("nameTerminal");
        boolean importFlg = (null != quote && null != quote.getFileType() && quote.getFileType().equalsIgnoreCase("I"));
        if (importFlg) {
            if (null != quote.getDoorDestination() && !quote.getDoorDestination().equals("")) {
                quoteDetails2.addCell(makeCellLeftNoBorder("Door Origin"));
                quoteDetails2.addCell(makeCellLeftNoBorderCheckNull(": " + String.valueOf(getCityStateName(quote.getDoorDestination()))));
            }
        } else {
            if (quote.getDoorOrigin() != null && !quote.getDoorOrigin().equals("") && quote.getRampCheck() != null && !quote.getRampCheck().equals("")
                    && quote.getRampCheck().equalsIgnoreCase("on")) {
                quoteDetails2.addCell(makeCellLeftNoBorder("Ramp Origin"));
                quoteDetails2.addCell(makeCellLeftNoBorderCheckNull(": " + String.valueOf(quote.getDoorOrigin())));
            } else if (quote.getDoorOrigin() != null && !quote.getDoorOrigin().equals("")) {
                quoteDetails2.addCell(makeCellLeftNoBorder("Door Origin"));
                String zip = quote.getZip();
                if (null != zip && zip.contains("-")) {
                    zip = zip.substring(0, zip.indexOf("-"));
                }
                quoteDetails2.addCell(makeCellLeftNoBorderCheckNull(": "
                        + String.valueOf(quote.getDoorOrigin()) + "        " + "Origin Zip" + ": " + zip));
            }
        }

        if (quote.getOrigin_terminal() != null && !quote.getOrigin_terminal().equalsIgnoreCase("")) {
            quoteDetails2.addCell(makeCellLeftNoBorder("Origin"));
            quoteDetails2.addCell(makeCellLeftNoBorderBold(": " + getCityStateName(quote.getOrigin_terminal())));
        }
        if (quote.getPlor() != null && quote.getRampCity() != null && quote.getRampCity().replace("/ ", "/").trim().equals(quote.getPlor().
                replace("/ ", "/").trim())) {
            if (!quote.getPlor().equalsIgnoreCase("")) {
                quoteDetails2.addCell(makeCellLeftNoBorder("POL"));
                quoteDetails2.addCell(makeCellLeftNoBorderBold(": " + getCityStateName(quote.getPlor())));
            }
        } else {
            if (quote.getRampCity() != null && !quote.getRampCity().equalsIgnoreCase("")) {
                quoteDetails2.addCell(makeCellLeftNoBorder("Ramp City"));
                quoteDetails2.addCell(makeCellLeftNoBorderBold(": " + getCityStateName(quote.getRampCity())));
            }
            if (quote.getPlor() != null && !quote.getPlor().equalsIgnoreCase("")) {
                quoteDetails2.addCell(makeCellLeftNoBorder("POL"));
                quoteDetails2.addCell(makeCellLeftNoBorderBold(": " + getCityStateName(quote.getPlor())));
            }
        }
        if (quote.getFinaldestination() != null && !quote.getFinaldestination().equalsIgnoreCase("")) {
            quoteDetails2.addCell(makeCellLeftNoBorder("POD"));
            quoteDetails2.addCell(makeCellLeftNoBorderBold(": " + getCityStateName(quote.getFinaldestination())));
        }
        if (quote.getDestination_port() != null && !quote.getDestination_port().equalsIgnoreCase("")) {
            quoteDetails2.addCell(makeCellLeftNoBorder("Destination"));
            quoteDetails2.addCell(makeCellLeftNoBorderBold(": " + getCityStateName(quote.getDestination_port())));
        }
        if (importFlg) {
            if (quote.getDoorOrigin() != null && !quote.getDoorOrigin().equals("")) {
                quoteDetails2.addCell(makeCellLeftNoBorder("Door Dest"));
                String zip = quote.getZip();
                if (null != zip && zip.contains("-")) {
                    zip = zip.substring(0, zip.indexOf("-"));
                }
                quoteDetails2.addCell(makeCellLeftNoBorderCheckNull(": "
                        + String.valueOf(quote.getDoorOrigin()) + "        " + "Dest Zip" + ": " + zip));
            }
        } else {
            if (null != quote.getDoorDestination() && !quote.getDoorDestination().equals("")) {
                if ("on".equalsIgnoreCase(quote.getOnCarriage())) {
                    quoteDetails2.addCell(makeCellLeftNoBorder("Final Destination"));
                } else {
                    quoteDetails2.addCell(makeCellLeftNoBorder("Door Destination"));
                }
                quoteDetails2.addCell(makeCellLeftNoBorderCheckNull(": " + String.valueOf(getCityStateName(quote.getDoorDestination()))));
            }
        }
        // Check for CommodityPrint Check Box.
        if (quote.getCommodityPrint() != null && quote.getCommodityPrint().equals("on")) {
            quoteDetails2.addCell(makeCellLeftNoBorder("Commodity"));
            quoteDetails2.addCell(makeCellLeftNoBorderBold(": " + quote.getCommcode().getCodedesc()));
        }

        // Check for carrier print check box
        if (quote.getCarrierPrint() != null && quote.getCarrierPrint().equals("on")) {
            quoteDetails2.addCell(makeCellLeftNoBorder("Carrier"));
            quoteDetails2.addCell(makeCellLeftNoBorderBold(": " + quote.getSslname()));
        }
        quoteDetails2.addCell(makeCellLeftNoBorder("Transit Time"));

        if (quote.getTransitTime() != null) {
            quoteDetails2.addCell(makeCellLeftNoBorderCheckNull(": " + String.valueOf(quote.getTransitTime()) + " " + "Days"));
        } else {
            quoteDetails2.addCell(makeCellLeftNoBorderBold(": "));
        }

        cell = makeCellleftwithLeftBorder("");
        cell.setBorderWidthBottom(0.6f);
        cell.addElement(quoteDetails1);
        cell.setBorderWidthRight(0.6f);
        quoteDetails.addCell(cell);
        cell = makeCellleftNoBorder("");
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.addElement(quoteDetails2);
        cell.setBorderWidthTop(0.6f);
        quoteDetails.addCell(cell);

        document.add(quoteDetails);
        PdfPTable DescriptionRemarksTable = makeTable(2);
        DescriptionRemarksTable.setWidthPercentage(100);
        DescriptionRemarksTable.setWidths(new float[]{49, 51});

        PdfPTable DescriptionTable = makeTable(1);
        DescriptionTable.setWidthPercentage(100);
        cell = makeCellCenterNoBorder("Goods Description");
        cell.setPaddingTop(-2f);
        DescriptionTable.addCell(cell);
        cell = makeCellLeftNoBorderBold(quote.getGoodsdesc());
        DescriptionTable.addCell(cell);
        cell = makeCellleftBold("");
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.addElement(DescriptionTable);
        DescriptionRemarksTable.addCell(cell);
        PdfPTable remarksTable = makeTable(1);
        remarksTable.setWidthPercentage(100);
        cell = makeCellCenterNoBorder("Remarks");
        cell.setPaddingTop(-2f);
        remarksTable.addCell(cell);
        cell = makeCellLeftNoBorderBold(quote.getComment1());
        remarksTable.addCell(cell);
        cell = makeCellleftBold("");
        cell.setBorderWidthTop(0.0f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthLeft(0);
        cell.addElement(remarksTable);
        DescriptionRemarksTable.addCell(cell);

        //FOR PORTS REMARKS
        PdfPTable portsRemarksTable1 = makeTable(2);
        portsRemarksTable1.setWidths(new float[]{15, 85});
        portsRemarksTable1.setWidthPercentage(100);

        cell = makeCellleftwithLeftBorder("Port Remarks/GRI:");
        portsRemarksTable1.addCell(cell);

        StringBuilder stringBuilder = new StringBuilder();
        if (quote.getRemarks() != null && !quote.getRemarks().equals("")) {
            stringBuilder.append(quote.getRemarks());
            if (quote.getRatesRemarks() != null && !quote.getRatesRemarks().equals("")) {
                stringBuilder.append("\n");
                stringBuilder.append(quote.getRatesRemarks());
            }
        } else {
            stringBuilder.append(quote.getRatesRemarks());
        }
        if (CommonUtils.isNotEmpty(quote.getFclTempRemarks())) {
            stringBuilder.append("\n");
            stringBuilder.append("\n");
            stringBuilder.append(quote.getFclTempRemarks());
        }
        if (CommonUtils.isNotEmpty(quote.getFclGRIRemarks())) {
            stringBuilder.append("\n");
            stringBuilder.append("\n");
            stringBuilder.append(quote.getFclGRIRemarks());
        }
        if (CommonUtils.isNotEmpty(propRemarks)) {
            stringBuilder.append("\n");
            stringBuilder.append("\n");
            stringBuilder.append(propRemarks);
        }
        if (quotation.getRatesNonRates() != null && quotation.getRatesNonRates().equals("R")) {
            portsRemarksTable1.addCell(makeCellLeftWithRightBorderColored(stringBuilder.toString()));
        } else if (quotation.getRatesNonRates() != null && quotation.getRatesNonRates().equals("N")
                && quotation.getPrintPortRemarks() != null && quotation.getPrintPortRemarks().equals("on")) {
            portsRemarksTable1.addCell(makeCellLeftWithRightBorderColored(stringBuilder.toString()));
        } else {
            portsRemarksTable1.addCell(makeCellLeftWithRightBorderColored(""));
        }

        cell = makeCellLeftWithRightLeftBorder("");
        cell.setBorderWidthBottom(0.6f);
        cell.setColspan(2);
        portsRemarksTable1.addCell(cell);

        //FOR REGION REMARKS
        PdfPTable regionRemarksTable = makeTable(2);
        regionRemarksTable.setWidths(new float[]{15, 85});
        regionRemarksTable.setWidthPercentage(100);

        cell = makeCellleftwithLeftBorder("Region Remarks:");
        regionRemarksTable.addCell(cell);

        StringBuilder stringBuilderNew = new StringBuilder();
        if (quote.getRegionRemarks() != null && !quote.getRegionRemarks().equals("")) {
            stringBuilderNew.append(quote.getRegionRemarks());
        }
        if (CommonUtils.isNotEmpty(quotation.getOrigin_terminal())) {
            String[] countryName = quotation.getOrigin_terminal().split("/");
            orginRemarks = new PortsDAO().fetchOrginRemarks(countryName[0]);
            stringBuilderNew.append("\n");
            stringBuilderNew.append("\n");
            stringBuilderNew.append(orginRemarks);
        }

        regionRemarksTable.addCell(makeCellLeftWithRightBorderColored(stringBuilderNew.toString()));
        cell = makeCellLeftWithRightLeftBorder("");
        cell.setBorderWidthBottom(0.6f);
        cell.setColspan(2);
        regionRemarksTable.addCell(cell);

        // TABLE FOR CHARGES
        PdfPTable ChargeHeading = makeTable(5);
        ChargeHeading.setWidthPercentage(100);
        ChargeHeading.setWidths(new float[]{15, 22, 30, 8, 15});

        // Goods Description.
        if (quote.getPrintDesc() != null && quote.getPrintDesc().equals("on")) {
            ChargeHeading.addCell(makeCellleftNoBorder("Goods Description :"));
            PdfPCell goodsdescCell = makeCellleftNoBorder(quote.getGoodsdesc());
            goodsdescCell.setColspan(3);
            ChargeHeading.addCell(goodsdescCell);
        }
        cell = makeCellleftwithUnderLine("Container Size");
        cell.setBorderWidthLeft(0.6f);
        ChargeHeading.addCell(cell);
        ChargeHeading.addCell(makeCellleftwithUnderLine(""));
        ChargeHeading.addCell(makeCellleftwithUnderLine("Charge"));
        ChargeHeading.addCell(makeCellleftwithUnderLine("Currency"));
        cell = makeCellRightwithUnderLine("Amount");
        cell.setBorderWidthRight(0.6f);
        ChargeHeading.addCell(cell);

        //Bundle in to ofr calculation;
        ChargesDAO chargesDAO = new ChargesDAO();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        Double oceanFreightRateTotal = 0.0;
        Double amtDup = 0.00;
        int count = 0;
        for (CostBean otherChargesLIst1 : otherChargesLIst) {
            CostBean costb = (CostBean) otherChargesLIst1;
            if (costb.getOtherprint() != null && costb.getOtherprint().equals("on")) {
                amtDup = (costb.getMarkup() != null ? Double.parseDouble(costb.getMarkup().replaceAll(",", "")) : 0.00);
                oceanFreightRateTotal = oceanFreightRateTotal + amtDup;
            }
        }
        List fclRatesListForCalcualtion = (List) chargesDAO.getChargesforQuotation1(quotation.getQuoteId());
        for (Object fclRatesListForCalcualtion1 : fclRatesListForCalcualtion) {
            Charges charges = (Charges) fclRatesListForCalcualtion1;
            if (charges.getUnitType() != null && !charges.getUnitType().equals("") && !charges.getUnitType().equals("0.00")) {
                GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(charges.getUnitType()));
                if (genericCode != null) {
                    charges.setUnitName(genericCode.getCodedesc());
                }
            }
        }
        boolean importFlag = (null != quote && null != quote.getFileType() && quote.getFileType().equalsIgnoreCase("I"));
        List fclRates1 = new ArrayList();
        if ("N".equalsIgnoreCase(quotation.getRatesNonRates())) {
            fclRates1 = quotationBC.orderNonRatedList(fclRatesListForCalcualtion);
        } else {
            fclRates1 = quotationBC.consolidateRates(fclRatesListForCalcualtion, messageResources, importFlag);
        }
        String tempUnitType = "";
        int k = 0;
        double[] OFR = new double[10];
        if (!fclRates1.isEmpty() && fclRates1.get(0) != null) {
            Charges charges1 = (Charges) fclRates1.get(0);
            if (null == charges1.getSpecialEquipmentUnit()) {
                charges1.setSpecialEquipmentUnit("");
            }
            tempUnitType = charges1.getUnitType() + "-" + charges1.getSpecialEquipmentUnit() + "-" + charges1.getStandardCharge();
        }
        for (Object fclRates11 : fclRates1) {
            Charges charges1 = (Charges) fclRates11;
            if (null == charges1.getSpecialEquipmentUnit()) {
                charges1.setSpecialEquipmentUnit("");
            }
            if (tempUnitType.equalsIgnoreCase(charges1.getUnitType() + "-" + charges1.getSpecialEquipmentUnit() + "-" + charges1.getStandardCharge())) {
                if (charges1.getPrint() != null && charges1.getPrint().equalsIgnoreCase("on") && !charges1.getChargeCodeDesc().equalsIgnoreCase("OCNFRT")) {
                    if ("M".equalsIgnoreCase(charges1.getChargeFlag()) || "CH".equalsIgnoreCase(charges1.getChargeFlag())) {
                        if (spotRate) {
                            oceanFreightRateTotal = oceanFreightRateTotal + (null != charges1.getSpotRateMarkUp() ? charges1.getSpotRateMarkUp() : 0d) + charges1.getAdjestment();
                        } else {
                            oceanFreightRateTotal = oceanFreightRateTotal + charges1.getMarkUp() + charges1.getAdjestment();
                        }
                    } else {
                        if (spotRate) {
                            oceanFreightRateTotal = oceanFreightRateTotal + charges1.getAmount() + (null != charges1.getSpotRateMarkUp() ? charges1.getSpotRateMarkUp() : 0d) + charges1.getAdjestment();
                        } else {
                            oceanFreightRateTotal = oceanFreightRateTotal + charges1.getAmount() + charges1.getMarkUp() + charges1.getAdjestment();
                        }
                    }
                }
            } else {
                OFR[k] = oceanFreightRateTotal;
                k++;
                oceanFreightRateTotal = 0.0;
                tempUnitType = charges1.getUnitType() + "-" + charges1.getSpecialEquipmentUnit() + "-" + charges1.getStandardCharge();
                if (charges1.getPrint() != null && charges1.getPrint().equalsIgnoreCase("on") && !charges1.getChargeCodeDesc().equalsIgnoreCase("OCNFRT")) {
                    if ("M".equalsIgnoreCase(charges1.getChargeFlag()) || "CH".equals(charges1.getChargeFlag())) {
                        if (spotRate) {
                            oceanFreightRateTotal = oceanFreightRateTotal + (null != charges1.getSpotRateMarkUp() ? charges1.getSpotRateMarkUp() : 0d) + charges1.getAdjestment();
                        } else {
                            oceanFreightRateTotal = oceanFreightRateTotal + charges1.getMarkUp() + charges1.getAdjestment();
                        }
                    } else {
                        if (spotRate) {
                            oceanFreightRateTotal = oceanFreightRateTotal + charges1.getAmount() + (null != charges1.getSpotRateMarkUp() ? charges1.getSpotRateMarkUp() : 0d) + charges1.getAdjestment();
                        } else {
                            oceanFreightRateTotal = oceanFreightRateTotal + charges1.getAmount() + charges1.getMarkUp() + charges1.getAdjestment();
                        }
                    }
                }
            }
        }
        OFR[k] = oceanFreightRateTotal;
        //charges details
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        int u = 0;
        double oceanFreightRateTotal1 = 0.0;
        List fclRatesListForPrint = (List) chargesDAO.getChargesforQuotation1(quotation.getQuoteId());
        for (Object fclRatesListForPrint1 : fclRatesListForPrint) {
            Charges charges = (Charges) fclRatesListForPrint1;
            if (charges.getUnitType() != null && !charges.getUnitType().equals("") && !charges.getUnitType().equals("0.00")) {
                GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(charges.getUnitType()));
                if (genericCode != null) {
                    charges.setUnitName(genericCode.getCodedesc());
                }
            }
        }
//        List fclRates = quotationBC.consolidateRates(fclRatesListForPrint, messageResources);
        PdfPTable chargesTable = makeTable(5);
        chargesTable.setWidthPercentage(100);
        chargesTable.setWidths(new float[]{15, 22, 30, 8, 15});

        String temp = "";
        Double GrandTotal = 0.0;
        Double subTotal = 0.0;
        int m = 0;
        int totalSize = 0;
        boolean printCharges = false;
        boolean printGage = false;
        boolean printSplEqp = true;
        boolean hasGaugeComment = false;
        String comment = "";
        GenericCode gc = new GenericCode();
        String specialEqu = "";
        String unitType = "";

        for (Iterator iter = fclRates1.iterator(); iter.hasNext();) {
            Charges charges = (Charges) iter.next();
            if (null == charges.getSpecialEquipmentUnit()) {
                charges.setSpecialEquipmentUnit("");
            }
            if (CommonUtils.isNotEmpty(charges.getSpecialEquipment())) {
//                gc = genericCodeDAO.findByCodeDesc(charges.getSpecialEquipment(),new Integer(41));
                specialEqu = charges.getSpecialEquipment();
            } else {
                specialEqu = "";
            }

            GenericCode genericCode = genericCodeDAO.findById(null != charges.getUnitType() && !"".equals(charges.getUnitType()) ? Integer.parseInt(charges.getUnitType()) : 0);
            if (null != genericCode && null != genericCode.getCodedesc()) {
                unitType = genericCode.getCodedesc().substring(2, genericCode.getCodedesc().length());
            }
            if (charges.getPrint() != null && charges.getPrint().equalsIgnoreCase("on") && (!charges.getChargeCodeDesc().equalsIgnoreCase("OCNFRT") && !charges.getChargeCodeDesc().equalsIgnoreCase("OFIMP"))) {
                if (spotRate) {
                    GrandTotal = GrandTotal + charges.getAmount() + (null != charges.getSpotRateMarkUp() ? charges.getSpotRateMarkUp() : 0d) + charges.getAdjestment();
                } else {
                    GrandTotal = GrandTotal + charges.getAmount() + charges.getMarkUp() + charges.getAdjestment();
                }
            } else {
                if (temp.equalsIgnoreCase(charges.getUnitType() + "-" + charges.getSpecialEquipmentUnit() + "-" + charges.getStandardCharge())) {
                    printCharges = true;
                    Double amnt = 0.0;
                    if (charges.getChargeFlag() != null && ("M".equals(charges.getChargeFlag()) || "CH".equals(charges.getChargeFlag()))) {
                        amnt = charges.getMarkUp() + charges.getAdjestment();
                    } else {
                        if (spotRate) {
                            amnt = charges.getAmount() + (null != charges.getSpotRateMarkUp() ? charges.getSpotRateMarkUp() : 0d) + charges.getAdjestment();
                        } else {
                            amnt = charges.getAmount() + charges.getMarkUp() + charges.getAdjestment();
                        }
                    }
                    m++;
                    if (printGage) {
                        cell = makeCellLeftWithNoBorderColoredGreen(OUT_OF_GAUGE);
                        printGage = false;
                    } else if (hasGaugeComment) {
                        hasGaugeComment = false;
                        cell = makeCellLeftNoBorder(comment);
                    } else {
                        cell = makeCellLeftNoBorderBold("");
                    }
                    cell.setNoWrap(true);
                    cell.setBorderWidthLeft(0.6f);
                    chargesTable.addCell(cell);
                    cell = makeCellLeftNoBorderBold("");
                    chargesTable.addCell(cell);
                    String chargeCodeDesc = "";
                    if (charges.getChgCode() != null) {
                        chargeCodeDesc = charges.getChgCode();
                        if (chargeCodeDesc.equalsIgnoreCase("Intermodal F/S") || chargeCodeDesc.equalsIgnoreCase("Intermodal Ramp")) {
                            chargeCodeDesc = "INTERMODAL RAMP";
                        } else {
                            if (chargeCodeDesc.equals("INSURANCE") && charges.getChargeFlag().equals("I")) {
                                chargeCodeDesc = charges.getChgCode();
                                chargeCodeDesc += " (Value of Goods $" + numformat.format(quote.getCostofgoods()) + ")";
                            } else {
                                chargeCodeDesc = charges.getChgCode();
                            }
                        }
                    }
                    chargesTable.addCell(makeCellLeftNoBorderWithDots(chargeCodeDesc));
                    chargesTable.addCell(makeCellLeftNoBorderBold(charges.getCurrecny()));
                    if (charges.getChgCode().equalsIgnoreCase(messageResources.getMessage("OceanFreightPopUp"))) {
                        amnt = amnt + OFR[u];
                        u++;
                        cell = makeCellRightNoBorderBold(number.format(amnt));
                        cell.setBorderWidthRight(0.6f);
                        chargesTable.addCell(cell);
                        subTotal = subTotal + amnt;
                        oceanFreightRateTotal1 = 0.0;
                    } else {
                        cell = makeCellRightNoBorderBold(number.format(amnt));
                        cell.setBorderWidthRight(0.6f);
                        chargesTable.addCell(cell);
                        if (charges.getChargeFlag() != null && ("M".equals(charges.getChargeFlag()) || "CH".equals(charges.getChargeFlag()))) {
                            subTotal = subTotal + charges.getMarkUp() + charges.getAdjestment();
                        } else {
                            if (spotRate) {
                                subTotal = subTotal + charges.getAmount() + (null != charges.getSpotRateMarkUp() ? charges.getSpotRateMarkUp() : 0d) + charges.getAdjestment();
                            } else {
                                subTotal = subTotal + charges.getAmount() + charges.getMarkUp() + charges.getAdjestment();
                            }
                        }
                    }
                    temp = charges.getUnitType() + "-" + charges.getSpecialEquipmentUnit() + "-" + charges.getStandardCharge();
                    if (charges.getChargeFlag() != null && ("M".equals(charges.getChargeFlag()) || "CH".equals(charges.getChargeFlag()))) {
                        GrandTotal = GrandTotal + charges.getMarkUp() + charges.getAdjestment();
                    } else {
                        if (spotRate) {
                            GrandTotal = GrandTotal + charges.getAmount() + (null != charges.getSpotRateMarkUp() ? charges.getSpotRateMarkUp() : 0d) + charges.getAdjestment();
                        } else {
                            GrandTotal = GrandTotal + charges.getAmount() + charges.getMarkUp() + charges.getAdjestment();
                        }
                    }
                }
                
                
                else {
                    count = 0;
                    if (m == 0) {
                        m++;
                    } else {
                        if (printGage) {
                            cell = makeCellLeftWithNoBorderColoredGreen(OUT_OF_GAUGE);
                            printGage = false;
                            cell.setNoWrap(true);
                            cell.setBorderWidthLeft(0.6f);
                            chargesTable.addCell(cell);
                            cell = makeCellLeftNoBorderBold("");
                            chargesTable.addCell(cell);
                            cell = makeCellLeftNoBorderBold("");
                            chargesTable.addCell(cell);
                            cell = makeCellLeftNoBorderBold("");
                            chargesTable.addCell(cell);
                            cell = makeCellLeftNoBorderBold("");
                            cell.setBorderWidthRight(0.6f);
                            chargesTable.addCell(cell);
                        }
                        if (hasGaugeComment) {
                            hasGaugeComment = false;
                            cell = makeCellLeftNoBorder(comment);
                            cell.setNoWrap(true);
                            cell.setBorderWidthLeft(0.6f);
                            chargesTable.addCell(cell);
                            cell = makeCellLeftNoBorderBold("");
                            chargesTable.addCell(cell);
                            cell = makeCellLeftNoBorderBold("");
                            chargesTable.addCell(cell);
                            cell = makeCellLeftNoBorderBold("");
                            chargesTable.addCell(cell);
                            cell = makeCellLeftNoBorderBold("");
                            cell.setBorderWidthRight(0.6f);
                            chargesTable.addCell(cell);

                        }
                        if (m > 0 && !subTotal.equals(0.0)) {
                            cell = makeCellRightWithBoldFont("Total............");
                            cell.setBorderWidthLeft(0.6f);
                            cell.setColspan(4);
                            chargesTable.addCell(cell);
                            cell = makeCellRightWithBoldFont(number.format(subTotal) + "(USD)");
                            cell.setColspan(1);
                            cell.setBorderWidthRight(0.6f);
                            chargesTable.addCell(cell);
                            m = 1;
                        } else {
                            cell = makeCellRightWithBoldFont("");
                            cell.setBorderWidthLeft(0.6f);
                            cell.setBorderWidthRight(0.6f);
                            cell.setColspan(5);
                            chargesTable.addCell(cell);
                        }
                        cell = makeCellRightWithBoldFont("");
                        cell.setBorderWidthBottom(0.1f);
                        cell.setBorderWidthLeft(0.5f);
                        cell.setBorderWidthRight(0.5f);
                        cell.setColspan(5);
                        cell.setBorderColorBottom(Color.LIGHT_GRAY);
                        chargesTable.addCell(cell);
                        subTotal = 0.0;
                    }
                    totalSize++;
                    printCharges = true;
                    //GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(charges.getUnitType()));
                    Paragraph quoteParagraph = new Paragraph();
                    String container = charges.getNumber();
                    Double amnt = 0.0;
                    if (genericCode != null) {
                        container += " X " + (genericCode.getCodedesc() != null ? genericCode.getCodedesc().substring(2, genericCode.getCodedesc().length()) : "");
                        quoteParagraph.add(new Chunk(container, blackFontBold));
                        printSplEqp = true;
                        cell = makeCell(quoteParagraph, Element.ALIGN_LEFT);
                        cell.setBorderWidthLeft(0.6f);
                        cell.setLeading(0.7f, 0.7f);
                        chargesTable.addCell(cell);
                        quoteParagraph = new Paragraph();
                        if (CommonUtils.isNotEmpty(charges.getOutOfGauge()) && charges.getOutOfGauge().equals("Y")) {
                            if (CommonUtils.isNotEmpty(charges.getOutOfGaugeComment())) {
                                hasGaugeComment = true;
                                comment = charges.getOutOfGaugeComment();
                            }
                            printGage = true;
                        }
                        if (CommonUtils.isNotEmpty(charges.getSpecialEquipment())) {
                            cell = makeCellLeftWithNoBorderColoredGreen(specialEqu);
                            cell.setNoWrap(true);
                            printSplEqp = false;
                        } else {
                            cell = makeCellRightWithBoldFont("");
                        }
                    }
                    if (charges.getChargeFlag() != null && ("M".equals(charges.getChargeFlag()) || "CH".equals(charges.getChargeFlag()))) {
                        amnt = charges.getMarkUp() + charges.getAdjestment();
                    } else {
                        if (spotRate) {
                            amnt = charges.getAmount() + (null != charges.getSpotRateMarkUp() ? charges.getSpotRateMarkUp() : 0d) + charges.getAdjestment();
                        } else {
                            amnt = charges.getAmount() + charges.getMarkUp() + charges.getAdjestment();
                        }
                    }
                    //cell = makeCellLeftNoBorderBold(container);
                    chargesTable.addCell(cell);
                    String chargeCodeDesc = "";
                    if (charges.getChgCode() != null) {
                        chargeCodeDesc = charges.getChgCode();
                        if (chargeCodeDesc.equalsIgnoreCase("Intermodal F/S") || chargeCodeDesc.equalsIgnoreCase("Intermodal Ramp")) {
                            chargeCodeDesc = "INTERMODAL RAMP";
                        } else {
                            chargeCodeDesc = charges.getChgCode();
                        }
                    }
                    chargesTable.addCell(makeCellLeftNoBorderWithDots(chargeCodeDesc));
                    chargesTable.addCell(makeCellLeftNoBorderBold(charges.getCurrecny()));

                    if (charges.getChargeCodeDesc().equalsIgnoreCase(messageResources.getMessage("oceanfreightcharge"))) {
                        amnt = amnt + OFR[u];
                        u++;
                        subTotal = subTotal + amnt;
                        cell = makeCellRightNoBorderBold(number.format(amnt));
                        cell.setBorderWidthRight(0.6f);
                        chargesTable.addCell(cell);
                        oceanFreightRateTotal1 = 0.0;
                    } else if (charges.getChargeCodeDesc().equalsIgnoreCase(messageResources.getMessage("oceanfreightImpcharge"))) {
                        amnt = amnt + OFR[u];
                        u++;
                        subTotal = subTotal + amnt;
                        cell = makeCellRightNoBorderBold(number.format(amnt));
                        cell.setBorderWidthRight(0.6f);
                        chargesTable.addCell(cell);
                        oceanFreightRateTotal1 = 0.0;
                    } else {
                        cell = makeCellRightNoBorderBold(number.format(amnt));
                        cell.setBorderWidthRight(0.6f);
                        chargesTable.addCell(cell);
                        if (charges.getChargeFlag() != null && ("M".equals(charges.getChargeFlag()) || "CH".equals(charges.getChargeFlag()))) {
                            subTotal = subTotal + charges.getMarkUp() + charges.getAdjestment();
                        } else {
                            if (spotRate) {
                                subTotal = subTotal + charges.getAmount() + (null != charges.getSpotRateMarkUp() ? charges.getSpotRateMarkUp() : 0d) + charges.getAdjestment();
                            } else {
                                subTotal = subTotal + charges.getAmount() + charges.getMarkUp() + charges.getAdjestment();
                            }
                        }
                    }
                    temp = charges.getUnitType() + "-" + charges.getSpecialEquipmentUnit() + "-" + charges.getStandardCharge();
                    if (charges.getChargeFlag() != null && ("M".equals(charges.getChargeFlag()) || "CH".equals(charges.getChargeFlag()))) {
                        GrandTotal = GrandTotal + charges.getMarkUp() + charges.getAdjestment();
                    } else {
                        if (spotRate) {
                            GrandTotal = GrandTotal + charges.getAmount() + (null != charges.getSpotRateMarkUp() ? charges.getSpotRateMarkUp() : 0d) + charges.getAdjestment();
                        } else {
                            GrandTotal = GrandTotal + charges.getAmount() + charges.getMarkUp() + charges.getAdjestment();
                        }
                    }
                    count++;
                }
            }
        }
        if (printGage) {
            cell = makeCellLeftWithNoBorderColoredGreen(OUT_OF_GAUGE);
            printGage = false;
            cell.setNoWrap(true);
            cell.setBorderWidthLeft(0.6f);
            chargesTable.addCell(cell);
            cell = makeCellLeftNoBorderBold("");
            chargesTable.addCell(cell);
            cell = makeCellLeftNoBorderBold("");
            chargesTable.addCell(cell);
            cell = makeCellLeftNoBorderBold("");
            chargesTable.addCell(cell);
            cell = makeCellLeftNoBorderBold("");
            cell.setBorderWidthRight(0.6f);
            chargesTable.addCell(cell);
        }
        if (hasGaugeComment) {
            hasGaugeComment = false;
            cell = makeCellLeftNoBorder(comment);
            cell.setNoWrap(true);
            cell.setBorderWidthLeft(0.6f);
            chargesTable.addCell(cell);
            cell = makeCellLeftNoBorderBold("");
            chargesTable.addCell(cell);
            cell = makeCellLeftNoBorderBold("");
            chargesTable.addCell(cell);
            cell = makeCellLeftNoBorderBold("");
            chargesTable.addCell(cell);
            cell = makeCellLeftNoBorderBold("");
            cell.setBorderWidthRight(0.6f);
            chargesTable.addCell(cell);
        }
        if (m > 0 && !subTotal.equals(0.0)) {
            cell = makeCellRightWithBoldFont("Total............");
            cell.setBorderWidthLeft(0.6f);
            cell.setColspan(4);
            chargesTable.addCell(cell);
            cell = makeCellRightWithBoldFont(number.format(subTotal) + "(USD)");
            cell.setBorderWidthRight(0.6f);
            cell.setColspan(1);
            chargesTable.addCell(cell);
            cell = makeCellRightWithBoldFont("");
            cell.setBorderWidthBottom(0.1f);
            cell.setBorderWidthLeft(0.5f);
            cell.setBorderWidthRight(0.5f);
            cell.setColspan(5);
            cell.setBorderColorBottom(Color.LIGHT_GRAY);
            chargesTable.addCell(cell);
            m = 0;
        } else {
            cell = makeCellRightWithBoldFont("");
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthRight(0.6f);
            cell.setColspan(5);
            chargesTable.addCell(cell);
            cell = makeCellRightWithBoldFont("");
            cell.setBorderWidthBottom(0.1f);
            cell.setBorderWidthLeft(0.5f);
            cell.setBorderWidthRight(0.5f);
            cell.setColspan(5);
            cell.setBorderColorBottom(Color.LIGHT_GRAY);
            chargesTable.addCell(cell);
        }

        // other charges details
        PdfPTable otherChargeDetails = makeTable(4);
        otherChargeDetails.setWidthPercentage(100);
        otherChargeDetails.setWidths(new float[]{37, 30, 8, 15});
        CostBean costb = new CostBean();
        String flag = "off";
        Double amt = 0.00;
        String otherCharge = "";
        boolean otherCharges = false;
        boolean printGage1 = true;
        Double totalOfOthercharges = 0.00;
        for (CostBean otherChargesLIst1 : otherChargesLIst) {
            costb = (CostBean) otherChargesLIst1;
            if (costb.getOtherprint() != null && !costb.getOtherprint().trim().equals("on") && (!costb.getRetail().equals("0.00") || !costb.getMarkup().equals("0.00"))) {
                if (flag.equals("off")) {
                    otherCharge = ReportConstants.OTHER_CHARGES;
                    Paragraph quoteParagraph1 = new Paragraph();
                    quoteParagraph1.add(new Chunk(otherCharge, blackFontBold));
                    cell = makeCell(quoteParagraph1, Element.ALIGN_LEFT);
                    cell.setBorderWidthLeft(0.6f);
                    otherChargeDetails.addCell(cell);
                    flag = "on";
                } else {
                    if (CommonUtils.isNotEmpty(quote.getOutofgage()) && quote.getOutofgage().equals("Y") && printGage1) {
                        cell = makeCellLeftWithNoBorderColoredGreen("                              " + OUT_OF_GAUGE);
                        printGage1 = false;
                    } else {
                        cell = makeCellLeftNoBorderBold("");
                    }
                    //cell =makeCellLeftNoBorderBold("");
                    cell.setBorderWidthLeft(0.6f);
                    otherChargeDetails.addCell(cell);
                }
                otherChargeDetails.addCell(makeCellLeftNoBorderWithDots(costb.getChargecode()));
                otherChargeDetails.addCell(makeCellLeftNoBorderBold(costb.getCurrency()));

                if (costb.getChargeFlag() != null && ("M".equals(costb.getChargeFlag()) || "CH".equals(costb.getChargeFlag()))) {
                    amt = (costb.getMarkup() != null ? Double.parseDouble(costb.getMarkup().replaceAll(",", "")) : 0.00);
                } else {
                    amt = (costb.getRetail() != null ? Double.parseDouble(costb.getRetail().replaceAll(",", "")) : 0.00) + (costb.getMarkup() != null ? Double.parseDouble(costb.getMarkup().replaceAll(",", "")) : 0.00);
                }
                GrandTotal = GrandTotal + amt;
                totalOfOthercharges = totalOfOthercharges + amt;
                cell = makeCellRightNoBorderBold(number.format(amt));
                cell.setBorderWidthRight(0.6f);
                otherChargeDetails.addCell(cell);
                otherCharges = true;
            }
        }
        if (null != otherChargesLIst && !totalOfOthercharges.equals(0.0)) {
            cell = makeCellRightWithBoldFont("Total............");
            cell.setBorderWidthLeft(0.6f);
            cell.setColspan(3);
            otherChargeDetails.addCell(cell);
            cell = makeCellRightWithBoldFont(number.format(totalOfOthercharges) + "(USD)");
            cell.setBorderWidthRight(0.6f);
            cell.setColspan(1);
            otherChargeDetails.addCell(cell);
            totalSize++;
        } else {
            cell = makeCellRightWithBoldFont("");
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthRight(0.6f);
            cell.setColspan(4);
            otherChargeDetails.addCell(cell);
        }
        PdfPCell cell2 = makeCellRightWithBoldFont("");
        cell2.setColspan(4);
        cell2.setBorderWidthLeft(0.6f);
        cell2.setBorderWidthRight(0.6f);
        cell2.setBorderWidthBottom(0.4f);
        otherChargeDetails.addCell(cell2);
        //hazmat details
        PdfPTable hazmatDetails = makeTable(6);
        hazmatDetails.setWidthPercentage(100);

        if (quotation.getHazmat() != null && quotation.getHazmat().equalsIgnoreCase("Y")) {
            HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();
            List hazMatList1 = new ArrayList();
            hazMatList1 = hazmatMaterialDAO.findbydoctypeid1(QuotationConstants.QUOTE, quotation.getQuoteId());
            if (!hazMatList1.isEmpty()) {
                cell = makeCellLeftNoBorder("HAZMAT Details");
                cell.setBorderWidthLeft(0.6f);
                cell.setBorderWidthRight(0.6f);
                cell.setColspan(6);
                hazmatDetails.addCell(cell);
                cell = makeCellleftwithUnderLineSmallFont("UN Number");
                cell.setBorderWidthLeft(0.6f);
                hazmatDetails.addCell(cell);
                hazmatDetails.addCell(makeCellleftwithUnderLineSmallFont("Proper Shipping Name"));
                hazmatDetails.addCell(makeCellleftwithUnderLineSmallFont("Technical Name"));
                hazmatDetails.addCell(makeCellleftwithUnderLineSmallFont("IMO Class Code(Primary)"));
                hazmatDetails.addCell(makeCellleftwithUnderLineSmallFont("PKG Group"));
                cell = makeCellleftwithUnderLineSmallFont("Gross Weight");
                cell.setBorderWidthRight(0.6f);
                hazmatDetails.addCell(cell);
                for (Iterator iter = hazMatList1.iterator(); iter.hasNext();) {
                    HazmatMaterial hazmatMaterial = (HazmatMaterial) iter.next();
                    String pckGroup = (null == hazmatMaterial.getPackingGroupCode() || hazmatMaterial.getPackingGroupCode().equalsIgnoreCase("0")) ? " " : hazmatMaterial.getPackingGroupCode();
                    String grossWeight = (null != hazmatMaterial.getGrossWeight()) ? numformat.format(hazmatMaterial.getGrossWeight()) + "  " : "0.00  ";
                    grossWeight += (null != hazmatMaterial.getGrossWeightUMO()) ? hazmatMaterial.getGrossWeightUMO() : " ";
                    cell = makeCellLeftNoBorderBold(hazmatMaterial.getUnNumber());
                    cell.setBorderWidthLeft(0.6f);
                    hazmatDetails.addCell(cell);
                    hazmatDetails.addCell(makeCellLeftNoBorderBold(hazmatMaterial.getPropShipingNumber()));
                    hazmatDetails.addCell(makeCellLeftNoBorderBold(hazmatMaterial.getTechnicalName()));
                    hazmatDetails.addCell(makeCellLeftNoBorderBold(hazmatMaterial.getImoClssCode()));
                    hazmatDetails.addCell(makeCellLeftNoBorderBold(pckGroup));
                    cell = makeCellLeftNoBorderBold(grossWeight);
                    cell.setBorderWidthRight(0.6f);
                    hazmatDetails.addCell(cell);
                }

                cell = new PdfPCell();
                cell.setBorderWidthBottom(0.6f);
                cell.setBorderWidthTop(0.0f);
                cell.setBorderWidthLeft(0.6f);
                cell.setBorderWidthRight(0.6f);
                cell.setColspan(6);
                hazmatDetails.addCell(cell);
            }
        }

        //created by information
        User userDetails = getPleaseContactDetails(quote.getQuoteBy() != null ? quote.getQuoteBy() : "");
        PdfPTable contactUsTable = makeTable(1);
        contactUsTable.setWidthPercentage(100);
        StringBuilder buffer = new StringBuilder("");
        buffer.append("Please Send Inquiries and Docs to ");
        String email = quotationBC.getEmailToDisplayINReport(QuotationConstants.TYPEOFLOAD, StringFormatter.getIssuingTerminal(quote.getIssuingTerminal()),
                StringFormatter.orgDestStringFormatter(quote.getFinaldestination()));
        if (quote.getDocsInquiries() != null && quote.getDocsInquiries().equals("on")) {
            buffer.append(userDetails.getEmail() != null ? userDetails.getEmail() : "");
            if (CommonUtils.isNotEmpty(userDetails.getFirstName())) {
                buffer.append("(");
                buffer.append(userDetails.getFirstName());
                if (CommonUtils.isNotEmpty(userDetails.getLastName())) {
                    buffer.append(" ");
                    buffer.append(userDetails.getLastName());
                }
                buffer.append(")");
            }
        } else if (email != null) {
            buffer.append(email);
        }
        buffer.append(".");
        contactUsTable.addCell(makeCellLeftNoBorderBold(buffer.toString()));
        cell = makeCellleftNoBorder("");
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.addElement(contactUsTable);
        PdfPTable createdByDetails = makeTable(1);
        createdByDetails.setWidthPercentage(100);

        createdByDetails.addCell(cell);

        //disclaimer
        List disclaimerList = new ArrayList();
        //  boolean importFlag = (null != quote && null != quote.getFileType() && quote.getFileType().equalsIgnoreCase("I"));
        if (importFlag) {
            disclaimerList = genericCodeDAO.getAllCommentCodesForImportReports();
        } else {
            disclaimerList = genericCodeDAO.getAllCommentCodesForReports();
        }
        PdfPTable disclaimerTable = makeTable(1);
        disclaimerTable.setWidthPercentage(100);
        cell = makeCellLeftNoBorder("*  *  *  *  *  *  *  *  *  * "
                + "*  *  *  *  *  *  *  *  * I M P O R T A N T   D I S C L O S U R E S * *  *  *  *  *  "
                + "*  *  *  *  *  *  *  *  *  *  *  *  * ");
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        disclaimerTable.addCell(cell);
        QuoteDwrBC quoteDwrBC = new QuoteDwrBC();
        String destin = "";
        String checkRegion = "false";
        if (quote.getDestination_port() != null && !"".equals(quote.getDestination_port())) {
            destin = quote.getDestination_port();
            checkRegion = quoteDwrBC.checkForTheRegion(destin);
        }
        cell = makeCellLeftNoBorder(getOriginRemarks(quote));
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        disclaimerTable.addCell(cell);
        if (checkRegion != null && "true".equals(checkRegion)) {
            if (quote.getDeductFfcomm() != null && "N".equals(quote.getDeductFfcomm())) {
                disclaimerList.remove(0);
            }
            if (quote.getDeductFfcomm() != null && "N".equals(quote.getDeductFfcomm())) {
                for (int i = 0; i < disclaimerList.size(); i++) {
                    if (i == 4) {
                        break;
                    }
                    cell = makeCellLeftNoBorder(String.valueOf(disclaimerList.get(i)));
                    cell.setBorderWidthLeft(0.6f);
                    cell.setBorderWidthRight(0.6f);
                    disclaimerTable.addCell(cell);

                }
            } else {
                for (int i = 0; i < disclaimerList.size(); i++) {
                    if (i == 5) {
                        break;
                    }
                    if (i == 0) {
                        cell = makeCellLeftWithNoBorderColored(disclaimerList.get(i).toString());
                    } else {
                        cell = makeCellLeftNoBorder(String.valueOf(disclaimerList.get(i)));
                    }
                    cell.setBorderWidthLeft(0.6f);
                    cell.setBorderWidthRight(0.6f);
                    disclaimerTable.addCell(cell);

                }
            }
        } else {
            for (int i = 0; i < disclaimerList.size(); i++) {
                if (i == 5) {
                    break;
                }
                if (i == 0) {
                    cell = makeCellLeftWithNoBorderColored(disclaimerList.get(i).toString());
                } else {
                    cell = makeCellLeftNoBorder(String.valueOf(disclaimerList.get(i)));
                }
                cell.setBorderWidthLeft(0.6f);
                cell.setBorderWidthRight(0.6f);
                disclaimerTable.addCell(cell);

            }
        }
        cell = makeCellLeftNoBorder("");
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        disclaimerTable.addCell(cell);
        if (checkRegion != null && "true".equals(checkRegion)) {
            if (quote.getDeductFfcomm() != null && "N".equals(quote.getDeductFfcomm())) {
                for (int i = 4; i < disclaimerList.size(); i++) {
                    cell = makeCellLeftNoBorder("-" + String.valueOf(disclaimerList.get(i)));
                    cell.setBorderWidthLeft(0.6f);
                    cell.setBorderWidthRight(0.6f);
                    disclaimerTable.addCell(cell);
                }
            } else {
                for (int i = 5; i < disclaimerList.size(); i++) {
                    cell = makeCellLeftNoBorder("-" + String.valueOf(disclaimerList.get(i)));
                    cell.setBorderWidthLeft(0.6f);
                    cell.setBorderWidthRight(0.6f);
                    disclaimerTable.addCell(cell);
                }
            }
        } else {
            for (int i = 5; i < disclaimerList.size(); i++) {
                cell = makeCellLeftNoBorder("-" + String.valueOf(disclaimerList.get(i)));
                cell.setBorderWidthLeft(0.6f);
                cell.setBorderWidthRight(0.6f);
                disclaimerTable.addCell(cell);
            }
        }
        cell = makeCellLeftNoBorder("");
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        disclaimerTable.addCell(cell);
        document.add(DescriptionRemarksTable);
        document.add(portsRemarksTable1);
        document.add(regionRemarksTable);
        document.add(ChargeHeading);
        document.add(chargesTable);
        document.add(otherChargeDetails);
        document.add(hazmatDetails);
        document.add(createdByDetails);
        if (quotation.getRatesNonRates() != null && quotation.getRatesNonRates().equals("R")) {
            document.add(disclaimerTable);
        } else if (quotation.getRatesNonRates() != null && quotation.getRatesNonRates().equals("N")
                && quotation.getImportantDisclosures() != null && quotation.getImportantDisclosures().equals("on")) {
            document.add(disclaimerTable);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            //---------------
            //this for print page number at the bottom in the format x of y
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            String text = "Page " + writer.getPageNumber() + " of ";
            float textBase = document.bottom() - (document.bottomMargin() - 30);
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
            log.info("QuotesReportPdfCreator onEndPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    public String getCityStateName(String name) throws Exception {
        String finalName = "";
        if (null != name && !name.equalsIgnoreCase("")) {
            String subName = "";
            subName = StringFormatter.orgDestStringFormatter(name);
            if (CommonUtils.isNotEmpty(subName)) {
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
            } else {
                finalName = name;
            }
        }
        return finalName;
    }

    public String getPackageTypeDesc(String packType, int id) throws Exception {
        if (!packType.equalsIgnoreCase("") && !packType.equalsIgnoreCase("0")) {
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            GenericCode genericCode = genericCodeDAO.findByCodeName(packType, id);
            return genericCode.getCodedesc();
        } else {
            return "";
        }
    }

    public void destroy() {
        document.close();
    }

    private User getPleaseContactDetails(String loginName) throws Exception {
        UserDAO userDAO = new UserDAO();
        return userDAO.findUserName(loginName);

    }

    public String createReport(List<QuotationDTO> QtFieldsList, List<CostBean> otherChargesLIst, String comments,
            String ratechangeAlert, String disclaimer, Quotation quotation, String grandTotal, String fileName,
            String contextPath, MessageResources messageResources, String terminalEmail, String terminalName,
            HttpServletRequest request) throws Exception {
        messageResource = messageResources;
        setQuote(quotation);
        contextPath1 = contextPath;
        this.initialize(fileName, request, quotation);
        this.createBody(QtFieldsList, otherChargesLIst, comments, ratechangeAlert, disclaimer, quotation,
                grandTotal, messageResources, terminalEmail, terminalName, request);
        this.destroy();
        return "fileName";
    }

    public Quotation getQuote() {
        return quote;
    }

    public void setQuote(Quotation quote) {
        this.quote = quote;
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

    public String getOriginRemarks(Quotation quote) throws Exception {
        String destCode = quote.getOrigin_terminal();
        if (null != destCode && !destCode.equals("")) {
            if (destCode.lastIndexOf("(") != -1 && destCode.lastIndexOf(")") != -1) {
                String code = "";
                code = destCode.substring(destCode.lastIndexOf("(") + 1, destCode.lastIndexOf(")"));
                UnLocationDAO unLocationDAO = new UnLocationDAO();
                return unLocationDAO.getOriginRemarks(code);
            }
        }
        return "";
    }
}
