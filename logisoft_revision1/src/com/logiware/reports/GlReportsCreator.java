package com.logiware.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.bean.ReportBean;
import com.logiware.form.GlReportsForm;
import com.logiware.hibernate.dao.GlReportsDAO;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;
/**
 *
 * @author Lakshmi Naryanan
 */
public class GlReportsCreator extends BaseReportCreator {
private static final Logger log = Logger.getLogger(GlReportsCreator.class);
    private GlReportsForm glReportsForm;

    public GlReportsCreator() {
    }

    public GlReportsCreator(GlReportsForm glReportsForm, String contextPath) {
	this.glReportsForm = glReportsForm;
	this.contextPath = contextPath;
    }

    private void init(String fileName) throws Exception {
	document = new Document(PageSize.A4.rotate());
	document.setMargins(5, 5, 5, 30);
	writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
	writer.setUserunit(1f);
	writer.setPageEvent(new GlReportsCreator(glReportsForm, contextPath));
	document.open();
	writer.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 1f), writer));
    }

    private void writeChargeCodeHeader() throws Exception {
	String title = "Charge Code Report";
	PdfPCell titleCell = createCell(title, headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOTTOM, Color.LIGHT_GRAY);
	headerTable.addCell(titleCell);
	PdfPTable filtersTable = new PdfPTable(4);
	filtersTable.setWidthPercentage(100);
	filtersTable.setWidths(new float[]{8, 15, 7, 70});
	filtersTable.addCell(createTextCell("Charge Code : ", blackBoldFont8, Rectangle.NO_BORDER));
	filtersTable.addCell(createTextCell(glReportsForm.getChargeCode(), blackNormalFont7, Rectangle.NO_BORDER));
	filtersTable.addCell(createTextCell("Date Range : ", blackBoldFont8, Rectangle.NO_BORDER));
	filtersTable.addCell(createTextCell(glReportsForm.getFromDate() + " - " + glReportsForm.getToDate(), blackNormalFont7, Rectangle.NO_BORDER));
	PdfPCell filtersCell = createEmptyCell(Rectangle.BOTTOM);
	filtersCell.addElement(filtersTable);
	headerTable.addCell(filtersCell);
    }

    private void writeGlCodeHeader() throws Exception {
	String title = "GL Code Report";
	PdfPCell titleCell = createCell(title, headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOTTOM, Color.LIGHT_GRAY);
	headerTable.addCell(titleCell);
	PdfPTable filtersTable = new PdfPTable(4);
	filtersTable.setWidthPercentage(100);
	filtersTable.setWidths(new float[]{8, 15, 7, 70});
	filtersTable.addCell(createTextCell("GL Account : ", blackBoldFont8, Rectangle.NO_BORDER));
	filtersTable.addCell(createTextCell(glReportsForm.getGlAccount(), blackNormalFont7, Rectangle.NO_BORDER));
	filtersTable.addCell(createTextCell("Date Range : ", blackBoldFont8, Rectangle.NO_BORDER));
	filtersTable.addCell(createTextCell(glReportsForm.getFromDate() + " - " + glReportsForm.getToDate(), blackNormalFont7, Rectangle.NO_BORDER));
	PdfPCell filtersCell = createEmptyCell(Rectangle.BOTTOM);
	filtersCell.addElement(filtersTable);
	headerTable.addCell(filtersCell);
    }

    private void writeCashHeader() throws Exception {
	String title = "Cash Report";
	PdfPCell titleCell = createCell(title, headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOTTOM, Color.LIGHT_GRAY);
	headerTable.addCell(titleCell);
	PdfPTable filtersTable = new PdfPTable(2);
	filtersTable.setWidthPercentage(100);
	filtersTable.setWidths(new float[]{10, 90});
	filtersTable.addCell(createTextCell("Reporting date : ", blackBoldFont8, Rectangle.NO_BORDER));
	filtersTable.addCell(createTextCell(glReportsForm.getReportingDate(), blackNormalFont7, Rectangle.NO_BORDER));
	PdfPCell filtersCell = createEmptyCell(Rectangle.BOTTOM);
	filtersCell.addElement(filtersTable);
	headerTable.addCell(filtersCell);
    }

    private void writeFclPlHeader() throws Exception {
	String title = "FCL PL Report";
	PdfPCell titleCell = createCell(title, headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOTTOM, Color.LIGHT_GRAY);
	headerTable.addCell(titleCell);
	PdfPTable filtersTable = new PdfPTable(2);
	filtersTable.setWidthPercentage(100);
	filtersTable.setWidths(new float[]{10, 90});
	filtersTable.addCell(createTextCell("Report Type : ", blackBoldFont8, Rectangle.NO_BORDER));
	filtersTable.addCell(createTextCell(glReportsForm.getReportType(), blackNormalFont7, Rectangle.NO_BORDER));
	filtersTable.addCell(createTextCell("GL Period : ", blackBoldFont8, Rectangle.NO_BORDER));
	filtersTable.addCell(createTextCell(glReportsForm.getGlPeriod(), blackNormalFont7, Rectangle.NO_BORDER));
	filtersTable.addCell(createTextCell("Date Range : ", blackBoldFont8, Rectangle.NO_BORDER));
	filtersTable.addCell(createTextCell(glReportsForm.getFromDate() + " - " + glReportsForm.getToDate(), blackNormalFont7, Rectangle.NO_BORDER));
	PdfPCell filtersCell = createEmptyCell(Rectangle.BOTTOM);
	filtersCell.addElement(filtersTable);
	headerTable.addCell(filtersCell);
    }

    private void writeHeader() throws Exception {
	headerTable = new PdfPTable(1);
	headerTable.setWidthPercentage(100);
	String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
	Image image = Image.getInstance(contextPath + imagePath);
	image.scalePercent(75);
	headerTable.addCell(createImageCell(image));
	headerTable.addCell(createEmptyCell(Rectangle.BOTTOM));
	if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "chargeCode")) {
	    writeChargeCodeHeader();
	} else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "glCode")) {
	    writeGlCodeHeader();
	} else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "cash")) {
	    writeCashHeader();
	} else {
	    writeFclPlHeader();
	}
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
	pageTemplate = writer.getDirectContent().createTemplate(20, 10);
	pageTemplate.setBoundingBox(new Rectangle(-20, -20, 20, 50));
	try {
	    helvFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
	} catch (Exception e) {
	    log.info("onOpenDocument failed on " + new Date(),e);
	}
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
	try {
	    writeHeader();
	    document.add(headerTable);
	} catch (Exception e) {
	    log.info("onStartPage failed on " + new Date(),e);
	}
    }

    private void writeChargeCodeContent() throws Exception {
	List<ReportBean> chargeCodes = new GlReportsDAO().getChargeCodes(glReportsForm);
	contentTable = new PdfPTable(13);
	contentTable.setWidthPercentage(100);
	contentTable.setWidths(new float[]{15, 8, 9, 9, 8, 7, 6, 6, 6, 7, 7, 6, 6});
	contentTable.addCell(createHeaderCell("TP Name", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("TP Number", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Invoice", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("B/L", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Voyage", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("D/R", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Enter Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Reporting Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Posted Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Revenue", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Cost", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("User", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Type", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.setHeaderRows(1);
	double totalRevenue = 0d;
	double totalCost = 0d;
	for (ReportBean chargeCode : chargeCodes) {
	    contentTable.addCell(createTextCell(StringUtils.left(chargeCode.getCustomerName(), 25), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(chargeCode.getCustomerNumber(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(chargeCode.getInvoiceNumber(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(chargeCode.getBlNumber(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(chargeCode.getVoyageNumber(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(chargeCode.getDockReceipt(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(chargeCode.getCreatedDate(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(chargeCode.getReportingDate(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(chargeCode.getPostedDate(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(chargeCode.getRevenue(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(chargeCode.getCost(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(chargeCode.getCreatedBy(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(chargeCode.getType(), blackNormalFont7, Rectangle.BOTTOM));
	    totalRevenue += Double.parseDouble(chargeCode.getRevenue().replace(",", ""));
	    totalCost += Double.parseDouble(chargeCode.getCost().replace(",", ""));
	}
	PdfPCell grandTotalCell = createCell("Grand Total", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, LAVENDAR);
	grandTotalCell.setColspan(9);
	contentTable.addCell(grandTotalCell);
	contentTable.addCell(createAmountCell(totalRevenue, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(totalCost, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
    }

    private void writeGlCodeContent() throws Exception {
	List<ReportBean> glCodes = new GlReportsDAO().getGlCodes(glReportsForm);
	contentTable = new PdfPTable(14);
	contentTable.setWidthPercentage(100);
	contentTable.setWidths(new float[]{7, 13, 8, 6, 6, 6, 6, 6, 6, 6, 9, 7, 7, 7});
	contentTable.addCell(createHeaderCell("G/L", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("TP Name", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("TP Number", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Invoice", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("B/L", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Voyage", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("D/R", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Transaction Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Reporting Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Posted Date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Amount", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("User", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Type", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("JE Batch", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.setHeaderRows(1);
	double totalAmount = 0d;
	for (ReportBean glCode : glCodes) {
	    contentTable.addCell(createTextCell(glCode.getAccount(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(StringUtils.left(glCode.getCustomerName(), 35), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(glCode.getCustomerNumber(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(glCode.getInvoiceNumber(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(glCode.getBlNumber(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(glCode.getVoyageNumber(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(glCode.getDockReceipt(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(glCode.getCreatedDate(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(glCode.getReportingDate(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(glCode.getPostedDate(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(glCode.getAmount(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(glCode.getCreatedBy(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(glCode.getType(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(glCode.getJeBatch(), blackNormalFont7, Rectangle.BOTTOM));
	    totalAmount += Double.parseDouble(glCode.getAmount().replace(",", ""));
	}
	PdfPCell grandTotalCell = createCell("Grand Total", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, LAVENDAR);
	grandTotalCell.setColspan(10);
	contentTable.addCell(grandTotalCell);
	contentTable.addCell(createAmountCell(totalAmount, blackNormalFont7, redNormalFont7, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createEmptyCell(Rectangle.BOTTOM, LAVENDAR));
    }

    private void writeCashContent() throws Exception {
	List<ReportBean> cashAccounts = new GlReportsDAO().getCashAccounts(glReportsForm);
	contentTable = new PdfPTable(5);
	contentTable.setWidthPercentage(100);
	contentTable.setWidths(new float[]{15, 30, 20, 20, 15});
	contentTable.addCell(createHeaderCell("G/L", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Account name", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("G/L balance", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Bank balance", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOTTOM));
	contentTable.addCell(createHeaderCell("Last reconcile date", blackBoldFont7, Element.ALIGN_LEFT, Rectangle.BOTTOM));
	contentTable.setHeaderRows(1);
	for (ReportBean cashAccount : cashAccounts) {
	    contentTable.addCell(createTextCell(cashAccount.getGlAccountNo(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(cashAccount.getAccountName(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(cashAccount.getGlBalance(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(cashAccount.getBankBalance(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(cashAccount.getLastReconciledDate(), blackNormalFont7, Rectangle.BOTTOM));
	}
    }

    private void writeFclPlContent() throws Exception {
	List<ReportBean> fclPlFiles = new GlReportsDAO().getFclPlFiles(glReportsForm);
	contentTable = new PdfPTable(20);
	contentTable.setWidthPercentage(100);
	contentTable.setWidths(new float[]{11, 5, 5.5f, 5.5f, 5.5f, 5, 5, 5.5f, 5, 5, 5, 4, 4, 5, 5, 5, 5, 5, 5, 5});
	Color PINK = Color.decode("#c91ac9");
	Color GREEN = Color.decode("#92d050");
	Color BLUE = Color.decode("#4bacc6");
	Color BROWN = Color.decode("#c0504d");
	contentTable.addCell(createHeaderCell("File", blackBoldFont7, Element.ALIGN_CENTER, Rectangle.BOX, PINK));
	contentTable.addCell(createHeaderCell("Reporting Date", blackBoldFont7, Element.ALIGN_CENTER, Rectangle.BOX, PINK));
	contentTable.addCell(createHeaderCell("Total Revenue", blackBoldFont7, Element.ALIGN_CENTER, Rectangle.BOX, GREEN));
	contentTable.addCell(createHeaderCell("Total Cost", blackBoldFont7, Element.ALIGN_CENTER, Rectangle.BOX, GREEN));
	contentTable.addCell(createHeaderCell("Profit", blackBoldFont7, Element.ALIGN_CENTER, Rectangle.BOX, GREEN));
	contentTable.addCell(createHeaderCell("O/F Rev", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BLUE));
	contentTable.addCell(createHeaderCell("Deferral", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BLUE));
	contentTable.addCell(createHeaderCell("O/F Cost", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BLUE));
	contentTable.addCell(createHeaderCell("Oversold", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BLUE));
	contentTable.addCell(createHeaderCell("Truck Rev", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BROWN));
	contentTable.addCell(createHeaderCell("Truck Cost", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BROWN));
	contentTable.addCell(createHeaderCell("Whs Rev", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BLUE));
	contentTable.addCell(createHeaderCell("Whs Cost", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BLUE));
	contentTable.addCell(createHeaderCell("Doc Fee", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BROWN));
	contentTable.addCell(createHeaderCell("FFcom", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BLUE));
	contentTable.addCell(createHeaderCell("FAE", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BROWN));
	contentTable.addCell(createHeaderCell("Exch Rev", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BLUE));
	contentTable.addCell(createHeaderCell("Exch Cost", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BLUE));
	contentTable.addCell(createHeaderCell("Other Rev", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BROWN));
	contentTable.addCell(createHeaderCell("Other Cost", blackBoldFont7, Element.ALIGN_RIGHT, Rectangle.BOX, BROWN));
	contentTable.setHeaderRows(1);
	double sales = 0d;
	double costs = 0d;
	double profits = 0d;
	double oceanfreightRevenue = 0d;
	double oceanfreightDeferral = 0d;
	double oceanfreightCost = 0d;
	double oceanfreightOversold = 0d;
	double drayRevenue = 0d;
	double drayCost = 0d;
	double warehouseRevenue = 0d;
	double warehouseCost = 0d;
	double documentRevenue = 0d;
	double ffcomCost = 0d;
	double faeCost = 0d;
	double passRevenue = 0d;
	double passCost = 0d;
	double otherRevenue = 0d;
	double otherCost = 0d;
	for (ReportBean fclPlFile : fclPlFiles) {
	    contentTable.addCell(createTextCell(fclPlFile.getFile(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(fclPlFile.getReportingDate(), blackNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getSales(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getCosts(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getProfits(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getOceanfreightRevenue(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getOceanfreightDeferral(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getOceanfreightCost(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getOceanfreightOversold(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getDrayRevenue(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getDrayCost(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getWarehouseRevenue(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getWarehouseCost(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getDocumentRevenue(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getFfcomCost(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getFaeCost(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getPassRevenue(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getPassCost(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getOtherRevenue(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(fclPlFile.getOtherCost(), blackNormalFont7, redNormalFont7, Rectangle.BOTTOM));
	    sales += Double.parseDouble(fclPlFile.getSales().replace(",", ""));
	    costs += Double.parseDouble(fclPlFile.getCosts().replace(",", ""));
	    profits += Double.parseDouble(fclPlFile.getProfits().replace(",", ""));
	    oceanfreightRevenue += Double.parseDouble(fclPlFile.getOceanfreightRevenue().replace(",", ""));
	    oceanfreightDeferral += Double.parseDouble(fclPlFile.getOceanfreightDeferral().replace(",", ""));
	    oceanfreightCost += Double.parseDouble(fclPlFile.getOceanfreightCost().replace(",", ""));
	    oceanfreightOversold += Double.parseDouble(fclPlFile.getOceanfreightOversold().replace(",", ""));
	    drayRevenue += Double.parseDouble(fclPlFile.getDrayRevenue().replace(",", ""));
	    drayCost += Double.parseDouble(fclPlFile.getDrayCost().replace(",", ""));
	    warehouseRevenue += Double.parseDouble(fclPlFile.getWarehouseRevenue().replace(",", ""));
	    warehouseCost += Double.parseDouble(fclPlFile.getWarehouseCost().replace(",", ""));
	    documentRevenue += Double.parseDouble(fclPlFile.getDocumentRevenue().replace(",", ""));
	    ffcomCost += Double.parseDouble(fclPlFile.getFfcomCost().replace(",", ""));
	    faeCost += Double.parseDouble(fclPlFile.getFaeCost().replace(",", ""));
	    passRevenue += Double.parseDouble(fclPlFile.getPassRevenue().replace(",", ""));
	    passCost += Double.parseDouble(fclPlFile.getPassCost().replace(",", ""));
	    otherRevenue += Double.parseDouble(fclPlFile.getOtherRevenue().replace(",", ""));
	    otherCost += Double.parseDouble(fclPlFile.getOtherCost().replace(",", ""));
	}
	PdfPCell cell = createCell("Total: ", redBoldFont8, Element.ALIGN_RIGHT, Rectangle.BOTTOM, LAVENDAR);
	cell.setColspan(2);
	contentTable.addCell(cell);
	contentTable.addCell(createAmountCell(sales, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(costs, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(profits, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(oceanfreightRevenue, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(oceanfreightDeferral, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(oceanfreightCost, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(oceanfreightOversold, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(drayRevenue, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(drayCost, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(warehouseRevenue, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(warehouseCost, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(documentRevenue, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(ffcomCost, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(faeCost, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(passRevenue, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(passCost, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(otherRevenue, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
	contentTable.addCell(createAmountCell(otherCost, blackNormalFont6, redNormalFont6, Rectangle.BOTTOM, LAVENDAR));
    }

    private void writeContent() throws Exception {
	if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "chargeCode")) {
	    writeChargeCodeContent();
	} else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "glCode")) {
	    writeGlCodeContent();
	} else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "cash")) {
	    writeCashContent();
	} else {
	    writeFclPlContent();
	}
	document.add(contentTable);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
	PdfContentByte cb = writer.getDirectContent();
	cb.saveState();
	String text = "Page " + writer.getPageNumber() + " of ";
	float textBase = document.bottom() - 20;
	float textSize = helvFont.getWidthPoint(text, 12);
	cb.beginText();
	cb.setFontAndSize(helvFont, 12);
	cb.setTextMatrix((document.right() / 2) - (textSize / 2), textBase);
	cb.showText(text);
	cb.endText();
	cb.addTemplate(pageTemplate, (document.right() / 2) + (textSize / 2), textBase);
	cb.restoreState();
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
	pageTemplate.beginText();
	pageTemplate.setFontAndSize(helvFont, 12);
	pageTemplate.setTextMatrix(0, 0);
	pageTemplate.showText(String.valueOf(writer.getPageNumber() - 1));
	pageTemplate.endText();
    }

    private void exit() {
	document.close();
    }

    public String create() throws Exception {
	StringBuilder fileName = new StringBuilder();
        fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/GLReports/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	File file = new File(fileName.toString());
	if (!file.exists()) {
	    file.mkdirs();
	}
	if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "chargeCode")) {
	    fileName.append("ChargeCodeReport.pdf");
	} else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "glCode")) {
	    fileName.append("GLCodeReport.pdf");
	} else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "cash")) {
	    fileName.append("CashReport.pdf");
	} else {
	    fileName.append("Fcl_Pl_").append(glReportsForm.getReportType().replace(" ", "_")).append(".pdf");
	}
	init(fileName.toString());
	writeContent();
	exit();
	return fileName.toString();
    }
}
