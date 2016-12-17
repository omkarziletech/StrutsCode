<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                                <td><html:text property="fclBl.shipperName" onfocus="disableDojos(this)" maxlength="50" style="text-transform: uppercase"
                                           styleId="houseName" size="38" onblur="hideMasterShipperblur();" styleClass="textlabelsBoldForTextBox mandatory"/>
                            <input id="houseName_check"  type="hidden" value="${fclBlForm.fclBl.shipperName}" />
                            <div id="shipperMaster_choices"  style="display:none;" class="autocomplete"></div>
                            <script type="text/javascript">
                                    initAutocompleteWithFormClear("houseName", "shipperMaster_choices", "houseShipper", "houseName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=3&acctTyp=S", "getMasterShipperData()", "onMasterBlur('${path}');");
                            </script>
                            <c:choose>
                                <c:when test="${not empty fclBlForm.fclBl.shipperName}">
                                    <html:checkbox property="fclBl.ediShipperCheck" styleId="ediShipperCheck"
                                                   onclick="editAccountForEdi(this)"
                                                   onmouseover="tooltip.show('<strong>Edit Shipper Name For EDI</strong>', null, event);"
                                                   onmouseout="tooltip.hide();"/>
                                </c:when>
                                <c:otherwise>
                                    <html:checkbox property="fclBl.ediShipperCheck" styleId="ediShipperCheck"
                                                   onclick="editAccountForEdi(this)" style="visibility:hidden"
                                                   onmouseover="tooltip.show('<strong>Edit Shipper Name For EDI</strong>', null, event);"
                                                   onmouseout="tooltip.hide();"/>
                                </c:otherwise>
                            </c:choose>
                            <img src="${path}/img/icons/display.gif" id="toggle"
                                 onclick="getPartner('houseName', 'MasterShipper')" />
                            <c:choose>
                                <c:when test="${isShipperNotes}">
                                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Shipper Notes"
                                         id="shipperIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#houseShipper').val(), jQuery('#houseName').val());"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Shipper Notes"
                                         id="shipperIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#houseShipper').val(), jQuery('#houseName').val());"/>
                                </c:otherwise>
                            </c:choose>
                            <img src="${path}/img/icons/add2.gif" id="addNewTP"
                                 onclick="openTradingPartner('blMasterShipper')"/>
                            <c:if test="${null ne fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag eq 'I'}">
                                <img src="${path}/img/icons/star-blue.png" alt="Add Contact"  id="contactNameButton"
                                     onclick="openContactInfo('MS')"/>
                            </c:if>
                    </td>
                </tr>
                <tr class="textlabelsBold"><td class="lab-sty">Shipper Number</td></tr>
                <tr class="textlabelsBold">
                    <td>
                <html:text property="fclBl.shipperNo" maxlength="10"
                           styleId="houseShipper"  size="38" readonly="true"
                           styleClass="BackgrndColorForTextBox" style="text-transform: uppercase" tabindex="-1"/>
                <input type="hidden"
                       id="houseShipper_check" name="houseShipper_check"/>
        </td>
    </tr>
    <tr class="textlabelsBold"><td class="lab-sty">Shipper Address</td></tr>
    <tr class="textlabelsBold">
        <td>
    <html:textarea  property="fclBl.shipperAddress" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox"
                    rows="6" cols="47" styleId="houseShipper1" />
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
                <td><html:text  styleClass="textlabelsBoldForTextBox mandatory" property="fclBl.consigneeName" styleId="houseConsigneeName" maxlength="50"
                            size="38" onblur="hideMasterConsigneeblur();" style="text-transform: uppercase" onfocus="disableDojos(this)" onkeyup="copyNotListedPartner(this, 'masterConsigneeCopy')"/>
            <input id="houseConsigneeName_check" type="hidden"value="${fclBlForm.fclBl.consigneeName}"/>
            <div id="houseConsigneeName_choices"  style="display:none;" class="autocomplete"></div>
            <script type="text/javascript">
                                    initAutocompleteWithFormClear("houseConsigneeName", "houseConsigneeName_choices", "houseConsignee", "houseConsigneeName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=4&acctTyp=C", "getMasterConsigneeData()", "onConsigneeBlur('${path}');");
            </script>
            <input type="hidden" id="masterConsigneeCopy"/>
            <html:checkbox property="fclBl.masterConsigneeCheck" styleId="masterConsigneeCheck"
                           onclick="disableAutoCompleter(this)"
                           onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);"
                           onmouseout="tooltip.hide();"/>
            <c:choose>
                <c:when test="${not empty fclBlForm.fclBl.consigneeName}">
                    <html:checkbox property="fclBl.ediConsigneeCheck" styleId="ediConsigneeCheck"
                                   onclick="editAccountForEdi(this)"
                                   onmouseover="tooltip.show('<strong>Edit Customer Name For EDI</strong>', null, event);"
                                   onmouseout="tooltip.hide();"/>
                </c:when>
                <c:otherwise>
                    <html:checkbox property="fclBl.ediConsigneeCheck" styleId="ediConsigneeCheck"
                                   onclick="editAccountForEdi(this)" style="visibility: hidden"
                                   onmouseover="tooltip.show('<strong>Edit Customer Name For EDI</strong>', null, event);"
                                   onmouseout="tooltip.hide();"/>
                </c:otherwise>
            </c:choose>
            <img src="${path}/img/icons/display.gif" id="toggle"
                 onclick="getPartner('houseConsigneeName', 'MasterConsignee')" />
            <c:choose>
                <c:when test="${isConsigneeNotes}">
                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Shipper Notes"
                         id="consigneeIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#houseConsignee').val(), jQuery('#houseConsigneeName').val());"/>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Shipper Notes"
                         id="consigneeIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#houseConsignee').val(), jQuery('#houseConsigneeName').val());"/>
                </c:otherwise>
            </c:choose>
            <img src="${path}/img/icons/add2.gif" id="addNewTP"
                 onclick="openTradingPartner('blMasterConsignee')"/>
            <img src="${path}/img/icons/comparison.gif" alt="Add Contact" id="contactNameButton"
                 onclick="openContactInfo('MC')"/>
    </td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Consignee Number</td></tr>
<tr class="textlabelsBold">
    <td><html:text  styleClass="BackgrndColorForTextBox" property="fclBl.consigneeNo" styleId="houseConsignee" maxlength="100"
                readonly="true" size="38" style="text-transform: uppercase" tabindex="-1"/>
<input  type="hidden" id="houseConsignee_check"  />
</td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Consignee Address</td></tr>
<tr class="textlabelsBold">
    <td><html:textarea property="fclBl.consigneeAddress" style="text-transform: uppercase"  styleClass="textlabelsBoldForTextBox"
                   rows="6" cols="47" styleId="houseConsignee1" />
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
                <td><html:text  styleClass="textlabelsBoldForTextBox" property="fclBl.notifyPartyName" styleId="houseNotifyPartyName" maxlength="50"
                            size="38" onblur="hideMasterNotifyblur();" style="text-transform: uppercase" onfocus="disableDojos(this)"
                            onkeyup="copyNotListedPartner(this, 'masterNotifyCopy')"/>
            <input id="houseNotifyPartyName_check" type="hidden" value="${fclBlForm.fclBl.notifyPartyName}" />
            <div id="houseNotifyPartyName_choices"  style="display:none;" class="autocomplete"></div>
            <script type="text/javascript">
                                    initAutocompleteWithFormClear("houseNotifyPartyName", "houseNotifyPartyName_choices", "houseNotifyParty", "houseNotifyPartyName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=5&acctTyp=C", "getMasterNotifyData();", "onPartyBlur('${path}');");
            </script>
            <input type="hidden" id="masterNotifyCopy"/>
            <html:checkbox property="fclBl.masterNotifyCheck" styleId="masterNotifyCheck"
                           onclick="disableAutoCompleter(this)"
                           onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);" onmouseout="tooltip.hide();"/>
            <c:choose>
                <c:when test="${not empty fclBlForm.fclBl.notifyPartyName}">
                    <html:checkbox property="fclBl.ediNotifypartyCheck" styleId="ediNotifyPartyCheck"
                                   onclick="editAccountForEdi(this)"
                                   onmouseover="tooltip.show('<strong>Edit Customer Name For EDI</strong>', null, event);"
                                   onmouseout="tooltip.hide();"/>
                </c:when>
                <c:otherwise>
                    <html:checkbox property="fclBl.ediNotifypartyCheck" styleId="ediNotifyPartyCheck"
                                   onclick="editAccountForEdi(this)" style="visibility: hidden"
                                   onmouseover="tooltip.show('<strong>Edit Customer Name For EDI</strong>', null, event);"
                                   onmouseout="tooltip.hide();"/>
                </c:otherwise>
            </c:choose>
            <img src="${path}/img/icons/display.gif" id="toggle"
                 onclick="getPartner('houseNotifyPartyName', 'MasterNotifyParty')"/>
            <c:choose>
                <c:when test="${isMNotifyNotes}">
                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Shipper Notes"
                         id="mNotifyIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#houseNotifyParty').val(), jQuery('#houseNotifyPartyName').val());"/>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Shipper Notes"
                         id="mNotifyIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#houseNotifyParty').val(), jQuery('#houseNotifyPartyName').val());"/>
                </c:otherwise>
            </c:choose>
            <img src="${path}/img/icons/add2.gif" id="addNewTP"
                 onclick="openTradingPartner('blMasterNotify')"/>

    </td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Notify Party Number</td></tr>
<tr class="textlabelsBold">
    <td><html:text  styleClass="BackgrndColorForTextBox" property="fclBl.notifyParty"
                styleId="houseNotifyParty" readonly="true" size="38" style="text-transform: uppercase" tabindex="-1" />
<input type="hidden" value="${fclBl.houseNotifyPartyNo}" id="houseNotifyParty_check" />
</td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Notify Party Address</td></tr>
<tr class="textlabelsBold">
    <td style="padding-bottom: 6px;">
<html:textarea property="fclBl.houseNotifyParty"
               style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox"
               rows="6" cols="47" styleId="houseNotifyPartyaddress"/>
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
        <tr>
            <td>
                <table cellpadding="0" width="100%">
                    <c:choose>
                        <c:when test="${not empty fclBlForm.correctedShipperName}">
                            <tr class="textlabelsBold"><td class="lab-sty">Shipper Name</td></tr>
                            <tr class="textlabelsBold">
                                <td><html:text property="correctedShipperName" size="38" maxlength="50" readonly="true" styleId="correctedShipperName" styleClass="BackgrndColorForTextBox mandatory"/>
                            <img src="${path}/img/icons/display.gif" id="toggle" onclick="getPartner('accountName', 'Shipper')"/>
                            <img src="${path}/img/icons/add2.gif" id="addNewTP" onclick="openTradingPartner('blHouseShipper')"/>
                            <img src="${path}/img/icons/comparison.gif" alt="Add Contact"  id="contactNameButtonForFF" onclick="openContactInfo('CS')"/>
                    </td>
                </tr>
                <tr class="textlabelsBold"><td class="lab-sty">Shipper Number</td></tr>
                <tr class="textlabelsBold">
                    <td><html:text  property="correctedShipperNo"  styleClass="BackgrndColorForTextBox"
                                styleId="correctedShipperNo" readonly="true" size="38" style="text-transform: uppercase" tabindex="-1" maxlength="100" />
        </td>
    </tr>
    <tr class="textlabelsBold"><td class="lab-sty">Shipper Address</td></tr>
    <tr class="textlabelsBold">
        <td><html:textarea property="correctedShipperAddress"  style="text-transform: uppercase"
                       styleClass="textlabelsBoldForTextBox"
                       rows="6" cols="47" styleId="streamShip" />
</td>
</tr>
<html:hidden property="fclBl.houseShipperName"/>
<html:hidden property="fclBl.houseShipperNo"/>
<html:hidden property="fclBl.houseShipperAddress"/>
<input type="hidden" id="importFlag" value="${fclBlForm.fclBl.importFlag}"/>
</c:when>
<c:otherwise>
    <tr class="textlabelsBold"><td class="lab-sty">Shipper Name</td></tr>
    <tr class="textlabelsBold">
        <td><html:text property="fclBl.houseShipperName" size="38" maxlength="50" onblur="hideHouseShipperblur();" styleId="accountName" styleClass="textlabelsBoldForTextBox mandatory"
                   style="text-transform: uppercase" onfocus="disableDojos(this)" />
    <input id="accountName_check" type="hidden" value="${fclBlForm.fclBl.houseShipperName}" />
    <div id="accountName_choices"  style="display:none;" class="autocomplete"></div>
    <script type="text/javascript">
                                    initAutocompleteWithFormClear("accountName", "accountName_choices", "shipper", "accountName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=6&acctTyp=HS",
                                            "getHouseShipperData()", "onHouseBlur('${path}');");
    </script>
    <c:if test="${null ne fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag eq 'I'}">
        <html:checkbox property="fclBl.houseShipperCheck" styleId="houseShipperCheck"
                       onclick="disableAutoCompleter(this)" onmouseover="tooltip.show('<strong>TP Not Listed</strong>');"
                       onmouseout="tooltip.hide();"/>
    </c:if>
    <c:choose>
        <c:when test="${not empty fclBlForm.fclBl.houseShipperName}">
            <html:checkbox property="fclBl.editHouseShipperCheck" styleId="editHouseShipperCheck"
                           onclick="editHouseShipper(this)" onmouseover="tooltip.show('<strong>Edit Customer Name</strong>');"
                           onmouseout="tooltip.hide();"/>

        </c:when>
        <c:otherwise>
            <html:checkbox property="fclBl.editHouseShipperCheck" styleId="editHouseShipperCheck"
                           onclick="editHouseShipper(this)" onmouseover="tooltip.show('<strong>Edit Customer Name</strong>');"
                           onmouseout="tooltip.hide();" style="visibility: hidden"/>
        </c:otherwise>
    </c:choose>
    <img src="${path}/img/icons/display.gif" id="toggle" onclick="getPartner('accountName', 'Shipper')"/>
    <c:choose>
        <c:when test="${isHShipperNotes}">
            <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Shipper Notes"
                 id="hShipperIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#shipper').val(), jQuery('#accountName').val());"/>
        </c:when>
        <c:otherwise>
            <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Shipper Notes"
                 id="hShipperIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#shipper').val(), jQuery('#accountName').val());"/>
        </c:otherwise>
    </c:choose>
    <img src="${path}/img/icons/add2.gif" id="addNewTP" onclick="openTradingPartner('blHouseShipper')"/>
    <c:choose>
        <c:when test="${null ne fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag eq 'I'}">
            <img src="${path}/img/icons/star-blue.png" alt="Add Contact"  id="contactNameButton"
                 onclick="openContactInfo('HS')"/>
        </c:when>
        <c:otherwise>
            <img src="${path}/img/icons/comparison.gif" alt="Add Contact"  id="contactNameButton"
                 onclick="openContactInfo('HS')"/>
        </c:otherwise>
    </c:choose>
</td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Shipper Number</td></tr>
<tr class="textlabelsBold">
    <td><html:text  property="fclBl.houseShipperNo"  styleClass="BackgrndColorForTextBox"
                styleId="shipper" readonly="true" size="38" style="text-transform: uppercase" tabindex="-1" maxlength="100" />
<input  type="hidden"  id="shipper_check" />
</td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Shipper Address</td></tr>
<tr class="textlabelsBold">
    <td><html:textarea property="fclBl.houseShipperAddress"  style="text-transform: uppercase"
                   styleClass="textlabelsBoldForTextBox"
                   rows="6" cols="47" styleId="streamShip" />
</td>
</tr>
</c:otherwise>
</c:choose>
</table>
</td>
</tr>

<!-- Consignee -->
<tr>
    <td><table cellpadding="0" width="100%">
            <tr class="textlabelsBold"><td class="lab-sty">Consignee Name</td></tr>
            <tr class="textlabelsBold">
                <td><html:text styleClass="textlabelsBoldForTextBox mandatory"  property="fclBl.houseConsigneeName" styleId="consigneeName" maxlength="50"
                           size="38" onblur="hideHouseConsigneeblur();" style="text-transform: uppercase" onfocus="disableDojos(this)"
                           onkeyup="copyNotListedPartner(this, 'consigneeCopy')"/>
            <input id="consigneeName_check" type="hidden" value="${fclBlForm.fclBl.houseConsigneeName}"/>
            <div id="consigneeName_choices"  style="display:none;" class="autocomplete"></div>
            <script type="text/javascript">
                                    initAutocompleteWithFormClear("consigneeName", "consigneeName_choices", "consignee", "consigneeName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=7&acctTyp=C", "getHouseConsigneeData()", "onHouseConsigneeBlur('${path}');");
            </script>
            <input type="hidden" id="consigneeCopy"/>
            <html:checkbox property="fclBl.consigneeCheck" styleId="consigneeCheck"
                           onclick="disableAutoCompleter(this)" onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);"
                           onmouseout="tooltip.hide();"  />
            <c:choose>
                <c:when test="${not empty fclBlForm.fclBl.houseConsigneeName}">
                    <html:checkbox property="fclBl.editHouseConsigneeCheck" styleId="editHouseConsigneeCheck"
                                   onclick="editHouseConsignee(this)" onmouseover="tooltip.show('<strong>Edit Customer Name</strong>', null, event);"
                                   onmouseout="tooltip.hide();"/>

                </c:when>
                <c:otherwise>
                    <html:checkbox property="fclBl.editHouseConsigneeCheck" styleId="editHouseConsigneeCheck"
                                   onclick="editHouseConsignee(this)" onmouseover="tooltip.show('<strong>Edit Customer Name</strong>', null, event);"
                                   onmouseout="tooltip.hide();" style="visibility: hidden"/>

                </c:otherwise>
            </c:choose>
            <img src="${path}/img/icons/display.gif" id="toggle"onclick="getPartner('consigneeName', 'Consignee')"/>
            <c:choose>
                <c:when test="${isHConsigneeNotes}">
                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Shipper Notes"
                         id="hConsigneeIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#consignee').val(), jQuery('#consigneeName').val());"/>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Shipper Notes"
                         id="hConsigneeIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#consignee').val(), jQuery('#consigneeName').val());"/>
                </c:otherwise>
            </c:choose>
            <img src="${path}/img/icons/add2.gif" id="addNewTP" onclick="openTradingPartner('blHouseConsignee')"/>
            <c:choose>
                <c:when test="${null ne fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag eq 'I'}">
                    <img src="${path}/img/icons/star-blue.png" alt="Add Contact"  id="contactNameButton"
                         onclick="openContactInfo('HC')" />
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/comparison.gif" alt="Add Contact"  id="contactNameButton"
                         onclick="openContactInfo('HC')" />
                </c:otherwise>
            </c:choose>

    </td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Consignee Number</td></tr>
<tr class="textlabelsBold">
    <td><html:text  styleClass="BackgrndColorForTextBox" property="fclBl.houseConsignee"  maxlength="100"
                styleId="consignee" readonly="true" size="38" style="text-transform: uppercase" tabindex="-1" />
<input type="hidden"  id="consignee_check" />
</td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Consignee Address</td></tr>
<tr class="textlabelsBold">
    <td><html:textarea property="fclBl.houseConsigneeAddress" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox"
                   rows="6" cols="47" styleId="streamShipConsignee"/>
</td>
</tr>
</table></td>
</tr>

<!-- Notify Party -->
<tr>
    <td><table cellpadding="0" width="100%">
            <tr class="textlabelsBold"><td class="lab-sty">Notify Party Name</td></tr>
            <tr class="textlabelsBold">
                <td><html:text  styleClass="textlabelsBoldForTextBox" property="fclBl.houseNotifyPartyName" styleId="notifyPartyName" maxlength="50"
                            size="38" onblur="hideHouseNotifyblur();" style="text-transform: uppercase" onfocus="disableDojos(this)"
                            onkeyup="copyNotListedPartner(this, 'notifyCopy')"/>
            <input id="notifyPartyName_check" type="hidden"  value="${fclBlForm.fclBl.houseNotifyPartyName}"/>
            <div id="notifyPartyName_choices"  style="display:none;" class="autocomplete"></div>
            <script type="text/javascript">
                                    initAutocompleteWithFormClear("notifyPartyName", "notifyPartyName_choices", "notifyParty", "notifyPartyName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=8&acctTyp=C", "getHouseNotifyPartyData()", "onHousePartyBlur('${path}');");
            </script>
            <input type="hidden" id="notifyCopy"/>
            <html:checkbox property="fclBl.notifyCheck" styleId="notifyCheck"
                           onclick="disableAutoCompleter(this)" onmouseover="tooltip.show('<strong>TP Not Listed</strong>', null, event);"
                           onmouseout="tooltip.hide();"/>
            <c:choose>
                <c:when test="${not empty fclBlForm.fclBl.houseNotifyPartyName}">
                    <html:checkbox property="fclBl.editHouseNotifyCheck" styleId="editHouseNotifyCheck"
                                   onclick="editHouseNotifyParty(this)" onmouseover="tooltip.show('<strong>Edit Customer Name</strong>', null, event);"
                                   onmouseout="tooltip.hide();"/>
                </c:when>
                <c:otherwise>
                    <html:checkbox property="fclBl.editHouseNotifyCheck" styleId="editHouseNotifyCheck"
                                   onclick="editHouseNotifyParty(this)" onmouseover="tooltip.show('<strong>Edit Customer Name</strong>', null, event);"
                                   onmouseout="tooltip.hide();" style="visibility: hidden"/>
                </c:otherwise>
            </c:choose>
            <img src="${path}/img/icons/display.gif" id="toggle" onclick="getPartner('notifyPartyName', 'NotifyParty')"/>
            <c:choose>
                <c:when test="${isHNotifyNotes}">
                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Shipper Notes"
                         id="hNotifyIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#notifyParty').val(), jQuery('#notifyPartyName').val());"/>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Shipper Notes"
                         id="hNotifyIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#notifyParty').val(), jQuery('#notifyPartyName').val());"/>
                </c:otherwise>
            </c:choose>
            <img src="${path}/img/icons/add2.gif" id="addNewTP" onclick="openTradingPartner('blHouseNotify')"/>
    </td>
</tr>
<tr class="textlabelsBold"><td class="lab-sty">Notify Party Number</td></tr>
<tr class="textlabelsBold">
    <td><html:text  styleClass="BackgrndColorForTextBox" property="fclBl.houseNotifyPartyNo"  maxlength="100"
                styleId="notifyParty" readonly="true" size="38" style="text-transform: uppercase" tabindex="-1"/>
<input type="hidden" id="notifyParty_check" />
</td></tr>
<tr class="textlabelsBold"><td class="lab-sty">Notify Party Address</td></tr>
<tr class="textlabelsBold">
    <td style="padding-bottom: 6px;">
<html:textarea  property="fclBl.streamshipnotifyparty" style="text-transform: uppercase"
                styleClass="textlabelsBoldForTextBox" styleId="streamshipNotifyParty"
                rows="6" cols="47" />
</td></tr>
</table></td>

</tr>
</table>
</td><td>

    <table cellpadding="0" cellspacing="0" border="0" class="tableBorderNew"width="100%">
        <tr class="tableHeadingNew"><td>Freight Forwarder</td></tr>
        <tr><td><table cellpadding="0" width="100%">
                    <c:choose>
                        <c:when test="${not empty fclBlForm.correctedForwarderName}">
                            <tr class="textlabelsBold">
                                <td class="lab-sty">Freight Forwarder Name</td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td><html:text property="correctedForwarderName" styleId="correctedForwarderName"  size="38" maxlength="50"
                                           styleClass="BackgrndColorForTextBox" style="text-transform: uppercase" />
                            <img src="${path}/img/icons/display.gif" id="toggle"  onclick="getPartner('forwardingAgentName', 'Forwarder')"/>
                            <c:choose>
                                <c:when test="${isFreightNotes}">
                                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Shipper Notes"
                                         id="freightIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#correctedForwarderNo').val(), jQuery('#correctedForwarderName').val());"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Shipper Notes"
                                         id="freightIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#correctedForwarderNo').val(), jQuery('#correctedForwarderName').val());"/>
                                </c:otherwise>
                            </c:choose>
                            <img src="${path}/img/icons/add2.gif" id="addNewTP" onclick="openTradingPartner('blForwarder')"/>
                            <img src="${path}/img/icons/comparison.gif" alt="Add Contact"  id="contactNameButtonForFF" onclick="openContactInfo('CF')"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td class="lab-sty">Freight Forwarder Number</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>
                <html:text property="correctedForwarderNo" style="text-transform: uppercase" maxlength="100"
                           styleId="correctedForwarderNo" readonly ="true" size="38" styleClass="BackgrndColorForTextBox" tabindex="-1"/>
        </td>
    </tr>
    <tr class="textlabelsBold">
        <td class="lab-sty">Freight Forwarder Address</td></tr>
    <tr class="textlabelsBold"><td colspan="2">
    <html:textarea property="correctedForwarderAddress"  style="text-transform: uppercase"   styleClass="textlabelsBoldForTextBox"
                   rows="6" cols="47" styleId="forwardingAgentno"/></td></tr>
<html:hidden property="fclBl.forwardingAgentName"/>
<html:hidden property="fclBl.forwardagentNo"/>
<html:hidden property="fclBl.forwardingAgent"/>
<input type="hidden" id="importFlag" value="${fclBlForm.fclBl.importFlag}"/>
</c:when>
<c:otherwise>
    <tr class="textlabelsBold">
        <td class="lab-sty">Freight Forwarder Name</td>
    </tr>
    <tr class="textlabelsBold">
        <td><html:text property="fclBl.forwardingAgentName" styleId="forwardingAgentName"  size="38" maxlength="50" onfocus="disableDojos(this)"
                   styleClass="textlabelsBoldForTextBox" onblur="hideAgentCheckblur();" style="text-transform: uppercase" />
    <input id="forwardingAgentName_check" type="hidden" value="${fclBlForm.fclBl.forwardingAgentName}"/>
    <div id="forwardingAgentName_choices"  style="display:none;" class="autocomplete"></div>
    <script type="text/javascript">
                                    initAutocompleteWithFormClear("forwardingAgentName", "forwardingAgentName_choices", "forwardingAgent1", "forwardingAgentName_check",
                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=10&acctTyp=F", "getForwardingData()", "onFreightBlur('${path}');");
    </script>
    <c:choose>
        <c:when test="${not empty fclBlForm.fclBl.forwardingAgentName}">
            <html:checkbox property="fclBl.editAgentNameCheck" styleId="editAgentNameCheck"
                           onclick="editAgentName(this)" onmouseover="tooltip.show('<strong>Edit Freight Forwarder Name</strong>');"
                           onmouseout="tooltip.hide();"/>
        </c:when>
        <c:otherwise>
            <html:checkbox property="fclBl.editAgentNameCheck" styleId="editAgentNameCheck"
                           onclick="editAgentName(this)" onmouseover="tooltip.show('<strong>Edit Freight Forwarder Name</strong>');"
                           onmouseout="tooltip.hide();" style="visibility: hidden"/>
        </c:otherwise>
    </c:choose>
    <img src="${path}/img/icons/display.gif" id="toggle"  onclick="getPartner('forwardingAgentName', 'Forwarder');"/>
    <c:choose>
        <c:when test="${isFreightNotes}">
            <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Shipper Notes"
                 id="freightIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#forwardingAgent1').val(), jQuery('#forwardingAgentName').val());"/>
        </c:when>
        <c:otherwise>
            <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Shipper Notes"
                 id="freightIcon" onclick="openBlueScreenNotesInfo('${path}', jQuery('#forwardingAgent1').val(), jQuery('#forwardingAgentName').val());"/>
        </c:otherwise>
    </c:choose>
    <img src="${path}/img/icons/add2.gif" id="addNewTP" onclick="openTradingPartner('blForwarder');"/>
    <img src="${path}/img/icons/comparison.gif" alt="Add Contact"  id="contactNameButtonForFF" onclick="openContactInfo('F');"/>
</td></tr>
<tr class="textlabelsBold">
    <td class="lab-sty">Freight Forwarder Number</td>
</tr>
<tr class="textlabelsBold">
    <td><html:text property="fclBl.forwardagentNo" style="text-transform: uppercase" maxlength="100"
               styleId="forwardingAgent1" readonly ="true" size="38" styleClass="BackgrndColorForTextBox" tabindex="-1"/>
<input type="hidden" id="forwardingAgent1_check" />
</td>
</tr>
<tr class="textlabelsBold">
    <td class="lab-sty">Freight Forwarder Address</td></tr>
<tr class="textlabelsBold"><td colspan="2">
<html:textarea property="fclBl.forwardingAgent"  style="text-transform: uppercase"   styleClass="textlabelsBoldForTextBox"
               rows="6" cols="47" styleId="forwardingAgentno"/></td></tr>
</c:otherwise>
</c:choose>
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
