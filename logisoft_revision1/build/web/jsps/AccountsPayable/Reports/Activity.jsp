<%-- 
    Document   : Activity
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
                <table border="0" cellpadding="2" cellspacing="0" width="100%" class="tableBorderNew table-td-align-middle" >
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
                        <td>ECU Designation</td>
                        <td>
                            <html:select property="ecuDesignation"
                                         styleId="ecuDesignation" styleClass="dropdown_accounting" value="${apReportsForm.ecuDesignation}">
                               <html:option value="">SELECT</html:option>
                               <html:option value="LO">LOCAL(LO)</html:option>
                               <html:option value="IC">INTERCOMPANY(IC)</html:option>
                               <html:option value="AG">AGENTS(AG)</html:option>
                               <html:option value="AA">AFFILIATED AGENTS(AA)</html:option>
                           </html:select>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>User Name</td>
                        <td>
                            <html:text property="userName" styleId="userName" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
                            <html:hidden property="userId" styleId="userId"></html:hidden>
                            <input type="hidden" name="userNameValid" id="userNameValid" value="${apReportsForm.userName}"></input>
                            <div id="userNameDiv" class="newAutoComplete"></div>
                        </td>
                        <td>Date From</td>
                        <td>
                            <div class="float-left">
                                <html:text styleClass="textlabelsBoldForTextBox" property="fromDate" styleId="txtcal1" onchange="return validateDate(this)"/>
                            </div>
                            <div class="calendar-img">
                                <img src="${path}/img/CalendarIco.gif" alt="From Date" id="cal1" onmousedown="insertDateFromCalendar(this.id,0);"/>
                            </div>
                        </td>
                        <td>Date To</td>
                        <td>
                            <div class="float-left">
                                <html:text styleClass="textlabelsBoldForTextBox"   property="toDate" styleId="txtcal2" onchange="return validateDate(this)"/>
                            </div>
                            <div class="calendar-img">
                                <img src="${path}/img/CalendarIco.gif" alt="To Date" id="cal2" onmousedown="insertDateFromCalendar(this.id,0);"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6" align="center">
                            <input type="button" class="buttonStyleNew" value="Clear" onclick="clearValues('showActivity')">
                            <input type="button" class="buttonStyleNew" value="Print" onclick="printReport('printActivity')">
                            <input type="button" class="buttonStyleNew" value="Export To Excel" onclick="exportToExcel('exportActivityToExcel')" style="width:100px">
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
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/ApReports.js"></script>
        <c:if test="${not empty reportFileName}">
            <script type="text/javascript">
                window.parent.parent.showGreyBox('Aging Report','${path}/servlet/FileViewerServlet?fileName=${reportFileName}');
            </script>
        </c:if>
    </body>
</html>
