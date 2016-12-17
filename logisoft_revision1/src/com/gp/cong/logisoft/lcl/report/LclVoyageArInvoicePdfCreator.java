/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.logiware.hibernate.dao.ArRedInvoiceChargesDAO;
import com.logiware.hibernate.domain.ArRedInvoice;
import com.logiware.hibernate.domain.ArRedInvoiceCharges;
import java.io.FileOutputStream;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class LclVoyageArInvoicePdfCreator extends LclReportFormatMethods {

    protected BaseFont helv;
    private LclSsHeader lclssHeader;
    private String invoiceId;
    private Iterable<Object> lclBookingPieceList;
    private ArRedInvoice arRedInvoice;
    private List<ArRedInvoiceCharges> invoiceCharges = null;
    private LclUtils lclUtils = new LclUtils();

    public LclVoyageArInvoicePdfCreator(LclSsHeader lclssHeader, ArRedInvoice arRedInvoice) throws Exception {
        this.lclssHeader = lclssHeader;
        this.arRedInvoice = arRedInvoice;
        List<ArRedInvoiceCharges> invoiceCharge = new ArRedInvoiceChargesDAO().findByProperty("arRedInvoiceId", arRedInvoice.getId());
        this.invoiceCharges = invoiceCharge;
    }

    public void createPdf(String realPath, String outputFileName) throws Exception {
            Document document = new Document();
            document.setPageSize(PageSize.A4);
            document.setMargins(10, 10, 10, 10);
            PdfWriter pdfWriter = null;
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
            document.open();
            document.add(imageTable(realPath));
            document.add(toTable());
            document.add(addressTable());
            document.add(emptyTable());
            document.add(fortTable());
            document.add(attnTable());
            document.add(emptyTable1());
            document.add(piecesTable());
            document.add(piecescontentTable());
            //document.add(shipperTable());
          //  document.add(shippercontentTable());
            document.add(descriptionTable());
            document.add(emptyTable2());
            document.add(chargesTable());
            document.add(administrationTable());
            if (invoiceCharges.size() > 1) {
                for (int i = 1; i < invoiceCharges.size(); i++) {
                    ArRedInvoiceCharges arc = invoiceCharges.get(i);
                    document.add(chargeTable(arc));
                }
            }
            document.add(totalTable());
            document.add(emptyTable3());
            document.add(pleaseTable());
            document.add(emptyTable4());
            document.add(voiceTable());
            document.add(emptyTable5());
            document.add(paymentsTable());
            document.add(bankTable());
            document.add(emptyTable6());
            document.add(invoiceTable());
            document.add(thanksTable());

            document.close();
    }

    //image table
    public PdfPTable imageTable(String realPath) throws Exception {
        PdfPTable imageTable = null;
        String status;
            String path = LoadLogisoftProperties.getProperty("application.image.logo");
            Paragraph pg = null;
            Phrase p = null;
            PdfPCell cell = null;
            imageTable = new PdfPTable(2);
            imageTable.setWidthPercentage(100f);
            imageTable.setWidths(new float[]{1f, 2f});

            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthTop(0.06f);
            Image img = Image.getInstance(realPath + path);
            img.scaleToFit(200f, 200f);
            img.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(img);
            imageTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBorderWidthRight(0.06f);
            cell.setBorderWidthTop(0.06f);
            PdfPTable innerTable = null;
            innerTable = new PdfPTable(1);
            PdfPCell cell1 = null;
            cell1 = new PdfPCell();
            cell.setFixedHeight(15);
            cell1.setBorder(0);
            pg = new Paragraph("MAILING ADDRESS: 2401 N.W. 69TH STREET,MIAMI,FL 33147", blackBoldFontSize8);
            pg.setAlignment(Element.ALIGN_LEFT);
            pg.setLeading(10f);
            cell1.addElement(pg);
            pg = new Paragraph("TEL: 9632853048 / FAX: 9916321448", blackBoldFontSize8);
            pg.setAlignment(Element.ALIGN_CENTER);
            pg.setLeading(10f);
            cell1.addElement(pg);
            pg = new Paragraph("", blackBoldFontSize8);
            pg.setAlignment(Element.ALIGN_CENTER);
            pg.setLeading(100f);
            cell1.addElement(pg);
            innerTable.addCell(cell1);

            cell1 = new PdfPCell();
            cell1.setBorder(0);
            PdfPTable inner1Table = null;
            inner1Table = new PdfPTable(2);
            inner1Table.setWidths(new float[]{1.10f, 1f});
            PdfPCell cell2 = null;
            cell2 = new PdfPCell();
            //cell2.setFixedHeight(75);
            cell2.setPaddingTop(2f);
            cell2.setPaddingBottom(2f);
            cell2.setPaddingLeft(2f);
            cell2.setPaddingRight(2f);
            pg = new Paragraph("INVOICE / FACTURA", boxRedFont);
            pg.setAlignment(Element.ALIGN_CENTER);
            pg.setLeading(10f);
            cell2.addElement(pg);
            inner1Table.addCell(cell2);
//            cell1.addElement(inner1Table);
//            innerTable.addCell(cell1);
//            cell.addElement(innerTable);

            cell2 = new PdfPCell();
            cell2.setBorder(0);
            pg = new Paragraph("", boxRedFont);
            pg.setAlignment(Element.ALIGN_CENTER);
            pg.setLeading(20f);
            cell2.addElement(pg);
            inner1Table.addCell(cell2);
            cell1.addElement(inner1Table);
            innerTable.addCell(cell1);
            cell.addElement(innerTable);
            imageTable.addCell(cell);
        return imageTable;
    }
    //to table

    public PdfPTable toTable() throws DocumentException {
        PdfPTable toTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        toTable = new PdfPTable(6);
        toTable.setWidthPercentage(100f);
        toTable.setWidths(new float[]{.48f, 4.85f, .65f, 1.5f, 1.15f, 2});

        cell = new PdfPCell();
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg = new Paragraph("TO:", blackBoldFontSize8);
        pg.setLeading(8f);
        pg.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(pg);
        toTable.addCell(cell);

        cell = new PdfPCell();
        pg = new Paragraph("" + arRedInvoice.getCustomerName(), blackBoldFontSize8);
        pg.setLeading(8f);
        pg.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(pg);
        toTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        toTable.addCell(cell);

        cell = new PdfPCell();
        pg = new Paragraph("INVOICE NO.", blackBoldFontSize8);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setLeading(8f);
        pg.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pg);
        toTable.addCell(cell);

        cell = new PdfPCell();
        pg = new Paragraph("DATE", blackBoldFontSize8);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setLeading(8f);
        pg.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pg);
        toTable.addCell(cell);

        cell = new PdfPCell();
        pg = new Paragraph("REFERENCE NO.", blackBoldFontSize8);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setLeading(8f);
        pg.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pg);
        toTable.addCell(cell);
        return toTable;
    }
//address table

    public PdfPTable addressTable() throws DocumentException {
        PdfPTable addressTable = null;
        Paragraph pg = null;
        PdfPCell cell = null;
        addressTable = new PdfPTable(6);
        addressTable.setWidthPercentage(100f);
        addressTable.setWidths(new float[]{.48f, 4.85f, .65f, 1.5f, 1.15f, 2});

        cell = new PdfPCell();
        pg = new Paragraph("", blackBoldFontSize8);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        pg.setLeading(8f);
        cell.addElement(pg);
        addressTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        pg = new Paragraph("" + arRedInvoice.getAddress(), blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_LEFT);
        pg.setLeading(8f);
        cell.addElement(pg);
        addressTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        addressTable.addCell(cell);

        cell = new PdfPCell();
        pg = new Paragraph("" + arRedInvoice.getInvoiceNumber(), blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        addressTable.addCell(cell);

        cell = new PdfPCell();
        pg = new Paragraph("" + DateUtils.parseDateToString(arRedInvoice.getDate()), blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        addressTable.addCell(cell);

        cell = new PdfPCell();
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        addressTable.addCell(cell);
        return addressTable;
    }
//    empty table

    public PdfPTable emptyTable() throws DocumentException {
        PdfPTable emptyTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        emptyTable = new PdfPTable(6);
        emptyTable.setWidthPercentage(100f);
        emptyTable.setWidths(new float[]{.48f, 4.85f, .65f, 1.5f, 1.15f, 2});

        cell = new PdfPCell();
        cell.setFixedHeight(8);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        emptyTable.addCell(cell);

        cell = new PdfPCell();
        cell.setFixedHeight(8);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        emptyTable.addCell(cell);

        cell = new PdfPCell();
        cell.setFixedHeight(8);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        emptyTable.addCell(cell);

        cell = new PdfPCell();
        cell.setFixedHeight(8);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorder(0);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        emptyTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setFixedHeight(8);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        emptyTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setFixedHeight(8);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        emptyTable.addCell(cell);
        return emptyTable;
    }

//     //fort table
    public PdfPTable fortTable() throws DocumentException {
        PdfPTable fortTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        fortTable = new PdfPTable(5);
        fortTable.setWidthPercentage(100f);
        fortTable.setWidths(new float[]{.48f, 4.85f, .66f, 2.40f, 2.25f});

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        fortTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(5f);
        cell.addElement(pg);
        fortTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        fortTable.addCell(cell);


        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("BILL TO ACCT NO.", blackBoldFontSize8);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        fortTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        pg = new Paragraph("ECI Ref.", blackBoldFontSize8);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        fortTable.addCell(cell);

        return fortTable;
    }
    //attn table

    public PdfPTable attnTable() throws DocumentException {
        PdfPTable attnTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        attnTable = new PdfPTable(5);
        attnTable.setWidthPercentage(100f);
        attnTable.setWidths(new float[]{.48f, 4.85f, .66f, 2.40f, 2.25f});

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        pg = new Paragraph("ATTN", blackBoldFontSize8);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setLeading(8f);
        cell.addElement(pg);
        attnTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        attnTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorder(0);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        attnTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        pg = new Paragraph("" + arRedInvoice.getCustomerNumber(), blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        attnTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        pg = new Paragraph("" + lclssHeader.getScheduleNo(), blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        attnTable.addCell(cell);

        return attnTable;
    }

    //    empty table
    public PdfPTable emptyTable1() throws DocumentException {
        PdfPTable emptyTable1 = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        emptyTable1 = new PdfPTable(1);
        emptyTable1.setWidthPercentage(100f);


        cell = new PdfPCell();
        cell.setFixedHeight(10f);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        emptyTable1.addCell(cell);
        return emptyTable1;
    }
//pieces table

    public PdfPTable piecesTable() throws DocumentException {
        PdfPTable piecesTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        piecesTable = new PdfPTable(5);
        piecesTable.setWidthPercentage(100f);
        piecesTable.setWidths(new float[]{1.20f, 2.22f, 2.25f, 3.12f, 2.55f});

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("PIECES", blackBoldFontSize6);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setLeading(5f);
        pg.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pg);
        piecesTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("WEIGHT", blackBoldFontSize6);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setLeading(5f);
        pg.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pg);
        piecesTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("MEASUREMENTS", blackBoldFontSize6);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(5f);
        cell.addElement(pg);
        piecesTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("ORIGIN", blackBoldFontSize6);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(5f);
        cell.addElement(pg);
        piecesTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg = new Paragraph("DESTINATION", blackBoldFontSize6);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(5f);
        cell.addElement(pg);
        piecesTable.addCell(cell);

        return piecesTable;
    }

    //piecescontent table
    public PdfPTable piecescontentTable() throws DocumentException {
        PdfPTable piecescontentTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        piecescontentTable = new PdfPTable(5);
        piecescontentTable.setWidthPercentage(100f);
        piecescontentTable.setWidths(new float[]{1.20f, 2.22f, 2.25f, 3.12f, 2.55f});

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("", blackBoldFontSize6);
        pg.setLeading(6f);
        pg.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pg);
        piecescontentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("", blackBoldFontSize6);
        pg.setLeading(6f);
        pg.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pg);
        piecescontentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("", blackBoldFontSize6);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(6f);
        cell.addElement(pg);
        piecescontentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("" + lclssHeader.getOrigin().getUnLocationName(), blackBoldFontSize6);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(6f);
        cell.addElement(pg);
        piecescontentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        pg = new Paragraph("" + lclssHeader.getDestination().getUnLocationName(), blackBoldFontSize6);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(6f);
        cell.addElement(pg);
        piecescontentTable.addCell(cell);

        return piecescontentTable;
    }
//   shipper table

    public PdfPTable shipperTable() throws DocumentException {
        PdfPTable shipperTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        shipperTable = new PdfPTable(2);
        shipperTable.setWidthPercentage(100f);
        shipperTable.setWidths(new float[]{1f, 1f});

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0.06f);
        pg = new Paragraph("SHIPPER / FORWARDER", blackBoldFontSize6);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(5f);
        cell.addElement(pg);
        shipperTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthRight(0.06f);
        pg = new Paragraph("CONSIGNEE", blackBoldFontSize6);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(5f);
        cell.addElement(pg);
        shipperTable.addCell(cell);

        return shipperTable;
    }
    //shippercontent Table

    public PdfPTable shippercontentTable() throws DocumentException {
        PdfPTable shipperTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        shipperTable = new PdfPTable(2);
        shipperTable.setWidthPercentage(100f);
        shipperTable.setWidths(new float[]{1f, 1f});

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0.06f);
        //pg = new Paragraph("" + lclBooking.getShipContact().getContactName(), blackBoldFontSize6);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(6f);
        cell.addElement(pg);
        shipperTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
     //   pg = new Paragraph("" + lclBooking.getConsContact().getContactName(), blackBoldFontSize6);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(6f);
        cell.addElement(pg);
        shipperTable.addCell(cell);

        return shipperTable;
    }
//    description table

    public PdfPTable descriptionTable() {
        PdfPTable descriptionTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        descriptionTable = new PdfPTable(1);
        descriptionTable.setWidthPercentage(100f);

        cell = new PdfPCell();
        pg = new Paragraph("DESCRIPTION / DETAILS", blackBoldFontSize10);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        descriptionTable.addCell(cell);

        return descriptionTable;
    }
//     //empty table1

    public PdfPTable emptyTable2() {
        PdfPTable emptyTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        emptyTable = new PdfPTable(1);
        emptyTable.setWidthPercentage(100f);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setFixedHeight(60f);
        pg = new Paragraph("" + arRedInvoice.getNotes(), blackBoldFontSize7);
        pg.setAlignment(Element.ALIGN_LEFT);
        pg.setLeading(8f);
        cell.addElement(pg);
        emptyTable.addCell(cell);
        return emptyTable;
    }
//     //charges table

    public PdfPTable chargesTable() {
        PdfPTable chargesTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        chargesTable = new PdfPTable(1);
        chargesTable.setWidthPercentage(100f);

        cell = new PdfPCell();
        pg = new Paragraph("CHARGES", blackBoldFontSize10);
        cell.setBackgroundColor(new BaseColor(183, 211, 240));
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        chargesTable.addCell(cell);

        return chargesTable;
    }

    //  administration table
    public PdfPTable administrationTable() throws DocumentException {
        PdfPTable administrationTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        administrationTable = new PdfPTable(4);
        administrationTable.setWidthPercentage(100f);
        administrationTable.setWidths(new float[]{6f, 4.5f, 1.25f, 1.25f});

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        if (CommonUtils.isNotEmpty(invoiceCharges)) {
            ArRedInvoiceCharges arc = invoiceCharges.get(0);
            pg = new Paragraph("" + arc.getChargeCode(), blackBoldFontSize8);
            pg.setAlignment(Element.ALIGN_LEFT);
            pg.setLeading(8f);
            cell.addElement(pg);
            administrationTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            pg = new Paragraph("" + arc.getDescription(), blackBoldFontSize8);
            pg.setAlignment(Element.ALIGN_LEFT);
            pg.setLeading(8f);
            cell.addElement(pg);
            administrationTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthBottom(0.06f);
            pg = new Paragraph("$", blackBoldFontSize8);
            pg.setLeading(8f);
            cell.addElement(pg);
            administrationTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthRight(0.06f);
            if (CommonUtils.isNotEmpty(invoiceCharges)) {
                //  ArRedInvoiceCharges arc = invoiceCharges.get(0);
                pg = new Paragraph("" + arc.getAmount(), blackBoldFontSize8);
            } else {
                pg = new Paragraph("", blackBoldFontSize8);
            }
            pg.setAlignment(Element.ALIGN_RIGHT);
            pg.setLeading(8f);
            cell.addElement(pg);
            administrationTable.addCell(cell);
        }
        return administrationTable;

    }

    public PdfPTable chargeTable(ArRedInvoiceCharges arc) throws DocumentException {
        PdfPTable administrationTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        administrationTable = new PdfPTable(4);
        administrationTable.setWidthPercentage(100f);
        administrationTable.setWidths(new float[]{6f, 4.5f, 1.25f, 1.25f});

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        pg = new Paragraph("" + arc.getChargeCode(), blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        administrationTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        pg = new Paragraph("" + arc.getDescription(), blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        administrationTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        pg = new Paragraph("$", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        administrationTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        pg = new Paragraph("" + arc.getAmount(), blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_RIGHT);
        pg.setLeading(8f);
        cell.addElement(pg);
        administrationTable.addCell(cell);

        return administrationTable;
    }

    public PdfPTable totalTable() throws DocumentException {
        PdfPTable totalTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        totalTable = new PdfPTable(4);
        totalTable.setWidthPercentage(100f);
        totalTable.setWidths(new float[]{6f, 4.5f, 1.25f, 1.25f});


        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        totalTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        pg = new Paragraph("INVOICE TOTAL", blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        totalTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        pg = new Paragraph("$", blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_LEFT);
        pg.setLeading(8f);
        cell.addElement(pg);
        totalTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        Double amount = 0.00;
        for (ArRedInvoiceCharges arc : invoiceCharges) {
            amount += arc.getAmount();
        }
        pg = new Paragraph("" + amount, blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_RIGHT);
        pg.setLeading(8f);
        cell.addElement(pg);
        totalTable.addCell(cell);

        return totalTable;
    }

    public PdfPTable emptyTable3() {
        PdfPTable emptyTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        emptyTable = new PdfPTable(1);
        emptyTable.setWidthPercentage(100f);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setFixedHeight(10f);
        pg = new Paragraph("", blackBoldFontSize10);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(30f);
        cell.addElement(pg);
        emptyTable.addCell(cell);
        return emptyTable;
    }
    //please table

    public PdfPTable pleaseTable() throws DocumentException, Exception {
        PdfPTable pleaseTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        pleaseTable = new PdfPTable(4);
        pleaseTable.setWidthPercentage(100f);
        pleaseTable.setWidths(new float[]{6f, 4.5f, 1.25f, 1.25f});


        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        pg = new Paragraph("", blackBoldFontSize8);
        pg.setLeading(8f);
        cell.addElement(pg);
        pleaseTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("PLEASE PAY THIS AMOUNT ---->", blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_LEFT);
        pg.setLeading(8f);
        cell.addElement(pg);
        pleaseTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("$", blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_LEFT);
        pg.setLeading(8f);
        cell.addElement(pg);
        pleaseTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        Double amount = 0.00;
        List<ArRedInvoiceCharges> invoiceCharges = new ArRedInvoiceChargesDAO().findByProperty("arRedInvoiceId", arRedInvoice.getId());
        for (ArRedInvoiceCharges arc : invoiceCharges) {
            amount += arc.getAmount();
        }
        pg = new Paragraph("" + amount, blackBoldFontSize8);
        pg.setAlignment(Element.ALIGN_RIGHT);
        pg.setLeading(8f);
        cell.addElement(pg);
        pleaseTable.addCell(cell);

        return pleaseTable;
    }
//empty table

    public PdfPTable emptyTable4() {
        PdfPTable emptyTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        emptyTable = new PdfPTable(1);
        emptyTable.setWidthPercentage(100f);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setFixedHeight(20f);
        pg = new Paragraph("", blackBoldFontSize10);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        emptyTable.addCell(cell);
        return emptyTable;
    }
//     //this voice table

    public PdfPTable voiceTable() {
        PdfPTable voiceTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        voiceTable = new PdfPTable(1);
        voiceTable.setWidthPercentage(100f);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        pg = new Paragraph("THIS INVOICE IS DUE UPON RECEIPT", blackBoldFontSize10);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        voiceTable.addCell(cell);
        return voiceTable;
    }

    public PdfPTable emptyTable5() {
        PdfPTable emptyTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        emptyTable = new PdfPTable(1);
        emptyTable.setWidthPercentage(100f);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setFixedHeight(20f);
        pg = new Paragraph("", blackBoldFontSize10);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        emptyTable.addCell(cell);
        return emptyTable;
    }

    public PdfPTable paymentsTable() throws DocumentException {
        PdfPTable paymentsTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        paymentsTable = new PdfPTable(3);
        paymentsTable.setWidthPercentage(100f);
        paymentsTable.setWidths(new float[]{1.5f, 3f, 1.5f});


        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        pg = new Paragraph("", blackBoldFontSize10);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        paymentsTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("PAYMENTS VIA WIRE TRANSFER SHOULD BE SENT", blackBoldFontSize6);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(6f);
        cell.addElement(pg);
        pg = new Paragraph("AS FOLLOWS:", blackBoldFontSize6);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(10f);
        cell.addElement(pg);
        paymentsTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        pg = new Paragraph("", blackBoldFontSize10);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        paymentsTable.addCell(cell);
        return paymentsTable;
    }

    public PdfPTable bankTable() throws DocumentException {
        PdfPTable bankTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        bankTable = new PdfPTable(3);
        bankTable.setWidthPercentage(100f);
        bankTable.setWidths(new float[]{1.5f, 3f, 1.5f});

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        pg = new Paragraph("", blackBoldFontSize10);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        bankTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        pg = new Paragraph("BANK: REGIONS BANK", blackBoldFontSize6);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        pg = new Paragraph("2800 PONCE DE LEON BOULEVARD CORAL GABLES, FLORIDA 33134", blackBoldFontSize6);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        pg = new Paragraph("ACCT NAME: O.T.I CARGO INC.", blackBoldFontSize6);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        pg = new Paragraph("Acct no.: 9660257206", blackBoldFontSize6);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        pg = new Paragraph("ABA Routing no.: 063104668", blackBoldFontSize6);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        bankTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        pg = new Paragraph("", blackBoldFontSize10);
        pg.setAlignment(Element.ALIGN_CENTER);
        pg.setLeading(8f);
        cell.addElement(pg);
        bankTable.addCell(cell);
        return bankTable;
    }

    public PdfPTable emptyTable6() {
        PdfPTable emptyTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        emptyTable = new PdfPTable(1);
        emptyTable.setWidthPercentage(100f);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setFixedHeight(300f);
        pg = new Paragraph("", blackBoldFontSize10);
        pg.setAlignment(Element.ALIGN_LEFT);
        pg.setLeading(8f);
        cell.addElement(pg);
        emptyTable.addCell(cell);
        return emptyTable;
    }
// invoiceno table

    public PdfPTable invoiceTable() throws DocumentException {
        PdfPTable invoiceTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        invoiceTable = new PdfPTable(2);
        invoiceTable.setWidthPercentage(100f);
        invoiceTable.setWidths(new float[]{1.5f, 1.5f});

        cell = new PdfPCell();
        cell.setBorder(0);
        pg = new Paragraph("Invoice#" + arRedInvoice.getInvoiceNumber(), bodyNormalFont);
        pg.setLeading(8f);
        pg.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(pg);
        invoiceTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        pg = new Paragraph("Page 1 of 1", bodyNormalFont);
        pg.setLeading(8f);
        pg.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(pg);
        invoiceTable.addCell(cell);

        return invoiceTable;
    }

    //thank table
    public PdfPTable thanksTable() {
        PdfPTable thanksTable = null;
        Paragraph pg = null;
        Phrase p = null;
        PdfPCell cell = null;
        thanksTable = new PdfPTable(1);
        thanksTable.setWidthPercentage(100f);

        cell = new PdfPCell();
        cell.setBorder(0);
        pg = new Paragraph("Thank you for choosing ECONOCARIBE CONSOLIDATORS, INC for all your shipping needs", blackItalicFontSize10);
        pg.setLeading(15f);
        pg.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pg);
        thanksTable.addCell(cell);

        return thanksTable;
    }
}
