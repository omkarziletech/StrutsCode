<%-- 
    Document   : ports
    Created on : Mar 3, 2014, 7:00:27 PM
    Author     : Lakshmi Narayanan
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="bold uppercase">
    <span class="blue-70">${values[1]}</span><%--city--%>
    <c:if test="${not empty values[2]}">
        <span class="red">/${values[2]}</span><%--state--%>
    </c:if>
    <c:if test="${not empty values[3]}">
        <span class="green">/${values[3]}</span><%--country--%>
    </c:if>
    <c:if test="${not empty values[4]}">
        <span class="red-90">(${values[4]})</span><%--un loc code--%>
    </c:if>
</div>