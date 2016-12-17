<%--
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : sunil
--%>
<%@taglib tagdir="/WEB-INF/tags/cong" prefix="cong" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@tag description="Checkbox implementation" pageEncoding="UTF-8"%>
<%@attribute name="name" required="true"%>
<%@attribute name="value"%>
<%@attribute name="label"%>
<%@attribute name="styleClass"%>
<%@attribute name="container" type="java.lang.Boolean"%>
<%@attribute name="id"%>
<%@attribute name="disabled"%>
<%@attribute name="tooltip"%>
<%@attribute name="title"%>
<%@attribute name="alt"%>
<%@attribute name="onclick"%>
<%@attribute name="onmouseover"%>
<%@attribute name="onmouseout"%>

<c:catch var="exception">
    <c:if test="${id == null}" var="_var">
        <c:set var="id" value="${name}"/>
    </c:if>

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
    <c:choose>
        <c:when test="${not empty form}">
            <html:checkbox property="${name}" onmouseover="${onmouseover}" onmouseout="${onmouseout}" title="${title}" name="${form}" alt="${alt}" styleId="${id}" styleClass="${styleClass}" disabled="${disabled}" onclick="${onclick}"/>
            <html:hidden property="${name}" name="${form}" value="off"/>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${value}">
                    <c:set var="checked" value="checked"/>
                </c:when>
                <c:otherwise>
                    <c:set var="checked" value=""/>
                </c:otherwise>
            </c:choose>
            <input type="checkbox" name="${name}" onmouseover="${onmouseover}" onmouseout="${onmouseout}" id="${id}" alt="${alt}" title="${title}" class="${styleClass}" ${disabled}  onclick="${onclick}" ${checked}/>
            <input type="hidden" name="${name}" value="off"/>
        </c:otherwise>
    </c:choose>

    <jsp:doBody/>
    <c:if test="${container}">
        </${container}>
    </c:if>  
</c:catch>
<cong:prompt text="${exception}" type="warning"/>
