<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<table border="0" width="100%">
    <tr class="caption">
        <td colspan="6" align="center">Trade Route</td>
    </tr>
    <cong:tr>
        <cong:td width="24%">
            <cong:table width="100%" cellpadding="0" cellspacing="0" border="0">
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">CTC/Retail/FTF<span style="color:red;font-size: 19px;vertical-align: middle">*</span></cong:td>
                    <cong:td styleClass="textBoldforlcl">
                        <cong:radio value="C" name="rateType" container="NULL" id="ctc" onclick="checkOriginTerminal();
                                coloadOrTerminal();
                                clearAllRates('AutoRates will be removed.Manual Rates will be recalculated.Are you sure you want to delete?', $('#fileNumberId').val());"/> C
                        <cong:radio value="R" name="rateType" container="NULL" id="rateR" onclick="checkOriginTerminal();
                                clearAllRates('AutoRates will be removed.Manual Rates will be recalculated.Are you sure you want to delete?', $('#fileNumberId').val());"/> R
                        <cong:radio value="F" name="rateType" container="NULL" id="rateF" onclick="checkOriginTerminal();
                                clearAllRates('AutoRates will be removed.Manual Rates will be recalculated.Are you sure you want to delete?', $('#fileNumberId').val());"/>FTF
                        <cong:hidden name="rateTypes" value="${lclBooking.rateType}" id="rateTypes"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" width="5%" valign="middle"> Pickup Y/N</cong:td>
                    <cong:td styleClass="textBoldforlcl">
                        <cong:radio value="Y" name="pooDoor" container="NULL" id="doorOriginY" onclick="showPickupButton();"/> Yes
                        <cong:radio value="N" name="pooDoor" container="NULL" id="doorOriginN" onclick="changeDoor();"/> No</cong:td>
                    <cong:td>  <div class="button-style1 pickupInfo" align="center"  style="float: left;" id="pickupInfo" 
                                    onclick="showPickupInfo('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', 'Pickup');">Pickup Info</div>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" width="5%">Door Origin/City/Zip </cong:td>
                    <cong:td styleClass="textBoldforlcl">
                        <cong:autocompletor styleClass="textlabelsBoldForTextBox systemPickCtiy" readOnly="false" id="doorOriginCityZip" 
                                            name="doorOriginCityZip" query="CONCAT_CITY" fields="pickZip,pickCity,pickState"
                                            template="concatOrigin" container="NULL" width="500" shouldMatch="true" callback="changeDoorOrigin()" 
                                            scrollHeight="200px" value="${lclBookingForm.doorOriginCityZip}"/>
                        <cong:hidden name="pickupCity" id="pickCity"/>
                        <cong:hidden name="pickupState" id="pickState"/>
                        <cong:hidden name="pickupZip" id="pickZip"/>
                        <cong:hidden name="duplicateDoorOrigin" id="duplicateDoorOrigin" value="${lclBookingForm.doorOriginCityZip}"/>
                        <cong:text name="manualDoorOriginCityZip" id="manualDoorOriginCityZip" 
                                   value="${lclBookingForm.doorOriginCityZip}" onkeyup="notAllowSpecialChar(this);" styleClass="textlabelsBoldForTextBox"/>
                    </cong:td>
                    <cong:td>
                        <input type="checkbox" id="checkDoorOriginCityZip" name="checkDoorOriginCityZip"  onclick="showManualDoorCity(this);"
                               title="Make pickup field free form text for Canada pickups <br>(CTS rates will not work)" 
                               ${not empty lclBookingForm.doorOriginCityZip && fn:indexOf(lclBookingForm.doorOriginCityZip,"/") < 0 ? 'checked':''}/>
                        <img src="${path}/img/map.png" id="lclDoorOriginCityMap" align="middle" width="12" height="12" title="Google Map Search"
                             onclick="getDoorOriginCityGoogleMap('${path}')" />
                    </cong:td> 
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" width="5%">Origin CFS</cong:td>
                    <cong:td width="5%">
                        <c:choose>
                            <c:when test="${lclBooking.bookingType eq 'T'}">
                                <cong:autocompletor id="portOfOriginR" name="portOfDestination" 
                                                    callback="if($('#finalDestinationId').val()!=''){calculateCharge('', '#doorOriginCityZip', '#pickupReadyDate');}checkOriginTerminal();if($('#portOfDestination').val()==''){submitAjaxFormForAgent('refreshAgent','#lclBookingForm','#m','');};setRelayDetails('Origin');filldeliverCargo();checkRoleDuty();"
                                                    template="one" fields="NULL,NULL,originUnlocationCode,portOfOriginId,warehsterminalNo" query="RELAY_ORIGIN" styleClass="mandatory textlabelsBoldForTextBox"
                                                    width="500" container="NULL" paramFields="finalDestinationId" value="${pod}" shouldMatch="true" scrollHeight="200px"/>
                                <cong:hidden name="podUnlocationcode" id="originUnlocationCode" value="${lclBooking.portOfDestination.unLocationCode}"/>
                                <cong:hidden name="portOfDestinationId" id="portOfOriginId" value="${lclBooking.portOfDestination.id}"/>
                                <input type="hidden" id="warehsterminalNo" name="warehsterminalNo" value="${lclBooking.portOfDestination.terminal.trmnum}"/>
                            </c:when><c:otherwise>
                                <cong:autocompletor id="portOfOriginR" name="portOfOrigin" 
                                                    callback="if($('#finalDestinationId').val()!=''){calculateCharge('', '#doorOriginCityZip', '#pickupReadyDate');}checkOriginTerminal();if($('#portOfDestination').val()==''){submitAjaxFormForAgent('refreshAgent','#lclBookingForm','#m','');};setRelayDetails('Origin');filldeliverCargo();setCfclValues();checkRoleDuty();"
                                                    template="one" fields="NULL,NULL,originUnlocationCode,portOfOriginId,warehsterminalNo" query="RELAY_ORIGIN" styleClass="mandatory textlabelsBoldForTextBox"
                                                    width="500" container="NULL" paramFields="finalDestinationId" value="${lclBookingForm.portOfOrigin}" shouldMatch="true" scrollHeight="200px"/>
                                <cong:hidden name="portOfOriginId" id="portOfOriginId" value="${lclBookingForm.portOfOriginId}"/>
                                <cong:hidden name="originUnlocationCode" id="originUnlocationCode" value="${lclBookingForm.originUnlocationCode}"/>
                                <input type="hidden" id="warehsterminalNo" name="warehsterminalNo" value="${lclBooking.portOfOrigin.terminal.trmnum}"/>
                            </c:otherwise></c:choose>
                            <cong:checkbox name="showFullRelay" id="showFullRelay" onclick="showHideRelay()" title="Show all Origin Cities"/>
                            <span><img src="${path}/img/map.png" id="lclOriginMap" width="12" height="12" title="Google Map Search"
                                   onclick="getOriginGoogleMap('${path}', 'displayOriginMap', 'E')" /></span>
                        <span valign="middle" id="showAstar" style="visibility: hidden">
                            <img src="${path}/img/icons/astar.gif" width="12" height="12" id="clearRates" title="Change Origin"
                                 onclick="clearAllValues('This will allow to change the Origin but will remove all the existing rates.Continue?', $('#fileNumberId').val(), 'showAstarOrigin');"/>
                        </span>
                        <c:if test="${lclBooking.bookingType eq 'T'}">
                            <span id="originDetails" title="Absolute  Origin : ${lclBookingForm.portOfOrigin} <br/> POL : ${pol}">
                                <cong:img src="${path}/img/icons/iicon.png" id="viewgif" width="12" height="12"/>
                            </span>
                        </c:if>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="2" >
                        <cong:table id="polPod">
                            <jsp:include page="/jsps/LCL/ajaxload/polPod.jsp"/>
                        </cong:table>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Destination</cong:td>
                    <cong:td>
                        <cong:autocompletor id="finalDestinationR" name="finalDestination" template="one" fields="unlocationName,NULL,unlocationCode,finalDestinationId"
                                            query="CONCAT_RELAY_NAME_FD" paramFields="portOfOriginId" callback="if(isLockPort('Booking')){submitAjaxFormForAgent('refreshAgent','#lclBookingForm','#m','');lockInsurance();calculateRates();setRelayDetails('destination');}setCfclValues();"
                                            styleClass="mandatory textlabelsBoldForTextBox"  width="500" container="NULL" value="${lclBookingForm.finalDestination}" shouldMatch="true" scrollHeight="200px"/>
                        <cong:checkbox name="basedOnCity" id="basedOnCity" onclick="showRelayFd();" title="Checked=Look by City Name <br> UnChecked=Look by Country"/>
                        <img src="${path}/img/map.png" id="lclDestinationMap" width="12" height="12" title="Google Map Search"
                             onclick="getDestinationGoogleMap('${path}', 'displayDestinationMap', 'E')" />
                        <span valign="middle" id="showAstarDestn" style="visibility: hidden">
                            <img src="${path}/img/icons/astar.gif" width="12" height="12" id="clearRatesDestn" title="Change Destination" 
                                 onclick="clearAllValues('This will allow to change the  Destination but will remove all the existing rates.Continue?', $('#fileNumberId').val(), 'showAstarDestn');"/>
                        </span>
                        <cong:hidden name="finalDestinationId" id="finalDestinationId" value="${lclBookingForm.finalDestinationId}"/>
                        <cong:hidden name="unlocationCode" id="unlocationCode" value="${lclBookingForm.unlocationCode}"/>
                        <cong:hidden name="unlocationName" id="unlocationName" value="${lclBookingForm.unlocationName}"/>
                        <input type="hidden" name="unknownDest" id="unknownDest" value="${unknownDest}">
                        <input type="hidden" name="unknownDestId" id="unknownDestId" value="${unknownDestId}">
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle" width="5%" > Relay Ovr</cong:td>
                    <cong:td colspan="3" styleClass="textBoldforlcl">
                        <cong:radio  value="Y" name="relayOverride" id ="relayOverYes" container="NULL" onclick="setRelayOverRideYes();
                                setRelayDetails('relayOverride');setPreCobFn();"/> Yes
                        <cong:radio value="N" name="relayOverride" id ="relayOverNo" container="NULL" onclick="changePort();
                                setRelayOverRide();RecalculateRelayCharge('', '#doorOriginCityZip', '#pickupReadyDate');
                                updatePOD();setPreCobFn();"/> No
                        <div id="podFdTransTime">
                            <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;POD-FD TT</label>
                            <cong:text name="podfdtt" id="podfdtrans" maxlength="2" style="width:25px;" onchange="calculatePodFdTrans();"
                                       value="${lclBooking.podfdtt}" onkeyup="checkForNumberAndDecimal(this)"  styleClass="textlabelsBoldForTextBox"/>
                        </div>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:td>
        <cong:td width="24%" valign="top">
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="textlabelsBoldforlcl">Deliver Cargo to</td>
                    <td>
                <cong:autocompletor id="deliveryCargoToCode" name="whsCode" query="EXPORT_DELIVER_CARGO"  styleClass="smallTextlabelsBoldForTextBox"
                                    fields="deliverCargoToName,deliverCargoToAddress,deliverCargoToCity,deliverCargoToState,deliverCargoToZip,deliverCargoToPhone,deliverCargoToFax"
                                    template="delwhse" container="NULL" width="500" shouldMatch="true" scrollHeight="200px" value="${lclBookingForm.whsCode}" callback="validateInlandRates();"/>
                <cong:text name="pooWhseContact.companyName"  style="width:205px" id="deliverCargoToName"
                           styleClass="text textlabelsBoldForTextBox textCap" value="${lclBooking.pooWhseContact.companyName}"/>
                </td>
                </tr>
                <tr>
                    <td class="textlabelsBoldforlcl">Address</td>
                    <td>
                <cong:textarea cols="3" rows="15" styleClass="smallDeliTextarea textlabelsBoldForTextBox"
                               id="deliverCargoToAddress" name="pooWhseContact.address" value='${lclBooking.pooWhseContact.address}'>
                </cong:textarea>
                </td>
                </tr>
                <tr>
                    <td  class="textlabelsBoldforlcl">City</td>
                    <td>
                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="deliverCargoToCity" style="width:100px"
                           name="pooWhseContact.city" value="${lclBooking.pooWhseContact.city}" maxlength="50"/>
                <span class="textlabelsBoldforlcl">&nbsp;&nbsp;State</span>
                <cong:text styleClass="text textlabelsBoldForTextBox textCap" style="width:100px"  id="deliverCargoToState"
                           name="pooWhseContact.state" value="${lclBooking.pooWhseContact.state}" maxlength="50"/>
                </td>
                </tr>
                <tr>
                    <td  class="textlabelsBoldforlcl">Zip</td>
                    <td>
                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="deliverCargoToZip" style="width:100px"
                           name="pooWhseContact.zip" value="${lclBooking.pooWhseContact.zip}" maxlength="50"/>
                <span class="textlabelsBoldforlcl"> Phone</span>
                <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="deliverCargoToPhone"
                           name="pooWhseContact.phone1" style="width:100px" value="${lclBooking.pooWhseContact.phone1}" maxlength="50"/>
                </td>
                <cong:hidden id="deliverCargoToFax" name="pooWhseContact.fax1" value="${lclBooking.pooWhseContact.fax1}"/>
                </tr>

                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Term to do BL</cong:td>
                    <cong:td>
                        <cong:div id="showTerminalLocation">
                            <cong:autocompletor id="terminal" name="terminal" template="commTempFormat" styleClass="mandatory textlabelsBoldForTextBox" query="TERM_BL" fields="NULL,trmnum"
                                                width="500" container="NULL"   value="${lclBookingForm.terminal}" shouldMatch="true" scrollHeight="200px" callback="updateTerminal();coloadOrTerminal()"/>
                            <cong:hidden name="trmnum" id="trmnum" value="${lclBookingForm.trmnum}"/>
                        </cong:div>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>&nbsp;</cong:td>
                    <cong:td style="font-weight:bold">
                        <a href="#" onclick="showInlandVoyageInfo('${path}', '${lclBooking.lclFileNumber.id}');" style="color: blue">
                            Inland Voyage Info
                        </a>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td><div class="${hideShow}">
                            <span class="button-style1 movePlan" id="movePlan" onclick="movePlan('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', '#portOfOriginId', '#finalDestinationId')">Move Plan</span>
                        </div></cong:td>
                    <td rowspan="3" style="vertical-align:top;">
                        <div>
                            <%@include file="voyageInfo.jsp" %>
                        </div>
                    </td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="1">
                        <div class="${hideShow}">
                            <span class="${lclBookingForm.destinationServices ? 'green-background' : 'button-style1'} destinationServices" 
                                  id="destinationServices" onclick="destinationServices('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', 'addService')">
                                Destination Services</span>
                        </div>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="2">
                        <cong:div>
                            <span class="button-style1 floatLeft multiRates" id="routeOption" onclick="displayMultiRates('${path}', '${lclBooking.lclFileNumber.id}', '#doorOriginCityZip')">Routing Options</span>
                        </cong:div>
                    </cong:td>
                </cong:tr>
            </table>
        </cong:td>
        <cong:td>
            <cong:table border="0">
                <cong:tr><cong:td>
                        <cong:table width="100%" cellpadding="0" cellspacing="0" border="0">
                            <cong:tr>
                                <cong:td></cong:td>
                                <cong:td style="font-weight:bold">
                                    Booked for Voyage
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">ECI Voy#</cong:td>
                                <cong:td>
                                    <cong:hidden name="masterScheduleNo" id="masterScheduleNo"/>
                                    <c:set var="bookedSsDetail" value="${lclBooking.bookedSsHeaderId.vesselSsDetail}"/>
                                    <input type="hidden" id="existMasterSchd" name="existMasterSchd" value="${lclBooking.bookedSsHeaderId.id}"/>
                                    <input type="text" class="textlabelsBoldForTextBoxDisabledLook" id="eciVoyage" name="eciVoyage" value="${lclBooking.bookedSsHeaderId.scheduleNo}" readOnly="true"/>
                                    <span valign="middle" id="showAstarVoy" style="display: none">
                                        <img src="${path}/img/icons/astar.gif" width="12" height="12" id="clearRates" 
                                             title="Change Booked for Voyage" onclick="clearBookedVoyage('This will allow to change Booked For Voyage');"/>
                                    </span>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Sail Date</cong:td>
                                <cong:td>
                                    <fmt:formatDate pattern="dd-MMM-yyyy" var="polEtd" value="${bookedSsDetail.std}"/>
                                    <input type="text" class="textlabelsBoldForTextBoxDisabledLook" id="sailDate" name="sailDate" value="${polEtd}" readOnly="true"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">SS Line</cong:td>
                                <cong:td>
                                    <input type="text" class="textlabelsBoldForTextBoxDisabledLook"
                                           readOnly="true" id="ssLine" name="ssLine" value="${bookedSsDetail.spAcctNo.accountName}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Vessel Name</cong:td>
                                <cong:td>
                                    <input type="text" class="textlabelsBoldForTextBoxDisabledLook" 
                                           readOnly="true" id="vesselName" name="vesselName" value="${bookedSsDetail.spReferenceName}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">SS Voy</cong:td>
                                <cong:td>
                                    <input type="text" class="textlabelsBoldForTextBoxDisabledLook"
                                           readOnly="true" id="ssVoyage" name="ssVoyage" value="${bookedSsDetail.spReferenceNo}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Pier</cong:td>
                                <cong:td>
                                    <c:choose><c:when test="${not empty scheduleKNum}">
                                            <c:set var="sched" value="(${scheduleKNum})"/>
                                        </c:when><c:otherwise>
                                            <c:set var="sched" value="${scheduleKNum}"/>
                                        </c:otherwise></c:choose>
                                        <input type="text" class="textlabelsBoldForTextBoxDisabledLook" 
                                               readOnly="true" id="pier" name="pier" value="${pierName}${sched}"/>
                                    <span >  <img src="${path}/img/icons/iicon.png" width="12" height="12"  id="pierName"  title="${mouseOverPier}"/></span>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Origin LRD</cong:td>
                                <cong:td style="float:left">                        
                                    <cong:calendarNew styleClass="textlabelsBoldForTextBoxsizeWidth152" showTime="true" is12HrFormat="true" id="originLrd" name="pooLrdt" value="${lclBooking.pooWhseLrdt}"/>
                                    <cong:hidden name="selectedLrd" id="selectedLrd"/>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                    <cong:td>
                        <cong:table>
                            <cong:tr>
                                <cong:td></cong:td>
                                <cong:td style="font-weight:bold">Picked on Voyage</cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">ECI Voy#</cong:td>
                                <cong:td>
                                    <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook"  id="pickedOnVoyageNo"
                                               name="eciVoyage1" readOnly="true" value="${pickOnVoyage.lclSsHeader.scheduleNo}"/>
                                    <cong:hidden name="masterScheduleNo" id="masterScheduleNo"/>
                                    <input type="hidden" name="pickedOrginId" id="pickedOrginId" value="${pickOnVoyage.lclSsHeader.origin.id}"/>
                                    <input type="hidden" name="pickedDestId" id="pickedDestId" value="${pickOnVoyage.lclSsHeader.destination.id}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Sail Date</cong:td>
                                <cong:td>
                                    <fmt:formatDate pattern="dd-MMM-yyyy" var="polEtd" value="${pickOnVoyage.std}"/>
                                    <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook" name="sailDate1" readOnly="true" style="align:right" value="${polEtd}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">SS Line</cong:td>
                                <cong:td>
                                    <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" name="ssLine1" style="align:right" value="${pickOnVoyage.spAcctNo.accountName}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Vessel Name </cong:td>
                                <cong:td>
                                    <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" name="vesselName1" value="${pickOnVoyage.spReferenceName}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">SS Voy</cong:td>
                                <cong:td>
                                    <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" name="ssVoyage1" value="${pickOnVoyage.spReferenceNo}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Pier</cong:td>
                                <cong:td>
                                    <c:set var="pier" value="${fn:toUpperCase(pickOnVoyage.departure.unLocationName)}"/>
                                    <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" name="ssVoyage1" value="${pier}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Unit #</cong:td>
                                <cong:td>
                                    <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" name="unitNo" value="${unitNum}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl" style="padding-bottom:0.3em;">Size</cong:td>
                                <cong:td>
                                    <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook" 
                                               readOnly="true" name="unitSize" value="${unitSize}"/>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">ETA FD</cong:td>
                    <cong:td>
                        <c:set var="idForPickedOrBooked" value="${not empty pickOnVoyage.sta ? 'pickedId' : 'fdEta'}"/>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="pickorbookedSTA" 
                                        value="${not empty pickOnVoyage.sta ? pickOnVoyage.sta : bookedSsDetail.sta}"/>
                        <input type="text"  style="padding-top:0.3em;" readOnly="true" style="width: 100px;" 
                               class="textlabelsBoldForTextBoxDisabledLook" id="${idForPickedOrBooked}" 
                               name="${idForPickedOrBooked}" value="${pickorbookedSTA}" />
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:td>
        <cong:td>
            <cong:table width="100%" cellpadding="0" cellspacing="0" border="0">
                <cong:tr>
                    <cong:td>&nbsp;</cong:td>
                    <cong:td></cong:td>
                </cong:tr>

                <cong:tr>
                    <input type="hidden" name="previousBillingType" id="previousBillingType" value="${lclBooking.billingType}"/>
                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">P/C/Both</cong:td>
                    <cong:td  styleClass="textBoldforlcl">
                        <cong:radio value="P" name="pcBoth" id="radioP" onclick="validateBillingType();" container="NULL"/>P
                        <cong:radio value="C" name="pcBoth" id="radioC" onclick="validateBillingType();" container="NULL"/>C
                        <cong:radio value="B" name="pcBoth" id="radioB" onclick="validateBillingType();" container="NULL"/>Both
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Insurance</cong:td>
                    <cong:td  styleClass="textBoldforlcl">
                        <c:set var="insuranceFlag" value="${lclBooking.insurance}"/>
                        <input type="radio" name="insurance" id="insuranceY" value="Y" ${insuranceFlag ? 'checked':''} onclick="addInsurance();"/> Yes
                        <input type="radio" name="insurance" id="insuranceN" value="N" ${!insuranceFlag ? 'checked':''} onclick="removeInsuranceCharge();"/> No
                    </cong:td>
                    <cong:td></cong:td>
                    <cong:td></cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Value of Goods</cong:td>
                    <cong:td>
                        <input type="text" title="" class="text textlabelsBoldForTextBox" id="valueOfGoods"
                               name="valueOfGoods" value="${lclBooking.valueOfGoods}" style="width:75px;" onchange="calculateInsuranceCharge();"/>
                        CIF
                        <input type="text"  class="textlabelsBoldForTextBoxDisabledLook" id="cifValue"
                               name="cifValue" style="width:50px;" maxlength="7" readonly="true" value="${lclBooking.cifValue}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Delivery Metro PRSJU</cong:td>
                    <cong:td  styleClass="textBoldforlcl">
                        <cong:radio value="I" name="deliveryMetro" id="deliveryMetroI" container="NULL" onclick="calculateDeliveryMetroCharge()"/>I
                        <cong:radio value="O" name="deliveryMetro" id="deliveryMetroO" container="NULL" onclick="calculateDeliveryMetroCharge()"/>O
                        <cong:radio value="N" name="deliveryMetro" id="deliveryMetroN" container="NULL" onclick="calculateDeliveryMetroCharge()"/>N</cong:td>
                    <cong:hidden name="deliveryMetroField" value="${lclBooking.deliveryMetro}" id="deliveryMetroField"/>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Documentation Yes/No</cong:td>
                    <cong:td  styleClass="textBoldforlcl">
                        <c:choose>
                            <c:when test="${lclBooking.documentation==true}">
                                <cong:radio name="documentation" id="docYes" value="Y" checked="yes" container="NULL" onclick="addDocumentCharge('${path}');"/>Yes
                                <cong:radio name="documentation" id="docNo" value="N" container="NULL" onclick="removeDocumCharge();"/> No
                            </c:when>
                            <c:otherwise>
                                <cong:radio name="documentation" id="docYes" value="Y" container="NULL" onclick="addDocumentCharge('${path}');"/>Yes
                                <cong:radio name="documentation" id="docNo" value="N" checked="yes" container="NULL" onclick="removeDocumCharge();"/> No
                            </c:otherwise>
                        </c:choose>
                        <input type="hidden" name="documSave" id="documSave"/>
                        <input type="hidden" name="bookPieceId" id="bookPieceId"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Spot Rate</cong:td>
                    <cong:td styleClass="textBoldforlcl">
                        <cong:table border="0" width="10%">
                            <cong:tr>
                                <cong:td>
                                    <cong:radio value="Y" name="spotRate" container="NULL" onclick="spotRate_yes(this)"/> Yes
                                    <cong:radio value="N" name="spotRate" container="NULL" onclick="spotRateByNo(this)"/>No
                                    <input type="hidden" name="existSpotRate" id="existSpotRate" value="${lclBooking.spotRate}"/>
                                </cong:td>
                                <cong:td>
                                    <input type='button' ${lclBooking.spotRate ? "":"style='display:none;'"} id="lclSpotRate" value="Spot Rate"
                                           class="${lclBooking.spotWmRate!=null && lclBooking.spotWmRate!='' ? 'green-background' : 'button-style1'} lclSpotRate" 
                                           onClick ="showSpotRateInfo('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}');"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td colspan="2">
                                    <label id="spotratelabel">${lclBooking.spotRate ? spotratelabel:''}</label>
                                    <input type="hidden" name="spotLabel" id="spotLabel" value="${spotratelabel}"/>
                                    <input type="hidden" name="engmet" id="engmet" value="${engmet}"/>
                                    <input type="hidden" name="weight" styleClass="text twoDigitDecFormat"  id="weight" />
                                    <input type="hidden" name="measure" styleClass="text twoDigitDecFormat"  id="measure" />
                                </cong:td>

                            </cong:tr>
                        </cong:table>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:td>
    </cong:tr>



    <cong:hidden name="issuingTerminal" id="issuingTerminal" value="${lclBookingForm.issuingTerminal}"/>
    <cong:hidden name="ptrmnum" id="ptrmnum" value="${lclBookingForm.trmnum}"/>
    <cong:hidden name="to" id="to" value="${lclBookingForm.to}"/>
    <cong:hidden name="manualCompanyName" id="manualCompanyName" value="${lclBookingForm.manualCompanyName}"/>
    <cong:hidden name="cityStateZip" id="cityStateZip" value="${lclBookingForm.cityStateZip}"/>
    <cong:hidden name="pickupCutoffDate" id="pickupCutoffDate" value="${lclBookingForm.pickupCutoffDate}"/>
    <cong:hidden name="pickupReadyDate" id="pickupReadyDate" value="${lclBookingForm.pickupReadyDate}"/>
    <cong:hidden name="pickupReferenceNo" id="pickupReferenceNo" value="${lclBookingForm.pickupReferenceNo}"/>
    <cong:hidden name="whsecompanyName" id="whsecompanyName" value="${lclBookingForm.whsecompanyName}"/>
    <cong:hidden name="whseAddress" id="whseAddress" value="${lclBookingForm.whseAddress}"/>
    <cong:hidden name="whseCity" id="whseCity" value="${lclBookingForm.whseCity}"/>
    <cong:hidden name="whseState" id="whseState" value="${lclBookingForm.whseState}"/>
    <cong:hidden name="whseZip" id="whseZip" value="${lclBookingForm.whseZip}"/>
    <cong:hidden name="whsePhone" id="whsePhone" value="${lclBookingForm.whsePhone}"/>
    <cong:hidden name="pickupInstructions" id="pickupInstructions" value="${lclBookingForm.pickupInstructions}"/>
    <cong:hidden name="termsOfService" id="termsOfService" value="${lclBookingForm.termsOfService}"/>
    <cong:hidden name="chargeAmount" id="chargeAmount" value="${lclBookingForm.chargeAmount}"/>
    <cong:hidden name="pickupCost" id="pickupCost" value="${lclBookingForm.pickupCost}"/>
    <cong:hidden name="spAcct" id="spAcct" value="${lclBookingForm.spAcct}"/>
    <cong:hidden name="scacCode" id="scacCode" value="${lclBookingForm.scacCode}"/>
    <cong:hidden name="pfax1" id="pfax1" value="${lclBookingForm.pfax1}"/>
    <cong:hidden name="pemail1" id="pemail1" value="${lclBookingForm.pemail1}"/>
    <cong:hidden name="pcontactName" id="pcontactName" value="${lclBookingForm.pcontactName}"/>
    <cong:hidden name="paddress" id="paddress" value="${lclBookingForm.paddress}"/>
    <cong:hidden name="pphone1" id="pphone1" value="${lclBookingForm.pphone1}"/>
    <cong:hidden name="pickupHours" id="pickupHours" value="${lclBookingForm.pickupHours}"/>
    <cong:hidden name="pickupReadyNote" id="pickupReadyNote" value="${lclBookingForm.pickupReadyNote}"/>
    <cong:hidden name="pcommodityDesc" id="pcommodityDesc" value="${lclBookingForm.pcommodityDesc}"/>
    <cong:hidden name="shipSupplier" id="shipSupplier" value="${lclBookingForm.shipSupplier}"/>
    <cong:hidden name="quickdrPortOfOriginId" id="quickdrPortOfOriginId" value="${lclBookingForm.quickdrPortOfOriginId}"/>
    <cong:hidden name="portOfOriginForDr" id="portOfOriginForDr" value="${lclBookingForm.portOfOriginForDr}"/>
    <cong:hidden name="shipAcctForDr" id="shipAcctForDr" value="${lclBookingForm.shipAcctForDr}"/>
    <cong:hidden name="finalDestinationForDr" id="finalDestinationForDr" value="${lclBookingForm.finalDestinationForDr}"/>
    <cong:hidden name="finalDestinationIdForDr" id="finalDestinationIdForDr" value="${lclBookingForm.finalDestinationIdForDr}"/>
    <cong:hidden name="shipContactForDr" id="shipContactForDr" value="${lclBookingForm.shipContactForDr}"/>
    <cong:hidden name="consContactForDr" id="consContactForDr" value="${lclBookingForm.consContactForDr}"/>
    <cong:hidden name="consAcctForDr" id="consAcctForDr" value="${lclBookingForm.consAcctForDr}"/>
    <cong:hidden name="fwdContactForDr" id="fwdContactForDr" value="${lclBookingForm.fwdContactForDr}"/>
    <cong:hidden name="commodityTypeForDr" id="commodityTypeForDr" value="${lclBookingForm.commodityTypeForDr}"/>
    <cong:hidden name="fwdAcctForDr" id="fwdAcctForDr" value="${lclBookingForm.fwdAcctForDr}"/>
    <cong:hidden name="commodityNoForDr" id="commodityNoForDr" value="${lclBookingForm.commodityNoForDr}"/>
    <cong:hidden name="bookedWeightImperial" id="bookedWeightImperial" value="${lclBookingForm.bookedWeightImperial}"/>
    <cong:hidden name="bookedVolumeImperial" id="bookedVolumeImperial" value="${lclBookingForm.bookedVolumeImperial}"/>
    <cong:hidden name="bookedPieceCount" id="bookedPieceCount" value="${lclBookingForm.bookedPieceCount}"/>
    <input type="hidden" name="packageDr" id="packageDr" value="Packages"/>
    <input type="hidden" name="packageIdDr" id="packageIdDr" value="175"/>
    <input type="hidden" name="commodityTypeId" id="commodityTypeId" value="${lclBookingForm.commodityTypeId}"/>
    <input type="hidden" name="printDr" id="printDr" value="${lclBookingForm.printDr}"/>
    <input type="hidden" name="labelFieldName" id="labelFieldName" value="${lclBookingForm.labelFieldName}"/>
    <input type="hidden" name="copyFnVal" id="copyFnVal" value="${lclBookingForm.copyFnVal}"/>
</table>
