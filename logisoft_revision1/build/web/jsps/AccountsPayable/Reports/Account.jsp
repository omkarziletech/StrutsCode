<%-- 
    Document   : Account
    Created on : Aug 17, 2010, 4:57:19 PM
    Author     : Lakshmi Naryanan
--%>
<%@include file="../../includes/jspVariables.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <%@include file="../../includes/baseResources.jsp"%>
        <%@include file="../../includes/resources.jsp"%>
    </head>
    <body>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <html:form action="/apReports" name="apReportsForm" type="com.gp.cvst.logisoft.struts.form.ApReportsForm" scope="request">
            <html:hidden property="action"/>
            <html:hidden property="reportType"/>
            <div>
                <table border="0" cellpadding="0" cellspacing="0" width="100%" class="tableBorderNew">
                    <tr class="textlabelsBold">
                        <td colspan="3">
                            <html:radio property="accountFilterType" value="30days">Accounts not paid in over 30 days </html:radio>
                        </td>
                        <td colspan="3">
                            <html:radio property="accountFilterType" value="365dayPeriod">Accounts inactive for 365 day period</html:radio>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td colspan="3">
                            <html:radio property="accountFilterType" value="w9OnFile">Accounts do not have w-9 on file</html:radio>
                        </td>
                        <td colspan="3">
                            <html:radio property="accountFilterType" value="einOnFile">Accounts that do not have EIN on file</html:radio>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td colspan="3">
                            <html:radio property="accountFilterType" value="missingInformation">Accounts that have missing information</html:radio>
                        </td>
                        <td colspan="3">
                            <html:radio property="accountFilterType" value="duplicateVendor">Duplicate vendor</html:radio>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td colspan="3">
                            <html:radio property="accountFilterType" value="addedSuspended">Accounts that were added/suspended in a certain period</html:radio>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6" align="center">
                            <input type="button" class="buttonStyleNew" value="Clear" onclick="clearValues('showAccount')">
                            <input type="button" class="buttonStyleNew" value="Print" onclick="printReport('printAccount')">
                            <input type="button" class="buttonStyleNew" value="Export To Excel" onclick="exportToExcel('exportAccountToExcel')" style="width:100px">
                        </td>
                    </tr>
                </table>
            </div>
        </html:form>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/ApReports.js"></script>
        <c:if test="${not empty reportFileName}">
            <script type="text/javascript">
                window.parent.parent.showGreyBox('Aging Report','${path}/servlet/FileViewerServlet?fileName=${reportFileName}');
            </script>
        </c:if>
    </body>
</html>
