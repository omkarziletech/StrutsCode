<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String message="";
String scheduleCode = "";
if(session.getAttribute("Notfound") != null)
{
  scheduleCode = session.getAttribute("Notfound").toString();
  session.removeAttribute("Notfound");
}

if(request.getAttribute("message")!=null)
{
message=(String)request.getAttribute("message");
}
String portcode="";
if(request.getAttribute("portcode")!=null)
{
portcode=(String)request.getAttribute("portcode");
}
if(portcode.equals("portcode"))
{
%>
	<script>
		
		parent.parent.GB_hide();
		parent.parent.getPortAddPage();
	</script>
<%
}
%>
 
<html> 
	<head>
	<%@include file="../includes/resources.jsp" %>	
		<title>JSP for PortCodeForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function searchform()
		{
		if(document.portCodeForm.scheduleCode.value=="")
		{
		alert("Please enter the scheduleCode");
		return;
		}
		if(document.portCodeForm.scheduleCode.value.length<5)
		{
		alert("Please the scheduleCode should be 5 characters");
		return;
		}
		if(document.portCodeForm.controlNo.value=="")
		{
		alert("Please enter the Control Number");
		return;
		}
		if(document.portCodeForm.controlNo.value.length<4)
		{
		alert("Please the Control Number should be 4 characters");
		return;
		}
		document.portCodeForm.buttonValue.value="search";
		document.portCodeForm.submit();
		}
		function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
		</script>
	</head>
	<body class="whitebackgrnd">
<html:form action="/portCode" scope="request">
<table width="100%"  border="0" cellpadding="0" cellspacing="0" align="center" class="tableBorderNew">
<tr class="tableHeadingNew">Add Port Code </tr>
<tr><font color="blue" size="2"><%=message%></font></tr>
<tr>
	<td>
		<table>
			<tr class="style2">
				 <td  class="style2">Schedule Code<font color="red">*</font></td>
           		 <td ><html:text property="scheduleCode" maxlength="5" size="7" onkeyup="toUppercase(this)" value="<%=scheduleCode %>" styleClass="areahighlightyellow1"></html:text></td>
           		 <td width="50%" align="right">Control Number<font color="red">*</font></td>
                 <td ><html:text property="controlNo" maxlength="4" size="5" onkeyup="toUppercase(this)"   styleClass="areahighlightyellow1"></html:text></td>
            </tr>
            </table>
            <table width="55%"> 
            <tr>&nbsp;</tr>
            	<tr>   
          			<td align="right">
		<input type="button" class="buttonStyleNew" id="search" value="Submit"  onclick="searchform()"/>          			
        			
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



