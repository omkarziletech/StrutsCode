<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String message="";
String unitNo = "";

if(session.getAttribute("Notfound") != null)
{
  unitNo = session.getAttribute("Notfound").toString();
  session.removeAttribute("Notfound");
}

if(request.getAttribute("message")!=null)
{
message=(String)request.getAttribute("message");
}
String unitno="";

if(request.getAttribute("unitno")!=null)
{
unitno=(String)request.getAttribute("unitno");
}
if(unitno.equals("addunitno"))
{
%>
	<script>
		self.close();
		opener.location.href="<%=path%>/jsps/Containermanagement/CreateUnit.jsp";
	</script>
<%
}
%>
<html> 
	<head>
		<title>JSP for AddUnitpopupForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function searchform()
		{
		if(document.addUnitpopupForm.unitNo.value=="")
		{
		alert("Please enter the UnitNo");
		return;
		}
		 
		document.addUnitpopupForm.buttonValue.value="search";
		document.addUnitpopupForm.submit();
		}
		function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
		</script>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/addUnitpopup" scope="request">
			<font color="blue" size="2"><%=message%></font>
		<table width="100%"  border="0" cellpadding="0" 
				cellspacing="0" align="center">
				
				<tr height="8"></tr>
				<tr>
    		<td height="15"  class="headerbluesmall">&nbsp;&nbsp;Add UnitNo </td>
    		<td></td>
    		<td></td>
  		   </tr>
  		   <tr height="8">
  		   </tr>
				<tr>
				<td>
				<table>
				<tr>
				<td class="style2">UnitNo</td>
           		<td ><html:text property="unitNo"  value="<%=unitNo %>" size="17" onkeyup="toUppercase(this)" styleClass="areahighlightyellow1" ></html:text></td>
           		
          <td><img src="<%=path%>/img/go.gif" id="search" border="0" onclick="searchform()" 
              	   style="cursor: pointer; cursor: hand;"/>
            </td>  
            </tr>
            </table>
            </td>
            
           		</tr>
           		</table>
           		<html:hidden property="buttonValue" styleId="buttonValue" />

		</html:form>
	</body>
	
	
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

