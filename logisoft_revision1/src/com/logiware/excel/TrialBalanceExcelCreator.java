package com.logiware.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.bean.ReportBean;
import com.logiware.hibernate.dao.TrialBalanceDAO;
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

    private String startAccount;
    private String endAccount;
    private String year;
    private String period;
    private boolean ecuReport;

    public TrialBalanceExcelCreator() {
    }

    public TrialBalanceExcelCreator(String startAccount, String endAccount, String year, String period, boolean reportType) {
        this.startAccount = startAccount;
        this.endAccount = endAccount;
        this.year = year;
        this.period = period;
        this.ecuReport = reportType;
    }

    private void writeHeader() throws IOException {
        createRow();
        createHeaderCell("Trial Balance Report", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 3);
        row.setHeightInPoints(20);
        if (CommonUtils.isNotEmpty(startAccount) && CommonUtils.isNotEmpty(endAccount)) {
            createRow();
            resetColumnIndex();
            createHeaderCell("Start Account : ", subHeaderOneCellStyleLeftBold);
            createHeaderCell(startAccount, subHeaderOneCellStyleLeftNormal);
            createHeaderCell("End Account : ", subHeaderOneCellStyleLeftBold);
            createHeaderCell(endAccount, subHeaderOneCellStyleLeftNormal);
        }
        createRow();
        resetColumnIndex();
        createHeaderCell("Year : ", subHeaderTwoCellStyleLeftBold);
        createHeaderCell(year, subHeaderTwoCellStyleLeftBold);
        createHeaderCell("Period : ", subHeaderTwoCellStyleLeftBold);
        String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        createHeaderCell(month[Integer.parseInt(period) - 1], subHeaderTwoCellStyleLeftBold);
        createRow();
        resetColumnIndex();
        if (ecuReport) {
            createHeaderCell("ECU Account", tableHeaderCellStyleCenterBold);
            createHeaderCell("Account Type", tableHeaderCellStyleCenterBold);
        }else{
            createHeaderCell("Account", tableHeaderCellStyleCenterBold);
            createHeaderCell("Description", tableHeaderCellStyleCenterBold);
        }
        createHeaderCell("Debit", tableHeaderCellStyleCenterBold);
        createHeaderCell("Credit", tableHeaderCellStyleCenterBold);
    }

    private void writeContent() throws Exception {
        List<ReportBean> trialBalances = new TrialBalanceDAO().getTrialBalances(startAccount, endAccount, year, period, ecuReport);
        int rowCount = 0;
        double totalDebit = 0d;
        double totalCredit = 0d;
        if (ecuReport) {
            for (ReportBean trialBalance : trialBalances) {
                createRow();
                resetColumnIndex();
                CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
                CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
                createTextCell(trialBalance.getReportCategory(), textCellStyle);
                createTextCell(trialBalance.getAccountType(), textCellStyle);
                createDoubleCell(trialBalance.getDebit(), doubleCellStyle);
                createDoubleCell(trialBalance.getCredit(), doubleCellStyle);
                totalDebit += Double.parseDouble(trialBalance.getDebit().replace(",", ""));
                totalCredit += Double.parseDouble(trialBalance.getCredit().replace(",", ""));
                rowCount++;
            }
        } else {
            for (ReportBean trialBalance : trialBalances) {
                createRow();
                resetColumnIndex();
                CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
                CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
                createTextCell(trialBalance.getAccount(), textCellStyle);
                createTextCell(trialBalance.getDescription(), textCellStyle);
                createDoubleCell(trialBalance.getDebit(), doubleCellStyle);
                createDoubleCell(trialBalance.getCredit(), doubleCellStyle);
                totalDebit += Double.parseDouble(trialBalance.getDebit().replace(",", ""));
                totalCredit += Double.parseDouble(trialBalance.getCredit().replace(",", ""));
                rowCount++;
            }
        }
        setColumnAutoSize();
        createRow();
        resetColumnIndex();
        createEmptyCell(darkAshCellStyleRightBold);
        createTextCell("Total", darkAshCellStyleRightBold);
        createDoubleCell(totalDebit, darkAshCellStyleRightBold);
        createDoubleCell(totalCredit, darkAshCellStyleRightBold);
    }

    public String create() throws Exception {
        try {
            String sheetName = "TrialBalance";
            StringBuilder fileName = new StringBuilder();
            fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/TrialBalance/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileName.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            fileName.append("TrialBalance_").append(year).append("_").append(period).append(".xlsx");
            init(fileName.toString(), sheetName);
            writeHeader();
            writeContent();
            writeIntoFile();
            return fileName.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }
}
