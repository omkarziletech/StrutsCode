/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.ExcelGenerator.BaseExcelGenerator;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.hibernate.dao.ArBatchDAO;
import java.awt.Color;
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Pattern;
import jxl.format.RGB;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

/**
 *
 * @author Administrator
 */
public class ExportNSInvoiceToExcel extends BaseExcelGenerator {

    private void generateExcelsheet(String batchId) throws Exception {
        ArBatchDAO arBatchDAO = new ArBatchDAO();
        List<Object[]> receivablesList = arBatchDAO.getReceivables(batchId);
        Object customerReference = arBatchDAO.getCustomerRef(batchId);
        List<Object[]> payablesList = arBatchDAO.getPayables(batchId);
        String sheetName = "NS Invoice Report";
        writableSheet = writableWorkbook.createSheet(sheetName, 0);
        WritableCellFormat grayHeaderCell = createCellFormat(wfBoldForColumns, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.CENTRE);
        grayHeaderCell.setBackground(Colour.GREY_25_PERCENT);
        WritableCellFormat columnHeaderCell = createCellFormat(wfBoldForColumns, null, Border.NONE, BorderLineStyle.NONE, Colour.BLACK, true, Alignment.CENTRE);
        columnHeaderCell.setBackground(Colour.BLUE_GREY);
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        String companyName = systemRulesDAO.getSystemRulesByCode("CompanyName");
        String companyAddress = systemRulesDAO.getSystemRulesByCode("CompanyAddress");
        String companyPhone = systemRulesDAO.getSystemRulesByCode("CompanyPhone");
        String companyFax = systemRulesDAO.getSystemRulesByCode("CompanyFax");
        String accNo = "", accName = "", address = "", invoiceNo = "", depositDate = "", date = "", notes = "";
        Double receivableTotal = 0d, payableTotal = 0d, netTotal = 0d;
        StringBuilder toAddress;
        int endCol = 4;
        writableSheet.setColumnView(0, 4);
        for (int col = 1; col <= endCol; col++) {
            writableSheet.setColumnView(col, 5);
        }
        int row = 0;
        writableSheet.setRowView(row, 400);
        writableSheet.mergeCells(0, row, endCol, row);
        writableSheet.addCell(new Label(0, row, companyName, noBorderHeaderCell));
        row++;
        writableSheet.mergeCells(0, row, endCol, row);
        StringBuilder addressess = new StringBuilder();
        addressess.append(companyAddress).append(". PHONE: ").append(companyPhone).append(". FAX: ").append(companyFax);
        writableSheet.addCell(new Label(0, row, addressess.toString(), noBoldCellCenter));
        /*Table Column Header*/
        row = row + 2;
        if (null != customerReference) {
            Object res[] = (Object[]) customerReference;
            accNo = null != res[0] ? res[0].toString() : "";
            accName = null != res[1] ? res[1].toString() : "";
            address = null != res[2] ? res[2].toString() : "";
            invoiceNo = null != res[3] ? res[3].toString() : "";
            depositDate = null != res[4] ? res[4].toString() : "";
            notes = null != res[5].toString() && !res[5].toString().equals("") ? res[5].toString() : "From NettSettlement " + batchId;
        }
        //For Receivables
        for (Object object : receivablesList) {
            Object result[] = (Object[]) object;
            String amount = null != result[1] ? result[1].toString().replace(",", "") : "";
            double recAmount = Double.parseDouble(amount);
            receivableTotal = receivableTotal + recAmount;
        }
        //For Payables
        for (Object payableObject : payablesList) {
            Object payableResult[] = (Object[]) payableObject;
            String payableAmount = null != payableResult[1].toString() ? payableResult[1].toString().replace(",", "") : "";
            double payAmount = Double.parseDouble(payableAmount);
            payableTotal = payableTotal + payAmount;
        }
        //netTotal
        netTotal = receivableTotal + payableTotal;
        String netFavour = netTotal >= 0 ? "INVOICE" : "CREDIT NOTE";
        toAddress = new StringBuilder();
        if (CommonUtils.isNotEmpty(depositDate)) {
            date = DateUtils.formatDate(DateUtils.parseDate(depositDate, "yyyy-MM-dd"), "MM/dd/yyyy");
        }
        toAddress.append(accName).append(" (").append(accNo).append(")").append(address);
        writableSheet.mergeCells(0, row, 1, row);
        //Customer Reference
        writableSheet.addCell(new Label(0, row, "TO", grayHeaderCell));
        writableSheet.addCell(new Label(2, row, netFavour, grayHeaderCell));
        writableSheet.addCell(new Label(3, row, ConstantsInterface.RECEIVABLES_DATE, grayHeaderCell));
        writableSheet.addCell(new Label(4, row, ConstantsInterface.RECEIVABLES_REF, grayHeaderCell));
        row++;
        if (CommonUtils.isNotEmpty(accNo)) {
            if (CommonUtils.isNotEmpty(address)) {
                writableSheet.setRowView(row, 2200);
            }
            writableSheet.mergeCells(0, row, 1, row);
            writableSheet.addCell(new Label(0, row, toAddress.toString(), noBordernoBoldCell));
            writableSheet.addCell(new Label(2, row, invoiceNo, noBorderCellBlackCenter));
            writableSheet.addCell(new Label(3, row, date, noBorderCellBlackCenter));
            writableSheet.addCell(new Label(4, row, notes, noBorderCellBlackCenter));
        }
        row++;
        writableSheet.mergeCells(0, row, 3, row);
        //netnetFavour
        netFavour = netFavour.equalsIgnoreCase("CREDIT NOTE") ? ConstantsInterface.NET_AMOUNT_YOUR_FAVOUR : ConstantsInterface.NET_AMOUNT_OUR_FAVOUR;
        WritableFont wfBoldLavendar = createFont(new WritableFont(WritableFont.ARIAL), 10, "BOLD", false, UnderlineStyle.NO_UNDERLINE, Colour.AUTOMATIC);
        WritableCellFormat lavendarBgCell = createCellFormat(wfBoldLavendar, null, Border.NONE, BorderLineStyle.THIN, Colour.AUTOMATIC, true, Alignment.RIGHT);
        lavendarBgCell.setBackground(Colour.TAN);

        writableSheet.addCell(new Label(0, row, netFavour, lavendarBgCell));
        writableSheet.addCell(new Number(4, row, netTotal, lavendarBgCell));
        row++;
        writableSheet.addCell(new Number(3, row, netTotal, noBorderNumberCellBoldBlackRight));

        writableSheet.addCell(new Label(0, row, ConstantsInterface.ECONOCARIBE_RECEIVABLES, grayHeaderCell));
        writableSheet.addCell(new Label(1, row, "", grayHeaderCell));
        writableSheet.addCell(new Label(3, row, ConstantsInterface.ECONOCARIBE_PAYABLES, grayHeaderCell));
        writableSheet.addCell(new Label(4, row, "", grayHeaderCell));

        row++;
        writableSheet.setColumnView(0, 30);
        writableSheet.setColumnView(1, 20);
        writableSheet.setColumnView(2, 16);
        writableSheet.setColumnView(3, 30);
        writableSheet.setColumnView(4, 25);

        writableSheet.addCell(new Label(0, row, ConstantsInterface.ECONOCARIBE_RECEIVABLES_INVOICE, grayHeaderCell));
        writableSheet.addCell(new Label(1, row, ConstantsInterface.RECEIVABLES_AMOUNT, grayHeaderCell));
        writableSheet.addCell(new Label(3, row, ConstantsInterface.ECONOCARIBE_PAYABLES_INVOICE, grayHeaderCell));
        writableSheet.addCell(new Label(4, row, ConstantsInterface.PAYABLES_AMOUNT, grayHeaderCell));

        /*Table Data*/
        row++;
        receivableTotal = 0d;
        payableTotal = 0d;
        int receivableRow = row;
        int payableRow = row;
        //For Receivables
        for (Object object : receivablesList) {
            Object result[] = (Object[]) object;
            String amount = null != result[1] ? result[1].toString().replace(",", "") : "";
            double recAmount = Double.parseDouble(amount);
            writableSheet.addCell(new Label(0, receivableRow, null != result[0] ? result[0].toString() : "", noBordernoBoldCell));
            writableSheet.addCell(new Number(1, receivableRow, recAmount, noBorderNumberCellBlackRight));
            receivableTotal = receivableTotal + recAmount;
            receivableRow++;
        }
        //For Payables
        for (Object payableObject : payablesList) {
            Object payableResult[] = (Object[]) payableObject;
            String payableAmount = null != payableResult[1].toString() ? payableResult[1].toString().replace(",", "") : "";
            double payAmount = Double.parseDouble(payableAmount);
            writableSheet.addCell(new Label(3, payableRow, null != payableResult[0] ? payableResult[0].toString() : "", noBordernoBoldCell));
            writableSheet.addCell(new Number(4, payableRow, payAmount, noBorderNumberCellBlackRight));
            payableTotal = payableTotal + payAmount;
            payableRow++;
        }
        row = receivableRow > payableRow ? receivableRow : payableRow;
        row++;
        writableSheet.addCell(new Label(0, row, ConstantsInterface.RECEIVABLES_TOTAL, noBorderBoldAlignRight));
        writableSheet.addCell(new Number(1, row, receivableTotal, noBorderNumberCellBoldBlackRight));
        writableSheet.addCell(new Label(3, row, ConstantsInterface.PAYABLES_TOTAL, noBorderBoldAlignRight));
        writableSheet.addCell(new Number(4, row, payableTotal, noBorderNumberCellBoldBlackRight));
    }

    public String exportToExcel(String batchId) throws Exception {
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
        fileName.append("/ArBatch/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdir();
        }
        fileName.append("NET_SETT_").append(batchId).append(".xls");
        super.init(fileName.toString());
        this.generateExcelsheet(batchId);
        super.write();
        super.close();
        return fileName.toString();
    }
}
