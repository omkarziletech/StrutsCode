<%-- 
    Document   : exportsVoyageNotification
    Created on : Mar 17, 2015, 3:36:51 PM
    Author     : aravindhan.v
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="init.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/lclExportNotification.js"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Export Notification</title>
    </head>
    <html:form action="/exportNotification" styleId="lclExportNotiFicationForm">
        <input type="hidden" id="buttonValue" name="buttonValue"/>
        <input type="hidden" id="methodName" name="methodName"/>
        <input type="hidden" id="notificationId" name="notificationId"/>
        <br>
        <table width="100%" border="0" cellpadding="1" cellspacing="1" class="tableBorderNew" align="center">
            <tr class="tableHeadingNew"><td>Search Criteria</td></tr>
            <tr>
                <td>
                    <div id="notification" style="margin-top: 2%;">
                        <table width="100%" border="0" cellpadding="1" cellspacing="1" align="center">
                            <tr>
                                <td class="style2" width="9%"></td>
                                <td class="style2" style="text-align:right">VoyageNo</td><td>
                                    <cong:autocompletor id="voyageNo" name="voyageNo" template="one" fields="headerId" query="VOYAGE_SCHEDULE_NO"
                                                        styleClass="mandatory" width="100" container="NULL" params='E' shouldMatch="true"
                                                        callback="getunitNo();" value="${exportNotiFicationForm.voyageNo}"/>
                                    <cong:hidden id="headerId" name="headerId" value="${exportNotiFicationForm.headerId}"/>
                                </td><td class="style2">
                                    UnitNo
                                    <cong:autocompletor id="unitNo" name="unitNo" template="one"  fields="unitId"  query="UNIT_NO" paramFields="voyageNoHiddenId"
                                                        callback=""   width="100" container="NULL" shouldMatch="true"  value=""/>
                                    <cong:hidden id="unitId" name="unitId"/>
                                    <input type="hidden" id="voyageNoHiddenId" name="voyageNoHiddenId" value="${exportNotiFicationForm.headerId}"/>
                                </td>
                            </tr>

                            <table width="100%" border="0" cellpadding="1" cellspacing="1" align="center">
                                <tr>
                                    <td>
                                        <input type="button" style="margin-left:35%" class="button-style1" id="clear" value="Search" onClick="searchNotificationList('displayNotificationList','mainScreenSearch');">
                                        <input type="button" style="margin-left:5%" class="button-style1" id="save" value="Add New" onclick="addNew('${path}');" >
                                    </td>
                                </tr>
                            </table>

                        </table>
                    </div>
                </td>
            </tr>
        </table>
        <br/>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
            <tr class="tableHeadingNew">
                <td width="30%">Search Notification Results</td>
        </table>
        <table width="100%" border="1" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
            <c:if test="${not empty notificationList}">
                <tr>
                    <td><div id="unassignedContainerdiv" style="width:100%;">
                            <display:table name="${notificationList}" class="dataTable" pagesize="20"  id="notificationList" sort="list" requestURI="/exportNotification.do">
                                <display:column title="Voyage#" headerClass="sortable" sortable="true" style="text-transform:uppercase;">${notificationList.voyageNo}</display:column>
                                <display:column title="Unit No" headerClass="sortable" sortable="true" style="text-transform:uppercase;">${notificationList.unitNo}</display:column>
                                <display:column title="Content" headerClass="sortable" sortable="true" style="text-transform:uppercase;">${notificationList.remarks}</display:column>
                                <display:column title="Entered By" headerClass="sortable" sortable="true" style="text-transform:uppercase;">${notificationList.userName}</display:column>
                                <display:column title="Action" >
                                    <img src="${path}/img/icons/search_over.gif" id="edit"  width="18" height="18" onclick="viewReport('${path}','${notificationList.notificationId}','viewNotificationReport');"/>
                                    <img src="${path}/images/error.png" width="16" height="16" onclick="deleteNotification('deleteNotification','${notificationList.notificationId}','${notificationList.headerId}','${notificationList.unitId}')"/>
                                </display:column>
                            </display:table>
                        </div></td></tr>
            </table>
        </c:if>
    </html:form>
</html>


