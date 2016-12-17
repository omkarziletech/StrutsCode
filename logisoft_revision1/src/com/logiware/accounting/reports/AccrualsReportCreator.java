package com.logiware.accounting.reports;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.accounting.form.AccrualsForm;
import com.logiware.accounting.model.ResultModel;
import com.logiware.reports.BaseReportCreator;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

/**
 *
 * @author Lakshmi Narayanan
 */
public class AccrualsReportCreator extends BaseReportCreator {
    private static final Logger log = Logger.getLogger(AccrualsReportCreator.class);
    private AccrualsForm accrualsForm;

    public AccrualsReportCreator() {
    }

    public AccrualsReportCreator(AccrualsForm accrualsForm, String contextPath) {
	this.accrualsForm = accrualsForm;
	this.contextPath = contextPath;
    }

    private void init(String fileName) throws DocumentException, FileNotFoundException {
	document = new Document(PageSize.A4.rotate());
	document.setMargins(15, 15, 15, 30);
	writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
	writer.setUserunit(1f);
	writer.setPageEvent(new AccrualsReportCreator(accrualsForm, contextPath));
	document.open();
	writer.setOpenAction(PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), writer));
    }

    private String getSearchBy(String searchBy) {
	if ("invoice_number".equals(searchBy)) {
	    return "Invoice Number";
	} else if ("container_no".equals(searchBy)) {
	    return "Container Number";
	} else if ("drcpt".equals(searchBy)) {
	    return "Dock Receipt";
	} else if ("bill_ladding_no".equals(searchBy)) {
	    return "House Bill";
	} else if ("sub_house_bl".equals(searchBy)) {
	    return "Sub-House Bill";
	} else if ("voyage_no".equals(searchBy)) {
	    return "Voyage";
	} else if ("master_bl".equals(searchBy)) {
	    return "Master Bill";
	} else if ("booking_no".equals(searchBy)) {
	    return "Booking Number";
	} else {
	    return "";
	}
    }

    private void writeHeader() throws Exception {
	headerTable = new PdfPTable(1);
	headerTable.setWidthPercentage(100);
	String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
	Image image = Image.getInstance(contextPath + imagePath);
	image.scalePercent(75);
	PdfPCell logoCell = createImageCell(image);
	headerTable.addCell(logoCell);
	PdfPCell titleCell = createCell("Accruals Report", headerBoldFont15, Element.ALIGN_CENTER, Rectangle.BOX, Color.LIGHT_GRAY);
	headerTable.addCell(titleCell);
	PdfPCell cell = createCell("Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), headerBoldFont11, Element.ALIGN_RIGHT, Rectangle.NO_BORDER);
	headerTable.addCell(cell);
	PdfPTable filtersTable = new PdfPTable(4);
	filtersTable.setWidthPercentage(100);
	filtersTable.setWidths(new float[]{9, 20, 9, 62});
	if (CommonUtils.isNotEmpty(accrualsForm.getSearchBy())) {
	    filtersTable.addCell(createTextCell("Search By : ", blackBoldFont8, Rectangle.NO_BORDER));
	    filtersTable.addCell(createTextCell(getSearchBy(accrualsForm.getSearchBy()), blackNormalFont7, Rectangle.NO_BORDER));
	    filtersTable.addCell(createTextCell("Search Value : ", blackBoldFont8, Rectangle.NO_BORDER));
	    filtersTable.addCell(createTextCell(accrualsForm.getSearchValue(), blackNormalFont7, Rectangle.NO_BORDER));
	} else {
	    filtersTable.addCell(createTextCell("Vendor Name : ", blackBoldFont8, Rectangle.NO_BORDER));
	    String vendorName = CommonUtils.isNotEmpty(accrualsForm.getSearchVendorName()) ? accrualsForm.getSearchVendorName() : accrualsForm.getVendorName();
	    filtersTable.addCell(createTextCell(vendorName, blackNormalFont7, Rectangle.NO_BORDER));
	    String vendorNumber = CommonUtils.isNotEmpty(accrualsForm.getSearchVendorNumber()) ? accrualsForm.getSearchVendorNumber() : accrualsForm.getVendorNumber();
	    filtersTable.addCell(createTextCell("Vendor Number : ", blackBoldFont8, Rectangle.NO_BORDER));
	    filtersTable.addCell(createTextCell(vendorNumber, blackNormalFont7, Rectangle.NO_BORDER));
	}
	PdfPCell filtersCell = createEmptyCell(Rectangle.BOTTOM);
	filtersCell.addElement(filtersTable);
	headerTable.addCell(filtersCell);
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

    private void writeContent() throws Exception {
	contentTable = new PdfPTable(14);
	contentTable.setWidthPercentage(100);
	contentTable.setWidths(new float[]{15, 8, 10, 10, 8, 8, 8, 7, 7, 5, 5, 5, 5, 8});
	contentTable.addCell(createHeaderCell("Vendor Name", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Vendor Number", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Invoice Number", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("B/L", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Container", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Voyage", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("D/R", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Reporting Date", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Amount", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("BS Cost Code", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Cost Code", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Terminal", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Type", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.addCell(createHeaderCell("Status", blackBoldFont6, Element.ALIGN_CENTER, Rectangle.BOX));
	contentTable.setHeaderRows(1);
	List<ResultModel> accruals = new AccrualsDAO().searchForReport(accrualsForm);
	for (ResultModel accrual : accruals) {
	    contentTable.addCell(createTextCell(StringUtils.abbreviate(accrual.getVendorName(), 20), blackNormalFont6, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(accrual.getVendorNumber(), blackNormalFont6, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(accrual.getInvoiceNumber(), blackNormalFont6, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(accrual.getBlNumber(), blackNormalFont6, Rectangle.BOTTOM));
	    StringBuilder container = new StringBuilder();
	    if (CommonUtils.isNotEmpty(accrual.getContainer())) {
		int count = 1;
		String[] containers = StringUtils.splitByWholeSeparator(StringUtils.removeEnd(accrual.getContainer(), ","), ",");
		if (containers.length > 1) {
		    for (String con : containers) {
			container.append(count).append(") ").append(con).append("\n");
			count++;
		    }
		} else {
		    container.append(accrual.getContainer());
		}
	    }
	    contentTable.addCell(createTextCell(container.toString(), blackNormalFont6, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(accrual.getVoyage(), blackNormalFont6, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(accrual.getDockReceipt(), blackNormalFont6, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(accrual.getReportingDate(), blackNormalFont6, Rectangle.BOTTOM));
	    contentTable.addCell(createAmountCell(accrual.getAccruedAmount(), blackNormalFont6, redNormalFont6, Rectangle.BOTTOM));
	    if (CommonUtils.isEqualIgnoreCase(accrual.getTransactionType(), "AR")) {
		contentTable.addCell(createTextCell("", blackNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell("", blackNormalFont6, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell("", blackNormalFont6, Rectangle.BOTTOM));
	    } else {
		contentTable.addCell(createTextCell(accrual.getBluescreenCostCode(), blackNormalFont6, Element.ALIGN_CENTER, Rectangle.BOTTOM));
		contentTable.addCell(createTextCell(accrual.getCostCode(), blackNormalFont6, Rectangle.BOTTOM));
		String terminal = com.logiware.tags.String.lastSubString(accrual.getGlAccount(), "-");
		contentTable.addCell(createTextCell(terminal, blackNormalFont6, Element.ALIGN_CENTER, Rectangle.BOTTOM));
	    }
	    contentTable.addCell(createTextCell(accrual.getTransactionType(), blackNormalFont6, Element.ALIGN_CENTER, Rectangle.BOTTOM));
	    contentTable.addCell(createTextCell(accrual.getStatus(), blackNormalFont6, Rectangle.BOTTOM));
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
	fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/AccountPayable/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	File file = new File(fileName.toString());
	if (!file.exists()) {
	    file.mkdirs();
	}
	fileName.append("Accruals.pdf");
	init(fileName.toString());
	writeContent();
	exit();
	return fileName.toString();
    }
}
