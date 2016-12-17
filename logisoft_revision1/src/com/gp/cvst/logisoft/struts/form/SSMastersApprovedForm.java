/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form;

import java.util.List;
import org.apache.struts.action.ActionForm;
import com.logiware.bean.AccountingBean;

public class SSMastersApprovedForm extends ActionForm {

    private String moduleName;
    private String fileNo;
    private String sslineName;
    private String sslineNo;
    private String bookingNo;
    private String masterNo;
    private String containerNo;
    private String prepaidOrCollect;
    private String action;
    private List<AccountingBean> ssMastersApprovedList;
    private Integer pageNo = 1;
    private Integer currentPageSize = 100;
    private Integer totalPageSize = 0;
    private Integer noOfPages = 0;
    private Integer noOfRecords = 0;
    private String sortBy = "bl.file_no";
    private String orderBy = "desc";
    private String selectedFileNo;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Integer getCurrentPageSize() {
        return currentPageSize;
    }

    public void setCurrentPageSize(Integer currentPageSize) {
        this.currentPageSize = currentPageSize;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getMasterNo() {
        return masterNo;
    }

    public void setMasterNo(String masterNo) {
        this.masterNo = masterNo;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(Integer noOfPages) {
        this.noOfPages = noOfPages;
    }

    public Integer getNoOfRecords() {
        return noOfRecords;
    }

    public void setNoOfRecords(Integer noOfRecords) {
        this.noOfRecords = noOfRecords;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getPrepaidOrCollect() {
        return prepaidOrCollect;
    }

    public void setPrepaidOrCollect(String prepaidOrCollect) {
        this.prepaidOrCollect = prepaidOrCollect;
    }

    public String getSortBy() {
        return null != sortBy && !sortBy.equals("") ? sortBy : "bl.file_no";
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public List<AccountingBean> getSsMastersApprovedList() {
        return ssMastersApprovedList;
    }

    public void setSsMastersApprovedList(List<AccountingBean> ssMastersApprovedList) {
        this.ssMastersApprovedList = ssMastersApprovedList;
    }

    public String getSslineName() {
        return sslineName;
    }

    public void setSslineName(String sslineName) {
        this.sslineName = sslineName;
    }

    public String getSslineNo() {
        return sslineNo;
    }

    public void setSslineNo(String sslineNo) {
        this.sslineNo = sslineNo;
    }

    public Integer getTotalPageSize() {
        return totalPageSize;
    }

    public void setTotalPageSize(Integer totalPageSize) {
        this.totalPageSize = totalPageSize;
    }

    public String getSelectedFileNo() {
        return selectedFileNo;
    }

    public void setSelectedFileNo(String selectedFileNo) {
        this.selectedFileNo = selectedFileNo;
    }
}
