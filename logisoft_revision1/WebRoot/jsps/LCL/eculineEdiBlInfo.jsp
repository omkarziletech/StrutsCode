<%-- 
    Document   : eculineEdiBlInfo
    Created on : Jun 14, 2013, 3:19:43 PM
    Author     : Rajesh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Eculine EDI Edit Bill of Lading</title>
        <%@include file="init.jsp" %>
        <%@include file="/taglib.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/jsps/LCL/js/eculineEdi.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                toggleAccount();
            });
        </script>
    </head>
    <body>
        <html:form action="/lclEculineEdiBlInfo.do?methodName=updateBl" name="lclEculineEdiBlInfoForm"
                   styleId="lclEculineEdiBlInfoForm" type="com.gp.cvst.logisoft.struts.form.lcl.LclEculineEdiBlInfoForm" scope="request" method="post">
            <div id="parties-detail" onclick="toggle('#parties-table');" class="font-12px bold" style="background-color: lightgray; cursor: pointer;">
                Parties
                <span class="align-right green">&nbsp;&nbsp;Click this bar to Expand / Hide</span>
            </div>
            <table class="table no-border" id="parties-table">
                <tr>
                    <td class="label">Shipper</td>
                    <td>
                        <cong:autocompletor name="shipperContact" template="tradingPartner" id="shipperContact" query="SHIPPER"
                                            fields="shipperCode,shipper_acct_type,shipper_sub_type,NULL,NULL,NULL,NULL,NULL,shipperAddress,shipperCity,shipperState,shipperCountry,shipperZip,NULL,shipperPhone,shipperFax,shipperEmail,NULL,NULL,NULL,shipperPoa,shipperCredit"
                                            styleClass="textlabelsBoldForTextBox textCap" value="${bl.shipperCode}" width="600" container="NULL" shouldMatch="true" scrollHeight="200px" callback="fillShipAddress();"/>
                        <html:text property="manualShipper" styleClass="textbox" styleId="manualShipper" value="${bl.shipperCode}"/>
                        <html:hidden property="shipperCode" styleId="shipperCode" value="${bl.shipperCode}"/>
                        <input type="hidden" id="shipperAddress"/>
                        <input type="hidden" id="shipperCity"/>
                        <input type="hidden" id="shipperState"/>
                        <input type="hidden" id="shipperCountry"/>
                        <input type="hidden" id="shipperZip"/>
                        <c:choose>
                            <c:when test="${bl.manualShipper}">
                                <input type="checkbox" name="chkManualSh" id="chkManualSh" checked onchange="toggleAcct('Sh');"/>
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" name="chkManualSh" id="chkManualSh" onchange="toggleAcct('Sh');"/>
                            </c:otherwise>
                        </c:choose>
                        <cong:img src="${path}/jsps/LCL/images/add2.gif" width="16" height="16" alt="display" styleClass="tp" onclick="openTradingPartner('${path}','SH')"/>
                    </td>
                    <td class="label">Consignee</td>
                    <td>
                        <cong:autocompletor name="consContact" template="tradingPartner" id="consContact" query="CONSIGNEE"
                                            fields="consigneeCode,consignee_acct_type,consignee_sub_type,NULL,NULL,NULL,NULL,NULL,consigneeAddress,consigneeCity,consigneeState,consigneeCountry,consigneeZip,NULL,consigneePhone,consigneeFax,consigneeEmail,NULL,NULL,NULL,consigneePoa,consigneeCredit"
                                            styleClass="textlabelsBoldForTextBox textCap" value="${bl.consCode}" width="600" container="NULL" shouldMatch="true" scrollHeight="200px" callback="fillConsAddress();"/>
                        <html:text property="manualCons" styleClass="textbox" styleId="manualCons" value="${bl.consCode}"/>
                        <html:hidden property="consCode" styleId="consigneeCode" value="${bl.consCode}"/>
                        <input type="hidden" id="consigneeAddress"/>
                        <input type="hidden" id="consigneeCity"/>
                        <input type="hidden" id="consigneeState"/>
                        <input type="hidden" id="consigneeCountry"/>
                        <input type="hidden" id="consigneeZip"/>
                        <c:choose>
                            <c:when test="${bl.manualCons}">
                                <input type="checkbox" name="chkManualCo" id="chkManualCo" checked onchange="toggleAcct('Co');"/>
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" name="chkManualCo" id="chkManualCo" onchange="toggleAcct('Co');"/>
                            </c:otherwise>
                        </c:choose>
                        <cong:img src="${path}/jsps/LCL/images/add2.gif" width="16" height="16" alt="display" styleClass="tp" onclick="openTradingPartner('${path}','CO')"/>
                    </td>
                    <td class="label">Notify1</td>
                    <td>
                        <cong:autocompletor name="notify1Contact" template="tradingPartner" id="notify1Contact" query="MAIN_SCREEN_CLIENT"
                                            fields="notify1Code,NULL,NULL,NULL,NULL,NULL,NULL,NULL,notify1Address,notify1City,notify1State,notify1Country,notify1Zip"
                                            styleClass="textlabelsBoldForTextBox textCap" value="${bl.notify1Code}" width="600" container="NULL" shouldMatch="true" scrollHeight="200px" callback="fillNotify1Address();"/>
                        <html:text property="manualNotify1" styleClass="textbox" styleId="manualNotify1" value="${bl.notify1Code}"/>
                        <html:hidden property="notify1Code" styleId="notify1Code" value="${bl.notify1Code}"/>
                        <input type="hidden" id="notify1Address"/>
                        <input type="hidden" id="notify1City"/>
                        <input type="hidden" id="notify1State"/>
                        <input type="hidden" id="notify1Country"/>
                        <input type="hidden" id="notify1Zip"/>
                        <c:choose>
                            <c:when test="${bl.manualNotify1}">
                                <input type="checkbox" name="chkManualNo1" id="chkManualNo1" checked onchange="toggleAcct('No1');"/>
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" name="chkManualNo1" id="chkManualNo1" onchange="toggleAcct('No1');"/>
                            </c:otherwise>
                        </c:choose>
                        <cong:img src="${path}/jsps/LCL/images/add2.gif" width="16" height="16" alt="display" styleClass="tp" onclick="openTradingPartner('${path}','NO1')"/>
                    </td>
                    <td class="label">Notify2</td>
                    <td>
                        <cong:autocompletor name="notify2Contact" template="tradingPartner" id="notify2Contact" query="MAIN_SCREEN_CLIENT"
                                            fields="notify2Code,NULL,NULL,NULL,NULL,NULL,NULL,NULL,notify2Address,notify2City,notify2State,notify2Country,notify2Zip"
                                            styleClass="textlabelsBoldForTextBox textCap" value="${bl.notify2Code}" width="600" container="NULL" shouldMatch="true" scrollHeight="200px" callback="fillNotify2Address();"/>
                        <html:hidden property="notify2Code" styleId="notify2Code" value="${bl.notify2Code}"/>
                        <html:text property="manualNotify2" styleClass="textbox" styleId="manualNotify2" value="${bl.notify2Code}"/>
                        <input type="hidden" id="notify2Address"/>
                        <input type="hidden" id="notify2City"/>
                        <input type="hidden" id="notify2State"/>
                        <input type="hidden" id="notify2Country"/>
                        <input type="hidden" id="notify2Zip"/>
                        <c:choose>
                            <c:when test="${bl.manualNotify2}">
                                <input type="checkbox" name="chkManualNo2" id="chkManualNo2" checked onchange="toggleAcct('Sh');"/>
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" name="chkManualNo2" id="chkManualNo2" onchange="toggleAcct('Sh');"/>
                            </c:otherwise>
                        </c:choose>
                        <cong:img src="${path}/jsps/LCL/images/add2.gif" width="16" height="16" alt="display" styleClass="tp" onclick="openTradingPartner('${path}','NO2')"/>
                    </td>

                </tr>
                <tr>
                    <td class="label"></td>
                    <td>
                        <html:textarea property="shipperNad" styleId="shipperNad" value="${bl.shipperNad}" rows="4" cols="30"/>
                    </td>
                    <td class="label"></td>
                    <td>
                        <html:textarea property="consNad" styleId="consNad" styleClass="textarea" value="${bl.consNad}" rows="4" cols="30"/>
                    </td>
                    <td class="label"></td>
                    <td>
                        <html:textarea property="notify1Nad" styleId="notify1Nad" styleClass="textarea" value="${bl.notify1Nad}" rows="4" cols="30"/>
                    </td>
                    <td class="label"></td>
                    <td>
                        <html:textarea property="notify2Nad" styleId="notify2Nad" styleClass="textarea" value="${bl.notify2Nad}" rows="4" cols="30"/>
                    </td>

                </tr>
            </table>
            <table class="table no-border" id="loc-table">
                <tr>
                    <td colspan="2" class="font-12px bold" style="background-color: #eeeeee;">Location</td>
                </tr>
                <tr>
                    <td class="label">Pre-carriage By</td>
                    <td>
                        <html:text property="precarriageBy" styleId="precarriageBy" styleClass="textbox width-200px" value="${bl.precarriageBy}"/>
                    </td>
                    <td class="label">Place of receipt</td>
                    <td>
                        <html:text property="placeOfReceipt" styleId="placeOfReceipt" styleClass="textbox width-200px" value="${bl.placeOfReceipt}"/>
                    </td>
                    <td class="label">POL UnLocCode</td>
                    <td>
                        <cong:autocompletor name="polUncode" template="tradingPartner" id="polUncode" query="PORT"
                                            fields=""
                                            styleClass="textlabelsBoldForTextBox textCap" value="${bl.polUncode}" width="250" container="NULL" shouldMatch="true" scrollHeight="200px" callback="getUnloc('#polUncode', '#polDesc');"/>
                    </td>
                    <td class="label">POL desc</td>
                    <td>
                        <html:text property="polDesc" styleId="polDesc" styleClass="textbox width-200px" value="${bl.polDesc}"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">POD UnLocCode</td>
                    <td>
                        <cong:autocompletor name="podUncode" template="tradingPartner" id="podUncode" query="ORIGIN_UNLOC"
                                            fields=""
                                            styleClass="textlabelsBoldForTextBox textCap" value="${bl.podUncode}" width="250" container="NULL" shouldMatch="true" scrollHeight="200px" callback="getUnloc('#podUncode', '#podDesc');"/>
                    </td>
                    <td class="label">POD desc</td>
                    <td>
                        <html:text property="podDesc" styleId="podDesc" styleClass="textbox width-200px" value="${bl.podDesc}"/>
                    </td>
                    <td class="label">Place of delivery desc</td>
                    <td>
                        <html:text property="poddeliveryDesc" styleId="poddeliveryDesc" styleClass="textbox width-200px" value="${bl.poddeliveryDesc}"/>
                    </td>

                    <td class="label">Place of delivery UnLocCode</td>
                    <td>
                        <html:text property="poddeliveryUncode" styleId="poddeliveryUncode" styleClass="textbox width-200px" value="${bl.poddeliveryUncode}"/>
                    </td>
                </tr>
            </table>

            <table class="table no-border" id="cntr-table">
                <c:forEach items="${bl.cargoDetailsCollection}" var="cargo" varStatus="status">
                    <c:if test="${err eq cargo.id}">
                        <c:set var="error" value="error-indicator"></c:set>
                    </c:if>
                    <tr>
                        <td colspan="2" class="font-12px bold" style="background-color: #eeeeee;">Container # ${error}<span class="red">${cargo.containerId}</span></td>
                    </tr>
                    <tr>
                        <td class="label">Package amount</td>
                        <td>
                            <html:text property="packageAmount" styleId="packageAmount" styleClass="textbox" value="${cargo.packageAmount}"/>
                        </td>
                        <td class="label">Package</td>
                        <td>
                            <cong:autocompletor name="packageDesc" id="packageDesc${status.count}" template="packageType" styleClass="textbox ${error}"
                                                fields="NULL,packageAbbr${status.count}" width="500"  query="PACKAGE_TYPE" value="${cargo.packageDesc}" container="NULL" shouldMatch="true"
                                                callback="getPackageAbbr('${status.count}')"/>
                            <input type="hidden" id="packageAbbr${status.count}"/>
                        </td>
                        <td class="label">Goods desc</td>
                        <td>
                            <html:textarea property="goodDesc" styleId="goodDesc" styleClass="textarea" value="${cargo.goodDesc}" rows="3" cols="20"/>
                        </td>
                        <td class="label">Discharge instruction</td>
                        <td>
                            <html:text property="dischargeInstruction" styleId="dischargeInstruction" styleClass="textbox" value="${cargo.dischargeInstruction}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">Weight</td>
                        <td>
                            <html:text property="weightValues" styleId="weightValues" styleClass="textbox" value="${cargo.weightValues}"/>
                        </td>
                        <td class="label">Volume</td>
                        <td>
                            <html:text property="volumeValues" styleId="volumeValues" styleClass="textbox" value="${cargo.volumeValues}"/>
                        </td>
                        <td class="label">Commercial value</td>
                        <td>
                            <html:text property="commercialValue" styleId="commercialValue" styleClass="textbox" value="${cargo.commercialValue}"/>
                        </td>
                        <td class="label">Currency</td>
                        <td>
                            <html:text property="currency" styleId="currency" styleClass="textbox" value="${cargo.currency}"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <html:hidden styleId="id" property="id" value="${bl.id}"/>
            <input type="hidden" id="methodName"/>
            <input type="button" class="button" value="Update" onclick="updateBl();"/>
        </html:form>
    </body>
</html>
