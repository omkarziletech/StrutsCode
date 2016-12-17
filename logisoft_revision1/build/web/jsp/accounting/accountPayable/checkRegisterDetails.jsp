<%-- 
    Document   : checkRegisterDetails
    Created on : 10 Jul, 2015, 10:03:31 PM
    Author     : Lucky
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<div class="table-banner green align-right">${fn:length(checkRegisterForm.invoiceList)} invoice record${fn:length(checkRegisterForm.invoiceList ) > 1 ? 's' : ''} found</div>
<div class="scrollable-table-popup" style="height: 325px;">
    <div>
        <div>
            <table>
                <thead>
                    <tr>
                        <th><div label="Check Number"/></th>
                        <th><div label="Vendor Name"/></th>
                        <th><div label="Vendor Number"/></th>
                        <th><div label="Invoice/BL"/></th>
                        <th><div label="Payment Method"/></th>
                        <th><div label="Payment Amount"/></th>
                        <th><div label="Payment Date"/></th>
                        <th><div label="Action"/></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="invoice" items="${checkRegisterForm.invoiceList}">
                        <tr>
                            <td>${checkRegisterForm.checkNumber}</td>
                            <td>${checkRegisterForm.vendorName}</td>
                            <td>${checkRegisterForm.vendorNumber}</td>
                            <td>${invoice.invoiceOrBl}</td>
                            <td>${checkRegisterForm.paymentMethod}</td>
                            <td class="align-right ${fn:startsWith(invoice.invoiceAmount, '-') ? 'red' : 'black'}">
                                ${fn:startsWith(invoice.invoiceAmount, '-') ? '('.concat(fn:substringAfter(invoice.invoiceAmount, '-')).concat(')') : invoice.invoiceAmount}
                            </td>
                            <td>${checkRegisterForm.paymentDate}</td>
                            <td class="align-center">
                                <c:if test="${writeMode}">
                                    <img title="Notes" src="${path}/images/notepad_${invoice.manualNotes ? 'green' : 'yellow'}.png" onclick="showNotes('${invoice.noteModuleId}', '${invoice.noteModuleRefId}')"/>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>