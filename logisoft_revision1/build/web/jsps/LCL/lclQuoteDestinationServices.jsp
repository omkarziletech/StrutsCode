<%-- 
    Document   : lclQuoteDestinationServices
    Created on : Oct 28, 2014, 3:39:53 PM
    Author     : aravindhan.v
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@include file="init.jsp" %>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>

<body style="background:#ffffff">
    <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
        Destination Services for File No:<span class="fileNo">${fileNumber}</span>
    </cong:div><br><br>
    <cong:form name="lclQuoteCostAndChargeForm" id="lclQuoteCostAndChargeForm" action="/lclQuoteCostAndCharge.do">
        <cong:hidden name="id" id="lclQuoteAcId" value="${lclQuoteCostAndChargeForm.id}"/>
        <cong:hidden name="fileNumber" value="${fileNumber}"/>
        <cong:hidden name="manualEntry" id="manualEntry" value="${lclQuoteCostAndChargeForm.manualEntry}"/>
        <cong:hidden name="fileNumberStatus" value="${lclQuoteCostAndChargeForm.fileNumberStatus}"/>
        <cong:hidden name="fileNumberId" id="fileNumberId" value="${fileNumberId}"/>
        <cong:hidden name="moduleName"  id="moduleName" value='${lclQuoteCostAndChargeForm.moduleName}' />
        <input type="hidden" name="markup" id="markup" value="${onCarrigeMarkUp}"/>
        <input type="hidden" name="dtchAdj" id="dtchAdj" value="${DTHC_currency_adj}"/>
        <cong:hidden name="profitMax"  id="profitMax" value='${sellMax}' />
        <cong:hidden name="profitMin"  id="profitMin" value='${sellMin}' />
        <input type="hidden" name="profit"  id="profit" value='${profit}' />
        <input type="hidden" name="onCarriageMax"  id="onCarriageMax" value='${oc_max_profit}' />
        <input type="hidden" name="onCarriageMin"  id="onCarriageMin" value='${oc_min_profit}' />

        <table border="0" width="100%">
            <tr>
                <td class="textlabelsBoldforlcl">Destination Service</td>
                <td>
                    <html:select property="chargesCode" styleId="chargesCode" value="${lclQuoteCostAndChargeForm.chargesCode}" style="width:120px;"
                                 styleClass="textlabelsBoldForTextBox smalldropdownStyleForText mandatory"  
                                 onchange="if(checkChargeCode()){showValueDs();setValuesDs();setSelectForCharges();}">
                        <html:option value="">Select One</html:option>
                        <html:optionsCollection name="chargeCodeList"/>
                    </html:select>
                        <%--<cong:hidden name="chargesCode" id="chargesCode"/>--%>
                </td>
                <td class="textlabelsBoldforlcl">UOM</td>
                <td>
                    <cong:radio name="uom" value="M" id="metric" onclick="showValue();" container="NULL"/>M
                    <cong:radio name="uom" value="I" id="imperial" onclick="showValue();" container="NULL"/>I
                </td>
                <td class="bold" colspan="3">
                    <input type="radio" name="rateOption" value="FLAT" id="FL"  onclick="setSelect('FL');"/>Flat Rate Only
                    <input type="radio" name="rateOption" value="WMRATE" id="WM" checked onclick="setSelect('WM');"/>W/M Rate Only
                    <input type="radio" name="rateOption" value="BOTH" id="BO" onclick="setSelect('BO');"/>Both
                </td>
            </tr>
            <tr><td colspan="5"><pre>&nbsp;</pre></td></tr>
        </table>
        <table border="0" width="100%">
            <tr>
                <td class="textlabelsBoldforlcl">Flat Cost Amount&nbsp;&nbsp;&nbsp;&nbsp;</td>
                <td>
                    <cong:text styleClass="textlabelsBoldForTextBox twoDigitDecFormat flat" style="width:76px" 
                               name="costAmount" id="costAmount"  onkeyup="checkForNumberAndDecimal(this);"  
                               onchange="checkValue(this,$('#costAmount').val(),$('#flatRateAmount').val(),'cost','Sell');"/>
                </td>
                <td id="costOr" class="textlabelsBoldforlcl rate_or"></td>
                <td align="right" id="costWm" class="textlabelsBoldforlcl">W/M Cost</td>
                <td id="costMeasure">
                    <cong:text name="measureForCost"  styleClass="textlabelsBoldForTextBox twoDigitDecFormat wmrate" container="null"
                               style="width:76px" id="measureForCost" onkeyup="checkForNumberAndDecimal(this);"
                               onchange="checkValue(this,$('#measureForCost').val(),$('#measure').val(),'cost','Sell');"/>

                    <cong:label id="msr1" text=" /CBM" styleClass="textlabelsBoldforlcl"></cong:label>
                    <cong:text name="weightForCost" styleClass="textlabelsBoldForTextBox twoDigitDecFormat wmrate" container="null"
                               style="width:76px" id="weightForCost" onkeyup="checkForNumberAndDecimal(this);"
                               onchange="checkValue(this,$('#weightForCost').val(),$('#weight').val(),'cost','Sell');"/>

                    <cong:label id="wei1" text="/1000 KGS" styleClass="textlabelsBoldforlcl"></cong:label>

                    </td>
                    <td align="right" id="costMin" class="textlabelsBoldforlcl">Minimum</td>
                    <td id="costMinimum"><cong:text name="minimumForCost" styleClass="textlabelsBoldForTextBox twoDigitDecFormat wmrate" 
                           style="width:76px" id="minimumForCost" onkeyup="checkForNumberAndDecimal(this);" 
                           onchange="checkValue(this,$('#minimumForCost').val(),$('#minimum').val(),'cost','Sell');"/>
                </td>
            </tr>
            <tr><td height="20%">&nbsp;</td></tr>
            <tr>
                <td class="textlabelsBoldforlcl">
                    <img src="./img/icons/calc.png" id="costCalc" title="Calculate Sell Rate : <br> Profit: <font color=red>${profit}%</font>
                         Min Profit: <font color=red>${sellMin} </font> Max Profit: <font color=red>${sellMax} </font>"
                         container="NULL" onclick="calculateCharge('${profit}', '${sellMin}', '${sellMax}');"/>
                    <input type="checkbox" name="overrideCalculator" id="overrideCalculator" 
                           title="Override Calculation/Enter Sell Rate Manually" onclick="overrideCalc(this);"/>
                    Flat Sell Amount&nbsp;&nbsp;&nbsp;&nbsp;
                </td>
                <td>
                    <cong:text styleClass="textlabelsBoldForTextBox twoDigitDecFormat flat flatsell" style="width:76px" 
                               name="flatRateAmount" id="flatRateAmount"  onkeyup="checkForNumberAndDecimal(this);"  
                               onchange="checkValue(this,$('#costAmount').val(),$('#flatRateAmount').val(),'cost','Sell');"/>
                </td>
                <td id="rateOr" class="textlabelsBoldforlcl rate_or"></td>
                <td align="right" id="rateWm" class="textlabelsBoldforlcl">W/M Sell</td>
                <td id="rateMeasure" width="10%">
                    <cong:text name="measure" styleClass="textlabelsBoldForTextBox twoDigitDecFormat wmrate wmsell" container="null"
                               style="width:76px" id="measure" onkeyup="checkForNumberAndDecimal(this);"  
                               onchange="checkValue(this,$('#measureForCost').val(),$('#measure').val(),'cost','Sell');"/>

                    <cong:label id="msr" text=" /CBM" styleClass="textlabelsBoldforlcl"></cong:label>
                    <cong:text name="weight" styleClass="textlabelsBoldForTextBox twoDigitDecFormat wmrate wmsell" container="null" 
                               style="width:76px" id="weight" onkeyup="checkForNumberAndDecimal(this);"  
                               onchange="checkValue(this,$('#weightForCost').val(),$('#weight').val(),'cost','Sell');"/>

                    <cong:label id="wei" text="/1000 KGS" styleClass="textlabelsBoldforlcl"></cong:label>
                    </td>
                    <td align="right" id="rateMin" class="textlabelsBoldforlcl">Minimum</td>
                    <td id="rateMinimum">
                    <cong:text name="minimum" styleClass="textlabelsBoldForTextBox twoDigitDecFormat wmrate wmsell" 
                               style="width:76px" id="minimum" onkeyup="checkForNumberAndDecimal(this);"  
                               onchange="checkValue(this,$('#minimumForCost').val(),$('#minimum').val(),'cost','Sell');"/>
                </td>
            </tr>
            <tr><td colspan="7"><pre>&nbsp;</pre></td></tr>
        </table> 
        <table width="100%" border="0">
            <tr>
                <td width="40%">
                    <table>
                        <tr>
                            <td width="20%"></td>
                            <td class="textlabelsBoldforlcl vendor">Vendor Name&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td>
                                <cong:autocompletor  name="thirdPartyname" styleClass="textlabelsBoldForTextBox vendor" 
                                                     readOnly="" id="thirdPartyname" fields="thirdpartyaccountNo"
                                                     container="null"     shouldMatch="true" width="600" query="VENDOR" 
                                                     template="tradingPartner" scrollHeight="300px" value="${lclQuoteCostAndChargeForm.thirdPartyname}"/>
                                <input type="text" name="thirdpartyaccountNo" id="thirdpartyaccountNo" 
                                       class="readonly textlabelsBoldForTextBoxDisabledLook" size="10" 
                                       value="${lclCostAndChargeForm.thirdpartyaccountNo}"/>
                            </td>
                        </tr>
                    </table>
                </td>
                <td width="30%">
                    <table>
                        <tr>
                            <td class="textlabelsBoldforlcl city" id="cityTemp">CITY/COUNTRY&nbsp;&nbsp;</td>
                            <td  id="manualCity">
                                <cong:text styleClass="mandatory text textuppercaseLetter city" 
                                           container="NULL" maxlength="25" name="manualCityName" id="manualcityName" /></td>
                            <td id="autoCompletorCity">
                                <cong:autocompletor id="cityName" name="cityName" template="one" fields="portname" 
                                                    query="CONCAT_CITY_DEST" styleClass="mandatory textlabelsBoldForTextBox city"
                                                    width="300" container="NULL" shouldMatch="true" scrollHeight="200px" 
                                                    value="${lclQuoteCostAndChargeForm.cityName}"/>
                            </td>
                            <td>
                                <cong:checkbox styleClass="city" name="manualCityChk" id="manualCityChk" onclick="showChangesCity()"
                                               title="City/Country not listed" container="NULL"/>
                            </td>
                        </tr>
                    </table>
                </td>
                <td width="30%" rowspan="3">
                    <table>
                        <tr>
                            <td style="padding-top:1.2em;" id="transitDays" class="textlabelsBoldforlcl TTDAY">
                                TT Dest-Oncarriage FD&nbsp;&nbsp;</td>
                            <td style="padding-top:1.2em;">
                                <cong:text name="totaldestCarriageTT" id="totaldestCarriageTT" styleClass="mandatory textlabelsBoldForTextBox TTDAY" 
                                           container="null" style="width:108px" maxlength="3"/>
                            </td>
                        </tr>
                        <tr>
                            <td width="28%" class="textlabelsBoldforlcl frequency" style="padding-top:.7em;">Frequency&nbsp;&nbsp;</td>
                            <td width="20%" class="frequency" style="padding-top:.7em;">
                                <html:select property="destFrequency" styleId="destFrequency" 
                                             value="${lclQuoteCostAndChargeForm.destFrequency}" style="width:110px;"
                                             styleClass="textlabelsBoldforlcl smalldropdownStyleForText mandatory"  onchange="setTransitDay();">
                                    <html:option value="">Select</html:option>
                                    <html:option value="7">Once a Week</html:option>
                                    <html:option value="30">Once a Month</html:option>
                                    <html:option value="14">Every other week</html:option>
                                    <html:option value="1">Daily</html:option>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td class="textlabelsBoldforlcl totalDestTT" style="padding-top:.7em;">Total Destination-FD TT&nbsp;&nbsp;</td>
                            <td class="totalDestTT" style="padding-top:.7em;">
                                <cong:text name="totalDestTT" id="totalDestTT" styleClass="bold mandatory" style="width:108px" 
                                           readOnly="true" container="null" maxlength="3" value="${lclQuoteCostAndChargeForm.totalDestTT}"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr><td colspan="3"><pre>&nbsp;</pre></td></tr>
        </table>
        <table width="100%">
            <tr>
                <td>
                    <table class="remark">
                        <tr>
                            <td class="bold">
                                Destination Services Remark </td>
                            <td>
                                <html:textarea cols="20" rows="10" styleId="destinatnSersRemark" 
                                               styleClass="refusedTextarea textlabelsBoldForTextBox textAreaLimit30"
                                               onkeyup="limitTextarea(this,15,30)" property="destinatnSersRemark" 
                                               value="${lclQuoteCostAndChargeForm.destinatnSersRemark}" />
                            </td>
                        </tr>
                    </table>
                    <table class="agent">
                        <tr>
                            <td id="alterAgent" width="32%" class="textlabelsBoldforlcl">
                                Oncarriage Destination Agent&nbsp;&nbsp;&nbsp;&nbsp;
                                <cong:autocompletor  name="alternateAgent"  styleClass="agent mandatory" id="alternateAgent"  readOnly="" 
                                                     fields="alternateAgentAccntNo" shouldMatch="true"  container="null" width="500" 
                                                     query="AGENT"  params="E" template="tradingPartner"  scrollHeight="160px" 
                                                     value="${lclQuoteCostAndChargeForm.alternateAgent}"/>
                                <cong:hidden name="alternateAgentAccntNo" id="alternateAgentAccntNo" value="${lclQuoteCostAndChargeForm.alternateAgentAccntNo}"/>
                                <input type="checkbox" name="sameFD" id="sameFD" class="agent" onclick="setOnCarriageAgent(this);"/>Same as FD agent.
                            </td>
                        </tr>
                    </table>        
                </td>
                <td width="20%"></td>
                <td width="30%"></td>
            </tr>
            <tr><td colspan="3"><pre>&nbsp;</pre></td></tr>
        </table>
        <table>
            <tr>
                <td>
                    <input type="button" class="button-style1" value="Save" onclick="saveCodeDs('save')" id="saveCode"/>
                    <input type="button" class="button-style1" value="Save and Close"  id="saveClose" onclick="saveCodeDs('saveClose');"/>
                    <input type="button" class="button-style1" value="Cancel"  id="cancelCode" onclick="closeWindow();"/>
                </td>
            </tr>
            <tr><td><pre>&nbsp;</pre></td></tr>
        </table>
        <table class="display-table">
            <thead>
            <th>Charge Code</th>
            <th>Charge Description</th>
            <th>Charge Amount</th>
            <th>Cost/Vendor</th>
            <th>Rate</th>
            <th>Action</th>
        </thead>
        <tbody>
            <c:if test="${not empty chargeList}">
                <c:forEach var="destCharge" items="${chargeList}">
                    <c:set var="rowStyle" value="${rowStyle eq 'oddStyle' ? 'evenStyle' : 'oddStyle'}"/>
                    <tr class="${rowStyle}">
                        <td>
                            ${destCharge.arglMapping.chargeCode}
                        </td>
                        <td>
                            ${destCharge.arglMapping.chargeDescriptions}
                        </td>
                        <td>
                            ${destCharge.arAmount}
                        </td>
                        <td>
                            ${destCharge.apAmount} / ${destCharge.supAcct.accountno}
                        </td>
                        <td>
                            ${destCharge.label2}
                        </td>
                        <td>
                            <img src="${path}/images/edit.png"  style="cursor:pointer" class="" width="13" height="13" alt="edit"
                                 onclick="displayDestinationServices('${path}', '${destCharge.id}',
                                                 '${destCharge.lclFileNumber.id}', '${destCharge.lclFileNumber.fileNumber}', 'editService');"
                                 title="Edit Charge"/>
                            <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete"
                                 onclick="deleteCharge('Are you sure you want to delete?', '${destCharge.id}',
                                                 '${destCharge.lclFileNumber.id}', '${destCharge.lclFileNumber.fileNumber}', 'deleteService');"
                                 title="Delete Charge"/>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
        </tbody>
    </table>
    <cong:hidden name="methodName" id="methodName"/>
    <cong:hidden name="destination" id="destination"/>
    <input type="hidden" name="buttonValue" id="buttonValue" value="${buttonValue}"/>
    <input type="hidden" name="bookingAcId" id="bookingAcId" value="${bookingAcId}"/>
    <input type="hidden" name="engmet" id="engmet" value="${engmet}"/>
</cong:form>
</body>
<script type="text/javascript">
    var dthc_Current_id = $("#dtchAdj").val();
    var onCarriageMarkUp = $("#markup").val();
    var profit = $("#profit").val();
    var max = $("#profitMax").val();
    var min = $("#profitMin").val();
    var oc_MaxProfit = $("#onCarriageMax").val();
    var oc_MinProfit = $("#onCarriageMin").val();

    $(document).ready(function () {
        $('#moduleName').val(parent.$('#moduleName').val());
        $("#totalDestTT").attr("readonly", true);
        $("#thirdpartyaccountNo").attr("readonly", true);
        $("#totalDestTT").addClass("readonly");
        if ($('#thirdpartyaccountNo').val() === "" || $('#thirdPartyname').val() === "") {
            $('#thirdpartyaccountNo').val(parent.$('#agentNumber').val());
            $('#thirdPartyname').val(parent.$("#agentName").val());
        }
        $(document).keydown(function (e) {
            if ($(e.target).attr("readonly")) {
                if (e.keyCode === 8) {
                    return false;
                }
            }
        });
        setToolTipText();
        $('#manualCity').hide();
        showValue();
        showValueDs();
        setOptionForRate();
        setCityCountryName();
    });

    function setOptionForRate() {
        var cost = $("#costAmount").val();
        var WmRate = $("#measureForCost").val();
        if (cost !== "" && cost !== "0.00" && WmRate !== "" && WmRate !== "0.00") {
            $("#BO").attr("checked", true);
            $(".rate_or").text("(AND)");
        } else if ((cost === "" || cost === "0.00") && (WmRate !== "" || WmRate !== "0.00")) {
            $("#WM").attr("checked", true);
            $(".rate_or").text("");
            $(".flat").attr("readonly", true);
            $(".flat").addClass("readonly");
            $(".flat").val("");
        } else if ((cost !== "" || cost !== "0.00") && (WmRate === "" || WmRate === "0.00")) {
            $("#FL").attr("checked", true);
            $(".wmrate").attr("readonly", true);
            $(".wmrate").addClass("readonly");
            $(".wmrate").val("");
            $(".rate_or").text("");
        }
        if ((cost === "" || cost === "0.00") && (WmRate === "" || WmRate === "0.00")) {
            $(".sell").attr("readonly", true);
            $(".sell").addClass("readonly");
        }
    }
    function showValue() {
        var buttonValue = $('#buttonValue').val();
        if ($('#metric').is(":checked")) {
            if (buttonValue == 'addCharge') {
                document.getElementById('wei').innerHTML = "/1000 KGS"
                document.getElementById('msr').innerHTML = "/CBM"
            }
            document.getElementById('wei1').innerHTML = "/1000 KGS"
            document.getElementById('msr1').innerHTML = "/CBM"
        }
        if ($('#imperial').is(":checked")) {
            if (buttonValue == 'addCharge') {
                document.getElementById('wei').innerHTML = "/100 LBS"
                document.getElementById('msr').innerHTML = "/CFT"
            }
            document.getElementById('wei1').innerHTML = "/100 LBS"
            document.getElementById('msr1').innerHTML = "/CFT"
        }
    }
    function saveCodeDs(button) {
        var chargesCode = $("#chargesCode option:selected").val();
        var thirdPartyname = $('#thirdPartyname').val();
        var flag = true;
        if (chargesCode === 'Select One') {
            flag = false;
            $("#chargesCode").css("border-color", "red");
            $.prompt("Select Atleast One Destination Service");
        } else if (!checkCostAndFlatRate()) {
            flag = false;
        } else if (chargesCode !== 'DTHC PREPAID' && $("#cityName").val() === "" && $("#manualcityName").val() === "") {
            $.prompt("Please Enter City ");
            $("#cityName").css("border-color", "red");
            flag = false;
        } else if (chargesCode === 'ONCARR' && $("#totaldestCarriageTT").val() === "") {
            $.prompt("Please Enter TT-Dest-Oncarriage FD");
            $("#totaldestCarriageTT").css("border-color", "red");
            flag = false;
        } else if (chargesCode === 'ONCARR' && $("#alternateAgent").val() === "") {
            $.prompt("Please Enter Agent");
            $("#alternateAgent").css("border-color", "red");
            flag = false;
        } else if (chargesCode === 'ONCARR' && $("#destFrequency").val() === "") {
            $.prompt("Please Enter Frequency");
            $("#destFrequency").css("border-color", "red");
            flag = false;
        } else if (chargesCode !== 'Select One') {
            flag = setEnableDisableCost(flag, 'LCLE');
        }
        if ($('#manualEntry').val() == 'true' && flag === true) {
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "checkVendorOptional",
                    param1: chargesCode,
                    dataType: "json"
                },
                success: function (data) {
                    if (data == true) {
                        submitAjaxForm('addManualChargesForExport', '#lclQuoteCostAndChargeForm', button);
                    } else {
                        if (thirdPartyname != "") {
                            submitAjaxForm('addManualChargesForExport', '#lclQuoteCostAndChargeForm', button);
                        } else {
                            sampleAlert('Vendor is required');
                            $("#thirdPartyname").css("border-color", "red");
                        }
                    }
                }
            });
        }
    }

    function showAlert(txt, id) {
        $.prompt(txt);
        $("#" + id).css("border-color", "red");
    }

    function checkChargeCost(fl) {
        if ($("#costAmount").val() === "" || $("#costAmount").val() === "0.00") {
            showAlert("Please enter cost amount", 'costAmount');
            return false;
            fl = false;
        } else if ($("#flatRateAmount").val() === "" || $("#flatRateAmount").val() === "0.00") {
            showAlert("Please enter charge amount", 'flatRateAmount');
            return false;
            fl = false;
        }
        return fl;
    }
    function checkwmRateCBM(fl) {
        if ($("#measureForCost").val() === "" || $("#measureForCost").val() === "0.00") {
            showAlert("Please enter W/M(Cost) Cbm amount", 'measureForCost');
            return false;
            fl = false;
        } else if ($("#weightForCost").val() === "" || $("#weightForCost").val() === "0.00") {
            showAlert("Please enter W/M(Cost) Kgs amount", 'weightForCost');
            return false;
            fl = false;
        } else if ($("#measure").val() === "" || $("#measure").val() === "0.00") {
            showAlert("Please enter W/M(Sell) Cbm amount", 'measure');
            return false;
            fl = false;
        } else if ($("#weight").val() === "" || $("#weight").val() === "0.00") {
            showAlert("Please enter W/M(Sell) Kgs amount", 'weight');
            return false;
            fl = false;
        }
        return fl;
    }
    function checkCostAndFlatRate() {
        var selectedRadio = $("input:radio[name=rateOption]:checked").val();
        var flag = true;
        if (selectedRadio === 'FLAT') {
            flag = checkChargeCost(flag);
        } else if (selectedRadio === 'WMRATE') {
            flag = checkwmRateCBM(flag);
        } else if (selectedRadio === 'BOTH') {
            var flag = checkChargeCost(flag);
            if (flag) {
                flag = checkwmRateCBM(flag);
            }
        }
        return flag;
    }
    function showValueDs() {
        var chargeCode = $("#chargesCode option:selected").val();
        if (chargeCode === 'DTHC PREPAID') {
            //$("#chargesCode").val(chargeCode);
            $(".remark").show();
            $(".city").hide();
            $(".agent").hide();
            $(".TTDAY").hide();
            $("#cityName").val('');
            $(".frequency").hide();
            $(".totalDestTT").hide();
        } else if (chargeCode === 'ONCARR') {
            //$("#chargesCode").val(chargeCode);
            $(".remark").hide();
            $(".city").show();
            $(".agent").show();
            $(".TTDAY").show();
            $(".frequency").show();
            $(".totalDestTT").show();
            $(".agent").show();
        } else {
            //$("#chargesCode").val(chargeCode);
            $(".remark").hide();
            $(".city").show();
            $(".agent").hide();
            $(".TTDAY").hide();
            $(".frequency").hide();
            $(".totalDestTT").hide();
        }
    }
    function setValuesDs() {
        $("#flatRateAmount").val('');
        $("#measure").val('');
        $("#weight").val('');
        $("#minimum").val('');
        $("#costAmount").val('');
        $("#measureForCost").val('');
        $("#weightForCost").val('');
        $("#minimumForCost").val('');
        $("#destinatnSersRemark").val('');
        $("#alternateAgent").val('');
        $(".city").val('');
        $("#WM").click();
        setCityCountryName();
    }
    function setCityCountryName() {
        var chargeCode = $("#chargesCode option:selected").val();
        $("#cityTemp").text(chargeCode === "ONCARR" ? "CITY, COUNTRY NAME CY"
                : chargeCode === "DELIV" ? "DOOR CITY, COUNTRY NAME"
                : chargeCode === "DAP" ? "DAP CITY, COUNTRY NAME"
                : chargeCode === "DDP" ? "DDP CITY, COUNTRY NAME" : "CITY/COUNTRY");
    }
    function checkForNumberAndDecimal(obj) {
        if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
            obj.value = "";
            sampleAlert("This field should be Numeric");
        }
    }
    function closeWindow() {
        parent.$.colorbox.close();
    }
    function sampleAlert(txt) {
        $.prompt(txt);
    }

    function showChangesCity() {
        $('#manualcityName').val('');
        $('#cityName').val('');
        if ($('#manualCityChk').is(":checked")) {
            $('#manualCity').show();
            $('#autoCompletorCity').hide();
        } else {
            $('#manualCity').hide();
            $('#autoCompletorCity').show();
        }
    }


    function setSelect(ele) {
        if (ele === "FL") {
            $(".wmrate").attr("readonly", true);
            $(".wmrate").addClass("readonly");
            $(".flat").attr("readonly", false);
            $(".flat").removeClass("readonly");
            $(".rate_or").text("");
        } else if (ele === "WM") {
            $(".flat").attr("readonly", true);
            $(".flat").addClass("readonly");
            $(".wmrate").attr("readonly", false);
            $(".wmrate").removeClass("readonly");
            $(".rate_or").text("");
        } else if (ele === "BO") {
            $(".wmrate").attr("readonly", false);
            $(".wmrate").removeClass("readonly");
            $(".flat").attr("readonly", false);
            $(".flat").removeClass("readonly");
            $(".rate_or").text("(AND)");
        }
        $(".wmrate").val('');
        $(".flat").val('');
        var flag = $("#overrideCalculator").is(":checked");
        if (!flag) {
            $(".wmsell").attr("readonly", true);
            $(".wmsell").addClass("readonly");
            $(".flatsell").attr("readonly", true);
            $(".flatsell").addClass("readonly");
        }
    }
    function calculateCharge(profit, min, max) {
        if (!$("#overrideCalculator").is(":checked")) {
            var chargeCode = $("#chargesCode option:selected").val();
            var costAmount = Number($("#costAmount").val());
            var weight = Number($("#weightForCost").val());
            var measure = Number($("#measureForCost").val());
            var minimum = Number($("#minimumForCost").val());
            if (chargeCode === 'DTHC PREPAID') {
                var calculationValue = $("#dtchAdj").val();
                $("#flatRateAmount").val(costAmount !== 0 ? Number(costAmount + ((calculationValue / 100) * costAmount)).toFixed(2) : '');
                $("#weight").val(weight !== 0 ? Number(weight + ((calculationValue / 100) * weight)).toFixed(2) : '');
                $("#measure").val(measure !== 0 ? Number(measure + ((calculationValue / 100) * measure)).toFixed(2) : '');
                $("#minimum").val(minimum !== 0 ? Number(minimum + ((calculationValue / 100) * minimum)).toFixed(2) : '');
                $("#costCalc").data(calculationValue + " Currency  Adjustment");
            } else if (chargeCode === 'ONCARR') {
                var calculationValue = Number($("#markup").val());
                $("#flatRateAmount").val(costAmount !== 0 ? Number(costAmount + calculationValue).toFixed(2) : '');
                $("#weight").val(weight !== 0 ? Number(weight + calculationValue).toFixed(2) : '');
                $("#measure").val(measure !== 0 ? Number(measure + calculationValue).toFixed(2) : '');
                $("#minimum").val(minimum !== 0 ? Number(minimum + calculationValue).toFixed(2) : '');
            } else {
                $("#flatRateAmount").val(costAmount !== 0 ? Number(costAmount + (Number(profit / 100) * costAmount)).toFixed(2) : '');
                $("#weight").val(weight !== 0 ? Number(weight + (Number(profit / 100) * weight)).toFixed(2) : '');
                $("#measure").val(measure !== 0 ? Number(measure + (Number(profit / 100) * measure)).toFixed(2) : '');
                $("#minimum").val(minimum !== 0 ? Number(minimum + (Number(profit / 100) * minimum)).toFixed(2) : '');
            }
        }
    }

    function setSelectForCharges() {
        var chargeCode = $("#chargesCode option:selected").val();
        if (chargeCode === 'DTHC PREPAID') {
            $("#BO").click();
            $("#costCalc").data('tipText', 'Calculate Sell Rate : <br> <font color=red>' + dthc_Current_id + '%</font> Currency  Adjustment');
        } else if (chargeCode === 'DELIV') {
            $("#costCalc").data('tipText', 'Calculate Sell Rate : <br> Profit: <font color=red>' + profit + '%</font>' +
                    ' Min Profit: <font color=red>' + min + '</font> Max Profit: <font color=red>' + max + '</font>');
            $("#FL").click();
        } else if (chargeCode === 'ONCARR') {
            $("#costCalc").data('tipText', 'Calculate Sell Rate : <br> Markup: <font color=red>' + onCarriageMarkUp + '</font>' +
                    ' Min Profit: <font color=red>' + oc_MinProfit + '</font> Max Profit: <font color=red>' + oc_MaxProfit + ' </font>');
            $("#WM").click();
        } else {
            $("#costCalc").data('tipText', 'Calculate Sell Rate : <br> Profit:<font color=red>' + profit + '%</font>' +
                    ' Min Profit: <font color=red>' + min + '</font> Max Profit: <font color=red>' + max + '</font>');
            $("#BO").click();
        }
    }

    function setTransitDay() {
        var ele = Number($("#destFrequency option:selected").val());
        var ttDays = Number($("#totaldestCarriageTT").val());
        $("#totalDestTT").val(ttDays !== 0 && ele !== 1 && ele !== "" ? Number(ele + ttDays) : ele === 1 ? Number(ttDays) : "");
    }
    function setOnCarriageAgent(ele) {
        if ($("#" + ele.id).is(":checked")) {
            $('#alternateAgentAccntNo').val(parent.$('#agentNumber').val());
            $('#alternateAgent').val(parent.$("#agentName").val());
        } else {
            $('#alternateAgentAccntNo').val("");
            $('#alternateAgent').val("");
        }
    }
    function setToolTipText() {
        var chargeCode = $("#chargesCode option:selected").val();
        if (chargeCode === 'DTHC PREPAID') {
            $("#costCalc").data('tipText', 'Calculate Sell Rate : <br> <font color=red>' + dthc_Current_id + '%</font> Currency  Adjustment');
        } else if (chargeCode === 'DELIV') {
            $("#costCalc").data('tipText', 'Calculate Sell Rate : <br> Profit: <font color=red>' + profit + '%</font>' +
                    ' Min Profit: <font color=red>' + min + '</font> Max Profit: <font color=red>' + max + '</font>');
        } else if (chargeCode === 'ONCARR') {
            $("#costCalc").data('tipText', 'Calculate Sell Rate : <br> Markup: <font color=red>' + onCarriageMarkUp + '</font>' +
                    ' Min Profit: <font color=red>' + oc_MinProfit + '</font> Max Profit: <font color=red>' + oc_MaxProfit + ' </font>');
        } else {
            $("#costCalc").data('tipText', 'Calculate Sell Rate : <br> Profit:<font color=red>' + profit + '%</font>' +
                    ' Min Profit: <font color=red>' + min + '</font> Max Profit: <font color=red>' + max + '</font>');
        }
    }

    function checkChargeCode() {
        var flag = true;
        var chargeCode = $("#chargesCode option:selected").val();
        var fileID = $('#fileNumberId').val();
        if (chargeCode !== '') {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO",
                    methodName: "isChargeCodeValidate",
                    param1: fileID,
                    param2: chargeCode,
                    param3: "true",
                    dataType: "json"
                },
                async: false,
                success: function (data) {
                    if (data === 'true') {
                        flag = false;
                        $.prompt("Charge Code is already exists.Please Select different Charge Code");
                        $("#chargesCode").val("");
                    }
                }
            });
        }
        return flag;
    }
    function submitAjaxForm(methodName, formName, button) {
        showLoading();
        var chargeCode = $("#chargesCode option:selected").val();
        var sellMin = chargeCode === 'ONCARR' ? oc_MinProfit : min;
        var sellMax = chargeCode === 'ONCARR' ? oc_MaxProfit : max;
        $("#profitMax").val(sellMax);
        $("#profitMin").val(sellMin);
        $("#methodName").val(methodName);
        var params = $(formName).serialize();
        var fileNumberId = $('#fileNumberId').val();
        var fileNumberStatus = $('#fileNumberStatus').val();
        var billingType = parent.$("input:radio[name='pcBoth']:checked").val();
        var moduleName = $('#moduleName').val();
        params += "&fileNumberId=" + fileNumberId + "&billingType=" + billingType + "&fileNumberStatus=" + fileNumberStatus;
        params += "&moduleName=" + moduleName + "&isDest_Service_Page=true";
        params += "&calculateMinMax=" + $("#overrideCalculator").is(":checked");
        $.post($(formName).attr("action"), params,
                function (data) {
                    parent.$("#destinationServices").removeClass('button-style1');
                    parent.$("#destinationServices").addClass('green-background');
                    if (button === 'save') {
                        displayDestinationServices('${path}', "", fileNumberId, "", 'addService');
                    } else {
                        closeWindow();
                    }
                    closePreloading();
                });
    }

    function displayDestinationServices(path, chargeId, fileNumberId, fileNumber, buttonValue) {
        showLoading();
        var fd = parent.$('#unlocationCode').val();
        var href = path + "/lclQuoteCostAndCharge.do?methodName=displayQuoteDestinationServices&buttonValue=" + buttonValue
                + "&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber
                + "&destination=" + fd + "&manualEntry=true" + "&id=" + chargeId;
        document.location.href = href;
    }

    function deleteCharge(txt, chargesCodeId, fileNumberId, fileNumber, buttonValue) {
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    showLoading();
                    var fd = parent.$('#unlocationCode').val();
                    var href = path + "/lclQuoteCostAndCharge.do?methodName=displayQuoteDestinationServices&buttonValue=" + buttonValue
                            + "&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber
                            + "&destination=" + fd + "&id=" + chargesCodeId + "&manualEntry=true";
                    document.location.href = href;
                } else if (v === 2) {
                    $.prompt.close();
                }
            }
        });
    }

    function overrideCalc(obj) {
        if ($("#" + obj.id).is(":checked")) {
            var ele = $("input:radio[name=rateOption]:checked").attr("id");
            if (ele === "FL") {
                $(".wmsell").attr("readonly", true);
                $(".wmsell").addClass("readonly");
                $(".flatsell").attr("readonly", false);
                $(".flatsell").removeClass("readonly");
            } else if (ele === "WM") {
                $(".flatsell").attr("readonly", true);
                $(".flatsell").addClass("readonly");
                $(".wmsell").attr("readonly", false);
                $(".wmsell").removeClass("readonly");
            } else if (ele === "BO") {
                $(".wmsell").attr("readonly", false);
                $(".wmsell").removeClass("readonly");
                $(".flatsell").attr("readonly", false);
                $(".flatsell").removeClass("readonly");
            }
            $(".wmsell").val('');
            $(".flatsell").val('');
        } else {
            $(".wmsell").attr("readonly", true);
            $(".wmsell").addClass("readonly");
            $(".flatsell").attr("readonly", true);
            $(".flatsell").addClass("readonly");
            $(".wmsell").val('');
            $(".flatsell").val('');
        }
    }

    function checkValue(obj, v1, v2, label1, label2) {
     if (v1 !== "" && v2 !== "") {
            if (Number(v1) > Number(v2)) {
                $.prompt(label2 + " should not be lower than " + label1 + " amount");
                $("#" + obj.id).val("");
                return false;
            }
        }
    }
</script>