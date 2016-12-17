package com.gp.cong.logisoft.reports.dto;

import java.util.HashMap;

import com.gp.cong.logisoft.reports.data.ReportSource;



public class ReportDTO {
	
	// tells which report to use
	private String compiledReport;
	// the file name to save it as i.e. shipping_advise.pdf
	private String fileName;
	// parameters used for the report
	private HashMap parameters;
	// boolean stating whether the data source is required
	private boolean dataSourceRequired;
	// the report datasource to be used
	private ReportSource reportDataSource;
	// tells the report wheter to create a file in the file system or not
	// true = create file false = don't create file
	private boolean saveFile;
	
	public boolean isSaveFile() {
		return saveFile;
	}
	public void setSaveFile(boolean saveFile) {
		this.saveFile = saveFile;
	}
	public String getCompiledReport() {
		return compiledReport;
	}
	public void setCompiledReport(String compiledReport) {
		this.compiledReport = compiledReport;
	}
	public boolean isDataSourceRequired() {
		return dataSourceRequired;
	}
	public void setDataSourceRequired(boolean dataSourceRequired) {
		this.dataSourceRequired = dataSourceRequired;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public HashMap getParameters() {
		return parameters;
	}
	public void setParameters(HashMap parameters) {
		this.parameters = parameters;
	}
	public ReportSource getReportDataSource() {
		return reportDataSource;
	}
	public void setReportSource(ReportSource reportDataSource) {
		this.reportDataSource = reportDataSource;
	}

}
