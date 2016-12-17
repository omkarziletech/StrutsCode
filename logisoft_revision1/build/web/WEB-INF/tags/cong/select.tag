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
<%@attribute name="onChange"%>

<c:if test="${id == null}" var="_var">
    <c:set var="id" value="${name}"/>
</c:if>

<c:if test="${not empty value}">
    <cong:prompt text="value is not implemented for select tag" type="warning"/>
</c:if>

<select property="${name}" name="${id}" style="${style}" class="smallDropDown ${styleClass} text"  id="${id}" onChange="${onChange}">
    <jsp:doBody/>
</select>

<c:if test="${not empty tooltip}">
    <cong:tooltip id="${id}" tooltip="${tooltip}"/>
</c:if>
