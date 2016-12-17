<%-- 
    Document   : tradingPartnerNotesReport
    Created on : Mar 8, 2012, 12:19:01 PM
    Author     : logiware
--%>
<html>
    <head>
        <title>AR Account Notes Reports</title>
        <%@include file="../includes/jspVariables.jsp" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp"%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
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
        <html:form action="/arAccountNotesReport" name="arAccountNotesReportForm" styleId="arAccountNotesReportForm"
                   type="com.gp.cvst.logisoft.struts.form.ArAccountNotesReportForm" scope="request">
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="reportType" styleId="reportType"/>
            <div>
                <table border="0" cellpadding="2" cellspacing="0" width="100%" class="tableBorderNew">
                    <tr class="textlabelsBold">
                    <td>Customer Name</td>
                    <td>
                        <html:text property="customerName" styleId="customerName" styleClass="textlabelsBoldForTextBox" style="text-transform:upperCase"/>
                        <input type="hidden" name="customerNameCheck" id="customerNameCheck" value="${arAccountNotesReportForm.customerName}"/>
                        <div id="customerNameChoices" style="display: none" class="autocomplete"></div>
                    </td>
                    <td>Customer Number</td>
                    <td>
                        <html:text property="customerNumber" styleId="customerNumber" styleClass="textlabelsBoldForTextBoxDisabledLook" 
                                   readonly="true" style="text-transform:uppercase;"/>
                    </td>
                    </tr>
                    <tr class="textlabelsBold">
                    <td>Account Assigned To</td>
                    <td>
                        <html:text property="accountAssignedTo" styleId="accountAssignedTo" styleClass="textlabelsBoldForTextBox" 
                                   style="text-transform:uppercase;"/>
                        <input type="hidden" name="accountAssignedToCheck" id="accountAssignedToCheck" value="${arAccountNotesReportForm.accountAssignedTo}"/>
                        <div id="accountAssignedToDiv" class="newAutoComplete"></div>
                    </td>
                    <td>Notes Entered By</td>
                    <td>
                        <html:text property="notesEnteredBy" styleId="notesEnteredBy" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
                        <input type="hidden" name="notesEnteredByCheck" id="notesEnteredByCheck" value="${arAccountNotesReportForm.notesEnteredBy}"/>
                        <div id="notesEnteredByDiv" class="newAutoComplete"></div>
                    </td>
                    </tr>
                    <tr class="textlabelsBold">
                    <td>Date From</td>
                    <td>
                        <div class="float-left">
                            <html:text property="fromDate" styleId="txtcal1" styleClass="textlabelsBoldForTextBox" onchange="return validateDate(this,'from')"/>
                        </div>
                        <div class="calendar-img">
                            <img src="${path}/img/CalendarIco.gif" alt="From Date" id="cal1" onmousedown="insertDateFromCalendar(this.id,0);"/>
                        </div>
                    </td>
                    <td>Date To</td>
                    <td>
                        <div class="float-left">
                            <html:text property="toDate" styleId="txtcal2" styleClass="textlabelsBoldForTextBox" onchange="return validateDate(this,'to')"/>
                        </div>
                        <div class="calendar-img">
                            <img src="${path}/img/CalendarIco.gif" alt="To Date" id="cal2" onmousedown="insertDateFromCalendar(this.id,0);"/>
                        </div>
                    </td>
                    </tr>
                    <tr>
                    <td colspan="6" align="center">
                        <input type="button" class="buttonStyleNew" value="Clear" id="clear"/>
                        <input type="button" class="buttonStyleNew" value="Print" onclick="gotoAction('print')" />
                        <input type="button" class="buttonStyleNew" value="Export To Excel" onclick="gotoAction('exportToExcel')"/>
                    </td>
                    </tr>
                </table>
            </div>
        </html:form>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsReceivable/arAccountNotesReport.js"></script>
        <c:if test="${not empty reportFileName}">
            <script type="text/javascript">
                window.parent.parent.showGreyBox('Ar Account Notes Report','${path}/servlet/FileViewerServlet?fileName=${reportFileName}');
            </script>
        </c:if>
    </body>
</html>
