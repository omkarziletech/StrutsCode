<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <link rel="stylesheet" href="<%=path%>/css/default/style.css" type="text/css" media="print, projection, screen" />
  </head>
  <body class="whitebackgrnd">
   		<display:table  name="${blVoidList}"   id="blvoidTable" class="displaytagstyle"> 
		<display:setProperty name="paging.banner.placement" ><span style="display:none;"></span></display:setProperty>
        <display:column title="BOLID" property="billLaddingNo"></display:column>
        <display:column title="Transaction Date">
        <fmt:formatDate pattern="dd-MMM-yyyy" var="transactionDate" value="${blvoidTable.transactionDate}"/>
        ${transactionDate}
        </display:column>
        <display:column title="Cust Name" property="custName"></display:column>
        <display:column title="Cust No" property="custNo"></display:column>
        <display:column title="Charge Code" property="chargeCode"></display:column>
        <display:column title="Amount" property="transactionAmt"></display:column>
   </display:table>
  </body>
</html>
