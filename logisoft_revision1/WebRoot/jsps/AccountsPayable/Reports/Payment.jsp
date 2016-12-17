<%-- 
    Document   : CheckRegister
    Created on : Aug 17, 2010, 4:59:54 PM
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
                        <td>User</td>
                        <td>
                            <html:radio property="filterByUser" value="${commonConstants.YES}">Yes</html:radio>
                            <html:radio property="filterByUser" value="${commonConstants.NO}">No</html:radio>
                        </td>
                        <td>Payment Date</td>
                        <td>
                            <div class="float-left">
                                <html:text styleClass="textlabelsBoldForTextBox"  property="transactionDate" styleId="txtcal" onchange="return validateDate(this)"/>
                            </div>
                            <div class="calendar-img">
                                <img src="${path}/img/CalendarIco.gif" alt="cal" id="cal" height="14px" onmousedown="insertDateFromCalendar(this.id,0);"/>
                            </div>
                        </td>
                        <td>Payment Method </td>
                        <td>
                            <html:radio property="paymentMethod" value="${commonConstants.PAYMENT_METHOD_CHECK}">CHECK</html:radio>
                            <html:radio property="paymentMethod" value="${commonConstants.PAYMENT_METHOD_ACH}">ACH</html:radio>
                            <html:radio property="paymentMethod" value="${commonConstants.PAYMENT_METHOD_WIRE}">WIRE</html:radio>
                        </td>
                    </tr>
                    <tr class="textlabelsBold" valign="top">
                        <td> Division</td>
                        <td>
                            <html:radio property="divisionOrTerminal" value="${commonConstants.YES}">Yes</html:radio>
                            <html:radio property="divisionOrTerminal" value="${commonConstants.NO}">No</html:radio>
                        </td>
                        <td>Cost Code</td>
                        <td><html:text styleClass="textlabelsBoldForTextBox"  property="costCode"/></td>
                        <td>Voided Checks</td>
                        <td>
                            <html:radio property="voidedCheck" value="${commonConstants.YES}">Yes</html:radio>
                            <html:radio property="voidedCheck" value="${commonConstants.NO}">No</html:radio>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6" align="center">
                            <input type="button" class="buttonStyleNew" value="Clear" onclick="clearValues('showPayment')">
                            <input type="button" class="buttonStyleNew" value="Print" onclick="printReport('printPayment')">
                            <input type="button" class="buttonStyleNew" value="Export To Excel" onclick="exportToExcel('exportPaymentToExcel')" style="width:100px">
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
