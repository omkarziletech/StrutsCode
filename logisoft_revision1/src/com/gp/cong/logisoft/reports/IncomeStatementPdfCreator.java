package com.gp.cong.logisoft.reports;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;

import com.gp.cong.logisoft.reports.dto.IncomeStatementReportDTO;
import com.gp.cong.logisoft.reports.dto.IncomeStatementRevenueDTO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.util.Date;

import org.apache.log4j.Logger;

public class IncomeStatementPdfCreator {

    private static final Logger log = Logger.getLogger(IncomeStatementPdfCreator.class);
    Font blackBoldFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont = new Font(Font.HELVETICA, 9, Font.BOLD, Color.BLACK);
    Font headingFont1 = new Font(Font.BOLD, 18, 0, Color.BLACK);
    Font blackBoldHeadingFont = new Font(Font.HELVETICA, 7, Font.BOLD, Color.BLACK);
    Font blackFont = new Font(Font.HELVETICA, 9, 0, Color.BLACK);
    float[] twoColumnDefinitionSize = {50F, 50F};
    float[] threeColumnDefinitionSize = {33.33F, 33.33F, 33.33F};
    float[] fourColumnDefinitionSize = {26F, 29F, 15F, 30F};
    Document document = null;
    //BL Report Object
    //BLReportDTO  blReportDTO = null;

    public void initialize(String fileName) throws FileNotFoundException, DocumentException {
	document = new Document(PageSize.A4.rotate());
	document.setMargins(10, 10, 10, 10);
	PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	document.open();
	PdfAction action = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ, -1, -1, 0.75f), pdfWriter);
	pdfWriter.setOpenAction(action);
    }

    public void createBody(IncomeStatementReportDTO incomeStatementReportDTO, String contextPath) throws DocumentException, MalformedURLException, IOException, Exception {
	NumberFormat numberFormat = new DecimalFormat("##,###,##0.00");
	//start of image
	String imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
	Image img = Image.getInstance(contextPath + imagePath);
	PdfPTable table = new PdfPTable(1);
	table.setWidthPercentage(100);
	img.scalePercent(50);
	PdfPCell celL = new PdfPCell();
	celL.addElement(new Chunk(img, -0, -30));
	celL.setBorder(0);
	celL.setHorizontalAlignment(Element.ALIGN_CENTER);
	// celL.setVerticalAlignment(Element.ALIGN_LEFT);
	table.addCell(celL);
	//This is for printing company name
	PdfPTable companyName = new PdfPTable(1);
	companyName.setWidthPercentage(100);
	companyName.addCell(makeCellLeftNoBorder("Company Name: " + incomeStatementReportDTO.getCompanyName()));

	//Image table
	PdfPCell cell;
	PdfPTable bookTable = new PdfPTable(2);
	bookTable.setWidthPercentage(100);
	bookTable.setWidths(new float[]{50, 50});
	bookTable.getDefaultCell().setPadding(0);
	bookTable.getDefaultCell().setBorderWidth(0.5f);
	bookTable.getDefaultCell().setBorderWidthLeft(0.0f);
	bookTable.getDefaultCell().setBorderWidthRight(0.0f);

	bookTable.addCell(makeCellCenterNoBorder(""));
	bookTable.addCell(makeCellCenterNoBorder(""));
	bookTable.addCell(makeCellCenterNoBorder(""));
	bookTable.addCell(makeCellCenterNoBorder(""));

	//Heading table
	String heading = "Statement of Earnings";
	Phrase headingPhrase = new Phrase(heading, headingFont1);
	PdfPCell headingCell = new PdfPCell(headingPhrase);
	headingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	headingCell.setVerticalAlignment(Element.ALIGN_TOP);
	//  headingCell.setPadding(100);
	headingCell.setPaddingTop(-2);
	headingCell.setPaddingBottom(2);
	headingCell.setBorder(0);
	headingCell.setBackgroundColor(Color.LIGHT_GRAY);
	headingCell.setColspan(2);
	bookTable.addCell(headingCell);
	//end of heading
	//empty table
	PdfPTable Empty = makeTable(1);
	PdfPCell Emptycell = makeCellCenterNoBorder("");
	Empty.addCell(Emptycell);

	//period table
	PdfPTable periodtable = new PdfPTable(3);
	periodtable.setWidthPercentage(100);
	periodtable.setWidths(new float[]{33, 33, 33});

	periodtable.addCell(makeCellCenterNoBorder("BeginningPeriod :" + incomeStatementReportDTO.getBeginningPeriod()));
	periodtable.addCell(makeCellCenterNoBorder("EndPeriod :" + incomeStatementReportDTO.getEndPeriod()));
	periodtable.addCell(makeCellCenterNoBorder("Year :" + incomeStatementReportDTO.getYear()));

	//Current and Previous year table
	PdfPTable yearTable = makeTable(7);
	yearTable.setWidthPercentage(100);
	yearTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});

	yearTable.addCell(makeCellLeftNoColor(""));
	yearTable.addCell(makeCellCenterRightAndBottomWithBackground("Current Year"));
	yearTable.addCell(makeCellCenterRightAndBottomWithBackground("Previous Year"));
	yearTable.addCell(makeCellCenterRightAndBottomWithBackground("Current Month"));
	yearTable.addCell(makeCellCenterRightAndBottomWithBackground("Prior Year"));
	yearTable.addCell(makeCellCenterRightAndBottomWithBackground("Budget YTD"));
	yearTable.addCell(makeCellCenterRightAndBottomWithBackground("Annual Budget"));

	//SalesRevenue table
	PdfPTable salesTable = makeTable(7);
	salesTable.setWidthPercentage(100);
	salesTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});

	salesTable.addCell(makeCellLeft("Sales Revenue"));
	salesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	salesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	salesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	salesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	salesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	salesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));

	for (Iterator iterator = incomeStatementReportDTO.getSalesRevenue().iterator(); iterator.hasNext();) {
	    IncomeStatementRevenueDTO incomeStatementRevenueDTO = (IncomeStatementRevenueDTO) iterator.next();
	    salesTable.addCell(makeCellCenter1(incomeStatementRevenueDTO.getAccountNumber()));
	    if (incomeStatementRevenueDTO.getAmount() != null) {
		salesTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementRevenueDTO.getAmount())));
	    } else {
		salesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    }
	    if (incomeStatementRevenueDTO.getPrevYearAmount() != null) {
		salesTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementRevenueDTO.getPrevYearAmount())));
	    } else {
		salesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    }
	    salesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    salesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    salesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    salesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	}

	//TotalSalesRevenue table
	PdfPTable totalTable = makeTable(7);
	totalTable.setWidthPercentage(100);
	totalTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});

	//totalTable.addCell(makeCellLeft("Total Sales Revenue"));
	totalTable.addCell(makeCellCenter1(""));
	totalTable.addCell(makeCellCenterForDoubleWithColor(Math.round(incomeStatementReportDTO.getCurrentYearSalesRevenueTotal())));
	totalTable.addCell(makeCellCenterForDoubleWithColor(Math.round(incomeStatementReportDTO.getPreviousYearSalesRevenueTotal())));
	totalTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	totalTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	totalTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	totalTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));

	//CostofGoods table
	PdfPTable costTable = makeTable(7);
	costTable.setWidthPercentage(100);
	costTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});

	costTable.addCell(makeCellLeft("Cost of Sales"));
	costTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	costTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	costTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	costTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	costTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	costTable.addCell(makeCellCenterRightAndBottomWithBackground(""));

	for (Iterator iterator = incomeStatementReportDTO.getCostOfGoodsSoldRevenue().iterator(); iterator.hasNext();) {
	    IncomeStatementRevenueDTO incomeStatementRevenueDTO = (IncomeStatementRevenueDTO) iterator.next();
	    costTable.addCell(makeCellCenter1(incomeStatementRevenueDTO.getAccountNumber()));
	    if (incomeStatementRevenueDTO.getAmount() != null) {
		Long currentYearAmount = Math.round(incomeStatementRevenueDTO.getAmount());
		String currentAmount = currentYearAmount.toString();
		if (currentYearAmount < 0) {
		    currentAmount = "(" + currentAmount + ")";
		}

		costTable.addCell(makeCellCenterForDouble(currentAmount));
	    } else {
		costTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    }
	    if (incomeStatementRevenueDTO.getPrevYearAmount() != null) {
		Long prevYearAmount = Math.round(incomeStatementRevenueDTO.getPrevYearAmount());
		String prevAmount = prevYearAmount.toString();
		if (prevYearAmount < 0) {
		    prevAmount = "(" + prevAmount + ")";
		}
		costTable.addCell(makeCellCenterForDouble(prevAmount));
	    } else {
		costTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    }
	    costTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    costTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    costTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    costTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	}
	/*costTable.addCell(makeCellLeft("Expense Group Subtotal"));
	 costTable.addCell( makeCellLeftForDouble(Math.round(incomeStatementReportDTO.getCurrentYearCostOfGoodsSold())));
	 costTable.addCell( makeCellLeftForDouble(Math.round(incomeStatementReportDTO.getPreviousYearCostOfGoodsSold())));
	 costTable.addCell( makeCellLeftForDouble(Math.round(0.0)));
	 costTable.addCell( makeCellLeftForDouble(Math.round(0.0)));
	 costTable.addCell( makeCellLeftForDouble(Math.round(0.0)));
	 costTable.addCell( makeCellLeftForDouble(Math.round(0.0)));*/

	for (Iterator iterator = incomeStatementReportDTO.getCostOfGoodsSoldRevenueGroup2().iterator(); iterator.hasNext();) {
	    IncomeStatementRevenueDTO incomeStatementRevenueDTO = (IncomeStatementRevenueDTO) iterator.next();
	    costTable.addCell(makeCellCenter1(incomeStatementRevenueDTO.getAccountNumber()));
	    if (incomeStatementRevenueDTO.getAmount() != null) {
		costTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementRevenueDTO.getAmount())));
	    } else {
		costTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    }
	    if (incomeStatementRevenueDTO.getPrevYearAmount() != null) {
		costTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementRevenueDTO.getPrevYearAmount())));
	    } else {
		costTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    }
	    costTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    costTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    costTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    costTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	}

	costTable.addCell(makeCellLeft("Expense Group Subtotal"));
	costTable.addCell(makeCellLeftForDouble(Math.round(incomeStatementReportDTO.getCurrentYearCostOfGoodsSoldGroup2())));
	costTable.addCell(makeCellLeftForDouble(Math.round(incomeStatementReportDTO.getPreviousYearCostOfGoodsSoldGroup2())));

	//TotalCostofGoodsSold table
	PdfPTable totalCostTable = makeTable(7);
	totalCostTable.setWidthPercentage(100);
	totalCostTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});

	//totalCostTable.addCell(makeCellLeft("Total Cost of Goods Sold"));
	totalCostTable.addCell(makeCellCenter1(""));
	totalCostTable.addCell(makeCellCenterForDoubleWithColor(Math.round(incomeStatementReportDTO.getCurrentYearCostOfGoodsSold())));
	totalCostTable.addCell(makeCellCenterForDoubleWithColor(Math.round(incomeStatementReportDTO.getPreviousYearCostOfGoodsSold())));
	totalCostTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	totalCostTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	totalCostTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	totalCostTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));

	//Gross profit table
	PdfPTable grossProfitTable = new PdfPTable(7);
	grossProfitTable.setWidthPercentage(100);
	grossProfitTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});

	grossProfitTable.addCell(makeCellLeftNoColor("GrossProfit(Loss)"));
	grossProfitTable.addCell(makeCellCenterForDoubleWithColor(Math.round(incomeStatementReportDTO.getCurrentYearGrossProfit())));
	grossProfitTable.addCell(makeCellCenterForDoubleWithColor(Math.round(incomeStatementReportDTO.getPreviousYearGrossProfit())));
	grossProfitTable.addCell(makeCellLeftNoColor("Gross Margin"));
	grossProfitTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	grossProfitTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	grossProfitTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));

	if (incomeStatementReportDTO.getCurrentYearGrossMarginPercentage() != null) {
	    grossProfitTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getCurrentYearGrossMarginPercentage())));
	} else {
	    grossProfitTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	}
	if (incomeStatementReportDTO.getPreviousYearGrossMarginPercentage() != null) {
	    grossProfitTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearGrossMarginPercentage())));
	} else {
	    grossProfitTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	}
	grossProfitTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	grossProfitTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	grossProfitTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	grossProfitTable.addCell(makeCellCenterForDouble(Math.round(0.0)));

	//Operating Expenses table
	PdfPTable operatingExpensesTable = makeTable(7);
	operatingExpensesTable.setWidthPercentage(100);
	operatingExpensesTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});

	operatingExpensesTable.addCell(makeCellLeft("Operating Expenses"));
	operatingExpensesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	operatingExpensesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	operatingExpensesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	operatingExpensesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	operatingExpensesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	operatingExpensesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));

	/*operatingExpensesTable.addCell(makeCellLeft("Expense Group3"));
	 operatingExpensesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	 operatingExpensesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	 operatingExpensesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	 operatingExpensesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	 operatingExpensesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	 operatingExpensesTable.addCell(makeCellCenterRightAndBottomWithBackground(""));*/
	/*operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0incomeStatementReportDTO.getCurrentYearExpenseGroup3Sum())));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0incomeStatementReportDTO.getPreviousYearExpenseGroup3Sum())));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));*/

	//current year expences Group 3 Values
	for (Iterator iterator = incomeStatementReportDTO.getCurrentYearExpenseGroup3Account().iterator(); iterator.hasNext();) {
	    Object[] incomeStatementRevenueDTO = (Object[]) iterator.next();
	    operatingExpensesTable.addCell(makeCellCenter1(null != incomeStatementRevenueDTO[1] ? incomeStatementRevenueDTO[1].toString() : ""));
	    operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(null != incomeStatementRevenueDTO[0] ? Double.parseDouble(incomeStatementRevenueDTO[0].toString()) : 0d)));
	    operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	}
	//previous year expences group 3 values
	for (Iterator iterator = incomeStatementReportDTO.getPreviousYearExpenseGroup3Account().iterator(); iterator.hasNext();) {
	    Object[] incomeStatementRevenueDTO = (Object[]) iterator.next();
	    operatingExpensesTable.addCell(makeCellCenter1(null != incomeStatementRevenueDTO[1] ? incomeStatementRevenueDTO[1].toString() : ""));
	    operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(null != incomeStatementRevenueDTO[0] ? Double.parseDouble(incomeStatementRevenueDTO[0].toString()) : 0d)));
	    operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	}
	operatingExpensesTable.addCell(makeCellCenter1(""));
	operatingExpensesTable.addCell(makeCellCenterForDoubleWithColor(Math.round(incomeStatementReportDTO.getCurrentYearExpenseGroup3Sum())));
	operatingExpensesTable.addCell(makeCellCenterForDoubleWithColor(Math.round(incomeStatementReportDTO.getPreviousYearExpenseGroup3Sum())));
	operatingExpensesTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	operatingExpensesTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	operatingExpensesTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	operatingExpensesTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	/*operatingExpensesTable.addCell(makeCellLeft("Expense Group4"));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getCurrentYearExpenseGroup4Sum())));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearExpenseGroup4Sum())));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));*/

	/*	operatingExpensesTable.addCell(makeCellLeft("Expense Group5"));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getCurrentYearExpenseGroup5Sum())));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearExpenseGroup5Sum())));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));

	 operatingExpensesTable.addCell(makeCellCenter1("Other Operating Expenses"));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getCurrentYearOtherOperatingExpenses())));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearOtherOperatingExpenses())));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingExpensesTable.addCell(makeCellCenterForDouble(Math.round(0.0)));*/

	//Total operating Expenses
	PdfPTable totalExpensesTable = makeTable(7);
	totalExpensesTable.setWidthPercentage(100);
	totalExpensesTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});

	/*	totalExpensesTable.addCell(makeCellLeft("Total Operating Expenses "));
	 totalExpensesTable.addCell( makeCellLeftForDoubleTopBorder(Math.round(incomeStatementReportDTO.getCurrentYearTotalOperatingExpenses())));
	 totalExpensesTable.addCell( makeCellLeftForDoubleTopBorder(Math.round(incomeStatementReportDTO.getPreviousYearTotalOperatingExpenses())));
	 totalExpensesTable.addCell( makeCellLeftForDoubleTopBorder(Math.round(0.0)));
	 totalExpensesTable.addCell( makeCellLeftForDoubleTopBorder(Math.round(0.0)));
	 totalExpensesTable.addCell( makeCellLeftForDoubleTopBorder(Math.round(0.0)));
	 totalExpensesTable.addCell( makeCellLeftForDoubleTopBorder(Math.round(0.0)));*/
	//Total Earnings (Loss) from Operations
	totalExpensesTable.addCell(makeCellLeft("Earnings (Loss) from Operations"));
	totalExpensesTable.addCell(makeCellCenterForDoubleWithColor(Math.round(incomeStatementReportDTO.getCurrentEarningOperations())));
	totalExpensesTable.addCell(makeCellCenterForDoubleWithColor(Math.round(incomeStatementReportDTO.getPreviousEarningOperations())));
	totalExpensesTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	totalExpensesTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	totalExpensesTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	totalExpensesTable.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	//Operating Income table
	PdfPTable incomeTable = new PdfPTable(7);
	incomeTable.setWidthPercentage(100);
	incomeTable.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});
	//Added By Nagendra
	incomeTable.addCell(makeCellLeft("Other Income and Expenses"));
	incomeTable.addCell(makeCellCenterForDStrinBorderBootom(""));
	incomeTable.addCell(makeCellCenterForDStrinBorderBootom(""));
	incomeTable.addCell(makeCellCenterForDStrinBorderBootom(""));
	incomeTable.addCell(makeCellCenterForDStrinBorderBootom(""));
	incomeTable.addCell(makeCellCenterForDStrinBorderBootom(""));
	incomeTable.addCell(makeCellCenterForDStrinBorderBootom(""));

	//Income Group 2
				/*incomeTable.addCell(makeCellLeft("Income Group 2"));
	 incomeTable.addCell(makeCellLeft(""));
	 incomeTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	 incomeTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	 incomeTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	 incomeTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	 incomeTable.addCell(makeCellCenterRightAndBottomWithBackground(""));
	 incomeTable.addCell(makeCellCenterRightAndBottomWithBackground(""));*/
	//Current Income Group 2 Values
	for (Iterator iterator = incomeStatementReportDTO.getCurrentYearIncomeGroup2Account().iterator(); iterator.hasNext();) {
	    Object[] incomeStatementRevenueDTO = (Object[]) iterator.next();
	    incomeTable.addCell(makeCellCenter1(null != incomeStatementRevenueDTO[1] ? incomeStatementRevenueDTO[1].toString() : ""));
	    incomeTable.addCell(makeCellCenterForDouble(Math.round(null != incomeStatementRevenueDTO[0] ? Double.parseDouble(incomeStatementRevenueDTO[0].toString()) : 0d)));
	    incomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    incomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    incomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    incomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    incomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	}
	//previous Income Group 2 Values
	for (Iterator iterator = incomeStatementReportDTO.getPreviousYearIncomeGroup2Account().iterator(); iterator.hasNext();) {
	    Object[] incomeStatementRevenueDTO = (Object[]) iterator.next();
	    incomeTable.addCell(makeCellCenter1(null != incomeStatementRevenueDTO[1] ? incomeStatementRevenueDTO[1].toString() : ""));
	    incomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    incomeTable.addCell(makeCellCenterForDouble(Math.round(null != incomeStatementRevenueDTO[0] ? Double.parseDouble(incomeStatementRevenueDTO[0].toString()) : 0d)));
	    incomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    incomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    incomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    incomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	}

	/*	incomeTable.addCell(makeCellLeft("Other Income Total"));
	 incomeTable.addCell(makeCellLeft(""));
	 incomeTable.addCell(makeCellLeftForDoubleTopBorder(Math.round(null!=incomeStatementReportDTO.getCurrentOtherIncome2Total() ? incomeStatementReportDTO.getCurrentOtherIncome2Total() : 0d)));
	 incomeTable.addCell(makeCellLeftForDoubleTopBorder(Math.round(null != incomeStatementReportDTO.getPreviousOtherIncome2Total() ? incomeStatementReportDTO.getPreviousOtherIncome2Total() : 0d)));
	 incomeTable.addCell(makeCellLeftForDoubleTopBorder(Math.round(0.0)));
	 incomeTable.addCell(makeCellLeftForDoubleTopBorder(Math.round(0.0)));
	 incomeTable.addCell(makeCellLeftForDoubleTopBorder(Math.round(0.0)));
	 incomeTable.addCell(makeCellLeftForDoubleTopBorder(Math.round(0.0)));*/

	PdfPTable operatingExpensesTableValues = makeTable(7);
	operatingExpensesTableValues.setWidthPercentage(100);
	operatingExpensesTableValues.setWidths(new float[]{40, 10, 10, 10, 10, 10, 10});
	//Expences group 4
				/*operatingExpensesTableValues.addCell(makeCellLeft("Expense Group4"));
	 operatingExpensesTableValues.addCell(makeCellLeft(""));
	 operatingExpensesTableValues.addCell(makeCellLeftForDoubleTopBorder(""));
	 operatingExpensesTableValues.addCell(makeCellLeftForDoubleTopBorder(""));
	 operatingExpensesTableValues.addCell(makeCellLeftForDoubleTopBorder(""));
	 operatingExpensesTableValues.addCell(makeCellLeftForDoubleTopBorder(""));
	 operatingExpensesTableValues.addCell(makeCellLeftForDoubleTopBorder(""));
	 operatingExpensesTableValues.addCell(makeCellLeftForDoubleTopBorder(""));*/

	//current year expences Group 4 Values
	for (Iterator iterator = incomeStatementReportDTO.getCurrentYearExpenseGroup4Account().iterator(); iterator.hasNext();) {
	    Object[] incomeStatementRevenueDTO = (Object[]) iterator.next();
	    operatingExpensesTableValues.addCell(makeCellCenter1(null != incomeStatementRevenueDTO[1] ? incomeStatementRevenueDTO[1].toString() : ""));
	    operatingExpensesTableValues.addCell(makeCellCenterForDouble(Math.round(null != incomeStatementRevenueDTO[0] ? Double.parseDouble(incomeStatementRevenueDTO[0].toString()) : 0d)));
	    operatingExpensesTableValues.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTableValues.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTableValues.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTableValues.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTableValues.addCell(makeCellCenterForDouble(Math.round(0.0)));

	}
	//previous year expences Group 4 Values
	for (Iterator iterator = incomeStatementReportDTO.getPreviousYearExpenseGroup4Account().iterator(); iterator.hasNext();) {
	    Object[] incomeStatementRevenueDTO = (Object[]) iterator.next();
	    operatingExpensesTableValues.addCell(makeCellCenter1(null != incomeStatementRevenueDTO[1] ? incomeStatementRevenueDTO[1].toString() : ""));
	    operatingExpensesTableValues.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTableValues.addCell(makeCellCenterForDouble(Math.round(null != incomeStatementRevenueDTO[0] ? Double.parseDouble(incomeStatementRevenueDTO[0].toString()) : 0d)));
	    operatingExpensesTableValues.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTableValues.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTableValues.addCell(makeCellCenterForDouble(Math.round(0.0)));
	    operatingExpensesTableValues.addCell(makeCellCenterForDouble(Math.round(0.0)));
	}
	/*operatingExpensesTableValues.addCell(makeCellLeft("Other Expense Total"));*/
	operatingExpensesTableValues.addCell(makeCellLeft(""));
	operatingExpensesTableValues.addCell(makeCellCenterForDoubleWithColor(Math.round(null != incomeStatementReportDTO.getCurrentOtherExpense4Total() ? incomeStatementReportDTO.getCurrentOtherExpense4Total() : 0d)));
	operatingExpensesTableValues.addCell(makeCellCenterForDoubleWithColor(Math.round(null != incomeStatementReportDTO.getPreviousOtherExpense4Total() ? incomeStatementReportDTO.getPreviousOtherExpense4Total() : 0d)));
	operatingExpensesTableValues.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	operatingExpensesTableValues.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	operatingExpensesTableValues.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	operatingExpensesTableValues.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));

	//Other Income And Expenses Total
				/*operatingExpensesTableValues.addCell(makeCellLeft("Other Income And Expenses Total"));
	 operatingExpensesTableValues.addCell(makeCellLeftForDoubleTopBorder(Math.round(incomeStatementReportDTO.getCurrentOtherIncomeAndExpenseTotal())));
	 operatingExpensesTableValues.addCell(makeCellLeftForDoubleTopBorder(Math.round(incomeStatementReportDTO.getPreviousOtherIncomeAndExpenseTotal())));
	 operatingExpensesTableValues.addCell(makeCellLeftForDoubleTopBorder(Math.round(0.0)));
	 operatingExpensesTableValues.addCell(makeCellLeftForDoubleTopBorder(Math.round(0.0)));
	 operatingExpensesTableValues.addCell(makeCellLeftForDoubleTopBorder(Math.round(0.0)));
	 operatingExpensesTableValues.addCell(makeCellLeftForDoubleTopBorder(Math.round(0.0)));*/

	//Net Earnings
	operatingExpensesTableValues.addCell(makeCellLeft("Net Earnings (Loss)"));
	operatingExpensesTableValues.addCell(makeCellCenterForDoubleWithColor(Math.round(incomeStatementReportDTO.getCurrentNetEarnings())));
	operatingExpensesTableValues.addCell(makeCellCenterForDoubleWithColor(Math.round(incomeStatementReportDTO.getPreviousNetEarnings())));
	operatingExpensesTableValues.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	operatingExpensesTableValues.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	operatingExpensesTableValues.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));
	operatingExpensesTableValues.addCell(makeCellCenterForDoubleWithColor(Math.round(0.0)));

	//Operating Income Table
	/*PdfPTable operatingIncomeTable=makeTable(7);
	 operatingIncomeTable.setWidthPercentage(100);
	 operatingIncomeTable.setWidths(new float[]{40,10,10,10,10,10,10});

	 operatingIncomeTable.addCell(makeCellLeftNoColor("Operating Income"));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getCurrentYearOperatingIncome())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearOperatingIncome())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellLeftNoColor("Operating Margin"));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getCurrentYearOperatingIncomeMargin())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearOperatingIncomeMargin())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellLeftNoColor("Interest Paid"));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getCurrentYearInterestPaid())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearInterestPaid())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellLeftNoColor("Income Before Taxes"));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getCurrentYearIncomeBeforeTaxes())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearIncomeBeforeTaxes())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellLeftNoColor("Taxes"));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getCurrentYearTaxes())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearTaxes())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellLeftNoColor("Net Income from Continuing Operations"));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearNetIncomeFromContinuingOperations())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearNetIncomeFromContinuingOperations())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellLeftNoColor("Profit Margin"));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellLeftNoColor("Non-recurring Events"));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getCurrentYearNonRecurringEvents())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearNonRecurringEvents())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellLeftNoColor("Net Income"));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getCurrentYearNetIncome())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearNetIncome())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellLeftNoColor("Dividents to Stock Holders"));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getCurrentYearDividendToStockholders())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearDividendToStockholders())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellLeftNoColor("Net Income Available to Share Holders"));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getCurrentYearNetIncomeAvailableToStockholders())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(incomeStatementReportDTO.getPreviousYearNetIncomeAvailableToStockholders())));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 operatingIncomeTable.addCell(makeCellCenterForDouble(Math.round(0.0)));
	 */
	document.add(table);
	document.add(Empty);
	document.add(Empty);
	document.add(Empty);
	document.add(Empty);
	document.add(Empty);
	document.add(Empty);
	document.add(Empty);
	document.add(Empty);
	document.add(companyName);

	document.add(bookTable);
	document.add(Empty);

	document.add(periodtable);
	document.add(Empty);
	document.add(Empty);

	document.add(yearTable);
	document.add(Empty);

	document.add(salesTable);
	document.add(totalTable);
	//document.add(Empty);

	document.add(costTable);
	document.add(totalCostTable);
	document.add(Empty);

	document.add(grossProfitTable);
	document.add(Empty);
	document.add(operatingExpensesTable);
	document.add(totalExpensesTable);
	document.add(Empty);
	document.add(incomeTable);
	document.add(operatingExpensesTableValues);

    }

    public void destroy() {
	document.close();
    }

    private PdfPTable makeTable(int rows) {
	PdfPTable table = new PdfPTable(rows);
	table.getDefaultCell().setBorder(0);
	table.getDefaultCell().setBorderWidth(0);
	return table;
    }

    private PdfPCell makeCell(Phrase phrase, int alignment) {
	PdfPCell cell = new PdfPCell(phrase);
	cell.setHorizontalAlignment(alignment);
	cell.setBorder(0);
	return cell;
    }

    private PdfPCell makeTableCell(PdfPTable table) {
	PdfPCell cell = new PdfPCell(table);
	cell.setBorderWidth(0f);
	cell.setBorderWidthBottom(0.5f);
	cell.setPadding(0f);
	return cell;
    }
    /*	private PdfPCell makeCellCenter4(String text){
     Phrase phrase = new Phrase(text, blackFont);
     PdfPCell cell = makeCell(phrase,Element.ALIGN_CENTER);
     cell.setBackgroundColor(Color.LIGHT_GRAY);
     cell.setBorderWidthRight(0.1f);
     cell.setBorderWidthBottom(0.1f);
     return cell;
     }*/

    private PdfPCell makeCellLeft(String text) {
	Phrase phrase = new Phrase(text, headingFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
	cell.setBorderWidthRight(0.1f);
	cell.setBorderWidthBottom(0.1f);
	cell.setBackgroundColor(Color.LIGHT_GRAY);
	return cell;
    }

    private PdfPCell makeCellLeftNoColor(String text) {
	Phrase phrase = new Phrase(text, headingFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
	cell.setBorderWidthRight(0.1f);
	cell.setBorderWidthBottom(0.1f);
	return cell;
    }

    private PdfPCell makeCellLeftForDouble(Long value) {
	String value1 = String.valueOf(value);
	Phrase phrase = new Phrase(value1, blackFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
	cell.setBorderWidthRight(0.5f);
	cell.setBackgroundColor(Color.LIGHT_GRAY);
	return cell;
    }

    private PdfPCell makeCellLeftForDoubleTopBorder(Long value) {
	String value1 = String.valueOf(value);
	Phrase phrase = new Phrase(value1, blackFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
	cell.setBorderWidthRight(0.5f);
	cell.setBorderWidthTop(0.5f);
	cell.setBackgroundColor(Color.LIGHT_GRAY);
	return cell;
    }
    /*private PdfPCell makeCellLeftForDoubleTopBorder(String value){
     String value1=String.valueOf(value);
     Phrase phrase = new Phrase(value1, blackFont);
     PdfPCell cell = makeCell(phrase,Element.ALIGN_RIGHT);
     cell.setBorderWidthRight(0.5f);
     cell.setBorderWidthTop(0.5f);
     cell.setBackgroundColor(Color.LIGHT_GRAY);
     return cell;
     }*/
    /*private PdfPCell makeCellLeftForDouble(String value){
     String value1=String.valueOf(value);
     Phrase phrase = new Phrase(value1, blackFont);
     PdfPCell cell = makeCell(phrase,Element.ALIGN_RIGHT);
     cell.setBorderWidthRight(0.5f);
     cell.setBackgroundColor(Color.LIGHT_GRAY);
     return cell;
     }*/

    private PdfPCell makeCellLeftNoBorder(String text) {
	Phrase phrase = new Phrase(text, blackBoldFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
	cell.setBorderWidthRight(0.0f);
	return cell;
    }

    private PdfPCell makeCellCenter1(String text) {
	Phrase phrase = new Phrase(text, blackFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_LEFT);
	cell.setBorderWidthRight(0.1f);
	cell.setBorderWidthBottom(0.1f);
	return cell;
    }
    /*private PdfPCell makeCellRight(String text){
     Phrase phrase = new Phrase(text, blackBoldFont);
     return makeCell(phrase,Element.ALIGN_RIGHT);
     }*/

    private PdfPCell makeCellCenter(String text) {
	Phrase phrase = new Phrase(text, blackBoldFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
	cell.setBorderWidthLeft(0.5f);
	return cell;
    }

    private PdfPCell makeCellCenterNoBorder(String text) {
	PdfPCell cell = makeCellCenter(text);
	cell.setBorderWidthLeft(0f);
	return cell;
    }

    private PdfPCell makeCellCenterRightAndBottomWithBackground(String text) {
	Phrase phrase = new Phrase(text, headingFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_CENTER);
	cell.setBackgroundColor(Color.LIGHT_GRAY);
	cell.setBorderWidthRight(0.1f);
	cell.setBorderWidthBottom(0.1f);
	return cell;
    }

    private PdfPCell makeCellCenterForDouble(Long value) {
	String value1 = String.valueOf(value);
	Phrase phrase = new Phrase(value1, blackFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
	cell.setBorderWidthRight(0.1f);
	cell.setBorderWidthBottom(0.1f);
	return cell;
    }
    /*private PdfPCell makeCellCenterForDoubleWithBackGroundColor(Long value){
     String value1=String.valueOf(value);
     Phrase phrase = new Phrase(value1,blackFont);
     PdfPCell cell = makeCell(phrase,Element.ALIGN_RIGHT);
     cell.setBorderWidthRight(0.1f);
     cell.setBorderWidthBottom(0.1f);
     return cell;
     }*/

    private PdfPCell makeCellCenterForDouble(String value) {
	String value1 = String.valueOf(value);
	Phrase phrase = new Phrase(value1, blackFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
	cell.setBorderWidthRight(0.1f);
	cell.setBorderWidthBottom(0.1f);
	return cell;
    }

    private PdfPCell makeCellCenterForDoubleWithColor(Long value) {
	String value1 = String.valueOf(value);
	Phrase phrase = new Phrase(value1, blackFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
	cell.setBackgroundColor(Color.LIGHT_GRAY);
	cell.setBorderWidthRight(0.1f);
	cell.setBorderWidthBottom(0.1f);
	return cell;
    }

    private PdfPCell makeCellCenterForDStrinBorderBootom(String value) {
	String value1 = String.valueOf(value);
	Phrase phrase = new Phrase(value1, blackFont);
	PdfPCell cell = makeCell(phrase, Element.ALIGN_RIGHT);
	cell.setBackgroundColor(Color.LIGHT_GRAY);
	cell.setBorderWidthRight(0.1f);
	cell.setBorderWidthBottom(0.1f);
	return cell;
    }

    public String createReport(IncomeStatementReportDTO incomeStatementReportDTO, String fileName, String contextPath) {
	try {
	    this.initialize(fileName);
	    this.createBody(incomeStatementReportDTO, contextPath);
	    this.destroy();
	} catch (Exception e) {
	    log.info("createReport failed on " + new Date(),e);
	}
	return "fileName";
    }
}
