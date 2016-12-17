<%-- 
    Document   : arTransactionHistory
    Created on : May 6, 2010, 12:24:14 AM
    Author     : LakshmiNarayanan
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
            <td class="lightBoxHeader">Transaction History</td>
            <td>
                <a id="lightBoxClose" href="javascript: closePopUpDiv()">
                    <img alt="close" src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                </a>
            </td>
        </tr>
    </tbody>
</table>
<div class="scrolldisplaytable" style="width:690px;height:270px;overflow: auto">
    <c:choose>
        <c:when test="${!empty arTransactionHistoryList}">
            <c:set var="totalTransactionAmt" value="0"/>
            <c:set var="totalAdjustmentAmt" value="0"/>
            <display:table name="${arTransactionHistoryList}" defaultsort="3" defaultorder="ascending"
                           class="displaytagstyle" id="arTransactionHistory" style="width:100%;align:center" sort="list">
                <display:setProperty name="paging.banner.placement" value="none" />
                <display:column title="Batch#" property="batchId"/>
                <display:column title="Check#" property="checkNo"/>
                <display:column title="Date" property="transactionDate" format="{0,date,MM/dd/yyyy}"/>
                <display:column title="Amount" property="transactionAmount" format="{0,number,###,###,##0.00}" style="text-align:right"/>
                <display:column title="Type" property="transactionType"/>
                <display:column title="GL Account" property="glAccount"/>
                <display:column title="Adj Date" property="adjustmentDate" format="{0,date,MM/dd/yyyy}"/>
                <display:column title="Adj Amount" property="adjustmentAmount" format="{0,number,###,###,##0.00}" style="text-align:right"/>
                <display:column title="User" property="userName"/>
                <display:column style="display:none">
                    <c:set var="totalTransactionAmt" value="${totalTransactionAmt+arTransactionHistory.transactionAmount}"/>
                    <c:set var="totalAdjustmentAmt" value="${totalAdjustmentAmt+arTransactionHistory.adjustmentAmount}"/>
                </display:column>
                <display:footer>
                    <td colspan="3" align="right" style="font-weight: bolder">Total: </td>
                    <td align="right" style="font-size: 11px">
                        <fmt:formatNumber  pattern="###,###,##0.00" value="${totalTransactionAmt}"/>
                    </td>
                    <td colspan="3" align="right" style="font-weight: bolder">Adj.Total:</td>
                    <td align="right" style="font-size: 11px">
                        <fmt:formatNumber  pattern="###,###,##0.00" value="${totalAdjustmentAmt}"/>
                    </td>
                    <td></td>
                    <tr>
                        <td colspan="3" align="right" style="font-weight: bolder">Grand Total: </td>
                        <td align="right" style="font-size: 11px">
                            <fmt:formatNumber  pattern="###,###,##0.00" value="${totalTransactionAmt+totalAdjustmentAmt}"/>
                        </td>
                        <td colspan="5"></td>
                    </tr>
                </display:footer>
            </display:table>
        </c:when>
        <c:otherwise>
            <span class="pagebanner">
                No Transactions Found.
            </span>
        </c:otherwise>
    </c:choose>
    <c:if test="${!empty unpostedPayments}">
        <div style="background-color: lightslategray;font-weight: bolder">Unposted Payments</div>
        <c:set var="totalTransactionAmt" value="0"/>
        <c:set var="totalAdjustmentAmt" value="0"/>
        <display:table name="${unpostedPayments}" defaultsort="3" defaultorder="ascending"
                       class="displaytagstyle" id="arTransactionHistory" style="width:100%;align:center" sort="list">
            <display:setProperty name="paging.banner.placement" value="none" />
            <display:column title="Batch#" property="batchId"/>
            <display:column title="Check#" property="checkNo"/>
            <display:column title="Date" property="transactionDate" format="{0,date,MM/dd/yyyy}"/>
            <display:column title="Amount" property="transactionAmount" format="{0,number,###,###,##0.00}" style="text-align:right"/>
            <display:column title="Type" property="transactionType"/>
            <display:column title="GL Account" property="glAccount"/>
            <display:column title="Adj Date" property="adjustmentDate" format="{0,date,MM/dd/yyyy}"/>
            <display:column title="Adj Amount" property="adjustmentAmount" format="{0,number,###,###,##0.00}" style="text-align:right"/>
            <display:column title="User" property="userName"/>
            <display:column style="display:none">
                <c:set var="totalTransactionAmt" value="${totalTransactionAmt+arTransactionHistory.transactionAmount}"/>
                <c:set var="totalAdjustmentAmt" value="${totalAdjustmentAmt+arTransactionHistory.adjustmentAmount}"/>
            </display:column>
            <display:footer>
                <td colspan="3" align="right" style="font-weight: bolder">Total: </td>
                <td align="right" style="font-size: 11px">
                    <fmt:formatNumber  pattern="###,###,##0.00" value="${totalTransactionAmt}"/>
                </td>
                <td colspan="3" align="right" style="font-weight: bolder">Adj.Total:</td>
                <td align="right" style="font-size: 11px">
                    <fmt:formatNumber  pattern="###,###,##0.00" value="${totalAdjustmentAmt}"/>
                </td>
                <td></td>
                <tr>
                    <td colspan="3" align="right" style="font-weight: bolder">Grand Total: </td>
                    <td align="right" style="font-size: 11px">
                        <fmt:formatNumber  pattern="###,###,##0.00" value="${totalTransactionAmt+totalAdjustmentAmt}"/>
                    </td>
                    <td colspan="5"></td>
                </tr>
            </display:footer>
        </display:table>
    </c:if>
</div>

