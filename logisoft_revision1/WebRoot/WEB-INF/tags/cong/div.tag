<%-- 
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : sunil
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="styleClass"%>
<%@attribute name="id"%>
<%@attribute name="style"%>
<%@attribute name="onclick"%>

<div class="${styleClass}"  id="${id}" onclick="${onclick}" style="${style}">
    <jsp:doBody/>
</div>  
