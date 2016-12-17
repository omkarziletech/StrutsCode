<%-- 
    Document   : ssMasterDisputedSearch
    Created on : Sep 22, 2015, 7:31:06 PM
    Author     : Mei
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../init.jsp"%>
<%@include file="../../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
<script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
<script type="text/javascript" src='<c:url value="/jsps/LCL/js/ssMasterBl/ssMasterBl.js"></c:url>'></script>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SS MASTER DISPUTE For EXPORT</title>
    </head>
    <body>
        <cong:form action="/lcl/ssMasterDisputedBl" name="ssMasterBlForm" id="ssMasterBlForm">
            <cong:hidden name='methodName' id='methodName'/>
            <div id="pane" style="overflow: auto;">
                <table width="100%" border="0" cellpadding="1" cellspacing="1" class="tableBorderNew" align="center">
                    <tr class="tableHeadingNew">
                        <td colspan="8">Search Criteria</td>
                    </tr>
                    <tr><td colspan="8"><br/></td></tr>
                    <tr>
                        <td class ="textlabelsBoldforlcl">Voyage Number</td>
                        <td><cong:text  name='voyageNumber' id='voyageNumber'/></td>
                        <td class ="textlabelsBoldforlcl">Origin</td>
                        <td>
                            <cong:autocompletor id="origin" name="origin" template="one" fields="NULL,NULL,NULL,originId" position="right"
                                                query="EXPORT_CONCAT_PORTS"  value="${ssMasterBlForm.origin}" styleClass="textlabelsLclBoldForMainScreenTextBox "  width="250" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                            <cong:hidden id="originId" name="originId"/>
                        </td>
                        <td class ="textlabelsBoldforlcl">Destination</td>
                        <td>
                            <cong:autocompletor id="destination" name="destination" template="one" fields="NULL,NULL,NULL,destinationId" position="right"
                                                query="CONCAT_PORT_NAME"  styleClass="textlabelsLclBoldForMainScreenTextBox "  width="250" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                            <cong:hidden id="destinationId" name="destinationId"/>
                        </td>
                        <td class ="textlabelsBoldforlcl">POL</td>
                        <td>
                            <cong:autocompletor id="pol" name="pol" template="one" fields="NULL,NULL,NULL,originId" position="right"
                                                query="EXPORT_CONCAT_PORTS"  styleClass="textlabelsLclBoldForMainScreenTextBox"  width="250" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                            <cong:hidden id="polId" name="polId"/>
                        </td>
                    </tr>
                    <tr>
                        <td class ="textlabelsBoldforlcl">POD</td>
                        <td>
                            <cong:autocompletor id="pod" name="pod" template="one" fields="NULL,NULL,NULL,destinationId" position="right"
                                                query="CONCAT_PORT_NAME"  styleClass="textlabelsLclBoldForMainScreenTextBox"  width="250" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                            <cong:hidden id="podId" name="podId"/>
                        </td>
                        <td class ="textlabelsBoldforlcl">SSL</td>
                        <td>
                            <cong:autocompletor  name="ssl" styleClass="textlabelsLclBoldForMainScreenTextBox"  position="right" id="ssl" fields="sslAccountNo"
                                                 shouldMatch="true" width="600" query="EXPORT_SSLINE" template="tradingPartner" container="null" scrollHeight="300px"/>
                            <cong:hidden id="sslAccountNo" name="sslAccountNo" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                        </td>
                        <td class ="textlabelsBoldforlcl">ETA</td>
                        <td><cong:calendarNew styleClass="textbox" id="eta" name="eta"/></td>
                        <td class ="textlabelsBoldforlcl">ETD</td>
                        <td><cong:calendarNew styleClass="textbox" id="etd" name="etd"/></td>
                    </tr>
                    <tr>
                        <td class ="textlabelsBoldforlcl">SSL BL</td>
                        <td>
                            <html:select property="sslBlPrepaid" styleId="sslBlPrepaid" styleClass="dropdown" style="width: 128px;">
                                <html:option value="">Select</html:option>
                                <html:option value="P">Prepaid</html:option>
                                <html:option value="C">Collect</html:option>
                            </html:select>
                        </td>
                    </tr>
                    <tr><td colspan="8"><br/></td></tr>
                    <tr>
                        <td colspan="8" class="align-center">
                            <input type="button" value="Search" class="button" onclick="search();"/>
                            <input type="button" value="Reset" class="button" onclick="resetAll();"/>
                        </td>
                    </tr>
                    <tr><td colspan="8"><br/></td></tr>
                </table>
                <table width="100%" border="1" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center" id="masterResult">
                    <tr class="tableHeadingNew">
                        <td colspan="8"> Master BL List</td>
                    </tr>
                    <tr>
                        <td colspan="8">
                            <div id="results">
                                <%@include file="/jsps/LCL/ssMasterDisputed/ssMasterDisputedResult.jsp" %>   
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </cong:form>
    </body>
</html>
