<%--
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : sunil
--%>
<%@taglib tagdir="/WEB-INF/tags/cong" prefix="cong" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@attribute name="name" required="true"%>
<%@attribute name="value"%>
<%@attribute name="label"%>
<%@attribute name="styleClass"%>
<%@attribute name="readOnly" type="java.lang.Boolean"%>
<%@attribute name="id"%>
<%@attribute name="validate"%>
<%@attribute name="tooltip"%>
<%@attribute name="container"%>
<%@attribute name="error_message"%>
<%@attribute name="disabled"%>
<%@attribute name="maxlength"%>
<%@attribute name="onclick"%>
<%@attribute name="onkeyup"%>
<%@attribute name="callback"%>

<c:catch var="exception">
    <c:if test="${id == null}" var="_var">
        <c:set var="id" value="${name}"/>
    </c:if>
    <c:if test="${container == null}">
        <c:set var="container" value="td"/>
    </c:if>
    <c:if test="${readOnly}">
        <c:set var="readOnlyClass" value="readOnly"/>
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
        <c:when test="${form != null}">
            <c:choose>
                <c:when test="${value != null}">
                    <html:password property="${name}" name="${form}" styleId="${id}" styleClass="text ${styleClass} ${readOnlyClass}" readonly="${readOnly}" value="${value}"  disabled="${disabled}"
                               maxlength="${maxlength}"
                               onkeyup="${onkeyup}"
                               onclick="${onclick}"/>
                </c:when>
                <c:otherwise>
                    <html:password property="${name}" name="${form}" styleId="${id}" styleClass="text ${styleClass} ${readOnlyClass}" readonly="${readOnly}"  disabled="${disabled}"
                               maxlength="${maxlength}"
                               onkeyup="${onkeyup}"
                               onclick="${onclick}"/>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <html:password property="${name}" styleId="${id}" styleClass="text ${styleClass} ${readOnlyClass}"  readonly="${readOnly}"  value="${value}"  disabled="${disabled}"
                       maxlength="${maxlength}"
                       onkeyup="${onkeyup}"
                       onclick="${onclick}"/>
        </c:otherwise>
    </c:choose>
        
    <jsp:doBody/>
    <c:if test="${container != 'NULL'}">
        </${container}>
    </c:if>
</c:catch>
<c:if test="${not empty tooltip}">
    <cong:tooltip id="${id}" tooltip="${tooltip}"/>
</c:if>
<cong:prompt type="exception" text="${exception}"/>
