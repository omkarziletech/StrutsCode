<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String message="";
DBUtil dbUtil=new DBUtil();
request.setAttribute("accountTypelist", dbUtil.getGenericCodeList(26,"yes", "Select Account type"));
String account="";
String Cname="";
if(request.getAttribute("account")!=null)
{
account=(String)request.getAttribute("account");
}
if(session.getAttribute("Notfound")!=null)//if the name to be searched is not there
{
   Cname=(String)session.getAttribute("Notfound");
}

if(account.equals("sameaccount"))
{
%>
	<script>
	 var result =confirm("This Account name already exist, do you want to use same name");
	 if(result)
	 {
	
	 parent.parent.GB_hide();
		  parent.parent.getAddCustomerPage();
<%--		self.close();--%>
<%--		opener.location.href="<%=path%>/jsps/Tradingpartnermaintainance/CustomerFrame.jsp";--%>
	}
	</script>
<%
}
else if(account.equals("account"))
{
%>
	<script>
		 parent.parent.GB_hide();
		  parent.parent.getAddCustomerPage();
		
<%--		self.close();--%>
<%--		opener.location.href="<%=path%>/jsps/Tradingpartnermaintainance/CustomerFrame.jsp";--%>
	
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
		
		
		function searchform1()//search form
		{
		
		if(document.customerCodeForm.name.value=="")
		{
		alert("Please enter the Account Name");
		return;
		}
		document.customerCodeForm.buttonValue.value="search";
		document.customerCodeForm.submit();
		}//
		
		</script>
	</head>
 <body class="whitebackgrnd">
 <html:form action="/customerCode" scope="request">
 <font color="blue" size="2"><%=message%></font>
 <table width="100%"  border="0" cellpadding="0" cellspacing="0" align="center" class="tableBorderNew">
 <tr class="tableHeadingNew" colspan="2">Add Customer Name</tr>
 
 <tr style="padding:bottom:500px;">
    <td>&nbsp;</td>
 </tr>
 <tr style="padding:bottom:500px;">
    <td>&nbsp;</td>
 </tr>
 <tr>
	<td>
	<table>
	   <tr>
			<td class="style2">Name</td>
           	<td ><html:text property="name" value="<%=Cname%>"maxlength="40"  onkeyup="toUppercase(this)" styleClass="areahighlightyellow1" ></html:text></td>
            <td align="right">
           	 <input type="button" class="buttonStyleNew" value="Submit"  id="search" onclick="searchform1()" />
            </td>  
       </tr>
   </table>
  </td>
 </tr>
 <tr style="padding:bottom:500px;">
    <td>&nbsp;</td>
 </tr>
 <tr style="padding:bottom:500px;">
    <td>&nbsp;</td>
 </tr>
 <html:hidden property="buttonValue" styleId="buttonValue" />
 </table>
 </html:form>
 </body>
 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>



