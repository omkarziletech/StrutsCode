<%@include file="/constant.jsp" %>
<%@attribute name="value" type="java.util.Date"%>
<%@attribute name="format"%>
<jsp:setProperty name="dateUtil" property="date" value="${value}"/>
<c:choose>
    <c:when test="${not empty value}">
        <c:choose>
            <c:when test="${dateUtil.havingTime}">
                <fmt:formatDate var="formattedDate"  value="${value}" pattern="${format}"  />
            </c:when>
            <c:otherwise>
                <fmt:formatDate var="formattedDate"  value="${value}" pattern="MM/dd/yyyy"  />
            </c:otherwise>
        </c:choose>
    </c:when>
</c:choose>
${formattedDate}
