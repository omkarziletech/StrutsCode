package com.gp.cong.logisoft.ExcelGenerator;

import java.io.File;
import java.util.List;
import jxl.write.WriteException;


import jxl.write.Label;
import jxl.write.Number;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.Constants.ApConstants;
import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.bean.AccountingBean;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.struts.form.AgingReportForm;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

public class ExportArAgingToExcel extends BaseExcelGenerator {
 private static final Logger log = Logger.getLogger(ExportArAgingToExcel.class);

    private int row = 0;
    private int endCell = 9;
    private int currentCell = 0;


    private boolean generateExcelSheet(AgingReportForm agingReportForm, List<AccountingBean> arAgingTransactions) {
        try {
            String sheetName = "AR Aging";
            if (CommonUtils.isEqual(agingReportForm.getAllCustomersCheck(), CommonConstants.ON)) {
                sheetName = "AR Aging For All Customer";
            }
            writableSheet = writableWorkbook.createSheet(sheetName, 0);
            this.generateHeader(agingReportForm);
            if (CommonUtils.isEqual(ReportConstants.REPORT_TYPE_SUMMARY, agingReportForm.getReport())) {
                this.generateSummaryReport(agingReportForm, arAgingTransactions);
            } else {
                this.generateDetailReport(agingReportForm, arAgingTransactions);
            }
            return true;
        } catch (Exception e) {
            log.info("generateExcelSheet failed on " + new Date(),e);
            return false;
        }
    }

    private void generateHeader(AgingReportForm agingReportForm) throws WriteException, Exception {
        String title = null;
        if (CommonUtils.isEqual(agingReportForm.getAllCustomersCheck(), CommonConstants.ON)) {
            title = "Ar Aging Report For All Customers";
            if (CommonUtils.isEqual(ReportConstants.REPORT_TYPE_SUMMARY, agingReportForm.getReport())) {
                endCell = 10;
            } else {
                endCell = 12;
            }
        } else {
            title = "Ar Aging Report";
            if (CommonUtils.isEqual(ReportConstants.REPORT_TYPE_SUMMARY, agingReportForm.getReport())) {
                endCell = 7;
            } else {
                endCell = 9;
            }
        }
        writableSheet.mergeCells(0, row, endCell, row);
        writableSheet.addCell(new Label(0, row, title, headerCell));
        row++;
        writableSheet.mergeCells(0, row, endCell, row);
        String cutOffTitle = "Cut-Off Date: " + agingReportForm.getDateRangeTo();
        if (CommonUtils.isEqual(agingReportForm.getNoPaymentDate(), CommonConstants.ON)) {
            cutOffTitle = "Includes All Payments Regardless of Date\n" + cutOffTitle;
        }
        writableSheet.addCell(new Label(0, row, cutOffTitle, headerCell));
        if (CommonUtils.isNotEqual(agingReportForm.getAllCustomersCheck(), CommonConstants.ON)) {
            row++;
            writableSheet.setRowView(row, 2000);
            writableSheet.mergeCells(0, row, 4, row);
            writableSheet.mergeCells(5, row, endCell, row);
            CustAddress custAddress = new CustAddressDAO().findByAccountNo(agingReportForm.getCustomerNumber());
            if (null != custAddress) {
                String customer = ReportConstants.CUSTOMERNAME + " " + custAddress.getAcctName()
                        + "\n" + ReportConstants.CUSTOMERNO + " " + custAddress.getAcctNo() + "\n";
                String eciAcctNo = new TradingPartnerDAO().getEciAcct(agingReportForm.getCustomerNumber());
                String blueScreenAccount = ReportConstants.BLUESCREEN_ACCOUNT + " : " + eciAcctNo;
                String address = ReportConstants.CUSTOMERADDRESS + " ";
                if (CommonUtils.isNotEmpty(custAddress.getAddress1())) {
                    address += custAddress.getAddress1() + ",\n";
                }
                if (CommonUtils.isNotEmpty(custAddress.getCity1())) {
                    address += custAddress.getCity1() + ",\n";
                }
                if (CommonUtils.isNotEmpty(custAddress.getState())) {
                    address += custAddress.getState() + ",\n";
                }
                if (null != custAddress.getCuntry() && CommonUtils.isNotEmpty(custAddress.getCuntry().getCodedesc())) {
                    address += custAddress.getCuntry().getCodedesc() + ",\n";
                }
                if (CommonUtils.isNotEmpty(custAddress.getZip())) {
                    address += custAddress.getZip() + ",\n";
                }
                if (CommonUtils.isNotEmpty(custAddress.getPhone())) {
                    address += custAddress.getPhone() + ",\n";
                }
                if (CommonUtils.isNotEmpty(custAddress.getFax())) {
                    address += custAddress.getFax() + ",";
                }
                writableSheet.addCell(new Label(0, row, customer+blueScreenAccount, headerCell));
                writableSheet.addCell(new Label(5, row, StringUtils.removeEnd(address, ","), headerCell));
            }
        }
        row++;
        for (int i = 0; i <= endCell; i++) {
            writableSheet.setColumnView(i, 20);
        }
        currentCell = 0;
        if (CommonUtils.isEqual(agingReportForm.getAllCustomersCheck(), CommonConstants.ON)) {
            writableSheet.setColumnView(0, 30);
            writableSheet.setColumnView(2, 50);
            writableSheet.addCell(new Label(currentCell++, row, ReportConstants.BLUESCREEN_ACCOUNT, headerCell));
            writableSheet.addCell(new Label(currentCell++, row, ReportConstants.ACCT_NO, headerCell));
            writableSheet.addCell(new Label(currentCell++, row, ReportConstants.ACCT_NAME, headerCell));
        }
        if (CommonUtils.isEqual(ReportConstants.REPORT_TYPE_SUMMARY, agingReportForm.getReport())) {
            writableSheet.addCell(new Label(currentCell++, row, "Collector", headerCell));
            writableSheet.addCell(new Label(currentCell++, row, "Credit Status", headerCell));
            writableSheet.addCell(new Label(currentCell++, row, "Credit Limit", headerCell));
        } else {
            writableSheet.addCell(new Label(currentCell++, row, ReportConstants.BL_DR_NO, headerCell));
            writableSheet.addCell(new Label(currentCell++, row, ReportConstants.INV_NO, headerCell));
            writableSheet.addCell(new Label(currentCell++, row, ReportConstants.INVOICEDATE, headerCell));
            writableSheet.addCell(new Label(currentCell++, row, ReportConstants.REF_NO, headerCell));
        }
        writableSheet.addCell(new Label(currentCell++, row, "0-30 days", headerCell));
        writableSheet.addCell(new Label(currentCell++, row, "31-60 days", headerCell));
        writableSheet.addCell(new Label(currentCell++, row, "61-90 days", headerCell));
        writableSheet.addCell(new Label(currentCell++, row, "91+ days", headerCell));
        writableSheet.addCell(new Label(currentCell++, row, ApConstants.TOTAL, headerCell));
        if (CommonUtils.isEqual(ReportConstants.REPORT_TYPE_DETAIL, agingReportForm.getReport())) {
            writableSheet.addCell(new Label(currentCell++, row, ReportConstants.VOY_NO, headerCell));
        }
    }

    private void generateSummaryReport(AgingReportForm agingReportForm, List<AccountingBean> arAgingTransactions) throws WriteException {
        Double age1Total = 0d;
        Double age2Total = 0d;
        Double age3Total = 0d;
        Double age4Total = 0d;
        Double grandTotal = 0d;
        for (AccountingBean arTransaction : arAgingTransactions) {
            row++;
            currentCell = 0;
            if (CommonUtils.isEqual(agingReportForm.getAllCustomersCheck(), CommonConstants.ON)) {
                writableSheet.addCell(new Label(currentCell++, row, arTransaction.getBlueScreenAccount(), noBorderCellBlackLeft));
                writableSheet.addCell(new Label(currentCell++, row, arTransaction.getCustomerNumber(), noBorderCellBlackLeft));
                writableSheet.addCell(new Label(currentCell++, row, arTransaction.getCustomerName(), noBorderCellBlackLeft));
            }
            writableSheet.addCell(new Label(currentCell++, row, arTransaction.getCollector(), noBorderCellBlackLeft));
            String creditStatus = TradingPartnerConstants.NOCREDIT;
            if (CommonUtils.isEmpty(arTransaction.getCreditLimit())) {
                creditStatus = TradingPartnerConstants.NOCREDIT;
            } else if (CommonUtils.isEmpty(arTransaction.getCreditLimit())
                    && (CommonUtils.isEqualIgnoreCase(arTransaction.getCreditStatus(), TradingPartnerConstants.SUSPENDED_SEE_ACCOUNTING)
                    || CommonUtils.isEqualIgnoreCase(arTransaction.getCreditStatus(), TradingPartnerConstants.LEGAL_SEE_ACCOUNTING))) {
                creditStatus = arTransaction.getCreditStatus();
            } else {
                creditStatus = arTransaction.getCreditStatus();
            }
            writableSheet.addCell(new Label(currentCell++, row, creditStatus, noBorderCellBlackLeft));
            writableSheet.addCell(new Number(currentCell++, row, arTransaction.getCreditLimit(), noBorderNumberCellBlackRight));
            writableSheet.addCell(new Number(currentCell++, row, arTransaction.getAge0_30Balance(), noBorderNumberCellBlackRight));
            writableSheet.addCell(new Number(currentCell++, row, arTransaction.getAge31_60Balance(), noBorderNumberCellBlackRight));
            writableSheet.addCell(new Number(currentCell++, row, arTransaction.getAge61_90Balance(), noBorderNumberCellBlackRight));
            writableSheet.addCell(new Number(currentCell++, row, arTransaction.getAge91Balance(), noBorderNumberCellBlackRight));
            writableSheet.addCell(new Number(currentCell++, row, arTransaction.getTotal(), noBorderNumberCellBlackRight));
            age1Total += arTransaction.getAge0_30Balance();
            age2Total += arTransaction.getAge31_60Balance();
            age3Total += arTransaction.getAge61_90Balance();
            age4Total += arTransaction.getAge91Balance();
            grandTotal += arTransaction.getTotal();
        }
        row++;
        currentCell = 0;
        writableSheet.mergeCells(0, row, endCell - 5, row);
        writableSheet.addCell(new Label(currentCell++, row, "Grand Total:", noBorderCellRedRight));
        currentCell = endCell - 4;
        writableSheet.addCell(new Number(currentCell++, row, age1Total, noBorderNumberCellBlackRight));
        writableSheet.addCell(new Number(currentCell++, row, age2Total, noBorderNumberCellBlackRight));
        writableSheet.addCell(new Number(currentCell++, row, age3Total, noBorderNumberCellBlackRight));
        writableSheet.addCell(new Number(currentCell++, row, age4Total, noBorderNumberCellBlackRight));
        writableSheet.addCell(new Number(currentCell++, row, grandTotal, noBorderNumberCellBlackRight));
    }

    private void generateDetailReport(AgingReportForm agingReportForm, List<AccountingBean> arAgingTransactions) throws WriteException, Exception {
        Double age1Total = 0d;
        Double age2Total = 0d;
        Double age3Total = 0d;
        Double age4Total = 0d;
        Double grandTotal = 0d;
        for (AccountingBean arTransaction : arAgingTransactions) {
            row++;
            currentCell = 0;
            if (CommonUtils.isEqual(agingReportForm.getAllCustomersCheck(), CommonConstants.ON)) {
                writableSheet.addCell(new Label(currentCell++, row, arTransaction.getBlueScreenAccount(), noBorderCellBlackLeft));
                writableSheet.addCell(new Label(currentCell++, row, arTransaction.getCustomerNumber(), noBorderCellBlackLeft));
                writableSheet.addCell(new Label(currentCell++, row, arTransaction.getCustomerName(), noBorderCellBlackLeft));
            }
            writableSheet.addCell(new Label(currentCell++, row, arTransaction.getBillOfLadding(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(currentCell++, row, arTransaction.getInvoiceNumber(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(currentCell++, row, DateUtils.formatDate(arTransaction.getTransactionDate(), "MM/dd/yyyy"), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(currentCell++, row, arTransaction.getCustomerReference(), noBorderCellBlackLeft));
            writableSheet.addCell(new Number(currentCell++, row, arTransaction.getAge0_30Balance(), noBorderNumberCellBlackRight));
            writableSheet.addCell(new Number(currentCell++, row, arTransaction.getAge31_60Balance(), noBorderNumberCellBlackRight));
            writableSheet.addCell(new Number(currentCell++, row, arTransaction.getAge61_90Balance(), noBorderNumberCellBlackRight));
            writableSheet.addCell(new Number(currentCell++, row, arTransaction.getAge91Balance(), noBorderNumberCellBlackRight));
            writableSheet.addCell(new Number(currentCell++, row, arTransaction.getTotal(), noBorderNumberCellBlackRight));
            writableSheet.addCell(new Label(currentCell++, row, arTransaction.getVoyage(), noBorderCellBlackLeft));
            age1Total += arTransaction.getAge0_30Balance();
            age2Total += arTransaction.getAge31_60Balance();
            age3Total += arTransaction.getAge61_90Balance();
            age4Total += arTransaction.getAge91Balance();
            grandTotal += arTransaction.getTotal();
        }
        row++;
        currentCell = 0;
        writableSheet.mergeCells(0, row, endCell - 6, row);
        writableSheet.addCell(new Label(currentCell++, row, "Grand Total:", noBorderCellRedRight));
        currentCell = endCell - 5;
        writableSheet.addCell(new Number(currentCell++, row, age1Total, noBorderNumberCellBlackRight));
        writableSheet.addCell(new Number(currentCell++, row, age2Total, noBorderNumberCellBlackRight));
        writableSheet.addCell(new Number(currentCell++, row, age3Total, noBorderNumberCellBlackRight));
        writableSheet.addCell(new Number(currentCell++, row, age4Total, noBorderNumberCellBlackRight));
        writableSheet.addCell(new Number(currentCell++, row, grandTotal, noBorderNumberCellBlackRight));
        writableSheet.addCell(new Label(currentCell++, row, "", noBorderCellBlackLeft));
    }

    public String exportToExcel(AgingReportForm agingReportForm, List<AccountingBean> arAgingTransactions) throws Exception {
        String fileName = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/ArAgingReport/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/" + agingReportForm.getReport();
        File file = new File(fileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (CommonUtils.isEqual(agingReportForm.getAllCustomersCheck(), CommonConstants.ON)) {
            fileName += "/AllCustomers.xls";
        } else {
            fileName += "/" + agingReportForm.getCustomerNumber() + ".xls";
        }
        file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        super.init(fileName);
        if (null != super.writableWorkbook) {
            if (this.generateExcelSheet(agingReportForm, arAgingTransactions)) {
                try {
                    super.write();
                    super.close();
                } catch (Exception e) {
                    fileName = null;
                    log.info("exportToExcel failed on " + new Date(),e);
                }
            }
        }
        return fileName;
    }
}
