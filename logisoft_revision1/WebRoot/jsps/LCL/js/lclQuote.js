var loadForm;

var isQuoteFormChanged = false;   // VARIABLE DECLARE  FOR CHECKING THE CHANGES IN  QUOTE PAGE  .

jQuery(document).ready(function () {
    toggleGeneralInformation();
    setClientManualCheckBox();//New Client CheckBox
    setCreditStatus("shipperCreditClient", "shipperCredit");
    setCreditStatus("consigneeCreditClient", "consigneeCredit");
    displayOsdBox();
    lockAgentQuotesInfo();
    QuoteRateVerified();
    showPickupButton();
    checkHazmatBoth();
    if ($('#lockMessage').val() !== '') {
        $(".convertToBooking").hide();
        showQuoteReadOnly();
    }
    quoteCompletion();
    setNotifyManualCheckBox();
    checkNonRated('L');
    var moduleName = $('#moduleName').val();
    if (moduleName !== 'Imports') {
        getBookingContact();
        var trmnum = $('#trmnum').val();
        var rateType = $('input:radio[name=rateType]:checked').val();
        if ($("#fileNumberId").val() === '' && (trmnum === '59' || (rateType !== undefined && rateType === 'C'))) {
            $('#aesByN').attr('checked', true);
        }
    }
    else {
        if ($("#fileNumberId").val() == '') {
            $('#clientWithConsignee').attr("checked", true);
            setConsigneeForClient();
        }
        if ($("#client_no").val() != "") {
            checkConsigneeAcctType();
        }
    }
    quotFormChangecDetected();  // METHOD DECLARE  FOR CHECKING THE CHANGES IN  QUOTE PAGE  .
    if ($('#reportSaveFlag').val() === 'true') {
        $('#reportSaveFlag').val('');
        quoteReport();
    }
    $("#consigneeNameClient").change(function () {
        clearConsigneeValues();
    });
    $("#shipperNameClient").change(function () {
        clearShipperValues();
    });
    $("#client").change(function () {
        clearClientTextValueByExp();
    });

});
function setClientManualCheckBox() {
    var acctno = $('#client_no').val();
    var newClient = $('#ManualClient').val();
    if (newClient != '' && acctno === '') {
        $('#clientManual').attr("checked", true);
        $('#clientContainer').hide();
        $('#clientConsContainer').hide();
        $('#clientContactManual').show();
        $('#clientContactDojo').hide();
        $('#clientText').show();
    } else {
        $('#clientWithConsignee').attr('checked', false);
        $('#clientContainer').show();
        $('#clientContactManual').hide();
        $('#clientContactDojo').show();
        $('#clientConsContainer').hide();
        $('#clientText').hide();
    }
}
function showConsContainer() {
    $('#client').val($('#clientCons').val());
}
function clientDetails() {
    if ($('#clientWithConsignee').is(":checked")) {
    } else {
        $('#clientCons').val($('#client').val());
    }
}
function acctTypeCheck() {
    target = jQuery("#acct_type").val();
    subtype = jQuery("#sub_type").val();
    var type;
    var subTypes;
    var array1 = new Array();
    var array2 = new Array();
    var moduleName = $('#moduleName').val();
    if ($('#clientDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#clientDisableAcct').val() + "</span>");
        setClientDetails();
    } else {
        if (target != null) {
            type = target;
            array1 = type.split(",");
        }
        if (subtype != null) {
            subTypes = (subtype).toLowerCase();
            array2 = subTypes.split(",");
        }
        if (target != "") {
            if ((!array1.contains('S') && target != 'C' && array1.contains('V') && target != 'O') && !array2.contains('forwarder')) {
                $.prompt("Please select the customers with account type S,O,C and V with subtype forwarder");
                setClientDetails();
            }
        }
    }
    if (moduleName !== 'Imports') {
        setCreditStatus('clientCreditClient', 'creditForClient');
        setPoaStatus('clientpoa', 'clientPoa');
    }
}
function setClientDetails() {
    var moduleName = $('#moduleName').val();
    if (moduleName !== 'Imports') {
        clearClientTextValueByExp();
        displayClientDetails();
    } else {
        clearClientValuesByImp();
    }
}
function setConsigneeForClient() {
    $('#clientManual').attr("checked", false);
    $("#clientConsContainer").show();
    $("#clientText").hide();
    $("#clientContainer").hide();
}
function showConsigneeForClient() {
    if ($('#clientWithConsignee').is(":checked")) {
        $('#clientManual').attr("checked", false);
        $("#clientConsContainer").show();
        $("#clientText").hide();
        $("#clientContainer").hide();
        setClientDetails();
    } else {
        $("#clientContainer").show();
        $('#clientManual').attr("checked", false);
        $("#clientText").hide();
        $("#clientConsContainer").hide();
        setClientDetails();
    }
}
function checkConsigneeAcctType() {
    var clientNo = $('#client_no').val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO",
            methodName: "getTradingpatnerAccType",
            param1: clientNo
        },
        async: false,
        success: function (data) {
            var array = data.split(",");
            for (var i = 0; i <= array.length - 1; i++) {
                if (array[i] == "C") {
                    $('#clientWithConsignee').attr("checked", true);
                    break;
                }
            }
        }
    });
}
function validateSubHouseBl(fileNumberId) {
    var SubHouseBl = $('#subHouseBl').val();
    if (SubHouseBl !== "" || SubHouseBl === null || SubHouseBl === undefined) {
        $.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHsCodeDAO",
                methodName: "validateQuoteSubHouseBl",
                param1: SubHouseBl,
                param2: fileNumberId
            },
            preloading: true,
            success: function (data) {
                if (data == 'available') {
                    $.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO",
                            methodName: "validateSubHouseBl",
                            param1: SubHouseBl,
                            param2: fileNumberId
                        },
                        preloading: true,
                        success: function (data) {
                            if (data !== "available") {
                                $.prompt(data, {
                                    callback: function () {
                                        $('#subHouseBl').val('');
                                    }
                                });
                            }
                        }
                    });
                }
                if (data !== "available") {
                    $.prompt(data, {
                        callback: function () {
                            $('#subHouseBl').val('');
                        }
                    });
                }
            }
        });
    }
}
function newClient() {
    if ($('#clientManual').is(":checked")) {
        $('#clientWithConsignee').attr("checked", false);
        $('#newClientContact').attr("checked", true);
        $("#clientContactDojo").hide();
        $('#contactName').val('');
        $('#clientContactManul').val('');
        $("#clientContactManual").show();
        $("#clientContainer").hide();
        $("#clientConsContainer").hide();
        $("#clientText").show();
        setClientDetails();
    }
    else {
        $('#clientWithConsignee').attr("checked", false);
        $('#newClientContact').attr("checked", false);
        $("#clientContactDojo").show();
        $("#clientContactManual").hide();
        $("#clientContainer").show();
        $("#clientText").hide();
        $("#clientConsContainer").hide();
        $('#ManualClient').val('');
        $('#clientContactManul').val('');
        setClientDetails();
    }
}
function clearClientValuesByImp() {
    jQuery("#client").val('');
    jQuery("#clientCons").val('');
    jQuery("#client_no").val('');
    jQuery("#contactName").val('');
    jQuery("#email").val('');
    jQuery("#address").val('');
    jQuery("#phone").val('');
    jQuery("#fax").val('');
    jQuery("#fmcNumber").val('');
    jQuery("#otiNumber").val('');
    jQuery("#commodityNumber").val('');
    $('#clntNotes').attr("src", path + "/img/icons/e_contents_view.gif");
}
function clearClientTextValueByExp() {
    $("#contactName").val('');
    $("#email").val('');
    $("#client").val('');
    $("#ManualClient").val('');
    $("#clientCons").val('');
    $('#client_no').val('');
    $('#address').val('');
    $('#city').val('');
    $('#state').val('');
    $('#zip').val('');
    $('#country').val('');
    $('#phone').val('');
    $('#fax').val('');
    $('#clientpoa').val('');
    $('#otiNumber').val('');
    $('#fmcNo').val('');
    $("#clientPoa").text('');
    $("#clientCreditClient").text('');
    $('#clientBsEciAcctNo').val('');
    $('#clientBsEciFwNo').val('');
    $('#clntNotes').attr("src", path + "/img/icons/e_contents_view.gif");
}
function setCreditStatus(creditId, creditValueId) {
    var creditValue = $('#' + creditValueId).val();
    if (creditValue != '') {
        // creditValue = creditValue.substring(0, creditValue.indexOf('-'));
        if (creditValue === 'Suspended/See Accounting' || creditValue === 'No Credit') {
            $('#' + creditId).addClass('red');
        }
        else {
            $('#' + creditId).addClass('green');
        }
        $('#' + creditId).text(creditValue);
    }
}
function setPoaStatus(poaValueId, poaId) {
    var poa = $("#" + poaValueId).val();
    if (poa != null && poa != '') {
        if (poa === 'Y') {
            $('#' + poaId).text('YES')
            $('#' + poaId).addClass('green');
        } else {
            $('#' + poaId).addClass('red');
            $('#' + poaId).text('NO')
        }
    }
}
function congAlert(txt) {
    $.prompt(txt);
}

function showBlock(tar) {
    $('.smallInputPopupBox').hide();
    $(tar).show("slow");
}
function hideBlock(tar) {
    $(tar).hide("slow");
}
function checkValidHotCode(id1, id2) {
    $("." + id2).each(function () {
        if ($(this).val() === $("#" + id1).val()) {
            $.prompt("Hot code already exists.");
            $("#hotCodes").val('');
            return false;
        }
    });
}
function addHotCode3pRef(fileId) {
    addGeneralInfOn3pRef("hotCodesBox", "hotCodesList", "hotCodes", "hotCodes", fileId);
}
function addCustomerPo3pRef(fileId) {
    addGeneralInfOn3pRef("customerPoBox", "customerPoList", "customerPo", "customerPo", fileId);
}
function addNcmNo3pRef(fileId) {
    addGeneralInfOn3pRef("ncmNoBox", "ncmNoList", "ncmNo", "ncmNo", fileId);
}
function addWareHouseDoc3pRef(fileId) {
    addGeneralInfOn3pRef("warehouseDocBox", "warehouseList", "wareHouseDoc", "wareHouseDoc", fileId);
}
function addTracking3pRef(fileId) {
    addGeneralInfOn3pRef("trackingBox", "trackingList", "tracking", "tracking", fileId);
}
function addGeneralInfOn3pRef(hideSelectorText, selector, refType, refValue, fileId) {
    showProgressBar();
    var brand = getTradingPartnerPriority();
    $('#' + hideSelectorText).hide();
    $('#methodName').val('addLcl3pReference');
    var params = $("#lclQuoteForm").serialize();
    params += "&refTypeFlag=" + refType + "&fileNumberId=" + fileId + "&refValue=" + $('#' + refValue).val();
    $.post($("#lclQuoteForm").attr("action"), params, function (data) {
        $("#" + selector).html(data);
        $("#" + selector, window.parent.document).html(data);
        $('#' + refValue).val('');
        hideProgressBar();
    });
}
function saveHsCode(tar, selector, methodName, noteId, fileNumberId) {
    showProgressBar();
    $(tar).hide("");
    $('#methodName').val(methodName);
    var params = $("#lclQuoteForm").serialize();
    params += "&noteId=" + noteId + "&fileNumberId=" + fileNumberId;
    $.post($("#lclQuoteForm").attr("action"), params, function (data) {
        $(selector).html(data);
        $(selector, window.parent.document).html(data);
        $('#' + noteId).val("");
        if (noteId == 'hsCode') {
            openPopupHsCode();
        }
        hideProgressBar();
    });
}
function openPopupHsCode() {
    var fdUnlocCode = getDestination();
    var fileNumberId = $('#fileNumberId').val();
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkEmptyForQuotePortsHsCode",
            param1: fdUnlocCode,
            param2: fileNumberId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data != null && data != undefined && data != "") {
                var pieces = data.split("\n");
                for (var i = 0; i < (pieces.length - 1); i++) {
                    var data1 = pieces[i];
                    $('#bookingHsCode').val(data1.substring(0, data1.indexOf('/')));
                    $('#bookingHsCodeId').val(data1.substring(data1.indexOf('/') + 1));
                    $('#hsCodePiece').val('');
                    $('#hsCodeWeightMetric').val('');
                    $('#packageType').val('');
                    $('#packageTypeId').val('');
                    $('#hsCodeBoxForOther').show();
                }
            }
        }
    });
}
function addHsCode(tar, methodName, fileNumberId, selector, noteId) {
    $.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHsCodeDAO",
            methodName: "hsCodeAlreadyExist",
            param1: fileNumberId,
            param2: $("#hsCode").val().trim(),
            param3: $("#bookingHsCode").val().trim()
        },
        preloading: true,
        async: false,
        success: function (data) {
            if (data === 'true' && $('#bookingHsCodeId').val().trim() === '') {
                $.prompt("This Quote - " + $('#fileNumber').val() + " already has same HS code");
            } else {
                //xxxx.xx
                var regexp = /^\d{4}\.\d{2}$/i;
                //xxxx.xx.xxxx
                var regexp1 = /^\d{4}\.\d{2}\.\d{4}$/i;
                if (noteId != 'hsCode') {
                    if ($('#hsCode').val() === null || $('#hsCode').val() === "") {
                        $.prompt('Code is required');
                        $("#hsCode").css("border-color", "red");
                        hideProgressBar();
                    } else if (!regexp.exec($('#hsCode').val()) && !regexp1.exec($('#hsCode').val())) {
                        $.prompt('HS Code should be in format NNNN.NN or NNNN.NN.NNNN');
                        $("#hsCode").css("border-color", "red");
                        hideProgressBar();
                    } else {
                        saveHsCode(tar, selector, methodName, noteId, fileNumberId);
                    }
                } else {
                    if ($('#bookingHsCode').val() == null || $('#bookingHsCode').val() == "") {
                        $.prompt('Code is required');
                        $("#bookingHsCode").css("border-color", "red");
                    }
                    else if (!regexp.exec($('#bookingHsCode').val()) && !regexp1.exec($('#bookingHsCode').val())) {
                        $.prompt('HS Code should be in format NNNN.NN or NNNN.NN.NNNN');
                        $("#bookingHsCode").css("border-color", "red");
                        hideProgressBar();
                    }
                    else if ($('#hsCodePiece').val() == null || $('#hsCodePiece').val() == "") {
                        $.prompt('Piece is required');
                        $("#hsCodePiece").css("border-color", "red");
                    }
                    else if ($('#hsCodeWeightMetric').val() == null || $('#hsCodeWeightMetric').val() == "") {
                        $.prompt('Weight is required');
                        $("#hsCodeWeightMetric").css("border-color", "red");
                    }
                    else if ($('#packageType').val() == null || $('#packageType').val() == "") {
                        $.prompt('packageType is required');
                        $("#packageType").css("border-color", "red");
                    }
                    else {
                        $(tar).hide("");
                        $("#bookingHsCode").css("border-color", "");
                        $("#hsCodePiece").css("border-color", "");
                        $("#hsCodeWeightMetric").css("border-color", "");
                        $("#packageType").css("border-color", "");
                        $('#methodName').val(methodName);
                        saveHsCode(tar, selector, methodName, noteId, fileNumberId);
                    }
                }
            }
        }
    });
}

function showHsCodeBlock(tar) {
    var fdUnlocCode = getDestination();
    var fileNumberId = $('#fileNumberId').val();
    $('#hsCode').val('');
    $('#bookingHsCode').val('');
    $('#bookingHsCodeId').val('');
    $('#packageType').val('');
    $('#packageTypeId').val('');
    $('#hsCodePiece').val('');
    $('#hsCodeWeightMetric').val('');
    if (null != fdUnlocCode && "" != fdUnlocCode) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getPortsHsCodeForQuote",
                param1: fdUnlocCode,
                param2: fileNumberId
            },
            preloading: true,
            async: false,
            success: function (data) {
                if (data === "true") {
                    tar = "#hsCodeBoxForOther";
                    $(tar).show("slow");
                } else {
                    tar = "#hsCodeBox";
                    $(tar).show("slow");
                }
            }
        });
    } else {
        $.prompt("Please Enter Destination");
    }

}

function submitAjaxForm(methodName, formName, selector, id) {
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&id=" + id;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                //            var moduleName = $('#moduleName').val();
                //            if (methodName == "lclRelayFind" && moduleName === "Exports") {
                //                submitAjaxFormForVoyage('displayVoyage', '#lclQuoteForm', '#upcomingSailing', '');
                //            }
                if (methodName === "refreshAgent") {
                    lclQuotedefaultAgent();
                }
            });
}
function submitAjaxFormForVoyage(methodName, formName, selector, id) {
    showProgressBar();
    $("#methodName").val(methodName);
    var polid = $('#portOfLoadingId').val();
    var podid = $('#portOfDestinationId').val();
    var relay = $('input:radio[name=relayOverride]:checked').val();
    var params = $(formName).serialize();
    if (polid != "" && podid != "" && methodName == 'displayVoyage') {
        params += "&id=" + id + "&polid=" + polid + "&podid=" + podid + "&relay=" + relay;
    }
    else {
        params += "&id=" + id;
    }
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
                if (methodName == "lclRelayFind") {
                    submitAjaxFormForVoyage('displayVoyage', '#lclQuoteForm', '#upcomingSailing', '');
                }
            });
    hideProgressBar();
}
function submitAjaxFormForRates(methodName, formName, selector, origin, destination,
        pol, pod, radioValue, doorOriginCityZip, pickupReadyDate) {
    //    alert(" methodName "+ methodName+ " origin "+origin + " destination "+destination+" pol " +pol+"  pod " +pod);
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod +
            "&radioValue=" + radioValue + "&doorOriginCityZip=" + doorOriginCityZip + "&pickupReadyDate=" + pickupReadyDate;
    $.post($(formName).attr("action"), params,
            function (data) {
                var origin = $("#portOfOriginR").val();
                $("#quoteChargeOrigin").text(origin.substring(0, origin.indexOf("/")));
                var destination = $("#finalDestinationR").val();
                $(".quoteChargeDest").text(destination.substring(0, destination.indexOf("/")));
                $(selector).html(data);
                $(selector).find("[title != '']").not("link").tooltip();
                $(selector, window.parent.document).html(data);
                if ($('#moduleName').val() === "Imports") {
                    $('#methodName').val("displayTransitTime");
                    var params2 = $('#lclQuoteForm').serialize();
                    $.post($('#lclQuoteForm').attr("action"), params2,
                            function (data) {
                                $("#trasitTime").html(data);
                                $("#trasitTime", window.parent.document).html(data);
                                if ($("#validateImpRates").val() !== "") {
                                    $.prompt($("#validateImpRates").val());
                                }
                                hideProgressBar();
                            });
                } else {
                    hideProgressBar();
                }
            });
}
function submitForm(methodName) {
    window.parent.showLoading();
    var fileNumberId = $('#fileNumberId').val();
    if ($('#moduleName').val() !== "Imports" && fileNumberId !== null && fileNumberId !== "" && fileNumberId !== "0") {
        displayClientNotes('Q');
        //        displayShipperNotes('Q');
        //        displayForwarderNotes('Q');
        //        displayConsigneeNotes('Q');
    }
    $('#methodName').val(methodName);
    document.lclQuoteForm.submit();
}
function validateQuoteform(module) {
    var ratetype = $('input:radio[name=rateType]:checked').val();
    var thirdParty = $('input:radio[name=billForm]:checked').val();
    var CFCL = $('input:radio[name=cfcl]:checked').val();
    var fileId = $('#fileNumberId').val();
    var haz = $('.Qhazmat').val();
    var ert = $('#rtdTransaction').val();
    var insurance = $('input:radio[name=insurance]:checked').val();
    var pickupYesNo = $('input:radio[name=pooDoor]:checked').val();
    var qtOrginAgentStatus = sessionStorage.getItem("qtOrginAgentStatus");
    var datatableobj = document.getElementById('commObj');
    if (module === 'Imports' && qtOrginAgentStatus === "Clear" && ($('#supplierNameOrg').val() === "" || $('#supplierCode').val() === "")) {
        $.prompt("Origin Agent is Required");
        return false;
    } else {
        sessionStorage.removeItem("qtOrginAgentStatus");
    }
    if (module != "Imports" && (document.getElementById("portOfOriginR") == null || document.getElementById("portOfOriginR").value == "")) {
        $.prompt("Origin CFS is required");
        $("#portOfOriginR").css("border-color", "red");
        return false;
    }
    else if (document.getElementById("finalDestinationR") == null || document.getElementById("finalDestinationR").value == "") {
        $.prompt(module != "Imports" ? "Destination is required" : "Final Destination is required");
        $("#finalDestinationR").css("border-color", "red");
        return false;
    }
    else if (module != 'Imports' && (ratetype == null || ratetype == "" || ratetype == undefined)) {
        $.prompt("CTC/Retail/FTF  is required");
        return false;
    }
    else if (insurance == "Y" && (document.getElementById("valueOfGoods").value == null || document.getElementById("valueOfGoods").value == "")) {
        $.prompt("Please enter the Value of Goods");
        $("#valueOfGoods").css("border-color", "red");
        return false;
    } else if (thirdParty == "T" && (document.getElementById('thirdPartyName').value == '' || document.getElementById('thirdPartyName').value == null)) {
        $.prompt("Third Party Account Name is required");
        $('#thirdPartyName').css("border-color", "red");
        return false;
    } else if (CFCL == "Y" && (document.getElementById('cfclAccount').value == '' || document.getElementById('cfclAccount').value == null)) {
        $.prompt("CFCL Account Name is required");
        $('#cfclAccount').css("border-color", "red");
        return false;
    }
    else if (ert == "" || ert == null) {
        $.prompt("ERT field is required");
        $("#rtdTransaction").css("border-color", "red");
        return false;
    }
    else if (document.getElementById("terminalLocation") == null || document.getElementById("terminalLocation").value == "") {
        $.prompt("Term to do BL is Required")
        $("#terminalLocation").css("border-color", "red");
        return false;
    }
    else if (pickupYesNo == "Y") {
        if (module === 'Exports' && $("#doorOriginCityZip").val() === "" && $("#manualDoorOriginCityZip").val() === "") {
            $.prompt("Door Origin/City/Zip is required");
            $("#doorOriginCityZip").css("border-color", "red");
            $("#manualDoorOriginCityZip").css("border-color", "red");
            return false;
        } else if (module === 'Imports' && $("#doorOriginCityZip").val() === "") {
            $.prompt("Door Dest/City/Zip is required");
            $("#doorOriginCityZip").css("border-color", "red");
            return false;
        }
        else if (module !== 'Imports' && datatableobj === null) {
            $.prompt("Please add atleast one commodity/tariff#");
            return false;
        }
    }
    else if (module !== 'Imports' && datatableobj === null) {
        $.prompt("Please add atleast one commodity/tariff#");
        return false;
    }
    if (fileId != "" && module != 'Imports' && pickupYesNo == "Y") {
        if (!isValidateRates(fileId)) {
            $.prompt('Please Enter Inland Charge/Sell Amount and Inland Cost is Required');
            return false;
        }
    }
    if (CFCL === "Y") {
        if ($("#portOfLoading").val() === "") {
            $("#portOfLoading").val($("#portOfOriginR").val());
            $("#polUnlocationcode").val($("#originUnlocationCode").val());
            $("#portOfLoadingId").val($("#portOfOriginId").val());
        }
        if ($("#portOfDestination").val() === "") {
            $("#portOfDestination").val($("#finalDestinationR").val());
            $("#podUnlocationcode").val($("#unlocationCode").val());
            $("#portOfDestinationId").val($("#finalDestinationId").val());
        }
    }
    if (fileId != "" && haz == 'Haz' && qtHotCodeValidate(fileId)) {
        if (($("#lcl3p-container tr").length) == 0) {
            $.prompt('Please Enter Atleast One Hot Code');
        } else if (!qtHazmatCodeExit(fileId)) {
            $.prompt('Please Enter Atleast One Hazmat Hot Code');
        }
        else {
            submitForm('saveQuote');
        }

    } else {
        submitForm('saveQuote');
    }
}
function isValidateRates(fileId) {
    var flag = false;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteAcDAO",
            methodName: "isValidateRates",
            param1: fileId,
            param2: "INLAND",
            dataType: "json"
        },
        preloading: true,
        async: false,
        success: function (data) {
            if (data) {
                flag = true;
            }
        }
    });
    return flag;
}
function getTerminalNo(value) {//remove
    var termCode = "";
    if (value.indexOf("/") > -1) {
        termCode = value.substring(value.indexOf("/") + 1, value.indexOf("/") + 3)
    }
    return termCode;

}

$(document).ready(function () {
    $('#client').keyup(function () {
        var client = $('#client').val();
        if (client === "") {
            setClientDetails();
        }
    });
    $('#ManualClient').keyup(function () {
        var clientNew = $('#ManualClient').val();
        if (clientNew === "") {
            setClientDetails();
        }
    });
    $('#clientCons').keyup(function () {
        var clientCons = $('#clientCons').val();
        if (clientCons == "") {
            setClientDetails();
        }
    });
    $('#notifyName').keyup(function () {
        var notifyName = $('#notifyName').val();
        if (notifyName == "") {
            clearNotifyDetails();
        }
    });

    $('#doorOriginCityZip').keyup(function () {
        var destination = $('#doorOriginCityZip').val();
        if (destination == "") {
            $('#pickupInf').hide();
        }
    });


    var moduleName = $('#moduleName').val();

    $('#portOfLoading').keyup(function () {
        var pol = $('#portOfLoading').val();
        if (moduleName != 'Imports' && pol == "") {
            clearBookedForVoyage();
            submitAjaxFormforRates('displayemptyVoyage', '#lclQuoteForm', '#upcomingSailing', '');
        } else {
            if (pol == "") {
                $('#supplierNameOrg').val('');
                $('#supplierCode').val('');
                $('#agentName').val('');
                $('#agentNumber').val('');
            }
        }
    });

    loadForm = $("#lclQuoteForm").serialize();
});
/* ***************************************** */
/*         START PICKUP                      */
/* ***************************************** */
function showPickupButton() {
    var pooDoor = $('input:radio[name=pooDoor]:checked').val();
    var module = $('#moduleName').val();
    $("#manualDoorOriginCityZip").hide();
    if (pooDoor === "Y" && module === "Exports") {
        $('#doorOriginCityZip').removeClass("textlabelsBoldForTextBoxDisabledLook");
        $('#doorOriginCityZip').addClass("textlabelsBoldForTextBox");
        $('#doorOriginCityZip').removeAttr("readonly");
        $("#checkDoorOriginCityZip").show();
        if ($('#doorOriginCityZip').val() === '') {
            $("#checkDoorOriginCityZip").attr('checked', false);
        }
        $("#doorOriginCityZip").show();
    } else if (pooDoor === "Y" && module === "Imports") {
        $('#doorOriginCityZip').removeClass("textlabelsBoldForTextBoxDisabledLookWidth");
        $('#doorOriginCityZip').addClass("textlabelsBoldForTextBoxWidth");
        $('#doorOriginCityZip').removeAttr("readonly");
    } else if (pooDoor !== "Y" && module === "Exports") {
        $('#doorOriginCityZip').attr("readonly", true);
        $("#checkDoorOriginCityZip").hide();
    }
    var doorOrigin = $('#doorOriginCityZip').val();
    if (doorOrigin !== null && doorOrigin !== undefined && doorOrigin !== "") {
        $('#pickupInf').show();
        if (module === "Exports" && doorOrigin.indexOf("/") < 0) {
            $('#doorOriginCityZip').hide();
            $("#manualDoorOriginCityZip").show();
        }
    } else {
        $('#pickupInf').hide();
    }
}

function changeDoorOrigin() {//if door delivery or pick up N
    var fileId = $('#fileNumberId').val();
    var pooDoor = $('input:radio[name=pooDoor]:checked').val();
    var module = $('#moduleName').val();
    if (pooDoor === "N" && fileId !== "") {
        if (isValidateRates(fileId) && module === "Exports") {
            deletePickupRates('Inland Rates will be deleted.Are you sure want to continue?', true, fileId);
        } else if (module === "Imports") {
            deletePickupRates('Door Delivery Rates will be deleted.Are you sure want to continue?', true, fileId);
        } else {
            deletePickValue(fileId);
            $('#doorOriginCityZip').val('');
        }
    } else {
        $('#doorOriginCityZip').val('');
    }
    if (module === "Exports") {
        $("#checkDoorOriginCityZip").hide();
        $("#manualDoorOriginCityZip").val('');
        $("#manualDoorOriginCityZip").addClass("textlabelsBoldForTextBoxDisabledLook");
        $('#doorOriginCityZip').addClass("textlabelsBoldForTextBoxDisabledLook");
    } else {
        $('#doorOriginCityZip').addClass("textlabelsBoldForTextBoxDisabledLookWidth");
    }
    $('#doorOriginCityZip').attr("readonly", true);
    $('#pickupInf').hide();
}

function changeOrigin() {//change door or pickUp Values
    var doorOrigin = $('#doorOriginCityZip').val();
    if (doorOrigin !== null && doorOrigin !== undefined && doorOrigin !== "") {
        $('#pickupInf').show();
    }
}
function deletePickValue(fileId) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "deleteQuotePad",
            param1: fileId,
            dataType: "json"
        },
        async: false,
        close: function (data) {
        }
    });
}
function deletePickupRates(txt, deleteFlag, fileId) {
    $.prompt(txt, {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                submitAjaxFormForPickupCharge('deletingPickUpCharge', '#lclQuoteForm', '#chargeDesc', deleteFlag);
                var pooDoor = $('input:radio[name=pooDoor]:checked').val();
                if (pooDoor === "N") {
                    deletePickValue(fileId);
                    $('#doorOriginCityZip').val('');
                }
                if ($('#moduleName').val() === 'Exports') {
                    $("#checkDoorOriginCityZip").hide();
                    $("#checkDoorOriginCityZip").attr('checked', false);
                }
                hideProgressBar();
                $.prompt.close();
            }
            else if (v === 2) {
                $('#pickupInf').show();
                var doorOrigin = $('#doorOriginCityZip').val();
                $('#duplicateDoorOrigin').val(doorOrigin);
                $('#doorOriginY').attr('checked', true);
                $('#doorOriginCityZip').removeClass().addClass("text");
                $('#doorOriginCityZip').removeAttr("readonly");
                if ($('#moduleName').val() === 'Exports') {
                    $("#checkDoorOriginCityZip").show();
                    $("#checkDoorOriginCityZip").attr('checked', false);
                    $("#manualDoorOriginCityZip").hide();
                    $("#manualDoorOriginCityZip").removeClass("textlabelsBoldForTextBoxDisabledLook");
                    $("#doorOriginCityZip").show();
                    $("#doorOriginCityZip").val('');
                }
                hideProgressBar();
                $.prompt.close();
            }

        }
    });
}

function submitAjaxFormForPickupCharge(methodName, formName, selector, deleteFlag) {//delete pickUpRates and refresh the charge details
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&fileNumberId=" + $('#fileNumberId').val() + "&deleteFlag=" + deleteFlag;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                $.fn.colorbox.close();
            });
}
/* ***************************************** */
/*         End PICKUP                      */
/* ***************************************** */
function quoteCommodity(path, fileNumberId, fileNumber, tabFlag, moduleId) {//Add New Commodity
    var pooUnlocCode = getOrigin();
    var fdUnlocCode = getDestination();
    if (fdUnlocCode === '007UN') {
        fdUnlocCode = 'PRSJU';
    }
    var polUnlocCode = $('#polUnlocationcode').val();
    var podUnlocCode = $('#podUnlocationcode').val();
    var rateType = $("input:radio[name='rateType']:checked").val();
    var spotRate = $('input:radio[name=spotRate]:checked').val();
    var datatableobj = document.getElementById('commObj');
    if (fdUnlocCode == null || fdUnlocCode == "") {
        $.prompt("Destination is Required");
        $("#finalDestinationR").css("border-color", "red");
    } else if (moduleId === 'Imports' && ($('#supplierNameOrg').val() === null || $('#supplierNameOrg').val() === "")) {
        $.prompt("Origin Agent is Required");
        $("#supplierNameOrg").css("border-color", "red");
    } else if (moduleId != 'Imports' && (rateType == null || rateType == "" || rateType == undefined)) {
        $.prompt("RateType is required");
    } else if (datatableobj != null && datatableobj.rows.length >= 2) {
        if (spotRate == "Y") {
            $.prompt("Only one Commodity details is allowed for Spot Rate");
        }
        else {
            commodityValidate("A commodity detail line already exists. Are you sure you want to add a 2nd commodity detail line?",
                    path, fileNumberId, fileNumber, tabFlag, moduleId, pooUnlocCode, polUnlocCode, podUnlocCode, fdUnlocCode, rateType);
        }
        //   $(".addCommodity").removeAttr(href);
        // var txt = "You have already selected a commodity/tariff#,  are you sure you want to add a new one?";
        // commodityValidate(txt,
        //    path, fileNumberId, fileNumber, tabFlag, moduleId, pooUnlocCode, polUnlocCode, podUnlocCode, fdUnlocCode, rateType);
    } else {
        addNewCommodityPopUp(path, fileNumberId, fileNumber, tabFlag, moduleId, pooUnlocCode, polUnlocCode, podUnlocCode, fdUnlocCode, rateType);
    }
}
function commodityValidate(txt, path, fileNumberId, fileNumber, tabFlag, moduleId, pooUnlocCode, polUnlocCode, podUnlocCode, fdUnlocCode, rateType) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                addNewCommodityPopUp(path, fileNumberId, fileNumber, tabFlag, moduleId, pooUnlocCode, polUnlocCode, podUnlocCode, fdUnlocCode, rateType);
                $.prompt.close();
            }
            else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function addNewCommodityPopUp(path, fileNumberId, fileNumber, tabFlag, moduleId, pooUnlocCode, polUnlocCode, podUnlocCode, fdUnlocCode, rateType) {
    var notifyNo = $('#notifyCode').val();
    var consNo = $('#consigneeCode').val();
    var clientNo = $('#client_no').val();
    var agentNo = $('#supplierCode').val();
    var spotRate = $('input:radio[name=spotRate]:checked').val();
    var editDimFlag = 'false';
    var expCommodityNo = "";
    var cfcl = $('input:radio[name=cfcl]:checked').val();
    if (moduleId === 'Exports') {
        if (spotRate === 'Y') {
            expCommodityNo = $('#spotRateCommNo').val();
        }
        else {
            expCommodityNo = setTraffiDetails();
        }
    }
    var href = path + "/lclQuoteCommodity.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&tabFlag=" + tabFlag;
    href = href + "&editDimFlag=" + editDimFlag + "&originUnlocCode=" + pooUnlocCode + "&fdUnlocCode=" + fdUnlocCode + "&rateType=" + rateType + "&polUnlocCode=" + polUnlocCode + "&podUnlocCode=" + podUnlocCode;
    href = href + "&moduleName=" + moduleId + "&coloadCommNo=" + $('#coloadCommNo').val();
    href = href + "&expCommodityNo=" + expCommodityNo + "&clientNo=" + clientNo + "&notifyNo=" + notifyNo + "&consNo=" + consNo + "&agentNo=" + agentNo;
    href = href + "&billingType=" + $('input:radio[name=pcBothImports]:checked').val() + "&importCommodity=" + $('#importCommodity').val();
    href = href + "&commodityNumber=" + $('#commodityNumber').val() + "&acctType=" + $('#acct_type').val() + "&cfcl=" + cfcl;
    $.colorbox({
        iframe: true,
        href: href,
        width: "90%",
        height: "90%",
        title: "Add Commodity",
        scrolling: false,
        onClosed: function () {
            if (fileNumberId !== "") {
                updateEconoEcuLineForQuote();
            }
        }
    });
}
function getOrigin() {//get pooUnlocationCode
    var pooName = "";
    var pooObj = $('#portOfOriginR');
    if (pooObj != undefined && pooObj != null && pooObj.val() != "") {
        pooName = pooObj.val();
    }
    if (pooName.lastIndexOf("(") > -1 && pooName.lastIndexOf(")") > -1) {
        return pooName.substring(pooName.lastIndexOf("(") + 1, pooName.lastIndexOf(")"));
    }
    return "";
}
function getDestination() {//get fdUnlocationCode
    var fdName = "";
    var fdObj = $('#finalDestinationR');
    if (fdObj != undefined && fdObj != null && fdObj.val() != "") {
        fdName = fdObj.val();
    }
    if (fdName.lastIndexOf("(") > -1 && fdName.lastIndexOf(")") > -1) {
        return fdName.substring(fdName.lastIndexOf("(") + 1, fdName.lastIndexOf(")"));
    }
    return "";
}

function openClient(path, accountName, accountNo) {//Replicate Quote by Client
    var address = $('#address').val();
    var city = $('#city').val();
    var state = $('#state').val();
    var zip = $('#zip').val();
    var href = path + "/lclClientDetails.do?methodName=displayQuote&accountName=" + accountName + "&accountNo=" + accountNo + "&address=" + address + "&city=" + city + "&state=" + state + "&zip=" + zip;
    $("#replicateClient").attr("href", href);
    $("#replicateClient").colorbox({
        iframe: true,
        width: "90%",
        height: "90%",
        title: "Client"
    });
}
function checkQuoteRates(fileId) {
    var flag = false;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkingQuoteRates",
            param1: fileId,
            dataType: "json"
        },
        preloading: true,
        async: false,
        success: function (data) {
            if (data === true) {
                flag = true;
            }
        }
    });
    return flag;
}
function deleteQuoteCommodity(count, qtPieceId, fileNumberId) {
    var filenumberId = $('#fileNumberId').val();
    if (filenumberId != "") {
        if (checkQuoteRates(filenumberId)) {
            var txt = 'AutoRates will be removed. Manual rates will be recalculated. Are you sure you want to delete?';
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v == 1) {
                        showProgressBar();
                        $("#methodName").val("deleteLclCommodity");
                        var params = $("#lclQuoteForm").serialize();
                        params += "&count=" + count + "&commodityId=" + qtPieceId + "&fileNumberId=" + fileNumberId;
                        $.post($("#lclQuoteForm").attr("action"), params,
                                function (data) {
                                    $("#commodityDesc").html(data);
                                    $("#commodityDesc", window.parent.document).html(data);
                                    var destination = $('#unlocationCode').val();
                                    $("#methodName").val("modifyCommodityAndCharges");
                                    var params1 = $("#lclQuoteForm").serialize();
                                    params1 += "&id=" + fileNumberId + "&destination=" + destination + "&recalculate=true";
                                    $.post($("#lclQuoteForm").attr("action"), params1,
                                            function (data) {
                                                $("#chargeDesc").html(data);
                                                $("#chargeDesc", window.parent.document).html(data);
                                                $("#chargeDesc").find("[title != '']").not("link").tooltip();
                                                parent.$.fn.colorbox.close();
                                                hideProgressBar();
                                            });
                                });
                        hideProgressBar();
                        $.prompt.close();

                    }
                    else if (v == 2) {
                        $.prompt.close();
                    }
                }
            });
        }
        else {
            ratesNotFound(count, qtPieceId, fileNumberId);
        }
    } else {
        ratesNotFound(count, qtPieceId, fileNumberId);
    }
}
function ratesNotFound(count, qtPieceId, fileNumberId) {
    var txt = "Are you sure you want to delete?";
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                $.prompt.close();
                submitAjaxFormDelete('deleteLclCommodity', '#lclQuoteForm', '#commodityDesc', count, qtPieceId, fileNumberId);
            }
            else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}

function submitAjaxFormDelete(methodName, formName, selector, count, qtPieceId, fileNumberId) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&id=" + count + "&commodityId=" + qtPieceId + "&fileNumberId=" + fileNumberId;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}

function editQuoteHazmat(path, qtPieceId) {
    var fileId = $('#fileNumberId').val();
    if (fileId != null && fileId != "" && fileId != '0') {
        var fileNo = $('#fileNumber').val();
        var href = path + "/lclQuoteHazmat.do?methodName=displayQuoteHazmat&qtPieceId=" + qtPieceId
                + "&fileNumberId=" + fileId + "&fileNumber=" + fileNo;
        $(".Qhazmat").attr("href", href);
        $(".Qhazmat").colorbox({
            iframe: true,
            width: "84%",
            height: "98%",
            title: "Haz.Mat",
            onClosed: function () {
                showProgressBar();
                var fileNumberId = $('#fileNumberId').val();
                $("#methodName").val("refreshQtCommodity");
                var params = $("#lclQuoteForm").serialize();
                params += "&fileNumberId=" + fileNumberId;
                $.post($('#lclQuoteForm').attr("action"), "methodName=getLcl3pReference&thirdPName=hotCodes&fileNumberId=" + fileNumberId,
                        function (data) {
                            $('#hotCodesList').html(data);
                            $('#hotCodesList', window.parent.document).html(data);
                        });
                $.post($('#lclQuoteForm').attr("action"), params,
                        function (data) {
                            $('#commodityDesc').html(data);
                            $('#commodityDesc', window.parent.document).html(data);
                            $.fn.colorbox.close();
                            hideProgressBar();
                        });
            }
        });
    } else {
        $.prompt("Please Save Quote");
    }
}

function openDetails(path, bookingPieceId) {
    var href = path + "/commodityDetails.do?methodName=display&bookingPieceId=" + bookingPieceId;
    $(".details").attr("href", href);
    $(".details").colorbox({
        iframe: true,
        width: "63%",
        height: "85%",
        title: "Commodity Details"
    });
}
function editCommodity(path, countVal, editDimFlag, fileNumberId, fileNumber, copyFnVal, quotePieceId) {
    var originalClose = $.colorbox.close;
    $.colorbox.close = function () {
        var $frame = $(".cboxIframe").contents();
        if (($frame.find("#lclQuoteCommodityForm").serialize() !== $frame.find("#loadForm").val()) &&
                ($frame.find("#methodName").val() === "editLclCommodity")) {
            if ($frame.find("#moduleName").val() === 'Exports' && fileNumberId != '0') {
                $frame.find("body").append(
                        $.prompt("Some fields have been modified, if you exit your changes will not be saved.", {
                            buttons: {
                                SaveAndExit: true,
                                ExitWithoutSaving: false
                            },
                            submit: function (v) {
                                if (v) {
                                    var val = ($frame.find('input:radio[name=ups]:checked').val());
                                    $("#ups").val(val);
                                    parent.showLoading();
                                    $frame.find("#methodName").val('saveOrUpdateCommodity');
                                    $frame.find("#ratesRecalFlag").val('true');
                                    $.ajaxx({
                                        dataType: "json",
                                        url: $frame.find("#lclQuoteCommodityForm").attr("action"),
                                        data: $frame.find("#lclQuoteCommodityForm").serialize(),
                                        success: function (result) {
                                            $("#commodityDesc").html(result.commodityDesc);
                                            $("#chargeDesc").html(result.chargeDesc);
                                            originalClose();
                                            parent.closePreloading();
                                        }
                                    });
                                } else {
                                    originalClose();
                                }
                            }
                        }));
                $frame.find("body").clear();
            } else {
                $frame.find("body").append(
                        $.prompt("Some fields have been modified, Use Save option"));
            }
        } else {
            originalClose();
        }
    }
    var pooUnLocCode = getOrigin();
    var fdUnlocCode = getDestination();
    if (fdUnlocCode === '007UN') {
        fdUnlocCode = 'PRSJU';
    }
    var rateType = $("input:radio[name='rateType']:checked").val();
    var cfcl = $('input:radio[name=cfcl]:checked').val();
    var moduleName = $('#moduleName').val();
    var href = path + "/lclQuoteCommodity.do?methodName=editLclCommodity&quotePieceId=" + quotePieceId + "&editDimFlag=" + editDimFlag + "&fileNumber=" + fileNumber;
    href = href + "&originUnlocCode=" + pooUnLocCode + "&fdUnlocCode=" + fdUnlocCode + "&rateType=" + rateType + "&fileNumberId=" + fileNumberId + "&copyFnVal=" + copyFnVal;
    href = href + "&moduleName=" + moduleName + "&countVal=" + countVal + "&cfcl=" + cfcl + "&agentNo=" + $("#agentNumber").val();
    $.colorbox({
        iframe: true,
        href: href,
        width: "90%",
        height: "90%",
        title: "Commodity",
        scrolling: false,
        onClosed: function (data) {
            if ($("#moduleName").val() === 'Exports' && $("#eculineCommodity").val() !== "") {
                var brand = getTradingPartnerPriority();
                if (brand === "None") {
                    updateEconoEcuLineForQuote();
                }
            }
        }
    });
}



var mouse_is_inside = false;
$(document).ready(function ()
{
    $('#aesBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#inbondBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#hotCodesBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#hsCodeBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#customerPoBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#warehouseDocBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#ncmNoBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#trackingBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#osdCodeBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#amsHblBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#aesBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#inbondBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#hsCodeBox').hide();
    });
    $("table").mouseup(function () {
        if (!mouse_is_inside) {
            var hotCode = $("#hotCodes").val();
            if (hotCode == null || hotCode == '') {
                $('#hotCodesBox').hide();
            }
        }
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#customerPoBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#warehouseDocBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#ncmNoBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#trackingBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#osdCodeBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#amsHblBox').hide();
    });
});

function editQtWhseDetails(path, fileNumberId, fileNumber, quotePieceId) {
    if (fileNumberId != null && fileNumberId != "" && fileNumberId != '0') {
        var href = path + "/lclQuoteCommodity.do?methodName=displayWhse&fileNumber=" + fileNumber +
                "&fileNumberId=" + fileNumberId + "&quotePieceId=" + quotePieceId;
        $.colorbox({
            iframe: true,
            href: href,
            width: "70%",
            height: "80%",
            title: "Warehouse Details",
            onClosed: function () {
                var fileNumberId = $('#fileNumberId').val();
                $("#methodName").val("refreshQtCommodity");
                var params = $("#lclQuoteForm").serialize();
                params += "&fileNumberId=" + fileNumberId;
                $.post($("#lclQuoteForm").attr("action"), params,
                        function (data) {
                            $("#commodityDesc").html(data);
                            $("#commodityDesc", window.parent.document).html(data);
                            $("#commodityDesc").find("[title != '']").not("link").tooltip();
                        });
            }
        });
    } else {
        $.prompt("Please Save Quote");
    }
}
//creating a new manual shipper for parties tab (Imports & Exports)
function newShipperName() {
    var moduleName = $('#moduleName').val();
    if ($('#newShipper').is(":checked")) {
        $("#dojoShipper").hide();
        $("#manualShipper").show();
        if (moduleName === 'Exports') {
            $("#manualShipp").show();
            $("#dojoShipp").hide();
            clearConsigneeClient();
            $('#newShipp').attr('checked', true);
        }
        clearShipperValues();
    } else {
        $("#dojoShipper").show();
        $("#manualShipper").hide();
        if (moduleName === 'Exports') {
            $("#dojoShipp").show();
            $("#manualShipp").hide();
            clearConsigneeClient();
            $('#dupShipName').val('');
            $('#newShipp').attr('checked', false);
        }
        $('#dupShipperName').val('');
        clearShipperValues();
    }
}
function clearShipperValues() {
    jQuery("#shipperContactClient").val('');
    jQuery("#shipperEmailClient").val('');
    jQuery("#shipperCode").val('');
    jQuery("#shipperAddress").val('');
    jQuery("#shipperCity").val('');
    jQuery("#shipperState").val('');
    jQuery("#shipperZip").val('');
    jQuery("#shipperCountry").val('');
    jQuery("#shipperPhone").val('');
    jQuery("#shipperFax").val('');
    jQuery("#shipperEmail").val('');
    jQuery("#shipperName").val('');
    jQuery("#shipperPoa").val('');
    jQuery("#shipperCredit").val('');
    jQuery("#shipperCreditClient").text('');
    jQuery("#shipperPoaId").text('');
    $('#shpNotes').attr("src", path + "/img/icons/e_contents_view.gif");
}
function newShipperChange() {
    if ($('#moduleName').val() === 'Exports') {
        $('#dupShipName').val($('#dupShipperName').val());
    }
}
//by new Consignee CheckBox
function newConsigneeName() {
    var moduleName = $('#moduleName').val();
    if ($('#newConsignee').is(":checked")) {
        $("#dojoConsignee").hide();
        $("#manualConsignee").show();
        if (moduleName === 'Exports') {
            $("#manualConsign").show();
            $("#dojoConsign").hide();
            clearConsigneeClient();
            $('#newConsign').attr('checked', true);
        }
        clearConsigneeValues();
    } else {
        $("#dojoConsignee").show();
        $("#manualConsignee").hide();
        if (moduleName === 'Exports') {
            $("#manualConsign").hide();
            $("#dojoConsign").show();
            clearConsigneeClient();
            $('#newConsign').attr('checked', false);
            $('#dupConsName').val('');
        }
        $('#dupConsigneeName').val('');
        clearConsigneeValues();
    }
}
function newConsigneeChange() {
    if ($('#moduleName').val() === 'Exports') {
        $("#dupConsName").val($("#dupConsigneeName").val());
    }
}
function clearConsigneeValues() {
    jQuery("#consigneeContactName").val('');
    jQuery("#consigneeEmailClient").val('');
    jQuery("#consigneeCode").val('');
    jQuery("#consigneeAddress").val('');
    jQuery("#consigneeCity").val('');
    jQuery("#consigneeState").val('');
    jQuery("#consigneeZip").val('');
    jQuery("#consigneeCountry").val('');
    jQuery("#consigneePhone").val('');
    jQuery("#consigneeFax").val('');
    jQuery("#consigneeEmail").val('');
    jQuery("#consigneeName").val('');
    jQuery("#consigneeCredit").val('');
    jQuery("#consigneeCreditClient").text('');
    jQuery("#consigneePoa").val('');
    jQuery("#consigneePoaId").text('');
    $('#conNotes').attr("src", path + "/img/icons/e_contents_view.gif");
}
function notifyAccttype() {
    var acctType = jQuery("#notify_acct_type").val();
    var moduleName = $('#moduleName').val();
    if ($('#notifyDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#notyDisableAcct').val() + "</span>");
        clearNotifyDetails();
    } else if (acctType !== "" && acctType.indexOf("C") === -1) {
        $.prompt('Please select the customers with account type C');
        clearNotifyDetails();
    }
    if (moduleName === "Imports") {
        setCreditStatus("notifyCreditClient", "notifyCredit");
    }
    else {
        setPoaStatus('notifyPoa', 'notifyPoaId');
    }
    var notifyAddress = jQuery("#notifyAddress").val();
    if ((notifyAddress !== undefined && notifyAddress !== "")) {
        jQuery("#notifyAddress").val(jQuery("#notifyAddress").val().replace(/\,/g, " "));
    }
}
function clearNotifyDetails() {
    jQuery("#notifyName").val('');
    jQuery("#notifyCode").val('');
    jQuery("#notifyAddress").val('');
    jQuery("#notifyCity").val('');
    jQuery("#notifyState").val('');
    jQuery("#notifyZip").val('');
    jQuery("#notifyCountry").val('');
    jQuery("#notifyPhone").val('');
    jQuery("#notifyFax").val('');
    jQuery("#notifyEmail").val('');
    jQuery("#notifyCredit").val('');
    jQuery('#notifyCreditClient').text('');
    jQuery('#notifyPoaId').text('');
    jQuery("#notifyPoa").val('');
    $('#notNotes').attr("src", path + "/img/icons/e_contents_view.gif");
}
function setNotifyManualCheckBox() {//checkbox checked by manual
    var notifyManual = $('#dupNotifyName').val();
    var acctNo = $("#notifyCode").val();
    if (notifyManual != '' && acctNo === '') {
        $("#dojoNotify").hide();
        $("#manualNotify").show();
        $("#newNotify").attr('checked', true);
    } else {
        $('#dupNotifyName').val('');
        $("#dojoNotify").show();
        $("#manualNotify").hide();
        $("#newNotify").attr('checked', false);
    }
}

function newNotifyName() {
    if ($('#newNotify').is(":checked")) {
        $("#dojoNotify").hide();
        $("#manualNotify").show();
        clearNotifyDetails();
    } else {
        $("#dojoNotify").show();
        $("#manualNotify").hide();
        clearNotifyDetails();
    }
}
function deleteHotCode(txt, lcl3pRefId) {
    deleteGeneralInformation(txt, lcl3pRefId, 'hotCodes', 'hotCodesList')
}
function deleteCustPo(txt, lcl3pRefId) {
    deleteGeneralInformation(txt, lcl3pRefId, 'customerPo', 'customerPoList')
}
function deleteNcm(txt, lcl3pRefId) {
    deleteGeneralInformation(txt, lcl3pRefId, 'ncm', 'ncmNoList')
}
function deleteWarehouseDoc(txt, lcl3pRefId) {
    deleteGeneralInformation(txt, lcl3pRefId, 'wareHouseDoc', 'warehouseList')
}
function deleteTracking(txt, lcl3pRefId) {
    deleteGeneralInformation(txt, lcl3pRefId, 'tracking', 'trackingList')
}
function deleteGeneralInformation(txt, lcl3pRefId, lcl3pRefFlag, refreshDivId) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                var brand = getTradingPartnerPriority();
                $('#methodName').val('deleteLcl3pReference');
                var params = $("#lclQuoteForm").serialize();
                var fileNumberId = $("#fileNumberId").val();
                params += "&noteId=" + lcl3pRefFlag + "&fileNumberId=" + fileNumberId + "&lcl3pRefId=" + lcl3pRefId;
                $.post($("#lclQuoteForm").attr("action"), params, function (data) {
                    $('#' + refreshDivId).html(data);
                    $("#" + refreshDivId, window.parent.document).html(data);
                    hideProgressBar();
                });
                $.prompt.close();

            }
            else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function deleteHsCode(txt, lcl3pRefId) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                $('#methodName').val('deleteLclQuoteHsCode');
                var params = $("#lclQuoteForm").serialize();
                var fileNumberId = $("#fileNumberId").val();
                params += "&noteId=hsCode&fileNumberId=" + fileNumberId + "&lcl3pRefId=" + lcl3pRefId;
                $.post($("#lclQuoteForm").attr("action"), params, function (data) {
                    $('#hsCodeList').html(data);
                    $("#hsCodeList", window.parent.document).html(data);
                    hideProgressBar();
                });
                $.prompt.close();
            }
            else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}

function showClientSearchOption(path, searchByValue) {
    var href = path + "/lclQuote.do?methodName=clientSearch&searchByValue=" + searchByValue;
    $(".clientSearchEdit").attr("href", href);
    $(".clientSearchEdit").colorbox({
        iframe: true,
        width: "35%",
        height: "51%",
        title: searchByValue + " Search"
    });
}

function addQuoteCharge(path, fileNumberId, fileNumber) {
    var destination = getDestination();
    var moduleName = $('#moduleName').val();
    var count = new Array();
    $(".chargeAmount").each(function () {
        count.push($(this).text().trim());
    });
    if (count.length >= 25 && moduleName === 'Imports') {
        $.prompt("More than 25 charges is not allowed");
        return false;
    }
    var href = path + "/lclQuoteCostAndCharge.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&destination=" + destination + "&manualEntry=true";
    href = href + "&moduleName=" + moduleName;
    $(".quoteCostAndCharge").attr("href", href);
    $(".quoteCostAndCharge").colorbox({
        iframe: true,
        width: "65%",
        height: "65%",
        title: "Add Charge",
        onClosed: function () {
            $("#chargeDesc").find("[title != '']").not("link").tooltip();
        }
    });
}

function editQuoteCharge(path, id, fileNumberId, fileNumber, manualEntry) {
    var href = path + "/lclQuoteCostAndCharge.do?methodName=editQuoteCharge&fileNumberId=" + fileNumberId;
    href = href + "&fileNumber=" + fileNumber + "&id=" + id + "&moduleName=" + $('#moduleName ').val() + "&manualEntry=" + manualEntry;
    $(".quoteCostAndCharge").attr("href", href);
    $(".quoteCostAndCharge").colorbox({
        iframe: true,
        width: "65%",
        height: "70%",
        title: "Edit Charge",
        onClosed: function () {
            $("#chargeDesc").find("[title != '']").not("link").tooltip();
        }
    });
}

function deleteQuoteCharge(txt, id, fileNumberId) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                $("#methodName").val("deleteQuoteCharge");
                var params = $("#lclQuoteForm").serialize();
                params += "&qcid=" + id + "&fileNumberId=" + fileNumberId + "&moduleName=" + $('#moduleName').val();
                $.post($("#lclQuoteForm").attr("action"), params,
                        function (data) {
                            $("#chargeDesc").html(data);
                            $("#chargeDesc", window.parent.document).html(data);
                            $("#chargeDesc").find("[title != '']").not("link").tooltip();
                            hideProgressBar();
                        });
                $.prompt.close();
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}

function submitAjaxFormForCost(methodName, formName, selector, id, fileNumberId) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&id=" + id + "&fileNumberId=" + fileNumberId;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}

function calculateCharge(radioValue, doorOriginCityZip, pickupReadyDate) {
    var module = $('#moduleName').val();
    var origin = getUnlocationCode('portOfOriginR');
    var pol = getUnlocationCode('portOfLoading');
    var pod = getUnlocationCode('portOfDestination');
    var destination = getUnlocationCode('finalDestinationR');
    var methodName = "";
    var doorOriginCityZip = $("#doorOriginCityZip").val();
    var pickupReadyDate = $('#pickupReadyDate').val();
    if (module === "Imports") {
        methodName = "calculateImportCharges";
    }
    else {
        methodName = "calculateCharges";
    }
    var fileId = $('#fileNumberId').val();
    if (fileId !== null && fileId !== '') {
        var spotRate = $('input:radio[name=spotRate]:checked').val();
        if ("Exports" === module && spotRate === 'Y') {
            $.prompt('Cannot change rates as Spot Rate is set to Yes');
            return false;
        }
        var insurance = $('input:radio[name=insurance]:checked').val();
        if (insurance === "Y" && (document.getElementById('valueOfGoods') === null || $('#valueOfGoods').val() === "")) {
            $.prompt("Please enter the Value of Goods");
            $("#valueOfGoods").css("border-color", "red");
            $("#warning").show();
            return false;
        }
        var commodityDetails = $('#commObj').html();
        if (commodityDetails === null) {
            $.prompt("Please add Weight and Measure from Commodity");
        } else {
            $.prompt("Are you sure want to recalculate Rates?", {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        var origin = getUnlocationCode('portOfOriginR');
                        var destination = getUnlocationCode('finalDestinationR');
                        var pol = getUnlocationCode('portOfLoading');
                        var pod = getUnlocationCode('portOfDestination');
                        submitAjaxFormForRates(methodName, '#lclQuoteForm', '#chargeDesc', origin, destination, pol, pod, radioValue, doorOriginCityZip, pickupReadyDate);
                        if (checkQuoteRates(fileId)) {
                            $('#showAstar').show();
                            $('#showAstarDestn').show();
                            if ($('#portOfOriginR').val() != "") {
                                $('#portOfOriginR').addClass("text-readonly");
                                $('#portOfOriginR').attr("readonly", true);
                            }
                            if (module != "Imports") {
                                $('#finalDestinationR').addClass("text-readonly");
                                $('#finalDestinationR').attr("readonly", true);
                                var deliveryMetro = $('input:radio[name=deliveryMetro]:checked').val();
                                $('#deliveryMetroField').val(deliveryMetro);
                            }
                        }
                        $.prompt.close();
                    } else {
                        $.prompt.close();
                    }
                }
            });
        }
    }

}

function copyQuote(txt, path, fileNumberId) {
    var moduleName = $('#moduleName').val();
    $.prompt(txt, {
        buttons: {
            Ok: 1
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                window.location = path + "/lclQuote.do?methodName=copyQuote" + "&fileNumberId=" + fileNumberId + "&moduleName=" + moduleName;
                $.prompt.close();
            }
        }
    });
}

function openInbond(path, fileNumberId, fileNumber) {
    var moduleName = $('#moduleName').val();
    var href = path + "/lclInbonds.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&moduleName=" + moduleName;
    $(".inbondButton").attr("href", href);
    $(".inbondButton").colorbox({
        iframe: true,
        href: href,
        width: "45%",
        height: "80%",
        title: "Inbonds",
        onClosed: function () {
            var moduleName = $('#moduleName').val();
            if (moduleName === 'Imports') {
                var fileNumberId = $('#fileNumberId').val();
                $("#methodName").val("closeInbond");
                var params = $('#lclQuoteForm').serialize();
                params += "&fileNumberId=" + fileNumberId;
                $.post($('#lclQuoteForm').attr("action"), params,
                        function (data) {
                            $('#inbondNumersList').html(data);
                            $('#inbondNumersList', window.parent.document).html(data);
                            $.fn.colorbox.close();
                        });
            } else {
                showProgressBar();
                closeInbondNoHotcode();
            }
        }
    });
}
function closeInbondNoHotcode() {
    var insertInbondFlag = $("#insertInbondFlag").val();
    var fileNumberId = $('#fileNumberId').val();
    $("#methodName").val("addLcl3pReference");
    var params = $('#lclQuoteForm').serialize();
    params += "&refTypeFlag=hotCodes&fileNumberId=" + fileNumberId + "&refValue=INB/INBOND CARGO&inbFlag=true"+"&insertInbondFlag="+insertInbondFlag;
    $.post($('#lclQuoteForm').attr("action"), params,
            function (data) {
                hideProgressBar();
                $('#hotCodesList').html(data);
                $('#hotCodesList', window.parent.document).html(data);
            });
}
function openAes(path, fileNumberId, fileNumber) {
    var href = path + "/lclQuote.do?methodName=displayAES&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber;
    $.colorbox({
        iframe: true,
        href: href,
        width: "65%",
        height: "65%",
        title: "AES Details"
    });
}

function changeAgentStyle() {//maybe remove
    var agentRadioVal = $('input:radio[name=defaultAgent]:checked').val();
    if (agentRadioVal === "N") {
        document.getElementById('agentName').className = "text";
        document.getElementById('agentName').readOnly = false;
    }
}
function displayOsdBox() {
    var osdValues = $('input:radio[name=overShortdamaged]:checked').val();
    if (osdValues === "Y") {
        $("#osdRemarksTextAreaId").show();
    } else {
        $("#osdRemarksTextAreaId").hide();
    }
}
function deleteOsdRemarks(txt, id) {
    var user = $("#loginUserId").val();
    var fileState = $("#fileState").val();
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "deleteRemarks",
                        param1: id,
                        param2: "OSD",
                        param3: user,
                        param4: fileState,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        var osdValues = $('input:radio[name=overShortdamaged]:checked').val();
                        if (osdValues === "N" && data === true) {
                            $('#osdRemarksTextAreaId').hide();
                            $('#osdRemarks').val('');
                        }
                    }
                });
            } else if (v === 2) {
                var $radios = $('input:radio[name=overShortdamaged]');
                $radios.filter('[value=Y]').attr('checked', true);
            }
        }
    });
}
function showQuotePickupInfo(path, fileNumberId, fileNumber, popupName) {
    var moduleName = $('#moduleName').val();
    if (fileNumberId !== null && fileNumberId !== undefined && fileNumberId !== "") {
        var cityZip = "";
        if (moduleName === 'Exports') {
            cityZip = $("#checkDoorOriginCityZip").is(":checked") ? $('#manualDoorOriginCityZip').val() : $('#doorOriginCityZip').val();
        } else {
            cityZip = $('#doorOriginCityZip').val();
        }
        var href = path + "/lclQuotePickupInfo.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber="
                + fileNumber + "&cityStateZip=" + cityZip.toUpperCase() + "&moduleName=" + moduleName;
        $(".pickupInfo").attr("href", href);
        $(".pickupInfo").colorbox({
            iframe: true,
            width: "90%",
            height: "95%",
            title: popupName
        });
    }
    else {
        $.prompt("Please save Quote");
    }
}
function showBarrel(path, fileNumberId, fileNumber) {
    var href = path + "/lclBarrel.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber;
    $(".barrel").attr("href", href);
    $(".barrel").colorbox({
        iframe: true,
        width: "85%",
        height: "85 %",
        title: "Barrel"
    });
}

function checkHazmatBoth() {
    var fileNumberId = $('#fileNumberId').val();
    if (fileNumberId !== null && fileNumberId !== "" && fileNumberId !== '0') {
        var haz = $('.Qhazmat').val();
        if (haz === 'Haz') {
            $('.hazmatLabelVal').html("<span style='color:red;font-weight:bold;'>** HAZ **</span>");
        }
    }
}



function convertToBooking(path, fileNumberId, fileNumber, moduleName) {
    var quoteComplete = $('input:radio[name=quoteComplete]:checked').val();
    var quoteCompleted = $('input:radio[name=quoteCompleted]:checked').val();
    var trLength = $('#upcomingSailing tr').length; //upComing sailing Length    
    var diffDays = getDifference($("#quoteOn").text());
    if (quoteComplete === 'N' || quoteCompleted === 'N') {
        $.prompt("Quote is not completed");
        return false;
    }
    if (diffDays >= 30) {
        $.prompt("Quote is 30 days old or more, it cannot be converted into a DR");
        return false;
    }
    if (moduleName === "Imports") {
        $.prompt("Are you sure? You want to convert this Quote to Booking.", {
            buttons: {
                Ok: 1,
                Cancel: 2
            },
            submit: function (v) {
                if (v === 1) {
                    window.parent.showLoading();
                    $("#methodName").val("convertToBkg");
                    var params = $("#lclQuoteForm").serialize();
                    $.post($("#lclQuoteForm").attr("action"), params, function () {
                        window.parent.changeLclChilds(path, fileNumberId, "B", moduleName);
                    });
                    $.prompt.close();
                }
                else if (v === 2) {
                    $.prompt.close();
                }
            }
        });
    } else if (trLength === 0) {
        $.prompt("Upcoming Voyage is not available for this Quote, Do you want to Continue?", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    doDirectConvert(path, fileNumberId, "B", "", "", "");
                }
                else if (v === 2) {
                    $.prompt.close();
                }
            }
        });
    }
    else {
        var url = path + "/lclQuote.do?methodName=displayBookingVoyage&fileNumberId=" + fileNumberId
                + "&fileNumber=" + fileNumber + "&voyageAction=true&prevSailing=false";
        window.location = url;
    }
}

function doDirectConvert(path, fileNumberId, moduleId, ssHeaderId, originLrd, fdEtaDate) {
    showProgressBar();
    var url = path + "/lclQuote.do?methodName=convertToBkg&fileNumberId="
            + fileNumberId + "&bookedSsHeaderId=" + ssHeaderId + "&originLrdDate=" + originLrd + "&fdEtaDate=" + fdEtaDate;
    $.post(url, function () {
        window.parent.changeLclChilds(path, fileNumberId, moduleId, "Exports");
    });
}

function clearorgDestValues(showAstarType) {
    var moduleName = $('#moduleName').val();
    if (moduleName === 'Imports') {
        document.getElementById('clearRates').style.visibility = "hidden";
        $('#portOfOriginR').removeAttr("readonly");
        $('#portOfOriginR').removeClass("text-readonly");
        $('#portOfOriginR').removeClass("textlabelsBoldForTextBoxDisabledLookWidth");
        $('#portOfOriginR').removeClass("textlabelsBoldForTextBoxDisabledLook");
        $('#portOfOriginR').addClass("textlabelsBoldForTextBoxWidth");
        $('#portOfLoading').removeClass("text-readonly");
        $('#portOfLoading').removeClass("textlabelsBoldForTextBoxDisabledLookWidth");
        $('#portOfLoading').removeClass("textlabelsBoldForTextBoxDisabledLook");
        $('#portOfLoading').addClass("textlabelsBoldForTextBoxWidth");
        $('#portOfLoading').removeAttr("readonly");
        $('#portOfDestination').removeClass("text-readonly");
        $('#portOfDestination').removeClass("textlabelsBoldForTextBoxDisabledLookWidth");
        $('#portOfDestination').removeClass("textlabelsBoldForTextBoxDisabledLook");
        $('#portOfDestination').addClass("textlabelsBoldForTextBoxWidth");
        $('#portOfDestination').removeAttr("readonly");
        $('#finalDestinationR').removeClass("textlabelsBoldForTextBoxDisabledLookWidth");
        $('#finalDestinationR').addClass("textlabelsBoldForTextBoxWidth");
        $('#finalDestinationR').removeClass("textlabelsBoldForTextBoxDisabledLook");
        $('#finalDestinationR').removeClass("text-readonly");
        $('#finalDestinationR').removeAttr("readonly");
        $('#supplierNameOrg').val('');
        $('#supplierCode').val('');
        $('#oldDesnCode').val('');
        $('#unlocationCode').val('');
        $('#originUnlocationCode').val('');
        $('#portOfOriginR').val('');
        $('#portOfOriginId').val('');
        $('#portOfLoading').val('');
        $('#portOfLoadingId').val('');
        $('#portOfDestination').val('');
        $('#portOfDestinationId').val('');
        $('#finalDestinationR').val('');
        $('#finalDestinationId').val('');
        $('#terminalLocation').val('');
        sessionStorage.setItem("qtOrginAgentStatus", "Clear");
    } else {
        $('#finalDestinationR').removeClass("textlabelsBoldForTextBoxDisabledLook");
        $('#finalDestinationR').addClass("textlabelsBoldForTextBox");
        $('#portOfOriginR').removeClass("textlabelsBoldForTextBoxDisabledLook");
        $('#portOfOriginR').addClass("textlabelsBoldForTextBox");
        document.getElementById('pooSailing').innerHTML = "";
        document.getElementById('polSailing').innerHTML = "";
        document.getElementById('podSailing').innerHTML = "";
        document.getElementById('fdSailing').innerHTML = "";
        $('#deliverCargoTo').val('');
        $('#whsCode').val('');
        $('#nonRatedY').attr('checked', false);
        $('#nonRatedN').attr('checked', true);
        if (showAstarType === 'showAstarOrigin') {
            $('#portOfOriginR').removeClass("text-readonly");
            $('#portOfOriginR').removeAttr("readonly");
            $('#portOfOriginR').val('');
            $('#portOfOriginId').val('');
            $('#portOfLoading').val('');
            $('#portOfLoadingId').val('');
            $('#finalDestinationR').removeClass("text-readonly");
            $('#finalDestinationR').removeAttr("readonly");
        }
        if (showAstarType === 'showAstarDestn') {
            $('#finalDestinationR').removeClass("text-readonly");
            $('#finalDestinationR').removeAttr("readonly");
            $('#internalRemarks').val('');
            $('#specialRemarks').val('');
            $('#portGriRemarks').val('');
            $('#finalDestinationR').val('');
            $('#finalDestinationId').val('');
            $('#portOfDestination').val('');
            $('#portOfDestinationId').val('');
            $('#portOfOriginR').removeClass("text-readonly");
            $('#portOfOriginR').removeAttr("readonly");
            $('#agentName').val('');
            $('#agentNumber').val('');
            $('#agentInfo').val('');
        }
        if ($('#deliverCargoTo') !== undefined && $('#deliverCargoTo') !== null) {
            $('#deliverCargoTo').val('');
        }
        $('#rtdTransaction').val("N");
        $('#whsCode').val('');
    }
    $('#destinationCode').val('');
    $('#originCode').val('');
    // $('#portOfOriginR').val('');
    // $('#portOfOriginId').val('');
    // $('#portOfLoading').val('');
    // $('#portOfLoadingId').val('');
    // $('#portOfDestination').val('');
    // $('#portOfDestinationId').val('');
    // $('#finalDestinationR').val('');
    // $('#finalDestinationId').val('');
    $('#originChargeV').text('');
    $('#destinationChargeV').text('');

}

function clearAllValues(txt, fileId, showAstarType) {
    var moduleName = $('#moduleName').val();
    $.prompt(txt, {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                $("#methodName").val("deleteAutoCharges");
                var params = $("#lclQuoteForm").serialize();
                params += "&fileNumberId=" + $('#fileNumberId').val();
                $.post($("#lclQuoteForm").attr("action"), params,
                        function (data) {
                            $('#chargeDesc').html(data);
                            $('#chargeDesc', window.parent.document).html(data);
                            hideProgressBar();
                        });
                clearorgDestValues(showAstarType);
                $('#transitTime').val('');
                $.prompt.close();
                if (moduleName === "Exports") {
                    validateInlandRates();
                }
                //                if (moduleName === "Exports") {
                //                    if (showAstarType === 'showAstarOrigin') {
                //                        document.getElementById('showAstar').style.visibility = "hidden";
                //            }
                //                    if (showAstarType === 'showAstarDestn') {
                //                        document.getElementById('showAstarDestn').style.visibility = "hidden";
                //                    }
                //                }
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}

function QuoteRateVerified() {
    var fileNumberId = $('#fileNumberId').val();
    var portOfOrigin = $('#portOfOriginR').val();
    var moduleName = $('#moduleName').val();
    if (fileNumberId != "") {
        //   document.getElementById('showAstar').style.position = "relative"
        //  document.getElementById('showAstar').style.visibility = "visible";

        if (moduleName != 'Imports') {
            document.getElementById('showAstar').style.position = "relative"
            document.getElementById('showAstar').style.visibility = "visible";
            document.getElementById('showAstarDestn').style.position = "relative"
            document.getElementById('showAstarDestn').style.visibility = "visible";
            $('#finalDestinationR').addClass("text-readonly");
            $('#finalDestinationR').removeClass("textlabelsBoldForTextBox");
            $('#finalDestinationR').attr("readonly", true);
            if (portOfOrigin != '') {
                $('#portOfOriginR').addClass("text-readonly");
                $('#portOfOriginR').removeClass("textlabelsBoldForTextBox");
                $('#portOfOriginR').attr("readonly", true);
            }
        }
        if (moduleName == 'Imports') {
            document.getElementById('showAstar').style.position = "relative"
            document.getElementById('showAstar').style.visibility = "visible";
            var pol = $('#portOfLoading').val();
            var pod = $('#portOfDestination').val();
            if (pol != "") {
                $('#portOfLoading').addClass("text-readonly");
                $('#portOfLoading').addClass("textlabelsBoldForTextBoxDisabledLookWidth");
                $('#portOfLoading').attr("readonly", true);
            } else {
                $('#portOfLoading').removeClass("text-readonly");
                $('#portOfLoading').attr("readonly", false);
            }
            if (pod != "") {
                $('#portOfDestination').addClass("text-readonly");
                $('#portOfDestination').addClass("textlabelsBoldForTextBoxDisabledLookWidth");
                $('#portOfDestination').attr("readonly", true);
            }
            else {
                $('#portOfDestination').removeClass("text-readonly");
                $('#portOfDestination').attr("readonly", false);
            }
            $('#portOfOriginR').addClass("text-readonly");
            $('#portOfOriginR').removeClass("textlabelsBoldForTextBox");
            $('#portOfOriginR').attr("readonly", true);
        }
    } else {
        // document.getElementById('showAstar').style.visibility = "hidden";
        if (moduleName != 'Imports') {
            document.getElementById('showAstar').style.visibility = "hidden";
            document.getElementById('showAstarDestn').style.visibility = "hidden";
        } else {
            document.getElementById('showAstar').style.visibility = "hidden";
        }
        $('#portOfOriginR').removeClass("text-readonly");
        $('#finalDestinationR').removeClass("text-readonly");
        $('#portOfOriginR').addClass("textlabelsBoldForTextBox");
        $('#finalDestinationR').addClass("textlabelsBoldForTextBox");
        $('#portOfOriginR').attr("readonly", false);
        $('#finalDestinationR').attr("readonly", false);
    }
}

function submitAjaxFormforRates(methodName, formName, selector, id) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&id=" + id;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                fillPolPod();
                hideProgressBar();
            });
}

function quoteCompletion() {
    var fileId = $('#fileNumberId').val();
    if (fileId !== "") {
        $('#quoteComplt').show();
        $('#quoteComplte').show();
    } else {
        $('#quoteComplt').hide();
        $('#quoteComplte').hide();
    }
    var quoteComplete = $('input:radio[name=quoteComplete]:checked').val();
    var quoteCompleted = $('input:radio[name=quoteCompleted]:checked').val();
    if (quoteComplete === 'Y' || quoteCompleted === 'Y' && fileId !== "") {
        showQuoteReadOnly();
    }
}
function showQuoteReadOnly() {
    var element;
    var form = document.getElementById('lclQuoteForm');
    // $('#Qcopy').hide();
    $('#Qsave').hide();
    $('#Qrevert').hide();
    $('#saveQ').hide();
    $('#Qinbond').hide();
    $('#Qaes').hide();
    $('#aesQ').hide();
    $('.whseDetail').hide();
    $('#inbondQ').hide();
    $('#ReleaseQ').hide();
    $('#copyQ').hide();
    $('#QmovePlan').hide();
    $('#Qrouting').hide();
    $('#Qcommodity1').hide();
    $('#Qaddhot').hide();
    $('#Qaddcust').hide();
    $('#Qaddncm').hide();
    $('#QaddWhse').hide();
    $('#QaddHs').hide();
    $('#Qaddtrack').hide();
    $('#addaes').hide();
    $('#hazmatButton').hide();
    $('.hazmat').hide();
    $('#lclSpotRate').hide();
    $('#originLrd').attr('disabled', true);
    $('#storageDate').attr('disabled', true);
    $('#insuranceY').attr('disabled', true);
    $('#insuranceN').attr('disabled', true);
    $("#valueOfGoods").addClass("textlabelsBoldForTextBoxDisabledLook");
    $('#valueOfGoods').attr("readonly", true);
    $('#Qconsolidate').hide();
    $('#Qcalculate').hide();
    $('#QcostCharge').hide();
    $('#destinationServices').hide();
    $(".inbondButton").hide();
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type === "text" || element.type === "textarea") {
            if (element.id === "stdchgratebasis" || element.id === "msrForM" || element.id === "ofratebasis"
                    || element.id === "wgtForM" || element.id === "wgtForI" || element.id === "msrForI") {
            }
            else {
                element.style.backgroundColor = "#CCEBFF";
                element.readOnly = true;
                element.style.borderTop = "0px";
                element.style.borderBottom = "0px";
                element.style.borderRight = "0px";
                element.style.borderLeft = "0px solid";
            }
        }
        if (element.type === "select-one" || element.type === "radio" || element.type === "checkbox") {
            //            element.style.border = 0;
            element.disabled = true;
        }
    }
    var imgs = document.getElementsByTagName("img");
    for (var k = 0; k < imgs.length; k++) {
        if (imgs[k].id !== "trianleicon" && imgs[k].id !== "collpaseicon") {
            imgs[k].style.visibility = "hidden";
        }
        if (imgs[k].id === "viewgif" || imgs[k].id === "viewgif1" || imgs[k].id === "viewgif2") {
            imgs[k].style.visibility = "visible";
        }
        if(imgs[k].name === "dimsQuoteMainDetails"){
           imgs[k].style.visibility = "visible"; 
        }
    }
    var fileState = $('#fileState').val();
    if (fileState === 'Q') {
        // don't hide bundle into ofr check box
        $('.bundleCheck').removeAttr('disabled');
    }
}
function setQuoteCompleteNo() {
    var $quoteComplete = $('#quoteCompleteN');
    var $quoteCompleted = $('#quoteCompleteNN');
    $quoteComplete.attr("checked", true);
    $quoteCompleted.attr("checked", true);
}
function confirmQuoteComplete(txt, moduleId) {
    var fileId = $('#fileNumberId').val();
    var pickupYesNo = $('input:radio[name=pooDoor]:checked').val();
    if (fileId !== "" && pickupYesNo === "Y") {
        if (moduleId !== 'Imports' && !isValidateRates(fileId)) {
            $.prompt('Please Enter Inland Charge/Sell Amount and Inland Cost is Required');
            setQuoteCompleteNo();
            return false;
        }
        if (moduleId === 'Exports' && $("#doorOriginCityZip").val() === "" && $("#manualDoorOriginCityZip").val() === "") {
            $.prompt("Door Origin/City/Zip is required");
            $("#doorOriginCityZip").css("border-color", "red");
            $("#manualDoorOriginCityZip").css("border-color", "red");
            setQuoteCompleteNo();
            return false;
        } else if (moduleId === 'Imports' && $("#doorOriginCityZip").val() === "") {
            $.prompt("Door Dest/City/Zip is required");
            $("#doorOriginCityZip").css("border-color", "red");
            setQuoteCompleteNo();
            return false;
        }
    }
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                $('#quoteCompleteY').attr('checked', true);
                $('#quoteCompleteYY').attr('checked', true);
                submitForm('saveQuote');
                quoteCompletion();
                $.prompt.close();
            } else if (v === 2) {
                setQuoteCompleteNo();
                $.prompt.close();
            }
        }
    });
}

function checkNonRated(val) {
    var nonRated = $('input:radio[name=nonRated]:checked').val();
    var quoteComplete = $('input:radio[name=quoteComplete]:checked').val();
    var quoteCompleted = $('input:radio[name=quoteCompleted]:checked').val();
    var destination = $('#finalDestinationR').val();
    var unknownDest = $('#unknownDest').val();
    if (nonRated == 'Y' && (quoteComplete == 'N' || quoteCompleted == 'N')) {
        $('#showFullRelayFd').attr('disabled', false);
        $('#showFullRelay').attr('disabled', false);
        if (destination == "") {
            $('#finalDestinationR').val($('#unknownDestId').val());
            $('#finalDestinationId').val($('#unknownDestId').val());
            $('#finalDestinationR').val(unknownDest);
            $('#finalDestinationR').addClass('textlabelsBoldForTextBoxDisabledLook');
            $('#portOfLoading').val($('#portOfOriginR').val());
            $('#portOfLoadingId').val($('#portOfOriginId').val());
            $('#portOfDestination').val($('#finalDestinationR').val());
            $('#portOfDestinationId').val($('#finalDestinationId').val());
        }
        else {
            if (val != 'L') {
                var txt = "Are You Sure you want to change Destination?";
                $.prompt(txt, {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v == 1) {

                            $("#methodName").val("deleteAutoCharges");
                            var params = $("#lclQuoteForm").serialize();
                            params += "&fileNumberId=" + $('#fileNumberId').val();
                            $.post($("#lclQuoteForm").attr("action"), params,
                                    function (data) {
                                        $('#chargeDesc').html(data);
                                        $('#chargeDesc', window.parent.document).html(data);
                                        var origin = $("#portOfOriginR").val();
                                        var destination = $("#finalDestinationR").val();
                                        $(".quoteChargeOrigin").text(origin.substring(0, origin.indexOf("/")));
                                        $(".quoteChargeDest").text(destination.substring(0, destination.indexOf("/")));

                                    });


                            $('#finalDestinationId').val($('#unknownDestId').val());
                            $('#finalDestinationR').val(unknownDest);
                            $('#finalDestinationR').addClass('textlabelsBoldForTextBoxDisabledLook');
                            $('#portOfLoading').val($('#portOfOriginR').val());
                            $('#portOfLoadingId').val($('#portOfOriginId').val());
                            $('#portOfDestination').val($('#finalDestinationR').val());
                            $('#portOfDestinationId').val($('#finalDestinationId').val());
                            $('#specialRemarks').val('');
                            $('#portGriRemarks').val('');
                            $('#internalRemarks').val('');
                            $('#portGriRemarksPod').val('');
                            $('#internalRemarksPod').val('');
                            $('#specialRemarksPod').val('');
                            $(".splRemarks").text('');
                            $(".internRemarks").text('');
                            $(".griRemarks").text('');
                            $('#upcomingSection').hide();
                        }
                        else if (v == 2) {
                            $('#nonRatedN').attr('checked', true);
                            $('#finalDestinationR').val();
                        }
                    }
                });
            }
        }
    }
    else {
        $('#showFullRelayFd').attr('disabled', true);
        $('#showFullRelay').attr('disabled', true);
        $('#showFullRelayFd').attr('checked', false);
        $('#showFullRelay').attr('checked', false);
        if (destination != "" && destination.indexOf('007UN') != -1) {
            $('#finalDestinationR').removeClass("textlabelsBoldForTextBoxDisabledLook");
            document.getElementById('finalDestinationR').value = "";
            $('#finalDestinationR').removeAttr("readonly");
            $('#finalDestinationR').removeClass("text-readonly");
            $('#portOfLoading').val();
            $('#portOfLoadingId').val();
            $('#portOfDestination').val();
            $('#portOfDestinationId').val();
            $('#portOfLoading').addClass("text-readonly");
            $('#portOfLoading').addClass("textlabelsBoldForTextBoxDisabledLook");
            $('#portOfDestination').addClass("text-readonly");
            $('#portOfDestination').addClass("textlabelsBoldForTextBoxDisabledLook");
        }
    }
}

function checkQuoteCompletion(moduleId) {
    var confirmationMsg = "Once you make this quote complete, you will no longer be able to make any changes to it. \n ARE YOU SURE Y/N?";
    var CFCL = $('input:radio[name=cfcl]:checked').val();
    var $cfcl = $("#cfclAccount");
    var $quoteComplete = $('#quoteCompleteN');
    var $quoteCompleted = $('#quoteCompleteNN');
    var $client = $("#client");
    var $clientCons = $("#clientCons");
    var $manualClient = $("#ManualClient");
    var $shipperNameClient = $("#shipperNameClient");
    var $dupShipName = $("#dupShipName");
    var $fwdNameClient = $("#forwarderNameClient");
    var $consigneeNameClient = $("#consigneeNameClient");
    var $dupConsName = $("#dupConsName");
    var pod = $('#portOfDestination').val();
    var $contactName = $("#contactName");
    var $clientContactManul = $("#clientContactManul");
    var $shipperContactClient = $("#shipperContactClient");
    var $shipperManualContact = $("#shipperManualContact");
    var $forwardercontactClient = $("#forwardercontactClient");
    var $forwarederContactManual = $("#forwarederContactManual");
    var $consigneeContactName = $("#consigneeContactName");
    var $consigneeManualContact = $("#consigneeManualContact");
    //var pwk = $('input:radio[name=pwk]:checked').val();

    if (CFCL === "Y" && $cfcl.val() === "") {
        $.prompt("CFCL Account is required");
        $cfcl.css("border-color", "red");
        return false;
    }
    if (moduleId !== "Imports" && forceClientRoleDuty()
            && ($client === null || $client.val() === "")
            && ($clientCons === null || $clientCons.val() === "")
            && ($manualClient === null || $manualClient.val() === "")
            && ($shipperNameClient === null || $shipperNameClient.val() === "")
            && ($dupShipName === null || $dupShipName.val() === "")
            && ($fwdNameClient === null || $fwdNameClient.val() === "")
            && ($consigneeNameClient === null || $consigneeNameClient.val() === "")
            && ($dupConsName === null || $dupConsName.val() === "")) {
        $.prompt("Please select the Client");
        $client.css("border-color", "red");
        $quoteComplete.attr("checked", true);
        $quoteCompleted.attr("checked", true);
        return false;
    } else if (forceClientRoleDuty() && ($contactName === null || $contactName.val() === "") && ($clientContactManul === null || $clientContactManul.val() === "")
            && ($shipperContactClient === null || $shipperContactClient.val() === "") && ($shipperManualContact === null || $shipperManualContact.val() === "")
            && ($forwardercontactClient === null || $forwardercontactClient.val() === "") && ($forwarederContactManual === null || $forwarederContactManual.val() === "")
            && ($consigneeContactName === null || $consigneeContactName.val() === "") && ($consigneeManualContact === null || $consigneeManualContact.val() === "")) {
        $.prompt("Please select atleast one booking contact name");
        $contactName.css("border-color", "red");
        $quoteComplete.attr("checked", true);
        $quoteCompleted.attr("checked", true);
        return false;
    }
    $("#methodName").val("validateQuoteComplete");
    var params = $("#lclQuoteForm").serialize();
    $.post($("#lclQuoteForm").attr("action"), params, function (data) {
        if (data === "Upcoming Voyage is not available for this Quote, Do you want to Continue?") {
            $.prompt(data, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        confirmQuoteComplete(confirmationMsg, moduleId);
                    }
                    else if (v === 2) {
                        setQuoteCompleteNo();
                        $.prompt.close();
                    }
                }
            });
        }
        else if (data !== 'null') {
            $.prompt(data);
            $quoteComplete.attr('checked', true);
            $quoteCompleted.attr('checked', true);
            return false;
        }
        else if (pod === "") {
            $.prompt("POD field is required");
        }
        else {
            confirmQuoteComplete(confirmationMsg, moduleId);
        }
    });
}

// file is not created yet
function confirmAbort(txt, path, fileNumberId, moduleId, module) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                window.parent.changeLclChilds(path, fileNumberId, moduleId, module);
                hideProgressBar();
                $.prompt.close();
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function confirmChanges(txt, path, fileNumberId, moduleId, module) {
    var msg = "Do you want to save the quote changes?";
    var qtOrginAgentStatus = sessionStorage.getItem("qtOrginAgentStatus");
    if (qtOrginAgentStatus === "Clear" && ($('#supplierNameOrg').val() === "" || $('#supplierCode').val() === "")) {
        $.prompt("Origin Agent is Required");
        return false;
    } else {
        sessionStorage.removeItem("qtOrginAgentStatus");
    }
    $.prompt(msg, {
        buttons: {
            Yes: 1,
            No: 2,
            Cancel: 3
        },
        submit: function (v) {
            if (v === 1) { //save and go back
                $("#methodName").val("saveQuote");
                var params = $("#lclQuoteForm").serialize();
                showProgressBar();
                $.post($("#lclQuoteForm").attr("action"), params, function () {
                    checkFileNumber(txt, path, fileNumberId, moduleId, module);
                    hideProgressBar();
                });
            } else if (v === 2) { //go back
                checkFileNumber(txt, path, fileNumberId, moduleId, module);
            }
            else { //do nothing
                return;
            }
        }
    });
}

// METHODS  DECLARE  FOR CHECKING THE CHANGES IN  QUOTE PAGE  .

function quotFormChangecDetected() {
    var form = "#lclQuoteForm";
    var $selector = $(form + " input[type=text], " + form + " textarea");
    $($selector).each(function () {
        $(this).data('initial_value', $(this).val());
    });

    $($selector).keyup(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            isQuoteFormChanged = true;
        }
    });
    $($selector).change(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            isQuoteFormChanged = true;
        }
    });
    $(form).bind('change paste', function () {
        isQuoteFormChanged = true;
    });
}

function isFormChanged() {
    var form = "#lclQuoteForm";
    var $selector = $(form + " input[type=text], " + form + " textarea");
    $($selector).each(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            isQuoteFormChanged = true;
        }
    });
    return isQuoteFormChanged;
}

function goBackSearch(txt, path, fileNumberId, moduleId, module) {
    var fileId = $('#fileNumberId').val();
    var pickupYesNo = $('input:radio[name=pooDoor]:checked').val();
    if (fileId !== "" && pickupYesNo === "Y") {
        if (module !== 'Imports' && !isValidateRates(fileId)) {
            $.prompt('Please Enter Inland Charge/Sell Amount and Inland Cost is Required');
            return false;
        }
        if (module === 'Exports' && $("#doorOriginCityZip").val() === "" && $("#manualDoorOriginCityZip").val() === "") {
            $.prompt("Door Origin/City/Zip is required");
            $("#doorOriginCityZip").css("border-color", "red");
            $("#manualDoorOriginCityZip").css("border-color", "red");
            return false;
        } else if (module === 'Imports' && $("#doorOriginCityZip").val() === "") {
            $.prompt("Door Dest/City/Zip is required");
            $("#doorOriginCityZip").css("border-color", "red");
            return false;
        }
    }
    if (fileNumberId === null || fileNumberId === '') {
        confirmAbort(txt, path, fileNumberId, moduleId, module);
    } else if (isFormChanged() && $('#quoteCompleteN').is(":checked") && $('#quoteCompleteNN').is(":checked")) {
        confirmChanges(txt, path, fileNumberId, moduleId, module);
    } else {
        checkFileNumber(txt, path, fileNumberId, moduleId, module);
    }
}
//from quote.jsp
function checkFileNumber(txt, path, fileNumberId, moduleId, module) {
    var ratetype = $('input:radio[name=rateType]:checked').val();
    var thirdParty = $('input:radio[name=billForm]:checked').val();
    var CFCL = $('input:radio[name=cfcl]:checked').val();
    var fileId = $('#fileNumberId').val();
    var ert = $('#rtdTransaction').val();
    var insurance = $('input:radio[name=insurance]:checked').val();
    var pickupYesNo = $('input:radio[name=pooDoor]:checked').val();
    if (module != "Imports" && (document.getElementById("portOfOriginR") == null || document.getElementById("portOfOriginR").value == "")) {
        $.prompt("Origin CFS is required");
        $("#portOfOriginR").css("border-color", "red");
        $("#warning").show();
    } else if (document.getElementById("finalDestinationR") == null || document.getElementById("finalDestinationR").value == "") {
        if (module != "Imports") {
            $.prompt("Destination is required");
        } else {
            $.prompt("Final Destination is required");
        }
        $("#finalDestination").css("border-color", "red");
        $("#finalDestinationR").css("border-color", "red");
        $("#countryName").css("border-color", "red");
        $("#warning").show();
    }
    else if (module != 'Imports' && (ratetype == null || ratetype == "" || ratetype == undefined)) {
        $.prompt("CTC/Retail/FTF  is required");
    }
    else if (insurance == "Y" && (document.getElementById("valueOfGoods").value == null || document.getElementById("valueOfGoods").value == "")) {
        $.prompt("Please enter the Value of Goods");
        $("#valueOfGoods").css("border-color", "red");
        $("#warning").show();
    } else if (thirdParty == "T" && (document.getElementById('thirdPartyName').value == '' || document.getElementById('thirdPartyName').value == null)) {
        $.prompt("Third Party Account Name is required");
        $('#thirdPartyName').css("border-color", "red");
    }
    else if (CFCL == "Y" && (document.getElementById('cfclAccount').value == '' || document.getElementById('cfclAccount').value == null)) {
        $.prompt("CFCL Account is required");
        $('#cfclAccount').css("border-color", "red");
    } else if (ert == "" || ert == null) {
        $.prompt("ERT field is required");
        $("#rtdTransaction").css("border-color", "red");
    }
    else {
        var haz = $('.Qhazmat').val();
        if (fileId != "" && haz == 'Haz' && qtHotCodeValidate(fileNumberId)) {
            if (($("#lcl3p-container tr").length) == 0) {
                $.prompt('Please Enter Atleast One Hot Code');
            } else if (!qtHazmatCodeExit(fileId)) {
                $.prompt('Please Enter Atleast One Hazmat Hot Code');
            }
            else {
                showProgressBar();
                goBackFromQuote(path, fileNumberId, moduleId, module);
                hideProgressBar();
            }
        } else {
            showProgressBar();
            goBackFromQuote(path, fileNumberId, moduleId, module);
            hideProgressBar();
        }
    }
}

function goBackFromQuote(path, fileNumberId, moduleId, module) {
    var homeScreenQtFileFlag = $("#homeScreenQtFileFlag").val();
    var fileNumber = $("#moduleId").val();
    if (parent.$("#toScreenName").val() === 'EXP VOYAGE') {
        goToUnitLoadScreenFromQuote(path, fileNumberId);
    } else {
        if (homeScreenQtFileFlag === "true") {
            window.parent.showHome(homeScreenQtFileFlag, fileNumber);
        } else {
            window.parent.changeLclChilds(path, fileNumberId, moduleId, module);
        }
    }
}

function goToUnitLoadScreenFromQuote(path, fileId) {
    var fromScreen = '';
    var headerId = parent.$("#pickVoyId").val();
    var detailId = parent.$("#detailId").val();
    var unitSsId = parent.$("#unitSsId").val();
    var toScreenName = parent.$("#toScreenName").val();
    if (parent.$("#fromScreen").val() === 'UnitLoadScreenToBooking') {
        fromScreen = "BookingToUnitLoadScreen";
    } else if (parent.$("#fromScreen").val() === 'UnitViewDrScreenToBooking') {
        fromScreen = "BookingToUnitViewDrScreen";
    }
    var filter = parent.$("#filterByChanges").val();
    var inTransitDr = parent.$("#inTransitDr").val();
    window.parent.goToBookingFromVoyage(path, fileId, filter, headerId, detailId, unitSsId, fromScreen, toScreenName, inTransitDr);
}

function isHazmat(fileId) {
    var flag = false;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHazmatDAO",
            methodName: "isHazmat",
            param1: fileId,
            dataType: "json"
        },
        preloading: true,
        async: false,
        success: function (data) {
            if (data === "false") {
                flag = true;
            }
        }
    });
    return flag;
}
function ShipReference() {
    var shipref = $('#dupShipref').val();
    $('#shipperClientRef').val(shipref);
}

function ShipReferences() {
    var shipref = $('#shipperClientRef').val();
    $('#dupShipref').val(shipref);
}
function clientRefForwarder() {
    var forwarderClientReff = $('#forwarderClientReff').val();
    $('#forwarderClientRef').val(forwarderClientReff);

}

function ClientRefForwarders() {
    var forwarderClientRef = $('#forwarderClientRef').val();
    $('#forwarderClientReff').val(forwarderClientRef);

}


function clientRefConsignee() {
    var consigneeClientReff = $('#consigneeClientReff').val();
    $('#consigneeClientRef').val(consigneeClientReff);

}

function clientRefConsignees() {
    var consigneeClientRef = $('#consigneeClientRef').val();
    $('#consigneeClientReff').val(consigneeClientRef);
}

function lclQuotedefaultAgent() {
    var defaultagentValues = $('input:radio[name=defaultAgent]:checked').val();
    if (defaultagentValues === 'Y') {
        var moduleName = $('#moduleName').val();
        var fdUnlocationcode = moduleName === 'Exports' ? $('#podUnlocationcode').val() : $('#unlocationCode').val();
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getDefaultAgentForLcl",
                param1: fdUnlocationcode,
                param2: 'L',
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data[3] === 'Y') {
                    $.prompt("This customer is disabled and merged with " + data[4] !== null ? data[4] : "");
                    $("#agentName").val('');
                    $("#agentNumber").val('');
                } else {
                    if (data[0] !== undefined && data[0] !== "" && data[0] !== null) {
                        $('#agentName').val(data[1]);
                        $('#agentNumber').val(data[0]);
                        $('#agentBrand').val(data[4]);
                        checkEculineInQuote($('#agentNumber').val(), "A");
                        $("#agentName").addClass("text-readonly");
                        $("#agentName").addClass("textlabelsBoldForTextBoxDisabledLook");
                        $("#agentNumber").addClass("textlabelsBoldForTextBoxDisabledLook");
                        $("#agentName").attr("readonly", true);
                        $("#agentNumber").attr("readonly", true);
                        $("#agentInfo").val('');
                    } else {
                        $("#agentName").val('');
                        $("#agentNumber").val('');
                        $('#agentBrand').val('');
                    }
                }
            }
        });
    } else {
        $("#agentName").val("");
        $("#agentNumber").val("");
        $("#agentName").removeClass("textlabelsBoldForTextBoxDisabledLook");
        $("#agentName").addClass("textlabelsBoldForTextBox");
        $("#agentName").removeAttr("readonly");
    }
}
function lclQuoteDefaultValues() {
    var defaultAgentValues = $('input:radio[name=defaultAgent]:checked').val();
    if (defaultAgentValues === 'N') {
        $("#agentName").val("");
        $("#agentNumber").val("");
        $("#agentInfo").val("");
        $("#agentName").removeAttr("readonly");
        $("#agentName").removeClass("text-readonly textlabelsBoldForTextBoxDisabledLook");
        $("#agentName").addClass("textlabelsBoldForTextBox");
        $("#agentInfo").removeClass("textlabelsBoldForTextBoxDisabledLook");
        $("#agentInfo").addClass("textlabelsBoldForTextBox");
        $("#agentInfo").removeAttr("readonly");
        $('#agentBrand').val('');
    } else {
        $("#agentName").removeClass("textlabelsBoldForTextBox");
        $("#agentName").addClass("text-readonly textlabelsBoldForTextBoxDisabledLook");
        $("#agentInfo").removeClass("textlabelsBoldForTextBox");
        $("#agentInfo").addClass("textlabelsBoldForTextBoxDisabledLook");
        $("#agentName").attr('readonly', true);
        $("#agentInfo").attr('readonly', true);
    }
}
function lockAgentQuotesInfo() {
    var defaultAgentValues = $('input:radio[name=defaultAgent]:checked').val();
    if (defaultAgentValues === 'N') {
        $("#agentName").removeClass("textlabelsBoldForTextBoxDisabledLook").addClass("textlabelsBoldForTextBox");
        $("#agentInfo").removeClass("textlabelsBoldForTextBoxDisabledLook").addClass("textlabelsBoldForTextBox");
        $("#agentName").removeAttr("readonly");
        $("#agentInfo").removeAttr("readonly");
    }
}
function copyValuesofAgent() {
    var agentNo = $("#agentNumber").val();
    if ($("#rtdTransaction").val() == "Y" && null != agentNo && agentNo != '') {
        $("#agentInfo").val(agentNo);
        /*else {
         document.getElementById("rtdTransaction").selectedIndex = 1;
         document.getElementById("rtdTransaction").value = "";
         congAlert("You must first select an agent");
         } */
    } else {
        $("#agentInfo").val('');
    }
}

function getDoorOriginCityGoogleMap(path) {
    var doorOriginCity = $("#doorOriginCityZip").val();
    if (doorOriginCity != null && doorOriginCity != "") {
        if (doorOriginCity.indexOf("-") > -1 && doorOriginCity.indexOf("/") > -1) {
            var doorCityName = doorOriginCity.substring(doorOriginCity.indexOf("-") + 1, doorOriginCity.indexOf("/"));
            var doorZipCode = doorOriginCity.substring(0, doorOriginCity.indexOf("-"));
        }
        var href = path + "/lclServiceMap.do?methodName=displayDoorOriginCityMap&countryName=" + doorCityName + "&zipCode=" + doorZipCode;
        $("#lclDoorOriginCityMap").attr("href", href);
        $("#lclDoorOriginCityMap").colorbox({
            iframe: true,
            width: "100%",
            height: "90%",
            title: "Door Origin City Map"
        });
    }
    else {
        $.prompt("Please Select Door Origin City");
    }
}

function getOriginGoogleMap(path, methodName, flagName) {
    var portOfOrigin = $("#portOfOriginR").val();
    if (portOfOrigin == null || portOfOrigin == "") {
        $.prompt("Origin CFS is required");
        $("#portOfOriginR").css("border-color", "red");
    } else {
        if (flagName == 'I') {
            originUnloc = portOfOrigin.substring(portOfOrigin.lastIndexOf("(") + 1, portOfOrigin.lastIndexOf(")"));
        } else {
            originUnloc = $("#originUnlocationCode").val();
        }
        var href = path + "/lclServiceMap.do?methodName=" + methodName + "&countryName=" + originUnloc;
        $("#lclOriginMap").attr("href", href);
        $("#lclOriginMap").colorbox({
            iframe: true,
            width: "100%",
            height: "90%",
            title: "Origin Map"
        });
    }
}

function getDestinationGoogleMap(path, methodName, flagName) {
    var finalDestination = $("#finalDestinationR").val();
    if (finalDestination == null || finalDestination == "") {
        $.prompt("Destination is required");
        $("#finalDestinationR").css("border-color", "red");
    } else {
        if (flagName == 'I') {
            countryName = $("#unlocationCode").val();
        } else {
            if (finalDestination.lastIndexOf("/") > -1 && finalDestination.lastIndexOf("(") > -1) {
                countryName = finalDestination.substring(finalDestination.lastIndexOf("/") + 1, finalDestination.lastIndexOf("("));
            }
        }
        var href = path + "/lclServiceMap.do?methodName=" + methodName + "&countryName=" + countryName;
        $("#lclDestinationMap").attr("href", href);
        $("#lclDestinationMap").colorbox({
            iframe: true,
            width: "100%",
            height: "90%",
            title: "Destination Map"
        });
    }
}

function showSpotRateInfo(path, fileNumberId, fileNumber) {
    var destination = getDestination();
    var href = path + "/lclQuote.do?methodName=displaySpotRate&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&destination=" + destination;
    $(".lclSpotRate").attr("href", href);
    $(".lclSpotRate").colorbox({
        iframe: true,
        width: "37%",
        height: "65%",
        title: "SpotRate"
    });
}



function toggleGeneralInformation() {// generalInfomation tab  hide or show
    var disp = document.getElementById('generalInformation').style.display;
    if (disp === "block" || disp === "") {
        $('#expand').text('');
        $('#collapse').text('Click to Expand');
    }
    else {
        $('#collapse').text('');
        $('#expand').text('Click to Hide');
    }
    jQuery('#generalInformation').slideToggle();
}

function displayNotes(path, acctNo, id, fileId, fileNo) {
    var href = "";
    var refpath = path + "/lclRemarks.do?methodName=display&actions=specialNotes&moduleId=Quote&fileNumberId=" + fileId + "&fileNumber=" + fileNo + "";
    if (id == 'clntNotes') {
        href = refpath + "&clntAcctNo=" + acctNo + "&clntId=" + id;
    }
    if (id == 'shpNotes') {
        href = refpath + "&shpAcctNo=" + acctNo + "&shpId=" + id;
    }
    if (id == 'fwdNotes') {
        href = refpath + "&frwdAcctNo=" + acctNo + "&fwdId=" + id;
    }
    if (id === 'conNotes' || id === 'notNotes' || id === 'noty2Notes') {
        href = refpath + "&consAcctNo=" + acctNo + "&consId=conNotes";
    }
    $('#' + id).attr("href", href);
    $('#' + id).colorbox({
        iframe: true,
        width: "80%",
        height: "80%",
        title: "Notes"
    });
}
function notesCount(path, id, acctNo, type) {
    if (acctNo !== null) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkNotesCount",
                param1: acctNo,
                param2: $('#moduleName').val(),
                param3: type
            },
            async: false,
            success: function (data) {
                if (data === "true") {
                    $('#' + id).attr("src", path + "/img/icons/e_contents_view1.gif");
                } else {
                    $('#' + id).attr("src", path + "/img/icons/e_contents_view.gif");
                }
            }
        });
    }
}

//update Contact Details by Vendor (Imports and Exports)
function openLclContactInfo(path, party) {
    var vendorNo = "";
    var vendorName = "";
    var subtype = "";
    if (party === 'Supplier') {
        vendorNo = jQuery("#supplierCode").val();
        vendorName = jQuery("#supplierName").val();
        subtype = 'LCL_IMPORT_SUPPLIER';
    } else if (party === 'Shipper') {
        vendorNo = jQuery("#shipperCode").val();
        vendorName = jQuery("#shipperName").val();
        subtype = 'LCL_IMPORT_SHIPPER';
    } else if (party === 'Consignee') {
        vendorNo = jQuery("#consigneeCode").val();
        vendorName = jQuery("#consigneeName").val();
        subtype = 'LCL_IMPORT_CONSIGNEE';
    } else if (party === 'Notify') {
        vendorNo = jQuery("#notifyCode").val();
        vendorName = jQuery("#notifyName").val();
        subtype = 'LCL_IMPORT_NOTIFY';
    }
    vendorName = vendorName.replace("&", "amp;");
    var href = path + "/lclContactDetails.do?methodName=display&vendorName=" + vendorName + "&vendorNo=" + vendorNo + "&vendorType=" + subtype;
    $(".contactR").attr("href", href);
    $(".contactR").colorbox({
        iframe: true,
        width: "90%",
        height: "90%",
        title: "Contact"
    });
}

function displayNotesPopUp(path, fileNumberId, fileNumber, moduleName) {
    var href = path + "/lclRemarks.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&moduleId=Quote" + "&actions=manualNotes" + "&moduleName=" + moduleName;
    $(".notes").attr("href", href);
    $(".notes").colorbox({
        iframe: true,
        width: "60%",
        height: "62%",
        title: "Notes"
    });
}

function getUnlocationCode(selectorName) {
    var unlocCode = "";
    var obj = document.getElementById(selectorName);
    if (obj !== undefined && obj !== null && obj.value !== "") {
        unlocCode = document.getElementById(selectorName).value;
    }
    if (unlocCode.lastIndexOf("(") > -1 && unlocCode.lastIndexOf(")") > -1) {
        return unlocCode.substring(unlocCode.lastIndexOf("(") + 1, unlocCode.lastIndexOf(")"));
    }
    return "";
}
function quotePreviewReport() {//Preview Button for Quote PDF for common in imp & Exp
    if (isQuoteFormChanged) {
        $('#reportSaveFlag').val('true');
        var moduleName = $('#moduleName').val();
        validateQuoteform(moduleName);
    } else {
        quoteReport();
    }
}
function quoteReport() {
    var fileId = $('#fileNumberId').val();
    var fileNo = $('#fileNumber').val();
    showLoading();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclPrintUtil",
            methodName: "printQuoteReport",
            param1: "",
            param2: fileId,
            param3: fileNo,
            param4: "Quotation",
            request: "true"
        },
        success: function (data) {
            closePreloading();
            window.parent.showLightBox('Quotation', path + '/servlet/FileViewerServlet?fileName=' + data, 800, 1000);
        }
    });
}
function thirdPartyAcct() {
    if ($('#thirdPartyDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#thirdpartyDisableAcct').val() + "</span>");
        $('#thirdPartyName').val('');
        $('#thirdPartyDisabled').val('');
        $('#thirdPartyAccount').val('');

    }

}
function qtHotCodeValidate(fileNumberId) {
    var statusFlag = false;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "qtHotCodeValidate",
            param1: fileNumberId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            statusFlag = data;
        }
    });
    return statusFlag;
}
function qtHazmatCodeExit(fileNumberId) {
    var statusFlag = false;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "qtHazmatCodeExit",
            param1: fileNumberId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            statusFlag = data;
        }
    });
    return statusFlag;
}

function calculateRelayTTREVCharge() {
    var moduleName = $('#moduleName').val();
    var relay = $('input:radio[name=relayOverride]:checked').val();
    var fileId = $('#fileNumberId').val();
    var pooId = $("#portOfOriginId").val();
    var polId = $('#portOfLoadingId').val();
    var polUnloc = $('#polUnlocationcode').val();
    if (moduleName === 'Exports' && relay !== undefined && relay === 'Y' && pooId !== polId) {
        $.prompt("Since the POL has changed, T&T Charges will be recalculated", {
            buttons: {
                Ok: 1
            },
            submit: function (v) {
                if (v === 1) {
                    showProgressBar();
                    submitAjaxFormForRelayTTREV('calculateTTREVCharge', '#lclQuoteForm', '#chargeDesc', fileId, relay, polUnloc);
                    $.prompt.close();
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

function RecalculateRelayCharge(radioValue, doorOriginCityZip, pickupReadyDate) {
    var module = $('#moduleName').val();
    var origin = getUnlocationCode('portOfOriginR');
    var pol = getUnlocationCode('portOfLoading');
    var pod = getUnlocationCode('portOfDestination');
    var destination = getUnlocationCode('finalDestinationR');
    var methodName = "";
    var doorOriginCityZip = $("#doorOriginCityZip").val();
    var pickupReadyDate = $('#pickupReadyDate').val();
    methodName = "calculateCharges";
    var fileId = $('#fileNumberId').val();
    if (fileId !== null && fileId !== '') {
        $.prompt("Since the POL has changed, T&T Charges will be recalculated", {
            buttons: {
                Ok: 1
            },
            submit: function (v) {
                if (v === 1) {
                    showProgressBar();
                    submitAjaxFormForRates(methodName, '#lclQuoteForm', '#chargeDesc', origin, destination, pol, pod, radioValue, doorOriginCityZip, pickupReadyDate);
                    if (checkQuoteRates(fileId)) {
                        $('#showAstar').show();
                        $('#showAstarDestn').show();
                        if ($('#portOfOriginR').val() != "") {
                            $('#portOfOriginR').addClass("text-readonly");
                            $('#portOfOriginR').attr("readonly", true);
                        }
                        $('#finalDestinationR').addClass("text-readonly");
                        $('#finalDestinationR').attr("readonly", true);
                    }
                    $.prompt.close();
                }
            }
        });
    }
}
function confirmBussinessUnitInQt(fileNumberId, newBrand) {
    var oldBrand = $("#oldBrand").val();
    var userId = $("#loginUserId").val();
    var oldValue = oldBrand === "ECU" ? "Ecu Worldwide" : oldBrand === "ECI" ? "Econo" : "OTI";
    var newValue = newBrand === "ECU" ? "Ecu Worldwide" : newBrand === "ECI" ? "Econo" : "OTI";
    var textMessage = "Please note that the Brand is changing from <span style=color:red>"
            + oldValue + "</span>" + " to " + "<span style=color:red>" + newValue + "</span>.";
    if (fileNumberId !== null && fileNumberId !== "" && getPreviousBusinessUnit(newBrand, fileNumberId)) {
        $.prompt(textMessage, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    $("#oldBrand").val(newBrand);
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.lcl.dwr.LclDwr",
                            methodName: "updateBusinessUnit",
                            param1: fileNumberId,
                            param2: newBrand,
                            param3: "",
                            param4: userId,
                            param5: oldBrand,
                            dataType: "json"
                        }
                    });
                } else {
                    switch (oldBrand) {
                        case "ECU":
                            $('#ECU').attr("checked", true);
                            break;
                        case "OTI":
                            $('#ECU').attr("checked", true);
                            break;
                        case "ECI":
                            $('#ECI').attr("checked", true);
                            break;
                    }
                    $.prompt.close();
                }
            }
        });
    }
}

function updateEconoEcuLineForQuote() {
    if ($("#fileNumberId").val() !== '') {
        showProgressBar();
        $("#methodName").val("updateEconoEculineQT");
        var params = $("#lclQuoteForm").serialize();
        $.post($("#lclQuoteForm").attr("action"), params, function (data) {
            $("input:radio[name=businessUnit]").val([data.businessUnit]);
            $('#hotCodesList').html(data.hotCodesList);
            hideProgressBar();
        });
    }
}

function coloadOrTerminal() {
    var trmnum = $('#trmnum').val();
    var rateType = $('input:radio[name=rateType]:checked').val();
    if ($("#fileNumberId").val() === '' && (trmnum === '59' || (rateType !== undefined && rateType === 'C'))) {
        $('#aesByN').attr('checked', true);
    }
    if (trmnum === '59' && $(".econo").text() === "Econo") {
        $("input:radio[name=businessUnit]").val(['ECI']);
        var businessUnit = $('input:radio[name=businessUnit]:checked').val();
        if ($("#fileNumberId").val() !== '') {
            showProgressBar();
            updateEconoOrEculine(businessUnit, $('#fileNumberId').val(), "", "QT-AutoNotes");
            hideProgressBar();
        }
    }
}
// for Exports #mantis :13725
function validateInlandRates() {
    var fileId = $('#fileNumberId').val();
    var pooDoor = $('input:radio[name=pooDoor]:checked').val();
    if (fileId !== "" && pooDoor === 'Y') {
        if (isValidateRates(fileId)) {
            var txt = 'This will cause the Inland rate to be removed. You must enter/calculate it again.';
            $.prompt(txt, {
                buttons: {
                    Ok: 1
                },
                submit: function (v) {
                    if (v === 1) {
                        showProgressBar();
                        submitAjaxFormForDeletingInland('deleteInlandForExports', '#lclQuoteForm', '#chargeDesc');
                        hideProgressBar();
                        $.prompt.close();
                    }
                }
            });
        } else {
            submitAjaxFormForDeletingInland('deleteInlandForExports', '#lclQuoteForm', '#chargeDesc');
        }
    }
}

function submitAjaxFormForDeletingInland(methodName, formName, selector) {
    var fileId = $('#fileNumberId').val();
    var moduleName = $('#moduleName').val();
    var deliveryCargoToCode = $('#deliveryCargoToCode').val();
    var deliverCargoToName = $('#deliverCargoToName').val();
    var deliverCargoToAddress = $('#deliverCargoToAddress').val();
    var deliverCargoToCity = $('#deliverCargoToCity').val();
    var deliverCargoToState = $('#deliverCargoToState').val();
    var deliverCargoToZip = $('#deliverCargoToZip').val();
    var deliverCargoToPhone = $('#deliverCargoToPhone').val();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&fileId=" + fileId + "&moduleName=" + moduleName;
    params += "&deliverCargoToName=" + deliverCargoToName + "&deliverCargoToAddress=" + deliverCargoToAddress;
    params += "&deliverCargoToCity=" + deliverCargoToCity + "&deliverCargoToState=" + deliverCargoToState;
    params += "&deliverCargoToZip=" + deliverCargoToZip + "&deliverCargoToPhone=" + deliverCargoToPhone;
    params += "&deliveryCargoToCode=" + deliveryCargoToCode;

    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                $.fn.colorbox.close();
            });
}

function forceClientRoleDuty() {
    var quoteClient = false;
    var roleId = $('#userRoleId').val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO",
            methodName: "getRoleDetails",
            param1: "lcl_quote_client",
            param2: roleId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            quoteClient = data;
        }
    });
    return quoteClient;
}
function addQuoteClientHotCodes(acctNo) {
    $("#methodName").val("addingQuoteClientHotCodes");
    var params = $("#lclQuoteForm").serialize();
    params += "&accountNo=" + acctNo;
    $.post($("#lclQuoteForm").attr("action"), params, function (data) {
        $("#hotCodesList").html(data);
        $("#hotCodesList", window.parent.document).html(data);
    });
}