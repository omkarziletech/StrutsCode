package com.logiware.accounting.excel;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.ArReportsDAO;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.bean.ReportBean;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ArNotesExcelCreator extends BaseExcelCreator implements ConstantsInterface {

    private ArReportsForm arReportsForm;

    public ArNotesExcelCreator() {
    }

    public ArNotesExcelCreator(ArReportsForm arReportsForm) {
	this.arReportsForm = arReportsForm;
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
	List<ReportBean> notes = new ArReportsDAO().getArNotes(arReportsForm);
	int rowCount = 0;
	for (ReportBean note : notes) {
	    createRow();
	    resetColumnIndex();
	    CellStyle textCellStyle = rowCount % 2 == 0 ? tableEvenRowCellStyleLeftNormal : tableOddRowCellStyleLeftNormal;
	    createTextCell(note.getCustomerName(), textCellStyle);
	    createTextCell(note.getCustomerNumber(), textCellStyle);
	    createTextCell(note.getNotesDescription(), textCellStyle);
	    createTextCell(note.getFollowupDate(), textCellStyle);
	    createTextCell(note.getCreatedDate(), textCellStyle);
	    createTextCell(note.getCreatedBy(), textCellStyle);
	    rowCount++;
	}
	setColumnAutoSize();
    }

    public String create() throws Exception{
	try {
	    StringBuilder fileNameBuilder = new StringBuilder();
	    fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ArReports/");
            fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	    File file = new File(fileNameBuilder.toString());
	    if (!file.exists()) {
		file.mkdirs();
	    }
	    fileNameBuilder.append("AR Account Notes.xlsx");
	    init(fileNameBuilder.toString(), "AR Account Notes");
	    writeHeader();
	    writeContent();
	    writeIntoFile();
	    return fileNameBuilder.toString();
	} catch (Exception e) {
	    throw e;
	} finally {
	    exit();
	}
    }
}
