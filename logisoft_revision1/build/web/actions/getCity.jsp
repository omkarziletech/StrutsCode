<%@ page language="java"
	import="java.util.*,org.json.JSONArray,java.util.*,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.hibernate.dao.PortsDAO,com.gp.cong.logisoft.domain.UnLocation,com.gp.cong.logisoft.domain.Customer"
	pageEncoding="ISO-8859-1"%>
<jsp:directive.page
	import="com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;" />
<%
	String cityid = "";
	String functionName = null;
	String orgRegion="";

	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}

	if (functionName.equals("WAREHOUSE")
			|| functionName.equals("EDIT_TERMINAL")
			|| functionName.equals("NEW_TERMINAL")
			|| functionName.equals("ADDRESSES")) {
		if (request.getParameter("city") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			cityid = request.getParameter("city");

		} else if (request.getParameter("acCity") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			cityid = request.getParameter("acCity");

		}
	}

	JSONArray accountNoArray = new JSONArray();
	StringBuffer cityBuilder = new StringBuffer();
	cityBuilder.append("<ul>");
    if (cityid != null && !cityid.trim().equals("")) {
		String city="",country="",state="",unLoc="";
		PortsDAO portsDAO = new PortsDAO();
		List pierList = portsDAO.findForUnlocCodeAndPortName(orgRegion, cityid);
		Iterator iter = pierList.iterator();
		while (iter.hasNext()) {
			Ports accountDetails = (Ports) iter.next();
			if(functionName.equals("ADDRESSES") &&  request.getParameter("from") != null
				&& request.getParameter("from").equals("0")){
				city=accountDetails.getPortname();
				country=accountDetails.getCountryName();
				state=accountDetails.getStateCode();
				unLoc=accountDetails.getUnLocationCode();
				if(null != state && !state.equalsIgnoreCase("")){
					cityBuilder.append("<li id='"+unLoc+"'><b>"+
					city+"/"+state+"/"+country+"("+unLoc+")</b></li>");
				}else{
					cityBuilder.append("<li id='"+unLoc+"'><b>"+
					city+"/"+country+"("+unLoc+")</b> </li>");
				}
			}else if((functionName.equals("EDIT_TERMINAL")||functionName.equals("NEW_TERMINAL")) &&  request.getParameter("from") != null
				&& request.getParameter("from").equals("0")){
                                city=accountDetails.getPortname();
                                country=accountDetails.getCountryName();
				state=accountDetails.getStateCode();
				unLoc=accountDetails.getUnLocationCode();
				if(null != state && !state.equalsIgnoreCase("")){
					cityBuilder.append("<li><b>"+
					city+"/"+country+"/"+unLoc+"/"+state+"</b></li>");
				}else{
					cityBuilder.append("<li><b>"+
					city+"/"+country+"/"+unLoc+"</b></li>");
				}
                        }else{
				accountNoArray.put(accountDetails.getPortname()+":- "+accountDetails.getUnLocationCode());
                        }
		}

    }
    cityBuilder.append("</ul>");
	
    if(functionName.equals("ADDRESSES") &&  request.getParameter("from") != null
				&& request.getParameter("from").equals("0")){
		out.println(cityBuilder.toString());
    }else if((functionName.equals("EDIT_TERMINAL")||functionName.equals("NEW_TERMINAL")) &&  request.getParameter("from") != null
				&& request.getParameter("from").equals("0")){
        out.println(cityBuilder.toString());
    }else{
    	out.println(accountNoArray.toString());
    }
	
%>
