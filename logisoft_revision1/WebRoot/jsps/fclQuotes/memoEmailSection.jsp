<%-- 
    Document   : memoEmailSection
    Created on : Nov 27, 2012, 2:51:43 PM
    Author     : Rajeshkumar S
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>

<table>
    <tr>
        <td class="textlabelsBold">Credit Memo To</td>
        <td>
            <div id="creditMemoEmailDiv" class="divstyleThin">
                <c:if test="${not empty arCreditEmails}">
                    <c:forEach var="arCreditEmailId" items="${arCreditEmails}">
                        <c:if test="${not empty arCreditEmailId}">
                            <input type="checkbox" class="creditEmailId" checked disabled value="${arCreditEmailId}"/>${arCreditEmailId}<br>
                        </c:if>
                    </c:forEach>
                </c:if>
                <c:if test="${not empty creditEmails}">
                    <c:forEach var="creditEmailId" items="${creditEmails}">
                        <c:if test="${not empty creditEmailId}">
                            <input type="checkbox" class="creditEmailId" checked value="${creditEmailId}"/>${creditEmailId}<br>
                        </c:if>
                    </c:forEach>
                </c:if>
                <c:if test="${(not empty arCreditEmails || not empty creditEmails) && not empty creditContacts }">
                    <c:forEach var="creditContact" items="${creditContacts}">
                        <c:if test="${not empty creditContact}">
                            <input type="checkbox" class="creditEmailId" value="${creditContact}"/>${creditContact}<br>
                        </c:if>
                    </c:forEach>
                </c:if>
            </div>
        </td>
        <td class="textlabelsBold">Debit Memo To</td>
        <td>
            <div id="debitMemoEmailDiv" class="divstyleThin">
                <c:if test="${not empty arDebitEmails}">
                    <c:forEach var="arDebitEmailId" items="${arDebitEmails}">
                        <c:if test="${not empty arDebitEmailId}">
                            <input type="checkbox" class="debitEmailId" checked disabled value="${arDebitEmailId}"/>${arDebitEmailId}<br>
                        </c:if>
                    </c:forEach>
                </c:if>
                <c:if test="${not empty debitEmails}">
                    <c:forEach var="debitEmailId" items="${debitEmails}">
                        <c:if test="${not empty debitEmailId}">
                            <input type="checkbox" class="debitEmailId" checked value="${debitEmailId}"/>${debitEmailId}<br>
                        </c:if>
                    </c:forEach>
                </c:if>
                <c:if test="${(not empty arDebitEmails || not empty debitEmails) && not empty debitContacts}">
                    <c:forEach var="debitContact" items="${debitContacts}">
                        <c:if test="${not empty debitContact}">
                            <input type="checkbox" class="debitEmailId" value="${debitContact}"/>${debitContact}<br>
                        </c:if>
                    </c:forEach>
                </c:if>
            </div>
        </td>
    </tr>
</table>