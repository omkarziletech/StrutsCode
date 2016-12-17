<%@ page language="java"
	import="java.util.*,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.GenericCode"%>

<%

	String code = "";
	String codeDesc = "";
	String codeType = "";
	String transactionType = "AR";
	String shipmentType = "FCLE";
	String functionName = null;

	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (null!=request.getParameter("import") && request.getParameter("import").equalsIgnoreCase("true")) {
		shipmentType = "FCLI";
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("ACCRUALS")) {
		if (request.getParameter("costCode") != null) {
			codeType = "36";
			code = request.getParameter("costCode");			
		}
	} else if (functionName.equals("AP_INVOICE")){
		if (request.getParameter("chargeCode") != null) {
			codeType = "36";
			code = request.getParameter("chargeCode");
		}
	} else if (functionName.equals("GLMAPPING")){
		if (request.getParameter("chargeCode") != null) {
			codeType = "36";
			code = request.getParameter("chargeCode");
		}
	}else if (functionName.equals("FCL_BL_CHARGES")) {
		if(request.getParameter("from") != null
			&& request.getParameter("from").equals("6")){
			if( request.getParameter("chargeCodeDesc") != null){
				codeType = "36";
				codeDesc = request.getParameter("chargeCodeDesc");
			}
		}else if(request.getParameter("from") != null
			&& request.getParameter("from").equals("7")){
		    if( request.getParameter("costCodeDesc") != null){
                        	 transactionType = "AC";
				codeType = "36";
				codeDesc = request.getParameter("costCodeDesc");
			}
		}else if(request.getParameter("from") != null
			&& request.getParameter("from").equals("8")){
		    if( request.getParameter("chargeDescription") != null){
				codeType = "36";
				codeDesc = request.getParameter("chargeDescription");
			}
		}
	}else if (functionName.equals("FCLBLCORRECTIONS")) {	
	   if(request.getParameter("from") != null
			&& request.getParameter("from").equals("1")){
			codeType = "36";
			code = request.getParameter("chargeCode");
	   }
	}
	StringBuffer stringBuffer = new StringBuffer();
	stringBuffer.append("<ul>");
	
	if (code != null && !code.trim().equals("")) {
		GenericCodeDAO genericDAO = new GenericCodeDAO();
		List<Object[]> codeList = new ArrayList<Object[]>();
		if (codeType != null && !codeType.equals("")) {
                    codeList = genericDAO.getChargeCode(code, null, shipmentType, transactionType);
		} 
		if(null!=codeList){
			if (functionName.equals("FCL_BL_CHARGES")||functionName.equals("FCLBLCORRECTIONS")) {
				for(Object[] genericCode : codeList) {
					stringBuffer.append("<li id='"+ genericCode[1].toString() + "'>"
						+ "<b><font class='blue-70'>"+genericCode[0].toString()
						+ " </font><font class='green'> <--> " + genericCode[1].toString()
						+ "</font></b></li>");
				}
			}else{
				for(Object[] genericCode : codeList) {
					stringBuffer.append("<li id='"+ genericCode[0].toString() + "'>"
						+ "<b><font class='blue-70'>"+genericCode[0].toString()
						+ " </font><font class='green'> <--> " + genericCode[1].toString()
						+ "</font></b></li>");
				}
			}
		}
	}
	if(codeDesc!=null && !codeDesc.trim().equals("")){
	    GenericCodeDAO genericDAO = new GenericCodeDAO();
		List<Object[]> codeList = new ArrayList<Object[]>();
		if (codeType != null && !codeType.equals("")) {
                    codeList = genericDAO.getChargeCode(null, codeDesc, shipmentType, transactionType);
		} 
		if(null!=codeList){
		     for(Object[] genericCode : codeList) {
					stringBuffer.append("<li id='"+ genericCode[0].toString() + "'>"
						+ "<b><font class='blue-70'>"+genericCode[1].toString()
						+ " </font><font class='green'> <--> " +genericCode[0].toString()
						+ "</font></b></li>");
			 }
			
		}
	}
	stringBuffer.append("</ul>");	
	out.println(stringBuffer.toString());
%>
