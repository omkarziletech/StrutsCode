package com.logiware.accounting.excel;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.ExcelGenerator.ExcelSheetConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.ChartOfAccountBean;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 *
 * @author venugopal.s
 */
public class ChartOfAccountsExcelCreator extends BaseExcelCreator {

    private List<ChartOfAccountBean> accountDetailsList;

    public ChartOfAccountsExcelCreator() {
    }

    public ChartOfAccountsExcelCreator(List<ChartOfAccountBean> accountDetailsList) {
        this.accountDetailsList = accountDetailsList;
    }

    private void writeHeader() throws Exception {
        createRow();
        createHeaderCell(ExcelSheetConstants.COA_LIST_OF_ACCOUNTS, headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 4);
        createHeaderCell(ExcelSheetConstants.DATE, headerCellStyleCenterBold);
        createHeaderCell(DateUtils.formatDate(new Date(), "MM/dd/yyyy"), headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 6, 7);

        createRow();
        resetColumnIndex();
        createHeaderCell(ExcelSheetConstants.COA_ACCOUNT, tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell(ExcelSheetConstants.COA_ACCOUNT_DESCRIPTION, tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 40);
        createHeaderCell(ExcelSheetConstants.COA_NORMAL_BALANCE, tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell(ExcelSheetConstants.COA_MULTI_CURRENCY, tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell(ExcelSheetConstants.COA_ACCOUNT_STATUS, tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 15);
        createHeaderCell(ExcelSheetConstants.COA_ACCOUNT_TYPE, tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell(ExcelSheetConstants.COA_GROUP_DESCRIPTION, tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell(ExcelSheetConstants.COA_CONTROL_ACCOUNT, tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
    }

    private void writeContent() throws Exception {
        int rowCount = 0;
        for (ChartOfAccountBean accountBean : accountDetailsList) {
            rowCount++;
            createRow();
            resetColumnIndex();
            if (rowCount % 2 == 0) {
                createTextCell(accountBean.getAcct(), tableEvenRowCellStyleLeftNormal);
                createTextCell(accountBean.getDesc(), tableEvenRowCellStyleLeftNormal);
                createTextCell(accountBean.getNormalbalance(), tableEvenRowCellStyleLeftNormal);
                createTextCell(accountBean.getMulticurrency(), tableEvenRowCellStyleLeftNormal);
                createTextCell(accountBean.getStatus(), tableEvenRowCellStyleLeftNormal);
                createTextCell(accountBean.getAcctType(), tableEvenRowCellStyleLeftNormal);
                createTextCell(accountBean.getDescription(), tableEvenRowCellStyleLeftNormal);
                createTextCell(accountBean.getControlAcct(), tableEvenRowCellStyleLeftNormal);
            } else {
                createTextCell(accountBean.getAcct(), tableOddRowCellStyleLeftNormal);
                createTextCell(accountBean.getDesc(), tableOddRowCellStyleLeftNormal);
                createTextCell(accountBean.getNormalbalance(), tableOddRowCellStyleLeftNormal);
                createTextCell(accountBean.getMulticurrency(), tableOddRowCellStyleLeftNormal);
                createTextCell(accountBean.getStatus(), tableOddRowCellStyleLeftNormal);
                createTextCell(accountBean.getAcctType(), tableOddRowCellStyleLeftNormal);
                createTextCell(accountBean.getDescription(), tableOddRowCellStyleLeftNormal);
                createTextCell(accountBean.getControlAcct(), tableOddRowCellStyleLeftNormal);
            }
        }
    }

    public String create() throws Exception {
        try {
            StringBuilder fileNameBuilder = new StringBuilder();
            fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/GeneralLedger/");
            fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileNameBuilder.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            fileNameBuilder.append("ChartOfAccounts.xlsx");
            init(fileNameBuilder.toString(), "ChartOfAccounts");
            writeHeader();
            writeContent();
            writeIntoFile();
            return fileNameBuilder.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }

}
