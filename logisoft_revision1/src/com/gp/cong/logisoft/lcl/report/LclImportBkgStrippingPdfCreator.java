/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingImportAms;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsImportsDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author Logiware
 */
public class LclImportBkgStrippingPdfCreator extends LclReportFormatMethods {

    private static final Logger log = Logger.getLogger(LclImportBkgStrippingPdfCreator.class);
    private LclUnitSs lclUnitSs;
    boolean flag = false;
    Document document = null;
    HttpServletRequest request;

    public LclImportBkgStrippingPdfCreator(LclUnitSs lclUnitSs, HttpServletRequest request) {
        this.lclUnitSs = lclUnitSs;
        this.request = request;
    }

    class TableHeader extends PdfPageEventHelper {

        String header;
        PdfTemplate total;

        public void setHeader(String header) {
            this.header = header;
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(30, 16);
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                PdfPTable footerTable = freightTable();
                footerTable.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
                footerTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
            } catch (Exception e) {
                log.info("onEndPage failed on " + new Date(), e);
            }
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                document.add(headerTable(request));
            } catch (Exception ex) {
                log.info("Exception on class LclImportBkgStrippingPdfCreator in method onStartPage" + new Date(), ex);
            }
        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);
        }
    }

    private void init(String outputFileName) throws Exception {
        document = new Document(PageSize.A4.rotate());
        document.setMargins(5, 5, 8, 20);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        LclImportBkgStrippingPdfCreator.TableHeader event = new LclImportBkgStrippingPdfCreator.TableHeader();
        pdfWriter.setPageEvent(event);
        document.open();
    }

    public void createImportBLPdf(Document document) throws Exception {
        document.add(tallyDetailsTable());
        document.add(totalsTable());
    }

    public PdfPTable headerTable(HttpServletRequest request) throws Exception {
        Font blackNormalFont7 = FontFactory.getFont("COURIER", 10f, Font.NORMAL);
        Paragraph p = null;
        String brand ="";
        String companyName ="";
        User user = (User) request.getSession().getAttribute("loginuser");
        LclUnitSsImports lclUnitSsImports = new LclUnitSsImportsDAO().getLclUnitSSImportsByHeader(lclUnitSs.getLclUnit().getId(), lclUnitSs.getLclSsHeader().getId());
        LclSsDetail lclSsDetail = new LclSsDetailDAO().findByTransMode(lclUnitSs.getLclSsHeader().getId(), "V");
        LclSsHeader lclSsHeader = new LclSsHeaderDAO().findById(lclUnitSs.getLclSsHeader().getId());
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100f);
        headerTable.setWidths(new float[]{2.5f, 4.95f, 2.5f});
        if((lclUnitSs !=null && lclUnitSs.getLclSsHeader() !=null && !lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().isEmpty()) 
                && lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno() !=null) {
        brand =new TradingPartnerDAO().getBusinessUnit(lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno());
        }
        if (CommonUtils.isNotEmpty(brand)) {
            if ("ECI".equalsIgnoreCase(brand)) {
                companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
            } else if ("OTI".equalsIgnoreCase(brand)) {
                companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
            } else {
                companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");

            }
        }
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, "DATE   " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), blackNormalFont7);
        cell.addElement(p);
        p = new Paragraph(10f, "TIME   " + DateUtils.formatStringDateToTimeTT(new Date()), blackNormalFont7);
        cell.addElement(p);
        p = new Paragraph(10f, "REQUESTED BY - " + user.getLoginName().toUpperCase(), blackNormalFont7);
        cell.addElement(p);
        p = new Paragraph(15f, "VESSEL : " + lclSsDetail.getSpReferenceName(), blackNormalFont7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(15f, "CONTAINER #:" + lclUnitSs.getLclUnit().getUnitNo(), blackNormalFont7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setPaddingBottom(25f);
        headerTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, companyName, blackNormalFont7);
        cell.addElement(p);
        if (flag == false) {
            p = new Paragraph(10f, "STRIPPING TALLY", blackNormalFont7);
        } else {
            p = new Paragraph(10f, "TALLY TOTALS PAGE", blackNormalFont7);
        }
        cell.addElement(p);
        StringBuilder voyage = new StringBuilder();
        if (lclSsHeader.getBillingTerminal() != null && lclSsHeader.getBillingTerminal().getTrmnum() != null) {
            voyage.append(lclSsHeader.getBillingTerminal().getTrmnum());
        }
        if (lclSsHeader.getOrigin() != null && lclSsHeader.getOrigin().getUnLocationCode() != null) {
            voyage.append("-").append(lclSsHeader.getOrigin().getUnLocationCode());
        }
        if (lclSsHeader.getDestination() != null && lclSsHeader.getDestination().getUnLocationCode() != null) {
            voyage.append("-").append(lclSsHeader.getDestination().getUnLocationCode());
        }
        if (lclSsHeader.getScheduleNo() != null) {
            voyage.append("-").append(lclSsHeader.getScheduleNo());
        }
        p = new Paragraph(10f, "" + lclUnitSsImports.getOriginAcctNo().getAccountName(), blackNormalFont7);
        cell.addElement(p);
        p = new Paragraph(15f, "VOYAGE : " + voyage.toString(), blackNormalFont7);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(15f, "SEAL #:____________ CHECKER:____________ DATE UNLOADED:____________", blackNormalFont7);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        headerTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, "PAGE  " + pdfWriter.getPageNumber(), blackNormalFont7);
        cell.addElement(p);
        p = new Paragraph(10f, "SEQ#  " + pdfWriter.getPageNumber(), blackNormalFont7);
        cell.addElement(p);
        headerTable.addCell(cell);

        if (flag == false) {
            cell = new PdfPCell();
            cell.setColspan(3);
            cell.setBorder(0);
            PdfPTable htable = new PdfPTable(9);
            htable.setWidthPercentage(100.35f);
            htable.setWidths(new float[]{1.5f, .9f, .9f, 1f, 1f, 1f, .9f, 1.9f, 2.3f});
            PdfPCell hcell = new PdfPCell();
            hcell = new PdfPCell();
            hcell.setBorder(0);
            hcell.setBorderWidthLeft(0.06f);
            hcell.setBorderWidthTop(0.06f);
            hcell.setBorderWidthBottom(0.06f);
            p = new Paragraph(9f, "DESTINATION /\nB/L# \nSUB-HOUSE", blackNormalFont7);
            p.setAlignment(Element.ALIGN_CENTER);
            hcell.addElement(p);
            htable.addCell(hcell);

            hcell = new PdfPCell();
            hcell.setBorder(0);
            hcell.setBorderWidthLeft(0.06f);
            hcell.setBorderWidthTop(0.06f);
            hcell.setBorderWidthBottom(0.06f);
            p = new Paragraph(10f, "DR #", blackNormalFont7);
            p.setAlignment(Element.ALIGN_CENTER);
            hcell.addElement(p);
            htable.addCell(hcell);

            hcell = new PdfPCell();
            hcell.setBorder(0);
            hcell.setBorderWidthLeft(0.06f);
            hcell.setBorderWidthTop(0.06f);
            hcell.setBorderWidthBottom(0.06f);
            p = new Paragraph(10f, "PCS COUNT", blackNormalFont7);
            p.setAlignment(Element.ALIGN_CENTER);
            hcell.addElement(p);
            htable.addCell(hcell);

            hcell = new PdfPCell();
            hcell.setBorder(0);
            hcell.setBorderWidthLeft(0.06f);
            hcell.setBorderWidthTop(0.06f);
            hcell.setBorderWidthBottom(0.06f);
            p = new Paragraph(10f, "RECEIVED", blackNormalFont7);
            p.setAlignment(Element.ALIGN_CENTER);
            hcell.addElement(p);
            htable.addCell(hcell);

            hcell = new PdfPCell();
            hcell.setBorder(0);
            hcell.setBorderWidthLeft(0.06f);
            hcell.setBorderWidthTop(0.06f);
            hcell.setBorderWidthBottom(0.06f);
            p = new Paragraph(10f, "TALLEY", blackNormalFont7);
            p.setAlignment(Element.ALIGN_CENTER);
            hcell.addElement(p);
            htable.addCell(hcell);

            hcell = new PdfPCell();
            hcell.setBorder(0);
            hcell.setBorderWidthLeft(0.06f);
            hcell.setBorderWidthTop(0.06f);
            hcell.setBorderWidthBottom(0.06f);
            p = new Paragraph(10f, "EXCEPTIONS", blackNormalFont7);
            p.setAlignment(Element.ALIGN_CENTER);
            hcell.addElement(p);
            htable.addCell(hcell);

            hcell = new PdfPCell();
            hcell.setBorder(0);
            hcell.setBorderWidthLeft(0.06f);
            hcell.setBorderWidthTop(0.06f);
            hcell.setBorderWidthBottom(0.06f);
            p = new Paragraph(10f, "LOCATION", blackNormalFont7);
            p.setAlignment(Element.ALIGN_CENTER);
            hcell.addElement(p);
            htable.addCell(hcell);

            hcell = new PdfPCell();
            hcell.setBorder(0);
            hcell.setBorderWidthLeft(0.06f);
            hcell.setBorderWidthTop(0.06f);
            hcell.setBorderWidthBottom(0.06f);
            p = new Paragraph(10f, "MARKS & NOS", blackNormalFont7);
            p.setAlignment(Element.ALIGN_CENTER);
            hcell.addElement(p);
            htable.addCell(hcell);

            hcell = new PdfPCell();
            hcell.setBorder(0);
            hcell.setBorderWidthLeft(0.06f);
            hcell.setBorderWidthRight(0.06f);
            hcell.setBorderWidthTop(0.06f);
            hcell.setBorderWidthBottom(0.06f);
            p = new Paragraph(10f, "DESCRIPTIONS", blackNormalFont7);
            p.setAlignment(Element.ALIGN_CENTER);
            hcell.addElement(p);
            htable.addCell(hcell);
            cell.addElement(htable);
            headerTable.addCell(cell);
        }
        return headerTable;
    }

    public PdfPTable tallyDetailsTable() throws DocumentException, Exception {
        Paragraph p = null;
        int pieceCount = 0;
        String ambHouseBL = "";
        flag = false;
        Font blackNormalFont5 = FontFactory.getFont("COURIER", 10f, Font.NORMAL);
        Double cft = 0.00;
        Double cbm = 0.00;
        Double kgs = 0.00;
        Double lbs = 0.00;
        table = new PdfPTable(9);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, .9f, .9f, 1f, 1f, 1f, .9f, 1.9f, 2.3f});
        List<LclBookingPieceUnit> lclBookingPieceUnitsList = lclUnitSs.getLclBookingPieceUnitList();
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        if (lclBookingPieceUnitsList != null && lclBookingPieceUnitsList.size() > 0) {
            for (int i = 0; i < lclBookingPieceUnitsList.size(); i++) {
                LclBookingPieceUnit lclBookingPieceUnit = (LclBookingPieceUnit) lclBookingPieceUnitsList.get(i);
                LclBooking lclBooking = bookingDAO.findById(lclBookingPieceUnit.getLclBookingPiece().getLclFileNumber().getId());
                bookingDAO.getCurrentSession().evict(lclBooking);
                List<LclBookingImportAms> lclBookingImportAmsList = new LclBookingImportAmsDAO().findByProperty("lclFileNumber.id", lclBooking.getLclFileNumber().getId());
                Collections.sort(lclBookingImportAmsList, new Comparator<LclBookingImportAms>() {

                    public int compare(LclBookingImportAms o1, LclBookingImportAms o2) {
                        return o2.getId().compareTo(o1.getId());
                    }
                });
                if (lclBookingImportAmsList != null && lclBookingImportAmsList.size() > 0) {
                    for (int j = 0; j < lclBookingImportAmsList.size(); j++) {
                        LclBookingImportAms lclBookingImportAms = (LclBookingImportAms) lclBookingImportAmsList.get(j);
                        if (CommonFunctions.isNotNull(lclBookingImportAms.getAmsNo())) {
                            ambHouseBL = lclBookingImportAms.getAmsNo();
                        }
                        break;
                    }
                }
                LclBookingImport lclBookingImport = new LclBookingImportDAO().findById(lclBookingPieceUnit.getLclBookingPiece().getLclFileNumber().getId());
                if (CommonUtils.isNotEmpty(lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount())) {
                    pieceCount = lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount();
                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount() != null) {
                    pieceCount = lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount();
                }
                if (lclBookingPieceUnit.getLclBookingPiece().getActualWeightImperial() != null && lclBookingPieceUnit.getLclBookingPiece().getActualWeightImperial().doubleValue() != 0.00) {
                    lbs = lclBookingPieceUnit.getLclBookingPiece().getActualWeightImperial().doubleValue();
                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedWeightImperial() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedWeightImperial().doubleValue() != 0.00) {
                    lbs = lclBookingPieceUnit.getLclBookingPiece().getBookedWeightImperial().doubleValue();
                }
                if (lclBookingPieceUnit.getLclBookingPiece().getActualVolumeImperial() != null && lclBookingPieceUnit.getLclBookingPiece().getActualVolumeImperial().doubleValue() != 0.00) {
                    cft = lclBookingPieceUnit.getLclBookingPiece().getActualVolumeImperial().doubleValue();
                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeImperial() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeImperial().doubleValue() != 0.00) {
                    cft = lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeImperial().doubleValue();
                }
                if (lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue() != 0.00) {
                    kgs = lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue();
                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                    kgs = lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue();
                }
                if (lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric().doubleValue() != 0.00) {
                    cbm = lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric().doubleValue();
                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                    cbm = lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric().doubleValue();
                }

                StringBuilder sb = new StringBuilder();
                sb.append(lclBooking.getFinalDestination().getUnLocationName());
                if (lclBooking.getFinalDestination().getStateId() != null && lclBooking.getFinalDestination().getStateId().getCode() != null) {
                    sb.append(",").append(lclBooking.getFinalDestination().getStateId().getCode());
                } else if (lclBooking.getFinalDestination().getCountryId() != null && lclBooking.getFinalDestination().getCountryId().getCode() != null) {
                    sb.append(",").append(lclBooking.getFinalDestination().getCountryId().getCode());
                }
                /* DESTINATION /B/L#SUB-HOUSE */
                cell = new PdfPCell();
                cell.setFixedHeight(75f);
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.06f);
                cell.setBorderWidthBottom(0.06f);
                p = new Paragraph(7f, "" + sb.toString().toUpperCase(), blackNormalFont5);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                p = new Paragraph(10f, "" + ambHouseBL.toUpperCase(), blackNormalFont5);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                if (lclBookingImport != null && lclBookingImport.getSubHouseBl() != null) {
                    p = new Paragraph(10f, "" + lclBookingImport.getSubHouseBl().toUpperCase(), blackNormalFont5);
                } else {
                    p = new Paragraph(10f, "", blackNormalFont5);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);
                /* DR # */
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.06f);
                cell.setBorderWidthBottom(0.06f);
                cell.setFixedHeight(75f);
                p = new Paragraph(10f, "IMP-" + lclBookingPieceUnit.getLclBookingPiece().getLclFileNumber().getFileNumber(), blackNormalFont5);
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);
                /* PCS COUNT */
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.06f);
                cell.setBorderWidthBottom(0.06f);
                cell.setFixedHeight(75f);
                p = new Paragraph(10f, "" + pieceCount, blackNormalFont5);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);
                /* RECEIVED */
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.06f);
                cell.setBorderWidthBottom(0.06f);
                cell.setFixedHeight(75f);
                p = new Paragraph(10f, "", blackNormalFont5);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);
                /* TALLEY */
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.06f);
                cell.setBorderWidthBottom(0.06f);
                cell.setFixedHeight(75f);
                p = new Paragraph(10f, "", blackNormalFont5);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);
                /* EXCEPTIONS */
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.06f);
                cell.setBorderWidthBottom(0.06f);
                cell.setFixedHeight(75f);
                p = new Paragraph(10f, "", blackNormalFont5);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);
                /* LOCATION */
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.06f);
                cell.setBorderWidthBottom(0.06f);
                cell.setFixedHeight(75f);
                p = new Paragraph(10f, "", blackNormalFont5);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);
                /* MARKS & NOS */
                cell = new PdfPCell();
                cell.setFixedHeight(100f);
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.06f);
                cell.setBorderWidthBottom(0.06f);
                if (lclBookingPieceUnit.getLclBookingPiece() != null && CommonUtils.isNotEmpty(lclBookingPieceUnit.getLclBookingPiece().getMarkNoDesc())) {
                    p = new Paragraph(10f, "" + lclBookingPieceUnit.getLclBookingPiece().getMarkNoDesc().toUpperCase(), blackNormalFont5);
                } else {
                    p = new Paragraph(10f, "", blackNormalFont5);
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                if (CommonUtils.isNotEmpty(lclBookingPieceUnit.getLclBookingPiece().getMarkNoDesc())) {
                    p = new Paragraph(10f, " ", blackNormalFont5);
                    p.setAlignment(Element.ALIGN_LEFT);
                    cell.addElement(p);
                }
                if (null != lclBooking.getConsContact() && CommonUtils.isNotEmpty(lclBooking.getConsContact().getCompanyName())) {
                    p = new Paragraph(10f, "CONS:" + lclBooking.getConsContact().getCompanyName(), blackNormalFont5);
                } else {
                    p = new Paragraph(10f, "", blackNormalFont5);
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);
                /* DESCRIPTIONS */
                cell = new PdfPCell();
                cell.setFixedHeight(75f);
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.06f);
                cell.setBorderWidthRight(0.06f);
                cell.setBorderWidthBottom(0.06f);
                String commodityDescription = "";
                String description = "";
                if (lclBookingPieceUnit != null && lclBookingPieceUnit.getLclBookingPiece() != null && lclBookingPieceUnit.getLclBookingPiece().getPieceDesc() != null) {
                    commodityDescription = lclBookingPieceUnit.getLclBookingPiece().getPieceDesc();
                    if (commodityDescription.length() >= 162) {
                        description = commodityDescription.substring(0, 162);
                    } else {
                        description = lclBookingPieceUnit.getLclBookingPiece().getPieceDesc();
                    }
                    p = new Paragraph(8f, "" + description.toUpperCase(), blackNormalFont5);
                } else {
                    p = new Paragraph(8f, "", blackNormalFont5);
                }
                cell.addElement(p);
                PdfPTable desTable = new PdfPTable(4);
                desTable.setWidthPercentage(101f);
                desTable.setWidths(new float[]{1.3f, 2.4f, 1.3f, 2.4f});
                PdfPCell desCell = null;
                desCell = new PdfPCell();
                desCell.setBorder(0);
                desCell.setColspan(2);
                p = new Paragraph(6f, "CFT: " + NumberUtils.convertToTwoDecimal(cft), blackNormalFont5);
                p.setAlignment(Element.ALIGN_LEFT);
                desCell.addElement(p);
                desTable.addCell(desCell);
                desCell = new PdfPCell();
                desCell.setBorder(0);
                desCell.setColspan(2);
                p = new Paragraph(6f, "CBM: " + NumberUtils.convertToTwoDecimal(cbm), blackNormalFont5);
                p.setAlignment(Element.ALIGN_LEFT);
                desCell.addElement(p);
                desTable.addCell(desCell);
                desCell = new PdfPCell();
                desCell.setBorder(0);
                desCell.setColspan(2);
                p = new Paragraph(6f, "LBS: " + NumberUtils.convertToTwoDecimal(lbs), blackNormalFont5);
                p.setAlignment(Element.ALIGN_LEFT);
                desCell.addElement(p);
                desTable.addCell(desCell);
                desCell = new PdfPCell();
                desCell.setBorder(0);
                desCell.setColspan(2);
                p = new Paragraph(6f, "KGS: " + NumberUtils.convertToTwoDecimal(kgs), blackNormalFont5);
                p.setAlignment(Element.ALIGN_LEFT);
                desCell.addElement(p);
                desTable.addCell(desCell);

                cell.addElement(desTable);
                table.addCell(cell);
            }
        }
        return table;
    }

    public PdfPTable totalsTable() throws DocumentException, Exception {
        flag = true;
        Paragraph p = null;
        document.newPage();
        Double cftTotal = 0.00;
        Double cbmTotal = 0.00;
        Double kgsTotal = 0.00;
        Double lbsTotal = 0.00;
        Font blackNormalFont5 = FontFactory.getFont("COURIER", 10f, Font.NORMAL);
        table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 2f, 1.25f, 1.75f, 2f, 2f, 1f});
        List<LclBookingPieceUnit> lclBookingPieceUnitsList = lclUnitSs.getLclBookingPieceUnitList();
        if (lclBookingPieceUnitsList != null && lclBookingPieceUnitsList.size() > 0) {
            for (int i = 0; i < lclBookingPieceUnitsList.size(); i++) {
                LclBookingPieceUnit lclBookingPieceUnit = (LclBookingPieceUnit) lclBookingPieceUnitsList.get(i);
                if (lclBookingPieceUnit.getLclBookingPiece().getActualWeightImperial() != null && lclBookingPieceUnit.getLclBookingPiece().getActualWeightImperial().doubleValue() != 0.00) {
                    lbsTotal = lbsTotal + lclBookingPieceUnit.getLclBookingPiece().getActualWeightImperial().doubleValue();
                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedWeightImperial() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedWeightImperial().doubleValue() != 0.00) {
                    lbsTotal = lbsTotal + lclBookingPieceUnit.getLclBookingPiece().getBookedWeightImperial().doubleValue();
                }
                if (lclBookingPieceUnit.getLclBookingPiece().getActualVolumeImperial() != null && lclBookingPieceUnit.getLclBookingPiece().getActualVolumeImperial().doubleValue() != 0.00) {
                    cftTotal = cftTotal + lclBookingPieceUnit.getLclBookingPiece().getActualVolumeImperial().doubleValue();
                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeImperial() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeImperial().doubleValue() != 0.00) {
                    cftTotal = cftTotal + lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeImperial().doubleValue();
                }
                if (lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue() != 0.00) {
                    kgsTotal = kgsTotal + lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue();
                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                    kgsTotal = kgsTotal + lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue();
                }
                if (lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric().doubleValue() != 0.00) {
                    cbmTotal = cbmTotal + lclBookingPieceUnit.getLclBookingPiece().getActualVolumeMetric().doubleValue();
                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric().doubleValue() != 0.00) {
                    cbmTotal = cbmTotal + lclBookingPieceUnit.getLclBookingPiece().getBookedVolumeMetric().doubleValue();
                }
            }
        }

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, " ", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(10f, "CUBIC FEET:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "CUBIC METERS:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "POUNDS:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "KILOGRAMS:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "Dock Receipts:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, "MIAMI TOTALS", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, " ", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(80f, "CUBIC FEET:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "CUBIC METERS:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "POUNDS:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "KILOGRAMS:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "DOCK RECEIPTS:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(80f, "GRAND TOTALS", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(10f, "" + NumberUtils.convertToTwoDecimal(cftTotal), blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "" + NumberUtils.convertToTwoDecimal(cbmTotal), blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "" + NumberUtils.convertToTwoDecimal(lbsTotal), blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "" + NumberUtils.convertToTwoDecimal(kgsTotal), blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "" + lclBookingPieceUnitsList.size(), blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, " ", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(10f, "CUBIC FEET:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "CUBIC METERS: ", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "POUNDS:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "KILOGRAMS:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "Dock Receipts:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, "FOREIGN TOTALS", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(10f, "" + NumberUtils.convertToTwoDecimal(cftTotal), blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "" + NumberUtils.convertToTwoDecimal(cbmTotal), blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "" + NumberUtils.convertToTwoDecimal(lbsTotal), blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "" + NumberUtils.convertToTwoDecimal(kgsTotal), blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "" + lclBookingPieceUnitsList.size(), blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, "", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    private PdfPTable freightTable() throws Exception {
        Paragraph p = null;
        Font blackNormalFont5 = FontFactory.getFont("COURIER", 10f, Font.NORMAL);
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);

        cell = new PdfPCell();
        cell.setBorder(0);
        if (flag == false) {
            p = new Paragraph(6f, "DID CONTAINER HAVE OVER FREIGHT?  (CIRCLE)  YES  NO", blackNormalFont5);
        } else {
            p = new Paragraph(6f, " ", blackNormalFont5);
        }
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public void createPdf(String realPath, String outputFileName, String lclUnitSsId) throws Exception {
        init(outputFileName);
        createImportBLPdf(document);
        exit();
    }

    private void exit() {
        document.close();
    }
}
