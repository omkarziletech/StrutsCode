<%@ page language="java" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String message="";

if(request.getAttribute("message")!=null)
{
message=(String)request.getAttribute("message");
}

String itemcode="";
if(request.getAttribute("itemcode")!=null)
{
itemcode=(String)request.getAttribute("itemcode");
}
if(itemcode.equals("additem"))
{
%>
	<script>
	parent.parent.GB_hide();
		parent.parent.getItemPage();
		reload_on_close = true;
<%--		self.close();--%>
<%--		opener.location.href="<%=path%>/jsps/admin/addMenuaction.jsp";--%>
	</script>
<%
}
%>
 
<html> 
	<head>
		<title>JSP for ItemCodeForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function searchform()
		{
		if(document.itemCodeForm.itemName.value=="")
		{
		alert("Please enter the Item Name");
		return;
		}
		document.itemCodeForm.buttonValue.value="search";
		document.itemCodeForm.submit();
		}
		function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
		</script>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/itemCode" scope="request">
		
			
		
<table width="100%"  border="0" cellpadding="0" cellspacing="0" align="center" class="tableBorderNew">
<tr><td><font color="blue" size="2"><%=message%></font></td></tr>
<tr class="tableHeadingNew" colspan="2">Add Item Name</tr>
<tr>
   <td>
	  <table>
		   <tr>
				<td width="80" class="style2">Item Name</td>
           		<td ><html:text property="itemName" maxlength="30" size="10" styleClass="areahighlightyellow"></html:text></td>
           	    <td>
	      <input type="button" class="buttonStyleNew" onclick="searchform()" name="search" value="Submit"/>
          </tr>
      </table>
   </td>
</tr>
<tr>
   <td><table><tr>
      <td>&nbsp;</td>
</tr></table>
</td></tr>
</table>
        <html:hidden property="buttonValue" styleId="buttonValue" />
    </html:form>
	</body>
	
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

