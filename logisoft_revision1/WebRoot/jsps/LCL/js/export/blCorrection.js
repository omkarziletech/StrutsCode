var formChanged = false;
$(document).ready(function () {
    $("[title != '']").not("link").tooltip();
    detectFormChange();
    displayGrids();
    $("#expThirdPartyName").keyup(function () {
        if ($(this).val() === "") {
            $("#expThirdPartyNo").val("");
        }
    });
    $("#expAgentName").keyup(function () {
        if ($(this).val() === "") {
            $("#expAgentNo").val("");
        }
    });
    $("#expShipperName").keyup(function () {
        if ($(this).val() === "") {
            $("#expShipperNo").val("");
        }
    });
    $("#expForwarderName").keyup(function () {
        if ($(this).val() === "") {
            $("#expForwarderNo").val("");
        }
    });
    $(".commodity_number").each(function () {
        $(this).attr('alt', '');
    });
    $(".commodity_name").each(function () {
        $(this).attr('alt', '');
    });
});
function setReadOnlyViewMode() {
    var viewMode = $('#viewMode').val();
    if (viewMode === "view") {
        var form = document.getElementById('lclCorrectionForm');
        var element;
        for (var i = 0; i < form.elements.length; i++) {
            element = form.elements[i];
            if (element.type == "select-one" || element.type == "radio" ||
                    element.type == "checkbox" || element.type == "textarea") {
                element.style.border = 0;
                element.disabled = true;
            }
        }
        $('#addCharges').hide();
        $('#saveCorreBtn').hide();
    }
}
function detectFormChange() {
    var form = "#lclCorrectionForm";
    var $selector = $(form + " input[type=text], " + form + " textarea");
    $($selector).each(function () {
        $(this).data('initial_value', $(this).val());
    });
    $($selector).keyup(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            formChanged = true;
        }
    });
    $($selector).change(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            formChanged = true;
        }
    });
    $(form).bind('change paste', function () {
        formChanged = true;
    });
}
function isFormChanged() {
    var form = "#lclCorrectionForm";
    var $selector = $(form + " input[type=text], " + form + " textarea");
    $($selector).each(function () {
        $(this).data('initial_value', $(this).val());
    });
    $($selector).keyup(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            formChanged = true;
        }
    });
    $($selector).change(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            formChanged = true;
        }
    });
    $(form).bind('change paste', function () {
        formChanged = true;
    });
    return formChanged;
}
function displayGrids() {
    setCorrectionCode();
    // addMemoEmailSection("");
    setReadOnlyViewMode();
}
function setCorrectionCode() {
    var corrCode = $("#correctionCodeId option:selected").text();
    var correctionCode = corrCode.split("-");
    var viewMode = $('#viewMode').val();
    if (correctionCode[0] === '001' && viewMode !== "view") {
        setEnableTextBox('expShipperName');
        setEnableTextBox('expAgentName');
        setEnableTextBox('expForwarderName');
        setEnableTextBox('expThirdPartyName');
        $(".commodityRadio").hide();
        $("#calculateCharges").hide();
    } else if (correctionCode[0] === '019' && viewMode !== "view") {
        $(".commodityRadio").show();
        $("#calculateCharges").show();
    } else {
        setDisableTextBox('expShipperName');
        setDisableTextBox('expAgentName');
        setDisableTextBox('expForwarderName');
        setDisableTextBox('expThirdPartyName');
        $(".commodityRadio").hide();
        $("#calculateCharges").hide();
    }
}
function setEnableCode() {
    setCorrectionCode();
}
function addMemoEmailSection(differnceAmt) {
    var fileId = $('#fileId').val();
    var correctionId = $('#correctionId').val();
    var newAcctNo = getCustomer();
    var oldAcctNo = $('#customerAcctNo').val();
    var viewMode = $('#viewMode').val();
    var cnType = getCodeType();
    if (cnType != "T" && cnType != "U" && cnType != "Select Correction Type") {
        $.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getMemoEmailIds",
                forward: "/jsps/LCL/lclMemoEmailSection.jsp",
                param1: fileId,
                param2: correctionId,
                param3: cnType,
                param4: oldAcctNo,
                param5: newAcctNo,
                param6: differnceAmt,
                param7: viewMode,
                request: true
            },
            async: false,
            success: function (data) {
                $("#memoDivSection").html(data);
                if (viewMode == "view") {
                    jQuery("#memoDivSection").find(".creditEmailId, .debitEmailId").attr("disabled", true);
                    jQuery("#creditMemoEmailDiv").attr("class", "divstyleThinDisabled");
                    jQuery("#debitMemoEmailDiv").attr("class", "divstyleThinDisabled");
                }
            }
        });
    }
}
function getCodeType() {
    var cnType = '';
    var cnTypeVal = $("#correctionTypeId option:selected").text().toLowerCase();
    if (cnTypeVal != '' && cnTypeVal != undefined) {
        var codetypeArray = cnTypeVal.split("-");
        cnType = codetypeArray[0].toUpperCase();
    }
    return cnType;
}

function validateAcct(id1, id2, id3) {
    if ($("#" + id1).val() !== '') {
        $.prompt("This Customer is disabled and merged with <font color='red'>" + $("#" + id1).val() + "</font>");
        $("#" + id2).val("");
        $("#" + id3).val("");
        return false;
    }
}
function saveCorrection() {
    if (validateCorrection()) {
        var errorMessage = getMemoEmailForMainCorrection();
        if (errorMessage !== "") {
            $.prompt(errorMessage);
        }
        else {
            var ship = $("#oldShipper").val() === $("#expShipperNo").val() ? true : false;
            var Agent = $("#oldAgent").val() === $("#expAgentNo").val() ? true : false;
            var Fwd = $("#oldForwarder").val() === $("#expForwarderNo").val() ? true : false;
            var Third = $("#oldThirdParty").val() === $("#expThirdPartyNo").val() ? true : false;
            if ($('#correctionId').val() === '0' && ship && Agent && Fwd && Third) {
                $.prompt("Plese Enter New Amount to Save Correction");
                return false;
            }
            showLoading();
            $('#expShipperNo').val($('#expShipperNo').val());
            $('#expAgentNo').val($('#expAgentNo').val());
            $('#expThirdPartyNo').val($('#expThirdPartyNo').val());
            $("#expForwarderNo").val($("#expForwarderNo").val());
            $('#customerAcctNo').val(getCustomer());
            $("#methodName").val('saveCorrection');
            $("#lclCorrectionForm").submit();
        }
    }
}
function validateCorrection() {
    if (document.lclCorrectionForm.comments.value.toString().trim() == "") {
        $.prompt("Please enter Comments");
        document.lclCorrectionForm.comments.style.borderColor = "red";
        document.lclCorrectionForm.comments.value = "";
        return false;
    }
    if (document.lclCorrectionForm.correctionType.value == "0") {
        $.prompt("Please enter Correction Type");
        document.lclCorrectionForm.correctionType.style.borderColor = "red";
        return false;
    }
    if (document.lclCorrectionForm.correctionCode.value == "0") {
        $.prompt("Please enter Correction Code");
        document.lclCorrectionForm.correctionCode.style.borderColor = "red";
        return false;
    }
    var code = getCodeType();
    if ((code == 'F' || code == 'H' || code == 'D') && (document.getElementById("forwarderNo").value == null || document.getElementById("forwarderNo").value == "")) {
        $.prompt("Forwarder is required in BL.");
        return false;
    } else if ((code == 'G' || code == 'I' || code == 'C') && (document.getElementById("shipperNo").value == null || document.getElementById("shipperNo").value == "")) {
        $.prompt("Shipper is required in BL.");
        return false;
    } else if (code == 'E' && (document.getElementById("thirdPartyAcctNo").value == null && document.getElementById("thirdPartyAcctNo").value == "")) {
        $.prompt("Third Party is required in BL.");
        return false;
    }
    return true;
}

function getMemoEmailForMainCorrection() {
    var creditEmail = [];
    var debitEmail = [];
    var totalCreditEmailCount = 0;
    var totalDebitEmailCount = 0;
    var errMsg = "";
    jQuery(".creditEmailId").each(function () {
        totalCreditEmailCount++;
        if (jQuery(this).is(":checked") == true) {
            creditEmail.push(jQuery(this).val());
        }
    });
    jQuery(".debitEmailId").each(function () {
        totalDebitEmailCount++;
        if (jQuery(this).is(":checked") == true) {
            debitEmail.push(jQuery(this).val());
        }
    });
    jQuery('#creditMemoEmail').val(creditEmail);
    jQuery('#debitMemoEmail').val(debitEmail);
    if (totalCreditEmailCount > 0 && creditEmail.length <= 0) {
        errMsg += "-->Please choose atleast one credit email<br>";
    }
    if (totalDebitEmailCount > 0 && debitEmail.length <= 0) {
        errMsg += "-->Please choose atleast one debit email<br>";
    }
    return errMsg;
}
function editCorrection(correctionId) {
    $('#correctionId').val(correctionId);
    $("#methodName").val('editCorrection');
    $("#lclCorrectionForm").submit();
}
function viewCorrection(correctionId) {
    showLoading();
    $("#methodName").val('viewCorrections');
    $('#correctionId').val(correctionId);
    $("#lclCorrectionForm").submit();
}

function isContainNonApprovedCorrection() {
    var flag = true;
    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getLatestCorrectionStatus",
            param1: $('#fileId').val()
        },
        async: false,
        success: function (data) {
            if (data === "O") {
                $.prompt("Please Post Last Correction before creating new Correction for this BL");
                flag = false;
            }
        }
    });
    return flag;
}
function addCorrection() {
    if (isContainNonApprovedCorrection()) {
        $("#methodName").val('addCorrection');
        $("#lclCorrectionForm").submit();
    }
}
function viewVoidCorrections(path) {
    var fileId = $('#fileId').val();
    var fileNo = $('#fileNo').val();
    var href = path + "/blCorrection.do?methodName=viewVoidCorrection&fileNo=" + fileNo + "&fileId=" + fileId;
    $.colorbox({
        iframe: true,
        width: "90%",
        height: "80%",
        href: href,
        title: "VOIDED Correction"
    });
}
function displayCorrectionNotesPopUp() {
    var path = document.lclCorrectionForm.path.value;
    var fileId = $("#fileId").val();
    var fileNo = $("#fileNo").val();
    var selectedMenu = $("#selectedMenu").val();
    var href = path + "/lclRemarks.do?methodName=display&fileNumberId=" + fileId + "&fileNumber=" + fileNo + "&moduleName=" + selectedMenu;
    href = href + "&actions=11";
    $.colorbox({
        iframe: true,
        width: "90%",
        height: "70%",
        href: href,
        title: "Notes"
    });
}
function getCustomer() {
    var customer = "";
    var code = getCodeType();
    if (code == 'B') {
        customer = document.getElementById("blagentNo").value;
    } else if ((code == 'F' || code == 'H' || code == 'D')) {
        customer = document.getElementById("blforwarderNo").value;
    } else if ((code == 'G' || code == 'I' || code == 'C')) {
        customer = document.getElementById("blshipAcct").value;
    } else if (code == 'E') {
        customer = document.getElementById("thirdPartyAcctNos").value;
    }
    return customer;
}
function validateCorrection() {
    if (document.lclCorrectionForm.comments.value.toString().trim() == "") {
        $.prompt("Please enter Comments");
        document.lclCorrectionForm.comments.style.borderColor = "red";
        document.lclCorrectionForm.comments.value = "";
        return false;
    }
    if (document.lclCorrectionForm.correctionType.value == "0") {
        $.prompt("Please enter Correction Type");
        document.lclCorrectionForm.correctionType.style.borderColor = "red";
        return false;
    }
    if (document.lclCorrectionForm.correctionCode.value == "0") {
        $.prompt("Please enter Correction Code");
        document.lclCorrectionForm.correctionCode.style.borderColor = "red";
        return false;
    }
    var code = getCodeType();
    if ((code == 'F' || code == 'H' || code == 'D') && ($("#forwarderNo").val() == null || $("#forwarderNo").val() == "")) {
        $.prompt("Forwarder is required in BL.");
        return false;
    } else if ((code == 'G' || code == 'I' || code == 'C') && ($("#shipperNo").val() == null || $("#shipperNo").val() == null == "")) {
        $.prompt("Shipper is required in BL.");
        return false;
    } else if (code == 'E' && ($("#thirdPartyAcctNo").val() == null && $("#thirdPartyAcctNo").val() == "")) {
        $.prompt("Third Party is required in BL.");
        return false;
    }
    return true;
}
function addCorrectionCharge(path, correctionId) {
    if (validateCorrection()) {
        var blBillToParty = $('#blBillToParty').val();
        var href = path + "/blCorrection.do?methodName=addCorrectionCharge&oldAmount=0.00&correctionId="
                + correctionId + "&billToParty=" + blBillToParty + "&correctionType=" + $("#correctionTypeId").val();
        $.colorbox({
            iframe: true,
            width: "80%",
            height: "80%",
            href: href,
            title: "Add Correction Charge"
        });
    }
}
function editCorrectionCharges(path, chargeId, chargeCode, chargeDesc, oldAmount, newAmount,
        differenceAmount, correctionId, billToParty, correctionChargeId, lclBookingAcId) {
    if (validateCorrection()) {
        var corrCode = $("#correctionCodeId option:selected").text();
        var correctionCode = corrCode.split("-");
        var href = path + "/blCorrection.do?methodName=editCorrectionCharge&chargeId=" + chargeId + "&chargeCode=" + chargeCode + "&chargeDescriptions=" + chargeDesc;
        href = href + "&oldAmount=" + oldAmount + "&newAmount=" + newAmount + "&differenceAmount=" + differenceAmount + "&correctionId=" + correctionId;
        href = href + "&billToParty=" + billToParty + "&correctionChargeId=" + correctionChargeId + "&correctionCode=" + correctionCode[0];
        href = href + "&lclBookingAcId=" + lclBookingAcId + "&correctionType=" + $("#correctionTypeId").val();
        $.colorbox({
            iframe: true,
            width: "90%",
            height: "90%",
            href: href,
            title: "Edit Correction Charge"
        });
    }
}
function goBackCorrection() {
    showLoading();
    $("#methodName").val('goBackCorrection');
    $("#lclCorrectionForm").submit();
}
function setEnableTextBox(id) {
    $('#' + id).removeClass('text-readonly');
    $('#' + id).removeAttr('readonly');
    $('#' + id).addClass("textlabelsBoldForTextBox");
}
function setDisableTextBox(id) {
    $('#' + id).removeClass("textlabelsBoldForTextBox");
    $('#' + id).addClass('text-readonly');
    $('#' + id).attr('readonly', true);
}

function deleteCorrection(correctionId, noticeNo, blNo) {
    var txt = 'Are you sure You want to delete?';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showLoading();
                $("#methodName").val('deleteCorrections');
                $("#correctionId").val(correctionId);
                $("#noticeNo").val(noticeNo);
                $("#blNo").val(blNo);
                $("#lclCorrectionForm").submit();
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function deleteCorrectionCharges(correctionChargeId) {
    var txt = 'Are you sure You want to delete?';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showLoading();
                $("#methodName").val('deleteCorrectionCharges');
                $('#correctionChargeId').val(correctionChargeId);
                $("#lclCorrectionForm").submit();
                hideProgressBar();
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function closeCorrection() {
    parent.$.colorbox.close();
}
function displayCorrectionNotes(path) {
    var fileId = $("#fileId").val();
    var fileNo = $("#fileNo").val();
    var selectedMenu = "Exports";
    var href = path + "/lclRemarks.do?methodName=display&fileNumberId=" + fileId + "&fileNumber=" + fileNo + "&moduleName=" + selectedMenu;
    href = href + "&actions=correction";
    $.colorbox({
        iframe: true,
        width: "90%",
        height: "70%",
        href: href,
        title: "Notes"
    });
}
var path = "/" + window.location.pathname.split('/')[1];
function PrintReportsOpenSeperately(correctionId, noticeNo, buttonValue) {
    var href = "";
    var fileNo = $("#fileNo").val();
    var fileId = $("#fileId").val();
    var selectedMenu = "Exports";
    var creditDebitNotePrint = $("#blNo").val();
    var issuTerm = $("#issuingTerminal").val();
    href = path + "/printConfig.do?screenName=LclCreditDebitNote&CreditDebitNotePrint=" + creditDebitNotePrint + "&fileNo=" + fileNo;
    href = href + "&noticeNo=" + noticeNo + "&fileId=" + fileId + "&correctionId=" + correctionId + "&selectedMenu=" + selectedMenu +"&issuingTerminal=" + issuTerm;
    mywindow = window.open(href, '', 'width=800,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}

function enableCommodity(index) {
    $(".commodityRadio").each(function () {
        if ($(this).val() === index) {
            $("#commodityType" + index).removeClass("commodity_name");
            $("#commodityType" + index).attr("readOnly", false);
            $("#commodityType" + index).addClass("commodity_name_enable");
            $("#commodityType" + index).attr("alt", "COMMODITY_TYPE_NAME");
            $("#commodityNo" + index).removeClass("commodity_number");
            $("#commodityNo" + index).attr("readOnly", false);
            $("#commodityNo" + index).addClass("commodity_number_enable");
            $("#commodityNo" + index).attr("alt", "COMMODITY_TYPE_CODE");

            $("#KGS" + index).attr("readOnly", false);
            $("#KGS" + index).removeClass("commodity_number");
            $("#CBM" + index).attr("readOnly", false);
            $("#CBM" + index).removeClass("commodity_number");

            $("#LBS" + index).attr("readOnly", false);
            $("#LBS" + index).removeClass("commodity_number");
            $("#CFT" + index).attr("readOnly", false);
            $("#CFT" + index).removeClass("commodity_number");
        } else {
            $("#commodityType" + $(this).val()).addClass("commodity_name");
            $("#commodityType" + $(this).val()).attr("readOnly", true);
            $("#commodityType" + $(this).val()).removeClass("commodity_name_enable");
            $("#commodoityType" + $(this).val()).attr("alt", "");
            $("#commodityNo" + $(this).val()).addClass("commodity_number");
            $("#commodityNo" + $(this).val()).attr("readOnly", true);
            $("#commodityNo" + $(this).val()).removeClass("commodity_number_enable");
            $("#commodityNo" + $(this).val()).attr("alt", "");
            $("#KGS" + $(this).val()).attr("readOnly", true);
            $("#KGS" + $(this).val()).addClass("commodity_number");
            $("#CBM" + $(this).val()).attr("readOnly", true);
            $("#CBM" + $(this).val()).addClass("commodity_number");

            $("#LBS" + $(this).val()).attr("readOnly", true);
            $("#LBS" + $(this).val()).addClass("commodity_number");
            $("#CFT" + $(this).val()).attr("readOnly", true);
            $("#CFT" + $(this).val()).addClass("commodity_number");
        }
    });
}

function checkForNumberAndDecimal(obj) {
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        sampleAlert("This field should be Numeric");
    }
}

function calculateCorrectedCharges(path, correctionId) {
    if (validatedCommodityChanges() && validateCorrection()
            && isContainNonApprovedCorrection()) {
        $.prompt("The Rates will be recomputed as per new value and correction will be saved. Do you want to continue.", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    showLoading();
                    $('#correctionId').val(correctionId);
                    $("#methodName").val('reCalculateCharges');
                    $("#lclCorrectionForm").submit();
                } else {
                    $("#methodName").val('addCorrection');
                    $("#lclCorrectionForm").submit();
                }
            }
        });
    }
}

function validatedCommodityChanges() {
    var value = $("input:radio[name=commodity]:checked").val();
    if (value === undefined) {
        $.prompt("Please select and edit Commodity details.");
        return false;
    } else {
        var oldCommName = $("#oldCommodityType" + value).val();
        var oldCommNo = $("#oldCommodityNo" + value).val();
        var oldKGS = $("#oldKGS" + value).val();
        var oldCFT = $("#oldCBM" + value).val();
        var oldLBS = $("#oldLBS" + value).val();
        var oldCBM = $("#oldCFT" + value).val();

        var commName = $("#commodityType" + value).val();
        var commNo = $("#commodityNo" + value).val();
        var KGS = $("#KGS" + value).val();
        var CFT = $("#CBM" + value).val();
        var LBS = $("#LBS" + value).val();
        var CBM = $("#CFT" + value).val();

        if ((commName === oldCommName) && (oldCommNo === commNo)
                && (oldKGS === KGS) && (oldCFT === CFT) && (oldLBS === LBS) && (oldCBM === CBM)) {
            $.prompt("No Modification Detected to compute the rates.");
            return false;
        }
    }
    return true;
}

function ConvertToCFT(obj, index) {
    var cftValue = obj.value * 35.314;
    $("#CFT" + index).val(cftValue.toFixed(2));
}
function ConvertToCBM(obj, index) {
    var cftValue = obj.value / 35.314;
    $("#CBM" + index).val(cftValue.toFixed(3));
}
function ConvertToKGS(obj, index) {
    var cftValue = obj.value / 2.2046;
    $("#KGS" + index).val(cftValue.toFixed(3));
}
function ConvertToLBS(obj, index) {
    var cftValue = obj.value * 2.2046;
    $("#LBS" + index).val(cftValue.toFixed(2));
}

function setCommonValues(index) {
    $("#barrel" + index).val(false);
}