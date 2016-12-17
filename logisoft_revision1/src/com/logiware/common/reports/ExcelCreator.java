package com.logiware.common.reports;

import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.excel.BaseExcelCreator;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ExcelCreator extends BaseExcelCreator {

    private void writeHeader(String[] headers) throws IOException {
	createRow();
	resetColumnIndex();
	for (String header : headers) {
	    createHeaderCell(header, tableHeaderCellStyleCenterBold);
	}
    }

    private void writeContent(List<String[]> data) throws IOException {
	int rowCount = 0;
	for (String[] rowData : data) {
	    createRow();
	    resetColumnIndex();
	    if (rowCount % 2 == 0) {
		for (String column : rowData) {
		    createTextCell(column, tableEvenRowCellStyleLeftNormal);
		}
	    } else {
		for (String column : rowData) {
		    createTextCell(column, tableOddRowCellStyleLeftNormal);
		}
	    }
	    rowCount++;
	}
	setColumnAutoSize();
    }

    public String create(String reportName, boolean isHeader, List<String[]> data) throws Exception {
	try {
	    StringBuilder fileNameBuilder = new StringBuilder();
	    fileNameBuilder.append(LoadLogisoftProperties.getProperty("reportLocation"));
	    fileNameBuilder.append("/Reports/");
	    fileNameBuilder.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd/"));
	    File file = new File(fileNameBuilder.toString());
	    if (!file.exists()) {
		file.mkdirs();
	    }
	    fileNameBuilder.append(reportName).append("_").append(DateUtils.formatDate(new Date(), "yyyyMMdd_kkmmssSSS")).append(".xlsx");
	    init(fileNameBuilder.toString(), reportName);
	    if (isHeader) {
		writeHeader(data.get(0));
		data.remove(0);
	    }
	    writeContent(data);
	    writeIntoFile();
	    return fileNameBuilder.toString();
	} catch (Exception e){
	    throw e;
	} finally {
	    exit();
	}
    }
}
