package com.logiware.accounting.excel;

import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.FiscalPeriodDAO;
import com.logiware.accounting.model.AccountModel;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author Lakshmi Narayanan
 */
public class TrialBalanceExcelCreator extends BaseExcelCreator {

    private String startPeriod;
    private String endPeriod;
    private boolean ecuFormat;

    public TrialBalanceExcelCreator() {
    }

    public TrialBalanceExcelCreator(String startPeriod, String endPeriod, boolean ecuFormat) {
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.ecuFormat = ecuFormat;
    }

    private void writeHeader() throws IOException {
        createRow();
        createHeaderCell("Trial Balance Report", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 3);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("Starting Period : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(startPeriod, subHeaderOneCellStyleLeftNormal);
        createHeaderCell("Ending Period : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(endPeriod, subHeaderOneCellStyleLeftNormal);
        createRow();
        resetColumnIndex();
        if (ecuFormat) {
            createHeaderCell("ECU Account", tableHeaderCellStyleCenterBold);
            createHeaderCell("Account Type", tableHeaderCellStyleCenterBold);
        } else {
            createHeaderCell("Account", tableHeaderCellStyleCenterBold);
            createHeaderCell("Description", tableHeaderCellStyleCenterBold);
        }
        createHeaderCell("Debit", tableHeaderCellStyleCenterBold);
        createHeaderCell("Credit", tableHeaderCellStyleCenterBold);
    }

    private void writeContent() throws Exception {
        List<AccountModel> accounts = new FiscalPeriodDAO().getTrialBalances(startPeriod, endPeriod, ecuFormat);
        int rowCount = 0;
        double totalDebit = 0d;
        double totalCredit = 0d;
        AccountModel profit = accounts.get(accounts.size() - 1);
        accounts.remove(accounts.size() - 1);
        for (AccountModel account : accounts) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(account.getAccount(), textCellStyle);
            createTextCell(account.getDescription(), textCellStyle);
            createAmountCell(account.getDebit(), doubleCellStyle);
            createAmountCell(account.getCredit(), doubleCellStyle);
            totalDebit += Double.parseDouble(account.getDebit().replace(",", ""));
            totalCredit += Double.parseDouble(account.getCredit().replace(",", ""));
            rowCount++;
        }
        setColumnAutoSize();
        createRow();
        resetColumnIndex();
        createEmptyCell(darkAshCellStyleRightBold);
        createTextCell("Total", darkAshCellStyleRightBold);
        createAmountCell(totalDebit, darkAshCellStyleRightBold);
        createAmountCell(totalCredit, darkAshCellStyleRightBold);
        setColumnAutoSize();
        createRow();
        resetColumnIndex();
        createEmptyCell(lightAshCellStyleRightBold);
        createTextCell(profit.getDescription(), lightAshCellStyleRightBold);
        createAmountCell(profit.getDebit(), lightAshCellStyleRightBold);
        createAmountCell(profit.getCredit(), lightAshCellStyleRightBold);
    }

    public String create() throws Exception {
        try {
            StringBuilder filePath = new StringBuilder();
            filePath.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/TrialBalance/");
            filePath.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(filePath.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            filePath.append("TrialBalance_").append(startPeriod).append("_").append(endPeriod).append(".xlsx");
            init(filePath.toString(), "TrialBalance");
            writeHeader();
            writeContent();
            writeIntoFile();
            return filePath.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }
}
