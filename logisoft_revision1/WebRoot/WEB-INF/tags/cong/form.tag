<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@attribute name="action"  required="true"%>
<%@attribute name="name" required="true" %>
<%@attribute name="styleClass"%>
<%@attribute name="enctype" %>
<%@attribute name="method" %>
<%@attribute name="target" %>
<%@attribute name="id" %>
<c:if test="${not empty name}">
    <c:set var="form" value="${name}" scope="request"/>
</c:if>
<c:if test="${empty method}">
    <c:set var="method" value="POST" scope="request"/>
</c:if>

<c:if test="${empty id}">
    <c:set var="id" value="${name}"/>
</c:if>

<c:choose>
    <c:when test="${not empty enctype}">
        <html:form styleId="${id}" method="${method}" action="${action}" enctype="${enctype}" target="${target}" styleClass="${styleClass}" scope="request">
            <jsp:doBody/>
        </html:form>
    </c:when>
    <c:otherwise>
        <html:form styleId="${id}" method="${method}" action="${action}" styleClass="${styleClass}" scope="request">
            <jsp:doBody/>
        </html:form>
    </c:otherwise>
</c:choose>

