package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.ExportUnitQueryUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingHotCode;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceWhse;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO;
import com.gp.cong.logisoft.lcl.bc.ExportBookingUtils;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.PdfPCellEvent;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class LclExportNewPdfFormat extends LclReportFormatMethods {

    private static final Logger log = Logger.getLogger(LclExportOldPdfFormat.class);
    private LclUnitSs lclUnitSs;
    private String loadingdate = "";
    StringBuilder unitsNumber = new StringBuilder();
    private String hazmetDate = "";
    private String vesselname = "";
    private String sslinevoyage = "";
    private String sailingDate = "";
    private String Voy = "";
    private String masterBkgNO = "";
    private StringBuilder destination = new StringBuilder();
    StringBuilder origin = new StringBuilder();
    private String ssLine = "";
    StringBuilder sailDate = new StringBuilder();
    private PdfPTable PdfPTable;
    private Integer totalPieceCount = 0;
    private Double totalvolumemeasure = 0.00;
    private Double totalweightmeasure = 0.00;
    private String sealNo = "";
    private Integer drCount = 0;
    private Integer allocatedDrCount = 0;
    private Integer totalDrCount = 0;
    private Integer pageNum = 1;
    private Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
    private String loaded_by;
    private String separateName = "";
    private String locationDoor = "";

    public LclExportNewPdfFormat(LclUnitSs lclUnitSs) throws Exception {
        this.lclUnitSs = lclUnitSs;
        if (CommonFunctions.isNotNull(lclUnitSs) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader())) {
            if (CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getDestination().getCountryId().getCodedesc())) {
                destination.append(lclUnitSs.getLclSsHeader().getDestination().getCountryId().getCodedesc());
            }
            if (CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName())) {
                destination.append(", ").append(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName());
            }
        }
        if (CommonUtils.isNotEmpty(lclUnitSs.getSpBookingNo())) {
            masterBkgNO = lclUnitSs.getSpBookingNo().toUpperCase();
        }
        if (CommonFunctions.isNotNull(lclUnitSs) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader())) {
            if (CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getOrigin().getUnLocationName())) {
                origin.append(lclUnitSs.getLclSsHeader().getOrigin().getUnLocationName());
            }
            if (null != lclUnitSs.getLclSsHeader().getOrigin().getStateId()
                    && CommonUtils.isNotEmpty(lclUnitSs.getLclSsHeader().getOrigin().getStateId().getCode())) {
                origin.append(", ").append(lclUnitSs.getLclSsHeader().getOrigin().getStateId().getCode());
            }
        }
        if (CommonFunctions.isNotNull(lclUnitSs)) {
            if (CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getUnitNo())) {
                unitsNumber.append(lclUnitSs.getLclUnit().getUnitNo()).append("      ");
                unitsNumber.append(new LclUtils().getContainerSize(lclUnitSs.getId()));
            }
        }
        if (CommonFunctions.isNotNull(lclUnitSs)) {
            locationDoor = new LclUnitWhseDAO().getLocationValues(lclUnitSs.getLclUnit().getId(),
                    lclUnitSs.getLclSsHeader().getId());
            if (CommonFunctions.isNotNull(lclUnitSs.getSUHeadingNote())) {
                sealNo = lclUnitSs.getSUHeadingNote();
            }
            LclUnitWhse unitWhse = new LclUnitWhseDAO().getLclUnitWhseFirstRecord(lclUnitSs.getLclUnit().getId(),
                    lclUnitSs.getLclSsHeader().getId());
            loaded_by = null != unitWhse && null != unitWhse.getStuffedByUser()
                    ? unitWhse.getStuffedByUser().getFirstName() + " " + unitWhse.getStuffedByUser().getLastName() : "";
        }
        List<LclSsDetail> lclSsDetailList = lclUnitSs.getLclSsHeader().getLclSsDetailList();
        if (lclSsDetailList != null && lclSsDetailList.size() > 0) {
            for (Object lclSSDetail : lclSsDetailList) {
                LclSsDetail lclssDetail = (LclSsDetail) lclSSDetail;
                //pieces values
                if (CommonFunctions.isNotNull(lclssDetail.getSpAcctNo())) {
                    ssLine = lclssDetail.getSpAcctNo().getAccountName();
                }
                if (CommonFunctions.isNotNull(lclssDetail.getSpReferenceName())) {
                    vesselname = (lclssDetail.getSpReferenceName());
                }
                if (CommonFunctions.isNotNull(lclssDetail.getStd())) {
                    sailingDate = (DateUtils.parseDateToString(lclssDetail.getStd()));
                }
                if (CommonFunctions.isNotNull(lclssDetail.getSpReferenceNo())) {
                    sslinevoyage = (lclssDetail.getSpReferenceNo());
                }
                if (CommonFunctions.isNotNull(lclssDetail.getGeneralLrdt())) {
                    loadingdate = DateUtils.parseDateToString(lclssDetail.getGeneralLrdt());
                }
                if (CommonFunctions.isNotNull(lclssDetail.getHazmatLrdt())) {
                    hazmetDate = DateUtils.parseDateToString(lclssDetail.getHazmatLrdt());
                }
            }
        }
        if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getScheduleNo())) {
            Voy = (lclUnitSs.getLclSsHeader().getScheduleNo());
        }

    }

    class TableHeader extends PdfPageEventHelper {

        PdfTemplate total;

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(30, 16);
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                document.add(writeheader());
                document.add(secondHeader());
                Rectangle rect = new Rectangle(975, 582, 30, 25);
                rect.setBorder(Rectangle.BOX);
                rect.setBorderWidth(0.6f);
                document.add(rect);
            } catch (Exception ex) {
                log.info("Exception on class LclExportNewPdfFormat in method onStartPage" + new Date(), ex);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                Font blackFont = new Font(Font.FontFamily.COURIER, 12f, Font.NORMAL);
                PdfContentByte cb = writer.getDirectContent();
                if (countMap.isEmpty()) {
                    allocatedDrCount = drCount;
                } else {
                    allocatedDrCount = countMap.get(document.getPageNumber());
//                    if (countMap.get(document.getPageNumber()) != null && document.getPageNumber() > 1) {
//                        allocatedDrCount = countMap.get(document.getPageNumber()) - countMap.get(document.getPageNumber() - 1);
//                    } else if (countMap.get(document.getPageNumber()) != null) {
//                        allocatedDrCount = countMap.get(document.getPageNumber());
//                    } else if (countMap.get(document.getPageNumber()) == null) {
//                        allocatedDrCount = drCount - countMap.get(document.getPageNumber() - 1);
//                    } else {
//                        allocatedDrCount = 0;
//                    }
                }
                Phrase footer = new Phrase("(" + allocatedDrCount + "/" + totalDrCount + ")Drs", blackFont);
                ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                        footer, document.right() - 19, document.bottom() - 10, 0);
            } catch (Exception ex) {
                log.info("Exception on class LclExportNewPdfFormat in method onStartPage" + new Date(), ex);
            }
        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);
        }
    }

    abstract class CustomBorder implements PdfPCellEvent {

        private int border = 0;

        public CustomBorder(int border) {
            this.border = border;
        }

        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
            setLineDash(canvas);
            if ((border & PdfPCell.TOP) == PdfPCell.TOP) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getTop());
            }
            if ((border & PdfPCell.BOTTOM) == PdfPCell.BOTTOM) {
                canvas.moveTo(position.getRight(), position.getBottom());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }
            if ((border & PdfPCell.RIGHT) == PdfPCell.RIGHT) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getRight(), position.getBottom());
            }
            if ((border & PdfPCell.LEFT) == PdfPCell.LEFT) {
                canvas.moveTo(position.getLeft(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }
        }

        public abstract void setLineDash(PdfContentByte canvas);
    }

    class DashedBorder extends CustomBorder {

        public DashedBorder(int border) {
            super(border);
        }

        @Override
        public void setLineDash(PdfContentByte canvas) {
            canvas.setLineCap(0);
            canvas.setLineWidth(0.6f);
        }
    }

    private void init(String outputFileName) throws Exception {
        blackBoldFontSize6 = new Font(Font.FontFamily.COURIER, 9f, Font.BOLD);
        document = new Document(PageSize.LEGAL.rotate());
        document.setMargins(30, 33, 30, 40);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        LclExportNewPdfFormat.TableHeader event = new LclExportNewPdfFormat.TableHeader();
        pdfWriter.setPageEvent(event);
        ExportUnitQueryUtils exportQueryUtils = new ExportUnitQueryUtils();
        Map<String, String> drListMap = new HashMap<String, String>();
        document.open();
        String secure_list = exportQueryUtils.getSecureCargoBkgList(lclUnitSs.getId(),
                lclUnitSs.getLclSsHeader().getServiceType(), null);
        totalDrCount += null != secure_list ? secure_list.split(",").length : 0;
        drListMap.put("secure", null != secure_list ? secure_list : "");

        String upscargo_list = exportQueryUtils.getUPSCargoBkgList(lclUnitSs.getId(),
                lclUnitSs.getLclSsHeader().getServiceType(), null);
        totalDrCount += null != upscargo_list ? upscargo_list.split(",").length : 0;
        drListMap.put("ups", null != upscargo_list ? upscargo_list : "");

        String general_list = exportQueryUtils.getGeneralCargoBkgList(lclUnitSs.getId(),
                lclUnitSs.getLclSsHeader().getServiceType(), null);
        totalDrCount += null != general_list ? general_list.split(",").length : 0;
        drListMap.put("general", null != general_list ? general_list : "");

        String hazmatcargo_list = exportQueryUtils.getHazmatCargoBkgList(lclUnitSs.getId(),
                lclUnitSs.getLclSsHeader().getServiceType(), null);
        totalDrCount += null != hazmatcargo_list ? hazmatcargo_list.split(",").length : 0;
        drListMap.put("hazmat", null != hazmatcargo_list ? hazmatcargo_list : "");

        String arriveOrCargo_list = "", intrCargoName = "";
        if ("E".equalsIgnoreCase(lclUnitSs.getLclSsHeader().getServiceType())) {
            intrCargoName = "**INTR CARGO***";
            arriveOrCargo_list = exportQueryUtils.getINTRCargo(lclUnitSs.getLclSsHeader().getOrigin().getId(),
                    lclUnitSs.getLclSsHeader().getDestination().getId(), lclUnitSs.getId(), null);
        } else if ("N".equalsIgnoreCase(lclUnitSs.getLclSsHeader().getServiceType())) {
            intrCargoName = "*TO ARRIVE*****";
            arriveOrCargo_list = exportQueryUtils.toArriveBookingList(lclUnitSs.getId(),
                    lclUnitSs.getLclSsHeader().getOrigin().getId(),
                    lclUnitSs.getLclSsHeader().getDestination().getId(), null);
        }
        totalDrCount += null != arriveOrCargo_list ? arriveOrCargo_list.split(",").length : 0;
        drListMap.put("arrival", null != arriveOrCargo_list ? arriveOrCargo_list : "");

        String fileIds = drListMap.get("secure");
        separateName = "*SECURED CARGO*";
        separateDrList(fileIds);
        fileIds = drListMap.get("ups");
        separateName = "*U.P.S. CARGO**";
        separateDrList(fileIds);
        fileIds = drListMap.get("general");
        separateName = "*GENERAL CARGO*";
        separateDrList(fileIds);
        fileIds = drListMap.get("hazmat");
        separateName = "HAZARDOUS CARGO";
        separateDrList(fileIds);
        fileIds = drListMap.get("arrival");
        separateName = intrCargoName;
        separateDrList(fileIds);
        document.add(totalCommodityCountsTable());
        document.close();
    }

    public void separateDrList(String fileIds) throws Exception {
        Map<String, String> orderMap = new TreeMap<String, String>();
        if (!fileIds.equalsIgnoreCase("")) {
            int count = 0;
            for (String fileId : fileIds.split(",")) {
                String wareHouseLocation = new LclBookingPieceWhseDAO().getWareHouseOrder(fileId);
                if (wareHouseLocation != null) {
                    orderMap.put(wareHouseLocation + "" + fileId, fileId);
                } else {
                    orderMap.put(String.valueOf(--count), fileId);
                }
            }
        } else {
            document.add(commonDrList(null, "", separateName));

        }
        for (Map.Entry<String, String> map : orderMap.entrySet()) {
            drCount++;
            document.add(commonDrList(null, map.getValue(), separateName));
            if (drCount == 5) {
                countMap.put(pageNum, 5);
                document.newPage();
                pageNum++;
                drCount = 0;
            }
            countMap.put(pageNum, drCount);

        }
//        
    }

    private PdfPTable writeheader() throws Exception {
        PdfPTable headerTable = new PdfPTable(12);
        PdfPCell headerCell = null;
        Paragraph p;
        headerTable.setWidthPercentage(100f);
        headerTable.setWidths(new float[]{1.5f, 3f, 1.2f, 2f, 1.2f, 1.5f, 0.1f, 1.5f, 2.7f, 1f, 2.7f, 3f});

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        p = new Paragraph(6f, "Destination", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setColspan(2);
        headerCell.setPaddingTop(-2f);
        p = new Paragraph(new Chunk(destination.toString().toUpperCase(), blackBoldFontSize6).setUnderline(0.1f, -2f));
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        p = new Paragraph(6f, "Origin", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_RIGHT);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setColspan(2);
        headerCell.setPaddingTop(-3f);
        p = new Paragraph(new Chunk(origin.toString().toUpperCase(), blackBoldFontSize6).setUnderline(0.1f, -2f));
        headerCell.addElement(p);
        headerCell.setBorderWidthRight(0.6f);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        p = new Paragraph(6f, " Arrival Dt", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(1f);
        p = new Paragraph(6f, "_____________________", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        p = new Paragraph(6f, "Over", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_RIGHT);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setBorder(0);
        headerCell.setPaddingTop(1f);
        p = new Paragraph(6f, " _____________________", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        //2
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        p = new Paragraph(6f, "Loaded By", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setColspan(2);
        headerCell.setPaddingTop(-3f);
        p = new Paragraph(new Chunk(loaded_by.toUpperCase(), blackBoldFontSize6).setUnderline(0.1f, -2f));
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        p = new Paragraph(6f, "Location Door", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_RIGHT);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setColspan(2);
        headerCell.setPaddingTop(-4f);
        p = new Paragraph(new Chunk(locationDoor.toUpperCase(), blackBoldFontSize6).setUnderline(0.1f, -2f));
        headerCell.addElement(p);
        headerCell.setBorderWidthRight(0.6f);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        p = new Paragraph(6f, "", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        p = new Paragraph(6f, "", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        //3rd
        headerCell = new PdfPCell();
        headerCell.setPaddingLeft(10f);
        p = new Paragraph(6f, "Unit  #", blackBoldFontSize6);
        headerCell.setBorder(0);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setColspan(2);
        headerCell.setPaddingTop(-4f);
        p = new Paragraph(new Chunk(unitsNumber.toString(), blackBoldFontSize6).setUnderline(0.1f, -2f));
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        p = new Paragraph(6f, "Seal", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_RIGHT);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setColspan(2);
        headerCell.setPaddingTop(-4f);
        p = new Paragraph(new Chunk(sealNo.toString(), blackBoldFontSize6).setUnderline(0.1f, -2f));
        headerCell.addElement(p);
        headerCell.setBorderWidthRight(0.6f);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-4f);
        p = new Paragraph(6f, " Strip Date", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-5f);
        p = new Paragraph(6f, "_____________________", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-4f);
        p = new Paragraph(6f, "Short", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_RIGHT);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-6f);
        p = new Paragraph(6f, " _____________________", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        //4
        p = new Paragraph(6f, "S/S Line", blackBoldFontSize6);
        headerCell.setBorder(0);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-4f);
        p = new Paragraph(new Chunk(ssLine, blackBoldFontSize6).setUnderline(0.1f, -2f));
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        p = new Paragraph(6f, "Vessel", blackBoldFontSize6);
        headerCell.setBorder(0);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-4f);
        p = new Paragraph(new Chunk(vesselname, blackBoldFontSize6).setUnderline(0.1f, -2f));
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setPaddingLeft(5f);
        p = new Paragraph(6f, "Ec.Voy ", blackBoldFontSize6);
        headerCell.setBorder(0);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-4f);
        p = new Paragraph(new Chunk(Voy, blackBoldFontSize6).setUnderline(0.1f, -2f));
        headerCell.addElement(p);
        headerCell.setBorderWidthRight(0.6f);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        p = new Paragraph(6f, "", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        p = new Paragraph(6f, "", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        //5
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingRight(-10f);
        p = new Paragraph(6f, "Sailing Date", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        p = new Paragraph(6f, "" + sailingDate, blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingLeft(-20f);
        p = new Paragraph(6f, "S/S Line Voyage", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-4f);
        p = new Paragraph(new Chunk(sslinevoyage.toString(), blackBoldFontSize6).setUnderline(0.1f, -2f));
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingRight(-10f);
        p = new Paragraph(6f, "Booking#", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-4f);
        p = new Paragraph(new Chunk(masterBkgNO, blackBoldFontSize6).setUnderline(0.1f, -2f));
        headerCell.addElement(p);
        headerCell.setBorderWidthRight(0.6f);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-6f);
        p = new Paragraph(6f, "   To Miami", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-8f);
        p = new Paragraph(6f, "_____________________", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-6f);
        p = new Paragraph(6f, "Damage", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_RIGHT);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-9f);
        p = new Paragraph(6f, " _____________________", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        //6
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingRight(-10f);
        p = new Paragraph(6f, "Printed Date", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setColspan(5);
        headerCell.setPaddingRight(-10f);
        p = new Paragraph(6f, "     " + DateUtils.parseDateToString(new Date()), blackBoldFontSize6);
        headerCell.addElement(p);
        headerCell.setBorderWidthRight(0.6f);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-6f);
        p = new Paragraph(6f, " ", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-6f);
        p = new Paragraph(6f, "By", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_RIGHT);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-9f);
        p = new Paragraph(6f, " _____________________", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        //7
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingRight(-10f);
        p = new Paragraph(3f, "Printed Time", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setColspan(5);
        headerCell.setPaddingTop(-1f);
        p = new Paragraph(6f, "      " + DateUtils.formatStringDateToTimeTT(new Date()), blackBoldFontSize6);
        headerCell.addElement(p);
        headerCell.setBorderWidthRight(0.6f);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setColspan(3);
        headerCell.setPaddingTop(-3f);
        p = new Paragraph(3f, " Loading Deadline . . :" + loadingdate, blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);

        //8
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingLeft(29);
        p = new Paragraph(5f, "Page", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setColspan(5);
        headerCell.setPaddingTop(-1f);
        p = new Paragraph(5f, "          " + pdfWriter.getPageNumber(), blackBoldFontSize6);
        headerCell.addElement(p);
        headerCell.setBorderWidthRight(0.6f);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-6f);
        p = new Paragraph(5f, "", blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setColspan(3);
        headerCell.setPaddingTop(-6f);
        p = new Paragraph(5f, "  Haz Loading Deadline :" + hazmetDate, blackBoldFontSize6);
        headerCell.addElement(p);
        headerTable.addCell(headerCell);

        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerTable.addCell(headerCell);
        headerCell = new PdfPCell();
        headerCell.setBorder(0);
        headerCell.setPaddingTop(-4f);
        headerTable.addCell(headerCell);

        return headerTable;
    }

    public PdfPTable secondHeader() throws DocumentException, Exception {
        PdfPTable subheaderTable = new PdfPTable(9);
        subheaderTable.setWidths(new float[]{2.2f, 0.5f, 1.5f, 1.5f, 2f, 1f, 6.05f, 6f, 1.95f});
        subheaderTable.setWidthPercentage(100f);
        PdfPCell cell = null;
        Paragraph p = null;
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, "Dock\nReceipt#", blackBoldFontSize6);
        cell.addElement(p);
        cell.setPaddingLeft(10);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        subheaderTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, "", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        subheaderTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, "Cft", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        subheaderTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, "Weight", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        subheaderTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, "Line", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        subheaderTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, "Pcs\nOn D/R", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        subheaderTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, "Loading\nInstructions / Remarks", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        subheaderTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.addElement(new Paragraph(6f, "", blackBoldFontSize6));
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        subheaderTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, "Pcs\nRecvd", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        subheaderTable.addCell(cell);
        return subheaderTable;
    }

    public PdfPTable commonDrList(PdfPTable subheaderTable, String fileId, String separationName) throws DocumentException, Exception {
        Paragraph p = null;
        Font blackStarSize = new Font(Font.FontFamily.COURIER, 9f, Font.BOLD);
        subheaderTable = new PdfPTable(9);
        subheaderTable.setWidths(new float[]{2.2f, 0.5f, 1.5f, 1.5f, 2f, 1f, 6.05f, 6f, 1.95f});
        subheaderTable.setWidthPercentage(100f);
        if (!separationName.equalsIgnoreCase("") && separationName.equalsIgnoreCase(separateName)) {
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setPadding(0f);
            cell.setPaddingLeft(-3f);
            cell.setPaddingRight(-7f);
            cell.setPaddingBottom(4f);
            cell.setColspan(19);
            p = new Paragraph(8f, " ***********************************************************"
                    + "********************" + separationName + "******************************"
                    + "**************************************************", blackStarSize);
            cell.addElement(p);
            subheaderTable.addCell(cell);
            separateName = "";
        }
        if (!"".equalsIgnoreCase(fileId)) {
            String shipper = "", consingnee = "", fileNumber = "", loadingRemarks = "", hotCode = "", inbondValues = "", packageAbbr = "", commdityDesc = "";
            Double volumeMeasure = 0.00;
            Double weightMeasure = 0.00;
            int pieceCount = 0;
            String wareHouse = "", wareHouseLine = "";
            List<LclBookingPieceWhse> lclBookingPieceWhse = new ArrayList<LclBookingPieceWhse>();
            LclBooking lclbooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
            new LCLBookingDAO().getCurrentSession().evict(lclbooking);
            if (CommonFunctions.isNotNull(lclbooking)) {
                if (CommonUtils.isNotEmpty(lclbooking.getLclFileNumber().getLclBookingPieceList())) {
                    wareHouseLine = new ExportBookingUtils().getWarehouseLineLocation(lclbooking.getLclFileNumber().getLclBookingPieceList(), false);
                }
                if (CommonFunctions.isNotNull(lclbooking.getShipContact()) && CommonUtils.isNotEmpty(lclbooking.getShipContact().getCompanyName())) {
                    shipper = lclbooking.getShipContact().getCompanyName();
                }
                if (CommonFunctions.isNotNull(lclbooking.getConsContact()) && CommonUtils.isNotEmpty(lclbooking.getConsContact().getCompanyName())) {
                    consingnee = lclbooking.getConsContact().getCompanyName();
                }
                if (lclbooking.getLclFileNumber().getShortShipSequence() != 0) {
                    fileNumber = "ZZ" + lclbooking.getLclFileNumber().getShortShipSequence();
                } else {
                    fileNumber = lclbooking.getPortOfOrigin() != null ? lclbooking.getPortOfOrigin().getUnLocationCode().substring(2, 5) : "";
                }
                fileNumber += "-" + lclbooking.getLclFileNumber().getFileNumber();
                List<LclBookingPiece> lclBookingPiecelist = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclbooking.getLclFileNumber().getId());
                for (LclBookingPiece lclBookingPiece : lclBookingPiecelist) {
                    packageAbbr += null != lclBookingPiece.getActualPackageType()
                            ? lclBookingPiece.getActualPackageType().getAbbr01()
                            : lclBookingPiece.getPackageType().getAbbr01();
                    commdityDesc += null != lclBookingPiece.getPieceDesc() ? lclBookingPiece.getPieceDesc().toUpperCase() : "";
                    if (lclBookingPiece.getActualPieceCount() != null) {
                        pieceCount += lclBookingPiece.getActualPieceCount();
                        totalPieceCount += lclBookingPiece.getActualPieceCount();
                    } else if (lclBookingPiece.getBookedPieceCount() != null) {
                        pieceCount += lclBookingPiece.getBookedPieceCount();
                        totalPieceCount += lclBookingPiece.getBookedPieceCount();
                    }
                    if (lclBookingPiece.getActualWeightImperial() != null && lclBookingPiece.getActualWeightImperial().doubleValue() != 0.00) {
                        weightMeasure += lclBookingPiece.getActualWeightImperial().doubleValue();
                        totalweightmeasure += lclBookingPiece.getActualWeightImperial().doubleValue();
                    } else if (lclBookingPiece.getBookedWeightImperial() != null && lclBookingPiece.getBookedWeightImperial().doubleValue() != 0.00) {
                        weightMeasure += lclBookingPiece.getBookedWeightImperial().doubleValue();
                        totalweightmeasure += lclBookingPiece.getBookedWeightImperial().doubleValue();
                    }
                    if (lclBookingPiece.getActualVolumeImperial() != null && lclBookingPiece.getActualVolumeImperial().doubleValue() != 0.00) {
                        volumeMeasure += lclBookingPiece.getActualVolumeImperial().doubleValue();
                        totalvolumemeasure += lclBookingPiece.getActualVolumeImperial().doubleValue();
                    } else if (lclBookingPiece.getBookedVolumeImperial() != null && lclBookingPiece.getBookedVolumeImperial().doubleValue() != 0.00) {
                        volumeMeasure += lclBookingPiece.getBookedVolumeImperial().doubleValue();
                        totalvolumemeasure += lclBookingPiece.getBookedVolumeImperial().doubleValue();
                    }
                    lclBookingPieceWhse = lclBookingPiece.getLclBookingPieceWhseList();
                    if (CommonUtils.isNotEmpty(lclBookingPieceWhse) && null != lclBookingPieceWhse.get(0).getWarehouse()) {
                        wareHouse = lclBookingPieceWhse.get(0).getWarehouse().getWarehouseNo();
                    }
                }
                if (CommonUtils.isNotEmpty(fileId)) {
                    LclRemarks lclRemarks = new LclRemarksDAO().getLclRemarksByType(fileId, "Loading Remarks");
                    if (CommonFunctions.isNotNull(lclRemarks) && CommonFunctions.isNotNull(lclRemarks.getRemarks())) {
                        loadingRemarks = lclRemarks.getRemarks();
                    }
                    List<LclBookingHotCode> lclBookingHotCodes = new LclBookingHotCodeDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
                    if (!lclBookingHotCodes.isEmpty()) {
                        for (LclBookingHotCode lclBookingHotCode : lclBookingHotCodes) {
                            hotCode += lclBookingHotCode.getCode() + ",";
                        }
                    }
                    List<LclInbond> lclInbondsList = new LclInbondsDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileId));
                    if (lclInbondsList != null && lclInbondsList.size() > 0) {
                        for (int i = 0; i < lclInbondsList.size(); i++) {
                            LclInbond lclInbond = (LclInbond) lclInbondsList.get(i);
                            if (CommonFunctions.isNotNull(lclInbond.getInbondType())) {
                                inbondValues += lclInbond.getInbondType();
                            }
                            if (CommonFunctions.isNotNull(lclInbond.getInbondNo())) {
                                inbondValues += lclInbond.getInbondNo() + " ";
                            }

                        }
                    }
                }
            }
            if (CommonUtils.isNotEmpty(hotCode)) {
                hotCode = hotCode.substring(0, hotCode.length() - 1);
            }
            if (CommonUtils.isNotEmpty(commdityDesc) && commdityDesc.length() >= 65) {
                commdityDesc = commdityDesc.substring(0, 65);
            }
            String lcl3pWhseDoc = new Lcl3pRefNoDAO().get3pWhseDoc(Long.parseLong(fileId));
            StringBuilder whDocSplit = new StringBuilder();
            if (CommonUtils.isNotEmpty(lcl3pWhseDoc)) {
            if (lcl3pWhseDoc.length() > 10) {
                int k = 10;
                for (int j = 0; j < lcl3pWhseDoc.length(); j += 10) {
                    if (j == 0) {
                        whDocSplit.append("\n").append("                             WHDOC:").append(StringUtils.substring(lcl3pWhseDoc, j, k));
                        k += 10;
                    } else {
                        whDocSplit.append("\n").append("                                   ").append(StringUtils.substring(lcl3pWhseDoc, j, k));
                        k += 10;
                    }
                }
            } else {
                whDocSplit.append("\n").append("                             WHDOC:").append(lcl3pWhseDoc);
            }
            }
            if (CommonUtils.isNotEmpty(wareHouseLine)) {
                wareHouseLine = wareHouseLine.substring(0, wareHouseLine.length() - 1).toUpperCase();
            }
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setFixedHeight(30f);
            p = new Paragraph(6f, "  " + wareHouse, blackBoldFontSize6);
            p.setAlignment(Element.ALIGN_RIGHT);
            cell.addElement(p);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "", blackBoldFontSize6);
            cell.addElement(p);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "", blackBoldFontSize6);
            cell.addElement(p);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "", blackBoldFontSize6);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "" + wareHouseLine, blackBoldFontSize6);
            cell.addElement(p);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "", blackBoldFontSize6);
            cell.addElement(p);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "Remarks:" + loadingRemarks + "\n" + hotCode, blackBoldFontSize6);
            cell.addElement(p);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "Shipper . :" + shipper + "\nConsignee : " + consingnee + "\n" + inbondValues, blackBoldFontSize6);
            cell.addElement(p);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            subheaderTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "  " + fileNumber, blackBoldFontSize6);
            cell.addElement(p);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "", blackBoldFontSize6);
            cell.addElement(p);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "" + Math.round(volumeMeasure) + " ", blackBoldFontSize6);
            cell.addElement(p);
            p.setAlignment(Element.ALIGN_RIGHT);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "" + Math.round(weightMeasure) + "  ", blackBoldFontSize6);
            p.setAlignment(Element.ALIGN_RIGHT);
            cell.addElement(p);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "", blackBoldFontSize6);
            cell.addElement(p);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "" + pieceCount + "\n" + packageAbbr, blackBoldFontSize6);
            cell.addElement(p);
            p.setAlignment(Element.ALIGN_RIGHT);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, "", blackBoldFontSize6);
            cell.addElement(p);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            p = new Paragraph(6f, commdityDesc + whDocSplit.toString(), blackBoldFontSize6);
            cell.addElement(p);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
            subheaderTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            subheaderTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setColspan(9);
            cell.setPaddingTop(-7f);
            p = new Paragraph(6f, "________________________SECTOR_______________________________________"
                    + "_________________________________________________________________________________"
                    + "________________________", blackBoldFontSize6);
            cell.addElement(p);
            subheaderTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setColspan(9);
            cell.setPaddingTop(0f);
            cell.addElement(sector_table());
            subheaderTable.addCell(cell);
        }
        return subheaderTable;
    }

    private PdfPTable sector_table() throws DocumentException {
        Paragraph p = null;
        PdfPTable sectorTable = new PdfPTable(11);
        PdfPCell sectorcell = null;
        sectorTable.setWidths(new float[]{1.43f, 0.34f, 1.3f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f});
        sectorTable.setWidthPercentage(100f);

        sectorcell = new PdfPCell();
        sectorcell.setBorder(0);
        sectorcell.setFixedHeight(20f);
        sectorcell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
        sectorcell.setBorderWidthBottom(0.6f);
        sectorTable.addCell(sectorcell);

        sectorcell = new PdfPCell();
        sectorcell.setBorder(0);
        sectorcell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
        sectorcell.setBorderWidthBottom(0.6f);
        sectorTable.addCell(sectorcell);

        sectorcell = new PdfPCell();
        sectorcell.setBorder(0);
        p = new Paragraph(6f, " 1   2   3   4", blackBoldFontSize6);
        sectorcell.addElement(p);
        p.setAlignment(Element.ALIGN_LEFT);
        sectorcell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
        sectorcell.setBorderWidthBottom(0.6f);
        sectorTable.addCell(sectorcell);

        sectorcell = new PdfPCell();
        sectorcell.setBorder(0);
        p = new Paragraph(6f, "SKIDS", blackBoldFontSize6);
        sectorcell.addElement(p);
        p.setAlignment(Element.ALIGN_LEFT);
        sectorcell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
        sectorcell.setBorderWidthBottom(0.6f);
        sectorTable.addCell(sectorcell);

        sectorcell = new PdfPCell();
        sectorcell.setBorder(0);
        p = new Paragraph(6f, "BOXES", blackBoldFontSize6);
        sectorcell.addElement(p);
        p.setAlignment(Element.ALIGN_LEFT);
        sectorcell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
        sectorcell.setBorderWidthBottom(0.6f);
        sectorTable.addCell(sectorcell);

        sectorcell = new PdfPCell();
        sectorcell.setBorder(0);
        p = new Paragraph(6f, "CRATES", blackBoldFontSize6);
        sectorcell.addElement(p);
        p.setAlignment(Element.ALIGN_LEFT);
        sectorcell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
        sectorcell.setBorderWidthBottom(0.6f);
        sectorTable.addCell(sectorcell);

        sectorcell = new PdfPCell();
        sectorcell.setBorder(0);
        p = new Paragraph(6f, "DRUMS", blackBoldFontSize6);
        sectorcell.addElement(p);
        p.setAlignment(Element.ALIGN_LEFT);
        sectorcell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
        sectorcell.setBorderWidthBottom(0.6f);
        sectorTable.addCell(sectorcell);

        sectorcell = new PdfPCell();
        sectorcell.setBorder(0);
        p = new Paragraph(6f, "BUNDLES", blackBoldFontSize6);
        sectorcell.addElement(p);
        p.setAlignment(Element.ALIGN_LEFT);
        sectorcell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
        sectorcell.setBorderWidthBottom(0.6f);
        sectorTable.addCell(sectorcell);

        sectorcell = new PdfPCell();
        sectorcell.setBorder(0);
        p = new Paragraph(6f, "PCS & DESC", blackBoldFontSize6);
        sectorcell.addElement(p);
        p.setAlignment(Element.ALIGN_LEFT);
        sectorcell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
        sectorcell.setBorderWidthBottom(0.6f);
        sectorTable.addCell(sectorcell);

        sectorcell = new PdfPCell();
        sectorcell.setBorder(0);
        p = new Paragraph(6f, "TOT PCS OUT", blackBoldFontSize6);
        sectorcell.addElement(p);
        p.setAlignment(Element.ALIGN_LEFT);
        sectorcell.setCellEvent(new DashedBorder(PdfPCell.RIGHT));
        sectorcell.setBorderWidthBottom(0.6f);
        sectorTable.addCell(sectorcell);

        sectorcell = new PdfPCell();
        sectorcell.setBorder(0);
        sectorcell.setBorderWidthBottom(0.6f);
        sectorTable.addCell(sectorcell);
        return sectorTable;
    }

    private PdfPTable totalCommodityCountsTable() throws DocumentException {
        Paragraph p = null;
        table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.3f, 1.1f, .9f, 1.7f, 8f});

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(0);
        p = new Paragraph("Totals:", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(0);
        p = new Paragraph("" + Math.round(totalvolumemeasure), blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell.addElement(p);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph("" + Math.round(totalweightmeasure), blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph("" + totalPieceCount, blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph("", blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public void createExportUnitReport(String realPath, String outputFileName, String fileId) throws Exception {
        init(outputFileName);
    }
}
