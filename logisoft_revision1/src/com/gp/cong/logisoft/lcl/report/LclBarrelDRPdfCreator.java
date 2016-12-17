package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Meiyazhakan
 */
public class LclBarrelDRPdfCreator extends LclReportFormatMethods {

    private LclBooking lclBooking;
    private Image img;
    // private String realPath;

    public LclBarrelDRPdfCreator(LclBooking lclBooking, String realPath) throws Exception {
        this.lclBooking = lclBooking;
        img = Image.getInstance(realPath + "/img/reports/blackTickIcon.gif");
    }

    public void createReport(String realPath, String outputFileName,
            String documentName, HttpServletRequest request) throws Exception {
        document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(1, 1, 20, 8);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        document.open();
        document.add(content1(realPath, request));
        document.add(content2());
        document.add(content3());
        document.add(content4());
        document.add(footerTable());
        document.close();
    }

    public PdfPTable content1(String realPakth, HttpServletRequest request) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        Date now = new Date();
        RefTerminal terminal = loginUser.getTerminal();
        StringBuilder userAddress = new StringBuilder();
        if (CommonUtils.isNotEmpty(terminal.getAddres1())) {
            userAddress.append(terminal.getAddres1()).append("\n");
        }
        if (CommonUtils.isNotEmpty(terminal.getCity1())) {
            userAddress.append(terminal.getCity1());
        }
        if (CommonUtils.isNotEmpty(terminal.getState())) {
            userAddress.append(" ").append(terminal.getState());
        }
        if (CommonUtils.isNotEmpty(terminal.getZipcde())) {
            userAddress.append(" ").append(terminal.getZipcde());
        }
        if (CommonUtils.isNotEmpty(terminal.getPhnnum1())) {
            userAddress.append("\n").append("Phn: ").append(terminal.getPhnnum1());
        }
        if (CommonUtils.isNotEmpty(terminal.getFaxnum1())) {
            userAddress.append("   Fax: ").append(terminal.getFaxnum1());
        }
        String barrelEmail = LoadLogisoftProperties.getProperty("barrel.email");
        if (CommonUtils.isNotEmpty(barrelEmail)) {
            userAddress.append("\n").append("E-Mail: ").append(barrelEmail);
        }
        userAddress.append("\n").append("Printed On: ").append(DateUtils.formatDate(now, "dd-MMM-yyyy HH:mm:ss"));

        Font blackNormalFont8 = FontFactory.getFont("Arial", 12f, Font.NORMAL);

        Paragraph p = null;
        table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{3f, 2.5f});
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-10f);
        cell.setPaddingBottom(55f);
        p = new Paragraph(11f, "" + userAddress.toString(), blackNormalCourierFont12f);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
//        p = new Paragraph("YOUR PERSONAL SHIPPING SOLUTION TO THE CARIBBEAN AND LATIN AMERICA", headingFont);
//        p.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable content2() throws Exception {


        StringBuilder shipperValues = new StringBuilder();
        if (null != lclBooking.getShipAcct()) {
            shipperValues.append(lclBooking.getShipAcct().getAccountName());
            if (lclBooking.getShipContact() != null) {
                if (CommonUtils.isNotEmpty(lclBooking.getShipContact().getAddress())) {
                    shipperValues.append("\n").append(lclBooking.getShipContact().getAddress());
                }
                if (CommonUtils.isNotEmpty(lclBooking.getShipContact().getCity())) {
                    shipperValues.append("\n").append(lclBooking.getShipContact().getCity());
                }
                if (CommonUtils.isNotEmpty(lclBooking.getShipContact().getState())) {
                    shipperValues.append(" ").append(lclBooking.getShipContact().getState());
                }
                if (CommonUtils.isNotEmpty(lclBooking.getShipContact().getZip())) {
                    shipperValues.append(" ").append(lclBooking.getShipContact().getZip());
                }
                if (CommonUtils.isNotEmpty(lclBooking.getShipContact().getPhone1())) {
                    shipperValues.append("\nPhone: ").append(lclBooking.getShipContact().getPhone1());
                }
            }
        }
        StringBuilder consValues = new StringBuilder();
        if (null != lclBooking.getConsAcct()) {
            consValues.append(lclBooking.getConsAcct().getAccountName());
            if (lclBooking.getConsContact() != null) {
                if (CommonUtils.isNotEmpty(lclBooking.getConsContact().getAddress())) {
                    consValues.append("\n").append(lclBooking.getConsContact().getAddress());
                }
                if (CommonUtils.isNotEmpty(lclBooking.getConsContact().getCity())) {
                    consValues.append("\n").append(lclBooking.getConsContact().getCity());
                }
                if (CommonUtils.isNotEmpty(lclBooking.getConsContact().getState())) {
                    consValues.append(" ").append(lclBooking.getConsContact().getState());
                }
                if (CommonUtils.isNotEmpty(lclBooking.getConsContact().getZip())) {
                    consValues.append(" ").append(lclBooking.getConsContact().getZip());
                }
                if (CommonUtils.isNotEmpty(lclBooking.getConsContact().getPhone1())) {
                    consValues.append("\nPhone: ").append(lclBooking.getConsContact().getPhone1());
                }
            }
        }

        StringBuilder fdValues = new StringBuilder();
        if (CommonFunctions.isNotNull(lclBooking) && CommonFunctions.isNotNull(lclBooking.getFinalDestination())) {
            if (CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationCode())) {
                fdValues.append(lclBooking.getFinalDestination().getUnLocationCode()).append(" - ");
            }
            if (CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getUnLocationName())) {
                fdValues.append(lclBooking.getFinalDestination().getUnLocationName());
            }
            if (CommonUtils.isNotEmpty(lclBooking.getFinalDestination().getCountryId().getCodedesc())) {
                fdValues.append(", ").append(lclBooking.getFinalDestination().getCountryId().getCodedesc());
            }
        }
        Font headingFont = FontFactory.getFont("Arial", 12f, Font.NORMAL);
        Font blackNormalFont8 = FontFactory.getFont("Arial", 8f, Font.NORMAL);
        String cityCode;
        Paragraph p = null;
        PdfPCell pCell = null;
        PdfPTable pTable = null;
        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{4.5f, 0.5f, 2.5f});
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setPadding(0f);
        cell.setPaddingTop(-4f);
        cell.setPaddingBottom(1f);
//        p = new Paragraph("RECEIPT FOR MERCHANDISE", headingFont);
//        p.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setRowspan(5);
        cell.setPaddingLeft(40);
        cell.setPaddingTop(10f);
        p = new Paragraph(11f, "" + shipperValues.toString().toUpperCase(), blackNormalCourierFont12f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(14);
        cell.setPaddingTop(3);
        //img.scalePercent(50);
        //img.setAlignment(Element.ALIGN_LEFT);
        //  cell.addElement(img);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-1f);
        cell.setPaddingBottom(2f);
//        p = new Paragraph("SEA", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_LEFT);
//        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(14);
        cell.setPaddingTop(10);
        //img.scalePercent(50);
        //img.setAlignment(Element.ALIGN_LEFT);
        //  cell.addElement(img);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-1f);
        cell.setPaddingBottom(2f);
//        p = new Paragraph("AIR FREIGHT AWB#", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_LEFT);
//        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setPaddingTop(-1f);
        cell.setPaddingBottom(2f);
//        p = new Paragraph("DOCUMENT NO:", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_LEFT);
//        cell.addElement(p);
        table.addCell(cell);
        if (null != lclBooking.getPortOfOrigin()) {
            cityCode = lclBooking.getPortOfOrigin().getUnLocationCode().substring(2);
        } else {
            cityCode = lclBooking.getPortOfDestination().getUnLocationCode().substring(2);
        }
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setPaddingTop(30);
        p = new Paragraph("" + cityCode + "-" + lclBooking.getLclFileNumber().getFileNumber(), blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        //  cell.setBorderWidthRight(0.06f);
        pTable = new PdfPTable(5);
        pTable.setWidthPercentage(100f);
        pTable.setWidths(new float[]{0.7f, 0.5f, 0.8f, 0.5f, 1.5f});
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingTop(-1f);
        pCell.setPaddingBottom(2f);
//        p = new Paragraph("TERMS:", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_LEFT);
//        pCell.addElement(p);
        pTable.addCell(pCell);
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingLeft(5);
        pCell.setPaddingTop(9);
//        img.scalePercent(50);
//        img.setAlignment(Element.ALIGN_LEFT);
//        pCell.addElement(img);
        pTable.addCell(pCell);
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingTop(-1f);
        pCell.setPaddingBottom(2f);
//        p = new Paragraph("CASH", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_LEFT);
//        pCell.addElement(p);
        pTable.addCell(pCell);
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingLeft(27);
        pCell.setPaddingTop(10);
//        img.scalePercent(50);
//        img.setAlignment(Element.ALIGN_LEFT);
//        pCell.addElement(img);
        pTable.addCell(pCell);
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingTop(-1f);
        pCell.setPaddingBottom(2f);
//        p = new Paragraph("FREIGHT COLLECT", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_LEFT);
//        pCell.addElement(p);
        pTable.addCell(pCell);

        cell.addElement(pTable);
        table.addCell(cell);

        //2nd cell
        cell = new PdfPCell();
        cell.setRowspan(5);
        cell.setBorder(0);
        cell.setPaddingLeft(40);
        cell.setPaddingTop(20);
        p = new Paragraph(11f, "" + consValues.toString().toUpperCase(), blackNormalCourierFont12f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        // cell.setBorderWidthRight(0.06f);
        pTable = new PdfPTable(5);
        pTable.setWidthPercentage(100f);
        pTable.setWidths(new float[]{0.7f, 0.5f, 0.8f, 0.5f, 1.5f});
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingTop(-1f);
        pCell.setPaddingBottom(2f);
        p = new Paragraph("", blackNormalFont8);
        p.setAlignment(Element.ALIGN_LEFT);
        pCell.addElement(p);
        pTable.addCell(pCell);
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingLeft(5);
        pCell.setPaddingTop(6);
//        img.scalePercent(50);
//        img.setAlignment(Element.ALIGN_LEFT);
//        pCell.addElement(img);
        pTable.addCell(pCell);
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingTop(-1f);
        pCell.setPaddingBottom(2f);
//        p = new Paragraph("CHECK", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_LEFT);
//        pCell.addElement(p);
        pTable.addCell(pCell);
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingLeft(27);
        pCell.setPaddingTop(6);
//        img.scalePercent(50);
//        img.setAlignment(Element.ALIGN_LEFT);
//        pCell.addElement(img);
        pTable.addCell(pCell);
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingTop(-1f);
        pCell.setPaddingBottom(2f);
//        p = new Paragraph("CREDIT CARD", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_LEFT);
//        pCell.addElement(p);
        pTable.addCell(pCell);

        cell.addElement(pTable);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setPaddingTop(-1f);
        cell.setPaddingBottom(2f);
//        p = new Paragraph("FINAL DESTINATION:", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_LEFT);
//        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setPaddingLeft(15f);
        cell.setPaddingTop(30f);
        p = new Paragraph(10f, "" + fdValues.toString().toUpperCase(), blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setPaddingTop(-1f);
        cell.setPaddingBottom(2f);
//        p = new Paragraph("DESTINATION:", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_LEFT);
//        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        pTable = new PdfPTable(4);
        pTable.setWidthPercentage(100f);
        pTable.setWidths(new float[]{0.5f, 1f, 0.5f, 1f});
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingLeft(19);
        pCell.setPaddingTop(15);
//        img.scalePercent(50);
//        img.setAlignment(Element.ALIGN_LEFT);
//        pCell.addElement(img);
        pTable.addCell(pCell);
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingTop(-1f);
        pCell.setPaddingBottom(2f);
//        p = new Paragraph("DOOR TO DOOR", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_LEFT);
//        pCell.addElement(p);
        pTable.addCell(pCell);
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingLeft(29);
        pCell.setPaddingTop(15);
//        img.scalePercent(50);
//        img.setAlignment(Element.ALIGN_LEFT);
//        pCell.addElement(img);
        pTable.addCell(pCell);
        pCell = new PdfPCell();
        pCell.setBorder(0);
        pCell.setPaddingTop(-1f);
        pCell.setPaddingBottom(2f);
//        p = new Paragraph("DOOR TO PORT", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_LEFT);
//        pCell.addElement(p);
        pTable.addCell(pCell);

        cell.addElement(pTable);
        table.addCell(cell);

        return table;
    }

    public PdfPTable content3() throws Exception {
        PdfPTable ntable = new PdfPTable(1);
        ntable.setWidthPercentage(100f);
        PdfPCell ncell = new PdfPCell();
        ncell.setFixedHeight(250f);
        ncell.setBorder(0);
        int commodityCount = 0;
        Font headingFont = FontFactory.getFont("Arial", 12f, Font.NORMAL);
        Font blackNormalFont8 = blackNormalCourierFont10f;
        Paragraph p = null;
        table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 3.4f, 0.8f, 0.5f, 0.9f, 0.5f, 1f, 1f});

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(8);
        cell.setPadding(0f);
        cell.setPaddingTop(50f);
        cell.setPaddingBottom(10f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setRowspan(2);
        cell.setPadding(0f);
        cell.setBorder(0);
//        cell.setBorderWidthLeft(1f);
//        cell.setBorderWidthRight(1f);
//        cell.setBorderWidthBottom(1f);
//        cell.setBorderWidthTop(1f);
//        cell.setPaddingTop(4f);
//        cell.setPaddingBottom(1f);
//        p = new Paragraph(8f,"", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setRowspan(2);
        cell.setPadding(0f);
        cell.setBorder(0);
//        cell.setBorderWidthRight(1f);
//        cell.setBorderWidthBottom(1f);
//        cell.setBorderWidthTop(1f);
//        cell.setPaddingTop(4f);
//        cell.setPaddingBottom(1f);
//        p = new Paragraph("", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setPadding(0f);
//        cell.setPaddingTop(4f);
//        cell.setPaddingBottom(1f);
//        cell.setBorderWidthRight(1f);
//        cell.setBorderWidthTop(1f);
//        p = new Paragraph("", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setRowspan(2);
        // cell.setPadding(0f);
        cell.setBorder(0);
//        cell.setBorderWidthRight(1f);
//        cell.setBorderWidthBottom(1f);
//        cell.setBorderWidthTop(1f);
//        cell.setPaddingTop(4f);
//        cell.setPaddingBottom(1f);
//        p = new Paragraph("", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setRowspan(2);
        cell.setBorder(0);
//        cell.setPadding(0f);
//        cell.setBorderWidthRight(1f);
//        cell.setBorderWidthBottom(1f);
//        cell.setBorderWidthTop(1f);
//        cell.setPaddingTop(4f);
//        cell.setPaddingBottom(1f);
//        p = new Paragraph("", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(p);
        table.addCell(cell);
        //2nd cell
        cell = new PdfPCell();
        cell.setBorder(0);
//        cell.setPaddingTop(-1f);
//        cell.setPaddingBottom(3f);
//        cell.setBorderWidthBottom(1f);
//        p = new Paragraph("", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
//        cell.setPaddingTop(-1f);
//        cell.setPaddingBottom(3f);
//        cell.setBorderWidthBottom(1f);
//        p = new Paragraph("", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
//        cell.setPaddingTop(-1f);
//        cell.setPaddingBottom(3f);
//        cell.setBorderWidthBottom(1f);
//        p = new Paragraph("", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
//        cell.setPaddingTop(-1f);
//        cell.setPaddingBottom(3f);
//        cell.setBorderWidthBottom(1f);
//        cell.setBorderWidthRight(1f);
//        p = new Paragraph("", blackNormalFont8);
//        p.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(p);
        table.addCell(cell);

        List<LclBookingPiece> lclBookingPieceList = lclBooking.getLclFileNumber().getLclBookingPieceList();
        if (!lclBookingPieceList.isEmpty()) {
            for (LclBookingPiece lclBookingPiece : lclBookingPieceList) {
                commodityCount++;
                //3r cell
                //for (int i = 0; i < 6; i++) {
                cell = new PdfPCell();
                cell.setBorder(0);
                //  cell.setBorderWidthLeft(1f);
                //cell.setBorderWidthRight(1f);
                cell.setPadding(15f);
                String packageDesc = "";
                if (lclBookingPiece != null && lclBookingPiece.getActualPieceCount() != null && 0 != lclBookingPiece.getActualPieceCount()
                        && lclBookingPiece.getActualPackageType() != null) {
                    if (lclBookingPiece.getActualPieceCount() <= 1) {
                        packageDesc = lclBookingPiece.getActualPackageType().getDescription();
                    } else {
                        packageDesc = lclBookingPiece.getActualPackageType().getDescription() + "" + lclBookingPiece.getPackageType().getPlural().toLowerCase();
                    }
                    p = new Paragraph(8f, "" + lclBookingPiece.getActualPieceCount() + " " + packageDesc, blackNormalFont8);
                } else if (lclBookingPiece != null && lclBookingPiece.getBookedPieceCount() != null && lclBookingPiece.getPackageType() != null) {
                    if (lclBookingPiece.getBookedPieceCount() <= 1) {
                        packageDesc = lclBookingPiece.getPackageType().getDescription();
                    } else {
                        packageDesc = lclBookingPiece.getPackageType().getDescription() + "" + lclBookingPiece.getPackageType().getPlural().toLowerCase();
                    }
                    p = new Paragraph(8f, "" + lclBookingPiece.getBookedPieceCount() + " " + packageDesc, blackNormalFont8);
                }
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setPadding(15f);

                // cell.setBorderWidthRight(1f);
                if (CommonUtils.isNotEmpty(lclBookingPiece.getPieceDesc())) {
                    p = new Paragraph(8f, "" + lclBookingPiece.getPieceDesc().toUpperCase(), blackNormalFont8);
                } else {
                    p = new Paragraph(8f, "", blackNormalFont8);
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);

                ///measure Values
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setColspan(4);
                //  cell.setBorderWidthRight(1f);
                PdfPTable nTable = new PdfPTable(4);
                nTable.setWidthPercentage(100f);
                PdfPCell nCell = null;

                List<LclBookingPieceDetail> lclBookingPieceDetailsList = lclBookingPiece.getLclBookingPieceDetailList();
                if (!lclBookingPieceDetailsList.isEmpty()) {
//                    commodityCount--;
                    for (int i = 0; i < lclBookingPieceDetailsList.size(); i++) {
                        LclBookingPieceDetail lclBookingPieceDetail = (LclBookingPieceDetail) lclBookingPieceDetailsList.get(i);
                        nCell = new PdfPCell();
                        nCell.setBorder(0);
                        cell.setPadding(10f);
                        if (lclBookingPieceDetail != null && lclBookingPieceDetail.getActualLength() != null) {
                            p = new Paragraph(8f, "" + NumberUtils.truncateTwoDecimal(lclBookingPieceDetail.getActualLength().doubleValue()), blackNormalFont8);
                        }
                        p.setAlignment(Element.ALIGN_RIGHT);
                        nCell.addElement(p);
                        nTable.addCell(nCell);
                        nCell = new PdfPCell();
                        nCell.setBorder(0);
                        cell.setPadding(10f);
                        if (lclBookingPieceDetail != null && lclBookingPieceDetail.getActualWidth() != null) {
                            p = new Paragraph(8f, "" + NumberUtils.truncateTwoDecimal(lclBookingPieceDetail.getActualWidth().doubleValue()), blackNormalFont8);
                        }
                        p.setAlignment(Element.ALIGN_RIGHT);
                        nCell.addElement(p);
                        nTable.addCell(nCell);
                        nCell = new PdfPCell();
                        nCell.setBorder(0);
                        cell.setPadding(10f);
                        if (lclBookingPieceDetail != null && lclBookingPieceDetail.getActualHeight() != null) {
                            p = new Paragraph(8f, "" + NumberUtils.truncateTwoDecimal(lclBookingPieceDetail.getActualHeight().doubleValue()), blackNormalFont8);
                        }
                        p.setAlignment(Element.ALIGN_RIGHT);
                        nCell.addElement(p);
                        nTable.addCell(nCell);
                        nCell = new PdfPCell();
                        nCell.setBorder(0);
                        cell.setPadding(10f);
                        if (lclBookingPieceDetail != null && lclBookingPieceDetail.getPieceCount() != null) {
                            p = new Paragraph(8f, "" + NumberUtils.truncateTwoDecimal(lclBookingPieceDetail.getPieceCount().doubleValue()), blackNormalFont8);
                        }
                        p.setAlignment(Element.ALIGN_RIGHT);
                        nCell.addElement(p);
                        nTable.addCell(nCell);
                    }
//                    if (lclBookingPieceDetailsList.size() % 2 != 0) {
//
//                        nCell = new PdfPCell();
//                        nCell.setBorder(0);
//                        p = new Paragraph("", blackNormalFont8);
//                        p.setAlignment(Element.ALIGN_CENTER);
//                        nCell.addElement(p);
//                        nTable.addCell(nCell);
//                        nCell = new PdfPCell();
//                        nCell.setBorder(0);
//                        p = new Paragraph("", blackNormalFont8);
//                        p.setAlignment(Element.ALIGN_CENTER);
//                        nCell.addElement(p);
//                        nTable.addCell(nCell);
//                        nCell = new PdfPCell();
//                        nCell.setBorder(0);
//                        p = new Paragraph("", blackNormalFont8);
//                        p.setAlignment(Element.ALIGN_CENTER);
//                        nCell.addElement(p);
//                        nTable.addCell(nCell);
//                        nCell = new PdfPCell();
//                        nCell.setBorder(0);
//                        p = new Paragraph("", blackNormalFont8);
//                        p.setAlignment(Element.ALIGN_CENTER);
//                        nCell.addElement(p);
//                        nTable.addCell(nCell);
//                    }
                } else {
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    p = new Paragraph("", blackNormalFont8);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    p = new Paragraph("", blackNormalFont8);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    p = new Paragraph("", blackNormalFont8);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    p = new Paragraph("", blackNormalFont8);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    p = new Paragraph("", blackNormalFont8);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    p = new Paragraph("", blackNormalFont8);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    p = new Paragraph("", blackNormalFont8);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);
                    nCell = new PdfPCell();
                    nCell.setBorder(0);
                    p = new Paragraph("", blackNormalFont8);
                    p.setAlignment(Element.ALIGN_CENTER);
                    nCell.addElement(p);
                    nTable.addCell(nCell);

                }
                cell.addElement(nTable);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                // cell.setBorderWidthRight(1f);
                cell.setPadding(15f);
                cell.setPaddingRight(10f);
                if (lclBookingPiece.getActualVolumeImperial() != null) {
                    p = new Paragraph(8f, "" + NumberUtils.roundDecimalToInteger(lclBookingPiece.getActualVolumeImperial().doubleValue()), blackNormalFont8);
                } else if (lclBookingPiece.getBookedVolumeImperial() != null) {
                    p = new Paragraph(8f, "" + NumberUtils.roundDecimalToInteger(lclBookingPiece.getBookedVolumeImperial().doubleValue()), blackNormalFont8);
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setPadding(15f);
                // cell.setBorderWidthRight(1f);
                cell.setPaddingRight(10f);
                if (lclBookingPiece != null && lclBookingPiece.getActualWeightImperial() != null) {
                    p = new Paragraph(8f, "" + NumberUtils.roundDecimalToInteger(lclBookingPiece.getActualWeightImperial().doubleValue()), blackNormalFont8);
                } else if (lclBookingPiece != null && lclBookingPiece.getBookedWeightImperial() != null) {
                    p = new Paragraph(8f, "" + NumberUtils.roundDecimalToInteger(lclBookingPiece.getBookedWeightImperial().doubleValue()), blackNormalFont8);
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);
            }
        }

        for (int i = 0; i < (6 - commodityCount); i++) {
            cell = new PdfPCell();
            cell.setPadding(12);
            cell.setBorder(0);
            cell.setColspan(8);
            table.addCell(cell);
        }

        // }
        //4th cell
        cell = new PdfPCell();
        cell.setBorder(0);
//        cell.setBorderWidthLeft(1f);
//        cell.setBorderWidthRight(1f);
//        cell.setBorderWidthTop(1f);
//        cell.setBorderWidthBottom(1f);
        cell.setPaddingTop(5f);
        cell.setPaddingBottom(8f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        // cell.setBorderWidthRight(1f);
        //cell.setBorderWidthTop(1f);
        // cell.setBorderWidthBottom(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        //  cell.setBorderWidthTop(1f);
        //cell.setBorderWidthBottom(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        // cell.setBorderWidthTop(1f);
        //cell.setBorderWidthBottom(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        // cell.setBorderWidthTop(1f);
        //cell.setBorderWidthBottom(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        //  cell.setBorderWidthRight(1f);
        // cell.setBorderWidthTop(1f);
        //cell.setBorderWidthBottom(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        //  cell.setBorderWidthTop(1f);
        //cell.setBorderWidthBottom(1f);
        //cell.setBorderWidthRight(1f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        // cell.setBorderWidthRight(1f);
        //cell.setBorderWidthTop(1f);
        //cell.setBorderWidthBottom(1f);
        table.addCell(cell);

        ncell.addElement(table);
        ntable.addCell(ncell);
        return ntable;
    }

    public PdfPTable content4() throws Exception {
        Font headingFontt = FontFactory.getFont("Arial", 12f, Font.BOLD);
        Font blackNormalFont8 = FontFactory.getFont("Arial", 12f, Font.NORMAL);
        Font blackNormalFont9 = FontFactory.getFont("Arial", 8f, Font.NORMAL);
        Font headingFont = FontFactory.getFont("Arial", 12f, Font.NORMAL);
        Paragraph p = null;
        table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1f, 0.5f, 1f, 0.5f, 1f, 0.5f, 1.2f, 2.2f});

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setPaddingBottom(20f);
        cell.setBorder(0);
        table.addCell(cell);



        LclRemarks externalRemarks = new LclRemarksDAO().getRemarks(lclBooking.getFileNumberId(), "E", "");
        StringBuilder content = new StringBuilder();
        if (null != externalRemarks && externalRemarks.getRemarks() != null) {
            content.append(externalRemarks.getRemarks()).append("\n");
        }
        LclSsHeader ssHeader = lclBooking.getBookedSsHeaderId();
        if (null != ssHeader) {
            StringBuilder bookedContent = new StringBuilder();
            LclSsDetail ssDetail = ssHeader.getVesselSsDetail();
            bookedContent.append("Booked For Voyage: ").append(ssHeader.getScheduleNo()).append("\n");
            bookedContent.append("Vessel: ").append(ssDetail.getSpReferenceName()).append("\n");
            bookedContent.append("SS VOY: ").append(ssDetail.getSpReferenceNo()).append("      SS LINE: ").append(ssDetail.getSpAcctNo().getAccountName()).append("\n");
            String state = null != ssDetail.getDeparture().getStateId() ? ssDetail.getDeparture().getStateId().getCode() : "";
            bookedContent.append("Sails From: ").append(ssDetail.getDeparture().getUnLocationName()).append(",").append(state).append("\n");
            bookedContent.append("Sailing Date: ").append(DateUtils.formatStringDateToAppFormatMMM(ssDetail.getStd())).append("\n");
            content.append(bookedContent);
        }
        content.append("**   ALL CARGO TENDERED FOR TRANSPORTATION   **").append("\n");
        content.append("**        IS SUBJECT TO INSPECTION.          **");

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setFixedHeight(180f);
        cell.setBorder(0);
        cell.setPaddingLeft(25f);
        p = new Paragraph(10f, content.toString(), blackNormalCourierFont12f);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    public PdfPTable footerTable() throws Exception {
        Paragraph p = null;
        table = new PdfPTable(2);
        table.setWidthPercentage(103f);
        table.setWidths(new float[]{3f, 1.6f});
        StringBuilder agentDetails = new StringBuilder();
        if (null != lclBooking.getAgentAcct()) {
            CustAddress custAddress = new CustAddressDAO().findPrimeContact(lclBooking.getAgentAcct().getAccountno());
            if (CommonFunctions.isNotNull(custAddress)) {
                if (CommonFunctions.isNotNull(lclBooking.getAgentAcct().getAccountName())) {
                    agentDetails.append(lclBooking.getAgentAcct().getAccountName());
                }
                if (CommonFunctions.isNotNull(custAddress.getAddress1())) {
                    agentDetails.append("\n").append(custAddress.getAddress1()).append("\n");
                }
                if (CommonFunctions.isNotNull(custAddress.getCity1())) {
                    agentDetails.append(custAddress.getCity1());
                }
                if (CommonFunctions.isNotNull(custAddress.getState())) {
                    agentDetails.append("  ").append(custAddress.getState());
                }
                if (CommonFunctions.isNotNull(custAddress.getZip())) {
                    agentDetails.append("  ").append(custAddress.getZip());
                }
                if (CommonFunctions.isNotNull(custAddress.getPhone())) {
                    agentDetails.append("\n").append("Phone: ").append(custAddress.getPhone());
                }
            }
        }
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setFixedHeight(40f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, "" + agentDetails.toString(), blackNormalCourierFont12f);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }
}
