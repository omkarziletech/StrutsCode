<%-- 
    Document   : statement
    Created on : May 17, 2012, 1:36:15 PM
    Author     : logiware
--%>

<html>
    <head>
        <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
        <title>Statement Report</title>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <%@include file="../../includes/jspVariables.jsp"%>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/default/style.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/cal/skins/aqua/theme.css" title="Aqua"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" media="all"/>
    </head>
    <body>
        <html:form action="/apReports" name="apReportsForm" styleId="apReportsForm" type="com.gp.cvst.logisoft.struts.form.ApReportsForm" scope="request">
            <html:hidden property="action" styleId="action"/>
            <div>
                <table border="0" cellpadding="0" cellspacing="3" width="100%" class="tableBorderNew">
                    <tr class="tableHeadingNew">
                        <td colspan="5"> Statement </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Vendor Name</td>
                        <td>
                            <html:text property="vendorName" styleId="vendorName" styleClass="textlabelsBoldForTextBox" style="text-transform:upperCase"/>
                            <input type="hidden" name="vendorNameCheck" id="vendorNameCheck" value="${apReportsForm.vendorName}"/>
                            <div id="vendorNameChoices" style="display: none" class="autocomplete"></div>
                        </td>
                        <td>Vendor Number</td>
                        <td>
                            <html:text property="vendorNumber" styleId="vendorNumber" styleClass="textlabelsBoldForTextBoxDisabledLook" 
                                       readonly="true" style="text-transform:uppercase;"/>
                        </td>
                        <td>
                            <html:checkbox property="allCustomers" styleId="allCustomers" value="true"/>All Customer
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>AP Specialist</td>
                        <td>
                            <html:select property="apSpecialist" styleId="apSpecialist" styleClass="dropdown_accounting">
				<html:optionsCollection name="UserList" styleClass="unfixedtextfiledstyle"/>
                            </html:select>
                        </td>
                        <td>Agents</td>
                        <td>
                            <html:select property="agents" styleId="agents" styleClass="dropdown_accounting">
                                <html:option value="onlyAgents">Only Agents</html:option>
                                <html:option value="agentsIncluded">Agents Included</html:option>
                                <html:option value="agentsNotIncluded">Agents Not Included</html:option>
                            </html:select>
                        </td>
                        <td>
                            <html:checkbox property="ecuLineReport" styleId="ecuLineReport" value="true"/>Ecu Line Format
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
			<td>Include</td>
                        <td>
                            <html:select property="include" styleId="include" styleClass="dropdown_accounting">
                                <html:option value="Negative AP + AC only">Negative AP + AC only</html:option>
                                <html:option value="Negative AP only">Negative AP only</html:option>
                                <html:option value="Negative AC only">Negative AC only</html:option>
                                <html:option value="All AP + Open AC only">All AP + Open AC only</html:option>
                                <html:option value="All AP only">All AP only</html:option>
                                <html:option value="Open AC only">Open AC only</html:option>
                            </html:select>
                        </td>
                        <td>
                            <html:checkbox property="includeAR" styleId="includeAR" value="true"/>Include AR
                        </td>
                    </tr>
                    <tr>
                        <td colspan="5" align="center">
                            <input type="button" value="Print" class="buttonStyleNew" onclick="printReport('printStatement')"/>
                            <input type="button" value="Export To Excel" class="buttonStyleNew" onclick="exportToExcel('exportStatementToExcel')"/>
                            <input type="button" value="Clear" class="buttonStyleNew" onclick="clearValues('showStatement')"/>
                        </td>
                    </tr>
                </table>
            </div>
        </html:form>
	<script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/ApReports.js"></script>
	<c:if test="${not empty reportFileName}">
            <script type="text/javascript">
                window.parent.parent.showGreyBox('AP Statement','${path}/servlet/FileViewerServlet?fileName=${reportFileName}');
            </script>
        </c:if>
    </body>
</html>
