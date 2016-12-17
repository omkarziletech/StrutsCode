package com.logiware.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.ExcelGenerator.BaseExcelGenerator;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.bean.AccountingBean;
import java.io.File;
import java.util.Date;
import java.util.List;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class SubledgerHistoryExcelCreator extends BaseExcelGenerator {

    private WritableCellFormat noBorderHeaderCell = null;
    private WritableCellFormat grayHeaderCell = null;
    private WritableCellFormat columnHeaderCell = null;
    private int row = 0;

    private void writeContents(String journalEntryId, String subledgerType, String period, List<AccountingBean> transactions) throws Exception {
        this.writableSheet = this.writableWorkbook.createSheet(subledgerType, 0);
        this.writableSheet.setRowView(this.row, 400);
        this.writableSheet.mergeCells(0, this.row, 3, this.row);
        this.writableSheet.addCell(new Label(0, this.row, "Subledger History Report", this.noBorderHeaderCell));
        this.row += 1;
        this.writableSheet.setRowView(this.row, 400);
        this.writableSheet.mergeCells(0, this.row, 1, this.row);
        this.writableSheet.addCell(new Label(0, this.row, "Journal Entry: " + journalEntryId, this.noBorderHeaderCell));
        this.writableSheet.mergeCells(2, this.row, 3, this.row);
        this.writableSheet.addCell(new Label(2, this.row, "Subledger: " + subledgerType, this.noBorderHeaderCell));
        this.row += 1;
        this.writableSheet.setRowView(this.row, 400);
        this.writableSheet.mergeCells(0, this.row, 1, this.row);
        this.writableSheet.addCell(new Label(0, this.row, "Period: " + period, this.noBorderHeaderCell));
        this.writableSheet.mergeCells(2, this.row, 3, this.row);
        this.writableSheet.addCell(new Label(2, this.row, "Created Date: " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"), this.noBorderHeaderCell));
        if (CommonUtils.isEqualIgnoreCase(subledgerType, "ACC")) {
            writeACCContents(transactions);
        } else if (CommonUtils.isEqualIgnoreCase(subledgerType, "PJ")) {
            writePJContents(transactions);
        } else if (CommonUtils.isEqualIgnoreCase(subledgerType, "CD")) {
            writeCDContents(transactions);
        } else if (CommonUtils.isEqualIgnoreCase(subledgerType, "NET SETT")) {
            writeNSContents(transactions);
        } else if (CommonUtils.isEqualIgnoreCase(subledgerType, "RCT")) {
            writeRCTContents(transactions);
        } else {
            writeARContents(transactions);
        }
    }

    private void writeACCContents(List<AccountingBean> transactions) throws Exception {
        this.row += 1;
        for (int i = 0; i <= 12; i++) {
            this.writableSheet.setColumnView(i, 20);
        }
        this.writableSheet.setColumnView(1, 50);
        this.writableSheet.setColumnView(3, 30);
        this.writableSheet.setColumnView(4, 30);
        this.writableSheet.setColumnView(10, 30);
        this.writableSheet.setColumnView(11, 30);
        this.writableSheet.addCell(new Label(0, this.row, "Vendor Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(1, this.row, "Vendor Name", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(2, this.row, "GL Account", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(3, this.row, "BL Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(4, this.row, "Invoice Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(5, this.row, "Voyage", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(6, this.row, "Charge Code", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(7, this.row, "Transaction Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(8, this.row, "Reporting Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(9, this.row, "Posted Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(10, this.row, "Debit", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(11, this.row, "Credit", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(12, this.row, "Line Item Number", this.columnHeaderCell));
        for (AccountingBean transaction : transactions) {
            this.row += 1;
            this.writableSheet.addCell(new Label(0, this.row, transaction.getVendorNumber(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(1, this.row, transaction.getVendorName(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(2, this.row, transaction.getGlAccount(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(3, this.row, transaction.getBillOfLadding(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(4, this.row, transaction.getInvoiceNumber(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(5, this.row, transaction.getVoyage(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(6, this.row, transaction.getChargeCode(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(7, this.row, transaction.getFormattedDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(8, this.row, transaction.getFormattedReportingDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(9, this.row, transaction.getFormattedPostedDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Number(10, this.row, transaction.getDebitAmount(), this.noBorderNumberCellBlackRight));
            this.writableSheet.addCell(new Number(11, this.row, transaction.getCreditAmount(), this.noBorderNumberCellBlackRight));
            this.writableSheet.addCell(new Label(12, this.row, transaction.getLineItemId(), this.noBorderCellBlackLeft));
        }
    }

    private void writePJContents(List<AccountingBean> transactions) throws Exception {
        this.row += 1;
        for (int i = 0; i <= 12; i++) {
            this.writableSheet.setColumnView(i, 20);
        }
        this.writableSheet.setColumnView(1, 50);
        this.writableSheet.setColumnView(3, 30);
        this.writableSheet.setColumnView(4, 30);
        this.writableSheet.setColumnView(10, 30);
        this.writableSheet.setColumnView(11, 30);
        this.writableSheet.addCell(new Label(0, this.row, "Vendor Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(1, this.row, "Vendor Name", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(2, this.row, "GL Account", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(3, this.row, "BL Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(4, this.row, "Invoice Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(5, this.row, "Voyage", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(6, this.row, "Charge Code", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(7, this.row, "Transaction Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(8, this.row, "Reporting Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(9, this.row, "Posted Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(10, this.row, "Debit", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(11, this.row, "Credit", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(12, this.row, "Line Item Number", this.columnHeaderCell));
        for (AccountingBean transaction : transactions) {
            this.row += 1;
            this.writableSheet.addCell(new Label(0, this.row, transaction.getVendorNumber(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(1, this.row, transaction.getVendorName(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(2, this.row, transaction.getGlAccount(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(3, this.row, transaction.getBillOfLadding(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(4, this.row, transaction.getInvoiceNumber(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(5, this.row, transaction.getVoyage(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(6, this.row, transaction.getChargeCode(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(7, this.row, transaction.getFormattedDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(8, this.row, transaction.getFormattedReportingDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(9, this.row, transaction.getFormattedPostedDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Number(10, this.row, transaction.getDebitAmount(), this.noBorderNumberCellBlackRight));
            this.writableSheet.addCell(new Number(11, this.row, transaction.getCreditAmount(), this.noBorderNumberCellBlackRight));
            this.writableSheet.addCell(new Label(12, this.row, transaction.getLineItemId(), this.noBorderCellBlackLeft));
        }
    }

    private void writeCDContents(List<AccountingBean> transactions) throws Exception {
        this.row += 1;
        for (int i = 0; i <= 12; i++) {
            this.writableSheet.setColumnView(i, 20);
        }
        this.writableSheet.setColumnView(1, 50);
        this.writableSheet.setColumnView(3, 30);
        this.writableSheet.setColumnView(4, 30);
        this.writableSheet.setColumnView(9, 30);
        this.writableSheet.setColumnView(10, 30);
        this.writableSheet.addCell(new Label(0, this.row, "Vendor Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(1, this.row, "Vendor Name", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(2, this.row, "GL Account", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(3, this.row, "BL Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(4, this.row, "Invoice Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(5, this.row, "Voyage", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(6, this.row, "Charge Code", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(7, this.row, "Transaction Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(8, this.row, "Posted Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(9, this.row, "Debit", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(10, this.row, "Credit", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(11, this.row, "AP Batch Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(12, this.row, "Line Item Number", this.columnHeaderCell));
        for (AccountingBean transaction : transactions) {
            this.row += 1;
            this.writableSheet.addCell(new Label(0, this.row, transaction.getVendorNumber(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(1, this.row, transaction.getVendorName(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(2, this.row, transaction.getGlAccount(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(3, this.row, transaction.getBillOfLadding(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(4, this.row, transaction.getInvoiceNumber(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(5, this.row, transaction.getVoyage(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(6, this.row, transaction.getChargeCode(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(7, this.row, transaction.getFormattedDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(8, this.row, transaction.getFormattedPostedDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Number(9, this.row, transaction.getDebitAmount(), this.noBorderNumberCellBlackRight));
            this.writableSheet.addCell(new Number(10, this.row, transaction.getCreditAmount(), this.noBorderNumberCellBlackRight));
            this.writableSheet.addCell(new Number(11, this.row, transaction.getApBatchId(), this.noBorderCellBlackRight));
            this.writableSheet.addCell(new Label(12, this.row, transaction.getLineItemId(), this.noBorderCellBlackLeft));
        }
    }

    private void writeNSContents(List<AccountingBean> transactions) throws Exception {
        this.row += 1;
        for (int i = 0; i <= 13; i++) {
            this.writableSheet.setColumnView(i, 20);
        }
        this.writableSheet.setColumnView(1, 50);
        this.writableSheet.setColumnView(3, 30);
        this.writableSheet.setColumnView(4, 30);
        this.writableSheet.setColumnView(9, 30);
        this.writableSheet.setColumnView(10, 30);
        this.writableSheet.addCell(new Label(0, this.row, "Vendor Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(1, this.row, "Vendor Name", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(2, this.row, "GL Account", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(3, this.row, "BL Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(4, this.row, "Invoice Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(5, this.row, "Voyage", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(6, this.row, "Charge Code", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(7, this.row, "Transaction Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(8, this.row, "Posted Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(9, this.row, "Debit", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(10, this.row, "Credit", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(11, this.row, "AR Batch Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(12, this.row, "Transaction Type", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(13, this.row, "Line Item Number", this.columnHeaderCell));
        for (AccountingBean transaction : transactions) {
            this.row += 1;
            this.writableSheet.addCell(new Label(0, this.row, transaction.getVendorNumber(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(1, this.row, transaction.getVendorName(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(2, this.row, transaction.getGlAccount(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(3, this.row, transaction.getBillOfLadding(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(4, this.row, transaction.getInvoiceNumber(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(5, this.row, transaction.getVoyage(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(6, this.row, transaction.getChargeCode(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(7, this.row, transaction.getFormattedDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(8, this.row, transaction.getFormattedPostedDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Number(9, this.row, transaction.getDebitAmount(), this.noBorderNumberCellBlackRight));
            this.writableSheet.addCell(new Number(10, this.row, transaction.getCreditAmount(), this.noBorderNumberCellBlackRight));
            if (null != transaction.getArBatchId()) {
                this.writableSheet.addCell(new Number(11, this.row, transaction.getArBatchId(), this.noBorderCellBlackRight));
            } else {
                this.writableSheet.addCell(new Label(11, this.row, "", this.noBorderCellBlackRight));
            }
            this.writableSheet.addCell(new Label(12, this.row, transaction.getTransactionType(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(13, this.row, transaction.getLineItemId(), this.noBorderCellBlackLeft));
        }
    }

    private void writeRCTContents(List<AccountingBean> transactions) throws Exception {
        this.row += 1;
        for (int i = 0; i <= 13; i++) {
            this.writableSheet.setColumnView(i, 20);
        }
        this.writableSheet.setColumnView(1, 50);
        this.writableSheet.setColumnView(3, 30);
        this.writableSheet.setColumnView(4, 30);
        this.writableSheet.setColumnView(9, 30);
        this.writableSheet.setColumnView(10, 30);
        this.writableSheet.addCell(new Label(0, this.row, "Vendor Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(1, this.row, "Vendor Name", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(2, this.row, "GL Account", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(3, this.row, "BL Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(4, this.row, "Invoice Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(5, this.row, "Voyage", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(6, this.row, "Charge Code", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(7, this.row, "Transaction Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(8, this.row, "Posted Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(9, this.row, "Debit", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(10, this.row, "Credit", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(11, this.row, "AR Batch Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(12, this.row, "Transaction Type", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(13, this.row, "Line Item Number", this.columnHeaderCell));
        for (AccountingBean transaction : transactions) {
            this.row += 1;
            this.writableSheet.addCell(new Label(0, this.row, transaction.getVendorNumber(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(1, this.row, transaction.getVendorName(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(2, this.row, transaction.getGlAccount(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(3, this.row, transaction.getBillOfLadding(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(4, this.row, transaction.getInvoiceNumber(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(5, this.row, transaction.getVoyage(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(6, this.row, transaction.getChargeCode(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(7, this.row, transaction.getFormattedDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(8, this.row, transaction.getFormattedPostedDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Number(9, this.row, transaction.getDebitAmount(), this.noBorderNumberCellBlackRight));
            this.writableSheet.addCell(new Number(10, this.row, transaction.getCreditAmount(), this.noBorderNumberCellBlackRight));
            if (null != transaction.getArBatchId()) {
                this.writableSheet.addCell(new Number(11, this.row, transaction.getArBatchId(), this.noBorderCellBlackRight));
            } else {
                this.writableSheet.addCell(new Label(11, this.row, "", this.noBorderCellBlackRight));
            }
            this.writableSheet.addCell(new Label(12, this.row, transaction.getTransactionType(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(13, this.row, transaction.getLineItemId(), this.noBorderCellBlackLeft));
        }
    }

    private void writeARContents(List<AccountingBean> transactions) throws Exception {
        this.row += 1;
        for (int i = 0; i <= 11; i++) {
            this.writableSheet.setColumnView(i, 20);
        }
        this.writableSheet.setColumnView(1, 50);
        this.writableSheet.setColumnView(3, 30);
        this.writableSheet.setColumnView(4, 30);
        this.writableSheet.setColumnView(9, 30);
        this.writableSheet.setColumnView(10, 30);
        this.writableSheet.addCell(new Label(0, this.row, "Vendor Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(1, this.row, "Vendor Name", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(2, this.row, "GL Account", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(3, this.row, "BL Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(4, this.row, "Invoice Number", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(5, this.row, "Voyage", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(6, this.row, "Charge Code", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(7, this.row, "Transaction Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(8, this.row, "Posted Date", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(9, this.row, "Debit", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(10, this.row, "Credit", this.columnHeaderCell));
        this.writableSheet.addCell(new Label(11, this.row, "Line Item Number", this.columnHeaderCell));
        for (AccountingBean transaction : transactions) {
            this.row += 1;
            this.writableSheet.addCell(new Label(0, this.row, transaction.getVendorNumber(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(1, this.row, transaction.getVendorName(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(2, this.row, transaction.getGlAccount(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(3, this.row, transaction.getBillOfLadding(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(4, this.row, transaction.getInvoiceNumber(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(5, this.row, transaction.getVoyage(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(6, this.row, transaction.getChargeCode(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(7, this.row, transaction.getFormattedDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Label(8, this.row, transaction.getFormattedPostedDate(), this.noBorderCellBlackLeft));
            this.writableSheet.addCell(new Number(9, this.row, transaction.getDebitAmount(), this.noBorderNumberCellBlackRight));
            this.writableSheet.addCell(new Number(10, this.row, transaction.getCreditAmount(), this.noBorderNumberCellBlackRight));
            this.writableSheet.addCell(new Label(11, this.row, transaction.getLineItemId(), this.noBorderCellBlackLeft));
        }
    }

    public String exportToExcel(String journalEntryId, String subledgerType, String period, List<AccountingBean> transactions) throws Exception {
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
        fileName.append("/Documents/JournalEntry/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append("Subledger_History_").append(journalEntryId).append(".xls");
        init(fileName.toString());
        WritableFont font16 = createFont(new WritableFont(WritableFont.ARIAL), 16, "BOLD", false, UnderlineStyle.NO_UNDERLINE, Colour.AUTOMATIC);
        this.noBorderHeaderCell = createCellFormat(font16, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.LEFT);
        this.grayHeaderCell = createCellFormat(font16, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.LEFT);
        this.columnHeaderCell = createCellFormat(this.wfBoldForColumns, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.CENTRE);
        this.grayHeaderCell.setBackground(Colour.GREY_40_PERCENT);
        this.columnHeaderCell.setBackground(Colour.GRAY_25);
        writeContents(journalEntryId, subledgerType, period, transactions);
        write();
        close();
        return fileName.toString();
    }
}