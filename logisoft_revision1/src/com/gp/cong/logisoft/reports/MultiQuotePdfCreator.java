package com.gp.cong.logisoft.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.bc.fcl.QuotationConstants;
import com.gp.cong.logisoft.bc.fcl.QuoteDwrBC;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.MultiChargesOrderBean;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.domain.MultiQuote;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.gp.cvst.logisoft.hibernate.dao.MultiQuoteChargesDao;
import com.gp.cvst.logisoft.hibernate.dao.MultiQuoteDao;
import com.lowagie.text.Chunk;
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
 *
 * @author NambuRajasekar.M
 */
public class MultiQuotePdfCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(MultiQuotePdfCreator.class);
    Quotation quotation;
    HttpServletRequest request;
    Document document = new Document(PageSize.A4);
    PdfWriter pdfWriter = null;
    protected PdfTemplate total;
    protected BaseFont helv;
    private static final HashMap<String, Quotation> hashMap = new HashMap<String, Quotation>();
    private static String contextPath;

    public MultiQuotePdfCreator(HttpServletRequest request, Quotation quotation) {
        this.request = request;
        this.quotation = quotation;
    }

    public String createReport(Quotation quotation, String quoteNo, String filePath, String fileNo, String realPath, MessageResources messageResources, HttpServletRequest request) throws Exception {
        contextPath = realPath;
        this.initialize(filePath, request, quotation);
        this.createBody(messageResources, request);
        this.destroy();
        return "fileName";
    }

    public void initialize(String fileName, HttpServletRequest request, Quotation quotation) throws FileNotFoundException, DocumentException, Exception {
        document.setMargins(4, 4, 4, 4);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        pdfWriter.setPageEvent(new MultiQuotePdfCreator(request, quotation));
        String companyName = null != LoadLogisoftProperties.getProperty("application.fclBl.edi.partnerName")
                ? LoadLogisoftProperties.getProperty("application.fclBl.edi.partnerName").toUpperCase() : "OTI cargo Inc";
        HeaderFooter footer = new HeaderFooter(
                new Phrase("Thank you for choosing " + companyName + " for all your shipping needs", new Font(Font.HELVETICA, 10, Font.ITALIC, Color.BLACK)), false);
        footer.setBorder(Rectangle.NO_BORDER);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.setFooter(footer);
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
            log.info("MultiQuotePdfCreator onOpenDocument failed on " + new Date(), e);
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
            log.info("MultiQuotePdfCreator onCloseDocument failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        try {
            onStartMultiQuotePage(writer, document);
        } catch (Exception e) {
            log.info("MultiQuotePdfCreator onStartPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    public void onStartMultiQuotePage(PdfWriter writer, Document document) throws Exception {
        String path = LoadLogisoftProperties.getProperty("application.image.logo");
        PdfPCell cell = new PdfPCell();
        PdfPCell celL = new PdfPCell();
        PdfPTable table = new PdfPTable(1);

        if (path.equalsIgnoreCase("/img/companyLogo/EconocaribePrintLogo.gif")) {
            Image img = Image.getInstance(contextPath + path);
            table.setWidthPercentage(100);
            img.scalePercent(60);
            img.scaleAbsoluteWidth(556);
            celL.addElement(new Chunk(img, -2, -25));
            celL.setBorder(0);
            celL.setHorizontalAlignment(Element.ALIGN_LEFT);
            celL.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(celL);
            DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
            Date currentDate = new Date();
            cell = makeCellRightNoBorderWhiteFont("Date Printed : " + df7.format(currentDate));
            cell.setPaddingTop(10f);
        } else {
            Image img = Image.getInstance(contextPath + path);
            table.setWidthPercentage(100);
            img.scalePercent(60);
            img.scaleAbsoluteWidth(200);
            celL.addElement(new Chunk(img, 180, -25));
            celL.setBorder(0);
            celL.setHorizontalAlignment(Element.ALIGN_CENTER);
            celL.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(celL);
            DateFormat df7 = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            Date currentDate = new Date();
            cell = makeCellRightNoBorder("Date Printed : " + df7.format(currentDate));
            cell.setPaddingTop(10f);
        }
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
//        DateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");

        String quoteDate = "Date Created : " + "" + df7.format(quotation.getQuoteDate());
        Phrase headingPhrase1 = new Phrase(quoteDate, blackFontForAR);
        PdfPCell headingCell1 = new PdfPCell(headingPhrase1);
        headingCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        headingCell1.setVerticalAlignment(Element.ALIGN_CENTER);
        headingCell1.setPaddingTop(2);
        headingCell1.setPaddingBottom(0);
        headingCell1.setBorder(0);
        headingCell1.setBackgroundColor(Color.LIGHT_GRAY);
        bookTable.addCell(headingCell1);

        String heading = "FCL Rate Quote" + " 04-" + String.valueOf(quotation.getFileNo());
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

    public void createBody(MessageResources messageResources, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        PdfPCell cell = null;
        PdfPTable mainTable = makeTable(2);
        mainTable.setWidthPercentage(100f);
        mainTable.setWidths(new float[]{49f, 51f});

        PdfPTable toEmailTable = makeTable(4);
        toEmailTable.setWidthPercentage(100);
        toEmailTable.setTotalWidth(100f);
        toEmailTable.setWidths(new float[]{12, 35, 12, 35});

        cell = makeCell("ClientDetails", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthBottom(0.6f);
        toEmailTable.addCell(cell);

        cell = makeCell("UserDetails", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        toEmailTable.addCell(cell);

//1 Row
        cell = makeCellLeftNoBorder("To");
//        cell.setPaddingLeft(1.5f);
        toEmailTable.addCell(cell);
        cell = makeCellLeftNoBorderBold(": " + quotation.getContactname());
        toEmailTable.addCell(cell);
        cell = makeCellLeftNoBorder("From");
        cell.setBorderWidthLeft(0.6f);
        toEmailTable.addCell(cell);
        cell = makeCellLeftNoBorderBold(": " + user.getFirstName() + " " + user.getLastName());//userFrom
        toEmailTable.addCell(cell);
//2nd Row       
        cell = makeCellLeftNoBorder("Email");
//        cell.setPaddingLeft(1.5f);
        toEmailTable.addCell(cell);

        cell = makeCellLeftNoBorderBold(": " + quotation.getEmail1());
        toEmailTable.addCell(cell);
        cell = makeCellLeftNoBorder("Phone");
        cell.setBorderWidthLeft(0.6f);
        toEmailTable.addCell(cell);
        cell = makeCellLeftNoBorderBold(": " + user.getTelephone());//userFrom
        toEmailTable.addCell(cell);
//3rd Row       
        cell = makeCellLeftNoBorder("Phone");
        toEmailTable.addCell(cell);
        cell = makeCellLeftNoBorderBold(": " + quotation.getPhone());
        toEmailTable.addCell(cell);
        cell = makeCellLeftNoBorder("Fax");
        cell.setBorderWidthLeft(0.6f);
        toEmailTable.addCell(cell);
        cell = makeCellLeftNoBorderBold(": " + user.getFax());//userFrom
        toEmailTable.addCell(cell);
        //4th Row       
        cell = makeCellLeftNoBorder("Fax");
        toEmailTable.addCell(cell);
        cell = makeCellLeftNoBorderBold(": " + quotation.getFax());
        toEmailTable.addCell(cell);
        cell = makeCellLeftNoBorder("Email");
        cell.setBorderWidthLeft(0.6f);
        toEmailTable.addCell(cell);
        cell = makeCellLeftNoBorderBold(": " + user.getEmail());//userFrom
        toEmailTable.addCell(cell);
        //5th Row       
        cell = makeCellLeftNoBorder("Company");
        toEmailTable.addCell(cell);
        cell = makeCellLeftNoBorderBold(": " + quotation.getClientname());
        toEmailTable.addCell(cell);
        cell = createEmptyCell();//Empty cell
        cell.setBorderWidthLeft(0.6f);
        cell.setColspan(2);
        toEmailTable.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(4);
        cell.addElement(toEmailTable);
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.6f);
        mainTable.addCell(cell);

        //GetMultiQuote -- ORIGIN AND DESTINATION
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        List<MultiQuote> multiQuote = new MultiQuoteDao().findByPropertyAsc("quotation.quoteId", quotation.getQuoteId());

        for (MultiQuote mq : multiQuote) {
            PdfPTable chargesTable = makeTable(4);
            chargesTable.setWidthPercentage(100);
            chargesTable.setTotalWidth(100f);
            chargesTable.setWidths(new float[]{12, 35, 12, 35});
            cell = makeCell("SSL Name: " + mq.getCarrier(), Element.ALIGN_LEFT, headingFontSize8, 0, Color.decode("#A1D490"));
            cell.setColspan(2);
            cell.setBorderWidthBottom(0.6f);
            chargesTable.addCell(cell);//
            cell = makeCell("SSL Acct#  " + mq.getCarrierNo(), Element.ALIGN_LEFT, headingFontSize8, 0, Color.decode("#A1D490"));
            cell.setColspan(2);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthBottom(0.6f);
            chargesTable.addCell(cell);
            cell = makeCell("Origin: " + mq.getOrigin(), Element.ALIGN_LEFT, headingFontSize8, 0, Color.decode("#A1D490"));
            cell.setColspan(2);
            cell.setBorderWidthBottom(0.6f);
            chargesTable.addCell(cell);
            cell = makeCell("Destination: " + mq.getDestination(), Element.ALIGN_LEFT, headingFontSize8, 0, Color.decode("#A1D490"));
            cell.setColspan(2);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthBottom(0.6f);
            chargesTable.addCell(cell);
            cell = makeCellLeftNoBorder("Unit");
            cell.setBorderWidthBottom(0.6f);
            chargesTable.addCell(cell);
            cell = makeCellLeftNoBorder("Charge");
            cell.setBorderWidthBottom(0.6f);
            chargesTable.addCell(cell);
            cell = makeCellLeftNoBorder("Currency");
            cell.setBorderWidthBottom(0.6f);
            chargesTable.addCell(cell);
            cell = makeCellLeftNoBorder("Amount");
            cell.setBorderWidthBottom(0.6f);
            chargesTable.addCell(cell);
            List<MultiChargesOrderBean> multiQuotecharges = new MultiQuoteChargesDao().getChargesListForPdf(mq.getId());
            boolean aFlag = false;
            boolean bFlag = false;
            boolean cFlag = false;
            boolean dFlag = false;
            boolean eFlag = false;
            double Amount = 0.0;
            for (MultiChargesOrderBean charges : multiQuotecharges) {
                if (charges.getUnitNo().equalsIgnoreCase("A=20")) {
                    if (!aFlag) {
                        cell = makeCellLeftNoBorderBold(charges.getUnitNo());
                        chargesTable.addCell(cell);
                        aFlag = true;
                    } else {
                        cell = createEmptyCell();
                        chargesTable.addCell(cell);
                    }
                    cell = makeCellLeftNoBorderBold(charges.getChargeCodeDesc());
                    chargesTable.addCell(cell);
                    cell = makeCellLeftNoBorderBold(charges.getCurrency());
                    chargesTable.addCell(cell);
                    cell = makeCellLeftNoBorderBold(number.format(charges.getAmount()));
                    chargesTable.addCell(cell);
                    Amount = Amount + charges.getAmount().doubleValue();
                } else if (charges.getUnitNo().equalsIgnoreCase("B=40")) {
                    if (Amount != 0.00 && aFlag) {
                        cell = createEmptyCell();
                        cell.setColspan(2);
                        chargesTable.addCell(cell);
                        cell = makeCell("Total............" + number.format(Amount) + "(USD)", Element.ALIGN_LEFT, headingFont3, 0);
                        cell.setColspan(2);
                        chargesTable.addCell(cell);
                        Amount = 0.00;
                        aFlag = false;
                    }
                    if (!bFlag) {
                        cell = makeCellLeftNoBorderBold(charges.getUnitNo());
                        chargesTable.addCell(cell);
                        bFlag = true;
                    } else {
                        cell = createEmptyCell();
                        chargesTable.addCell(cell);
                    }
                    cell = makeCellLeftNoBorderBold(charges.getChargeCodeDesc());
                    chargesTable.addCell(cell);
                    cell = makeCellLeftNoBorderBold(charges.getCurrency());
                    chargesTable.addCell(cell);
                    cell = makeCellLeftNoBorderBold(number.format(charges.getAmount()));
                    chargesTable.addCell(cell);
                    Amount = Amount + charges.getAmount().doubleValue();
                } else if (charges.getUnitNo().equalsIgnoreCase("C=40HC")) {
                    if (Amount != 0.00 && bFlag) {
                        cell = createEmptyCell();
                        cell.setColspan(2);
                        chargesTable.addCell(cell);
                        cell = makeCell("Total............" + number.format(Amount) + "(USD)", Element.ALIGN_LEFT, headingFont3, 0);
                        cell.setColspan(2);
                        chargesTable.addCell(cell);
                        Amount = 0.00;
                        bFlag = false;
                    }
                    if (!cFlag) {
                        cell = makeCellLeftNoBorderBold(charges.getUnitNo());
                        chargesTable.addCell(cell);
                        cFlag = true;
                    } else {
                        cell = createEmptyCell();
                        chargesTable.addCell(cell);
                    }

                    cell = makeCellLeftNoBorderBold(charges.getChargeCodeDesc());
                    chargesTable.addCell(cell);
                    cell = makeCellLeftNoBorderBold(charges.getCurrency());
                    chargesTable.addCell(cell);
                    cell = makeCellLeftNoBorderBold(number.format(charges.getAmount()));
                    chargesTable.addCell(cell);
                    Amount = Amount + charges.getAmount().doubleValue();
                } else if (charges.getUnitNo().equalsIgnoreCase("D=45")) {
                    if (Amount != 0.00 && cFlag) {
                        cell = createEmptyCell();
                        cell.setColspan(2);
                        chargesTable.addCell(cell);
                        cell = makeCell("Total............" + number.format(Amount) + "(USD)", Element.ALIGN_LEFT, headingFont3, 0);
                        cell.setColspan(2);
                        chargesTable.addCell(cell);
                        Amount = 0.00;
                        cFlag = false;
                    }
                    if (!dFlag) {
                        cell = makeCellLeftNoBorderBold(charges.getUnitNo());
                        chargesTable.addCell(cell);
                        dFlag = true;
                    } else {
                        cell = createEmptyCell();
                        chargesTable.addCell(cell);
                    }
                    cell = makeCellLeftNoBorderBold(charges.getChargeCodeDesc());
                    chargesTable.addCell(cell);
                    cell = makeCellLeftNoBorderBold(charges.getCurrency());
                    chargesTable.addCell(cell);
                    cell = makeCellLeftNoBorderBold(number.format(charges.getAmount()));
                    chargesTable.addCell(cell);
                    Amount = Amount + charges.getAmount().doubleValue();
                } else if (charges.getUnitNo().equalsIgnoreCase("E=48")) {
                    if (Amount != 0.00 && dFlag) {
                        cell = createEmptyCell();
                        cell.setColspan(2);
                        chargesTable.addCell(cell);
                        cell = makeCell("Total............" + number.format(Amount) + "(USD)", Element.ALIGN_LEFT, headingFont3, 0);
                        cell.setColspan(2);
                        chargesTable.addCell(cell);
                        Amount = 0.00;
                        dFlag = false;
                    }
                    if (!eFlag) {
                        cell = makeCellLeftNoBorderBold(charges.getUnitNo());
                        chargesTable.addCell(cell);
                        eFlag = true;
                    } else {
                        cell = createEmptyCell();
                        chargesTable.addCell(cell);
                    }
                    cell = makeCellLeftNoBorderBold(charges.getChargeCodeDesc());
                    chargesTable.addCell(cell);
                    cell = makeCellLeftNoBorderBold(charges.getCurrency());
                    chargesTable.addCell(cell);
                    cell = makeCellLeftNoBorderBold(number.format(charges.getAmount()));
                    chargesTable.addCell(cell);
                    Amount = Amount + charges.getAmount().doubleValue();
                }
            }
            if (Amount != 0.00) { // last Unit value
                cell = createEmptyCell();
                cell.setColspan(2);
                chargesTable.addCell(cell);

                cell = makeCell("Total............" + number.format(Amount) + "(USD)", Element.ALIGN_LEFT, headingFont3, 0);
                cell.setColspan(2);
                chargesTable.addCell(cell);
            }
            cell = new PdfPCell();
            cell.setColspan(4);
            cell.addElement(chargesTable);
            cell.setBorder(0);
            cell.setPadding(0f);
            cell.setBorderWidthRight(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthBottom(0.6f);
            mainTable.addCell(cell);
        }
        document.add(mainTable);
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        QuotationBC quotationBC = new QuotationBC();
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
                    String grossWeight = (null != hazmatMaterial.getGrossWeight()) ? number.format(hazmatMaterial.getGrossWeight()) + "  " : "0.00  ";
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

        ////created by information
        User userDetails = getPleaseContactDetails(quotation.getQuoteBy() != null ? quotation.getQuoteBy() : "");
        PdfPTable contactUsTable = makeTable(1);
        contactUsTable.setWidthPercentage(100);
        StringBuilder buffer = new StringBuilder("");
        buffer.append("Please Send Inquiries and Docs to ");
        String email = quotationBC.getEmailToDisplayINReport(QuotationConstants.TYPEOFLOAD, StringFormatter.getIssuingTerminal(quotation.getIssuingTerminal()),
                StringFormatter.orgDestStringFormatter(quotation.getFinaldestination()));
        if (quotation.getDocsInquiries() != null && quotation.getDocsInquiries().equals("on")) {
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
        boolean importFlag = (null != quotation && null != quotation.getFileType() && quotation.getFileType().equalsIgnoreCase("I"));
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
        if (quotation.getDestination_port() != null && !"".equals(quotation.getDestination_port())) {
            destin = quotation.getDestination_port();
            checkRegion = quoteDwrBC.checkForTheRegion(destin);
        }
        cell = makeCellLeftNoBorder(getOriginRemarks(quotation));
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        disclaimerTable.addCell(cell);
        if (checkRegion != null && "true".equals(checkRegion)) {
            if (quotation.getDeductFfcomm() != null && "N".equals(quotation.getDeductFfcomm())) {
                disclaimerList.remove(0);
            }
            if (quotation.getDeductFfcomm() != null && "N".equals(quotation.getDeductFfcomm())) {
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
            if (quotation.getDeductFfcomm() != null && "N".equals(quotation.getDeductFfcomm())) {
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
//        document.add(DescriptionRemarksTable);
//        document.add(portsRemarksTable1);
//        document.add(regionRemarksTable);
//        document.add(ChargeHeading);
//        document.add(chargesTable);
//        document.add(otherChargeDetails);
        document.add(hazmatDetails);
        document.add(createdByDetails);
//        if (quotation.getRatesNonRates() != null && quotation.getRatesNonRates().equals("R")) {
        document.add(disclaimerTable);
//        } 
//        else if (quotation.getRatesNonRates() != null && quotation.getRatesNonRates().equals("N")
//                && quotation.getImportantDisclosures() != null && quotation.getImportantDisclosures().equals("on")) {
//            document.add(disclaimerTable);
//        }

    }

    private User getPleaseContactDetails(String loginName) throws Exception {
        UserDAO userDAO = new UserDAO();
        return userDAO.findUserName(loginName);

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
            log.info("MultiQuotePdfCreator onEndPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    public void destroy() {
        document.close();
    }
}
