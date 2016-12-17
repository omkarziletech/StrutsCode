<%--
    Document   : quickDR
    Created on : Oct 21, 2012, 7:02:14 PM
    Author     : Ram
--%>
<%@include file="init.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/export/booking/quickDr.js"/>
<%@include file="/jsps/preloader.jsp" %>
<cong:body>
    <cong:form name="lclBookingForm" id="lclBookingForm" action="lclBooking.do">
        <cong:hidden id="trmnum" name="trmnum"/>
        <cong:hidden id="terminal" name="terminal"/>
        <cong:table border="0" styleClass="widthFull">
            <cong:tr styleClass="tableHeadingNew">
                <cong:td colspan="7"  width="100%">
                    Quick Bookings
                    <div class="textlabelsBoldforlcl" style="float:right"> Unknown Dest
                        <input type="radio" name="nonRated" id="nonRatedY" value="Y" onclick="checkNonRated()"/>Yes
                        <input type="radio" name="nonRated" id="nonRatedN" value="N" onclick="checkNonRated()"/>No&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </div>
                    <div style="float:right">
                        <input type="button" value="Copy From" class="button-style2" onclick="openCopy()"/>
                    </div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" width="13%">
                    Origin CFS
                </cong:td>
                <cong:td>
                    <cong:autocompletor id="portOfOriginForDr" name="portOfOriginForDr"
                                        template="one" fields="NULL,NULL,originUnlocationCode,quickdrPortOfOriginId" query="RELAY_ORIGIN" styleClass="mandatory textlabelsBoldForTextBox"  paramFields="finalDestinationId"
                                        width="250" container="NULL" shouldMatch="true" scrollHeight="180px" value="${lclBookingForm.portOfOriginForDr}"/>
                    <cong:hidden name="quickdrPortOfOriginId" id="quickdrPortOfOriginId" value="${lclBookingForm.quickdrPortOfOriginId}"/>
                </cong:td>
                <input type="hidden" name="disableAcct" id="disableAcct"/>
                <input type="hidden" name="disableAcctNo" id="disableAcctNo"/>
                <cong:td styleClass="textlabelsBoldforlcl" width="12%">Shipper</cong:td>
                <cong:td width="12%">
                    <cong:div id="shipDr">
                        <cong:autocompletor name="shipContactForDr" template="tradingPartner" id="shipContactForDr"
                                            fields="shipAcctForDr,shipperaccttype,shippersubtype,NULL,NULL,NULL,NULL,disableAcct,shipperAddressClient,shipperCityClient,shipperStateClient,shipperCountryClient,shipperZipClient,shipperContactClient,shipperPhoneClient,shipperFaxClient,shipperEmailClient,NULL,NULL,NULL,shipperPoaClient,creditForShipper,disableAcctNo,shipcoload,shipcoloadDesc,shipretail,shipretailDesc,NULL,shipperBrand"
                                            styleClass="textlabelsBoldForTextBox" query="SHIPPER"  width="500" container="NULL" shouldMatch="true" value="${lclBooking.shipContact.companyName}"
                                            scrollHeight="180px" callback="assignTraiffWithCustomer('shipContactForDr','shipAcctForDr');"/>
                        <input type ="hidden" id="shipcoload" name="shipcoload"/>
                        <input type ="hidden" id="shipcoloadDesc" name="shipcoloadDesc"/>
                        <input type ="hidden" id="shipretail" name="shipretail"/>
                        <input type ="hidden" id="shipretailDesc" name="shipretailDesc"/>
                        <input type ="hidden" id="shipperBrand" name="shipperBrand"/>
                    </cong:div>
                    <cong:div id="shipperManual" style="display:none">
                        <cong:text name="shipContactForDr" id="tempShipper"
                                   value="${lclBooking.shipContact.companyName}" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" maxlength="50"/>
                    </cong:div>
                </cong:td>
                <cong:td width="2%"  styleClass="textBoldforlcl">
                    <cong:checkbox id="newSupplierForDr" name="newSupplierForDr" onclick="newSupplierName();" container="NULL"/>New</cong:td>
                <cong:td styleClass="textlabelsBoldforlcl" width="10%">AcctNo</cong:td>
                <cong:td>
                    <cong:text name="shipAcctForDr" id="shipAcctForDr" value="${lclBooking.shipAcct.accountno}" style="width:85px"
                               container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Destination</cong:td>
                <cong:td>
                    <cong:autocompletor id="finalDestinationForDr" name="finalDestinationForDr" template="one" fields="unlocationName,NULL,unlocationCode,finalDestinationIdForDr"
                                        query="CONCAT_RELAY_NAME_FD" styleClass="mandatory textlabelsBoldForTextBox"  width="350" 
                                        container="NULL" shouldMatch="true" callback="if(isLockPort()){setUpcomingSailings();setDefaultAgentSetUp();}" scrollHeight="160px" value="${lclBookingForm.finalDestinationForDr}"/>
                    <cong:checkbox id="basedOnCountry" name="basedOnCountry" onclick="showByCountry();" container="NULL" title="Selected = Search by country"  />
                </cong:td>
                <cong:hidden name="unlocationCode" id="unlocationCode" value=""/>
                <cong:hidden name="finalDestinationIdForDr" id="finalDestinationIdForDr" value="${lclBookingForm.finalDestinationIdForDr}"/>
                <cong:td styleClass="textlabelsBoldforlcl">Consignee</cong:td>
                <cong:td>
                    <cong:div id="consDojo">
                        <cong:autocompletor name="consContactForDr" template="tradingPartner" id="consContactForDr"
                                            fields="consAcctForDr,consigneeaccttype,consigneesubtype,consBsEciAcctNo,consBsEciFwNo,NULL,NULL,disableAcct,consigneeAddressClient,consigneeCityClient,consigneeStateClient,consigneeCountryClient,consigneeZipClient,NULL,dupConsigneePhone,dupConsigneeFax,NULL,cFmc,cotiNumber,consigneePoa,creditForConsignee,NULL,disableAcctNo,conscoload,conscoloadDesc,consretail,consretailDesc"
                                            query="QUICKDR_CONSIGNEE"  width="450" container="NULL" value="${lclBooking.consContact.companyName}" styleClass="textlabelsBoldForTextBox"
                                            shouldMatch="true" scrollHeight="160px" callback="assignTraiffWithCustomer('consContactForDr','consAcctForDr')"/>
                        <input type ="hidden" id="conscoload" name="conscoload"/>
                        <input type ="hidden" id="conscoloadDesc" name="conscoloadDesc"/>
                        <input type ="hidden" id="consretail" name="consretail"/>
                        <input type ="hidden" id="consretailDesc" name="consretailDesc"/>

                    </cong:div>
                    <cong:div id="consManual" style="display:none">
                        <cong:text name="consContactForDr" id="tempConsignee"
                                   value="${lclBooking.consContact.companyName}" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" maxlength="50"/>
                    </cong:div>
                </cong:td>
                <cong:td width="1%"  styleClass="textBoldforlcl">
                    <cong:checkbox id="newConsigneeForDr" name="newConsigneeForDr" onclick="newConsigneeName();" container="NULL"/>New</cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">AcctNo</cong:td>
                <cong:td>
                    <cong:text id="consAcctForDr" name="consAcctForDr" style="width:85px" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook" readOnly="true" value="${lclBooking.consAcct.accountno}"/>
                </cong:td>
                <cong:tr>
                    <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Tariff</cong:td>
                    <cong:td>
                        <cong:autocompletor name="commodityTypeForDr" id="commodityTypeForDr" template="two" fields="commodityNoForDr,isHazmat,commodityTypeId"
                                            query="COMMODITY_TYPE_NAME" width="350" styleClass="textlabelsBoldForTextBox  mandatory"
                                            value="${lclBookingForm.commodityTypeForDr}" container="NULL" scrollHeight="140px" callback="checkHazCommodity();"
                                            shouldMatch="true"/>
                        <input type ="hidden" id="isHazmat" name="isHazmat"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Forwarder</cong:td>
                    <cong:td>
                        <cong:div id="frwdDojo">
                            <cong:autocompletor name="fwdContactForDr" template="tradingPartner" id="fwdContactForDr"
                                                fields="fwdAcctForDr,forwarderaccttype,forwardersubtype,NULL,NULL,NULL,NULL,disableAcct,forwarderAddressClient,forwarderCityClint,forwarderStateClient,forwarderCountryClient,forwarderZipClient,forwardercontactClient,forwarderPhoneClient,forwarderFaxClient,forwarderEmailClient,NULL,NULL,NULL,forwarderPoaClient,creditForForwarder,disableAcctNo,fwdcoload,fwdcoloadDesc,fwdretail,fwdretailDesc,fwdBrand"
                                                query="FORWARDER"  width="600" container="NULL" value="${noFFAcct.accountName}"
                                                styleClass="textlabelsBoldForTextBox"
                                                shouldMatch="true"  scrollHeight="140px" callback="assignTraiffWithCustomer('fwdContactForDr','fwdAcctForDr');"/>
                            <input type ="hidden" id="fwdcoload" name="fwdcoload"/>
                            <input type ="hidden" id="fwdcoloadDesc" name="fwdcoloadDesc"/>
                            <input type ="hidden" id="fwdretail" name="fwdretail"/>
                            <input type ="hidden" id="fwdretailDesc" name="fwdretailDesc"/>
                            <input type ="hidden" id="fwdBrand" name="fwdBrand"/>
                        </cong:div>
                        <cong:div id="frwdManual" style="display:none">
                            <cong:text name="frwdContactForDr" id="tempForwarder"
                                       value="${noFFAcct.accountName}" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" maxlength="50"/>
                        </cong:div>
                    </cong:td>
                    <cong:td width="10%" styleClass="textBoldforlcl">
                        <cong:checkbox id="newForwarderForDr" name="newForwarderForDr" onclick="newForwarderName();" container="NULL"/>New</cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">AcctNo</cong:td>
                    <cong:td>
                        <cong:text name="fwdAcctForDr" id="fwdAcctForDr" value="${noFFAcct.accountno}"
                                   style="width:85px" container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"/>
                    </cong:td>
                </cong:tr>
            </cong:tr>
            <cong:tr>

                <input type="hidden" name="packageDr" id="packageDr" value="Packages"/>
                <input type="hidden" name="packageIdDr" id="packageIdDr" value="58"/>
                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Tariff #</cong:td>
                <cong:td>
                    <cong:autocompletor name="commodityNoForDr" id="commodityNoForDr" template="two"
                                        fields="commodityType,NULL,NULL,commodityTypeId" query="COMMODITY_TYPE_CODE"
                                        width="350" styleClass="text weight textlabelsBoldForTextBox"
                                        container="NULL" value="${lclBookingForm.commodityNoForDr}"
                                        scrollHeight="120px" shouldMatch="true" />
                    <cong:hidden name="commodityTypeId" id="commodityTypeId" value="${lclBookingForm.commodityTypeId}"/>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl" valign="middle">PrintD/R</cong:td>
                <cong:td  styleClass="textBoldforlcl">
                    <input type="radio" name="printDR" id="printDRY" value="Y" checked="yes"/> Yes
                    <input type="radio" name="printDR" id="printDRN" value="N"/> No
                </cong:td>
                <cong:td></cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">How many Labels</cong:td>
                <cong:td>
                    <cong:text name="labelField" styleClass="text" style="width:30px"
                               id="labelField" maxlength="4" onkeyup="checkForNumber(this)"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="td textlabelsBoldforlcl">Piece</cong:td>
                <cong:td>
                    <cong:text name="bookedPieceCount" id="bookedPieceCount" styleClass="text1 floatLeft booked textlabelsBoldForTextBox"
                               container="NULL" onkeyup="checkForNumber(this)" maxlength="11"/>
                    <cong:td styleClass="td textlabelsBoldforlcl">Measure</cong:td>
                    <cong:td>
                        <cong:text name="bookedVolumeImperial" id="bookedVolumeImperial" 
                                   styleClass="text1 weight booked bookedBarrel textlabelsBoldForTextBox" container="NULL"
                                   onkeyup="checkForNumberAndDecimal(this)"/>
                        <span  class="textBoldforlcl">CFT</span>
                    </cong:td>
                </cong:td>
                <cong:td></cong:td>
                <cong:td styleClass="td textlabelsBoldforlcl">Weight</cong:td>
                <cong:td>
                    <cong:text name="bookedWeightImperial" id="bookedWeightImperial" onkeyup="checkForNumberAndDecimal(this)"
                               styleClass="text1 weight booked bookedBarrel textlabelsBoldForTextBox"/>
                    <span  class="textBoldforlcl">LBS</span>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">HaZmat</cong:td>
                    <td class="textBoldforlcl">                  
                        <input type="hidden" name="existHazmat" id="existHazmat" value="${lclBookingPiece.hazmat}"/>
                    <c:choose>
                        <c:when test="${lclBookingPiece.hazmat==true}">
                            <input type="radio" name="hazmatBtn" id="hazmatY" class="hazYes" value="Y" checked="yes"  onclick="calculateHazRates('Y')"  />Yes
                            <input type="radio" name="hazmatBtn" id="hazmatN" class="hazNo" value="N"  onclick="calculateHazRates('N')" /> No
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="hazmatBtn" id="hazmatY" class="hazYes" value="Y"   onclick="calculateHazRates('Y')"  />Yes
                            <input type="radio" name="hazmatBtn" id="hazmatN" class="hazNo" value="N" checked="yes" onclick="calculateHazRates('N')" /> No
                        </c:otherwise>
                    </c:choose>                 
                </td>
                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Brand:</cong:td>
                    <td class="textBoldforlcl">
                    <c:set var="ECI_OTI_label" value ="${lclBookingForm.companyCode eq 'ECU' ? 'Econo' : 'OTI'}"/>
                    <c:set var="ECI_OTI_value" value ="${lclBookingForm.companyCode eq 'ECU' ? 'ECI' : 'OTI'}"/>
                    <cong:radio container="null" name="businessUnit" value='${ECI_OTI_value}' id="econo"
                                styleClass="businessUnit"/><span class="econo">${ECI_OTI_label}</span>
                    <cong:radio container="null" name="businessUnit" value='ECU' id="eculine"
                                styleClass="businessUnit"/><span class="eculine">ECU Worldwide</span>
                </td>

                <cong:td></cong:td>
                <cong:td>
                    <span class="button-style1" id="addHoldShipment" onclick="openHoldShipment()">Hold Shipment</span>
                </cong:td>
                <cong:td styleClass="td textlabelsBoldforlcl"></cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="td textlabelsBoldforlcl" valign="middle">Over/Short/Damage</cong:td>
                <cong:td> <cong:radio value="Y" name="overShortdamaged" id="overShortdamaged" container="NULL" onclick="displayOsdBox();"/> Yes
                    <cong:radio value="N" name="overShortdamaged" id="overShortdamaged" container="NULL" onclick="deleteOsdRemarks('Are you sure you want to delete OSD Remarks?');"/> No</cong:td>
                <cong:td></cong:td>
                <cong:td></cong:td>
                <cong:td></cong:td>
                <cong:td styleClass="td textlabelsBoldforlcl">HOT Codes</cong:td>
                <cong:td>
                    <span class="button-style2" id="addhot" onclick="showBlock('#hotCodesBox')">Add</span>
                    <div id="hotCodesBox" valign="middle" class="smallInputPopupBox">
                        <cong:autocompletor name="hotCodes" id="hotCodes" fields="NULL,NULL,NULL,genCodefield1,NULL" query="CONCAT_HOTCODE_TYPE"  width="300"
                                            shouldMatch="true" styleClass="text" container="NULL" template="concatHTC" scrollHeight="200px" value="" callback="checkValidHotCode('hotCodes','hotCode');"/>
                        <input type="hidden" id="genCodefield1" name="genCodefield1"/>                                    
                        <input type="button" class="button-style3" style="max-width:70%" id="AddButton" value="Add"
                               onclick="addHotCode3pRef()"/>
                    </div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td colspan="6"></cong:td>
                <cong:td id="hotCodesList" styleClass="td textlabelsBoldforlcl" valign="middle">
                    <jsp:include page="/jsps/LCL/ajaxload/generalInfo/quickBookingHotCode.jsp"/>
                </cong:td>  
            </cong:tr> 


            <cong:tr>
                <cong:td colspan="1"></cong:td>
                <cong:td>
                    <div id='osdRemarksId'>
                        <cong:textarea rows="4" cols="30"  id="osdRemarks" name="osdRemarks"  value="${lclBookingForm.osdRemarks}" style="resize:none;text-transform: uppercase"></cong:textarea>
                        </div>
                </cong:td>
            </cong:tr>                    
            <cong:tr>

                <cong:td styleClass="td textlabelsBoldforlcl" id="cfclLabel">CFCL</cong:td>
                <cong:td colspan="6">
                    <c:set var="cfclFlag" value="${lclBookingForm.cfclForDR}"/>
                    <input type="radio" id ="cfclYesForDR" name="cfclForDR"  value="Y" onclick="setUpcomingSailings();
                            setCfclLabelColor();" ${cfclFlag ? 'checked' :''} /> Yes
                    <input type="radio" id ="cfclNoForDR" name="cfclForDR"  value="N" onclick="setUpcomingSailings();
                            setCfclLabelColor();" ${!cfclFlag ? 'checked' :''} /> No

                    CFCL Account
                    <cong:autocompletor name="cfclAcctNameForDr" template="tradingPartner" id="cfclAcctNameForDr"
                                        fields="cfclAcctNoForDr,NULL,NULL,NULL,NULL,NULL,NULL,disabledAccount,forwardAccount" position="right"
                                        styleClass="textlabelsBoldForTextBox" query="CFCL_ACCOUNT"
                                        width="600" container="NULL" shouldMatch="true" value="${lclBookingForm.cfclAcctNameForDr}"
                                        callback="cfclAccttypeCheck();" scrollHeight="300px"/>
                    Acct No
                    <cong:text name="cfclAcctNoForDr" id="cfclAcctNoForDr" value="${lclBookingForm.cfclAcctNoForDr}" style="align:right;width:80px"
                               container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"/>
                    <input type="hidden" name="disabledAccount" id="disabledAccount"/>
                    <input type="hidden" name="forwardAccount" id="forwardAccount"/>

                </cong:td>
            </cong:tr>


            <tr>
                <td colspan="3"></td>
                <td>
                    <div class="button-style1 saveDr" onclick="validateform('${path}');"  id="save">
                        Save Bkg
                    </div>
                </td>
                <td colspan="3"></td>
            </tr>
        </cong:table>
        <table width="100%" class="caption"><tr><td width="20%">Upcoming Sailings</td>
                <td width="20%" class="caption">
                    <input type="checkbox" name="showOlder" id="showOlder" title="Show Older" style="vertical-align: middle;"
                           onclick="setUpcomingSailings();"/>Show Older
                </td>
                <td width="20%" class="caption">Origin:<b class="origin greenBold"></b></td>
                <td width="20%" class="caption">Destination:<b class="destination greenBold"></b></td>

            </tr></table>
        <table class="dataTable">
            <tr>
                <td class="text-readonly">
                    <div style="height:140px; overflow-y:auto;background-color: #ffffff;" id="upcomingSailing">
                        <jsp:include page="/jsps/LCL/lclVoyage.jsp"/>
                    </div>
                </td>
            </tr>
        </table>
        <input type="hidden" name="unknownDest" id="unknownDest" value="${unknownDest}"/>
        <input type="hidden" name="unknownDestId" id="unknownDestId" value="${unknownDestId}"/>
        <cong:hidden id="portOfOriginId" name="portOfOriginId"/>
        <cong:hidden id="portOfLoadingId" name="portOfLoadingId"/>
        <cong:hidden id="portOfDestinationId" name="portOfDestinationId"/>
        <cong:hidden id="finalDestinationId" name="finalDestinationId"/>
        <cong:hidden id="methodName" name="methodName"/>
        <cong:hidden id="masterScheduleNo" name="masterScheduleNo"/>
        <cong:hidden id="pooLrdt" name="pooLrdt"/>
        <cong:hidden id="fdEta" name="fdEta"/>
        <cong:hidden id="companyCode" name="companyCode"/>
        <cong:hidden id="moduleName" name="moduleName"/>
        <cong:hidden name="previousSailing" id="previousSailing"/>
        <input type="hidden" name="bkgHazmat" id="bkgHazmat" value="${hazmat}"/>
        <div id="add-Comments-container" class="static-popup" style="display: none;width: 400px;height: 100px;">
            <table class="table" style="margin: 2px;width: 398px;" border="0">
                <tr class="tableHeadingNew">
                    <td>
                        <div class="float-left">
                            <label id="headingAdjustmentComments">Copy</label>
                        </div>
                    </td>
                    <td>
                        <span style="float: right;">
                            <a id="lightBoxClose" href="javascript:close();">
                                <img src="${path}/js/greybox/w_close.gif" alt="close" title="Close" style="border: none;">Close
                            </a>
                        </span>
                    </td>
                </tr>
                <tr><td colspan="2"></td></tr>
                <tr><td colspan="2"></td></tr>
                <tr>
                    <td class="td textlabelsBoldforlcl">
                        DR#
                    </td>
                    <td>
                        <cong:text container="null" id="fileNumber" name="fileNumber" styleClass="textlabelsBoldForTextBox" value=""/>
                    </td>
                </tr>
                <tr><td colspan="2"></td></tr>
                <tr><td colspan="2"></td></tr>
                <tr>
                    <td align="center" colspan="2">
                        <input type="button" value="Submit" class="button-style2" onclick="copyQuickDr()"/>
                    </td>
                </tr>
            </table>
        </div>
        <div id="add-hotCodeComments-container" class="static-popup" style="display: none;width: 600px;height: 150px;">
            <table class="table" style="margin: 2px;width: 598px;">
                <tr>
                    <th>
                <div class="float-left">
                    <label id="headingComments"></label>
                </div>
                </th>
                </tr>
                <tr>
                    <td></td>
                </tr>
                <tr>
                <input type="hidden" name="hiddenDeleteHotCodeFlag" id="hiddenDeleteHotCodeFlag"/>
                <!--                <input type="text" name="3pRefId" id="3pRefId"/>-->
                <td class="label">
                    <textarea id="hotCodeComments" name="hotCodeComments" cols="85" rows="5" class="textBoldforlcl" 
                              style="resize:none;text-transform: uppercase"></textarea>
                </td>
                </tr>
                <tr>
                    <td align="center">
                        <input type="hidden" name="xxxHotCodeComment" id="xxxHotCodeComment" value="${lclBookingForm.hotCodeComments}"/>
                        <input type="button"  value="Save" id="saveHotCode"
                               align="center" class="button" onclick="addHotCodeXXX3pRef();"/>
                        <input type="button"  value="Cancel" id="cancelHotCode"
                               align="center" class="button" onclick="cancelHotCodeXXXComments();"/>
                    </td>
                </tr>
            </table>
        </div>  
        <div id="add-holdShipment-container" class="static-popup" style="display: none;width: 600px;height: 150px;">
            <table class="table" style="margin: 2px;width: 598px;">
                <tr>
                    <th colspan="2">
                <div class="float-left">
                    <label id="headerHoldShipmentComments">Enter a required explanation for putting the shipment on Hold</label>
                </div>
                </th>
                </tr>
                <tr>
                    <td align="right" style="padding-top: 10px;xwidth:25%" class="label">Comments</td>
                    <td align="left" style="padding-top: 10px;width: 75%">
                        <input type="hidden" name="hiddenHoldShipmentComments" id="hiddenHoldShipmentComments"/>
                        <textarea id="holdShipmentComments" name="holdShipmentComments" cols="40" rows="5" 
                                  class="textBoldforlcl" style="resize:none;text-transform: uppercase"></textarea>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2" style="padding-top:  10px">
                        <input type="button"  value="Save"
                               align="center" class="button" onclick="saveHoldShipment();"/>
                        <input type="button"  value="Cancel"
                               align="center" class="button" onclick="closeHoldShipment();"/>
                    </td>
                </tr>
            </table>
        </div> 

    </cong:form>
</cong:body>
