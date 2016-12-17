<%@ page language="java" import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.PortsDAO,com.gp.cong.logisoft.domain.CustomerTemp,org.json.JSONObject" pageEncoding="ISO-8859-1"%>
<%

String requestFor = request.getParameter("requestFor");
String agent = request.getParameter("agent");
String finalDestination = request.getParameter("finalDestination");
String portOfdischarge=request.getParameter("portofDischarge");
String destination=request.getParameter("destination");

PortsDAO portsDAO=new PortsDAO();		
if (requestFor == null) {
		return;
	}
JSONObject jsonObj = new JSONObject();

	if (requestFor.equals("AGENT")) {
		List portsList = portsDAO.getPortsForAgentInfo(finalDestination);
		if(portsList.size()==0){
			jsonObj.put("Agent","");
			jsonObj.put("AgentNo","");
		}
	}else if(requestFor.equals("PODAGENT")){
	List portsList = portsDAO.getPortsForAgentInfo(portOfdischarge);
		if(portsList.size()==0){
			jsonObj.put("Agent","");
			jsonObj.put("AgentNo","");
		}
	}else if(requestFor.equals("DESTAGENT")){
	List portsList = portsDAO.getPortsForAgentInfoWithDefault(destination,"");		
        CustomerTemp customerTemp=(CustomerTemp)portsList;	
        jsonObj.put("agent",customerTemp.getAccountName());
        jsonObj.put("agentNo",customerTemp.getAccountNo());
	}
	out.println(jsonObj.toString());
%>


