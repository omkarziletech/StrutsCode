<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<jsp:directive.page import="com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO"/>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
GenericCodeDAO genericCodeDAO  = new GenericCodeDAO();
List listOfRemarks = genericCodeDAO.findForChargeCodesForAirRates(null,null,"53");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'remarksForFclBL.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <%if(listOfRemarks!=null && listOfRemarks.size()>0){
						 	int i=0; 
						 	 %>
    <display:table name="<%=listOfRemarks%>"  class="displaytagstyle" 
 			id="remarks" sort="list" style="width:100%" pagesize="<%=pageSize%>"> 
 			<%
  			            String remarksValue="";
  						GenericCode genericCode=(GenericCode)listOfRemarks.get(i);
  						if(genericCode.getCodedesc()!=null){
  						  remarksValue=genericCode.getCodedesc().toString();
  						}
  						String index=Integer.toString(i);
  			        %>
  			           
  			   <display:column title="" style="width:20px;">
  			   		<input type="checkbox" property="rcheck" value="<%=index%>"/>
  			   </display:column>   
  	           <display:column title="Remarks"><%=remarksValue%></display:column>
  
				<%i++;%>
		</display:table>  
  </body>
</html>
