/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.List;

/**
 *
 * @author Logiware
 */
public class LclImportBLPdfCreator extends LclReportFormatMethods {

    private LclBl lclbl;

    public LclImportBLPdfCreator(LclBl lclbl) {
        this.lclbl = lclbl;
    }

    public void createImportBLPdf(String realPath, String outputFileName, String documentName) throws Exception {
        document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(8, 8, 15, 15);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        document.open();
        document.add(headerTable(realPath));
        document.add(contentTable());
        document.add(commodityTable());
        document.add(trackingContainerTable());
        document.add(commentsTable());
        document.close();
    }

    public PdfPTable headerTable(String realPath) throws Exception {
        Font blackNormalFont11 = FontFactory.getFont("Arial", 10f, Font.NORMAL);
        Font blackBoldFont11 = FontFactory.getFont("Arial", 10f, Font.BOLD);
        Font blackNormalFont14 = FontFactory.getFont("Arial", 14f, Font.NORMAL);
        Paragraph p = null;
        Phrase pEmpty = null;
        String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100f);
        headerTable.setWidths(new float[]{2.5f, 3f, 3f});

        cell = new PdfPCell();
        cell.setBorder(0);
        pEmpty = new Phrase("");
        pEmpty.setLeading(30f);
        cell.addElement(pEmpty);
        headerTable.addCell(cell);

        cell = new PdfPCell();
        cell.setRowspan(3);
        cell.setBorder(0);
        cell.setPadding(0f);
        Image img = Image.getInstance(realPath + imagePath);
        img.scalePercent(60);
        img.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(img);
        headerTable.addCell(cell);

        headerTable.addCell(createEmptyCell(0, 0f, 0));

        cell = new PdfPCell();
        cell.setPaddingTop(0.5f);
        cell.setPaddingBottom(2.5f);
        cell.setBorderWidthLeft(1f);
        cell.setBorderWidthBottom(1f);
        cell.setBorderWidthTop(1f);
        cell.setBorderWidthRight(1f);
        p = new Paragraph(10f, "For Online Shipment Tracking go to", blackNormalFont11);
        cell.addElement(p);
        p = new Paragraph(10f, "www.econocaribe.com", blackBoldFont11);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(10f, "and Click Cargo Tracking", blackNormalFont11);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        headerTable.addCell(cell);


        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        headerTable.addCell(cell);

        headerTable.addCell(createEmptyCell(0, 0f, 0));
        headerTable.addCell(createEmptyCell(0, 0f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthBottom(0.06f);
        p = new Paragraph(10f, "CFS NOTIFICATION", blackNormalFont14);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        headerTable.addCell(cell);
        return headerTable;
    }

    public PdfPTable contentTable() throws Exception {
        Phrase p = null;
        contentTable = new PdfPTable(8);
        contentTable.setWidthPercentage(100f);
        contentTable.setWidths(new float[]{1f, 2f, 1f, 2f, 1.5f, 2.5f, 2f, 2f});
        //empty
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(8);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthTop(0.6f);
        p = new Paragraph(5f, "NOTIFY PARTY:", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Phrase(5f, "MASTER BILL OF LADDING #", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthTop(0.6f);
        p = new Phrase(5f, "INVOICE #", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        //2
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Phrase(5f, "NOTIFY PARTY:sdasdasdasdasda", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setPaddingBottom(5f);
        p = new Phrase(5f, "dasdasdasd", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setPaddingTop(5f);
        cell.setPaddingBottom(5f);
        cell.setBorderWidthBottom(0.6f);
        p = new Phrase(5f, "dsadasd#", blackBoldFont14);
        cell.addElement(p);
        contentTable.addCell(cell);
        //3
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Phrase(5f, "NOTIFY PARTY:sdasdasdasdasda", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingBottom(5f);
        cell.setBorderWidthLeft(0.6f);
        p = new Phrase(5f, "IT #:", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        p = new Phrase(5f, "IT #:", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //4
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Phrase(5f, "NOTIFY PARTY:sdasdasdasdasda", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Phrase(5f, "AMS House BL #:", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Phrase(5f, "", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setRowspan(4);
        PdfPTable nTable = new PdfPTable(4);
        nTable.setWidthPercentage(100f);
        nTable.setWidths(new float[]{0.5f, 0.4f, 1.2f, 1f});
        PdfPCell nCell = null;
        nCell = new PdfPCell();
        nCell.setBorder(0);
        nTable.addCell(nCell);
        nCell = new PdfPCell();
        nCell.setBorder(0);
        nCell.setBorderWidthLeft(0.6f);
        nCell.setBorderWidthTop(0.6f);
        nCell.setBorderWidthBottom(0.6f);
        p = new Phrase(5f, "Note:", blackNormalFont7);
        nCell.addElement(p);
        nTable.addCell(nCell);
        nCell = new PdfPCell();
        nCell.setBorder(0);
        nCell.setPadding(0f);
        nCell.setPaddingBottom(1f);
        nCell.setBorderWidthRight(0.6f);
        nCell.setBorderWidthTop(0.6f);
        nCell.setBorderWidthBottom(0.6f);
        p = new Phrase(7f, "AMS House BgdfgdfgdfgdfgdfgdfgdfL", blackNormalFont7);
        nCell.addElement(p);
        nTable.addCell(nCell);
        nCell = new PdfPCell();
        nCell.setBorder(0);
        nTable.addCell(nCell);
        cell.addElement(nTable);
        contentTable.addCell(cell);

        //5
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setPadding(0f);
        p = new Phrase(2f, "", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(0f);
        p = new Phrase(2f, "", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Phrase(2f, "", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        //6
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Phrase(5f, "NOTIFY PARTY:sdasdasdasdasda", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setPaddingTop(10f);
        p = new Phrase(5f, "sub House BL #:", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Phrase(5f, "", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //7
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthBottom(0.6f);
        p = new Phrase(5f, "Phone", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthLeft(0.6f);
        p = new Phrase(3f, "", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //consignee and shipper table
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Phrase(5f, "CONSIGNEE:", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthLeft(0.6f);
        p = new Phrase(5f, "SHIPPER", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //consignee values
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setPadding(10f);
        p = new Phrase(5f, "CONSIGNEE values", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setPadding(10f);
        cell.setBorderWidthLeft(0.6f);
        p = new Phrase(5f, "SHIPPER values", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //phone
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Phrase("Phone:", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthLeft(0.6f);
        p = new Phrase("Phone:", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //cargo and delivery
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthTop(0.6f);
        p = new Phrase(5f, "CARGO LOCATED AT:", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        p = new Phrase(5f, "FOR DELIVERY INFO CALL OR EMAIL:", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //consignee values
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setPadding(10f);
        p = new Phrase(5f, "cargo values", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setPadding(10f);
        cell.setBorderWidthLeft(0.6f);
        p = new Phrase(5f, "delivery values", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //phone
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthBottom(0.6f);
        p = new Phrase("Phone:", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        p = new Phrase("Phone:", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //receipt

        contentTable.addCell(createEmptyCell(0, 0f, 2));
        contentTable.addCell(makeCellLeftBorderValue("PLACE OF RECEIPT", 2));
        contentTable.addCell(makeCellLeftBorderValue("CONTAINER #", 2));
        contentTable.addCell(makeCellNoBorderValue("FILE #", 2, 0.5f, 1f));

        //values receipt

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setPaddingTop(0.6f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthBottom(0.6f);
        contentTable.addCell(cell);
        contentTable.addCell(makeCellLeftBottomBorderValue("PLACE OF RECEIPT", 2, 0.5f, 2f,blackNormalFont7));
        contentTable.addCell(makeCellLeftBottomBorderValue("CONTAINER #", 2, 0.5f, 2f,blackNormalFont7));
        contentTable.addCell(makeCellLeftBottomBorderValue("FILE #", 2, 0.5f, 2f,blackNormalFont7));
        //vessel table details
        contentTable.addCell(makeCellNoBorderValue("VESSEL", 2, 0.5f, 1f));
        contentTable.addCell(makeCellLeftBorderValue("PORT OF LOADING #", 2));
        contentTable.addCell(makeCellLeftBorderValue("APPROXIMATE DUE AT WHSE #", 2));
        contentTable.addCell(makeCellLeftBorderValue("STRIPPING DATE", 0));
        contentTable.addCell(makeCellLeftBorderValue("LAST FREE DAY", 0));
        //vessel Values details

        contentTable.addCell(makeCellBottomBorderValue("z1", 2, 0.5f, 2f,blackNormalFont7));
        contentTable.addCell(makeCellLeftBottomBorderValue("z2", 2, 0.5f, 2f,blackNormalFont7));
        contentTable.addCell(makeCellLeftBottomBorderValue("x1", 2, 0.5f, 2f,blackNormalFont7));
        contentTable.addCell(makeCellLeftBottomBorderValue("x23", 0, 0.5f, 2f,blackNormalFont7));
        contentTable.addCell(makeCellLeftBottomBorderValue("x2", 0, 0.5f, 2f,blackNormalFont7));
        //pod table details
        contentTable.addCell(makeCellNoBorderValue("PORT OF DISCHARGE", 2, 0.5f, 1f));
        contentTable.addCell(makeCellLeftBorderValue("PORT OF DELIVERY / ETA", 2));
        contentTable.addCell(makeCellLeftBorderValue("SAIL DATE", 0));
        contentTable.addCell(makeCellLeftBorderValue("ARRIVAL DATE", 0));
        contentTable.addCell(makeCellLeftBorderValue("G.O.DATE", 2));
        //pod table values
        contentTable.addCell(makeCellBottomBorderValue("z1", 2, 0.5f, 3f,blackNormalFont7));
        contentTable.addCell(makeCellLeftBottomBorderValue("z2", 2, 0.5f, 3f,blackNormalFont7));
        contentTable.addCell(makeCellLeftBottomBorderValue("x1", 0, 0.5f, 3f,blackNormalFont7));
        contentTable.addCell(makeCellLeftBottomBorderValue("x23", 0, 0.5f, 3f,blackNormalFont7));
        contentTable.addCell(makeCellLeftBottomBorderValue("x2", 2, 0.5f, 3f,blackNormalFont7));
        //empty cell
        contentTable.addCell(makeCellBottomBorderValue("", 8, 0.5f, 2f,blackNormalFont7));
        return contentTable;
    }

    public PdfPTable commodityTable() throws DocumentException, Exception {
        Paragraph p = null;
        Phrase pValues = null;
        table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 1f, 4f, 1.3f, 1f});
        //empty cell
        table.addCell(createEmptyCell(0, 3f, 5));
        table.addCell(makeCellBottomBorderValue("", 5, 0.5f, 1f,blackNormalFont7));
        //marks
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        p = new Paragraph(7f, "MARKS AND NUMBERS", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //no of pkgs
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "NO.OF.PKGS", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //desc
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "DESCRIPTION OF PACKAGES AND GOODS", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //grossweight
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "GROSS WEIGHT", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //measure
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "MEASURE", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        String fileId = lclbl.getFileNumberId().toString();
        Lcl3pRefNo lcl3pRefNo = new Lcl3pRefNoDAO().getLclHscCodeByType(fileId, LclReportConstants.NCM_REF);
        Lcl3pRefNo lcl3pRefNoaes = new Lcl3pRefNoDAO().getLclHscCodeByType(fileId, LclReportConstants.AES_NUMBER);
        //values
        List<LclBlPiece> lclBlPiecesList = lclbl.getLclFileNumber().getLclBlPieceList();
        if (lclBlPiecesList != null && lclBlPiecesList.size() > 0) {
            for (Object lclBlList : lclBlPiecesList) {
                LclBlPiece lclBlPiece = (LclBlPiece) lclBlList;

                //marks
                cell = new PdfPCell();
                cell.setBorder(0);
                if (lclBlPiece.getMarkNoDesc() != null && !lclBlPiece.getMarkNoDesc().equals("")) {
                    p = new Paragraph(7f, "" + lclBlPiece.getMarkNoDesc().toUpperCase(), blackNormalFont9);
                    p.setAlignment(Element.ALIGN_LEFT);
                    cell.addElement(p);
                }
                if (lcl3pRefNo != null && lcl3pRefNo.getReference() != null) {
                    p = new Paragraph(7f, "NCM: " + lcl3pRefNo.getReference(), blackNormalFont9);
                    p.setAlignment(Element.ALIGN_LEFT);
                    cell.addElement(p);
                }
                if (lcl3pRefNoaes != null && lcl3pRefNoaes.getReference() != null) {
                    p = new Paragraph(7f, "AES: " + lcl3pRefNoaes.getReference(), blackNormalFont9);
                    p.setAlignment(Element.ALIGN_LEFT);
                    cell.addElement(p);
                }
                table.addCell(cell);
                //no of pkgs
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.06f);
                if (lclBlPiece != null && lclBlPiece.getBookedPieceCount() != null && lclBlPiece.getPackageType().getAbbr01() != null) {
                    p = new Paragraph(7f, "" + lclBlPiece.getBookedPieceCount() + "" + lclBlPiece.getPackageType().getAbbr01(), blackNormalFont9);
                    p.setAlignment(Element.ALIGN_CENTER);
                    p.setSpacingAfter(80f);
                    cell.addElement(p);
                }
                table.addCell(cell);
                //desc
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                if (lclBlPiece != null && lclBlPiece.getPieceDesc() != null) {
                    p = new Paragraph(7f, "" + lclBlPiece.getPieceDesc().toUpperCase(), blackNormalFont9);
                    p.setAlignment(Element.ALIGN_LEFT);
                    p.setSpacingAfter(80f);
                    cell.addElement(p);
                }
                table.addCell(cell);
                //grossweight
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                p = new Paragraph(7f, "" + lclBlPiece.getBookedWeightMetric() + " KGS", blackNormalFont9);
                p.setAlignment(Element.ALIGN_CENTER);
                p.setSpacingAfter(80f);
                cell.addElement(p);
                table.addCell(cell);
                //measure
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                p = new Paragraph(7f, "" + lclBlPiece.getBookedVolumeMetric() + " CBM", blackNormalFont9);
                p.setAlignment(Element.ALIGN_CENTER);
                p.setSpacingAfter(80f);
                cell.addElement(p);
                table.addCell(cell);

            }
        }
        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(0);
        cell.setBorderWidthTop(0.6f);
        table.addCell(cell);
        //emptycell
        table.addCell(createEmptyCell(0, 0.001f, 5));
        //customer comments
        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(0);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        pValues = new Phrase(6f, "COMMENTS:", blackBoldFontSize7);
        cell.addElement(pValues);
        table.addCell(cell);
        //emptycell
        table.addCell(createEmptyCell(0, 1f, 5));
        return table;
    }

    public PdfPTable trackingContainerTable() throws DocumentException, Exception {
        table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 0.5f, 1.5f, 0.5f, 3f, 1.5f, 5f, 1f});
        table.addCell(makeCellBottomBorderValue("DATE", 0, 0.5f, 1f,blackNormalFont7));
        table.addCell(createEmptyCell(0, 1f, 0));
        table.addCell(makeCellBottomBorderValue("TIME", 0, 0.5f, 1f,blackNormalFont7));
        table.addCell(createEmptyCell(0, 1f, 0));
        table.addCell(makeCellBottomBorderValue("CONTAINER STATUS", 0, 0.5f, 1f,blackNormalFont7));
        table.addCell(createEmptyCell(0, 1f, 0));
        table.addCell(makeCellNoBorderValue("EXPRESS RELEASE (*)", 0, 0.5f, 1f));
        table.addCell(createEmptyCell(0, 1f, 0));

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setBorder(0);
        cell.setPadding(30f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setBorder(0);
        cell.setBorderWidthTop(0.6f);
        table.addCell(cell);
        return table;
    }

    public PdfPTable commentsTable() throws DocumentException, Exception {
        Phrase p = null;
        table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{3f, 2f, 0.5f, 8f});

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Phrase(7f, "FREIGHT CHARGES", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        p = new Phrase(7f, "AMOUNT", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, 0.6f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-2f);
        p = new Phrase(5f, "ATTENTION", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);
        //2n
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, 0.6f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-7f);
        p = new Phrase(8f, "ATTENTfjdsgfgsdhfgkshdkfksdhfkhksjhfkshdfkhsdkfhkslf f        "
                + "                    fdshfklhdsflkhsdl                 fdshfklsdhfkhsdkfhkshfkhsdfhlsdhflshdlfhlsdf f   flkshdlfkhskdffdfsdfdfsdfsdION", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        //3
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, 0.6f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Phrase(5f, "ATTENTION", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        //4
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, 0.6f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(3f);
        table.addCell(cell);
        //6
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, 0.6f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Phrase(5f, "CHARGES MUST BE SURRENDERED TO ECI BEFORE RELEASE OF CARGO CAN BE OBTAINED.", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        //7
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, 0.6f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(15f);
        table.addCell(cell);
        //8
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, 0.6f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Phrase(5f, "CHARGES MUST BE SURRENDERED TO ECI BEFORE RELEASE OF CARGO CAN BE OBTAINED.", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        //9
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, 0.6f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);
        //10
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        p = new Phrase(7f, "5 Calender Days Free Storage", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Phrase(7f, "TOTAL", blackNormalFont7);
        cell.addElement(p);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, 0.6f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-2f);
        p = new Phrase(7f, "CHARGES MUST BE SURRENDERED TO ECI BEFORE RELEASExvxcvxcvx OF CARGO CAN BE OBTAINED.", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);

        //content
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        p = new Phrase(7f, "CHARGES MUST BE SURRENDERED TO ECI BEFORE RELEASE", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, 0.6f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Phrase(7f, "CHARGES MUST BE SURRENDERED TO ECI BEFORE RELEASExvxcvxcvx OF CARGO CAN BE OBTAINED.", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        PdfPTable nTable = new PdfPTable(2);
        nTable.setWidths(new float[]{4f, 2.5f});
        nTable.setWidthPercentage(100f);
        PdfPCell ncell = null;
        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Phrase("CHARGES MUST BE SURRENDERED TO ECI BEFORE RELEASE", blackNormalFont7);
        ncell.addElement(p);
        nTable.addCell(ncell);
        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Phrase("ECONOCARIABE CONSOLIDATORS, INC", blackNormalFont7);
        ncell.addElement(p);
        nTable.addCell(ncell);

        cell.addElement(nTable);
        table.addCell(cell);
        return table;
    }
}
