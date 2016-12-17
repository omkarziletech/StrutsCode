<%-- 
    Document   : arApplyPayments
    Created on : Jun 6, 2011, 8:49:11 PM
    Author     : lakshh
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <%@include file="../includes/jspVariables.jsp"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}"/>
        <title>AR Batch</title>
        <%@include file="../includes/baseResources.jsp"%>
        <%@include file="../includes/resources.jsp"%>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.number.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.fileupload.js"></script>
        <script type="text/javascript" src="${path}/js/fileUpload/jquery.fileinput.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>

        <script type="text/javascript">
            var path = "${path}";
        </script>
        <link href="${path}/css/fileUpload/enhanced.css" type="text/css" rel="stylesheet"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <style type="text/css">
            @media print{
                #nav, #sidebar, #content, #header, #buttons{
                    display:none;
                }
                .popup{
                    height: 100%;
                }
            }
        </style>
    </head>
    <body>
        <div id="content">
            <%@include file="../preloader.jsp"%>
            <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
            <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
            <un:useConstants className="com.gp.cvst.logisoft.AccountingConstants" var="accountingConstants"/>
            <html:form action="/arBatch" name="arBatchForm"
                       styleId="arBatchForm" type="com.logiware.form.ArBatchForm" scope="request" method="post" onsubmit="showPreloading()">
                <html:hidden property="action" styleId="action"/>
                <html:hidden property="batchId" styleId="batchId"/>
                <html:hidden property="selectedBatchId" styleId="selectedBatchId"/>
                <html:hidden property="batchType" styleId="batchType"/>
                <html:hidden property="paymentCheckId" styleId="paymentCheckId"/>
                <html:hidden property="noOfInvoices" styleId="noOfInvoices"/>
                <html:hidden property="pageNo" styleId="pageNo"/>
                <html:hidden property="noOfPages" styleId="noOfPages"/>
                <html:hidden property="totalPageSize" styleId="totalPageSize"/>
                <html:hidden property="sortBy" styleId="sortBy"/>
                <html:hidden property="orderBy" styleId="orderBy"/>
                <html:hidden property="filterOpen" styleId="filterOpen"/>
                <html:hidden property="excludedPaymentIds" styleId="excludedPaymentIds"/>
                <html:hidden property="changes" styleId="changes"/>
                <html:hidden property="user" styleId="user"/>
                <html:hidden property="selectedSubType" styleId="selectedSubType"/>
                <html:checkbox property="searchByUser" styleId="searchByUser" style="display:none"/>
                <input type="hidden" name="notesConstantArInvoice" id="notesConstantArInvoice" value="${notesConstants.AR_INVOICE}"/>
                <c:if test="${not empty message}">
                    <div class="message">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="error">${error}</div>
                </c:if>
                <div class="message autosavenotification" style="float: right"></div>
                <table width="100%" border="0" cellpadding="5" cellspacing="0" class="tableBorderNew">
                    <tr class="textlabelsBold">
                        <td>
                            <div style="float:left">
                                <c:set var="readonly" value="false"/>
                                <c:set var="styleClass" value="textbox"/>
                                <c:if test="${not empty arBatchForm.paymentCheckId}">
                                    <c:set var="readonly" value="true"/>
                                    <c:set var="styleClass" value="textbox readonly"/>
                                </c:if>
                                Select Customer
                                <html:text property="customerName" styleId="customerName" readonly="${readonly}" styleClass="${styleClass}" style="width: 150px;"/>
                                <html:text property="customerNumber" styleId="customerNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                                <input type="hidden" id="customerNameCheck" value="${arBatchForm.customerName}"/>
                                <html:checkbox title="Show Disabled" property="disabled1" styleId="disabled1" value="true" styleClass="check-box"/>
                                <html:checkbox title="Show All Accounts" property="showAllAccounts1" styleId="showAllAccounts1" value="true" styleClass="check-box"/>
                                <input type="button" class="buttonStyleNew" value="Search"  id="search"/>
                                <input type="button" class="buttonStyleNew" value="Clear" id="refresh"/>
                                <input type="button" class="buttonStyleNew" value="Trading Partner" id="gotoTradingPartner"/>
                                <input type="button" value="Go Back" class="buttonStyleNew" id="goBack"/>
                                <input type="button" value="Scan/Attach"
                                       class="buttonStyleNew" style="width: 70px" onclick="scanOrAttach('${arBatchForm.batchId}')"/>
                                <html:checkbox property="autosave" styleId="autosave" value="true" onclick="activateDeactivateAutosave()"/>
                                Auto save
                            </div>
                            <div style="float:right;padding:4px 0 0 0">
                                <div style="float:left;padding:0 20px 0 20px">
                                    <c:choose>
                                        <c:when test="${arBatchForm.noOfInvoices > 1}">
                                            ${arBatchForm.noOfInvoices} invoices included.
                                        </c:when>
                                        <c:otherwise>
                                            ${arBatchForm.noOfInvoices} invoice included.
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div style="float:left;padding:0 20px 0 20px">
                                    Records Limit per page
                                    <html:text property="currentPageSize" styleId="currentPageSize"
                                               styleClass="textlabelsBoldForTextBox" style="width:30px" maxlength="3"/>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>
                            <div class="float-right">
                                <div class="float-left" style="padding: 4px 2px 0 0"><label>Choose Template</label></div>
                                <div class="float-left templateDiv"><input type="file" name="file" id="file"/></div>
                                <div class="float-left" style="padding: 2px 0 0 2px"><input type="button" value="Import" class="buttonStyleNew" id="import"/></div>
                            </div>
                        </td>
                    </tr>
                </table>
                <table width="100%"  border="0" cellpadding="5" cellspacing="0" class="tableBorderNew" style="margin: 10px 0 0 0">
                    <tr class="tableHeadingNew">
                        <td colspan="6">Apply Payments for Batch # - ${arBatchForm.batchId}</td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Check Number</td>
                        <td>
                            <c:choose>
                                <c:when test="${arBatchForm.batchType==accountingConstants.AR_NET_SETT_BATCH}">
                                    <html:text property="checkNumber" styleId="checkNumber" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/>
                                </c:when>
                                <c:when test="${fn:startsWith(arBatchForm.checkNumber,'NoCheck')}">
                                    <html:text property="checkNumber" styleId="checkNumber"
                                               styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" maxlength="20"/>
                                    <input type="checkbox" name="noCheck" id="noCheck" value="true" title="No Check" checked/>
                                </c:when>
                                <c:otherwise>
                                    <html:text property="checkNumber" styleId="checkNumber" styleClass="textlabelsBoldForTextBox uppercase" maxlength="20"/>
                                    <html:checkbox property="noCheck" styleId="noCheck" value="true" title="No Check" tabindex="-1"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>Batch Total Amount</td>
                        <td>
                            <html:text property="batchAmount" styleId="batchAmount"
                                       styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
                        </td>
                        <td>Batch Remaining Amount</td>
                        <td>
                            <html:text property="batchBalance" styleId="batchBalance"
                                       styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Check Amount</td>
                        <td>
                            <c:choose>
                                <c:when test="${arBatchForm.batchType==accountingConstants.AR_NET_SETT_BATCH
                                                || fn:startsWith(arBatchForm.checkNumber,'NoCheck')}">
                                    <html:text property="checkTotal" styleId="checkTotal" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/>
                                </c:when>
                                <c:otherwise>
                                    <html:text property="checkTotal" styleId="checkTotal" styleClass="textlabelsBoldForTextBox" maxlength="11"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>Check Applied Amount</td>
                        <td>
                            <html:text property="checkApplied" styleId="checkApplied"
                                       styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
                        </td>
                        <td>Check Remaining Amount</td>
                        <td>
                            <html:text property="checkBalance" styleId="checkBalance"
                                       styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" tabindex="-1"/>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
<!--                   <td><html:checkbox property="onAccountApplied" styleId="onAccountApplied" value="true"/>Apply On Account</td>
                    <td>
                        <c:set var="display" value="none"/>
                        <c:if test="${arBatchForm.onAccountApplied}">
                            <c:set var="display" value="block"/>
                        </c:if>
                        <div style="display:${display}" class="onAccountDiv">
                        <html:text property="appliedOnAccount.paidAmount" styleId="onAccountAmount" styleClass="textlabelsBoldForTextBox" maxlength="11"/>
                    </div>
                     </td>-->
                        <td colspan="3"><html:checkbox property="prepaymentApplied" styleId="prepaymentApplied" value="true"/>Apply Prepayment</td>
                        <td colspan="3"><html:checkbox property="chargeCodeApplied" styleId="chargeCodeApplied" value="true"/>Apply To Charge Code</td>
                    </tr>
                    <tr class="textlabelsBold">
                        <c:set var="display" value="none"/>
                        <c:if test="${arBatchForm.prepaymentApplied}">
                            <c:set var="display" value="block"/>
                        </c:if>
                        <td colspan="3" style="vertical-align: text-top">
                            <div style="display:${display}" id="prepayments">
                                <table width="100%"  border="0" cellpadding="0" cellspacing="2" class="tableBorderNew" id="ppTable">
                                    <tr class="tableHeadingNew">
                                        <td>Dock Receipt</td>
                                        <td>Amount</td>
                                        <td>Notes</td>
                                        <td style="text-align: center"><img src="${path}/img/icons/add2.gif" border="0" alt="" class="addPP"/></td>
                                    </tr>
                                    <tr class="textlabelsBold defaultPP" style="display:none">
                                        <td>
                                            <input type="text" class="docReceipt textlabelsBoldForTextBox"/>
                                            <input type="hidden" class="docReceiptCheck"/>
                                            <select name="select" id="selectType" class="dropdown ignore-case" >
                                                <option value="select">SELECT</option>
                                                <option value="FCL">FCL</option>
                                                <option value="LCLI">LCLI</option>
                                                <option value="LCLE">LCLE</option>
                                            </select>
                                        </td>
                                        <td><input type="text" class="prepaymentAmount textlabelsBoldForTextBox" maxlength="11"/></td>
                                        <td><input type="text" class="prepaymentNote textlabelsBoldForTextBox" maxlength="200" 
                                                   style="text-transform:uppercase;"/></td>
                                        <td align="center">
                                            <img src="${path}/img/icons/remove.gif"  alt="" border="0" class="removePP"/>
                                        </td>
                                    </tr>
                                    <c:forEach var="prepayment" items="${arBatchForm.appliedPrepayments}" varStatus="varStatus">
                                        <tr class="textlabelsBold">
                                            <td>                                         
                                                <input type="text" name="docReceipt${varStatus.count}" id="docReceipt${varStatus.count}"
                                                       class="docReceipt textlabelsBoldForTextBox" value="${prepayment.docReceipt}"/>
                                                <input type="hidden" value="${prepayment.docReceipt}"
                                                       id="docReceipt${varStatus.count}Check" class="docReceiptCheck"/>
                                                <select name="select" id="selectType${varStatus.count}" class="dropdown ignore-case" onchange="emptyDocReceipt(${varStatus.count})">
                                                    <option value="select">SELECT</option>
                                                    <option value="FCL">FCL</option>
                                                    <option value="LCLI">LCLI</option>
                                                    <option value="LCLE">LCLE</option>
                                                </select></td>
                                            <td>
                                                <input type="text" maxlength="11"
                                                       class="prepaymentAmount textlabelsBoldForTextBox" value="${prepayment.paidAmount}"/>
                                            </td>
                                            <td><input type="text" maxlength="200"
                                                       class="prepaymentNote textlabelsBoldForTextBox" value="${prepayment.notes}"/></td>
                                            <td align="center">
                                                <img src="${path}/img/icons/remove.gif"  alt="" border="0" class="removePP"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </td>
                        <c:set var="display" value="none"/>
                        <c:if test="${arBatchForm.chargeCodeApplied}">
                            <c:set var="display" value="block"/>
                        </c:if>
                        <td colspan="3" style="vertical-align: text-top">
                            <div style="display:${display}" id="glAccounts">
                                <table width="100%"  border="0" cellpadding="0" cellspacing="2" class="tableBorderNew" id="glTable">
                                    <tr class="tableHeadingNew">
                                        <td>GL account</td>
                                        <td>Amount</td>
                                        <td>Notes</td>
                                        <td align="center"><img src="${path}/img/icons/add2.gif" border="0" alt="" class="addGL"/></td>
                                    </tr>
                                    <tr class="textlabelsBold defaultGL" style="display:none">
                                        <td>
                                            <input type="text" class="appliedGlAccount textlabelsBoldForTextBox"/>
                                            <input type="hidden" class="appliedGlAccountCheck"/>
                                        </td>
                                        <td><input type="text" class="glAccountAmount textlabelsBoldForTextBox" maxlength="11"/></td>
                                        <td><input type="text" class="glAccountNote textlabelsBoldForTextBox" maxlength="200" 
                                                   style="text-transform: uppercase;"/></td>
                                        <td align="center">
                                            <img src="${path}/img/icons/remove.gif"  alt="" border="0" class="removeGL"/>
                                        </td>
                                    </tr>
                                    <c:forEach var="glAccount" items="${arBatchForm.appliedGLAccounts}" varStatus="varStatus">
                                        <tr class="textlabelsBold">
                                            <td>
                                                <input type="text" name="appliedGlAccount${varStatus.count}" id="appliedGlAccount${varStatus.count}"
                                                       class="appliedGlAccount textlabelsBoldForTextBox" value="${glAccount.glAccount}"/>
                                                <input type="hidden" value="${glAccount.glAccount}"
                                                       id="appliedGlAccount${varStatus.count}Check" class="appliedGlAccountCheck"/>
                                            </td>
                                            <td>
                                                <input type="text" maxlength="11" class="glAccountAmount textlabelsBoldForTextBox" value="${glAccount.paidAmount}"/>
                                            </td>
                                            <td>
                                                <input type="text" class="glAccountNote textlabelsBoldForTextBox" value="${glAccount.notes}" maxlength="200"/>
                                            </td>
                                            <td align="center">
                                                <img src="${path}/img/icons/remove.gif"  alt="" border="0" class="removeGL"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
                <table width="100%"  border="0" cellpadding="1" cellspacing="0" class="tableBorderNew" style="margin: 10px 0 0 0">
                    <tr class="tableHeadingNew" onclick="toggleFilter()" style="cursor: pointer">
                        <td>Search Payments for Batch # - ${arBatchForm.batchId}</td>
                        <td align="right">
                            <a>
                                <img alt="" src="${path}/img/icons/up.gif" border="0" style="margin: 0 0 0 0;float: right;"/>
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <c:set var="display" value="none"/>
                            <c:if test="${arBatchForm.filterOpen}">
                                <c:set var="display" value="block"/>
                            </c:if>
                            <div id="filter_options" style="display: ${display}">
                                <table width="100%"  border="0" cellpadding="0" cellspacing="2">
                                    <tr class="textlabelsBold">
                                        <td>Search By</td>
                                        <td>
                                            <html:select property="searchBy" styleId="searchBy" styleClass="dropdown_accounting">
                                                <html:optionsCollection property="searchByTypes"/>
                                            </html:select>
                                        </td>
                                        <td>Search Value</td>
                                        <td>
                                            <html:text property="searchValue" styleId="searchValue" styleClass="textlabelsBoldForTextBox" 
                                                       style="text-transform:uppercase;"/>
                                            <c:if test="${not empty arBatchForm.searchValue 
                                                          && fn:containsIgnoreCase(arBatchForm.searchBy,commonConstants.SEARCH_BY_INVOICE_BL_DR)}">
                                                  <input type="text" id="subTotal" class="textlabelsBoldForTextBoxDisabledLook" value="0.00" readonly="true"/>
                                            </c:if> 
                                        </td>
                                        <td>Other Customer</td>
                                        <td>
                                            <html:text property="otherCustomerName" styleId="otherCustomerName" styleClass="textlabelsBoldForTextBox" 
                                                       style="text-transform:uppercase;"/>
                                            <html:text property="otherCustomerNumber" styleId="otherCustomerNumber"
                                                       styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true" style="text-transform:uppercase;"/>
                                            <input id="otherCustomerNameCheck"  type="hidden" value="${arBatchForm.otherCustomerName}"/>
                                            <html:checkbox title="Show Disabled" property="disabled2" styleId="disabled2" value="true" styleClass="check-box"/>
                                            <html:checkbox title="Show All Accounts" property="showAllAccounts2" styleId="showAllAccounts2" value="true" styleClass="check-box"/>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>Show Payables</td>
                                        <td>
                                            <html:radio property="showPayables" styleId="showPayables" value="true"/>Yes
                                            <html:radio property="showPayables" styleId="showPayables" value="false"/>No
                                        </td>
                                        <td>Show All Members</td>
                                        <td>
                                            <html:radio property="showAssociatedCompanies" styleId="showAssociatedCompanies" value="true"/>Yes
                                            <html:radio property="showAssociatedCompanies" styleId="showAssociatedCompanies" value="false"/>No
                                        </td>
                                        <td>Show AR's with Zero Balance</td>
                                        <td>
                                            <html:radio property="showZeroBalanceReceivables" styleId="showZeroBalanceReceivables" value="true"/>Yes
                                            <html:radio property="showZeroBalanceReceivables" styleId="showZeroBalanceReceivables" value="false"/>No
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td>Show Accruals</td>
                                        <td>
                                            <html:radio property="showAccruals" styleId="showAccruals" value="true"/>Yes
                                            <html:radio property="showAccruals" styleId="showAccruals" value="false"/>No
                                        </td>
                                        <td>Show Inactive Accruals</td>
                                        <td>
                                            <html:radio property="showInactiveAccruals" styleId="showInactiveAccruals" value="true"/>Yes
                                            <html:radio property="showInactiveAccruals" styleId="showInactiveAccruals" value="false"/>No
                                        </td>
                                        <td>Show Assigned Accruals</td>
                                        <td>
                                            <html:radio property="showAssignedAccruals" styleId="showAssignedAccruals" value="true"/>Yes
                                            <html:radio property="showAssignedAccruals" styleId="showAssignedAccruals" value="false"/>No
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="6" align="center">
                                            <input type="button" class="buttonStyleNew" value="Search" id="searchByFilter"/>
                                            <input type="button" class="buttonStyleNew" value="Reset" id="reset"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
                <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="margin: 10px 0 0 0">
                    <tr class="tableHeadingNew">
                        <td>Search Results</td>
                        <td align="right">
                            <input type="button" class="buttonStyleNew" value="Add Accrual" style="width: 100px;margin: 0 0 0 5px"  id="addAccrual"/>
                            <input type="button" class="buttonStyleNew" value="Save" style="width: 50px;margin: 0 0 0 5px"  id="save"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div class="searcResults">
                                <%@include file="arApplyPaymentsResults.jsp"%>
                            </div>
                        </td>
                    </tr>
                </table>
                <input type="hidden" name="className" id="className"/>
                <input type="hidden" name="methodName" id="methodName"/>
                <input type="hidden" name="forward" id="forward"/>
                <input type="hidden" name="setRequest" id="setRequest"/>
            </html:form>
            <div id="add-accrual-container" class="static-popup" style="display: none;width: 600px;height: 200px;">
                <c:import url="/jsps/AccountsPayable/accruals/addAccrual.jsp"/>    
            </div>
            <div id="update-accrual-container" class="static-popup" style="display: none;width: 600px;height: 200px;"></div>
            <input type="hidden" name="updateAccrualId" id="updateAccrualId">
            <input type="hidden" name="userEmailAddress" id="userEmailAddress" value="${loginuser.email}"/>
        </div>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsReceivable/arApplyPayments.js"></script>
        <c:if test="${not empty fileName}">
            <iframe src="${path}/servlet/FileViewerServlet?fileName=${fileName}" style="display:none;"></iframe>
        </c:if>
    </body>
</html>