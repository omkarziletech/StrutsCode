<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri='http://cong.logiwareinc.com/string' prefix='str'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${not empty sendDebitEmailList}">
    <c:choose>
        <c:when test="${not empty debitEmailValues}">
            <c:set var="sendDebitMemoToEmails" value="${fn:split(debitEmailValues, ',')}"/>
            <c:forEach var="customerContact" items="${sendDebitEmailList}">
                <c:if test="${not empty customerContact.email}"> 
                    <c:choose>
                        <c:when test="${customerContact.accountingSelected}">
                            <input type="checkbox" value="${customerContact.email}" class="debitEmail" checked disabled/>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${str:contains(sendDebitMemoToEmails, customerContact.email)}">
                                    <input type="checkbox" value="${customerContact.email}" class="debitEmail" checked/>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" value="${customerContact.email}" class="debitEmail"/>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                    ${customerContact.email}<br/>
                </c:if>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:forEach var="customerContact" items="${sendDebitEmailList}">
                <c:if test="${not empty customerContact.email}"> 
                    <c:choose>
                        <c:when test="${customerContact.accountingSelected}">
                            <input type="checkbox" value="${customerContact.email}" class="debitEmail" checked disabled/>
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" value="${customerContact.email}" class="debitEmail" checked/>
                        </c:otherwise>
                    </c:choose>
                    ${customerContact.email}
                    <br/>
                </c:if>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</c:if>   

