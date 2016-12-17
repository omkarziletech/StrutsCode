/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.bc.accounting;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.DsoReportForm;
import com.logiware.bean.ReportBean;
import com.logiware.excel.ExportArReportsToExcel;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.reports.ArReportsCreator;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author logiware
 */
public class ARReportsBC implements ConstantsInterface {

    public String printArReports(DsoReportForm dsoReportForm, String realPath) throws Exception {
        List<ReportBean> arDsoList = new ArrayList<ReportBean>();
        String reportFileName = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/ArReports/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(reportFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        reportFileName += dsoReportForm.getReportType() + ".pdf";
        if (CommonUtils.isEqualIgnoreCase(dsoReportForm.getReportType(), AR_DSO_REPORT)) {
            arDsoList = new AccountingTransactionDAO().getArDsoList(dsoReportForm);
            return new ArReportsCreator().createArReports(dsoReportForm, arDsoList, reportFileName, realPath);
        } else {
            return null;
        }
    }

    public String exportToExcelApReports(DsoReportForm dsoReportForm) throws Exception {
        List<ReportBean> arDsoList = new ArrayList<ReportBean>();
        arDsoList = new AccountingTransactionDAO().getArDsoList(dsoReportForm);
        String excelFileName = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/ArReports/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(excelFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        excelFileName += dsoReportForm.getReportType() + ".xls";
        return new ExportArReportsToExcel().exportToExcel(excelFileName, dsoReportForm, arDsoList);
    }
}
