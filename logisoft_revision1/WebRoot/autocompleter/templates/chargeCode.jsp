<%-- 
    Document   : chargeCode
    Created on : Sep 18, 2012, 6:21:41 PM
    Author     : Lakshmi Naryanan
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--Cost Code--%>
<font color="#8f0c02">
    ${values[0]}
</font>
<%--Description--%>
<c:if test="${not empty values[1]}">
    <-->
    <font color="green">
	${values[1]}
    </font>
</c:if>