package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.ExportQuoteUtils;
import com.gp.cong.lcl.common.constant.ImportQuoteUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuoteDestinationServices;
import com.gp.cong.logisoft.domain.lcl.LclQuotePad;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.lcl.model.LclBookingVoyageBean;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.beanutils.BeanUtils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;

public class LclQuotePdfCreator extends LclReportFormatMethods implements LclCommonConstant {

    private LclQuote lclQuote;
    private Integer polTransitTime = 0;
    private String quoteType = "";
    StringBuilder originValues = new StringBuilder();
    StringBuilder fdValues = new StringBuilder();
    StringBuilder polValues = new StringBuilder();
    StringBuilder podValues = new StringBuilder();
    StringBuilder deliverValues = new StringBuilder();
    private List<String> LQ = new ArrayList<String>();
    private List<String> ILQ = new ArrayList<String>();
    private boolean commodityflag = false;
    private String specialRemarks = "";
    private String externalComments = "";
    private String companyWebsite = "";

    public LclQuotePdfCreator(LclQuote lclQuote) throws Exception {
        this.lclQuote = lclQuote;
        new LCLQuoteDAO().getCurrentSession().evict(lclQuote);
        quoteType = lclQuote.getQuoteType();
        if (LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(quoteType)) {
            quoteType = LCL_IMPORT_TYPE;
        }
        String[] remarkTypes = {"Special Remarks", "E"};
        List remarks = new LclRemarksDAO().getRemarksByTypes(lclQuote.getFileNumberId(), remarkTypes);
        for (Object row : remarks) {
            Object[] col = (Object[]) row;
            if (col[1].toString().equalsIgnoreCase("E")) {
                externalComments = col[0].toString();
            }
            if (col[1].toString().equalsIgnoreCase("Special Remarks")) {
                specialRemarks = col[0].toString();
            }
        }
        LclBookingPlanDAO lclBookingPlanDAO = new LclBookingPlanDAO();
        LclBookingPlanBean lclBookingPlanBean = new LclBookingPlanBean();
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType) && lclQuote.getPortOfOrigin() != null && lclQuote.getFinalDestination() != null) {
            lclBookingPlanBean = lclBookingPlanDAO.getRelay(lclQuote.getPortOfOrigin().getId(),
                    lclQuote.getFinalDestination().getId(), "N");
        }
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType) && lclBookingPlanBean != null && lclBookingPlanBean.getPol_transit_time() != null) {
            polTransitTime = lclBookingPlanBean.getPol_transit_time();
        }
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType) && lclQuote.getPortOfOrigin() != null) {
            if (CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationName())) {
                originValues.append(lclQuote.getPortOfOrigin().getUnLocationName()).append(",");
            }
            if (null != lclQuote.getPortOfOrigin().getStateId() && CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getStateId().getCode())) {
                originValues.append(lclQuote.getPortOfOrigin().getStateId().getCode()).append("  ");
            }
            if (CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationCode())) {
                originValues.append("(").append(lclQuote.getPortOfOrigin().getUnLocationCode()).append(")");
            }
        } else {
            if (lclQuote.getPortOfOrigin() != null) {
                if (CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationName())) {
                    originValues.append(lclQuote.getPortOfOrigin().getUnLocationName()).append(",");
                }
                if (null != lclQuote.getPortOfOrigin().getCountryId() && CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getCountryId().getCodedesc())) {
                    originValues.append(lclQuote.getPortOfOrigin().getCountryId().getCodedesc()).append("  ");
                }
                if (CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationCode())) {
                    originValues.append("(").append(lclQuote.getPortOfOrigin().getUnLocationCode()).append(")");
                }
            }
        }
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType) && lclQuote.getPortOfLoading() != null) {
            if (CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getUnLocationName())) {
                polValues.append(lclQuote.getPortOfLoading().getUnLocationName()).append(",");
            }
            if (null != lclQuote.getPortOfLoading().getStateId() && CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getStateId().getCode())) {
                polValues.append(lclQuote.getPortOfLoading().getStateId().getCode()).append("  ");
            }
            if (CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getUnLocationCode())) {
                polValues.append("(").append(lclQuote.getPortOfLoading().getUnLocationCode()).append(")");
            }
        } else {
            if (lclQuote.getPortOfLoading() != null) {
                if (CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getUnLocationName())) {
                    polValues.append(lclQuote.getPortOfLoading().getUnLocationName()).append(",");
                }
                if (null != lclQuote.getPortOfLoading().getCountryId() && CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getCountryId().getCodedesc())) {
                    polValues.append(lclQuote.getPortOfLoading().getCountryId().getCodedesc()).append("  ");
                }
                if (CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getUnLocationCode())) {
                    polValues.append("(").append(lclQuote.getPortOfLoading().getUnLocationCode()).append(")");
                }
            }
        }
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType) && lclQuote.getFinalDestination() != null) {
            if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getCountryId().getCodedesc())) {
                fdValues.append(lclQuote.getFinalDestination().getCountryId().getCodedesc()).append(",");
            }
            if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationName())) {
                fdValues.append(lclQuote.getFinalDestination().getUnLocationName()).append(" ");
            }
            if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationCode())) {
                fdValues.append("(").append(lclQuote.getFinalDestination().getUnLocationCode()).append(")");
            }
        } else {
            if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationName())) {
                fdValues.append(lclQuote.getFinalDestination().getUnLocationName()).append(",");
            }
            if (null != lclQuote.getFinalDestination().getStateId() && CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getStateId().getCode())) {
                fdValues.append(lclQuote.getFinalDestination().getStateId().getCode()).append(" ");
            } else if (null != lclQuote.getFinalDestination().getCountryId() && CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getCountryId().getCodedesc())) {
                fdValues.append(lclQuote.getFinalDestination().getCountryId().getCodedesc()).append(" ");
            }
            if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationCode())) {
                fdValues.append("(").append(lclQuote.getFinalDestination().getUnLocationCode()).append(")");
            }
        }
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType) && lclQuote.getPortOfDestination() != null) {
            if (lclQuote.getPortOfDestination().getCountryId() != null && CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getCountryId().getCodedesc())) {
                podValues.append(lclQuote.getPortOfDestination().getCountryId().getCodedesc()).append(",");
            }
            if (CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getUnLocationName())) {
                podValues.append(lclQuote.getPortOfDestination().getUnLocationName()).append(" ");
            }
            if (CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getUnLocationCode())) {
                podValues.append("(").append(lclQuote.getPortOfDestination().getUnLocationCode()).append(")");
            }
        } else {
            if (lclQuote.getPortOfDestination() != null) {
                if (CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getUnLocationName())) {
                    podValues.append(lclQuote.getPortOfDestination().getUnLocationName()).append(",");
                }
                if (null != lclQuote.getPortOfDestination().getStateId() && CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getStateId().getCode())) {
                    podValues.append(lclQuote.getPortOfDestination().getStateId().getCode()).append(" ");
                } else if (null != lclQuote.getPortOfDestination().getCountryId() && CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getCountryId().getCodedesc())) {
                    podValues.append(lclQuote.getPortOfDestination().getCountryId().getCodedesc()).append(" ");
                }
                if (CommonUtils.isNotEmpty(lclQuote.getPortOfDestination().getUnLocationCode())) {
                    podValues.append("(").append(lclQuote.getPortOfDestination().getUnLocationCode()).append(")");
                }
            }
        }

        List<LclQuotePiece> lclQuotationPieceList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", lclQuote.getLclFileNumber().getId());
        if (!lclQuotationPieceList.isEmpty()) {
            for (LclQuotePiece lclQuotePiece : lclQuotationPieceList) {
                if (lclQuotePiece.getBookedVolumeImperial() == null || lclQuotePiece.getBookedWeightImperial() == null
                        || lclQuotePiece.getBookedVolumeImperial().doubleValue() == 0.00
                        || lclQuotePiece.getBookedWeightImperial().doubleValue() == 0.00) {
                    commodityflag = true;
                }
            }
        }
        printQuoteComments();
        if (LCL_IMPORT_TYPE.equalsIgnoreCase(quoteType)) {
            printQuoteImpComments();
        }
    }

    public void printQuoteImpComments() throws Exception {
        Iterator bookingCommentsIterator = new GenericCodeDAO().getLclPrintComments(39, "ILQ");
        while (bookingCommentsIterator.hasNext()) {
            Object[] row = (Object[]) bookingCommentsIterator.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null && CommonFunctions.isNotNull(codeDesc)) {
                ILQ.add(codeDesc);
            }
        }
    }

    public void printQuoteComments() throws Exception {
        Iterator bookingCommentsIterator = new GenericCodeDAO().getLclPrintComments(39, "LQ");
        while (bookingCommentsIterator.hasNext()) {
            Object[] row = (Object[]) bookingCommentsIterator.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null && CommonFunctions.isNotNull(codeDesc)) {
                LQ.add(codeDesc);
            }
        }
    }

    class TableHeader extends PdfPageEventHelper {

        PdfTemplate total;

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(30, 16);
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
        }
    }

    public void createQuotePdf(String realPath, String outputFileName) throws Exception {
        document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(4, 0, 8, 8);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        LclQuotePdfCreator.TableHeader event = new LclQuotePdfCreator.TableHeader();
        pdfWriter.setPageEvent(event);
        document.open();
        document.add(onStartPage(realPath));
        document.add(bodyTable());
        if ("E".equalsIgnoreCase(quoteType)) {
            document.add(externalCommentsTable());
        }
        document.add(shipTable());
        if ("E".equalsIgnoreCase(quoteType)) {
            document.add(voyageTable());
            document.add(voyageListTable());
            document.add(remarksTable());
            document.add(cargoTable());
        }
        if ("T".equalsIgnoreCase(lclQuote.getQuoteType())) {
            document.add(remarksTable());
        }
        document.add(ratesTable());
        if ("I".equalsIgnoreCase(quoteType)) {
            document.add(externalCommentsTable());
        }
        document.add(dispalyContentTable(realPath));
        document.add(emptyTable());
        document.add(advisteTable());
        document.add(samplesTable());
        if ("E".equalsIgnoreCase(quoteType)) {
            document.add(rulesTable());
        } else {
            document.add(importCommentsTable());
        }
        document.add(advisoryTable());
        document.close();
    }

    public PdfPTable onStartPage(String realPath) throws Exception {

        Font fontArialBold = FontFactory.getFont("Arial", 10f, Font.BOLD);
        Font colorBoldFont = FontFactory.getFont("Arial", 12f, Font.BOLD, new BaseColor(00, 102, 00));
        String logoImage = "";
        Date d = new Date();
        Phrase p = null;
        Paragraph pValues = null;
        table = new PdfPTable(6);
        table.setWidths(new float[]{0.1f, 1.8f, 2.5f, 3.5f, 2.3f, 1.8f});
        table.setWidthPercentage(100f);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setRowspan(3);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setPaddingBottom(3f);
        p = new Phrase("Quote Issued By. :", fontArialBold);
        p.setLeading(20f);
        cell.addElement(p);
        table.addCell(cell);
        StringBuilder firstLastName = new StringBuilder();
        if (lclQuote.getEnteredBy() != null && lclQuote.getEnteredBy().getFirstName() != null) {
            firstLastName.append(lclQuote.getEnteredBy().getFirstName()).append(" ");
        }
        if (lclQuote.getEnteredBy() != null && lclQuote.getEnteredBy().getLastName() != null) {
            firstLastName.append(lclQuote.getEnteredBy().getLastName()).append(" ");
        }
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingBottom(3f);
        if (!"".equals(firstLastName.toString())) {
            pValues = new Paragraph(20f, "" + firstLastName.toString(), fontArialBold);
            pValues.setAlignment(Element.ALIGN_LEFT);
        } else {
            pValues = new Paragraph(25f, "", fontArialBold);
        }
        String mnemonicCode = lclQuote.getLclFileNumber().getBusinessUnit();
        if (CommonUtils.isEmpty(mnemonicCode)) {
            mnemonicCode = "ECU";
        }
        logoImage = LoadLogisoftProperties.getProperty(mnemonicCode.equalsIgnoreCase("ECU") ? "application.image.logo"
                : "application.image.econo.logo");
        companyWebsite = LoadLogisoftProperties.getProperty(mnemonicCode.equalsIgnoreCase("ECU") ? "application.ECU.website"
                : mnemonicCode.equalsIgnoreCase("OTI") ? "application.OTI.website" : "application.Econo.website");
        cell.addElement(pValues);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setRowspan(4);
        cell.setBorder(0);
        cell.setPadding(0f);
        Image img = Image.getInstance(realPath + logoImage);
        img.scalePercent(60);
        // img.scaleToFit(150f, 50f);
        img.setAlignment(Element.ALIGN_CENTER);
        img.setAlignment(Element.ALIGN_TOP);
        cell.addElement(img);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingBottom(3f);
        pValues = new Paragraph(20f, "Today's Date:", fontArialBold);
        pValues.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(pValues);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingBottom(3f);
        pValues = new Paragraph(20f, " " + DateUtils.formatStringDateToAppFormatMMM(new Date()), fontArialBold);
        pValues.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(pValues);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setPaddingBottom(3f);
        p = new Phrase(9f, "Quote Date . . . . . :", fontArialBold);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingBottom(3f);
        if (lclQuote.getLclFileNumber() != null && lclQuote.getLclFileNumber().getCreatedDatetime() != null) {
            pValues = new Paragraph(9f, "" + DateUtils.formatStringDateToAppFormatMMM(lclQuote.getLclFileNumber().getCreatedDatetime()), fontArialBold);
            pValues.setAlignment(Element.ALIGN_LEFT);
        } else {
            pValues = new Paragraph(9f, "", fontArialBold);
        }
        cell.addElement(pValues);
        cell.setPadding(0f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingBottom(3f);
        pValues = new Paragraph(9f, "Time:", fontArialBold);
        pValues.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(pValues);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingBottom(3f);
        pValues = new Paragraph(9f, " " + DateUtils.formatStringDateToTimeTT(new Date()), fontArialBold);
        pValues.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(pValues);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingBottom(3f);
        if ("E".equalsIgnoreCase(quoteType)) {
            pValues = new Paragraph(9f, "Quote Number: " + lclQuote.getPortOfOrigin().getUnLocationCode().substring(2) + "-" + lclQuote.getLclFileNumber().getFileNumber(), colorBoldFont);
        } else {
            pValues = new Paragraph(9f, "Quote Number: " + lclQuote.getLclFileNumber().getFileNumber(), colorBoldFont);
        }
        cell.addElement(pValues);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setPaddingBottom(3f);
        if ("E".equalsIgnoreCase(quoteType)) {
            pValues = new Paragraph(9f, "LCL Ocean Export Quote", colorBoldFont);
        } else {
            pValues = new Paragraph(9f, "LCL Ocean Import Quote", colorBoldFont);
        }
        pValues.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(pValues);
        table.addCell(cell);
        return table;
    }

    public PdfPTable bodyTable() throws DocumentException {
        Font fontCompSub = FontFactory.getFont("Arial", 9f, Font.BOLD);
        PdfPCell cell2 = null;
        Paragraph pHeading = null;
        String contactCompanyName = "";
        String contactName = "";
        String contactPhone = "";
        String contactFax = "";
        String contactEmail = "";
        PdfPTable ntable = new PdfPTable(1);
        ntable.setWidthPercentage(100f);
        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setColspan(2);
        cell1.setBorderColor(new BaseColor(00, 51, 153));
        cell1.setBorderWidthBottom(3f);
        cell1.setBorderWidthLeft(3f);
        cell1.setBorderWidthRight(3f);
        cell1.setBorderWidthTop(10f);
        cell1.setPadding(0f);
        //Heading
        pHeading = new Paragraph(8f, "Company Details", mainHeadingQuote);
        pHeading.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(pHeading);
        PdfPTable ntable1 = new PdfPTable(6);
        ntable1.setWidthPercentage(100f);
        ntable1.setWidths(new float[]{0.1f, 1.15f, 0.09f, 4.09f, 1f, 2f});
        //company Name
        ntable1.addCell(createEmptyCell(0, 1f, 6));
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        ntable1.addCell(cell2);
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        pHeading = new Paragraph(8f, "Company Name. . .", fontCompSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        pHeading = new Paragraph(8f, ":", fontCompSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        if (!LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType)) {
            if (lclQuote.getClientContact() != null && CommonUtils.isNotEmpty(lclQuote.getClientContact().getCompanyName())) {
                contactCompanyName = lclQuote.getClientContact().getCompanyName();
            }
        } else {
            if (lclQuote.getClientContact() != null && CommonUtils.isNotEmpty(lclQuote.getClientContact().getCompanyName()) && CommonUtils.isNotEmpty(lclQuote.getClientContact().getContactName())) {
                contactCompanyName = lclQuote.getClientContact().getCompanyName();
            } else if (lclQuote.getShipContact() != null && CommonUtils.isNotEmpty(lclQuote.getShipContact().getCompanyName()) && CommonUtils.isNotEmpty(lclQuote.getShipContact().getContactName())) {
                contactCompanyName = lclQuote.getShipContact().getCompanyName();
            } else if (lclQuote.getConsContact() != null && CommonUtils.isNotEmpty(lclQuote.getConsContact().getCompanyName()) && CommonUtils.isNotEmpty(lclQuote.getConsContact().getContactName())) {
                contactCompanyName = lclQuote.getConsContact().getCompanyName();
            } else if (lclQuote.getFwdContact() != null && CommonUtils.isNotEmpty(lclQuote.getFwdContact().getCompanyName()) && CommonUtils.isNotEmpty(lclQuote.getFwdContact().getContactName())) {
                contactCompanyName = lclQuote.getFwdContact().getCompanyName();
            } else if (("").equalsIgnoreCase(contactCompanyName) && CommonUtils.isNotEmpty(lclQuote.getClientContact().getCompanyName())) {
                contactCompanyName = lclQuote.getClientContact().getCompanyName();
            }
        }
        pHeading = new Paragraph(8f, "" + contactCompanyName, fontgreenCont);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        pHeading = new Paragraph(8f, "To Phone. . . . .:", fontCompSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        if (!LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType)) {
            if (lclQuote.getClientContact() != null && CommonUtils.isNotEmpty(lclQuote.getClientContact().getPhone1())) {
                contactPhone = lclQuote.getClientContact().getPhone1();
            }
        } else {
            if (lclQuote.getClientContact() != null && CommonUtils.isNotEmpty(lclQuote.getClientContact().getPhone1()) && CommonUtils.isNotEmpty(lclQuote.getClientContact().getContactName())) {
                contactPhone = lclQuote.getClientContact().getPhone1();
            } else if (lclQuote.getShipContact() != null && CommonUtils.isNotEmpty(lclQuote.getShipContact().getPhone1()) && CommonUtils.isNotEmpty(lclQuote.getShipContact().getContactName())) {
                contactPhone = lclQuote.getShipContact().getPhone1();
            } else if (lclQuote.getConsContact() != null && CommonUtils.isNotEmpty(lclQuote.getConsContact().getPhone1()) && CommonUtils.isNotEmpty(lclQuote.getConsContact().getContactName())) {
                contactPhone = lclQuote.getConsContact().getPhone1();
            } else if (lclQuote.getFwdContact() != null && CommonUtils.isNotEmpty(lclQuote.getFwdContact().getPhone1()) && CommonUtils.isNotEmpty(lclQuote.getFwdContact().getContactName())) {
                contactPhone = lclQuote.getFwdContact().getPhone1();
            } else if (("").equalsIgnoreCase(contactPhone) && CommonUtils.isNotEmpty(lclQuote.getClientContact().getPhone1())) {
                contactPhone = lclQuote.getClientContact().getPhone1();
            }
        }
        pHeading = new Paragraph(8f, "" + contactPhone, fontCompNormalSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);
        //3
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        ntable1.addCell(cell2);
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        pHeading = new Paragraph(8f, "Contact Person. . .", fontCompSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        pHeading = new Paragraph(8f, ":", fontCompSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        if (!LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType)) {
            if (lclQuote.getClientContact() != null && CommonUtils.isNotEmpty(lclQuote.getClientContact().getContactName())) {
                contactName = lclQuote.getClientContact().getContactName();
            }
        } else {
            if (lclQuote.getClientContact() != null && CommonUtils.isNotEmpty(lclQuote.getClientContact().getContactName())) {
                contactName = lclQuote.getClientContact().getContactName();
            } else if (lclQuote.getShipContact() != null && CommonUtils.isNotEmpty(lclQuote.getShipContact().getContactName())) {
                contactName = lclQuote.getShipContact().getContactName();
            } else if (lclQuote.getConsContact() != null && CommonUtils.isNotEmpty(lclQuote.getConsContact().getContactName())) {
                contactName = lclQuote.getConsContact().getContactName();
            } else if (lclQuote.getFwdContact() != null && CommonUtils.isNotEmpty(lclQuote.getFwdContact().getContactName())) {
                contactName = lclQuote.getFwdContact().getContactName();
            }
        }
        pHeading = new Paragraph(8f, "" + contactName, fontgreenCont);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        pHeading = new Paragraph(8f, "To Fax . . . . . . .:", fontCompSub);
        cell2.addElement(pHeading);

        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        if (!LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType)) {
            if (lclQuote.getClientContact() != null && CommonUtils.isNotEmpty(lclQuote.getClientContact().getFax1())) {
                contactFax = lclQuote.getClientContact().getFax1();
            }
        } else {
            if (lclQuote.getClientContact() != null && CommonUtils.isNotEmpty(lclQuote.getClientContact().getFax1()) && CommonUtils.isNotEmpty(lclQuote.getClientContact().getContactName())) {
                contactFax = lclQuote.getClientContact().getFax1();
            } else if (lclQuote.getShipContact() != null && CommonUtils.isNotEmpty(lclQuote.getShipContact().getFax1()) && CommonUtils.isNotEmpty(lclQuote.getShipContact().getContactName())) {
                contactFax = lclQuote.getShipContact().getFax1();
            } else if (lclQuote.getConsContact() != null && CommonUtils.isNotEmpty(lclQuote.getConsContact().getFax1()) && CommonUtils.isNotEmpty(lclQuote.getConsContact().getContactName())) {
                contactFax = lclQuote.getConsContact().getFax1();
            } else if (lclQuote.getFwdContact() != null && CommonUtils.isNotEmpty(lclQuote.getFwdContact().getFax1()) && CommonUtils.isNotEmpty(lclQuote.getFwdContact().getContactName())) {
                contactFax = lclQuote.getFwdContact().getFax1();
            } else if (("").equalsIgnoreCase(contactFax) && CommonUtils.isNotEmpty(lclQuote.getClientContact().getFax1())) {
                contactFax = lclQuote.getClientContact().getFax1();
            }
        }
        pHeading = new Paragraph(8f, "" + contactFax, fontCompNormalSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);
        //4
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        ntable1.addCell(cell2);
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        pHeading = new Paragraph(8f, "Email . . . . . . . . . . .", fontCompSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        pHeading = new Paragraph(8f, ":", fontCompSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setColspan(3);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        if (!LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType)) {
            if (lclQuote.getClientContact() != null && CommonUtils.isNotEmpty(lclQuote.getClientContact().getEmail1())) {
                contactEmail = lclQuote.getClientContact().getEmail1();
            }
        } else {
            if (lclQuote.getClientContact() != null && CommonUtils.isNotEmpty(lclQuote.getClientContact().getEmail1())) {
                contactEmail = lclQuote.getClientContact().getEmail1();
            } else if (lclQuote.getShipContact() != null && CommonUtils.isNotEmpty(lclQuote.getShipContact().getEmail1())) {
                contactEmail = lclQuote.getShipContact().getEmail1();
            } else if (lclQuote.getConsContact() != null && CommonUtils.isNotEmpty(lclQuote.getConsContact().getEmail1())) {
                contactEmail = lclQuote.getConsContact().getEmail1();
            } else if (lclQuote.getFwdContact() != null && CommonUtils.isNotEmpty(lclQuote.getFwdContact().getEmail1())) {
                contactEmail = lclQuote.getFwdContact().getEmail1();
            }
        }
        pHeading = new Paragraph(8f, "" + contactEmail, fontCompNormalSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);
        //
        ntable1.addCell(createEmptyCell(0, 1f, 6));
        cell1.addElement(ntable1);
        ntable.addCell(cell1);
        return ntable;
    }

    public PdfPTable externalCommentsTable() throws DocumentException, Exception {

        Font fontRemarkSub = FontFactory.getFont("Arial", 9f, Font.BOLD);
        Font fontRemarkNormalSub = new Font(FontFamily.HELVETICA, 9f, Font.NORMAL);
        PdfPCell cell2 = null;
        Phrase p = null;
        Paragraph pHeading = null;
        PdfPTable ntable = new PdfPTable(1);
        ntable.setWidthPercentage(100f);
        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setBorderColor(new BaseColor(00, 51, 153));
        cell1.setBorderWidthLeft(3f);
        cell1.setBorderWidthRight(3f);
        cell1.setBorderWidthBottom(3f);
        cell1.setPadding(0f);
        PdfPTable ntable1 = new PdfPTable(1);
        ntable1.setWidthPercentage(98.85f);
        //special Remarkheading
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(255, 217, 00));
        cell2.setPaddingTop(0f);
        cell2.setPaddingBottom(2f);
        cell2.setBorderColor(new BaseColor(255, 178, 0));
        cell2.setBorderWidthBottom(0.06f);
        pHeading = new Paragraph(7f, "Additional Comments", fontRemarkSub);
        pHeading.setAlignment(Element.ALIGN_CENTER);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(BaseColor.WHITE);
        cell2.setPaddingBottom(0.001f);
        cell2.setColspan(2);
        ntable1.addCell(cell2);

        //remarks sub
        cell2 = new PdfPCell();
        cell2.setPaddingTop(1f);
        cell2.setPaddingBottom(3f);
        cell2.setBorder(0);
        if (!"".equals(externalComments)) {
            pHeading = new Paragraph(9f, "" + externalComments.toUpperCase(), fontRemarkNormalSub);
        } else {
            pHeading = new Paragraph(9f, "", fontRemarkNormalSub);
            cell2.setPadding(10f);
        }
        pHeading.setAlignment(Element.ALIGN_LEFT);
        pHeading.setAlignment(Element.ALIGN_JUSTIFIED);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        ntable1.addCell(cell2);
        cell1.addElement(ntable1);
        ntable.addCell(cell1);
        return ntable;
    }

    public PdfPTable emptyTable1() {
        PdfPTable table = null;
        PdfPCell cell = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setFixedHeight(2f);
        table.addCell(cell);
        return table;
    }

    public PdfPTable shipTable() throws DocumentException, Exception {
        Font fontShipSub = FontFactory.getFont("Arial", 9f, Font.BOLD);
        StringBuilder polpodValues = new StringBuilder();
        StringBuilder podfdValues = new StringBuilder();
        String ctcValues = "";
        if ("E".equalsIgnoreCase(quoteType)) {
            if (lclQuote.getRateType().equalsIgnoreCase("C")) {
                ctcValues = "COLOAD";
            } else if (lclQuote.getRateType().equalsIgnoreCase("R")) {
                ctcValues = "RETAIL";
            } else if (lclQuote.getRateType().equalsIgnoreCase("F")) {
                ctcValues = "FTF";
            }
            if (CommonFunctions.isNotNull(lclQuote.getPortOfLoading()) && CommonFunctions.isNotNull(lclQuote.getPortOfLoading().getUnLocationName())) {
                polpodValues.append(lclQuote.getPortOfLoading().getUnLocationName()).append(" to ");
            }
            if (CommonFunctions.isNotNull(lclQuote.getPortOfDestination()) && CommonFunctions.isNotNull(lclQuote.getPortOfDestination().getUnLocationName())) {
                polpodValues.append(lclQuote.getPortOfDestination().getUnLocationName());
            }
            if (CommonFunctions.isNotNull(lclQuote.getPortOfDestination()) && CommonFunctions.isNotNull(lclQuote.getPortOfDestination().getUnLocationName())) {
                podfdValues.append(lclQuote.getPortOfDestination().getUnLocationName()).append(" to ");
            }
        }
        PdfPCell cell2 = null;
        Phrase p = null;
        Paragraph pHeading = null;
        PdfPTable ntable = new PdfPTable(1);
        ntable.setWidthPercentage(100f);
        PdfPCell cell1 = new PdfPCell();
        cell1.setPadding(0f);
        cell1.setBorder(0);
        cell1.setColspan(2);
        cell1.setBorderColor(new BaseColor(00, 51, 153));
        cell1.setBorderWidthLeft(3f);
        cell1.setBorderWidthRight(3f);
        cell1.setBorderWidthTop(9f);
        cell1.setBorderWidthBottom(0f);
        cell1.setPadding(0f);
        //heading
        pHeading = new Paragraph(7f, "Shipment Details", mainHeadingQuote);
        pHeading.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(pHeading);
        PdfPTable ntable1 = new PdfPTable(6);
        ntable1.setWidthPercentage(100f);
        ntable1.setWidths(new float[]{0.1f, 1.5f, 0.09f, 4.09f, 0.9f, 1f});
        ntable1.addCell(createEmptyCell(0, 1f, 6));
        //origin
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        pHeading = new Paragraph(8f, "Origin CFS. . . . . . . . . . . . .", fontShipSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        pHeading = new Paragraph(8f, ":", fontShipSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        cell2.setPaddingLeft(0f);
        pHeading = new Paragraph(8f, "" + originValues.toString().toUpperCase(), fontgreenCont);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setRowspan(6);
        cell2.setBorder(0);
        if (null != lclQuote.getLclFileNumber().getLclQuoteImport() && null != lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo()) {
            pHeading = new Paragraph(8f, "IPI CFS . . .. . :", fontShipSub);
        } else {
            pHeading = new Paragraph(8f, "", fontgreenCont);
        }
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);
        cell2 = new PdfPCell();
        cell2.setRowspan(5);
        cell2.setBorder(0);
        if (!ctcValues.equals("")) {
            pHeading = new Paragraph(8f, "" + ctcValues, fontgreenCont);
        } else {
            StringBuilder ipicfsDetails = new StringBuilder();
            if (null != lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo()) {
                if (lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getAccountno() != null) {
                    ipicfsDetails.append(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getAccountName()).append(" (");
                    ipicfsDetails.append(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getAccountno()).append(")").append("\n");
                }
                if (null != lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getCustAddr()) {
                    if (CommonUtils.isNotEmpty(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getCustAddr().getAddress1())) {
                        ipicfsDetails.append(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getCustAddr().getAddress1()).append("\n");
                    }
                    if (CommonUtils.isNotEmpty(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getCustAddr().getCity2())) {
                        ipicfsDetails.append(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getCustAddr().getCity2()).append(",");
                    }
                    if (CommonUtils.isNotEmpty(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getCustAddr().getState())) {
                        ipicfsDetails.append(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getCustAddr().getState());
                    }
                    if (CommonUtils.isNotEmpty(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getCustAddr().getZip())) {
                        ipicfsDetails.append("-").append(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getCustAddr().getZip());
                    }
                    if (CommonUtils.isNotEmpty(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getCustAddr().getPhone())) {
                        ipicfsDetails.append("\n").append("Phone:").append(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getCustAddr().getPhone());
                    }
                    if (CommonUtils.isNotEmpty(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getCustAddr().getFax())) {
                        ipicfsDetails.append("\n").append("Fax:").append(lclQuote.getLclFileNumber().getLclQuoteImport().getIpiCfsAcctNo().getCustAddr().getFax());
                    }
                }
            }
            Font greenBoldFont75 = FontFactory.getFont("Arial", 7f, Font.BOLD, new BaseColor(00, 128, 00));
            pHeading = new Paragraph(8f, "" + ipicfsDetails.toString(), greenBoldFont75);
        }
        pHeading.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);
        String companyCode = new SystemRulesDAO().getSystemRuleNameByRuleCode("CompanyCode");
        if (companyCode.equalsIgnoreCase("03")) {
            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            ntable1.addCell(cell2);
            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            pHeading = new Paragraph(8f, "POL . . . . . . . . . . . . . . . . . . ", fontShipSub);
            cell2.addElement(pHeading);
            ntable1.addCell(cell2);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            pHeading = new Paragraph(8f, ":", fontShipSub);
            cell2.addElement(pHeading);
            ntable1.addCell(cell2);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            cell2.setPaddingLeft(0f);
            pHeading = new Paragraph(8f, "" + polValues.toString().toUpperCase(), fontgreenCont);
            cell2.addElement(pHeading);
            ntable1.addCell(cell2);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            ntable1.addCell(cell2);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            pHeading = new Paragraph(8f, "POD . . . . . . . . . . . . . . . . . . ", fontShipSub);
            cell2.addElement(pHeading);
            ntable1.addCell(cell2);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            pHeading = new Paragraph(8f, ":", fontShipSub);
            cell2.addElement(pHeading);
            ntable1.addCell(cell2);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            cell2.setPaddingLeft(0f);
            pHeading = new Paragraph(8f, "" + podValues.toString().toUpperCase(), fontgreenCont);
            cell2.addElement(pHeading);
            ntable1.addCell(cell2);
        }
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        ntable1.addCell(cell2);
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        pHeading = new Paragraph(8f, "Destination . . . . . . . . . . . . ", fontShipSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        pHeading = new Paragraph(8f, ":", fontShipSub);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(4f);
        cell2.setPaddingLeft(0f);
        String destination_service_city = new LclQuotePadDAO().getCountryForQuoteDestinationServices(lclQuote.getFileNumberId());
        pHeading = new Paragraph(7f, CommonUtils.isNotEmpty(destination_service_city) ? "" + destination_service_city
                : "" + fdValues.toString().toUpperCase(), fontgreenCont);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);
        LclQuotePad lclQuotePad = new LclQuotePadDAO().getLclQuotePadByFileNumber(lclQuote.getFileNumberId());
        if (quoteType.equals("I") && null != lclQuotePad && CommonUtils.isNotEmpty(lclQuotePad.getPickUpCity())) {
            String[] doorOriginCityZip = lclQuotePad.getPickUpCity().split("/");
            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            ntable1.addCell(cell2);
            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            pHeading = new Paragraph(8f, "Door Delivery City . . . . . . ", fontShipSub);
            cell2.addElement(pHeading);
            ntable1.addCell(cell2);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            pHeading = new Paragraph(8f, ":", fontShipSub);
            cell2.addElement(pHeading);
            ntable1.addCell(cell2);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            cell2.setPaddingLeft(0f);
            pHeading = new Paragraph(8f, "" + doorOriginCityZip[0].toUpperCase(), fontgreenCont);
            cell2.addElement(pHeading);
            ntable1.addCell(cell2);
        }
        if (quoteType.equals("I") && null != lclQuotePad && null != lclQuotePad.getPickupContact()
                && CommonUtils.isNotEmpty(lclQuotePad.getPickupContact().getAddress())) {
            StringBuilder doorAddress = new StringBuilder();
            doorAddress.append(lclQuotePad.getPickupContact().getAddress()).append("\n");
            if (CommonUtils.isNotEmpty(lclQuotePad.getPickupContact().getCity())) {
                doorAddress.append(lclQuotePad.getPickupContact().getCity());
            }
            if (CommonUtils.isNotEmpty(lclQuotePad.getPickupContact().getCity())
                    && CommonUtils.isNotEmpty(lclQuotePad.getPickupContact().getState())) {
                doorAddress.append(",").append(" ");
            }
            if (CommonUtils.isNotEmpty(lclQuotePad.getPickupContact().getState())) {
                doorAddress.append(lclQuotePad.getPickupContact().getState());
            }
            if (CommonUtils.isNotEmpty(lclQuotePad.getPickupContact().getZip())) {
                doorAddress.append(" ").append(lclQuotePad.getPickupContact().getZip());
            }
            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            ntable1.addCell(cell2);
            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            pHeading = new Paragraph(8f, "Door Delivery Address . . ", fontShipSub);
            cell2.addElement(pHeading);
            ntable1.addCell(cell2);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            pHeading = new Paragraph(8f, ":", fontShipSub);
            cell2.addElement(pHeading);
            ntable1.addCell(cell2);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            cell2.setPaddingLeft(0f);
            pHeading = new Paragraph(9f, "" + doorAddress.toString().toUpperCase(), fontgreenCont);
            cell2.addElement(pHeading);
            ntable1.addCell(cell2);
            cell2 = new PdfPCell();
            cell2.setBorder(0);
            ntable1.addCell(cell2);
        }
        if (quoteType.equals("I")) {
            int transitTime = 0;
            LclQuoteAcDAO lclquoteacdao = new LclQuoteAcDAO();
            List<LclQuotePiece> lclQuotePieceList = lclQuote.getLclFileNumber().getLclQuotePieceList();
            String ofimpFlag = lclquoteacdao.isChargeCodeValidate(String.valueOf(lclQuote.getFileNumberId()), "OFIMP", "");
            if ("true".equalsIgnoreCase(ofimpFlag) && lclQuote.getPortOfLoading() != null && lclQuote.getPortOfDestination() != null && null != lclQuotePieceList
                    && !lclQuotePieceList.isEmpty()) {
                LclQuotePiece lclQuotePiece = lclQuotePieceList.get(0);
                transitTime = lclquoteacdao.getTransitTime(lclQuote.getPortOfLoading().getUnLocationCode(),
                        lclQuote.getPortOfDestination().getUnLocationCode(), lclQuotePiece.getCommodityType().getCode());
            }
            if (transitTime != 0) {
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                cell2.setPadding(0f);
                cell2.setPaddingBottom(4f);
                ntable1.addCell(cell2);

                cell2 = new PdfPCell();
                cell2.setBorder(0);
                cell2.setPadding(0f);
                cell2.setPaddingBottom(4f);
                pHeading = new Paragraph(8f, "Transit Time . . . . . . . . . . . .", fontShipSub);
                cell2.addElement(pHeading);
                ntable1.addCell(cell2);

                cell2 = new PdfPCell();
                cell2.setBorder(0);
                cell2.setPadding(0f);
                pHeading = new Paragraph(8f, ":", fontShipSub);
                cell2.addElement(pHeading);
                ntable1.addCell(cell2);

                cell2 = new PdfPCell();
                cell2.setBorder(0);
                cell2.setPadding(0f);
                cell2.setColspan(4);
                cell2.setPaddingBottom(3f);
                pHeading = new Paragraph(8f, "" + transitTime, fontgreenCont);
                cell2.addElement(pHeading);
                ntable1.addCell(cell2);
            }
        }

        if ("E".equalsIgnoreCase(quoteType) && (companyCode.equalsIgnoreCase("03"))) {
            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setPadding(0f);
            cell2.setPaddingBottom(4f);
            ntable1.addCell(cell2);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setColspan(5);
            p = new Paragraph("Transit Days from Port of Loading to Port of Discharge " + "(" + polpodValues.toString().toUpperCase() + ") " + polTransitTime + " Days", fontShipSub);
            p.setLeading(5f);
            cell2.addElement(p);
            ntable1.addCell(cell2);
            LclQuoteDestinationServices destService = new LclQuoteAcDAO().getDestinationChargeWithChargeCode(lclQuote.getFileNumberId(), "ONCARR");
            if (null != destService && destService.getOncarriageTt() > 0) {
                String country[] = destService.getCountry().split("/");
                podfdValues.append(country[0]);
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                cell2.setPadding(0f);
                cell2.setPaddingBottom(4f);
                ntable1.addCell(cell2);
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                cell2.setColspan(5);
                p = new Paragraph("Transit Days from Port of Discharge to Final Destination  " + "(" + podfdValues.toString().toUpperCase() + ") " + destService.getOncarriageTt() + " Days", fontShipSub);
                p.setLeading(5f);
                cell2.addElement(p);
                ntable1.addCell(cell2);
            }
        }
        cell1.addElement(ntable1);
        ntable.addCell(cell1);
        return ntable;
    }

    public PdfPTable voyageTable() throws DocumentException {
        PdfPCell cell2 = null;
        Phrase p = null;
        Paragraph cValues = null;
        PdfPTable ntable = new PdfPTable(1);
        ntable.setWidthPercentage(100f);
        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setColspan(2);
        cell1.setBorderColor(new BaseColor(00, 51, 153));
        cell1.setBorderWidthLeft(3f);
        cell1.setBorderWidthRight(3f);
        cell1.setBorderWidthTop(0f);
        cell1.setBorderWidthBottom(0f);
        cell1.setPadding(0f);
        PdfPTable ntable1 = new PdfPTable(15);
        ntable1.setWidths(new float[]{7f, .5f, 7.75f, .5f, 4.5f, .5f, 9.75f, .75f, 3.5f, .95f, 3.5f, 1f, 4f, 1f, 4.75f});
        ntable1.setWidthPercentage(98f);
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingTop(2f);
        cell2.setBorderWidthBottom(0.06f);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        cell2.setPaddingBottom(1f);
        cValues = new Paragraph(8f, "Origin Last Rcvd", ratessubHeadingQuote);
        cValues.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
        //empty
        cell2 = new PdfPCell();
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        cell2.setBorder(0);
        ntable1.addCell(cell2);
        //cbm
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        cell2.setPadding(0f);
        cell2.setPaddingBottom(2f);
        cell2.setPaddingTop(2f);
        cell2.setBorderWidthBottom(0.06f);
        cValues = new Paragraph(8f, "Vessel", ratessubHeadingQuote);
        cValues.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
        //empty
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        ntable1.addCell(cell2);
        //kilos
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        cell2.setPadding(0f);
        cell2.setPaddingBottom(2f);
        cell2.setPaddingTop(2f);
        cell2.setBorderWidthBottom(0.06f);
        cValues = new Paragraph(8f, "SS Voy", ratessubHeadingQuote);
        cValues.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
        //empty
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        ntable1.addCell(cell2);
        //commodity
        cell2 = new PdfPCell();
//        cell2.setColspan(3);
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        cell2.setPadding(0f);
        cell2.setPaddingBottom(2f);
        cell2.setPaddingTop(2f);
        cell2.setBorderWidthBottom(0.06f);
        cValues = new Paragraph(8f, "Departure Pier", ratessubHeadingQuote);
        cValues.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
        //emptycolumn
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        ntable1.addCell(cell2);
        //ratecbm
        cell2 = new PdfPCell();
        cell2.setPadding(0f);
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        cell2.setPaddingBottom(2f);
        cell2.setPaddingTop(2f);
        cell2.setBorderWidthBottom(0.06f);
        cValues = new Paragraph(8f, "ETD", ratessubHeadingQuote);
        cValues.setAlignment(Element.ALIGN_CENTER);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
        //emptycolumn
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        ntable1.addCell(cell2);
        //ratekg
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        cell2.setPadding(0f);
        cell2.setPaddingTop(2f);
        cell2.setPaddingBottom(2f);
        cell2.setBorderWidthBottom(0.06f);
        cValues = new Paragraph(8f, "ETA", ratessubHeadingQuote);
        cValues.setAlignment(Element.ALIGN_CENTER);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
        //
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        ntable1.addCell(cell2);
//
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        cell2.setPadding(0f);
        cell2.setPaddingTop(2f);
        cell2.setPaddingBottom(2f);
        cell2.setBorderWidthBottom(0.06f);
        cValues = new Paragraph(8f, "Port-Final", ratessubHeadingQuote);
        cValues.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
        //
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        ntable1.addCell(cell2);
//
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        cell2.setPadding(0f);
        cell2.setPaddingTop(2f);
        cell2.setPaddingBottom(2f);
        cell2.setBorderWidthBottom(0.06f);
        cValues = new Paragraph(8f, "Origin-Final", ratessubHeadingQuote);
        cValues.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
        //
        cell1.addElement(ntable1);
        ntable.addCell(cell1);
        return ntable;
    }

    public PdfPTable voyageListTable() throws DocumentException, Exception {
        PdfPCell cell2 = null;
        Phrase p = null;
        Paragraph cValues = null;
        PdfPTable ntable = new PdfPTable(1);
        ntable.setWidthPercentage(100f);
        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setColspan(2);
        cell1.setBorderColor(new BaseColor(00, 51, 153));
        cell1.setBorderWidthLeft(3f);
        cell1.setBorderWidthRight(3f);
        cell1.setBorderWidthTop(0f);
        cell1.setBorderWidthBottom(0f);
        cell1.setPadding(0f);

        PdfPTable ntable1 = new PdfPTable(8);
        ntable1.setWidths(new float[]{5.75f, 6.75f, 3.95f, 8.15f, 3.75f, 4.25f, 3.5f, 3.25f});
        ntable1.setWidthPercentage(98f);
        if (lclQuote.getPortOfOrigin() != null && lclQuote.getFinalDestination() != null) {
            String cfcl = lclQuote.getLclFileNumber().getLclBookingExport().isCfcl() ? "C" : "E";
            LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
            LclBookingPlanBean lclBookingPlanBean = bookingPlanDAO.getRelay(lclQuote.getPortOfOrigin().getId(),
                    lclQuote.getFinalDestination().getId(), "N");
            List<LclBookingVoyageBean> voyageList = bookingPlanDAO.getUpComingSailingsSchedule(lclQuote.getPortOfOrigin().getId(),
                    lclQuote.getPortOfLoading().getId(), lclQuote.getPortOfDestination().getId(), lclQuote.getFinalDestination().getId(), "V", lclBookingPlanBean, cfcl);
            if (voyageList != null && voyageList.size() > 0) {
                for (int j = 0; j < voyageList.size(); j++) {
                    if (j == 5) {
                        break;
                    }
                    LclBookingVoyageBean lclBookingVoyageBean = (LclBookingVoyageBean) voyageList.get(j);
                    //Empty
                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    cell2.setPadding(0);
                    cell2.setPaddingBottom(3f);
                    cell2.setColspan(8);
                    ntable1.addCell(cell2);
                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    cell2.setPadding(0f);
                    cell2.setPaddingBottom(0.6f);
                    cell2.setPaddingTop(0.6f);
                    cValues = new Paragraph(8f, "" + DateUtils.formatDate(lclBookingVoyageBean.getOriginLrd(), "dd-MMM-yy hh:mm a"), fontCompNormalSub);
                    cValues.setAlignment(Element.ALIGN_LEFT);
                    cell2.addElement(cValues);
                    ntable1.addCell(cell2);

                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    cell2.setPadding(0f);
                    cell2.setPaddingBottom(0.6f);
                    cell2.setPaddingTop(0.6f);
                    cValues = new Paragraph(8f, "" + lclBookingVoyageBean.getVesselName(), fontCompNormalSub);
                    cValues.setAlignment(Element.ALIGN_LEFT);
                    cell2.addElement(cValues);
                    ntable1.addCell(cell2);

                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    cell2.setPadding(0f);
                    cell2.setPaddingBottom(0.6f);
                    cell2.setPaddingTop(0.6f);
                    cValues = new Paragraph(8f, "" + lclBookingVoyageBean.getSsVoyage(), fontCompNormalSub);
                    cValues.setAlignment(Element.ALIGN_LEFT);
                    cell2.addElement(cValues);
                    ntable1.addCell(cell2);

                    cell2 = new PdfPCell();
                    cell2.setPadding(0f);
                    cell2.setBorder(0);
                    cell2.setPaddingBottom(0.6f);
                    cell2.setPaddingTop(0.6f);
                    cValues = new Paragraph(8f, "" + lclBookingVoyageBean.getDepartPier(), fontCompNormalSub);
                    cValues.setAlignment(Element.ALIGN_LEFT);
                    cell2.addElement(cValues);
                    ntable1.addCell(cell2);

                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    cell2.setPadding(0f);
                    cell2.setPaddingTop(0.6f);
                    cell2.setPaddingBottom(0.6f);
                    cValues = new Paragraph(8f, "" + DateUtils.formatDate(lclBookingVoyageBean.getSailingDate(), "dd-MMM-yy"), fontCompNormalSub);
                    cValues.setAlignment(Element.ALIGN_LEFT);
                    cell2.addElement(cValues);
                    ntable1.addCell(cell2);

                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    cell2.setPadding(0f);
                    cell2.setPaddingTop(0.6f);
                    cell2.setPaddingBottom(0.6f);
                    cValues = new Paragraph(8f, "" + DateUtils.formatDate(lclBookingVoyageBean.getPodAtEta(), "dd-MMM-yy"), fontCompNormalSub);
                    cValues.setAlignment(Element.ALIGN_LEFT);
                    cell2.addElement(cValues);
                    ntable1.addCell(cell2);
                    //   Long totalpolFDTT = lclBookingVoyageBean.getTtPolPod() + lclBookingVoyageBean.getTtPooFd();
                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    cell2.setPadding(0f);
                    cell2.setPaddingTop(0.6f);
                    cell2.setPaddingBottom(0.6f);
                    cValues = new Paragraph(8f, "" + lclBookingVoyageBean.getTtPolPod() + " Days", fontCompNormalSub);
                    cValues.setAlignment(Element.ALIGN_LEFT);
                    cell2.addElement(cValues);
                    ntable1.addCell(cell2);

                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    cell2.setPadding(0f);
                    cell2.setPaddingTop(0.6f);
                    cell2.setPaddingBottom(0.6f);
                    cValues = new Paragraph(8f, "" + lclBookingVoyageBean.getTtPooFd() + " Days", fontCompNormalSub);
                    cValues.setAlignment(Element.ALIGN_LEFT);
                    cell2.addElement(cValues);
                    ntable1.addCell(cell2);
                }
            }
        }
        cell1.addElement(ntable1);
        ntable.addCell(cell1);
        return ntable;
    }

    public PdfPTable remarksTable() throws DocumentException, Exception {
        Font fontRemarkSub = FontFactory.getFont("Arial", 9f, Font.BOLD);
        Font fontRemarkNormalSub = new Font(FontFamily.HELVETICA, 9f, Font.NORMAL);
        PdfPCell cell2 = null;
        Phrase p = null;
        Paragraph pHeading = null;
        PdfPTable ntable = new PdfPTable(1);
        ntable.setWidthPercentage(100f);
        PdfPCell cell1 = new PdfPCell();
        cell1.setPadding(0f);
        cell1.setBorder(0);
        cell1.setBorderColor(new BaseColor(00, 51, 153));
        cell1.setBorderWidthLeft(3f);
        cell1.setBorderWidthRight(3f);
        cell1.setBorderWidthTop(0f);
        cell1.setPadding(0f);

        PdfPTable ntable1 = new PdfPTable(1);
        ntable1.setWidthPercentage(98.85f);
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBorderWidthTop(0.6f);
        cell2.setPaddingTop(0f);
        ntable1.addCell(cell2);
        StringBuilder fdval = new StringBuilder();
        if (lclQuote.getFinalDestination() != null) {
            if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getCountryId().getCodedesc())) {
                fdval.append(lclQuote.getFinalDestination().getCountryId().getCodedesc()).append(", ");
            }
            if (CommonUtils.isNotEmpty(lclQuote.getFinalDestination().getUnLocationName())) {
                fdval.append(lclQuote.getFinalDestination().getUnLocationName()).append(" ");
            }
        }
        //special Remarkheading
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(255, 217, 00));
        cell2.setPaddingTop(0f);
        cell2.setPaddingBottom(2f);
        cell2.setBorderColor(new BaseColor(255, 178, 0));
        cell2.setBorderWidthBottom(0.06f);
        pHeading = new Paragraph(7f, "Special Remarks for " + fdval.toString().toUpperCase(), fontRemarkSub);
        pHeading.setAlignment(Element.ALIGN_CENTER);
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(BaseColor.WHITE);
        cell2.setPaddingBottom(0.001f);
        cell2.setColspan(2);
        ntable1.addCell(cell2);
        String destinationRemarks = "";
        if (CommonUtils.isNotEmpty(specialRemarks)) {
            destinationRemarks = StringUtils.remove(specialRemarks.toString(), "FD:");
        }
        //remarks sub
        cell2 = new PdfPCell();
        cell2.setPaddingTop(1f);
        cell2.setPaddingLeft(100f);
        cell2.setPaddingBottom(3f);
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(255, 255, 00));
        if (!"".equals(destinationRemarks)) {
            pHeading = new Paragraph(9f, "" + destinationRemarks, fontRemarkNormalSub);
        } else {
            pHeading = new Paragraph(9f, "", fontRemarkNormalSub);
            cell2.setPadding(12f);
        }
        cell2.addElement(pHeading);
        ntable1.addCell(cell2);
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBackgroundColor(new BaseColor(255, 255, 00));
        cell2.setBorderWidthBottom(0.6f);
        ntable1.addCell(cell2);
        cell1.addElement(ntable1);
        ntable.addCell(cell1);
        return ntable;
    }

    public PdfPTable cargoTable() throws DocumentException, Exception {
        Font deliverFont = FontFactory.getFont("Arial", 8f, Font.BOLD);
        Font deliverFont1 = FontFactory.getFont("Arial", 8.5f, Font.NORMAL);
        Phrase p = null;
        Paragraph pdeliver = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderColor(new BaseColor(00, 51, 153));
        cell.setBorderWidthLeft(3f);
        cell.setBorderWidthRight(3f);
        cell.setPaddingTop(5f);
        cell.setPaddingBottom(3f);
        if (lclQuote.getPooDoor() == true) {
            LclQuotePad lclQuotePad = new LclQuotePadDAO().getLclQuotePadByFileNumber(lclQuote.getFileNumberId());
            String spIns = "";
            String shipperName = "";
            String shipperZip = "";
            String[] zipCode;
            String commdesc = "";
            String shipperAddress = "";
            StringBuilder contactPhone = new StringBuilder();
            if (CommonFunctions.isNotNull(lclQuotePad)) {
                if (lclQuotePad.getPickupContact() != null && lclQuotePad.getPickupContact().getCompanyName() != null) {
                    shipperName = lclQuotePad.getPickupContact().getCompanyName();
                }
                if (lclQuotePad.getPickupContact() != null && CommonUtils.isNotEmpty(lclQuotePad.getPickupContact().getZip())) {
                    shipperZip = lclQuotePad.getPickupContact().getZip();
                }
                if (CommonUtils.isNotEmpty(lclQuotePad.getPickUpCity())) {
                    zipCode = lclQuotePad.getPickUpCity().split("-");
                    shipperZip = zipCode[0];
                }
                if (lclQuotePad.getPickupContact() != null && lclQuotePad.getPickupContact().getAddress() != null) {
                    shipperAddress = lclQuotePad.getPickupContact().getAddress();
                }
                if (lclQuotePad.getPickupContact() != null && lclQuotePad.getPickupContact().getContactName() != null) {
                    contactPhone.append(lclQuotePad.getPickupContact().getContactName());
                }
                if (CommonFunctions.isNotNull(lclQuotePad.getPickupReferenceNo())) {
                    spIns = lclQuotePad.getPickupReferenceNo();
                }
                if (CommonFunctions.isNotNull(lclQuotePad.getCommodityDesc())) {
                    commdesc = lclQuotePad.getCommodityDesc();
                }
            }
            PdfPTable ntable = new PdfPTable(1);
            ntable.setWidthPercentage(98f);
            PdfPCell cell1 = new PdfPCell();
            cell1.setBorder(0);
            cell1.setBorderColor(new BaseColor(128, 00, 00));
            cell1.setBorderWidthBottom(1f);
            cell1.setBorderWidthLeft(1f);
            cell1.setBorderWidthRight(1f);
            cell1.setBorderWidthTop(1f);

            PdfPTable ntable1 = new PdfPTable(3);
            PdfPCell cell2 = null;
            ntable1.setWidths(new float[]{4f, 0.01f, 2f});
            ntable1.setWidthPercentage(100f);
            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setBorderColor(new BaseColor(128, 00, 00));
            cell2.setBackgroundColor(new BaseColor(204, 204, 204));
            cell2.setBorderWidthBottom(1f);
            cell2.setBorderWidthLeft(1f);
            cell2.setBorderWidthTop(1f);
            cell2.setBorderWidthRight(1f);
            PdfPTable cargoTable = new PdfPTable(4);
            cargoTable.setWidths(new float[]{1.45f, 0.09f, 4f, 1f});
            cargoTable.setWidthPercentage(100f);
            PdfPCell cargoCell = null;
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(3f);
            cargoCell.setColspan(4);
            pdeliver = new Paragraph(8f, "Cargo Pickup Information", deliverFont);
            pdeliver.setAlignment(Element.ALIGN_CENTER);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);
            //emptycell
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setColspan(4);
            cargoCell.setBorderColor(BaseColor.WHITE);
            cargoCell.setBorderWidthBottom(0.6f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            cargoTable.addCell(cargoCell);
            //shipper
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            pdeliver = new Paragraph(8f, "Shipper Supplier . . . .", deliverFont);
            pdeliver.setAlignment(Element.ALIGN_LEFT);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);
            //dot
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            pdeliver = new Paragraph(8f, ":", deliverFont);
            pdeliver.setAlignment(Element.ALIGN_CENTER);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);
            //values
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setColspan(2);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            pdeliver = new Paragraph(8f, "" + shipperName.toUpperCase(), deliverFont1);
            pdeliver.setAlignment(Element.ALIGN_LEFT);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);
            //address
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            pdeliver = new Paragraph(8f, "Address. . . . . . . . . . . .", deliverFont);
            pdeliver.setAlignment(Element.ALIGN_LEFT);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);
            //dot
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            pdeliver = new Paragraph(8f, ":", deliverFont);
            pdeliver.setAlignment(Element.ALIGN_CENTER);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);
            //values
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setColspan(2);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            pdeliver = new Paragraph(8f, "" + shipperAddress.toUpperCase(), deliverFont1);
            pdeliver.setAlignment(Element.ALIGN_LEFT);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);

            //phone
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            pdeliver = new Paragraph(8f, "Contact. . . . .", deliverFont);
            pdeliver.setAlignment(Element.ALIGN_LEFT);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);
            //dot
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            pdeliver = new Paragraph(8f, ":", deliverFont);
            pdeliver.setAlignment(Element.ALIGN_CENTER);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);
            //values
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            pdeliver = new Paragraph(8f, "" + contactPhone.toString().toUpperCase(), deliverFont1);
            pdeliver.setAlignment(Element.ALIGN_LEFT);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);

            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-14f);
            String zip = CommonUtils.isNotEmpty(shipperZip) ? shipperZip.toUpperCase() : "";
            Chunk c1 = new Chunk("Zip:" + zip, blackBoldFontSize7);
            c1.setBackground(BaseColor.YELLOW, 1f, 1f, 1f, 1f);
            cargoCell.addElement(c1);
            cargoTable.addCell(cargoCell);
            //special
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            pdeliver = new Paragraph(8f, "Special Instructions. .", deliverFont);
            pdeliver.setAlignment(Element.ALIGN_LEFT);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);
            //dot
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            pdeliver = new Paragraph(8f, ":", deliverFont);
            pdeliver.setAlignment(Element.ALIGN_CENTER);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);
            //values
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            cargoCell.setColspan(2);
            pdeliver = new Paragraph(8f, "" + spIns.toUpperCase(), deliverFont1);
            pdeliver.setAlignment(Element.ALIGN_LEFT);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);
            //commodity
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            pdeliver = new Paragraph(8f, "Commodity. . . . . . . . .", deliverFont);
            pdeliver.setAlignment(Element.ALIGN_LEFT);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);
            //dot
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            pdeliver = new Paragraph(8f, ":", deliverFont);
            pdeliver.setAlignment(Element.ALIGN_CENTER);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);
            //values
            cargoCell = new PdfPCell();
            cargoCell.setBorder(0);
            cargoCell.setPadding(0f);
            cargoCell.setPaddingTop(-1f);
            cargoCell.setPaddingBottom(1f);
            cargoCell.setColspan(2);
            pdeliver = new Paragraph(8f, "" + commdesc.toUpperCase(), deliverFont1);
            pdeliver.setAlignment(Element.ALIGN_LEFT);
            cargoCell.addElement(pdeliver);
            cargoTable.addCell(cargoCell);

            cell2.addElement(cargoTable);
            ntable1.addCell(cell2);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            ntable1.addCell(cell2);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setBorderColor(new BaseColor(128, 00, 00));
            cell2.setBorderWidthBottom(1f);
            cell2.setBorderWidthRight(1f);
            cell2.setBorderWidthLeft(1f);
            cell2.setBorderWidthTop(1f);
            cell2.setBackgroundColor(new BaseColor(204, 204, 204));

            PdfPTable deliTable = new PdfPTable(1);
            deliTable.setWidthPercentage(100f);
            PdfPCell deliCell = null;
            deliCell = new PdfPCell();
            deliCell.setBorder(0);
            deliCell.setPaddingTop(-1f);
            deliCell.setPaddingBottom(3f);
            pdeliver = new Paragraph(8f, "Deliver Cargo To", deliverFont);
            pdeliver.setAlignment(Element.ALIGN_CENTER);
            deliCell.addElement(pdeliver);
            deliTable.addCell(deliCell);
            //emptycell
            deliCell = new PdfPCell();
            deliCell.setBorder(0);
            deliCell.setBorderColor(BaseColor.WHITE);
            deliCell.setBorderWidthBottom(0.6f);
            deliCell.setPaddingTop(-1f);
            deliCell.setPaddingBottom(1f);
            deliTable.addCell(deliCell);
            //deliver values
            deliCell = new PdfPCell();
            deliCell.setBorder(0);
            deliCell.setPaddingTop(-1f);
            deliCell.setPaddingBottom(3f);
            if (lclQuote.getPooWhseContact() != null) {
                StringBuilder deliverCargo = new StringBuilder();
                if (CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getCompanyName())) {
                    deliverCargo.append(lclQuote.getPooWhseContact().getCompanyName()).append("-");
                }
                if (null != lclQuote.getPooWhseContact().getWarehouse() && CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getWarehouse().getWarehouseNo())) {
                    deliverCargo.append(lclQuote.getPooWhseContact().getWarehouse().getWarehouseNo()).append("\n");
                }
                if (CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getAddress())) {
                    deliverCargo.append(lclQuote.getPooWhseContact().getAddress()).append("\n");
                }
                if (CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getCity())) {
                    deliverCargo.append(lclQuote.getPooWhseContact().getCity()).append(",");
                }
                if (CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getState())) {
                    deliverCargo.append(lclQuote.getPooWhseContact().getState()).append("  ");
                }
                if (CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getZip())) {
                    deliverCargo.append(lclQuote.getPooWhseContact().getZip()).append("\n");
                }
                if (CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getPhone1())) {
                    deliverCargo.append("Phone:").append(lclQuote.getPooWhseContact().getPhone1());
                }
                pdeliver = new Paragraph(9f, "" + deliverCargo.toString(), deliverFont1);
            } else {
                pdeliver = new Paragraph(9f, "", deliverFont1);
            }
            pdeliver.setAlignment(Element.ALIGN_LEFT);
            deliCell.addElement(pdeliver);
            deliTable.addCell(deliCell);
            cell2.addElement(deliTable);
            ntable1.addCell(cell2);
            cell1.addElement(ntable1);
            ntable.addCell(cell1);
            cell.addElement(ntable);
        } else {
            PdfPTable ntable = new PdfPTable(1);
            ntable.setWidthPercentage(98f);
            PdfPCell cell1 = new PdfPCell();
            cell1.setBorder(0);
            cell1.setBorderColor(new BaseColor(128, 00, 00));
            cell1.setBorderWidthBottom(1f);
            cell1.setBorderWidthLeft(1f);
            cell1.setBorderWidthRight(1f);
            cell1.setBorderWidthTop(1f);

            PdfPTable ntable1 = new PdfPTable(2);
            ntable1.setWidths(new float[]{0.9f, 5.9f});
            ntable1.setWidthPercentage(100f);
            PdfPCell cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setBorderColor(new BaseColor(128, 00, 00));
            cell2.setBorderWidthBottom(1f);
            cell2.setBorderWidthLeft(1f);
            cell2.setBorderWidthTop(1f);
            cell2.setPaddingBottom(3);
            cell2.setPaddingTop(2);
            cell2.setBackgroundColor(new BaseColor(204, 204, 204));
            p = new Phrase("Deliver Cargo To:", deliverFont);
            p.setLeading(8f);
            cell2.addElement(p);
            ntable1.addCell(cell2);

            PdfPCell cell3 = new PdfPCell();
            cell3.setBorder(0);
            cell3.setBorderColor(new BaseColor(128, 00, 00));
            cell3.setBorderWidthBottom(1f);
            cell3.setBorderWidthRight(1f);
            cell3.setBorderWidthTop(1f);
            cell3.setPaddingBottom(5f);
            cell3.setPaddingTop(2);
            cell3.setBackgroundColor(new BaseColor(204, 204, 204));
            if (lclQuote.getPooWhseContact() != null) {
                StringBuilder deliverCargo = new StringBuilder();
                if (CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getCompanyName())) {
                    deliverCargo.append(lclQuote.getPooWhseContact().getCompanyName()).append("-");
                }
                if (null != lclQuote.getPooWhseContact().getWarehouse() && CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getWarehouse().getWarehouseNo())) {
                    deliverCargo.append(lclQuote.getPooWhseContact().getWarehouse().getWarehouseNo()).append("\n");
                }
                if (CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getAddress())) {
                    deliverCargo.append(lclQuote.getPooWhseContact().getAddress()).append("\n");
                }
                if (CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getCity())) {
                    deliverCargo.append(lclQuote.getPooWhseContact().getCity()).append(",");
                }
                if (CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getState())) {
                    deliverCargo.append(lclQuote.getPooWhseContact().getState()).append("   ");
                }
                if (CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getZip())) {
                    deliverCargo.append(lclQuote.getPooWhseContact().getZip()).append("\n");
                }
                if (CommonUtils.isNotEmpty(lclQuote.getPooWhseContact().getPhone1())) {
                    deliverCargo.append("Phone:").append(lclQuote.getPooWhseContact().getPhone1());
                }
                pdeliver = new Paragraph(9f, "" + deliverCargo.toString(), deliverFont1);
            } else {
                pdeliver = new Paragraph(9f, "", deliverFont1);
            }
            pdeliver.setAlignment(Element.ALIGN_LEFT);

            cell3.addElement(pdeliver);
            ntable1.addCell(cell3);

            cell1.addElement(ntable1);
            ntable.addCell(cell1);
            cell.addElement(ntable);
        }

        table.addCell(cell);
        return table;
    }

    public PdfPTable ratesTable() throws DocumentException, Exception {
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        PortsDAO portsDAO = new PortsDAO();
        Ports ports = portsDAO.getByProperty("unLocationCode", lclQuote.getFinalDestination().getUnLocationCode());
        List<LclQuoteAc> formattedChargesList = null;
        PdfPCell cell2 = null;
        Phrase p = null;
        Paragraph cValues = null;
        PdfPTable ntable = new PdfPTable(1);
        ntable.setWidthPercentage(100f);
        PdfPCell cell1 = new PdfPCell();
        Double arAmount = 0.0;
        Double ofrWeightPerUnit = 0.0, ofrVolumePerUnit = 0.0, barWeightPerUnit = 0.0, barVolumePerUnit = 0.0;
        int barrelPieceCount = 0;
        List<LclQuotePiece> lclQuotationPieceList = new LclQuotePieceDAO().findByProperty("lclFileNumber.id", lclQuote.getLclFileNumber().getId());
        String moduleName = "";
        if (LCL_IMPORT_TYPE.equalsIgnoreCase(quoteType)) {
            moduleName = LCL_IMPORT;
        } else {
            moduleName = LCL_EXPORT;
        }
        List<LclQuoteAc> ocnfrtOfimplist = lclQuoteAcDAO.getOcnftOfimpList(lclQuote.getFileNumberId());
        List<LclQuoteAc> chargeList = lclQuoteAcDAO.getLclCostByFileNumberAscPdf(lclQuote.getFileNumberId(), moduleName);
        if (!ocnfrtOfimplist.isEmpty()) {
            for (LclQuoteAc lclQuoteAc : ocnfrtOfimplist) {
                if (lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRW") || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRV") || lclQuoteAc.getRatePerUnitUom().equalsIgnoreCase("FRM")) {
                    //ratecbm
                    if (lclQuoteAc.getRatePerWeightUnit() != null) {
                        ofrWeightPerUnit = lclQuoteAc.getRatePerWeightUnit().doubleValue();
                    }
                    if (lclQuoteAc.getRatePerVolumeUnit() != null) {
                        ofrVolumePerUnit = lclQuoteAc.getRatePerVolumeUnit().doubleValue();
                    }
                    break;
                }

            }
        }
        if (!chargeList.isEmpty()) {
            boolean OFBARR_Bundle = false;
            boolean TTBARR_Bundle = false;
            for (LclQuoteAc lclQuoteAc : chargeList) {
                if (lclQuoteAc.getBundleIntoOf()) {
                    String chargeCode = lclQuoteAc.getArglMapping().getChargeCode();
                    if ("OFBARR".equalsIgnoreCase(chargeCode)) {
                        OFBARR_Bundle = true;
                    }
                    if (OFBARR_Bundle) {
                        if (lclQuoteAc.getRatePerWeightUnit() != null) {
                            barWeightPerUnit = barWeightPerUnit + lclQuoteAc.getRatePerWeightUnit().doubleValue();
                        }
                        if (lclQuoteAc.getRatePerVolumeUnit() != null) {
                            barVolumePerUnit = barVolumePerUnit + lclQuoteAc.getRatePerVolumeUnit().doubleValue();
                        }
                        if ("TTBARR".equalsIgnoreCase(chargeCode)) {
                            OFBARR_Bundle = false;
                        }
                    } else if (CommonUtils.notIn(chargeCode, "OFBARR", "TTBARR")) {
                        if (lclQuoteAc.getRatePerWeightUnit() != null) {
                            ofrWeightPerUnit = ofrWeightPerUnit + lclQuoteAc.getRatePerWeightUnit().doubleValue();
                        }
                        if (lclQuoteAc.getRatePerVolumeUnit() != null) {
                            ofrVolumePerUnit = ofrVolumePerUnit + lclQuoteAc.getRatePerVolumeUnit().doubleValue();
                        }
                    }
                }
            }
        }
        cell1.setBorder(0);
        cell1.setColspan(2);
        cell1.setBorderColor(new BaseColor(00, 51, 153));
        cell1.setBorderWidthBottom(4f);
        cell1.setBorderWidthLeft(3f);
        cell1.setBorderWidthRight(3f);
        cell1.setBorderWidthTop(12f);
        cell1.setPadding(0f);

        cValues = new Paragraph(10f, "Quote Charges", mainHeadingQuote);
        cValues.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(cValues);
        PdfPTable ntable1 = new PdfPTable(14);
        ntable1.setWidths(new float[]{0.1f, 1f, 0.2f, 1f, 0.2f, 1f, 0.2f, 3f, 3f, 2f, 0.2f, 1.5f, 0.2f, 1.5f});
        ntable1.setWidthPercentage(99f);
        //empty Table
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(0.6f);
        cell2.setPaddingTop(15f);
        ntable1.addCell(cell2);

        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(0.6f);
        cell2.setPaddingTop(15f);
        cell2.setBorderWidthBottom(0.06f);
        cValues = new Paragraph(8f, "Pieces", ratessubHeadingQuote);
        cValues.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
        //empty
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        ntable1.addCell(cell2);
        //cbm
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(0.6f);
        cell2.setPaddingTop(15f);
        cell2.setBorderWidthBottom(0.06f);
        if ((ports.getEngmet() != null && ports.getEngmet().equalsIgnoreCase("M")) || "I".equalsIgnoreCase(quoteType)) {
            cValues = new Paragraph(8f, "CBM", ratessubHeadingQuote);
        } else {
            cValues = new Paragraph(8f, "CFT", ratessubHeadingQuote);
        }
        cValues.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
        //empty
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        ntable1.addCell(cell2);
        //kilos
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(0.6f);
        cell2.setPaddingTop(15f);
        cell2.setBorderWidthBottom(0.06f);
        if ((ports.getEngmet() != null && ports.getEngmet().equalsIgnoreCase("M")) || "I".equalsIgnoreCase(quoteType)) {
            cValues = new Paragraph(8f, "Kilos", ratessubHeadingQuote);
        } else {
            cValues = new Paragraph(8f, "LBS", ratessubHeadingQuote);
        }
        cValues.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
        //empty
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        ntable1.addCell(cell2);
        //commodity
        cell2 = new PdfPCell();
        cell2.setColspan(3);
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingBottom(0.6f);
        cell2.setPaddingTop(15f);
        cell2.setBorderWidthBottom(0.06f);
        cValues = new Paragraph(8f, " Commodity", ratessubHeadingQuote);
        cValues.setAlignment(Element.ALIGN_LEFT);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
        //emptycolumn
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        ntable1.addCell(cell2);
        //ratecbm
        cell2 = new PdfPCell();
        cell2.setPadding(0f);
        cell2.setBorder(0);
        cell2.setPaddingBottom(2f);
        cell2.setPaddingTop(7f);
        cell2.setBorderWidthBottom(0.06f);
        if ((ports.getEngmet() != null && ports.getEngmet().equalsIgnoreCase("M")) || "I".equalsIgnoreCase(quoteType)) {
            cValues = new Paragraph(8f, "Rate Per CBM", ratessubHeadingQuote);
        } else {
            cValues = new Paragraph(8f, "Rate Per CFT", ratessubHeadingQuote);
        }
        cValues.setAlignment(Element.ALIGN_CENTER);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
        //emptycolumn
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        ntable1.addCell(cell2);
        //ratekg
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setPaddingTop(7f);
        cell2.setPaddingBottom(3f);
        cell2.setBorderWidthBottom(0.06f);
        if ((ports.getEngmet() != null && ports.getEngmet().equalsIgnoreCase("M")) || "I".equalsIgnoreCase(quoteType)) {
            cValues = new Paragraph(8f, "Rate Per 1000kg.", ratessubHeadingQuote);
        } else {
            cValues = new Paragraph(8f, "Rate Per 100LBS.", ratessubHeadingQuote);
        }
        cValues.setAlignment(Element.ALIGN_CENTER);
        cell2.addElement(cValues);
        ntable1.addCell(cell2);
//rate values
        if (!lclQuotationPieceList.isEmpty()) {
            for (LclQuotePiece lclQuotePiece : lclQuotationPieceList) {
                if (lclQuotePiece.isIsBarrel()) {
                    barrelPieceCount += lclQuotePiece.getBookedPieceCount();
                }
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                ntable1.addCell(cell2);
                //pieces values
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                cell2.setColspan(2);
                if (lclQuotePiece.getBookedPieceCount() != null && lclQuotePiece.getPackageType().getAbbr01() != null) {
                    cValues = new Paragraph(8f, "" + lclQuotePiece.getBookedPieceCount(), blackBoldFont75F);
                    cValues.setAlignment(Element.ALIGN_CENTER);
                } else {
                    cValues = new Paragraph(8f, "", fontCompNormalSub);
                }
                cell2.addElement(cValues);
                ntable1.addCell(cell2);
                //cbm
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                if ((ports.getEngmet() != null && ports.getEngmet().equalsIgnoreCase("M")) || "I".equalsIgnoreCase(quoteType)) {
                    if (lclQuotePiece.getBookedVolumeMetric() != null) {
                        cValues = new Paragraph(8f, "" + NumberUtils.convertToThreeDecimalhash(lclQuotePiece.getBookedVolumeMetric().doubleValue()), blackBoldFont75F);
                    } else {
                        cValues = new Paragraph(8f, "", fontCompNormalSub);
                    }
                } else {
                    if (lclQuotePiece.getBookedVolumeImperial() != null) {
                        cValues = new Paragraph(8f, "" + NumberUtils.convertToThreeDecimalhash(lclQuotePiece.getBookedVolumeImperial().doubleValue()), blackBoldFont75F);
                    } else {
                        cValues = new Paragraph(8f, "", fontCompNormalSub);
                    }
                }
                cell2.addElement(cValues);
                ntable1.addCell(cell2);
                //empty
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                ntable1.addCell(cell2);

                //kilos
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                if ((ports.getEngmet() != null && ports.getEngmet().equalsIgnoreCase("M")) || "I".equalsIgnoreCase(quoteType)) {
                    if (lclQuotePiece.getBookedWeightMetric() != null) {
                        cValues = new Paragraph(8f, "" + NumberUtils.convertToThreeDecimalhash(lclQuotePiece.getBookedWeightMetric().doubleValue()), blackBoldFont75F);
                    } else {
                        cValues = new Paragraph(8f, "", blackContentBoldFont);
                    }
                } else {
                    if (lclQuotePiece.getBookedWeightImperial() != null) {
                        cValues = new Paragraph(8f, "" + NumberUtils.convertToThreeDecimalhash(lclQuotePiece.getBookedWeightImperial().doubleValue()), blackBoldFont75F);
                    } else {
                        cValues = new Paragraph(8f, "", blackContentBoldFont);
                    }
                }
                cell2.addElement(cValues);
                ntable1.addCell(cell2);
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                ntable1.addCell(cell2);
                //commodity
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                cell2.setColspan(3);
                if (lclQuotePiece.getCommodityType() != null && lclQuotePiece.getCommodityType().getCode() != null && lclQuotePiece.getCommodityType().getDescEn() != null) {
                    cValues = new Paragraph(8f, "" + lclQuotePiece.getCommodityType().getCode() + "  " + lclQuotePiece.getCommodityType().getDescEn(), blackBoldFont75F);
                    cValues.setAlignment(Element.ALIGN_LEFT);
                } else {
                    cValues = new Paragraph(8f, "", fontCompNormalSub);
                }
                cell2.addElement(cValues);
                ntable1.addCell(cell2);
//        }
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                ntable1.addCell(cell2);

                if (lclQuotePiece.isIsBarrel()) {
                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    cell2.setPadding(0f);
                    cValues = new Paragraph(8f, "", fontCompNormalSubblue);
                    cValues.setAlignment(Element.ALIGN_CENTER);
                    cell2.addElement(cValues);
                    ntable1.addCell(cell2);
                    //
                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    ntable1.addCell(cell2);
                    //ratekg
                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    cValues = new Paragraph(8f, "(Barrel)", fontCompNormalSubblue);
                    cValues.setAlignment(Element.ALIGN_CENTER);
                    cell2.addElement(cValues);
                    ntable1.addCell(cell2);
                } else {
                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    cell2.setPadding(0f);
                    cValues = new Paragraph(8f, NumberUtils.convertToTwoDecimal(ofrVolumePerUnit + barVolumePerUnit), fontCompNormalSubblue);
                    cValues.setAlignment(Element.ALIGN_CENTER);
                    cell2.addElement(cValues);
                    ntable1.addCell(cell2);
                    //
                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    ntable1.addCell(cell2);
                    //ratekg
                    cell2 = new PdfPCell();
                    cell2.setBorder(0);
                    cValues = new Paragraph(8f, NumberUtils.convertToTwoDecimal(ofrWeightPerUnit + barWeightPerUnit), fontCompNormalSubblue);
                    cValues.setAlignment(Element.ALIGN_CENTER);
                    cell2.addElement(cValues);
                    ntable1.addCell(cell2);
                }

            }

            Map<String, LclQuoteAc> ofimpMap = new LinkedHashMap();
            LclQuoteAc lclQuoteacBean = null;
            List<LclQuoteAc> ocfrtList = new ArrayList();
            if (!ocnfrtOfimplist.isEmpty()) {
                for (LclQuoteAc lclQuoteAc : ocnfrtOfimplist) {
                    if (ofimpMap.containsKey(lclQuoteAc.getRatePerUnitUom())) {
                        lclQuoteacBean = ofimpMap.get(lclQuoteAc.getRatePerUnitUom());
                        lclQuoteacBean.setRatePerUnitUom(lclQuoteacBean.getRatePerUnitUom());
                        if (lclQuoteacBean.getRatePerUnitUom().equals("FL")) {
                            lclQuoteacBean.setArAmount(new BigDecimal(lclQuoteacBean.getArAmount().doubleValue() + lclQuoteAc.getArAmount().doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
                        } else if (lclQuoteacBean.getRatePerUnitUom().equals("V")) {
                            lclQuoteacBean.setArAmount(new BigDecimal(lclQuoteacBean.getArAmount().doubleValue() + lclQuoteAc.getArAmount().doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
                            lclQuoteacBean.setRatePerVolumeUnit(new BigDecimal(lclQuoteacBean.getRatePerVolumeUnit().doubleValue() + lclQuoteAc.getRatePerVolumeUnit().doubleValue()));
                            lclQuoteacBean.setRatePerWeightUnit(new BigDecimal(lclQuoteacBean.getRatePerWeightUnit().doubleValue() + lclQuoteAc.getRatePerWeightUnit().doubleValue()));
                            lclQuoteacBean.setRateFlatMinimum(new BigDecimal(lclQuoteacBean.getRateFlatMinimum().doubleValue() + lclQuoteAc.getRateFlatMinimum().doubleValue()));
                        } else if (lclQuoteacBean.getRatePerUnitUom().equals("W")) {
                            lclQuoteacBean.setArAmount(new BigDecimal(lclQuoteacBean.getArAmount().doubleValue() + lclQuoteAc.getArAmount().doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
                            lclQuoteacBean.setRatePerVolumeUnit(new BigDecimal(lclQuoteacBean.getRatePerVolumeUnit().doubleValue() + lclQuoteAc.getRatePerVolumeUnit().doubleValue()));
                            lclQuoteacBean.setRatePerWeightUnit(new BigDecimal(lclQuoteacBean.getRatePerWeightUnit().doubleValue() + lclQuoteAc.getRatePerWeightUnit().doubleValue()));
                            lclQuoteacBean.setRateFlatMinimum(new BigDecimal(lclQuoteacBean.getRateFlatMinimum().doubleValue() + lclQuoteAc.getRateFlatMinimum().doubleValue()));
                        } else if (lclQuoteacBean.getRatePerUnitUom().equals("M")) {
                            lclQuoteacBean.setArAmount(new BigDecimal(lclQuoteacBean.getArAmount().doubleValue() + lclQuoteAc.getArAmount().doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
                            lclQuoteacBean.setRatePerVolumeUnit(new BigDecimal(lclQuoteacBean.getRatePerVolumeUnit().doubleValue() + lclQuoteAc.getRatePerVolumeUnit().doubleValue()));
                            lclQuoteacBean.setRatePerWeightUnit(new BigDecimal(lclQuoteacBean.getRatePerWeightUnit().doubleValue() + lclQuoteAc.getRatePerWeightUnit().doubleValue()));
                            lclQuoteacBean.setRateFlatMinimum(new BigDecimal(lclQuoteacBean.getRateFlatMinimum().doubleValue() + lclQuoteAc.getRateFlatMinimum().doubleValue()));
                        }
                    } else {
                        lclQuoteacBean = (LclQuoteAc) BeanUtils.cloneBean(lclQuoteAc);
                    }
                    ofimpMap.put(lclQuoteAc.getRatePerUnitUom(), lclQuoteacBean);
                }
                // ocfrtList.add(lclQuoteacBean);
            }
            Collection<LclQuoteAc> coll = ofimpMap.values();
            for (LclQuoteAc rates : coll) {
                ocfrtList.add(rates);
            }

            ocfrtList.addAll(chargeList);
            Double bundleMinchg = 0.0;
            Double flatRateMinimum = 0.0;
            Double ratePerVolumeUnit = 0.0;
            Double ratePerWeightUnit = 0.0;
            Double barrelRatePerVolumeUnit = 0.0;
            Double barrelRatePerWeightUnit = 0.0;
            Double minimum = 0.0;
            Double minimumValue = 0.0;
            Double ttcbmcftrate = 0.0;
            Double ttlbskgsrate = 0.0;
            Double ttbarrelcbmcftrate = 0.0;
            Double ttbarrellbskgsrate = 0.0;
            Double barrelBundlechg = 0.0;
            Double barrelAmount = 0.0;
            boolean is_OFBARR_Bundle = false;
            if (ocfrtList != null && ocfrtList.size() > 0 && lclQuotationPieceList != null) {
                ExportQuoteUtils exportQuoteUtils = new ExportQuoteUtils();
                if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType)) {
                    if (lclQuotationPieceList.size() == 1) {
                        formattedChargesList = exportQuoteUtils.getFormattedLabelChargesForQuote(lclQuotationPieceList, ocfrtList, ports.getEngmet(), "Yes");
                    } else {
                        formattedChargesList = exportQuoteUtils.getRolledUpChargesForQuote(lclQuotationPieceList, ocfrtList, ports.getEngmet(), "Yes");
                    }
                } else {
                    formattedChargesList = new ImportQuoteUtils().impLabelFormatChargeforPdfQuote(ocfrtList);
                }
                if (!formattedChargesList.isEmpty() && LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType)) {
                    for (LclQuoteAc lclQuoteAc : formattedChargesList) {
                        if (lclQuoteAc.getBundleIntoOf() && lclQuoteAc.getPrintOnBl()) {
                            if ("OFBARR".equalsIgnoreCase(lclQuoteAc.getArglMapping().getChargeCode())) {
                                is_OFBARR_Bundle = true;
                                break;
                            }
                        }
                    }
                    for (int i = 0; i < formattedChargesList.size(); i++) {
                        LclQuoteAc lclQuoteAc = (LclQuoteAc) formattedChargesList.get(i);
                        BigDecimal adjusmentAmt = lclQuoteAc.getAdjustmentAmount() != null ? lclQuoteAc.getAdjustmentAmount() : new BigDecimal(0.00);
                        if (lclQuoteAc.getBundleIntoOf() && lclQuoteAc.getPrintOnBl()) {
                            if (!is_OFBARR_Bundle && "TTBARR".equalsIgnoreCase(lclQuoteAc.getArglMapping().getChargeCode())) {
                                if (null != lclQuoteAc.getRolledupCharges()) {
                                    barrelBundlechg += lclQuoteAc.getRolledupCharges().doubleValue() + adjusmentAmt.doubleValue();
                                } else if (!CommonUtils.isEmpty(lclQuoteAc.getArAmount())) {
                                    barrelBundlechg += lclQuoteAc.getArAmount().doubleValue() + adjusmentAmt.doubleValue();
                                }
                                if (!CommonUtils.isEmpty(lclQuoteAc.getRatePerVolumeUnit())) {
                                    ttbarrelcbmcftrate += lclQuoteAc.getRatePerVolumeUnit().doubleValue();
                                }
                                if (!CommonUtils.isEmpty(lclQuoteAc.getRatePerWeightUnit())) {
                                    ttbarrellbskgsrate += lclQuoteAc.getRatePerWeightUnit().doubleValue();
                                }
                            } else {
                                if (!CommonUtils.isEmpty(lclQuoteAc.getRolledupCharges())) {
                                    bundleMinchg += lclQuoteAc.getRolledupCharges().doubleValue() + adjusmentAmt.doubleValue();
                                } else if (!CommonUtils.isEmpty(lclQuoteAc.getArAmount())) {
                                    bundleMinchg += lclQuoteAc.getArAmount().doubleValue() + adjusmentAmt.doubleValue();
                                }
                                if (!CommonUtils.isEmpty(lclQuoteAc.getRatePerVolumeUnit())) {
                                    ttcbmcftrate += lclQuoteAc.getRatePerVolumeUnit().doubleValue();
                                }
                                if (!CommonUtils.isEmpty(lclQuoteAc.getRatePerWeightUnit())) {
                                    ttlbskgsrate += lclQuoteAc.getRatePerWeightUnit().doubleValue();
                                }
                                if (!CommonUtils.isEmpty(lclQuoteAc.getRateFlatMinimum())) {
                                    minimumValue += lclQuoteAc.getRateFlatMinimum().doubleValue();
                                }
                            }
                        }
                        if (lclQuoteAc.getPrintOnBl()) {
                            if (!CommonUtils.isEmpty(lclQuoteAc.getRolledupCharges())) {
                                arAmount += lclQuoteAc.getRolledupCharges().doubleValue() + adjusmentAmt.doubleValue();
                            } else if (!CommonUtils.isEmpty(lclQuoteAc.getArAmount())) {
                                arAmount += lclQuoteAc.getArAmount().doubleValue() + adjusmentAmt.doubleValue();
                            }
                        }
                    }
                    for (int k = 0; k < formattedChargesList.size(); k++) {
                        LclQuoteAc lclQuoteAc = (LclQuoteAc) formattedChargesList.get(k);
                        BigDecimal adjusmentAmt = lclQuoteAc.getAdjustmentAmount() != null ? lclQuoteAc.getAdjustmentAmount() : new BigDecimal(0.00);
                        if (lclQuoteAc.getArglMapping().getChargeCode().equals(CommonConstants.OFR_CHARGECODE)) {
                            if (lclQuoteAc.getRolledupCharges() != null) {
                                flatRateMinimum = lclQuoteAc.getRolledupCharges().doubleValue() + bundleMinchg;
                                ratePerVolumeUnit = lclQuoteAc.getRatePerVolumeUnit().doubleValue() + ttcbmcftrate;
                                ratePerWeightUnit = lclQuoteAc.getRatePerWeightUnit().doubleValue() + ttlbskgsrate;
                                minimum = lclQuoteAc.getRateFlatMinimum().doubleValue() + minimumValue;
                            } else if (lclQuoteAc.getArAmount() != null) {
                                flatRateMinimum = lclQuoteAc.getArAmount().doubleValue() + bundleMinchg;
                            }
                        } else if (!is_OFBARR_Bundle && lclQuoteAc.getArglMapping().getChargeCode().equalsIgnoreCase("OFBARR")) {
                            if (null != lclQuoteAc.getRolledupCharges()) {
                                barrelAmount = lclQuoteAc.getRolledupCharges().doubleValue() + barrelBundlechg + adjusmentAmt.doubleValue();
                            } else if (lclQuoteAc.getArAmount() != null) {
                                barrelAmount = lclQuoteAc.getArAmount().doubleValue() + barrelBundlechg + adjusmentAmt.doubleValue();
                            }
                            if (!CommonUtils.isEmpty(lclQuoteAc.getRatePerVolumeUnit())) {
                                barrelRatePerVolumeUnit = lclQuoteAc.getRatePerVolumeUnit().doubleValue() + ttbarrelcbmcftrate;
                            }
                            if (!CommonUtils.isEmpty(lclQuoteAc.getRatePerWeightUnit())) {
                                barrelRatePerWeightUnit = lclQuoteAc.getRatePerWeightUnit().doubleValue() + ttbarrellbskgsrate;
                            }
                        }
                    }
                }
            }

            //empty column Space
            cell2 = new PdfPCell();
            cell2.setBorder(0);
            cell2.setColspan(14);
            cell2.setFixedHeight(10f);
            ntable1.addCell(cell2);
            //to Calculate rates
            if (formattedChargesList != null && !formattedChargesList.isEmpty()) {
                for (Object lclquoteList : formattedChargesList) {
                    LclQuoteAc lclQuoteAc = (LclQuoteAc) lclquoteList;
                    BigDecimal adjusmentAmt = lclQuoteAc.getAdjustmentAmount() != null ? lclQuoteAc.getAdjustmentAmount() : new BigDecimal(0.00);
                    if (!CommonUtils.isEmpty(lclQuoteAc.getArAmount()) && moduleName.equalsIgnoreCase("Imports")) {
                        arAmount += lclQuoteAc.getArAmount().doubleValue();
                    }
                    //empty Column
                    if (!lclQuoteAc.getBundleIntoOf() && lclQuoteAc.getPrintOnBl()) {
                        cell2 = new PdfPCell();
                        cell2.setBorder(0);
                        ntable1.addCell(cell2);
                        cell2 = new PdfPCell();
                        cell2.setBorder(0);
                        cell2.setColspan(4);
                        if (lclQuoteAc.getLabel1() != null) {
                            cValues = new Paragraph(8f, "" + lclQuoteAc.getLabel1(), contentNormalFont);
                            cValues.setAlignment(Element.ALIGN_CENTER);
                            cell2.addElement(cValues);
                        } else if (barrelPieceCount > 0 && lclQuoteAc.getArglMapping() != null && lclQuoteAc.getArglMapping().getChargeCode().equals("OFBARR")) {
                            if (barrelPieceCount == 1) {
                                cValues = new Paragraph(8f, "** 1 Barrel", contentNormalFont);
                            } else {
                                cValues = new Paragraph(8f, "** " + barrelPieceCount + " Barrels", contentNormalFont);
                            }
                            cValues.setAlignment(Element.ALIGN_CENTER);
                            cell2.addElement(cValues);
                        }
                        ntable1.addCell(cell2);

                        //bluescreenChargeCode
                        cell2 = new PdfPCell();
                        cell2.setBorder(0);
                        cValues = new Paragraph(8f, "", contentNormalFont);
                        cValues.setAlignment(Element.ALIGN_RIGHT);
                        cell2.addElement(cValues);
                        ntable1.addCell(cell2);

                        cell2 = new PdfPCell();
                        cell2.setBorder(0);
                        ntable1.addCell(cell2);
                        //ChargeCode values
                        cell2 = new PdfPCell();
                        cell2.setBorder(0);
                        String chargeDesc = null;
                        if (lclQuoteAc.getArglMapping() != null && CommonUtils.isNotEmpty(lclQuoteAc.getArglMapping().getChargeDescriptions())) {
                            chargeDesc = lclQuoteAc.getArglMapping().getChargeDescriptions();
                        } else if (lclQuoteAc.getArglMapping() != null && CommonUtils.isNotEmpty(lclQuoteAc.getArglMapping().getChargeCode())) {
                            chargeDesc = lclQuoteAc.getArglMapping().getChargeCode();
                        }
                        cValues = new Paragraph(8f, "" + chargeDesc, contentNormalFont);
                        cell2.addElement(cValues);
                        ntable1.addCell(cell2);

                        //Amount
                        cell2 = new PdfPCell();
                        cell2.setBorder(0);
                        if (lclQuoteAc.getArAmount() != null) {
                            if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType) && lclQuoteAc.getArglMapping().getChargeCode().equals(CommonConstants.OFR_CHARGECODE)) {
                                Double arAmt = flatRateMinimum + adjusmentAmt.doubleValue();
                                cValues = new Paragraph(8f, "" + NumberUtils.convertToTwoDecimal(arAmt), blackBoldFont75F);
                            } else if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType) && lclQuoteAc.getArglMapping().getChargeCode().equals("OFBARR")) {
                                cValues = new Paragraph(8f, "" + NumberUtils.convertToTwoDecimal(barrelAmount), blackBoldFont75F);
                            } else {
                                if (lclQuoteAc.getRolledupCharges() != null) {
                                    Double arAmt = lclQuoteAc.getRolledupCharges().doubleValue() + adjusmentAmt.doubleValue();
                                    cValues = new Paragraph(8f, "" + NumberUtils.convertToTwoDecimal(arAmt), blackBoldFont75F);
                                } else if (lclQuoteAc.getArAmount() != null) {
                                    Double arAmt = lclQuoteAc.getArAmount().doubleValue() + adjusmentAmt.doubleValue();
                                    cValues = new Paragraph(8f, "" + NumberUtils.convertToTwoDecimal(arAmt), blackBoldFont75F);
                                }
                            }
                            cValues.setAlignment(Element.ALIGN_RIGHT);
                            cell2.addElement(cValues);
                        }
                        ntable1.addCell(cell2);

                        cell2 = new PdfPCell();
                        cell2.setBorder(0);
                        cell2.setColspan(5);
                        if (lclQuoteAc.getLabel2() != null) {
                            if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType) && lclQuoteAc.getArglMapping().getChargeCode().equals(CommonConstants.OFR_CHARGECODE) && lclQuotationPieceList.size() == 1) {
                                if (ports.getEngmet().equalsIgnoreCase("E")) {
                                    cValues = new Paragraph(8f, "($" + NumberUtils.convertToTwoDecimal(ratePerVolumeUnit)
                                            + " CFT " + NumberUtils.convertToTwoDecimal(ratePerWeightUnit) + "/100.00 LBS,$" + NumberUtils.convertToTwoDecimal(minimum) + " MINIMUM)", contentNormalFont);
                                } else {
                                    cValues = new Paragraph(8f, "($" + NumberUtils.convertToTwoDecimal(ratePerVolumeUnit)
                                            + " CBM " + NumberUtils.convertToTwoDecimal(ratePerWeightUnit) + "/1000.00 KGS,$" + NumberUtils.convertToTwoDecimal(minimum) + " MINIMUM)", contentNormalFont);
                                }
                            } else if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType) && lclQuoteAc.getArglMapping().getChargeCode().equals("OFBARR")) {
                                if (barrelRatePerVolumeUnit > 0.00) {
                                    cValues = new Paragraph(8f, "$" + NumberUtils.convertToTwoDecimal(barrelRatePerVolumeUnit) + " PER BARREL.", contentNormalFont);
                                } else if (barrelRatePerWeightUnit > 0.00) {
                                    cValues = new Paragraph(8f, "$" + NumberUtils.convertToTwoDecimal(barrelRatePerWeightUnit) + " PER BARREL.", contentNormalFont);
                                }
                            } else {
                                if (lclQuoteAc.getArglMapping().getChargeCode().equalsIgnoreCase(CHARGE_CODE_DOOR)) {
                                    cValues = new Paragraph(8f, "" + lclQuoteAc.getLabel2() + " (Subject to Change)", contentNormalFont);
                                } else {
                                    cValues = new Paragraph(8f, "" + lclQuoteAc.getLabel2(), contentNormalFont);

                                }
                            }
                        } else {
                            cValues = new Paragraph(8f, "", contentNormalFont);
                        }
                        cValues.setAlignment(Element.ALIGN_LEFT);
                        cell2.addElement(cValues);
                        ntable1.addCell(cell2);

                    }
                }
                Font deliverFont = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
                //Total Hotcode
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                cell2.setColspan(8);
                cell2.setPaddingRight(-54f);
                cValues = new Paragraph(8f, " Total($-USD)", deliverFont);
                cValues.setAlignment(Element.ALIGN_RIGHT);
                cell2.addElement(cValues);
                ntable1.addCell(cell2);
                //total values dispaly
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                cell2.setBorderWidthTop(0.06f);
                cValues = new Paragraph(8f, "" + NumberUtils.convertToTwoDecimal(arAmount), blackBoldFont75F);
                cValues.setAlignment(Element.ALIGN_RIGHT);
                cell2.addElement(cValues);
                ntable1.addCell(cell2);
                //Prepaid Collect Both
                cell2 = new PdfPCell();
                cell2.setBorder(0);
                if (lclQuote.getBillingType().equalsIgnoreCase("P")) {
                    cValues = new Paragraph(8f, "Prepaid", blackBoldFont75F);
                    cValues.setAlignment(Element.ALIGN_LEFT);
                    cell2.addElement(cValues);
                } else if (lclQuote.getBillingType().equalsIgnoreCase("C")) {
                    cValues = new Paragraph(8f, "Collect", blackBoldFont75F);
                    cValues.setAlignment(Element.ALIGN_LEFT);
                    cell2.addElement(cValues);
                } else {
                    cValues = new Paragraph(8f, "Both", blackBoldFont75F);
                    cValues.setAlignment(Element.ALIGN_LEFT);
                    cell2.addElement(cValues);
                }
                ntable1.addCell(cell2);

                cell2 = new PdfPCell();
                cell2.setBorder(0);
                cell2.setColspan(4);
                ntable1.addCell(cell2);
            }
            String companyCode = new SystemRulesDAO().getSystemRuleNameByRuleCode("CompanyCode");
            if (companyCode.equalsIgnoreCase("02")) {
                Font rulesFont = FontFactory.getFont("Arial", 8f, Font.BOLD);
                cell2 = new PdfPCell();
                cell2.setColspan(14);
                cell2.setBackgroundColor(new BaseColor(255, 255, 00));
                Paragraph pHeading = null;
                pHeading = new Paragraph(5f, "*We suggest that all cargo be insured.If you dont't have your own insurance,you can take insurance through O.T.I.Cargo,Inc.", rulesFont);
                cell2.addElement(pHeading);
                ntable1.addCell(cell2);
                cell2 = new PdfPCell();
                cell2.setColspan(14);
                cell2.setBorder(0);
                cell2.setPaddingBottom(2f);
                ntable1.addCell(cell2);
            } else {
                cell2 = new PdfPCell();
                cell2.setColspan(14);
                cell2.setBorder(0);
                cell2.setPaddingBottom(6f);
                ntable1.addCell(cell2);
            }

            if (commodityflag) {
                Font bodyboldFont = FontFactory.getFont("Arial", 7.5f, Font.BOLD);
                //empty cell
                cell2 = new PdfPCell();
                cell2.setColspan(14);
                cell2.setBorder(0);
                cell2.setFixedHeight(10f);
                ntable1.addCell(cell2);
                //empty cell
                cell2 = new PdfPCell();
                cell2.setColspan(4);
                cell2.setBorder(0);
                ntable1.addCell(cell2);
                cell2 = new PdfPCell();
                cell2.setColspan(7);
                //cell2.setBorder(0);
                cell2.setPadding(0f);
                cell2.setPaddingTop(-1f);
                cell2.setPaddingBottom(3f);
                cValues = new Paragraph(9f, "This rate quote is based on minimum weight or measure.\nPLEASE CONTACT OUR OFFICE with complete information in order to receive an accurate quote.", bodyboldFont);
                cValues.setAlignment(Element.ALIGN_CENTER);
                cell2.addElement(cValues);
                ntable1.addCell(cell2);
                cell2 = new PdfPCell();
                cell2.setColspan(3);
                cell2.setBorder(0);
                ntable1.addCell(cell2);
                //empty cell
                cell2 = new PdfPCell();
                cell2.setColspan(14);
                cell2.setBorder(0);
                cell2.setFixedHeight(7f);
                ntable1.addCell(cell2);
            }
        } else {
            cell1.setPaddingBottom(10f);
        }
        cell1.addElement(ntable1);
        ntable.addCell(cell1);

        return ntable;
    }

    public PdfPTable emptyTable() throws Exception {
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setFixedHeight(20f);
        table.addCell(cell);
        return table;
    }

    public PdfPTable dispalyContentTable(String contextPath) throws DocumentException, IOException, Exception {
        String comment1 = !LQ.isEmpty() ? LQ.get(0) : "";
        LCLPortConfigurationDAO lCLPortConfigurationDAO = new LCLPortConfigurationDAO();
        //Font displayFont = FontFactory.getFont("Cambria (Headings)", 8.5f, Font.ITALIC, new BaseColor(00, 102, 12));
        //BaseFont base = BaseFont.createFont("c:/windows/fonts/Cambria.ttf", new BaseColor(00, 102, 12));
        BaseFont palationRomanBase = BaseFont.createFont(contextPath + "/ttf/palabi.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font palatinoRomanSmallFont = new Font(palationRomanBase, 8.5f, Font.ITALIC, new BaseColor(00, 102, 12));
        Paragraph pContent = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        if (lclQuote.getPortOfDestination() != null && lclQuote.getPortOfDestination().getUnLocationCode() != null) {
            String remarks[] = lCLPortConfigurationDAO.lclDefaultDestinationImportRemarks(lclQuote.getPortOfDestination().getUnLocationCode());
            if (remarks[2] != null) {
                pContent = new Paragraph(10f, "" + remarks[2], boldRedFont);
                pContent.setAlignment(Element.ALIGN_CENTER);
                pContent.setSpacingAfter(20f);
                cell.addElement(pContent);
            } else {
                emptyTable();
            }
        }
        pContent = new Paragraph(8f, "" + comment1, palatinoRomanSmallFont);
        pContent.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pContent);
        table.addCell(cell);
        return table;
    }

    public PdfPTable advisteTable() throws DocumentException {
        PdfPCell cell2 = null;
        Phrase p = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setColspan(2);
        cell1.setBorderColor(new BaseColor(153, 00, 00));
        cell1.setBorderWidthLeft(3f);
        cell1.setBorderWidthRight(3f);
        cell1.setBorderWidthTop(12f);
        cell1.setPadding(0f);
        Paragraph pHeading = null;
        pHeading = new Paragraph(10f, "Important Disclosures", mainHeadingQuote);
        pHeading.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(pHeading);
        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100f);
        cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setPadding(0f);
        cell2.setBorderColor(new BaseColor(153, 00, 00));
        cell2.setBorderWidthLeft(1f);
        cell2.setBorderWidthTop(1f);
        cell2.setPaddingLeft(10f);
        cell2.setPaddingBottom(3f);
        table1.addCell(cell2);
        cell1.addElement(table1);
        table.addCell(cell1);
        return table;
    }

    public PdfPTable samplesTable() throws DocumentException {
        String comment2 = !LQ.isEmpty() && LQ.size() >= 2 ? LQ.get(1) : "";
        Font deliverFont = FontFactory.getFont("Arial", 8.5f, Font.BOLD);
        Paragraph p = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderColor(new BaseColor(153, 00, 00));
        cell.setBorderWidthLeft(3f);
        cell.setBorderWidthRight(3f);

        cell.setPaddingTop(5f);
        PdfPTable ntable = new PdfPTable(1);
        ntable.setWidthPercentage(98f);
        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setBorderColor(new BaseColor(128, 00, 00));
        cell1.setBorderWidthBottom(1f);
        cell1.setBorderWidthLeft(1f);
        cell1.setBorderWidthRight(1f);
        cell1.setBorderWidthTop(1f);

        PdfPTable ntable1 = new PdfPTable(1);
        ntable1.setWidthPercentage(100f);
        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(0);
        cell2.setBorderColor(new BaseColor(128, 00, 00));
        cell2.setBorderWidthBottom(1f);
        cell2.setBorderWidthLeft(1f);
        cell2.setBorderWidthTop(1f);
        cell2.setBorderWidthRight(1f);
        cell2.setPaddingBottom(3f);
        cell2.setPaddingTop(3f);
        cell2.setBackgroundColor(new BaseColor(204, 204, 204));
        p = new Paragraph(7f, "" + comment2, deliverFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell2.addElement(p);
        ntable1.addCell(cell2);

        cell1.addElement(ntable1);
        ntable.addCell(cell1);
        cell.addElement(ntable);
        table.addCell(cell);
        return table;
    }

    public PdfPTable rulesTable() throws Exception {
        Font rulesFont = FontFactory.getFont("Arial", 8f, Font.BOLD);
        Font redFont = FontFactory.getFont("Arial", 8f, Font.BOLD, BaseColor.RED);
        Phrase p = null;
        PdfPCell nCell = null;
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(5f);
        cell.setBorderColor(new BaseColor(153, 00, 00));
        cell.setBorderWidthLeft(3f);
        cell.setBorderWidthRight(3f);

        PdfPTable nTable = new PdfPTable(1);
        nTable.setWidthPercentage(98f);
        nCell = new PdfPCell();
        nCell.setBorder(0);

        if (lclQuote.getRateType().equalsIgnoreCase("C")) {
            p = new Phrase("*** These rates are non-commissionable. ***", redFont);
            p.setLeading(7f);
        }
        nCell.addElement(p);
        nTable.addCell(nCell);

        for (int index = 2; index < LQ.size(); index++) {
            nCell = new PdfPCell();
            nCell.setBorder(0);
            cell.setPaddingBottom(3f);
            p = new Phrase(LQ.get(index), rulesFont);
            p.setLeading(8f);
            nCell.addElement(p);
            nTable.addCell(nCell);
        }
        cell.addElement(nTable);
        table.addCell(cell);
        return table;
    }

    public PdfPTable importCommentsTable() throws Exception {
        LclQuoteAcDAO lclQuoteAcDAO = new LclQuoteAcDAO();
        Font rulesFont = FontFactory.getFont("Arial", 8f, Font.BOLD);
        Paragraph p = null;
        PdfPCell nCell = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(5f);
        cell.setBorderColor(new BaseColor(153, 00, 00));
        cell.setBorderWidthLeft(3f);
        cell.setBorderWidthRight(3f);

        PdfPTable nTable = new PdfPTable(1);
        nTable.setWidthPercentage(98f);

        nCell = new PdfPCell();
        nCell.setBorder(0);
        cell.setPaddingBottom(3f);
        p = new Paragraph(13f, !ILQ.isEmpty() ? ILQ.get(0) : "", rulesFont);
        p.setAlignment(Element.ALIGN_CENTER);
        nCell.addElement(p);
        nTable.addCell(nCell);

        nCell = new PdfPCell();
        nCell.setBorder(0);
        cell.setPaddingBottom(3f);
        p = new Paragraph(10f, !ILQ.isEmpty() && ILQ.size() >= 2 ? ILQ.get(1) : "", rulesFont);
        nCell.addElement(p);
        nTable.addCell(nCell);

        nCell = new PdfPCell();
        nCell.setBorder(0);
        cell.setPaddingBottom(3f);
        String exworks = lclQuoteAcDAO.isChargeCodeValidate(String.valueOf(lclQuote.getLclFileNumber().getId()), CHARGE_CODE_EXWORK, "");
        if ("true".equalsIgnoreCase(exworks)) {
            p = new Paragraph(10f, "Quotation based on Ex Works terms.", rulesFont);
        } else {
            StringBuilder commentsValues = new StringBuilder();
            if (null != lclQuote.getPortOfOrigin() && CommonUtils.isNotEmpty(lclQuote.getPortOfOrigin().getUnLocationName())) {
                commentsValues.append(lclQuote.getPortOfOrigin().getUnLocationName()).append(" (").append(lclQuote.getPortOfOrigin().getUnLocationCode()).append(")");
            } else if (null != lclQuote.getPortOfLoading() && CommonUtils.isNotEmpty(lclQuote.getPortOfLoading().getUnLocationName())) {
                commentsValues.append(lclQuote.getPortOfLoading().getUnLocationName()).append(" (").append(lclQuote.getPortOfLoading().getUnLocationCode()).append(")");
            }
            p = new Paragraph(10f, !ILQ.isEmpty() && ILQ.size() >= 3 ? ILQ.get(2).replace("FUZHOU (CNFOC)", commentsValues.toString()) : "", rulesFont);
        }
        nCell.addElement(p);
        nTable.addCell(nCell);

        nCell = new PdfPCell();
        nCell.setBorder(0);
        cell.setPaddingBottom(3f);
        p = new Paragraph(10f, !ILQ.isEmpty() && ILQ.size() >= 4 ? ILQ.get(3) : "", rulesFont);
        nCell.addElement(p);
        nTable.addCell(nCell);
        String isDoorDeliv = lclQuoteAcDAO.isChargeCodeValidate(String.valueOf(lclQuote.getLclFileNumber().getId()), CHARGE_CODE_DOOR, "");
        if (!"true".equalsIgnoreCase(isDoorDeliv)) {
            nCell = new PdfPCell();
            nCell.setBorder(0);
            cell.setPaddingBottom(3f);
            p = new Paragraph(10f, !ILQ.isEmpty() && ILQ.size() >= 5 ? ILQ.get(4) : "", rulesFont);
            nCell.addElement(p);
            nTable.addCell(nCell);
        }
        nCell = new PdfPCell();
        nCell.setBorder(0);
        cell.setPaddingBottom(3f);
        p = new Paragraph(7f, "");
        nCell.addElement(p);
        nTable.addCell(nCell);

        for (int index = 4; index < ILQ.size(); index++) {
            nCell = new PdfPCell();
            nCell.setBorder(0);
            cell.setPaddingBottom(3f);
            p = new Paragraph(10f, ILQ.get(index), rulesFont);
            nCell.addElement(p);
            nTable.addCell(nCell);
        }

        LCLPortConfigurationDAO lclPortConfigurationDAO = new LCLPortConfigurationDAO();
        if (lclQuote.getPortOfDestination() != null && lclQuote.getPortOfDestination().getUnLocationCode() != null) {
            String remarks[] = lclPortConfigurationDAO.lclDefaultDestinationImportRemarks(lclQuote.getPortOfDestination().getUnLocationCode());
            if (lclQuote.getPortOfDestination().getUnLocationCode().equals("PRSJU")) {
                nCell = new PdfPCell();
                nCell.setBorder(0);
                cell.setPaddingBottom(3f);
                p = new Paragraph(10f, "Special Remarks for San Juan, Puerto Rico".toUpperCase(), blueNormalFont8);
                p.setAlignment(Element.ALIGN_CENTER);
                nCell.addElement(p);
                p = new Paragraph(10f, "" + remarks[1], rulesFont);
                p.setAlignment(Element.ALIGN_LEFT);
                nCell.addElement(p);
                p = new Paragraph(10f, "" + remarks[0], rulesFont);
                p.setAlignment(Element.ALIGN_LEFT);
                nCell.addElement(p);
                nTable.addCell(nCell);
            }
        }
        cell.addElement(nTable);
        table.addCell(cell);
        return table;
    }

    public PdfPTable advisoryTable() throws DocumentException, Exception {
        String LA001 = "";
        String LA002 = "";
        String LA100 = "";
        Iterator quoteCommentsIterator = new GenericCodeDAO().getLclPrintComments(39, "LA0");
        while (quoteCommentsIterator.hasNext()) {
            Object[] row = (Object[]) quoteCommentsIterator.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null) {
                if ("LA001".equalsIgnoreCase(code)) {
                    LA001 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("LA002".equalsIgnoreCase(code)) {
                    LA002 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                }
            }
        }
        int rowsize = 0;
        List advisoryCommentsList = null;
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType)) {
            advisoryCommentsList = new GenericCodeDAO().getLclPrintComment(39, "LA1");
            rowsize = (null != advisoryCommentsList && !advisoryCommentsList.isEmpty()) ? advisoryCommentsList.size() + 2 : 3;
        }
        String comment9 = LA001 != null ? LA001 : "";
        String comment10 = LA002 != null ? LA002 : "";
        String comment11 = LA100 != null ? LA100 : "";
        Paragraph pValue = null;
        PdfPCell nCell = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderColor(new BaseColor(153, 00, 00));
        cell.setBorderWidthLeft(3f);
        cell.setBorderWidthRight(3f);
        cell.setBorderWidthBottom(7f);

        PdfPTable nTable = new PdfPTable(2);
        nTable.setWidths(new float[]{1, 4f});
        nTable.setWidthPercentage(99f);

        nCell = new PdfPCell();
        nCell.setBorder(0);
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType)) {
            nCell.setRowspan(rowsize);
            rowsize = rowsize != 0 ? rowsize * 4 : 5;
            pValue = new Paragraph(25f + rowsize, "**ADVISORY**", contenthBoldFont);
        } else {
            nCell.setRowspan(2);
            pValue = new Paragraph(15f, "**ADVISORY**", contenthBoldFont);
        }
        nCell.setBackgroundColor(BaseColor.YELLOW);
        nCell.setBorderWidthTop(0.06f);
        nCell.addElement(pValue);
        nTable.addCell(nCell);

        nCell = new PdfPCell();
        nCell.setBorder(0);
        nCell.setBorderWidthTop(0.06f);
        nCell.setBackgroundColor(new BaseColor(255, 255, 00));
        if (!"".equals(comment9)) {
            pValue = new Paragraph(8f, "" + comment9, contentNormalFont);
            pValue.add(new Chunk(" " + this.companyWebsite + ".", blueNormalFont8));
        } else {
            pValue = new Paragraph(8f, "", contentNormalFont);
            nCell.setPadding(9f);
        }
        nCell.addElement(pValue);
        nTable.addCell(nCell);
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(quoteType)) {
            nCell = new PdfPCell();
            nCell.setBorder(0);
            nCell.setBackgroundColor(new BaseColor(255, 255, 00));
            if (!"".equals(comment10)) {
                pValue = new Paragraph(7f, "" + comment10, contentNormalFont);
            } else {
                pValue = new Paragraph(8f, "", contentNormalFont);
                nCell.setPadding(9f);
            }
            nCell.addElement(pValue);
            nTable.addCell(nCell);

            if (null != advisoryCommentsList && !advisoryCommentsList.isEmpty()) {
                for (Object row : advisoryCommentsList) {
                    Object[] col = (Object[]) row;
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    nCell.setBackgroundColor(new BaseColor(255, 255, 00));
                    pValue = new Paragraph(7f, "" + col[1].toString(), contentNormalFont);
                    nCell.addElement(pValue);
                    nTable.addCell(nCell);
                }
            } else {
                nCell = new PdfPCell();
                nCell.setBorder(0);
                nCell.setBackgroundColor(new BaseColor(255, 255, 00));
                nTable.addCell(nCell);
            }
            nCell = new PdfPCell();
            nCell.setBorder(0);
            nCell.setBackgroundColor(new BaseColor(255, 255, 00));
            nTable.addCell(nCell);
        }
        nCell = new PdfPCell();
        nCell.setBorder(0);
        nCell.setBackgroundColor(new BaseColor(255, 255, 242));
        nCell.setPaddingBottom(6f);
        nTable.addCell(nCell);

        cell.addElement(nTable);
        table.addCell(cell);

        return table;

    }
}
