package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.reports.*;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.directwebremoting.WebContextFactory;

import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.beans.LCLCorrectionChargeBean;
import com.gp.cong.logisoft.beans.LCLCorrectionNoticeBean;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.struts.form.FclBlCorrectionsForm;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class LclCorrectionsReportPdfCreator extends ReportFormatMethods {

    Document document = null;
    PdfWriter pdfWriter = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

    public void initialize(String fileName) throws FileNotFoundException,
            DocumentException {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 30, 30);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(
                fileName));
        document.open();
    }

    public void createBody(String contextPath, LCLCorrectionNoticeBean lclCorrectionNoticeBean, HttpServletRequest request)
            throws MalformedURLException, IOException, DocumentException, Exception {
        LoadLogisoftProperties loadLogisoftProperties = new LoadLogisoftProperties();
        BaseFont palationRomanBase = BaseFont.createFont(contextPath + "/ttf/Palatino-Roman.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font palatinoRomanSmallFont = new Font(palationRomanBase, 8, Font.NORMAL, Color.BLACK);

        NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy ");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");


        PdfPCell cell = null;

        Calendar calendar = new GregorianCalendar();
        String am_pm;
        String currentTime;
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        if (calendar.get(Calendar.AM_PM) == 0) {
            am_pm = "AM";
        } else {
            am_pm = "PM";
        }

        currentTime = hour + ":" + minute + " " + am_pm;
        PdfPTable headingTable = makeTable(5);
        headingTable.setWidthPercentage(100);


        LoadLogisoftProperties loadLogisoftProperties1 = new LoadLogisoftProperties();
        String path = loadLogisoftProperties1.getProperty("application.image.logo");
        PdfPCell celL = new PdfPCell();
        PdfPTable table = new PdfPTable(1);
        String realPath = request.getRealPath("/");
        Image img = Image.getInstance(realPath + path);
        table.setWidthPercentage(100);
        img.scalePercent(60);
        img.scaleAbsoluteWidth(200);
        celL.addElement(new Chunk(img, 185, -5));
        celL.setBorder(0);
        celL.setHorizontalAlignment(Element.ALIGN_CENTER);
        celL.setColspan(5);
        celL.setVerticalAlignment(Element.ALIGN_CENTER);

        headingTable.addCell(celL);

        headingTable.setWidths(new float[]{8, 28, 46, 8, 10});
        headingTable.addCell(makeCellLeftNoBorderFclBL("Date :"));
        headingTable.addCell(makeCellLeftNoBorderPalatinoFclBl(sdf1.format(new Date()), palatinoRomanSmallFont));
        cell = makeCellleftNoBorderForHeadingFclBL("");
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        headingTable.addCell(cell);
        headingTable.addCell(makeCellLeftNoBorderFclBL(""));
        headingTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));


        headingTable.addCell(makeCellLeftNoBorderFclBL(""));
        headingTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
        if (lclCorrectionNoticeBean.getVoidStatus() == 1) {
            cell = makeCellleftNoBorderForHeadingWithRedFont(FclBlConstants.VOIDED);
            headingTable.addCell(cell);
        } else {
            headingTable.addCell(makeCellLeftNoBorderPalatinoFclBl("                        " + "Correction Notices", palatinoRomanSmallFont));
        }


        headingTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
        headingTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));

        cell = makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont);
        cell.setColspan(5);
        headingTable.addCell(cell);
        headingTable.addCell(cell);

//        cell = makeCellLeftNoBorderPalatinoFclBl("",palatinoRomanSmallFont);
//        cell.setColspan(2);
//        headingTable.addCell(cell);
//        headingTable.addCell(makeCellLeftNoBorderPalatinoFclBl((null!=blCorrections.getOrigin()?blCorrections.getOrigin():"")+
//        		"  ,  "+(null!=blCorrections.getDestination()?blCorrections.getDestination():""),palatinoRomanSmallFont));

        headingTable.addCell(cell);

        cell = makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont);
        cell.setColspan(5);
        headingTable.addCell(cell);
        headingTable.addCell(cell);

        PdfPTable middleTable = makeTable(4);
        middleTable.setWidthPercentage(100);
        middleTable.setWidths(new float[]{20, 30, 5, 45});


        cell = makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont);
        cell.setColspan(4);
        middleTable.addCell(cell);
        middleTable.addCell(cell);
        //FOR LABEL cell = makeCellLeftNoBorderFclBL("SHIPPER/EXPORTER");
        //FOR DATA cell = makeCellLeftNoBorderPalatinoFclBl("SHIPPER/EXPORTER",palatinoRomanSmallFont);
        PdfPTable detailsTable = makeTable(2);
        detailsTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        detailsTable.setWidthPercentage(100);

        detailsTable.setWidths(new float[]{17, 83});
        PdfPTable destinationOriginTable = makeTable(4);
        destinationOriginTable.setWidths(new float[]{17, 35, 15, 33});
        destinationOriginTable.setWidthPercentage(100);
        destinationOriginTable.addCell(makeCellRightNoBorderFclBL("Correction Notice Date :"));
        destinationOriginTable.addCell(makeCellLeftNoBorderPalatinoFclBl(sdf.format(lclCorrectionNoticeBean.getCorrectionDate()), palatinoRomanSmallFont));
        destinationOriginTable.addCell(makeCellRightNoBorderFclBL("Origin :"));
        destinationOriginTable.addCell(makeCellLeftNoBorderPalatinoFclBl(lclCorrectionNoticeBean.getOrigin(), palatinoRomanSmallFont));

        destinationOriginTable.addCell(makeCellRightNoBorderFclBL("Bill of Lading Number :    "));
        //detailsTable.addCell(makeCellLeftNoBorderPalatinoFclBl(lclCorrectionNoticeBean.getBlNo(),palatinoRomanSmallFont));
        destinationOriginTable.addCell(makeCellLeftNoBorderPalatinoFclBl(lclCorrectionNoticeBean.getBlNo() + "  " + "CN# " + lclCorrectionNoticeBean.getCorrectionNo()
                + (lclCorrectionNoticeBean.getCorrectionStatus().equalsIgnoreCase("A") && CommonUtils.isNotEmpty(lclCorrectionNoticeBean.getPostedDate()) ? "  (Posted)" : "  (Not Posted)"), palatinoRomanSmallFont));
        destinationOriginTable.addCell(makeCellRightNoBorderFclBL("Destination :"));
        destinationOriginTable.addCell(makeCellLeftNoBorderPalatinoFclBl(lclCorrectionNoticeBean.getDestination(), palatinoRomanSmallFont));
        cell.addElement(destinationOriginTable);
        cell.setColspan(2);
        detailsTable.addCell(cell);

        detailsTable.addCell(makeCellRightNoBorderFclBL("Sailing Date :    "));
        detailsTable.addCell(makeCellLeftNoBorderPalatinoFclBl((null != lclCorrectionNoticeBean.getSailDate() ? sdf.format(lclCorrectionNoticeBean.getSailDate()) : ""), palatinoRomanSmallFont));
        detailsTable.addCell(makeCellRightNoBorderFclBL("Vessel :    "));
        detailsTable.addCell(makeCellLeftNoBorderPalatinoFclBl(lclCorrectionNoticeBean.getVesselName(), palatinoRomanSmallFont));
        detailsTable.addCell(makeCellRightNoBorderFclBL("Shipper :    "));
        detailsTable.addCell(makeCellLeftNoBorderPalatinoFclBl(lclCorrectionNoticeBean.getShipperName(), palatinoRomanSmallFont));
        detailsTable.addCell(makeCellRightNoBorderFclBL("Freight Forwarder :    "));
        detailsTable.addCell(makeCellLeftNoBorderPalatinoFclBl(lclCorrectionNoticeBean.getForwarderName(), palatinoRomanSmallFont));
        detailsTable.addCell(makeCellRightNoBorderFclBL("Consignee :    "));
        detailsTable.addCell(makeCellLeftNoBorderPalatinoFclBl(lclCorrectionNoticeBean.getConsigneeName(), palatinoRomanSmallFont));
        detailsTable.addCell(makeCellRightNoBorderFclBL("Bill To :    "));
        detailsTable.addCell(makeCellLeftNoBorderPalatinoFclBl(lclCorrectionNoticeBean.getBillingTypeLabel(), palatinoRomanSmallFont));

        cell = makeCellleftNoBorder("");
        cell.setColspan(2);
        detailsTable.addCell(cell);
        detailsTable.addCell(cell);
        // adding pdfptable to document
        //charges table
        double totalAmount = 0d;
        double newAmount = 0d;
        double diffAmount = 0d;
        PdfPTable chargesTable = makeTable(6);
        chargesTable.setWidthPercentage(100);
        chargesTable.setWidths(new float[]{10, 17, 20, 13, 20, 20});
        chargesTable.addCell(makeCellLeftNoBorderFclBLUnderLined("Code"));
        chargesTable.addCell(makeCellLeftNoBorderFclBLUnderLined("Billed As"));
        chargesTable.addCell(makeCellRightNoBorderFclBLUnderLined("Current Charges"));
        chargesTable.addCell(makeCellRightNoBorderFclBLUnderLined("Bill To Party"));
        if (lclCorrectionNoticeBean.getStrCorrectionType().equalsIgnoreCase("A")) {
            chargesTable.addCell(makeCellRightNoBorderFclBLUnderLined("New Amount"));
            chargesTable.addCell(makeCellRightNoBorderFclBLUnderLined("Net Difference"));
        } else {
            chargesTable.addCell(makeCellLeftNoBorderFclBL(""));
            chargesTable.addCell(makeCellLeftNoBorderFclBL(""));
        }
        cell = makeCellleftNoBorder("");
        cell.setColspan(6);
        chargesTable.addCell(cell);
        if (lclCorrectionNoticeBean.getVoidStatus() == 0 && (lclCorrectionNoticeBean.getStrCorrectionType().equalsIgnoreCase("A") ||
            lclCorrectionNoticeBean.getStrCorrectionType().equalsIgnoreCase("Y"))) {
            List<LCLCorrectionChargeBean> lclCorrectionChargesList = (List) request.getAttribute("lclCorrectionChargesList");
            if (null != lclCorrectionChargesList) {
                for (LCLCorrectionChargeBean lclCorrectionChargeBean : lclCorrectionChargesList) {
                    if (lclCorrectionChargeBean.getOldAmount() != null) {
                        totalAmount += lclCorrectionChargeBean.getOldAmount().doubleValue();
                    }
                    if (lclCorrectionChargeBean.getNewAmount() != null) {
                        newAmount += lclCorrectionChargeBean.getNewAmount().doubleValue();
                    }
                    if (lclCorrectionChargeBean.getDifferenceAmount() != null) {
                        diffAmount += lclCorrectionChargeBean.getDifferenceAmount().doubleValue();
                    }
                    chargesTable.addCell(makeCellLeftNoBorderPalatinoFclBl(lclCorrectionChargeBean.getChargeCode(), palatinoRomanSmallFont));
                    chargesTable.addCell(makeCellLeftNoBorderPalatinoFclBl(lclCorrectionChargeBean.getChargeDescriptions(), palatinoRomanSmallFont));
                    chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl(lclCorrectionChargeBean.getOldAmount().toString(), palatinoRomanSmallFont));
                    chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl(lclCorrectionChargeBean.getBillToPartyLabel(), palatinoRomanSmallFont));
                    chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl((null != lclCorrectionChargeBean.getNewAmount() ? "$" + lclCorrectionChargeBean.getNewAmount().toString() : ""), palatinoRomanSmallFont));
                    chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl((null != lclCorrectionChargeBean.getDifferenceAmount() ? "$" + lclCorrectionChargeBean.getDifferenceAmount().toString() : ""), palatinoRomanSmallFont));
                }
            }
            if (totalAmount != 0d) {
                cell = makeCellleftNoBorder("");
                cell.setColspan(6);
                chargesTable.addCell(cell);
                chargesTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                chargesTable.addCell(makeCellLeftNoBorderPalatinoFclBl("Total", palatinoRomanSmallFont));
                chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl("$" + numberFormat.format(totalAmount), palatinoRomanSmallFont));
                chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                if (lclCorrectionNoticeBean.getStrCorrectionType().equalsIgnoreCase("A")) {
                    chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl("$" + numberFormat.format(newAmount), palatinoRomanSmallFont));
                    chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl("$" + numberFormat.format(diffAmount), palatinoRomanSmallFont));
                } else {
                    chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                    chargesTable.addCell(makeCellRightNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
                }
            }
        }
        cell = makeCellleftNoBorder("");
        cell.setColspan(6);
        chargesTable.addCell(cell);
        chargesTable.addCell(cell);
        //end of charges table--------
        PdfPTable correctionTypeTable = makeTable(2);
        correctionTypeTable.setWidthPercentage(100);
        correctionTypeTable.setWidths(new float[]{10, 90});
        correctionTypeTable.addCell(makeCellLeftNoBorderFclBL("C/N Code :    "));
        correctionTypeTable.addCell(makeCellLeftNoBorderPalatinoFclBl(lclCorrectionNoticeBean.getCorrectionCodeValue(), palatinoRomanSmallFont));
        cell = makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont);
        cell.setColspan(2);
        correctionTypeTable.addCell(cell);
        cell = makeCellLeftNoBorderPalatinoFclBl(lclCorrectionNoticeBean.getCorrectionTypeValue(), palatinoRomanSmallFont);
        cell.setColspan(2);
        correctionTypeTable.addCell(cell);
        if (!lclCorrectionNoticeBean.getStrCorrectionType().equalsIgnoreCase("A")) {
            correctionTypeTable.addCell(makeCellLeftNoBorderFclBL("New BillTo :    "));
            //correctionTypeTable.addCell(makeCellLeftNoBorderPalatinoFclBl(blCorrections.getAccountName()+"/"+blCorrections.getAccountNumber(),palatinoRomanSmallFont));
        }

        cell = makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont);
        cell.setColspan(2);
        correctionTypeTable.addCell(cell);
        correctionTypeTable.addCell(cell);

        cell = makeCellLeftNoBorderFclBL("Comments:");
        cell.setColspan(2);
        correctionTypeTable.addCell(cell);

        cell = makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont);
        cell.setColspan(2);
        correctionTypeTable.addCell(cell);


        PdfPTable commentsTable = makeTable(2);
        commentsTable.setWidthPercentage(100);
        commentsTable.setWidths(new float[]{3, 97});
        commentsTable.addCell(makeCellLeftNoBorderPalatinoFclBl("", palatinoRomanSmallFont));
        commentsTable.addCell(makeCellLeftNoBorderPalatinoFclBl(lclCorrectionNoticeBean.getComments(), palatinoRomanSmallFont));

        document.add(headingTable);
        document.add(middleTable);
        document.add(detailsTable);
        document.add(chargesTable);
        document.add(correctionTypeTable);
        document.add(commentsTable);

    }

    public void destroy() {
        document.close();
    }

    public String createLclCorrectionsReport(LCLCorrectionNoticeBean lclCorrectionNoticeBean, String fileName,
            String contextPath, MessageResources messageResources, HttpServletRequest request) throws Exception {
        this.initialize(fileName);
        this.createBody(contextPath, lclCorrectionNoticeBean, request);
        this.destroy();
        return "fileName";
    }
}
