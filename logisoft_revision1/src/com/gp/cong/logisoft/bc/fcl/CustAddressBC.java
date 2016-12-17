package com.gp.cong.logisoft.bc.fcl;

import com.gp.cong.common.CommonUtils;
import java.util.List;

import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.domain.GeneralInformation;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.Vendor;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;

public class CustAddressBC {

    CustAddressDAO custAddressDAO = new CustAddressDAO();
    CustAddress custAddress = new CustAddress();

    public CustAddress getCustInfo(String clientName)throws Exception {

        List clientlist = custAddressDAO.findBy(clientName, null, null, null);
        String bankName = null;
        String bankAddress = "";
        for (int i = 0; i < clientlist.size(); i++) {
            CustAddress custAddress1 = (CustAddress) clientlist.get(i);
            if (custAddress1.getPrimeAddress() != null && custAddress1.getPrimeAddress().equals("on")) {
                Vendor vendor = custAddressDAO.getVendorAddress(custAddress1.getAcctNo());
                if (vendor != null) {
                    bankName = vendor.getDba();
                    if (vendor.getAddress1() != null) {
                        bankAddress += vendor.getAddress1() + ",";
                    }
                    if (vendor.getCuntry() != null) {
                        bankAddress += vendor.getCuntry() + ",";
                    }
                    if (vendor.getState() != null) {
                        bankAddress += vendor.getState() + ",";
                    }
                    if (vendor.getCity2() != null) {
                        bankAddress += vendor.getCity2() + ",";
                    }
                    if (vendor.getZip() != null) {
                        bankAddress += vendor.getZip();
                    }
                }
            }
        }
        if (clientlist != null && clientlist.size() > 0) {
            custAddress = (CustAddress) clientlist.get(0);
            custAddress.setBankName(bankName);
            custAddress.setBankAddress(bankAddress);
        }
        return custAddress;
    }

    public CustAddress getClientDetails(String clientName, String clientNumber)throws Exception {
        CustAddress custAddress = new CustAddress();
            List customerList = custAddressDAO.getCustomerForLookUp("", "", clientNumber);
            if(CommonUtils.isNotEmpty(customerList)){
                custAddress = (CustAddress) customerList.get(0);
            }
        return custAddress;
    }
     public CustAddress getClientAddress(String clientNumber)throws Exception {
         return custAddressDAO.findPrimeContact(clientNumber);
    }

    public CustAddress getCustInfo(String clientName, String customerType)throws Exception {
        custAddress = new CustAddress();
        List clientlist = custAddressDAO.findBy(clientName, null, null, customerType);
        if (clientlist != null && clientlist.size() > 0) {
            custAddress = (CustAddress) clientlist.get(0);
        }
        return custAddress;
    }

    public CustAddress getCustInfoForCustNo(String clientNo)throws Exception {
        custAddress = new CustAddress();
        if(CommonFunctions.isNotNull(clientNo)){
	        List clientlist = custAddressDAO.findBy(null,clientNo, null, null);
	        if (clientlist != null && clientlist.size() > 0) {
	            custAddress = (CustAddress) clientlist.get(0);
	            TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
	            tradingPartnerBC.setSubType(custAddress);
	        }
        }
        return custAddress;
    }
    public GeneralInformation getGeneralInfoForCustNo(String clientNo)throws Exception {
        GeneralInformation generalInformation = new GeneralInformation();
        if(CommonFunctions.isNotNull(clientNo)){
            generalInformation =new GeneralInformationDAO().getGeneralInformationByAccountNumber(clientNo);
        }
        return generalInformation;
    }

    public boolean checkForPortName(String cityName)throws Exception {
    	PortsDAO portsDAO=new PortsDAO();
    	List portsList=portsDAO.getPortName(cityName.replaceAll("'", "''"));
    	if(null != portsList && portsList.size()>0){
    		return true;
    	}else{
    		return false;
    	}
    }
    public String getCompleteAddress(String accoutnNumber)throws Exception {
    	if(CommonFunctions.isNotNull(accoutnNumber)){
    		custAddress=getCustInfoForCustNo(accoutnNumber);
    		return getCustomerAddress(custAddress);
    	}else{
    		return "";
    	}
    }
    public String getCustomerAddress(CustAddress custAddress){
    	StringBuffer address= new StringBuffer(); ;
    	if(custAddress!=null){
	    	if (CommonFunctions.isNotNull(custAddress.getAddress1())) {
                        if (CommonFunctions.isNotNull(custAddress.getCoName())) {
                            address.append(custAddress.getCoName());
                            address.append(" \n");
                        }
	    		address.append(custAddress.getAddress1().replace("/[\r\n]+/g", ""));
	    		address.append(" \n");
	    	}
	    	if (CommonFunctions.isNotNull(custAddress.getCity1())) {
	    		address.append(custAddress.getCity1()+", ");
	    	}
	    	if (CommonFunctions.isNotNull(custAddress.getState()+" ")) {
	    		address.append(custAddress.getState()+" ");
	    	}
	    	if (CommonFunctions.isNotNull(custAddress.getZip())) {
	    		address.append(custAddress.getZip()+" ");
	    	}
	    	address.append(" \n");
	    	if (CommonFunctions.isNotNull(custAddress.getCuntry()) && !custAddress.getCuntry().getCodedesc().equalsIgnoreCase(FclBlConstants.COUNTRYNAME)) {
	    		address.append(custAddress.getCuntry().getCodedesc());
	    		address.append(" \n");
	    	}
	    	if (CommonFunctions.isNotNull(custAddress.getPhone())) {
	    		address.append("PHONE ");
	    		address.append(custAddress.getPhone());
	    	}
                if (CommonFunctions.isNotNull(custAddress.getFax())) {
	    		address.append("FAX ");
	    		address.append(custAddress.getFax());
	    	}
                if (CommonFunctions.isNotNull(custAddress.getEmail1())) {
	    		address.append("EMAIL1 ");
	    		address.append(custAddress.getEmail1());
	    	}
                if (CommonFunctions.isNotNull(custAddress.getEmail2())) {
	    		address.append("EMAIL2 ");
	    		address.append(custAddress.getEmail2());
	    	}
                    	}
    	  return address.toString();
    }
    public String getCompleteShipperAddress(String accoutnNumber)throws Exception {
    	if(CommonFunctions.isNotNull(accoutnNumber)){
    		custAddress=getCustInfoForCustNo(accoutnNumber);
    		return getShipperAddress(custAddress);
    	}else{
    		return "";
    	}
    }
    public String getShipperAddress(CustAddress custAddress){
    	StringBuffer address= new StringBuffer(); ;
    	if(custAddress!=null){
	    	if (CommonFunctions.isNotNull(custAddress.getAddress1())) {
                        if (CommonFunctions.isNotNull(custAddress.getCoName())) {
                            address.append(custAddress.getCoName());
                            address.append(" \n");
                        }
	    		address.append(custAddress.getAddress1().replace("/[\r\n]+/g", ""));
	    		address.append(" \n");
	    	}
	    	if (CommonFunctions.isNotNull(custAddress.getCity1())) {
	    		address.append(custAddress.getCity1()+", ");
	    	}
	    	if (CommonFunctions.isNotNull(custAddress.getState()+" ")) {
	    		address.append(custAddress.getState()+" ");
	    	}
	    	if (CommonFunctions.isNotNull(custAddress.getZip())) {
	    		address.append(custAddress.getZip()+" ");
	    	}
	    	address.append(" \n");
	    	if (CommonFunctions.isNotNull(custAddress.getCuntry()) && !custAddress.getCuntry().getCodedesc().equalsIgnoreCase(FclBlConstants.COUNTRYNAME)) {
	    		address.append(custAddress.getCuntry().getCodedesc());
	    	}
    	}
    	  return address.toString();
    }
    public String concatNotifyPartyAddress(TradingPartner tradingPartner){
    	StringBuffer address= new StringBuffer("");
    	if(tradingPartner!=null){

	    	if (CommonFunctions.isNotNull(tradingPartner.getNotifyPartyAddress())) {
	    		address.append(tradingPartner.getNotifyPartyAddress().replace("/[\r\n]+/g", ""));
	    		address.append(" \n");
	    	}
	    	if (CommonFunctions.isNotNull(tradingPartner.getNotifyPartyCity())) {
	    		address.append(tradingPartner.getNotifyPartyCity()+", ");
	    	}
	    	if (CommonFunctions.isNotNull(tradingPartner.getNotifyPartyState()+" ")) {
	    		address.append(tradingPartner.getNotifyPartyState()+" ");
	    	}
	    	if (CommonFunctions.isNotNull(tradingPartner.getNotifyPartyPostalCode())) {
	    		address.append(tradingPartner.getNotifyPartyPostalCode()+" ");
	    	}
	    	address.append(" \n");
	    	if (CommonFunctions.isNotNull(tradingPartner.getNotifyPartyCountry()) && !tradingPartner.getNotifyPartyCountry().equalsIgnoreCase(FclBlConstants.COUNTRYNAME)) {
	    		address.append(tradingPartner.getNotifyPartyCountry());
	    		address.append(" \n");
	    	}
    	}
    	  return address.toString();
    }
    public String[] getSchedBDesc(String schedBnumber)throws Exception {
        String[] data = new String[4];
        GenericCode genericCode = new GenericCodeDAO().getGenericCodeId("68", schedBnumber);
        if(null != genericCode && CommonUtils.isNotEmpty(genericCode.getCodedesc())){
            data[0] = genericCode.getCodedesc();
            data[1] = null != genericCode.getField1()?new GenericCodeDAO().getFieldByCodeAndCodetypeId("UOM Codes", genericCode.getField1(),"codedesc")+"-->"+genericCode.getField1():"";
            data[2] = null != genericCode.getField2()?new GenericCodeDAO().getFieldByCodeAndCodetypeId("UOM Codes", genericCode.getField2(),"codedesc")+"-->"+genericCode.getField2():"";
            return data;
        }
        return null;
    }
}
