<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String message="";
String wareHouseCode = "";

if(session.getAttribute("Notfound") != null)
{
  wareHouseCode = session.getAttribute("Notfound").toString();
  session.removeAttribute("Notfound");
}

if(request.getAttribute("message")!=null)
{
message=(String)request.getAttribute("message");
}
String warehousecode="";

if(request.getAttribute("warehousecode")!=null)
{
warehousecode=(String)request.getAttribute("warehousecode");
}
if(warehousecode.equals("addwarehouse"))
{
%>
	<script>
		
		parent.parent.GB_hide();
		parent.parent.disabled("20");
<%--		self.close();--%>
<%--		opener.location.href="<%=path%>/jsps/datareference/addWarehouse.jsp";--%>
	</script>
<%
}
%>
 
<html> 
	<head>
		<title>JSP for WarehouseCodeForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function searchform()
		{
		if(document.warehouseCodeForm.warehouseCode.value=="")
		{
		alert("Please enter the Warehouse Code");
		return;
		}
		if(document.warehouseCodeForm.warehouseCode.value.length!=4)
 		{  
 		alert("Please the Warehouse Code should be 4 characters");
  		return;
  		}  
		
		document.warehouseCodeForm.buttonValue.value="search";
		document.warehouseCodeForm.submit();
		}
		function toUppercase(obj) 
	{
		obj.value = obj.value.toUpperCase();
	}
		
		</script>
		<%@include file="../includes/resources.jsp" %>
	</head>

  <body class="whitebackgrnd" >
		<html:form action="/warehouseCode" scope="request">
			<font color="blue" size="2"><%=message%></font>
		<table width="100%"  border="0" cellpadding="0" 
				cellspacing="0" align="center" class="tableBorderNew">
				
				<tr height="8"></tr>
				<tr class="tableHeadingNew">Add Warehouse Code  </tr>
  		   <tr height="8">
  		   </tr>
				<tr>
				<td>
				<table>
				<tr>
				<td class="style2">Whrs Code<font color="red">*</font></td>
           		<td ><html:text property="warehouseCode" maxlength="4" value="<%=wareHouseCode %>" size="3" style="text-transform: uppercase" styleClass="areahighlightyellow1" ></html:text></td>
           		
          <td>
          <input type="button" class="buttonStyleNew" value="Submit" id="search"  onclick="searchform()"   /> 
         
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

