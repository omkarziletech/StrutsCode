package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsManifestDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class LclImportUnitDeliveryPdfCreator extends LclReportFormatMethods {

    private static final Logger log = Logger.getLogger(LclImportUnitDeliveryPdfCreator.class);
    private String unitNumber = "";
    private String userName = "";
    private String userFirstName = "";
    private String userLastName = "";
    private String userExtension = "";
    private String userEmail = "";
    StringBuilder fax = new StringBuilder();
    StringBuilder userPhone = new StringBuilder();
    private String truckingCompany = "";
    private String truckingInformation = "";
    private String unitTerminalAddress = "";
    private String unitTerminal = "";
    private String unitTerminalPhFax = "";
    private String unitTerminalCityStateZipe = "";
    private String hazmat = "";
    private String cfsCityStateZipe = "";
    private String cfsAddress = "";
    private String cfsName = "";
    private String cfsPhFax = "";
    private String ITNumber = "";
    private String arrivalPierPod = "";
    private LclUnitSs lclUnitSs;
    StringBuilder eciReference = new StringBuilder();
    StringBuilder companyDetails = new StringBuilder();
    String portOfDischarge = "";
    String etaDate = new String();
    String size = new String();
    String masterBl = new String();
    Double weight = 0.00;
    HttpServletRequest request;

    public LclImportUnitDeliveryPdfCreator(LclUnitSs lclUnitSs, HttpServletRequest request) throws Exception {
        this.lclUnitSs = lclUnitSs;
        this.request = request;
        List<LclBookingPieceUnit> lclBookingPieceUnitsList = lclUnitSs.getLclBookingPieceUnitList();
        if (lclBookingPieceUnitsList != null && lclBookingPieceUnitsList.size() > 0) {
            for (int i = 0; i < lclBookingPieceUnitsList.size(); i++) {
                LclBookingPieceUnit lclBookingPieceUnit = (LclBookingPieceUnit) lclBookingPieceUnitsList.get(i);
                if (lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue() != 0.00) {
                    weight = weight + lclBookingPieceUnit.getLclBookingPiece().getActualWeightMetric().doubleValue();
                } else if (lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric() != null && lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue() != 0.00) {
                    weight = weight + lclBookingPieceUnit.getLclBookingPiece().getBookedWeightMetric().doubleValue();
                }
                if (!lclBookingPieceUnit.getLclUnitSs().getLclUnit().getLclUnitWhseList().isEmpty()) {
                    Collections.sort(lclBookingPieceUnit.getLclUnitSs().getLclUnit().getLclUnitWhseList(), new Comparator<LclUnitWhse>() {

                        public int compare(LclUnitWhse o1, LclUnitWhse o2) {
                            return o2.getId().compareTo(o1.getId());
                        }
                    });
                    if (lclBookingPieceUnit.getLclUnitSs().getLclUnit().getLclUnitWhseList() != null && lclBookingPieceUnit.getLclUnitSs().getLclUnit().getLclUnitWhseList().size() > 0) {
                        for (int j = 0; j < lclBookingPieceUnit.getLclUnitSs().getLclUnit().getLclUnitWhseList().size(); j++) {
                            LclUnitWhse lclUnitWhse = (LclUnitWhse) lclBookingPieceUnit.getLclUnitSs().getLclUnit().getLclUnitWhseList().get(0);
                            truckingCompany = lclUnitWhse.getWarehouse().getWarehouseName().toUpperCase();
                        }
                    }
                    if (lclBookingPieceUnit.getLclUnitSs().getLclUnit().getLclUnitSsImportsList() != null && lclBookingPieceUnit.getLclUnitSs().getLclUnit().getLclUnitSsImportsList().size() > 0) {
                        LclUnitSsImports imports = (LclUnitSsImports) lclBookingPieceUnit.getLclUnitSs().getLclUnit().getLclUnitSsImportsList().get(0);
                        if (CommonFunctions.isNotNull(imports)) {
                            if (CommonFunctions.isNotNull(imports.getUnitWareHouseId())) {
                                if (CommonFunctions.isNotNull(imports.getUnitWareHouseId().getWarehouseName())) {
                                    unitTerminal = imports.getUnitWareHouseId().getWarehouseName().toUpperCase();
                                }
                                if (CommonFunctions.isNotNull(imports.getUnitWareHouseId().getAddress())) {
                                    unitTerminalAddress = imports.getUnitWareHouseId().getAddress().toUpperCase();
                                }
                                if (CommonFunctions.isNotNull(imports.getUnitWareHouseId().getCity()) || CommonFunctions.isNotNull(imports.getUnitWareHouseId().getState()) || CommonFunctions.isNotNull(imports.getUnitWareHouseId().getZipCode())) {
                                    unitTerminalCityStateZipe = imports.getUnitWareHouseId().getCity().toUpperCase() + " " + imports.getUnitWareHouseId().getState().toUpperCase() + " " + imports.getUnitWareHouseId().getZipCode();
                                }
                                if (CommonFunctions.isNotNull(imports.getUnitWareHouseId().getPhone()) || CommonFunctions.isNotNull(imports.getUnitWareHouseId().getFax())) {
                                    unitTerminalPhFax = " Ph:" + imports.getUnitWareHouseId().getPhone() + " Fax:" + imports.getUnitWareHouseId().getFax();
                                }
                            }
                            if (CommonFunctions.isNotNull(imports.getCfsWarehouseId())) {
                                if (CommonFunctions.isNotNull(imports.getCfsWarehouseId().getWarehouseName())) {
                                    cfsName = imports.getCfsWarehouseId().getWarehouseName().toUpperCase();
                                }
                                if (CommonFunctions.isNotNull(imports.getCfsWarehouseId().getAddress())) {
                                    cfsAddress = imports.getCfsWarehouseId().getAddress().toUpperCase();
                                }
                                if (CommonFunctions.isNotNull(imports.getCfsWarehouseId().getCity()) || CommonFunctions.isNotNull(imports.getCfsWarehouseId().getState()) || CommonFunctions.isNotNull(imports.getCfsWarehouseId().getZipCode())) {
                                    cfsCityStateZipe = imports.getCfsWarehouseId().getCity().toUpperCase() + " " + imports.getCfsWarehouseId().getState().toUpperCase() + " " + imports.getCfsWarehouseId().getZipCode();
                                }
                                if (CommonFunctions.isNotNull(imports.getCfsWarehouseId().getPhone()) || CommonFunctions.isNotNull(imports.getCfsWarehouseId().getFax())) {
                                    cfsPhFax = " Ph:" + imports.getCfsWarehouseId().getPhone() + " Fax:" + imports.getCfsWarehouseId().getFax();
                                }
                            }
                            if (CommonFunctions.isNotNull(imports.getItNo())) {
                                ITNumber = imports.getItNo().toUpperCase();
                            }
                        }
                    }
                }
            }
        }
        if (lclUnitSs.getLclUnit().getHazmatPermitted()) {
            hazmat = "YES";
        } else {
            hazmat = "NO";
        }
        if (lclUnitSs != null && lclUnitSs.getLclUnit() != null && lclUnitSs.getTruckingRemarks() != null) {
            truckingInformation = lclUnitSs.getTruckingRemarks().toUpperCase();
        }
        if (lclUnitSs != null && lclUnitSs.getLclUnit() != null && lclUnitSs.getLclUnit().getUnitType() != null && lclUnitSs.getLclUnit().getUnitType().getDescription() != null) {
            size = lclUnitSs.getLclUnit().getUnitType().getDescription().toUpperCase();
        }
        if (CommonFunctions.isNotNull(lclUnitSs)) {
            if (CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getUnitNo())) {
                unitNumber = lclUnitSs.getLclUnit().getUnitNo();
            }
        }
        List<LclSsDetail> lclSsDetailList = lclUnitSs.getLclSsHeader().getLclSsDetailList();
        if (lclSsDetailList != null && lclSsDetailList.size() > 0) {
            for (Object lclSSDetail : lclSsDetailList) {
                LclSsDetail lclssDetail = (LclSsDetail) lclSSDetail;
                if (CommonFunctions.isNotNull(lclssDetail.getSta())) {
                    etaDate = DateUtils.parseDateToString(lclssDetail.getSta());
                }
                if (CommonFunctions.isNotNull(lclssDetail) && CommonFunctions.isNotNull(lclssDetail.getArrival())) {
                    if (CommonFunctions.isNotNull(lclssDetail.getArrival().getStateId())) {
                        arrivalPierPod = lclssDetail.getArrival().getUnLocationName().toUpperCase() + "/" + lclssDetail.getArrival().getStateId().getCode().toUpperCase() + "(" + lclssDetail.getArrival().getUnLocationCode().toUpperCase() + ")";
                    } else if (CommonFunctions.isNotNull(lclssDetail.getArrival().getCountryId())) {
                        arrivalPierPod = lclssDetail.getArrival().getUnLocationName().toUpperCase() + "/" + lclssDetail.getArrival().getCountryId().getCode() + "(" + lclssDetail.getArrival().getUnLocationCode().toUpperCase() + ")";
                    }
                }
            }
        }
        LclUnitSsManifest lclUnitSsManifest = new LclUnitSsManifestDAO().getLclUnitSSManifestByHeader(lclUnitSs.getLclUnit().getId(), lclUnitSs.getLclSsHeader().getId());
        if (CommonFunctions.isNotNull(lclUnitSsManifest.getMasterbl())) {
            masterBl = lclUnitSsManifest.getMasterbl().toUpperCase();
        }
        User user = (User) request.getSession().getAttribute("loginuser");
        if (user != null && user.getLoginName() != null) {
            userName = user.getLoginName();
        }
        if (user != null && user.getFirstName() != null) {
            userFirstName = user.getFirstName();
        }
        if (user != null && user.getLastName() != null) {
            userLastName = user.getLastName();
        }
        if (user != null && user.getTelephone() != null) {
            String phoneNumber = user.getTelephone();
            userPhone.append("(").append(phoneNumber.substring(0, 3)).append(") ").append(phoneNumber.substring(3, 6)).append("-").append(phoneNumber.substring(6));
        }
        if (user != null && user.getExtension() != null) {
            userExtension = user.getExtension();
        }
        if (user != null && user.getEmail() != null) {
            userEmail = user.getEmail();
        }
        LclSsHeader lclSsHeader = new LclSsHeaderDAO().findById(lclUnitSs.getLclSsHeader().getId());
        RefTerminal terminal = null;
        if (lclUnitSs != null && lclUnitSs.getLclSsHeader() != null && lclUnitSs.getLclSsHeader().getBillingTerminal() != null && lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum() != null) {
            terminal = new RefTerminalDAO().getByProperty("trmnum", lclUnitSs.getLclSsHeader().getBillingTerminal().getTrmnum());
        }
        if (terminal != null && terminal.getAddres1() != null) {
            companyDetails.append(terminal.getAddres1());
        }
        if (terminal != null && terminal.getCity1() != null) {
            companyDetails.append(" ").append(terminal.getCity1());
        }
        if (terminal != null && terminal.getState() != null) {
            companyDetails.append(" ,").append(terminal.getState());
        }
        if (terminal != null && terminal.getZipcde() != null) {
            companyDetails.append(" ").append(terminal.getZipcde());
        }
        if (terminal != null && terminal.getPhnnum1() != null) {
            String phone = terminal.getPhnnum1();
            companyDetails.append(" PH:").append(phone.substring(0, 3)).append("-").append(phone.substring(3, 6)).append("-").append(phone.substring(6));
        }
        if (user != null && CommonUtils.isNotEmpty(user.getFax())) {
            fax.append(user.getFax().substring(0, 3)).append("-").append(user.getFax().substring(3, 6)).append("-").append(user.getFax().substring(6));
        } else {
            if (terminal != null && terminal.getFaxnum1() != null) {
                fax.append(" FAX:").append(terminal.getFaxnum1().substring(0, 3)).append("-").append(terminal.getFaxnum1().substring(3, 6)).append("-").append(terminal.getFaxnum1().substring(6));
            }
        }
        if (terminal != null && terminal.getTrmnum() != null) {
            eciReference.append(terminal.getTrmnum());
        }
        if (lclSsHeader != null && lclSsHeader.getOrigin() != null && lclSsHeader.getOrigin().getUnLocationCode() != null) {
            eciReference.append("-").append(lclSsHeader.getOrigin().getUnLocationCode());
        }
        if (lclSsHeader != null && lclSsHeader.getDestination() != null && lclSsHeader.getDestination().getUnLocationCode() != null) {
            eciReference.append("-").append(lclSsHeader.getDestination().getUnLocationCode());
        }
        if (lclSsHeader != null && lclSsHeader.getScheduleNo() != null) {
            eciReference.append("-").append(lclSsHeader.getScheduleNo());
        }
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
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                PdfPTable footTable = footerTable();
                footTable.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
                footTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
            } catch (Exception ex) {
                log.info("Exception on class LclImportUnitDeliveryPdfCreator in method onEndPage" + new Date(), ex);
            }
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                document.add(writeHeaderContent());
                Rectangle rect = new Rectangle(535, 800, 60, 60);
                rect.setBorder(Rectangle.BOX);
                rect.setUseVariableBorders(true);
                rect.setBorderWidth(0.6f);
                document.add(rect);
                rect = new Rectangle(533, 798, 62, 62);
                rect.setBorder(Rectangle.BOX);
                rect.setBorderWidth(0.6f);
                document.add(rect);
            } catch (Exception ex) {
                log.info("Exception on class LclImportUnitDeliveryPdfCreator in method onStartPage" + new Date(), ex);
            }
        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);
        }
    }

    private void init(String outputFileName) throws Exception {
        document = new Document(PageSize.A4);
        document.setMargins(15, 15, 8, 25);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        LclImportUnitDeliveryPdfCreator.TableHeader event = new LclImportUnitDeliveryPdfCreator.TableHeader();
        pdfWriter.setPageEvent(event);
        document.open();
    }

    private PdfPTable writeHeaderContent() throws Exception {
        table = new PdfPTable(1);
        Font blackBoldFontSize = FontFactory.getFont("CENTURY GOTHIC", 16f, Font.BOLD, new BaseColor(00, 51, 102));
        Font blackBoldFontSize11 = FontFactory.getFont("CENTURY GOTHIC", 9.5f, Font.BOLD, new BaseColor(00, 51, 102));
        Font blackNormalFontSize10 = FontFactory.getFont("CENTURY GOTHIC", 10f, Font.NORMAL);
        String realPath = request.getSession().getServletContext().getRealPath("/");
        String brand ="";
        String companyName ="";
        String logo ="";
        if((lclUnitSs !=null && lclUnitSs.getLclSsHeader() !=null && !lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().isEmpty()) 
                && lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno() !=null) {
        brand =new TradingPartnerDAO().getBusinessUnit(lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0).getOriginAcctNo().getAccountno());
        }
        if (CommonUtils.isNotEmpty(brand)) {
            if ("ECI".equalsIgnoreCase(brand)) {
                companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
                logo = LoadLogisoftProperties.getProperty("application.image.econo.logo");
            } else if ("OTI".equalsIgnoreCase(brand)) {
                companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
                logo = LoadLogisoftProperties.getProperty("application.image.econo.logo");
            } else {
                companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
                logo = LoadLogisoftProperties.getProperty("application.image.logo");

            }
        }
        table.setWidthPercentage(70f);
        cell = new PdfPCell();
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(80f);
        cell.setBorderWidth(1f);
        Image img = Image.getInstance(realPath + logo);
        img.scalePercent(75);
        img.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(img);
        Paragraph p = new Paragraph(20f, "" + companyDetails.toString().toUpperCase() + " FAX: " + fax, blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(30f, companyName.toUpperCase(), blackBoldFontSize);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(2f, "_____________________________________________________________________________________________", bodyNormalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        p = new Paragraph(1f, "_____________________________________________________________________________________________", bodyNormalFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        PdfPTable ntable = new PdfPTable(2);
        ntable.setWidthPercentage(101f);
        ntable.setWidths(new float[]{1.25f, 1f});
        PdfPCell ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Paragraph(17f, "DATE:", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "", blackNormalFontSize10);
        p.add(new Chunk("" + DateUtils.formatStringDateToAppFormatMMM(new Date())).setUnderline(0.1f, -2f));
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, " ", blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "CARGO LOCATION:", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "" + unitTerminal, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "" + unitTerminalAddress, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "" + unitTerminalCityStateZipe, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "" + unitTerminalPhFax, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "" + portOfDischarge, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        ntable.addCell(ncell);

        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Paragraph(17f, "TRUCKING COMPANY:", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "" + truckingInformation, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "SCAC CODE:       ____________", blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "PLEASE DELIVER CONTAINER TO:", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "" + cfsName, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "" + cfsAddress, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "" + cfsCityStateZipe, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "" + cfsPhFax, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        ntable.addCell(ncell);
        cell.addElement(ntable);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        ntable = new PdfPTable(3);
        ntable.setWidthPercentage(101f);
        ntable.setWidths(new float[]{2f, 1.15f, 1f});
        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Paragraph(40f, "SIZE", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "", blackNormalFontSize10);
        p.add(new Chunk("" + size).setUnderline(0.1f, -2f));
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        ntable.addCell(ncell);

        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Paragraph(40f, "DESCRIPTION", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "", blackNormalFontSize10);
        p.add(new Chunk("" + unitNumber).setUnderline(0.1f, -2f));
        ncell.addElement(p);
        ntable.addCell(ncell);

        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Paragraph(40f, "WEIGHT", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "", blackNormalFontSize10);
        p.add(new Chunk("" + NumberUtils.convertToThreeDecimal(weight)).setUnderline(0.1f, -2f));
        ncell.addElement(p);
        ntable.addCell(ncell);
        cell.addElement(ntable);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        ntable = new PdfPTable(2);
        ntable.setWidthPercentage(101f);
        ntable.setWidths(new float[]{1f, 2f});
        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Paragraph(35f, "ETA:    ", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "PICK UP DATE:    ", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "ECI REFERENCE:    ", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "IT NUMBER:    ", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        ntable.addCell(ncell);

        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Paragraph(35f, "", blackNormalFontSize10);
        p.add(new Chunk("" + etaDate).setUnderline(0.1f, -2f));
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "", blackNormalFontSize10);
        p.add(new Chunk("As soon as avail").setUnderline(0.1f, -2f));
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "", blackNormalFontSize10);
        p.add(new Chunk("" + eciReference.toString()).setUnderline(0.1f, -2f));
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "", blackNormalFontSize10);
        p.add(new Chunk("" + ITNumber.toString()).setUnderline(0.1f, -2f));
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        ntable.addCell(ncell);
        cell.addElement(ntable);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        ntable = new PdfPTable(2);
        ntable.setWidthPercentage(101f);
        ntable.setWidths(new float[]{1f, 1.25f});
        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Paragraph(25f, "MASTER BILL OF LADING:", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "HOUSE BILL OF LADING:", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "PORT OF DISCHARGE:", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "CONTAINER:", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "INLAND FREIGHT PREPAID BY:", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "RELATED DOCUMENTS:", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "HAZMAT:", blackBoldFontSize11);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        ntable.addCell(ncell);

        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Paragraph(25f, "", blackNormalFontSize10);
        p.add(new Chunk("" + masterBl).setUnderline(0.1f, -2f));
        ncell.addElement(p);
        p = new Paragraph(17f, "VARIOUS", blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "" + arrivalPierPod, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "" + unitNumber, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, companyName, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(17f, "", blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        p.add(new Chunk("MANIFEST / HBL / MBL / PTT").setUnderline(0.1f, -2f));
        ncell.addElement(p);
        p = new Paragraph(17f, hazmat, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        ntable.addCell(ncell);
        cell.addElement(ntable);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(30f);
        p = new Paragraph(15f, "If you have any questions or need further assistance, please do not hesitate to contact\n" + userFirstName + " " + userLastName + " at " + userPhone + " Ext. " + userExtension + " Email " + userEmail, blackNormalFontSize10);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    private PdfPTable footerTable() throws Exception {
        table = new PdfPTable(2);
        Font blackFontSize4 = FontFactory.getFont("CALIBRI", 4f, Font.NORMAL);
        table.setWidthPercentage(50f);
        table.setWidths(new float[]{2.25f, .5f});
        cell = new PdfPCell();
        cell.setBorder(0);
        Paragraph p = new Paragraph(1f, " ", blackFontSize4);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        p = new Paragraph(7f, "", blackFontSize4);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(1f, "Created By: " + userFirstName + " " + userLastName, blackFontSize4);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(7f, "" + DateUtils.formatDate(new Date(), "MM/dd/yyyy hh:mm"), blackFontSize4);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public void createExportUnitReport(String realPath, String outputFileName, String fileId) throws Exception {
        init(outputFileName);
        exit();
    }

    private void exit() {
        document.close();
    }
}
