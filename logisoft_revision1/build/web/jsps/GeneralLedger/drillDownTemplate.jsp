<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div style="width:100%;float:left;">
    <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" style="border: 1px solid white">
        <thead>
            <tr>
                <th>Charge Code</th>
                <th style="text-align: right">Amount</th>
            </tr>
        </thead>
        <tbody>
            <c:set var="zebra" value="odd"/>
            <c:choose>
                <c:when test="${not empty drillDownDetials}">
                    <c:forEach var="subledger" items="${drillDownDetials}">
                        <tr class="${zebra}">
                            <td>${subledger.chargeCode}</td>
                            <c:choose>
                                <c:when test="${fn:startsWith(subledger.amount,'-')}">
                                    <td style="text-align:right;" class="red">(${fn:replace(subledger.amount,'-','')})</td>
                                </c:when>
                                <c:otherwise>
                                    <td style="text-align:right;">${subledger.amount}</td>
                                </c:otherwise>
                            </c:choose>
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
                </c:when>
                <c:otherwise>
                    <tr class="${zebra}">
                        <td colspan="2">
                            <div align="right" id="pager" class="pagebanner" style="padding-right: 15px;">
                                No details found.
                            </div>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>