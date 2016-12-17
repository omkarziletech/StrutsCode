<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.domain.CustomerTemp,com.gp.cong.logisoft.domain.Customer"
	pageEncoding="ISO-8859-1"%>
<jsp:directive.page
	import="com.gp.cong.logisoft.hibernate.dao.CustomerDAO" />
<jsp:directive.page
	import="com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO" />
<jsp:directive.page import="com.gp.cvst.logisoft.domain.CustAddress" />
<%@page import="com.gp.cvst.logisoft.AccountingConstants"%>
<%  
    String acctno = null;
	String acctname = null;
	String index = null;
	String customerType = null;
	String functionName = null;
	String accountName = "";
	String acctNameForSteamShipLine = null;
	String delimiter=AccountingConstants.DELIMITER_FOR_CUSTOMERNAME_NUMBER;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("ACCOUNTS_PAYABLE")
			|| functionName.equals("ACCRUALS")
			|| functionName.equals("AP_INQUIRY")
			|| functionName.equals("MANIFEST_FOR_TL")
			|| functionName.equals("MANIFEST")
			|| functionName.equals("BANKACCOUNT")) {
		if (request.getParameter("vendor") != null) {
			acctname = request.getParameter("vendor");
		} else if (request.getParameter("accountName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("0")) {
			acctname = request.getParameter("accountName");
		}
	} else if (functionName.equals("ACCOUNTS_RECIEVABLE_INQUIRY")
			|| functionName.equals("AR_APPLY_PAYMENTS")
			|| functionName.equals("QUOTE")) {
		if (request.getParameter("customerName") != null
		&& !request.getParameter("customerName").equals("")
		&& request.getParameter("from").equals("0")) {
			acctname = request.getParameter("customerName");
		} else if (request.getParameter("otherCustomer") != null
		&& !request.getParameter("otherCustomer").equals("")
		&& request.getParameter("from").equals("3")) {
			acctname = request.getParameter("otherCustomer");
		} else if (request.getParameter("customerNumber") != null
		&& !request.getParameter("customerNumber").equals("")
		&& request.getParameter("from").equals("2")) {
			acctno = request.getParameter("customerNumber");
		} else if (request.getParameter("client") != null
		&& !request.getParameter("client").equals("")
		&& request.getParameter("from").equals("5")) {
			acctname = request.getParameter("client");
		} else if (request.getParameter("carrier") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("1")) {
			acctname = request.getParameter("carrier");
		} else if (request.getParameter("vendorName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("3")) {
			acctname = request.getParameter("vendorName");
			customerType = "V";
		} else if (request.getParameter("accountNo") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("4")) {
			acctno = request.getParameter("accountNo");
			customerType = "V";
		} else if (request.getParameter("customerName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("6")) {
		     accountName = request.getParameter("customerName");
			
		}else if (request.getParameter("sslDescription") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("7")) {
		     acctNameForSteamShipLine = request.getParameter("sslDescription");
		}
	} else if(functionName.equals("CLOSE_BATCH_POPUP")){
	   if(request.getParameter("otherCustomerName") != null){
		   acctname = request.getParameter("otherCustomerName");
		} 
	
	} else if (functionName.equals("AR_CREDIT_HOLD")) {
		if (request.getParameter("customerType") != null) {

			customerType = request.getParameter("customerType");
			if (customerType.equals("F")) {
		customerType = "F";
			} else if (customerType.equals("S")) {
		customerType = "S";
			} else if (customerType.equals("C")) {
		customerType = "C";
			} else {
		customerType = null;
			}

			if (request.getParameter("customer") != null) {
		acctname = request.getParameter("customer");
			}
		}
	} else if (functionName.equals("BOOKING")
			|| functionName.equals("FCL_BL_CHARGES")
			|| functionName.equals("EDITBOOKING")
			|| functionName.equals("QUOTE_NEW")) {
		if (request.getParameter("index") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("0")) {
			index = request.getParameter("index");
			customerType = "V";
			acctname = request.getParameter("accountname" + index);
			
		}else if (request.getParameter("index1") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("7")) {
			index = request.getParameter("index1");
			customerType = "V";
			acctname = request.getParameter("collapseaccountname" + index);
			
		} else if (request.getParameter("billThirdPartyName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("1")) {
			customerType ="T";
			acctname = request.getParameter("billThirdPartyName");
		} else if (request.getParameter("fowardername") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("2")) {
			customerType ="F";
			acctname = request.getParameter("fowardername");
		} else if (request.getParameter("consigneename") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("3")) {
			acctname = request.getParameter("consigneename");
		} else if (request.getParameter("shipperName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("4")) {
			acctname = request.getParameter("shipperName");
		} else if (request.getParameter("truckerName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("6")) {
			acctname = request.getParameter("truckerName");
		} else if (request.getParameter("index") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("5")) {
			index = request.getParameter("index");
			customerType = "V";
			acctname = request.getParameter("otheraccountname" + index);
		} else if (request.getParameter("accountName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("7")) {
			if (request.getParameter("pcollect") != null
			&& request.getParameter("pcollect")
			.equalsIgnoreCase("P")) {
		customerType = "F,S,A";
			} else if (request.getParameter("pcollect") != null
			&& request.getParameter("pcollect")
			.equalsIgnoreCase("T")) {
		customerType = "T";
			}

			acctname = request.getParameter("accountName");
		}

	} else if (functionName.equals("FCL_BILL_LADDING")) {
		if (request.getParameter("accountName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("0")) {
			acctname = request.getParameter("accountName");
		} else if (request.getParameter("forwardingAgentName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("1")) {
			acctname = request.getParameter("forwardingAgentName");
		} else if (request.getParameter("fowardername") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("2")) {
			acctname = request.getParameter("fowardername");
		} else if (request.getParameter("consigneeName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("3")) {
			acctname = request.getParameter("consigneeName");
		} else if (request.getParameter("houseName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("4")) {
			acctname = request.getParameter("houseName");
		} else if (request.getParameter("agentName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("5")) {
			acctname = request.getParameter("agentName");
		} else if (request.getParameter("houseConsigneeName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("6")) {
			acctname = request.getParameter("houseConsigneeName");
		} else if (request.getParameter("houseNotifyPartyName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("7")) {
			customerType = "N";
			acctname = request.getParameter("houseNotifyPartyName");
		} else if (request.getParameter("notifyPartyName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("8")) {
			customerType = "N";
			acctname = request.getParameter("notifyPartyName");
		}
	} else if (functionName.equals("CUSTOMER_STATEMENT")) {
		if (request.getParameter("customerName") != null) {
			acctname = request.getParameter("customerName");
		}
	} else if (functionName.equals("ADD_QUOTE")) {
		if (request.getParameter("routedbymsg") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("1")) {
			customerType = "A";
			acctname = request.getParameter("routedbymsg");
		}
	} else if (functionName.equals("SEARCH_FILE")
			&& !functionName.equalsIgnoreCase("")) {
		if (request.getParameter("carrier") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("2")) {
			acctNameForSteamShipLine = request.getParameter("carrier");

		}else if (request.getParameter("forwarder") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("3")) {
			acctname = request.getParameter("forwarder");

		}else if (request.getParameter("conginee") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("4")) {
			acctname = request.getParameter("conginee");

		}else if (request.getParameter("shipper") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("5")) {
			acctname = request.getParameter("shipper");

		}
	} else if (functionName.equals("QUOTE_CHARGES")
			&& !functionName.equalsIgnoreCase("")) {
		if (request.getParameter("vendorName") != null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("0")) {
			acctname = request
			.getParameter("vendorName");
			customerType="V";

		}
	}else if(functionName.equalsIgnoreCase("SEARCH_FCL")&& !functionName.equalsIgnoreCase("")
			||functionName.equalsIgnoreCase("FCL_SELL_RATES")
			||functionName.equalsIgnoreCase("ADD_FCL_POPUP")){
		if(request.getParameter("sslinename")!=null
		&& request.getParameter("from") != null
		&& request.getParameter("from").equals("0")){
		   acctname = request.getParameter("sslinename");
		}
	}else if(functionName.equalsIgnoreCase("BL_CORRECTIONS")&& !functionName.equalsIgnoreCase("")){
		if(request.getParameter("shipper")!=null && request.getParameter("from") != null
		&& request.getParameter("from").equals("0")){
		    acctname = request.getParameter("shipper");
		}else if(request.getParameter("forwarder")!=null && request.getParameter("from") != null
		&& request.getParameter("from").equals("1")){
		   acctname = request.getParameter("forwarder");
		}else if(request.getParameter("thirdParty")!=null && request.getParameter("from") != null
		&& request.getParameter("from").equals("2")){
		   acctname = request.getParameter("thirdParty");
		}
	}
	JSONArray accountNoArray = new JSONArray();

	if (acctname != null && !acctname.trim().equals("")) {
		List list = null;
		CustomerDAO customerDAO = new CustomerDAO();
		if (customerType == null) {
			list = customerDAO.findForAgenttNo(acctno, acctname);
		} else {
			list = customerDAO.findForConsigneetNo(acctno, acctname,customerType);			
		}
		Iterator iter = list.iterator();
		try {
			while (iter.hasNext()) {
		CustomerTemp accountDetails = (CustomerTemp) iter.next();
		if (accountDetails != null) {
			if (functionName.equals("AP_INQUIRY") 
			||functionName.equals("ACCRUALS")
			||functionName.equals("ACCOUNTS_PAYABLE")
			|| functionName.equals("MANIFEST_FOR_TL")
			|| functionName.equals("MANIFEST")
			|| functionName.equals("BANKACCOUNT")
			|| functionName.equals("ACCOUNTS_RECIEVABLE_INQUIRY")
			|| functionName.equals("AR_APPLY_PAYMENTS")
			|| functionName.equals("CLOSE_BATCH_POPUP")) {
			if(!functionName.equals("ACCOUNTS_RECIEVABLE_INQUIRY") &&
			      !functionName.equals("AR_APPLY_PAYMENTS") &&
			      !functionName.equals("CLOSE_BATCH_POPUP")){
			      delimiter=";  ";
			 }     
				String city = accountDetails.getCity2();
				String state = accountDetails.getState();
				String type = accountDetails.getType();
				String addressLine = accountDetails.getAddress1();
				
				if (null != city && !city.trim().equals("")
					&& null != state
					&& !state.trim().equals("")
					&& null != type
					&& !type.trim().equals("")
					&& null != addressLine
					&& !addressLine.trim().equals("") ) {
					accountNoArray.put(accountDetails
					.getAccountName()
					+ delimiter
					+ accountDetails.getAccountNo()
					+ ":-   "
					+ accountDetails.getAddress1()
					+ ",  "
					+ accountDetails.getCity2()
					+ ",  "
					+ accountDetails.getState()
					+ ":-   "
					+ accountDetails.getType());
				}else if (null != city
					&& !city.trim().equals("")
					&& null != state
					&& !state.trim().equals("")
					&& null != type
					&& !type.trim().equals("")) {
					accountNoArray.put(accountDetails
					.getAccountName()
					+ delimiter
					+ accountDetails.getAccountNo()
					+ ":-   "
					+ accountDetails.getCity2()
					+ ",  "
					+ accountDetails.getState()
					+ ":-   "
					+ accountDetails.getType());
			  }  
				else if (null != city
					&& !city.trim().equals("")
					&& null != state
					&& !state.trim().equals("")
					&& null != addressLine
					&& !addressLine.trim().equals("")) {
					accountNoArray.put(accountDetails
					.getAccountName()
					+ delimiter
					+ accountDetails.getAccountNo()
					+ ":-   "
					+ accountDetails.getAddress1()
					+ ",  "
					+ accountDetails.getCity2()
					+ ",  "
					+ accountDetails.getState());
				} else if (null != city
					&& !city.trim().equals("")
					&& null != addressLine
					&& !addressLine.trim().equals("")) {
					accountNoArray.put(accountDetails
					.getAccountName()
					+ delimiter
					+ accountDetails.getAccountNo()
					+ ":-    "
					+ accountDetails.getAddress1()
					+ ",  "
					+ accountDetails.getCity2());
				} else if (null != city
					&& !city.trim().equals("")
					&& null != state
					&& !state.trim().equals("")) {
					accountNoArray.put(accountDetails
					.getAccountName()
					+ delimiter
					+ accountDetails.getAccountNo()
					+ ":-   "
					+ accountDetails.getCity2()
					+ ",  "
					+ accountDetails.getState());
				} else if (null != city
					&& !city.trim().equals("")) {
					accountNoArray.put(accountDetails
					.getAccountName()
					+ delimiter
					+ accountDetails.getAccountNo()
					+ ":-   "
					+ accountDetails.getCity2());
				} else {
			        accountNoArray.put(accountDetails
					.getAccountName()
					+ delimiter
					+ accountDetails.getAccountNo());
				}

			} else {				
				accountNoArray.put(accountDetails
				.getAccountName()+" :- "+accountDetails.getAccountNo());
			}

		}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} else if (acctno != null && !acctno.trim().equals("")) {
		List list = null;
		CustomerDAO customerDAO = new CustomerDAO();
		if (customerType == null) {
			list = customerDAO.findForAgenttNo(acctno, acctname);
		} else {
			list = customerDAO.findForConsigneetNo(acctno, acctname,
			customerType);
		}
		Iterator iter = list.iterator();
		try {
			while (iter.hasNext()) {
		CustomerTemp accountDetails = (CustomerTemp) iter
				.next();
		if (functionName != null
				&& functionName.equals("AR_APPLY_PAYMENTS")) {
			String city = accountDetails.getCity2();
			String state = accountDetails.getState();
			String type = accountDetails.getType();

			if (null != city && !city.trim().equals("")
			&& null != state
			&& !state.trim().equals("") && null != type
			&& !type.trim().equals("")) {
				accountNoArray.put(accountDetails
				.getAccountNo()
				+ ":-    "
				+ accountDetails.getAccountName()
				+ ":-    "
				+ accountDetails.getCity2()
				+ " "
				+ accountDetails.getState()
				+ " "
				+ accountDetails.getType());
			} else if (null != city && !city.trim().equals("")
			&& null != state
			&& !state.trim().equals("")) {
				accountNoArray.put(accountDetails
				.getAccountNo()
				+ ":-    "
				+ accountDetails.getAccountName()
				+ ":-    "
				+ accountDetails.getCity2()
				+ " " + accountDetails.getState());
			} else if (null != city && !city.trim().equals("")) {
				accountNoArray.put(accountDetails
				.getAccountNo()
				+ ":-    "
				+ accountDetails.getAccountName()
				+ ":-    " + accountDetails.getCity2());
			}
		} else {
			accountNoArray.put(accountDetails.getAccountNo());
		}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} else if (acctNameForSteamShipLine != null
			&& !acctNameForSteamShipLine.equals("")) {
		List list = null;
		CustAddressDAO custAddressDAO = new CustAddressDAO();
		list = custAddressDAO.getCustomerForLookUp(
		acctNameForSteamShipLine, "V%SS", null);
		Iterator iter = list.iterator();
		try {
			while (iter.hasNext()) {
		CustAddress custAddress = (CustAddress) iter.next();
		accountNoArray.put(custAddress.getAcctName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}else if (accountName != null
			&& !accountName.equals("")) {
		List list = null;
		CustAddressDAO custAddressDAO = new CustAddressDAO();
		list = custAddressDAO.getCustomerForLookUp(accountName, "", null);
		Iterator iter = list.iterator();
		try {
			while (iter.hasNext()) {
		CustAddress custAddress = (CustAddress) iter.next();
		accountNoArray.put(custAddress.getAcctName()+" /"+custAddress.getAcctNo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
 if("false".equals(request.getParameter("isDojo"))){
     StringBuilder buffer = new StringBuilder("<UL>");
            for(int i =0; i < accountNoArray.length(); i++){
                buffer.append("<li>");
                buffer.append(accountNoArray.get(i));
                buffer.append("</li>");
            }
    buffer.append("</UL>");
    out.println(buffer.toString());
    }else{
	out.println(accountNoArray.toString());
   }
%>

