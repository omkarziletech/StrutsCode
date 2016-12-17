package com.logiware.accounting.form;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.logiware.accounting.model.ApInvoiceModel;
import com.logiware.accounting.model.LineItemModel;
import java.util.List;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ApInvoiceForm extends BaseForm {

    private Integer creditId;
    private Integer creditTerm = 0;
    private String creditDesc = "Due Upon Receipt";
    private String dueDate;
    private String forComments;
    private boolean reject;
    private boolean recurring;
    private Integer id;
    private String invoiceAmount = "0.00";
    private Integer lineItemId;
    private LineItemModel lineItem;
    private List<LineItemModel> lineItems;
    private List<ApInvoiceModel> invoices;

    public Integer getCreditId() throws Exception {
	if (CommonUtils.isEmpty(creditId)) {
	    Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Credit Terms");
	    GenericCode genericCode = new GenericCodeDAO().getGenericCode(creditDesc, codeTypeId);
	    creditId = genericCode.getId();
	    creditTerm = Integer.parseInt(genericCode.getCode());
	}
	return creditId;
    }

    public void setCreditId(Integer creditId) {
	this.creditId = creditId;
    }

    public Integer getCreditTerm() {
	return creditTerm;
    }

    public void setCreditTerm(Integer creditTerm) {
	this.creditTerm = creditTerm;
    }

    public String getCreditDesc() {
	return creditDesc;
    }

    public void setCreditDesc(String creditDesc) {
	this.creditDesc = creditDesc;
    }

    public String getDueDate() {
	return dueDate;
    }

    public void setDueDate(String dueDate) {
	this.dueDate = dueDate;
    }

    public String getForComments() {
	return forComments;
    }

    public void setForComments(String forComments) {
	this.forComments = forComments;
    }

    public boolean isReject() {
	return reject;
    }

    public void setReject(boolean reject) {
	this.reject = reject;
    }

    public boolean isRecurring() {
	return recurring;
    }

    public void setRecurring(boolean recurring) {
	this.recurring = recurring;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getInvoiceAmount() {
	return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
	this.invoiceAmount = invoiceAmount;
    }

    public Integer getLineItemId() {
	return lineItemId;
    }

    public void setLineItemId(Integer lineItemId) {
	this.lineItemId = lineItemId;
    }

    public LineItemModel getLineItem() {
	if (null == lineItem) {
	    lineItem = new LineItemModel();
	}
	return lineItem;
    }

    public void setLineItem(LineItemModel lineItem) {
	this.lineItem = lineItem;
    }

    public List<LineItemModel> getLineItems() {
	return lineItems;
    }

    public void setLineItems(List<LineItemModel> lineItems) {
	this.lineItems = lineItems;
    }

    public List<ApInvoiceModel> getInvoices() {
	return invoices;
    }

    public void setInvoices(List<ApInvoiceModel> invoices) {
	this.invoices = invoices;
    }
}
