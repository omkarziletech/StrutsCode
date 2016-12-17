<%@include file="/taglib.jsp" %>
<%@attribute name="role"%>
<c:catch>
    <c:if test="${not empty user}">
        <c:if test="${user.role.description eq role}">
            <jsp:doBody/>            
        </c:if>
    </c:if>
</c:catch>

