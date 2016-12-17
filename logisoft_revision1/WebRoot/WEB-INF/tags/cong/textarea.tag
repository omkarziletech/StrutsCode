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
<%@attribute name="labeltitle"%>
<%@attribute name="styleClass"%>
<%@attribute name="readOnly" type="java.lang.Boolean"%>
<%@attribute name="disabled"%>
<%@attribute name="id"%>
<%@attribute name="validate"%>
<%@attribute name="table"%>
<%@attribute name="column"%>
<%@attribute name="container"%>
<%@attribute name="tooltip"%>
<%@attribute name="error_message"%>
<%@attribute name="URL"%>
<%@attribute name="rows"%>
<%@attribute name="cols"%>
<%@attribute name="style"%>
<%@attribute name="onchange"%>
<%@attribute name="onclick"%>
<%@attribute name="onkeypress"%>
<%@attribute name="tabindex"%>
<%@attribute name="onkeyup" %>
<%@attribute name="onkeydown" %>
<c:set var="tabIndex" value="0"/>
    <c:if test="${readOnly}">
        <c:set var="tabIndex" value="-1"/>
    </c:if>
<html:textarea  property="${name}"   styleId="${id}" style="${style}" styleClass="${styleClass} ${readOnlyClass}" readonly="${readOnly}" value="${value}" rows="${rows}" cols="${cols}" disabled="${disabled}" onclick="${onclick}" onkeypress="${onkeypress}"  onchange="${onchange} " tabindex="${tabIndex}" onkeydown="${onkeydown}" onkeyup="${onkeyup}"/>