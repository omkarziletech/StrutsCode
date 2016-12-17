  package com.gp.cvst.logisoft.reports.dto;

import java.util.Date;
import java.util.List;

import com.gp.cvst.logisoft.domain.BookingFcl;

public class SearchBookingReportDTO {

 private String userLoginName;
 private String bookingnumber;
 private String bookingdate;
 private String portoforigin;
 private String originterminal;
 private String portofdischarge;
 private String bookingbyuser;
 private String destination;
 private String numbers;
 private String rates;
 private String ratedesc;
 private String shipper;
 private String addressforShipper;
 private String addressforForwarder;
 private String unitType;
 private String attenName;
 private String etd;
    private String eta;
    private String vessel;
    private String voyageCarrier;
    private String cutofDate;
    private String remarks;
    private String exportPositoningPickup;
    private String exportDevliery;
    private String truckerCode;
    private String positioningDate;
    private String username;
    private String addressForExpPositioning;
    private String portCutOff;
    private String SSLine;
    
    private String Portname;
   
    private String EmptyEarliestReturn;
    private String LoadLocation;
    private String LoadDate;
    private String shipperPhone;
    private String shipperName;
    private String shipperAddress;
    private String consigneeName;
    private String consigneePhone;
    private String consigneeAddress;
    
    private String forwarderName;
    private String forwarderPhone;
    private String forwarderAddress;
        private String truckerphone;
        private String loadDate;
        private String earliestpickup;
        private String address;
        private String ssline;
        private String sslName;
        private String voyagedoccut;
        private String earlistreturn;
        private String contact;
        private String emptyreturndate;
        private String rateconfirmation;
        private String puequipment;
        private String poe;
        private String pol;
        private String equipment;
        private String commodity;
        private String commodityDesc;
        private String company;
        private String from;
        private String date;
        private String phone;
        private String phone2;
       
        private String amount1;
        private String amount2;
        private String amount3;
        private String amount4;
        private String amount5;
        private String amount6;
        private String amount7;
        private String amount8;
        private String amount9;
        private String amount10;
        private String amount11;
        private String amount12;
        private String amount13;
        private String amount14;
        private String amount15;
        private String amount16;
        private String amount17;
        private String amount18;
        private String amount19;
        private String amount20;
        private String trucker;
       
        private String phone1;
        private String forwarder;
        private String email;
        private String pickupempty;
        private String return1;
        private String fieldName;
        private String sellRate;
        private String buyRate;
        private String sellRateTotal;
        private String buyRateTotal;
	    private String earlistpickupdate;
	    private String earlistreturndate;
	    private String comodity;
	    private String doccut;
	    private String fax;
	    private String contractref;
	    private String bookingrep;
	    private String tell;
	    private String loadingAddress;
	    private String bookingno;
	    private String attname;
	    private String carriername;
	    private String bargecutoff;
	    private String portcut;
	    private String bargecut;
	    private String autocutoff;
	    private String posdate;
	    private String forwardphone;
	    private String forwardemail;
	    private String forwardfax;
	    private String forwardzip;
	    private String consigneephone;
	    private String consigneeemail;
	    private String consigneefax;
	    private String consigneezip;
	    private String yen;	 
	     private String baht;
	     private String bdt;
	     private String cyp;
	     private String eur;
	     private String hkd;
	     private String lkr;
	     private String nt;
	     private String prs;
	     private String rmb;
	     private String won;
	     private String addessForExpDelivery;
	     private String positionlocation;	//newfields
		 private String emptypickupaddress;
		 private String shippercheck;
		 private String forwardercheck;
		 private String consigneecheck;
		 private String bookingemail;
		 private String loadcontact;
		 private String loadphone;
		 private BookingFcl bookingflFcl;
		 private List objectList;
		 private List objectList1;
		 private String fileName;
		 private String contextPath;
		 private List chargesList;
		 private List otherChargesList;
		 
		 private String userName;
		 private String userPhone;
		 private String userFax;
		 private String userEmail;
		 
		 //--new field to just to display region remarks based on destination in Reports.
		 private String regionRemarks;
		 
		
		public String getUserEmail() {
			return userEmail;
		}
		public void setUserEmail(String userEmail) {
			this.userEmail = userEmail;
		}
		public String getUserFax() {
			return userFax;
		}
		public void setUserFax(String userFax) {
			this.userFax = userFax;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getUserPhone() {
			return userPhone;
		}
		public void setUserPhone(String userPhone) {
			this.userPhone = userPhone;
		}
		public List getChargesList() {
			return chargesList;
		}
		public void setChargesList(List chargesList) {
			this.chargesList = chargesList;
		}
		public List getOtherChargesList() {
			return otherChargesList;
		}
		public void setOtherChargesList(List otherChargesList) {
			this.otherChargesList = otherChargesList;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getContextPath() {
			return contextPath;
		}
		public void setContextPath(String contextPath) {
			this.contextPath = contextPath;
		}
		public BookingFcl getBookingflFcl() {
			return bookingflFcl;
		}
		public void setBookingflFcl(BookingFcl bookingflFcl) {
			this.bookingflFcl = bookingflFcl;
		}
			public String getAddessForExpDelivery() {
				return addessForExpDelivery;
			}
			public void setAddessForExpDelivery(String addessForExpDelivery) {
				this.addessForExpDelivery = addessForExpDelivery;
			}
			public String getBookingemail() {
				return bookingemail;
			}
			public void setBookingemail(String bookingemail) {
				this.bookingemail = bookingemail;
			}
			public String getConsigneecheck() {
				return consigneecheck;
			}
			public void setConsigneecheck(String consigneecheck) {
				this.consigneecheck = consigneecheck;
			}
			public String getEmptypickupaddress() {
				return emptypickupaddress;
			}
			public void setEmptypickupaddress(String emptypickupaddress) {
				this.emptypickupaddress = emptypickupaddress;
			}
			public String getForwardercheck() {
				return forwardercheck;
			}
			public void setForwardercheck(String forwardercheck) {
				this.forwardercheck = forwardercheck;
			}
			public String getLoadcontact() {
				return loadcontact;
			}
			public void setLoadcontact(String loadcontact) {
				this.loadcontact = loadcontact;
			}
			public String getLoadphone() {
				return loadphone;
			}
			public void setLoadphone(String loadphone) {
				this.loadphone = loadphone;
			}
			public String getPositionlocation() {
				return positionlocation;
			}
			public void setPositionlocation(String positionlocation) {
				this.positionlocation = positionlocation;
			}
			public String getShippercheck() {
				return shippercheck;
			}
			public void setShippercheck(String shippercheck) {
				this.shippercheck = shippercheck;
			}
public String getYen() {
			return yen;
		}
		public void setYen(String yen) {
			this.yen = yen;
		}
public String getForwardphone() {
			return forwardphone;
		}
		public void setForwardphone(String forwardphone) {
			this.forwardphone = forwardphone;
		}
public String getPosdate() {
			return posdate;
		}
		public void setPosdate(String posdate) {
			this.posdate = posdate;
		}
public String getBargecut() {
			return bargecut;
		}
		public void setBargecut(String bargecut) {
			this.bargecut = bargecut;
		}
public String getPortcut() {
			return portcut;
		}
		public void setPortcut(String portcut) {
			this.portcut = portcut;
		}
public String getAttname() {
			return attname;
		}
		public void setAttname(String attname) {
			this.attname = attname;
		}
public String getBookingno() {
			return bookingno;
		}
		public void setBookingno(String bookingno) {
			this.bookingno = bookingno;
		}
public String getDoccut() {
		return doccut;
	}
	public void setDoccut(String doccut) {
		this.doccut = doccut;
	}
public String getComodity() {
		return comodity;
	}
	public void setComodity(String comodity) {
		this.comodity = comodity;
	}
public String getEarlistpickupdate() {
		return earlistpickupdate;
	}
	public void setEarlistpickupdate(String earlistpickupdate) {
		this.earlistpickupdate = earlistpickupdate;
	}
	public String getEarlistreturndate() {
		return earlistreturndate;
	}
	public void setEarlistreturndate(String earlistreturndate) {
		this.earlistreturndate = earlistreturndate;
	}
public String getTrucker() {
			return trucker;
		}
		public void setTrucker(String trucker) {
			this.trucker = trucker;
		}
public String getShipperPhone() {
  return shipperPhone;
 }
 public void setShipperPhone(String shipperPhone) {
  this.shipperPhone = shipperPhone;
 }
 public String getLoadDate() {
  return LoadDate;
 }
 public void setLoadDate(String loadDate) {
  LoadDate = loadDate;
 }
 public String getLoadLocation() {
  return LoadLocation;
 }
 public void setLoadLocation(String loadLocation) {
  LoadLocation = loadLocation;
 }
 public String getPortname() {
  return Portname;
 }
 public void setPortname(String portname) {
  Portname = portname;
 }
 public String getPortCutOff() {
  return portCutOff;
 }
 public void setPortCutOff(String portCutOff) {
  this.portCutOff = portCutOff;
 }
 public String getSSLine() {
  return SSLine;
 }
 public void setSSLine(String line) {
  SSLine = line;
 }
 public String getAddressForExpPositioning() {
  return addressForExpPositioning;
 }
 public void setAddressForExpPositioning(String addressForExpPositioning) {
  this.addressForExpPositioning = addressForExpPositioning;
 }
 public String getExportDevliery() {
  return exportDevliery;
 }
 public void setExportDevliery(String exportDevliery) {
  this.exportDevliery = exportDevliery;
 }
 public String getExportPositoningPickup() {
  return exportPositoningPickup;
 }
 public void setExportPositoningPickup(String exportPositoningPickup) {
  this.exportPositoningPickup = exportPositoningPickup;
 }
 
 public String getRemarks() {
  return remarks;
 }
 public void setRemarks(String remarks) {
  this.remarks = remarks;
 }
 public String getTruckerCode() {
  return truckerCode;
 }
 public void setTruckerCode(String truckerCode) {
  this.truckerCode = truckerCode;
 }
 public String getVessel() {
  return vessel;
 }
 public void setVessel(String vessel) {
  this.vessel = vessel;
 }
 public String getVoyageCarrier() {
  return voyageCarrier;
 }
 public void setVoyageCarrier(String voyageCarrier) {
  this.voyageCarrier = voyageCarrier;
 }
 
 public String getCutofDate() {
  return cutofDate;
 }
 public void setCutofDate(String cutofDate) {
  this.cutofDate = cutofDate;
 }
 public String getEta() {
  return eta;
 }
 public void setEta(String eta) {
  this.eta = eta;
 }
 public String getEtd() {
  return etd;
 }
 public void setEtd(String etd) {
  this.etd = etd;
 }
 
 public String getUsername() {
  return username;
 }
 public void setUsername(String username) {
  this.username = username;
 }
 public String getPositioningDate() {
  return positioningDate;
 }
 public void setPositioningDate(String positioningDate) {
  this.positioningDate = positioningDate;
 }
 public String getAttenName() {
  return attenName;
 }
 public void setAttenName(String attenName) {
  this.attenName = attenName;
 }
 public String getUnitType() {
  return unitType;
 }
 public void setUnitType(String unitType) {
  this.unitType = unitType;
 }
 public String getAddressforForwarder() {
  return addressforForwarder;
 }
 public void setAddressforForwarder(String addressforForwarder) {
  this.addressforForwarder = addressforForwarder;
 }
 public String getAddressforShipper() {
  return addressforShipper;
 }
 public void setAddressforShipper(String addressforShipper) {
  this.addressforShipper = addressforShipper;
 }
 public String getShipper() {
  return shipper;
 }
 public void setShipper(String shipper) {
  this.shipper = shipper;
 }
 public String getBookingbyuser() {
  return bookingbyuser;
 }
 public void setBookingbyuser(String bookingbyuser) {
  this.bookingbyuser = bookingbyuser;
 }
 public String getBookingdate() {
  return bookingdate;
 }
 public void setBookingdate(String bookingdate) {
  this.bookingdate = bookingdate;
 }
 
 public String getBookingnumber() {
  return bookingnumber;
 }
 public void setBookingnumber(String bookingnumber) {
  this.bookingnumber = bookingnumber;
 }
 public String getDestination() {
  return destination;
 }
 public void setDestination(String destination) {
  this.destination = destination;
 }
 public String getNumbers() {
  return numbers;
 }
 public void setNumbers(String numbers) {
  this.numbers = numbers;
 }
 public String getOriginterminal() {
  return originterminal;
 }
 public void setOriginterminal(String originterminal) {
  this.originterminal = originterminal;
 }
 public String getPortofdischarge() {
  return portofdischarge;
 }
 public void setPortofdischarge(String portofdischarge) {
  this.portofdischarge = portofdischarge;
 }
 public String getPortoforigin() {
  return portoforigin;
 }
 public void setPortoforigin(String portoforigin) {
  this.portoforigin = portoforigin;
 }
 public String getRatedesc() {
  return ratedesc;
 }
 public void setRatedesc(String ratedesc) {
  this.ratedesc = ratedesc;
 }
 public String getRates() {
  return rates;
 }
 public void setRates(String rates) {
  this.rates = rates;
 }
  
 public String getEmptyEarliestReturn() {
  return EmptyEarliestReturn;
 }
 public void setEmptyEarliestReturn(String emptyEarliestReturn) {
  EmptyEarliestReturn = emptyEarliestReturn;
 }
 
 public String getAddress() {
  return address;
 }
 public void setAddress(String address) {
  this.address = address;
 }
 public String getCommodity() {
  return commodity;
 }
 public void setCommodity(String commodity) {
  this.commodity = commodity;
 }
 public String getCompany() {
  return company;
 }
 public void setCompany(String company) {
  this.company = company;
 }
 public String getContact() {
  return contact;
 }
 public void setContact(String contact) {
  this.contact = contact;
 }
 public String getDate() {
  return date;
 }
 public void setDate(String date) {
  this.date = date;
 }
 public String getEarliestpickup() {
  return earliestpickup;
 }
 public void setEarliestpickup(String earliestpickup) {
  this.earliestpickup = earliestpickup;
 }
 public String getEarlistreturn() {
  return earlistreturn;
 }
 public void setEarlistreturn(String earlistreturn) {
  this.earlistreturn = earlistreturn;
 }
 public String getEmptyreturndate() {
  return emptyreturndate;
 }
 public void setEmptyreturndate(String emptyreturndate) {
  this.emptyreturndate = emptyreturndate;
 }
 
 public String getFrom() {
  return from;
 }
 public void setFrom(String from) {
  this.from = from;
 }
 public String getPhone() {
  return phone;
 }
 public void setPhone(String phone) {
  this.phone = phone;
 }
 public String getPoe() {
  return poe;
 }
 public void setPoe(String poe) {
  this.poe = poe;
 }
 public String getPuequipment() {
  return puequipment;
 }
 public void setPuequipment(String puequipment) {
  this.puequipment = puequipment;
 }
 public String getRateconfirmation() {
  return rateconfirmation;
 }
 public void setRateconfirmation(String rateconfirmation) {
  this.rateconfirmation = rateconfirmation;
 }
 public String getSsline() {
  return ssline;
 }
 public void setSsline(String ssline) {
  this.ssline = ssline;
 }
 public String getTruckerphone() {
  return truckerphone;
 }
 public void setTruckerphone(String truckerphone) {
  this.truckerphone = truckerphone;
 }
 public String getVoyagedoccut() {
  return voyagedoccut;
 }
 public void setVoyagedoccut(String voyagedoccut) {
  this.voyagedoccut = voyagedoccut;
 }
 public String getCommodityDesc() {
  return commodityDesc;
 }
 public void setCommodityDesc(String commodityDesc) {
  this.commodityDesc = commodityDesc;
 }
 public String getSslName() {
  return sslName;
 }
 public void setSslName(String sslName) {
  this.sslName = sslName;
 }
 public String getPhone2() {
  return phone2;
 }
 public void setPhone2(String phone2) {
  this.phone2 = phone2;
 }
 public String getAmount1() {
  return amount1;
 }
 public void setAmount1(String amount1) {
  this.amount1 = amount1;
 }
 public String getAmount10() {
  return amount10;
 }
 public void setAmount10(String amount10) {
  this.amount10 = amount10;
 }
 public String getAmount11() {
  return amount11;
 }
 public void setAmount11(String amount11) {
  this.amount11 = amount11;
 }
 public String getAmount12() {
  return amount12;
 }
 public void setAmount12(String amount12) {
  this.amount12 = amount12;
 }
 public String getAmount13() {
  return amount13;
 }
 public void setAmount13(String amount13) {
  this.amount13 = amount13;
 }
 public String getAmount14() {
  return amount14;
 }
 public void setAmount14(String amount14) {
  this.amount14 = amount14;
 }
 public String getAmount15() {
  return amount15;
 }
 public void setAmount15(String amount15) {
  this.amount15 = amount15;
 }
 public String getAmount16() {
  return amount16;
 }
 public void setAmount16(String amount16) {
  this.amount16 = amount16;
 }
 public String getAmount17() {
  return amount17;
 }
 public void setAmount17(String amount17) {
  this.amount17 = amount17;
 }
 public String getAmount18() {
  return amount18;
 }
 public void setAmount18(String amount18) {
  this.amount18 = amount18;
 }
 public String getAmount19() {
  return amount19;
 }
 public void setAmount19(String amount19) {
  this.amount19 = amount19;
 }
 public String getAmount2() {
  return amount2;
 }
 public void setAmount2(String amount2) {
  this.amount2 = amount2;
 }
 public String getAmount20() {
  return amount20;
 }
 public void setAmount20(String amount20) {
  this.amount20 = amount20;
 }
 public String getAmount3() {
  return amount3;
 }
 public void setAmount3(String amount3) {
  this.amount3 = amount3;
 }
 public String getAmount4() {
  return amount4;
 }
 public void setAmount4(String amount4) {
  this.amount4 = amount4;
 }
 public String getAmount5() {
  return amount5;
 }
 public void setAmount5(String amount5) {
  this.amount5 = amount5;
 }
 public String getAmount6() {
  return amount6;
 }
 public void setAmount6(String amount6) {
  this.amount6 = amount6;
 }
 public String getAmount7() {
  return amount7;
 }
 public void setAmount7(String amount7) {
  this.amount7 = amount7;
 }
 public String getAmount8() {
  return amount8;
 }
 public void setAmount8(String amount8) {
  this.amount8 = amount8;
 }
 public String getAmount9() {
  return amount9;
 }
 public void setAmount9(String amount9) {
  this.amount9 = amount9;
 }
 public String getConsigneeAddress() {
  return consigneeAddress;
 }
 public void setConsigneeAddress(String consigneeAddress) {
  this.consigneeAddress = consigneeAddress;
 }
 public String getConsigneeName() {
  return consigneeName;
 }
 public void setConsigneeName(String consigneeName) {
  this.consigneeName = consigneeName;
 }
 public String getConsigneePhone() {
  return consigneePhone;
 }
 public void setConsigneePhone(String consigneePhone) {
  this.consigneePhone = consigneePhone;
 }
 public String getForwarderAddress() {
  return forwarderAddress;
 }
 public void setForwarderAddress(String forwarderAddress) {
  this.forwarderAddress = forwarderAddress;
 }
 public String getForwarderName() {
  return forwarderName;
 }
 public void setForwarderName(String forwarderName) {
  this.forwarderName = forwarderName;
 }
 public String getForwarderPhone() {
  return forwarderPhone;
 }
 public void setForwarderPhone(String forwarderPhone) {
  this.forwarderPhone = forwarderPhone;
 }
 public String getShipperAddress() {
  return shipperAddress;
 }
 public void setShipperAddress(String shipperAddress) {
  this.shipperAddress = shipperAddress;
 }
 public String getShipperName() {
  return shipperName;
 }
 public void setShipperName(String shipperName) {
  this.shipperName = shipperName;
 }
public String getFax() {
	return fax;
}
public void setFax(String fax) {
	this.fax = fax;
}
public String getBuyRate() {
	return buyRate;
}
public void setBuyRate(String buyRate) {
	this.buyRate = buyRate;
}
public String getBuyRateTotal() {
	return buyRateTotal;
}
public void setBuyRateTotal(String buyRateTotal) {
	this.buyRateTotal = buyRateTotal;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getFieldName() {
	return fieldName;
}
public void setFieldName(String fieldName) {
	this.fieldName = fieldName;
}
public String getForwarder() {
	return forwarder;
}
public void setForwarder(String forwarder) {
	this.forwarder = forwarder;
}
public String getPhone1() {
	return phone1;
}
public void setPhone1(String phone1) {
	this.phone1 = phone1;
}
public String getPickupempty() {
	return pickupempty;
}
public void setPickupempty(String pickupempty) {
	this.pickupempty = pickupempty;
}
public String getReturn1() {
	return return1;
}
public void setReturn1(String return1) {
	this.return1 = return1;
}
public String getSellRate() {
	return sellRate;
}
public void setSellRate(String sellRate) {
	this.sellRate = sellRate;
}
public String getSellRateTotal() {
	return sellRateTotal;
}
public void setSellRateTotal(String sellRateTotal) {
	this.sellRateTotal = sellRateTotal;
}
public String getContractref() {
	return contractref;
}
public void setContractref(String contractref) {
	this.contractref = contractref;
}
public String getBookingrep() {
	return bookingrep;
}
public void setBookingrep(String bookingrep) {
	this.bookingrep = bookingrep;
}
public String getTell() {
	return tell;
}
public void setTell(String tell) {
	this.tell = tell;
}
public String getPol() {
	return pol;
}
public void setPol(String pol) {
	this.pol = pol;
}
public String getLoadingAddress() {
	return loadingAddress;
}
public void setLoadingAddress(String loadingAddress) {
	this.loadingAddress = loadingAddress;
}
public String getCarriername() {
	return carriername;
}
public void setCarriername(String carriername) {
	this.carriername = carriername;
}
public String getBargecutoff() {
	return bargecutoff;
}
public void setBargecutoff(String bargecutoff) {
	this.bargecutoff = bargecutoff;
}
public String getAutocutoff() {
	return autocutoff;
}
public void setAutocutoff(String autocutoff) {
	this.autocutoff = autocutoff;
}
public String getForwardemail() {
	return forwardemail;
}
public void setForwardemail(String forwardemail) {
	this.forwardemail = forwardemail;
}
public String getForwardfax() {
	return forwardfax;
}
public void setForwardfax(String forwardfax) {
	this.forwardfax = forwardfax;
}
public String getForwardzip() {
	return forwardzip;
}
public void setForwardzip(String forwardzip) {
	this.forwardzip = forwardzip;
}
public String getConsigneeemail() {
	return consigneeemail;
}
public void setConsigneeemail(String consigneeemail) {
	this.consigneeemail = consigneeemail;
}
public String getConsigneefax() {
	return consigneefax;
}
public void setConsigneefax(String consigneefax) {
	this.consigneefax = consigneefax;
}
public String getConsigneephone() {
	return consigneephone;
}
public void setConsigneephone(String consigneephone) {
	this.consigneephone = consigneephone;
}
public String getConsigneezip() {
	return consigneezip;
}
public void setConsigneezip(String consigneezip) {
	this.consigneezip = consigneezip;
}
public String getBaht() {
	return baht;
}
public void setBaht(String baht) {
	this.baht = baht;
}
public String getBdt() {
	return bdt;
}
public void setBdt(String bdt) {
	this.bdt = bdt;
}
public String getCyp() {
	return cyp;
}
public void setCyp(String cyp) {
	this.cyp = cyp;
}
public String getEur() {
	return eur;
}
public void setEur(String eur) {
	this.eur = eur;
}
public String getHkd() {
	return hkd;
}
public void setHkd(String hkd) {
	this.hkd = hkd;
}
public String getLkr() {
	return lkr;
}
public void setLkr(String lkr) {
	this.lkr = lkr;
}
public String getNt() {
	return nt;
}
public void setNt(String nt) {
	this.nt = nt;
}
public String getPrs() {
	return prs;
}
public void setPrs(String prs) {
	this.prs = prs;
}
public String getRmb() {
	return rmb;
}
public void setRmb(String rmb) {
	this.rmb = rmb;
}
public String getWon() {
	return won;
}
public void setWon(String won) {
	this.won = won;
}
public List getObjectList() {
	return objectList;
}
public void setObjectList(List objectList) {
	this.objectList = objectList;
}
public List getObjectList1() {
	return objectList1;
}
public void setObjectList1(List objectList1) {
	this.objectList1 = objectList1;
}
public String getEquipment() {
	return equipment;
}
public void setEquipment(String equipment) {
	this.equipment = equipment;
}
public String getUserLoginName() {
	return userLoginName;
}
public void setUserLoginName(String userLoginName) {
	this.userLoginName = userLoginName;
}
public String getRegionRemarks() {
	return regionRemarks;
}
public void setRegionRemarks(String regionRemarks) {
	this.regionRemarks = regionRemarks;
}


}