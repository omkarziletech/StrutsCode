/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.bean.ReportBean;
import com.logiware.form.GlReportsForm;
import com.logiware.hibernate.dao.GlReportsDAO;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 *
 * @author logiware
 */
public class GlReportsExcelCreator extends BaseExcelCreator implements ConstantsInterface {

    private GlReportsForm glReportsForm;

    public GlReportsExcelCreator() {
    }

    public GlReportsExcelCreator(GlReportsForm glReportsForm) {
        this.glReportsForm = glReportsForm;
    }

    private void writeChargeCodeHeader() throws IOException {
        createRow();
        createHeaderCell("Charge Code Report", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 12);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("Charge Code : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(glReportsForm.getChargeCode(), subHeaderOneCellStyleLeftNormal);
        createHeaderCell("Date Range : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(glReportsForm.getFromDate() + "-" + glReportsForm.getToDate(), subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 3, 12);
        createRow();
        resetColumnIndex();
        createHeaderCell("TP Name", tableHeaderCellStyleCenterBold);
        createHeaderCell("TP Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Invoice", tableHeaderCellStyleCenterBold);
        createHeaderCell("B/L", tableHeaderCellStyleCenterBold);
        createHeaderCell("Voyage", tableHeaderCellStyleCenterBold);
        createHeaderCell("D/R", tableHeaderCellStyleCenterBold);
        createHeaderCell("Enter Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Reporting Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Posted Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Revenue", tableHeaderCellStyleCenterBold);
        createHeaderCell("Cost", tableHeaderCellStyleCenterBold);
        createHeaderCell("User", tableHeaderCellStyleCenterBold);
        createHeaderCell("Type", tableHeaderCellStyleCenterBold);
    }

    private void writeGlCodeHeader() throws IOException {
        createRow();
        createHeaderCell("GL Code Report", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 13);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("GL Account : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(glReportsForm.getGlAccount(), subHeaderOneCellStyleLeftNormal);
        createHeaderCell("Date Range : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(glReportsForm.getFromDate() + "-" + glReportsForm.getToDate(), subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 3, 13);
        createRow();
        resetColumnIndex();
        createHeaderCell("G/L", tableHeaderCellStyleCenterBold);
        createHeaderCell("TP Name", tableHeaderCellStyleCenterBold);
        createHeaderCell("TP Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Invoice", tableHeaderCellStyleCenterBold);
        createHeaderCell("B/L", tableHeaderCellStyleCenterBold);
        createHeaderCell("Voyage", tableHeaderCellStyleCenterBold);
        createHeaderCell("D/R", tableHeaderCellStyleCenterBold);
        createHeaderCell("Transaction Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Reporting Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Posted Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Amount", tableHeaderCellStyleCenterBold);
        createHeaderCell("User", tableHeaderCellStyleCenterBold);
        createHeaderCell("Type", tableHeaderCellStyleCenterBold);
        createHeaderCell("JE Batch", tableHeaderCellStyleCenterBold);
    }

    private void writeCashHeader() throws IOException {
        createRow();
        createHeaderCell("Cash Report", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 4);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("Reporting date", subHeaderOneCellStyleLeftBold);
        createHeaderCell(glReportsForm.getReportingDate(), subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 1, 4);
        createRow();
        resetColumnIndex();
        createHeaderCell("G/L", tableHeaderCellStyleCenterBold);
        createHeaderCell("Account name", tableHeaderCellStyleCenterBold);
        createHeaderCell("G/L balance", tableHeaderCellStyleCenterBold);
        createHeaderCell("Bank balance", tableHeaderCellStyleCenterBold);
        createHeaderCell("Last reconcile date", tableHeaderCellStyleCenterBold);
    }

    private void writeFclPlHeader() throws IOException {
        createRow();
        createHeaderCell("FCL PL Report", headerCellStyleLeftBold);
        mergeCells(rowIndex, rowIndex, 0, 19);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("Report Type : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(glReportsForm.getReportType(), subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 1, 19);
        createRow();
        resetColumnIndex();
        createHeaderCell("GL Period : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(glReportsForm.getGlPeriod(), subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 1, 19);
        createRow();
        resetColumnIndex();
        createHeaderCell("Date Range : ", subHeaderOneCellStyleLeftBold);
        createHeaderCell(glReportsForm.getFromDate() + "-" + glReportsForm.getToDate(), subHeaderOneCellStyleLeftNormal);
        mergeCells(rowIndex, rowIndex, 1, 19);
        createRow();
        resetColumnIndex();
        XSSFColor PINK = createColor("#c91ac9");
        XSSFColor GREEN = createColor("#92d050");
        XSSFColor BLUE = createColor("#4bacc6");
        XSSFColor BROWN = createColor("#c0504d");
        CellStyle pinkCellStyle = createCellStyle(PINK, blackBoldFont10, CellStyle.ALIGN_CENTER);
        CellStyle greenCellStyle = createCellStyle(GREEN, blackBoldFont10, CellStyle.ALIGN_CENTER);
        CellStyle blueCellStyle = createCellStyle(BLUE, blackBoldFont10, CellStyle.ALIGN_CENTER);
        CellStyle brownCellStyle = createCellStyle(BROWN, blackBoldFont10, CellStyle.ALIGN_CENTER);
        createHeaderCell("File", pinkCellStyle);
        createHeaderCell("Reporting Date", pinkCellStyle);
        createHeaderCell("Total Revenue", greenCellStyle);
        createHeaderCell("Total Cost", greenCellStyle);
        createHeaderCell("Profit", greenCellStyle);
        createHeaderCell("O/F Rev", blueCellStyle);
        createHeaderCell("Deferral", blueCellStyle);
        createHeaderCell("O/F Cost", blueCellStyle);
        createHeaderCell("Oversold", blueCellStyle);
        createHeaderCell("Truck Rev", brownCellStyle);
        createHeaderCell("Truck Cost", brownCellStyle);
        createHeaderCell("Whs Rev", blueCellStyle);
        createHeaderCell("Whs Cost", blueCellStyle);
        createHeaderCell("Doc Fee", brownCellStyle);
        createHeaderCell("FFcom", blueCellStyle);
        createHeaderCell("FAE", brownCellStyle);
        createHeaderCell("Exch Rev", blueCellStyle);
        createHeaderCell("Exch Cost", blueCellStyle);
        createHeaderCell("Other Rev", brownCellStyle);
        createHeaderCell("Other Cost", brownCellStyle);
    }

    private void writeEcuMappingHeader() throws Exception {
        if (CommonUtils.isEqualIgnoreCase(glReportsForm.getReportType(), "mapping")) {
            createRow();
            createHeaderCell("ECU Account Mapping", headerCellStyleLeftBold);
            mergeCells(rowIndex, rowIndex, 0, 2);
            row.setHeightInPoints(20);
            createRow();
            resetColumnIndex();
            createHeaderCell("Date : " + DateUtils.formatDate(new Date(), "MM/dd/yyyy hh:mm:ss a"), subHeaderOneCellStyleLeftBold);
            mergeCells(rowIndex, rowIndex, 0, 2);
            createRow();
            resetColumnIndex();
            createHeaderCell("G/L", tableHeaderCellStyleCenterBold);
            createHeaderCell("Description", tableHeaderCellStyleCenterBold);
            createHeaderCell("ECU Report Category", tableHeaderCellStyleCenterBold);
        } else {
            createRow();
            createHeaderCell("ECU Account Report", headerCellStyleCenterBold);
            mergeCells(rowIndex, rowIndex, 0, 13);
            row.setHeightInPoints(20);
            createRow();
            resetColumnIndex();
            createHeaderCell("ECU Account : ", subHeaderOneCellStyleLeftBold);
            createHeaderCell(glReportsForm.getReportCategory(), subHeaderOneCellStyleLeftNormal);
            createHeaderCell("Date Range : ", subHeaderOneCellStyleLeftBold);
            createHeaderCell(glReportsForm.getFromDate() + "-" + glReportsForm.getToDate(), subHeaderOneCellStyleLeftNormal);
            mergeCells(rowIndex, rowIndex, 3, 13);
            createRow();
            resetColumnIndex();
            createHeaderCell("G/L", tableHeaderCellStyleCenterBold);
            createHeaderCell("TP Name", tableHeaderCellStyleCenterBold);
            createHeaderCell("TP Number", tableHeaderCellStyleCenterBold);
            createHeaderCell("Invoice", tableHeaderCellStyleCenterBold);
            createHeaderCell("B/L", tableHeaderCellStyleCenterBold);
            createHeaderCell("Voyage", tableHeaderCellStyleCenterBold);
            createHeaderCell("D/R", tableHeaderCellStyleCenterBold);
            createHeaderCell("Transaction Date", tableHeaderCellStyleCenterBold);
            createHeaderCell("Reporting Date", tableHeaderCellStyleCenterBold);
            createHeaderCell("Posted Date", tableHeaderCellStyleCenterBold);
            createHeaderCell("Amount", tableHeaderCellStyleCenterBold);
            createHeaderCell("User", tableHeaderCellStyleCenterBold);
            createHeaderCell("Type", tableHeaderCellStyleCenterBold);
            createHeaderCell("JE Batch", tableHeaderCellStyleCenterBold);
        }
    }

    private void writeHeader() throws Exception {
        if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "chargeCode")) {
            writeChargeCodeHeader();
        } else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "glCode")) {
            writeGlCodeHeader();
        } else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "cash")) {
            writeCashHeader();
        } else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "fclPl")) {
            writeFclPlHeader();
        } else {
            writeEcuMappingHeader();
        }
    }

    private void writeChargeCodeContent() throws Exception {
        List<ReportBean> disputedItems = new GlReportsDAO().getChargeCodes(glReportsForm);
        int rowCount = 0;
        double totalRevenue = 0d;
        double totalCost = 0d;
        for (ReportBean reportBean : disputedItems) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(reportBean.getCustomerName(), textCellStyle);
            createTextCell(reportBean.getCustomerNumber(), textCellStyle);
            createTextCell(reportBean.getInvoiceNumber(), textCellStyle);
            createTextCell(reportBean.getBlNumber(), textCellStyle);
            createTextCell(reportBean.getVoyageNumber(), textCellStyle);
            createTextCell(reportBean.getDockReceipt(), textCellStyle);
            createTextCell(reportBean.getCreatedDate(), textCellStyle);
            createTextCell(reportBean.getReportingDate(), textCellStyle);
            createTextCell(reportBean.getPostedDate(), textCellStyle);
            createDoubleCell(reportBean.getRevenue(), doubleCellStyle);
            createDoubleCell(reportBean.getCost(), doubleCellStyle);
            createTextCell(reportBean.getCreatedBy(), textCellStyle);
            createTextCell(reportBean.getType(), textCellStyle);
            totalRevenue += Double.parseDouble(reportBean.getRevenue().replace(",", ""));
            totalCost += Double.parseDouble(reportBean.getCost().replace(",", ""));
            rowCount++;
        }
        setColumnAutoSize();
        createRow();
        resetColumnIndex();
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createTextCell("Grand Total", darkAshCellStyleRightBold);
        createDoubleCell(totalRevenue, darkAshCellStyleRightBold);
        createDoubleCell(totalCost, darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
    }

    private void writeGlCodeContent() throws Exception {
        List<ReportBean> glCodes = new GlReportsDAO().getGlCodes(glReportsForm);
        int rowCount = 0;
        double totalAmount = 0d;
        for (ReportBean reportBean : glCodes) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(reportBean.getAccount(), textCellStyle);
            createTextCell(reportBean.getCustomerName(), textCellStyle);
            createTextCell(reportBean.getCustomerNumber(), textCellStyle);
            createTextCell(reportBean.getInvoiceNumber(), textCellStyle);
            createTextCell(reportBean.getBlNumber(), textCellStyle);
            createTextCell(reportBean.getVoyageNumber(), textCellStyle);
            createTextCell(reportBean.getDockReceipt(), textCellStyle);
            createTextCell(reportBean.getCreatedDate(), textCellStyle);
            createTextCell(reportBean.getReportingDate(), textCellStyle);
            createTextCell(reportBean.getPostedDate(), textCellStyle);
            createDoubleCell(reportBean.getAmount(), doubleCellStyle);
            createTextCell(reportBean.getCreatedBy(), textCellStyle);
            createTextCell(reportBean.getType(), textCellStyle);
            createTextCell(reportBean.getJeBatch(), textCellStyle);
            totalAmount += Double.parseDouble(reportBean.getAmount().replace(",", ""));
            rowCount++;
        }
        setColumnAutoSize();
        createRow();
        resetColumnIndex();
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createTextCell("Grand Total", darkAshCellStyleRightBold);
        createDoubleCell(totalAmount, darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
        createEmptyCell(darkAshCellStyleRightBold);
    }

    private void writeCashContent() throws Exception {
        List<ReportBean> cashAccounts = new GlReportsDAO().getCashAccounts(glReportsForm);
        int rowCount = 0;
        for (ReportBean cashAccount : cashAccounts) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            CellStyle doubleCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleRightNormal : tableOddRowCellStyleRightNormal;
            createTextCell(cashAccount.getGlAccountNo(), textCellStyle);
            createTextCell(cashAccount.getAccountName(), textCellStyle);
            createDoubleCell(cashAccount.getGlBalance(), doubleCellStyle);
            createDoubleCell(cashAccount.getBankBalance(), doubleCellStyle);
            createTextCell(cashAccount.getLastReconciledDate(), textCellStyle);
            rowCount++;
        }
        setColumnAutoSize();
    }

    private void writeFclPlContent() throws Exception {
        List<ReportBean> fclPlFiles = new GlReportsDAO().getFclPlFiles(glReportsForm);
        int rowCount = 0;
        double sales = 0d;
        double costs = 0d;
        double profits = 0d;
        double oceanfreightRevenue = 0d;
        double oceanfreightDeferral = 0d;
        double oceanfreightCost = 0d;
        double oceanfreightOversold = 0d;
        double drayRevenue = 0d;
        double drayCost = 0d;
        double warehouseRevenue = 0d;
        double warehouseCost = 0d;
        double documentRevenue = 0d;
        double ffcomCost = 0d;
        double faeCost = 0d;
        double passRevenue = 0d;
        double passCost = 0d;
        double otherRevenue = 0d;
        double otherCost = 0d;
        for (ReportBean fclPlFile : fclPlFiles) {
            createRow();
            resetColumnIndex();
            if (rowCount % 2 == 0) {
                createTextCell(fclPlFile.getFile(), tableEvenRowCellStyleLeftNormal);
                createTextCell(fclPlFile.getReportingDate(), tableEvenRowCellStyleLeftNormal);
                createDoubleCell(fclPlFile.getSales(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getCosts(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getProfits(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getOceanfreightRevenue(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getOceanfreightDeferral(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getOceanfreightCost(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getOceanfreightOversold(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getDrayRevenue(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getDrayCost(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getWarehouseRevenue(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getWarehouseCost(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getDocumentRevenue(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getFfcomCost(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getFaeCost(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getPassRevenue(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getPassCost(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getOtherRevenue(), tableEvenRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getOtherCost(), tableEvenRowCellStyleRightNormal);
            } else {
                createTextCell(fclPlFile.getFile(), tableOddRowCellStyleLeftNormal);
                createTextCell(fclPlFile.getReportingDate(), tableOddRowCellStyleLeftNormal);
                createDoubleCell(fclPlFile.getSales(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getCosts(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getProfits(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getOceanfreightRevenue(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getOceanfreightDeferral(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getOceanfreightCost(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getOceanfreightOversold(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getDrayRevenue(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getDrayCost(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getWarehouseRevenue(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getWarehouseCost(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getDocumentRevenue(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getFfcomCost(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getFaeCost(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getPassRevenue(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getPassCost(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getOtherRevenue(), tableOddRowCellStyleRightNormal);
                createDoubleCell(fclPlFile.getOtherCost(), tableOddRowCellStyleRightNormal);
            }
            sales += Double.parseDouble(fclPlFile.getSales().replace(",", ""));
            costs += Double.parseDouble(fclPlFile.getCosts().replace(",", ""));
            profits += Double.parseDouble(fclPlFile.getProfits().replace(",", ""));
            oceanfreightRevenue += Double.parseDouble(fclPlFile.getOceanfreightRevenue().replace(",", ""));
            oceanfreightDeferral += Double.parseDouble(fclPlFile.getOceanfreightDeferral().replace(",", ""));
            oceanfreightCost += Double.parseDouble(fclPlFile.getOceanfreightCost().replace(",", ""));
            oceanfreightOversold += Double.parseDouble(fclPlFile.getOceanfreightOversold().replace(",", ""));
            drayRevenue += Double.parseDouble(fclPlFile.getDrayRevenue().replace(",", ""));
            drayCost += Double.parseDouble(fclPlFile.getDrayCost().replace(",", ""));
            warehouseRevenue += Double.parseDouble(fclPlFile.getWarehouseRevenue().replace(",", ""));
            warehouseCost += Double.parseDouble(fclPlFile.getWarehouseCost().replace(",", ""));
            documentRevenue += Double.parseDouble(fclPlFile.getDocumentRevenue().replace(",", ""));
            ffcomCost += Double.parseDouble(fclPlFile.getFfcomCost().replace(",", ""));
            faeCost += Double.parseDouble(fclPlFile.getFaeCost().replace(",", ""));
            passRevenue += Double.parseDouble(fclPlFile.getPassRevenue().replace(",", ""));
            passCost += Double.parseDouble(fclPlFile.getPassCost().replace(",", ""));
            otherRevenue += Double.parseDouble(fclPlFile.getOtherRevenue().replace(",", ""));
            otherCost += Double.parseDouble(fclPlFile.getOtherCost().replace(",", ""));
            rowCount++;
        }
        createRow();
        resetColumnIndex();
        createTextCell("Total:", darkAshCellStyleRightBold);
        mergeCells(rowIndex, rowIndex, 0, 1);
        createDoubleCell(sales, darkAshCellStyleRightBold);
        createDoubleCell(costs, darkAshCellStyleRightBold);
        createDoubleCell(profits, darkAshCellStyleRightBold);
        createDoubleCell(oceanfreightRevenue, darkAshCellStyleRightBold);
        createDoubleCell(oceanfreightDeferral, darkAshCellStyleRightBold);
        createDoubleCell(oceanfreightCost, darkAshCellStyleRightBold);
        createDoubleCell(oceanfreightOversold, darkAshCellStyleRightBold);
        createDoubleCell(drayRevenue, darkAshCellStyleRightBold);
        createDoubleCell(drayCost, darkAshCellStyleRightBold);
        createDoubleCell(warehouseRevenue, darkAshCellStyleRightBold);
        createDoubleCell(warehouseCost, darkAshCellStyleRightBold);
        createDoubleCell(documentRevenue, darkAshCellStyleRightBold);
        createDoubleCell(ffcomCost, darkAshCellStyleRightBold);
        createDoubleCell(faeCost, darkAshCellStyleRightBold);
        createDoubleCell(passRevenue, darkAshCellStyleRightBold);
        createDoubleCell(passCost, darkAshCellStyleRightBold);
        createDoubleCell(otherRevenue, darkAshCellStyleRightBold);
        createDoubleCell(otherCost, darkAshCellStyleRightBold);
        setColumnAutoSize();
    }

    private void writeEcuMappingContent() throws Exception {
        if (CommonUtils.isEqualIgnoreCase(glReportsForm.getReportType(), "mapping")) {
            List<ReportBean> ecuMappings = new GlReportsDAO().getEcuMappings(glReportsForm);
            int rowCount = 0;
            for (ReportBean ecuMapping : ecuMappings) {
                createRow();
                resetColumnIndex();
                CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
                createTextCell(ecuMapping.getGlAccountNo(), textCellStyle);
                createTextCell(ecuMapping.getDescription(), textCellStyle);
                createTextCell(ecuMapping.getReportCategory(), textCellStyle);
                rowCount++;
            }
        } else {
            writeGlCodeContent();
        }
        setColumnAutoSize();
    }

    private void writeContent() throws Exception {
        if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "chargeCode")) {
            writeChargeCodeContent();
        } else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "glCode")) {
            writeGlCodeContent();
        } else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "cash")) {
            writeCashContent();
        } else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "fclPl")) {
            writeFclPlContent();
        } else {
            writeEcuMappingContent();
        }
    }

    public String create() throws Exception {
        try {
            String sheetName = "";
            StringBuilder fileName = new StringBuilder();
            fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/GLReports/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileName.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "chargeCode")) {
                fileName.append("ChargeCodeReport.xlsx");
                sheetName = "Charge Code Report";
            } else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "glCode")) {
                fileName.append("GLCodeReport.xlsx");
                sheetName = "GL Code Report";
            } else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "cash")) {
                fileName.append("CashReport.xlsx");
                sheetName = "Cash Report";
            } else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "fclPl")) {
                fileName.append("Fcl_Pl_").append(glReportsForm.getReportType().replace(" ", "_")).append(".xlsx");
                sheetName = "FCL PL Reports";
            } else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "ecuMapping")
                    && CommonUtils.isEqualIgnoreCase(glReportsForm.getReportType(), "mapping")) {
                fileName.append("ECU_Account_Mapping.xlsx");
                sheetName = "ECU Account Mapping";
            } else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "ecuMapping")) {
                fileName.append("ECU_Account_Reports.xlsx");
                sheetName = "ECU Account Reports";
            }
            init(fileName.toString(), sheetName);
            writeHeader();
            writeContent();
            writeIntoFile();
            return fileName.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            exit();
        }
    }
}
