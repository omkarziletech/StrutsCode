<%-- 
    Document   : reconcile
    Created on : Mar 28, 2012, 5:49:01 PM
    Author     : Lakshmi Naryanan
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<font color="#093ba1">
${values[0]} <%--GL Account--%>
</font>
<-->
<font color="#8f0c02">
${values[1]} <%--Bank Name--%>
</font>
<-->
<font color="green">
${values[2]}<%--Bank Account--%>
</font>
<c:if test="${fn:length(values)>4}">
    <br>
    <font color="#585858">
    ${values[3]}<%--Bank Address--%>
    </font>
</c:if>
