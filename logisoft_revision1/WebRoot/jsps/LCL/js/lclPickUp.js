jQuery(document).ready(function () {
    if ($('#moduleName').val() === 'Exports') {
        updateDeliverToWarehouse();
//        exportsOnload();
        readyDefaultDate();
        newcompanyTruckerCheck();
        if ($('#dupShipper').is(":checked")) {
            copyShipper();
        }
    } else {
        importsOnload();
        showShipperToDetails();
    }
    if (parent.$("#lockMessage").val() !== '' && parent.$("#lockMessage").val() !== null) {
        $(".disabledButton").hide();
    }

    readonlyChargeBox();

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

function importsOnload() {
    enableDoorDeliveryETA();
    if ($('#whsecompanyName').val() === null || $('#whsecompanyName').val() === '') {
        var FD = parent.$('#unlocationCode').val();
        var POD = parent.$('#podUnlocationcode').val();
        if (FD !== POD) {
            var whseName = parent.document.getElementById("stGeorgeAccount").value;
            var whseAddress = parent.document.getElementById("ipiCfsSearchAdd").value;
            var whseState = parent.document.getElementById("ipiCfsSearchState").value;
            var whsePhone = parent.document.getElementById("ipiCfsSearchPhone").value;
            var whseFax = parent.document.getElementById("ipiCfsSearchFax").value;
            var whseCity = parent.document.getElementById("ipiCfsSearchCity").value;
            var whseZip = parent.document.getElementById("ipiCfsSearchZip").value;
            $('#whsecompanyName').val(whseName);
            $('#whseAddress').val(whseAddress);
            $('#whsecity').val(whseCity);
            $('#whseState').val(whseState);
            $('#whseZip').val(whseZip);
            $('#whsePhone').val(whsePhone);
            $('#whseFax').val(whseFax);
        }
        if (FD === POD && $('#unitId').val() !== '') {
            $('#whsecompanyName').val(parent.$('#impCFSWareName').val() + "-" + parent.$('#cfsWarehouseNo').val());
            $('#whseAddress').val(parent.$('#cfsWarehouseAddress').val());
            $('#whsecity').val(parent.$('#cfsWarehouseCity').val());
            $('#whseState').val(parent.$('#cfsWarehouseState').val());
            $('#whseZip').val(parent.$('#cfsWarehouseZip').val());
            $('#whsePhone').val(parent.$('#cfsWarehousePhone').val());
            $('#whseFax').val(parent.$('#cfsWarehouseFax').val());
            $('#whseId').val(parent.$('#impCfsWarehsId').val());
            // }
            // });
        }
    }
}
function exportsOnload() {
    if ($('#whsecompanyName').val() === null || $('#whsecompanyName').val() === '') {
        var originUnlocationCode = parent.$('#originUnlocationCode').val();
        whseDetails(originUnlocationCode);
    }
}
function whseDetails(originUnlocationCode) {
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getdeliverCargoDetailsForWhse",
            param1: originUnlocationCode,
            dataType: "json"
        },
        success: function (data) {
            if (data !== undefined && data !== "" && data !== null) {
                $('#whsecompanyName').val(data[0]);
                $('#whseAddress').val(data[1]);
                $('#whsecity').val(data[2]);
                $('#whseState').val(data[3]);
                $('#whseZip').val(data[4]);
                $('#whsePhone').val(data[5])
            }
        }
    });
}
function readonlyChargeBox() {
    var pickup = $('#chargeAmount').val();
    var moduleName = $('#moduleName').val();
    if (moduleName === 'Imports') {
        if (pickup !== "") {
            $('#chargeAmount').addClass('textlabelsBoldForTextBoxDisabledLook');
            $('#chargeAmount').attr("readonly", true);
        }
        enableVendor();
    }
}
function enableVendor() {
    var pickupCost = $('#pickupCost').val();
    if (pickupCost === '' || pickupCost === '0.00') {
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
function callCarrierRates(whseZip, pickupReadyDate, fileId, moduleName, fileNumberStatus, invoiceStatus, transType, billToParty) {
    var ipiCfsZip = "";
    var fdUnlocation = "";
    var fdUnlocationcode = "";
    if ($("#pickupReadyDate").val() === null || $("#pickupReadyDate").val() === "") {
        $("#pickupReadyDate").css("border-color", "red");
        $.prompt("Please select Ready Date");
        return false;
    }
    else if ($('#whseZip').val() === null || $('#whseZip').val() === "") {
        $("#whseZip").css("border-color", "red");
        $("#whsecompanyName").css("border-color", "red");
        $.prompt("Please select Warehouse Details");
        return false;
    } else {
        showLoading();
        var fromZip = "";
        var toZip = "";
        if (moduleName === "Imports") {
            ipiCfsZip = parent.$("#ipiCfsSearchZip").val();
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
            submitForm('savePickupInfo', 'N');
            fromZip = getFromZip();
            toZip = whseZip;
        }
        $("#methodName").val("validateCarrierRates");
        var params = $("#lclPickupInfoForm").serialize();
        params += "&fromZip=" + fromZip + "&pickupReadyDate=" + pickupReadyDate + "&toZip=" + toZip;
        $.post($("#lclPickupInfoForm").attr("action"), params, function (data) {
            if (data !== 'null') {
                closePreloading();
                $.prompt(data);
                return false;
            } else {
                closePreloading();
                var href = "lclPickupInfo.do?methodName=carrier&toZip=" + toZip + "&pickupReadyDate=" + pickupReadyDate
                        + "&fileNumberId=" + fileId + "&fromZip=" + fromZip + "&fileNumberStatus=" + fileNumberStatus
                        + "&invoiceStatus=" + invoiceStatus + "&transType=" + transType + "&billToParty="
                        + billToParty + "&moduleName=" + moduleName;
                $.colorbox({
                    iframe: true,
                    width: "80%",
                    height: "80%",
                    href: href,
                    title: "Carrier Rates",
                    onClosed: function () {
                        if (moduleName === "Exports") {
                            $("#methodName").val("display");
                            $("#lclPickupInfoForm").submit();
                        }
                    }
                });
            }
        });
    }
}
function conformboxEdi(moduleName) {
    if ($("#lastFreeDate").val() != "") {
        $.prompt("Sure you want to send EDI to CTS ?", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    sendEDI(moduleName);
                    $.prompt.close();
                }
                else if (v === 2) {
                    $.prompt.close();
                }
            }
        });
    } else {
        $.prompt("Last Free Date is Required");
    }
}
function sendEDI(moduleName) {
    showLoading();
    parent.$('#doorOriginCityZip').val($('#cityStateZip').val());
    $("#methodName").val('save');
    $("#sendEdiToCtsFlag").val('true');
    $("#lclPickupInfoForm").submit();
}
function displayEdiHistory(id, fileId, path) {
    var href = path + "/lclRemarks.do?methodName=sendEdiToCtsList&fileNumberId=" + fileId + "&histEdi=" + id;
    $("#histEdi").attr("href", href);
    $("#histEdi").colorbox({
        iframe: true,
        width: "70%",
        height: "60%",
        title: "CTS EDI History"
    });
}
function getFromZip() {
    var fromZip = '';
    var originCityZip = $("#cityStateZip").val();
    if (originCityZip.indexOf("-") > -1) {
        fromZip = originCityZip.substr(0, originCityZip.indexOf("-"));
    }
    return fromZip;
}

function editCharge(txt) {
    var pickup = $('#chargeAmount').val();
    var pickupCost = $('#pickupCost').val();
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                $('#clearCharge').hide();
                $('#costVendorAcct').addClass('textlabelsBoldForTextBoxDisabledLook');
                $('#costVendorAcct').attr("readonly", true);
                $('#chargeAmount').removeClass('textlabelsBoldForTextBoxDisabledLook');
                $('#chargeAmount').removeAttr("readOnly");
                $('#chargeAmount').addClass("textlabelsBoldForTextBox");
                $.prompt.close();
                hideProgressBar();
            }
            else if (v === 2) {
                $('#chargeAmount').val(pickup);
                $('#pickupCost').val(pickupCost);
                $.prompt.close();
            }
        }
    });
}
function copyShipper() {//Exports Only
    var shipName = parent.$('#shipperNameClient').val();
    var dupShipperName = parent.$('#dupShipName').val();
    var shipAccount = parent.$('#shipperCode').val();
    var shipaddress = parent.$('#shipperAddress').val();
    var shipCity = parent.$('#shipperCity').val();
    var shipState = parent.$('#shipperState').val();
    var shipZip = parent.$('#shipperZip').val();
    var shipPhone = parent.$('#shipperPhone').val();
    var shipFax = parent.$('#shipperFax').val();
    var shipContactName = parent.$('#shipperContactClient').val();
    var shipEmail = parent.$('#shipperEmail').val();
    if ($('#dupShipper').is(":checked")) {
        if (shipName != "") {
            $("#dojoPickUpShipper").show();
            $("#manualShipp").hide();
            $("#dupShipName").val('');
            $('#shipSupplier').val(shipName);
            $("#shipperAccountNo").val(shipAccount);
            // $('#toAccountNo').val(shipAccount);
            $('#newCompanyName').attr('checked', false);
        } else if (dupShipperName != "") {
            $("#manualShipp").show();
            $("#dojoPickUpShipper").hide();
            $('#shipSupplier').val('');
            $("#shipperAccountNo").val('');
            $("#dupShipName").val(dupShipperName);
            $('#newCompanyName').attr('checked', true);
        } else {
            $("#dupShipName").val("");
            $('#newCompanyName').attr('checked', false);
        }
        $('#address').val(shipaddress);
        $('#city').val(shipCity);
        $('#zip').val(shipZip);
        $('#state').val(shipState);
        $('#phone1').val(shipPhone);
        $('#contactName').val(shipContactName);
        $('#fax1').val(shipFax);
        $('#email1').val(shipEmail);
        $('#manualShipper').attr("checked", false);
        $('#tempShipper').val("");
        $('#dojoTPShipper').show();
        $('#shipperManual').hide();
    } else {
        $('#shipSupplier').val("");
        $('#shipperAccountNo').val("");
        $('#toAccountNo').val("");
        $("#dupShipName").val("");
        $('#address').val("");
        $('#city').val("");
        $('#zip').val("");
        $('#state').val("");
        $('#phone1').val("");
        $('#contactName').val("");
        $('#fax1').val("");
        $('#email1').val("");
        $('#tempShipper').val("");
        $('#dojoTPShipper').show();
        $('#shipperManual').hide();
        $("#dojoPickUpShipper").show();
        $("#manualShipp").hide();
        $('#newCompanyName').attr('checked', false);
    }
}
function newcompanyName() {
    if ($('#newCompanyName').is(":checked")) {
        $("#manualShipp").show();
        $("#dojoPickUpShipper").hide();
        $("#shipSupplier").val('');
        $("#shipperAccountNo").val('');
        $("#address").val('');
        $('#address').val("");
        $('#city').val("");
        $('#zip').val("");
        $('#state').val("");
        $('#newCompanyName').attr('checked', true);
        $('#dupShipper').attr('checked', false);
    } else {
        $("#dojoPickUpShipper").show();
        $("#manualShipp").hide();
        $("#dupShipName").val('');
        $("#address").val('');
        $('#city').val("");
        $('#zip').val("");
        $('#state').val("");
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
    if ($('#dupShipper').is(":checked") && $('#address').val() == "") {
        var shipAddrsConcat = parent.$('#shipperAddress').val() + "\n" + parent.$('#shipperCity').val() + "\n" + parent.$('#shipperState').val() + "\n" + parent.$('#shipperZip').val();
        $('#address').val(shipAddrsConcat);
    }
}
function concatShipAddress() {
    var concatAddressShip = $('#address').val() + "\n" + $('#city').val() + "\n" + $('#state').val() + "\n" + $('#zip').val();
    $('#address').val(concatAddressShip);
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
function concatShipTo() {//Imports Concat shipper details
    var shipToAddress = "";
    shipToAddress += $("#address").val() + "\n";
    shipToAddress += $("#city").val() + " ";
    shipToAddress += $("#state").val() + " ";
    shipToAddress += $("#zipCode").val() + "\n";
    $("#address").val(shipToAddress);
}
function sendEmailmeforDoorDel() {//Imports Only
    showProgressBar();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "sendEmailForDoorPdf",
            param1: $('#fileNumberId').val(),
            param2: $('#fileNumber').val(),
            request: "true"
        },
        success: function (data) {
            if (data === '') {
                $.prompt("Please Enter Email Address");
            } else {
                $.prompt("Email Sent Successfully to " + "</br>" + data, {
                    buttons: {
                        Ok: 1
                    },
                    submit: function (v) {
                        if (v === 1) {
                            parent.$.fn.colorbox.close();
                        }
                    }
                });
            }
        }
    });
    hideProgressBar();
}
function openTradingPartner(path, ev) {//open TP PopUp
    var accountNumber = $('#accountNo').val();
    var href;
    if (accountNumber === "") {
        href = path + "/tradingPartner.do?buttonValue=removesession";
    } else {
        href = path + "/jsps/Tradingpartnermaintainance/SearchCustomer.jsp?callFrom=fclBillLadding&programid=156&field=" + ev;
        if (accountNumber) {
            href = path + "/tradingPartner.do?buttonValue=SearchMachList&account=" + accountNumber;
        }
    }
    $(".trading").attr("href", href);
    $(".trading").colorbox({
        iframe: true,
        width: "100%",
        height: "90 %",
        title: "Trading Partner"
    });
}
$('#shipSupplier').keyup(function () {
    var shipSupplier = $('#shipSupplier').val();
    if (shipSupplier === "") {
        $('#shipSupplier').val('');
        $('#shipperAccountNo').val('');
        $('#accountNo').val('');
        $('#email1').val('');
        $('#address').val('');
        $('#contactName').val('');
        $('#fax1').val('');
        $('#phone1').val('');
    }
});
$('#whsecompanyName').keyup(function () {
    if ($('#whsecompanyName').val() === "") {
        $('#whsecompanyName').val('');
        $('#whseAddress').val('');
        $('#whsecity').val('');
        $('#whseState').val('');
        $('#whseZip').val('');
        $('#whsePhone').val('');
        $('#whseId').val('');
    }
});
function showShipperToDetails() {
    var manualCompanyName = $('#manualCompanyName').val();
    if (manualCompanyName !== "") {
        $('#dupShipper').attr("checked", true);
        $('#manualVendor').show();
        $('#dojoVendor').hide();
    }
    else {
        $('#dupShipper').attr("checked", false);
        $('#manualVendor').hide();
        $('#dojoVendor').show();
    }
}
function newmanualVendor() {
    var moduleName = $('#moduleName').val();
    var $address = $("#address");
    var $state = $("#state");
    var $city = $("#city");
    var $zipCode = $("#zipCode");
    if (moduleName === 'Imports') {
        $('#city').val("");
        $('#zipCode').val("");
        $('#state').val("");
        $('#shipSupplier').val("");
        if ($('#dupShipper').is(":checked")) {
            $("#manualVendor").show();
            $("#dojoVendor").hide();
            $('#accountNo').val('');
            $('#companyName').val('');
            $('#address').val('');
            $address.attr("readonly", false);
            $address.removeClass('textlabelsBoldForTextBoxDisabledLook');
            $state.attr("readonly", false);
            $state.removeClass('textlabelsBoldForTextBoxDisabledLook');
            $city.attr("readonly", false);
            $city.removeClass('textlabelsBoldForTextBoxDisabledLook');
            $zipCode.attr("readonly", false);
            $zipCode.removeClass('textlabelsBoldForTextBoxDisabledLook');
            $address.select();
        } else {
            $("#dojoVendor").show();
            $("#manualVendor").hide();
            $('#manualCompanyName').val('');
            $('#address').val('');
            $address.attr("readonly", true);
            $address.addClass('textlabelsBoldForTextBoxDisabledLook');
            $state.attr("readonly", true);
            $state.addClass('textlabelsBoldForTextBoxDisabledLook');
            $city.attr("readonly", true);
            $city.addClass('textlabelsBoldForTextBoxDisabledLook');
            $zipCode.attr("readonly", true);
            $zipCode.addClass('textlabelsBoldForTextBoxDisabledLook');
            $address.select();
        }
    } else {
        if ($('#manualShipper').is(":checked")) {
            $("#manualVendor").show();
            $("#dojoVendor").hide();
            $("#toDojo").val("");
        } else {
            $("#dojoVendor").show();
            $("#manualVendor").hide();
            $("#manualCompanyName").val("");
            $("#toDojo").val("");
        }
    }

}
function checkForNumberAndDecimal(obj) {
    var result;
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This field should be Numeric");
    }
}
function enableDoorDeliveryETA() {
    var doorDeliveryStatus = $("#doorDeliveryStatus").val();
    if (doorDeliveryStatus === 'P' || doorDeliveryStatus === 'PC' || doorDeliveryStatus === 'PD'
          || doorDeliveryStatus === 'PA' || doorDeliveryStatus === 'PB' || doorDeliveryStatus === 'OH' ) {
        $("#doorDeliveryEta").val('');
        $("#doorDelivery").hide();
        $("#doorDeliveryEta").hide();
    } else {
        $("#doorDeliveryEta").show();
        $("#doorDelivery").show();
    }
}
function checkForNumber(obj) {
    if (!/^([0-9]{0,10})$/.test(obj.value)) {
        obj.value = "";
        sampleAlert("This field should be Numeric");
    }
}
function submitForm(methodName, flag) {
    var moduleName = $('#moduleName').val();
    if (($('#pickupCost').val() !== "" && $('#pickupCost').val() !== null && $('#pickupCost').val() !== '0.00') && ($('#costVendorAcct').val() === "" || $('#costVendorAcct').val() === null) && moduleName === 'Imports') {
        $.prompt("Vendor Name is required");
        $("#costVendorAcct").css("border-color", "red");
        $("#costVendorAcct").show();
    }
    else {
        if (($('#pickupCost').val() !== "" && $('#pickupCost').val() !== null && $('#pickupCost').val() !== '0.00') && ($('#chargeAmount').val() !== "" && $('#chargeAmount').val() !== null && $('#chargeAmount').val() !== '0.00')
                && moduleName === 'Exports') {
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
        var chargeCode = moduleName === 'Exports' ? "INLAND" : "DOORDEL";
        if (methodName == "save") {
            if ($('#apGlMappingFlagId').val() === "N" && ($('#pickupCost').val() !== "" && $('#pickupCost').val() !== '0.00')) {
                $.prompt('No Gl Mapping found for CostCode ---> ' + chargeCode);
                $('#pickupCost').val('');
                return false;
            } else if ($('#arGlMappingFlagId').val() === "N" && ($('#chargeAmount').val() !== "" && $('#chargeAmount').val() !== '0.00')) {
                $.prompt('No Gl Mapping found for ChargeCode --->' + chargeCode);
                $('#chargeAmount').val('');
                return false;
            }
        }
        $("#sendEdiToCtsFlag").val('');
        var doorDeliveryEta = $("#doorDeliveryEta").val();
        if ((doorDeliveryEta === '') &&  (methodName == "save") && ($("#doorDeliveryStatus").val() !=='P'
                && $("#doorDeliveryStatus").val() !=='PC' && $("#doorDeliveryStatus").val() !=='PD'
                && $("#doorDeliveryStatus").val() !=='PA' && $("#doorDeliveryStatus").val() !=='PB' && $("#doorDeliveryStatus").val() !=='OH'))
                {
            $.prompt("Door Delivery ETA is required");
        } else {
            if (flag === 'N') {
                showLoading();
            } else {
                showProgressBar();
            }
            if (($('#pickupCost').val() !== "" && $('#pickupCost').val() !== null && $('#pickupCost').val() !== '0.00') || ($('#chargeAmount').val() !== "" && $('#chargeAmount').val() !== null && $('#chargeAmount').val() !== '0.00') && moduleName === 'Imports') {
                parent.$('#pickupFlag').val('true');
            } else {
                parent.$('#pickupFlag').val('false');
            }
// parent.$('#doorOriginCityZip').val($('#cityStateZip').val());
            parent.$('#spReferenceNo').val($('#invoiceStatus').val());
            parent.$('#transType').val($('#apTransType').val());
            $('#methodName').val(methodName);
            submitAjaxFormForPickup(methodName, '#lclPickupInfoForm', '#chargeDesc', flag);
        }
    }
}
function submitAjaxFormForPickup(methodName, formName, selector, flag) {
    if (methodName == "save") {
        $("#methodName").val(methodName);
        var params = $("#lclPickupInfoForm").serialize();
        params += "&moduleName=Imports";
        $.post($(formName).attr("action"), params,
                function (data) {
                    $(selector).html(data);
                    $(selector, window.parent.document).html(data);
                    if ($('#moduleName').val() === 'Imports' && flag === 'Y') {
                        sendEmailmeforDoorDel();
                    }
                    if (flag === 'N') {
                        parent.$.fn.colorbox.close();
                    }
                });
    } else if (methodName == "savePickupInfo") {
        $("#methodName").val("save");
        var params = $("#lclPickupInfoForm").serialize();
        params += "&moduleName=Imports";
        $.post($(formName).attr("action"), params,
                function (data) {
                    $(selector).html(data);
                    $(selector, window.parent.document).html(data);
                });
    }
}

function concatWhseValues() {
    var whseName = "";
    whseName += $('#whsecompanyName').val() + "-";
    whseName += $('#whseNo').val();
    $('#whsecompanyName').val(whseName);
}
function copyPickupDetailsFromExistingBooking() {
    var fileNumberId = $('#fileNumberId').val();
    if (fileNumberId == null || fileNumberId == "" || fileNumberId == '0') {
        $('#whsePhone').val(parent.$('#whsePhone').val());
        $('#whsecompanyName').val(parent.$('#whsecompanyName').val());
        $('#whsecity').val(parent.$('#whseCity').val());
        $('#whseState').val(parent.$('#whseState').val());
        $('#whseZip').val(parent.$('#whseZip').val());
        $('#whseAddress').val(parent.$('#whseAddress').val());
        $('#pickupInstructions').val(parent.$('#pickupInstructions').val());
        $('#shipSupplier').val(parent.$('#shipSupplier').val());
        $('#tempShipper').val(parent.$('#shipSupplier').val());
        $('#spAcct').val(parent.$('#spAcct').val());
        $('#address').val(parent.$('#paddress').val());
        $('#cityStateZip').val(parent.$('#cityStateZip').val());
        $('#phone1').val(parent.$('#pphone1').val());
        $('#contactName').val(parent.$('#pcontactName').val());
        $('#fax1').val(parent.$('#pfax1').val());
        $('#email1').val(parent.$('#pemail1').val());
        $('#termsOfService').val(parent.$('#termsOfService').val());
        $('#commodityDesc').val(parent.$('#pcommodityDesc').val());
        $('#pickupReferenceNo').val(parent.$('#pickupReferenceNo').val());
        $('#pickupCutoffDate').val(parent.$('#pickupCutoffDate').val());
        $('#pickupReadyDate').val(parent.$('#pickupReadyDate').val());
        $('#cityStateZip').val(parent.$('#cityStateZip').val());
        $('#issuingTerminal').val(parent.$('#issuingTerminal').val());
        $('#to').val(parent.$('#to').val());
        $('#trmnum').val(parent.$('#ptrmnum').val());
        $('#pickupCost').val(parent.$('#pickupCost').val());
        $('#chargeAmount').val(parent.$('#chargeAmount').val());
        $('#duplicateChargeAmount').val(parent.$('#chargeAmount').val());
        $('#scacCode').val(parent.$('#scacCode').val());
        $('#pickupHours').val(parent.$('#pickupHours').val());
        $('#pickupReadyNote').val(parent.$('#pickupReadyNote').val());
    }
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

function getScacCodeList(path) {
    var href = path + "/lclPickupInfo.do?methodName=getScacCodeList";
    $("#scacCodeList").attr("href", href);
    $("#scacCodeList").colorbox({
        iframe: true,
        width: "50%",
        height: "95%",
        title: "SCAC Code"
    });
}

function setMaxLenght() {
    $("#scacCode").attr('maxlength', '250');
}
function updateDeliverToWarehouse() {
    if ($('#whsecompanyName').val() === null || $('#whsecompanyName').val() === '') {
        $('#whsecompanyName').val(parent.$('#deliverCargoToName').val() + "-" + parent.$('#deliveryCargoToCode').val());
        $('#whseAddress').val(parent.$('#deliverCargoToAddress').val());
        $('#whsecity').val(parent.$('#deliverCargoToCity').val());
        $('#whseState').val(parent.$('#deliverCargoToState').val());
        $('#whseZip').val(parent.$('#deliverCargoToZip').val());
        $('#whsePhone').val(parent.$('#deliverCargoToPhone').val());
    }
}

function submitPickUpinfo(methodName, saveFlag, moduleName) {
    if (moduleName === 'Exports') {
        var bookingZip = $(".originZip").text().trim().split("-");
        var pickUpZip = $("#zip").val();
        if (bookingZip[0] !== '' && pickUpZip !== '' && bookingZip[0] !== pickUpZip) {
            $.prompt("The Zip code on the booking screen must match the Zip " +
                    "code on the dispatch. Please correct and obtain the new rates if necessary.", {
                        buttons: {
                            OK: 1,
                            Cancel: 2
                        },
                        submit: function (v) {
                            if (v === 1) {
                                $("#pickUpInfo").val(methodName === "save" ? "true" : "false");
                                submitAjaxForm(methodName, saveFlag, moduleName);

                            }
                        }
                    });
        } else {
            submitAjaxForm(methodName, saveFlag, moduleName);
        }
    } else {
        submitAjaxForm(methodName, saveFlag, moduleName);
    }
}

function submitAjaxForm(methodName, saveFlag, moduleName) {
    if (methodName === "save") {
        submitForm(methodName, saveFlag);
    } else if (methodName === "callCts") {
        callCarrierRates($('#whseZip').val(), $('#pickupReadyDate').val(), $('#fileNumberId').val(), moduleName);
    } else if (methodName === 'sendEdi') {
        conformboxEdi(moduleName);
    }
}
