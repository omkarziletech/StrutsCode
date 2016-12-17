function validateform(path) {
    var nonRated = $('input:radio[name=nonRated]:checked').val();
    var cfcl = $('input:radio[name=cfclForDR]:checked').val();
    var tariff = $("#commodityTypeForDr").val();
    if (document.getElementById("portOfOriginForDr") == null ||
            document.getElementById("portOfOriginForDr").value == "") {
        $.prompt("Origin CFS is required");
        $("#portOfOriginForDr").css("border-color", "red");
        return false;
    }
    if ((document.getElementById("finalDestinationForDr").value == null ||
            document.getElementById("finalDestinationForDr").value == "") && (nonRated != 'Y')) {
        $.prompt("Destination is required");
        $("#finalDestinationForDr").css("border-color", "red");
        return false;
    }
    if (tariff == "" || tariff === null) {
        $.prompt("Tariff# is Required")
        $("#commodityTypeForDr").css("border-color", "red");
        return false;
    }
    if ($('#commodityTypeId').val() === '' && $('#commodityNoForDr').val() === '000000') {
        $.prompt("Tariff# is Invalid.Please Change the Tariff Details")
        $("#commodityTypeForDr").val("");
        $("#commodityNoForDr").val("");
        $("#commodityTypeForDr").css("border-color", "red");
        $("#commodityNoForDr").css("border-color", "red");
        return false;
    }
    if ((cfcl === "Y") && ($('#cfclAcctNoForDr').val() === '' || $('#cfclAcctNoForDr').val() === null)) {
        $.prompt("CFCL Account is required");
        $('#cfclAcctNameForDr').css("border-color", "red");
        return false;
    }
    showProgressBar();
    submitForm(path);
}

function submitForm(path) {
    var pooId = $("#quickdrPortOfOriginId").val();
    var pooName = $("#portOfOriginForDr").val();
    var shipAcctForDr = $("#shipAcctForDr").val();
    var fdName = $("#finalDestinationForDr").val();
    var fdId = $("#finalDestinationIdForDr").val();
    var shipContactForDr = "";
    if ($("#shipContactForDr").val() != null && $("#shipContactForDr").val() != "") {
        shipContactForDr = $("#shipContactForDr").val();
    } else if ($("#tempShipper").val() != null && $("#tempShipper").val() != "") {
        shipContactForDr = $("#tempShipper").val();
    }
    if ($("#consContactForDr").val() != null && $("#consContactForDr").val() != "") {
        var consContactForDr = $("#consContactForDr").val();
    } else if ($("#tempConsignee").val() != null && $("#tempConsignee").val() != "") {
        var consContactForDr = $("#tempConsignee").val();
    } else {
        var consContactForDr = "";
    }
    if ($("#fwdContactForDr").val() != null && $("#fwdContactForDr").val() != "") {
        var fwdContactForDr = $("#fwdContactForDr").val();
    } else if ($("#tempForwarder").val() != null && $("#tempForwarder").val() != "") {
        var fwdContactForDr = $("#tempForwarder").val();
    } else {
        var fwdContactForDr = "";
    }
    var consAcctForDr = $("#consAcctForDr").val();
    var fwdAcctForDr = $("#fwdAcctForDr").val();
    var commodityNoForDr = $("#commodityNoForDr").val();
    var bookedPieceCount = $("#bookedPieceCount").val();
    var bookedWeightImperial = $("#bookedWeightImperial").val();
    var bookedVolumeImperial = $("#bookedVolumeImperial").val();
    var packageIdDr = $("#packageIdDr").val();
    var packageDr = $("#packageDr").val();
    var commodityTypeId = $("#commodityTypeId").val();
    var labelFieldName = $("#labelField").val() === "" ? "" : $("#labelField").val();
    var printDr = $('input:radio[name=printDR]:checked').val();
    var businessUnit = $('input:radio[name=businessUnit]:checked').val();
    var nonRated = $('input:radio[name=nonRated]:checked').val();
    var hazmat = $('input:radio[name=hazmatBtn]:checked').val() === 'Y' ? true : false;
    var overShortdamaged = $('input:radio[name=overShortdamaged]:checked').val() === 'Y' ? true : false;
    var osdRemarks = $("#osdRemarks").val();
    var hotCodeComments = $('#xxxHotCodeComment').val();
    var holdComments = $('#holdShipmentComments').val();
    var cfclForDr = $('input:radio[name=cfclForDR]:checked').val() === 'Y' ? true : false;
    // alert("Submit Form");

    var href = path + "/lclBooking.do?methodName=quickDr&quickdrPortOfOriginId=" + pooId + "&portOfOriginForDr=" + pooName;
    href = href + "&shipAcctForDr=" + shipAcctForDr + "&finalDestinationForDr=" + fdName + "&finalDestinationIdForDr=" + fdId;
    href = href + "&shipContactForDr=" + encodeURIComponent(shipContactForDr) + "&consContactForDr=" + encodeURIComponent(consContactForDr) + "&consAcctForDr=" + consAcctForDr;
    href = href + "&fwdContactForDr=" + encodeURIComponent(fwdContactForDr) + "&fwdAcctForDr=" + fwdAcctForDr + "&commodityNoForDr=" + commodityNoForDr;
    href = href + "&bookedPieceCount=" + bookedPieceCount + "&bookedWeightImperial=" + bookedWeightImperial + "&bookedVolumeImperial=" + bookedVolumeImperial;
    href = href + "&packageIdDr=" + packageIdDr + "&packageDr=" + packageDr + "&commodityTypeId=" + commodityTypeId + "&labelFieldName=" + labelFieldName;
    href = href + "&printDr=" + printDr + "&nonRated=" + nonRated + "&hazmat=" + hazmat + "&moduleName=Exports&masterScheduleNo=" + $("#masterScheduleNo").val();
    href = href + "&pooLrdt=" + $("#pooLrdt").val() + "&fdEta=" + $('#fdEta').val() + "&businessUnit=" + businessUnit + "&osdValues=" + overShortdamaged + "&osdRemarks=" + osdRemarks + "&hotCodeComments=" + hotCodeComments;
    href = href + "&cfclForDR=" + cfclForDr + "&cfclAcctNoForDr=" + $('#cfclAcctNoForDr').val() + "&cfclAcctNameForDr=" + $('#cfclAcctNameForDr').val() + "&holdComments=" + holdComments;
    $(".saveDr").attr("href", href);
    $(".saveDr").colorbox({
        iframe: true,
        width: "50%",
        height: "50%",
        title: "Quick DR",
        onLoad: function () {
            $('#cboxClose').hide();
        }
    });
}
function showBlock(tar) {    //show Dojo Box
    $('.smallInputPopupBox').hide();
    $("#hotCodes").val('');
    $(tar).show("slow");
}
function checkValidHotCode(id1, id2) { //validate hot code
    $("." + id2).each(function () {
        if ($(this).val() === $("#" + id1).val()) {
            $.prompt("Hot code already exists.");
            $("#hotCodes").val('');
            return false;
        }
    });
}



function addHotCode3pRef() {//add hot code
    var hotcodeArray = $('#hotCodes').val().split("/");
    var moduleName = $('#moduleName').val();

    if (hotcodeArray[0] === "XXX" && moduleName === "Exports") {
        showAlternateMask();
        $("#add-hotCodeComments-container").center().show(500, function () {
            $('#headingComments').text('Enter a required explanation for adding the value "XXX" Hot Code');
            $('#hotCodeComments').val('');
            $('#hiddenDeleteHotCodeFlag').val('');
            $('#3pRefId').val('');
        });
    }
    else {
        addGeneralInfOn3pRef("hotCodesBox", "hotCodesList", "hotCodes", "hotCodes", "");
    }
}
function addGeneralInfOn3pRef(hideSelectorText, selector, refType, refValue, comments) {  //adding Hot Code in div tag
    showProgressBar();
    $('#' + hideSelectorText).hide();
    $("#methodName").val("quickDrAddHotCode");
    params += "&thirdPName=" + refType;
    params += "&refValue=" + $('#' + refValue).val() + "&hotCodeComments=" + comments;

    var params = $("#lclBookingForm").serialize();
    $.post($("#lclBookingForm").attr("action"), params,
            function (data) {
                $("#hotCodesList").html(data);
                hideProgressBar();
            });
}
function addHotCodeXXX3pRef() {  //after enter xxx hotcode
    var hotCodeXXXComments = $('#hotCodeComments').val();
    if (hotCodeXXXComments === null || hotCodeXXXComments === "") {
        $.prompt("Hot Code XXX Comments is required"); // if no comments
        $('#xxxHotCodeComment').val('');
        return false;
    }
    $('#xxxHotCodeComment').val(hotCodeXXXComments);
    $("#addHoldShipment").removeClass('button-style1');
    $("#addHoldShipment").addClass('red-background');
    var deleteFlag = $('#hiddenDeleteHotCodeFlag').val();
    $("#add-hotCodeComments-container").center().hide(500, function () {
        hideAlternateMask();
    });

    addGeneralInfOn3pRef("hotCodesBox", "hotCodesList", "hotCodes", "hotCodes", hotCodeXXXComments);

}

function cancelHotCodeXXXComments() {
    $("#add-hotCodeComments-container").center().hide(500, function () { // cancel xxx hotcode
        $('#hotCodes').val('');
        $('#hotCodesBox').hide();
        hideAlternateMask();
    });
}


function submitAjaxFormforGeneral(methodName, thirdPName, code, refreshDivId, comments) { //delete the hotcode
    //alert("deletehotcode "+ code);
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $("#lclBookingForm").serialize();
    params += "&hotCodeId=" + code;
    //alert(params);
    $.post($("#lclBookingForm").attr("action"), params, function (data) {
        $('#' + refreshDivId).html(data);   // refresh div id 
        hideProgressBar();
    });
}


function deleteHotCode(txt, code) {
    //alert(code);
    var refValue = $("#hotCodesRef" + code).val();
    // alert("hotCodesRef " +  $("#hotCodesRef" + code).val());
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                var hotcodeArray = refValue.split("/");
                var moduleName = $('#moduleName').val();
                if (hotcodeArray[0] === "XXX" && moduleName === "Exports") { //while deleting hot code , no need  for comments                   
                    submitAjaxFormforGeneral("quickDrDeleteHotCode", "hotCodes", code, "hotCodesList", "");
                    $('#hotCodeComments').val('');
                    $('#xxxHotCodeComment').val('');
                    if ($('#hiddenHoldShipmentComments').val().trim() == '') {
                        $("#addHoldShipment").addClass('button-style1');
                        $("#addHoldShipment").removeClass('red-background');
                    }
                } else {
                    submitAjaxFormforGeneral("quickDrDeleteHotCode", "hotCodes", code, "hotCodesList", "");
                }
                $.prompt.close();
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}



function displayOsdBox() {//display osd box hide or show
    var osdValues = $('input:radio[name=overShortdamaged]:checked').val();
    if (osdValues === "Y") {
        $('#osdRemarksId').show();
    } else {
        $('#osdRemarksId').hide();
    }
}



function deleteOsdRemarks(txt, id) {//delete OSD Remarks
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                $('#osdRemarksId').hide();
                $('#osdRemarks').val('');
            }
            else if (v === 2) {
                $('input:radio[name=overShortdamaged]').val(['Y']);
            }
        }
    });
}

function newSupplierName() {
    if ($('#newSupplierForDr').is(":checked")) {
        $('#shipperManual').show()
        $('#shipDr').hide();
        $('#shipContactForDr').val('');
        $('#tempShipper').val('');
        $('#shipAcctForDr').val('');
    } else {
        $('#shipperManual').hide();
        $('#shipDr').show();
        $('#shipContactForDr').val('');
        $('#tempShipper').val('');
        $('#shipAcctForDr').val('');
    }
}
function showByCountry() {
    $('#finalDestinationForDr').val('');
    $('#finalDestinationIdForDr').val('');
    $('#unlocationCode').val('');
    if ($('#basedOnCountry').is(":checked")) {
        $('#finalDestinationForDr').attr('alt', 'CONCAT_COUNTRY_NAME');
    }
    else {
        $('#finalDestinationForDr').attr('alt', 'CONCAT_RELAY_NAME_FD');
    }
}

function newForwarderName() {
    if ($('#newForwarderForDr').is(":checked")) {
        $('#frwdManual').show();
        $('#frwdDojo').hide();
        $('#fwdContactForDr').val('');
        $('#tempForwarder').val('');
        $('#fwdAcctForDr').val('');
    } else {
        $('#frwdManual').hide();
        $('#frwdDojo').show();
        $('#fwdContactForDr').val('');
        $('#tempForwarder').val('');
        $('#fwdAcctForDr').val('');
    }
}
function newConsigneeName() {
    if ($('#newConsigneeForDr').is(":checked")) {
        $('#consManual').show();
        $('#consDojo').hide();
        $('#consContactForDr').val('');
        $('#tempConsignee').val('');
        $('#consAcctForDr').val('');
    } else {
        $('#consManual').hide();
        $('#consDojo').show();
        $('#consContactForDr').val('');
        $('#tempConsignee').val('');
        $('#consAcctForDr').val('');
    }
}
function checkForNumberAndDecimal(obj) {
    var result;
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This field should be Numeric");
    }
}

function checkNonRated() {
    var nonRated = $('input:radio[name=nonRated]:checked').val();
    var destination = $('#finalDestinationForDr').val();
    var unknownDest = $('#unknownDest').val();
    if (nonRated == 'Y') {
        if (destination == "") {
            $("#finalDestinationForDr").prop("readonly", true);
            $('#finalDestinationIdForDr').val($('#unknownDestId').val());
            $('#finalDestinationForDr').val(unknownDest);
            $('#finalDestinationForDr').addClass('textlabelsBoldForTextBoxDisabledLook');
        }
        else {

            var txt = "Are You Sure you want to change Destination?";
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v == 1) {
                        $('#finalDestinationIdForDr').val($('#unknownDestId').val());
                        $('#finalDestinationForDr').val(unknownDest);
                        $('#finalDestinationForDr').addClass('textlabelsBoldForTextBoxDisabledLook');
                    } else if (v == 2) {
                        $('#nonRatedN').attr('checked', true);
                        $('#finalDestinationForDr').val();
                    }
                }
            });
        }
    }
    else if (nonRated == 'N') {
        $("#finalDestinationForDr").prop("readonly", false);
        $('#finalDestinationForDr').removeClass('textlabelsBoldForTextBoxDisabledLook');
        $('#finalDestinationForDr').val('');
    }
    else {
        if (destination != "" && destination.indexOf('007UN') != -1) {
            $('#finalDestinationForDr').removeClass("textlabelsBoldForTextBoxDisabledLook");
            document.getElementById('finalDestinationForDr').value = "";
            $('#finalDestinationForDr').removeAttr("readonly");
            $('#finalDestinationForDr').removeClass("text-readonly");
        }
    }

}
var bkgHazFlag = true;
$(document).ready(function () {
    $(document).keydown(function (e) {
        if ($(e.target).attr("readonly")) {
            if (e.keyCode === 8) {
                return false;
            }
        }
    });
    displayOsdBox();
    var bkgHazmat = $('#bkgHazmat').val() === "" ? 'N' : $('#bkgHazmat').val();
    $('input:radio[name=hazmatBtn]').val([bkgHazmat]);
    if (bkgHazmat === 'Y' && bkgHazFlag) {
        calculateHazRates(bkgHazmat);
        bkgHazFlag = false;
    }
    $("#labelField").keyup(function (e) {
        if (parseInt($('#labelField').val()) === 0) {
            $.prompt("Label Copy cannot be 0");
            $('#labelField').val('');
            return false
        }
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.logiware.common.dao.PropertyDAO",
                methodName: "getProperty",
                param1: "LabelPrintCount",
                dataType: "json"
            },
            success: function (data) {
                if (parseInt($('#labelField').val()) > data) {
                    $.prompt("Labels Copy should be less than <span style=color:red>" + data + "</span>");
                    $('#labelField').val('');
                }
            }
        });
    });
});
function setUpcomingSailings() {
    $(".origin").text($("#portOfOriginForDr").val());
    $(".destination").text($("#finalDestinationForDr").val());
    $("#methodName").val("upComingSailingsForQuickDr");
    $("#portOfOriginId").val($("#quickdrPortOfOriginId").val());
    $("#finalDestinationId").val($("#finalDestinationIdForDr").val());
    $("#previousSailing").val($("#showOlder").prop("checked"));
    var params = $("#lclBookingForm").serialize();
    $.post($("#lclBookingForm").attr("action"), params,
            function (data) {
                $("#upcomingSailing").html(data);
                $("#upcomingSailing", window.parent.document).html(data);
            });
}
function setBkgVoyageDetails() {
    var voyage = $('input:radio[name=voyageRadio]:checked').val();
    var voy = voyage.split('==');
    $('#masterScheduleNo').val(voy[0]);
    $("#pooLrdt").val(voy[7]); // adding  for to set Origin Lrd in Booked Voyage.
    $("#fdEta").val(voy[6]); // adding  for to set Origin Lrd in Booked Voyage.
    $('#pierName').attr("title", voy[10]);
}

function assignTraiffWithCustomer(id1, id2) {
    if ($('#disableAcct').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#disableAcctNo').val() + "</span>");
        $('#' + id1).val("");
        $('#' + id2).val("");
        return false;
    }
    var shipCommondityNo = $("#shipretail").val() === '' ? $("#shipcoload").val() : $("#shipretail").val();
    var consCommondityNo = $("#consretail").val() === '' ? $("#conscoload").val() : $("#consretail").val();
    var fwdCommondityNo = $("#fwdretail").val() === '' ? $("#fwdcoload").val() : $("#fwdretail").val();

    var shipCommondityDesc = $("#shipretailDesc").val() === '' ? $("#shipcoloadDesc").val() : $("#shipretailDesc").val();
    var consCommondityDesc = $("#consretailDesc").val() === '' ? $("#conscoloadDesc").val() : $("#consretailDesc").val();
    var fwdCommondityDesc = $("#fwdretailDesc").val() === '' ? $("#fwdcoloadDesc").val() : $("#fwdretailDesc").val();

    var commodityNo = shipCommondityNo !== '' ? shipCommondityNo : consCommondityNo !== '' ? consCommondityNo : fwdCommondityNo;
    var commodityDesc = shipCommondityDesc !== '' ? shipCommondityDesc : consCommondityDesc !== '' ? consCommondityDesc : fwdCommondityDesc;

    $("#commodityTypeForDr").val(commodityNo !== '000000' ? commodityDesc : '');
    $("#commodityNoForDr").val(commodityNo !== '000000' ? commodityNo : '');
    if (id2 != "consAcctForDr") {
        setTradingPartnerPriority();
    }
    addingClientHotCodes(id2);
}
function checkForNumber(obj) {
    if (!/^([0-9]{0,10})$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This field should be Numeric");
    }
}
function  getTradingPartnerPriority() {
    var shipperBrand = $("#shipperBrand").val();
    var forwarderBrand = $("#fwdBrand").val();
    var brand = forwarderBrand !== "" && forwarderBrand !== "None" ? forwarderBrand :
            shipperBrand !== "" && shipperBrand !== "None" ? shipperBrand : "None";
    return brand;
}
function setTradingPartnerPriority() {
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
}
function setDefaultAgentSetUp() {
    var brand = getTradingPartnerPriority();
    if (brand === "None") {
        var fdUnlocationcode = $('#unlocationCode').val();
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
                if (data === 'B') {
                    $("input:radio[name=businessUnit]").val([$("#econo").val()]);
                } else {
                    $("input:radio[name=businessUnit]").val([$("#eculine").val()]);
                }
            }
        });
    }
}
function openCopy() {
    showAlternateMask();
    $("#add-Comments-container").center().show(300, function () {
        $('#fileNumber').val('');
    });
}
function close() {
    $("#add-Comments-container").center().hide(300, function () {
        $('#fileNumber').val('');
        hideAlternateMask();
    });
}
function copyQuickDr() {
    var fileNumber = $('#fileNumber').val();
    if (fileNumber === null || fileNumber === "") {
        $.prompt("Please Enter Booking File Number");
        $("#fileNumber").css("border-color", "red");
        return false;
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO",
            methodName: "getFileState",
            param1: fileNumber
        },
        async: false,
        success: function (data) {
            if (data === 'B') {
                showLoading();
                $("#moduleName").val("Exports");
                $("#methodName").val("copyQuickDr");
                $("#lclBookingForm").submit();
                close();
            } else {
                $.prompt("Please Select existing Booking FileNumber");
                $('#fileNumber').val('');
            }
        }
    });
}

function checkHazCommodity() {
    var haz = $('#isHazmat').val();
    if (haz === 'true') {
        $('.hazYes').attr('checked', true);
        $("#hazmatY").click();
    }
}

function calculateHazRates(hazmat) {
    var bkghazmat = $('#bkgHazmat').val();
    var fdUnlocation = $('#finalDestinationForDr').val();
    var fdUnlocationcode = fdUnlocation.substring(fdUnlocation.indexOf("(") + 1, fdUnlocation.indexOf(")"));
    if (document.getElementById("portOfOriginForDr") == null ||
            document.getElementById("portOfOriginForDr").value == "") {
        $.prompt("Origin CFS is required");
        $("#portOfOriginForDr").css("border-color", "red");
        $('input:radio[name=hazmatBtn]').val(['N']);
        return false;
    }
    if ((document.getElementById("finalDestinationForDr").value == null ||
            document.getElementById("finalDestinationForDr").value == "")) {
        $.prompt("Destination is required");
        $("#finalDestinationForDr").css("border-color", "red");
        $('input:radio[name=hazmatBtn]').val(['N']);
        return false;
    }
    if (hazmat === 'Y') {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getHazardousStatus",
                param1: fdUnlocationcode,
                param2: fdUnlocationcode,
                dataType: "json"

            },
            async: false,
            success: function (data) {
                if (data[0] === "N") {
                    hazMsg("We do not accept hazardous cargo to this market:" + data[1], false, hazmat, "", "");
                } else if (data[0] === "R") {
                    hazMsg("Hazardous Restrictions apply for :" + data[1] + ". Please confirm that cargo is acceptable for export and determine proper charges.", true, hazmat, "", "");
                }
                else {
                    calculateHazmatRates(hazmat, "", "");
                }
            }
        });

    } else if (hazmat === 'N') {
        removeHazmatRates(hazmat);
    }
}
function hazMsg(txtMsg, hazFlag, isHazmat, fileId, existHazValue) {
    $.prompt(txtMsg, {
        buttons: {
            Ok: 1
        },
        submit: function (v) {
            if (v === 1 && hazFlag) {
                calculateHazmatRates(isHazmat, fileId, existHazValue);
            }
            else {
                clearHazmatValues();
            }
        }
    });
}


function calculateHazmatRates(hazmat, fileId, oldHazmat) {
    $.prompt("Hazmat Rates will be added.Are you sure you want to continue", {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                $('#hazmatY').attr('checked', true);
                $('#hazmatN').attr('checked', false);
            } else if (v === 2) {
                $('#hazmatY').attr('checked', false);
                $('#hazmatN').attr('checked', true);
            }
            $.prompt.close();
        }
    });
}

function clearHazmatValues() {
    var haz = $('#isHazmat').val();
    //  alert(haz);
    if (haz == 'true') {
        $("#commodityTypeForDr").val("");
        $('#commodityNoForDr').val('');
        $("#commodityTypeForDr").css("border-color", "red");
    }
    $('input:radio[name=hazmatBtn]').val(["N"]);
}

function removeHazmatRates(hazmat) {
    $.prompt("Hazmat Rates will be removed.Are you sure you want to continue", {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                $('#hazmatN').attr('checked', true);
                $('#hazmatY').attr('checked', false);
            } else if (v === 2) {
                $('#hazmatY').attr('checked', true);
                $('#hazmatN').attr('checked', false);
            }
            $.prompt.close();
        }
    });
}

function addingClientHotCodes(id) {
    var acctNo = $("#" + id).val();
    $("#methodName").val("quickDrAddHotCode");
    var params = $("#lclBookingForm").serialize();
    params += "&accountNo=" + acctNo;
    $.post($("#lclBookingForm").attr("action"), params, function (data) {
        $("#hotCodesList").html(data);
        hideProgressBar();
    });
}

function isLockPort() {
    var flag = true;
    var podUnlocCode = $('#unlocationCode').val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO",
            methodName: "getLockport",
            param1: podUnlocCode,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data === true) {
                $.prompt("This market is currently locked. Quotes and Bookings are not allowed");
                $("#finalDestinationForDr").val('');
                $("#finalDestinationIdForDr").val('');
                $("#unlocationCode").val('');
                submitAjaxFormforRates('displayemptyVoyage', '#lclBookingForm', '#upcomingSailing', '');
                flag = false;
            }
        }
    });
    return flag;
}
function cfclAccttypeCheck() {
    if ($('#disabledAccount').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#forwardAccount').val() + "</span>");
        $('#cfclAccount').val('');
        $('#cfclAccountNo').val('');
    }
}

function setCfclLabelColor() {
    var cfclForDr = $('input:radio[name=cfclForDR];checked').val();
    if (cfclForDr === 'Y') {
        document.getElementById('cfclLabel').style.color = "blue";
        document.getElementById('cfclLabel').style.fontWeight = "bold";
    }
    else {
        document.getElementById('cfclLabel').style.color = "black";
    }
}
function openHoldShipment() {
    showAlternateMask();
    $("#add-holdShipment-container").center().show(300, function () {
        $("#holdShipmentComments").css("border-color", "");
    });
}
function closeHoldShipment() {
    $("#add-holdShipment-container").center().hide(300, function () {
        var hiddenComments = $('#hiddenHoldShipmentComments').val();
        var hotCodeXXXComments = $('#hotCodeComments').val();
        if (hiddenComments.trim() !== '' || hotCodeXXXComments.trim() !== '') {
            $('#holdShipmentComments').val(hiddenComments);
            $("#addHoldShipment").removeClass('button-style1');
            $("#addHoldShipment").addClass('red-background');
            $("#addHoldShipment").css('hover', null);
        } else {
            $('#hiddenHoldShipmentComments').val('');
            $('#holdShipmentComments').val('');
            $("#addHoldShipment").removeClass('red-background');
        }
        hideAlternateMask();
    });
}
function saveHoldShipment() {
    var comments = $('#holdShipmentComments').val();
    if (comments === null || comments === "") {
        $.prompt('Please Enter Comments');
        $('#holdShipmentComments').val($('#hiddenHoldShipmentComments').val());
        $("#holdShipmentComments").css("border-color", "red");
        return false;
    }
    $("#add-holdShipment-container").center().hide(300, function () {
        $('#hiddenHoldShipmentComments').val(comments);
        $("#addHoldShipment").removeClass('button-style1');
        $("#addHoldShipment").addClass('red-background');
        hideAlternateMask();
    });
}

