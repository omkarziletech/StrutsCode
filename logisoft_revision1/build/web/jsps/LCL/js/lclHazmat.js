var path = "/" + window.location.pathname.split('/')[1];
$(document).ready(function () {
    var dispo = parent.$("#exportDisposition").val();
    if (dispo === 'OBKG') {
        $("#outerPkgNoPieces").removeClass("mandatory");
        $("#outerPkgType").removeClass("mandatory");
        $("#outerPkgComposition").removeClass("mandatory");
        $("#totalGrossWeight").removeClass("mandatory");
    }
    $('#saveHazmat').click(function () {
        var error = true;
        $(".required").each(function () {
            if ($(this).val().length == 0)
            {
                $.prompt('This field is required');
                error = false;
                $(this).css("border-color", "red");
                $(this).focus();
                return false;
            }
        });
        if (error) {
            if ($("#unHazmatNo").val() == null || $("#unHazmatNo").val() == "") {
                $.prompt('UN# is required');
                return false;
            }
            else if ($("#properShippingName").val() == null || $("#properShippingName").val() == "") {
                $.prompt('Proper Shipping Name is required');
                return false;
            }
            else if ($("#hazClass").val() == null || $("#hazClass").val() == "") {
                $.prompt('Please select Class');
                return false;
            }
            var liquidVolume = $("#liquidVolume").val();
            var liquidVolumeLitorGal = $("#liquidVolumeLitreorGals").val();
            var moduleName = $("#moduleName").val();
            if (($("#liquidVolume").val() !== "" || $("#liquidVolumeLitreorGals").val() !== "") && moduleName === 'Exports') {
                if (liquidVolume !== "" && liquidVolumeLitorGal === "") {
                    $.prompt('Please select Liters or Gallons ');
                    return false;
                } else if (liquidVolume === "" && liquidVolumeLitorGal !== "") {
                    $.prompt('Please select Total volume ');
                    return false;
                }
            }
            
           
            if (dispo != 'OBKG') {
                if ($("#outerPkgNoPieces").val() == null || $("#outerPkgNoPieces").val() == "") {
                    $.prompt('Please select Outer Packaging Pieces');
                    return false;
                }
                else if ($("#outerPkgType").val() == null || $("#outerPkgType").val() == "") {
                    $.prompt('Please select Outer Packaging Type');
                    return false;
                }
                else if ($("#outerPkgComposition").val() == null || $("#outerPkgComposition").val() == "") {
                    $.prompt('Please select Outer Pkg Composition');
                    return false;
                }
                else if ($("#totalGrossWeight").val() == null || $("#totalGrossWeight").val() == "") {
                    $.prompt('Please select Total Gross Weight/Kgs');
                    return false;
                }
            }
            submitHazmatForm('saveorUpdateBkgHazmat');
        }
    });
});


function submitHazmatForm(methodName) {
    showLoading();
    $("#methodName").val(methodName);
    $('#lclHazmatForm').submit();
}

function editBkgHazmat(hazmatId) {
    showLoading();
    $('#buttonFlag').val('editFlag');
    $('#hazmatId').val(hazmatId);
    submitHazmatForm('editBkgHazmat');
}

function addBkgHazmat() {
    $('#hazmatId').val('');
    $('input:radio[name=printHouseBL]').val(["Y"]);
    $('input:radio[name=printMasterBL]').val(["Y"]);
    $('input:radio[name=sendEdiMaster]').val(["Y"]);
    $('#hazmatsection').show();
    $('#addHazmat').hide();
    $('#saveHazmat').show();
}

function deleteBkgHazmat(hazmatId) {
    var txt = 'Are you sure you want to delete?';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showLoading();
                $('#buttonFlag').val('display');
                $('#hazmatId').val(hazmatId);
                submitHazmatForm('deleteBkgHazmat')
            }
            else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}

function closeHazmat(id, bookingPieceId) {
    showProgressBar();
    $("#methodName").val("closeHazmat");
    var params = $('#lclHazmatForm').serialize();
    params += "&id=" + id + "&bookingPieceId=" + bookingPieceId;
    $.post($('#lclHazmatForm').attr("action"), params,
        function (data) {
            $('#commodityDesc').html(data);
            $('#commodityDesc', window.parent.document).html(data);
            parent.$.fn.colorbox.close();
            hideProgressBar();
        });
}
jQuery(document).ready(function () {
    if ($('#buttonFlag').val() === 'display') {
        $('#addHazmat').show();
        $('#hazmatsection').hide();
        $('#saveHazmat').hide();
    } else {
        $('#addHazmat').hide();
        $('#saveHazmat').show();
        $('#hazmatsection').show();
    }
});

//$(document).ready(function () {
//    $('#freeForm').click(function () {
//        var error = true;
//        $(".required").each(function () {
//            if ($(this).val().length == 0)
//            {
//                $.prompt('This field is required');
//                error = false;
//                $(this).css("border-color", "red");
//                $(this).focus();
//                return false;
//            }
//        });
//        if (error) {
//            openFreeForm();
//        }
//    });
//});
//function openFreeForm() {
//    var freeFormat = "";
//    var unNumber = $("#unHazmatNo").val();
//    var properShipperName = $("#properShippingName").val();
//    var hazClass = $("#hazClass").val();
//    var innerPkgUom = $("#innerPkgUom").val();
//    var reportableQuantity = $('input:radio[name=reportableQuantity]:checked').val();
//    var marinePollutant = $('input:radio[name=marinePollutant]:checked').val();
//    var exceptedQuantity = $('input:radio[name=exceptedQuantity]:checked').val();
//    var limitedQuantity = $('input:radio[name=limitedQuantity]:checked').val();
//    var inhalationHazard = $('input:radio[name=inhalationHazard]:checked').val();
//    var residue = $('input:radio[name=residue]:checked').val();
//    if (unNumber !== "") {
//        freeFormat = freeFormat + "UN " + unNumber;
//    }
//    freeFormat = setFreeFormat(freeFormat, properShipperName);
//    if ($("#technicalName").val() !== "") {
//        freeFormat = setFreeFormat(freeFormat, "(" + $("#technicalName").val() + ")");
//    }
//    if (hazClass !== "") {
//        freeFormat = setFreeFormat(freeFormat, "CLASS " + hazClass);
//    }
//    if ($("#imoPriSubClassCode").val() !== "") {
//        freeFormat = setFreeFormat(freeFormat, "(" + $("#imoPriSubClassCode").val() + ")");
//    }
//    if ($("#imoSecSubClassCode").val() !== "") {
//        freeFormat = setFreeFormat(freeFormat, "(" + $("#imoSecSubClassCode").val() + ")");
//    }
//    if ($("#packingGroupCode").val() !== "") {
//        freeFormat = setFreeFormat(freeFormat, "PG " + $("#packingGroupCode").val());
//    }
//    if ($("#flashPoint").val() !== "") {
//        freeFormat = setFreeFormat(freeFormat, "FLASH POINT " + $("#flashPoint").val() + "C");
//    }
//    if ($("#outerPkgNoPieces").val() !== "") {
//        freeFormat = setFreeFormat(freeFormat, $("#outerPkgNoPieces").val());
//    } else {
//        freeFormat = setFreeFormat(freeFormat, $("#outerPkgNoPieces").val());
//    }
//    freeFormat += " " + $("#outerPkgComposition").val();
//    if ($("#outerPkgNoPieces").val() > 1) {
//        if ($("#outerPkgType").val() !== "") {
//            if ($("#outerPkgType").val() === "Box") {
//                freeFormat += " " + $("#outerPkgType").val() + "ES";
//            } else {
//                freeFormat += " " + $("#outerPkgType").val() + "S";
//            }
//        }
//    } else {
//        freeFormat += " " + $("#outerPkgType").val();
//    }
//    freeFormat = setFreeFormat(freeFormat, $("#innerPkgNoPieces").val());
//    freeFormat += " " + $("#innerPkgComposition").val();
//    if ($("#innerPkgNoPieces").val() > 1) {
//        if ($("#innerPkgType").val() === "Box") {
//            freeFormat += " " + $("#innerPkgType").val() + "ES";
//        } else {
//            freeFormat += " " + $("#innerPkgType").val() + "S";
//        }
//    } else {
//        freeFormat += " " + $("#innerPkgType").val();
//    }
//    if ($("#innerPkgNwtPiece").val() !== "" && $("#innerPkgNwtPiece").val() !== '0.00') {
//        freeFormat = setFreeFormat(freeFormat, "@ " + $("#innerPkgNwtPiece").val() + " " + innerPkgUom + " EACH");
//    }
//    if ($("#totalGrossWeight").val() !== "") {
//        freeFormat = setFreeFormat(freeFormat, "TOTAL GROSS WEIGHT " + $("#totalGrossWeight").val() + " KGS");//$("#totalGrossWeight").val()
//    }
//    if ($("#liquidVolume").val() !== "") {
//        freeFormat = setFreeFormat(freeFormat, "TOTAL VOUME " + $("#liquidVolume").val() + " LITER");//$("#totalVol").val()
//    }
//    freeFormat = setFreeFormat(freeFormat, $("#emergencyContact").val());
//    freeFormat = setFreeFormat(freeFormat, $("#emergencyPhone").val());
//    if ($("#emsCode").val() !== "") {
//        freeFormat = setFreeFormat(freeFormat, "EMS " + $("#emsCode").val());
//    }
//    freeFormat = setFreeFormat(freeFormat, '');//$("#innerPackWgtVol").val()
//    freeFormat = setFreeFormat(freeFormat, '');//$("#totalNetWgt").val()
//    if (reportableQuantity == "Y") {
//        freeFormat = freeFormat + ", Reportable Quantity";
//    }
//    if (marinePollutant == "Y") {
//        freeFormat = freeFormat + ", Marine Pollutant";
//    }
//    if (exceptedQuantity == "Y") {
//        freeFormat = freeFormat + ", Excepted Quantity";
//    }
//    if (limitedQuantity == "Y") {
//        freeFormat = freeFormat + ", Limited Quantity";
//    }
//    if (inhalationHazard == "Y") {
//        freeFormat = freeFormat + ", Inhalation Hazard";
//    }
//    if (residue == "Y") {
//        freeFormat = freeFormat + ", Residue";
//    }
//    if ($("#unHazmatNo").val() == null || $("#unHazmatNo").val() == "") {
//        $.prompt('UN# is required');
//    }
//    else {
//        var href = path + "/hazmatFreeForm.do?methodName=display&freeFormatValue="
//        + freeFormat + "&selectedSection=LCLBKG&hazmatId=" + $("#hazmatId").val();
//        $(".freeFormat").attr("href", href);
//        $(".freeFormat").colorbox({
//            iframe: true,
//            width: "50%",
//            height: "70%",
//            title: "Free Form"
//        });
//    }
//}
//
//function setFreeFormat(freeFormat, source) {
//    if (source != "") {
//        freeFormat = freeFormat + ", " + source;
//    }
//    return freeFormat;
//}
