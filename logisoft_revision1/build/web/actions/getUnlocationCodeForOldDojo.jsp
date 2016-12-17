<%@ page
	import="com.gp.cong.logisoft.util.AutoCompleter,
    com.gp.cvst.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.hibernate.dao.PortsDAO,com.gp.cong.logisoft.domain.UnLocation,com.gp.cvst.logisoft.beans.ChartOfAccountBean"%>
<%@ page import="java.util.*"%>
<jsp:directive.page import="org.json.JSONArray"/>

<%  
	String orgRegion = null;
	String orgRegionDesc = null;
	String terminalNumber = null;
	String functionName = null;
	String originService="";
	String portCity="";
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
			originService="Y";
		}else if (request.getParameter("doorOrigin") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("5")) {
			orgRegionDesc = request.getParameter("doorOrigin");
			
		}

	} else if (functionName.equals("FCL_BILL_LADDING")) {
		if (request.getParameter("terminalName") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			orgRegionDesc = request.getParameter("terminalName");
		}

	} else if (functionName.equals("FCL_BL")) {
		if (request.getParameter("portofdischarge") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			orgRegionDesc = request.getParameter("portofdischarge");
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
			|| functionName.equals("RETAIL_ADD_AIR_RATES_POPUP")) {
		if (request.getParameter("terminalNumber") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			
			orgTerm = request.getParameter("terminalNumber");
		} else if (request.getParameter("destSheduleNumber") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			orgTerm = request.getParameter("destSheduleNumber");
		}

	}
	JSONArray autoCompleter = new JSONArray();
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
		if(originService=="Y"){
		pierList = portsDAO.findForUnlocCodeAndPortNameForOriginService(orgRegion, orgRegionDesc,"getUnlocationCodeForOldDojo");
		}else if(portCity=="Y"){
		pierList = portsDAO.findForUnlocCodeAndPortNameForDestinationServiceBYPortCity(orgRegion, orgRegionDesc);
		
		}else{
		pierList = portsDAO.findForUnlocCodeAndPortNameforPortsTemp(orgRegion, orgRegionDesc);
		}
		Iterator iter = pierList.iterator();
		while (iter.hasNext()) {

			PortsTemp accountDetails = (PortsTemp) iter.next();			
			if(accountDetails.getStateCode()!=null){
				autoCompleter.put(accountDetails.getPortname()+"/"+accountDetails.getStateCode()+
			 	"/("+accountDetails.getUnLocationCode()+")");
			}else{
				autoCompleter.put(accountDetails.getPortname()+"/("+accountDetails.getUnLocationCode()+")");
			}
		}

	} else if (terminalNumber != null
			&& !terminalNumber.trim().equals("")) {
		PortsDAO portsDAO = new PortsDAO();
		List pierList = portsDAO.findForUnlocCodeAndPortName(terminalNumber,
				orgRegionDesc);
		Iterator iter = pierList.iterator();
		while (iter.hasNext()) {

			Ports accountDetails = (Ports) iter.next();
			autoCompleter.put(accountDetails.getUnLocationCode() + "/"
					+ accountDetails.getPortname());
		}
	}else if (orgTerm != null
			&& !orgTerm.trim().equals("")) {
		PortsDAO portsDAO = new PortsDAO();
		List pierList = portsDAO.findForUnlocCodeAndPortName(orgTerm,
				orgRegionDesc);
		Iterator iter = pierList.iterator();
		while (iter.hasNext()) {
			Ports accountDetails = (Ports) iter.next();
			autoCompleter.put(accountDetails.getUnLocationCode() + ":-"
					+ accountDetails.getPortname());
		}
	} 
	out.println(autoCompleter.toString()); 
%>
