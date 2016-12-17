<%-- 
    Document   : ShowPaymentsTemplate
    Created on : Oct 6, 2009, 12:48:50 PM
    Author     : Lakshminarayanan
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
            <td class="lightBoxHeader">Payment Details</td>
            <td>
                <a id="lightBoxClose" href="javascript: closePopUpDiv()">
                    <img alt="" src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                </a>
            </td>
        </tr>
    </tbody>
</table>
<div class="scrolldisplaytable">
    <c:choose>
        <c:when test="${not empty paymentList}">
            <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" style="border: 1px solid white">
                <thead>
                    <tr>
                        <th>Check Number</th>
                        <th style="text-align:right;">Paid Amount</th>
                        <th>Paid Date</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="zebra" value="odd"/>
                    <c:forEach var="payment" items="${paymentList}">
                        <tr class="${zebra}">
                            <td>${payment.chequenumber}</td>
                            <c:choose>
                                <c:when test="${fn:contains(payment.amount, '-')}">
                                    <c:set var="amount" value="(${fn:replace(payment.amount,'-','')})"/>
                                    <c:set var="color" value="red"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="amount" value="${payment.amount}"/>
                                    <c:set var="color" value="black"/>
                                </c:otherwise>
                            </c:choose>
                            <td class="${color}" style="text-align:right;">${amount}</td>
                            <td>${payment.checkDate}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <span class="pagebanner">
                No Payment Details Found.
            </span>
        </c:otherwise>
    </c:choose>
</div>

