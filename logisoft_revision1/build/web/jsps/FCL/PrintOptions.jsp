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
                <html:radio property="fclBl.agentsForCarrier" value="Yes"
                            styleId="agentsForCarrierYes" name="fclBlForm"/></td>
        <td><html:radio property="fclBl.agentsForCarrier" value="No"
                    styleId="agentsForCarrierNo" name="fclBlForm"/>
    <span style="padding-left: 45px;">
        <html:radio property="fclBl.agentsForCarrier" value="A"
                    styleId="agentsForCarrierA" name="fclBlForm"/>
    </span>
</td>
<td>&nbsp;</td>
</tr>
<tr class="textlabelsBold">
    <td>Shipper Load, Stow, Count and Seal</td>
    <td><html:radio property="fclBl.shipperLoadsAndCounts" value="Yes"
                styleId="shipperLoadYes" name="fclBlForm"/></td>
<td><html:radio property="fclBl.shipperLoadsAndCounts" value="No"
                styleId="shipperLoadNo" name="fclBlForm"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Print Unit# and Seal in Marks and Nos</td>
    <td><html:radio property="fclBl.printContainersOnBl" value="Yes"
                styleId="printUnitYes" name="fclBlForm"/></td>
<td><html:radio property="fclBl.printContainersOnBl" value="No"
                styleId="printUnitNo" name="fclBlForm"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Print Point and Country of Origin</td>
    <td><html:radio property="fclBl.countryOfOrigin" value="Yes" styleId="printPointYes" name="fclBlForm"/></td>
<td><html:radio property="fclBl.countryOfOrigin" value="No" styleId="printPointNo" name="fclBlForm"/></td>
</tr>
<tr  class="textlabelsBold">
    <td>No of Packages</td>
    <td><html:radio property="fclBl.noOfPackages" value="Yes" onclick="hideAlternatePack()"
                styleId="noOfPackYes" name="fclBlForm"/></td>
<td colspan="2">
    <span  style="float:left">
        <html:radio property="fclBl.noOfPackages" value="No" onclick="hideAlternatePack()"
                    styleId="noOfPackNo" name="fclBlForm"/>
    </span>
    <span style="float:left;padding-left: 45px">
        <html:radio property="fclBl.noOfPackages" value="Alt" onclick="hideAlternatePack()"
                    styleId="noOfPackAlternate" />
    </span>
    <span style="float: left;padding-left: 10px">
        Alternate
    </span>
    <span style="display: none;padding-left: 165px" id="noOfPackAlt" >
        <html:text styleClass="textlabelsBoldForTextBox" styleId="noOfPackAlter" property="fclBl.alternateNoOfPackages" maxlength="15" style="text-transform: uppercase"/>
    </span>
</td>
</tr>
<tr class="textlabelsBold">
    <td>Print Pieces and Packages in Body of BL (STC:)</td>
    <td><html:radio property="fclBl.totalContainers" value="Yes"
                styleId="totalContainersYes" name="fclBlForm"/></td>
<td><html:radio property="fclBl.totalContainers" value="No"
                styleId="totalContainersNo" name="fclBlForm"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Proof</td>
    <td><html:radio property="fclBl.proof" value="Yes"
                styleId="proofYes" name="fclBlForm"/></td>
<td><html:radio property="fclBl.proof" value="No"
                styleId="proofNo" name="fclBlForm"/></td>
<td>
    Internal Remarks
</td>
</tr>
<tr class="textlabelsBold">
    <td>Pre Alert</td>
    <td><html:radio property="fclBl.preAlert"  value="Yes"
                styleId="preAlertYes" name="fclBlForm"/></td>
<td><html:radio property="fclBl.preAlert" value="No"
                styleId="preAlertNo" name="fclBlForm"/></td>
<td rowspan="2">
<html:textarea styleClass="textlabelsBoldForTextBox" styleId="internalRemark"
               property="fclBl.internalRemark" onblur="return limitTextchar(this, 500);" rows="4" cols="80"/>
</td>
</tr>
<c:if test="${fclBlForm.fclBl.readyToPost == 'M'}">
    <tr class="textlabelsBold">
        <td>Non-Negotiable</td>
        <td><html:radio property="fclBl.nonNegotiable" value="Yes"
                    styleId="nonNegotiableYes" name="fclBlForm"/></td>
    <td><html:radio property="fclBl.nonNegotiable" value="No"
                    styleId="nonNegotiableNo" name="fclBlForm"/></td>
    </tr>
</c:if>
<tr class="textlabelsBold">
    <td>Print Rev No</td>
    <td><html:radio property="fclBl.printRev"  value="Yes"
                styleId="printRev" name="fclBlForm"/></td>
<td><html:radio property="fclBl.printRev" value="No"
                styleId="printRev" name="fclBlForm"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Print Alternate Port</td>
    <td><html:radio property="fclBl.printAlternatePort"  value="Yes"
                styleId="printAlternatePortYes" onclick="hideAlternatePort()" name="fclBlForm"/></td>
<td colspan="2">
    <span style="float:left">
        <html:radio property="fclBl.printAlternatePort" value="No"
                    styleId="printAlternatePortNo" onclick="hideAlternatePort()" name="fclBlForm"/> &nbsp;&nbsp;&nbsp;
    </span>
    <span style="display: none;float:left" id="alterNatePortId">
        <html:text styleClass="textlabelsBoldForTextBox"  styleId="alternatePort"  property="fclBl.alternatePort"  maxlength="50" style="text-transform: uppercase"/>
    </span>
</td>
</tr>
<tr class="textlabelsBold">
    <td>HBL POL Override</td>
    <td><html:radio property="fclBl.hblPOLOverride"  value="Yes"
                styleId="hblPOLOverrideYes" onclick="hideHblPOL()" name="fclBlForm"/></td>
<td colspan="2">
    <span style="float:left">
        <html:radio property="fclBl.hblPOLOverride" value="No"
                    styleId="hblPOLOverrideNo" onclick="hideHblPOL()" name="fclBlForm"/> &nbsp;&nbsp;&nbsp;
    </span>
    <span style="display: none;float:left" id="hblPOLId">
        <html:text styleClass="textlabelsBoldForTextBox"  styleId="hblPOL"  property="fclBl.hblPOL"  maxlength="50" style="text-transform: uppercase"/>
    </span>
</td>
</tr>
<tr class="textlabelsBold">
    <td>HBL POD Override</td>
    <td><html:radio property="fclBl.hblPODOverride"  value="Yes"
                styleId="hblPODOverrideYes" onclick="hideHblPOD()" name="fclBlForm"/></td>
<td colspan="2">
    <span style="float:left">
        <html:radio property="fclBl.hblPODOverride" value="No"
                    styleId="hblPODOverrideNo" onclick="hideHblPOD()" name="fclBlForm"/> &nbsp;&nbsp;&nbsp;
    </span>
    <span style="display: none;float:left" id="hblPODId">
        <html:text styleClass="textlabelsBoldForTextBox"  styleId="hblPOD"  property="fclBl.hblPOD"  maxlength="50" style="text-transform: uppercase"/>
    </span>
</td>
</tr>
<tr class="textlabelsBold">
    <td>HBL FD Override</td>
    <td><html:radio property="fclBl.hblFDOverride"  value="Yes"
                styleId="hblFDOverrideYes" onclick="hideHblFD()" name="fclBlForm"/></td>
<td colspan="2">
    <span style="float:left">
        <html:radio property="fclBl.hblFDOverride" value="No"
                    styleId="hblFDOverrideNo" onclick="hideHblFD()" name="fclBlForm"/> &nbsp;&nbsp;&nbsp;
    </span>
    <span style="display: none;float:left" id="hblFDId">
        <html:text styleClass="textlabelsBoldForTextBox"  styleId="hblFD"  property="fclBl.hblFD"  maxlength="50" style="text-transform: uppercase"/>
    </span>
</td>
</tr>
<tr class="textlabelsBold">
    <td>HBL Place of Receipt Override</td>
    <td><html:radio property="fclBl.hblPlaceReceiptOverride" value="Yes" styleId="hblPlaceReceiptOverrideYes" name="fclBlForm" onclick="hidehblPlaceReceiptOverride()"/></td>
<td colspan="2">
    <span style="float:left">
        <html:radio property="fclBl.hblPlaceReceiptOverride" value="No" styleId="hblPlaceReceiptOverrideNo" name="fclBlForm" onclick="hidehblPlaceReceiptOverride()"/> &nbsp;&nbsp;&nbsp;
    </span>
    <span style="display: none;float:left" id="hblPlaceReceiptId">
        <html:text styleClass="textlabelsBoldForTextBox"  styleId="hblPlaceReceipt"  property="fclBl.hblPlaceReceipt"  maxlength="50" style="text-transform: uppercase"/>
    </span>
</td>
</tr>
<tr class="textlabelsBold">
    <td>Use Door Origin as PLOR on Master</td>
    <td><html:radio property="fclBl.doorOriginAsPlor"  value="Yes"
                styleId="doorOriginAsPlor" name="fclBlForm"/></td>
<td><html:radio property="fclBl.doorOriginAsPlor" value="No"
                styleId="doorOriginAsPlor" name="fclBlForm"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Use Door Origin as PLOR on House</td>
    <td><html:radio property="fclBl.doorOriginAsPlorHouse"  value="Yes" styleId="doorOriginAsPlorHouse" name="fclBlForm"/></td>
<td><html:radio property="fclBl.doorOriginAsPlorHouse" value="No" styleId="doorOriginAsPlorHouse" name="fclBlForm"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Use Door Destination For Final Delivery To House</td>
    <td><html:radio property="fclBl.doorDestinationAsFinalDeliveryHouse"  value="Yes"
                styleId="doorDestinationAsFinalDeliveryToHouse" name="fclBlForm"/></td>
<td><html:radio property="fclBl.doorDestinationAsFinalDeliveryHouse" value="No"
                styleId="doorDestinationAsFinalDeliveryToHouse" name="fclBlForm"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Use Door Destination For Final Delivery To Master</td>
    <td><html:radio property="fclBl.doorDestinationAsFinalDeliveryMaster"  value="Yes"
                styleId="doorDestinationAsFinalDeliveryToMaster" name="fclBlForm"/></td>
<td><html:radio property="fclBl.doorDestinationAsFinalDeliveryMaster" value="No"
                styleId="doorDestinationAsFinalDeliveryToMaster" name="fclBlForm"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Collect Third Party</td>
    <td><html:radio property="fclBl.collectThirdParty"  value="Yes"
                styleId="collectThirdParty" name="fclBlForm"/></td>
<td><html:radio property="fclBl.collectThirdParty" value="No"
                styleId="collectThirdParty" name="fclBlForm"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Truncate Insignificant Zeros on Cube or Weight</td>
    <td><html:radio property="fclBl.trimTrailingZerosForQty"  value="Yes"
                styleId="trimTrailingZerosForQty" name="fclBlForm"/></td>
<td><html:radio property="fclBl.trimTrailingZerosForQty" value="No"
                styleId="trimTrailingZerosForQty" name="fclBlForm"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Print and omit term and port from B/L#</td>
    <td><html:radio property="fclBl.omitTermAndPort" value="Yes" styleId="omitTermAndPort" name="fclBlForm"/></td>
<td><html:radio property="fclBl.omitTermAndPort" value="No" styleId="omitTermAndPort" name="fclBlForm"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Print Service Contract number on Master</td>
    <td><html:radio property="fclBl.serviceContractNo" value="Yes" styleId="serviceContractNo" name="fclBlForm"/></td>
<td><html:radio property="fclBl.serviceContractNo" value="No" styleId="serviceContractNo" name="fclBlForm"/></td>
</tr>
<c:if test="${fclBlForm.fclBl.readyToPost == 'M'}">
    <tr class="textlabelsBold">
        <td>Certified True Copy Watermark</td>
        <td><html:radio property="fclBl.certifiedTrueCopy" value="Yes" styleId="certifiedTrueCopyYes" name="fclBlForm" onclick="waterMarkChoiceForcertifiedTrue()"/></td>
    <td><html:radio property="fclBl.certifiedTrueCopy" value="No" styleId="certifiedTrueCopyNo" name="fclBlForm"/></td>
    </tr>
</c:if>
<tr class="textlabelsBold">
    <td>Print Rated Manifest</td>
    <td><html:radio property="fclBl.ratedManifest" value="Yes" styleId="ratedManifest" name="fclBlForm"/></td>
<td><html:radio property="fclBl.ratedManifest" value="No" styleId="ratedManifest" name="fclBlForm"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Omit 2 Letter Country Code</td>
    <td><html:radio property="fclBl.omit2LetterCountryCode" value="Yes" styleId="omit2LetterCountryCode" name="fclBlForm"/></td>
<td><html:radio property="fclBl.omit2LetterCountryCode" value="No" styleId="omit2LetterCountryCode" name="fclBlForm"/></td>
</tr>
<tr class="textlabelsBold">
    <td>Dock Receipt</td>
    <td><html:radio property="fclBl.dockReceipt" value="Yes" styleId="dockReceiptYes" name="fclBlForm" onclick="waterMarkChoiceFordockReceipt()"/></td>
<td><html:radio property="fclBl.dockReceipt" value="No" styleId="dockReceiptNo" name="fclBlForm"/></td>
</tr>
</table>
</td>
</tr>
<tr class="textlabels">
    <td align="left" colspan="2">
    </td>
</tr>
</table>