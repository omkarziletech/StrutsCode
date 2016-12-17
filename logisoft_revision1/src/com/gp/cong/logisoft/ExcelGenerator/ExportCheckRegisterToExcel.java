package com.gp.cong.logisoft.ExcelGenerator;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import java.util.Date;
import java.util.List;

import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.struts.form.CheckRegisterForm;

import jxl.write.Label;

public class ExportCheckRegisterToExcel extends BaseExcelGenerator {

    private void generateExcelSheet(CheckRegisterForm checkRegisterForm,List<Transaction> checkRegisters) throws Exception {
        String sheetName = "CheckRegister";
        writableSheet = writableWorkbook.createSheet(sheetName, 0);
        int row = 0;
        writableSheet.mergeCells(0, row, 1, row);
        writableSheet.addCell(new Label(0, row, ExcelSheetConstants.PAYMENT_DETAILS_HEADING, headerCell));
        row++;
        writableSheet.mergeCells(0, row, 1, row);
        writableSheet.addCell(new Label(0, row, "Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), headerCell));
        row++;
        writableSheet.mergeCells(0, row, 1, row);
        TradingPartner tradingPartner = new TradingPartnerDAO().findById(checkRegisterForm.getCustNo());
        writableSheet.addCell(new Label(0, row, "Pay To: " + tradingPartner.getAccountName()+"("+tradingPartner.getAccountno()+")", headerCell));
        row++;
        writableSheet.mergeCells(0, row, 1, row);
        writableSheet.addCell(new Label(0, row, "Payment Method: " + checkRegisterForm.getPaymentMethod(), headerCell));
        row++;
        writableSheet.mergeCells(0, row, 1, row);
        writableSheet.addCell(new Label(0, row, "Check #: " + checkRegisterForm.getCheckNo(), headerCell));
        row++;
        writableSheet.mergeCells(0, row, 1, row);
        writableSheet.addCell(new Label(0, row, "Check Date: " + checkRegisterForm.getPaymentDate(), headerCell));
        writableSheet.setColumnView(0, 40);
        writableSheet.setColumnView(1, 30);

        /*Table Column Header*/
        row++;
        writableSheet.addCell(new Label(0, row, ExcelSheetConstants.INVOICE_NO, columnHeaderCell));
        writableSheet.addCell(new Label(1, row, ExcelSheetConstants.AMOUNT, columnHeaderCell));
        /*Table Data*/
        row++;
        if (CommonUtils.isNotEmpty(checkRegisters)) {
            double total = 0d;
            for (Transaction transaction : checkRegisters) {
                writableSheet.addCell(new Label(0, row, transaction.getInvoiceNumber(), noBorderCellBlackLeft));
                writableSheet.addCell(new jxl.write.Number(1, row, transaction.getTransactionAmt(), noBorderNumberCellBlackRight));
                total+= transaction.getTransactionAmt();
                row++;
            }
            writableSheet.addCell(new Label(0, row, "Total ", noBorderBoldAlignLeft));
            writableSheet.addCell(new jxl.write.Number(1, row, total, noBorderNumberCellBoldBlackRight));
        }
    }

    public void exportToExcel(String fileName,CheckRegisterForm checkRegisterForm, List<Transaction> checkRegisters) throws Exception {
        super.init(fileName);
        this.generateExcelSheet(checkRegisterForm,checkRegisters);
        super.write();
        super.close();
    }
}
