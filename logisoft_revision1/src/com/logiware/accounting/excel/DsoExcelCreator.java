package com.logiware.accounting.excel;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.dao.ArReportsDAO;
import com.logiware.accounting.form.ArReportsForm;
import com.logiware.bean.ReportBean;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author Lakshmi Naryanan
 */
public class DsoExcelCreator extends BaseExcelCreator {

    private ArReportsForm arReportsForm;

    public DsoExcelCreator() {
    }

    public DsoExcelCreator(ArReportsForm arReportsForm) {
	this.arReportsForm = arReportsForm;
    }

    private void writeHeader() throws IOException {
	createRow();
	createHeaderCell("DSO Report", headerCellStyleCenterBold);
	mergeCells(rowIndex, rowIndex, 0, 1);
	row.setHeightInPoints(20);
    }

    private void writeContent() throws Exception {
	ReportBean dso = new ArReportsDAO().getDsoModel(arReportsForm);
	createRow();
	resetColumnIndex();
	createTextCell("DSO For : ", tableOddRowCellStyleRightBold);
	createTextCell(arReportsForm.getDsoFilter(), tableOddRowCellStyleLeftNormal);
	CellStyle textCellStyleRight = tableEvenRowCellStyleRightBold;
	CellStyle textCellStyleLeft = tableEvenRowCellStyleLeftNormal;
	if (CommonUtils.isEqual(arReportsForm.getDsoFilter(), "By Collector")) {
	    String collector = new UserDAO().getLoginName(Integer.parseInt(arReportsForm.getCollector()));
	    createRow();
	    resetColumnIndex();
	    createTextCell("Collector : ", tableEvenRowCellStyleRightBold);
	    createTextCell(collector, tableEvenRowCellStyleLeftNormal);
	    textCellStyleRight = tableOddRowCellStyleRightBold;
	    textCellStyleLeft = tableOddRowCellStyleLeftNormal;
	} else if (CommonUtils.isEqual(arReportsForm.getDsoFilter(), "By Customer")) {
	    createRow();
	    resetColumnIndex();
	    createTextCell("Customer Name : ", tableEvenRowCellStyleRightBold);
	    createTextCell(arReportsForm.getCustomerName(), tableEvenRowCellStyleLeftNormal);
	    createRow();
	    resetColumnIndex();
	    createTextCell("Customer Number : ", tableOddRowCellStyleRightBold);
	    createTextCell(arReportsForm.getCustomerNumber(), tableOddRowCellStyleLeftNormal);
	    textCellStyleRight = tableEvenRowCellStyleRightBold;
	    textCellStyleLeft = tableEvenRowCellStyleLeftNormal;
	}
	createRow();
	resetColumnIndex();
	createTextCell("Total Amount Open Receivables : ", textCellStyleRight);
	createTextCell(dso.getOpenReceivables(), textCellStyleLeft);
	textCellStyleRight = textCellStyleRight == tableEvenRowCellStyleRightBold ? tableOddRowCellStyleRightBold : tableEvenRowCellStyleRightBold;
	textCellStyleLeft = textCellStyleLeft == tableEvenRowCellStyleLeftNormal ? tableOddRowCellStyleLeftNormal : tableEvenRowCellStyleLeftNormal;
	createRow();
	resetColumnIndex();
	createTextCell("Total Amount Sales : ", textCellStyleRight);
	createTextCell(dso.getSales(), textCellStyleLeft);
	textCellStyleRight = textCellStyleRight == tableEvenRowCellStyleRightBold ? tableOddRowCellStyleRightBold : tableEvenRowCellStyleRightBold;
	textCellStyleLeft = textCellStyleLeft == tableEvenRowCellStyleLeftNormal ? tableOddRowCellStyleLeftNormal : tableEvenRowCellStyleLeftNormal;
	createRow();
	resetColumnIndex();
	createTextCell("Selected Period : ", textCellStyleRight);
	createTextCell(arReportsForm.getDsoPeriod(), textCellStyleLeft);
	textCellStyleRight = textCellStyleRight == tableEvenRowCellStyleRightBold ? tableOddRowCellStyleRightBold : tableEvenRowCellStyleRightBold;
	textCellStyleLeft = textCellStyleLeft == tableEvenRowCellStyleLeftNormal ? tableOddRowCellStyleLeftNormal : tableEvenRowCellStyleLeftNormal;
	createRow();
	resetColumnIndex();
	createTextCell("DSO Ratio : ", textCellStyleRight);
	createTextCell(dso.getDsoRatio(), textCellStyleLeft);
	setColumnAutoSize();
    }

    public String create() throws Exception {
	try{
	    StringBuilder fileNameBuilder = new StringBuilder();
	    fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/ArReports/");
            fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
	    File file = new File(fileNameBuilder.toString());
	    if (!file.exists()) {
		file.mkdirs();
	    }
	    fileNameBuilder.append("DSO.xlsx");
	    init(fileNameBuilder.toString(), "DSO");
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
