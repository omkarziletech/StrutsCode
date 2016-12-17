<%--
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : sunil
--%>
<%@taglib tagdir="/WEB-INF/tags/cong" prefix="cong" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@attribute name="name" required="true"%>
<%@attribute name="id"%>
<%@attribute name="value"%>
<%@attribute name="styleClass"%>
<c:set var="styleClass" value="${styleClass} accessible"/>
<c:if test="${empty id}">
    <c:set var="id" value="${name}"/>
</c:if>
<c:catch var="exception">
    <c:choose>
        <c:when test="${not empty value}">
            <c:choose>
                <c:when test="${form != null}">
                    <html:hidden property="${name}" value="${value}"  name="${form}" styleId="${id}" styleClass="${styleClass}"/>
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="${name}" id="${id}" class="${styleClass}" value="${value}"/>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${form != null}">
                    <html:hidden property="${name}" name="${form}" styleId="${id}" styleClass="${styleClass}"/>
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="${name}" id="${id}" class="${styleClass}"/>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
</c:catch>
<cong:prompt type="exception" text="${exception}"/>
