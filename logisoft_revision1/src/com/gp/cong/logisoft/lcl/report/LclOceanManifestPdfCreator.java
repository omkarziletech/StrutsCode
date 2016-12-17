package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import static com.gp.cong.common.CommonUtils.log;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.ExportUnitQueryUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.lcl.ExportVoyageHblBatchForm;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import java.util.Date;

public class LclOceanManifestPdfCreator extends LclReportFormatMethods {

    private LclUnitSs lclUnitSs;
    private String unitNumber = "";
    private String sailingDate = "";
    private String voyage = "";
    StringBuilder sailDate = new StringBuilder();
    private PdfWriter PdfWriter;
    StringBuilder origin = new StringBuilder();
    private String sealNo = "";
    private StringBuilder Vessel = new StringBuilder();
    private String portOfLoading = "";
    private String portOfDischarge = "";
    private String flag = "";
    private String ruleName = "";
    private Long isFileExist = 0l;
    private Double CFT = 0.00;
    private Double CBM = 0.00;
    private Double KGS = 0.00;
    private Double LBS = 0.00;
    private String metricEngmet = "";
    private int pieceTotal = 0;
    private boolean documentOpenFlag =false;
    private int count = 0;

    class oceanManifestPageEvent extends PdfPageEventHelper {

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                document.add(createHeader());
                document.add(createSubHeader());
                document.add(createUnitsTable());
            } catch (Exception ex) {
                log.info("Exception on class LclOceanManifestPdfCreator in method onStartPage" + new Date(), ex);
            }
        }
//        @Override
//        public void onEndPage(PdfWriter writer, Document document) {
//            try {
//                PdfPTable commityTable = createEndTable();
//                commityTable.setTotalWidth(document.getPageSize().getWidth() - 60);
//                commityTable.writeSelectedRows(0, 20, 30, 40, writer.getDirectContent());
//            } catch (Exception ex) {
//                log.info("Exception on class LclOceanManifestPdfCreator in method onEndPage" + new Date(), ex);
//            }
//        }
    }

    public LclOceanManifestPdfCreator(LclUnitSs lclUnitSs) throws Exception {
        this.lclUnitSs = lclUnitSs;
        LclUtils utils = new LclUtils();
        if(null != lclUnitSs){
        if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader())) {
            portOfDischarge = utils.getConcatenatedOriginByWithOutCode(lclUnitSs.getLclSsHeader().getDestination());
            portOfLoading = utils.getConcatenatedOriginByWithOutCode(lclUnitSs.getLclSsHeader().getOrigin());
        }
        unitNumber = lclUnitSs.getLclUnit().getUnitNo();
        if (CommonFunctions.isNotNull(lclUnitSs.getSUHeadingNote())) {
            sealNo = lclUnitSs.getSUHeadingNote();
        }
        LclSsDetail ssDetail = lclUnitSs.getLclSsHeader().getVesselSsDetail();
        if (null != ssDetail) {
            if (CommonFunctions.isNotNull(ssDetail.getSpReferenceName())) {
                flag = new LclSsDetailDAO().getFlagFromElite(ssDetail.getSpReferenceName());
                Vessel.append(ssDetail.getSpReferenceName()).append(" .  ").append(ssDetail.getTransMode()).append("  ");
            }
            if (CommonFunctions.isNotNull(ssDetail.getSpReferenceNo())) {
                Vessel.append(ssDetail.getSpReferenceNo());
            }
            if (CommonFunctions.isNotNull(ssDetail.getStd())) {
                sailingDate = (DateUtils.formatStringDateToAppFormatMMM(ssDetail.getStd()));
            }
        }
        if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getScheduleNo())) {
            voyage = (lclUnitSs.getLclSsHeader().getScheduleNo());
        }
        }
    }
   
    public void CreateOceanManifestPdf(String realPath, String outputFileName) throws IOException, DocumentException, SQLException, Exception {
        document = new Document();
        document.setPageSize(PageSize.LEGAL.rotate());
        document.setMargins(26, 25, 15, 40);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        //  pdfWriter.setViewerPreferences(PdfWriter.FitWindow);
        LclOceanManifestPdfCreator.oceanManifestPageEvent event = new LclOceanManifestPdfCreator.oceanManifestPageEvent();
        pdfWriter.setPageEvent(event);
        document.open();
        document.add(createBodyTable());
        document.add(setTotalTable());
        document.add(createEndTable());
        document.close();

    }
    
    public PdfPTable createHeader() throws Exception {

        Paragraph p = null;
        table = new PdfPTable(8);
        table.setWidthPercentage(99f);
        table.setWidths(new float[]{1.5f, 0.2f, 0.6f, 0.2f, 2f, 2f, 3f, 2f});
        ruleName = new SystemRulesDAO().getSystemRulesByCode("CompanyName");
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(8);
        p = new Paragraph(8f, null != ruleName ? ruleName : "", blackboldCourierFont11f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setPaddingLeft(-30f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        cell.setBorderWidthTop(0.06f);
        p = new Paragraph(8f, "TRAILER NO.", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        p = new Paragraph(8f, "SEAL", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(1);
        cell.setPaddingRight(-6f);
        p = new Paragraph(8f, "", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        String companyAddress = new SystemRulesDAO().getSystemRulesByCode("CompanyAddress");
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(8f, null != companyAddress ? companyAddress.toUpperCase() : "" + "", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setPaddingLeft(-6f);
        p = new Paragraph(8f, "CARGO", blackboldCourierFont11f);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "" + unitNumber, courierBoldFont6);
        p.setAlignment(Element.ALIGN_CENTER);
//        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        p = new Paragraph(8f, "" + sealNo, courierBoldFont6);
        p.setAlignment(Element.ALIGN_CENTER);
//        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(1);
        cell.setPaddingRight(-6f);
        p = new Paragraph(8f, "", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(1);
        cell.setPaddingLeft(-6f);
        p = new Paragraph(8f, "", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        p = new Paragraph(8f, "MANIFEST", blackboldCourierFont11f);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell.addElement(table);
        table.addCell(cell);

        return table;
    }

    private Element createSubHeader() throws DocumentException, Exception {
        cell = new PdfPCell();
        cell.setColspan(8);
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        PdfPTable table = new PdfPTable(7);
        table.setWidths(new float[]{2.2f, 0.8f, 1.5f, 0.6f, 1.5f, 2f, 0.4f});
        table.setWidthPercentage(99f);
        Paragraph p;

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "SAILING ON VESSEL/VOYAGE", blackBoldFontSize6);
//        p = new Paragraph(8f, lclUnitSs.getLclSsHeader()blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "FLAG", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "DATE", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "VOYAGE", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "PORT OF LOADING", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "PORT OF DISCHARGE", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "PG NO.", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "" + Vessel.toString(), courierBoldFont6);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.addElement(p);
        table.addCell(cell);
        //flag
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "" + flag, courierBoldFont6);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "     " + sailingDate, courierBoldFont6);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "  " + lclUnitSs.getLclSsHeader().getScheduleNo(), courierBoldFont6);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "" + portOfLoading, courierBoldFont6);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "" + portOfDischarge, courierBoldFont6);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "" + pdfWriter.getCurrentPageNumber(), courierBoldFont6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);
        //1

        cell.addElement(table);
        table.addCell(cell);

        return table;
    }

    private Element createUnitsTable() throws DocumentException {

        cell = new PdfPCell();
        cell.setColspan(13);
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        PdfPTable table = new PdfPTable(12);
        table.setWidths(new float[]{1.1f, 0.6f, 2f, 2f, 1.3f, 0.3f, 0.3f, 2.5f, 0.6f, 0.6f, 0.4f, 0.4f});
        table.setWidthPercentage(99f);
        Paragraph p;

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "B/L", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "D/R NO.", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "CONSIGNEE", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "NOTIFY", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "MARKS", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "QTY.", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "TYPE", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "COMMODITY", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "CFT.", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "WT.", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "O/F PPD", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(0);
        p = new Paragraph(8f, "O/F COLL", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);

        cell.addElement(table);
        table.addCell(cell);

        return table;

    }

    private Element createBodyTable() throws DocumentException, Exception {
        table = new PdfPTable(12);
        table.setWidths(new float[]{1.1f, 0.6f, 2f, 2f, 1.3f, 0.3f, 0.3f, 2.5f, 0.6f, 0.6f, 0.4f, 0.4f});
        table.setWidthPercentage(99f);
        Paragraph p;
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        LclConsolidateDAO lclConsolidateDAO = new LclConsolidateDAO();
        LclBookingHsCodeDAO lclBookingHsCodeDAO = new LclBookingHsCodeDAO();
        LclInbondsDAO inbondDAO = new LclInbondsDAO();
        LclReportUtils reportUtils = new LclReportUtils();
        LclBl lclbl = null;

        List<OceanManifestBean> pickedDrList = new LclBLPieceDAO().getPickedDRList(lclUnitSs.getId(), lclUnitSs.getLclSsHeader().getId());
        if (pickedDrList != null && pickedDrList.size() > 0) {
            int count = 0;

            for (OceanManifestBean pickedLclBl : pickedDrList) {
                count++;
                String packageAbbr = "";
                int pieceCount = 0;
                StringBuilder commdityDesc = new StringBuilder();
                StringBuilder measureValue = new StringBuilder();
                StringBuilder weightValue = new StringBuilder();
                double prepaidAmount = 0.00;
                double collectAmount = 0.00;

                if (CommonFunctions.isNotNull(pickedLclBl.getPiece())) {
                    pieceCount = pickedLclBl.getPiece();
                    pieceTotal = pieceTotal + pickedLclBl.getPiece();
                }
                if (CommonFunctions.isNotNull(pickedLclBl.getCft()) && !metricEngmet.equalsIgnoreCase("M")) {
                    measureValue.append(Math.round(pickedLclBl.getCft())).append(" CFT").append("\n");
                    CFT = CFT + pickedLclBl.getCft();
                }
                if (CommonFunctions.isNotNull(pickedLclBl.getCbm())) {
                    measureValue.append(pickedLclBl.getCbm().doubleValue()).append(" CBM");
                    CBM = CBM + pickedLclBl.getCbm();
                }
                if (CommonFunctions.isNotNull(pickedLclBl.getLbs()) && !metricEngmet.equalsIgnoreCase("M")) {
                    weightValue.append(Math.round(pickedLclBl.getLbs())).append(" LBS").append("\n");
                    LBS = LBS + pickedLclBl.getLbs();
                }
                if (CommonFunctions.isNotNull(pickedLclBl.getKgs())) {
                    weightValue.append(pickedLclBl.getKgs().doubleValue()).append(" KGS");
                    KGS = KGS + pickedLclBl.getKgs();
                }
                if (CommonFunctions.isNotNull(pickedLclBl.getPackageType())) {
                    packageAbbr = pickedLclBl.getPackageType();
                }
                if (CommonFunctions.isNotNull(pickedLclBl.getCommodityType())) {
                    commdityDesc.append(pickedLclBl.getComDescrption()).append("\n");
                }

                
                if (count == 1) {
                StringBuilder blNumber = new StringBuilder();
                String shipperName = "";
                StringBuilder notyAddress = new StringBuilder();
                StringBuilder consigneeAddress = new StringBuilder();
                StringBuilder markDesc = new StringBuilder();
                String unLocationCode = "";
                String fileNo = "";
        
                List<Long> conoslidatelist = lclConsolidateDAO.getConsolidatesFiles(pickedLclBl.getFileId());//consolidate Dr
                conoslidatelist.add(pickedLclBl.getFileId());
                StringBuilder inbondAppend = new StringBuilder();//Inbond
                inbondAppend.append(reportUtils.appendInbond(conoslidatelist, inbondDAO));
                StringBuilder ncmAppend = new StringBuilder();//NCM
                ncmAppend.append(reportUtils.appendNcm(conoslidatelist, lcl3pRefNoDAO));
                StringBuilder hsAppend = new StringBuilder();//HS
                hsAppend.append(reportUtils.appendHsCode(conoslidatelist, lclBookingHsCodeDAO));
                StringBuilder aesAppend = new StringBuilder();//AES
                aesAppend.append(reportUtils.appendAes(conoslidatelist, lcl3pRefNoDAO));
                   
                if (CommonUtils.isNotEmpty(pickedLclBl.getMarksDesc())) {
                    markDesc.append(pickedLclBl.getMarksDesc().trim().toUpperCase()).append("\n");
                }
                if (CommonUtils.isNotEmpty(inbondAppend)) {
                    markDesc.append("INBOND:").append(inbondAppend.toString()).append("\n");
                }
                if (CommonUtils.isNotEmpty(ncmAppend)) {
                    markDesc.append(ncmAppend.toString());
                }
                if (CommonUtils.isNotEmpty(aesAppend)) {
                    markDesc.append(aesAppend.toString());
                }
                if (CommonUtils.isNotEmpty(hsAppend)) {
                    markDesc.append(hsAppend.toString());
                }
                    StringBuilder conoslidatelistFileNo = new StringBuilder();
                    lclbl = null != pickedLclBl.getFileId() ? new LCLBlDAO().getByProperty("lclFileNumber.id", pickedLclBl.getFileId()) : null;
                    new LCLBlDAO().getCurrentSession().evict(lclbl);
                    if (CommonFunctions.isNotNull(conoslidatelist) && !conoslidatelist.isEmpty()) {
                        for (int j = 0; j < conoslidatelist.size(); j++) {
                            Long obj = conoslidatelist.get(j);
                            fileNo = new LclFileNumberDAO().getFileNumberByFileId(String.valueOf(obj));
                            unLocationCode = lclbl.getPortOfOrigin().getUnLocationCode().substring(2);
                            conoslidatelistFileNo.append("").append(unLocationCode).append("-").append(fileNo).append("\n");
                            List<LclBookingPiece> bookingPieceList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", obj);
                            if (CommonFunctions.isNotNull(bookingPieceList) && !bookingPieceList.isEmpty()) {
                                for (LclBookingPiece bookingPiece : bookingPieceList) {
                                    List<LclBookingHazmat> bookingHazmatList = bookingPiece.getLclBookingHazmatList();
                                    for (int i = 0; i < bookingHazmatList.size(); i++) {
                                        LclBookingHazmat lclBookingHazmat = (LclBookingHazmat) bookingHazmatList.get(i);
                                        if (lclBookingHazmat.getPrintOnHouseBl()) {
                                            commdityDesc.append(reportUtils.concateHazmatDetails(lclBookingHazmat)).append("\n");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    String unLocationCode1 = lclbl.getPortOfDestination().getUnLocationCode();
                    String isPrintDollars = new LCLPortConfigurationDAO().getprintOFdollars(unLocationCode1);
                    String isCorrection = new LCLCorrectionDAO().isCorrection(lclbl.getFileNumberId());
                    if ("Y".equalsIgnoreCase(isPrintDollars)) {
                        List<LclBlAc> lclblAclist = null != pickedLclBl.getFileId() ? new LclBlAcDAO()
                                .findByProperty("lclFileNumber.id", pickedLclBl.getFileId()) : null;
                        if ("true".equalsIgnoreCase(isCorrection)) {
                            List<Object[]> corrected_Amount = new LCLCorrectionDAO().getCorrectionCharge(lclbl.getFileNumberId());
                            for (Object[] col : corrected_Amount) {
                                if ("A".equalsIgnoreCase(col[1].toString())) {
                                    collectAmount = collectAmount + Double.parseDouble(col[0].toString());
                                } else {
                                    prepaidAmount = prepaidAmount + Double.parseDouble(col[0].toString());
                                }
                            }
                            for (LclBlAc lclblAc : lclblAclist) {
                                if (lclblAc.getPrintOnBl()) {
                                    if ("A".equalsIgnoreCase(lclblAc.getArBillToParty())) {
                                        collectAmount = collectAmount + lclblAc.getAdjustmentAmount().doubleValue();
                                    } else {
                                        prepaidAmount = prepaidAmount + lclblAc.getAdjustmentAmount().doubleValue();
                                    }
                                }
                            }
                        } else {
                            for (LclBlAc lclblAc : lclblAclist) {
                                if (lclblAc.getPrintOnBl()) {
                                    if ("A".equalsIgnoreCase(lclblAc.getArBillToParty())) {
                                        collectAmount = collectAmount + (lclblAc.getArAmount().doubleValue() + lclblAc.getAdjustmentAmount().doubleValue());
                                    } else {
                                        prepaidAmount = prepaidAmount + (lclblAc.getArAmount().doubleValue() + lclblAc.getAdjustmentAmount().doubleValue());
                                    }
                                }
                            }
                        }
                    }
                    blNumber.append(new LCLBlDAO().getExportBlNumbering(lclbl.getFileNumberId().toString())).append("\n").append("Shipper:").append("\n");
                    if (null != lclbl.getShipContact() && CommonUtils.isNotEmpty(lclbl.getShipContact().getCompanyName())) {
                        shipperName = lclbl.getShipContact().getCompanyName().toUpperCase();
                    }
                    if (CommonFunctions.isNotNull(lclbl.getConsContact())) {
                        if (CommonUtils.isNotEmpty(lclbl.getConsContact().getCompanyName())) {
                            consigneeAddress.append(lclbl.getConsContact().getCompanyName()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbl.getConsContact().getAddress())) {
                            consigneeAddress.append(lclbl.getConsContact().getAddress());
                        }
                    }
                    if (CommonFunctions.isNotNull(lclbl.getNotyContact())) {
                        if (CommonFunctions.isNotNull(lclbl.getNotyContact().getCompanyName())) {
                            notyAddress.append(lclbl.getNotyContact().getCompanyName()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbl.getNotyContact().getAddress())) {
                            notyAddress.append(lclbl.getNotyContact().getAddress());
                        }
                    }

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    p = new Paragraph(8f, !isFileExist.equals(pickedLclBl.getFileId()) ? blNumber.toString() : "", courierBoldFont6);
                    if (!isFileExist.equals(pickedLclBl.getFileId())) {
                        p.add(new Chunk(shipperName, blackBoldFontSize6).setUnderline(0.1f, -2f));
                        p.setAlignment(Element.ALIGN_LEFT);
                    }
                    cell.setBorderWidthRight(0.06f);
                    cell.setBorderWidthLeft(0.06f);
                    cell.addElement(p);
                    table.addCell(cell);

                    table.addCell(createTextCell(!isFileExist.equals(pickedLclBl.getFileId()) ? conoslidatelistFileNo.toString() : "", 8f, 0.06f, 0, courierBoldFont6));
                    table.addCell(createTextCell(!isFileExist.equals(pickedLclBl.getFileId()) ? consigneeAddress.toString().toUpperCase() : "", 8f, 0.06f, 0, courierBoldFont6));
                    table.addCell(createTextCell(!isFileExist.equals(pickedLclBl.getFileId()) ? notyAddress.toString().toUpperCase() : "", 8f, 0.06f, 0, courierBoldFont6));
                    table.addCell(createTextCell(markDesc.toString().toUpperCase(), 8f, 0.06f, 0, courierBoldFont6));
                } else {
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    p = new Paragraph(8f, "", courierBoldFont6);
                    cell.setBorderWidthRight(0.06f);
                    cell.setBorderWidthLeft(0.06f);
                    cell.addElement(p);
                    table.addCell(cell);

                    table.addCell(createTextCell("", 8f, 0.06f, 0, courierBoldFont6));
                    table.addCell(createTextCell("", 8f, 0.06f, 0, courierBoldFont6));
                    table.addCell(createTextCell("", 8f, 0.06f, 0, courierBoldFont6));
                    table.addCell(createTextCell("", 8f, 0.06f, 0, courierBoldFont6));

                }
                table.addCell(createTextCell("" + pieceCount, 8f, 0.06f, 0, courierBoldFont6));
                table.addCell(createTextCell("" + packageAbbr, 8f, 0.06f, 0, courierBoldFont6));
                table.addCell(createTextCell(commdityDesc.toString().toUpperCase(), 8f, 0.06f, 0, courierBoldFont6));
                table.addCell(createTextCell(measureValue.toString(), 8f, 0.06f, 0, courierBoldFont6));
                table.addCell(createTextCell(weightValue.toString(), 8f, 0.06f, 0, courierBoldFont6));
                table.addCell(prepaidAmount == 0.00 ? createEmptyCell(0, 0.06f) : createTextCell(!isFileExist.equals(pickedLclBl.getFileId())
                        ? NumberUtils.convertToThreeDecimalhash(prepaidAmount) : "", 8f, 0.06f, 0, blackBoldFontSize6));
                table.addCell(collectAmount == 0.00 ? createEmptyCell(0, 0.06f) : createTextCell(!isFileExist.equals(pickedLclBl.getFileId())
                        ? NumberUtils.convertToThreeDecimalhash(collectAmount) : "", 8f, 0.06f, 0, blackBoldFontSize6));
                createEmptyRow();
                isFileExist = pickedLclBl.getFileId();
                if (count == pickedLclBl.getTotalFile()) {
                    count = 0;
                }
            }
        }
        return table;
    }

    private PdfPTable setTotalTable() throws Exception {
        table = new PdfPTable(12);
        table.setWidths(new float[]{1.1f, 0.6f, 2f, 2f, 1.3f, 0.3f, 0.3f, 2.5f, 0.6f, 0.6f, 0.4f, 0.4f});
        table.setWidthPercentage(99f);
        Paragraph p;
        createEmptyRow();
        createSymbol();

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        table.addCell(cell);
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(8f, "" + "TOTAL ", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(8f, "" + pieceTotal + "", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.addElement(p);
        table.addCell(cell);
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        cell = new PdfPCell();
        cell.setBorder(0);
        if (!metricEngmet.equalsIgnoreCase("M")) {
            p = new Paragraph(8f, "" + Math.round(CFT) + " CFT", blackBoldFontSize6);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.setBorderWidthRight(0.06f);
            cell.addElement(p);
        }
        p = new Paragraph(8f, "" + NumberUtils.convertToThreeDecimal(CBM) + " CBM", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        if (!metricEngmet.equalsIgnoreCase("M")) {
            p = new Paragraph(8f, "" + Math.round(LBS) + " LBS", blackBoldFontSize6);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.setBorderWidthRight(0.06f);
            cell.addElement(p);
        }
        p = new Paragraph(8f, "" + NumberUtils.convertToThreeDecimal(KGS) + " KGS", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));

        createSymbol();
        createSymbol();

        createEmptyRow();
        return table;
    }

    private PdfPTable createEndTable() {
        table = new PdfPTable(1);
        table.setWidthPercentage(99f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        cell.setPadding(1f);
        table.addCell(cell);
        return table;
    }

    private void createEmptyRow() {
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        table.addCell(cell);
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        table.addCell(cell);
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
        table.addCell(createTextCell("", 8f, 0.06f, 0, blackBoldFontSize6));
    }

    private void createSymbol() {
        Paragraph p = null;
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(8f, "--------------------", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.setPaddingRight(-10f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(8f, "------", blackBoldFontSize6);
        cell.setBorderWidthRight(0.06f);
        cell.setPaddingRight(-10f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(8f, "-------", blackBoldFontSize6);
        cell.setPaddingRight(-10f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(8f, "----------------------------------------------------", blackBoldFontSize6);
        cell.setPaddingRight(-10f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(8f, "-------------", blackBoldFontSize6);
        cell.setPaddingRight(-10f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(8f, "-------------", blackBoldFontSize6);
        cell.setPaddingRight(-10f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(8f, "--------", blackBoldFontSize6);
        cell.setPaddingRight(-10f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(8f, "--------", blackBoldFontSize6);
        cell.setPaddingRight(-10f);
        cell.setBorderWidthRight(0.06f);
        cell.addElement(p);
        table.addCell(cell);
    }
     public void setEmailReport(LclUnitSs lclUnitSs1) throws Exception {
       
        LclUtils utils = new LclUtils();
        this.lclUnitSs =lclUnitSs1;
       if (CommonFunctions.isNotNull(lclUnitSs1.getLclSsHeader())) {
            portOfDischarge = utils.getConcatenatedOriginByWithOutCode(lclUnitSs1.getLclSsHeader().getDestination());
            portOfLoading = utils.getConcatenatedOriginByWithOutCode(lclUnitSs1.getLclSsHeader().getOrigin());
        }
        unitNumber = lclUnitSs1.getLclUnit().getUnitNo();
        if (CommonFunctions.isNotNull(lclUnitSs1.getSUHeadingNote())) {
            sealNo = lclUnitSs1.getSUHeadingNote();
        }
        LclSsDetail ssDetail = lclUnitSs1.getLclSsHeader().getVesselSsDetail();
        if (null != ssDetail) {
            if(CommonUtils.isNotEmpty(Vessel)){
              Vessel.setLength(0);
            }
            if (CommonFunctions.isNotNull(ssDetail.getSpReferenceName())) {
               flag = new LclSsDetailDAO().getFlagFromElite(ssDetail.getSpReferenceName());
               Vessel.append(ssDetail.getSpReferenceName()).append(" .  ").append(ssDetail.getTransMode()).append("  ");
            }
            if (CommonFunctions.isNotNull(ssDetail.getSpReferenceNo())) {
                Vessel.append(ssDetail.getSpReferenceNo());
            }
            if (CommonFunctions.isNotNull(ssDetail.getStd())) {
              sailingDate = (DateUtils.formatStringDateToAppFormatMMM(ssDetail.getStd()));
            }
        }
        if (CommonFunctions.isNotNull(lclUnitSs1.getLclSsHeader().getScheduleNo())) {
            voyage = (lclUnitSs1.getLclSsHeader().getScheduleNo());
        }
        
    }

    public String createManifestEmailReport(String realPath, String outputFileName, ExportVoyageHblBatchForm batchForm)
            throws IOException, DocumentException, SQLException, Exception {

        List<Long> unitlist = null;
        unitlist = new ExportUnitQueryUtils().getUnitSSIdList(Long.parseLong(batchForm.getHeaderId()), Long.parseLong(batchForm.getUnitSSId()));
        for (Long unit : unitlist) {
            LclUnitSs lclUnitSs1 = new LclUnitSsDAO().findById(unit);
            this.lclUnitSs=lclUnitSs1;
            setEmailReport(lclUnitSs1);
            CFT = 0.00;
            CBM = 0.00;
            KGS = 0.00;
            LBS = 0.00;
            pieceTotal = 0;
            if (!documentOpenFlag) {
               document = new Document();
               document.setPageSize(PageSize.LEGAL.rotate());
               document.setMargins(26, 25, 15, 40);
               pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
               document.open();
               document.add(createHeader());
               document.add(createSubHeader());
               document.add(createUnitsTable());
             }
            if (!documentOpenFlag) {
                documentOpenFlag = true;
                document.add(createBodyTable());
                document.add(setTotalTable());
                document.add(createEndTable());
             } else {
                document.newPage();
                document.add(createHeader());
                document.add(createSubHeader());
                document.add(createUnitsTable());
                document.add(createBodyTable());
                document.add(setTotalTable());
                document.add(createEndTable());
            }
        }
        if (documentOpenFlag) {
            document.close();
        } else {
            outputFileName = null;
        }
        return outputFileName;
    }

}
