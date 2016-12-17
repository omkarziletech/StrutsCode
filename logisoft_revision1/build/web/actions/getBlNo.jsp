<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.CustomerDAO,com.gp.cong.logisoft.domain.CustomerTemp"%>
<%@page import="com.gp.cvst.logisoft.hibernate.dao.FclBlDAO"%>
<%@page import="com.gp.cvst.logisoft.domain.FclBl"%>
<%
	String billOfLaddingNumber = "";
	String functionName = null;
	String blNumber="";
	String readyToPost="";
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	
	if (functionName == null) {
		return;
	}
	if (functionName.equals("ARINVOICEGENERATOR")) {
		if (request.getParameter("bl_drNumber") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			billOfLaddingNumber = request.getParameter("bl_drNumber");

		}
	}
	else if(functionName.equals("ACCRUALS")){
		if (request.getParameter("blNumber") != null)
 			{
			billOfLaddingNumber = request.getParameter("blNumber");
			}
	}else if(functionName.equals("FCL_BL_CORRECTION")){
		if(request.getParameter("blNumber")!=null && request.getParameter("from")!=null
		&& request.getParameter("from").equalsIgnoreCase("0")){ 
			blNumber = request.getParameter("blNumber");
		}
	}
	
	JSONArray accountNoArray = new JSONArray();
	if (billOfLaddingNumber != null && !billOfLaddingNumber.trim().equals("")) {
		FclBlDAO fclBlDAO=new FclBlDAO();
		List customerList = fclBlDAO.getAllBlNumbers(billOfLaddingNumber);
		Iterator iter = customerList.iterator();
		while (iter.hasNext()) {
			FclBl fclBl = (FclBl) iter.next();
			accountNoArray.put(fclBl.getBolId());
		}
	}
	if(blNumber!=null && !blNumber.trim().equals("")){
	    FclBlDAO fclBlDAO=new FclBlDAO();
	    readyToPost="M";
		List customerList = fclBlDAO.getManifestedBlNumbers(blNumber,readyToPost);
		Iterator iter = customerList.iterator();
		while (iter.hasNext()) {
			FclBl fclBl = (FclBl) iter.next();
			accountNoArray.put(fclBl.getBolId());
		}
	}
	out.println(accountNoArray.toString());
%>
