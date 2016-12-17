<cong:table border="0">
    <tr class="caption">
        <td colspan="2"  align="left"></td>
        <td align="left">&nbsp;Trade Route</td>
        <td  colspan="2" align="right"></td>
    </tr>
    <cong:tr>
        <td width="30%" style="vertical-align: top;">
        <cong:table width="100%" cellpadding="0" cellspacing="0" border="0">
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" width="10%">Absolute Origin</cong:td>
                <cong:td width="20%">
                    <cong:autocompletor id="portOfOriginR" name="portOfOrigin" template="one" fields="NULL,NULL,originUnlocationCode,NULL,portOfOriginId" query="CONCAT_WITHOUT_US_COUNTRY" styleClass="textlabelsBoldForTextBoxWidth"
                                        width="500" container="NULL" value="${lclBookingForm.portOfOrigin}" shouldMatch="true" scrollHeight="150" callback="calculateClearRates();"/>
                    <cong:hidden name="originUnlocationCode" id="originUnlocationCode" value="${lclBookingForm.originUnlocationCode}"/>
                    <cong:hidden name="portOfOriginId" id="portOfOriginId" value="${lclBookingForm.portOfOriginId}"/>
                    <span><img src="${path}/img/map.png" id="lclOriginMap" align="middle" width="12" height="12" title="Google Map Search"
                               onclick="getOriginGoogleMap('${path}', 'displayOriginMap', 'I');" /></span>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" width="10%">POL</cong:td>
                <cong:td width="20%">
                    <cong:autocompletor name="portOfLoading" id="portOfLoading" styleClass="textlabelsBoldForTextBoxWidth" readOnly="false" template="one" 
                                        shouldMatch="true" fields="NULL,NULL,polUnlocationcode,NULL,portOfLoadingId" query="CONCAT_WITHOUT_US_COUNTRY" 
                                        width="500" labeltitle="Port Of Loading" container="NULL" value="${pol}" scrollHeight="200" 
                                        callback="submitAjaxFormForImpAgent('refreshImpOriginAgent','#lclBookingForm','#originRefresh','');calculateClearRates();"/>
                    <cong:hidden name="portOfLoadingId" id="portOfLoadingId" value="${portOfLoadingId}"/>
                    <cong:hidden name="polUnlocationcode" id="polUnlocationcode" value="${polUnlocationcode}"/>
                    <cong:hidden name="relaySearch" id="relaySearch" value="${relaySearch}"/>
                    <span valign="middle" id="showAstar" style="visibility: hidden">
                        <img src="${path}/img/icons/astar.gif" width="12" height="12" alt="star" id="clearRates" title="Change Origin & Destination"
                             onclick="clearAllValues('This will allow to change the Origin & Destination,but will remove all the existing rates.Continue?', $('#fileNumberId').val(), '');"/>
                    </span>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" width="10%">POD</cong:td>
                <cong:td width="20%">
                    <cong:autocompletor name="portOfDestination" id="portOfDestination" styleClass="textlabelsBoldForTextBoxWidth" readOnly="false"  
                                        query="CONCAT_WITH_US_STATE" fields="NULL,NULL,podUnlocationcode,NULL,portOfDestinationId" template="one" 
                                        width="500" container="NULL" scrollHeight="200" value="${pod}" shouldMatch="true" 
                                        callback="setFinalDestinationForImports('${lclBookingForm.moduleName}');showUpcomingSailingsForImp();fillforeignPOD();calculateClearRates();termsTodoBl();" />
                    <cong:hidden name="portOfDestinationId" id="portOfDestinationId" value="${portOfDestinationId}"/>
                    <cong:hidden name="podUnlocationcode" id="podUnlocationcode"  value="${podUnlocationcode}"/>
                    <cong:hidden name="polCode" id="polCode" value="${polCode}"/>
                    <cong:hidden name="podCode" id="podCode" value="${podCode}"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Final Destination </cong:td>
                <cong:td>
                    <cong:autocompletor id="finalDestinationR" name="finalDestination" template="one" fields="unlocationName,NULL,unlocationCode,finalDestinationId" callback="setIPICFSstate();checkTranshipment();fillforeignPOD();calculateRatesPolFD();showUpcomingSailingsForImp();"
                                        query="CONCAT_WITH_US_STATE" styleClass="mandatory textlabelsBoldForTextBoxWidth"  width="500" container="NULL" value="${lclBookingForm.finalDestination}" shouldMatch="true" scrollHeight="150"/>
                    <img src="${path}/img/map.png" id="lclDestinationMap" align="middle" width="12" height="12" title="Google Map Search"
                         onclick="getDestinationGoogleMap('${path}', 'displayOriginMap', 'I');" />
                    <cong:hidden name="finalDestinationId" id="finalDestinationId" value="${lclBookingForm.finalDestinationId}"/>
                    <cong:hidden name="unlocationCode" id="unlocationCode" value="${lclBookingForm.unlocationCode}"/>
                    <cong:hidden name="unlocationName" id="unlocationName" value="${lclBookingForm.unlocationName}"/>
                    <input type="hidden" name="oldFd" id="oldFd" value="${lclBookingForm.finalDestination}"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:hidden name="pickupFlag" id="pickupFlag" value="${lclBookingForm.pickupFlag}"/>
                <cong:hidden name="transType" id="transType" value="${lclBookingForm.transType}"/>
                <cong:hidden name="spReferenceNo" id="spReferenceNo" value="${lclBookingForm.spReferenceNo}"/>
                <cong:td styleClass="textlabelsBoldforlcl" width="5%" valign="middle">Door Delivery Y/N</cong:td>
                <cong:td colspan="2"  styleClass="textBoldforlcl">
                    <cong:radio value="Y" name="pooDoor" container="NULL" id="doorOriginY" onclick="showPickupButton();toggleDoorDeliveryComment();"/> Yes
                    <cong:radio value="N" name="pooDoor" container="NULL" id="doorOriginN" onclick="changeDoor('${lclBooking.lclFileNumber.status}');toggleDoorDeliveryComment();"/> No
                    <cong:div styleClass="button-style1 pickupInfo" style="float: right;" id="pickupInfo"
                              onclick="showPickupInfo('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', 'Door Delivery', '${lclssheader.closedBy}');">Door Delivery</cong:div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" width="5%">Door Dest/City/Zip</cong:td>
                <cong:td>
                    <c:choose>
                        <c:when test="${lclBooking.pooPickup==true}">
                            <c:set var="doorOriginCityZipClass" value="textlabelsBoldForTextBoxWidth"/>
                            <c:set var="readOnly" value="false"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="doorOriginCityZipClass" value="textlabelsBoldForTextBoxDisabledLookWidth"/>
                            <c:set var="readOnly" value="true"/>
                        </c:otherwise>
                    </c:choose>
                    <cong:autocompletor styleClass="${doorOriginCityZipClass}" readOnly="${readOnly}" id="doorOriginCityZip" name="doorOriginCityZip" query="CONCAT_CITY" fields="pickZip,pickCity,pickState"
                                        template="concatOrigin" container="NULL" width="400" shouldMatch="true" callback="changeDoorOrigin('${lclBooking.lclFileNumber.status}')" scrollHeight="200" value="${lclBookingForm.doorOriginCityZip}"/>
                    <img src="${path}/img/map.png" id="lclDoorOriginCityMap" align="middle" width="12" height="12" title="Google Map Search"
                         onclick="getDoorOriginCityGoogleMap('${path}');" />
                    <cong:hidden name="pickupCity" id="pickCity"/>
                    <cong:hidden name="pickupState" id="pickState"/>
                    <cong:hidden name="pickupZip" id="pickZip"/>
                    <cong:hidden name="duplicateDoorOrigin" id="duplicateDoorOrigin" value="${lclBookingForm.doorOriginCityZip}"/>
                </cong:td>
            </cong:tr>
            <cong:tr id="usaExit">
                <cong:td styleClass="textlabelsBoldforlcl">USA Port of Exit</cong:td>
                <cong:td> <cong:autocompletor id="portExit" name="portExit" query="RELAYNAME" fields="NULL,NULL,NULL,portExitId" callback="checkTranshipment();showUpcomingSailingsForImp();"
                                              template="one" container="NULL" width="300" shouldMatch="true" scrollHeight="200" styleClass="mandatory textlabelsBoldForTextBoxWidth" value="${lclBookingForm.portExit}"/></cong:td>
                <cong:hidden name="portExitId" id="portExitId" value="${lclBookingForm.portExitId}"/>
            </cong:tr>
            <cong:tr id="portDiscg">
                <cong:td styleClass="textlabelsBoldforlcl">Foreign Port of Discharge</cong:td>
                <cong:td><cong:autocompletor id="foreignDischarge" name="foreignDischarge" query="UNLOCATION" fields="NULL,NULL,NULL,foreignDischargeId" callback="showUpcomingSailingsForImp();"
                                             template="one" container="NULL" width="400" shouldMatch="true" scrollHeight="200" styleClass="mandatory textlabelsBoldForTextBoxWidth" value="${lclBookingForm.foreignDischarge}"/>
                </cong:td>
                <cong:hidden name="foreignDischargeId" id="foreignDischargeId" value="${lclBookingForm.foreignDischargeId}"/>
                <input type="hidden" id="impForeignPod" name="impForeignPod" value="${lclBookingImport.foreignPortOfDischarge.unLocationCode}"/>
            </cong:tr>
        </cong:table>
        </td>
        <cong:td width="30%" valign="top">
            <cong:table width="100%" cellpadding="0" cellspacing="0" border="0">
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Pickup Date</cong:td>
                    <cong:td>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="pickDate" value="${lclBookingImport.pickupDateTime}"/>
                        <cong:calendar container="NULL" styleClass="textlabelsBoldForTextBox" id="pickupDate" name="pickupDate" value="${pickDate}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">G/O Date</cong:td>
                    <cong:td>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="goDateTime" value="${lclBookingImport.goDatetime}"/>
                        <cong:calendar container="NULL" styleClass="textlabelsBoldForTextBox"  id="goDate" name="goDate" value="${goDateTime}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr id="ipiATD-hide-show">
                    <cong:td styleClass="textlabelsBoldforlcl">IPI ATD</cong:td>
                    <cong:td>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="ipiATDDate" value="${lclBookingImport.ipiATDDate}"/>
                        <cong:calendar container="NULL" styleClass="textlabelsBoldForTextBox"  id="ipiATDDate" name="ipiATDDate" value="${ipiATDDate}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr id="fd-container">
                    <cong:td styleClass="textlabelsBoldforlcl">ETA at FD</cong:td>
                    <cong:td>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="etaFdDate" value="${lclBookingImport.fdEta}"/>
                        <cong:calendar container="NULL" styleClass="textlabelsBoldForTextBox"  id="etaFDDate" name="etaFDDate" value="${etaFdDate}"/>
                        <input type="hidden" id="unitId" value="${lclBookingForm.unitId}"/>
                        <input type="hidden" id="headerId" value="${lclBookingForm.headerId}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">IPI CFS</cong:td>
                    <cong:td>
                        <cong:autocompletor name="stGeorgeAccount" template="tradingPartner" id="stGeorgeAccount" fields="stGeorgeAccountNo,NULL,NULL,NULL,NULL,NULL,NULL,NULL,ipiCfsSearchAdd,ipiCfsSearchCity,ipiCfsSearchPhone,ipiCfsSearchState,NULL,ipiCfsSearchZip,ipiCfsSearchFax,ipiCfsSearchCoName,ipiCfsFirmsCode"  scrollHeight="300" paramFields="ipiSearchState"
                                            styleClass="textlabelsBoldForTextBox textuppercaseLetter" query="IMPORT_CFS" width="500" container="NULL" shouldMatch="true" callback="showIpiCfsAddress()" value="${lclBookingImport.ipiCfsAcctNo.accountName}"/>
                        <input type="hidden" id="ipiSearchState" name="ipiSearchState"/>
                        <input type="hidden" name="ipiCfsFirmsCode" id="ipiCfsFirmsCode" value="${lclBookingImport.ipiCfsAcctNo.firmsCode}"/>
                        <input type="hidden" name="ipiCfsSearchCoName" id="ipiCfsSearchCoName" value="${lclBookingImport.ipiCfsAcctNo.custAddr.coName}"/>
                        <input type="hidden" name="ipiCfsSearchState" id="ipiCfsSearchState" value="${lclBookingImport.ipiCfsAcctNo.custAddr.state}"/>
                        <input type="hidden" name="ipiCfsSearchCity" id="ipiCfsSearchCity" value="${lclBookingImport.ipiCfsAcctNo.custAddr.city2}"/>
                        <input type="hidden" name="ipiCfsSearchPhone" id="ipiCfsSearchPhone" value="${lclBookingImport.ipiCfsAcctNo.custAddr.phone}"/>
                        <input type="hidden" name="ipiCfsSearchFax" id="ipiCfsSearchFax" value="${lclBookingImport.ipiCfsAcctNo.custAddr.fax}"/>
                        <input type="hidden" name="ipiCfsSearchZip" id="ipiCfsSearchZip" value="${lclBookingImport.ipiCfsAcctNo.custAddr.zip}"/>
                        <input type="hidden" name="ipiCfsSearchAdd" id="ipiCfsSearchAdd" value="${lclBookingImport.ipiCfsAcctNo.custAddr.address1}"/>
                        <input type="hidden" name="ipiCfsEmail1" id="ipiCfsEmail1" value="${lclBookingImport.ipiCfsAcctNo.custAddr.email1}"/>
                        <img src="${path}/images/icons/search_filter.png" id="ipiCFSEdit" class="clientSearchEdit" title="Click here to edit IPI CFS Search options"
                             onclick="showClientSearchOption('${path}', 'IMPORT_CFS')"/>
                        <cong:text name="stGeorgeAccountNo" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook" style="width:70px" id="stGeorgeAccountNo" container="NULL" value="${lclBookingImport.ipiCfsAcctNo.accountno}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td></cong:td>
                    <cong:td>
                        <cong:textarea rows="4" cols="35" readOnly="true"  styleClass="textlabelsBoldForTextBoxDisabledLook" name="stGeorgeAddress" id="stGeorgeAddress" value=""/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">IPI Load Number</cong:td>
                    <cong:td>
                        <cong:div>
                            <cong:text name="ipiLoadNo" id="ipiLoadNo" styleClass="textlabelsBoldForTextBox" value="${lclBookingImport.ipiLoadNo}" container="NULL" maxlength="20"/>
                        </cong:div>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Loaded on IPI Container</cong:td>
                    <cong:td>
                        <cong:div >
                            <cong:checkbox name="ipiLoadedContainer" id="ipiLoadedContainer" container="NULL"/>
                        </cong:div>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:td>
        <cong:td width="20%" valign="top">
            <cong:table width="100%" cellpadding="0" cellspacing="0" border="0">
                <cong:tr>
                    <cong:td>&nbsp;</cong:td>
                    <cong:td>&nbsp;</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Vessel Arrival</cong:td>
                    <cong:td>
                        <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="impVesselArrival" name="impVesselArrival" value="${lclBookingForm.impVesselArrival}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Approx Due</cong:td>
                    <cong:td>
                        <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="importApproxDue" name="importApproxDue" value="${lclBookingForm.importApproxDue}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Strip Date</cong:td>
                    <cong:td>
                        <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="stripDate" name="impStripDate" value="${lclBookingForm.impStripDate}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Unit IT#</cong:td>
                    <cong:td>
                        <input type="hidden" id="impUnitNoHidd" value="${lclBookingForm.unitItNo}">
                        <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook textuppercaseLetter" readOnly="true" id="unitItNo" name="unitItNo" value="${lclBookingForm.unitItNo}" />
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Unit IT Date</cong:td>
                    <cong:td>
                        <input type="hidden" id="impUnitDateHidd" value="${lclBookingForm.unitItDate}">
                        <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook" id="unitItDate" name="unitItDate" value="${lclBookingForm.unitItDate}" readOnly="true" />
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td align="right" styleClass="textlabelsBoldforlcl">Unit IT Port</cong:td>
                    <cong:td>
                        <input type="hidden" id="impUnitPortHidd" value="${lclBookingForm.unitItPort}">
                        <cong:text name="unitItPort" id="unitItPort" styleClass="textlabelsBoldForTextBoxDisabledLook" value="${lclBookingForm.unitItPort}" readOnly="true"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Last FD</cong:td>
                    <cong:td>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="lastFdDate" value="${lclBookingImport.lastFreeDateTime}"/>
                        <input type="hidden" id="fdDate" value="${lastFdDate}">
                        <cong:calendar container="NULL" styleClass="textlabelsBoldForTextBox" id="lastFd" name="lastFd" value="${lastFdDate}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr><cong:td ></cong:td> <cong:td style="font-weight:bold">Export Voyage</cong:td></cong:tr>
                <cong:tr><cong:td ></cong:td> <cong:td>
                        <cong:hidden name="masterScheduleNo" id="masterScheduleNo"/>
                        <input type="hidden" id="existMasterSchd" name="existMasterSchd" value="${lclBooking.bookedSsHeaderId.id}"/>
                        <c:set var="bookedSsDetail" value="${lclBooking.bookedSsHeaderId.vesselSsDetail}"/>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="polEtd" value="${bookedSsDetail.std}"/>
                        <span id="transSailing" class="greenBold">
                            <c:if test="${not empty polEtd}">${lclBooking.bookedSsHeaderId.scheduleNo} / ${polEtd}</c:if>
                        </span></cong:td></cong:tr>
            </cong:table>
        </cong:td>
        <cong:td>
            <cong:table>
                <cong:tr>
                    <cong:td></cong:td>
                    <cong:td style="font-weight:bold">
                        Imports Voyage
                        <c:choose>
                            <c:when test="${lclBooking.lclFileNumber.status eq 'X'}"><%-- terminated --%>
                                <input type="button" class="button"  value="Link"/>
                            </c:when>
                            <c:when test="${not empty lclBookingForm.impEciVoyage && not empty lclBooking.lclFileNumber.id}">
                                <c:if test="${empty lclBookingSegregation}">
                                    <div  class="button-style1" id="clearImpVoyage" style="float: right;width:20%;" onclick="unlinkDRBkgUnit('Are you sure you want to Unlink?');">
                                        Unlink
                                    </div>
                                </c:if>
                                <%-- hide by default, show only when it necessary (after unlink, show this button)--%>
                                <input type="button" style="display: none" class="button-style1" id="link-voy" onclick="linkVoyage('${path}');" value="Link"/>
                            </c:when>
                            <c:otherwise>
                                <input type="button" class="button" id="link-voyage" onclick="linkVoyage('${path}');" value="Link"/>
                            </c:otherwise>
                        </c:choose>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">ECI Voy #</cong:td>
                    <cong:td>
                        <cong:text styleClass="link textlabelsBoldForTextBoxDisabledLook" id="impEciVoyage" name="impEciVoyage" readOnly="true" value="${lclBookingForm.impEciVoyage}" onclick="openEciVoyage('${path}');"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Sail Date</cong:td>
                    <cong:td>
                        <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook" id="impSailDate" name="impSailDate" readOnly="true" style="align:right" value="${lclBookingForm.impSailDate}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">SS Line</cong:td>
                    <cong:td>
                        <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="impSsLine" name="impSsLine" style="align:right" value="${lclBookingForm.impSsLine}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Vessel Name </cong:td>
                    <cong:td>
                        <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="impVesselName" name="impVesselName" value="${lclBookingForm.impVesselName}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">SS Voy</cong:td>
                    <cong:td>
                        <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="impSsVoyage" name="impSsVoyage" value="${lclBookingForm.impSsVoyage}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Arrival pier</cong:td>
                    <cong:td>
                        <cong:text  styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="impPier" name="impPier" value="${lclBookingForm.impPier}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Unit #</cong:td>
                    <cong:td>
                        <cong:text  styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="impUnitNo" name="impUnitNo" value="${lclBookingForm.impUnitNo}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">CFS Devanning Whse</cong:td>
                    <cong:td>
                        <span title="<c:import url="/jsps/LCL/booking/addressTooltip.jsp">
                              <c:param name="whName" value="${lclBookingForm.impCFSWareName}"/>
                            <c:param name="whNo" value="${lclBookingForm.cfsWarehouseNo}"/>
                            <c:param name="whCoName" value="${lclBookingForm.cfsWarehouseCoName}"/>
                            <c:param name="whAddress" value="${lclBookingForm.cfsWarehouseAddress}"/>
                            <c:param name="whCity" value="${lclBookingForm.cfsWarehouseCity}"/>
                            <c:param name="whState" value="${lclBookingForm.cfsWarehouseState}"/>
                            <c:param name="whZip" value="${lclBookingForm.cfsWarehouseZip}"/>
                            <c:param name="whPhone" value="${lclBookingForm.cfsWarehousePhone}"/>
                            <c:param name="whFax" value="${lclBookingForm.cfsWarehouseFax}"/>
                            </c:import>">
                            <cong:text  styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="impCFSWareName"
                                        name="impCFSWareName" value="${lclBookingForm.impCFSWareName}"/>
                        </span>
                        <img alt="Copy" title="copy data to clipboard" src="${path}/img/copy_icon.gif" style="vertical-align: middle"
                             onclick="copyToClipboard('Name: ${lclBookingForm.impCFSWareName}\nAddress :${lclBookingForm.cfsWarehouseAddress}\nCity : ${lclBookingForm.cfsWarehouseCity}\nState : ${lclBookingForm.cfsWarehouseState}\nZip : ${lclBookingForm.cfsWarehouseZip}\nPhone : ${lclBookingForm.cfsWarehousePhone}\nFax : ${lclBookingForm.cfsWarehouseFax}')"/>
                        <%-- CFS Warehouse Address fields hidden --%>
                        <cong:hidden id="cfsWarehouseAddress" name="cfsWarehouseAddress" value="${lclBookingForm.cfsWarehouseAddress}" />
                        <cong:hidden id="cfsWarehouseCity" name="cfsWarehouseCity" value="${lclBookingForm.cfsWarehouseCity}" />
                        <cong:hidden id="cfsWarehouseState" name="cfsWarehouseState" value="${lclBookingForm.cfsWarehouseState}" />
                        <cong:hidden id="cfsWarehouseZip" name="cfsWarehouseZip" value="${lclBookingForm.cfsWarehouseZip}" />
                        <cong:hidden id="cfsWarehousePhone" name="cfsWarehousePhone" value="${lclBookingForm.cfsWarehousePhone}" />
                        <cong:hidden id="cfsWarehouseFax" name="cfsWarehouseFax" value="${lclBookingForm.cfsWarehouseFax}" />
                        <input type="hidden" id="cfsWarehouseNo" name="cfsWarehouseNo" value="${lclBookingForm.cfsWarehouseNo}" />
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Unit Terminal</cong:td>
                    <cong:td>
                        <span title="<c:import url="/jsps/LCL/booking/addressTooltip.jsp">
                              <c:param name="whName" value="${lclBookingForm.impUnitWareNo}"/>
                            <c:param name="whCoName" value="${lclBookingForm.unitWarehouseCoName}"/>
                            <c:param name="whAddress" value="${lclBookingForm.unitWarehouseAddress}"/>
                            <c:param name="whCity" value="${lclBookingForm.unitWarehouseCity}"/>
                            <c:param name="whState" value="${lclBookingForm.unitWarehouseState}"/>
                            <c:param name="whZip" value="${lclBookingForm.unitWarehouseZip}"/>
                            <c:param name="whPhone" value="${lclBookingForm.unitWarehousePhone}"/>
                            <c:param name="whFax" value="${lclBookingForm.unitWarehouseFax}"/>
                            </c:import>">
                            <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="impUnitWareNo"
                                       name="impUnitWareNo" value="${lclBookingForm.impUnitWareNo}"/>
                        </span>
                        <%-- Unit Terminal Warehouse Address fields hidden --%>
                        <cong:hidden id="unitWarehouseCoName" name="unitWarehouseCoName" value="${lclBookingForm.unitWarehouseCoName}" />
                        <cong:hidden id="unitWarehouseAddress" name="unitWarehouseAddress" value="${lclBookingForm.unitWarehouseAddress}" />
                        <cong:hidden id="unitWarehouseCity" name="unitWarehouseCity" value="${lclBookingForm.unitWarehouseCity}" />
                        <cong:hidden id="unitWarehouseState" name="unitWarehouseState" value="${lclBookingForm.unitWarehouseState}" />
                        <cong:hidden id="unitWarehouseZip" name="unitWarehouseZip" value="${lclBookingForm.unitWarehouseZip}" />
                        <cong:hidden id="unitWarehousePhone" name="unitWarehousePhone" value="${lclBookingForm.unitWarehousePhone}" />
                        <cong:hidden id="unitWarehouseFax" name="unitWarehouseFax" value="${lclBookingForm.unitWarehouseFax}" />
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:td>
        <cong:td width="20%" valign="top">
            <cong:table width="100%" cellpadding="0" cellspacing="0" border="0">
                <cong:tr>
                    <cong:td>&nbsp;</cong:td>
                    <cong:td></cong:td>
                </cong:tr>

                <tr  id="defaultAgentDetails">
                <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Default Agent</cong:td>
                <cong:td>
                    <cong:radio value="Y" name="defaultAgent" id="defaultAgent"  container="NULL" onclick="fillLCLdefaultAgent();"/> Yes
                    <cong:radio value="N" name="defaultAgent" id="defaultAgent" container="NULL" onclick="clearLCLDefaultValues();"/> No
                </cong:td>
                </tr>
                <cong:tr id="m">
                    <jsp:include page="/jsps/LCL/ajaxload/refreshAgent.jsp"/>
                </cong:tr>
                <tr id="agentNumberDetails">
                <cong:td styleClass="textlabelsBoldforlcl">Agent Number</cong:td>
                <cong:td>
                    <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook" id="agentNumber" 
                               name="exportAgentAcctNo" value="${lclBookingImport.exportAgentAcctNo.accountno}" readOnly="true"/>
                </cong:td>
                </tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">ERT Y/N</cong:td>
                    <cong:td>
                        <html:select property="rtdTransaction" styleId="rtdTransaction" value="${lclBookingForm.rtdTransaction}" styleClass="smallDropDown mandatory" onchange="setDefaultRouteAgent()">
                            <html:option value="">Select</html:option>
                            <html:option value="Y">Yes</html:option>
                            <html:option value="N">No</html:option>
                        </html:select>
                    </cong:td>
                </cong:tr>
                <tr id="agentInfoDetails">
                <cong:td styleClass="textlabelsBoldforlcl">
                    <c:choose>
                        <c:when test="${lclBookingForm.moduleName eq 'Imports'}">
                            <c:set var="agentLabel" value="ERT Agent"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="agentLabel" value="Agent Info"/>
                        </c:otherwise>
                    </c:choose>${agentLabel}</cong:td>
                <cong:td>
                    <c:choose>
                        <c:when test="${lclBookingForm.moduleName eq 'Imports'}">
                            <c:set var="agentType" value="I"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="agentType" value="E"/>
                        </c:otherwise>
                    </c:choose>
                    <cong:autocompletor id="agentInfo"  position="left" name="rtdAgentAcct" template="tradingPartner" params="${agentType}" width="600" shouldMatch="true" query="AGENT"
                                        styleClass="textlabelsBoldForTextBox"  container="NULL" value="${lclBooking.rtdAgentAcct.accountno}" scrollHeight="300px"/>
                </cong:td>
                </tr>
                <tr>
                <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Spot Rate</cong:td>
                <cong:td styleClass="textBoldforlcl">
                    <cong:radio value="Y" name="spotRate" container="NULL"/> Yes
                    <cong:radio value="N" name="spotRate" container="NULL"/> No
                </cong:td>
                <input type="hidden" name="engmet" id="engmet" value="${engmet}"/>
                <input type="hidden" name="weight"  id="weight" />
                <input type="hidden" name="measure" id="measure" />
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;padding: 10px;"></td></tr>
                <tr>
                    <td colspan="2" style="text-align: center;">
                <c:if test="${not empty lclBookingForm.voyageOwner}">
                    <input type="hidden" name="voyageOwner" id="voyageOwner" value="${lclBookingForm.voyageOwner}"/>
                    <span id="blueBold" style="font-size: 12px;">Voyage Owner:</span>
                    <span id="greenBold" style="font-size: 12px;">${lclBookingForm.voyageOwner}</span></c:if></td>
                </tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Term to do BL</cong:td>
                    <cong:td>
                        <cong:div id="showTerminalLocation">
                            <cong:autocompletor id="terminal" name="terminal" template="commTempFormat" styleClass="mandatory textlabelsBoldForTextBox" query="IMPORTTERMINAL" fields="NULL,trmnum"
                                                position="left"   width="300" container="NULL"   value="${lclBookingForm.terminal}" shouldMatch="true" scrollHeight="200px"/>
                            <cong:hidden name="trmnum" id="trmnum" value="${lclBookingForm.trmnum}"/>
                        </cong:div>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:td>
    </cong:tr>
</cong:table>
