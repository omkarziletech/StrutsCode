<%-- 
    Document   : hiddenFields
    Created on : Feb 27, 2014, 6:44:13 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<html:hidden property="action" styleId="action"/>
<span class="error italic">${error}</span>
<span class="message italic">${message}</span>