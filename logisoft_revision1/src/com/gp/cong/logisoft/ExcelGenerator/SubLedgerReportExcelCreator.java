/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.ExcelGenerator;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.accounting.GLMappingConstant;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.Subledger;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.SubledgerDAO;
import com.gp.cvst.logisoft.struts.form.RecieptsLedgerForm;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author logiware
 */
public class SubLedgerReportExcelCreator extends BaseExcelCreator implements ConstantsInterface {

    private RecieptsLedgerForm recieptsLedgerForm;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private Double total = 0d;

    public SubLedgerReportExcelCreator() {
    }

    public SubLedgerReportExcelCreator(RecieptsLedgerForm recieptsLedgerForm) {
        this.recieptsLedgerForm = recieptsLedgerForm;
    }

    private String getSheetName(String subLedgerType) throws Exception {
        String sheetName = "All SubLedgers";
        if (null != subLedgerType && !subLedgerType.trim().equals("ALL")) {
            List<Subledger> list = new SubledgerDAO().findByProperty("subLedgerCode", subLedgerType);
            for (Subledger subledger : list) {
                sheetName = null != subledger.getSubLedgerDesc() ? subledger.getSubLedgerDesc() : "SubLedger";
            }
        }
        return sheetName;
    }

    private String getSubLedgerPeriod(String period) throws Exception {
        String subLedgerPeriod = "";
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        if (CommonUtils.isNotEmpty(period)) {
        }
        FiscalPeriod fiscalPeriod = fiscalPeriodDAO.findById(Integer.parseInt(period));
        return null != fiscalPeriod ? fiscalPeriod.getPeriodDis() : "";
    }

    private void writePJSubLedgerHeader() throws Exception {
        createRow();
        createHeaderCell("SubLedger", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 15);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("SubLedger Type : ", subHeaderOneCellStyleLeftBold);
        String subLedgerType = getSheetName(recieptsLedgerForm.getSubLedgerType());
        createHeaderCell(subLedgerType, subHeaderOneCellStyleLeftNormal);
        createHeaderCell("Date : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(sdf.format(new Date()), subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 3, 15);
        createRow();
        resetColumnIndex();
        createHeaderCell("Period : ", subHeaderTwoCellStyleLeftBold);
        String subLedgerPeriod = getSubLedgerPeriod(recieptsLedgerForm.getPeriod());
        createHeaderCell(subLedgerPeriod, subHeaderTwoCellStyleLeftNormal);
        createHeaderCell("Start Date : ", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(recieptsLedgerForm.getStartDate(), subHeaderTwoCellStyleLeftNormal);
        createHeaderCell("End Date : ", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(recieptsLedgerForm.getEndDate(), subHeaderTwoCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 5, 15);
        createRow();
        resetColumnIndex();
        createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold);
        createHeaderCell("Vendor Account", tableHeaderCellStyleCenterBold);
        createHeaderCell("AP Batch Id", tableHeaderCellStyleCenterBold);
        createHeaderCell("AR Batch Id", tableHeaderCellStyleCenterBold);
        createHeaderCell("GL Account", tableHeaderCellStyleCenterBold);
        createHeaderCell("BL Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Invoice Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Invoice For", tableHeaderCellStyleCenterBold);
        createHeaderCell("Voyage", tableHeaderCellStyleCenterBold);
        createHeaderCell("Charge Code", tableHeaderCellStyleCenterBold);
        createHeaderCell("Record Type", tableHeaderCellStyleCenterBold);
        createHeaderCell("Transaction Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Reporting Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Posted Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Transaction Amount", tableHeaderCellStyleCenterBold);
        createHeaderCell("Sub Total", tableHeaderCellStyleCenterBold);
    }

    private void writeACCSubLedgerHeader() throws Exception {
        createRow();
        createHeaderCell("SubLedger", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 12);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("SubLedger Type : ", subHeaderOneCellStyleLeftBold);
        String subLedgerType = getSheetName(recieptsLedgerForm.getSubLedgerType());
        createHeaderCell(subLedgerType, subHeaderOneCellStyleLeftNormal);
        createHeaderCell("Date : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(sdf.format(new Date()), subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 3, 12);
        createRow();
        resetColumnIndex();
        createHeaderCell("Period : ", subHeaderTwoCellStyleLeftBold);
        String subLedgerPeriod = getSubLedgerPeriod(recieptsLedgerForm.getPeriod());
        createHeaderCell(subLedgerPeriod, subHeaderTwoCellStyleLeftNormal);
        createHeaderCell("Start Date : ", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(recieptsLedgerForm.getStartDate(), subHeaderTwoCellStyleLeftNormal);
        createHeaderCell("End Date : ", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(recieptsLedgerForm.getEndDate(), subHeaderTwoCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 5, 12);
        createRow();
        resetColumnIndex();
        createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold);
        createHeaderCell("Vendor Account", tableHeaderCellStyleCenterBold);
        createHeaderCell("GL Account", tableHeaderCellStyleCenterBold);
        createHeaderCell("BL Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Invoice Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Voyage", tableHeaderCellStyleCenterBold);
        createHeaderCell("Charge Code", tableHeaderCellStyleCenterBold);
        createHeaderCell("Record Type", tableHeaderCellStyleCenterBold);
        createHeaderCell("Transaction Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Reporting Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Posted Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Transaction Amount", tableHeaderCellStyleCenterBold);
        createHeaderCell("Sub Total", tableHeaderCellStyleCenterBold);
    }

    private void writeBothRevExpAndBillOfLaddingHeader() throws Exception {
        createRow();
        createHeaderCell("SubLedger", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 3);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("List Of SubLedger : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell("Date : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(sdf.format(new Date()), subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 2, 3);
        createRow();
        resetColumnIndex();
        createHeaderCell("SubLedger Type : ", subHeaderTwoCellStyleLeftBold);
        String subLedgerType = getSheetName(recieptsLedgerForm.getSubLedgerType());
        createHeaderCell(subLedgerType, subHeaderTwoCellStyleLeftNormal);
        createHeaderCell("Period : ", subHeaderTwoCellStyleLeftBold);
        String subLedgerPeriod = getSubLedgerPeriod(recieptsLedgerForm.getPeriod());
        createHeaderCell(subLedgerPeriod, subHeaderTwoCellStyleLeftNormal);
        createRow();
        resetColumnIndex();
        createHeaderCell("BL Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Revenue", tableHeaderCellStyleCenterBold);
        createHeaderCell("Expense", tableHeaderCellStyleCenterBold);
        createHeaderCell("Difference", tableHeaderCellStyleCenterBold);
    }

    private void writeSubLedgerHeader() throws Exception {
        String subLedgerType = recieptsLedgerForm.getSubLedgerType();
        String sheetName = "";
        createRow();
        createHeaderCell("SubLedger", headerCellStyleCenterBold);
        if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.ALL)) {
            mergeCells(rowIndex, rowIndex, 0, 18);
        } else {
            mergeCells(rowIndex, rowIndex, 0, 17);
        }
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("List Of SubLedger : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell("Date : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(sdf.format(new Date()), subHeaderOneCellStyleLeftNormal);
        if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.ALL)) {
            mergeCells(rowIndex, rowIndex, 2, 18);
        } else {
            mergeCells(rowIndex, rowIndex, 2, 17);
        }
        createRow();
        resetColumnIndex();
        createHeaderCell("SubLedger Type : ", subHeaderTwoCellStyleLeftBold);
        sheetName = getSheetName(subLedgerType);
        createHeaderCell(sheetName, subHeaderTwoCellStyleLeftNormal);
        createHeaderCell("Period : ", subHeaderTwoCellStyleLeftBold);
        String subLedgerPeriod = getSubLedgerPeriod(recieptsLedgerForm.getPeriod());
        createHeaderCell(subLedgerPeriod, subHeaderTwoCellStyleLeftNormal);
        if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.ALL)) {
            mergeCells(rowIndex, rowIndex, 3, 18);
        } else {
            mergeCells(rowIndex, rowIndex, 3, 17);
        }
        createRow();
        resetColumnIndex();
        createHeaderCell("Vendor Name", tableHeaderCellStyleCenterBold);
        createHeaderCell("Vendor Account", tableHeaderCellStyleCenterBold);
        if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.ALL)) {
            createHeaderCell("SubLedger Type", tableHeaderCellStyleCenterBold);
        }
        createHeaderCell("AP Batch Id", tableHeaderCellStyleCenterBold);
        createHeaderCell("AR Batch Id", tableHeaderCellStyleCenterBold);
        createHeaderCell("GL Account", tableHeaderCellStyleCenterBold);
        createHeaderCell("BL Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Invoice Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Charge Code", tableHeaderCellStyleCenterBold);
        createHeaderCell("Voyage", tableHeaderCellStyleCenterBold);
        createHeaderCell("Transaction Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Reporting Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Posted Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Debit", tableHeaderCellStyleCenterBold);
        createHeaderCell("Credit", tableHeaderCellStyleCenterBold);
        createHeaderCell("Record Type", tableHeaderCellStyleCenterBold);
        createHeaderCell("Line Item Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Journal Entry Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Sub Total", tableHeaderCellStyleCenterBold);
    }

    private void writeHeader() throws Exception {
        String subLedgerType = recieptsLedgerForm.getSubLedgerType();
        if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.SUB_LEDGER_CODE_PURCHASE_JOURNAL)) {
            writePJSubLedgerHeader();
        } else if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.SUB_LEDGER_CODE_ACCRUALS)) {
            writeACCSubLedgerHeader();
        } else {
            if (null != recieptsLedgerForm.getRevOrExp() && recieptsLedgerForm.getRevOrExp().trim().equals(GLMappingConstant.BOTH)
                    && null != recieptsLedgerForm.getSortBy() && recieptsLedgerForm.getSortBy().trim().equals(CommonConstants.SORT_BY_BILL_OF_LADDING)) {
                writeBothRevExpAndBillOfLaddingHeader();
            } else {
                writeSubLedgerHeader();
            }
        }
    }

    private String calculateSubTotal(String sortBy, TransactionBean transactionBean, List<TransactionBean> subLedgerList, int i) throws Exception {
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
                        subTotal = NumberUtils.formatNumber(total, "###,###,##0.00");
                        total = 0d;
                    }
                } else {
                    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
                        total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
                    }
                    subTotal = NumberUtils.formatNumber(total, "###,###,##0.00");
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
                        subTotal = NumberUtils.formatNumber(total, "###,###,##0.00");
                        total = 0d;
                    }
                } else {
                    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
                        total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
                    }
                    subTotal = NumberUtils.formatNumber(total, "###,###,##0.00");
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
                        subTotal = NumberUtils.formatNumber(total, "###,###,##0.00");
                        total = 0d;
                    }
                } else {
                    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
                        total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
                    }
                    subTotal = NumberUtils.formatNumber(total, "###,###,##0.00");
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
                        subTotal = NumberUtils.formatNumber(total, "###,###,##0.00");
                        total = 0d;
                    }
                } else {
                    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
                        total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
                    }
                    subTotal = NumberUtils.formatNumber(total, "###,###,##0.00");
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
                        subTotal = NumberUtils.formatNumber(total, "###,###,##0.00");
                        total = 0d;
                    }
                } else {
                    if (null != transactionBean.getAmount() && !transactionBean.getAmount().trim().equals("")) {
                        total += Double.parseDouble(transactionBean.getAmount().replace(",", ""));
                    }
                    subTotal = NumberUtils.formatNumber(total, "###,###,##0.00");
                }
            }
        }
        return subTotal;
    }

    private void writePJSubledgerContent(List<TransactionBean> subLedgers) throws Exception {
        int rowCount = 0;
        if (null != subLedgers && !subLedgers.isEmpty()) {
            int i = 0;
            for (TransactionBean subLedger : subLedgers) {
                createRow();
                resetColumnIndex();
                CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
                CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
                createTextCell(subLedger.getCustomer(), textCellStyle);
                createTextCell(subLedger.getCustomerNo(), textCellStyle);
                createTextCell(null != subLedger.getApBatchId() ? subLedger.getApBatchId().toString() : "", textCellStyle);
                createTextCell(null != subLedger.getArBatchId() ? subLedger.getArBatchId().toString() : "", textCellStyle);
                createTextCell(subLedger.getGlAcctNo(), textCellStyle);
                createTextCell(subLedger.getBillofLadding(), textCellStyle);
                createTextCell(subLedger.getInvoiceOrBl(), textCellStyle);
                createTextCell(subLedger.getInvoiceNotes(), textCellStyle);
                createTextCell(subLedger.getVoyagenumber(), textCellStyle);
                createTextCell(subLedger.getChargeCode(), textCellStyle);
                createTextCell(subLedger.getRecordType(), textCellStyle);
                createTextCell(subLedger.getTransDate(), textCellStyle);
                createTextCell(null != subLedger.getSailingDate() ? DateUtils.parseDateToString(subLedger.getSailingDate()) : "", textCellStyle);
                createTextCell(null != subLedger.getPostedDate() ? DateUtils.parseDateToString(subLedger.getPostedDate()) : "", textCellStyle);
                createDoubleCell(null != subLedger.getAmount() ? Double.parseDouble(subLedger.getAmount().replaceAll(",", "")) : 0d, doubleCellStyle);
                String subTotal = "";
                if (null != recieptsLedgerForm.getSortBy() && !recieptsLedgerForm.getSortBy().trim().equals("0")) {
                    subTotal = this.calculateSubTotal(recieptsLedgerForm.getSortBy(), subLedger, subLedgers, i);
                }
                createDoubleCell("" != subTotal ? Double.parseDouble(subTotal.replaceAll(",", "")) : 0d, doubleCellStyle);
                rowCount++;
                i++;
            }
            setColumnAutoSize();
        }

    }

    private void writeACCSubledgerContent(List<TransactionBean> subLedgers) throws Exception {
        int rowCount = 0;
        if (null != subLedgers && !subLedgers.isEmpty()) {
            double grandTotal = 0d;
            double grandSubTotal = 0d;
            int i = 0;
            for (TransactionBean subLedger : subLedgers) {
                createRow();
                resetColumnIndex();
                CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
                CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
                createTextCell(subLedger.getCustomer(), textCellStyle);
                createTextCell(subLedger.getCustomerNo(), textCellStyle);
                createTextCell(subLedger.getGlAcctNo(), textCellStyle);
                createTextCell(subLedger.getBillofLadding(), textCellStyle);
                createTextCell(subLedger.getInvoiceOrBl(), textCellStyle);
                createTextCell(subLedger.getVoyagenumber(), textCellStyle);
                createTextCell(subLedger.getChargeCode(), textCellStyle);
                createTextCell(subLedger.getRecordType(), textCellStyle);
                createTextCell(subLedger.getTransDate(), textCellStyle);
                createTextCell(null != subLedger.getSailingDate() ? DateUtils.parseDateToString(subLedger.getSailingDate()) : "", textCellStyle);
                createTextCell(null != subLedger.getPostedDate() ? DateUtils.parseDateToString(subLedger.getPostedDate()) : "", textCellStyle);
                createDoubleCell(null != subLedger.getAmount() ? Double.parseDouble(subLedger.getAmount().replaceAll(",", "")) : 0d, doubleCellStyle);
                grandTotal += Double.parseDouble(subLedger.getAmount().replaceAll(",", ""));
                String subTotal = this.calculateSubTotal(CommonConstants.SORT_BY_GL_ACCOUNT, subLedger, subLedgers, i);
                if (null != subTotal && !subTotal.trim().equals("")) {
                    grandSubTotal += Double.parseDouble(subTotal.replaceAll(",", ""));
                }
                createDoubleCell("" != subTotal ? Double.parseDouble(subTotal.replaceAll(",", "")) : 0d, doubleCellStyle);
                rowCount++;
                i++;
            }
            setColumnAutoSize();
            createRow();
            resetColumnIndex();
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createEmptyCell(darkAshCellStyleRightBold);
            createTextCell("Grand Total", darkAshCellStyleRightBold);
            createDoubleCell(grandTotal, darkAshCellStyleRightBold);
            createDoubleCell(grandSubTotal, darkAshCellStyleRightBold);
        }
    }

    private void writeBothRevExpAndBillOfLaddingContent(List<TransactionBean> subLedgers) throws Exception {
        int rowCount = 0;
        if (null != subLedgers && !subLedgers.isEmpty()) {
            for (TransactionBean subLedger : subLedgers) {
                createRow();
                resetColumnIndex();
                CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
                CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
                createTextCell(subLedger.getBillofLadding(), textCellStyle);
                createTextCell(subLedger.getInvoiceOrBl(), textCellStyle);
                createTextCell(subLedger.getVoyagenumber(), textCellStyle);
                createDoubleCell(null != subLedger.getAmount() ? Double.parseDouble(subLedger.getAmount().replaceAll(",", "")) : 0d, doubleCellStyle);
                rowCount++;
            }
            setColumnAutoSize();
        }
    }

    private void writeSubLedgerContent(List<TransactionBean> subLedgers) throws Exception {
        String subLedgerType = recieptsLedgerForm.getSubLedgerType();
        int rowCount = 0;
        if (null != subLedgers && !subLedgers.isEmpty()) {
            int i = 0;
            for (TransactionBean subLedger : subLedgers) {
                createRow();
                resetColumnIndex();
                CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
                CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
                createTextCell(subLedger.getCustomer(), textCellStyle);
                createTextCell(subLedger.getCustomerNo(), textCellStyle);
                if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.ALL)) {
                    createTextCell(subLedger.getSubLedgerCode(), textCellStyle);
                }
                createTextCell(null != subLedger.getApBatchId() ? subLedger.getApBatchId().toString() : "", textCellStyle);
                createTextCell(null != subLedger.getArBatchId() ? subLedger.getArBatchId().toString() : "", textCellStyle);
                createTextCell(subLedger.getGlAcctNo(), textCellStyle);
                createTextCell(subLedger.getBillofLadding(), textCellStyle);
                createTextCell(subLedger.getInvoiceOrBl(), textCellStyle);
                createTextCell(subLedger.getChargeCode(), textCellStyle);
                createTextCell(subLedger.getVoyagenumber(), textCellStyle);
                createTextCell(subLedger.getTransDate(), textCellStyle);
                createTextCell(null != subLedger.getSailingDate() ? DateUtils.parseDateToString(subLedger.getSailingDate()) : "", textCellStyle);
                createTextCell(null != subLedger.getPostedDate() ? DateUtils.parseDateToString(subLedger.getPostedDate()) : "", textCellStyle);
                createTextCell(subLedger.getDebit(), textCellStyle);
                createTextCell(subLedger.getCredit(), textCellStyle);
                createTextCell(subLedger.getRecordType(), textCellStyle);
                createTextCell(subLedger.getLineitemNo(), textCellStyle);
                createTextCell(subLedger.getJournalentryNo(), textCellStyle);
                String subTotal = "";
                if (null != recieptsLedgerForm.getSortBy() && !recieptsLedgerForm.getSortBy().trim().equals("0")) {
                    subTotal = this.calculateSubTotal(recieptsLedgerForm.getSortBy(), subLedger, subLedgers, i);
                }
                createDoubleCell(!"".equals(subTotal) ? Double.parseDouble(subTotal.replaceAll(",", "")) : 0d, doubleCellStyle);
                rowCount++;
                i++;
            }
            setColumnAutoSize();
        }
    }

    private void writeContent(List<TransactionBean> subLedgerList) throws Exception {
        String subLedgerType = recieptsLedgerForm.getSubLedgerType();
        if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.SUB_LEDGER_CODE_PURCHASE_JOURNAL)) {
            writePJSubledgerContent(subLedgerList);
        } else if (null != subLedgerType && subLedgerType.trim().equals(CommonConstants.SUB_LEDGER_CODE_ACCRUALS)) {
            writeACCSubledgerContent(subLedgerList);
        } else {
            if (null != recieptsLedgerForm.getRevOrExp() && recieptsLedgerForm.getRevOrExp().trim().equals(GLMappingConstant.BOTH)
                    && null != recieptsLedgerForm.getSortBy() && recieptsLedgerForm.getSortBy().trim().equals(CommonConstants.SORT_BY_BILL_OF_LADDING)) {
                writeBothRevExpAndBillOfLaddingContent(subLedgerList);
            } else {
                writeSubLedgerContent(subLedgerList);
            }
        }
    }

    public String createExcel(List<TransactionBean> subLedgerList) throws Exception {
        try {
            StringBuilder fileName = new StringBuilder();
            fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/SubLedger/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileName.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            fileName.append("SubLedgerReport.xlsx");
            String subLedgerType = recieptsLedgerForm.getSubLedgerType();
            String sheetName = getSheetName(subLedgerType);
            init(fileName.toString(), sheetName);
            writeHeader();
            writeContent(subLedgerList);
            writeIntoFile();
            return fileName.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }
}
