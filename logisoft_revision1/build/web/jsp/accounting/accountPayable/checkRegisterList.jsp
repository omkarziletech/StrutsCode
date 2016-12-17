<%-- 
    Document   : checkRegisterList
    Created on : 8 Jul, 2015, 9:50:30 PM
    Author     : Lucky
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${not empty checkRegisterForm.paymentList}">
        <div class="table-banner green align-right">${fn:length(checkRegisterForm.paymentList)} record${fn:length(checkRegisterForm.paymentList) > 1 ? 's' : ''} found</div>
        <div class="scrollable-table">
            <div>
                <div>
                    <table>
                        <thead>
                            <tr>
                                <th><div label="Batch Number"/></th>
                                <th><div label="Check Number"/></th>
                                <th><div label="Vendor Name"/></th>
                                <th><div label="Vendor Number"/></th>
                                <th><div label="Payment Method"/></th>
                                <th><div label="Payment Amount"/></th>
                                <th><div label="Payment Date"/></th>
                                <th><div label="GL Account"/></th>
                                <th><div label="Bank Account"/></th>
                                <th><div label="Cleared"/></th>
                                <th><div label="Cleared Date"/></th>
                                <th><div label="Void"/></th>
                                <th><div label="Reprint"/></th>
                                <th><div label="Action"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="payment" items="${checkRegisterForm.paymentList}" varStatus="status">
                                <tr>
                                    <td>
                                        ${payment.batchId}
                                        <input type="hidden" class="batchId" value="${payment.batchId}"/>
                                    </td>
                                    <td>
                                        ${payment.checkNumber}
                                        <input type="hidden" class="checkNumber" value="${payment.checkNumber}"/>
                                    </td>
                                    <td>
                                        <span title="${payment.vendorName}">
                                            ${str:abbreviate(payment.vendorName, 20)}
                                        </span>
                                        <input type="hidden" class="vendorName" value="${payment.vendorName}"/>
                                    </td>
                                    <td>
                                        ${payment.vendorNumber}
                                        <input type="hidden" class="vendorNumber" value="${payment.vendorNumber}"/>
                                    </td>
                                    <td>
                                        ${payment.paymentMethod}
                                        <input type="hidden" class="paymentMethod" value="${payment.paymentMethod}"/>
                                    </td>
                                    <td class="align-right ${fn:startsWith(payment.paymentAmount, '-') ? 'red' : 'black'}">
                                        ${fn:startsWith(payment.paymentAmount, '-') ? '('.concat(fn:substringAfter(payment.paymentAmount, '-')).concat(')') : payment.paymentAmount}
                                        <input type="hidden" class="paymentAmount" value="${payment.paymentAmount}"/>
                                    </td>
                                    <td>
                                        ${payment.paymentDate}
                                        <input type="hidden" class="paymentDate" value="${payment.paymentDate}"/>
                                    </td>
                                    <td>
                                        ${payment.glAccount}
                                        <input type="hidden" class="glAccount" value="${payment.glAccount}"/>
                                    </td>
                                    <td>
                                        ${payment.bankAccount}
                                        <input type="hidden" class="bankName" value="${payment.bankName}"/>
                                        <input type="hidden" class="bankAccount" value="${payment.bankAccount}"/>
                                    </td>
                                    <td class="align-center">
                                        <c:if test="${payment.cleared}">
                                            <img src="${path}/images/icons/yes.png" title="Cleared"/>
                                        </c:if>
                                    </td>
                                    <td class="align-center">
                                        <c:if test="${payment.cleared}">
                                            ${payment.clearedDate}
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${payment.voided}">
                                                <input type="checkbox" class="voided" checked disabled/>
                                            </c:when>
                                            <c:when test="${writeMode && roleDuty.checkRegisterController}">
                                                <input type="checkbox" class="voided" onclick="voided(this)"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox" class="voided" disabled/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${!payment.cleared && !payment.voided 
                                                            && fn:toUpperCase(payment.paymentMethod) eq 'CHECK'
                                                            && writeMode && roleDuty.checkRegisterController}">
                                                <input type="checkbox" class="reprinted" onclick="reprinted(this)"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox" class="reprinted" disabled/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="align-center">
                                        <input type="hidden" class="ids" value="${payment.ids}"/>
                                        <img src="${path}/img/icons/showall.gif" title="Show Details" onclick="showInvoiceDetails('${payment.ids}', '${payment.vendorName}', '${payment.vendorNumber}', '${payment.paymentMethod}', '${payment.paymentDate}', '${payment.checkNumber}', '${writeMode}');"/>
                                        <c:if test="${writeMode}">
                                            <img src="${path}/images/icons/pdf.png" title="Print" onclick="createReport('${payment.ids}', '${payment.vendorName}', '${payment.vendorNumber}', '${payment.paymentMethod}', '${payment.paymentDate}', '${payment.checkNumber}');"/>
                                            <img src="${path}/images/icons/excel.png" title="Export to Excel" onclick="createExcel('${payment.ids}', '${payment.vendorName}', '${payment.vendorNumber}', '${payment.paymentMethod}', '${payment.paymentDate}', '${payment.checkNumber}');"/>
                                            <img src="${path}/images/notepad_${payment.manualNotes ? 'green' : 'yellow'}.png" title="Notes" onclick="showNotes('${payment.noteModuleId}', '${payment.noteModuleRefId}')"/>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-banner green">No records found</div>
    </c:otherwise>
</c:choose>