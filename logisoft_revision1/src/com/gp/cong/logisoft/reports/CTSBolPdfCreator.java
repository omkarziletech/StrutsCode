package com.gp.cong.logisoft.reports;

import com.gp.cong.common.DateUtils;
import com.logiware.domestic.DomesticBooking;
import com.logiware.domestic.DomesticCharges;
import com.logiware.domestic.DomesticChargesDAO;
import com.logiware.domestic.DomesticPurchaseOrder;
import java.awt.Color;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.BaseFont;
import java.text.*;

public class CTSBolPdfCreator extends ReportFormatMethods {

    Document document = null;

    public void initialize(String fileName) throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
        pdfWriter.setOpenAction(action);
    }

    public void createBody(DomesticBooking booking, List<DomesticPurchaseOrder> purchaseOrderList, String contextPath) throws DocumentException, MalformedURLException, IOException, Exception {
        String imagePath = "/img/companyLogo/primaryFrieght.jpg";
        Image img = Image.getInstance(contextPath + imagePath);
        PdfPCell mainCell = null;
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        img.scalePercent(60);
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Chunk(img, 220, -30));
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setMinimumHeight(60);
        table.addCell(cell);
        PdfPTable emptyTable = new PdfPTable(1);
        emptyTable.addCell(makeCellCenter(""));
        emptyTable.addCell(makeCellCenter(""));
        emptyTable.addCell(makeCellCenter(""));
        emptyTable.addCell(makeCellCenter(""));
        PdfPTable mainTable = makeTable(2);
        mainTable.setWidthPercentage(100);
        mainTable.setWidths(new float[]{50, 50});
        PdfPTable leftTable = makeTable(1);
        leftTable.setWidthPercentage(100);
        PdfPTable rightTable = makeTable(1);
        rightTable.setWidthPercentage(100);
        PdfPTable header = new PdfPTable(3);
        header.setWidthPercentage(100);
        header.setWidths(new float[]{30,40,30});
        String date = DateUtils.formatDate(new Date(), "dd-MMM-yyyy");
        cell = makeCell("DATE:"+date ,
                new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 0, 0f, 0f, 0f, 0f);
        header.addCell(cell);
        cell = makeCell("BILL OF LADDING",
                new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK), 1, 0f, 0f, 0f, 0f);
        header.addCell(cell);
        cell = makeCell("Page 1 of 1",
                new Font(Font.HELVETICA, 8, Font.BOLD, Color.BLACK), 2, 0f, 0f, 0f, 0f);
        header.addCell(cell);

        PdfPTable shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        cell = makeCell("SHIP FROM", new Font(1, 8, Font.BOLD, Color.WHITE), 1, 0.6f, 0f, 0.6f, 0.6f);
        cell.setBackgroundColor(Color.BLACK);
        mainTable.addCell(cell);

        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0.6f);
        mainCell.setBorderWidthRight(0.6f);
        mainCell.setBorderWidthBottom(0f);
        mainCell.setBorderWidthTop(0.6f);
        mainTable.addCell(mainCell);

        Paragraph para = new Paragraph();
        para.add(new Chunk("NAME: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getShipperName(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        shipper.addCell(para);
        para = new Paragraph();
        para.add(new Chunk("ADDRESS: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getShipperAddress1(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        para = new Paragraph();
        para.add(new Chunk("CITY/STATE/ZIP: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getShipperCity() + "," + booking.getShipperState() + "," + booking.getShipperZipcode(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        para = new Paragraph();
        para.add(new Chunk("SID#: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getShipperReference(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        para.add(new Chunk("            PHONE: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getShipperContactPhone(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        mainCell.addElement(shipper);
        mainTable.addCell(mainCell);

        shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthRight(0.6f);
        mainCell.setBorderWidthBottom(0.6f);
        mainCell.setBorderWidthTop(0f);
        cell = makeCell("BILL OF LADDING NUMBER: " + booking.getBookingNumber(), new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK), 0, 0f, 0f, 0f, 0f);
        shipper.addCell(cell);
        mainCell.addElement(shipper);
        mainTable.addCell(mainCell);


        cell = makeCell("SHIP TO", new Font(1, 8, Font.BOLD, Color.WHITE), 1, 0.6f, 0f, 0.6f, 0.6f);
        cell.setBackgroundColor(Color.BLACK);
        cell.setMinimumHeight(5);
        mainTable.addCell(cell);



        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthRight(0.6f);
        shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        para = new Paragraph();
        para.add(new Chunk("CARRIER NAME: ", new Font(1, 8, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getCarrierName(), new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        mainCell.setMinimumHeight(5);
        mainCell.addElement(shipper);
        mainTable.addCell(mainCell);

        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0.6f);
        mainCell.setBorderWidthRight(0.6f);
        shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        para = new Paragraph();
        para.add(new Chunk("NAME: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getConsigneeName(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        para = new Paragraph();
        para.add(new Chunk("ADDRESS: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getConsigneeAddress1(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        mainCell.addElement(shipper);
        mainCell.setLeading(0.3f, 0.3f);
        mainTable.addCell(mainCell);

        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthRight(0.6f);
        shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        para = new Paragraph();
        para.add(new Chunk("Trailer number: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk("", new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        para = new Paragraph();
        para.add(new Chunk("Seal number(s): ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk("", new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        mainCell.addElement(shipper);
        mainTable.addCell(mainCell);

        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0.6f);
        mainCell.setBorderWidthRight(0.6f);
        mainCell.setBorderWidthBottom(0.6f);
        mainCell.setBorderWidthTop(0f);
        shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        para = new Paragraph();
        para.add(new Chunk("CITY/STATE/ZIP: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getConsigneeCity() + "," + booking.getConsigneeState() + "," + booking.getConsigneeZipcode(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        para = new Paragraph();
        para.add(new Chunk("CID#: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getConsigneeReference(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        para.add(new Chunk("                       PHONE: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getConsigneeContactPhone(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        mainCell.addElement(shipper);
        mainTable.addCell(mainCell);

        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0f);
        mainCell.setBorderWidthRight(0.6f);
        mainCell.setBorderWidthTop(0.6f);
        shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        para = new Paragraph();
        para.add(new Chunk("SCAC: ", new Font(1, 8, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getScac(), new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        para = new Paragraph();
        para.add(new Chunk("Pro Number: ", new Font(1, 8, Font.BOLD, Color.BLACK)));
        para.add(new Chunk("", new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        mainCell.addElement(shipper);
        mainTable.addCell(mainCell);

        cell = makeCell("THIRD PARTY FRIEGHT CHARGES BILL TO", new Font(1, 8, 1, Color.WHITE), 1, 0.6f, 0f, 0.6f, 0.6f);
        cell.setBackgroundColor(Color.BLACK);
        mainTable.addCell(cell);

        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthRight(0.6f);
        mainTable.addCell(mainCell);


        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0.6f);
        mainCell.setBorderWidthRight(0.6f);
        shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        para = new Paragraph();
        para.add(new Chunk("NAME: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getBilltoName(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        para = new Paragraph();
        para.add(new Chunk("ADDRESS: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getBilltoAddress1(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        mainCell.addElement(shipper);
        mainTable.addCell(mainCell);


        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthBottom(0.6f);
        mainCell.setBorderWidthRight(0.6f);
        mainTable.addCell(mainCell);

        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0.6f);
        mainCell.setBorderWidthRight(0.6f);
        mainCell.setBorderWidthBottom(0.6f);
        shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        para = new Paragraph();
        para.add(new Chunk("CITY/STATE/ZIP: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk(booking.getBilltoCity() + "," + booking.getBilltoState() + "," + booking.getBilltoZipcode(), new Font(Font.HELVETICA, 9, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        mainCell.addElement(shipper);
        mainTable.addCell(mainCell);

        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthRight(0.6f);
        shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        para = new Paragraph();
        para.add(new Chunk("FRIEGHT CHARGES TERM:          ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK)));
        para.add(new Chunk("(freight charges are prepaid unless marked otherwise)", new Font(Font.HELVETICA, 7, Font.NORMAL, Color.BLACK)));
        shipper.addCell(para);
        mainCell.addElement(shipper);
        mainTable.addCell(mainCell);

        shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0.6f);
        mainCell.setBorderWidthRight(0.6f);
        mainCell.setBorderWidthTop(0f);
        cell = makeCell("SPECIAL INSTRUCTIONS: ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 0, 0f, 0f, 0f, 0f);
        shipper.addCell(cell);
        mainCell.addElement(shipper);
        mainTable.addCell(mainCell);

        shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthRight(0.6f);
        mainCell.setBorderWidthBottom(0.6f);
        para = new Paragraph();
        para.add(new Chunk("PREPAID:_____   COLLECT:_____   3RD PARTY:", new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK)));
        para.add(new Chunk("  X  ", new Font(Font.HELVETICA, 12, Font.UNDERLINE, Color.BLACK)));
        shipper.addCell(para);
        mainCell.addElement(shipper);
        mainTable.addCell(mainCell);


        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthLeft(0.6f);
        mainCell.setBorderWidthRight(0.6f);
        mainCell.setBorderWidthBottom(0.6f);
        mainCell.setMinimumHeight(30f);
        mainTable.addCell(mainCell);

        shipper = makeTable(1);
        shipper.setWidthPercentage(100);
        mainCell = makeCellleftNoBorderWithBoldFont("");
        mainCell.setBorderWidthRight(0.6f);
        mainCell.setBorderWidthBottom(0.6f);
        mainCell.setMinimumHeight(30f);
        cell = makeCell("Master Bill of Ladding: with attached \n underlying bill of ladding", new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK), 1, 0f, 0f, 0f, 0f);
        shipper.addCell(cell);
        mainCell.addElement(shipper);
        mainTable.addCell(mainCell);

        cell = makeCell("CUSTOMER ORDER INFORMATION", new Font(1, 8, 1, Color.WHITE), 1, 0.6f, 0f, 0.6f, 0.6f);
        cell.setBackgroundColor(Color.BLACK);
        cell.setColspan(2);
        mainTable.addCell(cell);


        PdfPTable orderTable = makeTable(6);
        orderTable.setWidthPercentage(100);
        orderTable.setWidths(new float[]{30f, 13f, 13f, 7f, 7f, 30f});
        cell = makeCell("CUSTOMER ORDER NUMBER ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0.6f, 0.6f);
        orderTable.addCell(cell);
        cell = makeCell("#PKGS ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0.6f, 0.6f);
        orderTable.addCell(cell);
        cell = makeCell("WEIGHT ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0.6f, 0.6f);
        orderTable.addCell(cell);
        cell = makeCell("PALLET/SLIP ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0.6f, 0.6f);
        cell.setColspan(2);
        orderTable.addCell(cell);
        cell = makeCell("ADDITIONAL SHIPPER INFO ", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0.6f, 0.6f);
        orderTable.addCell(cell);

        int size = 0;
        int packages = 0;
        double weight = 0;
        for (DomesticPurchaseOrder order : purchaseOrderList) {
            size++;
            packages += order.getPackageQuantity();
            weight += order.getWeight();
            cell = makeCell(order.getPurchaseOrderNo(), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            cell.setMinimumHeight(15f);
            orderTable.addCell(cell);
            cell = makeCell("" + order.getPackageQuantity(), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable.addCell(cell);
            cell = makeCell("" + order.getWeight(), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable.addCell(cell);
            cell = makeCell("PAL".equals(order.getHandlingUnitType()) ? "Y" : "N", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable.addCell(cell);
            cell = makeCell("PAL".equals(order.getHandlingUnitType()) ? "N" : "Y", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable.addCell(cell);
            cell = makeCell(order.getExtraInfo(), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable.addCell(cell);
        }
        for (int i = size; i < 6; i++) {
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            cell.setMinimumHeight(15f);
            orderTable.addCell(cell);
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable.addCell(cell);
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable.addCell(cell);
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable.addCell(cell);
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable.addCell(cell);
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable.addCell(cell);
        }
        cell = makeCell("GRAND TOTAL", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        cell.setMinimumHeight(15f);
        orderTable.addCell(cell);
        cell = makeCell("" + packages, new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable.addCell(cell);
        cell = makeCell("" + weight, new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable.addCell(cell);
        cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable.addCell(cell);
        cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable.addCell(cell);
        cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable.addCell(cell);
        cell = makeCell("CARRIER INFORMATION", new Font(1, 8, 1, Color.WHITE), 1, 0.6f, 0f, 0.6f, 0.6f);
        cell.setBackgroundColor(Color.BLACK);
        cell.setColspan(6);
        orderTable.addCell(cell);

        PdfPTable orderTable1 = makeTable(9);
        orderTable1.setWidthPercentage(100);
        orderTable1.setWidths(new float[]{9f, 9f, 9f, 9f, 14f, 5f, 25f, 10f, 10f});

        cell = makeCell("HANDLING UNIT", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
        cell.setColspan(2);
        cell.setMinimumHeight(15f);
        orderTable1.addCell(cell);
        cell = makeCell("PACKAGE", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        cell.setColspan(2);
        orderTable1.addCell(cell);
        cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0f, 0f, 0f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("COMMODITY DESCRIPTION", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("LTL ONLY", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        cell.setColspan(2);
        orderTable1.addCell(cell);

        cell = makeCell("QTY", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
        cell.setMinimumHeight(15f);
        orderTable1.addCell(cell);
        cell = makeCell("TYPE", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
        cell.setMinimumHeight(15f);
        orderTable1.addCell(cell);
        cell = makeCell("QTY", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        cell.setMinimumHeight(15f);
        orderTable1.addCell(cell);
        cell = makeCell("TYPE", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        cell.setMinimumHeight(15f);
        orderTable1.addCell(cell);
        cell = makeCell("WEIGHT", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("H.M", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("Commodities requiring special or additional caring or attention in handling or stowing must be marked and packedas to ensure safe transportation with ordinary care", new Font(Font.HELVETICA, 6, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("NMFC#", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("CLASS", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable1.addCell(cell);

        size = 0;
        packages = 0;
        int handling = 0;
        weight = 0;
        for (DomesticPurchaseOrder order : purchaseOrderList) {
            size++;
            packages += order.getPackageQuantity();
            weight += order.getWeight();
            handling += order.getHandlingUnitQuantity();

            cell = makeCell("" + order.getHandlingUnitQuantity(), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
            cell.setMinimumHeight(15f);
            orderTable1.addCell(cell);
            cell = makeCell(order.getHandlingUnitType(), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell("" + order.getPackageQuantity(), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell(order.getPackageType(), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell("" + order.getWeight(), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell(order.isHazmat() ? "Y" : "N", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell(order.getDescription(), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell(order.getNmfc(), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell(order.getClasses(), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable1.addCell(cell);
        }
        for (int i = size; i < 6; i++) {

            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
            cell.setMinimumHeight(15f);
            orderTable1.addCell(cell);
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable1.addCell(cell);
            cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
            orderTable1.addCell(cell);
        }
        cell = makeCell("" + handling, new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
        cell.setMinimumHeight(15f);
        orderTable1.addCell(cell);
        cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("" + packages, new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("" + weight, new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0f, 0.6f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("GRAND TOTAL", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable1.addCell(cell);
        cell = makeCell("", new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        orderTable1.addCell(cell);

        PdfPTable descTable = makeTable(2);
        descTable.setWidthPercentage(100);
        descTable.setWidths(new float[]{60f, 40f});

        cell = makeCell("Where the rate is dependent on value, shippers are required to state specificallyin writing "
                + "the agreed or \n declared value of the property as follow",
                new Font(Font.HELVETICA, 7, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0f, 0.6f);
        descTable.addCell(cell);

        cell = makeCell("COD AMOUNT: $___________",
                new Font(Font.HELVETICA, 14, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0f, 0.6f);
        descTable.addCell(cell);

        cell = makeCell("The aggreed or declared value of the property is specifically stated by the shipper to be not exceeding \n "
                + "___________________________ per ______________________________",
                new Font(Font.HELVETICA, 7, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0.6f, 0f);
        descTable.addCell(cell);

        cell = makeCell("FEE TERMS:    COLLECT:     PREPAID:     \n"
                + "CUSTOMER CHECK ACCEPTABLE:",
                new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK), 1, 0.6f, 0.6f, 0.6f, 0f);
        descTable.addCell(cell);

        cell = makeCell("NOTE liability Limitation for loss or damage in this shipment may be applicable. See 49 U.S.C - 14706(c)(1)(A)and(B)",
                new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0f, 0.6f);
        cell.setColspan(2);
        descTable.addCell(cell);

        cell = makeCell("RECEIVED, subject to individually determined rates or contracts that have been agreed upon in writing between the carrier"
                + " and shipper if applicable, otherwise to the rates,classification and rules that have been established by the carrier are available to the "
                + "shipper,on request and to all applicable state and federal registration. The sipper hereby certifies that he/she is familiar with all terms "
                + "and conditions on the NMFC Uniform straight bill of ladding, including those on the back thereof and the said terms and conditions are hereby agreed"
                + " to by the shipper and accepted for him/herself and his/her assigns",
                new Font(Font.HELVETICA, 7, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0.6f, 0.6f);
        descTable.addCell(cell);

        cell = makeCell("The carrier shall not make delivery of this shipment without payment of frieght and allother lawful charges \n"
                + "____________Shipper Signature",
                new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0.6f, 0.6f);
        descTable.addCell(cell);
        PdfPTable signTable = makeTable(4);
        signTable.setWidthPercentage(100);
        signTable.setWidths(new float[]{33f, 17f, 16f, 34f});
        cell = makeCell("SHIPPER SIGNATURE/DATE ",
                new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0f, 0.6f);
        signTable.addCell(cell);
        cell = makeCell("TRAILER LOADED",
                new Font(1, 9.0F, 4, Color.black), 0, 0.6f, 0f, 0f, 0.6f);
        signTable.addCell(cell);
        cell = makeCell("FRIEGHT COUNTED",
                new Font(1, 9.0F, 4, Color.black), 0, 0f, 0.6f, 0f, 0.6f);
        signTable.addCell(cell);
        cell = makeCell("CARRIER SIGNATURE/PICKUP DATE ",
                new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0f, 0.6f);
        signTable.addCell(cell);

        cell = makeCell("This is to certify that above named  materials and properly classified ,packed,marked and labeled , and are in proper"
                + "condition for transportation according to the applicable regulation of DOT",
                new Font(Font.HELVETICA, 7, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0.6f, 0f);
        signTable.addCell(cell);
        cell = makeCell("BY SHIPPER \n"
                + "By Driver",
                new Font(1, 9.0F, Font.BOLD, Color.black), 0, 0f, 0f, 0.6f, 0f);
        signTable.addCell(cell);
        cell = makeCell("BY SHIPPER \n"
                + "By Driver/pallets set to contain \n"
                + "By Driver/peices",
                new Font(1, 9.0F, Font.BOLD, Color.black), 0, 0f, 0.6f, 0.6f, 0f);
        signTable.addCell(cell);
        cell = makeCell("Carrier acknowledges receipt for packages and required placards. Carrier certifies emergency response information was made"
                + " available and/or carrier has the DOT emergency response guidebook or equivalent document in the vehicle.\n"
                + "Property described above is received in good order, except as noted",
                new Font(Font.HELVETICA, 7, Font.BOLD, Color.BLACK), 0, 0.6f, 0.6f, 0.6f, 0f);
        signTable.addCell(cell);

        PdfPTable headingTable = makeTable(1);
        headingTable.setWidthPercentage(100);

        cell = makeCell("SUPPLEMENT TO THE BILL OF LADDING \n\n",
                new Font(Font.HELVETICA, 15, Font.BOLD, Color.BLACK), 1, 0f, 0f, 0f, 0f);
        headingTable.addCell(cell);
        cell = makeCell("BILL OF LADDING :" + booking.getBookingNumber(),
                new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK), 2, 0f, 0f, 0f, 0f);
        headingTable.addCell(cell);
        cell = makeCell("CUSTOMER ORDER INFORMATION", new Font(1, 8, 1, Color.WHITE), 1, 0.6f, 0f, 0.6f, 0.6f);
        cell.setBackgroundColor(Color.BLACK);
        cell.setColspan(2);
        headingTable.addCell(cell);
        document.add(header);
        document.add(mainTable);
        document.add(orderTable);
        document.add(orderTable1);
        document.add(descTable);
        document.add(signTable);
//        document.newPage();
//
//        document.add(headingTable);
//        document.add(orderTable);
//        document.add(orderTable1);


    }

    public void destroy() {
        document.close();
    }

    public PdfPCell makeCell(String text, Font font, int align, float left, float right, float bottom, float top) {
        Phrase phrase = new Phrase(text, font);
        PdfPCell cell = makeCell(phrase, align);
        cell.setBorderWidthLeft(left);
        cell.setBorderWidthRight(right);
        cell.setBorderWidthBottom(bottom);
        cell.setBorderWidthTop(top);
        return cell;
    }

    public String createReport(DomesticBooking booking, List<DomesticPurchaseOrder> purchaseOrderList, String fileName, String contextPath) throws Exception {
        this.initialize(fileName);
        this.createBody(booking, purchaseOrderList, contextPath);
        this.destroy();
        return "fileName";
    }
}
