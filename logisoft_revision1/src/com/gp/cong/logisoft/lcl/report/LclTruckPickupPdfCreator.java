package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
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
public class LclTruckPickupPdfCreator extends LclReportFormatMethods {

    private LclBooking lclBooking;
    private LclUtils lclUtils = new LclUtils();
    private String terminalNo = "";
    private String companyName = "";
    private String termLocation = "";
    private String phoneNumber = "";
    private StringBuilder bookingNumber = new StringBuilder();
    private StringBuilder terminalDetails = new StringBuilder();

    public LclTruckPickupPdfCreator(LclBooking lclBooking) throws Exception {
        this.lclBooking = lclBooking;
        terminalNo = lclBooking.getTerminal().getTrmnum();
        String cityCode;
        if (!lclBooking.getBookingType().equalsIgnoreCase("T")) {
            cityCode = lclBooking.getPortOfOrigin().getUnLocationCode().substring(2);
        } else {
            cityCode = lclBooking.getPortOfDestination().getUnLocationCode().substring(2);
        }

        if (cityCode != null) {
            bookingNumber.append(cityCode).append("-");
        }
        if (lclBooking.getLclFileNumber() != null && lclBooking.getLclFileNumber().getFileNumber() != null) {
            bookingNumber.append(lclBooking.getLclFileNumber().getFileNumber());
        }
        RefTerminal refTerminal = new RefTerminalDAO().findById(terminalNo);
        if (refTerminal.getTrmnam() != null) {
            companyName = refTerminal.getTrmnam();
        }
        if (refTerminal.getTerminalLocation() != null) {
            termLocation = refTerminal.getTerminalLocation();
        }
        if (refTerminal.getAddres1() != null) {
            terminalDetails.append(refTerminal.getAddres1()).append(" ");
        }
        if (refTerminal.getCity1() != null) {
            terminalDetails.append(refTerminal.getCity1()).append(" ");
        }
        if (refTerminal.getState() != null) {
            terminalDetails.append(refTerminal.getState()).append(" ");
        }
        if (refTerminal.getZipcde() != null) {
            terminalDetails.append(refTerminal.getZipcde());
        }
        if (refTerminal.getPhnnum1() != null) {
            String pNoSpaceRemove = StringUtils.remove(refTerminal.getPhnnum1(), " ");
            if (CommonUtils.isNotEmpty(pNoSpaceRemove)) {
                String ph1 = pNoSpaceRemove.substring(0, 3);
                String ph2 = pNoSpaceRemove.substring(3, 6);
                String ph3 = pNoSpaceRemove.substring(6);
                phoneNumber = "(" + ph1 + ") " + ph2 + "-" + ph3;
            }
        }
    }

    public void lclTruckPickupreport(String outputFileName) throws DocumentException, FileNotFoundException, Exception {
        StringBuilder commodityListValues = new StringBuilder();
        StringBuilder deliverCargo = new StringBuilder();
        String phoneNumberDeliver = null;
        LclBookingPad lclBookingPad = new LclBookingPadDAO().getLclBookingPadByFileNumber(lclBooking.getFileNumberId());
        if (lclBookingPad != null && lclBookingPad.getDeliveryContact() != null && lclBookingPad.getDeliveryContact().getId() != null) {
            if (lclBookingPad.getDeliveryContact().getCompanyName() != null) {
                deliverCargo.append(lclBookingPad.getDeliveryContact().getCompanyName()).append("\n");
            }
            if (lclBookingPad.getDeliveryContact().getAddress() != null) {
                deliverCargo.append(lclBookingPad.getDeliveryContact().getAddress());
            }
            if (lclBookingPad.getDeliveryContact().getCity() != null) {
                deliverCargo.append(lclBookingPad.getDeliveryContact().getCity()).append(", ");
            }
            if (lclBookingPad.getDeliveryContact().getState() != null) {
                deliverCargo.append(lclBookingPad.getDeliveryContact().getState()).append(" ");
            }
            if (lclBookingPad.getDeliveryContact().getZip() != null) {
                deliverCargo.append(lclBookingPad.getDeliveryContact().getZip());
            }
            if (lclBookingPad.getDeliveryContact().getPhone1() != null) {
                String pNoSpaceRemove = StringUtils.remove(lclBookingPad.getDeliveryContact().getPhone1(), " ");
                String ph1 = pNoSpaceRemove.substring(0, 3);
                String ph2 = pNoSpaceRemove.substring(3, 6);
                String ph3 = pNoSpaceRemove.substring(6);
                phoneNumberDeliver = "(" + ph1 + ") " + ph2 + "-" + ph3;
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
                commodityListValues.append("POUNDS:  ").append(NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getActualWeightImperial().doubleValue())).append("     ");
            } else if (lclBookingPiece != null && lclBookingPiece.getBookedWeightImperial() != null) {
                commodityListValues.append("POUNDS:  ").append(NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getBookedWeightImperial().doubleValue())).append("      ");
            } else {
                commodityListValues.append("POUNDS:      ").append("               ");
            }
            if (lclBookingPiece != null && lclBookingPiece.getActualVolumeImperial() != null) {
                commodityListValues.append("CFT:    ").append(NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getActualVolumeImperial().doubleValue())).append("         ");
            } else if (lclBookingPiece != null && lclBookingPiece.getBookedVolumeImperial() != null) {
                commodityListValues.append("CFT:  ").append(NumberUtils.convertToThreeDecimalhash(lclBookingPiece.getBookedVolumeImperial().doubleValue())).append("          ");
            } else {
                commodityListValues.append("CFT:    ").append("              ");
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

        StringBuilder cityStateZip = new StringBuilder();
        StringBuilder phonefaxDetails = new StringBuilder();
        if (lclBookingPad != null && lclBookingPad.getPickupContact().getId() != null) {
            if (lclBookingPad.getPickupContact().getCity() != null) {
                cityStateZip.append(lclBookingPad.getPickupContact().getCity());
            } else {
                cityStateZip.append("");
            }

            if (CommonUtils.isNotEmpty(lclBookingPad.getPickupContact().getCity())
                    && CommonUtils.isNotEmpty(lclBookingPad.getPickupContact().getState())) {
                cityStateZip.append(", ");
            }
            if (lclBookingPad.getPickupContact().getState() != null) {
                cityStateZip.append(lclBookingPad.getPickupContact().getState());
            }
            if (lclBookingPad.getPickupContact().getZip() != null) {
                cityStateZip.append("  ").append(lclBookingPad.getPickupContact().getZip());
            }
            if (lclBookingPad.getPickupContact().getPhone1() != null) {
                phonefaxDetails.append("PHN: ").append(lclBookingPad.getPickupContact().getPhone1());
            } else {
                phonefaxDetails.append("PHN: ").append("            ");
            }
            if (lclBookingPad.getPickupContact().getFax1() != null) {
                phonefaxDetails.append("    ").append("FAX: ").append(lclBookingPad.getPickupContact().getFax1());
            } else {
                phonefaxDetails.append("FAX: ").append("        ");
            }
        }
        PdfPCell createCell = null;
        Paragraph p = null;
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
        p = new Paragraph("TRUCK PICK-UP REQUEST", blackNormalCourierFont10f);
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
        pTable.addCell(createEmptyCell(0, 2f, 7));
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
        starCell.setColspan(2);
        starCell.setPaddingLeft(10f);
        if (lclBookingPad != null && CommonUtils.isNotEmpty(lclBookingPad.getPickUpTo())) {
            p = new Paragraph(8f, "" + lclBookingPad.getPickUpTo().toUpperCase(), blackNormalCourierFont10f);
            starCell.addElement(p);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
            starCell.addElement(p);
        }
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setPadding(0f);
        if (lclBookingPad != null && lclBookingPad.getScac() != null) {
            p = new Paragraph(8f, "" + lclBookingPad.getScac(), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_CENTER);
            starCell.addElement(p);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
            starCell.addElement(p);
        }
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 3f, 7));
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
        if (lclBookingPad != null && lclBookingPad.getPickupContact() != null && lclBookingPad.getPickupContact().getCompanyName() != null) {
            p = new Paragraph(8f, "" + lclBookingPad.getPickupContact().getCompanyName(), blackNormalCourierFont10f);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
        }
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 3f, 7));
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
        if (lclBookingPad != null && lclBookingPad.getPickupContact() != null && lclBookingPad.getPickupContact().getAddress() != null) {
            p = new Paragraph(8f, "" + lclBookingPad.getPickupContact().getAddress(), blackNormalCourierFont10f);
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
//
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(3);
        starCell.setPaddingLeft(10f);
        p = new Paragraph(8f, "" + cityStateZip.toString().toUpperCase(), blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_LEFT);
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 3f, 7));
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
        starCell.setPaddingLeft(10f);
        if (lclBookingPad != null && lclBookingPad.getPickupContact() != null && lclBookingPad.getPickupContact().getContactName() != null) {
            p = new Paragraph(8f, "" + lclBookingPad.getPickupContact().getContactName(), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
        }
        starCell.addElement(p);
        pTable.addCell(starCell);

        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(2);
        p = new Paragraph(8f, "" + phonefaxDetails.toString().toUpperCase(), blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_LEFT);
        starCell.addElement(p);
        pTable.addCell(starCell);


        //email
        //CONTACT
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
        if (lclBookingPad != null && lclBookingPad.getPickupContact() != null && lclBookingPad.getPickupContact().getEmail1() != null) {
            p = new Paragraph(8f, "" + lclBookingPad.getPickupContact().getEmail1(), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
        }
        starCell.addElement(p);
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 3f, 7));

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
        pTable.addCell(createEmptyCell(0, 3f, 7));
        //Dims Details
        if (null != lclBookingPiece && null != lclBookingPiece.getLclBookingPieceDetailList() && !lclBookingPiece.getLclBookingPieceDetailList().isEmpty()) {
            starCell = new PdfPCell();
            starCell.setBorder(0);
            starCell.setColspan(7);
            starCell.addElement(createDimsDetails(lclBookingPiece.getLclBookingPieceDetailList()));
            pTable.addCell(starCell);
        }

        //emptycell
        pTable.addCell(createEmptyCell(0, 3f, 7));
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
        if (lclBookingPad != null && lclBookingPad.getCommodityDesc() != null) {
            p = new Paragraph(8f, "" + lclBookingPad.getCommodityDesc(), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
            starCell.addElement(p);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
            starCell.addElement(p);
        }
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 3f, 7));
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
        if (lclBookingPad != null && lclBookingPad.getPickupHours() != null) {
            p = new Paragraph(8f, "" + lclBookingPad.getPickupHours(), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
            starCell.addElement(p);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
            starCell.addElement(p);
        }
        pTable.addCell(starCell);
        starCell = new PdfPCell();
        starCell.setBorder(0);
        starCell.setColspan(2);
        if (lclBookingPad != null && lclBookingPad.getPickupReadyDate() != null) {
            p = new Paragraph(8f, "READY:  " + DateUtils.formatStringDateToAppFormatMMM(lclBookingPad.getPickupReadyDate()), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
            starCell.addElement(p);
        } else {
            p = new Paragraph(8f, "READY:", blackNormalCourierFont10f);
            starCell.addElement(p);
        }
        pTable.addCell(starCell);
        //emptycell
        pTable.addCell(createEmptyCell(0, 3f, 7));
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
        if (lclBookingPad != null && lclBookingPad.getPickupReferenceNo() != null) {
            p = new Paragraph(8f, "" + lclBookingPad.getPickupReferenceNo().toUpperCase(), blackNormalCourierFont10f);
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
        if (lclBooking != null && lclBooking.getConsAcct() != null && lclBooking.getConsAcct().getAccountName() != null) {
            p = new Paragraph(8f, "" + lclBooking.getConsAcct().getAccountName(), blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
        } else {
            p = new Paragraph(8f, "   ", blackNormalCourierFont10f);
        }
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
            cutoffDateNote.append(DateUtils.formatStringDateToAppFormatMMM(lclBookingPad.getPickupCutoffDate()));
        }
        if (lclBookingPad != null && lclBookingPad.getPickupReadyNote() != null) {
            cutoffDateNote.append("  ").append(lclBookingPad.getPickupReadyNote());
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
            p = new Paragraph(8f, "" + lclBookingPad.getTermsOfService(), blackNormalFont9);
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

    private PdfPTable createDimsDetails(List<LclBookingPieceDetail> bookingPieceDetailList) throws DocumentException {
        Paragraph p = null;
        PdfPCell dimsCell = null;
        PdfPTable dimsTable = new PdfPTable(8);
        dimsTable.setWidthPercentage(100f);
        dimsTable.setWidths(new float[]{3f, 0.1f, .8f, .8f, .8f, .8f, 0.1f, 5.5f});

        dimsCell = new PdfPCell();
        dimsCell.setBorder(0);
        dimsTable.addCell(dimsCell);

        dimsCell = new PdfPCell();
        dimsCell.setBorder(0);
        p = new Paragraph(9f, "|", blackNormalFont13);
        dimsCell.addElement(p);
        dimsTable.addCell(dimsCell);

        dimsCell = new PdfPCell();
        dimsCell.setBorder(0);
        dimsCell.setColspan(4);
        p = new Paragraph(9f, "----------DIMS----------", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        dimsCell.addElement(p);
        dimsTable.addCell(dimsCell);

        dimsCell = new PdfPCell();
        dimsCell.setBorder(0);
        p = new Paragraph(9f, "|", blackNormalFont13);
        dimsCell.addElement(p);
        dimsTable.addCell(dimsCell);

        dimsCell = new PdfPCell();
        dimsCell.setBorder(0);
        dimsTable.addCell(dimsCell);
        createDims(dimsTable, dimsCell, p, "L", "W", "H", "PC");
        for (LclBookingPieceDetail bookPiece : bookingPieceDetailList) {
            createDims(dimsTable, dimsCell, p, NumberUtils.truncateTwoDecimal(bookPiece.getActualLength().doubleValue()),
                    NumberUtils.truncateTwoDecimal(bookPiece.getActualWidth().doubleValue()),
                    NumberUtils.truncateTwoDecimal(bookPiece.getActualHeight().doubleValue()),
                    NumberUtils.truncateTwoDecimal(bookPiece.getPieceCount().doubleValue()));
        }

        return dimsTable;
    }

    private void createDims(PdfPTable dimsTable, PdfPCell dimsCell, Paragraph p,
            String length, String width, String height, String pcs) {
        dimsCell = new PdfPCell();
        dimsCell.setBorder(0);
        dimsTable.addCell(dimsCell);

        dimsCell = new PdfPCell();
        dimsCell.setBorder(0);
        p = new Paragraph(9f, "|", blackNormalFont13);
        dimsCell.addElement(p);
        dimsTable.addCell(dimsCell);

        dimsCell = new PdfPCell();
        dimsCell.setBorder(0);
        p = new Paragraph(9f, "" + length, blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        dimsCell.addElement(p);
        dimsTable.addCell(dimsCell);



        dimsCell = new PdfPCell();
        dimsCell.setBorder(0);
        p = new Paragraph(9f, "" + width, blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        dimsCell.addElement(p);
        dimsTable.addCell(dimsCell);



        dimsCell = new PdfPCell();
        dimsCell.setBorder(0);
        p = new Paragraph(9f, "" + height, blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        dimsCell.addElement(p);
        dimsTable.addCell(dimsCell);


        dimsCell = new PdfPCell();
        dimsCell.setBorder(0);
        p = new Paragraph(9f, "" + pcs, blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        dimsCell.addElement(p);
        dimsTable.addCell(dimsCell);

        dimsCell = new PdfPCell();
        dimsCell.setBorder(0);
        p = new Paragraph(9f, "|", blackNormalFont13);
        dimsCell.addElement(p);
        dimsTable.addCell(dimsCell);

        dimsCell = new PdfPCell();
        dimsCell.setBorder(0);
        dimsTable.addCell(dimsCell);
    }
}
