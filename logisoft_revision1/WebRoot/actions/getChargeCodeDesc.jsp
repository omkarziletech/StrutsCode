<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.GenericCode"%>

<%
	String code = "";
	String codeDesc = "";
	String codeType = null;
	String index = "";
	String functionName = null;
	String codeForOperation = "";
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}

	if (functionName.equals("SEARCH_FCL")
			|| functionName.equals("FCL_SELL_RATES")) {
		if (request.getParameter("orgRegion") != null
				&& null != request.getParameter("from")
				&& !request.getParameter("from").equals("")
				&& request.getParameter("from").equals("0")) {
			codeDesc = request.getParameter("orgRegion");
			codeType = "19";
		} else if (request.getParameter("destRegion") != null
				&& null != request.getParameter("from")
				&& !request.getParameter("from").equals("")
				&& request.getParameter("from").equals("1")) {
			codeDesc = request.getParameter("destRegion");
			codeType = "19";
		} else if (request.getParameter("comDescription") != null
				&& null != request.getParameter("from")
				&& !request.getParameter("from").equals("")
				&& request.getParameter("from").equals("2")) {
			codeDesc = request.getParameter("comDescription");
			codeType = "4";
		}

	} else if (functionName.equals("GLOBAL_RATES")) {
		if (request.getParameter("originRegion") != null
				&& null != request.getParameter("from")
				&& !request.getParameter("from").equals("")
				&& request.getParameter("from").equals("0")) {
			codeDesc = request.getParameter("originRegion");
			codeType = "19";
		} else if (request.getParameter("destinationRegion") != null
				&& null != request.getParameter("from")
				&& !request.getParameter("from").equals("")
				&& request.getParameter("from").equals("1")) {
			codeDesc = request.getParameter("destinationRegion");
			codeType = "19";
		}

	} else if (functionName.equals("ADD_FCL")) {
		if (request.getParameter("orgRegion") != null
				&& request.getParameter("from").equals("0")) {
			codeDesc = request.getParameter("orgRegion");
			codeType = "19";
		} else if (request.getParameter("destRegion") != null
				&& request.getParameter("from").equals("1")) {
			codeDesc = request.getParameter("destRegion");
			codeType = "19";
		} else if (request.getParameter("comDescription") != null
				&& request.getParameter("from").equals("2")) {
			codeDesc = request.getParameter("comDescription");
			codeType = "4";
		}

	} else if (functionName.equals("FCL_BL_CHARGES")) {
		if (request.getParameter("index") != null
				&& !request.getParameter("index").equals("")) {
			index = request.getParameter("index");
			codeDesc = request.getParameter("charge" + index);
			codeType = "36";
		} else if (request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			codeDesc = request.getParameter("chargeCodeDesc");
			codeType = "36";
		}
	} else if (functionName.equals("AIR_RATES")
			|| functionName.equals("FLIGHT_SCHEDULE")
			|| functionName.equals("MANAGE_AIR_RATES")) {
		if (request.getParameter("terminalName") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			codeType = "1";
			codeDesc = request.getParameter("terminalName");
		} else if ((request.getParameter("destAirportname") != null && request
				.getParameter("from").equals("1"))) {
			codeType = "1";
			codeDesc = request.getParameter("destAirportname");
		} else if ((request.getParameter("comDescription") != null && request
				.getParameter("from").equals("2"))) {
			codeType = "4";
			codeDesc = request.getParameter("comDescription");
		}
	} else if (functionName.equals("AGSC_FTF")
			|| functionName.equals("FTF_COMMODITY")
			|| functionName.equals("AGSS")
			|| functionName.equals("LCL_COMMODITY")
			|| functionName.equals("CSSS")
			|| functionName.equals("RETAIL_AGSC")
			|| functionName.equals("RETAIL_CSSC")
			|| functionName.equals("UNIVERSAL_COMMODITY")) {
		if (request.getParameter("desc") != null
				&& !request.getParameter("desc").equals("")) {
			codeDesc = request.getParameter("desc");
			codeType = "2";
		}
	} else if (functionName.equals("BOOKING")) {
		if (request.getParameter("comdesc") != null
				&& !request.getParameter("comdesc").equals("")) {
			codeDesc = request.getParameter("comdesc");
			codeType = "4";
		}
	} else if (functionName.equals("QUOTE")) {
		if (request.getParameter("description") != null
				&& !request.getParameter("description").equals("")) {
			codeDesc = request.getParameter("description");
			codeType = "4";
		}
	} else if (functionName.equals("RETAIL_ADD_AIR_RATES_POPUP")
			|| functionName.equals("SEARCH_FCL_FUTURE")
			|| functionName.equals("MANAGE_RETAIL_RATES")
			|| functionName.equals("ADD_LCL_COLOAD_POPUP")
			|| functionName.equals("ADD_FUTURE_FCL_POPUP")
			|| functionName.equals("ADD_FTF_POPUP")
			|| functionName.equals("SEARCH_FTF")
			|| functionName.equals("SEARCH_LCL_COLOAD")
			|| functionName.equals("SEARCH_UNIVERSAL")
			|| functionName.equals("UNIVERSAL_ADD_POPUP")) {
		if (request.getParameter("comDescription") != null
				&& !request.getParameter("comDescription").equals("")) {
			codeDesc = request.getParameter("comDescription");
			codeType = "4";
		}
	} else if (functionName.equals("GENERAL_INFORMATION")) {
		if (request.getParameter("commDesc") != null
				&& !request.getParameter("commDesc").equals("")
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			codeDesc = request.getParameter("commDesc");
			codeType = "4";
		} else if (request.getParameter("impCommodityDesc") != null
				&& !request.getParameter("impCommodityDesc").equals("")
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			codeDesc = request.getParameter("impCommodityDesc");
			codeType = "4";
		} else if (request.getParameter("salesCode") != null
				&& !request.getParameter("salesCode").equals("")
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("2")) {
			code = request.getParameter("salesCode");
			codeType = "23";
		} else if (request.getParameter("salesCodeName") != null
				&& !request.getParameter("salesCodeName").equals("")
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("3")) {
			codeDesc = request.getParameter("salesCodeName");
			codeType = "23";
		}
	} else if (functionName.equals("VOYAGE_EXPORT")) {
		if (request.getParameter("reasonDescription") != null
				&& !request.getParameter("reasonDescription")
						.equals("")
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			codeDesc = request.getParameter("reasonDescription");
			codeType = "15";
		}
	} else if (functionName.equals("AR_INVOICE")) {
		if (request.getParameter("chargeCodeDesc") != null
				&& !request.getParameter("chargeCodeDesc").equals("")
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			codeDesc = request.getParameter("chargeCodeDesc");
			codeType = "2";
		}
	} else if (functionName.equals("GOODS_REMARKS")) {
		if (request.getParameter("goodsdescTemp") != null
				&& !request.getParameter("goodsdescTemp").equals("")
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			code = request.getParameter("goodsdescTemp");
			codeType = "53";
		} else if (request.getParameter("commentTemp") != null
				&& !request.getParameter("commentTemp").equals("")
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			code = request.getParameter("commentTemp");
			codeType = "53";
		}
	} else if (functionName.equalsIgnoreCase("OPERATION")) {
		if (null != request.getParameter("hotCodes")
				&& null != request.getParameter("from")
				&& request.getParameter("from").equals("1")) {
			code = request.getParameter("hotCodes");
			codeType = "57";
		}else if (null != request.getParameter("hotCodes1")
				&& null != request.getParameter("from")
				&& request.getParameter("from").equals("2")) {
			code = request.getParameter("hotCodes1");
			codeType = "57";
		}
	}
	
	JSONArray accountNoArray = new JSONArray();

	if (codeDesc != null && !codeDesc.trim().equals("")) {
		GenericCodeDAO genericDAO = new GenericCodeDAO();
		List codeList = genericDAO.findForChargeCodesForAirRates(code,
				codeDesc, codeType);
		Iterator iter = codeList.iterator();
		while (iter.hasNext()) {

			GenericCode accountDetails = (GenericCode) iter.next();
			accountNoArray.put(accountDetails.getCodedesc().toString());
		}
		out.println(accountNoArray.toString());
	} else if (code != null && !code.trim().equals("")) {
		GenericCodeDAO genericDAO = new GenericCodeDAO();
		List codeList = genericDAO.findForChargeCodesForAirRates(code,
				codeDesc, codeType);
		Iterator iter = codeList.iterator();
		while (iter.hasNext()) {

			GenericCode accountDetails = (GenericCode) iter.next();
			if (functionName.equals("OPERATION") || functionName.equals("GENERAL_INFORMATION")) {
				accountNoArray.put(accountDetails.getCode() + ":-"
						+ accountDetails.getCodedesc());
			} else {
				accountNoArray.put(accountDetails.getCode() + ": "
						+ accountDetails.getCodedesc());
			}

		}
		out.println(accountNoArray.toString());
	}
%>
