/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


public class ARRedInvoiceForm extends ActionForm {
    /*
	 * Generated Methods
	 */
        private String action;
	private String bl_drNumber;
	private String fileNo;
	private String cusName;
	private String dueDate;
	private String cusNumber;
	private String invoiceNumber;
	private String chargeCode;
	private String chargecode1;
	private String arCustomertype="F";
	private Double amount;
	private Double amount1;
        private String buttonValue;
        private String quantity;
	private String description;
	private String glAccount;
	private String accountNumber;
	private String contactName;
	private String address;
	private String phoneNumber;
	private String date;
	private String term="11344";
	private String termDesc="Due Upon Receipt";
	private String notes;
	private String rejectinvoice;
	private String recurring;
	private String apInvoiceChargesId;
	private String arRedInvoiceId;
	private String startingPeriod;
	private String endingPeriod;
	private String editArRedInvoice;
	private String shipmentType;
	private String accrualsId;
	private Double totalAmount;
	private String terminalCode;
	private String blNumber;
	private String invoiceBy;
	private String tempDescription;
	private String notification;
	private String screenName;
	private String invoices;
	private String invoiceStartdate;
	private String invoiceEnddate;
	private String orderBy;
	private String fileType;
        private String voyInternal;

	public String getAccrualsId() {
		return accrualsId;
	}

	public void setAccrualsId(String accrualsId) {
		this.accrualsId = accrualsId;
	}

	public String getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(String shipmentType) {
		this.shipmentType = shipmentType;
	}

	public String getEditArRedInvoice() {
		return editArRedInvoice;
	}

	public void setEditArRedInvoice(String editArRedInvoice) {
		this.editArRedInvoice = editArRedInvoice;
	}

	public String getApInvoiceChargesId() {
		return apInvoiceChargesId;
	}

	public void setApInvoiceChargesId(String apInvoiceChargesId) {
		this.apInvoiceChargesId = apInvoiceChargesId;
	}

	public String getArRedInvoiceId() {
		return arRedInvoiceId;
	}

	public void setArRedInvoiceId(String arRedInvoiceId) {
		this.arRedInvoiceId = arRedInvoiceId;
	}

	public String getRecurring() {
		return recurring;
	}

	public void setRecurring(String recurring) {
		this.recurring = recurring;
	}

	public String getRejectinvoice() {
		return rejectinvoice;
	}

	public void setRejectinvoice(String rejectinvoice) {
		this.rejectinvoice = rejectinvoice;
	}

	/**
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest   request) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.accountNumber="";
		this.accrualsId="";
		this.address="";
		this.arRedInvoiceId="";
		this.arCustomertype="";
		this.bl_drNumber="";
		this.buttonValue="";
		this.chargeCode="";
		this.cusName="";
		this.date="";
		this.dueDate="";
		this.editArRedInvoice="";
		this.endingPeriod="";
		this.recurring="";
		this.rejectinvoice="";
		this.term="";
		this.invoiceNumber="";
		this.phoneNumber="";
		this.shipmentType="";
		this.startingPeriod="";
		this.glAccount="";
		this.cusNumber="";
		this.contactName="";
		this.chargecode1="";
		this.notes="";
	}



	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getButtonValue() {
		return buttonValue;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
	public String getNotes() {
		return null != notes?notes.toUpperCase():"";
	}

	public void setNotes(String notes) {
		this.notes = null != notes?notes.toUpperCase():"";
	}
	public String getBl_drNumber() {
		return bl_drNumber;
	}

	public void setBl_drNumber(String bl_drNumber) {
		this.bl_drNumber = bl_drNumber;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getCusNumber() {
		return cusNumber;
	}

	public void setCusNumber(String cusNumber) {
		this.cusNumber = cusNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public String getChargecode1() {
		return chargecode1;
	}

	public void setChargecode1(String chargecode1) {
		this.chargecode1 = chargecode1;
	}

	public String getArCustomertype() {
		return arCustomertype;
	}

	public void setArCustomertype(String arCustomertype) {
		this.arCustomertype = arCustomertype;
	}

	public Double getAmount1() {
		return amount1;
	}

	public void setAmount1(Double amount1) {
		this.amount1 = amount1;
	}



	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return null != description?description.toUpperCase():"";
	}

	public void setDescription(String description) {
		this.description = null != description?description.toUpperCase():"";
	}

	public String getGlAccount() {
		return glAccount;
	}

	public void setGlAccount(String glAccount) {
		this.glAccount = glAccount;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getTermDesc() {
		return termDesc;
	}

	public void setTermDesc(String termDesc) {
		this.termDesc = termDesc;
	}

	public String getStartingPeriod() {
		return startingPeriod;
	}

	public void setStartingPeriod(String startingPeriod) {
		this.startingPeriod = startingPeriod;
	}

	public String getEndingPeriod() {
		return endingPeriod;
	}

	public void setEndingPeriod(String endingPeriod) {
		this.endingPeriod = endingPeriod;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getBlNumber() {
        return blNumber;
    }

    public void setBlNumber(String blNumber) {
        this.blNumber = blNumber;
    }

    public String getInvoiceBy() {
        return invoiceBy;
    }

    public void setInvoiceBy(String invoiceBy) {
        this.invoiceBy = invoiceBy;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getTempDescription() {
        return tempDescription;
    }

    public void setTempDescription(String tempDescription) {
        this.tempDescription = tempDescription;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getInvoiceEnddate() {
        return invoiceEnddate;
    }

    public void setInvoiceEnddate(String invoiceEnddate) {
        this.invoiceEnddate = invoiceEnddate;
    }

    public String getInvoiceStartdate() {
        return invoiceStartdate;
    }

    public void setInvoiceStartdate(String invoiceStartdate) {
        this.invoiceStartdate = invoiceStartdate;
    }

    public String getInvoices() {
        return invoices;
    }

    public void setInvoices(String invoices) {
        this.invoices = invoices;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getVoyInternal() {
        return voyInternal;
    }

    public void setVoyInternal(String voyInternal) {
        this.voyInternal = voyInternal;
    }
}
