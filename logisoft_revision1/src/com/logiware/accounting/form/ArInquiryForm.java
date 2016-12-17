package com.logiware.accounting.form;

import com.logiware.accounting.model.CustomerModel;
import com.logiware.accounting.model.VendorModel;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ArInquiryForm extends BaseForm {

    private String searchFilter;
    private String searchValue1;
    private String searchValue2;
    private String searchValue3;
    private String searchValue4;
    private String searchValue5;
    private String fromAmount6 = "0.00";
    private String toAmount6 = "0.00";
    private String fromAmount7 = "0.00";
    private String toAmount7 = "0.00";
    private String fromAmount8 = "0.00";
    private String toAmount8 = "0.00";
    private String searchDate = "Invoice Date";
    private String[] showFilters = {};
    private boolean nsInvoiceOnly;
    private String accountType;
    private String excludeIds;
    private String emailIds;
    private String source;
    private String correctionNotice;
    private CustomerModel arSummary;
    private VendorModel apSummary;
    private VendorModel acSummary;
    private Long sourceId;

    public String getSearchFilter() {
        return searchFilter;
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    public String getSearchValue1() {
        return searchValue1;
    }

    public void setSearchValue1(String searchValue1) {
        this.searchValue1 = searchValue1;
    }

    public String getSearchValue2() {
        return searchValue2;
    }

    public void setSearchValue2(String searchValue2) {
        this.searchValue2 = searchValue2;
    }

    public String getSearchValue3() {
        return searchValue3;
    }

    public void setSearchValue3(String searchValue3) {
        this.searchValue3 = searchValue3;
    }

    public String getSearchValue4() {
        return searchValue4;
    }

    public void setSearchValue4(String searchValue4) {
        this.searchValue4 = searchValue4;
    }

    public String getSearchValue5() {
        return searchValue5;
    }

    public void setSearchValue5(String searchValue5) {
        this.searchValue5 = searchValue5;
    }

    public String getFromAmount6() {
        return fromAmount6;
    }

    public void setFromAmount6(String fromAmount6) {
        this.fromAmount6 = fromAmount6;
    }

    public String getToAmount6() {
        return toAmount6;
    }

    public void setToAmount6(String toAmount6) {
        this.toAmount6 = toAmount6;
    }

    public String getFromAmount7() {
        return fromAmount7;
    }

    public void setFromAmount7(String fromAmount7) {
        this.fromAmount7 = fromAmount7;
    }

    public String getToAmount7() {
        return toAmount7;
    }

    public void setToAmount7(String toAmount7) {
        this.toAmount7 = toAmount7;
    }

    public String getFromAmount8() {
        return fromAmount8;
    }

    public void setFromAmount8(String fromAmount8) {
        this.fromAmount8 = fromAmount8;
    }

    public String getToAmount8() {
        return toAmount8;
    }

    public void setToAmount8(String toAmount8) {
        this.toAmount8 = toAmount8;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public String[] getShowFilters() {
        if (null == showFilters || showFilters.length == 0) {
            showFilters = new String[]{"Open Invoices", "NS Invoices", "Credit Hold - Yes", "Credit Hold - No"};
        }
        return showFilters;
    }

    public void setShowFilters(String[] showFilters) {
        this.showFilters = showFilters;
    }

    public boolean isNsInvoiceOnly() {
        return nsInvoiceOnly;
    }

    public void setNsInvoiceOnly(boolean nsInvoiceOnly) {
        this.nsInvoiceOnly = nsInvoiceOnly;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getExcludeIds() {
        return excludeIds;
    }

    public void setExcludeIds(String excludeIds) {
        this.excludeIds = excludeIds;
    }

    public String getEmailIds() {
        return emailIds;
    }

    public void setEmailIds(String emailIds) {
        this.emailIds = emailIds;
    }

    public CustomerModel getArSummary() {
        return arSummary;
    }

    public void setArSummary(CustomerModel arSummary) {
        this.arSummary = arSummary;
    }

    public VendorModel getApSummary() {
        return apSummary;
    }

    public void setApSummary(VendorModel apSummary) {
        this.apSummary = apSummary;
    }

    public VendorModel getAcSummary() {
        return acSummary;
    }

    public void setAcSummary(VendorModel acSummary) {
        this.acSummary = acSummary;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCorrectionNotice() {
        return correctionNotice;
    }

    public void setCorrectionNotice(String correctionNotice) {
        this.correctionNotice = correctionNotice;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

}
