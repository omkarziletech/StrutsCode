var path = "/" + window.location.pathname.split('/')[1];
/*
 *  Document   : lclImpQuote
 *  Author     : Mei
 */
$(document).ready(function () {
    toggleUpcomingSailings();
    checkTranshipment();
    setCreditStatus("notifyCreditClient", "notifyCredit");
    setShipperTooltip();
    setConsigneeTooltip();
    setNotyTooltip();
    setNotify2Tooltip();//tootipdetails for Notify2
    showIpiCfsAddress();
    showNotify2ManualCheck();
    setBillToParty();//based on billing type enable or disable radio button
    setIPICFSstate();//code for assigning search state value in IFC
    shipperAcctTypeCheck();
    consigneeAcctTypeCheck();
});
//Import Details Tab hide or Expand
function toggleImportDetails() {
    var disp = document.getElementById('hide').innerHTML;
    if (disp === "Click to Hide" || disp === "") {
        document.getElementById('hide').innerHTML = "";
        document.getElementById('hide').innerHTML = "Click to Expand";
    }
    else {
        document.getElementById('hide').innerHTML = "";
        document.getElementById('hide').innerHTML = "Click to Hide";
    }
    jQuery('#importDetails').slideToggle();
}
//Imports
function checkTranshipment() {
    if ($('#podUnlocationcode').val() !== $('#unlocationCode').val()) {
        $("#stGeorgeAccount").removeClass("textlabelsBoldForTextBoxDisabledLook");
        $("#stGeorgeAccount").addClass("textlabelsBoldForTextBox");
        $("#ipiSearchEdit").show();
        $("#stGeorgeAccount").attr("readonly", false);
    } else {
        $("#stGeorgeAccount").addClass("textlabelsBoldForTextBoxDisabledLook");
        $("#stGeorgeAccount").attr("readonly", true);
        $("#ipiSearchEdit").hide();
        $('#stGeorgeAccount').val('');
        $('#stGeorgeAccountNo').val('');
        $('#stGeorgeAddress').val('');
        $('#etaFDDate').val('');
        $('#fd-container').hide();
    }
    var shipment = $('input:radio[name=transShipMent]:checked').val();
    if (shipment === 'Y') {
        // $('#polLabel').attr('width', '14%');
        //$('#polDojo').attr('width', '10%');
        $('#usaExit').show();
        $('#portDiscg').show();
        $('#portSpecialRemarks').show();
        $('#portInternalRemarks').show();
        $('#griRemarks').show();
        $('#finalDestinationR').attr('alt', 'CONCAT_RELAY_NAME_FD');
        $('#portRemark').addClass("black-border");
        $('#regionalRemark').addClass("black-border");
        $('#defaultAgentDetails').show();
        $('#m').show();
        $('#agentNumberDetails').show();
        $('#upcomingImpSection').show();
    }
    else {
        //  $('#polLabel').attr('width', '14%');
        //$('#polDojo').attr('width', '20%');
        $('#defaultAgentDetails').hide();//Default Agent Details
        $('#m').hide();//Agent Name,Number Details
        $('#agentNumberDetails').hide();//Agent Name,Number Details
        $('#usaExit').hide();
        $('#portDiscg').hide();
        $('#portSpecialRemarks').val('');
        $('#portInternalRemarks').val('');
        $('#griRemarks').val('');
        $('#portSpecialRemarks').hide();
        $('#portInternalRemarks').hide();
        $('#griRemarks').hide();
        $('#finalDestinationR').attr('alt', 'DEST_UNLOC');
        $('#portRemark').removeClass("black-border");
        $('#regionalRemark').removeClass("black-border");
        $('#upcomingImpSection').hide();
        $('#agentInfo').val('ECOIMP0001');
    }
//    externalGRIRemarks();
}

function externalGRIRemarks() {
    var foreignDischarge = $('#finalDestinationR').val();
    var foreigndischarge = foreignDischarge.substring(foreignDischarge.indexOf('(') + 1, foreignDischarge.indexOf(')'));
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "defaultDestinationImportRemarks",
            param1: foreigndischarge,
            dataType: "json"
        },
        success: function (data) {
            if (data[2] !== undefined && data[2] !== "" && data[2] !== null) {
                $('#externalGRIRemarks').val(data[2]);
            } else {
                $('#externalGRIRemarks').val('');
            }
        }
    });

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
function setToolTipDetails(headingName, acctCode, acctName, toolTip) {
    var moduleName = $('#moduleName').val();

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
function submitAmsHblPopupBox(methodName, fileNumberId, selector) {//AMS House Bl Details
    if ($('#amsHblNo').val() === null || $('#amsHblNo').val() === "") {
        $.prompt('AMS No is required');
        $("#amsHblNo").css("border-color", "red");
    } else {
        showProgressBar();
        $('#methodName').val(methodName);
        var params = $("#lclQuoteForm").serialize();
        params += "&fileNumberId=" + fileNumberId;
        $.post($("#lclQuoteForm").attr("action"), params, function (data) {
            $(selector).html(data);
            $(selector, window.parent.document).html(data);
            hideProgressBar();
        });
        $('#amsHblNo').val('');
        $('#amsHblPiece').val('');
        $('#amsHblBox').hide();
    }
}
function deleteAmsHbl(txt) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                $('#methodName').val('deleteImpAmsHBL');
                var params = $("#lclQuoteForm").serialize();
                var lcl3pRefId = $("#custId").val();
                var fileNumberId = $("#fileNumberId").val();
                params += "&fileNumberId=" + fileNumberId + "&lcl3pRefId=" + lcl3pRefId;
                $.post($("#lclQuoteForm").attr("action"), params, function (data) {
                    $('#amsHblList').html(data);
                    $("#amsHblList", window.parent.document).html(data);
                    hideProgressBar();
                });
                $.prompt.close();
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
// Default Agent for Import Origin Agent based on POLUnLocationCode for non-transhipment
function filldefaultAgentForImports() {
    var agentCount = $('#agentCount').val();
    var moduleName = $('#moduleName').val();
    if (!agentCount) {
        var portOfLoading = $('#portOfLoading').val();
        var polUnlocation = portOfLoading.substring(portOfLoading.indexOf('(') + 1, portOfLoading.indexOf(')'));
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getDefaultAgentForLcl",
                param1: polUnlocation,
                param2: 'I',
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data[3] === 'Y') {
                    $.prompt("This customer is disabled and merged with " + data[4] !== null ? data[4] : "");
                    $("#supplierNameOrg").val('');
                    $("#supplierCode").val('');
                } else {
                    if (data[0] !== undefined && data[0] !== "" && data[0] !== null) {
                        $("#supplierCode").val(data[0]);
                        $("#supplierNameOrg").val(data[1]);
                        if (moduleName === 'Imports') {
                            updateBussinessUnit();
                        }
                    } else {
                        $("#supplierNameOrg").val('');
                        $("#supplierCode").val('');
                    }
                }
            }
        });
    }
}
// refresh origin Agent details based on POL Code for non-transhipment
function submitAjaxFormForImpAgent(methodName, formName, selector, id) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&id=" + id;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                filldefaultAgentForImports();
                hideProgressBar();
            });
}
//Create New TP Method for Green Plus Icon
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
                    if (null !== data && data !== "") {
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
            $.prompt(errorMsg);
        }
    }
// don't remove this code
//   window.parent.GB_showFullScreen("Trading Partner", path+"/jsps/Tradingpartnermaintainance/SearchCustomer.jsp?callFrom="+callfrom+"&programid=156&lclFlag=lcl&field="+vendorFlag+"&vacctname="+vendor+"&vAddress="+vaddress+"&vCity="+vCity+"&vUnLocCode="+vunLocCode)
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
                var href = path + "/lclNewTPDetails.do?buttonValue=" + vendorflag + "&accountName=" + $('#' + vendorName).val() + "&accountType=" + acctType + "&address=" + $('#' + address).val();
                href = href + "&address1=" + $('#' + address).val() + "&city=" + $('#' + city).val() + "&state=" + $('#' + state).val() + "&zip=" + $('#' + zipCode).val();
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
function createNewTp(path) {
    var tpFlag = $('#tpFlag').val();
    var acctNo = $('#tpacctNo').val();
    if (tpFlag === "LCL_IMP_QUOTE_SHIPPER") {
        if (acctNo !== "" && acctNo !== null) {
            createConsigneeAcctShipperAcct('shipperName', acctNo, 'S');
        } else {
            addNewTradingPartner(path, tpFlag, 'S', 'dupShipperName', 'shipperAddress', 'shipperCity', 'shipperState',
                    'shipUnlocCode', 'shipperPhone', 'shipperFax', 'shipperEmail', 'shipperZip', 'shipperSalesPersonCode', 'shipperColoadRetailRadio');
        }
    } else if (tpFlag === "LCL_IMP_QUOTE_CONSIGNEE") {
        if (acctNo !== "" && acctNo !== null) {
            createConsigneeAcctShipperAcct('consigneeName', acctNo, 'C');
        } else {
            addNewTradingPartner(path, tpFlag, 'C', 'dupConsigneeName', 'consigneeAddress', 'consigneeCity', 'consigneeState',
                    'consigneeUnLocCode', 'consigneePhone', 'consigneeFax', 'consigneeEmail', 'consigneeZip', 'consigneeSalesPersonCode', 'consigneeColoadRetailRadio');
        }
    } else if (tpFlag === "LCL_IMP_QUOTE_NOTIFY") {
        addNewTradingPartner(path, tpFlag, 'C', 'dupNotifyName', 'notifyAddress', 'notifyCity', 'notifyState',
                'notifyUnLocCode', 'notifyPhone', 'notifyFax', 'notifyEmail', 'notifyZip', 'notifySalesPersonCode', 'notifyColoadRetailRadio');
    } else if (tpFlag === "LCL_IMP_QUOTE_NOTIFY2") {
        addNewTradingPartner(path, tpFlag, 'C', 'dupNotify2Name', 'notify2Address', 'notify2City', 'notify2State',
                'notify2UnLocCode', 'notify2Phone', 'notify2Fax', 'notify2Email', 'notify2Zip', 'notify2SalesPersonCode', 'notify2ColoadRetailRadio');
    }
    cancelAdd();
}



//set New TP Values for Pop Close Icon
function setNewTPInfoDetails(accountName, accountNo, callFrom) {
    if (callFrom === "LCL_IMP_QUOTE_SHIPPER") {
        $("#dupShipperName").val('');
        $('#newShipper').attr('checked', false);
        $("#manualShipper").hide();
        $("#dojoShipper").show();
        $('#shipperName').val(accountName);
        $('#shipperCode').val(accountNo);
        showShipDetails();
    } else if (callFrom === "LCL_IMP_QUOTE_CONSIGNEE") {
        $("#dupConsigneeName").val('');
        $('#newConsignee').attr('checked', false);
        $("#manualConsignee").hide();
        $("#dojoConsignee").show();
        $('#consigneeName').val(accountName);
        $('#consigneeCode').val(accountNo);
        showConsDetails();
    } else if (callFrom === "LCL_IMP_QUOTE_NOTIFY") {
        $("#dupNotifyName").val('');
        $('#newNotify').attr('checked', false);
        $("#manualNotify").hide();
        $("#dojoNotify").show();
        $('#notifyName').val(accountName);
        $('#notifyCode').val(accountNo);
        showNotyDetails();
    } else if (callFrom === "LCL_IMP_QUOTE_NOTIFY2") {
        $("#dupNotify2Name").val('');
        $('#newNotify2').attr('checked', false);
        $("#manualNotify2").hide();
        $("#dojoNotify2").show();
        $('#notify2Name').val(accountName);
        $('#notify2Code').val(accountNo);
        showNotify2Details();
    }
}
//display Address TP Details for Shipper,Consignee & Notify for Floder Icon
function displayTradingDetails(acctNo, acctName, maualacct, acctType, address, city, state, country, phone, fax, email, zipCode, checkBoxValue, salesPersonCode, coloadRetailRadio) {
    var fileId = $('#fileNumberId').val();
    if (fileId !== null && fileId !== "" && fileId !== '0') {
        var vendorName = "";
        if ($('#' + acctName).val() !== '') {
            vendorName = $('#' + acctName).val();
        } else {
            vendorName = $('#' + maualacct).val();
        }
        var newCheck = $('#' + checkBoxValue).is(":checked");
        var href = path + "/tradingContact.do?methodName=display&fileId=" + fileId + "&acctNo=" + $('#' + acctNo).val() + "&acctName=" + vendorName + "&vendorType=" + acctType + "&fileType=Quote&checkValue=" + newCheck;
        href = href + "&address=" + $('#' + address).val() + "&city=" + $('#' + city).val() + "&state=" + $('#' + state).val() + "&zip=" + $('#' + zipCode).val();
        href = href + "&fax=" + $('#' + fax).val() + "&phone=" + $('#' + phone).val() + "&email=" + $('#' + email).val() + "&country=" + $('#' + country).val() + "&salesPersonCode=" + $("#" + salesPersonCode).val() + "&importQuoteColoadRetail=" + $("#" + coloadRetailRadio).val();
        $(".tradingDetails").attr("href", href);
        $(".tradingDetails").colorbox({
            iframe: true,
            width: "60%",
            height: "50%",
            title: "Address Details"
        });
    } else {
        $.prompt("Please Save Quote");
    }
}

function showIpiCfsAddress() {
    var acctNo = $('#stGeorgeAccountNo').val();
    if ($('#podUnlocationcode').val() !== $('#unlocationCode').val() && acctNo !== "" && acctNo !== null) {
        var ipiCFSAddress = "";
        if ($('#ipiCfsFirmsCode').val() !== null && $('#ipiCfsFirmsCode').val() !== '') {
            ipiCFSAddress += "FIRMS CODE:" + $('#ipiCfsFirmsCode').val() + "\n";
        }
        if ($('#ipiCfsCoName').val() !== null && $('#ipiCfsCoName').val() !== '') {
            ipiCFSAddress += $('#ipiCfsCoName').val() + "\n";
        }
        if ($('#ipiCfsaddress').val() !== null && $('#ipiCfsaddress').val() !== '') {
            ipiCFSAddress += $('#ipiCfsaddress').val() + "\n";
        }
        if ($('#ipiCfsCity').val() !== null && $('#ipiCfsCity').val() !== '') {
            ipiCFSAddress += $('#ipiCfsCity').val() + ",";
        }
        if ($('#ipiCfsState').val() !== null && $('#ipiCfsState').val() !== '') {
            ipiCFSAddress += $('#ipiCfsState').val() + '-';
        }
        if ($('#ipiCfsZip').val() !== null && $('#ipiCfsZip').val() !== '') {
            ipiCFSAddress += $('#ipiCfsZip').val() + "\n";
        }
        if ($('#ipiCfsPhone').val() !== null && $('#ipiCfsPhone').val() !== '') {
            ipiCFSAddress += "Phone:" + $('#ipiCfsPhone').val() + "\n";
        }
        if ($('#ipiCfsFax').val() !== null && $('#ipiCfsFax').val() !== '') {
            ipiCFSAddress += "Fax:" + $('#ipiCfsFax').val();
        }
        $('#stGeorgeAddress').val(ipiCFSAddress);
    }
}
function showNotify2Details() {//Notify2 ToolTip
    setToolTipDetails('Notify2', 'notify2Code', 'notify2Name', 'notify2');
}
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
function notify2AccttypeCheck() {
    var acctType = jQuery("#notify2AcctType").val();
    if ($('#noty2Disabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#noty2DisableAcct').val() + "</span>");
        clearNotify2Values();
    } else {
        if (acctType !== "" && acctType.indexOf('C') === -1) {
            $.prompt("Please select the customers with account type C");
            clearNotify2Values();
        } else {
            showNotify2Details();
            notesCount(path, 'noty2Notes', $('#notify2Code').val(), 'N2');
            var creditDetails = $('#notify2CreditDetails').val();
            if (creditDetails !== "") {
                jQuery('#notify2CreditValues').text(creditDetails);
            } else {
                jQuery('#notify2CreditValues').text('');
            }
        }
    }
}
$(document).ready(function () {

    $('#shipperName').keyup(function () {
        if ($('#shipperName').val() === "") {
            clearShipperValues();
            $('#shpNotes').attr("src", path + "/img/icons/e_contents_view.gif");
            setToolTipDetails('Shipper', 'shipperCode', 'shipperName', 'shipper');
        }
    });
    $('#notify2Name').keyup(function () {
        if ($('#notify2Name').val() === "") {
            clearNotify2Values();
        }
    });
    $('#consigneeName').keyup(function () {
        var consignee = $('#consigneeName').val();
        if (consignee === "") {
            clearConsigneeValues();
        }
    });
    $('#foreignDischarge').keyup(function () {
        if ($('#foreignDischarge').val() == "") {
            $('#specialRemarks').val('');
            $('#portInternalRemarks').val('');
            $('#portGriRemarks').val('');
        }
    });
    $('#finalDestinationR').keyup(function () {
        if ($('#finalDestinationR').val() == "") {
            $('#specialRemarks').val('');
            $('#internalRemarks').val('');
            $('#portGriRemarks').val('');
        }
    });
});
function clearNotify2Values() {// clear all notify2 id values
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
    $("#notify2AcctType").val('');
    $("#noty2Notes").attr("src", path + "/img/icons/e_contents_view.gif");
    $("#notify2SalesPersonCode").val('');
    jQuery('#notify2CreditValues').text('');
}
function freightReleasePopUp(path, fileId) {//Freight Release PopUp
    if (fileId !== null && fileId !== "" && fileId !== '0') {
        var expressRelease = $('input:radio[name=expressReleaseClasuse]:checked').val();
        var entryNo = $('#entryNo').val();
        var href = path + "/importRelease.do?methodName=display&moduleName=LCL_IMP_QUOTE&fileNumberId=" + fileId + "&expressRelease=" + expressRelease + "&entryNo=" + entryNo;
        $("#freightRelease").attr("href", href);
        $("#freightRelease").colorbox({
            iframe: true,
            width: "80%",
            height: "80%",
            title: "Freight Release"
        });
    }
    else {
        $.prompt("Please Save Quote");
    }
}
function setBillToParty() {///set Bill to party
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
function setBillingType(val) {//Set billingType based on BillTOParty
    if ($('#radioP').attr('checked')) {
        $('#billA').attr("disabled", false);//Enable Agent
        $('#billA').attr("checked", true);
        $('#billC').attr("disabled", true);//disable third,cons,notify
        $('#billC').removeAttr("checked");
        $('#billT').attr("disabled", true);
        $('#billT').removeAttr("checked");
        $('#billN').attr("disabled", true);
        $('#billN').removeAttr("checked");
    } else if ($('#radioC').attr('checked')) {
        $('#billA').attr("disabled", true);//disable agent
        $('#billA').removeAttr("checked");
        $('#billC').attr("disabled", false);//enable third,cons,notify
        $('#billT').attr("disabled", false);
        $('#billN').attr("disabled", false);
        if (val === "defaultC") {
            $('#billC').attr("checked", true);//Default Consignee enable
        }
    }
}
function updateBillToCode() {//any modification in BillToParty based on BilingType also
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
                        submitAjaxFormforBill("updateBillToCode", "#lclQuoteForm", "#chargeDesc", billType, billToParty);
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
function setEnableBillToCode() {//commom MethoD for BillingType
    $('#billC').attr("disabled", false);
    $('#billT').attr("disabled", false);
    $('#billN').attr("disabled", false);
    $('#billA').attr("disabled", true);
}
function submitAjaxFormforBill(methodName, formName, selector, billType, billToParty) {//ajax Call for BillToParty Changes MethodName---(updateBillToCode)
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&pcBothImports=" + billType + "&billtoCodeImports=" + billToParty + "&exitBillToCode=" + $('#hiddenBillToCode').val() + "&existBillingType=" + $('#existBillingType').val();
    $.post($(formName).attr("action"), params, function (data) {
        $(selector).html(data);
        $(selector, window.parent.document).html(data);
        hideProgressBar();
    });
    $('#hiddenBillToCode').val(billToParty);
    $('#existBillingType').val(billType);
}
function setClearIdValues(id1, id2) {//Common Method for Clear Id Values
    $('#' + id1).val('');
    $('#' + id2).val('');
}

function calculateConsColoadRates(text, fileId, pol, pod, coloadComm, billingType, headingName, ertData) {//ajax Call ColoadConsignee (MethodName---->checkColoadRates)
    $.prompt(text, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                $("#methodName").val("setColoadCommRates");
                var params = $("#lclQuoteForm").serialize();
                params += "&fileNumberId=" + fileId + "&polUnCode=" + pol + "&podUnCode=" + pod + "&coloadComm=" + coloadComm + "&billingType=" + billingType + "&ratesFlag=commodity";
                if (ertData === "true") {
                    $("#rtdTransaction").val('Y');
                    checkingERTLogic(headingName, fileId, "store");
                }
                $.post($("#lclQuoteForm").attr("action"), params,
                        function (data) {
                            $("#commodityDesc").html(data);
                            $("#commodityDesc", window.parent.document).html(data);
                            $("#methodName").val("setColoadCommRates");
                            var params1 = $("#lclQuoteForm").serialize();
                            params1 += "&fileNumberId=" + fileId + "&polUnCode=" + pol + "&podUnCode=" + pod + "&coloadComm=" + coloadComm + "&billingType=" + billingType + "&ratesFlag=charge";
                            $.post($("#lclQuoteForm").attr("action"), params1,
                                    function (data) {
                                        $("#chargeDesc").html(data);
                                        $("#chargeDesc", window.parent.document).html(data);
                                        hideProgressBar();
                                        parent.$.fn.colorbox.close();
                                    });
                        });
                $.prompt.close();
            }
            else if (v === 2) {
                if (ertData === "true") {
                    if (headingName === "Consignee") {
                        $('#consigneeCode').val('');
                        $('#consigneeName').val('');
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
                    } else if (headingName === "Agent") {
                        $('#supplierCode').val('');
                        $('#supplierNameOrg').val('');
                    }
                }
                $.prompt.close();
            }
        }
    });
}

function calculateRatesPolFD() {//Rates Calculation in POL and Fd
    var fileId = $('#fileNumberId').val();
    if (fileId !== "") {
        var destination = getDestination();
        var pol = $('#polCode').val();
        var pod = $('#podCode').val();
        var oldDesnCode = $('#oldDesnCode').val();
        var fdUnlocationCode = $("#unlocationCode").val();
        var origin = getOrigin();
        $("#methodName").val("calculateImpPodFdRates");
        var params = $('#lclQuoteForm').serialize();
        params += "&finalDestinationId=" + $('#finalDestinationId').val() + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod +
                "&fileNumberId=" + $('#fileNumberId').val() + "&oldDesnCode=" + oldDesnCode + "&origin=" + origin;
        if ((oldDesnCode !== "" && fdUnlocationCode !== "") && oldDesnCode !== fdUnlocationCode) {
            $.prompt("Autorates will be Recalculated. Are you sure want to continue?", {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (V) {
                    if (V === 1) {
                        submitAjaxForm("calculateImportCharges", '#lclQuoteForm', '#chargeDesc', origin, destination, pol, pod);
                        $('#oldDesnCode').val(destination);
                    } else if (V === 2) {
                        $("#finalDestinationR").val("");
                        $.prompt.close();
                    }
                }
            });
        } else {
            showProgressBar();
            $.post($('#lclQuoteForm').attr("action"), params,
                    function (data) {
                        $('#chargeDesc').html(data);
                        $('#chargeDesc', window.parent.document).html(data);
                        if ($("#validateImpRates").val() !== "") {
                            $.prompt($("#validateImpRates").val());
                        }
                        hideProgressBar();
                        $('#oldDesnCode').val(destination);
                    });
        }
    }
}
function setTranshipment() {//Create Transhipment for remarks,
    var transhipment = $('input:radio[name=transShipMent]:checked').val();
    if (transhipment === 'Y') {
        $('#agentInfo').val('');
        var fd = $('#finalDestinationR').val();
        var fdUnlocationCode = fd.substring(fd.indexOf('(') + 1, fd.indexOf(')'));
        if (fdUnlocationCode !== "") {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "defaultDestinationImportRemarks",
                    param1: fdUnlocationCode,
                    dataType: "json"
                },
                async: false,
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
        if ($("#finalDestinationId").val() !== "" && $("#portOfDestinationId").val() !== "") {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "fillForeignPODImp",
                    param1: $("#portOfDestinationId").val(),
                    param2: $("#finalDestinationId").val(),
                    dataType: "json"
                },
                async: false,
                success: function (data) {
                    if (data !== null) {
                        if (data[0] !== "" && data[0] !== null) {
                            showProgressBar();
                            $("#foreignDischargeId").val(data[0]);
                            $("#foreignDischarge").val(data[1] + "/" + data[3] + "(" + data[2] + ")");
                            $('#portExitId').val(data[4]);
                            $('#portExit').val(data[5] + "/" + data[7] + "(" + data[6] + ")");
                            hideProgressBar();
                            showUpcomingSailingsForImp();
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
    } else {
        $('#rtdTransaction').val('Y');
        $('#agentInfo').val('ECOIMP0001');
    }
}
function showUpcomingSailingsForImp() {//Ajax Call For Upcoming Sailings
    var origin = $('#portOfDestinationId').val();
    var fd = $('#finalDestinationId').val();
    var pol = $('#portExitId').val();
    var pod = $('#foreignDischargeId').val();
    if (origin !== '' && fd !== '' && pol !== '' && pod !== '') {
        submitAjaxFormForImpVoyage('displayVoyage', '#lclQuoteForm', '#upcomingSailing', false);
    }
    submitAjaxFormForImpVoyage('refreshAgent', '#lclQuoteForm', '#m', true);
    impUpcomingTab();
}
function impUpcomingTab() {//Set upcomingTab for origin,Pol,Pod and Fd Values
    $('#originSailing').text($("#portOfDestination").val());
    $('#polSailing').text($("#portExit").val());
    $('#podSailing').text($("#foreignDischarge").val());
    $('#destinationSailing').text($("#finalDestinationR").val());
}
function submitAjaxFormForImpVoyage(methodName, formName, selector, id) {//Ajax Call For Upcoming Sailings (Method--->showUpcomingSailingsForImp)
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                if (id) {
                    fillImpAgent();//Set Default Agent Based on FD Transhipment File
                }
            });
    hideProgressBar();
}
function fillImpAgent() {//Set Default Agent Based on FD Transhipment File
    var transhipment = $('input:radio[name=transShipMent]:checked').val();
    if (transhipment === 'Y') {
        var fd = $('#finalDestinationR').val();
        var fdUnlocationCode = fd.substring(fd.indexOf('(') + 1, fd.indexOf(')'));
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getDefaultAgentForLcl",
                param1: fdUnlocationCode,
                param2: 'L',
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data[3] === 'Y') {
                    $.prompt("This customer is disabled and merged with " + data[4] !== null ? data[4] : "");
                    $("#agentName").val('');
                    $("#agentNumber").val('');
                } else {
                    if (data[0] !== undefined && data[0] !== "" && data[0] !== null) {
                        $('#agentName').val(data[1]);
                        $('#agentNumber').val(data[0]);
                        $("#agentName").addClass("text-readonly");
                        $("#agentName").addClass("textlabelsBoldForTextBoxDisabledLook");
                        $("#agentNumber").addClass("textlabelsBoldForTextBoxDisabledLook");
                        $("#agentName").attr("readonly", true);
                        $("#agentNumber").attr("readonly", true);
                        $("#agentInfo").val(data[0]);
                        $("#rtdTransaction").val('Y');
                    }
                    else {
                        $("#agentName").val('');
                        $("#agentNumber").val('');
                    }
                }
            }
        });
    }
}
// upcoming Sailings tab  hide or show
function toggleUpcomingSailings() {
    var transhipment = $('input:radio[name=transShipMent]:checked').val();
    if (transhipment === 'Y') {
        var disp = document.getElementById('upcomingSailing').style.display;
        if (disp === "block" || disp === "") {
            document.getElementById('exp').innerHTML = "";
            document.getElementById('col').innerHTML = "Click to Expand";
            jQuery('#upcomingSailing').slideToggle();
        }
        else {
            document.getElementById('col').innerHTML = "";
            document.getElementById('exp').innerHTML = "Click to Hide";
            jQuery('#upcomingSailing').slideToggle();
        }
        impUpcomingTab();
    }
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
function setIPICFSstate() {//code for assigning search state value in IPICFS
    if ($('#podUnlocationcode').val() !== $('#unlocationCode').val()) {
        var finalDestn = $("#finalDestinationR").val();
        var state = finalDestn.substring(finalDestn.lastIndexOf("/") + 1, finalDestn.lastIndexOf("("));
        state = "'" + state + "'";
        $("#ipiSearchState").val(state);
        $('#stGeorgeAccount').attr('alt', 'IMPORT_CFS_STATE');
    } else {
        $('#ipiSearchState').val('');
        $('#stGeorgeAccount').attr('alt', 'IMPORT_CFS');
    }
}
function deleteAutoRates() {
    var fileId = $('#fileNumberId').val();
    if (fileId !== null && fileId !== "" && fileId !== '0') {
        showProgressBar();
        $("#methodName").val("deleteAutoCharges");
        var params = $("#lclQuoteForm").serialize();
        params += "&fileNumberId=" + fileId;
        $.post($("#lclQuoteForm").attr("action"), params,
                function (data) {
                    $('#chargeDesc').html(data);
                    $('#chargeDesc', window.parent.document).html(data);
                    hideProgressBar();
                });
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
                var origin = getOrigin();
                var destination = getDestination();
                // var pol = $('#polCode').val();
                var pol = getPOL();
                //var pod = $('#podCode').val();
                var pod = getPOD();
                submitAjaxForm("calculateImportCharges", '#lclQuoteForm', '#chargeDesc', origin, destination, pol, pod);
                setDisableTextBox("portOfOriginR");
                setDisableTextBox("portOfLoading");
                setDisableTextBox("portOfDestination");
                setEnableTextBox("finalDestinationR");
                document.getElementById('clearRates').style.position = "relative"
                document.getElementById('clearRates').style.visibility = "visible";
            }
        }
    }
}
function getPOL() {
    var pol = "";
    var portOfLoading = document.getElementById('portOfLoading');
    if (portOfLoading != undefined && portOfLoading != null && portOfLoading.value != "") {
        pol = document.getElementById('portOfLoading').value;
    }
    if (pol.indexOf("(") > -1 && pol.indexOf(")") > -1) {
        return pol.substring(pol.indexOf("(") + 1, pol.indexOf(")"));
    }
    return "";
}
function getPOD() {
    var pod = "";
    var portOfDestination = document.getElementById('portOfDestination');
    if (portOfDestination != undefined && portOfDestination != null && portOfDestination.value != "") {
        pod = document.getElementById('portOfDestination').value;
    }
    if (pod.indexOf("(") > -1 && pod.indexOf(")") > -1) {
        return pod.substring(pod.indexOf("(") + 1, pod.indexOf(")"));
    }
    return "";
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
function submitAjaxForm(methodName, formName, selector, origin, destination, pol, pod, radioValue, doorOriginCityZip, pickupReadyDate) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod + "&radioValue=" + radioValue + "&doorOriginCityZip=" + doorOriginCityZip + "&pickupReadyDate=" + pickupReadyDate;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                $('#methodName').val("displayTransitTime");
                var params2 = $('#lclQuoteForm').serialize();
                $.post($('#lclQuoteForm').attr("action"), params2,
                        function (data) {
                            $("#trasitTime").html(data);
                            $("#trasitTime", window.parent.document).html(data);
                            if ($("#validateImpRates").val() !== "") {
                                $.prompt($("#validateImpRates").val());
                            }
                        });
                hideProgressBar();
            });
}
function showAllCitiesInCountry() {
    if ($('#showAllCities').is(":checked")) {
        $('#portOfLoading').val('');
        $('#portOfLoading').removeClass().addClass("textlabelsBoldForTextBoxWidth");
        $('#portOfLoading').removeAttr("readonly");
        $('#portOfLoading').attr('alt', 'SEARCH_BY_COUNTRY');
    } else {
        $('#portOfLoading').val('');
        $('#portOfLoading').attr('alt', 'CONCAT_WITHOUT_US_COUNTRY');
    }
}
function setFinalDestinationForImports(moduleName) {
    var shipment = $('input:radio[name=transShipMent]:checked').val();
    if (shipment.toUpperCase() === 'N') {
        $('#finalDestinationId').val($('#portOfDestinationId').val());
        $('#finalDestinationR').val($('#portOfDestination').val());
        externalGRIRemarks();
    }
}
function shipperAcctTypeCheck() {
    var acctno = $('#shipperCode').val();
    var shipperManual = $('#dupShipperName').val();
    if (shipperManual !== '' && acctno === '') {
        $('#newShipper').attr("checked", true);
        $("#dojoShipper").hide();
        $("#manualShipper").show();
    }
    else {
        $('#newShipper').attr("checked", false);
        $('#dupShipperName').val('');
        $("#manualShipper").hide();
        $("#dojoShipper").show();
    }
}
function consigneeAcctTypeCheck() {
    var acctno = $('#consigneeCode').val();
    var consManual = $('#dupConsigneeName').val();
    if (consManual !== '' && acctno === '') {
        $('#newConsignee').attr("checked", true);
        $("#dojoConsignee").hide();
        $("#manualConsignee").show();
    }
    else {
        $('#newConsignee').attr("checked", false);
        $('#dupConsigneeName').val('');
        $("#manualConsignee").hide();
        $("#dojoConsignee").show();
    }
}
function updateAms(fileId, methodName) {
    var $msg = $("#update-msg");
    var $val = $("#update-val");
    var $ams = $("#defaultAms");
    if ($ams.val() !== "") {
        $("#methodName").val(methodName);
        var params = $("#lclQuoteForm").serialize();
        params += "&fileId=" + fileId;
        $.post($("#lclQuoteForm").attr("action"), params, function (data) {
            $msg.text("Updated AMS/HBL # : ");
            $val.text($ams.val());
            $("#more-ams").removeClass("display-hide");
            setTimeout(function () {
                $msg.text("");
                $val.text("");
            }, 4000);
        });
    }
}

function triggerRates(commodityNo) {
    $("#methodName").val("triggerRates");
    $.ajaxx({
        dataType: "json",
        url: $("#lclQuoteForm").attr("action"),
        data: $("#lclQuoteForm").serialize() + "&commodityNo=" + commodityNo,
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
                        $("#retailCommodity").val(data.commodityNo);
                    } else if (trigger === "Notify") {
                        $("#notifyCommNo").val(data.commodityNo);
                    } else if (trigger === "Consignee") {
                        $("#coloadCommNo").val(data.commodityNo);
                    } else {
                        $("#agCommodityNo").val(data.commodityNo);
                    }
                    if (fileNumberId !== "") {
                        if (data.message.indexOf("ERT will change to Yes") >= 0) {
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
function consignee_AccttypeCheck() {
    var acctType = jQuery("#consignee_acct_type").val();
    if ($('#consDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#consDisableAcct').val() + "</span>");
        clearConsigneeValues();
    } else {
        setCreditStatus("consigneeCreditClient", "consigneeCredit");
        if (acctType !== "" && acctType.indexOf("C") === -1) {
            updateTpByAcctType("This account is not a Consignee, would you like to make it one?", "consigneeName", "consigneeCode", "C");
        }
    }

}
function shipper_AccttypeCheck() {
    var acctType = jQuery("#shipper_acct_type").val();
    if ($('#shipperDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#shipDisableAcct').val() + "</span>");
        clearShipperValues();
    } else {
        setCreditStatus("shipperCreditClient", "shipperCredit");
        if (acctType !== "" && acctType.indexOf("S") === -1) {
            updateTpByAcctType("This account is not a Shipper, would you like to make it one?", "shipperName", "shipperCode", "S");
        }
    }
}
function updateTpByAcctType(txt, acctName, acctNo, acctType) {
    var city = "";
    var tpFlag = "";
    if (acctType === 'C') {
        city = $("#consigneeCity").val();
        tpFlag = 'LCL_IMP_QUOTE_CONSIGNEE';
    } else if (acctType === 'S') {
        city = $("#shipperCity").val();
        tpFlag = 'LCL_IMP_QUOTE_SHIPPER';
    }
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                        methodName: "existingImpCustomerList",
                        forward: "/jsps/Tradingpartnermaintainance/tradingPartnerExistsCustomerList.jsp",
                        param1: $("#" + acctName).val(),
                        param2: city
                    },
                    preloading: true,
                    success: function (data) {
                        if (null !== data && data !== "") {
                            showPopUp();
                            createHTMLElement("div", "docListDiv", "60%", "50%", document.body);
                            $("#docListDiv").html(data);
                            $('#tpFlag').val(tpFlag);
                            $('#tpacctNo').val(acctNo);
                        } else {
                           createConsigneeAcctShipperAcct(acctName, acctNo, acctType)
                        }
                    }
                });
            } else {
                if (acctType === 'S') {
                    clearShipperValues();
                }
                else {
                    clearConsigneeValues();
                }
                $.prompt.close();
            }
        }
    });
}
function createConsigneeAcctShipperAcct(acctName, acctNo, acctType) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "createConsigneeAcctShipperAcct",
            param1: $("#" + acctNo).val(),
            param2: acctType,
            param3: $("#loginName").val(),
            dataType: "json"
        },
        async: false,
        success: function (data) {
            jQuery("#" + acctName).val(data[0]);
            jQuery("#" + acctNo).val(data[1]);
        }
    });
}

function updateBussinessUnit() {
    var fileNumberId = $("#fileNumberId").val();
    var originAcctNo = $("#supplierCode").val();
    var userId = $("#loginUserId").val();
    var oldBrand = $("#oldBrand").val();
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
                    $("#terminalLocation").val(data[0] + " / " + data[1]);
                    $("#trmnum").val(data[1]);
                }
            }
        });
    }
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
        if (tpFlag === "LCL_IMP_QUOTE_SHIPPER") {
            setValues(existingaccount, 'shipperName', 'shipperCode', 'shipperAddress', 'shipperCity', 'shipperState', 'shipperZip', 'shipperCountry', 'shipperPhone', 'shipperFax', 'shipperEmail', 'shipperSalesPersonCode', 'shipUnlocCode', '', 'newShipper');
        } else if (tpFlag === "LCL_IMP_QUOTE_CONSIGNEE") {
            setValues(existingaccount, 'consigneeName', 'consigneeCode', 'consigneeAddress', 'consigneeCity', 'consigneeState', 'consigneeZip', 'consigneeCountry', 'consigneePhone', 'consigneeFax', 'consigneeEmail', 'consigneeSalesPersonCode', 'consigneeUnLocCode', 'consigneeColoadRetailRadio', 'newConsignee');
        } else if (tpFlag === "LCL_IMP_QUOTE_NOTIFY") {
            setValues(existingaccount, 'notifyName', 'notifyCode', 'notifyAddress', 'notifyCity', 'notifyState', 'notifyZip', 'notifyCountry', 'notifyPhone', 'notifyFax', 'notifyEmail', 'notifySalesPersonCode', 'notifyUnLocCode', 'notifyColoadRetailRadio', 'newNotify');
        } else if (tpFlag === "LCL_IMP_QUOTE_NOTIFY2") {
            setValues(existingaccount, 'notify2Name', 'notify2Code', 'notify2Address', 'notify2City', 'notify2State', 'notify2Zip', 'notify2Country', 'notify2Phone', 'notify2Fax', 'notify2Email', 'notify2SalesPersonCode', 'notify2UnLocCode', 'notify2ColoadRetailRadio', 'newNotify2');
        }
        cancelAdd();
    } else {
        $.prompt("Please select Any account")
        return false;
    }
}
function setValues(existingaccount, aaccoutName, accountNo, address, city, state, zip, country, phone, fax, email, salesPersonCode, unlocCode, coloadRetail, newCheckBoxValue) {
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
}