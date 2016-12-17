package com.logiware.common.reports;

import au.com.bytecode.opencsv.CSVWriter;
import com.gp.cong.common.DateUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class CsvCreator {

    private FileWriter fileWriter = null;
    private CSVWriter writer = null;

    private void init(String fileName) throws IOException {
	fileWriter = new FileWriter(fileName);
	writer = new CSVWriter(fileWriter);
    }

    private void write(List<String[]> data) {
	writer.writeAll(data);
    }

    private void exit() throws IOException {
	if (null != writer) {
	    writer.close();
	}
	if (null != fileWriter) {
	    fileWriter.close();
	}
    }

    public String create(String reportName, List<String[]> data) throws IOException, Exception {
	try {
	    StringBuilder fileName = new StringBuilder();
	    fileName.append(LoadLogisoftProperties.getProperty("reportLocation"));
	    fileName.append("/Reports/");
	    fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd/"));
	    File file = new File(fileName.toString());
	    if (!file.exists()) {
		file.mkdirs();
	    }
	    fileName.append(reportName).append("_").append(DateUtils.formatDate(new Date(), "yyyyMMdd_kkmmssSSS")).append(".csv");
	    init(fileName.toString());
	    write(data);
	    return fileName.toString();
	} catch (IOException e) {
	    throw e;
	} finally {
	    exit();
	}
    }
}
