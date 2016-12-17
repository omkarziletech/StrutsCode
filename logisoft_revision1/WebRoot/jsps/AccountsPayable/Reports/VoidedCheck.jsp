<%-- 
    Document   : VoidedCheck
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
                <table border="0" cellpadding="2" cellspacing="0" width="100%" class="tableBorderNew">
                    <tr class="textlabelsBold" valign="top">
                        <td>From Date</td>
                        <td>
                            <div class="float-left">
                                <html:text styleClass="textlabelsBoldForTextBox"  property="fromDate" styleId="txtcal3" onchange="return validateDate(this)"/>
                            </div>
                            <div class="calendar-img">
                                 <img src="${path}/img/CalendarIco.gif" alt="From Date" width="14px" id="cal3" onmousedown="insertDateFromCalendar(this.id,0);"/>
                            </div>
                        </td>
                        <td>To Date</td>
                        <td>
                            <div class="float-left">
                                <html:text styleClass="textlabelsBoldForTextBox"  property="toDate" styleId="txtcal4" onchange="return validateDate(this)"/>
                            </div>
                            <div class="calendar-img">
                                <img src="${path}/img/CalendarIco.gif" alt="To Date" width="14px" id="cal4" onmousedown="insertDateFromCalendar(this.id,0);"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6" align="center">
                            <input type="button" class="buttonStyleNew" value="Clear" onclick="clearValues('showVoidedCheck')">
                            <input type="button" class="buttonStyleNew" value="Print" onclick="printReport('printVoidedCheck')">
                            <input type="button" class="buttonStyleNew" value="Export To Excel" onclick="exportToExcel('exportVoidedCheckToExcel')" style="width:100px">
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
