<!DOCTYPE HTML PUBLIC "-//W3C//Dcong:td HTML 4.01 cong:transitional//EN"
    "http://www.w3.org/cong:tr/html4/loose.dcong:td">

<html>
    <%@include file="../init.jsp" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <%@include file="../colorBox.jsp" %>
    <%@include file="../../includes/baseResources.jsp" %>
    <%@include file="../../includes/resources.jsp" %>
    <%@include file="../../includes/jspVariables.jsp" %>
    <%@include file="../../../taglib.jsp" %>
    <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.datepick.css"/>
    <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
    <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/jquery.datepick.js"></script>
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Rate Quote</title>
    </head>
    <body>
        <cong:form name="rateQuoteForm" id="rateQuoteForm" action="/rateQuote">
            <cong:table style="border-collapse: collapse; border: 1px solid #dcdcdc;width:100%" >
                <cong:tr styleClass="tableHeadingNew">
                    <input type="hidden" name="methodName" id="methodName"/>
                    <cong:td width="35%">Search Rates</cong:td>
                    <cong:td width="65%">Accessorials</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                        <cong:table style="border-collapse: collapse; border: 0px solid #dcdcdc;width:25%" styleClass="textlabelsBoldforlcl">
                            <cong:tr><cong:td>&nbsp;&nbsp;&nbsp;</cong:td></cong:tr>
                            <cong:tr>
                                <cong:td>From Zip</cong:td>
                                <cong:td align="left">
                                    <cong:autocompletor styleClass="textlabelsBoldForTextBox mandatory"  id="fromZip" name="fromZip" scrollHeight="200px"
                                                        query="CONCAT_CITY" fields="NULL,NULL,NULL" template="concatOrigin" container="NULL" width="500" shouldMatch="true" />
                                </cong:td>
                                <cong:td></cong:td>
                                <cong:td>To Zip</cong:td>
                                <cong:td>
                                    <cong:autocompletor styleClass="textlabelsBoldForTextBox mandatory"  id="toZip" name="toZip" scrollHeight="200px"
                                                        query="CONCAT_CITY" fields="NULL,NULL,NULL" template="concatOrigin" container="NULL" width="500" shouldMatch="true" />
                                </cong:td>
                            </cong:tr>
                            <cong:tr><cong:td>&nbsp;</cong:td></cong:tr>
                            <cong:tr>
                                <cong:td>Ship Date</cong:td>
                                <cong:td>
                                    <html:text property="shipDate" styleId="shipDate" styleClass="textbox mandatory" maxlength="10"/>
                                </cong:td>
                                <cong:td></cong:td>
                                <cong:td>Cubic Feet</cong:td>
                                <cong:td>
                                    <cong:text name="cubicFeet" id="cubicFeet" style="align:right" onkeyup="allowOnlyWholeNumbers(this);"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr><cong:td>&nbsp;</cong:td></cong:tr>
                            <cong:tr>
                                <cong:td align="right" >Unit</cong:td>
                                <cong:td align="left">
                                    <html:select property="unit" styleId="unit" value="" styleClass="smallDropDown" style="align:right" >
                                        <html:option value="I">Imperial(US)</html:option>
                                        <html:option value="M">Metric</html:option>
                                    </html:select>
                                </cong:td>
                                <cong:td align="right">
                                    <cong:checkbox name="TSA" id="TSA">
                                    </cong:checkbox></cong:td>
                                <cong:td align="left" valign="middle">TSA Only</cong:td>
                            </cong:tr>
                            <cong:tr><cong:td>&nbsp;</cong:td></cong:tr>
                        </cong:table>
                        <cong:table>
                            <cong:tr>
                                <cong:td styleClass="tableHeadingNew">Weight</cong:td>
                                <cong:td styleClass="tableHeadingNew">Length</cong:td>
                                <cong:td styleClass="tableHeadingNew">Width</cong:td>
                                <cong:td styleClass="tableHeadingNew">Height</cong:td>
                                <cong:td styleClass="tableHeadingNew">Pallet&nbsp;&nbsp;&nbsp;&nbsp;</cong:td>
                                <cong:td styleClass="tableHeadingNew">Qty</cong:td>
                                <cong:td styleClass="tableHeadingNew">Package&nbsp;&nbsp;</cong:td>
                                <cong:td styleClass="tableHeadingNew">Qty</cong:td>
                                <cong:td styleClass="tableHeadingNew">CBM/CFT</cong:td>
                                <cong:td styleClass="tableHeadingNew">&nbsp;</cong:td>
                                <cong:td styleClass="tableHeadingNew">Class&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</cong:td>
                            </cong:tr>
                            <cong:tr><cong:td>&nbsp;</cong:td></cong:tr>
                            <cong:tr>
                                <cong:td><html:text property="weight1" styleId="weight1" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);"/></cong:td>
                                <cong:td><html:text property="length1" styleId="length1" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('1')"/></cong:td>
                                <cong:td><html:text property="width1" styleId="width1" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('1')"/></cong:td>
                                <cong:td><html:text property="height1" styleId="height1" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('1')"/></cong:td>
                                <cong:td>
                                    <html:select property="palletType1" styleId="palletType1" styleClass="smallDropDown" style="width:100%" value="${loginuser.ctsPalletType}">
                                        <html:optionsCollection  name="palletList" />
                                    </html:select></cong:td><cong:td>
                                    <html:text property="pallet1" styleId="pallet1" size="7" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="calculateCbmOrCft('1')"/></cong:td>
                                <cong:td>
                                    <html:select property="packageType1" styleId="packageType1" styleClass="smallDropDown" style="width:100%" value="${loginuser.ctsPackageType}">
                                        <html:optionsCollection  name="packageList" />
                                    </html:select></cong:td>
                                <cong:td><html:text property="package1" styleId="package1" size="7" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="calculateCbmOrCft('1')"/></cong:td>
                                <cong:td>
                                    <html:text property="cbmOrCft1" styleId="cbmOrCft1" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" readonly="true" /></cong:td>
                                <cong:td><img src="${path}/img/icons/calc.png" title="Calc" onclick="calculateCbmOrCft('1')" style="display: none"/>
                                </cong:td>

                                <cong:td>
                                    <html:select property="class1" styleId="class1" value="70" styleClass="smallDropDown" style="width:100%">
                                        <html:optionsCollection  name="classList" />
                                    </html:select>
                                </cong:td>
                            </cong:tr>
                            <cong:tr><cong:td>&nbsp;</cong:td></cong:tr>
                            <cong:tr>
                                <cong:td><html:text property="weight2" styleId="weight2" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);"/></cong:td>
                                <cong:td><html:text property="length2" styleId="length2" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('2')"/></cong:td>
                                <cong:td><html:text property="width2" styleId="width2" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('2')"/></cong:td>
                                <cong:td><html:text property="height2" styleId="height2" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('2')"/></cong:td>
                                <cong:td>
                                    <html:select property="palletType2" styleId="palletType2" styleClass="smallDropDown" style="width:100%" value="${loginuser.ctsPalletType}">
                                        <html:optionsCollection  name="palletList" />
                                    </html:select></cong:td><cong:td>
                                    <html:text property="pallet2" styleId="pallet2" size="7" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="calculateCbmOrCft('2')"/></cong:td>
                                <cong:td>
                                    <html:select property="packageType2" styleId="packageType2" styleClass="smallDropDown" style="width:100%" value="${loginuser.ctsPackageType}">
                                        <html:optionsCollection  name="packageList" />
                                    </html:select></cong:td>
                                <cong:td><html:text property="package2" styleId="package2" size="7" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="calculateCbmOrCft('2')"/></cong:td>
                                <cong:td><html:text property="cbmOrCft2" styleId="cbmOrCft2" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" readonly="true"/></cong:td><cong:td>
                                    <img src="${path}/img/icons/calc.png" title="Calc" onclick="calculateCbmOrCft('2')" style="display: none"/>
                                </cong:td>
                                <cong:td><html:select property="class2" styleId="class2"  styleClass="smallDropDown" style="width:100%">
                                        <html:optionsCollection  name="classList" />
                                    </html:select>  </cong:td>
                            </cong:tr>
                            <cong:tr><cong:td>&nbsp;</cong:td></cong:tr>
                            <cong:tr>
                                <cong:td><html:text property="weight3" styleId="weight3" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);"/></cong:td>
                                <cong:td><html:text property="length3" styleId="length3" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('3')"/></cong:td>
                                <cong:td><html:text property="width3" styleId="width3" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('3')"/></cong:td>
                                <cong:td><html:text property="height3" styleId="height3" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('3')"/></cong:td>
                                <cong:td>
                                    <html:select property="palletType3" styleId="palletType3" styleClass="smallDropDown" style="width:100%" value="${loginuser.ctsPalletType}">
                                        <html:optionsCollection  name="palletList" />
                                    </html:select></cong:td><cong:td>
                                    <html:text property="pallet3" styleId="pallet3" size="7" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="calculateCbmOrCft('3')"/></cong:td>
                                <cong:td>
                                    <html:select property="packageType3" styleId="packageType3" styleClass="smallDropDown" style="width:100%" value="${loginuser.ctsPackageType}">
                                        <html:optionsCollection  name="packageList" />
                                    </html:select></cong:td>
                                <cong:td><html:text property="package3" styleId="package3" size="7" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="calculateCbmOrCft('3')"/></cong:td>
                                <cong:td><html:text property="cbmOrCft3" styleId="cbmOrCft3" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" readonly="true"/></cong:td><cong:td>
                                    <img src="${path}/img/icons/calc.png" title="Calc" onclick="calculateCbmOrCft('3')" style="display: none"/>
                                </cong:td>
                                <cong:td><html:select property="class3" styleId="class3"  styleClass="smallDropDown" style="width:100%">
                                        <html:optionsCollection  name="classList" />
                                    </html:select>  </cong:td>
                            </cong:tr>
                            <cong:tr><cong:td>&nbsp;</cong:td></cong:tr>
                            <cong:tr>
                                <cong:td><html:text property="weight4" styleId="weight4" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);"/></cong:td>
                                <cong:td><html:text property="length4" styleId="length4" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('4')"/></cong:td>
                                <cong:td><html:text property="width4" styleId="width4" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('4')"/></cong:td>
                                <cong:td><html:text property="height4" styleId="height4" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('4')"/></cong:td>
                                <cong:td>
                                    <html:select property="palletType4" styleId="palletType4" styleClass="smallDropDown" style="width:100%" value="${loginuser.ctsPalletType}">
                                        <html:optionsCollection  name="palletList" />
                                    </html:select></cong:td><cong:td>
                                    <html:text property="pallet4" styleId="pallet4" size="7" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="calculateCbmOrCft('4')"/></cong:td>
                                <cong:td>
                                    <html:select property="packageType4" styleId="packageType4" styleClass="smallDropDown" style="width:100%" value="${loginuser.ctsPackageType}">
                                        <html:optionsCollection  name="packageList" />
                                    </html:select></cong:td>
                                <cong:td><html:text property="package4" styleId="package4" size="7" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="calculateCbmOrCft('4')"/></cong:td>
                                <cong:td><html:text property="cbmOrCft4" styleId="cbmOrCft4" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" readonly="true"/></cong:td><cong:td>
                                    <img src="${path}/img/icons/calc.png" title="Calc" onclick="calculateCbmOrCft('4')" style="display: none"/>
                                </cong:td>
                                <cong:td><html:select property="class4" styleId="class4"  styleClass="smallDropDown" style="width:100%" >
                                        <html:optionsCollection  name="classList" />
                                    </html:select>  </cong:td>
                            </cong:tr>
                            <cong:tr><cong:td>&nbsp;</cong:td></cong:tr>
                            <cong:tr>
                                <cong:td><html:text property="weight5" styleId="weight5" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);"/></cong:td>
                                <cong:td><html:text property="length5" styleId="length5" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('5')"/></cong:td>
                                <cong:td><html:text property="width5" styleId="width5" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('5')"/></cong:td>
                                <cong:td><html:text property="height5" styleId="height5" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('5')"/></cong:td>
                                <cong:td>
                                    <html:select property="palletType5" styleId="palletType5" styleClass="smallDropDown" style="width:100%" value="${loginuser.ctsPalletType}">
                                        <html:optionsCollection  name="palletList" />
                                    </html:select>
                                </cong:td>
                                <cong:td>
                                    <html:text property="pallet5" styleId="pallet5" size="7" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="calculateCbmOrCft('5')"/></cong:td>
                                <cong:td>
                                    <html:select property="packageType5" styleId="packageType5" styleClass="smallDropDown" style="width:100%" value="${loginuser.ctsPackageType}">
                                        <html:optionsCollection  name="packageList" />
                                    </html:select>
                                </cong:td>
                                <cong:td><html:text property="package5" styleId="package5" size="7" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="calculateCbmOrCft('5')"/></cong:td>
                                <cong:td><html:text property="cbmOrCft5" styleId="cbmOrCft5" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" readonly="true"/></cong:td><cong:td>
                                    <img src="${path}/img/icons/calc.png" title="Calc" onclick="calculateCbmOrCft('5')" style="display: none"/>
                                </cong:td>
                                <cong:td><html:select property="class5" styleId="class5"   styleClass="smallDropDown" style="width:100%" >
                                        <html:optionsCollection  name="classList" />
                                    </html:select>  </cong:td>
                            </cong:tr>
                            <cong:tr><cong:td>&nbsp;</cong:td></cong:tr>
                            <cong:tr>
                                <cong:td><html:text property="weight6" styleId="weight6" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);"/></cong:td>
                                <cong:td><html:text property="length6" styleId="length6" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('6')"/></cong:td>
                                <cong:td><html:text property="width6" styleId="width6" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('6')"/></cong:td>
                                <cong:td><html:text property="height6" styleId="height6" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="recalculateUnit('6')"/></cong:td>
                                <cong:td>
                                    <html:select property="palletType6" styleId="palletType6" styleClass="smallDropDown" style="width:100%" value="${loginuser.ctsPalletType}">
                                        <html:optionsCollection  name="palletList" />
                                    </html:select></cong:td><cong:td>
                                    <html:text property="pallet6" styleId="pallet6" size="7" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="calculateCbmOrCft('6')"/>
                                </cong:td>
                                <cong:td>
                                    <html:select property="packageType6" styleId="packageType6" styleClass="smallDropDown" style="width:100%" value="${loginuser.ctsPackageType}">
                                        <html:optionsCollection  name="packageList" />
                                    </html:select></cong:td>
                                <cong:td><html:text property="package6" styleId="package6" size="7" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" onchange="calculateCbmOrCft('6')"/></cong:td>
                                <cong:td><html:text property="cbmOrCft6" styleId="cbmOrCft6" size="10" styleClass="textlabelsBoldForTextBox" onkeyup="allowOnlyWholeNumbers(this);" readonly="true"/></cong:td><cong:td>
                                    <img src="${path}/img/icons/calc.png" title="Calc" onclick="calculateCbmOrCft('6')" style="display: none"/>
                                </cong:td>
                                <cong:td><html:select property="class6" styleId="class6" styleClass="smallDropDown" style="width:100%">
                                        <html:optionsCollection  name="classList" />
                                    </html:select></cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                    <cong:td>
                        <div id="accessorial"  styleClass="textlabelsBoldforlcl">
                            <cong:table id="accessorial" style="border-collapse: collapse; border: 0px solid #dcdcdc;width:100%">
                                <cong:tr><cong:td ><cong:checkbox name="collectionFee" id="collectionFee">Advance Collection Fee</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="hourCharge" id="hourCharge" >After Hours Charge <input type="text" style="width:30px" name="hourChargeNo" id="hourChargeNo"/> Hrs</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="hourDelivery" id="hourDelivery" >After Hours Delivery</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="appont" id="appont" >Appointment</cong:checkbox></cong:td></cong:tr>
                                <cong:tr><cong:td><cong:checkbox name="shipDelivery" id="shipDelivery" >Blind Shipment Delivery</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="pickUp" id="pickUp">Blind Shipment Pick Up</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="charge" id="charge" >Bond charge</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="customs" id="customs">Canadian Customs</cong:checkbox></cong:td></cong:tr>
                                <cong:tr><cong:td><cong:checkbox name="BOLFee" id="BOLFee">Change BOL Fee</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="COD" id="COD">COD <input type="text" style="width:30px" name="codVal" id="codVal"/> </cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="consolidate" id="consolidate">Consolidation</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="constSite" id="constSite">Construction Site</cong:checkbox></cong:td></cong:tr>
                                <cong:tr><cong:td><cong:checkbox name="correctBOL" id="correctBOL">Corrected BOL</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="crossBorderFee" id="crossBorderFee">Cross Border Fee</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="delayCharge" id="delayCharge">Delay Charge</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="descInspection" id="descInspection">Description Inspection</cong:checkbox></cong:td></cong:tr>
                                <cong:tr><cong:td><cong:checkbox name="detentFee" id="detentFee">Detention Fee</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="driverAssist" id="driverAssist">Driver Assist</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="dryRunCharge" id="dryRunCharge">Dry Run Charge <input type="text" style="width:30px" name="" id=""/> Qty</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="excessLength" id="excessLength">Excess Length</cong:checkbox></cong:td></cong:tr>
                                <cong:tr><cong:td><cong:checkbox name="tradeShow" id="tradeShow">Exhibition trade Show</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="extraLabor" id="extraLabor">Extra Labor <input type="text" style="width:30px" name="extraLaborVal" id="extraLaborVal"/> Hrs</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="freezeProtect" id="freezeProtect">Freeze Protection</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="hazmat" id="hazmat">Hazmat</cong:checkbox></cong:td></cong:tr>
                                <cong:tr><cong:td><cong:checkbox name="homeLandSec" id="homeLandSec">Homeland Security</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="inbondFreight" id="inbondFreight">Inbond Freight</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="insideDelivery" id="insideDelivery">Inside Delivery</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="layOverFee" id="layOverFee">Layover Fee</cong:checkbox></cong:td></cong:tr>
                                <cong:tr><cong:td><cong:checkbox name="liftGateDelivery" id="liftGateDelivery">Lift Gate at Delivery</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="liftGatePickup" id="liftGatePickup">Lift Gate at Pick Up</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="limitAccess" id="limitAccess">Limited Access</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="loadUnload" id="loadUnload">Loading or Unloading</cong:checkbox></cong:td></cong:tr>
                                <cong:tr><cong:td><cong:checkbox name="lumperFee" id="lumperFee">Lumper Fee <input type="text" style="width:30px" name="lumperFeeVal" id="lumperFeeVal"/> Hrs</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="markTag" id="markTag">Marking and Tagging <input type="text" style="width:30px" name="markTagVal" id="markTagVal"/> Qty</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="militaryDelivery" id="militaryDelivery">Military Base Delivery</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="notify" id="notify">Notification</cong:checkbox></cong:td></cong:tr>
                                <cong:tr><cong:td><cong:checkbox name="permitFee" id="permitFee">Permit Fee</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="pierCharge" id="pierCharge">Pier Charge</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="portCharge" id="portCharge">Port Charge</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="reconsignment" id="reconsignment">Reconsignment</cong:checkbox></cong:td></cong:tr>
                                <cong:tr><cong:td><cong:checkbox name="redelivery" id="redelivery">Redelivery</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="removeDebris" id="removeDebris">Remove Debris</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="residentDelivery" id="residentDelivery">Residential Delivery</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="satDelivery" id="satDelivery">Saturday Delivery <input type="text" style="width:30px" name="satDeliveryVal" id="satDeliveryVal"/> Hrs</cong:checkbox></cong:td></cong:tr>
                                <cong:tr><cong:td><cong:checkbox name="stopCharge" id="stopCharge">Stop Off Charge</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="sortSegregate" id="sortSegregate">Sorting Segregating <input type="text" style="width:30px" name="sortSegregateVal" id="sortSegregateVal"/> Qty</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="tarpCharge" id="tarpCharge">Tarp Charge</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="teamCharge" id="teamCharge">Team Charge</cong:checkbox></cong:td></cong:tr>
                                <cong:tr><cong:td><cong:checkbox name="truckNotUsed" id="truckNotUsed">truck Not Used</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="truckOrderedNotUsed" id="truckOrderedNotUsed">truck ordered not used</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="unPack" id="unPack">Unpacking</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="holidayDel" id="holidayDel">Weekend or Holiday Del <input type="text" style="width:30px" name="holidayDelVal" id="holidayDelVal"/> Hrs</cong:checkbox></cong:td></cong:tr>
                                <cong:tr><cong:td><cong:checkbox name="weightInspect" id="weightInspect">Weight Inspection</cong:checkbox></cong:td>
                                    <cong:td><cong:checkbox name="weightVerify" id="weightVerify">Weight Verification</cong:checkbox></cong:td></cong:tr>
                            </cong:table>
                        </div>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <table style="border-collapse: collapse; border: 0px solid #dcdcdc;width:100%">
                <cong:tr><cong:td>&nbsp;</cong:td></cong:tr>
                <cong:tr>
                    <cong:td >
                        <cong:button styleClass="button-style1 " label="Rate it!" onclick="result();"></cong:button>
                        <cong:button styleClass="button-style1 " label="Back" onclick="backToRate()"></cong:button>
                    </cong:td>
                </cong:tr>
            </table>
        </cong:form>
    </body>
</html>

<script type="text/javascript">
    function result(){
        if($('#fromZip').val()== ''){
            sampleAlert("Please Enter from zip.");return;
        }
        if($('#toZip').val()== ''){
            sampleAlert("Please Enter to zip.");return;
        }
        if($('#shipDate').val()== ''){
            sampleAlert("Please select shipDate.");return;
        }
        if($('#cubicFeet').val()== ''){
            sampleAlert("Please Enter cubic feet.");return;
        }
        if($('#weight1').val()== ''){
            sampleAlert("Please Enter weight1.");return;
        }else if($('#length1').val()== '' || $('#width1').val()== '' || $('#height1').val()== ''){
            sampleAlert("Please enter length,width,height for line 1.");return;
        }else if($('#cbmOrCft1').val()== ''){
            sampleAlert("Please calculate CBM/CFT for line 1.");return;
        }
        if($('#class2').val() != ''){
            if($('#weight2').val()== ''){
                sampleAlert("Please Enter weight2.");return;
            }else if($('#length2').val()== '' || $('#width2').val()== '' || $('#height2').val()== ''){
                sampleAlert("Please enter length,width,height for line 2.");return;
            }else if($('#cbmOrCft2').val()== ''){
                sampleAlert("Please calculate CBM/CFT for line 2.");return;
            }
        }
        if($('#class3').val() != ''){
            if($('#weight3').val()== ''){
                sampleAlert("Please Enter weight3.");return;
            }else if($('#length3').val()== '' || $('#width3').val()== '' || $('#height3').val()== ''){
                sampleAlert("Please enter length,width,height for line 3.");return;
            }else if($('#cbmOrCft2').val()== ''){
                sampleAlert("Please calculate CBM/CFT for line 3.");return;
            }
        }if($('#class4').val() != ''){
            if($('#weight4').val()== ''){
                sampleAlert("Please Enter weight4.");return;
            }else if($('#length4').val()== '' || $('#width4').val()== '' || $('#height4').val()== ''){
                sampleAlert("Please enter length,width,height for line 4.");return;
            }else if($('#cbmOrCft2').val()== ''){
                sampleAlert("Please calculate CBM/CFT for line 4.");return;
            }
        }if($('#class5').val() != ''){
            if($('#weight5').val()== ''){
                sampleAlert("Please Enter weight5.");return;
            }else if($('#length5').val()== '' || $('#width5').val()== '' || $('#height5').val()== ''){
                sampleAlert("Please enter length,width,height for line 5.");return;
            }else if($('#cbmOrCft2').val()== ''){
                sampleAlert("Please calculate CBM/CFT for line 5.");return;
            }
        }if($('#class6').val() != ''){
            if($('#weight6').val()== ''){
                sampleAlert("Please Enter weight6.");return;
            }else if($('#length6').val()== '' || $('#width6').val()== '' || $('#height6').val()== ''){
                sampleAlert("Please enter length,width,height for line 6.");return;
            }else if($('#cbmOrCft2').val()== ''){
                sampleAlert("Please calculate CBM/CFT for line 6.");return;
            }
        }


        $('#methodName').val("rateQuote");
        $('#rateQuoteForm').submit();
    }
    function calculateCbmOrCft(line){
        var length = $('#length'+line).val();
        var width = $('#width'+line).val();
        var height = $('#height'+line).val();
        if(length == '' || width == '' || height == ''){
            sampleAlert("Please enter length,width,height");return;
        }
        var pallet = $('#pallet'+line).val();
        if(pallet == ''){
            pallet = $('#package'+line).val();
        }
        if(pallet == ''){
            sampleAlert("Please enter pallet or package");return;
            $('#cbmOrCft'+line).val('');
        }
        var unit = $('#unit').val();
        var cbmOrCft = 0;
        if(unit == 'I'){
            cbmOrCft = (length * width * height * pallet)/1728;
        }else{
            cbmOrCft = (length * width * height * pallet)/1000000;
        }
        $('#cbmOrCft'+line).val(cbmOrCft.toFixed(2));
        calculateCubicFeet();

    }
    function calculateCubicFeet(){
        var cube1 = 0;
        if($('#cbmOrCft1').val() != ''){
            cube1 = parseFloat(cube1) + parseFloat($('#cbmOrCft1').val());
        }
        if($('#cbmOrCft2').val() != ''){
            cube1 = parseFloat(cube1) + parseFloat($('#cbmOrCft2').val());
        }
        if($('#cbmOrCft3').val() != ''){
            cube1 = parseFloat(cube1) + parseFloat($('#cbmOrCft3').val());
        }
        if($('#cbmOrCft4').val() != ''){
            cube1 = parseFloat(cube1) + parseFloat($('#cbmOrCft4').val());
        }
        if($('#cbmOrCft5').val() != ''){
            cube1 = parseFloat(cube1) + parseFloat($('#cbmOrCft5').val());
        }
        if($('#cbmOrCft6').val() != ''){
            cube1 = parseFloat(cube1) + parseFloat($('#cbmOrCft6').val());
        }
        $('#cubicFeet').val(cube1.toFixed(2))
    }
    function recalculateUnit(line){
        var cbmOrCft = $('#cbmOrCft'+line).val();
        if(undefined != cbmOrCft && cbmOrCft != ''){
            calculateCbmOrCft(line);
        }
        var classes = $('#class'+line).val();
        if(classes == ''){
            $('#class'+line).val('70')
        }
    }
    function backToRate(){
        $('#methodName').val("searchQuote");
        $('#rateQuoteForm').submit();
    }
    function initDate() {
        $("#shipDate").datepick({
            showOnFocus: false,
            showTrigger: "<img src='${path}/images/icons/calendar-blue.gif' class='trigger' title='Ship Date'/>"
        });
    }
    $(document).ready(function() {
        $("#rateQuoteForm").submit(function() {
            window.parent.showPreloading();
        });
        window.parent.closePreloading();
        initDate();
        $("[title != '']").not("link").tooltip();
    });
    function allowOnlyWholeNumbers(obj){
        if(!/^\d*(\.\d{0,2})?$/.test(obj.value)){
            obj.value= "";
            return false;
        }
    }
</script>
