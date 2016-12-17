<%-- 
    Document   :compareApplyPayments
    Created on :Jun 23, 2011, 5:23:46 PM
    Author     :lakshh
--%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table style="background-image:url(${path}/js/greybox/header_bg.gif);border-bottom:2px solid #AAAAAA;width:100%" id="header">
    <tbody>
        <tr>
            <td class="lightBoxHeader">Applypayments before and after save</td>
            <td>
                <a id="lightBoxClose" href="javascript:closePopUpDiv()">
                    <img alt="" src="/logisoft/js/greybox/w_close.gif" title="Close" style="border:none;">Close
                </a>
            </td>
        </tr>
    </tbody>
</table>
<div style="width:100%;height:80%;overflow:auto;">
    <table cellpadding="0" cellspacing="2" border="0" style="width:100%;" class="tableBorderNew">
        <tr class="tableHeading" style="text-align:center;">
            <td>Before</td>
            <td>After</td>
        </tr>
        <tr class="tableHeadingNew">
            <td colspan="2" style="text-align: center">Check Details</td>
        </tr>
        <tr>
            <td style="vertical-align:top">
                <table cellpadding="0" cellspacing="2" border="0" style="width:200px;" class="displaytagstyleNew">
                    <thead>
                        <tr>
                            <th>Check number</th>
                            <th>Check Amount</th>
                            <th>Applied Amount</th>
                            <th>Check Balance</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr class="odd">
                            <td class="uppercase">${before.checkNumber}</td>
                            <c:choose>
                                <c:when test="${not empty before.checkTotalAmount && fn:contains(before.checkTotalAmount,'-')}">
                                    <td style="text-align:right;" class="red">(${fn:replace(before.checkTotalAmount,'-','')})</td>
                                </c:when>
                                <c:otherwise>
                                    <td style="text-align:right;">${before.checkTotalAmount}</td>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${not empty before.appliedAmount && fn:contains(before.appliedAmount,'-')}">
                                    <td style="text-align:right;" class="red">(${fn:replace(before.appliedAmount,'-','')})</td>
                                </c:when>
                                <c:otherwise>
                                    <td style="text-align:right;">${before.appliedAmount}</td>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${not empty before.checkBalance && fn:contains(before.checkBalance,'-')}">
                                    <td style="text-align:right;" class="red">(${fn:replace(before.checkBalance,'-','')})</td>
                                </c:when>
                                <c:otherwise>
                                    <td style="text-align:right;">${before.checkBalance}</td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </tbody>
                </table>
            </td>
            <td style="vertical-align:top">
                <table cellpadding="0" cellspacing="2" border="0" style="width:200px;" class="displaytagstyleNew">
                    <thead>
                        <tr>
                            <th>Check number</th>
                            <th>Check Amount</th>
                            <th>Applied Amount</th>
                            <th>Check Balance</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr class="odd">
                            <td class="uppercase">${after.checkNumber}</td>
                            <c:choose>
                                <c:when test="${not empty after.checkTotalAmount && fn:contains(after.checkTotalAmount,'-')}">
                                    <td style="text-align:right;" class="red">(${fn:replace(after.checkTotalAmount,'-','')})</td>
                                </c:when>
                                <c:otherwise>
                                    <td style="text-align:right;">${after.checkTotalAmount}</td>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${not empty after.appliedAmount && fn:contains(after.appliedAmount,'-')}">
                                    <td style="text-align:right;" class="red">(${fn:replace(after.appliedAmount,'-','')})</td>
                                </c:when>
                                <c:otherwise>
                                    <td style="text-align:right;">${after.appliedAmount}</td>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${not empty after.checkBalance && fn:contains(after.checkBalance,'-')}">
                                    <td style="text-align:right;" class="red">(${fn:replace(after.checkBalance,'-','')})</td>
                                </c:when>
                                <c:otherwise>
                                    <td style="text-align:right;">${after.checkBalance}</td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        <c:if test="${not empty before.onAccount || not empty after.onAccount}">
            <tr class="tableHeadingNew">
                <td colspan="2" style="text-align: center">Apply to On Account</td>
            </tr>
            <tr class="textLabelsBold" style="font-weight: bolder;font-size: 15px;text-align: center">
                <td>Before ==>
                    <c:choose>
                        <c:when test="${not empty before.onAccount && fn:contains(before.onAccount,'-')}">
                            <label class="red">(${fn:replace(before.onAccount,'-','')})</label>
                        </c:when>
                        <c:otherwise>
                            ${before.onAccount}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>After ==> 
                    <c:choose>
                        <c:when test="${not empty after.onAccount && fn:contains(after.onAccount,'-')}">
                            <label class="red">(${fn:replace(after.onAccount,'-','')})</label>
                        </c:when>
                        <c:otherwise>
                            ${after.onAccount}
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:if>
        <c:if test="${not empty before.prepayments || not empty after.prepayments}">
            <tr class="tableHeadingNew">
                <td colspan="2" style="text-align: center">Apply to Prepayments</td>
            </tr>
            <tr>
                <td style="vertical-align:top">
                    <table cellpadding="0" cellspacing="2" border="0" style="width:200px;" class="displaytagstyleNew">
                        <thead>
                            <tr>
                                <th>Dock Receipt</th>
                                <th>Amount</th>
                                <th>Notes</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="zebra" value="odd"/>
                            <c:forEach var="prepayment" items="${before.prepayments}">
                                <tr class="${zebra}">
                                    <td class="uppercase">${prepayment.docReceipt}</td>
                                    <c:choose>
                                        <c:when test="${fn:contains(prepayment.paidAmount,'-')}">
                                            <td style="text-align:right;" class="red">(${fn:replace(prepayment.paidAmount,'-','')})</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="text-align:right;">${prepayment.paidAmount}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="uppercase">${prepayment.notes}</td>
                                </tr>
                                <c:choose>
                                    <c:when test="${zebra=='odd'}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tbody>
                    </table>
                </td>
                <td style="vertical-align:top">
                    <table cellpadding="0" cellspacing="2" border="0" style="width:200px;" class="displaytagstyleNew">
                        <thead>
                            <tr>
                                <th>Dock Receipt</th>
                                <th>Amount</th>
                                <th>Notes</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="zebra" value="odd"/>
                            <c:forEach var="prepayment" items="${after.prepayments}">
                                <tr class="${zebra}">
                                    <td class="uppercase">${prepayment.docReceipt}</td>
                                    <c:choose>
                                        <c:when test="${fn:contains(prepayment.paidAmount,'-')}">
                                            <td style="text-align:right;" class="red">(${fn:replace(prepayment.paidAmount,'-','')})</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="text-align:right;">${prepayment.paidAmount}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="uppercase">${prepayment.notes}</td>
                                </tr>
                                <c:choose>
                                    <c:when test="${zebra=='odd'}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tbody>
                    </table>
                </td>
            </tr>
        </c:if>
        <c:if test="${not empty before.chargeCodes || not empty after.chargeCodes}">
            <tr class="tableHeadingNew">
                <td colspan="2" style="text-align: center">Apply to Charge Code/GL Account</td>
            </tr>
            <tr>
                <td style="vertical-align:top">
                    <table cellpadding="0" cellspacing="2" border="0" style="width:200px;" class="displaytagstyleNew">
                        <thead>
                            <tr>
                                <th>GL Account</th>
                                <th>Amount</th>
                                <th>Notes</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="zebra" value="odd"/>
                            <c:forEach var="chargeCode" items="${before.chargeCodes}">
                                <tr class="${zebra}">
                                    <td>${chargeCode.glAccount}</td>
                                    <c:choose>
                                        <c:when test="${fn:contains(chargeCode.paidAmount,'-')}">
                                            <td style="text-align:right;" class="red">(${fn:replace(chargeCode.paidAmount,'-','')})</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="text-align:right;">${chargeCode.paidAmount}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="uppercase">${chargeCode.notes}</td>
                                </tr>
                                <c:choose>
                                    <c:when test="${zebra=='odd'}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tbody>
                    </table>
                </td>
                <td style="vertical-align:top">
                    <table cellpadding="0" cellspacing="2" border="0" style="width:200px;" class="displaytagstyleNew">
                        <thead>
                            <tr>
                                <th>GL Account</th>
                                <th>Amount</th>
                                <th>Notes</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="zebra" value="odd"/>
                            <c:forEach var="chargeCode" items="${after.chargeCodes}">
                                <tr class="${zebra}">
                                    <td>${chargeCode.glAccount}</td>
                                    <c:choose>
                                        <c:when test="${fn:contains(chargeCode.paidAmount,'-')}">
                                            <td style="text-align:right;" class="red">(${fn:replace(chargeCode.paidAmount,'-','')})</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="text-align:right;">${chargeCode.paidAmount}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="uppercase">${chargeCode.notes}</td>
                                </tr>
                                <c:choose>
                                    <c:when test="${zebra=='odd'}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tbody>
                    </table>
                </td>
            </tr>
        </c:if>
        <c:if test="${not empty before.transactions || not empty after.transactions}">
            <tr class="tableHeadingNew">
                <td colspan="2" style="text-align: center">Applied Invoices</td>
            </tr>
            <tr>
                <td style="vertical-align:top">
                    <table cellpadding="0" cellspacing="2" border="0" style="width:300px;" class="displaytagstyleNew">
                        <thead>
                            <tr>
                                <th>Customer Number</th>
                                <th>Invoice/BL</th>
                                <th>Type</th>
                                <th>Amount</th>
                                <th>Balance In Process</th>
                                <th>Paid Amount</th>
                                <th>Adjust Amount</th>
                                <th>GL Account</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="zebra" value="odd"/>
                            <c:forEach var="transaction" items="${before.transactions}">
                                <tr class="${zebra}">
                                    <td class="uppercase">${transaction.customerNumber}</td>
                                    <td class="uppercase">${transaction.invoiceOrBl}</td>
                                    <td>${transaction.transactionType}</td>
                                    <c:choose>
                                        <c:when test="${fn:contains(transaction.transactionAmount,'-')}">
                                            <td style="text-align:right;" class="red">(${fn:replace(transaction.transactionAmount,'-','')})</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="text-align:right;">${transaction.transactionAmount}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${fn:contains(transaction.balanceInProcess,'-')}">
                                            <td style="text-align:right;" class="red">(${fn:replace(transaction.balanceInProcess,'-','')})</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="text-align:right;">${transaction.balanceInProcess}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${fn:contains(transaction.paidAmount,'-')}">
                                            <td style="text-align:right;" class="red">(${fn:replace(transaction.paidAmount,'-','')})</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="text-align:right;">${transaction.paidAmount}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${fn:contains(transaction.adjustAmount,'-')}">
                                            <td style="text-align:right;" class="red">(${fn:replace(transaction.adjustAmount,'-','')})</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="text-align:right;">${transaction.adjustAmount}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td>${transaction.glAccount}</td>
                                </tr>
                                <c:choose>
                                    <c:when test="${zebra=='odd'}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tbody>
                    </table>
                </td>
                <td style="vertical-align:top">
                    <table cellpadding="0" cellspacing="2" border="0" style="width:300px;" class="displaytagstyleNew">
                        <thead>
                            <tr>
                                <th>Customer Number</th>
                                <th>Invoice/BL</th>
                                <th>Type</th>
                                <th>Amount</th>
                                <th>Balance In Process</th>
                                <th>Paid Amount</th>
                                <th>Adjust Amount</th>
                                <th>GL Account</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="zebra" value="odd"/>
                            <c:forEach var="transaction" items="${after.transactions}">
                                <tr class="${zebra}">
                                    <td class="uppercase">${transaction.customerNumber}</td>
                                    <td class="uppercase">${transaction.invoiceOrBl}</td>
                                    <td>${transaction.transactionType}</td>
                                    <c:choose>
                                        <c:when test="${fn:contains(transaction.transactionAmount,'-')}">
                                            <td style="text-align:right;" class="red">(${fn:replace(transaction.transactionAmount,'-','')})</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="text-align:right;">${transaction.transactionAmount}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${fn:contains(transaction.balanceInProcess,'-')}">
                                            <td style="text-align:right;" class="red">(${fn:replace(transaction.balanceInProcess,'-','')})</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="text-align:right;">${transaction.balanceInProcess}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${fn:contains(transaction.paidAmount,'-')}">
                                            <td style="text-align:right;" class="red">(${fn:replace(transaction.paidAmount,'-','')})</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="text-align:right;">${transaction.paidAmount}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${fn:contains(transaction.adjustAmount,'-')}">
                                            <td style="text-align:right;" class="red">(${fn:replace(transaction.adjustAmount,'-','')})</td>
                                        </c:when>
                                        <c:otherwise>
                                            <td style="text-align:right;">${transaction.adjustAmount}</td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td>${transaction.glAccount}</td>
                                </tr>
                                <c:choose>
                                    <c:when test="${zebra=='odd'}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tbody>
                    </table>
                </td>
            </tr>
        </c:if>
    </table>
</div>
<div style="text-align: center" id="buttons">
    <input type="button" class="buttonStyleNew" value="Save Now" onclick="saveAfterCompare()"/>
    <input type="button" class="buttonStyleNew" value="Cancel" onclick="closePopUpDiv()"/>
    <input type="button" class="buttonStyleNew" value="Print" onclick="window.print()"/>
</div>