<%-- 
    Document   : Aging
    Created on : Aug 17, 2010, 4:24:58 PM
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
                        <td>Vendor Name</td>
                        <td>
                            <html:text property="vendorName" styleId="vendorName" styleClass="textlabelsBoldForTextBox" style="text-transform:upperCase"/>
                            <input type="hidden" name="vendorNameCheck" id="vendorNameCheck" value="${apReportsForm.vendorName}"/>
                            <div id="vendorNameChoices" style="display: none" class="autocomplete"></div>
                        </td>
                        <td>Vendor Number</td>
                        <td><html:text property="vendorNumber" styleId="vendorNumber" styleClass="textlabelsBoldForTextBoxDisabledLook" 
                                   readonly="true" style="text-transform:uppercase;"/></td>
                        <td>All Customer</td>
                        <td>
                            <html:checkbox property="showAllCustomer" value="${commonConstants.YES}" onclick="grayOutFields(this)"></html:checkbox>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Transaction Type</td>
                        <td>
                            <html:radio property="transactionType" value="${commonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE}">AP</html:radio>
                            <html:radio property="transactionType" value="${commonConstants.TRANSACTION_TYPE_ACCRUALS}">AC</html:radio>
                            <html:radio property="transactionType" value="${commonConstants.BOTH}">Both</html:radio>
                            <html:radio property="transactionType" value="${commonConstants.TRANSACTION_TYPE_INACTIVE_ACCRUALS}">Inactive AC</html:radio>
                        </td>
                        <td>Show Master/Sub</td>
                        <td>
                            <html:radio property="showMaster" value="${commonConstants.YES}">Yes</html:radio>
                            <html:radio property="showMaster" value="${commonConstants.NO}">No</html:radio>
                        </td>
                        <td>Summary </td>
                        <td><html:radio property="showDetailOrSummary" value="${commonConstants.AP_SUMMARY_REPORT}"/></td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Cost Code</td>
                        <td><html:text property="costCode" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/></td>
                        <td>GL Account</td>
                        <td><html:text property="glAccount" styleClass="textlabelsBoldForTextBox"/></td>
                        <td>Detail </td>
                        <td><html:radio property="showDetailOrSummary" value="${commonConstants.AP_DETAIL_REPORT}"/></td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Cut Off Date</td>
                        <td>
                            <div class="float-left">
                                <html:text styleClass="textlabelsBoldForTextBox"  property="cutOffDate" styleId="txtcutDate" onchange="return validate(this)"/>
                            </div>
                            <div class="calendar-img">
                                <img src="${path}/img/CalendarIco.gif" width="14px" alt="Cut Off Date" id="cutDate" onmousedown="insertDateFromCalendar(this.id,0);"/>
                            </div>
                        </td>
                        <td>Division/Terminal</td>
                        <td>
                            <html:radio property="divisionOrTerminal" value="${commonConstants.DIVISION}">Division</html:radio>
                            <html:radio property="divisionOrTerminal" value="${commonConstants.TERMINAL}">Terminal</html:radio>
                            <html:radio property="divisionOrTerminal" value="${commonConstants.BOTH}">Both</html:radio>
                        </td>
                        <td>User</td>
                        <td>
                            <html:radio property="filterByUser" value="${commonConstants.YES}">Yes</html:radio>
                            <html:radio property="filterByUser" value="${commonConstants.NO}">No</html:radio>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6" align="center">
                            <input type="button" class="buttonStyleNew" value="Clear" onclick="clearValues('showAging')">
                            <input type="button" class="buttonStyleNew" value="Print" onclick="printReport('printAging')">
                            <input type="button" class="buttonStyleNew" value="Export To Excel" onclick="exportToExcel('exportAgingToExcel')" style="width:100px">
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
            initAutocomplete("vendorName","vendorNameChoices","vendorNumber","vendorNameCheck","${path}/servlet/AutoCompleterServlet?action=Vendor&textFieldId=vendorName&accountType=V","");
        </script>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/ApReports.js"></script>
        <c:if test="${not empty reportFileName}">
            <script type="text/javascript">
                window.parent.parent.showGreyBox('Aging Report','${path}/servlet/FileViewerServlet?fileName=${reportFileName}');
            </script>
        </c:if>
    </body>
</html>
