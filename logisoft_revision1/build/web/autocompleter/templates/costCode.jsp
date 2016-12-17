<%-- 
    Document   : costCode
    Created on : May 17, 2012, 6:02:41 PM
    Author     : Lakshmi Naryanan
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--Blue Screen Cost Code--%>
<font color="#093ba1">
    ${values[1]}
</font>
<-->
<%--Cost Code--%>
<font color="#8f0c02">
    ${values[0]}
</font>
<-->
<%--Shipment Type--%>
<font color="red">
    ${values[2]}
</font>
<%--Description--%>
<c:if test="${not empty values[3]}">
    <-->
    <font color="green">
	${values[3]}
    </font>
</c:if>