<%--
    Document   : addFclBookingCost
    Created on : May 09, 2013, 2:44:23 PM
    Author     : Balaji.E
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
    <c:if test="${not empty fclBlCostCodesList}">
        <div>
            <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew"  style="border: 1px solid white">
                <thead>
                    <tr>
                        <th></th>
                        <th>Cost Code </th>
                        <th> TT </th>
                        <th> Cost Code Desc </th>
                        <th> Vendor Name </th>
                        <th> Vendor Account </th>
                        <th> Invoice Number </th>
                        <th> Amount </th>
                        <th> Currency </th>
                        <th> Date Paid </th>
                        <th> Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="zebra" value="odd"/>
                    <c:forEach var="bookingAccruals" items="${fclBlCostCodesList}">
                        <tr class="${zebra}">
                            <td style="color:#D200D2;font-weight:bold;font-size:14pt;width:8px;">*</td>
                            <td>${bookingAccruals.costCode}</td>
                            <td>${bookingAccruals.transactionType}</td>
                            <td>${bookingAccruals.costCodeDesc}</td>
                            <td>${bookingAccruals.accName}</td>
                            <td>${bookingAccruals.accNo}</td>
                            <td>${bookingAccruals.invoiceNumber}</td>
                            <fmt:formatNumber var="costAmount" value="${bookingAccruals.amount}" pattern="##########0.00"/>
                            <td>${costAmount}</td>
                            <td>USD</td>
                            <td>${bookingAccruals.datePaid}</td>
                            <td>
                                <c:if test="${bookingAccruals.transactionType!='AP' && bookingAccruals.transactionType!='IP' && bookingAccruals.transactionType!='PN'}">
                                    <img alt="Edit Accrual" title="Edit Accrual"
                                         src="${path}/img/icons/edit.gif" onclick="editBookingAccruals('${bookingAccruals.codeId}');"/>
                                </c:if>
                                <c:if test="${bookingAccruals.transactionType!='AP' && bookingAccruals.transactionType!='IP' && bookingAccruals.transactionType!='PN'
                                      && bookingAccruals.transactionType!='DS'}">
                                      <img alt="Delete Accrual" title="Delete Accrual"
                                           src="${path}/img/icons/delete.gif" onclick="deleteBookingAccruals('${bookingAccruals.codeId}');"/>
                                </c:if>
                                      <c:if test="${not empty bookingAccruals.costComments}">
                                      <img id="viewgif1" alt="comments" src="${path}/img/icons/view.gif" onmouseover="tooltip.showComments('<strong>${bookingAccruals.costComments} </strong>',100,event);"
                                     onmouseout="tooltip.hideComments();" style="color:black;"/>
                                      </c:if>
                            </td>
                        </tr>
                        <c:choose>
                            <c:when test="${zebra eq 'odd'}">
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
</c:if>