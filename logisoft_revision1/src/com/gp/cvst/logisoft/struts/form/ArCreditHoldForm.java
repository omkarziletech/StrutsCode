package com.gp.cvst.logisoft.struts.form;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.logiware.bean.AccountingBean;
import com.logiware.utils.ListUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;

public class ArCreditHoldForm extends ActionForm {

  private String customerType;
  private String customerName;
  private String customerNumber;
  private String billingTerminal;
  private String destination;
  private String salesPerson;
  private String collectorName;
  private String billOfLadding;
  private String action;
  private Integer pageNo = 1;
  private Integer currentPageSize = 100;
  private Integer totalPageSize = 0;
  private Integer noOfPages = 0;
  private Integer noOfRecords = 0;
  private String sortBy = "c.invoice_date";
  private String orderBy = "asc";
  private List<AccountingBean> transactions;
  private String removeFromHoldIds;
  private String putOnHoldIds;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public List<AccountingBean> getTransactions()throws Exception {
    if (null == transactions || transactions.isEmpty()) {
      transactions = ListUtils.lazyList(AccountingBean.class);
    }
    return transactions;
  }

  public void setTransactions(List<AccountingBean> transactions) {
    this.transactions = transactions;
  }

  public String getPutOnHoldIds() {
    return putOnHoldIds;
  }

  public void setPutOnHoldIds(String putOnHoldIds) {
    this.putOnHoldIds = putOnHoldIds;
  }

  public String getRemoveFromHoldIds() {
    return removeFromHoldIds;
  }

  public void setRemoveFromHoldIds(String removeFromHoldIds) {
    this.removeFromHoldIds = removeFromHoldIds;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerNumber() {
    return customerNumber;
  }

  public void setCustomerNumber(String customerNumber) {
    this.customerNumber = customerNumber;
  }

  public String getCustomerType() {
    return customerType;
  }

  public void setCustomerType(String customerType) {
    this.customerType = customerType;
  }

  public List<LabelValueBean> getCustomerTypes() {
    List<LabelValueBean> customerTypes = new ArrayList<LabelValueBean>();
    customerTypes.add(new LabelValueBean("Select", ""));
    customerTypes.add(new LabelValueBean("F-Forwarder", TradingPartnerConstants.FORWARDER));
    customerTypes.add(new LabelValueBean("S-Shipper", TradingPartnerConstants.SHIPPER));
    customerTypes.add(new LabelValueBean("C-Consignee", TradingPartnerConstants.CONSIGNEE));
    return customerTypes;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getBillingTerminal() {
    return billingTerminal;
  }

  public void setBillingTerminal(String billingTerminal) {
    this.billingTerminal = billingTerminal;
  }

  public String getCollectorName() {
    return collectorName;
  }

  public void setCollectorName(String collectorName) {
    this.collectorName = collectorName;
  }

  public String getSalesPerson() {
    return salesPerson;
  }

  public void setSalesPerson(String salesPerson) {
    this.salesPerson = salesPerson;
  }

  public String getBillOfLadding() {
    return billOfLadding;
  }

  public void setBillOfLadding(String billOfLadding) {
    this.billOfLadding = billOfLadding;
  }

  public Integer getCurrentPageSize() {
    return currentPageSize;
  }

  public void setCurrentPageSize(Integer currentPageSize) {
    this.currentPageSize = currentPageSize;
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

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }

  public Integer getTotalPageSize() {
    return totalPageSize;
  }

  public void setTotalPageSize(Integer totalPageSize) {
    this.totalPageSize = totalPageSize;
  }
}
