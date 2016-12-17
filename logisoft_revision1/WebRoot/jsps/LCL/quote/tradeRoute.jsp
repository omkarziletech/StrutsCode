<%@page import="com.gp.cong.logisoft.domain.lcl.LclQuote"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<table>
    <tr class="caption">       
        <td colspan="3" align="center">Trade Route</td>
    </tr>
    <tr>
    <cong:td width="1%" >
        <cong:table width="100%" cellpadding="0" cellspacing="0" border="0">
            <cong:td valign="middle" styleClass="textlabelsBoldforlcl">CTC/Retail/FTF
                <span style="color:red;font-size: 19px;vertical-align: middle">*</span></cong:td>
            <cong:td  styleClass="textBoldforlcl"><cong:radio value="C" name="rateType" id="ctc" container="NULL" onclick="fillaesbyQtRateType();
                    lockQuoteInsurance();
                    clearAllRates('AutoRates will be removed.Manual Rates will be recalculated.Are you sure you want to delete?', $('#fileNumberId').val());
                    checkOriginTerminal();"/> C
                <cong:radio value="R" name="rateType" id="rateR" container="NULL" onclick="fillaesbyQtRateType();
                        lockQuoteInsurance();
                        clearAllRates('AutoRates will be removed.Manual Rates will be recalculated.Are you sure you want to delete?', $('#fileNumberId').val());
                        clearColoadRates($('#fileNumberId').val());
                        checkOriginTerminal();"/> R
                <cong:radio value="F" name="rateType" id="rateF" container="NULL" onclick="fillaesbyQtRateType();
                        lockQuoteInsurance();
                        clearAllRates('AutoRates will be removed.Manual Rates will be recalculated.Are you sure you want to delete?', $('#fileNumberId').val());
                        checkOriginTerminal();"/> FTF
                <cong:hidden name="rateTypes" value="${lclQuote.rateType}" id="rateTypes"/>
            </cong:td>
            <cong:tr>
                <cong:td valign="middle" width="5%" styleClass="textlabelsBoldforlcl"> Pickup Y/N</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <cong:radio value="Y" name="pooDoor" id="doorOriginY" container="NULL" onclick="showPickupButton();"/> Yes
                    <cong:radio value="N" name="pooDoor" id="doorOriginN" container="NULL" onclick="changeDoorOrigin();"/> No</cong:td>
                <cong:td align="left">
                    <div class="button-style1 pickupInfo" align="center" style="float:left;"
                         id="pickupInf" onclick="showQuotePickupInfo('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}', 'PickUp');">Pickup Info</div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" width="5%">Door Origin/City/Zip </cong:td>
                <cong:td>
                    <c:set var="doorOriginCityZipClass" value="${lclQuote.pooDoor ? 'textlabelsBoldForTextBox':'textlabelsBoldForTextBoxDisabledLook'}"/>
                    <c:set var="readOnly" value="${lclQuote.pooDoor ? 'true':'false'}"/>
                    <cong:autocompletor styleClass="${doorOriginCityZipClass}" readOnly="${readOnly}" id="doorOriginCityZip" name="doorOriginCityZip" scrollHeight="200"
                                        query="CONCAT_CITY" fields="pickZip,NULL,NULL" template="concatOrigin" container="NULL" width="400" shouldMatch="true" callback="changeOrigin()" value="${lclQuoteForm.doorOriginCityZip}"/>
                    <cong:hidden name="duplicateDoorOrigin" id="duplicateDoorOrigin" value="${lclQuoteForm.doorOriginCityZip}"/>
                    <cong:hidden name="pickZip" id="pickZip" />
                    <cong:text name="manualDoorOriginCityZip" id="manualDoorOriginCityZip" 
                               value="${lclQuoteForm.doorOriginCityZip}" onkeyup="notAllowSpecialChar(this);" styleClass="textlabelsBoldForTextBox"/>
                </cong:td>
                <cong:td>
                    <input type="checkbox" id="checkDoorOriginCityZip" name="checkDoorOriginCityZip"  onclick="showManualDoorCity(this);"
                           title="Make pickup field free form text for Canada pickups <br>(CTS rates will not work)"  
                           ${not empty lclQuoteForm.doorOriginCityZip && fn:indexOf(lclQuoteForm.doorOriginCityZip,"/") < 0 ? 'checked' :' '}/>
                    <img src="${path}/img/map.png" id="lclDoorOriginCityMap" align="" width="12" height="12" title="Google Map Search"
                         onclick="getDoorOriginCityGoogleMap('${path}')" />
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" width="5%">Origin CFS</cong:td>
                <cong:td width="5%">
                    <cong:autocompletor id="portOfOriginR" name="portOfOrigin" 
                                        callback="if($('#finalDestinationId').val()!=''){calculateCharge('', '#doorOriginCityZip', '#pickupReadyDate');};if($('#portOfDestination').val()==''){submitAjaxForm('refreshAgent','#lclQuoteForm','#m','');}; checkOriginTerminal();setRelayDetails('Origin'); quoteDeliverCargo();setCfclValues();"
                                        template="one" fields="NULL,NULL,originUnlocationCode,portOfOriginId,warehsterminalNo" query="RELAY_ORIGIN" styleClass="mandatory textlabelsBoldForTextBox"
                                        width="400" container="NULL" paramFields="finalDestinationId" value="${lclQuoteForm.portOfOrigin}" shouldMatch="true" scrollHeight="200"/>
                    <cong:hidden name="originUnlocationCode" id="originUnlocationCode" value="${lclQuoteForm.originUnlocationCode}"/>
                    <cong:hidden name="portOfOriginId" id="portOfOriginId" value="${lclQuoteForm.portOfOriginId}"/>
                    <input type="hidden" id="warehsterminalNo" name="warehsterminalNo" value="${lclQuote.portOfOrigin.terminal.trmnum}"/>
                    <cong:checkbox name="showFullRelay" id="showFullRelay" onclick="showHideRelay();" title="Show all Origin Cities"/>
                    <img src="${path}/img/map.png" id="lclOriginMap" width="12px" height="12px" title="Google Map Search"
                         onclick="getOriginGoogleMap('${path}', 'displayOriginMap', 'E')" alt="mapIconOrigin" />
                    <span id="showAstar" style="visibility: hidden">
                        <img src="${path}/img/icons/astar.gif" alt="clearRatesIcon" width="12" height="12" id="clearRates"
                             title="Change Origin"  onclick="clearAllValues('This will allow to change the Origin but will remove all the existing rates.Continue?', $('#fileNumberId').val(), 'showAstarOrigin');"/>&nbsp;
                    </span>
                </cong:td>
            </cong:tr>
            <cong:td colspan="2">
                <cong:table id="polPod">
                    <jsp:include page="/jsps/LCL/ajaxload/polPod.jsp"/>
                </cong:table>
                <cong:hidden name="polCode" id="polCode" value="${polCode}"/>
                <cong:hidden name="podCode" id="podCode" value="${podCode}"/>
            </cong:td>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Destination</cong:td>
                <cong:td>
                    <cong:autocompletor id="finalDestinationR" name="finalDestination" template="one" fields="unlocationName,NULL,unlocationCode,finalDestinationId"  query="CONCAT_RELAY_NAME_FD" paramFields="portOfOriginId"
                                        callback="if(isLockPort('Quote')){setRelayDetails('Des');submitAjaxForm('refreshAgent','#lclQuoteForm','#m','');lockQuoteInsurance();calculateRates();}setCfclValues();"
                                        styleClass="mandatory textlabelsBoldForTextBox"  width="400" container="NULL" value="${lclQuoteForm.finalDestination}" shouldMatch="true" scrollHeight="200"/>
                    <cong:hidden name="finalDestinationId" id="finalDestinationId" value="${lclQuoteForm.finalDestinationId}"/>
                    <cong:hidden name="unlocationCode" id="unlocationCode" value="${lclQuoteForm.unlocationCode}"/>
                    <input type="hidden" name="unknownDest" id="unknownDest" value="${unknownDest}">
                    <input type="hidden" name="unknownDestId" id="unknownDestId" value="${unknownDestId}">
                    <cong:checkbox name="basedOnCity" id="basedOnCity" onclick="showRelayFd();" title="Checked=Look by City Name<br>UnChecked=Look by Country"/>
                    <img src="${path}/img/map.png" id="lclDestinationMap" width="12" height="12" title="Google Map Search"
                         onclick="getDestinationGoogleMap('${path}', 'displayDestinationMap', 'E')" />
                    <span valign="middle" id="showAstarDestn" style="visibility: hidden"><img src="${path}/img/icons/astar.gif" width="12" height="12" id="clearRatesDestn" title="Change Destination" onclick="clearAllValues('This will allow to change the  Destination but will remove all the existing rates.Continue?', $('#fileNumberId').val(), 'showAstarDestn');"/>
                    </span>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td valign="middle" styleClass="textlabelsBoldforlcl" width="5%"> Relay Ovr</cong:td>
                <cong:td styleClass="textBoldforlcl">
                    <cong:radio value="Y" name="relayOverride" id ="qutRelayOverYes" container="NULL" onclick="setRelayOverRideYes();setRelayDetails('relayOverride');"/> Yes
                    <cong:radio value="N" name="relayOverride" id ="qutRelayOverNo" container="NULL" 
                                onclick="changeQtRelay();
                                        setRelay();
                                        RecalculateRelayCharge('', '#doorOriginCityZip', '#pickupReadyDate')"/> No
                </cong:td>
            </cong:tr>
        </cong:table>
    </cong:td>
    <cong:td width="30%" valign="top">
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td class="textlabelsBoldforlcl">Deliver Cargo to</td>
                <td>
            <cong:autocompletor id="deliveryCargoToCode" name="whsCode" query="EXPORT_DELIVER_CARGO" styleClass="smallTextlabelsBoldForTextBox"
                                fields="deliverCargoToName,deliverCargoToAddress,deliverCargoToCity,deliverCargoToState,deliverCargoToZip,deliverCargoToPhone,deliverCargoToFax"
                                template="delwhse" container="NULL" width="400" shouldMatch="true" scrollHeight="200" value="${lclQuoteForm.whsCode}" callback="validateInlandRates();"/>
            <cong:text name="pooWhseContact.companyName"  style="width:205px" id="deliverCargoToName"
                       styleClass="text textlabelsBoldForTextBox textCap"/>
            </td>
            </tr>
            <tr>
                <td class="td textlabelsBoldforlcl" width="300px">Address</td>
                <td>
            <cong:textarea cols="3" rows="15" styleClass="smallDeliTextarea textlabelsBoldForTextBox"
                           id="deliverCargoToAddress" name="pooWhseContact.address" value='${lclQuote.pooWhseContact.address}'>
            </cong:textarea>
            </td>
            </tr>
            <tr>
                <td  class="textlabelsBoldforlcl">City</td>
                <td>
            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="deliverCargoToCity" style="width:100px"
                       name="pooWhseContact.city" value="${lclQuote.pooWhseContact.city}" maxlength="50"/>
            <span class="textlabelsBoldforlcl">&nbsp;&nbsp;State</span>
            <cong:text styleClass="text textlabelsBoldForTextBox textCap" style="width:100px"  id="deliverCargoToState"
                       name="pooWhseContact.state" value="${lclQuote.pooWhseContact.state}" maxlength="50"/>
            </td>
            </tr>
            <tr>
                <td  class="textlabelsBoldforlcl">Zip</td>
                <td>
            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="deliverCargoToZip" style="width:100px"
                       name="pooWhseContact.zip" value="${lclQuote.pooWhseContact.zip}" maxlength="50"/>
            <span class="textlabelsBoldforlcl"> Phone</span>
            <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="deliverCargoToPhone" name="pooWhseContact.phone1" style="width:100px"
                       value="${lclQuote.pooWhseContact.phone1}" maxlength="50"/>
            </td>
            <cong:hidden id="deliverCargoToFax" name="pooWhseContact.fax1" value="${lclQuote.pooWhseContact.fax1}"/>
            </tr>
            <tr><td colspan="2">&nbsp;</td></tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Term to do BL</cong:td>
                <cong:td>
                    <cong:div id="showTerminalLocation">
                        <cong:autocompletor id="terminalLocation" name="terminalLocation" template="commTempFormat" query="TERM_BL" fields="NULL,trmnum"
                                            width="500" container="NULL" styleClass="textlabelsBoldForTextBox mandatory" value="${lclQuoteForm.terminalLocation}" shouldMatch="true" scrollHeight="200" callback="coloadOrTerminal()"/>
                        <cong:hidden name="trmnum" id="trmnum" value="${lclQuoteForm.trmnum}"/>
                    </cong:div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td> <div class="${hideShow}">
                        <span class="button-style1 movePlan" id="QmovePlan" onclick="movePlan('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}', '#portOfOriginId', '#finalDestinationId')">Move Plan</span>
                    </div></cong:td>
                <cong:td style="font-weight:bold" styleClass="underline">
                    Inland Voyage Info
                </cong:td>
            </cong:tr>
            <cong:tr >
                <cong:td colspan="2">
                    <div class="${hideShow}">
                        <span class="${lclQuoteForm.destinationServices ? 'green-background' :'button-style1'} destinationServices" 
                              id="destinationServices"  onclick="destinationServices('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}', '${lclQuote.lclFileNumber.status}', 'addService')">Destination Services</span>
                    </div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td colspan="2">
                    <cong:div>
                        <span class="button-style1 floatLeft multiRates" id="Qrouting" onclick="displayMultiRates('${path}', '${lclQuote.lclFileNumber.id}', '#doorOriginCityZip')">Routing Options</span>
                    </cong:div>
                </cong:td>
            </cong:tr>
        </table>
    </cong:td>
    <cong:td width="20%" valign="top">
        <cong:table width="100%" cellpadding="0" cellspacing="0" border="0" styleClass="textBoldforlcl">
            <cong:tr>
                <cong:td>&nbsp;</cong:td>
                <cong:td></cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td valign="middle" styleClass="textlabelsBoldforlcl">P/C/Both</cong:td>
                <cong:td>
                    <cong:radio value="P" name="pcBoth" id="pcBoth" onclick="updateBillingType();" container="NULL"/>P
                    <cong:radio value="C" name="pcBoth" id="pcBoth" onclick="updateBillingType();" container="NULL"/>C
                    <cong:radio value="B" name="pcBoth" id="pcBoth" onclick="updateBillingType();" container="NULL"/>Both
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td valign="middle" styleClass="textlabelsBoldforlcl">Insurance</cong:td>
                <cong:td>
                    <c:choose>
                        <c:when test="${lclQuote.insurance==true}">
                            <cong:radio name="insurance" id="insuranceY" value="Y" checked="yes" container="NULL" onclick="addInsurance();"/> Yes
                            <cong:radio name="insurance" id="insuranceN" value="N" container="NULL" onclick="removeInsuranceCharge();"/> No
                        </c:when>
                        <c:otherwise>
                            <cong:radio name="insurance" id="insuranceY" value="Y" container="NULL" onclick="addInsurance();"/> Yes
                            <cong:radio name="insurance" id="insuranceN" value="N" checked="yes" container="NULL" onclick="removeInsuranceCharge();"/> No
                        </c:otherwise>
                    </c:choose>
                </cong:td>
                <cong:td></cong:td>
                <cong:td></cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Value of Goods</cong:td>
                <cong:td><input type="text" title="" class="text" name="valueOfGoods" id="valueOfGoods" value="${lclQuote.valueOfGoods}"
                                style="width:75px;" onchange="calculateInsCharges();"/>
                    CIF
                    <input type="text"  class="textlabelsBoldForTextBoxDisabledLook" id="cifValue"
                           name="cifValue" style="width:50px;" maxlength="7" readonly="true" value="${lclQuote.cifValue}"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Delivery Metro PRSJU</cong:td>
                <cong:td><cong:radio value="I" name="deliveryMetro" id="deliveryMetroI" container="NULL" onclick="calculateMetroCharges();"/>I
                    <cong:radio value="O" name="deliveryMetro" id="deliveryMetroO" container="NULL" onclick="calculateMetroCharges();"/>O
                    <cong:radio value="N" name="deliveryMetro" id="deliveryMetroN" container="NULL" onclick="calculateMetroCharges();"/>N</cong:td>
                <cong:hidden name="deliveryMetroField" value="${lclQuote.deliveryMetro}" id="deliveryMetroField"/>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Documentation Yes/No</cong:td>
                <cong:td>
                    <c:choose>
                        <c:when test="${lclQuote.documentation==true}">
                            <cong:radio name="documentation" id="docYesQ" value="Y" checked="yes" container="NULL" onclick="addQuoteDocumCharge('${path}', '${fileNumberId}');"/>Yes
                            <cong:radio name="documentation" id="docNoQ" value="N" container="NULL" onclick="removeDocumCharge();"/> No
                        </c:when>
                        <c:otherwise>
                            <cong:radio name="documentation" id="docYesQ" value="Y" container="NULL" onclick="addQuoteDocumCharge('${path}', '${fileNumberId}');"/>Yes
                            <cong:radio name="documentation" id="docNoQ" value="N" checked="yes" container="NULL" onclick="removeDocumCharge();"/> No
                        </c:otherwise>
                    </c:choose>
                    <input type="hidden" name="documSave" id="documSave"/>
                    <input type="hidden" name="bookPieceId" id="bookPieceId"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td valign="middle" styleClass="textlabelsBoldforlcl">Spot Rate</cong:td>
                <cong:td>
                    <cong:table border="0" width="10%">
                        <cong:tr>
                            <cong:td>
                                <cong:radio value="Y" name="spotRate" container="NULL" onclick="spotRate_yes(this)"/> Yes
                                <cong:radio value="N" name="spotRate" container="NULL" onclick="spotRateByNo(this)"/>No
                                <input type="hidden" name="existSpotRate" id="existSpotRate" value="${lclQuote.spotRate}"/>
                            </cong:td>
                            <cong:td>
                                <input type='button' ${lclQuote.spotRate ? "":"style='display:none;'"} id="lclSpotRate" value="Spot Rate"
                                       class="${lclQuote.spotWmRate!=null && lclQuote.spotWmRate!='' ? 'green-background' : 'button-style1'} lclSpotRate" 
                                       onClick ="showSpotRateInfo('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}');"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td colspan="2">
                                <label id="spotratelabel">${lclQuote.spotRate ? spotratelabel:''}</label>
                                <input type="hidden" name="spotLabel" id="spotLabel" value="${spotratelabel}"/>
                                <input type="hidden" name="weight" styleClass="text twoDigitDecFormat"  id="weight" />
                                <input type="hidden" name="measure" styleClass="text twoDigitDecFormat"  id="measure" />
                            </cong:td>
                        </cong:tr>
                    </cong:table>
                </cong:td>
            </cong:tr>
        </cong:table>
    </cong:td>
</tr>
<cong:hidden name="issuingTerminal" id="issuingTerminal" value="${lclQuoteForm.issuingTerminal}"/>
<cong:hidden name="ptrmnum" id="ptrmnum" value="${lclQuoteForm.trmnum}"/>
<cong:hidden name="to" id="to" value="${lclQuoteForm.to}"/>
<cong:hidden name="manualCompanyName" id="manualCompanyName" value="${lclQuoteForm.manualCompanyName}"/>
<cong:hidden name="cityStateZip" id="cityStateZip" value="${lclQuoteForm.cityStateZip}"/>
<cong:hidden name="pickupCutoffDate" id="pickupCutoffDate" value="${lclQuoteForm.pickupCutoffDate}"/>
<cong:hidden name="pickupReadyDate" id="pickupReadyDate" value="${lclQuoteForm.pickupReadyDate}"/>
<cong:hidden name="pickupReferenceNo" id="pickupReferenceNo" value="${lclQuoteForm.pickupReferenceNo}"/>
<cong:hidden name="whsecompanyName" id="whsecompanyName" value="${lclQuoteForm.whsecompanyName}"/>
<cong:hidden name="whseAddress" id="whseAddress" value="${lclQuoteForm.whseAddress}"/>
<cong:hidden name="whseCity" id="whseCity" value="${lclQuoteForm.whseCity}"/>
<cong:hidden name="whseState" id="whseState" value="${lclQuoteForm.whseState}"/>
<cong:hidden name="whseZip" id="whseZip" value="${lclQuoteForm.whseZip}"/>
<cong:hidden name="whsePhone" id="whsePhone" value="${lclQuoteForm.whsePhone}"/>
<cong:hidden name="pickupInstructions" id="pickupInstructions" value="${lclQuoteForm.pickupInstructions}"/>
<cong:hidden name="termsOfService" id="termsOfService" value="${lclQuoteForm.termsOfService}"/>
<cong:hidden name="chargeAmount" id="chargeAmount" value="${lclQuoteForm.chargeAmount}"/>
<cong:hidden name="pickupCost" id="pickupCost" value="${lclQuoteForm.pickupCost}"/>
<cong:hidden name="spAcct" id="spAcct" value="${lclQuoteForm.spAcct}"/>
<cong:hidden name="scacCode" id="scacCode" value="${lclQuoteForm.scacCode}"/>
<cong:hidden name="pfax1" id="pfax1" value="${lclQuoteForm.pfax1}"/>
<cong:hidden name="pemail1" id="pemail1" value="${lclQuoteForm.pemail1}"/>
<cong:hidden name="pcontactName" id="pcontactName" value="${lclQuoteForm.pcontactName}"/>
<cong:hidden name="paddress" id="paddress" value="${lclQuoteForm.paddress}"/>
<cong:hidden name="pphone1" id="pphone1" value="${lclQuoteForm.pphone1}"/>
<cong:hidden name="pickupHours" id="pickupHours" value="${lclQuoteForm.pickupHours}"/>
<cong:hidden name="pickupReadyNote" id="pickupReadyNote" value="${lclQuoteForm.pickupReadyNote}"/>
<cong:hidden name="pcommodityDesc" id="pcommodityDesc" value="${lclQuoteForm.pcommodityDesc}"/>
<cong:hidden name="shipSupplier" id="shipSupplier" value="${lclQuoteForm.shipSupplier}"/>
<input type="hidden" name="copyFnVal" id="copyFnVal" value="${lclQuoteForm.copyFnVal}"/>

<cong:hidden name="portOfOriginIdForDr" id="portOfOriginIdForDr" value="${lclQuoteForm.portOfOriginIdForDr}"/>
<cong:hidden name="portOfOriginForDr" id="portOfOriginForDr" value="${lclQuoteForm.portOfOriginForDr}"/>
<cong:hidden name="clientAcctForDr" id="clientAcctForDr" value="${lclQuoteForm.clientAcctForDr}"/>
<cong:hidden name="clientCompanyForDr" id="clientCompanyForDr" value="${lclQuoteForm.clientCompanyForDr}"/>
<cong:hidden name="finalDestinationForDr" id="finalDestinationForDr" value="${lclQuoteForm.finalDestinationForDr}"/>
<cong:hidden name="finalDestinationIdForDr" id="finalDestinationIdForDr" value="${lclQuoteForm.finalDestinationIdForDr}"/>
<cong:hidden name="commodityTypeForDr" id="commodityTypeForDr" value="${lclQuoteForm.commodityTypeForDr}"/>
<cong:hidden name="commodityNoForDr" id="commodityNoForDr" value="${lclQuoteForm.commodityNoForDr}"/>
<input type="hidden" name="packageDr" id="packageDr" value="Packages"/>
<input type="hidden" name="packageIdDr" id="packageIdDr" value="175"/>
<input type="hidden" name="commodityTypeId" id="commodityTypeId" value="${lclQuoteForm.commodityTypeId}"/>
<input type="hidden" name="rateTypeForDr" id="rateTypeForDr" value="${lclQuoteForm.rateTypeForDr}"/>
<input type="hidden" name="unlocationCodeForDr" id="unlocationCodeForDr" value="${lclQuoteForm.unlocationCodeForDr}"/>
<input type="hidden" name="originUnlocationCodeForDr" id="originUnlocationCodeForDr" value="${lclQuoteForm.originUnlocationCodeForDr}"/>
<input type="hidden" name="stdRateBasis" id="stdRateBasis" value="${lclQuoteForm.stdRateBasis}"/>
<input type="hidden" name="retailCommodity" id="retailCommodity" value="${lclQuoteForm.retailCommodity}"/>
<input type="hidden" name="commodityNo" id="commodityNo" value="${lclQuoteForm.commodityNo}"/>
<input type="hidden" name="hazmatDr" id="hazmatDr" value="${lclQuoteForm.hazmatDr}"/>
<input type="hidden" name="originDr" id="originDr" value="${lclQuoteForm.originDr}"/>
<input type="hidden" name="destinationDr" id="destinationDr" value="${lclQuoteForm.destinationDr}"/>
</table>
