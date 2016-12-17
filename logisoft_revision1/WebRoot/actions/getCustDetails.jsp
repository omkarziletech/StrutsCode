<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cvst.logisoft.domain.*,org.apache.commons.lang3.StringUtils,com.gp.cong.logisoft.bc.fcl.*,com.gp.cong.logisoft.domain.UnLocation,com.gp.cong.logisoft.domain.FclBuy,com.gp.cong.logisoft.bc.ratemanagement.UnLocationBC,org.json.JSONObject,com.gp.cong.logisoft.domain.CarriersOrLineTemp,com.gp.cong.logisoft.bc.ratemanagement.GenericCodeBC,com.gp.cong.logisoft.bc.ratemanagement.CarriersOrLineBC,com.gp.cong.logisoft.domain.GenericCode"%>
<%@page import="com.gp.cvst.logisoft.domain.Batch"%>
<%@page import="com.gp.cong.logisoft.domain.Vendor"%>
<%@page import="com.gp.cong.logisoft.bc.tradingpartner.APConfigurationBC"%>

<%
	String requestFor = request.getParameter("requestFor");
	String customerType = null;

	JSONObject jsonObj = new JSONObject();

	if (requestFor == null) {
		return;
	}	
	if (requestFor.equals("CustAddress")||requestFor.equals("SSLineNo")) {
		String clientName1 = request.getParameter("custName");
		String clientName = clientName1.replace("amp;", "").trim();
		if (request.getParameter("customerType") != null) {
			customerType = request.getParameter("customerType");
			if (customerType.equals("Forwarder")) {
				customerType = "F";
			} else if (customerType.equals("Shipper")) {
				customerType = "S";
			} else if (customerType.equals("Consignee")) {
				customerType = "C";
			} else {
				customerType = null;
			}
		}
		CustAddressBC custAddressBC = new CustAddressBC();
		CustAddress custAddress = null;
		if (customerType == null) {
			custAddress = custAddressBC.getCustInfo(clientName);
		} else {
			custAddress = custAddressBC.getCustInfo(clientName,
					customerType);
		}
		jsonObj.put("custName", custAddress.getAcctName());
		
		jsonObj.put("custNumber", custAddress.getAcctNo());
		jsonObj.put("custCity", custAddress.getCity1());
		if (custAddress.getCuntry() != null
				&& custAddress.getCuntry().getCodedesc() != null) {
			jsonObj.put("custCountry", custAddress.getCuntry()
					.getCodedesc());
		} else {
			String empty = "";
			jsonObj.put("custCountry", empty);
		}
		jsonObj.put("custState", custAddress.getState());
		jsonObj.put("custZip", custAddress.getZip());
		jsonObj.put("custAddress", custAddress.getAddress1());
		jsonObj.put("custContactName", custAddress.getContactName());
		jsonObj.put("custPhone", custAddress.getPhone());
		jsonObj.put("custFax", custAddress.getFax());
		jsonObj.put("custEmail", custAddress.getEmail1());
		jsonObj.put("custBankName", custAddress.getBankName());
		jsonObj.put("custBankAddress", custAddress.getBankAddress());
	} else if (requestFor.equals("CustAddressForNo")||requestFor.equals("SSLineName")) {
		String clientNo = request.getParameter("custNo");
		CustAddressBC custAddressBC = new CustAddressBC();
		CustAddress custAddress = custAddressBC.getCustInfoForCustNo(clientNo);
		jsonObj.put("custName", custAddress.getAcctName().toString());
		jsonObj.put("custNumber", custAddress.getAcctNo());
		jsonObj.put("custCity", custAddress.getCity1());
		if (custAddress.getCuntry() != null
				&& custAddress.getCuntry().getCodedesc() != null) {
			jsonObj.put("custCountry", custAddress.getCuntry()
					.getCodedesc());
		} else {
			String empty = "";
			jsonObj.put("custCountry", empty);
		}
		jsonObj.put("custState", custAddress.getState());
		jsonObj.put("custZip", custAddress.getZip());
		jsonObj.put("custAddress", custAddress.getAddress1());
		jsonObj.put("custContactName", custAddress.getContactName());
		jsonObj.put("custPhone", custAddress.getPhone());
		jsonObj.put("custFax", custAddress.getFax());
		jsonObj.put("custEmail", custAddress.getEmail1());
	} else if (requestFor.equals("CustNumber")) {
		String clientName1 = request.getParameter("custName");
		String clientName = clientName1.replace("amp;", "").trim();
		CustAddressBC custAddressBC = new CustAddressBC();
		CustAddress custAddress = null;
		custAddress = custAddressBC.getCustInfo(clientName);
		jsonObj.put("custNumber", custAddress.getAcctNo());
	}else if (requestFor.equals("clientDetails")) {
		String accountName = request.getParameter("accountName");
		String accountNumber = request.getParameter("accountNumber");
		CustAddressBC custAddressBC = new CustAddressBC();
		CustAddress custAddress = null;
		custAddress = custAddressBC.getClientDetails(accountName,accountNumber);
		jsonObj.put("clientName", custAddress.getAcctName());
		jsonObj.put("clientNo", custAddress.getAcctNo());
		    String clientTypes="";
			String clientType[]=StringUtils.split(custAddress.getAcctType(), ",");
				if(clientType!=null){
				for (int i = 0; i < clientType.length; i++) {
					String clienttype=clientType[i];
					if(clienttype.equals("S")){
						clientTypes=clientTypes+"Shipper"+",";
					}
					if(clienttype.equals("F")){
						clientTypes=clientTypes+"Forwarder"+",";
					}
					if(clienttype.equals("C")){
						clientTypes=clientTypes+"Consignee"+",";
					}
					if(clienttype.equals("N")){
						clientTypes=clientTypes+"Notify Party"+",";
					}
					if(clienttype.equals("SS")){
						clientTypes=clientTypes+"Stream ShipeLine"+",";
					}
					if(clienttype.equals("T")){
						clientTypes=clientTypes+"Truck Line"+",";
					}
					if(clienttype.equals("A")){
						clientTypes=clientTypes+"Agent"+",";
					}
					if(clienttype.equals("I")){
						clientTypes=clientTypes+"Import Agent"+",";
					}
					if(clienttype.equals("E")){
						clientTypes=clientTypes+"Export Agent"+",";
					}
					if(clienttype.equals("V")){
						clientTypes=clientTypes+"Vendor"+",";
					}
					if(clienttype.equals("O")){
						clientTypes=clientTypes+"Others"+",";
					}
				}
				}
			jsonObj.put("clientType", clientTypes);
			jsonObj.put("contactName",custAddress.getContactName());
			jsonObj.put("email",custAddress.getEmail1());
			jsonObj.put("phone",custAddress.getPhone());
			jsonObj.put("fax",custAddress.getFax());
		
	}else if("CustNumberAndTerms".equals(requestFor)) {
		String clientName1 = request.getParameter("custName");
		String clientName = clientName1.replace("amp;", "").trim();
		CustAddressBC custAddressBC = new CustAddressBC();
		APConfigurationBC apConfigurationBC = new APConfigurationBC();
		CustAddress custAddress = null;
		custAddress = custAddressBC.getCustInfo(request.getParameter("custName"));
		Vendor vendor = null;
		
		if(null != custAddress) {
			vendor = apConfigurationBC.getApInvoiceByCustomerNumber(custAddress.getAcctNo());
			jsonObj.put("custNumber", custAddress.getAcctNo());
		}
		
		if(null != vendor) {
			GenericCode code = vendor.getCterms();
			if(null != code) {
				jsonObj.put("term", code.getId());
			}
			
		}
		
	}	
	out.println(jsonObj.toString());
%>