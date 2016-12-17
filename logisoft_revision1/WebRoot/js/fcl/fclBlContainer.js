
function updateContainerDetails() {
    var bol = jQuery('#bol').val();
    var unitNo = jQuery('#trailerNo').val();
    var sealNumber = jQuery('#sealNo').val();
    var trailerNoOld = jQuery('#checkContainer').val();
    var breakBulk = jQuery('#breakBulk').val();
    if (unitNo !== '' && (unitNo.length < 13 || unitNo.lastIndexOf("-") !== 11 || unitNo.indexOf("-") !== 4)) {
        alertNew('Unit number must be "AAAA-NNNNNN-N" in format');
        return;
    }
    if (breakBulk !== "Y") {
        if (unitNo === "") {
            alertNew('Please Enter Unit Number');
            return;
        }
        if (sealNumber === "") {
            alertNew('Please Enter Seal Number');
            return;
        }
        if (jQuery('#sizeLegend').val() === '0') {
            alertNew('Please Select Container Size');
            return;
        }
    }
    if (trailerNoOld.toUpperCase() !== unitNo.toUpperCase()) {
        checkAvailabilty(unitNo, bol, "updateContainer");
    } else {
        document.getElementById("sizeLegend").disabled = false;
        jQuery("#methodName").val('updateContainer');
        jQuery("#fclBlContainer").submit();
    }
}
function validateUnitNumber(obj) {
    var unitNo = obj.value;
    unitNo = unitNo.replace(/-/g, '');
    if (unitNo.length < 11 || unitNo.length > 11 || isNotAlphabetic(unitNo.substring(0, 4)) || !isInteger(unitNo.substring(4, 12))) {
        alertNew('Unit number must be "AAAA-NNNNNN-N" in format');
        return;
    } else {
        if (unitNo.lastIndexOf("-") !== 11 || unitNo.indexOf("-") !== 4) {
            obj.value = unitNo.substring(0, 4) + "-" + unitNo.substring(4, 10) + "-" + unitNo.substring(10);
        }
    }

}
function addContainerDetails() {
    var bol = jQuery('#bol').val();
    var unitNo = jQuery('#trailerNo').val();
    var breakBulk = jQuery('#breakBulk').val();
    if (unitNo !== '' && (unitNo.length < 13 || unitNo.lastIndexOf("-") !== 11 || unitNo.indexOf("-") !== 4)) {
        alertNew('Unit number must be "AAAA-NNNNNN-N" in format');
        return false;
    }
    if (null !== breakBulk && breakBulk === 'Y') {
        jQuery("#methodName").val('saveContainer');
        jQuery("#fclBlContainer").submit();
    } else {
        if (jQuery('#sizeLegend').val() === '0') {
            alertNew('Please Select Container Size');
            return false;
        }
        checkAvailabilty(unitNo, bol, "saveContainer");
    }

}
function checkAvailabilty(unitNo, bol, methodName) {
    var unit = unitNo;
    if (unit !== '') {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkUnitNoAvailabilty",
                param1: unit,
                param2: bol,
                dataType: "json"
            },
            success: function(data) {
                if (data) {
                    jQuery('#trailerNo').val();
                    alertNew("Unit number is already exist.Please enter different Unit number");
                    return;
                } else {
                    document.getElementById("sizeLegend").disabled = false;
                    jQuery("#methodName").val(methodName);
                    jQuery("#fclBlContainer").submit();
                }
            }
        });
    }
}
function setToCommetFoucus() {
    if (event.keyCode === 9 || event.keyCode === 13) {
        setTimeout("set()", 0);
        return false;
    }
}
function set() {
    document.getElementById('marksNo').focus();
    document.getElementById('marksNo').select();
}
function allowFreeFormat(val) {
    val.value = val.value.replace(/-/g, '');
}

function formatUnitNo(val) {
    if (event.keyCode !== 8 && event.keyCode !== 46) {
        var inputValue = val.value;
        if (inputValue.length < 5 && isNotAlphabetic(inputValue)) {
            val.value = inputValue.substring(0, inputValue.length - 1);
            alertNew("Please enter alphabetic value");
            return;
        }

        if (inputValue.length > 4) {
            if (inputValue.length === 5) {
                if (!isInteger(inputValue.substring(4))) {
                    val.value = inputValue.substring(0, inputValue.length - 1);
                    alertNew("Please enter numeric value");
                    return;
                } else {
                    if (inputValue.length === 5) {
                        val.value = inputValue.substring(0, inputValue.length - 1) + "-" + inputValue.substring(inputValue.length - 1);
                    }
                }
            } else if (!isInteger(inputValue.substring(5, 11))) {
                val.value = inputValue.substring(0, inputValue.length - 1);
                alertNew("Please enter numeric value");
                return;
            } else {
                if (inputValue.length === 12) {
                    if (!isInteger(inputValue.substring(11))) {
                        val.value = inputValue.substring(0, inputValue.length - 1);
                        alertNew("Please enter numeric value");
                        return;
                    } else {
                        val.value = inputValue.substring(0, inputValue.length - 1) + "-" + inputValue.substring(inputValue.length - 1);
                    }
                }
                if (inputValue.length === 13) {
                    if (!isInteger(inputValue.substring(12))) {
                        val.value = inputValue.substring(0, inputValue.length - 1);
                        alertNew("Please enter numeric value");
                        return;
                    }
                }
            }
        }
    }
}
function limitTextarea(textarea, maxLines, maxChar) {
    var lines = textarea.value.replace(/\r/g, '').split('\n'),
            lines_removed,
            char_removed,
            i;
    if (maxLines && lines.length > maxLines) {
        alertNew('You can not enter\nmore than ' + maxLines + ' lines');
        lines = lines.slice(0, maxLines);
        lines_removed = 1;
    }
    if (maxChar) {
        i = lines.length;
        while (i-- > 0)
            if (lines[i].length > maxChar) {
                lines[i] = lines[i].slice(0, maxChar);
                char_removed = 1;
            }
        if (char_removed)
            alertNew('You can not enter more\nthan ' + maxChar + ' characters per line');
    }
    if (char_removed || lines_removed)
        textarea.value = lines.join('\n');
}

function watchTextarea() {
    document.getElementById('marksNo').onkeyup();
}
function load() {
    setTimeout("focusTrailerNo()", 100);
    var manifest = jQuery('#readyToPost').val();
    var sizeLegend = jQuery('#sizeLegend').val();
    if (manifest === 'M') {
        jQuery('#sizeLegend').attr("disabled", true);
        jQuery('#sizeLegend').addClass("BackgrndColorForTextBox");
    } else if (sizeLegend !== "") {
        jQuery('#sizeLegend').attr("disabled", false);
        jQuery('#sizeLegend').addClass("textlabelsBoldForTextBox");
    }
}
function focusTrailerNo() {
    if (!jQuery('#trailerNo').attr("readonly")) {
        document.getElementById('trailerNo').focus();
    }
}