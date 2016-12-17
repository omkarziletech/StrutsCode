<%@ page language="java" import="java.util.*,com.gp.cong.logisoft.domain.FclOrgDestMiscData,com.gp.cvst.logisoft.beans.TransactionBean" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String originTerminal=null;
String destinationPort=null;
String ssline=null;
String daysintransit=null;
String remarks=null;
String localdrayage=null;
TransactionBean transactionBean=new TransactionBean();
transactionBean.setLocaldryage("N");
FclOrgDestMiscData fclOrgDestMiscData=new FclOrgDestMiscData();
if(request.getAttribute("originalTerminal")!=null)
{
originTerminal=(String)request.getAttribute("originalTerminal");
}
if(request.getParameter("ssline")!=null){
ssline=(String)request.getAttribute("ssline");
}
if(request.getAttribute("destinationPort")!=null)
{
destinationPort=(String)request.getAttribute("destinationPort");
}

if(request.getAttribute("fclOrgDestData")!=null)
{
fclOrgDestMiscData=(FclOrgDestMiscData)request.getAttribute("fclOrgDestData");
if(fclOrgDestMiscData.getDaysInTransit()!=null)
{
daysintransit=fclOrgDestMiscData.getDaysInTransit().toString();
}
if(fclOrgDestMiscData.getRemarks()!=null)
{
remarks=fclOrgDestMiscData.getRemarks();
}
transactionBean.setLocaldryage(fclOrgDestMiscData.getLocalDrayage());
}
request.setAttribute("transactionBean",transactionBean);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title> FclOrgDestMiscData</title>
    
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		
		function saveRecord(val1,val2,val3){
		document.searchFCLForm.terminalNumber.value=val1;
		document.searchFCLForm.destSheduleNumber.value=val2;
		document.searchFCLForm.sslinenumber.value=val3;
		document.searchFCLForm.buttonValue.value="saveForRemarksPage";
		document.searchFCLForm.submit();
		}
		
		</script>
  </head>
  <body >
  <html:form action="/fclSellRates" scope="request">
  
  <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
  <tr class="tableHeadingNew" colspan="2">Enter Remarks</tr>
  <tr class="style2">
	  <td>Transit Time</td>
	  <td><html:text property="daysInTransit" value="<%=daysintransit%>"></html:text></td>
	  <td>Local Drayage</td>
	  <td><html:radio property="localdryage" value="Y" name="transactionBean"/></td>
	  <td>Y</td>
	  <td><html:radio property="localdryage" value="N" name="transactionBean"/></td>
	  <td>N</td>
  </tr>
  <tr class="style2">
	  <td>Remarks</td>
	  <td><html:textarea property="remarks" cols="16" rows="3" value="<%=remarks%>"></html:textarea></td>
  </tr>
  <tr>
  	  <td style="padding-left:15px;">&nbsp;</td>
      <td align="right"><input type="button" value="Save" class="buttonStyleNew" onclick="saveRecord('<%=originTerminal%>','<%=destinationPort%>','<%=ssline%>')"/></td>
  </tr>
  </table>
  <html:hidden property="buttonValue" styleId="buttonValue" />
  <html:hidden  property="terminalNumber" value="<%=originTerminal%>" />
  <html:hidden  property="destSheduleNumber" value="<%=destinationPort%>" />
  <html:hidden  property="sslinenumber" value="<%=ssline%>" />
  </html:form>
  </body>
  <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
