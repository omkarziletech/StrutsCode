<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String message="";
String Cname="";
if(request.getAttribute("message")!=null)
{
message=(String)request.getAttribute("message");
}
DBUtil dbUtil=new DBUtil();
request.setAttribute("accountTypelist", dbUtil.getGenericCodeList(26,"yes", "Select Account type"));
String account="";
if(request.getAttribute("account")!=null)
{
account=(String)request.getAttribute("account");
}
if(session.getAttribute("Notfound")!=null) //name isnt found then pass it to add form
{
  Cname=(String)session.getAttribute("Notfound");
}
 
 if(account.equals("sameaccount"))
{
%>
	<script>
	 var result =confirm("This Account name already exist");
	 
	</script>
<%
}
 else if(account.equals("account"))
{
%>
	<script>
		  parent.parent.GB_hide();
		  parent.parent.getAddMasterCustomerPage();
<%--		self.close();--%>
<%--		opener.location.href="<%=path%>/jsps/Tradingpartnermaintainance/MasterCustomerFrame.jsp";--%>
	
	</script>
<%
}
%>
 
<html> 
	<head>
		<title>JSP for CustomerCodeForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script type="text/javascript">
		function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
		function searchform1()
		{
		
		if(document.masterCustomerCodeForm.name.value=="")
		{
		alert("Please enter the Account Name");
		return;
		}
		document.masterCustomerCodeForm.buttonValue.value="search";
		document.masterCustomerCodeForm.submit();
		}
		
		</script>
	</head>
	<body class="whitebackgrnd">
<html:form action="/masterCustomerCode" scope="request">
<font color="blue" size="2"><%=message%></font>
<table width="100%"  border="0" cellpadding="0" class="tableBorderNew" cellspacing="0" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">Add Customer Name</tr>
  <tr style="padding:bottom:500px;">
    <td>&nbsp;</td>
 </tr>
  
   <tr>		
     <td class="style2">&nbsp;Name</td>
     <td align="left"><html:text property="name" maxlength="40" value="<%=Cname%>" onkeyup="toUppercase(this)" styleClass="areahighlightyellow1" ></html:text>
      <input type="button" class="buttonStyleNew" value="Submit"  id="search" onclick="searchform1()" />
   </tr>
   <tr style="padding:bottom:500px;">
    <td>&nbsp;</td>
 </tr>
 <tr style="padding:bottom:500px;">
    <td>&nbsp;</td>
 </tr>
</table>
    <html:hidden property="buttonValue" styleId="buttonValue" />
    </html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>





