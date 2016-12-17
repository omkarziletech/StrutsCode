package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
import com.gp.cong.logisoft.domain.lcl.LclBookingHsCode;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsExports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class LclMasterBLPdfCreator extends LclReportFormatMethods {

    String shipperName = "";
    String bookingNo = "";
    String shipperAddress = "";
    String consigneeName = "";
    String consigneeAddress = "";
    String notifyName = "";
    String notifyAddress = "";
    StringBuilder vesselDetails = new StringBuilder();
    StringBuilder origin = new StringBuilder();
    StringBuilder destination = new StringBuilder();
    StringBuilder releaseClause = new StringBuilder();
    String preCollValues = null;
    String frecollpreValues = null;
    String exportRef = "";
    StringBuilder deptPier = new StringBuilder();
    StringBuilder arrivalPier = new StringBuilder();
    String originunlocationcode = "";
    String voyageNo = "";
    boolean printViaMaseterBl = false;
    private static final Logger log = Logger.getLogger(LclMasterBLPdfCreator.class);
    private LclSSMasterBl lclSSMasterBl;
    private int unitCount = 0;
    private List<LclUnitSs> unitSsList = null;

    public LclMasterBLPdfCreator(LclSSMasterBl lclSSMasterBl) {
        this.lclSSMasterBl = lclSSMasterBl;
    }

    class TableHeader extends PdfPageEventHelper {

        String header;
        PdfTemplate total;

        public void setHeader(String header) {
            this.header = header;
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(30, 16);
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                Rectangle rect = new Rectangle(590, 840, 5, 5, 5);
                rect.setBorder(Rectangle.BOX);
                rect.setBorderWidth(0.6f);
                document.add(rect);

            } catch (DocumentException ex) {
                log.info("Exception on class LclMasterBLPdfCreator in method onEndPage" + new Date(), ex);
            }

        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);
        }
    }

    public void setSsMasterBL() throws Exception {
        origin.append("                ").append(lclSSMasterBl.getLclSsHeader().getOrigin().getUnLocationName());
        if (null != lclSSMasterBl.getLclSsHeader().getOrigin().getStateId()) {
            origin.append(", ").append(lclSSMasterBl.getLclSsHeader().getOrigin().getStateId().getCode());
        }
        destination.append("    ").append(lclSSMasterBl.getLclSsHeader().getDestination().getUnLocationName());
        if (null != lclSSMasterBl.getLclSsHeader().getDestination().getStateId()
                && CommonUtils.isNotEmpty(lclSSMasterBl.getLclSsHeader().getDestination().getStateId().getCode())) {
            destination.append(", ").append(lclSSMasterBl.getLclSsHeader().getDestination().getStateId().getCode());
        } else {
            destination.append(", ").append(lclSSMasterBl.getLclSsHeader().getDestination().getCountryId().getCodedesc());
        }
        LclSsDetail lclssDetail = lclSSMasterBl.getLclSsHeader().getVesselSsDetail();
        LclSsExports lclSsExport = lclSSMasterBl.getLclSsHeader().getLclSsExports();
        voyageNo = lclSSMasterBl.getLclSsHeader().getScheduleNo();
        if (null != lclssDetail) {
            if (CommonFunctions.isNotNull(lclssDetail.getSpReferenceName())) {
                vesselDetails.append("    ").append(lclssDetail.getSpReferenceName()).append("      ");
            }
            if (CommonFunctions.isNotNull(lclssDetail.getSpReferenceNo())) {
                vesselDetails.append("VOY  ").append(lclssDetail.getSpReferenceNo()).append("       ");
            }
            if (CommonFunctions.isNotNull(lclssDetail.getSpAcctNo())
                    && CommonFunctions.isNotNull(lclssDetail.getSpAcctNo().getAccountName())) {
                vesselDetails.append("S/S Line  ").append(lclssDetail.getSpAcctNo().getAccountName());
            }
            if (CommonFunctions.isNotNull(lclssDetail.getDeparture())
                    && CommonFunctions.isNotNull(lclssDetail.getDeparture().getUnLocationName())) {
                deptPier.append(lclssDetail.getDeparture().getUnLocationName()).append(", ");
            }
            if (CommonFunctions.isNotNull(lclssDetail.getDeparture())
                    && CommonFunctions.isNotNull(lclssDetail.getDeparture().getStateId())
                    && CommonFunctions.isNotNull(lclssDetail.getDeparture().getStateId().getCode())) {
                deptPier.append(lclssDetail.getDeparture().getStateId().getCode());
            }
            if (CommonFunctions.isNotNull(lclssDetail.getArrival())
                    && CommonFunctions.isNotNull(lclssDetail.getArrival().getUnLocationName())) {
                arrivalPier.append(lclssDetail.getArrival().getUnLocationName()).append(", ");
            }

            if (CommonFunctions.isNotNull(lclssDetail.getArrival())
                    && CommonFunctions.isNotNull(lclssDetail.getArrival().getCountryId())
                    && CommonFunctions.isNotNull(lclssDetail.getArrival().getCountryId().getCodedesc())) {
                arrivalPier.append(lclssDetail.getArrival().getCountryId().getCodedesc());
            }
        }
        if (null != lclSsExport) {
            printViaMaseterBl = lclSsExport.isPrintViaMasterbl();
        }
        if (CommonFunctions.isNotNull(lclSSMasterBl.getSpBookingNo())) {
            bookingNo = lclSSMasterBl.getSpBookingNo();
        }
        if (CommonFunctions.isNotNull(lclSSMasterBl.getShipSsContactId()) && CommonFunctions.isNotNull(lclSSMasterBl.getShipSsContactId().getCompanyName())) {
            shipperName = lclSSMasterBl.getShipSsContactId().getCompanyName();
        }
        if (CommonFunctions.isNotNull(lclSSMasterBl.getShipEdi())) {
            shipperAddress = lclSSMasterBl.getShipEdi();
        }
        if (CommonFunctions.isNotNull(lclSSMasterBl.getConsSsContactId()) && CommonFunctions.isNotNull(lclSSMasterBl.getConsSsContactId().getCompanyName())) {
            consigneeName = lclSSMasterBl.getConsSsContactId().getCompanyName();
        }
        if (CommonFunctions.isNotNull(lclSSMasterBl.getConsEdi())) {
            consigneeAddress = lclSSMasterBl.getConsEdi();
        }
        if (CommonFunctions.isNotNull(lclSSMasterBl.getNotySsContactId()) && CommonFunctions.isNotNull(lclSSMasterBl.getNotySsContactId().getCompanyName())) {
            notifyName = lclSSMasterBl.getNotySsContactId().getCompanyName();
        }
        if (CommonFunctions.isNotNull(lclSSMasterBl.getNotyEdi())) {
            notifyAddress = lclSSMasterBl.getNotyEdi();
        }
        if (CommonFunctions.isNotNull(lclSSMasterBl.getReleaseClause())) {
            releaseClause.append(lclSSMasterBl.getReleaseClause().getCodedesc());
        }
        if (CommonFunctions.isNotNull(lclSSMasterBl.getExportRefEdi())) {
            exportRef = lclSSMasterBl.getExportRefEdi();
        }
        String prepaidCollect = lclSSMasterBl.getPrepaidCollect();
        preCollValues = "All Destination Charges " + ("C".equalsIgnoreCase(lclSSMasterBl.getDestPrepaidCollect()) ? "Collect" : "Prepaid");
        if (prepaidCollect.equalsIgnoreCase("P")) {
            frecollpreValues = "*** FREIGHT PREPAID ***";
        } else {
            frecollpreValues = "*** FREIGHT COLLECT ***";
        }
        originunlocationcode = lclSSMasterBl.getLclSsHeader().getOrigin().getUnLocationCode().substring(2);
    }

    public void createMasterPdf(String realPath, String outputFileName) throws Exception, DocumentException, SQLException {
        document = new Document(PageSize.A4, 8, 12, 8, 40);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        TableHeader event = new TableHeader();
        pdfWriter.setPageEvent(event);
        document.open();
        setSsMasterBL();
        document.add(getStartTable());
        unitSsList = new LclSSMasterBlDAO().getUnitSsListLinkWithMaster(lclSSMasterBl.getLclSsHeader().getId(), lclSSMasterBl.getSpBookingNo());
        if (null != unitSsList && !unitSsList.isEmpty()) {
            for (LclUnitSs unitSS : unitSsList) {
                document.add(createBody(unitSS));
            }
        }
        document.close();
    }

    public PdfPTable createBody(LclUnitSs unitSS) throws DocumentException, Exception {
        LclReportUtils lclReportUtils = new LclReportUtils();
        Paragraph p = null;
        unitCount++;
        table = new PdfPTable(3);
        table.setWidths(new float[]{4f, 6f, 2.5f});
        table.setWidthPercentage(100f);

        cell = new PdfPCell(new Phrase(""));
        cell.setColspan(3);
        cell.setBorder(0);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setPaddingTop(15f);
        p = new Paragraph(6f, "CONTINUED ON NEXT PAGE", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setColspan(3);
        cell.setBorder(0);
        table.addCell(cell);
        table.setHeaderRows(2);
        table.setFooterRows(2);
        table.setSkipFirstHeader(true);
        table.setSkipLastFooter(true);

        String fileNumber = "";
        String avoidDuplicateFile = "";
        String sealValues = CommonUtils.isNotEmpty(unitSS.getSUHeadingNote()) ? unitSS.getSUHeadingNote().toUpperCase() : "";
        String unitSize = CommonUtils.isNotEmpty(unitSS.getLclUnit().getUnitType().getShortDesc())
                ? unitSS.getLclUnit().getUnitType().getShortDesc() : unitSS.getLclUnit().getUnitType().getDescription();
        List<LclBookingPieceUnit> pieceUnitList = unitSS.getLclBookingPieceUnitList();

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, unitSS.getLclUnit().getUnitNo() + "        " + unitSize.toUpperCase() + "\n", blackboldCourierFont11f);
        p.add(new Paragraph(10f, "SEAL  " + sealValues, blackboldCourierFont11f));
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(10f, CommonUtils.isNotEmpty(unitSS.getTotalPieces()) ? "STC " + unitSS.getTotalPieces() + " PACKAGES" + "\n" : "" + " \n", blackNormalCourierFont10f);
        p.add(new Paragraph(10f, CommonUtils.isNotEmpty(unitSS.getBlBody()) ? unitSS.getBlBody().toUpperCase() : "" + "\n", blackNormalCourierFont10f));
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        BigDecimal CFT = null != unitSS.getVolumeImperial() ? unitSS.getVolumeImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        BigDecimal LBS = null != unitSS.getWeightImperial() ? unitSS.getWeightImperial().setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

        p = new Paragraph(10f, null != unitSS.getVolumeMetric() ? unitSS.getVolumeMetric().toString() + "  CBM\n" : "0  CBM\n", blackboldCourierFont11f);
        p.add(new Paragraph(10f, null != unitSS.getWeightMetric() ? unitSS.getWeightMetric().toString() + "  KGS\n" : "0  KGS\n", blackboldCourierFont11f));
        p.add(new Paragraph(10f, CFT + "  CFT", blackboldCourierFont11f));
        p.add(new Paragraph(10f, LBS + "  LBS", blackboldCourierFont11f));
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        StringBuilder packageValues = new StringBuilder();
        List<Long> totalFileNumberList = new ArrayList<Long>();
        Map<String, StringBuilder> hazmatMap = new HashMap<String, StringBuilder>();
        packageValues.append(unitSS.getTotalPieces()).append("  ");
        for (LclBookingPieceUnit pieceUnit : pieceUnitList) {
            StringBuilder hazmatValues = new StringBuilder();
            LclBookingPiece bkgPiece = pieceUnit.getLclBookingPiece();
            if (!avoidDuplicateFile.equalsIgnoreCase(bkgPiece.getLclFileNumber().getFileNumber())) {
                avoidDuplicateFile = bkgPiece.getLclFileNumber().getFileNumber();
                fileNumber = "Dock Receipt:" + originunlocationcode + "-" + bkgPiece.getLclFileNumber().getFileNumber() + "\n";
                totalFileNumberList.add(bkgPiece.getLclFileNumber().getId());
                List<LclBookingHazmat> lclBookingHazmatList = bkgPiece.getLclBookingHazmatList();
                for (int i = 0; i < lclBookingHazmatList.size(); i++) {
                    LclBookingHazmat lclBookingHazmat = (LclBookingHazmat) lclBookingHazmatList.get(i);
                    hazmatValues.append(lclReportUtils.concateHazmatDetails(lclBookingHazmat)).append("\n\n");
                }
                if (CommonUtils.isNotEmpty(hazmatValues)) {
                    hazmatMap.put(fileNumber, hazmatValues);
                }

            }
        }
        PdfPTable aestable = new PdfPTable(3);
        PdfPTable inbondtable = new PdfPTable(4);
        PdfPTable hsCodeTable = new PdfPTable(4);

        List<LclInbond> inbondList = null;
        List<Lcl3pRefNo> aesList = null;
        List<LclBookingHsCode> bookingHsCodesList = null;
        if (CommonUtils.isNotEmpty(totalFileNumberList)) {
            Lcl3pRefNoDAO _3pRefNoDAO = new Lcl3pRefNoDAO();
            LclInbondsDAO inbondDAO = new LclInbondsDAO();
            LclBookingHsCodeDAO bkgHsCodeDAO = new LclBookingHsCodeDAO();

            bookingHsCodesList = bkgHsCodeDAO.getHsCodeList(totalFileNumberList);
            if (null != bookingHsCodesList && !bookingHsCodesList.isEmpty()) {
                hsCodeTable.setWidthPercentage(100);
                int size = bookingHsCodesList.size() % 4;
                for (LclBookingHsCode bookingHsCode : bookingHsCodesList) {
                    hsCodeTable.addCell(createTextCell(bookingHsCode.getCodes(),
                            7f, 0f, 0, blackNormalCourierFont10f));
                }
                if (size > 0) {
                    hsCodeTable.addCell(createEmptyCell(0, 1, 4 - size));
                }
            }

            aesList = _3pRefNoDAO.getAesExcepList(totalFileNumberList, LclReportConstants.AES_NUMBER);
            if (null != aesList && !aesList.isEmpty()) {
                int size = aesList.size() % 3;
                aestable.setWidthPercentage(100);
                for (Lcl3pRefNo aes : aesList) {
                    aestable.addCell(createTextCell(aes.getReference().toUpperCase(), 7f, 0f, 0, blackNormalCourierFont10f));
                }
                if (size > 0) {
                    aestable.addCell(createEmptyCell(0, 1, 3 - size));
                }
            }
            inbondList = inbondDAO.getInbondList(totalFileNumberList);
            if (null != inbondList && !inbondList.isEmpty()) {
                inbondtable.setWidthPercentage(100);
                int size = inbondList.size() % 4;
                for (LclInbond inbond : inbondList) {
                    inbondtable.addCell(createTextCell(inbond.getInbondType() + "," + inbond.getInbondNo(),
                            7f, 0f, 0, blackNormalCourierFont10f));
                }
                if (size > 0) {
                    inbondtable.addCell(createEmptyCell(0, 1, 4 - size));
                }
            }
        }
        if (CommonUtils.isNotEmpty(hazmatMap)) {
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setColspan(3);
            p = new Paragraph(7f, "_____________________________________H A Z A R D O U S____________________________________\n\n", blackNormalCourierFont10f);
            cell.addElement(p);
            cell.setPaddingTop(18f);
            table.addCell(cell);

            for (String key : hazmatMap.keySet()) {
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setColspan(3);
                p = new Paragraph(9f, "", blackNormalCourierFont10f);
                p.add(new Chunk(key, blackBoldCourierFont10f).setUnderline(0.6f, -3f));
                cell.addElement(p);
                table.addCell(cell);
                String[] hazmatArray = hazmatMap.get(key).toString().split("\n\n");
                for (String value : hazmatArray) {
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    cell.setColspan(3);
                    p = new Paragraph(9f, "" + value.toUpperCase(), blackNormalCourierFont10f);
                    p.setAlignment(Element.ALIGN_LEFT);
                    cell.addElement(p);
                    table.addCell(cell);
                }
            }

        }

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        p = new Paragraph(7f, "CHEMTREC CONTRACTED BY ECONOCARIBE CONSOLIDATORS INC", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(18f);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        p = new Paragraph(7f, "CONTRACT/CUSTOMER # CCN7371", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        p = new Paragraph(7f, "EMERGENCY CONTACT:", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        p = new Paragraph(7f, "Within USA and Canada: 1-800-424-9300", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        p = new Paragraph(7f, "Outside USA and Canada: +1 703-527-3887 (collect calls accepted)", blackNormalCourierFont10f);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        if (CommonUtils.isNotEmpty(aesList)) {
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setColspan(3);
            p = new Paragraph(7f, "_________________________________________A. E. S._________________________________________\n\n", blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
            cell.addElement(aestable);
            table.addCell(cell);
        }
        if (null != bookingHsCodesList && !bookingHsCodesList.isEmpty()) {
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setColspan(3);
            p = new Paragraph(9f, "________________________________________HS-CODES__________________________________________\n\n", blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
            cell.addElement(hsCodeTable);
            table.addCell(cell);
        }

        if (null != inbondList && !inbondList.isEmpty()) {
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setColspan(3);
            p = new Paragraph(9f, "_____________________________________I N B O N D S________________________________________\n\n", blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
            cell.addElement(inbondtable);
            table.addCell(cell);
        }
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        if (unitSsList.size() != unitCount) {
            p = new Paragraph(7f, "= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =", blackNormalCourierFont10f);
            p.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p);
        }
        table.addCell(cell);
        return table;
    }

    public PdfPTable getStartTable() throws DocumentException, Exception {
        table = new PdfPTable(2);
        table.setWidths(new float[]{3f, 2f});
        table.setWidthPercentage(100f);
        table.addCell(makeCellNoBorderFontalign(lclSSMasterBl.isPrintDockRecipt() ? "DOCK RECEIPT" : "MASTER BILL OF LADING", 2f, Element.ALIGN_CENTER, 2, blackNormalCourierFont10f));
        table.addCell(createEmptyCell(0, 1, 2));
        //shipper details
        table.addCell(makeCellNoBorderFontalign("" + shipperName.toUpperCase(), 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("" + bookingNo.toUpperCase(), 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("" + shipperAddress.toUpperCase(), 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("CONTRACT#  " + new LclSsDetailDAO().getContractNumber(lclSSMasterBl.getLclSsHeader().getId()), 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));

        table.addCell(createEmptyCell(0, 1, 2));
        //consignee details
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        String companyName = systemRulesDAO.getSystemRulesByCode("CompanyNameMnemonic");
        String company = !"OTI".equalsIgnoreCase(companyName) ? "REF#" : "REF#";

        table.addCell(makeCellNoBorderFontalign("" + consigneeName, 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("", 2f, Element.ALIGN_LEFT, 2, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("" + consigneeAddress, 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("" + company + " " + voyageNo, 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        //Empty Cell
        table.addCell(createEmptyCell(0, 1, 2));
        String totalInvoiceValue = "";
        if (lclSSMasterBl.isInvoiceValue()) {
            totalInvoiceValue = "VALUE OF GOODS USD :$";
            totalInvoiceValue += new LclSSMasterBlDAO().getMasterBlInvoiceValueTotal(lclSSMasterBl.getLclSsHeader().getId().toString(), lclSSMasterBl.getSpBookingNo());
        }
        table.addCell(makeCellNoBorderFontalign("" + notifyName.toUpperCase(), 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("" + totalInvoiceValue, 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("" + notifyAddress.toUpperCase(), 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("   ", 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));

        table.addCell(createEmptyCell(0, 1, 2));

        table.addCell(makeCellNoBorderFontalign("", 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("   " + frecollpreValues, 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("", -1f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("" + preCollValues, -1f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        table.addCell(createEmptyCell(0, 1, 2));

        if (null != lclSSMasterBl && CommonUtils.isNotEmpty(lclSSMasterBl.getExportRefEdi())) {
            table.addCell(makeCellNoBorderFontalign("", -1f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
            table.addCell(makeCellNoBorderFontalign("" + lclSSMasterBl.getExportRefEdi().toUpperCase(), -1f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
            table.addCell(createEmptyCell(0, 1, 2));
        }
        table.addCell(makeCellNoBorderFontalign("" + origin.toString().toUpperCase(), 2f, Element.ALIGN_LEFT, 0, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("              U.S.A.", 2f, Element.ALIGN_CENTER, 0, blackNormalCourierFont10f));

        table.addCell(makeCellNoBorderFontalign("" + vesselDetails.toString().toUpperCase(), 2f, Element.ALIGN_LEFT, 2, blackNormalCourierFont10f));
        table.addCell(makeCellNoBorderFontalign("" + deptPier.toString().toUpperCase(), -1f, Element.ALIGN_CENTER, 2, blackNormalCourierFont10f));
        table.addCell(createEmptyCell(0, 1, 2));
        if (printViaMaseterBl) {
            table.addCell(makeCellNoBorderFontalign("" + destination.toString().toUpperCase() + " " + "Via" + " " + arrivalPier.toString().toUpperCase(), 2f, Element.ALIGN_LEFT, 2, blackNormalCourierFont10f));
            table.addCell(createEmptyCell(0, 1, 2));
        } else {
            table.addCell(makeCellNoBorderFontalign("" + destination.toString().toUpperCase(), 2f, Element.ALIGN_LEFT, 2, blackNormalCourierFont10f));
            table.addCell(createEmptyCell(0, 1, 2));

        }

        return table;
    }
//    public PdfPTable getEndTable() throws DocumentException, Exception {
//        table = new PdfPTable(2);
//        table.setWidths(new float[]{3f, 2f});
//        table.setWidthPercentage(100f);
//
//        table.addCell(createEmptyCell(0, 1, 2));
//        //Adding Release Clauses
//        String specialInst = CommonUtils.isNotEmpty(lclSSMasterBl.getRemarks()) ? lclSSMasterBl.getRemarks() : "";
//        table.addCell(makeCellNoBorderFontalign("" + specialInst, 2f, Element.ALIGN_CENTER, 2, blackNormalCourierFont10f));
//        table.addCell(createEmptyCell(0, 1, 2));
//        //emptycell
//        table.addCell(createEmptyCell(0, 5f, 2));
//        //content details
//        table.addCell(makeCellNoBorderFontalign("CHEMTREC CONTRACTED BY ECONOCARIBE CONSOLIDATORS INC", -3f, Element.ALIGN_CENTER, 2, blackBoldCourierFont10f));
//        table.addCell(makeCellNoBorderFontalign("CONTRACT/CUSTOMER # CCN7371", -3f, Element.ALIGN_CENTER, 2, blackBoldCourierFont10f));
//        table.addCell(makeCellNoBorderFontalign("EMERGENCY CONTACT:", -3f, Element.ALIGN_CENTER, 4, blackBoldCourierFont10f));
//        table.addCell(makeCellNoBorderFontalign("Within USA and Canada: 1-800-424-9300", -3f, Element.ALIGN_CENTER, 2, blackBoldCourierFont10f));
//        table.addCell(makeCellNoBorderFontalign("Outside USA and Canada: +1 703-527-3887 (collect calls accepted)", -3f, Element.ALIGN_CENTER, 2, blackBoldCourierFont10f));
//        return table;
//    }
}
