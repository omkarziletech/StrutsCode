/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingImportAms;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsImportsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsManifestDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
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
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Logiware
 */
public class LclImportBkgConciseStrippingPdfCreator extends LclReportFormatMethods {

    private LclUnitSs lclUnitSs;
    private String CST01 = "";
    private String CST02 = "";
    private String CST03 = "";
    private String CST04 = "";
    private String companyName="";
    public LclImportBkgConciseStrippingPdfCreator(LclUnitSs lclUnitSs) throws Exception {
        this.lclUnitSs = lclUnitSs;
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
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);
        }
    }

    private void init(String outputFileName) throws Exception {
        document = new Document(PageSize.A4);
        document.setMargins(8, 8, 15, 20);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        LclImportBkgConciseStrippingPdfCreator.TableHeader event = new LclImportBkgConciseStrippingPdfCreator.TableHeader();
        pdfWriter.setPageEvent(event);
        document.open();
    }

    public void createImportBLPdf(Document document) throws Exception {
        String cfsWhseName = "";
        LclUnitSsImports lclUnitSsImports = new LclUnitSsImportsDAO().getLclUnitSSImportsByHeader(lclUnitSs.getLclUnit().getId(), lclUnitSs.getLclSsHeader().getId());
        if (lclUnitSsImports != null && lclUnitSsImports.getCfsWarehouseId() != null && lclUnitSsImports.getCfsWarehouseId().getWarehouseName() != null) {
            cfsWhseName = lclUnitSsImports.getCfsWarehouseId().getWarehouseName();
        }
        document.add(headerTable(cfsWhseName,lclUnitSs.getLclSsHeader().getId()));
        document.add(companyDetailsTable());
        if (lclUnitSsImports != null) {
            document.add(vesselTable(lclUnitSsImports));
        }
        document.add(marksTable());
        document.add(commentsTable(cfsWhseName));
    }

    public PdfPTable headerTable(String cfsWhseName, Long headerId) throws Exception {
        Font blackNormalFont7 = FontFactory.getFont("COURIER", 10f, Font.NORMAL);
        Paragraph p = null;
        String webSite="";
        String brand = "";
        Iterator bookingCommentsIterator = new GenericCodeDAO().getLclPrintComments(39, "CST");
        if((lclUnitSs !=null && lclUnitSs.getLclSsHeader() !=null && !lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().isEmpty()) 
                && lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno() !=null) {
        brand =new TradingPartnerDAO().getBusinessUnit(lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno());
        }
        if (CommonUtils.isNotEmpty(brand)) {
            if ("ECI".equalsIgnoreCase(brand)) {
                companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
                webSite = LoadLogisoftProperties.getProperty("application.Econo.website");
            } else if ("OTI".equalsIgnoreCase(brand)) {
                companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
                webSite = LoadLogisoftProperties.getProperty("application.OTI.website");
            } else {
                companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
                webSite = LoadLogisoftProperties.getProperty("application.ECU.website");

            }
        } 
        while (bookingCommentsIterator.hasNext()) {
            Object[] row = (Object[]) bookingCommentsIterator.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null) {
                if ("CST01".equalsIgnoreCase(code)) {
                    CST01 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                }
            }
        }
        String comment1 = CST01 != null ? CST01 : "";
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100f);

        cell = new PdfPCell();
        cell.setBorder(0);
        if (CommonUtils.isNotEmpty(cfsWhseName)) {
            p = new Paragraph(10f, cfsWhseName + " WAREHOUSE OF HOUSTON", blackNormalFont7);
            p.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p);
        }
        p = new Paragraph(15f, "CONTAINER STRIPPING-OUTTURN REPORT", blackNormalFont7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(10f, "*********************************", blackNormalFont7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        headerTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        String headecomment = comment1.replaceAll("ECONOCARIBE", cfsWhseName).replace("ECI", cfsWhseName);
        headecomment =headecomment.replace("www.eciservices.com", webSite);
        p = new Paragraph(10f, "" + headecomment, blackNormalFont7);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        headerTable.addCell(cell);
        return headerTable;
    }

    public PdfPTable companyDetailsTable() throws DocumentException, Exception {
        Font blackNormalFont5 = FontFactory.getFont("COURIER", 11f, Font.NORMAL);
        Paragraph p = null;
        String name = "";
        String address = "";
        String fax = "";
        String city = "";
        String state = "";
        String zip = "";
        String prepaidCollect = "";
        if (lclUnitSs != null && CommonUtils.isNotEmpty(lclUnitSs.getSpBookingNo())) {
            LclSSMasterBl lclSSMasterBl = new LclSSMasterBlDAO().findBkgNo(lclUnitSs.getLclSsHeader().getId(), lclUnitSs.getSpBookingNo());
            if (null != lclSSMasterBl && CommonUtils.isNotEmpty(lclSSMasterBl.getPrepaidCollect())) {
                prepaidCollect = "C".equalsIgnoreCase(lclSSMasterBl.getPrepaidCollect()) ? "COLLECT" : "PREPAID";
            } else {
                prepaidCollect = "PREPAID";
            }
        }
        if (lclUnitSs.getLclSsHeader().getBillingTerminal() != null) {
            RefTerminal terminal = new RefTerminalDAO().getByProperty("trmnum", lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum());
            if (terminal != null && terminal.getTrmnam() != null) {
                name = terminal.getTrmnam();
            }
            if (terminal != null && terminal.getAddres1() != null) {
                address = terminal.getAddres1();
            }
            if (terminal != null && terminal.getFaxnum1() != null) {
                fax = terminal.getFaxnum1();
            }
            if (terminal != null && terminal.getCity1() != null) {
                city = terminal.getCity1();
            }
            if (terminal != null && terminal.getState() != null) {
                state = terminal.getState();
            }
            if (terminal != null && terminal.getZipcde() != null) {
                zip = terminal.getZipcde();
            }
        }

        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2.f, 1f, 1f});

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(20f, "" + companyName, blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "" + address.toUpperCase(), blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "*CALL FOR DELY APPT*", blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "" + city.toUpperCase() + " " + state.toUpperCase() + " " + zip, blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "FAX#" + fax.toUpperCase(), blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(20f, "", blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(20f, "JOB#:", blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "DATE: " + DateUtils.formatStringDateToAppFormatMMM(new Date()), blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "Time: " + DateUtils.formatDate(new Date(), "hh:mm"), blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "" + prepaidCollect, blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        p = new Paragraph(10f, "=========================================================================================================================================", blackNormalFont7);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable vesselTable(LclUnitSsImports lclUnitSsImports) throws DocumentException, Exception {
        Paragraph p = null;
        StringBuilder importFileNumber = new StringBuilder();
        String eta = "";
        String freeDate = "";
        String goDate = "";
        String stripDate = "";
        String masterBl = "";
        LclSsDetail lclSsDetail = new LclSsDetailDAO().findByTransMode(lclUnitSs.getLclSsHeader().getId(), "V");
        LclSsHeader lclSsHeader = new LclSsHeaderDAO().findById(lclUnitSs.getLclSsHeader().getId());
        RefTerminal terminal = new RefTerminalDAO().getByProperty("trmnum", lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum());
        if (terminal != null && terminal.getTrmnum() != null) {
            importFileNumber.append(terminal.getTrmnum());
        }
        if (lclSsHeader != null && lclSsHeader.getOrigin() != null && lclSsHeader.getOrigin().getUnLocationCode() != null) {
            importFileNumber.append("-").append(lclSsHeader.getOrigin().getUnLocationCode());
        }
        if (lclSsHeader != null && lclSsHeader.getDestination() != null && lclSsHeader.getDestination().getUnLocationCode() != null) {
            importFileNumber.append("-").append(lclSsHeader.getDestination().getUnLocationCode());
        }
        if (lclSsHeader != null && lclSsHeader.getScheduleNo() != null) {
            importFileNumber.append("-").append(lclSsHeader.getScheduleNo());
        }
        if (CommonFunctions.isNotNull(lclSsDetail.getSta())) {
            eta = DateUtils.formatDate(lclSsDetail.getSta(), "dd-MMM-yyyy");
        }
        if (CommonFunctions.isNotNull(lclUnitSsImports.getLastFreeDate())) {
            freeDate = DateUtils.formatDate(lclUnitSsImports.getLastFreeDate(), "dd-MMM-yyyy");
        }
        if (!lclUnitSs.getLclUnit().getLclUnitWhseList().isEmpty()) {
            Collections.sort(lclUnitSs.getLclUnit().getLclUnitWhseList(), new Comparator<LclUnitWhse>() {

                public int compare(LclUnitWhse o1, LclUnitWhse o2) {
                    return o2.getId().compareTo(o1.getId());
                }
            });
            if (lclUnitSs.getLclUnit().getLclUnitWhseList() != null && lclUnitSs.getLclUnit().getLclUnitWhseList().size() > 0) {
                for (int j = 0; j < lclUnitSs.getLclUnit().getLclUnitWhseList().size(); j++) {
                    LclUnitWhse lclUnitWhse = (LclUnitWhse) lclUnitSs.getLclUnit().getLclUnitWhseList().get(0);
                    if (CommonFunctions.isNotNull(lclUnitWhse.getDestuffedDatetime())) {
                        stripDate = DateUtils.formatDate(lclUnitWhse.getDestuffedDatetime(), "dd-MMM-yyyy");
                    }
                    break;
                }
            }
        }
        if (CommonFunctions.isNotNull(lclUnitSsImports.getGoDate())) {
            goDate = DateUtils.formatDate(lclUnitSsImports.getGoDate(), "dd-MMM-yyyy");
        }
        LclUnitSsManifest lclUnitSsManifest = new LclUnitSsManifestDAO().getLclUnitSSManifestByHeader(lclUnitSs.getLclUnit().getId(), lclUnitSs.getLclSsHeader().getId());
        if (lclUnitSsManifest != null && lclUnitSsManifest.getMasterbl() != null) {
            masterBl = lclUnitSsManifest.getMasterbl().toUpperCase();
        }
        Font blackNormalFont5 = FontFactory.getFont("COURIER", 11f, Font.NORMAL);
        table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.25f, 2.65f, 2f, 2f, 2.75f});

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, "VESSEL", blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "CONTAINER", blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "REFERENCE", blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "E.T.A", blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, "FREE TIME", blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, ": " + lclSsDetail.getSpReferenceName().toUpperCase(), blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, ": " + lclUnitSs.getLclUnit().getUnitNo().toUpperCase(), blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, ": " + importFileNumber.toString().toUpperCase(), blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, ": " + eta, blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, ": " + freeDate, blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, "DOCUMENTS RCVD", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "AVAIL @ PIER", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "AVAIL @ ECI", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "STRIP DATE", blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(10f, "G.O. DATE" + goDate, blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, ": ", blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, ": ", blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, ": ", blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, ": " + stripDate, blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(10f, ": " + goDate, blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, "MBL: " + masterBl.toUpperCase(), blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(5);
        p = new Paragraph(10f, "=========================================================================================================================================", blackNormalFont7);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    private PdfPTable marksTable() throws Exception {
        Paragraph p = null;
        Font blackNormalFont5 = FontFactory.getFont("COURIER", 11f, Font.NORMAL);
        table = new PdfPTable(13);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{.5f, .15f, 2.10f, .15f, 2.10f, .15f, 1.05f, .15f, 1.05f, .15f, .5f, .15f, 3f});
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(16f, "LOT", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(7f, "---", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, " ", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(16f, "HOUSE BL", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(7f, "---------------", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, " ", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(16f, "MARKS", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(7f, "---------------", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, " ", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(9f, "QTY\nMNFST", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(5f, "-------", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, " ", blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(9f, "QTY\nRECVD", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(5f, "-------", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, " ", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(16f, "PLT", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(7f, "---", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, " ", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(16f, "COMMENTS", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(7f, "----------------------", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        String comments = "";
        int qty = 0;
        int lot = 0;
        Double qtyTotal = 0.00;
        List<LclBookingPieceUnit> lclBookingPieceUnitsList = lclUnitSs.getLclBookingPieceUnitList();
        if (lclBookingPieceUnitsList != null && lclBookingPieceUnitsList.size() > 0) {
            for (int i = 0; i < lclBookingPieceUnitsList.size(); i++) {
                lot++;
                LclBookingPieceUnit lclBookingPieceUnit = (LclBookingPieceUnit) lclBookingPieceUnitsList.get(i);
                LclBooking lclBooking = new LCLBookingDAO().findById(lclBookingPieceUnit.getLclBookingPiece().getLclFileNumber().getId());
                comments = new LclRemarksDAO().getLclRemarksByTypeSQL(lclBooking.getLclFileNumber().getId().toString(), "OSD");
                String[] comment = comments.split("\n");
                comments = comment[0];
                List<LclBookingImportAms> lclBookingImportAmsList = new LclBookingImportAmsDAO().findByProperty("lclFileNumber.id", lclBooking.getLclFileNumber().getId());
                Collections.sort(lclBookingImportAmsList, new Comparator<LclBookingImportAms>() {

                    public int compare(LclBookingImportAms o1, LclBookingImportAms o2) {
                        return o2.getId().compareTo(o1.getId());
                    }
                });
                String houseBl = "";
                if (lclBookingImportAmsList != null && lclBookingImportAmsList.size() > 0) {
                    for (int j = 0; j < lclBookingImportAmsList.size(); j++) {
                        LclBookingImportAms lclBookingImportAms = (LclBookingImportAms) lclBookingImportAmsList.get(j);
                        if (CommonFunctions.isNotNull(lclBookingImportAms.getAmsNo())) {
                            houseBl = lclBookingImportAms.getAmsNo();
                        }
                        break;
                    }
                }
                if (lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount() != null && lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount().doubleValue() != 0.00) {
                    qtyTotal = qtyTotal + lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount();
                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount().doubleValue() != 0.00) {
                    qtyTotal = qtyTotal + lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount();
                }
                if (lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount() != null && lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount().doubleValue() != 0.00) {
                    qty = lclBookingPieceUnit.getLclBookingPiece().getActualPieceCount();
                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount().doubleValue() != 0.00) {
                    qty = lclBookingPieceUnit.getLclBookingPiece().getBookedPieceCount();
                }
                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, "" + lot, blackNormalFont5);
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, " ", blackNormalFont5);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, "" + houseBl.toUpperCase(), blackNormalFont5);
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(6f, " ", blackNormalFont5);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                if (lclBookingPieceUnit != null && lclBookingPieceUnit.getLclBookingPiece() != null && lclBookingPieceUnit.getLclBookingPiece().getMarkNoDesc() != null) {
                    p = new Paragraph(9f, "" + lclBookingPieceUnit.getLclBookingPiece().getMarkNoDesc().toUpperCase(), blackNormalFont5);
                } else {
                    p = new Paragraph(9f, " ", blackNormalFont5);
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(6f, " ", blackNormalFont5);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, "" + qty, blackNormalFont5);
                p.setAlignment(Element.ALIGN_RIGHT);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(6f, " ", blackNormalFont5);
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, "" + qty, blackNormalFont5);
                p.setAlignment(Element.ALIGN_RIGHT);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(6f, " ", blackNormalFont5);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, "", blackNormalFont5);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(6f, "", blackNormalFont5);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(7f, "" + comments, blackNormalFont5);
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);
            }
        }

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setPaddingBottom(15f);
        p = new Paragraph(15f, "", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setPaddingBottom(15f);
        p = new Paragraph(15f, "*****TOTALS*****", blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingBottom(15f);
        BigDecimal totals = new BigDecimal(qtyTotal);
        p = new Paragraph(15f, "" + totals.setScale(0), blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setPaddingBottom(15f);
        p = new Paragraph(15f, "" + totals.setScale(0), blackNormalFont5);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setPaddingBottom(15f);
        p = new Paragraph(15f, "", blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    private PdfPTable commentsTable(String cfsWhseName) throws Exception {
        Paragraph p = null;
        Iterator bookingCommentsIterator = new GenericCodeDAO().getLclPrintComments(39, "CST");
        while (bookingCommentsIterator.hasNext()) {
            Object[] row = (Object[]) bookingCommentsIterator.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null) {
                if ("CST02".equalsIgnoreCase(code)) {
                    CST02 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("CST03".equalsIgnoreCase(code)) {
                    CST03 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("CST04".equalsIgnoreCase(code)) {
                    CST04 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                }
            }
        }
        String comment2 = CST02 != null ? CST02 : "";
        String comment3 = CST03 != null ? CST03 : "";
        String comment4 = CST04 != null ? CST04 : "";
        Font blackNormalFont5 = FontFactory.getFont("COURIER", 10f, Font.NORMAL);
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, "" + comment2, blackNormalFont5);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        String Replacecomment3 = comment3.replaceAll("ECONOCARIBE", cfsWhseName).replace("ECI", cfsWhseName);
        p = new Paragraph(20f, "" + Replacecomment3, blackNormalFont5);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(10f);
        String Replacecomment4 = comment4.replaceAll("ECONOCARIBE", cfsWhseName).replace("ECI", cfsWhseName);
        p = new Paragraph(10f, "" + Replacecomment4, blackNormalFont5);
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
