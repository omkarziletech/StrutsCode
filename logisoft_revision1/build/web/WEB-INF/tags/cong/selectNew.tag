<%--
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : Lakshmi Narayanan
Index and value is common for this tag
--%>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/cong/" prefix="cong"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@attribute name="name" required="true"%>
<%@attribute name="label"%>
<%@attribute name="value"%>
<%@attribute name="query"%>
<%@attribute name="id"%>
<%@attribute name="collections"%>
<%@attribute name="style"%>
<%@attribute name="styleClass"%>
<%@attribute name="container"%>
<%@attribute name="tooltip"%>
<%@attribute name="_form"%>
<%@attribute name="indexProperty"%>
<%@attribute name="valueProperty"%>

<c:if test="${id == null}" var="_var">
    <c:set var="id" value="${name}"/>
</c:if>
<c:if test="${container == null}">
    <c:set var="container" value="td"/>
</c:if>
<c:if test="${container != 'NULL'}">
    <${container}>
</c:if>
<c:if test="${not empty label}">
    <label>${label}</label>
</c:if>
<c:if test="${not empty value}">
    <cong:prompt text="value is not implemented for select tag" type="warning"/>
</c:if>

<c:if test="${empty form}">
    <c:set var="form" value="${_form}"/>
</c:if>

<html:select property="${name}" name="${form}" style="${style}" styleClass="${styleClass} text"  styleId="${id}">
    <c:choose>
        <c:when test="${not empty collections}">
            <c:if test="${empty valueProperty || empty indexProperty}">
                <cong:prompt type="info" text="valueProperty is required for selected tag"/>
            </c:if>
            <c:if test="${not empty valueProperty &&  not empty indexProperty}">
                <html:option value="">SELECT</html:option>
                <html:optionsCollection name="${collections}" label="${valueProperty}" value="${indexProperty}"/>
            </c:if>
        </c:when>
        <c:otherwise>
            <jsp:doBody/>
        </c:otherwise>
    </c:choose>
</html:select>
<c:if test="${container != 'NULL'}">
    </${container}>
</c:if>

<c:if test="${not empty tooltip}">
    <cong:tooltip id="${id}" tooltip="${tooltip}"/>
</c:if>
