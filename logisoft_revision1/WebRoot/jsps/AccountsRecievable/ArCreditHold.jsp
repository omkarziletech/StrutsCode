<%--
    Document   : ArCreditHold
    Created on : Jul 19, 2010, 12:32:07 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <%@include file="../includes/jspVariables.jsp"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
            <title>AR Credit Hold</title>
        <%@include file="../includes/baseResources.jsp"%>
        <%@include file="../includes/resources.jsp"%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <c:set var="accessMode" value="1"/>
        <c:set var="canEdit" value="true" scope="request"/>
        <c:if test="${param.accessMode==0}">
            <c:set var="accessMode" value="0"/>
            <c:set var="canEdit" value="false" scope="request"/>
        </c:if>
        <style>

            div.new-autocomplete {
                position: absolute;
                margin: 0px;
                padding: 0px;
            }
            div.new-autocomplete ul {
                list-style-type: none;
                border: 1px solid darkgray;
                box-sizing: border-box;
                -moz-box-sizing: border-box;
                background-color: Menu;
                margin: 0px;
                padding: 0px;
                width:175px;
                height:150px;
                overflow-y:auto;
                overflow-x:hidden;
            }
            div.new-autocomplete ul li {
                list-style-type: none;
                display: block;
                margin: 0px;
                padding: 0px 5px 7px 0px;
                width: 100%;
                box-sizing: border-box;
                -moz-box-sizing: border-box;
                cursor: pointer;
                font-family: verdana,arial,helvetica,sans-serif;
                font-size: 11px;
                line-height: 13px;
                text-align:left;
                background-color: Menu;
                color: MenuText;
            }
            div.new-autocomplete ul li.selected {
                background-color: Highlight;
                color: HighlightText;
            }
        </style>
    </head>
    <body>
        <%@include file="../preloader.jsp"%>
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <html:form action="/arCreditHold?accessMode=${accessMode}" name="arCreditHoldForm" styleId="arCreditHoldForm"
                   type="com.gp.cvst.logisoft.struts.form.ArCreditHoldForm" scope="request" method="post" onsubmit="showPreloading()">
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="removeFromHoldIds" styleId="removeFromHoldIds"/>
            <html:hidden property="putOnHoldIds" styleId="putOnHoldIds"/>
            <html:hidden property="pageNo" styleId="pageNo"/>
            <html:hidden property="noOfPages" styleId="noOfPages"/>
            <html:hidden property="currentPageSize" styleId="currentPageSize"/>
            <html:hidden property="totalPageSize" styleId="totalPageSize"/>
            <html:hidden property="sortBy" styleId="sortBy"/>
            <html:hidden property="orderBy" styleId="orderBy"/>
            <input type="hidden" name="notesConstantArInvoice" id="notesConstantArInvoice" value="${notesConstants.AR_INVOICE}"/>
            <c:if test="${not empty message}">
                <div class="message">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            <table cellpadding="0" cellspacing="3" width="100%" class="tableBorderNew" style="padding: 10px 0 0 0">
                <tr class="tableHeadingNew">
                    <td colspan="6">AR Credit Hold</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Customer Name</td>
                    <td>
                        <html:text property="customerName" styleId="customerName" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase"/>
                        <html:text property="customerNumber" styleId="customerNumber" readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                        <input type="hidden" name="customerNameCheck" id="customerNameCheck" value="${arCreditHoldForm.customerName}"/>
                        <div id="customerNameChoices" class="autocomplete" style="display: none"></div>
                        <c:if test="${canEdit}">
                            <input type="button" class="buttonStyleNew" value="Trading Partner" style="width: 100px" onclick="gotoTradingPartner()"/>
                        </c:if>
                    </td>
                    <td>BL Number</td>
                    <td>
                        <html:text property="billOfLadding" styleId="billOfLadding" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase"/>
                    </td>
                    <td>Sales Person</td>
                    <td>
                        <html:text property="salesPerson" styleId="salesPerson" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Billing Terminal</td>
                    <td>
                        <html:text property="billingTerminal" styleId="billingTerminal" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase"/>
                        <input type="hidden" name="billingTerminalCheck" id="billingTerminalCheck" value="${arCreditHoldForm.billingTerminal}">
                        <div id="billingTerminalChoices" class="new-autocomplete" style="display: none"></div>
                    </td>
                    <td>Destination</td>
                    <td>
                        <html:text property="destination" styleId="destination" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase"/>
                        <input type="hidden" name="destinationCheck" id="destinationCheck" value="${arCreditHoldForm.destination}">
                        <div id="destinationChoices" class="new-autocomplete" style="display: none;"/>
                    </td>
                    <td>Collector</td>
                    <td>
                        <html:text property="collectorName" styleId="collectorName" styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
                        <input type="hidden" name="collectorNameCheck" id="collectorNameCheck" value="${arCreditHoldForm.collectorName}"/>
                        <div id="collectorNameChoices" class="new-autocomplete" style="display: none;"></div>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td colspan="6" align="center">
                        <input type="button" value="Search" class="buttonStyleNew" onclick="search()"/>
                        <input type="button" value="Clear" class="buttonStyleNew" onclick="refresh()"/>
                        <c:if test="${not empty arCreditHoldForm.transactions && canEdit && roleDuty.creditHolder}">
                            <input type="button" value="Save" class="buttonStyleNew" onclick="save()"/>
                        </c:if>
                    </td>
                </tr>
            </table>
            <table cellpadding="0" cellspacing="3" width="100%" class="tableBorderNew" style="padding: 10px 0 0 0">
                <tr class="tableHeadingNew">
                    <td>Credit Hold Transactions</td>
                </tr>
                <tr>
                    <td>
                        <div id="results">
                            <c:import url="/jsps/AccountsRecievable/arCreditHoldResults.jsp"/>
                        </div>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
    <script type="text/javascript" src="${path}/js/Accounting/AccountsReceivable/arCreditHold.js"></script>
</html>