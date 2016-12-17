<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.PortsTemp,com.gp.cong.logisoft.hibernate.dao.PortsDAO,com.gp.cong.logisoft.domain.Ports"%>

<% 
	String pierCode = "";
	String portName = "";
	String scheduleCode = "";
	String functionName = null;
        boolean unlocationCode= false;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
  
	if (functionName.equals("CARRIER")) {
		if (request.getParameter("portcode") != null) {
			scheduleCode = request.getParameter("portcode");
		}

	}else if (functionName.equals("EDIT_TERMINAL")||functionName.equals("NEW_TERMINAL")) { 
		if (request.getParameter("govSchCode") != null) {
			scheduleCode = request.getParameter("govSchCode");
		}
                if (request.getParameter("unLocCode") != null) {
			scheduleCode = request.getParameter("unLocCode");
                        unlocationCode= true;
		}
                if (request.getParameter("unLocationCode1") != null) {
			scheduleCode = request.getParameter("unLocationCode1");
                        unlocationCode= true;
		}

	}else if (functionName.equals("MANAGE_PORTS")) {
		if (request.getParameter("sheduleCode") != null) {
		scheduleCode = request.getParameter("sheduleCode");
	    }

	}else if ( functionName.equals("ADD_FUTURE_FCL_POPUP")
	|| functionName.equals("ADD_LCL_COLOAD_POPUP")
	|| functionName.equals("PRAC_MANAGE_RETAIL_RATES")
	|| functionName.equals("RETAIL_ADD_AIR_RATES_POPUP")
	|| functionName.equals("UNIVERSAL_ADD_POPUP")
	|| functionName.equals("INLAND_VOYAGE")
	|| functionName.equals("EXPORT_VOYAGE")) {
		if (request.getParameter("destSheduleNumber") != null & request.getParameter("from")!=null && request.getParameter("from").equals("0")) {
		pierCode = request.getParameter("destSheduleNumber");

		}else if (request.getParameter("destAirportname") != null && request.getParameter("from")!=null && request.getParameter("from").equals("1")) {
			portName = request.getParameter("destAirportname");
    	}else if (request.getParameter("podNo") != null && request.getParameter("from")!=null && request.getParameter("from").equals("2")) {
			pierCode = request.getParameter("podNo");
    	}else if (request.getParameter("podName") != null && request.getParameter("from")!=null && request.getParameter("from").equals("3")) {
			portName = request.getParameter("podName");
    	}else if (request.getParameter("destination") != null && request.getParameter("from")!=null && request.getParameter("from").equals("4")) {
			pierCode = request.getParameter("destination");
    	}else if (request.getParameter("destinationName") != null && request.getParameter("from")!=null && request.getParameter("from").equals("5")) {
			portName = request.getParameter("destinationName");
    	}else if (request.getParameter("optITPort") != null && request.getParameter("from")!=null && request.getParameter("from").equals("6")) {
			pierCode = request.getParameter("optITPort");
    	}else if (request.getParameter("portName") != null && request.getParameter("from")!=null && request.getParameter("from").equals("7")) {
			portName = request.getParameter("portName");
    	}
    }else if (functionName.equals("RELAY_INQUIRY")) {
	 if (request.getParameter("destinationCode") != null ) {
		pierCode = request.getParameter("destinationCode");
	}

	else  if (request.getParameter("originCode") != null ) {
		pierCode = request.getParameter("originCode");
	}
	}else if (functionName.equals("RELAY_CODE")) {
	   	if (request.getParameter("pol") != null && request.getParameter("from")!=null && request.getParameter("from").equals("0")) {
		    	pierCode = request.getParameter("pol");
	 	}else 	if (request.getParameter("polText") != null && request.getParameter("from")!=null && request.getParameter("from").equals("1")) {
		    	portName = request.getParameter("polText");
	 	}else 	if (request.getParameter("pod") != null && request.getParameter("from")!=null && request.getParameter("from").equals("2")) {
		    	pierCode = request.getParameter("pod");
	 	}else 	if (request.getParameter("podText") != null && request.getParameter("from")!=null && request.getParameter("from").equals("3")) {
		    	portName = request.getParameter("podText");
	 	}
    
    
	}
	else if(functionName.equals("ADD_FTF_POPUP")){
		if (request.getParameter("destSheduleNumber") != null && request.getParameter("from")!=null && request.getParameter("from").equals("0")) {
			pierCode = request.getParameter("destSheduleNumber");
    	}else if (request.getParameter("destAirportname") != null && request.getParameter("from")!=null && request.getParameter("from").equals("1")) {
			portName = request.getParameter("destAirportname");
    	}
	}else if(functionName.equals("AR_CREDIT_HOLD")){
		if (request.getParameter("destination") != null ) {
			portName = request.getParameter("destination");
    	}
	}
	
	
	
	
	


	

	JSONArray accountNoArray = new JSONArray();
	
	if (scheduleCode != null && !scheduleCode.equals("")) {
		PortsDAO portsDAO = new PortsDAO();
		List portslist = portsDAO.findForUnlocCodeAndPortName(scheduleCode,
		null);
		Iterator iter = portslist.iterator();
		while (iter.hasNext()) {
			Ports accountDetails = (Ports) iter.next();
                        if(unlocationCode){
                            accountNoArray.put(accountDetails.getUnLocationCode());
                        }else{
                            accountNoArray.put(accountDetails.getUnLocationCode()
                            + ":- " + accountDetails.getPortname());
                        }
		}

	}

	if (portName != null && !portName.trim().equals("")
			) {
		PortsDAO portsDAO = new PortsDAO();
		List pierList = portsDAO.findForUnlocCodeAndPortName(pierCode, portName);
		Iterator iter = pierList.iterator();
		while (iter.hasNext()) {

			Ports accountDetails = (Ports) iter.next();
			accountNoArray.put(accountDetails.getPortname()+"; "+accountDetails.getUnLocationCode());
		}
	}
    	if (pierCode != null && !pierCode.trim().equals("")) {
		PortsDAO portsDAO = new PortsDAO();
		List pierList = portsDAO.findForUnlocCodeAndPortName(pierCode, portName);
		Iterator iter = pierList.iterator();
		while (iter.hasNext()) {

			Ports accountDetails = (Ports) iter.next();
			accountNoArray.put(accountDetails.getUnLocationCode()+"; "+accountDetails.getPortname());
		}
	}
	//out.println(accountNoArray.toString());
        StringBuilder buffer = new StringBuilder("<UL>");
        if("false".equals(request.getParameter("isDojo"))){
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
