<%-- 
    Document   : hiddenFields
    Created on : Oct 6, 2013, 6:48:31 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<html:hidden property="action" styleId="action"/>
<html:hidden property="limit" styleId="limit"/>
<html:hidden property="sortBy" styleId="sortBy"/>
<html:hidden property="orderBy" styleId="orderBy"/>
<html:hidden property="selectedPage" styleId="selectedPage"/>
<html:hidden property="toggled" styleId="toggled"/>
<html:hidden property="emailIds" styleId="emailIds"/>
<html:hidden property="excludeIds" styleId="excludeIds"/>
<input type="hidden" name="userEmailAddress" id="userEmailAddress" value="${loginuser.email}"/>