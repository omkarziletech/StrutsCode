<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <title>Search Ap Invoice</title>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <c:set var="accessMode" value="1"/>
        <c:set var="canEdit" value="true"/>
        <c:if test="${param.accessMode==0}">
            <c:set var="accessMode" value="0"/>
            <c:set var="canEdit" value="false"/>
        </c:if>
    </head>
    <body class="whitebackgrnd">
        <html:form action="/apinvoice?accessMode=${accessMode}" name="apInvoiceForm" type="com.gp.cvst.logisoft.struts.form.APInvoiceForm" scope="request">
            <html:hidden property="buttonValue" value="" styleId="buttonValue"/>
            <html:hidden property="apInvoiceChargesId"/>
            <html:hidden property="apInvoiceId"/>
            <html:hidden property="currentPageSize" styleId="currentPageSize"/>
            <html:hidden property="pageNo" styleId="pageNo"/>
            <html:hidden property="noOfPages" styleId="noOfPages"/>
            <html:hidden property="totalPageSize" styleId="totalPageSize"/>
            <html:hidden property="noOfRecords" styleId="noOfRecords"/>
            <html:hidden property="orderBy" styleId="orderBy"/>
            <html:hidden property="sortBy" styleId="sortBy"/>
            <c:if test="${!empty displayMessage}">
            <span style="font-size:medium;color:red;">
                <c:out value="${displayMessage}"/>
            </span>
        </c:if>
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
            <td>
                <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
                    <tr class="tableHeadingNew">
                    <td colspan="6"> Search AP Invoice</td>
                    </tr>
                    <tr></tr>
                    <tr class="textlabelsBold">
                    <td>Customer</td>
                    <td>
                        <input name="cusName" id="cusName" value="${apInvoiceform.cusName}"
                               class="textlabelsBoldForTextBox" style="text-transform: uppercase"/>
                        <input name="custname_check" id="custname_check" value="${apInvoiceform.cusName}" type="hidden"/>
                        <div id="custname_choices" style="display: none" class="autocomplete"></div>
                    </td>
                    <td>Account Number</td>
                    <td>
                        <html:text property="accountNumber" styleId="accountNumber" value="${apInvoiceform.accountNumber}" 
                                   styleClass="textlabelsBoldForTextBoxDisabledLook" tabindex="-1" style="text-transform:uppercase;"/>
                    </td>
                    <td>Invoice Number</td>
                    <td><html:text property="invoiceNumber" styleId="invoiceNumber" value="${apInvoiceform.invoiceNumber}" 
                               styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;">
                            
                        </html:text>
                    </td>
                        </tr>
                        <tr>
                        <td colspan="6" align="center"><input type="button" value="Search" name="search" class="buttonStyleNew" onclick="SearchApInvoice()"/>
                            <input type="button" value="Clear" name="clear" class="buttonStyleNew" onclick="clearApInvoice()"/>
                        <c:if test="${canEdit}">
                            <input type="button" value="Add New"  name="addNew" class="buttonStyleNew" onclick="addNewApInvoice()"/>
                        </c:if>
                    </td>
                    </tr>
                </table >
            </td>
        </tr>
        <tr>
        <td>
            <br/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                <td>List of AP Invoices</td>
                </tr>
                <tr>
                <td><%@include file="apInvoiceResults.jsp"%></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</html:form>
</body>
<script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/SearchApInvoice.js"></script>
<%@include file="../includes/baseResourcesForJS.jsp" %>
<script type="text/javascript">
    jQuery.noConflict();
    jQuery("#apInvoice").tablesorter({widgets: ['zebra']});
</script>
</html>