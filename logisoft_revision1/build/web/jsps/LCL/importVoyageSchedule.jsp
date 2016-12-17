<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/importVoyageSchedule.js"></cong:javascript>
<%@include file="/taglib.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Voyage Schedule</title>
    </head>
    <body  class="whitebackgrnd" id="pane">
        <cong:form action="/lclImportVoyage" name="lclUnitsScheduleForm" id="lclUnitsScheduleForm">
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="columnName" name="columnName"/>
            <cong:hidden id="loginUserSearchFlag" name="loginUserSearchFlag"/>
            <cong:hidden id="voyageId" name="voyageId" value="${lclUnitsScheduleForm.voyageId}"/>
            <cong:hidden id="serviceType" name="serviceType" value="I"/>
            <br>
            <table width="100%" border="0" cellpadding="1" cellspacing="1" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew"><td>Search Criteria</td></tr>
                <tr> <td>
                        <table width="100%" border="0" cellpadding="1" cellspacing="1" align="center">
                            <tr>
                                <td class="style2" width="9%"></td>
                                <td class="textlabelsBoldforlcl">Origin</td>
                                <td>
                                    <cong:autocompletor id="origin" name="origin" template="one"  fields="NULL,NULL,polCode,NULL,portOfOriginId" scrollHeight="300"
                                                        query="CONCAT_WITHOUT_US_COUNTRY" callback="searchVoyageDetails('searchVoyageResult')" styleClass="textuppercaseLetter mandatory"  width="350" container="NULL"/>
                                    <cong:hidden id="portOfOriginId" name="portOfOriginId" value="${lclUnitsScheduleForm.portOfOriginId}"/>
                                    <cong:hidden id="polCode" name="polCode" value="${lclUnitsScheduleForm.polCode}"/>
                                </td>
                                <td class="textlabelsBoldforlcl">Destination</td>
                                <td>
                                    <cong:autocompletor id="destination" name="destination" template="one" fields="NULL,NULL,NULL,NULL,finalDestinationId" query="CONCAT_WITH_US_STATE" styleClass="textuppercaseLetter mandatory" width="250"
                                                        container="NULL" callback="searchVoyageDetails('searchVoyageResult')" scrollHeight="300" />
                                    <cong:hidden id="finalDestinationId" name="finalDestinationId" />
                                </td>
                                <td  class="textlabelsBoldforlcl">Starting Voyage#</td>
                                <td>
                                    <cong:text name="voyageNo" id="voyageNo" onkeypress="checkForNumberOnly(this)" styleClass="textuppercaseLetter" onkeyup="clearValues();"/>
                                    <input type="button" class="button" id="addnew" value='Go' onclick="searchByVoyageNo('searchVoyageResult');"/></td>
                                <td class="textlabelsBoldforlcl">Billing Terminal</td>
                                <td>
                                    <cong:autocompletor name="billsTerminal" id="billsTerminal" width="400" scrollHeight="200px" container="NULL" position="left" callback="searchVoyageDetails('searchVoyageResult')"
                                                        styleClass="textuppercaseLetter" query="IMPORTTERMINAL" fields="NULL,billsTerminalNo" template="three"/>
                                    <cong:hidden name="billsTerminalNo" id="billsTerminalNo" />
                                </td>
                                <td class="textlabelsBoldforlcl">Owner</td>
                                <td>
                                    <cong:autocompletor name="loginName" id="loginName" width="400" scrollHeight="200px" container="NULL" position="left"
                                                        callback="searchVoyageDetails('searchVoyageResult')" styleClass="textuppercaseLetter" query="SALES_PERSON" fields="loginId" template="one"/>
                                    <cong:hidden name="loginId" id="loginId" />
                                    <input type="button" class="button" id="addnew" value='Go' onclick="searchByVoyOwner('searchVoyageResult');"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="style2" width="9%"></td>
                                <td align="right" class="textlabelsBoldforlcl">UnitNo#</td>
                                <td>
                                    <cong:text name="unitNo" id="unitNo"  styleClass="textuppercaseLetter" onchange="autoFormatUnitNumber(this)" />
                                    <input type="checkbox" name="unitNoCheck" id="unitNoCheck" onclick="autoFormatUnitNumber(this)"
                                                    style="vertical-align: middle;" title="Use Non-Standard Format" container="NULL" />
                                    <input type="button" class="button" 
                                           id="addnew" value='Go' onclick="searchUnitNo('searchVoyageResult');"/></td>
                                <td  align="right" class="textlabelsBoldforlcl">Master BL#</td>
                                <td>
                                    <cong:text name="masterBl" id="masterBl"  styleClass="textuppercaseLetter"/>
                                    <input type="button" class="button" id="addnew" value='Go' onclick="searchMasterBl('searchVoyageResult');"/></td>
                                <td class="textlabelsBoldforlcl">Agent#</td>
                                <td>
                                    <cong:autocompletor id="agentNo" name="agentNo" template="two" shouldMatch="true" fields="agentNo" query="IMPORT_ORIGIN_AGENT" styleClass="textuppercaseLetter" width="250"
                                                        container="NULL" scrollHeight="300" />
                                    <input type="button" class="button" value='Go' onclick="searchByAgentNo('searchVoyageResult');"/></td>

                                <td class="textlabelsBoldforlcl">Limit</td>
                                <td>
                                    <html:select property="limit" styleId="limit" styleClass="dropdown" onchange="searchByLimit('searchVoyageResult')" value="${lclUnitsScheduleForm.limit}" >
                                        <html:option value="25" >25</html:option>
                                        <html:option value="50" >50</html:option>
                                        <html:option value="100" >100</html:option>
                                        <html:option value="250" >250</html:option>
                                    </html:select>
                                </td>   
                                <td class="textlabelsBoldforlcl">Disposition</td>
                                <td>
                                    <cong:autocompletor name="dispositionCode" id="dispositionCode" width="400" scrollHeight="300px" container="NULL" position="left" styleClass="textuppercaseLetter"
                                                        callback="searchVoyageDetails('searchVoyageResult')" query="DISPOSITION" fields="NULL,dispositionId" template="two"/>
                                    <cong:hidden name="dispositionId" id="dispositionId" />
                                    <input type="button" class="button" id="dispo-search" value="Go" onclick="searchByDisposition();"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="12" align="center">
                                    <input type="button" class="button" id="addnew" value='Add New' onclick="createNewVoyage('addVoyage','${path}');"/>
                                    <input type="button" class="button" id="restart" value='Restart' onclick="resetAllFields();"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <br>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center" >
                <tr class="tableHeadingNew">
                    <td width="30%">Search Results</td>
                    <td width="2%" style="font-size: 12px;text-transform: uppercase;">POL:</td>
                    <td width="20%" id="polName" class="boldGreen">${lclUnitsScheduleForm.origin}</td>
                    <td width="2%" style="font-size: 12px;text-transform: uppercase;">POD:</td>
                    <td id="podName" class="boldGreen">${lclUnitsScheduleForm.destination}</td>
                    <c:if test="${not empty lclUnitsScheduleForm.voyageNo}">
                        <td width="2%" style="font-weight:bold;font-size: 12px;">Starting Voyage#:</td>
                        <td width="20%" id="voyagNoSearch" class="boldGreen">${lclUnitsScheduleForm.voyageNo}</td>
                    </c:if>
                </tr>
            </table>
            <%@include  file="/jsps/LCL/import/voyage/search/results.jsp" %>
        </cong:form>
    </body>
</html>
