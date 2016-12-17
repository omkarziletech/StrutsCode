<%-- 
    Document   : TimeLapseBetweenAccrualEntry
    Created on : Aug 17, 2010, 6:40:57 PM
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
                <table border="0" cellpadding="2" cellspacing="0" width="100%" class="tableBorderNew">
                    <tr class="textlabelsBold">
                        <td>Division</td>
                        <td>
                            <html:radio property="divisionOrTerminal" value="${commonConstants.YES}">Yes</html:radio>
                            <html:radio property="divisionOrTerminal" value="${commonConstants.NO}">No</html:radio>
                        </td>
                        <td>User</td>
                        <td>
                            <html:radio property="filterByUser" value="${commonConstants.YES}">Yes</html:radio>
                            <html:radio property="filterByUser" value="${commonConstants.NO}">No</html:radio>
                        </td>
                        <td>Time Lapse Amount</td>
                        <td><html:text property="timeLapseAmount" styleClass="textlabelsBoldForTextBox"/></td>
                    </tr>
                    <tr>
                        <td colspan="6" align="center">
                            <input type="button" class="buttonStyleNew" value="Clear" onclick="clearValues('showTimeLapseBetweenAccrualEntry')">
                            <input type="button" class="buttonStyleNew" value="Print" onclick="printReport('printTimeLapseBetweenAccrualEntry')">
                            <input type="button" class="buttonStyleNew" value="Export To Excel" onclick="exportToExcel('exportTimeLapseBetweenAccrualEntryToExcel')" style="width:100px">
                        </td>
                    </tr>
                </table>
            </div>
        </html:form>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/ApReports.js"></script>
        <c:if test="${not empty reportFileName}">
            <script type="text/javascript">
                window.parent.parent.showGreyBox('Aging Report','${path}/servlet/FileViewerServlet?fileName=${reportFileName}');
            </script>
        </c:if>
    </body>
</html>
