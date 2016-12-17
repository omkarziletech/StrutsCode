/**
 *
 * @author meiyazhakan.r
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.Element;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Meiyazhakan
 */
public class ImportRoutingPdfCreator extends LclReportFormatMethods {

    private LclBooking lclBooking;
    private LclUtils lclUtils = new LclUtils();
    private String terminalNo = "";
    private String companyName = "";
    private String termLocation = "";
    private String termAddress = "";
    private String phoneNumber = "";
    private String faxNumber = "";
    private StringBuilder bookingNumber = new StringBuilder();
    private StringBuilder terminalDetails = new StringBuilder();

    public ImportRoutingPdfCreator(LclBooking lclBooking) throws Exception {
        this.lclBooking = lclBooking;
        new LCLBookingDAO().getCurrentSession().evict(this.lclBooking);
        terminalNo = lclBooking.getTerminal().getTrmnum();
        if (lclBooking.getLclFileNumber() != null && lclBooking.getLclFileNumber().getFileNumber() != null) {
            bookingNumber.append(lclBooking.getLclFileNumber().getFileNumber());
        }
        RefTerminal refTerminal = new RefTerminalDAO().findById(terminalNo);
        if (lclBooking.getEnteredBy().getFirstName()!= null || lclBooking.getEnteredBy().getLastName()!=null) {
            companyName=lclBooking.getEnteredBy().getFirstName()+"  "+lclBooking.getEnteredBy().getLastName();
        }
         if (lclBooking.getEnteredBy().getCity()!= null && lclBooking.getEnteredBy().getState()!= null) {
             termLocation=lclBooking.getEnteredBy().getCity()+" "+lclBooking.getEnteredBy().getState();
        }
        if (lclBooking.getEnteredBy().getAddress1() != null || lclBooking.getEnteredBy().getAddress2() != null) {
            terminalDetails.append(lclBooking.getTerminal().getAddres1()).append(lclBooking.getTerminal().getAddres2()).append(" ");
        }
        if (lclBooking.getTerminal().getCity1() != null || lclBooking.getTerminal().getCity1() != null) {
            terminalDetails.append(lclBooking.getTerminal().getState()).append("\n");
        }
        if (lclBooking.getEnteredBy().getZipCode() != null) {
            terminalDetails.append(lclBooking.getTerminal().getZipcde());
        }
        if (null != lclBooking.getEnteredBy() && CommonUtils.isNotEmpty(lclBooking.getEnteredBy().getTelephone())) {
            String pNoSpaceRemove = StringUtils.remove(lclBooking.getEnteredBy().getTelephone(), " ");
            String ph1 = pNoSpaceRemove.substring(0, 3);
            String ph2 = pNoSpaceRemove.substring(3, 6);
            String ph3 = pNoSpaceRemove.substring(6);
            phoneNumber = "(" + ph1 + ") " + ph2 + "-" + ph3;
        }
        if (lclBooking.getEnteredBy().getFax() != null) {
            faxNumber = " * Fax " + lclBooking.getEnteredBy().getFax();
        }
    }

    public void createReport(String outputFileName) throws DocumentException, FileNotFoundException, Exception {
        StringBuilder commodityListValues = new StringBuilder();
        StringBuilder deliverCargo = new StringBuilder();
        String phoneNumberDeliver = null;
        LclBookingPad lclBookingPad = new LclBookingPadDAO().getLclBookingPadByFileNumber(lclBooking.getFileNumberId());
        if (lclBookingPad != null && lclBookingPad.getDeliveryContact() != null && lclBookingPad.getDeliveryContact().getId() != null) {
            if (CommonUtils.isNotEmpty(lclBookingPad.getDeliveryContact().getCompanyName())) {
                deliverCargo.append(lclBookingPad.getDeliveryContact().getCompanyName()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclBookingPad.getDeliveryContact().getAddress())) {
                deliverCargo.append(lclBookingPad.getDeliveryContact().getAddress());
            }
            if (CommonUtils.isNotEmpty(lclBookingPad.getDeliveryContact().getCity())) {
                deliverCargo.append(lclBookingPad.getDeliveryContact().getCity()).append(", ");
            }
            if (CommonUtils.isNotEmpty(lclBookingPad.getDeliveryContact().getState())) {
                deliverCargo.append(lclBookingPad.getDeliveryContact().getState()).append(" ");
            }
            if (CommonUtils.isNotEmpty(lclBookingPad.getDeliveryContact().getZip())) {
                deliverCargo.append(lclBookingPad.getDeliveryContact().getZip());
            }
            if (CommonUtils.isNotEmpty(lclBookingPad.getDeliveryContact().getPhone1())) {
                String pNoSpaceRemove = StringUtils.remove(lclBookingPad.getDeliveryContact().getPhone1(), " ");
                String ph1 = pNoSpaceRemove.substring(0, 3);
                String ph2 = pNoSpaceRemove.substring(3, 6);
                String ph3 = pNoSpaceRemove.substring(6);
                phoneNumberDeliver = "(" + ph1 + ") " + ph2 + "-" + ph3;
            }
        }
        StringBuilder clientDetails = new StringBuilder();
        String shipperValues = "";
        String clientEmail = "";
        StringBuilder shipAddress = new StringBuilder();
        shipperValues = CommonFunctions.isNotNull(lclBooking.getShipAcct()) ? lclBooking.getShipAcct().getAccountName()
                + "(" + lclBooking.getShipAcct().getAccountno() + ")" : "";
        if (CommonFunctions.isNotNull(lclBooking.getShipContact())) {
            if (CommonUtils.isNotEmpty(lclBooking.getShipContact().getAddress())) {
                shipAddress.append(lclBooking.getShipContact().getAddress()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getShipContact().getCity())) {
                shipAddress.append(lclBooking.getShipContact().getCity()).append(",");
            } else {
                shipAddress.append(" ");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getShipContact().getState())) {
                shipAddress.append(lclBooking.getShipContact().getState()).append(", ");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getShipContact().getZip())) {
                shipAddress.append(lclBooking.getShipContact().getZip()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getShipContact().getPhone1())) {
                shipAddress.append("PHN: ").append(lclBooking.getShipContact().getPhone1()).append("  ");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getShipContact().getFax1())) {
                shipAddress.append("FAX: ").append(lclBooking.getShipContact().getFax1());
            }
        }
        if (lclBooking.getClientContact() != null) {
            if (CommonUtils.isNotEmpty(lclBooking.getClientContact().getCompanyName())) {
                clientDetails.append(lclBooking.getClientContact().getCompanyName()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getClientContact().getAddress())) {
                clientDetails.append(lclBooking.getClientContact().getAddress()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getClientContact().getCity())) {
                clientDetails.append(lclBooking.getClientContact().getCity()).append(",");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getClientContact().getState())) {
                clientDetails.append(lclBooking.getClientContact().getState());
            }
            if (CommonUtils.isNotEmpty(lclBooking.getClientContact().getZip())) {
                clientDetails.append(", ").append(lclBooking.getClientContact().getZip());
            }
            if (CommonUtils.isNotEmpty(lclBooking.getClientContact().getPhone1())) {
                clientDetails.append("\n").append("PHN: ").append(lclBooking.getClientContact().getPhone1());
            }
            if (CommonUtils.isNotEmpty(lclBooking.getClientContact().getFax1())) {
                clientDetails.append("   ").append("FAX: ").append(lclBooking.getClientContact().getFax1()).append(" ");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getClientContact().getEmail1())) {
                clientEmail = lclBooking.getClientContact().getEmail1();
            }
        }
        LclBookingPiece lclBookingPiece = null;
        List<LclBookingPiece> lclBookingPieceList = lclBooking.getLclFileNumber().getLclBookingPieceList();
        if (!lclBookingPieceList.isEmpty()) {
            lclBookingPiece = lclBookingPieceList.get(0);
            if (lclBookingPiece != null && lclBookingPiece.getActualPieceCount() != null && 0 != lclBookingPiece.getActualPieceCount()) {
                commodityListValues.append(lclBookingPiece.getActualPieceCount()).append("          ");
            } else if (lclBookingPiece != null && lclBookingPiece.getBookedPieceCount() != null) {
                commodityListValues.append(lclBookingPiece.getBookedPieceCount()).append("       ");
            } else {
                commodityListValues.append("      ").append("           ");
            }
            if (lclBookingPiece != null && lclBookingPiece.getActualWeightImperial() != null) {
                commodityListValues.append("POUNDS:    ").append(NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getActualWeightImperial().doubleValue())).append("         ");
            } else if (lclBookingPiece != null && lclBookingPiece.getBookedWeightImperial() != null) {
                commodityListValues.append("POUNDS:  ").append(NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getBookedWeightImperial().doubleValue())).append("          ");
            } else {
                commodityListValues.append("POUNDS:    ").append("              ");
            }
            if (lclBookingPiece != null && lclBookingPiece.getActualVolumeImperial() != null) {
                commodityListValues.append("CFT:  ").append(NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getActualVolumeImperial().doubleValue())).append("     ");
            } else if (lclBookingPiece != null && lclBookingPiece.getBookedVolumeImperial() != null) {
                commodityListValues.append("CFT:  ").append(NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getBookedVolumeImperial().doubleValue())).append("      ");
            } else {
                commodityListValues.append("CFT:      ").append("               ");
            }
        } else {
            commodityListValues.append("              ").append("POUNDS:    ").append("              ").append("CFT:      ").append("               ");
        }
        String TR001 = null;
        String TR002 = null;
        Iterator bookingCommentsIterator = new GenericCodeDAO().getLclPrintComments(39, "TR");
        while (bookingCommentsIterator.hasNext()) {
            Object[] row = (Object[]) bookingCommentsIterator.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null) {
                if ("TR001".equalsIgnoreCase(code)) {
                    TR001 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                }
                if ("TR002".equalsIgnoreCase(code)) {
                    TR002 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                }
            }
        }

        PdfPCell createCell = null;
        Paragraph p = null;
        Chunk c = null;
        document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(5, 2, 5, 5);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        document.open();
        PdfPTable borderTable = new PdfPTable(1);
        borderTable.setWidthPercentage(100f);
        borderTable.setSpacingAfter(5f);
        PdfPCell cell = new PdfPCell();
        PdfPTable startTable = new PdfPTable(6);
        startTable.setWidthPercentage(95f);
        createCell = new PdfPCell();
        createCell.setColspan(3);
        createCell.setBorder(0);
        p = new Paragraph("ROUTING INSTRUCTIONS", blackNormalCourierFont10f);
        createCell.addElement(p);
        startTable.addCell(createCell);

        createCell = new PdfPCell();
        createCell.setBorder(0);
        createCell.setColspan(3);
        p = new Paragraph("TODAYS DATE:  " + DateUtils.formatStringDateToAppFormatMMM(new Date()), blackNormalCourierFont10f);
        createCell.addElement(p);
        p.setAlignment(Element.ALIGN_RIGHT);
        startTable.addCell(createCell);

        createCell = new PdfPCell();
        createCell.setBorder(0);
        createCell.setColspan(6);
        createCell.setFixedHeight(10f);
        startTable.addCell(createCell);



        createCell = new PdfPCell();
        createCell.setBorder(0);
        createCell.setColspan(6);
        PdfPTable pTable = new PdfPTable(7);
        PdfPCell starCell = null;
        pTable.setWidthPercentage(106f);
        pTable.setWidths(new float[]{0.2f, 2.5f, 0.6f, 1.2f, 4.5f, 4f, 3f});

        starCell = new PdfPCell();
        starCell.setColspan(7);
        starCell.setBorder(0);
        starCell.setPadding(0f);
        p = new Paragraph("******************************************************************************************", blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "*", blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "  BOOKING#  ", blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        starCell.setPaddingLeft(-4f);
        p = new Paragraph(8f, "*", blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "FROM:  ", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        starCell.setPaddingLeft(10f);
        starCell.setPaddingRight(-50f);
        p = new Paragraph(8f, "" + companyName, blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "BOOKING DATE:  ", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        if (lclBooking.getEnteredDatetime() != null) {
            p = new Paragraph(8f, "   " + DateUtils.formatStringDateToAppFormatMMM(lclBooking.getEnteredDatetime()), blackNormalCourierFont10f);
        } else {
            p = new Paragraph(8f, "   ", blackNormalFont9);
        }
        starCell.addElement(p);
        pTable.addCell(starCell);
        //2nd star
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "*", blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "  " + bookingNumber.toString(), blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_LEFT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        starCell.setPaddingLeft(-4f);
        p = new Paragraph(8f, "*", blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "", blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        starCell.setPaddingLeft(10f);
        p = new Paragraph(8f, "" + termLocation, blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "", blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "", blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);
        //3rd

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "****************", blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);



        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "", blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPadding(0f);
        starCell.setPaddingLeft(10f);
        p = new Paragraph(8f, "" + terminalDetails.toString().toUpperCase(), blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);

        //4th row
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        starCell.setBorder(0);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "PHONE:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPadding(0f);
        starCell.setPaddingLeft(10f);
        p = new Paragraph(8f, "" + phoneNumber, blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);
        //stra
        starCell = new PdfPCell();
        starCell.setColspan(7);
        starCell.setBorder(0);
        starCell.setPadding(0f);
        p = new Paragraph("******************************************************************************************", blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //content
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "TO:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        starCell.setPaddingLeft(10f);
        String toValues = CommonFunctions.isNotNull(lclBooking.getSupAcct()) ? lclBooking.getSupAcct().getAccountName() : "";
        p = new Paragraph(8f, "" + toValues, blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(2);
        starCell.setPadding(0f);
        if (lclBookingPad != null && lclBookingPad.getScac() != null) {
            p = new Paragraph(8f, "" + lclBookingPad.getScac(), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_CENTER);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
        }
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //SHIPPER/SUPPLIER:
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "SHIPPER/SUPPLIER:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPaddingLeft(10f);
        if (!"".equals(shipperValues)) {
            p = new Paragraph(8f, "" + shipperValues, blackNormalCourierFont10f);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
        }
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 2f, 7));
        //ADDRESS
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "ADDRESS:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPaddingLeft(10f);
        if (shipAddress != null) {
            p = new Paragraph(8f, "" + shipAddress.toString(), blackNormalCourierFont10f);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
        }
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "", blackNormalCourierFont10f);
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //CONTACT
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "CONTACT:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPaddingLeft(10f);
        if (clientDetails != null && !"".equals(clientDetails.toString())) {
            p = new Paragraph(8f, "" + clientDetails.toString(), blackNormalCourierFont10f);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
        }
        p.setAlignment(Element.ALIGN_LEFT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        //emailCONTACT
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        starCell.setPadding(0f);
        p = new Paragraph(8f, "CONTACT EMAIL:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPadding(0f);
        starCell.setPaddingLeft(10f);
        if (clientEmail != null && !"".equals(clientEmail)) {
            p = new Paragraph(8f, "" + clientEmail, blackNormalCourierFont10f);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
        }
        p.setAlignment(Element.ALIGN_LEFT);
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //pieces
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "PIECES:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        p = new Paragraph(8f, "   " + commodityListValues.toString(), blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_LEFT);
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //commodity
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "COMMODITY:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPaddingLeft(10f);
        if (lclBookingPiece != null && lclBookingPiece.getPieceDesc() != null) {
            p = new Paragraph(8f, "" + lclBookingPiece.getPieceDesc().toUpperCase(), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
            starCell.addElement(p);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
            starCell.addElement(p);
        }
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //shipperhrs
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "SHIPPER HOURS:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPaddingLeft(10f);
        if (lclBookingPad != null && lclBookingPad.getDeliveryHours() != null) {
            p = new Paragraph(8f, "" + lclBookingPad.getDeliveryHours().toUpperCase(), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
            starCell.addElement(p);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
            starCell.addElement(p);
        }
        pTable.addCell(starCell);
        StringBuilder readyDate=new StringBuilder();
          if(lclBookingPad != null && lclBookingPad.getPickupReadyDate() != null) {
            readyDate.append(DateUtils.formatStringDateToAppFormatMMM(lclBookingPad.getPickupReadyDate()));
          } else{readyDate.append("");}
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(2);
        p = new Paragraph(8f, "READY DATE:  " +readyDate.toString().toUpperCase(), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
            starCell.addElement(p);

        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //special
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "SPECIAL INSTRUCTIONS:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        pTable.addCell(starCell);
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        pTable.addCell(starCell);
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPadding(0f);
        starCell.setPaddingLeft(-10f);
        if (lclBookingPad != null && lclBookingPad.getPickUpTo() != null) {
            p = new Paragraph(8f, "" + lclBookingPad.getPickUpTo().toUpperCase(), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
            starCell.addElement(p);
        } else {
            p = new Paragraph(8f, "   ", blackNormalFont9);
            starCell.addElement(p);
        }
        pTable.addCell(starCell);
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        pTable.addCell(starCell);
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPadding(0f);
        starCell.setPaddingLeft(-10f);
        if (lclBookingPad != null && lclBookingPad.getPickupInstructions() != null) {
            p = new Paragraph(8f, "" + lclBookingPad.getPickupInstructions(), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
            starCell.addElement(p);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
            starCell.addElement(p);
        }
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //booking
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "BOOKING NO.:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPaddingLeft(10f);
        p = new Paragraph(8f, "" + bookingNumber.toString(), blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_LEFT);
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //deliver
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(10f, "DELIVER TO:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPaddingLeft(10f);
        p = new Paragraph(10f, "" + deliverCargo.toString(), blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_LEFT);
        starCell.addElement(p);
        pTable.addCell(starCell);
        //phone
        //deliver
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "PHONE:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPaddingLeft(10f);
        if (phoneNumberDeliver != null) {
            p = new Paragraph(8f, "" + phoneNumberDeliver, blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
        } else {
            p = new Paragraph(8f, "   ", blackNormalFont9);
        }
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //consignee
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "CONSIGNEE:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPaddingLeft(10f);
        if (lclBooking != null && lclBooking.getConsAcct() != null && lclBooking.getConsContact().getCompanyName() != null) {
            p = new Paragraph(8f, "" + lclBooking.getConsContact().getCompanyName(), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
        }
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "ROUTED VIA:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPaddingLeft(10f);
        p = new Paragraph(8f, "" + lclUtils.getConcatenatedPortOfDestination(lclBooking).toUpperCase(), blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_LEFT);
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //destination
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "DESTINATION:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPaddingLeft(10f);
        p = new Paragraph(8f, "" + lclUtils.getConcatenatedFinalDestination(lclBooking).toUpperCase(), blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_LEFT);
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //cut offcreateEmptyCell
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
        p = new Paragraph(8f, "CUT OFF:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);
        StringBuilder cutoffDateNote = new StringBuilder();
        if (lclBookingPad != null && lclBookingPad.getPickupCutoffDate() != null) {
            cutoffDateNote.append(DateUtils.formatStringDateToAppFormatMMM(lclBookingPad.getPickupCutoffDate())).append("   ");
        } else {
            cutoffDateNote.append("        ");
        }
        if (lclBookingPad != null && lclBookingPad.getPickupReadyNote() != null) {
            cutoffDateNote.append("READY: ").append(lclBookingPad.getPickupReadyNote());
        } else {
            cutoffDateNote.append("       READY:");
        }
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPaddingLeft(10f);
        p = new Paragraph(8f, "" + cutoffDateNote.toString().toUpperCase(), blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_LEFT);
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //cut offcreateEmptyCell
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(4);
         p = new Paragraph(8f, "TERM OF SALE:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_RIGHT);
        starCell.addElement(p);
        pTable.addCell(starCell);
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPaddingLeft(10f);
        if (lclBookingPad != null && lclBookingPad.getTermsOfService() != null) {
            p = new Paragraph(8f, "" + lclBookingPad.getTermsOfService().toUpperCase(), blackNormalFont9);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
        }
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 5f, 7));
        //****

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(7);
        PdfPTable doubleStarTable = new PdfPTable(4);
        PdfPCell doubleStarCell = null;
        doubleStarTable.setWidthPercentage(95f);
        doubleStarTable.setWidths(new float[]{2.5f, 0.3f, 6f, 2.1f});

        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarTable.addCell(doubleStarCell);

        doubleStarCell = new PdfPCell();
        doubleStarCell.setColspan(3);
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        p = new Paragraph(8f, "*****************************************************", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        //
        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarTable.addCell(doubleStarCell);

        doubleStarCell = new PdfPCell();
        doubleStarCell.setColspan(3);
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        p = new Paragraph(8f, "*****************************************************", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        //1
        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarTable.addCell(doubleStarCell);

        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        doubleStarCell = new PdfPCell();
        doubleStarCell.setPadding(0f);
        doubleStarCell.setBorder(0);
        doubleStarTable.addCell(doubleStarCell);
        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarCell.setPaddingLeft(3f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        //2
        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarTable.addCell(doubleStarCell);

        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        doubleStarCell = new PdfPCell();
        doubleStarCell.setRowspan(4);
        doubleStarCell.setBorder(0);
        String comment2 = TR002 != null ? TR002 : "";
        p = new Paragraph(9f, "" + comment2, blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarCell.setPaddingLeft(3f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        //3
        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarTable.addCell(doubleStarCell);

        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);

        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarCell.setPaddingLeft(3f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        //
        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarTable.addCell(doubleStarCell);

        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);

        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarCell.setPaddingLeft(3f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        //4
        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarTable.addCell(doubleStarCell);

        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarCell.setPaddingLeft(3f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        //5
        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarTable.addCell(doubleStarCell);

        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        doubleStarCell = new PdfPCell();
        doubleStarCell.setPadding(0f);
        doubleStarCell.setBorder(0);
        p = new Paragraph(8f, "BOOKING#  " + bookingNumber.toString(), blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        doubleStarCell = new PdfPCell();
        doubleStarCell.setPadding(0f);
        doubleStarCell.setBorder(0);
        doubleStarCell.setPaddingLeft(3f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        //6
        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarTable.addCell(doubleStarCell);

        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        doubleStarCell = new PdfPCell();
        doubleStarCell.setPadding(0f);
        doubleStarCell.setBorder(0);
        doubleStarTable.addCell(doubleStarCell);
        doubleStarCell = new PdfPCell();
        doubleStarCell.setPadding(0f);
        doubleStarCell.setBorder(0);
        doubleStarCell.setPaddingLeft(3f);
        p = new Paragraph(8f, "**", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarTable.addCell(doubleStarCell);

        doubleStarCell = new PdfPCell();
        doubleStarCell.setColspan(3);
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        p = new Paragraph(8f, "*****************************************************", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);
        //
        doubleStarCell = new PdfPCell();
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        doubleStarTable.addCell(doubleStarCell);

        doubleStarCell = new PdfPCell();
        doubleStarCell.setColspan(3);
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        p = new Paragraph(8f, "*****************************************************", blackNormalCourierFont10f);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);

        //emptycell
        doubleStarTable.addCell(createEmptyCell(0, 8f, 7));


        doubleStarCell = new PdfPCell();
        doubleStarCell.setColspan(4);
        doubleStarCell.setBorder(0);
        doubleStarCell.setPadding(0f);
        String comment1 = TR001 != null ? TR001 : "";
        p = new Paragraph(8f, "" + comment1, blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        doubleStarCell.addElement(p);
        doubleStarTable.addCell(doubleStarCell);

        starCell.addElement(doubleStarTable);
        pTable.addCell(starCell);














        createCell.addElement(pTable);
        startTable.addCell(createCell);




        cell.addElement(startTable);
        borderTable.addCell(cell);
        document.add(borderTable);

        document.close();

    }
}
