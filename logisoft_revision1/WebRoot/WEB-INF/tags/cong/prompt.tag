<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@attribute name="text" required="true"%>
<%@attribute name="type"%>
<%@attribute name="onclick"%>
<c:if test="${empty type}">
    <c:set var="type" value="success"/>
</c:if>

<c:if test="${not empty text}">
    <div class="prompt" onclick="${onclick}">
        <label class="${type}">${text}</label>
    </div>
</c:if>
