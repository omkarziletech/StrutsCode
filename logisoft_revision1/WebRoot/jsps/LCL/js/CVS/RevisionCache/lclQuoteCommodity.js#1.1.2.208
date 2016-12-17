var path = "/" + window.location.pathname.split('/')[1];
// validation for commodity
$(document).ready(function () {
    var overShortDamage = parent.$('input:radio[name=overShortdamaged]:checked').val();
    var osdRemark = parent.$("#osdRemarks").val();
    var moduleName = $('#moduleName').val();
    var fileNumberId = $("#fileNumberId").val();
    if (overShortDamage === 'Y') {
        $("#overShortdamagedY").attr('checked', true);
        $("#osdRemarksTextAreaId").show();
    } else {
        $("#overShortdamagedN").attr('checked', true);
        $("#osdRemarksTextAreaId").hide();
    }
    if (osdRemark !== null) {
        $("#osdRemarks").val(osdRemark);
    }
    if (moduleName === 'Exports') {
        var smallParcel = parent.$("#ups").val() === "true" ? true : false;
        $("input:radio[name=ups]").val([smallParcel]);
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
    $('#saveCommodity').click(function () {
        parent.$("#ups").val($("input:radio[name=ups]:checked").val());
        var packageType = $('#packageType').val();
        var commodityType = $('#commodityType').val();
        if (commodityType === '') {
            $.prompt('Tariff field is required');
            $('#commodityType').css("border-color", "red");
            $('#commodityType').focus();
            return false;
        }
        if (packageType === '') {
            $.prompt('Package Type field is required');
            $('#packageType').css("border-color", "red");
            $('#packageType').focus();
            return false;
        }
        if (saveComm()) {
            $.prompt("Commodity is already added. Please select different Commodity");
            $("#commodityType").val("");
            $('#commodityNo').val('');
            $("#commodityType").css("border-color", "red");
            $('#saveCommodity').attr("disabled", false);
            return false;
        }
        if (parent.$('#moduleName').val() === 'Imports') {
            var orgUncode = $("#originUnlocationCode", window.parent.document).val();
            var polUncode = $("#polUnlocationcode", window.parent.document).val();
            var podUncode = $("#podUnlocationcode", window.parent.document).val();
            var commodityNo = $("#commodityNo").val();
            $.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.lcl.LCLImportRatesDAO",
                    methodName: "checkErt",
                    param1: orgUncode,
                    param2: polUncode,
                    param3: podUncode,
                    param4: commodityNo
                },
                preloading: true,
                success: function (data) {
                    if (null !== data && data === "Y" && $("#rtdTransaction", window.parent.document).val() === "N") {
                        $("#rtdTransaction", window.parent.document).val("Y");
                        $.prompt("ERT will change to Yes", {
                            callback: function () {
                                ratesValidation();
                            }
                        });
                    } else if (null !== data && data === "N" && $("#rtdTransaction", window.parent.document).val() === "Y") {
                        $("#rtdTransaction", window.parent.document).val("N");
                        $.prompt("ERT will change to No", {
                            callback: function () {
                                ratesValidation();
                            }
                        });
                    } else {
                        ratesValidation();
                    }
                }
            });
        } else {
            var smalParcelFlag = $("#smallParcelFlag").val();
            var totalMeasureImp = $("#smalParcelMeasureImp").val();
            var bkgvolumeImp = $('#bookedVolumeImperial').val();
            var previousImpMeasure = parent.$("#isMeasureImpChanged").val();
            var saveRemarks = parent.$("#saveRemarks").val();
            if (smalParcelFlag === 'true' && previousImpMeasure === 'true' && saveRemarks === 'true') {
                var remarks = "Cuft was changed from " + totalMeasureImp + " to " + bkgvolumeImp + ", due to small parcel.";
                parent.$("#smallParcelRemarks").val(remarks);
                $("#smallParcelRemarks").val(remarks);
            }
            validateCommodity();
        }
    });
    setQuotePieceDetail();
    if (moduleName === 'Exports') {
        var commNo = $('#commodityNo').val();
        var oldComm = $("#oldTariffNo").val();
        if (!isvalOfcom(commNo)) {
            $('#includeDestY').attr('disabled', true);
            $('#includeDestN').attr('disabled', true);
        } else {
            $('#includeDestY').removeAttr("disabled");
            $('#includeDestN').removeAttr('disabled');
        }
    }
});
function barrelPkgTypeValidation(barrel, pkgType) {
    if (barrel === 'Y' && (pkgType != 'BARREL' && pkgType != 'BARRELS')) {
        $.prompt('Pkg Type must be BARREL/BARRELS');
        $("#packageType").val("");
        $("#packageType").css("border-color", "red");
        $('#saveCommodity').attr("disabled", false);
        return false;
    }
}
function barrelRatesValidation(pooUnlocCode, fdUnlocCode, rateType, commodityNo) {
    rateType = rateType === 'R' ? 'Y' : rateType;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkBarrelRate",
            param1: pooUnlocCode,
            param2: fdUnlocCode,
            param3: rateType,
            param4: commodityNo,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data === 'false') {
                $.prompt('BARREL rate is not available for this commodity.please select another commodity');
                $('#commodityType').val('');
                $('#commodityNo').val('');
                // $('#saveCommodity').attr("disabled", false);
            } else {
                ratesValidation();
            }
        }
    });
}

function validateCommodity() {
    var barrel = $("input[name='isBarrel']:checked").val();
    var pkgType = $.trim($("#packageType").val().toUpperCase());
    var isBarrel = (barrel === 'Y' ? true : false);
    var commodityNo = $('#commodityNo').val();
    var pooUnlocCode = $("#originUnlocCodeId").val();
    var fdUnlocCode = $("#fdUnlocCodeId").val();
    var rateType = $("#rateType").val();
    var moduleName = $('#moduleName').val();
    if (isBarrel) {
        barrelPkgTypeValidation(barrel, pkgType);
        if (moduleName === "Exports") {
            barrelRatesValidation(pooUnlocCode, fdUnlocCode, rateType, commodityNo);
        } else {
            ratesValidation();
        }
    }
    else {
        ratesValidation();
    }

}
function ratesValidation() {
    var fileNumberId = $("#fileNumberId").val();
    var quotePieceId = $("#quotePieceId").val();
    if (fileNumberId !== null && fileNumberId !== '0' && fileNumberId !== "" && quotePieceId != null && quotePieceId != "") {
        isRatesValidation();
        var rateRecFlag = $('#ratesRecalFlag').val();
        if (rateRecFlag == "true") {
            modifyRatesCommodity(fileNumberId)
        } else {
            if (quotePieceId === "" || quotePieceId === null) {
                $('#ratesRecalFlag').val(true);//multiple commodity
            }
            saveCommodity(fileNumberId);
        }
    } else {
        if (quotePieceId === "" || quotePieceId === null) {
            $('#ratesRecalFlag').val(true);
        }
        saveCommodity(fileNumberId);
    }
}
function modifyRatesCommodity(fileNumberId) {
    var txt = 'AutoRates will be removed. Manual Rates will be recalculated.Are you Sure you want to Continue?';
    $.prompt(txt, {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                saveCommodity(fileNumberId);
                $.prompt.close();
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function saveCommodity(fileNumberId) {
    var methodName = "saveOrUpdateCommodity"
    var validateImpRatesText = "";
    if (fileNumberId === null || fileNumberId === '0' || fileNumberId === "") {
        methodName = "addQtCommodity"
    }
    $("#methodName").val(methodName);
    $.ajaxx({
        dataType: "json",
        url: $("#lclQuoteCommodityForm").attr("action"),
        data: $("#lclQuoteCommodityForm").serialize(),
        preloading: true,
        success: function (result) {
            parent.$("#commodityDesc").html(result.commodityDesc);
            var rateRecFlag = $('#ratesRecalFlag').val();
            if (rateRecFlag == "true") {
                parent.$("#chargeDesc").html(result.chargeDesc);
                validateImpRatesText = parent.$("#validateImpRates").val();
            }
            if ($('#moduleName').val() === 'Imports' && (validateImpRatesText.trim() !== "" && validateImpRatesText.trim() !== undefined && validateImpRatesText.trim() != null)) {
                validateImpRates(validateImpRatesText);
            } else {
                parent.$.fn.colorbox.close();
                if (fileNumberId === null || fileNumberId === '0' || fileNumberId === "") {
                    if ($('#moduleName').val() === 'Exports' && $('#originUnlocCodeId').val() != null && $('#originUnlocCodeId').val() != "") {
                        parent.submitForm('saveQuote');
                    } else if ($('#moduleName').val() === 'Imports') {
                        parent.submitForm('saveQuote');
                    }
                }
            }
        }
    });
}
function checkForNumberAndDecimal(obj) {
    var result;
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This field should be Numeric");

    }
}
function checkForNumber(obj) {
    if (!/^([0-9]{0,10})$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This field should be Numeric");
    }
}
function openDetails(path, editDimFlag, dimFlag) {
    var commodityNo = $('#commodityNo').val();
    var fileNumberId = $('#fileNumberId').val();
    var fileNumber = $('#fileNumber').val();
    var quotePieceId = $('#quotePieceId').val();
    var moduleName = $("#moduleName").val();
    var countVal = $('#countVal').val();//session commodity value id
    var href = path + "/quoteCommodityDetails.do?methodName=display&bookingPieceId=" + quotePieceId + "&commodityNo=" + commodityNo + "&fileNumberId=" + fileNumberId + "&countVal=" + countVal + "&moduleName=" + moduleName + "&fileNumber=" + fileNumber;
    $(".details").attr("href", href);
    $(".details").colorbox({
        iframe: true,
        width: "60%",
        height: "75%",
        title: "Commodity Details",
        onClosed: function () {
            showDimsDetails();
        }
    });
}
function checkCommodity() {
    var haz = $('#commodityHazmat').val();
    var oldHazmatVal = $('#oldHazmat').val();
    var fileId = $('#fileNumberId').val();
    if (oldHazmatVal != "true") {
        if (haz === 'true') {
            $('.hazYes').attr('checked', true);
            parent.$('.hazmatLabelVal').html("<span style='color:red;font-weight:bold;'>** HAZ **</span>");
            if (fileId !== "" && fileId !== null && fileId !== '0') {
                $("#hazmatY").click();
            }
            else {
                calculateHazRates();
            }
        }
        if (haz === 'false') {
            $('.hazNo').attr('checked', true);
        }
    }
}

function commodityDesc() {
    var moduleName = $('#moduleName').val();
    var commNo = $('#commodityNo').val();
    var fileLong = parent.$('#fileNumberId').val().toString();
    var fileId = $('#fileNumberId').val();
    var oldComm = $("#oldTariffNo").val();
    if (moduleName === 'Exports') {
        if ($('#pieceDesc').val() === "") {
            $('#pieceDesc').val($('#commodityType').val());
        }
        var brand = parent.getTradingPartnerPriority();
        if (brand === "None") {
            parent.$("#eculineCommodity").val("ECULINE_OR_ECONO_FROM_COMMODITY");
        }
        if (isClassOfComm(commNo, fileLong, oldComm)) {
            if (!isvalOfcom(commNo)) {
                $('#includeDestY').attr('disabled', true);
                $('#includeDestN').attr('disabled', true);
            } else {
                $('#includeDestY').removeAttr("disabled");
                $('#includeDestN').removeAttr('disabled');
            }
        } else {
            $('#includeDestY').attr('disabled', true);
            $('#includeDestN').attr('disabled', true);
            $('#includeDestN').attr('checked', true);
        }
    }
}

function checkSameCommodity() {//check same commodity validation
    var moduleName = $('#moduleName').val();
    var commNo = $('#commodityNo').val();
    var fileNumberId = $('#fileNumberId').val();
    var barrel = $("input[name='isBarrel']:checked").val();
    var commodityId = $('#quotePieceId').val();
    var hazmat = $("input[name='hazmat']:checked").val();
    var isHazmat = (hazmat === 'Y' ? true : false);
    var isBarrel = (barrel === 'Y' ? true : false);
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "quoteCommodityNum",
            param1: commNo,
            param2: fileNumberId,
            param3: commNo, //remove
            param4: isHazmat,
            param5: isBarrel,
            param6: commodityId,
            dataType: "json",
            request: "true"
        },
        async: false,
        success: function (data) {
            if (data) {
                sampleAlert("Commodity is already added. Please select different Commodity");
                $('#commodityType').val('');
                $('#commodityNo').val('');
                $('#commodityType').css('border-color', 'red');
                // $('#commodityType').focus();
                return false;
            }
        }
    });
    var spotRate = parent.$('input:radio[name=spotRate]:checked').val();
    if (moduleName === 'Exports' && spotRate !== undefined && spotRate === 'Y') {
        if ($("#commodityNo").val() !== '' && $("#commodityNo").val() !== '212600') {
            sampleAlert("Cannot change commodity as Spot Rate is set to Yes.");
            $("#commodityNo").val($('#oldTariffNo').val());
            $("#commodityType").val($('#oldcommodity').val());
            $("#commodityTypeId").val($('#oldcommodityId').val());
        }
    }
}

function calculateHazRates() {
    var fileId = $('#fileNumberId').val();
    var oldHazmat = $('#oldHazmat').val() == "true" ? 'Y' : 'N';
    var hazmat = $("input[name='hazmat']:checked").val();
    var podUnlocation = parent.$('#portOfDestination').val();
    var fdUnlocation = parent.$('#finalDestinationR').val();
    var podUnlocationcode = podUnlocation.substring(podUnlocation.indexOf("(") + 1, podUnlocation.indexOf(")"));
    var fdUnlocationcode = fdUnlocation.substring(fdUnlocation.indexOf("(") + 1, fdUnlocation.indexOf(")"));
    var fdCode = fdUnlocationcode === "" ? podUnlocationcode : fdUnlocationcode;
    var podCode = podUnlocationcode === "" ? fdUnlocationcode : podUnlocationcode;
    if (hazmat === 'Y' && oldHazmat == 'N') {
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
                    hazMsg("We do not accept hazardous cargo to this market:" + data[1], false, hazmat, fileId, oldHazmat);
                } else if (data[0] === "R") {
                    hazMsg("Hazardous Restrictions apply for :" + data[1] + ". Please confirm that cargo is acceptable for export and determine proper charges.", true, hazmat, fileId, oldHazmat);
                }
                else {
                    calculateHazmatRates(hazmat, fileId, oldHazmat);
                }
            }
        });

    } else if (hazmat === 'N' && oldHazmat == 'Y') {
        removeHazmatRates(hazmat, fileId);
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
function clearHazmatValues() {
    $('input:radio[name=hazmat]').val(["N"]);
    var haz = $('#commodityHazmat').val();
    if (haz == 'true') {
        $("#commodityType").val("");
        $('#commodityNo').val('');
        $("#commodityType").css("border-color", "red");
    }
}

function calculateHazmatRates(hazmat, fileId, oldHazmat) {
    $.prompt("Hazmat Rates will be added.Are you sure you want to continue", {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                parent.$('.hazmatLabelVal').html("<span style='color:red;font-weight:bold;'>** HAZ **</span>");
                $('#hazmatY').attr('checked', true);
                $('#oldHazmat').val(true);
                if (fileId !== "" && fileId !== null && fileId !== '0') {
                    submitAjaxFormForHazmat('calculateHazmatCharge', '#lclQuoteCommodityForm', '#chargeDesc', hazmat, fileId);
                }
            } else if (v === 2) {
                $('#oldHazmat').val(false);
                $('#hazmatY').attr('checked', false);
                $('#hazmatN').attr('checked', true);
            }
            $.prompt.close();
        }
    });
}

function removeHazmatRates(hazmat, fileId) {
    $.prompt("Hazmat Rates will be removed.Are you sure you want to continue", {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                parent.$('.hazmatLabelVal').html("");
                $('#hazmatN').attr('checked', true);
                $('#hazmatY').attr('checked', false);
                $('#oldHazmat').val(false);
                if (fileId !== "" && fileId !== null && fileId !== '0') {
                    submitAjaxFormForHazmat('calculateHazmatCharge', '#lclQuoteCommodityForm', '#chargeDesc', hazmat, fileId);
                }
            } else if (v === 2) {
                $('#hazmatY').attr('checked', true);
                $('#hazmatN').attr('checked', false);
                $('#oldHazmat').val(true);
            }
            $.prompt.close();
        }
    });
}

function submitAjaxFormForHazmat(methodName, formName, selector, hazmat, fileId) {
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&hazmat=" + hazmat + "&fileNumberId=" + fileId;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
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
    $('#bookedPieceCount').keydown(function (e) {
        var barrel = $("input[name='isBarrel']:checked").val();
        if (e.keyCode === 9) {
            if (barrel === 'Y') {
                $('#bookedWeightImperial').focus().val();
                return false;
            } else {
                if (parent.$('#moduleName').val() === "Imports") {
                    $('#packageType').focus().val();
                    $('#packageTypeP').focus().val();
                } else {

                    $('#packageType').focus().val();
                    $('#packageTypeP').focus().val();

                }
                return false;
            }
        }
    });
    $("#markNoDesc").textarea({
        height: "150px",
        width: "180px",
        cols: 25,
        rows: 12,
        limit: 250,
        target: "#lclCommodityForm"
    });
    $("#pieceDesc").textarea({
        height: "150px",
        width: "350px",
        cols: 50,
        rows: 12,
        limit: 250,
        target: "#lclCommodityForm"
    });
});

function openTariffPopUp(path) {
    var originNo = $('#originNo').val();
    var destinationNo = $('#destinationNo').val();
    var href = path + "/lclQuoteCommodity.do?methodName=displayTariffDetails&originNo=" + originNo + "&destinationNo=" + destinationNo;
    $("#tarif").attr("href", href);
    $("#tarif").colorbox({
        iframe: true,
        width: "70%",
        height: "95%",
        title: "Tariff Details"
    });
}


document.onkeydown = mykeyhandler;

function mykeyhandler() {
    var activeElType = document.activeElement.type;
    var activeNodeName = document.activeElement.nodeName;
    if (window.event && window.event.keyCode === 8 && activeElType != 'text' && activeElType != 'textarea') {
        // try to cancel the backspace
        window.event.cancelBubble = true;
        window.event.returnValue = false;
        return false;
    }
}

function toggleMeasure(module) {
    var $weight_metric = $("#bookedWeightMetric");
    var $volume_metric = $("#bookedVolumeMetric");
    var $weight_imperial = $("#bookedWeightImperial");
    var $volume_imperial = $("#bookedVolumeImperial");
    var $toggle_Imp_met = $("#toggle-Imp-met");
    var _readonly = "textlabelsBoldForTextBoxDisabledLook";
    if ($volume_imperial.hasClass(_readonly)) {
        $toggle_Imp_met.data('tipText', 'Switch to Metric input');
        $('#toggle-measure').attr("src", path + '/images/icons/arrow/green_Up.png')
        $volume_imperial.removeClass(_readonly);
        $weight_imperial.removeClass(_readonly);
        $weight_metric.addClass(_readonly);
        $volume_metric.addClass(_readonly);

        $volume_imperial.removeAttr('tabindex');
        $weight_imperial.removeAttr('tabindex');
        $weight_metric.attr('tabindex', -1);
        $volume_metric.attr('tabindex', -1);

        $weight_metric.attr("readonly", true);
        $volume_metric.attr("readonly", true);
        $weight_imperial.attr("readonly", false);
        $volume_imperial.attr("readonly", false);
    } else {
        $toggle_Imp_met.data('tipText', 'Switch to Imperial input');
        $('#toggle-measure').attr("src", path + '/images/icons/arrow/green_down.png')
        $weight_imperial.attr('tabindex', -1);
        $volume_imperial.attr('tabindex', -1);
        $weight_metric.removeAttr('tabindex');
        $volume_metric.removeAttr('tabindex');


        $weight_metric.attr("readonly", false);
        $volume_metric.attr("readonly", false);
        $weight_imperial.attr("readonly", true);
        $volume_imperial.attr("readonly", true);

        $volume_imperial.addClass(_readonly);
        $weight_imperial.addClass(_readonly);
        $weight_metric.removeClass(_readonly);
        $volume_metric.removeClass(_readonly);
    }
}

jQuery(document).ready(function () {
    validateRateStandard();
    $('#bookedPieceCount').focus().val();//set cursor focus on booking piece count
    if ($('#moduleName').val() === "Imports") {
        $("#billToParty").val(parent.$('input:radio[name=billtoCodeImports]:checked').val());
        $("#transhipment").val(parent.$("input:radio[name='transShipMent']:checked").val());
        $("#billingType").val(parent.$("input:radio[name='pcBothImports']:checked").val());
    } else {
        var delMetro = parent.$('input:radio[name=deliveryMetro]:checked').val();
        $("#deliveryMetro").val(delMetro);
        var pcBoth = parent.$('input:radio[name=pcBoth]:checked').val();
        $("#pcBoth").val(pcBoth);
        var calcHeavy = parent.$('input:radio[name=calcHeavy]:checked').val();
        $("#calcHeavy").val(calcHeavy);
    }
    var org = parent.$("#portOfOriginR").val();
    var orgName = org.substring(0, org.indexOf('/'));
    $("#originName").val(orgName);
    var dest = parent.$("#finalDestinationR").val();
    var destName = dest.substring(0, dest.indexOf('/'));
    $("#destinationName").val(destName);
    var pol = parent.$("#portOfLoading").val();
    var polUnLoc = pol.substring(pol.lastIndexOf('(') + 1, pol.lastIndexOf(')'));
    $("#polUnlocCodeId").val(polUnLoc);
    var pod = parent.$("#portOfDestination").val();
    var podUnLoc = pod.substring(pod.lastIndexOf('(') + 1, pod.lastIndexOf(')'));
    $("#podUnlocCodeId").val(podUnLoc);
    showDimsToolTip();
    barrelCalculation();
    $('#loadForm').val($('#lclQuoteCommodityForm').serialize());
});
function calculateBarrelBookedCuft() {
    var val = $("input[name='isBarrel']:checked").val();
    var bookedPiece = $("#bookedPieceCount").val();
    if (val == "Y") {
        $("#bookedVolumeImperial").val(parseFloat(bookedPiece * 14).toFixed(3));
        $("#bookedVolumeMetric").val(parseFloat((bookedPiece * 14) / 35.3146).toFixed(3));
    }
}

function setPackageTypeQuery(obj) {
    var val = obj.value;
    if (val <= 1 || val === null || val === undefined) {
        $('#packageType').attr('alt', 'COMMODITY_PACKAGE_TYPE');
    } else {
        $('#packageType').attr('alt', 'PACKAGE_TYPE_PLURAL');
    }
}
function isRatesValidation() {
    var oldCommNo = $("#oldCommNo").val();
    var commodityNo = $("#commodityNo").val();
    var oldBkgWeightImp = $('#bookWeigtImp').val();
    var bkgWeightImp = $('#bookedWeightImperial').val();
    var oldBkgvolumeImp = $('#bookVolumeImp').val();
    var bkgvolumeImp = $('#bookedVolumeImperial').val();
    var oldBkgWeightMet = $('#oldBkgWeightMetric').val();
    var bkgWeightMet = $('#bookedWeightMetric').val();
    var oldBkgvolumeMet = $('#oldBkgVolumeMetric').val();
    var bkgvolumeMet = $('#bookedVolumeMetric').val();
    var oldHazmat = $('#oldHazmat').val() == "true" ? 'Y' : 'N';
    var oldBarrel = $('#oldBarrel').val() == "true" ? 'Y' : 'N';
    var oldDestFee = $('#oldDestFee').val() === "true" ? 'Y' : 'N';
    var includeDestfees = $("input[name='includeDestfees']:checked").val();
    var hazmat = $("input[name='hazmat']:checked").val();
    var isBarrel = $("input[name='isBarrel']:checked").val();
    var oldPieceCount = $('#oldPieceCount').val();
    var bookedPieceCount = $('#bookedPieceCount').val();
    var moduleName = $('#moduleName').val();
    if ((oldPieceCount !== bookedPieceCount) || (oldCommNo !== commodityNo) || (oldBkgWeightImp !== bkgWeightImp) || (oldBkgvolumeImp !== bkgvolumeImp)
            || (oldBkgWeightMet !== bkgWeightMet) || (oldBkgvolumeMet !== bkgvolumeMet)
            || (oldHazmat !== hazmat) || (oldBarrel !== isBarrel) || (moduleName === 'Exports' && oldDestFee !== includeDestfees)) {
        $('#ratesRecalFlag').val(true);
    }
}
function showDimsDetails() {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "displayQuoteDimsDetails",
            param1: $('#quotePieceId').val(),
            param2: $('#fileNumberId').val(),
            param3: $('#commodityNo').val(),
            request: "true",
            dataType: "json"
        },
        preloading: true,
        success: function (data) {
            JToolTipForLeft('#dimsDetails', data[0], 450);
            $("#QtdetailListSize").val(data[1]);
            setQuotePieceDetail();
            return true;
        }
    });
}

function showDimsToolTip() {
    JToolTipForLeft('#dimsDetails', $('#dimsToolTip').val(), 450);
    return true;
}
function barrelCalculation() {
    //alert ("inside barrelCalculation "); 
    var bookedPiece = $("#bookedPieceCount").val();
    var barrelVal = $("input[name='isBarrel']:checked").val();
    if (barrelVal == "Y") {
        //   alert ("inside barrelCalculation barrelVal == Y"); 
        // $("#bookedDim").hide();
        $("#bookedDim").show();
        $('#piecetd').attr("width", "");
        $('#piecetd1').attr("width", "");
        $(".bookedBarrel").removeClass("text-readonly");
        $(".bookedBarrel").removeAttr("readonly");
//        $('#piecetd').attr("width", "53%");
//        $('#piecetd1').attr("width", "57%");
//        $(".bookedBarrel").addClass("text-readonly");
//        $(".bookedBarrel").attr("readonly", true);
//        $("#bookedVolumeImperial").val(parseFloat(bookedPiece * 14).toFixed(3));
//        $("#bookedVolumeMetric").val(parseFloat((bookedPiece * 14) / 35.3146).toFixed(3));
    }
    if (barrelVal == "N") {
        // alert ("inside barrelCalculation barrelVal == NNNN"); 
        $("#bookedDim").show();
        $('#piecetd').attr("width", "");
        $('#piecetd1').attr("width", "");
        $(".bookedBarrel").removeClass("text-readonly");
        $(".bookedBarrel").removeAttr("readonly");
    }
}
function setBarrelPkgDetails() {
    // alert("setBarrelPkgDetails" );
    var barrelVal = $("input[name='isBarrel']:checked").val();
    barrelCalculation();
    if (barrelVal == "Y") {
        //    alert("inside barrelVal == y ");
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "displayPackageType",
                param1: "BARREL",
                dataType: "json"
            },
            preloading: true,
            success: function (data) {
                //    alert ("displayPackageType");
                $('#packageType').val(data.substring(0, data.indexOf('==')));
                $('#packageTypeId').val(data.substring(data.indexOf('==') + 2));
                $('#packageType').attr('readOnly', true);
                $('#packageType').addClass('text-readonly');
                var bookedPiece = $("#bookedPieceCount").val();
                $("#bookedVolumeImperial").val(parseFloat(bookedPiece * 14).toFixed(3));
                $("#bookedVolumeMetric").val(parseFloat((bookedPiece * 14) / 35.3146).toFixed(3));
            }

        });
    }
}
function clearPkgType() {
    barrelCalculation();
    $('#packageType').removeClass("text-readonly");
    $('#packageType').removeAttr("readonly");
    $("#packageType").val('');
    $("#packageTypeId").val('');
}
function cancelCommodity() {
    parent.$.fn.colorbox.close();
}

function sampleAlert(txt) {
    $.prompt(txt);
}

function saveComm() {
    var flag = false;
    var commNo = $('#commodityNo').val();
    var fileNumberId = $('#fileNumberId').val();
    var barrel = $("input[name='isBarrel']:checked").val();
    var commodityId = $('#quotePieceId').val();
    var hazmat = $("input[name='hazmat']:checked").val();
    var isHazmat = (hazmat === 'Y' ? true : false);
    var isBarrel = (barrel === 'Y' ? true : false);
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "quoteCommodityNum",
            param1: commNo,
            param2: fileNumberId,
            param3: commNo, //remove
            param4: isHazmat,
            param5: isBarrel,
            param6: commodityId,
            dataType: "json",
            request: "true"
        },
        async: false,
        success: function (data) {
            if (data) {
                flag = data;
            }
        }
    });
    return flag;

}
function showBlock(tar) {
    $('.smallInputPopupBox').hide();
    $(tar).show("slow");
}
function hideBlock(tar) {
    $(tar).hide("slow");
}

function addHotCode3pRef(fileId) {
    addGeneralInfOn3pRef("hotCodesBox", "hotCodesList", "hotCodes", "hotCodes", fileId);
}

function addTracking3pRef(fileId) {
    addGeneralInfOn3pRef("trackingBox", "trackingList", "tracking", "tracking", fileId);
}

function addGeneralInfOn3pRef(hideSelectorText, selector, refType, refValue, fileId) {
    showProgressBar();
    $('#' + hideSelectorText).hide();
    $('#methodName').val('addHotcodeTrackingComm');
    var params = $("#lclQuoteCommodityForm").serialize();
    params += "&refTypeFlag=" + refType + "&fileNumberId=" + fileId + "&refValue=" + $('#' + refValue).val();
    $.post($("#lclQuoteCommodityForm").attr("action"), params, function (data) {
        $("#" + selector).html(data);
        $("#" + selector, window.parent.document).html(data);
        if ($('#moduleName').val() === 'Exports' &&
                "EBL" === $('#' + refValue).val().substring(0, $('#' + refValue).val().indexOf("/"))) { // eculine logic for export
            parent.$("#eculine").attr("checked", true);
        }
        $('#' + refValue).val('');
        hideProgressBar();
    });
}

function displayOsdBox() {
    var osdValues = $('input:radio[name=overShortdamaged]:checked').val();
    parent.$('input:radio[name=overShortdamaged]').val([osdValues]);
    if (osdValues === "Y") {
        $("#osdRemarksTextAreaId").show();
        parent.$("#osdRemarksTextAreaId").show();
    } else {
        parent.$("#osdRemarksTextAreaId").hide();
        $("#osdRemarksTextAreaId").hide();
    }
}
function deleteOsdRemarks(txt, id) {
    var user = parent.$("#loginUserId").val();
    var fileState = parent.$("#fileState").val();
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
                            parent.$('input:radio[name=overShortdamaged]').val([osdValues]);
                            parent.$("#osdRemarks").val('');
                            parent.$("#osdRemarksTextAreaId").hide();
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

function checkValidHotCode(id1, id2) {
    $("." + id2).each(function () {
        if ($(this).val() === $("#" + id1).val()) {
            $.prompt("Hot code already exists.");
            $("#hotCodes").val('');
            return false;
        }
    });
}

function deleteHotCode(txt, lcl3pRefId) {
    deleteGeneralInformation(txt, lcl3pRefId, 'hotCodes', 'hotCodesList')
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
                var businessUnit = parent.$("input:radio[name=businessUnit]").val();
                $('#methodName').val('deleteLcl3pReferenceComm');
                var params = $("#lclQuoteCommodityForm").serialize();
                var fileNumberId = $("#fileNumberId").val();
                params += "&noteId=" + lcl3pRefFlag + "&fileNumberId=" + fileNumberId + "&lcl3pRefId=" + lcl3pRefId + "&businessUnit=" + businessUnit;
                $.post($("#lclQuoteCommodityForm").attr("action"), params, function (data) {
                    $('#' + refreshDivId).html(data);
                    $("#" + refreshDivId, window.parent.document).html(data);
                    hideProgressBar();
                });
                var refValue = $("#hotCodesRef" + lcl3pRefId).val();
                var module = parent.$('#moduleName').val();
                if (refValue !== undefined && module === 'Exports') {
                    var hotcodeArray = refValue.split("/");
                    if (hotcodeArray[0] === 'EBL') {
                        parent.$("#econo").attr("checked", true);
                    }
                }
                $.prompt.close();
            }
            else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}

function osdRefresh() {
    parent.$("#osdRemarks").val($('#osdRemarks').val());
}

function setQuotePieceDetail() {
    if ($("#QtdetailListSize").val() === 'true' && $('#moduleName').val() === 'Exports') {
        $(".details").removeClass('button-style1');
        $(".details").addClass('green-background');
    } else if ($('#moduleName').val() === 'Exports') {
        $(".details").removeClass('green-background');
        $(".details").addClass('button-style1');
    }
}

function isClassOfComm(comm, fileNumberId, oldComm) {
    var flag = false;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO",
            methodName: "isClassOfcom",
            param1: fileNumberId,
            param2: comm,
            param3: oldComm
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}

function isvalOfcom(comm) {
    var flag = false;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO",
            methodName: "isvalOfcom",
            param1: comm
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}

function validateImpRates(txt) {
    closePreloading();
    $.prompt(txt, {
        buttons: {
            Ok: 0
        },
        async: false,
        submit: function (v) {
            if (v === 0) {
                parent.$.fn.colorbox.close();
                parent.submitForm('saveQuote');
            }
        }
    });
}

function getPortOfDestination() {
    var portOfDestination = parent.$('#portOfDestination').val();
    if (portOfDestination ===""){
         portOfDestination= parent.$('#finalDestinationR').val();
     }
    if (portOfDestination !== undefined && portOfDestination !== null && portOfDestination.value !== "") {
        if (portOfDestination.lastIndexOf("(") > -1 && portOfDestination.lastIndexOf(")") > -1) {
            return portOfDestination.substring(portOfDestination.lastIndexOf("(") + 1, portOfDestination.lastIndexOf(")"));
        }
    }
    return "";
}

function validateRateStandard(){
     var pod= getPortOfDestination();
     jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "validateRateStandard",
                param1: pod,
        },
        async: false,
        success: function (data) {
            if(data === true){
                $('#bookedWeightMetric').attr("readonly", false);
                $('#bookedVolumeMetric').attr("readonly", false);
                $('#bookedWeightImperial').attr("readonly", true);
                $('#bookedVolumeImperial').attr("readonly", true);
                $('#bookedWeightMetric').attr("class", "twoDigitDecFormat textlabelsBoldForTextBox  booked bookedBarrel");
                $('#bookedVolumeMetric').attr("class", "twoDigitDecFormat textlabelsBoldForTextBox  booked bookedBarrel");
                $('#bookedWeightImperial').attr("class", "text1 weight booked textlabelsBoldForTextBox textlabelsBoldForTextBoxDisabledLook");
                $('#bookedVolumeImperial').attr("class", "text1 weight booked textlabelsBoldForTextBox textlabelsBoldForTextBoxDisabledLook");
        }}
    });
}