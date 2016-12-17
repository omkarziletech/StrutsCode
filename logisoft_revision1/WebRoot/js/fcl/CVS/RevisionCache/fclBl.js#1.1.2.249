var myArray = new Array();
var myArray1 = new Array();
function loadData() {
    for (var i = 0; i < document.fclBlForm.elements.length; i++) {
        if (document.fclBlForm.elements[i].type === "radio" || document.fclBlForm.elements[i].type === "text" || document.fclBlForm.elements[i].type === "checkbox" || document.fclBlForm.elements[i].type === "select-one" || document.fclBlForm.elements[i].type === "textarea") {
            if (document.fclBlForm.elements[i].type === "radio" || document.fclBlForm.elements[i].type === "checkbox") {
                if (document.fclBlForm.elements[i].name !== "autoDeductFFCom") {
                    myArray[i] = document.fclBlForm.elements[i].checked;
                }
            } else {
                myArray[i] = document.fclBlForm.elements[i].value;
            }
        }
    }
    insuranceAllowedForPort();
}
function changeScanButtonColor(masterStatus, documentName,docList,fileCount) {
     var fileNo;
     var receivedMaster;
    if (trim(masterStatus) !== "") {
        document.getElementById("masterDiv").childNodes[0].nodeValue = "Yes(" + masterStatus + ")";
        document.getElementById("masterScanStatus").value = masterStatus;
        jQuery('#receivedMaster').val('Yes');
      fileNo=document.getElementById('fileNo').value;
      receivedMaster= document.getElementById('receivedMaster').value;
     jQuery.ajaxx({
                    dataType : "json",
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.ScanDAO",
                        methodName: "updateMasterReceived",
                        param1: fileNo,
                        param2: receivedMaster,
                        dataType : "json"
                    },
	    async: false
             });
       } else if(documentName==="SS LINE MASTER BL" && fileCount===0){
        document.getElementById("masterDiv").childNodes[0].nodeValue = "No";
        jQuery('#receivedMaster').val('No');
       fileNo=document.getElementById('fileNo').value;
       receivedMaster= document.getElementById('receivedMaster').value;
     jQuery.ajaxx({
                    dataType : "json",
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.ScanDAO",
                        methodName: "updateMasterReceived",
                        param1: fileNo,
                        param2: receivedMaster,
                        dataType : "json"
                    },
	    async: false
           });
    }
    if(docList===0){
        document.getElementById("scanAttach").className = "buttonStyleNew";
        document.getElementById("scanAttachDown").className = "buttonStyleNew";
    } else {
        document.getElementById("scanAttach").className = "buttoncolor";
        document.getElementById("scanAttachDown").className = "buttoncolor";
    }
}
function disabledFClBL(view, readyToPost, blClosed, accessMode, allowRoutedAgent) {
    jQuery("#eventCode").val("");
    hideAlternatePort();
    hideHblPOL();
    hideHblPOD();
    hideHblFD();
    hideAlternatePack();
    hidehblPlaceReceiptOverride();
    if (readyToPost === 'M' || readyToPost === 'm') {
        hidebkgvesselvoy();
    } else {
        unHidebkgvesselvoy();
    }
    if (view === '3' || accessMode === '0') {
        window.parent.disableFieldsWhileLocking(document.getElementById("fclBl"));
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            imgs[k].style.display = "block";
        }
    } else {
        if (null !== blClosed && blClosed === 'Y') {
            disableOnBlClosed(document.getElementById("fclBl"));
            var imgs = document.getElementsByTagName("img");
            for (var k = 0; k < imgs.length; k++) {
                imgs[k].style.display = "none";
            }
        }
        else if (readyToPost === 'M' || readyToPost === 'm') {
            diableOnManifest(document.getElementById("fclBl"), allowRoutedAgent);
        }
    }
}
function disableOnBlClosed(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type === "text" || element.type === "textarea" || element.type === "select-one") {
            element.style.border = 0;
            if (element.type === "select-one") {
                element.disabled = true;
            }
            element.readOnly = true;
            element.tabIndex = -1;
            if (element.type === "text" || element.type === "textarea") {
                element.style.backgroundColor = "#CCEBFF";
            } else {
                element.className = "textlabelsBoldForTextBox";
            }
        } else if (element.type === "checkbox" || element.type === "radio") {
            if (element.type === "radio" && (
                    element.id === "agentsForCarrierYes" || element.id === "agentsForCarrierNo"
                    || element.id === "agentsForCarrierA" || element.id === "shipperLoadYes" || element.id === "shipperLoadNo"
                    || element.id === "printUnitYes" || element.id === "printUnitNo" || element.id === "printPointYes"
                    || element.id === "printPointNo" || element.id === "noOfPackYes" || element.id === "noOfPackNo" || element.id === "noOfPackAlternate"
                    || element.id === "totalContainersYes" || element.id === "totalContainersNo" || element.id === "proofYes"
                    || element.id === "proofNo" || element.id === "nonNegotiableYes" || element.id === "nonNegotiableNo"
                    || element.id === "importReleaseYes" || element.id === "preAlertYes" || element.id === "preAlertNo"
                    || element.id === "importReleaseNo" || element.id === "printRev" || element.id === "doorOriginAsPlor" || element.id === "doorOriginAsPlorHouse"
                    || element.id === "doorDestinationAsFinalDeliveryToMaster" || element.id === "collectThirdParty" || element.id === "omitTermAndPort" || element.id === "serviceContractNo"
                    || element.id === "doorDestinationAsFinalDeliveryToHouse" || element.id === "printAlternatePortYes"
                    || element.id === "printAlternatePortNo" || element.id === "printAlternatePortYes"
                    || element.id === "hblPOLOverrideYes" || element.id === "hblPOLOverrideNo"
                    || element.id === "hblPODOverrideYes" || element.id === "hblPODOverrideNo"
                    || element.id === "hblFDOverrideYes" || element.id === "hblFDOverrideNo"
                    || element.id === "trimTrailingZerosForQty" || element.id === "bundleIntoOfr"
                    || element.id === "certifiedTrueCopyYes" || element.id === "certifiedTrueCopyNo" || element.id === "ratedManifest" || element.id === "omit2LetterCountryCode"
                    || element.id === "dockReceiptYes" || element.id === "dockReceiptNo" || element.id === "hblPlaceReceiptOverrideYes" || element.id === "hblPlaceReceiptOverrideNo")) {
                element.disabled = false;
            } else {
                element.style.border = 0;
                element.disabled = true;
                element.className = "textlabelsBoldForTextBox";
            }
        } else if (element.type === "button") {
            if (element.value === "Save To Draft" || element.value === "Add" || element.value === "Restore"
                    || element.value === "Look Up" || element.value === "Preview Accruals" || element.value === "HAZ" || element.value === "PKGS") {
                element.style.visibility = "hidden";
            }
            if (element.id === "costadd" || element.id === "note") {
                element.style.visibility = "visible";
            }
        }
    }
    return false;
}
/*this method will call after manifest to make few fileds editable */
function diableOnManifest(form, allowRoutedAgent) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type === "button") {
            if (element.value === "Save To Draft" || element.value === "Add" || element.value === "Restore"
                    || element.value === "Look Up" || element.value === "Preview Accruals") {
                element.style.visibility = "hidden";
            }
            if (element.id === "costadd" || element.id === "note" || element.id === "note") {
                element.style.visibility = "visible";
            }
        }
        if (element.type === "text" || element.type === "textarea") {
            if (element.type === "text" && (element.id === "vesselname" || element.id === "voyage" || element.id === "newMasterBL"
                    || element.id === "voyageInternal" || element.id === "pointAndCountryOfOrigin"
                    || element.id === "importOriginalBL"
                    || element.id === "txtcal4" || element.id === "houseConsigneeName"
                    || element.id === "houseNotifyPartyName" || element.id === "houseName"
                    || element.id === "newMasterBL" || element.id === "consigneeName"
                    || element.id === "notifyPartyName" || element.id === "noOfPackAlter"
                    || element.id === "etaValue" || element.id === "importEtaValue" || element.id === "alternatePort"
                    || element.id === "hblPOL" || element.id === "hblPOD" || element.id === "hblFD" || element.id === "aesComment"
                    || element.id === "hblPlaceReceipt")) {
                element.style.border = "1px solid #C4C5C4";
                element.style.backgroundColor = "#FCFCFC";
                element.readOnly = false;
                element.tabIndex = 0;
            } else if (element.type === "text" && (element.id === "paidStatus")) {
                element.style.backgroundColor = "#FCFCFC";
                element.tabIndex = -1;
            } else {
                if (element.type === "textarea" && (element.id === "domesticRouting" || element.id === "exportReference"
                        || element.id === "onwardInlandRouting" || element.id === "houseShipper1" || element.id === "houseConsignee1"
                        || element.id === "houseNotifyPartyaddress" || element.id === "confOnBoardComments" || element.id === "streamShip" || element.id === "forwardingAgentno"
                        || element.id === "streamShipConsignee" || element.id === "streamshipNotifyParty" || element.id === "importCommentsId"
                        || element.id === "internalRemark" || element.id === "aesComment")) {
                    element.style.border = "1px solid #C4C5C4";
                    element.style.backgroundColor = "#FCFCFC";
                    element.readOnly = false;
                    element.tabIndex = 0;
                } else {
                    if (allowRoutedAgent === "true" && element.id === "routedByAgent") {
                        element.style.border = "1px solid #C4C5C4";
                        element.style.backgroundColor = "#FCFCFC";
                        element.readOnly = false;
                        element.tabIndex = 0;
                    } else {
                        element.style.backgroundColor = "#CCEBFF";
                        element.style.border = 0;
                        element.readOnly = true;
                        element.tabIndex = -1;
                        element.className = "textlabelsBoldForTextBox capitalize";

                    }
                }
            }
            //This field in AES details tab
            if (element.type === "text" && (element.id === "houseName" || element.id === "houseConsigneeName" || element.id === "accountName" || element.id === "consigneeName" || element.id === "aesComment")) {
                element.style.borderLeft = "red 2px solid";
            }
            //---this is for display fields in Charges tab------
            if (element.type === "text" && (element.id === "disAgent" || element.id === "disAgentNo" || element.id === "disRoutedAgent"
                    || element.id === "disRoutedAgentNo" || element.id === "disOrigin" || element.id === "disDestination"
                    || element.id === "disCommodityCode" || element.id === "disCarrier" || element.id === "disIssuingTerminal")) {
                element.style.border = 0;
                element.readOnly = true;
                element.tabIndex = -1;
                element.style.backgroundColor = "#FCFCFC";
                element.className = "displayWithBackGroundColorWhite";
            }
            //---for blue color display---
            if (element.type === "text" && (element.id === "disShipper" || element.id === "disForwarder"
                    || element.id === "disThirdParty" || element.id === "disAgentName")) {
                element.style.border = 0;
                element.readOnly = true;
                element.tabIndex = -1;
                element.style.backgroundColor = "#FCFCFC";
                element.className = "displayBlueWithBackGroundColorWhite";
            }
        }
        if (element.type === "checkbox" || element.type === "radio") {
            if (element.type === "radio" && (element.id === "importFreightYes" || element.id === "importFreightNo" || element.id === "originalBlYes"
                    || element.id === "originalBlNo" || element.id === "agentsForCarrierYes" || element.id === "agentsForCarrierNo"
                    || element.id === "agentsForCarrierA" || element.id === "shipperLoadYes" || element.id === "shipperLoadNo"
                    || element.id === "printUnitYes" || element.id === "printUnitNo" || element.id === "printPointYes"
                    || element.id === "printPointNo" || element.id === "noOfPackYes" || element.id === "noOfPackNo" || element.id === "noOfPackAlternate"
                    || element.id === "totalContainersYes" || element.id === "totalContainersNo" || element.id === "proofYes"
                    || element.id === "proofNo" || element.id === "nonNegotiableYes" || element.id === "nonNegotiableNo"
                    || element.id === "destBlPrepaid" || element.id === "destBlCollect" || element.id === "importReleaseYes"
                    || element.id === "importReleaseNo" || element.id === "printRev" || element.id === "doorOriginAsPlorHouse" || element.id === "doorOriginAsPlor" || element.id === "omitTermAndPort" || element.id === "serviceContractNo"
                    || element.id === "doorDestinationAsFinalDeliveryToMaster" || element.id === "collectThirdParty" || element.id === "printAlternatePortYes"
                    || element.id === "doorDestinationAsFinalDeliveryToHouse" || element.id === "paymentReleaseYes" || element.id === "printAlternatePortNo"
                    || element.id === "hblPOLOverrideYes" || element.id === "hblPOLOverrideNo"
                    || element.id === "hblPODOverrideYes" || element.id === "hblPODOverrideNo"
                    || element.id === "hblFDOverrideYes" || element.id === "hblFDOverrideNo" || element.id === "certifiedTrueCopyYes" || element.id === "certifiedTrueCopyNo"
                    || element.id === "paymentReleaseNo" || element.id === "preAlertYes" || element.id === "preAlertNo" || element.id === "trimTrailingZerosForQty"
                    || element.id === "bundleIntoOfr" || element.id === "ratedManifest" || element.id === "omit2LetterCountryCode" || element.id === "dockReceiptYes" || element.id === "dockReceiptNo"
                    || element.id === "hblPlaceReceiptOverrideYes"|| element.id === "hblPlaceReceiptOverrideNo" || element.id === "hblPlaceReceipt"
                    || element.id === "brandEcono" || element.id === "brandEcuworldwide" || element.id === "brandOti")) {
                element.disabled = false;
            } else {
                if (element.type === "checkbox" && (element.id === "ediCheckBox" || element.id === "AESCheckBox"
                        || element.id === "newMasterBLCheckBox" || element.id === "masterConsigneeCheck" || element.id === "editHouseNotifyCheck"
                        || element.id === "masterNotifyCheck" || element.id === "consigneeCheck" || element.id === "editHouseConsigneeCheck"
                        || element.id === "notifyCheck" || element.id === "ediShipperCheck"
                        || element.id === "ediConsigneeCheck" || element.id === "ediNotifyPartyCheck")) {
                    element.disabled = false;
                }
                else {
                    element.tabIndex = -1;
                    element.disabled = true;
                }
            }
        }
        if (element.type === "select-one") {
            if (allowRoutedAgent === "true" && element.id === "routedAgentCheck") {
                element.disabled = false;
                jQuery('#routedAgentCheck').addClass("dropdown_accounting");
            } else {
                element.style.border = 0;
                element.disabled = true;
            }
        }
        var imgs = document.getElementsByTagName("img");//imgs[k].id === "chargescollapse"
        for (var k = 0; k < imgs.length; k++) {
            if (imgs[k].name === "containerEnableIcon" || imgs[k].name === "containerDisableIcon"
                    || imgs[k].id === "toggle" || imgs[k].id === "addNewTP"
                    || imgs[k].id === "cal313" || imgs[k].id === "cal71"
                    || imgs[k].id === "etdCal"
                    || imgs[k].id === "etaCal" || imgs[k].id === "chargesDeleteCollapse"
                    || imgs[k].id === "caldateOutYard" || imgs[k].id === "caldateIntoYard") {
                imgs[k].style.display = "none";
            }
        }
    }
}

function setTabName(tabName) {
    if (undefined !== tabName && null !== tabName && 'null' !== tabName) {
        jQuery("#selectedTab").val(tabName);
    }
    if (null !== document.getElementById("collapsetable")) {
        document.getElementById("collapsetable").style.display = "block";
    }
    if (null !== document.getElementById("chargestable")) {
        document.getElementById("chargestable").style.display = "none";
    }
    if (null !== document.getElementById("expandcosttable")) {
        document.getElementById("expandcosttable").style.display = "none";
    }
    if (null !== document.getElementById("collapseCosttable")) {
        document.getElementById("collapseCosttable").style.display = "block";
    }
}
function setFocusOnTab(selectedTab) {
    jQuery(function() {
        var $tabs = jQuery('#fclBLContainer').tabs();
        if (selectedTab === 'aes') {
            $tabs.tabs('select', 6);
        } else if (selectedTab === 'print') {
            $tabs.tabs('select', 5);
        } else if (selectedTab === 'charges') {
            $tabs.tabs('select', 4);
        } else if (selectedTab === 'container') {
            $tabs.tabs('select', 3);
        } else if (selectedTab === 'general') {
            $tabs.tabs('select', 2);
        } else if (selectedTab === 'shipperForwarder') {
            $tabs.tabs('select', 1);
        } else {
            $tabs.tabs('select', 0);
        }
    });
}
function getSslBlCollect(roleDuty) {   
    var code = "";
    var ready = jQuery("#manifest").val();
    var dest = jQuery("#finalDestination").val();
    var fileNumber = jQuery("#fileNumber").val();
    if (dest.lastIndexOf("(") !== -1) {
        code = dest.substring(dest.lastIndexOf("(") + 1, dest.lastIndexOf(")"));
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "getDestCode",
            param1: code
        },
        async: false,
        success: function(data) {
            if (data !== null && data !== '') {
                if (data === 'X') {
                    if (!jQuery("#sslBlPrepaid").is(":checked") && !jQuery("#sslBlCollect").is(":checked")) {
                        jQuery("#sslBlPrepaid").attr("checked", false);
                        jQuery("#sslBlCollect").attr("checked", true);
                        jQuery("#sslBlPrepaid").attr("disabled", true);
                        if (roleDuty) {
                            jQuery("#sslBlPrepaid").attr("disabled", false);
                        }
                        jQuery("#sslBlCollect").attr("disabled", false);
                        if (ready === 'M' || ready === 'm') {
                            jQuery("#sslBlCollect").attr("disabled", true);
                            jQuery("#sslBlPrepaid").attr("disabled", true);
                        }
//                    } else if (!roleDuty) {
//                        jQuery("#sslBlPrepaid").attr("disabled", true);
//                        jQuery("#sslBlCollect").attr("disabled", true);
                    }else{
                        if (!roleDuty) {
                            jQuery("#sslBlPrepaid").attr("disabled", true);
                            jQuery("#sslBlCollect").attr("disabled", true);
                        } else {
                            jQuery("#sslBlPrepaid").attr("disabled", false);
                            jQuery("#sslBlCollect").attr("disabled", false);
                        }
		    }
                } else if (data === 'N') {
                    if (!jQuery("#sslBlPrepaid").is(":checked") && !jQuery("#sslBlCollect").is(":checked")) {
                        jQuery("#sslBlPrepaid").attr("checked", true);
                        jQuery("#sslBlCollect").attr("checked", false);
                        jQuery("#sslBlPrepaid").attr("disabled", false);
                        jQuery("#sslBlCollect").attr("disabled", true);
                        if (roleDuty) {
                            jQuery("#sslBlCollect").attr("disabled", false);
                        }
                        if (ready === 'M' || ready === 'm') {
                            jQuery("#sslBlCollect").attr("disabled", true);
                            jQuery("#sslBlPrepaid").attr("disabled", true);
                        }
//                    } else if (!roleDuty) {
//                        jQuery("#sslBlPrepaid").attr("disabled", true);
//                        jQuery("#sslBlCollect").attr("disabled", true);
                    }else{
			 if (!roleDuty) {
                            jQuery("#sslBlPrepaid").attr("disabled", true);
                            jQuery("#sslBlCollect").attr("disabled", true);
                        } else {
                            jQuery("#sslBlPrepaid").attr("disabled", false);
                            jQuery("#sslBlCollect").attr("disabled", false);
                        }
		    }
                } else if (data === 'Y') {
//		      if (!roleDuty) {
//			jQuery("#sslBlPrepaid").attr("disabled", true);
//			jQuery("#sslBlCollect").attr("disabled", true);
//		    }else{
			jQuery("#sslBlCollect").attr("disabled", false);
			jQuery("#sslBlPrepaid").attr("disabled", false);
//		    }
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                            methodName: "getPrepaidCollect",
                            param1: fileNumber
                        },async: false,
                        success: function(data) {
                            if (!jQuery("#sslBlPrepaid").is(":checked") && !jQuery("#sslBlCollect").is(":checked")) {
                                if (data === 'true') {
                                    jQuery("#sslBlPrepaid").attr("checked", true);
                                } else {
                                    jQuery("#sslBlCollect").attr("checked", true);
                                }
                            }
                        }
                    });
                }
                if (jQuery("#sslBlPrepaid").is(":checked")) {
                    jQuery("#sslPrepaidCollectValue").val(jQuery("#sslBlPrepaid").val());
                } else {
                    jQuery("#sslPrepaidCollectValue").val(jQuery("#sslBlCollect").val());
                }
            } else {
                if (!jQuery("#sslBlPrepaid").is(":checked") && !jQuery("#sslBlCollect").is(":checked")) {
                    jQuery("#sslBlCollect").attr("checked", true);
                }
            }
        }
    });
}
function saveBlOnNavigate() {
    var bol = jQuery("#bol").val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.dwr.DwrUtil",
            methodName: "setFileList",
            param1: bol,
            request: "true"
        },
        async: false,
        success: function(data) {
            if (null !== data && data !== '') {
            }
        }
    });
}
function getBillTOCode() {//HOUSE bl GO COLLECT
    var code = "";
    var ready = jQuery("#manifest").val();
    var dest = jQuery("#finalDestination").val();
    var bol = jQuery("#bol").val();
    if (dest.lastIndexOf("(") !== -1) {
        code = dest.substring(dest.lastIndexOf("(") + 1, dest.lastIndexOf(")"));
    }
    if (ready !== 'M' && ready !== 'm') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "getDestCodeforHBL",
                param1: code
            },
            async: false,
            success: function(data) {
                if (data !== null && data !== '') {
                    if (data === 'N') {//pripaid and both
                        jQuery("#houseBlP").attr("disabled", false);
                        jQuery("#houseBlC").attr("checked", false);
                        jQuery("#houseBlC").attr("disabled", true);
                        jQuery("#houseBlB").attr("disabled", false);
                        jQuery("#billToCodeF").attr("disabled", false);
                        jQuery("#billToCodeS").attr("disabled", false);
                        jQuery("#billToCodeT").attr("disabled", false);
                        jQuery("#billToCodeA").attr("disabled", false);
                        jQuery("#billToCodeA").attr("checked", false);
                        jQuery("#billToCodeA").attr("disabled", true);
                        if ((jQuery("#houseBlP").is(":checked")) || (!jQuery("#houseBlP").is(":checked") && !jQuery("#houseBlC").is(":checked")
                                && !jQuery("#houseBlB").is(":checked") && !jQuery("#billToCodeF").is(":checked")
                                && !jQuery("#billToCodeS").is(":checked") && !jQuery("#billToCodeT").is(":checked") && !jQuery("#billToCodeA").is(":checked"))) {
                            if (!jQuery("#houseBlP").is(":checked") && !jQuery("#houseBlC").is(":checked")
                                    && !jQuery("#houseBlB").is(":checked") && !jQuery("#billToCodeF").is(":checked")
                                    && !jQuery("#billToCodeS").is(":checked") && !jQuery("#billToCodeT").is(":checked")
                                    && !jQuery("#billToCodeA").is(":checked")) {
                                jQuery("#houseBlP").attr("checked", true);
                                jQuery("#billToCodeF").attr("checked", true);
                                jQuery.ajaxx({
                                    data: {
                                        className: "com.logiware.dwr.FclDwr",
                                        methodName: "setBillToParty",
                                        forward: "/jsps/FCL/FclBlCharges.jsp",
                                        param1: bol,
                                        param2: "Shipper",
                                        param3: jQuery("#houseBL").val()
                                    },
                                    success: function(data) {
                                        loadCostAndCharges(data);
                                    }
                                });
                                alertNew("FCL House B/L Go Collect has changed No ,so all collect charges will be changed as Prepaid(Shipper)");
                            }
                        }
                    } else if (data === 'Y') {//pripaid collect and both
                        jQuery("#houseBlP").attr("disabled", false);
                        jQuery("#houseBlC").attr("disabled", false);
                        jQuery("#houseBlB").attr("disabled", false);
                        if ((jQuery("defaultAgentN").is(":checked")) && jQuery("#agent").val() === '' &&
                                jQuery("#houseBlC").is(":checked")) {
                            jQuery("#houseBlB").attr("checked", true);
                            jQuery("#billToCodeA").attr("checked", false);
                            jQuery("#billToCodeA").attr("disabled", true);
                        }
                    }
                    jQuery("#fclSsblGoCollect").val(data);
                }
            }
        });
    }
}
function disableBillToCodeonLoad() {
    if (jQuery("#houseBlC").is(":checked")) {
        jQuery("#billToCodeF").attr("disabled", true);
        jQuery("#billToCodeS").attr("disabled", true);
        jQuery("#billToCodeT").attr("disabled", true);
        //        setBillToCodeForPreviousValue();
        if (jQuery("#billToCodeA").is(":checked")) {
            document.getElementById("consigneeCheck").style.visibility = "visible";
        }
    } else {
        if (jQuery("#houseBlB").is(":checked")) {
            jQuery("#billToCodeF").attr("checked", false);
            jQuery("#billToCodeS").attr("checked", false);
            jQuery("#billToCodeT").attr("checked", false);
            jQuery("#billToCodeA").attr("checked", false);
            jQuery("#billToCodeF").attr("disabled", true);
            jQuery("#billToCodeS").attr("disabled", true);
            jQuery("#billToCodeT").attr("disabled", true);
            jQuery("#billToCodeA").attr("disabled", true);
        } else {
            jQuery("#billToCodeF").attr("disabled", false);
            jQuery("#billToCodeS").attr("disabled", false);
            jQuery("#billToCodeT").attr("disabled", false);
            jQuery("#billToCodeA").attr("disabled", false);
            jQuery("#billToCodeA").attr("checked", false);
            jQuery("#billToCodeA").attr("disabled", true);
            if (!jQuery("#billToCodeF").is(":checked") && !jQuery("#billToCodeS").is(":checked") && !jQuery("#billToCodeT").is(":checked")) {
                jQuery("#billToCodeS").attr("checked", true);
            }
        }
    }
    if (!jQuery("#billToCodeT").is(":checked") && !jQuery("#houseBlB").is(":checked")) {
        document.getElementById("billThirdPartyName").readOnly = true;
        document.getElementById("billThirdPartyName").tabIndex = -1;
        document.getElementById("billThirdPartyName").style.border = 0;
        document.getElementById("billThirdPartyName").style.backgroundColor = "#CCEBFF";
        if (document.getElementById("contactNameButtonForT")) {
            document.getElementById("contactNameButtonForT").style.visibility = "hidden";
        }
        document.getElementById("billTrePty").readOnly = true;
        document.getElementById("billTrePty").tabIndex = -1;
        document.getElementById("billTrePty").style.border = 0;
        document.getElementById("billTrePty").style.backgroundColor = "#CCEBFF";
    }
}
function makeButtonRedColorForPkgs() {
    var bol = jQuery("#bol").val();
    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "showFclBlContainer",
            forward: "/jsps/FCL/fclBlContainer.jsp",
            param1: bol
        },
        success: function(data) {
            if (data) {
                jQuery("#containerDetails").html(data);
            }
        }
    });
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
    hideAgentCheck();
}
function coverPage() {
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
function backToSearch() {
    if (jQuery("#readyToPost").attr('checked')) {
        var displayMessage = "";
        if (jQuery("#houseName").val() === "" || jQuery("#houseShipper").val() === "") {
            displayMessage += "-->Please Enter Master Shipper Name And Number  <br>";
            jQuery("#houseName").css("border-color", "red");
        }
        if (jQuery("#masterConsigneeCheck").is(":checked")) {
            if (jQuery("#houseConsigneeName").val() === "") {
                displayMessage += "-->Please Enter Master Consignee Name  <br>";
            }
        } else if (jQuery("#houseConsigneeName").val() === "" || jQuery("#houseConsignee").val() === "") {
            displayMessage += "-->Please Enter Master Consignee Name And Number  <br>";
            jQuery("#houseConsigneeName").css("border-color", "red");
        }
        if (jQuery("#consigneeCheck").is(":checked")) {
            if (jQuery("#consigneeName").val() === "") {
                displayMessage += "-->Please Enter House Consignee Name  <br>";
            }
        } else if (jQuery("#consigneeName").val() === "" || jQuery("#consignee").val() === "") {
            displayMessage += "-->Please Enter House Consignee Name And Number  <br>";
            jQuery("#consigneeName").css("border-color", "red");
            jQuery("#consignee").css("border-color", "red");
        }
        if (displayMessage !== "") {
            alertNew(displayMessage);
            return;
        }
    }
    var result = false;
    for (var i = 0; i < document.fclBlForm.elements.length; i++) {
        if (document.fclBlForm.elements[i].type === "radio" || document.fclBlForm.elements[i].type === "text" || document.fclBlForm.elements[i].type === "checkbox" || document.fclBlForm.elements[i].type === "select-one" || document.fclBlForm.elements[i].type === "textarea") {
            if (document.fclBlForm.elements[i].type === "radio" || document.fclBlForm.elements[i].type === "checkbox") {
                if (document.fclBlForm.elements[i].name !== "autoDeductFFCom") {
                    myArray1[i] = document.fclBlForm.elements[i].checked;
                }
            } else {
                myArray1[i] = document.fclBlForm.elements[i].value;
            }
        }
    }
    for (var j = 0; j < myArray1.length; j++) {
        if (myArray[j] !== myArray1[j]) {
            result = true;
        }
    }
    if (result === true) {
        document.getElementById("confirmNo").style.width = "88" + "px";
        document.getElementById("confirmNo").value = "Exit without Save";
        confirmYesNoCancel("Do you want to save the BL changes?", "goBack");
    } else {
        setAction("goToSearch");
    }
}
function reverseBlToBooking() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForAssignedHazmats",
            param1: jQuery("#bol").val()
        },
        success: function(data) {
            if (data !== null && data !== "") {
                alertNew("Unassign all assigned hazmats");
                return;
            } else {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.logiware.dwr.FclDwr",
                        methodName: "checkCostIsPaid",
                        param1: jQuery("#bol").val(),
                        dataType: "json"
                    },
                    success: function(result) {
                        if (result === true) {
                            alertNew("Cannot reverse to Booking because some of the Costs are paid in Accounting.");
                            return;
                        } else {
                            confirmYesOrNo("Are you sure? You want to reverse this BL to Booking", "reverseToBooking");
                        }
                    }
                });
            }
        }
    });
}
var customer = "";
function doManifest() {
    var fileNo = jQuery("#fileNo").val();
    var displayMessage = "";
    if (jQuery('#billToCodeA').attr('checked') && jQuery("#agent").val() === '') {
        displayMessage += "-->Please Select Agent name<br>";
    }
    if (jQuery.trim(jQuery("#routedAgentCheck").val()) === ''
            && jQuery.trim(jQuery("#ratesNonRates").val()) === '') {
        if (!jQuery("#ratesNonRates").is(":checked")) {
            displayMessage += "-->Please Select ERT<br>";
        }
    }
    if (jQuery("#houseName").val() === "" || jQuery("#houseShipper").val() === "") {
        displayMessage += "-->Please Enter Master Shipper Name And Number  <br>";
        jQuery("#houseName").css("border-color", "red");
    }
    if (jQuery("#masterConsigneeCheck").is(":checked")) {
        if (jQuery("#houseConsigneeName").val() === "") {
            displayMessage += "-->Please Enter Master Consignee Name  <br>";
        }
    } else if (jQuery("#houseConsigneeName").val() === "" || jQuery("#houseConsignee").val() === "") {
        displayMessage += "-->Please Enter Master Consignee Name And Number  <br>";
        jQuery("#houseConsigneeName").css("border-color", "red");
    }
    if (jQuery("#shipper").val() === "" || jQuery("#accountName").val() === "") {
        displayMessage += "-->Please Enter House Shipper Name And Number  <br>";
        jQuery("#accountName").css("border-color", "red");
    }
    if (jQuery("#consigneeCheck").is(":checked")) {
        if (jQuery("#consigneeName").val() === "") {
            displayMessage += "-->Please Enter House Consignee Name  <br>";
        }
    } else if (jQuery("#consigneeName").val() === "" || jQuery("#consignee").val() === "") {
        displayMessage += "-->Please Enter House Consignee Name And Number  <br>";
        jQuery("#consigneeName").css("border-color", "red");
    }
    if (jQuery("#forwardingAgentName").val() === "" || jQuery("#forwardingAgent1").val() === "") {
        displayMessage += "-->Please Enter FF Name And Number  <br>";
    }
    if (displayMessage !== '') {
        alertNew(displayMessage);
        return false;
    }
    if (jQuery("#billToCodeF").is(":checked")) {
        customer = jQuery("#forwardingAgent1").val();
    } else if (jQuery("#billToCodeS").is(":checked")) {
        customer = jQuery("#shipper").val();
    }
    else if (jQuery("#billToCodeT").is(":checked")) {
        customer = jQuery("#billTrePty").val();
    } else if (jQuery("#billToCodeA").is(":checked")) {
        customer = jQuery("#agentNo").val();
    } else {
        var bill = document.fclBlForm.billTo;
        var billTo = "";
        for (var i = 0; i < document.fclBlForm.billTo.length; i++) {
            if (bill[i].value.toLowerCase() === "forwarder") {
                if (billTo.indexOf("Forwarder") === -1) {
                    if (billTo !== "") {
                        billTo = billTo + ",Forwarder";
                    } else {
                        billTo = "Forwarder";
                    }
                    if (customer !== "") {
                        customer = customer + "," + jQuery("#forwardingAgent1").val();
                    } else {
                        customer = jQuery("#forwardingAgent1").val();
                    }
                }
            } else if (bill[i].value.toLowerCase() === "shipper") {
                if (billTo.indexOf("Shipper") === -1) {
                    if (billTo !== "") {
                        billTo = billTo + ",Shipper";
                    } else {
                        billTo = "Shipper";
                    }
                    if (customer !== "") {
                        customer = customer + "," + jQuery("#shipper").val();
                    } else {
                        customer = jQuery("#shipper").val();
                    }
                }
            } else if (bill[i].value.toLowerCase() === "thirdparty") {
                if (billTo.indexOf("ThirdParty") === -1) {
                    if (billTo !== "") {
                        billTo = billTo + ",ThirdParty";
                    } else {
                        billTo = "ThirdParty";
                    }
                    if (customer !== "") {
                        customer = customer + "," + jQuery("#billTrePty").val();
                    } else {
                        customer = jQuery("#billTrePty").val();
                    }
                }
            } else if (bill[i].value.toLowerCase() === "agent") {
                if (billTo.indexOf("Agent") === -1) {
                    if (billTo !== "") {
                        billTo = billTo + ",Agent";
                    } else {
                        billTo = "Agent";
                    }
                    if (customer !== "") {
                        customer = customer + "," + jQuery("#agentNo").val();
                    } else {
                        customer = jQuery("#agentNo").val();
                    }
                }
            }
        }
    }
    var containsJapan = jQuery("#finalDestination").val();
    var contains = (containsJapan.indexOf('JAPAN') > 1);
    if (contains) {
        reminderBox("Japan AFR must be filed on all Japan shipments");
    } else {
        if (checkAddChargeMappingWithGL(fileNo, "AR") && checkAddChargeMappingWithGL(fileNo, "AC")) {
               manifestExport(customer);
        }
    }
}
function manifestExport(customerAccountNo) {
    if (jQuery("#readyToPost").attr('checked')) {
        if (jQuery('#portofladding').val() === '') {
            alertNew("Please Enter Port of loading");
        } else {
            jQuery.ajaxx({
                data: {
                    className: "com.logiware.dwr.FclDwr",
                    methodName: "validateMasterAccount",
                    param1: customerAccountNo
                },
                success: function(data) {
                    if (data !== '') {
                        alertNew(data + " cannot be billed as it is a Master Account");
                    } else {
                        jQuery.ajaxx({
                            data: {
                                className: "com.logiware.dwr.FclDwr",
                                methodName: "validateCustomerOnManifest",
                                param1: jQuery("#bol").val()
                            },
                            success: function(data) {
                                if (null !== data && data !== '') {
                                    alertNew(data);
                                } else {
                                    confirmNew("Do you want to Manifest this BL ?", "readyToPost");
                                }
                            }
                        });
                    }
                }
            });
        }
    } else {
        alertNew("This BL is not ready to Manifest");
    }
}
function doUnManifest() {
    var closedBy = jQuery('#blClosedBy').val();
    if (closedBy !== '') {
        alertNew("Please Open BL..");
    } else {
        confirmNew("Do you want to Unmanifest BL Y/N", "unManifest");
    }
}
function scanOrAttach() {
    var fileNo = jQuery("#fileNo").val();
    var importFlag = jQuery("#importFlag").val();
    if (null !== fileNo && fileNo !== '') {
        var screenName = "FCLFILE";
        if (null !== importFlag && importFlag === 'I') {
            screenName = "IMPORT FILE";
            importFlag = "true";
        }
        var master = jQuery("#newMasterBL").val();
        GB_show("Scan", rootPath + "/scan.do?screenName=" + screenName + "&documentId=" + fileNo + "&ssMasterBl=" + master, 420, 1200);

    } else {
        alertNew("Please save the file before Scan/Attach");
    }
}
function validateDate(data) {
    if (data.value !== "") {
        data.value = data.value.getValidDateTime("/", "", false);
        if (data.value === "" || data.value.length > 10) {
            alertNew("Please enter valid date");
            data.value = "";
            document.getElementById(data.id).focus();
        }
    }
}
function confirmOnBoard() {
    showPopUp();
    document.getElementById('confirmOnBoard').style.display = 'block';
    if (jQuery("#etaValue").val() === jQuery("#txtcal4").val()) {
        jQuery('#verifiedEtaCheck').attr('checked', true);
    }
}
function printFaxEmail() {
    var result = false;
    var displayMessage = "";
    var fileNo = jQuery("#fileNo").val();
    var bol = jQuery("#bol").val();
    var bolId = jQuery("#bolId").val();
    var dest = jQuery("#finalDestination").val();
    var importFlag = jQuery("#importFlag").val();
    var issuetrm = jQuery("#billingTerminal").val();
    if (null !== fileNo && fileNo !== '') {
        if (jQuery.trim(jQuery("#vesselname").val()) === '') {
            displayMessage += "-->Please Enter Vessel Name <br>";
        }
        if (jQuery.trim(jQuery("#voyage").val()) === '') {
            displayMessage += "-->Please Enter SS Voyage <br>";
        }
        if (jQuery.trim(jQuery("#txtcal313").val()) === '' || jQuery.trim(jQuery("#txtcal71").val()) === '') {
            displayMessage += "-->Please Enter Container Cut Off and Doc Cut Off <br>";
        }
        if (jQuery.trim(jQuery("#txtetdCal").val()) === '' || jQuery.trim(jQuery("#txtetaCal").val()) === '') {
            displayMessage += "-->Please Enter ETA and ETD <br>";
        }
        if (importFlag === 'I' && jQuery.trim(jQuery("#importAMSHosueBlNumber").val()) === '') {
            displayMessage += "-->Please Enter AMS House BL# <br>";
        }
        if (jQuery.trim(jQuery("#portofladding").val()) === '') {
            displayMessage += "-->Please Enter Port of loading <br>";
        }
        if (jQuery.trim(jQuery("#routedAgentCheck").val()) === ''
                && jQuery.trim(jQuery("#ratesNonRates").val()) === '') {
            if (!jQuery("#ratesNonRates").is(":checked")) {
                displayMessage += "-->Please Select ERT<br>";
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
                if (data !== null && undefined !== data && data !== "") {
                    displayMessage += data;
                }
            }
        });
        if (displayMessage !== '') {
            alertBoxNew(displayMessage);
        } else {
            for (var i = 0; i < document.fclBlForm.elements.length; i++) {
                if (document.fclBlForm.elements[i].type === "radio" || document.fclBlForm.elements[i].type === "text" || document.fclBlForm.elements[i].type === "checkbox" || document.fclBlForm.elements[i].type === "select-one" || document.fclBlForm.elements[i].type === "textarea") {
                    if (document.fclBlForm.elements[i].type === "radio" || document.fclBlForm.elements[i].type === "checkbox") {
                        if (document.fclBlForm.elements[i].name !== "autoDeductFFCom") {
                            myArray1[i] = document.fclBlForm.elements[i].checked;
                        }
                    } else {
                        myArray1[i] = document.fclBlForm.elements[i].value;
                    }
                }
            }
            for (var j = 0; j < myArray1.length; j++) {
                if (myArray[j] !== myArray1[j]) {
                    result = true;
                }
            }
            if (result === true) {
                jQuery("#action").val("printBlReport");
                setAction("saveFclBl");
            } else {
                var path = rootPath + "/printConfig.do?screenName=BL&fileNo=" + fileNo + "&blId=" + bol + "&bolNo=" + bolId + "&destination=" + dest + "&filesToPrint=all&billingTerminal=" + issuetrm + '&importFlag=' + importFlag + '&subject=FCL Booking-';
                GB_show('Print/Fax/Email FCL BL  FileNumber ' + fileNo, path, 400, 1000);
            }
        }
    } else {
        alertNew("Please save the file before Print/Fax/Email");
    }
}
function openHazmatPopUp(index, id, trailerNo) {
    return GB_show('HazMat', '<%=path%>/fCLHazMat.do?buttonValue=fclbl&indexValue=' + index +
            '&containerId=' + id + '&bolid=' + bol + '&name=FclBl' + '&fileNo=' + fileNo + '&unitNo=' + trailerNo + '&manifest=' + ready, 500, 1000);
}
function closeConfirmOnBoard() {
    closePopUp();
    document.getElementById('confirmOnBoard').style.display = 'none';
}
function checkConfirmOnBoard() {
    if (jQuery('#confirmOnBoardYes').attr('checked')) {
        document.getElementById("cal4").style.visibility = "visible";
        document.getElementById("txtcal4").style.borderLeft = "red 2px solid";
        document.getElementById("confOnBoardComments").disabled = false;
        document.getElementById("verifiedEtaCheck").disabled = false;
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            if (imgs[k].name === "containerEnableIcon" || imgs[k].name === "containerDisableIcon") {
                imgs[k].style.visibility = "hidden";
            }
        }
    } else {
        document.getElementById("cal4").style.visibility = "hidden";
        document.getElementById("txtcal4").value = "";
        document.getElementById("txtcal4").style.borderLeft = "#c4c5c4 1px solid";
        document.getElementById("confOnBoardComments").value = "";
        document.getElementById("confOnBoardComments").disabled = true;
        jQuery('#verifiedEtaCheck').attr('checked', false);
        document.getElementById("verifiedEtaCheck").disabled = true;
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            if (imgs[k].name === "containerEnableIcon" || imgs[k].name === "containerDisableIcon") {
                imgs[k].style.visibility = "visible";
            }
        }

    }
}
function copyVerifyEtaDate() {
    if (jQuery('#verifiedEtaCheck').attr('checked')) {
        jQuery('#txtcal4').val(document.getElementById("etaValue").value);
    } else {
        jQuery('#txtcal4').val("");
    }
}
function saveConfirmOnBoard(user) {
    var verifyDate = jQuery('#txtcal4').val();
    var bol = jQuery("#bol").val();
    var confirmOnBoard = "";
    if (jQuery('#confirmOnBoardYes').attr('checked')) {
        confirmOnBoard = "Y";
    }
    else {
        confirmOnBoard = "N";
    }
    if (jQuery('#confirmOnBoardYes').attr('checked') && jQuery('#txtcal4').val() === "") {
        alertNew("Please enter Verified ETA");
        return;
    }
    var comments = jQuery('#confOnBoardComments').val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "updateFclBlForConfirmOnBoard",
            param1: bol,
            param2: verifyDate,
            param3: confirmOnBoard,
            param4: user,
            param5: comments,
            param6: "",
            request: "true"
        },
        async: false,
        success: function(data) {
            closePopUp();
            document.getElementById("confirmOnBoard").style.display = "none";
            if (jQuery('#confirmOnBoardYes').attr('checked')) {
                jQuery("#confirmBy").val(user);
                jQuery("#confirmOn").val(data);
                jQuery("#action").val("printComfirmOnBoardReport");
                jQuery('#confirmOnBoardButton').addClass("buttonStyle");
                setAction("saveFclBl");
            }
            else {
                jQuery("#confirmBy").val("");
                jQuery("#confirmOn").val("");
                jQuery('#confirmOnBoardButton').addClass("buttonStyleNew");
                setAction("saveFclBl");
            }
        }
    });
}
function confirmFunction() {
    document.getElementById('AlertBoxOk').style.display = 'none';
    grayOut(false, '');
    var fileNo = jQuery("#fileNo").val();
    var bol = jQuery("#bol").val();
    var bolId = jQuery("#bolId").val();
    var dest = jQuery("#finalDestination").val();
    var issuetrm = jQuery("#billingTerminal").val();
    var result = false;
    for (var i = 0; i < document.fclBlForm.elements.length; i++) {
        if (document.fclBlForm.elements[i].type === "radio" || document.fclBlForm.elements[i].type === "text" || document.fclBlForm.elements[i].type === "checkbox" || document.fclBlForm.elements[i].type === "select-one" || document.fclBlForm.elements[i].type === "textarea") {
            if (document.fclBlForm.elements[i].type === "radio" || document.fclBlForm.elements[i].type === "checkbox") {
                if (document.fclBlForm.elements[i].name !== "autoDeductFFCom") {
                    myArray1[i] = document.fclBlForm.elements[i].checked;
                }
            } else {
                myArray1[i] = document.fclBlForm.elements[i].value;
            }
        }
    }
    for (var j = 0; j < myArray1.length; j++) {
        if (myArray[j] !== myArray1[j]) {
            result = true;
        }
    }
    if (result === true) {
        jQuery("#action").val("printBookingReport");
        setAction("saveFclBl");
    } else {
        var importFlag = jQuery("#importFlag").val() === "I" ? true : false;
        var path = "/logisoft/printConfig.do?screenName=BL&fileNo=" + fileNo + "&blId=" + bol + "&bolNo=" + bolId + "&destination=" + dest + "&filesToPrint=booking&billingTerminal=" + issuetrm + '&importFlag=' + importFlag + '&subject=FCL Booking-';
        GB_show('Print/Fax/Email FCL BL  FileNumber ' + fileNo, path, 400, 1000);
    }
}
function gotoMarksNumbers(unitnumber, id, bol, fileNo, ready) {
    var importFlag = jQuery("#importFlag").val() === "I" ? true : false;
    GB_show('Description of Packages and Goods', rootPath + '/fclmarksnumber.do?button=NewFCLBL&containerId=' + id + '&bolid=' + bol + '&fileNo=' + fileNo + '&unitNo=' + unitnumber + '&manifest=' + ready + '&importFlag=' + importFlag, 460, 1200);
}
var isContainerChanged = false;
function unitUpdate() {
    isContainerChanged = true;
}
function getUpdatedContainerDetails(bol) {
    GB_hide();
    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "showFclBlContainer",
            forward: "/jsps/FCL/fclBlContainer.jsp",
            param1: bol
        },
        async: false,
        success: function(data) {
            if (data) {
                jQuery("#containerDetails").html(data);
            }
        }
    });
    var fileNum = jQuery("#fileNo").val();
    var isMultiFile = (fileNum.indexOf('-') > 1);
    if (isContainerChanged && !isMultiFile) {
        getUpdatedChargesDetailsAfterContainerUpdation();
    }
}
function loadCostAndCharges(data) {
    jQuery("#costCharges").html(data);
}
function getUpdatedChargesDetails() {
    GB_hide();
    var bol = jQuery("#bol").val();
    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "showFclBlCharges",
            forward: "/jsps/FCL/FclBlCharges.jsp",
            param1: bol
        },
        success: function(data) {
            if (data) {
                jQuery("#costCharges").html(data);
            }
        }
    });
}
function getUpdatedChargesDetailsAfterCorrection() {
    var bol = jQuery("#bol").val();
    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "showFclBlCharges",
            forward: "/jsps/FCL/FclBlCharges.jsp",
            param1: bol
        },
        success: function(data) {
            if (data) {
                jQuery("#costCharges").html(data);
            }
        }
    });
}
function getUpdatedChargesDetailsAfterContainerUpdation() {
    var bol = jQuery("#bol").val();
    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "updateFclBlChargesAfterContainerUpdation",
            param1: bol,
            request: "true"
        },
        success: function(data) {
            if (data === "true") {
                jQuery.ajaxx({
                    data: {
                        className: "com.logiware.dwr.FclDwr",
                        methodName: "postAccrualsAfterContainerUpdate",
                        param1: bol,
                        request: "true"
                    },
                    success: function() {
                        jQuery.ajaxx({
                            data: {
                                className: "com.logiware.dwr.FclDwr",
                                methodName: "showFclBlCharges",
                                forward: "/jsps/FCL/FclBlCharges.jsp",
                                param1: bol
                            },
                            success: function(data) {
                                if (data) {
                                    jQuery("#costCharges").html(data);
                                }
                            }
                        });
                    }
                });
            }
        }
    });
}
function refreshPage() {
    GB_hide();
    var bol = jQuery("#bol").val();
    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "showAesFiling",
            forward: "/jsps/FCL/SedFiling.jsp",
            param1: bol
        },
        success: function(data) {
            if (data) {
                jQuery("#aesFiling").html(data);
            }
        }
    });
}
var hasCost = true;
function showCostCodes() {
    if (hasCost === true) {
        document.getElementById("collapseCosttable").style.display = "none";
        document.getElementById("expandcosttable").style.display = "block";
        hasCost = false;
    } else {
        document.getElementById("expandcosttable").style.display = "none";
        document.getElementById("collapseCosttable").style.display = "block";
        hasCost = true;
    }
}
function togglePreviewAccruals() {
    if (hasTransactionType == true) {
        document.getElementById("accrualsTables").style.display = "block";
        document.getElementById("previewAccruals").style.display = "none";
    } else {
        document.getElementById("accrualsTables").style.display = "none";
        document.getElementById("previewAccruals").style.display = "block";
        document.getElementById("previewAccruals").value = "Preview Accruals";
    }
}
function showAccruals() {
    if (document.getElementById("accrualsTables").style.display === "none") {
        document.getElementById("accrualsTables").style.display = "block";
        if (document.getElementById("totalCostForDown")) {
            document.getElementById("totalCostForDown").style.display = "block";
        }
        document.getElementById("totalCostFor").style.display = "none";
        document.getElementById("previewAccruals").value = "Close Accruals";
    } else {
        document.getElementById("accrualsTables").style.display = "none";
        if (document.getElementById("totalCostForDown")) {
            document.getElementById("totalCostForDown").style.display = "none";
        }
        document.getElementById("totalCostFor").style.display = "block";
        document.getElementById("previewAccruals").value = "Preview Accruals";
    }
}
function addNewBlCharges(chargesListSize) {
    var importFlag = jQuery("#importFlag").val();
    if (chargesListSize < 12) {
        var fileNo = jQuery("#fileNo").val();
        var bol = jQuery("#bol").val();
        if (null !== fileNo && fileNo !== '') {
            var noFFComm = "false";
            if (jQuery("#houseBlB").is(":checked") && (jQuery("#forwardingAgentName").val() === 'NO FF ASSIGNED' ||
                    jQuery("#forwardingAgentName").val() === 'NO FF ASSIGNED / B/L PROVIDED' ||
                    jQuery("#forwardingAgentName").val() === 'NO FRT. FORWARDER ASSIGNED')) {
                noFFComm = "true";
            }
            GB_show('Charges', '/logisoft/jsps/FCL/AddFclBlCharges.jsp?button=addNewCharges&noFFComm=' + noFFComm + '&bolId=' + bol + '&collexpand=' + 'collapse&importFlag=' + importFlag, 350, 900);
        }
        else {
            alertNew("Please save the file before Scan/Attach");
        }
    } else {
        alertNew("Already reached maximum number of charges");
    }
}
function editcharges(chargesId, val, amount, adjestmentAmount, manualCharge, oldAmount) {
    var bol = jQuery("#bol").val();
    var importFlag = false;
    var noFFComm = "false";
    if (jQuery("#houseBlB").is(":checked") && (jQuery("#forwardingAgentName").val() === 'NO FF ASSIGNED' ||
            jQuery("#forwardingAgentName").val() === 'NO FF ASSIGNED / B/L PROVIDED' ||
            jQuery("#forwardingAgentName").val() === 'NO FRT. FORWARDER ASSIGNED')) {
        noFFComm = "true";
    }
    GB_show('Charges', '/logisoft/fclBillLadding.do?button=editBlCharges&noFFComm=' + noFFComm + '&adjestmentAmount=' + adjestmentAmount + '&rollUpAmount=' + amount +
            '&bolId=' + bol + '&chargesId=' + chargesId + '&collexpand=' + val + '&manualCharge=' + manualCharge + '&oldAmount=' + oldAmount + '&importFlag=' + importFlag, 350, 1000);
}
function editCosts(costId, amount, chargeCodeStatus) {
    var bol = jQuery("#bol").val();
    var masterbl = jQuery("#newMasterBL").val();
    GB_show('Edit Accruals', '/logisoft/fclBillLadding.do?button=editBlCost&rollUpAmount=' + amount + '&bolId=' + bol + '&costId=' + costId + '&chargeCodeStatus=' + chargeCodeStatus + '&masterBl=', 350, 1000);
}
function addNewCosts() { //costCodeAdd()
    var bol = jQuery("#bol").val();
    var importFlag = jQuery("#importFlag").val();
    var masterbl = jQuery("#newMasterBL").val();
    var fileNo = jQuery("#fileNo").val();
    GB_show('Add Accruals', '/logisoft/jsps/FCL/AddFclBlCosts.jsp?bolId=' + bol + '&masterBl=' + masterbl + '&importFlag=' + importFlag +'&fileNo=' +fileNo, 350, 1000);
}
var faeCommisionSetupOrAdvSurCharge = "";
function checkHouseBlCode(houseBl) {
    jQuery("#houseBL").val(houseBl);
    var bol = jQuery("#bol").val();
    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "IsFaeCommisionSetupOrAdvSurCharge",
            param1: bol,
            param2: "no"
        },
        async: false,
        success: function(data) {
            if (data !== '') {
                faeCommisionSetupOrAdvSurCharge = data.toString();
            }
        }
    });
    if (jQuery("#houseBlC").is(":checked")) {
        jQuery('#billToCodeA').attr('checked', true);
        if (jQuery("#agent").val() === "") {
            alertNew('Please select Agent Name And Number  from TradeRoute Tab');
            var code = "";
            var dest = jQuery("#finalDestination").val();
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
                success: function(data) {
                    if (data !== null && data !== '') {
                        if (data === 'Y') {
                            jQuery('#houseBlP').attr('checked', true);
                            jQuery('#billToCodeF').attr('checked', true);
                            jQuery('#billToCodeF').attr('disabled', false);
                            jQuery('#billToCodeS').attr('disabled', false);
                            jQuery('#billToCodeT').attr('disabled', false);
                            setBillToCodeForPreviousValue();
                            document.getElementById('billThirdPartyName').value = '';
                            document.getElementById('billTrePty').value = '';
                            document.getElementById("billThirdPartyName").tabIndex = -1;
                            document.getElementById("billThirdPartyName").readOnly = true;
                            document.getElementById("billThirdPartyName").style.border = 0;
                            document.getElementById("billThirdPartyName").style.backgroundColor = "#CCEBFF";
                            if (document.getElementById("contactNameButtonForT")) {
                                document.getElementById("contactNameButtonForT").style.visibility = "hidden";
                            }
                            document.getElementById("billTrePty").readOnly = true;
                            document.getElementById("billTrePty").style.border = 0;
                            document.getElementById("billTrePty").style.backgroundColor = "#CCEBFF";
                            jQuery('#billToCodeA').attr('checked', false);
                            jQuery.ajaxx({
                                data: {
                                    className: "com.logiware.dwr.FclDwr",
                                    methodName: "setBillToParty",
                                    forward: "/jsps/FCL/FclBlCharges.jsp",
                                    param1: bol,
                                    param2: "Shipper",
                                    param3: "P"
                                },
                                success: function(data) {
                                    loadCostAndCharges(data);
                                }
                            });
                        }
                    }
                }
            });
            return;
        }
        jQuery('#billToCodeF').attr('disabled', true);
        jQuery('#billToCodeS').attr('disabled', true);
        jQuery('#billToCodeT').attr('disabled', true);
        jQuery('#billToCodeA').attr('disabled', false);
        confirmYesOrNo("Please note that all Bill to Code will be changed for all Charges - Yes to continue and Cancel to abort operation", "fromPrepaidAgent");
    } else if (jQuery("#houseBlB").is(":checked")) {
        confirmYesOrNo("Are you sure want to change the House BL Prepaid/Collect to Both - Yes to continue and No to abort operation", "fromPrepaidOrCollect");
    } else {
        jQuery('#billToCodeF').attr('disabled', false);
        jQuery('#billToCodeS').attr('disabled', false);
        jQuery('#billToCodeT').attr('disabled', false);
        jQuery('#billToCodeA').attr('disabled', false);
        jQuery('#billToCodeA').attr('checked', false);
        jQuery('#billToCodeA').attr('disabled', true);
        if (!jQuery("#billToCodeF").is(":checked") && !jQuery("#billToCodeS").is(":checked")
                && !jQuery("#billToCodeT").is(":checked")) {
            if (faeCommisionSetupOrAdvSurCharge == 'INCENT,ADVSHP,ADVFF' || faeCommisionSetupOrAdvSurCharge == 'INCENT,ADVSHP' || faeCommisionSetupOrAdvSurCharge == 'INCENT,ADVFF') {
                confirmYesOrNo("--->Please note that all Bill to Code will be changed for all Charges <br> --->INCENT chargecode will be deleted <br> ---> All Advance Surcharge will be deleted <br> - Yes to continue and No to abort operation", "fromCollect");
            } else if (faeCommisionSetupOrAdvSurCharge == 'INCENT') {
                confirmYesOrNo("--->Please note that all Bill to Code will be changed for all Charges <br> --->INCENT chargecode will be deleted <br> - Yes to continue and No to abort operation", "fromCollect");
            } else if (faeCommisionSetupOrAdvSurCharge == 'ADVSHP,ADVFF' || faeCommisionSetupOrAdvSurCharge == 'ADVSHP' || faeCommisionSetupOrAdvSurCharge == 'ADVFF') {
                confirmYesOrNo("--->Please note that all Bill to Code will be changed for all Charges <br> ---> All Advance Surcharge will be deleted <br> - Yes to continue and No to abort operation", "fromCollect");
            } else {
                confirmYesOrNo("Please note that all Bill to Code will be changed for all Charges - Yes to continue and No to abort operation", "fromCollect");
            }

        }
    }
    modifyAccountDetails();
}
function changeSchedButtonColor(schedSize, index) {
    if (schedSize === '0') {
        document.getElementById("schedB" + index).className = "buttonStyleNew";
    } else {
        document.getElementById("schedB" + index).className = "buttonColor";
    }
    GB_hide();
}
function hideArInvoice(fileNo) {
    var voyageInternal =jQuery("#voyageInternal").val() !==null && jQuery("#voyageInternal").val() !== undefined ? jQuery("#voyageInternal").val() : "";
    GB_hide();
    makeARInvoiceButtonGreen(fileNo,voyageInternal);

}
function addAesDetails(bol) {
    var sslBookingValues = document.getElementsByName('fclBl.bookingNo').item(0).value;
    GB_showFullScreen("Sed Filing", "/logisoft/sedFiling.do?buttonValue=goToSedFiling&bol=" + bol + "&bkgnum=" + sslBookingValues);
}
function addSchedBDetails(index, trnref) {
    GB_showFullScreen("Sched B Details", "/logisoft/sedFiling.do?buttonValue=goToSchedB&index=" + index + "&trnref=" + trnref);
}
function makeARInvoiceButtonGreen(fileNo,voyageInternal) {
    if (null !== fileNo && fileNo !== "") {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getARInvoiceForThisBL",
                param1: fileNo,
                param2: voyageInternal
            },
            success: function(data) {
                if (document.getElementById("arInvoice")) {
                    if (data === "ARINVOICE") {
                        document.getElementById("arInvoice").className = "buttoncolor";
                        document.getElementById("arInvoiceDown").className = "buttoncolor";
                    } else {
                        document.getElementById("arInvoice").className = "buttonStyleNew";
                        document.getElementById("arInvoiceDown").className = "buttonStyleNew";
                    }
                }
            }
        });
    }
}
function makeInbondButtonGreen() {
    var bol = jQuery("#bol").val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "getInbondForThisBL",
            param1: bol
        },
        success: function(data) {
            if (document.getElementById("inbond")) {
                if (data === "INBOND") {
                    document.getElementById("inbond").className = "buttoncolor";
                    document.getElementById("inbondDown").className = "buttoncolor";
                } else {
                    document.getElementById("inbond").className = "buttonStyleNew";
                    document.getElementById("inbondDown").className = "buttonStyleNew";
                }
            }
        }
    });
}
function modifyAccountDetails() {
    var customerNumber = '';
    var forwarderNumber = '';
    var shipperNumber = '';
    var thirdPartyNumber = '';
    var agentNumber = '';
    var result = "";
    var autoCreditStatus = "";
    if (jQuery("#billToCodeF").is(":checked")) {
        customerNumber = jQuery("#forwardingAgent1").val();//forwardingAgent1
    } else if (jQuery("#billToCodeS").is(":checked")) {
        customerNumber = jQuery("#shipper").val();//shipper
    } else if (jQuery("#billToCodeT").is(":checked")) {
        customerNumber = jQuery("#billTrePty").val();//billTrePty
    } else if (jQuery("#billToCodeA").is(":checked")) {
        customerNumber = jQuery('#agentNo').val();
    } else if (jQuery("#houseBlB").is(":checked")) {
        var bill = document.fclBlForm.billTo;
        hideAndShowCredtiStatus(bill);
        for (var i = 0; i < document.fclBlForm.billTo.length; i++) {
            if (bill[i].value.toLowerCase() === "forwarder") {
                forwarderNumber = jQuery("#forwardingAgent1").val();
            }
            if (bill[i].value.toLowerCase() === "shipper") {
                shipperNumber = jQuery("#shipper").val();
            }
            if (bill[i].value.toLowerCase() === "thirdparty") {
                thirdPartyNumber = jQuery("#billTrePty").val();
            }
            if (bill[i].value.toLowerCase() === "agent") {
                agentNumber = jQuery('#agentNo').val();
            }
            if (forwarderNumber !== '') {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                        methodName: "validateCustomer",
                        param1: forwarderNumber,
                        param2: "E"
                    },
                    async: false,
                    success: function(data) {
                        if (data !== null && data !== '') {
                            var chargecode = data.split("===");
                            var autosHold = data.split("===");
                            var crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                            var autoCredit = autosHold[1].substring();
                            if (crditWarning === "In Good Standing ") {
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
                            if (result !== "") {
                                document.getElementById('forwarderWarning').innerHTML = result;
                            }
                            if (autoCreditStatus !== "") {
                                document.getElementById('forwarderStatus').innerHTML = autoCreditStatus;
                            }
                        }
                    }
                });
            }
            if (shipperNumber !== '') {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                        methodName: "validateCustomer",
                        param1: shipperNumber,
                        param2: "E"
                    },
                    async: false,
                    success: function(data) {
                        if (data !== null && data !== '') {
                            var chargecode = data.split("===");
                            var autosHold = data.split("===");
                            var crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                            var autoCredit = autosHold[1].substring();
                            if (crditWarning === "In Good Standing ") {
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
                            if (result !== "") {
                                document.getElementById('shipperWarning').innerHTML = result;
                            }
                            if (autoCreditStatus !== "") {
                                document.getElementById('shipperStatus').innerHTML = autoCreditStatus;
                            }
                        }
                    }
                });
            }
            if (thirdPartyNumber !== '') {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                        methodName: "validateCustomer",
                        param1: thirdPartyNumber,
                        param2: "E"
                    },
                    async: false,
                    success: function(data) {
                        if (data !== null && data !== '') {
                            var chargecode = data.split("===");
                            var autosHold = data.split("===");
                            var crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                            var autoCredit = autosHold[1].substring();
                            if (crditWarning === "In Good Standing ") {
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
                            if (result !== "") {
                                document.getElementById('thirdpartyWarning').innerHTML = result;
                            }
                            if (autoCreditStatus !== "") {
                                document.getElementById('thirdpartyStatus').innerHTML = autoCreditStatus;
                            }
                        }
                    }
                });
            }
            if (agentNumber !== '') {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                        methodName: "validateCustomer",
                        param1: agentNumber,
                        param2: "E"
                    },
                    async: false,
                    success: function(data) {
                        if (data !== null && data !== '') {
                            var chargecode = data.split("===");
                            var autosHold = data.split("===");
                            var crditWarning = chargecode[0].substring(3, chargecode[0].indexOf("For the Party "));
                            var autoCredit = autosHold[1].substring();
                            if (crditWarning === "In Good Standing ") {
                                result = "CREDIT: " + crditWarning;
                                document.getElementById('agentWarning').className = "creditStyle";
                            } else {
                                result = "WARNING: " + crditWarning;
                                document.getElementById('agentWarning').className = "warningStyle";
                            }
                            if (autoCredit.indexOf("HHG/PE/AUTOS CREDIT") > -1) {
                                autoCreditStatus = "HHG/PE/AUTOS CREDIT";
                                document.getElementById('agentStatus').className = "creditStyle";
                            } else {
                                autoCreditStatus = "NO CREDIT FOR HHG/PE/AUTOS";
                                document.getElementById('agentStatus').className = "warningStyle";
                            }
                            if (result !== "") {
                                document.getElementById('agentWarning').innerHTML = result;
                            }
                            if (autoCreditStatus !== "") {
                                document.getElementById('agentStatus').innerHTML = autoCreditStatus;
                            }
                        }
                    }
                });
            }
        }
        if (shipperNumber !== '') {
            creditStatusBillTo(shipperNumber);
        } else if (forwarderNumber !== '') {
            creditStatusBillTo(forwarderNumber);
        } else if (thirdPartyNumber !== '') {
            creditStatusBillTo(thirdPartyNumber);
        } else if (agentNumber !== '') {
            creditStatusBillTo(agentNumber);
        }
    }
    if (document.getElementById("houseBlC").checked || document.getElementById("houseBlP").checked) {
        jQuery("#creditStatusInfo").hide();
        creditStatusBillTo(customerNumber);
    }
}
function setBillToCodeForPreviousValue() {
    if (jQuery("#houseBlC").is(":checked")) {
        jQuery('#billToCodeA').attr('checked', false);
        billtoBasedonParty();
    } else if (jQuery("#houseBlB").is(":checked")) {
        return;
    } else if (document.getElementsByTagName('billTo')) {
        billtoBasedonParty();
    } else {
        jQuery('#billToCodeS').attr('checked', true);
    }
    modifyAccountDetails();
}
function billtoBasedonParty() {
    var bill = document.fclBlForm.billTo;
    for (var i = 0; i < document.fclBlForm.billTo.length; i++) {
        if (bill[i].value.toLowerCase() === "forwarder") {
            jQuery('#billToCodeF').attr('checked', true);
            if (!jQuery("#houseBlB").is(":checked")) {
                jQuery('#houseBlP').attr('checked', true);
            }
            break;
        } else if (bill[i].value.toLowerCase() === "shipper") {
            jQuery('#billToCodeS').attr('checked', true);
            if (!jQuery("#houseBlB").is(":checked")) {
                jQuery('#houseBlP').attr('checked', true);
            }
            break;
        }
        else if (bill[i].value.toLowerCase() === "thirdParty") {
            jQuery('#billToCodeT').attr('checked', true);
            if (!jQuery("#houseBlB").is(":checked")) {
                jQuery('#houseBlP').attr('checked', true);
            }
            break;
        } else if (bill[i].value.toLowerCase() === "agent") {
            jQuery('#billToCodeA').attr('checked', true);
            if (!jQuery("#houseBlB").is(":checked")) {
                jQuery('#houseBlC').attr('checked', true);
            }
            break;
        }
    }//for
}
function checkForCustomersPresence() {
    var bol = jQuery("#bol").val();
    if (jQuery('#accountName').val() !== "") {
        jQuery('#houseBlP').attr('checked', true);
        jQuery('#billToCodeS').attr('checked', true);
        document.getElementById('billThirdPartyName').value = '';
        document.getElementById('billTrePty').value = '';
        document.getElementById("billThirdPartyName").tabIndex = -1;
        document.getElementById("billThirdPartyName").readOnly = true;
        document.getElementById("billThirdPartyName").style.border = 0;
        document.getElementById("billThirdPartyName").style.backgroundColor = "#CCEBFF";
        if (document.getElementById("contactNameButtonForT")) {
            document.getElementById("contactNameButtonForT").style.visibility = "hidden";
        }
        document.getElementById("billTrePty").readOnly = true;
        document.getElementById("billTrePty").style.border = 0;
        document.getElementById("billTrePty").style.backgroundColor = "#CCEBFF";
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "setBillToParty",
                forward: "/jsps/FCL/FclBlCharges.jsp",
                param1: bol,
                param2: "Shipper",
                param3: "P"
            },
            success: function(data) {
                loadCostAndCharges(data);
            }
        });
    }
    else if (jQuery("#forwardingAgentName").val() !== ""
            && jQuery("#forwardingAgentName").val() !== 'NO FF ASSIGNED'
            && jQuery("#forwardingAgentName").val() !== 'NO FRT. FORWARDER ASSIGNED'
            && jQuery("#forwardingAgentName").val() !== 'NO FF ASSIGNED / B/L PROVIDED') {
        jQuery('#houseBlP').attr('checked', true);
        jQuery('#billToCodeF').attr('checked', true);
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "setBillToParty",
                forward: "/jsps/FCL/FclBlCharges.jsp",
                param1: bol,
                param2: "Forwarder",
                param3: "P"
            },
            success: function(data) {
                loadCostAndCharges(data);
            }
        });
    } else if (jQuery("#billThirdPartyName").val() !== "") {
        jQuery('#houseBlP').attr('checked', true);
        jQuery('#billToCodeT').attr('checked', true);
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "setBillToParty",
                forward: "/jsps/FCL/FclBlCharges.jsp",
                param1: bol,
                param2: "ThirdParty",
                param3: "P"
            },
            success: function(data) {
                loadCostAndCharges(data);
            }
        });
    } else {
        if (jQuery("#houseBlP").is(":checked")
                && (jQuery("#forwardingAgentName").val() === 'NO FF ASSIGNED'
                        || jQuery("#forwardingAgentName").val() === 'NO FRT. FORWARDER ASSIGNED'
                        || jQuery("#forwardingAgentName").val() === 'NO FF ASSIGNED / B/L PROVIDED')) {
            alertNew("Please change Bill To Party or change Forwarder, because Forwarder is NO FF ASSIGNED");
        } else {
            alertNew("Please select Shipper or Forwarder or ThirdParty to change from Collect to Prepaid");
        }
        jQuery('#houseBlC').attr('checked', true);
        jQuery('#billToCodeA').attr('checked', true);
        return;
    }
}
function clearBillThirdParty() {
    document.getElementById("billThirdPartyName").value = "";
    document.getElementById("billThirdPartyName").readOnly = true;
    document.getElementById("billThirdPartyName").tabIndex = -1;
    document.getElementById("billThirdPartyName").style.backgroundColor = "#CCEBFF";
    document.getElementById("billThirdPartyName").style.border = 0;
    if (document.getElementById("contactNameButtonForT")) {
        document.getElementById("contactNameButtonForT").style.visibility = "hidden";
    }
    document.getElementById("billTrePty").value = "";
    document.getElementById("billTrePty").readOnly = true;
    document.getElementById("billTrePty").style.backgroundColor = "#CCEBFF";
    document.getElementById("billTrePty").style.border = 0;
}

function activeBillThirdParty() {
    document.getElementById("consigneeCheck").style.visibility = "visible";
    document.getElementById("billThirdPartyName").readOnly = false;
    document.getElementById("billThirdPartyName").style.border = "1px solid #C4C5C4";
    document.getElementById("billThirdPartyName").style.backgroundColor = "#FCFCFC";
    if (document.getElementById("contactNameButtonForT")) {
        document.getElementById("contactNameButtonForT").style.visibility = "visible";
    }
    document.getElementById("billTrePty").readOnly = false;
    document.getElementById("billTrePty").style.border = "1px solid #C4C5C4";
    document.getElementById("billTrePty").style.backgroundColor = "#FCFCFC";
    document.getElementById("billTrePty").style.border = "1px solid #C4C5C4";
}
function checkBillToCode() {
    if (jQuery("#accountName").val() === "") {
        document.getElementById("billThirdPartyName").readOnly = true;
        if (document.getElementById("contactNameButtonForT")) {
            document.getElementById("contactNameButtonForT").style.visibility = 'hidden';
        }
        document.getElementById('billTrePty').readOnly = true;
        document.getElementById('toggle').style.visibility = "hidden";
    }
    if (jQuery('#billToCodeF').attr('checked')) {
        jQuery("#houseBL").val("P");
        jQuery('#houseBlP').attr('checked', true);
        if (jQuery("#forwardingAgentName").val() === "") {
            alertNew("Please select Forwarder Name and Number in ShipperForwarderConsigneeNotify Tab");
            setBillToCodeForPreviousValue();
            jQuery('#billToCodeF').attr('checked', false);
            return;
        }
        if (jQuery("#forwardingAgentName").val() === 'NO FF ASSIGNED' ||
                jQuery("#forwardingAgentName").val() === 'NO FF ASSIGNED / B/L PROVIDED' ||
                jQuery("#forwardingAgentName").val() === 'NO FRT. FORWARDER ASSIGNED') {
            alertNew("Please change Bill To Code or change Forwarder, because Forwarder is NO FF ASSIGNED");
            setBillToCodeForPreviousValue();
            jQuery('#billToCodeF').attr('checked', false);
            return;
        }
        confirmYesOrNo("Please note that all Bill to Code will be changed for all Charges - Yes to continue and Cancel to abort operation", "forwarder");
    } else if (jQuery('#billToCodeS').attr('checked')) {
        jQuery("#houseBL").val("P");
        jQuery('#houseBlP').attr('checked', true);
        if (jQuery("#accountName").val() === "") {
            alertNew("Please select  House B/L Shipper Name And Number from Shipper Forwarder Consignee Notify");
            setBillToCodeForPreviousValue();
            jQuery('#billToCodeS').attr('checked', false);
            return;
        }
        confirmYesOrNo("Please note that all Bill to Code will be changed for all Charges - Yes to continue and Cancel to abort operation", "shipper");
    } else if (jQuery('#billToCodeA').attr('checked')) {
        jQuery("#houseBL").val("C");
        jQuery('#houseBlC').attr('checked', true);
        if (jQuery("#agent").val() === "") {
            alertNew("Please select Agent Name And Number  from TradeRoute Tab");
            jQuery('#billToCodeA').attr('checked', false);
            setBillToCodeForPreviousValue();
            return;
        }
        confirmYesOrNo("Please note that all Bill to Code will be changed for all Charges - Yes to continue and Cancel to abort operation", "agent");
    } else if (jQuery('#billToCodeT').attr('checked')) {
        jQuery("#houseBL").val("P");
        jQuery('#houseBlP').attr('checked', true);
        document.getElementById("billThirdPartyName").readOnly = false;
        document.getElementById("billThirdPartyName").style.border = "1px solid #C4C5C4";
        document.getElementById("billThirdPartyName").style.backgroundColor = "#FCFCFC";
        if (document.getElementById("contactNameButtonForT")) {
            document.getElementById("contactNameButtonForT").style.visibility = "visible";
        }
        document.getElementById("billTrePty").readOnly = false;
        document.getElementById("billTrePty").style.border = "1px solid #C4C5C4";
        document.getElementById("billTrePty").style.backgroundColor = "#FCFCFC";
        document.getElementById("toggle").style.visibility = "visible";
        if (document.getElementById("billThirdPartyName").value === "") {
            alertNew("Please select Third Party Name and Number");
            setBillToCodeForPreviousValue();
            jQuery('#billToCodeT').attr('checked', false);
            return;
        }
        var bol = jQuery("#bol").val();
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "IsFaeCommisionSetupOrAdvSurCharge",
                param1: bol,
                param2: "yes"
            },
            async: false,
            success: function(data) {
                if (data !== '') {
                    faeCommisionSetupOrAdvSurCharge = data.toString();
                }
            }
        });
        if (faeCommisionSetupOrAdvSurCharge == 'INCENTADDED') {
            confirmYesOrNo("--->Please note that all Bill to party will be changed for all Charges,<br> --->INCENT chargecode will be deleted, <br> ---> - Yes to continue and Cancel to abort operation", "thirdparty");
        } else {
            confirmYesOrNo("Please note that all Bill to party will be changed for all Charges - Yes to continue and Cancel to abort operation", "thirdparty");
        }
    }
}
function checkReadyToPost() {
    //	blAction wil be false, if export or else true
    if (jQuery("#readyToPost").is(":checked")) {
        if (jQuery("#aesbutton").hasClass("buttonStyleNew")) {
            jQuery("#aesbutton").css("border-color", "red");
        } else {
            jQuery("#aesbutton").css("background-color");
        }
        if (jQuery("#confirmOnBoardButton").hasClass("buttonStyleNew")) {
            jQuery("#confirmOnBoardButton").css("border-color", "red");
        } else {
            jQuery("#confirmOnBoardButton").css("background-color");
        }
        var displayMessage = "";
        var bol = jQuery("#bol").val();
        if (jQuery("#vesselname").val() === '') {
            displayMessage += "-->Please Enter Vessel Name <br>";
            jQuery("#vesselname").css("border-color", "red");
        }
        if (jQuery("#voyage").val() === '') {
            displayMessage += "-->Please Enter SS Voyage <br>";
        }
        if (jQuery("#confirmOnBoardNo").is(":checked")) {
            displayMessage += "-->Please  Confirm on board <br>";
        }
        if (jQuery.trim(jQuery("#txtcal313").val()) === '' || jQuery.trim(jQuery("#txtcal71").val()) === '') {
            displayMessage += "-->Please Enter Container Cut Off and Doc Cut Off <br>";
            jQuery("#txtcal313").css("border-color", "red");
            jQuery("#txtcal71").css("border-color", "red");
        }
        if (jQuery("#txtetdCal").val() === '' || jQuery("#txtetaCal").val() === '') {
            displayMessage += "-->Please Enter ETA and ETD <br>";
            jQuery("#txtetdCal").css("border-color", "red");
            jQuery("#txtetaCal").css("border-color", "red");
        }
        if (jQuery('importAMSHosueBlNumber').val() === '') {
            displayMessage += "-->Please Enter AMS House BL# <br>";
        }
        if (jQuery('portofladding').val() === '') {
            displayMessage += "-->Please Enter Port of loading <br>";
        }
        if (jQuery('#billToCodeA').attr('checked') && jQuery("#agent").val() === '') {
            displayMessage += "-->Please Select Agent name<br>";
        }
        if (jQuery.trim(jQuery("#routedAgentCheck").val()) === ''
                && jQuery.trim(jQuery("#ratesNonRates").val()) === '') {
            if (!jQuery("#ratesNonRates").is(":checked")) {
                displayMessage += "-->Please Select ERT<br>";
                jQuery("#routedAgentCheck").css("border-color", "red");
            }
        }
        if (jQuery("#houseName").val() === "" || jQuery("#houseShipper").val() === "") {
            displayMessage += "-->Please Enter Master Shipper Name And Number  <br>";
        }
        if (jQuery("#masterConsigneeCheck").is(":checked")) {
            if (jQuery("#houseConsigneeName").val() === "") {
                displayMessage += "-->Please Enter Master Consignee Name  <br>";
            }
        } else if (jQuery("#houseConsigneeName").val() === "" || jQuery("#houseConsignee").val() === "") {
            displayMessage += "-->Please Enter Master Consignee Name And Number  <br>";
            jQuery("#houseConsigneeName").css("border-color", "red");
            jQuery("#houseName").css("border-color", "red");
        }
        if (jQuery("#shipper").val() === "" || jQuery("#accountName").val() === "") {
            displayMessage += "-->Please Enter House Shipper Name And Number  <br>";
            jQuery("#shipper").css("border-color", "red");
            jQuery("#accountName").css("border-color", "red");
        }
        if (jQuery("#consigneeCheck").is(":checked")) {
            if (jQuery("#consigneeName").val() === "") {
                displayMessage += "-->Please Enter House Consignee Name  <br>";
            }
        } else if (jQuery("#consigneeName").val() === "" || jQuery("#consignee").val() === "") {
            displayMessage += "-->Please Enter House Consignee Name And Number  <br>";
            jQuery("#consigneeName").css("border-color", "red");
            jQuery("#consignee").css("border-color", "red");
        }
        if (jQuery("#forwardingAgentName").val() === "" || jQuery("#forwardingAgent1").val() === "") {
            displayMessage += "-->Please Enter FF Name And Number  <br>";
            jQuery("#forwardingAgentName").css("border-color", "red");
        }
       if ((jQuery("#directConsignmentN").is(':checked')) && (jQuery("#ratesNonRates").val() !== "N")) {
             if (jQuery("#agent").val() === "" && jQuery("#routedAgentCheck").val() === "") {
                displayMessage += "-->Please select agent and ERT or check direct Consignment in Trade Route Tab <br>";
                jQuery("#routedAgentCheckDiv").css("border", " 1px solid red");
            } else if (jQuery("#agent").val() === "") {
                displayMessage += "-->WARNING: An agent has not been selected on this file <br>";
            } else if (jQuery("#routedAgentCheck").val() === "") {
                displayMessage += "-->WARNING: An ERT has not been selected on this file <br>";
                jQuery("#routedAgentCheck").css("border-color", "red");
            }
        }
        if (jQuery('#houseBlC').attr('checked') || jQuery('#houseBlP').attr('checked')) {
            var billToParty = "";
            if (jQuery('#billToCodeF').attr('checked')) {
                billToParty = "Forwarder";
            } else if (jQuery('#billToCodeS').attr('checked')) {
                billToParty = "Shipper";
            } else if (jQuery('#billToCodeT').attr('checked')) {
                billToParty = "ThirdParty";
            } else if (jQuery('#billToCodeA').attr('checked')) {
                billToParty = "Agent";
            }
            if (document.fclBlForm.billTo) {
                for (var i = 0; i < document.fclBlForm.billTo.length; i++) {
                    if (document.fclBlForm.billTo[i].value !== billToParty) {
                        displayMessage += "-->All the charges are not billed to the proper party  <br>";
                        break;
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
                if (data !== null && undefined !== data && data !== "") {
                    displayMessage += data;
                }
            }
        });

        //---CONCATENATING ALL THE MESSAGES AND DISPLAYING ONCE. IF NO ERROR MESSAGE IT WILL GO TO ELSE PART----
        if (displayMessage !== "") {
            jQuery("#readyToPost").attr("checked", false);
            alertNew(displayMessage);
        } else {
            var customer = "";
            if (jQuery("#billToCodeF").is(":checked")) {
                customer = jQuery("#forwardingAgent1").val();
            } else if (jQuery("#billToCodeS").is(":checked")) {
                customer = jQuery("#shipper").val();
            } else if (jQuery("#billToCodeT").is(":checked")) {
                customer = jQuery("#billTrePty").val();
            } else if (jQuery("#billToCodeA").is(":checked")) {
                customer = jQuery("#agentNo").val();
            } else {
                var bill = document.fclBlForm.billTo;
                var billTo = "";
                for (var i = 0; i < document.fclBlForm.billTo.length; i++) {
                    if (bill[i].value.toLowerCase() === "forwarder") {
                        if (billTo.indexOf("Forwarder") === -1) {
                            if (billTo !== "") {
                                billTo = billTo + ",Forwarder";
                            } else {
                                billTo = "Forwarder";
                            }
                            if (customer !== "") {
                                customer = customer + "," + jQuery("#forwardingAgent1").val();
                            } else {
                                customer = jQuery("#forwardingAgent1").val();
                            }
                        }
                    } else if (bill[i].value.toLowerCase() === "shipper") {
                        if (billTo.indexOf("Shipper") === -1) {
                            if (billTo !== "") {
                                billTo = billTo + ",Shipper";
                            } else {
                                billTo = "Shipper";
                            }
                            if (customer !== "") {
                                customer = customer + "," + jQuery("#shipper").val();
                            } else {
                                customer = jQuery("#shipper").val();
                            }
                        }
                    } else if (bill[i].value.toLowerCase() === "thirdparty") {
                        if (billTo.indexOf("ThirdParty") === -1) {
                            if (billTo !== "") {
                                billTo = billTo + ",ThirdParty";
                            } else {
                                billTo = "ThirdParty";
                            }
                            if (customer !== "") {
                                customer = customer + "," + jQuery("#billTrePty").val();
                            } else {
                                customer = jQuery("#billTrePty").val();
                            }
                        }
                    } else if (bill[i].value.toLowerCase() === "agent") {
                        if (billTo.indexOf("Agent") === -1) {
                            if (billTo !== "") {
                                billTo = billTo + ",Agent";
                            } else {
                                billTo = "Agent";
                            }
                            if (customer !== "") {
                                customer = customer + "," + jQuery("#agentNo").val();
                            } else {
                                customer = jQuery("#agentNo").val();
                            }
                        }
                    }
                }
            }
            jQuery.ajaxx({
                data: {
                    className: "com.logiware.dwr.FclDwr",
                    methodName: "validateMasterAccount",
                    param1: customer
                },
                success: function(data) {
                    if (data !== '') {
                        jQuery("#readyToPost").attr("checked", false);
                        alertNew(data + " cannot be billed as it is a Master Account");
                    } else {
                        jQuery.ajaxx({
                            data: {
                                className: "com.logiware.dwr.FclDwr",
                                methodName: "validateCustomerOnManifest",
                                param1: jQuery("#bol").val()
                            },
                            success: function(data) {
                                if (null !== data && data !== '') {
                                    jQuery("#readyToPost").attr("checked", false);
                                    alertNew(data);
                                } else if (jQuery("#agent").val() === "") {
                                    var destination = jQuery("#finalDestination").val();
                                    jQuery.ajaxx({
                                        data: {
                                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                                            methodName: "checkForAgentsAndRules",
                                            param1: destination
                                        },
                                        async: false,
                                        success: function(data) {
                                            if (data !== "" && data === "rules" && jQuery("#ratesNonRates").val() !== 'N') {
                                                confirmYesOrNo("The Agent Field is empty. Would you like to proceed", "agentFalg");
                                                return false;
                                            } else {
                                                checkCustomerNotification();
                                            }
                                        }
                                    });
                                } else {
                                    var agentNo = jQuery("#agentNo").val();
                                    var agentName = jQuery("#agent").val();
                                    var carrierName = jQuery("#streamShipName").val();
                                    var carrierNo = jQuery("#sslinenumber").val();
                                    var bolId = jQuery("#bol").val();
                                    if (jQuery("#sslBlCollect").is(":checked")) {
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
                                                            if (null !== data) {
                                                                var array = new Array();
                                                                if (null !== data.acctType) {
                                                                    array = data.acctType.split(',');
                                                                    if (array.contains('V')) {
                                                                        confirmYesOrNo("All costs in this file will be transferred to " + agentName + " from " + carrierName + " Confirm 'Y/N'", "transferCost");
                                                                    } else {
                                                                        checkCustomerNotification();
                                                                    }
                                                                } else {
                                                                    checkCustomerNotification();
                                                                }
                                                            } else {
                                                                checkCustomerNotification();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    checkCustomerNotification();
                                                }
                                            }
                                        });
                                    } else if (jQuery("#sslBlPrepaid").is(":checked")) {
                                        jQuery.ajaxx({
                                            dataType: "json",
                                            data: {
                                                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                                                methodName: "checkCarrierForCost",
                                                param1: bolId,
                                                param2: agentName,
                                                dataType: "json"
                                            },
                                            success: function(data) {
                                                if (data) {
                                                    jQuery.ajaxx({
                                                        dataType: "json",
                                                        data: {
                                                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                                                            methodName: "getCustInfoForCustNo",
                                                            param1: carrierNo,
                                                            dataType: "json"
                                                        },
                                                        success: function(data) {
                                                            if (null !== data) {
                                                                var array = new Array();
                                                                if (null !== data.acctType) {
                                                                    array = data.acctType.split(',');
                                                                    if (array.contains('V')) {
                                                                        confirmYesOrNo("All costs in this file will be transferred to " + carrierName + " from " + agentName + " Confirm 'Y/N'", "transferCostToCarrier");
                                                                    } else {
                                                                        checkCustomerNotification();
                                                                    }
                                                                } else {
                                                                    checkCustomerNotification();
                                                                }
                                                            } else {
                                                                checkCustomerNotification();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    checkCustomerNotification();
                                                }
                                            }
                                        });
                                    } else {
                                        checkCustomerNotification();
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }
    return displayMessage;
}
function enableSslBlCollect(manifestedFlag, roleDuty) {
    if ((manifestedFlag === 'M' || manifestedFlag === 'm') && roleDuty) {
        var bolId = jQuery("#bol").val();
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getCostTransactionType",
                param1: bolId,
                dataType: "json"
            },
            success: function(data) {
                if (data) {
                    document.getElementById('sslBlPrepaid').tabIndex = 0;
                    document.getElementById('sslBlPrepaid').disabled = false;
                    document.getElementById('sslBlCollect').tabIndex = 0;
                    document.getElementById('sslBlCollect').disabled = false;
                }
            }
        });
    }

}

function onchangeSslBlPrepaidCollect(ele, roleDuty) {
    if (jQuery(ele).val() !== jQuery("#sslPrepaidCollectValue").val() && jQuery(ele).is(":checked")) {
        jQuery("#sslPrepaidCollectValue").val(jQuery(ele).val());
        if (roleDuty) {
            var code = "";
            var dest = jQuery("#finalDestination").val();
            if (dest.lastIndexOf("(") !== -1) {
                code = dest.substring(dest.lastIndexOf("(") + 1, dest.lastIndexOf(")"));
            }
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "getDestCode",
                    param1: code
                },
                async: false,
                success: function(data) {
                    if (data !== null && data !== '') {
                        if ((data === 'X' && jQuery("#sslBlPrepaid").is(":checked")) || (data === 'N' && jQuery("#sslBlCollect").is(":checked"))) {
                            confirmYesOrNo("Please be advised that this selection is not the default setting for this Destination. Are you sure you want to continue?", "sslBlPrepaidCollect");
                        } else {
                            checkAPStatusNotification();
                        }
                    }
                }
            });
        } else {
            checkAPStatusNotification();
        }
    }
}

function updateCostVendor() {
    var bolId = jQuery("#bol").val();
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "checkOCNFRTStatus",
            param1: bolId,
            dataType: "json"
        },
        success: function(data) {
            if (data) {
                if (jQuery("#sslBlPrepaid").is(":checked")) {
                    jQuery("#sslBlCollect").attr("checked", true);
                    jQuery("#sslPrepaidCollectValue").val(jQuery("#sslBlCollect").val());
                } else {
                    jQuery("#sslBlPrepaid").attr("checked", true);
                    jQuery("#sslPrepaidCollectValue").val(jQuery("#sslBlPrepaid").val());
                }
                alertNew("Ocean Costs have already been posted.Please contact Accounting.");
            } else {
                var agentName = jQuery("#agent").val();
                var agentNumber = jQuery("#agentNo").val();
                var sslBLpc = jQuery("#sslPrepaidCollectValue").val();
                jQuery.ajaxx({
                    data: {
                        className: "com.logiware.dwr.FclDwr",
                        methodName: "updateAccountNameAC",
                        forward: "/jsps/FCL/FclBlCharges.jsp",
                        param1: bolId,
                        param2: sslBLpc,
                        param3: agentName,
                        param4: agentNumber
                    },
                    success: function(data) {
                        if (data) {
                            jQuery("#costCharges").html(data);
//                            showAccruals();
                        }
                    }
                });
            }
        }
    });
}

function checkAPStatusNotification() {
    var agentName = jQuery("#agent").val();
    var carrierName = jQuery("#streamShipName").val();
    var bolId = jQuery("#bol").val();
    var directConsignment = jQuery("#directConsignmentY").is(":checked");    
    if (!directConsignment) {
        if (jQuery("#sslBlCollect").is(":checked") && agentName === "") {
            alertNew("Please Select Agent Name");
            jQuery("#sslBlPrepaid").attr("checked", true);
            jQuery("#sslPrepaidCollectValue").val(jQuery("#sslBlPrepaid").val());
        } else if (jQuery("#sslBlPrepaid").is(":checked") && agentName === "") {
            alertNew("Please Select Agent Name");
            jQuery("#sslBlCollect").attr("checked", true);
            jQuery("#sslPrepaidCollectValue").val(jQuery("#sslBlCollect").val());
        }
        else {
            if (jQuery("#sslBlCollect").is(":checked")) {
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
                            confirmYesOrNo("All costs in this file will be transferred to " + agentName + " from " + carrierName + ". Are you sure you want to continue?", "updateCostVendor");
                        }
                    }
                });
            } else if (jQuery("#sslBlPrepaid").is(":checked")) {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                        methodName: "checkCarrierForCost",
                        param1: bolId,
                        param2: agentName,
                        dataType: "json"
                    },
                    success: function(data) {
                        if (data) {
                            confirmYesOrNo("All costs in this file will be transferred to " + carrierName + " from " + agentName + ". Are you sure you want to continue?", "updateCostVendor");
                        }
                    }
                });
            }
        }
    }
}

function confirmationFunction() {
    document.getElementById('AlertOk').style.display = 'none';
    grayOut(false, '');
    var bolId = jQuery("#bol").val();
    var ready = jQuery("#manifest").val();
    if (ready === 'M' || ready === 'm') {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkAPStatus",
                param1: bolId,
                dataType: "json"
            },
            success: function(data) {
                if (data[1] !== null && data[1] !== "" && jQuery("#agentNo").val() !== "") {
                    if (jQuery("#sslBlCollect").is(":checked")) {
                        confirmYesOrNo(data[1] + " Items have been Paid, are you sure you want to continue?", "prepaidTocollectForAP");
                    } else if (jQuery("#sslBlPrepaid").is(":checked")) {
                        confirmYesOrNo(data[1] + " Items have been Paid, are you sure you want to continue?", "collectToprepaidForAP");
                    }
                }
            }
        });
    }
}
function checkCustomerNotification() {
    var customerNumber = '';
    var shipperNumber = '';
    var agentNumber = '';
    var thirdPartyNumber = '';
    var forwarderNumber = "", result = "", displayAutoMsg = "";
    if (jQuery("#billToCodeF").is(":checked")) {
        customerNumber = jQuery("#forwardingAgent1").val();//forwardingAgent1
    } else if (jQuery("#billToCodeS").is(":checked")) {
        customerNumber = jQuery("#shipper").val();//shipper
    } else if (jQuery("#billToCodeT").is(":checked")) {
        customerNumber = jQuery("#billTrePty").val();//billTrePty
    } else if (jQuery("#billToCodeA").is(":checked")) {
        customerNumber = jQuery("#agentNo").val();
    } else {
        if (document.fclBlForm.billTo) {
            var bill = document.fclBlForm.billTo;
            for (var i = 0; i < document.fclBlForm.billTo.length; i++) {
                if (bill[i].value.toLowerCase() === "forwarder") {
                    forwarderNumber = jQuery("#forwardingAgent1").val();
                } else if (bill[i].value.toLowerCase() === "shipper") {
                    shipperNumber = jQuery("#shipper").val();
                } else if (bill[i].value.toLowerCase() === "thirdparty") {
                    thirdPartyNumber = jQuery("#billTrePty").val();
                } else if (bill[i].value.toLowerCase() === "agent") {
                    agentNumber = jQuery("#agentNo").val();
                }
            }
        }
    }
    if (customerNumber !== '') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "validateCustomer",
                param1: customerNumber,
                param2: "E"
            },
            async: false,
            success: function(data) {
                if (data !== null && data !== '') {
                    var creditWarning = data.split("===")[0];
                    var autoCredit = data.split("===")[1];
                    result += creditWarning + "<br>";
                    if (autoCredit.indexOf("NO CREDIT FOR HHG/PE/AUTOS") > -1) {
                        result += "NO CREDIT FOR HHG/PE/AUTOS";
                    } else {
                        result += "HHG/PE/AUTOS CREDIT";
                    }
                    result += "<br>--------------------------------------------------------<br>";
                }
            }
        });
    } else {
        if (forwarderNumber !== '') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "validateCustomer",
                    param1: forwarderNumber,
                    param2: "E"
                },
                async: false,
                success: function(data) {
                    if (data !== null && data !== '') {
                        var creditWarning = data.split("===")[0];
                        var autoCredit = data.split("===")[1];
                        result += creditWarning + "<br>";
                        if (autoCredit.indexOf("NO CREDIT FOR HHG/PE/AUTOS") > -1) {
                            result += "NO CREDIT FOR HHG/PE/AUTOS";
                        } else {
                            result += "HHG/PE/AUTOS CREDIT";
                        }
                        result += "<br>--------------------------------------------------------<br>";
                    }
                }
            });
        }
        if (shipperNumber !== '') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "validateCustomer",
                    param1: shipperNumber,
                    param2: "E"
                },
                async: false,
                success: function(data) {
                    if (data !== null && data !== '') {
                        var creditWarning = data.split("===")[0];
                        var autoCredit = data.split("===")[1];
                        result += creditWarning + "<br>";
                        if (autoCredit.indexOf("NO CREDIT FOR HHG/PE/AUTOS") > -1) {
                            result += "NO CREDIT FOR HHG/PE/AUTOS";
                        } else {
                            result += "HHG/PE/AUTOS CREDIT";
                        }
                        result += "<br>--------------------------------------------------------<br>";
                    }
                }
            });
        }
        if (thirdPartyNumber !== '') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "validateCustomer",
                    param1: thirdPartyNumber,
                    param2: "E"
                },
                async: false,
                success: function(data) {
                    if (data !== null && data !== '') {
                        var creditWarning = data.split("===")[0];
                        var autoCredit = data.split("===")[1];
                        result += creditWarning + "<br>";
                        if (autoCredit.indexOf("NO CREDIT FOR HHG/PE/AUTOS") > -1) {
                            result += "NO CREDIT FOR HHG/PE/AUTOS";
                        } else {
                            result += "HHG/PE/AUTOS CREDIT";
                        }
                        result += "<br>--------------------------------------------------------<br>";
                    }
                }
            });
        }
        if (agentNumber !== '') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "validateCustomer",
                    param1: agentNumber,
                    param2: "E"
                },
                async: false,
                success: function(data) {
                    if (data !== null && data !== '') {
                        var creditWarning = data.split("===")[0];
                        var autoCredit = data.split("===")[1];
                        result += creditWarning + "<br>";
                        if (autoCredit.indexOf("NO CREDIT FOR HHG/PE/AUTOS") > -1) {
                            result += "NO CREDIT FOR HHG/PE/AUTOS";
                        } else {
                            result += "HHG/PE/AUTOS CREDIT";
                        }
                        result += "<br>--------------------------------------------------------<br>";
                    }
                }
            });
        }
    }
    var bol = jQuery("#bol").val();
    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "IsFaeCommisionSetupOrAdvSurCharge",
            param1: bol,
            param2: "yes"
        },
        async: false,
        success: function(data) {
            if (data !== '') {
                faeCommisionSetupOrAdvSurCharge = data.toString();
            }
        }
    });
    if (displayAutoMsg !== "") {
        jQuery("#readyToPost").attr("checked", false);
        alertNew("<font color='red'>" + displayAutoMsg + "</font>");
    } else if (faeCommisionSetupOrAdvSurCharge == 'INCENTNOTADDED') {
        confirmYesOrNo("FAE Commissions setup have not been Applied,<br>Are you Sure Want to check ready To Post ?", "faeCommisionSetup");
    } else if (result !== "") {
        result = "<font color='red'>" + result + "</font>";
        result += "<br> Do you want to check ready To Post  ? "
        confirmNew(result, "autoNotification");
    } else {
        jQuery("#readyToPost").attr("checked", false);
        saveAction('readyToPost');
    }

}
function saveAction(methodName) {
    if (jQuery("#readyToPost").is(":checked")) {
        alertNew("Ready To Post Check box is checked Please manifest the BL or Uncheck the Ready To Post check box and Save");
    }
    if (jQuery("#fileTypeC").is(":checked") && jQuery("#voyage").val() === "") {
        alertNew("Please enter Voyage...");
    }
    if (jQuery("#billToCodeF").is(":checked") && (jQuery("#forwardingAgentName").val() === 'NO FF ASSIGNED' ||
            jQuery("#forwardingAgentName").val() === 'NO FF ASSIGNED / B/L PROVIDED' ||
            jQuery("#forwardingAgentName").val() === 'NO FRT. FORWARDER ASSIGNED')) {
        alertNew("Please change Bill To Party or change Forwarder, because Forwarder is NO FF ASSIGNED");
    }
    setAction(methodName);
}

function displayAutoNotificationContacts(manifestWithoutCharges) {
    if (document.getElementById('contactConfigDetails') !== null && manifestWithoutCharges !== 'yes') {
        showPopUp();
        document.getElementById('contactConfigDetails').style.display = 'block';
    } else {
        jQuery("#readyToPost").attr("checked", true);
    }
}
function sendFrieghtInvoice() {
    var count = 0;
    var selectedIds = new Array();
    jQuery("input[name='freightInvoiceContactIds']:checked").each(function() {
        selectedIds.push(jQuery(this).val());
        count++;
    });
    if (count === 0) {
        alertNew("Please select at least one contact from the list to receive the Freight Invoice.");
    } else {
        jQuery("#freightInvoiceContacts").val(selectedIds);
        jQuery("#readyToPost").attr("checked", true);
        jQuery("#action").val("sendFrieghtInvoice");
        closePopUp();
        document.getElementById('contactConfigDetails').style.display = 'none';
    }
}
function frieghtInvoiceWillNotSent() {
    jQuery("#readyToPost").attr("checked", true);
    jQuery("#action").val("frieghtInvoiceWillNotSent");
    closePopUp();
    document.getElementById('contactConfigDetails').style.display = 'none';
}
function doNotSendFrieghtInvoice() {
    jQuery("#readyToPost").attr("checked", true);
    jQuery("#action").val("doNotSendFrieghtInvoice");
    closePopUp();
    document.getElementById('contactConfigDetails').style.display = 'none';
}
function manifestFclBl() {
    hidebkgvesselvoy();
    jQuery("#eventCode").val("100009");
    setAction("manifest");
}
function hidebkgvesselvoy() {
    var frameRef = window.parent.$("#tab" + 2);
    var tab = window.parent.$("#tab" + 2).attr("title");
    if (undefined !== tab && tab === 'Bookings') {
        if (frameRef.EditBookingsForm) {
            var vessel = frameRef.EditBookingsForm.vessel;
            var ssVoy = frameRef.EditBookingsForm.ssVoy;
            vessel.style.backgroundColor = "#CCEBFF";
            vessel.readOnly = true;
            vessel.tabIndex = -1;
            vessel.style.border = 0;
            vessel.className = "textlabelsBoldForTextBox";
            vessel.style.borderLeft = "red 2px solid";
            ssVoy.style.backgroundColor = "#CCEBFF";
            ssVoy.readOnly = true;
            ssVoy.tabIndex = -1;
            ssVoy.style.border = 0;
            ssVoy.className = "textlabelsBoldForTextBox";
            ssVoy.style.borderLeft = "red 2px solid";
        }
    }
}
function unHidebkgvesselvoy() {
    var frameRef = window.parent.$("#tab" + 2);
    var tab = window.parent.$("#tab" + 2).attr("title");
    if (undefined !== tab && tab === 'Bookings') {
        if (frameRef.EditBookingsForm) {
            var vessel = frameRef.EditBookingsForm.vessel;
            var ssVoy = frameRef.EditBookingsForm.ssVoy;
            vessel.style.backgroundColor = "#FCFCFC";
            vessel.readOnly = false;
            vessel.tabIndex = 0;
            vessel.style.border = "1px solid #C4C5C4";
            vessel.className = "textlabelsBoldForTextBox";
            vessel.style.borderLeft = "red 2px solid";
            ssVoy.style.backgroundColor = "#FCFCFC";
            ssVoy.readOnly = false;
            ssVoy.tabIndex = 0;
            ssVoy.style.border = "1px solid #C4C5C4";
            ssVoy.className = "textlabelsBoldForTextBox";
            ssVoy.style.borderLeft = "red 2px solid";
        }
    }
}
function closeOrAudit(val) {
    var masterBl = jQuery('#newMasterBL').val();
    var masterStatus = jQuery('#masterScanStatus').val();
    var closedBy = jQuery('#blClosedBy').val();
    var auditedBy = jQuery('#blAuditedBy').val();
    if (val === 'blOpen' && auditedBy !== '') {
        alertNew("Please Cancel Audit BL..");
        return false;
    }
    if (val === 'blAudit' && (closedBy === null || closedBy === '')) {
        alertNew("Please Close BL..");
        return false;
    }
    if (val === 'blAudit') {
        if (masterBl === "") {
            alertNew("Please Enter Master BL..");
        } else if (masterStatus !== 'Approved') {
            alertNew("Cannot Audit BL, Master in Disputed State..");
        } else if (masterStatus === "") {
            alertNew("Cannot Audit BL, Master not Attached or Scanned..");
        } else {
            confirmYesOrNo("Do you want to Audit BL Y/N", val);
        }
    } else if (val === 'blOpen') {
        confirmYesOrNo("Do you want to Open BL Y/N", val);
    } else if (val === 'blClose') {
        var alertMessage = "";
        if (masterBl === "") {
            alertMessage += "Please Enter Master BL..<br>";
        } else if (masterStatus === 'Disputed') {
            alertMessage += "Cannot Close BL, Master in Disputed State..<br>";
        }
        if (masterStatus === '') {
            alertMessage += "Cannot Close BL, SS LINE MASTER BL not Attached or Scanned..<br>";
        }
        if (alertMessage !== "") {
            alertNew(alertMessage);
        } else {
            confirmYesOrNo("Do you want to Close BL Y/N", val);
        }
    } else if (val === 'blCancelAudit') {
        confirmYesOrNo("Do you want to Cancel Audit BL Y/N", val);
    }
}
function resendcost(costId) {
    jQuery("#selectedId").val(costId);
    setAction("resendAccrual");
}
function createMultipleBL() {
    var bol = jQuery("#bol").val();
    var displayMessage = "";
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForUnitNoInContainerDetailsToMultipleBl",
            param1: bol
        },
        async: false,
        success: function(data) {
            if (data !== null && undefined !== data && data !== "") {
                displayMessage = data;
            }
        }
    });
    if (displayMessage !== "") {
        alertNew(displayMessage);
    } else {
        confirmYesOrNo("Do you want to create Multiple BL", "createMultipleBL");
    }
}
function addInsuranceRate() {
    var costOfGoods = jQuery("#costOfGoods").val();
    var insuranceRate = jQuery("#insuranceRate").val();
    GB_show("Insurance Rate", "/logisoft/jsps/fclQuotes/calculateInsurance.jsp?costOfGoods=" + costOfGoods + "&insuranceAmount=" + insuranceRate, width = "200", height = "300");
}
function calculateInsurance(costOfGoods, insuranceRate) {
    jQuery("#costOfGoods").val(costOfGoods);
    jQuery("#insuranceRate").val(insuranceRate);
    setAction('calculateInsurance');
}
function removeInsurance() {
    confirmNew("Insurance Charges will be deleted and all the BL information will be saved. Are you sure?", "deleteInsurance");
}
function chargesdelete(chargeId) {
    jQuery("#selectedId").val(chargeId);
    confirmYesOrNo("Are you sure you want to delete this charge", "deleteBlCharge");
}
function deleteCostDetails(costId) {
    jQuery("#selectedId").val(costId);
    confirmYesOrNo("Are you sure you want to delete this CostCode", "deleteBlCost");
}
function deleteAesFiling(trnref) {
    jQuery("#selectedId").val(trnref);
    confirmYesOrNo("Are you sure you want to delete this AES Details", "deleteAesDetails");
}
function deleteAesDetails() {
    var bol = jQuery("#bol").val();
    var trnref = jQuery("#selectedId").val();
    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "deleteAesFiling",
            forward: "/jsps/FCL/SedFiling.jsp",
            param1: bol,
            param2: trnref
        },
        success: function(data) {
            if (data) {
                jQuery("#aesFiling").html(data);
            }
        }
    });
}
function FEACalculation() {
    if (jQuery("#agentNo").val() === '') {
        alertNew("Agent Name and Number cannot be empty");
        return;
    } else if (jQuery("#routedAgentCheck").val() === '') {
        alertNew("Please Select ERT");
        return;
    }
    confirmYesOrNo("Are you sure you want do FAE Calculation", "FAECalculation");
}
function postBeforeManifest() {
    var fileNo = jQuery("#fileNo").val();
    if (checkAddChargeMappingWithGL(fileNo, "AC")) {
    confirmYesOrNo("Do you want to Post Accruals Before Manifesting?", "postAccrualsBeforeManifest");
    }
}
function closeEdi() {
    jQuery("#ediCheckBox").attr('checked', false);
    jQuery("#sendEdi").css({
        visibility: "hidden"
    });
    jQuery("#sendEdi1").css({
        visibility: "hidden"
    });
}
function generate304Xml() {
    var fileno = jQuery("#fileNo").val();
    var ediType = jQuery("#inttraGtnexus").val();
    if (jQuery("lineMove").val() === '00') {
        alertNew("Please Select Line Move");
    } else if (fileno !== "") {
        if (ediType === "I") {
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
            if (ediType === "G") {
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
    var bol = jQuery("#bol").val();
    var userName = jQuery("#userName").val();
    if (null !== data && data !== "") {
        if (data === 'XML generated successfully') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "updateEdi",
                    param1: bol,
                    param2: userName
                },
                success: function(date) {
                    if (null !== date && date !== "") {
                        jQuery("#ediCreatedOn").val(date);
                        jQuery("#ediCreatedBy").val(userName);
                        document.getElementById("ediby").innerHTML = userName;
                        document.getElementById("edion").innerHTML = date;
                    }
                }
            });
        }
        alertNew(data);
    }
    closeEdi();
}
function validateDataForEdi(validate) {
    var fileno = jQuery("#fileNo").val();
    var fclInttra = jQuery("#inttraGtnexus").val();
    jQuery("#ediCheckBox").attr('checked', true);
    if (undefined !== fileno && null !== fileno && fileno !== '') {
        if (fclInttra === "I") {
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
            if (fclInttra === "G") {
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
function ediAlert(data) {
    if (null !== data && data !== "") {
        if (data !== 'No Error') {
            alertNew(data);
            closeEdi();
        } else {
            if (jQuery("#ediCheckBox").attr('checked')) {
                document.getElementById("sendEdi").style.visibility = "visible";
                document.getElementById("sendEdi1").style.visibility = "visible";
            }
        }
    }
}
function hideAlternatePort() {
    if (document.getElementById("alterNatePortId") && undefined !== document.getElementById("alterNatePortId") && jQuery("#printAlternatePortYes").is(":checked")) {
        document.getElementById("alterNatePortId").style.display = "block";
    } else if (document.getElementById("alterNatePortId") && undefined !== document.getElementById("alterNatePortId")) {
        document.getElementById("alterNatePortId").style.display = "none";
    }
}
function hideHblPOL() {
    if (jQuery("#hblPOLOverrideYes").is(":checked")) {
        document.getElementById("hblPOLId").style.display = "block";
    } else {
        document.getElementById("hblPOLId").style.display = "none";
    }
}
function hideHblPOD() {
    if (jQuery("#hblPODOverrideYes").is(":checked")) {
        document.getElementById("hblPODId").style.display = "block";
    } else {
        document.getElementById("hblPODId").style.display = "none";
    }
}
function hideHblFD() {
    if (jQuery("#hblFDOverrideYes").is(":checked")) {
        document.getElementById("hblFDId").style.display = "block";
    } else {
        document.getElementById("hblFDId").style.display = "none";
    }
}
function readyToSendEdi() {
    if (jQuery("#ediCheckBox").attr('checked')) {
        if (jQuery("lineMove").val() !== '00') {
            jQuery("#action").val("sendEdi");
            setAction("saveFclBl");
        } else {
            alertNew("Please Select Line Move");
            jQuery("#ediCheckBox").attr('checked', false);
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
function openCommentPopUp(size, id, action, roleDuty) {
    var bol = jQuery("#bol").val();
    if (action === '') {
        disableOrEnable(size, id, action);
    } else {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "checkAccrualPosted",
                param1: bol,
                dataType: "json"
            },
            success: function (data) {
                if (data && roleDuty === 'true') {
                    if (action === 'disableContainer') {
                       
                        var txt = "Accruals have been posted on this file, are you sure you want to disable this Container?";
                        jQuery.prompt(txt, {
                            buttons: {
                                yes: 1,
                                No: 2
                            },
                            submit: function (v) {
                                if (v === 1) {
                                  
                                     disableOrEnable(size, id, action);
                                }
                            }
                        });

                    } else {
                        var text = "Accruals have been posted on this file, are you sure you want to enable this Container?";
                        jQuery.prompt(text,{
                           buttons: {
                             yes: 1,
                             No: 2
                           }, 
                           submit: function(v) {
                               if(v === 1){
                                disableOrEnable(size, id, action); 
                              }
                           }
                        });
                    }
                } else if (data && roleDuty === 'false') {
                    alertNew("Accruals posted and Role Duty is not checked,Cannot Disable or Enable Container");

                } else {
                    disableOrEnable(size, id, action);
                }
            }
        });
    }

}
function disableOrEnable(size, id, action) {
    showPopUp();
    document.getElementById('containerCommentBox').style.display = 'block';
    if (action === '') {
        var comment = '';
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getCommentsOfEachContainer",
                param1: id
            },
            success: function(data) {
                if (data !== null && data !== '') {
                    j = data.indexOf("-");
                    if (j !== -1) {
                        comment = data.substring(0, j);
                    } else {
                        comment = data;
                    }
                    jQuery("#comment").val(comment);
                }
            }
        });
    } else {
        jQuery("#comment").val("");
    }
    jQuery("#size").val(size);
    jQuery("#selectedId").val(id);
    jQuery("#action").val(action);
}
function disableOrEnableForAes(id, action) {
    showPopUp();
    jQuery("#containerCommentBoxForAes").center().show(500);
    if (action === '') {
        var comment = '';
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getCommentsOfAes",
                param1: id
            },
            success: function(data) {
                if (data !== null && data !== '') {
                    j = data.indexOf("-");
                    if (j !== -1) {
                        comment = data.substring(0, j);
                    } else {
                        comment = data;
                    }
                    jQuery("#aesComment").val(comment);
                }
            }
        });
    } else {
        jQuery("#aesComment").val("");
    }
    jQuery("#selectedId").val(id);
    jQuery("#action").val(action);
}
function closeCommentPopUp() {
    jQuery("#comment").val("");
    jQuery("#size").val("");
    jQuery("#selectedId").val("");
    jQuery("#action").val("");
    closePopUp();
    document.getElementById('containerCommentBox').style.display = 'none';
}
function closeCommentPopUpForAes() {
    jQuery("#aesComment").val("");
    jQuery("#selectedId").val("");
    jQuery("#action").val("");
    closePopUp();
    jQuery("#containerCommentBoxForAes").hide(500);
}
function enableDisableContainer() {
    var action = jQuery("#action").val();
    closePopUp();
    document.getElementById('containerCommentBox').style.display = 'none';
    setAction(action);
}
function enableDisableContainerForAes() {
    var action = jQuery("#action").val();
    closePopUp();
    document.getElementById('containerCommentBoxForAes').style.display = 'none';
    setAction(action);
}
function makePageEditWhileSaving(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type === "checkbox" || element.type === "radio" || element.type === "select-one") {
            element.disabled = false;
        }
    }
}
function setAction(methodName) {
    if (jQuery("#readyToPost").attr('checked')) {
        var displayMessage = "";
        if (jQuery("#houseName").val() === "" || jQuery("#houseShipper").val() === "") {
            displayMessage += "-->Please Enter Master Shipper Name And Number  <br>";
            jQuery("#houseName").css("border-color", "red");
        }
        if (jQuery("#masterConsigneeCheck").is(":checked")) {
            if (jQuery("#houseConsigneeName").val() === "") {
                displayMessage += "-->Please Enter Master Consignee Name  <br>";
            }
        } else if (jQuery("#houseConsigneeName").val() === "" || jQuery("#houseConsignee").val() === "") {
            displayMessage += "-->Please Enter Master Consignee Name And Number  <br>";
            jQuery("#houseConsigneeName").css("border-color", "red");
        }
        if (jQuery("#consigneeCheck").is(":checked")) {
            if (jQuery("#consigneeName").val() === "") {
                displayMessage += "-->Please Enter House Consignee Name  <br>";
            }
        } else if (jQuery("#consigneeName").val() === "" || jQuery("#consignee").val() === "") {
            displayMessage += "-->Please Enter House Consignee Name And Number  <br>";
            jQuery("#consigneeName").css("border-color", "red");
            jQuery("#consignee").css("border-color", "red");
        }
        if (displayMessage !== "") {
            alertNew(displayMessage);
            return;
        }
    }
    if (jQuery("#billToCodeT").is(":checked") && jQuery.trim(jQuery("#billThirdPartyName").val()) === "") {
        alertNew("Please select Third Party Name and Number");
        return;
    }
    if (document.fclBlForm.billingTerminal.value === "" && methodName === "saveFclBl") {
        alertNew("Please Select Issuing Terminal");
    } else {
        makePageEditWhileSaving(document.getElementById("fclBl"));
        jQuery("#methodName").val(methodName);
        if(methodName === 'manifest'){
            document.getElementById("manifestBl").disabled = true;
        }
        if(methodName === 'unManifest'){
          document.getElementById("unManifestBl").disabled = true;
        }
        jQuery("#fclBl").submit();
         if(methodName === 'unManifest'){
           document.getElementById("manifestBl").enabled = true;
        }
         if(methodName === 'manifest'){
          document.getElementById("unManifestBl").enabled = true;
        }
        showPreloading();
    }
}
function confirmMessageFunction(id1, id2) {
    var bol = jQuery("#bol").val();
    if (id1 === 'fromCollect' && id2 === 'yes') {
        jQuery("#readyToPost").attr('checked', false);
        document.getElementById("consigneeCheck").style.visibility = "visible";
        if (faeCommisionSetupOrAdvSurCharge != '') {
            deleteIncentAndAdvSurCharge(faeCommisionSetupOrAdvSurCharge);
        } else {
            checkForCustomersPresence();//
        }
    } else if (id1 === 'fromCollect' && id2 === 'no') {
        noFunctionForHouseBl();
    } else if (id1 === 'fromPrepaidOrCollect' && id2 === 'yes') {
        jQuery("#readyToPost").attr('checked', false);
        jQuery('#billToCodeA').attr('checked', false);
        jQuery('#billToCodeF').attr('checked', false);
        jQuery('#billToCodeS').attr('checked', false);
        jQuery('#billToCodeT').attr('checked', false);
        jQuery('#billToCodeA').attr('disabled', true);
        jQuery('#billToCodeF').attr('disabled', true);
        jQuery('#billToCodeS').attr('disabled', true);
        jQuery('#billToCodeT').attr('disabled', true);
        activeBillThirdParty();
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "setHouseBlToBoth",
                forward: "/jsps/FCL/FclBlCharges.jsp",
                param1: bol
            },
            success: function(data) {
                loadCostAndCharges(data);
            }
        });
    } else if (id1 === 'fromPrepaidOrCollect' && id2 === 'no') {
        noFunctionForHouseBl();
    } else if (id1 === 'fromPrepaidAgent' && id2 === 'yes') {
        jQuery("#readyToPost").attr('checked', false);
        clearBillThirdParty();
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "setBillToParty",
                forward: "/jsps/FCL/FclBlCharges.jsp",
                param1: bol,
                param2: "Agent",
                param3: jQuery("#houseBL").val()
            },
            success: function(data) {
                loadCostAndCharges(data);
            }
        });
    } else if (id1 === 'fromPrepaidAgent' && id2 === 'no') {
        noFunctionForHouseBl();
    } else if (id1 === "forwarder" && id2 === "yes") {
        document.getElementById('billThirdPartyName').value = '';
        document.getElementById('billTrePty').value = '';
        jQuery("#readyToPost").attr('checked', false);
        modifyAccountDetails();
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "setBillToParty",
                forward: "/jsps/FCL/FclBlCharges.jsp",
                param1: bol,
                param2: "Forwarder",
                param3: jQuery("#houseBL").val()
            },
            success: function(data) {
                loadCostAndCharges(data);
            }
        });
    } else if (id1 === "forwarder" && id2 === "no") {
        noFunctionForHouseBl();
    }
    if (id1 === "shipper" && id2 === "yes") {
        document.getElementById('billThirdPartyName').value = '';
        document.getElementById('billTrePty').value = '';
        jQuery("#readyToPost").attr('checked', false);
        modifyAccountDetails();
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "setBillToParty",
                forward: "/jsps/FCL/FclBlCharges.jsp",
                param1: bol,
                param2: "Shipper",
                param3: jQuery("#houseBL").val()
            },
            success: function(data) {
                loadCostAndCharges(data);
            }
        });
    } else if (id1 === "shipper" && id2 === "no") {
        noFunctionForHouseBl();
    } else if (id1 === "agent" && id2 === "yes") {
        jQuery("#readyToPost").attr('checked', false);
        clearBillThirdParty();
        modifyAccountDetails();
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "setBillToParty",
                forward: "/jsps/FCL/FclBlCharges.jsp",
                param1: bol,
                param2: "Agent",
                param3: jQuery("#houseBL").val()
            },
            success: function(data) {
                loadCostAndCharges(data);
            }
        });
    } else if (id1 === "agent" && id2 === "no") {
        noFunctionForHouseBl();
    }
    if (id1 === "thirdparty" && id2 === "yes") {
        jQuery("#readyToPost").attr('checked', false);
        jQuery("#billTrePty").attr('readonly', true);
        jQuery("#billTrePty").attr('style', '#CCEBFF');
        if (!jQuery("#houseBlB").attr('checked')) {
            jQuery("#billToCodeT").attr('checked', true);
        }
        deleteIncentForthirdParty();
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "setBillToParty",
                forward: "/jsps/FCL/FclBlCharges.jsp",
                param1: bol,
                param2: "ThirdParty",
                param3: jQuery("#houseBL").val()
            },
            success: function(data) {
                loadCostAndCharges(data);
            }
        });
    } else if (id1 === "thirdparty" && id2 === "no") {
        document.getElementById('billThirdPartyName').value = '';
        document.getElementById('billTrePty').value = '';
        jQuery("#billTrePty").attr('readonly', true);
        jQuery("#billTrePty").attr('style', '#CCEBFF');
        noFunctionForHouseBl();
    } else if (id1 === 'agentFalg' && id2 === 'yes') {
        checkCustomerNotification();
    } else if (id1 === 'agentFalg' && id2 === 'no') {
        jQuery("#readyToPost").attr('checked', false);
    } else if (id1 === 'transferCost' && id2 === 'yes') {
        jQuery("#action").val("transferCost");
        checkCustomerNotification();
    } else if (id1 === 'transferCost' && id2 === 'no') {
        checkCustomerNotification();
    } else if (id1 === 'transferCostToCarrier' && id2 === 'yes') {
        jQuery("#action").val("transferCostToCarrier");
        checkCustomerNotification();
    } else if (id1 === 'transferCostToCarrier' && id2 === 'no') {
        checkCustomerNotification();
    } else if (id1 === 'prepaidTocollectForAP' && id2 === 'yes') {
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "changeCostAcctName",
                forward: "/jsps/FCL/FclBlCharges.jsp",
                param1: bol,
                param2: jQuery("#agentNo").val(),
                param3: jQuery("#agent").val(),
                param4: jQuery("#sslinenumber").val()
            },
            success: function(data) {
                loadCostAndCharges(data);
            }
        });
    } else if (id1 === 'collectToprepaidForAP' && id2 === 'yes') {
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "changeCostAcctName",
                forward: "/jsps/FCL/FclBlCharges.jsp",
                param1: bol,
                param2: jQuery("#sslinenumber").val(),
                param3: jQuery("#streamShipName").val(),
                param4: jQuery("#agentNo").val()
            },
            success: function(data) {
                loadCostAndCharges(data);
            }
        });
    } else if (id1 === 'autoNotification' && id2 === 'ok') {
        jQuery("#readyToPost").attr('checked', false);
        saveAction('readyToPost');
    } else if (id1 === 'autoNotification' && id2 === 'cancel') {
        jQuery("#readyToPost").attr('checked', false);
    } else if (id1 === 'readyToPost' && id2 === 'ok') {
        manifestFclBl();
    } else if (id1 === 'unManifest' && id2 === 'ok') {
        jQuery("#eventCode").val('100010');
        setAction(id1);
    } else if (id1 === 'blAudit' && id2 === 'yes') {
        jQuery("#eventCode").val('100014');
        setAction(id1);
    } else if (id1 === 'blOpen' && id2 === 'yes') {
        jQuery("#eventCode").val('100013');
        setAction(id1);
    } else if (id1 === 'blClose' && id2 === 'yes') {
        jQuery("#eventCode").val('100012');
        setAction(id1);
    } else if (id1 === 'blCancelAudit' && id2 === 'yes') {
        jQuery("#eventCode").val('100015');
        setAction(id1);
    } else if (id1 === 'reverseToBooking' && id2 === 'yes') {
        jQuery("#eventCode").val('100008');
        setAction(id1);
    } else if (id1 === 'deleteBl' && id2 === 'yes') {
        setAction(id1);
    } else if (id1 === 'deleteBlCharge' && id2 === 'yes') {
        setAction(id1);
    } else if (id1 === 'deleteBlCost' && id2 === 'yes') {
        setAction(id1);
    } else if (id1 === 'postAccrualsBeforeManifest' && id2 === 'yes') {
        setAction(id1);
    } else if (id1 === 'deleteAesDetails' && id2 === 'yes') {
        deleteAesDetails();
    } else if (id1 === 'createMultipleBL' && id2 === 'yes') {
        jQuery("#eventCode").val('100011');
        setAction(id1);
    } else if (id1 === 'FAECalculation' && id2 === 'yes') {
        calculateFAE();
    } else if (id1 === 'deleteInsurance' && id2 === 'ok') {
        setAction(id1);
    } else if (id1 === 'clearShipperForEdi' && id2 === 'ok') {
        clearShipperDataForEdi();
    } else if (id1 === 'clearShipperForEdi' && id2 === 'cancel') {
        jQuery("#ediShipperCheck").attr('checked', false);
    } else if (id1 === 'clearConsigneeForEdi' && id2 === 'ok') {
        clearConsigneeDataForEdi();
    } else if (id1 === 'clearConsigneeForEdi' && id2 === 'cancel') {
        jQuery("#ediConsigneeCheck").attr('checked', false);
    } else if (id1 === 'editAgentCheck' && id2 === 'ok') {
        clearAgentForEdi();
    } else if (id1 === 'editAgentCheck' && id2 === 'cancel') {
        jQuery("#editAgentNameCheck").attr('checked', false);
    } else if (id1 === 'clearNotifyForEdi' && id2 === 'ok') {
        clearNotifyDataForEdi();
    } else if (id1 === 'clearNotifyForEdi' && id2 === 'cancel') {
        jQuery("#ediNotifyPartyCheck").attr('checked', false);
    } else if (id1 === 'clearNotifyParty' && id2 === 'ok') {
        clearNotifyPartyData();
        document.getElementById("notifyParty").disabled = true;
        document.getElementById("notifyParty").style.backgroundColor = '#CCEBFF';
    } else if (id1 === 'clearNotifyParty' && id2 === 'cancel') {
        document.getElementById("notifyCheck").checked = false;
    } else if (id1 === 'clearConsignee' && id2 === 'ok') {
        clearConsigneeData();
        document.getElementById("consignee").disabled = true;
        document.getElementById("consignee").style.backgroundColor = '#CCEBFF';
    } else if (id1 === 'clearConsignee' && id2 === 'cancel') {
        document.getElementById("consigneeCheck").checked = false;
    } else if (id1 === 'clearMasterNotifyParty' && id2 === 'ok') {
        clearMasterNotifyData();
        document.getElementById("houseNotifyParty").disabled = true;
        document.getElementById("houseNotifyParty").style.backgroundColor = '#CCEBFF';
    } else if (id1 === 'clearMasterNotifyParty' && id2 === 'cancel') {
        document.getElementById("masterNotifyCheck").checked = false;
    } else if (id1 === 'clearMasterConsignee' && id2 === 'ok') {
        clearMasterConsigneeData();
        document.getElementById("houseConsignee").disabled = true;
        document.getElementById("houseConsignee").style.backgroundColor = '#CCEBFF';
    } else if (id1 === 'clearMasterConsignee' && id2 === 'cancel') {
        document.getElementById("masterConsigneeCheck").checked = false;
    } else if (id1 === 'clearHouseShipper' && id2 === 'ok') {
        clearHouseShipperData();
        document.getElementById("shipper").disabled = true;
        document.getElementById("shipper").style.backgroundColor = '#CCEBFF';
    } else if (id1 === 'clearHouseShipper' && id2 === 'cancel') {
        document.getElementById("houseShipperCheck").checked = false;
    } else if (id1 === 'UncheckClearNotify' && id2 === 'yes') {
        clearNotifyPartyData();
    } else if (id1 === 'UncheckClearConsignee' && id2 === 'yes') {
        clearConsigneeData();
    } else if (id1 === 'UncheckClearMasterNotify' && id2 === 'yes') {
        clearMasterNotifyData();
    } else if (id1 === 'UncheckClearMasterConsignee' && id2 === 'yes') {
        clearMasterConsigneeData();
    } else if (id1 === 'UnCheckClearHouseShipper' && id2 === 'yes') {
        clearHouseShipperData();
    } else if (id1 === 'UncheckClearConsignee' && id2 === 'no') {
        document.getElementById("consigneeCheck").checked = true;
    } else if (id1 === 'UncheckClearMasterNotify' && id2 === 'no') {
        document.getElementById("masterNotifyCheck").checked = true;
    } else if (id1 === 'UncheckClearMasterConsignee' && id2 === 'no') {
        document.getElementById("masterConsigneeCheck").checked = true;
    } else if (id1 === 'UncheckClearNotify' && id2 === 'no') {
        document.getElementById("notifyCheck").checked = true;
    } else if (id1 === 'goBack' && id2 === 'yes') {
        if (document.fclBlForm.billingTerminal.value === "") {
            alertNew("Please Select Issuing Terminal");
        } else {
            jQuery("#action").val("save");
            setAction("goToSearch");
        }
    } else if (id1 === 'goBack' && id2 === 'no') {
        setAction("goToSearch");
    } else if (id1 === 'goBack' && id2 === 'cancel') {
        return;
    } else if (id1 === "sslBlPrepaidCollect") {
        if (id2 === "yes") {
            checkAPStatusNotification();
        } else {
            if (jQuery("#sslBlPrepaid").is(":checked")) {
                jQuery("#sslBlCollect").attr("checked", true);
                jQuery("#sslPrepaidCollectValue").val(jQuery("#sslBlCollect").val());
            } else {
                jQuery("#sslBlPrepaid").attr("checked", true);
                jQuery("#sslPrepaidCollectValue").val(jQuery("#sslBlPrepaid").val());
            }
        }
    } else if (id1 === "updateCostVendor") {
        if (id2 === "yes") {
            updateCostVendor();
        } else {
            if (jQuery("#sslBlPrepaid").is(":checked")) {
                jQuery("#sslBlCollect").attr("checked", true);
                jQuery("#sslPrepaidCollectValue").val(jQuery("#sslBlCollect").val());
            } else {
                jQuery("#sslBlPrepaid").attr("checked", true);
                jQuery("#sslPrepaidCollectValue").val(jQuery("#sslBlPrepaid").val());
            }
        }
    } else if (id1 === "faeCommisionSetup" && id2 === "yes") {
        jQuery("#readyToPost").attr('checked', false);
        saveAction('readyToPost');
    } else if (id1 === "faeCommisionSetup" && id2 === "no") {
        jQuery("#readyToPost").attr("checked", false);
    }else if (id1 === "Econo/Ecu Worldwide" && id2 === "yes") {
       
        setAction("addBrandValue");
    } else if (id1 === "Ecu Worldwide/Econo" && id2 === "yes") {
        jQuery("#brandEcuworldwide").attr('checked',true);
        setAction("addBrandValue");
    } else if (id1 === "OTI/Ecu Worldwide" && id2 === "yes") {
      
        jQuery("#brandOti").attr('checked',true);
        setAction("addBrandValue");
    }
    else if (id1 === "Ecu Worldwide/OTI" && id2 === "yes") {
      
        jQuery("#brandEcuworldwide").attr('checked',true);
        setAction("addBrandValue");
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
    }
    
}
function fileSed(resend, trnref, index, obj, fileNo) {
    var sedtable = document.getElementById("sedFilingTable");
    var rowObj = sedtable.rows[index];
    var aesStatus = rowObj.cells[6].innerHTML;
    if (undefined !== aesStatus && null !== aesStatus && trim(aesStatus) === 'Sent' && resend !== 'true') {
        alertNew("You can't send AES untill you receive the status for previous");
        return;
    } else {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "validateAesData",
                param1: trnref
            },
            success: function(data) {
                if (null !== data && data !== '') {
                    alertNew(data);
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "checkSchedBAvailability",
                            param1: trnref,
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data) {
                                alertNew("Please Enter Atleast one Sched B Information");
                            } else {
                                jQuery.ajaxx({
                                    data: {
                                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                                        methodName: "createFlatFile",
                                        param1: trnref,
                                        param2: fileNo,
                                        param3: "aes",
                                        request: "true"
                                    },
                                    success: function(data) {
                                        if (null !== data && data === 'Success') {
                                            if (document.getElementById("sedFilingTable") !== null) {
                                                var rowObj = sedtable.rows[index];
                                                rowObj.cells[6].innerHTML = "Sent";
                                                document.getElementById(obj.id).style.backgroundColor = "yellow";
                                                jQuery(rowObj).find("#aesCommentLock").show();
                                                jQuery(rowObj).find("#deleteAesDetails").hide();
                                            }
                                            alertNew("AES Sent Successfully for Transaction Ref# " + trnref);
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
function makeFclBlCorrectionButtonRed() {
    var blNumber = jQuery("#bolId").val();
    var bol = jQuery("#bol").val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "getCorrectionsForThisBL",
            param1: blNumber,
            param2: bol
        },
        success: function(data) {
            if (data !== "") {
                if (data.indexOf(",") > -1) {
                    var result = data.split(",");
                    if (document.getElementById("correctionButton") && document.getElementById("correctionButtonDown")) {
                        if (result[0] === "Corrections Exist") {
                            document.getElementById("correctionButton").className = "buttonColor";
                            document.getElementById("correctionButtonDown").className = "buttonColor";
                        } else {
                            document.getElementById("correctionButton").className = "buttonStyleNew";
                            document.getElementById("correctionButtonDown").className = "buttonStyleNew";
                        }
                    }
                    if (document.getElementById("aesbutton") && document.getElementById("aesbuttonDown")) {
                        if (result[1] === "AES") {
                            document.getElementById("aesbutton").className = "buttoncolor";
                            document.getElementById("aesbuttonDown").className = "buttoncolor";
                        } else {
                            document.getElementById("aesbutton").className = "buttonStyleNew";
                            document.getElementById("aesbuttonDown").className = "buttonStyleNew";
                        }
                    }
                }
                getUpdatedChargesDetailsAfterCorrection();
            }
        }
    });
}
function resendToBlueScreen() {
    var fileNo = jQuery("#fileNo").val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.dwr.DwrUtil",
            methodName: "resendToBlueScreen",
            param1: fileNo,
            request: "true"
        },
        success: function(data) {
            alertNew("BL resend to Blue Screen Submitted Succesfully");
        }
    });
}
function backToBooking() {
    confirmYesOrNo("Are you sure? You want to Delete this BL to BL", "deleteBl");
}
function calculateFAE() {
    var bol = jQuery("#bol").val();
    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "calculateFae",
            forward: "/jsps/FCL/FclBlCharges.jsp",
            param1: bol
        },
        success: function(data) {
            if (data) {
                jQuery("#costCharges").html(data);
                changeBilltoForIncent();
            }

        }
    });
}
function trimSslNum() {
    return jQuery("#booking").val(jQuery("#booking").val().replace(' ', ''));
}

function waterMarkChoiceForcertifiedTrue() {
    jQuery('#certifiedTrueCopyYes').attr("checked", true);
    jQuery('#certifiedTrueCopyYes').val('Yes');
    jQuery('#certifiedTrueCopyNo').attr("checked", false);
    jQuery('#certifiedTrueCopyNo').val('No');
    jQuery('#dockReceiptNo').attr("checked", true);
    jQuery('#dockReceiptNo').val('No');
}

function waterMarkChoiceFordockReceipt() {
    jQuery('#dockReceiptYes').attr("checked", true);
    jQuery('#dockReceiptYes').val('Yes');
    jQuery('#dockReceiptNo').attr("checked", false);
    jQuery('#dockReceiptNo').val('No');
    jQuery('#certifiedTrueCopyNo').attr("checked", true);
    jQuery('#certifiedTrueCopyNo').val('No');
}

function hideAlternatePack() {
    if (jQuery("#noOfPackAlternate").is(":checked")) {
        document.getElementById("noOfPackAlt").style.display = "block";
    } else {
        document.getElementById("noOfPackAlt").style.display = "none";
    }
}

function hideAddAccrual() {
    if (jQuery("#defaultAgentY").is(":checked")) {
        jQuery("#faeAdd").show();
    } else {
        jQuery("#faeAdd").hide();
    }
}
function changeBilltoForIncent() {
    var houseBl = jQuery("#houseBL").val();
    var incentOrFae = jQuery("#faeOrIncent").val();
    if (houseBl != 'C' && incentOrFae == 'INCENT') {
        jQuery('#houseBlB').attr('checked', true);
        jQuery('#billToCodeA').attr('checked', false);
        jQuery('#billToCodeA').attr('disabled', true);
        jQuery('#billToCodeF').attr('checked', false);
        jQuery('#billToCodeF').attr('disabled', true);
        jQuery('#billToCodeS').attr('checked', false);
        jQuery('#billToCodeS').attr('disabled', true);
        jQuery('#billToCodeT').attr('checked', false);
        jQuery('#billToCodeT').attr('disabled', true);
        activeBillThirdParty();
    }
}
function deleteIncentAndAdvSurCharge(incentOrAdvCharg) {
    var bol = jQuery("#bol").val();
    var chargeType = incentOrAdvCharg.toString();
    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "deleteIncentAndAdvsur",
            forward: "/jsps/FCL/FclBlCharges.jsp",
            param1: bol,
            param2: chargeType
        },
        success: function(data) {
            if (data) {
                jQuery("#costCharges").html(data);
                checkForCustomersPresence();//
            }
        }
    });
}
function deleteIncentForthirdParty() {
    var bol = jQuery("#bol").val();
    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.FclDwr",
            methodName: "deleteIncentForThrdParty",
            param1: bol,
            request: "true"
        }
    });
    getUpdatedChargesDetailsAfterCorrection();
}
function noFunctionForHouseBl() {
    var bol = jQuery("#bol").val();
    if (oldhouseBL == 'P') {
        document.getElementById("consigneeCheck").style.visibility = "visible";
        jQuery('#houseBlP').attr('checked', true);
        jQuery('#billToCodeF').attr('disabled', false);
        jQuery('#billToCodeS').attr('disabled', false);
        jQuery('#billToCodeT').attr('disabled', false);
        jQuery('#billToCodeA').attr('checked', false);
        jQuery('#billToCodeA').attr('disabled', true);
        if (oldBillToCode == "billToF") {
            jQuery.ajaxx({
                data: {
                    className: "com.logiware.dwr.FclDwr",
                    methodName: "setBillToParty",
                    forward: "/jsps/FCL/FclBlCharges.jsp",
                    param1: bol,
                    param2: "Forwarder",
                    param3: "P"
                },
                success: function(data) {
                    loadCostAndCharges(data);
                }
            });
        } else if (oldBillToCode == "billToS") {
            jQuery.ajaxx({
                data: {
                    className: "com.logiware.dwr.FclDwr",
                    methodName: "setBillToParty",
                    forward: "/jsps/FCL/FclBlCharges.jsp",
                    param1: bol,
                    param2: "Shipper",
                    param3: "P"
                },
                success: function(data) {
                    loadCostAndCharges(data);
                }
            });
        } else if (oldBillToCode == "billToT") {
            jQuery.ajaxx({
                data: {
                    className: "com.logiware.dwr.FclDwr",
                    methodName: "setBillToParty",
                    forward: "/jsps/FCL/FclBlCharges.jsp",
                    param1: bol,
                    param2: "ThirdParty",
                    param3: "P"
                },
                success: function(data) {
                    loadCostAndCharges(data);
                }
            });
        }
        setBillToCodeForPreviousValue();
    } else if (oldhouseBL == 'C') {
        jQuery('#houseBlC').attr('checked', true);
        jQuery('#billToCodeA').attr('disabled', false);
        jQuery('#billToCodeF').attr('disabled', true);
        jQuery('#billToCodeS').attr('disabled', true);
        jQuery('#billToCodeT').attr('disabled', true);
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "setBillToParty",
                forward: "/jsps/FCL/FclBlCharges.jsp",
                param1: bol,
                param2: "Agent",
                param3: "C"
            },
            success: function(data) {
                loadCostAndCharges(data);
            }
        });
        setBillToCodeForPreviousValue();
    } else if (oldhouseBL == 'B') {
        jQuery('#houseBlB').attr('checked', true);
        jQuery('#billToCodeA').attr('checked', false);
        jQuery('#billToCodeA').attr('disabled', true);
        jQuery('#billToCodeF').attr('checked', false);
        jQuery('#billToCodeF').attr('disabled', true);
        jQuery('#billToCodeS').attr('checked', false);
        jQuery('#billToCodeS').attr('disabled', true);
        jQuery('#billToCodeT').attr('checked', false);
        jQuery('#billToCodeT').attr('disabled', true);
        activeBillThirdParty();
        setBillToCodeForPreviousValue();
    }
}
var oldhouseBL = "";
var oldBillToCode = "";
function onKeyDownForHouseBl() {
    oldhouseBL = jQuery("#houseBL").val();
    if (jQuery("#houseBlP").is(":checked")) {
        oldhouseBL = "P";
        if (jQuery("#billToCodeF").is(":checked")) {
            oldBillToCode = "billToF";
        } else if (jQuery("#billToCodeS").is(":checked")) {
            oldBillToCode = "billToS";
        } else if (jQuery("#billToCodeT").is(":checked")) {
            oldBillToCode = "billToT";
        }
    } else if (jQuery("#houseBlC").is(":checked")) {
        oldhouseBL = "C";
    } else if (jQuery("#houseBlB").is(":checked")) {
        oldhouseBL = "B";
    }
}
function okForReminderExport() {
    document.getElementById("reminderBox").style.display = "none";
    grayOut(false, "");
    manifestExport(customer);
}

function creditStatusBillToBoth() {
    showAlternateMask();
    jQuery("#creditStatus").center().show(500);
}

function hideCreditStatus() {
    jQuery("#creditStatus").hide(500);
    hideAlternateMask();
}

function creditStatusBillTo(customerNumber) {
    var result = "";
    var autoCreditStatus = "";
    if (customerNumber !== '') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "validateCustomer",
                param1: customerNumber,
                param2: "E"
            },
            async: false,
            success: function(data) {
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
        if (result !== "") {
            document.getElementById('warningMessage').innerHTML = result;
        }
        if (autoCreditStatus !== "") {
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
        if (bill[j].value.toLowerCase().match("agent")) {
            arr.push("agent");
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
    if (arr.indexOf("agent") == -1) {
        jQuery("#agentId").hide();
    }
}

function hidehblPlaceReceiptOverride() {
    if (jQuery("#hblPlaceReceiptOverrideYes").is(":checked")) {
        document.getElementById("hblPlaceReceiptId").style.display = "block";
    } else {
        document.getElementById("hblPlaceReceiptId").style.display = "none";
    }
}
function getHouseShipperInfoForPopUp() {
    getMasterShipperData();
}
function getHouseConsigneeInForPopUp() {
    getHouseShipperData();
}
function getForwardingInfoForPopUp() {
    getHouseConsigneeData();
}
function getHouseNotifyPartyInfoForPopUp() {
    getMasterConsigneeData();
}

function getMasterShipperInfoForPopUp() {
    getHouseNotifyPartyData();
}

function getMasterConsigneeInfoForPopUp(){
    getMasterNotifyData();
}

function getMasterNotifyInfoForPopUp() {
    getForwardingData();
}
function getAllRemarksFromPopUp(val) {
     var commentVal = jQuery("#exportReference").val();
     var totalLength = commentVal.length + val.length;
    if (totalLength > 250) {
        alertNew("More than 250 characters are not allowed");
        return;
    }else{
    var oldarray = jQuery("#exportReference").val();
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
 document.getElementById('exportReference').value=oldarray;
 }
}
function checkBrand(val, bolid, companyCode) {

    if (val === "Econo" && companyCode === '03') {
        
        checkpreviousBrandValueEco03(bolid, val);
    } else if (val === "OTI" && companyCode === '02') {
        checkpreviousBrandValueOti02(bolid, val);
    } else if (val === "Ecu Worldwide" && companyCode === '03') {
       
        checkpreviousBrandValueEcu03(bolid, val);
    } else if (val === "Ecu Worldwide" && companyCode === '02') {
        checkpreviousBrandValueEcu03(bolid, val);
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
            param3: 'false'
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

