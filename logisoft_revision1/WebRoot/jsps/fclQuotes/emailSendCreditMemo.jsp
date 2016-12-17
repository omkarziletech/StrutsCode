<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri='http://cong.logiwareinc.com/string' prefix='str'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${not empty sendCreditMemoList}">
    <c:choose>
        <c:when test="${not empty creditEmailValues}">
            <c:set var="sendCreditMemoToEmails" value="${fn:split(creditEmailValues, ',')}"/>
            <c:forEach var="customerContact" items="${sendCreditMemoList}">
                <c:if test="${not empty customerContact.email}"> 
                    <c:choose>
                        <c:when test="${customerContact.accountingSelected}">
                            <input type="checkbox" value="${customerContact.email}" class="creditEmail" checked disabled/>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${str:contains(sendCreditMemoToEmails, customerContact.email)}">
                                    <input type="checkbox" value="${customerContact.email}" class="creditEmail" checked/>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" value="${customerContact.email}" class="creditEmail"/>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                    ${customerContact.email}<br/>
                </c:if>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:forEach var="customerContact" items="${sendCreditMemoList}">
                <c:if test="${not empty customerContact.email}"> 
                    <c:choose>
                        <c:when test="${customerContact.accountingSelected}">
                            <input type="checkbox" value="${customerContact.email}" class="creditEmail" checked disabled/>
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" value="${customerContact.email}" class="creditEmail" checked/>
                        </c:otherwise>
                    </c:choose>
                    ${customerContact.email}
                    <br/>
                </c:if>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</c:if>