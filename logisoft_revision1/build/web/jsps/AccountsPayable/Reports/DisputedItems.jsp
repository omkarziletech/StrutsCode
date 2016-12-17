<%-- 
    Document   : DisputedItems
    Created on : Aug 17, 2010, 6:33:09 PM
    Author     : Lakshmi Naryanan
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <%@include file="../../includes/jspVariables.jsp"%>
        <%@include file="../../includes/baseResources.jsp"%>
        <%@include file="../../includes/resources.jsp"%>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
    </head>
    <body>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <html:form action="/apReports" name="apReportsForm" styleId="apReportsForm" type="com.gp.cvst.logisoft.struts.form.ApReportsForm" scope="request">
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="currentPageSize" styleId="currentPageSize"/>
            <html:hidden property="pageNo" styleId="pageNo"/>
            <html:hidden property="noOfPages" styleId="noOfPages"/>
            <html:hidden property="totalPageSize" styleId="totalPageSize"/>
            <html:hidden property="noOfRecords" styleId="noOfRecords"/>
            <html:hidden property="orderBy" styleId="orderBy"/>
            <html:hidden property="sortBy" styleId="sortBy"/>
            <html:hidden property="reportType"/>
            <div>
                <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableBorderNew">
                    <tr class="textlabelsBold">
                    <td>User Name</td>
                    <td>
                        <html:text property="userName" styleId="userName" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
                        <html:hidden property="userId" styleId="userId"></html:hidden>
                        <input type="hidden" name="userNameValid" id="userNameValid" value="${apReportsForm.userName}"></input>
                        <div id="userNameDiv" class="newAutoComplete"></div>
                    </td>
                    <td>From Date</td>
                    <td>
                        <div class="float-left">
                            <html:text styleClass="textlabelsBoldForTextBox" property="fromDate" styleId="txtcal1" onchange="return validateDate(this,'from')" />
                        </div>
                        <div class="calendar-img">
                            <img src="${path}/img/CalendarIco.gif" alt="From Date" id="cal1" onmousedown="insertDateFromCalendar(this.id,0);"/>
                        </div>
                    </td>
                    <td>To Date</td>
                    <td>
                        <div class="float-left">
                            <html:text styleClass="textlabelsBoldForTextBox"   property="toDate" styleId="txtcal2" onchange="return validateDate(this,'to')"/>
                        </div>
                        <div class="calendar-img">
                            <img src="${path}/img/CalendarIco.gif" alt="To Date" id="cal2" onmousedown="insertDateFromCalendar(this.id,0);"/>
                        </div>
                    </td>
                    <td>Invoice Number</td>
                    <td><html:text property="invoiceNumber" styleClass="textlabelsBoldForTextBox" styleId="invoiceNumber" 
                               style="text-transform:uppercase;"/> </td>
                    </tr>
                    <tr class="textlabelsBold">
                    <td colspan="8" align="center">
                        <input type="button" class="buttonStyleNew" value="Clear" onclick="clearValues('showDisputedItems')">
                        <input type="button" class="buttonStyleNew" value="Print" onclick="printReport('printDisputedItems')">
                        <input type="button" class="buttonStyleNew" value="Export To Excel"  onclick="exportToExcel('exportDisputedItemsToExcel')" style="width:100px">
                        <input type="button" class="buttonStyleNew" value="Email Log" onclick="gotoAction('getForDisputedEmailLog')">
                        <input type="button" class="buttonStyleNew" value="Export Email Log" onclick="gotoAction('exportEmailLogToExcel')">
                        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp; Resolved Date
                        <input type="radio" name="resolveOptions" id="resolveOptions" value="All" checked/>All
                        <input type="radio" name="resolveOptions" id="resolveOptionsY" value="Y" checked/>Yes
	                <input type="radio" name="resolveOptions" id="resolveOptionsN" value="N" />No
                    </td>
                    </tr>
                    <tr class="tableHeadingNew">
                        <td colspan="8">List of Disputed Email Logs</td>
                    </tr>
                    <tr>
                    <td colspan="8"><%@include file="disputedEmailLog.jsp"%></td>
                    </tr>
                </table>
            </div>
        </html:form>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/ApReports.js"></script>
        <c:if test="${not empty reportFileName}">
            <script type="text/javascript">
                window.parent.parent.showGreyBox('Disputed Items','${path}/servlet/FileViewerServlet?fileName=${reportFileName}');
            </script>
        </c:if>
    </body>
</html>
