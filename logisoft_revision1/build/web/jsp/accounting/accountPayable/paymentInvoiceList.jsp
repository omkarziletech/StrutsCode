<%-- 
    Document   : paymentInvoiceList
    Created on : 24 Jun, 2015, 1:11:38 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<div class="table-banner green align-right">${fn:length(apPaymentForm.invoiceList)} invoice record${fn:length(apPaymentForm.invoiceList ) > 1 ? 's' : ''} found</div>
<div class="result-container" id="invoice-list">
    <table width="100%" cellpadding="0" cellspacing="1" class="display-table margin-none">
        <thead>
            <tr>
                <th>Invoice/BL</th>
                <th>Invoice Amount</th>
                <th>Invoice Date</th>
                <th>Due Date</th>
                <th>Age</th>
                <th>Terms</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:set var="zebra" value="odd"/>
            <c:forEach var="invoice" items="${apPaymentForm.invoiceList}" varStatus="status">
                <tr class="${zebra}">
                    <td>${invoice.invoiceOrBl}</td>
                    <td class="align-right ${fn:startsWith(invoice.invoiceAmount, '-') ? 'red' : 'black'}">
                        ${fn:startsWith(invoice.invoiceAmount, '-') ? '('.concat(fn:substringAfter(invoice.invoiceAmount, '-')).concat(')') : invoice.invoiceAmount}
                    </td>
                    <td>${invoice.invoiceDate}</td>
                    <td>${invoice.dueDate}</td>
                    <td class="align-right">${invoice.age}</td>
                    <td>${invoice.creditTerms}</td>
                    <td class="align-center">
                        <c:if test="${writeMode}">
                            <img src="${path}/images/icons/close.png" title="Remove Invoice" onclick="removeInvoice('${invoice.id}', '${apPaymentForm.ids}', '${writeMode}');"/>
                        </c:if>
                        <img title="Notes" src="${path}/images/notepad_${invoice.manualNotes ? 'green' : 'yellow'}.png" onclick="showNotes('${invoice.noteModuleId}', '${invoice.noteModuleRefId}')"/>
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${status.index % 2 eq 0}">
                        <c:set var="zebra" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="zebra" value="odd"/>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tbody>
    </table>
</div>