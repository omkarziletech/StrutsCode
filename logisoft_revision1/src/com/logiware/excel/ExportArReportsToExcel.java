/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.excel;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.ExcelGenerator.BaseExcelGenerator;
import com.gp.cvst.logisoft.struts.form.DsoReportForm;
import com.logiware.bean.ReportBean;
import java.util.List;
import jxl.write.Label;

/**
 *
 * @author logiware
 */
public class ExportArReportsToExcel extends BaseExcelGenerator {

    int row = 0;

    private boolean generateExcelSheet(DsoReportForm dsoReportForm, List<ReportBean> arDsoList)throws Exception {
        String sheetName = "AR Report";
        if (null != dsoReportForm.getReportType() && !dsoReportForm.getReportType().trim().equals("")) {
            sheetName = dsoReportForm.getReportType();
        }
        writableSheet = writableWorkbook.createSheet(sheetName, 0);
            if (CommonUtils.isEqualIgnoreCase(dsoReportForm.getReportType(), CommonConstants.AR_DSO_REPORT)) {
                this.generateDpoWorkSheet(dsoReportForm, arDsoList);
            }
            return true;
    }

    private void generateDpoWorkSheet(DsoReportForm dsoReportForm, List<ReportBean> arDsoList)throws Exception {
            row++;
            int col = 0;
            writableSheet.mergeCells(0, row, 3, row);
            writableSheet.addCell(new Label(0, row, "DSO For : " + dsoReportForm.getSearchDsoBy(), headerCell));
            row += 2;
            writableSheet.setColumnView(0, 40);
            writableSheet.setColumnView(1, 30);
            writableSheet.setColumnView(2, 20);
            writableSheet.setColumnView(3, 20);
            writableSheet.addCell(new Label(col++, row, "Total Amount Open Receivables", headerCell));
            writableSheet.addCell(new Label(col++, row, "Total Amount Sales", headerCell));
            writableSheet.addCell(new Label(col++, row, "Selected Period", headerCell));
            writableSheet.addCell(new Label(col++, row, "DSO ratio", headerCell));
            for (ReportBean reportBean : arDsoList) {
                row++;
                col = 0;
                writableSheet.addCell(new Label(col++, row, reportBean.getOpenReceivables(), noBorderCellBlackRight));
                writableSheet.addCell(new Label(col++, row, reportBean.getTotalAmount(), noBorderCellBlackRight));
                writableSheet.addCell(new Label(col++, row, reportBean.getNumberOfDays(), noBorderCellBlackRight));
                writableSheet.addCell(new Label(col++, row, reportBean.getDsoRatio(), noBorderCellBlackRight));
            }
    }

    public String exportToExcel(String excelFilePath, DsoReportForm dsoReportForm, List<ReportBean> arDsoList)throws Exception {
        String excelFileName = null;
        if (null != excelFilePath) {
            init(excelFilePath);
            if (null != writableWorkbook) {
                if (this.generateExcelSheet(dsoReportForm, arDsoList)) {
                    try {
                        write();
                        close();
                        excelFileName = excelFilePath;
                    } catch (Exception e) {
                        excelFileName = null;
                        throw e;
                    }
                }
            }
        }
        return excelFileName;
    }
}
