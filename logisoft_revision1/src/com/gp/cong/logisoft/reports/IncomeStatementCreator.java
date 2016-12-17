package com.gp.cong.logisoft.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.AccountBalanceDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.bean.AccountsBean;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.FileOutputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
/**
 *
 * @author LakshmiNarayanan
 */
public class IncomeStatementCreator extends ReportFormatMethods {

    private static final Logger log = Logger.getLogger(IncomeStatementCreator.class);
    private Document document = null;
    private PdfWriter pdfWriter = null;
    /* The PdfTemplate that contains the total number of pages. */
    private PdfTemplate totalNoOfPages;
    /* The font that will be used. */
    private BaseFont helv;

    private void doInit(String fileName) {
        try {
            // this.statementVO = statementVO;
            document = new Document(PageSize.A4);
            document.setMargins(10, 10, 20, 80);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            pdfWriter.setPageEvent(new IncomeStatementCreator());
            document.open();
            PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f),
                    pdfWriter);
            pdfWriter.setOpenAction(action);
        } catch (Exception e) {
            log.info("ArStatementReport not initialized",e);
        }
    }

    @Override
    public void onOpenDocument(PdfWriter pdfWriter, Document document) {
        log.info("On Open IncomeStatement Document");
        try {
            totalNoOfPages = pdfWriter.getDirectContent().createTemplate(20, 10);
            totalNoOfPages.setBoundingBox(new Rectangle(-10, -10, 20, 50));
            helv = BaseFont.createFont(BaseFont.HELVETICA_BOLD,
                    BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.info("On Open IncomeStatement Document failed :- ",e);
        }
    }

    private void writeDocument(String contextPath, String fromPeriod, String toPeriod, Integer year,String budgetSet) throws Exception {
        AccountBalanceDAO accountBalanceDAO = new AccountBalanceDAO();
        try {
            List<AccountsBean> salesRevenueAccounts = new ArrayList<AccountsBean>();
            List<AccountsBean> costOfSalesAccounts = new ArrayList<AccountsBean>();
            List<AccountsBean> operatingExpensesAccounts = new ArrayList<AccountsBean>();
            List<AccountsBean> otherIncomeOrExpensesAccounts = new ArrayList<AccountsBean>();
            List<AccountsBean> incomeGroup1Accounts = accountBalanceDAO.getAccountsForIncomeStatement(ReportConstants.INCOMEGROUP, fromPeriod, toPeriod, year,budgetSet);
            if (CommonUtils.isNotEmpty(incomeGroup1Accounts)) {
                salesRevenueAccounts.addAll(incomeGroup1Accounts);
            }
            List<AccountsBean> expensesGroup1Accounts = accountBalanceDAO.getAccountsForIncomeStatement(ReportConstants.EXPENSEGROUP1, fromPeriod, toPeriod, year,budgetSet);
            List<AccountsBean> expensesGroup2Accounts = accountBalanceDAO.getAccountsForIncomeStatement(ReportConstants.EXPENSEGROUP2, fromPeriod, toPeriod, year,budgetSet);
            if (CommonUtils.isNotEmpty(expensesGroup1Accounts)) {
                costOfSalesAccounts.addAll(expensesGroup1Accounts);
            }
            List<AccountsBean> expensesGroup3Accounts = accountBalanceDAO.getAccountsForIncomeStatement(ReportConstants.EXPENSEGROUP3, fromPeriod, toPeriod, year,budgetSet);
            if (CommonUtils.isNotEmpty(expensesGroup3Accounts)) {
                operatingExpensesAccounts.addAll(expensesGroup3Accounts);
            }
            List<AccountsBean> incomeGroup2Accounts = accountBalanceDAO.getAccountsForIncomeStatement(ReportConstants.INCOMEGROUP2, fromPeriod, toPeriod, year,budgetSet);
            if (CommonUtils.isNotEmpty(incomeGroup2Accounts)) {
                otherIncomeOrExpensesAccounts.addAll(incomeGroup2Accounts);
            }
            List<AccountsBean> expensesGroup4Accounts = accountBalanceDAO.getAccountsForIncomeStatement(ReportConstants.EXPENSEGROUP4, fromPeriod, toPeriod, year,budgetSet);
            if (CommonUtils.isNotEmpty(expensesGroup4Accounts)) {
                otherIncomeOrExpensesAccounts.addAll(expensesGroup4Accounts);
            }
            //Company Logo
            String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
            Image img;
            img = Image.getInstance(contextPath + imagePath);
            PdfPTable imageTable = new PdfPTable(1);
            imageTable.setWidthPercentage(100);
            img.scalePercent(50);
            PdfPCell imageCell = new PdfPCell();
            imageCell.addElement(new Chunk(img, 0, -22));
            imageCell.setBorder(0);
            imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            imageCell.setVerticalAlignment(Element.ALIGN_CENTER);
            imageTable.addCell(imageCell);
            document.add(imageTable);
            //empty table
            PdfPTable emptyTable = makeTable(1);
            PdfPCell emptyCell = makeCellRightNoBorder("");
            emptyTable.addCell(emptyCell);
            document.add(emptyTable);
            document.add(emptyTable);
            document.add(emptyTable);
            document.add(emptyTable);
            document.add(emptyTable);
            document.add(emptyTable);

            //Company Details
            PdfPTable companyTable = new PdfPTable(1);
            companyTable.setWidthPercentage(100);
            companyTable.addCell(makeCell("Company Name: " + new SystemRulesDAO().getSystemRulesByCode("CompanyName"),
                    Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackBoldFont, 0));
            document.add(companyTable);
            document.add(emptyTable);
            document.add(emptyTable);

            //Statement Title
            PdfPTable titleTable = new PdfPTable(2);
            titleTable.setWidthPercentage(100);
            titleTable.setWidths(new float[]{50, 50});
            titleTable.getDefaultCell().setPadding(0);
            titleTable.getDefaultCell().setBorderWidth(0.5f);
            titleTable.getDefaultCell().setBorderWidthLeft(0.0f);
            titleTable.getDefaultCell().setBorderWidthRight(0.0f);
            PdfPCell titleCell = new PdfPCell(new Phrase("Statement of Earnings", headingFont1));
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleCell.setVerticalAlignment(Element.ALIGN_TOP);
            titleCell.setPaddingTop(-2);
            titleCell.setPaddingBottom(2);
            titleCell.setBorder(0);
            titleCell.setBackgroundColor(Color.LIGHT_GRAY);
            titleCell.setColspan(2);
            titleTable.addCell(titleCell);
            document.add(titleTable);
            document.add(emptyTable);

            //Period Details
            PdfPTable periodTable = new PdfPTable(3);
            periodTable.setWidthPercentage(100);
            periodTable.setWidths(new float[]{33, 33, 33});
            DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
            periodTable.addCell(makeCell("BeginningPeriod :" + dateFormatSymbols.getMonths()[Integer.parseInt(fromPeriod) - 1],
                    Element.ALIGN_CENTER, Element.ALIGN_CENTER, headingFont, 0));
            periodTable.addCell(makeCell("EndPeriod :" + dateFormatSymbols.getMonths()[Integer.parseInt(toPeriod) - 1],
                    Element.ALIGN_CENTER, Element.ALIGN_CENTER, headingFont, 0));
            periodTable.addCell(makeCell("Year :" + year,
                    Element.ALIGN_CENTER, Element.ALIGN_CENTER, headingFont, 0));
            document.add(periodTable);
            document.add(emptyTable);
            document.add(emptyTable);

            //List Heading
            PdfPTable headingTable = new PdfPTable(7);
            headingTable.setWidthPercentage(100);
            headingTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});
            headingTable.addCell(makeCell("Accounts", Element.ALIGN_CENTER, Element.ALIGN_CENTER, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            headingTable.addCell(makeCell("Current Month", Element.ALIGN_CENTER, Element.ALIGN_CENTER, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            headingTable.addCell(makeCell("Current Year (YTD)", Element.ALIGN_CENTER, Element.ALIGN_CENTER, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            headingTable.addCell(makeCell("Prior Year to date", Element.ALIGN_CENTER, Element.ALIGN_CENTER, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            headingTable.addCell(makeCell("Prior Year (Totals)", Element.ALIGN_CENTER, Element.ALIGN_CENTER, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            headingTable.addCell(makeCell("Budget YTD", Element.ALIGN_CENTER, Element.ALIGN_CENTER, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            headingTable.addCell(makeCell("Annual Budget", Element.ALIGN_CENTER, Element.ALIGN_CENTER, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            document.add(headingTable);

            //Sales Revenue
            PdfPTable salesRevenueTable = new PdfPTable(7);
            salesRevenueTable.setWidthPercentage(100);
            salesRevenueTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});
            salesRevenueTable.addCell(makeCell("Sales Revenue", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            salesRevenueTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            salesRevenueTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            salesRevenueTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            salesRevenueTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            salesRevenueTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            salesRevenueTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            Double totalCurrentYearBalanceForSalesRevenue = 0d;
            Double totalPreviousYearBalanceForSalesRevenue = 0d;
            Double totalCurrentMonthBalanceForSalesRevenue = 0d;
            Double totalPriorYearBalanceForSalesRevenue = 0d;
            Double totalBudgetYtdBalanceForSalesRevenue = 0d;
            Double totalAnnualBudgetBalanceForSalesRevenue = 0d;
            for (AccountsBean salesRevenueAccount : salesRevenueAccounts) {
                salesRevenueTable.addCell(makeCell(salesRevenueAccount.getAccountDescription(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFont, PdfPCell.BOX, null));
                salesRevenueTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(salesRevenueAccount.getCurrentMonthBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                salesRevenueTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(salesRevenueAccount.getCurrentYearBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                salesRevenueTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(salesRevenueAccount.getPriorYearBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                salesRevenueTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(salesRevenueAccount.getPreviousYearBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                salesRevenueTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(salesRevenueAccount.getBudgetYtdBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                salesRevenueTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(salesRevenueAccount.getAnnualBudgetBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));

                totalCurrentMonthBalanceForSalesRevenue += salesRevenueAccount.getCurrentMonthBalance();
                totalCurrentYearBalanceForSalesRevenue += salesRevenueAccount.getCurrentYearBalance();
                totalPriorYearBalanceForSalesRevenue += salesRevenueAccount.getPriorYearBalance();
                totalPreviousYearBalanceForSalesRevenue += salesRevenueAccount.getPreviousYearBalance();
                totalBudgetYtdBalanceForSalesRevenue += salesRevenueAccount.getBudgetYtdBalance();
                totalAnnualBudgetBalanceForSalesRevenue += salesRevenueAccount.getAnnualBudgetBalance();
            }

            //Total of Sales Revenue
            salesRevenueTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            salesRevenueTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalCurrentMonthBalanceForSalesRevenue), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            salesRevenueTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalCurrentYearBalanceForSalesRevenue), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            salesRevenueTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalPriorYearBalanceForSalesRevenue), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            salesRevenueTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalPreviousYearBalanceForSalesRevenue), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            salesRevenueTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalBudgetYtdBalanceForSalesRevenue), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            salesRevenueTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalAnnualBudgetBalanceForSalesRevenue), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            document.add(salesRevenueTable);

            //Cost Of Sales
            PdfPTable costOfSalesTable = new PdfPTable(7);
            costOfSalesTable.setWidthPercentage(100);
            costOfSalesTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});
            costOfSalesTable.addCell(makeCell("Cost Of Sales", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            costOfSalesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            costOfSalesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            costOfSalesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            costOfSalesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            costOfSalesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            costOfSalesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            Double totalCurrentYearBalanceForCostOfSales = 0d;
            Double totalPreviousYearBalanceForCostOfSales = 0d;
            Double totalCurrentMonthBalanceForCostOfSales = 0d;
            Double totalPriorYearBalanceForCostOfSales = 0d;
            Double totalBudgetYtdBalanceForCostOfSales = 0d;
            Double totalAnnualBudgetBalanceForCostOfSales = 0d;
            for (AccountsBean costOfSalesAccount : costOfSalesAccounts) {
                costOfSalesTable.addCell(makeCell(costOfSalesAccount.getAccountDescription(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFont, PdfPCell.BOX, null));
                costOfSalesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(costOfSalesAccount.getCurrentMonthBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                costOfSalesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(costOfSalesAccount.getCurrentYearBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                costOfSalesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(costOfSalesAccount.getPriorYearBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                costOfSalesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(costOfSalesAccount.getPreviousYearBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                costOfSalesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(costOfSalesAccount.getBudgetYtdBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                costOfSalesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(costOfSalesAccount.getAnnualBudgetBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));

                totalCurrentMonthBalanceForCostOfSales += costOfSalesAccount.getCurrentMonthBalance();
                totalCurrentYearBalanceForCostOfSales += costOfSalesAccount.getCurrentYearBalance();
                totalPriorYearBalanceForCostOfSales += costOfSalesAccount.getPriorYearBalance();
                totalPreviousYearBalanceForCostOfSales += costOfSalesAccount.getPreviousYearBalance();
                totalBudgetYtdBalanceForCostOfSales += costOfSalesAccount.getBudgetYtdBalance();
                totalAnnualBudgetBalanceForCostOfSales += costOfSalesAccount.getAnnualBudgetBalance();
            }
            //Total of Cost Of Sales
            costOfSalesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            costOfSalesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalCurrentMonthBalanceForCostOfSales), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            costOfSalesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalCurrentYearBalanceForCostOfSales), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            costOfSalesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalPriorYearBalanceForCostOfSales), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            costOfSalesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalPreviousYearBalanceForCostOfSales), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            costOfSalesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalBudgetYtdBalanceForCostOfSales), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            costOfSalesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalAnnualBudgetBalanceForCostOfSales), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            document.add(costOfSalesTable);
            // Gross Profit
            PdfPTable grossProfitTable = new PdfPTable(7);
            grossProfitTable.setWidthPercentage(100);
            grossProfitTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});
            Double grossProfitForCurrentMonth = totalCurrentMonthBalanceForSalesRevenue - totalCurrentMonthBalanceForCostOfSales;
            Double grossProfitForCurrentYear = totalCurrentYearBalanceForSalesRevenue - totalCurrentYearBalanceForCostOfSales;
            Double grossProfitForPriorYear = totalPriorYearBalanceForSalesRevenue - totalPriorYearBalanceForCostOfSales;
            Double grossProfitForPreviousYear = totalPreviousYearBalanceForSalesRevenue - totalPreviousYearBalanceForCostOfSales;
            Double grossProfitForBudgetYtd = totalBudgetYtdBalanceForSalesRevenue - totalBudgetYtdBalanceForCostOfSales;
            Double grossProfitForAnnualBudget = totalAnnualBudgetBalanceForSalesRevenue - totalAnnualBudgetBalanceForCostOfSales;
            grossProfitTable.addCell(makeCell("Gross Profit(Loss)", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            grossProfitTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(grossProfitForCurrentMonth), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            grossProfitTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(grossProfitForCurrentYear), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            grossProfitTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(grossProfitForPriorYear), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            grossProfitTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(grossProfitForPreviousYear), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            grossProfitTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(grossProfitForBudgetYtd), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            grossProfitTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(grossProfitForAnnualBudget), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            document.add(grossProfitTable);
//            Double grossMarginForCurrentYear = grossProfitForCurrentYear*100;
//            grossTable.addCell(makeCell("Gross Margin", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
//            grossTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(grossProfitForCurrentYear), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
//            grossTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(grossProfitForPreviousYear), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
//            grossTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(grossProfitForCurrentMonth), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
//            grossTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(grossProfitForPriorYear), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
//            grossTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(grossProfitForBudgetYtd), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
//            grossTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(grossProfitForAnnualBudget), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));


            //Operating Expenses table
            PdfPTable operatingExpensesTable = makeTable(7);
            operatingExpensesTable.setWidthPercentage(100);
            operatingExpensesTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});
            operatingExpensesTable.addCell(makeCell("Operating Expenses", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            operatingExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            operatingExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            operatingExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            operatingExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            operatingExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            operatingExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            Double totalCurrentYearBalanceForOperatingExpenses = 0d;
            Double totalPreviousYearBalanceForOperatingExpenses = 0d;
            Double totalCurrentMonthBalanceForOperatingExpenses = 0d;
            Double totalPriorYearBalanceForOperatingExpenses = 0d;
            Double totalBudgetYtdBalanceForOperatingExpenses = 0d;
            Double totalAnnualBudgetBalanceForOperatingExpenses = 0d;
            for (AccountsBean operatingExpensesAccount : operatingExpensesAccounts) {
                operatingExpensesTable.addCell(makeCell(operatingExpensesAccount.getAccountDescription(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFont, PdfPCell.BOX, null));
                operatingExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(operatingExpensesAccount.getCurrentMonthBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                operatingExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(operatingExpensesAccount.getCurrentYearBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                operatingExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(operatingExpensesAccount.getPriorYearBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                operatingExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(operatingExpensesAccount.getPreviousYearBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                operatingExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(operatingExpensesAccount.getBudgetYtdBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                operatingExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(operatingExpensesAccount.getAnnualBudgetBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));

                totalCurrentMonthBalanceForOperatingExpenses += operatingExpensesAccount.getCurrentMonthBalance();
                totalCurrentYearBalanceForOperatingExpenses += operatingExpensesAccount.getCurrentYearBalance();
                totalPriorYearBalanceForOperatingExpenses += operatingExpensesAccount.getPriorYearBalance();
                totalPreviousYearBalanceForOperatingExpenses += operatingExpensesAccount.getPreviousYearBalance();
                totalBudgetYtdBalanceForOperatingExpenses += operatingExpensesAccount.getBudgetYtdBalance();
                totalAnnualBudgetBalanceForOperatingExpenses += operatingExpensesAccount.getAnnualBudgetBalance();
            }
            //Total of Operating Expenses
            operatingExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            operatingExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalCurrentMonthBalanceForOperatingExpenses), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            operatingExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalCurrentYearBalanceForOperatingExpenses), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            operatingExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalPriorYearBalanceForOperatingExpenses), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            operatingExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalPreviousYearBalanceForOperatingExpenses), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            operatingExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalBudgetYtdBalanceForOperatingExpenses), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            operatingExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalAnnualBudgetBalanceForOperatingExpenses), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            document.add(operatingExpensesTable);

            // Earnings (Loss) from Operations
            PdfPTable earningsFromOperationsTable = new PdfPTable(7);
            earningsFromOperationsTable.setWidthPercentage(100);
            earningsFromOperationsTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});
            Double earningsFromOperationsForCurrentMonth = grossProfitForCurrentMonth - totalCurrentMonthBalanceForOperatingExpenses;
            Double earningsFromOperationsForCurrentYear = grossProfitForCurrentYear - totalCurrentYearBalanceForOperatingExpenses;
            Double earningsFromOperationsForPriorYear = grossProfitForPriorYear - totalPriorYearBalanceForOperatingExpenses;
            Double earningsFromOperationsForPreviousYear = grossProfitForPreviousYear - totalPreviousYearBalanceForOperatingExpenses;
            Double earningsFromOperationsForBudgetYtd = grossProfitForBudgetYtd - totalBudgetYtdBalanceForOperatingExpenses;
            Double earningsFromOperationsForAnnualBudget = grossProfitForAnnualBudget - totalAnnualBudgetBalanceForOperatingExpenses;
            earningsFromOperationsTable.addCell(makeCell("Earnings (Loss) from Operations", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            earningsFromOperationsTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(earningsFromOperationsForCurrentMonth), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            earningsFromOperationsTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(earningsFromOperationsForCurrentYear), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            earningsFromOperationsTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(earningsFromOperationsForPriorYear), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            earningsFromOperationsTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(earningsFromOperationsForPreviousYear), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            earningsFromOperationsTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(earningsFromOperationsForBudgetYtd), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            earningsFromOperationsTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(earningsFromOperationsForAnnualBudget), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            document.add(earningsFromOperationsTable);

            //Other Income and Expenses
            PdfPTable otherInComeOrExpensesTable = makeTable(7);
            otherInComeOrExpensesTable.setWidthPercentage(100);
            otherInComeOrExpensesTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});
            otherInComeOrExpensesTable.addCell(makeCell("Operating Expenses", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            otherInComeOrExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            otherInComeOrExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            otherInComeOrExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            otherInComeOrExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            otherInComeOrExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            otherInComeOrExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont,  PdfPCell.BOX, Color.LIGHT_GRAY));
            Double totalCurrentYearBalanceForOtherInComeOrExpenses = 0d;
            Double totalPreviousYearBalanceForOtherInComeOrExpenses = 0d;
            Double totalCurrentMonthBalanceForOtherInComeOrExpenses = 0d;
            Double totalPriorYearBalanceForOperOtherInComeOrExpenses = 0d;
            Double totalBudgetYtdBalanceForOtherInComeOrExpenses = 0d;
            Double totalAnnualBudgetBalanceForOtherInComeOrExpenses = 0d;
            for (AccountsBean operatingExpensesAccount : otherIncomeOrExpensesAccounts) {
                otherInComeOrExpensesTable.addCell(makeCell(operatingExpensesAccount.getAccountDescription(), Element.ALIGN_LEFT, Element.ALIGN_LEFT, blackFont, PdfPCell.BOX, null));
                otherInComeOrExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(operatingExpensesAccount.getCurrentMonthBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                otherInComeOrExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(operatingExpensesAccount.getCurrentYearBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                otherInComeOrExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(operatingExpensesAccount.getPriorYearBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                otherInComeOrExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(operatingExpensesAccount.getPreviousYearBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                otherInComeOrExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(operatingExpensesAccount.getBudgetYtdBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));
                otherInComeOrExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(operatingExpensesAccount.getAnnualBudgetBalance()), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, null));

                totalCurrentMonthBalanceForOtherInComeOrExpenses += operatingExpensesAccount.getCurrentMonthBalance();
                totalCurrentYearBalanceForOtherInComeOrExpenses += operatingExpensesAccount.getCurrentYearBalance();
                totalPriorYearBalanceForOperOtherInComeOrExpenses += operatingExpensesAccount.getPriorYearBalance();
                totalPreviousYearBalanceForOtherInComeOrExpenses += operatingExpensesAccount.getPreviousYearBalance();
                totalBudgetYtdBalanceForOtherInComeOrExpenses += operatingExpensesAccount.getBudgetYtdBalance();
                totalAnnualBudgetBalanceForOtherInComeOrExpenses += operatingExpensesAccount.getAnnualBudgetBalance();
            }
            //Total of Other Income and Expenses
            otherInComeOrExpensesTable.addCell(makeCell("", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            otherInComeOrExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalCurrentMonthBalanceForOtherInComeOrExpenses), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            otherInComeOrExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalCurrentYearBalanceForOtherInComeOrExpenses), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            otherInComeOrExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalPriorYearBalanceForOperOtherInComeOrExpenses), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            otherInComeOrExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalPreviousYearBalanceForOtherInComeOrExpenses), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            otherInComeOrExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalBudgetYtdBalanceForOtherInComeOrExpenses), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            otherInComeOrExpensesTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(totalAnnualBudgetBalanceForOtherInComeOrExpenses), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            document.add(otherInComeOrExpensesTable);

            // Earnings (Loss) from Operations
            PdfPTable netEarningsTable = new PdfPTable(7);
            netEarningsTable.setWidthPercentage(100);
            netEarningsTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});
            Double netEarningsForCurrentMonth = earningsFromOperationsForCurrentMonth - totalCurrentMonthBalanceForOtherInComeOrExpenses;
            Double netEarningsForCurrentYear = earningsFromOperationsForCurrentYear - totalCurrentYearBalanceForOtherInComeOrExpenses;
            Double netEarningsForPriorYear = earningsFromOperationsForPriorYear - totalPriorYearBalanceForOperOtherInComeOrExpenses;
            Double netEarningsForPreviousYear = earningsFromOperationsForPreviousYear - totalPreviousYearBalanceForOtherInComeOrExpenses;
            Double netEarningsForBudgetYtd = earningsFromOperationsForBudgetYtd - totalBudgetYtdBalanceForOtherInComeOrExpenses;
            Double netEarningsForAnnualBudget = earningsFromOperationsForAnnualBudget - totalAnnualBudgetBalanceForOtherInComeOrExpenses;
            netEarningsTable.addCell(makeCell("Net Earnings (Loss)", Element.ALIGN_LEFT, Element.ALIGN_LEFT, headingFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            netEarningsTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(netEarningsForCurrentMonth), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            netEarningsTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(netEarningsForCurrentYear), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            netEarningsTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(netEarningsForPriorYear), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            netEarningsTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(netEarningsForPreviousYear), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            netEarningsTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(netEarningsForBudgetYtd), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            netEarningsTable.addCell(makeCell(NumberUtils.formatNumber(Math.round(netEarningsForAnnualBudget), "###,###,##0"), Element.ALIGN_RIGHT, Element.ALIGN_RIGHT, blackFont, PdfPCell.BOX, Color.LIGHT_GRAY));
            document.add(netEarningsTable);
        } catch (Exception e) {
            throw e;
        }
    }

    //This is for Footer and Page X Of Y
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            log.debug("In onEndPage, page = " + document.getPageNumber());
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            BaseFont bfHelvNormal = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            String lineOnBottom = "_____________________________________________________________________________________________________________________________________________";
            cb.beginText();
            cb.setFontAndSize(bfHelvNormal, 8);
            cb.setTextMatrix(document.left(), document.bottomMargin() - 30);
            cb.showText(lineOnBottom);
            cb.endText();
            //This is for Page X Of Y
            String text = "Page " + writer.getPageNumber() + " of ";
            cb.beginText();
            cb.setFontAndSize(bfHelvNormal, 8);
            cb.setTextMatrix(document.right() - 300, document.bottomMargin() - 50);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(totalNoOfPages, document.right() - 300 + helv.getWidthPoint(text, 8), document.bottomMargin() - 50);
            cb.restoreState();
        } catch (Exception e) {
            log.info("On End Page of ArStatementReport failed :- ",e);
        }
    }

    //This is for caluculating total number of pages.
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        BaseFont bfHelvNormal = null;
        try {
            bfHelvNormal = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            totalNoOfPages.beginText();
            totalNoOfPages.setFontAndSize(bfHelvNormal, 8);//helv
            totalNoOfPages.setTextMatrix(0, 0);
            totalNoOfPages.showText(String.valueOf(document.getPageNumber() - 1));
            totalNoOfPages.endText();
        } catch (Exception e) {
            log.info("On Close ArStatementReport failed :- ",e);
        }
    }

    private void doExit() {
        try {
            document.close();
        } catch (Exception e) {
            log.info("Exit ArStatement failed :- ",e);
        }
    }

    public String createStatement(String fileName, String contextPath,String fromPeriod,String toPeriod,Integer year,String budgetSet) {
        try {
            this.doInit(fileName);
            this.writeDocument(contextPath, fromPeriod, toPeriod, year,budgetSet);
            this.doExit();
            return fileName;
        } catch (Exception e) {
            log.info("createStatement failed :- ",e);
        }
        return null;
    }
}
