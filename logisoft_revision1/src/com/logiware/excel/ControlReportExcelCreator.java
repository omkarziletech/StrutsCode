package com.logiware.excel;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;

import jxl.write.Label;
import jxl.write.Number;

import com.gp.cong.logisoft.Constants.ApConstants;
import com.gp.cong.logisoft.ExcelGenerator.BaseExcelGenerator;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.bean.AccountingBean;
import com.logiware.form.ControlReportForm;
import java.io.File;
import java.util.Date;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;

public class ControlReportExcelCreator extends BaseExcelGenerator implements ApConstants {

    private void generateExcelSheet(ControlReportForm controlReportForm) throws Exception {

        WritableCellFormat headerCell = createCellFormat(wfBold, null, Border.NONE, BorderLineStyle.THIN, Colour.AUTOMATIC, true, Alignment.LEFT);
        headerCell.setBackground(Colour.BLUE_GREY);
        WritableCellFormat columnHeaderCell = createCellFormat(wfBoldForColumns, null, Border.NONE, BorderLineStyle.THIN, Colour.AUTOMATIC, true, Alignment.CENTRE);
        columnHeaderCell.setBackground(Colour.GREY_50_PERCENT);

        String sheetName = "Accruals";
        writableSheet = writableWorkbook.createSheet(sheetName, 0);
        int row = 0;
        String title = "Accruals Control Report";
        if (CommonUtils.isEqualIgnoreCase(controlReportForm.getReportType(), CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
            sheetName = "Account Receivable";
            title = "Account Receivable Control Report";
        }
        writableSheet.setColumnView(0, 50);
        writableSheet.setColumnView(1, 20);
        writableSheet.setColumnView(2, 20);
        writableSheet.setColumnView(3, 20);
        writableSheet.setColumnView(4, 40);
        writableSheet.mergeCells(0, row, 4, row);
        writableSheet.addCell(new Label(0, row, title, headerCell));
        row++;
        row++;
        writableSheet.setRowView(row, 400);
        writableSheet.mergeCells(0, row, 4, row);
        String date = "From Date : " + controlReportForm.getFromDate() + "\nTo Date : " + controlReportForm.getToDate();
        writableSheet.addCell(new Label(0, row, date, noBorderCell));
        row++;
        if (CommonUtils.isNotEmpty(controlReportForm.getNumberOfBluScreenRecords())
                || CommonUtils.isNotEmpty(controlReportForm.getNumberOfLogiwareRecords())) {
            writableSheet.addCell(new Label(1, row, "", columnHeaderCell));
            writableSheet.addCell(new Label(2, row, "Blue Screen", columnHeaderCell));
            writableSheet.addCell(new Label(3, row, "Logiware", columnHeaderCell));
            writableSheet.addCell(new Label(4, row, "Difference", columnHeaderCell));
            row++;
            writableSheet.addCell(new Label(1, row, "No. Of Records", noBorderCellBlackRight));
            writableSheet.addCell(new Number(2, row, controlReportForm.getNumberOfBluScreenRecords(), noBorderCellBlackRight));
            writableSheet.addCell(new Number(3, row, controlReportForm.getNumberOfLogiwareRecords(), noBorderCellBlackRight));
            int diffOfRecords = controlReportForm.getNumberOfBluScreenRecords() - controlReportForm.getNumberOfLogiwareRecords();
            writableSheet.addCell(new Number(4, row, diffOfRecords, noBorderCellBlackRight));
            row++;
            writableSheet.addCell(new Label(1, row, "Total Amount", noBorderCellBlackRight));
            writableSheet.addCell(new Number(2, row, controlReportForm.getTotalAmountInBlueScreen(), noBorderNumberCellBlackRight));
            writableSheet.addCell(new Number(3, row, controlReportForm.getTotalAmountInLogiware(), noBorderNumberCellBlackRight));
            double diffOfAmount = controlReportForm.getTotalAmountInBlueScreen() - controlReportForm.getTotalAmountInLogiware();
            writableSheet.addCell(new Number(4, row, diffOfAmount, noBorderNumberCellBlackRight));
            int blueScreenItemsSize = 0;
            int logiwareItemsSize = 0;
            double blueScreenTotal = 0d;
            double logiwareTotal = 0d;
            if (CommonUtils.isEqualIgnoreCase(controlReportForm.getReportType(), CommonConstants.TRANSACTION_TYPE_ACCRUALS)) {
                //Blue Screen
                row++;
                writableSheet.mergeCells(0, row, 4, row);
                writableSheet.addCell(new Label(0, row, "Blue Screen Detail", headerCell));
                if (CommonUtils.isNotEmpty(controlReportForm.getBlueScreenAccruals())) {
                    blueScreenItemsSize = controlReportForm.getBlueScreenAccruals().size();
                }
                row++;
                writableSheet.mergeCells(0, row, 4, row);
                writableSheet.addCell(new Label(0, row, blueScreenItemsSize + " Blue Screen Accruals missed/conflict in Logiware", noBorderCellBlackLeft));
                if (CommonUtils.isNotEmpty(controlReportForm.getBlueScreenAccruals())) {
                    row++;
                    writableSheet.addCell(new Label(0, row, "Vendor Name", columnHeaderCell));
                    writableSheet.addCell(new Label(1, row, "Vendor Number", columnHeaderCell));
                    writableSheet.addCell(new Label(2, row, "Invoice/Bl", columnHeaderCell));
                    writableSheet.addCell(new Label(3, row, "Amount", columnHeaderCell));
                    writableSheet.addCell(new Label(4, row, "Blue Screen Key", columnHeaderCell));
                    for (AccountingBean blueScreenAccrual : controlReportForm.getBlueScreenAccruals()) {
                        row++;
                        writableSheet.addCell(new Label(0, row, blueScreenAccrual.getVendorName(), noBorderCellBlackLeft));
                        writableSheet.addCell(new Label(1, row, blueScreenAccrual.getVendorNumber(), noBorderCellBlackLeft));
                        writableSheet.addCell(new Label(2, row, blueScreenAccrual.getInvoiceOrBl(), noBorderCellBlackLeft));
                        writableSheet.addCell(new Number(3, row, blueScreenAccrual.getAmount(), noBorderNumberCellBlackRight));
                        writableSheet.addCell(new Label(4, row, blueScreenAccrual.getApCostKey(), noBorderCellBlackLeft));
                        blueScreenTotal += blueScreenAccrual.getAmount();
                    }
                }
                row++;
                writableSheet.mergeCells(0, row, 2, row);
                writableSheet.addCell(new Label(0, row, "Total", noBorderCellBlackRight));
                writableSheet.addCell(new Number(3, row, blueScreenTotal, noBorderNumberCellBlackRight));

                //Logiware
                row++;
                writableSheet.mergeCells(0, row, 4, row);
                writableSheet.addCell(new Label(0, row, "Logiware Detail", headerCell));
                if (CommonUtils.isNotEmpty(controlReportForm.getLogiwareAccruals())) {
                    logiwareItemsSize = controlReportForm.getLogiwareAccruals().size();
                }
                row++;
                writableSheet.mergeCells(0, row, 4, row);
                writableSheet.addCell(new Label(0, row, logiwareItemsSize + " Logiware Accruals missed/conflict in Blue Screen", noBorderCellBlackLeft));
                if (CommonUtils.isNotEmpty(controlReportForm.getLogiwareAccruals())) {
                    row++;
                    writableSheet.addCell(new Label(0, row, "Vendor Name", columnHeaderCell));
                    writableSheet.addCell(new Label(1, row, "Vendor Number", columnHeaderCell));
                    writableSheet.addCell(new Label(2, row, "Invoice/Bl", columnHeaderCell));
                    writableSheet.addCell(new Label(3, row, "Amount", columnHeaderCell));
                    writableSheet.addCell(new Label(4, row, "Blue Screen Key", columnHeaderCell));
                    for (AccountingBean logiwareAccrual : controlReportForm.getLogiwareAccruals()) {
                        row++;
                        writableSheet.addCell(new Label(0, row, logiwareAccrual.getVendorName(), noBorderCellBlackLeft));
                        writableSheet.addCell(new Label(1, row, logiwareAccrual.getVendorNumber(), noBorderCellBlackLeft));
                        writableSheet.addCell(new Label(2, row, logiwareAccrual.getInvoiceOrBl(), noBorderCellBlackLeft));
                        writableSheet.addCell(new Number(3, row, logiwareAccrual.getAmount(), noBorderNumberCellBlackRight));
                        writableSheet.addCell(new Label(4, row, logiwareAccrual.getApCostKey(), noBorderCellBlackLeft));
                        logiwareTotal += logiwareAccrual.getAmount();
                    }
                }
                row++;
                writableSheet.mergeCells(0, row, 2, row);
                writableSheet.addCell(new Label(0, row, "Total", noBorderCellBlackRight));
                writableSheet.addCell(new Number(3, row, logiwareTotal, noBorderNumberCellBlackRight));
            } else {
                //Blue Screen
                row++;
                writableSheet.mergeCells(0, row, 4, row);
                writableSheet.addCell(new Label(0, row, "Blue Screen Detail", headerCell));
                if (CommonUtils.isNotEmpty(controlReportForm.getBlueScreenAccountReceivables())) {
                    blueScreenItemsSize = controlReportForm.getBlueScreenAccountReceivables().size();
                }
                row++;
                writableSheet.mergeCells(0, row, 4, row);
                writableSheet.addCell(new Label(0, row, blueScreenItemsSize + " Blue Screen Account Receivables missed/conflict in Logiware",
                        noBorderCellBlackLeft));
                if (CommonUtils.isNotEmpty(controlReportForm.getBlueScreenAccountReceivables())) {
                    row++;
                    writableSheet.mergeCells(0, row, 1, row);
                    writableSheet.addCell(new Label(0, row, "Vendor Name", columnHeaderCell));
                    writableSheet.addCell(new Label(2, row, "Vendor Number", columnHeaderCell));
                    writableSheet.addCell(new Label(3, row, "Invoice/Bl", columnHeaderCell));
                    writableSheet.addCell(new Label(4, row, "Amount", columnHeaderCell));
                    for (AccountingBean blueScreenAccountReceivable : controlReportForm.getBlueScreenAccountReceivables()) {
                        row++;
                        writableSheet.mergeCells(0, row, 1, row);
                        writableSheet.addCell(new Label(0, row, blueScreenAccountReceivable.getVendorName(), noBorderCellBlackLeft));
                        writableSheet.addCell(new Label(2, row, blueScreenAccountReceivable.getVendorNumber(), noBorderCellBlackLeft));
                        writableSheet.addCell(new Label(3, row, blueScreenAccountReceivable.getInvoiceOrBl(), noBorderCellBlackLeft));
                        writableSheet.addCell(new Number(4, row, blueScreenAccountReceivable.getAmount(), noBorderNumberCellBlackRight));
                        blueScreenTotal += blueScreenAccountReceivable.getAmount();
                    }
                }
                row++;
                writableSheet.mergeCells(0, row, 2, row);
                writableSheet.addCell(new Label(0, row, "Total", noBorderCellBlackRight));
                writableSheet.addCell(new Number(3, row, blueScreenTotal, noBorderNumberCellBlackRight));

                //Logiware
                row++;
                writableSheet.mergeCells(0, row, 4, row);
                writableSheet.addCell(new Label(0, row, "Logiware Detail", headerCell));
                if (CommonUtils.isNotEmpty(controlReportForm.getLogiwareAccountReceivables())) {
                    logiwareItemsSize = controlReportForm.getLogiwareAccountReceivables().size();
                }
                row++;
                writableSheet.mergeCells(0, row, 4, row);
                writableSheet.addCell(new Label(0, row, logiwareItemsSize + " Logiware Account Receivables missed/conflict in Blue Screen", noBorderCellBlackLeft));
                if (CommonUtils.isNotEmpty(controlReportForm.getLogiwareAccountReceivables())) {
                    row++;
                    writableSheet.mergeCells(0, row, 1, row);
                    writableSheet.addCell(new Label(0, row, "Vendor Name", columnHeaderCell));
                    writableSheet.addCell(new Label(2, row, "Vendor Number", columnHeaderCell));
                    writableSheet.addCell(new Label(3, row, "Invoice/Bl", columnHeaderCell));
                    writableSheet.addCell(new Label(4, row, "Amount", columnHeaderCell));
                    for (AccountingBean logiwareAccountReceivable : controlReportForm.getLogiwareAccountReceivables()) {
                        row++;
                        writableSheet.mergeCells(0, row, 1, row);
                        writableSheet.addCell(new Label(0, row, logiwareAccountReceivable.getVendorName(), noBorderCellBlackLeft));
                        writableSheet.addCell(new Label(2, row, logiwareAccountReceivable.getVendorNumber(), noBorderCellBlackLeft));
                        writableSheet.addCell(new Label(3, row, logiwareAccountReceivable.getInvoiceOrBl(), noBorderCellBlackLeft));
                        writableSheet.addCell(new Number(4, row, logiwareAccountReceivable.getAmount(), noBorderNumberCellBlackRight));
                        logiwareTotal += logiwareAccountReceivable.getAmount();
                    }
                }
                row++;
                writableSheet.mergeCells(0, row, 2, row);
                writableSheet.addCell(new Label(0, row, "Total", noBorderCellBlackRight));
                writableSheet.addCell(new Number(3, row, logiwareTotal, noBorderNumberCellBlackRight));
            }
            row++;
            writableSheet.mergeCells(0, row, 4, row);
            writableSheet.addCell(new Label(0, row, "Difference in Detail", headerCell));
            row++;
            writableSheet.addCell(new Label(2, row, "", columnHeaderCell));
            writableSheet.addCell(new Label(3, row, "No. Of Records", columnHeaderCell));
            writableSheet.addCell(new Label(4, row, "Total Amount", columnHeaderCell));
            row++;
            writableSheet.addCell(new Label(2, row, "Blue Screen", noBorderCellBlackRight));
            writableSheet.addCell(new Label(3, row, "" + blueScreenItemsSize, noBorderCellBlackRight));
            writableSheet.addCell(new Number(4, row, blueScreenTotal, noBorderNumberCellBlackRight));
            row++;
            writableSheet.addCell(new Label(2, row, "Logiware", noBorderCellBlackRight));
            writableSheet.addCell(new Label(3, row, "" + logiwareItemsSize, noBorderCellBlackRight));
            writableSheet.addCell(new Number(4, row, logiwareTotal, noBorderNumberCellBlackRight));
            row++;
            writableSheet.addCell(new Label(2, row, "Difference", noBorderCellBlackRight));
            writableSheet.addCell(new Label(3, row, "" + (blueScreenItemsSize - logiwareItemsSize), noBorderCellBlackRight));
            writableSheet.addCell(new Number(4, row, blueScreenTotal - logiwareTotal, noBorderNumberCellBlackRight));
        }
    }

    public String exportToExcel(ControlReportForm controlReportForm) throws Exception {
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/Accounting/ControlReport/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append(controlReportForm.getReportType()).append("ControlReport.xls");
        super.init(fileName.toString());
        this.generateExcelSheet(controlReportForm);
        super.write();
        super.close();
        return fileName.toString();
    }
}
