package com.logiware.referencedata.form;

import com.gp.cong.logisoft.domain.GenericCode;
import java.io.Serializable;

/**
 *
 * @author Lakshmi Narayanan
 */
public class SessionForm implements Serializable {

    private String action;
    private Integer limit = 100;
    private Integer selectedPage = 1;
    private Integer selectedRows = 0;
    private Integer totalPages = 0;
    private Integer totalRows = 0;
    private String sortBy = "id";
    private String orderBy = "asc";
    private GenericCode genericCode;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
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

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public GenericCode getGenericCode() {
        if (null == genericCode) {
            genericCode = new GenericCode();
        }
        return genericCode;
    }

    public void setGenericCode(GenericCode genericCode) {
        this.genericCode = genericCode;
    }
}
