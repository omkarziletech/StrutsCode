<%-- 
    Document   : vendor
    Created on : May 10, 2012, 08:21:01 PM
    Author     : Lakshmi Naryanan
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--Vendor Name--%>
<font color="#093ba1">
    ${values[0]}
</font>
<-->
<%--Vendor Number--%>
<font color="#8f0c02">
    ${values[1]}
</font>
<-->
<%--Account Type--%>
<font color="red">
    ${values[2]}
    <c:if test="${not empty values[3]}"> 
	- ${values[3]}
    </c:if>
</font>
<%--Master--%>
<c:if test="${not empty values[4]}">
    <font color="green">
	(${values[4]})
    </font>
</c:if>
<%--Address--%>
<c:if test="${not empty values[5]}">
    <br>
    <font color="#585858" style="font-size: 8px !important;">
	${values[5]} <%--Street Address--%>
	<c:if test="${not empty values[6]}">
	    , ${values[6]} <%--City--%>
	</c:if>
	<c:if test="${not empty values[7]}">
	    , ${values[7]} <%--State--%>
	</c:if>
	<c:if test="${not empty values[8]}">
	    , ${values[8]} <%--Country--%>
	</c:if>
    </font>
</c:if>
