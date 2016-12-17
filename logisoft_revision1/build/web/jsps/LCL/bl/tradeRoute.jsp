<%-- 
    Document   : tradeRoute
    Created on : Oct 24, 2012, 11:15:33 AM
    Author     : logiware
--%>
<cong:table>
    <c:set var="bookedSsDetail" value="${lclBl.bookedSsHeaderId.vesselSsDetail}"/>
    <cong:tr>
        <cong:td>
            <cong:table align="center" id="noteTable" cellpadding="0" cellspacing="0" width="100%" border="1" style="border:1px solid #dcdcdc">
                <cong:tr styleClass="tableHeadingNew">
                    <cong:td width="50%">TRADE ROUTE</cong:td>
                    <cong:td width="50%" align="left">ROUTING INSTRUCTIONS</cong:td>
                </cong:tr>
                <cong:tr >
                    <cong:td>
                        <cong:table>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl" width="6%">Place Of Receipt</cong:td>
                                <cong:td>
                                    <cong:autocompletor id="portOfOriginR" name="portOfOrigin" template="one" fields="NULL,NULL,unlocationCode,portOfOriginId" query="US_CANADA_CITY" styleClass="textlabelsBoldForTextBox textBig"
                                                        width="500" container="NULL" value="${pickUpCity}"  shouldMatch="true" scrollHeight="200px"/>
                                    <cong:hidden name="portOfOriginId" id="portOfOriginId" />
                                    <cong:hidden name="pooCode" id="pooCode" value="${lclBlForm.pooCode}"/>
                                </cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">Pier</cong:td>
                                <cong:td>
                                    <cong:autocompletor id="pier" name="pier" template="one" fields="" query="CONCAT_WITH_US_STATE" styleClass="textlabelsBoldForTextBox textBig"
                                                        width="500" container="NULL"  shouldMatch="true" scrollHeight="200px" value="${not empty lclBlForm.pier ? lclBlForm.pier : bookedSsDetail.departure.unLocationName}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Exp Carrier(VSL)(FLG)</cong:td>
                                <cong:td><cong:text styleClass="textlabelsBoldForTextBox textBig" id="vesselName" name="vesselName" value="${not empty lclBlForm.vesselName ? lclBlForm.vesselName : bookedSsDetail.spReferenceName}"/></cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">Port Of Loading</cong:td>
                                <cong:td styleClass="textlabelsBoldleftforlcl">
                                    <cong:autocompletor name="portOfLoading" id="portOfLoading" styleClass="${fieldCss} textlabelsBoldForTextBox textBig" template="BlDojo"
                                                        fields="NULL,NULL,NULL,portOfLoadingId" query="PORT_WO_UNLOC" width="500" labeltitle="Port Of Loading" container="NULL"
                                                        value="${pol}" scrollHeight="200px"/>
                                    <cong:hidden name="portOfLoadingId" id="portOfLoadingId" value="${lclBl.portOfLoading.id}"/>
                                    <cong:hidden name="polUnlocationcode" id="polUnlocationcode" value="${polUnlocationcode}"/>
                                    <input type="hidden" name="relaySearch" id="relaySearch" value="${relaySearch}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Sea Port Of Discharge</cong:td>
                                <cong:td styleClass="textlabelsBoldleftforlcl">
                                    <cong:autocompletor name="portOfDestination" id="portOfDestination" styleClass="${fieldCss} textlabelsBoldForTextBox textBig" query="PORT_WO_UNLOC"
                                                        fields="NULL,NULL,NULL,portOfDestinationId" template="BlDojo" width="500" container="NULL" scrollHeight="200px"
                                                        value="${pod}" callback="getTermsType(false)" />
                                    <cong:hidden name="portOfDestinationId" id="portOfDestinationId" value="${lclBl.portOfDestination.id}"/>
                                    <cong:hidden name="podUnlocationcode" id="podUnlocationcode"  value="${podUnlocationcode}"/>
                                </cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">Final Delivery To</cong:td>
                                <cong:td >
                                    <input type="hidden" id="previousDestName" value="${lclBlForm.finalDestination}"/>
                                    <cong:autocompletor id="finalDestinationf" name="finalDestination" template="BlDojo" fields="unlocationName,NULL,unlocationCode,finalDestinationId"
                                                        query="DEST_WO_UNLOC" paramFields="portOfOriginId" callback="setDeliveryMetro();calculateBlCharge()"
                                                        styleClass="textlabelsBoldForTextBox textBig"  width="500" container="NULL" value="${lclBlForm.finalDestination}" shouldMatch="true" scrollHeight="200px"/>
                                    <cong:hidden name="finalDestinationId" id="finalDestinationId" value="${lclBlForm.finalDestinationId}"/>
                                    <cong:hidden name="finalDestinationId" id="finalDestinationId"/>
                                    <cong:hidden name="unlocationCode" id="unlocationCode" value="${lclBlForm.unlocationCode}"/>
                                    <cong:hidden name="unlocationName" id="unlocationName" value="${lclBlForm.unlocationName}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="setTopBorderForTable" colspan="4"></cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">SS Line</cong:td>
                                <cong:td><cong:text styleClass="textlabelsBoldForTextBox textBig" id="ssLine" name="ssLine" value="${not empty lclBlForm.ssLine ? lclBlForm.ssLine : bookedSsDetail.spAcctNo.accountName}"/>
                                    <cong:hidden name="masterScheduleNo" id="masterScheduleNo" value="${lclBl.bookedSsHeaderId.id}"/>
                                </cong:td>

                                <cong:td styleClass="textlabelsBoldforlcl">SS Voy#</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test ="${not empty lclBlForm.ssVoyage1}">
                                            <cong:text styleClass="textlabelsBoldForTextBox textBig"  id="ssVoyage1" name="ssVoyage1" value="${lclBlForm.ssVoyage1}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <cong:text styleClass="textlabelsBoldForTextBox textBig"  id="ssVoyage" name="ssVoyage" value="${bookedSsDetail.spReferenceNo}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">ECI Voy#</cong:td>
                                <input type="hidden" id="pickedOnVoyageNo" value="${lclBlForm.voyageNumber}"/>
                                <input type="hidden" id="sailDate1" value="${lclBlForm.sailDate1}"/>
                                <input type="hidden" id="bookedForVoyageNo" value="${lclBl.lclFileNumber.lclBooking.bookedSsHeaderId.scheduleNo}"/>
                                <fmt:formatDate pattern="dd-MMM-yyyy" var="bookedForVoyagesailDate" value="${lclBl.lclFileNumber.lclBooking.bookedSsHeaderId.vesselSsDetail.std}"/>
                                <input type="hidden" id="bookedForVoyagesailDate" value="${bookedForVoyagesailDate}"/>
                                <cong:td>
                                    <cong:text styleClass="textlabelsBoldForTextBox text-readonly textBig" id="eciVoyage"  name="eciVoyage" 
                                               value="${not empty lclBlForm.voyageNumber ? lclBlForm.voyageNumber : lclBl.bookedSsHeaderId.scheduleNo}" readOnly="true"/>
                                    <img src="${path}/img/icons/astar.gif" title="Upcoming Sailings" alt="sailings" onclick="upcomingSailingsResult('${path}')"/> 
                                </cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">Sail Date</cong:td>
                                <cong:td><fmt:formatDate pattern="dd-MMM-yyyy" var="polEtd" value="${bookedSsDetail.std}"/>
                                    <cong:text styleClass="textlabelsBoldForTextBox text-readonly textBig"
                                               id="sailDate" name="sailDate" value="${not empty lclBlForm.sailDate1 ? lclBlForm.sailDate1 : polEtd}" readOnly="true"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Unit#</cong:td>
                                <cong:td><cong:text styleClass="textlabelsBoldForTextBox text-readonly textBig text-transform: uppercase" 
                                                    id="unitNumber" name="unitNumber" value="${lclBlForm.unitNumber}" readOnly="true"/>
                                </cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">Seal#</cong:td>
                                <cong:td><cong:text styleClass="textlabelsBoldForTextBox text-readonly textBig text-transform: uppercase" 
                                                    id="sealNumber" name="sealNumber" value="${lclBlForm.sealNumber}" readOnly="true"/>
                                </cong:td>
                            </cong:tr>

                        </cong:table>
                    </cong:td>
                    <cong:td>
                        <cong:table>
                            <cong:tr>
                                <cong:td>
                                    <cong:textarea id="routingInstruction"  styleClass="textlabelsBoldForTextBox commonClass" name="routingInstruction" 
                                                   cols="89" rows="8"  style="text-transform: uppercase"  value="${lclBlForm.routingInstruction}"/>
                                </cong:td>
                                <cong:td></cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>

                </cong:tr>

            </cong:table>
        </cong:td>
    </cong:tr>
</cong:table>
