/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.accounting.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.FiscalYear;
import com.gp.cvst.logisoft.hibernate.dao.AccountBalanceDAO;
import com.gp.cvst.logisoft.struts.form.TransactionHistoryForm;
import com.logiware.accounting.dao.ChartOfAccountsDAO;
import com.logiware.accounting.dao.FiscalPeriodDAO;
import com.logiware.accounting.model.AccountModel;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author venugopal.s
 */
public class TransactionHistoryExcelCreator extends BaseExcelCreator {

    private TransactionHistoryForm transactionHistoryForm;

    public TransactionHistoryExcelCreator() {
    }

    public TransactionHistoryExcelCreator(TransactionHistoryForm transactionHistoryForm) {
        this.transactionHistoryForm = transactionHistoryForm;
    }

    private void writeHeader() throws IOException {
        createRow();
        createHeaderCell("Transaction History Report", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 9);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("Starting Account", subHeaderOneCellStyleLeftBold);
        createHeaderCell(transactionHistoryForm.getAccount(), subHeaderOneCellStyleLeftNormal);
        createHeaderCell("Period From", subHeaderOneCellStyleLeftBold);
        createHeaderCell(transactionHistoryForm.getDatefrom(), subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 3, 9);
        createRow();
        resetColumnIndex();
        createHeaderCell("Ending Account", subHeaderOneCellStyleLeftBold);
        createHeaderCell(transactionHistoryForm.getAccount1(), subHeaderOneCellStyleLeftNormal);
        createHeaderCell("Period To", subHeaderOneCellStyleLeftBold);
        createHeaderCell(transactionHistoryForm.getDateto(), subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 3, 9);

        createRow();
        resetColumnIndex();
        createHeaderCell("Account", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Period", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Date", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Source Code", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Reference", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Description", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 40);
        createHeaderCell("Debit", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Credit", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Net Change", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 30);
        createHeaderCell("Balance", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);

    }

    private void writeContent() throws Exception {
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        AccountBalanceDAO accountBalanceDAO = new AccountBalanceDAO();
        ChartOfAccountsDAO chartOfAccountsDAO = new ChartOfAccountsDAO();
        FiscalYear fiscalYear = fiscalPeriodDAO.getLastClosedYear(transactionHistoryForm.getDatefrom());
        Integer sPeriod = Integer.parseInt(transactionHistoryForm.getDatefrom());
        Integer ePeriod = Integer.parseInt(fiscalYear.getEndPeriod());
        List<String> periods = fiscalPeriodDAO.getPeriods(transactionHistoryForm.getDatefrom(), transactionHistoryForm.getDateto(), ePeriod <= sPeriod);
        FiscalPeriod previousPeriod = fiscalPeriodDAO.getPreviousPeriod(transactionHistoryForm.getDatefrom());
        Map<String, Double> closingBalances = accountBalanceDAO.getClosingBalances(transactionHistoryForm.getAccount(), transactionHistoryForm.getAccount1(), previousPeriod.getPeriodDis());
        String whereClause = chartOfAccountsDAO.buildTransactionsWhere(transactionHistoryForm.getAccount(), transactionHistoryForm.getAccount1(), periods);
        List<AccountModel> transactions = chartOfAccountsDAO.getTransactions(whereClause);
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        Map<String, Double> sumAmountMap = new HashMap<String, Double>();
        Double openingBalance = 0.00;
        Double sumAmount = 0.00;
        String period = null;
        for (AccountModel transaction : transactions) {
            if (period != null && CommonUtils.isNotEqual(period, transaction.getPeriod())) {
                createRow();
                resetColumnIndex();
                createEmptyCell(subHeaderOneCellStyleGreen);
                mergeCells(rowIndex, rowIndex, 0, 7);
                createHeaderCell("Period Closing Balance", subHeaderOneCellStyleGreen);
                createDoubleCell(sumAmount, subHeaderOneCellStyleGreen);
            }
            sumAmount = 0.00;
            if (null == countMap.get(transaction.getAccount())) {
                if (CommonUtils.isNotEmpty(closingBalances.get(transaction.getAccount()))) {
                    openingBalance = closingBalances.get(transaction.getAccount());
                } else {
                    openingBalance = 0.00;
                }
                countMap.put(transaction.getAccount(), 1);
                createRow();
                resetColumnIndex();
                createEmptyCell(subHeaderOneCellStyleGray);
                mergeCells(rowIndex, rowIndex, 0, 7);
                createHeaderCell("Opening Balance", subHeaderOneCellStyleGray);
                createDoubleCell(openingBalance, subHeaderOneCellStyleGray);
            } else {
                openingBalance = 0.00;
                countMap.put(transaction.getAccount(), countMap.get(transaction.getAccount()) + 1);
            }

            DBUtil dBUtil = new DBUtil();
            sumAmount = (null != sumAmountMap.get(transaction.getAccount()) ? sumAmountMap.get(transaction.getAccount()) : 0.00)
                    + (Double.parseDouble(dBUtil.removeComma(transaction.getNetChange())) + openingBalance);
            sumAmountMap.put(transaction.getAccount(), sumAmount);
            createRow();
            resetColumnIndex();
            createTextCell(transaction.getAccount(), cellStyleLeftBold);
            createTextCell(transaction.getPeriod(), cellStyleLeftBold);
            createTextCell(transaction.getDate(), cellStyleLeftBold);
            createTextCell(transaction.getSourceCode(), cellStyleLeftBold);
            createTextCell(transaction.getReference(), cellStyleLeftBold);
            createTextCell(transaction.getDescription(), cellStyleLeftBold);
            createTextCell(transaction.getDebit(), cellStyleLeftBold);
            createTextCell(transaction.getCredit(), cellStyleLeftBold);
            createTextCell(transaction.getNetChange(), cellStyleLeftBold);
            createDoubleCell(sumAmount, cellStyleLeftBold);
            period = transaction.getPeriod();
        }
    }

    public String create() throws Exception {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/GeneralLedger/");
            stringBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(stringBuilder.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            stringBuilder.append("TransactionHistory.xlsx");
            init(stringBuilder.toString(), "TransactionHistory");
            writeHeader();
            writeContent();
            writeIntoFile();
            return stringBuilder.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }
}
