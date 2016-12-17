<%--
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : sunil
--%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib tagdir="/WEB-INF/tags/cong/" prefix="cong"%>
<%@tag description="Checkbox implementation" pageEncoding="UTF-8"%>
<%@attribute name="name" required="true"%>
<%@attribute name="value"%>
<%@attribute name="label"%>
<%@attribute name="styleClass"%>
<%@attribute name="container" type="java.lang.Boolean"%>
<%@attribute name="id"%>
<%@attribute name="disabled"%>
<%@attribute name="checked"%>
<%@attribute name="onclick"%>
<%@attribute name="tooltip"%>
<%@attribute name="title"%>

<c:if test="${empty id}">
    <c:set var="id" value="${name}"/>
</c:if>
<c:choose>
    <c:when test="${form == null}">
        <input type="radio" name="${name}" ${checked} id="${styleId}" class="${styleClass}" title="${title}" onclick="${onclick}" value="${value}"/>
    </c:when>
    <c:otherwise>
        <c:if test="${container == null}">
            <c:set var="container" value="td"/>
        </c:if>
        <c:if test="${container != 'NULL'}">
            <${container}>
        </c:if>
        <c:if test="${label != null}">
            <label>
                ${label}
            </label>
        </c:if>
        <html:radio  value="${value}" property="${name}" styleId="${id}" styleClass="${styleClass}" disabled="${disabled}" onclick="${onclick}" title="${title}"/>
        <jsp:doBody/>
    </c:otherwise>
</c:choose>
<c:if test="${container}">
    </${container}>
</c:if>

<c:if test="${not empty tooltip}">
    <cong:tooltip id="${id}" tooltip="${tooltip}"/>
</c:if>
