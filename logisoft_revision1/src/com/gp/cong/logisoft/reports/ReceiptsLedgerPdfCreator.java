package com.gp.cong.logisoft.reports;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.logisoft.ExcelGenerator.ExcelSheetConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.Subledger;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.SubledgerDAO;
import com.gp.cvst.logisoft.struts.form.RecieptsLedgerForm;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;

@SuppressWarnings("unchecked")
public class ReceiptsLedgerPdfCreator extends ReportFormatMethods {

    Document document = null;

    private void initialize(String fileName) throws FileNotFoundException,
	    DocumentException {
	document = new Document(PageSize.A4.rotate());
	document.setMargins(10, 10, 10, 10);
	PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	document.open();
	PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
	pdfWriter.setOpenAction(action);
    }
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private NumberFormat number = new DecimalFormat("###,###,##0.00");
    private Double total = 0d;

    private void createBody(RecieptsLedgerForm recieptsLedgerForm, List<TransactionBean> subLedgerList, String realPath) throws DocumentException, IOException, IOException, Exception {

	// table for company details and logo
	String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
	Image img = Image.getInstance(realPath + imagePath);
	PdfPTable imagetable = new PdfPTable(1);
	imagetable.setWidthPercentage(100);
	img.scalePercent(50);
	PdfPCell imageCell = new PdfPCell();
	imageCell.addElement(new Chunk(img, 200, -12));
	imageCell.setBorder(0);
	imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	imageCell.setVerticalAlignment(Element.ALIGN_CENTER);
	imagetable.addCell(imageCell);
	document.add(imagetable);

	PdfPTable emptyTable = new PdfPTable(2);
	emptyTable.setWidthPercentage(100);
	emptyTable.setWidths(new float[]{50, 50});
	emptyTable.getDefaultCell().setPadding(0);
	emptyTable.getDefaultCell().setBorderWidth(0.5f);
	emptyTable.getDefaultCell().setBorderWidthLeft(0.0f);
	emptyTable.getDefaultCell().setBorderWidthRight(0.0f);

	emptyTable.addCell(makeCellCenterNoBorder(""));
	emptyTable.addCell(makeCellCenterNoBorder(""));
	emptyTable.addCell(makeCellCenterNoBorder(""));
	emptyTable.addCell(makeCellCenterNoBorder(""));
	emptyTable.addCell(makeCellCenterNoBorder(""));
	emptyTable.addCell(makeCellCenterNoBorder(""));
	emptyTable.addCell(makeCellCenterNoBorder(""));
	emptyTable.addCell(makeCellCenterNoBorder(""));
	emptyTable.addCell(makeCellCenterNoBorder(""));
	emptyTable.addCell(makeCellCenterNoBorder(""));
	emptyTable.addCell(makeCellCenterNoBorder(""));
	emptyTable.addCell(makeCellCenterNoBorder(""));
	emptyTable.addCell(makeCellCenterNoBorder(""));
	emptyTable.addCell(makeCellCenterNoBorder(""));

	document.add(emptyTable);
	String subLedgerType = recieptsLedgerForm.getSubLedgerType();
	if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.SUB_LEDGER_CODE_PURCHASE_JOURNAL)) {
	    this.createPJSubLedgerReport(subLedgerType, recieptsLedgerForm, subLedgerList);
	} else if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.SUB_LEDGER_CODE_ACCRUALS)) {
	    this.createACCSubLedgerReport(subLedgerType, recieptsLedgerForm, subLedgerList);
	} else {
	    PdfPTable table = new PdfPTable(18);
	    table.setWidthPercentage(100);
	    table.setWidths(new float[]{7, 7, 7, 5, 5, 7, 7, 7, 7, 5, 5, 7, 7, 7, 5, 5, 7, 7});
	    if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.ALL)) {
		table = new PdfPTable(19);
		table.setWidthPercentage(100);
		table.setWidths(new float[]{7, 7, 7, 5, 5, 7, 7, 7, 7, 5, 5, 7, 7, 7, 5, 5, 7, 7, 6});
		PdfPCell titleCell = makeCell("SubLedger : All", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, 0);
		titleCell.setColspan(13);
		table.addCell(titleCell);
	    } else if (null != subLedgerType && !subLedgerType.trim().equals("")) {
		String reportName = "";
		SubledgerDAO subledgerDAO = new SubledgerDAO();
		List<Subledger> list = subledgerDAO.findByProperty("subLedgerCode", subLedgerType);
		for (Subledger subledger : list) {
		    reportName = null != subledger.getSubLedgerDesc() ? subledger.getSubLedgerDesc() : "";
		}
		PdfPCell titleCell = makeCell("SubLedger : " + reportName, Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, 0);
		titleCell.setColspan(12);
		table.addCell(titleCell);
	    }
	    Date date = new Date();
	    PdfPCell dateCell = makeCell("Date: " + simpleDateFormat.format(date), Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0);
	    dateCell.setColspan(6);
	    table.addCell(dateCell);
	    FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
	    FiscalPeriod fiscalPeriod = fiscalPeriodDAO.findById(Integer.parseInt(recieptsLedgerForm.getPeriod()));
	    if (null != fiscalPeriod) {
		PdfPCell periodCell = makeCell("Period:  " + fiscalPeriod.getPeriodDis(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, textFontForBatch, 0);
		if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.ALL)) {
		    periodCell.setColspan(9);
		} else {
		    periodCell.setColspan(8);
		}
		table.addCell(periodCell);
		PdfPCell startDateCell = makeCell("Start Date:  " + recieptsLedgerForm.getStartDate(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, textFontForBatch, 0);
		startDateCell.setColspan(5);
		table.addCell(startDateCell);

		PdfPCell endDateCell = makeCell("End Date: " + recieptsLedgerForm.getEndDate(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, textFontForBatch, 0);
		endDateCell.setColspan(5);
		table.addCell(endDateCell);
	    }
	    //add headers
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_VENDOR_NAME, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_VENDOR_ACCOUNT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.ALL)) {
		table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_TYPE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    }
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_AP_BATCH_ID, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_AR_BATCH_ID, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_GL_ACCOUNT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_BL_NUMBER, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_INVOICE_NUMBER, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_VOYAGE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_CHARGE_CODE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_RECORD_TYPE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_TRANSACTION_DATE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_REPORTING_DATE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_POSTED_DATE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_DEBIT, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_CREDIT, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_LINE_ITEM_NUMBER, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_JOURNAL_ENTRY_NUMBER, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_SUB_TOTAL, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	    int i = 0;
	    total = 0d;
	    blackFontSize6 = new Font(Font.HELVETICA, 6, 0, Color.BLACK);
	    for (TransactionBean transactionBean : subLedgerList) {
		table.addCell(makeCell(StringUtils.abbreviate(transactionBean.getCustomer(), 12), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		table.addCell(makeCell(transactionBean.getCustomerNo(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.ALL)) {
		    table.addCell(makeCell(transactionBean.getSubLedgerCode(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		}
		String arBatchId = null != transactionBean.getArBatchId() ? transactionBean.getArBatchId().toString() : "";
		String apBatchId = null != transactionBean.getApBatchId() ? transactionBean.getApBatchId().toString() : "";
		table.addCell(makeCell(apBatchId, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		table.addCell(makeCell(arBatchId, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		table.addCell(makeCell(transactionBean.getGlAcctNo(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		table.addCell(makeCell(transactionBean.getBillofLadding(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		table.addCell(makeCell((null != transactionBean.getInvoiceOrBl() ? transactionBean.getInvoiceOrBl().toUpperCase() : transactionBean.getInvoiceOrBl()), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		table.addCell(makeCell(transactionBean.getVoyagenumber(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		table.addCell(makeCell(transactionBean.getChargeCode(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		table.addCell(makeCell(transactionBean.getRecordType(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		table.addCell(makeCell(transactionBean.getTransDate(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		if (null != transactionBean.getSailingDate()) {
		    table.addCell(makeCell(simpleDateFormat.format(transactionBean.getSailingDate()), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		} else {
		    table.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		}
		if (null != transactionBean.getPostedDate()) {
		    table.addCell(makeCell(simpleDateFormat.format(transactionBean.getPostedDate()), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		} else {
		    table.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		}
		table.addCell(makeCell(transactionBean.getDebit(), Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackFontSize6, 0));
		table.addCell(makeCell(transactionBean.getCredit(), Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackFontSize6, 0));
		table.addCell(makeCell(transactionBean.getLineitemNo(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		table.addCell(makeCell(transactionBean.getJournalentryNo(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
		String subTotal = "";
		if (null != recieptsLedgerForm.getSortBy() && !recieptsLedgerForm.getSortBy().trim().equals("0")) {
		    subTotal = this.calculateSubTotal(recieptsLedgerForm.getSortBy(), transactionBean, subLedgerList, i);
		}
		if (null != subTotal && subTotal.contains("-")) {
		    subTotal = "(" + StringUtils.replace(subTotal, "-", "") + ")";
		    table.addCell(makeCell(subTotal, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, redFontSize6, 0));
		} else {
		    table.addCell(makeCell(subTotal, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackFontSize6, 0));
		}
		i++;
	    }
	    document.add(table);
	}
    }

    private void createPJSubLedgerReport(String subLedgerType, RecieptsLedgerForm recieptsLedgerForm, List<TransactionBean> subLedgerList) throws DocumentException, Exception {
	PdfPTable table = new PdfPTable(16);
	table.setWidthPercentage(100);
	table.setWidths(new float[]{10, 7, 5, 5, 7, 7, 7, 7, 7, 7, 5, 5, 5, 6, 6, 6});
	String reportName = "";
	SubledgerDAO subledgerDAO = new SubledgerDAO();
	List<Subledger> list = subledgerDAO.findByProperty("subLedgerCode", subLedgerType);
	for (Subledger subledger : list) {
	    reportName = null != subledger.getSubLedgerDesc() ? subledger.getSubLedgerDesc() : "";
	}
	PdfPCell titleCell = makeCell("SubLedger : " + reportName, Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, 0);
	titleCell.setColspan(6);
	table.addCell(titleCell);
	Date date = new Date();
	PdfPCell dateCell = makeCell("Date: " + simpleDateFormat.format(date), Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0);
	dateCell.setColspan(10);
	table.addCell(dateCell);
	FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
	FiscalPeriod fiscalPeriod = fiscalPeriodDAO.findById(Integer.parseInt(recieptsLedgerForm.getPeriod()));
	if (null != fiscalPeriod) {
	    PdfPCell periodCell = makeCell("Period:  " + fiscalPeriod.getPeriodDis(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, textFontForBatch, 0);
	    periodCell.setColspan(4);
	    table.addCell(periodCell);
	    PdfPCell startDateCell = makeCell("Start Date:  " + recieptsLedgerForm.getStartDate(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, textFontForBatch, 0);
	    startDateCell.setColspan(4);
	    table.addCell(startDateCell);

	    PdfPCell endDateCell = makeCell("End Date: " + recieptsLedgerForm.getEndDate(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, textFontForBatch, 0);
	    endDateCell.setColspan(8);
	    table.addCell(endDateCell);
	}
	//add headers
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_VENDOR_NAME, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_VENDOR_ACCOUNT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_AP_BATCH_ID, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_AR_BATCH_ID, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_GL_ACCOUNT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_BL_NUMBER, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_INVOICE_NUMBER, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_INVOICE_FOR, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_VOYAGE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_CHARGE_CODE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_RECORD_TYPE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_TRANSACTION_DATE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_REPORTING_DATE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_POSTED_DATE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_TRANSACTION_AMOUNT, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_SUB_TOTAL, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	int i = 0;
	total = 0d;
	for (TransactionBean transactionBean : subLedgerList) {
	    table.addCell(makeCell(StringUtils.abbreviate(transactionBean.getCustomer(), 15), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getCustomerNo(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    String arBatchId = null != transactionBean.getArBatchId() ? transactionBean.getArBatchId().toString() : "";
	    String apBatchId = null != transactionBean.getApBatchId() ? transactionBean.getApBatchId().toString() : "";
	    table.addCell(makeCell(apBatchId, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(arBatchId, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getGlAcctNo(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getBillofLadding(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell((null != transactionBean.getInvoiceOrBl() ? transactionBean.getInvoiceOrBl().toUpperCase() : transactionBean.getInvoiceOrBl()), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getInvoiceNotes(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getVoyagenumber(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getChargeCode(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getRecordType(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getTransDate(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    if (null != transactionBean.getSailingDate()) {
		table.addCell(makeCell(simpleDateFormat.format(transactionBean.getSailingDate()), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    } else {
		table.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    }
	    if (null != transactionBean.getPostedDate()) {
		table.addCell(makeCell(simpleDateFormat.format(transactionBean.getPostedDate()), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    } else {
		table.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    }
	    if (null != transactionBean.getAmount() && transactionBean.getAmount().contains("-")) {
		String amount = "(" + StringUtils.replace(transactionBean.getAmount(), "-", "") + ")";
		table.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, redFontSize6, 0));
	    } else {
		table.addCell(makeCell(transactionBean.getAmount(), Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    }
	    String subTotal = "";
	    if (null != recieptsLedgerForm.getSortBy() && !recieptsLedgerForm.getSortBy().trim().equals("0")) {
		subTotal = this.calculateSubTotal(recieptsLedgerForm.getSortBy(), transactionBean, subLedgerList, i);
	    }
	    if (null != subTotal && subTotal.contains("-")) {
		subTotal = "(" + StringUtils.replace(subTotal, "-", "") + ")";
		table.addCell(makeCell(subTotal, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, redFontSize6, 0));
	    } else {
		table.addCell(makeCell(subTotal, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    }
	    i++;
	}
	document.add(table);
    }

    private void createACCSubLedgerReport(String subLedgerType, RecieptsLedgerForm recieptsLedgerForm, List<TransactionBean> subLedgerList) throws DocumentException, Exception {
	PdfPTable table = new PdfPTable(13);
	table.setWidthPercentage(100);
	table.setWidths(new float[]{12, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8});
	String reportName = "";
	SubledgerDAO subledgerDAO = new SubledgerDAO();
	List<Subledger> list = subledgerDAO.findByProperty("subLedgerCode", subLedgerType);
	for (Subledger subledger : list) {
	    reportName = null != subledger.getSubLedgerDesc() ? subledger.getSubLedgerDesc() : "";
	}
	PdfPCell titleCell = makeCell("SubLedger : " + reportName, Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, 0);
	titleCell.setColspan(6);
	table.addCell(titleCell);
	Date date = new Date();
	PdfPCell dateCell = makeCell("Date: " + simpleDateFormat.format(date), Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0);
	dateCell.setColspan(7);
	table.addCell(dateCell);
	FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
	FiscalPeriod fiscalPeriod = fiscalPeriodDAO.findById(Integer.parseInt(recieptsLedgerForm.getPeriod()));
	if (null != fiscalPeriod) {
	    PdfPCell periodCell = makeCell("Period:  " + fiscalPeriod.getPeriodDis(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, textFontForBatch, 0);
	    periodCell.setColspan(4);
	    table.addCell(periodCell);
	    PdfPCell startDateCell = makeCell("Start Date:  " + recieptsLedgerForm.getStartDate(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, textFontForBatch, 0);
	    startDateCell.setColspan(4);
	    table.addCell(startDateCell);

	    PdfPCell endDateCell = makeCell("End Date: " + recieptsLedgerForm.getEndDate(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, textFontForBatch, 0);
	    endDateCell.setColspan(5);
	    table.addCell(endDateCell);
	}
	//add headers
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_VENDOR_NAME, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_VENDOR_ACCOUNT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_GL_ACCOUNT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_BL_NUMBER, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_INVOICE_NUMBER, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_VOYAGE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_CHARGE_CODE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_RECORD_TYPE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_TRANSACTION_DATE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_REPORTING_DATE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_POSTED_DATE, Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_TRANSACTION_AMOUNT, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	table.addCell(makeCell(ExcelSheetConstants.SUBLEDGER_SUB_TOTAL, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0));
	int i = 0;
	total = 0d;
	Double grandTotal = 0d;
	Double grandSubTotal = 0d;
	for (TransactionBean transactionBean : subLedgerList) {
	    table.addCell(makeCell(StringUtils.abbreviate(transactionBean.getCustomer(), 15), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getCustomerNo(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getGlAcctNo(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getBillofLadding(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell((null != transactionBean.getInvoiceOrBl() ? transactionBean.getInvoiceOrBl().toUpperCase() : transactionBean.getInvoiceOrBl()), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getVoyagenumber(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getChargeCode(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getRecordType(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    table.addCell(makeCell(transactionBean.getTransDate(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    if (null != transactionBean.getSailingDate()) {
		table.addCell(makeCell(simpleDateFormat.format(transactionBean.getSailingDate()), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    } else {
		table.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    }
	    if (null != transactionBean.getPostedDate()) {
		table.addCell(makeCell(simpleDateFormat.format(transactionBean.getPostedDate()), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    } else {
		table.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    }
	    if (null != transactionBean.getAmount() && transactionBean.getAmount().contains("-")) {
		String amount = "(" + StringUtils.replace(transactionBean.getAmount(), "-", "") + ")";
		table.addCell(makeCell(amount, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, redFontSize6, 0));
	    } else {
		table.addCell(makeCell(transactionBean.getAmount(), Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    }
	    grandTotal += Double.parseDouble(transactionBean.getAmount().replaceAll(",", ""));
	    String subTotal = this.calculateSubTotal(CommonConstants.SORT_BY_GL_ACCOUNT, transactionBean, subLedgerList, i);
	    if (null != subTotal && !subTotal.trim().equals("")) {
		grandSubTotal += Double.parseDouble(subTotal.replaceAll(",", ""));
	    }
	    if (null != subTotal && subTotal.contains("-")) {
		subTotal = "(" + StringUtils.replace(subTotal, "-", "") + ")";
		table.addCell(makeCell(subTotal, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, redFontSize6, 0));
	    } else {
		table.addCell(makeCell(subTotal, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackFontSize6, 0));
	    }
	    i++;
	}
	PdfPCell grandTotalCell = makeCell("Grand Total: ", Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackBoldHeadingFont, 0);
	grandTotalCell.setColspan(10);
	table.addCell(grandTotalCell);
	if (grandTotal < 0d) {
	    table.addCell(makeCell("(" + number.format((-1) * grandTotal) + ")", Element.ALIGN_RIGHT, Element.ALIGN_LEFT, redFontSize6, 0));
	} else {
	    table.addCell(makeCell(number.format(grandTotal), Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackFontSize6, 0));
	}
	if (grandSubTotal < 0d) {
	    table.addCell(makeCell("(" + number.format((-1) * grandSubTotal) + ")", Element.ALIGN_RIGHT, Element.ALIGN_LEFT, redFontSize6, 0));
	} else {
	    table.addCell(makeCell(number.format(grandSubTotal), Element.ALIGN_RIGHT, Element.ALIGN_LEFT, blackFontSize6, 0));
	}
	document.add(table);
    }

    private String calculateSubTotal(String sortBy, TransactionBean transactionBean, List<TransactionBean> subLedgerList, int i) {
	String subTotal = "";
	if (sortBy.trim().equals(CommonConstants.SORT_BY_GL_ACCOUNT)) {
	    String glAcctNo = null != transactionBean.getGlAcctNo() ? transactionBean.getGlAcctNo() : "";
	    if (null != glAcctNo && !glAcctNo.trim().equals("")) {
		if ((i + 1) < subLedgerList.size()) {
		    TransactionBean tempTransactionBean = subLedgerList.get(i + 1);
		    String tempGlAcctNo = null != tempTransactionBean.getGlAcctNo() ? tempTransactionBean.getGlAcctNo().toString() : null;
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    if (!glAcctNo.equals(tempGlAcctNo)) {
			subTotal = number.format(total);
			total = 0d;
		    }
		} else {
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    subTotal = number.format(total);
		}
	    }
	} else if (sortBy.trim().equals(CommonConstants.SORT_BY_VENDOR)) {
	    String vendorNo = null != transactionBean.getCustomerNo() ? transactionBean.getCustomerNo() : "";
	    if (null != vendorNo && !vendorNo.trim().equals("")) {
		if ((i + 1) < subLedgerList.size()) {
		    TransactionBean tempTransactionBean = subLedgerList.get(i + 1);
		    String tempVendorNo = null != tempTransactionBean.getCustomerNo() ? tempTransactionBean.getCustomerNo().toString() : null;
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    if (!vendorNo.equals(tempVendorNo)) {
			subTotal = number.format(total);
			total = 0d;
		    }
		} else {
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    subTotal = number.format(total);
		}
	    }
	} else if (sortBy.trim().equals(CommonConstants.SORT_BY_CHARGECODE)) {
	    String chargeCode = null != transactionBean.getChargeCode() ? transactionBean.getChargeCode() : "";
	    if (null != chargeCode && !chargeCode.trim().equals("")) {
		if ((i + 1) < subLedgerList.size()) {
		    TransactionBean tempTransactionBean = subLedgerList.get(i + 1);
		    String tempChargeCode = null != tempTransactionBean.getChargeCode() ? tempTransactionBean.getChargeCode().toString() : null;
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    if (!chargeCode.equals(tempChargeCode)) {
			subTotal = number.format(total);
			total = 0d;
		    }
		} else {
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    subTotal = number.format(total);
		}
	    }
	} else if (sortBy.trim().equals(CommonConstants.SORT_BY_TRANSACTION_DATE)) {
	    String transactionDate = null != transactionBean.getTransDate() ? transactionBean.getTransDate() : "";
	    if (null != transactionDate && !transactionDate.trim().equals("")) {
		if ((i + 1) < subLedgerList.size()) {
		    TransactionBean tempTransactionBean = subLedgerList.get(i + 1);
		    String tempTransactionDate = null != tempTransactionBean.getTransDate() ? tempTransactionBean.getTransDate().toString() : null;
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    if (!tempTransactionDate.equals(tempTransactionDate)) {
			subTotal = number.format(total);
			total = 0d;
		    }
		} else {
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    subTotal = number.format(total);
		}
	    }
	} else if (sortBy.trim().equals(CommonConstants.SORT_BY_BILL_OF_LADDING)) {
	    String blNumber = null != transactionBean.getBillofLadding() ? transactionBean.getBillofLadding() : "";
	    if (null != blNumber && !blNumber.trim().equals("")) {
		if ((i + 1) < subLedgerList.size()) {
		    TransactionBean tempTransactionBean = subLedgerList.get(i + 1);
		    String tempBlNumber = null != tempTransactionBean.getBillofLadding() ? tempTransactionBean.getBillofLadding().toString() : null;
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    if (!tempBlNumber.equals(tempBlNumber)) {
			subTotal = number.format(total);
			total = 0d;
		    }
		} else {
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    subTotal = number.format(total);
		}
	    }
	}
	return subTotal;
    }

    private void destroy() {
	document.close();
    }

    public String generateReport(RecieptsLedgerForm recieptsLedgerForm, List<TransactionBean> subLedgerList, String realPath, String fileName)throws Exception {
	    this.initialize(fileName);
	    this.createBody(recieptsLedgerForm, subLedgerList, realPath);
	    this.destroy();
	return fileName;
    }
}
