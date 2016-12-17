$(document).ready(function () {
    var buttonval = $("#buttonval").val();
    var costAmount = $("#costAmount").val();
    $('#moduleName').val(parent.$('#moduleName').val());
    disableAutoChargeValues();
    $('#hiddenChargeCode').val($('#chargesCode').val());
    $('#hiddenBillToParty').val($('#billToParty').val());
    $('#hiddenChargeCode').focus();
    costAmountValidate(costAmount, buttonval);
    setUomFeilds();
    setDisableFeilds();
    if ($("#moduleName").val() === 'Exports') {
        setOptionForRate();
    } else {
        enableAutoIPIBtn();
    }
    setDisableCostFeilds();
    setChangeValues();
    setKeyDownValues();
    if (parent.$('#supplierNameOrg').val() === '') {
        $("#selectAgentAccount").hide();
        $("#agent").hide();
        $("#selectWareHouseAccount").hide();
        $("#CFSDev").hide();
    }
    if (($('#methodName').val() === 'editCharge') && ($('#thirdPartyname').val() !== "")
            && ($('#thirdPartyname').val() === parent.$('#supplierNameOrg').val())) {
        $("#selectAgentAccount").attr('checked', true);
    } else {
        $("#selectAgentAccount").attr('checked', false);
    }

    if (($('#methodName').val() === 'editCharge') && ($('#thirdPartyname').val() !== "")
            && ($('#thirdpartyaccountNo').val() === $('#cfAcctNo').val())) {
        $("#selectWareHouseAccount").attr('checked', true);
    } else {
        $("#selectWareHouseAccount").attr('checked', false);
    }
    $('#thirdPartyname').keyup(function () {
        if ($(this).val() === '') {
            $("#selectAgentAccount").attr('checked', false);
            $("#selectWareHouseAccount").attr('checked', false)
            $('#thirdpartyaccountNo').val('');
        }
    });
    if ($("#costStatus").val() === 'IP') {
        disableTextBox("costAmount");
        disableTextBox("thirdPartyname");
        disableTextBox("invoiceNumber");
    }
    if ($('#methodName').val() === 'editCharge' && $("#moduleName").val() === 'Exports') {
        disableFieldForAutoCharge();
        makeChargeFieldsReadOnly();
    }
    if ($("#costStatus").val() === 'DS') {
        disableTextBox("thirdPartyname");
        disableTextBox("invoiceNumber");
        $("#lastInvoice").attr("disabled", true);
    }
    if ($('#postAr').val() === 'false' && $('#billToParty').val() === 'W' && $('#methodName').val() === 'editCharge'
            && $("#moduleName").val() === 'Imports') {
        enableTextBox("flatRateAmount");
        enableTextBox("arAmount");
    }
});

function selectAgentAccountNo() {
    $("#selectWareHouseAccount").attr('checked', false);
    var checked = $("#selectAgentAccount").attr('checked') ? true : false;
    if (checked) {
        $('#thirdPartyname').val(parent.$('#supplierNameOrg').val());
        $('#thirdpartyaccountNo').val(parent.$('#supplierCode').val());
    }
    else {
        $('#thirdPartyname').val('');
        $('#thirdpartyaccountNo').val('');
    }
}

function selectWareHouseAccountNo() {
    $("#selectAgentAccount").attr('checked', false);
    var checked = $("#selectWareHouseAccount").attr('checked') ? true : false;
    var cfsWarehouseNo = parent.$('#cfsWarehouseNo').val();
    if (checked && cfsWarehouseNo != '' && cfsWarehouseNo != null) {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.WarehouseDAO",
                methodName: "getWarehouseAccountNo",
                param1: cfsWarehouseNo,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data[1] != null && data[1] != "") {
                    $('#thirdPartyname').val(data[0]);
                    $('#thirdpartyaccountNo').val(data[1]);
                }
                else {
                    $('#thirdPartyname').val('');
                    $('#thirdpartyaccountNo').val('');
                    $("#selectWareHouseAccount").attr('checked', false);
                    $.prompt("Warehouse does not have IPI vendor");
                }
            }
        });
    }
    else {
        $.prompt("CFS Devanning Whse is Required");
        $("#selectWareHouseAccount").attr('checked', false)
        $('#thirdPartyname').val('');
        $('#thirdpartyaccountNo').val('');
    }

}

function vendorTypeCheck() {
    if ($('#thirdpartyDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#thirdparyDisableAcct').val() + "</span>");
        $('#thirdpartyaccountNo').val('');
        $('#thirdPartyname').val('');
    }
    if ($('#thirdPartyname').val() === parent.$('#supplierNameOrg').val()) {
        $("#selectAgentAccount").attr('checked', true);
    }
}
function setChangeValues() {
    $('#thirdPartyname').change(function () {
        $('#thirdpartyaccountNo').val('');
    });
}
function setKeyDownValues() {
    $(document).keydown(function (e) {
        if ($(e.target).attr("readonly")) {
            if (e.keyCode === 8) {
                return false;
            }
        }
    });
}
function disableAutoChargeValues() {
    var manual = $('#manualEntry').val();
    if (manual === 'false' && $('#moduleName').val() === 'Imports') {
        var form = document.getElementById('lclCostAndChargeForm');
        var element;
        for (var i = 0; i < form.elements.length; i++) {
            element = form.elements[i];
            if ((element.type === "text" || element.type === "textarea") && (element.id !== "costAmount" && element.id !== "measureForCost" &&
                    element.id !== "weightForCost" && element.id !== "minimumForCost" && element.id !== "thirdPartyname" &&
                    element.id !== "invoiceNumber" && element.id !== "arAmount")) {
                element.style.backgroundColor = "#CCEBFF";
                element.readOnly = true;
                element.style.borderTop = "0px";
                element.style.borderBottom = "0px";
                element.style.borderRight = "0px";
                element.style.borderLeft = "0px solid";
            }
            if (element.type === "radio") {
                element.disabled = true;
            }
        }
    }
}

function getSellVendorValues(chargeCode) {
    var dataValue = "";
    $.ajaxx({
        dataType: "json",
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

function saveCharge(buttonVal, screen) {
    var fileNumberId = $("#fileNumberId").val();
    var moduleName = $('#moduleName').val();
    var lclBookingAcId = $('#lclBookingAcId').val();
    var count = new Array();
    parent.$(".chargeAmount").each(function () {
        count.push($(this).text().trim());
    });
    var bill = $('#hiddenBillToParty').length > 0 ? $('#hiddenBillToParty').val() : "";
    var chargesCode = $('#hiddenChargeCode').length > 0 ? $('#hiddenChargeCode').val() : "";
    var costAmount = $('#costAmount').val();
    var measureForCost = $('#measureForCost').val();
    var weightForCost = $('#weightForCost').val();
    var minimumForCost = $('#minimumForCost').val();
    var measure = $('#measure').val();
    var weight = $('#weight').val();
    var minimum = $('#minimum').val();
    var terminalNo = parent.$("#trmnum").val();
    var costCode = $("#chargesCode").val();
    if ($("#moduleName").val() !== "Imports") {
        var chargeFlag = false, costFlag = false;
        var methodName = $('#manualEntry').val() === 'true' ? 'addManualChargesForExport' : 'addCharges';
        $(".chargeAmt").each(function () {
            if ($(this).val() !== "" && $(this).val() !== "0.00") {
                chargeFlag = true;
            }
        });
        $(".costAmt").each(function () {
            if ($(this).val() !== "" && $(this).val() !== "0.00") {
                costFlag = true;
            }
        });
        if (chargesCode === null || chargesCode === "") {
            $.prompt("Code is required");
            $('#hiddenChargeCode').css("border-color", "red");
        } else {
            var data = getSellVendorValues(chargesCode);
            if (screen !== "addCost" && data[0].trim() === 'C' && chargeFlag) {
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
            if (screen !== "addCost" && bill === "" || bill === null) {
                $.prompt("Bill to Party is required");
                $("#hiddenBillToParty").css("border-color", "red");
                return false;
            }
            if (costFlag) {
                var originId = parent.$("#portOfOriginId").val();
                if (data[1].trim() !== 'Y' && data[0].trim() !== 'S' && $('#thirdPartyname').val() === "") {
                    $.prompt("Vendor is required");
                    $('#thirdPartyname').css("border-color", "red");
                    $('#thirdPartyname').focus();
                } else if (!validateCostGLAccount(costCode, originId)) {
                    clearCost();
                    return false;
                } else {
                    if (count.length > 11 && screen !== "addCost" && $("#methodName").val() !== 'editCharge') {
                        $.prompt("More than 12 charges is not allowed");
                    } else {
                        submitAjaxForm(methodName, '#lclCostAndChargeForm', '#chargeDesc', buttonVal, screen, chargesCode);
                    }
                }
            } else {
                if (count.length > 11 && screen !== "addCost" && $("#methodName").val() !== 'editCharge') {
                    $.prompt("More than 12 charges is not allowed");
                } else {
                    submitAjaxForm(methodName, '#lclCostAndChargeForm', '#chargeDesc', buttonVal, screen, chargesCode);
                }
            }
        }
    } else {
        if (chargesCode === null || chargesCode === "") {
            $.prompt("Code is required");
            $('#hiddenChargeCode').css("border-color", "red");
        } else if ((screen === "addCost") && (costAmount === null || costAmount === "" || costAmount === '0.00') &&
                ((weightForCost === null || weightForCost === "" || weightForCost === '0.00') ||
                        (measureForCost === null || measureForCost === "" || measureForCost === '0.00') ||
                        (minimumForCost === null || minimumForCost === "" || minimumForCost === '0.00'))) {
            $.prompt('Cost Amount is required and it should not be equal to zero');
            $('#costAmount').css("border-color", "red");
            if ((measureForCost === "" || measureForCost === '0.00') && (minimumForCost === "" || minimumForCost === '0.00') && (weightForCost === "" || weightForCost === '0.00')) {
                $('#measureForCost').css("border-color", "red");
            }
            else if ((measureForCost !== "" || measureForCost !== '0.00') && (weightForCost === "" || weightForCost === '0.00')) {
                $('#weightForCost').css("border-color", "red");
            }
            else if ((measureForCost !== "" || measureForCost !== '0.00') && (weightForCost !== "" || weightForCost !== '0.00')) {
                $('#minimumForCost').css("border-color", "red");
            }
        } else if (screen === "addCharge" && ($('#thirdPartyname').val() !== "" || $("#invoiceNumber").val() !== "") && ((costAmount === null || costAmount === "" || costAmount === '0.00') && ((weightForCost === null || weightForCost === "" || weightForCost === '0.00') || (measureForCost === null || measureForCost === "" || measureForCost === '0.00') || (minimumForCost === null || minimumForCost === "" || minimumForCost === '0.00')))) {
            $.prompt('Cost Amount is required and it should not be equal to zero');
            $('#costAmount').css("border-color", "red");
        }
        else if (screen !== "addCost" && bill === "" || bill === null) {
            $.prompt("Bill to Party is required");
            $("#hiddenBillToParty").css("border-color", "red");
        } else if ($('#thirdPartyname').val() === "" && costAmount !== "" && costAmount !== '0.00') {
            $.prompt("Vendor Name is required");
            $('#thirdPartyname').css("border-color", "red");
            $('#thirdPartyname').focus();
        } else {
            if (count.length >= 25 && screen !== "addCost" && $("#methodName").val() !== 'editCharge') {
                $.prompt("More than 25 charges is not allowed");
            } else {
                if (lclBookingAcId === null || lclBookingAcId === "") {
                    if (bill === 'A') {
                        var chargeCodeFlag = validateAgentInvoice(fileNumberId, chargesCode);
                        if (chargeCodeFlag) {
                            submitAjaxForm('addImpCharge', '#lclCostAndChargeForm', '#chargeDesc', buttonVal, screen, chargesCode);
                        } else {
                            $.prompt("<span style=color:red>" + chargesCode + '</span> Charge Code Already Exists and not yet posted');
                        }
                    } else {
                        $.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.logisoft.struts.action.lcl.LclCostAndChargeAction",
                                methodName: "checkChargeForBillToParty",
                                param1: chargesCode,
                                param2: bill,
                                param3: fileNumberId,
                                param4: moduleName,
                                dataType: "json"

                            },
                            async: false,
                            success: function (data) {
                                if (data === false) {
                                    $.prompt('Charge Already Exists. Please choose another bill to party.');
                                } else {
                                    if ($("#moduleName").val() !== "Imports" || ($("#moduleName").val() === "Imports" && checkAddCostMappingWithGL(costCode, terminalNo)))
                                        submitAjaxForm('addImpCharge', '#lclCostAndChargeForm', '#chargeDesc', buttonVal, screen, chargesCode);
                                }
                            }
                        });
                    }
                }
                else {
                    submitAjaxForm('addImpCharge', '#lclCostAndChargeForm', '#chargeDesc', buttonVal, screen, chargesCode);
                }

            }
        }
    }
}
function validateAgentInvoice(fileId, chargeCode) {
    var flag;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO",
            methodName: "isValidateAgentCharges",
            param1: fileId,
            param2: 'LCLI',
            param3: 'AR',
            param4: chargeCode,
            dataType: "json"

        },
        async: false,
        success: function (data) {
            if (data == null) {
                flag = true;
            } else {
                flag = data;
            }
        }
    });
    return flag;
}
function submitAjaxForm(methodName, formName, selector, buttonVal, screen, chargesCode) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    var fileNumberId = $('#fileNumberId').val();
    var fileNumberStatus = $('#fileNumberStatus').val();
    var billingType = parent.$("input:radio[name='pcBoth']:checked").val();
    var moduleName = $('#moduleName').val();
    params += "&fileNumberId=" + fileNumberId + "&billingType=" + billingType + "&fileNumberStatus=" + fileNumberStatus;
    params += "&moduleName" + moduleName + "&unitSsCollectType=" + parent.$('#unitCollect').val();
    $.post($(formName).attr("action"), params,
            function (data) {
                if (moduleName === 'Exports') {
                    pickUpEnable(chargesCode);
                }
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                if (buttonVal === "SM") {
                    $('#hiddenChargeCode').val('');
                    $('#arAmount').val('');
                    $('#hiddenBillToParty').val('');
                    $('#lastInvoiceNumber').val($('#invoiceNumber').val());
                    $('#lastVendorName').val($('#thirdPartyname').val());
                    $('#lastVendorNumber').val($('#thirdpartyaccountNo').val());
                    $('#lastInvoice').attr('checked', false);
                    $('#selectAgentAccount').attr('checked', false);
                    $("#selectWareHouseAccount").attr('checked', false);
                    clearCost();
                    clearCharge();
                    costAmountValidate('', screen);
                    //                enableFLChargeFeilds();
                    //                enableFLCostFeilds();
                }
                else if (buttonVal === "SE") {
                    setTimeout(function () {
                        parent.$.fn.colorbox.close();
                    }, 100);
                }
                hideProgressBar();
            });
}
function pickUpEnable(chargeCode) {
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

function setUomFeilds() {
    var buttonValue = $('#buttonValue').val();
    if ($('#metric').is(":checked")) {
        if (buttonValue === 'addCharge') {
            document.getElementById('wei').innerHTML = "/1000 KGS";
            document.getElementById('msr').innerHTML = "/CBM";
        }
        document.getElementById('wei1').innerHTML = "/1000 KGS";
        document.getElementById('msr1').innerHTML = "/CBM";
    }
    if ($('#imperial').is(":checked")) {
        if (buttonValue === 'addCharge') {
            document.getElementById('wei').innerHTML = "/100 LBS";
            document.getElementById('msr').innerHTML = "/CFT";
        }
        document.getElementById('wei1').innerHTML = "/100 LBS";
        document.getElementById('msr1').innerHTML = "/CFT";
    }
}

function clearCost() {
    $('#invoiceNumber').val('');
    $('#thirdpartyaccountNo').val('');
    $('#thirdPartyname').val('');
    $('#minimumForCost').val('');
    $('#weightForCost').val('');
    $('#measureForCost').val('');
    $('#costAmount').val('');
}

function clearCharge() {
    $('#minimum').val('');
    $('#weight').val('');
    $('#measure').val('');
    $('#flatRateAmount').val('');
}

function rateChargeReadonly() {
    var buttonValue = $('#buttonValue').val();
    if (buttonValue === 'addCharge') {
        var flatRate = $('#flatRateAmount').val();
        if (flatRate !== "") {
            disableWMChargeFeilds();
        } else {
            enableWMChargeFeilds();
        }
    }
}

function rateCostReadonly() {
    var costAmount = $('#costAmount').val();
    if (costAmount !== "") {
        disableWMCostFeilds();
    } else {
        enableWMCostFeilds();
    }
}

function uomReadonlyCharge() {
    var buttonValue = $('#buttonValue').val();
    if (buttonValue === 'addCharge') {
        var weight = $('#weight').val();
        var measure = $('#measure').val();
        var minimum = $('#minimum').val();
        if ((minimum !== "" && minimum !== "0.00") || (measure !== "" && measure !== "0.00") || (weight !== "" && weight !== "0.00")) {
            disableFLChargeFeilds();
        } else {
            if ($("#moduleName").val() !== "Imports") {
                enableFLChargeFeilds();
            }
        }
    }
}

function uomReadonlyCost() {
    var weightCost = $('#weightForCost').val();
    var measureCost = $('#measureForCost').val();
    var minimumCost = $('#minimumForCost').val();
    if ((minimumCost !== "" && minimumCost !== "0.00") || (measureCost !== "" && measureCost !== "0.00") || (weightCost !== "" && weightCost !== "0.00")) {
        disableFLCostFeilds();
    } else {
        if ($("#moduleName").val() !== "Imports") {
            enableFLCostFeilds();
        }
    }
}
function checkChargeCode(shipmentType) {
    var chargeCode = $('#hiddenChargeCode').val();
    var fileID = $('#fileNumberId').val();
    var buttonValue = $('#buttonValue').val();
    var moduleName = $("#moduleName").val();
    $('#chargesCode').val($('#hiddenChargeCode').val());
    if (chargeCode !== '') {
        if (moduleName !== "Imports") {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "chargeCodeValidate",
                    param1: chargeCode,
                    param2: fileID,
                    dataType: "json"
                },
                async: false,
                success: function (data) {
                    if (data === true) {
                        $("#hiddenChargeCode").val('');
                        $("#hiddenChargeCode").css("border-color", "red");
                        $.prompt("Charge Code is already exists.Please Select different Charge Code");
                    } else if (!parent.isDestinationCharge(chargeCode)) {
                        $("#hiddenChargeCode").val('');
                        $("#hiddenChargeCode").css("border-color", "red");
                        $.prompt("For <span class='red'>" + chargeCode + "</span> charge code, you need to use the Destination Services button.");
                        return false;
                    }
                }
            });
            setSelect();
            //setEnableDisableCost(chargeCode, shipmentType);
        } else if (buttonValue === 'addCharge') {
            var bill = $('#hiddenBillToParty').length > 0 ? $('#hiddenBillToParty').val() : "";
            if ("Imports" === moduleName && bill === 'A') {
                var chargeCodeFlag1 = validateAgentInvoice(fileID, chargeCode);
                if (!chargeCodeFlag1) {
                    $.prompt("<span style=color:red>" + chargeCode + '</span> Charge Code Already Exists and not yet posted');
                    $("#hiddenChargeCode").val($('#existChargeCode').val());
                    $('#chargesCode').val($('#existChargeCode').val());
                    $("#chargesCodeId").val($('#existChargeCodeId').val());
                    $("#hiddenBillToParty").val($('#existBillToParty').val());
                    return false;
                }
            }
            setEnableDisableCost(chargeCode, shipmentType);
        } else if (buttonValue === 'addCost') {
            checkCostCode(chargeCode, shipmentType);
        }
    }

}
function setEnableDisableCost(chargeCode, shipmentType) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "chargeCostValidate",
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
                disableTextBox("costAmount");
                disableTextBox("thirdPartyname");
                disableTextBox("invoiceNumber");
                $("#selectAgentAccount").hide();
                $('#agent').hide();
                $("#selectWareHouseAccount").hide();
                $("#CFSDev").hide();
                clearCost();
                $("#thirdPartyname").val('');
                $("#thirdpartyaccountNo").val('');
                $("#selectAgentAccount").attr('checked', false);
                $("#selectWareHouseAccount").attr('checked', false);
                disableTextBox("measureForCost");
                disableTextBox("weightForCost");
                disableTextBox("minimumForCost");
            } else {
                enableTextBox("costAmount");
            }
        }

    });
}
function checkCostCode(chargeCode, shipmentType) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "chargeCostValidate",
            param1: chargeCode,
            param2: shipmentType,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data[1] === 'N') {
                $.prompt("No cost code is mapped with this code.Please contact accounting.");
                $("#chargesCode").val('');
                clearCost();
            }
        }
    });
}

function validateBillToParty() {
    var forwarder = parent.$('#forwarderCodeClient').val();
    var shipper = parent.$('#shipperCodeClient').val();
    var thirdParty = parent.$("#thirdpartyaccountNo").val();
    var billToParty = $('#hiddenBillToParty').val();
    if (billToParty === 'T' && thirdParty === '') {
        $.prompt("Please Enter Third Party Account Name in Booking.");
        $('#hiddenBillToParty').val($('#billToParty').val());
        return false;
    } else if (billToParty === 'S' && shipper === '') {
        $.prompt("Please Enter Shipper Account Name in Booking.");
        $('#hiddenBillToParty').val($('#billToParty').val());
        return false;
    } else if (billToParty === 'F' && forwarder === '') {
        $.prompt("Please Enter Forwarder Account Name in Booking.");
        $('#hiddenBillToParty').val($('#billToParty').val());
        return false;
    }
    $('#billToParty').val($('#hiddenBillToParty').val());
}


function changeBillToParty() {
    var thirdToParty = parent.$('#thirdPartyname').val();
    var consParty = parent.$('#consigneeCode').val();
    var notifyParty = parent.$('#notifyCode').val();
    var cfsDevWarehouse = parent.$('#impCFSWareName').val();
    var billToParty = $('#hiddenBillToParty').val();
    if (billToParty == 'T' && thirdToParty === '') {
        $.prompt("Please Enter Third Party Account Name in Booking.");
        $('#hiddenBillToParty').val($('#billToParty').val());
        return;
    } else if (billToParty == 'C' && consParty === '') {
        $.prompt("Please Enter Consignee Account Name in Booking.");
        $('#hiddenBillToParty').val($('#billToParty').val());
        return;
    } else if (billToParty == 'N' && notifyParty === '') {
        $.prompt("Please Enter Notify Party Account Name in Booking.");
        $('#hiddenBillToParty').val($('#billToParty').val());
        return;
    } else if (billToParty == 'W' && cfsDevWarehouse === '') {
        $.prompt("CFS Devanning Whse is required");
        $('#hiddenBillToParty').val($('#billToParty').val());
        return;
    }
    var fileNumberId = $("#fileNumberId").val();
    var chargeCode = $('#hiddenChargeCode').val();
    var bill = $('#hiddenBillToParty').length > 0 ? $('#hiddenBillToParty').val() : "";
    var moduleName = $('#moduleName').val();
    if ("Imports" === moduleName && bill === 'A') {
        var chargeCodeFlag1 = validateAgentInvoice(fileNumberId, chargeCode);
        if (!chargeCodeFlag1) {
            $.prompt("<span style=color:red>" + chargeCode + '</span> Charge Code Already Exists and not yet posted');
            $("#hiddenChargeCode").val($('#existChargeCode').val());
            $('#chargesCode').val($('#existChargeCode').val());
            $("#chargesCodeId").val($('#existChargeCodeId').val());
            $("#hiddenBillToParty").val($('#existBillToParty').val());
            return false;
        }
    }
    $('#billToParty').val($('#hiddenBillToParty').val());
    var fileStatus = parent.$('#fileStatus').val();
    if (moduleName === 'Imports' && fileStatus === 'M' && billToParty !== 'A') {
        var txt = "Are you sure? You Want To change Bill To Party.";
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                Cancel: 2
            },
            submit: function (v) {
                if (v === 1) {
                    showProgressBar();
                    $("#methodName").val("createImpCorrection");
                    var params = $("#lclCostAndChargeForm").serialize();
                    var fileNumberId = $('#fileNumberId').val();
                    var moduleName = $('#moduleName').val();
                    params += "&fileNumberId=" + fileNumberId + "&moduleName=" + moduleName;
                    $.post($("#lclCostAndChargeForm").attr("action"), params,
                            function (data) {
                                $("#chargeDesc").html(data);
                                $("#chargeDesc", window.parent.document).html(data);
                                parent.$('#correctionNotice').addClass('green-background');
                                parent.$('#correctionNoticeBottom').addClass('green-background');
                                hideProgressBar();
                                $.prompt.close();
                                parent.$.fn.colorbox.close();
                            });
                }
                else if (v === 2) {
                    $('#hiddenBillToParty').val($('#billToPartyOldValue').val());
                    $('#billToParty').val($('#billToPartyOldValue').val());
                    $.prompt.close();
                }
            }
        });
    }
}
function tabFocusRestrictor(e) {
    if (e.which === 9) {
        $('#chg').focus();
        return false;
    }
}
function lastInvoiceNo() {
    if ($('#lastInvoice').is(":checked")) {
        $('#invoiceNumber').val($('#lastInvoiceNumber').val());
        $('#thirdPartyname').val($('#lastVendorName').val());
        $('#thirdpartyaccountNo').val($('#lastVendorNumber').val());
    } else {
        $('#invoiceNumber').val('');
        $('#thirdPartyname').val('');
        $('#thirdpartyaccountNo').val('');
    }
}
function setDisableFeilds() {
    var costStatus = $('#costStatus').val();
    var chargeInvoiceNumber = $('#chargeInvoiceNumber').val();
    if (costStatus !== "" && costStatus === "AP") {
        //        disableFLChargeFeilds();
        disableCostDetailFeilds();
        disableAutoCompletorById("hiddenChargeCode");
        $("#selectAgentAccount").hide();
        $("#agent").hide();
        $("#selectWareHouseAccount").hide();
        $("#CFSDev").hide();

    }
    if (costStatus !== 'AP' && chargeInvoiceNumber !== '') {
        disableTextBox('arAmount');
        disableFLChargeFeilds();
        disableAutoCompletorById("hiddenChargeCode");
        disableListBox("hiddenBillToParty");
    }
    if ($('#moduleName').val() === "Imports") {
        disableWMCostFeilds();
        disableWMChargeFeilds();
        disableFieldByName("uom", "radio");
        if (parent.$('#fileStatus').val() === 'M' && $('#hiddenBillToParty').val() !== 'undefined' && $('#hiddenChargeCode').val() !== ""
                && ($('#hiddenChargeCode').val() !== 'DOORDEL' || $('#hiddenBillToParty').val() !== 'A') && $("#buttonValue").val() !== "addCost") {
            disableAutoCompletorById("hiddenChargeCode");
            if ($('#hiddenBillToParty').val() !== 'A') {
                if ($('#manualEntry').val() === 'false') {
                    disableTextBox('arAmount');
                } else {
                    disableTextBox('flatRateAmount');
                }
            }
            disableListBox("hiddenBillToParty");
        }
    }
}
function setDisableCostFeilds() {
    if ($("#quoteAcId").val() !== "" && $('#disableCost').val() === "disableCostButton") {
        disableCostDetailFeilds();
    }
}
function disableCostDetailFeilds() {
    disableTextBox("costAmount");
    disableTextBox("thirdPartyname");
    disableTextBox("invoiceNumber");
    disableTextBox("measureForCost");
    disableTextBox("weightForCost");
    disableTextBox("minimumForCost");
    $("#lastInvoice").attr("disabled", true);
}
function disableWMCostFeilds() {
    disableTextBox("measureForCost");
    disableTextBox("weightForCost");
    disableTextBox("minimumForCost");
}
function enableWMCostFeilds() {
    enableTextBox("measureForCost");
    enableTextBox("weightForCost");
    enableTextBox("minimumForCost");
}
function disableWMChargeFeilds() {
    disableTextBox("measure");
    disableTextBox("weight");
    disableTextBox("minimum");
}
function enableWMChargeFeilds() {
    enableTextBox("measure");
    enableTextBox("weight");
    enableTextBox("minimum");
}
function disableFLChargeFeilds() {
    disableTextBox("flatRateAmount");
}
function enableFLChargeFeilds() {
    enableTextBox("flatRateAmount");
}
function disableFLCostFeilds() {
    disableTextBox("costAmount");
}
function enableFLCostFeilds() {
    enableTextBox("costAmount");
}
function disableTextBox(id) {
    $('#' + id).addClass('textlabelsBoldForTextBoxDisabledLook');
    $('#' + id).attr('readonly', true);
    $('#' + id).attr('tabindex', -1);
}
function disableListBox(id) {
    $('#' + id).addClass('textlabelsBoldForTextBoxDisabledLook');
    $('#' + id).attr('readonly', true);
    $('#' + id).attr('disabled', true);
    $('#' + id).attr('tabindex', -1);
}
function enableTextBox(id) {
    $('#' + id).removeClass('textlabelsBoldForTextBoxDisabledLook');
    $('#' + id).addClass('textlabelsBoldForTextBox');
    $('#' + id).attr('readonly', false);
    $("#" + id).css("background", "");
    $('#' + id).attr('tabindex', 0);
}
function disableFieldByName(selector, type) {
    $('input:' + type + '[name=' + selector + ']').each(function () {
        $(this).attr("disabled", true);
    });
}

function disableAutoCompletorById(selector) {
    var $selector = $('#' + selector);
    $selector.addClass('text-readonly');
    $selector.attr('readonly', true);
    $selector.attr('disabled', true);
    $selector.attr('tabindex', -1);
}
function checkForNumberAndDecimal(obj) {
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This field should be Numeric");
    }
}
function allowNegativeNumbers(obj) {
    if (!/^-?\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This field should be Numeric");
    }
}
function showClientSearchOption(path, searchByValue) {
    var href = path + "/lclBooking.do?methodName=clientSearch&searchByValue=" + searchByValue;
    $(".clientSearchEdit").attr("href", href);
    $(".clientSearchEdit").colorbox({
        iframe: true,
        width: "45%",
        height: "73%",
        title: searchByValue + " Search"
    });
}

function costAmountValidate(id, buttonValue) {
    var costAmount = $(id).val() !== undefined ? $(id).val() : id;
    var measureForCost = $(id).val() !== undefined ? $(id).val() : id;
    if ((buttonValue === 'addCharge' && buttonValue !== 'addCost') && ((costAmount === null || costAmount === "" || costAmount === '0' || costAmount === '0.00') || (measureForCost === null || measureForCost === "" || measureForCost === '0' || measureForCost === '0.00'))) {
        disableTextBox("thirdPartyname");
        disableTextBox("thirdpartyaccountNo");
        disableTextBox("invoiceNumber");
        $('#lastInvoice').hide();
        $("#selectAgentAccount").hide();
        $('#agent').hide();
        $("#selectWareHouseAccount").hide();
        $("#selectAgentAccount").attr("checked", false);
        $('#CFSDev').hide();
    } else {
        enableTextBox("thirdPartyname");
//        enableTextBox("thirdpartyaccountNo");
        enableTextBox("invoiceNumber");
        $("#selectAgentAccount").show();
        $('#lastInvoice').show();
        $('#agent').show();
        $("#selectWareHouseAccount").show();
        $('#CFSDev').show();
    }
    if (($("#costStatus").val() === 'DS') && (costAmount === null || costAmount === "" || costAmount !== '0' || costAmount !== '0.00' || costAmount === '0.00')) {
        disableTextBox("thirdPartyname");
        disableTextBox("thirdpartyaccountNo");
        disableTextBox("invoiceNumber");
        $('#lastInvoice').hide();
        $("#selectAgentAccount").hide();
        $("#selectAgentAccount").attr("checked", false);
        $('#agent').hide();
    }
}

function disableFieldForAutoCharge() {
    var billCode = parent.$("input:radio[name=pcBoth]:checked").val();
    var form = document.getElementById("lclCostAndChargeForm");
    var element;
    var cost = $("#costAmount").val();
    if ($("#manualEntry").val() === 'false') {
        for (var e = 0; e < form.elements.length; e++) {
            element = form.elements[e];
            if (element.type === 'text') {
                element.style.backgroundColor = "#CCEBFF";
                element.readOnly = true;
            }
            if (cost !== '' && element.id === 'costAmount') {
                $("#" + element.id).removeAttr("readOnly");
                $("#" + element.id).css("background", "");
            }
            if (element.type === "radio") {
                element.style.border = 0;
                element.disabled = true;
            }
            if (element.type === 'select-one' && billCode === 'C') {
                element.disabled = true;
                $("#hiddenBillToParty").val($("#billToParty").val());
            } else {
                $("#hiddenBillToParty").val($("#billToParty").val());
            }
        }
    }
}


function disableClassText(ele) {
    $('.' + ele).addClass('textlabelsBoldForTextBoxDisabledLook');
    $('.' + ele).attr('readonly', true);
    $('.' + ele).attr('tabindex', -1);
    $("." + ele).val('');
}

function enableClassText(ele) {
    $('.' + ele).removeClass('textlabelsBoldForTextBoxDisabledLook');
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
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.lcl.bc.LclBookingUtils",
            methodName: "validateForChargeandCost",
            param1: chargeCode,
            param2: "LCLE",
            dataType: "json"
        },
        async: false,
        success: function (data) {
            value = data;
        }
    });
    return value;
}

function checkAddCostMappingWithGL(costCode, terminalNo) {
    var flag = true;
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
function enableAutoIPIBtn() {
    var podUnLocCode = parent.$("#podUnlocationcode").val();
    var fdUnlocCode = parent.$("#unlocationCode").val();
    var transShipMent = parent.$('input:radio[name=transShipMent]:checked').val();
    if (podUnLocCode !== fdUnlocCode && transShipMent === 'N') {
        $("#autoIpi").show();
    } else {
        $("#autoIpi").hide();
    }
}
function getAutoIPICost() {
    var fileNumberId = $("#fileNumberId").val();
    var impCfsWarehsId = parent.$("#impCfsWarehsId").val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getAutoIPICost",
            param1: fileNumberId,
            param2: impCfsWarehsId,
            param3: parent.$("#podUnlocationcode").val(),
            param4: parent.$("#unlocationCode").val(),
            param5: parent.$('input:radio[name=transShipMent]:checked').val(),
            request: "true",
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data !== null && data[1] !== null && data[1] !== "") {
                $("#hiddenChargeCode").val(data[0]);
                $("#costAmount").val(data[1]);
                $("#chargesCodeId").val(data[2]);
                $("#chargesCode").val(data[0]);
            } else {
                $.prompt("IPI Auto cost not found");
            }
        }
    });
}

function validateCostGLAccount(chargeId, originId) {
    var flag = true;
    var bookedHeader = parent.$("#eciVoyage").val();
    var pickedHeader = parent.$("#pickedOnVoyageNo").val();
    var voyScheduleNo = pickedHeader === '' ? bookedHeader : pickedHeader;
    var voyOriginId = "";
    if (voyScheduleNo !== '') {
        voyOriginId = parent.getVoyageOriginId(voyScheduleNo);
    } else {
        voyOriginId = parent.$("#portOfLoadingId").val();
    }
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO",
            methodName: "validateLclExportGlAccount",
            param1: chargeId,
            param2: originId,
            param3: parent.$("#trmnum").val(),
            param4: voyOriginId,
            param5: "AC",
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
function makeChargeFieldsReadOnly() {
    var voyageClosedBy = parent.$("#voyageClosedBy").val();
    var blUnitCob = parent.$("#blUnitCob").val();
    var fileNumberStatus = $('#fileNumberStatus').val();
    if ((blUnitCob == 'true' || fileNumberStatus == 'M') && voyageClosedBy == '') {
        disableFLChargeFeilds();
        disableTextBox("arAmount");
        disableWMChargeFeilds();
        disableListBox("hiddenBillToParty");
        disableAutoCompletorById("hiddenChargeCode");
        disableFieldByName("rateOption", "radio");
        enableWMCostFeilds();
    }
}