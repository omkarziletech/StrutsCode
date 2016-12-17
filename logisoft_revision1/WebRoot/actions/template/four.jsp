<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<font color="#093ba1">
    ${values[0]}
</font>
<c:if test="${not empty values[1]}">
<-->
<font color="#8f0c02">
    ${values[1]}
</font>
</c:if>
<br/>
<c:if test="${not empty values[2]}">
<font color="green">
    ${values[2]}
</font>
</c:if>
<c:if test="${not empty values[3]}">
<-->
<font color="blue">
    ${values[3]}
</font>
</c:if>