<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page language="java"  import="java.util.*,com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.domain.*,com.gp.cvst.logisoft.beans.TransactionBean,com.gp.cvst.logisoft.reports.dto.AgingReportPeriodDTO"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="includes/jspVariables.jsp" %>
 
<html> 
	<head>
		<title>JSP for ArManifestAgeingForm form</title>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
if(path==null)
{
 path="../..";
}
CustAddress custAddress=new CustAddress();
String CustomerName="";
String CustomerNo="";
List cust=null;
String less30="";
String from30to60="";
String from60to90="";
String above90="";
if(session.getAttribute("batch")!=null)
{

 custAddress = (CustAddress)session.getAttribute("batch");
if(custAddress.getAcctName()!=null)
{

CustomerName = custAddress.getAcctName();

}
if(custAddress.getAcctNo()!=null)
{
CustomerNo = custAddress.getAcctNo();
}
//PaymentOnAccountDAO ponAcctDao=new PaymentOnAccountDAO();
//double cb=ponAcctDao.getOnAccountBalance(CustomerNo);
//currentBalance=Double.toString(cb);
}
AgingReportPeriodDTO agReportPeriodDTO=new AgingReportPeriodDTO();
if(request.getAttribute("Manifestamount")!=null)
{
agReportPeriodDTO=(AgingReportPeriodDTO)request.getAttribute("Manifestamount");
 
//AgingReportPeriodDTO agingReport=new AgingReportPeriodDTO();
less30=agReportPeriodDTO.getRangeone();
from30to60=agReportPeriodDTO.getRangetwo();
from60to90=agReportPeriodDTO.getRangethree();
above90=agReportPeriodDTO.getRangefour();
}
%>


<%@include file="includes/baseResources.jsp" %>

<script type="text/javascript">

function popup(mylink, windowname)
{
if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname, 'width=500,height=200,scrollbars=yes');
return false;
}
function manifest()
{
alert("Hai");
document.arManifestAgeingForm.button.value="manifest";
document.arManifestAgeingForm.submit();
}
function save()
{
alert("Hai");
document.arManifestAgeingForm.button.value="save";
document.arManifestAgeingForm.submit();
}
</script>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/arManifestAgeing" name="arManifestAgeingForm" type="com.gp.cvst.logisoft.struts.form.ArManifestAgeingForm" scope="request">
		<html:hidden property="button"/>
		<table width="812" cellpadding="0" cellspacing="0">
<tr class="textlabels">
<td width="589">
  <p align="left" class="headerbluelarge">ArManifestAgeing</p>
</td>
<td width="82"><img src="<%=path%>/img/save.gif" width="72" height="23" border="0" onclick="save()" /></td>
<td width="82"><img src="<%=path%>/img/manifest.gif" width="72" height="23" border="0" onclick="manifest()" /></td>

</tr>
<tr>
<td height="12"  class="headerbluesmall" colspan="10">&nbsp;&nbsp;ArManifestAgeing</td>
</tr>
</table>
<table>
<tr class="textlabels">
<td valign="top" height="17">Customer Name</td>
    <td  class="textlabels">&nbsp;<html:text property="custName"  value="<%=CustomerName %>" styleClass="areahighlightgrey" />
        <img src="<%=path%>/img/search1.gif" height="16" onclick="return popup('<%=path%>/jsps/AccountsRecievable/customerSearch.jsp?button='+'armanifestaging&amp;customersearch='+document.arManifestAgeingForm.custName.value,'windows3')"/></td>
     </tr>
     <tr class="textlabels">
     <td>CustomerNumber</td>
      <td>&nbsp;<html:text property="customerNumber"  value="<%=CustomerNo %>"  styleClass="areahighlightgrey" /></td>
        
  </tr>
<tr class="textlabels">
<td valign="top" height="17">Less30</td>
<td valign="top" height="17">30-60</td>
<td valign="top" height="17">60-90</td>
<td valign="top" height="17">Great90</td>
</tr>
<tr>
<td valign="top"><html:text property="less30" value="<%=less30%>"></html:text></td>
<td valign="top"><html:text property="from30to60" value="<%=from30to60%>"></html:text></td>
<td valign="top"><html:text property="from60to90" value="<%=from60to90%>"></html:text></td>
<td valign="top"><html:text property="great90" value="<%=above90%>"></html:text></td>
</tr>
</table>
	
		</html:form>
	</body>
</html>

