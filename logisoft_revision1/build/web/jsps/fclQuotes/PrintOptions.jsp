<table width="100%">
    <tr>
        <td valign="top">
            <table class="tableBorderNew" border="0"  cellpadding="0" cellspacing="0" width="100%">
                <tr class="tableHeadingNew"><td colspan="4">Print Options</td></tr>
                <tr class="textlabelsBold">
                    <td width="30%"></td>
                    <td width="10%">Yes</td>
                    <td>No
                        <span style="padding-left: 50px;">
                            A(As Carrier)
                        </span>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td width="30%">As Agents for Carrier</td>
                    <td width="10%">
                <html:radio property="agentsForCarrier" value="Yes"
                            name="fclBillLaddingform"  styleId="agentsForCarrierYes"/></td>
        <td><html:radio property="agentsForCarrier" value="No"
                    name="fclBillLaddingform"  styleId="agentsForCarrierNo"/>
    <span style="padding-left: 45px;">
        <html:radio property="agentsForCarrier" value="A"
                    name="fclBillLaddingform"  styleId="agentsForCarrierA"/>
    </span>
</td>
<td>&nbsp;</td>
</tr>
<tr class="textlabelsBold">
    <td>Shipper Load, Stow, Count and Seal</td>
    <td><html:radio property="shipperLoadsAndCounts" value="Yes"
                styleId="shipperLoadYes"/></td>
<td><html:radio property="shipperLoadsAndCounts" value="No"
                styleId="shipperLoadNo" /></td>
</tr>
<tr class="textlabelsBold">
    <td>Print Unit# and Seal in Marks and Nos</td>
    <td><html:radio property="printContainersOnBL" value="Yes"
                styleId="printUnitYes"/></td>
<td><html:radio property="printContainersOnBL" value="No"
                styleId="printUnitNo"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Print Point and Country of Origin</td>
    <td><html:radio property="conturyOfOrigin" value="Yes" styleId="printPointYes" /></td>
<td><html:radio property="conturyOfOrigin" value="No" styleId="printPointNo" /></td>
</tr>
<tr class="textlabelsBold">
    <td>No of Packages</td>
    <td><html:radio property="noOfPackages" value="Yes" onclick="hideAlternatePack()"
                styleId="noOfPackYes" /></td>
<td colspan="2">
    <span  style="float:left">
        <html:radio property="noOfPackages" value="No" onclick="hideAlternatePack()"
                    styleId="noOfPackNo"/>
    </span>
    <span style="float:left;padding-left: 45px">
        <html:radio property="noOfPackages" value="Alt" onclick="hideAlternatePack()"
                    styleId="noOfPackAlternate" />
    </span>
    <span style="float: left;padding-left: 10px">
        Alternate
    </span>
    <span style="display: none;padding-left: 165px" id="noOfPackAlt" >
        <html:text styleClass="textlabelsBoldForTextBox" styleId="noOfPackAlter" property="alternateNoOfPackages" value="${fclBl.alternateNoOfPackages}" maxlength="15" style="text-transform: uppercase"/>
    </span>
</td>
</tr>
<tr class="textlabelsBold">
    <td>Print Pieces and Packages in Body of BL (STC:)</td>
    <td><html:radio property="totalContainers" value="Yes"
                styleId="totalContainersYes" /></td>
<td><html:radio property="totalContainers" value="No"
                styleId="totalContainersNo"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Proof</td>
    <td><html:radio property="proof" value="Yes"
                styleId="proofYes" /></td>
<td><html:radio property="proof" value="No"
                styleId="proofNo"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Pre Alert</td>
    <td><html:radio property="preAlert"  value="Yes"
                styleId="preAlertYes" /></td>
<td><html:radio property="preAlert" value="No"
                styleId="preAlertNo"/></td>
</tr>
<%if (readyToPost.equalsIgnoreCase("M")) {%>
<tr class="textlabelsBold">
    <td>Non-Negotiable</td>
    <td><html:radio property="nonNegotiable" value="Yes"
                styleId="nonNegotiableYes" /></td>
<td><html:radio property="nonNegotiable" value="No"
                styleId="nonNegotiableNo"/></td>
</tr>
<%}%>
<tr class="textlabelsBold">
    <td>Print Rev No</td>
    <td><html:radio property="printRev"  value="Yes"
                styleId="printRev" /></td>
<td><html:radio property="printRev" value="No"
                styleId="printRev"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Print Alternate Port</td>
    <td><html:radio property="printAlternatePort"  value="Yes"
                styleId="printAlternatePort" onclick="hideAlternatePort()"/></td>
<td colspan="2">
    <span style="float:left">
        <html:radio property="printAlternatePort" value="No"
                    styleId="printAlternatePort" onclick="hideAlternatePort()"/> &nbsp;&nbsp;&nbsp;
    </span>
    <span style="display: none;float:left" id="alterNatePortId">
        <input type="text" class="textlabelsBoldForTextBox"  id="alternatePort"  name="alternatePort" value="${fclBl.alternatePort}" maxlength="50" style="text-transform: uppercase"/>
    </span>
</td>
</tr>
<tr class="textlabelsBold">
    <td>AN POL Override</td>
    <td><html:radio property="hblPOLOverride"  value="Yes"
                styleId="hblPOLOverrideYes" onclick="hideHblPOL()" /></td>
<td colspan="2">
    <span style="float:left">
        <html:radio property="hblPOLOverride" value="No"
                    styleId="hblPOLOverrideNo" onclick="hideHblPOL()" /> &nbsp;&nbsp;&nbsp;
    </span>
    <span style="display: none;float:left" id="hblPOLId">
        <html:text styleClass="textlabelsBoldForTextBox"  styleId="hblPOL"  property="hblPOL" value="${fclBl.hblPOL}" maxlength="50" style="text-transform: uppercase"/>
    </span>
</td>
</tr>
<tr class="textlabelsBold">
    <td>AN POD Override</td>
    <td><html:radio property="hblPODOverride"  value="Yes"
                styleId="hblPODOverrideYes" onclick="hideHblPOD()" /></td>
<td colspan="2">
    <span style="float:left">
        <html:radio property="hblPODOverride" value="No"
                    styleId="hblPODOverrideNo" onclick="hideHblPOD()" /> &nbsp;&nbsp;&nbsp;
    </span>
    <span style="display: none;float:left" id="hblPODId">
        <html:text styleClass="textlabelsBoldForTextBox"  styleId="hblPOD"  property="hblPOD" value="${fclBl.hblPOD}"  maxlength="50" style="text-transform: uppercase"/>
    </span>
</td>
</tr>
<tr class="textlabelsBold">
    <td>AN FD Override</td>
    <td><html:radio property="hblFDOverride"  value="Yes"
                styleId="hblFDOverrideYes" onclick="hideHblFD()"/></td>
<td colspan="2">
    <span style="float:left">
        <html:radio property="hblFDOverride" value="No"
                    styleId="hblFDOverrideNo" onclick="hideHblFD()" /> &nbsp;&nbsp;&nbsp;
    </span>
    <span style="display: none;float:left" id="hblFDId">
        <html:text styleClass="textlabelsBoldForTextBox"  styleId="hblFD"  property="hblFD" value="${fclBl.hblFD}"  maxlength="50" style="text-transform: uppercase"/>
    </span>
</td>
</tr>
<tr class="textlabelsBold">
    <td>Use Door Origin as PLOR on Master</td>
    <td><html:radio property="doorOriginAsPlor"  value="Yes"
                styleId="doorOriginAsPlor" /></td>
<td><html:radio property="doorOriginAsPlor" value="No"
                styleId="doorOriginAsPlor"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Use Door Destination For Final Delivery To AN</td>
    <td><html:radio property="doorDestinationAsFinalDeliveryToHouse"  value="Yes"
                styleId="doorDestinationAsFinalDeliveryToHouse" /></td>
<td><html:radio property="doorDestinationAsFinalDeliveryToHouse" value="No"
                styleId="doorDestinationAsFinalDeliveryToHouse"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Use Door Destination For Final Delivery To Master</td>
    <td><html:radio property="doorDestinationAsFinalDeliveryToMaster" value="Yes" styleId="doorDestinationAsFinalDeliveryToMaster"
                disabled="true" /></td>
<td><html:radio property="doorDestinationAsFinalDeliveryToMaster" value="No" styleId="doorDestinationAsFinalDeliveryToMaster"
                disabled="true" /></td>
</tr>
<tr class="textlabelsBold">
    <td>Collect Third Party</td>
    <td><html:radio property="collectThirdParty"  value="Yes"
                styleId="collectThirdParty" /></td>
<td><html:radio property="collectThirdParty" value="No"
                styleId="collectThirdParty"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Truncate Insignificant Zeros on Cube or Weight</td>
    <td><html:radio property="trimTrailingZerosForQty"  value="Yes"
                styleId="trimTrailingZerosForQty" /></td>
<td><html:radio property="trimTrailingZerosForQty" value="No"
                styleId="trimTrailingZerosForQty"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Print and omit term and port from B/L#</td>
    <td><html:radio property="omitTermAndPort" value="Yes" styleId="omitTermAndPort"/></td>
<td><html:radio property="omitTermAndPort" value="No" styleId="omitTermAndPort"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Print Service Contract number on Master</td>
    <td><html:radio property="serviceContractNo" value="Yes" styleId="serviceContractNo"/></td>
<td><html:radio property="serviceContractNo" value="No" styleId="serviceContractNo"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Replace Arrival Notice with Bill of Lading</td>
    <td><html:radio property="replaceArrival" value="Yes" styleId="replaceArrival"/></td>
<td><html:radio property="replaceArrival" value="No" styleId="replaceArrival"/></td>
</tr>
<c:if test="${fclBl.readyToPost=='M'}">
    <tr class="textlabelsBold">
        <td>Certified True Copy Watermark</td>
        <td><html:radio property="certifiedTrueCopy" value="Yes" styleId="certifiedTrueCopy"/></td>
    <td><html:radio property="certifiedTrueCopy" value="No" styleId="certifiedTrueCopy"/></td>
    </tr>
</c:if>
</table>
</td>
</tr>
<tr class="textlabels">
    <td align="left" colspan="2">
    </td>
</tr>
</table>