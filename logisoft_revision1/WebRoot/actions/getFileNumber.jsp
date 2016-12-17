<%-- 
    Document   : getZipCode.jsp
    Created on : Oct 26, 2010, 6:05:41 PM
    Author     : Pradeep
--%>

<%@ page language="java"
	import="java.util.*"%>


<%
 StringBuffer stringBuffer = new StringBuffer("");
 String fileNo = request.getParameter("bl_drNumber");
 String disableDojo = request.getParameter("disableDojo");
   List<String> fileNoList = new ArrayList<String>();
    if(com.gp.cong.common.CommonUtils.isNotEmpty(fileNo)){
      com.gp.cvst.logisoft.hibernate.dao.FclBlDAO fclBlDAO = new com.gp.cvst.logisoft.hibernate.dao.FclBlDAO();
      fileNoList = fclBlDAO.getAllFileNumber(fileNo);
    }
    stringBuffer.append("<ul>");
    for(String string: fileNoList){
        if(null != string && !"".equals(string)){
           stringBuffer.append("<li id = '04-"+string+"'>");
           stringBuffer.append("04-"+string);
           stringBuffer.append("</li>");
        }
   }
   stringBuffer.append("</ul>");
    if(null!=disableDojo){//-----This is to disable the dojo in a textBox when a checkBox is clicked.----
        out.println("");
    }else{
        out.println(stringBuffer.toString());
    }
//zipCodeDAO.
%>