package com.logiware.edi.tracking;

import org.apache.struts.action.ActionForm;

public class EdiTrackingSystemForm extends ActionForm{

	private String drNumber;
	private String messageType;
	private String ediCompany;
	private String match;
	private String buttonValue;
        private String portOfLoad;
        private String portOfDischarge;
        private String placeOfReceipt;
        private String placeOfDelivery;
        private String bookingNo;
        private String ediStatus;
        private String transportService;
        private String transactionStatus;
        private String requestor;
        private String fromDate;
        private String toDate;
        
	public String getDrNumber() {
		return drNumber;
	}
	public void setDrNumber(String drNumber) {
		this.drNumber = drNumber;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getEdiCompany() {
		return ediCompany;
	}
	public void setEdiCompany(String ediCompany) {
		this.ediCompany = ediCompany;
	}
	public String getMatch() {
		return match;
	}
	public void setMatch(String match) {
		this.match = match;
	}
	public String getButtonValue() {
		return buttonValue;
	}
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getPlaceOfDelivery() {
        return placeOfDelivery;
    }

    public void setPlaceOfDelivery(String placeOfDelivery) {
        this.placeOfDelivery = placeOfDelivery;
    }

    public String getPlaceOfReceipt() {
        return placeOfReceipt;
    }

    public void setPlaceOfReceipt(String placeOfReceipt) {
        this.placeOfReceipt = placeOfReceipt;
    }

    public String getPortOfDischarge() {
        return portOfDischarge;
    }

    public void setPortOfDischarge(String portOfDischarge) {
        this.portOfDischarge = portOfDischarge;
    }

    public String getPortOfLoad() {
        return portOfLoad;
    }

    public void setPortOfLoad(String portOfLoad) {
        this.portOfLoad = portOfLoad;
    }

    public String getEdiStatus() {
        return ediStatus;
    }

    public void setEdiStatus(String ediStatus) {
        this.ediStatus = ediStatus;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransportService() {
        return transportService;
    }

    public void setTransportService(String transportService) {
        this.transportService = transportService;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

}
