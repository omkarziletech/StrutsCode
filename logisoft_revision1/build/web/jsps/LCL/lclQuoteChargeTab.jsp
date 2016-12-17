<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>

<body style="background:#ffffff">
    <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
        Charges for File No:<span class="fileNo">${fileNumber}</span>
    </cong:div><br><br>
    <cong:form name="lclQuoteCostAndChargeForm" id="lclQuoteCostAndChargeForm" action="/lclQuoteCostAndCharge.do">
        <jsp:useBean id="billToParty" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO"/>
        <c:set var="billToPartyList" value="${billToParty.allBillToPartyImp}"/>
        <cong:hidden name="id" value="${id}"/>
        <cong:hidden name="fileNumber" value="${fileNumber}"/>
        <cong:hidden name="fileNumberId" id="fileNumberId" value="${fileNumberId}"/>
        <cong:hidden name="moduleName"  id="moduleName" value='${lclQuoteCostAndChargeForm.moduleName}' />
        <cong:hidden name="manualEntry"  id="manualEntry" value='${lclQuoteCostAndChargeForm.manualEntry}' />
        <input type="hidden" name="disableCost" id="disableCost" value="${disableCost}"/>
        <c:set var="shipmentType" value="${lclQuoteCostAndChargeForm.moduleName eq 'Imports' ? 'LCLI' : 'LCLE'}"/>
        <c:set var="manual" value="${lclQuoteCostAndChargeForm.manualEntry ? true : false}"/>
        <cong:table width="100%" styleClass="textBoldforlcl" border="0">
            <cong:tr>
                <cong:td styleClass="td">Code</cong:td>
                <cong:td><cong:autocompletor name="chargesCode" id="chargesCode" template="two" query="CHARGE_CODE" fields="chargesCodeDesc,chargesCodeId"
                                             shouldMatch="true" scrollHeight="150"   params="${shipmentType}" container="NULL" 
                                             styleClass="text mandatory " width="400" callback="checkChargeCode()"/>
                    <cong:hidden name="chargesCodeId" id="chargesCodeId"/>
                    <cong:hidden name="chargesCodeDesc" id="chargesCodeDesc"/>
                </cong:td>
                <cong:td colspan="2" styleClass="td" valign="middle">UOM&nbsp;&nbsp;&nbsp;</cong:td>
                <cong:td colspan="4">
                    <cong:radio name="uom" value="M" id="metric" onclick="showValue();" container="NULL"/>M
                    <cong:radio name="uom" value="I" id="imperial" onclick="showValue();" container="NULL"/>I
                    <c:if test="${shipmentType eq 'LCLE'}">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="radio" name="rateOption" value="FLAT" id="FL"  onclick="setSelect();"/>Flat Rate Only
                        <input type="radio" name="rateOption" value="WMRATE" id="WM" onclick="setSelect();"/>W/M Rate Only
                        <input type="radio" name="rateOption" value="BOTH" id="BO" checked onclick="setSelect();"/>Both
                    </c:if>
                </cong:td>
            </cong:tr>
            <cong:tr><cong:td><br/></cong:td></cong:tr>
                    <!-- Charge section starts here -->
            <cong:tr>
                <c:choose>
                    <c:when test="${lclQuoteCostAndChargeForm.manualEntry}">
                        <cong:td styleClass="td">Charge Amount(Sell)</cong:td>
                        <cong:td>
                            <cong:text styleClass="text twoDigitDecFormat chargeAmt flatCharge" style="width:76px" name="flatRateAmount" id="flatRateAmount" 
                                       container="null" onkeyup="checkForNumberAndDecimal(this);"/>(flat rate)
                        </cong:td>
                    </c:when>
                    <c:otherwise>
                        <cong:td styleClass="td">Charge Amount</cong:td>
                        <cong:td>
                            <cong:text styleClass="text twoDigitDecFormat chargeAmt flatCharge" style="width:76px" name="arAmount" id="arAmount" container="null"/>
                        </cong:td>
                    </c:otherwise>
                </c:choose>
                <cong:td styleClass="rate_or">${shipmentType eq 'LCLI' ? '(OR)' : ''}</cong:td>
                <cong:td align="right">W/M Rate</cong:td>
                <cong:td>
                    <cong:text name="measure" styleClass="text twoDigitDecFormat chargeAmt wmrateCharge" style="width:76px" id="measure" container="null"
                               onkeyup="checkForNumberAndDecimal(this);"/>
                    <cong:label id="msr" text=" /CBM"></cong:label>
                    <cong:text name="weight" styleClass="text twoDigitDecFormat chargeAmt wmrateCharge" style="width:76px" id="weight" container="null"
                               onkeyup="checkForNumberAndDecimal(this);"/>
                    <cong:label id="wei" text="/1000 KGS"></cong:label>
                </cong:td>
                <cong:td>Minimum
                    <cong:text name="minimum" styleClass="text twoDigitDecFormat chargeAmt wmrateCharge" style="width:76px" id="minimum"  container="null"
                               onkeyup="checkForNumberAndDecimal(this);" />
                </cong:td>
            </cong:tr>
            <cong:tr><cong:td><br/></cong:td></cong:tr>
                    <!-- Charge section ends here -->    
            <cong:tr>
                <c:choose>
                    <c:when test="${shipmentType ne 'LCLI'}">
                        <cong:td styleClass="td" valign="middle">Bill this Charge On</cong:td>
                        <cong:td>
                            <input type="radio" name="billCharge" id="billChargeInvoice" value="IV" container="NULL" 
                                   checked="${lclQuoteAc.billCharge=='IV'?'yes':''}"/>Invoice
                            <input type="radio" name="billCharge" id="billChargeBL" value="BL" container="NULL" 
                                   checked="${lclQuoteAc.billCharge=='IV'?'':'yes'}"/>B/L
                        </cong:td>
                    </c:when>
                    <c:otherwise>
                        <cong:td styleClass="td" valign="middle">Bill To Party</cong:td>
                        <cong:td colspan="4">
                            <html:select property="billToParty" styleId="billToParty" style="width:134px" styleClass="smallDropDown textlabelsBoldforlcl" >
                                <html:optionsCollection name="billToPartyList"/>
                            </html:select>
                        </cong:td>
                    </c:otherwise>
                </c:choose>
            </cong:tr>
            <cong:tr><cong:td><br/></cong:td></cong:tr>
                    <!-- Cost section starts here -->  
            <cong:tr>
                <cong:td styleClass="td">Cost Amount</cong:td>
                <cong:td><cong:text styleClass="text twoDigitDecFormat costAmt flatCost" style="width:76px" name="apAmount" id="apAmount" container="null"
                                    onkeyup="allowNegativeNumbers(this);" onchange="costAmountValidate(this);"/>(flat rate)
                </cong:td>
                <cong:td styleClass="rate_or">${shipmentType eq 'LCLI' ? '(OR)' : ''}</cong:td>
                <cong:td align="right">W/M Cost</cong:td>
                <cong:td>
                    <cong:text name="measureForCost" styleClass="text twoDigitDecFormat costAmt wmrateCost" style="width:76px" id="measureForCost" container="null"
                               onkeyup="checkForNumberAndDecimal(this);enableThirdParty();if(${shipmentType eq 'LCLE'}){enableVendor(this);}" />
                    <cong:label id="msrcost" text=" /CFT"></cong:label>
                    <cong:text name="weightForCost" styleClass="text twoDigitDecFormat costAmt wmrateCost" style="width:76px" id="weightForCost" container="null"
                               onkeyup="checkForNumberAndDecimal(this);enableThirdParty();if(${shipmentType eq 'LCLE'}){enableVendor(this);}"/>
                    <cong:label id="weicost" text="/100 LBS"></cong:label>
                </cong:td>
                <cong:td>Minimum
                    <cong:text name="minimumForCost" styleClass="text twoDigitDecFormat costAmt wmrateCost" style="width:76px" id="minimumForCost" container="null"
                               onkeyup="checkForNumberAndDecimal(this);enableThirdParty();"/>
                </cong:td>
            </cong:tr>
        </cong:table><br/>
        <!-- Charge section ends here -->      
        <cong:table border="0">
            <cong:tr>
                <cong:td width="6%">&nbsp;</cong:td>
                <cong:td styleClass="bold" align="right" width="8%">Vendor Name 
                    <cong:autocompletor  name="thirdPartyname"  id="thirdPartyname" fields="thirdpartyaccountNo,NULL,NULL,NULL,NULL,NULL,NULL,thirdpartyDisabled,NULL,NULL,NULL,NULL,NULL,thirdparyDisableAcct" 
                                         container="NULL"  shouldMatch="true" width="600" query="VENDOR" callback="vendorTypeCheck();" template="tradingPartner" scrollHeight="150"/>
                    <c:if test="${shipmentType eq 'LCLI'}">
                        <span title="Agent">
                            <input type="checkbox" name="selectQuoteAgentAccount"  id="selectQuoteAgentAccount" onclick="selectQuoteAgentAccountNo();"/>
                        </span>
                    </c:if>
                    <input type="hidden" name="thirdpartyDisabled" id="thirdpartyDisabled"/>
                    <input type="hidden" name="thirdparyDisableAcct" id="thirdparyDisableAcct"/>
                </cong:td>
                <cong:td width="6%">&nbsp;</cong:td>
                <cong:td  styleClass="bold">Vendor#
                    <cong:text  name="thirdpartyaccountNo" id="thirdpartyaccountNo" styleClass="text-readonly" readOnly="true"/>
                </cong:td>
                <cong:td  styleClass="bold">Invoice Number
                    <cong:text styleClass="text"  maxlength="25" name="invoiceNumber" id="invoiceNumber" onkeyup="checkForNumberAndDecimal(this)"/>
                </cong:td>
            </cong:tr>
        </cong:table><br><br>
        <cong:table>
            <cong:tr>
                <cong:td >
                    <input type="button" class="button-style1" value="Save" id="saveCode" onclick="submitCharge();"/>
                </cong:td>
            </cong:tr>
        </cong:table>
        <cong:hidden name="methodName" id="methodName"/>
        <cong:hidden name="fileNumberId" id="fileNumberId"/>
        <input type="hidden" name="quoteAcId" id="quoteAcId" value="${quoteAcId}"/>
        <input type="hidden" name="engmet" id="engmet" value="${engmet}"/>
        <input type="hidden" name="costAmount" id="costAmount"/>
    </cong:form>
</body>

<script type="text/javascript">

    $(document).ready(function () {
        var quoteAcId = document.getElementById('quoteAcId').value;
        var apAmount = $("#apAmount").val();
        var flatrate = $('#flatRateAmount').val();
        if ($("#moduleName").val() === "Imports") {
            costAmountValidate(apAmount);
            disableWMCostFields();
        } else {
            setOptionForRate();
            costAmountValidateForExport();
        }

        if (quoteAcId === null || quoteAcId === "" || quoteAcId === "0") {
            if (document.getElementById('engmet').value === "E") {
                $('#imperial').attr('checked', true);
            }
        }
        if (parent.$('#supplierNameOrg').val() === '') {
            $("#selectQuoteAgentAccount").hide();
            //$("#quoteAgent").hide();
        }
        if ($('#methodName').val() === 'editQuoteCharge' && $('#thirdPartyname').val() !== ""
                && ($('#thirdPartyname').val() === parent.$('#supplierNameOrg').val())) {
            $("#selectQuoteAgentAccount").attr('checked', true);
        } else {
            $("#selectQuoteAgentAccount").attr('checked', false);
        }
        $('#thirdPartyname').keyup(function () {
            if ($(this).val() === '') {
                $("#selectQuoteAgentAccount").attr('checked', false);
                $('#thirdpartyaccountNo').val('');
            }
        });
        showValue();
        if (flatrate === '0.00' && $("#moduleName").val() === "Imports") {
            $('#flatRateAmount').addClass('text-readonly');
            $('#flatRateAmount').attr('readonly', true);
        }
    });
    function submitCharge() {
        var costCode = $("#chargesCode").val();
        var costFlag = false;
        $(".mandatory").each(function () {
            if ($(this).val().length === 0) {
                sampleAlert('This field is required');
                $(this).css("border-color", "red");
                $(this).focus();
                return false;
            } else {
                $(".costAmt").each(function () {
                    if ($(this).val() !== "" && $(this).val() !== "0.00") {
                        costFlag = true;
                    }
                });
                if ($("#moduleName").val() !== "Imports") {
                    $("#costAmount").val($("#apAmount").val());
                    var chargeFlag = false;
                    var methodName = $('#manualEntry').val() === 'true' ? 'addManualChargesForExport' : 'addCharges';
                    $(".chargeAmt").each(function () {
                        if ($(this).val() !== "" && $(this).val() !== "0.00") {
                            chargeFlag = true;
                        }
                    });
                    var data = getSellVendorValues($('#chargesCode').val());
                    if (data[0].trim() === 'C' && chargeFlag) {
                        $.prompt('Charge is not applicable for selected code');
                        return false;
                    }
                    if (data[0].trim() === 'S' && costFlag) {
                        $.prompt('Cost is not applicable for selected code');
                        return false;
                    }
                    if (!chargeFlag && !costFlag) {
                        $.prompt('Cost Amount/Charge Amount is required and it should not be equal to zero');
                        return false;
                    }
                    if (costFlag) {
                        var originId = parent.$("#portOfOriginId").val();
                        if (data[1].trim() !== 'Y' && $('#thirdPartyname').val() === "") {
                            $.prompt("Vendor is required");
                            $('#thirdPartyname').css("border-color", "red");
                            $('#thirdPartyname').focus();
//                        } else if (!validateCostGLAccount(costCode, originId)) {
//                            return false;
//                        }
                        } else {
                            submitAjaxForm(methodName, '#lclQuoteCostAndChargeForm', '#chargeDesc');
                        }
                    } else {
                        submitAjaxForm(methodName, '#lclQuoteCostAndChargeForm', '#chargeDesc');
                    }
                } else {
                    if (($('#thirdPartyname').val() !== "" || $('#invoiceNumber').val() !== "") && ($('#apAmount').val() === null || $('#apAmount').val() === "" || $('#apAmount').val() === '0.00')) {
                        $.prompt('Cost Amount is required and it should not be equal to zero');
                        $('#apAmount').css("border-color", "red");
                    } else if ($('#apAmount').val() !== null && $('#apAmount').val() !== "" && $('#apAmount').val() !== '0.00' && $('#thirdPartyname').val() === "") {
                        $.prompt("Vendor is required");
                        $('#thirdPartyname').css("border-color", "red");
                        $('#thirdPartyname').focus();
                    } else if (costFlag) {
                        if (checkAddCostMappingWithGL(costCode)) {
                            submitAjaxForm('addCharges', '#lclQuoteCostAndChargeForm', '#chargeDesc');
                        }
                    } else {
                        submitAjaxForm('addCharges', '#lclQuoteCostAndChargeForm', '#chargeDesc');
                    }
                }
            }
        });
    }

    function costAmountValidateForExport() {
        var costFlag = false;
        $(".costAmt").each(function () {
            if ($(this).val() !== "" && $(this).val() !== "0.00") {
                costFlag = true;
            }
        });
        if (!costFlag) {
            disableTextBox("thirdPartyname");
            disableTextBox("invoiceNumber");
        }
    }
    function getSellVendorValues(chargeCode) {
        var dataValue = "";
        $.ajaxx({dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getLCLSellVendorValues",
                param1: chargeCode,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                dataValue = data;
            }
        });
        return dataValue;
    }
    function allowNegativeNumbers(obj) {
        var result;
        if (!/^-?\d*(\.\d{0,6})?$/.test(obj.value)) {
            obj.value = "";
            sampleAlert("This field should be Numeric");
        }
    }
    function vendorTypeCheck() {
        if ($('#thirdpartyDisabled').val() === 'Y') {
            $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#thirdparyDisableAcct').val() + "</span>");
            $('#thirdpartyaccountNo').val('');
            $('#thirdPartyname').val('');
        }
    }
    function costAmountValidate(id) {
        var costAmount = $(id).val() !== undefined ? $(id).val() : id;
        if (costAmount === null || costAmount === "" || costAmount === '0' || costAmount === '0.00') {
            disableTextBox("thirdPartyname");
            disableTextBox("invoiceNumber");
            $("#apAmount").val('');
            $("#thirdPartyname").val('');
            $("#thirdpartyaccountNo").val('');
            $("#selectQuoteAgentAccount").hide();
            $("#selectQuoteAgentAccount").attr("checked", false);
            //$('#quoteAgent').hide();
        } else {
            enableTextBox("thirdPartyname");
            enableTextBox("invoiceNumber");
            $("#selectQuoteAgentAccount").show();
            //$('#quoteAgent').show();
        }
    }
    function enableThirdParty() {
        var measureForCost = $('#measureForCost').val();
        var weightForCost = $('#weightForCost').val();
        var minimumForCost = $('#minimumForCost').val();
        if ((weightForCost !== null && weightForCost !== "" && weightForCost !== '0.00') &&
                (measureForCost !== null && measureForCost !== "" && measureForCost !== '0.00') &&
                (minimumForCost !== null && minimumForCost !== "" && minimumForCost !== '0.00')) {
            enableTextBox("thirdPartyname");
            enableTextBox("invoiceNumber");
            $("#selectQuoteAgentAccount").show();
            //$('#quoteAgent').show();
        } else {
            disableTextBox("thirdPartyname");
            disableTextBox("invoiceNumber");
            $("#selectQuoteAgentAccount").hide();
            //$('#quoteAgent').hide();
        }
    }
    function uomReadonlyCost() {
        var weightCost = $('#weightForCost').val();
        var measureCost = $('#measureForCost').val();
        var minimumCost = $('#minimumForCost').val();
        if ((minimumCost !== "" && minimumCost !== "0.00") || (measureCost !== "" && measureCost !== "0.00") || (weightCost !== ""
                && weightCost !== "0.00")) {
            disableFLCostFields();
        } else {
            if ($("#moduleName").val() !== "Imports") {
                enableFLCostFields();
            }
        }
    }

    function rateChargeReadonly() {
        var flatRate = $('#flatRateAmount').val();
        if (flatRate !== "") {
            disableWMChargeFields();
        } else {
            enableWMChargeFields();
        }
    }

    function disableWMCostFields() {
        disableTextBox("measureForCost");
        disableTextBox("weightForCost");
        disableTextBox("minimumForCost");
    }
    function enableWMCostFields() {
        enableTextBox("measureForCost");
        enableTextBox("weightForCost");
        enableTextBox("minimumForCost");
    }
    function disableWMChargeFields() {
        disableTextBox("measure");
        disableTextBox("weight");
        disableTextBox("minimum");
    }
    function enableWMChargeFields() {
        enableTextBox("measure");
        enableTextBox("weight");
        enableTextBox("minimum");
    }

    function disableFLCostFields() {
        disableTextBox("apAmount");
    }
    function enableFLCostFields() {
        enableTextBox("apAmount");
    }

    function rateCostReadonly() {
        var apAmount = $('#apAmount').val();
        if (apAmount !== "") {
            disableWMCostFields();
        } else {
            enableWMCostFields();
        }
    }
    function disableWMCostFields() {
        disableTextBox("measureForCost");
        disableTextBox("weightForCost");
        disableTextBox("minimumForCost");
    }
    function enableWMCostFields() {
        enableTextBox("measureForCost");
        enableTextBox("weightForCost");
        enableTextBox("minimumForCost");
    }
    function enableVendor(ele) {
        if (Number(ele.value) > 0) {
            enableTextBox("thirdPartyname");
            enableTextBox("invoiceNumber");
        }
    }

    jQuery(document).ready(function () {
        if ($('#moduleName').val() === 'Imports') {
            disableFieldByName("uom", "radio");
            disableTextBox("weight");
            disableTextBox("measure");
            disableTextBox("minimum");
        }
        if ($("#quoteAcId").val() !== "" && $('#disableCost').val() === "disableCostButton") {
            disableTextBox("apAmount");
            disableTextBox("thirdPartyname");
            disableTextBox("invoiceNumber");
            disableTextBox("measureForCost");
            disableTextBox("weightForCost");
            disableTextBox("minimumForCost");
        }
        $('#thirdPartyname').change(function () {
            $('#thirdpartyaccountNo').val('');
        });
    });
    function submitAjaxForm(methodName, formName, selector) {
        showProgressBar();
        $("#methodName").val(methodName);
        var params = $(formName).serialize();
        var fileNumberId = $('#fileNumberId').val();
        var billingType = parent.$("input:radio[name='pcBoth']:checked").val();
        params += "&fileNumberId=" + fileNumberId + "&billingType=" + billingType + "&moduleName=" + $('#moduleName').val();
        $.post($(formName).attr("action"), params, function (data) {
            if ($('#moduleName').val() === 'Exports') {
                pickUpEnable();
            }
            $(selector).html(data);
            $(selector, window.parent.document).html(data);
            parent.$.fn.colorbox.close();
        });
    }
    function pickUpEnable() {
        var chargeCode = $('#chargesCode').val();
        var pooDoorSelector = parent.$('input:radio[name=pooDoor]:checked');
        if (chargeCode === "INLAND" && pooDoorSelector.val() === 'N') {
            parent.$('#doorOriginY').attr('checked', true);
            parent.$('#doorOriginCityZip').removeClass("textlabelsBoldForTextBoxDisabledLook");
            parent.$('#doorOriginCityZip').addClass("textlabelsBoldForTextBox");
            parent.$('#doorOriginCityZip').removeAttr("readonly");
        }
    }
    function sampleAlert(txt) {
        $.prompt(txt);
    }
    function checkForNumberAndDecimal(obj) {
        var result;
        if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
            obj.value = "";
            sampleAlert("This field should be Numeric");
        }
    }

    function showValue() {
        if ($('#metric').is(":checked")) {
            document.getElementById('wei').innerHTML = "/1000 KGS";
            document.getElementById('msr').innerHTML = "/CBM";
            document.getElementById('weicost').innerHTML = "/1000 KGS";
            document.getElementById('msrcost').innerHTML = "/CBM";
        }
        if ($('#imperial').is(":checked")) {
            document.getElementById('wei').innerHTML = "/100 LBS";
            document.getElementById('msr').innerHTML = "/CFT";
            document.getElementById('weicost').innerHTML = "/100 LBS";
            document.getElementById('msrcost').innerHTML = "/CFT";
        }
    }
    function rateReadonly() {
        var flatRate = $('#flatRateAmount').val();
        var costAmount = $('#costAmount').val();
        var flatRate = $('#flatRateAmount').val();
        var costAmount = $('#costAmount').val();
        if (flatRate !== "") {
            $('#weight').addClass('text-readonly');
            $('#weight').attr('readonly', true);
            $('#measure').addClass('text-readonly');
            $('#measure').attr('readonly', true);
            $('#minimum').addClass('text-readonly');
            $('#minimum').attr('readonly', true);
        }
        if (flatRate === "") {
            $('#weight').removeClass('text-readonly');
            $('#weight').attr('readonly', false);
            $('#measure').removeClass('text-readonly');
            $('#measure').attr('readonly', false);
            $('#minimum').removeClass('text-readonly');
            $('#minimum').attr('readonly', false);
        }
        if (costAmount !== "") {
            $('#weightForCost').addClass('text-readonly');
            $('#weightForCost').attr('readonly', true);
            $('#measureForCost').addClass('text-readonly');
            $('#measureForCost').attr('readonly', true);
            $('#minimumForCost').addClass('text-readonly');
            $('#minimumForCost').attr('readonly', true);
        }
        if (costAmount === "") {
            $('#weightForCost').removeClass('text-readonly');
            $('#weightForCost').attr('readonly', false);
            $('#measureForCost').removeClass('text-readonly');
            $('#measureForCost').attr('readonly', false);
            $('#minimumForCost').removeClass('text-readonly');
            $('#minimumForCost').attr('readonly', false);
        }
    }

    function uomReadonly() {
        if ($('#moduleName').val() !== 'Imports') {
            var weight = $('#weight').val();
            var measure = $('#measure').val();
            var minimum = $('#minimum').val();
            var weightCost = $('#weightForCost').val();
            var measureCost = $('#measureForCost').val();
            var minimumCost = $('#minimumForCost').val();
            if (minimum !== "" || measure !== "" || weight !== "") {
                $('#flatRateAmount').addClass('text-readonly');
                $('#flatRateAmount').attr('readonly', true);
            }
            if (minimum === "" && measure === "" && weight === "") {
                $('#flatRateAmount').removeClass('text-readonly');
                $('#flatRateAmount').attr('readonly', false);
            }
            if (minimumCost !== "" || measureCost !== "" || weightCost !== "") {
                $('#costAmount').addClass('text-readonly');
                $('#costAmount').attr('readonly', true);
            }
            if (minimumCost === "" && measureCost === "" && weightCost === "") {
                $('#costAmount').removeClass('text-readonly');
                $('#costAmount').attr('readonly', false);
            }
        }
    }
    function selectQuoteAgentAccountNo() {
        var checked = $("#selectQuoteAgentAccount").attr('checked') ? true : false;
        if (checked) {
            $('#thirdPartyname').val(parent.$('#supplierNameOrg').val());
            $('#thirdpartyaccountNo').val(parent.$('#supplierCode').val());
        }
        else {
            $('#thirdPartyname').val('');
            $('#thirdpartyaccountNo').val('');
        }
    }

    function checkChargeCode() {
        var chargeCode = $('#chargesCode').val();
        var fileId = $('#fileNumberId').val();
        var shipmentType = $('#moduleName').val() === "Imports" ? "LCLI" : "LCLE";
        if (chargeCode !== '' && fileId !== '') {
            jQuery.ajaxx({
                dataType: "json", data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO",
                    methodName: "isChargeCodeValidate",
                    param1: fileId, param2: chargeCode,
                    param3: "true", dataType: "json"
                },
                async: false,
                success: function (data) {
                    if (data === 'true') {
                        $("#chargesCode").val('');
                        $("#chargesCode").css("border-color", "red");
                        $.prompt("Charge Code is already exists.Please Select different Charge Code");
                        return false;
                    } else if (!parent.isDestinationCharge(chargeCode) && shipmentType === 'LCLE') {
                        $("#chargesCode").val('');
                        $("#chargesCode").css("border-color", "red");
                        $.prompt("For <span class='red'>" + chargeCode + "</span> charge code, you need to use the Destination Services button.");
                        return false;
                    } else {
                        if (shipmentType === 'LCLI') {
                            validateCostChargeCode(chargeCode, shipmentType);
                        } else {
                            setSelect();
                        }
                    }
                }
            });
        }
    }

    function validateCostChargeCode(chargeCode, shipmentType) {
        jQuery.ajaxx({
            dataType: "json", data: {
                className: "com.gp.cong.logisoft.lcl.bc.LclBookingUtils",
                methodName: "validateForChargeandCost",
                param1: chargeCode,
                param2: shipmentType,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data[0] === 'N') {
                    disableTextBox("flatRateAmount");
                } else {
                    enableTextBox("flatRateAmount");
                }
                if (data[1] === 'N') {
                    disableTextBox("apAmount");
                    disableTextBox("thirdPartyname");
                    disableTextBox("invoiceNumber");
                    $("#selectQuoteAgentAccount").hide();
                    // $('#quoteAgent').hide();
                    $("#thirdPartyname").val('');
                    $("#thirdpartyaccountNo").val('');
                    $("#selectQuoteAgentAccount").attr('checked', false);
                    disableTextBox("measureForCost");
                    disableTextBox("weightForCost");
                    disableTextBox("minimumForCost");
                } else {
                    enableTextBox("apAmount");
                }
            }
        });
    }

    $(document).ready(function () {
        $(document).keydown(function (e) {
            if ($(e.target).attr("readonly")) {
                if (e.keyCode === 8) {
                    return false;
                }
            }
        });
    });
    function disableFieldByName(selector, type) {
        $('input:' + type + '[name=' + selector + ']').each(function () {
            $(this).attr("disabled", true);
        });
    }

    function disableTextBox(id) {
        $('#' + id).addClass('text-readonly');
        $('#' + id).attr('readonly', true);
        $('#' + id).attr('tabindex', -1);
        $("#" + id).val('');
    }
    function enableTextBox(id) {
        $('#' + id).removeClass('text-readonly');
        $('#' + id).attr('readonly', false);
        $('#' + id).addClass('text');
        $('#' + id).attr('tabindex', 0);
    }

    function disableClassText(ele) {
        $('.' + ele).addClass('text-readonly');
        $('.' + ele).attr('readonly', true);
        $('.' + ele).attr('tabindex', -1);
        $("." + ele).val('');
    }

    function enableClassText(ele) {
        $('.' + ele).removeClass('text-readonly');
        $('.' + ele).attr('readonly', false);
        $('.' + ele).addClass('text');
        $('.' + ele).attr('tabindex', 0);
    }

    function validateCostCharge(value, ele) {
        if (value[0] === 'N' && value[1] === 'Y') {
            if (ele === 'FL') {
                enableClassText("flatCost");
                disableClassText("wmrateCost");
            } else if (ele === 'WM') {
                disableClassText("flatCost");
                enableClassText("wmrateCost");
            } else if (ele === 'BO') {
                enableClassText("flatCost");
                enableClassText("wmrateCost");
            }
            disableClassText("wmrateCharge");
            disableClassText("flatCharge");
        } else if (value[0] === 'Y' && value[1] === 'N') {
            if (ele === 'FL') {
                disableClassText("wmrateCharge");
                enableClassText("flatCharge");
            } else if (ele === 'WM') {
                enableClassText("wmrateCharge");
                disableClassText("flatCharge");
            } else if (ele === 'BO') {
                enableClassText("wmrateCharge");
                enableClassText("flatCharge");
            }
            disableClassText("flatCost");
            disableClassText("wmrateCost");
        }
    }

    function setSelect() {
        var ele = $("input:radio[name=rateOption]:checked").attr("id");
        var chargeCode = $('#chargesCode').val();
        var value = chargeCode !== "" ? checkcostAvaliable(chargeCode) : "";
        if (ele === "FL") {
            if (value === "" || (value[0] === 'Y' && value[1] === 'Y')) {
                enableClassText("flatCharge");
                enableClassText("flatCost");
                disableClassText("wmrateCharge");
                disableClassText("wmrateCost");
            } else {
                validateCostCharge(value, ele);
            }
            $(".rate_or").text("");
        } else if (ele === "WM") {
            if (value === "" || (value[0] === 'Y' && value[1] === 'Y')) {
                disableClassText("flatCharge");
                enableClassText("wmrateCharge");
                disableClassText("flatCost");
                enableClassText("wmrateCost");
            } else {
                validateCostCharge(value, ele);
            }
            $(".rate_or").text("");
        } else if (ele === "BO") {
            if (value === "" || (value[0] === 'Y' && value[1] === 'Y')) {
                enableClassText("flatCharge");
                enableClassText("wmrateCharge");
                enableClassText("flatCost");
                enableClassText("wmrateCost");
            } else {
                validateCostCharge(value, ele);
            }
            $(".rate_or").text("(AND)");
        }
        $(".wmrate").val('');
        $(".flat").val('');
    }

    function setOptionForRate() {
        var charge = $("#flatRateAmount").val();
        var WmRate = $("#measure").val();
        if (charge !== "" && charge !== "0.00" && WmRate !== "" && WmRate !== "0.00") {
            $("#BO").attr("checked", true);
            $(".rate_or").text("(AND)");
        } else if ((charge === "" || charge === "0.00") && (WmRate !== "" || WmRate !== "0.00")) {
            $("#WM").attr("checked", true);
            disableClassText("flatCharge");
            disableClassText("flatCost");
            $(".rate_or").text("");
        } else if ((charge !== "" || charge !== "0.00") && (WmRate === "" || WmRate === "0.00")) {
            $("#FL").attr("checked", true);
            disableClassText("wmrateCharge");
            disableClassText("wmrateCost");
            $(".rate_or").text("");
        }
    }
    function checkcostAvaliable(chargeCode) {
        var value = "";
        jQuery.ajaxx({
            dataType: "json", data: {
                className: "com.gp.cong.logisoft.lcl.bc.LclBookingUtils",
                methodName: "validateForChargeandCost",
                param1: chargeCode,
                param2: "LCLE",
                dataType: "json"
            },
            async: false,
            success: function (data) {
                value = data;
            }});
        return value;
    }

    function validateCostGLAccount(chargeId, originId) {
        var flag = true;
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO",
                methodName: "validateLclExportGlAccount",
                param1: chargeId,
                param2: originId,
                param3: parent.$("#trmnum").val(),
                param4: "AC",
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data !== '') {
                    flag = false;
                    $.prompt(data);
                }
            }
        });
        return flag;
    }

    function checkAddCostMappingWithGL(costCode) {
        var flag = true;
        var terminalNo = parent.$("#trmnum").val();
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkCostMappingWithGL",
                param1: costCode,
                param2: terminalNo,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data !== "") {
                    $.prompt("No gl account is mapped with these charge code.Please contact accounting -> <span style=color:red>" + data + "</span>.");
                    flag = false;
                }
            }
        });
        return flag;
    }
</script>
