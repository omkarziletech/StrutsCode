/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    var unknownDest = $('input:radio[name=nonRated]:checked').val();
    if (unknownDest == 'N') {
// setOriginLrd();
    }
//code for assigning search state value in IFC
});

function goBackEculine(path, fileNumberId) {
    showLoading();
    var segFileNumberId = $('#parentFileNumberId').val();
    if (segFileNumberId !== '') {
        fileNumberId = segFileNumberId;
    }
    var url = path + "/lclEculineEdi.do?methodName=openContainer&fileId=" + fileNumberId;
    document.location.href = url;
}

function goBackEciSearch(txt, path, fileNumberId, moduleId, module) {
    var pickupYesNo = $('input:radio[name=pooDoor]:checked').val();
    var agentNumber = $('#agentNumber').val();
    if (fileNumberId !== "" && pickupYesNo === "Y") {
        if (module !== 'Imports' && !isValidateRates(fileNumberId)) {
            $.prompt('Please Enter Inland Charge/Sell Amount and Inland Cost is Required');
            return false;
        }
        if (module === 'Exports' && $("#doorOriginCityZip").val() === "" && $("#manualDoorOriginCityZip").val() === "") {
            $.prompt("Door Origin/City/Zip is required");
            $("#doorOriginCityZip").css("border-color", "red");
            $("#manualDoorOriginCityZip").css("border-color", "red");
            return false;
        } else if (module === 'Imports' && $("#doorOriginCityZip").val() === "") {
            $.prompt("Door Dest/City/Zip is required");
            $("#doorOriginCityZip").css("border-color", "red");
            return false;
        }
    }
    if (module === 'Exports' && agentNumber === "" && fileNumberId !== '') {
        $.prompt("Agent  is Required");
        return false;
    }
    if (module === 'Exports' && $('#rtdTransaction').val() === '') {
        $.prompt("ERT field is required");
        return false;
    }
    if (fileNumberId === null || fileNumberId === '') {
        confirmAbort(txt, path, fileNumberId, moduleId);
    } else if (isFormChanged()) {
        if (getContact(module)) {
            confirmChanges(txt, path, fileNumberId, moduleId, module, "");
            localStorage.removeItem('lclContactId');
            localStorage.removeItem('lclAccountNo');
        }
    } else if (getContact(module)) {
        checkFileNumber(txt, path, fileNumberId, moduleId, module);
        localStorage.removeItem('lclContactId');
        localStorage.removeItem('lclAccountNo');
    }
}
function ShipReference() {
    var shipref = $('#dupShipref').val();
    $('#shipperClientRef').val(shipref);
}
function clientRefForwarder() {
    var forwarderClientReff = $('#forwarderClientReff').val();
    $('#forwarderClientRef').val(forwarderClientReff);
}
function clientRefConsignee() {
    var consigneeClientReff = $('#consigneeClientReff').val();
    $('#consigneeClientRef').val(consigneeClientReff);
}
function goBackSearch(txt, path, fileNumberId, moduleId, module) {
    var datatableobj = document.getElementById('commObj');
    if (datatableobj === null && module === "Exports") {
        $.prompt("Please add atleast one commodity/tariff#");
        return false;
    }
    var orginAgentStatus = sessionStorage.getItem("orginAgentStatus");
    if (module === 'Imports' && orginAgentStatus === "Clear" && ($('#supplierNameOrg').val() === "" || $('#supplierCode').val() === "")) {
        $.prompt("Origin Agent is Required");
        return false;
    } else {
        sessionStorage.removeItem("orginAgentStatus");
    }
    if (parent.$("#container .selected").text().indexOf("Eculine") != -1) { //goback Eculine
        goBackEculine(path, fileNumberId);
    }
    else {
        goBackEciSearch(txt, path, fileNumberId, moduleId, module);
    }
}
// file is not created yet
function confirmAbort(txt, path, fileNumberId, moduleId) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                confirmNoChanges(path, fileNumberId, moduleId, $('#moduleName').val());
                $.prompt.close();
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function confirmChanges(txt, path, fileNumberId, moduleId, module, buttonValue) {
    var msg = "Do you want to save the booking changes?";
    var orginAgentStatus = sessionStorage.getItem("orginAgentStatus");
    var loginUserRoleId = $('#userRoleId').val();
    if (module === 'Imports' && orginAgentStatus === "Clear" && ($('#supplierNameOrg').val() === "" || $('#supplierCode').val() === "")) {
        $.prompt("Origin Agent is Required");
        return false;
    } else {
        sessionStorage.removeItem("orginAgentStatus");
    }
    if (module === 'Imports' && ($('#scac').val() === "" && $('#defaultAms').val() !== "") && $("#fileNumberId").val() !== "") {
        $.prompt('SCAC is required');
        $('#scac').css("border-color", "red");
        $("#scac").focus();
    } else if (module === 'Imports' && ($('#defaultAms').val() === "" && $('#scac').val() !== "") && $("#fileNumberId").val() !== "") {
        $.prompt('AMS No is required');
        $("#defaultAms").css("border-color", "red");
        $("#defaultAms").focus();
    } else if (module === 'Imports' && ($('#scac').val() !== "" && $('#defaultAms').val() !== "")) {
        var defaultAmsScac = $("#defaultAms").val().toUpperCase() + $("#scac").val().toUpperCase();
        var flag = false;
        $('.hblAmsNoHblScac').each(function () {
            if ($(this).text().trim().trim().toUpperCase() === defaultAmsScac) {
                flag = true;
            }
        });
        if (flag) {
            $.prompt("This DR - " + $('#fileNumber').val() + " already has same scac - <span style=color:red>" + $('#scac').val().toUpperCase() + "</span>"
                    + " and ams# - <span style=color:red>" + $('#defaultAms').val().toUpperCase() + "</span>");
            $('#scac').focus();
        }
        else if ($('#segId').val() === '' || $('#segId').val() === null || $('#segId').val() === undefined) {
            $.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO",
                    methodName: "validateScacAndAms",
                    param1: $('#scac').val(),
                    param2: $('#defaultAms').val(),
                    param3: $("#fileNumberId").val()
                },
                success: function (data) {
                    if (data === "available") {
                        $.prompt(msg, {
                            buttons: {
                                Yes: 1,
                                No: 2,
                                Cancel: 3
                            },
                            submit: function (v) {
                                if (v == 1) { //save and go back
                                    showProgressBar();
                                    var params = $("#lclBookingForm").serialize();
                                    $.post(path + "/lclBooking.do?methodName=saveBooking", params, function () {
                                        checkFileNumber(txt, path, fileNumberId, moduleId, module, buttonValue);
                                        hideProgressBar();
                                    });
                                } else if (v == 2) { //go back
                                    checkFileNumber(txt, path, fileNumberId, moduleId, module, buttonValue);
                                } else { //do nothing
                                    return;
                                }
                            }
                        });
                    } else {
                        $.prompt(data, {
                            callback: function () {
                                $('#scac').focus();
                            }
                        });
                    }
                }
            });
        } else {
            $.prompt(msg, {
                buttons: {
                    Yes: 1,
                    No: 2,
                    Cancel: 3
                },
                submit: function (v) {
                    if (v == 1) { //save and go back
                        showProgressBar();
                        var params = $("#lclBookingForm").serialize();
                        $.post(path + "/lclBooking.do?methodName=saveBooking", params, function () {
                            checkFileNumber(txt, path, fileNumberId, moduleId, module, buttonValue);
                            hideProgressBar();
                        });
                    } else if (v == 2) { //go back
                        checkFileNumber(txt, path, fileNumberId, moduleId, module, buttonValue);
                    }
                    else { //do nothing
                        return;
                    }
                }
            });
        }
    } else {
        $.prompt(msg, {
            buttons: {
                Yes: 1,
                No: 2,
                Cancel: 3
            },
            submit: function (v) {
                if (v == 1) { //save and go back
                    showProgressBar();
                    var params = $("#lclBookingForm").serialize();
                    $.post(path + "/lclBooking.do?methodName=saveBooking", params, function () {
                        checkFileNumber(txt, path, fileNumberId, moduleId, module, buttonValue, 'save');
                        hideProgressBar();
                    });
                } else if (v == 2) { //go back
                    checkFileNumber(txt, path, fileNumberId, moduleId, module, buttonValue, 'goback');
                } else { //do nothing
                    return;
                }
            }
        });
    }
}

function goToUnitLoadScreenFromBooking(path, fileId) {
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
    window.parent.goToBookingFromVoyage(path, fileId, filter, headerId,
            detailId, unitSsId, fromScreen, toScreenName, inTransitDr);
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

function checkFileNumber(txt, path, fileNumberId, moduleId, module, buttonValue, condition) {
    var filterByChanges = $('#filterByChanges').val();
    var moduleName = $('#moduleName').val();
    var pickVoyId = $('#pickVoyId').val();
    var detailId = $('#detailId').val();
    var headerId = $("#headerId").val();
    var unitId = $("#unitId").val();
    var unitSsId = $("#unitSsId").val();
    var impUnitFlag = $("#impSearchFlag").val();
    var pol = $('#pol').val();
    var pod = $('#pod').val();
    var originId = $('#originId').val();
    var destinationId = $('#destinationId').val();
    var bookingType = $('#bookingType').val();
    var callBackFlag = $('#callBackFlag').val();
    var podUnlocationcode = $('#podUnlocationcode').val();
    var fdUnlocation = $('#finalDestinationR').val();
    var toScreenName = $("#toScreenName").val();
    var consolidatedId = $("#consolidatedId").val();
    var fromScreen = $("#fromScreen").val();
    var fdUnlocationcode = fdUnlocation.substring(fdUnlocation.indexOf("(") + 1, fdUnlocation.indexOf(")"));
    var fdCode = fdUnlocationcode.substring(0, 2);
    var scheduleNo = $('#masterScheduleNo').val();
    var unknownDest = $('input:radio[name=nonRated]:checked').val();
    if (bookingType === 'T' && moduleName === "Exports" && impUnitFlag !== "") {
        moduleName = 'Imports';
    }
    if (fromScreen === 'ar_inquiry' && toScreenName === 'LCLBooking') {
        window.parent.showArInquiryForLcl($('#fileNumber').val(), "ar_inquiry");
    } else if (null !== pickVoyId && pickVoyId !== "" && pickVoyId !== undefined) {
        if (filterByChanges === 'lclExport' || filterByChanges === 'lclDomestic' || filterByChanges === 'lclCfcl') {
            goToUnitLoadScreenFromBooking(path, fileNumberId);
        } else if (filterByChanges === 'lclImports') {
            goToImpVoyageFromExportBooking(path, fileNumberId);
        } else if (filterByChanges === 'LCL_IMP_DR') {
            goToImpSearchFromExportBooking(path, fileNumberId, moduleId);
        }
    } else if (fromScreen === 'ConsolidatePopUp' && toScreenName === 'Booking') {
        var fileState = getfileNumberState(consolidatedId);
        window.parent.goToConsolidatePage(path, consolidatedId, fileState, moduleName, fromScreen, toScreenName, fileNumberId);
    } else if (fromScreen === 'ConsolidateDesc' && toScreenName === 'Booking') {
        var fileState = getfileNumberState(consolidatedId);
        window.parent.goToConsolidatePage(path, consolidatedId, fileState, moduleName, fromScreen, toScreenName, fileNumberId);
    }
    else {
        if (fileNumberId !== "" && fileNumberId !== null) {
            var loginUser = $('#userRoleId').val();
            if (condition !== 'save' && condition !== 'goback') {
                condition = "";
            }
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "checkBookingVoyage",
                    param1: module,
                    param2: $('#userRoleId').val(),
                    param3: condition,
                    dataType: "json"
                },
                success: function (data) {
                    if (data === true && scheduleNo === "" && unknownDest === 'N') {
                        if (isFormChanged()) {
                            congAlert("Booked for Voyage is Required");
                        } else {
                            confirmNoChanges(path, fileNumberId, moduleId, moduleName);
                        }
                    }
                    else {
                        var ratetype = $('input:radio[name=rateType]:checked').val();
                        var thirdParty = $('input:radio[name=billForm]:checked').val();
                        var CFCL = $('input:radio[name=cfcl]:checked').val();
                        var haz = $('.hazmat').val();
                        var ert = $('#rtdTransaction').val();
                        var insurance = $('input:radio[name=insurance]:checked').val();
                        var pickupYesNo = $('input:radio[name=pooDoor]:checked').val();
                        var shipment = $('input:radio[name=transShipMent]:checked').val();
                        if (module !== "Imports" && ($("#portOfOriginR") === null || $("#portOfOriginR").val() === "")) {
                            $.prompt("Origin CFS is required");
                            return false;
                        } else if ($("#finalDestinationR") === null || $("#finalDestinationR").val() === "") {
                            if (module !== "Imports") {
                                $.prompt("Destination is required");
                            }
                            else {
                                $.prompt("Final Destination is required");
                            }
                            $("#finalDestinationR").css("border-color", "red");
                            return false;
                        } else if (module !== 'Imports' && (ratetype === null || ratetype === "" || ratetype === undefined)) {
                            $.prompt("CTC/Retail/FTF  is required");
                            return false;
                        }
                        else if (insurance === "Y" && ($("#valueOfGoods").val() === null || $("#valueOfGoods").val() === "")) {
                            $.prompt("Please enter the Value of Goods");
                            $("#valueOfGoods").css("border-color", "red");
                            return false;
                        }
//                        else if (thirdParty === "T" && ($('#thirdPartyname').val() === '' || $('#thirdPartyname').val() === null)) {
//                            $.prompt("Third Party Account Name is required");
//                            $('#thirdPartyname').css("border-color", "red");
//                            return false;
//                        }
                        else if (CFCL === "Y" && ($('#cfclAccount').val() === '' || $('#cfclAccount').val() === null)) {
                            $.prompt("CFCL Account is required");
                            $('#cfclAccount').css("border-color", "red");
                            return false;
                        } else if ($("#terminal") === null || $("#terminal").val() === "") {
                            $.prompt("Term to do BL is Required")
                            $("#terminal").css("border-color", "red");
                            return false;
                        } else if (ert === "" || ert === null) {
                            $.prompt("ERT field is required");
                            $("#rtdTransaction").css("border-color", "red");
                            return false;
                        } else if (module === 'Exports' && pickupYesNo !== null && pickupYesNo === "Y"
                                && $("#doorOriginCityZip").val() === "" && $("#manualDoorOriginCityZip").val() === "") {
                            $.prompt("Door Origin  is required");
                            $("#doorOriginCityZip").css("border-color", "red");
                            $("#manualDoorOriginCityZip").css("border-color", "red");
                            return false;
                        } else if (module === 'Imports' && pickupYesNo !== null && pickupYesNo === "Y" && $("#doorOriginCityZip").val() === "") {
                            $.prompt("Door Origin  is required");
                            $("#doorOriginCityZip").css("border-color", "red");
                            return false;
                        } else if ((module === 'Imports' && headerId !== null && headerId !== "" && fileNumberId !== null && fileNumberId !== "") && document.getElementById('commObj') === null) {
                            $.prompt("Please select atleast One Commodity");
                            return false;
                        } else if (module === 'Imports' && $('#stGeorgeAccount').val() === '' && podUnlocationcode !== fdUnlocationcode && shipment !== 'Y') {
                            $.prompt("Please enter IPI CFS");
                            return false;
                        } else if (module === 'Imports' && shipment === 'Y' && fdCode === 'US') {
                            $.prompt("Final destination should not be in USA, Please change the Final destination.");
                            $("#finalDestinationR").css("border-color", "red");
                            result = false;
                        } else if (module === 'Imports' && shipment === 'Y' && ($("#portExit").val() === "" || $("#portExit").val() === null)) {
                            $.prompt("USA Port of Exit is required");
                            $("#portExit").css("border-color", "red");
                            return false;
                        } else if (module === 'Imports' && shipment === 'Y' && ($("#foreignDischarge").val() === "" || $("#foreignDischarge").val() === null)) {
                            $.prompt("Foreign Port of Discharge is required");
                            $("#foreignDischarge").css("border-color", "red");
                            return false;
                        } else {
                            if (impUnitFlag !== "" && headerId !== null && headerId !== "" && moduleName === 'Imports') {
                                goBackToUnitScreen(path, headerId, unitId, fileNumberId);
                            } else if (fileNumberId !== "" && haz === 'Haz') {
                                hazValidateBackButton(path, fileNumberId, moduleId, moduleName, callBackFlag, buttonValue, unitSsId);
                            } else {
                                if (buttonValue === 'EciVoyage') {
                                    changeMenuTabs(path, fileNumberId, "IMP VOYAGE", moduleName, "OPEN IMP VOYAGE", unitSsId);
                                } else {
                                    changeMenuTabs(path, fileNumberId, moduleId, moduleName, callBackFlag);
                                }
                            }
                        }
                    }
                }
            });
        }
        else {
            confirmNoChanges(path, fileNumberId, moduleId, moduleName);
        }
    }
}
function goBackToUnitScreen(path, headerId, unitId, fileNumberId) {
    var fileNumber = $('#fileNumber').val();
    var fileStatus = $('#fileStatus').val();
    var unitStatus = $('#unitStatus').val();
    if (fileStatus === 'B' && unitStatus === 'M' && $('input:radio[name=transShipMent]:checked').val() === 'N') {
        $.prompt("This D/R has not been posted to accounting.Do you want to post it now? ", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    if (validateBillToPartyImp()) {
                        $.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.struts.action.lcl.LclImportAddUnitsAction",
                                methodName: "checkChargeAndCostMappingWithGLForFileNumber",
                                param1: fileNumber
                            },
                            success: function (data) {
                                if (data !== "null" && data !== "") {
                                    $.prompt("No gl account is mapped with these charge code.Please contact accounting - <span style=color:red>" + data + "</span>.");
                                } else {
                                    redirectImpUnitScreen(path, headerId, unitId, fileNumberId, "Acct_Post");
                                }
                            }
                        });
                    }
                    $.prompt.close();
                }
                else if (v === 2) {
                    redirectImpUnitScreen(path, headerId, unitId, "", "");
                    $.prompt.close();
                }
            }
        });
    }
    else {
        redirectImpUnitScreen(path, headerId, unitId, "", "");
    }
}
function validateBillToPartyImp() {
    var consigneeChargeFlag = false;
    var notifyChargeFlag = false;
    var thirdPartyChargeFlag = false;
    var rowCount = $("#chargesTable tbody tr").length;
    for (var i = 1; i <= rowCount; i++) {
        var value = $("#chargesTable").find('tr:nth-child(' + i + ') td:nth-child(6)').html();
        if (trim(value).indexOf("Consignee") !== -1) {
            consigneeChargeFlag = true;
        }
        if (trim(value).indexOf("Notify Party") !== -1) {
            notifyChargeFlag = true;
        }
        if (trim(value).indexOf("Third Party") !== -1) {
            thirdPartyChargeFlag = true;
        }
    }
    var errorMsg = '';
    if (consigneeChargeFlag && (!$('#newConsignee').is(':checked') && $('#consigneeCode').val() === '')) {
        errorMsg = 'House Consignee is Required. <br>';
        $("#consigneeName").css("border-color", "red");
    }
    if (consigneeChargeFlag && $('#newConsignee').is(':checked')) {
        errorMsg += 'Consignee does not exist in Trading Partners.Cannot bill charges to Consignee.Please correct. <br>';
        $("#dupConsigneeName").css("border-color", "red");
    }
    if (notifyChargeFlag && (!$('#newNotify').is(':checked') && $('#notifyCode').val() === '')) {
        errorMsg += 'Notify is Required. <br>';
        $("#notifyName").css("border-color", "red");
    }
    if (notifyChargeFlag && $('#newNotify').is(':checked')) {
        errorMsg += 'Notify does not exist in Trading Partners.Cannot bill charges to Notify.Please correct. <br>';
        $("#dupNotifyName").css("border-color", "red");
    }
    if (thirdPartyChargeFlag && $('#thirdpartyaccountNo').val() === '') {
        errorMsg += 'Third Party is Required.<br>';
        $("#thirdPartyname").css("border-color", "red");
    }
    if (errorMsg !== '') {
        $.prompt(errorMsg);
        return false;
    }
    return true;
}

function redirectImpUnitScreen(path, headerId, unitId, fileNumberId, postFlag) {
    window.parent.showLoading();
    var searchOriginId = $('#searchOriginId').val();
    var searchFdId = $('#searchFdId').val();
    var searchTerminalNo = $('#searchTerminalNo').val();
    var searchLoginId = $('#searchLoginId').val();
    var searchMasterBl = $('#searchMasterBl').val();
    var searchUnitNo = $('#searchUnitNo').val();
    var searchAgentNo = $('#searchAgentNo').val();
    var searchVoyageNo = $('#searchVoyageNo').val();
    var searchDispoId = $('#searchDispoId').val();
    var searchOrigin = $('#searchOrigin').val();
    var searchFd = $('#searchFd').val();
    var searchTerminal = $('#searchTerminal').val();
    var limit = $('#limit').val();

    var href = path + "/lclImportAddVoyage.do?methodName=redirectImportsVoyage&headerId=" + headerId + "&unitId=" + unitId +
            "&fileNumber=" + $('#fileNumber').val() + "&fileNumberId=" + fileNumberId + "&postFlag=" + postFlag + "&unitssId=" + $('#unitSsId').val()
            + "&searchOriginId=" + searchOriginId + "&searchFdId=" + searchFdId + "&searchTerminalNo=" + searchTerminalNo
            + "&searchLoginId=" + searchLoginId + "&searchUnitNo=" + searchUnitNo + "&searchMasterBl=" + searchMasterBl
            + "&searchAgentNo=" + searchAgentNo + "&searchDispoId=" + searchDispoId + "&searchVoyageNo=" + searchVoyageNo + "&searchVoyageLimit=" + limit
            + "&searchOrigin=" + searchOrigin + "&searchFd=" + searchFd + "&searchTerminal=" + searchTerminal;
    window.location.href = href;
    $.prompt.close();
}
function confirmNoChanges(path, fileNumberId, moduleId, moduleName) {
    var impUnitFlag = $("#impSearchFlag").val();
    var headerId = $("#headerId").val();
    if (impUnitFlag !== "" && headerId !== null && headerId !== "" && moduleName === 'Imports') {
        var unitId = $("#unitId").val();
        redirectImpUnitScreen(path, headerId, unitId, "", "");
    } else {
        changeMenuTabs(path, fileNumberId, moduleId, moduleName);
    }
}
function changeMenuTabs(path, fileNumberId, moduleId, moduleName, callBackFlag, unitSsId) {
    showProgressBar();
    var homeScreenDrFileFlag = moduleId !== "IMP VOYAGE" ? $("#homeScreenDrFileFlag").val() : "false";
    callBackFlag = $("#tabName").val() === "doorDeliverySearchTab" && moduleId !== "IMP VOYAGE" ? "doorDeliverySearchTab" : callBackFlag;
    var fileNumber = $("#moduleId").val();
    if (callBackFlag === "LCLEXPTRANS") {
        moduleName = "Imports";
    }
    if (homeScreenDrFileFlag === "true") {
        window.parent.showHome(homeScreenDrFileFlag, fileNumber);
    } else {
        if (parent.$("#fromScreen").val() === 'UnitLoadScreenToBooking') {
            goToUnitLoadScreenFromBooking(path, "");
        } else {
            window.parent.changeLclChilds(path, fileNumberId, moduleId, moduleName, callBackFlag, unitSsId);
        }
    }
    hideProgressBar();
}
function showClientSearchOption(path, searchByValue) {
    var href = path + "/lclBooking.do?methodName=clientSearch&searchByValue=" + searchByValue;
    $(".clientSearchEdit").attr("href", href);
    $(".clientSearchEdit").colorbox({
        iframe: true,
        width: "35%",
        height: "40%",
        title: searchByValue + " Search"
    });
}
var missingFields = '';
var i = 1;
function convertToBL(path, fileNumberId, moduleId) {
    var datatableobj = document.getElementById('commObj');
    var moduleName = $('#moduleName').val();
    if (datatableobj === null && moduleName === "Exports") {
        $.prompt("Please add atleast one commodity/tariff#");
        return false;
    }
    var restrict = consolidateForConvertBl();
    missingFields = '';
    i = 1;
    var pcBoth = $('input:radio[name=pcBoth]:checked').val();
    var billForm = $('input:radio[name=billForm]:checked').val();
    var rateType = $("input:radio[name=rateType]:checked").val();
    var agentNumber = $('#agentNumber').val();
//    var shipperCode = $('#shipperCode').val();
//    var shipperCodeClient = $('#shipperCodeClient').val();
//    var forwarderCode = $('#forwarderCode').val();
    var thirdpartyaccountNo = $('#thirdpartyaccountNo').val();
    var CFCL = $('input:radio[name=cfcl]:checked').val();
    var ert = $('#rtdTransaction').val();
    var pwk = $('input:radio[name=pwk]:checked').val();
    var hsCode = $('#hsCode').val();
    var ratesFlag = true;
    var HsCodeFlag = true;

    var isConsolidate = $("#conosolidateId tr").length > 1 ? true : false;
    if (isConsolidate) {
        var data = getParentDR(fileNumberId);
        if (data[0] !== null && data[0] !== fileNumberId) {
            $.prompt("This DR is not the Parent DR, please create the BL on Parent DR <span style='color:red;'>" + data[1] + "</sapn>");
            return false;
        }
    }
    if (poaRestrication()) {
        missingFields = missingFields + i + "." + "Cannot convert to BL due to <span style='color:red'>POA</span> restrictions <br>";
        i = i + 1;
    }
    if (rateType === '' || rateType === null || rateType === undefined) {
        missingFields = missingFields + i + "." + "CTC/Retail/FTF is required <br>";
        i = i + 1;
    }
    if (ert == "" || ert == null) {
        missingFields = missingFields + i + "." + "ERT field is required <br>";
        $("#rtdTransaction").css("border-color", "red");
        $('#agentName').css("border-color", "");
        $('#thirdPartyname').css("border-color", "");
        $('#shipperNameClient').css("border-color", "");
        $('#forwarderNameClient').css("border-color", "");
        $('#billToCodeBorder').css("border", " ");
        i = i + 1;
    }
    if (CFCL == "Y" && ($('#cfclAccount').val() == '' || $('#cfclAccount').val() == null)) {
        missingFields = missingFields + i + "." + "CFCL Account is required <br>";
        $('#cfclAccount').css("border-color", "red");
        i = i + 1;
    }
    if (pwk == 'N') {
        missingFields = missingFields + i + "." + "PWK/Docs Received should be as YES <br>";
        $("#pwkBorder").css("border", "1px solid #ff0000");
        i = i + 1;
    }
    if (moduleId != 'Imports' && bkgContactRoleDuty() && !isBookingContactAvaliable()) {
        missingFields = missingFields + i + "." + "Please select atleast one booking contact name <br>";
        i = i + 1;
    }
    if (agentNumber === "") {
        missingFields = missingFields + i + "." + "Agent  is Required <br>";
        $('#agentName').css("border-color", "red");
        $('#thirdPartyname').css("border-color", "");
        $('#shipperNameClient').css("border-color", "");
        $('#forwarderNameClient').css("border-color", "");
        $('#billToCodeBorder').css("border", " ");
        $("#rtdTransaction").css("border-color", "");
        i = i + 1;
    }
    //------------15977: DR – Convert to BL – do not check for PPD/COL--------------
//    if (pcBoth == 'P' && billForm == "T" && thirdpartyaccountNo == "") {
//        missingFields = missingFields + i + "." + "Please select Third Party Account <br>";
//        $('#thirdPartyname').css("border-color", "red");
//        $('#agentName').css("border-color", "");
//        $('#shipperNameClient').css("border-color", "");
//        $('#forwarderNameClient').css("border-color", "");
//        $('#billToCodeBorder').css("border", " ");
//        $("#rtdTransaction").css("border-color", "");
//        i = i + 1;
//    }
//    // congAlert("Manual Shipper Account is Not Allowed For This Bill To Code Value");
//    if (pcBoth === 'P' && billForm === "S" && (shipperCodeClient === "" && shipperCode === "")) {
//        missingFields = missingFields + i + "." + "Please select shipper Account <br>";
//        $('#shipperNameClient').css("border-color", "red");
//        $('#agentName').css("border-color", "");
//        $('#thirdPartyname').css("border-color", "");
//        $('#forwarderNameClient').css("border-color", "");
//        $('#billToCodeBorder').css("border", " ");
//        $("#rtdTransaction").css("border-color", "");
//        i = i + 1;
//    }
//    if ((pcBoth == 'P' && billForm == "F" && forwarderCode == "") || (validateForwarderCharge() && forwarderCode === "")) {
//        missingFields = missingFields + i + "." + "Please select Forwarder Account <br>";
//        $('#forwarderNameClient').css("border-color", "red");
//        $('#agentName').css("border-color", "");
//        $('#thirdPartyname').css("border-color", "");
//        $('#shipperNameClient').css("border-color", "");
//        $('#billToCodeBorder').css("border", " ");
//        $("#rtdTransaction").css("border-color", "");
//        i = i + 1;
//    }
//
//    var freightFlag = filterNoFreightForwarderAc(forwarderCode);
//    if (forwarderCode !== "" && (freightFlag && pcBoth === 'P') && billForm !== 'S' && billForm !== 'T') {
//        missingFields = missingFields + i + "." + "You cannot bill to this Freight Forwarder account, please choose another, or change Prepaid options <br>";
//        $('#forwarderNameClient').css("border-color", "red");
////            clearForwarder();
//        i = i + 1;
//    }
    if (!validateBillToForConvertBl()) {
        missingFields = missingFields + i + "." + "Please ensure all charge items have a bill to party <br>";
        i = i + 1;
    }
    if (hsCodeForConvert() === "HSCODE" && moduleName === "Exports") {
        if (HsCodeFlag === true) {
            var fileId = $('#fileNumberId').val();
            var hsCodeVal;
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "getHsCode",
                    param1: fileId,
                    dataType: "json"
                },
                async: false,
                success: function (data) {
                    hsCodeVal = data;
                    if ((hsCode === null || hsCode === "") && (hsCodeVal === null || hsCodeVal === "")) {
                        missingFields = missingFields + i + "." + "Hs Code# is Required <br>";
                        i = i + 1;
                    }
                }
            });
        }
    }
    if (ratesFlag === true)
    {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkRates",
                param1: $('#fileNumberId').val(),
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data == false && $('#bookingType').val() != 'T') {
                    missingFields = missingFields + i + "." + "Please Calculate AUTORATES to Convert To BL <br>";
                    return false;
                }
            }
        });
    }
    if (restrict !== '' && restrict !== null) {
        missingFields = missingFields + i + "." + restrict + "<br>";
    }
    if ('' !== missingFields) {
        $.prompt(missingFields);
        return false;
    }
    if ($('#notifyCode').val() === '' && $('#consigneeCodeClient').val() !== '') {
        defaultNotifyParty();
    }
    convertBookingToBL(path, fileNumberId, moduleId, pcBoth, billForm, thirdpartyaccountNo);
}

function validateForwarderCharge() {
    var forwarderFlag = false
    jQuery(".billToPartyCharges").each(function () {
        var billToParty = $(this).html().trim().trim();
        if (billToParty === "Forwarder") {
            forwarderFlag = true;
        }
    });
    return forwarderFlag;
}
function filterNoFreightForwarderAc(forwarderCode) {
    var flag;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO",
            methodName: "getFreightForwardAcctStatus",
            param1: forwarderCode,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}

function getParentDR(fileNumberId) {
    var value = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO",
            methodName: "getParentConsolidateFile",
            param1: fileNumberId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            value = data;
        }
    });
    return value;
}

function convertBookingToBL(path, fileNumberId, moduleId, pcBoth, billToParty, acctNo) {
    var pickupYesNo = $('input:radio[name=pooDoor]:checked').val();
    //var isConsolidate = $("#conosolidateId tr").length > 1 ? true : false;
//    if (isConsolidate) {
//        var data = getParentDR(fileNumberId);
//        if (data[0] !== null && data[0] !== fileNumberId) {
//            $.prompt("This DR is not the Parent DR, please create the BL on Parent DR <span style='color:red;'>" + data[1] + "</sapn>");
//            return false;
//        }
//    }
    if (fileNumberId !== "" && pickupYesNo === "Y") {
        if (!isValidateRates(fileNumberId)) {
            $.prompt('Please Enter Inland Charge/Sell Amount and Inland Cost is Required');
            return false;
        }
    }
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkCarrierCommodity",
            param1: fileNumberId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data == true) {
                congAlert("Cannot convert Carrier to Carrier commodity (032500), Please select new Tariff");
                return false;
            } else {
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "checkRates",
                        param1: $('#fileNumberId').val(),
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        if (data == false && $('#bookingType').val() != 'T') {
                            congAlert("Please Calculate AUTORATES to Convert To BL.");
                            return false;
                        } else {
                            $.ajaxx({
                                dataType: "json",
                                data: {
                                    className: "com.gp.cong.lcl.dwr.LclDwr",
                                    methodName: "commodity",
                                    param1: fileNumberId,
                                    dataType: "json"
                                },
                                async: false,
                                success: function (data) {
                                    if (data == false) {
                                        congAlert("Please select commodity");
                                        return false;
                                    } else {
                                        if (isFormChanged()) {
                                            var msg = "Do you want to save the booking changes?";
                                            $.prompt(msg, {
                                                buttons: {
                                                    Yes: 1,
                                                    No: 2,
                                                    Cancel: 3
                                                },
                                                submit: function (v) {
                                                    if (v == 1) {
                                                        window.parent.showLoading();
                                                        var moduleName = $('#moduleName').val();
                                                        var fileNumberId = $('#fileNumberId').val();
                                                        if (moduleName != 'Imports' && fileNumberId != null && fileNumberId != "" && fileNumberId != "0") {
                                                            displayClientNotes('B');
                                                            displayShipperNotes('B');
                                                            displayForwarderNotes('B');
                                                            displayConsigneeNotes('B');
                                                        }
                                                        $("#methodName").val("saveBooking");
                                                        var params = $("#lclBookingForm").serialize();
                                                        params += "&fileNumberId=" + fileNumberId;
                                                        $.post($("#lclBookingForm").attr("action"), params,
                                                                function (data) {
                                                                    window.parent.closePreloading();
                                                                    convertLclBookingToBL(path, fileNumberId, moduleId, pcBoth, billToParty, acctNo);
                                                                });
                                                    } else if (v == 2) {
                                                        convertLclBookingToBL(path, fileNumberId, moduleId, pcBoth, billToParty, acctNo);
                                                    } else {
                                                        return;
                                                    }
                                                }
                                            });
                                        } else {
                                            convertLclBookingToBL(path, fileNumberId, moduleId, pcBoth, billToParty, acctNo);
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

function convertLclBookingToBL(path, fileNumberId, moduleId, pcBoth, billToParty, acctNo) {
    if (fileNumberId === '' || fileNumberId === undefined) {
        fileNumberId = $('#fileNumberId').val();
    }
    var txt = "Are you sure you want to convert?";
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "checkBookingNofromEdi",
                        param1: fileNumberId
                    },
                    success: function (data) {
                        if (data == 'true') {
                            var href = path + '/lclBooking.do?methodName=getDataFromEdiSystem&fileNumberId=' + fileNumberId;
                            $.colorbox({
                                iframe: false,
                                width: "65%",
                                height: "35%",
                                href: href,
                                title: "EDI Tracking"
                            });
                        }
                        else {
                            showProgressBar();
                            $("#methodName").val("convertToBL");
                            var params = $("#lclBookingForm").serialize();
                            $.post($("#lclBookingForm").attr("action"), params, function () {
                                var moduleName = $('#moduleName').val();
                                if ($("#toScreenName").val() === 'EXP VOYAGE' || $("#toScreenName").val() === 'EXPORT_TO_IMPORT') {
                                    converToBlFromUnitScreen(path, fileNumberId, "converToBlFlag");
                                } else {
                                    window.parent.changeLclChilds(path, fileNumberId, moduleId, moduleName, "converToBlFlag", "");
                                }
                                hideProgressBar();
                            });
                        }
                    }
                });
            }
            else if (v == 2) {
                $.prompt.close();
            }

        }
    });
}
function converToBlFromUnitScreen(path, fileId, callback) {
    var headerId = parent.$("#pickVoyId").val();
    var detailId = parent.$("#detailId").val();
    var unitSsId = parent.$("#unitSsId").val();
    var toScreenName = parent.$("#toScreenName").val();
    var fromScreen = parent.$("#fromScreen").val();
    var filter = parent.$("#filterByChanges").val();
    var inTransitDr = parent.$("#inTransitDr").val();
    window.parent.goToBookingFromVoyage(path, fileId, filter, headerId, detailId, unitSsId, fromScreen, toScreenName, inTransitDr, callback);
}
function closeLclEdiData() {
    if (null != document.getElementById("lclEdiDataDiv")) {
        document.body.removeChild(document.getElementById("lclEdiDataDiv"));
    }
    closePopUp();
}
function continuelclEdiData(path, fileNumberId, moduleId) {
    var lclEdiData = [];
    var totalLclEdiDataCount = 0;
    var pcBoth = $('input:radio[name=pcBoth]:checked').val();
    var billForm = $('input:radio[name=billForm]:checked').val();
    var agentNumber = $('#agentNumber').val();
    var shipperCode = $('#shipperCode').val();
    var shipperCodeClient = $('#shipperNameClient').val();
    var forwarderCode = $('#forwarderCode').val();
    var thirdpartyaccountNo = $('#thirdpartyaccountNo').val();
    var CFCL = $('input:radio[name=cfcl]:checked').val();
    jQuery(".lclEdiData").each(function () {
        totalLclEdiDataCount++;
        if (jQuery(this).is(":checked") == true) {
            lclEdiData.push("true");
        }
        else {
            lclEdiData.push("false");
        }
    });
    $("#ediData").val(lclEdiData);
    if (CFCL == "Y" && (document.getElementById('cfclAccount').value == '' || document.getElementById('cfclAccount').value == null)) {
        congAlert("CFCL Account is required");
        $('#cfclAccount').css("border-color", "red");
        return false;
    }
    if (pcBoth == 'C') {
        if (agentNumber == "") {
            congAlert("Agent  is Required");
        } else {
            lclEdiDataconvertToBL(path, fileNumberId, moduleId, pcBoth, billForm, agentNumber, lclEdiData);
        }
    } else if (pcBoth == 'P') {
        if (billForm == null || billForm == "" || billForm == undefined) {
            congAlert("Bill To Code is required");
        } else {
            if (billForm == "T") {
                if (thirdpartyaccountNo == "") {
                    congAlert("Bill to account is required");
                } else {
                    lclEdiDataconvertToBL(path, fileNumberId, moduleId, pcBoth, billForm, thirdpartyaccountNo, lclEdiData);
                }
            }
            else if (billForm == "S") {
                if (shipperCode == "" && shipperCodeClient == "") {
                    congAlert("Please select shipper Account ");
                } else {
                    lclEdiDataconvertToBL(path, fileNumberId, moduleId, pcBoth, billForm, shipperCode, lclEdiData);
                }
            }
            else if (billForm == "F") {
                if (forwarderCode == "" && forwarderCode == "") {
                    congAlert("Please select Forwarder Account");
                } else {
                    lclEdiDataconvertToBL(path, fileNumberId, moduleId, pcBoth, billForm, forwarderCode, lclEdiData);
                }
            }
        }
    }
    else {
        if (forwarderCode == "" && forwarderCode == "") {
            congAlert("Please select Forwarder Account");
        } else {
            lclEdiDataconvertToBL(path, fileNumberId, moduleId, pcBoth, "F", forwarderCode, lclEdiData);
        }
    }
}
function lclEdiDataconvertToBL(path, fileNumberId, moduleId, pcBoth, billToParty, acctNo, lclEdiData) {
    showProgressBar();
    $("#methodName").val("convertToBL");
    var params = $("#lclBookingForm").serialize();
    $.post($("#lclBookingForm").attr("action"), params, function () {
        var moduleName = $('#moduleName').val();
        window.parent.changeLclChilds(path, fileNumberId, moduleId, moduleName);
        hideProgressBar();
    });
}
function checkAndUnceckAll(check, checkboxNames) {
    var check = document.getElementById(check).checked;
    var checkBoxes = document.getElementsByName(checkboxNames);
    for (i = 0; i < checkBoxes.length; i++) {
        checkBoxes[i].checked = check;
    }
}
function newConsigneeContactName() {
    if ($('#newConsigneeContact').is(":checked")) {
        $('#consigneeManual').show();
        $('#consigneeDojo').hide();
        $('#consigneeManualContact').val('');
        $('#consigneeContactName').val('');
        $('#consigneeEmailClient').val('');
        $('#dupConsPhone').val('');
        $('#dupConsFax').val('');
        $('#consigneeFaxClient').val('');
        $('#consigneePhoneClient').val('');
    } else {
        $('#consigneeManual').hide();
        $('#consigneeDojo').show();
        $('#consigneeManualContact').val('');
        $('#dupConsPhone').val('');
        $('#dupConsFax').val('');
        $('#consigneeContactName').val('');
        $('#consigneeEmailClient').val('');
        $('#consigneeFaxClient').val($('#dupConsigneeFax').val());
        $('#consigneePhoneClient').val($('#dupConsigneePhone').val());
    }
}
function newForwarderContactName() {
    if ($('#newForwarderContact').is(":checked")) {
        $('#forwarederManual').show();
        $('#forwarederDojo').hide();
        $('#forwarederContactManual').val('');
        $('#dupFwdPhone').val('');
        $('#dupFwdFax').val('');
        $('#forwardercontactClient').val('');
        $('#forwarderEmailClient').val('');
        $('#forwarderFaxClient').val('');
        $('#forwarderPhoneClient').val('');
    } else {
        $('#forwarederManual').hide();
        $('#forwarederDojo').show();
        $('#dupFwdPhone').val('');
        $('#dupFwdFax').val('');
        $('#forwarederContactManual').val('');
        $('#forwardercontactClient').val('');
        $('#forwarderEmailClient').val('');
        $('#forwarderFaxClient').val($('#dupForwarderFax').val());
        $('#forwarderPhoneClient').val($('#dupForwarderPhone').val());
    }
}

function newClientContactName() {
    if ($('#newClientContact').is(":checked")) {
        $('#clientContactManual').show();
        $('#clientContactDojo').hide();
        $('#clientContactManul').val('');
        $('#contactName').val('');
        $('#email').val('');
        $('#dupFax').val('');
        $('#dupPhone').val('');
        $('#fax').val('');
        $('#phone').val('');
    } else {
        $('#clientContactManual').hide();
        $('#clientContactDojo').show();
        $('#clientContactManul').val('');
        $('#contactName').val('');
        $('#dupFax').val('');
        $('#dupPhone').val('');
        $('#email').val('');
        $('#fax').val($('#dupClientFax').val());
        $('#phone').val($('#dupClientPhone').val());
    }
}

function newShipperContactName() {
    if ($('#newShipperContact').is(":checked")) {
        $('#shippManual').show();
        $('#shipperDojo').hide();
        $('#shipperManualContact').val('');
        $('#dupShipPhone').val('');
        $('#dupShipFax').val('');
        $('#shipperEmailClient').val('');
        $('#shipperContactClient').val('');
        $('#shipperFaxClient').val('');
        $('#shipperPhoneClient').val('');
    } else {
        $('#shippManual').hide();
        $('#shipperDojo').show();
        $('#shipperEmailClient').val('');
        $('#shipperManualContact').val('');
        $('#shipperContactClient').val('');
        $('#dupShipPhone').val('');
        $('#dupShipFax').val('');
        $('#shipperFaxClient').val($('#dupShipperFax').val());
        $('#shipperPhoneClient').val($('#dupShipperPhone').val());
    }
}

function submitAesHistory(path, filenumber) {
    var href = path + '/aesHistory.do?fileNumber=' + filenumber;
    $(".linkSpan").attr("href", href);
    $(".linkSpan").colorbox({
        iframe: true,
        width: "75%",
        height: "90%",
        title: "AES Tracking"
    });
}

function fileSed(resend, trnref, index, obj, fileNo) {
    var doorOrigin = $('#doorOriginCityZip').val();
    var sedtable = document.getElementById("aes");
    var rowObj = sedtable.rows[index];
    var aesStatus = rowObj.cells[6].innerHTML;
    if (undefined != aesStatus && null != aesStatus && trim(aesStatus) == 'Sent' && resend != 'true') {
        $.prompt("You can't send AES untill you receive the status for previous");
        return;
    } else {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkSchedBAvailability",
                param1: trnref,
                dataType: "json"
            },
            success: function (data) {
                if (data) {
                    $.prompt("Please Enter Atleast one Sched B Information");
                }
                else {
                    $.ajaxx({
                        data: {
                            className: "com.gp.cong.lcl.dwr.LclDwr",
                            methodName: "createFlatFile",
                            param1: trnref,
                            param2: $('#fileNumberId').val(),
                            param3: doorOrigin,
                            request: "true"
                        },
                        success: function (data) {
                            if (null != data && data == 'Success') {
                                if (document.getElementById("aes") != null) {
                                    var rowObj = sedtable.rows[index];
                                    if (rowObj != undefined) {
                                        rowObj.cells[6].innerHTML = "Sent";
                                    }
                                    document.getElementById(obj.id).style.backgroundColor = "yellow";
                                }
                                if (document.getElementById("deleteAesDetails")) {
                                    document.getElementById("deleteAesDetails").style.visibility = "hidden";
                                }
                                $.prompt("AES Sent Successfully for Transaction Ref# " + trnref);
                            }
                        }
                    });
                }
            }
        });
    }
}
function setOriginLrd() {
    var haz = false;
    var originLrd = $('#selectedLrd').val();
    var eciVoy = $('#eciVoyage').val();
    var sched = $('.scheduleNo').val();
    var lrDate;
    $('.scheduleNo').each(function () {
        if ($(this).html().indexOf(eciVoy) != -1) {
            lrDate = $(this).parent().next().next().html();
        }
    });
    if (originLrd === "" && lrDate != undefined && lrDate != '') {
        originLrd = (lrDate.replace(/^\s\s*/, '').replace(/\s\s*$/, ''));
    }
    $('#commObj tr').each(function () {
        var tempHaz = $(this).find(".hazClass").html();
        if (tempHaz != null && tempHaz.indexOf("Y") !== -1) {
            haz = true;
        }
    });
    if (haz) {
        var date = originLrd;
        var days = -1;
        originLrd = addDate(date, days);
        originLrd = getWeekday(originLrd);
    }
    if (originLrd != null && originLrd != undefined && originLrd.split("-").length == 3 && originLrd.indexOf("NaN") == -1
            && eciVoy != "" && eciVoy != null && eciVoy != undefined) {
        $('#originLrd').val(originLrd);
    } else {
        $('#originLrd').val("");
    }
}

function openEciVoyage(path) {
    var unitSsId = $("#unitSsId").val();
    var fileNumberId = $("#fileNumberId").val();
    if ($('#impEciVoyage').val() !== '') {
        if (isFormChanged()) {
            confirmChanges("", path, fileNumberId, "IMP VOYAGE", "Imports", "EciVoyage");
        } else {
            changeMenuTabs(path, fileNumberId, "IMP VOYAGE", "Imports", "OPEN IMP VOYAGE", unitSsId);
        }
    }
}

function getfileNumberState(fileId) { // get file state for consolidated file.
    var value = '';
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO",
            methodName: "getFileStateById",
            param1: fileId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            value = data;
        }
    });
    return value;
}

function isBookingContactAvaliable() {// for booking contact
    var flag = false;
    $(".contactName").each(function () {
        if ($(this).val() !== '') {
            flag = true;
        }
    });
    return flag;
}
function getContact(module) {
    var fileStatus = $('#fileStatus').val();
    if (($("#dispoId").text() === 'OBKG' && $("#bookingType").val() === 'T') && module !== 'Imports') {
        return true;
    } else if (module !== 'Imports' && 'X' !== fileStatus && bkgContactRoleDuty() && !isBookingContactAvaliable()) {
        $.prompt("Please select atleast one booking contact name");
        return false;
    } else {
        return true;
    }
}
function okPrompt(path, fileNumberId, moduleId, moduleName, callBackFlag) {
    $.prompt("Hazmat info will be required prior to releasing this shipment", {
        buttons: {
            Ok: 0
        },
        submit: function (v) {
            if (v === 0) {
                changeMenuTabs(path, fileNumberId, moduleId, moduleName, callBackFlag);
            }
        }
    });
}

function hazValidateBackButton(path, fileNumberId, moduleId, moduleName, callBackFlag, buttonValue, unitSsId) {
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkHaz",
            param1: fileNumberId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data) {
                if (moduleName === 'Imports') {
                    $.prompt('UnHazmat No is required');
                } else {
                    okPrompt(path, fileNumberId, moduleId, moduleName, callBackFlag);
                }
            } else if (($("#lcl3p-container tr").length) === 0) {
                $.prompt('Please Enter Atleast One Hot Code');
            } else if (($("#lcl3p-container tr").length) !== 0) {
                $("#methodName").val('isCheckHazmatCode');
                var params = $("#lclBookingForm").serialize();
                params += "&fileId=" + $('#fileNumberId').val();
                $.post($("#lclBookingForm").attr("action"), params,
                        function (data) {
                            if (!data) {
                                $.prompt('Please Enter Atleast One Hazmat Hot Code');
                            } else if (buttonValue === 'EciVoyage') {
                                changeMenuTabs(path, fileNumberId, "IMP VOYAGE", moduleName, "OPEN IMP VOYAGE", unitSsId);
                            } else {
                                changeMenuTabs(path, fileNumberId, moduleId, moduleName, callBackFlag);
                            }
                        });
            } else if (buttonValue === 'EciVoyage') {
                changeMenuTabs(path, fileNumberId, "IMP VOYAGE", moduleName, "OPEN IMP VOYAGE", unitSsId);
            } else {
                changeMenuTabs(path, fileNumberId, moduleId, moduleName, callBackFlag);
            }
        }
    });
}
function holdUnHold(fileId) {
    if (fileId === '' || fileId === '0') {
        $.prompt("Please Save Booking");
    } else {
        var removeDrHold = $('#removeDrHold').val();
        var hold = $('#hold').val();
        var holdUnHold = false;
        if (hold === 'Y') {
            holdUnHold = true;
        }
        if (hold === 'Y' && removeDrHold === 'false') {
            return false;
        } else {
            var releaseClass = $("#lclReleaseButton1").attr('class') === 'green-background' ? true : false;
            var releaseClass2 = $("#lclReleaseButton2").attr('class') === 'green-background' ? true : false;
            if (hold !== 'Y' && (releaseClass || releaseClass2)) {
                $.prompt("You cannot put on Hold because DR is released. Please un-release first.");
                return false;
            }
            var fileNumber = $('#fileNumber').val();
            var href = path + "/lclRemarks.do?methodName=displayOnHold&fileNumberId=" +
                    fileId + "&fileNumber=" + fileNumber + "&hold=" + holdUnHold;
            $.colorbox({
                href: href,
                iframe: true,
                width: "45%",
                height: "60%",
                title: "Hold/UnHold",
                onClosed: function () {

                }
            });
        }
    }
}

function lclEdiAndKnDataconvertToBL(path, fileNumberId, moduleId, buttonValue, lclEdiData) {
    window.parent.showLoading();
    $("#methodName").val("convertToBL");
    var moduleName = $('#moduleName').val();
    var params = $("#lclBookingForm").serialize();
    params += "&ediButton=" + buttonValue;
    $.post($("#lclBookingForm").attr("action"), params, function () {
        window.parent.closePreloading();
        window.parent.changeLclChilds(path, fileNumberId, moduleId, moduleName);
    });
}
function validateNoEeiAes(fileId) {
    var noEEiMessage = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "validateNoEeiAes",
            param1: fileId,
            param2: "booking",
            dataType: "json"

        },
        async: false,
        success: function (data) {
            noEEiMessage = data;
        }
    });
    return noEEiMessage;
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
                        param2: "booking",
                        request: true

                    },
                    success: function (data) {

                        if (data === 'true') {
                            $('#Baes').addClass("green-background");
                            $('#aesB').addClass("green-background");
                        }
                    }
                });
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
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
                                param2: "booking",
                                request: true

                            },
                            success: function (data) {

                                if (data === 'true') {

                                    $('#Baes').addClass("green-background");
                                    $('#aesB').addClass("green-background");
                                }
                            }
                        });
                    } else if (v === 2) {
                        $.prompt.close();
                    }
                }
            });
        } else {
            noEeiAes(fileId);
        }
    }
}
function preventtoAddNoEeiaes(fileId) {
    var noEEiMessage = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "preventtoAddNoEeiaes",
            param1: fileId,
            param2: "booking",
            dataType: "json"

        },
        async: false,
        success: function (data) {
            noEEiMessage = data;
        }
    });
    return noEEiMessage;
}

function getZeroAlert(fileId) {
    var disposition = $("#exportDisposition").val();
    var flag = true;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO",
            methodName: "getWeightVolume",
            param1: fileId,
            param2: disposition,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            var originalValue = false;
            if (data.includes("1")) {
                originalValue = true;
            }
            if (originalValue === true) {
                $.prompt("Piece,CFT,CBM,LBS and KGS should be greater than zero");
                flag = false;
            }
        }
    });
    return flag;
}

function getConsolidateFiles(fileId) {
    var consFile = "";
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getconsolidatedfiles",
            param1: fileId
        },
        async: false,
        success: function (data) {
            if (data != null && data != "") {
                consFile = data;
            }
        }
    });
}

function noBLRequiredforBooking() {
    var isCheckedNoBLRequired = $("#noBLRequired").is(":checked");
    var fileNumberId = $('#fileNumberId').val();
    var userId = $('#loginUserId').val();
    // alert(isCheckedNoBLRequired + " filenumberid  " + fileNumberId);
    // alert("userId " + userId);
    $.prompt("Are you sure?", {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                isCheckedNoBLRequired === true ? $('#noBLRequired').attr("checked", true) : $('#noBLRequired').attr("checked", false);
                $('#noBLRequired').val(isCheckedNoBLRequired);
                if (fileNumberId !== "" && fileNumberId !== null && fileNumberId !== undefined) {
                    var isCheckedOrUnCheckedRemarks = isCheckedNoBLRequired === true ? "INSERTED -> No B/L Required -> was Checked " : "INSERTED -> No B/L Required -> was UnChecked";
                    //alert(isCheckedOrUnCheckedRemarks);
                    //  alert("got file no.");
                    isCheckedNoBLRequired = isCheckedNoBLRequired === true ? "1" : "0";
                    // alert("field values " + isCheckedNoBLRequired );
                    $.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO",
                            methodName: "updateFields",
                            param1: fileNumberId,
                            param2: "no_bl_required",
                            param3: isCheckedNoBLRequired,
                            param4: userId,
                            dataType: "json"
                        },
                        async: false,
                        success: function (data) {
                            $.ajaxx({
                                dataType: "json",
                                data: {
                                    className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO",
                                    methodName: "insertLclRemark",
                                    param1: fileNumberId,
                                    param2: "auto",
                                    param3: isCheckedOrUnCheckedRemarks,
                                    param4: userId,
                                    dataType: "json"
                                },
                                async: false,
                                success: function (data) {
                                }
                            });
                        }
                    });
                }
            } else {
                isCheckedNoBLRequired === true ? $('#noBLRequired').attr("checked", false) : $('#noBLRequired').attr("checked", true);
                $.prompt.close();
            }
        }
    });
}
function hsCodeForConvert() {
    var fileId = $('#fileNumberId').val();
    var podUnlocationcode = $('#podUnlocationcode').val();
    var flag = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "ischeckConvertBl",
            param1: fileId,
            param2: podUnlocationcode,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}
function consolidateForConvertBl() {
    var fileId = $('#fileNumberId').val();
    var flag = "";
    var isConsolidate = $("#conosolidateId tr").length > 1 ? true : false;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO",
            methodName: "getConsFileNumbersAndFileId",
            param1: fileId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data !== '' && data !== null) {
                if (isConsolidate !== true) {
                    flag = ("Piece,CFT,CBM,LBS and KGS should be greater than zero");
                }
                else {
                    flag = ("Please check Piece,CFT,CBM,LBS and KGS for the following files<span class='red'> " + data + "</span> it should greater than zero");

                }
            }
        }
    });
    return flag;

}
function defaultNotifyParty() {
    $("#notifyName").val("SAME AS CONSIGNEE");
    $('#newNotify').is(':checked');
}