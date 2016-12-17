<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String message="";
if(request.getAttribute("message")!=null)
{
message=(String)request.getAttribute("message");
}
String terminalcode="";
if(request.getAttribute("terminalcode")!=null)
{
terminalcode=(String)request.getAttribute("terminalcode");
}
if(session.getAttribute("Notfound") != null)
{
  terminalcode = session.getAttribute("Notfound").toString();
  session.removeAttribute("Notfound");
}


if(terminalcode.equals("addterminal"))
{
%>
	<script>
	parent.parent.GB_hide();
	 parent.parent.getAddPage();
 </script>
<%
}
%>
<html> 
	<head>
		<title>JSP for TerminalCodeForm form</title>
<%@include file="../includes/baseResources.jsp" %>

	<script type="text/javascript">
		function searchform()
		{
		if(document.terminalCodeForm.trmnum.value=="")
		{
		alert("Please enter the Terminal Number");
		return;
		}
		document.terminalCodeForm.buttonValue.value="search";
		document.terminalCodeForm.submit();
		}
		function toUppercase(obj) 
		{
		obj.value = obj.value.toUpperCase();
		}
	</script>
	<%@include file="../includes/resources.jsp" %>
	</head>
	<body class="whitebackgrnd" >
		<html:form action="/terminalCode" scope="request">
			
			<font color="blue" size="3"><%=message%></font>
		
		<table width="100%"  border="0" cellpadding="0" 
				cellspacing="0" class="tableBorderNew">
				
				<tr class="tableHeadingNew">
    		Add Terminal Number 
    		<td></td>
    		<td></td>
  		   </tr>
  		   <tr height="8">
  		   </tr>
				<tr>
				<td>
				<table>
				<tr>
				<td  class="style2">Terminal Number<font color="red">*</font></td>
           		<td width="80"><html:text property="trmnum" maxlength="3" size="3" styleClass="areahighlightyellow1"  value="<%=terminalcode %>" onkeypress=" return checkItzero(event)" /></td>
           		
          <td>
          <input type="button" class="buttonStyleNew" value="Submit" id="search" onclick="searchform()"  /> 
        
            </td>  
            </tr>
            </table>
            </td>
            
           		</tr>
           		<tr>
           			<td>
           				<table>
           					<tr>
           						<td>
           							&nbsp;
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

