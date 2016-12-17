<%-- 
    Document   : hiddenFields
    Created on : Jun 3, 2014, 0:28:04 PM
    Author     : Lakshmi Narayanan
--%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<html:hidden property="action" styleId="action"/>
<html:hidden property="limit" styleId="limit"/>
<html:hidden property="sortBy" styleId="sortBy"/>
<html:hidden property="orderBy" styleId="orderBy"/>
<html:hidden property="selectedPage" styleId="selectedPage"/>
<html:hidden property="toggled" styleId="toggled"/>
<span class="error italic">${error}</span>
<span class="message italic">${message}</span>