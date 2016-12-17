var formChanged = false;
$(document).ready(function () {
    $("[title != '']").not("link").tooltip();
    detectFormChange();
    setBillToCodeDisabled();
});
function detectFormChange() {
    var form = "#lclCorrectionForm";
    var $selector = $(form + " input[type=text], " + form + " textarea");
    $($selector).each(function () {
        $(this).data('initial_value', $(this).val());
    });
    $($selector).keyup(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            formChanged = true;
        }
    });
    $($selector).change(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            formChanged = true;
        }
    });
    $(form).bind('change paste', function () {
        formChanged = true;
    });
}
function isFormChanged() {
    var form = "#lclCorrectionForm";
    var $selector = $(form + " input[type=text], " + form + " textarea");
    $($selector).each(function () {
        $(this).data('initial_value', $(this).val());
    });
    $($selector).keyup(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            formChanged = true;
        }
    });
    $($selector).change(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            formChanged = true;
        }
    });
    $(form).bind('change paste', function () {
        formChanged = true;
    });
    return formChanged;
}
function addLclBlCorrection() {
    var fileId = document.getElementById('fileId').value;
    var selectedMenu = $("#selectedMenu").val();
    var errorMessage = "BL";
    if (selectedMenu == "Imports") {
        errorMessage = "Booking";
    }
    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getLatestCorrectionStatus",
            param1: fileId
        },
        async: false,
        success: function (data) {
            if (data == "O") {
                sampleAlert("Please Post Last Correction before creating new Correction for this " + errorMessage);
                return false;
            }
            else
            {
                $("#methodName").val('addLclBlCorrections');
                $("#lclCorrectionForm").submit();
            }
        }
    });
}
function editLclBlCorrection(correctionId) {
    $("#methodName").val('editLclBlCorrections');
    document.getElementById('correctionId').value = correctionId;
    $("#lclCorrectionForm").submit();
}
function editLclBlSearchCorrection(correctionId, fileId) {
    $("#methodName").val('editLclBlCorrections');
    document.getElementById('correctionId').value = correctionId;
    document.getElementById('fileId').value = fileId;
    document.getElementById('screenName').value = "Search";
    $("#lclCorrectionForm").submit();
}
function viewLclBlCorrection(correctionId, headerId)
{
    $("#methodName").val('viewLclAddBlCorrections');
    document.getElementById('correctionId').value = correctionId;
    document.getElementById('headerId').value = headerId;
    $("#lclCorrectionForm").submit();
}
function viewLclBlSearchCorrection(correctionId, fileId)
{
    $("#methodName").val('viewLclAddBlCorrections');
    document.getElementById('correctionId').value = correctionId;
    document.getElementById('fileId').value = fileId;
    document.getElementById('screenName').value = "Search";
    $("#lclCorrectionForm").submit();
}

function viewVoidCorrection(correctionId)
{
    parent.$.prompt.close();
    parent.$("#methodName").val('viewLclAddBlCorrections');
    parent.document.getElementById('correctionId').value = correctionId;
    parent.$("#lclCorrectionForm").submit();
}

function viewLclBlVoidCorrection(path, selectedMenu)
{
    var fileId = document.getElementById('fileId').value;
    var fileNo = document.getElementById('fileNo').value;
    var href = path + "/lclCorrection.do?methodName=viewLclBlVoidCorrections&fileNo=" + fileNo + "&fileId=" + fileId + "&selectedMenu=" + selectedMenu;
    $.colorbox({
        iframe: true,
        width: "100%",
        height: "90%",
        href: href,
        title: "VOIDED Correction"
    });
}
function goBackCorrections(headerId)
{
    $("#methodName").val('viewLclBlCorrections');
    document.lclCorrectionForm.headerId.value = headerId;
    $("#lclCorrectionForm").submit();
}
function deleteLclBlCorrection(correctionId, noticeNo, blNo)
{
    var txt = 'Are you sure You want to delete?';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                $("#methodName").val('deleteLclBlCorrections');
                document.getElementById('correctionId').value = correctionId;
                document.getElementById('noticeNo').value = noticeNo;
                document.getElementById('blNo').value = blNo;
                $("#lclCorrectionForm").submit();
                hideProgressBar();
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function editCorrectionCharge(path, chargeId, chargeCode, chargeDesc, oldAmount, newAmount,
        differenceAmount, correctionId, billToParty, selectedMenu,
        correctionChargeId, lclBookingAcId) {
    if (validateCorrection()) {
        var corrCode = $("#correctionCodeId option:selected").text();
        var correctionCode = corrCode.split("-");
        var href = path + "/lclCorrection.do?methodName=editCorrectionCharge&chargeId=" + chargeId + "&chargeCode=" + chargeCode + "&chargeDescriptions=" + chargeDesc;
        href = href + "&oldAmount=" + oldAmount + "&newAmount=" + newAmount + "&differenceAmount=" + differenceAmount + "&correctionId=" + correctionId;
        href = href + "&billToParty=" + billToParty + "&selectedMenu=" + selectedMenu + "&correctionChargeId=" + correctionChargeId + "&correctionCode=" + correctionCode[0];
        href = href + "&lclBookingAcId=" + lclBookingAcId;
        openCorrectionPopup(href);
    }
}
function deleteCorrectionCharge(correctionChargeId) {
    var txt = 'Are you sure You want to delete?';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                $("#methodName").val('deleteCorrectionCharge');
                document.getElementById('correctionChargeId').value = correctionChargeId;
                $("#lclCorrectionForm").submit();
                hideProgressBar();
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function addCorrectionCharge(path, correctionId, selectedMenu) {
    if (validateCorrection()) {
        var billToParty = document.getElementById('billToParty').value;
        var href = path + "/lclCorrection.do?methodName=addCorrectionCharge&oldAmount=0.00&correctionId=" + correctionId + "&billToParty=" + billToParty;
        href += "&selectedMenu=" + selectedMenu;
        openCorrectionPopup(href);
    }
}
function saveLclCorrection() {
    if (validateCorrection() && validationCorrection()) {
        var errorMessage = getMemoEmailForMainCorrection();
        if (errorMessage !== "") {
            $.prompt(errorMessage);
        }
        else if (parent.$('#consigneeCode').val() !== $('#constAcctNo').val()
                || parent.$('#notifyCode').val() !== $('#notyAcctNo').val()
                || parent.$('#thirdpartyaccountNo').val() !== $('#thirdpartyaccountNo').val()
                || $('#formChangedVal').val() === 'true') {
            showLoading();
            $('#consigneeNo').val($('#constAcctNo').val());
            $('#notifyNo').val($('#notyAcctNo').val());
            $('#thirdPartyAcctNo').val($('#thirdpartyaccountNo').val());
            document.getElementById('customerAcctNo').value = getCustomer();
            $("#methodName").val('saveLclCorrection');
            $("#lclCorrectionForm").submit();
        } else {
            $.prompt("Please change Any Value ");
        }
    }
}
function postQuickCN(blNo, noticeNo) {

    /*if (document.lclCorrectionForm.correctionId.value == null || document.lclCorrectionForm.correctionId.value == "" ||
     document.lclCorrectionForm.correctionId.value == "0") {
     sampleAlert("Plese Enter New Amount to Post Quick CN");
     return false;
     }*/
    var formChangedVal = $('#formChangedVal').val();
    if (parent.$('#consigneeCode').val() !== $('#constAcctNo').val()
            || parent.$('#notifyCode').val() !== $('#notyAcctNo').val()
            || parent.$('#thirdpartyaccountNo').val() !== $('#thirdpartyaccountNo').val()
            || formChangedVal === 'true') {
        if (validateCorrection() && validationCorrection()) {
            var txt = 'The Quick Correction Notice will be Posted Immediately, are you sure you want to continue?';
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v == 1) {
                        showProgressBar();
                        $('#consigneeNo').val($('#constAcctNo').val());
                        $('#notifyNo').val($('#notyAcctNo').val());
                        $('#thirdPartyAcctNo').val($('#thirdpartyaccountNo').val());
                        $("#methodName").val('approveCorrections');
                        $("#buttonValue").val('Q');
                        $("#notesBlNo").val('(' + blNo + '-C-' + noticeNo + ')');
                        $("#lclCorrectionForm").submit();
                        hideProgressBar();
                    } else if (v == 2) {
                        $.prompt.close();
                    }
                }
            });
        }
    } else {
        $.prompt("Please change Any Value ");
        return;
    }
}
function searchCorrections()
{
    $("#methodName").val('viewSearchCorrection');
    $("#lclCorrectionForm").submit();
}
function searchAllCorrections()
{
    $("#methodName").val('searchCorrection');
    $("#lclCorrectionForm").submit();
}

$(document).ready(function () {
    $("#password").keypress(function (e) {
        var keycode = (e.keyCode ? e.keyCode : e.which);
        if (keycode === 13) {
            e.preventDefault();
            validatePassword();
        }
    });
});

function validatePassword() {
    document.getElementById("submitButton").disabled = true;
    var password = document.lclCorrectionForm.password.value;
    if (password === "" || password === null) {
        document.getElementById("submitButton").disabled = false;
        sampleAlert("Please enter password");
        document.lclCorrectionForm.password.style.borderColor = "red";
        $("#warning").parent.show();
    } else if ($("#userPassword").val() !== password) {
        document.getElementById("submitButton").disabled = false;
        document.lclCorrectionForm.password.value = "";
        $("#invalidPasswordLabel").removeClass("display-hide");
        $("#invalidPasswordLabel").addClass("display-show");
    } else {
        showLoading();
        parent.$("#methodName").val('approveCorrections');
        parent.$("#correctionId").val($("#correctionId").val());
        parent.$("#fileId").val($("#fileId").val());
        parent.$("#buttonValue").val('SA');
        parent.document.getElementById("concatenatedBlNo").value = document.getElementById("blNo").value + "-" + document.getElementById("noticeNo").value;
        parent.document.getElementById("notesBlNo").value = "(" + document.getElementById("blNo").value + "-C-" + document.getElementById("noticeNo").value + ")";
        parent.$("#lclCorrectionForm").submit();
    }

}

function approveUser(path, blNo, correctionId, fileId, noticeNo) {
    var href = path + "/lclCorrection.do?methodName=viewAuthenticationScreen&blNo=" + blNo + "&correctionId=" + correctionId;
    href = href + "&fileId=" + fileId + "&noticeNo=" + noticeNo;
    $.colorbox({
        iframe: true,
        width: "50%",
        height: "50%",
        href: href,
        title: "Approval"
    });
}
function reversePost(correctionId, correctionNo, blNo, index) {//remove this method
    var txt = "";
    if (document.lclCorrectionForm.lastApprovedCorrectionNo.value != document.lclCorrectionForm.lastCorrectionNo.value)
    {
        sampleAlert("You can reverse post only the latest correction Notice");
        return false;
    }
    else if (document.lclCorrectionForm.lastApprovedCorrectionNo.value != correctionNo)
    {
        var correctionDisplayTableObj = document.getElementById("correctionDisplayTable");
        if (correctionDisplayTableObj != null && correctionDisplayTableObj.rows.length > 1)
        {
            var row = correctionDisplayTableObj.rows.item(0);
            var rowslen = correctionDisplayTableObj.rows.length - 1;
            for (var i = 1; i <= rowslen; i++) {
                for (var j = 0; j < row.cells.length; j++) {
                    var col = row.cells.item(j);
                    if (col.innerHTML.toString().trim().toUpperCase() == 'CN #' && i == parseInt(index - 1)) {
                        txt = txt + "Please Reverse Posting " + correctionDisplayTableObj.rows[i].cells[j].innerHTML + " notice number before this.... ";
                        break;
                    }
                }
            }
        }
        sampleAlert(txt);
        return false;
    }
    txt = 'Do you want to Reverse this correction Notice Y/N';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                $("#methodName").val('reversePost');
                document.getElementById('correctionId').value = correctionId;
                document.getElementById('noticeNo').value = correctionNo;
                document.getElementById('blNo').value = blNo;
                $("#lclCorrectionForm").submit();
                hideProgressBar();
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function unApproveUser(correctionId, blNo, fileId, noticeNo) {
    $("#methodName").val('unApproveCorrections');
    document.getElementById('correctionId').value = correctionId;
    document.getElementById('concatenatedBlNo').value = blNo + "-" + noticeNo;
    document.getElementById('notesBlNo').value = "(" + blNo + "-C-" + noticeNo + ")";
    document.getElementById('fileId').value = fileId;
    document.getElementById('buttonValue').value = "UA";
    $("#lclCorrectionForm").submit();
}
function openCorrectionPopup(href)
{
    $.colorbox({
        iframe: true,
        width: "90%",
        height: "90%",
        href: href,
        title: "Add/Edit Correction Charge"
    });
}

function saveCorrectionCharges() {
    var trmnum = parent.parent.$("#trmnum").val();
    var chargesCode = $("#chargesCode").val() !=="" && $("#chargesCode").val() !== undefined ? $("#chargesCode").val() : $("#chargeCode").val();
    if (isWarehouseBill()) {
        if (validateCorrectionCharges()) {
             if(checkAddCorrectionChargeAndCostMappingWithGL(chargesCode , trmnum)) {
            parent.document.getElementById('chargeId').value = document.lclCorrectionForm.chargeId.value;
            parent.document.getElementById('oldAmount').value = document.getElementById('oldAmount').value;
            parent.document.getElementById('newAmount').value = document.lclCorrectionForm.newAmount.value;
            parent.document.getElementById('billToParty').value = document.lclCorrectionForm.billToParty.value;
            parent.document.getElementById('correctionChargeId').value = document.lclCorrectionForm.correctionChargeId.value;
            parent.document.getElementById('lclBookingAcId').value = document.lclCorrectionForm.lclBookingAcId.value;
            var selectedMenu = parent.$("#selectedMenu").val();
            if (selectedMenu != 'Imports') {
                parent.document.getElementById('currentProfit').value = parent.document.getElementById("lblCurrentProfit").innerHTML;
                parent.document.getElementById('profitAfterCN').value = parent.document.getElementById("lblProfitAfterCn").innerHTML;
            }
            parent.$('#consigneeNo').val(parent.$('#constAcctNo').val());
            parent.$('#notifyNo').val(parent.$('#notyAcctNo').val());
            parent.$('#thirdPartyAcctNo').val(parent.$('#thirdpartyaccountNo').val());
            var creditEmail = [];
            var debitEmail = [];
            parent.jQuery(".creditEmailId").each(function () {
                if (jQuery(this).is(":checked") === true) {
                    creditEmail.push(jQuery(this).val());
                }
            });
            parent.jQuery(".debitEmailId").each(function () {
                if (jQuery(this).is(":checked") === true) {
                    debitEmail.push(jQuery(this).val());
                }
            });
            parent.jQuery('#creditMemoEmail').val(creditEmail);
            parent.jQuery('#debitMemoEmail').val(debitEmail);
            parent.$('#billToCode').val(parent.$('#billToCode').val());
            parent.$('#currentExitBillToCode').val(parent.$('#currentExitBillToCode').val());
            parent.$('#billingType').val(parent.$('#billingType').val());
            showLoading();  
            parent.$("#methodName").val('saveLclCorrectionCharges');
            parent.$("#formChangedVal").val(true);
            parent.$("#lclCorrectionForm").submit();
          }
        }
    }
}
function getCodeType() {
    var code = '';
    var choice = document.lclCorrectionForm.correctionType.selectedIndex;
    if (document.lclCorrectionForm.correctionType[choice] != undefined) {
        var codeType = document.lclCorrectionForm.correctionType[choice].text;
        var codetypeArray = codeType.split("-");
        if (codetypeArray.length != undefined) {
            code = codetypeArray[0];
        }
    }
    return code;
}
function getCustomer() {
    var customer = "";
    var code = getCodeType();
    if (code == 'B') {
        customer = document.getElementById("agentNo").value;
    } else if ((code == 'F' || code == 'H' || code == 'D')) {
        customer = document.getElementById("forwarderNo").value;
    } else if ((code == 'G' || code == 'I' || code == 'C')) {
        customer = document.getElementById("shipperNo").value;
    } else if (code == 'E') {
        customer = document.getElementById("thirdPartyAcctNo").value;
    }
    return customer;
}
function validateCorrection() {
    if (document.lclCorrectionForm.comments.value.toString().trim() == "") {
        $.prompt("Please enter Comments");
        document.lclCorrectionForm.comments.style.borderColor = "red";
        document.lclCorrectionForm.comments.value = "";
        return false;
    }
    if (document.lclCorrectionForm.correctionType.value == "0") {
        $.prompt("Please enter Correction Type");
        document.lclCorrectionForm.correctionType.style.borderColor = "red";
        return false;
    }
    if (document.lclCorrectionForm.correctionCode.value == "0") {
        $.prompt("Please enter Correction Code");
        document.lclCorrectionForm.correctionCode.style.borderColor = "red";
        return false;
    }
    var code = getCodeType();
    if ((code == 'F' || code == 'H' || code == 'D') && (document.getElementById("forwarderNo").value == null || document.getElementById("forwarderNo").value == "")) {
        sampleAlert("Forwarder is required in BL.");
        return false;
    } else if ((code == 'G' || code == 'I' || code == 'C') && (document.getElementById("shipperNo").value == null || document.getElementById("shipperNo").value == "")) {
        sampleAlert("Shipper is required in BL.");
        return false;
    } else if (code == 'E' && (document.getElementById("thirdPartyAcctNo").value == null && document.getElementById("thirdPartyAcctNo").value == "")) {
        sampleAlert("Third Party is required in BL.");
        return false;
    }
    return true;
}
function validateCorrectionCharges() {
    var currentBillToParty = $("#billToParty").val();
    var existBillToParty = $("#existBillToParty").val();
    var currentBillToPartyAccount = "";
    var exitBillToPartyAccount = "";
    var screenName = "BL.";
    var Originally = $.trim($("#Originally", parent.document).val());
    var consigneeAcctNameAndConsigneeAcctNo = $("#constAcctName", parent.document).val() + " " + $("#constAcctNo", parent.document).val();
    var thirdPartyAcctNameAndthirdPartyAcctNo = $("#thirdPartyname", parent.document).val() + " " + $("#thirdpartyaccountNo", parent.document).val();
    var notifyAcctNameAndnotifyAcctNo = $("#notyAcctName", parent.document).val() + " " + $("#notyAcctNo", parent.document).val();
    var corrCode = $("#correctionCodeId option:selected", parent.document).text();
    var correctionCode = corrCode.split("-");
    if (parent.document.lclCorrectionForm.selectedMenu.value == "Imports") {
        screenName = "Booking.";
    }
    if (document.lclCorrectionForm.chargeId.value == "" || document.lclCorrectionForm.chargeId.value == "0") {
        sampleAlert("Please enter ChargeCode");
        document.lclCorrectionForm.chargesCode.style.borderColor = "red";
        $("#warning").parent.show();
        return false;
    }
    if (document.lclCorrectionForm.newAmount.value === "") {
        sampleAlert("Please enter New Amount");
        document.lclCorrectionForm.newAmount.style.borderColor = "red";
        $("#warning").parent.show();
        return false;
    }
    if (correctionCode[0] === "001" && Number(document.lclCorrectionForm.oldAmount.value).toFixed(2) !== 0.00) {
        var billToParty = document.lclCorrectionForm.billToParty.value;
        if (currentBillToParty !== existBillToParty) {
            currentBillToPartyAccount = currentBillToParty === 'Consignee' ? consigneeAcctNameAndConsigneeAcctNo : currentBillToParty === 'Notify' ? notifyAcctNameAndnotifyAcctNo : currentBillToParty === 'ThirdParty' ? thirdPartyAcctNameAndthirdPartyAcctNo : '';
            exitBillToPartyAccount = existBillToParty === 'Consignee' ? consigneeAcctNameAndConsigneeAcctNo : existBillToParty === 'Notify' ? notifyAcctNameAndnotifyAcctNo : existBillToParty === 'ThirdParty' ? thirdPartyAcctNameAndthirdPartyAcctNo : '';
            if (currentBillToPartyAccount === exitBillToPartyAccount && (currentBillToPartyAccount !== '' && exitBillToPartyAccount !== '')) {
                sampleAlert("New Bill to Party account cannot be same as the Old Bill to Party account. Please change the " + currentBillToParty + " account.");
                return false;
            }
        } else if (document.lclCorrectionForm.differenceAmount.value === "0.00") {
            sampleAlert("Old Amount and New Amount cannot be same.");
            return false;
        }
    } else if (document.lclCorrectionForm.differenceAmount.value === "0.00") {
        sampleAlert("Old Amount and New Amount cannot be same.");
        return false;
    }
    if (parent.document.getElementById('chargesTable') != null && $("#chargesCode").length > 0) {
        var chargestableobj = parent.document.getElementById('chargesTable');
        var row = chargestableobj.rows.item(0);
        var chargeCode = $("#chargesCode").val();
        for (var i = 1; i <= chargestableobj.rows.length - 1; i++) {
            var chargeCodeFound = false, billToPartyFound = false;
            for (var j = 0; j < row.cells.length; j++) {
                var col = row.cells.item(j);
                if (col.innerHTML.toString().trim().indexOf("Charge Code") != -1) {
                    if (chargestableobj.rows[i].cells[j].innerHTML == chargeCode) {
                        chargeCodeFound = true;
                    }
                }
                if (col.innerHTML.toString().trim().indexOf("Bill To Party") != -1) {
                    if (chargestableobj.rows[i].cells[j].innerHTML == document.lclCorrectionForm.billToParty.value) {
                        billToPartyFound = true;
                    }
                }
                if (chargeCodeFound && billToPartyFound) {
                    sampleAlert("Charge Code " + chargeCode + " with BillToParty " + document.lclCorrectionForm.billToParty.value + " Already Exists.");
                    return false;
                }
            }
        }
    }
    var billToParty = document.lclCorrectionForm.billToParty.value;
    var amount = Number(document.lclCorrectionForm.oldAmount.value).toFixed(2);
    var newAmount = Number(document.lclCorrectionForm.newAmount.value).toFixed(2);
    var difference = Math.abs(newAmount) - amount;
    if (parent.document.lclCorrectionForm.selectedMenu.value != "Imports") {
        if ((document.lclCorrectionForm.billToParty.value == "Forwarder") && (parent.document.getElementById("forwarderNo").value == null || parent.document.getElementById("forwarderNo").value == "")) {
            sampleAlert("Forwarder is required in " + screenName);
            return false;
        } else if ((document.lclCorrectionForm.billToParty.value == "Shipper") && (parent.document.getElementById("shipperNo").value == null || parent.document.getElementById("shipperNo").value == "")) {
            sampleAlert("Shipper is required in " + screenName);
            return false;
        }
        else if ((document.lclCorrectionForm.billToParty.value == "Agent") && (parent.document.getElementById("agentNo").value == null || parent.document.getElementById("agentNo").value == "")) {
            sampleAlert("Agent is required in " + screenName);
            return false;
        }
    }
    else {
        if ((billToParty === "Notify") && (parent.$("#notyAcctNo").val() === null || parent.$("#notyAcctNo").val() === "")) {
            sampleAlert("Notify Account Number is required ");
            return false;
        } else if ((billToParty === "Consignee") && (parent.$("#constAcctNo").val() === null || parent.$("#constAcctNo").val() === "")) {
            sampleAlert("Consignee Account Number is required");
            return false;
        }
    }
    if ((billToParty === "ThirdParty") && (parent.$("#thirdpartyaccountNo").val() === null || parent.$("#thirdpartyaccountNo").val() === "")) {
        sampleAlert("Third Party Account Number is required ");
        return false;
    }
    return true;
}

function displaySubGrids() {
    if (document.getElementById("correctionTypeIdA").value === document.lclCorrectionForm.correctionType.value ||
            document.getElementById("correctionTypeIdY").value == document.lclCorrectionForm.correctionType.value) {
        if (document.getElementById('chargesTable') != null)
        {
            document.getElementById('chargesTable').style.position = "relative";
            document.getElementById('chargesTable').style.visibility = "visible";
        }
        if (document.getElementById('addCharges') != null)
        {
            document.getElementById('addCharges').style.position = "relative";
            document.getElementById('addCharges').style.visibility = "visible";
        }
    }
    else {
        document.getElementById('chargesTable').style.position = "absolute";
        document.getElementById('chargesTable').style.visibility = "hidden";
        document.getElementById('addCharges').style.position = "absolute";
        document.getElementById('addCharges').style.visibility = "hidden";
    }
    addMemoEmailSection("");
    setCorrectionCode();
    setPartiesValues();
    var selectedMenu = $("#selectedMenu").val();
    if (selectedMenu != 'Imports')
    {
        setProfitValues();
    }

}

function addMemoEmailSection(differnceAmt) {
    var fileId = document.getElementById('fileId').value;
    var correctionId = document.getElementById('correctionId').value;
    var newAcctNo = getCustomer();
    var oldAcctNo = document.getElementById('customerAcctNo').value;
    var viewMode = document.getElementById('viewMode').value;
    var cnType = getCodeType();
    if (cnType != "T" && cnType != "U" && cnType != "Select Correction Type") {
        $.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getMemoEmailIds",
                forward: "/jsps/LCL/lclMemoEmailSection.jsp",
                param1: fileId,
                param2: correctionId,
                param3: cnType,
                param4: oldAcctNo,
                param5: newAcctNo,
                param6: differnceAmt,
                param7: viewMode,
                request: true
            },
            async: false,
            success: function (data) {
                $("#memoDivSection").html(data);
                if (viewMode == "view") {
                    jQuery("#memoDivSection").find(".creditEmailId, .debitEmailId").attr("disabled", true);
                    jQuery("#creditMemoEmailDiv").attr("class", "divstyleThinDisabled");
                    jQuery("#debitMemoEmailDiv").attr("class", "divstyleThinDisabled");
                }
            }
        });
    }
}
function getMemoEmailForMainCorrection() {
    var creditEmail = [];
    var debitEmail = [];
    var totalCreditEmailCount = 0;
    var totalDebitEmailCount = 0;
    var errMsg = "";
    jQuery(".creditEmailId").each(function () {
        totalCreditEmailCount++;
        if (jQuery(this).is(":checked") == true) {
            creditEmail.push(jQuery(this).val());
        }
    });
    jQuery(".debitEmailId").each(function () {
        totalDebitEmailCount++;
        if (jQuery(this).is(":checked") == true) {
            debitEmail.push(jQuery(this).val());
        }
    });
    jQuery('#creditMemoEmail').val(creditEmail);
    jQuery('#debitMemoEmail').val(debitEmail);
    if (totalCreditEmailCount > 0 && creditEmail.length <= 0) {
        errMsg += "-->Please choose atleast one credit email<br>";
    }
    if (totalDebitEmailCount > 0 && debitEmail.length <= 0) {
        errMsg += "-->Please choose atleast one debit email<br>";
    }
    return errMsg;
}

function PrintReportsOpenSeperately(correctionId, noticeNo, buttonValue) {
    var href = "", path = "";
    if (buttonValue == 'V')
    {
        path = parent.document.lclCorrectionForm.path.value;
    }
    else {
        path = document.lclCorrectionForm.path.value;
    }
    var fileNo = $("#fileNo").val();
    var fileId = $("#fileId").val();
    var selectedMenu = $("#selectedMenu").val();
    var creditDebitNotePrint = $("#blNo").val();
    if (selectedMenu == 'Imports')
    {
        creditDebitNotePrint = fileNo;
    }
    href = path + "/printConfig.do?screenName=LclCreditDebitNote&CreditDebitNotePrint=" + creditDebitNotePrint + "&fileNo=" + fileNo;
    href = href + "&noticeNo=" + noticeNo + "&fileId=" + fileId + "&correctionId=" + correctionId + "&selectedMenu=" + selectedMenu;
    mywindow = window.open(href, '', 'width=800,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}

function displayCorrectionNotesPopUp() {
    var path = document.lclCorrectionForm.path.value;
    var fileId = $("#fileId").val();
    var fileNo = $("#fileNo").val();
    var selectedMenu = $("#selectedMenu").val();
    var href = path + "/lclRemarks.do?methodName=display&fileNumberId=" + fileId + "&fileNumber=" + fileNo + "&moduleName=" + selectedMenu;
    href = href + "&actions=11";
    $.colorbox({
        iframe: true,
        width: "90%",
        height: "70%",
        href: href,
        title: "Notes"
    });
}
function setEnableCode() {
    setCorrectionCode();
}

function thirdPartyAcct() {
    if ($('#thirdPartyDisabled').val() === 'Y') {
        $.prompt("This Account is Disabled");
        $('#thirdPartyname').val('');
        $('#thirdPartyDisabled').val('');
        $('#thirdpartyaccountNo').val('');
    }
}
function changeBillToParty() {
    var billToParty = document.lclCorrectionForm.billToParty.value;
    if (billToParty === 'Notify' && (parent.$("#notyAcctNo").val() === null || parent.$("#notyAcctNo").val() === "")) {
        $.prompt("Notify Account Number is required");
    } else if (billToParty === "Consignee" && (parent.$("#constAcctNo").val() === null || parent.$("#constAcctNo").val() === "")) {
        $.prompt("Consignee Account Number is required");
    } else if (billToParty === "ThirdParty" && (parent.$("#thirdpartyaccountNo").val() === null || parent.$("#thirdpartyaccountNo").val() === "")) {
        $.prompt("Third Party Account Number is required");
    } else if (billToParty == 'Warehouse') {
        var cfsWarehouseNo = parent.parent.$('#cfsWarehouseNo').val();
        if (cfsWarehouseNo == null || cfsWarehouseNo == "") {
            $.prompt("Cfs Devanning Warehouse is required");
        } else {
            isWarehouseBill();
        }
    }

}
function isWarehouseBill() {
    var flag = false;
    var billToParty = document.lclCorrectionForm.billToParty.value;
    if (billToParty === "Warehouse") {
        var cfsWarehouseNo = parent.parent.$('#cfsWarehouseNo').val();
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
                if (data[1] != null && data[1] != "") {
                    parent.$('#cfsDevAcctNo').val(data[1]);
                    parent.$('#cfsDevAcctName').val(data[0]);
                    flag = true;
                } else {
                    $('#billToParty').val($('#existBillToParty').val());
                    $.prompt("Cfs Devanning Warehouse does not have IPI vendor");
                }
            }
        });
    } else {
        flag = true;
    }
    return flag;
}
function setCorrectionCode() {
    var billToParty = document.getElementById('billToParty').value;
    setBillToCodeDisabled();
    if (document.lclCorrectionForm.correctionType.value === '12010') {
        if (billToParty !== "T") {
            sampleAlert("Bill To party is not Third Party so Please select other option");
            $('#correctionTypeId').val('Select');
        } else {
            $('#correctionCodeId').val('12017');
            setEnableTextBox('thirdPartyname');
        }
    } else {
        var corrCode = $("#correctionCodeId option:selected").text();
        var correctionCode = corrCode.split("-");
        var viewMode = document.getElementById('viewMode').value;
        if (correctionCode[0] === '001' && viewMode !== "view") {
            setEnableTextBox('constAcctName');
            setEnableTextBox('notyAcctName');
            setEnableTextBox('thirdPartyname');
            setBillToCodeEnabled();
        }
    }
}
function setPartiesValues() {
    if ($('#correctionId').val() === '' || $('#correctionId').val() === null) {
        $('#constAcctName').val(parent.$('#consigneeName').val());
        $('#constAcctNo').val(parent.$('#consigneeCode').val());
        $('#notyAcctName').val(parent.$('#notifyName').val());
        $('#notyAcctNo').val(parent.$('#notifyCode').val());
        $('#thirdPartyname').val(parent.$('#thirdPartyname').val());
        $('#thirdpartyaccountNo').val(parent.$('#thirdpartyaccountNo').val());
    }
}

function setProfitValues()
{
    var viewMode = document.getElementById('viewMode').value;
    if (viewMode !== "view") {
        var chargetableobj = document.getElementById("chargesTable");
        var costAmount = Number(document.getElementById("costAmount").value);
        var row = chargetableobj.rows.item(0);
        var currentProfit = 0.00;
        var profitAfterCn = 0.00;
        for (var i = 1; i <= chargetableobj.rows.length - 1; i++) {
            for (var j = 0; j < row.cells.length; j++) {
                var col = row.cells.item(j);
                if (col.innerHTML.toString().trim().toUpperCase().indexOf("AMOUNT") != -1 &&
                        col.innerHTML.toString().trim().toUpperCase().indexOf("NEW AMOUNT") == -1)
                {
                    currentProfit += Number(chargetableobj.rows[i].cells[j].innerHTML.trim());
                    profitAfterCn += Number(chargetableobj.rows[i].cells[j].innerHTML.trim());
                }
                else if (col.innerHTML.toString().trim().toUpperCase().indexOf("DIFFERENCE") != -1 &&
                        chargetableobj.rows[i].cells[j].innerHTML.trim() != "")
                {
                    profitAfterCn += Number(chargetableobj.rows[i].cells[j].innerHTML.trim());
                }

            }
        }
        currentProfit -= costAmount;
        profitAfterCn -= costAmount;
        document.getElementById("lblCurrentProfit").innerHTML = currentProfit.toFixed(2);
        document.getElementById("lblProfitAfterCn").innerHTML = profitAfterCn.toFixed(2);
    }
}
function setEnableTextBox(id) {
    $('#' + id).removeClass('text-readonly');
    $('#' + id).removeAttr('readonly');
    $('#' + id).addClass("textlabelsBoldForTextBox");
}
function setDisableTextBox(id) {
    $('#' + id).removeClass("textlabelsBoldForTextBox");
    $('#' + id).addClass('text-readonly');
    $('#' + id).attr('readonly', true);
}
function setPartiesEnable() {
    if ($('#constAcctNo').val() !== parent.$('#consigneeCode').val()) {
        setDisableTextBox('notyAcctName');
        setDisableTextBox('thirdPartyname');
    } else if ($('#notyAcctNo').val() !== parent.$('#notifyCode').val()) {
        setDisableTextBox('constAcctName');
        setDisableTextBox('thirdPartyname');
    } else {
        setDisableTextBox('notyAcctName');
        setDisableTextBox('constAcctName');
    }
}
function closeCorrection(acctName, acctNo) {
    parent.$.colorbox.close();
    if (acctNo != "" && acctName != "") {
        parent.$('#thirdpartyaccountNo').val(acctNo);
        parent.$('#thirdPartyname').val(acctName);
        parent.$("#methodName").val('editBooking');
        parent.$("#lclBookingForm").submit();
    }
}

function validationCorrection() {
    var amount = Number(document.lclCorrectionForm.oldAmount.value).toFixed(2);
    var newAmount = Number(document.lclCorrectionForm.newAmount.value).toFixed(2);
    var differenceAmount = Math.abs(newAmount) - amount;
    var billToParty = $("#billToParty").val();
    var billToPartyChanged = $("#billToPartyChanged").val();
    var corrCode = $("#correctionCodeId option:selected").text();
    var correctionCode = corrCode.split("-");
    var Originally = $("#Originally").val().trim();
    var consigneeAcctNameAndConsigneeAcctNo = $("#constAcctName").val() + " " + $("#constAcctNo").val();
    var thirdPartyAcctNameAndthirdPartyAcctNo = $("#thirdPartyname").val() + " " + $("#thirdpartyaccountNo").val();
    var notifyAcctNameAndnotifyAcctNo = $("#notyAcctName").val() + " " + $("#notyAcctNo").val();
    var consigneeChargeFlag = false;
    var notifyChargeFlag = false;
    var thirdPartyChargeFlag = false;
    var rowCount = $("#chargesTable tbody tr").length;
    for (var i = 1; i <= rowCount; i++) {
        var value = $("#chargesTable").find('tr:nth-child(' + i + ') td:nth-child(3)').html();
        if (trim(value).indexOf("Consignee") !== -1) {
            consigneeChargeFlag = true;
        }
        if (trim(value).indexOf("Notify") !== -1) {
            notifyChargeFlag = true;
        }
        if (trim(value).indexOf("ThirdParty") !== -1) {
            thirdPartyChargeFlag = true;
        }
    }
    if (consigneeChargeFlag && $("#constAcctNo").val() === '') {
        $.prompt("Consignee AcctNo is Required.");
        $("#constAcctName").css("border-color", "red");
        return false;
    }
    if (notifyChargeFlag && $("#notyAcctNo").val() === '') {
        $.prompt("Notify Party AcctNo is Required.");
        $("#notyAcctName").css("border-color", "red");
        return false;
    }
    if (thirdPartyChargeFlag && $("#thirdpartyaccountNo").val() === '') {
        $.prompt("Third Party AcctNo is Required.");
        $("#thirdPartyname").css("border-color", "red");
        return false;
    }
    if (correctionCode[0] === '001') {
        setEnableTextBox('constAcctName');
        setEnableTextBox('notyAcctName');
        setEnableTextBox('thirdPartyname');
    }
    //    if (consigneeAcctNameAndConsigneeAcctNo === Originally && correctionCode[0] === '001' && differenceAmount !== 0 && billToParty === billToPartyChanged) {
    //        $.prompt("Consignee can not be same as original bill to party");
    //        $("#constAcctName").val('');
    //        $("#constAcctNo").val('');
    //        document.getElementById('constAcctName').style.borderColor = '#FF0000';
    //        document.getElementById('constAcctName').style.border = '#solid';
    //        setEnableTextBox('constAcctName');
    //        setEnableTextBox('notyAcctName');
    //        setEnableTextBox('thirdPartyname');
    //        return false;
    //    } else if (thirdPartyAcctNameAndthirdPartyAcctNo === Originally && correctionCode[0] === '001' && differenceAmount !== 0 && billToParty === billToPartyChanged) {
    //        $.prompt("Third Party can not be same as original bill to party");
    //        $("#thirdPartyname").val('');
    //        $("#thirdpartyaccountNo").val('');
    //        document.getElementById('thirdPartyname').style.borderColor = '#FF0000';
    //        document.getElementById('thirdPartyname').style.border = '#solid';
    //        setEnableTextBox('constAcctName');
    //        setEnableTextBox('notyAcctName');
    //        setEnableTextBox('thirdPartyname');
    //        return false;
    //    } else if (notifyAcctNameAndnotifyAcctNo === Originally && correctionCode[0] === '001' && differenceAmount !== 0 && billToParty === billToPartyChanged) {
    //        $.prompt("Notify Party can not be same as original bill to party");
    //        $("#notyAcctName").val('');
    //        $("#notyAcctNo").val('');
    //        document.getElementById('notyAcctName').style.borderColor = '#FF0000';
    //        document.getElementById('notyAcctName').style.border = '#solid';
    //        setEnableTextBox('constAcctName');
    //        setEnableTextBox('notyAcctName');
    //        setEnableTextBox('thirdPartyname');
    //        return false;
    //    }
    return true;
}
function updateBillToCode() {
    var billToParty = "";
    var billingType = "";
    var exisBillToCode = $('#exitBillToCode').val();
    var fileId = document.getElementById('fileId').value;
    var txt = "Are you sure you want to change the Terms?";
    var flag; 
    var consigneeAcctNo = $("#constAcctNo").val();
    var notifyAcctNo = $("#notyAcctNo").val();
    var thirdPartyAcctNo = $("#thirdpartyaccountNo").val();
    if ($('#billTypeC').attr('checked')) {
        billingType = 'C';
        if ($('#billToCodeC').attr('checked')) {
            billToParty = 'C';
            flag = validateBillToAcct(billToParty, exisBillToCode);
        } else if ($('#billToCodeN').attr('checked')) {
            billToParty = 'N';
            flag = validateBillToAcct(billToParty, exisBillToCode);
        } else if ($('#billToCodeT').attr('checked')) {
            billToParty = 'T';
            flag = validateBillToAcct(billToParty, exisBillToCode);
        } else if($('#billToCodeW').attr('checked')) {
            billToParty = 'W';
            flag = validateWarehouseBillToCode(billToParty,exisBillToCode);
        } else {
            billToParty = 'C';
            flag = validateBillToAcct(billToParty, exisBillToCode);
            $('#billToCodeC').attr("checked",true); 
            setBillToCodeEnabled();
        }
        setBillToCodeEnabled();
    } else if ($('#billTypeP').attr('checked')) {
        billToParty = 'A';
        billingType = 'P';
        flag = validateBillToAcct(billToParty, exisBillToCode);
        $("#exitBillingType").val('Prepaid');
        $('#billToCodeA').attr("checked",true);
        setBillToCodeEnabled();
    }
    if (exisBillToCode !== billToParty && flag) {
        var cfsDevAcctNo =$("#cfsDevAcctNo").val();
        var currentBillToAcctNo = billToParty === 'C' ? consigneeAcctNo : billToParty === 'N'  ? notifyAcctNo : billToParty === 'T' ? thirdPartyAcctNo : billToParty === 'W' ? cfsDevAcctNo :'';
        var exitBillToAcctNo = exisBillToCode === 'C' ? consigneeAcctNo : exisBillToCode === 'N'  ? notifyAcctNo : exisBillToCode === 'T' ? thirdPartyAcctNo : exisBillToCode === 'W' ? cfsDevAcctNo : '';
        if((currentBillToAcctNo !=='' && exitBillToAcctNo !=='') && currentBillToAcctNo === exitBillToAcctNo) {
        var exitBillTo=exisBillToCode === 'C' ? 'billToCodeC' : exisBillToCode === 'N'  ? 'billToCodeN' : exisBillToCode === 'T' ? 'billToCodeT' : exisBillToCode === 'A' ? 'billToCodeA' : exisBillToCode ==='W' ? 'billToCodeW' : '';    
        sampleAlert("New Bill to Party account cannot be same as the Old Bill to Party account. Please change the <span style = color:red>" + currentBillToAcctNo + "</span> account.");
        $('#' + exitBillTo).attr("checked", true);
            return false;
        }
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                     showProgressBar();
                    $("#fileId").val(fileId);
                    $("#billToCode").val(billToParty);
                    $("#billingType").val(billingType);
                    $("#formChangedVal").val(true);
                    $("#methodName").val('updateBillToCode');
                    var params = $("#lclCorrectionForm").serialize();
                    $.post($('#lclCorrectionForm').attr("action"), params,
                            function (data) {
                                $('#chargesTable').html(data);
                                $('#exitBillToCode').val(billToParty);
                                $('#currentExitBillToCode').val(billToParty);
                                 hideProgressBar();
                            });
                } else if (v === 2) {
                    setExitBillToCode(exisBillToCode);
                    setBillToCodeEnabled();
                    $.prompt.close();
                }
            }
        });
    }
}

function validateBillToAcct(billToCode, exisBillToCode) {
    var consigneeAcctName = $("#constAcctName").val();
    var notifyAcctName = $("#notyAcctName").val();
    var thirdPartyAcctName = $("#thirdPartyname").val();
    var txt = "";
    var flag = true;
    if (consigneeAcctName === "" && billToCode === "C") {
        txt = "Please Select Consignee Acct.";
        flag = okButton(txt, exisBillToCode);
    } else if (notifyAcctName === "" && billToCode === "N") {
        txt = "Please Select Notify Party Acct.";
        flag = okButton(txt, exisBillToCode);
    } else if (thirdPartyAcctName === "" && billToCode === "T") {
        txt = "Please Select Third Party Acct.";
        flag = okButton(txt, exisBillToCode);
    }
    return flag;
}

function okButton(txt, exisBillToCode) {
    $.prompt(txt, {
        buttons: {
            ok: 1
        },
        async: false,
        submit: function (v) {
            if (v === 1)
                $.prompt.close();
            if (exisBillToCode === "C") {
                $('#billToCodeC').attr("checked", true);
            } else if (exisBillToCode === "N") {
                $('#billToCodeN').attr("checked", true);
            } else if (exisBillToCode === "T") {
                $('#billToCodeT').attr("checked", true);
            } else if (exisBillToCode === "W"){
                $('#billToCodeW').attr("checked", true);
            } else if (exisBillToCode === "A") {
                $('#billToCodeA').attr("checked", true);
            }
        }
    });
    return false;
}
function setBillToCodeDisabled() {
    $('#billToCodeC').attr("disabled", true);
    $('#billToCodeN').attr("disabled", true);
    $('#billToCodeT').attr("disabled", true);
    $('#billToCodeW').attr("disabled", true);
    $('#billToCodeA').attr("disabled", true);
    $('#billTypeP').attr("disabled", true);
    $('#billTypeC').attr("disabled", true);
}

function setBillToCodeEnabled() {
    if ($('#billTypeC').attr('checked')) {
        $('#billToCodeC').attr("disabled", false);
        $('#billToCodeN').attr("disabled", false);
        $('#billToCodeT').attr("disabled", false);
        $('#billToCodeW').attr("disabled", false);
        $('#billToCodeA').attr("disabled", true);
    } else {
        $('#billToCodeA').attr("disabled", false);
        $('#billToCodeC').attr("disabled", true);
        $('#billToCodeN').attr("disabled", true);
        $('#billToCodeT').attr("disabled", true);
        $('#billToCodeW').attr("disabled", true);
    }
    $('#billTypeP').attr("disabled", false);
    $('#billTypeC').attr("disabled", false);
}
function setExitBillToCode(exisBillToCode) {
    switch (exisBillToCode) {
        case "C":
            $('#billToCodeC').attr("checked", true);
            $('#billTypeC').attr("checked", true);
            break;
        case "N":
            $('#billToCodeN').attr("checked", true);
            break;
        case "T":
            $('#billToCodeT').attr("checked", true);
            break;
        case "W":
            $('#billToCodeW').attr("checked", true);
            break;
        case "A":
            $('#billToCodeA').attr("checked", true);
            $('#billTypeP').attr("checked", true);
            break;
    }  
}

function validateWarehouseBillToCode(billToCode, exisBillToCode) {
    var flag = false;
    var txt ="Cfs Devanning Warehouse does not have IPI vendor";
    if (billToCode === "W") {
        var cfsWarehouseNo = parent.$('#cfsWarehouseNo').val();
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
                if (data[1] !== null && data[1] !== "") {
                    $('#cfsDevAcctNo').val(data[1]);
                    $('#cfsDevAcctName').val(data[0]);
                    flag = true;
                } else {
                    okButton(txt,exisBillToCode);
                }
            }
        });
    } else {
        flag = true;
    }
    return flag;
}

function checkAddCorrectionChargeAndCostMappingWithGL(chargesCode, terminalNo) {
    var flag = true;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkChargeAndCostMappingWithGL",
            param1: chargesCode,
            param2: terminalNo,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data !== "") {
                $.prompt("No gl account is mapped with these charge code.Please contact accounting -> <span style=color:red>" + data + "</span>.");
                flag = false;
            }
        }
    });
    return flag;
} 