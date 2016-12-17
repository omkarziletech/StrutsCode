<cong:table>
    <tr class="caption">
        <td colspan="2"  align="left"></td>
        <td align="left">Trade Route</td>
        <td  colspan="2" align="right"></td>
    </tr>
    <cong:tr>
        <td width="30%" style="vertical-align: top;">
        <cong:table width="100%" cellpadding="0" cellspacing="0" border="0">
            <cong:tr>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" width="14%">Absolute Origin</cong:td>
                <cong:td width="20%">
                    <cong:autocompletor id="portOfOriginR" name="portOfOrigin" template="one" fields="NULL,NULL,originUnlocationCode,NULL,portOfOriginId" query="CONCAT_WITHOUT_US_COUNTRY" shouldMatch="true" scrollHeight="200"
                                        styleClass="textlabelsBoldForTextBoxWidth" width="400" container="NULL" paramFields="finalDestinationId" value="${lclQuoteForm.portOfOrigin}" />
                    <cong:hidden name="originUnlocationCode" id="originUnlocationCode" value="${lclQuoteForm.originUnlocationCode}"/>
                    <cong:hidden name="portOfOriginId" id="portOfOriginId" value="${lclQuoteForm.portOfOriginId}"/>
                    <span><img src="${path}/img/map.png" id="lclOriginMap" align="middle" width="12" height="12" title="Google Map Search"
                               onclick="getOriginGoogleMap('${path}', 'displayOriginMap', 'I')" /></span>
                </cong:td>
            </cong:tr>






            <cong:tr>
                <cong:td id="polLabel" valign="bottom" styleClass="textlabelsBoldforlcl">POL</cong:td>
                <cong:td id="polDojo">
                    <cong:autocompletor name="portOfLoading" id="portOfLoading" styleClass="textlabelsBoldForTextBoxWidth" readOnly="false" template="one" shouldMatch="true"
                                        fields="NULL,NULL,polUnlocationcode,NULL,portOfLoadingId" query="CONCAT_WITHOUT_US_COUNTRY" width="500" labeltitle="Port Of Loading" container="NULL"
                                        value="${pol}" scrollHeight="200" callback="calculateClearRates();submitAjaxFormForImpAgent('refreshImpOriginAgent','#lclQuoteForm','#originRefresh','');"/>
                    <cong:hidden name="portOfLoadingId" id="portOfLoadingId" value="${portOfLoadingId}"/>
                    <cong:hidden name="polUnlocationcode" id="polUnlocationcode" value="${polUnlocationcode}"/>
                    <cong:hidden name="relaySearch" id="relaySearch" value="${relaySearch}"/>
                    <input type="checkbox" name="showAllCities" id="showAllCities" onclick="showAllCitiesInCountry()" title="Lookup by Country name" style="vertical-align: middle" />
                    <span valign="middle" id="showAstar" style="visibility: hidden">
                        <img src="${path}/img/icons/astar.gif" width="12" height="12" alt="star" id="clearRates" title="Change Origin & Destination" style="vertical-align: middle"
                             onclick="clearAllValues('This will allow to change the Origin & Destination,but will remove all the existing rates.Continue?', $('#fileNumberId').val(), '');"/>
                    </span>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="textlabelsBoldforlcl">POD</cong:td>
                <cong:td>
                    <cong:autocompletor name="portOfDestination" id="portOfDestination" styleClass="textlabelsBoldForTextBoxWidth" readOnly="false" query="CONCAT_WITH_US_STATE"
                                        fields="NULL,NULL,podUnlocationcode,NULL,portOfDestinationId" template="one" width="500" container="NULL"
                                        scrollHeight="200" value="${pod}" shouldMatch="true"
                                        callback="setFinalDestinationForImports('${lclQuoteForm.moduleName}');calculateClearRates();termsTodoBl();" />
                    <cong:hidden name="portOfDestinationId" id="portOfDestinationId" value="${portOfDestinationId}"/>
                    <cong:hidden name="podUnlocationcode" id="podUnlocationcode"  value="${podUnlocationcode}"/>
                </cong:td>
                <cong:hidden name="polCode" id="polCode" value="${polCode}"/>
                <cong:hidden name="podCode" id="podCode" value="${podCode}"/>
            </cong:tr>















            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Final Destination </cong:td>
                <cong:td>
                    <cong:autocompletor id="finalDestinationR" name="finalDestination" template="one" fields="unlocationName,NULL,unlocationCode,finalDestinationId"
                                        query="CONCAT_WITH_US_STATE" paramFields="portOfOriginId" callback="setIPICFSstate();checkTranshipment();calculateRatesPolFD();setTranshipment();externalGRIRemarks();"
                                        styleClass="mandatory textlabelsBoldForTextBoxWidth"  width="400" container="NULL" value="${lclQuoteForm.finalDestination}" shouldMatch="true" scrollHeight="200"/>
                    <cong:hidden name="finalDestinationId" id="finalDestinationId" value="${lclQuoteForm.finalDestinationId}"/>
                    <cong:hidden name="unlocationCode" id="unlocationCode" value="${lclQuoteForm.unlocationCode}"/>
                    <img src="${path}/img/map.png" id="lclDestinationMap" align="middle" width="12" height="12" title="Google Map Search"
                         onclick="getDestinationGoogleMap('${path}', 'displayOriginMap', 'I');" />
                    <input type="hidden" name="oldDesnCode" id="oldDesnCode" value="${lclQuote.finalDestination.unLocationCode}"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl"  valign="middle">Door Delivery Y/N</cong:td>
                <cong:td colspan="2" styleClass="textBoldforlcl">
                    <cong:radio value="Y" name="pooDoor" container="NULL" id="doorOriginY" onclick="showPickupButton();"/> Yes
                    <cong:radio value="N" name="pooDoor" container="NULL" id="doorOriginN" onclick="changeDoorOrigin();"/> No
                    <div class="button-style1 pickupInfo" style="float: right;" id="pickupInf"
                         onclick="showQuotePickupInfo('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}', 'Door Delivery');">Door Delivery</div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl" >Door Dest/City/Zip</cong:td>
                <cong:td>
                    <c:choose>
                        <c:when test="${lclQuote.pooDoor}">
                            <c:set var="doorOriginCityZipClass" value="textlabelsBoldForTextBoxWidth"/>
                            <c:set var="readOnly" value="false"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="doorOriginCityZipClass" value="textlabelsBoldForTextBoxDisabledLookWidth"/>
                            <c:set var="readOnly" value="true"/>
                        </c:otherwise>
                    </c:choose>
                    <cong:autocompletor styleClass="${doorOriginCityZipClass}" readOnly="${readOnly}" id="doorOriginCityZip" name="doorOriginCityZip" query="CONCAT_CITY" fields="pickZip,pickCity,pickState"
                                        template="concatOrigin" container="NULL" width="400" shouldMatch="true" callback="changeOrigin();" scrollHeight="200" value="${lclQuoteForm.doorOriginCityZip}"/>
                    <img src="${path}/img/map.png" id="lclDoorOriginCityMap" align="middle" width="12" height="12" title="Google Map Search"
                         onclick="getDoorOriginCityGoogleMap('${path}');" />
                    <input type="hidden" name="pickupCity" id="pickCity"/>
                    <cong:hidden name="pickupState" id="pickState"/>
                    <cong:hidden name="pickupZip" id="pickZip"/>
                    <input type="hidden" name="duplicateDoorOrigin" id="duplicateDoorOrigin" value="${lclQuoteForm.duplicateDoorOrigin}"/>
                </cong:td>
            </cong:tr>
            <cong:tr id="usaExit">
                <cong:td styleClass="textlabelsBoldforlcl">USA Port of Exit</cong:td>
                <cong:td>
                    <cong:autocompletor id="portExit" name="portExit" query="RELAYNAME" fields="NULL,NULL,NULL,portExitId"
                                        template="one" container="NULL" width="500" shouldMatch="true" scrollHeight="200"
                                        styleClass="textlabelsBoldForTextBoxWidth" value="${lclQuoteForm.portExit}"/>
                    <cong:hidden name="portExitId" id="portExitId" value="${lclQuoteForm.portExitId}"/>
                </cong:td>
            </cong:tr>
            <cong:tr id="portDiscg">
                <cong:td styleClass="textlabelsBoldforlcl">Foreign Port of Discharge</cong:td>
                <cong:td>
                    <cong:autocompletor id="foreignDischarge" name="foreignDischarge" query="UNLOCATION" fields="NULL,NULL,NULL,foreignDischargeId"
                                        template="one" container="NULL" width="500" shouldMatch="true" scrollHeight="200" styleClass="textlabelsBoldForTextBoxWidth"
                                        value="${lclQuoteForm.foreignDischarge}"/>
                </cong:td>
                <cong:hidden name="foreignDischargeId" id="foreignDischargeId" value="${lclQuoteForm.foreignDischargeId}"/>
            </cong:tr>
        </cong:table>
        </td>
        <cong:td width="25%" valign="top">
            <cong:table width="100%" cellpadding="0" cellspacing="0" border="0">
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Pickup Date</cong:td>
                    <cong:td>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="pickDate" value="${lclQuoteImport.pickupDateTime}"/>
                        <cong:calendar styleClass="textlabelsBoldForTextBox" id="pickupDate" name="pickupDate" container="NULL"  value="${pickDate}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">G/O Date</cong:td>
                    <cong:td>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="goDateTime" value="${lclQuoteImport.goDatetime}"/>
                        <cong:calendar styleClass="textlabelsBoldForTextBox" id="goDate" name="goDate" container="NULL" value="${goDateTime}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">IPI CFS</cong:td>
                    <cong:td>
                        <cong:autocompletor name="stGeorgeAccount" template="tradingPartner" id="stGeorgeAccount" fields="stGeorgeAccountNo,NULL,NULL,NULL,NULL,NULL,NULL,NULL,ipiCfsaddress,ipiCfsCity,ipiCfsPhone,ipiCfsState,NULL,ipiCfsZip,ipiCfsFax,ipiCfsCoName,ipiCfsFirmsCode"  scrollHeight="300" paramFields="ipiSearchState"
                                            styleClass="textlabelsBoldForTextBox textuppercaseLetter" query="IMPORT_CFS" width="600"  callback="showIpiCfsAddress()" container="NULL" shouldMatch="true" value="${lclQuoteImport.ipiCfsAcctNo.accountName}"/>
                        <input type="hidden" name="ipiSearchState" id="ipiSearchState"/>
                        <input type="hidden" name="ipiCfsFirmsCode" id="ipiCfsFirmsCode" value="${lclQuoteImport.ipiCfsAcctNo.firmsCode}"/>
                        <input type="hidden" name="ipiCfsSearchState" id="ipiCfsSearchState"/>
                        <input type="hidden" name="ipiCfsaddress" id="ipiCfsaddress" value="${lclQuoteImport.ipiCfsAcctNo.custAddr.address1}"/>
                        <input type="hidden" name="ipiCfsCoName" id="ipiCfsCoName" value="${lclQuoteImport.ipiCfsAcctNo.custAddr.coName}"/>
                        <input type="hidden" name="ipiCfsState" id="ipiCfsState" value="${lclQuoteImport.ipiCfsAcctNo.custAddr.state}"/>
                        <input type="hidden" name="ipiCfsCity" id="ipiCfsCity" value="${lclQuoteImport.ipiCfsAcctNo.custAddr.city2}"/>
                        <input type="hidden" name="ipiCfsPhone" id="ipiCfsPhone" value="${lclQuoteImport.ipiCfsAcctNo.custAddr.phone}"/>
                        <input type="hidden" name="ipiCfsZip" id="ipiCfsZip" value="${lclQuoteImport.ipiCfsAcctNo.custAddr.zip}"/>
                        <input type="hidden" name="ipiCfsAddress" id="ipiCfsAddress" value="${lclQuoteImport.ipiCfsAcctNo.custAddr.address1}"/>
                        <input type="hidden" name="ipiCfsSearchFax" id="ipiCfsFax" value="${lclQuoteImport.ipiCfsAcctNo.custAddr.fax}"/>
                        <img src="${path}/images/icons/search_filter.png" id="ipiSearchEdit" class="clientSearchEdit" title="Click here to edit IPI CFS Search options"
                             onclick="showClientSearchOption('${path}', 'IMPORT_CFS')"/>
                        <cong:text name="stGeorgeAccountNo" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook" style="width:70px" id="stGeorgeAccountNo" container="NULL" value="${lclQuoteImport.ipiCfsAcctNo.accountno}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td></cong:td>
                    <cong:td>
                        <cong:textarea rows="4" cols="35" readOnly="true"  styleClass="textlabelsBoldForTextBoxDisabledLook" name="stGeorgeAddress" id="stGeorgeAddress" value=""/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Term to do BL</cong:td>
                    <cong:td>
                        <cong:autocompletor id="terminalLocation" name="terminalLocation" template="commTempFormat" query="IMPORTTERMINAL" fields="NULL,trmnum"
                                         position="left"    width="500" container="NULL" styleClass="textlabelsBoldForTextBox mandatory" value="${lclQuoteForm.terminalLocation}" shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden name="trmnum" id="trmnum" value="${lclQuoteForm.trmnum}"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:td>
        <cong:td width="10%" valign="top">
            <cong:table width="100%" cellpadding="0" cellspacing="0">
                <cong:tr>
                    <cong:td>&nbsp;</cong:td>
                    <cong:td>&nbsp;</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Vessel Arrival</cong:td>
                    <cong:td>
                        <input type="text"class="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="vesselArrival" name="vesselArrival" value=""/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Approximate Due</cong:td>
                    <cong:td>
                        <input type="text"class="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="importApproxDue" name="importApproxDue" value=""/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Strip Date</cong:td>
                    <cong:td>
                        <input type="text" class="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="stripDate" name="stripDate" value=""/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Last FD</cong:td>
                    <cong:td>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="lastFdDateTime" value="${lclQuoteImport.lastFreeDateTime}"/>
                        <cong:calendar container="NULL" styleClass="textlabelsBoldForTextBox" id="lastFd" name="lastFd" value="${lastFdDateTime}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="6">
                        <cong:div id="trasitTime">
                            <cong:table cellpadding="0" cellspacing="0" width="100%">
                                <jsp:include page="/jsps/LCL/ajaxload/lclTransitTime.jsp"/>
                            </cong:table>
                        </cong:div>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:td>
        <cong:td width="20%">
            <cong:table>
                <cong:tr>
                    <cong:td></cong:td>
                    <cong:td style="font-weight:bold">Imports Voyage</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">ECI Voy#</cong:td>
                    <cong:td>
                        <input type="text"class="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="eciVoyage" name="eciVoyage"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Sail Date</cong:td>
                    <cong:td>
                        <input type="text"class="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="sailDate" name="sailDate"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">SS Line</cong:td>
                    <cong:td>
                        <input type="text"class="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="ssLine" name="ssLine"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Vessel Name </cong:td>
                    <cong:td>
                        <input type="text"class="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="vesselName" name="vesselName"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">SS Voy</cong:td>
                    <cong:td>
                        <input type="text"class="textlabelsBoldForTextBoxDisabledLook" readOnly="true" id="ssVoyage" name="ssVoyage"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>&nbsp;</cong:td>
                    <cong:td></cong:td>
                </cong:tr>
            </cong:table>
        </cong:td>
        <cong:td width="20%" valign="top">
            <cong:table width="100%" cellpadding="0" cellspacing="0" border="0">
                <cong:tr>
                    <cong:td>&nbsp;</cong:td>
                    <cong:td></cong:td>
                </cong:tr>
                <tr id="defaultAgentDetails">
                <cong:td valign="middle" styleClass="textlabelsBoldforlcl">  Default Agent</cong:td>
                <cong:td styleClass="textBoldforlcl">
                    <cong:radio value="Y" name="defaultAgent"  container="NULL" onclick="lclQuotedefaultAgent()"/> Yes
                    <cong:radio value="N" name="defaultAgent" container="NULL" onclick="lclQuoteDefaultValues()"/> No
                </cong:td>
                </tr>
                <cong:tr id="m">
                    <jsp:include page="/jsps/LCL/ajaxload/refreshQuoteAgentName.jsp"/>
                </cong:tr>
                <tr id="agentNumberDetails">
                <cong:td styleClass="textlabelsBoldforlcl">Agent Number</cong:td>
                <cong:td>
                    <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook" id="agentNumber" name="agentAcct" value="${lclQuoteImport.exportAgentAcctNo.accountno}" readOnly="true"/>
                </cong:td>
                </tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">ERT Y/N</cong:td>
                    <cong:td>
                        <html:select property="rtdTransaction" styleId="rtdTransaction" value="${lclQuoteForm.rtdTransaction}" styleClass="smallDropDown mandatory textlabelsBoldForTextBox" onchange="copyValuesofAgent()">
                            <html:option value="">Select</html:option>
                            <html:option value="Y">Yes</html:option>
                            <html:option value="N">No</html:option>
                        </html:select>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">ERT Agent</cong:td>
                    <cong:td>
                        <cong:autocompletor id="agentInfo"  position="left" name="rtdAgentAcct" template="tradingPartner" params="I" width="600" shouldMatch="true" query="AGENT"
                                            styleClass="text textlabelsBoldForTextBox"  container="NULL" value="${lclQuote.rtdAgentAcct.accountno}" scrollHeight="300px"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td valign="middle" styleClass="textlabelsBoldforlcl">Spot Rate</cong:td>
                    <cong:td>
                        <cong:table border="0" width="10%">
                            <cong:tr>
                                <cong:td styleClass="textBoldforlcl">
                                    <cong:radio value="Y" name="spotRate" container="NULL" onclick="updateSpotRate('Imports');
                                                refreshComm();"/> Yes
                                    <cong:radio value="N" name="spotRate" container="NULL" onclick="updateSpotRate('Imports');
                                                    refreshComm();"/> No</cong:td>
                                <c:choose>
                                    <c:when test="${lclQuote.spotWmRate!=null && lclQuote.spotWmRate!=''}">
                                        <cong:td align="left"><div id="lclSpotRate" class=" green-background  lclSpotRate" style="position: absolute" onclick="showSpotRateInfo('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}')">Spot Rate</div></cong:td>
                                    </c:when>
                                    <c:otherwise>
                                        <cong:td align="left"><div id="lclSpotRate" class="button-style1 lclSpotRate" style="position: absolute" onclick="showSpotRateInfo('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}')">Spot Rate</div></cong:td>
                                    </c:otherwise>
                                </c:choose>
                            </cong:tr>
                            <input type="hidden" name="weight" styleClass="text twoDigitDecFormat"  id="weight" />
                            <input type="hidden" name="measure" styleClass="text twoDigitDecFormat"  id="measure" />
                        </cong:table>
                    </cong:td>
                </cong:tr>

            </cong:table>
            <div id="lclSpotRateDisplay" class="lclSpotRateDisplay" style="position: absolute" align="right">
                <label id="rate"> ${lclQuote.spotWmRate}</label>
                <c:choose>
                    <c:when test="${lclQuote.spotRateUom=='W'}">
                        <c:if test="${engmet=='E'} ">
                            <label id="wei">
                                <c:if test="${not empty lclQuote.spotWmRate}">
                                    /100 LBS
                                </c:if></label>
                        </c:if>
                        <c:if test="${engmet=='M'}">
                            <label id="wei">
                                <c:if test="${not empty lclQuote.spotWmRate}">/1000 KGS</c:if></label>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${engmet=='E'}">
                            <label id="wei" >
                                <c:if test="${not empty lclQuote.spotWmRate}">/CFT
                                </c:if></label>
                        </c:if>
                        <c:if test="${engmet=='M'}">
                            <label id="wei">
                                <c:if test="${not empty lclQuote.spotWmRate}">/CBM</c:if></label>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </cong:td>
    </cong:tr>
</cong:table>
