<%-- 
    Document   : default
    Created on : Aug 13, 2010, 5:17:34 PM
    Author     : Lakshmi Naryanan
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<span color="#000000" style="font-weight: bold">
    ${values[0]}<--> <%--account_name --%>
</span>

<font color="#0000FF" style="font-weight: bold">
    ${values[1]} <%--account_number --%>
</font>
<font color="red" style="font-weight: bold">
    ${values[2]}<%--type--%>
</font>
<c:if test="${not empty values[13]}">
    - (
    <font color="red" style="font-weight: bold">
        ${values[13]}<%--type--%>
    </font>)
</c:if>
    
<font color="008000" style="font-weight: bold">
    ${values[14]}
</font>

<br/>
<span color="#000000">
    ${values[7]}<%--address --%>
</span>
<c:if test="${not empty values[8]}">
    ,<span color="#000000" style="font-weight: bold">
        ${values[9]}<%--City --%>
    </span>
</c:if>
<c:if test="${not empty values[10]}">
    ,<span color="#000000" style="font-weight: bold">
        ${values[10]}<%--State --%>
    </span>
</c:if>
<c:if test="${not empty values[11]}">
    ,<span color="#000000" style="font-weight: bold">
        ${values[11]}<%--Zip --%>
    </span>
</c:if>
<c:if test="${not empty values[4]}">
    ,<span color="#000000" style="font-weight: bold">
        ${values[4]}<%--phone --%>
    </span>
</c:if>
<c:if test="${not empty values[5]}">
    ,<span color="#000000" style="font-weight: bold">
        ${values[5]}<%--fax--%>
    </span>;
</c:if>
