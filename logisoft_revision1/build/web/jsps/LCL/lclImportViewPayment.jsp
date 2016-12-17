<%@include file="init.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<body style="background:#ffffff">
    <cong:form id="lclImportPaymentForm" name="lclImportPaymentForm" action="/lclImportPayment">
        <table width="99%" border="0" cellpadding="1" cellspacing="1" class="tableBorderNew" align="center">
            <tr class="tableHeadingNew"><td>View Payments</td></tr>
            <display:table  name="${translist}" id="transObj" class="dataTable" sort="list">
                <display:column title="Charge Code">
                    ${transObj[0]}
                </display:column>
                <display:column title="Amount">
                    ${transObj[1]}
                </display:column>
            </display:table>
        </table>
    </cong:form>
</body>