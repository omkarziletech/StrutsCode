package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.Element;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;

public class LclTruckOutboundPdfCreator extends LclReportFormatMethods implements LclCommonConstant {

    private LclUnitSs lclUnitSs;
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    private String vesselName = "";
    private String shipLine = "";
    private String voyageNumber = "";
    StringBuilder departPier = new StringBuilder();
    private String TPK01 = null;
    private String TPK02 = null;
    private String TPK03 = null;
    private String TPK04 = null;
    private String TPK05 = null;
    private String TPK06 = null;
    private String TPK07 = null;
    private String TPK08 = null;
    private String TPK09 = null;
    private String TPK10 = null;
    private String loaded_by = "";
    private String total_lbs = "";

    public LclTruckOutboundPdfCreator(LclUnitSs lclUnitSs) throws Exception {
        this.lclUnitSs = lclUnitSs;

        if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getScheduleNo())) {
            voyageNumber = lclUnitSs.getLclSsHeader().getScheduleNo();
        }
        if (CommonFunctions.isNotNull(lclUnitSs)) {
            LclUnitWhse unitWhse = new LclUnitWhseDAO().getLclUnitWhseFirstRecord(lclUnitSs.getLclUnit().getId(),
                    lclUnitSs.getLclSsHeader().getId());
            loaded_by = null != unitWhse && null != unitWhse.getStuffedByUser()
                    ? unitWhse.getStuffedByUser().getFirstName() + " " + unitWhse.getStuffedByUser().getLastName() : "";
            List commList = new LclUnitDAO().getCommodityValues(lclUnitSs.getId());
            if (!commList.isEmpty()) {
                Object[] obj = (Object[]) commList.get(0);
                if (obj[2] != null && !obj[2].toString().trim().equals("")) {
                    total_lbs = obj[2].toString() + " LBS";
                }
            }
        }
        LclSsDetail lclSsDetail = lclUnitSs.getLclSsHeader().getLclSsDetailList().get(0);
        if (CommonFunctions.isNotNull(lclSsDetail.getSpAcctNo()) && CommonFunctions.isNotNull(lclSsDetail.getSpAcctNo().getAccountName())) {
            vesselName = lclSsDetail.getSpAcctNo().getAccountName();
        }
        if ("E".equalsIgnoreCase(lclUnitSs.getLclSsHeader().getServiceType())) {
            if (CommonFunctions.isNotNull(lclSsDetail.getDeparture()) && CommonFunctions.isNotNull(lclSsDetail.getDeparture().getUnLocationName())) {
                departPier.append(lclSsDetail.getDeparture().getUnLocationName()).append(", ");
            }
            if (CommonFunctions.isNotNull(lclSsDetail.getDeparture().getStateId())
                    && CommonFunctions.isNotNull(lclSsDetail.getDeparture().getStateId().getCode())) {
                departPier.append(lclSsDetail.getDeparture().getStateId().getCode());
            }
        } else {
            if (CommonFunctions.isNotNull(lclSsDetail.getArrival()) && CommonFunctions.isNotNull(lclSsDetail.getArrival().getUnLocationName())) {
                departPier.append(lclSsDetail.getArrival().getUnLocationName()).append(", ");
            }
            if (CommonFunctions.isNotNull(lclSsDetail.getArrival().getStateId())
                    && CommonFunctions.isNotNull(lclSsDetail.getArrival().getStateId().getCode())) {
                departPier.append(lclSsDetail.getArrival().getStateId().getCode());
            }
        }
        printBookingComments();
    }

    public void printBookingComments() throws Exception {
        Iterator bookingCommentsIterator = genericCodeDAO.getLclPrintComments(39, "TPK");
        while (bookingCommentsIterator.hasNext()) {
            Object[] row = (Object[]) bookingCommentsIterator.next();
            String code = (String) row[0];
            String codeDesc = (String) row[1];
            if (code != null) {
                if ("TPK01".equalsIgnoreCase(code)) {
                    TPK01 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("TPK02".equalsIgnoreCase(code)) {
                    TPK02 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("TPK03".equalsIgnoreCase(code)) {
                    TPK03 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("TPK04".equalsIgnoreCase(code)) {
                    TPK04 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("TPK05".equalsIgnoreCase(code)) {
                    TPK05 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("TPK06".equalsIgnoreCase(code)) {
                    TPK06 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("TPK07".equalsIgnoreCase(code)) {
                    TPK07 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("TPK08".equalsIgnoreCase(code)) {
                    TPK08 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("TPK09".equalsIgnoreCase(code)) {
                    TPK09 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                } else if ("TPK10".equalsIgnoreCase(code)) {
                    TPK10 = CommonFunctions.isNotNull(codeDesc) ? codeDesc : "";
                }
            }
        }
    }

    public void createPdf(String realPath, String outputFileName, HttpServletRequest request) throws Exception {
        document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(8, 8, 8, 8);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        document.open();
        document.add(outboundDelivery(request));
        document.add(containerDetails());
        document.add(portDetails());
        document.add(voyageDetails());
        document.add(bookingDetails());
        document.add(inbondDetails());
        document.add(commentsTable());
        document.add(nameTable());
        document.add(signatureTable());
        document.add(footerTable());
        document.close();
    }

    public PdfPTable outboundDelivery(HttpServletRequest request) throws Exception {
        String companyName = "";
        String brand="";
        StringBuilder terminalDetails = new StringBuilder();
        StringBuilder phoneFax = new StringBuilder();
        User user = new User();
        HttpSession session = request.getSession();
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
        }
        String terminalNo = user.getTerminal().getTrmnum();
        RefTerminal refTerminal = new RefTerminalDAO().findById(terminalNo);
        if((lclUnitSs !=null && lclUnitSs.getLclSsHeader() !=null && !lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().isEmpty()) 
                && lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno() !=null) {
        brand =new TradingPartnerDAO().getBusinessUnit(lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno());
        }
       if (CommonUtils.isNotEmpty(brand)) {
            if ("ECI".equalsIgnoreCase(brand)) {
                companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
            } else if ("OTI".equalsIgnoreCase(brand)) {
                companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
            } else {
                companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");

            }
        }
        if (CommonFunctions.isNotNull(refTerminal)) {
//            if (CommonFunctions.isNotNull(refTerminal.getTrmnam())) {
//                companyName = refTerminal.getTrmnam();
//            }
            if (CommonFunctions.isNotNull(refTerminal.getAddres1())) {
                terminalDetails.append(refTerminal.getAddres1()).append("  ");
            }
            if (CommonFunctions.isNotNull(refTerminal.getCity1())) {
                terminalDetails.append(refTerminal.getCity1()).append(",  ");
            }
            if (CommonFunctions.isNotNull(refTerminal.getState())) {
                terminalDetails.append(refTerminal.getState()).append("  ");
            }
            if (CommonFunctions.isNotNull(refTerminal.getZipcde())) {
                terminalDetails.append(refTerminal.getZipcde());
            }
            if (CommonFunctions.isNotNull(refTerminal.getPhnnum1())) {
                String pNoSpaceRemove = StringUtils.remove(refTerminal.getPhnnum1(), " ");
                String ph1 = pNoSpaceRemove.substring(0, 3);
                String ph2 = pNoSpaceRemove.substring(3, 6);
                String ph3 = pNoSpaceRemove.substring(6);
                phoneFax.append("Tel. ").append("(").append(ph1).append(") ").append(ph2).append("-").append(ph3).append("  ");
            }
            if (CommonFunctions.isNotNull(refTerminal.getFaxnum1())) {
                String faxNoSpaceRemove = StringUtils.remove(refTerminal.getFaxnum1(), " ");
                String fax1 = faxNoSpaceRemove.substring(0, 3);
                String fax2 = faxNoSpaceRemove.substring(3, 6);
                String fax3 = faxNoSpaceRemove.substring(6);
                phoneFax.append("Fax ").append("(").append(fax1).append(") ").append(fax2).append("-").append(fax3);
            }
        }
        StringBuilder podValues = new StringBuilder();
        if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getUnLocationCode())) {
            podValues.append(lclUnitSs.getLclSsHeader().getDestination().getUnLocationCode()).append(" ");
        }
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(lclUnitSs.getLclSsHeader().getServiceType())) {
            if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getCountryId())
                    && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getCountryId().getCodedesc())) {
                podValues.append(lclUnitSs.getLclSsHeader().getDestination().getCountryId().getCodedesc()).append(", ");
            }
            if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName())) {
                podValues.append(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName());
            }
        } else if (LCL_IMPORT_TYPE.equalsIgnoreCase(lclUnitSs.getLclSsHeader().getServiceType())) {
            if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName())) {
                podValues.append(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName()).append(", ");
            }
            if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getCountryId())
                    && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getCountryId().getCodedesc())) {
                podValues.append(lclUnitSs.getLclSsHeader().getDestination().getStateId().getCode());
            }
        }
        table = new PdfPTable(3);
        table.setWidths(new float[]{1f, 1f, 5f});
        table.setWidthPercentage(100f);
        table.addCell(makeCellNoBorderFont("Port: " + podValues.toString().toUpperCase(), -2f, 3, blackBoldCourierFont10f));
        Chunk underline = null;
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingTop(-7f);
        cell.setPaddingLeft(200f);
        cell.setColspan(3);
        underline = new Chunk("OUT-BOUND DELIVERY RECEIPT", blackBoldCourierFont10f);
        underline.setUnderline(0.6f, -2f);
        cell.addElement(underline);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingTop(-5f);
        cell.setPaddingLeft(150f);
        cell.setColspan(3);
        underline = new Chunk("SAFE CONTAINER ACT SHIPPER CERTIFICATION", blackBoldCourierFont10f);
        underline.setUnderline(0.6f, -2f);
        cell.addElement(underline);
        table.addCell(cell);
        table.addCell(makeCellNoBorderFont("  ", -2f, 3, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("SHIPPER:  ", -2f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("" + companyName.replace(" ", "").replace("", " ").trim(), -2f, 2, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("  ", -2f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont(" ", -2f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("" + terminalDetails.toString().toUpperCase(), -2f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont(" ", -2f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont(" ", -2f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("" + phoneFax.toString(), -2f, 0, blackBoldCourierFont10f));
        return table;
    }

    public PdfPTable containerDetails() throws Exception {

        String containerNo = "";
        String sealNo = "";
        if (CommonFunctions.isNotNull(lclUnitSs)) {
            if (CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getUnitNo())) {
                containerNo = lclUnitSs.getLclUnit().getUnitNo();
            }
        }
        if (CommonFunctions.isNotNull(lclUnitSs)) {
            if (CommonFunctions.isNotNull(lclUnitSs.getSUHeadingNote())) {
                sealNo = lclUnitSs.getSUHeadingNote();
            }
        }
        table = new PdfPTable(6);
        table.setWidths(new float[]{1.35f, .95f, 1.7f, 1.5f, 1.2f, 0.5f});
        table.setWidthPercentage(100f);
        table.addCell(makeCellNoBorderFont(" ", 3f, 6, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("", 1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("THIS TRAILER/CONTAINER CONTAINS FREIGHT OF ALL KINDS", 0f, 5, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("Trailer/Container Number:  ", -3f, 2, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("" + containerNo, 0, -3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("  Seal Number:  ", -3f, 0, blackBoldCourierFont10f));
        if (CommonUtils.isNotEmpty(sealNo)) {
            table.addCell(makeCellBottomBorderValue("" + sealNo.toUpperCase(), 0, -3f, 0.6f, blackBoldCourierFont10f));
        } else {
            table.addCell(makeCellBottomBorderValue("", 0, -3f, 0.6f, blackBoldCourierFont10f));
        }
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        return table;
    }

    public PdfPTable portDetails() throws Exception {
        table = new PdfPTable(4);
        table.setWidths(new float[]{1.7f, 4.5f, 1f, 2.5f});
        table.setWidthPercentage(100f);
        table.addCell(makeCellNoBorderFont("Delivered TO:    ", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("" + vesselName, 0, -3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("  Pier:  ", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("" + departPier.toString().toUpperCase(), 0, -3f, 0.6f, blackBoldCourierFont10f));
        return table;
    }

    public PdfPTable voyageDetails() throws Exception {
        StringBuilder podValues = new StringBuilder();
        if (LCL_EXPORT_TYPE.equalsIgnoreCase(lclUnitSs.getLclSsHeader().getServiceType())) {
            if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getCountryId())
                    && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getCountryId().getCodedesc())) {
                podValues.append(lclUnitSs.getLclSsHeader().getDestination().getCountryId().getCodedesc()).append(", ");
            }
            if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName())) {
                podValues.append(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName());
            }
        } else if (LCL_IMPORT_TYPE.equalsIgnoreCase(lclUnitSs.getLclSsHeader().getServiceType())) {
            if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName())) {
                podValues.append(lclUnitSs.getLclSsHeader().getDestination().getUnLocationName()).append(", ");
            }
            if (CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getStateId())
                    && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getDestination().getStateId().getCode())) {
                podValues.append(lclUnitSs.getLclSsHeader().getDestination().getStateId().getCode());
            }
        }
        table = new PdfPTable(5);
        table.setWidths(new float[]{2.5f, 3.1f, 1.5f, 1.1f, 0.5f});
        table.setWidthPercentage(100f);
        table.addCell(makeCellNoBorderFont("Port TO Be Shipped To: ", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("" + podValues.toString().toUpperCase(), 0, -3f, 0.6f, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont(" Voyage #: ", -3f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellBottomBorderValue("" + voyageNumber, 0, -3f, 0.6f, blackBoldCourierFont10f));
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);
        //empty space
        table.addCell(createEmptyCell(0, 4f, 5));
        return table;
    }

    public PdfPTable bookingDetails() throws Exception {
        table = new PdfPTable(2);
        Paragraph p = null;
        table.setWidths(new float[]{4f, 3f});
        table.setWidthPercentage(100f);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(6f, "Certified Gross Cargo Weight: ", blackBoldCourierFont10f);
        p.add(new Chunk(total_lbs).setUnderline(0.3f, -2f));
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, "Booking# ", blackBoldCourierFont10f);
        if (CommonUtils.isNotEmpty(lclUnitSs.getSpBookingNo())) {
            p.add(new Chunk(lclUnitSs.getSpBookingNo()).setUnderline(0.3f, -2f));
        }
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    public PdfPTable inbondDetails() throws Exception {
        int count = 0;
        String fileNumber = new String();
        StringBuilder inbondValues = new StringBuilder();
        String concatenatedFileNumber = new String();
        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{.5f, 3f, 0.8f});
        table.addCell(makeCellNoBorderFont("Inbonds", -3f, 3, blackBoldCourierFont10f));
        List<LclBookingPieceUnit> lclBookingPieceUnitsList = lclUnitSs.getLclBookingPieceUnitList();
        for (LclBookingPieceUnit lclBookingPieceUnits : lclBookingPieceUnitsList) {
            fileNumber = lclBookingPieceUnits.getLclBookingPiece().getLclFileNumber().getId().toString();
            if (!concatenatedFileNumber.contains(fileNumber)) {
                concatenatedFileNumber += fileNumber + ",";
                List<LclInbond> lclInbondsList = lclBookingPieceUnits.getLclBookingPiece().getLclFileNumber().getLclInbondList();
                if (lclInbondsList != null && lclInbondsList.size() > 0) {
                    for (int i = 0; i < lclInbondsList.size(); i++) {
                        LclInbond lclInbond = (LclInbond) lclInbondsList.get(i);
                        if (CommonFunctions.isNotNull(lclInbond.getInbondType())) {
                            inbondValues.append(lclInbond.getInbondType());
                        }
                        if (CommonFunctions.isNotNull(lclInbond.getInbondNo())) {
                            inbondValues.append(lclInbond.getInbondNo()).append("          ");
                        }

                    }
                    count++;
                    table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
                    //first cell
                    table.addCell(makeCellNoBorderFont("" + inbondValues.toString().toUpperCase(), -3f, 0, blackBoldCourierFont10f));
                    inbondValues.setLength(0);
                    table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
                }
            }

        }

        //empty space
        for (int i = 0; i < (14 - count); i++) {
            table.addCell(createEmptyCell(0, 1f, 5));
        }
        return table;
    }

    public PdfPTable commentsTable() throws Exception {
        Paragraph p = null;
        String comment1 = TPK01 != null ? TPK01 : "";
        String comment2 = TPK02 != null ? TPK02 : "";
        String comment3 = TPK03 != null ? TPK03 : "";
        String comment4 = TPK04 != null ? TPK04 : "";
        String comment5 = TPK05 != null ? TPK05 : "";
        String comment6 = TPK06 != null ? TPK06 : "";
        String comment7 = TPK07 != null ? TPK07 : "";
        String comment8 = TPK08 != null ? TPK08 : "";
        String comment9 = TPK09 != null ? TPK09 : "";
        String comment10 = TPK10 != null ? TPK10 : "";
        table = new PdfPTable(2);
        table.setWidths(new float[]{0.2f, 6f});
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBorderWidthTop(0.9f);
        p = new Paragraph("CONTAINER PACKING CERTIFICATE", blackBoldCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //empty space
        table.addCell(createEmptyCell(0, 2f, 3));
        //comments
        table.addCell(makeCellNoBorderFont("1.", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("" + comment1, -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("2.", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("" + comment2, -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("3.", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("" + comment3, -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("4.", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("" + comment4, -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("5.", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("" + comment5, -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("6.", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("" + comment6, -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("7.", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("" + comment7, -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("8.", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("" + comment8, -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("9.", -1f, 0, blackBoldCourierFont10f));
        table.addCell(makeCellNoBorderFont("" + comment9, -1f, 0, blackBoldCourierFont10f));
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBorderWidthTop(0.9f);
        table.addCell(cell);
        //empty space
        table.addCell(createEmptyCell(0, 2f, 3));
        //comments
        table.addCell(makeCellNoBorderFont("" + comment10, -1f, 3, blackBoldCourierFont10f));
        return table;
    }

    public PdfPTable nameTable() throws Exception {
        Paragraph p = null;
        table = new PdfPTable(5);
        table.setWidths(new float[]{1.7f, 3.5f, 0.6f, 1.5f, 0.9f});
        table.setWidthPercentage(100f);
        //empty space
        table.addCell(createEmptyCell(0, 2f, 5));
        table.addCell(makeCellNoBorderFont("NAME OF PACKER: ", -3f, 0, blackBoldCourierFont10f));
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-8f);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph("" + loaded_by, blackBoldCourierFont10f);
        cell.addElement(p);
        table.addCell(cell);
        table.addCell(makeCellNoBorderFont(" DATE ", -3f, 0, blackBoldCourierFont10f));
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);
        //empty space
        table.addCell(createEmptyCell(0, 2f, 5));

        return table;
    }

    public PdfPTable signatureTable() throws Exception {
        Paragraph p = null;
        table = new PdfPTable(3);
        table.setWidths(new float[]{2.1f, 2.8f, 2.7f});
        table.setWidthPercentage(100f);
        //empty space
        table.addCell(createEmptyCell(0, 2f, 3));
        table.addCell(makeCellNoBorderFont("SIGNATURE OF PACKER: ", -3f, 0, blackBoldCourierFont10f));
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-8f);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph("", blackBoldCourierFont10f);
        cell.addElement(p);
        table.addCell(cell);
        table.addCell(makeCellNoBorderFont("", -3f, 0, blackBoldCourierFont10f));
        //empty space
        table.addCell(createEmptyCell(0, 2f, 3));

        return table;
    }

    public PdfPTable footerTable() throws Exception {
        Paragraph p = null;
        table = new PdfPTable(3);
        table.setWidths(new float[]{3.5f, 2.8f, 1.5f});
        table.setWidthPercentage(100f);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.09f);
        p = new Paragraph("SHIPPER", blackBoldCourierFont10f);
        p.setLeading(6f);
        cell.setPaddingTop(3f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setBorderWidthTop(0.09f);
        cell.setBorderWidthLeft(0.09f);
        p = new Paragraph(" CARRIER    ", blackBoldCourierFont10f);
        p.setLeading(6f);
        cell.setPaddingTop(3f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.09f);
        cell.setPadding(15f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        if (null != lclUnitSs.getLclUnit() && CommonUtils.isNotEmpty(lclUnitSs.getLclUnit().getRemarks())) {
            p = new Paragraph(9f, "" + lclUnitSs.getLclUnit().getRemarks(), blackBoldCourierFont10f);
            cell.addElement(p);
            cell.setPaddingLeft(10f);
        }
        cell.setPaddingTop(3f);
        cell.setBorderWidthBottom(0.09f);
        cell.setBorderWidthLeft(0.09f);
        table.addCell(cell);

        //
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.09f);
        p = new Paragraph("", blackBoldCourierFont10f);
        p.setLeading(6f);
        cell.setPaddingTop(3f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.09f);
        cell.setBorderWidthLeft(0.09f);
        p = new Paragraph(" PER  (Driver's Signature)", blackBoldCourierFont10f);
        p.setLeading(6f);
        cell.setPaddingTop(3f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.09f);
        cell.setBorderWidthLeft(0.09f);
        p = new Paragraph(" DATE:", blackBoldCourierFont10f);
        p.setLeading(6f);
        cell.setPaddingTop(3f);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(15f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.09f);
        cell.setBorderWidthLeft(0.09f);
        cell.setPadding(5f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.09f);
        cell.setBorderWidthLeft(0.09f);
        cell.setPadding(5f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setBorder(0);
        Chunk underline = new Chunk("OBSERVATIONS", blackBoldCourierFont10f);
        underline.setUnderline(1f, -2f);
        cell.setPaddingTop(3f);
        cell.addElement(underline);
        table.addCell(cell);
        return table;
    }
}
