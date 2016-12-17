var path = "/" + window.location.pathname.split('/')[1];
// validation for commodity
$(document).ready(function () {
    var fileStatus = $('#status').val();
    var fileNumberId = $("#fileNumberId").val();
    var moduleName = parent.$('#moduleName').val();
    var dispo = parent.$("#exportDisposition").val();
    $('#saveCommodity').click(function () {
        parent.$("#ups").val($("input:radio[name=ups]:checked").val());
        $('#saveCommodity').attr("disabled", true);
        var commodityType = $('#commodityType').val();
        if (commodityType === '') {
            alertMsgByFocus('Tariff field is required', '#commodityType');
            $('#saveCommodity').attr("disabled", false);
            return false;
        }
        var packageType = $('#packageType').val();
        if (packageType === '' && $('#packageType').attr("readonly") !== 'readonly') {
            alertMsgByFocus('Package Type field is required', '#packageType');
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
                                saveLclCommodity();
                            }
                        });
                    } else if (null !== data && data === "N" && $("#rtdTransaction", window.parent.document).val() === "Y") {
                        $("#rtdTransaction", window.parent.document).val("N");
                        $.prompt("ERT will change to No", {
                            callback: function () {
                                saveLclCommodity();
                            }
                        });
                    } else {
                        saveLclCommodity();
                    }
                }
            });
        } else {
            var osdRemark = $("#osdRemarks").val();
            var checkValue = $('input:radio[name=overShortdamaged]:checked').val();
            parent.$("overShortdamaged").val(checkValue);
            parent.$("#osdRemarks").val(osdRemark);
            if ($("#overShortdamagedY").val() === "Y") {
                parent.$("#osdRemarks").val(osdRemark);
            }
            isValidationByShortShipInBl();
        }
    });
    if (moduleName === 'Exports' && fileStatus !== 'WV' && (isCheckedReleaseStatus(fileNumberId)
            || fileStatus === 'L' || fileStatus === 'M')) {
        actualFieldsEnableDisable();
    }
    if (moduleName === 'Exports') {
        var commNo = $('#commodityNo').val();
        var smallParcel = parent.$("#ups").val() === "true" ? true : false;
        var commNo = $('#commodityNo').val();
        var oldComm = $("#oldTariffNo").val();
        var oldDestFee = $('#oldDestFee').val();
        if (!isvalOfcom(commNo)) {
            $('#includeDestY').attr('disabled', true);
            $('#includeDestN').attr('disabled', true);
        } else {
            $('#includeDestY').removeAttr("disabled");
            $('#includeDestN').removeAttr('disabled');
        }

        $("input:radio[name=ups]").val([smallParcel]);
    }
});

function isValidationByShortShipInBl() {//ShortShipMent Logic for Exports
    var moduleName = parent.$('#moduleName').val();
    var shortShipFileNo = $('#shortShipFileNo').val();
    var shortShipFileState = $('#shortShipOriginalState').val();
    var fileNumberId = $("#fileNumberId").val();
    var commodityChangesFlag = fileNumberId != "0" ? isRatesValidationForShortShip() : true;
    if (fileNumberId != 0 && moduleName === "Exports" && shortShipFileState === 'BL' && shortShipFileNo != '' && commodityChangesFlag) {
        $.prompt("The original DR has a Bill of Lading,such BL will not be updated automatically. If required, please update manually", {
            callback: function () {
                shortShipByCommodity(moduleName, commodityChangesFlag, fileNumberId);
            }
        });
    } else {
        shortShipByCommodity(moduleName, commodityChangesFlag, fileNumberId);
    }
}
function shortShipByCommodity(moduleName, commodityChangesFlag, fileNumberId) {//ShortShipMent Logic for Exports
    var shortShipFileNo = $('#shortShipFileNo').val();
    if (moduleName === "Exports" && shortShipFileNo != '' && commodityChangesFlag) {
        var actualPieceCount = fileNumberId != "0" ? $('#actualPieceCount').val() : $('#bookedPieceCount').val();
        var actualVolumeImperial = fileNumberId != "0" ? $('#actualVolumeImperial').val() : $('#bookedVolumeImperial').val();
        var actualWeightImperial = fileNumberId != "0" ? $('#actualWeightImperial').val() : $('#bookedWeightImperial').val();
        var shortShipFlag = isCheckedShortShip(shortShipFileNo, actualPieceCount,
                actualWeightImperial, actualVolumeImperial);
        if (shortShipFlag) {
            $.prompt("Please verify piece/cube/weight. Short Shipment is larger than Original DR");
            $('#saveCommodity').attr("disabled", false);
            return false;
        }
        saveLclCommodity();
    } else {
        saveLclCommodity();
    }
}

function saveLclCommodity() {
    var moduleName = parent.$('#moduleName').val();
    var fileNumberId = $("#fileNumberId").val();
    //id is equal to bkgPieceId
    var id = $('#id').val();
    var commNo = $('#commodityNo').val();
    var commName = $('#commName').val();
    var editDimFlag = $('#editDimFlag').val();
    var barrel = $("input[name='isBarrel']:checked").length > 0 ? $("input[name='isBarrel']:checked").val() : "";
    var commCode = $('#commCode').val();
    var methodName = "addLclCommodity";
    var origin = $("#originId").val();
    var destination = $("#destinationId").val();
    var rateType = $("#rateType").val();
    var pol = $("#pol").val();
    var pod = $("#pod").val();
    var commType = $('#commodityType').val();
    var actualVolumeMetric = $('#actualVolumeMetric').length > 0 ? $('#actualVolumeMetric').val() : "";
    var actualWeightMetric = $('#actualWeightMetric').length > 0 ? $('#actualWeightMetric').val() : "";
    var actPieceCount = $('#actPieceCount').length > 0 ? $('#actPieceCount').val() : "";
    var actualPieceCount = $('#actualPieceCount').length > 0 ? $('#actualPieceCount').val() : "";
    var actualVolumeImperial = $('#actualVolumeImperial').length > 0 ? $('#actualVolumeImperial').val() : "";
    var actualWeightImperial = $('#actualWeightImperial').length > 0 ? $('#actualWeightImperial').val() : "";
    var actVolumeImp = $('#actVolumeImp').length > 0 ? $('#actVolumeImp').val() : "";
    var actWeigtImp = $('#actWeigtImp').length > 0 ? $('#actWeigtImp').val() : "";
    if (fileNumberId === null || fileNumberId === '' || fileNumberId === undefined || fileNumberId === '0') {
        methodName = 'addCommodity';
    }
    var bookPieceCount = $('#bookPieceCount').length > 0 ? $('#bookPieceCount').val() : "";
    var bookVolumeImp = $('#bookVolumeImp').length > 0 ? $('#bookVolumeImp').val() : "";
    // var actHazmat = $('#actHazmat').length > 0 ? $('#actHazmat').val() : "";
    var hazmat = $("input[name='hazmat']:checked").length > 0 ? $("input[name='hazmat']:checked").val() : "";
    var bookWeigtImp = $('#bookWeigtImp').length > 0 ? $('#bookWeigtImp').val() : "";
    var bookedPieceCount = $('#bookedPieceCount').length > 0 ? $('#bookedPieceCount').val() : "";
    var bookedVolumeImperial = $('#bookedVolumeImperial').length > 0 ? $('#bookedVolumeImperial').val() : "";
    var bookedWeightImperial = $('#bookedWeightImperial').length > 0 ? $('#bookedWeightImperial').val() : "";
    var editbarrel = $('#barrel').length > 0 ? $('#barrel').val() : "";
    var pkgTyp = $.trim($("#packageType").val().toUpperCase());
    var packagPId = $('#packageTypeId').val();
    hazmat = $('#hazmatY').is(':checked') ? true : false;
    var isBarrel = (barrel === 'Y' ? true : false);
    if (barrel === 'Y' && (((pkgTyp !== 'BARREL' && pkgTyp !== 'BARRELS') && packagPId !== '')
            && ($('#actualPackageTypeId').val() !== "" && $('#actualPackageType').val() !== 'BARREL')) && commNo !== '') {
        $.prompt('Pkg type must be BARREL');
        $("#packageType").val("");
        $("#packageType").css("border-color", "red");
        $('#saveCommodity').attr("disabled", false);
        return false;
    }
    var fileStatus = $('#fileStatus').val() !== "" ? $('#fileStatus').val() : 'B';/* get file status*/
    if (moduleName === 'Exports' && fileStatus != 'B' && fileStatus != 'WU') {
        /* Actual values are required */
        var actualPackageId = $('#actualPackageTypeId').val();
        if (actualPieceCount === null || actualPieceCount === "") {
            alertMsgByFocus("Actual Piece Count is required", "#actualPieceCount");
            $('#saveCommodity').attr("disabled", false);
            return false;
        }
        if (actualPackageId === '' || actualPackageId === null || actualPackageId === '0') {
            alertMsgByFocus("Actual Package Type field is required", "#actualPackageType");
            $('#saveCommodity').attr("disabled", false);
            return false;
        }
        if ((actualVolumeImperial === null || actualVolumeImperial === "")) {
            alertMsgByFocus("Actual Volume values are required", "#actualVolumeImperial");
            $('#saveCommodity').attr("disabled", false);
            return false;
        }
        if ((actualWeightImperial === null || actualWeightImperial === "")) {
            alertMsgByFocus("Actual Weight values are required", "#actualWeightImperial");
            $('#saveCommodity').attr("disabled", false);
            return false;
        }
    }
    if (barrel === 'Y' && (pkgTyp !== '' || $('#actualPackageTypeId').val() !== "") && commNo !== null && commType !== null) {
        $.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkBarrelRate",
                param1: origin,
                param2: destination,
                param3: rateType,
                param4: commNo
            },
            async: false,
            success: function (data) {
                if (data === 'false') {
                    sampleAlert('BARREL rate is not available for this commodity.please select another commodity');
                    hideProgressBar();
                    $('#commodityType').val('');
                    $('#commodityNo').val('');
                    $('#saveCommodity').attr("disabled", false);
                    return false;
                } else {
                    if (fileNumberId === '0' && parent.$("#portOfOriginR").val() !== null && parent.$("#portOfOriginR").val() !== "") {
                        submitAjaxFormForRates('modifyMinimumRates', '#lclCommodityForm', '#chargeDesc', id, rateType, origin, destination, pol, pod, '', editDimFlag);
                    } else if (fileNumberId !== '0' && fileNumberId !== "" && fileNumberId !== null && fileNumberId !== undefined) {
                        var commCode = $('#commCode').val();
                        var commNo = $('#commodityNo').val();
                        var fileNumberId = $("#fileNumberId").val();
                        $.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.lcl.dwr.LclDwr",
                                methodName: "commodityNum",
                                param1: commNo,
                                param2: fileNumberId,
                                param3: commCode,
                                param4: hazmat,
                                param5: isBarrel,
                                param6: id,
                                request: "true",
                                dataType: "json"
                            },
                            async: false,
                            success: function (data) {
                                if (data) {
                                    sampleAlert("Commodity is already added. Please select different Commodity");
                                    $("#commodityType").val("");
                                    $('#commodityNo').val('');
                                    $("#commodityType").css("border-color", "red");
                                    $('#saveCommodity').attr("disabled", false);
                                    return false;
                                } else {
                                    if (commName === "") {
                                        submitAjaxFormForRates('modifyMinimumRates', '#lclCommodityForm', '#chargeDesc', id, rateType, origin, destination, pol, pod, '', editDimFlag);
                                    } else {
                                        $.ajaxx({
                                            dataType: "json",
                                            data: {
                                                className: "com.gp.cong.lcl.dwr.LclDwr",
                                                methodName: "deleteRate",
                                                param1: id,
                                                param2: fileNumberId,
                                                param3: bookPieceCount,
                                                param4: bookVolumeImp,
                                                param5: bookWeigtImp,
                                                param6: bookedPieceCount,
                                                param7: bookedVolumeImperial,
                                                param8: bookedWeightImperial,
                                                param9: commCode,
                                                param10: commNo,
                                                param11: editbarrel,
                                                param12: barrel,
                                                param13: actPieceCount,
                                                param14: actVolumeImp,
                                                param15: actWeigtImp,
                                                param16: actualWeightImperial,
                                                param17: actualVolumeImperial,
                                                param18: actualPieceCount,
                                                request: "true",
                                                dataType: "json"
                                            },
                                            async: false,
                                            success: function (data) {
                                                if (data === false) {
                                                    validationForActualFields(actualPieceCount, actualVolumeImperial, actualWeightImperial, bookedWeightImperial,
                                                            bookedWeightMetric, actualWeightMetric, bookedVolumeImperial, actualVolumeMetric, bookedVolumeMetric, fileNumberId,
                                                            rateType, origin, destination, pol, pod, methodName);
                                                }
                                                else {
                                                    if (commName === "") {
                                                        submitAjaxFormForRates('modifyMinimumRates', '#lclCommodityForm', '#chargeDesc', id, rateType, origin, destination, pol, pod, '', editDimFlag);
                                                    } else {
                                                        validateImperial(methodName, '#lclCommodityForm', '#commodityDesc', rateType, origin, destination, pol, pod);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    } else {
                        var commCode = $('#commCode').val();
                        var commNo = $('#commodityNo').val();
                        var fileNumberId = $("#fileNumberId").val();
                        var moduleName = parent.$('#moduleName').val();
                        $.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.lcl.dwr.LclDwr",
                                methodName: "commodityNum",
                                param1: commNo,
                                param2: fileNumberId,
                                param3: commCode,
                                param4: hazmat,
                                param5: isBarrel,
                                param6: id,
                                request: "true",
                                dataType: "json"
                            },
                            async: false,
                            success: function (data) {
                                if (data) {
                                    sampleAlert("Commodity is already added. Please select different Commodity");
                                    $("#commodityType").val("");
                                    $('#commodityNo').val('');
                                    $("#commodityType").css("border-color", "red");
                                    $('#saveCommodity').attr("disabled", false);
                                    return false;
                                } else {
                                    if (parent.$("#portOfOriginR").val() !== null && parent.$("#portOfOriginR").val() !== "") {
                                        submitAjaxFormForRates('modifyMinimumRates', '#lclCommodityForm', '#chargeDesc', id, rateType, origin, destination, pol, pod, '', editDimFlag);
                                    } else if (moduleName === 'Imports') {
                                        submitAjaxFormForImportBkg('addCommodity', '#lclCommodityForm');
                                    } else {
                                        validateImperial(methodName, '#lclCommodityForm', '#commodityDesc', rateType, origin, destination, pol, pod);
                                    }
                                }
                            }
                        });
                    }

                }
            }
        });
    } else {
        var bookedWeightMetric = $('#bookedWeightMetric').val();
        var bookedVolumeMetric = $('#bookedVolumeMetric').val();
        var editDimFlag = $('#editDimFlag').val();
        var commCode = $('#commCode').val();
        var packag = $('#packageType').val();
        var actualPackageTypeId = $('#actualPackageTypeId').val();
        if (commNo !== null && (packag !== '' || actualPackageTypeId !== '') && commType !== null) {
            if (fileNumberId !== "" && fileNumberId !== '0') {
                var commCode = $('#commCode').val();
                var commNo = $('#commodityNo').val();
                var fileNumberId = $("#fileNumberId").val();
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "commodityNum",
                        param1: commNo,
                        param2: fileNumberId,
                        param3: commCode,
                        param4: hazmat,
                        param5: isBarrel,
                        param6: id,
                        request: "true",
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        if (data) {
                            sampleAlert("Commodity is already added. Please select different Commodity");
                            $("#commodityType").val("");
                            $('#commodityNo').val('');
                            $("#commodityType").css("border-color", "red");
                            $('#saveCommodity').attr("disabled", false);
                            return false;
                        } else {
                            if (commName === "") {
                                submitAjaxFormForRates('modifyMinimumRates', '#lclCommodityForm', '#chargeDesc', id, rateType, origin, destination, pol, pod, '', editDimFlag);
                            }
                            else {
                                if (isRatesValidation()) {
                                    $.ajaxx({
                                        dataType: "json",
                                        data: {
                                            className: "com.gp.cong.lcl.dwr.LclDwr",
                                            methodName: "deleteRate",
                                            param1: id,
                                            param2: fileNumberId,
                                            param3: bookPieceCount,
                                            param4: bookVolumeImp,
                                            param5: bookWeigtImp,
                                            param6: bookedPieceCount,
                                            param7: bookedVolumeImperial,
                                            param8: bookedWeightImperial,
                                            param9: commCode,
                                            param10: commNo,
                                            param11: editbarrel,
                                            param12: barrel,
                                            param13: actPieceCount,
                                            param14: actVolumeImp,
                                            param15: actWeigtImp,
                                            param16: actualWeightImperial,
                                            param17: actualVolumeImperial,
                                            param18: actualPieceCount,
                                            request: "true",
                                            dataType: "json"
                                        },
                                        async: false,
                                        success: function (data) {
                                            if (data === false) {
                                                validationForActualFields(actualPieceCount, actualVolumeImperial, actualWeightImperial, bookedWeightImperial, bookedWeightMetric,
                                                        actualWeightMetric, bookedVolumeImperial, actualVolumeMetric, bookedVolumeMetric, fileNumberId, rateType, origin, destination, pol,
                                                        pod, methodName);
                                            }
                                            else {
                                                if (commName === "") {
                                                    submitAjaxFormForRates('modifyMinimumRates', '#lclCommodityForm', '#chargeDesc', id, rateType, origin, destination, pol, pod, '', editDimFlag);
                                                }
                                                else if (parent.$('#moduleName').val() === 'Imports') {
                                                    submitAjaxFormForImportBkg('addCommodity', '#lclCommodityForm');
                                                }
                                                else {
                                                    validateImperial(methodName, '#lclCommodityForm', '#commodityDesc', rateType, origin, destination, pol, pod);
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    var moduleName = parent.$('#moduleName').val();
                                    if (moduleName === 'Imports') {
                                        submitAjaxFormForImportBkg('addCommodity', '#lclCommodityForm');
                                    } else {
                                        validateImperial(methodName, '#lclCommodityForm', '#commodityDesc', rateType,
                                                origin, destination, pol, pod);
                                    }
                                }
                            }
                        }
                    }
                });
            } else {
                var commCode = $('#commCode').val();
                var commNo = $('#commodityNo').val();
                var fileNumberId = $("#fileNumberId").val();
                var moduleName = parent.$('#moduleName').val();
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "commodityNum",
                        param1: commNo,
                        param2: fileNumberId,
                        param3: commCode,
                        param4: hazmat,
                        param5: isBarrel,
                        param6: id,
                        request: "true",
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        if (data) {
                            sampleAlert("Commodity is already added. Please select different Commodity");
                            $("#commodityType").val("");
                            $('#commodityNo').val('');
                            $("#commodityType").css("border-color", "red");
                            $('#saveCommodity').attr("disabled", false);
                            return false;
                        } else {
                            if (fileNumberId === '0' && parent.$("#portOfOriginR").val() !== null && parent.$("#portOfOriginR").val() !== "") {
                                submitAjaxFormForRates('modifyMinimumRates', '#lclCommodityForm', '#chargeDesc', id, rateType, origin, destination, pol, pod, '', editDimFlag);
                            } else if (moduleName === 'Imports') {
                                submitAjaxFormForImportBkg('addCommodity', '#lclCommodityForm');
                            } else {
                                validateImperial(methodName, '#lclCommodityForm', '#commodityDesc', rateType, origin, destination, pol, pod);
                            }
                        }
                    }
                });
            }
        }
    }
}

function submitAjaxFormForRates(methodName, formName, selector, id, rateType, origin, destination, pol, pod, ok, editDimFlag) {
    showPreloading();
    var validateImpRatesText = "";
    $('#saveCommodity').attr("disabled", true);
    methodName = 'addLclCommodity';
    var fileNumberId = $("#fileNumberId").val();
    if (fileNumberId === null || fileNumberId === '' || fileNumberId === undefined || fileNumberId === '0') {
        methodName = 'addCommodity';
    } else {
        isRatesValidation();
    }
    $("#methodName").val(methodName);
    $("#unitStatus").val(parent.$("#unitStatus").val());
    var params = $("#lclCommodityForm").serialize();
    $.post($("#lclCommodityForm").attr("action"), params,
            function (data) {
                $("#commodityDesc").html(data);
                $("#commodityDesc", window.parent.document).html(data);
                $("#methodName").val("modifyMinimumRates");
                document.getElementById('calcHeavy').value = parent.$('input:radio[name=calcHeavy]:checked').val();
                var params1 = $("#lclCommodityForm").serialize();
                $.post($("#lclCommodityForm").attr("action"), params1,
                        function (data) {
                            $("#chargeDesc").html(data);
                            $("#chargeDesc", window.parent.document).html(data);
                            validateImpRatesText = parent.$("#validateImpRates").val();
                            if (validateImpRatesText.trim() !== "" && validateImpRatesText.trim() !== undefined && validateImpRatesText.trim() != null) {
                                validateImpRates(validateImpRatesText);
                            } else {
                                showLoading();
                                parentMethodName = parent.$("#isFormChanged").val() === 'true'
                                        || (null === fileNumberId || "" === fileNumberId || fileNumberId === '0')
                                        ? "saveBooking" : "editBooking";
                                parent.$("#methodName").val(parentMethodName);
                                $.fn.colorbox.close();
                                parent.$("#lclBookingForm").submit();
                            }
                        });
            });
}

function submitAjaxFormForImportBkg(methodName, formName) {
    submitAjaxFormForRates(methodName, formName);
}

function validateImperial(methodName, lclCommodityForm, commodityDesc, rateType, origin, destination, pol, pod) {
    var fileId = document.getElementById('fileNumberId').value;
    var bookedVolumeImperial = $('#bookedVolumeImperial').length > 0 ? $('#bookedVolumeImperial').val() : "";
    var bookedWeightImperial = $('#bookedWeightImperial').length > 0 ? $('#bookedWeightImperial').val() : "";
    var actualVolumeImperial = $('#actualVolumeImperial').length > 0 ? $('#actualVolumeImperial').val() : "";
    var actualWeightImperial = $('#actualWeightImperial').length > 0 ? $('#actualWeightImperial').val() : "";
    var bookedWeightMetric = $('#bookedWeightMetric').length > 0 ? $('#bookedWeightMetric').val() : "";
    var actualWeightMetric = $('#actualWeightMetric').length > 0 ? $('#actualWeightMetric').val() : "";
    var actualVolumeMetric = $('#actualVolumeMetric').length > 0 ? $('#actualVolumeMetric').val() : "";
    var bookedVolumeMetric = $('#bookedVolumeMetric').length > 0 ? $('#bookedVolumeMetric').val() : "";
    var fileNumberId = $('#fileNumberId').val();
    var moduleName = parent.$('#moduleName').val();
    var actualPieceCount = $('#actualPieceCount').length > 0 ? $('#actualPieceCount').val() : "";
    var bookingStatus = document.getElementById('status').value;
    if (bookingStatus === "WV" || bookingStatus === "R" || bookingStatus === "PR") {
        validationForActualFields(actualPieceCount, actualVolumeImperial, actualWeightImperial, bookedWeightImperial, bookedWeightMetric,
                actualWeightMetric, bookedVolumeImperial, actualVolumeMetric, bookedVolumeMetric, fileNumberId, rateType, origin, destination, pol, pod, methodName);
    } else {
        if (fileNumberId != 0 && moduleName === "Exports" && isRatesValidation()) {
            deletingRates('AutoRates will be removed. Manual Rates will be recalculated Are you Sure you want to Continue?', fileNumberId, rateType, origin, destination, pol, pod);
            return false;
        }
        submitAjaxForm(methodName, lclCommodityForm, commodityDesc, '', rateType, origin, destination, pol, pod, '');
    }
}

function validationForActualFields(actualPieceCount, actualVolumeImperial, actualWeightImperial, bookedWeightImperial,
        bookedWeightMetric, actualWeightMetric, bookedVolumeImperial, actualVolumeMetric, bookedVolumeMetric, fileNumberId,
        rateType, origin, destination, pol, pod, methodName) {
    var bookingStatus = document.getElementById('status').value;
    var unitStatus = parent.$("#unitStatus").val();
    var yesVerifiedClicked = $('#verifyCargo').val();
    var moduleName = parent.$('#moduleName').val();
    var fileStatus = $('#fileStatus').val() !== "" ? $('#fileStatus').val() : 'B';
    if (moduleName === "Exports" && fileStatus != 'B' && fileStatus != 'WU') {
        var actualPackageId = $('#actualPackageTypeId').val();
        if (actualPackageId === '') {
            alertMsgByFocus("Actual Package Type field is required", "#actualPackageType");
            $('#saveCommodity').attr("disabled", false);
            return false;
        }
        if ((actualPieceCount === null || actualPieceCount === "") ||
                (actualVolumeImperial === null || actualVolumeImperial === "") ||
                (actualWeightImperial === null || actualWeightImperial === "")) {
            if ((actualPieceCount === null || actualPieceCount === "")) {
                alertMsgByFocus("Actual Piece Count is required", "#actualPieceCount");
                $('#saveCommodity').attr("disabled", false);
                return false;
            }
            if ((actualVolumeImperial === null || actualVolumeImperial === "")) {
                alertMsgByFocus("Actual Volume values are required", "#actualVolumeImperial");
                $('#saveCommodity').attr("disabled", false);
                return false;
            }
            if ((actualWeightImperial === null || actualWeightImperial === "")) {
                alertMsgByFocus("Actual Weight values are required", "#actualWeightImperial");
                $('#saveCommodity').attr("disabled", false);
                return false;
            }
        } else {
            if (isRatesValidation()) {
                if (yesVerifiedClicked === 'V') {
                    if (((bookedWeightImperial !== null && actualWeightImperial !== null) && (bookedWeightMetric !== null && actualWeightMetric !== null) && (bookedWeightImperial !== actualWeightImperial)) && ((bookedVolumeImperial !== null && actualVolumeImperial !== null) && (bookedVolumeMetric !== null && actualVolumeMetric !== null) && (bookedVolumeImperial !== actualVolumeImperial))) {
                        deletingRates('AutoRates will be removed. Manual Rates will be recalculated because both Actual Cube and Weight are Different than Booked.Are you Sure you want to Continue?', fileNumberId, rateType, origin, destination, pol, pod);
                    } else if ((bookedVolumeImperial !== null && actualVolumeImperial !== null) && (bookedVolumeMetric !== null && actualVolumeMetric !== null) && (bookedVolumeImperial !== actualVolumeImperial)) {
                        deletingRates('AutoRates will be removed. Manual Rates will be recalculated because Actual Cube are Different than Booked.Are you Sure you want to Continue?', fileNumberId, rateType, origin, destination, pol, pod);
                    }
                    else if ((bookedWeightImperial !== null && actualWeightImperial !== null) && (bookedWeightMetric !== null && actualWeightMetric !== null) && (bookedWeightImperial !== actualWeightImperial)) {
                        deletingRates('AutoRates will be removed. Manual Rates will be recalculated because Actual Weight are Different than Booked.Are you Sure you want to Continue?', fileNumberId, rateType, origin, destination, pol, pod);
                    } else {
                        deletingRates('AutoRates will be removed. Manual Rates will be recalculated.Are you Sure you want to Continue?', fileNumberId, rateType, origin, destination, pol, pod);
                    }
                } else {
                    deletingRates('AutoRates  will be removed. Manual Rates will be recalculated.Are you Sure you want to Continue?', fileNumberId, rateType, origin, destination, pol, pod);
                }
            } else {
                submitAjaxForm(methodName, '#lclCommodityForm', '#commodityDesc', '', rateType, origin, destination, pol, pod, '');
            }

        }
    }
    else {
        if (isRatesValidation()) {
            if (fileNumberId !== null || fileNumberId !== "" || fileNumberId !== "0") {
                if (moduleName === 'Imports' && unitStatus === 'M' && bookingStatus === 'M') {
                    impCharges('Charges will not be modified, you must issue a Quick CN if you wish to change the charges', fileNumberId, rateType, origin, destination, pol, pod, methodName);
                } else {
                    deletingRates('AutoRates will be removed. Manual Rates will be recalculated.Are you Sure you want to Continue?', fileNumberId, rateType, origin, destination, pol, pod);
                }
            }
            else {
                submitAjaxForm(methodName, '#lclCommodityForm', '#commodityDesc', '', rateType, origin, destination, pol, pod, '');
            }
        } else {
            submitAjaxForm(methodName, '#lclCommodityForm', '#commodityDesc', '', rateType, origin, destination, pol, pod, '');
        }
    }
}

function impCharges(txt, fileNumberId, rateType, origin, destination, pol, pod, methodName) {
    var moduleName = parent.$('#moduleName').val();
    $.prompt(txt, {
        buttons: {
            Ok: 1
        },
        submit: function (v) {
            if (v === 1) {
                if (moduleName === 'Imports') {
                    showProgressBar();
                    submitAjaxFormByDisposition(methodName, '#lclCommodityForm', '#commodityDesc', '', rateType, origin, destination, pol, pod, '');
                    $.prompt.close();
                    hideProgressBar();
                }
            }
        }
    });
}


function submitAjaxFormByDisposition(methodName, formName, selector, id, rateType, origin, destination, pol, pod, ok, editDimFlag) {
    $("#methodName").val(methodName);
    var hazmat = $("input[name='hazmat']:checked").val();
    var params = $(formName).serialize();
    var postDRflag = "true";
    var fromZip = parent.$("#doorOriginCityZip").val();
    var fileNumberId = $('#fileNumberId').val();
    showLoading();
    params += "&id=" + id + "&fileNumberId=" + fileNumberId + "&rateType=" + rateType;
    params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod + "&ok=" + ok + "editDimFlag=" + editDimFlag + "&hazmat=" + hazmat;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                //************************calling modifyminimumrates function but not changing rates but changing cube and weight values
                $("#methodName").val("modifyMinimumRates");
                var params1 = $("#lclCommodityForm").serialize();
                params1 += "&id=" + fileNumberId + "&destination=" + destination + "&postDRflag=" + postDRflag + "&fromZip=" + fromZip;
                $.post($("#lclCommodityForm").attr("action"), params1,
                        function (data) {
                            $("#chargeDesc").html(data);
                            $("#chargeDesc", window.parent.document).html(data);
                            parent.$.fn.colorbox.close();
                        });
                //*****************************************************************
                //  submitForCargoButton('showCargo', '#lclCommodityForm', '#cargoReceivedId');
            });
    checkActualBookedValues();
}

function checkActualBookedValues() {
    var fileNumberId = $('#fileNumberId').val();
    var actualPieceCount = $('#actualPieceCount').val();
    var actualVolumeImperial = $('#actualVolumeImperial').val();
    var actualVolumeMetric = $('#actualVolumeMetric').val();
    var actualWeightImperial = $('#actualWeightImperial').val();
    var actualWeightMetric = $('#actualWeightMetric').val();
    var bookedPieceCount = $('#bookedPieceCount').val();
    var bookedVolumeImperial = $('#bookedVolumeImperial').val();
    var bookedVolumeMetric = $('#bookedVolumeMetric').val();
    var bookedWeightImperial = $('#bookedWeightImperial').val();
    var bookedWeightMetric = $('#bookedWeightMetric').val();
    var yesVerifiedClicked = $('#verifyCargo').val();
    if (fileNumberId !== null && fileNumberId !== "" && fileNumberId !== "0" && yesVerifiedClicked === 'V') {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkBookedActualValues",
                param1: fileNumberId,
                param2: actualPieceCount,
                param3: actualVolumeImperial,
                param4: actualWeightImperial,
                param5: actualWeightMetric,
                param6: actualVolumeMetric,
                param7: bookedPieceCount,
                param8: bookedVolumeImperial,
                param9: bookedVolumeMetric,
                param10: bookedWeightImperial,
                param11: bookedWeightMetric,
                request: "true",
                dataType: "json"
            },
            success: function (data) {
            }
        });
    }
}

function submitForm(methodName, formName) {
    $("#methodName").val(methodName);
    $(formName).submit();
}
function sampleAlert(txt) {
    $.prompt(txt);
}

function submitAjaxForm(methodName, formName, selector, id, rateType, origin, destination, pol, pod, ok, editDimFlag) {
    var moduleName = parent.$('#moduleName').val();
    var package = $('#packageTypeId').val();
    $('#saveCommodity').attr("disabled", true);
    $("#methodName").val(methodName);
    if ($("#cargoReceived").val() === 'true') {
        showLoading();
        $("#ok").val("ok");
        $("#methodName").val("addLclCommodity");
        $("#editDimFlag").val("false");
        $("#lclCommodityForm").submit();
    } else {
        showLoading();
        var hazmat = $("input[name='hazmat']:checked").val();
        var params = $(formName).serialize();
        var fileNumberId = $('#fileNumberId').val();
        params += "&id=" + id + "&fileNumberId=" + fileNumberId + "&rateType=" + rateType;
        params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod + "&ok=" + ok + "editDimFlag=" + editDimFlag + "&hazmat=" + hazmat + "&packageTypeId=" + package;
        $.post($(formName).attr("action"), params,
                function (data) {
                    $(selector).html(data);
                    $(selector, window.parent.document).html(data);
                    parent.$.fn.colorbox.close();
                    // submitForCargoButton('showCargo', '#lclCommodityForm', '#cargoReceivedId');
                });
        if ((methodName === 'addLclCommodity' || methodName === 'addCommodity')
                && (null === fileNumberId || "" === fileNumberId || fileNumberId === '0')) {
            if (moduleName === "Exports" && parent.$("#portOfOriginR").val() !== "") {
                parent.submitForm("saveBooking");
            }
        }

        if (moduleName !== 'Imports') {
            checkActualBookedValues();
        } else {
            defaultErtValues();
        }
    }
}

function submitAjaxFormForCharge(methodName, formName, selector, fileNumberId, destination) {
    showLoading();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    var fileNumberId = $('#fileNumberId').val();
    params += "&id=" + fileNumberId + "&destination=" + destination;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                parent.$.fn.colorbox.close();
            });
}
function submitForCargoButton(methodName, formName, selector) {
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
            });
}
function checkForNumberAndDecimal(obj) {
    var result;
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        sampleAlert("This field should be Numeric");

    }
}

function checkForNumber(obj) {
    if (!/^([0-9]{0,10})$/.test(obj.value)) {
        obj.value = "";
        sampleAlert("This field should be Numeric");
    }
}

function openDetails(path, bookingPieceId, editDimFlag, dimFlag) {
    var commodityNo = $('#commodityNo').val();
    var fileNumberId = $('#fileNumberId').val();
    var fileNumber = parent.$('#fileNumber').val();
    var packageType = $('#packageType').val();
    var packageTypeId = $('#packageTypeId').val();
    var copyFnVal = parent.$('#copyFnVal').val();
    var dispo = parent.$("#exportDisposition").val();
    var cargoReceived = $('#cargoReceived').val();
    var verifiedIds = $('#verifiedIds').val();
    if (bookingPieceId === '') {
        bookingPieceId = $('#id').val();
    }
    if (editDimFlag === '') {
        editDimFlag = $('#editDimFlag').val();
    }
    var origin = parent.getOrigin();
    var transhipment = "";
    var destination = parent.getDestination();
    if (destination === '007UN') {
        destination = 'PRSJU';
    }
    var rateType = parent.$("input:radio[name='rateType']:checked").val();
    var moduleName = parent.$('#moduleName').val();
    if (moduleName === 'Imports') {
        transhipment = parent.$("input:radio[name='transShipMent']:checked").val();
    }
    var href = path + "/commodityDetails.do?methodName=display&bookingPieceId=" + bookingPieceId + "&editDimFlag=" + editDimFlag + "&dimFlag=" + dimFlag + "&commodityNo=" + commodityNo + "&fileNumberId=" + fileNumberId + "&packageTypeId=" + packageTypeId + "&packageType=" + packageType + "&fileNumber=" + fileNumber;
    $(".details").attr("href", href);
    $(".details").colorbox({
        iframe: true,
        width: "60%",
        height: "75%",
        title: "Commodity Details",
        onClosed: function () {
            showProgressBar();
            var status = $("#status").val();
            if (((status === 'W' && dispo === 'RCVD') || status === 'WV' || status === 'R' || status === 'PR') && moduleName !== 'Imports') {
                parent.$('#cboxClose').hide();
            }
            if (status !== 'WV' && bookingPieceId !== null && bookingPieceId !== undefined && bookingPieceId !== "") {
                href = path + "/lclCommodity.do?methodName=editLclCommodity&id=" + bookingPieceId + "&editDimFlag=" + editDimFlag + "&fileNumber=" + fileNumber + "&shortShipFileNo=" + $('#shortShipFileNo').val();
                href = href + "&origin=" + origin + "&destination=" + destination + "&rateType=" + rateType + "&fileNumberId=" + fileNumberId + "&copyVal=" + copyFnVal + "&cargoReceived=" + cargoReceived + "&verifiedIds=" + verifiedIds;
                href = href + "&transhipment=" + transhipment + "&moduleName=" + moduleName + "&status=" + $('#status').val() + "&disposition=" + dispo + "&status1=" + $('#status').val();
                document.location.href = href;
                hideProgressBar();
            } else {
                if (status === 'WV') {
                    var commNo = $('#commodityNo').val();
                    if (isvalOfcom(commNo)) {
                        $('#includeDestY').removeAttr("disabled");
                        $('#includeDestN').removeAttr('disabled');
                    }
                    $('#container').hide();
                }
                hideProgressBar();
            }
        }
    });
}
//-------------DONE
function actualCommodityFlag(fileId) {
    var bookingStatus = document.getElementById('fileStatus').value;
    var moduleName = $('#moduleName').val();
    var dispo = parent.$("#exportDisposition").val();
    if (((bookingStatus === 'W' && (dispo === 'RCVD' || dispo === 'INTR')) || dispo === 'RCVD' || bookingStatus === 'WV' || bookingStatus === 'R'
            || bookingStatus === 'L' || bookingStatus === 'M') && moduleName !== 'Imports') {
        $('#actualField').show();
        $('#container').show();
        $('.booked').addClass("textlabelsBoldForTextBoxDisabledLook");
        $('.booked').attr("readonly", true);
        $('#bookedDim').hide();
        $('#toggle-measure').hide();
        $('#actualRow').show();
        $("#0").click();
    } else {
        $('#actualField').hide();
        $('#container').hide();
        $('#actualRow').hide();
    }
}
function cargoCommodity(path, fileNumberId, fileNumber, tabFlag, origin, destination, rateType, verifyCargo, status, cargoReceived, verifiedIds) {
    var moduleName = parent.$('#moduleName').val();
    var editDimFlag = 'false';
    var href = path + "/lclCommodity.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber +
            "&tabFlag=" + tabFlag + "&editDimFlag=" + editDimFlag + "&origin=" + origin + "&destination=" + destination +
            "&rateType=" + rateType + "&verifyCargo=" + verifyCargo + "&moduleName=" + moduleName + "&status=" + status +
            "&cargoReceived=" + cargoReceived + "&verifiedIds=" + verifiedIds;
    parent.$('#cboxClose').hide();
    parent.$.colorbox({
        iframe: true,
        href: href,
        width: "92%",
        height: "98%",
        title: "Commodity"
    });
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

function changePostDRcommodity() {
    var moduleName = parent.$('#moduleName').val();
    var fileStatus = parent.$('#fileStatus').val();
    if (moduleName === 'Imports' && fileStatus === 'M') {
        $.prompt('Charges/rates will not be changed since DR is posted, you must manually change them using Quick CN.');
    }
}

function setPackageTypeQuery(obj) {
    var val = obj.value;
    if (val <= 1 || val === null || val === undefined) {
        $('#packageType').attr('alt', 'COMMODITY_PACKAGE_TYPE');
        $('#actualPackageType').attr('alt', 'COMMODITY_PACKAGE_TYPE');
    } else {
        $('#packageType').attr('alt', 'PACKAGE_TYPE_PLURAL');
        $('#actualPackageType').attr('alt', 'PACKAGE_TYPE_PLURAL');
    }
}

function checkCommodity() {
    var haz = $('#hazmat').val();
    var existHazmat = $('#existHazmat').val();
    var fileId = $('#fileNumberId').val();
    var fileLong = parent.$('#fileNumberId').val().toString();
    if (existHazmat !== "true" && haz === 'true') {
        $('.hazYes').attr('checked', true);
        if (fileId !== "" && fileId !== null && fileId !== '0') {
            $("#hazmatY").click();
        }
        else {
            calculateHazmatRates();
        }
        parent.$('#hazmatFound').html("<span style='color:red;font-weight:bold;'>** HAZ **</span>");
        parent.$('#hazmatFound1').html("<span style='color:red;font-weight:bold;'>** HAZ **</span>");
    }
    if ($('#moduleName').val() === 'Exports') {
        var brand = parent.getTradingPartnerPriority();
        if (brand === "None") {
            parent.$("#eculineCommodity").val("ECULINE_OR_ECONO_FROM_COMMODITY");
        }
        if ($('#pieceDesc').val() === "") {
            $('#pieceDesc').val($('#commodityType').val());
        }
        var commNo = $('#commodityNo').val();
        var oldComm = $("#oldTariffNo").val();
        if (isClassOfComm(fileLong, commNo, oldComm)) {
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

function checkCommodityType() {
    var commNo = $('#commodityNo').val();
    var fileNumberId = $("#fileNumberId").val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "commodityNum",
            param1: commNo,
            param2: fileNumberId,
            param3: "",
            param4: "",
            param5: "",
            param6: "",
            request: "true",
            dataType: "json"
        },
        success: function (data) {
            if (data === true) {
                sampleAlert("This field should be Numeric");
                return false;
            }
        }
    });
}

function deletingRates(txt, fileNumberId, rateType, origin, destination, pol, pod) {
    var bookedVolumeImperial = $('#bookedVolumeImperial').length > 0 ? $('#bookedVolumeImperial').val() : "";
    var fileId = $('#fileNumberId').val();
    var bookedWeightImperial = $('#bookedWeightImperial').length > 0 ? $('#bookedWeightImperial').val() : "";
    var actualVolumeImperial = $('#actualVolumeImperial').length > 0 ? $('#actualVolumeImperial').val() : "";
    var actualWeightImperial = $('#actualWeightImperial').length > 0 ? $('#actualWeightImperial').val() : "";
    var actualVolumeMetric = $('#actualVolumeMetric').length > 0 ? $('#actualVolumeMetric').val() : "";
    var bookedVolumeMetric = $('#bookedVolumeMetric').length > 0 ? $('#bookedVolumeMetric').val() : "";
    var actualWeightMetric = $('#actualWeightMetric').length > 0 ? $('#actualWeightMetric').val() : "";
    var bookedWeightMetric = $('#bookedWeightMetric').length > 0 ? $('#bookedWeightMetric').val() : "";
    var actualPieceCount = $('#actualPieceCount').length > 0 ? $('#actualPieceCount').val() : "";
    if ((Number(actualVolumeImperial) >= 100000 || Number(bookedVolumeImperial) >= 100000) || (Number(actualVolumeMetric) >= 2831.710 || Number(bookedVolumeMetric) >= 2831.710)) {
        sampleAlert("Shipment Size must be Less than 100,000 CFT");

    }
    else if ((Number(actualWeightImperial) >= 100000 || Number(bookedWeightImperial) >= 100000) || (Number(actualWeightMetric) >= 45359.249 || Number(bookedWeightMetric) >= 45359.249)) {
        sampleAlert("Shipment Size must be Less than 100,000 LBS");
    }
    else {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "verifyCargoReceived",
                param1: fileId,
                param2: bookedVolumeImperial,
                param3: bookedWeightImperial,
                param4: actualVolumeImperial,
                param5: actualWeightImperial,
                request: "true",
                dataType: "json"
            },
            success: function (data) {
                if (data) {
                    if ((actualPieceCount === null || actualPieceCount === "") && (actualVolumeImperial === null || actualVolumeImperial === "") && (actualWeightImperial === null || actualWeightImperial === "")) {
                        sampleAlert("Actual Fields are required");
                        return false;
                    } else {
                        saveCommodity(txt, fileNumberId, rateType, origin, destination, pol, pod);
                    }
                }
                else {
                    var fileState = parent.$('#fileState').val();
                    var moduleName = parent.$('#moduleName').val();
                    var bookingStatus = document.getElementById('status').value;
                    if (moduleName === 'Exports' && fileState === 'BL' && $("#id").val() !== "") {
                        copyActualValueBl(txt, fileNumberId, rateType, origin, destination, pol, pod, bookingStatus);
                    }
                    else {
                        saveCommodity(txt, fileNumberId, rateType, origin, destination, pol, pod);
                    }
                }
            }
        });
    }
}

function saveCommodity(txt, fileNumberId, rateType, origin, destination, pol, pod) {
    var haz = $("input[name='hazmat']:checked").val();
    var validateImpRatesText = "";
    var package = $('#packageTypeId').val();
    $('#saveCommodity').attr("disabled", true);
    $.prompt(txt, {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                if ($("#cargoReceived").val() === 'true') {
                    showLoading();
                    $("#ok").val("ok");
                    $("#methodName").val("addLclCommodity");
                    $("#editDimFlag").val("false");
                    $("#lclCommodityForm").submit();
                } else {
                    showLoading();
                    var ok = 'ok';
                    var editDimFlag = 'false';
                    var fromZip = parent.$("#doorOriginCityZip").val();
                    $('#ok').val(ok);
                    //refreshing the commodity div
                    $("#methodName").val("addLclCommodity");
                    $("#billToParty").val(parent.$('input:radio[name=billForm]:checked').val());
                    var params = $("#lclCommodityForm").serialize();
                    params += "&id=" + '' + "&fileNumberId=" + fileNumberId + "&rateType=" + rateType + "&haz=" + haz;
                    params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod + "&ok=" + ok + "&editDimFlag=" + editDimFlag + "&packageTypeId=" + package;
                    $.post($("#lclCommodityForm").attr("action"), params,
                            function (data) {
                                $("#commodityDesc").html(data);
                                $("#commodityDesc", window.parent.document).html(data);
                                $("#methodName").val("modifyMinimumRates");
                                var params1 = $("#lclCommodityForm").serialize();
                                params1 += "&id=" + fileNumberId + "&destination=" + destination + "&fromZip=" + fromZip;
                                $.post($("#lclCommodityForm").attr("action"), params1,
                                        function (data) {
                                            $("#chargeDesc").html(data);
                                            $("#chargeDesc", window.parent.document).html(data);
                                            validateImpRatesText = parent.$("#validateImpRates").val();
                                            if (validateImpRatesText.trim() !== "" && validateImpRatesText.trim() !== undefined && validateImpRatesText.trim() != null) {
                                                validateImpRates(validateImpRatesText);
                                            } else {
                                                parentMethodName = parent.$("#isFormChanged").val() === 'true'
                                                        || (null === fileNumberId || "" === fileNumberId || fileNumberId === '0')
                                                        ? "saveBooking" : "editBooking";

                                                parent.$("#methodName").val(parentMethodName);
                                                $.fn.colorbox.close();
                                                parent.$("#lclBookingForm").submit();
                                            }
                                        });
                            });
                    checkActualBookedValues();
                    $.prompt.close();
                }
            }
            else if (v === 2) {
                $('#saveCommodity').attr("disabled", false);
                $.prompt.close();
            }
        }
    });
}

function FillAcualValues(txt) {
    $.prompt(txt, {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                var bookedPieceCount = $('#bookedPieceCount').val();
                var bookedVolumeImperial = $('#bookedVolumeImperial').val();
                var bookedVolumeMetric = $('#bookedVolumeMetric').val();
                var bookedWeightImperial = $('#bookedWeightImperial').val();
                var bookedWeightMetric = $('#bookedWeightMetric').val();
                $('#actualVolumeImperial').val(bookedVolumeImperial);
                $('#actualPieceCount').val(bookedPieceCount);
                $('#actualWeightImperial').val(bookedWeightImperial);
                $('#actualVolumeMetric').val(bookedVolumeMetric);
                $('#actualWeightMetric').val(bookedWeightMetric);
                hideProgressBar();
                $.prompt.close();
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });

}


function copySinPack() {
    var moduleName = parent.$('#moduleName').val();
    var bookingStatus = document.getElementById('status').value;
    if (bookingStatus === 'W' || bookingStatus === 'R') {
        $('#packageType').attr('name', '');
        $('#packageTypeId').attr('name', '');
    }
    if (moduleName === "Imports") {
        $('#bookedWeightMetric').focus().val();
    } else {
        var $weight_imperial = $("#bookedWeightImperial");
        var $volume_imperial = $("#bookedVolumeImperial");
        if ($weight_imperial.attr('readonly') && $volume_imperial.attr('readonly')) {
            $('#bookedWeightMetric').focus();
        } else {
            $('#bookedWeightImperial').focus();
        }
    }
}


function bookPieceCount() {
    $('#bookedPieceCount').focus().val();
    var count = $('#bookedPieceCount').val();
    if (count <= 1 || count === null || count === undefined) {
        $('#packageType').attr('alt', 'COMMODITY_PACKAGE_TYPE');
        $('#actualpackageType').attr('alt', 'COMMODITY_PACKAGE_TYPE');
    } else {
        $('#packageType').attr('alt', 'PACKAGE_TYPE_PLURAL');
        $('#actualPackageType').attr('alt', 'PACKAGE_TYPE_PLURAL');
    }
}

function checkSameCommodity() {
    var commCode = $('#commCode').val();
    var commNo = $('#commodityNo').val();
    var fileNumberId = $('#fileNumberId').val();
    var barrel = $("input[name='isBarrel']:checked").length > 0 ? $("input[name='isBarrel']:checked").val() : "";
    var isBarrel = (barrel === 'Y' ? true : false);
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "commodityNum",
            param1: commNo,
            param2: fileNumberId,
            param3: commCode,
            param4: "",
            param5: isBarrel,
            param6: "",
            request: "true",
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data === true) {
                sampleAlert("Commodity is already added. Please select different Commodity");
                $("#commodityType").val("");
                $('#commodityNo').val('');
                $("#commodityType").css("border-color", "red");
                return false;
            }
        }
    });
    $('#harmonizedCode').focus().val();
    var spotRate = parent.$('input:radio[name=spotRate]:checked').val();
    var moduleName = $('#moduleName').val();
    if (moduleName === 'Exports' && spotRate !== undefined && spotRate === 'Y') {
        if ($("#commodityNo").val() !== '' && $("#commodityNo").val() !== '212600') {
            sampleAlert("Cannot change commodity as Spot Rate is set to Yes.");
            $("#commodityNo").val($('#oldTariffNo').val());
            $("#commodityType").val($('#oldcommodity').val());
            $("#commodityTypeId").val($('#oldcommodityId').val());
        }
    }
}

function correctBookedVolumeMetric(obj) {
    if (obj.value > 50) {
        $.prompt('Are you sure you have the correct amount?', {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    if ($('#autoConvert').is(":checked")) {
                        if (!isNaN(parseFloat($("#bookedVolumeMetric").val()) * 35.3146)) {
                            if (parent.$('#moduleName').val() === 'Imports') {
                                $('#bookedVolumeImperial').val((parseFloat($("#bookedVolumeMetric").val()) * 35.314).toFixed(3));
                            } else {
                                $('#bookedVolumeImperial').val((parseFloat($("#bookedVolumeMetric").val()) * 35.314).toFixed(2));
                            }
                        }
                    }
                    $.prompt.close();
                }
                else if (v === 2) {
                    $("#bookedVolumeImperial").val("");
                    $("#bookedVolumeMetric").val("");
                    $("#bookedVolumeMetric").css("border-color", "red");
                    $(this).focus();
                    $.prompt.close();
                }
            }
        });
    }
}

function correctBookedWeightMetric(obj) {
    if (obj.value > 20000) {
        $.prompt('Are you sure you have the correct amount?', {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    $.prompt.close();
                    if ($('#autoConvert').is(":checked")) {
                        if (!isNaN(parseFloat($("#bookedWeightMetric").val()) * 2.2046)) {
                            $('#bookedWeightImperial').val((parseFloat($("#bookedWeightMetric").val()) * 2.2046).toFixed(3));
                        }
                    }
                }
                else if (v === 2) {
                    $("#bookedWeightImperial").val("");
                    $("#bookedWeightMetric").val("");
                    $("#bookedWeightMetric").css("border-color", "red");
                    $(this).focus();
                    $.prompt.close();
                }
            }
        });
    }
}
function checkActualVolumeImperial(obj) {
    if (obj.value >= 100000) {
        sampleAlert('Shipment Size must be Less than 100,000 CFT');
        $("#actualVolumeImperial").val("");
        $("#actualVolumeMetric").val("");
        $("#actualVolumeImperial").css("border-color", "red");
        $(this).focus();
    }
}

function checkActualVolumeMetric(obj) {
    if (obj.value >= 2831.710) {
        sampleAlert('Shipment Size must be Less than 100,000 CFT');
        $("#actualVolumeImperial").val("");
        $("#actualVolumeMetric").val("");
        $("#actualVolumeMetric").css("border-color", "red");
        $(this).focus();
    }
}

function checkActualWeightImperial(obj) {
    if (obj.value >= 100000) {
        sampleAlert('Shipment Size must be Less than 100,000 LBS');
        $("#actualWeightImperial").val("");
        $("#actualWeightMetric").val("");
        $("#actualWeightImperial").css("border-color", "red");
        $(this).focus();
    }
}

function checkActualWeightMetric(obj) {
    if (obj.value >= 45359.249) {
        sampleAlert('Shipment Size must be Less than 100,000 LBS');
        $("#actualWeightImperial").val("");
        $("#actualWeightMetric").val("");
        $("#actualWeightMetric").css("border-color", "red");
        $(this).focus();
    }
}

function calculateHazmatRates() {
    var fileId = $('#fileNumberId').val();
    var isHazmat = $("input[name='hazmat']:checked").val();
    var existHazValue = $('#existHazmat').val() === "true" ? "Y" : "N";
    if (isHazmat === existHazValue) {
        return false;
    }
    var podUnlocation = parent.$('#portOfDestination').val();
    var fdUnlocation = parent.$('#finalDestinationR').val();
    var podUnlocationcode = podUnlocation.substring(podUnlocation.indexOf("(") + 1, podUnlocation.indexOf(")"));
    var fdUnlocationcode = fdUnlocation.substring(fdUnlocation.indexOf("(") + 1, fdUnlocation.indexOf(")"));
    var fdCode = fdUnlocationcode === "" ? podUnlocationcode : fdUnlocationcode;
    var podCode = podUnlocationcode === "" ? fdUnlocationcode : podUnlocationcode;

    if (isHazmat === 'Y') {
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
                    hazMsg("We do not accept hazardous cargo to this market:" + data[1], false, isHazmat, fileId, existHazValue);
                } else if (data[0] === "R") {
                    hazMsg("Hazardous Restrictions apply for :" + data[1] + ". Please confirm that cargo is acceptable for export and determine proper charges.", true, isHazmat, fileId, existHazValue);
                }
                else {
                    calculateOrRemoveHazmatRates(isHazmat, fileId, existHazValue);
                }
            }
        });
    } else {
        calculateOrRemoveHazmatRates(isHazmat, fileId, existHazValue);
    }
}
function hazMsg(txtMsg, hazFlag, isHazmat, fileId, existHazValue) {
    $.prompt(txtMsg, {
        buttons: {
            Ok: 1
        },
        submit: function (v) {
            if (v === 1 && hazFlag) {
                calculateOrRemoveHazmatRates(isHazmat, fileId, existHazValue);
            }
            else {
                clearHazmatValues();
            }
        }
    });
}
function clearHazmatValues() {
    var haz = $('#hazmat').val();
    $('input:radio[name=hazmat]').val(["N"]);
    if (haz == 'true') {
        $("#commodityType").val("");
        $('#commodityNo').val('');
        $("#commodityType").css("border-color", "red");
    }
}
function calculateOrRemoveHazmatRates(isHazmat, fileId, existHazValue) {
    var txtMsg = isHazmat === "Y" ? "Hazmat Rates will be added.Are you sure you want to continue" :
            "Hazmat Rates will be removed.Are you sure you want to continue";
    $.prompt(txtMsg, {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                $('#existHazmat').val(isHazmat === "Y" ? 'true' : 'false');
                calculateByHazmatRates(isHazmat, fileId);
            } else if (v === 2) {
                $('input:radio[name=hazmat]').val([existHazValue]);
                $.prompt.close();
            }
        }
    });
}


function calculateByHazmatRates(isHazmat, fileId) {
    if (isHazmat === 'Y') {
        parent.$('#hazmatFound').html("<span style='color:red;font-weight:bold;'>** HAZ **</span>");
        parent.$('#hazmatFound1').html("<span style='color:red;font-weight:bold;'>** HAZ **</span>");
    } else {
        parent.$('#hazmatFound').html("");
        parent.$('#hazmatFound1').html("");
    }
    if (fileId !== "" && fileId !== null && fileId !== '0') {
        submitAjaxFormForHazmat('calculateHazmatCharge', '#lclCommodityForm', '#chargeDesc', isHazmat, fileId);
    }
}

function submitAjaxFormForHazmat(methodName, formName, selector, hazmat, fileId) {
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&hazmat=" + hazmat + "&fileNumberId=" + fileId + "&moduleName=" + $('#moduleName').val();
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
            });
}

function getPredefinedRemarks(path, multipleSelect, selector) {
    var moduleName = $('#moduleName').val();
    var url = path + '/lclBooking.do?methodName=getPredefinedRemarks&multipleSelect=' + multipleSelect
            + "&moduleName=" + moduleName + "&from=commodityPopup";
    $(selector).attr("href", url);
    $(selector).colorbox({
        iframe: true,
        width: "50%",
        height: "70%",
        title: "Predefined remarks"
    });
}

$(document).keydown(function (e) {
    if ($(e.target).attr("readonly")) {
        if (e.keyCode === 8) {
            return false;
        }
    }
});

function openTariffList(path) {
    var origin = $('#origin').val();
    var destination = $('#destination').val();
    var href = path + "/lclCommodity.do?methodName=displayTariffDetails&origin=" + origin + "&destination=" + destination;
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
    if (window.event && window.event.keyCode === 8 && activeElType !== 'text' && activeElType !== 'textarea') {
        // try to cancel the backspace
        window.event.cancelBubble = true;
        window.event.returnValue = false;
        return false;
    }
}
//Imports
function defaultErtValues() {
    var newComm = $("#commodityNo").val();
    var oldComm = $('#commCode').val();
    var origin = $("#originId").val();
    if (origin === '' || origin === null || origin === undefined) {
        origin = $("#polId").val();
    }
    var destination = $("#podId").val();
    if (destination === '' || destination === null || destination === undefined) {
        destination = $("#destinationId").val();
    }
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getdefaultErtValues",
            param1: newComm,
            param2: origin,
            param3: destination,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data) {
                parent.$('#rtdTransaction').val("Y");
                parent.$('#agentInfo').val(parent.$("#agentNumber").val());
                if (oldComm === '' && newComm !== '' || oldComm !== newComm) {
                    sampleAlert("ERT Field has been Changed");
                    return false;
                }
            } else {
                parent.$('#rtdTransaction').val("N");
                parent.$('#agentInfo').val('');
                return true;
            }
        }
    });
}

$(document).ready(function () {
    validateRateStandard();
    $('#bookedPieceCount').keydown(function (e) {
        var barrel = $("input[name='isBarrel']:checked").val();
        if (e.keyCode === 9) {
            if (barrel === 'Y') {
                $('#bookedWeightImperial').focus().val();
                return false;
            } else {
                $('#packageType').focus().val();
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

function isRatesValidation() {
    var moduleName = $('#moduleName').val();
    var existActWeiMet = $('#existActualWeightMetric').val();
    var existActVolMet = $('#existActualVolumeMetric').val();
    var existActWeiImp = $('#existActualWeightImperial').val();
    var existActVolImp = $('#existActualVolumeImperial').val();

    var existWeightMetric = (existActWeiMet === "" || existActWeiMet === undefined) ? $('#bkgWeightMetric').val() : existActWeiMet;
    var existVolumeMetric = (existActVolMet === "" || existActVolMet === undefined) ? $('#bkgVolumeMetric').val() : existActVolMet;
    var existWeightImp = (existActWeiImp === "" || existActWeiImp === undefined) ? $('#bkgWeightImperial').val() : existActWeiImp;
    var existVolumeImp = (existActVolImp === "" || existActVolImp === undefined) ? $('#bkgVolumeImperial').val() : existActVolImp;

    var actualWeiMet = $('#actualWeightMetric').val();
    var actualVolMet = $('#actualVolumeMetric').val();
    var actualWeiImp = $('#actualWeightImperial').val();
    var actualVolImp = $('#actualVolumeImperial').val();

    var weightMet = (actualWeiMet === "" || actualWeiMet === undefined) ? $('#bookedWeightMetric').val() : actualWeiMet;
    var volumeMet = (actualVolMet === "" || actualVolMet === undefined) ? $('#bookedVolumeMetric').val() : actualVolMet;
    var weightImp = (actualWeiImp === "" || actualWeiImp === undefined) ? $('#bookedWeightImperial').val() : actualWeiImp;
    var volumeImp = (actualVolImp === "" || actualVolImp === undefined) ? $('#bookedVolumeImperial').val() : actualVolImp;

    var oldTariffNo = $('#oldTariffNo').val();
    var newTariffNo = $('#commodityNo').val();
    var oldDestFee = $('#oldDestFee').val() === "true" ? 'Y' : 'N';
    var includeDestfees = $("input[name='includeDestfees']:checked").val();
    if ((parseFloat(existWeightMetric) !== parseFloat(weightMet)) || (parseFloat(existVolumeMetric) !== parseFloat(volumeMet)) ||
            (parseFloat(existWeightImp) !== parseFloat(weightImp)) || (parseFloat(existVolumeImp) !== parseFloat(volumeImp)) ||
            (oldTariffNo !== newTariffNo) || (moduleName === 'Exports' && oldDestFee !== includeDestfees)) {
        $('#ratesValidationFlag').val('true');
        return true;
    } else {
        $('#ratesValidationFlag').val('false');
    }
    return false;
}
function closePopUp() {
    parent.$.fn.colorbox.close();
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
                showPreloading();
                parent.$("#methodName").val('saveBooking');
                $.fn.colorbox.close();
                parent.$("#lclBookingForm").submit();
            }
        }
    });
}

function displayOsdBox() {//display osd box hide or show
    var osdValues = $('input:radio[name=overShortdamaged]:checked').val();
    if (osdValues === "Y") {
        $('#osdRemarksId').show();
        parent.$('input:radio[name=overShortdamaged]').val(['Y']);
    } else {
        $('#osdRemarksId').hide();
    }
}
function deleteOsdRemarks(txt, id) {//delete OSD Remarks
    var user =parent.$('#loginUserId').val();
    var fileState = parent.$('#fileState').val();
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
                        param3: user.toString(),
                        param4: fileState,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        var osdValues = $('input:radio[name=overShortdamaged]:checked').val();
                        if (osdValues === "N" && data === true) {
                            parent.$('input:radio[name=overShortdamaged]').val([osdValues]);
                            $('#osdRemarksId').hide();
                            $('#osdRemarks').val('');
                        }
                    }
                });
            } else if (v === 2) {
                $('input:radio[name=overShortdamaged]').val(['Y']);
            }
        }
    });
}

function addHotCode3pRef(fileId) {//add hot code
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
        addGeneralInfOn3pRef("hotCodesBox", "hotCodesList", "hotCodes", "hotCodes", fileId, "");
    }
}

function addHotCodeXXX3pRef() {
    var hotCodeXXXComments = $('#hotCodeComments').val();
    if (hotCodeXXXComments === null || hotCodeXXXComments === "") {
        $.prompt("Hot Code XXX Comments is required");
        return false;
    }
    var deleteFlag = $('#hiddenDeleteHotCodeFlag').val();
    var fileId = $("#fileNumberId").val();
    $("#add-hotCodeComments-container").center().hide(500, function () {
        hideAlternateMask();
    });
    if (deleteFlag === "deleteFlag") {
        submitAjaxFormforGeneral("deleteLcl3pReferenceComm", "hotCodes", $('#3pRefId').val(), "hotCodesList", hotCodeXXXComments);
    }
    else {
        addGeneralInfOn3pRef("hotCodesBox", "hotCodesList", "hotCodes", "hotCodes", fileId, hotCodeXXXComments);
    }
}

function addGeneralInfOn3pRef(hideSelectorText, selector, refType, refValue, fileId, comments) {
    showProgressBar();
    $('#' + hideSelectorText).hide();
    $('#methodName').val('addHotCodeTrackingComm');
    var params = $("#lclCommodityForm").serialize();
    params += "&thirdPName=" + refType + "&fileNumberId=" + fileId;
    params += "&refValue=" + $('#' + refValue).val() + "&hotCodeXXXComments=" + comments;
    $.post($("#lclCommodityForm").attr("action"), params, function (data) {
        $("#" + selector).html(data);
        $("#" + selector, window.parent.document).html(data);
        hideProgressBar();
        if ("EBL" === $('#' + refValue).val().substring(0, $('#' + refValue).val().indexOf("/"))) { // eculine logic for export
            parent.$("#eculine").attr("checked", true);
        }
        $('#' + refValue).val('');

    });
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

function submitTracking(tar, methodName, fileNumberId, selector, hsCodeFlag) {
    showProgressBar();
    if (hsCodeFlag !== 'hsCode') {
        $(tar).hide("");
        $('#methodName').val(methodName);
        var params = $("#lclCommodityForm").serialize();
        params += "&thirdPName=" + hsCodeFlag + "&fileNumberId=" + fileNumberId;
        $.post($("#lclCommodityForm").attr("action"), params, function (data) {
            $(selector).html(data);
            $(selector, window.parent.document).html(data);
            // if (hsCodeFlag === 'hotCodes') {
            //     $('#methodName').val("refreshCommodity");
            //     var params = $("#lclBookingForm").serialize();
            //     params += "fileNumberId=" + fileNumberId;
            //     $.post($("#lclBookingForm").attr("action"), params, function (data) {
            //         $("#commodityDesc").html(data);
            //         $("#commodityDesc", window.parent.document).html(data);
            //  });
            // }
            if (hsCodeFlag !== 'hsCode') {
                $('#' + hsCodeFlag).val('');
            }
            hideProgressBar();
        });
    }
}

function deleteHotCode(txt, id) {
    var refValue = $("#hotCodesRef" + id).val();
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                var hotcodeArray = refValue.split("/");
                var moduleName = $('#moduleName').val();
                if (hotcodeArray[0] === "XXX" && moduleName === "Exports") {
                    showAlternateMask();
                    $("#add-hotCodeComments-container").center().show(500, function () {
                        $('#headingComments').text('Enter a required explanation for removing the value "XXX" Hot Code');
                        $('#hotCodeComments').val('');
                        $('#hiddenDeleteHotCodeFlag').val('deleteFlag');
                        $('#3pRefId').val(id);
                    });
                } else if (hotcodeArray[0] === "EBL" && moduleName === "Exports" && $("#fileType").val() !== 'BL') {
                    parent.$("#econo").attr("checked", true);
                    submitAjaxFormforGeneral("deleteLcl3pReferenceComm", "hotCodes", id, "hotCodesList", "");
                } else {
                    submitAjaxFormforGeneral("deleteLcl3pReferenceComm", "hotCodes", id, "hotCodesList", "");
                }
                $.prompt.close();
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}

function cancelHotCodeXXXComments() {
    $("#add-hotCodeComments-container").center().hide(500, function () {
        $('#hotCodes').val('');
        $('#hotCodesBox').hide();
        hideAlternateMask();
    });
}

function deleteTracking(txt, id) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                submitAjaxFormforGeneral("deleteLcl3pReferenceComm", "tracking", id, "trackingList", "");
                $.prompt.close();
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}

function submitAjaxFormforGeneral(methodName, thirdPName, thirdpRefId, refreshDivId, comments) {
    showProgressBar();
    $('#methodName').val(methodName);
    var params = $("#lclCommodityForm").serialize();
    var fileNumberId = $("#fileNumberId").val();
    params += "&thirdPName=" + thirdPName + "&fileNumberId=" + fileNumberId;
    params += "&lcl3pRefId=" + thirdpRefId + "&hotCodecomments=" + comments;
    $.post($("#lclCommodityForm").attr("action"), params, function (data) {
        $('#' + refreshDivId).html(data);
        $("#" + refreshDivId, window.parent.document).html(data);
        hideProgressBar();
    });
}


function updateActualInBl(fileNumberId, bookingStatus) {
    var actualPieceCount = $('#actualPieceCount').val();
    var actualVolumeImperial = $('#actualVolumeImperial').val();
    var actualVolumeMetric = $('#actualVolumeMetric').val();
    var actualWeightImperial = $('#actualWeightImperial').val();
    var actualWeightMetric = $('#actualWeightMetric').val();
    var actualPackageTypeId = $('#actualPackageTypeId').val();
    var commodityId = $('#commodityTypeId').val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO",
            methodName: "actualValueCopyToBl",
            param1: fileNumberId,
            param2: commodityId,
            param3: actualPieceCount,
            param4: actualPackageTypeId,
            param5: actualVolumeImperial,
            param6: actualVolumeMetric,
            param7: actualWeightImperial,
            param8: actualWeightMetric,
            param9: bookingStatus,
            request: true,
            dataType: "json"
        },
        success: function (data) {
        }
    });
}

function copyActualValueBl(txt, fileNumberId, rateType, origin, destination, pol, pod, bookingStatus) {
    var fileState = parent.$('#fileState').val();
    var moduleName = parent.$('#moduleName').val();
    if (moduleName === 'Exports' && fileState === 'BL') {
        var text = "Are you sure you want to update actual fields in BL?";
        $.prompt(text, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    showProgressBar();
                    updateActualInBl(fileNumberId, bookingStatus);
                    $('#saveCommodity').attr("disabled", false);
                    saveCommodity(txt, fileNumberId, rateType, origin, destination, pol, pod);
                    $.prompt.close();
                    hideProgressBar();
                } else if (v === 2) {
                    $.prompt.close();
                    $('#saveCommodity').attr("disabled", false);
                    saveCommodity(txt, fileNumberId, rateType, origin, destination, pol, pod);
                }
            }
        });

    }
}


function actualFieldsEnableDisable() {
    var isReleaseFlag = parent.$("#actualFieldsChangeAfterRelease").val();
    if (isReleaseFlag === 'true') {
        enableBox('actualPieceCount');
        enableBox('actualPackageType');
        enableBox('actualWeightMetric');
        enableBox('actualVolumeMetric');
        enableBox('actualWeightImperial');
        enableBox('actualVolumeImperial');
    } else {
        disableBox('actualPieceCount');
        var cobFlag = parent.$('#cob').val();
        if (cobFlag === "true") {
            disableBox('actualPackageType');
        }
        disableBox('actualWeightMetric');
        disableBox('actualVolumeMetric');
        disableBox('actualWeightImperial');
        disableBox('actualVolumeImperial');
    }
}

function disableBox(ele) {
    $("#" + ele).addClass("textlabelsBoldForTextBoxDisabledLook");
    $("#" + ele).attr("readonly", true);
}

function enableBox(ele) {
    $("#" + ele).removeClass("textlabelsBoldForTextBoxDisabledLook");
    $("#" + ele).removeAttr("readonly");
}

function isCheckedReleaseStatus(fileNumberId) {
    var flag = false;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO",
            methodName: "getReleasedDateTime",
            param1: fileNumberId

        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}
function isCheckedShortShip(fileNo, pieceCount, weightImp, volumeImp) {//Validation by original DR ShortShip(commodityValues)
    var flag = false;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO",
            methodName: "getBkgPieceOldValues",
            param1: fileNo,
            param2: pieceCount,
            param3: weightImp,
            param4: volumeImp,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}
function isRatesValidationForShortShip() {
    var existWeightMetric = $('#existActualWeightMetric').val();
    var existVolumeMetric = $('#existActualVolumeMetric').val();
    var existWeightImp = $('#existActualWeightImperial').val();
    var existVolumeImp = $('#existActualVolumeImperial').val();

    var weightMet = $('#actualWeightMetric').val();
    var volumeMet = $('#actualVolumeMetric').val();
    var weightImp = $('#actualWeightImperial').val();
    var volumeImp = $('#actualVolumeImperial').val();
    var existPieceCount = $('#actPieceCount').val();
    var pieceCount = $('#actualPieceCount').val();
    if ((parseFloat(existWeightMetric) !== parseFloat(weightMet)) || (parseFloat(existVolumeMetric) !== parseFloat(volumeMet)) ||
            (parseFloat(existWeightImp) !== parseFloat(weightImp)) || (parseFloat(existVolumeImp) !== parseFloat(volumeImp)) ||
            (existPieceCount !== pieceCount)) {
        return true;
    }
    return false;
}

function isClassOfComm(fileNumberId, comm, oldComm) {
    var flag = false;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO",
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