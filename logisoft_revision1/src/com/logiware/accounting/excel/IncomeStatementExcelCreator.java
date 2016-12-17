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
public class IncomeStatementExcelCreator extends BaseExcelCreator {

    private String startPeriod;
    private String endPeriod;
    private Integer budgetSet;

    public IncomeStatementExcelCreator() {
    }

    public IncomeStatementExcelCreator(String startPeriod, String endPeriod, Integer budgetSet) {
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.budgetSet = budgetSet;
    }

    private void writeHeader() throws IOException {
        createRow();
        createHeaderCell("Statement of Earnings", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 7);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("Period : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(startPeriod + " - " + endPeriod, subHeaderOneCellStyleLeftNormal);
        createHeaderCell("Budget Set : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell("" + budgetSet, subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 3, 7);
        createRow();
        resetColumnIndex();
        createHeaderCell("Account", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 35);
        createHeaderCell("Current Period", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Current Selected Period", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Current Year (YTD)", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Prior Year to date", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Prior Year (Totals)", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Budget YTD", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
        createHeaderCell("Annual Budget", tableHeaderCellStyleCenterBold);
        sheet.setColumnWidth(columnIndex, 256 * 20);
    }

    private void writeContent() throws Exception {
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        List<AccountModel> salesRevenue = fiscalPeriodDAO.getIncomeStatement("Income Group 1", startPeriod, endPeriod, budgetSet);
        List<AccountModel> costOfSales = fiscalPeriodDAO.getIncomeStatement("Expense Group1", startPeriod, endPeriod, budgetSet);
        List<AccountModel> operatingExpenses = fiscalPeriodDAO.getIncomeStatement("Expense Group3", startPeriod, endPeriod, budgetSet);
        List<AccountModel> otherIncomeAndExpenses = fiscalPeriodDAO.getIncomeStatement("Income Group2", startPeriod, endPeriod, budgetSet);
        otherIncomeAndExpenses.addAll(fiscalPeriodDAO.getIncomeStatement("Expense Group4", startPeriod, endPeriod, budgetSet));
        Long cp1 = 0l;
        Long csp1 = 0l;
        Long cy1 = 0l;
        Long pyp1 = 0l;
        Long py1 = 0l;
        Long bp1 = 0l;
        Long by1 = 0l;
        createRow();
        resetColumnIndex();
        createTextCell("Sales Revenue", darkAshCellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, 7);
        int rowCount = 0;
        CellStyle textCellStyle;
        CellStyle doubleCellStyle;
        for (AccountModel account : salesRevenue) {
            createRow();
            resetColumnIndex();
            textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(account.getAccount(), textCellStyle);
            createLongCell(account.getCurrentPeriod(), doubleCellStyle);
            createLongCell(account.getCurrentSelectedPeriod(), doubleCellStyle);
            createLongCell(account.getCurrentYear(), doubleCellStyle);
            createLongCell(account.getPriorYearPeriod(), doubleCellStyle);
            createLongCell(account.getPriorYear(), doubleCellStyle);
            createLongCell(account.getBudgetPeriod(), doubleCellStyle);
            createLongCell(account.getBudgetYear(), doubleCellStyle);
            cp1 += Long.parseLong(account.getCurrentPeriod().replace(",", ""));
            csp1 += Long.parseLong(account.getCurrentSelectedPeriod().replace(",", ""));
            cy1 += Long.parseLong(account.getCurrentYear().replace(",", ""));
            pyp1 += Long.parseLong(account.getPriorYearPeriod().replace(",", ""));
            py1 += Long.parseLong(account.getPriorYear().replace(",", ""));
            bp1 += Long.parseLong(account.getBudgetPeriod().replace(",", ""));
            by1 += Long.parseLong(account.getBudgetYear().replace(",", ""));
            rowCount++;
        }
        createRow();
        resetColumnIndex();
        createTextCell("Total Sales Revenue", lavendarCellStyleLeftBold);
        createLongCell(cp1, lavendarCellStyleRightBold);
        createLongCell(csp1, lavendarCellStyleRightBold);
        createLongCell(cy1, lavendarCellStyleRightBold);
        createLongCell(pyp1, lavendarCellStyleRightBold);
        createLongCell(py1, lavendarCellStyleRightBold);
        createLongCell(bp1, lavendarCellStyleRightBold);
        createLongCell(by1, lavendarCellStyleRightBold);
        Long cp2 = 0l;
        Long csp2 = 0l;
        Long cy2 = 0l;
        Long pyp2 = 0l;
        Long py2 = 0l;
        Long bp2 = 0l;
        Long by2 = 0l;
        createRow();
        resetColumnIndex();
        createTextCell("Cost Of Sales", darkAshCellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, 7);
        for (AccountModel account : costOfSales) {
            createRow();
            resetColumnIndex();
            textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(account.getAccount(), textCellStyle);
            createLongCell(account.getCurrentPeriod(), doubleCellStyle);
            createLongCell(account.getCurrentSelectedPeriod(), doubleCellStyle);
            createLongCell(account.getCurrentYear(), doubleCellStyle);
            createLongCell(account.getPriorYearPeriod(), doubleCellStyle);
            createLongCell(account.getPriorYear(), doubleCellStyle);
            createLongCell(account.getBudgetPeriod(), doubleCellStyle);
            createLongCell(account.getBudgetYear(), doubleCellStyle);
            cp2 += Long.parseLong(account.getCurrentPeriod().replace(",", ""));
            csp2 += Long.parseLong(account.getCurrentSelectedPeriod().replace(",", ""));
            cy2 += Long.parseLong(account.getCurrentYear().replace(",", ""));
            pyp2 += Long.parseLong(account.getPriorYearPeriod().replace(",", ""));
            py2 += Long.parseLong(account.getPriorYear().replace(",", ""));
            bp2 += Long.parseLong(account.getBudgetPeriod().replace(",", ""));
            by2 += Long.parseLong(account.getBudgetYear().replace(",", ""));
            rowCount++;
        }
        createRow();
        resetColumnIndex();
        createTextCell("Total Cost Of Sales", lavendarCellStyleLeftBold);
        createLongCell(cp2, lavendarCellStyleRightBold);
        createLongCell(csp2, lavendarCellStyleRightBold);
        createLongCell(cy2, lavendarCellStyleRightBold);
        createLongCell(pyp2, lavendarCellStyleRightBold);
        createLongCell(py2, lavendarCellStyleRightBold);
        createLongCell(bp2, lavendarCellStyleRightBold);
        createLongCell(by2, lavendarCellStyleRightBold);
        createRow();
        resetColumnIndex();
        createTextCell("Gross Profit(Loss)", lightAshCellStyleLeftBold);
        createLongCell(cp1 - cp2, lightAshCellStyleRightBold);
        createLongCell(csp1 - csp2, lightAshCellStyleRightBold);
        createLongCell(cy1 - cy2, lightAshCellStyleRightBold);
        createLongCell(pyp1 - pyp2, lightAshCellStyleRightBold);
        createLongCell(py1 - py2, lightAshCellStyleRightBold);
        createLongCell(bp1 - bp2, lightAshCellStyleRightBold);
        createLongCell(by1 - by2, lightAshCellStyleRightBold);
        Long cp3 = 0l;
        Long csp3 = 0l;
        Long cy3 = 0l;
        Long pyp3 = 0l;
        Long py3 = 0l;
        Long bp3 = 0l;
        Long by3 = 0l;
        createRow();
        resetColumnIndex();
        createTextCell("Operating Expenses", darkAshCellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, 7);
        for (AccountModel account : operatingExpenses) {
            createRow();
            resetColumnIndex();
            textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(account.getAccount(), textCellStyle);
            createLongCell(account.getCurrentPeriod(), doubleCellStyle);
            createLongCell(account.getCurrentSelectedPeriod(), doubleCellStyle);
            createLongCell(account.getCurrentYear(), doubleCellStyle);
            createLongCell(account.getPriorYearPeriod(), doubleCellStyle);
            createLongCell(account.getPriorYear(), doubleCellStyle);
            createLongCell(account.getBudgetPeriod(), doubleCellStyle);
            createLongCell(account.getBudgetYear(), doubleCellStyle);
            cp3 += Long.parseLong(account.getCurrentPeriod().replace(",", ""));
            csp3 += Long.parseLong(account.getCurrentSelectedPeriod().replace(",", ""));
            cy3 += Long.parseLong(account.getCurrentYear().replace(",", ""));
            pyp3 += Long.parseLong(account.getPriorYearPeriod().replace(",", ""));
            py3 += Long.parseLong(account.getPriorYear().replace(",", ""));
            bp3 += Long.parseLong(account.getBudgetPeriod().replace(",", ""));
            by3 += Long.parseLong(account.getBudgetYear().replace(",", ""));
            rowCount++;
        }
        createRow();
        resetColumnIndex();
        createTextCell("Total Operating Expenses", lavendarCellStyleLeftBold);
        createLongCell(cp3, lavendarCellStyleRightBold);
        createLongCell(csp3, lavendarCellStyleRightBold);
        createLongCell(cy3, lavendarCellStyleRightBold);
        createLongCell(pyp3, lavendarCellStyleRightBold);
        createLongCell(py3, lavendarCellStyleRightBold);
        createLongCell(bp3, lavendarCellStyleRightBold);
        createLongCell(by3, lavendarCellStyleRightBold);
        createRow();
        resetColumnIndex();
        createTextCell("Earnings (Loss) from Operations", lightAshCellStyleLeftBold);
        createLongCell(cp1 - cp2 - cp3, lightAshCellStyleRightBold);
        createLongCell(csp1 - csp2 - csp3, lightAshCellStyleRightBold);
        createLongCell(cy1 - cy2 - cy3, lightAshCellStyleRightBold);
        createLongCell(pyp1 - pyp2 - pyp3, lightAshCellStyleRightBold);
        createLongCell(py1 - py2 - py3, lightAshCellStyleRightBold);
        createLongCell(bp1 - bp2 - bp3, lightAshCellStyleRightBold);
        createLongCell(by1 - by2 - by3, lightAshCellStyleRightBold);
        Long cp4 = 0l;
        Long csp4 = 0l;
        Long cy4 = 0l;
        Long pyp4 = 0l;
        Long py4 = 0l;
        Long bp4 = 0l;
        Long by4 = 0l;
        createRow();
        resetColumnIndex();
        createTextCell("Other Income and Expenses", darkAshCellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, 7);
        for (AccountModel account : otherIncomeAndExpenses) {
            createRow();
            resetColumnIndex();
            textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(account.getAccount(), textCellStyle);
            createLongCell(account.getCurrentPeriod(), doubleCellStyle);
            createLongCell(account.getCurrentSelectedPeriod(), doubleCellStyle);
            createLongCell(account.getCurrentYear(), doubleCellStyle);
            createLongCell(account.getPriorYearPeriod(), doubleCellStyle);
            createLongCell(account.getPriorYear(), doubleCellStyle);
            createLongCell(account.getBudgetPeriod(), doubleCellStyle);
            createLongCell(account.getBudgetYear(), doubleCellStyle);
            cp4 += Long.parseLong(account.getCurrentPeriod().replace(",", ""));
            csp4 += Long.parseLong(account.getCurrentSelectedPeriod().replace(",", ""));
            cy4 += Long.parseLong(account.getCurrentYear().replace(",", ""));
            pyp4 += Long.parseLong(account.getPriorYearPeriod().replace(",", ""));
            py4 += Long.parseLong(account.getPriorYear().replace(",", ""));
            bp4 += Long.parseLong(account.getBudgetPeriod().replace(",", ""));
            by4 += Long.parseLong(account.getBudgetYear().replace(",", ""));
            rowCount++;
        }
        createRow();
        resetColumnIndex();
        createTextCell("Total Other Income and Expenses", lavendarCellStyleLeftBold);
        createLongCell(cp4, lavendarCellStyleRightBold);
        createLongCell(csp4, lavendarCellStyleRightBold);
        createLongCell(cy4, lavendarCellStyleRightBold);
        createLongCell(pyp4, lavendarCellStyleRightBold);
        createLongCell(py4, lavendarCellStyleRightBold);
        createLongCell(bp4, lavendarCellStyleRightBold);
        createLongCell(by4, lavendarCellStyleRightBold);
        createRow();
        resetColumnIndex();
        createTextCell("Net Earnings (Loss)", lightAshCellStyleLeftBold);
        createLongCell(cp1 - cp2 - cp3 - cp4, lightAshCellStyleRightBold);
        createLongCell(csp1 - csp2 - csp3 - csp4, lightAshCellStyleRightBold);
        createLongCell(cy1 - cy2 - cy3 - cy4, lightAshCellStyleRightBold);
        createLongCell(pyp1 - pyp2 - pyp3 - pyp4, lightAshCellStyleRightBold);
        createLongCell(py1 - py2 - py3 - py4, lightAshCellStyleRightBold);
        createLongCell(bp1 - bp2 - bp3 - bp4, lightAshCellStyleRightBold);
        createLongCell(by1 - by2 - by3 - by4, lightAshCellStyleRightBold);
    }

    public String create() throws Exception {
        try {
            StringBuilder filePath = new StringBuilder();
            filePath.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/IncomeStatement/");
            filePath.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(filePath.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            filePath.append("IncomeStatement_").append(startPeriod).append("_").append(endPeriod).append(".xlsx");
            init(filePath.toString(), "IncomeStatement");
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
