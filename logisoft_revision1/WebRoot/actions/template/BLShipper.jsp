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
<c:if test="${values[12] == 'Forwarder'}">
    - (
    <font color="red" style="font-weight: bold">
        ${values[12]}<%--type--%>
    </font>)
</c:if>
    <c:if test="${not empty values[15] && values[15]!=null}">
        ,<font color="008000" style="font-weight: bold">
            SP=${values[15]}<%--type--%>
        </font>,
    </c:if>
<c:if test="${values[2]=='S'}">
    <font color="008000" style="font-weight: bold">
        ${values[17]}
    </font>
</c:if>
<c:if test="${values[2]=='C'}">
    <font color="008000" style="font-weight: bold">
        ${values[18]}
    </font>
</c:if>
<c:if test="${values[2]=='V'}">
    <font color="008000" style="font-weight: bold">
        ${values[17]}
    </font>
</c:if>
<br/>
<span color="#000000">
    ${values[7]}<%--address --%>
</span>
<c:if test="${not empty values[8]}">
    ,<span color="#000000" style="font-weight: bold">
        ${values[8]}<%--City --%>
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
    </span>;
</c:if>
