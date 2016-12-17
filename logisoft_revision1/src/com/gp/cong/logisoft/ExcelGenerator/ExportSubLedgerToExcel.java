package com.gp.cong.logisoft.ExcelGenerator;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.accounting.GLMappingConstant;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.Subledger;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.SubledgerDAO;
import com.gp.cvst.logisoft.struts.form.RecieptsLedgerForm;
import com.lowagie.text.DocumentException;

import org.apache.log4j.Logger;

public class ExportSubLedgerToExcel extends BaseExcelGenerator {

    private static final Logger log = Logger.getLogger(ExportSubLedgerToExcel.class);
    @SuppressWarnings("unchecked")
    private Double total = 0d;
    private Double checkNoTotal = 0d;

    private boolean generateExcelSheet(RecieptsLedgerForm recieptsLedgerForm, List<TransactionBean> subLedgerList) throws Exception {
	String subLedgerType = recieptsLedgerForm.getSubLedgerType();
	String sheetName = "All SubLedgers";
	if (null != subLedgerType && !subLedgerType.trim().equals("ALL")) {
	    SubledgerDAO subledgerDAO = new SubledgerDAO();
	    List<Subledger> list = subledgerDAO.findByProperty("subLedgerCode", subLedgerType);
	    for (Subledger subledger : list) {
		sheetName = null != subledger.getSubLedgerDesc() ? subledger.getSubLedgerDesc() : "SubLedger";
	    }
	}
	writableSheet = writableWorkbook.createSheet(sheetName, 0);
	if (null != writableSheet) {
	    try {
		if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.SUB_LEDGER_CODE_PURCHASE_JOURNAL)) {
		    this.generatePJSubledgerWorkSheet(sheetName, recieptsLedgerForm, subLedgerList);
		} else if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.SUB_LEDGER_CODE_ACCRUALS)) {
		    this.generateACCSubledgerWorkSheet(sheetName, recieptsLedgerForm, subLedgerList);
		} else {
		    if (null != recieptsLedgerForm.getRevOrExp() && recieptsLedgerForm.getRevOrExp().trim().equals(GLMappingConstant.BOTH)
			    && null != recieptsLedgerForm.getSortBy() && recieptsLedgerForm.getSortBy().trim().equals(CommonConstants.SORT_BY_BILL_OF_LADDING)) {
			this.generateForBothRevExpAndBillOfLadding(sheetName, recieptsLedgerForm, subLedgerList);
		    } else {
			int row = 0;
			writableSheet.mergeCells(0, row, 8, row);
			if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.ALL)) {
			    writableSheet.mergeCells(9, row, 18, row);
			} else {
			    writableSheet.mergeCells(9, row, 17, row);
			}
			writableSheet.addCell(new Label(0, row, ExcelSheetConstants.SUBLEDGER_HEADER, headerCell));
			writableSheet.addCell(new Label(9, row, ExcelSheetConstants.DATE + ":" + sdf.format(new Date()), headerCell));

			row++;
			writableSheet.mergeCells(0, row, 8, row);
			if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.ALL)) {
			    writableSheet.mergeCells(9, row, 18, row);
			} else {
			    writableSheet.mergeCells(9, row, 17, row);
			}
			String subLedgerPeriod = "";
			if (CommonUtils.isNotEmpty(recieptsLedgerForm.getPeriod())) {
			    FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
			    FiscalPeriod fiscalPeriod = fiscalPeriodDAO.findById(Integer.parseInt(recieptsLedgerForm.getPeriod()));
			    if (null != fiscalPeriod) {
				subLedgerPeriod = fiscalPeriod.getPeriodDis();
			    }
			}
			writableSheet.addCell(new Label(0, row, ExcelSheetConstants.SUBLEDGER_TYPE + ": " + sheetName, headerCell));
			writableSheet.addCell(new Label(9, row, ExcelSheetConstants.SUBLEDGER_PERIOD + ": " + subLedgerPeriod, headerCell));

			/*Table Column Header*/
			row++;
			int col = 0;
			writableSheet.setColumnView(0, 20);
			writableSheet.setColumnView(1, 20);
			writableSheet.setColumnView(2, 20);
			writableSheet.setColumnView(3, 20);
			writableSheet.setColumnView(4, 20);
			writableSheet.setColumnView(5, 20);
			writableSheet.setColumnView(6, 20);
			writableSheet.setColumnView(7, 20);
			writableSheet.setColumnView(8, 20);
			writableSheet.setColumnView(9, 20);
			writableSheet.setColumnView(10, 20);
			writableSheet.setColumnView(11, 20);
			writableSheet.setColumnView(12, 20);
			writableSheet.setColumnView(13, 20);
			writableSheet.setColumnView(14, 20);
			writableSheet.setColumnView(15, 20);
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_VENDOR_NAME, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_VENDOR_ACCOUNT, headerCell));
			if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.ALL)) {
			    writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_TYPE, headerCell));
			    writableSheet.setColumnView(15, 20);
			}
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_AP_BATCH_ID, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_AR_BATCH_ID, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_GL_ACCOUNT, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_BL_NUMBER, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_INVOICE_NUMBER, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_CHARGE_CODE, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_VOYAGE, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_TRANSACTION_DATE, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_REPORTING_DATE, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_POSTED_DATE, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_DEBIT, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_CREDIT, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_RECORD_TYPE, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_LINE_ITEM_NUMBER, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_JOURNAL_ENTRY_NUMBER, headerCell));
			writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_SUB_TOTAL, headerCell));

			/*Table Data*/
			row++;
			if (null != subLedgerList && !subLedgerList.isEmpty()) {
			    int i = 0;
			    total = 0d;
			    for (TransactionBean transactionBean : subLedgerList) {
				col = 0;
				writableSheet.addCell(new Label(col++, row, StringUtils.abbreviate(transactionBean.getCustomer(), 20), thinBorderCell));
				writableSheet.addCell(new Label(col++, row, transactionBean.getCustomerNo(), thinBorderCell));
				if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.ALL)) {
				    writableSheet.addCell(new Label(col++, row, transactionBean.getSubLedgerCode(), thinBorderCell));
				}
				String apBatchId = null != transactionBean.getApBatchId() ? transactionBean.getApBatchId().toString() : "";
				String arBatchId = null != transactionBean.getArBatchId() ? transactionBean.getArBatchId().toString() : "";
				writableSheet.addCell(new Label(col++, row, apBatchId, thinBorderCell));
				writableSheet.addCell(new Label(col++, row, arBatchId, thinBorderCell));
				writableSheet.addCell(new Label(col++, row, transactionBean.getGlAcctNo(), thinBorderCell));
				writableSheet.addCell(new Label(col++, row, transactionBean.getBillofLadding(), thinBorderCell));
				writableSheet.addCell(new Label(col++, row, transactionBean.getInvoiceOrBl(), thinBorderCell));
				writableSheet.addCell(new Label(col++, row, transactionBean.getChargeCode(), thinBorderCell));
				writableSheet.addCell(new Label(col++, row, transactionBean.getVoyagenumber(), thinBorderCell));
				writableSheet.addCell(new Label(col++, row, transactionBean.getTransDate(), thinBorderCell));
				String reportingDate = "";
				if (null != transactionBean.getSailingDate()) {
				    reportingDate = DateUtils.parseDateToString(transactionBean.getSailingDate());
				}
				writableSheet.addCell(new Label(col++, row, reportingDate, thinBorderCell));
				String postedDate = "";
				if (null != transactionBean.getPostedDate()) {
				    postedDate = DateUtils.parseDateToString(transactionBean.getPostedDate());
				}
				writableSheet.addCell(new Label(col++, row, postedDate, thinBorderCell));
				writableSheet.addCell(new Label(col++, row, transactionBean.getDebit(), thinBorderCell));
				writableSheet.addCell(new Label(col++, row, transactionBean.getCredit(), thinBorderCell));
				writableSheet.addCell(new Label(col++, row, transactionBean.getRecordType(), thinBorderCell));
				writableSheet.addCell(new Label(col++, row, transactionBean.getLineitemNo(), thinBorderCell));
				writableSheet.addCell(new Label(col++, row, transactionBean.getJournalentryNo(), thinBorderCell));
				String subTotal = "";
				if (null != recieptsLedgerForm.getSortBy() && !recieptsLedgerForm.getSortBy().trim().equals("0")) {
				    subTotal = this.calculateSubTotal(recieptsLedgerForm.getSortBy(), transactionBean, subLedgerList, i);
				}
				if (null != subTotal && subTotal.contains("-")) {
				    subTotal = "(" + StringUtils.replace(subTotal, "-", "") + ")";
				    writableSheet.addCell(new Label(col++, row, subTotal, thinBorderCellAlignRightWithRedFont));
				} else {
				    writableSheet.addCell(new Label(col++, row, subTotal, thinBorderCellAlignRight));
				}
				i++;
				row++;
			    }
			}
		    }
		}
		return true;
	    } catch (Exception e) {
		log.info("generateExcelSheet failed on " + new Date(),e);
		return false;
	    }
	} else {
	    return true;
	}
    }

    private void generateForBothRevExpAndBillOfLadding(String sheetName, RecieptsLedgerForm recieptsLedgerForm, List<TransactionBean> subLedgerList) throws DocumentException, RowsExceededException, WriteException, Exception {
	int row = 0;
	writableSheet.mergeCells(0, row, 6, row);
	writableSheet.mergeCells(7, row, 11, row);
	writableSheet.addCell(new Label(0, row, ExcelSheetConstants.SUBLEDGER_HEADER, headerCell));
	writableSheet.addCell(new Label(7, row, ExcelSheetConstants.DATE + ":" + sdf.format(new Date()), headerCell));

	row++;
	writableSheet.mergeCells(0, row, 6, row);
	writableSheet.mergeCells(7, row, 11, row);
	String subLedgerPeriod = "";
	FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
	FiscalPeriod fiscalPeriod = fiscalPeriodDAO.findById(Integer.parseInt(recieptsLedgerForm.getPeriod()));
	if (null != fiscalPeriod) {
	    subLedgerPeriod = fiscalPeriod.getPeriodDis();
	}
	writableSheet.addCell(new Label(0, row, ExcelSheetConstants.SUBLEDGER_TYPE + ": " + sheetName, headerCell));
	writableSheet.addCell(new Label(7, row, ExcelSheetConstants.SUBLEDGER_PERIOD + ": " + subLedgerPeriod, headerCell));

	/*Table Column Header*/
	row++;
	int col = 0;
	writableSheet.setColumnView(0, 30);
	writableSheet.setColumnView(1, 20);
	writableSheet.setColumnView(2, 20);
	writableSheet.setColumnView(3, 20);
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_BL_NUMBER, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_REVENUE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_EXPENSE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_DIFFERENCE, headerCell));

	/*Table Data*/
	row++;
	if (null != subLedgerList && !subLedgerList.isEmpty()) {
	    for (TransactionBean transactionBean : subLedgerList) {
		col = 0;
		writableSheet.addCell(new Label(col++, row, transactionBean.getBillofLadding(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getInvoiceOrBl(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getVoyagenumber(), thinBorderCell));
		writableSheet.addCell(new Number(col++, row, null != transactionBean.getAmount() ? Double.parseDouble(transactionBean.getAmount().replaceAll(",", "")) : 0d, thinBorderCell));
		row++;
	    }
	}
    }

    private void generatePJSubledgerWorkSheet(String sheetName, RecieptsLedgerForm recieptsLedgerForm, List<TransactionBean> subLedgerList) throws DocumentException, RowsExceededException, WriteException, Exception {
	int row = 0;
	writableSheet.mergeCells(0, row, 3, row);
	writableSheet.mergeCells(4, row, 15, row);
	writableSheet.addCell(new Label(0, row, ExcelSheetConstants.SUBLEDGER_TYPE + ": " + sheetName, headerCell));
	writableSheet.addCell(new Label(4, row, ExcelSheetConstants.DATE + ":" + sdf.format(new Date()), headerCell));

	row++;
	writableSheet.mergeCells(0, row, 2, row);
	writableSheet.mergeCells(3, row, 6, row);
	writableSheet.mergeCells(7, row, 15, row);
	String subLedgerPeriod = "";
	FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
	FiscalPeriod fiscalPeriod = fiscalPeriodDAO.findById(Integer.parseInt(recieptsLedgerForm.getPeriod()));
	if (null != fiscalPeriod) {
	    subLedgerPeriod = fiscalPeriod.getPeriodDis();
	}
	writableSheet.addCell(new Label(0, row, ExcelSheetConstants.SUBLEDGER_PERIOD + ": " + subLedgerPeriod, headerCell));
	writableSheet.addCell(new Label(3, row, ExcelSheetConstants.SUBLEDGER_START_DATE + ": " + recieptsLedgerForm.getStartDate(), headerCell));
	writableSheet.addCell(new Label(7, row, ExcelSheetConstants.SUBLEDGER_END_DATE + ": " + recieptsLedgerForm.getEndDate(), headerCell));

	/*Table Column Header*/
	row++;
	int col = 0;
	writableSheet.setColumnView(0, 30);
	writableSheet.setColumnView(1, 20);
	writableSheet.setColumnView(2, 20);
	writableSheet.setColumnView(3, 20);
	writableSheet.setColumnView(4, 20);
	writableSheet.setColumnView(5, 20);
	writableSheet.setColumnView(6, 20);
	writableSheet.setColumnView(7, 20);
	writableSheet.setColumnView(8, 20);
	writableSheet.setColumnView(9, 20);
	writableSheet.setColumnView(10, 20);
	writableSheet.setColumnView(11, 20);
	writableSheet.setColumnView(12, 20);
	writableSheet.setColumnView(13, 20);
	writableSheet.setColumnView(14, 20);
	writableSheet.setColumnView(15, 20);
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_VENDOR_NAME, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_VENDOR_ACCOUNT, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_AP_BATCH_ID, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_AR_BATCH_ID, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_GL_ACCOUNT, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_BL_NUMBER, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_INVOICE_NUMBER, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_INVOICE_FOR, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_VOYAGE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_CHARGE_CODE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_RECORD_TYPE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_TRANSACTION_DATE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_REPORTING_DATE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_POSTED_DATE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_TRANSACTION_AMOUNT, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_SUB_TOTAL, headerCell));

	/*Table Data*/
	row++;
	if (null != subLedgerList && !subLedgerList.isEmpty()) {
	    int i = 0;
	    for (TransactionBean transactionBean : subLedgerList) {
		col = 0;
		writableSheet.addCell(new Label(col++, row, StringUtils.abbreviate(transactionBean.getCustomer(), 20), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getCustomerNo(), thinBorderCell));
		String apBatchId = null != transactionBean.getApBatchId() ? transactionBean.getApBatchId().toString() : "";
		String arBatchId = null != transactionBean.getArBatchId() ? transactionBean.getArBatchId().toString() : "";
		writableSheet.addCell(new Label(col++, row, apBatchId, thinBorderCell));
		writableSheet.addCell(new Label(col++, row, arBatchId, thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getGlAcctNo(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getBillofLadding(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getInvoiceOrBl(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getInvoiceNotes(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getVoyagenumber(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getChargeCode(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getRecordType(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getTransDate(), thinBorderCell));
		String sailingDate = "";
		if (null != transactionBean.getSailingDate()) {
		    sailingDate = DateUtils.parseDateToString(transactionBean.getSailingDate());
		}
		writableSheet.addCell(new Label(col++, row, sailingDate, thinBorderCell));
		String postedDate = "";
		if (null != transactionBean.getPostedDate()) {
		    postedDate = DateUtils.parseDateToString(transactionBean.getPostedDate());
		}
		writableSheet.addCell(new Label(col++, row, postedDate, thinBorderCell));
		writableSheet.addCell(new Number(col++, row, null != transactionBean.getAmount()
			? Double.parseDouble(transactionBean.getAmount().replaceAll(",", "")) : 0d, numberCellFormat));
		String subTotal = "";
		if (null != recieptsLedgerForm.getSortBy() && !recieptsLedgerForm.getSortBy().trim().equals("0")) {
		    subTotal = this.calculateSubTotal(recieptsLedgerForm.getSortBy(), transactionBean, subLedgerList, i);
		}
		if (CommonUtils.isNotEmpty(subTotal)) {
		    writableSheet.addCell(new Number(col++, row, Double.parseDouble(subTotal.replaceAll(",", "")), numberCellFormat));
		} else {
		    writableSheet.addCell(new Label(col++, row, "", thinBorderCell));
		}
		row++;
		i++;
	    }
	}
    }

    private void generateACCSubledgerWorkSheet(String sheetName, RecieptsLedgerForm recieptsLedgerForm, List<TransactionBean> subLedgerList) throws DocumentException, RowsExceededException, WriteException, Exception {
	int row = 0;
	writableSheet.mergeCells(0, row, 3, row);
	writableSheet.mergeCells(4, row, 12, row);
	writableSheet.addCell(new Label(0, row, ExcelSheetConstants.SUBLEDGER_TYPE + ": " + sheetName, headerCell));
	writableSheet.addCell(new Label(4, row, ExcelSheetConstants.DATE + ":" + sdf.format(new Date()), headerCell));

	row++;
	writableSheet.mergeCells(0, row, 2, row);
	writableSheet.mergeCells(3, row, 6, row);
	writableSheet.mergeCells(7, row, 12, row);
	String subLedgerPeriod = "";
	if (CommonUtils.isNotEmpty(recieptsLedgerForm.getPeriod())) {
	    FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
	    FiscalPeriod fiscalPeriod = fiscalPeriodDAO.findById(Integer.parseInt(recieptsLedgerForm.getPeriod()));
	    if (null != fiscalPeriod) {
		subLedgerPeriod = fiscalPeriod.getPeriodDis();
	    }
	}
	writableSheet.addCell(new Label(0, row, ExcelSheetConstants.SUBLEDGER_PERIOD + ": " + subLedgerPeriod, headerCell));
	writableSheet.addCell(new Label(3, row, ExcelSheetConstants.SUBLEDGER_START_DATE + ": " + recieptsLedgerForm.getStartDate(), headerCell));
	writableSheet.addCell(new Label(7, row, ExcelSheetConstants.SUBLEDGER_END_DATE + ": " + recieptsLedgerForm.getEndDate(), headerCell));

	/*Table Column Header*/
	row++;
	int col = 0;
	writableSheet.setColumnView(0, 30);
	writableSheet.setColumnView(1, 20);
	writableSheet.setColumnView(2, 20);
	writableSheet.setColumnView(3, 20);
	writableSheet.setColumnView(4, 20);
	writableSheet.setColumnView(5, 20);
	writableSheet.setColumnView(6, 20);
	writableSheet.setColumnView(7, 20);
	writableSheet.setColumnView(8, 20);
	writableSheet.setColumnView(9, 20);
	writableSheet.setColumnView(10, 20);
	writableSheet.setColumnView(11, 20);
	writableSheet.setColumnView(12, 20);
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_VENDOR_NAME, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_VENDOR_ACCOUNT, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_GL_ACCOUNT, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_BL_NUMBER, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_INVOICE_NUMBER, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_VOYAGE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_CHARGE_CODE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_RECORD_TYPE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_TRANSACTION_DATE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_REPORTING_DATE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_POSTED_DATE, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_TRANSACTION_AMOUNT, headerCell));
	writableSheet.addCell(new Label(col++, row, ExcelSheetConstants.SUBLEDGER_SUB_TOTAL, headerCell));

	/*Table Data*/
	row++;
	if (null != subLedgerList && !subLedgerList.isEmpty()) {
	    Double grandTotal = 0d;
	    Double grandSubTotal = 0d;
	    int i = 0;
	    for (TransactionBean transactionBean : subLedgerList) {
		col = 0;
		writableSheet.addCell(new Label(col++, row, StringUtils.abbreviate(transactionBean.getCustomer(), 20), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getCustomerNo(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getGlAcctNo(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getBillofLadding(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getInvoiceOrBl(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getVoyagenumber(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getChargeCode(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getRecordType(), thinBorderCell));
		writableSheet.addCell(new Label(col++, row, transactionBean.getTransDate(), thinBorderCell));
		String sailingDate = "";
		if (null != transactionBean.getSailingDate()) {
		    sailingDate = DateUtils.parseDateToString(transactionBean.getSailingDate());
		}
		writableSheet.addCell(new Label(col++, row, sailingDate, thinBorderCell));
		String postedDate = "";
		if (null != transactionBean.getPostedDate()) {
		    postedDate = DateUtils.parseDateToString(transactionBean.getPostedDate());
		}
		writableSheet.addCell(new Label(col++, row, postedDate, thinBorderCell));
		writableSheet.addCell(new Number(col++, row, null != transactionBean.getAmount() ? Double.parseDouble(transactionBean.getAmount().replaceAll(",", "")) : 0d, numberCellFormat));
		grandTotal += Double.parseDouble(transactionBean.getAmount().replaceAll(",", ""));
		String subTotal = this.calculateSubTotal(CommonConstants.SORT_BY_GL_ACCOUNT, transactionBean, subLedgerList, i);
		if (null != subTotal && !subTotal.trim().equals("")) {
		    grandSubTotal += Double.parseDouble(subTotal.replaceAll(",", ""));
		}
		if (CommonUtils.isNotEmpty(subTotal)) {
		    writableSheet.addCell(new Number(col++, row, Double.parseDouble(subTotal.replaceAll(",", "")), numberCellFormat));
		} else {
		    writableSheet.addCell(new Label(col++, row, "", thinBorderCell));
		}
		row++;
		i++;
	    }
	    writableSheet.mergeCells(0, row, 10, row);
	    writableSheet.addCell(new Label(0, row, "Grand Total :", thinBorderCellAlignRight));
	    writableSheet.addCell(new Number(11, row, grandTotal, numberCellFormat));
	    writableSheet.addCell(new Number(12, row, grandSubTotal, numberCellFormat));
	}
    }

    private String calculateSubTotal(String sortBy, TransactionBean transactionBean, List<TransactionBean> subLedgerList, int i) {
	String subTotal = "";
	if (sortBy.trim().equals(CommonConstants.SORT_BY_GL_ACCOUNT)) {
	    String glAcctNo = null != transactionBean.getGlAcctNo() ? transactionBean.getGlAcctNo() : "";
	    if (null != glAcctNo && !glAcctNo.trim().equals("")) {
		if ((i + 1) < subLedgerList.size()) {
		    TransactionBean tempTransactionBean = (TransactionBean) subLedgerList.get(i + 1);
		    String tempGlAcctNo = null != tempTransactionBean.getGlAcctNo() ? tempTransactionBean.getGlAcctNo().toString() : null;
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    if (!glAcctNo.equals(tempGlAcctNo)) {
			subTotal = amountFormat.format(total);
			total = 0d;
		    }
		} else {
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    subTotal = amountFormat.format(total);
		}
	    }
	} else if (sortBy.trim().equals(CommonConstants.SORT_BY_VENDOR)) {
	    String vendorNo = null != transactionBean.getCustomerNo() ? transactionBean.getCustomerNo() : "";
	    if (null != vendorNo && !vendorNo.trim().equals("")) {
		if ((i + 1) < subLedgerList.size()) {
		    TransactionBean tempTransactionBean = (TransactionBean) subLedgerList.get(i + 1);
		    String tempVendorNo = null != tempTransactionBean.getCustomerNo() ? tempTransactionBean.getCustomerNo().toString() : null;
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    if (!vendorNo.equals(tempVendorNo)) {
			subTotal = amountFormat.format(total);
			total = 0d;
		    }
		} else {
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    subTotal = amountFormat.format(total);
		}
	    }
	} else if (sortBy.trim().equals(CommonConstants.SORT_BY_CHARGECODE)) {
	    String chargeCode = null != transactionBean.getChargeCode() ? transactionBean.getChargeCode() : "";
	    if (null != chargeCode && !chargeCode.trim().equals("")) {
		if ((i + 1) < subLedgerList.size()) {
		    TransactionBean tempTransactionBean = (TransactionBean) subLedgerList.get(i + 1);
		    String tempChargeCode = null != tempTransactionBean.getChargeCode() ? tempTransactionBean.getChargeCode().toString() : null;
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    if (!chargeCode.equals(tempChargeCode)) {
			subTotal = amountFormat.format(total);
			total = 0d;
		    }
		} else {
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    subTotal = amountFormat.format(total);
		}
	    }
	} else if (sortBy.trim().equals(CommonConstants.SORT_BY_TRANSACTION_DATE)) {
	    String transactionDate = null != transactionBean.getTransDate() ? transactionBean.getTransDate() : "";
	    if (null != transactionDate && !transactionDate.trim().equals("")) {
		if ((i + 1) < subLedgerList.size()) {
		    TransactionBean tempTransactionBean = (TransactionBean) subLedgerList.get(i + 1);
		    String tempTransactionDate = null != tempTransactionBean.getTransDate() ? tempTransactionBean.getTransDate().toString() : null;
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    if (!tempTransactionDate.equals(tempTransactionDate)) {
			subTotal = amountFormat.format(total);
			total = 0d;
		    }
		} else {
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    subTotal = amountFormat.format(total);
		}
	    }
	} else if (sortBy.trim().equals(CommonConstants.SORT_BY_BILL_OF_LADDING)) {
	    String blNumber = null != transactionBean.getBillofLadding() ? transactionBean.getBillofLadding() : "";
	    if (null != blNumber && !blNumber.trim().equals("")) {
		if ((i + 1) < subLedgerList.size()) {
		    TransactionBean tempTransactionBean = (TransactionBean) subLedgerList.get(i + 1);
		    String tempBlNumber = null != tempTransactionBean.getBillofLadding() ? tempTransactionBean.getBillofLadding().toString() : null;
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    if (!tempBlNumber.equals(tempBlNumber)) {
			subTotal = amountFormat.format(total);
			total = 0d;
		    }
		} else {
		    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
			total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
		    }
		    subTotal = amountFormat.format(total);
		}
	    }
	}
	return subTotal;
    }

    public String exportToExcel(String excelFilePath, RecieptsLedgerForm recieptsLedgerForm, List<TransactionBean> subLedgerList) throws Exception {
	String excelFileName = null;
	if (null != excelFilePath && null != subLedgerList && !subLedgerList.isEmpty()) {
	    super.init(excelFilePath);
	    if (null != super.writableWorkbook) {
		if (this.generateExcelSheet(recieptsLedgerForm, subLedgerList)) {
		    try {
			super.write();
			super.close();
			excelFileName = excelFilePath;
		    } catch (Exception e) {
			excelFileName = null;
			log.info("exportToExcel failed on " + new Date(),e);
		    }
		}
	    }
	}
	return excelFileName;
    }
}
