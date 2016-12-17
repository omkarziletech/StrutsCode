jQuery(document).ready(function() {
    checkVesselName();
    enableDoorDelivery();
});
var disableMessage = "This Customer is Disabled";
var newwindow = "";
var myArray = new Array();
var myArray1 = new Array();
var result = false;
var newwindow3 = "";
var newwindow1 = "";
var autoCreditStatus = "";
function getComCodeForCharges(ev, index) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getChargeCode",
                param1: ev
            },
            success: function(data) {
                var a = "charge" + index;
                document.getElementById(a).value = data;
            }
        });
    }
}
function refreshPage() {
    GB_hide();
    makePageEditableWhileSaving(document.getElementById("fclbl"));
    document.fclBillLaddingform.buttonValue.value = "update";
    document.fclBillLaddingform.submit();
}
function hideMasterShipper() {
    if (document.getElementById("houseName").value == "") {
        document.getElementById("ediShipperCheck").checked = false;
        document.getElementById("ediShipperCheck").style.visibility = 'hidden';
        document.getElementById("houseShipper").value = "";
        document.getElementById("houseShipper1").value = "";
    } else {
        document.getElementById("ediShipperCheck").style.visibility = 'visible';
    }
}
function hideAgentNameCheck() {
    if (document.getElementById("forwardingAgentName").value == "") {
        document.getElementById("editAgentNameCheck").checked = false;
        document.getElementById("editAgentNameCheck").style.visibility = 'hidden';
        document.getElementById("forwardingAgent1").value = "";
        document.getElementById("forwardingAgentno").value = "";
    } else {
        document.getElementById("editAgentNameCheck").style.visibility = 'visible';
    }
}
function hideAgentNameCheckblur() {
    if (document.getElementById("editAgentNameCheck").checked && document.getElementById("forwardingAgentName").value == "") {
        document.getElementById("forwardingAgentName").value = "";
        document.getElementById("forwardingAgent1").value = "";
        document.getElementById("forwardingAgentno").value = "";
        Event.observe("consigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("forwardingAgentName_check").value) {
                element.value = '';
                $("forwardingAgentName_check").value = '';
                $("forwardingAgent1").value = '';
                $("forwardingAgentName").value = '';
                $("forwardingAgentno").value = '';
            }
            hideAgentNameCheck();
        }
        );
        hideAgentNameCheck();
    }

}
function editAgentName(obj) {
    if (obj.checked) {
        Event.stopObserving("forwardingAgentName", "blur");
    } else {
        document.getElementById("forwardingAgentName").value = "";
        document.getElementById("forwardingAgent1").value = "";
        document.getElementById("forwardingAgentno").value = "";
        Event.observe("forwardingAgentName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("forwardingAgentName_check").value) {
                element.value = '';
                $("forwardingAgentName").value = '';
                $("forwardingAgentName_check").value = '';
                $("forwardagentNo").value = '';
                $("forwardingAgentno").value = '';
            }
            hideAgentNameCheck();
        }
        );
        hideAgentNameCheck();
    }
    hideAgentNameCheck();
}
function hideMasterShipperblur() {
    if (document.getElementById('ediShipperCheck').checked && document.getElementById("houseName").value == "") {
        document.getElementById("houseName").value = "";
        Event.observe("houseName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseName_check").value) {
                element.value = '';
                $("houseName_check").value = '';
                $("houseName").value = '';
                $("houseShipper").value = '';
                $("houseShipper1").value = '';
            }
            hideMasterShipper();
        }
        );
        hideMasterShipper();
    }
}
function hideMasterConsignee() {
    if (document.getElementById("houseConsigneeName").value == "") {
        document.getElementById("ediConsigneeCheck").checked = false;
        document.getElementById("ediConsigneeCheck").style.visibility = 'hidden';
        document.getElementById("houseConsignee").value = "";
        document.getElementById("houseConsignee1").value = "";
    }
    else {
        document.getElementById("ediConsigneeCheck").style.visibility = 'visible';
    }
}
function hideMasterConsigneeblur() {
    if (document.getElementById("ediConsigneeCheck").checked && document.getElementById("houseConsigneeName").value == "") {
        document.getElementById("houseConsigneeName").value = "";
        Event.observe("houseConsigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseConsigneeName_check").value) {
                element.value = '';
                $("houseConsigneeName_check").value = '';
                $("houseConsigneeName").value = '';
                $("houseConsignee").value = '';
                $("houseConsignee1").value = '';
            }
            hideMasterConsignee();
        }
        );
        hideMasterConsignee();
    }
}
function hideMasterNotify() {
    if (document.getElementById("houseNotifyPartyName").value == "") {
        document.getElementById("ediNotifyPartyCheck").checked = false;
        document.getElementById("ediNotifyPartyCheck").style.visibility = 'hidden';
        document.getElementById("houseNotifyParty").value = "";
        document.getElementById("houseNotifyPartyaddress").value = "";
    } else {
        document.getElementById("ediNotifyPartyCheck").style.visibility = 'visible';
    }
}
function hideMasterNotifyblur() {
    if (document.getElementById("ediNotifyPartyCheck").checked && document.getElementById("houseNotifyPartyName").value == "") {
        document.getElementById("houseNotifyPartyName").value = "";
        Event.observe("houseNotifyPartyName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseNotifyPartyName_check").value) {
                element.value = '';
                $("houseNotifyPartyName_check").value = '';
                $("houseNotifyPartyName").value = '';
                $("houseNotifyParty").value = '';
                $("houseNotifyPartyaddress").value = '';
            }
            hideMasterNotify();
        }
        );
        hideMasterNotify();
    }
}
function hideHouseShipper() {
    if (document.getElementById("accountName").value == "") {
        document.getElementById("editHouseShipperCheck").checked = false;
        document.getElementById("editHouseShipperCheck").style.visibility = 'hidden';
        document.getElementById("shipper").value = "";
        document.getElementById("streamShip").value = "";
    } else {
        document.getElementById("editHouseShipperCheck").style.visibility = 'visible';
    }
}
function hideHouseShipperblur() {
    if (document.getElementById("editHouseShipperCheck").checked && document.getElementById("accountName").value == "") {
        document.getElementById("accountName").value = "";
        document.getElementById("shipper").value = "";
        document.getElementById("streamShip").value = "";
        Event.observe("accountName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("accountName_check").value) {
                element.value = '';
                $("accountName_check").value = '';
                $("shipper").value = '';
                $("accountName").value = '';
                $("streamShip").value = '';
            }
            hideHouseShipper();
        }
        );
        hideHouseShipper();
    }
}
function hideHouseConsignee() {
    if (document.getElementById("consigneeName").value == "") {
        document.getElementById("editHouseConsigneeCheck").checked = false;
        document.getElementById("editHouseConsigneeCheck").style.visibility = 'hidden';
        document.getElementById("consignee").value = "";
        document.getElementById("streamShipConsignee").value = "";
    } else {
        document.getElementById("editHouseConsigneeCheck").style.visibility = 'visible';
    }
}
function hideHouseConsigneeblur() {
    if (document.getElementById("editHouseConsigneeCheck").checked && document.getElementById("consigneeName").value == "") {
        document.getElementById("consigneeName").value = "";
        document.getElementById("consignee").value = "";
        document.getElementById("streamShipConsignee").value = "";
        Event.observe("consigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("consigneeName_check").value) {
                element.value = '';
                $("consigneeName_check").value = '';
                $("consignee").value = '';
                $("consigneeName").value = '';
                $("streamShipConsignee").value = '';
            }
            hideHouseConsignee();
        }
        );
        hideHouseConsignee();
    }
}
function hideHouseNotify() {
    if (document.getElementById("notifyPartyName").value == "") {
        document.getElementById("editHouseNotifyCheck").checked = false;
        document.getElementById("editHouseNotifyCheck").style.visibility = 'hidden';
        document.getElementById("notifyParty").value = "";
        document.getElementById("streamshipNotifyParty").value = "";
    } else {
        document.getElementById("editHouseNotifyCheck").style.visibility = 'visible';
    }
}
function hideHouseNotifyblur() {
    if (document.getElementById("editHouseNotifyCheck").checked && document.getElementById("notifyPartyName").value == "") {
        document.getElementById("notifyPartyName").value = "";
        document.getElementById("notifyParty").value = "";
        document.getElementById("streamshipNotifyParty").value = "";
        Event.observe("consigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("notifyPartyName_check").value) {
                element.value = '';
                $("notifyPartyName_check").value = '';
                $("notifyParty").value = '';
                $("notifyPartyName").value = '';
                $("streamshipNotifyParty").value = '';
            }
            hideHouseNotify();
        }
        );
        hideHouseNotify();
    }

}

function loadData(confirmBoard, importRelease) {
    var element;
    var trailer;
    var forwarderName = document.fclBillLaddingform.forwardingAgentName.value.trim();
    if ((forwarderName == 'NO FF ASSIGNED' || forwarderName == 'NO FF ASSIGNED / B/L PROVIDED'
        || forwarderName == 'NO FRT. FORWARDER ASSIGNED') && document.getElementById("contactNameButtonForFF") != null) {
        document.getElementById("contactNameButtonForFF").style.display = "none";
    }
    //--to hide ConfirmOnBoard heading at top of page if its value is 'N'------
    if (confirmBoard != "Y") {
        document.getElementById("confirmBoardid").style.display = "none";
        //document.getElementById("cal4").style.visibility = "hidden";
        //document.getElementById("confOnBoardComments").disabled = true;
        document.getElementById("verifiedEtaCheck").disabled = true;
    } else {
        var imgs = document.getElementsByTagName("img");
        //imgs[k].id == "chargesexpand" || imgs[k].id == "chargesDeleteExpand" ||
        //imgs[k].id == "chargescollapse" || imgs[k].id == "chargesDeleteCollapse"
        for (var k = 0; k < imgs.length; k++) {
            if (imgs[k].name == "containerEnableIcon" ||
                imgs[k].name == "containerDisableIcon") {
                imgs[k].style.visibility = "hidden";
            }
        }
    }
    for (var i = 0; i < document.fclBillLaddingform.elements.length; i++) {
        element = document.fclBillLaddingform.elements[i];
        if (element.name == "trailerNo") {
            trailer = element.value;
        }
        if (element.type == "button") {
            if (trailer != "" && trailer != "undefined") {
        //element.style.visibility = "visible";
        }
        }
    }
    insuranceAllowedForPort();
}
function loadOldArray() {
    for (var i = 0; i < document.fclBillLaddingform.elements.length; i++) {
        if (document.fclBillLaddingform.elements[i].type == "radio" || document.fclBillLaddingform.elements[i].type == "text" || document.fclBillLaddingform.elements[i].type == "checkbox" || document.fclBillLaddingform.elements[i].type == "select-one" || document.fclBillLaddingform.elements[i].type == "textarea") {
            if (document.fclBillLaddingform.elements[i].type == "radio" || document.fclBillLaddingform.elements[i].type == "checkbox") {
                if (document.fclBillLaddingform.elements[i].name != "autoDeductFFCom") {
                    myArray[i] = document.fclBillLaddingform.elements[i].checked;
                }
            } else {
                myArray[i] = document.fclBillLaddingform.elements[i].value;
            }
        }
    }
}
function compareWithOldArray(ev) {
    if (document.fclBillLaddingform.readyToPost.checked && document.getElementById("manifestButtonDown") != null) {
        alertNew("Ready To Post Check box is checked Please manifest the BL or Uncheck the Ready To Post check box and click on GO Back");
        return;
    }
    var index = ev.indexOf("==");
    if (index == -1) {
        for (var i = 0; i < document.fclBillLaddingform.elements.length; i++) {
            if (document.fclBillLaddingform.elements[i].type == "radio" || document.fclBillLaddingform.elements[i].type == "text" || document.fclBillLaddingform.elements[i].type == "checkbox" || document.fclBillLaddingform.elements[i].type == "select-one" || document.fclBillLaddingform.elements[i].type == "textarea") {
                if (document.fclBillLaddingform.elements[i].type == "radio" || document.fclBillLaddingform.elements[i].type == "checkbox") {
                    if (document.fclBillLaddingform.elements[i].name != "autoDeductFFCom") {
                        myArray1[i] = document.fclBillLaddingform.elements[i].checked;
                    }
                } else {
                    myArray1[i] = document.fclBillLaddingform.elements[i].value;
                }
            }
        }
        for (var j = 0; j < myArray1.length; j++) {
            if (myArray[j] != myArray1[j]) {
                result = true;
            }
        }
        if (result == true) {
            cancel();
        } else {
            goBackCall();
        }
    } else {
        var blNo = ev.substring(0, index);
        jQuery.ajaxx({
            data: {
                className: "com.gp.cvst.logisoft.hibernate.dao.FclBlDAO",
                methodName: "findBol",
                param1: blNo
            },
            success: function(data) {
                goToPreviousBL(data);
            }
        });
        latestBl(ev1);
    }
    function goToPreviousBL(data) {
        latestBl(data);
    }
}
function setAutoDeduct(ev) {
    if (ev) {
        if (ev == "Y") {
            document.getElementById("autoYes").checked = "true";
        } else {
            if (ev == "N") {
                document.getElementById("autoNo").checked = "false";
            }
        }
        document.getElementById("autoYes").disabled = "true";
        document.getElementById("autoNo").disabled = "true";
    }
}
function enableThirdParty() {
    if (document.fclBillLaddingform.billToCode[2].checked) {
        document.getElementById("billThirdPartyName").disabled = false;
        document.getElementById("billThirdPartyName").style.border = "1px solid #C4C5C4";
        document.getElementById("billThirdPartyName").style.backgroundColor = "#FCFCFC";
        if (document.getElementById("contactNameButtonForT")) {
            document.getElementById("contactNameButtonForT").style.visibility = "visible";
        }
        document.getElementById("billTrePty").disabled = false;
        document.getElementById("billTrePty").style.border = "1px solid #C4C5C4";
        document.getElementById("billTrePty").style.backgroundColor = "#FCFCFC";
        document.getElementById("toggle").style.visibility = "visible";
        if (document.fclBillLaddingform.billThirdPartyName.value == "") {
            alertNew("Please select Third Party Name and Number");
            setBillToCodeForPreviousValue();
            document.fclBillLaddingform.billToCode[2].checked = false;
            return;
        }
        confirmYesOrNo("Please note that all Bill to party will be changed for all Charges - Yes to continue and Cancel to abort operation", "thirdparty");
    }
}
String.prototype.trim = function() {
    return this.replace(/^\s+|\s+$/, "");
};
function getChargeCodeDescription(ev, index) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getChargeCodeDesc",
                param1: ev
            },
            success: function(data) {
                var a = "chargeCode" + index;
                document.getElementById(a).value = data;
            }
        });
    }
}
function getCostCode(ev, index) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getChargeCode",
                param1: ev
            },
            success: function(data) {
                var a = "costCodeDesc" + index;
                document.getElementById(a).value = data;
            }
        });
    }
}
function getCostCodeDesc(ev, index) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getChargeCodeDesc",
                param1: ev
            },
            success: function(data) {
                var a = "costCode" + index;
                document.getElementById(a).value = data;
            }
        });
    }
}
// calling while loading....
function yesFunction() {
    var errorMessage = '';
    if (document.fclBillLaddingform.finalDestination.value == '') {
        errorMessage += "-->Please Enter Destination<br>";
    }
    if (document.fclBillLaddingform.terminalName.value == '') {
        errorMessage += "-->Please Enter Origin Terminal<br>";
    }
    if (document.fclBillLaddingform.billingTerminal.value == '') {
        errorMessage += "-->Please Enter Issuing Terminal<br>";
    }
    if (errorMessage != '') {
        alertNew(errorMessage);
        return false;
    }
    for (var i = 0; i < document.fclBillLaddingform.fileType.length; i++) {
        if (document.fclBillLaddingform.fileType[i].value == "C" && document.fclBillLaddingform.fileType[i].checked && document.fclBillLaddingform.voyage.value == "") {
            alertNew("Please enter Voyage...");
            return;
        }
    }


    makePageEditableWhileSaving(document.getElementById("fclbl"));
    document.getElementById("goBack1").disabled = true;
    document.getElementById("goBack2").disabled = true;
    document.fclBillLaddingform.buttonValue.value = "previousUpdate";
    document.fclBillLaddingform.submit();
}
function noFunction() {
    document.fclBillLaddingform.buttonValue.value = "previous";
    document.fclBillLaddingform.submit();
}
function cancel() {
    confirmYesNoCancel("Do you want to save the BL changes?", "goBack");
}
function confirmYesNoCancel(confirmMessage, confirmId) {
    returnValue = confirmId;
    DisplayConfirmYesNoCancel("ConfirmYesNoCancelDiv", 100, 50, confirmMessage, window.center({
        width: 100,
        height: 100
    }));
}
function confirmOptionYes() {
    document.getElementById("ConfirmYesNoCancelDiv").style.display = "none";
    grayOut(false, "");
    confirmMessageFunction(returnValue, "yes");
}
function confirmOptionNo() {
    document.getElementById("ConfirmYesNoCancelDiv").style.display = "none";
    grayOut(false, "");
    confirmMessageFunction(returnValue, "no");
}
function confirmOptionCancel() {
    document.getElementById("ConfirmYesNoCancelDiv").style.display = "none";
    grayOut(false, "");
    confirmMessageFunction(returnValue, "cancel");
}
function DisplayConfirmYesNoCancel(divId, left, top, confirmMessage, point) {
    document.getElementById(divId).style.left = left + "px";
    document.getElementById(divId).style.top = top + "px";
    document.getElementById("confirmMessagePara").innerHTML = confirmMessage;
    document.getElementById(divId).style.display = "block";
    document.getElementById(divId).style.left = point.x - 100 + "px";
    document.getElementById(divId).style.top = point.y + "px";
    document.getElementById(divId).style.zIndex = "1000";
    grayOut(true, "");
}
function goBackCall() {
    document.getElementById("goBack1").disabled = true;
    document.getElementById("goBack2").disabled = true;
    document.fclBillLaddingform.buttonValue.value = "previous";
    document.fclBillLaddingform.submit();
}
function manifest1(importFlag) {
  //  alert("manifest1");
  var fileNo = document.fclBillLaddingform.fileNo.value;
    if (document.fclBillLaddingform.readyToPost.checked) {
        if (document.getElementById('portofladding') != null && trim(document.getElementById('portofladding').value) == '') {
            alertNew("Please Enter Port of loading");
            return;
        } else if (checkImportManifest(importFlag)) {
            if(checkAddChargeMappingWithGL(fileNo, "AR") && checkAddChargeMappingWithGL(fileNo, "AC")) {
            confirmNew("Do you want to Manifest this BL ?", "readyToPost");
            }
        }
    } else {
        alertNew("This BL is not ready to Manifest");
        return;
    }
}


function saveMessage(Message) {
    if (document.fclBillLaddingform.bol.value == '') {
        alertNew('Please save Bl Before ' + Message);
        document.fclBillLaddingform.readyToPost.checked = false;
        return false;
    } else {
        return true;
    }
}
function autuNotificationMessage(importFlag) {
    if (document.fclBillLaddingform.billToCode[3].value == 'C') {
        document.fclBillLaddingform.readyToPost.checked = false;
        saved('manifest', importFlag);
    }
}
function directConsignCheck(){
    document.getElementById("defaultAgentN").checked = true;
    document.getElementById("agentNo").value = "";
    document.getElementById("agent").value = "";
    document.getElementById("routedAgentCheck").value = "";
    document.getElementById("routedByAgent").value = "";
    document.getElementById("agent").disabled = true;
    document.getElementById("routedAgentCheck").disabled = true;
    document.getElementById("routedByAgent").disabled = true;
    document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
    document.getElementById("routedAgentCheck").className = "dropdown_accountingDisabled";
    document.getElementById("routedByAgent").className = "textlabelsBoldForTextBoxDisabledLook";
    document.getElementById("agentNo").className = "textlabelsBoldForTextBoxDisabledLook";
}
function directConsignCheckNo(){
    document.getElementById("agent").className = "textlabelsBoldForTextBox";
    document.getElementById("routedAgentCheck").className = "dropdown_accounting";
    document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
    document.getElementById("directConsignmentN").checked = true;
    document.getElementById("defaultAgentY").checked = true;
    fillDefaultAgent();

}
function makeInbondButtonGreen() {
    var bol = document.fclBillLaddingform.bol.value;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "getInbondForThisBL",
            param1: bol
        },
        success: function(data) {
            if (document.getElementById("inbondButton")) {
                if (data == "INBOND") {
                    document.getElementById("inbondButton").className = "buttonColor";
                    document.getElementById("inbondButtonDown").className = "buttonColor";
                } else {
                    document.getElementById("inbondButton").className = "buttonStyleNew";
                    document.getElementById("inbondButtonDown").className = "buttonStyleNew";
                }
            }
        }
    });
}
function calculateInsurance(val1, val2) {
    document.fclBillLaddingform.costOfGoods.value = val1;
    document.fclBillLaddingform.insuranceRate.value = val2;
    document.fclBillLaddingform.buttonValue.value = "calculateInsurance";
    document.fclBillLaddingform.submit();
}
function viewVoid(billLaddingNumber) {
    // view bl void.....
    GB_show('BLVoid', '/logisoft/fclBillLadding.do?buttonValue=viewBLVoid&billofladding=' + billLaddingNumber, width = "300", height = "700");
}
var printOrNot = true;
function PrintReports(importflag) {
    if (!saveMessage('Printng')) {
        return false;
    }
    return checkConfirmBoard('printAction', importflag);
}
function checkConfirmBoard(blAction, importFlag) {
    //	blAction wil be false, if export or else true
    if (document.fclBillLaddingform.readyToPost.checked || blAction == 'printAction') {
        if (!saveMessage(' to Post')) {
            return false;
        }
        var displayMessage = "";
        var bol = document.fclBillLaddingform.bol.value;
        if ((document.getElementById('vesselname') != null && trim(document.getElementById('vesselname').value) == '') && (document.getElementById('vesselname_checkn') != null && trim(document.getElementById('vesselname_checkn').value) == '')) {
            displayMessage += "-->Please Enter Vessel Name <br>";
            jQuery("#vesselname_checkn").css("border-color", "red");
        }
        if (blAction != 'printAction') {
            if (document.fclBillLaddingform.vesselNameCheck.checked == true) {
                displayMessage += "-->Please remove check mark and select Vessel Name<br>";
                jQuery("#vesselname_checkn").css("border-color", "red");
            }
        }
        if (document.getElementById('voyage') != null && trim(document.getElementById('voyage').value) == '') {
            displayMessage += "-->Please Enter SS Voyage <br>";
            jQuery("#voyage").css("border-color", "red");
        }
        if (!document.fclBillLaddingform.confirmOnBoard[0].checked && blAction == 'true') {
            displayMessage += "-->Please  Confirm on board <br>";
        }
        if (document.fclBillLaddingform.eta.value == '' || document.fclBillLaddingform.sailDate.value == '') {
            displayMessage += "-->Please Enter ETA and ETD <br>";
            jQuery("#txtetdCal").css("border-color", "red");
            jQuery("#txtetaCal").css("border-color", "red");
        }
        if (document.getElementById('txtetaFd').value == '') {
            displayMessage += "-->Please Enter ETA FD Date <br>";
            jQuery("#txtetaFd").css("border-color", "red");
        }
        if (document.getElementById('importAMSHosueBlNumber') != null && trim(document.getElementById('importAMSHosueBlNumber').value) == '') {
            displayMessage += "-->Please Enter AMS House BL# <br>";
            jQuery("#importAMSHosueBlNumber").css("border-color", "red");
        }
        if (document.getElementById('portofladding') != null && trim(document.getElementById('portofladding').value) == '') {
            displayMessage += "-->Please Enter Port of loading <br>";
            jQuery("#portofladding").css("border-color", "red");
        }
        if (document.getElementById('routedAgentCheck') != null && trim(document.getElementById('routedAgentCheck').value) == ''
            && document.getElementById('ratesNonRates').value != 'N' && (document.getElementById("directConsignmentN").checked == true)) {
            if (document.getElementById("directConsignmentN").checked == true) {
                displayMessage += "-->Please Select ERT<br>";
                jQuery("#routedAgentCheck").css("border-color", "red");
            }
        }
        if (blAction == 'false' || blAction == 'printAction') {//  || blAction=='printAction'
            var forwrder = document.fclBillLaddingform.forwardingAgentName.value.trim();
            // || forwrder=='NO FF ASSIGNED' || forwrder=='NO FF ASSIGNED / B/L PROVIDED'

            if (trim(document.fclBillLaddingform.streamShipName.value) == "") {
                displayMessage += "-->Please Enter Steam Ship Line Name   <br>";
                jQuery("#streamShipName").css("border-color", "red");
            }
            if (forwrder == '') {
                displayMessage += "-->Please Enter Forwarder name <br>";
                jQuery("#forwardingAgentName").css("border-color", "red");
            }
            if (!document.getElementById('billToCodeF').checked && !document.getElementById('billToCodeS').checked && !document.getElementById('billToCodeT').checked &&
                !document.getElementById('billToCodeA').checked && !document.getElementById('billToCodeN').checked ) {
                displayMessage +=  "-->Please select Bill To Code to complete BL <br>";
            }
            if (document.getElementById('newMasterBL') != null && trim(document.getElementById('newMasterBL').value) == '') {
                displayMessage += "-->Please Enter MASTER BL # <br>";
                jQuery("#newMasterBL").css("border-color", "red");
            }
            if (document.fclBillLaddingform.shipper.value == "") {
                displayMessage += "-->Please Enter House Shipper Name   <br>";
                jQuery("#accountName").css("border-color", "red");

            }

            if (document.fclBillLaddingform.consigneeName.value == "") {
                displayMessage += "-->Please Enter House Consignee Name   <br>";
                jQuery("#consigneeName").css("border-color", "red");
            }
            if (document.fclBillLaddingform.houseName.value == "" || document.fclBillLaddingform.houseShipper.value == "") {
                displayMessage += "-->Please Enter Master Shipper Name And Number  <br>";
                jQuery("#houseName").css("border-color", "red");
            }
            if ((document.fclBillLaddingform.houseConsigneeName.value) == "") {
                displayMessage += "-->Please Enter Master Consignee Name  <br>";
                jQuery("#houseConsigneeName").css("border-color", "red");
            }
            if (document.fclBillLaddingform.notifyPartyName.value == "" && document.fclBillLaddingform.notifyParty.value == "" && document.fclBillLaddingform.billToCode[4].checked) {
                displayMessage += "-->Please Enter House Notify Party Name And Number  <br>";
            } else if (document.fclBillLaddingform.notifyPartyName.value == "" && document.fclBillLaddingform.billToCode[4].checked) {
                displayMessage += "-->Please Enter House Notify Party Name   <br>";
            } else if (document.fclBillLaddingform.notifyParty.value == "" && document.fclBillLaddingform.billToCode[4].checked) {
                displayMessage += "-->Please Enter House Notify Party Number   <br>";
            }

        } else if (blAction == 'true') {
            if (document.fclBillLaddingform.houseName.value == "" || document.fclBillLaddingform.houseShipper.value == "") {
                displayMessage += "-->Please Enter Master Shipper Name And Number  <br>";
            }
            if (document.fclBillLaddingform.masterConsigneeCheck.checked) {
                if ((document.fclBillLaddingform.houseConsigneeName.value) == "") {
                    displayMessage += "-->Please Enter Master Consignee Name  <br>";
                }
            } else if (document.fclBillLaddingform.houseConsigneeName.value == "" || document.fclBillLaddingform.houseConsignee.value == "") {
                displayMessage += "-->Please Enter Master Consignee Name And Number  <br>";
            }
            if (document.fclBillLaddingform.shipper.value == "" || document.fclBillLaddingform.accountName.value == "") {
                displayMessage += "-->Please Enter House Shipper Name And Number  <br>";
            }
            if (document.fclBillLaddingform.consigneeCheck.checked) {
                if (document.fclBillLaddingform.consigneeName.value == "") {
                    displayMessage += "-->Please Enter House Consignee Name  <br>";
                }
            } else if (document.fclBillLaddingform.consigneeName.value == "" || document.fclBillLaddingform.consignee.value == "") {
                displayMessage += "-->Please Enter House Consignee Name And Number  <br>";
            }
            if (document.fclBillLaddingform.forwardingAgentName.value == "" || document.fclBillLaddingform.forwardingAgent1.value == "") {
                displayMessage += "-->Please Enter FF Name And Number  <br>";
            }
            if ((document.getElementById("directConsignmentN").checked == true) && document.fclBillLaddingform.agent.value == "" && document.getElementById('ratesNonRates').value != 'N') {
                displayMessage += "-->WARNING: An agent has not been selected on this file <br>";
            }
            if (document.fclBillLaddingform.houseBL[0].checked || document.fclBillLaddingform.houseBL[1].checked) {
                var billToParty = "";
                if (document.fclBillLaddingform.billToCode[0].checked) {
                    billToParty = "Forwarder";
                } else if (document.fclBillLaddingform.billToCode[1].checked) {
                    billToParty = "Shipper";
                } else if (document.fclBillLaddingform.billToCode[2].checked) {
                    billToParty = "ThirdParty";
                } else if (document.fclBillLaddingform.billToCode[3].checked) {
                    billToParty = "Agent";
                }
                if (document.fclBillLaddingform.billTo) {
                    for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
                        if (document.fclBillLaddingform.billTo[i].value != billToParty) {
                            displayMessage += "-->All the charges are not billed to the proper party  <br>";
                            break;
                        }
                    }
                }
            }
        }
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForUnitNoInContainerDetailsToManifest",
                param1: bol
            },
            async: false,
            success: function(data) {
                if (data != null && undefined != data && data != "") {
                    displayMessage += data;
                }
            }
        });

        //---CONCATENATING ALL THE MESSAGES AND DISPLAYING ONCE. IF NO ERROR MESSAGE IT WILL GO TO ELSE PART----
        if (displayMessage != "") {
            document.fclBillLaddingform.readyToPost.checked = false;
            if (blAction == 'printAction' && null != importFlag && importFlag != 'true') {
                alertBoxNew(displayMessage);
            } else {
                alertNew(displayMessage);
            }
        } else {
            //---------CHECKING FOR AGENTS AND RULES--------------
            printOrNot = true;

            if (blAction != 'printAction') {
                var customer = "";
                if (jQuery("#billToCodeF").attr("checked")) {
                    customer = jQuery("#forwardingAgent1").val();
                } else if (jQuery("#billToCodeS").attr("checked")) {
                    customer = jQuery("#shipper").val();
                } else if (jQuery("#billToCodeT").attr("checked")) {
                    customer = jQuery("#billTrePty").val();
                } else if (jQuery("#billToCodeA").attr("checked")) {
                    customer = jQuery("#consignee").val();
                } else if (jQuery("#billToCodeN").attr("checked")) {
                    customer = jQuery("#notifyParty").val();
                } else {
                    var bill = document.fclBlForm.billTo;
                    var billTo = "";
                    for (var i = 0; i < document.fclBlForm.billTo.length; i++) {
                        if (((bill[i].value).toLowerCase()) == "forwarder") {
                            if (billTo.indexOf("Forwarder") == -1) {
                                if (billTo != "") {
                                    billTo = billTo + ",Forwarder";
                                } else {
                                    billTo = "Forwarder";
                                }
                                if (customer != "") {
                                    customer = customer + "," + jQuery("#forwardingAgent1").val();
                                } else {
                                    customer = jQuery("#forwardingAgent1").val();
                                }
                            }
                        } else if (bill[i].value == "Shipper") {
                            if (billTo.indexOf("Shipper") == -1) {
                                if (billTo != "") {
                                    billTo = billTo + ",Shipper";
                                } else {
                                    billTo = "Shipper";
                                }
                                if (customer != "") {
                                    customer = customer + "," + jQuery("#shipper").val();
                                } else {
                                    customer = jQuery("#shipper").val();
                                }
                            }
                        } else if (bill[i].value == "ThirdParty") {
                            if (billTo.indexOf("ThirdParty") == -1) {
                                if (billTo != "") {
                                    billTo = billTo + ",ThirdParty";
                                } else {
                                    billTo = "ThirdParty";
                                }
                                if (customer != "") {
                                    customer = customer + "," + jQuery("#billTrePty").val();
                                } else {
                                    customer = jQuery("#billTrePty").val();
                                }
                            }
                        } else if (bill[i].value == "Consignee") {
                            if (billTo.indexOf("Consignee") == -1) {
                                if (billTo != "") {
                                    billTo = billTo + ",Consignee";
                                } else {
                                    billTo = "Consignee";
                                }
                                if (customer != "") {
                                    customer = customer + "," + jQuery("#consignee").val();
                                } else {
                                    customer = jQuery("#consignee").val();
                                }
                            }
                        }
                        if (bill[i].value == "NotifyParty") {
                            if (billTo.indexOf("NotifyParty") == -1) {
                                if (billTo != "") {
                                    billTo = billTo + ",NotifyParty";
                                } else {
                                    billTo = "NotifyParty";
                                }
                                if (customer != "") {
                                    customer = customer + "," + jQuery("#notifyParty").val();
                                } else {
                                    customer = jQuery("#notifyParty").val();
                                }
                            }
                        }
                    }
                }
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                        methodName: "validateMasterAccount",
                        param1: customer
                    },
                    async: false,
                    success: function(data) {
                        if (data != '') {
                            jQuery("#readyToPost").attr("checked", false);
                            alertNew(data + " cannot be billed as it is a Master Account");
                        } else {
                            var destination = document.fclBillLaddingform.finalDestination.value;
                            if (document.fclBillLaddingform.agent.value == "") {
                                jQuery.ajaxx({
                                    data: {
                                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                                        methodName: "checkForAgentsAndRules",
                                        param1: destination
                                    },
                                    async: false,
                                    success: function(data) {
                                        if (data != "" && data == "rules" && document.getElementById('ratesNonRates').value != 'N') {
                                            confirmYesOrNo("The Agent Field is empty. Would you like to proceed", "agentFalg");
                                            return false;
                                        } else {
                                            autuNotificationMessage(importFlag);
                                        }
                                    }
                                });
                            } else {
                                if (document.fclBillLaddingform.streamShipBL && document.fclBillLaddingform.streamShipBL[1].checked) {
                                    var agentNo = document.fclBillLaddingform.agentNo.value;
                                    var agentName = document.fclBillLaddingform.agent.value;
                                    var carrierName = document.fclBillLaddingform.streamShipName.value;
                                    var bolId = document.fclBillLaddingform.bol.value;
                                    jQuery.ajaxx({
                                        dataType: "json",
                                        data: {
                                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                                            methodName: "checkCarrierForCost",
                                            param1: bolId,
                                            param2: carrierName,
                                            dataType: "json"
                                        },
                                        success: function(data) {
                                            if (data) {
                                                jQuery.ajaxx({
                                                    dataType: "json",
                                                    data: {
                                                        className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                                                        methodName: "getCustInfoForCustNo",
                                                        param1: agentNo,
                                                        dataType: "json"
                                                    },
                                                    success: function(data) {
                                                        if (null != data) {
                                                            var array = new Array();
                                                            if (null != data.acctType) {
                                                                array = data.acctType.split(',');
                                                                if (array.contains('V')) {
                                                                    confirmYesOrNo("All costs in this file will be transferred to " + agentName + " from " + carrierName + " Confirm 'Y/N'", "transferCost");
                                                                } else {
                                                                    autuNotificationMessage(importFlag);
                                                                }
                                                            } else {
                                                                autuNotificationMessage(importFlag);
                                                            }
                                                        } else {
                                                            autuNotificationMessage(importFlag);
                                                        }
                                                    }
                                                });
                                            } else {
                                                autuNotificationMessage(importFlag);
                                            }
                                        }
                                    });
                                } else {
                                    autuNotificationMessage(importFlag);
                                }
                            }
                        }
                    }
                });
            } else if (blAction == 'printAction') {
                saved('printAction', importFlag);
            }
        }
    }
    return displayMessage;
}

function popup1(mylink, windowname) {
    document.fclBillLaddingform.buttonValue.value = "popup";
    document.fclBillLaddingform.submit();
    if (!window.focus) {
        return true;
    }
    var href;
    if (typeof (mylink) == "string") {
        href = mylink;
    } else {
        href = mylink.href;
    }
    mywindow = window.open(href, windowname, "width=850,height=600,scrollbars=yes");
    mywindow.moveTo(40, 40);
    return false;
}
function popup3(mylink, windowname) {
    document.fclBillLaddingform.buttonValue.value = "hazmat";
    document.fclBillLaddingform.submit();
    if (!window.focus) {
        return true;
    }
    var href;
    if (typeof (mylink) == "string") {
        href = mylink;
    } else {
        href = mylink.href;
    }
    mywindow = window.open(href, windowname, "width=850,height=600,scrollbars=yes");
    mywindow.moveTo(40, 40);
    return false;
}
function popup2(mylink, windowname) {
    if (!window.focus) {
        return true;
    }
    var href;
    if (typeof (mylink) == "string") {
        href = mylink;
    } else {
        href = mylink.href;
    }
    mywindow = window.open(href, windowname, "width=800,height=600,scrollbars=no");
    mywindow.moveTo(40, 40);
    document.fclBillLaddingform.buttonValue.value = "popup";
    document.fclBillLaddingform.submit();
    return false;
}
function voidBl(bolId) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cvst.logisoft.hibernate.dao.FclBlDAO",
            methodName: "checkAccrualPosted",
            param1: bolId,
            dataType: "json"
        },
        success: function(data) {
            if (data) {
                alertNew('Accrual posted,Cannot Void Bl');
            } else {
                confirmYesOrNo("Do you want to Void BL Y/N", "voidBl");
            }
        }
    });
}
function unVoidBl() {
    confirmYesOrNo("Do you want to UnVoid BL Y/N", "UnVoidBl");
}
function disableFieldsWhileVoid(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "text" || element.type == "textarea" || element.type == "select-one") {
            element.style.border = 0;
            if (element.type == "select-one") {
                element.disabled = true;
            } else {
                element.readOnly = true;
                element.tabIndex = -1;
            }
            if (element.type == "text" || element.type == "textarea") {
                element.style.backgroundColor = "#CCEBFF";
                if (element.id == "SSBooking" || element.id == "vessel" || element.id == "ssVoy" || element.id == "txtcal313" || element.id == "txtcal71" || element.id == "txtcal22"
                    || element.id == "txtcal5" || element.id == "fowardername" || element.id == "txtcal13" || element.id == "portOfDischarge" || element.id == "originTerminal" || element.id == "issuingTerminal") {
                    element.style.borderLeft = "red 2px solid";
                }
            } else {
                element.className = "textlabelsBoldForTextBox";
            }
        } else if (element.type == "checkbox" || element.type == "radio") {
            element.style.border = 0;
            element.disabled = true;
            element.tabIndex = -1;
            element.className = "textlabelsBoldForTextBox";
        } else if (element.type == "button") {
            if (element.value != "Go Back" && element.value != "UnVoid") {
                element.style.visibility = "hidden";
            }
        }
    }
    return false;
}
function closedAudit(val, importFlag, OriginInvoiceAttached) {
    var masterBl = document.getElementById('newMasterBL').value;
    var masterStatus = document.getElementById('masterScanStatus').value;
    var houseBlStatus = document.getElementById('houseBlScanStatus').value;
    var orginBlStatus = document.getElementById('orginBlStatus').value;
    var closedBy = document.getElementById('blClosedBy').value;
    var auditedBy = document.getElementById('blAuditedBy').value;
    if (val == 'blOpned' && auditedBy != '') {
        alertNew("Please Cancel Audit BL..");
        return false;
    }
    if (val == 'blAudit' && (closedBy == null || closedBy == '')) {
        alertNew("Please Close BL..");
        return false;
    }
    if (val == 'blAudit') {
        if (trim(masterBl) == "") {
            alertNew("Please Enter Master BL..");
            return false;
        } else if (masterStatus != 'Approved') {
            alertNew("Cannot Audit BL, Master in Disputed State..");
            return false;
        } else if (trim(masterStatus) == "") {
            alertNew("Cannot Audit BL, Master not Attached or Scanned..");
            return false;
        }
        confirmYesOrNo("Do you want to Audit BL Y/N", val);
    }
    else if (val == 'blOpned') {
        confirmYesOrNo("Do you want to Open BL Y/N", val);
    } else if (val == 'blClosed') {
        var alertMessage = "";
        if (trim(masterBl) == "") {
            alertMessage += "Please Enter Master BL..<br>";
        } else if (trim(masterStatus) == 'Disputed') {
            alertMessage += "Cannot Close BL, Master in Disputed State..<br>";
        }
        if (trim(masterStatus) == '') {
            alertMessage += "Cannot Close BL, SS LINE MASTER BL not Attached or Scanned..<br>";
        }
        if (importFlag == "true") {
            if (trim(houseBlStatus) == "") {
                alertMessage += "Cannot Close BL, AGENT HOUSE BL not Attached or Scanned..<br>";
            }
            if (trim(orginBlStatus) == "" && OriginInvoiceAttached != 'true') {
                alertMessage += "Cannot Close BL, ORIGIN INVOICE not Attached or Scanned..<br>";
            }
        }
        if (alertMessage != "") {
            alertNew(alertMessage);
            return false;
        } else {
            confirmYesOrNo("Do you want to Close BL Y/N", val);
        }
    } else if (val == 'blAuditCancel') {
        confirmYesOrNo("Do you want to Cancel Audit BL Y/N", val);
    }
}
function saved(val, importFlag) {
    //--making all fields editable-----
    if (null != importFlag && importFlag == 'true') {
        var errorMessage = '';
        if (document.fclBillLaddingform.finalDestination.value == '') {
            errorMessage += "-->Please Enter Destination<br>";
            jQuery("#finalDestination").css("border-color", "red");
        }
        if (document.fclBillLaddingform.eta.value == '') {
            errorMessage += "-->Please Enter ETA <br>";
            jQuery("#etaCal").css("border-color", "red");
        }
        if (document.fclBillLaddingform.terminalName.value == '') {
            errorMessage += "-->Please Enter Origin Terminal<br>";
            jQuery("#terminalName").css("border-color", "red");
        }
        if (document.fclBillLaddingform.billingTerminal.value == '') {
            errorMessage += "-->Please Enter Issuing Terminal<br>";
            jQuery("#billingTerminal").css("border-color", "red");
        }
        if (errorMessage != '') {
            if (val == 'printAction' && null != importFlag && importFlag != 'true') {
                alertBoxNew(errorMessage);
            } else {
                alertNew(errorMessage);
            }
            return false;
        }
    }
    if (document.fclBillLaddingform.readyToPost.checked && document.getElementById("manifestButtonDown") != null) {
        if (val == 'printAction' && null != importFlag && importFlag != 'true') {
            alertNew("Ready To Post Check box is checked Please manifest the BL or Uncheck the Ready To Post check box and Save");
        } else {
            alertNew("Ready To Post Check box is checked Please manifest the BL or Uncheck the Ready To Post check box and Save");
        }
        return;
    }
    for (var i = 0; i < document.fclBillLaddingform.fileType.length; i++) {
        if (document.fclBillLaddingform.fileType[i].value == "C" && document.fclBillLaddingform.fileType[i].checked && document.fclBillLaddingform.voyage.value == "") {
            if (val == 'printAction' && null != importFlag && importFlag != 'true') {
                alertBoxNew("Please enter Voyage...");
            }
            else {
                alertNew("Please enter Voyage...");
            }
            return;
        }
    }
    if (document.fclBillLaddingform.billToCode[0].checked) {
        if (document.fclBillLaddingform.forwardingAgentName.value.trim() == "NO FF ASSIGNED" ||
            document.fclBillLaddingform.forwardingAgentName.value.trim() == 'NO FF ASSIGNED / B/L PROVIDED' ||
            document.fclBillLaddingform.forwardingAgentName.value.trim() == 'NO FRT. FORWARDER ASSIGNED') {
            if (val == 'printAction' && null != importFlag && importFlag != 'true') {
                alertBoxNew("Please change Bill To Party or change Forwarder, because Forwarder is NO FF ASSIGNED");
            } else {
                alertNew("Please change Bill To Party or change Forwarder, because Forwarder is NO FF ASSIGNED");
            }
            return;
        }
    }
    document.getElementById("save").disabled = true;
    makePageEditableWhileSaving(document.getElementById("fclbl"));
    deleteFclDoorDeliveryValues(document.getElementById("zip").value,document.getElementById("bol").value);
    document.fclBillLaddingform.action.value = val;
    document.fclBillLaddingform.buttonValue.value = "update";
    document.fclBillLaddingform.submit();
}
function disabled(val1) {
    if (val1 == 3) {
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            if (imgs[k].id != "previous" && imgs[k].id != "container" && imgs[k].id != "charges" && imgs[k].id != "clause") {
                imgs[k].style.visibility = "hidden";
            }
        }
        var input = document.getElementsByTagName("input");
        for (i = 0; i < input.length; i++) {
            if (input[i].id != "buttonValue" && input[i].name != "govSchCode" && input[i].name != "termNo" && input[i].name != "scheduleSuffix" && input[i].name != "state" && input[i].name != "country") {
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
        document.getElementById("save").style.visibility = "hidden";
    }
}
function BlnonNegotiation1() {
    var charges = confirm("Print report includes Charges?");
    if (charges == true) {
        document.fclBillLaddingform.buttonValue.value = "nonNegotiation";
        document.fclBillLaddingform.toprintCharges.value = "printCharges";
        document.fclBillLaddingform.submit();
    } else {
        document.fclBillLaddingform.buttonValue.value = "nonNegotiation";
        document.fclBillLaddingform.submit();
    }
}
function getHouseShipperInfoForPopUp() {
    getHouseShipperInfo();
}
function getHouseShipperInfo() {
    jQuery(document).ready(function() {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: document.getElementById("shipper").value
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    document.getElementById("accountName").value = "";
                    document.getElementById("shipper").value = "";
                    document.getElementById("streamShip").value = "";
                    jQuery('#hShipperIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
                    hideHouseShipper();
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: document.getElementById("shipper").value,
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data && null != data.acctNo) {
                                fillHouseShipper(data);
                            }
                        }
                    });
                }
            }
        });

        document.getElementById("shipper_check").value = document.getElementById("shipper").value;
    });
    setFocusFromDojo('consigneeName');
}
function fillHouseShipper(data) {
    var type;
    var subTypes;
    var array1 = new Array();
    var array2 = new Array();
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (data.subType != null) {
        subTypes = (data.subType).toLowerCase();
        array2 = subTypes.split(",");
    }
    if (null != data.acctType && array1.length > 0 && (array1.contains("S") || array1.contains("F") || array1.contains("E") || array1.contains("I") || (array1.contains("V") && array2.contains("forwarder")))) {
        if (data.acctNo != "") {
            document.getElementById("shipper").value = data.acctNo;
        } else {
            document.getElementById("shipper").value = "";
        }
        document.getElementById("streamShip").value = concatenateAddress(data);
        isCustomerNotes('hShipperIcon', data.acctNo);
    }
    hideHouseShipper();
}
function concatenateNotifyAddress(data) {
    var address = "";

    if (null != data.notifyPartyAddress && data.notifyPartyAddress != "") {
        address += wordwrap(data.notifyPartyAddress.replace(/[\r\n]+/g, ""), 35);
        address = address + " \n";
    }
    var address1 = "";
    if (null != data.notifyPartyCity && data.notifyPartyCity != "") {
        address1 += data.notifyPartyCity + ", ";
    }
    if (null != data.notifyPartyState && data.notifyPartyState != "") {
        address1 += data.notifyPartyState + " ";
    }
    if (null != data.notifyPartyPostalCode && data.notifyPartyPostalCode != "") {
        address1 += data.notifyPartyPostalCode + " ";
    }
    address += wordwrap(address1, 35) + "\n";
    if (null != data.notifyPartyCountry && data.notifyPartyCountry != "" && data.notifyPartyCountry != 'UNITED STATES') {
        address += wordwrap(data.notifyPartyCountry, 35) + "\n";
    }
    return address;
}
function concatenateAddress(data) {
    var address = "";
    if (null != data.coName && data.coName != "") {
        address += data.coName + " \n";
    }
    if (null != data.address1 && data.address1 != "") {
        //		address += wordwrap(data.address1.replace(/[\r\n]+/g, ""),35);
        address += data.address1;
        address = address + " \n";
    }
    var address1 = "";
    if (null != data.city1 && data.city1 != "") {
        address1 += data.city1 + ", ";
    }
    if (null != data.state && data.state != "") {
        address1 += data.state + " ";
    }
    if (null != data.zip && data.zip != "") {
        address1 += data.zip + " ";
    }
    address += wordwrap(address1, 35) + "\n";
    if (null != data.cuntry && data.cuntry != "" && data.cuntry.codedesc != 'UNITED STATES') {
        address += wordwrap(data.cuntry.codedesc, 35) + "\n";
    }
    // change compnay name in FClBlConstant
    if (null != data.phone && data.phone != "") {
        address += "PHONE: " + data.phone + "\n";
    }
    if (null != data.fax && data.fax != "") {
        address += "FAX: " + data.fax + "\n";
    }
    if (null != data.email1 && data.email1 != "") {
        address += "EMAIL: " + data.email1;
        if (null != data.email2 && data.email2 != "") {
            address += ",\n" + data.email2;
        }
    }
    else if (null != data.email2 && data.email2 != "") {
        address += "EMAIL: " + data.email2;
    }
    return address;
}
function wordwrap(str, width, brk, cut) {
    brk = brk || '\n';
    width = width || 35;
    cut = cut || false;
    if (!str) {
        return str;
    }
    var regex = '.{1,' + width + '}(\\s|$)' + (cut ? '|.{' + width + '}|.+$' : '|\\S+?(\\s|$)');
    return str.match(RegExp(regex, 'g')).join(brk);
}
function getMasterShipperInfoForPopUp() {
    getMasterShipperInfo();
}
function getMasterShipperInfo() {
    jQuery(document).ready(function() {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: document.getElementById("houseShipper").value
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    document.getElementById("houseName").value = "";
                    document.getElementById("houseShipper").value = "";
                    document.getElementById("houseShipper1").value = "";
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: document.getElementById("houseShipper").value,
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data && null != data.acctNo) {
                                populateMaster(data);
                            }
                        }
                    });
                }
            }
        });
        document.getElementById("houseShipper_check").value = document.getElementById("houseShipper").value;
    });
    setFocusFromDojo('houseConsigneeName');
}
function populateMaster(data) {
    var type;
    var subTypes;
    var array1 = new Array();
    var array2 = new Array();
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (data.subType != null) {
        subTypes = data.subType;
        array2 = subTypes.split(",");
    }
    if (array1.contains("S") || array1.contains("F") || array1.contains("Z") || array1.contains("E") || array1.contains("I") || (array1.contains("V") && array2.contains("Forwarder"))) {
        if (data.acctNo != "") {
            document.getElementById("houseShipper").value = data.acctNo;
        } else {
            document.getElementById("houseShipper").value = "";
        }
        document.getElementById("houseShipper1").value = concatenateAddress(data);
        isCustomerNotes("shipperIcon", data.acctNo);
    }
    hideMasterShipper();
}
function getHouseConsigneeInForPopUp() {
    getHouseConsigneeInfo();
}
function getHouseConsigneeInfo() {
    jQuery(document).ready(function() {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: document.getElementById("consignee").value
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    document.getElementById("consigneeName").value = "";
                    document.getElementById("consignee").value = "";
                    document.getElementById("streamShipConsignee").value = "";
                    jQuery('#hConsigneeIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
                    hideHouseConsignee();
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: document.getElementById("consignee").value,
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data && null != data.acctNo) {
                                fillHouseConsignee(data);
                            }
                        }
                    });
                }
            }
        });

        document.getElementById("consignee_check").value = document.getElementById("consignee").value;
    });
    setFocusFromDojo('notifyPartyName');
}
function fillHouseConsignee(data) {
    var type;
    var array1 = new Array();
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (array1.contains("C")) {
        if (data.acctNo != "") {
            document.getElementById("consignee").value = data.acctNo;
            if (document.getElementById("notifyPartyName").value == '') {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                        methodName: "getTradingPartner",
                        param1: data.acctNo,
                        dataType: "json"
                    },
                    success: function(data) {
                        if (null != data) {
                            if (null != data.notifyParty && data.notifyParty != '') {
                                document.getElementById("notifyPartyName").value = data.notifyParty;
                                document.getElementById("notifyPartyName_check").value = data.notifyParty;
                                document.fclBillLaddingform.notifyCheck.checked = true;
                                document.getElementById("streamshipNotifyParty").value = concatenateNotifyAddress(data);
                            }
                        }
                    }
                });
            }
            isCustomerNotes('hConsigneeIcon', data.acctNo);
        } else {
            document.getElementById("consignee").value = "";
        }
        document.getElementById("streamShipConsignee").value = concatenateAddress(data);
    }
    hideHouseConsignee();
}
function getMasterConsigneeInfoForPopUp() {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "getShipperDetails",
            param1: document.fclBillLaddingform.houseConsigneeName.value,

            dataType: "json"
        },
        success: function(data) {
            populateMasterConsigneeInfo(data);
        }
    });
}
function getMasterConsigneeInfo(importFlag) {
    jQuery(document).ready(function() {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: document.getElementById("houseConsignee").value
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    document.getElementById("houseConsigneeName").value = "";
                    document.getElementById("houseConsignee").value = "";
                    document.getElementById("houseConsignee1").value = "";
                    jQuery('#consigneeIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: document.getElementById("houseConsignee").value,
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data && null != data.acctNo) {
                                fillMasterConsignee(data, importFlag);
                            }
                        }
                    });
                }
            }
        });
        document.getElementById("houseConsignee_check").value = document.getElementById("houseConsignee").value;
    });
    hideMasterConsignee();
    setFocusFromDojo('houseNotifyPartyName');
}

function fillMasterConsignee(data, importFlag) {
    if (data != null) {
        if (data.acctNo != "") {
            document.getElementById("houseConsignee").value = data.acctNo;
            if (document.getElementById("houseNotifyPartyName").value == '') {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                        methodName: "getTradingPartner",
                        param1: data.acctNo,
                        dataType: "json"
                    },
                    success: function(data) {
                        if (null != data) {
                            if (null != data.notifyParty && data.notifyParty != '') {
                                document.getElementById("houseNotifyPartyName").value = data.notifyParty;
                                document.getElementById("houseNotifyPartyName").value = data.notifyParty;
                                document.fclBillLaddingform.masterNotifyCheck.checked = true;
                                document.getElementById("houseNotifyPartyaddress").value = concatenateNotifyAddress(data);
                            }
                        }
                    }
                });
            }
            isCustomerNotes('consigneeIcon', data.acctNo);
        } else {
            document.getElementById("houseConsignee").value = "";
        }
        document.getElementById("houseConsignee1").value = concatenateAddress(data);
        if (undefined != importFlag && null != importFlag && importFlag == 'true') {
    }
    } else {
        document.getElementById("houseConsigneeName").value = "";
        document.getElementById("houseConsignee").value = "";
        document.getElementById("houseConsignee1").value = "";
    }
    hideMasterConsignee();
}
function getHouseNotifyPartyInfoForPopUp() {
    getHouseNotifyPartyInfo();
}
function getHouseNotifyPartyInfo() {
    jQuery(document).ready(function() {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: document.getElementById("notifyParty").value
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    document.getElementById("notifyPartyName").value = "";
                    document.getElementById("notifyParty").value = "";
                    document.getElementById("streamshipNotifyParty").value = "";
                    jQuery('#hNotifyIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
                    hideHouseNotify();
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: document.getElementById("notifyParty").value,

                            dataType: "json"
                        },
                        success: function(data) {
                            if (data && null != data.acctNo) {
                                fillHouseNotify(data);
                            }
                        }
                    });
                }
            }
        });

        document.getElementById("notifyParty_check").value = document.getElementById("notifyParty").value;
    });
    setFocusFromDojo('forwardingAgentName');
}
function fillHouseNotify(data) {
    var type;
    var array1 = new Array();
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (array1.contains("C")) {
        if (data.acctNo != "") {
            document.getElementById("notifyParty").value = data.acctNo;
        } else {
            document.getElementById("notifyParty").value = "";
        }
        document.getElementById("streamshipNotifyParty").value = concatenateAddress(data);
        isCustomerNotes('hNotifyIcon', data.acctNo);
    }
    hideHouseNotify();
}
function getMasterNotifyInfoForPopUp() {
    getMasterNotifyInfo();
}
function getMasterNotifyInfo() {
    jQuery(document).ready(function() {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: document.getElementById("houseNotifyParty").value
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    document.getElementById("houseNotifyPartyName").value = "";
                    document.getElementById("houseNotifyParty").value = "";
                    document.getElementById("houseNotifyPartyaddress").value = "";
                    jQuery('#mNotifyIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: document.getElementById("houseNotifyParty").value,
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data && null != data.acctNo) {
                                fillMasterNotify(data);
                                hideMasterNotify();
                            }
                        }
                    });
                }
            }
        });

        document.getElementById("houseNotifyParty_check").value = document.getElementById("houseNotifyParty").value;
    });
    setFocusFromDojo('accountName');
}
function fillMasterNotify(data) {
    var type;
    var array1 = new Array();
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (array1.contains("C")) {
        if (data.acctNo != "") {
            document.getElementById("houseNotifyParty").value = data.acctNo;
        } else {
            document.getElementById("houseNotifyParty").value = "";
        }
        document.getElementById("houseNotifyPartyaddress").value = concatenateAddress(data);
        isCustomerNotes('mNotifyIcon', data.acctNo);
    }
    hideMasterNotify();
}
function getForwardingInfoForPopUp() {
    getForwardingInfo();
}
function getForwardingInfo() {
    jQuery(document).ready(function() {
        if (document.fclBillLaddingform.billToCode[0].checked && (document.fclBillLaddingform.forwardingAgentName.value.trim() == 'NO FF ASSIGNED' ||
            document.fclBillLaddingform.forwardingAgentName.value.trim() == 'NO FF ASSIGNED / B/L PROVIDED' ||
            document.fclBillLaddingform.forwardingAgentName.value.trim() == 'NO FRT. FORWARDER ASSIGNED')) {
            alertNew("Please change Bill To Party or change Forwarder, because Forwarder is NO FF ASSIGNED");
            document.fclBillLaddingform.forwardingAgentName.value = ""
            document.fclBillLaddingform.forwardingAgent1.value = ""
            document.fclBillLaddingform.forwardingAgentno.value = ""
            hideAgentNameCheck();
            return;
        }
        checkFFcomm();
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: document.getElementById("forwardingAgent1").value
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    document.getElementById("forwardingAgentName").value = "";
                    document.getElementById("forwardingAgent1").value = "";
                    document.getElementById("forwardingAgentno").value = "";
                    jQuery('#freightIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: document.getElementById("forwardingAgent1").value,
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data && null != data.acctNo) {
                                fillForwarder(data);
                            }
                        }
                    });
                }
            }
        });

        document.getElementById("forwardingAgent1_check").value = document.getElementById("forwardingAgent1").value;
        hideAgentNameCheck();
    });
}
function getForwarder(data) {
    var billto;
    if (document.fclBillLaddingform.billTo != undefined) {
        if (document.fclBillLaddingform.billTo.length != undefined) {
            billto = document.fclBillLaddingform.billTo[0].value;
        } else {
            billto = document.fclBillLaddingform.billTo.value;
        }
    }
    if (null != document.getElementById('shipperRow')) {
        document.getElementById('shipperRow').style.display = 'none';
    }
    if (null != document.getElementById('thirdpartyRow')) {
        document.getElementById('thirdpartyRow').style.display = 'none';
    }
    if (null != document.getElementById('agentRow')) {
        document.getElementById('agentRow').style.display = 'none';
    }
    if (null != document.getElementById('forwarderRow')) {
        document.getElementById('forwarderRow').style.display = 'none';
    }
    if (null != document.getElementById('notifyPartyRow')) {
        document.getElementById('notifyPartyRow').style.display = 'none';
    }
    if (null != document.getElementById('consigneeRow')) {
        document.getElementById('consigneeRow').style.display = 'none';
    }
    if (null != document.getElementById('newRow')) {
        document.getElementById('newRow').style.display = 'block';
        if (billto == 'Forwarder') {
            document.getElementById("partyLabel").innerHTML = "Bill To(Forwarder:)"
            document.getElementById("partyName").value = document.fclBillLaddingform.forwardingAgentName.value;
            document.getElementById("partyNumber").innerHTML = document.fclBillLaddingform.forwardingAgent1.value;
        } else if (billto == 'Shipper') {
            document.getElementById("partyLabel").innerHTML = "Bill To(Shipper:)"
            checkForAgentOrConsignee()
            document.getElementById("partyName").value = shipperForImportValue;
            document.getElementById("partyNumber").innerHTML = shipperAccountNoForImportValue;
        } else if (billto == 'ThirdParty') {
            document.getElementById("partyLabel").innerHTML = "Bill To(ThirdParty):"
            document.getElementById("partyName").value = document.fclBillLaddingform.billThirdPartyName.value;
            document.getElementById("partyNumber").innerHTML = document.fclBillLaddingform.billTrePty.value;
        } else if (billto == 'Agent') {
            document.getElementById("partyLabel").innerHTML = "Bill To(Agent):"
            document.getElementById("partyName").value = document.fclBillLaddingform.agent.value;
            document.getElementById("partyNumber").innerHTML = document.fclBillLaddingform.agentNo.value;
        } else if (billto == 'Consignee') {
            document.getElementById("partyLabel").innerHTML = "Bill To(Consignee):"
            document.getElementById("partyName").value = document.fclBillLaddingform.consigneeName.value;
            document.getElementById("partyNumber").innerHTML = document.fclBillLaddingform.consignee.value;
        } else if (billto == 'NotifyParty') {
            document.getElementById("partyLabel").innerHTML = "Bill To(NotifyParty):"
            document.getElementById("partyName").value = document.fclBillLaddingform.notifyPartyName.value;
            document.getElementById("partyNumber").innerHTML = document.fclBillLaddingform.notifyParty.value;
        }
        document.getElementById("partyTotalCharges").innerHTML = data; //partyTotalCharges
    }
}
function getForwarderDetails() {
    var cust = document.fclBillLaddingform.forwardingAgentName.value;
    var customer = cust.replace("&", "amp;");
    GB_show("Customer Search", "/logisoft/quoteCustomer.do?buttonValue=Forwarder&clientName=" + customer, width = "630", height = "900");

}
function getCustomer(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function(dataDup) {
            if (dataDup != "") {
                alertNew(dataDup);
            } else {
                fillCustomerDetails(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13);
            }
        }
    });
}
function fillForwarder(data) {
    var array1 = new Array();
    var type;
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (data.subType != null && (((data.subType).toLowerCase()) == 'forwarder')) {
        if (data.acctNo != "") {
            isCustomerNotes("freightIcon", data.acctNo);
            document.getElementById("forwardingAgent1").value = data.acctNo;
        } else {
            document.getElementById("forwardingAgent1").value = "";
        }
        document.getElementById("forwardingAgentno").value = concatenateAddress(data);
        var forwarderName = document.getElementById("forwardingAgentName").value;
        var bol = document.fclBillLaddingform.bol.value;
        if ((forwarderName == 'NO FF ASSIGNED' || forwarderName == 'NO FF ASSIGNED / B/L PROVIDED'
            || forwarderName == 'NO FRT. FORWARDER ASSIGNED')) {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.dwr.DwrUtil",
                    methodName: "deleteOrAddFFCommission",
                    param1: bol,
                    param2: "delete",
                    param3: "",
                    param4: "",
                    request: "true"
                }
            });
            if (document.getElementById("contactNameButtonForFF")) {
                document.getElementById("contactNameButtonForFF").style.display = "none";
            }
        } else {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.dwr.DwrUtil",
                    methodName: "deleteOrAddFFCommission",
                    param1: bol,
                    param2: "add",
                    param3: forwarderName,
                    param4: data.acctNo,
                    request: "true"
                }
            });
        }
    } else {
        alertNew("Select the Customers with Vendor (Sub type Forwarder)");
        document.getElementById("forwardingAgentName").value = document.getElementById("forwardingAgentName").value;
        document.getElementById("forwardingAgent1").value = "";
        document.getElementById("forwardingAgentno").value = "";
        jQuery('#freightIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
    }
}
function onMasterBlur(path) {
    document.getElementById('houseName').value = "";
    document.getElementById('houseShipper').value = "";
    document.getElementById('houseShipper1').value = "";
    jQuery('#shipperIcon').attr("src", path + "/img/icons/e_contents_view.gif");
    hideMasterShipper();
}
function onConsigneeBlur(path) {
    document.getElementById('houseConsigneeName').value = "";
    document.getElementById('houseConsignee').value = "";
    document.getElementById('houseConsignee1').value = "";
    jQuery('#consigneeIcon').attr("src", path + "/img/icons/e_contents_view.gif");
    hideMasterConsignee();
}
function onPartyBlur(path) {
    document.getElementById('houseNotifyPartyName').value = "";
    document.getElementById('houseNotifyParty').value = "";
    document.getElementById('houseNotifyPartyaddress').value = "";
    jQuery('#mNotifyIcon').attr("src", path + "/img/icons/e_contents_view.gif");
    hideMasterNotify();
}
function onHouseBlur(path) {
    document.getElementById('accountName').value = "";
    document.getElementById('shipper').value = "";
    document.getElementById('streamShip').value = "";
    jQuery('#hShipperIcon').attr("src", path + "/img/icons/e_contents_view.gif");
    hideHouseShipper();
}
function onHouseConsigneeBlur(path) {
    document.getElementById('consigneeName').value = "";
    document.getElementById('consignee').value = "";
    document.getElementById('streamShipConsignee').value = "";
    jQuery('#hConsigneeIcon').attr("src", path + "/img/icons/e_contents_view.gif");
    hideHouseConsignee();
}
function onHousePartyBlur(path) {
    document.getElementById('notifyPartyName').value = "";
    document.getElementById('notifyParty').value = "";
    document.getElementById('streamshipNotifyParty').value = "";
    jQuery('#hNotifyIcon').attr("src", path + "/img/icons/e_contents_view.gif");
    hideHouseNotify();
}
function onFreightBlur(path) {
    document.getElementById('forwardingAgentName').value = "";
    document.getElementById('forwardingAgent1').value = "";
    document.getElementById('forwardingAgentno').value = "";
    jQuery('#freightIcon').attr("src", path + "/img/icons/e_contents_view.gif");
    hideAgentNameCheck();
}

function getVesselNo(ev) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        document.getElementById("vessel").value = "";
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getVesselDetails",
                param1: "",
                param2: document.fclBillLaddingform.vesselname.value,
                dataType: "json"
            },
            success: function(data) {
                if (data) {
                    if (data.code != "") {
                        document.getElementById("vessel").value = data.code;
                    } else {
                        document.getElementById("vessel").value = "";
                    }
                }
            }
        });
    }
}
function getVesselName(ev) {
    if (event.keyCode == 9 || event.keyCode == 13 || event.keyCode == 0) {
        document.getElementById("vesselname").value = "";
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getVesselDetails",
                param1: document.fclBillLaddingform.vessel.value,
                param2: "",
                dataType: "json"
            },
            success: function(data) {
                if (data) {
                    if (data.codedesc != "") {
                        document.getElementById("vesselname").value = data.codedesc;
                    } else {
                        document.getElementById("vesselname").value = "";
                    }
                }
            }
        });
        if (document.getElementById("billThirdPartyName") != undefined && document.getElementById("billThirdPartyName").value != "") {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "getAccountNumberForThirdParty",
                    param1: document.getElementById("billThirdPartyName").value,
                    dataType: "json"
                },
                success: function(data) {
                    if (data) {
                        document.getElementById("billTrePty").value = data.accountNo != null ? data.accountNo : "";
                        document.getElementById("billThirdParty").value = data.address1 != null ? data.address1 : "";
                    }
                }
            });
        }
    }
}
function getshipper() {
    var cust = document.fclBillLaddingform.accountName.value;
    var customer = cust.replace("&", "amp;");
    GB_show("Customer Search", "/logisoft/quoteCustomer.do?buttonValue=Shipper&clientName=" + customer, width = "630", height = "900");
}
function setFocus(val1) {
    if (val1 == "Shipper") {
        document.fclBillLaddingform.houseName.focus();
    }
    if (val1 == "MasterShipper") {
        document.fclBillLaddingform.consigneeName.focus();
    }
    if (val1 == "Consignee") {
        document.fclBillLaddingform.houseConsigneeName.focus();
    }
    if (val1 == "MasterConsignee") {
        document.fclBillLaddingform.notifyPartyName.focus();
    }
    if (val1 == "NotifyParty") {
        document.fclBillLaddingform.houseNotifyPartyName.focus();
    }
    if (val1 == "MasterNotifyParty") {
        document.fclBillLaddingform.forwardingAgentName.focus();
    }
}
function fillCustomerDetails(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13) {
    var array1 = new Array();
    array1 = val4.split(",");
    if (val10 == "Shipper") {
        if (array1.contains("S") || array1.contains("F") || array1.contains("E") || array1.contains("I")) {
            document.fclBillLaddingform.accountName.value = val1;
            document.fclBillLaddingform.shipper.value = val2;
            document.fclBillLaddingform.streamShip.value = val9;
            setTimeout("setFocus('Shipper')", 150);
        } else {
            alertNew("Select the customers with Account Type S,F,E or I");
            document.fclBillLaddingform.accountName.value = "";
            document.fclBillLaddingform.shipper.value = "";
            document.fclBillLaddingform.streamShip.value = "";
        }
    } else {
        if (val10 == "MasterShipper") {
            if (array1.contains("S") || array1.contains("F") || array1.contains("E") || array1.contains("I")) {
                document.fclBillLaddingform.houseName.value = val1;
                document.fclBillLaddingform.houseShipper.value = val2;
                document.fclBillLaddingform.houseShipper1.value = val9;
                setTimeout("setFocus('MasterShipper')", 150);
            } else {
                alertNew("Select the customers with Account Type S,F,E or I");
                document.fclBillLaddingform.houseName.value = "";
                document.fclBillLaddingform.houseShipper.value = "";
                document.fclBillLaddingform.houseShipper1.value = "";
            }
        } else {
            if (val10 == "Consignee") {
                if (array1.contains("C")) {
                    document.fclBillLaddingform.consigneeName.value = val1;
                    document.fclBillLaddingform.consignee.value = val2;
                    document.fclBillLaddingform.streamShipConsignee.value = val9;
                    setTimeout("setFocus('Consignee')", 150);
                } else {
                    alertNew("Select the customers with Account Type C");
                    document.fclBillLaddingform.consigneeName.value = "";
                    document.fclBillLaddingform.consignee.value = "";
                    document.fclBillLaddingform.streamShipConsignee.value = "";
                }
            } else {
                if (val10 == "MasterConsignee") {
                    if (array1.contains("C")) {
                        document.fclBillLaddingform.houseConsigneeName.value = val1;
                        document.fclBillLaddingform.houseConsignee.value = val2;
                        document.fclBillLaddingform.houseConsignee1.value = val9;
                        setTimeout("setFocus('MasterConsignee')", 150);
                    } else {
                        alertNew("Select the customers with Account Type C");
                        document.fclBillLaddingform.houseConsigneeName.value = "";
                        document.fclBillLaddingform.houseConsignee.value = "";
                        document.fclBillLaddingform.houseConsignee1.value = "";
                    }
                } else {
                    if (val10 == "NotifyParty") {
                        if (array1.contains("C")) {
                            document.fclBillLaddingform.notifyPartyName.value = val1;
                            document.fclBillLaddingform.notifyParty.value = val2;
                            document.fclBillLaddingform.streamshipNotifyParty.value = val9;
                            setTimeout("setFocus('NotifyParty')", 150);
                        } else {
                            alertNew("Select the customers with Account Type C");
                            document.fclBillLaddingform.notifyPartyName.value = "";
                            document.fclBillLaddingform.notifyParty.value = "";
                            document.fclBillLaddingform.streamshipNotifyParty.value = "";
                        }
                    } else {
                        if (val10 == "MasterNotifyParty") {
                            if (array1.contains("C")) {
                                document.fclBillLaddingform.houseNotifyPartyName.value = val1;
                                document.fclBillLaddingform.houseNotifyParty.value = val2;
                                document.fclBillLaddingform.houseNotifyPartyaddress.value = val9;
                                setTimeout("setFocus('MasterNotifyParty')", 150);
                            } else {
                                alertNew("Select the customers with Account Type C");
                                document.fclBillLaddingform.houseNotifyPartyName.value = "";
                                document.fclBillLaddingform.houseNotifyParty.value = "";
                                document.fclBillLaddingform.houseNotifyPartyaddress.value = "";
                            }
                        } else {
                            if (val10 == "Forwarder") {
                                if (array1.contains("F")) {
                                    document.fclBillLaddingform.forwardingAgentName.value = val1;
                                    document.fclBillLaddingform.forwardingAgent1.value = val2;
                                    document.fclBillLaddingform.forwardingAgentno.value = val9;
                                } else {
                                    alertNew("Select the customers with Account Type F");
                                    document.fclBillLaddingform.forwardingAgentName.value = "";
                                    document.fclBillLaddingform.forwardingAgent1.value = "";
                                    document.fclBillLaddingform.forwardingAgentno.value = "";
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
function getMasterShipper() {
    var cust = document.fclBillLaddingform.houseName.value;
    var customer = cust.replace("&", "amp;");
    GB_show("Customer Search", "/logisoft/quoteCustomer.do?buttonValue=MasterShipper&clientName=" + customer, width = "630", height = "900");
}
function getConsignee() {
    var cust = document.fclBillLaddingform.consigneeName.value;
    var customer = cust.replace("&", "amp;");
    GB_show("Customer Search", "/logisoft/quoteCustomer.do?buttonValue=Consignee&clientName=" + customer, width = "630", height = "900");
}
function getMasterConsignee() {
    var cust = document.fclBillLaddingform.houseConsigneeName.value;
    var customer = cust.replace("&", "amp;");
    GB_show("Customer Search", "/logisoft/quoteCustomer.do?buttonValue=MasterConsignee&clientName=" + customer, width = "630", height = "900");
}
function getNotifyParty() {
    var cust = document.fclBillLaddingform.notifyPartyName.value;
    var customer = cust.replace("&", "amp;");
    GB_show("Customer Search", "/logisoft/quoteCustomer.do?buttonValue=NotifyParty&clientName=" + customer, width = "630", height = "900");
}
function getMasterNotifyParty() {
    var cust = document.fclBillLaddingform.houseNotifyPartyName.value;
    var customer = cust.replace("&", "amp;");
    GB_show("Customer Search", "/logisoft/quoteCustomer.do?buttonValue=MasterNotifyParty&clientName=" + customer, width = "630", height = "900");
}
function makeFormBorderless(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "button") {
            if (element.value == "Save To Draft" || element.value == "Save" || element.value == "Add"
                || element.value == "Restore" || element.value == "Look Up") {
                if (element.id != "costadd") {
                    element.style.visibility = "hidden";
                }
            }
        }
        if (element.type == "text" || element.type == "textarea") {
            if (element.name != "noOfOriginals") {
                element.style.border = 0;
                element.readOnly = true;
                element.tabIndex = -1;
                element.style.backgroundColor = "#CCEBFF";
            }
        } else {
            if (element.type == "select-one") {
                element.style.border = 0;
                element.disabled = true;
            } else {
                if (element.type == "checkbox") {
                    if (element.name == "readyToPost" || element.name == "ediCheckBox"
                        || element.name == "masterCheckBox") {
                        element.style.border = 0;
                        element.disabled = true;
                        element.className = "whitebackgrnd";
                    }
                }
            }
        }
        if (element.type == "radio") {
            if (element.name == "defaultAgent" || element.name == "streamShipBL"
                || element.name == "houseBL" || element.name == "destinationChargesPreCol"
                || element.name == "fclInttgra" || element.name == "fileType") {
                element.style.border = 0;
                element.disabled = true;
                element.className = "whitebackgrnd";
            }
        }
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            if (imgs[k].id != "costicon" && imgs[k].id != "editcost" && imgs[k].id != "collapsecost"
                && imgs[k].id != "viewgif1" && imgs[k].id != "viewgif2" && imgs[k].id != "viewgif3"
                && imgs[k].id != "viewgif4" && imgs[k].id != "chargesexpand" && imgs[k].id != "chargescollapse") {
                imgs[k].style.visibility = "hidden";
            }
        }
    }
    return false;
}

function callMakeFormBorderlessForPopupPage(form) {
    window.parent.makeFormBorderless(form);
}
function goToARInquiryPage() { // redirect to AR Inquiry page.
    window.location.href = "/logisoft/AccountRecievable.do?";
}

function numericTextbox(text) {
    var dateval = text.value
    if (dateval != "") {
        if ((!isNaN(parseFloat(dateval)) && isFinite(dateval)) == false) {
            alertNew("Please Enter Numeric Value For Transit Days");
            document.getElementById("noOfDays").value = "";
            document.getElementById("noOfDays").focus();
            return;
        }
    }
}
function fillClause() {
    if (event.keyCode == 9 || event.keyCode == 13) {
        var portName = document.fclBillLaddingform.portofdischarge.value;
        var index = portName.indexOf("/");
        var newPortName = portName.substring(0, index);
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cvst.logisoft.hibernate.dao.FclBlDAO",
                methodName: "getClauseForPortname",
                param1: newPortName,
                dataType: "json"
            },
            success: function(data) {
                hello(data);
            }
        });
    }
}
function hello(data) {
    if (data[0] != undefined) {
        document.getElementById("blClause").value = data[0];
        document.getElementById("clauseDescription").value = data[1];
    }
    var portName1 = document.fclBillLaddingform.portofdischarge.value;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
            methodName: "getPortsForAgentInfoWithDefault1",
            param1: portName1
        },
        succesS: function(data) {
            getAgent(data);
        }
    });
}
function getAgent(data) {
    document.getElementById("agentNo").value = data;
    var portName2 = document.fclBillLaddingform.portofdischarge.value;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
            methodName: "getPortsForAgentInfoWithDefault2",
            param1: portName2
        },
        succesS: function(data) {
            getAgentName(data);
        }
    });
}
function getAgentName(data) {
    document.getElementById("agent").value = data;
}
function setDojoAction() {
    var path = "";
    var portOfDischarge = document.fclBillLaddingform.portofdischarge.value;
    var destination = document.fclBillLaddingform.finalDestination.value;
    path = "&portOfDischarge=" + portOfDischarge + "&destination=" + destination;
    appendEncodeUrl(path);
}
function fillDefaultAgent() {
    document.getElementById("agent").className = "textlabelsBoldForTextBox";
    document.getElementById("routedAgentCheck").className = "dropdown_accounting";
    document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
    document.getElementById("directConsignmentN").checked = true;
    document.getElementById("directConsignmentY").checked = false;
    document.getElementById("agent").disabled = false;
    document.getElementById("routedAgentCheck").disabled = false;
    document.getElementById("routedByAgent").disabled = false;
    var destination = document.fclBillLaddingform.finalDestination.value;
    if (destination.indexOf("(") > -1 && destination.indexOf(")") > -1) {
        var destiNew = destination.substring(destination.indexOf("(") + 1, destination.indexOf(")"));
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                methodName: "getDefaultAgent",
                param1: destiNew,
                param2: "I",
                dataType: "json"
            },
            success: function(data) {
                if (data.accountno != undefined && data.accountno != "" && data.accountno != null) {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "checkForDisable",
                            param1: data.accountno
                        },
                        success: function(dataDup) {
                            if (dataDup != "") {
                                alertNew(dataDup);
                                document.getElementById("agentNo").value = "";
                                document.getElementById("agent").value = "";
                            } else {
                                if (data.accountName != undefined && data.accountName != "" && data.accountName != null) {
                                    document.getElementById("agent").value = data.accountName;
                                    document.getElementById("houseConsigneeName").value = data.accountName;
                                } else {
                                    document.getElementById("agent").value = "";
                                }
                                if (data.accountno != undefined && data.accountno != "" && data.accountno != null) {
                                    document.getElementById("agentNo").value = data.accountno;
                                    document.getElementById("houseConsignee").value = data.accountno;
                                } else {
                                    document.getElementById("agentNo").value = "";
                                }
                                if (data.address1 != undefined && data.address1 != "" && data.address1 != null) {
                                    document.getElementById("houseConsignee1").value = data.address1;
                                }
                            }
                        }
                    });
                    document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
                    document.getElementById("agent").readOnly = true;
                    document.getElementById("agentNo").className = "textlabelsBoldForTextBoxDisabledLook";
                    document.getElementById("agentNo").readOnly = true;
                    if (document.fclBillLaddingform.routedAgentCheck.value == "yes" || document.fclBillLaddingform.routedAgentCheck.value == "no") {
                        document.getElementById("routedByAgent").value = "";
                        document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
                        document.fclBillLaddingform.routedAgentCheck.value = "";
                    }
                }
            }
        });
    }
    Event.stopObserving("agent", "blur");
}

function getAgentInfo(val1, val2) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function(dataDup) {
            if (dataDup != "") {
                alertNew(dataDup);
                document.fclBillLaddingform.agent.value = "";
                document.fclBillLaddingform.agentNo.value = "";
            } else {
                document.fclBillLaddingform.agent.value = val1;
                document.fclBillLaddingform.agentNo.value = val2;
                fillDefaultAgent();
            }
        }
    });
}
function setZipCode(ev) {
    document.fclBillLaddingform.zip.value = ev;
}
function getRoutedByAgentFromPopup(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function(dataDup) {
            if (dataDup != "") {
                alertNew(dataDup);
                document.fclBillLaddingform.routedByAgent.value = "";
                document.fclBillLaddingform.country.value = "";
            } else {
                document.fclBillLaddingform.routedByAgent.value = val1;
                document.fclBillLaddingform.country.value = val13;
            }
        }
    });

}
function maxLength(field, maxChars) {
    if (event.keyCode != 8) {
        if (field.value.length >= maxChars) {
            event.returnValue = false;
            return false;
        }
    }
}
function checkRoutedAgent() {
    if (document.fclBillLaddingform.routedAgentCheck.checked) {
        document.fclBillLaddingform.routedByAgent.value = document.fclBillLaddingform.agentNo.value;
    } else {
        document.fclBillLaddingform.routedByAgent.value = "";
    }
}
function scan(fileNo, importFlag) {
    if (null != fileNo && fileNo != '') {
        var screenName = "FCLFILE";
        if (null != importFlag && importFlag == 'true') {
            screenName = "IMPORT FILE";
        }
        var master = document.getElementById("newMasterBL").value;
        GB_show("Scan", "/logisoft/scan.do?screenName=" + screenName + "&documentId=" + fileNo + "&ssMasterBl=" + master, 420, 1150);
    } else {
        alertNew("Please save the file before Scan/Attach");
    }
}
function arInvoice(fileNo, importFlag) {
    if (null !== fileNo && fileNo !== '') {
        var fileType = (importFlag === "true" ? "I" : "");
        GB_show("AR Invoice", "/logisoft/arRedInvoice.do?action=listArInvoice&fileNo=" + fileNo + "&screenName=BL&fileType=" + fileType + "&", 550, 1100);
    } else {
        alertNew("Please Save the file before AR Invoice");
    }
}
//this is to create xml for EDI
var ediId = null;
var ediUser = null;
function generateXml(fileno, fclInttra, bol, userName) {
    if (document.getElementById("lineMove").value == '00') {
        alertNew("Please Select Line Move");
        return false;
    }
    if (undefined != fileno && null != fileno && fileno != '') {
        ediId = bol;
        ediUser = userName;
        if (fclInttra == "INTTRA") {
            jQuery.ajaxx({
                data: {
                    className: "com.logiware.dwr.Edi304Dwr",
                    methodName: "createInttraXml",
                    param1: fileno,
                    param2: "",
                    request: "true"
                },
                success: function(data) {
                    ediData(data);
                }
            });
        } else {
            if (fclInttra == "GT Nexus") {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.edi.gtnexus.CreateGtnexusXml",
                        methodName: "writeXml",
                        param1: fileno,
                        param2: "",
                        request: "true"
                    },
                    success: function(data) {
                        ediData(data);
                    }
                });
            } else {
                alertNew("Selected Carrier is not valid to send EDI to either Inttra or GT Nexus");
                closeEdi();
            }
        }
    } else {
        alertNew("Please save BL before send EDI");
        closeEdi();
    }
}
function ediData(data) {
    if (null != data && data != "") {
        if (data == 'XML generated successfully') {
            jQuery.ajaxx({
                data: {
                    clasSName: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "updateEdi",
                    param1: ediId,
                    param2: ediUser
                },
                success: function(date) {
                    if (null != date && date != "") {
                        document.getElementById("ediby").childNodes[0].nodeValue = ediUser;
                        document.getElementById("edion").childNodes[0].nodeValue = date;
                    }
                }
            });
        }
        alertNew(data);
    }
    closeEdi();
}
function closeEdi() {
    document.getElementById("ediCheckBox").checked = false;
    checkEdi();
}
function readyToSendEdi(fileno, fclInttra, bol, userName, validate) {
    document.getElementById("ediCheckBox").checked = true;
    if (undefined != fileno && null != fileno && fileno != '') {
        ediId = bol;
        ediUser = userName;
        if (fclInttra == "INTTRA") {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.edi.inttra.ValidateInttraXml",
                    methodName: "writeXml",
                    param1: fileno,
                    param2: validate,
                    request: "true"
                },
                success: function(data) {
                    ediAlert(data);
                }
            });
        } else {
            if (fclInttra == "GT Nexus") {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.edi.gtnexus.ValidateGtnexusXml",
                        methodName: "writeXml",
                        param1: fileno,
                        param2: validate,
                        request: "true"
                    },
                    success: function(data) {
                        ediAlert(data);
                    }
                });
            } else {
                alertNew("Selected Carrier is not valid to send EDI to either Inttra or GT Nexus");
                closeEdi();
            }
        }
    } else {
        alertNew("Please save BL before send EDI");
        closeEdi();
    }
}

function checkUncheckEdi(importFlag) {
    if (document.getElementById("ediCheckBox").checked) {
        if (document.getElementById("lineMove").value != '00') {
            saved('sendEdi', importFlag);
        } else {
            alertNew("Please Select Line Move");
            document.getElementById("ediCheckBox").checked = false;
            return false;
        }
    } else {
        if (document.getElementById("sendEdi")) {
            document.getElementById("sendEdi").style.visibility = "hidden";
        }
        if (document.getElementById("sendEdi1")) {
            document.getElementById("sendEdi1").style.visibility = "hidden";
        }
    }
}
function ediAlert(data) {
    if (null != data && data != "") {
        if (data != 'No Error') {
            alertNew(data);
            closeEdi();
        } else {
            if (document.getElementById("ediCheckBox").checked) {
                if (document.getElementById("sendEdi") != null) {
                    document.getElementById("sendEdi").style.visibility = "visible";
                }
                if (document.getElementById("sendEdi1") != null) {
                    document.getElementById("sendEdi1").style.visibility = "visible";
                }
            }
        }
    }
}
function checkEdi() {
    if (document.getElementById("ediCheckBox").checked) {
        if (document.getElementById("sendEdi") != null) {
            document.getElementById("sendEdi").style.visibility = "visible";
        }
        if (document.getElementById("sendEdi1") != null) {
            document.getElementById("sendEdi1").style.visibility = "visible";
        }
    } else {
        if (document.getElementById("sendEdi") != null) {
            document.getElementById("sendEdi").style.visibility = "hidden";
        }
        if (document.getElementById("sendEdi1") != null) {
            document.getElementById("sendEdi1").style.visibility = "hidden";
        }
    }
    if ((document.getElementById("AESCheckBox")) && document.getElementById("AESCheckBox").checked) {
        if (document.getElementById("filingSed") != null) {
            document.getElementById("filingSed").style.visibility = "visible";
        }
        if (document.getElementById("filingSedDownButton") != null) {
            document.getElementById("filingSedDownButton").style.visibility = "visible";
        }
    } else {
        if (document.getElementById("filingSed") != null) {
            document.getElementById("filingSed").style.visibility = "hidden";
        }
        if (document.getElementById("filingSedDownButton") != null) {
            document.getElementById("filingSedDownButton").style.visibility = "hidden";
        }
    }
}
String.prototype.trim = function() {
    return this.replace(/^\s+|\s+$/, "");
};
/* this method wil call for void BL*/
function reverseManifest() {
    confirmNew("VOID will undo the Posting action. Continue", "cancelReadyToPost");
}
/* this method wil call for unManifest BL*/
function unManifest(blNumber) {
    if (document.getElementById('blClosedButton') == null) {
        alertNew("Please Open BL..");
        return false;
    }
    confirmNew("Do you want to Unmanifest BL Y/N", "unManifest");
}

function disableFieldsWhileBlClosed(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "text" || element.type == "textarea" || element.type == "select-one") {
            element.style.border = 0;
            if (element.type == "select-one") {
                element.disabled = true;
            }
            element.readOnly = true;
            element.tabIndex = -1;
            if (element.type == "text" || element.type == "textarea") {
                element.style.backgroundColor = "#CCEBFF";
            } else {
                element.className = "textlabelsBoldForTextBox";
            }
        } else if (element.type == "checkbox" || element.type == "radio") {
            if (element.type == "radio" && (
                element.id == "agentsForCarrierYes" || element.id == "agentsForCarrierNo"
                || element.id == "agentsForCarrierA" || element.id == "shipperLoadYes" || element.id == "shipperLoadNo"
                || element.id == "printUnitYes" || element.id == "printUnitNo" || element.id == "printPointYes"
                || element.id == "printPointNo" || element.id == "noOfPackYes" || element.id == "noOfPackNo" || element.id == "noOfPackAlternate"
                || element.id == "totalContainersYes" || element.id == "totalContainersNo" || element.id == "proofYes"
                || element.id == "proofNo" || element.id == "nonNegotiableYes" || element.id == "nonNegotiableNo"
                || element.id == "importReleaseYes" || element.id == "preAlertYes" || element.id == "preAlertNo"
                || element.id == "importReleaseNo" || element.id == "printRev" || element.id == "doorOriginAsPlor" || element.id == "omitTermAndPort" || element.id == "serviceContractNo"
                || element.id == "doorDestinationAsFinalDeliveryToMaster" || element.id == "collectThirdParty"
                || element.id == "doorDestinationAsFinalDeliveryToHouse" || element.id == "printAlternatePort"
                || element.id == "hblPOLOverrideYes" || element.id == "hblPOLOverrideNo"
                || element.id == "hblPODOverrideYes" || element.id == "hblPODOverrideNo"
                || element.id == "hblFDOverrideYes" || element.id == "hblFDOverrideNo" || element.id == "certifiedTrueCopy"
                || element.id == "trimTrailingZerosForQty" || element.id == "bundleIntoOfr" || element.id == "replaceArrival" || element.id == "dockReceiptYes"
                || element.id == "dockReceiptNo" || element.id == "resendCostToBlueYes" || element.id == "resendCostToBlueNo")) {
                element.disabled = false;
            } else {
                element.style.border = 0;
                element.disabled = true;
                element.className = "textlabelsBoldForTextBox";
            }
        } else if (element.type == "button") {
            if (element.value == "Save To Draft" || element.value == "Add" || element.value == "Restore"
                || element.value == "Look Up" || element.value == "Preview Accruals" || element.value == "HAZ" || element.value == "PKGS") {
                element.style.visibility = "hidden";
            }
            if (element.id == "costadd" || element.id == "note") {
                element.style.visibility = "visible";
            }
        }
    }
    return false;
}
/*this method will call after manifest to make few fileds editable */
function makeSomeFieldsEditableOnManifest(form, importFlag, allowRoutedAgent) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "button") {
            if (element.value == "Save To Draft" || element.value == "Add" || element.value == "Restore"
                || element.value == "Look Up" || element.value == "Preview Accruals") {
                element.style.visibility = "hidden";
            }
            if (element.id == "costadd" || element.id == "note" || element.id == "note") {
                element.style.visibility = "visible";
            }
        }
        if ((importFlag != "I") && (element.type == "text" || element.type == "textarea")) {
            if (element.type == "text" && (element.id == "vesselname" || element.id == "voyage" || element.id == "newMasterBL"
                || element.id == "voyageInternal" || element.id == "pointAndCountryOfOrigin"
                || element.id == "importOriginalBL"
                || element.id == "txtcal4" || element.id == "houseConsigneeName"
                || element.id == "houseNotifyPartyName" || element.id == "houseName"
                || element.id == "newMasterBL" || element.id == "consigneeName"
                || element.id == "notifyPartyName"
                || element.id == "etaValue" || element.id == "importEtaValue" || element.id == "alternatePort"
                || element.id == "hblPOL" || element.id == "hblPOD" || element.id == "hblFD" || element.id == "noOfPackAlter")) {
                element.style.border = "1px solid #C4C5C4";
                element.style.backgroundColor = "#FCFCFC";
                element.readOnly = false;
                element.tabIndex = 0;
            } else if (element.type == "text" && (element.id == "paidStatus")) {
                element.style.backgroundColor = "#FCFCFC";
                element.tabIndex = -1;
            } else {
                if (element.type == "textarea" && (element.id == "domesticRouting" || element.id == "exportReference"
                    || element.id == "onwardInlandRouting" || element.id == "houseShipper1" || element.id == "houseConsignee1"
                    || element.id == "houseNotifyPartyaddress" || element.id == "confOnBoardComments" || element.id == "streamShip"
                    || element.id == "streamShipConsignee" || element.id == "streamshipNotifyParty" || element.id == "importCommentsId"
                    || element.id == "internalRemark" || element.id == "forwardingAgentno"
                    )) {
                    element.style.border = "1px solid #C4C5C4";
                    element.style.backgroundColor = "#FCFCFC";
                    element.readOnly = false;
                    element.tabIndex = 0;
                } else {
            }
            }
            //This field in AES details tab
            if (element.type == "text" && (element.id == "houseName" || element.id == "houseConsigneeName" || element.id == "accountName" || element.id == "consigneeName")) {
                element.style.borderLeft = "red 2px solid";
            }
            //---this is for display fields in Charges tab------
            if (element.type == "text" && (element.id == "disAgent" || element.id == "disAgentNo" || element.id == "disRoutedAgent"
                || element.id == "disRoutedAgentNo" || element.id == "disOrigin" || element.id == "disDestination"
                || element.id == "disCommodityCode" || element.id == "disCarrier" || element.id == "disIssuingTerminal")) {
                element.style.border = 0;
                element.readOnly = true;
                element.tabIndex = -1;
                element.style.backgroundColor = "#FCFCFC";
                element.className = "displayWithBackGroundColorWhite";
            }
            //---for blue color display---
            if (element.type == "text" && (element.id == "disShipper" || element.id == "disForwarder"
                || element.id == "disThirdParty" || element.id == "disAgentName")) {
                element.style.border = 0;
                element.readOnly = true;
                element.tabIndex = -1;
                element.style.backgroundColor = "#FCFCFC";
                element.className = "displayBlueWithBackGroundColorWhite";
            }
        } else if ((importFlag == "I") && (element.type == "text" || element.type == "textarea")) {
            if (element.type == "text" && (element.id == "vesselname" || element.id == "voyage" || element.id == "newMasterBL"
                || element.id == "voyageInternal" || element.id == "pointAndCountryOfOrigin"
                || element.id == "importOriginalBL" || element.id == "txtcal4" || element.id == "newMasterBL"
                || element.id == "etaValue" || element.id == "importEtaValue" || element.id == "alternatePort"
                || element.id == "hblPOL" || element.id == "hblPOD" || element.id == "hblFD" || element.id == "noOfPackAlter")) {
                element.style.border = "1px solid #C4C5C4";
                element.style.backgroundColor = "#FCFCFC";
                element.readOnly = false;
                element.tabIndex = 0;
            } else if (element.type == "text" && (element.id == "paidStatus")) {
                element.style.backgroundColor = "#FCFCFC";
                element.tabIndex = -1;
            } else {
                if (element.type == "textarea" && (element.id == "domesticRouting" || element.id == "exportReference"
                    || element.id == "onwardInlandRouting" || element.id == "confOnBoardComments" || element.id == "importCommentsId"
                    || element.id == "internalRemark" || element.id == "houseShipper1" || element.id == "houseConsignee1" || element.id == "houseNotifyPartyaddress"
                    || element.id == "streamShip" || element.id == "streamShipConsignee" || element.id == "streamshipNotifyParty" || element.id == "forwardingAgentno"
                    )) {
                    element.style.border = "1px solid #C4C5C4";
                    element.style.backgroundColor = "#FCFCFC";
                    element.readOnly = false;
                    element.tabIndex = 0;
                } else {
                    if (element.id == "routedByAgent" && allowRoutedAgent == "true") {
                        element.style.border = "1px solid #C4C5C4";
                        element.style.backgroundColor = "#FCFCFC";
                        element.readOnly = false;
                        element.tabIndex = 0;
                    } else {
                        element.style.backgroundColor = "#CCEBFF";
                        element.style.border = 0;
                        element.readOnly = true;
                        element.tabIndex = -1;
                        element.className = "textlabelsBoldForTextBox";
                    }
                }
            }
            //This field in AES details tab
            if (element.type == "text" && (element.id == "houseName" || element.id == "houseConsigneeName" || element.id == "accountName" || element.id == "consigneeName")) {
                element.style.borderLeft = "red 2px solid";
            }
            //---this is for display fields in Charges tab------
            if (element.type == "text" && (element.id == "disAgent" || element.id == "disAgentNo" || element.id == "disRoutedAgent"
                || element.id == "disRoutedAgentNo" || element.id == "disOrigin" || element.id == "disDestination"
                || element.id == "disCommodityCode" || element.id == "disCarrier" || element.id == "disIssuingTerminal")) {
                element.style.border = 0;
                element.readOnly = true;
                element.tabIndex = -1;
                element.style.backgroundColor = "#FCFCFC";
                element.className = "displayWithBackGroundColorWhite";
            }
            //---for blue color display---
            if (element.type == "text" && (element.id == "disShipper" || element.id == "disForwarder"
                || element.id == "disThirdParty" || element.id == "disAgentName")) {
                element.style.border = 0;
                element.readOnly = true;
                element.tabIndex = -1;
                element.style.backgroundColor = "#FCFCFC";
                element.className = "displayBlueWithBackGroundColorWhite";
            }
        }
        if ((importFlag != "I") && (element.type == "checkbox" || element.type == "radio")) {
            if (element.type == "radio" && (element.id == "sslBlPrepaid" || element.id == "sslBlCollect"
                || element.id == "importFreightYes" || element.id == "importFreightNo" || element.id == "originalBlYes"
                || element.id == "originalBlNo" || element.id == "agentsForCarrierYes" || element.id == "agentsForCarrierNo"
                || element.id == "agentsForCarrierA" || element.id == "shipperLoadYes" || element.id == "shipperLoadNo"
                || element.id == "printUnitYes" || element.id == "printUnitNo" || element.id == "printPointYes"
                || element.id == "printPointNo" || element.id == "noOfPackYes" || element.id == "noOfPackNo" || element.id == "noOfPackAlternate"
                || element.id == "totalContainersYes" || element.id == "totalContainersNo" || element.id == "proofYes"
                || element.id == "proofNo" || element.id == "nonNegotiableYes" || element.id == "nonNegotiableNo"
                || element.id == "sslBlPrepaid" || element.id == "sslBlCollect" || element.id == "importReleaseYes"
                || element.id == "importReleaseNo" || element.id == "printRev" || element.id == "doorOriginAsPlor" || element.id == "omitTermAndPort" || element.id == "serviceContractNo"
                || element.id == "doorDestinationAsFinalDeliveryToMaster" || element.id == "collectThirdParty"
                || element.id == "doorDestinationAsFinalDeliveryToHouse" || element.id == "paymentReleaseYes" || element.id == "printAlternatePort"
                || element.id == "hblPOLOverrideYes" || element.id == "hblPOLOverrideNo" || element.id == "replaceArrival"
                || element.id == "hblPODOverrideYes" || element.id == "hblPODOverrideNo"
                || element.id == "hblFDOverrideYes" || element.id == "hblFDOverrideNo" || element.id == "certifiedTrueCopy"
                || element.id == "paymentReleaseNo" || element.id == "preAlertYes" || element.id == "preAlertNo" || element.id == "trimTrailingZerosForQty"
                || element.id == "bundleIntoOfr" || element.id == "replaceArrival" || element.id == "dockReceiptYes" || element.id == "dockReceiptNo"
                || element.id == "resendCostToBlueYes" || element.id == "resendCostToBlueNo"
                || element.id == "brandEcono" || element.id == "brandEcuworldwide" || element.id == "brandOti")) {
                element.disabled = false;
            } else {
                if (element.type == "checkbox" && (element.id == "ediCheckBox" || element.id == "AESCheckBox"
                    || element.id == "newMasterBLCheckBox" || element.id == "masterConsigneeCheck"
                    || element.id == "masterNotifyCheck" || element.id == "consigneeCheck"
                    || element.id == "notifyCheck" || element.id == "ediShipperCheck"
                    || element.id == "ediConsigneeCheck" || element.id == "ediNotifyPartyCheck" || element.id == "editHouseConsigneeCheck"
                    || element.id == "editHouseNotifyCheck")) {
                    element.disabled = false;
                } else {
                    element.tabIndex = -1;
                    element.disabled = true;
                }
            }
        } else if ((importFlag == "I") && (element.type == "checkbox" || element.type == "radio")) {
            if (element.type == "radio" && (element.id == "sslBlPrepaid" || element.id == "sslBlCollect"
                || element.id == "importFreightYes" || element.id == "importFreightNo" || element.id == "originalBlYes"
                || element.id == "originalBlNo" || element.id == "agentsForCarrierYes" || element.id == "agentsForCarrierNo"
                || element.id == "agentsForCarrierA" || element.id == "shipperLoadYes" || element.id == "shipperLoadNo"
                || element.id == "printUnitYes" || element.id == "printUnitNo" || element.id == "printPointYes"
                || element.id == "printPointNo" || element.id == "noOfPackYes" || element.id == "noOfPackNo" || element.id == "noOfPackAlternate"
                || element.id == "totalContainersYes" || element.id == "totalContainersNo" || element.id == "proofYes"
                || element.id == "proofNo" || element.id == "nonNegotiableYes" || element.id == "nonNegotiableNo"
                || element.id == "sslBlPrepaid" || element.id == "sslBlCollect" || element.id == "importReleaseYes"
                || element.id == "importReleaseNo" || element.id == "printRev" || element.id == "doorOriginAsPlor" || element.id == "omitTermAndPort" || element.id == "serviceContractNo"
                || element.id == "doorDestinationAsFinalDeliveryToMaster" || element.id == "collectThirdParty"
                || element.id == "doorDestinationAsFinalDeliveryToHouse" || element.id == "paymentReleaseYes" || element.id == "printAlternatePort"
                || element.id == "hblPOLOverrideYes" || element.id == "hblPOLOverrideNo" || element.id == "replaceArrival"
                || element.id == "hblPODOverrideYes" || element.id == "hblPODOverrideNo"
                || element.id == "hblFDOverrideYes" || element.id == "hblFDOverrideNo"
                || element.id == "paymentReleaseNo" || element.id == "preAlertYes" || element.id == "preAlertNo" || element.id == "trimTrailingZerosForQty"
                || element.id == "bundleIntoOfr" || element.id == "replaceArrival" || element.id == "certifiedTrueCopy"
                || element.id == "dockReceiptYes" || element.id == "dockReceiptNo"
                || element.id == "resendCostToBlueYes" || element.id == "resendCostToBlueNo"
                || element.id == "brandEcono" || element.id == "brandEcuworldwide" || element.id == "brandOti")) {
                element.disabled = false;
            } else {
                if (element.type == "checkbox" && (element.id == "ediCheckBox" || element.id == "AESCheckBox"
                    || element.id == "newMasterBLCheckBox")) {
                    element.disabled = false;
                } else {
                    element.tabIndex = -1;
                    element.disabled = true;
                }
            }
        }
        if (element.type == "select-one") {
            if (element.id == "routedAgentCheck" && allowRoutedAgent == "true") {
                element.disabled = false;
                jQuery('#routedAgentCheck').addClass("dropdown_accounting");
            } else {
                element.style.border = 0;
                element.disabled = true;
            }
        }
        var imgs = document.getElementsByTagName("img");//imgs[k].id == "chargescollapse"
        for (var k = 0; k < imgs.length; k++) {
            if (imgs[k].name == "containerEnableIcon" || imgs[k].name == "containerDisableIcon"
                || imgs[k].id == "toggle" || imgs[k].id == "addNewTP"
                || imgs[k].id == "etdCal" || imgs[k].id == "cal71" || imgs[k].id == "cal313"
                || imgs[k].id == "etaCal" || imgs[k].id == "chargesDeleteCollapse"
                || imgs[k].id == "caldateOutYard" || imgs[k].id == "caldateIntoYard"
                || imgs[k].name == "containerDeleteIcon" || imgs[k].id == "doorDeliveryCal" || imgs[k].id == "freeDateCal") {
                imgs[k].style.display = "none";
            }
        }
        //--hiding the tool tip ------
        var span = document.getElementsByTagName("span");
        for (var j = 0; j < span.length; j++) {
            if (span[j].id == "chargesSpanId" || span[j].id == "costSpanId") {
                span[j].style.visibility = "hidden";
            }
        }
    }
}
/*this method will call while saving make all fields enable */
function makePageEditableWhileSaving(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "text" || element.type == "textarea") {
            element.style.border = "1px solid #C4C5C4";
            element.style.backgroundColor = "#FCFCFC";
            element.readOnly = false;
            element.tabIndex = 0;
        }
        if (element.type == "select-one") {
            element.style.border = 0;
            element.disabled = false;
        }
        if (element.type == "checkbox" || element.type == "radio") {
            element.disabled = false;
        }
    }
}

//------------------container js method.....
function popup1(val, val1, val2, val3) {
    if (val3 != "3") {
        document.fclBillLaddingform.index.value = val;
        document.fclBillLaddingform.buttonValue.value = "popup";
        document.fclBillLaddingform.submit();
    }
    if (!newwindow.closed && newwindow.location) {
        newwindow.location.href = "<%=path%>/fclmarksnumber.do?button=" + "NewFCLBL&index=" + val + "&containerId=" + val1 + "&bolid=" + val2;
    } else {
        GB_show("Marks/Description", "<%=path%>/fclmarksnumber.do?button=" + "NewFCLBL&index=" + val + "&containerId=" + val1 + "&bolid=" + val2, width = "750", height = "600");
        if (!newwindow.opener) {
            newwindow.opener = self;
        }
    }
    if (window.focus) {
        newwindow.focus();
    }
    return false;
}
function popup2(val, val1, val2, val3) {
    if (val3 != "3") {
        document.fclBillLaddingform.index.value = val;
        document.fclBillLaddingform.buttonValue.value = "hazmat";
        document.fclBillLaddingform.submit();
    }
    if (!newwindow1.closed && newwindow1.location) {
        newwindow1.location.href = "<%=path%>/fCLHazMat.do?buttonValue=fclbl&index=" + val + "&containerId=" + val1 + "&bolid=" + val2 + "&name=FclBl";
    } else {
        newwindow1 = window.open("<%=path%>/fCLHazMat.do?buttonValue=fclbl&index=" + val + "&containerId=" + val1 + "&bolid=" + val2 + "&name=FclBl", "Hi", "width=500,height=800,scrollbars=yes");
        if (!newwindow1.opener) {
            newwindow1.opener = self;
        }
    }
    if (window.focus) {
        newwindow1.focus();
    }
    return false;
}
function submit1() {
    document.fclBillLaddingform.buttonValue.value = "saveContainer";
    document.fclBillLaddingform.submit();
}
function update() {
    document.fclBillLaddingform.buttonValue.value = "edit";
    document.fclBillLaddingform.submit();
}
function submitAdd1() {
    document.fclBillLaddingform.buttonValue.value = "add";
    document.fclBillLaddingform.submit();
}
function enable() {
    var element;
    var trailer;
    var d = new Date();
    var curr_date = d.getDate();
    var curr_month = d.getMonth() + 1;
    var curr_year = d.getFullYear();
    var now = curr_month + "/" + curr_date + "/" + curr_year;
    if (document.fclBillLaddingform.lastUpdate.length != undefined) {
        document.fclBillLaddingform.lastUpdate[val.id].value = now;
    } else {
        document.fclBillLaddingform.lastUpdate.value = now;
    }
    for (var i = 0; i < document.fclBillLaddingform.elements.length; i++) {
        element = document.fclBillLaddingform.elements[i];
        if (element.name == "trailerNo") {
            trailer = element.value;
        }
        if (element.type == "button") {
            if (trailer != "") {
                element.style.visibility = "visible";
            }
        }
    }
}
function getDate(val) {
    var d = new Date();
    var curr_date = d.getDate();
    var curr_month = d.getMonth() + 1;
    var curr_year = d.getFullYear();
    var now = curr_month + "/" + curr_date + "/" + curr_year;
    document.fclBillLaddingform.lastUpdate[val.id].value = now;
}
function checkNumeric(value) {
    var anum = /(^\d+$)|(^\d+\.\d+$)/;
    if (anum.test(value)) {
        return true;
    }
    return false;
}
function check() {
    var element;
    var trailer;
    for (var i = 0; i < document.fclBillLaddingform.elements.length; i++) {
        element = document.fclBillLaddingform.elements[i];
        if (element.name == "trailerNo") {
            trailer = element.value;
        }
        if (element.type == "button") {
            if (trailer != "" && trailer != "undefined") {
                element.style.visibility = "visible";
            }
        }
    }
}
function allowFreeFormat(val) {
    formatTrialNo(val);
}
//............................... CHARGES..........................
function checkbox() {
    var readyToPostCheck = "";
    if (document.fclBillLaddingform.readyToPost != undefined) {
        if (document.fclBillLaddingform.readyToPost.length != undefined) {
            for (var i = 0; i < document.fclBillLaddingform.readyToPost.length; i++) {
                if (document.fclBillLaddingform.readyToPost[i].checked) {
                    readyToPostCheck = readyToPostCheck + "1,";
                } else {
                    readyToPostCheck = readyToPostCheck + "0,";
                }
            }
        } else {
            if (document.fclBillLaddingform.readyToPost.checked) {
                readyToPostCheck = readyToPostCheck + "1,";
            } else {
                readyToPostCheck = readyToPostCheck + "0,";
            }
        }
    }
    var readyToPostCostCheck = "";

    if (document.fclBillLaddingform.readyToPostForCost != undefined) {
        if (document.fclBillLaddingform.readyToPostForCost.length != undefined) {
            for (var i = 0; i < document.fclBillLaddingform.readyToPostForCost.length; i++) {
                if (document.fclBillLaddingform.readyToPostForCost[i].checked) {
                    readyToPostCostCheck = readyToPostCostCheck + "1,";
                } else {
                    readyToPostCostCheck = readyToPostCostCheck + "0,";
                }
            }
        } else {
            if (document.fclBillLaddingform.readyToPostForCost.checked) {
                readyToPostCostCheck = readyToPostCostCheck + "1,";
            } else {
                readyToPostCostCheck = readyToPostCostCheck + "0,";
            }
        }
    }
    document.fclBillLaddingform.readyToPostCheck.value = readyToPostCheck;
    document.fclBillLaddingform.readyToPostCostCheck.value = readyToPostCostCheck;
}
function disabled(val1) {
    if (val1 == 1) {
        var update = document.getElementById("update");
        update.style.visibility = "hidden";
    } else {
        if (val1 == 2) {
            var save = document.getElementById("save");
            save.style.visibility = "hidden";
        }
    }
}
function addCharges() {
    checkbox();
    document.fclBillLaddingform.buttonValue.value = "addCharges";
    document.fclBillLaddingform.submit();
}
function saveCharges() {
    checkbox();
    if (document.fclBillLaddingform.billTo != undefined) {
        for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
            if (document.fclBillLaddingform.billTo[i] != undefined && document.fclBillLaddingform.billTo[i].value == "ThirdParty") {
                if (document.fclBillLaddingform.billThirdPartyName.value == "") {
                    alertNew("Please Enter the Third Party Name");
                    return;
                }
            }
        }
    }
    document.fclBillLaddingform.buttonValue.value = "saveCharges";
    document.fclBillLaddingform.submit();
}
function update() {
    checkbox();
    document.fclBillLaddingform.buttonValue.value = "editCharges";
    document.fclBillLaddingform.submit();
}
function getmanifest(val) {
    if (document.fclBillLaddingform.billTo.length == undefined) {
        if (document.fclBillLaddingform.billTo.value == "ThirdParty") {
            if (document.fclBillLaddingform.accountname.value == "") {
                alertNew("Please Enter the Third Party Name");
                return;
            }
        }
    } else {
        for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
            if (document.fclBillLaddingform.billTo[i].value == "ThirdParty") {
                if (document.fclBillLaddingform.accountname.value == "") {
                    alertNew("Please Enter the Third Party Name");
                    return;
                }
            }
        }
    }
}
function getcostmanifest(obj) {
    var rowindex = obj.parentNode.parentNode.rowIndex;
    var i = rowindex - 1;
    var textstr = "document.fclBillLaddingform.accountname" + (i);
    var textobj = eval(textstr);
    if (textobj.value == "") {
        alertNew("Please Select Account Name");
        document.fclBillLaddingform.readyToPostForCost[i].checked = false;
        return;
    }
}
function getUpdatedCompleteBL() {
    checkbox();
    makePageEditableWhileSaving(document.getElementById("fclbl"));
    document.fclBillLaddingform.buttonValue.value = "getUpdatedBLInfo";
    document.fclBillLaddingform.submit();
}
function disabled(val1) {
    if (val1 == 3) {
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            if (imgs[k].id != "previous" && imgs[k].id != "marks") {
                imgs[k].style.visibility = "hidden";
            }
        }
        var input = document.getElementsByTagName("input");
        for (i = 0; i < input.length; i++) {
            input[i].readOnly = true;
            input[i].tabIndex = -1;
            input[i].style.color = "blue";
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
        document.getElementById("save").style.visibility = "hidden";
        document.getElementById("add").style.visibility = "hidden";
        document.getElementById("add1").style.visibility = "hidden";
        document.getElementById("correction").style.visibility = "hidden";
        document.getElementById("correction1").style.visibility = "hidden";
    }
}
function makeFormBorderless(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "button") {
            if (element.value == "Add" || element.value == "Delete") {
                element.style.visibility = "hidden";
            }
        }
    }
    return false;
}
function checkAll() {
    if (document.fclBillLaddingform.chargesCheckAll.checked) {
        if (document.fclBillLaddingform.readyToPost.length != undefined) {
            for (var j = 0; j < document.fclBillLaddingform.readyToPost.length; j++) {
                document.fclBillLaddingform.readyToPost[j].checked = true;
            }
        } else {
            document.fclBillLaddingform.readyToPost.checked = true;
        }
    } else {
        if (document.fclBillLaddingform.readyToPost.length != undefined) {
            for (var j = 0; j < document.fclBillLaddingform.readyToPost.length; j++) {
                document.fclBillLaddingform.readyToPost[j].checked = false;
            }
        } else {
            document.fclBillLaddingform.readyToPost.checked = false;
        }
    }
}
function costCodeCheckAll() {
    if (document.fclBillLaddingform.fclCostCodeCheckAll.checked) {
        for (var j = 0; j < document.fclBillLaddingform.readyToPostForCost.length; j++) {
            document.fclBillLaddingform.readyToPostForCost[j].checked = true;
        }
    } else {
        for (var j = 0; j < document.fclBillLaddingform.readyToPostForCost.length; j++) {
            document.fclBillLaddingform.readyToPostForCost[j].checked = false;
        }
    }
}
function load1(val) {
    if (val != "") {
        var a = val.replace(/=/g, "\n");
    }
}
var hasCost = true;
function showCostCodes() {
    if (hasCost == true) {
        document.getElementById("collapseCosttable").style.display = "none";
        document.getElementById("expandcosttable").style.display = "block";
        hasCost = false;
    } else {
        document.getElementById("expandcosttable").style.display = "none";
        document.getElementById("collapseCosttable").style.display = "block";
        hasCost = true;
    }
}
var bool = true;
function showCharges() {
    if (bool == true) {
        document.getElementById("collapsetable").style.display = "none";
        document.getElementById("chargestable").style.display = "block";
        bool = false;
    } else {
        document.getElementById("collapsetable").style.display = "block";
        document.getElementById("chargestable").style.display = "none";
        bool = true;
    }
}
function showAccruals() {
    if (document.getElementById("accrualsTables").style.display == "none") {
        document.getElementById("accrualsTables").style.display = "block";
        document.getElementById("manifestedCostTable").style.display = "none";
        if (document.getElementById("totalCostForDown")) {
            document.getElementById("totalCostForDown").style.display = "block";
        }
        if (document.getElementById("totalCostFor")) {
            document.getElementById("totalCostFor").style.display = "none";
        }
        document.getElementById("previewAccruals").value = "Close Accruals";
    } else {
        document.getElementById("accrualsTables").style.display = "none";
        document.getElementById("manifestedCostTable").style.display = "block";
        if (document.getElementById("totalCostForDown")) {
            document.getElementById("totalCostForDown").style.display = "none";
        }
        if (document.getElementById("totalCostFor")) {
            document.getElementById("totalCostFor").style.display = "block";
        }
        document.getElementById("previewAccruals").value = "Preview Accruals";
    }
}
var idString = "";
var aesString = "";
function makeButtonRedOnSave() {
    if (null != document.getElementById("lineitemtable") && undefined != document.getElementById("lineitemtable")) {
        var table = document.getElementById("lineitemtable");
        var length = document.getElementById("lineitemtable").rows.length;
        for (var i = 0; i < length - 1; i++) {
            idString = idString + document.getElementById("containerId" + i).value + ",";
        }
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkAesDetailsForThisContainer",
                param1: idString
            },
            success: function(data) {
                test(data);
            }
        });
    }
    if (null != document.getElementById("sedFilingTable") && undefined != document.getElementById("sedFilingTable")) {
        var aeslength = document.getElementById("sedFilingTable").rows.length;
        for (var j = 0; j < aeslength - 1; j++) {
            aesString = aesString + document.getElementById("transactionRefId" + j).value + ",";
        }
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkAesDetailsForRefNum",
                param1: aesString
            },
            success: function(data) {
                makeAesButtonGreen(data);
            }
        });
    }
}

function makeAesButtonGreen(data) {
    if (undefined != data && null != data) {
        var ss = data.split(",");
        for (var i = 0; i < ss.length; i++) {
            if (ss[i] == "Aes") {
                var j = i + 1;
                if (undefined != document.getElementById("schedB" + j)) {
                    document.getElementById("schedB" + j).className = "buttonColor";
                }
            }
        }
    }
}
function test(data) {
    if (undefined != data && null != data) {
        var ss = data.split(",");
        for (var i = 0; i < ss.length; i++) {
            if (ss[i] == "AesMarksHazmat") {
                if (undefined != document.getElementById("aesbutton" + i)) {
                    document.getElementById("aesbutton" + i).className = "buttonColor";
                }
                if (undefined != document.getElementById("marksbutton" + i)) {
                    document.getElementById("marksbutton" + i).className = "buttonColor";
                }
                if (undefined != document.getElementById("hazmatbutton" + i)) {
                    document.getElementById("hazmatbutton" + i).className = "buttonColor";
                }
            } else {
                if (ss[i] == "AesMarks") {
                    if (undefined != document.getElementById("aesbutton" + i)) {
                        document.getElementById("aesbutton" + i).className = "buttonColor";
                    }
                    if (undefined != document.getElementById("marksbutton" + i)) {
                        document.getElementById("marksbutton" + i).className = "buttonColor";
                    }
                } else {
                    if (ss[i] == "MarksHazmat") {
                        if (undefined != document.getElementById("marksbutton" + i)) {
                            document.getElementById("marksbutton" + i).className = "buttonColor";
                        }
                        if (undefined != document.getElementById("hazmatbutton" + i)) {
                            document.getElementById("hazmatbutton" + i).className = "buttonColor";
                        }
                    } else {
                        if (ss[i] == "AesHazmat") {
                            if (undefined != document.getElementById("aesbutton" + i)) {
                                document.getElementById("aesbutton" + i).className = "buttonColor";
                            }
                            if (undefined != document.getElementById("hazmatbutton" + i)) {
                                document.getElementById("hazmatbutton" + i).className = "buttonColor";
                            }
                        } else {
                            if (ss[i] == "Aes") {
                                if (undefined != document.getElementById("aesbutton" + i)) {
                                    document.getElementById("aesbutton" + i).className = "buttonColor";
                                }
                            } else {
                                if (ss[i] == "Marks") {
                                    if (undefined != document.getElementById("marksbutton" + i)) {
                                        document.getElementById("marksbutton" + i).className = "buttonColor";
                                    }
                                } else {
                                    if (ss[i] == "Hazmat") {
                                        if (undefined != document.getElementById("hazmatbutton" + i)) {
                                            document.getElementById("hazmatbutton" + i).className = "buttonColor";
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
function changeScanButtonColor(masterStatus, documentName, docList) {
    var fileNo = document.fclBillLaddingform.fileNo.value;
    if (documentName == 'AGENT HOUSE BL' || documentName == 'ORIGIN INVOICE') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getHouseBlStatusOrOrginStatus",
                param1: documentName,
                param2: fileNo
            },
            success: function(data) {
                if (null != data && data != '') {
                    if (documentName == 'AGENT HOUSE BL') {
                        document.getElementById('houseBlScanStatus').value = "ScanOrAttached";
                    } else {
                        document.getElementById('orginBlStatus').value = "ScanOrAttached";
                    }
                }
            }
        });
    }
    if (trim(masterStatus) !== "") {
        document.getElementById("masterDiv").childNodes[0].nodeValue = "Yes(" + masterStatus + ")";
        document.getElementById("masterScanStatus").value = masterStatus;
        jQuery('#receivedMaster').val('Yes');
    } else if (documentName === "SS LINE MASTER BL") {
        document.getElementById("masterDiv").childNodes[0].nodeValue = "No";
        jQuery('#receivedMaster').val('No');
    }
    if (docList === 0) {
        document.getElementById("scanButton").className = "buttonStyleNew";
        document.getElementById("scanButtonDown").className = "buttonStyleNew";

    } else {
        document.getElementById("scanButton").className = "buttoncolor";
        document.getElementById("scanButtonDown").className = "buttoncolor";


    }
}
function copyEtaDate() {
    if (document.getElementById("verifiedEtaCheck").checked) {
        document.fclBillLaddingform.verifyEta.value = document.getElementById("etaValue").value;
    } else {
        document.fclBillLaddingform.verifyEta.value = "";
    }
}
function copyImportEtaDate() {
    if (null != document.getElementById("importVerifiedEtaCheck") && document.getElementById("importVerifiedEtaCheck").checked) {
        document.fclBillLaddingform.importVerifiedEta.value = document.getElementById("importEtaValue").value;
    } else {
        document.fclBillLaddingform.importVerifiedEta.value = "";
    }
}
function setSSLBookingNo() {
    if (document.getElementById("newMasterBLCheckBox").checked) {
        document.fclBillLaddingform.newMasterBL.value = document.fclBillLaddingform.booking.value;
    } else {
        document.fclBillLaddingform.newMasterBL.value = "";
    }
}
function checkDisableThirdParty() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForDisable",
            param1: document.getElementById("billTrePty").value
        },
        success: function(dataDup) {
            if (dataDup != "") {
                alertNew(dataDup);
                document.getElementById("billThirdPartyName").value = "";
                document.getElementById("billTrePty").value = "";
            } else {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                        methodName: "getCustInfoForCustNo",
                        param1: document.getElementById("billTrePty").value,
                        dataType: "json"
                    },
                    success: function(data) {
                        if (null != data) {
                            fillThirdPartyNameforFclBl(data);
                        }
                    }
                });
            }
        }
    });
}
function fillThirdPartyNameforFclBl(data) {
    if ((data.acctType == 'V' && (null == data.subType || data.subType == '')) || (data.acctType == 'C' && (null == data.subType || data.subType == ''))
        || (null != data.subType && ((data.subType).toLowerCase()) != 'forwarder' && data.acctType == 'V')) {
        alertNew("Please select the Customers only with Account Type S,O and V with Sub Type Forwarder");
        document.getElementById("billThirdPartyName").value = "";
        document.getElementById("billTrePty").value = "";
    } else {
        confirmYesOrNo("Please note that all Bill to party will be changed for all Charges - Yes to continue and Cancel to abort operation", "thirdparty");
    }
}

function fillThirdPartyName(data) {
    if (data.acctType == 'V' && (null == data.subType || data.subType == '') || (data.acctType == 'C' && (null == data.subType || data.subType == ''))) {
        alertNew("Please select the Customers only with Account Type S,O and V with subtypes");
        document.getElementById("billThirdPartyName").value = "";
        document.getElementById("billTrePty").value = "";
    } else {
        confirmYesOrNo("Please note that all Bill to party will be changed for all Charges - Yes to continue and Cancel to abort operation", "thirdparty");
    }
}
function checkDisabledAgent() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForDisable",
            param1: document.getElementById("agentNo").value
        },
        success: function(dataDup) {
            if (dataDup != "") {
                alertNew(dataDup);
                document.getElementById("agent").value = "";
                document.getElementById("agentNo").value = "";
            }
        }
    });
    setFocusFromDojo('routedByAgent');
}
function setDefaultRouteAgent() {
    var agentNo = document.fclBillLaddingform.agentNo.value;
    if (document.fclBillLaddingform.routedAgentCheck.value == "yes") {
        if (null != agentNo && agentNo != '') {
            document.fclBillLaddingform.routedByAgent.value = document.fclBillLaddingform.agentNo.value;
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "getUnlocDetail",
                    param1: agentNo
                },
                success: function(data) {
                    if (data == "false") {
                        document.getElementById("routedAgentCheck").selectedIndex = 0;
                        document.fclBillLaddingform.routedByAgent.value = "";
                        alertNew("Please Enter UnLocation Code for selected Routed Agent");
                    }
                }
            });
        }
    } else {
        document.fclBillLaddingform.routedByAgent.value = "";
        document.fclBillLaddingform.routedByAgent.className = "textlabelsBoldForTextBox";
        document.fclBillLaddingform.routedByAgent.readOnly = false;
        document.fclBillLaddingform.routedByAgent.tabIndex = -1;
    }
}
function editHouseShipperName(obj) {
    if (obj.checked) {
        Event.stopObserving("accountName", "blur");
    } else {
        document.getElementById("accountName").value = "";
        document.getElementById("shipper").value = "";
        document.getElementById("streamShip").value = "";
        Event.observe("accountName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("accountName_check").value) {
                element.value = '';
                $("accountName_check").value = '';
                $("shipper").value = '';
                $("accountName").value = '';
                $("streamShip").value = '';
            }
            hideHouseShipper();
        }
        );
        hideHouseShipper();
    }
}
function editHouseConsigneeName(obj) {
    if (obj.checked) {
        Event.stopObserving("consigneeName", "blur");
    } else {
        document.getElementById("consigneeName").value = "";
        document.getElementById("consignee").value = "";
        document.getElementById("streamShipConsignee").value = "";
        Event.observe("consigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("consigneeName_check").value) {
                element.value = '';
                $("consigneeName_check").value = '';
                $("consignee").value = '';
                $("consigneeName").value = '';
                $("streamShipConsignee").value = '';
            }
            hideHouseConsignee();
        }
        );
    }
    hideHouseConsignee();
}
function editHouseNotifyName(obj) {
    if (obj.checked) {
        Event.stopObserving("notifyPartyName", "blur");
    } else {
        document.getElementById("notifyPartyName").value = "";
        document.getElementById("notifyParty").value = "";
        document.getElementById("streamshipNotifyParty").value = "";
        Event.observe("consigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("notifyPartyName_check").value) {
                element.value = '';
                $("notifyPartyName_check").value = '';
                $("notifyParty").value = '';
                $("notifyPartyName").value = '';
                $("streamshipNotifyParty").value = '';
            }
            hideHouseNotify();
        }
        );
        hideHouseNotify();
    }
}
function editCustomerForEdi(obj) {
    if (obj.checked) {
        if (obj.id == "ediShipperCheck") {
            if (document.getElementById("houseName").value != "") {
                confirmNew("Do you want to edit Shipper for EDI", "clearShipperForEdi");
            } else {
                Event.stopObserving("houseName", "blur");
            }
        } else if (obj.id == "ediConsigneeCheck") {
            if (document.getElementById("houseConsigneeName").value != "") {
                confirmNew("Do you want to edit Consignee for EDI", "clearConsigneeForEdi");
            } else {
                Event.stopObserving("houseConsigneeName", "blur");
            }
        } else if (obj.id == "ediNotifyPartyCheck") {
            if (document.getElementById("houseNotifyPartyName").value != "") {
                confirmNew("Do you want to edit NotifyParty for EDI", "clearNotifyForEdi");
            } else {
                Event.stopObserving("houseNotifyPartyName", "blur");
            }
        }
    } else {
        if (obj.id == "ediShipperCheck") {
            clearShipperForEdi();
        } else if (obj.id == "ediConsigneeCheck") {
            clearConsigneeForEdi();
        } else if (obj.id == "ediNotifyPartyCheck") {
            clearNotifyForEdi();
        }
    }
}
function clearShipperForEdi() {
    if (document.getElementById('ediShipperCheck').checked) {
        Event.stopObserving("houseName", "blur");
    } else {
        document.getElementById("houseName").value = "";
        Event.observe("houseName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseName_check").value) {
                element.value = '';
                $("houseName_check").value = '';
                $("houseName").value = '';
                $("houseShipper").value = '';
                $("houseShipper1").value = '';
            }
            hideMasterShipper();
        }
        );
        hideMasterShipper();
    }
}
function clearNotifyForEdi() {
    if (document.getElementById('ediNotifyPartyCheck').checked) {
        Event.stopObserving("houseNotifyPartyName", "blur");
    } else {
        document.getElementById("houseNotifyPartyName").value = "";
        Event.observe("houseNotifyPartyName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseNotifyPartyName_check").value) {
                element.value = '';
                $("houseNotifyPartyName_check").value = '';
                $("houseNotifyPartyName").value = '';
                $("houseNotifyParty").value = '';
                $("houseNotifyPartyaddress").value = '';
            }
            hideMasterNotify();
        }
        );
        hideMasterNotify();
    }
}
function clearConsigneeForEdi() {
    if (document.getElementById('ediConsigneeCheck').checked) {
        Event.stopObserving("houseConsigneeName", "blur");
    } else {
        document.getElementById("houseConsigneeName").value = "";
        Event.observe("houseConsigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseConsigneeName_check").value) {
                element.value = '';
                $("houseConsigneeName_check").value = '';
                $("houseConsigneeName").value = '';
                $("houseConsignee").value = '';
                $("houseConsignee1").value = '';
            }
            hideMasterConsignee();
        });
    }
    hideMasterConsignee();
}

function disableAutoComplete(obj) {
    if (obj.checked) {
        if (obj.id == "notifyCheck") {
            if (document.getElementById("notifyPartyName").value != "") {
                confirmNew("Do you want to clear existing NotifyParty Details", "clearNotifyParty");
            } else {
                //document.getElementById("notifyParty").disabled=true;
                //document.getElementById("notifyParty").style.backgroundColor='#CCEBFF';
                document.getElementById("notifyPartyName").value = document.getElementById("notifyCopy").value;
                Event.stopObserving("notifyPartyName", "blur");
                hideHouseNotify();
            }
        } else if (obj.id == "consigneeCheck") {
            if (document.getElementById("consigneeName").value != "") {
                confirmNew("Do you want to clear existing Consignee Details", "clearConsignee");
            } else {
                //document.getElementById("consignee").disabled=true;
                //document.getElementById("consignee").style.backgroundColor='#CCEBFF';
                document.getElementById("consigneeName").value = document.getElementById("consigneeCopy").value;
                Event.stopObserving("consigneeName", "blur");
                hideHouseConsignee();
            }
        } else if (obj.id == "masterNotifyCheck") {
            if (document.getElementById("houseNotifyPartyName").value != "") {
                confirmNew("Do you want to clear existing NotifyParty Details", "clearMasterNotifyParty");
            } else {
                //document.getElementById("houseNotifyParty").disabled=true;
                //document.getElementById("houseNotifyParty").style.backgroundColor='#CCEBFF';
                document.getElementById("houseNotifyPartyName").value = document.getElementById("masterNotifyCopy").value;
                Event.stopObserving("houseNotifyPartyName", "blur");
                hideMasterNotify();
            }
        } else if (obj.id == "masterConsigneeCheck") {
            if (document.getElementById("houseConsigneeName").value != "") {
                confirmNew("Do you want to clear existing Consignee Details", "clearMasterConsignee");
            } else {
                document.getElementById('houseConsignee1').value = "";
                //document.getElementById("houseConsignee").disabled=true;
                //document.getElementById("houseConsignee").style.backgroundColor='#CCEBFF';
                document.getElementById("houseConsigneeName").value = document.getElementById("masterConsigneeCopy").value;
                Event.stopObserving("houseConsigneeName", "blur");
                hideMasterConsignee();
            }
        } else if (obj.id == "houseShipperCheck") {
            if (document.getElementById("accountName").value != "") {
                confirmNew("Do you want to clear existing Shipper Details", "clearHouseShipper");
            } else {
                document.getElementById('streamShip').value = "";
                //document.getElementById("shipper").disabled=true;
                //document.getElementById("shipper").style.backgroundColor='#CCEBFF';
                Event.stopObserving("accountName", "blur");
            }
        }
    } else {
        if (obj.id == "notifyCheck") {
            if (document.getElementById("notifyPartyName").value != "") {
                confirmUncheckClearNotifyBox("Do you want to clear existing NotifyParty Details");

            } else {
                document.getElementById("notifyParty").disabled = false;
                //document.getElementById("notifyParty").style.border='1px solid #C4C5C4';
                //document.getElementById("notifyParty").style.backgroundColor='#FFFFFF';
                Event.observe("notifyPartyName", "blur", function(event) {
                    var element = Event.element(event);
                    if (element.value != $("notifyPartyName_check").value) {
                        element.value = '';
                        $("notifyPartyName_check").value = '';
                        $("notifyPartyName").value = '';
                        $("notifyParty").value = '';
                        $("streamshipNotifyParty").value = '';
                    }
                    hideHouseNotify();
                }
                );
                hideHouseNotify();

            }
        } else if (obj.id == "consigneeCheck") {
            if (document.getElementById("consigneeName").value != "") {
                confirmUncheckClearConsigneeBox("Do you want to clear existing Consignee Details");
            } else {
                document.getElementById("consignee").disabled = false;
                //document.getElementById("consignee").style.border='1px solid #C4C5C4';
                //document.getElementById("consignee").style.backgroundColor='#FFFFFF';
                Event.observe("consigneeName", "blur", function(event) {
                    var element = Event.element(event);
                    if (element.value != $("consigneeName_check").value) {
                        element.value = '';
                        $("consigneeName_check").value = '';
                        $("consigneeName").value = '';
                        $("consignee").value = '';
                        $("streamShipConsignee").value = '';
                    }
                    hideHouseConsignee();
                }
                );
            }
            hideHouseConsignee();
        } else if (obj.id == "masterNotifyCheck") {
            if (document.getElementById("houseNotifyPartyName").value != "") {
                confirmUncheckClearMasterNotifyBox("Do you want to clear existing NotifyParty Details");
            } else {
                document.getElementById("houseNotifyParty").disabled = false;
                //document.getElementById("houseNotifyParty").style.border='1px solid #C4C5C4';
                //document.getElementById("houseNotifyParty").style.backgroundColor='#FFFFFF';
                Event.observe("houseNotifyPartyName", "blur", function(event) {
                    var element = Event.element(event);
                    if (element.value != $("houseNotifyPartyName_check").value) {
                        element.value = '';
                        $("houseNotifyPartyName_check").value = '';
                        $("houseNotifyPartyName").value = '';
                        $("houseNotifyParty").value = '';
                        $("houseNotifyPartyaddress").value = '';
                    }
                    hideMasterNotify();
                }
                );
                hideMasterNotify();
            }
        } else if (obj.id == "masterConsigneeCheck") {
            if (document.getElementById("houseConsigneeName").value != "") {
                confirmUncheckClearMasterConsigneeBox("Do you want to clear existing Consignee Details");
            } else {
                document.getElementById("houseConsignee").disabled = false;
                //document.getElementById("houseConsignee").style.border='1px solid #C4C5C4';
                //document.getElementById("houseConsignee").style.backgroundColor='#FFFFFF';
                Event.observe("houseConsigneeName", "blur", function(event) {
                    var element = Event.element(event);
                    if (element.value != $("houseConsigneeName_check").value) {
                        element.value = '';
                        $("houseConsigneeName_check").value = '';
                        $("houseConsigneeName").value = '';
                        $("houseConsignee").value = '';
                        $("houseConsignee1").value = '';
                    }
                    hideMasterConsignee();
                }
                );
            }
        } else if (obj.id == "houseShipperCheck") {
            if (document.getElementById("accountName").value != "") {
                confirmUnCheckClearHouseShipperBox("Do you want to clear existing Shipper Details");
            } else {
                document.getElementById("shipper").disabled = false;
                //document.getElementById("shipper").style.border='1px solid #C4C5C4';
                //document.getElementById("shipper").style.backgroundColor='#FFFFFF';
                Event.observe("accountName", "blur", function(event) {
                    var element = Event.element(event);
                    if (element.value != $("accountName_check").value) {
                        element.value = '';
                        $("accountName_check").value = '';
                        $("accountName").value = '';
                        $("shipper").value = '';
                        $("streamShip").value = '';
                    }
                }
                );
            }
        }
    }
}
function clearNotifyParty() {
    if (document.getElementById('notifyCheck').checked) {
        document.getElementById("notifyPartyName").value = "";
        document.getElementById("notifyParty").value = "";
        document.getElementById("streamshipNotifyParty").value = "";
        Event.stopObserving("notifyPartyName", "blur");
        hideHouseNotify();
    } else {
        document.getElementById("notifyParty").disabled = false;
        document.getElementById("notifyPartyName").value = "";
        document.getElementById("notifyParty").value = "";
        document.getElementById("streamshipNotifyParty").value = "";
        document.getElementById("notifyCopy").value = "";
        Event.observe("notifyPartyName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("notifyPartyName_check").value) {
                element.value = '';
                $("notifyPartyName_check").value = '';
                $("notifyPartyName").value = '';
                $("notifyParty").value = '';
                $("streamshipNotifyParty").value = '';
            }
            hideHouseNotify();
        }
        );
    }
    hideHouseNotify();
}
function clearConsignee() {
    if (document.getElementById('consigneeCheck').checked) {
        document.getElementById("consigneeName").value = "";
        document.getElementById("consignee").value = "";
        document.getElementById("streamShipConsignee").value = "";
        Event.stopObserving("consigneeName", "blur");
        hideHouseConsignee();
    } else {
        document.getElementById("consignee").disabled = false;
        document.getElementById("consigneeName").value = "";
        document.getElementById("consignee").value = "";
        document.getElementById("streamShipConsignee").value = "";
        document.getElementById("consigneeCopy").value = "";
        Event.observe("consigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("consigneeName_check").value) {
                element.value = '';
                $("consigneeName_check").value = '';
                $("consigneeName").value = '';
                $("consignee").value = '';
                $("streamShipConsignee").value = '';
            }
            hideHouseConsignee();
        }
        );
        hideHouseConsignee();
    }
}
function clearMasterNotify() {
    if (document.getElementById('masterNotifyCheck').checked) {
        document.getElementById("houseNotifyPartyName").value = "";
        document.getElementById("houseNotifyParty").value = "";
        document.getElementById("houseNotifyPartyaddress").value = "";
        Event.stopObserving("houseNotifyPartyName", "blur");
        hideMasterNotify();
    } else {
        document.getElementById("houseNotifyParty").disabled = false;
        document.getElementById("houseNotifyPartyName").value = "";
        document.getElementById("houseNotifyParty").value = "";
        document.getElementById("houseNotifyPartyaddress").value = "";
        document.getElementById("masterNotifyCopy").value = "";
        Event.observe("houseNotifyPartyName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseNotifyPartyName_check").value) {
                element.value = '';
                $("houseNotifyPartyName_check").value = '';
                $("houseNotifyPartyName").value = '';
                $("houseNotifyParty").value = '';
                $("houseNotifyPartyaddress").value = '';
            }
            hideMasterNotify();
        }
        );
        hideMasterNotify();
    }
}
function clearMasterConsignee() {
    if (document.getElementById('masterConsigneeCheck').checked) {
        document.getElementById("houseConsigneeName").value = "";
        document.getElementById("houseConsignee").value = "";
        document.getElementById("houseConsignee1").value = "";
        Event.stopObserving("houseConsigneeName", "blur");
    } else {
        document.getElementById("houseConsignee").disabled = false;
        document.getElementById("houseConsigneeName").value = "";
        document.getElementById("houseConsignee").value = "";
        document.getElementById("houseConsignee1").value = "";
        document.getElementById("masterConsigneeCopy").value = "";
        Event.observe("houseConsigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseConsigneeName_check").value) {
                element.value = '';
                $("houseConsigneeName_check").value = '';
                $("houseConsigneeName").value = '';
                $("houseConsignee").value = '';
                $("houseConsignee1").value = '';
            }
            hideMasterConsignee();
        }
        );
    }
    hideMasterConsignee();
}
function clearHouseShipper() {
    if (document.getElementById('houseShipperCheck') && document.getElementById('houseShipperCheck').checked) {
        document.getElementById("accountName").value = "";
        document.getElementById("shipper").value = "";
        document.getElementById("streamShip").value = "";
        Event.stopObserving("accountName", "blur");
    } else {
        document.getElementById("shipper").disabled = false;
        document.getElementById("accountName").value = "";
        document.getElementById("shipper").value = "";
        document.getElementById("streamShip").value = "";
        Event.observe("accountName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("accountName_check").value) {
                element.value = '';
                $("accountName_check").value = '';
                $("accountName").value = '';
                $("shipper").value = '';
                $("streamShip").value = '';
            }
        }
        );
    }
}
function disableDojo(obj) {
    var path = "";
    var disable = 'disable';
    if (obj.id == "consigneeName") {
        if (document.getElementById("consigneeCheck").checked || document.getElementById("editHouseConsigneeCheck").checked) {
            path = "&disableDojo=" + disable;
            Event.stopObserving("consigneeName", "blur");
        }
    } else if (obj.id == "notifyPartyName") {
        if (document.getElementById("notifyCheck").checked || document.getElementById("editHouseNotifyCheck").checked) {
            path = "&disableAutocomplete=" + disable;
            Event.stopObserving("notifyPartyName", "blur");
        }
    } else if (obj.id == "houseConsigneeName") {
        if (document.getElementById("masterConsigneeCheck").checked) {
            path = "&disableMasterConsigneeDojo=" + disable;
            Event.stopObserving("houseConsigneeName", "blur");
        } else if (document.getElementById("ediConsigneeCheck").checked) {
            path = "&disableMasterConsigneeDojo=" + disable;
            Event.stopObserving("houseConsigneeName", "blur");
        }
    } else if (obj.id == "houseNotifyPartyName") {
        if (document.getElementById("masterNotifyCheck").checked) {
            path = "&disableMasterNotifyDojo=" + disable;
            Event.stopObserving("houseNotifyPartyName", "blur");
        } else if (document.getElementById("ediNotifyPartyCheck").checked) {
            path = "&disableMasterNotifyDojo=" + disable;
            Event.stopObserving("houseNotifyPartyName", "blur");
        }
    } else if (obj.id == "accountName") {
        if (document.getElementById("houseShipperCheck") && document.getElementById("houseShipperCheck").checked) {
            path = "&disableHouseShipperDojo=" + disable;
            Event.stopObserving("accountName", "blur");
        } else if (document.getElementById("editHouseShipperCheck") && document.getElementById("editHouseShipperCheck").checked) {
            path = "&disableHouseShipperDojo=" + disable;
            Event.stopObserving("accountName", "blur");
        }
    } else if (obj.id == "houseName") {
        if (document.getElementById("ediShipperCheck") && document.getElementById("ediShipperCheck").checked) {
            path = "&disableHouseShipperDojo=" + disable;
            Event.stopObserving("houseName", "blur");
        }
    } else if (obj.id == "forwardingAgentName") {
        if (jQuery("#editAgentNameCheck") && jQuery("#editAgentNameCheck").attr("checked")) {
            path = "&disableForwarderDojo=" + disable;
            Event.stopObserving("forwardingAgentName", "blur");
        }
    }
    appendEncodeUrl(path);
}

function sendFrieghtInvoice() {
    var count = 0;
    var selectedIds = new Array();
    jQuery("input[name='freightInvoiceContactIds']:checked").each(function () {
        selectedIds.push(jQuery(this).val());
        count++;
    });
    if (count === 0) {
        alertNew("Please select at least one contact from the list to receive the Freight Invoice.");
    } else {
        jQuery("#freightInvoiceContacts").val(selectedIds);
        document.fclBillLaddingform.readyToPost.checked = true;
        document.fclBillLaddingform.action.value = "sendFrieghtInvoice";
        closePopUp();
        document.getElementById('contactConfigDetails').style.display = 'none';
    }
}
function frieghtInvoiceWillNotSent() {
    document.fclBillLaddingform.readyToPost.checked = true;
    document.fclBillLaddingform.action.value = "frieghtInvoiceWillNotSent";
    closePopUp();
    document.getElementById('contactConfigDetails').style.display = 'none';
}
function doNotSendFrieghtInvoice() {
    document.fclBillLaddingform.readyToPost.checked = true;
    document.fclBillLaddingform.action.value = "doNotSendFrieghtInvoice";
    closePopUp();
    document.getElementById('contactConfigDetails').style.display = 'none';
}

function setComCode() {
    var commodityCode = document.fclBillLaddingform.commodityCode.value;
    if (commodityCode != '') {
        var comAndDesc = commodityCode.split(':-');
        if (comAndDesc.length > 0) {
            document.fclBillLaddingform.commodityCode.value = comAndDesc[0];
            document.fclBillLaddingform.commodityDesc.value = comAndDesc[1];
        }
    }
}
function goToRemarksLookUp(importFlag) {
    GB_show("Pre-defined Remarks", "/logisoft/remarksLookUp.do?buttonValue=Quotation&importFlag=" + importFlag, width = "400", height = "700");
}
function goToRemarksLookUpforImport(importFlag) {
    GB_show("Pre-defined Remarks", "/logisoft/remarksLookUp.do?buttonValue=BlImports&importFlag=" + importFlag, width = "400", height = "700");
}
function getAllRemarksFromPopUp(val) {
    var commentVal = document.fclBillLaddingform.exportReference.value;
    var totalLength = commentVal.length + val.length;
    if (totalLength > 250) {
        alertNew("More than 250 characters are not allowed");
        return;
    }
    var oldarray = document.fclBillLaddingform.exportReference.value;
    var splittedArray;
    if (oldarray.length == 0) {
        splittedArray = oldarray;
    } else {
        splittedArray = oldarray.split("\n");
    }
    var newarray = val.split(",");
    var resultarray = new Array();
    var flag = false;
    for (var k = 0; k < newarray.length; k++) {
        flag = false;
        for (var l = 0; l < splittedArray.length; l++) {
            if (newarray[k].replace(/^[\s]+/, "").replace(/[\s]+$/, "").replace(/[\s]{2,}/, " ") == splittedArray[l].replace(/^[\s]+/, "").replace(/[\s]+$/, "").replace(/[\s]{2,}/, " ")) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            if (oldarray == "") {
                oldarray = newarray[k];
            } else {
                oldarray += "\n" + newarray[k];
            }
        }
    }
    document.fclBillLaddingform.exportReference.value = oldarray;
}
function getAllRemarksforImportFromPopUp(val) {
    var commentVal = document.fclBillLaddingform.domesticRouting.value;
    var totalLength = commentVal.length + val.length;
    if (totalLength > 250) {
        alertNew("More than 250 characters are not allowed");
        return;
    }
    var oldarray = document.fclBillLaddingform.domesticRouting.value;
    var splittedArray;
    if (oldarray.length == 0) {
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
            if (newarray[k].replace(/^[\s]+/, "").replace(/[\s]+$/, "").replace(/[\s]{2,}/, " ") == splittedArray[l].replace(/^[\s]+/, "").replace(/[\s]+$/, "").replace(/[\s]{2,}/, " ")) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            if (oldarray == "") {
                oldarray = newarray[k];
            } else {
                oldarray += "\n" + newarray[k];
            }
        }
    }
    document.fclBillLaddingform.domesticRouting.value = oldarray.replace(/>/g, "");
}
function setSSLNameForImportBL() {
    if (event.keyCode == 9 || event.keyCode == 13 || event.keyCode == 0) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: document.getElementById("sslinenumber").value
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    document.getElementById("streamShipName").value = "";
                    document.getElementById("sslinenumber").value = "";
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: document.getElementById("sslinenumber").value,
                            dataType: "json"
                        },
                        success: function(data) {
                            if (null != data) {
                                fillSSLName(data);
                            }
                        }
                    });
                }
            }
        });
    }
    setFocusFromDojo('vesselname');
}
function fillSSLName(data) {
    var type;
    var array1 = new Array();
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (array1.contains("V") && data.subType == 'Steamship Line') {
        document.getElementById("streamShipName").value = data.acctName;
        document.getElementById("sslinenumber").value = data.acctNo;
    } else {
        alertNew("Please select the Customers only with Account Type V and subType Steamship Line");
        document.getElementById("streamShipName").value = "";
        document.getElementById("sslinenumber").value = "";
    }
}
function validateBlETA(val, data) {
    if (data.value != "") {
        data.value = data.value.getValidDateTime("/", "", false);
        if (data.value == "" || data.value.length > 10) {
            alertNew("Please enter valid date");
            data.value = "";
            document.getElementById(data.id).focus();
        }
    }
    if (data.value !== "") {
        var date = new Date();
        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear();
        var etdDate = document.fclBillLaddingform.sailDate.value;
        var etaDate = document.fclBillLaddingform.eta.value;
        var originalDate = document.getElementById("etaDate").value;
        var etaFD = document.getElementById("txtetaFd").value;
        var d = etdDate.split("/");
        var s = d[0] + "/" + d[1] + "/" + d[2];
        var etd = new Date(s);
        var etaf = etaFD.split("/");
        var fd = etaf[0] + "/" + etaf[1] + "/" + etaf[2];
        var etafdd = new Date(fd);
        var e = etaDate.split("/");
        var r = e[0] + "/" + e[1] + "/" + e[2];
        var eta = new Date(r);
        var now = new Date();
        var eta1 = new Date();
        eta1.setDate(eta1.getDate() + 60);
        if (etafdd <= eta) {
            alertNew("ETA Cannot be greater than or equal to ETA at FD");
            document.fclBillLaddingform.eta.value = "";
            document.fclBillLaddingform.eta.focus();
        }
        if (val == 'true') {
            now.setDate(now.getDate() - 60);
        }
        if (eta < now) {
            if (val == 'true') {
                alertNew("ETA Cannot be less than ETD");
            } else {
                alertNew("ETA Cannot be less than ETD");
            }
            document.fclBillLaddingform.eta.value = originalDate;
            document.fclBillLaddingform.eta.focus();
            return;
        } else if (eta <= etd) {
            alertNew("ETA  Cannot be less than or equal to ETD");
            document.fclBillLaddingform.eta.value = originalDate;
            document.fclBillLaddingform.eta.focus();
            return;
        } else {
            document.getElementById("etaDate").value = document.fclBillLaddingform.eta.value;
            if (null != etaDate && etaDate != '' && etdDate != '') {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                        methodName: "getTransitDays",
                        param1: etdDate,
                        param2: etaDate
                    },
                    success: function(data) {
                        if (null != data) {
                            document.fclBillLaddingform.noOfDays.value = data;
                        }
                    }
                });
            }
        }
        if (null != etaDate && etaDate != '' && etdDate != '') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "getTransitDays",
                    param1: etdDate,
                    param2: etaDate
                },
                success: function(data) {
                    if (null != data) {
                        document.fclBillLaddingform.noOfDays.value = data;
                    }

                }
            });
        }
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "dateValidationforSixMonth",
                param1: etdDate,
                param2: etaDate
            },
            success: function(data) {
                if (data == "greater") {
                    alertNew("ETA can never be more than six months from the ETD");
                    document.getElementById("txtetaCal").value = "";
                    document.getElementById("txtetaCal").focus();
                    return;
                }
            }
        });
        yearValidation(data);
    }
}
function validateBlETD(data) {
    if (data.value !== "") {
        data.value = data.value.getValidDateTime("/", "", false);
        if (data.value == "" || data.value.length > 10) {
            alertNew("Please enter valid date");
            data.value = "";
            document.getElementById(data.id).focus();
        }
    }
    if (data.value != "") {
        var date = new Date();
        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear();
        var previousTime = new Date();
        previousTime.setDate(previousTime.getDate() - 60);
        var previousDate = previousTime.getMonth() + 1 + "/" + previousTime.getDate() + "/" + previousTime.getFullYear();
        var etaDate = document.fclBillLaddingform.eta.value;
        var etdDate = document.fclBillLaddingform.sailDate.value;
        var originalDate = document.getElementById("etdDate").value;
        var d = etdDate.split("/");
        var s = d[0] + "/" + d[1] + "/" + d[2];
        var etd = new Date(s);
        var e = etaDate.split("/");
        var r = e[0] + "/" + e[1] + "/" + e[2];
        var eta = new Date(r);
        var now = new Date();
        var etdYearValidation = d[2] == date.getFullYear() || d[2] == date.getFullYear() + 1 || d[2] == date.getFullYear() - 1;
        if (!etdYearValidation) {
            alertNew("Please Enter Valid Year");
            document.fclBillLaddingform.sailDate.value = "";
            document.fclBillLaddingform.sailDate.focus();
            return;
        }
        now.setDate(now.getDate() - 60);
        if (etd >= eta) {
            alertNew("ETD Cannot be greater than or equal to ETA");
            document.fclBillLaddingform.sailDate.value = "";
            document.fclBillLaddingform.sailDate.focus();
            return;
        } else {
            document.getElementById("etdDate").value = document.fclBillLaddingform.sailDate.value;
        }
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "dateValidationforSixMonth",
                param1: currentDate,
                param2: etdDate
            },
            success: function(data) {
                if (data == "greater") {
                    alertNew("ETD should not be greater than six month from Today's date");
                    document.getElementById("txtetdCal").value = "";
                    document.getElementById("txtetdCal").focus();
                    return;
                }
            }
        });
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "dateValidationforSixMonth",
                param1: currentDate,
                param2: etdDate
            },
            success: function(data) {
                if (data == "lesser") {
                    alertNew("ETD should not be Less than six month from Today's date");
                    document.getElementById("txtetdCal").value = "";
                    document.getElementById("txtetdCal").focus();
                    return;
                }
            }
        });
    }
}


function validateBlETAFD(data) {
    if (data.value != "") {
        data.value = data.value.getValidDateTime("/", "", false);
        if (data.value == "" || data.value.length > 10) {
            alertNew("Please enter valid date");
            data.value = "";
            document.getElementById(data.id).focus();
        }
    }
    if (data.value !== "") {
        var previousTime = new Date();
        previousTime.setDate(previousTime.getDate() - 60);
        var etaDate = document.fclBillLaddingform.eta.value;
        var originalDate = document.getElementById("txtetaFd").value;
        var d = originalDate.split("/");
        var s = d[0] + "/" + d[1] + "/" + d[2];
        var etd = new Date(s);
        var e = etaDate.split("/");
        var r = e[0] + "/" + e[1] + "/" + e[2];
        var eta = new Date(r);
        var now = new Date();
        now.setDate(now.getDate() - 60);
        var etdDate = document.fclBillLaddingform.sailDate.value;
        var etdd = etdDate.split("/");
        var r1 = etdd[0] + "/" + etdd[1] + "/" + etdd[2];
        var etddd = new Date(r1);
        if (etddd >= etd) {
            alertNew("ETA at FD Cannot be Less Than ETD");
            document.getElementById("txtetaFd").value = "";
            document.fclBillLaddingform.etaFd.focus();
            return;
        } else if (etd < eta) {
            alertNew("ETD at FD  Cannot be Less than ETA");
            document.getElementById("txtetaFd").value = "";
            document.fclBillLaddingform.etaFd.focus();
            return;
        } else {
            document.fclBillLaddingform.etaFd.value = document.getElementById("txtetaFd").value;
        }
        yearValidation(data);
    }
}
function fillPol() {
    document.getElementById("portOfLoading_check").value = document.fclBillLaddingform.terminalName.value;
    document.fclBillLaddingform.portofladding.value = document.fclBillLaddingform.terminalName.value;
    setFocusFromDojo('finalDestination');
}
function fillPod() {
    document.getElementById("portofDischarge_check").value = document.fclBillLaddingform.finalDestination.value;
    document.fclBillLaddingform.portofdischarge.value = document.fclBillLaddingform.finalDestination.value;
    setFocusFromDojo('portofladding');
    insuranceAllowedForPort();
}
function getContactInfo(party) {
    var customerName = "";
    var customer = "";
    if (party == 'MC') {
        customerName = document.fclBillLaddingform.houseConsigneeName.value;
        customer = document.fclBillLaddingform.houseConsignee.value;
    } else if (party == 'MS') {
        customerName = document.fclBillLaddingform.houseName.value;
        customer = document.fclBillLaddingform.houseShipper.value;
    } else if (party == 'HC') {
        customerName = document.getElementById("consigneeName").value;
        customer = document.fclBillLaddingform.consignee.value;
    } else if (party == 'HS') {
        customerName = document.getElementById("accountName").value;
        customer = document.fclBillLaddingform.shipper.value;
    } else if (party == 'F') {
        customerName = document.fclBillLaddingform.forwardingAgentName.value;
        customer = document.fclBillLaddingform.forwardingAgent1.value;
    } else if (party == 'T') {
        customerName = document.fclBillLaddingform.billThirdPartyName.value;
        customer = document.fclBillLaddingform.billTrePty.value;
    }
    customerName = customerName.replace("&", "amp;");
    GB_show("Contact Info", "/logisoft/customerAddress.do?buttonValue=Quotation&custNo=" + customer + "&custName=" + customerName, width = "400", height = "1100");
}
function checkPaymentRelease() {
    if (document.getElementById('paymentReleaseYes').checked) {
        var date = new Date();
        //--getMonth() & getCurrentTime() methods are in common.js----
        var month = getMonthName(date.getMonth());
        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear();
        setValue('txtpayRelCal', currentDate);//In Common.js
        document.getElementById('payRelCal').style.visibility = "visible";
        disableReadOnly('txtpayRelCal');
        disableReadOnly('paymentReleaseComments');
        disableReadOnly('checkNumber');
        disableReadOnly('paymentAmount');
        disableReadOnly('amountAndPaidBy');
    } else {
        setValue('txtpayRelCal', '');
        setValue('paymentReleaseComments', '');
        setValue('checkNumber', '');
        setValue('paymentAmount', '');
        setValue('amountAndPaidBy', '');
        makeReadOnly('txtpayRelCal');
        makeReadOnly('paymentReleaseComments');
        makeReadOnly('checkNumber');
        makeReadOnly('paymentAmount');
        makeReadOnly('amountAndPaidBy');
        document.getElementById('payRelCal').style.visibility = "hidden";
    }
}
function getTerminalAddress() {
    var terminal = getValue('billingTerminal');
    if (trim(terminal) != '') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getTerminalAddress",
                param1: terminal
            },
            success: function(data) {
                fillMasterConsigneeAddress(data);
            }
        });
    }
    updateDomesticRouting();
    setFocusFromDojo('doorOfOrigin');
}
function fillMasterConsigneeAddress(data) {
    if (data != '') {
        setValue('houseConsignee1', data);
    }
}
function setFocusFromDojo(focusTo) {
    if (document.getElementById(focusTo)) {
        document.getElementById(focusTo).focus();
    }
}
function setFocusToSSL() {
    setTimeout("setFocus()", 800);
}
function setFocus() {
    if (document.getElementById("sslBlPrepaid")) {
        document.getElementById("sslBlPrepaid").focus();
    } else {
        document.getElementById("streamShipBLPrepaid").focus();
    }
    //for alternate port
    if (document.fclBillLaddingform.printAlternatePort[0].checked) {
        document.getElementById("alterNatePortId").style.display = "block";
    } else {
        document.getElementById("alterNatePortId").style.display = "none";
    }
    if (jQuery("#hblPOLOverrideYes").attr("checked")) {
        document.getElementById("hblPOLId").style.display = "block";
    } else {
        document.getElementById("hblPOLId").style.display = "none";
    }
    if (jQuery("#hblPODOverrideYes").attr("checked")) {
        document.getElementById("hblPODId").style.display = "block";
    } else {
        if (document.getElementById("hblPODId")) {
            document.getElementById("hblPODId").style.display = "none";
        }
    }
    if (jQuery("#hblFDOverrideYes").attr("checked")) {
        document.getElementById("hblFDId").style.display = "block";
    } else {
        document.getElementById("hblFDId").style.display = "none";
    }
    hideAlternatePack();
}
function saveBlOnNavigate() {
    var bol = document.fclBillLaddingform.bol.value;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.dwr.DwrUtil",
            methodName: "setFileList",
            param1: bol,
            request: "true"
        },
        async: false,
        success: function(data) {
            if (null != data && data != '') {
        }
        }
    });
}
function allowOnlyWholeNumbers(obj) {
    var result;
    if (!/^[1-9]\d*$/.test(obj.value)) {
        //alertNew("This field accepts numbers only");
        result = obj.value.replace(/[^0-9]+/g, '');
        document.fclmarksnumberForm.noOfpkgs.value = result;
        return false;
    }
    return true;
}
function changeSchedButtonColor(schedSize, index) {
    if (schedSize == '0') {
        document.getElementById("schedB" + index).className = "buttonStyleNew";
    } else {
        document.getElementById("schedB" + index).className = "buttonColor";
    }
    GB_hide();
}
function hideArInvoice(fileNo) {
    GB_hide();
    makeARInvoiceButtonGreen(fileNo,"");

}
function hideAlternatePort() {
    if (document.fclBillLaddingform.printAlternatePort[0].checked) {
        document.getElementById("alternatePort").value = "";
        document.getElementById("alterNatePortId").style.display = "block";
    } else {
        document.getElementById("alterNatePortId").style.display = "none";
    }
}
function hideHblPOL() {
    if (jQuery("#hblPOLOverrideYes").attr("checked")) {
        document.getElementById("hblPOL").value = "";
        document.getElementById("hblPOLId").style.display = "block";
    } else {
        document.getElementById("hblPOLId").style.display = "none";
    }
}
function hideHblPOD() {
    if (jQuery("#hblPODOverrideYes").attr("checked")) {
        document.getElementById("hblPOD").value = "";
        document.getElementById("hblPODId").style.display = "block";
    } else {
        document.getElementById("hblPODId").style.display = "none";
    }
}
function hideHblFD() {
    if (jQuery("#hblFDOverrideYes").attr("checked")) {
        document.getElementById("hblFD").value = "";
        document.getElementById("hblFDId").style.display = "block";
    } else {
        document.getElementById("hblFDId").style.display = "none";
    }
}
function copyNotListedTp(from, to) {
    document.getElementById(to).value = from.value;
}
function changeImportReleaseColor(paymentRelease, importRelease, result, date, overPaid) {
    if (importRelease == 'Y') {
        if (paymentRelease == 'Y') {
            document.getElementById("importReleaseButton").className = "buttonColor";
            document.getElementById("importReleaseDown").className = "buttonColor";
        } else {
            document.getElementById("importReleaseButton").className = "buttonColorRed";
            document.getElementById("importReleaseDown").className = "buttonColorRed";
        }
    } else if (importRelease == 'N') {
        if (paymentRelease == 'Y') {
            document.getElementById("importReleaseButton").className = "buttonColorRed";
            document.getElementById("importReleaseDown").className = "buttonColorRed";
        } else {
            document.getElementById("importReleaseButton").className = "buttonStyleNew";
            document.getElementById("importReleaseDown").className = "buttonStyleNew";
        }
    } else {
        document.getElementById("importReleaseButton").className = "buttonStyleNew";
        document.getElementById("importReleaseDown").className = "buttonStyleNew";
    }
    if (result != 'Updated') {
        document.getElementById("paymentBy").childNodes[0].nodeValue = result;
        document.getElementById("paymentOn").childNodes[0].nodeValue = date;
    } else {
        document.getElementById("paymentBy").childNodes[0].nodeValue = '';
        document.getElementById("paymentOn").childNodes[0].nodeValue = '';
    }
    if (overPaid == 'Over Paid') {
        document.getElementById("paidStatus").value = 'Over Paid';
        document.getElementById("paidStatus").style.display = 'block';
        document.getElementById("paidStatus").style.color = 'green';
        document.getElementById("paidStatus").style.border = 0;
        document.getElementById("paidStatus").style.fontSize = '18px';
        document.getElementById("paidStatus").style.width = '100px';
        document.getElementById("paidStatus").style.fontWeight = 'bold';
    } else {
        document.getElementById("paidStatus").value = '';
        document.getElementById("paidStatus").style.display = 'none';
    }
}
function closeImportReleasePopup(overPaid) {
    if (overPaid == 'Over Paid') {
        document.getElementById("paidStatus").value = 'Over Paid';
        document.getElementById("paidStatus").style.display = 'block';
        document.getElementById("paidStatus").style.color = 'green';
        document.getElementById("paidStatus").style.border = 0;
        document.getElementById("paidStatus").style.fontSize = '18px';
        document.getElementById("paidStatus").style.width = '100px';
        document.getElementById("paidStatus").style.fontWeight = 'bold';
    } else {
        document.getElementById("paidStatus").value = '';
        document.getElementById("paidStatus").style.display = 'none';
    }
}
function setFocusToTab(selectedTab) {
    jQuery(function() {
        var $tabs = jQuery('#fclBLContainer').tabs();
        if (selectedTab == 'aes') {
            $tabs.tabs('select', 6);
        } else if (selectedTab == 'print') {
            $tabs.tabs('select', 5);
        } else if (selectedTab == 'charges') {
            $tabs.tabs('select', 4);
        } else if (selectedTab == 'container') {
            $tabs.tabs('select', 3);
        } else if (selectedTab == 'general') {
            $tabs.tabs('select', 2);
        } else if (selectedTab == 'shipperForwarder') {
            $tabs.tabs('select', 1);
        } else {
            $tabs.tabs('select', 0);
        }
    });
}
function manifestBl() {
    makePageEditableWhileSaving(document.getElementById("fclbl"));
    document.fclBillLaddingform.buttonValue.value = "manifest";
    document.getElementById('eventCode').value = '100009';
    document.getElementById('newProgressBar').style.display = 'block';
    document.getElementById('cover').style.display = 'block';
    document.getElementById("manifestButtonUp").disabled = true;
    document.fclBillLaddingform.submit();
    document.getElementById("unManifestButton").enabled = true;
}
function accountDetails() {
    var customerNumber = '';
    var forwarderNumber = '';
    var shipperNumber = '';
    var thirdPartyNumber = '';
    var consigneeNumber = '';
    var notifyPartyNumber = '';
    var result = "";
    var autoCreditStatus = "";
    var importFlagForAgent = document.fclBillLaddingform.billToCode[3].value;
    if (document.getElementById("houseBlBoth").checked) {
        //alert("houseBlBoth is checked");
        var bill = document.fclBillLaddingform.billTo;
        if (document.fclBillLaddingform.billTo != undefined && document.fclBillLaddingform.billTo.length != undefined) {
            hideAndShowCredtiStatus(bill);
            for (var i = 0; i < document.fclBillLaddingform.billTo.length; i++) {
                if (bill[i].value.toLowerCase() === "shipper") {
                    customerNumber = jQuery("#shipper").val();
                } else if (bill[i].value.toLowerCase() === "forwarder" && customerNumber == "") {
                    customerNumber = jQuery("#forwardingAgent1").val();
                } else if (bill[i].value.toLowerCase() === "thirdparty" && customerNumber == "") {
                    customerNumber = jQuery("#billTrePty").val();
                } else if (bill[i].value.toLowerCase() === "consignee" && customerNumber == "") {
                    customerNumber = jQuery("#consignee").val();
                } else if (bill[i].value.toLowerCase() === "notifyparty" && customerNumber == "") {
                    customerNumber = jQuery("#notifyParty").val();
                }
                creditStatusBillTo(customerNumber);
                if (bill[i].value.toLowerCase() === "forwarder") {
                    forwarderNumber = jQuery("#forwardingAgent1").val();
                }
                if (bill[i].value.toLowerCase() === "shipper") {
                    shipperNumber = jQuery("#shipper").val();
                }
                if (bill[i].value.toLowerCase() === "thirdparty") {
                    thirdPartyNumber = jQuery("#billTrePty").val();
                }
                if (bill[i].value.toLowerCase() === "consignee") {
                    consigneeNumber = jQuery("#consignee").val();
                }
                if (bill[i].value.toLowerCase() === "notifyparty") {
                    notifyPartyNumber = jQuery("#notifyParty").val();
                }
                if (forwarderNumber != '') {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "validateCustomer",
                            param1: forwarderNumber,
                            param2: "I"
                        },
                        async: false,
                        success: function(data) {
                            if (data != null && data != '') {
                                var chargecode = data.split("==");
                                var autosHold = data.split("===");
                                var crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                                var autoCredit = autosHold[1].substring();
                                if (crditWarning == "In Good Standing ") {
                                    result = "CREDIT: " + crditWarning;
                                    document.getElementById('forwarderWarning').className = "creditStyle";
                                } else {
                                    result = "WARNING: " + crditWarning;
                                    document.getElementById('forwarderWarning').className = "warningStyle";
                                }
                                if (autoCredit.indexOf("HHG/PE/AUTOS CREDIT") > -1) {
                                    autoCreditStatus = "HHG/PE/AUTOS CREDIT";
                                    document.getElementById('forwarderStatus').className = "creditStyle";
                                } else {
                                    autoCreditStatus = "NO CREDIT FOR HHG/PE/AUTOS";
                                    document.getElementById('forwarderStatus').className = "warningStyle";
                                }
                            }
                        }
                    });
                    if (result != "") {
                        document.getElementById('forwarderWarning').innerHTML = result;
                    }
                    if (autoCreditStatus != "") {
                        document.getElementById('forwarderStatus').innerHTML = autoCreditStatus;
                    }
                }
                if (shipperNumber != '') {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "validateCustomer",
                            param1: shipperNumber,
                            param2: "I"
                        },
                        async: false,
                        success: function(data) {
                            if (data != null && data != '') {
                                var chargecode = data.split("==");
                                var autosHold = data.split("===");
                                var crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                                var autoCredit = autosHold[1].substring();
                                if (crditWarning == "In Good Standing ") {
                                    result = "CREDIT: " + crditWarning;
                                    document.getElementById('shipperWarning').className = "creditStyle";
                                } else {
                                    result = "WARNING: " + crditWarning;
                                    document.getElementById('shipperWarning').className = "warningStyle";
                                }
                                if (autoCredit.indexOf("HHG/PE/AUTOS CREDIT") > -1) {
                                    autoCreditStatus = "HHG/PE/AUTOS CREDIT";
                                    document.getElementById('shipperStatus').className = "creditStyle";
                                } else {
                                    autoCreditStatus = "NO CREDIT FOR HHG/PE/AUTOS";
                                    document.getElementById('shipperStatus').className = "warningStyle";
                                }
                            }
                        }
                    });
                    if (result != "") {
                        document.getElementById('shipperWarning').innerHTML = result;
                    }
                    if (autoCreditStatus != "") {
                        document.getElementById('shipperStatus').innerHTML = autoCreditStatus;
                    }
                }
                if (thirdPartyNumber != '') {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "validateCustomer",
                            param1: thirdPartyNumber,
                            param2: "I"
                        },
                        async: false,
                        success: function(data) {
                            if (data != null && data != '') {
                                var chargecode = data.split("==");
                                var autosHold = data.split("===");
                                var crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                                var autoCredit = autosHold[1].substring();
                                if (crditWarning == "In Good Standing ") {
                                    result = "CREDIT: " + crditWarning;
                                    document.getElementById('thirdpartyWarning').className = "creditStyle";
                                } else {
                                    result = "WARNING: " + crditWarning;
                                    document.getElementById('thirdpartyWarning').className = "warningStyle";
                                }
                                if (autoCredit.indexOf("HHG/PE/AUTOS CREDIT") > -1) {
                                    autoCreditStatus = "HHG/PE/AUTOS CREDIT";
                                    document.getElementById('thirdpartyStatus').className = "creditStyle";
                                } else {
                                    autoCreditStatus = "NO CREDIT FOR HHG/PE/AUTOS";
                                    document.getElementById('thirdpartyStatus').className = "warningStyle";
                                }
                            }
                        }
                    });
                    if (result != "") {
                        document.getElementById('thirdpartyWarning').innerHTML = result;
                    }
                    if (autoCreditStatus != "") {
                        document.getElementById('thirdpartyStatus').innerHTML = autoCreditStatus;
                    }
                }
                if (consigneeNumber != '') {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "validateCustomer",
                            param1: consigneeNumber,
                            param2: "I"
                        },
                        async: false,
                        success: function(data) {
                            if (data != null && data != '') {
                                var chargecode = data.split("==");
                                var autosHold = data.split("===");
                                var crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                                var autoCredit = autosHold[1].substring();
                                if (crditWarning == "In Good Standing ") {
                                    result = "CREDIT: " + crditWarning;
                                    document.getElementById('consigneeWarning').className = "creditStyle";
                                } else {
                                    result = "WARNING: " + crditWarning;
                                    document.getElementById('consigneeWarning').className = "warningStyle";
                                }
                                if (autoCredit.indexOf("HHG/PE/AUTOS CREDIT") > -1) {
                                    autoCreditStatus = "HHG/PE/AUTOS CREDIT";
                                    document.getElementById('consigneeStatus').className = "creditStyle";
                                } else {
                                    autoCreditStatus = "NO CREDIT FOR HHG/PE/AUTOS";
                                    document.getElementById('consigneeStatus').className = "warningStyle";
                                }
                            }
                        }
                    });
                    if (result != "") {
                        document.getElementById('consigneeWarning').innerHTML = result;
                    }
                    if (autoCreditStatus != "") {
                        document.getElementById('consigneeStatus').innerHTML = autoCreditStatus;
                    }
                }
                if (notifyPartyNumber != '') {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "validateCustomer",
                            param1: notifyPartyNumber,
                            param2: "I"
                        },
                        async: false,
                        success: function(data) {
                            if (data != null && data != '') {
                                var chargecode = data.split("==");
                                var autosHold = data.split("===");
                                var crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                                var autoCredit = autosHold[1].substring();
                                if (crditWarning == "In Good Standing ") {
                                    result = "CREDIT: " + crditWarning;
                                    document.getElementById('notifyPartyWarning').className = "creditStyle";
                                } else {
                                    result = "WARNING: " + crditWarning;
                                    document.getElementById('notifyPartyWarning').className = "warningStyle";
                                }
                                if (autoCredit.indexOf("HHG/PE/AUTOS CREDIT") > -1) {
                                    autoCreditStatus = "HHG/PE/AUTOS CREDIT";
                                    document.getElementById('notifyPartyStatus').className = "creditStyle";
                                } else {
                                    autoCreditStatus = "NO CREDIT FOR HHG/PE/AUTOS";
                                    document.getElementById('notifyPartyStatus').className = "warningStyle";
                                }
                            }
                        }
                    });
                    if (result != "") {
                        document.getElementById('notifyPartyWarning').innerHTML = result;
                    }
                    if (autoCreditStatus != "") {
                        document.getElementById('notifyPartyStatus').innerHTML = autoCreditStatus;
                    }
                }

            }
        }
    } else {
        if (document.fclBillLaddingform.billToCode[0].checked) {
            customerNumber = document.fclBillLaddingform.forwardingAgent1.value;//forwardingAgent1
        } else if (document.fclBillLaddingform.billToCode[1].checked) {
            //            document.getElementById("editHouseShipperDiv").style.display="none";
            if (importFlagForAgent == 'C') {
                customerNumber = document.fclBillLaddingform.houseShipper.value;
            } else {
                customerNumber = document.fclBillLaddingform.shipper.value;//shipper
            }
        } else if (document.fclBillLaddingform.billToCode[2].checked) {
            customerNumber = document.fclBillLaddingform.billTrePty.value;//billTrePty
        } else if (document.fclBillLaddingform.billToCode[3].checked) {
            if (importFlagForAgent == 'C') {
                customerNumber = document.fclBillLaddingform.consignee.value;
            } else {
                customerNumber = document.fclBillLaddingform.agentNo.value;
            }
        }
        else if (document.fclBillLaddingform.billToCode[4].checked) {
            customerNumber = document.fclBillLaddingform.notifyParty.value;
        }
        creditStatusBillTo(customerNumber);
        jQuery("#creditStatusInfo").hide();
    }

}
function confirmFunction() {
    document.getElementById('AlertBoxOk').style.display = 'none';
    grayOut(false, '');
    document.getElementById("save").disabled = true;
    makePageEditableWhileSaving(document.getElementById("fclbl"));
    document.fclBillLaddingform.action.value = "printBookingReport";
    document.fclBillLaddingform.buttonValue.value = "update";
    document.fclBillLaddingform.submit();
}
function updateDomesticRouting() {
    var domesticRoute = "";
    var issuingTerm = "";
    var codeNo = "";
    var code = "";
    var codeVal = "";
    var temp;
    var temp1;
    if (document.fclBillLaddingform.billingTerminal.value != null) {
        issuingTerm = document.fclBillLaddingform.billingTerminal.value;
        domesticRoute += "Issued By ";
        domesticRoute += "(";
        temp = issuingTerm.split(",");
        if (temp != null && temp.length == 2) {
            for (var i = 0; i < temp.length; i++) {
                if (i == 0) {
                    codeVal = temp[i];
                } else {
                    var codeandNo = temp[i];
                    temp1 = codeandNo.split("-");
                    for (var j = 0; j < temp1.length; j++) {
                        if (j == 0) {
                            code = temp1[j];
                        } else {
                            codeNo = temp1[j];
                        }
                    }
                }
            }
        }
        domesticRoute += codeNo;
        domesticRoute += ") ";
        domesticRoute += codeVal;
        domesticRoute += " -";
        domesticRoute += code;
    }
    document.fclBillLaddingform.domesticRouting.value = domesticRoute;
}
function limitTextarea(textarea, maxLines, maxChar) {
    var lines = textarea.value.replace(/\r/g, '').split('\n'),
    lines_removed,
    char_removed,
    i;
    if (maxLines && lines.length > maxLines) {
        alertNew('You can not enter\nmore than ' + maxLines + ' lines');
        lines = lines.slice(0, maxLines);
        lines_removed = 1
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
function displayHeading() {
    if (document.fclBillLaddingform.confirmOnBoard[0].checked) {
        document.getElementById("cal4").style.visibility = "visible";
        document.getElementById("txtcal4").style.borderLeft = "red 2px solid";
        document.getElementById("confOnBoardComments").disabled = false;
        document.getElementById("verifiedEtaCheck").disabled = false;
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            if (imgs[k].name == "containerEnableIcon" || imgs[k].name == "containerDisableIcon") {
                imgs[k].style.visibility = "hidden";
            }
        }
    } else {
        document.getElementById("cal4").style.visibility = "hidden";
        document.fclBillLaddingform.verifyEta.value = "";
        document.getElementById("txtcal4").style.borderLeft = "#c4c5c4 1px solid";
        document.getElementById("confOnBoardComments").value = "";
        document.getElementById("confOnBoardComments").disabled = true;
        document.getElementById("verifiedEtaCheck").disabled = true;
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            if (imgs[k].name == "containerEnableIcon" || imgs[k].name == "containerDisableIcon") {
                imgs[k].style.visibility = "visible";
            }
        }

    }
}
function disableEditShipperCheck() {
    if (document.getElementById('editHouseShipperCheck') && document.getElementById('editHouseShipperCheck').checked) {
        document.getElementById("accountName").value = "";
        document.getElementById("shipper").value = "";
        document.getElementById("streamShip").value = "";
        document.getElementById('editHouseShipperCheck').checked = false;
    }
    document.getElementById("editHouseShipperDiv").style.display = "none";
}
function clearThirdParty() {
    document.getElementById("billThirdPartyName").value = "";
    document.getElementById("billThirdPartyName").disabled = true;
    document.getElementById("billThirdPartyName").style.backgroundColor = "#CCEBFF";
    document.getElementById("billThirdPartyName").style.border = 0;
    if (document.getElementById("contactNameButtonForT")) {
        document.getElementById("contactNameButtonForT").style.visibility = "hidden";
    }
    document.getElementById("billTrePty").value = "";
    document.getElementById("billTrePty").disabled = true;
    document.getElementById("billTrePty").style.backgroundColor = "#CCEBFF";
    document.getElementById("billTrePty").style.border = 0;
}
function addInsurance() {
    GB_show("Insurance Rate", "/logisoft/jsps/fclQuotes/calculateInsurance.jsp?costOfGoods=" + document.fclBillLaddingform.costOfGoods.value + "&insuranceAmount=" + document.fclBillLaddingform.insuranceRate.value, width = "200", height = "300");
}
function deleteInsurance() {
    confirmNew("Insurance Charges will be deleted and all the BL information will be saved. Are you sure?", "deleteInsurance");
}
function call() {
    var pageWidth1 = "100%";
    var cvr = document.getElementById("cover");
    if (document.body && (document.body.scrollWidth || document.body.scrollHeight)) {
        pageWidth1 = document.body.scrollWidth + "px";
    } else {
        if (document.body.offsetWidth) {
            pageWidth1 = document.body.offsetWidth + "px";
        }
    }
    cvr.style.width = pageWidth1;
    cvr.style.height = "100%";
    cvr.style.display = "block";
    document.getElementById("newProgressBar").style.display = "block";
}
function agentReadOnly() {
    if (document.getElementById("defaultAgentY").checked) {
        document.getElementById("agent").readOnly = true;
        document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
        document.getElementById("agentNo").readOnly = true;
        document.getElementById("agentNo").className = "textlabelsBoldForTextBoxDisabledLook";
    }
}
function clearValues() {
    Event.observe("agent", "blur", function(event) {
        var element = Event.element(event);
        if (element.value != $("agent_check").value) {
            element.value = '';
            $("agent").value = '';
            $("agentNo").value = '';
            $("routedByAgent").value = '';
        }
    });
    document.getElementById("agent").value = "";
    document.getElementById("agentNo").value = "";
    document.getElementById("routedByAgent").value = "";
    document.getElementById("agent").readOnly = false;
    document.getElementById("agent").tabIndex = 0;
    document.getElementById("agent").className = "textlabelsBoldForTextBox";
    document.getElementById("routedByAgent").readOnly = false;
    document.getElementById("routedByAgent").tabIndex = 0;
    if (document.fclBillLaddingform.routedAgentCheck.value == "yes" || document.fclBillLaddingform.routedAgentCheck.value == "no") {
        document.getElementById("routedByAgent").value = "";
        document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
        document.fclBillLaddingform.routedAgentCheck.value = "";
    }
    var code = "";
    var dest = document.getElementById("finalDestination").value;
    if (dest.lastIndexOf("(") != -1) {
        code = dest.substring(dest.lastIndexOf("(") + 1, dest.lastIndexOf(")"));
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "getDestCodeforHBL",
            param1: code
        },
        async: false,
        success: function(data) {
            if (data != null && data != '') {
                if (data == 'Y') {//pripaid
                    if (document.fclBillLaddingform.houseBL[1].checked == true) {
                        document.fclBillLaddingform.houseBL[0].checked = true;
                        document.fclBillLaddingform.houseBL[1].checked = false;
                        document.fclBillLaddingform.billToCode[0].checked = true;
                        document.fclBillLaddingform.billToCode[0].disabled = false;
                        document.fclBillLaddingform.billToCode[1].disabled = false;
                        document.fclBillLaddingform.billToCode[2].disabled = false;
                        document.fclBillLaddingform.billToCode[3].checked = false;
                    }
                }
            }
        }
    });
}
function checkImportManifest(importFlag) {
    var displayMessage = "";
    var forwrder = document.fclBillLaddingform.forwardingAgent1.value.trim();
    if (importFlag == 'true') {
        if (forwrder == "" && document.fclBillLaddingform.billToCode[0].checked) {
            displayMessage += "-->Please Enter Forwarder name <br>";
        }
        if (document.fclBillLaddingform.shipper.value == "") {
            displayMessage += "-->Please Enter House Shipper Name And Number <br>";
        }
        if (document.fclBillLaddingform.consignee.value == "") {
            displayMessage += "-->Please Enter House Consignee Name And Number  <br>";
        }
        if (document.fclBillLaddingform.houseName.value == "") {
            displayMessage += "-->Please Enter Master Shipper Name  <br>";
        }
        if (document.fclBillLaddingform.houseConsigneeName.value == "") {
            displayMessage += "-->Please Enter Master Consignee Name  <br>";
        }

        if (document.fclBillLaddingform.notifyPartyName.value == "" && document.fclBillLaddingform.notifyParty.value == "" && document.fclBillLaddingform.billToCode[4].checked) {
            displayMessage += "-->Please Enter House Notify Party Name And Number  <br>";
        } else if (document.fclBillLaddingform.notifyPartyName.value == "" && document.fclBillLaddingform.billToCode[4].checked) {
            displayMessage += "-->Please Enter House Notify Party Name   <br>";
        } else if (document.fclBillLaddingform.notifyParty.value == "" && document.fclBillLaddingform.billToCode[4].checked) {
            displayMessage += "-->Please Enter House Notify Party Number   <br>";
        }
    }
    if (displayMessage != "") {
        alertBoxNew(displayMessage);
        document.fclBillLaddingform.readyToPost.checked = false;
        return false;
    } else {
        return true;
    }
}
function checkVesselName() {
    if (document.getElementById('vesselname_check1')) {
        if (document.getElementById('vesselname_check1').checked) {
            document.getElementById('vesselname').style.position = "absolute";
            document.getElementById('vesselname').style.visibility = "hidden";
            document.getElementById('vesselname_checkn').style.position = "relative";
            document.getElementById('vesselname_checkn').style.visibility = "visible";
            document.getElementById('vesselname').value = "";
            document.getElementById('vessel').value = "";
        } else {
            document.getElementById('vesselname_checkn').style.position = "absolute";
            document.getElementById('vesselname_checkn').style.visibility = "hidden";
            document.getElementById('vesselname').style.position = "relative";
            document.getElementById('vesselname').style.visibility = "visible";
            document.getElementById('vesselname_checkn').value = "";
        }
    }
}
function showAdjustmentCommentDiv() {
    if (jQuery('#adjustmentAmount').val() == '') {
        document.getElementById('adjustmentChargeCommentsDiv').style.visibility = "hidden";
        document.getElementById('ApplyToAllDiv').style.visibility = "hidden";
        jQuery('#adjustmentChargeCommentsLabelDiv').css("display", "none");
    } else {
        document.getElementById('adjustmentChargeCommentsDiv').style.visibility = "visible";
        document.getElementById('ApplyToAllDiv').style.visibility = "visible";
        jQuery('#adjustmentChargeCommentsLabelDiv').css("display", "block");
    }
}
function reverseToQuotation(bolId) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForAssignedHazmats",
            param1: bolId
        },
        success: function(returnData) {
            if (returnData != null && returnData != "") {
                alertNew("Unassign all assigned hazmats");
                return;
            } else {
                    jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.logiware.dwr.FclDwr",
                        methodName: "checkCostIsPaid",
                        param1: bolId,
                        dataType: "json"
                    },
                    success: function(result) {
                        if (result === true) {
                            alertNew("Cannot reverse to Quote because some of the Costs are paid in Accounting.");
                            return;
                        } else {
                           confirmYesOrNo("Are you sure? You want to convert this BL to Quote ", "convertToQuote");
                           jQuery("#eventCode").val("100022"); //-- refer 'code' in 'genericcode_dup' table --//
                        }
                    }
                });
           }
        }
    });
}
var indexValue;
function deleteContainer(index, sizeLegend, bol, id, fileNo, manuallyAdded) {
    indexValue = index;
    confirmYesOrNo("Are you sure? You want to delete Container Details", "deleteContainer");
    document.fclBillLaddingform.trailerNoId.value = id;
    document.getElementById("idOfContainer").value = id;
    document.getElementById("sizeOfContainer").value = sizeLegend;
    document.getElementById("currentFileNo").value = fileNo;
    document.getElementById("bolId").value = bol;
    document.getElementById("manuallyAddedFlag").value = manuallyAdded;
}
function hideAlternatePack() {
    if (jQuery("#noOfPackAlternate").is(":checked")) {
        document.getElementById("noOfPackAlt").style.display = "block";
    } else {
        document.getElementById("noOfPackAlt").style.display = "none";
    }
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
        success: function(data) {
            if (data) {
                jQuery('#' + id).attr("src", rootPath + "/img/icons/e_contents_view1.gif");
            } else {
                jQuery('#' + id).attr("src", rootPath + "/img/icons/e_contents_view.gif");
            }
        }
    });
}
function openBlueScreenNotesInfo(path, customerNo, customerName) {
    GB_show("Notes", path + "/bluescreenCustomerNotes.do?methodName=displayNotes&customerNo=" + customerNo + "&customerName=" + customerName, 400, 1000);
}

function disableAddContainer() {
    if (document.fclBillLaddingform.houseBL) {
        if (document.fclBillLaddingform.houseBL[2].checked) {
            document.getElementById("add1").style.display = "none";
        } else {
            document.getElementById("add1").style.display = "block";
        }
    }
}

function creditStatusBillToBothImport() {
    showAlternateMask();
    jQuery("#creditStatus").center().show(500);
}

function hideCreditStatus() {
    jQuery("#creditStatus").hide(500);
    hideAlternateMask();
}

function creditStatusBillTo(customerNumber) {
    if (customerNumber != '') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "validateCustomer",
                param1: customerNumber,
                param2: "I"
            },
            async: false,
            success: function(data) {
                if (data != null && data != '') {
                    var chargecode = data.split("==");
                    var autosHold = data.split("===");
                    var crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                    var autoCredit = autosHold[1].substring();
                    if (crditWarning == "In Good Standing ") {
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
        if (result != "") {
            document.getElementById('warningMessage').innerHTML = result;
        }
        if (autoCreditStatus != "") {
            document.getElementById('autosCredit').innerHTML = autoCreditStatus;
        }
    }
}

function hideAndShowCredtiStatus(bill) {
    var arr = new Array();
    for (var j = 0; j < bill.length; j++) {
        if (bill[j].value.toLowerCase().match("forwarder")) {
            arr.push("forwarder");
        }
        if (bill[j].value.toLowerCase().match("shipper")) {
            arr.push("shipper");
        }
        if (bill[j].value.toLowerCase().match("thirdparty")) {
            arr.push("thirdparty");
        }
        if (bill[j].value.toLowerCase().match("consignee")) {
            arr.push("consignee");
        }
        if (bill[j].value.toLowerCase().match("notifyparty")) {
            arr.push("notifyparty");
        }
    }
    var removeDuplicate = arr.filter(function(elem, pos) {
        return arr.indexOf(elem) == pos;
    });
    if (removeDuplicate.length == 1) {
        jQuery("#creditStatusInfo").hide();
    }
    if (arr.indexOf("forwarder") == -1) {
        jQuery("#forwarderId").hide();
    }
    if (arr.indexOf("shipper") == -1) {
        jQuery("#shipperId").hide();
    }
    if (arr.indexOf("thirdparty") == -1) {
        jQuery("#thirdPartyId").hide();
    }
    if (arr.indexOf("consignee") == -1) {
        jQuery("#consigneeId").hide();
    }
    if (arr.indexOf("notifyparty") == -1) {
        jQuery("#notifyPartyId").hide();
    }
}
function updateDoorOrigin(elementId) {
    document.getElementById('doorOrigin_check').value = getValue(elementId);
    document.getElementById('doorOfOrigin').value = getValue(elementId)
    if (document.getElementById('fileType') && document.getElementById('fileType').value == 'I') {
        document.getElementById('routedAgentCheck').focus();
    } 
    enableDoorDelivery();
}
function hideOrgZip() {
    document.getElementById("originZip").value = "";
}
function enableDoorDelivery(){
    var importFlag = document.getElementById("importFlag").value;
    if (importFlag == "true") {
        var zip = document.getElementById("zip").value;
        if (zip !== null && zip !== undefined && zip !== "") {
            jQuery("#doorDelivery").show();
        }else{
            jQuery("#doorDelivery").hide();
        }
    }
}
function showDoorDeliveryInfo(path, bolId, fileNumber, popupName){
    var zip = document.getElementById("zip").value ;
    if (bolId !== null && bolId !== undefined && bolId !== "") {
        GB_show(popupName, path + "/fclDoorDeliveryInfo.do?methodName=display&bolId=" + bolId+ "&fileNumber=" + fileNumber + "&zip=" + zip, width = "300", height = "1100");
    }
    else {
        alertNew("Please save BL");
    }  
}
function deleteFclDoorDeliveryValues(zip, bol){
    if(zip ==="" || zip == null){
        jQuery.ajaxx({
            data: {
                className: "com.gp.cvst.logisoft.hibernate.dao.FclDoorDeliveryDAO",
                methodName: "deleteFclDoorDeliveryByBol",
                param1: bol
            },
            close: function (data) {
            }
        });
    }
}
function checkBrand(val, bolid, companyCode) {

    if (val === "Econo" && companyCode === '03') {

        checkpreviousBrandValueEco03(bolid, val);
    } else if (val === "OTI" && companyCode == '02') {
        checkpreviousBrandValueOti02(bolid, val);
    } else if (val === "Ecu Worldwide" && companyCode === '03') {

        checkpreviousBrandValueEcu03(bolid, val);
    } else if (val === "Ecu Worldwide" && companyCode === '02') {
        checkpreviousBrandValueEcu03(bolid, val);
    }
}
function showBrandValuesFromBooking(brand,importFlag,companyCode){
    
    if (brand === "Econo"){
        document.getElementById('brandEcono').checked = true;
        document.getElementById('brandEcuworldwide').checked = false;
    } else if(brand === "Ecu Worldwide"){
        document.getElementById('brandEcuworldwide').checked = true;
        document.getElementById('brandEcono').cheked == false
    } else if (brand === "OTI") {
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
function checkpreviousBrandValueEcu03(bolid, val) {

    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "checkBrandForBl",
            param1: bolid
        },
        success: function (data) {
            if (null !== data && data !== val) {
                
                confirmYesOrNo("Please note that the Brand is changing from " + data + " to " + val + "", "Ecu Worldwide/"+data);

            }
        }
    });

}
function checkpreviousBrandValueOti02(bolid, val) {

    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "checkBrandForBl",
            param1: bolid
        },
        success: function (data) {
            if (null !== data && data !== val) {
                
                confirmYesOrNo("Please note that the Brand is changing from " + data + " to " + val + "", "OTI/"+data);

            }
        }
    });

}
function checkpreviousBrandValueEco03(bolid, val) {

    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "checkBrandForBl",
            param1: bolid
        },
        success: function (data) {
            if (null !== data && data !== val) {
               
                confirmYesOrNo("Please note that the Brand is changing from " + data + " to " + val + "", "Econo/"+data);

            }
        }
    });

}
function checkAddChargeMappingWithGL(fileNumber, transactionType) {
    var flag = true;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkChargeMappingWithGL",
            param1: fileNumber,
            param2: transactionType,
            param3: 'true'
        },
        async: false,
        success: function (data) {
            if (data !== "") {
                alertNew("No gl account is mapped with these charge code.Please contact accounting -> <span style=color:red>" + data + "</span>.");
                flag = false;
            }
        }
    });
    return flag;
}
function showTransactions(path, importFlag, fileNo) {
    var url = ""
    if (null !== importFlag && importFlag === 'true') {
        url = path + "/fclBillLadding.do";
        url += "?buttonValue=showArTransactions";
    } else {
        url = path + "/fclBlNew.do";
        url += "?methodName=showArTransactions";
    }
    url += "&fileNo=" + fileNo;
    window.parent.showLightBox("Transactions", url, 400, 800);
}

