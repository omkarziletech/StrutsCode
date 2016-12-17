<%-- 
    Document   : containerDetails
    Created on : Jun 13, 2013, 7:08:52 PM
    Author     : Rajesh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Container Details</title>
        <%@include file="../init.jsp" %>
        <%@include file="/jsps/preloader.jsp" %>
        <%@include file="/taglib.jsp" %>
        <%@include file="/jsps/includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/jsps/LCL/js/eculineEdi.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                var sslineNo = $('#sslineNo').val();
                if (sslineNo == '') {
                    $('#ssline').val($('#sslineName').val());
                }
                var vesselError = $('#vesselError').val();
                if (vesselError === 'error-indicator') {
                    if ($("#vesselErrorCheck").attr("checked") == true) {
                        $("#vesselName").removeClass("error-indicator");
                    } else {
                        $("#vesselName").addClass("error-indicator");
                    }
                } else {
                    $("#vesselErrorCheck").attr('disabled', true);
                }
            });
        </script>
    </head>
    <div id="loadDataEculine">
        <body>
            <html:form action="/lclEculineEdi.do" name="lclEculineEdiForm"
                       styleId="lclEculineEdiForm" type="com.gp.cvst.logisoft.struts.form.lcl.LclEculineEdiForm" scope="request" method="post">
                <table class="table" id="inner-tbl">
                    <tr>
                        <td>
                            <c:choose>
                                <c:when test="${ecu.approved eq true}">
                                    <c:set var="isApproved" value="Approved"/>
                                    <c:set var="isApprovedClass" value="green"/>
                                </c:when>
                                <c:when test="${ecu.adjudicated}">
                                    <c:set var="isApproved" value="Ready To Approve"/>
                                    <c:set var="isApprovedClass" value="yellow"/>
                                    <c:set var="readyToAppr" value="true"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="isApproved" value="Not Ready To approve"/>
                                    <c:set var="isApprovedClass" value="red"/>
                                </c:otherwise>
                            </c:choose>
                            <input type="button" class="button" value="Go Back" onclick="goBackSearch('${path}', '${ecu.containerNo}', '${ecu.voyNo}');"/> <br/>
                            <div id="voy-detail" onclick="toggle('#voy-table');" class="head-tag">
                                For Container # :
                                <input type="text" class="boldRed" onClick="this.select();" value="${ecu.containerNo}"
                                       style="border: 0px none; background: transparent; outline:none;"/>
                                <span style="margin-left: 200px;">
                                    Container Status:
                                    <span class="${isApprovedClass}">${isApproved}</span>
                                    <c:choose>
                                        <c:when test="${ecu.approved == true}">
                                            <img title="Approved Voy# ${eculineEdi.voyNo}" src="${path}/jsps/LCL/images/approve.png" class="valign-bottom"/>
                                        </c:when>
                                        <c:when test="${ecu.adjudicated}">
                                            <img title="Click to Approve Container# ${eculineEdi.containerNo}" src="${path}/images/icons/check-yellow.png"  
                                                 onclick="approveVoyFromContainer('${path}', '${ecu.voyNo}', '${ecu.id}', '${ecu.containerNo}');" class="edit valign-bottom"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img title="Click to Check , Why Container# ${eculineEdi.containerNo} not Ready to Approve?" src="${path}/images/icons/check-gray.png"
                                                 class="edit valign-bottom" onclick="validateApproveVoyFromContainer('${ecu.agentNo}', '${ecu.billingTerminal}', '${ecu.vesselCode}', '${ecu.vesselErrorCheck}', '${ecu.sslineNo}', '${ecu.warehouseNo}', '${ecu.masterBl}', '${ecu.unitTypeId}', '${ecu.polUncode}', '${ecu.podUncode}', '${ecu.unPolCount}', '${ecu.unPodCount}');"/>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                                <span class="green" style="float:right;">
                                    Click this bar to Expand / Hide
                                </span>
                            </div>
                            <table class="table no-border" id="voy-table">
                                <tr>
                                    <td class="label">Container Size/Type</td>
                                    <td>
                                        <cong:autocompletor name="contSize" template="two" id="contSize" query="UNIT_TYPE"
                                                            fields="" styleClass="mandatory textbox textCap ${containerSize}" value="${ecu.contSize}" width="300"
                                                            container="NULL" shouldMatch="true" scrollHeight="200px" callback="removeErrorBorder('contSize')"/>
                                    </td>
                                    <fmt:formatDate var="arvlDate" value="${ecu.arvlDate}" pattern="dd-MMM-yyyy"/>
                                    <td class="label">Arrival Date
                                        <cong:calendar name="arvlDate" id="arvlDate" value="${arvlDate}" styleClass="textbox"/>
                                    </td>
                                    <td class="label">No. of House B/L</td>
                                    <td>
                                        <html:text property="noOfBl" styleId="noOfBl" readonly="true" styleClass="textbox readonly" value="${ecu.noOfBl}"/>
                                    </td>
                                    <td class="label">Port of load</td>
                                    <td>
                                        <cong:autocompletor name="polUncode" template="one" id="polUncode" query="ORIGIN_UNLOC"
                                                            fields="NULL,NULL,NULL,originId" styleClass="mandatory textbox textCap" value="${ecu.polUncode}" width="300" container="NULL" shouldMatch="true" scrollHeight="200px"
                                                            callback="getUnloc('#polUncode', '');removeBorderRedCol('polUncode');loadNewAgentValues();"/>
                                        <input type="hidden" name="originId" id="originId" value="${ecu.originId}"/>
                                    </td>
                                    <td class="label">Port of Discharge</td>
                                    <td>
                                        <cong:autocompletor name="podUncode" template="one" id="podUncode" query="PORT" position="left"
                                                            fields="NULL,NULL,NULL,destId" styleClass="mandatory textbox textCap" value="${ecu.podUncode}" width="300" container="NULL" shouldMatch="true" scrollHeight="200px"
                                                            callback="getUnloc('#podUncode', '');removeBorderRedCol('podUncode');"/>
                                        <input type="hidden" name="destinationId" id="destinationId" value="${ecu.destinationId}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label">S.S Line #</td><td width="-23px" class="label">
                                        <cong:autocompletor name="ssline" id="ssline" styleClass="mandatory textbox ${sslineNo}"  position="right" fields="ssline,NULL,NULL,NULL,NULL,ssLineNumber"
                                                            shouldMatch="true" width="600" query="IMPORT_SSLINE" template="tradingPartner" container="null" scrollHeight="300px"
                                                            value="${ecu.sslineNo}" callback="fillSsline();removeErrorBorder('ssline');" title="${ecu.sslineName}"/>
                                        SS Line<cong:label id="ssLineBluescreenNo"  style="font-weight:bold;color: blue;font-size: 12px;"></cong:label>
                                        <cong:hidden id="sslineNo" name="sslineNo" value="${ecu.sslineNo}"/>
                                        <input type="hidden" id="sslineName" name="sslineName" value="${ecu.sslineName}"/>
                                        <input type="hidden" id="ssLineNumber" name="ssLineNumber" value="${ecu.ssLineBluescreenNo}"/>
                                    </td><td class="label">
                                        Pieces</td>
                                    <td>
                                        <html:text property="pieces" styleId="pieces" readonly="true" styleClass="textbox readonly" value="${ecu.pieces}"/>
                                    </td>
                                    <td class="label">Weight</td>
                                    <td>
                                        <html:text property="weight" styleId="weight" readonly="true" styleClass="textbox readonly" value="${ecu.weight}"/>
                                    </td>
                                    <fmt:formatDate var="sailDate" value="${ecu.sailDate}" pattern="dd-MMM-yyyy"/>
                                    <td class="label">Sail Date
                                        <cong:calendar name="sailDate" id="sailDate" value="${sailDate}" styleClass="textbox"/>
                                    </td>
                                    <td class="label">Cubic Meters</td>
                                    <td>
                                        <html:text property="cube" styleId="cube" readonly="true" styleClass="textbox readonly" value="${ecu.cube}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label">Vessel</td>
                                    <td>
                                        <cong:autocompletor name="vesselName" id="vesselName" template="one" params="14" position="right" shouldMatch="true"
                                                            query="GENERICCODE_BY_CODETYPEID"  fields="" width="200" container="NULL"
                                                           scrollHeight="200px" styleClass="mandatory textbox ${vessel}" value="${ecu.vesselName}" callback="removeErrorBorder('vesselName');"/>
                                        <cong:checkbox  container="true" title="Check here if new vessel " styleClass="check-box" name="vesselErrorCheck" id="vesselErrorCheck" value="${ecu.vesselErrorCheck}" onclick="removeErrCheckVessel();"/>
                                        <input type="hidden" id="vesselError"  name="vesselError" value="${vessel}"/>
                                    </td>
                                    <td class="label">S.S.Voy #</td>
                                    <td>
                                        <html:text property="voyNo" styleId="voyNo" readonly="true" styleClass="textbox readonly" value="${ecu.voyNo}"/>
                                    </td>
                                    <td class="label">Lloyds #</td>
                                    <td>
                                        <html:text property="lloydsNo" styleId="lloydsNo" readonly="true" styleClass="textbox readonly" value="${ecu.lloydsNo}"/>
                                    </td>
                                    <td class="label">Seal #</td>
                                    <td>
                                        <html:text property="sealNo" styleId="sealNo" readonly="true" styleClass="textbox readonly" value="${ecu.sealNo}"/>
                                    </td>
                                    <td class="label">Master BL</td>
                                    <td>
                                        <html:text property="masterBl" styleId="masterBl" styleClass="mandatory textbox" value="${ecu.masterBl}" onchange="removeBorderRedCol('masterBl');"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label">Reference #</td>
                                    <td>
                                        <html:text property="refNo" styleId="refNo" readonly="true" styleClass="textbox readonly" value="${ecu.refNo}"/>
                                    </td>
                                    <td class="label">Sender Code</td>
                                    <td>
                                        <html:text property="sender" styleId="sender" readonly="true" styleClass="textbox readonly" value="${ecu.sender}"/>
                                    </td>
                                    <td class="label">Sender Email</td>
                                    <td>
                                        <html:text property="senderEmail" styleId="senderEmail" readonly="true" styleClass="textbox readonly"
                                                   value="${ecu.senderEmail}" title="${ecu.senderEmail}"/>
                                    </td>
                                    <td class="label">Receiver Code</td>
                                    <td>
                                        <html:text property="receiver" styleId="receiver" readonly="true" styleClass="textbox readonly"
                                                   value="${ecu.receiver}"/>
                                    </td>
                                    <td class="label">Receiver Email</td>
                                    <td>
                                        <html:text property="receiverEmail" styleId="receiverEmail" readonly="true" styleClass="textbox readonly"
                                                   value="${ecu.receiverEmail}" title="${ecu.receiverEmail}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label">Agent #</td>
                                    <td class="label">
                                        <cong:autocompletor name="agentName" id="agentName" width="400" scrollHeight="150px" container="NULL"
                                                            params="${ecu.polUncode}" query="IMPORTORIGINAGENT" fields="agentNo"
                                                            template="agent" shouldMatch="true" value="${ecu.agentNo}"
                                                            styleClass="mandatory textbox" callback="removeBorderRedCol('agentName');"/>
                                        <html:hidden property="agentNo" styleId="agentNo" value="${ecu.agentNo}"/>
                                    </td>
                                    <td class="label">CFS devanning</td>
                                    <td class="label">
                                        <cong:autocompletor name="warehouseName" id="warehouseName" width="400" scrollHeight="150px" container="NULL"
                                                            query="CFS_WAREHOUSE" fields="NULL,NULL,cfswarehsAddress,city,state,zipCode,phone,fax,warehouseNo" template="delwhse" shouldMatch="true" value="${ecu.warehouseName}"
                                                            styleClass="mandatory textbox" callback="removeBorderRedCol('warehouseName');cfsAddress();"/>
                                        <input type="hidden" name="cfswarehsAddress" id="cfswarehsAddress" value=""/>
                                        <input type="hidden" name="city" id="city" value=""/>
                                        <input type="hidden" name="state" id="state" value=""/>
                                        <input type="hidden" name="zipCode" id="zipCode" value=""/>
                                        <input type="hidden" name="phone" id="phone" value=""/>
                                        <input type="hidden" name="fax" id="fax" value=""/>
                                        <html:hidden property="warehouseNo" styleId="warehouseNo" value="${ecu.warehouseNo}"/>
                                    </td>
                                    <td class="label">Billing terminal</td>
                                    <td>
                                        <cong:autocompletor name="terminal" id="terminal" width="400" scrollHeight="150px" container="NULL"
                                                            query="IMPORTTERMINAL" fields="NULL,terminalNo" template="three" shouldMatch="true" value="${ecu.billingTerminal}"
                                                            styleClass="mandatory textbox" callback="removeBorderRedCol('terminal');"                              />
                                        <cong:hidden name="terminalNo" id="terminalNo" value="${ecu.terminalNo}"/>
                                    </td>
                                    <td class="label">Doc Received
                                        <cong:calendar id="docReceived" name="docReceived" value="${ecu.docReceived}" styleClass="mandatory textbox"/>
                                    </td>
                                    <td class="label"></td>
                             <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td class="label">Container remarks</td>
                                    <td colspan="1"><html:textarea property="containerRemarks" styleId="containerRemarks" styleClass="textarea" value="${ecu.containerRemarks}" rows="5" cols="50"/>
                                    </td>
                                    <td class="label"><cong:td styleClass="textlabelsBoldleftforlcl" colspan="2">
                                            <cong:textarea rows="4" cols="32" readOnly="true"  styleClass="textlabelsBoldForTextBoxDisabledLook" name="warehsAddress" id="warehsAddress"  value="${ecu.warehouseAddress}"/>
                                        </cong:td></td>
                                    <td class="label">
                                    </td>
                                    <td class="label"></td>
                                    <td>
                                    </td>
                                    <td class="label"></td>
                                    <td colspan="2">
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <c:if test="${ecu.approved eq false}">
                                            <input type="button" class="button" value="Save" onclick="updateContainer('${path}', '${ecu.containerNo}', '${ecu.voyNo}');"/>
                                        </c:if>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <input type="hidden" id="methodName" name="methodName"/>
                <html:hidden property="blToApprove" styleId="blToApprove"/>
                <html:hidden property="id" styleId="ecuId" value="${ecu.id}"/>
                <html:hidden property="containerNo" styleId="containerNo" value="${ecu.containerNo}"/>
                <html:hidden property="unitTypeId" styleId="unitTypeId" value="${ecu.unitTypeId}"/>
                <input type="hidden" id="ecuContSize" name="ecuContSize" value="${ecu.contSize}"/>
                <input type="hidden" id="setAgentValFlag" name="setAgentValFlag"/>
                <input type="hidden" name="autoCostFlag" id="autoCostFlag"/>
            </html:form>
            <%@include file="/jsps/LCL/eculine/billsOfLading.jsp" %>
        </body>
    </div>
</html>
