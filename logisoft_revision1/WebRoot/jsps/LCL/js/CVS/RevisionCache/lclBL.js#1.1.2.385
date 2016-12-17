var formChanged = false;
var loadForm;
var bl_pier;
var htabsFlag = false;
var countLinesError = "";
var path = "/" + window.location.pathname.split('/')[1];
jQuery(document).ready(function () {
//setPostBtn();
    checkBilltoCode();
    updateBillToParty();
    isSameasConsignee();
    checkBillingType('L');
    clearNotify();
    setCreditStatusOnLoad('shipperCreditStatusValue', 'status', 'shipperCreditLimitValue', 'limit');
    setCreditStatusOnLoad('fwdCreditStatusValue', 'fwdStatus', 'fwdCreditLimitValue', 'fwdLimit');
    setCreditStatusOnLoad("consCreditStatusValue", "consStatus", "consCreditLimitValue", "consLimit");
    setCreditStatusOnLoad("notyCreditStatusValue", "notiStatus", "notyCreditLimitValue", "notiLimit");
    setCreditStatusOnLoad("thirdpartyCreditStatusValue", "thirdPartyStatus", "thirdpartyCreditLimitValue", "thirdPartyLimit");
    if (($('#loginUserId').val() !== $('#blOwnerId').val())
            && $("#poolPostedData").text().trim() === 'POOL') {
        checkBlOwner();
    }

// checking role duty for Bl_Owner
    if ($('#editBlOwnerRole').val() !== 'true') {
        $('#blOwner').attr('alt', ' ');
        $('#blOwner').css("background", "#CCEBFF");
        $('#blOwner').attr("readonly", true);
    }
    showShipper();
    showConsignee();
    showNotifyName();
    showFwdName();
    if ($("input:radio[name='freeBl']:checked").val() == 'Y') {
        if ($("input:radio[name='spotRate']:checked").val() == 'Y') {
            $('#lclSpotRate').attr("disabled", true);
            $('#lclSpotRate').removeClass("green-background");
            $('#lclSpotRate').addClass("gray-background");
        }
        $('#calculate').hide();
        $('input:radio[name=spotRate]').attr('disabled', true);
        $('#costCharge').hide();
    }


    updateDeliveryMetro();
    fillDeliveryMetro(); //set delivery Metro details enable or disable radio button
    conditionForCobAndClosedVoyage();
    if ($('#lockMessage').val() != '') {
        $('.void_Button').hide();
        showAllReadOnly();
    }
    parent.$("ul.htabs li.selected").click(function () {
        htabsFlag = true;
        setTeriminalFromBkg();
        setPortOfDestinationFromBkg();
        $.ajax({
            url: "lclBlCostAndCharge.do?methodName=deleteCharge&fileNumberId=" + $('#fileNumberId').val(),
            success: function (data) {
                $('#chargeBlDesc').html(data);
                avoidBundleCheckBox(false);
                setDefaultAgentFromBkg();
            }
        });
    });
    if (!htabsFlag) {
        setDefaultAgentFromBkg();
    }
    lockInsurance();
    //  set_void_Button();
    avoidBundleCheckBox(true);
    $(".correctedPrintOnBL").attr("disabled", false);
    bl_pier = $("#pier").val();
    loadForm = $("#lclBlForm").serialize();
    var cob = $('#blUnitCob').val() === '' ? false : $('#blUnitCob').val();
    if (cob == 'true') {
        addClassAndAttr("portOfLoading", 'text-readonly', "readOnly", true)
        addClassAndAttr("portOfDestination", 'text-readonly', "readOnly", true)
        addClassAndAttr("finalDestinationf", 'text-readonly', "readOnly", true)
    } else {
        removeClassAndAttr("portOfLoading", 'text-readonly', "readOnly", false)
        removeClassAndAttr("portOfDestination", 'text-readonly', "readOnly", false)
        removeClassAndAttr("finalDestinationf", 'text-readonly', "readOnly", false)
    }
});
function addClassAndAttr(id, className, attrName, attrValue) {
    $("#" + id).addClass(className);
    $("#" + id).attr(attrName, attrValue);
}
function removeClassAndAttr(id, className, attrName, attrValue) {
    $("#" + id).removeClass(className);
    $("#" + id).attr(attrName, attrValue);
}
function setTeriminalFromBkg() {
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.common.constant.ExportUtils",
            methodName: "getTerimal",
            param1: $("#fileNumberId").val(),
            param2: "BL",
            dataType: "json"
        },
        async: false,
        success: function (data) {
            $('#terminal').val(data[0]);
            $('#trmnum').val(data[1]);
        }
    });
}

function setDefaultAgentFromBkg() {
    $("#methodName").val("updateAgentFromBkg");
    var params = $("#lclBlForm").serialize();
    params += "&agentAcctNo=" + $("#agentacctno").val();
    $.post($("#lclBlForm").attr("action"), params, function (data) {
        if (data !== null && data !== "") {
            // this is for Pickup Agent Acct
            if (data.agentName !== undefined) {
                $('#agentacctno').val(data.agentacctno);
                $('#agentName').val(data.agentName);
                $("#agentAddress").val(data.agentAddress);
                $("#agentphone").val(data.agentphone);
                $("#agentemail").val(data.agentemail);
                $("#agentfax").val(data.agentfax);
            }
//            if (data.pod !== "") {
//                $('#portOfDestination').val(data.pod);
//            }
//            if (data.fd !== "") {
//                $('#finalDestinationf').val(data.fd);
//            }
            // this is for Freight Agent Acct mantis#14157
            if (htabsFlag || data.TranshipmentFlag === "true") {
                setFreightAgentAcct(data.freightAgentNo, data.freightAgentName);
            }
        }
    });
}

function setFreightAgentAcct(freightAgentNo, freightAgentName) {
    $("#showFreightNo").show();
    $("#showFreightName").show();
    $("#showPickFreightNo").hide();
    $("#showPickFreightName").hide();
    document.getElementById('freightAgentNo').innerHTML = freightAgentNo;
    showfreightAgentToolTip(freightAgentName);
}

function showfreightAgentToolTip(freightAgentName) {
    JToolTipForTop('#frtAgt', freightAgentName, 150);
}

function conditionForCobAndClosedVoyage() {
    if ($("#voyageClosedUser").val() !== '') {
        checkBlOwnerWithPriority();
        enableFormElements();
    } else if ($('#blUnitCob').val() === 'true' && $('#file_status').val() === 'M') {
        checkBlOwnerWithPriority();
        $(".printOnBL").attr("disabled", false);
    }
}
function limitTexts() {
    var textarea = document.getElementById("eReference").value;
    var lines = textarea.split("\n");
    for (var i = 0; i < lines.length; i++) {
        if (lines[i].length <= 40)
            continue;
        var j = 0;
        space = 40;
        while (j++ <= 40) {
            if (lines[i].charAt(j) === " ")
                space = j;
        }
        lines[i + 1] = lines[i].substring(space + 1) + (lines[i + 1] || "");
        lines[i] = lines[i].substring(0, space);
    }
    document.getElementById("eReference").value = lines.slice(0, 3).join("\n");
}
//function limitTexts(ev) {
//    //    alert("limittexts");
//    var text = ev.value;
//    text = text.replace(/(\r\n|\n|\r)/gm, "");
//    text = text.replace(/(.{40})/g, "$1\n")
//    document.getElementById("eReference").value = text;
//    var arr = text.split("\n");
//    var lines_removed, char_removed;
//   
//    if (arr.length > 3) {
//        arr = arr.slice(0, 3);
//        lines_removed = 1
//    }
//    else {
//        for (var i = 0; i < arr.length; i++) {
//            if (arr[i].length > 40) {
//                arr[i] = arr[i].slice(0, 120);
//                char_removed = 1; 
//            }
//        }
//    }
//    if (char_removed || lines_removed)
//        ev.value = arr.join('\n');
//}

function disableManifestFields() {
    $('input:radio[name=billPPD]').attr("disabled", true);
    $('input:radio[name=pcBoth]').attr("disabled", true);
    $('.hideBtn').attr("disabled", true);
    $('.hideImg').hide();
    $('#calculate').hide();
    $('#costCharge').hide();
    $('#shipperName').addClass("textlabelsBoldForTextBoxDisabledLook");
    $("#shipperName").attr('readonly', true);
    $("#newShipper").attr('disabled', true);
    $("#ediShipperCheck").attr('disabled', true);
    $('#consigneeName').addClass("textlabelsBoldForTextBoxDisabledLook");
    $("#consigneeName").attr('readonly', true);
    $("#newConsignee").attr('disabled', true);
    $("#ediConsigneeCheck").attr('disabled', true);
    $('#consigneeName').addClass("textlabelsBoldForTextBoxDisabledLook");
    $("#consigneeName").attr('readonly', true);
    $("#newConsignee").attr('disabled', true);
    $("#ediConsigneeCheck").attr('disabled', true);
    $('#forwarderName').addClass("textlabelsBoldForTextBoxDisabledLook");
    $("#forwarderName").attr('readonly', true);
    $("#ediForwarderCheck").attr('disabled', true);
    $("#ediConsigneeCheck").attr('disabled', true);
    $('input:radio[name=rateType]').attr('disabled', true);
    $('input:radio[name=spotRate]').attr('disabled', true);
    $('input:radio[name=freeBl]').attr('disabled', true);
    $('#thirdPartyname').addClass('textlabelsBoldForTextBoxDisabledLook');
    $('#thirdPartyname').attr('readonly', true);
}
function updateTerminal() {
    var trmnum = $('#trmnum').val();
    var fileId = $('#fileNumberId').val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO",
            methodName: "updateTerminal",
            param1: trmnum,
            param2: fileId,
            request: "true",
            dataType: "json"
        },
        async: false,
        success: function (data) {
            $(".concatedBlNo").text(data);
            $("#blNumber").val("");
            if ($("#terminal").val() == "QUEENS, NY/59")
            {
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "updateCommodityPersonalEffects",
                        param1: fileId,
                        request: "true",
                        dataType: "json"
                    },
                    success: function () {
                    }
                });
            }
        }
    });
}

function shipper_Accttype_Check() {
    var acctType = $("#shipper_acct_type").val();
    var subtype = $("#shipper_sub_type").val();
    var type;
    var subTypes;
    var array1 = new Array();
    var array2 = new Array();
    if (acctType != null) {
        type = acctType;
        array1 = type.split(",");
    }
    if (subtype != null) {
        subTypes = (subtype).toLowerCase();
        array2 = subTypes.split(",");
    }

    if ($('#shipperDisabledforbl').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#shipperDisabledforblAcct').val() + "</span>");
        clearShipper();
    }
    if ((!array1.contains('S') && !array1.contains('E') && !array1.contains('I') && array1.contains('V') && !array2.contains('forwarder')) || (acctType == ('C'))) {
        $.prompt("Please select the customers with account type S,E,I and V with subtype forwarder");
        clearShipper();
    } else {
        setCreditStatus('shipperCreditStatusValue', 'status', 'shipperCreditLimitValue', 'limit');
        var termsType1 = $('#termsType1').val();
        var preventExpRelRole = $('#preventExpRel').val();
        var billType = $("input:radio[name=pcBoth]:checked").val();
        var billToParty = $("input:radio[name=billPPD]:checked").val();
        if ((termsType1 === 'ER' && preventExpRelRole === 'true' && billType === 'P') &&
                (billToParty === 'S' && $('#status').val() === 'No Credit')) {
            var filenumberId = $('#fileId').val();
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "checkPersonalEffects",
                    param1: filenumberId,
                    dataType: "json"
                },
                success: function (data) {
                    if (data === 'N') {
                        congAlert("BL prepaid Shipper party doesn't have credit");
                        $('#termsType1').val('');
                        return false;
                    }
                }
            });
        }
    }
}
function clearShipper() {
    $("#shipperName").val('');
    $("#shipperCode").val('');
    $("#shipperAddress").val('');
    $("#shipperCity").val('');
    $("#shipperState").val('');
    $("#shipperZip").val('');
    $("#shipperCountry").val('');
    $("#shipperPhone").val('');
    $("#shipperFax").val('');
    $("#shipperEmail").val('');
    $("#status").val('');
    $("#limit").val('');
    $("#shipperCreditStatusValue").text('');
    $("#shipperCreditLimitValue").text('0.0');
    $("#dupShipperName").val('');
    $("#editShipAcctName").val('');
}
function thirdParty_Accttype_Check() {
    if ($('#thirdPartyDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#thirdPartyDisabledAcct').val() + "</span>");
        clearThirdParty();
    }
    setCreditStatus("thirdpartyCreditStatusValue", "thirdPartyStatus", "thirdpartyCreditLimitValue", "thirdPartyLimit");
    var termsType1 = $('#termsType1').val();
    var preventExpRelRole = $('#preventExpRel').val();
    var billType = $("input:radio[name=pcBoth]:checked").val();
    var billToParty = $("input:radio[name=billPPD]:checked").val();
    if ((termsType1 === 'ER' && preventExpRelRole === 'true' && billType === 'P') &&
            (billToParty === 'T' && $('#thirdPartyStatus').val() === 'No Credit')) {
        var filenumberId = $('#fileId').val();
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkPersonalEffects",
                param1: filenumberId,
                dataType: "json"
            },
            success: function (data) {
                if (data === 'N') {
                    congAlert("BL prepaid Third Party doesn't have credit");
                    $('#termsType1').val('');
                    return false;
                }
            }
        });
    }
}

function clearThirdParty() {
    $("#thirdPartyname").val('');
    $("#thirdpartyaccountNo").val('');
    $("#thirdpartyforbl").val('');
    $("#thirdPartyStatus").val('');
    $("#thirdPartyLimit").val('');
    $("#thirdpartyCreditStatusValue").text('');
    $("#thirdpartyCreditLimitValue").text('');
}
function forwarder_Accttype_Check() {
    var acctType = $("#forwarder_acct_type").val();
    var sub_type = $("#forwarder_sub_type").val();
    var subTypes;
    var array1 = new Array();
    var array2 = new Array();
    if (acctType != null) {
        array1 = acctType.split(",");
    }
    if (sub_type != null) {
        subTypes = (sub_type).toLowerCase();
        array2 = subTypes.split(",");
    }
    if ($('#frwddisabledforbl').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#frwddisabledforblAcct').val() + "</span>");
        clearForwarder();
    } else if (!array1.contains("V")) {
        $.prompt("Please select the customers with account type V with subtype forwarder");
        clearForwarder();
    } else if (array1.contains("V") && !array2.contains('forwarder')) {
        $.prompt("Please select the customers with account type V with subtype forwarder");
        clearForwarder();
    } else {
        fwdCheck();
        setCreditStatus('fwdCreditStatusValue', 'fwdStatus', 'fwdCreditLimitValue', 'fwdLimit');
        var termsType1 = $('#termsType1').val();
        var preventExpRelRole = $('#preventExpRel').val();
        var billType = $("input:radio[name=pcBoth]:checked").val();
        var billToParty = $("input:radio[name=billPPD]:checked").val();
        var preventExpRelRole = $('#preventExpRel').val();
        if ((termsType1 === 'ER' && preventExpRelRole === 'true' && billType === 'P') &&
                (billToParty === 'F' && $('#fwdStatus').val() === 'No Credit')) {
            var filenumberId = $('#fileId').val();
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "checkPersonalEffects",
                    param1: filenumberId,
                    dataType: "json"
                },
                success: function (data) {
                    if (data === 'N') {
                        congAlert("BL prepaid Forwarder party doesn't have credit");
                        $('#termsType1').val('');
                        return false;
                    }
                }
            });
        }
    }
}

function clearForwarder() {
    $("#forwarderName").val('');
    $("#forwarderCode").val('');
    $("#forwarderAddressa").val('');
    $("#forwarderCity").val('');
    $("#forwarderCountry").val('');
    $("#forwarderState").val('');
    $("#forwarderZip").val('');
    $("#forwarderPhone").val('');
    $("#forwarderFax").val('');
    $("#forwarderEmail").val('');
    $("#fwdCreditStatusValue").text('');
    $("#fwdCreditLimitValue").text('0.0');
    $("#editFwdAcctName").val('');
    $("#fmc").val('');
}
function consignee_Accttype_Check() {
    var acctType = jQuery("#consignee_acct_type").val();
    if ($('#consigneeDisabledforbl').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#consigneeDisabledforblAcct').val() + "</span>");
        clearConsignee();
    }
    if (acctType.indexOf('C') === -1) {
        $.prompt("Please select the customers with account type C");
        clearConsignee();
    }
    var consAcct = $('#consigneeCode').val();
    var agentAcct = $('#agentacctno').val();
    var rateType = $('#rateType').val();
    if (consAcct === agentAcct && rateType === 'C') {
        $.prompt("This is an Open Co-Load Bill of Lading. The Consignee Account you selected is the same as our Overseas Office/Agent. Please make sure you contact the Client or Shipper (NVO) to remind them that they must send a copy of final HBL and documentation overseas.");
    } else {
        setCreditStatus("consCreditStatusValue", "consStatus", "consCreditLimitValue", "consLimit");
    }
}
function openInbond(path, fileNumberId, fileNumber) {
    var href = path + "/lclInbonds.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&moduleName=Exports";
    $(".inbondButton").attr("href", href);
    $(".inbondButton").colorbox({
        iframe: true,
        href: href,
        width: "45%",
        height: "80%",
        title: "Inbonds"
    });
}
function clearConsignee() {
    jQuery("#consigneeName").val('');
    jQuery("#consigneeCode").val('');
    jQuery("#consigneeAddress").val('');
    jQuery("#consigneeCity").val('');
    jQuery("#consigneeState").val('');
    jQuery("#consigneeZip").val('');
    jQuery("#consigneeCountry").val('');
    jQuery("#consigneePhone").val('');
    jQuery("#consigneeFax").val('');
    jQuery("#consigneeEmail").val('');
    jQuery("#consStatus").val('');
    jQuery("#consLimit").val('');
    jQuery("#consCreditStatusValue").text('');
    jQuery("#consCreditLimitValue").text('0.0');
    $("#editConsAcctName").val('');
    $("#dupConsigneeName").val('');
}
function notify_Accttype_Check() {
    var acctType = jQuery("#notify_acct_type").val();
    if ($('#notifyforbl').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#notifyforblAcct').val() + "</span>");
        clearNotify();
    }

    if (acctType.indexOf('C') === -1) {
        $.prompt("Please select the customers with account type C");
        clearNotify();
    } else {
        setCreditStatus('notyCreditStatusValue', 'notiStatus', 'notyCreditLimitValue', 'notiLimit')
    }
}
function clearNotify() {
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
    $("#dupNotifyName").val('');
    $("#editNotyAcctName").val('');
    jQuery("#notyCreditStatusValue").text('');
    jQuery("#notyCreditLimitValue").text('0.0');
    $('#sameasConsignee').attr('checked', false);
}
function setCreditStatus(objCreditId, creditValueId, objCreditLimt, creditLimtValId) {
    var creditValue = $('#' + creditValueId).val();
    if (creditValue != '') {
        creditValue = creditValue.substring(0, creditValue.indexOf('-'));
        if (creditValue != "") {
            setCreditValue(objCreditId, creditValue);
        } else {
            setCreditValue(objCreditId, $('#' + creditValueId).val());
        }
    }
    $('#' + objCreditLimt).text($('#' + creditLimtValId).val());
}
function setCreditStatusOnLoad(objCreditId, creditValueId, objCreditLimt, creditLimtValId) {
    var creditValue = $('#' + creditValueId).val();
    if (creditValue != '') {
        setCreditValue(objCreditId, creditValue);
    }
    $('#' + objCreditLimt).text($('#' + creditLimtValId).val());
}
function setCreditValue(objCreditId, creditValue) {
    if (creditValue === 'Suspended/See Accounting' || creditValue === 'No Credit') {
        $('#' + objCreditId).removeClass('green');
        $('#' + objCreditId).addClass('red');
    } else {
        $('#' + objCreditId).removeClass('red');
        $('#' + objCreditId).addClass('green');
    }
    $('#' + objCreditId).text(creditValue);
}
function congAlert(txt) {
    $.prompt(txt);
}


function submitBlForm(methodName) {
    showLoading();
    $("#editRateType").val($('input:radio[name=rateType]:checked').val());
    $("#pier").val(bl_pier);
    validateNotify();
    $("#methodName").val(methodName);
    $("#lclBlForm").submit();
}
function commonDisableMethod() {
    $('#save').hide();
    $('#saveR').hide();
    if ($('#editBlOwnerRole').val() === 'true' && $('#loginUserId').val() != $('#blOwnerId').val()) {
        $('#blOwner').css('background-color', '#FFFFFF');
        $('#blOwner').attr("readonly", false);
        document.getElementById('blOwner').readonly = false;
        document.getElementById("blOwner").className = "textlabelsLclBoldForBl";
        document.getElementById("blOwner").style.border = "1px solid #dedede";
        $('#save').show();
        $('#saveR').show();
    }
    $('#arInvoice').hide();
    $('#invoiceR').hide();
    $('#copy').hide();
    $('#copyR').hide();
    $('#revert').hide();
    $('#revertR').hide();
    $('#commodity1').hide();
    //$('#aesB').hide();
    $('#BLaes').hide();
    $('#consolidate').hide();
    $('#calculate').hide();
    $('#costCharge').hide();
    $('#pickupInfo').hide();
    $('.Blhazmat').hide();
    $('#voidunitBL').hide();
    $('#voidunitBLbottom').hide();
    $('#voidunitBL1').hide();
    $('#postButton').hide();
    $('#bottom_postButton').hide();
    $('#voidunitBL1bottom').hide();
    $("#lclSpotRate").hide();
}
function checkBlOwner() {
    var form = document.getElementById('lclBlForm');
    disabledFormElement(form);
    commonDisableMethod();
    hideFormImages();
}

function hideFormImages() {
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
function submitAjaxForm(methodName, formName, selector, origin, destination, pol, pod) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}
function addAesDetails(path, fileNumber, fileNumberId) {
    var href = path + "/lclAesDetails.do?methodName=display&fileNumber=" + fileNumber + "&fileNumberId=" + fileNumberId;
    $(".aes").attr("href", href);
    $(".aes").colorbox({
        iframe: true,
        width: "90%",
        height: "90%",
        title: "AES details"
    });
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
function editAes(path, id, trnref, fileNumber) {
    var href = path + "/lclAesDetails.do?methodName=editAes&id=" + id + "&trnref=" + trnref + "&fileNumber=" + fileNumber;
    $(".aes").attr("href", href);
    $(".aes").colorbox({
        iframe: true,
        width: "90%",
        height: "90%",
        title: "AES details"
    });
}
//commodity descritpin js code
function submitAjaxFormCrossUrl(id) {
    $('#commId').val(id);
}

function deleteBlCommodity(id, commId) {
    var filenumberId = $('#fileNumberId').val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkBlRates",
            param1: filenumberId,
            dataType: "json"
        },
        success: function (data) {
            if (data == true) {
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "deleteBlRates",
                        param1: filenumberId,
                        dataType: "json"
                    },
                    success: function (data) {
                        if (data == true) {
                            var txt = 'Rates will be removed. Recalculate Rates By Clicking Calculate Button.Are you sure you want to delete?';
                            $.prompt(txt, {
                                buttons: {
                                    Yes: 1,
                                    No: 2
                                },
                                submit: function (v) {
                                    if (v == 1) {
                                        showProgressBar();
                                        $.ajax({
                                            url: "lclBlCommodity.do?methodName=deleteLclCommodity&id=" + id + "&filenumberId=" + filenumberId + "&commId=" + commId,
                                            success: function (data) {
                                                $('#commodityDesc').html(data);
                                                hideProgressBar();
                                                $.prompt.close();
                                            }
                                        });
                                        for (var i = document.getElementById("chargesTable").rows.length; i > 1; i--)
                                        {
                                            document.getElementById("chargesTable").deleteRow(i - 1);
                                        }
                                        document.getElementById("totalchargestd").innerHTML = "0";
                                        hideProgressBar();
                                        $.prompt.close();
                                    } else if (v == 2) {
                                        $.prompt.close();
                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                var txt = 'Are you sure You want to delete?';
                $.prompt(txt, {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v == 1) {
                            showProgressBar();
                            $.ajax({
                                url: "lclBlCommodity.do?methodName=deleteLclCommodity&id=" + id + "&filenumberId=" + filenumberId + "&commId=" + commId,
                                success: function (data) {
                                    $('#commodityDesc').html(data);
                                    hideProgressBar();
                                }
                            });
                            hideProgressBar();
                            $.prompt.close();
                        } else if (v == 2) {
                            $.prompt.close();
                        }

                    }
                });
            }
        }
    });
}

function deleteAesDesc(txt, id, shpdr) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                $.ajax({
                    url: "lclAesDetails.do?methodName=closeAes&id=" + id + "&shpdr=" + shpdr,
                    success: function (data) {
                        $('#aesDesc').html(data);
                        hideProgressBar();
                    }
                });
                $.prompt.close();
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}

function closeAes(trnref) {
    $('#trn').val(trnref);
}

function editBlHazmat(path, blPieceId) {
    var fileId = $('#fileNumberId').val();
    var fileNo = $('#fileNumber').val();
    var href = path + "/lclBlHazmat.do?methodName=display&fileId=" + fileId + "&fileNo=" + fileNo + "&blPieceId=" + blPieceId;
    $(".Blhazmat").attr("href", href);
    $(".Blhazmat").colorbox({
        iframe: true,
        width: "84%",
        height: "98%",
        title: "Haz.Mat"
    });
}
function openDetails(path, bookingPieceId) {
    var href = path + "/commodityDetails.do?methodName=display&bookingPieceId=" + bookingPieceId;
    $(".details").attr("href", href);
    $(".details").colorbox({
        iframe: true,
        width: "62%",
        height: "85%",
        title: "Commodity Details"
    });
}
function editBlCommodity(path, blPieceId, editDimFlag, fileNumberId, fileNumber) {
    var pooUnlocCode = $('#pooCode').val();
    var fdUnlocCode = $('#unlocationCode').val();
    var polUnlocCode = $('#polUnlocationcode').val();
    var podUnlocCode = $('#podUnlocationcode').val();
    var rateType = $("input:radio[name='rateType']:checked").val();
    var href = path + "/lclBlCommodity.do?methodName=editLclCommodity&blPieceId=" + blPieceId +
            "&editDimFlag=" + editDimFlag + "&fileNumber=" + fileNumber + "&pooUnlocCode=" + pooUnlocCode + "&fdUnlocCode=" + fdUnlocCode +
            "&rateType=" + rateType + "&fileNumberId=" + fileNumberId + "&polUnlocCode=" + polUnlocCode + "&podUnlocCode=" + podUnlocCode;
    $(".addCommodity").attr("href", href);
    $(".addCommodity").colorbox({
        iframe: true,
        width: "83%",
        height: "90%",
        title: "Edit Commodity",
        onClosed: function () {
            conditionForCobAndClosedVoyage();
        }
    });
}

function openLclArInvoice(path, fileNumberId, fileNumber, listFlag, moduleName) {
    var href = path + "/lclArInvoice.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&listFlag=" + listFlag + "&moduleName=" + moduleName;
    $(".invoice").attr("href", href);
    $(".invoice").colorbox({
        iframe: true,
        width: "85%",
        height: "85 %",
        title: "AR Invoice"
    });
}
function showConsolidate(path, podId, fileNumber, fdId, fileNumberA) {
    var href = path + "/lclConsolidate.do?methodName=display&podId=" + $(podId).val() + "&fileNumber=" + fileNumber + "&fdId=" + $(fdId).val() + "&fileNumberA=" + fileNumberA;
    $(".consolidate").attr("href", href);
    $(".consolidate").colorbox({
        iframe: true,
        width: "90%",
        height: "90%",
        title: "Consolidate"
    });
}

function addBlCharge(path, fileNumber, fileNumberId) {
    var billToParty = $("input:radio[name=pcBoth]:checked").val();
    var blBillingType = $("input:radio[name=billPPD]:checked").val();
    var destination = $('#destination').val();
    var forwarderCode = $('#forwarderCode').val();
    var count = new Array();
    $(".chargeAmount").each(function () {
        count.push($(this).text().trim());
    });
    if (count.length >= 12) {
        $.prompt("More than 12 charges is not allowed");
    } else {
        var href = path + "/lclBlCostAndCharge.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber
                + "&destination=" + destination + "&manualEntry=true&billToParty=" + billToParty + "&blBillingType=" + blBillingType + "&fwdAcctNo=" + forwarderCode;
        $(".costAndCharge").attr("href", href);
        $(".costAndCharge").colorbox({
            iframe: true,
            width: "70%",
            height: "70%",
            title: "Add Charge",
            onClosed: function () {
                $("#chargeBlDesc").find("[title != '']").not("link").tooltip();
                avoidBundleCheckBox(false);
                setDefaultAgentFromBkg();
            }
        });
    }
}

function editBlCharge(path, id, fileNumberId, fileNumber, manualEntry) {
    var billToParty = $("input:radio[name=pcBoth]:checked").val();
    var blBillingType = $("input:radio[name=billPPD]:checked").val();
    var forwarderCode = $('#forwarderCode').val();
    var href = path + "/lclBlCostAndCharge.do?methodName=editCharge&fileNumberId=" + fileNumberId + "&fileNumber="
            + fileNumber + "&id=" + id + "&manualEntry=" + manualEntry + "&billToParty=" + billToParty + "&blBillingType=" + blBillingType + "&fwdAcctNo=" + forwarderCode;
    $(".costAndCharge").attr("href", href);
    $(".costAndCharge").colorbox({
        iframe: true,
        width: "70%",
        height: "60%",
        title: "Edit Charge",
        onClosed: function () {
            $("#chargeBlDesc").find("[title != '']").not("link").tooltip();
            avoidBundleCheckBox(false);
            setDefaultAgentFromBkg();
        }
    });
}

function deleteBlCharge(txt, id, fileNumberId, fileNumber, chargeCode) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                $.ajax({
                    url: "lclBlCostAndCharge.do?methodName=deleteCharge&id=" + id + "&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber,
                    success: function (data) {
                        $('#chargeBlDesc').html(data);
                        avoidBundleCheckBox(false);
                        hideProgressBar();
                        setDefaultAgentFromBkg();
                        if (chargeCode == 'INSURE') {
                            $('#printInsuranceY').attr('disabled', true);
                            $('#printInsuranceN').attr('disabled', true);
                        }
                    }
                });
                $.prompt.close();
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function calculateBlCharge() {
    var spotRate = $('input:radio[name=spotRate]:checked').val();
    if (spotRate === 'Y') {
        $("#finalDestinationf").val($("#previousDestName").val());
        $.prompt('Cannot change rates as Spot Rate is set to Yes');
        return false;
    }
    $.prompt('Are you Sure you want to recalculate the Standard charges?', {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v == 1) {
                $('#blNumber').val("");
                var pol = $('#polUnlocationcode').val();
                var pod = $('#podUnlocationcode').val();
                var destination = $('#unlocationCode').val();
                var origin = $('#pooCode').val();
                submitAjaxForm('calculateCharges', '#lclBlForm', '#chargeBlDesc', origin, destination, pol, pod);
                var deliveryMetro = $('input:radio[name=deliveryMetro]:checked').val();
                $('#deliveryMetroField').val(deliveryMetro);
            }
        }
    });
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

function deleteConsolidate(txt, id, fileIdA) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                $.ajax({
                    url: "lclConsolidate.do?methodName=delete&id=" + id + "&fileIdA=" + fileIdA,
                    success: function (data) {
                        $('#consolidateDesc').html(data);
                        hideProgressBar();
                    }
                });
                $.prompt.close();
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function checkBilltoCode() {
    var img = document.getElementById('clientSearchEditBlT');
    var billToParty = $('input:radio[name=pcBoth]:checked').val();
    if (billToParty === 'P' || billToParty === 'B') {
        $('#thirdPartyname').removeClass('textlabelsBoldForTextBoxDisabledLook');
        $('#thirdPartyname').removeAttr('readonly');
        img.style.visibility = 'visible';
    } else {
        $('#thirdPartyname').removeClass('textlabelsBoldForTextBoxDisabledLook');
        $('#thirdPartyname').removeAttr('readonly');
        $('#thirdPartyname').addClass('textlabelsBoldForTextBoxDisabledLook');
        $('#thirdPartyname').attr('readonly', true);
        img.style.visibility = 'hidden';
    }

}

function checkBillingType(val) {
    var img = document.getElementById('clientSearchEditBlT');
    if ($('#radioP').attr('checked')) {
        $('#billA').attr("disabled", true);
        $('#billS').attr("disabled", false);
        $('#billT').attr("disabled", false);
        $('#billF').attr("disabled", false);
    } else if ($('#radioC').attr('checked')) {
        $('#billA').attr("disabled", true);
        $('#billS').attr("disabled", true);
        $('#billT').attr("disabled", true);
        $('#billF').attr("disabled", true);
        $('#billA').attr("checked", true);
        img.style.visibility = 'hidden';
    } else if ($('#radioB').attr('checked')) {
        setDisabledBillTo();
    }
}

function setDisabledBillTo() {
    $('#billA').attr("disabled", true);
    $('#billS').attr("disabled", true);
    $('#billT').attr("disabled", true);
    $('#billF').attr("disabled", true);
    $('#billA').attr("checked", false);
    $('#billS').attr("checked", false);
    $('#billT').attr("checked", false);
    $('#billF').attr("checked", false);
}
function readyToSendLclEdi() {
    if (jQuery("#ediCheckBox").attr('checked')) {
        if (document.getElementById("sendLclEdi")) {
            document.getElementById("sendLclEdi").style.visibility = "visible";
        }
    } else {

        if (document.getElementById("sendLclEdi")) {
            document.getElementById("sendLclEdi").style.visibility = "hidden";
        }
    }
}

function goBackSearch(path, fileNumberId, moduleId) {
    if (loadForm != $("#lclBlForm").serialize()) {
        var msg = "Do you want to save the bill of lading changes?";
        $.prompt(msg, {
            buttons: {
                Yes: 1,
                No: 2,
                Cancel: 3
            },
            submit: function (v) {
                if (v == 1) { //save and go back
                    $("#editRateType").val($('input:radio[name=rateType]:checked').val());
                    $("#methodName").val("saveBl");
                    var params = $("#lclBlForm").serialize();
                    $.post($("#lclBlForm").attr("action"), params, function () {
                        checkFileNumber(path, fileNumberId, moduleId);
                    });
                } else if (v == 2) { //go back
                    checkFileNumber(path, fileNumberId, moduleId);
                } else { //do nothing
                    return;
                }
            }
        });
    } else {
        checkFileNumber(path, fileNumberId, moduleId);
    }
}

//from lclbl.jsp
function checkFileNumber(path, fileNumberId, moduleId) {
    showProgressBar();
    var toScreenName = parent.$("#toScreenName").val();
    var filter = parent.$('#filterByChanges').val();
    var fromScreen = parent.$("#fromScreen").val();
    if (fromScreen === 'ar_inquiry' && toScreenName === 'LCLBooking') {
        window.parent.showArInquiryForLcl($('#fileNumber').val(), "ar_inquiry");
    } else if (toScreenName === 'EXP VOYAGE') {
        backToUnitScreenFromBl(path, fileNumberId);
    } else if (toScreenName === 'EXPORT_TO_IMPORT') {
        if (filter === 'LCL_IMP_DR') {
            goToImpSearchFromExportBooking(path, fileNumberId, moduleId);
        } else {
            goToImpVoyageFromExportBooking(path, fileNumberId);
        }
    } else {
        window.parent.changeLclChilds(path, fileNumberId, moduleId, "Exports");
    }
    hideProgressBar();
}
function revertToBooking(path, fileNumberId, moduleId) {
    if ($('#file_status').val() === 'M') {
        var alerts = $('#blUnitCob').val() === 'true' ? "/COB" : "";
        $.prompt("BL is Manifested" + alerts);
        return false;
    }
    var txt = "Are you sure you want to reverse BL back to a booking ?";
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                $("#methodName").val("revertToBooking");
                var params = $("#lclBlForm").serialize();
                $.post($("#lclBlForm").attr("action"), params, function () {
                    if (parent.$("#toScreenName").val() === 'EXP VOYAGE' || parent.$("#toScreenName").val() === "EXPORT_TO_IMPORT") {
                        revertToBookingFromUnitScreen(path, fileNumberId);
                    } else {
                        window.parent.changeLclChilds(path, fileNumberId, moduleId, "Exports");
                    }
                    hideProgressBar();
                });
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}

function revertToBookingFromUnitScreen(path, fileId) {
    var headerId = parent.$("#pickVoyId").val();
    var detailId = parent.$("#detailId").val();
    var unitSsId = parent.$("#unitSsId").val();
    var toScreenName = parent.$("#toScreenName").val();
    var fromScreen = parent.$("#fromScreen").val();
    var filter = parent.$("#filterByChanges").val();
    var inTransitDr = parent.$("#inTransitDr").val();
    window.parent.goToBookingFromVoyage(path, fileId, filter, headerId, detailId, unitSsId, fromScreen, toScreenName, inTransitDr);
}

function backToUnitScreenFromBl(path, fileId) {
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

function goToImpVoyageFromExportBooking(path, fileId) {
    var headerId = parent.$("#pickVoyId").val();
    var detailId = parent.$("#detailId").val();
    var unitSsId = parent.$("#unitSsId").val();
    var toScreenName = parent.$("#fromScreen").val();
    var fromScreen = parent.$("#toScreenName").val();
    window.parent.navigateExportImportScreen(fileId, "", headerId, detailId,
            unitSsId, fromScreen, toScreenName, "OPEN IMP VOYAGE");
}

function goToImpSearchFromExportBooking(path, fileNumberId, moduleId) {
    window.parent.changeLclChilds(path, fileNumberId, moduleId, "Imports", "", 0);
}

function correctionNotice(path, fileId, fileNo, blNo, selectedMenu) {
    var href = path + "/lclCorrection.do?methodName=viewLclBlCorrections&fileId=" + fileId + "&fileNo=" + fileNo + "&selectedMenu=" + selectedMenu;
    href = href + "&blNo=" + blNo.substring(5, blNo.length) + "&screenName=Corrections";
    $.colorbox({
        iframe: true,
        href: href,
        width: "80%",
        height: "96%",
        title: "LclBlCorrections"
    });
}
function blCorrection(path, fileId, fileNo, blNo, moduleName) {
    var issuTerm = $("#terminal").val();
    var href = path + "/blCorrection.do?methodName=searchResult&fileId=" + fileId + "&fileNo=" + fileNo + "&selectedMenu=" + moduleName;
    href = href + "&blNo=" + blNo + "&screenName=Corrections" + "&issuingTerminal=" + issuTerm;
    $.colorbox({
        iframe: true,
        href: href,
        width: "80%",
        height: "96%",
        title: "LclBlCorrections"
    });
}

function newShipperName() {
    if ($('#newShipper').is(":checked")) {
        $('#ediShipperCheck').attr('checked', false);
        $("#dojoShipper").hide();
        $("#editAcctDiv").hide();
        $("#manualShipper").show();
    } else {
        $("#dojoShipper").show();
        $("#manualShipper").hide();
        $("#editAcctDiv").hide();
        $("#dupShipperName").val("");
    }
    clearShipper();
}

$(document).ready(function () {
//    $('#editShipAcctName').keyup(function () {
//        if ($('#editShipAcctName').val() == "") {
//            $('#ediShipperCheck').attr("checked", false);
//            $("#dojoShipper").show();
//            $("#manualShipper").hide();
//            $("#editAcctDiv").hide();
//            clearShipper();
//        }
//    });
//    $('#editConsAcctName').keyup(function () {
//        if ($('#editConsAcctName').val() == "" && !$('#ediConsigneeCheck').is(":checked")) {
//            $('#ediConsigneeCheck').attr("checked", false);
//            $("#dojoConsignee").show();
//            $("#manualConsignee").hide();
//            $("#editConsAcctDiv").hide();
//            clearConsignee();
//        }
//    });
//    $('#editNotyAcctName').keyup(function () {
//        if ($('#editNotyAcctName').val() == "") {
//            $('#ediNotifyCheck').attr("checked", false);
//            $("#dojoNotify").show();
//            $("#manualNotify").hide();
//            $("#editNotyAcctDiv").hide();
//            clearNotify();
//        }
//    });
//    $('#editFwdAcctName').keyup(function () {
//        if ($('#editFwdAcctName').val() == "") {
//            $('#ediForwarderCheck').attr("checked", false);
//            clearForwarder();
//            $("#fwdDojo").show();
//            $("#editFwdAcctDiv").hide();
//        }
//    });
    $('#forwarderName').change(function () {
        if ($('#forwarderName').val() == "") {
            clearForwarder();
        }
    });
    $('#shipperName').change(function () {
        if ($('#shipperName').val() == "") {
            clearShipper();
        }
    });
    $('#consigneeName').change(function () {
        if ($('#consigneeName').val() == "") {
            clearConsignee();
//            setExportsReference();
        }
    });
    $('#notifyName').change(function () {
        if ($('#notifyName').val() == "") {
            clearNotify();
        }
    });
    $('#thirdPartyname').change(function () {
        if ($('#notifyName').val() == "") {
            clearThirdParty();
        }
    });
});
function showShipper() {
    var acctno = $('#shipperCode').val();
    var dupShipperName = $('#dupShipperName').val();
    if (acctno != '') {
        if ($('#origShipAcctName').val() != $('#editShipAcctName').val()) {
            $('#editAcctDiv').show();
            $('#dojoShipper').hide();
            $('#manualShipper').hide();
            $('#ediShipperCheck').attr('checked', true);
        } else {
            $('#ediShipperCheck').attr('checked', false);
        }
    } else {
        if (dupShipperName != "") {
            $('#newShipper').attr("checked", true);
            $('#ediShipperCheck').attr("checked", false);
            $("#dojoShipper").hide();
            $("#manualShipper").show();
            $("#editShipAcct").hide();
        }
    }
}

function showConsignee() {
    var acctno = $('#consigneeCode').val();
    var dupConsigneeName = $('#dupConsigneeName').val();
    if (acctno != '') {
        if ($('#origConsAcctName').val() != $('#editConsAcctName').val()) {
            $("#dojoConsignee").hide();
            $("#manualConsignee").hide();
            $("#editConsAcctDiv").show();
            $('#ediConsigneeCheck').attr('checked', true);
        } else {
            $('#ediConsigneeCheck').attr('checked', false);
        }
    } else {
        if (dupConsigneeName != "") {
            $('#newConsignee').attr("checked", true);
            $('#ediConsigneeCheck').attr('checked', false);
            $("#dojoConsignee").hide();
            $("#manualConsignee").show();
            $("#editConsAcctDiv").hide();
        }
    }
}
function showNotifyName() {
    var acctno = $('#notifyCode').val();
    var dupNotifyName = $('#dupNotifyName').val();
    if (acctno != '') {
        if ($('#origNotyAcctName').val() != $('#editNotyAcctName').val()) {
            $("#dojoNotify").hide();
            $("#manualNotify").hide();
            $("#editNotyAcctDiv").show();
            $('#ediNotifyCheck').attr('checked', true);
        } else {
            $('#ediNotifyCheck').attr('checked', false);
        }
    } else {
        if (dupNotifyName != "") {
            $('#newNotify').attr("checked", true);
            $('#ediNotifyCheck').attr('checked', false);
            $("#dojoNotify").hide();
            $("#manualNotify").show();
            $("#editNotyAcctDiv").hide();
        }
    }
}

function showFwdName() {
    if ($('#forwarderCode').val() != "") {
        if ($('#origFwdAcctName').val() != $('#editFwdAcctName').val()) {
            $("#fwdDojo").hide();
            $("#editFwdAcctDiv").show();
            $('#ediForwarderCheck').attr('checked', true);
        } else {
            $('#ediForwarderCheck').attr('checked', false);
        }
    } else {
        $("#fwdDojo").show();
        $("#editFwdAcctDiv").hide();
        $('#ediForwarderCheck').attr('checked', false);
    }
}
function newConsigneeName() {
    if ($('#newConsignee').is(":checked")) {
        $('#ediConsigneeCheck').attr('checked', false);
        $("#dojoConsignee").hide();
        $("#editConsAcctDiv").hide();
        $("#manualConsignee").show();
    } else {
        $("#dojoConsignee").show();
        $("#manualConsignee").hide();
        $("#editConsAcctDiv").hide();
    }
    clearConsignee();
}
function newNotifyName() {
    if ($('#newNotify').is(":checked")) {
        $('#ediNotifyCheck').attr('checked', false);
        $("#dojoNotify").hide();
        $("#editNotyAcctDiv").hide();
        $("#manualNotify").show();
    } else {
        $("#dojoNotify").show();
        $("#manualNotify").hide();
        $("#editNotyAcctDiv").hide();
    }
    clearNotify();
}

function editAccountForEdi(obj) {
    if (obj.checked) {
        if (obj.id == "ediShipperCheck") {
            $('#newShipper').attr('checked', false);
            if (jQuery("#shipperName").val() != "") {
                $('#editShipAcctName').val($("#shipperName").val());
                $.prompt("Do you want to edit Shipper for EDI");
                $('#editAcctDiv').show();
                $('#manualShipper').hide();
                $('#dojoShipper').hide();
            } else {
                $('#editAcctDiv').show();
                $('#manualShipper').hide();
                $('#dojoShipper').hide();
            }
        } else if (obj.id == "ediConsigneeCheck") {
            $('#newConsignee').attr('checked', false);
            if (jQuery("#consigneeName").val() != "") {
                $.prompt("Do you want to edit Consignee for EDI");
                $('#editConsAcctName').val($('#consigneeName').val());
                $('#editConsAcctDiv').show();
                $('#manualConsignee').hide();
                $('#dojoConsignee').hide();
            } else {
                $('#editConsAcctDiv').show();
                $('#manualConsignee').hide();
                $('#dojoConsignee').hide();
            }
        } else if (obj.id == "ediNotifyCheck") {
            $('#newNotify').attr('checked', false);
            if (jQuery("#notifyName").val() != "") {
                $.prompt("Do you want to edit NotifyParty for EDI");
                $('#editNotyAcctName').val($('#notifyName').val());
                $('#editNotyAcctDiv').show();
                $('#manualNotify').hide();
                $('#dojoNotify').hide();
            } else {
                $('#editNotyAcctDiv').show();
                $('#manualNotify').hide();
                $('#dojoNotify').hide();
            }
        } else if (obj.id == "ediForwarderCheck") {
            if (jQuery("#forwarderName").val() != "") {
                $.prompt("Do you want to edit Forwarder for EDI");
                $('#editFwdAcctName').val($('#forwarderName').val());
                $('#editFwdAcctDiv').show();
                $('#fwdDojo').hide();
            } else {
                $('#editFwdAcctDiv').show();
                $('#fwdDojo').hide();
            }
        }
    } else {
        if (obj.id == "ediShipperCheck") {
            $('#editAcctDiv').hide();
            $('#manualShipper').hide();
            $('#dojoShipper').show();
            $("#dupShipperName").val('');
            $("#editShipAcctName").val('');
            if ($('#origShipAcctName').val() != "") {
                $('#shipperName').val($('#origShipAcctName').val());
            }
        } else if (obj.id == "ediConsigneeCheck") {
//            clearConsignee();
            $('#editConsAcctDiv').hide();
            $('#manualConsignee').hide();
            $('#dojoConsignee').show();
            $("#editConsAcctName").val('');
            $("#dupConsigneeName").val('');
            if ($('#origConsAcctName').val() != "") {
                $('#consigneeName').val($('#origConsAcctName').val());
            }
        } else if (obj.id == "ediNotifyCheck") {
            $('#editNotyAcctDiv').hide();
            $('#manualNotify').hide();
            $('#dojoNotify').show();
            $("#dupNotifyName").val('');
            $("#editNotyAcctName").val('');
            if ($('#origNotyAcctName').val() != "") {
                $('#notifyName').val($('#origNotyAcctName').val());
            }
//            clearNotify();
        } else if (obj.id == "ediForwarderCheck") {
            $('#editFwdAcctDiv').hide();
            $('#fwdDojo').show();
            $("#editFwdAcctName").val('');
//            clearForwarder();
            if ($('#origFwdAcctName').val() != "") {
                $('#forwarderName').val($('#origFwdAcctName').val());
            }
        }
    }
}
function isSameasConsignee() {
    if ($('#consigneeName').val() !== "" || $('#consigneeCode').val() !== "") {
        if ($('#notifyName').val() === $('#consigneeName').val() && $('#notifyCode').val() === $('#consigneeCode').val() && $('#notifyAddress').val() === $('#consigneeAddress').val()) {
            $('#sameasConsignee').attr('checked', true);
        } else {
            $('#sameasConsignee').attr('checked', false);
        }
    }
}
function insertNotifyParty() {
    if ($('#sameasConsignee').is(":checked")) {
        $('#notifyName').val($('#consigneeName').val());
        $('#notifyCode').val($('#consigneeCode').val());
        $('#notifyAddress').val($('#consigneeAddress').val());
        setCreditStatus('notyCreditStatusValue', 'consStatus', 'notyCreditLimitValue', 'consLimit');
    } else {
        $('#notifyName').val('');
        $('#notifyCode').val('');
        $('#notifyAddress').val('');
        $('#notyCreditStatusValue').text('');
        $('#notyCreditLimitValue').text('');
    }

}

function updateBlCharges(chargeId, flag) {//chargeBlDesc.jsp
    var checkValues = "";
    if (flag === 'B') {
        checkValues = $('#bundleToOf' + chargeId).is(":checked");
    } else if (flag === 'P') {
        checkValues = $('#printOnBL' + chargeId).is(":checked");
    } else if (flag === 'C') {
        checkValues = $('#correctedPrintOnBL' + chargeId).is(":checked");
    }
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO",
            methodName: "updateCharges",
            param1: chargeId,
            param2: checkValues,
            param3: flag,
            dataType: "json"
        },
        async: false
    });
}
function openAes(path, fileNumberId, fileNumber) {
    var href = path + "/lclBl.do?methodName=displayAES&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber;
    $.colorbox({
        iframe: true,
        href: href,
        width: "65%",
        height: "65%",
        title: "AES Details"
    });
}
function is_null_empty(val) {
    var data = val !== null && val !== '' ? true : false;
    return data;
}
function concat_Address(address, city, state, country, zip, phone) {
    var con_address = is_null_empty(address) ? address + "\n" : '';
    con_address += is_null_empty(city) ? city : '';
    con_address += is_null_empty(state) && is_null_empty(city) ? "," + state : is_null_empty(state) ? "" + state : '';
    con_address += is_null_empty(country) ? "," + country + "\n" : "\n";
    con_address += is_null_empty(zip) ? zip : "";
    con_address += is_null_empty(phone) ? " " + "PHONE" + phone : '';
    return con_address;
}
function shipCheck() {
    var address = $("#shipperAddress").val().replace(/\,/g, " ") + "\n";
    address += $("#shipperCity").val() + ",";
    address += $("#shipperState").val() + "  ";
    address += $("#shipperZip").val() + "";
    address += is_null_empty($("#shipperPhone").val()) ? "\nPHONE:" + $("#shipperPhone").val() : '';
    $("#shipperAddress").val(address);
}
function consCheck() {
    var address = $("#consigneeAddress").val().replace(/\,/g, " ") + "\n";
    address += $("#consigneeCity").val() + ",";
    address += $("#consigneeState").val() + ",";
    address += $("#consigneeCountry").val() + "  ";
    address += $("#consigneeZip").val() + "";
    address += is_null_empty($("#consigneePhone").val()) ? "\nPHONE:" + $("#consigneePhone").val() : '';
    $("#consigneeAddress").val(address);
}

function notyCheck() {

    var address = $("#notifyAddress").val().replace(/\,/g, " ") + "\n";
    address += $("#notifyCity").val() + ",";
    address += $("#notifyState").val() + ",";
    address += $("#notifyCountry").val() + "  ";
    address += $("#notifyZip").val() + "";
    address += is_null_empty($("#notifyPhone").val()) ? "\nPHONE:" + $("#notifyPhone").val() : '';
    $("#notifyAddress").val(address);
}
function fwdCheck() {
    var fwdVal = $('#fwdValidate').val();
    if (($('#billF').is(':checked')) || (fwdVal !== undefined && fwdVal === 'F')) {
        if (filterNoFreightForwarderAc()) {
            $("#forwarderAddressa").val('');
            $("#forwarderCity").val('');
            $("#forwarderState").val('');
            $("#forwarderCountry").val('');
            $("#forwarderZip").val('');
            $("#forwarderName").val('');
            $("#forwarderCode").val('');
            $.prompt("Please Select valid Forwarder Account");
        }
    }
    var address = $("#forwarderAddressa").val().replace(/\,/g, " ") + "\n";
    address += $("#forwarderCity").val() + ",";
    address += $("#forwarderState").val() + "  ";
    address += $("#forwarderZip").val() + "";
    address += is_null_empty($("#forwarderPhone").val()) ? "\nPHONE:" + $("#forwarderPhone").val() : '';
    $("#forwarderAddressa").val(address);
}
//function getTermsType(obj) {//remove dis method in future
//    var unLocId = $("#finalDestinationId").val();
//    if (obj) {
//        changeTermType(unLocId);
//    } else {
//        unLocId = $("#portOfDestinationId").val();
//        changeTermType(unLocId);
//    }
//}
//function changeTermType(unLocId) {
//    $.ajaxx({
//        dataType: "json",
//        data: {
//            className: "com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO",
//            methodName: "getTermsType",
//            param1: unLocId,
//            dataType: "json"
//        },
//        success: function (data) {
//            $("#termsType1").val(data);
//        }
//    });
//}
function setDisableStyle(id1, id2) {
    $('#' + id2).addClass('textlabelsBoldForTextBoxDisabledLook');
    $('#' + id2).attr('readonly', true);
    $('#' + id1).addClass('textlabelsBoldForTextBoxDisabledLook');
    $('#' + id1).attr('readonly', true);
}
function setEnableStyle(id) {
    $('#' + id).removeClass('textlabelsBoldForTextBoxDisabledLook');
    $('#' + id).removeAttr('readonly');
}


function updateBillCode() {
    $.prompt("Please Select valid Forwarder Account - Bill To party will change to Shipper", {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                $('#billS').attr('checked', true);
                $('#radioP').attr('checked', true);
                $("#billS").attr("disabled", false);
                $("#billT").attr("disabled", false);
                $("#billF").attr("disabled", false);
                $("#billA").attr("disabled", true);
                updateBillToCode("S");
                checkBilltoCode();
                $('#existBillTerms').val("P");
            } else {
                $("input[name=pcBoth]").val([$('#existBillTerms').val()]);
                $.prompt.close();
            }
        }
    });
}


function validateBillToParty(ele, updateFlag) {
    var flag = true;
    switch (ele) {
        case 'F':
            if (filterNoFreightForwarderAc()) {
                if ($("#shipperCode").val().trim().trim() !== '' && updateFlag) {
                    updateBillCode();
                } else {
                    if ($("#shipperCode").val().trim().trim() === ''
                            && $("#forwarderName").val().trim().trim() === '' && updateFlag) {
                        $.prompt("Cannot change to Prepaid - please enter valid Shipper or Forwarder accounts");
                    } else {
                        $.prompt("Please Select valid Forwarder Account");
                        $('#billF').attr('checked', false);
                        if (!updateFlag) {
                            $("input[name=billPPD]").val([$("#hiddenBillToCode").val()]);
                        }
                    }
                }
                flag = false;
            } else if ($("#forwarderName").val().trim().trim() === '') {
                $.prompt("Please Select Forwarder Name And Number");
                $('#billF').attr('checked', false);
                $("input[name=billPPD]").val([$("#hiddenBillToCode").val()]);
                flag = false;
            }
            break;
        case 'S':
            if ($("#shipperCode").val().trim().trim() === '') {
                $.prompt("Please Select Shipper Name And Number");
                $('#billS').attr('checked', false);
                $("input[name=billPPD]").val([$("#hiddenBillToCode").val()]);
                flag = false;
            }
            break;
        case 'T':
            if ($("#thirdpartyaccountNo").val().trim().trim() === '') {
                $.prompt("Please Select Third Party Name And Number");
                $('#billT').attr('checked', false);
                $("input[name=billPPD]").val([$("#hiddenBillToCode").val()]);
                flag = false;
            }
            break;
        case 'A':
            if ($("#agentName").val().trim().trim() === '') {
                $.prompt("Please Select Agent Name And Number");
                $('#billA').attr('checked', false);
                $("input[name=billPPD]").val([$("#hiddenBillToCode").val()]);
                flag = false;
            }
            break;
    }
    return flag;
}
function updateBillToCodeFromBillType(billToParty, billingType) {
    var flag = validateBillToParty(billToParty, true);
    if (!flag) {
        $("input[name=pcBoth]").val([$('#existBillTerms').val()]);
        return false;
    } else {
        $.prompt("Please note that all Bill to Code will be changed for all Charges", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    if (billingType === 'P') {
                        $("#billS").attr("disabled", false);
                        $("#billT").attr("disabled", false);
                        $("#billF").attr("disabled", false);
                        $("#billA").attr("disabled", true);
                    } else if (billingType === 'C') {
                        $("#billS").attr("disabled", true);
                        $("#billT").attr("disabled", true);
                        $("#billF").attr("disabled", true);
                        $("#billA").attr("disabled", true);
                    } else if (billingType === 'B') {
                        $("#billS").attr("disabled", true);
                        $("#billT").attr("disabled", true);
                        $("#billF").attr("disabled", true);
                        $("#billA").attr("disabled", true);
                        $(".blBillingType").text("BOTH");
                    }
                    checkBilltoCode();
                    var checkprintPpdBlBoth = $('#checkprintPpdBlBoth').val();
                    if (checkprintPpdBlBoth === 'Y') {
                        $('#printPpdBlBothY').attr('checked', false);
                        $('#printPpdBlBothN').attr('checked', true);
                    } else {
                        $('#printPpdBlBothY').attr('checked', false);
                        $('#printPpdBlBothN').attr('checked', true);
                    }
                    $("input[name=billPPD]").val([billToParty]);
                    updateBillToCode(billToParty);
                    $('#existBillTerms').val(billingType);
                    var preventExpRelRole = $('#preventExpRel').val();
                    if (billingType === 'P' && $('#termsType1').val() === 'ER' && preventExpRelRole === 'true') {
                        var filenumberId = $('#fileId').val();
                        $.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.lcl.dwr.LclDwr",
                                methodName: "checkAndSavePersonalEffects",
                                param1: filenumberId,
                                request: "true",
                                dataType: "json"
                            },
                            success: function (data) {
                                if (data === 'N') {
                                    $('#termsType1').val('');
                                }
                            }
                        });
                    }
                } else {
                    $("input[name=pcBoth]").val([$('#existBillTerms').val()]);
                    var checkprintPpdBlBoth = getPpdBLBothValue($('#fileId').val(), "PRINTPPDBLBOTH");
                    if (checkprintPpdBlBoth === 'Y') {
                        $('#printPpdBlBothY').attr('checked', true);
                        $('#printPpdBlBothN').attr('checked', false);
                    } else {
                        $('#printPpdBlBothY').attr('checked', false);
                        $('#printPpdBlBothN').attr('checked', true);
                    }
                }
            }
        });
    }
}
function updateBillingType(ele) {
    var flag = validateBillToParty(ele, false);
    var termsType1 = $('#termsType1').val();
    var preventExpRelRole = $('#preventExpRel').val();
    var billType = $("input:radio[name=pcBoth]:checked").val();
    var billToParty = $("input:radio[name=billPPD]:checked").val();
    if (flag) {
        if ((termsType1 === 'ER' && preventExpRelRole === 'true' && billType === 'P') &&
                ((billToParty === 'F' && $('#fwdStatus').val() === 'No Credit') || (billToParty === 'S' && $('#status').val() === 'No Credit') || (billToParty === 'T' && $('#thirdPartyStatus').val() === 'No Credit'))) {
            var filenumberId = $('#fileId').val();
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "checkPersonalEffects",
                    param1: filenumberId,
                    dataType: "json"
                },
                success: function (data) {
                    if (data === 'N') {
                        congAlert("BL prepaid party doesn't have credit");
                        $("input[name=billPPD]").val([$('#hiddenBillToCode').val()]);
                        return false;
                    } else {
                        $.prompt("Please note that all Bill to Code will be changed for all Charges", {
                            buttons: {
                                Yes: 1,
                                No: 2
                            },
                            submit: function (v) {
                                if (v === 1) {
                                    updateBillToCode(ele);
                                    $('#hiddenBillToCode').val(ele);
                                } else if (v === 2) {
                                    $("input[name=billPPD]").val([$('#hiddenBillToCode').val()]);
                                }
                            }
                        });
                    }
                }
            });
        } else {
            $.prompt("Please note that all Bill to Code will be changed for all Charges", {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        updateBillToCode(ele);
                        $('#hiddenBillToCode').val(ele);
                    } else if (v === 2) {
                        $("input[name=billPPD]").val([$('#hiddenBillToCode').val()]);
                    }
                }
            });
        }
    }
}
function updateBillToCode(ele) {
    var billType = $("input:radio[name=pcBoth]:checked").val();
    if ($('#fileNumberId').val() !== "" && billType !== 'B') {
        submitAjaxFormforBill("updateBillToCode", "#lclBlForm", "#chargeBlDesc", billType, ele);
    }
}
function submitAjaxFormforBill(methodName, formName, selector, billType, billToParty) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&pcBoth=" + billType + "&billPPD=" + billToParty + "&whsePhone=" + $('#hiddenBillToCode').val() + "&existBillTerms=" + $('#existBillTerms').val();
    $.post($(formName).attr("action"), params, function (data) {
        $(selector).html(data);
        $(selector, window.parent.document).html(data);
        hideProgressBar();
    });
    $('#hiddenBillToCode').val(billToParty);
    $('#existBillTerms').val(billType);
}
function updateBillToBoth(fileId, billType) {
    $.prompt("Are you sure want to change the Prepaid/Collect to Both", {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {

                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO",
                        methodName: "updateBillToCode",
                        param1: fileId,
                        param2: billType,
                        dataType: "json"
                    },
                    success: function () {
                        $(".blBillingType").text("BOTH");
                        $('#existBillTerms').val('B');
                        var checkprintPpdBlBoth = getPpdBLBothValue(fileId, "PRINTPPDBLBOTH");
                        if (checkprintPpdBlBoth === 'Y') {
                            $('#printPpdBlBothY').attr('checked', true);
                            $('#printPpdBlBothN').attr('checked', false);
                        } else {
                            $('#printPpdBlBothY').attr('checked', false);
                            $('#printPpdBlBothN').attr('checked', true);
                        }
                        checkBilltoCode();
                        setDisabledBillTo();
                    }
                });
            } else {
                $("input[name=pcBoth]").val([$('#existBillTerms').val()]);
                var checkprintPpdBlBoth = $('#checkprintPpdBlBoth').val();
                if (checkprintPpdBlBoth === 'Y') {
                    $('#printPpdBlBothY').attr('checked', true);
                    $('#printPpdBlBothN').attr('checked', false);
                } else {
                    $('#printPpdBlBothY').attr('checked', false);
                    $('#printPpdBlBothN').attr('checked', true);
                }
            }
        }
    });
}
/********************************  POST and UNPOST Logic ******************************************* */
function processPostedunPosted(roleDutyForUnPost) {
    allowToPost(roleDutyForUnPost);
}
function ProcessToPost(roleDutyForUnPost) {
    if (getPostAlert()) {
        roleDutyForUnPost = roleDutyForUnPost === 'true' ? true : false;
        var loginUserId = $('#loginUserId').val();
        if ($("#file_status").val() === 'M') {
            $.prompt("BL is already manifested");
        } else if ($("#postButton").attr('class').indexOf('green-background') !== -1 && roleDutyForUnPost) {
            confirmPostCondition('Are you sure want to unpost?', 'UP', loginUserId, roleDutyForUnPost);
        } else if (checkTermToDoBLAndRateType() && checkBillToPartyIsEmpty() && postcheck() && validateLclBlGoCollect()
                && $("#postButton").attr('class').indexOf('green-background') === -1) {
            var txtMsg = isVoyageValid();
            if ($("#forwarderCode").val() === 'NOFRTA0001' && checkDocCharge()) {
                $.prompt("No FF rule- this BL should have documentation, do you want to add it now?", {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v === 1) {
                            addDocumentCharge(path);
                        } else {
                            $('#postComment').val(true);
                            if (txtMsg !== "" && txtMsg !== null) {
                                checkHasAutoRates(txtMsg, loginUserId, roleDutyForUnPost);
                            } else {
                                checkHasAutoRates('Are you sure want to Post?', loginUserId, roleDutyForUnPost);
                            }
                        }
                    }
                });
            } else {
                $('#postComment').val(false);
                if (txtMsg !== "" && txtMsg !== null) {
                    checkHasAutoRates(txtMsg, loginUserId, roleDutyForUnPost);
                } else {
                    checkHasAutoRates('Are you sure want to Post?', loginUserId, roleDutyForUnPost);
                }

            }
        }
    }
}

function checkTermToDoBLAndRateType() {
    var rateType = $("#rateType").val();
    var terminalNo = $("#trmnum").val();
    var ratesFromTerminalNo = $("#ratesFromTerminalNo").val();
    rateType = rateType === 'R' ? 'Y' : rateType;
    var flag = true;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO",
            methodName: "getActYon",
            param1: terminalNo,
            param2: ratesFromTerminalNo,
            param3: rateType,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (rateType !== data) {
                flag = false;
            }
        }
    });
    if (!flag) {
        $.prompt("Term To Do BL does not belong to the Rate Type selected (Retail/Coload/FTF).  Please correct this problem.");
    }
    return flag;
}

function confirmPostCondition(txt, status, loginId, roleDutyForUnPost) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                setPostStatus(loginId, status);
                if (status === 'UP') {
                    $("#poolPostedData").text("POOL");
                    $("#poolPostedData").removeClass("purpleBold");
                    $("#poolPostedData").addClass("orangeBold");
                    $("#postButton").removeClass('green-background');
                    $("#postButton").addClass('button-style1');
                    //  $(".void_Button").hide();
                } else if (status === 'P') {
                    $("#poolPostedData").text("POSTED");
                    $("#poolPostedData").addClass("purpleBold");
                    $("#poolPostedData").removeClass("orangeBold");
                    $("#postButton").addClass('green-background');
                    // $(".void_Button").show();
                    if (!roleDutyForUnPost) {
                        $("#postButton").removeAttr('onclick');
                    }
                }
            }
        }
    });
}

function checkBillToPartyIsEmpty() {
    var flag = true;
    $(".billToParty").each(function () {
        var billToParty = $(this).html().trim().trim();
        if (billToParty === "") {
            flag = false;
        }
    });
    if (!flag) {
        $.prompt("Please ensure all charge items have a bill to party");
    }
    return flag;
}



function checkHasAutoRates(txtMsg, loginId, roleDuty) {
    var filenumberId = $('#fileId').val();
    var freeBL = $('input:radio[name=freeBl]:checked').val();
    if (freeBL === 'N') {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO",
                methodName: "hasAutoRates",
                param1: filenumberId,
                param2: "Exports",
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data === 'false') {
                    $.prompt("This B/L has no Ocean Freight charges, is this correct?", {
                        buttons: {
                            Yes: 1,
                            No: 2
                        },
                        submit: function (v) {
                            if (v === 1) {
                                confirmPostCondition(txtMsg, 'P', loginId, roleDuty);
                            } else {
                                $.prompt.close();
                            }
                        }
                    });
                } else {
                    confirmPostCondition(txtMsg, 'P', loginId, roleDuty);
                }
            }
        });
    } else {
        confirmPostCondition(txtMsg, 'P', loginId, roleDuty);
    }
}

function postcheck() {
    var flag = true;
    var term1 = $('#termsType1').val();
//    var docsBl = document.getElementById('docsBl').checked;
//    var docsCaricom = document.getElementById('docsCaricom').checked;
//    var docsAes = document.getElementById('docsAes').checked;
    var aesRequiredForPostingBLs = $("#aesRequiredForPostingBLs").val();
    var forwardAcctNo = $("#forwarderCode").val();
    var shipperAcctNo = $("#shipperCode").val();
    var thirdpartyAcctNo = $("#thirdpartyaccountNo").val();
    var agentAcctNo = $("#agentacctno").val();
    var terms = ($("#radioP").attr("checked") ? "P" : $("#radioC").attr("checked") ? "C" : $("#radioB").attr("checked") ? "B" : "");
    var billToCode = ($("#billF").attr("checked") ? "F" : $("#billS").attr("checked") ? "S" : $("#billT").attr("checked") ? "T" : $("#billA").attr("checked") ? "A" : "");
    if (aesRequiredForPostingBLs === 'true' && $(".aes_button").attr('class').indexOf('green-background') === -1) {
        $.prompt("AES required for Posting BL");
        flag = false;
    } else if (term1 === "") {
        $.prompt("Please select the TermsType1 in Terms Section");
        $("#termsType1").css("border-color", "red");
        flag = false;
    } else if (forwardAcctNo === "") {
        $.prompt("Forwarder Account is required");
        $("#forwarderName").css("border-color", "red");
        flag = false;
    } else if (terms === "B") {
        var autoBillTo = new Array();
        autoBillTo = getAutoBillToCode().split(",");
        if (shipperAcctNo === "" && autoBillTo.contains("S")) {
            $.prompt("Shipper Account is required");
            $("#shipContactName").css("border-color", "red");
            flag = false;
        } else if (thirdpartyAcctNo === "" && autoBillTo.contains("T")) {
            $.prompt("ThirdParty Account is required");
            $("#thirdPartyname").css("border-color", "red");
            flag = false;
        } else if (agentAcctNo === "" && autoBillTo.contains("A")) {
            $.prompt("Agent Account is required");
        }
    } else if (terms === "P") {
        if (shipperAcctNo === "" && billToCode === "S") {
            $.prompt("Shipper Account is required");
            $("#shipContactName").css("border-color", "red");
            flag = false;
        } else if (thirdpartyAcctNo === "" && billToCode === "T") {
            $.prompt("ThirdParty Account is required");
            $("#thirdPartyname").css("border-color", "red");
            flag = false;
        }
    } else if (terms === "C" && agentAcctNo === "" && billToCode === "A") {
        $.prompt("Agent Account is required");
    }
    return flag;
}
function checkPost() {

}


function setPostStatus(userId, flag) {
    var fileId = $('#fileNumberId').val();
    var postCmd = $('#postComment').val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO",
            methodName: "updatePostStatus",
            param1: fileId,
            param2: userId,
            param3: flag,
            param4: postCmd,
            dataType: "json"
        },
        preloading: true,
        success: function (v) {
            submitBlForm("saveBl");
        }
    });
}

function filterNoFreightForwarderAc() {
    var forwardAcctNo = $("#forwarderCode").val();
    var flag;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO",
            methodName: "getFreightForwardAcctStatus",
            param1: forwardAcctNo,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}

function filterNoFreightForwarderBlDocs() {
    var forwardAcctNo = $("#forwarderCode").val();
    var flag;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO",
            methodName: "getFreightForwardAcctBl",
            param1: forwardAcctNo,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}
/******************************** End ******************************************* */
/******************************** Void Button Logic  **************************** */
function set_void_Button() {
//    if ($("#file_status").val() === "V") {
//        $("#blOwner").attr("alt", '');
//        showAllReadOnly();
//    }
//    if ($("#postedByUser").val() === '') {
//        $(".void_Button").hide();
//    }
}

function Voided_And_UnVoided() {
    $.prompt("Are you sure want to void this BL ?", {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                blVoided();
            } else {
                $.prompt.close();
            }
        }
    });
}
function addVoidRemarks() {
    showAlternateMask();
    $("#add-void-bl").center().show(500, function () {
        $('#voidComments').val('');
    });
}
function blVoided() {
    if ($('#blUnitCob').val() === 'true' && $('#file_status').val() === 'M') {
        showAlternateMask();
        $("#add-void-approvePassword").center().show(500, function () {
        });
    } else {
        addVoidRemarks();
    }
}
function approveCobPassword() {
    var approvePassword = $('#approvePassword').val();
    if (approvePassword === "" || approvePassword === null) {
        $.prompt("Please enter password");
        return false;
    }
    if ($("#loginUserPassword").val() !== approvePassword) {
        $.prompt("Invalid password");
        $('#approvePassword').val("");
        return false;
    }

    $("#add-void-approvePassword").center().hide(500, function () {
    });
    addVoidRemarks();
}
function addBlVoidComments() {
    var comments = $('#voidComments').val();
    if (comments === '' || comments === null) {
        $.prompt("BL Void Comments is Required");
        return false;
    }
    submitBlVoid(comments);
}
function submitBlVoid(comments) {
    var fileId = $('#fileNumberId').val();
    showProgressBar();
    $("#methodName").val("updateVoidInBl");
    var params = $("#lclBlForm").serialize() + "&voidComments=" + comments;
    $.post($("#lclBlForm").attr("action"), params, function () {
        if (parent.$("#toScreenName").val() === 'EXP VOYAGE') {
            revertToBookingFromUnitScreen(path, fileId);
        } else {
            window.parent.changeLclChilds(path, fileId, "B", "Exports");
        }
        hideProgressBar();
    });
}
//function submit_for_void() {
//    window.parent.showLoading();
//    $("fileNumberId").val();
//    $("#methodName").val("updateVoidInBl");
//    $("#lclBlForm").submit();
//}
/******************************** End ******************************************* */
function showFields(id) {
    $('#' + id).show();
}
function hideFields(id) {
    $('#' + id).hide();
}
function disabledFormElement(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "text" || element.type == "textarea") {
            if (element.id == "stdchgratebasis" || element.id == "msrForM" || element.id == "ofratebasis"
                    || element.id == "wgtForM" || element.id == "wgtForI" || element.id == "msrForI") {
            } else {
                element.style.backgroundColor = "#CCEBFF";
                element.readOnly = true;
                element.style.borderTop = "0px";
                element.style.borderBottom = "0px";
                element.style.borderRight = "0px";
                element.style.borderLeft = "0px solid";
            }
        }
        if (element.type == "select-one" || element.type == "radio" || element.type == "checkbox") {
            element.style.border = 0;
            element.disabled = true;
        }
    }
}
function showAllReadOnly() {
    $('#save').hide();
    $('#saveR').hide();
    $('#arInvoice').hide();
    $('#invoiceR').hide();
    $('#copy').hide();
    $('#copyR').hide();
    $('#revert').hide();
    $('#revertR').hide();
    $('#commodity1').hide();
    $('.aes_button').hide();
    $('#consolidate').hide();
    $('#calculate').hide();
    $('#costCharge').hide();
    $('#pickupInfo').hide();
    $('.Blhazmat').hide();
    $('.inbondButton').hide();
    $('.disabledBtn').hide();
    $("#postButton").hide();
    var form = document.getElementById('lclBlForm');
    disabledFormElement(form);
    var imgs = document.getElementsByTagName("img");
    for (var k = 0; k < imgs.length; k++) {
        if (imgs[k].id !== "trianleicon" && imgs[k].id !== "collpaseicon") {
            imgs[k].style.visibility = "hidden";
        }
        if (imgs[k].id === "viewgif" || imgs[k].id === "viewgif1" || imgs[k].id === "viewgif2") {
            imgs[k].style.visibility = "visible";
        }
    }
}

function upcomingSailingsResult(path) {
    $.prompt("This will allow to change the ECI Voy#?", {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                var poo = $('#portOfOriginId').val();
                var pol = $('#portOfLoadingId').val();
                var pod = $('#portOfDestinationId').val();
                var fd = $('#finalDestinationId').val();
                var cfcl = $('#cfcl').val();
                var relayOverride = $('#relayOverride').val();
                var upcomingSailings = $('#masterScheduleNo').val();
                var href = path + "/lclBl.do?methodName=upcomingSailings&poo=" + poo +
                        "&pol=" + pol + "&pod=" + pod + "&fd=" + fd + "&sailings=" + upcomingSailings + "&relayOverride=" + relayOverride + "&cfcl=" + cfcl;
                $.colorbox({
                    iframe: true,
                    href: href,
                    width: "90%",
                    height: "70%",
                    title: "<span style=color:blue>Upcoming Sailings</span>"
                });
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}

function showClientSearchOption(path, searchByValue) {
    var href = path + "/lclBl.do?methodName=clientSearch&searchByValue=" + searchByValue;
    $(".clientSearchEditBl").attr("href", href);
    $(".clientSearchEditBl").colorbox({
        iframe: true,
        width: "35%",
        height: "40%",
        title: "<span style=color:blue>" + searchByValue + " Search" + "</span>"
    });
}

function updateBillToParty() {
    var val = $("#hiddenBillToCode").val();
    switch (val) {
        case 'S' :
            $("#billS").attr("checked", true);
            break;
        case 'F':
            $("#billF").attr("checked", true);
            break;
        case 'T' :
            $("#billT").attr("checked", true);
            document.getElementById('clientSearchEditBlT').style.visibility = 'visible';
            break;
        case 'A' :
            $("#billA").attr("checked", true);
            break;
    }
}
function getAutoBillToCode() {
    var fileId = $('#fileNumberId').val();
    var billTo = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getAutoBillToCode",
            param1: fileId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            billTo = data;
        }
    });
    return billTo;
}

function showSpotRateInfo(path, fileNumberId, fileNumber) {
    var href = path + "/lclBl.do?methodName=displaySpotRate&fileNumberId=" + fileNumberId
            + "&fileNumber=" + fileNumber;
    $.colorbox({
        iframe: true,
        href: href,
        width: "37%",
        height: "65%",
        title: "SpotRate"
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
                showProgressBar();
                var spotRate = $('input:radio[name=spotRate]').val();
                var pol = $('#polUnlocationcode').val();
                var pod = $('#podUnlocationcode').val();
                var destination = $('#unlocationCode').val();
                var origin = $('#pooCode').val();
                $("#methodName").val("recaculateSpotRate");
                var params = $("#lclBlForm").serialize();
                params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod + "&spotRate=" + spotRate;
                $.post($("#lclBlForm").attr("action"), params, function (data) {
                    $('#commodityDesc').html(data.commodityDesc);
                    $('#chargeBlDesc').html(data.chargeDesc);
                    $('#lclSpotRate').removeClass("green-background");
                    $('#lclSpotRate').addClass("button-style1");
                    $('#existSpotRate').val("false");
                    $("#lclSpotRate").hide();
                    $("#spotratelabel").text('');
                    hideProgressBar();
                });
            } else {
                $('input:radio[name=spotRate]').val(['Y']);
                $.prompt.close();
            }
        }
    });
}
function deleteChargesByFreeBlYes() {
    var txt = "This will remove all charges, and will not allow charges to be added. Are you sure?";
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                $("#methodName").val("deleteChargesByFreeBlYes");
                var params = $("#lclBlForm").serialize();
                $.post($("#lclBlForm").attr("action"), params, function (data) {
                    $('#chargeBlDesc').html(data);
                    $('#chargeBlDesc', window.parent.document).html(data);
                    $('#calculate').hide();
                    $('#costCharge').hide();
                    $('input:radio[name=spotRate]').attr('disabled', true);
                    if ($("input:radio[name='spotRate']:checked").val() == 'Y') {
                        $('#lclSpotRate').attr("disabled", true);
                        $('#lclSpotRate').removeClass("green-background");
                        $('#lclSpotRate').addClass("gray-background");
                    }
                    hideProgressBar();
                });
            } else {
                $('input:radio[name=freeBl]').val(['N']);
                $.prompt.close();
            }
        }
    });
}

function calculateChargesByFreeBlNo() {
    var txt = "Are you sure want to recalculate rates?";
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                var pol = $('#polUnlocationcode').val();
                var pod = $('#podUnlocationcode').val();
                var destination = $('#unlocationCode').val();
                var origin = $('#pooCode').val();
                $("#methodName").val("calculateChargesByFreeBlNo");
                var params = $("#lclBlForm").serialize();
                params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod;
                $.post($("#lclBlForm").attr("action"), params, function (data) {
                    $('#chargeBlDesc').html(data);
                    $('#chargeBlDesc', window.parent.document).html(data);
                    $('#calculate').show();
                    $('#costCharge').show();
                    $('input:radio[name=spotRate]').attr('disabled', false);
                    if ($("input:radio[name='spotRate']:checked").val() == 'Y') {
                        $('#lclSpotRate').attr("disabled", false);
                        $('#lclSpotRate').removeClass("gray-background");
                        $('#lclSpotRate').addClass("green-background");
                    }
                    hideProgressBar();
                });
            } else {
                $('input:radio[name=freeBl]').val(['Y']);
                $.prompt.close();
            }
        }
    });
}

function avoidBundleCheckBox(flag) {
    var oceanBillToParty = $(".billingCharge0").text().trim().trim();
    var count = 0;
    var otherBillToParty = "";
    var isBundle = false;
    var uncheckedChargeId = "";
    $(".billToParty").each(function () {
        otherBillToParty = $(".billingCharge" + count).text().trim().trim();
        if (oceanBillToParty !== otherBillToParty) {
            $(".bundle" + count).attr("checked", false);
            $(".bundle" + count).attr("disabled", true);
            uncheckedChargeId += $(".chargeId" + count).text().trim().trim() + ",";
        } else {
            if (flag && isBundle) {
                $(".bundle" + count).attr("checked", true);
            }
            $(".bundle" + count).attr("disabled", false);
        }
        count++;
    });
    if (uncheckedChargeId !== "") {
        updateForBundleOFR(uncheckedChargeId);
    }
}

function updateForBundleOFR(uncheckedChargeId) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBlAcDAO",
            methodName: "updateBundleOFR",
            param1: uncheckedChargeId,
            param2: "false",
            dataType: "json"
        },
        async: false,
        close: function () {
        }
    });
}


function disabledCondition(element) {
    element.style.backgroundColor = "#CCEBFF";
    element.readOnly = true;
    element.style.borderTop = "0px";
    element.style.borderBottom = "0px";
    element.style.borderRight = "0px";
    element.style.borderLeft = "0px solid";
}
function checkBlOwnerWithPriority() {
    commonDisableMethod();
    $('#save').show();
    $('#saveR').show();
    $('.hideBtn').attr("disabled", true);
    $('#calculate').hide();
    $('#costCharge').hide();
    var billToParty = $("input:radio[name=billPPD]:checked").val();
    var pcBoth = $("input:radio[name=pcBoth]:checked").val();
    var form = document.getElementById('lclBlForm');
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        var className = $("#" + form.elements[i].id).attr('class');
        if (element.type === "text" || element.type === "textarea") {
            if (className !== undefined && className.indexOf('shipperClass') !== -1) {
                className = billToParty === 'S' ? undefined : className;
            }
            if (className === undefined || (className.indexOf('shipperClass') === -1
                    && className.indexOf('commonClass') === -1)) {
                if (element.id == "freightPickupText" || element.id == "portText" || element.id == "hblPierText" || element.id == "hblPolText" || element.id == "deliveryText" || element.id == "editShipAcctName" || element.id == "editNotyAcctName") {
                    element.disabled = false;
                } else {
                    disabledCondition(element);
                }
            }
        }
        if ((element.type === "select-one" && element.name != "termsType1") || element.type === "radio" || element.type === "checkbox") {
            if (className === undefined || (className.indexOf('shipperClass') === -1
                    && className.indexOf('commonClass') === -1)) {
                if (element.name == "aesCodes" || element.name == "hsCodes" || element.name == "ncmCodes" || element.name == "freightPickup" || element.name == "imperial" || element.name == "alert"
                        || element.name == "proof" || element.name == "metric" || element.name == "port" || element.name == "payment" || element.name == "mini" || element.name == "pier"
                        || element.name == "arrival" || element.name == "hblPier" || element.name == "hblPol" || element.name == "deliveryOverride" || element.name == "ladenSailDate" || element.name == "printTermsType"
                        || element.name == "correctedBl" || element.name == "printHazBefore" || element.name == "printPpdBlBoth" || element.name == "printInsurance")
                {
//                    alert(element.name);
                    element.style.border = 0;
                    element.disabled = false;
                } else {
                    element.style.border = 0;
                    element.disabled = true;
                }


            }
        }
    }
    if (pcBoth != 'C' && billToParty == 'S' && jQuery("#shipperName").val() != "") {
        $('#editShipAcctName').val($("#shipperName").val());
        $('#editAcctDiv').show();
        $('#manualShipper').hide();
        $('#dojoShipper').hide();
        $("#newShipper").attr('disabled', true);
    }
    var imgs = document.getElementsByTagName("img");
    for (var k = 0; k < imgs.length; k++) {
        var className = $("#" + imgs[k].id).attr('class');
        if (className === undefined) {
            imgs[k].style.visibility = "hidden";
        } else if (className.indexOf("hideImg") !== -1 || className.indexOf('shipperClass') !== -1
                || className.indexOf('commonClass') !== -1) {
            imgs[k].style.visibility = "hidden";
        }
    }
}
function displayNotesPopUp(path, fileNumberId, fileNumber, moduleName) {
    var href = path + "/lclRemarks.do?methodName=display&fileNumberId=" + fileNumberId +
            "&fileNumber=" + fileNumber + "&moduleName=" + moduleName + "&actions=manualNotes&moduleId=Bl";
    $(".notes").attr("href", href);
    $(".notes").colorbox({
        iframe: true,
        width: "65%",
        height: "75%",
        title: "Notes",
        scrolling: false
    });
}

function roleDutyCheck() {
    var termsType1 = $('#termsType1').val();
    var preventExpRelRole = $('#preventExpRel').val();
    var billType = $("input:radio[name=pcBoth]:checked").val();
    var billToParty = $("input:radio[name=billPPD]:checked").val();
    if (termsType1 === 'ER' && preventExpRelRole === 'true' && billType === 'P') {
        var filenumberId = $('#fileId').val();
        if (billToParty === 'F' && $('#forwarderCode').val() === '') {
            congAlert("Please enter Forwarder account first");
            $('#termsType1').val('');
            return false;
        } else if (billToParty === 'S' && $('#shipperCode').val() === '') {
            congAlert("Please enter Shipper account first");
            $('#termsType1').val('');
            return false;
        } else if (billToParty === 'T' && $('#thirdpartyaccountNo').val() === '') {
            congAlert("Please enter Thirdparty account first");
            $('#termsType1').val('');
            return false;
        }
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkPersonalEffects",
                param1: filenumberId,
                dataType: "json"
            },
            success: function (data) {
                if ((billToParty === 'F' && $('#fwdStatus').val() === 'No Credit') || (billToParty === 'S' && $('#status').val() === 'No Credit') || (billToParty === 'T' && $('#thirdPartyStatus').val() === 'No Credit')) {
                    if (data === 'N') {
                        congAlert("BL prepaid party doesn't have credit");
                        $('#termsType1').val('');
                        return false;
                    }
                }
            }
        });
    }
}


function saveTerms() {
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "saveTermsType",
            param1: $('#fileId').val(),
            param2: $('#termsType1').val(),
            dataType: "json"
        }

    });
}
function addInsurance() {
    var insurance = $('input:radio[name=insurance]:checked').val();
    if (insurance == "Y") {
        if ($('#valueOfGoods').val() == null || $('#valueOfGoods').val() == "") {
            $('#valueOfGoods').removeAttr("readonly");
            $("#valueOfGoods").removeClass("textlabelsBoldForTextBoxDisabledLook");
            $("#valueOfGoods").css("border-color", "red");
        }
    }
}
function calculateInsuranceCharge() {
    var pol = $('#polUnlocationcode').val();
    var pod = $('#podUnlocationcode').val();
    var destination = $('#unlocationCode').val();
    var origin = $('#pooCode').val();
    var fileId = $('#fileNumberId').val();
    var insurance = $('input:radio[name=insurance]:checked').val();
    if (insurance == "Y" && ($('#valueOfGoods').val() == null || $('#valueOfGoods').val() == "")) {
        $.prompt("Please Enter the Value of Goods");
        $("#valueOfGoods").css("border-color", "red");
        $("#warning").show();
    } else {
        submitAjaxFormForInsurance('calculateInsuranceCharge', '#lclBlForm', '#chargeBlDesc',
                origin, destination, pol, pod, fileId, $('#valueOfGoods').val(), insurance);
        $('#printInsuranceY').attr('disabled', false);
        $('#printInsuranceN').attr('disabled', false);
    }
}
function removeInsuranceCharge() {
    var fileId = $('#fileNumberId').val();
    if (fileId != "") {
        var pol = $('#polUnlocationcode').val();
        var pod = $('#podUnlocationcode').val();
        var destination = $('#unlocationCode').val();
        var origin = $('#pooCode').val();
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
                        submitAjaxFormForInsurance('calculateInsuranceCharge', '#lclBlForm', '#chargeBlDesc',
                                origin, destination, pol, pod, fileId, $('#valueOfGoods').val(), insurance);
                        $("#valueOfGoods").val('');
                        $("#cifValue").val('');
                        $('#printInsuranceY').attr('disabled', true);
                        $('#printInsuranceN').attr('disabled', true);
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
        if ($("#valueOfGoods").val() == '') {
            $("#cifValue").val('');
        }
    }
}
function addDocumentCharge(path) {
    var fileId = $('#fileNumberId').val();
    if (fileId === '' || fileId === '0') {
        $.prompt("Please Save Bl");
    } else {
        var fileNumber = $('#fileNumber').val();
        var billingType = $('input:radio[name=billPPD]:checked').val();
        billingType = billingType != undefined ? billingType : 'F';
        var billToParty = $('input:radio[name=pcBoth]:checked').val();
        var href = path + "/lclBlCostAndCharge.do?methodName=displayDocum&fileNumberId=" +
                fileId + "&fileNumber=" + fileNumber + "&billToParty=" + billToParty + "&billingType=" + billingType;
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
        if (checkDocCharge()) {
            return false;
        }
        $('#documSave').val('');
        var pol = $('#polUnlocationcode').val();
        var pod = $('#podUnlocationcode').val();
        var destination = $('#unlocationCode').val();
        var origin = $('#pooCode').val();
        var documentation = $('input:radio[name=documentation]:checked').val();
        $.prompt("Are you sure you want to remove Documentation charge?", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    showProgressBar();
                    submitAjaxFormForInsurance('deleteDocumCharge', '#lclBlForm', '#chargeBlDesc',
                            origin, destination, pol, pod, fileId, documentation);
                    $.prompt.close();
                } else if (v == 2) {
                    $.prompt.close();
                    $('#docYes').attr('checked', true);
                }
            }
        });
    }
}
function submitAjaxFormForInsurance(methodName, formName, selector, origin, destination, pol, pod, fileId, valueOfGoods, insurance) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol
            + "&pod=" + pod + "&fileNumberId=" + fileId + "&valueOfGoods="
            + valueOfGoods + "&insurance=" + insurance;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}
function lockInsurance() {
    var destination = $('#unlocationCode').val();
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

function getPostAlert() {
    var flag = true;
    var fileId = $('#fileNumberId').val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO",
            methodName: "getPostAlert",
            param1: fileId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            var originalValue = false;
            if (data.includes("1")) {
                originalValue = true;
            }
            if (originalValue === true && $("#postButton").attr('class').indexOf('green-background') !== 0) {
                $.prompt("Piece,CFT,CBM,LBS and KGS should be greater than zero");
                flag = false;
            }
        }
    });
    return flag;
}

/*Start: Add & delete Delivery Metro PSRJU charges*/
function calculateDeliveryMetroCharge() {
    var hiddenDeliveryMetro = $('#deliveryMetroField').val();
    var deliveryMetro = $('input:radio[name=deliveryMetro]:checked').val();
    var fileId = $('#fileNumberId').val();
    var txtMsg = deliveryMetro === "N" ? "Delivery charge will be removed.Are you sure You want to continue?" :
            "Delivery charge will be added.Are you sure You want to continue?";
    if (fileId !== "" && deliveryMetro !== hiddenDeliveryMetro) {
        $.prompt(txtMsg, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    submitAjaxFormForDeliveryMetro('calculateDeliveryMetroChargeBl', '#lclBlForm', '#chargeBlDesc', deliveryMetro, fileId);
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

function updateDeliveryMetro() {
    var val = $("#deliveryMetroField").val();
    switch (val) {
        case 'I' :
            $("#deliveryMetroI").attr("checked", true);
            break;
        case 'O':
            $("#deliveryMetroO").attr("checked", true);
            break;
        case 'N' :
            $("#deliveryMetroN").attr("checked", true);
            break;
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
// mantis item: 14485
function enableFormElements() {
    $('#termsType1').attr("disabled", true);
    $('#save').show();
    $('.void_Button').hide();
    $('#saveR').show();
    $('#blOwner').css('background-color', '#FFFFFF');
    $('#blOwner').attr("readonly", false);
    $('#type2Date').attr("readonly", true);
    $(".printOnBL").attr("disabled", false);
    document.getElementById("type2DateImg").style.visibility = "hidden";
}
// Mantis item:14117
function validateLclBlGoCollect() {
    var billType = $("input:radio[name=pcBoth]:checked").val();
    var podUnlocCode = $('#podUnlocationcode').val();
    var flag = true;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO",
            methodName: "getLclOceanbl",
            param1: podUnlocCode,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data != 'Y' && billType == 'C') {
                flag = false;
                $.prompt("BL can not be Collect.");
            } else if (data != 'Y' && !checkBillToPartyIsAgent()) {
                $.prompt("BL can not be Collect.Please ensure that charges are not bill to Agent.");
                flag = false;
            }
        }
    });
    return flag;
}
function checkBillToPartyIsAgent() {
    var flag = true;
    $(".billToParty").each(function () {
        var billToParty = $(this).html().trim().trim();
        if (billToParty == "Agent") {
            flag = false;
        }
    });
    return flag;
}
function setExportsReference() {
    var consAcctNo = $('#consigneeCode').val();
    var text = $('#eReference').val().toUpperCase();
    var eReference = '';
    var arr = new Array();
    var newArr = new Array();
    arr = text.split("\n");
    for (var i = 0; i < arr.length; i++) {
        if (arr[i].trim() != "" && arr[i].trim() != null && (!arr[i].includes('COMMREG') && !arr[i].includes('FEDID'))) {
            newArr.push(arr[i] + "\n");
        }
    }
    if (newArr.length < 3) {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO",
                methodName: "getTradingPartnerValues",
                param1: consAcctNo,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                $('#eReference').val('');
                eReference = newArr.toString();
                if (data[0] !== "" && data[0] != null) {
                    eReference += "COMMREG # " + data[0] + "   ";
                }
                if (data[1] !== "" && data[1] != null) {
                    eReference += "FEDID  " + data[1];
                }
                $('#eReference').val(eReference.replace(",", ""));
            }
        });
    }
}

function validateNoEeiAes(fileId) {
    var noEEiMessage = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "validateNoEeiAes",
            param1: fileId,
            param2: "bl",
            dataType: "json"

        },
        async: false,
        success: function (data) {
            noEEiMessage = data;
        }
    });
    return noEEiMessage;
}
function noEeiAestoAdd(fileId) {
    var preventMessage = preventtoAddNoEeiaes(fileId);
    if (preventMessage === 'AES_ITNNUMBER') {
        $.prompt(" <span style='color:red'>Please Enter ITN Number only</span> ");
    } else {
        var errorMessage = validateNoEeiAes(fileId);
        if (errorMessage !== "") {
            $.prompt(errorMessage + " Are you sure you want to continue ?", {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        $.ajaxx({
                            data: {
                                className: "com.gp.cong.lcl.dwr.LclDwr",
                                methodName: "noEeiAestoAdd",
                                param1: fileId,
                                param2: "bl",
                                request: true

                            },
                            success: function (data) {
                                if (data === 'true') {
                                    $('.aes_button').addClass("green-background");
                                }
                            }
                        });
                    } else if (v == 2) {
                        $.prompt.close();
                    }
                }
            });
        } else {
            noEeiAes(fileId);
        }
    }
}
function noEeiAes(fileId) {
    var text = "Are you sure you want to add the following AES exception: NOEEI 30.37 (A) LOW VALUE";
    $.prompt(text, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "noEeiAestoAdd",
                        param1: fileId,
                        param2: "bl",
                        request: true

                    },
                    success: function (data) {
                        if (data === 'true') {
                            $('.aes_button').addClass("green-background");
                        }
                    }
                });
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}

function preventtoAddNoEeiaes(fileId) {
    var noEEiMessage = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "preventtoAddNoEeiaes",
            param1: fileId,
            param2: "bl",
            dataType: "json"

        },
        async: false,
        success: function (data) {
            noEEiMessage = data;
        }
    });
    return noEEiMessage;
}


function isVoyageValid() {
    var pickedOnVoyageNo = $("#pickedOnVoyageNo").val();
    var bookedForVoyageNo = $('#bookedForVoyageNo').val();
    var eciVoyage = $("#eciVoyage").val();
    var sailDate = $("#sailDate").val();
    var sailDate1 = $("#sailDate1").val();
    var bookedForVoyagesailDate = $("#bookedForVoyagesailDate").val();
    var text = "";
    if (eciVoyage !== null && eciVoyage !== "") {
        if ((pickedOnVoyageNo !== null && pickedOnVoyageNo !== "")) {
            if (pickedOnVoyageNo != eciVoyage) {
                text = "ECI Voy# <span style=color:red>" + eciVoyage + "(" + sailDate + ")</span> is not matching with Picked on Voyage <span style=color:red>" + pickedOnVoyageNo + "(" + sailDate1 + ")</span>. Do you want to continue posting?";
            }
        } else if (bookedForVoyageNo !== null && bookedForVoyageNo !== "") {
            if (bookedForVoyageNo != eciVoyage) {
                text = "ECI Voy# <span style=color:red>" + eciVoyage + "(" + sailDate + ")</span> is not matching with Booked for Voyage <span style=color:red>" + bookedForVoyageNo + "(" + bookedForVoyagesailDate + ")</span>. Do you want to continue posting?";
            }
        }

    }
    return text;
}
function setPortOfDestinationFromBkg() {
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.common.constant.ExportUtils",
            methodName: "getPortOfDestination",
            param1: $("#fileNumberId").val(),
            request: "true",
            dataType: "json"
        },
        async: false,
        success: function (data) {
            $('#portOfDestination').val(data[0]);
            $('#portOfDestinationId').val(data[1]);
            $('#podUnlocationcode').val(data[2]);
            $('#relayOverride').val(data[3]);
            $('#portOfLoading').val(data[4]);
            $('#portOfLoadingId').val(data[5]);
            $('#polUnlocationcode').val(data[6]);
            $('#finalDestinationf').val(data[7]);
            $('#finalDestinationId').val(data[8]);
            $('#unlocationCode').val(data[9]);
            $('#deliveryMetroField').val(data[10]);
        }
    });
}

function printExportBLReport(path, fileId, fileNo, screenName, transhipment, subject) {
    var portText = $('#portText').val();
    var freightPickupText = $('#freightPickupText').val();
    var hblPierText = $('#hblPierText').val();
    var hblPolText = $('#hblPolText').val();
    var deliveryText = $('#deliveryText').val();
    if ($("#portY").is(':checked') && (portText == '')) {
        $.prompt('Print Alt Port lower right is required in print option');
        return false;
    }
    if ($("#freightPickupY").is(':checked') && (freightPickupText == '')) {
        $.prompt('Freight Pickup account is required in print option ');
        return false;
    }
    if ($("#hblPierY").is(':checked') && (hblPierText == '')) {
        $.prompt('HBL Pier Override is required in print option ');
        return false;
    }
    if ($("#hblPolY").is(':checked') && (hblPolText == '')) {
        $.prompt('HBL Pol Override is required in print option ');
        return false;
    }
    if ($("#deliveryOverrideY").is(':checked') && (deliveryText == '')) {
        $.prompt('Final Delivery To override is required in print option ');
        return false;
    }

    var postedLclBl = $("#postButton").attr("class").indexOf('green-background') !== -1 ? "postBl" : "unPostBl";
    var billToParty = $("input:radio[name=billPPD]:checked").val();
    billToParty = billToParty === 'A' ? "Agent" : billToParty === 'S' ? "Shipper" : billToParty === 'F'
            ? "Forwarder" : billToParty === 'T' ? "ThirdParty" : "";
    var comment = "";
    if ($('#externalComment').val() !== undefined && $('#externalComment').val() !== '') {
        comment = $('#externalComment').val();
    }
    var cob = $('#blUnitCob').val() === '' ? false : $('#blUnitCob').val();
    var issuTerm = $("#terminal").val();
    issuTerm = screenName === 'LCLBL' ? issuTerm : '';
    var url = path + "/printConfig.do?screenName=" + screenName + "&fileId=" + fileId + "&cob=" + cob;
    url += "&fileNo=" + fileNo + "&comment=" + comment + "&subject=" + subject + "&postedLclBl=" + postedLclBl;
    url += "&emailSubject=" + subject + "&transhipment=" + transhipment + "&billToParty=" + billToParty;
    url += "&issuingTerminal=" + issuTerm + "&manifestFlag=" + $("#manifestFlagId").val();
    if ($("#printFlag").val() === "true") {
        showProgressBar();
        $("#editRateType").val($('input:radio[name=rateType]:checked').val());
        $("#pier").val(bl_pier);
        $("#methodName").val("saveBl");
        var params = $("#lclBlForm").serialize();
        $.post($("#lclBlForm").attr("action"), params, function (data) {
            $("#printFlag").val("false");
            hideProgressBar();
            GB_show("Print", url, 350, 1000);
        });
    } else {
        GB_show("Print", url, 350, 1000);
    }
}
function allowToPost(roleDutyForUnPost) {
    countLine('shipperAddress', 'Shipper');
    countLine('consigneeAddress', 'Consignee');
    countLine('notifyAddress', 'Notify');
    countLine('forwarderAddressa', 'Forwarder');
    if (countLinesError !== "" && $("#postButton").attr('class').indexOf('green-background') !== 0) {
        var index = countLinesError.lastIndexOf(",");
        var CountValue = countLinesError.substring(0, index);
        $.prompt(CountValue + " will have info that will not print on the BL because  more than 4 lines have been used", {
            buttons: {
                Continue: 1,
                GoBack: 2
            },
            submit: function (v) {
                if (v === 1) {
                    countLinesError = "";
                    ProcessToPost(roleDutyForUnPost);
                } else if (v === 2) {
                    countLinesError = "";
                    $.prompt.close();
                }
            }
        });
    } else {
        ProcessToPost(roleDutyForUnPost);
    }
}
function countLine(id, label) {
    var valId = $("#" + id).val();
    var trimedValue = valId.trim();
    var arr = new Array();
    if (trimedValue !== '') {
        arr = trimedValue.split("\n");
        var j = 0;
        for (i = 0; i < arr.length; i++) {
            j++;
            if (arr[i].length > 44) {
                j++;
            }
        }
        if (j > 4) {
            countLinesError = countLinesError + label + ",";
        }
    }

}

function getPpdBLBothValue(fileId, value) {
    var optionKey = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getOptions",
            param1: fileId,
            param2: value,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data !== undefined) {
                optionKey = data;
            }
        }
    });
    return optionKey;
}

function submitAjaxFormForCharges(amount, comments) {
    var blChargeId = $("#chargeIdTotal").val();
    var adjustmentAmount = amount - $("#oldOriginalChargeVal").val();
    var substringComments = (comments.length > 100 ? comments.substring(0, 99) : comments).toUpperCase();
    var userId = $("#loginUserId").val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.struts.action.lcl.LclBlAction",
            methodName: "reChargeAdjustment",
            param1: blChargeId,
            param2: adjustmentAmount,
            param3: substringComments,
            param4: userId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            showLoading();
            $("#methodName").val("editBooking");
            $("#lclBlForm").submit();
        }
    });
}
function modifyAdjustmentAmount(comments) {
    var blbkgChargeId = $('#chargeIdAdj').val(); // itscall a common file BKG/BL
    var newAdjusmentAmt = Number($('#adjustmentAmount' + blbkgChargeId).val());
    var substringComments = (comments.length > 100 ? comments.substring(0, 99) : comments).toUpperCase();
    var userId = $("#loginUserId").val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.struts.action.lcl.LclBlAction",
            methodName: "reChargeAdjustment",
            param1: blbkgChargeId,
            param2: newAdjusmentAmt,
            param3: substringComments,
            param4: userId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            showLoading();
            $("#methodName").val("editBooking");
            $("#lclBlForm").submit();
        }
    });
}

function ratesTypeBasedToRecalculateAutoRates(rateType) {
    $('#rateType').val(rateType);
    var fileNumberId = $('#fileNumberId').val();
    var pol = $('#polUnlocationcode').val();
    var pod = $('#podUnlocationcode').val();
    var destination = $('#unlocationCode').val();
    var origin = $('#pooCode').val();
    document.getElementById('selectedRateType').innerHTML = (rateType === "R" ? "Retail" : rateType === "C" ? "Coload" : "Foreign to Foreign");
    if (getRateTypeFlag(rateType, fileNumberId)) {
        $.prompt('Do you want to re-calculate auto rates based on this new selection?', {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    submitAjaxForm('calculateCharges', '#lclBlForm', '#chargeBlDesc', origin, destination, pol, pod);
                    updateRateType(rateType, fileNumberId);
                } else {
                    updateRateType(rateType, fileNumberId);
                }
            }
        });
    }
}

function updateRateType(rateType, fileId) {
    $.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO",
            methodName: "updateRateType",
            param1: rateType,
            param2: fileId
        },
        async: false,
        close: function (data) {

        }
    });
}

function getRateTypeFlag(rateType, fileId) {
    var result = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO",
            methodName: "getRateTypeFlag",
            param1: fileId,
            param2: rateType,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
}
function checkDocCharge() {
    var flag = true;
    $('#chargesTable .chargeCode').each(function () {
        var chargeCode = $(this).text();
        if (chargeCode.trim() == 'DOCUM') {
            flag = false;
        }
    });
    return flag;
}
function validateNotify() {
    var sameAsCons = $('#sameasConsignee').is(":checked");
    var newNotify = $('#newNotify').is(":checked");
    if (sameAsCons && newNotify) {
        $('#dupNotifyName').val($('#consigneeName').val());
    }
}
function clearNotify() {
    var notify = $("#notifyCode").val();
    if (notify === "") {
        $("#notifyAddress").val('');
    }
}
