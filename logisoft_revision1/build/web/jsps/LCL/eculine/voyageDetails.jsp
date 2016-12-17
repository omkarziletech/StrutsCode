<%-- 
    Document   : eculineEdiBl
    Created on : Jun 13, 2013, 7:08:52 PM
    Author     : Rajesh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Voyage Details</title>
        <%@include file="../init.jsp" %>
        <%@include file="/taglib.jsp" %>
        <%@include file="/jsps/includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/jsps/LCL/js/eculineEdi.js"></script>
    </head>
    <body>
        <html:form action="/lclEculineEdi.do" name="lclEculineEdiForm"
                   styleId="lclEculineEdiForm" type="com.gp.cvst.logisoft.struts.form.lcl.LclEculineEdiForm" scope="request" method="post">
            <table class="table" id="inner-tbl">
                <tr><td>
                        <div id="voy-detail" onclick="toggle('#voy-table');" class="font-12px bold" style="background-color: lightgray; cursor: pointer;">
                            For Container # :
                            <span class="font-12px red">${ecu.containerNo}</span>
                            <span class="align-right green">&nbsp;&nbsp;Click this bar to Expand / Hide</span>
                        </div>
                        <table class="table no-border" id="voy-table" style="display: none">
                            <tr>
                                <td class="label">Container size/Type</td>
                                <td>
                                    <html:text property="contSize" styleId="contSize" styleClass="textbox" value="${ecu.contSize}"/>
                                </td>
                                <td class="label">Arrival Date
                                    <cong:calendar name="arvlDate" id="arvlDate" value="${ecu.arvlDate}"/>
                                </td>
                                <td class="label">No. of B/Ls</td>
                                <td>
                                    <html:text property="noOfBl" styleId="noOfBl" readonly="true" styleClass="textbox readonly" value="${ecu.noOfBl}"/>
                                </td>
                                <td class="label">Port of load</td>
                                <td>
                                    <cong:autocompletor name="polUncode" template="tradingPartner" id="polUncode" query="ORIGIN_UNLOC"
                                                        fields="" styleClass="textlabelsBoldForTextBox textCap" value="${ecu.polUncode}"
                                                        width="250" container="NULL" shouldMatch="true" scrollHeight="200px"
                                                        callback="getUnloc('#polUncode', '');"/>
                                </td>
                                <td class="label">Port of Discharge</td>
                                <td>
                                    <cong:autocompletor name="podUncode" template="tradingPartner" id="podUncode" query="PORT"
                                                        fields="" styleClass="textlabelsBoldForTextBox textCap" value="${ecu.podUncode}" width="250"
                                                        container="NULL" shouldMatch="true" scrollHeight="200px"
                                                        callback="getUnloc('#podUncode', '');"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="label">S.S Line #</td>
                                <td>
                                    <cong:autocompletor name="ssline" id="ssline" styleClass="mandatory textbox ${sslineNo}"  position="right" fields="ssline"
                                                        shouldMatch="true" width="600" query="IMPORT_SSLINE" template="tradingPartner" container="null" scrollHeight="300px"
                                                        value="${ecu.sslineNo}" callback="fillSsline();" />
                                    <cong:hidden id="sslineNo" name="sslineNo" value="${ecu.sslineNo}"/>
                                </td>
                                <td class="label">Pieces</td>
                                <td>
                                    <html:text property="pieces" styleId="pieces" readonly="true" styleClass="textbox readonly" value="${ecu.pieces}"/>
                                </td>
                                <td class="label">Weight</td>
                                <td>
                                    <html:text property="weight" styleId="weight" readonly="true" styleClass="textbox readonly" value="${ecu.weight}"/>
                                </td>
                                <td class="label">Sail Date
                                    <cong:calendar name="sailDate" id="sailDate" value="${ecu.sailDate}"/>
                                </td>
                                <td class="label">Cubic Meters</td>
                                <td>
                                    <html:text property="cube" styleId="cube" readonly="true" styleClass="textbox readonly" value="${ecu.cube}"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="label">Vessel</td>
                                <td>
                                    <cong:autocompletor name="vesselName" id="vesselName" template="one" params="14" position="right"
                                                        query="GENERICCODE_BY_CODETYPEID" width="250" container="NULL"
                                                        styleClass="mandatory textbox ${vessel}" value="${ecu.vesselName}"/>
                                </td>
                                <td class="label">S.S.Voy #</td>
                                <td>
                                    <html:text property="voyNo" styleId="voyNo" readonly="true" styleClass="textbox readonly" value="${ecu.voyNo}"/>
                                </td>
                                <td class="label">Lloyds #</td>
                                <td>
                                    <html:text property="lloydsNo" styleId="lloydsNo" styleClass="textbox" value="${ecu.lloydsNo}"/>
                                </td>
                                <td class="label">Seal #</td>
                                <td>
                                    <html:text property="sealNo" styleId="sealNo" styleClass="textbox" value="${ecu.sealNo}"/>
                                </td>
                                <td class="label">Master BL</td>
                                <td>
                                    <html:text property="masterBl" styleId="masterBl" styleClass="mandatory textbox" value="${ecu.masterBl}"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="label">Reference #</td>
                                <td>
                                    <html:text property="refNo" styleId="refNo" styleClass="textbox" value="${ecu.refNo}"/>
                                </td>
                                <td class="label">Sender Code</td>
                                <td>
                                    <html:text property="sender" styleId="sender" styleClass="textbox" value="${ecu.sender}"/>
                                </td>
                                <td class="label">Sender Email</td>
                                <td>
                                    <html:text property="senderEmail" styleId="senderEmail" styleClass="textbox" value="${ecu.senderEmail}"/>
                                </td>
                                <td class="label">Receiver Code</td>
                                <td>
                                    <html:text property="receiver" styleId="receiver" styleClass="textbox" value="${ecu.receiver}"/>
                                </td>
                                <td class="label">Receiver Email</td>
                                <td>
                                    <html:text property="receiverEmail" styleId="receiverEmail" styleClass="textbox" value="${ecu.receiverEmail}"/>
                                </td>
                            </tr>
                            <tr></tr>
                            <tr></tr>
                            <tr></tr>
                            <tr></tr>
                            <tr>
                                <td class="label">Agent #</td>
                                <td class="label">
                                    <cong:autocompletor name="agentName" id="agentName" width="400" scrollHeight="150px" container="NULL"
                                                        params="${ecu.polUncode}"
                                                        query="IMPORTORIGINAGENT" fields="agentNo" template="two" shouldMatch="true" value="${ecu.agentNo}"
                                                        styleClass="mandatory textbox"/>
                                    <html:hidden property="agentNo" styleId="agentNo" value="${ecu.agentNo}"/>
                                </td>
                                <td class="label">CFS devanning</td>
                                <td class="label">
                                    <cong:autocompletor name="warehouseName" id="warehouseName" width="400" scrollHeight="150px" container="NULL"
                                                        query="IMPORTWAREHOUSE" fields="NULL,NULL,NULL,warehouseNo" template="one" shouldMatch="true" value="${ecu.warehouseName}"
                                                        styleClass="mandatory textbox"/>
                                    <html:hidden property="warehouseNo" styleId="warehouseNo" value="${ecu.warehouseNo}"/>
                                </td>
                                <td class="label">Billing terminal</td>
                                <td>
                                    <cong:autocompletor name="terminal" id="terminal" width="400" scrollHeight="150px" container="NULL"
                                                        query="IMPORTTERMINAL" fields="NULL,terminalNo" template="three" shouldMatch="true" value="${ecu.billingTerminal}"
                                                        styleClass="mandatory textbox"/>
                                    <cong:hidden name="terminalNo" id="terminalNo" value="${ecu.terminalNo}"/>
                                </td>
                                <td class="label">Container remarks</td>
                                <td colspan="2">
                                    <html:textarea property="containerRemarks" styleId="containerRemarks" styleClass="textarea" value="${ecu.containerRemarks}" rows="3" cols="30"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="button" class="button" value="Update" onclick="updateContainer();"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <input type="hidden" id="methodName" name="methodName"/>
            <html:hidden property="blToApprove" styleId="blToApprove"/>
            <html:hidden property="id" styleId="ecuId"/>
            <html:hidden property="containerNo" styleId="containerNo" value="${ecu.containerNo}"/>
        </html:form>
        <%@include file="/jsps/LCL/eculine/billsOfLading.jsp" %>
    </body>
</html>
