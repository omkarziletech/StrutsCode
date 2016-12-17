package com.logiware.form;

import com.logiware.hibernate.domain.ArMigrationLog;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ArMigrationLogForm extends ActionForm {

	private String action;
	private Integer recordsLimit = 100;
	private Integer currentPageNo = 1;
	private Integer currentNoOfRecords = 0;
	private Integer totalPageNo = 0;
	private Integer totalNoOfRecords = 0;
	private String sortBy = "reportedDate";
	private String orderBy = "desc";
	private String billOfLading;
	private String fileContent;
	private String fromDate;
	private Integer id;
	private String logType = "error";
	private List<ArMigrationLog> logs;
	private String toDate;
	private boolean missingCharges=false;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getCurrentNoOfRecords() {
		return currentNoOfRecords;
	}

	public void setCurrentNoOfRecords(Integer currentNoOfRecords) {
		this.currentNoOfRecords = currentNoOfRecords;
	}

	public Integer getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(Integer currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getRecordsLimit() {
		return recordsLimit;
	}

	public void setRecordsLimit(Integer recordsLimit) {
		this.recordsLimit = recordsLimit;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public Integer getTotalNoOfRecords() {
		return totalNoOfRecords;
	}

	public void setTotalNoOfRecords(Integer totalNoOfRecords) {
		this.totalNoOfRecords = totalNoOfRecords;
	}

	public Integer getTotalPageNo() {
		return totalPageNo;
	}

	public void setTotalPageNo(Integer totalPageNo) {
		this.totalPageNo = totalPageNo;
	}

	public String getBillOfLading() {
		return billOfLading;
	}

	public void setBillOfLading(String billOfLading) {
		this.billOfLading = billOfLading;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public List<ArMigrationLog> getLogs() {
		return logs;
	}

	public void setLogs(List<ArMigrationLog> logs) {
		this.logs = logs;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public boolean isMissingCharges() {
		return missingCharges;
	}

	public void setMissingCharges(boolean missingCharges) {
		this.missingCharges = missingCharges;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.missingCharges=false;
	}
}
