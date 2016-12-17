<%-- 
    Document   : glAccount
    Created on : Jun 21, 2012, 01:01:01 PM
    Author     : Lakshmi Naryanan
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<font color="#093ba1">
${values[0]} <%--GL Account--%>
</font>
<c:if test="${not empty values[1]}">
    <-->
    <font color="#8f0c02">
    ${values[1]} <%--Desciption--%>
    </font>
</c:if>
