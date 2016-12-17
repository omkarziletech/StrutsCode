<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO,com.gp.cong.logisoft.domain.CarriersOrLineTemp"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO"%>
<%@page import="com.gp.cvst.logisoft.domain.CustAddress"%>
<%
	String sslineno = "";
	String sslinename = "";
	String accountType = null;
	String functionName = null;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("MANAGING_CARRIERS_OAT")) {
		if (request.getParameter("carriercode") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			sslineno = request.getParameter("carriercode");
		}

	} else if (functionName.equals("BOOKING")
			|| functionName.equals("QUOTE")) {
		if (request.getParameter("sslDescription") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			sslinename = request.getParameter("sslDescription");
		} else if (request.getParameter("carrier") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			sslinename = request.getParameter("carrier");
			accountType = "SS";
		} else if (request.getParameter("sslBooking") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("2")) {
			sslinename = request.getParameter("sslBooking");
			accountType = "SS";
		}

	} else if (functionName.equals("ADD_FCL_POPUP")
			|| functionName.equals("ADD_FUTURE_FCL_POPUP")
			|| functionName.equals("FCL_SELL_RATES")
			|| functionName.equals("SEARCH_FCL_FUTURE")
			|| functionName.equals("SEARCH_FCL")
			|| functionName.equals("EXPORT_VOYAGE")) {
		if (request.getParameter("sslinename") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			
			sslinename = request.getParameter("sslinename");
		}else if (request.getParameter("lineName1") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			
			sslinename = request.getParameter("lineName1");
		}

	} else if (functionName.equals("FCL_BILL_LADDING")) {
		if (request.getParameter("streamShipName") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			sslinename = request.getParameter("streamShipName");
		}

	}

	JSONArray accountNoArray = new JSONArray();

	if (sslinename != null && !sslinename.trim().equals("") && accountType==null) {
		CarriersOrLineDAO carriersOrLineDAO = new CarriersOrLineDAO();
		List codeList = carriersOrLineDAO.findForSSLine(sslineno,sslinename);
		Iterator iter = codeList.iterator();

		while (iter.hasNext()) {
			CarriersOrLineTemp accountDetails = (CarriersOrLineTemp) iter
					.next();
			accountNoArray.put(accountDetails.getCarriername());
		}

	}
	if (sslineno != null && !sslineno.trim().equals("")) {
		CarriersOrLineDAO carriersOrLineDAO = new CarriersOrLineDAO();
		List codeList = carriersOrLineDAO.findForSSLine(sslineno,
				sslinename);
		Iterator iter = codeList.iterator();

		while (iter.hasNext()) {
			CarriersOrLineTemp accountDetails = (CarriersOrLineTemp) iter
					.next();
			accountNoArray.put(accountDetails.getCarriercode() + ":-"
					+ accountDetails.getCarriername());
		}

	}
	if(accountType!=null && !accountType.equals("")){
	   CustAddressDAO custAddressDAO = new CustAddressDAO();
	   List codeList = custAddressDAO.findBy1(sslinename,null,null,accountType);
		Iterator iter = codeList.iterator();
		while (iter.hasNext()) {
			CustAddress custAddress = (CustAddress) iter.next();
			accountNoArray.put(custAddress.getAcctName() + ":-"+ custAddress.getAcctNo());
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