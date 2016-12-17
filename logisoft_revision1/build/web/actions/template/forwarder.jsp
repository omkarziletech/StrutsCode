<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<font color="#093ba1" style="font-weight: bold">
    ${values[0]}<--> <%--account_name --%>
</font>
<font color="#8f0c02" style="font-weight: bold">
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
<c:if test="${values[2]=='S'}">
    <font color="008000" style="font-weight: bold">
        ${values[13]}
    </font>
</c:if>
<c:if test="${values[2]=='C'}">
    <font color="008000" style="font-weight: bold">
        ${values[14]}
    </font>
</c:if>
<c:if test="${values[2]=='V'}">
    <font color="008000" style="font-weight: bold">
        ${values[13]}
    </font>
</c:if>
<br/>
<font color="#585858" style="font-size: 8px !important;">
    ${values[7]}<%--address --%>
</font>
<c:if test="${not empty values[8]}">
    ,<font color="#585858" style="font-size: 8px !important;">
        ${values[8]}<%--City --%>
    </font>
</c:if>
<c:if test="${not empty values[10]}">
    ,<font color="#585858" style="font-size: 8px !important;">
        ${values[10]}<%--State --%>
    </font>
</c:if>
<c:if test="${not empty values[11]}">
    ,<font color="#585858" style="font-size: 8px !important;">
        ${values[11]}<%--Zip --%>
    </font>;
</c:if>