<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="init.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <%@include file="/jsps/preloader.jsp" %>
        <%@include file="/taglib.jsp" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <script type="text/javascript" src='<c:url value="/jsps/LCL/js/export/voyage/searchScreen.js"></c:url>'></script>
        <title>Voyage Search</title>
    </head>
    <body>
        <cong:form action="/lclUnitsSchedule" name="lclUnitsScheduleForm" id="lclUnitsScheduleForm">
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="columnName" name="columnName"/>
            <cong:hidden id="voyageId" name="voyageId"/>
            <cong:hidden id="sortBy" name="sortBy"/>
            <cong:hidden id="serviceType" name="serviceType" value="E"/>
            <html:hidden property="unitId" styleId="unitId"/>
            <html:hidden property="unitMultiFlag" styleId="unitMultiFlag"/>
            <cong:hidden id="polId" name="polId"  value="${lclUnitsScheduleForm.polId}"/>
            <cong:hidden id="podName" name="podName"  value="${lclUnitsScheduleForm.podName}"/>
            <cong:hidden id="polName" name="polName"  value="${lclUnitsScheduleForm.polName}"/>
            <cong:hidden id="podId" name="podId" value="${lclUnitsScheduleForm.podId}"/>
            <html:hidden property="bookingMultiFlag" styleId="bookingMultiFlag"/>
            <input type="hidden" id="bypassRelayCheck" value="${roleDuty.bypassRelayCheck}"/>
            <br>
            <div id="pane" style="overflow: auto;">
                <table width="100%" border="0" cellpadding="1" cellspacing="1" class="tableBorderNew" align="center">
                    <tr class="tableHeadingNew"><td>Search Criteria</td></tr>
                    <tr><td>
                            <table width="100%" border="0" cellpadding="1" cellspacing="1"  align="center">
                                <tr><td colspan="6"></td></tr>
                                <tr><td colspan="6"></td></tr>
                                <tr>
                                    <td width="19%"></td>
                                    <td class="textlabelsBoldforlcl" align="right" width="4%">Select</td>
                                    <td align="left">
                                        <html:select  property="filterByChanges" styleId="filterByChanges" styleClass="smallDropDown"
                                                      onchange="resetAllFields(true);" name="lclUnitsScheduleForm" tabindex="0">
                                            <html:option value="lclExport">USA LCL Exports</html:option>
                                            <html:option value="lclDomestic">USA Domestic Inland</html:option>
                                            <html:option value="lclCfcl">CFCL</html:option>
                                            <html:option value="unassignedContainers">Unassigned Containers</html:option>
                                            <html:option value="currentProcess">All Currently in Process</html:option>
                                        </html:select>
                                        <c:if test="${showUnCompleteUnits}">
                                            <input type="checkbox" name="showUnCompleteUnits" id="showUnCompleteUnits" ${lclUnitsScheduleForm.showUnCompleteUnits ? 'checked' : ''} 
                                                   style="vertical-align: middle" title="Show Incomplete Units" onclick="searchUnCompleteUnits();"/>
                                        </c:if>
                                        <input type="checkbox" name="isLclContainerSize" id="isLclContainerSize" ${lclUnitsScheduleForm.isLclContainerSize ? 'checked' : ''}
                                               style="vertical-align: middle" title="Show LCL Units" onclick="searchContainerSize();"/>
                                    </td>
                                    <td class="cfclAgentlabel textlabelsBoldforlcl">CFCL Agent</td>
                                    <td class="cfclAgenttextBox">
                                        <cong:autocompletor  name="cfclAcctName" id="cfclAcctName" fields="cfclAcctNo" shouldMatch="true"
                                                             callback="searchByCfclAcct('search')" width="500" query="CFCL_ACCOUNT" styleClass="textuppercaseLetter"
                                                             template="tradingPartner" container="null" scrollHeight="300px" value="${lclUnitsScheduleForm.cfclAcctName}"/>
                                        <cong:text id="cfclAcctNo" name="cfclAcctNo" value="${lclUnitsScheduleForm.cfclAcctNo}" style="width:70px" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                                    </td>                                                                                                            
                                </tr>
                                <input type="hidden" name="unitPooId" id="unitPooId"/>
                                <input type="hidden" name="unitPoo" id="unitPoo"/>
                                <input type="hidden" name="unitFdId" id="unitFdId"/>
                                <input type="hidden" name="unitFd" id="unitFd"/>
                                <input type="hidden" name="unitVoyageNo" id="unitVoyageNo"/>
                                <tr><td colspan="3"></td></tr>
                                <tr id="usaLclExports">
                                    <td class="style2" width="10%"></td>
                                    <td class="textlabelsBoldforlcl">Origin</td>
                                    <td >
                                        <cong:autocompletor id="origin" name="origin" template="one" fields="NULL,NULL,NULL,portOfOriginId" query="RELAYNAME"
                                                            styleClass="textuppercaseLetter mandatory pol" width="250" container="NULL"  shouldMatch="true"
                                                            callback="showBtn();searchVoyage('search')"
                                                            value="${lclUnitsScheduleForm.origin}" /></td>
                                        <cong:hidden id="portOfOriginId" name="portOfOriginId" value="${lclUnitsScheduleForm.portOfOriginId}"/>
                                    <td class="textlabelsBoldforlcl">Destination</td>
                                    <td>
                                        <cong:autocompletor id="destination" name="destination" template="one"  fields="NULL,NULL,unlocationCode,finalDestinationId"
                                                            query="CONCAT_RELAY_NAME_FD" callback="showBtn();searchVoyage('search')" styleClass="textuppercaseLetter mandatory pod"
                                                            width="350" container="NULL" shouldMatch="true"
                                                            value="${lclUnitsScheduleForm.destination}"/>
                                        <cong:hidden id="finalDestinationId" name="finalDestinationId" value="${lclUnitsScheduleForm.finalDestinationId}"/>
                                    </td>
                                    <td class="textlabelsBoldforlcl">Search by Voyage#</td>
                                    <td>
                                        <cong:autocompletor id="voyageNo" name="voyageNo" template="one" fields="unitPooId,unitPoo,unitFdId,unitFd" scrollHeight="300px"
                                                            query="EXP_VOYAGE_SEARCH" width="150" container="NULL"  shouldMatch="true"
                                                            callback="searchByVoyage()" value="${lclUnitsScheduleForm.voyageNo}"/>
                                    </td>
                                    <td class="textlabelsBoldforlcl">Search by Unit#</td>
                                    <td>
                                        <cong:autocompletor id="unitNo" name="unitNo" template="one" fields="unitId,unitMultiFlag,unitPooId,unitPoo,unitFdId,unitFd,unitVoyageNo"
                                                            query="EXP_UNIT_SEARCH" width="150" container="NULL"  shouldMatch="true" 
                                                            scrollHeight="300px" callback="searchByUnit()" value="${lclUnitsScheduleForm.unitNo}"/>
                                    </td>
                                     <td class="textlabelsBoldforlcl">Search by SSL Booking#</td>
                                    <td>
                                        <cong:autocompletor id="bookingNo" name="bookingNo" template="one" fields="bookingMultiFlag,unitPooId,unitPoo,unitFdId,unitFd,unitVoyageNo"
                                                            query="EXP_BOOKING_NO_SEARCH" width="150" container="NULL"  shouldMatch="true"
                                                            scrollHeight="300px" callback="searchByBookingNo()" value="${lclUnitsScheduleForm.bookingNo}"/>
                                    </td>
                                </tr>
                                <tr id="UnassignedContainers">
                                    <td class="style2" width="9%"></td>
                                    <td class="textlabelsBoldforlcl">Warehouse Name</td>

                                    <td>
                                        <cong:autocompletor name="warehouseNo" id="warehouseNo" width="400" scrollHeight="200px" styleClass="mandatory smallTextlabelsBoldForTextBox "
                                                            query="WAREHOUSE_WITH_WHSENAME" fields="warehouseName,warehouseId"
                                                            container="NULL" template="three" shouldMatch="true" callback="searchUnAssingedUnit('searchByUnAssignUnit');"/>
                                        <cong:text name="warehouseName" id="warehouseName" styleClass="textLCLuppercase"/>
                                        <cong:hidden name="warehouseId" id="warehouseId"/>
                                    <td class="style2" width="15%"></td>
                                    <td class="textlabelsBoldforlcl">Disposition</td>
                                    <td>
                                        <cong:autocompletor id="dispositionCode" name="dispositionCode" template="one"  fields="NULL,dispositionId"
                                                            query="DISPOSITION"  styleClass="mandatory readonly"  readOnly="true" width="350" container="NULL" value=""
                                                            shouldMatch="true" scrollHeight="200px" callback="searchUnAssingedUnit('searchByUnAssignUnit');"/>
                                        <cong:hidden name="dispositionId" id="dispositionId" />
                                    </td>
                                    <td class="style2" width="9%"></td>
                                </tr>
                                <tr>
                                    <td colspan="6" align="center">
                                        <input type="button" class="buttonStyleNew" id="viewAll" value='View All' onclick="searchByViewAll();"/>
                                        <input type="button" class="buttonStyleNew" id="addunit" value='Add Unit' onclick="addUnits('${path}');"/>
                                        <input type="button" class="buttonStyleNew" id="addnew" value='Add New' onclick="addNewVoyage('addVoyage');"/>
                                        <input type="button" class="buttonStyleNew" id="restart" value='Restart' onclick="resetAllFields(false);"/>
                                        <input type="button" class="buttonStyleNew" id="releaseReport" value='All Release Report' onclick="releaseReports();"/>
                                        <input type="button" class="buttonStyleNew" id="refreshBtn" value='Refresh' onclick="searchVoyage('');"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <br>

                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                    <tr class="tableHeadingNew">
                        <td width="30%">Search Results&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <span class="uncompletedUnits greenFontBold">${lclUnitsScheduleForm.showUnCompleteUnits ? 'Units not Completed': ''}</span></td>
                            <c:choose>
                                <c:when test="${lclUnitsScheduleForm.filterByChanges eq 'unassignedContainers'}">
                                <td width="2%" class="blackBoldArempty">WareHouse Name:</td>
                                <td width="20%" id="polNameLabel" class="greenFontBold">${lclUnitsScheduleForm.warehouseName}</td>
                                <td width="2%" class="blackBoldArempty">Disposition:</td>
                                <td id="podNameLabel" class="greenFontBold">${lclUnitsScheduleForm.dispositionCode}</td>
                            </c:when>
                            <c:otherwise>
                                <td width="2%" class="blackBoldArempty">POL:</td>
                                <td width="20%" class="greenFontBold">
                                    <label id="polNameLabel">${lclUnitsScheduleForm.polName}</label>
                                </td>
                                <td width="2%" class="blackBoldArempty">POD:</td>
                                <td id="podNameLabel" class="greenFontBold">${lclUnitsScheduleForm.podName}</td>
                            </c:otherwise>
                        </c:choose>
                        <td align="right" style="width:10%" class="blackBoldArempty">Limit
                            <html:select property="limit" styleId="limit" styleClass="dropdown"
                                         onchange="searchLimit();" value="${lclUnitsScheduleForm.limit}">
                                <html:option value="25" >25</html:option>
                                <html:option value="50" >50</html:option>
                                <html:option value="100" >100</html:option>
                                <html:option value="250" >250</html:option>
                            </html:select>
                        </td>
                    </tr>
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew"
                       align="center" id="voyagetable">
                    <tr>
                        <td>
                            <c:choose>
                                <c:when test="${not empty viewAllVoyageList}"><%-- View All --%>
                                    <%@include file="/jsps/LCL/export/voyage/search/originVoyageResult.jsp" %>
                                </c:when>
                                <c:when test="${not empty unassignedContainerList}">
                                    <%@include file="/jsps/LCL/export/voyage/search/unassignedContainerResult.jsp" %>
                                </c:when>
                                <c:when test="${not empty uncompleteunitsList}">
                                    <%@include file="/jsps/LCL/export/voyage/search/uncompleteUnitsResult.jsp" %>
                                </c:when>
                                <c:when test="${not empty voyageList}"><%-- Current Process --%>
                                    <%@include file="/jsps/LCL/export/voyage/search/voyageResults.jsp" %>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${not empty lclVoyageList}">
                                        <%@include file="/jsps/LCL/export/voyage/search/voyageSearchResult.jsp" %>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </table>
            </div>
        </cong:form>
    </body>
</html>
