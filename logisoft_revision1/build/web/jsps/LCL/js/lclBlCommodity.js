$(document).ready(function () {
    var fileNumberId = $("#fileNumberId").val();
    $(".hazYes").attr("disabled", true);
    $(".hazNo").attr("disabled", true);
    setPackageTypeQuery();
    $("#markNoDesc").textarea({
        height: "150px",
        width: "180px",
        cols: 25,
        rows: 12,
        limit: 20000,
        target: "#lclBlCommodityForm"
    });
    $("#pieceDesc").textarea({
        height: "150px",
        width: "350px",
        cols: 50,
        rows: 12,
        limit: 20000,
        target: "#lclBlCommodityForm"
    });
    showDimsToolTip();
    if (parent.$('#file_status').val() === 'M' && parent.$("#blUnitCob").val() === 'true'
            && parent.$("#blVoyageStatus").val() !== 'V') {
        disableBLCOB();
    }
    setBLPieceDetail();
    var commNo = $('#commodityNo').val();
    $("#pieceDesc").css({
        "overflow": "visible"
    });
    $("#markNoDesc").css({
        "overflow": "visible"
    });
});
function checkForNumberAndDecimal(obj) {
    var result;
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This field should be Numeric");

    }
}

function openDetails(path, blPieceId, editDimFlag, dimFlag) {
    var fileNumber = $('#fileNumber').val();
    var href = path + "/blCommodityDetails.do?methodName=display&blPieceId=" + blPieceId + "&editDimFlag=" + editDimFlag + "&dimFlag=" + dimFlag + "&fileNumber=" + fileNumber;
    $(".details").attr("href", href);
    $(".details").colorbox({
        iframe: true,
        width: "60%",
        height: "75%",
        title: "Commodity Details",
        onClosed: function () {
            showBlDimsDetails();
        }
    });
}
function setHazmatComm() {
    var haz = $('#commHazmat').val();
    if (haz == 'true') {
        $('.hazYes').attr('checked', true);
    } else {
        $('.hazNo').attr('checked', true);
    }
}


function checkSameCommodity() {
    var commoNo = $('#commodityNo').val();
    var fileNumberId = $("#fileNumberId").val();
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "commodityNumForBl",
            param1: commoNo,
            param2: fileNumberId,
            dataType: "json"
        },
        preloading: true,
        success: function (data) {
            if (data) {
                $.prompt("Commodity is already added.Please Select different Commodity");
                $("#commodityType").val('');
                $('#commodityNo').val('');
                $("#commodityType").css("border-color", "red");

            }
        }
    });
    var spotRate = parent.$('input:radio[name=spotRate]:checked').val();
    if (spotRate !== undefined && spotRate === 'Y') {
        if ($(".commodityNo").val() !== '' && $("#commodityNo").val() !== '212600') {
            sampleAlert("Cannot change commodity as Spot Rate is set to Yes.");
            $("#commodityNo").val($('#oldTariffNo').val());
            $("#commodityType").val($('#oldcommodity').val());
            $("#commodityTypeId").val($('#oldcommodityId').val());
        }
    }
}

function getPredefinedRemarks(path, multipleSelect, selector) {
    var url = path + '/lclBooking.do?methodName=getPredefinedRemarks&multipleSelect=' + multipleSelect
            + "&moduleName=Exports&from=commodityPopup";
    $(selector).attr("href", url);
    $(selector).colorbox({
        iframe: true,
        width: "50%",
        height: "70%",
        title: "Predefined remarks"
    });
}

function setPackageTypeQuery() {
    var pieceCount = $('#actualPieceCount').val()
    if (pieceCount <= 1 || pieceCount === null || pieceCount === undefined) {
        $('#packageType').attr('alt', 'COMMODITY_PACKAGE_TYPE');
    } else {
        $('#packageType').attr('alt', 'PACKAGE_TYPE_PLURAL');
    }
}
$(document).ready(function () {
    $('#saveBlCommodity').click(function () {
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
        barrelValidation();
    });
});
function barrelValidation() {
    var barrel = $("input[name='isBarrel']:checked").val();
    var pkgType = $.trim($("#packageType").val().toUpperCase());
    var isBarrel = (barrel === 'Y' ? true : false);
    var pooUnlocCode = $("#pooUnlocCode").val();
    var fdUnlocCode = $("#fdUnlocCode").val();
    var rateType = $("#rateType").val();
    var commodityNo = $('#commodityNo').val();
    if (isBarrel) {
        barrelPkgTypeValidation(barrel, pkgType);
        barrelRatesValidation(pooUnlocCode, fdUnlocCode, rateType, commodityNo);
    } else {
        ratesValidation();
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
function ratesValidation() {
    isRatesValidation();
    var rateRecFlag = $('#ratesRecalFlag').val();
    if (rateRecFlag == "true") {
        modifiyRates();//checking any values r changed r  not
    } else {
        saveCommodity();
    }

}
function isRatesValidation() {
    var oldCommNo = $("#oldCommNo").val();
    var commodityNo = $("#commodityNo").val();
    var oldActualWeightImp = Number($('#oldActualWeigtImp').val()).toFixed(3);
    var actualWeightImp = Number($('#actualWeightImperial').val()).toFixed(3);
    var oldActualvolumeImp = Number($('#oldActualVolumeImp').val()).toFixed(3);
    var actualvolumeImp = Number($('#actualVolumeImperial').val()).toFixed(3);
    var oldActualWeightMet = Number($('#oldActualWeightMetric').val()).toFixed(3);
    var actualWeightMet = Number($('#actualWeightMetric').val()).toFixed(3);
    var oldActualvolumeMet = Number($('#oldActualVolumeMetric').val()).toFixed(3);
    var actualVolumeMet = Number($('#actualVolumeMetric').val()).toFixed(3);
    var oldHazmat = $('#oldHazmat').val() == "true" ? 'Y' : 'N';
    var oldBarrel = $('#oldBarrel').val() == "true" ? 'Y' : 'N';
    var hazmat = $("input[name='hazmat']:checked").val();
    var isBarrel = $("input[name='isBarrel']:checked").val();
    var oldDestFee = $('#oldDestFee').val() === "true" ? 'Y' : 'N';
    var includeDestfees = $("input[name='includeDestfees']:checked").val() === "Y" ? 'Y' : 'N';
    if ((oldCommNo !== commodityNo) || (oldActualWeightImp !== actualWeightImp) || (oldActualvolumeImp !== actualvolumeImp)
            || (oldActualWeightMet !== actualWeightMet) || (oldActualvolumeMet !== actualVolumeMet)
            || (oldHazmat !== hazmat) || (oldBarrel !== isBarrel) || (oldDestFee !== includeDestfees)) {
        if (parent.$("#blUnitCob").val() === 'true'
                && $('#roleDutyChangeBLCommodityAfterCOB').val() === 'true') {
            $('#ratesRecalFlag').val(false);
        }else{
            $('#ratesRecalFlag').val(true);
        }
    }
}
function modifiyRates() {
    var txt = 'AutoRates will be removed. Manual Rates will be recalculated.Are you Sure you want to Continue?';
    $.prompt(txt, {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                saveCommodity();
                $.prompt.close();
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function saveCommodity() {
    $("#pcBoth").val(parent.$("input:radio[name='pcBoth']:checked").val());
    $("#methodName").val("saveOrUpdateCommodity");
    $.ajaxx({
        dataType: "json",
        url: $("#lclBlCommodityForm").attr("action"),
        data: $("#lclBlCommodityForm").serialize(),
        preloading: true,
        success: function (result) {
            parent.$("#commodityDesc").html(result.commodityDesc);
            var rateRecFlag = $('#ratesRecalFlag').val();
            if (rateRecFlag == "true") {
                parent.$("#chargeBlDesc").html(result.chargeDesc);
            }
            var freeBl = parent.$("input:radio[name='freeBl']:checked").val();
            if (freeBl === 'Y') {
                parent.$('#calculate').hide();
                parent.$('#costCharge').hide();
                parent.$('input:radio[name=spotRate]').attr('disabled', true);
                if (parent.$("input:radio[name='spotRate']:checked").val() == 'Y') {
                    parent.$('#lclSpotRate').attr("disabled", true);
                    parent.$('#lclSpotRate').removeClass("green-background");
                    parent.$('#lclSpotRate').addClass("gray-background");
                }
            }
            var billToParty = parent.$("input:radio[name=billPPD]:checked").val();
            var preventExpRelRole = parent.$('#preventExpRel').val();
            if ((billToParty === 'F' && parent.$('#fwdStatus').val() === 'No Credit') || (billToParty === 'S' && parent.$('#status').val() === 'No Credit') ||
                    (billToParty === 'T' && parent.$('#thirdPartyStatus').val() === 'No Credit'))
            {
                if (parent.$('#termsType1').val() === 'ER' && preventExpRelRole === 'true')
                {
                    var filenumberId = parent.$('#fileId').val();
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
                                parent.$('#termsType1').val('');
                                parent.$.fn.colorbox.close();
                            } else {
                                parent.$.fn.colorbox.close();
                            }
                        }
                    });
                } else {
                    parent.$.fn.colorbox.close();
                }
            } else {
                parent.$.fn.colorbox.close();
            }
        }
    });
}
function calculateActualCuft() {//barrel
    var val = $("input[name='isBarrel']:checked").val();
    var actualPiece = $("#actualPieceCount").val();
    if (val == "Y") {
        $("#actualVolumeImperial").val(parseFloat(actualPiece * 14).toFixed(3));
        $("#actualVolumeMetric").val(parseFloat((actualPiece * 14) / 35.3146).toFixed(3));
    }
}
function barrelBlCalculation() {
    var actualPiece = $("#actualPieceCount").val();
    var val = $("input[name='isBarrel']:checked").val();
    if (val == "Y") {
        $('#piecetd').attr("width", "52.5%");
        $('#piecetd1').attr("width", "57%");
        $("#actualDim").hide();
        $("#actualVolumeImperial").val(parseFloat(actualPiece * 14).toFixed(3));
        $("#actualVolumeMetric").val(parseFloat((actualPiece * 14) / 35.3146).toFixed(3));
    }
    if (val == "N") {
        $('#piecetd').attr("width", "");
        $('#piecetd1').attr("width", "");
        $("#actualDim").show();
    }
}
function setBarrelBlPkgDetails() {
    var barrelVal = $("input[name='isBarrel']:checked").val();
    barrelBlCalculation();
    if (barrelVal == "Y") {
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
                $('#packageType').val(data.substring(0, data.indexOf('==')));
                $('#packageTypeId').val(data.substring(data.indexOf('==') + 2));
                $('#packageType').attr('readOnly', true);
                $('#packageType').addClass('text-readonly');
            }
        });
    }
}
function clearBlPkgType() {
    barrelBlCalculation();
    $('#packageType').removeClass("text-readonly");
    $('#packageType').removeAttr("readonly");
    $("#packageType").val('');
    $("#packageTypeId").val('');
}

function showBlDimsDetails() {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.BlCommodityDetailsDAO",
            methodName: "displayBlDimsDetails",
            param1: $('#blPieceId').val(),
            dataType: "json"
        },
        preloading: true,
        success: function (data) {
            JToolTipForLeft('#dimsDetails', data[0], 450);
            $("#pieceDetailAvailable").val(data[1]);
            setBLPieceDetail();
            return true;
        }
    });
}
function showDimsToolTip() {
    JToolTipForLeft('#dimsDetails', $('#dimsToolTip').val(), 450);
    return true;
}

function setBLPieceDetail() {
    if ($("#pieceDetailAvailable").val() === 'true') {
        $(".details").removeClass('button-style1');
        $(".details").addClass('green-background');
    } else {
        $(".details").removeClass('green-background');
        $(".details").addClass('button-style1');
    }
}

function disableBLCOB() {
    var form = document.getElementById('lclBlCommodityForm');
    var element;
    var isChangeAfterCOB = $("#roleDutyChangeBLCommodityAfterCOB").val();
    if (isChangeAfterCOB==='false') {
        for (var i = 0; i < form.elements.length; i++) {
            element = form.elements[i];
            var className = $("#" + form.elements[i].id).attr('class');
            if (element.type === "text" || element.type === "textarea") {
                if (className === undefined) {
                    parent.disabledCondition(element);
                } else if (className.indexOf('commonClass') === -1) {
                    parent.disabledCondition(element);
                }
            }
            if (element.type === "select-one" || element.type === "radio" || element.type === "checkbox") {
                if (className === undefined) {
                    element.style.border = 0;
                    element.disabled = true;
                } else if (className.indexOf('commonClass') === -1) {
                    element.style.border = 0;
                    element.disabled = true;
                }
            }
        }
        $("#dimsDetails").hide();
        $("#actualDim").css("display", "none");
    }
}
function barrelPkgTypeValidation(barrel, pkgType) {
    if (barrel === 'Y' && (pkgType != 'BARREL' && pkgType != 'BARRELS')) {
        $.prompt('Pkg Type must be BARREL/BARRELS');
        $("#packageType").val("");
        $("#packageType").css("border-color", "red");
        $('#saveCommodity').attr("disabled", false);
        return false;
    }
}

function copyPO(fileNumberId){
    var txt = "Are you sure";
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
                        methodName: "fetchPOForCommodityDesc",
                        param1: fileNumberId,
                        dataType: "json"
                    },
                    preloading: true,
                    success: function (data) {
                        if(null !== data && data !== 'null'){
                            data = data.replace(/,/g,', ');
                            if($('#pieceDesc').val() !== ''){
                                $('#pieceDesc').val($('#pieceDesc').val()+","+data);
                            }else{
                                $('#pieceDesc').val(data);
                            }
                        }
                    }
                });
            }
        }
    });
}
