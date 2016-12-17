/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsDispo;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rajesh
 */
public class LclVoyageNotificationPdfCreator extends LclReportFormatMethods {

    public void createPdf(String contextPath, String outputFileName, String unitssId, String voyContent) throws Exception {
        LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(Long.parseLong(unitssId));
        String voyageNo = lclUnitSs.getLclSsHeader().getScheduleNo();
        String unitNo = lclUnitSs.getLclUnit().getUnitNo();
        document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(8, 2, 8, 8);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        document.open();
        document.add(onStartPage(contextPath, voyageNo, unitNo, voyContent));
        document.add(voyInfo(lclUnitSs));
        document.add(unitInfo(lclUnitSs));
        document.add(dispoInfo(lclUnitSs));
        document.add(voyContent(voyContent));
        document.add(thankyouMsg(contextPath));
        document.close();
    }

    public PdfPTable onStartPage(String realPath, String scheduleNo, String unitNo, String voyContent) throws Exception {
        String path = LoadLogisoftProperties.getProperty("application.image.logo");
        Font fontArialBold = FontFactory.getFont("Arial", 10f, Font.BOLD);
        Font colorBoldFont = FontFactory.getFont("Arial", 12f, Font.BOLD, new BaseColor(00, 102, 00));
        Phrase p = null;
        Paragraph pValues = null;
        table = new PdfPTable(6);
        table.setWidths(new float[]{0.1f, 1.8f, 2.5f, 3.5f, 1.8f, 1.8f});
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
        p = new Phrase("", fontArialBold);
        p.setLeading(20f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingBottom(3f);
        pValues = new Paragraph(25f, "", fontArialBold);
        cell.addElement(pValues);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setRowspan(3);
        cell.setBorder(0);
        cell.setPadding(0f);
        Image img = Image.getInstance(realPath + path);
        img.scalePercent(60);
        img.setAlignment(Element.ALIGN_CENTER);
        img.setAlignment(Element.ALIGN_TOP);
        cell.addElement(img);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingBottom(3f);
        pValues = new Paragraph(20f, "Date:", fontArialBold);
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
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingBottom(3f);
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
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setPaddingBottom(3f);
        table.addCell(cell);
        //voyage
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(5f);
        pValues = new Paragraph(8f, "VOYAGE NOTIFICATION", colorBoldFont);
        pValues.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pValues);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        table.addCell(cell);
        return table;
    }

    public PdfPTable voyInfo(LclUnitSs lclUnitSs) throws DocumentException {
        StringBuilder originValues = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getOrigin().getUnLocationName())) {
            originValues.append(lclUnitSs.getLclSsHeader().getOrigin().getUnLocationName()).append(",");
        }
        if (null != lclUnitSs.getLclSsHeader().getOrigin().getCountryId() && CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getOrigin().getCountryId().getCodedesc())) {
            originValues.append(lclUnitSs.getLclSsHeader().getOrigin().getCountryId().getCodedesc()).append("  ");
        }
        if (CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getOrigin().getUnLocationCode())) {
            originValues.append("(").append(lclUnitSs.getLclSsHeader().getOrigin().getUnLocationCode()).append(")");
        }

        StringBuilder fdValues = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName())) {
            fdValues.append(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName()).append(",");
        }
        if (null != lclUnitSs.getLclSsHeader().getDestination().getStateId() && CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getDestination().getStateId().getCode())) {
            fdValues.append(lclUnitSs.getLclSsHeader().getDestination().getStateId().getCode()).append(" ");
        }
        if (CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getDestination().getUnLocationCode())) {
            fdValues.append("(").append(lclUnitSs.getLclSsHeader().getDestination().getUnLocationCode()).append(")");
        }
        Font fontCompSub = FontFactory.getFont("Arial", 9f, Font.BOLD);
        Paragraph pHeading = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
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
        pHeading = new Paragraph(8f, "Voyage Information", mainHeadingQuote);
        pHeading.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(pHeading);
        PdfPTable ntable1 = new PdfPTable(6);
        ntable1.setWidthPercentage(100f);
        ntable1.setWidths(new float[]{0.1f, 1.15f, 0.09f, 4.09f, 1f, 2f});
        //company Name
        ntable1.addCell(createEmptyCell(0, 1f, 6));
        //Voyage Cell Origin
        ntable1.addCell(createEmptyCell(0, 1f, 0));
        ntable1.addCell(makeCellNoBorderValue("Voyage. . . . . . . . . .", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(":", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(lclUnitSs.getLclSsHeader().getScheduleNo().toUpperCase(), 4, 0f, 5f, fontgreenCont));
        //1Cell Origin
        ntable1.addCell(createEmptyCell(0, 1f, 0));
        ntable1.addCell(makeCellNoBorderValue("Origin. . . . . . . . . . .", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(":", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(originValues.toString().toUpperCase(), 4, 0f, 5f, fontgreenCont));
        ///2Cell Destination
        ntable1.addCell(createEmptyCell(0, 1f, 0));
        ntable1.addCell(makeCellNoBorderValue("Destination. . . . . .", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(":", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(fdValues.toString().toUpperCase(), 4, 0f, 5f, fontgreenCont));
        //3
        StringBuilder billValues = new StringBuilder();
        if (lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum() != null) {
            billValues.append(lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum()).append("-");
        }
        if (lclUnitSs.getLclSsHeader().getBillingTerminal().getTerminalLocation() != null) {
            billValues.append(lclUnitSs.getLclSsHeader().getBillingTerminal().getTerminalLocation());
        }
        ntable1.addCell(createEmptyCell(0, 1f, 0));
        ntable1.addCell(makeCellNoBorderValue("Billing Terminal. . ", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(":", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(billValues.toString().toUpperCase(), 4, 0f, 4f, fontgreenCont));

        ntable1.addCell(createEmptyCell(0, 1f, 6));
        cell1.addElement(ntable1);
        table.addCell(cell1);
        return table;
    }

    public PdfPTable unitInfo(LclUnitSs lclUnitSs) throws DocumentException, Exception {
        LclUnitSsImports lclUnitSsImports = lclUnitSs.getLclUnit().getLclUnitSsImportsList().get(0);
        StringBuilder cfsWarehs = new StringBuilder();
        if (lclUnitSsImports != null && lclUnitSsImports.getCfsWarehouseId() != null) {
            if (lclUnitSsImports.getCfsWarehouseId().getWarehouseName() != null) {
                cfsWarehs.append(lclUnitSsImports.getCfsWarehouseId().getWarehouseName()).append("-");
            }
            if (lclUnitSsImports.getCfsWarehouseId().getWarehouseNo() != null) {
                cfsWarehs.append(lclUnitSsImports.getCfsWarehouseId().getWarehouseNo());
            }
        }
        LclUnitSsManifest lclUnitSsManifest = lclUnitSs.getLclUnit().getLclUnitSsManifestList().get(0);
        Font fontCompSub = FontFactory.getFont("Arial", 9f, Font.BOLD);
        Paragraph pHeading = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
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
        pHeading = new Paragraph(8f, "Unit Information", mainHeadingQuote);
        pHeading.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(pHeading);
        PdfPTable ntable1 = new PdfPTable(6);
        ntable1.setWidthPercentage(100f);
        ntable1.setWidths(new float[]{0.1f, 1.15f, 0.09f, 4.09f, 1f, 2f});
        //company Name
        ntable1.addCell(createEmptyCell(0, 1f, 6));
        //unit No Cell
        ntable1.addCell(createEmptyCell(0, 1f, 0));
        ntable1.addCell(makeCellNoBorderValue("Unit No . . . . . . . . .", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(":", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(lclUnitSs.getLclUnit().getUnitNo().toUpperCase(), 4, 0f, 4f, fontgreenCont));
        //3
        ntable1.addCell(createEmptyCell(0, 1f, 0));
        ntable1.addCell(makeCellNoBorderValue("CFS(Devanning). . ", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(":", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(cfsWarehs.toString().toUpperCase(), 4, 0f, 4f, fontgreenCont));
        //
        ntable1.addCell(createEmptyCell(0, 1f, 0));
        ntable1.addCell(makeCellNoBorderValue("Master BL. . . . . . . ", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(":", 0, 0f, 4f, fontCompSub));
        ntable1.addCell(makeCellNoBorderValue(lclUnitSsManifest.getMasterbl().toUpperCase(), 4, 0f, 4f, fontgreenCont));

        ntable1.addCell(createEmptyCell(0, 1f, 6));
        cell1.addElement(ntable1);
        table.addCell(cell1);
        return table;
    }

    public PdfPTable dispoInfo(LclUnitSs lclUnitSs) throws DocumentException, Exception {
        Paragraph pHeading = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        PdfPCell cell1 = null;
        cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setBorderColor(new BaseColor(00, 51, 153));
        cell1.setBorderWidthBottom(3f);
        cell1.setBorderWidthLeft(3f);
        cell1.setBorderWidthRight(3f);
        cell1.setBorderWidthTop(10f);
        cell1.setPadding(0f);
        //Heading
        pHeading = new Paragraph(8f, "Disposition Details", mainHeadingQuote);
        pHeading.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(pHeading);
        //company Name
        PdfPTable ntable1 = new PdfPTable(9);
        ntable1.setWidthPercentage(100f);
        ntable1.setWidths(new float[]{0.2f, 2f, 0.25f, 1.5f, 0.25f, 4.5f, 1.5f, 5f, .5f});
        ntable1.addCell(createEmptyCell(0, 0.1f, 9));

        ntable1.addCell(createEmptyCell(0, 1f, 0));
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        Chunk c1 = new Chunk("DATE", greenCourierFont9);
        cell.addElement(c1);
        ntable1.addCell(cell);

        ntable1.addCell(createEmptyCell(0, .5f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        c1 = new Chunk("TIME", greenCourierFont9);
        cell.addElement(c1);
        ntable1.addCell(cell);

        ntable1.addCell(createEmptyCell(0, .5f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        c1 = new Chunk("CONTAINER STATUS", greenCourierFont9);
        cell.addElement(c1);
        ntable1.addCell(cell);

        ntable1.addCell(createEmptyCell(0, .5f, 0));
        ntable1.addCell(createEmptyCell(0, .5f, 0));
        ntable1.addCell(createEmptyCell(0, .5f, 0));
        LclSsDetail lclSsDetail = lclUnitSs.getLclSsHeader().getVesselSsDetail();
        List<LclUnitSsDispo> dispositionList = new LclUnitSsDispoDAO().getUnitDispoDetailsWithoutData(lclUnitSs.getLclUnit().getId(), lclSsDetail.getId());
        if (!dispositionList.isEmpty()) {
            for (int i = 0; i < dispositionList.size(); i++) {
                LclUnitSsDispo lclUnitSsDispo = (LclUnitSsDispo) dispositionList.get(i);
                String dateTimeV = DateUtils.formatDate(lclUnitSsDispo.getDispositionDatetime(), "dd-MMM-yyyy hh:mm a");
                String[] dateTimeArray1 = dateTimeV.split(" ");
                ntable1.addCell(createEmptyCell(0, 1f, 0));
                cell = new PdfPCell();
                cell.setBorder(0);
                pHeading = new Paragraph(5f, "" + dateTimeArray1[0], greenCourierFont9);
                cell.addElement(pHeading);
                ntable1.addCell(cell);

                ntable1.addCell(createEmptyCell(0, .5f, 0));

                cell = new PdfPCell();
                cell.setBorder(0);
                pHeading = new Paragraph(5f, "" + dateTimeArray1[1] + " " + dateTimeArray1[2], greenCourierFont9);
                cell.addElement(pHeading);
                ntable1.addCell(cell);

                ntable1.addCell(createEmptyCell(0, .5f, 0));

                cell = new PdfPCell();
                cell.setBorder(0);
                if (lclUnitSsDispo.getDisposition() != null && lclUnitSsDispo.getDisposition().getDescription() != null) {
                    pHeading = new Paragraph(5f, "" + lclUnitSsDispo.getDisposition().getDescription(), greenCourierFont9);
                } else {
                    pHeading = new Paragraph(5f, "" + lclUnitSsDispo.getDisposition().getEliteCode(), greenCourierFont9);
                }
                cell.addElement(pHeading);
                ntable1.addCell(cell);
                ntable1.addCell(createEmptyCell(0, .5f, 0));
                ntable1.addCell(createEmptyCell(0, .5f, 0));
                ntable1.addCell(createEmptyCell(0, .5f, 0));
            }
        }
        ntable1.addCell(createEmptyCell(0, 2f, 9));
        cell1.addElement(ntable1);
        table.addCell(cell1);
        return table;
    }

    public PdfPTable voyContent(String voyContent) throws DocumentException, Exception {
        Paragraph pHeading = null;
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        PdfPCell cell1 = null;
        cell1 = new PdfPCell();
        cell1.setBorder(0);
        cell1.setBorderColor(new BaseColor(00, 51, 153));
        cell1.setBorderWidthBottom(3f);
        cell1.setBorderWidthLeft(3f);
        cell1.setBorderWidthRight(3f);
        cell1.setBorderWidthTop(10f);
        cell1.setPadding(0f);
        //Heading
        pHeading = new Paragraph(8f, "Voyage Comments", mainHeadingQuote);
        pHeading.setAlignment(Element.ALIGN_CENTER);
        cell1.addElement(pHeading);
        //company Name
        PdfPTable ntable1 = new PdfPTable(2);
        ntable1.setWidthPercentage(100f);
        ntable1.setWidths(new float[]{0.1f, 5f});
        ntable1.addCell(createEmptyCell(0, 0.1f, 2));

        ntable1.addCell(createEmptyCell(0, 1f, 0));
        cell = new PdfPCell();
        cell.setBorder(0);
        pHeading = new Paragraph(8f, "" + voyContent.toUpperCase(), blackContentBoldFont);
        cell.addElement(pHeading);
        ntable1.addCell(cell);

        ntable1.addCell(createEmptyCell(0, 2f, 2));
        cell1.addElement(ntable1);
        table.addCell(cell1);
        return table;
    }

    public PdfPTable thankyouMsg(String contextPath) throws DocumentException, Exception {
        BaseFont palationRomanBase = BaseFont.createFont(contextPath + "/ttf/palabi.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font palatinoRomanSmallFont = new Font(palationRomanBase, 8.5f, Font.ITALIC, new BaseColor(00, 102, 12));
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        table.addCell(createEmptyCell(0, 10f, 0));
        cell = new PdfPCell();
        cell.setBorder(0);
        Paragraph pContent = new Paragraph(8f, "Thank you for choosing Econocaribe Consolidators, Inc. for your international shipping needs. \n Please contact us if you have any questions at 1-866-ECONO-IT (866-326-6648) or visit us at www.econocaribe.com.", palatinoRomanSmallFont);
        pContent.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(pContent);
        table.addCell(cell);
        return table;
    }
}
