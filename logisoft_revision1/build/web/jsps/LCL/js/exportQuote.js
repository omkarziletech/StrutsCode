var path = "/" + window.location.pathname.split('/')[1];
jQuery(document).ready(function () {
    toggleVoyageInformation();
    lockQuoteInsurance();
    deliveryMetro();
    showCityName();
    setCreditStatus("forwarderCreditClient", "forwarderCredit");
    setCreditStatus('clientCreditClient', 'creditForClient');
    checkBilltoCode();
    setSupplierManualCheckBox();
    setConsManualCheckBox();
    setShipperManualCheckBox();
    displayClientDetails();//setClienttooltip
    displayShipperDetails();//setShippertooltip
    displayConsigneeDetails();//setConsigneetooltip
    displayForwarderDetails();//setForwardertooltip
    setPoaDetails();
    setPoaStatus('supplierPoa', 'supplierPoaId');
    changerelay();
    fillPolPod();
    setCfclValues();



    $("#forwarderNameClient").change(function () {
        clearForwarderTextValue();
    });

});
function getPredefinedRemarks(path, multipleSelect) {
    var url = path + '/lclBooking.do?methodName=getPredefinedRemarks&multipleSelect=' + multipleSelect;
    $('#remarks-img').attr("href", url);
    $('#remarks-img').colorbox({
        iframe: true,
        width: "50%",
        height: "70%",
        title: "Predefined remarks"
    });
}
function updateCharges(id, index) {
    var checkValues = "";
    if (index == 'B') {
        checkValues = $('#bundleToOf' + id).is(":checked");
    } else {
        checkValues = $('#printOnBL' + id).is(":checked");
    }
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "updateQuoteCharges",
            param1: id,
            param2: checkValues,
            param3: index,
            dataType: "json"
        },
        async: false,
        success: function (data) {
        }
    });
}
function calculateAdjustmentAmount(qtChargeId, oldValue) {//update adjusmentAmt and calculate caf charges
    showAlternateMask();
    $("#add-Comments-container").center().show(500, function () {
        $('#headingAdjustmentComments').text('Adjustment Comments:');
        $('#adjustmentcomments').val('');
        $('#qtChargeId').val(qtChargeId);
        $('#oldAdjusmentVal').val(oldValue);
    });
}

function modifyAdjustmentAmount(comments) {
    var qtChargeId = $('#qtChargeId').val();
    var chargeAmt = Number($('#chargeAmount' + qtChargeId).val());
    var newAdjusmentAmt = Number($('#adjustmentAmount' + qtChargeId).val());
    var totalAmount = chargeAmt + newAdjusmentAmt;
    $('#tdTotalAdjAmt' + qtChargeId).val(totalAmount.toFixed(2));
    var totalChargeAmt = 0;
    var adjustmentAmount = 0;
    $(".chargeAmount").each(function () {
        totalChargeAmt += Number($(this).text().trim());
    });
    $(".adjustmentAmount").each(function () {
        $(this).val(Number($(this).val()).toFixed(2));
        adjustmentAmount += Number($(this).val().trim());
    });
    if (qtChargeId != null && qtChargeId != "") {
        var fileNumberId = $('#fileNumberId').val();
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkCAFQuoteRates",
                param1: qtChargeId,
                param2: newAdjusmentAmt,
                param3: comments,
                param4: fileNumberId,
                request: true,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data) {
                    calculateCAFCharge();
                } else {
                    submitAjaxFormForAdjustment('renderChargeDescAdjustment', '#lclQuoteForm', '#chargeDesc', fileNumberId);
                }
            }
        });
    }
}
function addAdjustmentComments() {
    var comments = $('#adjustmentcomments').val();
    if (comments.trim() === "") {
        $.prompt('Please Enter Adjustment Comments');
        return false;
    }
    modifyAdjustmentAmount(comments);
    $("#add-Comments-container").center().hide(500, function () {
        hideAlternateMask();
    });
}
function cancelAdjustmentComments() {
    $("#add-Comments-container").center().hide(500, function () {
        var qtChargeId = $('#qtChargeId').val();
        $('#adjustmentAmount' + qtChargeId).val($('#oldAdjusmentVal').val());
        $('#qtChargeId').val('');
        $('#oldAdjusmentVal').val('');
        hideAlternateMask();
    });
}
function updateBillingType() {
    var billingType = $('input:radio[name=pcBoth]:checked').val();
    if (billingType === 'P') {
        $('#billF').attr("checked", true);
    } else if (billingType === 'C') {
        $('#billA').attr("checked", true);
    } else if (billingType === 'B') {
        $('.disableBillToCode').attr("checked", false);
    }
    checkBilltoCode();
    var fileId = $('#fileNumberId').val();
    if (fileId !== '') {
        calculateCAFCharge();
    }
}
function calculateCAFCharge() {
    var fileId = document.getElementById('fileNumberId').value;
    if (fileId) {
        var destination = $('#unlocationCode').val();
        var origin = getOrigin();
        var pol = $('#polUnlocationcode').val();
        var pod = $('#podUnlocationcode').val();
        var pcboth = $('input:radio[name=pcBoth]:checked').val();
        if (pcboth == 'C') {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "checkLclCollectCharges",
                    param1: destination,
                    dataType: "json"
                },
                async: false,
                success: function (data) {
                    if (data) {
                        submitAjaxFormForCAF('calculateCAFCharge', '#lclQuoteForm', '#chargeDesc', origin, destination, pol, pod, fileId, pcboth);
                    }
                }
            });
        }
        else if (pcboth == 'P' || pcboth == 'B') {
            submitAjaxFormForCAF('calculateCAFCharge', '#lclQuoteForm', '#chargeDesc', origin, destination, pol, pod, fileId, pcboth);
        }
    }
}

function submitAjaxFormForCAF(methodName, formName, selector, origin, destination, pol, pod, fileId, pcboth) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod + "&fileNumberId=" + fileId + "&pcboth=" + pcboth;
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
    params += "&fileNumberId =" + fileId;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}

function clientSearch() {//client dojo by consignee chceckbox
    if (($('#clientSearchState').val() != "" && $('#clientSearchState').val() != null) &&
            ($('#clientSearchZip').val() != "" && $('#clientSearchZip').val() != null) &&
            ($('#clientSearchSalesCode').val() != "" && $('#clientSearchSalesCode').val() != null)) {
        if ($('#clientWithConsignee').is(":checked")) {
            $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_STATE_ZIP_SP');
        } else {
            $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_STATE_ZIP_SP');
        }
    }
    else {
        if (($('#clientSearchState').val() != "" && $('#clientSearchState').val() != null) &&
                ($('#clientSearchZip').val() != "" && $('#clientSearchZip').val() != null)) {
            if ($('#clientWithConsignee').is(":checked")) {
                $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_STATE_ZIP');
            }
            else {
                $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_STATE_ZIP');
            }
        } else if (($('#clientSearchZip').val() != "" && $('#clientSearchZip').val() != null) &&
                ($('#clientSearchSalesCode').val() != "" && $('#clientSearchSalesCode').val() != null)) {
            if ($('#clientWithConsignee').is(":checked")) {
                $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_ZIP_SP');
            }
            else {
                $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_ZIP_SP');
            }
        } else if (($('#clientSearchSalesCode').val() != "" && $('#clientSearchSalesCode').val() != null) &&
                ($('#clientSearchState').val() != "" && $('#clientSearchState').val() != null)) {
            if ($('#clientWithConsignee').is(":checked")) {
                $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_STATE_SP');
            }
            else {
                $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_STATE_SP');
            }
        } else {
            if ($('#clientSearchState').val() != "" && $('#clientSearchState').val() != null) {
                if ($('#clientWithConsignee').is(":checked")) {
                    $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_STATE');
                } else {
                    $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_STATE');
                }
            }
            else if ($('#clientSearchSalesCode').val() != "" && $('#clientSearchSalesCode').val() != null) {
                if ($('#clientWithConsignee').is(":checked")) {
                    $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_SP');
                }
                else {
                    $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_SP');
                }
            } else if ($('#clientSearchZip').val() != "" && $('#clientSearchZip').val() != null) {
                if ($('#clientWithConsignee').is(":checked")) {
                    $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_ZIP');
                } else {
                    $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_ZIP');
                }
            }
        }
    }
}
function toggleVoyageInformation() {//upcoming sailings
    var disp = document.getElementById('upcomingSailing').style.display;
    if (disp == "block" || disp == "") {
        $('#exp').text('');
        $('#col').text("Click to Expand");
        jQuery('#upcomingSailing').slideToggle();
         $("#upcomingSailing").css("max-height", "100px");
        $("#upcomingSailing").css("overflow-y", "auto");
    }
    else {
        $('#col').text('');
        $('#exp').text("Click to Hide");
        jQuery('#upcomingSailing').slideToggle();
        $("#upcomingSailing").css("max-height", "100px");
        $("#upcomingSailing").css("overflow-y", "auto");
    }
}
function fillaesbyQtRateType() {//ratetype based on set values in aes radiobutton
    var ratetype = $('input:radio[name=rateType]:checked').val();
    var trmnum = $('#trmnum').val();
    $('#aesByN').attr('checked', true);
    if ($("#fileNumberId").val() === '' && trmnum === '59') {
        $('#aesByN').attr('checked', true);
    } else if (ratetype === "R") {
        $('#aesByY').attr('checked', true);
    }
}
function checkOriginTerminal() {
    var pooUnlocCode = getOrigin();
    var ratetype = $('input:radio[name=rateType]:checked').val();
    var rateTypes;
    if (ratetype === "R") {
        rateTypes = "Retail";
    }
    else if (ratetype === "C") {
        rateTypes = "Coload";
    } else if (ratetype === "F") {
        rateTypes = "Foreign to Foreign";
    }
    if ((pooUnlocCode !== undefined && pooUnlocCode !== "") && (ratetype !== undefined && ratetype !== "")) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkTerminal",
                param1: pooUnlocCode,
                param2: ratetype
            },
            async: false,
            success: function (data) {
                if (!data) {
                    $.prompt(rateTypes + " Terminal is not available.Please select different origin");
                    $('#portOfOriginR').val('');
                    $('#portOfOriginR').attr("readonly", false);
                    $('#portOfOriginR').css("border-color", "red");
                    $('#portOfOriginR').removeClass("text-readonly");
                    $('#portOfOriginR').removeClass("textlabelsBoldForTextBoxDisabledLook");
                    $('#portOfOriginR').addClass("textlabelsBoldForTextBox");
                }
                //                else {
                //                    showMinimumRatesForColoadRatetype();
                //            }
            }
        });
    }
    if (pooUnlocCode !== undefined && pooUnlocCode !== "") {
        var nonRated = $('input:radio[name=nonRated]:checked').val();
        var pol = $('#portOfLoading').val();
        if (nonRated === 'Y' && pol === '') {
            $('#portOfLoading').val($('#portOfOriginR').val());
        }
    }
}
function showMinimumRatesForColoadRatetype() {//Calculating Coload Rates
    var pooUnlocCode = getOrigin();
    var fdUnlocCode = getDestination();
    var ratetype = $('input:radio[name=rateType]:checked').val();
    var fileId = $('#fileNumberId').val();
    if ((fileId === null || fileId === "" || fileId === undefined) && (ratetype === 'C' && ratetype !== "" && ratetype !== undefined)) {
        if ((pooUnlocCode !== null && pooUnlocCode !== "" && pooUnlocCode !== undefined) && (fdUnlocCode !== null && fdUnlocCode !== "" && fdUnlocCode !== undefined)) {
            submitFormForCoload('modifyColoadRates');
        }
    }
}
function submitFormForCoload(methodName) {
    showLoading();
    $('#methodName').val(methodName);
    document.lclQuoteForm.methodName.value = methodName;
    document.lclQuoteForm.submit();
}
function displayMultiRates(path, fileNumberId, doorOriginCityZip) {//Routing Options
    var pooUnlocCode = getOrigin();
    var ert = $('#rtdTransaction').val();
    var zip = $(doorOriginCityZip).val();
    var destinationValue = $('#finalDestinationR').val();
    var datatableobj = document.getElementById('commObj');
    var rateType = $('input:radio[name=rateType]:checked').val();
    if ((zip == null || zip == "") && (pooUnlocCode == null || pooUnlocCode.length == 0)) {
        $.prompt("Please Select Door Origin/Origin CFS");
    }
    else if (destinationValue == null || destinationValue.length == 0) {
        $.prompt("Please Select Destination");
        $("#finalDestinationR").css("border-color", "red");
    }
    else if (rateType == "" || rateType == null) {
        $.prompt("CTC/Retail/FTF  is required");
    }
    else if (ert == "" || ert == null) {
        $.prompt("ERT field is required");
        $("#rtdTransaction").css("border-color", "red");
    }
    else if (datatableobj === null) {
        $.prompt("Please add atleast one commodity/tariff#");
        return false;
    }
    else {
        if (rateType === "R") {
            rateType = "Y";
        }
        var fdUnlocCode = getDestination();
        var fd = $('#finalDestinationId').val();
        var href = path + "/lclQuotemultiRate.do?methodName=displayRates&origin=" + pooUnlocCode + "&destination=" + fdUnlocCode + "&destinationValue=" + destinationValue +
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
}
function openContact(path, accountName, accountNo, contactName, subtype) {
    if (($('#client').val() == "" || $('#client_no').val() == "")) {
        $.prompt('Contact are not allowed, Please select client name and try.');
    }
    else if ($('#contactName').val() != "" || $('#clientContactManul').val() != "" || $('#shipperContactClient').val() != "" || $('#shipperManualContact').val() != "" ||
            $('#consigneeContactName').val() != "" || $('#consigneeManualContact').val() != "" || $('#forwardercontactClient').val() != "" || $('#forwarederContactManual').val() != "") {
        $.prompt('Only one Booking Contact are allowed, Please remove the existing one and try.');
    }
    else {
        openContactPopUp(path, accountName, accountNo, contactName, subtype, "Client");
    }
}
function openShipContact(path, accountName, accountNo, contactName, subtype) {
    if ($('#shipperNameClient').val() == "" || $('#shipperCodeClient').val() == "") {
        $.prompt('Contact are not allowed, Please select shipper name and try.');
    }
    else if ($('#contactName').val() != "" || $('#clientContactManul').val() != "" || $('#shipperContactClient').val() != "" || $('#shipperManualContact').val() != "" ||
            $('#consigneeContactName').val() != "" || $('#consigneeManualContact').val() != "" || $('#forwardercontactClient').val() != "" || $('#forwarederContactManual').val() != "") {
        $.prompt('Only one Booking Contact are allowed, Please remove the existing one and try.');
    }
    else {
        openContactPopUp(path, accountName, accountNo, contactName, subtype, "Shipper");
    }
}
function openQtFwdContact(path, accountName, accountNo, contactName, subtype) {
    if ($('#forwarderNameClient').val() == "" || $('#forwarderCodeClient').val() == "") {
        $.prompt('Contact are not allowed, Please select forwarder name and try.');
    }
    else if ($('#contactName').val() != "" || $('#clientContactManul').val() != "" || $('#shipperContactClient').val() != "" || $('#shipperManualContact').val() != "" ||
            $('#consigneeContactName').val() != "" || $('#consigneeManualContact').val() != "" || $('#forwardercontactClient').val() != "" || $('#forwarederContactManual').val() != "") {
        $.prompt('Only one Booking Contact are allowed, Please remove the existing one and try.');
    }
    else {
        openContactPopUp(path, accountName, accountNo, contactName, subtype, "Forwarder");
    }
}
function openConsContact(path, accountName, accountNo, contactName, subtype) {
    if ($('#consigneeNameClient').val() == "" || $('#consigneeCodeClient').val() == "") {
        $.prompt('Contact are not allowed, Please select consignee name and try.');
    }
    else if ($('#contactName').val() != "" || $('#clientContactManul').val() != "" || $('#shipperContactClient').val() != "" || $('#shipperManualContact').val() != "" ||
            $('#consigneeContactName').val() != "" || $('#consigneeManualContact').val() != "" || $('#forwardercontactClient').val() != "" || $('#forwarederContactManual').val() != "") {
        $.prompt('Only one Booking Contact are allowed, Please remove the existing one and try.');
    }
    else {
        openContactPopUp(path, accountName, accountNo, contactName, subtype, "Consignee");
    }
}
function openContactPopUp(path, vendorName, vendorNo, contactName, subType, popUpName) {
    var href = path + "/lclContactDetails.do?methodName=display&vendorName="
            + vendorName + "&vendorNo=" + vendorNo + "&contactName=" + contactName +
            "&vendorType=" + subType;
    $.colorbox({
        iframe: true,
        href: href,
        width: "90%",
        height: "90%",
        title: popUpName + " Contact"
    });
}
function lockQuoteInsurance() {
    var fdUnlocCode = getDestination();
    var ratetype = $('input:radio[name=rateType]:checked').val();
    var quoteComplete = $('input:radio[name=quoteComplete]:checked').val();
    var quoteCompleted = $('input:radio[name=quoteCompleted]:checked').val();
    var insurance = $('input:radio[name=insurance]:checked').val();
    var fileId = $('#fileNumberId').val();
    if (insurance == 'N') {
        $("#valueOfGoods").addClass("textlabelsBoldForTextBoxDisabledLook");
        $('#valueOfGoods').attr("readonly", true);
    }
    if (fdUnlocCode != "" && (ratetype != "C" || ratetype != "F" || ratetype != "R")) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkInsurance",
                param1: fdUnlocCode
            },
            async: false,
            success: function (data) {
                if (data === "true") {
                    $('#insuranceY').attr('checked', false);
                    $('#insuranceY').attr('disabled', true);
                    $('#insuranceN').attr('disabled', true);
                    $('#insuranceN').attr('checked', false);
                    $("#valueOfGoods").addClass("textlabelsBoldForTextBoxDisabledLook");
                    $('#valueOfGoods').attr("readonly", true);
                } else {
                    if (fdUnlocCode != "" && (ratetype != "C" && ratetype != "R" && ratetype != "F") && (fileId != null || fileId != "" || fileId != undefined) && (quoteComplete == 'N' || quoteCompleted == 'N') && (insurance != 'N')) {
                        $('#insuranceY').attr('disabled', false);
                        $('#insuranceN').attr('disabled', false);
                        $('#valueOfGoods').removeAttr("readonly");
                        $("#valueOfGoods").removeClass("textlabelsBoldForTextBoxDisabledLook");
                    }
                    else if (fdUnlocCode != "" && (ratetype == "C" || ratetype == "R" || ratetype == "F")) {
                        if ((quoteComplete == "N" || quoteCompleted == "N") && (insurance == "Y")) {
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
function deliveryMetro() {
    var deliveryMetro = $('input:radio[name=deliveryMetro]:checked').val();
    if (deliveryMetro === 'N') {
        $('#deliveryMetroI').attr('disabled', true);
        $('#deliveryMetroO').attr('disabled', true);
        $('#deliveryMetroN').attr('checked', true);
    }
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
            }
            else {
                $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
            }
        }
        else {
            if ($('#showFullRelayFd').is(":checked")) {
                $('#finalDestinationR').attr('alt', 'CONCAT_CITY_NAME');
            }
            else {
                $('#finalDestinationR').attr('alt', 'CONCAT_COUNTRY_NAME');
            }
        }
    }
    else {
        if (nonRated == 'N') {
            if ($('#basedOnCity').is(":checked")) {
                $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
            }
            else {
                $('#finalDestinationR').attr('alt', 'CONCAT_COUNTRY_NAME');
            }
        }
        else {
            if ($('#basedOnCity').is(":checked")) {
                if ($('#showFullRelayFd').is(":checked")) {
                    $('#finalDestinationR').attr('alt', 'CONCAT_PORT_NAME');
                }
                else {
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
function showHideRelay() {
    $('#portOfOriginR').val('');
    $('#portOfOriginId').val('');
    var destination = $('#finalDestinationR').val();
    var relay = $('input:radio[name=relayOverride]:checked').val();
    var nonRated = $('input:radio[name=nonRated]:checked').val();
    if (relay == 'Y') {
        if ($('#showFullRelay').is(":checked")) {
            $('#portOfOriginR').attr('alt', 'PORTNAME');
        }
        else {
            $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
        }
    }
    else {
        if (nonRated == 'N') {
            if (destination != "") {
                $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
            }
            else {
                if ($('#showFullRelay').is(":checked")) {
                    $('#portOfOriginR').attr('alt', 'PORTNAME');
                }
                else {
                    $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
                }
            }
        }
        else {
            if ($('#showFullRelay').is(":checked")) {
                $('#portOfOriginR').attr('alt', 'PORTNAME');
            }
            else {
                if (destination != "") {
                    $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
                }
                else {
                    $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
                }
            }
        }
    }
}
function addInsurance() {
    var insurance = $('input:radio[name=insurance]:checked').val();
    var fileId = $('#fileNumberId').val();
    if (insurance === "Y") {
        var valueOfGoods = $("#valueOfGoods").val();
        var commodityObj = $('#commObj').html();
        if (fileId === null || fileId === "" || fileId === "0" || fileId === undefined) {
            $.prompt('Please save Quote');
            $('#insuranceN').attr('checked', true);
            $("#valueOfGoods").val("");
        } else if (commodityObj === null) {
            $.prompt('Please add Weight and Measure from Commodity');
            $('#insuranceN').attr('checked', true);
            $("#valueOfGoods").val("");
        } else if (valueOfGoods === null || valueOfGoods === "") {
            $('#valueOfGoods').removeAttr("readonly");
            $("#valueOfGoods").removeClass("textlabelsBoldForTextBoxDisabledLook");
            $("#valueOfGoods").css("border-color", "red");
        }
    }
}
function calculateInsCharges() {
    var insurance = $('input:radio[name=insurance]:checked').val();
    var valueOfGoods = $('#valueOfGoods').val();
    if (insurance === "Y" && (valueOfGoods === null || valueOfGoods === "")) {
        $.prompt("Please enter the Value of Goods");
        $("#valueOfGoods").css("border-color", "red");
        $("#warning").show();
    } else {
        submitAjaxFormForInsurance('calculateInsuranceCharge', '#lclQuoteForm', valueOfGoods, insurance);
        $('#showAstar').show();
        $('#showAstarDestn').show();
        $('#portOfOriginR').addClass("text-readonly");
        $('#finalDestinationR').addClass("text-readonly");
        $('#portOfOriginR').attr("readonly", true);
        $('#finalDestinationR').attr("readonly", true);
    }
}

function removeInsuranceCharge() {
    var insurance = $('input:radio[name=insurance]:checked').val();
    if (($("#valueOfGoods").val() != '') && (insurance == 'N')) {
        $.prompt('Are you sure you want to remove insurance charge and value of goods', {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    showProgressBar();
                    submitAjaxFormForInsurance('calculateInsuranceCharge', '#lclQuoteForm', $('#valueOfGoods').val(), insurance);
                    $("#valueOfGoods").val('');
                    $("#cifValue").val('');
                    $.prompt.close();
                }
                else if (v == 2) {
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
function submitAjaxFormForInsurance(methodName, formName, valueOfGoods, insurance) {
    var pooUnlocCode = getOrigin();
    var fdUnlocCode = getDestination();
    var polUnlocCode = $('#polCode').val();
    var podUnlocCode = $('#podCode').val();
    var fileId = $('#fileNumberId').val();
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&origin=" + pooUnlocCode + "&destination=" + fdUnlocCode + "&pol=" + polUnlocCode + "&pod=";
    params += podUnlocCode + "&fileNumberId=" + fileId + "&valueOfGoods=" + valueOfGoods + "&insurance=" + insurance;
    $.post($(formName).attr("action"), params,
            function (data) {
                $('#chargeDesc').html(data);
                $('#chargeDesc', window.parent.document).html(data);
                hideProgressBar();
            });
}
//function removeDocum() {
//    var fileId = $('#fileNumberId').val();
//    $('#documSave').val('');
//    if (fileId != "") {
//        removeDocumCharge('Are you sure you want to remove Documentation charge?');
//    }
//}
function addQuoteDocumCharge(path, fileId) {
    var fileNo = $('#fileNumber').val();
    if (fileNo === "") {
        $.prompt("Please Save Quote");
        $('#docYesQ').attr('checked', false);
        $('#docNoQ').attr('checked', true);
    } else {
        var fdUnlocCode = getDestination();
        var href = path + "/lclQuoteCostAndCharge.do?methodName=displayDocum&fileNumberId=" + fileId + "&fileNumber=" + fileNo + "&destination=" + fdUnlocCode;
        $.colorbox({
            iframe: true,
            href: href,
            width: "32%",
            height: "35%",
            title: "Charges",
            onClosed: function () {
                var hiddenDocumCharge = $('#documSave').val();
                if (hiddenDocumCharge == 'S') {
                    $('#docYesQ').attr('checked', true);
                } else {
                    $('#docNoQ').attr('checked', true);
                }
            }
        });
    }
}
function removeDocumCharge() {
    var documentation = $('input:radio[name=documentation]:checked').val();
    $.prompt('Are you sure you want to remove Documentation charge?', {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                $('#documSave').val('')
                showProgressBar();
                submitAjaxFormForInsurance('deleteDocumCharge', '#lclQuoteForm', documentation, '');
                $.prompt.close();
            }
            else if (v == 2) {
                $.prompt.close();
                $('#docYesQ').attr('checked', true);
            }
        }
    });
}
function quoteDeliverCargo() {
    var pooUnlocCode = getOrigin();
    var fileNumber = $('#fileNumber').val();
    if (fileNumber != "" && fileNumber != null) {
        var concatFileNo = pooUnlocCode.substring(2) + "-" + fileNumber;
        $('#fileNumberQuote').text(concatFileNo);
    }
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePadDAO",
            methodName: "updateQuoteDeliveryContactForExp",
            param1: $("#fileNumberId").val(),
            param2: pooUnlocCode,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            deliverCargodata(data);
        }
    });
    $('#finalDestinationR').focus().val();
}
function deliverCargodata(data) {
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
        $("#deliveryCargoToCode").val("");
        $("#deliverCargoToName").val("");
        $("#deliverCargoToAddress").val("");
        $("#deliverCargoToCity").val("");
        $("#deliverCargoToState").val("");
        $("#deliverCargoToZip").val("");
        $("#deliverCargoToPhone").val("");
        $("#deliverCargoToFax").val("");
    }
}
function checkBilltoCode() {
    var billingType = $('input:radio[name=pcBoth]:checked').val();
    $('.disableBillToCode').attr("disabled", true);
    if (billingType === 'P') {
        $('#billF').attr("disabled", false);
        $('#billS').attr("disabled", false);
        $('#billT').attr("disabled", false);
    } else if (billingType === 'C') {
        $('#billA').attr("disabled", false);
    }
    if (billingType !== 'C') {
        $('#thirdPartyName').removeClass('textlabelsBoldForTextBoxDisabledLook');
        $('#thirdPartyName').removeAttr('readonly');
        $('#thirdPartyAccount').addClass('textlabelsBoldForTextBoxDisabledLook');
        $('#thirdPartyAccount').attr('readonly', true);
    }
}
function setSupplierManualCheckBox() {
    var supManual = $('#dupSupplierName').val();
    var acctNo = $("#supplierCode").val();
    if (supManual != '' && acctNo === '') {
        hideSupplierByDojo();
    } else {
        showSupplierByDojo();
    }
}
function hideSupplierByDojo() {
    $("#dojoSupplier").hide();
    $("#manualSupplier").show();
    $("#newSupplier").attr('checked', true);
}
function showSupplierByDojo() {
    $('#dupSupplierName').val('');
    $("#dojoSupplier").show();
    $("#manualSupplier").hide();
    $("#newSupplier").attr('checked', false);
}
function supplierAcctCheck() {
    if ($('#supplierDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#supplierDisabledAcct').val() + "</span>");
        clearSupplierFeilds();
    } else {
        setPoaStatus('supplierPoa', 'supplierPoaId');
    }
}
function newSupplierName() {
    if ($('#newSupplier').is(":checked")) {
        hideSupplierByDojo()
    } else {
        showSupplierByDojo();
    }
    clearSupplierFeilds();
}
function clearSupplierFeilds() {
    $("#supplierName").val("");
    $("#supplierCode").val("");
    $("#supplierPhone").val("");
    $("#supplierFax").val("");
    $("#supplierEmail").val("");
    $("#supplierAddress").val("");
    $("#supplierCity").val("");
    $("#supplierCountry").val("");
    $("#supplierState").val("");
    $("#supplierZip").val("");
    $("#supplierPoa").val("");
    $("#supReference").val("");
    $("#supplierClient").val("");
    $("#newSupplierClientCity").val("");
    $("#supplierPoa").val("");
    $("#supplierPoaId").text("");
}

function calculateMetroCharges() {
    var hiddenDeliveryMetro = $('#deliveryMetroField').val();
    var deliveryMetro = $('input:radio[name=deliveryMetro]:checked').val();
    var fileId = $('#fileNumberId').val();
    if (fileId !== "" && deliveryMetro !== hiddenDeliveryMetro) {
        var txt = 'The old Delivery charge will be removed from the charges.Are you sure You want to recalculate Rates?';
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    submitAjaxFormForDeliveryMetro('calculateDeliveryMetroCharge', '#lclQuoteForm', '#chargeDesc', deliveryMetro, fileId);
                    var deliveryMetro = $('input:radio[name=deliveryMetro]:checked').val();
                    $('#deliveryMetroField').val(deliveryMetro);
                    $.prompt.close();
                }
                else if (v == 2) {
                    $('#deliveryMetroField').val(hiddenDeliveryMetro);
                    if (hiddenDeliveryMetro == "I") {
                        $('#deliveryMetroI').attr('checked', true);
                    }
                    else if (hiddenDeliveryMetro == "O") {
                        $('#deliveryMetroO').attr('checked', true);
                    }
                    else if (hiddenDeliveryMetro == "N") {
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
function addCalcHeavyCharges() {
    var fileId = $('#fileNumberId').val();
    var calcHeavy = $('input:radio[name=calcHeavy]:checked').val();
    if (fileId != "") {
        submitAjaxFormForCalcHeavy('calculateHeavyCharge', '#lclQuoteForm', '#chargeDesc', fileId, calcHeavy);
    }
}
function removeCalcHeavyCharge() {
    var fileId = $('#fileNumberId').val();
    if (fileId != "") {
        $.prompt('Are you sure you want to remove Calc Heavy/Dense/ExLen?', {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    var calcHeavy = $('input:radio[name=calcHeavy]:checked').val();
                    submitAjaxFormForCalcHeavy('calculateHeavyCharge', '#lclQuoteForm', '#chargeDesc', fileId, calcHeavy);
                    $.prompt.close();
                }
                else if (v == 2) {
                    $.prompt.close();
                    $('#calcYes').attr('checked', true);
                }
            }
        });
    }
}
function submitAjaxFormForCalcHeavy(methodName, formName, selector, fileId, calcHeavy) {
    var pooUnlocCode = getOrigin();
    var fdUnlocCode = getDestination();
    var polUnlocCode = $('#polUnlocationcode').val();
    var podUnlocCode = $('#podUnlocationcode').val();
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&origin=" + pooUnlocCode + "&destination=" + fdUnlocCode + "&pol=" + polUnlocCode + "&pod=" + podUnlocCode + "&fileNumberId=" + fileId + "&calcHeavy=" + calcHeavy;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}
function clearBkgContactShipper() {
    $('#shipperEmailClient').val('');
    $('#shipperManualContact').val('');
    $('#shipperContactClient').val('');
}
function newShipperContactName() {
    if ($('#newShipperContact').is(":checked")) {
        $('#shippManual').show();
        $('#shipperDojo').hide();
        clearBkgContactShipper();
    } else {
        $('#shippManual').hide();
        $('#shipperDojo').show();
        clearBkgContactShipper();
    }
}
function newConsigneeContactName() {
    if ($('#newConsigneeContact').is(":checked")) {
        $('#consigneeManual').show();
        $('#consigneeDojo').hide();
        clearBkgContactConsignee();
    } else {
        $('#consigneeManual').hide();
        $('#consigneeDojo').show();
        clearBkgContactConsignee();
    }
}
function clearBkgContactConsignee() {
    $('#consigneeContactName').val('');
    $('#consigneeManualContact').val('');
    $('#consigneeEmailClient').val('');
    $('#dupConsPhone').val('');
    $('#dupConsFax').val('');
}
function clearBkgContactForwarder() {
    $('#forwarederContactManual').val('');
    $('#forwardercontactClient').val('');
    $('#forwarderEmailClient').val('');
    $('#dupFwdPhone').val('');
    $('#dupFwdFax').val('');
}
function newForwarderContactName() {
    if ($('#newForwarderContact').is(":checked")) {
        $('#forwarederManual').show();
        $('#forwarederDojo').hide();
        clearBkgContactForwarder();
    } else {
        $('#forwarederManual').hide();
        $('#forwarederDojo').show();
        clearBkgContactForwarder();
    }
}
function clearBkgContactClient() {
    $('#clientContactManul').val('');
    $('#contactName').val('');
    $('#dupFax').val('');
    $('#dupPhone').val('');
    $('#email').val('');
}

function newClientContactName() {
    if ($('#newClientContact').is(":checked")) {
        $('#clientContactManual').show();
        $('#clientContactDojo').hide();
        clearBkgContactClient();
    } else {
        $('#clientContactManual').hide();
        $('#clientContactDojo').show();
        clearBkgContactClient();
    }
}
function cfcl_AccttypeCheck() {
    if ($('#disabledAccount').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#forwardAccount').val() + "</span>");
        $('#cfclAccount').val('');
        $('#cfclAccountNo').val('');
    }
}
function setRelayOverRide() {
    if ($("#isRelay").val() === 'true') {
        $("#qutRelayOverYes").attr("checked", true);
        changeQtRelay();
    } else {
        $("#qutRelayOverNo").attr("checked", true);
        changeQtRelay();
    }
}
function quotedestinationRemarks() {
    var podUnlocationcode = document.getElementById('podUnlocationcode').value;
    var fdUnlocationcode = document.getElementById('unlocationCode').value;
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
            }
            else {
                $("#portGriRemarks").val('');
                $("#griRemarksFd").html('');
            }
            if (data[1] !== undefined && data[1] !== "" && data[1] !== null) {
                $.prompt("<span style='color:red;font-weight:bold;'>INTERNAL REMARKS FOR " + destination + "</span>" + "\n" + "</br>" + data[1]);
            }
        }
    });
}
function changerelay() {
    var relay = $('input:radio[name=relayOverride]:checked').val();
    if (relay !== undefined && relay === "Y") {
        $('#portOfLoading').removeClass().addClass("text");
        $('#portOfDestination').removeClass().addClass("text");
        $('#portOfLoading').removeAttr("readonly");
        $('#portOfDestination').removeAttr("readonly");
        $('#showFullRelay').removeAttr('disabled');
        $('#showFullRelayFd').removeAttr('disabled');
    }
    if (relay !== undefined && relay === "N") {
        $('#portOfLoading').addClass("text-readonly");
        $('#portOfDestination').addClass("text-readonly");
        $('#portOfLoading').attr("readonly", true);
        $('#portOfDestination').attr("readonly", true);
        $('input[name=showFullRelay]').attr('checked', false);
        $('input[name=showFullRelayFd]').attr('checked', false);
        $('#showFullRelay').attr('disabled', true);
        $('#showFullRelayFd').attr('disabled', true);
    }
}
function changeQtRelay() {
    var relay = $('input:radio[name=relayOverride]:checked').val();
    if (relay != undefined && relay == "Y") {
        $('#portOfLoading').removeClass().addClass("text");
        $('#portOfDestination').removeClass().addClass("text");
        $('#portOfLoading').removeAttr("readonly");
        $('#portOfDestination').removeAttr("readonly");
        $('#showFullRelay').removeAttr('disabled');
        $('#showFullRelayFd').removeAttr('disabled');
        showHideRelayFd();
        showRelayFdForRelay();
    }
    if (relay != undefined && relay == "N") {
        $('#portOfLoading').addClass("text-readonly");
        $('#portOfDestination').addClass("text-readonly");
        $('#portOfLoading').attr("readonly", true);
        $('#portOfDestination').attr("readonly", true);
        $('input[name=showFullRelay]').attr('checked', false);
        $('input[name=showFullRelayFd]').attr('checked', false);
        $('#showFullRelay').attr('disabled', true);
        $('#showFullRelayFd').attr('disabled', true);
        showHideRelayFd();
        showRelayFdForRelay();
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
            }
            else {
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
    else {
        if (nonRated == 'N') {
            if ($('#basedOnCity').is(":checked")) {
                $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
            }
            else {
                $('#finalDestinationR').attr('alt', 'CONCAT_COUNTRY_NAME');
            }
        }
        else {
            if ($('#basedOnCity').is(":checked")) {
                if ($('#showFullRelayFd').is(":checked")) {
                    $('#finalDestinationR').attr('alt', 'CONCAT_PORT_NAME');
                } else {
                    $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
                }
            }
            else {
                if ($('#showFullRelayFd').is(":checked")) {
                    $('#finalDestinationR').attr('alt', 'CONCAT_CITY_NAME');
                } else {
                    $('#finalDestinationR').attr('alt', 'CONCAT_COUNTRY_NAME');
                }
            }
        }
    }
}
function showHideRelayFd() {
    var destination = $('#finalDestinationR').val();
    var relay = $('input:radio[name=relayOverride]:checked').val();
    var nonRated = $('input:radio[name=nonRated]:checked').val();
    if (relay == 'Y') {
        if ($('#showFullRelay').is(":checked")) {
            $('#portOfOriginR').attr('alt', 'PORTNAME');
        }
        else {
            $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
        }
    }
    else {
        if (nonRated == 'N') {
            if ($('#showFullRelay').is(":checked")) {
                $('#portOfOriginR').attr('alt', 'PORTNAME');
            }
            else {
                $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
            }
        }
        else {
            if ($('#showFullRelay').is(":checked")) {
                $('#portOfOriginR').attr('alt', 'PORTNAME');
            }
            else {
                $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
            }
        }
    }
}
function fillDeliveryMetro() {
    var unlocationCode = $('#unlocationCode').val().toUpperCase();
    if (unlocationCode === 'PRSJU') {
        $('#deliveryMetroI').attr('checked', true);
        $('#deliveryMetroI').attr('disabled', false);
        $('#deliveryMetroN').attr('disabled', false);
        $('#deliveryMetroO').attr('disabled', false);
    }
    else {
        $('#deliveryMetroN').attr('checked', true);
        $('#deliveryMetroI').attr('disabled', true);
        $('#deliveryMetroO').attr('disabled', true);
    }
}
function podDestination() {
    fillPolPod();
    setDefaultAgentSetUp();
}
function loadingVoyage() {
    submitAjaxFormForVoyage('displayVoyage', '#lclQuoteForm', '#upcomingSailing', 'relay');
    fillPolPod();
}
function clearColoadRates(fileId) {
    var ratetype = $('input:radio[name=rateType]:checked').val();
    var filenumberId = $('#fileNumberId').val();
    if (filenumberId == "") {
        if (ratetype == "R") {
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "deleteColoadRates",
                    param1: filenumberId,
                    request: true,
                    dataType: "json"
                },
                async: false,
                success: function (data) {
                    if (data == true) {
                        showProgressBar();
                        for (var i = document.getElementById("chargesTable").rows.length; i > 1; i--) {
                            document.getElementById("chargesTable").deleteRow(i - 1);
                        }
                        for (var j = document.getElementById("commObj").rows.length; j >= 1; j--) {
                            document.getElementById("commObj").deleteRow(j - 1);
                        }
                        $("#totalchargestd").text("0");
                        hideProgressBar();
                    }
                }
            });
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
function destinationServices(path, fileNumberId, fileNumber, fileNumberStatus, buttonValue) {
    var fd = $('#unlocationCode').val();
    var href = path + "/lclQuoteCostAndCharge.do?methodName=displayQuoteDestinationServices&buttonValue="
            + buttonValue + "&fileNumberId=" + fileNumberId + "&destination=" + fd + "&fileNumber="
            + fileNumber + "&fileNumberStatus=" + fileNumberStatus + "&manualEntry=true";
    $("#destinationServices").attr("href", href);
    $("#destinationServices").colorbox({
        iframe: true,
        width: "70%",
        height: "80%",
        title: "Destination Services",
        onClosed: function () {
            showLoading();
            $("#methodName").val("refreshQuoteRates");
            var params = $("#lclQuoteForm").serialize();
            $.post($("#lclQuoteForm").attr("action"), params, function (data) {
                $("#chargeDesc").html(data);
                $("#chargeDesc", window.parent.document).html(data);
                closePreloading();
            });
        }
    });
}

function editQuotedestinationServices(path, chargeId, fileNumberId, fileNumber, isManual, buttonValue) {//Destination services
    var fd = $('#unlocationCode').val();
    var href = path + "/lclQuoteCostAndCharge.do?methodName=displayQuoteDestinationServices&buttonValue=" + buttonValue
            + "&fileNumberId=" + fileNumberId + "&destination=" + fd + "&fileNumber=" + fileNumber
            + "&manualEntry=" + isManual + "&id=" + chargeId;
    $(".quoteCostAndCharge").attr("href", href);
    $(".quoteCostAndCharge").colorbox({
        iframe: true,
        href: href,
        width: "70%",
        height: "80%",
        title: "Destination Services",
        onClosed: function () {
            showLoading();
            $("#methodName").val("refreshQuoteRates");
            var params = $("#lclQuoteForm").serialize();
            $.post($("#lclQuoteForm").attr("action"), params, function (data) {
                $("#chargeDesc").html(data);
                $("#chargeDesc", window.parent.document).html(data);
                closePreloading();
            });
        }
    });
}

function setPoaDetails() {
    setPoaValues("shipperPoa", "shipperPoaClientId", "shipperPoaId");
    setPoaValues("clientpoa", "clientPoa", "clientPoa");
    setPoaValues("consigneePoa", "consigneePoaClientId", "consigneePoaId");
    setPoaValues("forwarderPoa", "forwarderpoaClient", "forwarder");
}
function setPoaValues(poaValueId, setPoaClientId, setPoaId) {
    var poa = $("#" + poaValueId).val();
    if (poa != null && poa != '') {
        if (poa === 'Y') {
            $('#' + setPoaClientId).text('YES')
            $('#' + setPoaClientId).addClass('green');
            $('#' + setPoaId).text('YES')
            $('#' + setPoaId).addClass('green');
        } else {
            $('#' + setPoaClientId).text('NO')
            $('#' + setPoaClientId).addClass('red');
            $('#' + setPoaId).text('NO')
            $('#' + setPoaId).addClass('red');
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
function clearAllRates(txt, fileId) {
    var hiddenRateType = $('#rateTypes').val();
    var filenumberId = $('#fileNumberId').val();
    if (filenumberId !== "") {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkQuoteRates",
                param1: filenumberId,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data) {
                    $.prompt(txt, {
                        buttons: {
                            Yes: 1,
                            No: 2
                        },
                        submit: function (v) {
                            if (v == 1) {
                                showProgressBar();
                                $.ajaxx({
                                    dataType: "json",
                                    data: {
                                        className: "com.gp.cong.lcl.dwr.LclDwr",
                                        methodName: "deleteQuoteRates",
                                        param1: fileId,
                                        param2: $('#loginUserId').val(),
                                        dataType: "json"
                                    },
                                    success: function (data) {
                                        var ratetype = $('input:radio[name=rateType]:checked').val();
                                        $('#rateTypes').val(ratetype);
                                        var rowCount = $("#chargesTable tbody tr").length;
                                        if (data[0] === 'true') {
                                            for (var i = rowCount; i > 0; i--)
                                            {
                                                var value = $("#chargesTable").find('tr:nth-child(' + i + ') td:nth-child(2)').html();
                                                if (trim(value) === '' || trim(value) === null) {
                                                    document.getElementById("chargesTable").deleteRow(i);
                                                }
                                            }
                                            if (data[1] !== undefined && data[1] !== "" && data[1] !== null) {
                                                document.getElementById('totalchargestd').innerHTML = data[1];
                                            }
                                        }
                                    }
                                });
                                hideProgressBar();
                                $.prompt.close();
                            }
                            else if (v == 2) {
                                $('#rateTypes').val(hiddenRateType);
                                if (hiddenRateType == "C") {
                                    $('#ctc').attr('checked', true);
                                    fillaesbyQtRateType();
                                }
                                else if (hiddenRateType == "R") {
                                    $('#rateR').attr('checked', true);
                                    fillaesbyQtRateType();
                                }
                                else if (hiddenRateType == "F") {
                                    $('#rateF').attr('checked', true);
                                    fillaesbyQtRateType();
                                }
                                $.prompt.close();
                            }
                        }
                    });
                }
            }
        });
    }
}
//KeyUp
$(document).ready(function () {
    $('#shipperNameClient').keyup(function () {
        var shipper = $('#shipperNameClient').val();
        if (shipper == "") {
            clearShipperValues();
            clearShipperValuesForClient();
        }
    });
    $('#shipperName').keyup(function () {
        var shipper = $('#shipperName').val();
        if (shipper == "") {
            clearShipperValues();
            clearShipperValuesForClient();
        }
    });
    $('#dupShipName').keyup(function () {
        var shipperNew = $('#dupShipName').val();
        if (shipperNew == "") {
            clearShipperValues();
            clearShipperValuesForClient();
        }
    });
    $('#forwarderNameClient').keyup(function () {
        var forwarder = $('#forwarderNameClient').val();
        if (forwarder == "") {
            clearForwarderTextValue();
            clearPartiesForwarder();
        }
    });
    $('#forwarderName').keyup(function () {
        var Forwarder = $('#forwarderName').val();
        if (Forwarder == "") {
            clearForwarderTextValue();
            clearPartiesForwarder();
        }
    });
    $('#dupConsName').keyup(function () {
        var consigneeNew = $('#dupConsName').val();
        if (consigneeNew == "") {
            clearConsigneeValues();
            clearConsigneeClient();
        }
    });
    $('#consigneeName').keyup(function () {
        var consignee = $('#consigneeName').val();
        if (consignee === "") {
            clearConsigneeValues();
            clearConsigneeClient();
        }
    });
    $('#consigneeNameClient').keyup(function () {
        var consignee = $('#consigneeNameClient').val();
        if (consignee === "") {
            clearConsigneeValues();
            clearConsigneeClient();
        }
    });
    $('#portOfOriginR').keyup(function () {
        var portOfOriginId = $('#portOfOriginR').val();
        if (portOfOriginId == "") {
            $('#portOfLoading').val('');
            $('#portOfLoadingId').val('');
            $('#portOfDestination').val('');
            $('#portOfDestinationId').val('');
            $('#portOfOriginId').val('');
            $('#agentName').val('');
            $('#agentNumber').val('');
            $('#agentBrand').val('');
            submitAjaxFormforRates('displayemptyVoyage', '#lclQuoteForm', '#upcomingSailing', '');
            showHideRelayFd();
            showRelayFdForRelay();
        }
    });
    $('#portOfDestination').keyup(function () {
        var pod = $('#portOfDestination').val();
        if (pod == "") {
            //clearBookedForVoyage();
            submitAjaxFormforRates('displayemptyVoyage', '#lclQuoteForm', '#upcomingSailing', '');
        }
    });
    $('#finalDestinationR').keyup(function () {
        var destination = $('#finalDestinationR').val();
        if (destination == "") {
            $('#portOfLoading').val('');
            $('#portOfLoadingId').val('');
            $('#portOfDestination').val('');
            $('#portOfDestinationId').val('');
            $('#finalDestinationId').val('');
            $('#agentName').val('');
            $('#agentNumber').val('');
            $('#agentBrand').val('');
            submitAjaxFormforRates('displayemptyVoyage', '#lclQuoteForm', '#upcomingSailing', '');
            showRelayFdForRelay();
            showHideRelayFd();
        }
    });
    $('#consigneeContactName').keyup(function () {
        if ($('#consigneeContactName').val() == "") {
            clearBkgContactConsignee();
        }
    });

    $('#consigneeManualContact').keyup(function () {
        if ($('#consigneeManualContact').val() == "") {
            clearBkgContactConsignee();
        }
    });

    $('#forwardercontactClient').keyup(function () {
        if ($('#forwardercontactClient').val() == "") {
            clearBkgContactForwarder();
        }
    });

    $('#forwarederContactManual').keyup(function () {
        if ($('#forwarederContactManual').val() == "") {
            clearBkgContactForwarder();
        }
    });
    $('#contactName').keyup(function () {
        if ($('#contactName').val() == "") {
            clearBkgContactClient();
        }
    });
    $('#clientContactManul').keyup(function () {
        if ($('#clientContactManul').val() == "") {
            clearBkgContactClient();
        }
    });
    $('#shipperContactClient').keyup(function () {
        if ($('#shipperContactClient').val() == "") {
            clearBkgContactShipper();
        }
    });
    $('#shipperManualContact').keyup(function () {
        if ($('#shipperManualContact').val() == "") {
            clearBkgContactShipper();
        }
    });
});

//creating a new manual shipper for Client section in Exports
function newShippName() {
    if ($('#newShipp').is(":checked")) {
        $("#dojoShipper").hide();
        $("#manualShipper").show();
        $("#manualShipp").show();
        $("#dojoShipp").hide();
        clearShipperValuesForClient();
        clearShipperValues();
        $('#newShipper').attr('checked', true);
    } else {
        $("#dojoShipper").show();
        $("#manualShipper").hide();
        $("#dojoShipp").show();
        $("#manualShipp").hide();
        clearShipperValuesForClient();
        clearShipperValues();
        $('#dupShipName').val('');
        $('#dupShipperName').val('');
        $('#newShipper').attr('checked', false);
    }
}
function newShippChange() {
    $('#dupShipperName').val($('#dupShipName').val());
}
function shipperAcctType() {
    var acctType = jQuery("#shipper_acct_type").val();
    if ($('#shipperDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#shipDisableAcct').val() + "</span>");
        clearShipperValuesForClient();
        clearShipperValues();
    } else {
        if (acctType !== "" && acctType.indexOf("S") === -1) {
            $.prompt('Please select the customers with account type S,E,I and V with subtype forwarder');
            clearShipperValues();
            clearShipperValuesForClient();
        }
        else {
            var shipAcctNo = $('#shipperCodeClient').val();
            if (shipAcctNo === "") {
                shipAcctNo = $('#shipperCode').val();
            }
            notesCount(path, 'shpNotes', shipAcctNo, 'S');
            setCreditStatus("shipperCreditClient", "shipperCredit");
            setPoaValues("shipperPoa", "shipperPoaClientId", "shipperPoaId");
        }
    }
}
function changeShipperValuesToClient() {
    $('#shipperNameClient').val($('#shipperName').val());
    $('#shipperCodeClient').val($('#shipperCode').val());
    $('#shipperFaxClient').val($('#shipperFax').val());
    $('#shipperCityClient').val($('#shipperCity').val());
    $('#shipperCountryClient').val($('#shipperCountry').val());
    $('#shipperStateClient').val($('#shipperState').val());
    $('#shipperZipClient').val($('#shipperZip').val());
    $('#shipperAddressClient').val($('#shipperAddress').val().replace(/\,/g, " "));
    $('#shipperEmailClient').val($('#shipperEmail').val());
    $('#shipperPhoneClient').val($('#shipperPhone').val());
    displayShipperDetails();
}
function changeShipperDetailsToParties() {
    $('#shipperName').val($('#shipperNameClient').val());
    $('#shipperCode').val($('#shipperCodeClient').val());
    $('#shipperAddress').val($('#shipperAddressClient').val().replace(/\,/g, " "));
    $('#shipperCity').val($('#shipperCityClient').val());
    $('#shipperCountry').val($('#shipperCountryClient').val());
    $('#shipperState').val($('#shipperStateClient').val());
    $('#shipperZip').val($('#shipperZipClient').val());
    $('#shipperFax').val($('#shipperFaxClient').val());
    $('#shipperPhone').val($('#shipperPhoneClient').val());
    $('#shipperEmail').val($('#shipperEmailClients').val());
    displayShipperDetails();
}
function clearShipperValuesForClient() {
    $('#shipperNameClient').val('');
    $('#shipperCodeClient').val('');
    $('#shipperAddressClient').val('');
    $('#shipperStateClient').val('');
    $('#shipperCityClient').val('');
    $('#shipperCountryClient').val('');
    $('#shipperZipClient').val('');
    $('#sFmc').val('');
    $('#sotiNumber').val('');
    $('#shipperPoa').val('');
    $('#shipperFaxClient').val('');
    $('#shipperPhoneClient').val('');
    $('#shipColoadNo').val('');
    $('#shipColoadDesc').val('');
    $('#shipRetailNo').val('');
    $('#shipRetailDesc').val('');
    $('#shipperCreditClient').text('');
    $('#shipperPoaClientId').text('');
    $('#shipBsEciAcctNo').val('');
    $('#shipBsEciFwNo').val('');
    $('#newShipperContact').attr('checked', false);
    $('#shpNotes').attr("src", path + "/img/icons/e_contents_view.gif");
    displayShipperDetails();
}
function setShipperManualCheckBox() {
    var acctno = $('#shipperCodeClient').val();
    var newClient = $('#dupShipName').val();
    if (newClient !== '' && acctno === '') {
        $('#newShipper').attr("checked", true);
        $('#newShipp').attr('checked', true);
        $('#dojoShipp').hide();
        $('#manualShipp').show();
        $('#dojoShipper').hide();
        $('#manualShipper').show();
        $('#shipperDojo').hide();
        $('#shippManual').show();
    } else {
        $('#dupShipName').val('')
        $('#dupShipperName').val('')
        $('#newShipper').attr("checked", false);
        $('#newShipp').attr('checked', false);
        $('#dojoShipper').show();
        $('#manualShipper').hide();
        $('#shipperDojo').show();
        $('#shippManual').hide();
        $('#dojoShipp').show();
        $('#manualShipp').hide();
    }
}
//Forwarder
function forwarder_AccttypeCheck() {
    target = jQuery("#forwarder_acct_type").val();
    sub_type = jQuery("#forwarder_sub_type").val();
    var subTypes;
    var array1 = new Array();
    var array2 = new Array();
    if ($('#forwardDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#forwardDisabledAcct').val() + "</span>");
        clearForwarderTextValue();
        clearPartiesForwarder();
    }
    if (target != null) {
        array1 = target.split(",");
    }
    if (sub_type != null) {
        subTypes = (sub_type).toLowerCase();
        array2 = subTypes.split(",");
    }
    if (!array1.contains("V") || (array1.contains("V") && !array2.contains('forwarder'))) {
        $.prompt("Please select the customers with account type V with subtype forwarder");
        clearForwarderTextValue();
        clearPartiesForwarder();
    } else {
        var forwardeAcctNo = $('#forwarderCodeClient').val();
        if (forwardeAcctNo === "") {
            forwardeAcctNo = $('#forwarderCode').val()
        }
        displayForwarderDetails();
        notesCount(path, 'fwdNotes', forwardeAcctNo, 'V');
        setPoaValues("forwarderPoa", "forwarderpoaClient", "forwarder");
        setCreditStatus("forwarderCreditClient", "forwarderCredit");
    }
}
function clearForwarderTextValue() {
    $('#forwardercontactClient').val('');
    $('#forwarderEmailClient').val('');
    $('#forwarderNameClient').val('');
    $('#forwarderCodeClient').val('');
    $('#forwarder_acct_type').val('');
    $('#forwarder_sub_type').val('');
    $('#forwarderCredit').val('');
    $('#forwarderCreditClient').text('');
    $('#forwarderPoa').val('');
    $('#forwarderpoaClient').text('');
    $('#forwarderAddressClient').val('');
    $('#forwarderAddressClient').val('');
    $('#forwarderZipClient').val('');
    $('#forwarderStateClient').val('');
    $('#forwarderCityClient').val('');
    $('#forwarderCountryClient').val('');
    $('#forwarderFaxClient').val('');
    $('#forwarderPhoneClient').val('');
    $('#fFmc').val('');
    $('#fotiNumber').val('');
    $('#frwdSearchState').val('');
    $('#frwdSearchZip').val('');
    $('#frwdSearchCountryUnLocCode').val('');
    $('#frwdSearchSalesCode').val('');
    $('#frwdSearchCountry').val('');
    $('#fwdColoadNo').val('');
    $('#fwdColoadDesc').val('');
    $('#fwdRetailNo').val('');
    $('#fwdRetailDesc').val('');
    $('#fwdBsEciFwNo').val('');
    $('#fwdBsEciAcctNo').val('');
    $('#newForwarderContact').attr('checked', false);
    $('#fwdNotes').attr("src", path + "/img/icons/e_contents_view.gif");
    displayForwarderDetails();
}
function clearPartiesForwarder() {
    $('#forwarderName').val('');
    $('#forwarderCode').val('');
    $('#forwarder_acct_type').val('');
    $('#forwarder_sub_type').val('');
    $('#forwarderCredit').val('');
    $('#forwarderCity').val('');
    $('#forwarderCountry').val('');
    $('#forwarderAddress').val('');
    $('#forwarderState').val('');
    $('#forwarderZip').val('');
    $('#forwarderFax').val('');
    $('#forwarderPhone').val('');
    $('#forwarderEmail').val('');
    $('#forwarder').text('');
    $('#fwdNotes').attr("src", path + "/img/icons/e_contents_view.gif");
    displayForwarderDetails();
}
function copyForwarderDetailstoClient() {
    $('#forwarderNameClient').val($('#forwarderName').val());
    $('#forwarderCodeClient').val($('#forwarderCode').val());
    $('#forwarderCityClient').val($('#forwarderCity').val());
    $('#forwarderCountryClient').val($('#forwarderCountry').val());
    $('#forwarderStateClient').val($('#forwarderState').val());
    $('#forwarderZipClient').val($('#forwarderZip').val());
    $('#forwarderAddressClient').val($('#forwarderAddress').val().replace(/\,/g, " "));
    $('#forwarderPhoneClient').val($('#forwarderPhone').val());
    $('#forwarderFaxClient').val($('#forwarderFax').val());
    displayForwarderDetails();
}
function copyForwarderToParties() {
    $('#forwarderName').val($('#forwarderNameClient').val());
    $('#forwarderCode').val($('#forwarderCodeClient').val());
    $('#forwarderFax').val($('#forwarderFaxClient').val());
    $('#forwarderCity').val($('#forwarderCityClient').val());
    $('#forwarderCountry').val($('#forwarderCountryClient').val());
    $('#forwarderState').val($('#forwarderStateClient').val());
    $('#forwarderZip').val($('#forwarderZipClient').val());
    $('#forwarderAddress').val($('#forwarderAddressClient').val().replace(/\,/g, " "));
    $('#forwarderPhone').val($('#forwarderPhoneClient').val());
    $('#forwarderEmail').val($('#forwarderEmailClient').val());
    displayForwarderDetails();
}
//Consignee
function consigneeAccttype() {
    var acctType = jQuery("#consignee_acct_type").val();
    if ($('#consDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#consDisableAcct').val() + "</span>");
        clearConsigneeValues();
        clearConsigneeClient();
    } else {
        if (acctType !== "" && acctType.indexOf("C") === -1) {
            $.prompt('Please select the customers with account type C');
            clearConsigneeValues();
            clearConsigneeClient();
        } else {
            displayConsigneeDetails();
            var consigneeAcctNo = $('#consigneeCodeClient').val();
            if (consigneeAcctNo === "") {
                consigneeAcctNo = $('#consigneeCode').val();
            }
            notesCount(path, 'conNotes', consigneeAcctNo, 'C');
            setCreditStatus("consigneeCreditClient", "consigneeCredit");
            setPoaValues("consigneePoa", "consigneePoaClientId", "consigneePoaId");
        }
    }

}
//Consignee
function clearConsigneeClient() {
    $('#consigneeNameClient').val('');
    $('#consigneeCodeClient').val('');
    $('#consigneeAddressClient').val('');
    $('#consigneeCityClient').val('');
    $('#consigneeCountryClient').val('');
    $('#consigneeStateClient').val('');
    $('#consigneeZipClient').val('');
    $('#cFmc').val('');
    $('#cotiNumber').val('');
    $('#consColoadNo').val('');
    $('#consColoadDesc').val('');
    $('#consRetailNo').val('');
    $('#consRetailDesc').val('');
    $('#consigneeFaxClient').val('');
    $('#consigneePhoneClient').val('');
    $('#consigneePoa').val('');
    $('#consigneePoaClientId').text('');
    $('#consigneeCredit').val('');
    $('#consBsEciAcctNo').val('');
    $('#consBsEciFwNo').val('');
    $('#consigneeCreditClient').text('');
    displayConsigneeDetails();
}
function copyConsigneeToParties() {
    $('#consigneeName').val($('#consigneeNameClient').val());
    $('#consigneeCode').val($('#consigneeCodeClient').val());
    $('#consigneeAddress').val($('#consigneeAddressClient').val().replace(/\,/g, " "));
    $('#consigneeCity').val($('#consigneeCityClient').val());
    $('#consigneeCountry').val($('#consigneeCountryClient').val());
    $('#consigneeState').val($('#consigneeStateClient').val());
    $('#consigneeZip').val($('#consigneeZipClient').val());
    $('#consigneeEmail').val($('#consigneeEmailClient').val());
    $('#consigneePhone').val($('#consigneePhoneClient').val());
    $('#consigneeFax').val($('#consigneeFaxClient').val());
    displayConsigneeDetails();
}
function copyConsigneeToClientSection() {
    $('#consigneeNameClient').val($('#consigneeName').val());
    $('#consigneeCodeClient').val($('#consigneeCode').val());
    $('#consigneeAddressClient').val($('#consigneeAddress').val().replace(/\,/g, " "));
    $('#consigneeCityClient').val($('#consigneeCity').val());
    $('#consigneeStateClient').val($('#consigneeState').val());
    $('#consigneeCountryClient').val($('#consigneeCountry').val());
    $('#consigneeZipClient').val($('#consigneeZip').val());
    $('#consigneePhoneClient').val($('#consigneePhone').val());
    $('#consigneeFaxClient').val($('#consigneeFax').val());
    displayConsigneeDetails();
}
function newConsignChange() {
    $("#dupConsigneeName").val($("#dupConsName").val());
}
function newConsigneeNameByClient() {
    if ($('#newConsign').is(":checked")) {
        $("#dojoConsignee").hide();
        $("#manualConsignee").show();
        $("#manualConsign").show();
        $("#dojoConsign").hide();
        clearConsigneeClient();
        clearConsigneeValues();
        $('#newConsignee').attr('checked', true);
    } else {
        $("#dojoConsignee").show();
        $("#manualConsignee").hide();
        $("#manualConsign").hide();
        $("#dojoConsign").show();
        $('#newConsignee').attr('checked', false);
        $('#dupConsName').val('');
        $('#dupConsigneeName').val('');
        clearConsigneeClient();
        clearConsigneeValues();
    }
}

function setConsManualCheckBox() {
    var acctno = $('#consigneeCodeClient').val();
    var newClient = $('#dupConsName').val();
    if (newClient != '' && acctno === '') {
        $('#newConsignee').attr("checked", true);
        $('#dojoConsignee').hide();
        $('#manualConsignee').show();
        $('#consigneeDojo').hide();
        $('#consigneeManual').show();
        $('#dojoConsign').hide();
        $('#manualConsign').show();
        $('#newConsign').attr('checked', true);
    } else {
        $('#newConsign').attr('checked', false);
        $('#newConsignee').attr("checked", false);
        $('#dojoConsignee').show();
        $('#manualConsignee').hide();
        $('#consigneeDojo').show();
        $('#consigneeManual').hide();
        $('#dojoConsign').show();
        $('#manualConsign').hide();
    }
}
function  fillPolPod() {
    $('#pooSailing').text($('#portOfOriginR').val());
    $('#polSailing').text($('#portOfLoading').val());
    $('#podSailing').text($('#portOfDestination').val());
    $('#fdSailing').text($('#finalDestinationR').val());
}
function setRelayDetails(textFieldValues) {
    var relayOverride = $('input:radio[name=relayOverride]:checked').val();
    if (relayOverride == 'N') {
        displayUpcomingSailings(textFieldValues);
    } else if (relayOverride === "Y" && textFieldValues === "relayOverride") {
        upcomingSailingsByRelayYes();
    }
    quotedestinationRemarks();
    if (textFieldValues === 'Des') {
        setDefaultAgentSetUp();
    }
}
function displayUpcomingSailings(textFieldValues) {
    if (textFieldValues === "Origin") {
        showRelayFdForRelay();
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
        var params = $('#lclQuoteForm').serialize();
        $.ajax({
            type: "POST",
            url: $('#lclQuoteForm').attr("action"),
            data: params,
            async: false,
            success: function (data) {
                $('#polPod').html(data.polPod);
                $('#upcomingSailing').html(data.upcomingSailings);
            }
        });

        showRelayFdForRelay();
        fillPolPod();
        setRelayOverRide();
        fillDeliveryMetro();
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
        var params = $('#lclQuoteForm').serialize();
        $.ajax({
            type: "POST",
            url: $('#lclQuoteForm').attr("action"),
            data: params,
            preloading: true,
            async: false,
            success: function (data) {
                $('#upcomingSailing').html(data.upcomingSailings);
            }
        });
    }
}


function setRelay() {
    if ($("#portOfOriginR").val() !== "" && $("#finalDestinationR").val() !== "") {
        if ($("#showOlder").prop("checked"))
        {
            $("#methodName").val('getRelayAndUpComingSailingsPrevious');
        } else {
            $("#previousSailing").val($("#showOlder").prop("checked"));
            $("#methodName").val('getRelayAndUpComingSailings');
        }
        var params = $('#lclQuoteForm').serialize();
        $.ajax({
            type: "POST",
            url: $('#lclQuoteForm').attr("action"),
            data: params,
            async: false,
            success: function (data) {
                $('#polPod').html(data.polPod);
                $('#upcomingSailing').html(data.upcomingSailings);
            }
        });
        fillPolPod();
        fillDeliveryMetro();
        showHideRelayFd();
    }
}
function setRelayOverRideYes() {
    changeQtRelay();
}

function upcomingSailingsByRelayYesPrevious() {
    if ($("#portOfDestinationId").val() !== "" && $('#portOfLoadingId').val() !== "" &&
            $('#portOfDestination').val() != "" &&
            $("#finalDestinationR").val() !== "") {
        $("#methodName").val('getRelayOverrideUpcomingPrevious');
        $("#previousSailing").val($("#showOlder").prop("checked"));
        var params = $("#lclQuoteForm").serialize();
        params += "&podId=" + $('#portOfDestinationId').val() + "&polId=" + $('#portOfLoadingId').val();
        $.post($("#lclQuoteForm").attr("action"), params, function (data) {
            $('#upcomingSailing').html(data);
        });
    }
}
function upcomingSailingsByRelayYes() {
    if ($('#portOfLoading').val() !== "" && $('#portOfDestination').val() != "") {
        if ($("#showOlder").prop("checked"))
        {
            $("#methodName").val('getRelayOverrideUpcomingPrevious');
        } else {
            $("#previousSailing").val($("#showOlder").prop("checked"));
            $("#methodName").val('getRelayOverrideUpcoming');
        }
        var params = $("#lclQuoteForm").serialize();
        params += "&podId=" + $('#portOfDestinationId').val() + "&polId=" + $('#portOfLoadingId').val();
        $.post($("#lclQuoteForm").attr("action"), params, function (data) {
            $('#upcomingSailing').html(data);
        });
        fillPolPod();
    }
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
                var params = $("#lclQuoteForm").serialize();
                params += "&commNo=" + commNo;
                $.post($("#lclQuoteForm").attr("action"), params, function (data) {
                    if (data) {
                        $('#commodityDesc').html(data.commodityQuoteDesc);
                        $('#chargeDesc').html(data.quoteChargeDesc);
                        hideProgressBar();
                    }
                });
            } else {
                $.prompt.close();
            }
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
                showProgressBar();
                parent.$.prompt.close();
                var spotRate = $('input:radio[name=spotRate]').val();
                var originCode = getUnlocationCode('portOfOriginR');
                var pol = getUnlocationCode('portOfLoading');
                var pod = getUnlocationCode('portOfDestination');
                var destinationCode = getUnlocationCode('finalDestinationR');
                var pickUpValues = $('#doorOriginCityZip').val();
                $("#methodName").val('recaculateBySpotRate');
                var params = $('#lclQuoteForm').serialize();
                params += "&originCode=" + originCode + "&destinationCode=" + destinationCode + "&spotRate=" + spotRate
                        + "&polUnlocationcode=" + pol + "&podUnlocationcode=" + pod + "&doorOriginCityZip=" + pickUpValues;
                $.post($('#lclQuoteForm').attr("action"), params, function (data) {
                    $('#chargeDesc').html(data.chargeDesc);
                    $('#commodityDesc').html(data.commodityDesc);
                    $('#lclSpotRate').removeClass("green-background");
                    $('#lclSpotRate').addClass("button-style1");
                    $("#lclSpotRate").hide();
                    $("#spotratelabel").text('');
                    $('#existSpotRate').val("false");
                    $('#externalComment').val('');
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
    if (spot && file_Id !== "")
    {
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
    }
    else {
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
                if ($("#trmnum").val() !== '59') {
                    if (data === 'B') {
                        $("input:radio[name=businessUnit]").val([$("#econo").val()]);
                        id = "econo";
                    } else {
                        $("input:radio[name=businessUnit]").val([$("#eculine").val()]);
                        id = "eculine";
                    }
                    if (data !== null && $('#fileNumberId').val() !== '') {
                        updateEconoOrEculine($("#" + id).val(), $('#fileNumberId').val(), "", "QT-AutoNotes");
                    }
                }
            }
        });
    }
}

function confirmBussinessUnitInQuote(ele, id) {
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
                        updateEconoOrEculine($("#" + ele.id).val(), $('#fileNumberId').val(), $("#loginUserId").val(), "QT-AutoNotes");
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

//  commented due to mantis item 10981
//function saveHotCode(businessUnit) {
//    showProgressBar();
//    $('#methodName').val('addLcl3pReference');
//    $("#hotCodes").val("EBL/ECULINE BILL OF LADING");
//    var params = $("#lclQuoteForm").serialize();
//    params += "&refValue=EBL/ECULINE BILL OF LADING&refTypeFlag=hotCodes&fileNumberId=" + $('#fileNumberId').val()
//            + "&brandPreferences=None";
//    $.post($("#lclQuoteForm").attr("action"), params, function (data) {
//        $("#hotCodesList").html(data);
//        $("#hotCodesList", window.parent.document).html(data);
//        $("#hotCodes").val("");
//        hideProgressBar();
//    });
//}
//
//function deleteEBLHotCode(ele) {
//    showProgressBar();
//    var refId = '';
//    $("." + ele).each(function () {
//        var code = $(this).val();
//        if ("EBL" === code.substring(code.indexOf(",") + 1, code.indexOf("/"))) {
//            refId = code.substring(0, code.indexOf(","));
//            $('#methodName').val('deleteLcl3pReference');
//            $("#hotCodes").val("EBL/ECULINE BILL OF LADING");
//            var params = $("#lclQuoteForm").serialize();
//            params += "&noteId=hotCodes&fileNumberId=" + $('#fileNumberId').val() + "&lcl3pRefId=" + refId
//                    + "&brandPreferences=None";
//            $.post($("#lclQuoteForm").attr("action"), params, function (data) {
//                $("#hotCodesList").html(data);
//                $("#hotCodesList", window.parent.document).html(data);
//                $("#hotCodes").val("");
//                hideProgressBar();
//            });
//        }
//    });
//}

function checkEculineInQuote(acctNo, cType) {
    if ($("#trmnum").val() !== '59') {
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
    var businessUnit = $('input:radio[name=businessUnit]:checked').val();
    if ($('#fileNumberId').val() !== '' && $('#fileNumberId').val() !== null) {
        showProgressBar();
        if ($("#trmnum").val() !== '59') {
            updateEconoOrEculine(businessUnit, $('#fileNumberId').val(), "", "QT-AutoNotes");
        }
        addQuoteClientHotCodes(acctNo);
        hideProgressBar();
    }
    if (cType !== "A") { // when selecting agent its no need.
        checkRateTypeDetails(acctNo, cType);
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
        $('#pickupInf').show();
    } else {
        $("#doorOriginCityZip").show();
        $("#doorOriginCityZip").val('');
        $("#manualDoorOriginCityZip").hide();
        $("#manualDoorOriginCityZip").val('');
    }
}

function cfclVoyage() {
    cfclOverride();
    var relayOverride = $('input:radio[name=relayOverride]:checked').val();
    var cfcl = $('input:radio[name=cfcl]:checked').val();
    if (relayOverride == 'N') {
        if ($("#portOfOriginR").val() !== "" && $("#finalDestinationR").val() !== "") {
            if ($("#showOlder").prop("checked"))
            {
                $("#methodName").val('getRelayAndUpComingSailingsPrevious');
            } else {
                $("#previousSailing").val($("#showOlder").prop("checked"));
                $("#methodName").val('getRelayAndUpComingSailings');
            }
            var params = $('#lclQuoteForm').serialize();
            $.ajax({
                type: "POST",
                url: $('#lclQuoteForm').attr("action"),
                data: params,
                async: false,
                success: function (data) {
                    $('#polPod').html(data.polPod);
                    $('#upcomingSailing').html(data.upcomingSailings);
                }
            });
        }
    } else {
        if ($("#portOfOriginR").val() !== "" && $('#portOfLoading').val() !== "" &&
                $('#portOfDestination').val() != "" &&
                $("#finalDestinationR").val() !== "") {
            if ($("#showOlder").prop("checked"))
            {
                $("#methodName").val('getRelayOverrideUpcomingPrevious');
            } else {
                $("#previousSailing").val($("#showOlder").prop("checked"));
                $("#methodName").val('getRelayOverrideUpcoming');
            }
            var params = $("#lclQuoteForm").serialize();
            $.post($("#lclQuoteForm").attr("action"), params, function (data) {
                $('#upcomingSailing').html(data);
            });
        }
    }
    if (cfcl !== "N") {
        changeQtRelay();
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
    if (!isHazmat) {
        checkHazDetails();
    } else {
        ratesRecalculation();
    }
}
function ratesRecalculation() {
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
                var doorOriginCityZip = $("#doorOriginCityZip").val();
                var pickupReadyDate = $('#pickupReadyDate').val();
                submitAjaxFormForRates("calculateCharges", '#lclQuoteForm', '#chargeDesc', origin, destination,
                        pol, pod, "", doorOriginCityZip, pickupReadyDate);
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
            }
            else {
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
            $('.Qhazmat').val('');
            ratesRecalculation();
        }
    });
}

function newTPAccountFromQuote(path, vendorId, addressId, acctType, countryId,
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
                                $('#methodName').val("saveQuote");
                                var params = $("#lclQuoteForm").serialize();
                                $.post($("#lclQuoteForm").attr("action"), params, function (data) {
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
function cfclOverride() {
    var bookingType = $("#bookingType").val();
    var cfcl = $('input:radio[name=cfcl]:checked').val();
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
            $("#polUnlocationcode").val($("#originUnlocationCode").val());
            $("#portOfLoadingId").val($("#portOfOriginId").val());
        }
        if (portOfDestination === "") {
            $("#portOfDestination").val($("#finalDestinationR").val());
            $("#podUnlocationcode").val($("#unlocationCode").val());
            $("#portOfDestinationId").val($("#finalDestinationId").val());
        }
        $('input:radio[name=relayOverride]').val(["Y"]);
    }
}
