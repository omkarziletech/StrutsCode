<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO,com.gp.cong.logisoft.domain.CarriersOrLineTemp"
	pageEncoding="ISO-8859-1"%>
<%
	String sslineno = "";
	String sslinename = "";
	String functionName = null;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("MANAGING_CARRIERS_OAT")) {
		if (request.getParameter("carriername") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			sslinename = request.getParameter("carriername");
		}
	} else if (functionName.equals("QUOTE")) {
		if (request.getParameter("sslcode") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			sslineno = request.getParameter("sslcode");
		}
	} else if (functionName.equals("FCL_BILL_LADDING")
			|| functionName.equals("EXPORT_VOYAGE")
			|| functionName.equals("ADD_FCL_POPUP")
			|| functionName.equals("ADD_FUTURE_FCL_POPUP")
			|| functionName.equals("SEARCH_FCL_FUTURE")
			|| functionName.equals("FCL_SELL_RATE")
			|| functionName.equals("SEARCH_FCL")) {
		if (request.getParameter("sslinenumber") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			sslineno = request.getParameter("sslinenumber");
		}else if (request.getParameter("lineNo1") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			sslineno = request.getParameter("lineNo1");
		}
	}
    JSONArray accountNoArray = new JSONArray();

	if (sslineno != null && !sslineno.trim().equals("")) {
		CarriersOrLineDAO carriersOrLineDAO = new CarriersOrLineDAO();
		List codeList = carriersOrLineDAO.findForSSLine(sslineno,
				sslinename);
		Iterator iter = codeList.iterator();

		while (iter.hasNext()) {
			CarriersOrLineTemp accountDetails = (CarriersOrLineTemp) iter
					.next();
			accountNoArray.put(accountDetails.getCarriercode()+":-"+accountDetails.getCarriername());
		}

	}
	if (sslinename != null && !sslinename.trim().equals("")) {
		CarriersOrLineDAO carriersOrLineDAO = new CarriersOrLineDAO();
		List codeList = carriersOrLineDAO.findForSSLine(sslineno,
				sslinename);
		Iterator iter = codeList.iterator();

		while (iter.hasNext()) {
			CarriersOrLineTemp accountDetails = (CarriersOrLineTemp) iter
					.next();
			accountNoArray.put(accountDetails.getCarriername());
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