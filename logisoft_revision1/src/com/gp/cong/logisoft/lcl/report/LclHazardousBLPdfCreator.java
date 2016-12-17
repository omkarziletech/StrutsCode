package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class LclHazardousBLPdfCreator extends LclReportFormatMethods {

    private static final Logger log = Logger.getLogger(LclHazardousBLPdfCreator.class);
    private LclUnitSs lclUnitSs;
    private String unitNumber = "";
    private String vesselName = "";
    private String carrierName = "";
    private String voyageNumber = "";
    private String ssVoyNo = "";
    private String originunlocationcode = "";
    private String destunlocationcode = "";
    private String consigneeName = "";
    private String consigneeAddress = "";
    private String departurePier = "";
    private String bookingNo = "";
    private StringBuilder destination = new StringBuilder();
    private PdfWriter PdfWriter;

    public LclHazardousBLPdfCreator(LclUnitSs lclUnitSs) throws Exception {
        this.lclUnitSs = lclUnitSs;
        if (CommonFunctions.isNotNull(lclUnitSs)) {
            if (CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getUnitNo())) {
                unitNumber = lclUnitSs.getLclUnit().getUnitNo();

            }
        }
        if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getScheduleNo())) {
            voyageNumber = lclUnitSs.getLclSsHeader().getScheduleNo();
        }
        bookingNo = CommonUtils.isNotEmpty(lclUnitSs.getSpBookingNo()) ? lclUnitSs.getSpBookingNo() : "";
        LclSsDetail lclssDetail = lclUnitSs.getLclSsHeader().getVesselSsDetail();
        if (lclssDetail != null) {
            if (CommonFunctions.isNotNull(lclssDetail.getSpReferenceName())) {
                vesselName = lclssDetail.getSpReferenceName();
            }
            if (CommonFunctions.isNotNull(lclssDetail.getSpAcctNo())
                    && CommonFunctions.isNotNull(lclssDetail.getSpAcctNo().getAccountName())) {
                carrierName = lclssDetail.getSpAcctNo().getAccountName();
            }
            if (CommonFunctions.isNotNull(lclssDetail.getSpReferenceNo())) {
                ssVoyNo = lclssDetail.getSpReferenceNo();
            }
            if (CommonFunctions.isNotNull(lclssDetail.getDeparture())
                    && CommonUtils.isNotEmpty(lclssDetail.getDeparture().getUnLocationName())) {
                departurePier = lclssDetail.getDeparture().getUnLocationName();
            }
            List<LclSSMasterBl> lclSSMasterBlList = lclssDetail.getLclSsHeader().getLclSsMasterBlList();
            if (!lclSSMasterBlList.isEmpty()) {
                LclSSMasterBl lclMasterBl = lclSSMasterBlList.get(0);
                if (CommonFunctions.isNotNull(lclMasterBl.getConsSsContactId()) && CommonFunctions.isNotNull(lclMasterBl.getConsSsContactId().getCompanyName())) {
                    consigneeName = lclMasterBl.getConsSsContactId().getCompanyName();
                }
                if (CommonUtils.isNotEmpty(lclMasterBl.getConsEdi())) {
                    consigneeAddress = lclMasterBl.getConsEdi().replaceAll("\r\n", " ");
                }
//                if (CommonFunctions.isNotNull(lclMasterBl.getConsSsContactId()) && CommonFunctions.isNotNull(lclMasterBl.getConsSsContactId().getCity())) {
//                    consigneeCity = lclMasterBl.getConsSsContactId().getCity();
//                }
//                 if (CommonFunctions.isNotNull(lclMasterBl.getConsSsContactId()) && CommonFunctions.isNotNull(lclMasterBl.getConsSsContactId().getPhone1())) {
//                    consigneePh = lclMasterBl.getConsSsContactId().getPhone1();
//                }
            }
        }
        if (lclUnitSs.getLclSsHeader().getOrigin().getUnLocationCode().startsWith("US")) {
            originunlocationcode = lclUnitSs.getLclSsHeader().getOrigin().getUnLocationCode().substring(2);
        } else {
            originunlocationcode = lclUnitSs.getLclSsHeader().getOrigin().getUnLocationCode();
        }
        destunlocationcode = lclUnitSs.getLclSsHeader().getDestination().getUnLocationCode();
        if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName())) {
            destination.append(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName()).append(", ");
        }
        if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getCountryId()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getCountryId().getCodedesc())) {
            destination.append(lclUnitSs.getLclSsHeader().getDestination().getCountryId().getCodedesc());
        }
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
                Rectangle rect = new Rectangle(590, 830, 5, 5);
                rect.setBorder(Rectangle.BOX);
                rect.setBorderWidth(0.6f);
                document.add(rect);
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_CENTER, new Phrase("TOTAL PAGES   " + writer.getPageNumber(), blackNormalCourierFont10f),
                        (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 820, 0);
            } catch (DocumentException ex) {
                log.info("Exception on class LclHazardousBLPdfCreator in method onEndPage" + new Date(), ex);
            }

        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);
        }
    }

    public void createHazardousPdf(String realPath, String outputFileName, HttpServletRequest request) throws IOException, DocumentException, SQLException, Exception {
        document = new Document(PageSize.A4, 8, 12, 12, 16);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        LclHazardousBLPdfCreator.TableHeader event = new LclHazardousBLPdfCreator.TableHeader();
        pdfWriter.setPageEvent(event);
        document.open();
        document.add(addressTable(pdfWriter));
        document.add(carrconsTable());
        document.add(destinationTable());
        document.add(accountTable());
        document.add(delivercarTable());
        document.add(bookingportTable());
        document.add(containerTable());
        document.add(dockReceiptTable());
        document.add(contentTable());
        document.add(signatureTable(request, realPath));
        //document.newPage();
        document.close();
    }

    public PdfPTable addressTable(PdfWriter writer) throws BadElementException, MalformedURLException, IOException, DocumentException, Exception {
        String terminalNumber = "";
        RefTerminal terminal = null;
        if (lclUnitSs != null && lclUnitSs.getLclSsHeader() != null && lclUnitSs.getLclSsHeader().getBillingTerminal() != null && lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum() != null) {
            terminal = new RefTerminalDAO().getByProperty("trmnum", lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum());
            if (terminal != null && terminal.getTrmnum() != null) {
                terminalNumber = terminal.getTrmnum() + "-";
            }
        }
        Paragraph p = null;
        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 4.65f, 2.35f});
        table.addCell(makeCellNoBorderFont("", 2f, 3, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFont("PAGE  -  " + String.valueOf(writer.getPageNumber()), 9f, 0, blackNormalCourierFont10f));
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(1f);
        p = new Paragraph("HAZARDOUS B/L FORM", blackBoldCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(1f);
        p = new Paragraph("REF#:" + terminalNumber + originunlocationcode + "-" + destunlocationcode + "-" + voyageNumber, blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);
        table.addCell(makeCellNoBorderFont("", 1f, 0, blackNormalCourierFont10f));
        cell = new PdfPCell();
        cell.setBorder(0);
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        String CompanyName = systemRulesDAO.getSystemRuleNameByRuleCode("CompanyName");
        String companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
        p = new Paragraph(CompanyName.toUpperCase(), blackBoldCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        table.addCell(makeCellNoBorderFont("", 1f, 0, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackNormalCourierFont10f));
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-10f);
        p = new Paragraph(companyAddress.toUpperCase() + "", blackBoldCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackNormalCourierFont10f));

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setPaddingTop(-5f);

        String CompanyPhone = new SystemRulesDAO().getSystemRuleNameByRuleCode("CompanyPhone");
        String CompanyFax = new SystemRulesDAO().getSystemRuleNameByRuleCode("CompanyFax");
        p = new Paragraph("Please Call" + " " + CompanyPhone + " " + "Fax" + " " + CompanyFax + " " + "If You Have Any Questions", blackBoldCourierFont10f);

        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setPaddingTop(-10f);
        p = new Paragraph("===============================================================================", blackBoldCourierFont10f);
        cell.addElement(p);
        table.addCell(cell);
        //emptycell
        table.addCell(createEmptyCell(0, 3f, 3));
        return table;

    }

    public PdfPTable carrconsTable() throws DocumentException {
        table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 0.06f, 5f, 1f});
        table.addCell(makeCellNoBorderFont("Carrier.......:", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("" + carrierName.toUpperCase(), 0, -3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("Consigned To..:", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue(consigneeName, 0, -3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        //2n cell
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue(consigneeAddress, 0, -3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        //3rd cell
//        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
//        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
//        table.addCell(makeCellBottomBorderValue("", 0, 10f, 0.6f, blackBoldCourierFont10f));
//        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
//        //4th cell
//        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
//        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
//        table.addCell(makeCellBottomBorderValue("", 0, 10f, 0.6f, blackBoldCourierFont10f));
//        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));

        return table;
    }

    public PdfPTable destinationTable() throws DocumentException {
        table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.52f, 0.06f, 2.8f, 0.7f, 0.5f, 0.9f, 1f, 0.2f});
        table.addCell(makeCellNoBorderFont("Destination...:", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue(destination.toString().toUpperCase(), 0, -3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont(" State", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("", 0, 3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont(" Country ", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("", 0, 3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("", 0, -1f, 0, blackBoldCourierFont10f));
        return table;
    }

    public PdfPTable accountTable() throws DocumentException, Exception {
        table = new PdfPTable(6);
        table.setWidthPercentage(100f);
//
        table.setWidths(new float[]{1.52f, 0.06f, 4f, 0.9f, 1f, 0.2f});
        table.addCell(makeCellNoBorderFont("For Account Of:", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue(LoadLogisoftProperties.getProperty("application.fclBl.edi.companyName"), 0, -3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont(" Voyage# ", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue(voyageNumber, 0, -3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("", 0, -1f, 0, blackBoldCourierFont10f));

        table.addCell(makeCellNoBorderFont("", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("2401 NW 69 Street Miami, Fl. 33147", 0, -3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -1f, 0, blackBoldCourierFont10f));
        return table;
    }

    public PdfPTable delivercarTable() throws DocumentException {
        table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.72f, 0.02f, 4.15f, .9f});
        table.addCell(makeCellNoBorderFont("Delivering Carrier:", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("", 0, 3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        //emptycell
        table.addCell(createEmptyCell(0, 2f, 4));
        return table;
    }

    public PdfPTable bookingportTable() throws DocumentException {
        table = new PdfPTable(5);
        table.setWidths(new float[]{1.4f, 1.5f, 0.8f, 2.2f, 0.9f});
        table.setWidthPercentage(100f);
        table.addCell(makeCellNoBorderFont("Booking Number:", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("" + bookingNo.toUpperCase(), 0, -3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("   Port:", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("" + departurePier.toUpperCase(), 0, -1f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        //emptycell
        table.addCell(createEmptyCell(0, 2f, 5));
        return table;
    }

    public PdfPTable containerTable() throws Exception {
        table = new PdfPTable(4);
        table.setWidths(new float[]{2f, 1.2f, 2f, 2f});
        table.setWidthPercentage(100f);
        table.addCell(makeCellNoBorderFontalign("1x" + new LclUtils().getContainerSize(lclUnitSs.getId()), -3f, Element.ALIGN_CENTER, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("CONTAINER:", -3f, Element.ALIGN_RIGHT, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("" + unitNumber.toUpperCase(), -3f, Element.ALIGN_LEFT, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("V." + ssVoyNo.toUpperCase(), -3f, Element.ALIGN_CENTER, 0, blackBoldCourierFont10f));
        //2n cell
        table.addCell(makeCellNoBorderFontalign("", -3f, Element.ALIGN_CENTER, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("VESSEL:", -3f, Element.ALIGN_RIGHT, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("" + vesselName.toUpperCase(), -3f, Element.ALIGN_LEFT, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("", -3f, Element.ALIGN_CENTER, 0, blackBoldCourierFont10f));
        //3rd cell
        table.addCell(makeCellNoBorderFontalign("", -3f, Element.ALIGN_CENTER, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("SEAL:", -3f, Element.ALIGN_RIGHT, 0, blackBoldCourierFont10f));
        String sealNo = CommonUtils.isNotEmpty(lclUnitSs.getSUHeadingNote()) ? lclUnitSs.getSUHeadingNote() : "";
        table.addCell(makeCellNoBorderFontalign("" + sealNo, -3f, Element.ALIGN_LEFT, 2, blackBoldCourierFont10f));
        //emptycell
        table.addCell(createEmptyCell(0, 3f, 4));
        return table;
    }

    public PdfPTable dockReceiptTable() throws DocumentException, Exception {
        Chunk underline = null;
        Paragraph p = null;
        table = new PdfPTable(2);
        table.setWidths(new float[]{3.5f, 1.5f});
        table.setWidthPercentage(100f);
        LclReportUtils lclReportUtils = new LclReportUtils();
        List<LclBookingPieceUnit> lclBookingPieceUnitList = lclUnitSs.getLclBookingPieceUnitList();
        String fileNumber = new String();
        Map<String, String> fileNumberMap = new HashMap<String, String>();
        for (LclBookingPieceUnit lclBookingPieceUnit : lclBookingPieceUnitList) {
            List<LclBookingHazmat> lclBookingHazmatList = lclBookingPieceUnit.getLclBookingPiece().getLclBookingHazmatList();
            for (int i = 0; i < lclBookingHazmatList.size(); i++) {
                LclBookingHazmat lclBookingHazmat = (LclBookingHazmat) lclBookingHazmatList.get(i);
                StringBuilder hazmatValues = new StringBuilder();
                hazmatValues.append(lclReportUtils.concateHazmatDetails(lclBookingHazmat)).append("\n");
                fileNumber = lclBookingPieceUnit.getLclBookingPiece().getLclFileNumber().getFileNumber();
                if (fileNumberMap.containsKey(fileNumber)) {
                    String existingHazValue = fileNumberMap.get(fileNumber);
                    hazmatValues.append(existingHazValue);
                }
                fileNumberMap.put(fileNumber, hazmatValues.toString());
            }
        }
        if (fileNumberMap != null && fileNumberMap.size() > 0) {
            for (Map.Entry<String, String> entry : fileNumberMap.entrySet()) {
                cell = new PdfPCell();
                cell.setBorder(0);
                underline = new Chunk("Dock Receipt:" + originunlocationcode + "-" + entry.getKey(), blackBoldCourierFont10f);
                underline.setUnderline(0.6f, -3f);
                cell.addElement(underline);
                table.addCell(cell);
                table.addCell(createEmptyCell(0, 5f, 1));
                String hazmatValues = entry.getValue().toUpperCase();
                String[] totalLine = hazmatValues.split("\n");
                for (String hzValues : totalLine) {
                    table.addCell(makeCellNoBorderFontalign("" + hzValues, -1f, Element.ALIGN_LEFT, 0, blackBoldCourierFont10f));
                    table.addCell(createEmptyCell(0, 5f, 1));
                }
            }
//            Iterator fileInfoIterator = fileNumberMap.keySet().iterator();
//            while (fileInfoIterator.hasNext()) {
//                String key = (String) fileInfoIterator.next();
//                String values = fileNumberMap.get(key);
//            }
        } else {
            //emptycell
            table.addCell(createEmptyCell(0, 40f, 1));
        }

        return table;
    }

    public PdfPTable contentTable() throws DocumentException {
        table = new PdfPTable(1);
        table.setWidths(new float[]{5f});
        table.setWidthPercentage(100f);
        table.addCell(makeCellNoBorderFontalign("CHEMTREC CONTRACTED BY ECONOCARIBE CONSOLIDATORS INC", 3f, Element.ALIGN_CENTER, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("CONTRACT/CUSTOMER # CCN7371", -3f, Element.ALIGN_CENTER, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("EMERGENCY CONTACT:", -3f, Element.ALIGN_CENTER, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("Within USA and Canada: 1-800-424-9300", -3f, Element.ALIGN_CENTER, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("Outside USA and Canada: +1 703-527-3887 (collect calls accepted)", -3f, Element.ALIGN_CENTER, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("DECLARATION", -3f, Element.ALIGN_LEFT, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("I hereby declare that the contents of this consignment are fully and accurately described above by the Proper Shipping Name, and are classified, packaged,"
                + "marked and labelled/placarded, and are in all respects in proper condition for transport according to applicable international and national government "
                + "regulations.", -3f, Element.ALIGN_LEFT, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("IT IS DECLARED THAT THE PACKING OF THE CONTAINER HAS BEEN CARRIED OUT IN "
                + "ACCORDANCE WITH THE APPLICABLE PROVISIONS OF 49 CFR And The IMDG Code", -3f, Element.ALIGN_LEFT, 0, blackBoldCourierFont10f));
        //emptycell
        table.addCell(createEmptyCell(0, 3f, 1));
        return table;
    }

    public PdfPTable signatureTable(HttpServletRequest request, String realPath) throws
            DocumentException, MalformedURLException, BadElementException, IOException, Exception {
        User user = user = (User) request.getSession().getAttribute("loginuser");
        String printedBy = "";
        if (CommonUtils.isNotEmpty(user.getFirstName())) {
            printedBy = user.getFirstName();
        }
        if (CommonUtils.isNotEmpty(user.getLastName())) {
            printedBy += " " + user.getLastName();
        }
        User images = new UserDAO().findById(user.getUserId());
        byte[] image = images.getSignatureImage();
        table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.15f, 1.5f, 1f, 2.2f, 0.66f, 1f});
        table.addCell(makeCellNoBorderFont("Printed By:", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderValue("" + printedBy.toUpperCase(), 0, -3f, 0.6f, boldBlockUnderline));
        table.addCell(makeCellNoBorderFont("Signature:", -3f, 0, blackBoldCourierFont10f));
        if (image != null) {
            Image img = Image.getInstance(image);
            img.scalePercent(2);
            img.scaleAbsoluteWidth(40f);
            img.scaleAbsoluteHeight(8f);
            table.addCell(img);
        } else {
            table.addCell(makeCellBottomBorderValue("", 0, -3f, 0.6f, blackBoldCourierFont10f));
        }
        table.addCell(makeCellNoBorderFont(" Date:", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("________", -3f, 0, blackBoldCourierFont10f));

        cell = new PdfPCell();
        cell.setColspan(6);
        cell.setBorder(0);
        table.addCell(cell);
        return table;
    }
}
