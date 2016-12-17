<table  cellpadding="0" cellspacing="3" width="100%">
    <tr valign="top"><td>
            <table cellpadding="0" cellspacing="0" border="0" class="tableBorderNew"
                   style="border-bottom-width: 2px;" width="100%">
                <tr class="tableHeadingNew"><td>Master B/L</td></tr>
                <!-- shipper -->
                <tr>
                    <td><table cellpadding="0" width="100%">
                            <tr class="textlabelsBold"><td class="lab-sty">Shipper Name</td></tr>
                            <tr class="textlabelsBold">
                                <td><input name="houseName" value="${fclBl.houseShipperName}" onfocus="disableDojo(this)" maxlength="50"
                                           id="houseName" size="38" onblur="hideMasterShipperblur();"  class="textlabelsBoldForTextBox mandatory"  style="text-transform: uppercase"/>
                                    <input id="houseName_check"  type="hidden" value="${fclBl.houseShipperName}" />
                                    <div id="shipperMaster_choices"  style="display:none;" class="autocomplete"></div>
                                    <script type="text/javascript">
                                    initAutocompleteWithFormClear("houseName", "shipperMaster_choices", "houseShipper", "houseName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=3&acctTyp=S", "getMasterShipperInfo()", "onMasterBlur('${path}');");
                                    </script>
                            <html:checkbox property="ediShipperCheck" styleId="ediShipperCheck"
                                           name="transactionBean" onclick="editCustomerForEdi(this)"
                                           onmouseover="tooltip.show('<strong>Edit Shipper Name For EDI</strong>', null, event);"
                                           onmouseout="tooltip.hide();"/>
                            <img src="${path}/img/icons/display.gif" id="toggle"
                                 onclick="getMasterShipper(this.value)" />
                            <c:choose>
                                <c:when test="${isShipperNotes}">
                                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14"
                                         id="shipperIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#houseShipper').val(), jQuery('#houseName').val());"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14"
                                         id="shipperIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#houseShipper').val(), jQuery('#houseName').val());"/>
                                </c:otherwise>
                            </c:choose>
                            <img src="${path}/img/icons/add2.gif" id="addNewTP"
                                 onclick="openTradingPartner('shipperNameMasterBL')"/>
                            <c:if test="${importFlag && fclBl.readyToPost != 'M'}">
                                <img src="${path}/img/icons/star-blue.png" alt="Add Contact"  id="contactNameButton"
                                     onclick="getContactInfo('MS')"/>
                            </c:if>
                    </td>
                </tr>
                <tr class="textlabelsBold"><td class="lab-sty">Shipper Number</td></tr>
                <tr class="textlabelsBold">
                    <td>
                        <input name="houseShipper" maxlength="60" value="${fclBl.houseShipper}"
                               id="houseShipper"  size="38" readonly="true"
                               class="BackgrndColorForTextBox" style="text-transform: uppercase" tabindex="-1"/>
                        <input type="hidden" value="${fclBl.houseShipper}"
                               id="houseShipper_check" name="houseShipper_check"/>
                    </td>
                </tr>
                <tr class="textlabelsBold"><td class="lab-sty">Shipper Address</td></tr>
                <tr class="textlabelsBold">
                    <td>
                <html:textarea  property="houseShipper1" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox"
                                value="${fclBl.houseShipperAddress}" rows="6" cols="47" styleId="houseShipper1" />
        </td>
    </tr>
</table></td>
<td><table>
    </table></td>
</tr>

<!-- Consignee -->
<tr>
    <td><table cellpadding="0" width="100%">
            <tr class="textlabelsBold"><td class="lab-sty">Consignee Name</td></tr>

            <tr class="textlabelsBold">
                <td><input  class="textlabelsBoldForTextBox mandatory" name="houseConsigneeName" id="houseConsigneeName" maxlength="50"
                            value="${fclBl.houseConsigneeName}"  size="38" onblur="hideMasterConsigneeblur();" style="text-transform: uppercase" onfocus="disableDojo(this)" onkeyup="copyNotListedTp(this, 'masterConsigneeCopy')"/>
                    <input id="houseConsigneeName_check" type="hidden" value="${fclBl.houseConsigneeName}" />
                    <div id="houseConsigneeName_choices"  style="display:none;" class="autocomplete"></div>
                    <script type="text/javascript">
                                    initAutocompleteWithFormClear("houseConsigneeName", "houseConsigneeName_choices", "houseConsignee", "houseConsigneeName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=4&acctTyp=C", "getMasterConsigneeInfo('${importFlag}')", "onConsigneeBlur('${path}');");
                    </script>
                    <input type="hidden" id="masterConsigneeCopy"/>
            <html:checkbox property="masterConsigneeCheck" styleId="masterConsigneeCheck"
                           name="transactionBean" onclick="disableAutoComplete(this)"
                           onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);"
                           onmouseout="tooltip.hide();"/>
            <html:checkbox property="ediConsigneeCheck" styleId="ediConsigneeCheck"
                           name="transactionBean" onclick="editCustomerForEdi(this)"
                           onmouseover="tooltip.show('<strong>Edit Customer Name For EDI</strong>', null, event);"
                           onmouseout="tooltip.hide();"/>
            <img src="${path}/img/icons/display.gif" id="toggle"
                 onclick="getMasterConsignee(this.value)" />
            <c:choose>
                <c:when test="${isConsigneeNotes}">
                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14"
                         id="consigneeIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#houseConsignee').val(), jQuery('#houseConsigneeName').val());"/>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14"
                         id="consigneeIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#houseConsignee').val(), jQuery('#houseConsigneeName').val());"/>
                </c:otherwise>
            </c:choose>
            <img src="${path}/img/icons/add2.gif" id="addNewTP"
                 onclick="openTradingPartner('houseConsigneeNameBL')"/>
            <c:choose>
                <c:when test="${importFlag}">
                    <c:if test="${fclBl.readyToPost != 'M'}">
                        <img src="${path}/img/icons/star-blue.png" alt="Add Contact"  id="contactNameButton"
                             onclick="getContactInfo('MC')"/>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/comparison.gif" alt="Add Contact" id="contactNameButton"
                         onclick="getContactInfo('MC')"/>
                </c:otherwise>
            </c:choose>
    </td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Consignee Number</td></tr>
<tr class="textlabelsBold">
    <td><input  class="BackgrndColorForTextBox" name="houseConsignee" id="houseConsignee" maxlength="100"
                value="${fclBl.houseConsignee}"  readonly="true" size="38" style="text-transform: uppercase" tabindex="-1"/>
        <input  type="hidden" id="houseConsignee_check" value="${fclBl.houseConsignee}" />
    </td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Consignee Address</td></tr>
<tr class="textlabelsBold">
    <td><html:textarea property="houseConsignee1" style="text-transform: uppercase"  styleClass="textlabelsBoldForTextBox"
                   value="${fclBl.houseConsigneeAddress}" rows="6" cols="47" styleId="houseConsignee1" />
</td>
</tr>
</table></td>
<td><table>


    </table></td>
</tr>

<!-- Notify Party -->
<tr><td><table cellpadding="0" width="100%">
            <tr class="textlabelsBold"><td class="lab-sty">Notify Party Name</td></tr>
            <tr class="textlabelsBold">
                <td><input  class="textlabelsBoldForTextBox" name="houseNotifyPartyName" id="houseNotifyPartyName" maxlength="50"
                            value="${fclBl.houseNotifyPartyName}"  size="38" onblur="hideMasterNotifyblur();" style="text-transform: uppercase" onfocus="disableDojo(this)"
                            onkeyup="copyNotListedTp(this, 'masterNotifyCopy')"/>
                    <input id="houseNotifyPartyName_check" type="hidden" value="${fclBl.houseNotifyPartyName}" />
                    <div id="houseNotifyPartyName_choices"  style="display:none;" class="autocomplete"></div>
                    <script type="text/javascript">
                                    initAutocompleteWithFormClear("houseNotifyPartyName", "houseNotifyPartyName_choices", "houseNotifyParty", "houseNotifyPartyName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=5&acctTyp=C", "getMasterNotifyInfo()", "onPartyBlur('${path}');");
                    </script>
                    <input type="hidden" id="masterNotifyCopy"/>
            <html:checkbox property="masterNotifyCheck" styleId="masterNotifyCheck"
                           name="transactionBean" onclick="disableAutoComplete(this)"
                           onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);" onmouseout="tooltip.hide();"/>
            <html:checkbox property="ediNotifyPartyCheck" styleId="ediNotifyPartyCheck"
                           name="transactionBean" onclick="editCustomerForEdi(this)"
                           onmouseover="tooltip.show('<strong>Edit Customer Name For EDI</strong>', null, event);"
                           onmouseout="tooltip.hide();"/>
            <img src="${path}/img/icons/display.gif" id="toggle"
                 onclick="getMasterNotifyParty(this.value)"/>
            <c:choose>
                <c:when test="${isMNotifyNotes}">
                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14"
                         id="mNotifyIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#houseNotifyParty').val(), jQuery('#houseNotifyPartyName').val());"/>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14"
                         id="mNotifyIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#houseNotifyParty').val(), jQuery('#houseNotifyPartyName').val());"/>
                </c:otherwise>
            </c:choose>
            <img src="${path}/img/icons/add2.gif" id="addNewTP"
                 onclick="openTradingPartner('houseNotifyPartyNameBL')"/>

    </td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Notify Party Number</td></tr>
<tr class="textlabelsBold">
    <td><input  class="BackgrndColorForTextBox" name="houseNotifyParty" value="${fclBl.houseNotifyPartyNo}" maxlength="60"
                id="houseNotifyParty" readonly="true" size="38" style="text-transform: uppercase" tabindex="-1" />
        <input type="hidden" value="${fclBl.houseNotifyPartyNo}" id="houseNotifyParty_check" />
    </td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Notify Party Address</td></tr>
<tr class="textlabelsBold">
    <td style="padding-bottom: 6px;"><html:textarea property="houseNotifyPartyaddress"
                                                style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox"
                                                value="${fclBl.houseNotifyParty}" rows="6" cols="47" styleId="houseNotifyPartyaddress"/>
</td>
</tr>
</table></td>
<td><table>
    </table></td>
</tr>

</table>
</td><td>
    <table  cellpadding="0" border="0" cellspacing="0"
            class="tableBorderNew" width="100%">
        <tr class="tableHeadingNew"><td>House B/L </td></tr>
        <!-- shipper -->
        <tr><td><table cellpadding="0" width="100%">
                    <tr class="textlabelsBold"><td class="lab-sty">Shipper Name</td></tr>
                    <tr class="textlabelsBold">
                        <td><input name="accountName" size="38" onblur="hideHouseShipperblur();" maxlength="50" id="accountName" class="textlabelsBoldForTextBox mandatory"
                                   value="${fclBl.shipperName}" style="text-transform: uppercase" onfocus="disableDojo(this)" />
                            <input id="accountName_check" type="hidden" value="${fclBl.shipperName}" />
                            <div id="accountName_choices"  style="display:none;" class="autocomplete"></div>
                            <script type="text/javascript">
                                    initAutocompleteWithFormClear("accountName", "accountName_choices", "shipper", "accountName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=6&acctTyp=HS",
                                            "getHouseShipperInfo()", "onHouseBlur('${path}');");
                            </script>
                    <c:if test="${importFlag}">
                        <html:checkbox property="houseShipperCheck" styleId="houseShipperCheck" name="transactionBean"
                                       onclick="disableAutoComplete(this)" onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);"
                                       onmouseout="tooltip.hide();"/>
                    </c:if>
                    <html:checkbox property="editHouseShipperCheck" styleId="editHouseShipperCheck" name="transactionBean"
                                   onclick="editHouseShipperName(this)" onmouseover="tooltip.show('<strong>Edit Customer Name</strong>', null, event);"
                                   onmouseout="tooltip.hide();"/>
                    <img src="${path}/img/icons/display.gif" id="toggle" onclick="getshipper();"/>
                    <c:choose>
                        <c:when test="${isHShipperNotes}">
                            <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14"
                                 id="hShipperIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#shipper').val(), jQuery('#accountName').val());"/>
                        </c:when>
                        <c:otherwise>
                            <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14"
                                 id="hShipperIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#shipper').val(), jQuery('#accountName').val());"/>
                        </c:otherwise>
                    </c:choose>
                    <img src="${path}/img/icons/add2.gif" id="addNewTP" onclick="openTradingPartner('shipperNameBL');"/>
                    <c:choose>
                        <c:when test="${importFlag}">
                            <c:if test="${fclBl.readyToPost != 'M'}">
                                <img src="${path}/img/icons/star-blue.png" alt="Add Contact"  id="contactNameButton"
                                     onclick="getContactInfo('HS')"/>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <img src="${path}/img/icons/comparison.gif" alt="Add Contact"  id="contactNameButton"
                                 onclick="getContactInfo('HS')"/>
                        </c:otherwise>
                    </c:choose>
            </td>
        </tr>
        <tr class="textlabelsBold"><td class="lab-sty">Shipper Number</td></tr>
        <tr class="textlabelsBold">
            <td><input  name="shipper" maxlength="60" value="${fclBl.shipperNo}" class="BackgrndColorForTextBox"
                        id="shipper" readonly="true" size="38" style="text-transform: uppercase" tabindex="-1" />
                <input  type="hidden" value="${fclBl.shipperNo}" id="shipper_check" />
            </td>
        </tr>
        <tr class="textlabelsBold"><td class="lab-sty">Shipper Address</td></tr>
        <tr class="textlabelsBold">
            <td><html:textarea property="streamShip"  style="text-transform: uppercase"
                           styleClass="textlabelsBoldForTextBox"
                           value="${fclBl.shipperAddress}" rows="6" cols="47" styleId="streamShip" />
</td>
</tr>
</table></td>
</tr>

<!-- Consignee -->
<tr>
    <td><table cellpadding="0" width="100%">
            <tr class="textlabelsBold"><td class="lab-sty">Consignee Name</td></tr>
            <tr class="textlabelsBold">
                <td><input class="textlabelsBoldForTextBox mandatory"  name="consigneeName" id="consigneeName" maxlength="50"
                           value="${fclBl.consigneeName}"  size="38" onblur="hideHouseConsigneeblur();" style="text-transform: uppercase" onfocus="disableDojo(this)"
                           onkeyup="copyNotListedTp(this, 'consigneeCopy')"/>
                    <input id="consigneeName_check" type="hidden" value="${fclBl.consigneeName}" />
                    <div id="consigneeName_choices"  style="display:none;" class="autocomplete"></div>
                    <script type="text/javascript">
                                    initAutocompleteWithFormClear("consigneeName", "consigneeName_choices", "consignee", "consigneeName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=7&acctTyp=HC", "getHouseConsigneeInfo()", "onHouseConsigneeBlur('${path}');");
                    </script>
                    <input type="hidden" id="consigneeCopy"/>
            <html:checkbox property="consigneeCheck" styleId="consigneeCheck" name="transactionBean"
                           onclick="disableAutoComplete(this)" onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);"
                           onmouseout="tooltip.hide();"  />
            <html:checkbox property="editHouseConsigneeCheck" styleId="editHouseConsigneeCheck" name="transactionBean"
                           onclick="editHouseConsigneeName(this)" onmouseover="tooltip.show('<strong>Edit Customer Name</strong>', null, event);"
                           onmouseout="tooltip.hide();"/>
            <img src="${path}/img/icons/display.gif" id="toggle"onclick="getConsignee(this.value);"/>
            <c:choose>
                <c:when test="${isHConsigneeNotes}">
                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14"
                         id="hConsigneeIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#consignee').val(), jQuery('#consigneeName').val());"/>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14"
                         id="hConsigneeIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#consignee').val(), jQuery('#consigneeName').val());"/>
                </c:otherwise>
            </c:choose>
            <img src="${path}/img/icons/add2.gif" id="addNewTP" onclick="openTradingPartner('consigneeNameBL');"/>
            <c:if test="${importFlag && fclBl.readyToPost != 'M'}">
                <img src="${path}/img/icons/star-blue.png" alt="Add Contact"  id="contactNameButton"
                     onclick="getContactInfo('HC')" />
            </c:if>

    </td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Consignee Number</td></tr>
<tr class="textlabelsBold">
    <td><input  class="BackgrndColorForTextBox" name="consignee" value="${fclBl.consigneeNo}" maxlength="100"
                id="consignee" readonly="true" size="38" style="text-transform: uppercase" tabindex="-1" />
        <input type="hidden" value="${fclBl.consigneeNo}" id="consignee_check" />
    </td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Consignee Address</td></tr>
<tr class="textlabelsBold">
    <td><html:textarea property="streamShipConsignee" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox"
                   value="${fclBl.consigneeAddress}" rows="6" cols="47" styleId="streamShipConsignee"/>
</td>
</tr>
</table></td>
</tr>

<!-- Notify Party -->
<tr>
    <td><table cellpadding="0" width="100%">
            <tr class="textlabelsBold"><td class="lab-sty">Notify Party Name</td></tr>
            <tr class="textlabelsBold">
                <td><input  class="textlabelsBoldForTextBox" name="notifyPartyName" id="notifyPartyName" maxlength="50"
                            value="${fclBl.notifyPartyName}"  size="38"  onblur="hideHouseNotifyblur();" style="text-transform: uppercase" onfocus="disableDojo(this)"
                            onkeyup="copyNotListedTp(this, 'notifyCopy')"/>
                    <input id="notifyPartyName_check" type="hidden" value="${fclBl.notifyPartyName}" />
                    <div id="notifyPartyName_choices"  style="display:none;" class="autocomplete"></div>
                    <script type="text/javascript">
                                    initAutocompleteWithFormClear("notifyPartyName", "notifyPartyName_choices", "notifyParty", "notifyPartyName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=8&acctTyp=HC", "getHouseNotifyPartyInfo()", "onHousePartyBlur('${path}');");
                    </script>
                    <input type="hidden" id="notifyCopy"/>
            <html:checkbox property="notifyCheck" styleId="notifyCheck" name="transactionBean"
                           onclick="disableAutoComplete(this)" onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);"
                           onmouseout="tooltip.hide();"/>
            <html:checkbox property="editHouseNotifyCheck" styleId="editHouseNotifyCheck" name="transactionBean"
                           onclick="editHouseNotifyName(this)" onmouseover="tooltip.show('<strong>Edit Customer Name</strong>', null, event);"
                           onmouseout="tooltip.hide();"/>
            <img src="${path}/img/icons/display.gif" id="toggle" onclick="getNotifyParty(this.value);"/>
            <c:choose>
                <c:when test="${isHNotifyNotes}">
                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14"
                         id="hNotifyIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#notifyParty').val(), jQuery('#notifyPartyName').val());"/>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14"
                         id="hNotifyIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#notifyParty').val(), jQuery('#notifyPartyName').val());"/>
                </c:otherwise>
            </c:choose>
            <img src="${path}/img/icons/add2.gif" id="addNewTP" onclick="openTradingPartner('notifyPartyNameBL');"/>
    </td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Notify Party Number</td></tr>
<tr class="textlabelsBold">
    <td><input  class="BackgrndColorForTextBox" name="notifyParty" value="${fclBl.notifyParty}" maxlength="100"
                id="notifyParty" readonly="true" size="38" style="text-transform: uppercase" tabindex="-1"/>
        <input type="hidden" id="notifyParty_check" value="${fclBl.notifyParty}"/>
    </td></tr>
<tr class="textlabelsBold"><td class="lab-sty">Notify Party Address</td></tr>
<tr class="textlabelsBold">
    <td style="padding-bottom: 6px;">
<html:textarea  property="streamshipNotifyParty" style="text-transform: uppercase"
                styleClass="textlabelsBoldForTextBox" styleId="streamshipNotifyParty"
                value="${fclBl.streamshipNotifyParty}" rows="6" cols="47" />
</td></tr>
</table></td>

</tr>
</table>
</td><td>

    <table cellpadding="0" cellspacing="0" border="0" class="tableBorderNew"width="100%">
        <tr class="tableHeadingNew"><td>Freight Forwarder</td></tr>
        <tr><td><table cellpadding="0" width="100%">
                    <tr class="textlabelsBold">
                        <td class="lab-sty">Freight Forwarder Name</td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td><input name="forwardingAgentName" id="forwardingAgentName" value="${fclBl.forwardingAgentName}" size="38" maxlength="50" onfocus="disableDojo(this)"
                                   class="textlabelsBoldForTextBox" onblur="hideAgentNameCheckblur();" style="text-transform: uppercase" />
                            <input id="forwardingAgentName_check" type="hidden" value="${fclBl.forwardingAgentName}" />
                            <div id="forwardingAgentName_choices"  style="display:none;" class="autocomplete"></div>
                            <script type="text/javascript">
                                    initAutocompleteWithFormClear("forwardingAgentName", "forwardingAgentName_choices", "forwardingAgent1", "forwardingAgentName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=10&acctTyp=V", "getForwardingInfo()", "onFreightBlur('${path}');");
                            </script>
                    <c:choose>
                        <c:when test="${not empty fclBl.forwardingAgentName}">
                            <html:checkbox property="editAgentNameCheck" styleId="editAgentNameCheck"
                                           onclick="editAgentName(this)" onmouseover="tooltip.show('<strong>Edit Freight Forwarder Name</strong>', null, event);"
                                           onmouseout="tooltip.hide();"/>
                        </c:when>
                        <c:otherwise>
                            <html:checkbox property="editAgentNameCheck" styleId="editAgentNameCheck"
                                           onclick="editAgentName(this)" onmouseover="tooltip.show('<strong>Edit Freight Forwarder Name</strong>', null, event);"
                                           onmouseout="tooltip.hide();" style="visibility: hidden"/>
                        </c:otherwise>
                    </c:choose>
                    <img src="${path}/img/icons/display.gif" id="toggle"  onclick="getForwarderDetails();"/>
                    <c:choose>
                        <c:when test="${isFreightNotes}">
                            <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14"
                                 id="freightIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#forwardingAgent1').val(), jQuery('#forwardingAgentName').val());"/>
                        </c:when>
                        <c:otherwise>
                            <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14"
                                 id="freightIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#forwardingAgent1').val(), jQuery('#forwardingAgentName').val());"/>
                        </c:otherwise>
                    </c:choose>
                    <img src="${path}/img/icons/add2.gif" id="addNewTP" onclick="openTradingPartner('forwardingAgentNameBL');"/>
                    <c:if test="${importFlag}">
                        <img src="${path}/img/icons/comparison.gif" alt="Add Contact"  id="contactNameButtonForFF" onclick="getContactInfo('F')"/>
                    </c:if>
            </td></tr>
        <tr class="textlabelsBold">
            <td class="lab-sty">Freight Forwarder Number</td>
        </tr>
        <tr class="textlabelsBold">
            <td><input name="forwardingAgent1" value="${fclBl.forwardAgentNo}" style="text-transform: uppercase" maxlength="100"
                       id="forwardingAgent1" readonly ="true" size="38" class="BackgrndColorForTextBox" tabindex="-1"/>
                <input type="hidden" id="forwardingAgent1_check" value="${fclBl.forwardAgentNo}" />
            </td>
        </tr>
        <tr class="textlabelsBold">
            <td class="lab-sty">Freight Forwarder Address</td></tr>
        <tr class="textlabelsBold"><td colspan="2">
        <html:textarea property="forwardingAgentno"  style="text-transform: uppercase"   styleClass="textlabelsBoldForTextBox"
                       value="${fclBl.forwardingAgent}" rows="6" cols="47" styleId="forwardingAgentno"/></td></tr>
</table>
</td>
</tr>
<tr>
    <td><table cellpadding="0">
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
        </table>
    </td>
</tr>
<tr>
    <td><table cellpadding="0">
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
        </table>
    </td>
</tr>
<tr>
    <td><table cellpadding="0">
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
            <tr><td>&nbsp;</td></tr>
        </table>
    </td>
</tr>
</table>
</td>
</tr>
</table>