jQuery(document).ready(function () {
    showpickUpReadonly();
    if ($('#moduleName').val() === 'Exports') {
        readyDefaultDate();
        newcompanyTruckerCheck();
    } else {
        readonlyChargeBox();
    }
});
function readyDefaultDate() {
    var m_names = new Array("Jan", "Feb", "Mar",
            "Apr", "May", "Jun", "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec");
    var d = new Date();
    var curr_date = d.getDate();
    var curr_month = d.getMonth();
    var curr_year = d.getFullYear();
    d.setDate(d.getDate() - 30);
    if ($("#pickupReadyDate").val() === null || $("#pickupReadyDate").val() === '') {
        $("#pickupReadyDate").val(curr_date + "-" + m_names[curr_month] + "-" + curr_year);
    }
}

function showpickUpReadonly() {
    var quoteComplete = parent.$('input:radio[name=quoteComplete]:checked').val();
    var form = document.getElementById('lclQuotePickupInfoForm');
    if (quoteComplete === 'Y') {
        var element;
        $("#savePickup").hide();
        $("#carrierRates").hide();
        $("#clearCharge").hide();
        $('#pickupCutoffDate').attr('disabled', true);
        $('#pickupReadyDate').attr('disabled', true);
        for (var i = 0; i < form.elements.length; i++) {
            element = form.elements[i];
            if (element.type === "text" || element.type === "textarea") {
                element.style.backgroundColor = "#CCEBFF";
                element.readOnly = true;
                element.style.borderTop = "0px";
                element.style.borderBottom = "0px";
                element.style.borderRight = "0px";
                element.style.borderLeft = "0px solid";
            }
            if (element.type === "checkbox" || element.type === "select-one") {
                element.style.border = 0;
                element.disabled = true;
            }
        }
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            if (imgs[k].id === "viewgif" || imgs[k].id === "viewgif1" || imgs[k].id === "viewgif2") {
                imgs[k].style.visibility = "visible";
            }
        }
    }
}
function callCarrierRates(whseZip, pickupReadyDate, fileId, moduleName) {
    var ipiCfsZip = "";
    var fdUnlocation = "";
    var fdUnlocationcode = "";
    if ($("#pickupReadyDate").val() === null || $("#pickupReadyDate").val() === "") {
        $("#pickupReadyDate").css("border-color", "red");
        $.prompt("Please select Ready Date");
        return false;
    } else if ($('#whseZip').val() === null || $('#whseZip').val() === "") {
        $("#whseZip").css("border-color", "red");
        $("#whsecompanyName").css("border-color", "red");
        $.prompt("Please select Warehouse Details");
        return false;
    } else {
        showProgressBar();
        var fromZip = "";
        var toZip = "";
        if (moduleName === "Imports") {
            ipiCfsZip = parent.$("#ipiCfsZip").val();
            fdUnlocation = parent.$('#finalDestinationR').val();
            fdUnlocationcode = fdUnlocation.substring(fdUnlocation.indexOf("(") + 1, fdUnlocation.indexOf(")"));
            fdUnlocationcode = fdUnlocationcode.substring(0, 2);
            if ((ipiCfsZip !== "" && ipiCfsZip !== undefined) && fdUnlocationcode === "US") {
                fromZip = ipiCfsZip;
                toZip = getFromZip();
            } else {
                fromZip = whseZip;
                toZip = getFromZip();
            }
        } else {
            submitForm('savePickupInfo');
            fromZip = getFromZip();
            toZip = whseZip;
        }
        $("#methodName").val("validateCarrierRates");
        var params = $("#lclQuotePickupInfoForm").serialize();
        params += "&fromZip=" + fromZip + "&pickupReadyDate=" + pickupReadyDate + "&toZip=" + toZip;
        $.post($("#lclQuotePickupInfoForm").attr("action"), params, function (data) {
            if (data !== 'null') {
                hideProgressBar();
                $.prompt(data);
            } else {
                hideProgressBar();
                var href = "lclQuotePickupInfo.do?methodName=carrier&toZip=" + toZip + "&pickupReadyDate=" + pickupReadyDate + "&fileNumberId=" + fileId + "&fromZip=" + fromZip + "&moduleName=" + moduleName;
                $.colorbox({
                    iframe: true,
                    width: "80%",
                    height: "80%",
                    href: href,
                    title: "Carrier Rates",
                    onClosed: function () {
                        $("#methodName").val("display");
                        $("#lclQuotePickupInfoForm").submit();
                    }
                });
            }
        });
    }
}
function enableDoorDeliveryETA() {
    var doorDeliveryStatus = $("#doorDeliveryStatus").val();
    if (doorDeliveryStatus == 'P') {
        $("#doorDeliveryEta").val('');
        $("#doorDelivery").hide();
        $("#doorDeliveryEta").hide();
    } else {
        $("#doorDeliveryEta").show();
        $("#doorDelivery").show();
    }
}
function newcompanyName() {
    if ($('#newCompanyName').is(":checked")) {
        $("#manualShipp").show();
        $("#dojoPickUpShipper").hide();
        $("#shipSupplier").val('');
        $("#shipperAccountNo").val('');
        $("#address").val('');
        $("#zip").val('');
        $("#shipperCity").val('');
        $("#shipperState").val('');
        $('#newCompanyName').attr('checked', true);
        $('#dupShipper').attr('checked', false);
    } else {
        $("#dojoPickUpShipper").show();
        $("#manualShipp").hide();
        $("#dupShipName").val('');
        $("#address").val('');
        $("#zip").val('');
        $("#shipperCity").val('');
        $("#shipperState").val('');
        $('#newCompanyName').attr('checked', false);
    }
}
function newcompanyTruckerCheck() {
    if ($('#newCompanyName').is(":checked")) {
        $("#manualShipp").show();
        $("#dojoPickUpShipper").hide();
    } else {
        $("#dojoPickUpShipper").show();
        $("#manualShipp").hide();
    }
    if ($('#manualShipper').is(":checked")) {
        $("#manualVendor").show();
        $("#dojoVendor").hide();
    }
    else {
        $("#dojoVendor").show();
        $("#manualVendor").hide();
    }
}
function showShipSupplierSearchOption(path, searchByValue) {
    var href = path + "/lclQuotePickupInfo.do?methodName=ShipperSearch&searchByValue=" + searchByValue;
    $(".clientSearchEdit").attr("href", href);
    $(".clientSearchEdit").colorbox({
        iframe: true,
        width: "35%",
        height: "51%",
        title: searchByValue + " Search"
    });
}
function shipSupplierAcctType() {
    var acctType = $("#shipper_acct_type").val();
    if ($('#shipSupDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#shipSupDisableAcct').val() + "</span>");
        $('#shipSupplier').val('');
        $('#address').val('');
        $('#zip').val('');
    }
}
function getFromZip() {
    var fromZip = '';
    var originCityZip = $("#cityStateZip").val();
    if (originCityZip.indexOf("-") > -1) {
        fromZip = originCityZip.substr(0, originCityZip.indexOf("-"));
    }
    return fromZip;
}
function getShipToGoogleMap(path, methodName) {
    var shipSupplier = $("#shipSupplier").val();
    if (shipSupplier == null || shipSupplier == "") {
        $.prompt("Shipper Account is required");
        $("#shipSupplier").css("border-color", "red");
    } else {
        shipSupplierUnloc = $("#shipToUnlocationCode").val();
        city = $("#shipperCity").val();
        state = $("#shipperState").val();
        country = $("#country").val();
        if ((shipSupplierUnloc == "" || shipSupplierUnloc == null) && (city == "" || city == null)) {
            $.prompt("Shipper UnlocationCode does not exist");
            return false;
        }
        var href = path + "/lclServiceMap.do?methodName=" + methodName + "&countryName=" + shipSupplierUnloc + "&city=" + city + "&state=" + state + "&country=" + country + "&address=" + $("#address").val().replace(/(?:\r\n|\r|\n)/g, ", ");
        $("#lclOriginMap").attr("href", href);
        $("#lclOriginMap").colorbox({
            iframe: true,
            width: "100%",
            height: "95%",
            title: "Origin Map"
        });
    }
}
function readonlyChargeBox() {
    var pickup = $('#chargeAmount').val();
    if (pickup !== "") {
        $('#costVendorAcct').removeClass('textlabelsBoldForTextBoxDisabledLook');
        $('#costVendorAcct').removeAttr("readonly");
        $('#chargeAmount').addClass('textlabelsBoldForTextBoxDisabledLook');
        $('#chargeAmount').attr("readonly", true);
    } else {
        $('#costVendorAcct').addClass('textlabelsBoldForTextBoxDisabledLook');
        $('#costVendorAcct').attr("readonly", true);
    }
}
function enableVendor() {
    var pickupCost = $('#pickupCost').val();
    if (pickupCost === '' || pickupCost === '0') {
        disableTextBox('costVendorAcct');
    } else {
        enableTextBox('costVendorAcct');
    }
}

function disableTextBox(selector) {
    $('#' + selector).addClass('textlabelsBoldForTextBoxDisabledLook');
    $('#' + selector).attr("readonly", true);
}
function enableTextBox(selector) {
    $('#' + selector).removeClass('textlabelsBoldForTextBoxDisabledLook');
    $('#' + selector).removeAttr("readonly");
}
function acctTypeCheck() {
    var acctType = $('#acctType').val();
    var disabledAcct = $('#costDisabled').val();
    if (disabledAcct === 'Y') {
        var msg = "This customer is disabled";
        if ($.trim($('#forwarderAcct').val()) !== "") {
            msg += " and merged with <span style=color:red>" + $('#forwarderAcct').val() + "</span>";
        }
        $.prompt(msg);
        clearVendorFeilds();
    } else if (acctType !== "" && acctType.indexOf("V") === -1) {
        $.prompt("Please select the customers with account type V");
        clearVendorFeilds();
    }
}
function clearVendorFeilds() {
    $('#costVendorNo').val('');
    $('#costVendorAcct').val('');
    $('#acctType').val('');
    $('#costDisabled').val('');
    $('#forwarderAcct').val('');
}

function submitForm(methodName) {
    if (($('#pickupCost').val() !== "" && $('#pickupCost').val() !== null && $('#pickupCost').val() !== '0.00') && ($('#chargeAmount').val() !== "" && $('#chargeAmount').val() !== null && $('#chargeAmount').val() !== '0.00')
            && $('#moduleName').val() === 'Exports') {
        var pickupCost = Math.round($('#pickupCost').val());
        var pickupCharge = Math.round($('#chargeAmount').val());
        if (methodName == "save") {
            if (pickupCost > pickupCharge) {
                $.prompt("Pickup cost cannot be greater than pickupcharge");
                return false;
            } else if ($('#costVendorAcct').val() === "" || $('#costVendorAcct').val() === null) {
                $.prompt("Vendor Name is required");
                $("#costVendorAcct").css("border-color", "red");
                $("#costVendorAcct").show();
                return false;
            }
        }
    }
    if (methodName == "save") {
        if ($('#apGlMappingFlagId').val() === "N" && ($('#pickupCost').val() !== "" && $('#pickupCost').val() !== '0.00')) {
            $.prompt('No Gl Mapping found for CostCode ---> INLAND');
            $('#pickupCost').val('');
            return false;
        } else if ($('#arGlMappingFlagId').val() === "N" && ($('#chargeAmount').val() !== "" && $('#chargeAmount').val() !== '0.00')) {
            $.prompt('No Gl Mapping found for ChargeCode ---> INLAND');
            $('#chargeAmount').val('');
            return false;
        }
    }
    showProgressBar();
    document.lclQuotePickupInfoForm.methodName.value = methodName;
    submitAjaxFormForPickup(methodName, '#lclQuotePickupInfoForm', '#chargeDesc');
}

function submitAjaxFormForPickup(methodName, formName, selector) {
    if (methodName == "save") {
        $("#methodName").val(methodName);
        var params = $(formName).serialize();
        $.post($(formName).attr("action"), params,
                function (data) {
                    $(selector).html(data);
                    $(selector, window.parent.document).html(data);
                    parent.$.fn.colorbox.close();
                });
    } else if (methodName === 'savePickupInfo') {
        $("#methodName").val("save");
        var params = $(formName).serialize();
        $.post($(formName).attr("action"), params,
                function (data) {
                    $(selector).html(data);
                    $(selector, window.parent.document).html(data);
                });
    }
}



function submitPickUpinfoQuote(methodName, moduleName) {
    var QuoteZip = $(".OrginZip").text().split("-");
    var Zip = $("#zip").val();
    if (moduleName === 'Exports') {
        if (QuoteZip[0] !== '' && Zip !== '' && QuoteZip[0] !== Zip) {
            $.prompt("The Zip code on the booking screen must match the Zip code on the dispatch. Please correct and obtain the new rates if necessary.", {
                buttons: {
                    OK: 1,
                    Cancel: 2,
                },
                submit: function (v) {
                    if (v === 1) {
                        $("#pickUpInfo").val(methodName === "save" ? "true" : "false");
                        submitAjaxFormQuote(methodName, moduleName);
                    }
                }
            });
        } else {
            submitAjaxFormQuote(methodName, moduleName);

        }
    } else {
        submitAjaxFormQuote(methodName, moduleName);
    }
}

function submitAjaxFormQuote(methodName, moduleName) {
    if (methodName === 'save') {
        submitForm(methodName);
    }
    else if (methodName === 'callCts') {
        callCarrierRates($('#whseZip').val(), $('#pickupReadyDate').val(), $('#fileNumberId').val(), moduleName);

    }
}