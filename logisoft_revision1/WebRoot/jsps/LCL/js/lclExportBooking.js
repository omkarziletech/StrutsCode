var path = "/" + window.location.pathname.split('/')[1];
jQuery(document).ready(function () {
    displayClientDetails(); //set client ToolTip
    displayShipperDetails(); //set Shipper ToolTip
    displayConsigneeDetails(); //set Consignee ToolTip
    displayForwarderDetails(); //set Forwarder ToolTip
    fillDeliveryMetro(); //set delivery Metro details enable or disable radio button
    lockInsurance();
    fillPolPod(); //set values upcomingsailing header tab
    displaydispoDesc();
    shortShip();
    checkBillingType();
    checkBilltoCode();
    showVoyageList();
    setCargoStatus();
    showCityName();
    openConoslidationPopUp();
    validateTermToBl();
    setEconoEcuWorldWide();
    disableCobFields();
    setCfclValues();
    if ($('#bookingType').val() === "T") {
        disabledExpBillToCode();
    }
    setCfclLabelColor();
    setPreCobFn();
});
function disableCobFields() {
    if ($("#cob").val() === 'true') {
        $("#agentName").attr('readonly', true);
        $("#agentName").removeClass("textlabelsBoldForTextBox").addClass("text-readonly textlabelsBoldForTextBoxDisabledLook");
        $('.defAgent').attr('readonly', true);
        $('.defAgent').attr('disabled', true);
        $('.defAgent').attr('disabled', true);
        $('.defAgent').attr('readonly', true);
    }
}
function displaydispoDesc() {
    if ($("#dispositionToolTip").val() !== null && $("#dispositionToolTip").val() !== "") {
        ImportsJToolTip('#disposition', $('#dispositionToolTip').val(), 700, 'topMiddle');
    }
}
function fillDeliveryMetro() {
    var unlocationCode = $('#unlocationCode').val().toUpperCase();
    if (unlocationCode === 'PRSJU') {
        $('#deliveryMetroI').attr('disabled', false);
        $('#deliveryMetroN').attr('disabled', false);
        $('#deliveryMetroO').attr('disabled', false);
    } else {
        $('#deliveryMetroI').attr('disabled', true);
        $('#deliveryMetroO').attr('disabled', true);
        $('#deliveryMetroN').attr('checked', true);
    }
}
function filldeliverCargo() {//fill Delivery Cargo Details
    var originUnlocationCode = getOrigin();
    var fileNumber = $('#fileNumber').val();
    if (fileNumber !== "" && fileNumber !== null) {
        var org = originUnlocationCode.substring(2);
        var originFileNumber = org + "-" + fileNumber;
        $('#fileNumberBooking').text(originFileNumber);
    }
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO",
            methodName: "updateDeliveryContactForExport",
            param1: $("#fileNumberId").val(),
            param2: originUnlocationCode,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            setDeliveryCargoDetails(data);
        }
    });
    $('#finalDestinationR').focus().val();
    fillPolPod();
}
function setTeriminalFromBkg() {
    if ($('#fileType').val() === "BL") {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.common.constant.ExportUtils",
                methodName: "getTerimal",
                param1: $("#fileNumberId").val(),
                param2: "Booking",
                response: true,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                $('#terminal').val(data[0]);
                $('#trmnum').val(data[1]);
                if (data[2] === 'V') {
                    $("#lclCargoButton").hide();
                    $("#lclCargoButtonBottom").hide();
                }
            }
        });
    }
}

function updateTerminal() {
    var trmnum = $('#trmnum').val();
    var fileId = $('#fileNumberId').val();
    var fileType = $('#fileType').val();
    if (fileType === "BL" && fileId !== '') {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO",
                methodName: "updateTerminal",
                param1: trmnum,
                param2: fileId,
                request: "true",
                dataType: "json"
            },
            close: function (data) {
            }
        });
    } else if ($('#trmnum').val() === "59" && $(".econo").text() === "Econo") {
        $("input:radio[name=businessUnit]").val(['ECI']);
        if (fileId !== '') {
            var businessUnit = $('input:radio[name=businessUnit]:checked').val();
            showProgressBar();
            updateEconoOrEculine(businessUnit, $('#fileNumberId').val(), "", "DR-AutoNotes");
            hideProgressBar();
        }
    }
}
function validateTermToBl() {
    parent.$("ul.htabs li.selected").click(function () {
        if ($('#moduleName').val() === 'Exports') {
            setTeriminalFromBkg();
        }
    });
}
function setDeliveryCargoDetails(data) {
    if (data !== undefined && data !== "" && data !== null) {
        $("#deliveryCargoToCode").val(data[0]);
        $("#deliverCargoToName").val(data[1]);
        $("#deliverCargoToAddress").val(data[2]);
        $("#deliverCargoToCity").val(data[3]);
        $("#deliverCargoToState").val(data[4]);
        $("#deliverCargoToZip").val(data[5]);
        $("#deliverCargoToPhone").val(data[6]);
        $("#deliverCargoToFax").val(data[7]);
    } else {
        clearDeliverCargoFields();
    }
}
function clearDeliverCargoFields() {
    $("#deliveryCargoToCode").val("");
    $("#deliverCargoToName").val("");
    $("#deliverCargoToAddress").val("");
    $("#deliverCargoToCity").val("");
    $("#deliverCargoToState").val("");
    $("#deliverCargoToZip").val("");
    $("#deliverCargoToPhone").val("");
    $("#deliverCargoToFax").val("");
}
function checkOriginTerminal() {
    var origin = getOrigin();
    var ratetype = $('input:radio[name=rateType]:checked').val();
    var rateTypes;
    if (ratetype === "R") {
        rateTypes = "Retail";
    } else if (ratetype === "C") {
        rateTypes = "Coload";
    } else if (ratetype === "F") {
        rateTypes = "Foreign to Foreign";
    }
    if ((origin !== undefined && origin !== "") && (ratetype !== undefined && ratetype !== "")) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkTerminal",
                param1: origin,
                param2: ratetype
            },
            async: false,
            success: function (data) {
                if (!data) {
                    $.prompt(rateTypes + " Terminal is not available.Please select different origin");
                    $('#portOfOriginR').val('');
                    $("#deliverCargoTo").val('');
                    $("#whsCode").val('');
                    $('#portOfOriginR').css("border-color", "red");
                    $('#portOfOriginR').removeClass("text-readonly");
                    $('#portOfOriginR').removeClass("textlabelsBoldForTextBoxDisabledLook");
                    $('#portOfOriginR').addClass("textlabelsBoldForTextBox");
                } else {
                    fillaesbyRateType();
                    lockInsurance();
                }
            }
        });
    }
    if (origin !== undefined && origin !== "") {
        var nonRated = $('input:radio[name=nonRated]:checked').val();
        var pol = $('#portOfLoading').val();
        if (nonRated === 'Y' && pol === '') {
            $('#portOfLoading').val($('#portOfOriginR').val());
        }
    }
}
function fillaesbyRateType() {
    var ratetype = $('input:radio[name=rateType]:checked').val();
    var trmnum = $('#trmnum').val();
    if ($("#fileNumberId").val() === '' && trmnum === '59') {
        $('#aesByN').attr('checked', true);
    } else if (ratetype === "R") {
        $('#aesByY').attr('checked', true);
    } else {
        $('#aesByN').attr('checked', true);
    }
}
function modifyAdjustmentAmount(comments) {  // this is for  Adjustment Only
    var bkgChargeId = $('#chargeIdAdj').val();
    var newAdjusmentAmt = Number($('#adjustmentAmount' + bkgChargeId).val());
    if ((bkgChargeId !== null && bkgChargeId !== "" && bkgChargeId !== undefined) &&
            (newAdjusmentAmt !== null && newAdjusmentAmt !== "" && newAdjusmentAmt !== undefined)) {
        var fileNumberId = $('#fileNumberId').val();
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "updateAdjusmentAmt",
                param1: bkgChargeId,
                param2: newAdjusmentAmt,
                param3: comments,
                param4: fileNumberId,
                request: true
            },
            success: function (data) {
                if (data === "true") {
                    calculateCAFCharge();
                } else {
                    submitAjaxFormForAdjustment('renderChargeDescAdjustment', '#lclBookingForm', '#chargeDesc', fileNumberId);
                }
                $("#add-Comments-container").center().hide(500, function () {
                    hideAlternateMask();
                });
            }
        });
    }
}
function submitAjaxFormForCharges(amount, comments) { // this is for Total Adjustment
    var bkgChargeId = $('#chargeIdTotal').val();
    var newAdjusmentAmt = amount - $("#oldOriginalChargeVal").val();
    if ((bkgChargeId !== null && bkgChargeId !== "" && bkgChargeId !== undefined) &&
            (newAdjusmentAmt !== null && newAdjusmentAmt !== "" && newAdjusmentAmt !== undefined)) {
        var fileNumberId = $('#fileNumberId').val();
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "updateAdjusmentAmt",
                param1: bkgChargeId,
                param2: newAdjusmentAmt,
                param3: comments,
                param4: fileNumberId,
                request: true
            },
            success: function (data) {
                if (data === "true") {
                    calculateCAFCharge();
                } else {
                    submitAjaxFormForAdjustment('renderChargeDescAdjustment', '#lclBookingForm', '#chargeDesc', fileNumberId);
                }
                $("#add-Comments-containerTotal").center().hide(500, function () {
                    hideAlternateMask();
                });
            }
        });
    }
}

function calculateCAFCharge() {
    var fileId = $('#fileNumberId').val();
    if (fileId != null && fileId != "" && fileId != "0" && fileId != undefined) {
        var destination = $('#unlocationCode').val();
        var origin = getOrigin();
        var pol = $('#polUnlocationcode').val();
        var pod = $('#podUnlocationcode').val();
        var pcboth = $('input:radio[name=pcBoth]:checked').val();
        if (pcboth == 'C') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "checkLclCollectCharges",
                    param1: destination
                },
                success: function (data) {
                    if (data === "true") {
                        submitAjaxFormForCAF('calculateCAFCharge', '#lclBookingForm', '#chargeDesc', origin, destination, pol, pod, fileId, pcboth);
                    } else {
                        if ($("#agentNumber").val() === '') {
                            $.prompt("Please Select Agent Name And Number");
                        } else {
                            updateBillToCode("A");
                        }
                    }
                }
            });
        } else if (pcboth == 'P' || pcboth == 'B') {
            submitAjaxFormForCAF('calculateCAFCharge', '#lclBookingForm', '#chargeDesc', origin, destination, pol, pod, fileId, pcboth);
        }
    }
}
function submitAjaxFormForCAF(methodName, formName, selector, origin, destination, pol, pod, fileId, pcboth) {
    showProgressBar();
    var billToParty = $('input:radio[name=billForm]:checked').val();
    billToParty = undefined === billToParty ? $('#previousbillToParty').val() : billToParty;
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod + "&fileNumberId="
            + fileId + "&pcboth=" + pcboth + "&billToParty=" + billToParty;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}

function submitAjaxFormForAdjustment(methodName, formName, selector, fileId) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    var destination = getDestination();
    params += "&fileNumberId =" + fileId + "&destination=" + destination;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}

function isValidateBillToNoty() {
    var consigneeChargeFlag = false;
    var rowCount = $("#chargesTable tbody tr").length;
    for (var i = 1; i <= rowCount; i++) {
        var value = $("#chargesTable").find('tr:nth-child(' + i + ') td:nth-child(6)').html();
        if (trim(value).indexOf("Notify Party") !== -1) {
            consigneeChargeFlag = true;
        }
    }
    if (consigneeChargeFlag) {
        $('#newNotify').attr('checked', false);
        $.prompt('Notify Party does not exist in Trading Partners.Cannot bill charges to Notify Party.Please correct.');
    } else {
        $('#notifyName').val('');
        $('#notifyCode').val('');
        $('#notifyCreditClient').text('');
        newNotifyName();
    }
}

function setRelayOverRide() {
    if ($("#isRelay").val() === 'true') {
        $("#relayOverYes").attr("checked", true);
        changePort();
    } else {
        $("#relayOverNo").attr("checked", true);
        changePort();
    }
}
function setDeliveryMetro() {
    var unlocationCode = $('#unlocationCode').val().toUpperCase();
    if (unlocationCode === 'PRSJU') {
        $('#deliveryMetroI').attr('checked', true);
        $('#deliveryMetroI').attr('disabled', false);
        $('#deliveryMetroN').attr('disabled', false);
        $('#deliveryMetroO').attr('disabled', false);
    } else {
        $('#deliveryMetroI').attr('disabled', true);
        $('#deliveryMetroO').attr('disabled', true);
        $('#deliveryMetroN').attr('checked', true);
    }
}
function destinationRemarks() {
    $("#specialRemarks").val('');
    $("#internalRemarks").val('');
    $("#portGriRemarks").val('');
    var fdUnlocationcode = document.getElementById('unlocationCode').value;
    var podUnlocationcode = document.getElementById('podUnlocationcode').value;
    var destination = $('#finalDestinationR').val();
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "defaultDestinationRemarks",
            param1: fdUnlocationcode,
            param2: podUnlocationcode,
            dataType: "json",
            async: false
        },
        success: function (data) {
            if (data[3] !== undefined && data[3] !== "" && data[3] !== null) {
                $("#specialRemarksPod").val(data[3]);
                $("#specialPod").html('<span style="color:#800080">POD : </span>' + data[3] + "<br/>");
            } else {
                $("#specialRemarksPod").val('');
                $("#specialPod").html('');
            }
            if (data[0] !== undefined && data[0] !== "" && data[0] !== null) {
                $("#specialRemarks").val(data[0]);
                $("#specialFd").html('<span style="color:#800080">FD : </span>' + data[0]);
            } else {
                $("#specialRemarks").val('');
                $("#specialFd").html('');
            }
            if (data[4] !== undefined && data[4] !== "" && data[4] !== null) {
                $("#internalRemarksPod").val(data[4]);
                $("#internalPod").html('<span style="color:#800080">POD : </span>' + data[4] + "<br/>");
            } else {
                $("#internalRemarksPod").val('');
                $("#internalPod").html('');
            }
            if (data[1] !== undefined && data[1] !== "" && data[1] !== null) {
                $("#internalRemarks").val(data[1]);
                $("#internalFd").html('<span style="color:#800080">FD : </span>' + data[1]);
            } else {
                $("#internalRemarks").val('');
                $("#internalFd").html('');
            }
            if (data[5] !== undefined && data[5] !== "" && data[5] !== null) {
                $("#portGriRemarksPod").val(data[5]);
                $("#griRemarksPod").html('<span style="color:#800080">POD : </span>' + data[5] + "<br/>");
            } else {
                $("#portGriRemarksPod").val('');
                $("#griRemarksPod").html('');
            }
            if (data[2] !== undefined && data[2] !== "" && data[2] !== null) {
                $("#portGriRemarks").val(data[2]);
                $("#griRemarksFd").html('<span style="color:#800080">FD : </span>' + data[2]);
            } else {
                $("#portGriRemarks").val('');
                $("#griRemarksFd").html('');
            }
            if (data[1] !== undefined && data[1] !== "" && data[1] !== null) {
                $.prompt("<span style='color:red;font-weight:bold;'>INTERNAL REMARKS FOR " + destination + "</span>" + "\n" + "</br>" + data[1]);
            }
        }
    });
}
function showHideRelayFd() {
// var destination = $('#finalDestinationR').val();  //---override the value of origin dojo withour relay
    var relay = $('input:radio[name=relayOverride]:checked').val();
    var nonRated = $('input:radio[name=nonRated]:checked').val();
    if (relay == 'Y') {
        if ($('#showFullRelay').is(":checked")) {
            $('#portOfOriginR').attr('alt', 'PORTNAME');
        } else {
            $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
        }
    } else {
        if (nonRated == 'N') {
            if ($('#showFullRelay').is(":checked")) {
                $('#portOfOriginR').attr('alt', 'PORTNAME');
            } else {
                $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
            }
        } else {
            if ($('#showFullRelay').is(":checked")) {
                $('#portOfOriginR').attr('alt', 'PORTNAME');
            } else {
                $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
            }
        }
    }
}
function showRelayFdForRelay() {
    var origin = $('#portOfOriginR').val();
    var relay = $('input:radio[name=relayOverride]:checked').val();
    var nonRated = $('input:radio[name=nonRated]:checked').val();
    if (relay == 'Y') {
        if ($('#basedOnCity').is(":checked")) {
            if ($('#showFullRelayFd').is(":checked")) {
                $('#finalDestinationR').attr('alt', 'CONCAT_PORT_NAME');
            } else {
                $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
            }
        } else {
            if ($('#showFullRelayFd').is(":checked")) {
                $('#finalDestinationR').attr('alt', 'CONCAT_CITY_NAME');
            } else {
                $('#finalDestinationR').attr('alt', 'CONCAT_COUNTRY_NAME');
            }
        }
    } else {
        if (nonRated == 'N') {
            if ($('#basedOnCity').is(":checked")) {
                $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
            } else {
                $('#finalDestinationR').attr('alt', 'CONCAT_COUNTRY_NAME');
            }
        } else {
            if ($('#basedOnCity').is(":checked")) {
                if ($('#showFullRelayFd').is(":checked")) {
                    $('#finalDestinationR').attr('alt', 'CONCAT_PORT_NAME');
                } else {
                    $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
                }
            } else {
                if ($('#showFullRelayFd').is(":checked")) {
                    $('#finalDestinationR').attr('alt', 'CONCAT_CITY_NAME');
                } else {
                    $('#finalDestinationR').attr('alt', 'CONCAT_COUNTRY_NAME');
                }
            }
        }
    }
}

$(document).ready(function () {
    $('#portOfOriginR').keyup(function () {
        var portOfOriginId = $('#portOfOriginR').val();
        if (portOfOriginId === "") {
            $('#portOfLoading').val('');
            $('#portOfLoadingId').val('');
            $('#portOfDestination').val('');
            $('#portOfDestinationId').val('');
            $('#portOfOriginId').val('');
            $('#agentName').val('');
            $('#agentNumber').val('');
            $('#agentBrand').val('');
            submitAjaxFormforRates('displayemptyVoyage', '#lclBookingForm', '#upcomingSailing', '');
            showHideRelayFd();
            showRelayFdForRelay();
        }
    });
    $('#finalDestinationR').keyup(function () {
        var destination = $('#finalDestinationR').val();
        var relayOverride = $('input:radio[name=relayOverride]:checked').val();
        var picked = $('#isPicked').val();
        if (destination === "" && relayOverride === 'N' && picked != 'true') {
            $('#portOfLoading').val('');
            $('#portOfLoadingId').val('');
            $('#portOfDestination').val('');
            $('#portOfDestinationId').val('');
            $('#finalDestinationId').val('');
            $('#agentName').val('');
            $('#agentNumber').val('');
            $('#agentBrand').val('');
            submitAjaxFormforRates('displayemptyVoyage', '#lclBookingForm', '#upcomingSailing', '');
            showRelayFdForRelay();
            showHideRelayFd();
        }
    });
});
function lockInsurance() {
    var destination = getDestination();
    var ratetype = $('input:radio[name=rateType]:checked').val();
    var insurance = $('input:radio[name=insurance]:checked').val();
    if (insurance == 'N') {
        $("#valueOfGoods").addClass("textlabelsBoldForTextBoxDisabledLook");
        $('#valueOfGoods').attr("readonly", true);
    }
    if (destination != "" && (ratetype != "C" || ratetype != "F" || ratetype != "R")) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkInsurance",
                param1: destination
            },
            success: function (data) {
                if (data === "true") {
                    $('#insuranceY').attr('checked', false);
                    $('#insuranceY').attr('disabled', true);
                    $('#insuranceN').attr('disabled', true);
                    $('#insuranceN').attr('checked', false);
                    $("#valueOfGoods").addClass("textlabelsBoldForTextBoxDisabledLook");
                    $('#valueOfGoods').attr("readonly", true);
                } else {
                    if (destination != "" && (ratetype != "C" && ratetype != "R" && ratetype != "F") && (insurance != "N")) {
                        $('#insuranceY').attr('disabled', false);
                        $('#insuranceN').attr('disabled', false);
                        $('#valueOfGoods').removeAttr("readonly");
                        $("#valueOfGoods").removeClass("textlabelsBoldForTextBoxDisabledLook");
                    } else if (destination != "" && (ratetype == "C" || ratetype == "R" || ratetype == "F")) {
                        if (insurance !== "N") {
                            $('#insuranceY').attr('disabled', false);
                            $('#insuranceN').attr('disabled', false);
                            $("#valueOfGoods").removeClass("textlabelsBoldForTextBoxDisabledLook")
                            $("#valueOfGoods").addClass("textlabelsBoldForTextBox");
                            $('#valueOfGoods').removeAttr("readonly");
                        }
                    }
                }
            }
        });
    }
}
//this method is used to upcoming sailing tab header
function fillPolPod() {
    $('#pooSailing').text($('#portOfOriginR').val());
    $('#polSailing').text($('#portOfLoading').val());
    $('#podSailing').text($('#portOfDestination').val());
    $('#fdSailing').text($('#finalDestinationR').val());
    if ($('#portOfOriginR').val() === '') {
        checkRoleDuty();
    }
}
function displayMultiRates(path, fileNumberId, doorOriginCityZip) {
    var origin = getOrigin();
    var ert = $('#rtdTransaction').val();
    var zip = $(doorOriginCityZip).val();
    var datatableobj = document.getElementById('commObj');
    var rateType = $('input:radio[name=rateType]:checked').val();
    var destinationValue = document.getElementById('finalDestinationR').value;
    if ((zip === null || zip === "") && (origin === null || origin.length === 0)) {
        $.prompt("Please Select Door Origin/Origin");
    } else if (destinationValue === null || destinationValue.length === 0) {
        $.prompt("Please Select Destination");
    } else if (rateType === "" || rateType === null || rateType === undefined) {
        $.prompt("CTC/Retail/FTF  is required");
    } else if (ert === "" || ert === null) {
        $.prompt("ERT field is required");
        $("#rtdTransaction").css("border-color", "red");
    } else if (datatableobj === null) {
        $.prompt("Please add atleast one commodity/tariff#");
        return false;
    } else {
        if (rateType === "R") {
            rateType = "Y";
        }
        var destination = getDestination();
        var fd = document.getElementById('finalDestinationId').value;
        var href = path + "/lclmultiRate.do?methodName=displayRates&origin=" + origin + "&destination=" + destination + "&destinationValue=" + destinationValue +
                "&zip=" + zip + "&rateType=" + rateType + "&fileNumberId=" + fileNumberId + "&fd=" + fd;
        $(".multiRates").attr("href", href);
        $(".multiRates").colorbox({
            iframe: true,
            width: "100%",
            height: "90%",
            href: window.parent.showPreloading(),
            title: "Routing Options"
        });
    }
}//end of method

function openClientBkgContact(path, accountName, accountNo, contactName, subtype) {
    if ($('#contactName').val() !== "" || $('#clientContactManul').val() !== "" || $('#shipperContactClient').val() !== "" || $('#shipperManualContact').val() !== "" ||
            $('#consigneeContactName').val() !== "" || $('#consigneeManualContact').val() !== "" || $('#forwardercontactClient').val() !== "" || $('#forwarederContactManual').val() !== "") {
        $.prompt('Only one Booking Contact are allowed, Please remove the existing one and try.');
        return false;
    } else if (($('#client').val() == "" || $('#client_no').val() == "")) {
        $.prompt('Contact are not allowed, Please select client name and try.');
        return false;
    } else {
        var href = path + "/lclContactDetails.do?methodName=display&vendorName=" + accountName + "&vendorNo=" + accountNo + "&contactName=" + contactName + "&vendorType=" + subtype;
        $.colorbox({
            iframe: true,
            href: href,
            width: "90%",
            height: "90%",
            title: "Contactexp"
        });
    }
}
function openShipContact(path, accountName, accountNo, contactName, subtype) {
    if ($('#shipperNameClient').val() == "" || $('#shipperCodeClient').val() == "") {
        $.prompt('Contact are not allowed, Please select shipper name and try.');
    } else if ($('#contactName').val() != "" || $('#clientContactManul').val() != "" || $('#shipperContactClient').val() != "" || $('#shipperManualContact').val() != "" ||
            $('#consigneeContactName').val() != "" || $('#consigneeManualContact').val() != "" || $('#forwardercontactClient').val() != "" || $('#forwarederContactManual').val() != "") {
        $.prompt('Only one Booking Contact are allowed, Please remove the existing one and try.');
    } else {
        var href = path + "/lclContactDetails.do?methodName=display&vendorName=" + accountName + "&vendorNo=" + accountNo + "&contactName=" + contactName + "&vendorType=" + subtype;
        $.colorbox({
            iframe: true,
            href: href,
            width: "90%",
            height: "90%",
            title: "Contact"
        });
    }
}


function openFwdContact(path, accountName, accountNo, contactName, subtype) {
    if ($('#contactName').val() != "" || $('#clientContactManul').val() != "" || $('#shipperContactClient').val() != "" || $('#shipperManualContact').val() != "" ||
            $('#consigneeContactName').val() != "" || $('#consigneeManualContact').val() != "" || $('#forwardercontactClient').val() != "" || $('#forwarederContactManual').val() != "") {
        $.prompt('Only one Booking Contact are allowed, Please remove the existing one and try.');
        return false;
    } else if ($('#forwarderNameClient').val() == "" || $('#forwarderCodeClient').val() == "") {
        $.prompt('Contact are not allowed, Please select forwarder name and try.');
        return false;
    } else {
        var href = path + "/lclContactDetails.do?methodName=display&vendorName=" + accountName + "&vendorNo=" + accountNo + "&contactName=" + contactName + "&vendorType=" + subtype;
        $.colorbox({
            iframe: true,
            width: "90%",
            height: "90%",
            href: href,
            title: "Contact"
        });
    }
}
function openConsContact(path, accountName, accountNo, contactName, subtype) {
    if ($('#contactName').val() != "" || $('#clientContactManul').val() != "" || $('#shipperContactClient').val() != "" || $('#shipperManualContact').val() != "" ||
            $('#consigneeContactName').val() != "" || $('#consigneeManualContact').val() != "" || $('#forwardercontactClient').val() != "" || $('#forwarederContactManual').val() != "") {
        $.prompt('Only one Booking Contact are allowed, Please remove the existing one and try.');
    } else if ($('#consigneeNameClent').val() == "" || $('#consigneeCodeClient').val() == "") {
        $.prompt('Contact are not allowed, Please select consignee name and try.');
    } else {
        var href = path + "/lclContactDetails.do?methodName=display&vendorName=" + accountName + "&vendorNo=" + accountNo + "&contactName=" + contactName + "&vendorType=" + subtype;
        $.colorbox({
            iframe: true,
            width: "90%",
            height: "90%",
            href: href,
            title: "Contact"
        });
    }
}
function addAesDetails(path, fileNumber, fileNumberId) {//Add AES Details
    var bookedVoyage = $('#eciVoyage').val();
    var pickedOnVoyageNo = $('#pickedOnVoyageNo').val();
    if (bookedVoyage === '' && pickedOnVoyageNo === '') {
        $.prompt("Booked/Picked Voyage Information is required");
    } else {
        var href = path + "/lclAesDetails.do?methodName=display&fileNumber=" + fileNumber + "&fileNumberId=" + fileNumberId;
        $.colorbox({
            iframe: true,
            width: "95%",
            height: "95%",
            href: href,
            title: "AES details"
        });
    }
}
function schedBdetails(path, fileNumber, fileNumberId) {
    var href = path + "/lclSchedBdetails.do?methodName=display&fileNumber=" + fileNumber + "&fileNumberId=" + fileNumberId;
    $(".schedB").attr("href", href);
    $(".schedB").colorbox({
        iframe: true,
        width: "90%",
        height: "90%",
        title: "Sched B details"
    });
}
function editAes(path, id, trnref, fileNumber) {//Edit AES Details
    var href = path + "/lclAesDetails.do?methodName=editAes&id=" + id + "&trnref=" + trnref + "&fileNumber=" + fileNumber;
    $(".aes").attr("href", href);
    $(".aes").colorbox({
        iframe: true,
        width: "90%",
        height: "90%",
        title: "AES details"
    });
}
function displaySchedBDetails(path, fileNumber, trnref) {//Display AES
    var href = path + "/lclAesDetails.do?methodName=displaySchedB&trnref=" + trnref + "&fileNumber=" + fileNumber;
    $(".schedB").attr("href", href);
    $(".schedB").colorbox({
        iframe: true,
        width: "80%",
        height: "90%",
        title: "SchedB details",
        onClosed: function () {
            var shpdr = $('#fileNumber').val();
            $("#methodName").val("closeSched");
            var params = $("#lclBookingForm").serialize();
            params += "&shpdr=" + shpdr;
            $.post($("#lclBookingForm").attr("action"), params,
                    function (data) {
                        $("#aesDesc").html(data);
                        $("#aesDesc", window.parent.document).html(data);
                    });
        }
    });
}
function destinationServices(path, fileNumberId, fileNumber, buttonValue) {//Destination services
    var fd = $('#unlocationCode').val();
    var headerId = $('#unlocationCode').val();
    var href = path + "/lclCostAndCharge.do?methodName=displayDestinationServices&buttonValue=" + buttonValue
            + "&fileNumberId=" + fileNumberId + "&destination=" + fd + "&fileNumber="
            + fileNumber + "&manualEntry=true" + "&headerId=" + headerId;
    $("#destinationServices").attr("href", href);
    $("#destinationServices").colorbox({
        iframe: true,
        width: "75%",
        height: "80%",
        title: "Destination Services",
        onClosed: function () {
            showLoading();
            $("#methodName").val("refreshChargesTab");
            var params = $("#lclBookingForm").serialize();
            $.post($("#lclBookingForm").attr("action"), params, function (data) {
                $("#chargeDesc").html(data);
                $("#chargeDesc", window.parent.document).html(data);
                closePreloading();
            });
        }
    });
}
function editdestinationServices(path, chargeId, fileNumberId, fileNumber, buttonValue) {//Destination services
    var fd = $('#unlocationCode').val();
    var fileNumberStatus = $('#fileNumberStatus').val();
    var href = path + "/lclCostAndCharge.do?methodName=displayDestinationServices&buttonValue=" + buttonValue
            + "&fileNumberId=" + fileNumberId + "&destination=" + fd
            + "&fileNumber=" + fileNumber + "&manualEntry=true" + "&id=" + chargeId + "&fileNumberStatus=" + fileNumberStatus;
    $(".costAndCharge").attr("href", href);
    $(".costAndCharge").colorbox({
        iframe: true,
        href: href,
        width: "70%",
        height: "80%",
        title: "Destination Services",
        onClosed: function () {
            showLoading();
            $("#methodName").val("refreshChargesTab");
            var params = $("#lclBookingForm").serialize();
            $.post($("#lclBookingForm").attr("action"), params, function (data) {
                $("#chargeDesc").html(data);
                $("#chargeDesc", window.parent.document).html(data);
                closePreloading();
            });
        }
    });
}
function showInlandVoyageInfo(path, fileId) {//Inland Service Details
    var href = path + "/lclBooking.do?methodName=inlandVoyageInfo&fileNumberId=" + fileId;
    $.colorbox({
        iframe: true,
        href: href,
        width: "60%",
        height: "80%",
        title: "Inland Voyage"
    });
}
function validateBillingType() {
    var billingType = $('input:radio[name=pcBoth]:checked').val();
    var existBillTo = $('#previousBillingType').val();
    if ("B" != billingType) {
        var billToParty = billingType == "C" ? "A" : "F";
        var txt1 = validateBillToAcct(billToParty);
        if (txt1 !== "") {
            $.prompt(txt1);
            $('input:radio[name=pcBoth]').val([existBillTo === '' ? "P" : existBillTo]);
            return false;
        }
    }

    var txt = "";
    if (billingType === 'B') {
        txt = "Are you sure want to change the Prepaid/Collect to Both";
    } else {
        txt = "Please note that all Bill to Code will be changed for all Charges";
    }
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                if (billingType === 'P') {
                    $('#billF').attr("checked", true);
                    $('#previousbillToParty').val("F");
                } else if (billingType === 'C') {
                    $('#billA').attr("checked", true);
                    $('#previousbillToParty').val("A");
                }
                checkBilltoCode();
                checkBillingType();
                calculateCAFCharge();
                $('#previousBillingType').val(billingType);
            } else {
                var $billType = $('input:radio[name=pcBoth]');
                $billType.filter('[value=' + existBillTo + ']').attr('checked', true);
            }
        }
    });
}
function checkBillingType() {
    $('#billA').attr("disabled", true);
    $('#billF').attr("disabled", true);
    $('#billS').attr("disabled", true);
    $('#billT').attr("disabled", true);
    if ($('#radioP').attr('checked')) {
//$('#billF').attr("checked", true);
        $('#billF').attr("disabled", false);
        $('#billS').attr("disabled", false);
        $('#billT').attr("disabled", false);
        // checkBillToParty(forwarder, shipper, thirdParty, val);
    } else if ($('#radioC').attr('checked')) {
        $('#billA').attr("disabled", false);
        $('#billA').attr("checked", true);
    } else if ($('#radioB').attr('checked')) {
        $('#billA').attr("disabled", true);
        $('#billS').attr("disabled", true);
        $('#billT').attr("disabled", true);
        $('#billF').attr("disabled", true);
        $('#billA').attr("checked", false);
        $('#billS').attr("checked", false);
        $('#billT').attr("checked", false);
        $('#billF').attr("checked", false);
        // checkBillToParty(forwarder, shipper, thirdParty, val);
    }
}

function checkBillToParty(forwarder, shipper, thirdParty, val) {
    if (forwarder !== '') {
        $('#billF').attr("checked", true);
    } else if (shipper !== '') {
        $('#billS').attr("checked", true);
    } else if (thirdParty !== '') {
        $('#billT').attr("checked", true);
    } else {
        $('#billF').attr("checked", true);
    }
    $('#thirdPartyname').addClass('textlabelsBoldForTextBoxDisabledLook');
    $('#thirdPartyname').attr('readonly', true);
}
function validateBillToAcct(billCode) {
    var txt = "";
    switch (billCode) {
        case 'S':
            if ($("#shipperCodeClient").val() === '') {
                txt = "Please Select Shipper Name And Number";
            }
            break;
        case 'F':
            if ($("#forwarderCodeClient").val() === '') {
                txt = "Please Select Forwarder Name And Number";
            }
            break;
        case 'T':
            if ($("#thirdpartyaccountNo").val() === '') {
                txt = "Please Select Third Party Name And Number";
            }
            break;
        case 'A':
            if ($("#agentNumber").val() === '') {
                txt = "Please Select Agent Name And Number";
            }
            break;
    }
    return txt;
}
function validateBillToCode(billCode, ele) {
    var existBillTo = $('#previousbillToParty').val();
    var txt = validateBillToAcct(billCode);
    if (txt !== "") {
        var $billTo = $('input:radio[name=billForm]');
        $billTo.filter('[value=' + existBillTo + ']').attr('checked', true);
        $.prompt(txt);
        $("#" + ele.id).attr("checked", false);
    } else {
        updateBillToCode(billCode);
        $('#previousbillToParty').val(billCode);
    }
}

function updateBillToCode(billCode) {
    if ($("#fileNumberId").val() !== '' && $("#fileNumberId").val() !== null && $("#fileNumberId").val() !== undefined) {
        var exitBillToParty = $("#previousbillToParty").val();
        var billingType = $('input:radio[name=pcBoth]:checked').val();
        showProgressBar();
        $("#methodName").val("updateBillToCode");
        var params = $("#lclBookingForm").serialize();
        params += "&exitBillToParty=" + exitBillToParty + "&billingType=" + billingType + "&billToparty=" + billCode;
        $.post($("#lclBookingForm").attr("action"), params, function (data) {
            $("#chargeDesc").html(data);
            $("#chargeDesc", window.parent.document).html(data);
            hideProgressBar();
        });
    }
}

function checkBilltoCode() {
    var billToParty = $('input:radio[name=pcBoth]:checked').val();
    if (billToParty === 'P' || billToParty === 'B') {
        $('#thirdPartyname').removeClass('textlabelsBoldForTextBoxDisabledLook');
        $('#thirdPartyname').removeAttr('readonly');
        $('#thirdpartyaccountNo').addClass('textlabelsBoldForTextBoxDisabledLook');
        $('#thirdpartyaccountNo').attr('readonly', true);
    } else {
        $('#thirdPartyname').addClass('textlabelsBoldForTextBoxDisabledLook');
        $('#thirdPartyname').attr('readonly', true);
        $('#thirdpartyaccountNo').addClass('textlabelsBoldForTextBoxDisabledLook');
        $('#thirdpartyaccountNo').attr('readonly', true);
    }
}
function createShortShip(path, fileNumberId, fileNumber) {
    var fileState = $('#fileType').val();
    if (fileState === 'BL') {
        $.prompt("The original DR has a Bill of Lading. By creating a short shipment,"
                + "such BL will not be updated automatically. If required, please update manually", {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v === 1) {
                            callBackByShortShip(path, fileNumber);
                        } else {
                            $.prompt.close();
                        }
                    }
                });
    } else {
        callBackByShortShip(path, fileNumber);
    }
}
function callBackByShortShip(path, fileNumber) {
    showProgressBar();
    var voyageId = $('#pickVoyId').val();
    var unitSsId = $('#unitssId').val();
    var filter = $("#filterByChanges").val();
    var detailId = $("#detailId").val();
    var toScreenName = $("#toScreenName").val();
    var fromScreenName = $("#fromScreen").val();
    window.parent.changeShortShipmentMenu(path, fileNumber, voyageId, filter, detailId, unitSsId, toScreenName, fromScreenName);
}
function shortShip() {
    var shortShip = $("#shortShip").val();
    var origin = getOrigin();
    var destination = getDestination();
    var rateType = $("input:radio[name='rateType']:checked").val();
    var fileNumberId = $("#fileNumberId").val();
    var path = $("#path").val();
    var moduleName = $("#moduleName").val();
    if (shortShip !== null && shortShip !== "") {
//newCommodity(path, fileNumberId, moduleId, 'false', moduleName,"true");
        var href = path + "/lclCommodity.do?methodName=editLclCommodity&id=0&editDimFlag=true&shortShipFileNo=" + shortShip;
        href = href + "&origin=" + origin + "&destination=" + destination + "&rateType=" + rateType + "&fileNumberId=" + fileNumberId;
        href = href + "&moduleName=" + moduleName + "&status=" + $("#fileStatus").val();
        href = href + "&disposition=" + $("#exportDisposition").val()
        $.colorbox({
            iframe: true,
            href: href,
            width: "90%",
            height: "90%",
            scrolling: false,
            title: "Commodity"
        });
    }
}
function showVoyageList() {
    var masterScheduleNo = $('#masterScheduleNo').val();
    var fileId = $('#fileNumberId').val();
    if (masterScheduleNo !== "") {
        $('#showAstarVoy').show();
        $('#upcomingSection').hide();
    }
    if (fileId === "" || masterScheduleNo === '') {
        $('#showAstarVoy').hide();
        $('#upcomingSection').show();
    }
}
function lockTransInExpBkg() {
    var fileNumber = $('#fileNumber').val();
    var moduleName = $('#moduleName').val();
    var bookingType = $('#bookingType').val();
    var fileStatus = $('#fileStatus').val();
    var fileType = $('#fileType').val();
    if (fileNumber != '' && moduleName == 'Exports' && bookingType == 'T') {
        if (fileType != 'BL' && (fileStatus == 'B')) {
            var form = document.getElementById('lclBookingForm');
            $('#commodity1').hide();
            $('.button-style2').hide();
            var element;
            for (var i = 0; i < form.elements.length; i++) {
                element = form.elements[i];
                if (element.type == "text" || element.type == "textarea") {
                    element.style.backgroundColor = "#CCEBFF";
                    element.readOnly = true;
                    element.style.borderTop = "0px";
                    element.style.borderBottom = "0px";
                    element.style.borderRight = "0px";
                    element.style.borderLeft = "0px solid";
                }
                if (element.type == "checkbox") {
//  element.style.border = 0;
                    element.disabled = true;
                }
            }
            var imgs = document.getElementsByTagName("img");
            for (var k = 0; k < imgs.length; k++) {
                if (imgs[k].id != "trianleicon" && imgs[k].id != "collpaseicon") {
                    imgs[k].style.visibility = "hidden";
                }
                if (imgs[k].id == "viewgif" || imgs[k].id == "viewgif1" || imgs[k].id == "viewgif2") {
                    imgs[k].style.visibility = "visible";
                }
            }
        }
    }
}
function movePlan(path, fileNumberId, fileNumber, pooId, fdId) {
    var href = path + "/lclBookingPlan.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&fromId=" + $(pooId).val() + "&toId=" + $(fdId).val();
    $(".movePlan").attr("href", href);
    $(".movePlan").colorbox({
        iframe: true,
        width: "80%",
        height: "80%",
        title: "Move Plan"
    });
}
/*Start: Add & delete Insurance charge*/
function addInsurance() {
    var insurance = $('input:radio[name=insurance]:checked').val();
    var fileId = $('#fileNumberId').val();
    if (insurance == "Y") {
        if (fileId === null || fileId === "" || fileId === "0" || fileId === undefined) {
            $.prompt("Please save Booking");
            $('#insuranceN').attr('checked', true);
            $("#valueOfGoods").val("");
        } else if (document.getElementById('commObj') === null) {
            $.prompt("Please add Weight and Measure from Commodity");
            $('#insuranceN').attr('checked', true);
            $("#valueOfGoods").val("");
        } else if ($('#valueOfGoods').val() == null || $('#valueOfGoods').val() == "") {
            $('#valueOfGoods').removeAttr("readonly");
            $("#valueOfGoods").removeClass("textlabelsBoldForTextBoxDisabledLook");
            $("#valueOfGoods").css("border-color", "red");
        }
    }
}
function cfclAccttypeCheck() {
    if ($('#disabledAccount').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#forwardAccount').val() + "</span>");
        $('#cfclAccount').val('');
        $('#cfclAccountNo').val('');
    }
}
function calculateInsuranceCharge() {
    var origin = getOrigin();
    var destination = getDestination();
    var pol = document.getElementById('polCode').value;
    var pod = document.getElementById('podCode').value;
    var fileId = $('#fileNumberId').val();
    var insurance = $('input:radio[name=insurance]:checked').val();
    if (insurance == "Y" && ($('#valueOfGoods').val() == null || $('#valueOfGoods').val() == "")) {
        $.prompt("Please Enter the Value of Goods");
        $("#valueOfGoods").css("border-color", "red");
        $("#warning").show();
    } else {
        submitAjaxFormForInsurance('calculateInsuranceCharge', '#lclBookingForm', '#chargeDesc', origin, destination, pol, pod, fileId, document.getElementById('valueOfGoods').value, insurance);
        $('#showAstar').show();
        $('#showAstarDestn').show();
        if ($('#portOfOriginR').val() != "") {
            $('#portOfOriginR').addClass("text-readonly");
            $('#portOfOriginR').attr("readonly", true);
        }
        $('#finalDestinationR').addClass("text-readonly");
        $('#finalDestinationR').attr("readonly", true);
    }
    setPreCobFn();
}

function removeInsuranceCharge() {
    var fileId = $('#fileNumberId').val();
    if (fileId != "") {
        var origin = getOrigin();
        var destination = getDestination();
        var pol = document.getElementById('polUnlocationcode').value;
        var pod = document.getElementById('podUnlocationcode').value;
        var insurance = $('input:radio[name=insurance]:checked').val();
        if (($("#valueOfGoods").val() != '') && (insurance == 'N')) {
            $.prompt("Are you sure you want to remove Insurance charge and Value of Goods", {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v == 1) {
                        showProgressBar();
                        submitAjaxFormForInsurance('calculateInsuranceCharge', '#lclBookingForm', '#chargeDesc', origin, destination, pol, pod, fileId, document.getElementById('valueOfGoods').value, insurance);
                        $("#valueOfGoods").val('');
                        $("#cifValue").val('');
                        $.prompt.close();
                    } else if (v == 2) {
                        $.prompt.close();
                        $('#insuranceY').attr('checked', true);
                    }
                }
            });
        }
        $("#valueOfGoods").addClass("textlabelsBoldForTextBoxDisabledLook");
        $('#valueOfGoods').attr("readonly", true);
        if ($("#valueOfGoods").val() === '') {
            $("#cifValue").val('');
        }
    }
}
/*End: Add & delete Insurance charge*/
/*Start: Add & delete Documentation charges*/
function addDocumentCharge(path) {
    var fileId = $('#fileNumberId').val();
    if (fileId === '' || fileId === '0') {
        $.prompt("Please Save Booking")
    } else {
        var fileNumber = $('#fileNumber').val();
        var billingType = $('input:radio[name=billForm]:checked').val();
        billingType = billingType != undefined ? billingType : 'F'
        var billToParty = $('input:radio[name=pcBoth]:checked').val();
        var href = path + "/lclCostAndCharge.do?methodName=displayDocum&fileNumberId=" +
                fileId + "&fileNumber=" + fileNumber + "&billToParty=" + billToParty + "&billingType=" + billingType
        $.colorbox({
            href: href,
            iframe: true,
            width: "32%",
            height: "35%",
            title: "Document Charges",
            onClosed: function () {
                var hiddenDocumCharge = $('#documSave').val();
                if (hiddenDocumCharge == 'S') {
                    $('#docYes').attr('checked', true);
                } else {
                    $('#docNo').attr('checked', true);
                }
            }
        });
    }
}
function removeDocumCharge() {
    var fileId = $('#fileNumberId').val();
    if (fileId != "") {
        $('#documSave').val('');
        var origin = getOrigin();
        var destination = getDestination();
        var pol = document.getElementById('polUnlocationcode').value;
        var pod = document.getElementById('podUnlocationcode').value;
        var documentation = $('input:radio[name=documentation]:checked').val();
        $.prompt("Are you sure you want to remove Documentation charge?", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    showProgressBar();
                    submitAjaxFormForInsurance('deleteDocumCharge', '#lclBookingForm', '#chargeDesc', origin, destination, pol, pod, fileId, documentation);
                    $.prompt.close();
                } else if (v == 2) {
                    $.prompt.close();
                    $('#docYes').attr('checked', true);
                }
            }
        });
    }
}
/*End: Add & delete Documentation charges*/
function submitAjaxFormForInsurance(methodName, formName, selector, origin, destination, pol, pod, fileId, valueOfGoods, insurance) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod + "&fileNumberId=" + fileId + "&valueOfGoods=" + valueOfGoods + "&insurance=" + insurance;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}
/*Start: Add & delete Delivery Metro PSRJU charges*/
function calculateDeliveryMetroCharge() {
    var hiddenDeliveryMetro = $('#deliveryMetroField').val();
    var deliveryMetro = $('input:radio[name=deliveryMetro]:checked').val();
    var fileId = $('#fileNumberId').val();
    if (fileId != "" && deliveryMetro !== hiddenDeliveryMetro) {
        var txt = 'The old Delivery charge will be removed from the charges.Are you sure You want to recalculate Rates?';
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    submitAjaxFormForDeliveryMetro('calculateDeliveryMetroCharge', '#lclBookingForm', '#chargeDesc', deliveryMetro, fileId);
                    var deliveryMetro = $('input:radio[name=deliveryMetro]:checked').val();
                    $('#deliveryMetroField').val(deliveryMetro);
                    $.prompt.close();
                } else if (v == 2) {
                    $('#deliveryMetroField').val(hiddenDeliveryMetro);
                    if (hiddenDeliveryMetro == "I") {
                        $('#deliveryMetroI').attr('checked', true);
                    } else if (hiddenDeliveryMetro == "O") {
                        $('#deliveryMetroO').attr('checked', true);
                    } else if (hiddenDeliveryMetro == "N") {
                        $('#deliveryMetroN').attr('checked', true);
                    }
                    $.prompt.close();
                }
            }
        });
    }
}
function submitAjaxFormForDeliveryMetro(methodName, formName, selector, deliveryMetro, fileId) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&deliveryMetro=" + deliveryMetro + "&fileNumberId=" + fileId;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}
/*End: Add & delete Delivery Metro PSRJU charges*/
/*Start: Add & delete calc and heavy charges*/
function addCalcHeavyCharges() {
    var fileId = $('#fileNumberId').val();
    if (fileId != "") {
        var calcHeavy = $('input:radio[name=calcHeavy]:checked').val();
        submitAjaxFormForCalcHeavy('calculateHeavyCharge', '#lclBookingForm', '#chargeDesc', fileId, calcHeavy);
    }
}

function removeCalcHeavyCharge() {
    var fileId = $('#fileNumberId').val();
    if (fileId != "") {
        var calcHeavy = $('input:radio[name=calcHeavy]:checked').val();
        $.prompt("Are you sure you want to remove Calc Heavy/Dense/ExLen?", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    showProgressBar();
                    submitAjaxFormForCalcHeavy('calculateHeavyCharge', '#lclBookingForm', '#chargeDesc', fileId, calcHeavy);
                    $.prompt.close();
                } else if (v == 2) {
                    $.prompt.close();
                    $('#calcYes').attr('checked', true);
                }
            }
        });
    }
}
function submitAjaxFormForCalcHeavy(methodName, formName, selector, fileId, calcHeavy) {
    var origin = getOrigin();
    var destination = getDestination();
    var pol = $('#polCode').val();
    var pod = $('#podCode').val();
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod + "&fileNumberId=" + fileId + "&calcHeavy=" + calcHeavy;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}
/*End: Add & delete calc and heavy charges*/
function showConsolidate(path, podId, fileNumber, fdId, fileNumberA, polId) {
    var href = path + "/lclConsolidate.do?methodName=display&podId=" + $(podId).val() + "&fileNumber=" + fileNumber + "&fdId=" + $(fdId).val();
    href = href + "&fileNumberId=" + fileNumberA + "&polId=" + polId;
    $(".consolidate").attr("href", href);
    $(".consolidate").colorbox({
        iframe: true,
        width: "90%",
        height: "90%",
        title: "Consolidate"
    });
}
function getBaseHsCodePopUp(path, fileNumberId) {
    var href = path + "/lclBooking.do?methodName=baseHsCodePopUp&fileNumberId=" + fileNumberId + "&fileNumber=" + $('#fileNumber').val();
    $(".consolidateHsCodes").attr("href", href);
    $(".consolidateHsCodes").colorbox({
        iframe: true,
        width: "40%",
        height: "60%",
        title: "Batch HS Codes"
    });
}

function showCargo(path, obj) {//open cargo PopUp
    var destination = $("#polUnlocationcode").val();
    var topButtonClass = $("#lclCargoButton").attr("class");
    var downButtonClass = $("#lclCargoButtonBottom").attr("class");
    if (topButtonClass.indexOf('green-background') !== -1 || downButtonClass.indexOf('green-background') !== -1) {
        var releaseClass = $("#lclReleaseButton1").attr('class') === 'green-background' ? true : false;
        var preReleaseClass = $("#lclPreReleaseButton1").attr('class') === 'green-background' ? true : false;
        if (releaseClass || preReleaseClass) {
            $.prompt("Cargo is already Verified");
        } else {
            $.prompt("Cargo is already Verified", {
                buttons: {
                    Ok: 1,
                    ReverseToOBKG: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        $.prompt.close();
                    } else {
                        reverseToOBKG();
                    }
                }
            });
        }
    } else {
        var fileId = $('#fileNumberId').val();
        if ($('#detailWarhouseNo').val() === "") {
            $.prompt("You cannot receive cargo unless there is a valid warehouse location on the commodity line");
            return false;
        } else {
            openCargoPopup(path, fileId, obj.id);
        }
    }
}
function reverseToOBKG() {
    var inlandSchNo = $('.scheduleNo').text();
    if (inlandSchNo != '' && inlandSchNo != null) {
        $.prompt("DR is picked in Inland Voyage");
        return false;
    }
    $.prompt("Are you sure?", {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                window.parent.showLoading();
                $("#methodName").val("reverseToOBKG");
                $("#lclBookingForm").submit();
            } else {
                $.prompt.close();
            }
        }
    });
}
function openCargoPopup(path, fileId, id) {
    var origin = getOrigin();
    var destination = getDestination();
    var rateType = $("input:radio[name='rateType']:checked").val();
    var bookingType = $("#bookingType").val();
    var status = $("#fileStatus").val();
    var pol = $("#polUnlocationcode").val();
    var disposition = $("#exportDisposition").val();
    var href = path + "/lclCargoPopup.do?methodName=display&fileId=" + fileId + "&origin=" + origin + "&destination=" + destination
            + "&rateType=" + rateType + "&bookingType=" + bookingType + "&status=" + status + "&disposition=" + disposition + "&pol=" + pol;
    $.colorbox({
        iframe: true,
        href: href,
        width: "35%",
        height: "40%",
        title: "Cargo Received"
    });
}
function setCargoStatus() {
    if ($("#fileStatus").val() === 'V') {
        $("#lclCargoButton").hide();
        $("#lclCargoButtonBottom").hide();
    }
    $('.shortShipBtn').hide();
    if (document.getElementById('commObj') === null) {
        hideReleaseBtn();
        hidePreReleaseBtn();
        hideCargoBtn();
    } else {
        var status = $('#fileStatus').val();
        var dispo = $("#dispoId").text();
        if ((status === 'W' && dispo === 'RUNV') || status === 'WU') {
            cargoUnVerifyBtnRed();
        } else if ((status === 'X' && status === 'W' && dispo === 'RCVD') || status === 'WV') {
            cargoVerifyBtnGreen();
            enableShotShipBtn();
        } else if (status !== 'X' && (status === 'WV' || status === 'W' || dispo === 'RCVD'
                || status === 'PR' || status === 'R' || status === 'L' || (status === 'M' && dispo !== 'OBKG'))) {
            cargoVerifyBtnGreen();
            enableShotShipBtn();
        }
        if ($('#lockMessage').val() !== '') {
            hideReleaseBtn();
            hidePreReleaseBtn();
        }
    }
}
function enableShotShipBtn() {
    $('.shortShipBtn').show();
}
function cargoUnVerifyBtnRed() {
    $("#lclCargoButton").removeClass("button-style1");
    $("#lclCargoButton").addClass("red-background");
    $("#lclCargoButtonBottom").removeClass("button-style1");
    $("#lclCargoButtonBottom").addClass("red-background");
}

function cargoVerifyBtnGreen() {
    $("#lclCargoButton").removeClass("button-style1");
    $("#lclCargoButton").removeClass("red-background");
    $("#lclCargoButton").addClass("green-background");
    $("#lclCargoButtonBottom").removeClass("button-style1");
    $("#lclCargoButtonBottom").removeClass("red-background");
    $("#lclCargoButtonBottom").addClass("green-background");
}
function cargoVerifyBtnBlue() {
    $("#lclCargoButton").removeClass("green-background");
    $("#lclCargoButton").addClass("button-style1");
    $("#lclCargoButtonBottom").removeClass("green-background");
    $("#lclCargoButtonBottom").addClass("button-style1");
}
function hideCargoBtn() {
    $("#lclCargoButton").hide();
    $("#lclCargoButtonBottom").hide();
}
function releaseBtnGreen() {
    $("#lclReleaseButton1").removeClass("button-style1");
    $("#lclReleaseButton1").addClass("green-background");
    $("#lclReleaseButton2").removeClass("button-style1");
    $("#lclReleaseButton2").addClass("green-background");
}
function releaseBtnBlue() {
    $("#lclReleaseButton1").removeClass("green-background");
    $("#lclReleaseButton1").addClass("button-style1");
    $("#lclReleaseButton2").removeClass("green-background");
    $("#lclReleaseButton2").addClass("button-style1");
}
function showReleaseBtn() {
    $("#lclReleaseButton1").show();
    $("#lclReleaseButton2").show();
}
function hideReleaseBtn() {
    $("#lclReleaseButton1").hide();
    $("#lclReleaseButton2").hide();
}

function isChecked3pRef(fileId, hotCodeType, hotCodeValue) {
    var flag;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO",
            methodName: "isHotCodeExistForThreeDigit",
            param1: fileId,
            param2: hotCodeType,
            param3: hotCodeValue,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}
function isInbondExists(fileId) {
    var flag;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO",
            methodName: "isInbondExists",
            param1: fileId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}
function poaRestrication() {
    var flag = true;
    var aesByEci = $('input:radio[name=aesBy]:checked').val();
    var shipperPoa = $('#shipperPoa').val();
    var consigneePoa = $('#consigneePoa').val();
    if (aesByEci === 'N') {
        flag = false;
    } else {
        if (shipperPoa === 'Y' || consigneePoa === 'Y') {
            flag = false;
        } else {
            flag = true;
        }
    }
    return flag;
}
function isLinkedDr(fileId) {
    var flag = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
            methodName: "getBkgLinkedByDetails",
            param1: fileId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data != null && data != "" && data[0] != null && data[0] != "" && data[1] != null) {
                flag = "Cannot perform Un-release Operation.DR is already linked to Unit#<span style=color:red>" + data[1] + "</span> of Voyage#<span style=color:red>" + data[0] + "</span>"
            } else {
                flag = "";
            }
        }
    });
    return flag;
}

function preReleaseBtnGreen() {
    $("#lclPreReleaseButton1").removeClass("button-style1");
    $("#lclPreReleaseButton1").addClass("green-background");
    $("#lclPreReleaseButton2").removeClass("button-style1");
    $("#lclPreReleaseButton2").addClass("green-background");
}
function preReleaseBtnBlue() {
    $("#lclPreReleaseButton1").removeClass("green-background");
    $("#lclPreReleaseButton1").addClass("button-style1");
    $("#lclPreReleaseButton2").removeClass("green-background");
    $("#lclPreReleaseButton2").addClass("button-style1");
}
function showPreReleaseBtn() {
    $("#lclPreReleaseButton1").show();
    $("#lclPreReleaseButton2").show();
}
function hidePreReleaseBtn() {
    $("#lclPreReleaseButton1").hide();
    $("#lclPreReleaseButton2").hide();
}

function consolidateRelease(data, fileId) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getconsolidatedfiles",
            param1: fileId
        },
        async: false,
        success: function (data) {
            if (data != null && data != "") {
                $.prompt("Please make sure of these files are also to be released " + data);
            }
        }
    });
}
function changeCurrentLocation(path) {
    var fileNumberId = $('#fileNumberId').val();
    var fileNumber = $('#fileNumber').val();
    $.prompt("Are you sure you want to change the Current Location?", {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                var oldCurreLoca = $("#oldCurrentLocationName").val()
                var href = path + "/lclBooking.do?methodName=openCurrentLocPopUp&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&oldCurreLoca=" + oldCurreLoca;
                $.colorbox({
                    iframe: true,
                    width: "30%",
                    height: "35%",
                    href: href,
                    title: "Current Location"
                });
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}

function checkingCode(fileId, destination, portOfDestination) {
    var codeValue = "";
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkHsCode",
            param1: fileId,
            param2: destination,
            param3: portOfDestination,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            codeValue = data;
        }
    });
    return codeValue;
}

function deleteConsolidate(fileNumberId, fileNumber, acceptance_remarks, current_file_id) {
    var txt = "";
    if (current_file_id !== "") {
        txt = "BL piece/cube/weight/charges have to be updated manually";
        deconsolidateWithBL(txt, fileNumberId, fileNumber, acceptance_remarks, "");
    } else {
        txt = "Are you sure you want to Deconsolidate";
        deconsolidateWithoutBL(txt, fileNumberId, fileNumber, acceptance_remarks, current_file_id);
    }
}

function deconsolidateWithBL(txt, fileNumberId, fileNumber, acceptance_remarks, current_file_id) {
    $.prompt(txt, {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                current_file_id = "";
                submitConsolidation(fileNumberId, fileNumber, acceptance_remarks, current_file_id);
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}

function deconsolidateWithoutBL(txt, fileNumberId, fileNumber, acceptance_remarks, current_file_id) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                submitConsolidation(fileNumberId, fileNumber, acceptance_remarks, current_file_id);
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}

function submitConsolidation(fileNumberId, fileNumber, acceptance_remarks, current_file_id) {
    showProgressBar();
    $("#methodName").val("deleteConsolidate");
    var params = $("#lclBookingForm").serialize();
    params += "&consolidateId=" + fileNumberId + "&fileNumber=" + fileNumber +
            "&acceptance_remarks=" + acceptance_remarks + "&current_file_id=" + current_file_id;
    $.post($("#lclBookingForm").attr("action"), params,
            function (data) {
                $("#consolidateDesc").html(data);
                $("#consolidateDesc", window.parent.document).html(data);
                hideProgressBar();
            });
}

function showCityName() {
    $('#basedOnCity').attr('checked', true);
}

function showRelayFd() {
    $('#finalDestinationR').val('');
    $('#finalDestinationId').val('');
    var origin = $('#portOfOriginR').val();
    var relay = $('input:radio[name=relayOverride]:checked').val();
    var nonRated = $('input:radio[name=nonRated]:checked').val();
    if (relay == 'Y') {
        if ($('#basedOnCity').is(":checked")) {
            if ($('#showFullRelayFd').is(":checked")) {
                $('#finalDestinationR').attr('alt', 'CONCAT_PORT_NAME');
            } else {
                $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
            }
        } else {
            if ($('#showFullRelayFd').is(":checked")) {
                $('#finalDestinationR').attr('alt', 'CONCAT_CITY_NAME');
            } else {
                $('#finalDestinationR').attr('alt', 'CONCAT_COUNTRY_NAME');
            }
        }
    } else {
        if (nonRated == 'N') {
            if ($('#basedOnCity').is(":checked")) {
                $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
            } else {
                $('#finalDestinationR').attr('alt', 'CONCAT_COUNTRY_NAME');
            }
        } else {
            if ($('#basedOnCity').is(":checked")) {
                if ($('#showFullRelayFd').is(":checked")) {
                    $('#finalDestinationR').attr('alt', 'CONCAT_PORT_NAME');
                } else {
                    $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
                }
            } else {
                if ($('#showFullRelayFd').is(":checked")) {
                    $('#finalDestinationR').attr('alt', 'CONCAT_CITY_NAME');
                } else {
                    $('#finalDestinationR').attr('alt', 'CONCAT_COUNTRY_NAME');
                }
            }
        }
    }
}

function setTraffiDetails() {
    var commodityNo = "";
    var rateType = $("input:radio[name='rateType']:checked").val();
    var clientCommodityNo = '', fwdCommodityNo = '', shipCommodityNo = '', consCommodityNo = '';
    if (rateType === 'R') {
        clientCommodityNo = $('#clientRetailNo').val() !== '000000' ? $('#clientRetailNo').val() : '';
        shipCommodityNo = $('#shipRetailNo').val() !== '000000' ? $('#shipRetailNo').val() : '';
        fwdCommodityNo = $('#fwdRetailNo').val() !== '000000' ? $('#fwdRetailNo').val() : '';
        consCommodityNo = $('#consRetailNo').val() !== '000000' ? $('#consRetailNo').val() : '';
    } else if (rateType === 'C') {
        clientCommodityNo = $('#clientColoadNo').val() !== '000000' ? $('#clientColoadNo').val() : '';
        shipCommodityNo = $('#shipColoadNo').val() !== '000000' ? $('#shipColoadNo').val() : '';
        fwdCommodityNo = $('#fwdColoadNo').val() !== '000000' ? $('#fwdColoadNo').val() : '';
        consCommodityNo = $('#consColoadNo').val() !== '000000' ? $('#consColoadNo').val() : '';
    }
    commodityNo = clientCommodityNo.length > 1 ? clientCommodityNo : shipCommodityNo.length > 1 ? shipCommodityNo : fwdCommodityNo.length > 1 ? fwdCommodityNo :
            consCommodityNo.length > 1 ? consCommodityNo : commodityNo;
    return commodityNo !== '' ? commodityNo.trim() : '';
}
function toggleVoyageInfo() {
    var disp = document.getElementById('upcomingSailing').style.display;
    if (disp == "block" || disp == "") {
        document.getElementById('exp').innerHTML = "";
        document.getElementById('col').innerHTML = "Click to Expand";
        jQuery('#upcomingSailing').slideToggle();
        $("#upcomingSailing").css("max-height", "100px");
        $("#upcomingSailing").css("overflow-y", "auto");
    } else {
        document.getElementById('col').innerHTML = "";
        document.getElementById('exp').innerHTML = "Click to Hide";
        jQuery('#upcomingSailing').slideToggle();
        $("#upcomingSailing").css("max-height", "100px");
        $("#upcomingSailing").css("overflow-y", "auto");
    }
}
function confirmBussinessUnit(ele, id) {
    if ($('#fileNumberId').val() !== '' && $('#fileNumberId').val() !== null) {
        if (getPreviousBusinessUnit($("#" + ele.id).val(), $('#fileNumberId').val())) {
            var txt = "Please note that the Brand is changing from <span style=color:red>"
                    + $("." + id).text().toUpperCase() + "</span> to<span style=color:red> " + $("." + ele.id).text().toUpperCase() + "</span>";
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        updateEconoOrEculine($("#" + ele.id).val(), $('#fileNumberId').val(), $("#loginUserId").val(), "DR-AutoNotes");
                        $("#" + ele.id).attr("checked", true);
                    } else {
                        $("#" + id).attr("checked", true);
                        $("#" + ele.id).attr("checked", false);
                    }
                }
            });
        }
    }
}
//  -- reverted due to  mantis item 10981 .
//function save3pRefNo(businessUnit) {
//    showProgressBar();
//    $('#methodName').val('addLcl3pReference');
//    $("#hotCodes").val("EBL/ECULINE BILL OF LADING");
//    var params = $("#lclBookingForm").serialize();
//    params += "&thirdPName=hotCodes&fileNumberId=" + $('#fileNumberId').val() + "&byHotCode=true"
//            + "&brandPreferences=None";
//    $.post($("#lclBookingForm").attr("action"), params, function (data) {
//        $("#hotCodesList").html(data);
//        $("#hotCodesList", window.parent.document).html(data);
//        $("#hotCodes").val("");
//        hideProgressBar();
//    });
//}
//
//function delete3pRefNo(ele) {
//    showProgressBar();
//    var refId = '';
//    $("." + ele).each(function () {
//        var code = $(this).val();
//        if ("EBL" === code.substring(code.indexOf(",") + 1, code.indexOf("/"))) {
//            refId = code.substring(0, code.indexOf(","));
//            $('#methodName').val('deleteLcl3pReference');
//            $("#hotCodes").val("EBL/ECULINE BILL OF LADING");
//            var params = $("#lclBookingForm").serialize();
//            params += "&thirdPName=hotCodes&fileNumberId=" + $('#fileNumberId').val() + "&lcl3pRefId=" + refId + "&byHotCode=true"
//                    + "&brandPreferences=None";
//            $.post($("#lclBookingForm").attr("action"), params, function (data) {
//                $("#hotCodesList").html(data);
//                $("#hotCodesList", window.parent.document).html(data);
//                $("#hotCodes").val("");
//                hideProgressBar();
//            });
//        }
//    });
//}
function checkEculine(acctNo, cType) {
    if ($("#fileType").val() !== 'BL' && $("#trmnum").val() !== '59') {
        var brand = getTradingPartnerPriority();
        if (brand === "Ecu Worldwide") {
            $("input:radio[name=businessUnit]").val(['ECU']);
        } else if (brand === "Econo") {
            $("input:radio[name=businessUnit]").val(['ECI']);
        } else if (brand === "OTI") {
            $("input:radio[name=businessUnit]").val(['OTI']);
        } else if (brand === "None") {
            if ($('#trmnum').val() === "59" && $(".econo").text() === "Econo") {
                $("input:radio[name=businessUnit]").val(['ECI']);
            }
        }
        var businessUnit = $('input:radio[name=businessUnit]:checked').val();
        if ($('#fileNumberId').val() !== '' && $('#fileNumberId').val() !== null) {
            showProgressBar();
            updateEconoOrEculine(businessUnit, $('#fileNumberId').val(), "", "DR-AutoNotes");
            hideProgressBar();
        }
    }
    if (cType !== "A") { // when select agent its no need.
        checkRateTypeDetails(acctNo, cType);
    }
    addClientHotCodes(acctNo);
}

function openConoslidationPopUp() {  // only for consolidation logic
    var fileNumberId = $("#fileNumberId").val();
    var consolidatedId = $("#consolidatedId").val();
    if ($("#toScreenName").val() === 'ConsolidatePopUp' && $("#fromScreen").val() === 'Booking') {
        var fileNumber = $("#fileNumber").val();
        var polId = $("#originId").val();
        $("#cont-2").click();
        var href = path + "/lclConsolidate.do?methodName=display&podId=" + $("#portOfDestinationId").val()
                + "&fileNumber=" + fileNumber + "&fdId=" + $('#finalDestinationId').val()
                + "&fileNumberId=" + fileNumberId + "&polId=" + polId + "&consolidatedId=" + consolidatedId;
        $.colorbox({
            iframe: true,
            href: href,
            width: "90%",
            height: "90%",
            title: "Consolidate",
            onClosed: function () {
                $("#consolidatedId").val("");
                $("#toScreenName").val('');
                $("#fromScreen").val('');
            }
        });
    }
}

function hazValidate(fileNumberId) {
    var hazStatus;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkHaz",
            param1: fileNumberId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            hazStatus = data;
        }
    });
    return hazStatus;
}
function setRelayOverRide() {
    if ($("#portOfOriginR").val() !== "" && $("#finalDestinationR").val() !== "") {
        if ($("#showOlder").prop("checked"))
        {
            $("#methodName").val('getRelayAndUpComingSailingsPrevious');
        } else {
            $("#previousSailing").val($("#showOlder").prop("checked"));
            $("#methodName").val('getRelayAndUpComingSailings');
        }

        var params = $('#lclBookingForm').serialize();
        $.ajax({
            type: "POST",
            url: $('#lclBookingForm').attr("action"),
            data: params,
            preloading: true,
            async: false,
            success: function (data) {
                $('#polPod').html(data.polPod);
                $('#upcomingSailing').html(data.upcomingSailings);
            }
        });
        fillPolPod();
        setDeliveryMetro();
        showHideRelayFd();
        clearBookedForVoyage();
    }
}
function setRelayDetails(textFieldValues) {
    var relayOverride = $('input:radio[name=relayOverride]:checked').val();
    if (relayOverride === 'N') {
        displayUpcomingSailings(textFieldValues); //Relay
    } else if (relayOverride === "Y" && textFieldValues === "relayOverride") {
        upcomingSailingsByRelayYes(textFieldValues); //Relay Override
    }
    if (textFieldValues === "destination") {
        setDefaultAgentSetUp();
    }
    setPreCobFn();
}
function displayUpcomingSailings(textFieldValues) {
    if (textFieldValues === "Origin") {
        showRelayFdForRelay();
        clearBookedForVoyage();
        fillPolPod();
    }
    if ($("#portOfOriginR").val() !== "" && $("#finalDestinationR").val() !== "") {
        if ($("#showOlder").prop("checked"))
        {
            $("#methodName").val('getRelayAndUpComingSailingsPrevious');
        } else {
            $("#previousSailing").val($("#showOlder").prop("checked"));
            $("#methodName").val('getRelayAndUpComingSailings');
        }
        var params = $('#lclBookingForm').serialize();
        $.ajax({
            type: "POST",
            url: $('#lclBookingForm').attr("action"),
            data: params,
            preloading: true,
            async: false,
            success: function (data) {
                $('#polPod').html(data.polPod);
                $('#upcomingSailing').html(data.upcomingSailings);
            }
        });
        showVoyageList();
        fillPolPod();
        destinationRemarks();
        setDeliveryMetro();
        showHideRelayFd();
        clearBookedForVoyage();
        setRelayOverRide();
    }
}

function setRelayDetailsPrevious() {
    var relayOverride = $('input:radio[name=relayOverride]:checked').val();
    if (relayOverride == 'N') {
        displayUpcomingSailingsPrevious(); //Relay
    } else {
        upcomingSailingsByRelayYesPrevious(); //Relay Override
    }
}

function displayUpcomingSailingsPrevious() {
    if ($("#portOfOriginR").val() !== "" && $("#finalDestinationR").val() !== "") {
        $("#methodName").val('getRelayAndUpComingSailingsPrevious');
        $("#previousSailing").val($("#showOlder").prop("checked"));
        var params = $('#lclBookingForm').serialize();
        $.ajax({
            type: "POST",
            url: $('#lclBookingForm').attr("action"),
            data: params,
            preloading: true,
            async: false,
            success: function (data) {
                $('#upcomingSailing').html(data.upcomingSailings);
            }
        });
    }
}

function setRelayOverRideYes() {
    changePort();
    setLoad();
    if ($("#portOfOriginR").val() !== "" && $('#portOfLoading').val() !== "" &&
            $('#portOfDestination').val() != "" &&
            $("#finalDestinationR").val() !== "") {
        $('#podFdTransTime').show();
    }
    setDefaultAgentSetUp();
}

function calculateRelayTTREVCharge() {
    var moduleName = $('#moduleName').val();
    var relay = $('input:radio[name=relayOverride]:checked').val();
    var fileId = $('#fileNumberId').val();
    var pooId = $("#portOfOriginId").val();
    var polId = $('#portOfLoadingId').val();
    var polUnloc = $('#polUnlocationcode').val();
    if (moduleName === 'Exports' && "" != fileId && relay !== undefined && relay === 'Y' && pooId !== polId) {
        $.prompt("Since the POL has changed, T&T Charges will be recalculated", {
            buttons: {
                Ok: 1
            },
            submit: function (v) {
                if (v === 1) {
                    showProgressBar();
                    submitAjaxFormForRelayTTREV('calculateTTREVCharge', '#lclBookingForm', '#chargeDesc', fileId, relay, polUnloc);
                    $.prompt.close();
                }
            }
        });
    }
    if ($('#hasBl').val() === "true") {
        $.prompt('Do you want to update the BL also?', {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    $('#methodName').val("changeBlFD");
                    var params = $("#lclBookingForm").serialize();
                    params += "fileNumberId=" + $('#fileNumberId').val();
                    $.post($("#lclBookingForm").attr("action"), params, function (data) {
                    });
                }
            }
        });
    }
}

function submitAjaxFormForRelayTTREV(methodName, formName, selector, fileId, relay, polUnloc) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&fileNumberId=" + fileId + "&relay=" + relay + "&polUnloc=" + polUnloc;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}

function upcomingSailingsByRelayYes(textFieldValues) {
    if ($("#portOfOriginR").val() !== "" && $('#portOfLoading').val() !== "" &&
            $('#portOfDestination').val() != "" &&
            $("#finalDestinationR").val() !== "") {
        $('#podFdTransTime').show();
    }
    if ($('#portOfDestinationId').val() !== "" && $('#portOfLoadingId').val() != "") {
        if ($("#showOlder").prop("checked"))
        {
            $("#methodName").val('getRelayOverrideUpcomingPrevious');
        } else {
            $("#previousSailing").val($("#showOlder").prop("checked"));
            $("#methodName").val('getRelayOverrideUpcoming');
        }
        var params = $("#lclBookingForm").serialize();
        params += "&podId=" + $('#portOfDestinationId').val() + "&polId=" + $('#portOfLoadingId').val();
        $.post($("#lclBookingForm").attr("action"), params, function (data) {
            $('#upcomingSailing').html(data);
            if ($('#eciVoyage').val() !== "" && $('#eciVoyage').val() != null) {
                $.prompt("Booked for voyage will be removed due to relay override");
                clearBookedForVoyage();
            }
        });
        fillPolPod();
        destinationRemarks();
    }
}

function upcomingSailingsByRelayYesPrevious() {
    if ($("#portOfOriginR").val() !== "" && $('#portOfLoading').val() !== "" &&
            $('#portOfDestination').val() !== "" &&
            $("#finalDestinationR").val() !== "") {
        $("#methodName").val('getRelayOverrideUpcomingPrevious');
        $("#previousSailing").val($("#showOlder").prop("checked"));
        var params = $("#lclBookingForm").serialize();
        params += "&podId=" + $('#portOfDestinationId').val() + "&polId=" + $('#portOfLoadingId').val();
        $.post($("#lclBookingForm").attr("action"), params, function (data) {
            $('#upcomingSailing').html(data);
        });
    }
}

function calculatePodFdTrans() {
    var pooId = $("#portOfOriginId").val();
    var polId = $('#portOfLoadingId').val();
    var podId = $('#portOfDestinationId').val();
    var fdId = $('#finalDestinationId').val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO",
            methodName: "validateRelayOverride",
            param1: pooId,
            param2: polId,
            param3: podId,
            param4: fdId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data) {
                $("#methodName").val('getRelayOverrideUpcoming');
                var params = $("#lclBookingForm").serialize();
                $.post($("#lclBookingForm").attr("action"), params, function (data) {
                    $('#upcomingSailing').html(data);
                });
            }
        }
    });
}

function calculates_Rates_When_Select_Customer(txt, commNo) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                $("#methodName").val("calculateRatesWhenSelectCustomer");
                var params = $("#lclBookingForm").serialize();
                params += "&commNo=" + commNo;
                $.post($("#lclBookingForm").attr("action"), params, function (data) {
                    if (data) {
                        $('#commodityDesc').html(data.commodityDesc);
                        $('#chargeDesc').html(data.chargeDesc);
                        hideProgressBar();
                    }
                });
            } else {
                $.prompt.close();
            }
        }
    });
}

//function validateHoldAndBookedVoyage() {
//    var flag = true;
//    var hold = $('#hold').val();
//    if (hold === 'Y') {
//        $.prompt("DR is on Hold.Cannot release");
//        flag = false;
//    } 
//    var bookedVoyage = $('#eciVoyage').val();
//    var cfcl = $('input:radio[name=cfcl]:checked').val();
//    if ('N' === cfcl) {
//        if (releaseRoleDuty() && bookedVoyage === '') {
//            $.prompt("OBooked for Voyage is required to Release");
//            flag = false;
//        }
//    }
//    return flag;
//}

function updateRelease(fileId, pooUnlocCode, buttonName, obj) {
    var isConsolidate = $("#conosolidateId tr").length > 1 ? true : false;
    if (isConsolidate) {
        validateConsolidatedDR(fileId, pooUnlocCode, buttonName, obj.id);
    } else {
        releaseDR(fileId, pooUnlocCode, buttonName, obj.id);
    }
}

function validateConsolidatedDR(fileId, pooUnlocCode, buttonName, buttonId) {
    if ($("#" + buttonId).attr('class') === 'green-background') {
        var message = isLinkedDr(fileId);
        if (message !== "") {
            $.prompt(message);
            return false
        }
    }
    if ($("#" + buttonId).attr('class') !== 'green-background') {
        var aesByEci = $('input:radio[name=aesBy]:checked').val();
        var shipperAcct = $('#shipperCodeClient').val();
        var consigneeAcct = $('#consigneeCodeClient').val();
        var destination = getDestination();
        var portOfDestination = getPortOfDestination();
        var errorMessage = releaseValidate(fileId, destination, portOfDestination,
                aesByEci, shipperAcct, consigneeAcct, buttonName);
        if (errorMessage !== "") {
            $.prompt(errorMessage);
            return false;
        }
        var consolidateFileNumber = getConsolidateFileNumber(fileId, "R", "true");
        var txt = "This Dock Receipt is consolidated with(<span class='red'>"
                + consolidateFileNumber + "</span>). Do you want to Release these DRs as well?";
        if (consolidateFileNumber !== '') {
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2,
                    Cancel: 3
                },
                submit: function (v) {
                    if (v === 1) {
                        releaseConsolidatedDR(fileId, pooUnlocCode, buttonName);
                    } else if (v === 2) {
                        releaseParentDR(fileId, pooUnlocCode, buttonName);
                    }
                }
            });
        } else {
            releaseDR(fileId, pooUnlocCode, buttonName, buttonId);
        }
    } else {
        var consolidateFile = consolidateFiles(fileId, "R", "false");
        var pickedConsolidate = getPickedFiles(consolidateFile[0]);
        var fileNumbers = "";
        var fId = "";
        consolidateFile = (consolidateFile == ',') ? "" : consolidateFile;
        pickedConsolidate = (pickedConsolidate == ',') ? "" : pickedConsolidate;
        pickedConsolidate = pickedConsolidate !== null ? pickedConsolidate : null;
        if (pickedConsolidate !== null && pickedConsolidate !== "") {
            var consId = consolidateFile[0];
            var PickId = pickedConsolidate[0];
            var consFileNo = consolidateFile[1];
            var PickFileNo = pickedConsolidate[1];
            var consolidateFileNo = new Array();
            var consolidateFileId = new Array();
            var pickedConsolidateFileNo = new Array();
            var pickedConsolidateFileId = new Array();
            consolidateFileNo = consFileNo.split(",");
            consolidateFileId = consId.split(",");
            pickedConsolidateFileId = PickId.split(",");
            pickedConsolidateFileNo = PickFileNo.split(",");
            for (i = 0; i < pickedConsolidateFileNo.length; i++) {
                consolidateFileNo.indexOf(pickedConsolidateFileNo[i]);
                consolidateFileNo.splice(consolidateFileNo.indexOf(pickedConsolidateFileNo[i]), 1);
            }
            for (i = 0; i < pickedConsolidateFileId.length; i++) {
                consolidateFileId.indexOf(pickedConsolidateFileId[i]);
                consolidateFileId.splice(consolidateFileId.indexOf(pickedConsolidateFileId[i]), 1);
            }
            consolidateFileId + "" + $("#fileNumberId").val();
        }

        $("#unPickedFiles").val(consolidateFileId);
        fileNumbers = pickedConsolidate !== null && pickedConsolidate !== "" ? consolidateFileNo : consolidateFile[1];
        fId = pickedConsolidate !== null && pickedConsolidate !== "" ? pickedConsolidateFileId : fileId;
        var txt = "This Dock Receipt is consolidated with(<span class='red'>"
                + fileNumbers + "</span>). Do you want to Un-Release these DRs as well?";
        if (fileNumbers != "" && fileNumbers !== undefined && fileNumbers !== null) {
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2,
                    Cancel: 3
                },
                submit: function (v) {
                    if (v === 1) {
                        unReleaseConsolidateDr(fId, pooUnlocCode, "UR");
                    } else if (v === 2) {
                        unReleaseParentDr(fileId, pooUnlocCode, "UR");
                    } else {
                        $.prompt.close();
                    }
                }
            });
        } else {
            releaseDR(fileId, pooUnlocCode, buttonName, buttonId);
        }
    }
}
function unReleaseConsolidateDr(fileId, pooUnlocCode, buttonName) {
    showProgressBar();
    $("#methodName").val("updateReleaseConsolidatedDR");
    var params = $("#lclBookingForm").serialize();
    params += "&fileId=" + fileId + "&unLocCode=" + pooUnlocCode + "&buttonName=" + buttonName;
    $.post($("#lclBookingForm").attr("action"), params, function (data) {
        if (data === 'UR') {
            setUnReleaseDateForReleaseButton();
            hideProgressBar();
        }
    });
}

function unReleaseParentDr(fileId, pooUnlocCode, buttonName) {
    showProgressBar();
    $("#methodName").val("updateRelease");
    var params = $("#lclBookingForm").serialize();
    params += "&fileId=" + fileId + "&unLocCode=" + pooUnlocCode + "&buttonName=" + buttonName;
    $.post($("#lclBookingForm").attr("action"), params, function (data) {
        if (data === 'UR') {
            setUnReleaseDateForReleaseButton();
            hideProgressBar();
        }
    });
}

function releaseConsolidatedDR(fileId, pooUnlocCode, buttonName) {
    var errorMessage = releaseValidateConsolidatedDR(fileId, buttonName);
    if (errorMessage !== "") {
        var releaseText = buttonName === "R" ? "Release" : "Pre-Release";
        $.prompt("Cannot " + releaseText + " the following DRs, please check: \n<br>" + errorMessage);
        return false;
    }
    showProgressBar();
    $("#methodName").val("updateReleaseConsolidatedDR");
    var params = $("#lclBookingForm").serialize();
    params += "&fileId=" + fileId + "&unLocCode=" + pooUnlocCode + "&buttonName=" + buttonName;
    $.post($("#lclBookingForm").attr("action"), params, function (data) {
        if (data === 'R') {
            releaseBtnGreen();
            $('#statuslabel').text('Released');
        }
        if (data === 'PR') {
            preReleaseBtnGreen();
            $('#statuslabel').text('Pre Released');
        }
        hideProgressBar();
    });
}
function releaseParentDR(fileId, pooUnlocCode, buttonName) {
    var releaseTxt = buttonName === "R" ? "Release" : "Pre-Release";
    var txt = "You will need to " + releaseTxt + " the other Dock Receipts manually";
    $.prompt(txt, {
        buttons: {
            Ok: 1
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                $("#methodName").val("updateRelease");
                var params = $("#lclBookingForm").serialize();
                params += "&fileId=" + fileId + "&unLocCode=" + pooUnlocCode + "&buttonName=" + buttonName;
                $.post($("#lclBookingForm").attr("action"), params, function (data) {
                    if (data === 'R') {
                        releaseBtnGreen();
                        $('#statuslabel').text('Released');
                    } else if (data === 'PR') {
                        preReleaseBtnGreen();
                        $('#statuslabel').text('Pre Released');
                    } else {
                        setUnReleaseDateForReleaseButton();
                    }
                    hideProgressBar();
                });
            }
        }
    });
}

function setUnReleaseDateForReleaseButton() {
    if ($("#dispoId").text() === 'RCVD') {
        $('#statuslabel').text('WAREHOUSE (Verified)');
    } else if ($("#dispoId").text() === 'RUNV') {
        $('#statuslabel').text('WAREHOUSE(Un-verified)');
    } else if ($("#dispoId").text() === 'OBKG') {
        var preReleaseClass = $("#lclPreReleaseButton1").attr('class') === 'green-background' ? true : false;
        if (preReleaseClass) {
            $('#statuslabel').text('WAREHOUSE (Pre Released)');
        } else {
            $('#statuslabel').text('Booking');
        }
    }
    releaseBtnBlue();
}
function releaseValidateConsolidatedDR(fileId, buttonValue) {
    var status = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "releaseValidateConsolidateDR",
            param1: fileId,
            param2: buttonValue,
            dataType: "json",
            request: "true"
        },
        async: false,
        success: function (data) {
            status = data;
        }
    });
    return status;
}


function getConsolidateFileNumber(fileId, buttonValue, isRelease) {
    var status = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO",
            methodName: "getReleaseORPreReleaseConsolidate",
            param1: fileId,
            param2: buttonValue,
            param3: isRelease,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            status = data[1];
        }
    });
    return status;
}


function releaseDR(fileId, pooUnlocCode, buttonName, buttonId) {
    var destination = getDestination();
    var portOfDestination = getPortOfDestination();
    if ($("#" + buttonId).attr('class') !== 'green-background') {
        var aesByEci = $('input:radio[name=aesBy]:checked').val();
        var shipperAcct = $('#shipperCodeClient').val();
        var consigneeAcct = $('#consigneeCodeClient').val();
        var errorMessage = releaseValidate(fileId, destination, portOfDestination,
                aesByEci, shipperAcct, consigneeAcct, buttonName);
        if (errorMessage !== "") {
            $.prompt(errorMessage);
            return false;
        }
    }

    if ($("#" + buttonId).attr('class') === 'green-background') {
        var message = isLinkedDr(fileId);
        if (message !== "") {
            $.prompt(message);
            return false;
        } else {
            confirmTORelease("Are you sure you want to Un-release?", fileId, pooUnlocCode, buttonName);
        }
    } else {
        confirmTORelease("Are you sure you want to release?", fileId, pooUnlocCode, buttonName);
    }
}

function confirmTORelease(txt, fileId, pooUnlocCode, buttonName) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                $("#methodName").val("updateRelease");
                var params = $("#lclBookingForm").serialize();
                params += "&fileId=" + fileId + "&unLocCode=" + pooUnlocCode + "&buttonName=" + buttonName;
                $.post($("#lclBookingForm").attr("action"), params, function (data) {
                    if (data === 'R') {
                        releaseBtnGreen();
                        $('#statuslabel').text('Released');
                    } else {
                        if ($("#dispoId").text() === 'RCVD') {
                            var preReleaseClass = $("#lclPreReleaseButton1").attr('class') === 'green-background' ? true : false;
                            if (preReleaseClass) {
                                $('#statuslabel').text('WAREHOUSE (Pre Released)');
                            } else {
                                $('#statuslabel').text('WAREHOUSE (Verified)');
                            }
                        } else if ($("#dispoId").text() === 'RUNV') {
                            var preReleaseClass = $("#lclPreReleaseButton1").attr('class') === 'green-background' ? true : false;
                            if (preReleaseClass) {
                                $('#statuslabel').text('WAREHOUSE (Pre Released)');
                            } else {
                                $('#statuslabel').text('WAREHOUSE(Un-verified)');
                            }
                        } else if ($("#dispoId").text() === 'OBKG') {
                            var preReleaseClass = $("#lclPreReleaseButton1").attr('class') === 'green-background' ? true : false;
                            if (preReleaseClass) {
                                $('#statuslabel').text('WAREHOUSE (Pre Released)');
                            } else {
                                $('#statuslabel').text('Booking');
                            }
                        }
                        releaseBtnBlue();
                    }
                    hideProgressBar();
                });
            }
        }
    });
}

/* Regarding mantis item# 10057 please verify before remove  */
function updatePreRelease(fileId, pooUnlocCode, buttonName, obj) {
    var txt = "";
    if ($("#lclReleaseButton1").attr('class') === 'green-background'
            && $("#lclPreReleaseButton1").attr('class') === 'button-style1') {
        $.prompt("After Release the DR,Can not proceed Pre-Release.");
        return false;
    }
    if ($("#lclReleaseButton1").attr('class') === 'green-background' && $("#" + obj.id).attr('class') === 'green-background') {
        $.prompt("Please Un-release to proceed Un-prerelease.");
        return false;
    }

    if ($("#" + obj.id).attr('class') === 'green-background') {
        txt = "Are you sure you want to Un-prerelease?"
        preReleaseDR(fileId, pooUnlocCode, buttonName, txt);
    } else {
        txt = "Are you sure you want to Pre-Release?"
        var aesByEci = $('input:radio[name=aesBy]:checked').val();
        var shipperAcct = $('#shipperCodeClient').val();
        var consigneeAcct = $('#consigneeCodeClient').val();
        var destination = getDestination();
        var portOfDestination = getPortOfDestination();
        var errorMessage = releaseValidate(fileId, destination, portOfDestination,
                aesByEci, shipperAcct, consigneeAcct, "Pre-");
        if (errorMessage !== "") {
            $.prompt(errorMessage);
            return false;
        }
        var isConsolidate = $("#conosolidateId tr").length > 1 ? true : false;
        if (isConsolidate && $("#" + obj.id).attr('class') !== 'green-background') {
            var fileNos = getConsolidateFileNumber(fileId, "PR", "true");
            if (fileNos !== '') {
                var txt = "This Dock Receipt is consolidated with(<span class='red'>" + fileNos + "</span>). Do you want to Pre-Release these DRs as well?";
                $.prompt(txt, {
                    buttons: {
                        Yes: 1,
                        No: 2,
                        Cancel: 3
                    },
                    submit: function (v) {
                        if (v === 1) {
                            releaseConsolidatedDR(fileId, pooUnlocCode, buttonName);
                        } else if (v === 2) {
                            releaseParentDR(fileId, pooUnlocCode, buttonName);
                        }
                    }
                });
            } else {
                preReleaseDR(fileId, pooUnlocCode, buttonName, txt);
            }
        } else {
            preReleaseDR(fileId, pooUnlocCode, buttonName, txt);
        }
    }

}

function preReleaseDR(fileId, pooUnlocCode, buttonName, txt) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                $("#methodName").val("updateRelease");
                var params = $("#lclBookingForm").serialize();
                params += "&fileId=" + fileId + "&unLocCode=" + pooUnlocCode + "&buttonName=" + buttonName;
                $.post($("#lclBookingForm").attr("action"), params, function (data) {
                    if (data === 'PR') {
                        $('#statuslabel').text('WAREHOUSE (Pre Released)');
                        preReleaseBtnGreen();
                    } else {
                        if ($("#dispoId").text() === 'RCVD') {
                            $('#statuslabel').text('WAREHOUSE (Verified)');
                        } else if ($("#dispoId").text() === 'RUNV') {
                            $('#statuslabel').text('WAREHOUSE(Un-verified)');
                        } else if ($("#dispoId").text() === 'OBKG') {
                            $('#statuslabel').text('Booking');
                        }
                        preReleaseBtnBlue();
                    }
                    hideProgressBar();
                });
            }
        }
    });
}


function releaseValidate(fileId, fd, pod, aesByEci, shipperAcct, consigneeAcct, buttonName) {
    var hazStatus = "";
    var hazmat;
    var hold = $('#hold').val();
    var cfcl = $('input:radio[name=cfcl]:checked').val();
    var bookedVoyage = $('#eciVoyage').val();
    var flag = false;
    var isHazmat = $('#hazmatFound').is(':empty');
    if (cfcl === "N") {
        if (releaseRoleDuty() && bookedVoyage === '') {
            flag = true;
        }
    }
    if (!isHazmat) {
        hazmat = true;
    } else {
        hazmat = false;
    }
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "releaseValidate",
            param1: fileId,
            param2: fd,
            param3: pod,
            param4: aesByEci,
            param5: shipperAcct,
            param6: consigneeAcct,
            param7: buttonName,
            param8: hazmat,
            param9: hold,
            param10: flag,
            dataType: "json",
            request: "true"
        },
        async: false,
        success: function (data) {
            hazStatus = data;
        }
    });
    return hazStatus;
}

function ratesNotAccepted(current_file_Id, remarks) {
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.lcl.bc.ExportBookingUtils",
            methodName: "addRemarksForConsolidationFiles",
            param1: current_file_Id,
            param2: remarks,
            param3: $("#loginUserId").val(),
            dataType: "json"
        },
        close: function (data) {
        }
    });
}
function spotRateByNo(ele) {
    var spot = ele.value === 'Y' ? true : false;
    var existSpotRate = $('#existSpotRate').val() === 'true' ? true : false;
    if (spot === existSpotRate) {
        return false;
    }
    var txt = "Are you sure want to change spotrate to No?";
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                $.prompt.close();
                showProgressBar();
                var originCode = $('#originUnlocationCode').val();
                var pol = $('#polUnlocationcode').val();
                var spotRate = $('input:radio[name=spotRate]').val();
                var pod = $('#podUnlocationcode').val();
                var destinationCode = $('#unlocationCode').val();
                var pickUpValues = $('#doorOriginCityZip').val();
                $("#methodName").val('recaculateBySpotRate');
                var params = $('#lclBookingForm').serialize();
                params += "&originCode=" + originCode + "&destinationCode=" + destinationCode + "&spotRate=" + spotRate
                        + "&polUnlocationcode=" + pol + "&podUnlocationcode=" + pod + "&doorOriginCityZip=" + pickUpValues;
                $.post($('#lclBookingForm').attr("action"), params, function (data) {
                    $('#chargeDesc').html(data.chargeDesc);
                    $('#commodityDesc').html(data.commodityDesc);
                    $('#lclSpotRate').removeClass("green-background");
                    $('#lclSpotRate').addClass("button-style1");
                    $("#lclSpotRate").hide();
                    $('#existSpotRate').val("false");
                    $("#spotratelabel").text('');
                    $("#externalComment").val('');
                    hideProgressBar();
                });
            } else {
                $('input:radio[name=spotRate]').val(['Y']);
                $.prompt.close();
            }
        }
    });
}
function spotRate_yes(ele) {
    var spot = ele.value === 'Y' ? true : false;
    var final_dest = $('#finalDestinationR').val();
    var file_Id = $('#fileNumberId').val();
    if (spot && file_Id !== "") {
        var existSpotRate = $('#existSpotRate').val() === 'true' ? true : false;
        if (spot === existSpotRate) {
            return false;
        }
        if (final_dest === null || final_dest === "") {
            $.prompt("Destination is required");
            $('input:radio[name=spotRate]').val(["N"]);
            $("#finalDestinationR").css("border-color", "red");
            return false;
        }
        if (document.getElementById("commObj") === null) {
            $.prompt("Please add Weight and Measure from Commodity");
            $('input:radio[name=spotRate]').val(["N"]);
            return false;
        }
        if ($("#commObj tr").length >= 3) {
            $.prompt("Only One commodity is allowed for spotrate");
            $('input:radio[name=spotRate]').val(["N"]);
            return false;
        }
        $('#existSpotRate').val("true");
        $("#lclSpotRate").show();
        $("#spotratelabel").text('');
    } else {
        $.prompt("Please Save the file before adding SpotRate");
        $('input:radio[name=spotRate]').val(["N"]);
    }
}

function setDefaultAgentSetUp() {
    var brand = getTradingPartnerPriority();
    if (brand === "None") {
        var fdUnlocationcode = $('#podUnlocationcode').val();
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO",
                methodName: "getDefaultPortSetUpCode",
                param1: fdUnlocationcode,
                dataType: "json",
                async: false
            },
            success: function (data) {
                var id = "";
                if ($("#fileType").val() !== 'BL' && $("#trmnum").val() !== '59') {
                    if (data === 'B') {
                        $("input:radio[name=businessUnit]").val([$("#econo").val()]);
                        id = "econo";
                    } else {
                        $("input:radio[name=businessUnit]").val([$("#eculine").val()]);
                        id = "eculine";
                    }
                    if (data !== null && $('#fileNumberId').val() !== '') {
                        updateEconoOrEculine($("#" + id).val(), $('#fileNumberId').val(), "", "DR-AutoNotes");
                    }
                }
            }
        });
    }
}
function setEconoEcuWorldWide() {
    if ($("#fileType").val() === 'BL') {
        $(".businessUnit").attr("disabled", true);
    }
}

function showManualDoorCity(ele) {
    if ($("#" + ele.id).is(":checked")) {
        $("#doorOriginCityZip").hide();
        $("#manualDoorOriginCityZip").show();
        $("#manualDoorOriginCityZip").removeClass("textlabelsBoldForTextBoxDisabledLook");
        $("#manualDoorOriginCityZip").val('');
        $("#doorOriginCityZip").val('');
        $("#pickupCity").val('');
        $("#pickupState").val('');
        $("#pickupZip").val('');
        $('#pickupInfo').show();
    } else {
        $("#doorOriginCityZip").show();
        $("#doorOriginCityZip").val('');
        $("#manualDoorOriginCityZip").hide();
        $("#manualDoorOriginCityZip").val('');
    }
}

function changeDrNumber(path, fileId, fileNumber) {
    $.prompt("This feature will allow you to change the D/R number, are you sure", {
        buttons: {
            Yes: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                $.prompt.close();
                getDrNumber(path);
            } else {
                $.prompt.close();
            }
        }
    });
}

function getDrNumber(path) {
    $.colorbox({
        iframe: true,
        href: path + "/jsps/LCL/changeExportDRNumber.jsp",
        width: "35%",
        height: "40%",
        title: "Change DR Number",
        onclosed: function (v) {

        }
    });
}

function cfclVoyage() {
    cfclOverrideBooking();
    var relayOverride = $('input:radio[name=relayOverride]:checked').val();
    var cfcl = $('input:radio[name=cfcl]:checked').val();
    if (relayOverride === 'N') {
        if ($("#portOfOriginR").val() !== "" && $("#finalDestinationR").val() !== "") {
            if ($("#showOlder").prop("checked"))
            {
                $("#methodName").val('getRelayAndUpComingSailingsPrevious');
            } else {
                $("#previousSailing").val($("#showOlder").prop("checked"));
                $("#methodName").val('getRelayAndUpComingSailings');
            }
            var bookedVoyage = $("#eciVoyage").val();
            if (bookedVoyage !== "") {
                var bookVoy = $.prompt("Booked for voyage will be removed due to CFCL");
                if (bookVoy !== null) {
                    clearBookedForVoyage();
                    // document.getElementById('clearRates').style.visibility = "hidden";
                    // $('#upcomingSailing').html(data);
                }
            }
            var params = $('#lclBookingForm').serialize();
            $.ajax({
                type: "POST",
                url: $('#lclBookingForm').attr("action"),
                data: params,
                preloading: true,
                async: false,
                success: function (data) {
                    $('#polPod').html(data.polPod);
                    $('#upcomingSailing').html(data.upcomingSailings);
                }
            });
            showVoyageList();
        }
    } else {
        if ($("#portOfOriginR").val() !== "" && $('#portOfLoading').val() !== "" &&
                $('#portOfDestination').val() != "" &&
                $("#finalDestinationR").val() !== "") {
            $('#podFdTransTime').show();
        }
        if ($('#portOfLoading').val() !== "" && $('#portOfDestination').val() != "") {
            if ($("#showOlder").prop("checked"))
            {
                $("#methodName").val('getRelayOverrideUpcomingPrevious');
            } else {
                $("#previousSailing").val($("#showOlder").prop("checked"));
                $("#methodName").val('getRelayOverrideUpcoming');
            }
            var bookedVoyage = $("#eciVoyage").val();
            if (bookedVoyage !== "") {
                var bookVoy = $.prompt("Booked for voyage will be removed due to CFCL");
                if (bookVoy !== null) {
                    clearBookedForVoyage();
                }
            }
            var params = $("#lclBookingForm").serialize();
            $.post($("#lclBookingForm").attr("action"), params, function (data) {
                $('#upcomingSailing').html(data);
            });
            showVoyageList();
        }

    }
    if (cfcl === "Y") {
        changePort();
    }
}


function validateBillToForConvertBl() {
    var billToCount = new Array();
    var charegCount = new Array();
    var flag = true;
    $(".billToChargeY").each(function () {
        if ($(this).text().trim().trim() !== '') {
            billToCount.push($(this).text().trim().trim());
        }
    });
    $(".chargeAmount").each(function () {
        if ($(this).text().trim().trim() !== '') {
            charegCount.push($(this).text().trim().trim());
        }
    });
    if (charegCount.length !== billToCount.length) {
        flag = false;
    }
    return flag;
}

function disabledExpBillToCode() {
    $('#radioP').attr('disabled', true);
    $('#radioC').attr('disabled', true);
    $("#radioB").attr("disabled", true);
    $('#billS').attr('disabled', true);
    $("#billT").attr("disabled", true);
    $("#billF").attr("disabled", true);
    $("#billA").attr("disabled", true);
}
function setCfclLabelColor() {
    var cfcl = $('input:radio[name=cfcl]:checked').val();
    if (cfcl === 'Y') {
        document.getElementById('cfclLabel').style.color = "blue";
        document.getElementById('cfclLabel').style.fontWeight = "bold";
    } else {
        document.getElementById('cfclLabel').style.color = "black";
    }
}

function calculateRates() {
    var fileId = $("#fileNumberId").val();
    if (fileId === "") {
        return false;
    }
    var spotRate = $('input:radio[name=spotRate]:checked').val();
    if (spotRate === 'Y') {
        $.prompt('Cannot change rates as Spot Rate is set to Yes');
        return false;
    }
    var insurance = $('input:radio[name=insurance]:checked').val();
    if (insurance === "Y" && (document.getElementById('valueOfGoods') === null || document.getElementById('valueOfGoods').value === "")) {
        $.prompt("Please enter the Value of Goods");
        $("#valueOfGoods").css("border-color", "red");
        return false;
    }
    if (document.getElementById("commObj") === null) {
        $.prompt("Please add Weight and Measure from Commodity");
        return false;
    }
    var isHazmat = $('#hazmatFound').is(':empty');
    // alert(isHazmat);
    if (!isHazmat) {
        checkHazDetails();
    } else {
        if ($('#hasBl').val() === "true") {
            changeFdInBl();
        } else {
            ratesRecalculation();
        }
    }
}
function ratesRecalculation() {
    var txt = "Are you sure want to recalculate Rates?";
    var hasBl = $('#hasBl').val();
    var calcBlRates = $('#calcBlRates').val();
    if (hasBl === 'true' && calcBlRates === "true") {
        txt = "Are you sure want to recalculate Rates for Bl Also?";
    }
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                if (hasBl === 'true' && calcBlRates === "true") {
                    $('#changeBlCharge').val(true);
                } else {
                    $('#changeBlCharge').val(false);
                }
                var origin = getUnlocationCode('portOfOriginR');
                var destination = getUnlocationCode('finalDestinationR');
                var pol = getUnlocationCode('portOfLoading');
                var pod = getUnlocationCode('portOfDestination');
                submitAjaxForm("calculateCharges", '#lclBookingForm', '#chargeDesc', origin, destination,
                        pol, pod, "", "", "");
                $('#showAstar').show();
                $('#showAstarDestn').show();
                if ($('#portOfOriginR').val() !== "") {
                    $('#portOfOriginR').addClass("text-readonly");
                    $('#portOfOriginR').attr("readonly", true);
                    var origin = $("#portOfOriginR").val();
                    $(".ChargeOrigin").text(origin.substring(0, origin.indexOf("/")));
                }
                $('#finalDestinationR').addClass("text-readonly");
                $('#finalDestinationR').attr("readonly", true);
                var destination = $("#finalDestinationR").val();
                $(".ChargeDest").text(destination.substring(0, destination.indexOf("/")));
                var deliveryMetro = $('input:radio[name=deliveryMetro]:checked').val();
                $('#deliveryMetroField').val(deliveryMetro);
                setPreCobFn();
                $.prompt.close();
            } else {
                $.prompt.close();
            }
        }
    });
}
function checkHazDetails() {
    var fileId = $('#fileNumberId').val();
    var pod = getUnlocationCode('portOfDestination');
    var destination = getUnlocationCode('finalDestinationR');
    var fdCode = destination === "" ? pod : destination;
    var podCode = pod === "" ? destination : pod;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getHazardousStatus",
            param1: podCode,
            param2: fdCode,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data[0] === "N") {
                var txt = "We do not accept hazardous cargo to this market:" + data[1];
                hazMessage(txt, false, fileId);
            } else if (data[0] === "R") {
                hazMessage("Hazardous Restrictions apply for :" + data[1]
                        + ". Please confirm that cargo is acceptable for export and determine proper charges.",
                        true, fileId);
            }
        }
    });
}



function hazMessage(txtMsg, hazFlag, fileId) {
    $.prompt(txtMsg, {
        buttons: {
            Ok: 1
        },
        async: false,
        submit: function (v) {
            if (v === 1 && hazFlag) {
                ratesRecalculation();
            } else {
                $('.hazmat').hide();
                $('#hazmatFound').text('');
                updateHazmatValue(fileId, "0");
            }
        }
    });
}

function updateHazmatValue(fileId, hazValue) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO",
            methodName: "updateHazMatValue",
            param1: fileId,
            param2: "hazmat",
            param3: hazValue,
            param4: $("#loginUserId").val(),
            dataType: "json"
        },
        async: false,
        success: function (data) {
            $('.hazmat').val('');
            showLoading();
            $('#methodName').val("refreshCommodity");
            var params = $("#lclBookingForm").serialize();
            params += "fileNumberId=" + fileId;
            $.post($("#lclBookingForm").attr("action"), params, function (data) {
                $("#commodityDesc").html(data);
                $("#commodityDesc", window.parent.document).html(data);
            });
            closePreloading();
            ratesRecalculation();
        }
    });
}
function updatePOD() {
    var pod = $("#portOfDestinationId").val();
    var fileId = $('#fileNumberId').val();
    var fileType = $('#fileType').val();
    var bookingType = $('#bookingType').val();
    var relay = $('input:radio[name=relayOverride]:checked').val();
    if (fileType === "BL" && fileId !== '') {
        relay = relay == 'Y' ? 1 : 0;
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO",
                methodName: "updatePod",
                param1: fileId,
                param2: pod,
                param3: bookingType,
                param4: relay,
                request: "true",
                dataType: "json"
            },
            async: false,
            close: function (data) {
            }
        });
    }
}

function cfclOverrideBooking() {
    var cfcl = $('input:radio[name=cfcl]:checked').val();
    var bookingType = $("#bookingType").val();
    setCfclValues();
    if (cfcl === "N" && bookingType !== "T") {
        $("#portOfLoading").val('');
        $("#portOfLoadingId").val('');
        $("#polUnlocationcode").val('');
        $("#portOfDestination").val('');
        $("#portOfDestinationId").val('');
        $("#podUnlocationcode").val('');
        $('input:radio[name=relayOverride]').val(["N"]);
    }
}
function setCfclValues() {
    var cfcl = $('input:radio[name=cfcl]:checked').val();
    var bookingType = $("#bookingType").val();
    if (cfcl === "Y" && bookingType !== "T") {
        var portOfLoading = $("#portOfLoading").val();
        var portOfDestination = $("#portOfDestination").val();
        if (portOfLoading === "") {
            $("#portOfLoading").val($("#portOfOriginR").val());
            $("#portOfLoadingId").val($("#portOfOriginId").val());
            $("#polUnlocationcode").val($("#originUnlocationCode").val());
        }
        if (portOfDestination === "") {
            $("#portOfDestination").val($("#finalDestinationR").val());
            $("#portOfDestinationId").val($("#finalDestinationId").val());
            $("#podUnlocationcode").val($("#unlocationCode").val());
        }
        $('input:radio[name=relayOverride]').val(["Y"]);
    }
}
function newTPAccountFromBooking(path, vendorId, addressId, acctType, countryId,
        cityId, stateId, zipId, phoneId, faxId, emailId, id1, id2, unlocId, imageId) {
    if ($("#" + id1).val() === '' && $("#" + imageId).is(":checked")) {
        var address = encodeURIComponent($("#" + addressId).val());
        var city = $("#" + cityId).val();
        var country = $("#" + countryId).val();
        var zip = $("#" + zipId).val();
        var state = $("#" + stateId).val();
        var vendorName = $("#" + vendorId).val();
        var phone = $("#" + phoneId).val();
        var fax = $("#" + faxId).val();
        var email = $("#" + emailId).val();
        var unLocCode = $("#" + unlocId).val();
        if ($("#fileNumberId").val() === '') {
            $.prompt("Please save the file.");
            return false;
        }
        if (vendorName === '') {
            $.prompt("Please enter vendor name.");
            return false;
        } else if (address === '' && city === '') {
            $.prompt("Please enter address and city.");
            return false;
        } else if (address === '') {
            $.prompt("Please enter address.");
            return false;
        } else if (city === '') {
            $.prompt("Please enter city.");
            return false;
        }

        var params = "buttonValue=LCL_EXPORT" + "&accountName=" + vendorName
                + "&accountType=" + acctType + "&address=" + address
                + "&address1=" + address + "&city=" + city
                + "&state=" + state + "&zip=" + zip + "&country=" + country
                + "&fax=" + fax + "&phone=" + phone + "&email=" + email + "&unLocCode=" + unLocCode + "&salesCode=";
        var cutstomerAcct = acctType === "S" ? "Shipper" : acctType === "C" ? "Consignee" : "";
        $.prompt("A new <b class='red'>" + cutstomerAcct + "</b> Account will be created as you sure you want to continue?", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    $.ajax({
                        type: "POST",
                        url: path + "/lclNewTPDetails.do",
                        data: params,
                        preloading: true,
                        async: false,
                        success: function (data) {
                            if (data.accountNo !== "") {
                                $("#" + id1).val(data.accountNo);
                                $("#" + id2).val(data.accountNo);
                                showProgressBar();
                                $('#methodName').val("saveBooking");
                                var params = $("#lclBookingForm").serialize();
                                $.post($("#lclBookingForm").attr("action"), params, function (data) {
                                    hideProgressBar();
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}
function getBaseHotCodePopUp(path, fileNumberId) {
    var href = path + "/lclBooking.do?methodName=baseHotCodePopUp&fileNumberId=" + fileNumberId + "&fileNumber=" + $('#fileNumber').val();
    $(".consolidateHotCodes").attr("href", href);
    $(".consolidateHotCodes").colorbox({
        iframe: true,
        width: "40%",
        height: "60%",
        title: "Batch HOT Codes"
    });
}

function changeDisposition(path) {
    var fileNumberId = $('#fileNumberId').val();
    var fileNumber = $('#fileNumber').val();
    var href = path + "/lclBooking.do?methodName=openDispositionPopUp&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber;
    $.colorbox({
        iframe: true,
        width: "55%",
        height: "50%",
        href: href,
        title: "Change Disposition"
    });
    $.prompt.close();
}

function checkRoleDuty() {
    var origin = getOrigin();
    $('input:radio[name=pwk]').val(["N"]);
    if (origin !== undefined && origin !== "") {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.struts.action.lcl.LclBookingAction",
                methodName: "checkRoleDuty",
                param1: origin,
                request: "true",
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data === "true") {
                    $('input:radio[name=pwk]').val(["Y"]);
                }
            }
        });
    }
}

function consolidateFiles(fileId, buttonValue, isRelease) {
    var status = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO",
            methodName: "getReleaseORPreReleaseConsolidate",
            param1: fileId,
            param2: buttonValue,
            param3: isRelease,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            status = data;
        }
    });
    return status;
}
function getPickedFiles(consolidateFileNumber) {
    var cons = consolidateFileNumber;
    var pickedFile;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
            methodName: "getPickedVoyage",
            param1: cons,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            pickedFile = data;
        }
    });
    return pickedFile;
}


function setPreCobFn() {
    var cob = $('#blUnitCob').val() === '' ? false : $('#blUnitCob').val();
    if (cob == 'true') {
        disableOrgAndFd("This file has been COB");
        jQuery(document).ready(function () {
            $("#basedOnCity").attr("disabled", true);
            $("#showFullRelay").attr("disabled", true);
            $("#portOfLoading").addClass("text-readonly");
            $("#portOfLoading").attr("readonly", true);
            $("#portOfDestination").addClass("text-readonly");
            $("#portOfDestination").attr("readonly", true);
            $("#finalDestinationR").addClass("text-readonly");
            $("#finalDestinationR").attr("readonly", true);
        });
        return false;
    }
    var picked = $('#isPicked').val();
    var polId = $('#portOfLoadingId').val();
    var podId = $('#portOfDestinationId').val();
    var pickPolId = $('#pickedOrginId').val();
    var pickPodId = $('#pickedDestId').val();
    if (picked === 'true' && cob !== 'true' && (polId == pickPolId) && (podId == pickPodId)) {
        disableOrgAndFd("This file has been picked");
        jQuery(document).ready(function () {
            $("#finalDestinationR").removeClass("text-readonly");
            $('#finalDestinationR').removeAttr("readonly");
            $("#portOfDestination").addClass("text-readonly");
            $("#portOfDestination").attr("readonly", true);
            $("#portOfLoading").addClass("text-readonly");
            $("#portOfLoading").attr("readonly", true);
        });
    } else if (picked === 'true') {
        jQuery(document).ready(function () {
            $("#portOfLoading").addClass("text-readonly");
            $("#portOfLoading").attr("readonly", true);
            $("#portOfDestination").addClass("text-readonly");
            $("#portOfDestination").attr("readonly", true);
            $("#finalDestinationR").addClass("text-readonly");
            $("#finalDestinationR").attr("readonly", true);
        });
    } else {
        jQuery(document).ready(function () {
            $("#finalDestinationR").removeClass("text-readonly");
            $('#finalDestinationR').removeAttr("readonly");
        });
    }
}
function disableOrgAndFd(title) {
    $("#clearRatesDestn").attr("title", title);
    $("#clearRatesDestn").attr("onclick", null);
    $("#clearRates").attr("title", title);
    $("#clearRates").attr("onclick", null);
}
function changeFdInBl() {
    $.prompt('Do you want to update the BL also?', {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                $('#methodName').val("changeBlFD");
                var params = $("#lclBookingForm").serialize();
                params += "fileNumberId=" + $('#fileNumberId').val();
                $.post($("#lclBookingForm").attr("action"), params, function (data) {
                    showProgressBar();
                    $('#calcBlRates').val(true);
                    ratesRecalculation();
                    hideProgressBar();
                });
            } else {
                $('#calcBlRates').val(false);
                $.prompt.close();
                ratesRecalculation();
            }
        }
    });
}