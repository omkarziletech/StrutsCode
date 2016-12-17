package com.gp.cong.logisoft.ExcelGenerator;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.CustomerStatementForm;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

/**
 * @description
 * ExportArStatementToExcel
 *
 * @author
 * LakshmiNarayanan
 */
public class ExportArStatementToExcel extends BaseExcelGenerator {

    private static Logger log = Logger.getLogger(ExportArStatementToExcel.class);

    private void generateExcelSheet(List<AccountingBean> transactions, CustomerBean customer, Map<String, CustomerBean> agingBuckets, boolean isEcuLinReport) throws IOException {
	ByteArrayOutputStream baos = null;
	try {
	    log.info("ArStatement Export To Excel writing contents started :- ");
	    writableSheet = writableWorkbook.createSheet("AR Statement", 0);
	    WritableFont font16 = createFont(new WritableFont(WritableFont.ARIAL), 16, BOLD_STYLE_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.AUTOMATIC);
	    WritableCellFormat boldCellLeft = createCellFormat(wfBold, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.LEFT);
	    WritableCellFormat boldCellRight = createCellFormat(wfBold, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.RIGHT);
	    WritableCellFormat noBoldCellCenter = createCellFormat(wfNoBold, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.CENTRE);
	    WritableCellFormat noBorderHeaderCell = createCellFormat(font16, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.CENTRE);
	    WritableCellFormat grayHeaderCell = createCellFormat(font16, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.CENTRE);
	    grayHeaderCell.setBackground(Colour.GREY_40_PERCENT);
	    WritableCellFormat columnHeaderCell = createCellFormat(wfBoldForColumns, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.CENTRE);
	    columnHeaderCell.setBackground(Colour.BLUE_GREY);
	    SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
	    String companyName = systemRulesDAO.getSystemRulesByCode("CompanyName");
	    String companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
	    String companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
	    String companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");
	    int endCol = 9;
	    writableSheet.setColumnView(0, 12);
	    writableSheet.setColumnView(1, 30);
	    for (int col = 2; col <= endCol; col++) {
		writableSheet.setColumnView(col, 20);
	    }
	    int row = 2;
	    writableSheet.setRowView(row, 400);
	    writableSheet.mergeCells(0, row, endCol, row);
	    writableSheet.addCell(new Label(0, row, companyName, noBorderHeaderCell));
	    row++;
	    writableSheet.mergeCells(0, row, endCol, row);
	    StringBuilder addressess = new StringBuilder();
	    addressess.append(companyAddress).append(". PHONE: ").append(companyPhone).append(". FAX: ").append(companyFax);
	    writableSheet.addCell(new Label(0, row, addressess.toString(), noBoldCellCenter));
	    row++;
	    writableSheet.mergeCells(0, row, endCol, row);
	    writableSheet.setRowView(row, 400);
	    writableSheet.addCell(new Label(0, row, "STATEMENT", grayHeaderCell));
	    row++;
	    int headerRow = row;
	    row++;
	    int firstRow = row;
	    row++;
	    int secondRow = row;
	    row++;
	    int thirdRow = row;
	    row++;
	    int fourthRow = row;
	    row++;
	    int fifthRow = row;
	    boolean agentFlag = false;
	    int agingStartCol = 0;
	    if (null != customer) {
		if (StringUtils.contains(customer.getAccountType(), "A")
			|| StringUtils.contains(customer.getAccountType(), "E") || StringUtils.contains(customer.getAccountType(), "I")) {
		    agentFlag = true;
		}
		String customerName = customer.getCustomerName();
		String address = null != customer.getAddress() ? customer.getAddress() : "";
		String customerNumber = "Account#: " + customer.getCustomerNumber();
		writableSheet.mergeCells(0, headerRow, 1, headerRow);
		writableSheet.addCell(new Label(0, headerRow, customerName, boldCellLeft));
		writableSheet.mergeCells(0, firstRow, 1, fourthRow);
		writableSheet.addCell(new Label(0, firstRow, address, noBorderCellBlackLeft));
		writableSheet.setRowView(fifthRow, 300);
		writableSheet.mergeCells(0, fifthRow, 1, fifthRow);
		writableSheet.addCell(new Label(0, fifthRow, customerNumber, boldCellLeft));
		agingStartCol += 2;
	    }
	    CustomerBean apAgingBuckets = agingBuckets.get("apAgingBuckets");
	    double apBalance = 0d;
	    double acBalance = 0d;
	    boolean hasApAging = false;
	    if (null != apAgingBuckets) {
		hasApAging = true;
		String current = "$" + apAgingBuckets.getCurrent();
		String thirtyOneToSixtyDays = "$" + apAgingBuckets.getThirtyOneToSixtyDays();
		String sixtyOneToNinetyDays = "$" + apAgingBuckets.getSixtyOneToNintyDays();
		String greaterThanNinetyDays = "$" + apAgingBuckets.getGreaterThanNintyDays();
		String total = "$" + apAgingBuckets.getTotal();
		writableSheet.addCell(new Label(agingStartCol, headerRow, "AP SUMMARY", boldCellRight));
		writableSheet.addCell(new Label(agingStartCol + 1, headerRow, "AMOUNT", boldCellRight));
		writableSheet.addCell(new Label(agingStartCol, firstRow, "CURRENT", boldCellRight));
		if (current.contains("-")) {
		    writableSheet.addCell(new Label(agingStartCol + 1, firstRow, current.replace("-", ""), noBorderCellRedRight));
		} else {
		    writableSheet.addCell(new Label(agingStartCol + 1, firstRow, current, noBorderCellBlackRight));
		}
		writableSheet.addCell(new Label(agingStartCol, secondRow, "31-60 DAYS", boldCellRight));
		if (thirtyOneToSixtyDays.contains("-")) {
		    writableSheet.addCell(new Label(agingStartCol + 1, secondRow, thirtyOneToSixtyDays.replace("-", ""), noBorderCellRedRight));
		} else {
		    writableSheet.addCell(new Label(agingStartCol + 1, secondRow, thirtyOneToSixtyDays, noBorderCellBlackRight));
		}
		writableSheet.addCell(new Label(agingStartCol, secondRow, "31-60 DAYS", boldCellRight));
		if (thirtyOneToSixtyDays.contains("-")) {
		    writableSheet.addCell(new Label(agingStartCol + 1, secondRow, thirtyOneToSixtyDays.replace("-", ""), noBorderCellRedRight));
		} else {
		    writableSheet.addCell(new Label(agingStartCol + 1, secondRow, thirtyOneToSixtyDays, noBorderCellBlackRight));
		}
		writableSheet.addCell(new Label(agingStartCol, thirdRow, "61-90 DAYS", boldCellRight));
		if (sixtyOneToNinetyDays.contains("-")) {
		    writableSheet.addCell(new Label(agingStartCol + 1, thirdRow, sixtyOneToNinetyDays.replace("-", ""), noBorderCellRedRight));
		} else {
		    writableSheet.addCell(new Label(agingStartCol + 1, thirdRow, sixtyOneToNinetyDays, noBorderCellBlackRight));
		}
		writableSheet.addCell(new Label(agingStartCol, fourthRow, ">90 DAYS", boldCellRight));
		if (greaterThanNinetyDays.contains("-")) {
		    writableSheet.addCell(new Label(agingStartCol + 1, fourthRow, greaterThanNinetyDays.replace("-", ""), noBorderCellRedRight));
		} else {
		    writableSheet.addCell(new Label(agingStartCol + 1, fourthRow, greaterThanNinetyDays, noBorderCellBlackRight));
		}
		writableSheet.addCell(new Label(agingStartCol, fifthRow, "TOTAL", boldCellRight));
		if (total.contains("-")) {
		    writableSheet.addCell(new Label(agingStartCol + 1, fifthRow, total.replace("-", ""), noBorderCellRedRight));
		} else {
		    writableSheet.addCell(new Label(agingStartCol + 1, fifthRow, total, noBorderCellBlackRight));
		}
		apBalance = Double.parseDouble(apAgingBuckets.getOutstandingPayables().replace(",", ""));
		acBalance = Double.parseDouble(apAgingBuckets.getOutstandingAccruals().replace(",", ""));
		agingStartCol += 2;
	    }
	    CustomerBean arAgingBuckets = agingBuckets.get("arAgingBuckets");
	    if (null != arAgingBuckets) {
		String current = "$" + arAgingBuckets.getCurrent();
		String thirtyOneToSixtyDays = "$" + arAgingBuckets.getThirtyOneToSixtyDays();
		String sixtyOneToNinetyDays = "$" + arAgingBuckets.getSixtyOneToNintyDays();
		String greaterThanNinetyDays = "$" + arAgingBuckets.getGreaterThanNintyDays();
		String total = "$" + arAgingBuckets.getTotal();
		writableSheet.addCell(new Label(agingStartCol, headerRow, "AR SUMMARY", boldCellRight));
		writableSheet.addCell(new Label(agingStartCol + 1, headerRow, "AMOUNT", boldCellRight));
		writableSheet.addCell(new Label(agingStartCol, firstRow, "CURRENT", boldCellRight));
		if (current.contains("-")) {
		    writableSheet.addCell(new Label(agingStartCol + 1, firstRow, current.replace("-", ""), noBorderCellRedRight));
		} else {
		    writableSheet.addCell(new Label(agingStartCol + 1, firstRow, current, noBorderCellBlackRight));
		}
		writableSheet.addCell(new Label(agingStartCol, secondRow, "31-60 DAYS", boldCellRight));
		if (thirtyOneToSixtyDays.contains("-")) {
		    writableSheet.addCell(new Label(agingStartCol + 1, secondRow, thirtyOneToSixtyDays.replace("-", ""), noBorderCellRedRight));
		} else {
		    writableSheet.addCell(new Label(agingStartCol + 1, secondRow, thirtyOneToSixtyDays, noBorderCellBlackRight));
		}
		writableSheet.addCell(new Label(agingStartCol, secondRow, "31-60 DAYS", boldCellRight));
		if (thirtyOneToSixtyDays.contains("-")) {
		    writableSheet.addCell(new Label(agingStartCol + 1, secondRow, thirtyOneToSixtyDays.replace("-", ""), noBorderCellRedRight));
		} else {
		    writableSheet.addCell(new Label(agingStartCol + 1, secondRow, thirtyOneToSixtyDays, noBorderCellBlackRight));
		}
		writableSheet.addCell(new Label(agingStartCol, thirdRow, "61-90 DAYS", boldCellRight));
		if (sixtyOneToNinetyDays.contains("-")) {
		    writableSheet.addCell(new Label(agingStartCol + 1, thirdRow, sixtyOneToNinetyDays.replace("-", ""), noBorderCellRedRight));
		} else {
		    writableSheet.addCell(new Label(agingStartCol + 1, thirdRow, sixtyOneToNinetyDays, noBorderCellBlackRight));
		}
		writableSheet.addCell(new Label(agingStartCol, fourthRow, ">90 DAYS", boldCellRight));
		if (greaterThanNinetyDays.contains("-")) {
		    writableSheet.addCell(new Label(agingStartCol + 1, fourthRow, greaterThanNinetyDays.replace("-", ""), noBorderCellRedRight));
		} else {
		    writableSheet.addCell(new Label(agingStartCol + 1, fourthRow, greaterThanNinetyDays, noBorderCellBlackRight));
		}
		writableSheet.addCell(new Label(agingStartCol, fifthRow, "TOTAL", boldCellRight));
		if (total.contains("-")) {
		    writableSheet.addCell(new Label(agingStartCol + 1, fifthRow, total.replace("-", ""), noBorderCellRedRight));
		} else {
		    writableSheet.addCell(new Label(agingStartCol + 1, fifthRow, total, noBorderCellBlackRight));
		}
		if (hasApAging) {
		    double arBalance = Double.parseDouble(arAgingBuckets.getTotal().replace(",", ""));
		    row++;
		    writableSheet.addCell(new Label(agingStartCol, row, "AR-AP", boldCellRight));
		    String netAmt1 = NumberUtils.formatNumber(arBalance + apBalance, "$###,###,##0.00");
		    if (netAmt1.contains("-")) {
			writableSheet.addCell(new Label(agingStartCol + 1, row, netAmt1.replace("-", ""), noBorderCellRedRight));
		    } else {
			writableSheet.addCell(new Label(agingStartCol + 1, row, netAmt1, noBorderCellBlackRight));
		    }
		    row++;
		    writableSheet.addCell(new Label(agingStartCol, row, "AR-AP-AC", boldCellRight));
		    String netAmt2 = NumberUtils.formatNumber(arBalance + apBalance + acBalance, "$###,###,##0.00");
		    if (netAmt2.contains("-")) {
			writableSheet.addCell(new Label(agingStartCol + 1, row, netAmt2.replace("-", ""), noBorderCellRedRight));
		    } else {
			writableSheet.addCell(new Label(agingStartCol + 1, row, netAmt2, noBorderCellBlackRight));
		    }
		}
	    }
	    row++;
	    row++;
	    writableSheet.setRowView(row, 300);
	    writableSheet.mergeCells(0, row, 1, row);
	    writableSheet.addCell(new Label(0, row, "Statement Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), boldCellLeft));
	    row++;
	    writableSheet.setRowView(row, 400);
	    writableSheet.mergeCells(0, row, 9, row);
	    writableSheet.addCell(new Label(0, row, "ACCOUNT DETAIL", grayHeaderCell));
	    row++;
	    WritableCellFormat cellFormat = new WritableCellFormat();
	    if (isEcuLinReport) {
		writableSheet.addCell(new Label(0, row, "Date", columnHeaderCell));
		writableSheet.addCell(new Label(1, row, "Invoice/BL", columnHeaderCell));
		writableSheet.addCell(new Label(2, row, "Booking#", columnHeaderCell));
		writableSheet.setColumnView(3, 40);
		writableSheet.addCell(new Label(3, row, "Customer Reference", columnHeaderCell));
		writableSheet.addCell(new Label(4, row, "Invoice Amount", columnHeaderCell));
		writableSheet.addCell(new Label(5, row, "Approved", columnHeaderCell));
		writableSheet.addCell(new Label(6, row, "Balance", columnHeaderCell));
		writableSheet.addCell(new Label(7, row, "Query Code", columnHeaderCell));
		writableSheet.addCell(new Label(8, row, "Ecu Comments", columnHeaderCell));
		writableSheet.setColumnView(9, 40);
		writableSheet.addCell(new Label(9, row, "Econo Comments", columnHeaderCell));
		double totalInvoiceAmount = 0d;
		cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
		cellFormat.setWrap(true);
		for (AccountingBean transaction : transactions) {
		    row++;
		    writableSheet.addCell(new Label(0, row, transaction.getFormattedDate(), cellFormat));
		    writableSheet.addCell(new Label(1, row, transaction.getInvoiceOrBl(), cellFormat));
		    writableSheet.addCell(new Label(2, row, transaction.getBookingNumber(), cellFormat));
		    writableSheet.addCell(new Label(3, row, transaction.getCustomerReference(), cellFormat));
		    double invoiceAmount = Double.parseDouble(transaction.getFormattedBalance().replaceAll(",", ""));
		    totalInvoiceAmount += invoiceAmount;
		    writableSheet.addCell(new Number(4, row, invoiceAmount, noBorderNumberCellBlackRight));
		    writableSheet.addCell(new Number(5, row, 0d, noBorderNumberCellBlackRight));
		    writableSheet.addCell(new Number(6, row, invoiceAmount, noBorderNumberCellBlackRight));
		    writableSheet.addCell(new Label(7, row, "", cellFormat));
		    writableSheet.addCell(new Label(8, row, "", cellFormat));
		    StringBuilder comments = new StringBuilder();
		    if (CommonUtils.isNotEmpty(transaction.getInvoiceNotes())) {
			int count = 1;
			for (String comment : StringUtils.splitByWholeSeparator(transaction.getInvoiceNotes(), "<--->")) {
			    comments.append(count).append(") ").append(comment).append("\n");
			    count++;
			}
		    }
		    writableSheet.addCell(new Label(9, row, comments.toString(), cellFormat));
		}
		row++;
		writableSheet.addCell(new Label(3, row, "Grand Total", cfBold));
		writableSheet.addCell(new Number(4, row, totalInvoiceAmount, noBorderNumberCellBlackRight));
		writableSheet.addCell(new Number(5, row, 0d, noBorderNumberCellBlackRight));
		writableSheet.addCell(new Number(6, row, totalInvoiceAmount, noBorderNumberCellBlackRight));
	    } else {
		writableSheet.addCell(new Label(0, row, "Date", columnHeaderCell));
		writableSheet.addCell(new Label(1, row, "Invoice/BL", columnHeaderCell));
		writableSheet.addCell(new Label(2, row, "Booking#", columnHeaderCell));
		writableSheet.setColumnView(3, 40);
		writableSheet.addCell(new Label(3, row, "Customer Reference", columnHeaderCell));
		writableSheet.addCell(new Label(4, row, "Invoice Amount", columnHeaderCell));
		writableSheet.addCell(new Label(5, row, "Payment/ Adjustment", columnHeaderCell));
		writableSheet.addCell(new Label(6, row, "Balance", columnHeaderCell));
		writableSheet.setColumnView(7, 40);
		writableSheet.addCell(new Label(7, row, "Comments", columnHeaderCell));
		if (agentFlag) {
		    writableSheet.setColumnView(8, 40);
		    writableSheet.addCell(new Label(8, row, "Consignee", columnHeaderCell));
		    writableSheet.setColumnView(9, 40);
		    writableSheet.addCell(new Label(9, row, "Voyage", columnHeaderCell));
		}
		double totalInvoiceAmount = 0d;
		double totalPayAdjAmount = 0d;
		double totalBalance = 0d;
		for (AccountingBean transaction : transactions) {
		    row++;
		    writableSheet.addCell(new Label(0, row, transaction.getFormattedDate(), cellFormat));
		    writableSheet.addCell(new Label(1, row, transaction.getInvoiceOrBl(), cellFormat));
		    writableSheet.addCell(new Label(2, row, transaction.getBookingNumber(), cellFormat));
		    writableSheet.addCell(new Label(3, row, transaction.getCustomerReference(), cellFormat));
		    double invoiceAmount = Double.parseDouble(transaction.getFormattedAmount().replaceAll(",", ""));
		    totalInvoiceAmount += invoiceAmount;
		    double payAdjAmount = -Double.parseDouble(transaction.getFormattedPayment().replaceAll(",", ""));
		    totalPayAdjAmount += payAdjAmount;
		    double balance = Double.parseDouble(transaction.getFormattedBalance().replaceAll(",", ""));
		    totalBalance += balance;
		    writableSheet.addCell(new Number(4, row, invoiceAmount, noBorderNumberCellBlackRight));
		    writableSheet.addCell(new Number(5, row, payAdjAmount, noBorderNumberCellBlackRight));
		    writableSheet.addCell(new Number(6, row, balance, noBorderNumberCellBlackRight));
		    StringBuilder comments = new StringBuilder();
		    if (CommonUtils.isNotEmpty(transaction.getInvoiceNotes())) {
			int count = 1;
			for (String comment : StringUtils.splitByWholeSeparator(transaction.getInvoiceNotes(), "<--->")) {
			    comments.append(count).append(") ").append(comment).append("\n");
			    count++;
			}
		    }
		    writableSheet.addCell(new Label(7, row, comments.toString(), cellFormat));
		    if (agentFlag) {
			writableSheet.addCell(new Label(8, row, transaction.getConsignee(), cellFormat));
			writableSheet.addCell(new Label(9, row, transaction.getVoyage(), cellFormat));
		    }
		}
		row++;
		writableSheet.addCell(new Label(3, row, "Grand Total", cfBold));
		writableSheet.addCell(new Number(4, row, totalInvoiceAmount, noBorderNumberCellBlackRight));
		writableSheet.addCell(new Number(5, row, totalPayAdjAmount, noBorderNumberCellBlackRight));
		writableSheet.addCell(new Number(6, row, totalBalance, noBorderNumberCellBlackRight));
	    }
	    row++;
	    row++;
	    writableSheet.setRowView(row, 400);
	    writableSheet.mergeCells(0, row, 9, row);
	    writableSheet.addCell(new Label(0, row, "Payment Methods", grayHeaderCell));
	    row++;
	    writableSheet.mergeCells(0, row, 2, row);
	    writableSheet.addCell(new Label(0, row, "Payment By Check", thinBorderCell));
	    writableSheet.mergeCells(3, row, 5, row);
	    writableSheet.addCell(new Label(3, row, "Electronic Funds Transafer Instructions:", thinBorderCell));
	    writableSheet.mergeCells(6, row, 9, row);
	    writableSheet.addCell(new Label(6, row, "Credit Card Payments", thinBorderCell));
	    row++;
	    writableSheet.setRowView(row, 2500);
	    writableSheet.mergeCells(0, row, 2, row);
	    writableSheet.addCell(new Label(0, row, "PLEASE REMIT PAYMENT \nTO: " + companyName + " \n" + companyAddress, thinBorderCell));
	    writableSheet.mergeCells(3, row, 5, row);
	    //Funds Transfer Instructions:
	    String eftBank = systemRulesDAO.getSystemRulesByCode("EFTBank");
	    String eftAcctName = systemRulesDAO.getSystemRulesByCode("EFTAcctName");
	    String eftABANo = systemRulesDAO.getSystemRulesByCode("EFTABANo");
	    String eftSwiftCode = systemRulesDAO.getSystemRulesByCode("EFTSwiftCode");
	    String eftAccountNo = systemRulesDAO.getSystemRulesByCode("EFTAccountNo");
	    String eftAddress = systemRulesDAO.getSystemRulesByCode("EFTBankAddress");
	    StringBuilder wirePayment = new StringBuilder();
	    wirePayment.append("If paying via wire transfer,please send to:");
	    wirePayment.append("\nBank :").append(eftBank);
	    wirePayment.append("\nAddress:").append(eftAddress);
	    wirePayment.append("\nABA #:").append(eftABANo).append(" \nSwift Code:").append(eftSwiftCode);
	    wirePayment.append("\nAccount Name: ").append(eftAcctName).append("\nAccount#:").append(eftAccountNo);
	    writableSheet.addCell(new Label(3, row, wirePayment.toString(), thinBorderCell));
	    writableSheet.mergeCells(6, row, 9, row);
	    writableSheet.addCell(new Label(6, row, "If paying with Credit Card \n" + systemRulesDAO.getSystemRulesByCode("CreditCardWeb"), thinBorderCell));
	    row++;
	    writableSheet.mergeCells(0, row, 9, row);
	    StringBuilder collectorDetails = new StringBuilder();
	    if (CommonUtils.isNotEmpty(customer.getCollector())) {
		collectorDetails.append(customer.getCollector()).append("(").append(customer.getCollectorEmail()).append(")");
	    }
	    writableSheet.addCell(new Label(0, row, companyName + " Account Contact: " + collectorDetails.toString(), thinBorderCell));
	} catch (Exception e) {
	    log.info("ArStatement Export To Excel writing contents failed :- ",e);
	} finally {
	    if (null != baos) {
		baos.close();
	    }
	}
    }

    public String exportToExcel(List<AccountingBean> transactions,
	    CustomerBean customerDetails, Map<String, CustomerBean> agingBuckets, CustomerStatementForm customerStatementForm) throws IOException, Exception {
	StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
	fileName.append("/Documents/CustomerStatement/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	File file = new File(fileName.toString());
	if (!file.exists()) {
	    file.mkdirs();
	}
	if (customerStatementForm.isAllCustomers()) {
	    fileName.append("AllCustomer");
	} else if (CommonUtils.isNotEmpty(customerStatementForm.getCollector())) {
	    fileName.append("Collector");
	} else {
	    fileName.append(customerDetails.getCustomerNumber());
	}
	fileName.append(".xls");
	init(fileName.toString());
	generateExcelSheet(transactions, customerDetails, agingBuckets, customerStatementForm.isEcuLineReport());
	write();
	close();
	return fileName.toString();
    }
}
