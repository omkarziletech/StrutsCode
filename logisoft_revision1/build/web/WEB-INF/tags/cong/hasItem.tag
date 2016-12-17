<%@include file="/taglib.jsp" %>
<%@include file="/constant.jsp" %>
<%@attribute name="itemId" required="true"%>
<c:if test="${not empty user}">
    <jsp:setProperty name="menu" property="itemId" value="${itemId}"/>
    <jsp:setProperty name="menu" property="roleId" value="${user.role.id}"/>
    <c:if test="${menu.itemAvailableForRole}">
        <jsp:doBody/>            
    </c:if>
</c:if>


