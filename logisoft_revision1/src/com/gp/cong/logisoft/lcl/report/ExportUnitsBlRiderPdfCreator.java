/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.itextpdf.text.BaseColor;
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
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Mei
 */
public class ExportUnitsBlRiderPdfCreator extends LclReportFormatMethods {

    private LclUnitSs lclUnitSs;
    private Integer totalPieceCount = 0;
    private Double totalvolumemeasure = 0.0;
    private Double totalweightmeasure = 0.0;

    public ExportUnitsBlRiderPdfCreator(LclUnitSs lclUnitSs) throws Exception {
        this.lclUnitSs = lclUnitSs;
    }

    class TableHeader extends PdfPageEventHelper {

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                document.add(createHeader());
            } catch (Exception ex) {
            }
        }
    }

    public void createPdf(String realPath, String outputFileName) throws IOException, DocumentException, SQLException, Exception {
        document = new Document();
        document.setPageSize(PageSize.A4.rotate());
        document.setMargins(5, 5, 8, 8);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        ExportUnitsBlRiderPdfCreator.TableHeader event = new ExportUnitsBlRiderPdfCreator.TableHeader();
        pdfWriter.setPageEvent(event);
        document.open();
        //document.add(createHeader());
        document.add(pickedBlContent());
        document.add(totalContent());
        document.close();
    }

    public PdfPTable createHeader() throws Exception {
        LclSsHeader lclSsHeader = lclUnitSs.getLclSsHeader();
        LclSSMasterBl lclSSMasterBl = null;
        StringBuilder consigneeDetails = new StringBuilder();
        StringBuilder origin = new StringBuilder();
        StringBuilder destination = new StringBuilder();
        String bkgRef = CommonUtils.isNotEmpty(lclUnitSs.getSpBookingNo()) ? lclUnitSs.getSpBookingNo() : "";
        if (CommonUtils.isNotEmpty(lclUnitSs.getSpBookingNo())) {
            lclSSMasterBl = new LclSSMasterBlDAO().findBkgNo(lclSsHeader.getId(), lclUnitSs.getSpBookingNo());
        }
        LclSsDetailDAO lclSSDetailsDAO = new LclSsDetailDAO();
        LclBookingPlanBean bookingPlan = new LclBookingPlanDAO().getRelay(lclSsHeader.getOrigin().getId(),
                lclSsHeader.getDestination().getId(), "N");
        String shipperAddress = "";
        if (bookingPlan != null && CommonUtils.isNotEmpty(bookingPlan.getPol_code())) {
            String[] shipperDetails = lclSSDetailsDAO.getAddress(bookingPlan.getPol_code());
            if (null != shipperDetails && shipperDetails[0] != null) {
                shipperAddress = shipperDetails[0] + "\n" + shipperDetails[1];
            }
        }
        if (null != lclSSMasterBl && null != lclSSMasterBl.getConsSsContactId()) {
            if (CommonUtils.isNotEmpty(lclSSMasterBl.getConsSsContactId().getCompanyName())) {
                consigneeDetails.append(lclSSMasterBl.getConsSsContactId().getCompanyName()).append("\n");
            }
            if (CommonUtils.isNotEmpty(lclSSMasterBl.getConsSsContactId().getAddress())) {
                consigneeDetails.append(lclSSMasterBl.getConsSsContactId().getAddress());
            }
        }

        if (CommonUtils.isNotEmpty(lclSsHeader.getOrigin().getUnLocationName())) {
            origin.append(lclSsHeader.getOrigin().getUnLocationName());
        }
        if (lclSsHeader.getOrigin() != null && lclSsHeader.getOrigin().getStateId() != null && lclSsHeader.getOrigin().getStateId().getCode() != null) {
            origin.append(", ").append(lclSsHeader.getOrigin().getStateId().getCode());
        }
        if (CommonUtils.isNotEmpty(lclSsHeader.getDestination().getCountryId().getCodedesc())) {
            destination.append(lclSsHeader.getDestination().getCountryId().getCodedesc());
        }
        if (CommonUtils.isNotEmpty(lclSsHeader.getDestination().getUnLocationName())) {
            destination.append(", ").append(lclSsHeader.getDestination().getUnLocationName());
        }

        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{0.1f, 5f, 0.1f});

        table.addCell(createTextCell("", headingBoldFont, 0, 0f, Element.ALIGN_LEFT));
        table.addCell(createTextCell("BILL OF LADING RIDER", arialBoldFont14Size, 0, 0f, Element.ALIGN_CENTER));
        table.addCell(createTextCell("", headingBoldFont, 0, 0f, Element.ALIGN_LEFT));

        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setBorder(0);


        PdfPTable subheaderTable = new PdfPTable(4);
        subheaderTable.setWidths(new float[]{0.1f, 2.5f, 3f, 0.1f});
        subheaderTable.setWidthPercentage(101f);

        subheaderTable.addCell(createTextCell("", headingBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable.addCell(createTextCell("SHIPPER:", blackBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable.addCell(createTextCell("CONSIGNEE:", blackBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable.addCell(createTextCell("", headingBoldFont, 0, 0f, Element.ALIGN_LEFT));

        subheaderTable.addCell(createTextCell("", headingBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable.addCell(createTextCell("" + shipperAddress, arialFontSize10Normal, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable.addCell(createTextCell("" + consigneeDetails, arialFontSize10Normal, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable.addCell(createTextCell("", headingBoldFont, 0, 0f, Element.ALIGN_LEFT));

        subheaderTable.addCell(makeCellNoBorderValue("", 4, 5f, 0f, blackNormalCourierFont12f));
        subheaderTable.addCell(makeCellNoBorderValue("", 4, 5f, 0f, blackNormalCourierFont12f));
        cell.addElement(subheaderTable);
        table.addCell(cell);


        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setBorder(0);
        PdfPTable subheaderTable1 = new PdfPTable(8);
        subheaderTable1.setWidths(new float[]{0.1f, 1f, 1.7f, 1.17f, 1f, 0.5f, 0.6f, 0.1f});
        subheaderTable1.setWidthPercentage(101f);

        subheaderTable1.addCell(createTextCell("", headingBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable1.addCell(createTextCell("PORT OF LOADING:", blackBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable1.addCell(createTextCell("" + origin.toString().toUpperCase(), arialFontSize10Normal, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable1.addCell(createTextCell("PORT OF DISCHARGE:", blackBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable1.addCell(makeCellNoBorderValue("" + destination.toString().toUpperCase(), 4, -1.5f, 0f, arialFontSize10Normal));
        String containerNo = "";
        String sealNo = "";
        if (CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getUnitNo())) {
            containerNo = lclUnitSs.getLclUnit().getUnitNo();
        }
        if (CommonFunctions.isNotNull(lclUnitSs.getSUHeadingNote())) {
            sealNo = lclUnitSs.getSUHeadingNote();
        }
        StringBuilder vesselDetails = new StringBuilder();
        LclSsDetail lclSsDetail = lclUnitSs.getLclSsHeader().getVesselSsDetail();
        if (lclSsDetail != null) {
            if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceName())) {
                vesselDetails.append(lclSsDetail.getSpReferenceName()).append(" .  ");
            }
            if (CommonFunctions.isNotNull(lclSsDetail.getTransMode())) {
                vesselDetails.append(lclSsDetail.getTransMode()).append("  ");
            }
            if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceNo())) {
                vesselDetails.append(lclSsDetail.getSpReferenceNo());
            }
        }
        subheaderTable1.addCell(createTextCell("", headingBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable1.addCell(createTextCell("VESSEL/VOYAGE:", blackBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable1.addCell(createTextCell("" + vesselDetails.toString().toUpperCase(), arialFontSize10Normal, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable1.addCell(createTextCell("CONTAINER NO:", blackBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable1.addCell(makeCellNoBorderValue("" + containerNo.toUpperCase(), 0, -1.5f, 0f, arialFontSize10Normal));
        subheaderTable1.addCell(makeCellNoBorderValue("SEAL NO:", 0, -1.5f, 0f, blackBoldFont));
        subheaderTable1.addCell(makeCellNoBorderValue("" + sealNo, 3, -1.5f, 0f, arialFontSize10Normal));


        subheaderTable1.addCell(createTextCell("", headingBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable1.addCell(createTextCell("BOOKING NO:", blackBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable1.addCell(createTextCell("" + bkgRef.toUpperCase(), arialFontSize10Normal, 0, 0f, Element.ALIGN_LEFT));
        String CompanyName = new SystemRulesDAO().getSystemRuleNameByRuleCode("CompanyCode").equalsIgnoreCase("02") ? "OTI" : "ECONOCARIBE";
        subheaderTable1.addCell(createTextCell(CompanyName + " REF#", blackBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subheaderTable1.addCell(makeCellNoBorderValue("" + lclSsHeader.getScheduleNo(), 4, -1.5f, 0f, arialFontSize10Normal));

        subheaderTable1.addCell(makeCellNoBorderValue("", 4, 5f, 0f, blackNormalCourierFont12f));
        subheaderTable1.addCell(makeCellNoBorderValue("", 4, 5f, 0f, blackNormalCourierFont12f));
        cell.addElement(subheaderTable1);
        table.addCell(cell);

        PdfPCell subCell = null;
        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setBorder(0);
        PdfPTable mainHeader = new PdfPTable(9);
        mainHeader.setWidths(new float[]{0.1f, 1f, 1f, 3.7f, 3f, 1f, 1f, 1f, 0.1f});
        mainHeader.setWidthPercentage(99f);
        Paragraph content = null;

        mainHeader.addCell(createTextCell("", headingBoldFont, 0, 0f, Element.ALIGN_LEFT));

        subCell = new PdfPCell();
        content = new Paragraph(12f, "QTY", blackBoldFont);
        content.setAlignment(Element.ALIGN_CENTER);
        subCell.setPaddingBottom(3f);
        cell.setPadding(0f);
        subCell.setBorderColor(new BaseColor(128, 00, 128));
        subCell.addElement(content);
        mainHeader.addCell(subCell);

        subCell = new PdfPCell();
        content = new Paragraph(12f, "TYPE", blackBoldFont);
        content.setAlignment(Element.ALIGN_CENTER);
        subCell.setBorderColor(new BaseColor(128, 00, 128));
        subCell.setPaddingBottom(3f);
        subCell.addElement(content);
        mainHeader.addCell(subCell);

        subCell = new PdfPCell();
        content = new Paragraph(12f, "COMMODITY", blackBoldFont);
        subCell.setBorderColor(new BaseColor(128, 00, 128));
        subCell.setPaddingBottom(3f);
        subCell.addElement(content);
        mainHeader.addCell(subCell);

        subCell = new PdfPCell();
        content = new Paragraph(12f, "MARKS & NUMBERS", blackBoldFont);
        subCell.setPaddingBottom(3f);
        subCell.setBorderColor(new BaseColor(128, 00, 128));
        subCell.addElement(content);
        mainHeader.addCell(subCell);

        subCell = new PdfPCell();
        content = new Paragraph(12f, "", blackBoldFont);
        subCell.setPaddingBottom(3f);
        subCell.setBorderColor(new BaseColor(128, 00, 128));
        subCell.addElement(content);
        mainHeader.addCell(subCell);

        subCell = new PdfPCell();
        content = new Paragraph(12f, "CBM", blackBoldFont);
        content.setAlignment(Element.ALIGN_CENTER);
        subCell.setPaddingBottom(3f);
        subCell.setBorderColor(new BaseColor(128, 00, 128));
        subCell.addElement(content);
        mainHeader.addCell(subCell);

        subCell = new PdfPCell();
        content = new Paragraph(12f, "WT.(KGS)", blackBoldFont);
        content.setAlignment(Element.ALIGN_CENTER);
        subCell.setPaddingBottom(3f);
        subCell.setBorderColor(new BaseColor(128, 00, 128));
        subCell.addElement(content);
        mainHeader.addCell(subCell);

        mainHeader.addCell(createTextCell("", headingBoldFont, 0, 0f, Element.ALIGN_LEFT));

        cell.addElement(mainHeader);
        table.addCell(cell);
        return table;
    }

    private Element pickedBlContent() throws DocumentException, Exception {
        Paragraph content = null;
        table = new PdfPTable(9);
        table.setWidths(new float[]{0.1f, 1f, 1f, 3.7f, 3f, 1f, 1f, 1f, 0.1f});
        table.setWidthPercentage(99f);
        LCLBlDAO blDAO = new LCLBlDAO();
        Lcl3pRefNoDAO lcl3pRefNoDAO = new Lcl3pRefNoDAO();
        LclConsolidateDAO lclConsolidateDAO = new LclConsolidateDAO();
        LclBookingHsCodeDAO lclBookingHsCodeDAO = new LclBookingHsCodeDAO();
        LclReportUtils reportUtils = new LclReportUtils();
        LclHazmatDAO hazmatDAO = new LclHazmatDAO();
        List<OceanManifestBean> pickedDrList = new LclBLPieceDAO().getPickedDRLclBlData(lclUnitSs.getId(), lclUnitSs.getLclSsHeader().getId(), true);
        if (pickedDrList != null && pickedDrList.size() > 0) {
            for (OceanManifestBean pickedLclBl : pickedDrList) {
                if (CommonUtils.isNotEmpty(pickedLclBl.getFileId())) {
                    String packageName = "";
                    int pieceCount = 0;
                    StringBuilder commdityDesc = new StringBuilder();
                    StringBuilder markDesc = new StringBuilder();
                    LclBl lclbl = blDAO.getByProperty("lclFileNumber.id", pickedLclBl.getFileId());
                    new LCLBlDAO().getCurrentSession().evict(lclbl);
                    markDesc.append("DR#").append(pickedLclBl.getFileNumber()).append("\n");
                    if (CommonFunctions.isNotNull(pickedLclBl.getMarksDesc())) {
                        markDesc.append(pickedLclBl.getMarksDesc().toUpperCase()).append("\n");
                    }
                    if (CommonFunctions.isNotNull(pickedLclBl.getPiece())) {
                        pieceCount = pickedLclBl.getPiece();
                        totalPieceCount += pieceCount;
                    }
                    if (CommonFunctions.isNotNull(pickedLclBl.getCbm())) {
                        totalvolumemeasure += pickedLclBl.getCbm();
                    }
                    if (CommonFunctions.isNotNull(pickedLclBl.getKgs())) {
                        totalweightmeasure += pickedLclBl.getKgs();
                    }
                    if (CommonFunctions.isNotNull(pickedLclBl.getCommodityType())) {
                        commdityDesc.append(pickedLclBl.getCommodityType().toUpperCase()).append("\n").append("\n");
                    }
                    if (CommonFunctions.isNotNull(pickedLclBl.getPackageName())) {
                        packageName = pickedLclBl.getPackageName();
                    }
                    List<Long> conoslidatelist = lclConsolidateDAO.getConsolidatesFiles(pickedLclBl.getFileId());
                    conoslidatelist.add(pickedLclBl.getFileId());

                    markDesc.append(reportUtils.appendAes(conoslidatelist, lcl3pRefNoDAO));//Aes List
                    markDesc.append(reportUtils.appendHsCode(conoslidatelist, lclBookingHsCodeDAO));//HS Code List

                    commdityDesc.append(reportUtils.appendHazmat(conoslidatelist, hazmatDAO));//Hazmat List

                    cell = new PdfPCell();
                    cell.setPadding(0f);
                    cell.setBorder(0);
                    table.addCell(cell);


                    cell = new PdfPCell();
                    cell.setPadding(0f);
                    cell.setBorder(0);
                    cell.setPaddingBottom(3f);
                    cell.setPaddingTop(3f);
                    cell.setPaddingLeft(4f);
                    cell.setBorderWidthLeft(0.6f);
                    cell.setBorderWidthRight(0.6f);
                    content = new Paragraph(11f, "" + pieceCount, arialFontSize10Normal);
                    content.setAlignment(Element.ALIGN_CENTER);
                    cell.setBorderColor(new BaseColor(128, 00, 128));
                    cell.addElement(content);
                    table.addCell(cell);


                    cell = new PdfPCell();
                    cell.setPadding(0f);
                    cell.setBorder(0);
                    cell.setPaddingBottom(3f);
                    cell.setPaddingTop(3f);
                    cell.setPaddingLeft(2f);
                    cell.setBorderWidthRight(0.6f);
                    content = new Paragraph(11f, "" + packageName, arialFontSize10Normal);
                    content.setAlignment(Element.ALIGN_CENTER);
                    cell.setBorderColor(new BaseColor(128, 00, 128));
                    cell.addElement(content);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setPadding(0f);
                    cell.setBorder(0);
                    cell.setPaddingBottom(3f);
                    cell.setPaddingTop(3f);
                    cell.setPaddingLeft(2f);
                    cell.setBorderWidthRight(0.6f);
                    content = new Paragraph(11f, "" + commdityDesc, arialFontSize10Normal);
                    cell.setBorderColor(new BaseColor(128, 00, 128));
                    cell.addElement(content);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setPadding(0f);
                    cell.setBorder(0);
                    cell.setPaddingBottom(3f);
                    cell.setPaddingTop(3f);
                    cell.setPaddingLeft(2f);
                    cell.setBorderWidthRight(0.6f);
                    content = new Paragraph(11f, "" + markDesc, arialFontSize10Normal);
                    cell.setBorderColor(new BaseColor(128, 00, 128));
                    cell.addElement(content);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setPadding(0f);
                    cell.setBorder(0);
                    cell.setPaddingBottom(3f);
                    cell.setPaddingTop(3f);
                    cell.setPaddingLeft(2f);
                    cell.setBorderWidthRight(0.6f);
                    content = new Paragraph(11f, "", arialFontSize10Normal);
                    cell.setBorderColor(new BaseColor(128, 00, 128));
                    cell.addElement(content);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setPadding(0f);
                    cell.setBorder(0);
                    cell.setPaddingBottom(3f);
                    cell.setPaddingTop(3f);
                    cell.setPaddingLeft(2f);
                    cell.setBorderWidthRight(0.6f);
                    if (CommonFunctions.isNotNull(pickedLclBl.getCbm())) {
                        content = new Paragraph(11f, "" + pickedLclBl.getCbm(), arialFontSize10Normal);
                    } else {
                        content = new Paragraph(11f, "", arialFontSize10Normal);
                    }
                    content.setAlignment(Element.ALIGN_CENTER);
                    cell.setBorderColor(new BaseColor(128, 00, 128));
                    cell.addElement(content);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setPadding(0f);
                    cell.setBorder(0);
                    cell.setPaddingBottom(3f);
                    cell.setPaddingTop(3f);
                    cell.setPaddingLeft(2f);
                    cell.setBorderWidthRight(0.6f);
                    if (CommonFunctions.isNotNull(pickedLclBl.getKgs())) {
                        content = new Paragraph(11f, "" + pickedLclBl.getKgs(), arialFontSize10Normal);
                    } else {
                        content = new Paragraph(11f, "", arialFontSize10Normal);
                    }
                    content.setAlignment(Element.ALIGN_CENTER);
                    cell.setBorderColor(new BaseColor(128, 00, 128));
                    cell.addElement(content);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setPadding(0f);
                    cell.setPaddingBottom(3f);
                    cell.setBorder(0);
                    table.addCell(cell);

                }
            }
        }
        return table;
    }

    private Element totalContent() throws DocumentException, Exception {
        Paragraph content = null;
        table = new PdfPTable(9);
        table.setWidths(new float[]{0.1f, 1f, 1.7f, 3f, 3f, 1f, 1f, 1f, 0.1f});
        table.setWidthPercentage(99f);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        table.addCell(cell);


        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);


        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBackgroundColor(new BaseColor(77, 77, 77));
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBackgroundColor(new BaseColor(77, 77, 77));
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBackgroundColor(new BaseColor(77, 77, 77));
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        table.addCell(cell);







        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        table.addCell(cell);


        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setPaddingLeft(2f);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        content = new Paragraph(11f, "" + totalPieceCount, blackBoldFont);
        content.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(new BaseColor(128, 00, 128));
        cell.addElement(content);
        table.addCell(cell);


        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setPaddingTop(3f);
        cell.setBorder(0);
        cell.setBackgroundColor(new BaseColor(77, 77, 77));
        content = new Paragraph(11f, "", arialFontSize10Normal);
        cell.setBorderColor(new BaseColor(128, 00, 128));
        cell.addElement(content);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setPaddingLeft(2f);
        cell.setBorder(0);
        cell.setBackgroundColor(new BaseColor(77, 77, 77));
        content = new Paragraph(14f, "", arialBoldFont14Size);
        content.add(new Chunk("TOTALS").setBackground(BaseColor.WHITE));
        cell.setBorderColor(new BaseColor(128, 00, 128));
        cell.addElement(content);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setPaddingLeft(2f);
        cell.setBorder(0);
        cell.setBackgroundColor(new BaseColor(77, 77, 77));
        content = new Paragraph(11f, "", arialFontSize10Normal);
        cell.setBorderColor(new BaseColor(128, 00, 128));
        cell.addElement(content);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setPaddingLeft(2f);
        cell.setBorder(0);
        cell.setBackgroundColor(new BaseColor(77, 77, 77));
        content = new Paragraph(11f, "", arialFontSize10Normal);
        cell.setBorderColor(new BaseColor(128, 00, 128));
        cell.addElement(content);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setPaddingLeft(2f);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        content = new Paragraph(11f, "" + NumberUtils.convertToThreeDecimal(totalvolumemeasure), blackBoldFont);
        content.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(new BaseColor(128, 00, 128));
        cell.addElement(content);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setPaddingLeft(2f);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        content = new Paragraph(11f, "" + NumberUtils.convertToThreeDecimal(totalweightmeasure), blackBoldFont);
        content.setAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(new BaseColor(128, 00, 128));
        cell.addElement(content);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        table.addCell(cell);







        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        table.addCell(cell);


        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);


        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setPaddingBottom(5f);
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBackgroundColor(new BaseColor(77, 77, 77));
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBackgroundColor(new BaseColor(77, 77, 77));
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBackgroundColor(new BaseColor(77, 77, 77));
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBackgroundColor(new BaseColor(77, 77, 77));
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderColor(new BaseColor(128, 00, 128));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        table.addCell(cell);

        return table;
    }
}
