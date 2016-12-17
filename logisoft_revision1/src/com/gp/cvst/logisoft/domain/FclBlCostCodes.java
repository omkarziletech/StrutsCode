package com.gp.cvst.logisoft.domain;

import java.util.Date;


import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.struts.form.EditBookingsForm;
import com.gp.cvst.logisoft.struts.form.FclBillLaddingForm;

/**
 * FclBl generated by MyEclipse Persistence Tools
 */

public class FclBlCostCodes implements java.io.Serializable {
    private Integer codeId;
    private Integer bookingId;
    private String accNo;
    private String accName;
    private String comments;
    private Double amount;
    private Integer bolId;
    private Date datePaid;
    private String currencyCode;
    private String costCode;
    private String costCodeDesc;
    private String readyToPost;
    private String checkNo;
    private String readOnlyFlag;
    private String costComments;
    private String bookingFlag;
    private String invoiceNumber;
    private String processedStatus;
    private String accrualsUpdatedBy;
    private String accrualsCreatedBy;
    private String transactionType;
    private String deleteFlag;
    private String manifestModifyFlag;
    private Date accrualsUpdatedDate;
    private Date accrualsCreatedDate;

	public String getBookingFlag() {
		return bookingFlag;
	}

	public void setBookingFlag(String bookingFlag) {
		this.bookingFlag = bookingFlag;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public String getReadyToPost() {
		return readyToPost;
	}

	public void setReadyToPost(String readyToPost) {
		this.readyToPost = readyToPost;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public Integer getBolId() {
		return bolId;
	}

	public void setBolId(Integer bolId) {
		this.bolId = bolId;
	}

	public Integer getCodeId() {
		return codeId;
	}

	public void setCodeId(Integer codeId) {
		this.codeId = codeId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCostCode() {
		return costCode;
	}

	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}

	public String getCostCodeDesc() {
		return costCodeDesc;
	}

	public void setCostCodeDesc(String costCodeDesc) {
		this.costCodeDesc = costCodeDesc;
	}

	public Date getDatePaid() {
		return datePaid;
	}

	public void setDatePaid(Date datePaid) {
		this.datePaid = datePaid;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getReadOnlyFlag() {
		return readOnlyFlag;
	}

	public void setReadOnlyFlag(String readOnlyFlag) {
		this.readOnlyFlag = readOnlyFlag;
	}

    public FclBlCostCodes(FclBillLaddingForm fclBillLaddingForm){
      this.costCode=fclBillLaddingForm.getCostCode();
      this.costCodeDesc=fclBillLaddingForm.getCostCodeDesc();
      this.accName=fclBillLaddingForm.getAccountName();
      this.accNo=fclBillLaddingForm.getAccountNo();
      this.invoiceNumber=fclBillLaddingForm.getInvoiceNumber();
      this.currencyCode=fclBillLaddingForm.getCostCurrency();
      this.costComments = CommonFunctions.isNotNull(fclBillLaddingForm.getCostComments())?
				fclBillLaddingForm.getCostComments().toUpperCase():fclBillLaddingForm.getCostComments();
      this.checkNo=fclBillLaddingForm.getChequeNumber();
      this.bolId=(fclBillLaddingForm.getBol()!=null && !fclBillLaddingForm.getBol().equals("")) ? Integer.parseInt(fclBillLaddingForm.getBol()) : 0;
    }
    public FclBlCostCodes(EditBookingsForm editBookingsForm){
      this.costCode=editBookingsForm.getCostCode();
      this.costCodeDesc=editBookingsForm.getCostCodeDesc();
      this.accName=editBookingsForm.getAccountName();
      this.accNo=editBookingsForm.getVendorAccountNo();
      this.invoiceNumber=editBookingsForm.getInvoiceNumber();
      this.currencyCode=editBookingsForm.getCostCurrency();
      this.costComments = CommonFunctions.isNotNull(editBookingsForm.getCostComments())?
				editBookingsForm.getCostComments().toUpperCase():editBookingsForm.getCostComments();
      this.bookingId=(editBookingsForm.getBookingId()!=null && !editBookingsForm.getBookingId().equals("")) ?
                                Integer.parseInt(editBookingsForm.getBookingId()) : 0;
    }

    public FclBlCostCodes(){
    	
    }

	public String getCostComments() {
		return costComments;
	}

	public void setCostComments(String costComments) {
		this.costComments = costComments;
	}

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getProcessedStatus() {
        return processedStatus;
    }

    public void setProcessedStatus(String processedStatus) {
        this.processedStatus = processedStatus;
    }

    public String getAccrualsCreatedBy() {
        return accrualsCreatedBy;
    }

    public void setAccrualsCreatedBy(String accrualsCreatedBy) {
        this.accrualsCreatedBy = accrualsCreatedBy;
    }

    public Date getAccrualsCreatedDate() {
        return accrualsCreatedDate;
    }

    public void setAccrualsCreatedDate(Date accrualsCreatedDate) {
        this.accrualsCreatedDate = accrualsCreatedDate;
    }

    public String getAccrualsUpdatedBy() {
        return accrualsUpdatedBy;
    }

    public void setAccrualsUpdatedBy(String accrualsUpdatedBy) {
        this.accrualsUpdatedBy = accrualsUpdatedBy;
    }

    public Date getAccrualsUpdatedDate() {
        return accrualsUpdatedDate;
    }

    public void setAccrualsUpdatedDate(Date accrualsUpdatedDate) {
        this.accrualsUpdatedDate = accrualsUpdatedDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getManifestModifyFlag() {
        return manifestModifyFlag;
    }

    public void setManifestModifyFlag(String manifestModifyFlag) {
        this.manifestModifyFlag = manifestModifyFlag;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = null != deleteFlag && !"".equals(deleteFlag)?deleteFlag:"no";
    }

    public Integer getbookingId() {
        return bookingId;
    }

    public void setbookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    
}