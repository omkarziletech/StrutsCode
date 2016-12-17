/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.beans.ManifestBean;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

/**
 *
 * @author RATHNAPANDIAN_G
 */
public class LclUnitExcepionsPdfCreator extends LclReportFormatMethods {

    public LclUnitSs lclUnitSs;
    public LclSsDetail lclssdetail;
    public String scheduleNo;
    public String origin;
    public String destination;
    public String orgin;
    public String pol;
    String unLocationCode = "";
    StringBuilder unitsNumber = new StringBuilder();
    StringBuilder sailDate = new StringBuilder();
    StringBuilder conoslidatelistFileNo = new StringBuilder();
    StringBuilder blNumber = new StringBuilder();

    class TableHeader extends PdfPageEventHelper {

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                document.add(onHeader());
                document.add(onHeaderBottom());
                document.add(onStatementLine());
                document.add(onHeaderMiddle());
                document.add(unitExceptionsValuesTable());

            } catch (Exception ex) {
            }
        }
    }

    public void createPdf(String realPath, String outputFileName, String unitssId) throws Exception {
        try {
            lclUnitSs = new LclUnitSsDAO().findById(new Long(unitssId));
            lclssdetail = new LclSsDetailDAO().findByTransMode(lclUnitSs.getLclSsHeader().getId(), "V");
            if(lclssdetail == null){
                lclssdetail = lclUnitSs.getLclSsHeader().getLclSsDetailList().get(0);
            }
            scheduleNo = lclUnitSs.getLclSsHeader().getScheduleNo();
            String unitNo = lclUnitSs.getLclUnit().getUnitNo();
            LclSSMasterBl lclSSMasterBl = null;
            StringBuilder consigneeDetails = new StringBuilder();
            document = new Document();
            document.setPageSize(PageSize.A4);
            document.setMargins(10, 10, 2, 2);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
            LclUnitExcepionsPdfCreator.TableHeader event = new LclUnitExcepionsPdfCreator.TableHeader();
            pdfWriter.setPageEvent(event);

            document.open();
            //document.add(onHeader());
            //document.add(onHeaderBottom());
            //document.add(onStatementLine());
            //document.add(onHeaderMiddle());
            //document.add(unitExceptionsValuesTable());
            document.add(onHeaderMiddledd(unitssId));

            //document.add(unitExceptionsValuesTable(unitssId));
            document.close();
        } catch (Exception e) {
            throw e;
        }
    }

    public PdfPTable onHeader() throws Exception {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(99f);
        table.setWidths(new float[]{3f, 5f, 3f});
        PdfPCell cell = new PdfPCell();

        cell.setBorder(0);
        cell.setPaddingTop(20f);
        Paragraph p = new Paragraph(6f, "Page# " + pdfWriter.getCurrentPageNumber(), blackNormalCourierFont12f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(20f);
        p = new Paragraph(6f, "ECONOCARIBE CONSOLIDATORS, Inc.\n\n", blackNormalCourierFont12f);
        p.add(" EXCEPTION LIST");
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(17f);
        p = new Paragraph(12f, "DATE PRINTED:" + DateUtils.formatDate(new Date(), "MM/dd/YY") + "\n", blackNormalCourierFont12f);
        p.add("TIME PRINTED:" + DateUtils.formatStringDateToTimeTT(new Date()));
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable onHeaderBottom() throws Exception {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(99f);
        table.setWidths(new float[]{8f, 4.5f});
        PdfPCell cell = new PdfPCell();

        cell.setBorder(0);
        cell.setPaddingTop(12f);
        String origin = lclUnitSs.getLclSsHeader().getOrigin().getUnLocationName();

        String concatOrgin = lclUnitSs.getLclSsHeader().getOrigin().getStateId().getCode();
        concatOrgin = null != concatOrgin ? concatOrgin : "";
        String SailDate = null != lclssdetail.getStd() ? DateUtils.formatDate(lclssdetail.getStd(), "MM/dd/YY") : "";

        String voyage = null != lclUnitSs.getLclSsHeader().getScheduleNo() ? lclUnitSs.getLclSsHeader().getScheduleNo() : "";
        String portOfDischarge = null != lclUnitSs.getLclSsHeader().getDestination().getUnLocationName()
                ? lclUnitSs.getLclSsHeader().getDestination().getUnLocationName() : "";
        String portOfDischargee = null != lclUnitSs.getLclSsHeader().getDestination().getCountryId().getCodedesc()
                ? lclUnitSs.getLclSsHeader().getDestination().getCountryId().getCodedesc() : "";
        Paragraph p = new Paragraph(10.5f, "\t \t \t \t \t \t \t \t \t \t \t Origin -" + origin.toUpperCase() + "," + concatOrgin.toUpperCase(), blackNormalCourierFont12f);
        p.add("\n\nPort of Discharge -" + portOfDischargee.toUpperCase() + "," + portOfDischarge.toUpperCase());
        p.add("\n\n \t \t \t \t \t \t \t \t \t \t Voyage -" + voyage);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(14f);
        cell.setPaddingLeft(7f);
        p = new Paragraph(9f, "\t \t \t \t \t \t Sailing Date-" + SailDate + "\n\n", blackNormalCourierFont12f);
        String vessel = null != lclssdetail.getSpReferenceName() ? lclssdetail.getSpReferenceName() : "";
        p.add("Vessel-" + vessel);

        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    public PdfPTable onStatementLine() throws DocumentException, Exception {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{3f});
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(12f);
        cell.setPaddingBottom(12f);
        Paragraph p = new Paragraph(11f, "\t \t \t \t \t \t \t \t \tWith Reference To The Above Mentioned Sailing,Please Be Advised \n ", blackNormalCourierFont12f);
        p.add("\t \t \t \t \t \t \t of The Following Discrepancies:");
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    public PdfPTable onHeaderMiddle() throws Exception {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{10f, 12f, 13f, 14f});
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(14f);
        Paragraph p = new Paragraph(6f, "Trailer#\n", blackNormalCourierFont12f);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        //D/c
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(14f);
        p = new Paragraph(6f, "Dock Receipts ", blackNormalCourierFont12f);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        //b/l
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(14f);
        p = new Paragraph(6f, "B / L #", blackNormalCourierFont12f);
        cell.addElement(p);
        table.addCell(cell);
        //Exception
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(14f);
        p = new Paragraph(6f, "\t \t Exceptions", blackNormalCourierFont12f);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    public PdfPTable unitExceptionsValuesTable() throws DocumentException, Exception {

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{6f});
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(7f);
        Paragraph p = new Paragraph(6f, "=============================================================================", blackNormalCourierFont12f);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    public PdfPTable onHeaderMiddledd(String unitssId) throws Exception {
        LclUnitDAO lclunitdao = new LclUnitDAO();
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{10f, 10f, 16f, 14f});
        List<ManifestBean> manifestBeanList = lclunitdao.getAllDRSForViewUnitExceptionByUnitSSId(new Long(unitssId), "false");
        if (manifestBeanList != null && manifestBeanList.size() > 0) {
            for (ManifestBean manifestBean : manifestBeanList) {
                String pol = manifestBean.getPol();
                PdfPCell cell = new PdfPCell();

                cell.setBorder(0);
                cell.setPaddingTop(14f);

                String trailer = null != lclUnitSs.getLclUnit().getUnitNo() ? lclUnitSs.getLclUnit().getUnitNo() : "";
                Paragraph p = new Paragraph(6f, trailer, blackNormalCourierFont12f);
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setPaddingTop(14f);
                p = new Paragraph(6f, manifestBean.getFileNo(), blackNormalCourierFont12f);
                p.setAlignment(Element.ALIGN_MIDDLE);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setPaddingTop(13f);
                String Bl = null != manifestBean.getBlNo() ? manifestBean.getBlNo() : "";
                p = new Paragraph(8f, Bl + "\n\n\n\n", blackNormalCourierFont12f);
                String consignee = null != manifestBean.getConsigneeName() ? manifestBean.getConsigneeName() : "";
                Paragraph r = new Paragraph(12f, "", blackNormalCourierFont12f);
                r.add(new Chunk("CONSIGNEE:").setUnderline(0.1f, -2f));
                r.add(consignee.toUpperCase());
                cell.addElement(p);
                cell.addElement(r);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setPaddingTop(10f);

                String exception = null != manifestBean.getUnitException() ? manifestBean.getUnitException() : "";
                p = new Paragraph(10f, exception.toUpperCase(), blackNormalCourierFont12f);
                cell.addElement(p);
                table.addCell(cell);
            }
        }
        return table;
    }

}
