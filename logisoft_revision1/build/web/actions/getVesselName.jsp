<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cvst.logisoft.util.DBUtil,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.GenericCode"
	pageEncoding="ISO-8859-1"%>
<%
	String accNo = "";
	String functionName = null;
        boolean autoCompleteWithId = false;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("BOOKING")) {
		if (request.getParameter("vessel") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			accNo = request.getParameter("vessel");
		}

	} else if (functionName.equals("FCL_BILL_LADDING")) {
		if (request.getParameter("vesselname") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			accNo = request.getParameter("vesselname");
                        autoCompleteWithId = true;
		}else if (request.getParameter("vesnam") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			accNo = request.getParameter("vesnam");
                        autoCompleteWithId = true;
		}

	} else if (functionName.equals("EXPORT_VOYAGE")
	    || functionName.equals("INLAND_VOYAGE")) {
		if (request.getParameter("vesselName") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			accNo = request.getParameter("vesselName");
		}else if (request.getParameter("vesselName1") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			accNo = request.getParameter("vesselName1");
		}

	}

	JSONArray accountNoArray = new JSONArray();
	List<GenericCode> accountList = null;
	if (accNo != null && !accNo.trim().equals("")) {
		GenericCodeDAO batchDAO = new GenericCodeDAO();
		accountList = batchDAO.findForGenericAction(14, null, accNo);
		Iterator iter = accountList.iterator();
		while (iter.hasNext()) {
			GenericCode accountDetails = (GenericCode) iter.next();
			accountNoArray.put(accountDetails.getCodedesc().toString());
		}
	}
        StringBuilder buffer = new StringBuilder("<UL>");
        if(autoCompleteWithId && accountList!=null){
            for(GenericCode genericCode:accountList){
                buffer.append("<li id='"+genericCode.getCode()+"'>");
                buffer.append("<font color='#093ba1'>");
                buffer.append(genericCode.getCodedesc());
                buffer.append("</font>");
                buffer.append("</li>");
            }
             buffer.append("</UL>");
             out.println(buffer.toString());
         }  else{
                if("false".equals(request.getParameter("isDojo"))){
                    for(int i =0; i < accountNoArray.length(); i++){
                        buffer.append("<li>");
                        buffer.append("<font color='#093ba1'>");
                        buffer.append(accountNoArray.get(i));
                        buffer.append("</font>");
                        buffer.append("</li>");
                    }
            buffer.append("</UL>");
            out.println(buffer.toString());
            }else{
                out.println(accountNoArray.toString());
            }
        }
%>

