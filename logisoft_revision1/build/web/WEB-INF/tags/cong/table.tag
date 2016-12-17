<%-- 
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : sunil
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="styleClass"%>
<%@attribute name="caption"%>
<%@attribute name="cellpadding"%>
<%@attribute name="cellspacing"%>
<%@attribute name="width"%>
<%@attribute name="border" %>
<%@attribute name="style"%>
<%@attribute name="id"%>
<%@attribute name="align"%>
<c:if test="${empty cellpadding}">
    <c:set var="cellpadding" value="0"/>
</c:if>
<c:if test="${empty cellspacing}">
    <c:set var="cellspacing" value="0"/>
</c:if>
<c:if test="${empty border}">
    <c:set var="border" value="0"/>
</c:if>
<c:if test="${empty width}">
    <c:set var="width" value="100%"/>
</c:if>

<table align="${align}" class="${styleClass}" cellpadding="${cellpadding}" cellspacing="${cellspacing}"  id="${id}" style="${style}" border="${border}" width="${width}">
     <jsp:doBody/>
</table>   
