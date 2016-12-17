package com.logiware.form;

import com.logiware.hibernate.domain.TruckerRates;
import java.io.Serializable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Lakshmi Narayanan
 */
public class TruckerRatesForm extends ActionForm implements Serializable {

    private static final long serialVersionUID = 1L;
    private String action;
    private FormFile uploadFile;
    private boolean errorRates;
    private Integer limit = 200;
    private Integer selectedPage = 1;
    private Integer selectedRows = 0;
    private Integer totalPages = 0;
    private Integer totalRows = 0;
    private String sortBy = "tr.id";
    private String orderBy = "desc";
    private List<TruckerRates> truckerRatesList;
    private TruckerRates truckerRates;

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public FormFile getUploadFile() {
	return uploadFile;
    }

    public void setUploadFile(FormFile uploadFile) {
	this.uploadFile = uploadFile;
    }

    public Integer getLimit() {
	return limit;
    }

    public void setLimit(Integer limit) {
	this.limit = limit;
    }

    public String getOrderBy() {
	return orderBy;
    }

    public void setOrderBy(String orderBy) {
	this.orderBy = orderBy;
    }

    public Integer getSelectedPage() {
	return selectedPage;
    }

    public void setSelectedPage(Integer selectedPage) {
	this.selectedPage = selectedPage;
    }

    public Integer getSelectedRows() {
	return selectedRows;
    }

    public void setSelectedRows(Integer selectedRows) {
	this.selectedRows = selectedRows;
    }

    public String getSortBy() {
	return sortBy;
    }

    public void setSortBy(String sortBy) {
	this.sortBy = sortBy;
    }

    public Integer getTotalPages() {
	return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
	this.totalPages = totalPages;
    }

    public Integer getTotalRows() {
	return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
	this.totalRows = totalRows;
    }

    public boolean isErrorRates() {
	return errorRates;
    }

    public void setErrorRates(boolean errorRates) {
	this.errorRates = errorRates;
    }

    public List<TruckerRates> getTruckerRatesList() {
	return truckerRatesList;
    }

    public void setTruckerRatesList(List<TruckerRates> truckerRatesList) {
	this.truckerRatesList = truckerRatesList;
    }

    public TruckerRates getTruckerRates() {
	if (null == truckerRates) {
	    truckerRates = new TruckerRates();
	}
	return truckerRates;
    }

    public void setTruckerRates(TruckerRates truckerRates) {
	this.truckerRates = truckerRates;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
	this.errorRates = false;
    }
}
