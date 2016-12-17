function validateNumbersOnly(obj) {
    if (!/^([0-9]{0,10})$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This should be Numeric");
    }
}
function validateFourDigit(obj) {
    var uNumber = obj.value;
    if (uNumber.length < 4) {
        $.prompt("UN# must be exactly 4 digits");
        $("#unHazmatNo").val('');
    }
}

function checkForNumberAndDecimal(obj) {
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This should be Numeric");
    }
}
function allowNegativeNumbers(obj) {
    if (!/^-?\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This field should be Numeric");
    }
}
function freeFormPopUp(path, moduleName) {
    if ($("#unHazmatNo").val() == null || $("#unHazmatNo").val() == "") {
        $.prompt('UN# is required');
        return false;
    }
    var moduleScreen = $("#moduleName").val() === undefined ? parent.$("#moduleName").val() : $("#moduleName").val();
    if (moduleScreen !== "Imports") {
        $('.truncateTwoDigit').each(function () {
            var str = $(this).val();
            var id = $(this).attr("id");
            if (str.indexOf('.') !== -1) {
                if (str.substring(str.indexOf('.')).length > 2) {
                    var s1 = str.split(".");// abc.12xx --> substring to abc.12
                    document.getElementById(id).value = s1[0] + "." + s1[1].substring(0, 2);
                }
            }
        });
    }
    openFreeForm(path, moduleName);
}
function openFreeForm(path, moduleName) {
    var freeFormat = "";
    var unNumber = $("#unHazmatNo").val();
    var properShipperName = $("#properShippingName").val();
    var hazClass = $("#hazClass").val();
    var innerPkgUom = $("#innerPkgUom").val();
    var liquidVolumeLitreorGals = $("#liquidVolumeLitreorGals").val();
    var reportableQuantity = $('input:radio[name=reportableQuantity]:checked').val();
    var marinePollutant = $('input:radio[name=marinePollutant]:checked').val();
    var exceptedQuantity = $('input:radio[name=exceptedQuantity]:checked').val();
    var limitedQuantity = $('input:radio[name=limitedQuantity]:checked').val();
    var inhalationHazard = $('input:radio[name=inhalationHazard]:checked').val();
    var residue = $('input:radio[name=residue]:checked').val();
    if (unNumber !== "") {
        freeFormat = freeFormat + "UN " + unNumber;
    }
    freeFormat = setFreeFormat(freeFormat, properShipperName);
    if ($("#technicalName").val() !== "") {
        freeFormat = setFreeFormat(freeFormat, "(" + $("#technicalName").val() + ")");
    }
    if (hazClass !== "") {
        freeFormat = setFreeFormat(freeFormat, "CLASS " + hazClass);
    }
    if ($("#imoPriSubClassCode").val() !== "") {
        freeFormat = setFreeFormat(freeFormat, "(" + $("#imoPriSubClassCode").val() + ")");
    }
    if ($("#imoSecSubClassCode").val() !== "") {
        freeFormat = setFreeFormat(freeFormat, "(" + $("#imoSecSubClassCode").val() + ")");
    }
    if ($("#packingGroupCode").val() !== "") {
        freeFormat = setFreeFormat(freeFormat, "PG " + $("#packingGroupCode").val());
    }
    if ($("#flashPoint").val() !== "") {
        freeFormat = setFreeFormat(freeFormat, "FLASH POINT " + $("#flashPoint").val() + " DEG " + " C");
    }
    if ($("#outerPkgNoPieces").val() !== "") {
        freeFormat = setFreeFormat(freeFormat, $("#outerPkgNoPieces").val());
    } else {
        freeFormat = setFreeFormat(freeFormat, $("#outerPkgNoPieces").val());
    }
    freeFormat += " " + $("#outerPkgComposition").val();
    if ($("#outerPkgNoPieces").val() > 1) {
        if ($("#outerPkgType").val() !== "") {
            if ($("#outerPkgType").val() === "Box") {
                freeFormat += " " + $("#outerPkgType").val() + "ES";
            } else {
                freeFormat += " " + $("#outerPkgType").val() + "S";
            }
        }
    } else {
        freeFormat += " " + $("#outerPkgType").val();
    }
    freeFormat = setFreeFormat(freeFormat, $("#innerPkgNoPieces").val());
    freeFormat += " " + $("#innerPkgComposition").val();
    if ($("#innerPkgNoPieces").val() > 1) {
        if ($("#innerPkgType").val() === "Box") {
            freeFormat += " " + $("#innerPkgType").val() + "ES";
        } else {
            freeFormat += " " + $("#innerPkgType").val() + "S";
        }
    } else {
        freeFormat += " " + $("#innerPkgType").val();
    }
    if ($("#innerPkgNwtPiece").val() !== "" && $("#innerPkgNwtPiece").val() !== '0.00') {
        freeFormat = setFreeFormat(freeFormat, "@ " + $("#innerPkgNwtPiece").val() + " " + innerPkgUom + " EACH");
    }
    if ($("#totalNetWeight").val() !== "") {
        freeFormat = setFreeFormat(freeFormat, "TOTAL NET WT " + $("#totalNetWeight").val() + " KGS");//$("#totalNetWeight").val()
    }
    if ($("#totalGrossWeight").val() !== "") {
        freeFormat = setFreeFormat(freeFormat, "TOTAL GROSS WT " + $("#totalGrossWeight").val() + " KGS");//$("#totalGrossWeight").val()
    }
    if ($("#liquidVolume").val() !== "") {

        if (liquidVolumeLitreorGals === undefined || liquidVolumeLitreorGals === "") {
            freeFormat = setFreeFormat(freeFormat, "TOTAL VOLUME " + $("#liquidVolume").val() + " LITERS");//$("#totalVol").val()
        } else {
            freeFormat = setFreeFormat(freeFormat, "TOTAL VOLUME " + $("#liquidVolume").val() + " " + liquidVolumeLitreorGals);//$("#totalVol").val()
        }
    }
    if ($("#emsCode").val() !== "") {
        freeFormat = setFreeFormat(freeFormat, "EMS " + $("#emsCode").val());
    }
    freeFormat = setFreeFormat(freeFormat, '');//$("#innerPackWgtVol").val()
    freeFormat = setFreeFormat(freeFormat, '');//$("#totalNetWgt").val()
    if (reportableQuantity == "Y") {
        freeFormat = freeFormat + ", Reportable Quantity";
    }
    if (marinePollutant == "Y") {
        freeFormat = freeFormat + ", Marine Pollutant";
    }
    if (exceptedQuantity == "Y") {
        freeFormat = freeFormat + ", Excepted Quantity";
    }
    if (limitedQuantity == "Y") {
        freeFormat = freeFormat + ", Limited Quantity";
    }
    if (inhalationHazard == "Y") {
        freeFormat = freeFormat + ", Inhalation Hazard";
    }
    if (residue == "Y") {
        freeFormat = freeFormat + ", Residue";
    }
    freeFormat = setFreeFormat(freeFormat, $("#emergencyContact").val());
    freeFormat = setFreeFormat(freeFormat, $("#emergencyPhone").val());
    var href = path + "/hazmatFreeForm.do?methodName=display&freeFormatValue="
            + freeFormat + "&selectedSection=" + moduleName + "&hazmatId=" + $("#hazmatId").val();
    $.colorbox({
        iframe: true,
        width: "50%",
        height: "70%",
        href: href,
        title: "Free Form"
    });
}
function setFreeFormat(freeFormat, source) {
    if (source != "") {
        freeFormat = freeFormat + ", " + source;
    }
    return freeFormat;
}

$(function () {
    $("body").delegate(".truncateTwoDigit", "change", function () {
        var moduleScreen = $("#moduleName").val() === undefined ? parent.$("#moduleName").val() : $("#moduleName").val();
        if (moduleScreen !== "Imports") {
            $(this).aDot($(this).val(), $(this).attr('id'));
        }
    });
});

(function ($) {
    $.fn.aDot = function (str, id) {
        if (str.indexOf('.') !== -1) {
            if (str.substring(str.indexOf('.')).length > 2) {
                var s1 = str.split(".");// abc.12xx --> substring to abc.12
                document.getElementById(id).value = s1[0] + "." + s1[1].substring(0, 2);
            }
        }
    }
})($);