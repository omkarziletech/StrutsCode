<%-- 
    Document   : applicationFooter
    Created on : Nov 7, 2011, 4:49:13 PM
    Author     : Thamizh
--%>
<%@taglib uri="/WEB-INF/tlds/dao.tld" prefix="dao"%>

<div id="footer-container">
    <div id="version-info">Version :${dao:getProperty('application.version')}</div>
    <span class="copy"> 
        ${dao:getProperty('application.fclBl.print.companyFullName')} - © Copyright  ${currentYear} : Logiware Inc
    </span>
    <div id="power-info"></div>
</div>