/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.ExportUnitQueryUtils;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.lcl.ExportVoyageHblBatchForm;
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
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Mei
 */
public class ExportUnitsLargeManifestPdf extends LclReportFormatMethods {

    private LclUnitSs lclUnitSs;
    StringBuilder unitsNumber = new StringBuilder();
    private String Voyage;
    StringBuilder sailDate = new StringBuilder();
    private PdfWriter PdfWriter;
    private StringBuilder destination = new StringBuilder();
    StringBuilder origin = new StringBuilder();
    private String sealNo = "";
    private StringBuilder Vessel = new StringBuilder();
    private String flagValue = "";
    private Integer totalPieceCount = 0;
    private Double totalvolumemeasure = 0.0;
    private Double totalweightmeasure = 0.0;
    private Double totalweightMetric = 0.0;
    private Double totalvolumeMetric = 0.0;
    private boolean documentOpenFlag = false;

    public ExportUnitsLargeManifestPdf(LclUnitSs lclUnitSs) throws Exception {
        this.lclUnitSs = lclUnitSs;
        if(null != lclUnitSs){
        if (CommonFunctions.isNotNull(lclUnitSs) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader())) {
            if (CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getOrigin().getUnLocationName())) {
                origin.append(lclUnitSs.getLclSsHeader().getOrigin().getUnLocationName());
            }
            if (lclUnitSs != null && lclUnitSs.getLclSsHeader() != null && lclUnitSs.getLclSsHeader().getOrigin() != null && lclUnitSs.getLclSsHeader().getOrigin().getStateId() != null && lclUnitSs.getLclSsHeader().getOrigin().getStateId().getCode() != null) {
                origin.append(", ").append(lclUnitSs.getLclSsHeader().getOrigin().getStateId().getCode());
            }
            if (CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getDestination().getCountryId().getCodedesc())) {
                destination.append(lclUnitSs.getLclSsHeader().getDestination().getCountryId().getCodedesc());
            }
            if (CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName())) {
                destination.append(", ").append(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName());
            }
        }
        if (CommonFunctions.isNotNull(lclUnitSs)) {
            if (CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getUnitNo())) {
                unitsNumber.append(lclUnitSs.getLclUnit().getUnitNo());
            }
        }
        if (CommonFunctions.isNotNull(lclUnitSs)) {
            if (CommonFunctions.isNotNull(lclUnitSs.getSUHeadingNote())) {
                sealNo = lclUnitSs.getSUHeadingNote();
            }
        }
        LclSsDetail lclSsDetail = lclUnitSs.getLclSsHeader().getVesselSsDetail();
        if (lclSsDetail != null) {
            if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceName())) {
                flagValue = new LclSsDetailDAO().getFlagFromElite(lclSsDetail.getSpReferenceName());
                Vessel.append(lclSsDetail.getSpReferenceName()).append(" .  ");
            }
            if (CommonFunctions.isNotNull(lclSsDetail.getTransMode())) {
                Vessel.append(lclSsDetail.getTransMode()).append("  ");
            }
            if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceNo())) {
                Vessel.append(lclSsDetail.getSpReferenceNo());
            }
        }
        if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getScheduleNo())) {
            Voyage = (lclUnitSs.getLclSsHeader().getScheduleNo());
        }
    }
    }

    class TableHeader extends PdfPageEventHelper {

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                document.add(createHeader());
                document.add(createVoyHeader());
            } catch (Exception ex) {
            }
        }
    }

    public void createPdf(String realPath, String outputFileName) throws IOException, DocumentException, SQLException, Exception {
        document = new Document();
        document.setPageSize(PageSize.A4.rotate());
        document.setMargins(5, 5, 8, 8);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        ExportUnitsLargeManifestPdf.TableHeader event = new ExportUnitsLargeManifestPdf.TableHeader();
        pdfWriter.setPageEvent(event);
        document.open();
        // document.add(createHeader());
        // document.add(createVoyHeader());
        document.add(createUnitHeader());
        document.add(totalCommValues());
        document.close();
    }

    public PdfPTable createHeader() throws Exception {
        table = new PdfPTable(3);
        table.setWidthPercentage(99f);
        table.setWidths(new float[]{2f, 3f, 2f});
        table.addCell(createEmptyCell(0, 0));
        String companyName = new SystemRulesDAO().getSystemRulesByCode("CompanyName");
        table.addCell(createTextCell(companyName.toUpperCase() + "", 0, blackNormalCourierFont12f));

        table.addCell(createEmptyCell(0, 0));
        String companyAddress = new SystemRulesDAO().getSystemRulesByCode("CompanyAddress");
        table.addCell(createTextCell("CARGO \n MANIFEST", 0, blackNormalCourierFont12f));
        table.addCell(createTextCell(companyAddress.toUpperCase() + "", 0, blackNormalCourierFont12f));
        table.addCell(createTextCell("CARGO \n MANIFEST", 0, blackNormalCourierFont12f));
        table.addCell(createTextCell("", 0, blackNormalCourierFont12f));
        table.addCell(createTextCell("", 0, blackNormalCourierFont12f));
        table.addCell(createTextCell("", 0, blackNormalCourierFont12f));
        return table;
    }

    private void setDottedLineByFirst(PdfPTable table) {
        table.addCell(createEmptyCell(0, 0));
        table.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        table.addCell(createTextCell("- - - - - - - - - - - - - - - - - ", 0, -2f, blackBoldFontSize10));
        table.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        table.addCell(createTextCell("- - - - - - - - - - - - - - - - - - - - - - - - - - ", 0, -2f, blackBoldFontSize10));
        table.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        table.addCell(createEmptyCell(0, 0));
    }

    private Element createVoyHeader() throws DocumentException, Exception {
        table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{4.2f, 0.1f, 2f, 0.1f, 3f, 0.1f, 0.28f});

        setDottedLineByFirst(table);

        table.addCell(createEmptyCell(0, 0));
        table.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        table.addCell(createTextCell("PORT OF LOADING", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
        table.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        table.addCell(createTextCell("PORT OF DISCHARGE", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
        table.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        table.addCell(createEmptyCell(0, 0));

        setDottedLineByFirst(table);

        table.addCell(createEmptyCell(0, 0));
        table.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        table.addCell(createTextCell("" + origin, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
        table.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        table.addCell(createTextCell("" + destination, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
        table.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        table.addCell(createEmptyCell(0, 0));

        cell = new PdfPCell();
        cell.setColspan(7);
        cell.setBorder(0);
        PdfPTable subheaderTable = new PdfPTable(16);
        subheaderTable.setWidths(new float[]{0.1f, 2f, 0.1f, 1.2f, 0.1f, 2.5f, 0.1f, 1.2f, 0.1f, 1.5f, 0.1f, 1.2f, 0.1f, 0.9f, 0.1f, 0.3f});
        subheaderTable.setWidthPercentage(100f);

        setDottedLinesSecond(subheaderTable);
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("TRAILER NO", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_CENTER));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("SEAL", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_CENTER));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("SAILING ON VESSEL", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_CENTER));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("FLAG", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_CENTER));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("DATE", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_CENTER));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("VOYAGE", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_CENTER));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("PAGE #", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_CENTER));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("", 0, -2f, blackBoldFontSize10));

        setDottedLinesSecond(subheaderTable);
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("" + unitsNumber, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("" + sealNo, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("" + Vessel.toString(), blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("" + flagValue, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("" + DateUtils.formatDate(new Date(), "dd-MMM-yyyy"), blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("" + Voyage, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("" + pdfWriter.getCurrentPageNumber(), blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));
        subheaderTable.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTable.addCell(createTextCell("", 0, -2f, blackBoldFontSize10));
        cell.addElement(subheaderTable);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(7);
        cell.setBorder(0);
        PdfPTable subheaderTables = new PdfPTable(10);
        subheaderTables.setWidths(new float[]{0.1f, 3f, 0.1f, 1.5f, 0.1f, 0.5f, 0.1f, 0.5f, 0.1f, 3.3f});
        subheaderTables.setWidthPercentage(100f);

        setDottedLineThird(subheaderTables);
        subheaderTables.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTables.addCell(createTextCell("B/L, SHIPPER, CONSIGNEE, NOTIFY", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
        subheaderTables.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTables.addCell(createTextCell("MARKS", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_CENTER));
        subheaderTables.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTables.addCell(createTextCell("QTY.", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_CENTER));
        subheaderTables.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTables.addCell(createTextCell("TYP", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_CENTER));
        subheaderTables.addCell(createTextCell("|", 0, -2f, blackNormalCourierFont12f));
        subheaderTables.addCell(createTextCell("COMMODITY, MEASURE, WEIGHT", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_CENTER));
        setDottedLineThird(subheaderTables);

        cell.addElement(subheaderTables);
        table.addCell(cell);

        return table;
    }

    private void setDottedLinesSecond(PdfPTable subheaderTable) {
        subheaderTable.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("- - - - - - - - - - - - - - ", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("- - - - - - - - ", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("- - - - - - - - - - - - - - - - - - ", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("- - - - - - - -", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("- - - - - - - - - -", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("- - - - - - - -", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("- - - - - -", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        subheaderTable.addCell(createTextCell("- -", 0, -2f, blackBoldFontSize10));
    }

    private void setDottedLineThird(PdfPTable subheaderTables) {
        subheaderTables.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        subheaderTables.addCell(createTextCell("- - - - - - - - - - - - - - - - - - - - - - - - - - -", 0, -2f, blackBoldFontSize10));
        subheaderTables.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        subheaderTables.addCell(createTextCell("- - - - - - - - - - - - -", 0, -2f, blackBoldFontSize10));
        subheaderTables.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        subheaderTables.addCell(createTextCell("- - - -", 0, -2f, blackBoldFontSize10));
        subheaderTables.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        subheaderTables.addCell(createTextCell("- - - -", 0, -2f, blackBoldFontSize10));
        subheaderTables.addCell(createTextCell("+", 0, -2f, blackBoldFontSize10));
        subheaderTables.addCell(createTextCell("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -", 0, -2f, blackBoldFontSize10));
    }

    private Element createUnitHeader() throws DocumentException, Exception {
        table = new PdfPTable(12);
        table.setWidthPercentage(100f);
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        LclConsolidateDAO lclConsolidateDAO = new LclConsolidateDAO();
        LclBookingHsCodeDAO lclBookingHsCodeDAO = new LclBookingHsCodeDAO();
        LclInbondsDAO inbondDAO = new LclInbondsDAO();
        LclReportUtils reportUtils = new LclReportUtils();
        LclBl lclbl = null;
        table.setWidths(new float[]{0.3f, 2.8f, 0.1f, 1.5f, 0.1f, 0.5f, 0.1f, 0.5f, 0.1f, 1.3f, 1.4f, 0.5f});
        List<OceanManifestBean> pickedDrList = new LclBLPieceDAO().getPickedDRList(lclUnitSs.getId(), lclUnitSs.getLclSsHeader().getId());
        Paragraph p = null;
        if (pickedDrList != null && pickedDrList.size() > 0) {
            int count = 0;
            for (OceanManifestBean pickedLclBl : pickedDrList) {
                count++;
                String packageAbbr = "";
                String cft = "";
                String cbm = "";
                String kgs = "";
                String lbs = "";
                int pieceCount = 0;

                if (CommonFunctions.isNotNull(pickedLclBl.getPiece())) {
                    pieceCount = pickedLclBl.getPiece();
                    totalPieceCount += pieceCount;
                }
                if (CommonFunctions.isNotNull(pickedLclBl.getCft())) {
                    cft = (Math.round(pickedLclBl.getCft())) + " CFT";
                    totalvolumeMetric += pickedLclBl.getCft();
                }
                if (CommonFunctions.isNotNull(pickedLclBl.getCbm())) {
                    cbm = pickedLclBl.getCbm() + " CBM";
                    totalvolumemeasure += pickedLclBl.getCbm();
                }
                if (CommonFunctions.isNotNull(pickedLclBl.getLbs())) {
                    lbs = (Math.round(pickedLclBl.getLbs())) + " LBS";
                    totalweightMetric += pickedLclBl.getLbs();
                }
                if (CommonFunctions.isNotNull(pickedLclBl.getKgs())) {
                    kgs = pickedLclBl.getKgs() + " KGS";
                    totalweightmeasure += pickedLclBl.getKgs();
                }
                if (CommonFunctions.isNotNull(pickedLclBl.getPackageType())) {
                    packageAbbr = pickedLclBl.getPackageType();
                }

                StringBuilder markDesc = new StringBuilder();
                if (count == 1) {
                    lclbl = null != pickedLclBl.getFileId() ? new LCLBlDAO().getByProperty("lclFileNumber.id", pickedLclBl.getFileId()) : null;
                    new LCLBlDAO().getCurrentSession().evict(lclbl);

                    StringBuilder blNumber = new StringBuilder();
                    String unLocationCode = "";
                    String unPolPodId = "";

                    UnLocation unlocation = new UnLocationDAO().findById(pickedLclBl.getUnLocId() != null ? pickedLclBl.getUnLocId().intValue() : null);
                    unLocationCode = unlocation != null ? unlocation.getUnLocationCode().substring(2, unlocation.getUnLocationCode().length()) : "";
                    UnLocation unLocationPod = new UnLocationDAO().findById(pickedLclBl.getPodId() != null ? pickedLclBl.getPodId().intValue() : null);

                    if (unlocation.getUnLocationCode() != null && unLocationPod != null) {
                        unPolPodId = unlocation != null ? (unLocationCode) + "-" + unLocationPod.getUnLocationCode() : "";
                    }
                    if (CommonFunctions.isNotNull(unPolPodId)) {
                        blNumber.append(new LCLBlDAO().getExportBlNumbering(lclbl.getFileNumberId().toString()));
                    }
                    //markDesc
                    if (CommonFunctions.isNotNull(pickedLclBl.getMarksDesc())) {
                        markDesc.append(pickedLclBl.getMarksDesc().toUpperCase()).append("\n");
                    }
                    List<Long> conoslidatelist = lclConsolidateDAO.getConsolidatesFiles(pickedLclBl.getFileId());
                    conoslidatelist.add(pickedLclBl.getFileId());
                    markDesc.append(reportUtils.appendAes(conoslidatelist, lcl3pRefNoDAO));//AES List
                    markDesc.append(reportUtils.appendHsCode(conoslidatelist, lclBookingHsCodeDAO));//HS Code
                    markDesc.append(reportUtils.appendInbond(conoslidatelist, inbondDAO));//Inbond List
                    markDesc.append(reportUtils.appendNcm(conoslidatelist, lcl3pRefNoDAO));//Inbond List
//                    markDesc.append(reportUtils.appendHazmat(conoslidatelist, hazmatDAO));//Hazmat List
                    table.addCell(createTextCell("BL:" + blNumber.toString(), blackNormalCourierFont12f, 0, 2, Element.ALIGN_LEFT));
                    table.addCell(makeCellNoBorderFont("", 0f, 2, blackNormalCourierFont12f));

                } else {
                    table.addCell(createEmptyCell(0));
                    table.addCell(createEmptyCell(0));
                    table.addCell(createTextCell(pickedLclBl.getMarksDesc() != null ? pickedLclBl.getMarksDesc().toUpperCase() : "", blackNormalCourierFont12f, 0, 2, Element.ALIGN_LEFT));
                }
                table.addCell(makeCellNoBorderFont("", 0f, 4, blackNormalCourierFont12f));
                cell = new PdfPCell();
                cell.setColspan(4);
                p = new Paragraph(9.5f, pickedLclBl.getComDescrption().toUpperCase(), blackNormalCourierFont12f);
                cell.addElement(p);
                cell.setBorder(0);
                table.addCell(cell);

                //2nd cell
                if (count == 1) {
                    StringBuilder shipperAddr = new StringBuilder();
                    StringBuilder consigneeAddress = new StringBuilder();
                    StringBuilder notyAddress = new StringBuilder();
                    // Shipper LclContact
                    if (CommonFunctions.isNotNull(lclbl.getShipContact())) {
                        if (CommonFunctions.isNotNull(lclbl.getShipContact().getCompanyName())) {
                            shipperAddr.append(lclbl.getShipContact().getCompanyName()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbl.getShipContact().getAddress())) {
                            shipperAddr.append(lclbl.getShipContact().getAddress());
                        }
                    }
                    //consignee LclContact
                    if (CommonFunctions.isNotNull(lclbl.getConsContact())) {
                        if (CommonFunctions.isNotNull(lclbl.getConsContact().getCompanyName())) {
                            consigneeAddress.append(lclbl.getConsContact().getCompanyName()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbl.getConsContact().getAddress())) {
                            consigneeAddress.append(lclbl.getConsContact().getAddress());
                        }
                    }
                    // Notify LclContact
                    if (CommonFunctions.isNotNull(lclbl.getNotyContact())) {
                        if (CommonFunctions.isNotNull(lclbl.getNotyContact().getCompanyName())) {
                            notyAddress.append(lclbl.getNotyContact().getCompanyName()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbl.getNotyContact().getAddress())) {
                            notyAddress.append(lclbl.getNotyContact().getAddress());
                        }
                    }

                    table.addCell(makeCellNoBorderFont("SH:", -2f, 0, blackNormalCourierFont12f));
                    table.addCell(createTextCell(shipperAddr.toString().toUpperCase(), blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
                    table.addCell(createTextCell(markDesc.toString(), blackNormalCourierFont12f, 0, 2, Element.ALIGN_LEFT));
                    table.addCell(createEmptyCell(0));

                    table.addCell(createTextCell("" + pieceCount, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));
                    table.addCell(createEmptyCell(0));
                    table.addCell(createTextCell(packageAbbr, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
                    table.addCell(makeCellNoBorderFont("", 0f, 4, blackNormalCourierFont12f));

                    if (CommonUtils.isNotEmpty(consigneeAddress.toString())) {
                        table.addCell(makeCellNoBorderFont("CO:", 0f, 0, blackNormalCourierFont12f));
                        table.addCell(createTextCell(consigneeAddress.toString().toUpperCase(), blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
                        table.addCell(makeCellNoBorderFont("", 0f, 10, blackNormalCourierFont12f));
                    }
                    if (CommonUtils.isNotEmpty(notyAddress.toString())) {
                        table.addCell(makeCellNoBorderFont("NO:", 0f, 0, blackNormalCourierFont12f));
                        table.addCell(createTextCell(notyAddress.toString().toUpperCase(), blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
                        table.addCell(makeCellNoBorderFont("", 0f, 10, blackNormalCourierFont12f));
                    }
                } else {
                    table.addCell(makeCellNoBorderFont("", 0f, 5, blackNormalCourierFont12f));
                    table.addCell(createTextCell("" + pieceCount, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));
                    table.addCell(createEmptyCell(0));
                    table.addCell(createTextCell(packageAbbr, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_LEFT));
                    table.addCell(makeCellNoBorderFont("", 0f, 4, blackNormalCourierFont12f));
                }

                table.addCell(makeCellNoBorderFont("", 0f, 9, blackNormalCourierFont12f));
                table.addCell(createTextCell("" + cft, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));
                table.addCell(createTextCell("" + lbs, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));
                table.addCell(createTextCell("", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));

                table.addCell(makeCellNoBorderFont("", 0f, 9, blackNormalCourierFont12f));
                table.addCell(createTextCell("" + cbm, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));
                table.addCell(createTextCell("" + kgs, blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));
                table.addCell(createTextCell("", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));
                if (count == pickedLclBl.getTotalFile()) {
                    table.addCell(makeCellNoBorderFont("==================================================================================================================", 0f, 12, blackNormalCourierFont12f));
                    count = 0;
                } else {
                    table.addCell(makeCellNoBorderFont("", 0f, 12, blackNormalCourierFont12f));
                }
            }
        }
        return table;
    }

    private Element totalCommValues() throws DocumentException, Exception {
        table = new PdfPTable(12);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{0.3f, 2.8f, 0.1f, 1.5f, 0.1f, 0.5f, 0.1f, 0.5f, 0.1f, 1.3f, 1.4f, 0.5f});

//        table.addCell(makeCellNoBorderFont("==================================================================================================================", 0f, 12, blackNormalCourierFont12f));
        table.addCell(makeCellNoBorderFont("", 0f, 3, blackNormalCourierFont12f));
        table.addCell(createTextCell("----------------------------------------------------------", blackNormalCourierFont12f, 0, 8, Element.ALIGN_RIGHT));
        table.addCell(createTextCell("", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));

        table.addCell(makeCellNoBorderFont("", 0f, 5, blackNormalCourierFont12f));
        table.addCell(createTextCell("" + totalPieceCount, blackNormalCourierFont12f, 0, 0, Element.ALIGN_RIGHT));
        table.addCell(createTextCell("", blackNormalCourierFont12f, 0, 3, Element.ALIGN_RIGHT));
        table.addCell(createTextCell("" + Math.round(totalvolumeMetric) + " CFT", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));
        table.addCell(createTextCell("" + Math.round(totalweightMetric) + " LBS", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));
        table.addCell(createTextCell("", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));

        table.addCell(makeCellNoBorderFont("", 0f, 9, blackNormalCourierFont12f));
        table.addCell(createTextCell("" + NumberUtils.convertToThreeDecimal(totalvolumemeasure) + " CBM", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));
        table.addCell(createTextCell("" + NumberUtils.convertToThreeDecimal(totalweightmeasure) + " KGS", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));
        table.addCell(createTextCell("", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));

        table.addCell(makeCellNoBorderFont("", 0f, 3, blackNormalCourierFont12f));
        table.addCell(createTextCell("----------------------------------------------------------", blackNormalCourierFont12f, 0, 8, Element.ALIGN_RIGHT));
        table.addCell(createTextCell("", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));

        table.addCell(makeCellNoBorderFont("", 0f, 3, blackNormalCourierFont12f));
        table.addCell(createTextCell("----------------------------------------------------------", blackNormalCourierFont12f, 0, 8, Element.ALIGN_RIGHT));
        table.addCell(createTextCell("", blackNormalCourierFont12f, 0, 0f, Element.ALIGN_RIGHT));
        return table;
    }
    public void setEmailReport(LclUnitSs lclUnitSs1) throws Exception {
       if (CommonFunctions.isNotNull(lclUnitSs1) && CommonFunctions.isNotNull(lclUnitSs1.getLclSsHeader())) {
               
                if (CommonUtils.isNotEmpty(lclUnitSs1.getLclSsHeader().getOrigin().getUnLocationName())) {
                    if (CommonUtils.isNotEmpty(origin)) {
                        origin.setLength(0);
                    }
                   origin.append(lclUnitSs1.getLclSsHeader().getOrigin().getUnLocationName());
                }
                if (lclUnitSs1 != null && lclUnitSs1.getLclSsHeader() != null && lclUnitSs1.getLclSsHeader().getOrigin() != null && lclUnitSs1.getLclSsHeader().getOrigin().getStateId() != null && lclUnitSs1.getLclSsHeader().getOrigin().getStateId().getCode() != null) {
                   origin.append(", ").append(lclUnitSs1.getLclSsHeader().getOrigin().getStateId().getCode());
                }
                
                if (CommonUtils.isNotEmpty(lclUnitSs1.getLclSsHeader().getDestination().getCountryId().getCodedesc())) {
                    
                    if (CommonUtils.isNotEmpty(destination)) {
                        destination.setLength(0);
                    }
                    destination.append(lclUnitSs1.getLclSsHeader().getDestination().getCountryId().getCodedesc());
                }
                if (CommonUtils.isNotEmpty(lclUnitSs1.getLclSsHeader().getDestination().getUnLocationName())) {
                    destination.append(", ").append(lclUnitSs1.getLclSsHeader().getDestination().getUnLocationName());
                }
            }
      
            if (CommonFunctions.isNotNull(lclUnitSs1)) {
                if (CommonFunctions.isNotNull(lclUnitSs1.getLclUnit().getUnitNo())) {
                     
                    if (CommonUtils.isNotEmpty(unitsNumber)) {
                        unitsNumber.setLength(0);
                    }
                    unitsNumber.append(lclUnitSs1.getLclUnit().getUnitNo());
                }
            }
            if (CommonFunctions.isNotNull(lclUnitSs1)) {
                if (CommonFunctions.isNotNull(lclUnitSs1.getSUHeadingNote())) {
                    sealNo = lclUnitSs1.getSUHeadingNote();
                }
            }
            
            LclSsDetail lclSsDetail = lclUnitSs1.getLclSsHeader().getVesselSsDetail();
            if (lclSsDetail != null) {
                if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceName())) {
                    flagValue = new LclSsDetailDAO().getFlagFromElite(lclSsDetail.getSpReferenceName());
                    
                    if (CommonUtils.isNotEmpty(Vessel)) {
                        Vessel.setLength(0);
                    }
                    Vessel.append(lclSsDetail.getSpReferenceName()).append(" .  ");
                }
                if (CommonFunctions.isNotNull(lclSsDetail.getTransMode())) {
                   Vessel.append(lclSsDetail.getTransMode()).append("  ");
                }
                if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceNo())) {
                    Vessel.append(lclSsDetail.getSpReferenceNo());
                }
            }
          
            if (CommonFunctions.isNotNull(lclUnitSs1.getLclSsHeader().getScheduleNo())) {
                   Voyage = (lclUnitSs1.getLclSsHeader().getScheduleNo());
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
            totalPieceCount = 0;
            totalvolumemeasure = 0.0;
            totalweightmeasure = 0.0;
            totalweightMetric = 0.0;
            totalvolumeMetric = 0.0;
           if (!documentOpenFlag) {
                document = new Document();
                document.setPageSize(PageSize.A4.rotate());
                document.setMargins(5, 5, 8, 8);
                pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
                document.open();
                document.add(createHeader());
                document.add(createVoyHeader());

            }
            if (!documentOpenFlag) {
                documentOpenFlag = true;
                document.add(createUnitHeader());
                document.add(totalCommValues());

            } else {
                document.newPage();
                document.add(createHeader());
                document.add(createVoyHeader());
                document.add(createUnitHeader());
                document.add(totalCommValues());
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
