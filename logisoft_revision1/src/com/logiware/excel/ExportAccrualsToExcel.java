package com.logiware.excel;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import java.util.List;

import jxl.write.Label;

import com.gp.cong.logisoft.Constants.ApConstants;
import com.gp.cong.logisoft.ExcelGenerator.BaseExcelGenerator;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.struts.form.AccrualsForm;
import java.io.File;
import java.util.Date;

public class ExportAccrualsToExcel extends BaseExcelGenerator implements ApConstants {

    private void generateExcelSheet(AccrualsForm accrualsForm, List<TransactionBean> accruals) throws Exception {
        String sheetName = "Accruals";
        writableSheet = writableWorkbook.createSheet(sheetName, 0);
        int row = 0;
        int endCol = 12;
        writableSheet.mergeCells(0, row, endCol, row);
        writableSheet.addCell(new Label(0, row, "Accruals Report", headerCell));
        row++;
        writableSheet.mergeCells(0, row, endCol, row);
        writableSheet.addCell(new Label(0, row, "Date:" + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), headerCell));
        if (CommonUtils.isNotEqual(accrualsForm.getCategory(), "0") && CommonUtils.isNotEmpty(accrualsForm.getDocNumber())) {
            row++;
            writableSheet.mergeCells(0, row, endCol, row);
            writableSheet.addCell(new Label(0, row, "Search by :" + accrualsForm.getCategory(), headerCell));
            row++;
            writableSheet.mergeCells(0, row, endCol, row);
            writableSheet.addCell(new Label(0, row, "Search Value :" + accrualsForm.getDocNumber(), headerCell));
        } else {
            TradingPartner tradingPartner = new TradingPartnerDAO().findById(accrualsForm.getVendornumber());
            if (null != tradingPartner) {
                row++;
                writableSheet.mergeCells(0, row, endCol, row);
                writableSheet.addCell(new Label(0, row, "Vendor Name :" + tradingPartner.getAccountName(), headerCell));
                row++;
                writableSheet.mergeCells(0, row, endCol, row);
                writableSheet.addCell(new Label(0, row, "Vendor Number :" + tradingPartner.getAccountno(), headerCell));
            }
        }
        row++;
        int col = 0;
        writableSheet.setColumnView(0, 50);
        writableSheet.setColumnView(1, 20);
        for (int current = endCol - 10; current <= endCol; current++) {
            writableSheet.setColumnView(current, 20);
        }
        writableSheet.setColumnView(endCol-4, 40);
        writableSheet.addCell(new Label(col++, row, "Vendor Name", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Vendor Number", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Invoice Number", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Bill of Ladding", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Cost Code", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Reporting Date", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Accrued Amount", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Check Number", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Container Number", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Dock Receipt", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Voyage Number", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Operations Contact", columnHeaderCell));
        writableSheet.addCell(new Label(col++, row, "Status", columnHeaderCell));

        for (TransactionBean accrual : accruals) {
            row++;
            col = 0;
            writableSheet.addCell(new Label(col++, row, accrual.getCustomer(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, accrual.getCustomerNo(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, accrual.getInvoiceOrBl(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, accrual.getBillofLadding(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, accrual.getChargeCode(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, DateUtils.formatDate(accrual.getSailingDate(), "MM/dd/yyyy"), noBorderCellBlackLeft));
            writableSheet.addCell(new jxl.write.Number(col++, row, null != accrual.getTransactionAmount() ? accrual.getTransactionAmount() : 0d, noBorderNumberCellBlackRight));
            writableSheet.addCell(new Label(col++, row, accrual.getChequenumber(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, accrual.getContainerNo(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, accrual.getDocReceipt(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, accrual.getVoyage(), noBorderCellBlackLeft));
            writableSheet.addCell(new Label(col++, row, accrual.getContact(), noBorderCellBlackLeft));
            String status = accrual.getStatus();
            if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_ASSIGN)) {
                status = "Assign";
            } else if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_INACTIVE)) {
                status = "Inactive";
            } else if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_IN_PROGRESS)) {
                status = "In Progress";
            } else if (CommonUtils.isEqualIgnoreCase(status, CommonConstants.STATUS_REJECT)) {
                status = "Reject";
            }
            writableSheet.addCell(new Label(col++, row, status, noBorderCellBlackLeft));
        }

    }

    public String exportToExcel(AccrualsForm accrualsForm, List<TransactionBean> accruals) throws Exception {
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/AccountPayable/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append("Accruals.xls");
        super.init(fileName.toString());
        this.generateExcelSheet(accrualsForm, accruals);
        super.write();
        super.close();
        return fileName.toString();
    }
}
