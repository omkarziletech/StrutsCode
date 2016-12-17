<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cvst.logisoft.util.DBUtil,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.GenericCode"
	pageEncoding="ISO-8859-1"%>
<%
	String accNo = "";
	String index = "";
	int codeTypeId = 0;
	String functionName = null;
	List accountList = null;
	JSONArray accountNoArray = new JSONArray();
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("BL_CLAUSE")) {
		if (request.getParameter("index") != null) {
			index = request.getParameter("index");
			accNo = request.getParameter("clause" + index);
			codeTypeId = 21;
			

		}

	} else if (functionName.equals("FCL_BILL_LADDING")) {
		if (request.getParameter("vessel") != null) {
			accNo = request.getParameter("vessel");
			codeTypeId = 14;

		}

	} else if (functionName.equals("EXPORT_VOYAGE")
			|| functionName.equals("INLAND_VOYAGE")) {
		if (request.getParameter("vessel") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			codeTypeId = 14;
			accNo = request.getParameter("vessel");

		} else if (request.getParameter("vesselNum") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			accNo = request.getParameter("vesselNum");
			codeTypeId = 14;

		} else if (request.getParameter("vessel1") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("2")) {
			accNo = request.getParameter("vessel1");
			codeTypeId = 14;

		}

	}
	
	if (accNo != null && !accNo.trim().equals("")) {
		GenericCodeDAO batchDAO = new GenericCodeDAO();
		accountList = batchDAO.findForGenericAction(codeTypeId, accNo,
				null);
		Iterator iter = accountList.iterator();
		while (iter.hasNext()) {
			GenericCode accountDetails = (GenericCode) iter.next();
			accountNoArray.put(accountDetails.getCode().toString()
					+ ":-" + accountDetails.getCodedesc());
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

