<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cvst.logisoft.util.DBUtil,com.gp.cong.logisoft.hibernate.dao.VoyageExportDAO,com.gp.cong.logisoft.domain.VoyageExport"
	pageEncoding="ISO-8859-1"%>
<%

	String accNo = "";
	String functionName = null;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}

	if (functionName.equals("FCL_BILL_LADDING")
			|| functionName.equals("FCL_BL")) {
		if (request.getParameter("voyage") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			accNo = request.getParameter("voyage");
		}
	}

	int accNumber=0;
	//if(accNo!=null && !accNo.equals("")){
	//	accNumber = Integer.parseInt(accNo);	
	//}
	JSONArray accountNoArray = new JSONArray();
	List accountList = null;
	if (accNo != null && !accNo.trim().equals("")) {
		VoyageExportDAO batchDAO = new VoyageExportDAO();
		accountList = batchDAO.getListofVoyages(accNo);
		Iterator iter = accountList.iterator();
		while (iter.hasNext()) {
			VoyageExport accountDetails = (VoyageExport) iter.next();
			accountNoArray.put(accountDetails.getInternalVoyage()
					.toString());
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

