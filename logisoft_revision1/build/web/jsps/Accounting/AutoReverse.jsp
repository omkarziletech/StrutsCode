<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cvst.logisoft.util.DBUtil,java.util.*,com.gp.cvst.logisoft.domain.FiscalPeriod,com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%
String path = request.getContextPath();
DBUtil dbUtil=new DBUtil();
List periodList=new ArrayList();
request.setAttribute("periodList",dbUtil.getperiodList());
String period="0";
Integer p1;
FiscalPeriod fiscalPeriod=new FiscalPeriod();
FiscalPeriodDAO fiscalperiodDAO=new FiscalPeriodDAO();
if(session.getAttribute("period")!=null)
{
period=(String)session.getAttribute("period");
p1=Integer.parseInt(period)+1;
period=String.valueOf(p1);
}
%>
<html> 
	<head>
		<title>JSP for AddLineItemForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script type="text/javascript">
		function addline()
		{
		document.autoReverseForm.buttonValue.value="go";
		document.autoReverseForm.submit();
		}
		</script>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/autoReverse" scope="request">
			<table width=100% border="0" cellpadding="0" cellspacing="0">
     <tr>
    <td  class="headerbluesmall" colspan="10">&nbsp;&nbsp;Auto Reverse </td> 
  </tr>
</table>

  <table width="100%" border="0" cellpadding="0" cellspacing="0" >
    <tr class="textlabels">
      
      <td>&nbsp;</td>
     <td></td>
      <td><img src="<%=path%>/img/go.gif" width="72" height="23" onclick="addline()"/></td>
      
    </tr>
    <tr class="textlabels">
      <td>Period</td>
       <td><html:select property="period" styleClass="smalltextstyle" value="<%=period%>">
      <html:optionsCollection name="periodList" styleClass="unfixedtextfiledstyle" />
      </html:select></td>
    </tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue"/>
		</html:form>
	</body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

