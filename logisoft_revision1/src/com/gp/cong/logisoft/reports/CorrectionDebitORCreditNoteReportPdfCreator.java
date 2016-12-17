package com.gp.cong.logisoft.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;

import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlCorrections;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
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
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class CorrectionDebitORCreditNoteReportPdfCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(CorrectionDebitORCreditNoteReportPdfCreator.class);
    Document document = null;
    PdfWriter pdfWriter = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    private String contextPath = null;
    private FclBlCorrections correction = null;
    protected PdfTemplate total;
    protected BaseFont helv;
    private final StringBuilder units = new StringBuilder();

    public CorrectionDebitORCreditNoteReportPdfCreator() {
    }

    public CorrectionDebitORCreditNoteReportPdfCreator(String contextPath, FclBlCorrections correction) {
        this.contextPath = contextPath;
        this.correction = correction;
    }

    public void initialize(String fileName, String contextPath, FclBlCorrections correction) throws Exception {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
        pdfWriter.setPageEvent(new CorrectionDebitORCreditNoteReportPdfCreator(contextPath, correction));
        String companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
        String fileNo = correction.getFileNo();
        String brand = new FclBlDAO().getBrand(fileNo);
        if (brand.equalsIgnoreCase("Econo")) {
            companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
        } else if (brand.equalsIgnoreCase("OTI")) {
            companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
        }
        HeaderFooter footer = new HeaderFooter(new Phrase("Thank you for choosing " + companyName + " for all your shipping needs", new Font(Font.HELVETICA, 10, Font.ITALIC, Color.BLACK)), false);
        footer.setBorder(Rectangle.NO_BORDER);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.setFooter(footer);
        document.open();
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(100, 100);
        total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        try {
            helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            log.info("onOpenDocument failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        } catch (IOException e) {
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
        try {
            String path = LoadLogisoftProperties.getProperty("application.image.logo");
            String fileNo = correction.getFileNo();
            String brand = new FclBlDAO().getBrand(fileNo);
            if (brand.equalsIgnoreCase("Econo") || brand.equalsIgnoreCase("OTI")) {
                path = LoadLogisoftProperties.getProperty("application.image.econo.logo");
            }
            SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
            String companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
            String companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
            String companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");
            PdfPTable headingMainTable = new PdfPTable(1);
            headingMainTable.setWidthPercentage(100);
            PdfPTable headingTable = new PdfPTable(1);
            headingTable.setWidths(new float[]{100});
            PdfPTable imgTable = new PdfPTable(1);
            imgTable.setWidthPercentage(100);
            Image img = Image.getInstance(contextPath + path);
            img.scalePercent(75);
            PdfPCell logoCell = new PdfPCell(img);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            logoCell.setVerticalAlignment(Element.ALIGN_LEFT);
            logoCell.setPaddingLeft(+25);
            imgTable.addCell(logoCell);
            PdfPTable addrTable = new PdfPTable(1);
            addrTable.setWidthPercentage(100);
            PdfPTable titleTable = new PdfPTable(3);
            titleTable.setWidthPercentage(100);
            titleTable.setWidths(new float[]{40, 20, 40});
            StringBuilder stringBuilder = new StringBuilder();
            addrTable.addCell(makeCellCenterNoBorderFclBL("MAILING ADDRESS: " + (CommonUtils.isNotEmpty(companyAddress) ? companyAddress.toUpperCase() : "")));
            stringBuilder.append("TEL: ");
            stringBuilder.append(CommonUtils.isNotEmpty(companyPhone) ? companyPhone : "").append(" / ");
            stringBuilder.append("FAX: ");
            stringBuilder.append(CommonUtils.isNotEmpty(companyFax) ? companyFax : "");
            addrTable.addCell(makeCellCenterNoBorderFclBL(stringBuilder.toString()));
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));
            titleTable.addCell(makeCellLeftNoBorderFclBL(""));
            String title = CommonUtils.isEqualIgnoreCase(correction.getDebitOrCreditNote(), FclBlConstants.CREDTINOTE) ? "CREDIT NOTE" : "INVOICE";
            PdfPCell cell = makeCell(title, Element.ALIGN_CENTER, new Font(Font.HELVETICA, 12, Font.BOLD, Color.RED), 0.06f);
            titleTable.addCell(cell);
            titleTable.addCell(makeCellLeftNoBorderFclBL(""));
            cell = new PdfPCell();
            cell.addElement(titleTable);
            cell.setBorder(0);
            addrTable.addCell(cell);
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));
            addrTable.addCell(makeCellLeftNoBorderFclBL(""));

            cell = new PdfPCell();
            cell.addElement(imgTable);
            cell.setBorder(0);
            cell.setPaddingLeft(+150);
            headingMainTable.addCell(cell);
//	    headingTable.addCell(cell);
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
            log.info("onOpenDocument failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    private List<FclBlContainer> sortContainerList(List<FclBlContainer> containerList) throws Exception {
        List<FclBlContainer> orderedContainerList = new ArrayList();
        List<String> legentSize = new GenericCodeDAO().getUnitCostTypeListInOrder();
        for (String legentName : legentSize) {
            for (FclBlContainer fclBlContainer : containerList) {
                String fcllegentName = fclBlContainer.getSizeLegend().getCodedesc();
                if (legentName.equals(fcllegentName)) {
                    orderedContainerList.add(fclBlContainer);
                }
            }
        }
        return orderedContainerList;
    }

    private PdfPTable fillMarksAndContainerInformation(PdfPTable particularsTable, FclBl bl)
            throws Exception {
        NumberFormat numberFormat = new DecimalFormat("###,###,##0.000");
        Set<FclBlContainer> containerSet = bl.getFclcontainer();
        Map<Integer, FclBlContainer> map = new HashMap<Integer, FclBlContainer>();
        if (containerSet != null) {
            for (FclBlContainer blContainer : containerSet) {
                if (!"D".equalsIgnoreCase(blContainer.getDisabledFlag())) {
                    map.put(blContainer.getTrailerNoId(), blContainer);
                }
            }
        }
        List<FclBlContainer> temp = new ArrayList<FclBlContainer>();
        for (FclBlContainer blContainer : containerSet) {
            temp.add(blContainer);
        }
        List<FclBlContainer> containerList = sortContainerList(temp);
        int count = 0;
        PdfPCell cell;
        if ("I".equalsIgnoreCase(bl.getImportFlag())) {
            FclBlContainerDAO fclBlContainerDAO = new FclBlContainerDAO();
            for (FclBlContainer container : containerList) {
                if (count > 0) {
                    units.append(", ");
                }
                units.append(container.getTrailerNo()).append(" ( ").append(container.getSizeLegend().getCodedesc()).append(" )");
                List<FclBlMarks> fclMarksList = fclBlContainerDAO.getPakagesDetails(container.getTrailerNoId());
                if (fclMarksList != null && !fclMarksList.isEmpty()) {
                    for (FclBlMarks fclBlmarks : fclMarksList) {
                        count++;
                        StringBuilder packages = new StringBuilder();
                        if (null != bl.getTotalContainers() && bl.getTotalContainers().equalsIgnoreCase("Yes")) {
                            packages.append(null != fclBlmarks.getNoOfPkgs() && fclBlmarks.getNoOfPkgs() != 0 ? fclBlmarks.getNoOfPkgs() : "");
                            packages.append(" ");
                            packages.append(null != fclBlmarks.getUom() ? fclBlmarks.getUom() : "");
                        }
                        cell = makeCellLeftNoBorderFontSize6(packages.toString());
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBorderWidthRight(0.06f);
                        particularsTable.addCell(cell);
                        double netWeightLBS = fclBlmarks.getNetweightLbs() != null ? fclBlmarks.getNetweightLbs() : 0.00;
                        if (netWeightLBS != 0.00) {
                            if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                cell = makeCellLeftNoBorderFontSize6(CommonUtils.removeTrailingZeros("" + netWeightLBS) + " LBS");
                            } else {
                                cell = makeCellLeftNoBorderFontSize6((numberFormat.format(netWeightLBS).toString()) + " LBS");
                            }
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setBorderWidthRight(0.06f);
                            cell.setNoWrap(true);

                        } else {
                            cell = makeCellLeftNoBorderFclBL("");
                            cell.setBorderWidthRight(0.06f);
                        }
                        particularsTable.addCell(cell);
                        double measureCFT = fclBlmarks.getMeasureCft() != null ? fclBlmarks.getMeasureCft() : 0.00;
                        if (measureCFT != 0.00) {
                            if ("yes".equalsIgnoreCase(bl.getTrimTrailingZerosForQty())) {
                                cell = makeCellLeftNoBorderFontSize6(CommonUtils.removeTrailingZeros("" + measureCFT) + " CFT");
                            } else {
                                cell = makeCellLeftNoBorderFontSize6((numberFormat.format(measureCFT).toString()) + " CFT");
                            }
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setBorderWidthRight(0.06f);
                            cell.setNoWrap(true);
                        } else {
                            cell = makeCellLeftNoBorderFclBL("");
                            cell.setBorderWidthRight(0.06f);
                        }
                        particularsTable.addCell(cell);
                        if (count == 1) {
                            cell = makeCellLeftNoBorderFontSize6(bl.getTerminal());
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setBorderWidthRight(0.06f);
                            particularsTable.addCell(cell);
                            cell = makeCellLeftNoBorderFontSize6(bl.getFinalDestination());
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            particularsTable.addCell(cell);
                        } else {
                            cell = makeCellLeftNoBorderFontSize6("");
                            cell.setBorderWidthRight(0.06f);
                            particularsTable.addCell(cell);
                            particularsTable.addCell(makeCellLeftNoBorderFontSize6(""));
                        }
                    }
                }
            }
            if (count == 0) {
                cell = makeCellLeftNoBorderFontSize6("");
                cell.setBorderWidthRight(0.06f);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6("");
                cell.setBorderWidthRight(0.06f);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6("");
                cell.setBorderWidthRight(0.06f);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6(bl.getTerminal());
                cell.setBorderWidthRight(0.06f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                particularsTable.addCell(cell);
                cell = makeCellLeftNoBorderFontSize6(bl.getFinalDestination());
                cell.setBorderWidthRight(0.06f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                particularsTable.addCell(cell);
            }
        } else {
            StringBuilder trailers = new StringBuilder();
            for (FclBlContainer fclBlContainer : containerList) {
                if (null != fclBlContainer.getTrailerNo() && !"".equals(fclBlContainer.getTrailerNo())) {
                    count++;
                    trailers.append(fclBlContainer.getTrailerNo());
                    trailers.append(",");
                }
            }
            cell = makeCellLeftNoBorderFontSize6(trailers.toString());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidthRight(0.06f);
            particularsTable.addCell(cell);
            String bookingNo = null != bl.getBookingNo() && !"".equals(bl.getBookingNo()) ? bl.getBookingNo() : "";
            cell = makeCellLeftNoBorderFontSize6(bookingNo);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidthRight(0.06f);
            particularsTable.addCell(cell);
            cell = makeCellLeftNoBorderFontSize6(bl.getTerminal());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidthRight(0.06f);
            particularsTable.addCell(cell);
            cell = makeCellLeftNoBorderFontSize6(bl.getFinalDestination());
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            particularsTable.addCell(cell);
        }
        return particularsTable;
    }

    public void createBody(String contextPath, FclBlCorrections correction, HttpServletRequest request)
            throws DocumentException, MalformedURLException, IOException, Exception {
        PdfPTable mainTable = makeTable(2);
        mainTable.setWidthPercentage(100);
        mainTable.setKeepTogether(true);
        PdfPTable clientPTable = new PdfPTable(2);
        clientPTable.setWidthPercentage(101);
        clientPTable.setWidths(new float[]{10, 90});
        clientPTable.setKeepTogether(true);
        PdfPCell cell = makeCell("TO:", Element.ALIGN_LEFT, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        CustAddress custAddress = new CustAddressDAO().getUniqueAddress(correction.getToPartyNo());
        cell = makeCell(custAddress.getAcctName(), Element.ALIGN_LEFT, blackFontForFclAr, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFontSize8, 0);
        cell.setBorderWidthRight(0.06f);
        clientPTable.addCell(cell);
        StringBuilder address = new StringBuilder("");
        address.append(custAddress.getAddress1()).append("\n");
        address.append("\n");
        address.append(custAddress.getCity1()).append(", ");
        address.append(custAddress.getState()).append(" ").append(custAddress.getZip());
        cell = makeCell(address.toString(), Element.ALIGN_LEFT, blackFontForFclAr, 0);
        cell.setBorderWidthRight(0.06f);
        cell.setMinimumHeight(30);
        clientPTable.addCell(cell);
        cell = makeCell("ATTN", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell = makeCell(CommonUtils.isNotEmpty(custAddress.getContactName()) ? custAddress.getContactName().toUpperCase() : "", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        clientPTable.addCell(cell);
        cell.addElement(clientPTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.0f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.0f);
        cell.setBorderWidthBottom(0.0f);
        mainTable.addCell(cell);

        List<TransactionLedger> lineItemList = (List) request.getAttribute("transactionList");
        String type = CommonUtils.isEqualIgnoreCase(correction.getDebitOrCreditNote(), FclBlConstants.CREDTINOTE) ? "CREDIT NOTE" : "INVOICE";

        PdfPTable correctionMainTable = new PdfPTable(1);
        correctionMainTable.setWidthPercentage(101.5f);
        correctionMainTable.setKeepTogether(true);
        PdfPTable correctionPTable1 = new PdfPTable(4);
        correctionPTable1.setWidths(new float[]{10, 25, 20, 45});
        correctionPTable1.setWidthPercentage(101);
        correctionPTable1.addCell(makeCellLeftNoBorderFclBL(""));
        cell = makeCell(type + " NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        correctionPTable1.addCell(cell);
        cell = makeCell("DATE", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        correctionPTable1.addCell(cell);
        cell = makeCell("REFERENCE NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        correctionPTable1.addCell(cell);
        correctionPTable1.addCell(makeCellLeftNoBorderFclBL(""));
        String noticeNo = "04" + correction.getFileNo() + "CN" + correction.getNoticeNo();
        cell = makeCell(noticeNo, Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        correctionPTable1.addCell(cell);
        SimpleDateFormat simpDate = new SimpleDateFormat("dd-MMM-yyyy");
        cell = makeCell(simpDate.format(correction.getDate()), Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        correctionPTable1.addCell(cell);
        FclBl bl = (FclBl) request.getAttribute("fclBl");
        cell = makeCell(bl.getExportReference(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthBottom(0.06f);
        correctionPTable1.addCell(cell);
        cell = makeCellLeftNoBorderFclBL("");
        cell.setColspan(4);
        correctionPTable1.addCell(cell);
        cell = new PdfPCell();
        cell.addElement(correctionPTable1);
        cell.setBorder(0);
        correctionMainTable.addCell(cell);

        PdfPTable correctionPTable2 = new PdfPTable(3);
        correctionPTable2.setWidths(new float[]{10, 40, 50});
        correctionPTable2.setWidthPercentage(101);
        if ("I".equalsIgnoreCase(bl.getImportFlag())) {
            correctionPTable2.addCell(makeCellLeftNoBorderFclBL(""));
            cell = makeCell("BILL TO ACCT NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthBottom(0.06f);
            correctionPTable2.addCell(cell);
            cell = makeCell("ECI Ref.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthBottom(0.06f);
            correctionPTable2.addCell(cell);
            correctionPTable2.addCell(makeCellLeftNoBorderFclBL(""));
            cell = makeCell(custAddress.getAcctNo(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthBottom(0.06f);
            correctionPTable2.addCell(cell);
            cell = makeCell("04" + bl.getFileNo(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthBottom(0.06f);
            correctionPTable2.addCell(cell);
        } else {
            correctionPTable2.addCell(makeCellLeftNoBorderFclBL(""));
            cell = makeCell("BILL TO ACCT NO.", Element.ALIGN_CENTER, headingFontSize8, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthBottom(0.06f);
            correctionPTable2.addCell(cell);
            cell = makeCell(custAddress.getAcctNo(), Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthBottom(0.06f);
            correctionPTable2.addCell(cell);
        }
        cell = new PdfPCell();
        cell.addElement(correctionPTable2);
        cell.setBorder(0);
        correctionMainTable.addCell(cell);

        cell = new PdfPCell();
        cell.addElement(correctionMainTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.0f);
        cell.setBorderWidthTop(0.0f);
        cell.setBorderWidthBottom(0.0f);
        mainTable.addCell(cell);

        if ("I".equalsIgnoreCase(bl.getImportFlag())) {
            PdfPTable othersTable = makeTable(5);
            othersTable.setWidthPercentage(100.5f);
            othersTable.setWidths(new float[]{10, 20, 20, 25, 25});
            cell = makeCell("PIECES", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthRight(0.06f);
            othersTable.addCell(cell);
            cell = makeCell("WEIGHT", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthRight(0.06f);
            othersTable.addCell(cell);
            cell = makeCell("MEASUREMENTS", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
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
            othersTable = fillMarksAndContainerInformation(othersTable, bl);
            cell = makeCell("ECI SHIPMENT FILE NUMBER.", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setColspan(3);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthBottom(0.06f);
            othersTable.addCell(cell);
            cell = makeCell("COMMENTS", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setColspan(2);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthBottom(0.06f);
            othersTable.addCell(cell);
            cell = makeCell("04" + bl.getFileNo(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            cell.setColspan(3);
            cell.setBorderWidthRight(0.06f);
            othersTable.addCell(cell);
            cell = makeCell(correction.getComments().toUpperCase(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            cell.setColspan(2);
            othersTable.addCell(cell);
            StringBuilder shipper = new StringBuilder();
            shipper.append(CommonUtils.isNotEmpty(bl.getShipperName()) ? bl.getShipperName() : "");
            if (CommonUtils.isNotEmpty(bl.getShipperName()) && CommonUtils.isNotEmpty(bl.getForwardingAgentName())) {
                shipper.append(" / ");
            }
            shipper.append(CommonUtils.isNotEmpty(bl.getForwardingAgentName()) ? bl.getForwardingAgentName() : "");
            cell = makeCell(shipper.toString(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            cell.setColspan(3);
            cell.setBorderWidthRight(0.06f);
            othersTable.addCell(cell);
            cell = makeCell(CommonUtils.isNotEmpty(bl.getConsigneeName()) ? bl.getConsigneeName() : "", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            cell.setColspan(2);
            othersTable.addCell(cell);
            cell = makeCell("DESCRIPTION / DETAILS", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthBottom(0.06f);
            cell.setColspan(5);
            othersTable.addCell(cell);
            cell = makeCellLeftNoBorderFclBL(correction.getComments());
            cell.setColspan(5);
            cell.setMinimumHeight(50);
            othersTable.addCell(cell);
            cell = new PdfPCell();
            cell.setColspan(2);
            cell.addElement(othersTable);
            cell.setBorder(0);
            cell.setBorderWidthRight(0.06f);
            cell.setBorderWidthLeft(0.06f);
            mainTable.addCell(cell);
        } else {
            PdfPTable othersTable = makeTable(4);
            othersTable.setWidthPercentage(100f);
            othersTable.setWidths(new float[]{25, 25, 25, 25});
            cell = makeCell("CONTAINER NO.", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setBorderWidthBottom(0.06f);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthRight(0.06f);
            othersTable.addCell(cell);
            cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
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
            othersTable = fillMarksAndContainerInformation(othersTable, bl);
            cell = makeCell("ECI SHIPMENT FILE NUMBER", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setColspan(2);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthBottom(0.06f);
            othersTable.addCell(cell);
            cell = makeCell("COMMENTS", Element.ALIGN_CENTER, blackBoldFontSize6, 0, Color.decode("#c5d9f1"));
            cell.setColspan(2);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthBottom(0.06f);
            othersTable.addCell(cell);
            cell = makeCell("04" + bl.getFileNo(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            cell.setColspan(2);
            cell.setBorderWidthRight(0.06f);
            othersTable.addCell(cell);
            cell = makeCell(correction.getComments().toUpperCase(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            cell.setColspan(2);
            othersTable.addCell(cell);
            StringBuilder shipper = new StringBuilder();
            shipper.append(CommonUtils.isNotEmpty(bl.getShipperName()) ? bl.getShipperName() : "");
            if (CommonUtils.isNotEmpty(bl.getShipperName()) && CommonUtils.isNotEmpty(bl.getForwardingAgentName())) {
                shipper.append(" / ");
            }
            shipper.append(CommonUtils.isNotEmpty(bl.getForwardingAgentName()) ? bl.getForwardingAgentName() : "");
            cell = makeCell(shipper.toString(), Element.ALIGN_CENTER, blackBoldFontSize6, 0);
            cell.setColspan(2);
            cell.setBorderWidthRight(0.06f);
            othersTable.addCell(cell);
            cell = makeCell(CommonUtils.isNotEmpty(bl.getConsigneeName()) ? bl.getConsigneeName() : "", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
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
            PdfPTable ptable = new PdfPTable(2);
            ptable.setWidthPercentage(100f);
            ptable.setWidths(new float[]{.78f, 5.15f});
            PdfPCell pcell = new PdfPCell();
            pcell.setBorder(0);
            cell.setBorderWidthBottom(0.06f);
            Paragraph p = new Paragraph(10f, "     Dock Receipt", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_RIGHT);
            pcell.addElement(p);
            p = new Paragraph(10f, "     Origin", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_RIGHT);
            pcell.addElement(p);
            p = new Paragraph(10f, "     Destination", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_RIGHT);
            pcell.addElement(p);
            p = new Paragraph(10f, "     Vessel", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_RIGHT);
            pcell.addElement(p);
            p = new Paragraph(10f, "     S.S. Voyage", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_RIGHT);
            pcell.addElement(p);
            p = new Paragraph(10f, "     Unit", blackFontForFclAr);
            p.setAlignment(Element.ALIGN_RIGHT);
            pcell.addElement(p);
            ptable.addCell(pcell);

            pcell = new PdfPCell();
            pcell.setBorder(0);
            p = new Paragraph(10f, ": 04" + bl.getFileNo(), blackBoldFontSize6);
            pcell.addElement(p);
            p = new Paragraph(10f, ": " + bl.getTerminal(), blackBoldFontSize6);
            pcell.addElement(p);
            p = new Paragraph(10f, ": " + bl.getFinalDestination(), blackBoldFontSize6);
            pcell.addElement(p);
            p = new Paragraph(10f, ": " + bl.getVessel().getCodedesc(), blackBoldFontSize6);
            pcell.addElement(p);
            p = new Paragraph(10f, ": " + bl.getVoyages(), blackBoldFontSize6);
            pcell.addElement(p);
            p = new Paragraph(10f, ": " + units.toString(), blackBoldFontSize6);
            pcell.addElement(p);
            ptable.addCell(pcell);
            cell.addElement(ptable);
            mainTable.addCell(cell);
        }
        PdfPTable chargesTable = makeTable(4);
        chargesTable.setWidthPercentage(100.5f);
        chargesTable.setWidths(new float[]{45, 35, 5, 15});
        cell = makeCell("CHARGES", Element.ALIGN_CENTER, headingFont, 0, Color.decode("#c5d9f1"));
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setColspan(4);
        chargesTable.addCell(cell);
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        double lateFee = 0.00;
        double payAmount = 0.00;
        int chargeCount = 0;
        double total = 0d;
        double amount;
        String codeTypeId = "36";
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        for (TransactionLedger transactionLedger : lineItemList) {
            if (null != correction.getCorrectionType() && (correction.getCorrectionType().getCode().equalsIgnoreCase("A")
                    || correction.getCorrectionType().getCode().equalsIgnoreCase("Y"))) {
                amount = null != transactionLedger.getDifferenceAmount() ? transactionLedger.getDifferenceAmount() : 0d;
            } else {
                amount = null != transactionLedger.getNewAmount() ? transactionLedger.getNewAmount() : 0d;
                if (CommonUtils.isEqualIgnoreCase(correction.getDebitOrCreditNote(), FclBlConstants.CREDTINOTE)) {
                    amount = -Math.abs(amount);
                } else {
                    amount = Math.abs(amount);
                }
            }
            total += amount;
            if (CommonUtils.isNotEmpty(amount)) {
                chargeCount++;
                String codeDesc = genericCodeDAO.getCodeDescription(codeTypeId, transactionLedger.getChargeCode());
                chargesTable.addCell(makeCellLeftNoBorderFclBL(""));
                cell = new PdfPCell();
                cell.setBorder(0);
                Paragraph p;
                if (CommonUtils.isNotEmpty(codeDesc)) {
                    p = new Paragraph(6f, "" + codeDesc, blackFontForFclBl);
                } else {
                    p = new Paragraph(6f, "" + transactionLedger.getChargeCode(), blackFontForFclBl);
                }
                p.setAlignment(Element.ALIGN_RIGHT);
                cell.addElement(p);
                chargesTable.addCell(cell);
                cell = makeCell("USD", Element.ALIGN_CENTER, blackFontForFclBl, 0.06f);
                if (chargeCount == 1) {
                    cell.setBorderWidthTop(0.0f);
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                } else {
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                }
                chargesTable.addCell(cell);
                cell = makeCell(number.format(amount), Element.ALIGN_RIGHT, blackFontForFclBl, Rectangle.BOX);
                if (chargeCount == 1) {
                    cell.setBorderWidth(0.0f);
                } else {
                    cell.setBorderWidthLeft(0.0f);
                    cell.setBorderWidthRight(0.0f);
                    cell.setBorderWidthBottom(0.0f);
                }
                chargesTable.addCell(cell);
            }
        }
        for (int i = 0; i < (14 - chargeCount); i++) {
            chargesTable.addCell(makeCellLeftNoBorderFclBL(""));
            chargesTable.addCell(makeCellRightNoBorderFclBL(""));
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
        cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell(type + " TOTAL", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell("USD", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthBottom(0.06f);
        chargesTable.addCell(cell);
        cell = makeCell(number.format(total), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
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

        payAmount = total;
        String creditStatus = new CustomerAccountingDAO().getCreditStatus(correction.getToPartyNo());
        boolean lateFeeFlag = false;
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        String lateFeeDate = "";
        PdfPTable paidTable = makeTable(5);
        paidTable.setWidthPercentage(100.5f);
        paidTable.setWidths(new float[]{45, 25, 10, 5, 15});
        if (CommonUtils.isNotEqualIgnoreEmpty(creditStatus, "No Credit")) {
            TradingPartner tradingPartner = tradingPartnerBC.findTradingPartnerById(correction.getToPartyNo());
            if (CommonUtils.isNotEmpty(tradingPartner.getAccounting())) {
                for (CustomerAccounting customerAccounting : tradingPartner.getAccounting()) {
                    if (CommonUtils.isEqualIgnoreCase(customerAccounting.getFclApplyLateFee(), "ON")) {
                        if (correction.getDate() != null) {
                            int creditTerm = Integer.parseInt(customerAccounting.getCreditRate().getCode());
                            Calendar c = Calendar.getInstance();
                            c.setTime(correction.getDate());
                            c.add(Calendar.DATE, creditTerm);
                            lateFeeDate = simpDate.format(c.getTime());
                            lateFeeFlag = true;
                        }
                    }
                    break;
                }
            }
        }

        if (lateFeeFlag) {
            lateFee = total * 0.015; // 1.5percent calculate
            payAmount = total + lateFee;
            cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 0);
            paidTable.addCell(cell);
            cell = makeCell("LATE FEE IF NOT PAID BY - ", Element.ALIGN_LEFT, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthLeft(0.06f);
            cell.setBorderWidthRight(0.06f);
            paidTable.addCell(cell);
            cell = makeCell(lateFeeDate, Element.ALIGN_LEFT, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.06f);
            paidTable.addCell(cell);
            cell = makeCell("USD", Element.ALIGN_CENTER, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.06f);
            cell.setBorderWidthLeft(0.06f);
            paidTable.addCell(cell);
            cell = makeCell(number.format(lateFee), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
            cell.setBorderWidthTop(0.06f);
            paidTable.addCell(cell);
        }
        cell = makeCell("", Element.ALIGN_LEFT, blackFontForFclBl, 1);
        paidTable.addCell(cell);
        cell = makeCell("PLEASE PAY THIS AMOUNT ", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setPaddingLeft(-12f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setColspan(2);
        paidTable.addCell(cell);
        cell = makeCell("USD", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthTop(0.06f);
        paidTable.addCell(cell);
        cell = makeCell(number.format(payAmount), Element.ALIGN_RIGHT, blackFontForFclBl, 0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        paidTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, blackFontForFclBl, 0);
        cell.setColspan(5);
        paidTable.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(paidTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        mainTable.addCell(cell);

        PdfPTable commandTable = new PdfPTable(1);
        commandTable.setWidthPercentage(100);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        if (lateFeeFlag == false) {
            cell = makeCell("THIS " + type + " IS DUE UPON RECEIPT", Element.ALIGN_CENTER, new Font(Font.HELVETICA, 10, Font.BOLDITALIC, Color.BLACK), Rectangle.NO_BORDER);
            commandTable.addCell(cell);
        } else {
            cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
            commandTable.addCell(cell);
        }
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = makeCell("", Element.ALIGN_CENTER, headingFont, Rectangle.NO_BORDER);
        commandTable.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.addElement(commandTable);
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthLeft(0.06f);
        cell.setExtraParagraphSpace(10f);
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
        bankDetails.addCell(makeCell("ACCT NAME: " + eftAcctName, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("Acct no.: " + eftAccountNo, Element.ALIGN_CENTER, blackBoldFontSize6, 0));
        bankDetails.addCell(makeCell("ABA Routing no.: " + eftABANo, Element.ALIGN_CENTER, blackBoldFontSize6, 0));

        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        //  cell.setBorderWidthBottom(0.06f);
        bankDetailsTable.addCell(cell);
        cell = new PdfPCell();
        cell.addElement(bankDetails);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.06f);
        cell.setBorderWidthRight(0.06f);
        //  cell.setBorderWidthBottom(0.06f);
        bankDetailsTable.addCell(cell);

        cell = makeCell("", Element.ALIGN_CENTER, blackBoldFontSize6, 0);
        // cell.setBorderWidthBottom(0.06f);
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

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            //this for print page number at the bottom in the format x of y
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            String type = CommonUtils.isEqualIgnoreCase(correction.getDebitOrCreditNote(), FclBlConstants.CREDTINOTE) ? "CREDIT NOTE" : "INVOICE";
            String correctionNo = type + "# " + "04" + correction.getFileNo() + "==" + correction.getNoticeNo();
            String text = "Page " + writer.getPageNumber() + " of ";
            float textBase = document.bottom() - (document.bottomMargin() - 30);
            //float textBase = document.bottom() - 20;
            float textSize = helv.getWidthPoint(text, 12);
            cb.beginText();
            cb.setFontAndSize(helv, 7);
            cb.setTextMatrix(document.left(), textBase);
            cb.showText(correctionNo);
            cb.setTextMatrix(document.left() + 280, textBase);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(total, document.left() + 260 + textSize, textBase);
            cb.restoreState();
        } catch (Exception e) {
            log.info("onEndPage failed on " + new Date(), e);
            throw new ExceptionConverter(e);
        }
    }

    public void destroy() {
        document.close();
    }

    public String createFclBlCorrectionsReport(FclBlCorrections correction, String fileName,
            String contextPath, MessageResources messageResources, HttpServletRequest request) throws Exception {
        this.initialize(fileName, contextPath, correction);
        this.createBody(contextPath, correction, request);
        this.destroy();
        return "fileName";
    }
}
