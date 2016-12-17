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
        <title>Eculine EDI Voyage search</title>
        <%@include file="init.jsp" %>
        <%@include file="/taglib.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
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
                        <table class="table no-border" id="voy-table">
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
                                    <cong:autocompletor name="polUncode" template="tradingPartner" id="polUncode" query="PORT"
                                                        fields="" styleClass="textlabelsBoldForTextBox textCap" value="${ecu.polUncode}"
                                                        width="250" container="NULL" shouldMatch="true" scrollHeight="200px"
                                                        callback="getUnloc('#polUncode', '');"/>
                                </td>
                                <td class="label">Port of Discharge</td>
                                <td>
                                    <cong:autocompletor name="podUncode" template="tradingPartner" id="podUncode" query="ORIGIN_UNLOC"
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
                                    <html:text property="masterBl" styleId="masterBl" styleClass="textbox" value="${ecu.masterBl}"/>
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
                                <td class="label">Billing terminal</td>
                                <td>
                                    <cong:autocompletor name="terminal" id="terminal" width="400" scrollHeight="150px" container="NULL"
                                                        query="IMPORTTERMINAL" fields="NULL,terminalNo" template="three" shouldMatch="true" value="${ecu.billingTerminal}"
                                                        styleClass="textbox"/>
                                    <cong:hidden name="terminalNo" id="terminalNo" value="${ecu.terminalNo}"/>
                                </td>
                                <td class="label">Container remarks</td>
                                <td colspan="2">
                                    <html:textarea property="containerRemarks" styleId="containerRemarks" styleClass="textarea" value="${ecu.containerRemarks}" rows="3" cols="30"/>
                                </td>
                                <td colspan="4"></td>
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
        </html:form>
        <display:table name="${blInfoList}" id="bl" class="display-table" requestURI="/lclEculineEdi.do">
            <display:column title="House B/L" property="blNo"></display:column>
            <display:column title="Shipper">
                <c:choose>
                    <c:when test="${not empty bl.shipperNo}">
                        <span title="Mapped successfully">${bl.shipperNo}</span>
                    </c:when>
                    <c:otherwise>
                        <span title="Not mapped with trading partner">${bl.shipper}</span>
                    </c:otherwise>
                </c:choose>
            </display:column>
            <display:column title="Consignee">
                <c:choose>
                    <c:when test="${not empty bl.consigneeNo}">
                        <span title="Mapped successfully">${bl.consigneeNo}</span>
                    </c:when>
                    <c:otherwise>
                        <span title="Not mapped with trading partner">${bl.consignee}</span>
                    </c:otherwise>
                </c:choose>
            </display:column>
            <display:column title="POL" property="pol"></display:column>
            <display:column title="POD" property="pod"></display:column>
            <display:column title="POR" property="por"></display:column>
            <display:column title="Delivery" property="delivery"></display:column>
            <display:column title="Pieces" property="pieces"></display:column>
            <display:column title="Weight">
                <span title="${bl.weight} ${bl.weightUnit}">${bl.weight}</span>
            </display:column>
            <display:column title="Volume">
                <span title="${bl.volume} ${bl.volumeUnit}">${bl.volume}</span>
            </display:column>
            <display:column title="Action">
                <img alt="Edit" title="Edit" src="${path}/images/icons/edit.gif" class="editBl" onclick="editBl('${path}','${bl.id}');"/>
            </display:column>
        </display:table>
    </body>
</html>
