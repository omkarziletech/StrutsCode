var path = "/" + window.location.pathname.split('/')[1];

$(document).ready(function () {
    displaydispoDesc();
    displayUnitAddressDetails();
    lockImpTranshipmentBkg();
    toggleGeneralInformation();
    toggleUpcomingSailings();
    checkTranshipment('status');
    showIpiCfsAddress();
    setShipperTooltip();
    setConsigneeTooltip();
    setNotyTooltip();
    setNotify2Tooltip();//tootipdetails for Notify2
    showNotify2ManualCheck();
    setBillToParty();//based on billing type enable or disable radio button
    isSubHouseCheckBox();
    isCheckedEditTpForConsignee();
    ischeckedEditTpNoty();
    disablePostDisableToAcct();
    checkETF_FD();
    setIPICFSstate();
    checkDisposition();
    checkDeleteIconForCommodity();
    toggleDoorDeliveryComment();
// disablecfsChargeBtn();
});

//Hide delete icon in commodity Tab
function checkDeleteIconForCommodity() {
    $(".spReferenceNo").each(function () {
        if (($(this).text()) !== "" || ($(this).text()) !== null) {
            $('#deleteIcon').hide();
        }
    });
    $(".accountStatus").each(function () {
        if ((($(this).text().trim().trim()) === "AP") || (($(this).text().trim().trim()) === "IP") || (($(this).text().trim().trim()) === "DS")) {
            $('#deleteIcon').hide();
        }
    });
}
// generalInfomation tab  hide or show
function toggleGeneralInformation() {
    $('#generalInformation').slideToggle();
    var disp = document.getElementById('hideG').innerHTML;
    if (disp === "Click to Expand" || disp === "") {
        document.getElementById('hideG').innerHTML = "";
        document.getElementById('hideG').innerHTML = "Click to Hide";
    }
    else {
        document.getElementById('hideG').innerHTML = "";
        document.getElementById('hideG').innerHTML = "Click to Expand";
    }
}
//Import Details Tab hide or Expand
function toggleImportDetails() {
    $('#importDetails').slideToggle();
    var disp = document.getElementById('hide').innerHTML;
    if (disp === "Click to Hide" || disp === "") {
        document.getElementById('hide').innerHTML = "";
        document.getElementById('hide').innerHTML = "Click to Expand";
    }
    else {
        document.getElementById('hide').innerHTML = "";
        document.getElementById('hide').innerHTML = "Click to Hide";
    }
}

// upcoming Sailings tab  hide or show
function toggleUpcomingSailings() {
    var disp = document.getElementById('upcomingSailing').style.display;
    var shipment = $('input:radio[name=transShipMent]:checked').val();
    if ((disp === "block" || disp === "") && shipment === 'Y') {
        document.getElementById('exp').innerHTML = "";
        document.getElementById('col').innerHTML = "Click to Expand";
        $('#upcomingSailing').slideToggle();
    }
    else {
        document.getElementById('col').innerHTML = "";
        document.getElementById('exp').innerHTML = "Click to Hide";
        $('#upcomingSailing').slideToggle();
    }
}
//transhipment logic
function checkTranshipment(fd) {
    var auditedBy = $('#auditedBy').val();
    var closedBy = $('#closedBy').val();
    var headerId = $("#headerId").val();
    if (closedBy !== '' || auditedBy !== '') {
        $('#transShipMentYes').attr('disabled', true);
        $('#transShipMentNo').attr('disabled', true);
        $('#transShipMent').val($('#hiddenTranshipment').val() === "true" ? "Y" : "N");
    }
    if ($('#podUnlocationcode').val() !== $('#unlocationCode').val() && $('#podUnlocationcode').val() !== "") {
        //        $('#goDate').val('');
        $("#stGeorgeAccount").removeClass("textlabelsBoldForTextBoxDisabledLook");
        $("#stGeorgeAccount").addClass("textlabelsBoldForTextBox");
        $("#ipiCFSEdit").show();
        $("#stGeorgeAccount").attr("readonly", false);
        //        $("#lastFd").val('');
        if (headerId !== '') {
            $("#lastFd").addClass("textlabelsBoldForTextBoxDisabledLook");
            $("#lastFd").attr("readonly", true);
            $('#lastFdImg').hide();
        } else {
            $("#lastFd").removeClass("textlabelsBoldForTextBoxDisabledLook");
            $("#lastFd").attr("readonly", false);
            $('#lastFdImg').show();
        }
        $('#fd-container').show();
        $('#unitItNo').val('');
        $('#unitItDate').val('');
        $('#unitItPort').val('');
        $('#ipiATD-hide-show').show();
    }
    else {
        if ($('#stripDate').val() !== null && $('#stripDate').val() !== '') {
            $('#goDate').val(addDate($('#stripDate').val(), 14));
        }
        $("#stGeorgeAccount").addClass("textlabelsBoldForTextBoxDisabledLook");
        $("#stGeorgeAccount").attr("readonly", true);
        $("#ipiCFSEdit").hide();
        $('#stGeorgeAccount').val('');
        $('#stGeorgeAccountNo').val('');
        $('#stGeorgeAddress').val('');
        $("#lastFd").val($('#fdDate').val());
        $("#unitItNo").val($('#impUnitNoHidd').val());
        $("#unitItDate").val($('#impUnitDateHidd').val());
        $("#unitItPort").val($('#impUnitPortHidd').val());
        $("#lastFd").removeClass("textlabelsBoldForTextBoxDisabledLook");
        $("#lastFd").attr("readonly", false);
        $('#lastFdImg').show();
        $('#etaFDDate').val('');
        $('#fd-container').hide();
        $('#ipiATD-hide-show').hide();
    }
    var shipment = $('input:radio[name=transShipMent]:checked').val();
    if (shipment === 'Y') {
        $('#polLabel').attr('width', '20%');
        $('#polDojo').attr('width', '10%');
        if (fd !== 'status' && (fd === undefined || fd === '')) {
            var foreignDischarge = $('#finalDestinationR').val();
            var foreigndischarge = foreignDischarge.substring(foreignDischarge.indexOf('(') + 1, foreignDischarge.indexOf(')'));
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "defaultDestinationImportRemarks",
                    param1: foreigndischarge,
                    dataType: "json"
                },
                success: function (data) {
                    if (data[0] !== undefined && data[0] !== "" && data[0] !== null) {
                        $('#specialRemarks').val(data[0]);
                    }
                    if (data[1] !== undefined && data[1] !== "" && data[1] !== null) {
                        $('#internalRemarks').val(data[1]);
                    }
                    if (data[2] !== undefined && data[2] !== "" && data[2] !== null) {
                        $('#portGriRemarks').val(data[2]);
                    }
                }
            });
        }
        $('#lclImpRrelease').show();
        $('#lclImpRreleasebot').show();
        $('#usaExit').show();
        $('#portDiscg').show();
        $('#portSpecialRemarks').show();
        $('#portInternalRemarks').show();
        $('#griRemarks').show();
        $('#upcomingImpSection').show();
        $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
        $('#remarksSection').addClass("black-border");
        $('#regionalRemark').addClass("black-border");
        $('#defaultAgentDetails').show();
        $('#m').show();
        $('#agentNumberDetails').show();
        $('#regionalRemark').show();
        impUpcomingTab();
    }
    else {
        $('#polLabel').attr('width', '10%');
        $('#polDojo').attr('width', '20%');
        $('#defaultAgentDetails').hide();//Default Agent Details
        $('#m').hide();//Agent Name,Number Details
        $('#agentNumberDetails').hide();//Agent Name,Number Details
        $('#transSailing').text('');
        $('#masterScheduleNo').val('');
        $('#upcomingImpSection').hide();
        $('#lclImpRrelease').hide();
        $('#lclImpRreleasebot').hide();
        $('#remarksSection').removeClass("black-border");
        $('#usaExit').hide();
        $('#portDiscg').hide();
        $('#portSpecialRemarks').val('');
        $('#portInternalRemarks').val('');
        $('#griRemarks').val('');
        $('#portSpecialRemarks').hide();
        $('#portInternalRemarks').hide();
        $('#griRemarks').hide();
        $('#finalDestinationR').attr('alt', 'DEST_UNLOC');
        $('#regionalRemark').removeClass("black-border");
        $('#regionalRemark').hide();
    }
}

//Import Payments Pop Up
function impPayments(path, fileNumberId, fileNumber, fileNumberStatus, headerId) {
    var billToCode = $('#hiddenBillToCode').val();
    var href = path + "/lclImportPayment.do?methodName=display&fileNumberId=" + fileNumberId + "&screenName=LCL FILE&moduleId=" + fileNumber;
    href = href + "&fileNumber=" + fileNumber + "&headerId=" + headerId + "&billToCode=" + billToCode + "&moduleName=Imports";
    $("#impPayments").attr("href", href);
    $("#impPayments").colorbox({
        iframe: true,
        width: "70%",
        height: "70%",
        title: "Payments",
        onClosed: function () {
            var formChageFlag = $('#formChangeFlag').val();
            if (formChageFlag === "true") {
                showProgressBar();
                var fileNumberId = $('#fileNumberId').val();
                $("#methodName").val("refreshChargesTab");
                var params = $("#lclBookingForm").serialize();
                params += "&fileNumberId=" + fileNumberId + "&fileNumberStatus="
                        + fileNumberStatus + "&headerId=" + headerId + "&unitSsCollectType=" + $('#unitCollect').val();
                $.post($("#lclBookingForm").attr("action"), params,
                        function (data) {
                            $("#chargeDesc").html(data);
                            $("#chargeDesc", window.parent.document).html(data);
                            $("#chargeDesc").find("[title != '']").not("link").tooltip();
                        });
                hideProgressBar();
            }
        }
    });
}
//Unit Notes Pop Up
function importUnitNotes(path) {
    var unitSsId = $('#unitSsId').val();
    var headerId = $('#headerId').val();
    var href = path + "/lclImportUnitNotes.do?methodName=displayNotes&unitSsId=" + unitSsId + "&headerId=" + headerId + "&unitId=" + $('#unitId').val() + "&unitNo=" + $('#impUnitNo').val() + "&actions=4";
    $.colorbox({
        iframe: true,
        href: href,
        width: "60%",
        height: "67%",
        title: "Unit Notes"
    });
}
// Freight Release Pop Up
function impFreightRelease(path, fileId) {
    if (fileId !== null && fileId !== "" && fileId !== '0') {
        var expressRelease = $('input:radio[name=expressReleaseClasuse]:checked').val();
        var entryNo = $('#entryNo').val();
        var href = path + "/importRelease.do?methodName=display&moduleName=ImpBooking&fileNumberId=" + fileId + "&expressRelease=" + expressRelease + "&entryNo=" + entryNo;
        $("#freightRelease").attr("href", href);
        $("#freightRelease").colorbox({
            iframe: true,
            width: "80%",
            height: "60%",
            title: "Freight Release"
        });
    }
    else {
        sampleAlert("Please Save Booking");
    }
}
function submitAjaxFormForImpAgent(methodName, formName, selector, id) {// refresh origin Agent details based on POL Code
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&id=" + id;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                if ($("#agentFlagCountValue").val() === "") {
                    filldefaultAgentForImports();
                } else {
                    $("#supplierNameOrg").val('');
                    $("#supplierCode").val('');
                }
                hideProgressBar();
            });
    $('#supplierNameOrg').val(parent.$('#agentName').val());
}
function filldefaultAgentForImports() {// fill Default Agent based POL
    var moduleName = $('#moduleName').val();
    var portOfLoading = document.getElementById('portOfLoading').value;
    var polUnlocation = portOfLoading.substring(portOfLoading.indexOf('(') + 1, portOfLoading.indexOf(')'));
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getDefaultAgentForLcl",
            param1: polUnlocation,
            param2: 'I',
            dataType: "json"
        },
        success: function (data) {
            if (data[3] === 'Y') {
                $.prompt("This customer is disabled and merged with" + data[4] !== null ? data[4] : "");
                $("#supplierNameOrg").val('');
                $("#supplierCode").val('');
            } else {
                if (data[0] !== undefined && data[0] !== "" && data[0] !== null) {
                    $("#supplierCode").val(data[0]);
                    $("#supplierNameOrg").val(data[1]);
                    if (moduleName === 'Imports') {
                        updateBussinessUnit();
                    }
                }
            }
        }
    });
}
//function removeBorder()
//{
//    if (!($("#supplierNameOrg").val() == null || $("#supplierNameOrg").val() == "" || $("#supplierNameOrg").val() == undefined)) {
//        $("#supplierNameOrg").css("border-color", "");
//    }
//}
//update Contact Details
function openLclContactInfo(path, party) {
    var vendorNo = "";
    var vendorName = "";
    var subtype = "";
    if (party === 'Supplier') {
        vendorNo = $("#supplierCode").val();
        vendorName = $("#supplierName").val();
        subtype = 'LCL_IMPORT_SUPPLIER';
    } else if (party === 'Shipper') {
        vendorNo = $("#shipperCode").val();
        vendorName = $("#shipperName").val();
        subtype = 'LCL_IMPORT_SHIPPER';
    } else if (party === 'Consignee') {
        vendorNo = $("#consigneeCode").val();
        vendorName = $("#consigneeName").val();
        subtype = 'LCL_IMPORT_CONSIGNEE';
    } else if (party === 'Notify') {
        vendorNo = $("#notifyCode").val();
        vendorName = $("#notifyName").val();
        subtype = 'LCL_IMPORT_NOTIFY';
    } else if (party === '2ndNotify') {
        vendorNo = $("#notify2Code").val();
        vendorName = $("#notify2Name").val();
        subtype = 'LCL_IMPORT_NOTIFY2';
    }
    vendorName = vendorName.replace("&", "amp;");
    var href = path + "/lclContactDetails.do?methodName=display&vendorName=" + vendorName + "&vendorNo=" + vendorNo + "&vendorType=" + subtype;
    $(".contactR").attr("href", href);
    $(".contactR").colorbox({
        iframe: true,
        width: "70%",
        height: "70%",
        title: "Contact"
    });
}
//Create New TP Method
function createNewTPAcct(path, vendorflag, acctType, checkBoxValue, vendorName, address, city, state, unLocCode, phone, fax, email, zipCode, salesPersonCode, coloadRetailRadio) {
    var errorMsg = "";
    if ($('#' + checkBoxValue).is(":checked")) {
        if ($('#' + vendorName).val() === null || $('#' + vendorName).val() === "") {
            errorMsg += "-> VendorName is required" + "</br>";
        }
        if ($('#' + address).val() === null || $('#' + address).val() === "") {
            errorMsg += "-> Address is required" + "</br>";
        }
        if ($('#' + city).val() === null || $('#' + city).val() === "") {
            errorMsg += "-> City is required" + "</br>";
        }
        if (errorMsg === "") {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                    methodName: "existingImpCustomerList",
                    forward: "/jsps/Tradingpartnermaintainance/tradingPartnerExistsCustomerList.jsp",
                    param1: $('#' + vendorName).val(),
                    param2: $('#' + city).val()
                },
                preloading: true,
                success: function (data) {
                    if (null != data && data != "") {
                        showPopUp();
                        createHTMLElement("div", "docListDiv", "60%", "50%", document.body);
                        $("#docListDiv").html(data);
                        $('#tpFlag').val(vendorflag);
                    } else {
                        addNewTradingPartner(path, vendorflag, acctType, vendorName, address, city, state, unLocCode, phone, fax, email, zipCode, salesPersonCode, coloadRetailRadio);
                    }
                }
            });
        } else {
            sampleAlert(errorMsg);
        }
    }
}
// don't remove this code
//   window.parent.GB_showFullScreen("Trading Partner",
function createNewTp(path) {
    var tpFlag = $('#tpFlag').val();
    var acctNo = $('#tpacctNo').val();
    if (tpFlag === "LCL_IMPORT_SHIPPER") {
        if (acctNo !== "" && acctNo !== null) {
            createConsigneeAcctShipperAcct(acctNo, 'S', 'S');
        } else {
            addNewTradingPartner(path, tpFlag, 'S', 'dupShipperName', 'shipperAddress', 'shipperCity', 'shipperState',
                    'shipUnlocCode', 'shipperPhone', 'shipperFax', 'shipperEmail', 'shipperZip', 'shipperSalesPersonCode', 'shipperColoadRetailRadio');
        }
    } else if (tpFlag === "LCL_IMPORT_CONSIGNEE") {
        if (acctNo !== "" && acctNo !== null) {
            createConsigneeAcctShipperAcct(acctNo, 'C', 'C');
        } else {
            addNewTradingPartner(path, tpFlag, 'C', 'dupConsigneeName', 'consigneeAddress', 'consigneeCity', 'consigneeState',
                    'consigneeUnLocCode', 'consigneePhone', 'consigneeFax', 'consigneeEmail', 'consigneeZip', 'consigneeSalesPersonCode', 'consigneeColoadRetailRadio');
        }
    } else if (tpFlag === "LCL_IMPORT_NOTIFY") {
        if (acctNo !== "" && acctNo !== null) {
            createConsigneeAcctShipperAcct(acctNo, 'N', 'C');
        } else {
            addNewTradingPartner(path, tpFlag, 'C', 'dupNotifyName', 'notifyAddress', 'notifyCity', 'notifyState',
                    'notifyUnLocCode', 'notifyPhone', 'notifyFax', 'notifyEmail', 'notifyZip', 'notifySalesPersonCode', 'notifyColoadRetailRadio');
        }
    } else if (tpFlag === "LCL_IMPORT_NOTIFY2") {
        if (acctNo !== "" && acctNo !== null) {
            createConsigneeAcctShipperAcct(acctNo, 'N2', 'C');
        } else {
            addNewTradingPartner(path, tpFlag, 'C', 'dupNotify2Name', 'notify2Address', 'notify2City', 'notify2State',
                    'notify2UnLocCode', 'notify2Phone', 'notify2Fax', 'notify2Email', 'notify2Zip', 'notify2SalesPersonCode', 'notify2ColoadRetailRadio');
        }
    }
    cancelAdd();
}
function addNewTradingPartner(path, vendorflag, acctType, vendorName, address, city, state, unLocCode, phone, fax, email, zipCode, salesPersonCode, coloadRetailRadio) {
    var txt = "Are you sure you want to Create a New Account";
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                var href = path + "/lclNewTPDetails.do?buttonValue=" + vendorflag + "&accountName=" + $('#' + vendorName).val() + "&accountType=" + acctType + "&address=" + encodeURIComponent($('#' + address).val());
                href = href + "&address1=" + encodeURIComponent($('#' + address).val()) + "&city=" + $('#' + city).val() + "&state=" + $('#' + state).val() + "&zip=" + $('#' + zipCode).val();
                href = href + "&fax=" + $('#' + fax).val() + "&phone=" + $('#' + phone).val() + "&email=" + $('#' + email).val() + "&unLocCode=" + $('#' + unLocCode).val() + "&salesCode=" + $("#" + salesPersonCode).val();
                href = href + "&importQuoteColoadRetail=" + $('#' + coloadRetailRadio).val();
                $.colorbox({
                    iframe: true,
                    width: "30%",
                    height: "30%",
                    href: href,
                    title: "Trading Partner"
                });
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
//set New TP Values
function setNewShipperInfoDetails(accountName, accountNo, callFrom) {
    if (callFrom === "LCL_IMPORT_SHIPPER") {
        $("#dupShipperName").val('');
        $('#newShipper').attr('checked', false);
        $("#manualShipper").hide();
        $("#dojoShipper").show();
        $('#shipperName').val(accountName);
        $('#shipperCode').val(accountNo);
        setToolTipTp('Shipper', 'shipperCode', 'shipperName', 'shipper');
    } else if (callFrom === "LCL_IMPORT_CONSIGNEE") {
        $("#dupConsigneeName").val('');
        $('#newConsignee').attr('checked', false);
        $("#manualConsignee").hide();
        $("#dojoConsignee").show();
        $('#consigneeName').val(accountName);
        $('#consigneeCode').val(accountNo);
        setToolTipTp('Consignee', 'consigneeCode', 'consigneeName', 'consContact');
    } else if (callFrom === "LCL_IMPORT_NOTIFY") {
        $("#dupNotifyName").val('');
        $('#newNotify').attr('checked', false);
        $("#manualNotify").hide();
        $("#dojoNotify").show();
        $('#notifyName').val(accountName);
        $('#notifyCode').val(accountNo);
        setToolTipTp('Notify', 'notifyCode', 'notifyName', 'noty');
    } else if (callFrom === "LCL_IMPORT_NOTIFY2") {
        $("#dupNotify2Name").val('');
        $('#newNotify2').attr('checked', false);
        $("#manualNotify2").hide();
        $("#dojoNotify2").show();
        $('#notify2Name').val(accountName);
        $('#notify2Code').val(accountNo);
        setToolTipTp('Notify2', 'notify2Code', 'notify2Name', 'notify2');
    }
}
//display TP Details for Shipper,Consignee & Notify
function displayTradingDetails(path, fileId, acctNo, acctName, maualacct, acctType, address, city, state, country, phone, fax, email, zipCode, checkBoxValue, salesPersonCode, coloadRetailRadio) {
    if (fileId !== null && fileId !== "" && fileId !== '0') {
        var vendorName = "";
        if ($('#' + acctName).val() !== '') {
            vendorName = $('#' + acctName).val();
        } else {
            vendorName = $('#' + maualacct).val();
        }
        var newCheck = $('#' + checkBoxValue).is(":checked");
        var href = path + "/tradingContact.do?methodName=display&fileId=" + fileId + "&acctNo=" + $('#' + acctNo).val() + "&acctName=" + vendorName + "&vendorType=" + acctType + "&fileType=Booking&checkValue=" + newCheck;
        href = href + "&address=" + encodeURIComponent($('#' + address).val()) + "&city=" + $('#' + city).val() + "&state=" + $('#' + state).val() + "&zip=" + $('#' + zipCode).val();
        href = href + "&fax=" + $('#' + fax).val() + "&phone=" + $('#' + phone).val() + "&email=" + $('#' + email).val() + "&country=" + $('#' + country).val() + "&salesPersonCode=" + $("#" + salesPersonCode).val() + "&importQuoteColoadRetail=" + $("#" + coloadRetailRadio).val();
        $(".tradingDetails").attr("href", href);
        $(".tradingDetails").colorbox({
            iframe: true,
            width: "60%",
            height: "50%",
            title: "Address Details"
        });
    } else {
        sampleAlert("Please Save Booking");
    }
}
//Display Unit Disposition Details
function displaydispoDesc() {
    if ($("#impEciVoyage").val() !== null && $("#impEciVoyage").val() !== "") {
        JToolTip('#impDisposition', $('#dispositionToolTip').val(), 400);
    }
    else {
        $("#dispoId").text("DATA");
    }
}

function copyToClipboard(str) {
    window.clipboardData.clearData("Text");
    window.clipboardData.setData('Text', str);
}

//Display Unit Terminal Details from Unit
function displayUnitAddressDetails() {
    if ($('#impUnitWareaddress').val() !== null && $('#impUnitWareaddress').val() !== "") {
        var unitDetails = "<table><tr><td  style='color:green;font-weight:bold;'>Unit Terminal Whse</td></tr></table>";
        unitDetails += "<tr><td><font size='2' color:black>" + $('#impUnitWareaddress').val() + "</font></td></tr></table>";
        JToolTip('#unitWareHsDetails', unitDetails, 220);
    }
    return true;
}
//show IPI Address
function showIpiCfsAddress() {
    var acctNo = $('#stGeorgeAccountNo').val();
    if (acctNo !== "" && acctNo !== null && $('#podUnlocationcode').val() !== $('#unlocationCode').val()) {
        var ipiCFSAddress = "";
        if ($('#ipiCfsFirmsCode').val() !== null && $('#ipiCfsFirmsCode').val() !== '') {
            ipiCFSAddress += "FIRMS CODE:" + $('#ipiCfsFirmsCode').val() + "\n";
        }
        if ($('#ipiCfsSearchCoName').val() !== null && $('#ipiCfsSearchCoName').val() !== '') {
            ipiCFSAddress += $('#ipiCfsSearchCoName').val() + "\n";
        }
        if ($('#ipiCfsSearchAdd').val() !== null && $('#ipiCfsSearchAdd').val() !== '') {
            ipiCFSAddress += $('#ipiCfsSearchAdd').val() + "\n";
        }
        if ($('#ipiCfsSearchCity').val() !== null && $('#ipiCfsSearchCity').val() !== '') {
            ipiCFSAddress += $('#ipiCfsSearchCity').val() + ",";
        }
        if ($('#ipiCfsSearchState').val() !== null && $('#ipiCfsSearchState').val() !== '') {
            ipiCFSAddress += $('#ipiCfsSearchState').val() + '-';
        }
        if ($('#ipiCfsSearchZip').val() !== null && $('#ipiCfsSearchZip').val() !== '') {
            ipiCFSAddress += $('#ipiCfsSearchZip').val() + "\n";
        }
        if ($('#ipiCfsSearchPhone').val() !== null && $('#ipiCfsSearchPhone').val() !== '') {
            ipiCFSAddress += "Phone:" + $('#ipiCfsSearchPhone').val() + "\n";
        }
        if ($('#ipiCfsSearchFax').val() !== null && $('#ipiCfsSearchFax').val() !== '') {
            ipiCFSAddress += "Fax:" + $('#ipiCfsSearchFax').val();
        }
        $('#stGeorgeAddress').val(ipiCFSAddress);
    }
}
function setToolTipTp(headingName, acctCode, acctName, toolTip) {//set ToolTip trading partner
    var moduleName = $('#moduleName').val();

    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getContactDetails",
            param1: headingName,
            param2: $('#' + acctCode).val(),
            param3: $('#' + acctName).val(),
            param4: moduleName
        },
        success: function (data) {
            if (headingName === "Notify" || headingName === "Notify2") {
                ImportsJToolTip('#' + toolTip, data, 1000, 'topRight');
            }
            else if (headingName === "Shipper") {
                ImportsJToolTip('#' + toolTip, data, 950, 'topLeft');
            } else if (headingName === "Consignee") {
                ImportsJToolTip('#' + toolTip, data, 1000, 'topMiddle');
            }
        }
    });
}
function setShipperTooltip() {
    ImportsJToolTip('#shipper', $('#shipperToolTip').val(), 950, 'topLeft');
}

function setConsigneeTooltip() {
    ImportsJToolTip('#consContact', $('#consigneeToolTip').val(), 1000, 'topMiddle');
}

function setNotyTooltip() {
    ImportsJToolTip('#noty', $('#notifyToolTip').val(), 1000, 'topRight');
}

function setNotify2Tooltip() {
    ImportsJToolTip('#notify2', $('#notify2ToolTip').val(), 1000, 'topRight');
}

function lockImpTranshipmentBkg() {//Check Transhipment and lock Import File
    var fileNumber = $('#fileNumber').val();
    var shipment = $('input:radio[name=transShipMent]:checked').val();
    var fileStatus = $('#fileStatus').val();
    if (fileNumber !== '' && shipment === 'Y') {
        if (fileStatus === 'R' || fileStatus === 'L') {
            var form = document.getElementById('lclBookingForm');
            var element;
            $('.button-style1').hide();
            $('.button-style2').hide();
            document.getElementById('showAstar').style.visibility = "hidden";
            $('#commodity1').hide();
            $('.whseDetail').hide();
            $('#PrintFaxEmail').show();
            $('#gobackbkg').show();
            $('#gobackbkg1').show();
            $('.notes').show();
            for (var i = 0; i < form.elements.length; i++) {
                element = form.elements[i];
                if (element.type === "text" || element.type === "textarea") {
                    element.style.backgroundColor = "#CCEBFF";
                    element.readOnly = true;
                    element.style.borderTop = "0px";
                    element.style.borderBottom = "0px";
                    element.style.borderRight = "0px";
                    element.style.borderLeft = "0px solid";
                }
                if (element.type === "select-one" || element.type === "radio" || element.type === "checkbox") {
                    //  element.style.border = 0;
                    element.disabled = true;
                }
            }
            var imgs = document.getElementsByTagName("img");
            for (var k = 0; k < imgs.length; k++) {
                if (imgs[k].id !== "trianleicon" && imgs[k].id !== "collpaseicon") {
                    imgs[k].style.visibility = "hidden";
                }
                if (imgs[k].id === "viewgif" || imgs[k].id === "viewgif1" || imgs[k].id === "viewgif2" || imgs[k].id === "triggerImg") {
                    imgs[k].style.visibility = "visible";
                }
            }
        }
    }
}
function calculateRatesPolFD() {
    var fileId = $('#fileNumberId').val();
    var status = $('#fileStatus').val();
    var fdUncCode = $("#unlocationCode").val();
    var oldFdUnlocationCode = $("#oldFdUnlocationCode").val();
    if (fileId !== "") {
        var origin = getOrigin();
        var destination = getDestination();
        var pol = document.getElementById('polCode').value;
        var pod = document.getElementById('podCode').value;
        var transShipment = $('input:radio[name=transShipMent]:checked').val();
        if (transShipment === 'Y' && status !== 'M') {
            submitAjaxForm("calculateImportCharges", '#lclBookingForm', '#chargeDesc', origin, destination, pol, pod, '', '', '');
        } else if (transShipment === 'N' && status !== 'M') {
            if ((fdUncCode !== "" && oldFdUnlocationCode !== "") && (fdUncCode !== oldFdUnlocationCode)) {
                yesPrompt(origin, destination, pol, pod);
            } else {
                submitAjaxForm("calculateImpPodFDRates", '#lclBookingForm', '#chargeDesc', origin, destination, pol, pod, '', '', '');
            }
        } else if ($('#fileStatus').val() === 'M') {
            var txt = "You are changing the Final Destination and the charges have already been posted. You must issue a CN in order to remove any charges to the customer";
            $.prompt(txt, {
                buttons: {
                    Continue: 1,
                    Abort: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        submitAjaxForm("calculateImpPodFDRates", '#lclBookingForm', '#chargeDesc', origin, destination, pol, pod, '', '', '');
                    }
                    else {
                        $("#finalDestinationR").val($("#oldFd").val());
                    }
                }
            });
        }
    }
}

function setBillToParty() {
    if ($('#fileStatus').val() !== '' && $('#fileStatus').val() !== 'B') {
        $('#billC').attr("disabled", true);//enable third,cons,notify
        $('#billT').attr("disabled", true);
        $('#billN').attr("disabled", true);
        $('#billA').attr("disabled", true);
        $('#billW').attr("disabled", true);
        $('#radioP').attr("disabled", true);
        $('#radioC').attr("disabled", true);
    } else {
        if ($('#radioP').attr('checked')) {
            $('#billA').attr("disabled", false);
            $('#billA').attr("checked", true);
            $('#billC').attr("disabled", true);
            $('#billC').removeAttr("checked");
            $('#billT').attr("disabled", true);
            $('#billT').removeAttr("checked");
            $('#billN').attr("disabled", true);
            $('#billN').removeAttr("checked");
        }
        else if ($('#radioC').attr('checked')) {
            $('#billA').attr("disabled", true);//disable agent
            $('#billA').removeAttr("checked");
            $('#billC').attr("disabled", false);//enable third,cons,notify
            $('#billT').attr("disabled", false);
            $('#billN').attr("disabled", false);
            if ($('#impCFSWareName').val() == null || $('#impCFSWareName').val() == "") {
                $('#billW').attr("disabled", true);
            }
        }
        // if ($('#billT').attr('checked')) {
        //   $('#thirdPartyname').removeClass('textlabelsBoldForTextBoxDisabledLook');
        //   $('#thirdPartyname').removeAttr('readonly');
        // } else {
        //  $('#thirdPartyname').val('');
        //   $('#thirdpartyaccountNo').val('');
        //    $('#thirdPartyname').addClass('textlabelsBoldForTextBoxDisabledLook');
        //    $('#thirdPartyname').attr('readonly', true);
        //  }
    }
}
function setBillingType(val) {
    if ($('#radioP').attr('checked')) {
        $('#billA').attr("disabled", false);//Enable Agent
        $('#billA').attr("checked", true);
        $('#billC').attr("disabled", true);//disable third,cons,notify
        $('#billC').removeAttr("checked");
        $('#billT').attr("disabled", true);
        $('#billT').removeAttr("checked");
        $('#billN').attr("disabled", true);
        $('#billN').removeAttr("checked");
        $('#billW').attr("disabled", true);
        $('#billW').removeAttr("checked");
    } else if ($('#radioC').attr('checked')) {
        $('#billA').attr("disabled", true);//disable agent
        $('#billA').removeAttr("checked");
        $('#billC').attr("disabled", false);//enable third,cons,notify
        $('#billT').attr("disabled", false);
        $('#billN').attr("disabled", false);
        $('#billW').attr("disabled", false);
        if ($('#impCFSWareName').val() == null || $('#impCFSWareName').val() == "") {
            $('#billW').attr("disabled", true);
        }
        if (val === "Default") {
            $('#billC').attr("checked", true);//Default Consignee enable
        }
    }
}
function updateBillToCode() {
    var billType = "";
    var billToParty = "";
    var existValue = $('#hiddenBillToCode').val();
    var thirdPartyName = $('#thirdPartyname').val();
    var thirdPartyAcctNo = $('#thirdpartyaccountNo').val();
    if ($('#fileNumberId').val() !== "") {
        if ($('#radioP').attr('checked')) {
            setClearIdValues("thirdpartyaccountNo", "thirdPartyname");//clear thirdpartyValues
            billType = 'P';
            billToParty = 'A';
        }
        if ($('#radioC').attr('checked')) {
            billType = 'C';
            if ($('#billC').attr('checked')) {
                setClearIdValues("thirdpartyaccountNo", "thirdPartyname");//clear thirdpartyValues
                billToParty = 'C';
            }
            if ($('#billN').attr('checked')) {
                setClearIdValues("thirdpartyaccountNo", "thirdPartyname");//clear thirdpartyValues
                billToParty = 'N';
            }
            if ($('#billT').attr('checked')) {
                billToParty = 'T';
            }
            if ($('#billW').attr('checked')) {
                billToParty = 'W';
            }
        }
        if (existValue !== billToParty) {
            var txt = "Are you sure you want to change the Terms?";
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        $.prompt.close();
                        submitAjaxFormforBill("updateBillToCode", "#lclBookingForm", "#chargeDesc", billType, billToParty);
                    } else if (v === 2) {
                        $('#thirdPartyname').val(thirdPartyName);
                        $('#thirdpartyaccountNo').val(thirdPartyAcctNo);
                        if (existValue === 'C') {
                            setEnableBillToCode();
                            $('#billC').attr("checked", true);
                            $('#radioC').attr("checked", true);
                        } else if (existValue === 'T') {
                            setEnableBillToCode();
                            $('#radioC').attr("checked", true);
                            $('#billT').attr("checked", true);
                        } else if (existValue === 'A') {
                            $('#billA').attr("disabled", false);
                            $('#billT').attr("disabled", true);
                            $('#billN').attr("disabled", true);
                            $('#billC').attr("disabled", true);
                            $('#billA').attr("checked", true);
                            $('#radioP').attr("checked", true);
                        } else if (existValue === 'N') {
                            setEnableBillToCode();
                            $('#radioC').attr("checked", true);
                            $('#billN').attr("checked", true);
                        }
                        $.prompt.close();
                    }
                }
            });
        }
    }
}
function setEnableBillToCode() {
    $('#billC').attr("disabled", false);
    $('#billT').attr("disabled", false);
    $('#billN').attr("disabled", false);
    $('#billA').attr("disabled", true);
}
function submitAjaxFormforBill(methodName, formName, selector, billType, billToParty) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&pcBothImports=" + billType + "&billtoCodeImports=" + billToParty + "&whsePhone=" + $('#hiddenBillToCode').val() + "&existBillingType=" + $('#existBillingType').val();
    params += "&transhipment=" + $('input:radio[name=transShipMent]:checked').val();
    $.post($(formName).attr("action"), params, function (data) {
        $(selector).html(data);
        $(selector, window.parent.document).html(data);
        hideProgressBar();
    });
    $('#hiddenBillToCode').val(billToParty);
    $('#existBillingType').val(billType);
}
function setClearIdValues(id1, id2) {
    $('#' + id1).val('');
    $('#' + id2).val('');
}
function setDisableStyle(id1, id2) {
    $('#' + id1).val('');
    $('#' + id2).val('');
    $('#' + id1).addClass('textlabelsBoldForTextBoxDisabledLook');
    $('#' + id1).attr('readonly', true);
}
function setDisableTextBox(id1) {
    $('#' + id1).addClass('textlabelsBoldForTextBoxDisabledLook');
    $('#' + id1).attr('readonly', true);
}
function setEnableStyle(id) {
    $('#' + id).removeClass('textlabelsBoldForTextBoxDisabledLook');
    $('#' + id).removeAttr('readonly');
}
function showScanOrAttach(path, vendorNo, invoiceNo, fileNumber) {
    var href = path + "/lclScanViewDetails.do?methodName=displayScanDetails&vendorNo=" + vendorNo + "&invoiceNo=" + invoiceNo;
    href = href + "&fileNumber=" + fileNumber;
    $(".scanView").attr("href", href);
    $(".scanView").colorbox({
        iframe: true,
        width: "70%",
        height: "60%",
        title: "Scan View"
    });
}
function newCheckNotify2() {
    if ($('#newNotify2').is(":checked")) {
        $("#dojoNotify2").hide();
        $("#manualNotify2").show();
        clearNotify2Values();
    } else {
        $("#dojoNotify2").show();
        $("#manualNotify2").hide();
        clearNotify2Values();
    }
}
function clearNotify2Values(path) {// clear all notify2 id values
    $("#notify2Name").val('');
    $("#dupNotify2Name").val('');
    $("#notify2Code").val('');
    $("#notify2Address").val('');
    $("#notify2City").val('');
    $("#notify2State").val('');
    $("#notify2Country").val('');
    $("#notify2Phone").val('');
    $("#notify2Fax").val('');
    $("#notify2Zip").val('');
    $("#notify2Email").val('');
    $('#notify2CreditValues').text('');
    $('#notify2ScanSopValues').text('');
    $("#notify2AcctType").val('');
    $("#noty2Disabled").val('');
    $("#noty2DisableAcct").val('');
    $('.notify2Notes').attr("src", path + "/img/icons/e_contents_view.gif");
    $("#notify2SalesPersonCode").val('');
}
function notify2AccttypeCheck(path) {
    var acctType = $("#notify2AcctType").val();
    var moduleName = $('#moduleName').val();
    if ($('#noty2Disabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#noty2DisableAcct').val() + "</span>");
        clearNotify2Values(path);
    } else {
        if (acctType !== "" && acctType.indexOf("C") === -1) {
            if (moduleName !== 'Imports') {
                $.prompt("Please select the customers with account type C");
                clearNotify2Values(path);
            } else {
                createNewConsignee(path, 'N2', 'C');
            }
        }
        var creditDetails = $('#notify2CreditDetails').val();
        var sacnSopDetails = $('#notify2ScanSopDetails').val();
        if (creditDetails !== "") {
            $('#notify2CreditValues').text(creditDetails);
        } else {
            $('#notify2CreditValues').text('');
        }
        if (sacnSopDetails !== "") {
            $('#notify2ScanSopValues').text(sacnSopDetails);
        } else {
            $('#notify2ScanSopValues').text('');
        }
        $('.importCreditNotify2').addClass('green');
        if ($('#importCreditNotify2').val() === 'Y') {
            $('.importCreditNotify2').text('$');
        }
    }
}
$(document).ready(function () {
    $('#notify2Name').keyup(function () {
        if ($('#notify2Name').val() === "") {
            clearNotify2Values($('#path').val());
        }
    });
});
function showNotify2ManualCheck() {
    var acctno = $('#notify2Code').val();
    var notify2Manual = $('#dupNotify2Name').val();
    if (notify2Manual !== '' && acctno === '') {
        $('#newNotify2').attr("checked", true);
        $("#dojoNotify2").hide();
        $("#manualNotify2").show();
    }
    else {
        $('#dupNotify2Name').val('');
        $('#newNotify2').attr("checked", false);
        $("#manualNotify2").hide();
        $("#dojoNotify2").show();
    }
}
function linkVoyage(path) {
    var fileId = $('#fileNumberId').val();
    var commodity = $("#commObj").val();
    var pol = $("#portOfLoadingId").val();
    var pod = $("#portOfDestinationId").val();
    var load = $("#portOfLoading").val();
    var dest = $("#portOfDestination").val();
    var billtoCode = $('input:radio[name=billtoCodeImports]:checked').val();
    var consignee = $("#consigneeCode").val();
    var notify = $("#notifyCode").val();
    var thirdparty = $("#thirdpartyaccountNo").val();
    var fileNumber = $('#fileNumber').val();
    /* validate commodity */
    if (commodity === null || commodity === undefined) {
        $.prompt("Please select atleast one commodity.");
        $("#commodity1").css({
            "border": "2px solid rgb(177, 57, 57)"
        });
    } else if (billtoCode === 'C' && (consignee === null || consignee === undefined || consignee === '')) {
        $.prompt("Please select Consignee");
    } else if (billtoCode === 'N' && (notify === null || notify === undefined || notify === '')) {
        $.prompt("Please select Notify");
    } else if (billtoCode === 'T' && (thirdparty === null || thirdparty === undefined || thirdparty === '')) {
        $.prompt("Please select Third Party");
    } else {
        if (isFormChanged()) {
            $.prompt("Please save the DR changes before Linking.");
            return false;
        } else {
            var url = path + "/lclBooking.do?methodName=linkVoyage&pol=" + pol + "&pod=" + pod + "&fileId=" + fileId +
                    "&load=" + load + "&dest=" + dest + "&fileNumber=" + fileNumber;
            $.colorbox({
                iframe: true,
                href: url,
                width: "80%",
                height: "80%",
                title: "Link voyage"
            });
        }
    }
}

function unlinkDRBkgUnit(txt) {
    var fileId = $("#fileNumberId").val();
    var unitSsId = $('#unitSsId').val();
    var VoyageClosedBy = $('#VoyageClosedBy').val();
    if (VoyageClosedBy != "" && VoyageClosedBy != null) {
        $.prompt("Cannot Unlink a DR from a Closed Voyage");
        return false;
    }
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {

                $.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "unlinkDRFromBkgPieceUnit",
                        param1: fileId,
                        param2: unitSsId,
                        request: "true"
                    },
                    success: function (data) {
                        if (data > 0) {
                            $('#unitSsId').val('');
                            $('#unitStatus').val('B');
                            $("#impEciVoyage").val('');
                            $("#impSailDate").val('');
                            $("#impSsLine").val('');
                            $("#impVesselName").val('');
                            $("#impSsVoyage").val('');
                            $('#impPier').val('');
                            $('#impVesselArrival').val('');
                            $('#importApproxDue').val('');
                            $('#stripDate').val('');
                            $('#unitItNo').val('');
                            $('#unitItDate').val('');
                            $('#unitItPort').val('');
                            $('#impUnitNo').val('');
                            $('#impCFSWareName').val('');
                            $('#impUnitWareNo').val('');
                            $('#impCFSWareaddress').val('');
                            $('#impUnitWareaddress').val('');
                            $('#clearImpVoyage').hide();
                            $('#link-voy').show();
                            $('#cfsWareHsDetails').hide();
                            $('#unitWareHsDetails').hide();
                            $('#dispImp').hide();
                            $('#dispoId').html('DATA');
                            window.parent.showLoading();
                            $("#methodName").val("editBooking");
                            $("#lclBookingForm").submit();
                        }
                        $.prompt.close();
                    }
                });
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function showTransactions(path) {
    var moduleName = $('#moduleName').val();
    var blNumber = '';
    if (moduleName === 'Imports') {
        blNumber = 'IMP-' + $.trim($('#fileNumber').val());
    } else {

    }
    var url = path + "/lclBooking.do";
    url += "?methodName=showArTransactions";
    url += "&fileNumber=" + $.trim($('#fileNumber').val());
    url += "&blNumber=" + $.trim(blNumber);
    $.colorbox({
        iframe: true,
        href: url,
        width: "70%",
        height: "60%",
        title: "Show AR Transaction"
    });
}

function checkColoadRates(headingName) {
    var consDisabled = $('#consDisabled').val();
    var commodityId = $('#commodityId').val();
    if (consDisabled !== 'Y' && commodityId !== undefined) {
        var pol = $('#polUnlocationcode').val();
        var pod = $('#podUnlocationcode').val();
        var clientCommodityNo = $("#retailCommodity").val().replace(/[^\d]/g, '');
        var billingType = $('input:radio[name=pcBothImports]:checked').val();
        var consCommodityNo = $('#coloadCommNo').val();
        var coloadComm = headingName === "Consignee" ? consCommodityNo : clientCommodityNo;
        coloadComm = (coloadComm === "" && headingName === "Agent") ? $("#agCommodityNo").val() : coloadComm;
        var fileId = $("#fileNumberId").val();
        var acctType = $("#consignee_acct_type").val();
        var clientAcctType = $("#acct_type").val();
        var agentAcctType = $("#acctType").val();
        var existinColoadCommon = $("#existinColoadCommon").val();
        var text = "This Consignee has a coload commodity# and Rates, do you want to change the tariff# and recalculate rates.";
        var clientErtText = "ERT will change to Yes and that Client commodity rates will be applied";
        var consigneeErtText = "ERT will change to Yes and that Consignee commodity rates will be applied";
        var agentErtText = "ERT will change to Yes and that Agent commodity rates will be applied";
        var oldFileId = localStorage.getItem('drOldFileId') !== null ? localStorage.getItem('drOldFileId') : '';
        var consignee = localStorage.getItem('drConsignee') !== null ? localStorage.getItem('drConsignee') : '';
        var client = localStorage.getItem('drClient') !== null ? localStorage.getItem('drClient') : '';
        var drAgent = localStorage.getItem('drAgent') !== null ? localStorage.getItem('drAgent') : '';
        if (fileId.trim() !== oldFileId.trim()) {
            localStorage.removeItem('drOldFileId');
            localStorage.removeItem('drConsignee');
            localStorage.removeItem('drClient');
            localStorage.removeItem('drAgent');
        }
        if ((acctType !== "" && acctType.indexOf("C") !== -1) || (clientAcctType !== "" && clientAcctType.indexOf("C") !== -1) || (agentAcctType !== "" && agentAcctType.indexOf("C") !== -1)) {
            if ($('#fileStatus').val() === 'M' && coloadComm !== null && coloadComm !== "") {
                $.prompt("DR has been posted.It cannot adjust rates,this must be manually done and also the commodity can be manually changed");
                $('#existinColoadCommon').val(coloadComm);
                checkingERTLogic(headingName, fileId, "clear");
            } else {
                if ((existinColoadCommon !== null && existinColoadCommon !== "" && existinColoadCommon === '000000')
                        && coloadComm === null || coloadComm === "" || coloadComm === '000000' && (fileId !== null && fileId !== "")) {
                    $.prompt("This change may impact rates.Make sure you check the tariff#, and the rates below.");
                    $('#existinColoadCommon').val(coloadComm);
                    checkingERTLogic(headingName, fileId, "clear");
                } else if (coloadComm !== null && coloadComm !== "" && pol !== null && pol !== "" && pod !== null && pod !== "" && fileId !== null && (fileId !== "" && fileId !== null)) {
                    var ertData = openERTRates(coloadComm, pol, pod);
                    $.ajaxx({
                        data: {
                            className: "com.gp.cong.lcl.dwr.LclDwr",
                            methodName: "isColoadCommRates",
                            param1: pol,
                            param2: pod,
                            param3: coloadComm,
                            param4: billingType
                        },
                        async: false,
                        success: function (data) {
                            if (ertData === "true") {
                                if (headingName === "Consignee") {
                                    calculateConsColoadRates(consigneeErtText, fileId, pol, pod, coloadComm, billingType, headingName, ertData);
                                } else if (headingName === "Client" && consignee !== "ConsigneeRatesLoaded") {
                                    calculateConsColoadRates(clientErtText, fileId, pol, pod, coloadComm, billingType, headingName, ertData);
                                } else if (headingName === "Agent" && (consignee !== "ConsigneeRatesLoaded" && client !== "clientRatesLoaded")) {
                                    calculateConsColoadRates(agentErtText, fileId, pol, pod, coloadComm, billingType, headingName, ertData);
                                }
                            } else if (data === 'true') {
                                checkingERTLogic(headingName, fileId, "clear");
                                if (consignee === "" && client === "" && drAgent === "") {
                                    calculateConsColoadRates(text, fileId, pol, pod, coloadComm, billingType, headingName, ertData);
                                }
                            } else {
                                $.prompt("There is no rate found for Tariff#" + coloadComm);
                            }
                        }
                    });
                }
            }
        }
    }
}
function openERTRates(coloadComm, pol, pod) {
    var ertData;
    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "ertRatesChecking",
            param1: coloadComm,
            param2: pol,
            param3: pod
        },
        async: false,
        success: function (data) {
            ertData = data;
        }
    });
    return ertData;
}

function  checkingERTLogic(headingName, fileId, ertStatus) {
    localStorage.setItem("drOldFileId", fileId);
    if (ertStatus === "store") {
        switch (headingName) {
            case 'Consignee' :
                localStorage.setItem("drConsignee", 'ConsigneeRatesLoaded');
                $('#coloadCommNo').val('');
                break;
            case 'Client'    :
                localStorage.setItem("drClient", 'clientRatesLoaded');
                $("#retailCommodity").val('');
                break;
            case  'Agent'    :
                localStorage.setItem("drAgent", 'agentRatesLoaded');
                $("#agCommodityNo").val('');
                break;
        }
    } else {
        switch (headingName)
        {
            case 'Consignee'  :
                localStorage.removeItem('drConsignee');
                $('#coloadCommNo').val('');
                break;
            case 'Client'     :
                localStorage.removeItem('drClient');
                $("#retailCommodity").val('');
                break;
            case 'Agent'      :
                localStorage.removeItem('drAgent');
                $("#agCommodityNo").val('');
                break;
        }
    }
}

function calculateConsColoadRates(text, fileId, pol, pod, coloadComm, billingType, headingName, ertData) {
    $.prompt(text, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                $("#methodName").val("setColoadCommRates");
                var params = $("#lclBookingForm").serialize();
                if (ertData === "true") {
                    document.getElementById("rtdTransaction").selectedIndex = 1;
                    checkingERTLogic(headingName, fileId, "store");
                }
                params += "&fileNumberId=" + fileId + "&polUnCode=" + pol + "&podUnCode=" + pod + "&coloadComm=" + coloadComm + "&billingType=" + billingType + "&ratesFlag=commodity";
                $.post($("#lclBookingForm").attr("action"), params,
                        function (data) {
                            showProgressBar();
                            $("#commodityDesc").html(data);
                            $("#commodityDesc", window.parent.document).html(data);
                            $("#methodName").val("setColoadCommRates");
                            var params1 = $("#lclBookingForm").serialize();
                            params1 += "&fileNumberId=" + fileId + "&polUnCode=" + pol + "&podUnCode=" + pod + "&coloadComm=" + coloadComm + "&billingType=" + billingType + "&ratesFlag=charge";
                            $.post($("#lclBookingForm").attr("action"), params1,
                                    function (data) {
                                        $("#methodName").val("setColoadCommRates");
                                        var params2 = $("#lclBookingForm").serialize();
                                        params2 += "&coloadComm=" + coloadComm + "&ratesFlag=ert";
                                        $.post($("#lclBookingForm").attr("action"), params2,
                                                function (data) {
                                                    if (ertData === "" && data === "N") {
                                                        $('#rtdTransaction').val(data);
                                                    }
                                                    if (data === 'Y') {
                                                        $('#agentInfo').val($("#agentNumber").val());
                                                    } else {
                                                        $('#agentInfo').val('');
                                                    }
                                                });
                                        $("#chargeDesc").html(data);
                                        $("#chargeDesc", window.parent.document).html(data);
                                        $("#chargeDesc").find("[title != '']").not("link").tooltip();
                                        hideProgressBar();
                                        parent.$.fn.colorbox.close();
                                    });
                        });
                $.prompt.close();
            }
            else if (v === 2) {
                if (ertData === "true") {
                    if (headingName === "Consignee") {
                        $("#consigneeName").val('');
                        $("#consigneeCode").val('');
                    } else if (headingName === "Client") {
                        $("#client").val('');
                        $("#client_no").val('');
                        $("#email").val('');
                        $("#address").val('');
                        $("#otiNumber").val('');
                        $("#contactName").val('');
                        $("#phone").val('');
                        $("#fax").val('');
                        $("#fmcNumber").val('');
                        $("#commodityNumber").val('');
                    } else if (headingName === "agent") {
                        $("#supplierNameOrg").val('');
                        $("#supplierCode").val('');
                    }
                }
                $.prompt.close();
            }
        }
    });
}

function insertSubHouseBl() {
    if ($('#subHouseBlVal').is(":checked")) {
        $('#subHouseBl').val($('#defaultAms').val());
    }
    else {
        $('#subHouseBl').val('');
    }
}
function calculateClearRates() {
    var flag = false;
    var fileId = $('#fileNumberId').val();
    if (fileId !== "") {
        if ($("#portOfOriginId").val() !== "" || $("#portOfLoadingId").val() !== "") {
            flag = true;
        }
        if (flag && $("#portOfLoadingId").val() !== "" && $("#portOfDestinationId").val() !== "") {
            var commodityDetails = $('#commObj').html();
            if (commodityDetails === null) {
                $.prompt("Please add Weight and Measure from Commodity");
            }
            else {
                var orgUncode = $("#originUnlocationCode").val();
                var polUncode = $("#polUnlocationcode").val();
                var podUncode = $("#podUnlocationcode").val();
                var commodityNo = $("#comno").val();
                if ((orgUncode !== "" || polUncode !== "") && podUncode !== "" && commodityNo !== "") {
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
                            if (null !== data && data === "Y") {
                                $.prompt("ERT will change to Yes", {
                                    callback: function () {
                                        $("#rtdTransaction").val("Y");
                                        if ($('#fileStatus').val() !== 'M') {
                                            calculateRates();
                                        }
                                    }
                                });
                            } else {
                                $.prompt("ERT will change to No", {
                                    callback: function () {
                                        $("#rtdTransaction").val("N");
                                        if ($('#fileStatus').val() !== 'M') {
                                            calculateRates();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
    }
}

function calculateRates() {
    var origin = getOrigin();
    var destination = getDestination();
    var pol = $('#polCode').val();
    var pod = $('#podCode').val();
    submitAjaxForm("calculateImportCharges", '#lclBookingForm', '#chargeDesc', origin, destination, pol, pod);
    setDisableTextBox("portOfOriginR");
    setDisableTextBox("portOfLoading");
    setDisableTextBox("portOfDestination");
    setEnableTextBox("finalDestinationR");
    document.getElementById('clearRates').style.position = "relative";
    document.getElementById('clearRates').style.visibility = "visible";
}

function isSubHouseCheckBox() {
    if ($('#subHouseBl').val() !== "" && $('#defaultAms').val() !== "" && $('#subHouseBl').val() === $('#defaultAms').val()) {
        $('#subHouseBlVal').attr('checked', true);
    }
    else {
        $('#subHouseBlVal').attr('checked', false);
    }
}
function setDisableTextBox(id) {
    $('#' + id).addClass('textlabelsBoldForTextBoxDisabledLook');
    $('#' + id).attr('readonly', true);
}
function setEnableTextBox(id) {
    $('#' + id).removeClass("textlabelsBoldForTextBoxDisabledLookWidth");
    $('#' + id).removeClass("textlabelsBoldForTextBoxDisabledLook");
    $('#' + id).addClass("textlabelsBoldForTextBoxWidth");
    $('#' + id).removeClass("text-readonly");
    $('#' + id).removeAttr("readonly");
}
function isCheckedEditTpForConsignee() {
    var consAcctNo = $('#consigneeCode').val();
    var consTpAcctName = $('#consigneeAcctName').val();
    var consEditAcctName = $('#consigneeName').val();
    if (consAcctNo !== null && consAcctNo !== "" && consTpAcctName !== consEditAcctName) {
        $('#editTpConsignee').attr('checked', true);
    }
}
function isUncheckedEditTpCons() {//edit consignee
    var consEditAcctName = $('#consigneeName').val();
    if ($('#editTpConsignee').is(":checked")) {
        $('#dupConsigneeName').val(consEditAcctName);
        $('#manualConsignee').show();
        $('#dojoConsignee').hide();
    }
    if (!$('#editTpConsignee').is(":checked")) {
        $('#consigneeName').val($('#consigneeAcctName').val());
        $('#dojoConsignee').show();
        $('#manualConsignee').hide();
    }
}

function isUncheckedEditTpNoty() {//edit Notify
    var notyEdit = $('#notifyName').val();
    if ($('#editTpNoty').is(":checked")) {
        $('#dupNotifyName').val(notyEdit);
        $('#manualNotify').show();
        $('#dojoNotify').hide();
    }
    if (!$('#editTpNoty').is(":checked")) {
        $('#notifyName').val($('#notifyAcctName').val());
        $('#dojoNotify').show();
        $('#manualNotify').hide();
    }
}
function isValidateBillToCons() {
    var consigneeChargeFlag = false;
    var rowCount = $("#chargesTable tbody tr").length;
    for (var i = 1; i <= rowCount; i++) {
        var value = $("#chargesTable").find('tr:nth-child(' + i + ') td:nth-child(6)').html();
        if (trim(value).indexOf("Consignee") !== -1) {
            consigneeChargeFlag = true;
        }
    }
    if (consigneeChargeFlag) {
        $('#newConsignee').attr('checked', false);
        $.prompt('Consignee does not exist in Trading Partners.Cannot bill charges to Consignee.Please correct.');
    } else {
        $('#consigneeName').val('');
        $('#consigneeCode').val('');
        $('.consigneeCreditV').text('');
        $('.consigneeScanSopV').text('');
    }
    newConsigneeName();
}



function ischeckedEditTpNoty() {
    var consAcctNo = $('#notifyCode').val();
    var consTpAcctName = $('#notyAcctName').val();
    var consEditAcctName = $('#notifyName').val();
    if (consAcctNo !== null && consAcctNo !== "" && consTpAcctName !== consEditAcctName) {
        $('#editTpNoty').attr('checked', true);
    }
}

function isValidateBillToNoty() {
    var consigneeChargeFlag = false;
    var rowCount = $("#chargesTable tbody tr").length;
    for (var i = 1; i <= rowCount; i++) {
        var value = $("#chargesTable").find('tr:nth-child(' + i + ') td:nth-child(6)').html();
        if (trim(value).indexOf("Notify Party") !== -1) {
            consigneeChargeFlag = true;
        }
    }
    if (consigneeChargeFlag) {
        $('#newNotify').attr('checked', false);
        $.prompt('Notify Party does not exist in Trading Partners.Cannot bill charges to Notify Party.Please correct.');
    } else {
        $('#notifyName').val('');
        $('#notifyCode').val('');
        $('#notifyCreditClient').text('');
    }
    newNotifyName();
}
function openRoutingPopUp(path, fileId, fileNo, id) {
    var href = path + "/importRouting.do?methodName=display&fileId=" + fileId + "&fileNo=" + fileNo;
    $("#" + id).attr("href", href);
    $("#" + id).colorbox({
        iframe: true,
        width: "50%",
        height: "60%",
        title: "Routing Instruction"
    });
}
function isCheckedChargeBillToParty(billToParty) {
    var rowCount = $("#chargesTable tbody tr").length;
    for (var i = 1; i <= rowCount; i++) {
        var value = $("#chargesTable").find('tr:nth-child(' + i + ') td:nth-child(6)').html();
        if (trim(value).indexOf(billToParty) !== -1) {
            return true;
        }
    }
    return false;
}
function disablePostDisableToAcct() {
    if ($('#fileStatus').val() !== 'B' && $('#fileStatus').val() !== '') {
        if (isCheckedChargeBillToParty("Consignee")) {
            setDisableTextBox('consigneeName');
            disabledCheckBox('editTpConsignee');
            disabledCheckBox('newConsignee');
        }
        if (isCheckedChargeBillToParty("Notify Party")) {
            setDisableTextBox('notifyName');
            disabledCheckBox('newNotify');
            disabledCheckBox('editTpNoty');
        }
        if (isCheckedChargeBillToParty("Third Party")) {
            setDisableTextBox('thirdPartyname');
        }
    }
}
function disabledCheckBox(id) {
    $('#' + id).attr("disabled", true);
}
function setScacVal(scacCode) {
    if ($('#setSacacValue').is(':checked')) {
        $("#amsHblScac").val($("#scac").val());
    } else {
        $("#amsHblScac").val('');
    }
}
function getDispStatus(headerId, unitId) {
    var dispStatus;
    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getDispStatus",
            param1: headerId,
            param2: unitId
        },
        async: false,
        success: function (data) {
            dispStatus = data;
        }
    });
    return dispStatus;
}
function checkETF_FD(path) {
    var consigneeCode = $("#consigneeCode").val();
    var shipperCode = $("#shipperCode").val();
    var notifyCode = $("#notifyCode").val();
    var notify2Code = $('#notify2Code').val();
    var txt = "Send Status Update To Customer";
    var eftValue = $("#etaFDDate").val();
    var headerId = $("#headerId").val();
    var fileNumberId = $('#fileNumberId').val();
    var unitId = $("#unitId").val();
    $("#etaFDDate").change(function () {
        var modifyDate = $(this).val();
        var dispStatus = getDispStatus(headerId, unitId);
        if (eftValue !== $(this).val() && eftValue !== '' && dispStatus === 'true') {
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        $.prompt.close();
                        if (consigneeCode === "" && shipperCode === "" && notifyCode === "" && notify2Code === "") {
                            $.prompt("Select Atleast One Customer Type");
                        } else {
                            $("#methodName").val("validateETACodeFList");
                            var params = $("#lclBookingForm").serialize();
                            params += "&consigneeCode=" + consigneeCode + "&shipperCode=" + shipperCode +
                                    "&notifyCode=" + notifyCode + "&notify2Code=" + notify2Code + "&headerId=" + headerId;
                            $.post($("#lclBookingForm").attr("action"), params,
                                    function (data) {
                                        if (data === '') {
                                            var href = "/logisoft/lclBooking.do?methodName=EftModifyDetails&consigneeCode=" + consigneeCode + "&shipperCode=" + shipperCode +
                                                    "&notifyCode=" + notifyCode + "&notify2Code=" + notify2Code + "&headerId=" + headerId + "&eftDate=" + modifyDate + "&fileNumberId=" + fileNumberId;
                                            $.colorbox({
                                                href: href,
                                                iframe: true,
                                                width: "60%",
                                                height: "70%",
                                                title: "ETA-FD MAIL DETAILS"
                                            });
                                        } else {
                                            $.prompt(data);
                                        }
                                    });
                        }
                    }
                    else {
                        $.prompt.close();
                        $("#etaFDDate").val(modifyDate);
                    }
                }
            });
        }
    });
}

function mailValidate(email) {
    var checked = $("#mailCheckbox").attr('checked') ? true : false;
    var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
    if (checked) {
        if (!emailReg.test(email)) {
            $.prompt("Select The Valid  Email ");
            $("#mailCheckbox").attr('checked', false);
        }
    }
}
function sendETFMail() {
    var modifyDate = $("#etaFDDate").val();
    var mailList = [];
    $(".emailList:checked").each(function () {
        mailList.push($(this).val());
    });
    var fileId = parent.$("#fileNumberId").val();
    var headerId = $("#headerId").val();
    if (mailList.length <= 0) {
        $.prompt("Select Atleast One Mail Content From The List");
    } else {
        $("#methodName").val("sendImpStatusPdf");
        var params = $("#lclBookingForm").serialize();
        params += "&maillist=" + mailList + "&fileId=" + fileId + "&headerId=" + headerId + "&eftDate=" + modifyDate;
        $.post($("#lclBookingForm").attr("action"), params,
                function (data) {
                    $.prompt(data, {
                        button: {
                            Ok: 1
                        },
                        submit: function (v) {
                            if (v == 1) {
                                parent.$.colorbox.close();
                            }
                        }
                    });
                });
    }
}
function closepopup() {
    var txt = "Content Is Selected Need To Close";
    var list = [];
    $(".emailList:checked").each(function () {
        list.push($(this).val());
    });
    if (list.length <= 0) {
        parent.$.colorbox.close();
    } else {
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    parent.$.colorbox.close();
                } else {
                    $.prompt.close();
                }
            }
        });
    }
}

function amsNoDelete() {
    if ($("#defaultAms").val() === "" && $('#amsHblId').val() !== "") {
        var amsHblId = '';
        var amsId = new Array();
        var amsNo = new Array();
        var pcNo = new Array();
        var scacNo = new Array();
        var amsNoChange = '';
        var pcNoChange = '';
        var scacNoChange = '';
        var tempData = $("#tempdata").val();
        $.prompt("Are you sure you want to delete ?", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    submitAjaxFormforGeneral("deleteImpAmsHBL", "Ams", $('#amsHblId').val(), "amsHblList");
                    $("#defaultPieces").val('');
                    $("#scac").val('');
                    $(".amsHblNo").each(function () {
                        amsNo.push($(this).text().replace(/[&\/\\#,+()$~%'":*?<>{}]/g, '').trim().trim());
                        for (var i = 0; i < amsNo.length; i++) {
                            amsNoChange = amsNo[0];
                        }
                        $("#defaultAms").val(amsNoChange);
                        $("#tempdata").val(amsNoChange);
                    });

                    $(".scacNo").each(function () {
                        scacNo.push($(this).text());
                        for (var i = 0; i < scacNo.length; i++) {
                            scacNoChange = scacNo[0];
                        }
                        $("#scac").val(scacNoChange);
                    });

                    $(".amsHblPcs").each(function () {
                        pcNo.push($(this).text());
                        for (var i = 0; i < pcNo.length; i++) {
                            pcNoChange = pcNo[0];
                        }
                        $("#defaultPieces").val(pcNoChange);
                    });

                    $(".amsHbl1").each(function () {
                        amsId.push($(this).val());
                    });
                    for (var i = 0; i < amsId.length; i++) {
                        amsHblId = amsId[0];
                    }
                    $('#amsHblId').val(amsHblId);
                    if ($("#defaultAms").val() === "" || $("#defaultPieces").val() === "") {
                        $("#more-ams").hide();
                    }
                    $.prompt.close();
                } else if (v === 2) {
                    $("#defaultAms").val(tempData);
                    $.prompt.close();
                }
            }
        });
    }
}
function fillforeignPOD() {//fill usa and foreign based on relay
    if ($("#finalDestinationId").val() !== "" && $("#portOfDestinationId").val() !== "") {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "fillForeignPODImp",
                param1: $("#portOfDestinationId").val(),
                param2: $("#finalDestinationId").val(),
                dataType: "json"
            },
            success: function (data) {
                if (data !== null) {
                    if (data[0] !== "" && data[0] !== null) {
                        showProgressBar();
                        $("#foreignDischargeId").val(data[0]);
                        $("#foreignDischarge").val(data[1] + "/" + data[3] + "(" + data[2] + ")");
                        $('#portExitId').val(data[4]);
                        $('#portExit').val(data[5] + "/" + data[7] + "(" + data[6] + ")");
                        hideProgressBar();
                        $("#impForeignPod").val(data[2]);
                        showUpcomingSailingsForImp();
                        submitAjaxFormForAgent('refreshAgent', '#lclBookingForm', '#m', '');

                    } else {
                        $("#foreignDischargeId").val('');
                        $("#foreignDischarge").val('');
                        $('#portExitId').val('');
                        $('#portExit').val('');
                    }
                }
            }
        });
    }
}
function showUpcomingSailingsForImp() {
    var origin = $('#portOfDestinationId').val();
    var fd = $('#finalDestinationId').val();
    var pol = $('#portExitId').val();
    var pod = $('#foreignDischargeId').val();
    var shipment = $('input:radio[name=transShipMent]:checked').val();
    if (origin != '' && fd != '' && pol != '' && pod != '' && shipment == 'Y') {
        submitAjaxFormForVoyage('displayVoyage', '#lclBookingForm', '#upcomingSailing', '');//upcoming sailings
    }
    impUpcomingTab();
}
function impUpcomingTab() {
    $('#originSailing').text($("#portOfDestination").val());
    $('#polSailing').text($("#portExit").val());
    $('#podSailing').text($("#foreignDischarge").val());
    $('#destinationSailing').text($("#finalDestinationR").val());
}
function submitAjaxFormForImpRates() {//delete charges by transhipment
    var fileNumberId = $('#fileNumberId').val();
    if (fileNumberId != '') {
        showProgressBar();
        $('#methodName').val("deleteManualChargeFromBkg");
        var params = $("#lclBookingForm").serialize();
        params += "&fileNumberId=" + fileNumberId;
        $.post($("#lclBookingForm").attr("action"), params, function (data) {
            $("#chargeDesc").html(data);
            $("#chargeDesc", window.parent.document).html(data);
            $("#chargeDesc").find("[title != '']").not("link").tooltip();
            hideProgressBar();
        });
    }
}
function submitAjaxFormForVoyage(methodName, formName, selector, id) {//upcoming sailings
    showProgressBar();
    $("#methodName").val(methodName);
    var relay = $('input:radio[name=relayOverride]:checked').val();
    var params = $(formName).serialize();
    params += "&id=" + id + "&relay=" + relay;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
            });
    hideProgressBar();
}
function correctionNotice(path, fileId, fileNo, selectedMenu, headerId) {
    validateBilltoPartyImports();
    var billtoCodeImports = $('input:radio[name=billtoCodeImports]:checked').val();
    var billingType = $('input:radio[name=pcBothImports]:checked').val();
    var acctNo = "", acctName = "";
    if (billtoCodeImports == "T") {
        acctNo = $('#thirdpartyaccountNo').val();
        acctName = $('#thirdPartyname').val();
    }
    else if (billtoCodeImports == "C") {
        acctNo = $('#consigneeCode').val();
        acctName = $('#consigneeName').val();
    }
    else if (billtoCodeImports == "N") {
        acctNo = $('#notifyCode').val();
        acctName = $('#notifyName').val();
    }
    else if (billtoCodeImports == "A") {
        acctNo = $('#supplierCode').val();
        acctName = $('#supplierNameOrg').val();
    }
    var href = path + "/lclCorrection.do?methodName=viewLclBlCorrections&fileId=" + fileId + "&fileNo=" + fileNo + "&selectedMenu=" + selectedMenu;
    href = href + "&billToParty=" + billtoCodeImports + "&customerAcctNo=" + acctNo + "&billingType=" + billingType + "&customerAcctName=" + acctName;
    href = href + "&buttonValue=correction&screenName=Corrections&updateBillToParty=Y&headerId=" + headerId + "&cfsDevWarhsNo=" + $('#cfsWarehouseNo').val();
    $.colorbox({
        iframe: true,
        href: href,
        width: "80%",
        height: "96%",
        title: "LclCorrections",
        onLoad: function () {
            $('#cboxClose').hide();
        },
        onClosed: function () {
            //window.parent.showLoading();
            //$("#methodName").val("editBooking");
            //$("#lclBookingForm").submit();
        }
    });
}
function quickCN(path, fileId, fileNo, selectedMenu) {
    validateBilltoPartyImports();
    var billtoCodeImports = $('input:radio[name=billtoCodeImports]:checked').val();
    var billingType = $('input:radio[name=pcBothImports]:checked').val();
    var acctNo = "", acctName = "";
    if (billtoCodeImports === "T") {
        acctNo = $('#thirdpartyaccountNo').val();
        acctName = $('#thirdPartyname').val();
    }
    else if (billtoCodeImports === "C") {
        acctNo = $('#consigneeCode').val();
        acctName = $('#consigneeName').val();
    }
    else if (billtoCodeImports === "N") {
        acctNo = $('#notifyCode').val();
        acctName = $('#notifyName').val();
    }
    else if (billtoCodeImports === "A") {
        acctNo = $('#supplierCode').val();
        acctName = $('#supplierNameOrg').val();
    }
    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getLatestCorrectionStatus",
            param1: fileId
        },
        success: function (data) {
            if (data === "O") {
                sampleAlert("Please Post Last Correction before creating new Correction for this Booking");
                return false;
            }
            else {
                var href = path + "/lclCorrection.do?methodName=addLclBlCorrections&fileId=" + fileId + "&fileNo=" + fileNo + "&selectedMenu=" + selectedMenu;
                href = href + "&billToParty=" + billtoCodeImports + "&customerAcctNo=" + acctNo + "&billingType=" + billingType + "&customerAcctName=" + encodeURIComponent(acctName);
                href = href + "&updateBillToParty=Y&buttonValue=quickcn&thirdPartyAcctNo=" + $('#thirdpartyaccountNo').val() + "&cfsDevWarhsNo=" + $('#cfsWarehouseNo').val();
                $.colorbox({
                    iframe: true,
                    href: href,
                    width: "80%",
                    height: "96%",
                    title: "Quick CN",
                    onClosed: function () {
                        window.parent.showLoading();
                        $("#methodName").val("editBooking");
                        $("#lclBookingForm").submit();
                    }
                });
            }
        }
    });
}
function addInvoiceCharge(path, fileNumberId, fileNumber, fileNumberStatus, buttonValue) {
    var count = new Array();
    $(".chargeAmount").each(function () {
        count.push($(this).text().trim());
    });
    if (count.length >= 25) {
        $.prompt("More than 25 charges is not allowed");
    } else {
        var destination = getDestination();
        var href = path + "/lclCostAndCharge.do?methodName=displayInvoiceCharges&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber;
        href = href + "&destination=" + destination + "&buttonValue=" + buttonValue + "&fileNumberStatus=" + fileNumberStatus;
        $(".costAndCharge").attr("href", href);
        $(".costAndCharge").colorbox({
            iframe: true,
            width: "70%",
            height: "70%",
            title: "Add Invoice Charge"
        });
    }
}
function setIPICFSstate() {
    //code for assigning search state value in IFC
    var finalDestn = $("#finalDestinationR").val();
    var state = finalDestn.substring(finalDestn.lastIndexOf("/") + 1, finalDestn.lastIndexOf("("));
    var multiState = "";
    if (state != "") {
        var st = state.split(","), part;
        for (var i = 0; i < (st.length); i++) {
            multiState += "'" + st[i] + "',";
        }
        var len = multiState.length;
        var data = multiState.substring(0, len - 1);
        $("#ipiSearchState").val(data);
        $('#stGeorgeAccount').attr('alt', 'IMPORT_CFS_STATE');
    } else {
        $('#ipiSearchState').val("''");
        $('#stGeorgeAccount').attr('alt', 'IMPORT_CFS');
    }
}
function getAmsPcs() {
    var hblPcs = 0;
    $(".amsHblPcs").each(function () {
        hblPcs += +$(this).text();
    });
    hblPcs += +$("#defaultPieces").val();
    return hblPcs;
}
function submitAmsHblPopupBox(methodName, fileNumberId, selector) {
    var amsPcs = getAmsPcs();
    var $scac = $('#amsHblScac');
    var commPcs = getCommodityPcs();
    amsPcs += +$("#amsHblPiece").val();
    var module = $('#moduleName').val();
    if (module === 'Imports' && $scac.val() === "") {
        $.prompt('SCAC is required');
        $scac.css("border-color", "red");
    } else if ($('#amsHblNo').val() === "") {
        $.prompt('AMS No is required');
        $("#amsHblNo").css("border-color", "red");
    } else if (amsPcs > commPcs) {
        $.prompt("AMS record pieces total SHOULD NOT be greater than the total commodity pieces");
    } else {
        var defaultAmsScac = $("#defaultAms").val().toUpperCase() + $("#scac").val().toUpperCase();
        var amsHblNoScac = $('#amsHblNo').val().toUpperCase() + $('#amsHblScac').val().toUpperCase();
        var flag = false;
        if (defaultAmsScac === amsHblNoScac) {
            flag = true;
        } else {
            $('.hblAmsNoHblScac').each(function () {
                if ($(this).text().trim().trim().toUpperCase() === amsHblNoScac) {
                    flag = true;
                }
            });
        }
        if (flag) {
            $.prompt("This DR - " + $('#fileNumber').val() + " already has same scac - <span style=color:red>" + $('#amsHblScac').val().toUpperCase() + "</span>"
                    + " and ams# - <span style=color:red>" + $('#amsHblNo').val().toUpperCase() + "</span>");
        } else {
            if ($('#segId').val() === '' || $('#segId').val() === null || $('#segId').val() === undefined) {
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO",
                        methodName: "validateScacAndAms",
                        param1: $('#amsHblScac').val(),
                        param2: $('#amsHblNo').val(),
                        param3: fileNumberId
                    },
                    preloading: true,
                    success: function (data) {
                        if (data === "available") {
                            $("#methodName").val(methodName);
                            var params = $("#lclBookingForm").serialize();
                            params += "&fileId=" + fileNumberId;
                            $.ajaxx({
                                url: path + "/lclBooking.do",
                                data: params,
                                preloading: true,
                                success: function (result) {
                                    $(selector).html(result);
                                    $scac.val("");
                                    $('#amsHblNo').val("");
                                    $('#amsHblPiece').val("");
                                    $('#amsHblBox').hide();
                                }
                            });
                        } else {
                            $.prompt(data, {
                                callback: function () {
                                    $scac.focus();
                                }
                            });
                        }
                    }
                });
            }
        }
    }
}
function validateSubHouseBl(fileNumberId) {
    var SubHouseBl = $('#subHouseBl').val();
    if (SubHouseBl !== "" || SubHouseBl === null || SubHouseBl === undefined) {
        $.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO",
                methodName: "validateSubHouseBl",
                param1: SubHouseBl,
                param2: fileNumberId
            },
            preloading: true,
            success: function (data) {
                if (data == "available") {
                    $.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHsCodeDAO",
                            methodName: "validateQuoteSubHouseBl",
                            param1: SubHouseBl,
                            param2: fileNumberId
                        },
                        preloading: true,
                        success: function (data) {
                            if (data !== "available") {
                                $.prompt(data, {
                                    callback: function () {
                                        $('#subHouseBl').val('');
                                    }
                                });
                            }
                        }
                    });
                }
                if (data !== "available") {
                    $.prompt(data, {
                        callback: function () {
                            $('#subHouseBl').val('');
                        }
                    });
                }
            }
        });
    }
}

function updateAms(fileId, methodName) {
    var $scac = $("#scac");
    var $ams = $("#defaultAms");
    var $amsPcs = $("#defaultPieces");
    var amsPcs = getAmsPcs() + (+$("#amsHblPiece").val());
    var $prevPcs = $("#prevPcs");
    var module = $('#moduleName').val();
    if ($("#commObj").text() === "") {
        $scac.val("");
        $ams.val("");
        $amsPcs.val("");
        $.prompt("Add commodity to proceed.");
    } else if (fileId === "") {
        $.prompt("Please save Booking");
    } else if (module === 'Imports' && $scac.val() === "") {
        $.prompt('SCAC is required');
        $scac.css("border-color", "red");
    } else if ($ams.val() === "") {
        $.prompt('AMS No is required');
        $ams.css("border-color", "red");
    } else if (amsPcs > getCommodityPcs()) {
        $amsPcs.val($prevPcs.val());
        $.prompt("AMS record pieces total SHOULD NOT be greater than the total commodity pieces");
    } else {
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
        } else {
            if ($('#segId').val() === '' || $('#segId').val() === null || $('#segId').val() === undefined) {
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO",
                        methodName: "validateScacAndAms",
                        param1: $scac.val(),
                        param2: $ams.val(),
                        param3: fileId
                    },
                    preloading: true,
                    success: function (data) {
                        if (data === "available") {
                            $("#methodName").val(methodName);
                            var params = $("#lclBookingForm").serialize();
                            params += "&fileId=" + fileId;
                            $.ajaxx({
                                url: path + "/lclBooking.do",
                                data: params,
                                preloading: true,
                                success: function (id) {
                                    $("#amsHblId").val(id);
                                    var $msg = $("#update-msg");
                                    var $val = $("#update-val");
                                    var $msgPcs = $("#msg-pcs");
                                    var $valPcs = $("#val-pcs");
                                    var $msScac = $("#msg-scac");
                                    var $valScac = $("#val-scac");
                                    $msScac.text("Inserted Scac : ");
                                    $valScac.text($scac.val().toUpperCase());
                                    $prevPcs.val($amsPcs.val());
                                    $msg.text("AMS/HBL # : ");
                                    $val.text($ams.val().toUpperCase());
                                    $msgPcs.text("Piece Count : ");
                                    $valPcs.text($amsPcs.val().toUpperCase());
                                    $("#more-ams").show();
                                    setTimeout(function () {
                                        $msg.text("");
                                        $val.text("");
                                        $msgPcs.text("");
                                        $valPcs.text("");
                                        $msScac.text("");
                                        $valScac.text("");
                                    }, 4000);
                                }
                            });
                        } else {
                            $.prompt(data, {
                                callback: function () {
                                    $scac.focus();
                                }
                            });
                        }
                    }
                });
            }
        }
    }
}


function transhipment() {
    var fileId = $("#fileNumberId").val();
    var fileNumber = $("#fileNumber").val();
    var shipment = $('input:radio[name=transShipMent]:checked').val();
    var fileStatus = $('#fileStatus').val();
    var allowTransshipment = $('#allowTransshipment').val();
    if (shipment === 'Y') {
        if (fileStatus === 'M') {
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "validateImpCostChargeStatus",
                    param1: fileId,
                    param2: fileNumber,
                    dataType: "json"
                },
                success: function (data) {
                    if (data !== "" && data !== null) {
                        if (allowTransshipment === 'false') {
                            $.prompt(data);
                            $('#transShipMentNo').attr('checked', true);
                        } else {
                            $.prompt(data, {
                                buttons: {
                                    Continue: 1,
                                    Abort: 2
                                },
                                submit: function (v) {
                                    if (v === 1) {
                                        allowImpTransshipment();
                                    } else {
                                        $('#transShipMentNo').attr('checked', true);
                                    }
                                }
                            });
                        }
                    } else {
                        reverseImportsCharges();
                        allowImpTransshipment();
                    }
                }
            });
        } else {
            $('#lclImpRrelease').show();
            $('#lclImpRreleasebot').show();
            $('#usaExit').show();
            $('#portDiscg').show();
            $('#portSpecialRemarks').show();
            $('#portInternalRemarks').show();
            $('#regionalRemark').show();
            $('#griRemarks').show();
            $('#upcomingImpSection').show();
            $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
            $('#remarksSection').addClass("black-border");
            $('#regionalRemark').addClass("black-border");
            $('#defaultAgentDetails').show();
            $('#m').show();
            $('#agentNumberDetails').show();
            fillforeignPOD();
            submitAjaxFormForImpRates();
            impUpcomingTab();
            fillAesItn();
//            submitForm('saveBooking');
        }

    } else {
        if (fileStatus === 'M') {
            var txt = "You are changing this Transshipment back to Imports, and the charges have already been posted. You must issue a CN in order to remove any charges to the customer";
            $.prompt(txt, {
                buttons: {
                    Continue: 1,
                    Abort: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        $('#polLabel').attr('width', '10%');
                        $('#polDojo').attr('width', '20%');
                        $('#defaultAgentDetails').hide();//Default Agent Details
                        $('#m').hide();//Agent Name,Number Details
                        $('#agentNumberDetails').hide();//Agent Name,Number Details
                        $('#transSailing').text('');
                        $('#masterScheduleNo').val('');
                        $('#upcomingImpSection').hide();
                        $('#lclImpRrelease').hide();
                        $('#lclImpRreleasebot').hide();
                        $('#remarksSection').removeClass("black-border");
                        $('#usaExit').hide();
                        $('#portDiscg').hide();
                        $('#portSpecialRemarks').val('');
                        $('#portInternalRemarks').val('');
                        $('#griRemarks').val('');
                        $('#portSpecialRemarks').hide();
                        $('#portInternalRemarks').hide();
                        $('#griRemarks').hide();
                        $('#regionalRemark').hide();
                        $('#finalDestinationR').attr('alt', 'DEST_UNLOC');
                        $('#regionalRemark').removeClass("black-border");

                    }
                    else {
                        $('#transShipMentYes').attr('checked', true);
                    }
                }
            });
        } else {
            $('#polLabel').attr('width', '10%');
            $('#polDojo').attr('width', '20%');
            $('#defaultAgentDetails').hide();//Default Agent Details
            $('#m').hide();//Agent Name,Number Details
            $('#agentNumberDetails').hide();//Agent Name,Number Details
            $('#transSailing').text('');
            $('#masterScheduleNo').val('');
            $('#upcomingImpSection').hide();
            $('#lclImpRrelease').hide();
            $('#lclImpRreleasebot').hide();
            $('#remarksSection').removeClass("black-border");
            $('#regionalRemark').removeClass("black-border");
            $('#usaExit').hide();
            $('#portDiscg').hide();
            $('#portSpecialRemarks').val('');
            $('#portInternalRemarks').val('');
            $('#griRemarks').val('');
            $('#portSpecialRemarks').hide();
            $('#portInternalRemarks').hide();
            $('#regionalRemark').hide();
            $('#griRemarks').hide();
            $('#finalDestinationR').attr('alt', 'DEST_UNLOC');
            calculateCharge('', '#doorOriginCityZip', '#pickupReadyDate');

            if (fileStatus !== 'B') {
                updateFileStatus(fileId, 'B');
            }
        }
    }
}
function allowImpTransshipment() {
    $('#polLabel').attr('width', '20%');
    $('#polDojo').attr('width', '10%');
    var foreignDischarge = $('#finalDestinationR').val();
    var foreigndischarge = foreignDischarge.substring(foreignDischarge.indexOf('(') + 1, foreignDischarge.indexOf(')'));
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "defaultDestinationImportRemarks",
            param1: foreigndischarge,
            dataType: "json"
        },
        success: function (data) {
            if (data[0] !== undefined && data[0] !== "" && data[0] !== null) {
                $('#specialRemarks').val(data[0]);
            }
            if (data[1] !== undefined && data[1] !== "" && data[1] !== null) {
                $('#internalRemarks').val(data[1]);
            }
            if (data[2] !== undefined && data[2] !== "" && data[2] !== null) {
                $('#portGriRemarks').val(data[2]);
            }
        }
    });
    $('#lclImpRrelease').show();
    $('#lclImpRreleasebot').show();
    $('#usaExit').show();
    $('#portDiscg').show();
    $('#portSpecialRemarks').show();
    $('#portInternalRemarks').show();
    $('#griRemarks').show();
    $('#regionalRemarkhide').show();
    $('#upcomingImpSection').show();
    $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
    $('#remarksSection').addClass("black-border");
    $('#regionalRemark').addClass("black-border");
    $('#defaultAgentDetails').show();
    $('#m').show();
    $('#agentNumberDetails').show();
    fillforeignPOD();
    impUpcomingTab();
    fillAesItn();
//                        submitForm('saveBooking');
}
function showShipDetails() {////Shipper ToolTip
    setToolTipDetails('Shipper', 'shipperCode', 'shipperName', 'shipper');
}
function showConsDetails() {//Consignee ToolTip
    setToolTipDetails('Consignee', 'consigneeCode', 'consigneeName', 'consContact');
}
function showNotyDetails() {//Notify ToolTip
    setToolTipDetails('Notify', 'notifyCode', 'notifyName', 'noty');
}
function showNotify2Details() {//Notify ToolTip
    setToolTipDetails('Notify2', 'notify2Code', 'notify2Name', 'noty2');
}
function setToolTipDetails(headingName, acctCode, acctName, toolTip) {
    var moduleName = $('#moduleName').val();
    if ($.trim($('#' + acctCode).val()) !== "") {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getContactDetails",
                param1: headingName,
                param2: $('#' + acctCode).val(),
                param3: $('#' + acctName).val(),
                param4: moduleName
            },
            async: false,
            success: function (data) {
                if (headingName === "Notify" || headingName === "Notify2") {
                    ImportsJToolTip('#' + toolTip, data, 1000, 'topRight');
                } else if (headingName === "Shipper") {
                    ImportsJToolTip('#' + toolTip, data, 950, 'topLeft');
                } else if (headingName === "Consignee") {
                    ImportsJToolTip('#' + toolTip, data, 1000, 'topMiddle');
                }
            }
        });
    }
}

function openSegDrPopup(path, fileNumberId, fileNumber, amsHblId, amsNo, pieces, finalDestination) {
    if (isFormChanged()) {
        $.prompt("Please save the DR changes before Segregation");
        return false;
    } else {
        var href = path + "/lclSegregation.do?methodName=displaySegDr&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber;
        href = href + "&amsHblId=" + amsHblId + "&amsNo=" + amsNo + "&pieces=" + pieces + "&finalDestination=" + finalDestination;
        $.colorbox({
            iframe: true,
            href: href,
            width: "45%",
            height: "50%",
            title: "Segregation DR"
        });
    }
}

function openSegBooking(path, segFileNumberId, segFileNumber) {
    var headerId = $("#headerId").val();
    var unitId = $('#unitId').val();
    var impVoyageFlag = $('#impSearchFlag').val();
    var href = path + "/lclBooking.do?methodName=editBooking&fileNumberId=" + segFileNumberId + "&headerId=" + headerId;
    href = href + "&unitId=" + unitId + "&moduleName=Imports&screenName=LCL FILE&moduleId=" + segFileNumber + "&impSearchFlag=" + impVoyageFlag;
    window.location.href = href;
}

function checkDisposition() {
    var headerId = $("#headerId").val();
    if (headerId !== '' && ($("#dispoId").text() === 'DATA' || $("#dispoId").text() === 'WATR')) {
        $("#pickupDate").addClass("textlabelsBoldForTextBoxDisabledLook");
        $("#pickupDate").attr("readonly", true);
        $('#pickupDateImg').hide();
    } else {
        $("#pickupDate").removeClass("textlabelsBoldForTextBoxDisabledLook");
        $("#pickupDate").attr("readonly", false);
        $('#pickupDateImg').show();
    }
}

function triggerRates(commodityNo) {
    $("#methodName").val("triggerRates");
    $.ajaxx({
        dataType: "json",
        url: $("#lclBookingForm").attr("action"),
        data: $("#lclBookingForm").serialize() + "&commodityNo=" + commodityNo,
        preloading: true,
        success: function (result) {
            $("#commodityDesc").html(result.commodityDesc);
            $("#chargeDesc").html(result.chargeDesc);
        }
    });
}

function checkErtAndRates(trigger) {
    var fileNumberId = $("#fileNumberId").val();
    var orgUncode = $("#originUnlocationCode").val();
    var polUncode = $("#polUnlocationcode").val();
    var podUncode = $("#podUnlocationcode").val();
    var billingType = $("input:radio[name=pcBothImports]:checked").val();
    var notifyNo = $('#notifyCode').val();
    var consNo = $('#consigneeCode').val();
    var clientNo = $('#client_no').val();
    var agentNo = $('#supplierCode').val();
    var disabled = $("#" + (trigger === "Client" ? "clientDisabled" : (trigger === "Notify" ? "notifyDisabled" : (trigger === "Consignee" ? "consDisabled" : "agentDisabled")))).val();
    if (disabled !== "Y") {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LCLImportRatesDAO",
                methodName: "checkErtAndRates",
                param1: orgUncode,
                param2: polUncode,
                param3: podUncode,
                param4: billingType,
                param5: clientNo,
                param6: notifyNo,
                param7: consNo,
                param8: agentNo,
                param9: trigger,
                dataType: "json"
            },
            preloading: true,
            success: function (data) {
                if (null !== data) {
                    if (trigger === "Client") {
                        $('#retailCommodity').val(data.commodityNo);
                    } else if (trigger === "Notify") {
                        $("#notifyCommNo").val(data.commodityNo);
                    } else if (trigger === "Consignee") {
                        $('#coloadCommNo').val(data.commodityNo);
                    } else {
                        $('#agCommodityNo').val(data.commodityNo);
                    }
                    if (fileNumberId !== "") {
                        if ($('#fileStatus').val() === 'M'
                                && (data.message.indexOf("ERT will change to Yes") >= 0
                                        || data.message.indexOf("Do you want to change the tariff# and recalculate rates?") >= 0)) {
                            $.prompt("DR has been posted. It cannot adjust rates, this must be manually done and also the commodity can be manually changed");
                        } else if (data.message.indexOf("ERT will change to Yes") >= 0) {
                            $.prompt(data.message, {
                                buttons: {
                                    "Continue": true,
                                    "Cancel": false
                                },
                                callback: function (v) {
                                    if (v) {
                                        $("#rtdTransaction").val("Y");
                                        triggerRates(data.commodityNo);
                                    } else {
                                        if (trigger === "Client") {
                                            $("#client").val("");
                                            $("#client_no").val("");
                                            $("#email").val("");
                                            $("#address").val("");
                                            $("#otiNumber").val("");
                                            $("#contactName").val("");
                                            $("#phone").val("");
                                            $("#fax").val("");
                                            $("#fmcNumber").val("");
                                            $("#commodityNumber").val("");
                                            $("#retailCommodity").val("");
                                        } else if (trigger === "Notify") {
                                            $("#notifyName").val("");
                                            $("#notifyCode").val("");
                                            $("#notifyCommNo").val("");
                                        } else if (trigger === "Consignee") {
                                            $("#consigneeName").val("");
                                            $("#consigneeCode").val("");
                                            $("#coloadCommNo").val("");
                                        } else {
                                            $("#supplierNameOrg").val("");
                                            $("#supplierCode").val("");
                                            $("#agCommodityNo").val("");
                                        }
                                    }
                                }
                            });
                        } else if (data.message.indexOf("Do you want to change the tariff# and recalculate rates?") >= 0) {
                            $("#rtdTransaction").val("N");
                            $.prompt(data.message, {
                                buttons: {
                                    "Continue": true,
                                    "Cancel": false
                                },
                                callback: function (v) {
                                    if (v) {
                                        triggerRates(data.commodityNo);
                                    }
                                }
                            });
                        } else if ($.inArray(data.message, ["Client rate exists", "Notify rate exists", "Consignee rate exists"]) < 0) {
                            $.prompt(data.message);
                        }
                    }
                } else if (fileNumberId !== "") {
                    $.prompt("This change may impact rates. Make sure you check the tariff#, and the rates below.");
                }
            }
        });
    }
}

function storageCharge(path, fileNumberId, fileNumber) {
    var lastFdDate = $('#lastFd').val();
    var href = path + "/lclImpStorageCharge.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&lastFdDate=" + lastFdDate + "&moduleName=Imports";
    $("#storageCharge").attr("href", href);
    $("#storageCharge").colorbox({
        iframe: true,
        width: "70%",
        height: "70%",
        title: "Storage"
    });
}
function setFinalDestinationForImports(moduleName) {
    var shipment = $('input:radio[name=transShipMent]:checked').val();
    if (shipment.toUpperCase() == 'N') {
        document.getElementById('finalDestinationId').value = $('#portOfDestinationId').val();
        document.getElementById('finalDestinationR').value = $('#portOfDestination').val();
    }
}
function openSop(id) {
    var documentId = $('#' + id).val();
    GB_show("Scan", "/logisoft/scan.do?action=sopDocument&screenName=TRADINGPARTNER&documentName=SOP&documentId=" + documentId, 350, 650);
}
function addCfsDevCharges(fileNumberId) {
    var cfsWarehouseNo = $('#cfsWarehouseNo').val();
    if (cfsWarehouseNo === null || cfsWarehouseNo === "") {
        $.prompt("Cfs Devanning Warehouse is required");
    }
    var iswarehouseCharge = isWareHouseChargeExist(fileNumberId);
    if (iswarehouseCharge) {
        $.prompt("Warehouse charges already exists.");
        return false;
    }
    var data = isWarehouseBill(cfsWarehouseNo);
    if (data[1] != null && data[1] != "") {
        var unitSsId = $('#unitSsId').val();
        $.prompt("Are you sure want to calculate warehouse charges?", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    showProgressBar();
                    $("#methodName").val("calculateImpCfsCharge");
                    var params = $("#lclBookingForm").serialize();
                    params += "&fileNumberId=" + fileNumberId + "&unitSsId=" + unitSsId;
                    $.post($("#lclBookingForm").attr("action"), params,
                            function (data) {
                                $("#chargeDesc").html(data);
                                $("#chargeDesc", window.parent.document).html(data);
                                hideProgressBar();
                            });
                } else if (v === 2) {
                    $.prompt.close();
                }
            }
        });
    } else {
        $.prompt("CFS Devanning Warehouse does not have IPI vendor");
    }
}
function isWarehouseBill(cfsWarehouseNo) {
    var flag;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.WarehouseDAO",
            methodName: "getWarehouseAccountNo",
            param1: cfsWarehouseNo,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}
function isWareHouseChargeExist(fileNumberId) {
    var flag;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO",
            methodName: "isWareHouseChargeExist",
            param1: fileNumberId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}
function disablecfsChargeBtn() {
    var fileStatus = $('#fileStatus').val();
    var unitCollect = $('#unitCollect').val();
    if (fileStatus === 'M' && (unitCollect == 'P' ||
            $('#podUnlocationcode').val() !== $('#unlocationCode').val())) {
        $("#cfsDevCharge").hide();
    }
}

function yesPrompt(origin, destination, pol, pod) {
    $.prompt("Autorates will be Recalculated. Are you sure want to continue?", {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                submitAjaxForm("calculateImportCharges", '#lclBookingForm', '#chargeDesc', origin, destination, pol, pod, '', '', '');
            } else if (v === 2) {
                $("#finalDestinationR").val("");
                $.prompt.close();
            }
        }
    });
}

function confirmBussinessUnitInImport(fileNumberId, newBrand) {
    var oldBrand = $("#oldBrand").val();
    var userId = $("#loginUserId").val();
    var oldValue = oldBrand === "ECU" ? "Ecu Worldwide" : oldBrand === "ECI" ? "Econo" : "OTI";
    var newValue = newBrand === "ECU" ? "Ecu Worldwide" : newBrand === "ECI" ? "Econo" : "OTI";
    var textMessage = "Please note that the Brand is changing from <span style=color:red>"
            + oldValue + "</span>" + " to " + "<span style=color:red>" + newValue + "</span>.";
    if (fileNumberId !== null && fileNumberId !== "" && getPreviousBusinessUnit(newBrand, fileNumberId)) {
        $.prompt(textMessage, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    $('#oldBrand').val(newBrand);
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.lcl.dwr.LclDwr",
                            methodName: "updateBusinessUnit",
                            param1: fileNumberId,
                            param2: newBrand,
                            param3: "",
                            param4: userId,
                            param5: oldBrand,
                            dataType: "json"
                        }
                    });
                } else {
                    switch (oldBrand) {
                        case "ECU":
                            $('#ECU').attr("checked", true);
                            break;
                        case "OTI":
                            $('#OTI').attr("checked", true);
                            break;
                        case "ECI":
                            $('#ECI').attr("checked", true);
                            break;
                    }
                    $.prompt.close();
                }
            }
        });
    }
}
function updateBussinessUnit() {
    var fileNumberId = $("#fileNumberId").val();
    var originAcctNo = $("#supplierCode").val();
    var oldBrand = $("#oldBrand").val();
    var userId = $("#loginUserId").val();
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "updateBusinessUnit",
            param1: fileNumberId,
            param2: "",
            param3: originAcctNo,
            param4: userId,
            param5: oldBrand,
            dataType: "json",
            async: false
        },
        success: function (data) {
            switch (data) {
                case "ECU":
                    $("#oldBrand").val(data);
                    $('#ECU').attr("checked", true);
                    break;
                case "ECI":
                    $("#oldBrand").val(data);
                    $('#ECI').attr("checked", true);
                    break;
            }
        }
    });
}
function termsTodoBl() {
    if ($("#podUnlocationcode").val() !== "") {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "fillTermstodoBlForPod",
                param1: $('#podUnlocationcode').val(),
                request: "true",
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data[0] !== null && data[1] !== null) {
                    $("#terminal").val(data[0] + " / " + data[1]);
                    $("#trmnum").val(data[1]);
                }
            }
        });
    }
}
// updating file status  B when we change transhipment Y to N
function updateFileStatus(fileId, fileStatus) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO",
            methodName: "updateLclFileNumbersStatus",
            param1: fileId,
            param2: fileStatus,
            dataType: "json"
        },
        async: false,
        close: function () {
        }
    });
}

function checkForNumber(obj) {
    if (!/^([0-9]{0,10})$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This field should be Numeric");
    }
}
function fillAesItn() {
    var fileId = $('#fileNumberId').val();
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "fillAesItnValues",
            param1: fileId,
            param2: "AES_EXCEPTION",
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data == '1') {
                $("#Baes").addClass("green-background");
                $("#aesB").addClass("green-background");
            }
        }
    });
}


function cancelAdd() {
    document.body.removeChild(document.getElementById("docListDiv"));
    closePopUp();
}
function useSelectedAccount() {
    var tpFlag = $('#tpFlag').val();
    var existingaccount = $('input:radio[name=existingaccount]:checked').val();
    if (existingaccount !== "" || existingaccount !== null) {
        var tpFlag = $('#tpFlag').val();
        if (tpFlag === "LCL_IMPORT_SHIPPER") {
            setValues(existingaccount, 'shipperName', 'shipperCode', 'shipperAddress', 'shipperCity', 'shipperState', 'shipperZip', 'shipperCountry', 'shipperPhone', 'shipperFax', 'shipperEmail', 'shipperSalesPersonCode', 'shipUnlocCode', '', 'newShipper', '');
        } else if (tpFlag === "LCL_IMPORT_CONSIGNEE") {
            setValues(existingaccount, 'consigneeName', 'consigneeCode', 'consigneeAddress', 'consigneeCity', 'consigneeState', 'consigneeZip', 'consigneeCountry', 'consigneePhone', 'consigneeFax', 'consigneeEmail', 'consigneeSalesPersonCode', 'consigneeUnLocCode', 'consigneeColoadRetailRadio', 'newConsignee', 'editTpConsignee');
        } else if (tpFlag === "LCL_IMPORT_NOTIFY") {
            setValues(existingaccount, 'notifyName', 'notifyCode', 'notifyAddress', 'notifyCity', 'notifyState', 'notifyZip', 'notifyCountry', 'notifyPhone', 'notifyFax', 'notifyEmail', 'notifySalesPersonCode', 'notifyUnLocCode', 'notifyColoadRetailRadio', 'newNotify', 'editTpNoty');
        } else if (tpFlag === "LCL_IMPORT_NOTIFY2") {
            setValues(existingaccount, 'notify2Name', 'notify2Code', 'notify2Address', 'notify2City', 'notify2State', 'notify2Zip', 'notify2Country', 'notify2Phone', 'notify2Fax', 'notify2Email', 'notify2SalesPersonCode', 'notify2UnLocCode', 'notify2ColoadRetailRadio', 'newNotify2', '');
        }
        cancelAdd();
    } else {
        $.prompt("Please select Any account")
        return false;
    }
}
function setValues(existingaccount, aaccoutName, accountNo, address, city, state, zip, country, phone, fax, email, salesPersonCode, unlocCode, coloadRetail, newCheckBoxValue, editCheckboxValue) {
    var existingaccountDetails = existingaccount.split('==');
    $('#' + aaccoutName).val(existingaccountDetails[0]);
    $('#' + accountNo).val(existingaccountDetails[1]);
    $('#' + address).val(existingaccountDetails[3]);
    $('#' + city).val(existingaccountDetails[4]);
    $('#' + state).val(existingaccountDetails[5]);
    $('#' + zip).val(existingaccountDetails[6]);
    $('#' + country).val(existingaccountDetails[7]);
    $('#' + phone).val(existingaccountDetails[8]);
    $('#' + email).val(existingaccountDetails[9]);
    $('#' + fax).val(existingaccountDetails[10]);
    $('#' + salesPersonCode).val('');
    $('#' + unlocCode).val(existingaccountDetails[2]);
    $('#' + coloadRetail).val('');
    $('#' + newCheckBoxValue).attr('checked', false);
    $('#' + editCheckboxValue).attr('checked', false);
}
function toggleDoorDeliveryComment() {
    var pickupYesNo = $('input:radio[name=pooDoor]:checked').val();
    var shipment = $('input:radio[name=transShipMent]:checked').val();
    if (pickupYesNo === "Y" || shipment === 'Y') {
        $('#doorDeliveryCommentN').attr('checked', true);
        $("#doorDeliveryCommentN").attr("disabled", true);
        $("#doorDeliveryCommentY").attr("disabled", true);
    } else {
        $("#doorDeliveryCommentN").attr("disabled", false);
        $("#doorDeliveryCommentY").attr("disabled", false);
    }
}
function isPaidCostAndCharges(fileId, fileNumber) {
    var text = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "validateImpCostChargeStatus",
            param1: fileId,
            param2: fileNumber,
            dataType: "json"
        },
        success: function (data) {
            if (data !== "" && data !== null) {
                text = data;
            }
        }
    });
    return text;
}
function reverseImportsCharges() {
    var fileId = $("#fileNumberId").val();
    var unitSsId = $('#unitSsId').val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO",
            methodName: "reverseImportsCharges",
            param1: fileId,
            param2: unitSsId,
            request: "true"
        },
        success: function (data) {
            if (data === "success") {
                submitForm('editBooking');
            }
        }
    });
}