package com.gp.cvst.logisoft.domain;

public class BookingPopUp implements java.io.Serializable{
	
	
	 private Integer id;
	 private Integer BookingId;
	 private Double sellrate;
	 private Double buyRate;
	 private String comments;
	 private String ChargeCode;
	 
	 

	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBookingId() {
		return BookingId;
	}
	public void setBookingId(Integer bookingId) {
		BookingId = bookingId;
	}
	public Double getBuyRate() {
		return buyRate;
	}
	public void setBuyRate(Double buyRate) {
		this.buyRate = buyRate;
	}
	public Double getSellrate() {
		return sellrate;
	}
	public void setSellrate(Double sellrate) {
		this.sellrate = sellrate;
	}
	public String getChargeCode() {
		return ChargeCode;
	}
	public void setChargeCode(String chargeCode) {
		ChargeCode = chargeCode;
	}
	 

}
