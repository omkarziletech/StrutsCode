<%-- 
    Document   : bolDetails
    Created on : Jun 14, 2013, 3:19:43 PM
    Author     : Rajesh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Eculine EDI Edit Bill of Lading Details</title>
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
                toggleAccount();
            });
        </script>
    </head>
    <body>
        <html:form action="/lclEculineEdiBlInfo.do" name="lclEculineEdiBlInfoForm"
                   styleId="lclEculineEdiBlInfoForm" type="com.gp.cvst.logisoft.struts.form.lcl.LclEculineEdiBlInfoForm" scope="request" method="post">
            <c:set var="bol" value="${bl[0]}" />
            <table class="table">
                <tr>
                    <td>
                        <input type="button" class="button" value="Go Back" onclick="getVoyageDetails('${path}', '${ecuId}', '${bol.blNo}');"/>
                        <c:choose>
                            <c:when test="${bol.approved eq '1'}">
                                <c:set var="isApproved" value="Approved" />
                                <c:set var="isApprovedClass" value="green" />
                            </c:when>
                            <c:when test="${ readyToApproveFlag  eq 'true' && bol.approved ne '1'}">
                                <c:set var="isApproved" value="Ready To Approve" />
                                <c:set var="isApprovedClass" value="yellow" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="isApproved" value="Not Ready To Approve" />
                                <c:set var="isApprovedClass" value="red" />
                                <input type="button" class="button" value="Save" onclick="updateBol();"/>
                            </c:otherwise>
                        </c:choose>
                        <div id="bol-details" class="head-tag font-14px">
                            For House Bill Of Lading # :
                            <input type="text" class="boldRed" onClick="this.select();" value="${bol.blNo}"
                                   style="border: 0px none; background: transparent; outline:none;"/>
                            <span style="margin-left: 200px;">
                                House B/L Status:
                                <span class="${isApprovedClass}">${isApproved}</span>
                            </span>
                            <%-- <c:if test="${isApproved eq 'Unapproved'}">
                                 <a href="javascript:void(0)" onclick="approveBol();">Click here to Approve</a>
                             </c:if>--%>
                            <c:choose>
                                <c:when test="${bol.approved eq '1'}">
                                    <img title="Approved" src="${path}/images/icons/check-green.png" class="approvedBkg"/>
                                </c:when>
                                <c:when test="${readyToApproveFlag  eq 'true' && bol.approved ne '1'}">
                                    <img title="Ready to Approve" src="${path}/images/icons/check-yellow.png" class="approvedBkg" onclick="readyToApproveBl();"/>
                                </c:when>
                                <c:otherwise>
                                    <img title="Click to Check , Why BL not Ready to Approve ?" src="${path}/images/icons/check-gray.png" class="approveBkg"
                                         onclick="validateApproveBkgFromBLScreen('${bol.polUncode}', '${bol.podUncode}', '${bol.packageId}', '${bol.poddeliveryUncode}',${readyToApproveFlag});"/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <br/>
                        <div id="parties-detail" onclick="toggle('#parties-table');" class="head-tag">
                            Parties
                            <span class="green" style="position: absolute; right: 10px; width: 900px; text-align:right;">
                                Click this bar to Expand / Hide
                            </span>
                        </div>
                        <table class="table no-border" id="parties-table">
                            <tr>
                                <td class="label">Shipper</td>
                                <td>
                                    <cong:autocompletor name="shipperContact" template="tradingPartner" id="shipperContact" query="TRADING_PARTNER_IMPORTS"
                                                        fields="shipperCode,shipper_acct_type,shipper_sub_type,NULL,NULL,NULL,NULL,shipperDisabled,shipperAddress,shipperCity,shipperState,shipperCountry,shipperZip,NULL,shipperPhone,shipperFax,shipperEmail,NULL,NULL,NULL,shipperPoa,shipperCredit,NULL,NULL,NULL,NULL,NULL,shipForwardAcct"
                                                        paramFields="shipperSearchState,shipperSearchZip,shipperSearchSalesCode,shipperCountryUnLocCode"
                                                        styleClass="textlabelsBoldForTextBox textCap ${isShipperMapped}" value="${bol.shipperCode}" width="600" container="NULL" shouldMatch="true" scrollHeight="200px" callback="fillShipAddress();shipper_AccttypeCheck();"/>
                                    <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                                         title="Click here to edit House Shipper Search options" onclick="showClientSearchOption('${path}', 'Shipper')"/>
                                    <input type="hidden" name="shipperDisabled" id="shipperDisabled"/>
                                    <input type="hidden" name="shipForwardAcct" id="shipForwardAcct"/>
                                    <input type="hidden" name="shipperSearchState" id="shipperSearchState"/>
                                    <input type="hidden" name="shipperSearchZip" id="shipperSearchZip"/>
                                    <input type="hidden" name="shipperSearchSalesCode" id="shipperSearchSalesCode"/>
                                    <input type="hidden" name="shipperSearchCountry" id="shipperSearchCountry"/>
                                    <input type="hidden" name="shipperCountryUnLocCode" id="shipperCountryUnLocCode"/>
                                    <html:text property="manualShipper" styleClass="textbox" styleId="manualShipper" value="${bol.shipperCode}"/>
                                    <html:hidden property="shipperCode" styleId="shipperCode" value="${bol.shipperCode}"/>
                                    <input type="hidden" id="shipperAddress"/>
                                    <input type="hidden" name="shipper_acct_type" id="shipper_acct_type"/>
                                    <input type="hidden" id="shipperCity"/>
                                    <input type="hidden" id="shipperState"/>
                                    <input type="hidden" id="shipperCountry"/>
                                    <input type="hidden" id="shipperZip"/>
                                    <c:choose>
                                        <c:when test="${bol.manualShipper}">
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
                                    <cong:autocompletor name="consContact" template="tradingPartner" id="consContact" query="IMPORTS_TP"
                                                        fields="consigneeCode,consignee_acct_type,consignee_sub_type,NULL,NULL,NULL,NULL,consigneeDisabled,consigneeAddress,consigneeCity,consigneeState,consigneeCountry,consigneeZip,NULL,consigneePhone,consigneeFax,consigneeEmail,NULL,NULL,NULL,consigneePoa,consigneeCredit,NULL,NULL,NULL,NULL,NULL,consigneeForwardAcct"
                                                        paramFields="consigneeSearchState,consigneeSearchZip,consigneeSearchSalesCode,consigneeCountryUnLocCode"
                                                        styleClass="textlabelsBoldForTextBox textCap ${isConsMapped}" value="${bol.consCode}" width="600" container="NULL" shouldMatch="true" scrollHeight="200px" callback="fillConsAddress();consignee_AccttypeCheck();"/>
                                    <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                                         title="Click here to edit House Consignee Search options" onclick="showClientSearchOption('${path}', 'Consignee');"/>
                                    <input type="hidden" name="consigneeDisabled" id="consigneeDisabled"/>
                                    <input type="hidden" name="consigneeForwardAcct" id="consigneeForwardAcct"/>
                                    <input type="hidden" name="consigneeSearchState" id="consigneeSearchState"/>
                                    <input type="hidden" name="consigneeSearchZip" id="consigneeSearchZip"/>
                                    <input type="hidden" name="consigneeSearchSalesCode" id="consigneeSearchSalesCode"/>
                                    <input type="hidden" name="consigneeSearchCountry" id="consigneeSearchCountry"/>
                                    <input type="hidden" name="consigneeCountryUnLocCode" id="consigneeCountryUnLocCode"/>
                                    <html:text property="manualCons" styleClass="textbox" styleId="manualCons" value="${bol.consCode}"/>
                                    <html:hidden property="consCode" styleId="consigneeCode" value="${bol.consCode}"/>
                                    <input type="hidden" id="consigneeAddress"/>
                                    <input type="hidden" name="consignee_acct_type" id="consignee_acct_type"/>
                                    <input type="hidden" id="consigneeCity"/>
                                    <input type="hidden" id="consigneeState"/>
                                    <input type="hidden" id="consigneeCountry"/>
                                    <input type="hidden" id="consigneeZip"/>
                                    <c:choose>
                                        <c:when test="${bol.manualCons}">
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
                                    <cong:autocompletor name="notify1Contact" template="tradingPartner" id="notify1Contact" query="TRADING_PARTNER_IMPORTS"
                                                        fields="notify1Code,notify1_acct_type,NULL,NULL,NULL,NULL,NULL,notifyDisabled,notify1Address,notify1City,notify1State,notify1Country,notify1Zip,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,notifyForwardAcct"
                                                        paramFields="notifySearchState,notifySearchZip,notifySearchSalesCode,notifyCountryUnLocCode"
                                                        styleClass="textlabelsBoldForTextBox textCap ${isNotify1Mapped}" value="${bol.notify1Code}" width="600" container="NULL" shouldMatch="true" scrollHeight="200px" callback="fillNotify1Address();notify1_AccttypeCheck();"/>
                                    <html:text property="manualNotify1" styleClass="textbox" styleId="manualNotify1" value="${bol.notify1Code}"/>
                                    <html:hidden property="notify1Code" styleId="notify1Code" value="${bol.notify1Code}"/>
                                    <input type="hidden" name="notifyDisabled" id="notifyDisabled"/>
                                    <input type="hidden" name="notifyForwardAcct" id="notifyForwardAcct"/>
                                    <input type="hidden" id="notify1Address"/>
                                    <input type="hidden" name="notify1_acct_type" id="notify1_acct_type"/>
                                    <input type="hidden" id="notify1City"/>
                                    <input type="hidden" id="notify1State"/>
                                    <input type="hidden" id="notify1Country"/>
                                    <input type="hidden" id="notify1Zip"/>
                                    <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" title="Click here to edit Notify Search options" onclick="showClientSearchOption('${path}', 'Notify')"/>
                                    <input type="hidden" name="notifySearchState" id="notifySearchState"/>
                                    <input type="hidden" name="notifySearchZip" id="notifySearchZip"/>
                                    <input type="hidden" name="notifySearchSalesCode" id="notifySearchSalesCode"/>
                                    <input type="hidden" name="notifySearchCountry" id="notifySearchCountry"/>
                                    <input type="hidden" name="notifyCountryUnLocCode" id="notifyCountryUnLocCode"/>
                                    <c:choose>
                                        <c:when test="${bol.manualNotify1}">
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
                                    <cong:autocompletor name="notify2Contact" template="tradingPartner" id="notify2Contact" query="TRADING_PARTNER_IMPORTS"
                                                        fields="notify2Code,notify2_acct_type,NULL,NULL,NULL,NULL,NULL,notify2Disabled,notify2Address,notify2City,notify2State,notify2Country,notify2Zip,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,notify2ForwardAcct"
                                                        paramFields="notify2SearchState,notify2SearchZip,notify2SearchSalesCode,notify2CountryUnLocCode"
                                                        styleClass="textlabelsBoldForTextBox textCap ${isNotify2Mapped}" value="${bol.notify2Code}" width="600" container="NULL" shouldMatch="true" scrollHeight="200px" callback="fillNotify2Address();notify2_AccttypeCheck();"/>                                    
                                    <html:hidden property="notify2Code" styleId="notify2Code" value="${bol.notify2Code}"/>
                                    <html:text property="manualNotify2" styleClass="textbox" styleId="manualNotify2" value="${bol.notify2Code}"/>
                                    <input type="hidden" name="notify2Disabled" id="notify2Disabled"/>
                                    <input type="hidden" name="notify2ForwardAcct" id="notify2ForwardAcct"/>
                                    <input type="hidden" id="notify2Address"/>
                                    <input type="hidden" id="notify2City"/>
                                    <input type="hidden" id="notify2State"/>
                                    <input type="hidden" id="notify2Country"/>
                                    <input type="hidden" name="notify2_acct_type" id="notify2_acct_type"/>
                                    <input type="hidden" id="notify2Zip"/>
                                    <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" title="Click here to edit 2nd Notify Search options" onclick="showClientSearchOption('${path}', '2nd Notify Party')"/>
                                    <input type="hidden" name="notify2SearchState" id="notify2SearchState"/>
                                    <input type="hidden" name="notify2SearchZip" id="notify2SearchZip"/>
                                    <input type="hidden" name="notify2SearchSalesCode" id="notify2SearchSalesCode"/>
                                    <input type="hidden" name="notify2SearchCountry" id="notify2SearchCountry"/>
                                    <input type="hidden" name="notify2CountryUnLocCode" id="notify2CountryUnLocCode"/>
                                    <c:choose>
                                        <c:when test="${bol.manualNotify2}">
                                            <input type="checkbox" name="chkManualNo2" id="chkManualNo2" checked onchange="toggleAcct('No2');"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" name="chkManualNo2" id="chkManualNo2" onchange="toggleAcct('No2');"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <cong:img src="${path}/jsps/LCL/images/add2.gif" width="16" height="16" alt="display" styleClass="tp" onclick="openTradingPartner('${path}','NO2')"/>
                                </td>

                            </tr>

                            <tr>
                                <td class="label">Name</td>
                                <td>
                                    <input type="text" id="shipperAcctName" class="textlabelsBoldForTextBox" style="width: 190px;" value="${bol.shipperAcctName}"/>
                                </td>
                                <td class="label">Name</td>
                                <td>
                                    <input type="text" id="consAcctName" class="textlabelsBoldForTextBox" style="width: 190px;" value="${bol.consAcctName}"/>
                                </td>
                                <td class="label">Name</td>
                                <td>
                                    <input type="text" id="notify1AcctName" class="textlabelsBoldForTextBox" style="width: 190px;" value="${bol.notify1AcctName}"/>
                                </td>
                                <td class="label">Name</td>
                                <td>
                                    <input type="text" id="notify2AcctName" class="textlabelsBoldForTextBox" style="width: 190px;" value="${bol.notify2AcctName}"/>
                                </td>

                            </tr>

                            <tr>
                                <td class="label"></td>
                                <td>
                                    <html:textarea property="shipperNad" styleId="shipperNad" styleClass="textarea" value="${bol.shipperNad}"
                                                   rows="7" cols="45"/>
                                </td>
                                <td class="label"></td>
                                <td>
                                    <html:textarea property="consNad" styleId="consNad" styleClass="textarea" value="${bol.consNad}"
                                                   rows="7" cols="45"/>
                                </td>
                                <td class="label"></td>
                                <td>
                                    <html:textarea property="notify1Nad" styleId="notify1Nad" styleClass="textarea" value="${bol.notify1Nad}"
                                                   rows="7" cols="45"/>
                                </td>
                                <td class="label"></td>
                                <td>
                                    <html:textarea property="notify2Nad" styleId="notify2Nad" styleClass="textarea" value="${bol.notify2Nad}"
                                                   rows="7" cols="45"/>
                                </td>

                            </tr>
                        </table>
                        <br/>
                        <div id="loc-detail" onclick="toggle('#loc-table');" class="head-tag">
                            Location
                            <span class="green" style="position: absolute; right: 10px; width: 900px; text-align:right;">
                                Click this bar to Expand / Hide
                            </span>
                        </div>
                        <table class="table no-border" id="loc-table">
                            <tr>
                                <td class="label">Pre-carriage By</td>
                                <td>
                                    <html:text property="precarriageBy" styleId="precarriageBy" styleClass="textbox width-200px" value="${bol.preCarriageBy}"/>
                                </td>
                                <td class="label">Place of receipt</td>
                                <td>
                                    <cong:autocompletor name="porUncode" template="tradingPartner" id="porUncode" query="ORIGIN_UNLOC"
                                                        fields="" styleClass="textbox width-40px textCap" value="${bol.porUncode}"
                                                        width="470" container="NULL" shouldMatch="true" scrollHeight="200px"
                                                        callback="getUnloc('#porUncode', '#placeOfReceipt');"/>
                                    <html:text property="placeOfReceipt" styleId="placeOfReceipt" styleClass="textbox width-150px readonly" readonly="true" value="${bol.placeOfReceipt}"/>
                                </td>
                                <td class="label">POL</td>
                                <td>
                                    <cong:autocompletor name="polUncode" template="tradingPartner" id="polUncode" query="ORIGIN_UNLOC_ECULINE"
                                                        fields="polDesc" styleClass="mandatory textbox width-40px textCap" value="${bol.polUncode}"
                                                        width="470" container="NULL" shouldMatch="true" scrollHeight="200px"
                                                        callback="removeBorderRedCol('polUncode');"/>
                                    <html:text property="polDesc" styleId="polDesc" styleClass="textbox width-150px readonly " value="${bol.polDesc}" readonly="true"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="label">POD</td>
                                <td>
                                    <cong:autocompletor name="podUncode" template="tradingPartner" id="podUncode" query="PORT_ECULINE"
                                                        fields="podDesc" styleClass="mandatory textbox width-40px textCap" value="${bol.podUncode}"
                                                        width="470" container="NULL" shouldMatch="true" scrollHeight="200px"
                                                        callback="removeBorderRedCol('podUncode');"/>
                                    <html:text property="podDesc" styleId="podDesc" styleClass="textbox width-150px readonly" readonly="true" value="${bol.podDesc}"/>
                                </td>
                                <td class="label">Place of delivery</td>
                                <td>
                                    <cong:autocompletor name="poddeliveryUncode" template="tradingPartner" id="poddeliveryUncode" query="PORT_ECULINE"
                                                        fields="poddeliveryDesc" styleClass="mandatory textbox width-40px textCap" value="${bol.poddeliveryUncode}"
                                                        width="470" container="NULL" shouldMatch="true" scrollHeight="200px"
                                                        callback="removeBorderRedCol('poddeliveryUncode');"/>
                                    <html:text property="poddeliveryDesc" styleId="poddeliveryDesc" readonly="true" styleClass="textbox width-150px readonly" value="${bol.poddeliveryDesc}"/>
                                </td>
                                <td></td>
                                <td></td>
                            </tr>
                        </table>
                        <br/>
                        <div id="commodity-detail" onclick="toggle('#commodity-table');" class="head-tag">
                            Commodity
                            <span class="green" style="position: absolute; right: 10px; width: 900px; text-align:right;">
                                Click this bar to Expand / Hide
                            </span>
                        </div>
                        <br/>
                        <table class="table no-border" id="commodity-table">
                            <tr>
                                <td>
                                    <%@include file="commodity.jsp" %>
                                </td>
                            </tr>
                        </table>
                        <div id="NoOf-Document" onclick="toggle('#Document-table');" class="head-tag">
                            No of Document
                            <span class="green" style="position: absolute; right: 10px; width: 900px; text-align:right;">
                                Click this bar to Expand / Hide
                            </span>
                        </div>
                        <table class="table no-border" id="Document-table">
                            <tr>
                                <td class="label">No of original </td>
                                <td>
                                    <html:text property="noOfOriginal" styleId="noOfOriginal" styleClass="textbox width-40px border-oliv readonly" value="${bol.noOfOriginal}" readonly="true"/>
                                </td>
                                <td class="label">No of copies </td>
                                <td>
                                    <html:text property="noOfCopies" styleId="noOfCopies" styleClass="textbox width-40px border-oliv readonly" value="${bol.noOfCopies}" readonly="true"/>
                                </td>
                                <td width="80%"></td>
                            </tr>
                        </table>
                        <html:hidden styleId="id" property="id" value="${bol.blId}"/>
                        <html:hidden styleId="methodName" property="methodName"/>
                        <html:hidden styleId="unitApproved" property="unitApproved" value="${bol.unitApproved}"/>
                        <input type="hidden" name="ecuShipperAdd" id="ecuShipperAdd"  value="${bol.shipperNad}"/>
                        <input type="hidden" name="ecuConsigneeAdd" id="ecuConsigneeAdd"  value="${bol.consNad}"/>
                        <input type="hidden" name="ecuNotify1Add" id="ecuNotify1Add"  value="${bol.notify1Nad}"/>
                        <input type="hidden" name="ecuNotify2Add" id="ecuNotify2Add"  value="${bol.notify2Nad}"/>
                        <input type="hidden" name="ecuShipCode" id="ecuShipCode"  value="${bol.shipperCode}"/>
                        <input type="hidden" name="ecuConCode" id="ecuConCode"  value="${bol.consCode}"/>
                        <input type="hidden" name="ecuNotify1Code" id="ecuNotify1Code"  value="${bol.notify1Code}"/>
                        <input type="hidden" name="ecuNotify2Code" id="ecuNotify2Code"  value="${bol.notify2Code}"/>
                        <input type="hidden" name="ecuPOLDesc" id="ecuPOLDesc"  value="${bol.polDesc}"/>
                        <input type="hidden" name="ecuPolUncode" id="ecuPolUncode"  value="${bol.polUncode}"/>
                        <input type="hidden" name="ecuPODDesc" id="ecuPODDesc"  value="${bol.podDesc}"/>
                        <input type="hidden" name="ecuPodUncode" id="ecuPodUncode"  value="${bol.podUncode}"/>
                        <input type="hidden" name="ecuDeliverDesc" id="ecuDeliverDesc"  value="${bol.poddeliveryDesc}"/>
                        <input type="hidden" name="ecuPoddeliveryUncode" id="ecuPoddeliveryUncode"  value="${bol.poddeliveryUncode}"/>
                        <br/>
                        <c:if test="${bol.approved eq '0'}">
                            <input type="button" class="button" value="Save" onclick="updateBol();"/>
                        </c:if>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html>
