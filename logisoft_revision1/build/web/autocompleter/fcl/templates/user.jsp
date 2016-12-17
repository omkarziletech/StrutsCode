<%-- 
    Document   : user
    Created on : Mar 19, 2014, 6:53:35 PM
    Author     : Lakshmi Narayanan
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="bold uppercase">
    <span class="blue-70">${values[0]}</span><%--login name--%>
    <c:if test="${not empty values[1]}">
        <span class="red"><-->${values[1]}</span><%--role--%>
    </c:if>
</div>
<c:if test="${not empty values[2]}">
    <div>
        <span class="grey font-8px">${values[2]}</span><%--email--%>
    </div>
</c:if>