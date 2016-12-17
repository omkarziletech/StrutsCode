package com.logiware.accounting.form;

import com.logiware.accounting.model.EdiInvoiceModel;
import java.util.List;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author Lakshmi Naryanan
 */
public class EdiInvoiceForm extends ActionForm {

    private static final long serialVersionUID = 5634697990276977918L;
    private String action;
    private Integer limit = 200;
    private Integer selectedPage = 1;
    private Integer selectedRows = 0;
    private Integer totalPages = 0;
    private Integer totalRows = 0;
    private String sortBy = "invoice_date";
    private String orderBy = "desc";
    private String vendorName;
    private String vendorNumber;
    private String invoiceNumber;
    private String status;
    private Integer id;
    private String blNumber;
    private String containerNumber;
    private String voyageNumber;
    private String dockReceipt;
    private String bluescreenCostCode;
    private String costCode;
    private String glAccount;
    private String shipmentType;
    private String amount;
    private String terminal;
    private List<EdiInvoiceModel> invoices;

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

    public String getVendorName() {
	return vendorName;
    }

    public void setVendorName(String vendorName) {
	this.vendorName = vendorName;
    }

    public String getVendorNumber() {
	return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
	this.vendorNumber = vendorNumber;
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getBlNumber() {
	return blNumber;
    }

    public void setBlNumber(String blNumber) {
	this.blNumber = blNumber;
    }

    public String getContainerNumber() {
	return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
	this.containerNumber = containerNumber;
    }

    public String getVoyageNumber() {
	return voyageNumber;
    }

    public void setVoyageNumber(String voyageNumber) {
	this.voyageNumber = voyageNumber;
    }

    public String getDockReceipt() {
	return dockReceipt;
    }

    public void setDockReceipt(String dockReceipt) {
	this.dockReceipt = dockReceipt;
    }

    public String getBluescreenCostCode() {
	return bluescreenCostCode;
    }

    public void setBluescreenCostCode(String bluescreenCostCode) {
	this.bluescreenCostCode = bluescreenCostCode;
    }

    public String getCostCode() {
	return costCode;
    }

    public void setCostCode(String costCode) {
	this.costCode = costCode;
    }

    public String getGlAccount() {
	return glAccount;
    }

    public void setGlAccount(String glAccount) {
	this.glAccount = glAccount;
    }

    public String getShipmentType() {
	return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
	this.shipmentType = shipmentType;
    }

    public String getAmount() {
	return amount;
    }

    public void setAmount(String amount) {
	this.amount = amount;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public List<EdiInvoiceModel> getInvoices() {
	return invoices;
    }

    public void setInvoices(List<EdiInvoiceModel> invoices) {
	this.invoices = invoices;
    }

}
