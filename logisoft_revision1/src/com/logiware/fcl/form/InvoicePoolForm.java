package com.logiware.fcl.form;

import com.logiware.fcl.model.ResultModel;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class InvoicePoolForm extends BaseForm {

    private String status;
    private String invoiceNumber;
    private String customerName;
    private String customerNumber;
    private String orderByField;
    private List<ResultModel> results;

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
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

    public String getOrderByField() {
	return orderByField;
    }

    public void setOrderByField(String orderByField) {
	this.orderByField = orderByField;
    }

    public List<ResultModel> getResults() {
	return results;
    }

    public void setResults(List<ResultModel> results) {
	this.results = results;
    }
}
