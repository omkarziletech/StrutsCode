<%-- 
    Document   : arBatchChecks
    Created on : Jun 12, 2011, 12:26:25 AM
    Author     : lakshh
--%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
    <tbody>
        <tr>
            <td class="lightBoxHeader">Checks for Batch - ${batchId}</td>
            <td>
                <a id="lightBoxClose" href="javascript:closeCheckDiv('${batchId}')">
                    <img alt="close" src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                </a>
            </td>
        </tr>
    </tbody>
</table>
<div class="message checkMessage">${message}</div>
<div class="error checkError">${error}</div>
<div style="width:100%;float:left;">
    <c:choose>
        <c:when test="${not empty paymentChecks}">
            <div class="scrolldisplaytable" style="height:  180px;">
                <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" id="paymentChecks" style="border: 1px solid white">
                    <thead>
                        <c:choose>
                            <c:when test="${arBatchForm.orderBy=='desc'}"><c:set var="orderBy" value="asc"/></c:when>
                            <c:otherwise><c:set var="orderBy" value="desc"/></c:otherwise>
                        </c:choose>
                        <tr>
                            <th>Check Number</th>
                            <th>Customer Number</th>
                            <th>Customer Name</th>
                            <th style="text-align: right">Check Amount</th>
                            <th style="text-align: right">Applied Amount</th>
                            <th style="text-align: center">Included Invoices</th>
                            <c:if test="${canEdit}">
                                <th>Action</th>
                            </c:if>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="zebra" value="odd"/>
                        <c:forEach var="check" items="${paymentChecks}">
                            <tr class="${zebra}">
                                <td>${check.checkNumber}</td>
                                <td>${check.customerNumber}</td>
                                <td>${check.customerName}</td>
                                <c:choose>
                                    <c:when test="${fn:startsWith(check.checkAmount,'-')}">
                                        <td style="text-align:right;" class="red">(${fn:replace(check.checkAmount,'-','')})</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td style="text-align:right;">${check.checkAmount}</td>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${fn:startsWith(check.appliedAmount,'-')}">
                                        <td style="text-align:right;" class="red">(${fn:replace(check.appliedAmount,'-','')})</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td style="text-align:right;">${check.appliedAmount}</td>
                                    </c:otherwise>
                                </c:choose>
                                        <td style="text-align: center">${check.noOfInvoices}</td>
                                <c:if test="${canEdit}">
                                    <td>
                                        <img src="${path}/img/icons/edit.gif"
                                             title="Edit Check" alt="Edit Check" border="0" onclick="editCheck('${batchId}','${check.checkId}','${check.subType}')"/>
                                        <img src="${path}/img/icons/remove.gif" title="Delete Check" alt="Delete Check"
                                             border="0" onclick="deleteCheck('${batchId}','${check.checkId}','${check.checkNumber}')"/>
                                         <c:choose>
                                            <c:when test="${roleDuty.viewAccountingScanAttach && transaction.subType == 'Y'}">

                                            </c:when>
                                            <c:otherwise>
                                                <img src="${path}/img/icons/upload.gif" title="Scan/Attach" alt="Scan/Attach"
                                                 border="0" onclick="showScanOrAttach('${batchId}-${check.checkNumber}')"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:if>
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
            </div>
        </c:when>
        <c:otherwise>No checks found</c:otherwise>
    </c:choose>
</div>
