<%-- 
    Document   : CheckRegister
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
        <html:form action="/apReports" name="apReportsForm" type="com.gp.cvst.logisoft.struts.form.ApReportsForm" scope="request" method="post">
            <html:hidden property="action"/>
            <html:hidden property="reportType"/>
            <div>
                <table border="0" cellpadding="2" cellspacing="0" width="100%" class="tableBorderNew">
                    <tr class="textlabelsBold">
                        <td>Bank Account</td>
                        <td>
                            <html:text property="bankAccount" styleId="bankAccount" styleClass="textlabelsBoldForTextBox"/>
                            <input name="bankAccountCheck" id="bankAccountCheck"  type="hidden" value="${apReportsForm.bankAccount}"/>
                            <div id="bankAccountChoices" style="display: none" class="autocomplete"></div>
                        </td>
                        <td>GL Account</td>
                        <td><html:text property="glAccount" styleId="glAccount" styleClass="textlabelsBoldForTextBox"/></td>
                        <td>All Bank Account</td>
                        <td><html:radio property="allBankAccount" styleId="allBankAccount" value="${commonConstants.All_BANK_ACCOUNT}"/> </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Check Number From</td>
                        <td><html:text property="fromCheck" styleId="fromCheck" styleClass="textlabelsBoldForTextBox uppercase"/></td>
                        <td>Check Number To</td>
                        <td><html:text property="toCheck" styleId="toCheck" styleClass="textlabelsBoldForTextBox uppercase"/></td>
                        <td>Summary</td>
                        <td><html:radio property="showDetailOrSummary" value="${commonConstants.AP_SUMMARY_REPORT}"/></td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>From Date</td>
                        <td>
                            <div class="float-left">
                                <html:text styleClass="textlabelsBoldForTextBox"  property="fromDate" styleId="txtfromDate" onchange="return validateDate(this)"/>
                            </div>
                            <div class="calendar-img">
                                <img src="${path}/img/CalendarIco.gif" alt="From Date" id="fromDate" onmousedown="insertDateFromCalendar(this.id,0);"/>
                            </div>
                        </td>
                        <td>To Date</td>
                        <td>
                            <div class="float-left">
                                <html:text styleClass="textlabelsBoldForTextBox" property="toDate" styleId="txttoDate" onchange="return validateDate(this)"/>
                            </div>
                            <div class="calendar-img">
                                <img src="${path}/img/CalendarIco.gif" alt="To Date" id="toDate" onmousedown="insertDateFromCalendar(this.id,0);"/>
                            </div>
                        </td>
                        <td>Detail </td>
                        <td><html:radio property="showDetailOrSummary" value="${commonConstants.AP_DETAIL_REPORT}"/></td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Payment Method</td>
                        <td>
                            <html:radio property="paymentMethod" value="${commonConstants.PAYMENT_METHOD_CHECK}">CHECK</html:radio>
                            <html:radio property="paymentMethod" value="${commonConstants.PAYMENT_METHOD_ACH}">ACH</html:radio>
                            <html:radio property="paymentMethod" value="${commonConstants.PAYMENT_METHOD_WIRE}">WIRE</html:radio>
                            <html:radio property="paymentMethod" value="${commonConstants.ALL}">ALL</html:radio>
                        </td>
                        <td>Status</td>
                        <td colspan="3">
                            <html:radio property="checkStatus" value="${commonConstants.YES}">Cleared</html:radio>
                            <html:radio property="checkStatus" value="${commonConstants.NO}">Not Cleared</html:radio>
                            <html:radio property="checkStatus" value="${commonConstants.STATUS_VOID}">Voided</html:radio>
                            <html:radio property="checkStatus" value="${commonConstants.ALL}">All</html:radio>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6" align="center">
                            <input type="button" class="buttonStyleNew" value="Clear" onclick="clearValues('showCheckRegister')">
                            <input type="button" class="buttonStyleNew" value="Print" onclick="printReport('printCheckRegister')">
                            <input type="button" class="buttonStyleNew" value="Export To Excel" onclick="exportToExcel('exportCheckRegisterToExcel')" style="width:100px">
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
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript">
            initAutocomplete("bankAccount","bankAccountChoices","glAccount","bankAccountCheck","${path}/servlet/AutoCompleterServlet?action=Bank&textFieldId=bankAccount","");
        </script>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/ApReports.js"></script>
        <c:if test="${not empty reportFileName}">
            <script type="text/javascript">
                window.parent.parent.showGreyBox('Check Registers Report','${path}/servlet/FileViewerServlet?fileName=${reportFileName}');
            </script>
        </c:if>
    </body>
</html>
