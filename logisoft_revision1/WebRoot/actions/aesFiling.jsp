<%@page import="com.gp.cong.logisoft.domain.User"%>
<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.GenericCode"%>
<%@page import="org.apache.struts.util.LabelValueBean"%>
<%@page import="com.gp.cvst.logisoft.AccountingConstants"%>
<%
try{
	String schedBNo = null;
	String schedBName = null;
        String searchBy = request.getParameter("searchBy");
        if(null != searchBy && !searchBy.equals("")){
            if("Number".equalsIgnoreCase(searchBy)){
                if (request.getParameter("scheduleB_Number") != null) {
                    schedBNo = request.getParameter("scheduleB_Number");
                }
            }else if("Name".equalsIgnoreCase(searchBy)){
                if (request.getParameter("scheduleB_Name") != null) {
                    schedBName = request.getParameter("scheduleB_Name");
                }
            }
        }
        
	StringBuffer accountNoArray = new StringBuffer();
	accountNoArray.append("<ul style='width: 575px'>");
	if (schedBNo != null && !schedBNo.trim().equals("")) {
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
                List list =genericCodeDAO.getFieldForSchedbNo(schedBNo);
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
                    GenericCode genericCode = (GenericCode)iter.next();
                    String code = null != genericCode ? genericCode.getCode() : "";
                    String codeDesc = null != genericCode ? genericCode.getCodedesc() : "";
                    accountNoArray.append("<li>");
                    accountNoArray.append("<font class='red-90'>");
                    accountNoArray.append(code);
                    accountNoArray.append("</font><font class='blue-70'> <--> ");
                    accountNoArray.append(codeDesc);
                    accountNoArray.append("</font>");
                    accountNoArray.append("</li>");
		}
	}else if (schedBName != null && !schedBName.trim().equals("")) {
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
                List list =genericCodeDAO.getFieldForSchedbName(schedBName);
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
                    GenericCode genericCode = (GenericCode)iter.next();
                    String code = null != genericCode ? genericCode.getCode() : "";
                    String codeDesc = null != genericCode ? genericCode.getCodedesc() : "";
                    accountNoArray.append("<li id = '"+code+"'>");
                    accountNoArray.append("<font class='blue-70'>");
                    accountNoArray.append(codeDesc);
                    accountNoArray.append("</font> <--> <font class='red-90'>").append(code);
                    accountNoArray.append("</font>");
                    accountNoArray.append("</li>");
		}
	}
      accountNoArray.append("</ul>");
      out.println(accountNoArray.toString());
}catch(Exception e){
	e.printStackTrace();
}
%>