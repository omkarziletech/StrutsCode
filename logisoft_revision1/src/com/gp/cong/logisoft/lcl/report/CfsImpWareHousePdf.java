package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.beans.BookingChargesBean;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author NambuRajasekar
 */
public class CfsImpWareHousePdf extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(CfsImpWareHousePdf.class);

    private LclUnitSs lclUnitSs;
    StringBuilder eciReference = new StringBuilder();
    StringBuilder companyDetails = new StringBuilder();
    private LclUtils lclUtils = new LclUtils();
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    String portOfDischarge = "";
    String etaDate = new String();
    String size = new String();
    String masterBl = new String();
    Document document = null;
    PdfWriter pdfWriter = null;
    protected PdfTemplate total;
    protected BaseFont helv;
    private String realPath;
    private String cfsName = "";
    private String companyName ="";
    private String logo ="";
    private String unitSsId ="";

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public CfsImpWareHousePdf(LclUnitSs lclUnitSs) throws Exception {
        this.lclUnitSs = lclUnitSs;
    }

    public CfsImpWareHousePdf(String realPath, String unitSsId) {
        this.realPath = realPath;
        this.unitSsId=unitSsId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void createPdf(String realPath, String outputFileName, String unitSsId) throws Exception {
        init(outputFileName,unitSsId);
        createBody(realPath, outputFileName, unitSsId);
        destroy();
    }

    public void init(String outputFileName,String unitSsId) throws FileNotFoundException, DocumentException, Exception {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        pdfWriter.setPageEvent(new CfsImpWareHousePdf(realPath,unitSsId));
        this.setImageLogo(unitSsId);
        HeaderFooter footer = new HeaderFooter(
                new Phrase("Thank you for choosing " + companyName + " for all your shipping needs", new Font(Font.HELVETICA, 10, Font.ITALIC, Color.BLACK)), false);
        footer.setBorder(Rectangle.NO_BORDER);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.setFooter(footer);
        document.open();
    }
//   

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(100, 100);
        total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        try {
            helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
                    BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("onOpenDocument failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        total.beginText();
        total.setFontAndSize(helv, 7);
        total.setTextMatrix(0, 0);
        total.showText(String.valueOf(writer.getPageNumber() - 1));
        total.endText();
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        onStartArRedInvoicePage(writer, document);
    }

    public void onStartArRedInvoicePage(PdfWriter writer, Document document) {
        try {
            this.setImageLogo(unitSsId);
            SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
            String companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
            String companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
            String companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");
            PdfPCell cell = new PdfPCell();
            PdfPTable headingMainTable = new PdfPTable(1);
            headingMainTable.setWidthPercentage(100);
            PdfPTable headingTable = new PdfPTable(1);
            headingTable.setWidths(new float[]{100});
            PdfPTable imgTable = new PdfPTable(1);
            imgTable.setWidthPercentage(100);
            Image img = Image.getInstance(realPath + logo);
            img.scalePercent(75);
            PdfPCell logoCell = new PdfPCell(img);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            logoCell.setVerticalAlignment(Element.ALIGN_LEFT);
            logoCell.setPaddingLeft(+27);
            imgTable.addCell(logoCell);
            PdfPTable addrTable = new PdfPTable(1);
            addrTable.setWidthPercentage(100);
            PdfPTable invoiceFacturaTable = new PdfPTable(3);
            invoiceFacturaTable.setWidthPercentage(100);
            invoiceFacturaTable.setWidths(new float[]{40, 20, 40});
            StringBuilder stringBuilder = new StringBuilder();
            addrTable.addCell(makeCellCenterNoBorderFclBL("MAILING ADDRESS: " + (CommonUtils.isNotEmpty(companyAddress) ? companyAddress.toUpperCase() : "")));
            stringBuilder.append("TEL: ");
            stringBuilder.append(CommonUtils.isNotEmpty(companyPhone) ? companyPhone : "").append(" / ");
            stringBuilder.append("FAX: ");
            stringBuilder.append(CommonUtils.isNotEmpty(companyFax) ? companyFax : "");
            addrTable.addCell(makeCellCenterNoBorderFclBL(stringBuilder.toString()));
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));
            invoiceFacturaTable.addCell(makeCellLeftNoBorderFclBL(""));
            cell = makeCell("INVOICE", Element.ALIGN_CENTER, new Font(Font.HELVETICA, 12, Font.BOLD, Color.RED), 0.06f);
            invoiceFacturaTable.addCell(cell);
            invoiceFacturaTable.addCell(makeCellLeftNoBorderFclBL(""));
            cell = new PdfPCell();
            cell.addElement(invoiceFacturaTable);
            cell.setBorder(0);
            addrTable.addCell(cell);
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));

            cell = new PdfPCell();
            cell.addElement(imgTable);
            cell.setBorder(0);
            cell.setPaddingLeft(+150);
            headingMainTable.addCell(cell);
//            headingTable.addCell(cell);
            cell = new PdfPCell();
            cell.addElement(addrTable);
            cell.setBorder(0);
            headingTable.addCell(cell);
            cell = makeCellLeftNoBorderFclBL("");
            cell.setBorderWidthRight(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthBottom(0.0f);
            cell.addElement(headingTable);
            headingMainTable.addCell(cell);
            document.add(headingMainTable);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public void createBody(String realPath, String outputFileName, String unitSsId) throws DocumentException, MalformedURLException, IOException, Exception {
        StringBuilder originValues = new StringBuilder();
        StringBuilder destinationValues = new StringBuilder();
        StringBuilder eciShipmentFileNo = new StringBuilder();
        String cfsVenderNo = "";
        String drNumber = "";
        String fileId = "";
        String unitNo = "";
        String fullDr = "";
        String chargecodeT = "";
        List cfsCharge = null;

        originValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclUnitSs.getLclSsHeader().getOrigin()));
        destinationValues.append(lclUtils.getConcatenatedOriginByUnlocation(lclUnitSs.getLclSsHeader().getDestination()));
        eciShipmentFileNo.append(lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum()).append("-");
        eciShipmentFileNo.append(lclUnitSs.getLclSsHeader().getOrigin().getUnLocationCode()).append("-");
        eciShipmentFileNo.append(lclUnitSs.getLclSsHeader().getDestination().getUnLocationCode()).append("-");
        eciShipmentFileNo.append(lclUnitSs.getLclSsHeader().getScheduleNo());
        if (CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getLclUnitSsImportsList().get(0).getCfsWarehouseId())) {
            cfsName = lclUnitSs.getLclUnit().getLclUnitSsImportsList().get(0).getCfsWarehouseId().getWarehouseName();
//            if (CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getLclUnitSsImportsList().get(0).getCfsWarehouseId().getVendorNo())) {
//                cfsVenderNo = lclUnitSs.getLclUnit().getLclUnitSsImportsList().get(0).getCfsWarehouseId().getVendorNo();
//            }
        }

//        if (CommonFunctions.isNotNull(cfsVenderNo)) {
            cfsCharge = new LclSsAcDAO().getCfsCharge(lclUnitSs.getLclSsHeader().getId());
//        }
        unitNo = lclUnitSs.getLclUnit().getUnitNo() + "  ( " + lclUnitSs.getLclUnit().getUnitType().getDescription().toUpperCase() + " )";
        LclUnitSsDAO lclUnitSSDAO = new LclUnitSsDAO();
        String[] fullUnitDr = lclUnitSSDAO.getTotalBkgInUnits(lclUnitSs.getId().toString()); // Full unit Dr's
        fullDr = fullUnitDr[1];
        String[] drNumbers = lclUnitSSDAO.getWareHouseFileId(lclUnitSs.getId().toString());//only wareHouse Dr's
        fileId = drNumbers[0];
        drNumber = drNumbers[1];
//        List<String> aList = null;
        List<String> bList = null;

//        if (fileId.contains(",")) {
//            aList = Arrays.asList(fileId.split(","));//fileId List
//        }
        if (drNumber != null) {
            bList = new ArrayList(Arrays.asList(drNumber.split(",")));// drNumber List
        }

        // tabel for company details and invoice heading
        PdfPCell cell = new PdfPCell();

        PdfPTable mainTable = makeTable(2);
        mainTable.setWidthPercentage(100);
        PdfPTable othersTable = makeTable(4);
        othersTable.setWidthPercentage(100f);
        othersTable.setWidths(new float[]{25, 25, 25, 25});
        cell = makeCell("BILL TO ACCT NO.", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("INVOICE NO.", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("ORIGIN", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("DESTINATION", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        othersTable.addCell(cell);
        othersTable = fillMarksAndVoyageContinerInformation(othersTable, originValues.toString(), destinationValues.toString());

        cell = makeCell("ECI SHIPMENT FILE NUMBER", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("UNIT NO.", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
        cell.setColspan(2);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("" + eciShipmentFileNo.toString(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("" + unitNo, Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        othersTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        cell.setBorderWidthRight(0.06f);
        othersTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setColspan(2);
        othersTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(othersTable);
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);

        cell = makeCell("DESCRIPTION / DETAILS", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setColspan(4);
        mainTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setColspan(4);
        PdfPCell pcell = null;
        PdfPTable ptable = new PdfPTable(4);
        ptable.setWidthPercentage(100f);
        ptable.setWidths(new float[]{0.08f, 0.93f, 0.09f, 5.28f});

        String masterBl = "";
        String vessel = "";
        String ssVoyage = "";
       
        LclSsDetail lclSsDetail = null;
        lclSsDetail = lclUnitSs.getLclSsHeader().getVesselSsDetail();
        masterBl = lclUnitSs.getLclSsHeader().getLclUnitSsManifestList().get(0).getMasterbl().toUpperCase();
        ssVoyage = lclUnitSs.getLclSsHeader().getVesselSsDetail().getSpReferenceNo();

        if (CommonUtils.isNotEmpty(lclSsDetail.getSpReferenceName())) {
            vessel = lclSsDetail.getSpReferenceName().toUpperCase();
        }
        if (null == fullDr) {
            fullDr = "";
        }
        Paragraph p = null;
        //DockReceipts
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "Dock Receipts", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "" + fullDr, blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);
        //MasterBL
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "Master BL#", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "" + masterBl, blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        //S.S. Voyage
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "S.S. Voyage", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "" + ssVoyage, blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        //Vessel
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        ptable.addCell(pcell);
        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "Vessel", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_RIGHT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "  : ", blackFontForFclAr);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        pcell = new PdfPCell();
        pcell.setBorder(0);
        pcell.setPadding(0f);
        p = new Paragraph(9f, "" + vessel, blackBoldFontSize6);
        p.setAlignment(Element.ALIGN_LEFT);
        pcell.addElement(p);
        ptable.addCell(pcell);

        cell.addElement(ptable);
        mainTable.addCell(cell);

        List<BookingChargesBean> lclBookingAcList = null;
        String code = "";
        String codeDesc = "";
        if (bList != null) {
            for (Object b : bList) {
                Paragraph p1 = null;
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.06f);
                cell.setBorderWidthRight(0.06f);
                cell.setColspan(5);
                PdfPTable ntable = new PdfPTable(5);
                PdfPCell ncell = null;
                ntable.setWidthPercentage(100f);
                ntable.setWidths(new float[]{1f, 1f, 1f, 1f, 1f});

                ncell = new PdfPCell();       //1
                ncell.setBorder(0);
                p1 = new Paragraph(5f, "" + b.toString(), blackFontForFclAr);
                p1.setAlignment(Element.ALIGN_LEFT);
                ncell.addElement(p1);
                ntable.addCell(ncell);

                String subStr = b.toString().substring(4);
                Long fId = new LclFileNumberDAO().getFileIdByFileNumber(subStr);
                
                String subHouseBl = new LclUnitSsDAO().getWareHouseForSubHouse(String.valueOf(fId));
                
               
                ncell = new PdfPCell();  //2
                ncell.setBorder(0);
                p1 = new Paragraph(5f, "Sub-House B/L # :   "  +subHouseBl, blackBoldFontSize6);
                p1.setAlignment(Element.ALIGN_LEFT);
                ncell.addElement(p1);
                ntable.addCell(ncell);

                ncell = new PdfPCell();  //3
                ncell.setBorder(0);
                p1 = new Paragraph(5f, "", blackBoldFontSize6);
                p1.setAlignment(Element.ALIGN_LEFT);
                ncell.addElement(p1);
                ntable.addCell(ncell);
                
                 ncell = new PdfPCell();  //2
                ncell.setBorder(0);
                p1 = new Paragraph(5f, "", blackBoldFontSize6);
                ncell.addElement(p1);
                ntable.addCell(ncell);

                ncell = new PdfPCell();  //3
                ncell.setBorder(0);
                p1 = new Paragraph(5f, "", blackBoldFontSize6);
                ncell.addElement(p1);
                ntable.addCell(ncell);

//                String subStr = b.toString().substring(4);
//                Long fId = new LclFileNumberDAO().getFileIdByFileNumber(subStr);

                lclBookingAcList = new LclCostChargeDAO().findBybookingAcWarehouse(String.valueOf(fId));
                for (int k = 0; k < lclBookingAcList.size(); k++) {
                    BookingChargesBean lclBookingA = (BookingChargesBean) lclBookingAcList.get(k);

                    code = CommonUtils.isNotEmpty(lclBookingA.getChargeCode()) ? lclBookingA.getChargeCode() : "";
                    codeDesc = genericCodeDAO.getGenericCodeDesc(code);

                    ncell = new PdfPCell();       //1
                    ncell.setBorder(0);
                    p1 = new Paragraph(5f, "", blackBoldFontSize6);
                    ncell.addElement(p1);
                    ntable.addCell(ncell);
                    
                    ncell = new PdfPCell();       //1
                    ncell.setBorder(0);
                    p1 = new Paragraph(5f, "", blackBoldFontSize6);
                    ncell.addElement(p1);
                    ntable.addCell(ncell);
                    
                    ncell = new PdfPCell();  //2
                    ncell.setBorder(0);
                    if (CommonUtils.isNotEmpty(codeDesc)) {
                        p1 = new Paragraph(5f, "" + codeDesc, blackBoldFontSize6);
                    } else {
                        p1 = new Paragraph(5f, "" + code, blackBoldFontSize6);
                    }
                    p1.setAlignment(Element.ALIGN_CENTER);
                    ncell.addElement(p1);
                    ntable.addCell(ncell);

                    ncell = new PdfPCell();  //3
                    ncell.setBorder(0);
                    p1 = new Paragraph(5f, "$" + lclBookingA.getTotalAmt().doubleValue(), blackBoldFontSize6);
                    p1.setAlignment(Element.ALIGN_LEFT);
                    ncell.addElement(p1);
                    ntable.addCell(ncell);
                    
                 
                    ncell = new PdfPCell();       //1
                    ncell.setBorder(0);
                    p1 = new Paragraph(5f, "", blackBoldFontSize6);
                    ncell.addElement(p1);
                    ntable.addCell(ncell);


                }
                cell.addElement(ntable);
                mainTable.addCell(cell);
            }
            }

        PdfPTable chargesTable = makeTable(4);
        chargesTable.setWidthPercentage(100.5f);
        chargesTable.setWidths(new float[]{45, 35, 5, 15});
        cell = makeCell("CHARGES", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
        cell.setColspan(4);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        chargesTable.addCell(cell);

        NumberFormat number = new DecimalFormat("###,###,##0.00");

        int chargeCount = 0;
        double total = 0.00;

        if (fileId != null) {
            lclBookingAcList = new LclCostChargeDAO().findBybookingAcWarehouse(fileId);
            for (int j = 0; j < lclBookingAcList.size(); j++) {
                chargeCount++;
                BookingChargesBean lclBookingAc = (BookingChargesBean) lclBookingAcList.get(j);
//            String codeDesc = "";
                code = CommonUtils.isNotEmpty(lclBookingAc.getChargeCode()) ? lclBookingAc.getChargeCode() : "";
                codeDesc = genericCodeDAO.getGenericCodeDesc(code);

                if (CommonUtils.isNotEmpty(lclBookingAc.getChargeCode())) {
//                    String desc = lclBookingAc.getChargeCode().toUpperCase();
                    cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl);
                    cell.setBorderWidthRight(0.0f);
                }
                if (chargeCount == 1) {
                    cell.setBorderWidthTop(0.0f);
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthLeft(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                } else {
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthLeft(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                }
                chargesTable.addCell(cell);

                if (CommonUtils.isNotEmpty(codeDesc)) {
                    cell = makeCell("" + codeDesc, Element.ALIGN_LEFT, blackFontForFclBl, 0.06f);
                    cell.setBorderWidthLeft(0.0f);
                } else {
                    cell = makeCell("" + code, Element.ALIGN_LEFT, blackFontForFclBl, 0.06f);
                    cell.setBorderWidthLeft(0.0f);
                }
                if (chargeCount == 1) {
                    cell.setBorderWidthTop(0.0f);
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                } else {
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                }
                chargesTable.addCell(cell);

                cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);//3
                if (chargeCount == 1) {
                    cell.setBorderWidthTop(0.0f);
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                } else {
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                }
                chargesTable.addCell(cell);
                cell = makeCell(number.format(lclBookingAc.getTotalAmt().doubleValue()), Element.ALIGN_RIGHT, blackFontForFclBl, Rectangle.BOX);//4
                if (chargeCount == 1) {
                    cell.setBorderWidth(0.0f);
                } else {
                    cell.setBorderWidthLeft(0.0f);
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                }
                chargesTable.addCell(cell);
                total = total + lclBookingAc.getTotalAmt().doubleValue();

            }
        }
        for (int i = 0; i < (14 - chargeCount); i++) {
            cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0.06f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            cell.setBorderWidthLeft(0.0f);
            chargesTable.addCell(cell);

            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            cell.setBorderWidthLeft(0.0f);
            chargesTable.addCell(cell);
            cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            chargesTable.addCell(cell);
            cell = makeCell("", Element.ALIGN_RIGHT, blackFontForFclBl, 0.06f);
            cell.setBorderWidthLeft(0.0f);
            cell.setBorderWidthRight(0.0f);
            cell.setBorderWidthBottom(0.0f);
            cell.setMinimumHeight(10f);
            chargesTable.addCell(cell);
        }

        if (CommonUtils.isNotEmpty(cfsCharge)) {
            for (Object cfsChargeObj : cfsCharge) {
                Object[] obj = (Object[]) cfsChargeObj;
                if (obj[0].toString().equalsIgnoreCase("172")) {
                    chargecodeT = "DRAYAGE";
                }
                if (obj[0].toString().equalsIgnoreCase("488")) {
                    chargecodeT = "STRIPPING";
                }
                cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0.06f);
                cell.setBorderWidthRight(0.0f);
                cell.setBorderWidthBottom(0.0f);
                cell.setBorderWidthLeft(0.0f);
                chargesTable.addCell(cell);
                cell = makeCell(chargecodeT, Element.ALIGN_LEFT, blackFontForFclBl, 0.06f);
                cell.setBorderWidthRight(0.0f);
                cell.setBorderWidthBottom(0.0f);
                cell.setBorderWidthLeft(0.0f);
                chargesTable.addCell(cell);
                cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);
                cell.setBorderWidthRight(0.0f);
                cell.setBorderWidthBottom(0.0f);
                chargesTable.addCell(cell);
                cell = makeCell("-" + obj[1].toString(), Element.ALIGN_RIGHT, blackFontForFclBl, 0.06f);
                cell.setBorderWidthLeft(0.0f);
                cell.setBorderWidthRight(0.0f);
                cell.setBorderWidthBottom(0.0f);
                cell.setMinimumHeight(10f);
                chargesTable.addCell(cell);
                total = total - Double.valueOf(obj[1].toString());
            }
        }
        cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell("INVOICE TOTAL ($-USD)", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell("$", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell(number.format(total), Element.ALIGN_RIGHT, blackFontForFclBl, 0);//4
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setColspan(4);
        chargesTable.addCell(cell);
        chargesTable.setKeepTogether(true);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(chargesTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);

        PdfPTable bankDetailsTable = new PdfPTable(3);
        bankDetailsTable.setWidthPercentage(100.5f);
        bankDetailsTable.setWidths(new float[]{25, 50, 25});
        PdfPTable bankDetails = new PdfPTable(1);
        bankDetails.setWidthPercentage(100f);

        //
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        String eftBank = systemRulesDAO.getSystemRulesByCode("EFTBank");
        String eftBankAddress = systemRulesDAO.getSystemRulesByCode("EFTBankAddress");
        String eftAcctName = systemRulesDAO.getSystemRulesByCode("EFTAcctName");
        String eftAccountNo = systemRulesDAO.getSystemRulesByCode("EFTAccountNo");
        String eftABANo = systemRulesDAO.getSystemRulesByCode("EFTABANo");

        bankDetailsTable.setKeepTogether(true);
        bankDetailsTable.addCell(makeCellLeftNoBorder(""));
        StringBuilder bankHead = new StringBuilder();
        bankHead.append(" PAYMENTS VIA WIRE TRANSFER SHOULD BE SENT ");
        cell = makeCell(bankHead.toString(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        bankDetailsTable.addCell(cell);
        bankDetailsTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0));

        bankDetailsTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        cell = makeCell("AS FOLLOWS: ", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        bankDetailsTable.addCell(cell);
        bankDetailsTable.addCell(makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("BANK: " + eftBank, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell(eftBankAddress, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("ACCT NAME: " + companyName, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("Acct no.: " + eftAccountNo, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("ABA Routing no.: " + eftABANo, Element.ALIGN_CENTER, blackBoldFontSize6, 0));

        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        bankDetailsTable.addCell(cell);
        cell = new PdfPCell();
        cell.addElement(bankDetails);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        bankDetailsTable.addCell(cell);

        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        bankDetailsTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setPadding(0f);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.addElement(bankDetailsTable);
        mainTable.addCell(cell);

        document.add(mainTable);

    }

    public PdfPTable fillMarksAndVoyageContinerInformation(PdfPTable particularsTable, String originValues, String destinationValues)
            throws Exception {
        PdfPCell cell = null;
        cell = makeCellLeftNoBorderFontSize6("" + cfsName);// Bill to acct
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.06f);
        particularsTable.addCell(cell);
        cell = makeCellLeftNoBorderFontSize6("" + lclUnitSs.getLclSsHeader().getScheduleNo());// invoice no.
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.06f);
        particularsTable.addCell(cell);
        cell = makeCellLeftNoBorderFontSize6(originValues.toUpperCase());// origin
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthRight(0.06f);
        particularsTable.addCell(cell);
        cell = makeCellLeftNoBorderFontSize6(destinationValues.toUpperCase());// destination
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        particularsTable.addCell(cell);
        return particularsTable;
    }

    public void destroy() {
        document.close();
    }
    
    public void setImageLogo(String unitSsId) throws Exception {
        String brand = "";
        LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(Long.parseLong(unitSsId));
        if ((lclUnitSs != null && lclUnitSs.getLclSsHeader() != null && !lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().isEmpty())
                && lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno() != null) {
            brand = new TradingPartnerDAO().getBusinessUnit(lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno());
        }
        if (CommonUtils.isNotEmpty(brand)) {
            if ("ECI".equalsIgnoreCase(brand)) {
                this.logo = LoadLogisoftProperties.getProperty("application.image.econo.logo");
                this.companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
            } else if ("OTI".equalsIgnoreCase(brand)) {
                this.logo = LoadLogisoftProperties.getProperty("application.image.econo.logo");
                this.companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
            } else {
                this.logo = LoadLogisoftProperties.getProperty("application.image.logo");
                this.companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
            }
        }
    }
}
