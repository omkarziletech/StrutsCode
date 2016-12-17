package com.gp.cong.logisoft.reports;

import java.util.Iterator;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import com.gp.cong.logisoft.reports.dto.IncomeStatementReportDTO;
import com.gp.cong.logisoft.reports.dto.IncomeStatementRevenueDTO;
import java.util.Date;

import org.apache.log4j.Logger;



public class IncomeStatementExportToExcel {
private static final Logger log = Logger.getLogger(IncomeStatementExportToExcel.class);
    public WritableSheet getExcelData(WritableWorkbook writableWorkbook, IncomeStatementReportDTO incomeStatementReportDTO) {
        WritableSheet writableSheet = writableWorkbook.createSheet("BudgetSheet", 0);
        try {
            WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableFont wf1 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            WritableCellFormat cf = new WritableCellFormat(wf);
            cf.setBackground(Colour.GRAY_25);
            WritableCellFormat cf1 = new WritableCellFormat(wf1);
            cf1.setAlignment(Alignment.RIGHT);
            WritableCellFormat cf2 = new WritableCellFormat(wf);
            cf2.setAlignment(Alignment.RIGHT);
            cf2.setBackground(Colour.GRAY_25);
            writableSheet.addCell(new Label(1, 1, "CompanyName:"));
            writableSheet.addCell(new Label(2, 1, incomeStatementReportDTO.getCompanyName()));

            //This is Title
            writableSheet.addCell(new Label(1, 3, "Statement of Earnings", cf));

            //This is for period
            writableSheet.addCell(new Label(1, 5, "BeginningPeriod:"));
            writableSheet.addCell(new Label(2, 5, incomeStatementReportDTO.getBeginningPeriod()));
            writableSheet.addCell(new Label(3, 5, "EndPeriod:"));
            writableSheet.addCell(new Label(4, 5, incomeStatementReportDTO.getEndPeriod()));
            writableSheet.addCell(new Label(5, 5, "Year:"));
            writableSheet.addCell(new Label(6, 5, incomeStatementReportDTO.getYear()));

            //This is for column titles
            writableSheet.addCell(new Label(1, 7, "", cf));
            writableSheet.addCell(new Label(2, 7, "Current Year", cf));
            writableSheet.addCell(new Label(3, 7, "Previous Year", cf));
            writableSheet.addCell(new Label(4, 7, "Current Month", cf));
            writableSheet.addCell(new Label(5, 7, "Prior Year", cf));
            writableSheet.addCell(new Label(6, 7, "Budget YTD", cf));
            writableSheet.addCell(new Label(7, 7, "Annual Budget", cf));

            //This is for salce revenue
            writableSheet.addCell(new Label(1, 8, "Sales Revenue", cf));
            int SalesRevenueRow = 9;
            int SalesRevenueCol = 1;
            for (Iterator iterator = incomeStatementReportDTO.getSalesRevenue().iterator(); iterator.hasNext();) {
                IncomeStatementRevenueDTO incomeStatementRevenueDTO = (IncomeStatementRevenueDTO) iterator.next();
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, incomeStatementRevenueDTO.getAccountNumber()));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementRevenueDTO.getAmount() ? incomeStatementRevenueDTO.getAmount() : 0d)), cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementRevenueDTO.getPrevYearAmount() ? incomeStatementRevenueDTO.getPrevYearAmount() : 0d)), cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                SalesRevenueRow++;
                SalesRevenueCol = 1;
            }

            //This is for Total Sales Revenue
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getCurrentYearSalesRevenueTotal() ? incomeStatementReportDTO.getCurrentYearSalesRevenueTotal() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getPreviousYearSalesRevenueTotal() ? incomeStatementReportDTO.getPreviousYearSalesRevenueTotal() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            SalesRevenueRow++;
            SalesRevenueCol = 1;

            //This is for Cost of Goods Sold
            writableSheet.addCell(new Label(1, SalesRevenueRow, "Cost of Sales", cf));
            SalesRevenueRow++;
            for (Iterator iterator = incomeStatementReportDTO.getCostOfGoodsSoldRevenue().iterator(); iterator.hasNext();) {
                IncomeStatementRevenueDTO incomeStatementRevenueDTO = (IncomeStatementRevenueDTO) iterator.next();
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, incomeStatementRevenueDTO.getAccountNumber()));
                if (incomeStatementRevenueDTO.getAmount() != null) {
                    Long currentYearAmount = Math.round(incomeStatementRevenueDTO.getAmount());
                    String currentAmount = String.valueOf(Math.round(currentYearAmount));
                    if (currentYearAmount < 0) {
                        currentAmount = "(" + currentAmount + ")";
                    }
                    writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, currentAmount, cf1));
                } else {
                    writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                }
                if (incomeStatementRevenueDTO.getPrevYearAmount() != null) {
                    Long prevYearAmount = Math.round(incomeStatementRevenueDTO.getPrevYearAmount());
                    String prevAmount = String.valueOf(Math.round(prevYearAmount));
                    if (prevYearAmount < 0) {
                        prevAmount = "(" + prevAmount + ")";
                    }
                    writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, prevAmount, cf1));
                } else {
                    writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                }
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                SalesRevenueRow++;
                SalesRevenueCol = 1;
            }
            for (Iterator iterator = incomeStatementReportDTO.getCostOfGoodsSoldRevenueGroup2().iterator(); iterator.hasNext();) {
                IncomeStatementRevenueDTO incomeStatementRevenueDTO = (IncomeStatementRevenueDTO) iterator.next();
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, incomeStatementRevenueDTO.getAccountNumber()));
                if (incomeStatementRevenueDTO.getAmount() != null) {
                    Long currentYearAmount = Math.round(incomeStatementRevenueDTO.getAmount());
                    String currentAmount = String.valueOf(Math.round(currentYearAmount));
                    if (currentYearAmount < 0) {
                        currentAmount = "(" + currentAmount + ")";
                    }
                    writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, currentAmount, cf1));
                } else {
                    writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                }
                if (incomeStatementRevenueDTO.getPrevYearAmount() != null) {
                    Long prevYearAmount = Math.round(incomeStatementRevenueDTO.getPrevYearAmount());
                    String prevAmount = String.valueOf(Math.round(prevYearAmount));
                    if (prevYearAmount < 0) {
                        prevAmount = "(" + prevAmount + ")";
                    }
                    writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, prevAmount, cf1));
                } else {
                    writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                }
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                SalesRevenueRow++;
                SalesRevenueCol = 1;
            }
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getCurrentYearCostOfGoodsSold() ? incomeStatementReportDTO.getCurrentYearCostOfGoodsSold() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getPreviousYearCostOfGoodsSold() ? incomeStatementReportDTO.getPreviousYearCostOfGoodsSold() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            SalesRevenueRow++;
            SalesRevenueCol = 1;

            //This is for GrossProfit(Loss)
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "GrossProfit(Loss)", cf));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getCurrentYearGrossProfit() ? incomeStatementReportDTO.getCurrentYearGrossProfit() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getPreviousYearGrossProfit() ? incomeStatementReportDTO.getPreviousYearGrossProfit() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "Gross Margin", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            SalesRevenueRow++;
            SalesRevenueCol = 1;

            //This is for Operating Expenses
            writableSheet.addCell(new Label(SalesRevenueCol, SalesRevenueRow, "Operating Expenses", cf));
            for (Iterator iterator = incomeStatementReportDTO.getCurrentYearExpenseGroup3Account().iterator(); iterator.hasNext();) {
                Object[] incomeStatementRevenueDTO = (Object[]) iterator.next();
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, incomeStatementRevenueDTO[1].toString()));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(Double.parseDouble(null != incomeStatementRevenueDTO[0] ? incomeStatementRevenueDTO[0].toString() : "0"))), cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                SalesRevenueRow++;
                SalesRevenueCol = 1;
            }

            //Operating Expances Total
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "", cf));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getCurrentYearExpenseGroup3Sum() ? incomeStatementReportDTO.getCurrentYearExpenseGroup3Sum() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getPreviousYearExpenseGroup3Sum() ? incomeStatementReportDTO.getPreviousYearExpenseGroup3Sum() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            SalesRevenueRow++;
            SalesRevenueCol = 1;

            //This is for Earnings (Loss) from Operations
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "Earnings (Loss) from Operations", cf));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getCurrentEarningOperations() ? incomeStatementReportDTO.getCurrentEarningOperations() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getPreviousEarningOperations() ? incomeStatementReportDTO.getPreviousEarningOperations() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            SalesRevenueRow++;
            SalesRevenueCol = 1;

            //This is for Other Income and Expenses
            writableSheet.addCell(new Label(SalesRevenueCol, SalesRevenueRow++, "Other Income and Expenses", cf));
            for (Iterator iterator = incomeStatementReportDTO.getCurrentYearIncomeGroup2Account().iterator(); iterator.hasNext();) {
                Object[] incomeStatementRevenueDTO1 = (Object[]) iterator.next();
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, null != incomeStatementRevenueDTO1[1] ? incomeStatementRevenueDTO1[1].toString() : ""));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(Double.parseDouble(null != incomeStatementRevenueDTO1[0] ? incomeStatementRevenueDTO1[0].toString() : "0"))), cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                SalesRevenueRow++;
                SalesRevenueCol = 1;
            }
            for (Iterator iterator = incomeStatementReportDTO.getPreviousYearIncomeGroup2Account().iterator(); iterator.hasNext();) {
                Object[] incomeStatementRevenueDTO1 = (Object[]) iterator.next();
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, null != incomeStatementRevenueDTO1[1] ? incomeStatementRevenueDTO1[1].toString() : ""));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(Double.parseDouble(null != incomeStatementRevenueDTO1[0] ? incomeStatementRevenueDTO1[0].toString() : "0"))), cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                SalesRevenueRow++;
                SalesRevenueCol = 1;
            }
            for (Iterator iterator = incomeStatementReportDTO.getCurrentYearExpenseGroup4Account().iterator(); iterator.hasNext();) {
                Object[] incomeStatementRevenueDTO1 = (Object[]) iterator.next();
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, null != incomeStatementRevenueDTO1[1] ? incomeStatementRevenueDTO1[1].toString() : ""));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(Double.parseDouble(null != incomeStatementRevenueDTO1[0] ? incomeStatementRevenueDTO1[0].toString() : "0"))), cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                SalesRevenueRow++;
                SalesRevenueCol = 1;
            }
            for (Iterator iterator = incomeStatementReportDTO.getPreviousYearExpenseGroup4Account().iterator(); iterator.hasNext();) {
                Object[] incomeStatementRevenueDTO = (Object[]) iterator.next();
                Object[] incomeStatementRevenueDTO1 = (Object[]) iterator.next();
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, null != incomeStatementRevenueDTO1[1] ? incomeStatementRevenueDTO1[1].toString() : ""));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(Double.parseDouble(null != incomeStatementRevenueDTO1[0] ? incomeStatementRevenueDTO1[0].toString() : "0"))), cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf1));
                SalesRevenueRow++;
                SalesRevenueCol = 1;
            }
            //OtherIncome Total
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "", cf));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getCurrentOtherExpense4Total() ? incomeStatementReportDTO.getCurrentOtherExpense4Total() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getPreviousOtherExpense4Total() ? incomeStatementReportDTO.getPreviousOtherExpense4Total() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            SalesRevenueRow++;
            SalesRevenueCol = 1;

            //This is for Net Earnings(Loss)
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "Net Earnings(Loss)", cf));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getCurrentNetEarnings() ? incomeStatementReportDTO.getCurrentNetEarnings() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, String.valueOf(Math.round(null != incomeStatementReportDTO.getPreviousNetEarnings() ? incomeStatementReportDTO.getPreviousNetEarnings() : 0d)), cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));
            writableSheet.addCell(new Label(SalesRevenueCol++, SalesRevenueRow, "0", cf2));

        } catch (Exception e) {
            log.info("IncomeStatementExportToExcel failed on " + new Date(),e);
        }
        return writableSheet;
    }
}