<%-- 
    Document   : addOrEditBlCorrection
    Created on : Jan 6, 2016, 11:00:44 PM
    Author     : Mei
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="../../colorBox.jsp" %>
<%@include file="../../../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/export/blCorrection.js"></cong:javascript>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Add/Edit Correction</title>
        </head>
        <body>
        <cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="/blCorrection.do">

            <cong:hidden id="searchFileNo" name="searchFileNo"/>
            <cong:hidden id="searchCorrectionCode" name="searchCorrectionCode"/>
            <cong:hidden id="searchDate" name="searchDate"/>
            <cong:hidden id="searchShipName" name="searchShipName"/>
            <cong:hidden id="searchShipperNo" name="searchShipperNo"/>
            <cong:hidden id="searchForwarderName" name="searchForwarderName"/>
            <cong:hidden id="searchForwarderNo" name="searchForwarderNo"/>
            <cong:hidden id="searchThirdPartyAcctName" name="searchThirdPartyAcctName"/>
            <cong:hidden id="searchThirdPartyAcctNo" name="searchThirdPartyAcctNo"/>
            <cong:hidden id="searchNoticeNo" name="searchNoticeNo"/>
            <cong:hidden id="searchPooName" name="searchPooName"/>
            <cong:hidden id="searchPooId" name="searchPooId"/>
            <cong:hidden id="searchPolName" name="searchPolName"/>
            <cong:hidden id="searchPolId" name="searchPolId"/>
            <cong:hidden id="searchPodName" name="searchPodName"/>
            <cong:hidden id="searchPodId" name="searchPodId"/>
            <cong:hidden id="searchFdName" name="searchFdName"/>
            <cong:hidden id="searchFdId" name="searchFdId"/>
            <cong:hidden id="searchCreatedBy" name="searchCreatedBy"/>
            <cong:hidden id="searchCreatedByUserId" name="searchCreatedByUserId"/>
            <cong:hidden id="searchApprovedBy" name="searchApprovedBy"/>
            <cong:hidden id="searchApproveByUserId" name="searchApproveByUserId"/>
            <cong:hidden id="filterBy" name="filterBy"/>
            <cong:hidden id="searchBlNo" name="searchBlNo"/>



            <cong:hidden id="fileNo" name="fileNo" />
            <cong:hidden id="blNo" name="blNo" />
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="formChangedVal" name="formChangedVal"/>

            <cong:hidden id="screenName" name="screenName"/>
            <cong:hidden name="filterBy" id="filterBy"/>

            <cong:hidden id="fileId" name="fileId" />
            <cong:hidden id="correctionId" name="correctionId" />
            <cong:hidden id="customerAcctName" name="customerAcctName" />
            <cong:hidden id="customerAcctNo" name="customerAcctNo" />
            <cong:hidden id="creditMemoEmail" name="creditMemoEmail" />
            <cong:hidden id="debitMemoEmail" name="debitMemoEmail" />
            <cong:hidden id="viewMode" name="viewMode" />
            <cong:hidden id="billingType" name="billingType" value="${lclCorrectionForm.billingType}"/>
            <cong:hidden id="partyNo" name="partyNo" value="${lclCorrectionForm.customerAcctNo}"/>

            <cong:hidden id="chargeId" name="chargeId" />
            <cong:hidden id="oldAmount" name="oldAmount" />
            <cong:hidden id="newAmount" name="newAmount" />
            <cong:hidden id="billToParty" name="billToParty" />
            <cong:hidden id="correctionChargeId" name="correctionChargeId" />
            <cong:hidden id="currentProfit" name="currentProfit" />
            <cong:hidden id="profitAfterCN" name="profitAfterCN" />
            <cong:hidden id="lclBookingAcId" name="lclBookingAcId" />

            <input type="hidden" name="blThirdPartyAcctNo" id="blThirdPartyAcctNo" value="${bl.thirdPartyAcct.accountno}"/>
            <input type="hidden" name="blforwarderNo" id="blforwarderNo" value="${bl.fwdAcct.accountno}"/>
            <input type="hidden" name="blshipAcct" id="blshipAcct" value="${bl.shipAcct.accountno}"/>
            <input type="hidden" name="blagentNo" id="blagentNo" value="${bl.agentAcct.accountno}"/>
            <input type="hidden" name="blBillToParty" id="blBillToParty" value="${bl.billToParty}"/>
            <cong:hidden id="previousNewAmount" name="previousNewAmount"/>
            <table border="0">
                <tr>
                    <td>
                        <table class="tableBorderNew">
                            <tr class="textlabelsBold">
                                <td>BL NO:</td>
                                <td>
                                    <b class="headerlabel" style="color:blue;">
                                        ${lclCorrectionForm.blNo}
                                    </b>
                                </td>
                            </tr></table>
                    </td>
                    <td>
                        <table class="tableBorderNew">
                            <tr class="textlabelsBold">
                                <c:if test="${not  empty lclCorrection.correctionNo}">
                                    <td>CN #</td>
                                    <td>
                                        <b class="headerlabel" style="color:blue;">
                                            <c:out value="${lclCorrection.correctionNo}"/></b>
                                    </td>
                                </c:if>
                            </tr></table>
                    </td>
                    <td>
                        <table class="tableBorderNew">
                            <tr class="textlabelsBold">
                                <c:if test="${not  empty lclCorrection.enteredBy}">
                                    <td>Created BY:</td>
                                    <td>
                                        <b class="headerlabel" style="color:blue;">
                                            <c:out value="${lclCorrection.enteredBy.loginName}"/>
                                        </b>
                                    </td>
                                </c:if>
                            </tr>
                        </table>
                    </td>
                    <td>
                        <table class="tableBorderNew">
                            <tr class="textlabelsBold">
                                <c:if test="${not  empty lclCorrection.enteredDatetime}">
                                    <td>On:</td>
                                    <td>
                                        <b class="headerlabel" style="color:blue;">
                                            <fmt:formatDate pattern="dd-MMM-yyyy hh:mm" var="corrCreatedTime" value="${lclCorrection.enteredDatetime}"/>
                                            <c:out value="${corrCreatedTime}"/>
                                        </b>
                                    </td>
                                </c:if>
                            </tr>
                        </table>
                    </td>
                    <td>
                        <c:set var="statusLabel" value="${not empty lclCorrection.postedDate ? 'Approved,Posted' : lclCorrection.status eq 'A' ? 'Approved' : ''} "/>
                        <span class="redBold">${statusLabel}</span>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td width="75%">
                        ${not empty lclCorrection.id ? "Edit" :"Add"} Corrections <span style="color: red;">&nbsp;${lclCorrectionForm.fileNo}</span>
                    </td>
                    <td width="25%">
                        <input type="button" value="Save" align="center" id="saveCorreBtn"  class="button-style1" onclick="saveCorrection();"/>
                        <input type="button" value="Go Back" align="center"  class="button-style1"onclick="goBackCorrection();"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table width="100%" border="0">
                            <tr>
                                <td style="padding-top: 0.8em;" class="textlabelsBoldforlcl">Origin</td>
                                <td style="padding-top: 0.8em;">
                                    <cong:text name="origin" id="origin"  readOnly="true"  styleClass="textlabelsBoldForTextBoxDisabledLook" />
                                </td >
                                <td style="padding-top: 0.8em;"class="textlabelsBoldforlcl">Destination</td>
                                <td style="padding-top: 0.8em;">
                                    <cong:text name="destination" id="destination"  readOnly="true"
                                               styleClass="BackgrndColorForTextBox" />
                                </td>
                                <td style="padding-top: 0.8em;"class="textlabelsBoldforlcl">Sail Date</td>
                                <td style="padding-top: 0.8em;">
                                    <input class="BackgrndColorForTextBox" value="${pickedVoyage.etaSailDate}" readonly="readonly"/>
                                </td>

                            </tr>
                            <tr>
                                <td style="padding-top: 0.8em;"class="textlabelsBoldforlcl">Third Party</td>
                                <td style="padding-top: 0.8em;">
                                    <cong:autocompletor  name="expThirdPartyName" styleClass="text-readonly" id="expThirdPartyName" container="null" readOnly="true"
                                                         fields="expThirdPartyNo,NULL,NULL,NULL,NULL,NULL,NULL,null,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,null,null,NULL,thirdPartyDisabledAcct"
                                                         shouldMatch="true" width="600" query="THIRD_PARTY_BL" template="tradingPartner" scrollHeight="300"
                                                         callback="validateAcct('thirdPartyDisabledAcct','expThirdPartyNo','expThirdPartyName');" />
                                    <input type="hidden" id="thirdPartyDisabledAcct"/>
                                    <input type="hidden" id="oldThirdParty" 
                                           value="${not empty lclCorrection.thirdPartyAcct ? lclCorrection.thirdPartyAcct.accountno : bl.thirdPartyAcct.accountno}"/>
                                    <cong:text id="expThirdPartyNo" style="width:70px" readOnly="true" name="expThirdPartyNo"
                                               styleClass="textlabelsBoldForTextBoxDisabledLook" />
                                </td> 
                                <td style="padding-top: 0.8em;" class="textlabelsBoldforlcl">Agent</td>
                                <td style="padding-top: 0.8em;">
                                    <cong:autocompletor  name="expAgentName" styleClass="text-readonly" id="expAgentName" container="null"
                                                         fields="expAgentNo,NULL,NULL,NULL,NULL,NULL,NULL" readOnly="true"
                                                         shouldMatch="true" width="600" query="TRADING_AGENT" template="tradingPartner" scrollHeight="300"
                                                         params="${bl.portOfDestination.unLocationCode}" />
                                    <input type="hidden" id="oldAgent" 
                                           value="${not empty lclCorrection.oldAgent ? lclCorrection.oldAgent.accountno : bl.agentAcct.accountno}"/>
                                    <cong:text id="expAgentNo" style="width:70px" readOnly="true" name="expAgentNo"
                                               styleClass="textlabelsBoldForTextBoxDisabledLook" />
                                </td>
                                <td style="padding-top: 0.8em;" class="textlabelsBoldforlcl">Voyage</td>
                                <td style="padding-top: 0.8em;">
                                    <input class="BackgrndColorForTextBox" value="${pickedVoyage.scheduleNo}" readonly="readonly"/>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding-top: 0.8em;" class="textlabelsBoldforlcl">Shipper</td>
                                <td style="padding-top: 0.8em;">
                                    <cong:autocompletor  name="expShipperName" styleClass="text-readonly" id="expShipperName" container="null" readOnly="true"
                                                         fields="expShipperNo,null,null,NULL,NULL,NULL,NULL,null,null,null,null,null,null,null,null,null,null,null,NULL,NULL,null,NULL,shipperDisabledforblAcct"
                                                         shouldMatch="true" width="600" query="SHIPPER" template="tradingPartner" scrollHeight="300" 
                                                         callback="validateAcct('shipperDisabledforblAcct','expShipperNo','expShipperName');" />
                                    <input type="hidden" id="shipperDisabledforblAcct"/>
                                    <input type="hidden" id="oldShipper" 
                                           value="${not empty lclCorrection.oldShipper ? lclCorrection.oldShipper.accountno : bl.shipAcct.accountno}"/>
                                    <cong:text id="expShipperNo" style="width:70px" readOnly="true" name="expShipperNo"
                                               styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                                </td>
                                <td style="padding-top: 0.8em;" class="textlabelsBoldforlcl">F/F #</td>
                                <td style="padding-top: 0.8em;">
                                    <cong:autocompletor  name="expForwarderName" styleClass="text-readonly" id="expForwarderName" container="null" readOnly="true"
                                                         fields="expForwarderNo,null,null,NULL,NULL,NULL,NULL,null,null,null,null,null,null,NULL,null,null,null,null,null,null,frwddisabledforblAcct"
                                                         shouldMatch="true" width="600" query="FORWARDER_BL" template="tradingPartner" scrollHeight="300" 
                                                         callback="validateAcct('frwddisabledforblAcct','expForwarderNo','expForwarderName');"/>
                                    <input type="hidden" id="frwddisabledforblAcct"/>
                                    <input type="hidden" id="oldForwarder" 
                                           value="${not empty lclCorrection.oldForwarder ? lclCorrection.oldForwarder.accountno : bl.fwdAcct.accountno}"/>
                                    <cong:text id="expForwarderNo" style="width:70px" readOnly="true" name="expForwarderNo"
                                               styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                                </td>
                                <td style="padding-top: 0.8em;" class="textlabelsBoldforlcl">File No</td>
                                <td style="padding-top: 0.8em;">
                                    <html:text property="fileNo"  styleClass="BackgrndColorForTextBox" 
                                               value="${lclCorrectionForm.fileNo}" readonly="true"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="5"></td>
                            </tr>
                            <tr><td><br/></td></tr>
                            <tr>
                                <td class="textlabelsBoldforlcl" colspan="1">Originally --></td>
                                <td valign="baseline" colspan="5" class="textlabelsBold">
                                    ${bl.billingType eq 'P' ? 'Prepaid' : bl.billingType eq 'C' ? 'Collect' :'Both'}
                                    By
                                    <font color="Blue">${lclCorrectionForm.billingType}</font>
                                    <font color="red"> ${lclCorrectionForm.customerAcctName} ${lclCorrectionForm.customerAcctNo}
                                    </font></td>
                            </tr>
                            <tr><td><br/></td></tr>
                            <tr>
                                <td colspan="8">
                                    <div id="memoDivSection">
                                        <table>
                                            <tr>
                                                <td class="textlabelsBold">Credit Memo To</td>
                                                <td>
                                                    <div id="creditMemoEmailDiv" class="divstyleThin">
                                                        <c:forEach var="arCreditEmailId" items="${creditEmailList}" varStatus="email">
                                                            <c:if test="${not empty arCreditEmailId}">
                                                                <input type="checkbox" class="creditEmailId" checked ${email.count eq 1 ? 'disabled' : ''} value="${arCreditEmailId}"/>${arCreditEmailId}<br>
                                                            </c:if>
                                                        </c:forEach>
                                                    </div>
                                                </td>
                                                <td class="textlabelsBold">Debit Memo To</td>
                                                <td>
                                                    <div id="debitMemoEmailDiv" class="divstyleThin">
                                                        <c:forEach var="arDebitEmailId" items="${debitEmailList}" varStatus="fax">
                                                            <c:if test="${not empty arDebitEmailId}">
                                                                <input type="checkbox" class="debitEmailId" checked ${fax.count eq 1 ? 'disabled' : ''} value="${arDebitEmailId}"/>${arDebitEmailId}<br>
                                                            </c:if>
                                                        </c:forEach>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                            <tr><td><br/></td></tr>
                            <tr class="textlabelsBold">
                                <td>Comments<b  class="mandatoryStarColor">*</b></td>
                                <td colspan="2">
                                    <html:textarea styleClass="textlabelsBoldForTextBox" property="comments" rows="4" cols="60"
                                                   value="${lclCorrection.comments}"  style="text-transform: uppercase"/>
                                </td>
                                <td colspan="4" valign="top">
                                    <input type="hidden" id="originNo" name="originNo" value=""/>
                                    <input type="hidden" id="destinationNo" name="destinationNo" value=""/>
                                    <table border="0" class="dataTable">
                                        <thead>
                                        <th>Tariff</th>
                                        <th>Tariff#</th>
                                        <th>KGS</th>
                                        <th>CBM</th>
                                        <th>LBS</th>
                                        <th>CFT</th>
                                        <th></th>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${lclCorrectionForm.commodityList}" var="piece" varStatus="count">    
                                                <tr>
                                                    <td>
                                                        <cong:autocompletor name="commodityList[${count.index}].commodityType.descEn" id="commodityType${count.index}" template="two" 
                                                                            fields="commodityNo${count.index},hazmat${count.index},commodityTypeId${count.index}" 
                                                                            query="COMMODITY_TYPE_NAME" width="500" styleClass="commodity_name textLCLuppercase" 
                                                                            value="${piece.commodityType.descEn}" container="NULL" scrollHeight="200" callback="setCommonValues(${count.index});"  
                                                                            paramFields="originNo,destinationNo" focusOnNext="true" shouldMatch="true" readOnly="true"/>
                                                    </td>
                                                    <td>
                                                        <cong:autocompletor name="commodityList[${count.index}].commodityType.code" id="commodityNo${count.index}" template="two" 
                                                                            fields="commodityType${count.index},hazmat${count.index},commodityTypeId${count.index}" 
                                                                            query="COMMODITY_TYPE_CODE" width="500" styleClass="text commodity_number" 
                                                                            value="${piece.commodityType.code}" container="NULL" position="left"
                                                                            scrollHeight="200" focusOnNext="true" shouldMatch="true" readOnly="true"/>
                                                        <cong:hidden name="commodityList[${count.index}].piece.commodityType.id"  
                                                                     id="commodityTypeId${count.index}"  value="${piece.commodityType.id}"/>
                                                    </td>
                                                    <td>
                                                        <cong:text name="commodityList[${count.index}].kgs" id="KGS${count.index}" 
                                                                   onkeyup ="checkForNumberAndDecimal(this);ConvertToLBS(this,'${count.index}')"
                                                                   style="width:50px;" styleClass="text commodity_number" readOnly="true" value="${piece.kgs}"/>
                                                    </td><td>
                                                        <cong:text name="commodityList[${count.index}].cbm" id="CBM${count.index}" 
                                                                   onkeyup ="checkForNumberAndDecimal(this);ConvertToCFT(this,'${count.index}')"
                                                                   style="width:50px;" styleClass="text commodity_number" readOnly="true" value="${piece.cbm}"/>
                                                    </td><td>
                                                        <cong:text name="commodityList[${count.index}].lbs" id="LBS${count.index}"  
                                                                   onkeyup ="checkForNumberAndDecimal(this);ConvertToKGS(this,'${count.index}')" 
                                                                   style="width:50px;" styleClass="text commodity_number" readOnly="true" value="${piece.lbs}"/>
                                                    </td>
                                                    <td>
                                                        <cong:text name="commodityList[${count.index}].cft" id="CFT${count.index}" 
                                                                   onkeyup ="checkForNumberAndDecimal(this);ConvertToCBM(this,'${count.index}')"
                                                                   style="width:50px;" styleClass="text commodity_number" readOnly="true" value="${piece.cft}"/>

                                                    </td>
                                                    <td>
                                                        <cong:hidden name="commodityList[${count.index}].pieceCount"  
                                                                     id="pieceCount${count.index}"  value="${piece.pieceCount}"/>
                                                        <cong:hidden name="commodityList[${count.index}].hazmat"  
                                                                     id="hazmat${count.index}"  value="${piece.hazmat}"/>
                                                        <cong:hidden name="commodityList[${count.index}].barrel"  
                                                                   id="barrel${count.index}"  value="${piece.barrel}"/>
                                                        
                                                        <input type="hidden" id="oldCommodityType${count.index}" value="${piece.commodityType.descEn}"/>
                                                        <input type="hidden" id="oldCommodityNo${count.index}" value="${piece.commodityType.code}"/>
                                                        <input type="hidden" id="oldKGS${count.index}" value="${piece.kgs}"/>
                                                        <input type="hidden" id="oldCBM${count.index}" value="${piece.cbm}"/>
                                                        <input type="hidden" id="oldLBS${count.index}" value="${piece.lbs}"/>
                                                        <input type="hidden" id="oldCFT${count.index}" value="${piece.cft}"/>
                                                        <c:if test="${empty lclCorrection.id}">                                                     
                                                            <input type="radio" name="commodity" id="commodity${count.index}" value="${count.index}"
                                                                   onclick="enableCommodity('${count.index}')" class="commodityRadio"/>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            <tr><td><br/></td></tr>
                            <tr class="textlabelsBold">
                                <td>C/N Type<b class="mandatoryStarColor">*</b></td>
                                <td colspan="5">
                                    <html:select property="correctionType" value="${lclCorrection.type.id}"
                                                 styleId="correctionTypeId"  styleClass="dropdown_accounting" style="width:481px;">
                                        <html:optionsCollection name="correctionTypeList"/>
                                    </html:select>
                                    <input type="button" class="buttonStyleNew" id="addCharges" value="Add Charges"
                                           onclick="addCorrectionCharge('${path}', '${lclCorrection.id}');" style="width:80px;" />
                                    <c:if test="${empty lclCorrection.id}">
                                        <input type="button" class="buttonStyleNew" id="calculateCharges" value="Continue"
                                               onclick="calculateCorrectedCharges('${path}', '${lclCorrection.id}');" style="width:80px;" />
                                    </c:if>
                                </td>
                                <td >
                                    <font class="green"> Current Profit     :</font>
                                    <font class="red-90"><label id="lblCurrentProfit">
                                            ${"viewCorrections" eq lclCorrectionForm.methodName ?  
                                               lclCorrection.currentProfit : lclCorrectionForm.currentProfit}
                                        </label>
                                    </font>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>C/N Code<b class="mandatoryStarColor">*</b></td>
                                <td colspan="5">
                                    <html:select property="correctionCode" styleClass="dropdown_accounting"
                                                 value="${lclCorrection.code.id}"
                                                 styleId="correctionCodeId" style="width:481px;" onchange="setEnableCode();">
                                        <html:optionsCollection name="correctionCodeList"/>
                                    </html:select>
                                </td>
                                <td>
                                    <font class="green"> Profit after C/N : </font><font class="red-90">
                                        <label id="lblProfitAfterCn">
                                            ${"viewCorrections" eq lclCorrectionForm.methodName ?  
                                               lclCorrection.profitAfterCN : lclCorrectionForm.profitAfterCN }
                                        </label>
                                    </font>
                                </td>
                                <td align="center"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <c:if test="${not empty lclCorrectionChargesList}">
                <display:table class="dataTable" name="${lclCorrectionChargesList}" id="chargesTable">
                    <display:column title="Charge Code" style="width:100Px;">${chargesTable.chargeCode}</display:column>
                    <display:column title="Charge Desc" style="width:200Px;">${chargesTable.chargeDescriptions}</display:column>
                    <display:column title="Bill To Party" style="width:200Px;">${chargesTable.billToPartyLabel}</display:column>
                    <display:column title="Amount" style="width:100Px;">${chargesTable.oldAmount}</display:column>
                    <display:column title="New Amount" style="width:100Px;">${chargesTable.newAmount}</display:column>
                    <display:column title="Difference" style="width:100Px;">${chargesTable.differenceAmount}</display:column>
                    <display:column title="Action" style="width:80Px;">
                        <c:if test="${lclCorrectionForm.viewMode!='view'}">
                            <img src="${path}/images/edit.png"  style="cursor:pointer" class="correctionCharge" width="13" height="13" alt="edit"
                                 onclick="editCorrectionCharges('${path}', '${chargesTable.chargeId}', '${chargesTable.chargeCode}',
                                                 '${chargesTable.chargeDescriptions}', '${chargesTable.oldAmount}',
                                                 '${chargesTable.newAmount}', '${chargesTable.differenceAmount}', '${lclCorrection.id}',
                                                 '${chargesTable.billToPartyLabel}', '${chargesTable.correctionChargeId}',
                                                 '${chargesTable.lclBookingAcId}');"
                                 />
                        </c:if>
                        <c:if test="${chargesTable.delete && lclCorrectionForm.viewMode!='view'}">
                            <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete"
                                 onclick="deleteCorrectionCharges('${chargesTable.correctionChargeId}');" />
                        </c:if>
                    </display:column>
                </display:table>
            </c:if>
        </cong:form>
    </body>
    <style type="text/css">
        .commodity_name{
            border: 0px;
            background-color:#CCEBFF;
            font-weight:bold;
            font-family:Arial;
            color: #333333;
            font-size: 10px;
            width:200px;
        }
        .commodity_number{
            border: 0px;
            background-color:#CCEBFF;
            font-weight:bold;
            font-family:Arial;
            color: #333333;
            font-size: 10px;
            width:50px;
        }
        .commodity_name_enable{
            font-weight: bold;
            font-family: Arial;
            color: #333333;
            font-size: 11px;
            width:200px;
        }
        .commodity_number_enable{
            font-weight: bold;
            font-family: Arial;
            color: #333333;
            font-size: 11px;
            width:50px;
        }
    </style>
</html>
