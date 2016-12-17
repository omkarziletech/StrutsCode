<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String message="";
String carrierCode = "";
if(session.getAttribute("Notfound") != null)
{
  carrierCode = session.getAttribute("Notfound").toString();
  session.removeAttribute("Notfound");
}

if(request.getAttribute("message")!=null)
{
message=(String)request.getAttribute("message");
}
String carriercode="";
if(request.getAttribute("carriercode")!=null)
{
carriercode=(String)request.getAttribute("carriercode");
}
if(carriercode.equals("addcarrier"))
{
	
%>
	<script>
	  parent.parent.test();
	   parent.parent.GB_hide();
	  </script>
<%
}
%>
 
<html> 
	<head>
		<title>JSP for CarrierCodeForm form</title>
<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		
		
		function searchform()
		{
		if(document.carrierCodeForm.carrierCode.value=="")
		{
		alert("Please enter the Carrier Code");
		return;
		}
		if(document.carrierCodeForm.carrierCode.value.length<5)
		{
		alert("Please the Carrier Code  should be 5 characters");
		return;
		}
		document.carrierCodeForm.buttonValue.value="search";
		document.carrierCodeForm.submit();
		}
		function toUppercase(obj) 
	{ 
		obj.value = obj.value.toUpperCase();
	}
<%--	function test(){--%>
<%--			window.open("<%= path%>/jsps/datareference/newManagingCarriersOAT.jsp");--%>
<%--			}--%>
        
  	   
 		</script>
 		 <%@include file="../includes/resources.jsp" %>
	</head>
		
	<body class="whitebackgrnd">
		
		<html:form action="/carrierCode" scope="request">
		
			
		<font color="blue" size="2"><%=message%></font>
		<table width="100%"  border="0" cellpadding="0" 
				cellspacing="0" align="center" class="tableBorderNew">
				<tr class="tableHeadingNew"><bean:message key="form.managingCarriersOATForm.addcarriercode"/></tr>
				
    		
  		   <tr height="8">
  		   </tr>
				<tr>
				<td>
				<table>
				<tr>
				<td  class="style2"><bean:message key="form.managingCarriersOATForm.carriercode"/><font color="red">*</font></td>
           		<td ><html:text property="carrierCode"  value="<%=carrierCode %>" maxlength="5" size="5" styleClass="areahighlightyellow1" onkeypress="return checkIts(event)" ></html:text></td>
           		
          <td>
          <input type="button" class="buttonStyleNew" value="Submit"  onclick="searchform()"  /> 
         
            </td>  
            </tr>
            </table>
            </td>
            
           		</tr>
           		<tr>
           			<td>
           				<table>
           				<tr>
           					<td>&nbsp;</td>
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

