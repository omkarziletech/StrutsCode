var myArray = new Array();
var myArray1 = new Array();
var result = false;
var disabledMessage = "This Customer is Disabled";
jQuery(document).ready(function () {
    if (jQuery('#fileTypeC').is(':checked') && jQuery("#vaoyageInternational").val() === "") {
        enableTextBox("vaoyageInternational");
        jQuery("#fileTypeS").attr('disabled', false);
        jQuery("#fileTypeP").attr('disabled', false);
    } else {
        disableTextBox("vaoyageInternational");
    }
    if(jQuery("#vaoyageInternational").val() !== "") {
      jQuery("#fileTypeS").attr('disabled', true);
      jQuery("#fileTypeP").attr('disabled', true);  
    }
});
function call() {
    grayOut(true, '');
    document.getElementById("newProgressBar").style.display = "block";
}
function call2() {
    var cvr = document.getElementById("cover");
    cvr.style.display = "none";
    document.getElementById("newProgressBar").style.display = "none";
}
function loadData() {
    for (var i = 0; i < document.EditBookingsForm.elements.length; i++) {
        if (document.EditBookingsForm.elements[i].type === "radio" || document.EditBookingsForm.elements[i].type === "text" || document.EditBookingsForm.elements[i].type === "checkbox" || document.EditBookingsForm.elements[i].type === "select-one" || document.EditBookingsForm.elements[i].type === "textarea") {
            if (document.EditBookingsForm.elements[i].type === "radio" || document.EditBookingsForm.elements[i].type === "checkbox") {
                myArray[i] = document.EditBookingsForm.elements[i].checked;
            } else {
                myArray[i] = document.EditBookingsForm.elements[i].value;
            }
        }
    }
}

function setNumberval(event){

    // Allow special chars + arrows
    if (event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9
        || event.keyCode == 27 || event.keyCode == 13
        || (event.keyCode == 65 && event.ctrlKey === true)
        || (event.keyCode >= 35 && event.keyCode <= 39)){
        return;
    }else {
        // If it's not a number stop the keypress
        if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
            event.preventDefault();
        }
    }

}
function setPOA(ev) {
    var code;
    var poaValue;
    if (ev === "forwarder") {
        code = document.EditBookingsForm.forwarder.value;
    } else if (ev === "shipper") {
        code = document.EditBookingsForm.shipper.value;
    } else if (ev === "consignee") {
        code = document.EditBookingsForm.consignee.value;
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "setPoa",
            param1: code
        },
        async: false,
        success: function (data) {
            if ("Y" === data) {
                poaValue = "Yes";
                if (ev === "forwarder") {
                    document.EditBookingsForm.forwarderPoa.value = poaValue;
                    document.getElementById("forwarderPoaid").innerHTML = poaValue;
                    document.getElementById("forwarderPoaid").className = "poaGreen";
                } else if (ev === "shipper") {
                    document.getElementById("shipperPoaid").innerHTML = poaValue;
                    document.EditBookingsForm.shipperPoa.value = poaValue;
                    document.getElementById("shipperPoaid").className = "poaGreen";
                } else if (ev === "consignee") {
                    document.getElementById("ConsigneePoaid").innerHTML = poaValue;
                    document.EditBookingsForm.consigneePoa.value = poaValue;
                    document.getElementById("ConsigneePoaid").className = "poaGreen";

                }
            } else if ("N" === data) {
                poaValue = "No";
                if (ev === "forwarder") {
                    document.getElementById("forwarderPoaid").innerHTML = poaValue;
                    document.EditBookingsForm.forwarderPoa.value = poaValue;
                    document.getElementById("forwarderPoaid").className = "poaRed";
                } else if (ev === "shipper") {
                    document.getElementById("shipperPoaid").innerHTML = poaValue;
                    document.EditBookingsForm.shipperPoa.value = poaValue;
                    document.getElementById("shipperPoaid").className = "poaRed";
                } else if (ev === "consignee") {
                    document.getElementById("ConsigneePoaid").innerHTML = poaValue;
                    document.EditBookingsForm.consigneePoa.value = poaValue;
                    document.getElementById("ConsigneePoaid").className = "poaRed";
                }
            }
        }
    });
}
function numericTextbox(text) {
    var dateval = text.value;
    if (dateval !== "") {
        if ((!isNaN(parseFloat(dateval)) && isFinite(dateval)) === false) {
            alertNew("Please Enter Numeric Value For Transit Days");
            document.getElementById("noOfDays").value = "";
            document.getElementById("noOfDays").focus();
            return;
        }
    }
}

function validateConvertToBl() {
    var bookingNo = document.EditBookingsForm.bookingId.value;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "checkVendorOptional",
            param1: bookingNo
        },
        async: false,
        success: function (data1) {
            if (data1 !== null && data1 !== '') {
                alertNew("Please select vendor for the charges " + data1);
                return;
            } else if (customerResult !== null && customerResult !== '') {
                document.EditBookingsForm.eventDesc.value = customerResult;
                if ((customerResult.search("Credit Hold") !== -1) || (customerResult.search("No Credit") !== -1) || (customerResult.search("Suspended/See Accounting") !== -1)) {
                    customerResult = "<font color='red'>" + customerResult + "</font>";
                    customerResult += "<br> Are you sure want to convert this Booking to BL ? ";
                    confirmNew(customerResult, "convertToBl");
                } else if (customerResult.search("Legal/See Accounting") !== -1) {
                    customerResult = "<font color='red'>" + customerResult + "</font>";
                    alertNew(customerResult);
                    return;
                } else {
                    confirmNew("Are you sure want to convert this Booking to BL", "convertToBl");
                }
            } else {
                confirmNew("Are you sure want to convert this Booking to BL", "convertToBl");
            }
        }
    });
}

function converttobl(importFlag) {
    var bookingNo = document.EditBookingsForm.bookingId.value;
    if ((document.EditBookingsForm.prepaidToCollect[0].checked === false) && (document.EditBookingsForm.prepaidToCollect[1].checked === false)) {
        alertNew("Please select Prepaid/Collect to convert to BL");
        return;
    }
    if (document.EditBookingsForm.prepaidToCollect[0].checked && !document.EditBookingsForm.billToCode[0].checked && !document.EditBookingsForm.billToCode[1].checked && !document.EditBookingsForm.billToCode[2].checked) {
        alertNew("Please select Bill To Code to convert to BL");
        return;
    }
    if (!document.EditBookingsForm.bookingComplete[0].checked) {
        alertNew("Please Complete Booking");
        return;
    }
    if (document.EditBookingsForm.estimatedDate.value === "") {
        alertNew("Please Select ETD");
        document.EditBookingsForm.estimatedDate.focus();
        return;
    }
    if (document.EditBookingsForm.collapseid.value === "") {
        getNumbersChanged();
    }
    // On-Carriage remarks
    if (jQuery('#doorDestination').val() !== "" && jQuery('#onCarriage').is(':checked')
        && jQuery("#hiddenRemarks").val() === "" && checkOnCarriage() === false) {
        confirmYesOrNo("Oncarriage charge is required. Click Yes to add Oncarriage charge or No to skip this requirement and provide Oncarriage remarks", "onCarriage");
        return;
    }
    if ((importFlag === 'false') && (document.EditBookingsForm.documentsReceived[1].checked)) {
        alertNew("Please make Docs Received as 'Y' to convert to BL");
        return;
    }
    var valFlag = false;
    if (document.EditBookingsForm.collapseCheck !== undefined) {
        if (document.EditBookingsForm.collapseCheck.length === undefined) {
            if (document.EditBookingsForm.collapseCheck.checked) {
                valFlag = true;
            }
        } else {
            for (var k = 0; k < document.EditBookingsForm.collapseCheck.length; k++) {
                if (document.EditBookingsForm.collapseCheck[k].checked) {
                    valFlag = true;
                    break;
                }
            }
        }
    }
    if (!valFlag) {
        if (document.EditBookingsForm.breakBulk) {
            if (document.EditBookingsForm.breakBulk[1].checked) {
                alertNew("Please select at least one Container in Cost & Charges tab to convert to BL");
                return;
            }
        } else {
            alertNew("Please select at least one Container in Cost & Charges tab to convert to BL");
            return;
        }
    }
    document.EditBookingsForm.bundleOfr.value = bundleCheck;
    document.EditBookingsForm.collapseid.value = collapseid;
    validateCustomers();
    var agentRouted = "";
    if (document.EditBookingsForm.routedByAgent.value !== "") {
        agentRouted = document.EditBookingsForm.routedByAgent.value;
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "getUnlocDetail",
            param1: agentRouted
        },
        async: false,
        success: function (data) {
            if (data === "false") {
                alertNew("Please Enter UnLocation Code for selected Routed Agent");
            } else {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                        methodName: "validateCostHasBlueScreenCostCode",
                        param1: bookingNo,
                        param2: importFlag === "false" ? "FCLE" : "FCLI"
                    },
                    success: function (costData) {
                        if (costData !== null && costData !== '') {
                            alertNew("No Blue Screen cost code set up in the General Ledger charges for " + costData);
                        } else {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                    methodName: "checkVendorOptionalwithflagforCost",
                                    param1: bookingNo
                                },
                                success: function (costData) {
                                    if (costData !== null && costData !== '') {
                                        alertNew("Cost cannot be zero for " + costData);
                                    } else {
                                        jQuery.ajaxx({
                                            data: {
                                                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                                methodName: "checkVendorOptionalwithflag",
                                                param1: bookingNo
                                            },
                                            success: function (data) {
                                                if (data !== null && data !== '') {
                                                    alertNew("Sell cannot be zero for " + data);
                                                } else {
                                                    if (jQuery('#hazmatY').is(':checked') && checkCharges(HAZARDOUS) === false) {
                                                        confirmYesOrNo("File is hazardous but does not have hazardous surcharges," +
                                                            " Do you want to continue to convert to BL?", "hazCharge");
                                                    } else {
                                                        var containsJapan = document.EditBookingsForm.portOfDischarge.value;
                                                        var contains = (containsJapan.indexOf('JAPAN') > 1);
                                                        if (contains && importFlag === 'false') {
                                                            reminderBox("Japan AFR must be filed on all Japan shipments");
                                                        } else {
                                                            validateConvertToBl();
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    });
}
var printValue = '';
function PrintReports(val,module) {

    printValue = val;
    if (!document.EditBookingsForm.bookingComplete[0].checked) {
        alertNew("Please Complete Booking");
        return;
    } else {
        var valFlag = false;
        if (document.EditBookingsForm.collapseCheck !== undefined) {
            if (document.EditBookingsForm.collapseCheck.length === undefined) {
                if (document.EditBookingsForm.collapseCheck.checked) {
                    valFlag = true;
                }
            } else {
                for (var k = 0; k < document.EditBookingsForm.collapseCheck.length; k++) {
                    if (document.EditBookingsForm.collapseCheck[k].checked) {
                        valFlag = true;
                        break;
                    }
                }
            }
        }
        if (!valFlag) {
            if (document.EditBookingsForm.breakBulk) {
                if (document.EditBookingsForm.breakBulk[1].checked) {
                    alertNew("Please select at least one Container in Cost & Charges tab to convert to BL");
                    return;
                }
            } else {
                alertNew("Please select at least one Container in Cost & Charges tab to convert to BL");
                return;
            }
        }
        // On-Carriage remarks
        if (jQuery('#doorDestination').val() !== "" && jQuery('#onCarriage').is(':checked')
            && jQuery("#hiddenRemarks").val() === "" && checkOnCarriage() === false) {
            confirmYesOrNo("Oncarriage charge is required. Click Yes to add Oncarriage charge or No to skip this requirement and provide Oncarriage remarks", "onCarriage");
            return;
        }
    }
    printFunction(printValue,module);
}
function printFunction(val,module) {
    //---making the Page editable to save-----
    revertPageToEditMode(document.getElementById("editbook"));
    if (document.EditBookingsForm.collapseid.value === "") {
        getNumbersChanged();
    }
    document.EditBookingsForm.bundleOfr.value = bundleCheck;
    document.EditBookingsForm.collapseid.value = collapseid;
    if (val === 'on') {//--This condition is entered for view mode-----
        var email = "";
        var quoteBy = document.EditBookingsForm.quoteBy.value;
        var fileNo = document.EditBookingsForm.fileNo.value;
        if (null !== quoteBy && quoteBy !== '') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "getQuoteEmail",
                    param1: fileNo
                },
                async: false,
                success: function (data) {
                    email = data;
                }
            });
        }
        //--make the page readonly when booking complete------
        makePageReadOnlyOnBookingComplete(document.getElementById("editbook"), '3', '');
        GB_show('Print/Fax/Email FCL Booking FileNumber ' + "" + fileNo, '/logisoft/printConfig.do?screenName=Booking&bookingId=' + document.EditBookingsForm.bookingId.value +
            '&ssBookingNo=' + SSBookingNo + '&destination=' + portofDischarge + '&subject=FCL Booking-' + fileNo +
            '&toAddress=' + email + '&emailMessage=Please find the attached File' + '&fileNo=' + fileNo
            + '&destination=' + document.getElementById("portOfDischarge").value + '&issuingTerminal=' + document.EditBookingsForm.issuingTerminal.value + '&moduleName=' + module, 400, 1000);
    } else {
        document.getElementById("bConf").disabled = true;
        document.getElementById("bConf1").disabled = true;
        document.EditBookingsForm.buttonValue.value = "BKGConfReport";
        document.EditBookingsForm.submit();
    }
}
function checkOnCarriage() {
    var found = false;
    jQuery('#expandRatesTable').find('tr').each(function () {
        var chargeCode = jQuery(this.cells[4]).html();
        if (chargeCode.indexOf("ONCARRIAGE") !== -1) {
            found = true;
        }
    });
    return found;
}
function checkCharges(charge) {
    var found = false;
    jQuery('#expandRatesTable').find('.expandRatesTableChargeCode').each(function () {
        var chargeCode = jQuery(this).val();
        if (chargeCode === charge) {
            found = true;
            return false;
        }
    });
    return found;
}
function onCarriageOverride() {
    //refer remarks-div in /jsp/common/popup.jsp included in /jsps/fclQuotes/fragment/tableBookingRates.jspf
    showPopUp();
    jQuery("#override-remarks").val("");
    jQuery("#remarks-div").css({
        display: "block"
    });
    jQuery("#remarks-title").text("On-Carriage Remarks");
    jQuery("#remarks-title").prop("title", "On-Carriage remarks");
    jQuery("#remarks-msg").prop("title", "On-Carriage remarks");
    jQuery("#override-remarks").prop("title", "On-Carriage remarks goes here!");
}
function openRates() {
    var bookingId = jQuery("#bkgId").val();
    var fileNo = jQuery("#moduleRefId").val();
    var hazmat = jQuery("#hazmatY").is(":checked") === true ? "Y" : "N";
    var splEquip = document.EditBookingsForm.specialequipment.value;
    var rateType = jQuery("#rated-yes").is(":checked") === true ? "rated" : "NonRated";
    var url = "/logisoft/bookingCharges.do?hazmat=" + hazmat + '&spcleqpmt=' + splEquip
    + '&provisions=onCarriage&bkgNo=' + bookingId + '&button=editBooking&fileNo=' + fileNo + '&ratedOption=' + rateType;
    GB_show('On-Carriage Rates', url, 400, 820);
}
function saveRemarks() {
    var remarks = jQuery("#override-remarks").val();
    if (trim(remarks) !== "") {
        jQuery('#remarks-div').css("display", "none");
        closePopUp();
        jQuery("#buttonValue").val("onCarriageRemarks");
        jQuery("#onCarriage").prop("checked", true);
        revertPageToEditMode(document.getElementById("editbook"));
        document.EditBookingsForm.submit();
    } else {
        alertNew("Please enter remarks");
    }
}
function cancelRemarks() {
    jQuery('#remarks-div').css("display", "none");
    closePopUp();
}
function chkDoorDest() {
    if (jQuery("#doorDestination").val() === "" && jQuery('#onCarriage').is(':checked')) {
        alertNew("Please enter Door at Dest");
        jQuery('#onCarriage').prop('checked', false);
        jQuery("#doorDestination").css("border-color", "red");
    } else {
        jQuery("#doorDestination").css("border-color", "");
    }
}
function compareWithOldArray() {
    for (var i = 0; i < document.EditBookingsForm.elements.length; i++) {
        if (document.EditBookingsForm.elements[i].type === "radio" || document.EditBookingsForm.elements[i].type === "text" || document.EditBookingsForm.elements[i].type === "checkbox" || document.EditBookingsForm.elements[i].type === "select-one" || document.EditBookingsForm.elements[i].type === "textarea") {
            if (document.EditBookingsForm.elements[i].type === "radio" || document.EditBookingsForm.elements[i].type === "checkbox") {
                myArray1[i] = document.EditBookingsForm.elements[i].checked;
            } else {
                myArray1[i] = document.EditBookingsForm.elements[i].value;
            }
        }
    }
    for (var j = 0; j < myArray1.length; j++) {
        if (myArray[j] !== myArray1[j]) {
            result = true;
        }
    }
    if (result === true) {
        goBack();
    } else {
         goBackCall();
    }
}
function validateDateFor(which) {
    if (event.keyCode === 9 || event.keyCode === 13 || event.keyCode === 0) {
        var x = which.value;
        var failFlag = 0;
        if (x.indexOf("-") === -1) {
            which.value = "";
            which.focus();
            alertNew("Date format must be dd-MM-YYYY e.g. 02-Nov-2007");
            return;
        }
        var ss = x.split("-");
        if (ss[0] < 1 || ss[1] > 31) {
            failFlag = 1;
        } // day
        if (!compare(ss[1])) {
            failFlag = 1;
        } // month
        if (ss[2] < 2000 || ss[2] > 2099) {
            failFlag = 1;
        } //max year 2099 or whatever
        if (failFlag === 1) {
            which.value = "";
            which.focus();
            alertNew("Date format must be dd-MM-YYYY e.g. 02-Nov-2007");
        } else {
            return;
        } // for test purposes
    }
}
function compare(ev) {
    var result = false;
    var monthArray = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
    for (var i = 0; i < monthArray.length; i++) {
        if (ev === monthArray[i]) {
            result = true;
        }
    }
    return result;
}
function getMonth(value) {
    var monthArray = new Array("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
    for (var i = 0; i < monthArray.length; i++) {
        if (value === monthArray[i]) {
            return i;
        }
    }
    return null;
}
var collapseid = "";
var bundleCheck = "";
var newfileNumber = "";
function loadFunction(fileNo) {
    newfileNumber = fileNo;
    loadData();
    if (document.EditBookingsForm.shipper.value === "") {
        hide("shipperContactButton");
    }
    if (document.EditBookingsForm.fowardername.value === "") {
        hide("forwarderContactButton");
    }
    if (document.EditBookingsForm.consigneename.value === "") {
        hide("congineeContactButton");
    }
    if (document.EditBookingsForm.truckerName && document.EditBookingsForm.truckerName.value === "") {
        hide("truckerContactButton");
    }
    for (var i = 0; i < document.EditBookingsForm.insurance.length; i++) {
        if (document.EditBookingsForm.insurance[i].value === "N" && document.EditBookingsForm.insurance[i].checked) {
            document.EditBookingsForm.costofgoods.readOnly = true;
            document.EditBookingsForm.costofgoods.tabIndex = -1;
        }
    }
    insuranceAllowedForBooking();
}
function hide(elementId) {
    getElement(elementId).style.visibility = "hidden";
}
function getElement(elementId) {
    return document.getElementById(elementId);
}
function tabtoLineMove() {
    document.EditBookingsForm.lineMove.focus();
}
function popup1(mylink, windowname) {
    if (!window.focus) {
        return true;
    }
    var href;
    if (typeof (mylink) === "string") {
        href = mylink;
    } else {
        href = mylink.href;
    }
    mywindow = window.open(href, windowname, "width=700,height=350,scrollbars=yes");
    mywindow.moveTo(200, 180);
    return false;
}
function dateValidation(data) {
    var date = data.value;
    var yr = data.value.substring(date.lastIndexOf("/") + 1);
    if (!isValidDate(date, yr)) {
        data.value = '';
    }
}
function validateDate(importFlag, ele) {
    if (jQuery.trim(ele.value) !== "") {
        ele.value = ele.value.getValidDateTime("/", "", false);
        if (jQuery.trim(ele.value) === "" || ele.value.length > 10) {
            alertNew("Please enter valid date");
            ele.value = "";
            document.getElementById(ele.id).focus();
        } else {
            var eta = document.EditBookingsForm.estimatedAtten.value;
            var etd = document.EditBookingsForm.estimatedDate.value;
            if ("true" === importFlag) {
                if (jQuery.trim(eta) !== "" && jQuery.trim(etd) !== "") {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                            methodName: "getTransitDays",
                            param1: etd,
                            param2: eta
                        },
                        success: function (data) {
                            if (null !== data) {
                                document.EditBookingsForm.noOfDays.value = data;
                            } else {
                                document.EditBookingsForm.lineMove.focus();
                                yearValidation(ele);
                            }
                        }
                    });
                } else {
                    document.EditBookingsForm.lineMove.focus();
                    yearValidation(ele);
                }
            } else {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                        methodName: "validateEtaDate",
                        param1: eta,
                        param2: etd
                    },
                    success: function (data) {
                        if (null !== data && data !== "") {
                            document.EditBookingsForm.estimatedAtten.value = "";
                            document.EditBookingsForm.estimatedAtten.select();
                            alertNew(data);
                        } else {
                            document.EditBookingsForm.lineMove.focus();
                            yearValidation(ele);
                        }
                    }
                });
            }
        }
    }
}

function validateDate1(importFlag, ele) {
    if (jQuery.trim(ele.value) !== "") {
        ele.value = ele.value.getValidDateTime("/", "", false);
        if (ele.value === "" || ele.value.length > 10) {
            alertNew("Please Enter Valid Date");
            ele.value = "";
            document.getElementById(ele.id).focus();
        } else {
            var etd = document.EditBookingsForm.estimatedDate.value;
            var eta = document.EditBookingsForm.estimatedAtten.value;
            var containerCutOff = document.EditBookingsForm.portCutOff.value;
            var docCutOff = document.EditBookingsForm.docCutOff.value;
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "validateEtdDate",
                    param1: etd,
                    param2: eta,
                    param3: importFlag === "false" ? containerCutOff : "",
                    param4: importFlag === "false" ? docCutOff : ""
                },
                success: function (data) {
                    if (null !== data && data !== "") {
                        if (data === "ETD should be less than ETA") {
                            document.EditBookingsForm.estimatedAtten.value = "";
                        }
                        document.EditBookingsForm.estimatedDate.value = "";
                        document.EditBookingsForm.estimatedDate.select();
                        alertNew(data);
                    } else {
                        if (data.id === "txtcal22") {
                            document.getElementById("txtcal5").focus();
                        }
                        yearValidation(ele);
                    }
                }
            });
        }
    }
}

function checkDateTime() {
    var dateTime = "";
    if (document.getElementById("timeCheckBox").checked && (document.getElementById("txtcal53").value !== null || document.getElementById("txtcal53").value !== "")) {
        dateTime = document.getElementById("txtcal53").value;
        document.getElementById("txtcal53").value = dateTime.substring(0, 11);
    }
    if (document.getElementById("timeCheckBox").checked === false) {
        document.getElementById("txtcal53").value = "";
    }
}
function displayDateOnly(val) {
    if (document.getElementById("timeCheckBox").checked) {
        insertDateFromCalendar(val.id, 0);
    } else {
        insertDateFromCalendar(val.id, 9);
    }
}
function concatSlashForDateValue(dateValue) {
    return (dateValue.substring(0, 2) + "/" + dateValue.substring(2, 4) + "/" + dateValue.substring(4));
}
function compareWithTodaysDate(importFlag, ele) {
    if (ele.value !== "") {
        var validateDate = ele.value;
        validateDate = validateDate.getValidDateTime("/", "", false);
        if (document.getElementById("timeCheckBox").checked) {
            if (validateDate === "") {
                alertNew("Please enter valid date");
                ele.value = "";
                document.getElementById("timeCheckBox").checked = false;
                document.getElementById(ele.id).focus();
            } else if (trim(ele.value).length > 10) {
                alertNew("Please Enter Date Alone");
                ele.value = trim(ele.value).substr(0, 10);
            } else if (validateDate !== "") {
                ele.value = validateDate;
            } else {
                alertNew("Please enter valid date");
                ele.value = "";
                document.getElementById("timeCheckBox").checked = false;
                document.getElementById(ele.id).focus();
            }
        } else {
            if (validateDate === "" || trim(ele.value).length > 19) {
                alertNew("Please enter valid date");
                document.getElementById("timeCheckBox").checked = false;
                ele.value = "";
                document.getElementById(ele.id).focus();
            } else if (validateDate !== "") {
                if (ele.value.length < 19) {
                    if (ele.value.toUpperCase().indexOf("PM") > -1 && ele.value.toUpperCase().indexOf(" PM") === -1) {
                        ele.value = ele.value.toUpperCase().replace("PM", " PM");
                    } else if (ele.value.toUpperCase().indexOf("AM") > -1 && ele.value.toUpperCase().indexOf(" AM") === -1) {
                        ele.value = ele.value.toUpperCase().replace("AM", " AM");
                    } else {
                        ele.value = validateDate + " 12:00 PM";
                    }
                }
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                        methodName: "dateTimeValidation",
                        param1: ele.value,
                        param2: "MM/dd/yyyy HH:mm a"
                    },
                    async: false,
                    success: function (data) {
                        if (null === data) {
                            alertNew("Please enter valid date");
                            document.getElementById("timeCheckBox").checked = false;
                            ele.value = "";
                            document.getElementById(ele.id).focus();
                        }
                    }
                });
            } else {
                alertNew("Please enter valid date");
                document.getElementById("timeCheckBox").checked = false;
                ele.value = "";
                document.getElementById(ele.id).focus();
            }
        }
        yearValidation(ele);
    }
    if (ele.value !== "") {
        var preventRun = false;
        var date = new Date();
        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear();
        var pickUpDate = document.EditBookingsForm.earlierPickUpDate.value;
        var spottingDate = document.EditBookingsForm.postioningDate.value;
        var containerDate = document.EditBookingsForm.portCutOff.value;
        if (importFlag === "true") {
            if (pickUpDate !== "" && pickUpDate !== null) {
                /**
                 * @Added a check if date value entered like 08082012
                 * Begin
                 */
                if (pickUpDate.length === 8 && !(isNaN(pickUpDate))) {
                    pickUpDate = concatSlashForDateValue(pickUpDate);
                    jQuery("[name=earlierPickUpDate]").val(pickUpDate);
                }
            //End
            }
        } else {
            if (pickUpDate !== "" && pickUpDate !== null) {
                /**
                 * @Added a check if date value entered like 08082012
                 * Begin
                 */
                if (pickUpDate.length === 8 && !(isNaN(pickUpDate))) {
                    pickUpDate = concatSlashForDateValue(pickUpDate);
                    jQuery("[name=earlierPickUpDate]").val(pickUpDate);
                }
                //End
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                        methodName: "dateValidation",
                        param1: pickUpDate,
                        param2: currentDate
                    },
                    async: false,
                    success: function (data) {
                        if (data === "lesser") {
                            alertNew("Earliest Date Cannot be less than today");
                            document.EditBookingsForm.earlierPickUpDate.value = "";
                            document.EditBookingsForm.earlierPickUpDate.focus();
                            preventRun = true;
                        }
                    }
                });
            }
            if (!preventRun && spottingDate !== "" && spottingDate !== null) {
                /**
                 * @Added a check if date value entered like 08082012
                 * Begin
                 */
                if (spottingDate.length === 8 && !(isNaN(spottingDate))) {
                    spottingDate = concatSlashForDateValue(spottingDate);
                    jQuery("[name=postioningDate]").val(spottingDate + " 12:00 PM");
                }
                //End
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                        methodName: "dateValidation",
                        param1: spottingDate,
                        param2: currentDate
                    },
                    async: false,
                    success: function (data) {
                        if (data === "lesser") {
                            alertNew("Spotting Date Cannot be less than today");
                            document.EditBookingsForm.postioningDate.value = "";
                            document.getElementById("timeCheckBox").checked = false;
                            document.EditBookingsForm.postioningDate.focus();
                            preventRun = true;
                        }
                    }
                });
            }
        }
        if (!preventRun && spottingDate !== "" && spottingDate !== null) {
            /**
             * @Added a check if date value entered like 08082012
             * Begin
             */
            if (spottingDate.length === 8 && !(isNaN(spottingDate))) {
                spottingDate = concatSlashForDateValue(spottingDate);
                jQuery("[name=postioningDate]").val(spottingDate + " 12:00 PM");
            }
            //End
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateValidation",
                    param1: spottingDate,
                    param2: containerDate
                },
                async: false,
                success: function (data) {
                    if (data === "greater") {
                        alertNew("Spotting Date Cannot be greater than ContainerCutOff Date");
                        document.EditBookingsForm.postioningDate.value = "";
                        document.getElementById("timeCheckBox").checked = false;
                        document.EditBookingsForm.postioningDate.focus();
                        preventRun = true;
                    }
                }
            });
        }
        if (!preventRun && containerDate !== "" && containerDate !== null) {
            /**
             * @Added a check if date value entered like 08082012
             * Begin
             */
            if (pickUpDate.length === 8 && !(isNaN(pickUpDate))) {
                pickUpDate = concatSlashForDateValue(pickUpDate);
                jQuery("[name=earlierPickUpDate]").val(pickUpDate);
            }
            //End
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateValidation",
                    param1: containerDate,
                    param2: pickUpDate
                },
                async: false,
                success: function (data) {
                    if (data === "lesser") {
                        alertNew("Earliest Date Cannot be greater than ContainerCutOff Date");
                        document.EditBookingsForm.earlierPickUpDate.value = "";
                        document.EditBookingsForm.earlierPickUpDate.focus();
                        preventRun = true;
                    }
                }
            });
        }
    }
}
function AlertMessage(importFlag, ele) {
    var preventRun = false;
    if (ele.value !== "") {
        if (ele.value.length < 11) {
            if (ele.value.length === 8 && !(isNaN(ele.value))) {
                ele.value = concatSlashForDateValue(ele.value);
            }
            ele.value = ele.value.getValidDateTime("/", "", false);
            ele.value = ele.value + " 12:00 PM";
        } else if (ele.value.toUpperCase().indexOf("PM") > -1 && ele.value.toUpperCase().indexOf(" PM") === -1) {
            ele.value = ele.value.toUpperCase().replace("PM", " PM");
        } else if (ele.value.toUpperCase().indexOf("AM") > -1 && ele.value.toUpperCase().indexOf(" AM") === -1) {
            ele.value = ele.value.toUpperCase().replace("AM", " AM");
        }
        if (ele.value !== "") {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateTimeValidation",
                    param1: ele.value,
                    param2: "MM/dd/yyyy HH:mm a"
                },
                async: false,
                success: function (data) {
                    if ('null' !== data) {
                        var date = new Date();
                        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear();
                        var previousTime = new Date();
                        previousTime.setDate(previousTime.getDate() - 60);
                        var previousDate = previousTime.getMonth() + 1 + "/" + previousTime.getDate() + "/" + previousTime.getFullYear();
                        var containerDate = document.EditBookingsForm.portCutOff.value;
                        var docDate = document.EditBookingsForm.docCutOff.value;
                        var etd = document.EditBookingsForm.estimatedDate.value;
                        var dateInYard = document.EditBookingsForm.dateInYard.value;
                        var earliestDate = document.EditBookingsForm.earlierPickUpDate.value;
                        if (importFlag === "true") {
                            if (containerDate !== "" && containerDate !== null) {
                                /**
                                 * @Added a check if date value entered like 08082012
                                 * Begin
                                 */
                                if (containerDate.length === 8 && !(isNaN(containerDate))) {
                                    containerDate = concatSlashForDateValue(containerDate);
                                    jQuery("[name=portCutOff]").val(containerDate);
                                    alertNew(containerDate);
                                }
                                //End
                                jQuery.ajaxx({
                                    data: {
                                        className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                        methodName: "dateValidationforImport",
                                        param1: containerDate,
                                        param2: currentDate
                                    },
                                    async: false,
                                    success: function (data) {
                                        if (data === "lesser") {
                                            alertNew("Container Cut off Cannot be less than " + previousDate);
                                            document.EditBookingsForm.portCutOff.value = "";
                                            document.EditBookingsForm.portCutOff.focus();
                                            preventRun = true;
                                        }
                                    }
                                });
                            }
                            if (!preventRun && docDate !== "" && docDate !== null) {
                                /**
                                 * @Added a check if date value entered like 08082012
                                 * Begin
                                 */
                                if (docDate.length === 8 && !(isNaN(docDate))) {
                                    docDate = concatSlashForDateValue(docDate);
                                    jQuery("[name=docCutOff]").val(docDate);
                                }
                                //End
                                jQuery.ajaxx({
                                    data: {
                                        className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                        methodName: "dateValidationforImport",
                                        param1: docDate,
                                        param2: currentDate
                                    },
                                    async: false,
                                    success: function (data) {
                                        if (data === "lesser") {
                                            alertNew("Doc Cut off Cannot be less than " + previousDate);
                                            document.EditBookingsForm.docCutOff.value = "";
                                            document.EditBookingsForm.docCutOff.focus();
                                            preventRun = true;
                                        }
                                    }
                                });
                            }
                        } else {
                            if (!preventRun && containerDate !== "" && containerDate !== null) {
                                /**
                                 * @Added a check if date value entered like 08082012
                                 * Begin
                                 */
                                if (containerDate.length === 8 && !(isNaN(containerDate))) {
                                    containerDate = concatSlashForDateValue(containerDate);
                                    jQuery("[name=portCutOff]").val(containerDate);
                                    alertNew(containerDate);
                                }
                                //End
                                jQuery.ajaxx({
                                    data: {
                                        className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                        methodName: "dateValidation",
                                        param1: containerDate,
                                        param2: currentDate
                                    },
                                    async: false,
                                    success: function (data) {
                                        if (data === "lesser") {
                                            alertNew("Container Cut off Cannot be less than today");
                                            document.EditBookingsForm.portCutOff.value = "";
                                            document.EditBookingsForm.portCutOff.focus();
                                            preventRun = true;
                                        }
                                    }
                                });
                            }
                            if (!preventRun && docDate !== "" && docDate !== null) {
                                /**
                                 * @Added a check if date value entered like 08082012
                                 * Begin
                                 */
                                if (docDate.length === 8 && !(isNaN(docDate))) {
                                    docDate = concatSlashForDateValue(docDate);
                                    jQuery("[name=docCutOff]").val(docDate);
                                }
                                //End
                                jQuery.ajaxx({
                                    data: {
                                        className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                        methodName: "dateValidation",
                                        param1: docDate,
                                        param2: currentDate
                                    },
                                    async: false,
                                    success: function (data) {
                                        if (data === "lesser") {
                                            alertNew("Doc Cut off Cannot be less than today");
                                            document.EditBookingsForm.docCutOff.value = "";
                                            document.EditBookingsForm.docCutOff.focus();
                                            preventRun = true;
                                        }
                                    }
                                });
                            }
                        }
                        if (!preventRun && etd !== "" && etd !== null) {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                    methodName: "dateValidation",
                                    param1: containerDate,
                                    param2: etd
                                },
                                async: false,
                                success: function (data) {
                                    if (data === "greater") {
                                        alertNew("Container Cut off Cannot be greater than ETD");
                                        document.EditBookingsForm.portCutOff.value = "";
                                        document.EditBookingsForm.portCutOff.focus();
                                        preventRun = true;
                                    }
                                }
                            });
                        }
                        if (!preventRun && etd !== "" && etd !== null) {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                    methodName: "dateValidation",
                                    param1: docDate,
                                    param2: etd
                                },
                                async: false,
                                success: function (data) {
                                    if (data === "greater") {
                                        alertNew("Doc Cut off Cannot be greater than ETD");
                                        document.EditBookingsForm.docCutOff.value = "";
                                        document.EditBookingsForm.docCutOff.focus();
                                        preventRun = true;
                                    }
                                }
                            });
                        }
                        if (!preventRun && document.EditBookingsForm.dateInYard.value !== "") {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                    methodName: "dateValidation",
                                    param1: containerDate,
                                    param2: dateInYard
                                },
                                async: false,
                                success: function (data) {
                                    if (data === "lesser") {
                                        alertNew("Container Cut off Cannot be lesser than DateBackIntoYard");
                                        document.EditBookingsForm.portCutOff.value = "";
                                        document.EditBookingsForm.portCutOff.focus();
                                        preventRun = true;
                                    }
                                }
                            });
                        }
                        if (!preventRun && earliestDate !== "" && earliestDate !== null) {
                            /**
                             * @Added a check if date value entered like 08082012
                             * Begin
                             */
                            if (earliestDate.length === 8 && !(isNaN(earliestDate))) {
                                earliestDate = concatSlashForDateValue(earliestDate);
                                jQuery("[name=earlierPickUpDate]").val(earliestDate);
                            }
                            //End
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                    methodName: "dateValidation",
                                    param1: containerDate,
                                    param2: earliestDate
                                },
                                async: false,
                                success: function (data) {
                                    if (data === "lesser") {
                                        alertNew("Container Cut off Cannot be lesser than Earliest Date");
                                        document.EditBookingsForm.portCutOff.value = "";
                                        document.EditBookingsForm.portCutOff.focus();
                                        preventRun = true;
                                    }
                                }
                            });
                        }
                    } else {
                        alertNew("Please Enter Valid Date");
                        ele.value = "";
                        document.getElementById(ele.id).focus();
                    }
                }
            });
        }
        if (!preventRun) {
            if (ele.value !== "") {
                if (ele.id === "txtcal313") {
                    document.getElementById("txtcal71").focus();
                } else if (ele.id === "txtcal71") {
                    document.getElementById("txtcal22").focus();
                }
            }
            yearValidation(ele);
        }
    }
}
function tabVessel() {
    document.getElementById("ssVoy").focus();
}
function tabLineMove() {
    if (document.getElementById("moveType")) {
        document.getElementById("moveType").focus();
    }
}
function clearWareHouseNameAndAddress() {
    document.EditBookingsForm.emptypickupaddress.value = "";
    document.EditBookingsForm.wareHouseTemp.value = "";
    if (document.getElementById("autoAddressFill").checked) {
        document.EditBookingsForm.addressofDelivery.value = "";
        document.EditBookingsForm.equipmentReturnName.value = "";
        document.EditBookingsForm.equipmentReturnTemp.value = "";
        document.getElementById("autoAddressFill").checked = false;
    }
}
function clearEquipmentReturnNameAndAddress() {
    document.EditBookingsForm.addressofDelivery.value = "";
    document.EditBookingsForm.equipmentReturnTemp.value = "";
}
function getWareHouseAdd() {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "getWarehouseAddressById",
            param1: document.getElementById("wareHouseId").value,
            dataType: "json"
        },
        success: function (data) {
            populateAddress(data);
        }
    });
}
function populateAddress(data) {
    if (null !== data) {
        if (data.address !== "" && data.address !== null && data.zipCode !== null && data.city !== null && data.state !== null) {
            document.EditBookingsForm.emptypickupaddress.value = data.warehouseName + "\n" + data.address + "\n" + data.city + "," + data.state + "," + data.zipCode;
        } else {
            document.EditBookingsForm.emptypickupaddress.value = "";
        }
        if (null !== data.warehouseNo) {
            document.getElementById("wareHouseTemp").value = data.warehouseNo;
        }
        if (data.address !== "" && data.address !== null && data.zipCode !== null && data.city !== null && data.state !== null) {
            if (document.getElementById("autoAddressFill").checked) {
                document.EditBookingsForm.addressofDelivery.value = data.warehouseName + "\n" + data.address + "\n" + data.city + "," + data.state + "," + data.zipCode;
            }
        }
        if (data.warehouseName === undefined) {
            document.EditBookingsForm.emptypickupaddress.value = "";
        }
    }
}
function addEquipmentReturn() {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "getWarehouseAddressById",
            param1: document.getElementById("equipmentReturnId").value,
            dataType: "json"
        },
        success: function (data) {
            populateEquipmentReturnAddress(data);
        }
    });
}
function populateEquipmentReturnAddress(data) {
    if (null !== data) {
        if (data.address !== "" && data.address !== null && data.zipCode !== null && data.city !== null && data.state !== null) {
            document.EditBookingsForm.addressofDelivery.value = data.warehouseName + "\n" + data.address + "\n" + data.city + "," + data.state + "," + data.zipCode;
        } else {
            document.EditBookingsForm.addressofDelivery.value = "";
        }
        if (null !== data.warehouseNo) {
            document.getElementById("equipmentReturnTemp").value = data.warehouseNo;
        }
        if (data.warehouseName === undefined) {
            document.EditBookingsForm.addressofDelivery.value = "";
        }
    }
}
function RefenceReport(val) {
    if (val === "on") {
        document.EditBookingsForm.buttonValue.value = "RefenceReportwithoutsave";
    } else {
        document.EditBookingsForm.buttonValue.value = "RefenceReport";
    }
    document.EditBookingsForm.submit();
}
function WorkOrderReport(val) {
    if (val === "on") {
        document.EditBookingsForm.buttonValue.value = "WorkOrderReportwithoutsave";
    } else {
        document.EditBookingsForm.buttonValue.value = "WorkOrderReport";
    }
    document.EditBookingsForm.submit();
}

function getAccountName(ev) {
    if (document.EditBookingsForm.collapseid.value === "") {
        getNumbersChanged();
    }
    document.EditBookingsForm.bundleOfr.value = bundleCheck;
    document.EditBookingsForm.collapseid.value = collapseid;
    if (event.keyCode === 9 || event.keyCode === 13) {
        if (ev.match(/^-?[0-9]+(.[0-9]{1,2})?$/) === null) {
            ev = '';
            alertNew('The format is wrong');
            return false;
        } else {
            document.EditBookingsForm.buttonValue.value = "recalc";
            document.EditBookingsForm.submit();
        }
    }
}
function getAccountNameById(adjustmentAmtValue, Id) {
    if (document.EditBookingsForm.collapseid.value === "") {
        getNumbersChanged();
    }
    document.EditBookingsForm.bundleOfr.value = bundleCheck;
    document.EditBookingsForm.collapseid.value = collapseid;
    //if (event.keyCode === 9 || event.keyCode === 13) {
    if (adjustmentAmtValue.match(/^-?[0-9]+(.[0-9]{1,2})?$/) === null) {
        adjustmentAmtValue = '';
        alertNew('The format is wrong');
        return false;
    } else {
        showPopUp();
        jQuery("#commentsAboutAdjustment").val("");
        jQuery("#commentsPopupDiv").css({
            display: "block"
        });
        jQuery("#commentsTitleMessage").text("Reason for making adjustment to rate");
        jQuery("#hiddenIdValue").val(Id);
        jQuery("#hiddenAdjustmentAmtValue").val(adjustmentAmtValue);
        var IpopTop = (screen.height - jQuery("#commentsPopupDiv").attr('offsetHeight')) / 2;
        var IpopLeft = (screen.width - jQuery("#commentsPopupDiv").attr('offsetWidth')) / 2;
        jQuery("#commentsPopupDiv").css({
            'left': IpopLeft + document.body.scrollLeft - 70
        });
        jQuery("#commentsPopupDiv").css({
            'top': IpopTop + document.body.scrollTop - 150
        });

    }
//}
}
function submitCommentsById(Id, adjustmentAmtValue, bookingId) {
    var comments = jQuery('#commentsAboutAdjustment').val();
    var isApplyToallContainerChecked = document.getElementById("applytoallcontainerchkbox").checked;
    if (trim(comments) === "") {
        alertNew("Please Enter Comments!!!");
    } else {
        jQuery('#commentsPopupDiv').css({
            display: "none"
        });
        closePopUp();
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "saveCommentsAboutAdjustment",
                param1: Id,
                param2: comments,
                param3: adjustmentAmtValue,
                param4: bookingId,
                param5: isApplyToallContainerChecked,
                request: "true"
            },
            success: function (data) {
                document.EditBookingsForm.buttonValue.value = "adjustmentChargeComments";
                document.EditBookingsForm.submit();
            }
        });
    }
}
function closeCommentsPopupByElementId(elementIdToClose, Id) {
    closePopUp();
    jQuery('#' + elementIdToClose).css({
        display: "none"
    });
    document.EditBookingsForm.buttonValue.value = "adjustmentChargeComments";
    document.EditBookingsForm.submit();
}
function closeReasonForAmendments() {
    closePopUp();
    jQuery('#reasonForAmendmentsPopupDiv').css({
        display: "none"
    });
}
function getElelmentValueById(elementId) {
    return (jQuery('#' + elementId).val());
}
function showToolTip(comment, w, e) {
    tooltip.showComments(comment, w, e);
}
function SAVE(action) {
    document.getElementById("bookingCompleteY").disabled = true;
    document.getElementById("save").disabled = true;
    document.getElementById("save1").disabled = true;
    //--setting action as empty when save() s not called while deleting PBA charges---
    var length = "";
    var optionValue = "";
    if (action === undefined && action !== 'deletePba') {
        document.EditBookingsForm.action.value = "";
    }

    var etd = document.EditBookingsForm.estimatedDate;
    if (etd.value === "" && document.EditBookingsForm.bookingComplete[0].checked) {
        alertNew("Enter the Estimated Date (ETD) ");
        etd.focus();
        return;
    }

    var bookingdate = document.EditBookingsForm.bookingDate;
    if (bookingdate.value === "") {
        alertNew("Enter the Booking Date");
        bookingdate.focus();
        return;
    }
    if (document.getElementById("emptypickupaddress").value.length >= 200) {
        alertNew("Enter less than 200 character in Equipment Pickup Name/Address");
        return;
    }
    if (document.getElementById("pickUpRemarks").value.length > 100) {
        alertNew("More than 100 characters are not allowed in Equipment Pickup Remarks");
        return;
    }
    if (document.getElementById("addressofDelivery").value.length >= 200) {
        alertNew("Enter less than 200 character in Equipment Return Name/Address");
        return;
    }
    if (document.getElementById("returnRemarks").value.length > 100) {
        alertNew("More than 100 characters are not allowed in Equipment Return Remarks");
        return;
    }
    if (document.getElementById("loadRemarks").value.length > 100) {
        alertNew("More than 100 characters are not allowed in Spotting Address Remarks");
        return;
    }
    if (document.EditBookingsForm.collapseid.value === "") {
        getNumbersChanged();
    }
    setNvoOnLoad();
    document.EditBookingsForm.bundleOfr.value = bundleCheck;
    document.EditBookingsForm.collapseid.value = collapseid;
    if (document.EditBookingsForm.hiddennumbers !== undefined) {
        for (var i = 0; i < document.EditBookingsForm.hiddennumbers.length; i++) {
            if (document.EditBookingsForm.hiddennumbers[i].value === "") {
                alertNew("Quantity Cannot be Zero or empty");
                return;
            }
        }
    }
    var b;
    if (document.EditBookingsForm.hiddenchargeCddesc !== undefined) {
        for (var i = 0; i < document.EditBookingsForm.hiddenchargeCddesc.length; i++) {
            if (document.EditBookingsForm.hiddenchargeCddesc[i].value === "005") {
                if (!document.EditBookingsForm.collapseprint[i].checked) {
                    b = true;
                    alertNew("The Commission Amount will show up in BL");
                    break;
                }
            }
        }
    }
    if (!b) {
        if (document.EditBookingsForm.chargeCddesc !== undefined) {
            for (var i = 0; i < document.EditBookingsForm.chargeCddesc.length; i++) {
                if (document.EditBookingsForm.chargeCddesc[i].value === "005") {
                    if (!document.EditBookingsForm.print[i].checked) {
                        alertNew("The Commission Amount will show up in BL");
                        break;
                    }
                }
            }
        }
    }
    //---making the Page editable-----
    revertPageToEditMode(document.getElementById("editbook"));
    //    mandatoryDOY(document.getElementById('editbook'));

    //checking for Non-Domestic Port----------
    if (document.EditBookingsForm.portOfDischarge.value !== "") {
        var str = document.EditBookingsForm.portOfDischarge.value;
        var temp = new Array();
        temp = str.split("/");
        var code = temp[1];
        var desc = temp[0];
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "checkForNonDomesticPort",
                param1: code,
                param2: desc
            },
            success: function (data) {
                nonDomesticResultCheck(data);
            }
        });
    }
}
function changeBookingNVOMove() {
    if (document.getElementById("doorDestination").value === "") {
        var length = document.getElementById("moveType").length;
        for (var i = 0; i < length; i++) {
            if (document.EditBookingsForm.doorDestination.value !== "") {
                if (document.getElementById("moveType").options[i].text === "DOOR TO PORT") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            } else {
                if (document.getElementById("moveType").options[i].text === "PORT TO DOOR") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            }
        }
    }
}
function selectBookingNVOmove() {
    var length = 0;
    var i = 0;
    if (document.getElementById("doorDestination").value !== "") {
        length = document.getElementById("moveType").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("moveType").options[i].text === "PORT TO DOOR") {
                document.getElementById("moveType").selectedIndex = i;
            }
        }

    } else if (document.getElementById("doorDestination").value === "") {
        length = document.getElementById("moveType").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("moveType").options[i].text === "DOOR TO PORT") {
                document.getElementById("moveType").selectedIndex = i;
            }
        }
    }
}
function nonDomesticResultCheck(data) {
    document.getElementById("save").disabled = true;
    document.getElementById("save1").disabled = true;
    var outYard = document.EditBookingsForm.dateOutYard.value;
    var terminal = document.EditBookingsForm.issuingTerminal.value;
    var temp = new Array();
    temp = terminal.split("-");
    var result = temp[1];
    if (data !== "") {
        if (outYard === "" && data === "K" && (result === "01" || result === "73") && document.EditBookingsForm.bookingComplete[0].checked) {
            document.getElementById("save").disabled = false;
            document.getElementById("save1").disabled = false;
            alertNew("Date Out Of Yard is required to Complete Booking");
            document.EditBookingsForm.bookingComplete[1].checked = true;
            return;
        } else {
            document.getElementById("save").disabled = true;
            document.getElementById("save1").disabled = true;
            document.getElementById("bConf").disabled = true;
            document.getElementById("bConf1").disabled = true;
            document.EditBookingsForm.buttonValue.value = "update";
            document.EditBookingsForm.submit();
        }
    } else {
        document.getElementById("save").disabled = true;
        document.getElementById("save1").disabled = true;
        document.getElementById("bConf").disabled = true;
        document.getElementById("bConf1").disabled = true;
        document.EditBookingsForm.buttonValue.value = "update";
        document.EditBookingsForm.submit();
    }
}
function selectradio() {
    if (special === "Y") {
        document.getElementById("y1").checked = true;
    } else {
        document.getElementById("n1").checked = true;
    }
    if (hazmat === "Y") {
        document.getElementById("y2").checked = true;
    } else {
        document.getElementById("n2").checked = true;
    }
    if (outgage === "Y") {
        document.getElementById("y3").checked = true;
    } else {
        document.getElementById("n3").checked = true;
    }
    if (outgage === "Y") {
        document.getElementById("y3").checked = true;
    } else {
        document.getElementById("n3").checked = true;
    }
    if (insu === "Y") {
        document.getElementById("y8").checked = true;
    } else {
        document.getElementById("n8").checked = true;
    }
    if (document.EditBookingsForm.prepaidToCollect.value === "P") {
        document.getElementById("y3").checked = true;
    } else {
        document.getElementById("n3").checked = true;
    }
}
function disabled(val1) {
    if (val1 === 3) {
        document.getElementById("charges").style.visibility = "hidden";
        document.getElementById("copy").style.visibility = "hidden";
        document.getElementById("conbl").style.visibility = "hidden";
        document.getElementById("save").style.visibility = "hidden";
        document.getElementById("save1").style.visibility = "hidden";
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            if (imgs[k].id !== "previous") {
                imgs[k].style.visibility = "hidden";
            }
        }
        var input = document.getElementsByTagName("input");
        for (i = 0; i < input.length; i++) {
            if (input[i].id !== "buttonValue" && input[i].name !== "govSchCode" && input[i].name !== "termNo" && input[i].name !== "scheduleSuffix" && input[i].name !== "state" && input[i].name !== "country") {
                input[i].readOnly = true;
                input[i].tabIndex = -1;
                input[i].style.color = "blue";
            }
        }
        var textarea = document.getElementsByTagName("textarea");
        for (i = 0; i < textarea.length; i++) {
            textarea[i].readOnly = true;
            textarea[i].tabIndex = -1;
            textarea[i].style.color = "blue";
        }
        var select = document.getElementsByTagName("select");
        for (i = 0; i < select.length; i++) {
            select[i].disabled = true;
            select[i].style.backgroundColor = "blue";
        }
    }
    var datatableobj = document.getElementById("otherChargestable");
    if (datatableobj !== null) {
        for (i = 0; i < datatableobj.rows.length; i++) {
            var tablerowobj = datatableobj.rows[i];
            if (tablerowobj.cells[12].innerHTML === "Y") {
                tablerowobj.cells[0].style.visibility = "visible";
                tablerowobj.cells[0].innerHTML = "*";
            }
        }
    }
    var datatableobj = document.getElementById("chargesTable");
    if (datatableobj !== null) {
        for (i = 0; i < datatableobj.rows.length; i++) {
            var tablerowobj = datatableobj.rows[i];
            if (tablerowobj.cells[14].innerHTML === "Y") {
                tablerowobj.cells[0].style.visibility = "visible";
                tablerowobj.cells[0].innerHTML = "*";
            }
        }
    }

    if (document.EditBookingsForm.amount.value === "0.00") {
        document.EditBookingsForm.amount.readOnly = true;
        document.EditBookingsForm.amount.tabIndex = -1;
    }
    if (document.EditBookingsForm.drayMarkUp.value === "0.00") {
        document.EditBookingsForm.drayMarkUp.readOnly = true;
        document.EditBookingsForm.drayMarkUp.tabIndex = -1;
    }
    if (document.EditBookingsForm.amount1.value === "0.00") {
        document.EditBookingsForm.amount1.readOnly = true;
        document.EditBookingsForm.amount1.tabIndex = -1;
    }
    if (document.EditBookingsForm.interMarkUp.value === "0.00") {
        document.EditBookingsForm.interMarkUp.readOnly = true;
        document.EditBookingsForm.interMarkUp.tabIndex = -1;
    }
    if (document.EditBookingsForm.costofgoods.value === "0.00" || document.EditBookingsForm.costofgoods.value === "0") {
        document.EditBookingsForm.costofgoods.readOnly = true;
        document.EditBookingsForm.costofgoods.tabIndex = -1;
    }
    if (document.EditBookingsForm.insureMarkUp.value === "0.00") {
        document.EditBookingsForm.insureMarkUp.readOnly = true;
        document.EditBookingsForm.insureMarkUp.tabIndex = -1;
    }
}
var newwindow = "";
function add1() {
    document.EditBookingsForm.buttonValue.value = "bookiingpopup";
    document.EditBookingsForm.submit();
    if (!newwindow.closed && newwindow.location) {
        newwindow.location.href = "/logisoft/jsps/fclQuotes/bookingPopUp.jsp";
    } else {
        newwindow = window.open("/logisoft/jsps/fclQuotes/bookingPopUp.jsp", "", "width=700,height=300");
        if (!newwindow.opener) {
            newwindow.opener = self;
        }
    }
    if (window.focus) {
        newwindow.focus();
    }
    return false;
}

function deleteInland() {
    if (document.getElementById("rampCheck").checked) {
        confirmNew("Intermodal Ramp Charges,Door Origin And Zip will be deleted, and all the Booking changes will be saved<br><br> " +
            "Please ensure that NVO Move is Change from Ramp !   Are you sure ?", "IntermodalRamp");
    } else {
        if (importFlag === "true") {
            confirmNew("Delivery Charges,Door Destination And Zip will be deleted, and all the Booking changes will be saved<br><br> " +
                "Please ensure that NVO Move is Change from Door !   Are you sure ?", "inland");
        } else {
            confirmNew("Inland Charges,Door Origin And Zip will be deleted, and all the Booking changes will be saved<br><br> " +
                "Please ensure that NVO Move is Change from Door !   Are you sure ?", "inland");
        }
    }
}
function getinsurance() {
    if (document.EditBookingsForm.bookingId.value === '') {
        alertNew('Please Save Booking,Before Select Insurance');
        document.getElementById("n8").checked = true;
        return;
    } else {
        GB_show("Insurance Rate", "/logisoft/jsps/fclQuotes/calculateInsurance.jsp?costOfGoods=" + document.EditBookingsForm.costofgoods.value + "&insuranceAmount=" + document.EditBookingsForm.insuranceAmount.value, width = "200", height = "300");
    }
}
function getinsurance1() {
    alertBoxNew("Insurance Charges will be deleted from the Grid");

}
function newAlertOkFunction(id) {
    if (id === 'Ok') {
        document.EditBookingsForm.costofgoods.readOnly = true;
        document.EditBookingsForm.costofgoods.tabIndex = -1;
        document.EditBookingsForm.buttonValue.value = "deleteinsurance";
        document.EditBookingsForm.submit();
    }
}
function getHazmat() {
    document.getElementById("commentRemark").value = "Hazardous Cargo";
    confirmNew("This option may add a Hazardous Surcharge to the Rates below and Saves all the Booking changes.INLAND charges will be removed and MUST be recalculated. Are you sure?", "addHazmatRates");
}
function deleteHazmatRates() {
    document.getElementById("commentRemark").value = "";
    confirmNew("This option will delete any Hazardous descriptions you might have entered and Saves all the Booking changes. Are you sure?", "deleteHazmatRates");
}
function recalcfunction() {
    document.EditBookingsForm.buttonValue.value = "recalc";
    document.EditBookingsForm.submit();
}
function getNumbersChanged() {
    collapseid = "expand";
    document.EditBookingsForm.collapseid.value = collapseid;
    saveExpandedBundleOFR();
}
function getNumbersChangedCollapse() {
    collapseid = "collapse";
    document.EditBookingsForm.collapseid.value = collapseid;
    saveCollapsedBundleOFR();
}
function checkAndUnCheckContainer(obj) {
    if (null !== obj) {
        if (obj.name === 'expandCheck') {
            var collapseContainer = document.getElementsByName('collapseCheck');
            var expandUnit = document.getElementsByName('expandCheck');
            var expand = document.getElementsByName('checkSplEqpUnits');
            var splunit = document.getElementsByName('checkStandardCharge');
            for (var i = 0; i < expandUnit.length; i++) {
                collapseContainer[i].checked = expandUnit[i].checked;
                expand[i].checked = expandUnit[i].checked;
                splunit[i].checked = expandUnit[i].checked;
            }
        //            for(var i=0;i<collapseContainer.length;i++) {
        //                if(obj.value === collapseContainer[i].value) {
        //                    collapseContainer[i].checked = obj.checked;
        //                    expand[i].checked = obj.checked;
        //                    splunit[i].checked = obj.checked;
        //                    break;
        //                }
        //            }
        } else {
            var expandContainer = document.getElementsByName('expandCheck');
            var collapseUnit = document.getElementsByName('collapseCheck');
            var collapse = document.getElementsByName('checkSplEqpUnitsCollapse');
            var unit = document.getElementsByName('checkStandardChargeCollapse');
            for (var j = 0; j < collapseUnit.length; j++) {
                expandContainer[j].checked = collapseUnit[j].checked;
                collapse[j].checked = collapseUnit[j].checked;
                unit[j].checked = collapseUnit[j].checked;
            }
        //            for(var j=0;j<expandContainer.length;j++) {
        //                if(obj.value === expandContainer[j].value) {
        //                    expandContainer[j].checked = obj.checked;
        //                    collapse[j].checked = obj.checked;
        //                    unit[j].checked = obj.checked;
        //                    break;
        //                }
        //            }
        }

    }

}
function popupcharges(mylink, windowname) {
    document.EditBookingsForm.buttonValue.value = "recalc";
    document.EditBookingsForm.submit();
    if (!window.focus) {
        return true;
    }
    var href;
    if (typeof (mylink) === "string") {
        href = mylink;
    } else {
        href = mylink.href;
    }
    window.open(href, windowname, "width=500,height=200,scrollbars=yes");
    return false;
}
function titleLetter(ev) {
    if (event.keyCode === 9) {
        window.open("/logisoft/jsps/AccountsRecievable/customerSearch.jsp?button=EditsearchCustShipper&customersearch=" + ev, "", "toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
        document.EditBookingsForm.buttonValue.value = "recalc";
        document.EditBookingsForm.submit();
    }
}
function titleLetter1(ev) {
    if (event.keyCode === 9) {
        window.open("/logisoft/jsps/AccountsRecievable/customerSearch.jsp?button=EditsearchCustForwarder&customersearch=" + ev, "", "toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
        document.EditBookingsForm.buttonValue.value = "recalc";
        document.EditBookingsForm.submit();
    }
}
function titleLetter2(ev) {
    if (event.keyCode === 9) {
        window.open("/logisoft/jsps/AccountsRecievable/customerSearch.jsp?button=EditsearchCustConsignee&customersearch=" + ev, "", "toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
        document.EditBookingsForm.buttonValue.value = "recalc";
        document.EditBookingsForm.submit();
    }
}
function titleLetterName(ev) {
    if (event.keyCode === 9) {
        document.EditBookingsForm.buttonValue.value = "recalc";
        document.EditBookingsForm.submit();
        window.open("/logisoft/jsps/ratemanagement/searchTerminal.jsp?button=EditNewBookingFCLs&tername=" + ev, "", "toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
    }
}
function getDestinationName(ev) {
    if (event.keyCode === 9) {
        document.EditBookingsForm.buttonValue.value = "recalc";
        document.EditBookingsForm.submit();
        window.open("/logisoft/jsps/datareference/SearchPierCode.jsp?button=EditNewBookingFCLs&percodename=" + ev, "", "toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
    }
}
function getForwarder() {
    if (event.keyCode === 9) {
        document.EditBookingsForm.buttonValue.value = "popupsearch";
        document.EditBookingsForm.submit();
    }
}
function getForwarderPress() {
    if (event.keyCode === 13) {
        document.EditBookingsForm.buttonValue.value = "popupsearch";
        document.EditBookingsForm.submit();
    }
}
function getConsignee() {
    if (event.keyCode === 9) {
        document.EditBookingsForm.buttonValue.value = "popupsearch";
        document.EditBookingsForm.submit();
    }
}
function getConsigneePress() {
    if (event.keyCode === 13) {
        document.EditBookingsForm.buttonValue.value = "popupsearch";
        document.EditBookingsForm.submit();
    }
}
function getShipper() {
    if (event.keyCode === 9) {
        document.EditBookingsForm.buttonValue.value = "popupsearch";
        document.EditBookingsForm.submit();
    }
}
function getShipperPress() {
    if (event.keyCode === 13) {
        document.EditBookingsForm.buttonValue.value = "popupsearch";
        document.EditBookingsForm.submit();
    }
}
function getComCode() {
    if (event.keyCode === 9) {
        document.EditBookingsForm.buttonValue.value = "popupsearch";
        document.EditBookingsForm.submit();
    }
}
function getComCodePress() {
    if (event.keyCode === 13) {
        document.EditBookingsForm.buttonValue.value = "popupsearch";
        document.EditBookingsForm.submit();
    }
}
function copy1() {
    document.EditBookingsForm.buttonValue.value = "copy";
    document.EditBookingsForm.submit();
}
function CostBookingsReport(val) {
    if (val === "on") {
        document.EditBookingsForm.buttonValue.value = "CostBookingReportWithoutSave";
    } else {
        document.EditBookingsForm.buttonValue.value = "CostBookingReport";
    }
    document.EditBookingsForm.submit();
}
function getOrigin() {
    if (event.keyCode === 9) {
        document.EditBookingsForm.buttonValue.value = "popupsearch";
        document.EditBookingsForm.submit();
    }
}
function getOriginPress() {
    if (event.keyCode === 13) {
        document.EditBookingsForm.buttonValue.value = "popupsearch";
        document.EditBookingsForm.submit();
    }
}
function getDestination() {
    if (event.keyCode === 9) {
        document.EditBookingsForm.buttonValue.value = "popupsearch";
        document.EditBookingsForm.submit();
    }
}
function getDestinationPress() {
    if (event.keyCode === 13) {
        document.EditBookingsForm.buttonValue.value = "popupsearch";
        document.EditBookingsForm.submit();
    }
}
function CheckedShip() {
    if (document.EditBookingsForm.forwardercheck.disabled || document.EditBookingsForm.consigneecheck.disabled) {
        document.EditBookingsForm.shippercheck.checked = true;
        return;
    } else {
        if (document.EditBookingsForm.shippercheck.checked) {
            document.EditBookingsForm.forwardercheck.checked = false;
            document.EditBookingsForm.consigneecheck.checked = false;
        }
    }
}
function CheckedForwarder() {
    if (document.EditBookingsForm.shippercheck.disabled || document.EditBookingsForm.consigneecheck.disabled) {
        document.EditBookingsForm.forwardercheck.checked = true;
        return;
    } else {
        if (document.EditBookingsForm.forwardercheck.checked) {
            document.EditBookingsForm.shippercheck.checked = false;
            document.EditBookingsForm.consigneecheck.checked = false;
        }
    }
}
function CheckedConsignee() {
    if (document.EditBookingsForm.shippercheck.disabled || document.EditBookingsForm.forwardercheck.disabled) {
        document.EditBookingsForm.consigneecheck.checked = true;
        return;
    } else {
        if (document.EditBookingsForm.consigneecheck.checked) {
            document.EditBookingsForm.shippercheck.checked = false;
            document.EditBookingsForm.forwardercheck.checked = false;
        }
    }
}
function getComCodeDesc(ev) {
    if (event.keyCode === 9 || event.keyCode === 13) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "getComcodeDesc",
                param1: document.EditBookingsForm.commcode.value
            },
            success: function (data) {
                populateComCodeDescDojo(data);
            }
        });
    }
}
function populateComCodeDescDojo(data) {
    if (data) {
        document.getElementById("comdesc").value = data;
    }
}
function getComCode(ev) {
    if (event.keyCode === 9 || event.keyCode === 13) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "getComCode",
                param1: document.EditBookingsForm.comdesc.value
            },
            success: function (data) {
                populateComCodeDojo(data);
            }
        });
    }
}
function populateComCodeDojo(data) {
    if (data) {
        document.getElementById("commcode").value = data;
    }
}
function getShipperInfoForPopUp() {
    getShipperInfo();
}
function getShipperInfo() {
    jQuery(document).ready(function () {
        document.getElementById("shipperContactButton").style.visibility = "visible";
        var preventRun = false;
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "checkForDisable",
                param1: document.getElementById("shipper").value
            },
            success: function (disabled) {
                if (disabled !== "") {
                    alertNew(disabled);
                    document.getElementById("shipperName").value = "";
                    document.getElementById("shipper").value = "";
                    document.getElementById("shipperAddress").value = "";
                    document.getElementById("shipperCity").value = "";
                    document.getElementById("shipperState").value = "";
                    document.getElementById("shipperZip").value = "";
                    document.getElementById("shipperCountry").value = "";
                    document.getElementById("shipPho").value = "";
                    document.getElementById("shipperFax").value = "";
                    document.getElementById("shipperEmail").value = "";
                    preventRun = true;
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                            methodName: "getCustAddressForNo",
                            param1: document.EditBookingsForm.shipper.value,
                            dataType: "json"
                        },
                        success: function (data) {
                            var type;
                            var subtypes;
                            var coName = "";
                            var array1 = new Array();
                            var array2 = new Array();
                            if (data.acctType !== null) {
                                type = data.acctType;
                                array1 = type.split(",");
                            }
                            if (data.subType !== null && data.subType !== undefined) {
                                subtypes = (data.subType).toLowerCase();
                                array2 = subtypes.split(",");
                            }
                            if (array1.contains("S") || array1.contains("F") || array1.contains("E") || array1.contains("I") || (array1.contains("V") && array2.contains("forwarder"))) {
                                if (data.acctNo !== null) {
                                    document.getElementById("shipper").value = data.acctNo;
                                } else {
                                    document.getElementById("shipper").value = "";
                                }
                                if (data.coName !== null && data.coName !== '') {
                                    coName = data.coName;
                                }
                                if (data.address1 !== null && coName !== '') {
                                    document.getElementById("shipperAddress").value = coName + "\n" + data.address1;
                                } else if (data.address1 !== null && coName === '') {
                                    document.getElementById("shipperAddress").value = data.address1;
                                } else {
                                    document.getElementById("shipperAddress").value = "";
                                }
                                if (data.city1 !== null) {
                                    document.getElementById("shipperCity").value = data.city1;
                                } else {
                                    document.getElementById("shipperCity").value = "";
                                }
                                if (data.state !== null) {
                                    document.getElementById("shipperState").value = data.state;
                                } else {
                                    document.getElementById("shipperState").value = "";
                                }
                                if (data.zip !== null) {
                                    document.getElementById("shipperZip").value = data.zip;
                                } else {
                                    document.getElementById("shipperZip").value = "";
                                }
                                if (data.cuntry !== null) {
                                    document.getElementById("shipperCountry").value = data.cuntry.codedesc;
                                } else {
                                    document.getElementById("shipperCountry").value = "";
                                }
                                if (data.phone !== null) {
                                    document.getElementById("shipPho").value = data.phone;
                                } else {
                                    document.getElementById("shipPho").value = "";
                                }
                                if (data.fax !== null) {
                                    document.getElementById("shipperFax").value = data.fax;
                                } else {
                                    document.getElementById("shipperFax").value = "";
                                }
                                if (data.email1 !== null) {
                                    document.getElementById("shipperEmail").value = data.email1;
                                } else {
                                    document.getElementById("shipperEmail").value = "";
                                }
                                setPOA("shipper");
                                isCustomerNotes('shipperIcon', jQuery('#shipper').val());
                            } else {
                                alertNew("Select the Customers with Account Type S,E,I and V with Sub Type Forwarder");
                                document.getElementById("shipperName").value = "";
                                document.getElementById("shipper").value = "";
                                document.getElementById("shipperAddress").value = "";
                                document.getElementById("shipperCity").value = "";
                                document.getElementById("shipperState").value = "";
                                document.getElementById("shipperZip").value = "";
                                document.getElementById("shipperCountry").value = "";
                                document.getElementById("shipPho").value = "";
                                document.getElementById("shipperFax").value = "";
                                document.getElementById("shipperEmail").value = "";
                                jQuery('#shipperIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
                                preventRun = true;
                            }
                        }
                    });
                }
                var importFlag = document.getElementById('importFlag').value;
                var shipperAccnt = document.getElementById("shipper").value;
                if (importFlag === 'false') {
                    addBrandvalueForShippereAccount(shipperAccnt);
                }
                if (!preventRun) {
                    document.EditBookingsForm.shipperAddress.focus();
                }
            }
        });
    });
}

function getShipperInfo1(ev) {
    document.getElementById("shipperName").value = "";
    if (event.keyCode === 0 || event.keyCode === 9 || event.keyCode === 13) {
        getShipperInfo();
    }
}
function onShipperBlur() {
    document.getElementById("shipperName").value = "";
    document.getElementById("shipper").value = "";
    document.getElementById("shipperAddress").value = "";
    document.getElementById("shipperCity").value = "";
    document.getElementById("shipperState").value = "";
    document.getElementById("shipperZip").value = "";
    document.getElementById("shipperCountry").value = "";
    document.getElementById("shipPho").value = "";
    document.getElementById("shipperFax").value = "";
    document.getElementById("shipperEmail").value = "";
    document.getElementById("shipperClientReference").value = "";
    document.getElementById("shipperPoaid").innerHTML = "";
    document.EditBookingsForm.shipperPoa.value = "";
    jQuery('#shipperIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
//document.EditBookingsForm.shipperClientReference.value="";
}
function onTruckerBlur() {
    document.getElementById("truckerName").value = "";
    document.getElementById("truckerCode").value = "";
    document.getElementById("addressoftrucker").value = "";
    document.getElementById("truckerCity").value = "";
    document.getElementById("truckerState").value = "";
    document.getElementById("truckerZip").value = "";
    document.getElementById("truckerPhone").value = "";
    document.getElementById("truckerEmail").value = "";
    document.getElementById("truckerClientReference").value = "";
}
function onSpottAddrrBlur() {
    document.getElementById("spottingAccountName").value = "";
    document.getElementById("spottingAccountNo").value = "";
    document.getElementById("addressofExpPosition").value = "";
    document.getElementById("spotAddrCity").value = "";
    document.getElementById("spotAddrState").value = "";
    document.getElementById("spotAddrZip").value = "";

}

function getForwarderInfoForPopUp() {
    getForwarderInfo();
}

function getForwarderInfo() {
    jQuery(document).ready(function () {
        if (document.EditBookingsForm.billToCode[0].checked && (document.EditBookingsForm.fowardername.value.trim() === "NO FF ASSIGNED"
            || document.EditBookingsForm.fowardername.value.trim() === "NO FF ASSIGNED / B/L PROVIDED"
            || document.EditBookingsForm.fowardername.value.trim() === "NO FRT. FORWARDER ASSIGNED")) {
            alertNew("Please change Bill To Party or change Forwarder, because Forwarder is NO FF ASSIGNED");
            document.EditBookingsForm.fowardername.value = "";
            document.EditBookingsForm.forwarder.value = "";
            document.EditBookingsForm.forwarderAddress.value = "";
            document.EditBookingsForm.forwarderCity.value = "";
            document.EditBookingsForm.forwarderZip.value = "";
            document.EditBookingsForm.forwarderCountry.value = "";
            document.EditBookingsForm.forwarderPhone.value = "";
            document.EditBookingsForm.forwarderFax.value = "";
            document.EditBookingsForm.forwarderEmail.value = "";
            return;
        } else {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "checkForDisable",
                    param1: document.getElementById("forwarder").value
                },
                success: function (disabled) {
                    if (disabled !== "") {
                        alertNew(disabled);
                        document.getElementById("fowardername").value = "";
                        document.getElementById("forwarder").value = "";
                        document.getElementById("forwarderAddress").value = "";
                        document.getElementById("forwarderCity").value = "";
                        document.getElementById("forwarderState").value = "";
                        document.getElementById("forwarderZip").value = "";
                        document.getElementById("forwarderCountry").value = "";
                        document.getElementById("forwarderPhone").value = "";
                        document.getElementById("forwarderFax").value = "";
                        document.getElementById("forwarderEmail").value = "";
                    } else {
                        jQuery.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                methodName: "getCustAddressForNo",
                                param1: document.EditBookingsForm.forwarder.value,
                                dataType: "json"
                            },
                            success: function (data) {
                                fillForwarder(data);
                            }
                        });
                    }
                }
            });
            var importFlag = document.getElementById('importFlag').value;
            
            var forwardAccnt = document.EditBookingsForm.forwarder.value;
           
            if (importFlag === 'false') {
               
                addBrandvalueForForwardAccount(forwardAccnt);
            }
        }
        document.getElementById("forwarder").focus();
    });
}

function onForwarderBlur() {
    document.getElementById("fowardername").value = "";
    document.getElementById("forwarder").value = "";
    document.EditBookingsForm.forwarderAddress.value = "";
    document.EditBookingsForm.forwarderCity.value = "";
    document.EditBookingsForm.forwarderState.value = "";
    document.EditBookingsForm.forwarderZip.value = "";
    document.EditBookingsForm.forwarderCountry.value = "";
    document.EditBookingsForm.forwarderPhone.value = "";
    document.EditBookingsForm.forwarderFax.value = "";
    document.getElementById("forwarderEmail").value = "";
    document.EditBookingsForm.forwarderClientReference.value = "";
    document.getElementById("forwarderPoaid").innerHTML = "";
    document.EditBookingsForm.forwarderPoa.value = "";
    jQuery('#forwarderIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
}

function fillForwarder(data) {
    var array1 = new Array();
    var type;
    var coName = "";
    if (data.acctType !== null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (data.subType !== null && data.subType !== undefined && (((data.subType).toLowerCase()) === 'forwarder')) {
        document.getElementById("fowardername").value = data.acctName;
        if (data.acctNo !== null) {
            document.getElementById("forwarder").value = data.acctNo;
        } else {
            document.getElementById("forwarder").value = "";
        }
        if (data.coName !== null && data.coName !== '') {
            coName = data.coName;
        }
        if (data.address1 !== null && coName !== '') {
            document.getElementById("forwarderAddress").value = coName + "\n" + data.address1;
        } else if (data.address1 !== null && coName === '') {
            document.getElementById("forwarderAddress").value = data.address1;
        } else {
            document.getElementById("forwarderAddress").value = "";
        }
        if (data.city1 !== null) {
            document.getElementById("forwarderCity").value = data.city1;
        } else {
            document.getElementById("forwarderCity").value = "";
        }
        if (data.state !== null) {
            document.getElementById("forwarderState").value = data.state;
        } else {
            document.getElementById("forwarderState").value = "";
        }
        if (data.zip !== null) {
            document.getElementById("forwarderZip").value = data.zip;
        } else {
            document.getElementById("forwarderZip").value = "";
        }
        if (data.cuntry !== null) {
            document.getElementById("forwarderCountry").value = data.cuntry.codedesc;
        } else {
            document.getElementById("forwarderCountry").value = "";
        }
        if (data.phone !== null) {
            document.getElementById("forwarderPhone").value = data.phone;
        } else {
            document.getElementById("forwarderPhone").value = "";
        }
        if (data.fax !== null) {
            document.getElementById("forwarderFax").value = data.fax;
        } else {
            document.getElementById("forwarderFax").value = "";
        }
        if (data.email1 !== null) {
            document.getElementById("forwarderEmail").value = data.email1;
        } else {
            document.getElementById("forwarderEmail").value = "";
        }
        setPOA("forwarder");
        isCustomerNotes('forwarderIcon', jQuery('#forwarder').val());
    } else {
        alertNew("Select the Customers with Vendor (Sub type Forwarder)");
        document.getElementById("fowardername").value = "";
        document.getElementById("forwarder").value = "";
        document.getElementById("forwarderAddress").value = "";
        document.getElementById("forwarderCity").value = "";
        document.getElementById("forwarderState").value = "";
        document.getElementById("forwarderZip").value = "";
        document.getElementById("forwarderCountry").value = "";
        document.getElementById("forwarderPhone").value = "";
        document.getElementById("forwarderFax").value = "";
        document.getElementById("forwarderEmail").value = "";
        jQuery('#forwarderIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
        return;
    }
}

function getForwarderInfo1(ev) {
    document.getElementById("fowardername").value = "";
    if (event.keyCode === 9 || event.keyCode === 13) {
        getForwarderInfo();
    }
}

function getConsigneeInfoForPopUp() {
    getConsigneeInfo();
}
function getConsigneeInfo() {
    jQuery(document).ready(function () {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "checkForDisable",
                param1: document.getElementById("consignee").value
            },
            success: function (disabled) {
                if (disabled !== "") {
                    alertNew(disabled);
                    document.getElementById("consigneename").value = "";
                    document.getElementById("consignee").value = "";
                    document.getElementById("consigneeAddress").value = "";
                    document.getElementById("consigneeCity").value = "";
                    document.getElementById("consigneeState").value = "";
                    document.getElementById("consigneeZip").value = "";
                    document.getElementById("consigneeCountry").value = "";
                    document.getElementById("consigneePhone").value = "";
                    document.getElementById("consigneeFax").value = "";
                    document.getElementById("consigneeEmail").value = "";
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                            methodName: "getCustAddressForNo",
                            param1: document.EditBookingsForm.consignee.value,
                            dataType: "json"
                        },
                        success: function (data) {
                            populateConsignee(data);
                        }
                    });
                }
            }
        });
        document.getElementById("consignee").focus();
    });
}
function onConsigneeBlur() {
    document.getElementById("consigneename").value = "";
    document.getElementById("consignee").value = "";
    document.getElementById("consigneeAddress").value = "";
    document.getElementById("consigneeCity").value = "";
    document.getElementById("consigneeState").value = "";
    document.getElementById("consigneeZip").value = "";
    document.getElementById("consigneeCountry").value = "";
    document.getElementById("consigneePhone").value = "";
    document.getElementById("consigneeFax").value = "";
    document.getElementById("consigneeEmail").value = "";
    document.getElementById("consigneeClientReference").value = "";
    document.getElementById("ConsigneePoaid").innerHTML = "";
    document.EditBookingsForm.consigneePoa.value = "";
    jQuery('#consigneeIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
//document.EditBookingsForm.consigneeClientReference.value="";
}

function populateConsignee(data) {
    var type;
    var coName = "";
    var array1 = new Array();
    if (data.acctType !== null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (array1.contains("C")) {
        document.getElementById("consigneename").value = data.acctName;
        if (data.acctNo !== null) {
            document.getElementById("consignee").value = data.acctNo;
        } else {
            document.getElementById("consignee").value = "";
        }
        if (data.coName !== null && data.coName !== '') {
            coName = data.coName;
        }
        if (data.address1 !== null && coName !== '') {
            document.getElementById("consigneeAddress").value = coName + "\n" + data.address1;
        } else if (data.address1 !== null && coName === '') {
            document.getElementById("consigneeAddress").value = data.address1;
        } else {
            document.getElementById("consigneeAddress").value = "";
        }
        if (data.city1 !== null) {
            document.getElementById("consigneeCity").value = data.city1;
        } else {
            document.getElementById("consigneeCity").value = "";
        }
        if (data.state !== null) {
            document.getElementById("consigneeState").value = data.state;
        } else {
            document.getElementById("consigneeState").value = "";
        }
        if (data.zip !== null) {
            document.getElementById("consigneeZip").value = data.zip;
        } else {
            document.getElementById("consigneeZip").value = "";
        }
        if (data.cuntry !== null) {
            document.getElementById("consigneeCountry").value = data.cuntry.codedesc;
        } else {
            document.getElementById("consigneeCountry").value = "";
        }
        if (data.phone !== null) {
            document.getElementById("consigneePhone").value = data.phone;
        } else {
            document.getElementById("consigneePhone").value = "";
        }
        if (data.fax !== null) {
            document.getElementById("consigneeFax").value = data.fax;
        } else {
            document.getElementById("consigneeFax").value = "";
        }
        if (data.email1 !== null) {
            document.getElementById("consigneeEmail").value = data.email1;
        } else {
            document.getElementById("consigneeEmail").value = "";
        }
        setPOA("consignee");
        isCustomerNotes('consigneeIcon', jQuery('#consignee').val());
    } else {
        alertNew("Select the Customers with Account Type C");
        document.getElementById("consigneename").value = "";
        document.getElementById("consignee").value = "";
        document.getElementById("consigneeAddress").value = "";
        document.getElementById("consigneeCity").value = "";
        document.getElementById("consigneeState").value = "";
        document.getElementById("consigneeZip").value = "";
        document.getElementById("consigneeCountry").value = "";
        document.getElementById("consigneePhone").value = "";
        document.getElementById("consigneeFax").value = "";
        document.getElementById("consigneeEmail").value = "";
        jQuery('#consigneeIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
        return;
    }
}

function getConsigneeInfo1(ev) {
    document.getElementById("consigneename").value = "";
    if (event.keyCode === 9 || event.keyCode === 13) {
        getConsigneeInfo();
    }
}

function getTruckerInfoForPopUp() {
    getTruckerInfo();
}
function getTruckerInfo() {
    if (event.keyCode === 9 || event.keyCode === 13 || event.keyCode === 0) {
        if (document.EditBookingsForm.truckerName.value !== "") {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "checkForDisable",
                    param1: document.getElementById("truckerCode").value
                },
                success: function (disabled) {
                    if (disabled !== "") {
                        alertNew(disabled);
                        document.getElementById("truckerName").value = "";
                        document.getElementById("truckerCode").value = "";
                        document.getElementById("addressoftrucker").value = "";
                        document.getElementById("truckerCity").value = "";
                        document.getElementById("truckerState").value = "";
                        document.getElementById("truckerZip").value = "";
                        document.getElementById("truckerPhone").value = "";
                        document.getElementById("truckerEmail").value = "";
                    } else {
                        jQuery.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                methodName: "getCustAddressForNo",
                                param1: document.EditBookingsForm.truckerCode.value,
                                dataType: "json"
                            },
                            success: function (data) {
                                populateTrucker(data);
                            }
                        });
                    }
                }
            });
        }
    }
}

function populateTrucker(data) {
    document.getElementById("truckerContactButton").style.visibility = "visible";
    if (data) {
        var type;
        var coName = "";
        var array1 = new Array();
        if (data.acctType !== null) {
            type = data.acctType;
            array1 = type.split(",");
        }
        if (array1.contains("V")) {
            document.getElementById("truckerName").value = data.acctName;
            document.getElementById("trucker_check").value = data.acctName;
            if (data.acctNo !== null) {
                document.getElementById("truckerCode").value = data.acctNo;
            } else {
                document.getElementById("truckerCode").value = "";
            }
            if (data.coName !== null && data.coName !== '') {
                coName = data.coName;
            }
            if (data.address1 !== null && coName !== '') {
                document.getElementById("addressoftrucker").value = coName + "\n" + data.address1;
            } else if (data.address1 !== null && coName === '') {
                document.getElementById("addressoftrucker").value = data.address1;
            } else {
                document.getElementById("addressoftrucker").value = "";
            }
            if (data.city1 !== null) {
                document.getElementById("truckerCity").value = data.city1;
            } else {
                document.getElementById("truckerCity").value = "";
            }
            if (data.state !== null) {
                document.getElementById("truckerState").value = data.state;
            } else {
                document.getElementById("truckerState").value = "";
            }
            if (data.zip !== null) {
                document.getElementById("truckerZip").value = data.zip;
            } else {
                document.getElementById("truckerZip").value = "";
            }
            if (data.phone !== null) {
                document.getElementById("truckerPhone").value = data.phone;
            } else {
                document.getElementById("truckerPhone").value = "";
            }
            if (data.email1 !== null) {
                document.getElementById("truckerEmail").value = data.email1;
            } else {
                document.getElementById("truckerEmail").value = "";
            }
        } else {
            alertNew("Select the Customers with Account Type V");
            document.getElementById("truckerName").value = "";
            document.getElementById("truckerCode").value = "";
            document.getElementById("addressoftrucker").value = "";
            document.getElementById("truckerCity").value = "";
            document.getElementById("truckerState").value = "";
            document.getElementById("truckerZip").value = "";
            document.getElementById("truckerPhone").value = "";
            document.getElementById("truckerEmail").value = "";
            return;
        }
    }
}

function getTruckerInfo1(ev) {
    document.getElementById("truckerName").value = "";
    if (event.keyCode === 9 || event.keyCode === 13) {
        getTruckerInfo();
    }
}
function getTruckerFromSSline() {
    if (document.getElementById("truckerCheckbox").checked) {
        if (document.EditBookingsForm.sslDescription.value !== "") {
            var tr = document.EditBookingsForm.sslDescription.value.split("//");
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "getCustAddressForNo",
                    param1: tr[1],
                    dataType: "json"
                },
                success: function (data) {
                    populateTruckerInfo1(data);
                }
            });
        }
    } else {
        document.getElementById("truckerName").value = "";
        document.getElementById("truckerCode").value = "";
        document.getElementById("addressoftrucker").value = "";
        document.getElementById("truckerCity").value = "";
        document.getElementById("truckerState").value = "";
        document.getElementById("truckerZip").value = "";
        document.getElementById("truckerPhone").value = "";
        document.getElementById("truckerEmail").value = "";
    }
}

function populateTruckerInfo1(data) {
    document.getElementById("truckerContactButton").style.visibility = "visible";
    if (data) {
        if (data.acctName !== null) {
            document.getElementById("truckerName").value = data.acctName;
        }
        if (null !== data.acctNo) {
            document.getElementById("truckerCode").value = data.acctNo;
        }
        else {
            document.getElementById("truckerCode").value = "";
        }
        if (null !== data.address1) {
            document.getElementById("addressoftrucker").value = data.address1;
        } else {
            document.getElementById("addressoftrucker").value = "";
        }
        if (null !== data.city1) {
            document.getElementById("truckerCity").value = data.city1;
        } else {
            document.getElementById("truckerCity").value = "";
        }
        if (null !== data.state) {
            document.getElementById("truckerState").value = data.state;
        } else {
            document.getElementById("truckerState").value = "";
        }
        if (null !== data.zip) {
            document.getElementById("truckerZip").value = data.zip;
        } else {
            document.getElementById("truckerZip").value = "";
        }
        if (null !== data.phone) {
            document.getElementById("truckerPhone").value = data.phone;
        }
        else {
            document.getElementById("truckerPhone").value = "";
        }
        if (null !== data.email1) {
            document.getElementById("truckerEmail").value = data.email1;
        } else {
            document.getElementById("truckerEmail").value = "";
        }
    }
}

function makeFormBorderless(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type === "button") {
            if (element.id === "copy" || element.value === "Charges" || element.value === "Recalc" || element.value === "ConvertToBl" ||
                element.value === "Look Up" || element.value === "Insurance" || element.value === "Hazmat" || element.value === "Get Rates" ||
                element.value === "Input Rates Manually" || element.value === "Inbond" ||
                element.value === "Add to Booking" || element.value === "Update Existing" || element.value === "IMS") {
                element.style.visibility = "hidden";
            }
        }
        if (element.type === "text" || element.type === "textarea") {
            if (element.id === "vessel" || element.id === "ssVoy") {
                element.style.borderTop = "1px solid #C4C5C4";
                element.style.borderBottom = "1px solid #C4C5C4";
                element.style.borderRight = "1px solid #C4C5C4";
                element.style.borderLeft = "red 2px solid";
                element.style.backgroundColor = "#FCFCFC";
                element.readOnly = false;
                element.tabIndex = 0;
                element.className = "textlabelsBoldForTextBox";
            } else {
                element.style.backgroundColor = "#CCEBFF";
                if (element.id === "SSBooking" || element.id === "txtcal313" || element.id === "txtcal71" || element.id === "txtcal22"
                    || element.id === "txtcal5" || element.id === "fowardername" || element.id === "portOfDischarge" || element.id === "originTerminal" || element.id === "issuingTerminal") {
                    element.style.borderLeft = "red 2px solid";
                }
            }

        }
        if (element.type === "select-one") {
            element.style.border = 0;
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
    }
}
function getShipper() {
    var cust = document.EditBookingsForm.shipperName.value;
    if (cust === "%") {
        cust = "percent";
    }
    cust = cust.replace("&", "amp;");
    GB_show("Shipper", "/logisoft/quoteCustomer.do?buttonValue=BookingShipper&clientName=" + cust, width = "400", height = "810");
}
function getForwarder() {
    var cust = document.EditBookingsForm.fowardername.value;
    if (cust === "%") {
        cust = "percent";
    }
    cust = cust.replace("&", "amp;");
    GB_show("Forwarder", "/logisoft/quoteCustomer.do?buttonValue=BookingForwarder&clientName=" + cust, width = "400", height = "810");
}
function getConsignee() {
    var cust = document.EditBookingsForm.consigneename.value;
    if (cust === "%") {
        cust = "percent";
    }
    cust = cust.replace("&", "amp;");
    GB_show("Consignee", "/logisoft/quoteCustomer.do?buttonValue=BookingConsignee&clientName=" + cust, width = "400", height = "810");
}
function getTrukerName() {
    var cust = document.EditBookingsForm.truckerName.value;
    var customer = cust.replace("&", "amp;");
    GB_show("Trucker", "/logisoft/quoteCustomer.do?buttonValue=BookingTruker&clientName=" + customer, width = "400", height = "810");
}
function getBookingCustomer(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12) {
    var accountNo = "";
    if (val4 === "AccountNameThirdParty") {
        accountNo = val2;
    } else {
        accountNo = val4;
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "checkForDisable",
            param1: accountNo
        },
        success: function (disabled) {
            if (disabled !== "") {
                alertNew(disabled);
            } else {
                getBookingCustomer1(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12);
            }
        }
    });
}

function getBookingCustomer1(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12) {
    val3 = val3.replace(":", "'");
    val3 = val3.replace(";;", "&");
    array1 = val2.split(",");
    val1 = val1.replace(":", "'");
    if (val1 === "BookingTruker") {
        if (array1.contains("V")) {
            document.getElementById("truckerContactButton").style.visibility = "visible";
            document.EditBookingsForm.truckerName.value = val3;
            document.EditBookingsForm.truckerCode.value = val4;
            document.EditBookingsForm.addressoftrucker.value = val5;
            document.EditBookingsForm.truckerPhone.value = val10;
            document.EditBookingsForm.truckerEmail.value = val12;
        } else {
            alertNew("Select the customer with Account Type V");
            document.EditBookingsForm.truckerName.value = "";
            document.EditBookingsForm.truckerCode.value = "";
            document.EditBookingsForm.addressoftrucker.value = "";
            document.EditBookingsForm.truckerPhone.value = "";
            document.EditBookingsForm.truckerEmail.value = "";
            return;
        }
    }
    if (val1 === "BookingShipper") {
        document.getElementById("shipperContactButton").style.visibility = "visible";
        if (array1.contains("S") || array1.contains("F") || array1.contains("I") || array1.contains("E")) {
            document.EditBookingsForm.shipperName.value = val3;
            document.EditBookingsForm.shipper.value = val4;
            document.EditBookingsForm.shipperAddress.value = val5;
            document.EditBookingsForm.shipperCity.value = val6;
            document.EditBookingsForm.shipperState.value = val7;
            document.EditBookingsForm.shipperZip.value = val8;
            document.EditBookingsForm.shipperCountry.value = val9;
            document.EditBookingsForm.shipPho.value = val10;
            document.EditBookingsForm.shipperFax.value = val11;
            document.EditBookingsForm.shipEmai.value = val12;
        } else {
            alertNew("Select the customers with Account Type S,F,E or I");
            document.getElementById("shipperName").value = "";
            document.getElementById("shipper").value = "";
            document.getElementById("shipperAddress").value = "";
            document.getElementById("shipperCity").value = "";
            document.getElementById("shipperState").value = "";
            document.getElementById("shipperZip").value = "";
            document.getElementById("shipperCountry").value = "";
            document.getElementById("shipPho").value = "";
            document.getElementById("shipperFax").value = "";
            document.getElementById("shipperEmail").value = "";
            return;
        }
    }
    if (val1 === "BookingForwarder") {
        if (array1.contains("F")) {
            document.getElementById("forwarderContactButton").style.visibility = "visible";
            document.EditBookingsForm.fowardername.value = val3;
            document.EditBookingsForm.forwarder.value = val4;
            document.EditBookingsForm.forwarderAddress.value = val5;
            document.EditBookingsForm.forwarderCity.value = val6;
            document.EditBookingsForm.forwarderState.value = val7;
            document.EditBookingsForm.forwarderZip.value = val8;
            document.EditBookingsForm.forwarderCountry.value = val9;
            document.EditBookingsForm.forwarderPhone.value = val10;
            document.EditBookingsForm.forwarderFax.value = val11;
            document.EditBookingsForm.forwarderEmail.value = val12;
        } else {
            alertNew("Select the customers with Account Type F");
            document.getElementById("fowardername").value = "";
            document.getElementById("forwarder").value = "";
            document.getElementById("forwarderAddress").value = "";
            document.getElementById("forwarderCity").value = "";
            document.getElementById("forwarderState").value = "";
            document.getElementById("forwarderZip").value = "";
            document.getElementById("forwarderCountry").value = "";
            document.getElementById("forwarderPhone").value = "";
            document.getElementById("forwarderFax").value = "";
            document.getElementById("forwarderEmail").value = "";
            return;
        }
    }
    if (val1 === "BookingConsignee") {
        if (array1.contains("C")) {
            document.getElementById("congineeContactButton").style.visibility = "visible";
            val1 = val1.replace(":", "'");
            document.EditBookingsForm.consigneename.value = val3;
            document.EditBookingsForm.consignee.value = val4;
            document.EditBookingsForm.consigneeAddress.value = val5;
            document.EditBookingsForm.consigneeCity.value = val6;
            document.EditBookingsForm.consigneeState.value = val7;
            document.EditBookingsForm.consigneeZip.value = val8;
            document.EditBookingsForm.consigneeCountry.value = val9;
            document.EditBookingsForm.consigneePhone.value = val10;
            document.EditBookingsForm.consigneeFax.value = val11;
            document.EditBookingsForm.consigneeEmail.value = val12;
        } else {
            alertNew("Select the customers with Account Type C");
            document.getElementById("consigneename").value = "";
            document.getElementById("consignee").value = "";
            document.getElementById("consigneeAddress").value = "";
            document.getElementById("consigneeCity").value = "";
            document.getElementById("consigneeState").value = "";
            document.getElementById("consigneeZip").value = "";
            document.getElementById("consigneeCountry").value = "";
            document.getElementById("consigneePhone").value = "";
            document.getElementById("consigneeFax").value = "";
            document.getElementById("consigneeEmail").value = "";
            return;
        }
    }
    if (val1 === "AccountNameShipper") {
        val1 = val1.replace(":", "'");
        document.EditBookingsForm.accountName.value = val3;
        document.EditBookingsForm.accountNumber.value = val4;
    }
    if (val1 === "AccountNameForwarder") {
        val1 = val1.replace(":", "'");
        document.EditBookingsForm.accountName.value = val3;
        document.EditBookingsForm.accountNumber.value = val4;
    }
    if (val4 === "AccountNameThirdParty") {
        val1 = val1.replace(":", "'");
        document.EditBookingsForm.accountName.value = val1;
        document.EditBookingsForm.accountNumber.value = val2;
    }
    if (val1 === "AccountNameAgent") {
        val1 = val1.replace(":", "'");
        document.EditBookingsForm.accountName.value = val3;
        document.EditBookingsForm.accountNumber.value = val4;
    }
}
function getShipperAddress() {
    var cust = document.EditBookingsForm.shipperName.value;
    cust = cust.replace("&", "amp;");
    if (document.EditBookingsForm.shipper.value === "") {
        alertNew("Please Select Account Name");
        return;
    }
    var addr = "percent";
    GB_show("Shipper", "/logisoft/contactAddress.do?buttonValue=BookingShipper&clientNo=" + document.EditBookingsForm.shipper.value + "&address=" + addr, width = "400", height = "800");
}
function getForwarderAddress() {
    if (document.EditBookingsForm.fowardername.value === "") {
        alertNew("Please Select Account Name");
        return;
    }
    var addr = "percent";
    GB_show("Forwarder", "/logisoft/contactAddress.do?buttonValue=BookingForwarder&clientNo=" + document.EditBookingsForm.forwarder.value + "&address=" + addr, width = "400", height = "800");
}
function getConsigneeAddress() {
    if (document.EditBookingsForm.consigneename.value === "") {
        alertNew("Please Select Account Name");
        return;
    }
    var addr = "percent";
    GB_show("Consignee", "/logisoft/contactAddress.do?buttonValue=BookingConsignee&clientNo=" + document.EditBookingsForm.consignee.value + "&address=" + addr, width = "400", height = "800");
}
function getTruckerAddress() {
    if (document.EditBookingsForm.truckerName.value === "") {
        alertNew("Please Select Account Name");
        return;
    }
    var addr = "percent";
    GB_show("Trucker", "/logisoft/contactAddress.do?buttonValue=BookingTruker&clientNo=" + document.EditBookingsForm.truckerCode.value + "&address=" + addr, width = "400", height = "800");
}
function refreshPage(val1, val2, val3, val4, val5, val6, val7, val8) {
    document.EditBookingsForm.unitSelect.value = val1;
    document.EditBookingsForm.number.value = val2;
    document.EditBookingsForm.chargeCode.value = val3;
    document.EditBookingsForm.chargeCodeDesc.value = val4;
    document.EditBookingsForm.costSelect.value = val5;
    document.EditBookingsForm.currency1.value = val6;
    document.EditBookingsForm.chargeAmt.value = val7;
    document.EditBookingsForm.minimumAmt.value = val8;
    document.EditBookingsForm.buttonValue.value = "addCharges";
    document.EditBookingsForm.submit();
}
function getAccountDetails(val2) {
    var cust = document.EditBookingsForm.accountName.value;
    if (cust === "%") {
        cust = "percent";
    }
    cust = cust.replace("&", "amp;");
    GB_show("ThirdParty", "/logisoft/quoteCustomer.do?buttonValue=AccountNameThirdParty&clientName=" + cust, width = "400", height = "810");
}
function calculateInsurance(val1, val2) {
    if (document.EditBookingsForm.collapseid !== null && document.EditBookingsForm.collapseid.value === "") {
        getNumbersChanged();
    }
    document.EditBookingsForm.bundleOfr.value = bundleCheck;
    document.EditBookingsForm.collapseid.value = collapseid;
    document.EditBookingsForm.costofgoods.value = val1;
    document.EditBookingsForm.insuranceAmount.value = val2;
    document.EditBookingsForm.buttonValue.value = "insurance";
    document.EditBookingsForm.submit();
}
function getAgent() {
    var portOfDischarge = document.EditBookingsForm.portOfDischarge.value;
    var destination = document.EditBookingsForm.destination.value;
    GB_show("Agent", "/logisoft/quoteAgent.do?buttonValue=Agent&portOfDischarge=" + portOfDischarge + "&destination=" + destination, width = "400", height = "600");
}
function setDojoAction() {
    var path = "";
    var portOfDischarge = document.EditBookingsForm.portOfDischarge.value;
    var destination = document.EditBookingsForm.destination.value;
    path = "&portOfDischarge=" + portOfDischarge + "&destination=" + destination;
    appendEncodeUrl(path);
}
function getAgentInfo(val1, val2) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function (disabled) {
            if (data !== "") {
                alertNew(data);
                document.EditBookingsForm.agent.value = "";
                document.EditBookingsForm.agentNo.value = "";
            } else {
                document.EditBookingsForm.agent.value = val1;
                document.EditBookingsForm.agentNo.value = val2;
            }
        }
    });
}
function getDestination() {
    if (document.EditBookingsForm.portOfDischarge.value === "") {
        var pod = document.EditBookingsForm.destination.value;
        var index = pod.indexOf("/");
        var podNew = pod.substring(0, index);
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "getAgent",
                param1: podNew
            },
            success: function (data) {
                populateAgentDojo(data);
            }
        });
    }
}
function populateAgentDojo(data) {
    if (data === "true") {
        document.getElementById("agent").value = "";
        document.getElementById("agentNo").value = "";
    }
}
function getPod() {
    if (document.EditBookingsForm.destination.value === "") {
        var pod = document.EditBookingsForm.portOfDischarge.value;
        var index = pod.indexOf("/");
        var podNew = pod.substring(0, index);
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "getAgent",
                param1: podNew
            },
            success: function (data) {
                populateAgent1Dojo(data);
            }
        });
    }
}
function populateAgent1Dojo(data) {
    if (data === "true") {
        document.getElementById("agent").value = "";
        document.getElementById("agentNo").value = "";
    //        document.getElementById("agentlookup").style.visibility = "hidden";
    }
}
function getSslCode() {
    var cust = document.EditBookingsForm.sslDescription.value;
    var customer = cust.replace("&", "amp;");
    if (customer === "%") {
        customer = "percent";
    }
    GB_show("Client Info", "/logisoft/quoteCustomer.do?buttonValue=CarrierQuotation&clientName=" + customer, width = "400", height = "810");
}
function getVesselName() {
    var cust = document.EditBookingsForm.vessel.value;
    if (cust === "%") {
        cust = "percent";
    }
    GB_show("Vessel Name", "/logisoft/vesselLookUp.do?buttonValue=bookingVessel&clientName=" + cust, width = "400", height = "500");
}
function getCarrier(val1, val2, val3, val4, val5, val6, val7) {
    document.EditBookingsForm.sslDescription.value = val1 + "/" + val2;
}
function setAddress(ev) {
    if (document.EditBookingsForm.emptypickupaddress.value) {
        if (document.getElementById("autoAddressFill").checked) {
            document.EditBookingsForm.addressofDelivery.value = document.EditBookingsForm.emptypickupaddress.value;
        }
    }
}
function goBack() {
    confirmYesNoCancel("Do you want to save the Booking changes?", "goBack");
}
function yesFunction() {
    //--yui method when clicked on yes-----
    var errorMessage = '';
    if (document.EditBookingsForm.portOfDischarge.value === '') {
        errorMessage += "-->Please Enter Destination<br>";
        jQuery("#portOfDischarge").css("border-color", "red");
    }
    if (document.EditBookingsForm.originTerminal.value === '') {
        errorMessage += "-->Please Enter Origin Terminal<br>";
        jQuery("#originTerminal").css("border-color", "red");
    }
    if (document.EditBookingsForm.issuingTerminal.value === '') {
        errorMessage += "-->Please Enter Issuing Terminal<br>";
        jQuery("#issuingTerminal").css("border-color", "red");
    }
    if (errorMessage !== '') {
        alertNew(errorMessage);
        return false;
    }
    var etd = document.EditBookingsForm.estimatedDate;
    if (etd.value === "" && document.EditBookingsForm.bookingComplete[0].checked) {
        alertNew("Enter the Estimated Date (ETD) ");
        etd.focus();
        return false;
    }
    var bookingdate = document.EditBookingsForm.bookingDate;
    if (bookingdate.value === "") {
        alertNew("Enter the Booking Date");
        bookingdate.focus();
        return false;
    }
    if (document.EditBookingsForm.hiddennumbers !== undefined) {
        for (var i = 0; i < document.EditBookingsForm.hiddennumbers.length; i++) {
            if (document.EditBookingsForm.hiddennumbers[i].value === "") {
                alertNew("Quantity Cannot be Zero or empty");
                return false;
            }
        }
    }
    if (document.getElementById("emptypickupaddress").value.length >= 200) {
        alertNew("Enter less than 200 character in Equipment Pickup Name/Address");
        return;
    }
    if (document.getElementById("pickUpRemarks").value.length > 100) {
        alertNew("More than 100 characters are not allowed in Equipment Pickup Remarks");
        return;
    }
    if (document.getElementById("addressofDelivery").value.length >= 200) {
        alertNew("Enter less than 200 character in Equipment Return Name/Address");
        return;
    }
    if (document.getElementById("returnRemarks").value.length > 100) {
        alertNew("More than 100 characters are not allowed in Equipment Return Remarks");
        return;
    }
    if (document.getElementById("loadRemarks").value.length > 100) {
        alertNew("More than 100 characters are not allowed in Spotting Address Remarks");
        return;
    }
    //---making the Page editable-----
    revertPageToEditMode(document.getElementById("editbook"));
    document.EditBookingsForm.buttonValue.value = "goBackSave";
    document.EditBookingsForm.submit();
}
function noFunction() {
    //--yui method when clicked on No-----
    document.EditBookingsForm.buttonValue.value = "goBack";
    jQuery("#cancel").attr("disabled", true);
    document.EditBookingsForm.submit();
}
function goBackCall() {
    document.EditBookingsForm.buttonValue.value = "goBack";
    jQuery("#cancel").attr("disabled", true);
    document.EditBookingsForm.submit();
}
function getIssTerm() {
    var issuingTerm = document.EditBookingsForm.issuingTerminal.value;
    var index = issuingTerm.indexOf("-");
    var newIssuingTerm = issuingTerm.substring(0, index);
    var issuTerm = newIssuingTerm.replace("&", "amp;");
    if (issuTerm === "%") {
        issuTerm = "percent";
    }
    GB_show("Issuing Termial Info", "/logisoft/issuingTerminal.do?buttonValue=Quotation&issuingTerminal=" + issuTerm, width = "400", height = "700");
}
function getIssuingTerminal(val1) {
    document.EditBookingsForm.issuingTerminal.value = val1;
}
function getRoutedByAgent() {
    var customerName = document.EditBookingsForm.routedByAgent.value;
    if (customerName === "%") {
        customerName = "percent";
    }
    GB_show("Routed By Agent", "/logisoft/quoteCustomer.do?buttonValue=RoutedBooking&clientName=" + customerName, width = "400", height = "810");
}
function getRoutedByAgentFromPopup(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14) {
    val1 = val1.replace(":", "'");
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function (data) {
            if (data !== "") {
                alertNew(data);
                document.EditBookingsForm.routedByAgent.value = "";
            } else {
                document.EditBookingsForm.routedByAgent.value = val1;
            }
        }
    });
}
function getAgentforDestination(ev, importFlag) {
    if (event.keyCode === 9 || event.keyCode === 13) {
        var pod = document.EditBookingsForm.portOfDischarge.value;
        var index = pod.indexOf("/");
        var podNew = pod.substring(0, index);
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                methodName: "getDefaultAgent",
                param1: podNew,
                param2: "true" === importFlag ? "I" : "F",
                dataType: "json"
            },
            success: function (data) {
                populateAgentDojo1(data);
            }
        });
    }
}
function populateAgentDojo1(data) {
    if (data) {
        if (data.accountName !== undefined) {
            document.getElementById("agent").value = data.accountName;
        }
        if (data.accountno !== undefined) {
            document.getElementById("agentNo").value = data.accountno;
        }
    }
}
function getAgentforPod(ev, importFlag) {
    if (document.getElementById("agent").value === "") {
        if (event.keyCode === 9 || event.keyCode === 13) {
            if (document.EditBookingsForm.destination) {
                var pod = document.EditBookingsForm.destination.value;
                var index = pod.indexOf("/");
                var podNew = pod.substring(0, index);
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                        methodName: "getDefaultAgent",
                        param1: podNew,
                        param2: "true" === importFlag ? "I" : "F",
                        dataType: "json"
                    },
                    success: function (data) {
                        populateAgentDojo2(data);
                    }
                });
            }
        }
    }
}
function populateAgentDojo2(data) {
    if (data) {
        if (data.accountName !== undefined) {
            document.getElementById("agent").value = data.accountName;
        }
        if (data.accountno !== undefined) {
            document.getElementById("agentNo").value = data.accountno;
        }
    }
}
function enableAgentLookUp() {
    if (document.getElementById("alternateAgentY").checked) {
    //        document.getElementById("agentlookup").style.visibility = "hidden";
    } else {
//        document.getElementById("agentlookup").style.visibility = "visible";
}
}
function fillDefaultAgent(importFlag) {
     document.getElementById("agent").className = "textlabelsBoldForTextBox";
     document.getElementById("routedAgentCheck").className = "dropdown_accounting";
     document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
    jQuery("#directConsignmentN").attr("checked",true);
    jQuery("#directConsignmentY").attr("checked", false);
    jQuery("#agent").attr("disabled", false);
    jQuery("#routedAgentCheck").attr("disabled", false);
    jQuery("#routedByAgent").attr("disabled", false);
    var destination = document.getElementById("portOfDischarge").value;
    if (destination.lastIndexOf("(") > -1 && destination.lastIndexOf(")") > -1) {
        var destiNew = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                methodName: "getDefaultAgent",
                param1: destiNew,
                param2: "true" === importFlag ? "I" : "F",
                dataType: "json"
            },
            success: function (data) {
                if (null !== data && jQuery.type(data.accountno) !== "undefined" && jQuery.trim(data.accountno) !== "") {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                            methodName: "checkForDisable",
                            param1: data.accountno
                        },
                        success: function (dataNew) {
                            if (jQuery.trim(dataNew) !== "") {
                                alertNew(dataNew);
                                document.getElementById("agent").value = "";
                                document.getElementById("agentNo").value = "";
                            } else {
                                document.getElementById("agent").value = data.accountName;
                                document.getElementById("agent_check").value = data.accountName;
                                document.getElementById("agentNo").value = data.accountno;
                            }
                        }
                    });
                    document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
                    document.getElementById("agent").readOnly = true;
                    document.getElementById("agent").tabIndex = -1;
                    if (document.getElementById("routedAgentCheck").value === "yes" || document.getElementById("routedAgentCheck").value === "no") {
                        document.getElementById("routedByAgent").value = "";
                        document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
                        document.getElementById("routedAgentCheck").value = "";
                    }
                }
            }
        });
    }
}
function directConsignmnt() {
    jQuery("#agent").addClass("textlabelsBoldForTextBoxDisabledLook");
    jQuery("#agent").addClass("readonly",true);
    jQuery("#routedAgentCheck").addClass("disabled",true);
    jQuery("#routedByAgent").addClass("readonly",true);
    jQuery("#routedAgentCheck").addClass("dropdown_accountingDisabled");
    jQuery("#routedByAgent").addClass("textlabelsBoldForTextBoxDisabledLook");
    jQuery("#alternateAgentN").attr("checked", true);
    jQuery("#routedAgentCheck").val("");
    jQuery("#routedByAgent").val("");
    jQuery("#agent").val("");
    jQuery("#agentNo").val("");
    jQuery("#agent").attr("disabled", true);
    jQuery("#routedAgentCheck").attr("disabled", true);
    jQuery("#routedByAgent").attr("disabled", true);
}

function directConsignmntNo(importFlag){
    var flag = importFlag === "true" ? "true" : "false";
    jQuery("#alternateAgentY").attr("checked", true);
    jQuery("#directConsignmentN").attr("checked", true);
     document.getElementById("agent").className = "textlabelsBoldForTextBox";
     document.getElementById("routedAgentCheck").className = "dropdown_accounting";
     document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
    fillDefaultAgent(flag);

}
function clearValues() {
    document.getElementById("agent").value = "";
    document.getElementById("agentNo").value = "";
    document.getElementById("routedByAgent").value = "";
    document.getElementById("agent").readOnly = false;
    document.getElementById("agent").tabIndex = 0;
    document.getElementById("agent").className = "textlabelsBoldForTextBox";
    document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
    document.getElementById("routedAgentCheck").className = "dropdown_accounting";
    document.getElementById("routedByAgent").readOnly = false;
    document.getElementById("routedByAgent").tabIndex = 0;
    if (document.EditBookingsForm.routedAgentCheck.value === "yes" || document.EditBookingsForm.routedAgentCheck.value === "no") {
        document.getElementById("routedByAgent").value = "";
        document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
        document.EditBookingsForm.routedAgentCheck.value = "";
    }
    var code = "";
    var dest = document.getElementById("portOfDischarge").value;
    if (dest.lastIndexOf("(") !== -1) {
        code = dest.substring(dest.lastIndexOf("(") + 1, dest.lastIndexOf(")"));
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "getDestCodeforHBL",
            param1: code
        },
        async: false,
        success: function (data) {
            if (data !== null && data !== '' && data === 'Y' && document.EditBookingsForm.prepaidToCollect[1].checked === true) {
                document.EditBookingsForm.prepaidToCollect[1].checked = false;
                document.EditBookingsForm.prepaidToCollect[0].checked = true;
                document.EditBookingsForm.billToCode[0].checked = true;
                document.EditBookingsForm.billToCode[0].disabled = false;
                document.EditBookingsForm.billToCode[1].disabled = false;
                document.EditBookingsForm.billToCode[2].disabled = false;
                document.EditBookingsForm.billToCode[3].disabled = true;
                document.EditBookingsForm.billToCode[3].checked = false;
            }
        }
    });
}
var _boolean = true;
function getExpandRates() {
    if (_boolean === true) {
        document.getElementById("expandRates").style.display = "block";
        document.getElementById("collapseRates").style.display = "none";
        _boolean = false;
    } else {
        document.getElementById("collapseRates").style.display = "block";
        document.getElementById("expandRates").style.display = "none";
        _boolean = true;
    }
}
function getWareHouseForEquipPick(ev) {
    GB_show("Equipment Pickup", "/logisoft/wareHouselookUp.do?buttonValue=BookingPositionLoc&wareHouse=" + document.getElementById("exportPositioning").value, width = "400", height = "700");
}
function getWareHouseDetailsForPos(ev1, ev2, ev3, ev4, ev5) {
    document.getElementById("exportPositioning").value = ev1;
    document.getElementById("emptypickupaddress").value = ev1 + "\n" + ev2 + "\n" + ev3 + "," + ev5 + "," + ev4;
    document.getElementById("addressofDelivery").value = ev1 + "\n" + ev2 + "\n" + ev3 + "," + ev5 + "," + ev4;
    document.getElementById("autoAddressFill").checked = "true";
}
function getWareHouseForLoad() {
    GB_show("Equipment Pickup", "/logisoft/wareHouselookUp.do?buttonValue=BookingLoadLoc&wareHouse=" + document.getElementById("positionlocation").value, width = "400", height = "700");
}
function getWareHouseDetailsForLoad(ev1, ev2) {
    document.getElementById("positionlocation").value = ev1;
    document.getElementById("addressofExpPosition").value = ev2;
}
function getWarehouseForReturn() {
    GB_show("Equipment Pickup", "/logisoft/wareHouselookUp.do?buttonValue=BookingEquipReturn&wareHouse=" + document.getElementById("loadLocation").value, width = "400", height = "700");
}
function getWareHouseDetailsForEquipReturn(ev1, ev2) {
    document.getElementById("loadLocation").value = ev1;
    document.getElementById("addressofDelivery").value = ev2;
}
function setZipCode(ev) {
    document.EditBookingsForm.zip.value = ev;
}
function displayRampCity() {
    var selectBoxValue = document.EditBookingsForm.moveType.selectedIndex;
    GB_show("RampCity Search", "/logisoft/searchquotation.do?buttonValue=searchPort&textName=rampCity&from=terminal&typeOfmove=" + selectBoxValue, width = "250", height = "600");
}
function disableThirdParty() {
    if (document.EditBookingsForm.billToCode[0].checked) {
        if (document.EditBookingsForm.fowardername.value.trim() === "NO FF ASSIGNED"
            || document.EditBookingsForm.fowardername.value.trim() === 'NO FF ASSIGNED / B/L PROVIDED'
            || document.EditBookingsForm.fowardername.value.trim() === "NO FRT. FORWARDER ASSIGNED") {
            alertNew("Please change Bill To Party or change Forwarder, because Forwarder is NO FF ASSIGNED");
            document.EditBookingsForm.billToCode[1].checked = true;
            return;
        }
    }
    if (document.EditBookingsForm.accountName.value !== "") {
        if (document.EditBookingsForm.billToCode[0].checked) {
            confirmNew("Do you want to change from Thirdparty to Forwarder", "Forwarder");
        }
        if (document.EditBookingsForm.billToCode[1].checked) {
            confirmNew("Do you want to change from Thirdparty to Shipper", "Shipper");
        }
        if (document.EditBookingsForm.billToCode[3].checked) {
            confirmNew("Do you want to change from Thirdparty to Agent", "Agent");
        }
    } else {
        document.getElementById("accountName").disabled = true;
        document.getElementById("accountNumber1").disabled = true;
        document.getElementById("toggle").style.visibility = "hidden";
    }
}
function enableThirdParty() {
    if (document.EditBookingsForm.billToCode[2].checked) {
        document.getElementById("accountName").disabled = false;
        document.getElementById("accountNumber1").disabled = false;
        document.getElementById("toggle").style.visibility = "visible";
    }
}
function setVesselName(val) {
    document.EditBookingsForm.vessel.value = val;
}
function getFFCommission() {
    var forwarder = document.getElementById("fowardername").value;
    if (forwarder === "" || forwarder.trim() === "NO FF ASSIGNED" || forwarder.trim() === 'NO FF ASSIGNED / B/L PROVIDED'
        || forwarder.trim() === 'NO FRT. FORWARDER ASSIGNED') {
        if (forwarder === "") {
            alertNew("Please select the Forwarder");
        } else {
            alertNew("You cannot Auto Deduct FF Commission because Forwarder is " + forwarder);
        }
        document.EditBookingsForm.deductFFcomm[1].checked = true;
    } else {
        if (document.EditBookingsForm.collapseid.value === "") {
            getNumbersChanged();
        }
        document.EditBookingsForm.bundleOfr.value = bundleCheck;
        document.EditBookingsForm.collapseid.value = collapseid;
        confirmNew("Commission Amount will be subtracted. Are you sure?", "getFFCommission");
    }
}
function deleteFFCommission() {
    if (document.EditBookingsForm.collapseid.value === "") {
        getNumbersChanged();
    }
    document.EditBookingsForm.bundleOfr.value = bundleCheck;
    document.EditBookingsForm.collapseid.value = collapseid;
    confirmNew("Commission Amount will be deleted. Are you sure?", "deleteFFCommission");
}
function addLocalDrayage(ev) {
    if (event.keyCode === 9 || event.keyCode === 13) {
        document.EditBookingsForm.buttonValue.value = "addLocalDrayage";
        document.EditBookingsForm.submit();
    }
}
function addInterModal(ev) {
    if (event.keyCode === 9 || event.keyCode === 13) {
        document.EditBookingsForm.buttonValue.value = "addInterModal";
        document.EditBookingsForm.submit();
    }
}
function deleteCharges(val1) {
    document.EditBookingsForm.buttonValue.value = "deleteCharge";
    document.EditBookingsForm.numbIdx.value = val1;
    confirmNew("Are you sure? You want to delete this Charge", "deleteCharge");
}
function changeToPerBl(charge, bookingNumber, id) {
    document.EditBookingsForm.buttonValue.value = "changeToPerBl";
    document.EditBookingsForm.numbIdx.value = id;
    document.EditBookingsForm.chargeCode.value = charge;
    confirmNew("Are you sure? You want to change this cost type from Per Container to Per BL Charges", "changeToPerBl");
}
function deleteCharges1(val1) {
    document.EditBookingsForm.buttonValue.value = "deleteBLCharge";
    document.EditBookingsForm.numbIdx.value = val1;
    confirmNew("Are you sure? You want to delete this Charge", "deleteBlCharge");
}
function popupAddRates(windowname, val1, val2, val3) {
    if (!window.focus) {
        return true;
    }
    var portDestionation = document.EditBookingsForm.portOfDischarge.value;
    var pol = document.EditBookingsForm.portOfOrigin.value;
    var pod = document.EditBookingsForm.destination.value;
    var haz = "";
    if (document.EditBookingsForm.hazmat[0].checked) {
        haz = "Y";
    } else {
        haz = "N";
    }
    if (document.EditBookingsForm.portOfDischarge.value === "" || document.EditBookingsForm.commcode.value === "") {
        alertNew("PLEASE SELECT  DESTIONATION PORT, COMMODITY CODE");
        document.EditBookingsForm.portofDischarge.focus();
        return;
    }
    GB_show("FCL Rates Comparison Grid", "/logisoft/fclQuotes.do?buttonValue=getRatesBooking&origin=" + document.EditBookingsForm.originTerminal.value + "&pol=" + pol + "&pod=" + pod + "&destn=" + portDestionation + "&comid=" + document.EditBookingsForm.commcode.value + "&carrier=" + document.EditBookingsForm.sslDescription.value + "&quoteDate=" + val1 + "&bookingDate=" + val2 + "&fileNo=" + val3 + "&hazmat=" + haz, 500, 850);
}
function selectedMenu(val1, val2, val3, val4, val5, val6, val7) {
    document.EditBookingsForm.selectedCheck.value = val3;
    document.EditBookingsForm.unitselected.value = val2;
    document.EditBookingsForm.sslDescription.value = val1;
    document.EditBookingsForm.selectedOrigin.value = val4;
    document.EditBookingsForm.selectedDestination.value = val5;
    document.EditBookingsForm.selectedComCode.value = val6;
    if (val7 === "newgetRatesBKG" || val7 === "oldgetRatesBKG") {
        document.getElementById("bkgAlert").value = val7;
        getConverttoBook(val7, val3);
    } else {
        document.EditBookingsForm.buttonValue.value = "newgetRates";
        document.EditBookingsForm.submit();
    }
}
function setTabName(tabName) {
    if (document.getElementById("bookingCompleteY") !== null && document.getElementById("bookingCompleteY") !== undefined && document.getElementById("bookingCompleteY").checked) {
        document.getElementById("y8").disabled = true;
        document.getElementById("n8").disabled = true;
    }
    document.getElementById("selectedTab").value = tabName;
}
function sendEmail(val1, val2) {
    if (!document.EditBookingsForm.bookingComplete[0].checked) {
        alertNew("Please Complete Booking");
        return;
    }
    var emailTo = "";
    if (document.getElementById("shippercheck").checked) {
        emailTo = document.getElementById("shipperEmail").value;
    } else {
        if (document.getElementById("forwardercheck").checked) {
            emailTo = document.getElementById("forwarderEmail").value;
        } else {
            emailTo = document.getElementById("consigneeEmail").value;
        }
    }
    GB_show("Email", "/logisoft/sendEmail.do?id=" + val1 + "&moduleName=Booking&toAddress=" + emailTo + "&subject=FCL Booking&ssBookingNo=" + val2, 455, 650);
}
function getRemark(ev) {
    if (event.keyCode === 13 || event.keyCode === 9 || event.keyCode === 0) {
        var ind = ev.indexOf(":");
        if (ind !== -1) {
            var newCode = ev.substring(0, ind);
            var newDesc = ev.substring(ind + 1, ev.length);
            document.getElementById("commentTemp").value = newCode;
            document.getElementById("commentRemark").value = newDesc;
        }
    }
}
function goToRemarksLookUp(importFlag) {
    GB_show("Pre-defined Remarks", "/logisoft/remarksLookUp.do?buttonValue=Quotation&importFlag=" + importFlag, width = "400", height = "700");
}
function getAllRemarksFromPopUp(val) {
    var commentVal = document.EditBookingsForm.remarks.value;
    var totalLength = commentVal.length + val.length;
    if (totalLength > 500) {
        alertNew("More than 500 characters are not allowed");
        return;
    }
    var oldarray = document.EditBookingsForm.remarks.value;
    var splittedArray;
    if (oldarray.length === 0) {
        splittedArray = oldarray;
    } else {
        splittedArray = oldarray.split("\n");
    }
    var newarray = val.split(">>");
    var resultarray = new Array();
    var flag = false;
    for (var k = 0; k < newarray.length; k++) {
        flag = false;
        for (var l = 0; l < splittedArray.length; l++) {
            if (newarray[k].replace(/^[\s]+/, "").replace(/[\s]+$/, "").replace(/[\s]{2,}/, " ") === splittedArray[l].replace(/^[\s]+/, "").replace(/[\s]+$/, "").replace(/[\s]{2,}/, " ")) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            if (oldarray === "") {
                oldarray = newarray[k];
            } else {
                oldarray += "\n" + newarray[k];
            }
        }
    }
    document.EditBookingsForm.remarks.value = oldarray.replace(/>/g, "");
}
var missingFields = '';
var i = 1;
function setBookingComplete(importFlag) {
    missingFields = '';
    i = 1;
    if (document.EditBookingsForm.bookingId.value === '') {
        alertNew('Please Save Booking, Before  Completing the Booking ');
        document.EditBookingsForm.bookingComplete[1].checked = true;
        return;
    }
    if ((document.EditBookingsForm.prepaidToCollect[0].checked === false) && (document.EditBookingsForm.prepaidToCollect[1].checked === false)) {
        missingFields = missingFields + i + "-->" + "Please select Prepaid/Collect to complete Booking <br>";
        i = i + 1;
    }
    if (document.EditBookingsForm.prepaidToCollect[0].checked && !document.EditBookingsForm.billToCode[0].checked && !document.EditBookingsForm.billToCode[1].checked && !document.EditBookingsForm.billToCode[2].checked) {
        missingFields = missingFields + i + "-->" + "Please select Bill To Code to complete Booking <br>";
        i = i + 1;
    }
    if (document.EditBookingsForm.billToCode[2].checked && document.getElementById("accountName").value === "") {
        missingFields = missingFields + i + "-->" + "Please Enter ThirdPartyName To Complete Booking <br>";
        i = i + 1;
    }
    if (document.EditBookingsForm.SSBooking.value === "") {
        missingFields = missingFields + i + "-->" + "Please enter SS Bkg# to complete Booking <br>";
        jQuery("#SSBooking").css("border-color", "red");
        i = i + 1;
    }
    if (importFlag === "false" && document.EditBookingsForm.vessel.value === "") {
        missingFields = missingFields + i + "-->" + "Please enter Vessel to complete Booking <br>";
        jQuery("#vessel").css("border-color", "red");
        i = i + 1;
    } else if (importFlag === "true" && document.getElementById("vessel_check").checked === true && jQuery("#vesselname_checkn").val() === "") {
        missingFields = missingFields + i + "-->" + "Please enter Vessel to complete Booking <br>";
        jQuery("#vessel").css("border-color", "red");
        i = i + 1;
    } else if (importFlag === "true" && document.getElementById("vessel_check").checked === false && jQuery("#vessel").val() === "") {
        missingFields = missingFields + i + "-->" + "Please enter Vessel to complete Booking <br>";
        jQuery("#vessel").css("border-color", "red");
        i = i + 1;
    }
    if (document.EditBookingsForm.voyage.value === "") {
        missingFields = missingFields + i + "-->" + "Please enter Voyage to complete Booking <br>";
        jQuery("#ssVoy").css("border-color", "red");
        i = i + 1;
    }
    if (document.EditBookingsForm.estimatedDate.value === "") {
        missingFields = missingFields + i + "-->" + "Please enter ETD to complete Booking <br>";
        jQuery("#txtcal22").css("border-color", "red");
        i = i + 1;
    }
    if (document.EditBookingsForm.estimatedAtten.value === "") {
        missingFields = missingFields + i + "-->" + "Please enter ETA to complete Booking <br>";
        jQuery("#txtcal5").css("border-color", "red");
        i = i + 1;
    }
    if (document.EditBookingsForm.portCutOff.value === "") {
        missingFields = missingFields + i + "-->" + "Please enter ContainerCutoff Date to complete Booking <br>";
        jQuery("#txtcal313").css("border-color", "red");
        i = i + 1;
    }
    if (document.EditBookingsForm.docCutOff.value === "") {
        missingFields = missingFields + i + "-->" + "Please enter DocCutOff Date to complete Booking <br>";
        jQuery("#txtcal71").css("border-color", "red");
        i = i + 1;
    }
    if (document.EditBookingsForm.issuingTerminal.value === '') {
        missingFields = missingFields + i + "-->Please Enter Issuing Terminal<br>";
        jQuery("#issuingTerminal").css("border-color", "red");
        i = i + 1;
    }
    if (document.EditBookingsForm.vaoyageInternational.value === '' && document.EditBookingsForm.fileType.value === 'C') {
        missingFields = missingFields + i + "-->Please Select Voy Internal(CFCL)<br>";
        jQuery("#issuingTerminal").css("border-color", "red");
        i = i + 1;
    }

    var destination = document.getElementById("portOfDischarge").value;
    var destUnlocCode=(destination.substring(destination.lastIndexOf("(")+1, destination.lastIndexOf(")")));
    var agentNo=jQuery('#agentNo').val();
    var directConsignment =document.querySelector('input[name="directConsignmntCheck"]:checked').value;
    if((agentNo==='' || agentNo==null) && (directConsignment!=='on')){
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "getDestCode",
                param1: destUnlocCode
            },
            async: false,
            success: function (data) {
                if (data === "X") {
                    missingFields = missingFields + i + "-->" +"Default Agent Must Enter To Complete Booking<br>";
                    i = i + 1;
                }
            }

        });
    }

    if (document.EditBookingsForm.lineMove.selectedIndex === 0) {
        missingFields = missingFields + i + "-->" + "Please select LineMove in TradeRoute tab to complete Booking <br>";
        jQuery("#lineMoveDiv").css("border", " 1px solid red");
        i = i + 1;
    }
    if(document.getElementById("directConsignmentN").checked == true){
        if (document.getElementById("routedAgentCheck").value === '' && document.EditBookingsForm.ratesNonRates.value !== "N") {
            missingFields = missingFields + i + "-->" + "Please select ERT in TradeRoute tab to complete Booking <br>";
            jQuery("#routedAgentCheck").css("border-color", "red");
            i = i + 1;
        }
    }
    if (document.EditBookingsForm.fowardername.value === "" || document.EditBookingsForm.forwarder.value === "") {
        missingFields = missingFields + i + "-->" + "Please enter at least Forwarder Details in ShipperForwarderConsigneeTrucker tab to complete Booking <br>";
        jQuery("#fowardername").css("border-color", "red");
        i = i + 1;
    }
    if (document.EditBookingsForm.billToCode[0].checked && (document.EditBookingsForm.fowardername.value.trim() === "NO FF ASSIGNED" ||
        document.EditBookingsForm.fowardername.value.trim() === "NO FF ASSIGNED / B/L PROVIDED" ||
        document.EditBookingsForm.fowardername.value.trim() === "NO FRT. FORWARDER ASSIGNED")) {
        missingFields = missingFields + i + "-->" + "Please change Bill To Party or change Forwarder, because Forwarder is NO FF ASSIGNED <br>";
        i = i + 1;
    }
    if (document.EditBookingsForm.billToCode[1].checked && document.getElementById("shipperName").value === "") {
        missingFields = missingFields + i + "-->" + "Please Enter Shipper To Complete Booking <br>";
        jQuery("#shipperName").css("border-color", "red");
        i = i + 1;
    }
    if (document.EditBookingsForm.hiddennumbers !== undefined) {
        for (var l = 0; l < document.EditBookingsForm.hiddennumbers.length; l++) {
            if (document.EditBookingsForm.hiddennumbers[l].value === "") {
                missingFields = missingFields + i + "-->" + "Quantity Cannot be Zero or empty <br>";
                i = i + 1;
            }
        }
    }
    if (jQuery("#spotRateY").is(":checked") && isSpotRated === "No Spot Rate") {
        missingFields = missingFields + i + "-->" + "On Spot Rate Files Spot Costs MUST be entered Manually <br>";
        i = i + 1;
    }
    var val = false;
    if (document.EditBookingsForm.collapseCheck !== undefined) {
        if (document.EditBookingsForm.collapseCheck.length === undefined) {
            if (document.EditBookingsForm.collapseCheck.checked) {
                val = true;
            }
        } else {
            for (var k = 0; k < document.EditBookingsForm.collapseCheck.length; k++) {
                if (document.EditBookingsForm.collapseCheck[k].checked) {
                    val = true;
                    break;
                }
            }
        }
    }
    if (!val) {
        if (document.EditBookingsForm.breakBulk) {
            if (document.EditBookingsForm.breakBulk[1].checked) {
                missingFields = missingFields + i + "-->" + "Please select at least one Container in Cost & Charges tab to complete Booking <br>";
                i = i + 1;
            }
        } else {
            missingFields = missingFields + i + "-->" + "Please select at least one Container in Cost & Charges tab to complete Booking <br>";
            i = i + 1;
        }
    }
    if (document.EditBookingsForm.portOfDischarge.value !== "") {
        var str = document.EditBookingsForm.portOfDischarge.value;
        var temp = new Array();
        temp = str.split("/");
        var code = temp[1];
        var desc = temp[0];
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "checkForNonDomesticPort",
                param1: code,
                param2: desc
            },
            async: false,
            success: function (data) {
                nonDomesticResult(data);
            }
        });
    }
}
function nonDomesticResult(data) {
    var outYard = document.EditBookingsForm.dateOutYard.value;
    var terminal = document.EditBookingsForm.issuingTerminal.value;
    var temp = new Array();
    temp = terminal.split("-");
    var result = temp[1];
    if (data !== "") {
        if (outYard === "" && data === "K" && (result === "01" || result === "73")) {
            missingFields = missingFields + i + "-->" + "Date Out Of Yard is required to Complete Booking <br>";
            i = i + 1;
        }
    }
    if (document.EditBookingsForm.bookingComplete[0].checked && document.getElementById("ReversetoQuote")) {
        document.getElementById("ReversetoQuote").style.visibility = "hidden";
        document.getElementById("ReversetoQuote1").style.visibility = "hidden";
    } else if (document.getElementById("ReversetoQuote")) {
        document.getElementById("ReversetoQuote").style.visibility = "visible";
        document.getElementById("ReversetoQuote1").style.visibility = "visible";
    }
    document.getElementById("y8").disabled = true;
    document.getElementById("n8").disabled = true;

    //checking for agent field----------------
    if ('' !== missingFields) {
        alertNew(missingFields);
        document.EditBookingsForm.bookingComplete[1].checked = true;
        return;
    } else {
        if (document.getElementById("emptypickupaddress").value.length >= 200) {
            alertNew("Enter less than 200 character in Equipment Pickup Name/Address");
            document.EditBookingsForm.bookingComplete[1].checked = true;
            return;
        }
        if (document.getElementById("pickUpRemarks").value.length > 100) {
            alertNew("More than 100 characters are not allowed in Equipment Pickup Remarks");
            document.EditBookingsForm.bookingComplete[1].checked = true;
            return;
        }
        if (document.getElementById("addressofDelivery").value.length >= 200) {
            alertNew("Enter less than 200 character in Equipment Return Name/Address");
            document.EditBookingsForm.bookingComplete[1].checked = true;
            return;
        }
        if (document.getElementById("returnRemarks").value.length > 100) {
            alertNew("More than 100 characters are not allowed in Equipment Return Remarks");
            document.EditBookingsForm.bookingComplete[1].checked = true;
            return;
        }
        if (document.getElementById("loadRemarks").value.length > 100) {
            alertNew("More than 100 characters are not allowed in Spotting Address Remarks");
            document.EditBookingsForm.bookingComplete[1].checked = true;
            return;
        }
        if (document.getElementById("agent").value === "" && document.EditBookingsForm.ratesNonRates.value !== "N") {
            document.getElementById("bookingCompleteY").disabled = true;
            confirmNew("WARNING: An agent has not been selected on this file.Continue to complete Booking ?", "allowBookingToComplete");
        } else {
            document.getElementById("bookingCompleteY").disabled = true;
            call();
            SAVE();
        //--make the page readonly when booking complete------
        //makePageReadOnlyOnBookingComplete(document.getElementById("editbook"),'');
        }
    }
}
function makePageReadOnlyOnBookingComplete(form, val, view) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if ((element.type === "text" || element.type === "textarea")) {
            if (element.id === "override-remarks") {
            } else if (element.id === "vessel" || element.id === "ssVoy") {
                element.style.borderLeft = "red 2px solid";
                element.className = "textlabelsBoldForTextBox";
            } else {
                element.style.backgroundColor = "#CCEBFF";
                element.style.border = 0;
                element.readOnly = true;
                if (element.id === "SSBooking" || element.id === "txtcal313" || element.id === "txtcal71" || element.id === "txtcal22"
                    || element.id === "txtcal5" || element.id === "fowardername" || element.id === "portOfDischarge" || element.id === "originTerminal" || element.id === "issuingTerminal") {
                    element.style.borderLeft = "red 2px solid";
                }
                element.tabIndex = -1;
                element.onFocus = element.blur();
                element.className = "textlabelsBoldForTextBox";
            }
        }
        if (element.type === "checkbox" || element.type === "radio") {
            if (element.type === "radio" && (element.id === "bookingCompleteY" || element.id === "bookingCompleteN"
                || element.id === "docsReceivedY" || element.id === "docsReceivedN" || element.id === "rampCheck")) {
                element.disabled = false;
            } else {
                element.disabled = true;
            }
        }
        //this condition is for viewmode to disable all radios including booking complete.
        if (element.type === "radio" && (val === "3" || view === "on")) {
            element.disabled = true;
        }
        if (element.type === "select-one") {
            element.style.border = 0;
            element.disabled = true;
        }
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            if (imgs[k].id === 'viewgif' || imgs[k].id === 'viewgif1' || imgs[k].id === 'viewgif2'
                || imgs[k].id === 'collpaseicon' || imgs[k].id === 'triangleicon') {
                imgs[k].style.visibility = "visible";
            } else {
                imgs[k].style.visibility = "hidden";
            }
        }
    }
    // don't hide Remarks image on view mode
    jQuery(".adjustmentRemarks").each(function () {
        jQuery(this).css("visibility", "visible");
    });
    jQuery(".remark").each(function () {
        jQuery(this).css("visibility", "visible");
    });
}
function setBookingCompleteN(cancelEdi, createOrChange, checkDefaultAgent, checkDirectConsign, importFlag, companyCode) {
    if (document.EditBookingsForm.bookingComplete[0].checked && document.getElementById("ReversetoQuote")) {
        document.getElementById("ReversetoQuote").style.visibility = "hidden";
        document.getElementById("ReversetoQuote1").style.visibility = "hidden";
    } else if (document.getElementById("ReversetoQuote")) {
        document.getElementById("ReversetoQuote").style.visibility = "visible";
        document.getElementById("ReversetoQuote1").style.visibility = "visible";
    }
    showCancelEdiOnload(cancelEdi, createOrChange);
    document.getElementById("y8").disabled = false;
    document.getElementById("n8").disabled = false;
    document.getElementById("addBookingAccrual").style.display = "block";
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "getFclBookingAccruals",
            param1: document.EditBookingsForm.bookingId.value
        },
        success: function (data) {
            if (data !== "") {
                document.getElementById("previewBookingAccruals").style.display = "block";
            }
        }
    });
    document.getElementById("inputRatesManually").style.display = "block";
    //---making the Page editable-----
    revertFieldToEditModeCompleteBookingN(document.getElementById("editbook"));
    greyOutTruckerCheckBox(document.getElementById('editbook'));
    getBillTO();
    directmentOnload(checkDefaultAgent, checkDirectConsign);
    setAlternateAgent(importFlag, companyCode);
    if(jQuery("#vaoyageInternational").val() !== "") {
    disableTextBox("vaoyageInternational");
    }
}

function directmentOnload(checkDefaultAgent, checkDirectConsign){

//    var checkDefault = '${checkDefaultAgent}';
//            var checkDirect = '${checkDirectConsign}';

             if ((checkDefaultAgent != null && checkDefaultAgent != undefined) && (checkDirectConsign != null && checkDirectConsign != undefined)) {
                        if (checkDefaultAgent == 'Y' && checkDirectConsign == 'off') {
                        document.getElementById("alternateAgentY").checked = true;
                        document.getElementById("directConsignmentN").checked = true;
                        document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
                        document.getElementById("agent").readOnly = true;
                        document.getElementById("routedAgentCheck").className = "dropdown_accounting";
                        document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
                    } else if (checkDefaultAgent == 'N' && checkDirectConsign == 'on') {
                        document.getElementById("alternateAgentN").checked = true;
                        document.getElementById("directConsignmentY").checked = true;
                        document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
                        document.getElementById("agent").readOnly = true;
                        document.getElementById("routedAgentCheck").className = "dropdown_accountingDisabled";
                        document.getElementById("routedByAgent").className = "textlabelsBoldForTextBoxDisabledLook";
                        //directConsignmnt();
                    } else  if(checkDefaultAgent == 'N' && checkDirectConsign == 'off') {
                        document.getElementById("alternateAgentN").checked = true;
                        document.getElementById("directConsignmentN").checked = true;
                        document.getElementById("agent").className = "textlabelsBoldForTextBox";
                        document.getElementById("agent").readOnly = true;
                        document.getElementById("routedAgentCheck").className = "dropdown_accounting";
                        document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
                    }
             }
}

function revertFieldToEditModeCompleteBookingN(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type === "text" || element.type === "textarea") {
            if (element.type === "text" && (element.id === "portOfDischarge" || element.id === "originTerminal" || element.id === "commcode"
                || element.id === "noOfDays" || element.id === "doorOrigin" || element.id === "wareHouseTemp" || element.id === "equipmentReturnTemp"
                || element.id === "zip" || element.id === "costofgoods" || element.id === "sslDescription" || element.id === "agentNo"
                || element.id === "sellRate" || element.id === "chargeAmount"
                || element.id === "shipper" || element.id === "forwarder" || element.id === "consignee")) {
                element.style.backgroundColor = "#CCEBFF";
                element.readOnly = true;
                element.style.border = 0;
                if (element.id === "SSBooking" || element.id === "vessel" || element.id === "ssVoy" || element.id === "txtcal313" || element.id === "txtcal71" || element.id === "txtcal22"
                    || element.id === "txtcal5" || element.id === "fowardername" || element.id === "portOfDischarge" || element.id === "originTerminal" || element.id === "issuingTerminal") {
                    element.style.borderLeft = "red 2px solid";
                }
                element.tabIndex = -1;
                element.className = "textlabelsBoldForTextBox";
            } else {
                element.style.border = "1px solid #C4C5C4";
                element.style.backgroundColor = "#FCFCFC";
                element.readOnly = false;
                if (element.id === "SSBooking" || element.id === "vessel" || element.id === "ssVoy" || element.id === "txtcal313" || element.id === "txtcal71" || element.id === "txtcal22"
                    || element.id === "txtcal5" || element.id === "fowardername" || element.id === "portOfDischarge" || element.id === "originTerminal" || element.id === "issuingTerminal") {
                    element.style.borderLeft = "red 2px solid";
                }
                element.tabIndex = 0;
            }
        }
        if (element.type === "checkbox" || element.type === "radio") {
            if (element.id === "breakBulkN" || element.id === "breakBulkY") {
                element.disabled = true;
            } else {
                element.disabled = false;
            }
        // element.style.border='1px solid #C4C5C4';
        }
        if (element.type === "select-one") {
            element.disabled = false;
        }
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            imgs[k].style.visibility = "visible";
        }
    }
    if (document.getElementById("expandRatesTable") !== null && document.getElementById("expandRatesTable") !== undefined) {
        var length = document.getElementById("expandRatesTable").rows.length;
        for (var i = 0; i < length - 1; i++) {
            if (document.getElementById("accountname" + i) !== null && document.getElementById("accountno" + i)) {
                document.getElementById("accountname" + i).style.backgroundColor = "#CCEBFF";
                document.getElementById("accountname" + i).readOnly = true;
                document.getElementById("accountname" + i).tabIndex = -1;
                document.getElementById("accountname" + i).style.border = 0;
                document.getElementById("accountname" + i).className = "textlabelsBoldForTextBox";
                document.getElementById("accountno" + i).style.backgroundColor = "#CCEBFF";
                document.getElementById("accountno" + i).readOnly = true;
                document.getElementById("accountno" + i).tabIndex = -1;
                document.getElementById("accountno" + i).style.border = 0;
                document.getElementById("accountno" + i).className = "textlabelsBoldForTextBox";
            }
        }
    }
    if (document.getElementById("collapseRatesTable") !== null && document.getElementById("collapseRatesTable") !== undefined) {
        var length = document.getElementById("collapseRatesTable").rows.length;
        for (var j = 0; j < length - 1; j++) {
            if (document.getElementById("accountname" + j) !== null && document.getElementById("accountno" + j)) {
                document.getElementById("accountname" + j).style.backgroundColor = "#CCEBFF";
                document.getElementById("accountname" + j).readOnly = true;
                document.getElementById("accountname" + j).tabIndex = -1;
                document.getElementById("accountname" + j).style.border = 0;
                document.getElementById("accountname" + j).className = "textlabelsBoldForTextBox";
                document.getElementById("accountno" + j).style.backgroundColor = "#CCEBFF";
                document.getElementById("accountno" + j).readOnly = true;
                document.getElementById("accountno" + j).tabIndex = -1;
                document.getElementById("accountno" + j).style.border = 0;
                document.getElementById("accountno" + j).className = "textlabelsBoldForTextBox";
            }
        }
    }
    if (document.getElementById("routedAgentCheck").type !== "select-one") {
        var textbox = jQuery('#routedAgentCheck');
        var selectbox = jQuery("<select></select>");
        selectbox.attr("id", textbox.attr("id"));
        selectbox.attr("name", textbox.attr("name"));
        selectbox.append("<option value=''>Select</option>");
        selectbox.append("<option value='yes'>Yes</option>");
        selectbox.append("<option value='no'>No</option>");
        selectbox.val(textbox.val());
        selectbox.width("125");
        selectbox.addClass("dropdown_accounting");
        selectbox.change(function () {
            setDefaultRouteAgent(importFalg);
        });
        jQuery("#routedAgentCheck_readonly").remove();
        textbox.replaceWith(selectbox);
    }
    if (document.getElementById("lineMove").type !== "select-one") {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "getTypeOfMove",
                dataType: "json"
            },
            success: function (data) {
                var textbox = jQuery('#lineMove');
                var selectbox = jQuery("<select></select>");
                selectbox.attr("id", textbox.attr("id"));
                selectbox.attr("name", textbox.attr("name"));
                jQuery.each(data, function (index, item) {
                    selectbox.append("<option value='" + item.value + "'>" + item.label + "</option>");
                });
                selectbox.val(textbox.val());
                selectbox.width("125");
                selectbox.addClass("dropdown_accounting");
                selectbox.change(function () {
                    greyOutTruckerCheckBox(document.getElementById("editbook"));
                });
                jQuery("#lineMove_readonly").remove();
                textbox.replaceWith(selectbox);
            }
        });
    }
    if (document.getElementById("moveType").type !== "select-one" && document.getElementById("rampCheck").value === 'on') {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getrampNvoMoveList",
                dataType: "json"
            },
            success: function (data) {
                var textbox = jQuery('#moveType');
                var selectbox = jQuery("<select></select>");
                selectbox.attr("id", textbox.attr("id"));
                selectbox.attr("name", textbox.attr("name"));
                jQuery.each(data, function (index, item) {
                    selectbox.append("<option value='" + item.value + "'>" + item.label + "</option>");
                });
                selectbox.val(textbox.val());
                selectbox.width("125");
                selectbox.addClass("dropdown_accounting");
                selectbox.change(function () {
                    greyOutTruckerCheckBox(document.getElementById("editbook"));
                });
                jQuery("#moveType_readonly").remove();
                textbox.replaceWith(selectbox);
            }
        });
    } else if (document.getElementById("moveType").type !== "select-one") {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getNvoMoveList",
                dataType: "json"
            },
            success: function (data) {
                var textbox = jQuery('#moveType');
                var selectbox = jQuery("<select></select>");
                selectbox.attr("id", textbox.attr("id"));
                selectbox.attr("name", textbox.attr("name"));
                jQuery.each(data, function (index, item) {
                    selectbox.append("<option value='" + item.value + "'>" + item.label + "</option>");
                });
                selectbox.val(textbox.val());
                selectbox.width("125");
                selectbox.addClass("dropdown_accounting");
                selectbox.change(function () {
                    greyOutTruckerCheckBox(document.getElementById("editbook"));
                });
                jQuery("#moveType_readonly").remove();
                textbox.replaceWith(selectbox);
            }
        });
    }
    if (document.getElementById("y1").checked) {
        if (document.getElementById("specialEqpmtSelectBox").type !== "select-one") {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "getUnitListForFCLTest1",
                    dataType: "json"
                },
                success: function (data) {
                    var textbox = jQuery('#specialEqpmtSelectBox');
                    var selectbox = jQuery("<select></select>");
                    selectbox.attr("id", textbox.attr("id"));
                    selectbox.attr("name", textbox.attr("name"));
                    jQuery.each(data, function (index, item) {
                        selectbox.append("<option value='" + item.value + "'>" + item.label + "</option>");
                    });
                    selectbox.val(textbox.val());
                    selectbox.width("125");
                    selectbox.addClass("dropdown_accounting");
                    selectbox.change(function () {
                        greyOutTruckerCheckBox(document.getElementById("editbook"));
                    });
                    jQuery("#specialEqpmtSelectBox_readonly").remove();
                    textbox.replaceWith(selectbox);
                }
            });
        }
    } else if (document.getElementById("specialEqpmtSelectBox_readonly")) {
        document.getElementById("specialEqpmtSelectBox_readonly").disabled = true;
        document.getElementById("specialEqpmtSelectBox_readonly").value = 'Select Special Equipments';
    }
}
function revertPageToEditMode(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type === "text" || element.type === "textarea") {
            if (element.type === "text" && (element.id === "portOfDischarge" || element.id === "originTerminal" || element.id === "commcode" || element.id === "noOfDays" || element.id === "issuingTerminal" || element.id === "doorOrigin" || element.id === "zip" || element.id === "costofgoods" || element.id === "sslDescription" || element.id === "sellRate" || element.id === "chargeAmount")) {
                element.style.backgroundColor = "#CCEBFF";
                element.readOnly = true;
                element.tabIndex = -1;
                element.style.border = 0;
                element.className = "textlabelsBoldForTextBox";
                if (element.id === "SSBooking" || element.id === "vessel" || element.id === "ssVoy" || element.id === "txtcal313" || element.id === "txtcal71" || element.id === "txtcal22"
                    || element.id === "txtcal5" || element.id === "fowardername" || element.id === "portOfDischarge" || element.id === "originTerminal" || element.id === "issuingTerminal") {
                    element.style.borderLeft = "red 2px solid";
                }
            } else {
                element.style.border = "1px solid #C4C5C4";
                element.style.backgroundColor = "#FCFCFC";
                element.readOnly = false;
                element.tabIndex = 0;
            }
        }
        if (element.type === "checkbox" || element.type === "radio") {
            element.disabled = false;
        // element.style.border='1px solid #C4C5C4';
        }
        if (element.type === "select-one") {
            element.disabled = false;
        }
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            imgs[k].style.visibility = "visible";
        }
    }
    if (document.getElementById("expandRatesTable") !== null && document.getElementById("expandRatesTable") !== undefined) {
        var length = document.getElementById("expandRatesTable").rows.length;
        for (var i = 0; i < length - 1; i++) {
            if (document.getElementById("accountname" + i) !== null && document.getElementById("accountno" + i)) {
                document.getElementById("accountname" + i).style.backgroundColor = "#CCEBFF";
                document.getElementById("accountname" + i).readOnly = true;
                document.getElementById("accountname" + i).tabIndex = -1;
                document.getElementById("accountname" + i).style.border = 0;
                document.getElementById("accountname" + i).className = "textlabelsBoldForTextBox";
                document.getElementById("accountno" + i).style.backgroundColor = "#CCEBFF";
                document.getElementById("accountno" + i).readOnly = true;
                document.getElementById("accountno" + i).tabIndex = -1;
                document.getElementById("accountno" + i).style.border = 0;
                document.getElementById("accountno" + i).className = "textlabelsBoldForTextBox";
            }
        }
    }
    if (document.getElementById("collapseRatesTable") !== null && document.getElementById("collapseRatesTable") !== undefined) {
        var length = document.getElementById("collapseRatesTable").rows.length;
        for (var j = 0; j < length - 1; j++) {
            if (document.getElementById("collapseaccountname" + j) !== null && document.getElementById("collapseaccountno" + j)) {
                document.getElementById("collapseaccountname" + j).style.backgroundColor = "#CCEBFF";
                document.getElementById("collapseaccountname" + j).readOnly = true;
                document.getElementById("collapseaccountname" + j).tabIndex = -1;
                document.getElementById("collapseaccountname" + j).style.border = 0;
                document.getElementById("collapseaccountname" + j).className = "textlabelsBoldForTextBox";
                document.getElementById("collapseaccountno" + j).style.backgroundColor = "#CCEBFF";
                document.getElementById("collapseaccountno" + j).readOnly = true;
                document.getElementById("collapseaccountno" + j).tabIndex = -1;
                document.getElementById("accountno" + j).style.border = 0;
                document.getElementById("accountno" + j).className = "textlabelsBoldForTextBox";
            }
        }
    }


}
function checkRoutedAgent() {
    if (document.getElementById("routedAgentCheck").checked) {
        document.getElementById("routedByAgent").value = document.getElementById("agentNo").value;
    } else {
        document.getElementById("routedByAgent").value = "";
    }
}
function setDefaultRouteAgent(importFlag) {
    var agentNo = document.getElementById("agentNo").value;
    if (document.getElementById("routedAgentCheck").value === "yes") {
        if (null !== agentNo && agentNo !== '') {
            document.getElementById("routedByAgent").value = agentNo;
        //            document.EditBookingsForm.routedByAgent.className="textlabelsBoldForTextBoxDisabledLook";
        //            document.EditBookingsForm.routedByAgent.readOnly=true;
        //            document.EditBookingsForm.routedByAgent.tabIndex=-1;
        } else {
            if (importFlag === "false") {
                document.getElementById("routedAgentCheck").selectedIndex = 0;
                alertNew("You must first select an agent");
            }
        }
    } else {
        document.getElementById("routedByAgent").value = "";
        document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
        document.getElementById("routedByAgent").readOnly = false;
        document.getElementById("routedByAgent").tabIndex = -1;
    }
}
function getConverttoBook(val, val1) {
    //---making the Page editable-----
    revertPageToEditMode(document.getElementById("editbook"));
    document.EditBookingsForm.selectedCheck.value = val1;
    if (document.EditBookingsForm.collapseid.value === "") {
        getNumbersChanged();
    }
    if (val === "oldgetRatesBKG") {
        document.EditBookingsForm.buttonValue.value = "converttobl";
    } else {
        document.EditBookingsForm.buttonValue.value = "converttoblnew";
    }
    document.EditBookingsForm.submit();
}
function dateInYardValidation(ev) {
    //---DateInYard should be greater than DateOutOfYard ---
    if (ev.value !== "") {
        ev.value = ev.value.getValidDateTime("/", "", false);
        if (ev.value === "" || ev.value.length > 10) {
            alertNew("Please enter valid date");
            ev.value = "";
            document.getElementById(ev.id).focus();
        }
    }
    if (ev.value !== "") {
        if (event.keyCode === 9 || event.keyCode === 13 || event.keyCode === 0) {
            if (document.EditBookingsForm.dateOutYard.value !== "") {
                var adate = document.EditBookingsForm.dateOutYard.value;
                var bdate = document.EditBookingsForm.dateInYard.value;
                var a = adate.split("-");
                var b = bdate.split("-");
                var sDate = new Date(a[2], a[0] - 1, getMonth(a[1]));
                var eDate = new Date(b[2], b[0] - 1, getMonthb([1]));
                if (sDate > eDate) {
                    alertNew("Date Back into Yard should be greater than or equal to Date out of Yard");
                    document.EditBookingsForm.dateInYard.value = "";
                    document.EditBookingsForm.dateInYard.select();
                }
            }
        }
    }
}
function setBookingNvoMove() {
    var optionValue = "";
    if (document.getElementById("moveType").type !== "select-one") {
        optionValue = document.getElementById("moveType").value;
    } else {
        optionValue = document.getElementById("moveType").options[document.getElementById("moveType").selectedIndex].text;
    }
    var length = "";
    var i = 0;
    if (document.getElementById("doorOrigin").value !== "" && document.getElementById("doorDestination").value === "") {
        if (optionValue !== "DOOR TO DOOR" && optionValue !== "DOOR TO RAIL" && optionValue !== "DOOR TO PORT" &&
            optionValue !== "RAMP TO DOOR" && optionValue !== "RAMP TO RAIL" && optionValue !== "RAMP TO PORT") {
            length = document.getElementById("moveType").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("moveType").options[i].text === "DOOR TO PORT") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            }
            alertNew("Please select the option that starts from DOOR");
        }
    } else if (document.getElementById("doorOrigin").value === "" && document.getElementById("doorDestination").value !== "") {
        if (optionValue !== "PORT TO DOOR" && optionValue !== "RAIL TO DOOR" &&
            optionValue !== "RAMP TO DOOR" && optionValue !== "RAMP TO RAIL" && optionValue !== "RAMP TO PORT") {
            length = document.getElementById("moveType").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("moveType").options[i].text === "PORT TO DOOR") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            }
            alertNew("Please select the option that ends from DOOR");
        }
    } else if (document.getElementById("doorOrigin").value !== "" && document.getElementById("doorDestination").value !== "") {
        if (optionValue !== "DOOR TO DOOR" &&
            optionValue !== "RAMP TO DOOR" && optionValue !== "RAMP TO RAIL" && optionValue !== "RAMP TO PORT") {
            length = document.getElementById("moveType").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("moveType").options[i].text === "DOOR TO DOOR") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            }
            alertNew("Please select the option that start and ends from DOOR");
        }
    } else if (document.getElementById("doorOrigin").value === "" && document.getElementById("doorDestination").value === "") {
        if (optionValue !== "PORT TO PORT" && optionValue !== "RAIL TO PORT" && optionValue !== "PORT TO RAIL"
            && optionValue !== "RAIL TO RAIL" && optionValue !== "RAMP TO DOOR" && optionValue !== "RAMP TO RAIL" && optionValue !== "RAMP TO PORT") {
            length = document.getElementById("moveType").length;
            for (var i = 0; i < length; i++) {
                if (document.getElementById("moveType").options[i].text === "PORT TO PORT") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            }
            alertNew("Please select the option that start and ends from PORT");
        }
    }
}
function dateInYardIsGreater(ev) {
    //---DateBackIntoYard should be greater than DateOutOfYard when onchange---
    if (ev.value !== "") {
        ev.value = ev.value.getValidDateTime("/", "", false);
        if (ev.value === "" || ev.value.length > 10) {
            alertNew("Please enter valid date");
            ev.value = "";
            document.getElementById(ev.id).focus();
        }
    }
    var preventRun = false;
    if (ev.value !== "") {
        if (document.EditBookingsForm.dateOutYard.value !== "") {
            var adate = document.EditBookingsForm.dateOutYard.value;
            var bdate = document.EditBookingsForm.dateInYard.value;
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateValidation",
                    param1: adate,
                    param2: bdate
                },
                async: false,
                success: function (data) {
                    if (data === "greater") {
                        alertNew("Date Back into Yard should be greater than or equal to Date out of Yard");
                        document.EditBookingsForm.dateInYard.value = "";
                        document.EditBookingsForm.dateInYard.select();
                        preventRun = true;
                    }
                }
            });
        }
        //---DateBackIntoYard should be lesser than or equal to ContainerCutoff date when onchange---
        if (!preventRun && document.EditBookingsForm.portCutOff.value !== "") {
            var adate = document.EditBookingsForm.portCutOff.value;
            var bdate = document.EditBookingsForm.dateInYard.value;
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateValidation",
                    param1: adate,
                    param2: bdate
                },
                async: false,
                success: function (data) {
                    if (data === "lesser") {
                        alertNew("Date Back into Yard should be lesser than or equal to Container Cutoff date");
                        document.EditBookingsForm.dateInYard.value = "";
                        document.EditBookingsForm.dateInYard.select();
                        preventRun = true;
                    }
                }
            });
        }
        if (!preventRun) {
            yearValidation(ev);
        }
    }
}
function dateOutOfYardValidation(ev) {
    //---DateOutOfYard should be lesser than DateInYard ---
    if (ev.value !== "") {
        ev.value = ev.value.getValidDateTime("/", "", false);
        if (ev.value === "" || ev.value.length > 10) {
            alertNew("Please enter valid date");
            ev.value = "";
            document.getElementById(ev.id).focus();
        }
    }
    if (ev.value !== "") {
        if (event.keyCode === 9 || event.keyCode === 13 || event.keyCode === 0) {
            if (document.EditBookingsForm.dateInYard.value !== "") {
                var adate = document.EditBookingsForm.dateOutYard.value;
                var bdate = document.EditBookingsForm.dateInYard.value;
                var a = adate.split("-");
                var b = bdate.split("-");
                var sDate = new Date(a[2], a[0] - 1, getMonth(a[1]));
                var eDate = new Date(b[2], b[0] - 1, getMonth(b[1]));
                if (sDate > eDate) {
                    alertNew("Date out of Yard should be less than or equal to Date Back into Yard");
                    document.EditBookingsForm.dateInYard.value = "";
                    document.EditBookingsForm.dateInYard.select();
                }
            }
        }
    }
}
function dateOutOfYardIsLesser(ev) {
    if (ev.value !== "") {
        ev.value = ev.value.getValidDateTime("/", "", false);
        if (ev.value === "" || ev.value.length > 10) {
            alertNew("Please enter valid date");
            ev.value = "";
            document.getElementById(ev.id).focus();
        }
    }
    var preventRun = false;
    //---DateOutOfYard should be lesser than DateInYard when onchange ---
    if (ev.value !== "") {
        if (document.EditBookingsForm.dateInYard.value !== "") {
            var adate = document.EditBookingsForm.dateOutYard.value;
            var bdate = document.EditBookingsForm.dateInYard.value;
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateValidation",
                    param1: adate,
                    param2: bdate
                },
                async: false,
                success: function (data) {
                    if (data === "lesser") {
                        alertNew("Date out of Yard should be less than or equal to Date Back into Yard");
                        document.EditBookingsForm.dateOutYard.value = "";
                        document.EditBookingsForm.dateOutYard.select();
                        preventRun = true;
                    }
                }
            });
        }
        if (!preventRun && document.EditBookingsForm.dateOutYard.value !== "" && document.EditBookingsForm.portCutOff.value !== "") {
            var outOfYard = document.EditBookingsForm.dateOutYard.value;
            var cutOff = document.EditBookingsForm.portCutOff.value;
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateValidation",
                    param1: outOfYard,
                    param2: cutOff
                },
                async: false,
                success: function (data) {
                    if (data === "greater") {
                        alertNew("Date Out of Yard must be less than or equal to the container cut off");
                        document.EditBookingsForm.dateOutYard.value = "";
                        document.EditBookingsForm.dateOutYard.select();
                        preventRun = true;
                    }
                }
            });
        }
        if (!preventRun) {
            yearValidation(ev);
        }
    }
}

function disableBillToCodeDup() {
    var agentOrConsignee = document.EditBookingsForm.billToCode[3].value;
    if (agentOrConsignee === 'A') {
        if (document.getElementById('agent').value === '') {
            alertNew('Please Select Agent First and then change to Collect');
            var code = "";
            var dest = document.getElementById("portOfDischarge").value;
            if (dest.lastIndexOf("(") !== -1) {
                code = dest.substring(dest.lastIndexOf("(") + 1, dest.lastIndexOf(")"));
            }
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "getDestCodeforHBL",
                    param1: code
                },
                async: false,
                success: function (data) {
                    if (data !== null && data !== '' && data === 'Y') {
                        document.getElementById('pRadio').checked = true;
                    }
                }
            });
            return;
        }
    } else {//consignee
        if (document.getElementById('consignee').value === '') {
            alertNew('Please Select Consignee Name from Shipper Forwarder Consignee Tab then change to Collect');
            document.getElementById('pRadio').checked = true;
            return;
        }
    }

    if (document.EditBookingsForm.prepaidToCollect[1].checked) {
        document.EditBookingsForm.billToCode[3].checked = true;
        document.EditBookingsForm.billToCode[0].disabled = true;
        document.EditBookingsForm.billToCode[1].disabled = true;
        document.EditBookingsForm.billToCode[2].disabled = true;
        document.EditBookingsForm.billToCode[3].disabled = true;
    } else {
        document.EditBookingsForm.billToCode[0].disabled = false;
        document.EditBookingsForm.billToCode[1].disabled = false;
        document.EditBookingsForm.billToCode[2].disabled = false;
        document.EditBookingsForm.billToCode[3].disabled = false;
        document.EditBookingsForm.billToCode[3].checked = false;
        document.EditBookingsForm.billToCode[3].disabled = true;
    }
}
function disableBillToCode(action) {
    var bookingId = document.EditBookingsForm.bookingId.value;
    if (document.EditBookingsForm.prepaidToCollect[1].checked) {
        document.EditBookingsForm.billToCode[3].checked = true;
        document.EditBookingsForm.billToCode[0].disabled = true;
        document.EditBookingsForm.billToCode[1].disabled = true;
        document.EditBookingsForm.billToCode[2].disabled = true;
        document.EditBookingsForm.billToCode[3].disabled = true;
    } else {
        var preventRun = true;
        if (action === 'radio') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "checkExistADVFFandADVSHP",
                    param1: bookingId
                },
                async: false,
                success: function (data) {
                    if (data !== null && data !== '') {
                        confirmNew("Change to Prepaid Shipper will delete All Advance Surcharge Y/N", "deletePBACharges");
                        preventRun = true;
                    } else {
                        // it wil exceute when click on radio btton
                        document.EditBookingsForm.billToCode[0].disabled = false;
                        document.EditBookingsForm.billToCode[0].checked = true;
                        document.EditBookingsForm.billToCode[1].disabled = false;
                        document.EditBookingsForm.billToCode[2].disabled = false;
                        document.EditBookingsForm.billToCode[3].disabled = false;
                        document.EditBookingsForm.billToCode[3].checked = false;
                        document.EditBookingsForm.billToCode[3].disabled = true;
                    }
                }
            });
        } else {
            // it wil execute while loading of a page
            document.EditBookingsForm.billToCode[0].disabled = false;
            document.EditBookingsForm.billToCode[1].disabled = false;
            document.EditBookingsForm.billToCode[2].disabled = false;
            document.EditBookingsForm.billToCode[3].disabled = false;
            document.EditBookingsForm.billToCode[3].checked = false;
            document.EditBookingsForm.billToCode[3].disabled = true;
        }
    }
    if (preventRun) {
        return;
    }
}
function disableSpecialEqpmt() {
    if (document.EditBookingsForm.specialequipment[0].checked) {
        enableSpecialEquipment();
    } else if (document.EditBookingsForm.specialequipment[1].checked) {
        document.EditBookingsForm.specialEqpmtUnit.disabled = true;
        if (document.EditBookingsForm.specialEqpmtSelectBox) {
            document.EditBookingsForm.specialEqpmtSelectBox.options[0].selected = true;
            document.EditBookingsForm.specialEqpmtSelectBox.disabled = true;
        } else if (document.getElementById("specialEqpmtSelectBox_readonly")) {
            jQuery("#specialEqpmtSelectBox_readonly").val("").attr("disabled", true);
        }
        confirmNew("Special Equipment Charges will be deleted and all the Booking changes will be saved. Are you sure?", "deleteSpecialEquipment");
    }
}
function scan(fileNo, importFlag) {
    if (null !== fileNo && fileNo !== '') {
        var screenName = "FCLFILE";
        if (null !== importFlag && importFlag === 'true') {
            screenName = "IMPORT FILE";
        }
        GB_show("Scan", "/logisoft/scan.do?screenName=" + screenName + "&documentId=" + fileNo + "&ignoreDocumentName=SS LINE MASTER BL" + "&", 420, 1150);
    } else {
        alertNew("Please save the file before Scan/Attach");
    }

}
function getCheckFFCommssionCollapse(val) {
    if (!document.EditBookingsForm.collapseprint[val].checked) {
        alertNew("The Commission Amount will show up in BL");
    }
    saveCollapsedBundleOFR();
}
function getCheckFFCommssionExapnd(val) {
    if (!document.EditBookingsForm.print[val].checked) {
        alertNew("The Commission Amount will show up in BL");
    }
    saveExpandedBundleOFR();
}
function saveCollapsedBundleOFR() {
    collapseid = "collapse";
    bundleCheck = "";
    if (document.EditBookingsForm.collapseprint !== undefined) {
        if (document.EditBookingsForm.collapseprint.length !== undefined) {
            for (var i = 0; i < document.EditBookingsForm.collapseprint.length; i++) {
                if (document.EditBookingsForm.collapseprint[i].checked) {
                    bundleCheck = bundleCheck + "1,";
                } else {
                    bundleCheck = bundleCheck + "0,";
                }
            }
        } else {
            if (document.EditBookingsForm.collapseprint.checked) {
                bundleCheck = bundleCheck + "1,";
            } else {
                bundleCheck = bundleCheck + "0,";
            }
        }
    }
    document.EditBookingsForm.collapseid.value = collapseid;
}
function saveExpandedBundleOFR() {
    collapseid = "expand";
    bundleCheck = "";
    if (document.EditBookingsForm.print !== undefined) {
        if (document.EditBookingsForm.print.length !== undefined) {
            for (var i = 0; i < document.EditBookingsForm.print.length; i++) {
                if (document.EditBookingsForm.print[i].checked) {
                    bundleCheck = bundleCheck + "1,";
                } else {
                    bundleCheck = bundleCheck + "0,";
                }
            }
        } else {
            if (document.EditBookingsForm.print.checked) {
                bundleCheck = bundleCheck + "1,";
            } else {
                bundleCheck = bundleCheck + "0,";
            }
        }
    }
    document.EditBookingsForm.collapseid.value = collapseid;
}
function saveOtherBundleOFR() {
    var check = "";
    if (document.EditBookingsForm.otherprint !== undefined) {
        if (document.EditBookingsForm.otherprint.length !== undefined) {
            for (var i = 0; i < document.EditBookingsForm.otherprint.length; i++) {
                if (document.EditBookingsForm.otherprint[i].checked) {
                    check = check + "1,";
                } else {
                    check = check + "0,";
                }
            }
        } else {
            if (document.EditBookingsForm.otherprint.checked) {
                check = check + "1,";
            } else {
                check = check + "0,";
            }
        }
    }
    document.EditBookingsForm.otherChargesBundleOfr.value = check;
    document.EditBookingsForm.collapseid.value = "";
}
function convertToQuote(ev) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO",
            methodName: "costIsAlreadyPaidByBookingId",
            param1: document.EditBookingsForm.bookingId.value,
            dataType: "json"
        },
        success: function (result) {
            if (result === true) {
                alertNew("Cannot reverse to Quotation because some of the Costs are paid in Accounting.");
                return;
            } else {
                confirmNew("Are you sure? You want to convert this booking to quotation", "convertToQuotation");
            }
        }
    });
}
function fillReturnAddress() {
    if (document.getElementById("autoAddressFill").checked) {
        document.getElementById("addressofDelivery").value = document.getElementById("emptypickupaddress").value;
        document.getElementById("equipmentReturnName").value = document.getElementById("exportPositioning").value;
        document.getElementById("equipmentReturnTemp").value = document.getElementById("wareHouseTemp").value;
        document.getElementById("equipmentReturn_check").value = document.getElementById("exportPositioning").value;
    } else {
        document.getElementById("addressofDelivery").value = "";
        document.getElementById("equipmentReturnName").value = "";
        document.getElementById("equipmentReturnTemp").value = "";
    }
}
function insertShipperDetails() {
    if (document.getElementById("spottingCheckBox").checked) {
        document.EditBookingsForm.spottingAccountName.value = document.EditBookingsForm.shipperName.value;
        document.EditBookingsForm.spottingAccountNo.value = document.EditBookingsForm.shipper.value;
        document.EditBookingsForm.addressofExpPosition.value = document.EditBookingsForm.shipperAddress.value;
        document.EditBookingsForm.spotAddrCity.value = document.EditBookingsForm.shipperCity.value;
        document.EditBookingsForm.spotAddrState.value = document.EditBookingsForm.shipperState.value;
        document.EditBookingsForm.spotAddrZip.value = document.EditBookingsForm.shipperZip.value;
    } else {
        document.EditBookingsForm.spottingAccountName.value = "";
        document.EditBookingsForm.spottingAccountNo.value = "";
        document.EditBookingsForm.addressofExpPosition.value = "";
        document.EditBookingsForm.spotAddrCity.value = "";
        document.EditBookingsForm.spotAddrState.value = "";
        document.EditBookingsForm.spotAddrZip.value = "";
    }
}

function getSpottingInfo() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: document.getElementById("spottingAccountNo").value
        },
        success: function (disabled) {
            if (disabled !== "") {
                alertNew(disabled);
                document.getElementById("spottingAccountNo").value = "";
                document.getElementById("spottingAccountName").value = "";
                document.getElementById("addressofExpPosition").value = "";
                document.getElementById("spotAddrCity").value = "";
                document.getElementById("spotAddrState").value = "";
                document.getElementById("spotAddrZip").value = "";
            } else {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                        methodName: "getCustAddressForNo",
                        param1: document.EditBookingsForm.spottingAccountNo.value,
                        dataType: "json"
                    },
                    success: function (data) {
                        var addr = "";
                        if (data) {
                            document.getElementById("spottingAccountName").value = data.acctName;
                            document.getElementById("spottingname_check").value = data.acctName;
                            if (data.acctNo !== null && data.acctNo !== "") {
                                document.getElementById("spottingAccountNo").value = data.acctNo;
                            } else {
                                document.getElementById("spottingAccountNo").value = "";
                            }
                            if (data.address1 !== null && data.address1 !== "") {
                                for (var k = 30; k < data.address1.length; k = k + 30) {
                                    if (data.address1.length >= k) {
                                        addr += data.address1.substring(k - 30, k) + "\n";
                                    } else {
                                        addr += data.address1 + "\n";
                                    }
                                }
                                addr += data.address1.substring(addr.trim().length, data.address1.trim().length) + "\n";
                                document.getElementById("addressofExpPosition").value = addr.trim();
                            } else {
                                document.getElementById("addressofExpPosition").value = "";
                            }
                            if (data.city1 !== null && data.city1 !== "") {
                                document.getElementById("spotAddrCity").value = data.city1;
                            }
                            else {
                                document.getElementById("spotAddrCity").value = "";
                            }
                            if (data.state !== null && data.state !== "") {
                                document.getElementById("spotAddrState").value = data.state;
                            }
                            else {
                                document.getElementById("spotAddrState").value = "";
                            }
                            if (data.zip !== null && data.zip !== "") {
                                document.getElementById("spotAddrZip").value = data.zip;
                            }
                            else {
                                document.getElementById("spotAddrZip").value = "";
                            }
                        }
                    }
                });
            }
        }
    });
}
function getSpottingInfoForPopUp() {
    if (event.keyCode === 9 || event.keyCode === 13 || event.keyCode === 0) {
        getSpottingInfo();
    }
}
function setFocusToSSVOY() {
    if (event.keyCode === 9 || event.keyCode === 13) {
        setTimeout("setFocusToSsVOY()", 150);
    }
}
function setFocusToSsVOY() {
    document.EditBookingsForm.voyage.select();
}
function getEquipmentDate() {
    document.EditBookingsForm.dateOutYard.value = document.EditBookingsForm.empPickupDate.value;
    document.EditBookingsForm.dateInYard.value = document.EditBookingsForm.empPickupDate.value;
}
function insertDate(ev) {
    document.getElementById("txtcal313").value = "";
    insertDateFromCalendar(ev.id, 9);
}
function insertDateForDocCutOff(ew) {
    document.getElementById("txtcal71").value = "";
    insertDateFromCalendar(ew.id, 9);
}
function setFocusToVoyInternal() {
    if (event.keyCode === 9 || event.keyCode === 13) {
        setTimeout("changeFocus()", 150);
    }
}
function changeFocus() {
    document.EditBookingsForm.vaoyageInternational.select();
}
function validateQuantity(val, index) {
    if (val === 0 || val < 0) {
        alertNew("Quantity Cannot be Zero,Empty or Negative");
        document.getElementById("hiddennumbers" + index).value = "1";
        return;
    }
}
function validateQuantityForExpand(val, index) {
    if (val === 0 || val < 0) {
        alertNew("Quantity Cannot be Zero,Empty or Negative");
        document.getElementById("numbers" + index).value = "1";
        return;
    }
}
function greyOutTruckerCheckBox(form) {
    var optionValue = "";
    var element;
    var imgs = document.getElementsByTagName("img");
    if (document.EditBookingsForm.bookingComplete[1].checked) {
        if (document.EditBookingsForm.lineMove.value === "DOOR TO DOOR" || document.EditBookingsForm.lineMove.value === "DOOR TO PORT" || document.EditBookingsForm.lineMove.value === "DOOR TO RAIL") {
            for (var i = 0; i < form.elements.length; i++) {
                element = form.elements[i];
                if ((element.type === "text" || element.type === "textarea") && (element.id === "txtcal99" || element.id === "exportPositioning" || element.id === "emptypickupaddress" || element.id === "pickUpRemarks"
                    || element.id === "addressofDelivery" || element.id === "returnRemarks" || element.id === "txtcal13" || element.id === "txtcal14"
                    || element.id === "truckerName" || element.id === "addressoftrucker" || element.id === "truckerCity" || element.id === "equipmentReturnName"
                    || element.id === "truckerState" || element.id === "truckerZip" || element.id === "truckerPhone" || element.id === "truckerEmail" || element.id === "truckerClientReference")) {
                    if (document.getElementById("issuingTerminal").value === "MIAMI, FL-01" && element.id === "txtcal13") {
                        element.readOnly = false;
                        element.tabIndex = 0;
                        element.className = "textlabelsBoldForTextBox";
                    } else {
                        element.style.backgroundColor = "#CCEBFF";
                        element.readOnly = true;
                        element.tabIndex = -1;
                        element.style.border = 0;
                    }
                //element.className = "BackgrndColorForTextBox";
                } else if (element.type === "checkbox") {
                    if (element.id === "autoAddressFill" || element.id === "truckerCheckbox") {
                        element.style.border = 0;
                        element.disabled = true;
                        element.className = "whitebackgrnd";
                    }

                }
            }
            for (var k = 0; k < imgs.length; k++) {
                if (document.getElementById("issuingTerminal").value === "MIAMI, FL-01") {
                    if (imgs[k].id === "cal99" || imgs[k].id === "cal14" || imgs[k].id === "toggle02" || imgs[k].id === "toggle01" || imgs[k].id === "addName"
                        || imgs[k].id === "truckerContactButton" || imgs[k].id === "addWareHouse" || imgs[k].id === "addEquipmentReturn") {
                        imgs[k].style.visibility = "hidden";
                    }
                } else {
                    if (imgs[k].id === "cal99" || imgs[k].id === "cal13" || imgs[k].id === "cal14" || imgs[k].id === "toggle02" || imgs[k].id === "toggle01"
                        || imgs[k].id === "addName" || imgs[k].id === "truckerContactButton" || imgs[k].id === "addWareHouse" || imgs[k].id === "addEquipmentReturn") {
                        imgs[k].style.visibility = "hidden";
                    }
                }
            }
        } else {
            for (var j = 0; j < form.elements.length; j++) {
                element = form.elements[j];
                if (element.type === "text" || element.type === "textarea") {
                    if (element.id === "txtcal99" || element.id === "exportPositioning" || element.id === "emptypickupaddress" || element.id === "pickUpRemarks" || element.id === "addressofDelivery" || element.id === "returnRemarks" || element.id === "txtcal13" || element.id === "txtcal14" || element.id === "truckerName" || element.id === "addressoftrucker" || element.id === "truckerCity" || element.id === "truckerState" ||
                        element.id === "truckerZip" || element.id === "truckerPhone" || element.id === "truckerEmail" || element.id === "truckerClientReference" || element.id === "equipmentReturnName") {
                        element.style.border = "1px solid #C4C5C4";
                        element.style.backgroundColor = "#FCFCFC";
                        element.readOnly = false;
                        element.tabIndex = 0;
                    }
                } else if (element.type === "checkbox") {
                    if (element.id === "autoAddressFill" || element.id === "truckerCheckbox" || element.id === "rampCheck") {
                        element.disabled = false;
                        element.style.color = "black";
                    //element.className = "textlabelsBoldForTextBox";
                    }

                }
            }
            for (var q = 0; q < imgs.length; q++) {
                if (imgs[q].id === "cal99" || imgs[q].id === "cal13" || imgs[q].id === "cal14" || imgs[q].id === "toggle02" || imgs[q].id === "toggle01" || imgs[q].id === "addName" || imgs[q].id === "truckerContactButton" || imgs[q].id === "addWareHouse" || imgs[q].id === "addEquipmentReturn") {
                    imgs[q].style.visibility = "visible";
                }
            }

        }
    }
}
function LineM() {
    document.getElementById('save1').focus();
}
function disableAutoFF() {
    if (undefined !== document.getElementById("portOfDischarge") && null !== document.getElementById("portOfDischarge")
        && "" !== document.getElementById("portOfDischarge").value && importFlag === 'false') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "checkForTheRegion",
                param1: document.getElementById("portOfDischarge").value
            },
            success: function (data) {
                if (undefined !== data && null !== data) {
                    if (data === "true") {
                        checkforCommodity();
                    } else {
                        document.getElementById("n5").checked = true;
                        document.getElementById("n5").disabled = true;
                        document.getElementById("y5").disabled = true;
                    }
                }
                if (null !== document.getElementById('inputRatesManually')
                    && undefined !== document.getElementById('inputRatesManually')
                    && document.getElementById('inputRatesManually').style.display === 'none') {
                    document.getElementById('n5').disabled = true;
                    document.getElementById('y5').disabled = true;
                }
            }
        });
    }
}

function checkforCommodity() {
    var commcode = document.getElementById("commcode").value;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "checkForCommodity",
            param1: commcode
        },
        success: function (data) {
            if (data === 'true') {
                document.getElementById('n5').disabled = false;
                document.getElementById('y5').disabled = false;
            } else {
                document.getElementById('n5').checked = true;
                document.getElementById('n5').disabled = true;
                document.getElementById('y5').disabled = true;
            }
        }
    });
}
function checkHazmat() {
    if (document.EditBookingsForm.hazmat[0].checked) {
        document.getElementById("hazmatButton").style.visibility = 'visible';
        document.getElementById("hazmatButtonDown").style.visibility = 'visible';
    } else {
        document.getElementById("hazmatButton").style.visibility = 'hidden';
        document.getElementById("hazmatButtonDown").style.visibility = 'hidden';
    }
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "checkHazmat",
            param1: document.EditBookingsForm.bookingId.value,
            dataType: "json"
        },
        success: function (data) {
            if (data === "true") {
                document.getElementById("hazmatButton").className = "buttonColor";
                document.getElementById("hazmatButtonDown").className = "buttonColor";
            } else {
                document.getElementById("hazmatButton").className = "buttonStyleNew";
                document.getElementById("hazmatButtonDown").className = "buttonStyleNew";
            }
        }
    });
}
function changeScanButtonColor(masterStatus, documentName, docList) {
    if (docList === 0) {
        document.getElementById("scanButton").className = "buttonStyleNew";
        document.getElementById("scanButtonDown").className = "buttonStyleNew";
    } else {
        document.getElementById("scanButton").className = "buttoncolor";
        document.getElementById("scanButtonDown").className = "buttoncolor";
    }
}
function checkForDisable() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: document.getElementById("accountNumber1").value
        },
        success: function (data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
                document.getElementById("accountName").value = "";
                document.getElementById("accountNumber1").value = "";
            }
        }
    });
}
function checkForDisableRouted() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: document.getElementById("RoutedDup").value
        },
        success: function (data) {
            if (data !== "") {
                alertNew(data);
                document.getElementById("routedByAgent").value = "";
                document.getElementById("RoutedDup").value = "";
            } else {
                document.getElementById("routedByAgent").value = document.getElementById("RoutedDup").value;
            }
        }
    });
}
function checkForDisableAgent(focusTo) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: document.getElementById("agentNo").value
        },
        success: function (data) {
            if (data !== "") {
                alertNew(data);
                document.getElementById("agent").value = "";
                document.getElementById("agentNo").value = "";
            }
        }
    });
    setFocusFromDojo(focusTo);
}
// method to save new booking........................................
function saveOrUpdate() {
    if (document.EditBookingsForm.bookingId.value === '') {
        saveNdewBooking();
    } else {
        SAVE();
    }
}
function saveNdewBooking() {
    //portOfDischarge
    //originTerminal
    //commcode
    //issuingTerminal
    var errorMessage = '';
    var length = "";
    var optionValue = "";
    if (document.EditBookingsForm.portOfDischarge.value === '') {
        errorMessage += "-->Please Enter Destination<br>";
        jQuery("#portOfDischarge").css("border-color", "red");
    }
    if (document.EditBookingsForm.originTerminal.value === '') {
        errorMessage += "-->Please Enter Origin Terminal<br>";
        jQuery("#originTerminal").css("border-color", "red");
    }
    if (document.EditBookingsForm.issuingTerminal.value === '') {
        errorMessage += "-->Please Enter Issuing Terminal<br>";
        jQuery("#issuingTerminal").css("border-color", "red");
    }
    if (errorMessage !== '') {
        alertNew(errorMessage);
        return false;
    }
    if (document.getElementById("emptypickupaddress").value.length > 200) {
        alertNew("Enter less than 200 character in Equipment Pickup Name/Address");
        return;
    }
    if (document.getElementById("pickUpRemarks").value.length > 100) {
        alertNew("More than 100 characters are not allowed in Equipment Pickup Remarks");
        return;
    }
    if (document.getElementById("addressofDelivery").value.length > 100) {
        alertNew("More than 100 characters are not allowed in Equipment Return Name/Address");
        return;
    }
    if (document.getElementById("returnRemarks").value.length > 100) {
        alertNew("More than 100 characters are not allowed in Equipment Return Remarks");
        return;
    }
    if (document.getElementById("loadRemarks").value.length > 100) {
        alertNew("More than 100 characters are not allowed in Spotting Address Remarks");
        return;
    }
    if (document.getElementById("moveType").type !== "select-one") {
        optionValue = document.getElementById("moveType").value;
    } else {
        optionValue = document.getElementById("moveType").options[document.getElementById("moveType").selectedIndex].text;
    }
    if (document.getElementById("doorOrigin").value !== "" && document.getElementById("doorDestination").value === "") {
        if (optionValue !== "DOOR TO DOOR" && optionValue !== "DOOR TO RAIL" && optionValue !== "DOOR TO PORT"
            && optionValue !== "RAMP TO DOOR" && optionValue !== "RAMP TO RAIL" && optionValue !== "RAMP TO PORT") {
            length = document.getElementById("moveType").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("moveType").options[i].text === "DOOR TO PORT") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            }
        }
    } else if (document.getElementById("doorOrigin").value === "" && document.getElementById("doorDestination").value !== "") {
        if (optionValue !== "PORT TO DOOR" && optionValue !== "RAIL TO DOOR"
            && optionValue !== "RAMP TO DOOR" && optionValue !== "RAMP TO RAIL" && optionValue !== "RAMP TO PORT") {
            length = document.getElementById("moveType").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("moveType").options[i].text === "PORT TO DOOR") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            }
        }
    } else if (document.getElementById("doorOrigin").value !== "" && document.getElementById("doorDestination").value !== "") {
        if (optionValue !== "DOOR TO DOOR" && optionValue !== "RAMP TO DOOR" && optionValue !== "RAMP TO RAIL" && optionValue !== "RAMP TO PORT") {
            length = document.getElementById("moveType").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("moveType").options[i].text === "DOOR TO DOOR") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            }
        }
    } else if (document.getElementById("doorOrigin").value === "" && document.getElementById("doorDestination").value === "") {
        if (optionValue !== "PORT TO PORT" && optionValue !== "RAIL TO PORT" && optionValue !== "PORT TO RAIL"
            && optionValue !== "RAIL TO RAIL" && optionValue !== "RAMP TO DOOR" && optionValue !== "RAMP TO RAIL" && optionValue !== "RAMP TO PORT") {
            length = document.getElementById("moveType").length;
            for (var i = 0; i < length; i++) {
                if (document.getElementById("moveType").options[i].text === "PORT TO PORT") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            }
        }
    }
    document.EditBookingsForm.bookingComplete[1].checked = true;
    document.EditBookingsForm.buttonValue.value = 'update';
    document.EditBookingsForm.submit();
}

function setFocusToBookingNo() {
    if (document.EditBookingsForm.bookingComplete[1].checked) {
        setTimeout("setFocus()", 800);
    }
}
function setFocus() {
    document.getElementById('scroll').scrollIntoView(true);
    document.EditBookingsForm.SSBooking.focus();
}
function setComCode() {
    var commodityCode = document.EditBookingsForm.commcode.value;
    if (commodityCode !== '') {
        var comAndDesc = commodityCode.split(':-');
        if (comAndDesc.length > 0) {
            document.EditBookingsForm.commcode.value = comAndDesc[0];
            document.EditBookingsForm.comdesc.value = comAndDesc[1];
        }
    }
}
function disableDojo(obj) {
    var path = "";
    var disable = 'disable';
    if (obj.id === "shipperName") {
        if (document.getElementById("shipperTpCheck").checked) {
            path = "&disableShipperDojo=" + disable;
            Event.stopObserving("shipperName", "blur");
        }
    } else if (obj.id === "consigneename") {
        if (document.getElementById("consigneeTpCheck").checked) {
            path = "&disableConsigneeDojo=" + disable;
            Event.stopObserving("consigneename", "blur");
        }
    } else if (obj.id === "spottingAccountName") {
        if (document.getElementById("spottAddrTpCheck").checked) {
            path = "&disableSpottAddrDojo=" + disable;
            Event.stopObserving("spottingAccountName", "blur");
        }
    } else if (obj.id === "truckerName") {
        if (document.getElementById("truckerTpCheck").checked) {
            path = "&disableTruckerDojo=" + disable;
            Event.stopObserving("truckerName", "blur");
        }
    }
    appendEncodeUrl(path);
}
function disableAutoComplete(obj) {
    if (obj.checked) {
        if (obj.id === "consigneeTpCheck") {
            if (document.getElementById("consigneeName").value !== "") {
                confirmNew("Do you want to clear existing Consignee Details", "clearConsigneeDetails");
            } else {
                document.getElementById("consigneeName").value = document.getElementById("consigneeTpCopy").value;
                Event.stopObserving("consigneeName", "blur");
            }
        } else if (obj.id === "shipperTpCheck") {
            if (document.getElementById("shipperName").value !== "") {
                confirmNew("Do you want to clear existing Shipper Details", "clearShipperDetails");
            } else {
                document.getElementById("shipperName").value = document.getElementById("shipperTpCopy").value;
                Event.stopObserving("shipperName", "blur");
            }
        } else if (obj.id === "spottAddrTpCheck") {
            if (document.getElementById("spottingAccountName").value !== "") {
                confirmNew("Do you want to clear existing Spotting Address Details", "clearSpotAddrDetails");
            } else {
                document.getElementById("spottingAccountName").value = document.getElementById("spottAddrTpCopy").value;
                Event.stopObserving("spottingAccountName", "blur");
            }
        } else if (obj.id === "truckerTpCheck") {
            if (document.getElementById("truckerName").value !== "") {
                confirmNew("Do you want to clear existing Trucker Details", "clearTruckerDetails");
            } else {
                document.getElementById("truckerName").value = document.getElementById("truckerTpCopy").value;
                Event.stopObserving("truckerName", "blur");
            }
        }
    } else {
        if (obj.id === "consigneeTpCheck") {
            if (document.getElementById("consigneeName").value !== "") {
                confirmConsigneeBox("Do you want to clear existing Consignee Details");
            } else {
                document.getElementById("consignee").disabled = false;
                //document.getElementById("consignee").style.border='1px solid #C4C5C4';
                //document.getElementById("consignee").style.backgroundColor='#FFFFFF';
                Event.observe("consigneeName", "blur", function (event) {
                    var element = Event.element(event);
                    if (element.value !== $("consigneeName_check").value) {
                        element.value = '';
                        $("consigneeName_check").value = '';
                        $("consigneeName").value = '';
                        $("consignee").value = '';
                        $("consigneeAddress").value = '';
                        $("consigneeCity").value = '';
                        $("consigneeState").value = '';
                        $("consigneeZip").value = '';
                        $("consigneeCountry").value = '';
                        $("consigneePhone").value = '';
                        $("consigneeFax").value = '';
                        $("consigneeEmail").value = '';
                        $("consigneeClientReference").value = '';
                    }
                }
                );
            }
        } else if (obj.id === "shipperTpCheck") {
            if (document.getElementById("shipperName").value !== "") {
                confirmShipperBox("Do you want to clear existing Shipper Details");
            } else {
                document.getElementById("shipper").disabled = false;
                //document.getElementById("shipper").style.border='1px solid #C4C5C4';
                //document.getElementById("shipper").style.backgroundColor='#FFFFFF';
                Event.observe("shipperName", "blur", function (event) {
                    var element = Event.element(event);
                    if (element.value !== $("shipperName_check").value) {
                        element.value = '';
                        $("shipperName_check").value = '';
                        $("shipperName").value = '';
                        $("shipper").value = '';
                        $("shipperAddress").value = '';
                        $("shipperCity").value = '';
                        $("shipperState").value = '';
                        $("shipperZip").value = '';
                        $("shipperCountry").value = '';
                        $("shipPho").value = '';
                        $("shipperFax").value = '';
                        $("shipperEmail").value = '';
                        $("shipperClientReference").value = '';
                    }
                }
                );
            }
        } else if (obj.id === "spottAddrTpCheck") {
            if (document.getElementById("spottingAccountName").value !== "") {
                confirmSpottAddrBox("Do you want to clear existing Spotting Address Details");
            } else {
                document.getElementById("spottingAccountNo").disabled = false;
                Event.observe("spottingAccountName", "blur", function (event) {
                    var element = Event.element(event);
                    if (element.value !== $("spottingname_check").value) {
                        element.value = '';
                        $("spottingname_check").value = '';
                        $("spottingAccountName").value = '';
                        $("spottingAccountNo").value = '';
                        $("addressofExpPosition").value = '';
                        $("spotAddrCity").value = '';
                        $("spotAddrState").value = '';
                        $("spotAddrZip").value = '';

                    }
                }
                );
            }
        } else if (obj.id === 'truckerTpCheck') {
            if (document.getElementById("truckerName").value !== "") {
                confirmTruckerBox("Do you want to clear existing Trucker Details");
            } else {
                document.getElementById("truckerCode").disabled = false;
                Event.observe("truckerName", "blur", function (event) {
                    var element = Event.element(event);
                    if (element.value !== $("trucker_check").value) {
                        element.value = '';
                        $("trucker_check").value = '';
                        $("truckerName").value = '';
                        $("truckerCode").value = '';
                        $("addressoftrucker").value = '';
                        $("truckerCity").value = '';
                        $("truckerState").value = '';
                        $("truckerZip").value = '';
                        $("truckerPhone").value = '';
                        $("truckerEmail").value = '';
                        $("truckerClientReference").value = '';
                    }
                }
                );
            }
        }
    }
}
function clearSpottAddrDetails() {
    if (document.getElementById('spottAddrTpCheck').checked) {
        onSpottAddrrBlur();
        Event.stopObserving("spottingAccountName", "blur");
    } else {
        onSpottAddrrBlur();
        Event.observe("spottingAccountName", "blur", function (event) {
            var element = Event.element(event);
            if (element.value !== $("spottingname_check").value) {
                element.value = '';
                $("spottingname_check").value = '';
                $("spottingAccountName").value = '';
                $("spottingAccountNo").value = '';
                $("addressofExpPosition").value = '';
                $("spotAddrCity").value = '';
                $("spotAddrState").value = '';
                $("spotAddrZip").value = '';
            }
        }
        );
    }
}
function clearShipperDetails() {
    if (document.getElementById('shipperTpCheck').checked) {
        onShipperBlur();
        Event.stopObserving("shipperName", "blur");
    } else {
        onShipperBlur();
        Event.observe("shipperName", "blur", function (event) {
            var element = Event.element(event);
            if (element.value !== $("shipperName_check").value) {
                element.value = '';
                $("shipperName_check").value = '';
                $("shipperName").value = '';
                $("shipper").value = '';
                $("shipperAddress").value = '';
                $("shipperCity").value = '';
                $("shipperState").value = '';
                $("shipperZip").value = '';
                $("shipperCountry").value = '';
                $("shipPho").value = '';
                $("shipperFax").value = '';
                $("shipperEmail").value = '';
                $("shipperClientReference").value = '';
            }
        }
        );
    }
}
function clearTruckerDetails() {
    if (document.getElementById('truckerTpCheck').checked) {
        onTruckerBlur();
        Event.stopObserving("truckerName", "blur");
    } else {
        onTruckerBlur();
        Event.observe("truckerName", "blur", function (event) {
            var element = Event.element(event);
            if (element.value !== $("trucker_check").value) {
                element.value = '';
                $("trucker_check").value = '';
                $("truckerName").value = '';
                $("truckerCode").value = '';
                $("addressoftrucker").value = '';
                $("truckerCity").value = '';
                $("truckerState").value = '';
                $("truckerZip").value = '';
                $("truckerPhone").value = '';
                $("truckerEmail").value = '';
                $("truckerClientReference").value = '';
            }
        }
        );
    }
}
function uncheckClearShipper() {
    if (document.getElementById('shipperTpCheck').checked) {
        onShipperBlur();
        Event.stopObserving("shipperName", "blur");
    } else {
        onShipperBlur();
        document.getElementById("shipperTpCopy").value = '';
        document.getElementById("shipper").disabled = false;
        Event.observe("shipperName", "blur", function (event) {
            var element = Event.element(event);
            if (element.value !== $("shipperName_check").value) {
                element.value = '';
                $("shipperName_check").value = '';
                $("shipperName").value = '';
                $("shipper").value = '';
                $("shipperAddress").value = '';
                $("shipperCity").value = '';
                $("shipperState").value = '';
                $("shipperZip").value = '';
                $("shipperCountry").value = '';
                $("shipPho").value = '';
                $("shipperFax").value = '';
                $("shipperEmail").value = '';
                $("shipperClientReference").value = '';
            }
        }
        );
    }
}
function uncheckClearSpottAddr() {
    if (document.getElementById('spottAddrTpCheck').checked) {
        onSpottAddrrBlur();
        Event.stopObserving("spottingAccountName", "blur");
    } else {
        onSpottAddrrBlur();
        document.getElementById("spottAddrTpCopy").value = '';
        document.getElementById("spottingAccountNo").disabled = false;
        Event.observe("spottingAccountName", "blur", function (event) {
            var element = Event.element(event);
            if (element.value !== $("spottingname_check").value) {
                element.value = '';
                $("spottingname_check").value = '';
                $("spottingAccountName").value = '';
                $("spottingAccountNo").value = '';
                $("addressofExpPosition").value = '';
                $("spotAddrCity").value = '';
                $("spotAddrState").value = '';
                $("spotAddrZip").value = '';
            }
        }
        );
    }
}
function uncheckClearTrucker() {
    if (document.getElementById('truckerTpCheck').checked) {
        onTruckerBlur();
        Event.stopObserving("truckerName", "blur");
    } else {
        onTruckerBlur();
        document.getElementById("truckerTpCopy").value = '';
        document.getElementById("truckerCode").disabled = false;
        Event.observe("truckerName", "blur", function (event) {
            var element = Event.element(event);
            if (element.value !== $("trucker_check").value) {
                element.value = '';
                $("trucker_check").value = '';
                $("truckerName").value = '';
                $("truckerCode").value = '';
                $("addressoftrucker").value = '';
                $("truckerCity").value = '';
                $("truckerState").value = '';
                $("truckerZip").value = '';
                $("truckerPhone").value = '';
                $("truckerEmail").value = '';
                $("truckerClientReference").value = '';
            }
        }
        );
    }
}
function uncheckClearConsignee() {
    if (document.getElementById('consigneeTpCheck').checked) {
        onConsigneeBlur();
        Event.stopObserving("consigneeName", "blur");
    } else {
        document.getElementById("consigneeTpCopy").value = '';
        document.getElementById("consignee").disabled = false;
        onConsigneeBlur();
        Event.observe("consigneeName", "blur", function (event) {
            var element = Event.element(event);
            if (element.value !== $("consigneeName_check").value) {
                element.value = '';
                $("consigneeName_check").value = '';
                $("consigneeName").value = '';
                $("consignee").value = '';
                $("consigneeAddress").value = '';
                $("consigneeCity").value = '';
                $("consigneeState").value = '';
                $("consigneeZip").value = '';
                $("consigneeCountry").value = '';
                $("consigneePhone").value = '';
                $("consigneeFax").value = '';
                $("consigneeEmail").value = '';
                $("consigneeClientReference").value = '';
            }
        }
        );
    }
}
function clearConsigneeDetails() {
    if (document.getElementById('consigneeTpCheck').checked) {
        onConsigneeBlur();
        Event.stopObserving("consigneeName", "blur");
    } else {
        onConsigneeBlur();
        Event.observe("consigneeName", "blur", function (event) {
            var element = Event.element(event);
            if (element.value !== $("consigneeName_check").value) {
                element.value = '';
                $("consigneeName_check").value = '';
                $("consigneeName").value = '';
                $("consignee").value = '';
                $("consigneeAddress").value = '';
                $("consigneeCity").value = '';
                $("consigneeState").value = '';
                $("consigneeZip").value = '';
                $("consigneeCountry").value = '';
                $("consigneePhone").value = '';
                $("consigneeFax").value = '';
                $("consigneeEmail").value = '';
                $("consigneeClientReference").value = '';
            }
        }
        );
    }
}
function disableImages() {
    var imgs = document.getElementsByTagName("img");
    for (var k = 0; k < imgs.length; k++) {
        imgs[k].style.visibility = "hidden";
    }
    // don't hide Adjustment Remarks image on view mode
    jQuery(".adjustmentRemarks").each(function () {
        jQuery(this).css("visibility", "visible");
    });
}
function copyQuoteContactName() {
    if (document.getElementById("contactNameCheck").checked) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "getQuoteContactName",
                param1: newfileNumber
            },
            success: function (data) {
                if (data !== null) {
                    document.EditBookingsForm.SSLineBookingRep.value = data;
                }
            }
        });
    } else {
        document.EditBookingsForm.SSLineBookingRep.value = "";
    }
}
function setFocusFromDojo(focusTo) {
    if (document.getElementById(focusTo)) {
        document.getElementById(focusTo).focus();
    }
}
function allowOnlyWholeNumbers(obj) {
    if (obj.value.match(/^-?[0-9]+(.[0-9]{1,2})?$/) === null) {
        obj.value = '';
        alertNew('The format is wrong');
        return false;
    }
}
function allowNagWholeNumbers(obj) {
    var result;
    if (!/^[1-9 . ]\d*$/.test(obj.value)) {
        result = obj.value.replace(/[^-\0-9 . ]+/g, '');
        obj.value = result;
        return false;
    }
    return true;
}

function copyNotListedTp(from, to) {
    document.getElementById(to).value = from.value;
}
function setFocusToTab(selectedTab) {
    jQuery(function () {
        var $tabs = jQuery('#bookingContainer').tabs();
        if (selectedTab === 'rates') {
            $tabs.tabs('select', 4);
        } else if (selectedTab === 'specialProvisions') {
            $tabs.tabs('select', 3);
        } else if (selectedTab === 'equipment') {
            $tabs.tabs('select', 2);
        } else if (selectedTab === 'shipperForwarder') {
            $tabs.tabs('select', 1);
        } else {
            $tabs.tabs('select', 0);
        }
    });
}
function getDocCharge() {
    GB_show("Document Charge Rate", "/logisoft/jsps/fclQuotes/calculateDocCharge.jsp", width = "200", height = "300");
}
function calculateDocumentCharge(amount) {
    document.EditBookingsForm.docChargeAmount.value = amount;
    document.EditBookingsForm.buttonValue.value = "addDocumentCharge";
    document.EditBookingsForm.submit();
}
function deleteDocCharge() {
    confirmNew("Do you want to delete Document Charges?", "docCharge");
}
function makeInbondButtonGreen() {
    var bookingNo = document.EditBookingsForm.bookingId.value;
    if (document.getElementById("inbondButton")) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "getInbondForThisBooking",
                param1: bookingNo
            },
            success: function (data) {
                if (data === "INBOND") {
                    document.getElementById("inbondButton").className = "buttonColor";
                    document.getElementById("inbondButtonDown").className = "buttonColor";
                } else {
                    document.getElementById("inbondButton").className = "buttonStyleNew";
                    document.getElementById("inbondButtonDown").className = "buttonStyleNew";
                }
            }
        });
    }
}
function getBillTO(view, blFlag) {
    var code = "";
    var dest = document.getElementById("portOfDischarge").value;
    if (dest.lastIndexOf("(") !== -1) {
        code = dest.substring(dest.lastIndexOf("(") + 1, dest.lastIndexOf(")"));
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "getDestCodeforHBL",
            param1: code
        },
        async: false,
        success: function (data) {
            if (data !== null && data !== '') {
                if (data === 'N') {//pripaid
                    document.EditBookingsForm.prepaidToCollect[0].checked = true;
                    document.EditBookingsForm.prepaidToCollect[0].disabled = false;
                    document.EditBookingsForm.prepaidToCollect[1].checked = false;
                    document.EditBookingsForm.prepaidToCollect[1].disabled = true;
                    document.EditBookingsForm.billToCode[0].disabled = false;
                    //document.EditBookingsForm.billToCode[0].checked = true;
                    document.EditBookingsForm.billToCode[1].disabled = false;
                    document.EditBookingsForm.billToCode[2].disabled = false;
                    document.EditBookingsForm.billToCode[3].disabled = false;
                    document.EditBookingsForm.billToCode[3].checked = false;
                    document.EditBookingsForm.billToCode[3].disabled = true;
                    if ((document.EditBookingsForm.prepaidToCollect[0].checked === true) && (document.EditBookingsForm.billToCode[0].checked === false
                        && document.EditBookingsForm.billToCode[1].checked === false && document.EditBookingsForm.billToCode[2].checked === false
                        && document.EditBookingsForm.billToCode[3].checked === false)) {
                        document.EditBookingsForm.billToCode[0].checked = true;
                    }
                    if (document.getElementById("bookingCompleteY").checked === true) {
                        makePageReadOnlyOnBookingComplete(document.getElementById("editbook"), view, blFlag);
                        document.EditBookingsForm.prepaidToCollect[0].disabled = true;
                        document.EditBookingsForm.prepaidToCollect[1].disabled = true;
                        document.EditBookingsForm.billToCode[0].disabled = true;
                        document.EditBookingsForm.billToCode[1].disabled = true;
                        document.EditBookingsForm.billToCode[2].disabled = true;
                        document.EditBookingsForm.billToCode[3].disabled = true;
                    }

                //disableBillToCode('radio');
                } else if (data === 'Y') {
                    document.EditBookingsForm.billToCode[0].disabled = false;
                    document.EditBookingsForm.billToCode[1].disabled = false;
                    document.EditBookingsForm.billToCode[2].disabled = false;
                    document.EditBookingsForm.billToCode[3].disabled = true;
                    if (document.getElementById("bookingCompleteY").checked === true) {
                        makePageReadOnlyOnBookingComplete(document.getElementById("editbook"), view, blFlag);
                        document.EditBookingsForm.prepaidToCollect[0].disabled = true;
                        document.EditBookingsForm.prepaidToCollect[1].disabled = true;
                        document.EditBookingsForm.billToCode[0].disabled = true;
                        document.EditBookingsForm.billToCode[1].disabled = true;
                        document.EditBookingsForm.billToCode[2].disabled = true;
                        document.EditBookingsForm.billToCode[3].disabled = true;
                    }
                }
            }
        }
    });
}
function setAlternateAgent(importFlag, companyCode) {
    if (importFlag === 'true') {
        if (companyCode === '02') {
            document.getElementById("alternateAgentN").checked = true;
        } else {
            document.getElementById("alternateAgentY").checked = true;
            document.getElementById("directConsignmentN").checked = true;
            document.getElementById("agent").className = "textlabelsBoldForTextBox";
            document.getElementById("routedAgentCheck").className = "dropdown_accounting";
            document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
            document.getElementById("agent").readOnly=false;
            document.getElementById("routedByAgent").readOnly=false;
            document.getElementById("routedAgentCheck").readOnly=false;
            jQuery("#routedAgentCheck").attr("disabled", false);
           }
    }
}
function defaultAgentforcons() {
    if (document.getElementById("alternateAgentY").checked) {
        fillDefaultAgent(importFlag);
        setFocusFromDojo('portOfOrigin');
    }
    insuranceAllowedForBooking();
}
function bkgCarrier() {
    var ssldesc = document.getElementById("sslDescription").value;
    var sslnumber = document.getElementById("steamShipNumber").value;
    document.getElementById("sslDescription").value = ssldesc + "//" + sslnumber;
}
function addOrUpdateSpecialEquipment(bookingId, action) {
    if (document.EditBookingsForm.specialequipment[1].checked === true) {
        alertNew("Please Check Special Equipment");
        return;
    } else if (document.EditBookingsForm.specialEqpmtSelectBox.value === '') {
        alertNew("Please Select Special Equipment");
        return;
    } else if (document.EditBookingsForm.specialEqpmtUnit.value === '') {
        alertNew("Please Select Unit Type");
        return;
    } else {
        document.getElementById("addEquipment").disabled = true;
        document.getElementById("updateEquipment").disabled = true;
        if (action === 'add') {
            document.EditBookingsForm.buttonValue.value = "addSpecialEquipment";
        } else {
            document.EditBookingsForm.buttonValue.value = "updateSpecialEquipment";
        }
        document.EditBookingsForm.submit();
    }
}
function deleteSpecialEquipmentUnit(val, standardChargeIndex) {
    document.EditBookingsForm.numbIdx.value = val;
    document.EditBookingsForm.standardChargeIndex.value = standardChargeIndex;
    confirmNew("Are you sure? You want to delete this Charge", "deleteSpecialEquipmentUnit");
}
function enableSpecialEquipment() {
    document.EditBookingsForm.specialEqpmtUnit.disabled = false;
    if (document.EditBookingsForm.specialEqpmtSelectBox) {
        document.EditBookingsForm.specialEqpmtSelectBox.disabled = false;
    }
    if (document.getElementById("specialEqpmtSelectBox_readonly")) {
        document.getElementById("specialEqpmtSelectBox_readonly").disabled = false;
        if (document.getElementById("specialEqpmtSelectBox_readonly").type !== "select-one") {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "getUnitListForFCLTest1",
                    dataType: "json"
                },
                success: function (data) {
                    var textbox = jQuery('#specialEqpmtSelectBox');
                    var selectbox = jQuery("<select></select>");
                    selectbox.attr("id", textbox.attr("id"));
                    selectbox.attr("name", textbox.attr("name"));
                    jQuery.each(data, function (index, item) {
                        selectbox.append("<option value='" + item.value + "'>" + item.label + "</option>");
                    });
                    selectbox.val(textbox.val());
                    selectbox.width("125");
                    selectbox.addClass("dropdown_accounting");
                    jQuery("#specialEqpmtSelectBox_readonly").remove();
                    textbox.replaceWith(selectbox);
                }
            });
        }
    }
}
function checkGaugeComment(mode, unit, obj) {
    if (obj.value === 'Y') {
        document.getElementById(mode + unit).style.visibility = "visible";
    } else {
        document.getElementById(mode + unit).style.visibility = "hidden";
    }

}
function submitOutOfGuageComments() {
    var comment = document.getElementById("outOfGaugeComment").value;
    document.EditBookingsForm.outOfGuageComment.value = comment;
    document.EditBookingsForm.buttonValue.value = "addSpecialEquipmentComment";
    document.EditBookingsForm.submit();

}
function openOutOfGuageComments(val, index, comment) {
    document.EditBookingsForm.numbIdx.value = val;
    document.EditBookingsForm.standardChargeIndex.value = index;

    showPopUp();
    var browserName = navigator.appName;
    window.scrollTo(0, 0);
    window.scrollTo(0, 0);
    var isIE5Min = getIEVersionNumber() <= 8;
    if (isIE5Min) {
        document.getElementById("outOfGaugeCommentDiv").style.position = 'absolute';
        document.getElementById("outOfGaugeCommentDiv").style.left = '40%';
        document.getElementById("outOfGaugeCommentDiv").style.top = '30px';
    } else {
        document.getElementById("outOfGaugeCommentDiv").style.position = 'fixed';
        document.getElementById("outOfGaugeCommentDiv").style.left = '40%';
        document.getElementById("outOfGaugeCommentDiv").style.top = '30px';
    }
    document.getElementById("outOfGaugeCommentDiv").style.display = 'block';
    //    var IpopTop = (screen.height - document.getElementById("outOfGaugeCommentDiv").offsetHeight)/2;
    //    var IpopLeft = (screen.width - document.getElementById("outOfGaugeCommentDiv").offsetWidth)/2;
    //    document.getElementById("outOfGaugeCommentDiv").style.left=IpopLeft + document.body.scrollLeft-70;
    //    document.getElementById("outOfGaugeCommentDiv").style.top=IpopTop + document.body.scrollTop-150;
    document.getElementById("outOfGaugeComment").value = comment.replace(/<br>/g, "\n");
}


function closeCommentDiv() {
    closePopUp();
    document.getElementById("outOfGaugeCommentDiv").style.display = 'none';
}
function watchTextarea(id) {
    document.getElementById(id).onkeyup();
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
function carrierStyle() {
    if (document.EditBookingsForm.ratesNonRates[0] && document.EditBookingsForm.ratesNonRates[0].checked) {
        if (document.getElementById('carrierContact')) {
            document.getElementById('carrierContact').style.visibility = "visible";
        }
    } else {
        if (document.getElementById('carrierContact')) {
            document.getElementById('carrierContact').style.visibility = "hidden";
        }
    }
}
function ratesFocusForBooking() {
    if (document.EditBookingsForm.moveType) {
        document.EditBookingsForm.moveType.focus();
    }
}
function confirmConsigneeBox(text) {
    confirmYesOrNo(text, "consignee");
}
function confirmShipperBox(text) {
    confirmYesOrNo(text, "shipper");
}
function confirmSpottAddrBox(text) {
    confirmYesOrNo(text, "spottAddr");
}
function confirmTruckerBox(text) {
    confirmYesOrNo(text, "truckerCode");
}
function newConfirmMessageFunction(id1, id2) {
    if (id1 === 'consignee' && id2 === 'yes') {
        uncheckClearConsignee();
    } else if (id1 === 'shipper' && id2 === 'yes') {
        uncheckClearShipper();
    } else if (id1 === 'spottAddr' && id2 === 'yes') {
        uncheckClearSpottAddr();
    } else if (id1 === 'truckerCode' && id2 === 'yes') {
        uncheckClearTrucker();
    } else if (id1 === 'consignee' && id2 === 'no') {
        document.getElementById('consigneeTpCheck').checked = true;
    } else if (id1 === 'shipper' && id2 === 'no') {
        document.getElementById('shipperTpCheck').checked = true;
    } else if (id1 === 'spottAddr' && id2 === 'no') {
        document.getElementById('spottAddrTpCheck').checked = true;
    } else if (id1 === 'truckerCode' && id2 === 'no') {
        document.getElementById('truckerTpCheck').checked = true;
    } else if (id1 === 'onCarriage' && id2 === 'no') {
        onCarriageOverride();
    } else if (id1 === 'onCarriage' && id2 === 'yes') {
        openRates();
    } else if (id1 === 'hazCharge' && id2 === 'no') {
    //-- Do nothing
    } else if (id1 === 'hazCharge' && id2 === 'yes') {
        validateConvertToBl();
    } else if (id1 === "acceptVid" && id2 === "yes") {
        document.EditBookingsForm.spotRate.value = "Y";
        document.EditBookingsForm.buttonValue.value = "acceptVidOnSpotRate";
        document.EditBookingsForm.submit();
    } else if (id1 === "acceptVid" && id2 === "no") {
        jQuery("#spotRateN").attr('checked', 'checked');
    }
       else if (id1 == "Econo/Ecu Worldwide" && id2 == "yes") {

        document.EditBookingsForm.brand.value = "Econo";
        document.EditBookingsForm.buttonValue.value = "addBrandValue";
        document.EditBookingsForm.submit();
    } else if (id1 == "OTI/Ecu Worldwide" && id2 == "yes") {

        document.EditBookingsForm.brand.value = "OTI";
        document.EditBookingsForm.buttonValue.value = "addBrandValue";
        document.EditBookingsForm.submit();
    }
    else if (id1 === "Ecu Worldwide/Econo" && id2 == "yes") {
        document.EditBookingsForm.brand.value = "Ecu Worldwide";
        document.EditBookingsForm.buttonValue.value = "addBrandValue";
        document.EditBookingsForm.submit();
    } else if (id1 === "Ecu Worldwide/OTI" && id2 === "yes"){
        document.EditBookingsForm.brand.value = "Ecu Worldwide";
        document.EditBookingsForm.buttonValue.value = "addBrandValue";
        document.EditBookingsForm.submit();
    } 
    else if (id1 === "Econo/Ecu Worldwide" && id2 == "no") {
        var data = "Econo/Ecu Worldwide";
        var splitarray = data.split("/");
        var splitarray1 = splitarray[1];
        
        if (splitarray1 === "Ecu Worldwide") {
            document.getElementById('brandEcono').checked = false;
            document.getElementById('brandEcuworldwide').checked = true;
        }
    } else if (id1 === "OTI/Ecu Worldwide" && id2 == "no") {
        var data = "OTI/Ecu Worldwide";
        var splitarray = data.split("/");
        var splitarray1 = splitarray[1];
       
        if (splitarray1 === "Ecu Worldwide") {
            document.getElementById('brandOti').checked = false;
            document.getElementById('brandEcuworldwide').checked = true;
        }
    } else if (id1 === "Ecu Worldwide/Econo" && id2 == "no") {
        var data = "Ecu Worldwide/Econo";
        var splitarray = data.split("/");
        var splitarray1 = splitarray[1];
        
        if (splitarray1 === "Econo") {
            document.getElementById('brandEcono').checked = true;
            document.getElementById('brandEcuworldwide').checked = false;
        }
    }
    else if (id1 === "Ecu Worldwide/OTI" && id2 == "no") {
        var data = "Ecu Worldwide/OTI";
        var splitarray = data.split("/");
        var splitarray1 = splitarray[1];
       
        if (splitarray1 === "OTI") {
            document.getElementById('brandOti').checked = true;
            document.getElementById('brandEcuworldwide').checked = false;
        }
    } else if(id1 === "saveCFCLLinkedDrs" && id2 === "yes") {
        var bookingId =jQuery("#bkgId").val();
        var fileNo = jQuery("#fileNumber").val();
        viewCFCLLinkedDrs("saveCFCLLinkedDrs",bookingId,fileNo);  
    } else if(id1 === "saveCFCLLinkedDrs" && id2 === "no") {
        jQuery('#vaoyageInternational').val('');
    } else if(id1 === "removedOldCFCLVoyageDetails" && id2 === "yes") {
      removedOldCFCLVoyageDetails();
    } else if(id1 === "deleteChassisCharge" && id2 === "yes"){
        document.EditBookingsForm.buttonValue.value = "deleteChassisCharge";
        document.EditBookingsForm.submit();
    }
    else if(id1 === "deleteChassisCharge" && id2 === "no"){
      document.getElementById('chassisChargeN').checked = true;
      
    }
}
function setPOAYESNO() {
    setPOA("shipper");
    setPOA("forwarder");
    setPOA("consignee");
}
function hideArInvoice(fileNo) {
    var voyageInternal = jQuery("#vaoyageInternational").val() !==null && jQuery("#vaoyageInternational").val() !== undefined ? jQuery("#vaoyageInternational").val() : "";
    GB_hide();
    makeARInvoiceButtonGreen(fileNo,voyageInternal);

}
function makeARInvoiceButtonGreen(fileNo,voyageInternal) {
    if (null !== fileNo && fileNo !== "" && document.getElementById("arRedInvoice")) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getARInvoiceForThisBL",
                param1: fileNo,
                param2: voyageInternal
            },
            success: function (data) {
                if (data === "ARINVOICE") {
                    document.getElementById("arRedInvoice").className = "buttonColor";
                    document.getElementById("arRedInvoiceDown").className = "buttonColor";
                } else {
                    document.getElementById("arRedInvoice").className = "buttonStyleNew";
                    document.getElementById("arRedInvoiceDown").className = "buttonStyleNew";
                }
            }
        });
    }
}
function getEmptyPickUp(bookingId, hazmat, fileNo, emptyPickUp) {
    GB_show('IMS Quote', rootPath + '/rateGrid.do?action=ImsQuote&originZip=' + document.EditBookingsForm.zip.value +
        '&doorOrigin=' + document.EditBookingsForm.doorOrigin.value + '&hazardous=' + hazmat + '&quoteId=' + bookingId + '&screenName=booking&fileNo=' + fileNo + '&emptyPickUp=' + emptyPickUp, 350, 800);
}
function resendToBlueScreen(fileNo) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "resendToBlueScreen",
            param1: fileNo,
            request: "true"
        },
        success: function (data) {
            alertNew("Booking resend to Blue Screen Submitted Succesfully");
        }
    });
}
function openContactInfo(party) {
    var customerName = "";
    var customer = "";
    if (party === 'S') {
        customerName = document.getElementById("shipperName").value;
        customer = document.getElementById("shipper").value;
    } else if (party === 'F') {
        customerName = document.getElementById("fowardername").value;
        customer = document.getElementById("forwarder").value;
    } else if (party === 'C') {
        customerName = document.getElementById("consigneename").value;
        customer = document.getElementById("consignee").value;
    } else if (party === 'trucker') {
        customerName = document.getElementById("truckerName").value;
        customer = document.getElementById("truckerCode").value;
    }
    customerName = customerName.replace("&", "amp;");
    GB_show("Contact Info", "/logisoft/customerAddress.do?buttonValue=Quotation&custNo=" + customer + "&custName=" + customerName, width = "400", height = "1100");
}

function getTypeofMoveList(bookingId) {
    if (document.getElementById("rampCheck").checked) {
        document.getElementById("inlandVal").innerHTML = "Intermodal Ramp";
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getrampNvoMoveList",
                dataType: "json"
            },
            success: function (data) {
                jQuery("#moveType").empty();
                jQuery.each(data, function (index, item) {
                    jQuery("#moveType").append("<option value='" + item.value + "'>" + item.label + "</option>");
                });
                jQuery("#rampCheck").val("on");
                selectNVOmove();
            }
        });
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "intrmodRampCheck",
                param1: bookingId
            },
            success: function (data) {
                if (data === 'Y') {
                    document.EditBookingsForm.inland[1].checked = false;
                    document.EditBookingsForm.inland[0].checked = true;
                } else {
                    document.EditBookingsForm.inland[0].checked = false;
                    document.EditBookingsForm.inland[1].checked = true;
                }
            }
        });
    } else {
        document.getElementById("inlandVal").innerHTML = "Inland";
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getNvoMoveList",
                dataType: "json"
            },
            success: function (data) {
                jQuery("#moveType").empty();
                jQuery.each(data, function (index, item) {
                    jQuery("#moveType").append("<option value='" + item.value + "'>" + item.label + "</option>");
                });
                jQuery("#rampCheck").val("off");
                selectNVOmove();
            }
        });
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "inlandCheck",
                param1: bookingId
            },
            success: function (data) {
                if (data === 'Y') {
                    document.EditBookingsForm.inland[1].checked = false;
                    document.EditBookingsForm.inland[0].checked = true;
                } else {
                    document.EditBookingsForm.inland[0].checked = false;
                    document.EditBookingsForm.inland[1].checked = true;
                }
            }
        });
    }
}

function selectNVOmove() {
    var path = "";
    var length = 0;
    var i = 0;
    if (document.getElementById("zip").value !== "" && document.getElementById("rampCheck").checked) {
        length = document.getElementById("moveType").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("moveType").options[i].text === "RAMP TO PORT") {
                document.getElementById("moveType").selectedIndex = i;
            }
        }
    } else if (document.getElementById("doorDestination").value !== "") {
        length = document.getElementById("moveType").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("moveType").options[i].text === "DOOR TO DOOR") {
                document.getElementById("moveType").selectedIndex = i;
            }
        }

    } else if (document.getElementById("doorDestination").value === "" && document.getElementById("zip").value !== "") {
        length = document.getElementById("moveType").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("moveType").options[i].text === "DOOR TO PORT") {
                document.getElementById("moveType").selectedIndex = i;
            }
        }
    }
}
function checkVesselName() {
    if (document.getElementById('vessel_check')) {
        var vesselCheck = document.getElementById('vessel_check').checked;
    }
    if (vesselCheck === false) {
        if (document.getElementById('vesselname_checkn')) {
            document.getElementById('vesselname_checkn').style.position = "absolute";
            document.getElementById('vesselname_checkn').style.visibility = "hidden";
            document.getElementById('vesselname_checkn').value = "";
        }
        if (document.getElementById('vessel')) {
            document.getElementById('vessel').style.position = "relative";
            document.getElementById('vessel').style.visibility = "visible";
        }
    } else {
        if (document.getElementById('vessel')) {
            document.getElementById('vessel').style.position = "absolute";
            document.getElementById('vessel').style.visibility = "hidden";
            document.getElementById('vessel').value = "";
        }
        if (document.getElementById('vesselname_checkn')) {
            document.getElementById('vesselname_checkn').style.position = "relative";
            document.getElementById('vesselname_checkn').style.visibility = "visible";
        }
    }
}
function editCollapseCharges(id, fileNumber, bookingNumber, sellRate, costRate, isCollapsed) {
    isCollapsed = isCollapsed === '' ? "true" : isCollapsed;
    GB_show('Edit Booking Charges', '/logisoft/bookingCharges.do?buttonValue=edit&id=' + id + '&fileNo=' +
        fileNumber + '&bookingId=' + bookingNumber + '&isCollapsed=' + isCollapsed + '&sellRate=' +
        sellRate + '&costRate=' + costRate + '&msg=<%=msg%>', 500, 1070);
}

function modifyAccountDetails() {
    var result = "";
    var autoCreditStatus = "";
    var billToCode = jQuery("input[name=billToCode]:checked").val();
    var customerNumber = ("F" === billToCode) ? jQuery("#forwarder").val() : ("S" === billToCode) ? jQuery("#shipper").val() : ("T" === billToCode) ? jQuery("#accountNumber1").val() : ("C" === billToCode) ? jQuery("#consignee").val() : jQuery("#agentNo").val();
    if (customerNumber !== '') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "validateCustomer",
                param1: customerNumber,
                param2: importFlag === "false" ? "E" : "I"
            },
            async: false,
            success: function (data) {
                if (data !== null && data !== '') {
                    var chargecode = data.split("===");
                    var autosHold = data.split("===");
                    var crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                    var autoCredit = autosHold[1].substring();
                    if (crditWarning === "In Good Standing ") {
                        result = "CREDIT: " + crditWarning;
                        document.getElementById('warningMessage').className = "creditStyle";
                    } else {
                        result = "WARNING: " + crditWarning;
                        document.getElementById('warningMessage').className = "warningStyle";
                    }
                    if (autoCredit.indexOf("HHG/PE/AUTOS CREDIT") > -1) {
                        autoCreditStatus = "<br>HHG/PE/AUTOS CREDIT<br>";
                        document.getElementById('autosCredit').className = "creditStyle";
                    } else {
                        autoCreditStatus = "<br>NO CREDIT FOR HHG/PE/AUTOS<br>";
                        document.getElementById('autosCredit').className = "warningStyle";
                    }
                }
            }
        });
    }
    if (result !== "") {
        document.getElementById('warningMessage').innerHTML = result;
    }
    if (autoCreditStatus !== "") {
        document.getElementById('autosCredit').innerHTML = autoCreditStatus;
    }
}

function inlandLabel(importFlag) {
    if (document.getElementById("rampCheck").checked) {
        document.getElementById("inlandVal").innerHTML = "Intermodal Ramp";
    } else if("true" === importFlag) {
        document.getElementById("inlandVal").innerHTML = "Delivery";
    }
    else{
         document.getElementById("inlandVal").innerHTML = "Inland";
    }
}
function hidebkgvesselvoy(fileNumber, blFlag) {
    var element;
    var blflag = blFlag;
    if (blflag !== null && blflag === "on") {
        var fileNO = fileNumber;
        var tab = window.parent.$("#tab" + 3).attr("title");
        if (undefined !== tab && tab === 'FCL BL') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "getBlManifestFlag",
                    param1: fileNO
                },
                async: false,
                success: function (data) {
                    var length = document.EditBookingsForm.elements.length;
                    if (data !== null && data !== '' && (data === 'M' || data === 'm')) {
                        for (var i = 0; i < length; i++) {
                            element = document.EditBookingsForm.elements[i];
                            if (element.type === "text" || element.type === "textarea") {
                                if (element.type === "text" && (element.id === "vessel" || element.id === "ssVoy")) {
                                    element.style.backgroundColor = "#CCEBFF";
                                    element.readOnly = true;
                                    element.tabIndex = -1;
                                    element.style.border = 0;
                                    element.className = "textlabelsBoldForTextBox";
                                    if (element.id === "vessel" || element.id === "ssVoy") {
                                        element.style.borderLeft = "red 2px solid";
                                    }
                                }
                            }
                        }
                    } else {
                        for (i = 0; i < length; i++) {
                            element = document.EditBookingsForm.elements[i];
                            if (element.type === "text" || element.type === "textarea") {
                                if (element.type === "text" && (element.id === "vessel" || element.id === "ssVoy")) {
                                    element.style.border = "1px solid #C4C5C4";
                                    element.style.backgroundColor = "#FCFCFC";
                                    element.readOnly = false;
                                    element.tabIndex = 0;
                                    if (element.id === "vessel" || element.id === "ssVoy") {
                                        element.style.borderLeft = "red 2px solid";
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
function defaultPOA(shipperPoa, forwarderPoa, consigneePoa) {
    var shipperPoaVal = shipperPoa;
    var forwarderPoaVal = forwarderPoa;
    var consigneePoaVal = consigneePoa;
    if (shipperPoaVal === "Yes") {
        document.getElementById("shipperPoaid").className = "poaGreen";
    } else if (shipperPoaVal === "No") {
        document.getElementById("shipperPoaid").className = "poaRed";
    }
    if (forwarderPoaVal === "Yes") {
        document.getElementById("forwarderPoaid").className = "poaGreen";
    } else if (forwarderPoaVal === "No") {
        document.getElementById("forwarderPoaid").className = "poaRed";
    }
    if (consigneePoaVal === "Yes") {
        document.getElementById("ConsigneePoaid").className = "poaRed";
    } else if (consigneePoaVal === "No") {
        document.getElementById("ConsigneePoaid").className = "poaRed";
    }
}

function getPopHazmat(fileNumber) {
    var fileNo = fileNumber;
    if (document.EditBookingsForm.bookingId.value === '') {
        alertNew('Please Save Booking Before create Manually charges ');
        return;
    }
    if (document.EditBookingsForm.hazmat[0].checked) {
        GB_show('Hazmat', '/logisoft/fCLHazMat.do?buttonValue=Booking&name=Booking&number=' + document.EditBookingsForm.bookingId.value + '&fileNo=' + fileNo,
            width = "500", height = "1200");
    } else {
        alertNew("Please Select Hazmat");
        return;
    }
}

function setVessName(blFlag, fileNumber) {
    var blflag = blFlag;
    if (blflag !== null && blflag === "on") {
        var vessel = "";
        var fileNO = fileNumber;
        vessel = document.getElementById("vessel").value;
        var frameRef = window.parent.document.frames["tab" + 3];
        var tab = window.parent.$("#tab" + 3).attr("title");
        if (undefined !== tab && tab === 'FCL BL') {
            frameRef.document.fclBillLaddingform.vesselname.value = vessel;
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "setVessName",
                    param1: vessel,
                    param2: fileNO,
                    request: "true"
                },
                async: false,
                success: function (data) {
                    if (data !== null && data !== '') {
                        frameRef.document.fclBillLaddingform.vessel.value = data;
                    }
                }
            });
        }
    }
}

function setSSVoyage(blFlag, fileNumber) {
    var blflag = blFlag;
    if (blflag !== null && blflag === "on") {
        var ssvoyage = "";
        var fileNO = fileNumber;
        ssvoyage = document.getElementById("ssVoy").value;
        var frameRef = window.parent.document.frames["tab" + 3];
        var tab = window.parent.$("#tab" + 3).attr("title");
        if (undefined !== tab && tab === 'FCL BL') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "setVoyValue",
                    param1: ssvoyage,
                    param2: fileNO
                },
                async: false,
                success: function (data) {
                    frameRef.document.fclBillLaddingform.voyage.value = ssvoyage;
                }
            });
        }
        document.getElementById("txtcal313").focus();
    }
}
function addingAccrualsInBooking(importFlag) {
    GB_show('Add Accruals', '/logisoft/jsp/fcl/addFclBookingCost.jsp?bookingId=' + document.EditBookingsForm.bookingId.value + '&importFlag=' + importFlag +'&fileNo=' +document.EditBookingsForm.fileNo.value, 300, 1000);
}
function showBookingAccruals() {
    if (document.getElementById("showBookingAccrual").style.display === "none") {
        document.getElementById("showBookingAccrual").style.display = "block";
        document.getElementById("previewBookingAccruals").value = "Close Accruals";
    } else {
        document.getElementById("showBookingAccrual").style.display = "none";
        document.getElementById("previewBookingAccruals").value = "Preview Accruals";
    }
}
function editBookingAccruals(costId) {
    GB_show('Edit Accruals', '/logisoft/editBooks.do?button=editBookingAccruals&costId=' + costId, 300, 1000);
}
function getUpdatedBookingChargesDetails(bookingId) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "showFclBookingCharges",
            forward: "/jsp/fcl/accrualsResult.jsp",
            param1: bookingId
        },
        success: function (data) {
            if (data) {
                document.getElementById("previewBookingAccruals").style.display = "block";
                document.getElementById("previewBookingAccruals").value = "Close Accruals";
                document.getElementById("showBookingAccrual").style.display = "block";
                jQuery("#showBookingAccrual").html(data);
            }
        }
    });
}
var customerResult = '';
function validateCustomers() {
    var customerNumber;
    if (document.EditBookingsForm.billToCode[0].checked) {
        customerNumber = document.EditBookingsForm.forwarder.value;//forwardingAgent1
    } else if (document.EditBookingsForm.billToCode[1].checked) {
        customerNumber = document.EditBookingsForm.shipper.value;//shipper
    } else if (document.EditBookingsForm.billToCode[2].checked) {
        customerNumber = document.EditBookingsForm.accountNumber.value;//billTrePty
    } else if (document.EditBookingsForm.billToCode[3].checked) {
        customerNumber = document.EditBookingsForm.agentNo.value;//agentNo
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "validateCustomer",
            param1: customerNumber,
            param2: importFlag === "false" ? "E" : "I"
        },
        async: false,
        success: function (data) {
            if (data !== null && data !== '') {
                var chargecode = data.split("===");
                customerResult = chargecode[0];
                customerResult += "<br>" + chargecode[1];
            }
        }
    });
}
function dontAddInsureToCharges(val1, val2, val3, val4, val5, val6, val7, val8) {
    document.EditBookingsForm.unitSelect.value = val1;
    document.EditBookingsForm.number.value = val2;
    document.EditBookingsForm.chargeCode.value = val3;
    document.EditBookingsForm.chargeCodeDesc.value = val4;
    document.EditBookingsForm.costSelect.value = val5;
    document.EditBookingsForm.currency1.value = val6;
    document.EditBookingsForm.chargeAmt.value = val7;
    document.EditBookingsForm.minimumAmt.value = val8;
    document.EditBookingsForm.buttonValue.value = "addChargesWithoutInsure";
    document.EditBookingsForm.submit();
}
function openCustomerNotesInfo(customerNo, customerName) {
    GB_show("Notes", "/logisoft/bluescreenCustomerNotes.do?methodName=displayNotes&customerNo=" + customerNo + "&customerName=" + customerName, 400, 1000);
}
function isCustomerNotes(id, acctNo) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkCustomerNotes",
            param1: acctNo,
            dataType: "json"
        },
        success: function (data) {
            if (data) {
                jQuery('#' + id).attr("src", rootPath + "/img/icons/e_contents_view1.gif");
            } else {
                jQuery('#' + id).attr("src", rootPath + "/img/icons/e_contents_view.gif");
            }
        }
    });
}
function okForReminderBook() {
    document.getElementById("reminderBox").style.display = "none";
    grayOut(false, "");
    validateConvertToBl();
}
function okForCopyBooking() {
    document.getElementById("confirmCopyBox").style.display = "none";
    document.EditBookingsForm.buttonValue.value = "copyBooking";
    document.EditBookingsForm.submit();
}
function disableBookingContact(thisVal) {
    if (thisVal.value === "Y") {
        document.getElementById("bookingContact").style.backgroundColor = "#CCEBFF";
        document.getElementById("bookingContact").readOnly = true;
        document.getElementById("bookingContact").tabIndex = -1;
        document.getElementById("bookingContact").className = "textlabelsBoldForTextBox";
    } else {
        document.getElementById("bookingContact").style.backgroundColor = "#FCFCFC";
        document.getElementById("bookingContact").readOnly = false;
        document.getElementById("bookingContact").tabIndex = 0;
        document.getElementById("bookingContact").className = "textlabelsBoldForTextBox";
    }
}
function insuranceAllowedForBooking() {
    var portname = document.getElementById('portOfDischarge').value;
    var unloc = portname.substring(portname.lastIndexOf("(") + 1, portname.lastIndexOf(")"));
    var contains = (insuranceAllowed.indexOf(unloc) !== -1);
    if (portname !== null && portname !== '') {
        if (contains) {
            document.getElementById('y8').disabled = true;
            document.getElementById('n8').checked = true;
            document.getElementById('n8').disabled = true;
        } else {
            document.getElementById('y8').disabled = false;
            document.getElementById('n8').disabled = false;
        }
    }
}
function setNvoOnLoad() {
    if (document.getElementById("moveType").type !== "select-one") {
        optionValue = document.getElementById("moveType").value;
    } else {
        optionValue = document.getElementById("moveType").options[document.getElementById("moveType").selectedIndex].text;
    }
    if (document.getElementById("doorOrigin").value !== "" && document.getElementById("doorDestination").value === "") {
        if (optionValue !== "DOOR TO DOOR" && optionValue !== "DOOR TO RAIL" && optionValue !== "DOOR TO PORT"
            && optionValue !== "RAMP TO DOOR" && optionValue !== "RAMP TO RAIL" && optionValue !== "RAMP TO PORT") {
            length = document.getElementById("moveType").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("moveType").options[i].text === "DOOR TO PORT") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            }
        }
    } else if (document.getElementById("doorOrigin").value === "" && document.getElementById("doorDestination").value !== "") {
        if (optionValue !== "PORT TO DOOR" && optionValue !== "RAIL TO DOOR" && optionValue !== "RAMP TO PORT"
            && optionValue !== "RAMP TO DOOR" && optionValue !== "RAMP TO RAIL" && optionValue !== "RAMP TO PORT") {
            length = document.getElementById("moveType").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("moveType").options[i].text === "PORT TO DOOR") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            }
        }
    } else if (document.getElementById("doorOrigin").value !== "" && document.getElementById("doorDestination").value !== "") {
        if (optionValue !== "DOOR TO DOOR" && optionValue !== "RAMP TO PORT"
            && optionValue !== "RAMP TO DOOR" && optionValue !== "RAMP TO RAIL" && optionValue !== "RAMP TO PORT") {
            length = document.getElementById("moveType").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("moveType").options[i].text === "DOOR TO DOOR") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            }
        }
    } else if (document.getElementById("doorOrigin").value === "" && document.getElementById("doorDestination").value === "") {
        if (optionValue !== "PORT TO PORT" && optionValue !== "RAIL TO PORT" && optionValue !== "PORT TO RAIL"
            && optionValue !== "RAIL TO RAIL" && optionValue !== "RAMP TO PORT"
            && optionValue !== "RAMP TO DOOR" && optionValue !== "RAMP TO RAIL" && optionValue !== "RAMP TO PORT") {
            length = document.getElementById("moveType").length;
            for (var i = 0; i < length; i++) {
                if (document.getElementById("moveType").options[i].text === "PORT TO PORT") {
                    document.getElementById("moveType").selectedIndex = i;
                }
            }
        }
    }
}
function copyBookings() {
    confirmNew("Do you want to copy the Booking ?", "copyBooking");
}

function confirmCancelBkgEdi() {
    confirmNew("Are you sure want to Cancel Bkg EDI Request?", "cancelBkgRequest");
}

function validateDataForEdi(createOrChange) {
    var fileno = jQuery("#moduleRefId").val();
    if (undefined !== fileno && null !== fileno && fileno !== '') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.edi.inttra.Validate300Xml",
                methodName: "validateXml",
                param1: fileno,
                param2: createOrChange,
                request: "true"
            },
            preloading: true,
            success: function (data) {
                if (data) {
                    ediAlert(data);
                }
            }
        });
    } else {
        alertNew("Please save Booking before send EDI");
        closeEdi();
    }
}

function showCancelEdiOnload(cancelEdi, createOrChange) {
    if (cancelEdi === 'true' && createOrChange === "change" && document.EditBookingsForm.bookingComplete[1].checked) {
        showCancelEdi();
    } else {
        closeCancelEdi();
    }
}

function closeEdi() {
    jQuery("#readyToSendEdi").attr('checked', false);
    jQuery("#sendEdi,#sendEdi1").css({
        visibility: "hidden"
    });
}

function showCancelEdi() {
    jQuery("#cancelEdi,#cancelEdi1").css({
        visibility: "visible"
    });
}
function showEdiCreateMsg() {
    jQuery("#ediCreatedByDiv").css({
        display: "block"
    });
    jQuery("#ediCancelByDiv").css({
        display: "none"
    });
}
function showEdiCancelMsg() {
    jQuery("#ediCreatedByDiv").css({
        display: "none"
    });
    jQuery("#ediCancelByDiv").css({
        display: "block"
    });
}

function closeCancelEdi() {
    jQuery("#cancelEdi,#cancelEdi1").css({
        visibility: "hidden"
    });
}

function ediAlert(data) {
    if (null !== data && data !== "") {
        if (data !== 'No Error') {
            alertNew(data);
            closeEdi();
        } else {
            jQuery("#readyToSendEdi").attr('checked', true);
            jQuery("#sendEdi").css({
                visibility: "visible"
            });
            jQuery("#sendEdi1").css({
                visibility: "visible"
            });
        }
    }
}
function submitAmendmentComments() {
    var comments = jQuery("#commentsAboutAmendment").val().trim();
    if (comments !== "") {
        var fileno = jQuery("#moduleRefId").val();
        var userName = jQuery("#userName").val();
        closeAmendmentCommentsPopup();
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "updateAmendmentComments",
                param1: fileno,
                param2: comments,
                param3: userName
            },
            success: function (data) {
                if (null !== data) {
                    closePopUp();
                    closeAmendmentCommentsPopup();
                    generate300Xml("change");
                }
            }
        });
    } else {
        alertNew("Please Enter Reason for Amendment!!!");
    }
}
function closeAmendmentCommentsPopup() {
    closePopUp();
    jQuery('#vessel').prop("disabled", false);
    jQuery('#vessel').css('background-color', '#FFFFFF');
    jQuery('#commentsPopupDivForAmendment').css({
        display: "none"
    });
}
function showAmendmentPopUp() {
    showPopUp();
    jQuery("#commentsAboutAmendment").val("");
    jQuery("#vessel").attr("disabled", "disabled");
    jQuery('#vessel').css('background-color', '#A9A9A9');
    jQuery("#commentsPopupDivForAmendment").css({
        display: "block"
    });
    jQuery("#commentsTitleMessage").text("Reason for Amendment");
    var IpopTop = (screen.height - jQuery("#commentsPopupDivForAmendment").attr('offsetHeight')) / 2;
    var IpopLeft = (screen.width - jQuery("#commentsPopupDivForAmendment").attr('offsetWidth')) / 2;
    jQuery("#commentsPopupDivForAmendment").css({
        'left': IpopLeft + document.body.scrollLeft - 70
    });
    jQuery("#commentsPopupDivForAmendment").css({
        'top': IpopTop + document.body.scrollTop - 150
    });
}
function amendmentOr300Xml(createOrChange) {
    if (createOrChange === "change") {
        showAmendmentPopUp();
    } else if (createOrChange === "create") {
        generate300Xml("create");
    }
}
function generate300Xml(purposeOfXml) {
    var fileno = jQuery("#moduleRefId").val();
    if (undefined !== fileno && null !== fileno && fileno !== '') {
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.Edi300Dwr",
                methodName: "createInttraXml",
                param1: fileno,
                param2: purposeOfXml,
                request: "true"
            },
            preloading: true,
            success: function (data) {
                if (data) {
                    ediData(data);
                }
            }
        });
    } else {
        alertNew("Please save Booking before send EDI Bkg Request");
        closeEdi();
    }
}

function ediData(result) {
    var fileno = jQuery("#moduleRefId").val();
    var userName = jQuery("#userName").val();

    if (null !== result && result === "EDI Booking Request sent Successfully"
        || result === "Amendment EDI Booking Request Sent successfully"
        || result === "Cancel EDI Booking Request Sent successfully") {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "updateEdiStatus",
                param1: fileno,
                param2: userName,
                param3: result
            },
            success: function (data) {
                if (null !== data && data !== "" && (result === "EDI Booking Request sent Successfully"
                    || result === "Amendment EDI Booking Request Sent successfully")) {
                    closeEdi();
                    showCancelEdi();
                    showEdiCreateMsg();
                    document.getElementById("ediby").innerHTML = userName.toUpperCase();
                    document.getElementById("edion").innerHTML = data;
                } else if (null !== data && data !== "") {
                    closeCancelEdi();
                    showEdiCancelMsg();
                    document.getElementById("ediCancelBy").innerHTML = userName.toUpperCase();
                    document.getElementById("ediCancelOn").innerHTML = data;
                }
            }
        });
    }
    alertNew(result);
}

function readyForEdi() {
    if (jQuery("#readyToSendEdi").attr('checked')) {
        jQuery("#bookingComments").val(jQuery("#bookingComments").val().replace(/\t/g, ' '));
        var errors = jQuery.validateXML(jQuery("#bookingComments").val());
        if (jQuery.trim(errors) !== "") {
            alertNew("Please Remove Following Invalid Characters\nin EDI Booking Comments : \n" + errors);
            return false;
        }
        showPopUp();
        jQuery("#readyToSendEdi").attr('checked', false);
        document.EditBookingsForm.action.value = "sendEdi";
        document.EditBookingsForm.buttonValue.value = "update";
        document.EditBookingsForm.submit();
    } else {
        closeEdi();
    }
}

function spotMsgForBook(status, isLoad) {
    if (importFlag == 'false') {
        if (status === "Y") {
            jQuery("#spotRateY").attr('checked', 'checked');
            document.getElementById("spotRateMsgDiv").style.display = "block";
            document.getElementById("spotRateMsgStatus").innerHTML = "**SPOT/BULLET RATE**";
            document.getElementById("spotRateMsgDiv2").style.display = "block";
            document.getElementById("spotRateMsgStatus2").innerHTML = "**SPOT/BULLET RATE**";
        } else {
            jQuery("#spotRateN").attr('checked', 'checked');
            document.getElementById("spotRateMsgDiv").style.display = "none";
            document.getElementById("spotRateMsgStatus").innerHTML = "";
            document.getElementById("spotRateMsgDiv2").style.display = "none";
            document.getElementById("spotRateMsgStatus2").innerHTML = "";
        }
        if (!isLoad) {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "notesForSpotRates",
                    param1: jQuery("#moduleRefId").val(),
                    param2: jQuery("#userName").val(),
                    param3: status
                }
            });
        }

    }
}

function confirmVid(status, isLoad) {
    if (jQuery("#moduleRefId").val() === '') {
        alertNew("Please Save the File to Enable Spot/Bullet Rate Feature");
        jQuery("#spotRateN").attr('checked', 'checked');
        return;
    }
    if (status === 'Y' && !isLoad) {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "isContainDeferral",
                param1: bookingNO.toString(),
                dataType: "json"
            },
            success: function (data) {
                if (data === 'DEFERRAL') {
                    confirmYesOrNo("Did you confirm if VID's were included in the rate you secured from the Steamship Line ?", "acceptVid");
                } else {
                    spotMsgForBook(status, isLoad);
                }
            }
        });

    } else {
        spotMsgForBook(status, isLoad);
    }
}
function setbrandvalueBasedONDestination(companyCode) {
   if (undefined !== document.EditBookingsForm.portOfDischarge.value && null !== document.EditBookingsForm.portOfDischarge.value && '' !== document.EditBookingsForm.portOfDischarge.value) {
   var pod = document.EditBookingsForm.portOfDischarge.value;
        if (pod.lastIndexOf("(") > -1 && pod.lastIndexOf(")") > -1) {
        var podNew = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
        
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "checkBrandForDestination",
                    param1: podNew
                },
                success: function (data) {
                    
                    if (data === "Ecu Worldwide") {
                        document.getElementById('brandEcuworldwide').checked = true;
                        document.getElementById('brandEcono').checked = false;
                    } else if(data === "Econocaribe"){
                        document.getElementById('brandEcono').checked = true;
                        document.getElementById('brandEcuworldwide').checked = false;
                    } else if(data === "OTI"){
                        document.getElementById('brandOti').checked = true;
                        document.getElementById('brandEcuworldwide').checked = false;
                    }
                }
            });

        
        }
    } else{
        
        if (companyCode === '03') {
            document.getElementById('brandEcono').checked = false;
            document.getElementById('brandEcuworldwide').checked = true;
        } else if(companyCode === '02'){
            document.getElementById('brandEcuworldwide').checked = false;
            document.getElementById('brandOti').checked = true;
        }
    }
}
function validateBrandFieldsForShipper(data) {
   
    if (data == "Ecu Worldwide") {
        document.getElementById('brandEcuworldwide').checked = true;
        document.getElementById('brandEcono').checked = false;
    } else if (data == "Econo") {
        document.getElementById('brandEcono').checked = true;
        document.getElementById('brandEcuworldwide').checked = false;
    } else if (data === "OTI") {
        document.getElementById('brandOti').checked = true;
        document.getElementById('brandEcuworldwide').checked = false;
    } else if (data === "None") {
        var companyCode = document.getElementById('companyCode').value;
        setbrandvalueBasedONDestination(companyCode);
    }
}
function addBrandvalueForShippereAccount(account) {
    if (undefined !== account && null !== account && '' !== account) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "checkBrandForClient",
                param1: account
            },
            success: function (data) {
             validateBrandFieldsForShipper(data);
            }
        });

    }
}

function showBrandValuesFromQuote(brand,importFlag,companyCode) {
   if (brand === "Econo") {
        document.getElementById('brandEcono').checked = true;
        document.getElementById('brandEcuworldwide').checked = false;
    } else if (brand === "Ecu Worldwide") {
        document.getElementById('brandEcuworldwide').checked = true;
        document.getElementById('brandEcono').cheked == false
    } else if (brand == "OTI") {
        document.getElementById('brandOti').checked = true;
        document.getElementById('brandEcuworldwide').cheked == false
    } else if (brand === '' && importFlag == 'true' && companyCode === '03') {
        document.getElementById('brandEcuworldwide').checked = true;
        document.getElementById('brandEcono').cheked == false
    } else if (brand === '' && importFlag == 'true' && companyCode === '02') {
        document.getElementById('brandOti').checked = true;
        document.getElementById('brandEcuworldwide').cheked == false
    }
}

function addBrandvalueForForwardAccount(forwardAccnt){

    if (undefined !== forwardAccnt && null !== forwardAccnt && '' !== forwardAccnt) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "checkBrandForClient",
                param1: forwardAccnt
            },
            success: function (data) {
             validateBrandFieldsForwardAcct(data);
            }
        });

    }
    
}
function validateBrandFieldsForwardAcct(data){
    
    if (data === "Ecu Worldwide") {
        
        document.getElementById('brandEcuworldwide').checked = true;
        document.getElementById('brandEcono').checked = false;
    } else if (data === "Econo") {
        document.getElementById('brandEcono').checked = true;
        document.getElementById('brandEcuworldwide').checked = false;
    } else if (data === "OTI") {
        document.getElementById('brandOti').checked = true;
        document.getElementById('brandEcuworldwide').checked = false;
    } else if (data === "None") {
        var accountNo = document.getElementById("shipper").value;
      
        if (accountNo !== undefined  && accountNo !== null  && accountNo !== '') {
            addBrandvalueForShippereAccount(accountNo);
        } else {
            var companyCode = document.getElementById('companyCode').value;
            
            setbrandvalueBasedONDestination(companyCode);
        }
    }
}

function checkBrand(val, bookingId, companyCode) {
    if (val === "Econo" && companyCode === '03') {
        
        checkpreviousBrandValueEco03(bookingId, val);
    } else if (val === "OTI" && companyCode === '02') {
        checkpreviousBrandValueOti02(bookingId, val);
    } else if (val === "Ecu Worldwide" && companyCode === '03') {
        
        checkpreviousBrandValueEcu03(bookingId, val);
    } else if (val === "Ecu Worldwide" && companyCode === '02') {
        checkpreviousBrandValueEcu03(bookingId, val);
    }

}
function checkpreviousBrandValueEcu03(bookingId, val) {

    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "checkBrandForBooking",
            param1: bookingId
        },
        success: function (data) {
            if (null !== data && data !== val) {
              
                confirmYesOrNo("Please note that the Brand is changing from " + data + " to " + val + "", "Ecu Worldwide/"+data);

            }
        }
    });

}
function checkpreviousBrandValueOti02(bookingId, val) {

    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "checkBrandForBooking",
            param1: bookingId
        },
        success: function (data) {
            if (null !== data && data !== val) {
                
                confirmYesOrNo("Please note that the Brand is changing from " + data + " to " + val + "", "OTI/"+data);

            }
        }
    });

}
function checkpreviousBrandValueEco03(bookingId, val) {

    jQuery.ajaxx({
        data: {
             className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "checkBrandForBooking",
            param1: bookingId
        },
        success: function (data) {
            if (null !== data && data !== val) {
                
                confirmYesOrNo("Please note that the Brand is changing from " + data + " to " + val + "", "Econo/"+data);

            }
        }
    });

}
function enableInternalVoy() {
    if(jQuery("#vaoyageInternational").val() === "") {
    enableTextBox("vaoyageInternational");
    }
}
function enableTextBox(id) {
    jQuery('#' + id).removeClass('textlabelsBoldForTextBoxDisabledLook');
    jQuery('#' + id).addClass('textlabelsBoldForTextBox');
    jQuery('#' + id).attr('readonly', false);
    jQuery("#" + id).css("background", "");
}
function disableTextBox(id) {
    jQuery('#' + id).addClass('textlabelsBoldForTextBoxDisabledLook');
    jQuery('#' + id).attr('readonly', true);
}

function getLclArInvoiceStatus(voyageNo) {
    var result ="";
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "checkLclArInvoiceStatus",
            param1: voyageNo,
             dataType: "json"
        },
        async: false,
        success: function (data) {
        result =data;
        }
    });
    return result;
}
function getHazmatStatus(voyageNo) {
    var result ="";
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "getCFCLHazmatStatus",
            param1: voyageNo,
             dataType: "json"
        },
        async: false,
        success: function (data) {
        result =data;
        }
    });
    return result;
}
function getChasisCharge(quoteId) {
  
    GB_show("Chassis Charge Rate", "/logisoft/jsps/fclQuotes/calculateChassisCharge.jsp?vendorName=" + document.EditBookingsForm.vendorAccountName + "&vendorNumber" + document.EditBookingsForm.vendorAccountNo + "&amount=" + document.EditBookingsForm.amount + "&markup=" + document.EditBookingsForm.markup, width = "200", height = "700");
}

function calculateChassisCharge(cost, sell, vendorName, VendorNo) {
   
    document.getElementById("vendorAccountName").value = vendorName;
    document.getElementById("vendorAccountNo").value = VendorNo;
    document.getElementById("amount").value = cost;
    document.getElementById("markUp").value = sell;
 
   document.EditBookingsForm.buttonValue.value = "addChassisCharge";
   document.EditBookingsForm.submit();
    
}
function deleteChassis(){
 confirmYesOrNo("Are you sure you want to remove CHASSIS from this file?", "deleteChassisCharge");   
}
function getBookingDetails(bookingId, voyageNo) {
    var result = "";
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "getBookingDetails",
            param1: bookingId,
            param2: voyageNo,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
}