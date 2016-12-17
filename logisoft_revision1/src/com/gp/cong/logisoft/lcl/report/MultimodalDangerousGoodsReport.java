/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Shanmugam
 */
public class MultimodalDangerousGoodsReport extends LclReportFormatMethods {

    private final LclUnitSs lclUnitSs;
    private final String outputFileName;
    private final HttpServletRequest request;

    public MultimodalDangerousGoodsReport(LclUnitSs lclUnitSs, String outputFileName, HttpServletRequest request) throws Exception {
        this.lclUnitSs = lclUnitSs;
        this.outputFileName = outputFileName;
        this.request = request;
    }

    class TableHeader extends PdfPageEventHelper {

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
        }

    }

    public void createPdf() throws IOException, DocumentException, SQLException, Exception {
        document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(18, 18, 25, 25);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        MultimodalDangerousGoodsReport.TableHeader event = new MultimodalDangerousGoodsReport.TableHeader();
        pdfWriter.setPageEvent(event);
        document.open();
        document.add(createHeader());
        document.close();
    }

    public PdfPTable createHeader() throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        LclSsHeader lclSsHeader = lclUnitSs.getLclSsHeader();
//        LclSSMasterBl lclSSMasterBl = null;
        StringBuilder consigneeDetails = new StringBuilder();
        StringBuilder origin = new StringBuilder();
        StringBuilder destination = new StringBuilder();
        StringBuilder departurePier = new StringBuilder();
        StringBuilder arrivalPier = new StringBuilder();
        String bkgRef = CommonUtils.isNotEmpty(lclUnitSs.getSpBookingNo()) ? lclUnitSs.getSpBookingNo() : "";
//        if (CommonUtils.isNotEmpty(lclUnitSs.getSpBookingNo())) {
//            lclSSMasterBl = new LclSSMasterBlDAO().findBkgNo(lclSsHeader.getId(), lclUnitSs.getSpBookingNo());
//        }
        LclSsDetailDAO lclSSDetailsDAO = new LclSsDetailDAO();
//        LclBookingPlanBean bookingPlan = new LclBookingPlanDAO().getRelay(lclSsHeader.getOrigin().getId(),
//                lclSsHeader.getDestination().getId(), "N");
        String shipperAddress = "";
        String placeOfCity = "";
        if (lclSsHeader.getOrigin() != null && CommonUtils.isNotEmpty(lclSsHeader.getOrigin().getUnLocationCode())) {
            String[] shipperDetails = lclSSDetailsDAO.getAddress(lclSsHeader.getOrigin().getUnLocationCode());
            placeOfCity = lclSSDetailsDAO.getCityAndState(lclSsHeader.getOrigin().getUnLocationCode());
            if (null != shipperDetails && shipperDetails[0] != null) {
                shipperAddress = shipperDetails[0] + "\n" + shipperDetails[1];
            }
        }
        TradingPartner agentAcct = null != lclSsHeader.getLclSsExports() ? lclSsHeader.getLclSsExports().getExportAgentAcctoNo() : null;
        if (null != agentAcct) {
            consigneeDetails.append(agentAcct.getAccountName()).append("\n");
            CustAddress data = new CustAddressDAO().findByAccountNoAndPrime(agentAcct.getAccountno());
            if (null != data) {
                if (CommonUtils.isNotEmpty(data.getAddress1())) {
                    consigneeDetails.append(data.getAddress1()).append("\n");
                }
                String address1 = "";
                if (CommonUtils.isNotEmpty(data.getCity1())) {
                    address1 += data.getCity1() + ", ";
                }
                if (CommonUtils.isNotEmpty(data.getState())) {
                    address1 += data.getState() + " ";
                }
                if (CommonUtils.isNotEmpty(data.getZip())) {
                    address1 += data.getZip() + " ";
                }
                if (CommonUtils.isNotEmpty(address1)) {
                    consigneeDetails.append(address1).append("\n");
                }
                if (CommonUtils.isNotEmpty(data.getPhone())) {
                    consigneeDetails.append(data.getPhone()).append("\n");
                }
                if (CommonUtils.isNotEmpty(data.getFax())) {
                    consigneeDetails.append(data.getFax()).append("\n");
                }
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
        LclSsDetail lclSsDetail = lclUnitSs.getLclSsHeader().getVesselSsDetail();
        if (null != lclSsDetail.getDeparture() && CommonUtils.isNotEmpty(lclSsDetail.getDeparture().getUnLocationName())) {
            departurePier.append(lclSsDetail.getDeparture().getUnLocationName());
        }
        if (null != lclSsDetail.getDeparture() && null != lclSsDetail.getDeparture().getStateId()) {
            departurePier.append(", ").append(lclSsDetail.getDeparture().getStateId().getCode());
        }
        if (null != lclSsDetail.getArrival() && null != lclSsDetail.getArrival().getCountryId()) {
            arrivalPier.append(lclSsDetail.getArrival().getCountryId().getCodedesc());
        }
        if (null != lclSsDetail.getArrival() && CommonUtils.isNotEmpty(lclSsDetail.getArrival().getUnLocationName())) {
            arrivalPier.append(", ").append(lclSsDetail.getArrival().getUnLocationName());
        }
        table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{46f, 54f});
        table.setKeepTogether(true);

        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100f);
        headerTable.setWidths(new float[]{30f, 50f, 20f});

        headerTable.addCell(createCell("", blackBoldFont, 0, 0f, Element.ALIGN_LEFT));
        headerTable.addCell(createCell(lclSsHeader.getScheduleNo() + "  MULTIMODAL DANGEROUS GOODS FORM", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT));
        headerTable.addCell(createCell("", blackBoldFont, 0, 0f, Element.ALIGN_LEFT));

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(headerTable);
        cell.setBorder(0);
        table.addCell(cell);

        PdfPCell mainCell = createEmptyCellWithBorder();

        PdfPTable subTable = new PdfPTable(1);
        subTable.setWidthPercentage(100);
        subTable.addCell(createCell("1.Shipper/Consigee/Sender", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT));
        subTable.addCell(createCell("" + shipperAddress, fontCompNormalSub, 0, 0f, Element.ALIGN_LEFT));
        mainCell.addElement(subTable);
        table.addCell(mainCell);

        mainCell = createEmptyCellWithBorder();

        subTable = new PdfPTable(2);
        subTable.setWidths(new float[]{35f, 65f});
        subTable.setWidthPercentage(100);
        cell = createCell("2.Transport Document Number:  " + bkgRef, blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setMinimumHeight(20);
        cell.setExtraParagraphSpace(2f);
        cell.setColspan(2);
        subTable.addCell(cell);
        mainCell.addElement(subTable);

        subTable = new PdfPTable(2);
        subTable.setWidths(new float[]{35f, 65f});
        subTable.setWidthPercentage(100);
        cell = createCell("3.Page 1 of 1", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setMinimumHeight(20);
        cell.setExtraParagraphSpace(2f);
        makeBorder(cell, 0, 0.6f, 0.6f, 0);
        subTable.addCell(cell);
        cell = createCell("4.Shipper's reference: " + lclSsHeader.getScheduleNo(), blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setMinimumHeight(20);
        cell.setExtraParagraphSpace(2f);
        cell.setBorderWidthTop(0.6f);
        subTable.addCell(cell);
        subTable.setKeepTogether(true);
        mainCell.addElement(subTable);

        subTable = new PdfPTable(2);
        subTable.setWidths(new float[]{35f, 65f});
        subTable.setWidthPercentage(100);
        cell = createCell("1 / 1", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setMinimumHeight(20);
        cell.setExtraParagraphSpace(2f);
        makeBorder(cell, 0, 0.6f, 0.6f, 0);
        subTable.addCell(cell);
        cell = createCell("5.Frieght forwarder's reference: ", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setMinimumHeight(20);
        cell.setExtraParagraphSpace(2f);
        cell.setBorderWidthTop(0.6f);
        subTable.addCell(cell);
        subTable.setKeepTogether(true);
        mainCell.addElement(subTable);
        mainCell.setBorderWidthLeft(0);
        table.addCell(mainCell);

        cell = createCell("6.Consignee", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setMinimumHeight(15);
        makeBorder(cell, 0.6f, 0.6f, 0, 0.6f);
        table.addCell(cell);
        cell = createCell("7.Carrier(to be completed by the carrier):" + lclSsDetail.getSpAcctNo().getAccountName().toUpperCase(), blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setMinimumHeight(15);
        makeBorder(cell, 0, 0.6f, 0, 0.6f);
        table.addCell(cell);
        cell = createCell("" + consigneeDetails, fontCompNormalSub, 0, 0f, Element.ALIGN_LEFT);
        makeBorder(cell, 0.6f, 0.6f, 0, 0.6f);
        table.addCell(cell);

        subTable = new PdfPTable(1);
        subTable.setWidthPercentage(100);
        subTable.addCell(createCell("SHIPPER'S DECLARATION", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT));
        cell = createCell("I hereby declare that the contents to this consignment are fully and accurately described below by the Proper"
                + " Shipping Name,and are classified, packaged, marked, labeled and placarded and are in all respects in proper condition for "
                + "transport according to the applicable international and national govermet regulations.", blackContentNormalFont, 0, 0f, Element.ALIGN_LEFT);
        subTable.addCell(cell);
        mainCell = createEmptyCellWithBorder();
        mainCell.setBorderWidthLeft(0);
        mainCell.setBorderWidthTop(0);
        subTable.setKeepTogether(true);
        mainCell.addElement(subTable);
        table.addCell(mainCell);
        cell = createCell("8.This shipment is within the limitations prescribed for: \n(Delete non-applicable)", blackBoldFont65, 0, 0f, Element.ALIGN_LEFT);
        makeBorder(cell, 0.6f, 0.6f, 0, 0);
        table.addCell(cell);
        cell = createCell("9.Additional handling information:", resultComFont, 0, 0f, Element.ALIGN_LEFT);
        makeBorder(cell, 0, 0.6f, 0, 0);
        table.addCell(cell);

        subTable = new PdfPTable(2);
        subTable.setWidths(new float[]{50f, 50f});
        subTable.setWidthPercentage(100);
        cell = createCell("PASSENGER AND CARGO \nAIRCRAFT/", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        makeBorder(cell, 0, 0.6f, 0, 0);
        subTable.addCell(cell);
        subTable.addCell(createCell("CARGO AIRCRAFT ONLY/", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT));
        mainCell = createEmptyCellWithBorder();
        mainCell.setBorderWidthBottom(0f);
        subTable.setKeepTogether(true);
        mainCell.addElement(subTable);
        table.addCell(mainCell);
        cell = createCell("EMERGENCY PHONE NUMBER:", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        makeBorder(cell, 0, 0.6f, 0, 0);
        table.addCell(cell);
        StringBuilder vesselDetails = new StringBuilder();

        if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceName())) {
            vesselDetails.append(lclSsDetail.getSpReferenceName()).append(" .  ");
        }
        if (CommonFunctions.isNotNull(lclSsDetail.getTransMode())) {
            vesselDetails.append(lclSsDetail.getTransMode()).append("  \n");
        }
        if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceNo())) {
            vesselDetails.append(lclSsDetail.getSpReferenceNo());
        }

        subTable = new PdfPTable(2);
        subTable.setWidths(new float[]{50f, 50f});
        subTable.setWidthPercentage(100);
        subTable.addCell(createCell("10.Vessel/flight No and date", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT));
        cell = createCell("11.Port/Place of loading", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthLeft(0.6f);
        subTable.addCell(cell);
        subTable.addCell(createCell(vesselDetails.toString(), blackContentBoldFont, 0, 0f, Element.ALIGN_CENTER));
        cell = createCell(departurePier.toString(), blackContentBoldFont, 0, 0f, Element.ALIGN_CENTER);
        cell.setBorderWidthLeft(0.6f);
        subTable.addCell(cell);
        mainCell = createEmptyCellWithBorder();
        mainCell.setBorderWidthBottom(0f);
        mainCell.addElement(subTable);
        table.addCell(mainCell);
        cell = createCell("CHEMTREC 703-527-3887 \n CONTRACT# 7371", blackContentBoldFont, 0, 0f, Element.ALIGN_CENTER);
        makeBorder(cell, 0, 0.6f, 0, 0);
        table.addCell(cell);

        subTable = new PdfPTable(2);
        subTable.setWidths(new float[]{50f, 50f});
        subTable.setWidthPercentage(100);
        subTable.addCell(createCell("12.Port/Place of discharge", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT));
        cell = createCell("13.Destination", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthLeft(0.6f);
        subTable.addCell(cell);
        subTable.addCell(createCell(arrivalPier.toString(), blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT));
        cell = createCell(destination.toString(), blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthLeft(0.6f);
        subTable.addCell(cell);
        mainCell = createEmptyCellWithBorder();
        mainCell.setBorderWidthBottom(0);
        subTable.setKeepTogether(true);
        mainCell.addElement(subTable);
        table.addCell(mainCell);
        cell = createCell("", headingBoldFont, 0, 0f, Element.ALIGN_CENTER);
        makeBorder(cell, 0, 0.6f, 0, 0);
        table.addCell(cell);

        subTable = new PdfPTable(6);
        subTable.setWidths(new float[]{12f, 40f, 5f, 17f, 23f, 13f});
        subTable.setWidthPercentage(100);
        subTable.addCell(createCell("14.Shipping \n marks", FontFactory.getFont("Arial", 6.5f, Font.NORMAL), 0, 0f, Element.ALIGN_LEFT));
        subTable.addCell(createCell("Numbers and kind of packages:description of goods", FontFactory.getFont("Arial", 6.5f, Font.NORMAL), 0, 0f, Element.ALIGN_LEFT));
        subTable.addCell(createCell("", FontFactory.getFont("Arial", 6.5f, Font.NORMAL), 0, 0f, Element.ALIGN_LEFT));
        subTable.addCell(createCell("Gross mass(kg)", FontFactory.getFont("Arial", 6.5f, Font.NORMAL), 0, 0f, Element.ALIGN_LEFT));
        subTable.addCell(createCell("Net mass(kg)", FontFactory.getFont("Arial", 6.5f, Font.NORMAL), 0, 0f, Element.ALIGN_LEFT));
        subTable.addCell(createCell("Cube(m3)", FontFactory.getFont("Arial", 6.5f, Font.NORMAL), 0, 0f, Element.ALIGN_LEFT));
        mainCell = createEmptyCellWithBorder();
        mainCell.setBorderWidthBottom(0);
        subTable.setKeepTogether(true);
        mainCell.addElement(subTable);
        mainCell.setColspan(2);
        table.addCell(mainCell);

        List<OceanManifestBean> pickedDrList = new LclBLPieceDAO().getPickedDRLclBlData(lclUnitSs.getId(), lclUnitSs.getLclSsHeader().getId(), false);
        LclConsolidateDAO lclConsolidateDAO = new LclConsolidateDAO();
        LclReportUtils reportUtils = new LclReportUtils();
        StringBuilder commdityDesc = new StringBuilder();
        LclHazmatDAO hazmatDAO = new LclHazmatDAO();
        if (pickedDrList != null && pickedDrList.size() > 0) {
            for (OceanManifestBean pickedLclBl : pickedDrList) {
                if (CommonUtils.isNotEmpty(pickedLclBl.getFileId())) {
                    List<Long> conoslidatelist = lclConsolidateDAO.getConsolidatesFiles(pickedLclBl.getFileId());
                    conoslidatelist.add(pickedLclBl.getFileId());
                    List<LclBookingHazmat> hazmatList = hazmatDAO.findByFileId(conoslidatelist);
                    if (hazmatList != null && !hazmatList.isEmpty()) {
                        for (LclBookingHazmat hazmat : hazmatList) {
                            commdityDesc.append(reportUtils.concateHazmatDetails(hazmat)).append("\n");
                        }
                    }
                }
            }
        }
        subTable = new PdfPTable(6);
        subTable.setWidths(new float[]{12f, 30f, 15f, 17f, 23f, 13f});
        subTable.setWidthPercentage(100);
        cell = createCell(commdityDesc.toString().toUpperCase(), blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setColspan(2);
        subTable.addCell(cell);
        subTable.addCell(createCell("", arialFontSize10Normal, 0, 0f, Element.ALIGN_LEFT));
        subTable.addCell(createCell("", arialFontSize10Normal, 0, 0f, Element.ALIGN_LEFT));
        subTable.addCell(createCell("", arialFontSize10Normal, 0, 0f, Element.ALIGN_LEFT));
        subTable.addCell(createCell("", arialFontSize10Normal, 0, 0f, Element.ALIGN_LEFT));
        mainCell = createEmptyCellWithBorder();
        mainCell.setBorderWidthBottom(0);
        subTable.setKeepTogether(true);
        mainCell.addElement(subTable);
        mainCell.setMinimumHeight(180);
        mainCell.setColspan(2);
        table.addCell(mainCell);

        subTable = new PdfPTable(5);
        subTable.setWidths(new float[]{20f, 20f, 20f, 20f, 20f});
        subTable.setWidthPercentage(100);
        cell = createCell("15.Container identification \n No/vehicle registration No.", FontFactory.getFont("Arial", 6.5f, Font.NORMAL), 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        cell = createCell("16.Seal Number(s)", FontFactory.getFont("Arial", 6.5f, Font.NORMAL), 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        cell = createCell("17.Container/Vehicle size & \n type", FontFactory.getFont("Arial", 6.5f, Font.NORMAL), 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        cell = createCell("19.Total gross mass \n (including tare)(kg)", FontFactory.getFont("Arial", 6.5f, Font.NORMAL), 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        subTable.addCell(createCell("19.Total gross mass \n (including tare)(kg)", FontFactory.getFont("Arial", 6.5f, Font.NORMAL), 0, 0f, Element.ALIGN_LEFT));

        String containerNo = "";
        String sealNo = "";
        if (CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getUnitNo())) {
            containerNo = lclUnitSs.getLclUnit().getUnitNo();
        }
        if (CommonFunctions.isNotNull(lclUnitSs.getSUHeadingNote())) {
            sealNo = lclUnitSs.getSUHeadingNote();
        }
        String[] description = new LclUnitSsDAO().getLclUnitId(lclUnitSs.getId());
        String desc = "";
        if (CommonUtils.isNotEmpty(description[0])) {
            desc = description[0];
        } else {
            if (description[1].equalsIgnoreCase("LCL")) {
                desc = description[1].toUpperCase();

            } else {
                desc = description[1].substring(0, 4).toUpperCase();
            }
        }
        cell = createCell(containerNo, blackContentBoldFont, 0, 0f, Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        cell = createCell(sealNo, blackContentBoldFont, 0, 0f, Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        cell = createCell("1x" + desc, blackContentBoldFont, 0, 0f, Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        cell = createCell("", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        subTable.addCell(createCell("", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT));
        mainCell = createEmptyCellWithBorder();
        mainCell.setBorderWidthBottom(0);
        subTable.setKeepTogether(true);
        mainCell.addElement(subTable);
        mainCell.setColspan(2);
        table.addCell(mainCell);

        subTable = new PdfPTable(3);
        subTable.setWidths(new float[]{40f, 20f, 40f});
        subTable.setWidthPercentage(100);
        cell = createCell("CONTAINER/VEHICLE PACKING CERTIFICATE", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        cell = createCell("21.RECEIVING ORGANIZATION RECEIPT", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setColspan(2);
        subTable.addCell(cell);
        cell = createCell("I hereby declare that the goods described above have been packed/loaded into the container/vehicle idetified above in "
                + "accordance with the applicable provisions. MUST BE COMPLETED AND SIGNED FOR ALL CONTAINER/VEHICLE LOADS BY PERSON RESPONSIBLE FOR PACKING/LOADING", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);

        cell = createCell("Received the above number of packages/containers/trailers in apparent good order and condition, unless stated here on :"
                + "RECEIVING ORGAIZATION REMARKS", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setColspan(2);
        subTable.addCell(cell);

        cell = createCell("20.Name of Company", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        subTable.addCell(createCell("Hauler's name", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT));
        cell = createCell("22.Name of company(OF SHIPPER PREPARING THIS NOTE)", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthLeft(0.6f);
        subTable.addCell(cell);
        cell = createCell("ECU WORLDWIDE.", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        subTable.addCell(createCell("", arialBoldFont14Size, 0, 0f, Element.ALIGN_LEFT));
        cell = createCell("ECU WORLDWIDE.", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthLeft(0.6f);
        subTable.addCell(cell);

        String printedBy = "";
        if (CommonUtils.isNotEmpty(user.getFirstName())) {
            printedBy = user.getFirstName();
        }
        if (CommonUtils.isNotEmpty(user.getLastName())) {
            printedBy += " " + user.getLastName();
        }
        cell = createCell("Name/status of declarant", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        subTable.addCell(createCell("Vehicle reg.no", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT));
        cell = createCell("Name/status of declarant", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthLeft(0.6f);
        subTable.addCell(cell);
        cell = createCell(printedBy, blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        subTable.addCell(createCell("", arialBoldFont14Size, 0, 0f, Element.ALIGN_LEFT));
        cell = createCell(printedBy, blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthLeft(0.6f);
        subTable.addCell(cell);

        cell = createCell("Place of date", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        subTable.addCell(createCell("Signature and date", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT));
        cell = createCell("Place of date", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthLeft(0.6f);
        subTable.addCell(cell);
        cell = createCell(placeOfCity + " " + DateUtils.formatDate(new Date(), "MM-dd-yyyy"), blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        subTable.addCell(createCell("", arialBoldFont14Size, 0, 0f, Element.ALIGN_LEFT));
        cell = createCell(placeOfCity + " " + DateUtils.formatDate(new Date(), "MM-dd-yyyy"), blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthLeft(0.6f);
        subTable.addCell(cell);

        cell = createCell("Signature of declarant", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthRight(0.6f);
        subTable.addCell(cell);
        cell = createCell("DRIVER'S SIGNATURE", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthTop(0.6f);
        subTable.addCell(cell);
        cell = createCell("Signature of declarant", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthLeft(0.6f);
        subTable.addCell(cell);
        cell = createCell("", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthRight(0.6f);
        cell.setMinimumHeight(10f);
        subTable.addCell(cell);
        cell = createCell("", arialBoldFont14Size, 0, 0f, Element.ALIGN_LEFT);
        subTable.addCell(cell);
        cell = createCell("", blackContentBoldFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setBorderWidthLeft(0.6f);
        cell.setMinimumHeight(10f);
        subTable.addCell(cell);

        subTable.setKeepTogether(true);
        mainCell = createEmptyCellWithBorder();
        mainCell.addElement(subTable);
        mainCell.setColspan(2);
        table.setKeepTogether(true);
        table.addCell(mainCell);
        cell = createCell("*DANGEROUS GOODS: You must specify Proper Shipping Name, hazard class, UN NO,packing group(where assigned) marine pollutant and observe the \n "
                + "mandatory requirements under applicable national and international governmental regulations.For the purpose of IMDG Code see 54.1.1 \n"
                + "For the purpose of IMDG Code, see 54.2", bodyNormalFont, 0, 0f, Element.ALIGN_LEFT);
        cell.setColspan(2);
        table.addCell(cell);
        return table;
    }

}
