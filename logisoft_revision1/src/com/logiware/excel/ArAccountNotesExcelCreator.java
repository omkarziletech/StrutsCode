/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.excel;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.ArAccountNotesReportForm;
import com.logiware.bean.ReportBean;
import com.logiware.hibernate.dao.ArReportsDAO;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author logiware
 */
public class ArAccountNotesExcelCreator extends BaseExcelCreator implements ConstantsInterface {

    private ArAccountNotesReportForm arAccountNotesReportForm;

    public ArAccountNotesExcelCreator() {
    }

    public ArAccountNotesExcelCreator(ArAccountNotesReportForm arAccountNotesReportForm) {
        this.arAccountNotesReportForm = arAccountNotesReportForm;
    }

    private void writeHeader() throws IOException {
        createRow();
        createHeaderCell("AR Account Notes Report", headerCellStyleCenterBold);
        mergeCells(rowIndex, rowIndex, 0, 5);
        row.setHeightInPoints(20);
        createRow();
        resetColumnIndex();
        createHeaderCell("Customer Name", tableHeaderCellStyleCenterBold);
        createHeaderCell("Customer Number", tableHeaderCellStyleCenterBold);
        createHeaderCell("Notes Description", tableHeaderCellStyleCenterBold);
        createHeaderCell("Followup Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Created Date", tableHeaderCellStyleCenterBold);
        createHeaderCell("Created By", tableHeaderCellStyleCenterBold);
    }

    private void writeContent() throws Exception {
        List<ReportBean> arAccountNotesList = new ArReportsDAO().getArAccountNotesList(arAccountNotesReportForm);
        int rowCount = 0;
        for (ReportBean reportBean : arAccountNotesList) {
            createRow();
            resetColumnIndex();
            CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
            createTextCell(reportBean.getCustomerName(), textCellStyle);
            createTextCell(reportBean.getCustomerNumber(), textCellStyle);
            createTextCell(reportBean.getNotesDescription(), textCellStyle);
            createTextCell(reportBean.getFollowupDate(), textCellStyle);
            createTextCell(reportBean.getCreatedDate(), textCellStyle);
            createTextCell(reportBean.getCreatedBy(), textCellStyle);
            rowCount++;
        }
        setColumnAutoSize();
    }

    public String createExcel() throws Exception {
        try {
            StringBuilder fileName = new StringBuilder();
            fileName.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ArReports/");
            fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File file = new File(fileName.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            fileName.append(arAccountNotesReportForm.getReportType()).append(".xlsx");
            init(fileName.toString(), arAccountNotesReportForm.getReportType());
            writeHeader();
            writeContent();
            writeIntoFile();
            return fileName.toString();
        } catch (Exception e){
            throw e;
        } finally {
            exit();
        }
    }
}
