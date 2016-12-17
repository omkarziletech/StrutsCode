<%@page import="com.logiware.bean.TradeRouteBean"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.UnLocationDAO"%>
<%@ page
	import="com.gp.cong.logisoft.util.AutoCompleter,
    com.gp.cvst.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.hibernate.dao.PortsDAO,com.gp.cong.logisoft.domain.UnLocation,
    com.gp.cvst.logisoft.beans.ChartOfAccountBean,com.gp.cong.logisoft.bc.ratemanagement.UnLocationBC"%>
<%@ page import="java.util.*"%>

<%
	String orgRegion = null;
	String orgRegionDesc = null;
	String terminalNumber = null;
	String functionName = null;
	String originService="";
	String portCity="";
	String unlocCode="";
        String disableDojo=null;
        boolean displayWithId = false;
        boolean stateCode = false;
        boolean countryCode = false;
        boolean countryFlag = false;
        String importFlag = "false";
     String orgTerm="";
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}	
	if (functionName == null) {
		return;
	}
	if (functionName.equals("AR_CREDIT_HOLD")) {
		if (request.getParameter("orgTerminal") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			orgRegion = request.getParameter("orgTerminal");
		}

	} else if (functionName.equals("BOOKING")) {
		if (request.getParameter("originTerminal") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
				
			orgRegionDesc = request.getParameter("originTerminal");
		} else if (request.getParameter("plor") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			orgRegionDesc = request.getParameter("plor");
		} else if (request.getParameter("portOfDischarge") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("2")) {
			orgRegionDesc = request.getParameter("portOfDischarge");
		}else if (request.getParameter("portOfOrigin") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("3")) {
			orgRegionDesc = request.getParameter("portOfOrigin");
		}

	} else if (functionName.equals("QUOTE")) {
		if (request.getParameter("isTerminal") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			orgRegionDesc = request.getParameter("isTerminal");
		} else if (request.getParameter("plor") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			orgRegionDesc = request.getParameter("plor");
		} else if (request.getParameter("portofDischarge") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("2")) {
			orgRegionDesc = request.getParameter("portofDischarge");
		}else if (request.getParameter("inbondPort") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("3")) {
			orgRegionDesc = request.getParameter("inbondPort");
		}
		else if (request.getParameter("placeofReceipt") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("3")) {
			orgRegionDesc = request.getParameter("placeofReceipt");
			portCity="Y";
		}else if (request.getParameter("plor") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("4")) {
			orgRegionDesc = request.getParameter("plor");
			portCity="Y";
		}else if (request.getParameter("pol") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("4")) {
			orgRegionDesc = request.getParameter("pol");
			portCity="Y";
		}else if (request.getParameter("doorOrigin") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("5")) {
				if(null!=request.getParameter("check") && request.getParameter("check").equalsIgnoreCase("true")){
					orgRegionDesc = request.getParameter("doorOrigin");	
				}
		}else if (request.getParameter("pol") != null && request.getParameter("from") != null && request.getParameter("from").equals("6")) {                    
                    if(request.getParameter("countryflag") != null
                        && request.getParameter("countryflag").equals("false")){                        
                        orgRegionDesc = request.getParameter("pol");
                        importFlag = request.getParameter("importFlag");
                        countryFlag = true;
                    }else{
                        orgRegionDesc = request.getParameter("pol");
                        countryFlag = false;
                    }
               }else if (request.getParameter("origin") != null && request.getParameter("from") != null && request.getParameter("from").equals("6")) {                    
                    if(request.getParameter("countryflag") != null
                        && request.getParameter("countryflag").equals("false")){                        
                        orgRegionDesc = request.getParameter("origin");
                        importFlag = request.getParameter("importFlag");
                        countryFlag = true;
                    }else{
                        orgRegionDesc = request.getParameter("origin");
                        countryFlag = false;
                    }
               }

	} else if (functionName.equals("FCL_BILL_LADDING")) {
		if(request.getParameter("terminalName") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			orgRegionDesc = request.getParameter("terminalName");
                        importFlag = request.getParameter("importFlag");
		}else if(request.getParameter("origin") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			orgRegionDesc = request.getParameter("origin");
                        displayWithId = true;
                        stateCode = true;
		}else if(request.getParameter("destn") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("2")) {
			orgRegionDesc = request.getParameter("destn");
                        displayWithId = true;
                        countryCode = true;
		}else if(request.getParameter("pol") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("3")) {
			orgRegionDesc = request.getParameter("pol");
                        displayWithId = true;
		}else if(request.getParameter("pod") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("4")) {
			orgRegionDesc = request.getParameter("pod");
                        displayWithId = true;
		}
	} else if (functionName.equals("FCL_BL")) {
		if (request.getParameter("portofdischarge") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			orgRegionDesc = request.getParameter("portofdischarge");
		}else if(request.getParameter("portofladding") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("2")) {
			orgRegionDesc = request.getParameter("portofladding");
		}

	} else if (functionName.equals("ADD_FCL")
			|| functionName.equals("ADD_FTF_POPUP")
			|| functionName.equals("ADD_LCL_COLOAD_POPUP")
			|| functionName.equals("FCL_SELL_RATES")
			|| functionName.equals("MANAGE_RETAIL_RATES")
			|| functionName.equals("SEARCH_FCL_FUTURE")
			|| functionName.equals("SEARCH_FCL")
			|| functionName.equals("SEARCH_FTF")
			|| functionName.equals("SEARCH_LCL_COLOAD")
			|| functionName.equals("SEARCH_UNIVERSAL")
			|| functionName.equals("RETAIL_ADD_AIR_RATES_POPUP")
                        || functionName.equals("CUSTOMER_ADDRESS")) {
		if (request.getParameter("terminalNumber") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			
			orgTerm = request.getParameter("terminalNumber");
		} else if (request.getParameter("destSheduleNumber") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			orgTerm = request.getParameter("destSheduleNumber");
		}else if (request.getParameter("unLocCode") != null) {
			unlocCode = request.getParameter("unLocCode");
		}

	}

	AutoCompleter autoCompleter = new AutoCompleter();
        StringBuilder buffer = new StringBuilder("");
	if (orgRegion != null && !orgRegion.trim().equals("")) {
		PortsDAO portsDAO = new PortsDAO();
		List portslist = portsDAO.findForUnlocCodeAndPortName(orgRegion,
				orgRegionDesc);
		Iterator iter = portslist.iterator();
		while (iter.hasNext()) {
			Ports accountDetails = (Ports) iter.next();
			autoCompleter.put(accountDetails.getUnLocationCode()
					+ "-" + accountDetails.getPortname());
		}
	} else if (orgRegionDesc != null
			&& !orgRegionDesc.trim().equals("")) {
		PortsDAO portsDAO = new PortsDAO();
		List pierList=new ArrayList();
                if(displayWithId){
                    if(stateCode || countryCode){
                        pierList  = portsDAO.searchForUnlocCodeAndPortNameforPortsTemp(orgRegionDesc.replace("'", "''").replace(" ", "").replace("\"", ""),importFlag);
                    }else{
                        pierList  = portsDAO.findForSchnumAndPortNameforPortsTemp(orgRegionDesc.replace("'", "''").replace(" ", "").replace("\"", ""));
                    }
                    Iterator iter = pierList.iterator();
                     buffer = new StringBuilder("<UL>");
                    while (iter.hasNext()) {
                            TradeRouteBean accountDetails = (TradeRouteBean) iter.next();
                            if(stateCode){
                                if(accountDetails.getStateCode()!=null && !accountDetails.getStateCode().equals("")){
                                    buffer.append("<li id='"+accountDetails.getStateCode()+"'>");
                                    buffer.append("<font class='blue-70'>"+accountDetails.getPortName()+"</font><font class='green'> / "+accountDetails.getStateCode()+
                                        "</font><font class='red-90'> / ("+accountDetails.getUnLocationCode()+") </font>");
                                    buffer.append("</li>");
                                }
                            }else if(countryCode){
                                if(accountDetails.getUnLocationCode()!=null && !accountDetails.getUnLocationCode().equals("")){
                                    String country = new UnLocationBC().getCountryCode(accountDetails.getUnLocationCode());
                                    buffer.append("<li id='"+country+"'>");
                                   if(accountDetails.getStateCode()!=null && !accountDetails.getStateCode().equals("")){
                                        buffer.append("<font class='blue-70'>"+accountDetails.getPortName()+"</font><font class='green'>/"+accountDetails.getStateCode()+
                                            "</font><font class='red-90'>/("+accountDetails.getUnLocationCode()+")</font>");
                                    }else{
                                        buffer.append("<font class='blue-70'>"+accountDetails.getPortName()+"</font><font class='red-90'>/("+accountDetails.getUnLocationCode()+")</font>");
                                    }
                                    buffer.append("</li>");
                                }
                            }else{
                                    buffer.append("<li id='"+accountDetails.getShedulenumber()+"'>");
                                        if(accountDetails.getStateCode()!=null && !accountDetails.getStateCode().equals("")){
                                            buffer.append("<font class='blue-70'>"+accountDetails.getPortName()+"</font><font class='green'>/"+accountDetails.getStateCode()+
                                                "</font><font class='red-90'>/("+accountDetails.getUnLocationCode()+")</font>");
                                        }else{
                                            buffer.append("<font class='blue-70'>"+accountDetails.getPortName()+"</font><font class='red-90'>/("+accountDetails.getUnLocationCode()+")</font>");
                                        }
                                    buffer.append("</li>");
                            }
                    }
                    buffer.append("</UL>");
                    
                }else{
                    if(originService=="Y"){
                        pierList = portsDAO.findForUnlocCodeAndPortNameForOriginService(orgRegion.replace("'", "''").replace(" ", "").replace("\"", ""), orgRegionDesc.replace("'", "''").replace(" ", "").replace("\"", ""),"getUnlocationCode");
                    }else if(portCity=="Y"){
                        pierList = portsDAO.searchForUnlocCodeAndPortNameForDestinationServiceBYPortCity(orgRegionDesc.replace("'", "''").replace(" ", "").replace("\"", ""));
                    }else{
                        pierList = portsDAO.searchForUnlocCodeAndPortNameforPortsTemp(orgRegionDesc.replace("'", "''").replace(" ", "").replace("\"", ""),importFlag);
                    }
                    Iterator iter = pierList.iterator();
                    while (iter.hasNext()) {
                        TradeRouteBean accountDetails = (TradeRouteBean) iter.next();
                            if(accountDetails.getStateCode()!=null && accountDetails.getCountryName()!=null){
                                autoCompleter.put("<font class='blue-70'>"+accountDetails.getPortName()+"</font><font class='green'>/"+accountDetails.getStateCode()+"</font><font class='red'>/"+accountDetails.getCountryName()+
                                "</font><font class='red-90'>("+accountDetails.getUnLocationCode()+")</font>");
                            }else if(accountDetails.getStateCode()!=null){
                                autoCompleter.put("<font class='blue-70'>"+accountDetails.getPortName()+"</font><font class='green'>/"+accountDetails.getStateCode()+
                                "</font><font class='red-90'>("+accountDetails.getUnLocationCode()+")</font>");
                             }else if(accountDetails.getCountryName()!=null){
                                autoCompleter.put("<font class='blue-70'>"+accountDetails.getPortName()+"</font><font class='green'>/"+accountDetails.getCountryName()+
                                "</font><font class='red-90'>("+accountDetails.getUnLocationCode()+")</font>");
                             }else{
                                autoCompleter.put("<font class='blue-70'>"+accountDetails.getPortName()+"</font><font class='red-90'>/("+accountDetails.getUnLocationCode()+")</font>");
                            }
                    }
                }

	} else if (terminalNumber != null
			&& !terminalNumber.trim().equals("")) {
		PortsDAO portsDAO = new PortsDAO();
		List pierList = portsDAO.findForUnlocCodeAndPortName(terminalNumber.replace("'", "''").replace(" ", "").replace("\"", ""),
				orgRegionDesc.replace("'", "''").replace(" ", "").replace("\"", ""));
		Iterator iter = pierList.iterator();
		while (iter.hasNext()) {

			Ports accountDetails = (Ports) iter.next();
			autoCompleter.put(accountDetails.getUnLocationCode() + "/"
					+ accountDetails.getPortname());
		}
	}else if (orgTerm != null
			&& !orgTerm.trim().equals("")) {
		PortsDAO portsDAO = new PortsDAO();
		List pierList = portsDAO.findForUnlocCodeAndPortName(orgTerm.replace("'", "''").replace(" ", "").replace("\"", ""),
				orgRegionDesc.replace("'", "''").replace(" ", "").replace("\"", ""));
		Iterator iter = pierList.iterator();
		while (iter.hasNext()) {
			Ports accountDetails = (Ports) iter.next();
			autoCompleter.put("<font class='blue-70'>"+accountDetails.getUnLocationCode() + "</font><font class='green'> :- "
					+ accountDetails.getPortname()+"</font>");
		}
	}else if (unlocCode != null
			&& !unlocCode.trim().equals("")) {
		UnLocationDAO unLocationDAO = new UnLocationDAO();
		List pierList = unLocationDAO.findForUnlocCode(unlocCode.replace("'", "''").replace(" ", "").replace("\"", ""));
		Iterator iter = pierList.iterator();
		while (iter.hasNext()) {
			UnLocation unLocation = (UnLocation) iter.next();
                        if(null != unLocation.getUnLocationCode() && !unLocation.getUnLocationCode().equals("")){
                            autoCompleter.put("<font class='blue-70'>"+unLocation.getUnLocationCode()+"</font>");
                        }
		}
	}
        if(displayWithId){
            out.println(buffer.toString());
        }else{
            out.println(autoCompleter.toString());
        }
%>
