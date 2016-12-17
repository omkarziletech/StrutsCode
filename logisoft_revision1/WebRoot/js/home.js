function openTabs(itemId, selectedMenu, index) {
    if (undefined != index && document.getElementById("tab" + index)) {
        var frameRef = document.getElementById("tab" + index).contentWindow;
        var tab = jQuery("#tab" + index).attr("title");
        var src = jQuery("#src" + index).val();
        if (undefined != tab && (tab == "Quotes" || tab == "IMPORT QUOTE" || tab == "FCL BL" || tab == "IMPORT FCL BL" || tab == "Bookings" || tab == "LCL BL")) {
            alert('Please Click Go Back');
            return;
        }
        if (undefined != tab && tab == "Trading Partner") {
            if (frameRef.frames.length > 0) {
                if (frameRef.frames[0].name == 'geniframe') {
                    alert('Please Click Go Back');
                    return;
                } else {
                    showTabs(itemId, selectedMenu);
                }
            } else {
                showTabs(itemId, selectedMenu);
            }
        } else if (typeof frameRef.catcher == 'function') {
            if (undefined != tab && (tab == "FCL BL" || tab == "IMPORT FCL BL")) {
                frameRef.saveBlOnNavigate();
            }
            if (!(frameRef.catcher())) {
                if (src != null && src != "" && src.indexOf('LCL FILE') > 0) {
                    $('#moduleId').val('LCL FILE');
                }
            }
            if (!(frameRef.catcher())) {
                navigateAway(itemId, selectedMenu, "");
            } else {
                showTabs(itemId, selectedMenu);
            }
        } else {
            showTabs(itemId, selectedMenu);
        }
    } else {
        showTabs(itemId, selectedMenu);
    }
}

function showChild(itemId, selectedMenu) {
    var index = jQuery("ul.htabs li.selected").find("a").attr("tabindex");
    if ($('#followupFlag').val() === "true") {
        jQuery.prompt("Did you review your follow up notes?", {
            buttons: {
                Yes: true,
                No: false
            },
            submit: function (v) {
                if (v) {
                    openTabs(itemId, selectedMenu, index);
                } else {
                    $('#followupFlag').val(false);
                }
            }
        });
    } else {
        openTabs(itemId, selectedMenu, index);
    }
}
function navigateAway(id, menu, tabName) {
    itemId = id;
    selectedMenu = menu;
    confirmYesOrNo('One or more form values on this page have changed ' + tabName + '. Are you sure you want to navigate away from this page', "Navigate");
}
function showTabs(id, menu) {
    document.homeForm.action.value = "showTabs";
    document.homeForm.itemId.value = id;
    document.homeForm.selectedMenu.value = menu;
    document.homeForm.submit();
}
function confirmNavigateFunction(id1, id2) {
    if (id2 == 'no') {
        //no need do anything
    } else if (id1 == 'Navigate' && id2 == 'yes') {
        document.homeForm.action.value = "showTabs";
        document.homeForm.itemId.value = itemId;
        document.homeForm.selectedMenu.value = selectedMenu;
        document.homeForm.submit();
    }
}
function changeChilds(path, recordId, moduleId) {
    showPreloading();
    var startsWith = path.substring(0, 1);
    if (startsWith.indexOf("&") < 0) {
        startsWith = "&";
    } else {
        startsWith = ""
    }
    window.location = rootPath + "/home.do?action=changeTabs&itemId=" + document.homeForm.itemId.value + "&selectedMenu=" + document.homeForm.selectedMenu.value + startsWith + path + "&recordId=" + recordId + "&moduleId=" + moduleId;
}
function changeLclChilds(path, fileId, moduleId, moduleName, callBack, unitSsId, lclFileType, fileNumber) {
    showPreloading();
    var startsWith = path.substring(0, 1);
    if (startsWith.indexOf("&") < 0) {
        startsWith = "&";
    } else {
        startsWith = ""
    }
    window.location = rootPath + "/home.do?action=changeTabs&itemId=" + document.homeForm.itemId.value + "&selectedMenu=" + moduleName + startsWith + path + "&recordId=" + fileId + "&moduleId=" + moduleId + "&callBack=" + callBack + "&unitSsId=" + unitSsId + "&lclFileType=" + lclFileType + "&fileNumber=" + fileNumber;
}
function changeShortShipmentMenu(path, shortShipFileNo, voyageId, filterBy, detailId, unitSsId, toScreen, fromScreen) {
    showPreloading();
    var startsWith = path.substring(0, 1);
    if (startsWith.indexOf("&") < 0) {
        startsWith = "&";
    } else {
        startsWith = ""
    }
    window.location = rootPath + "/home.do?action=changeTabs&itemId=" + document.homeForm.itemId.value + "&recordId="
            + "&selectedMenu=Exports" + startsWith + path + "&moduleId=B&callBack=shortShipFile&pickVoyId=" + voyageId
            + "&toScreenName=" + toScreen + "&fromScreen=" + fromScreen + "&inTransitDr=false"
            + "&unitSsId=" + unitSsId + "&shortShipfileNo=" + shortShipFileNo + "&detailId=" + detailId + "&filterByChanges=" + filterBy;
}
function changeLclUnitsSchedule(path, fileNumber, moduleId, pickVoyId, detailId, filterByChanges) {
    showProgressBar();
    var startsWith = path.substring(0, 1);
    if (startsWith.indexOf("&") < 0) {
        startsWith = "&";
    } else {
        startsWith = ""
    }
    window.location = rootPath + "/home.do?action=changeTabs&itemId=" + document.homeForm.itemId.value + "&selectedMenu=" + document.homeForm.selectedMenu.value + startsWith + path + "&recordId=" + fileNumber + "&moduleId=" + moduleId
            + "&pickVoyId=" + pickVoyId + "&detailId=" + detailId + "&filterByChanges=" + filterByChanges;
}

function goToConsolidatePage(path, fileId, fileState, moduleName, toScreenName, fromScreenName, fromFileId) {
    showPreloading();
    var startsWith = path.substring(0, 1);
    if (startsWith.indexOf("&") < 0) {
        startsWith = "&";
    } else {
        startsWith = ""
    }
    var href = rootPath + "/home.do?action=changeTabs&itemId=" + document.homeForm.itemId.value + "&selectedMenu="
            + moduleName + startsWith + path + "&recordId=" + fileId + "&moduleId=" + fileState +
            "&toScreenName=" + toScreenName + "&fromScreen=" + fromScreenName + "&consolidateId=" + fromFileId;
    window.location = href;
}

function goBack(path, fileNumber, moduleId, screenName) {
    showLoading();
    var startsWith = path.substring(0, 1);
    if (startsWith.indexOf("&") < 0) {
        startsWith = "&";
    } else {
        startsWith = ""
    }
    window.location = path + "/home.do?action=changeTabs&itemId=" + document.homeForm.itemId.value + "&selectedMenu=" + document.homeForm.selectedMenu.value + startsWith + path + "&recordId=" + fileNumber + "&moduleId=" + moduleId + "&screenName=" + screenName;
}

function changeLclScheduleChilds(moduleId) {
    window.location = rootPath + "/home.do?action=changeTabs&itemId=" + document.homeForm.itemId.value + "&moduleId=" + moduleId;
}
function changeChildsFromDisputedBl(path, recordId, moduleId, itemId, selectedMenu) {
    showPreloading();
    var startsWith = path.substring(0, 1);
    if (startsWith.indexOf("&") < 0) {
        startsWith = "&";
    } else {
        startsWith = ""
    }
    window.location = rootPath + "/home.do?action=changeTabs&itemId=" + itemId + "&selectedMenu=" + selectedMenu + startsWith + path + "&recordId=" + recordId + "&moduleId=" + moduleId;
}
function changeChildsByDesc(path) {
    showPreloading();
    var startsWith = path.substring(0, 1);
    if (startsWith.indexOf("&") < 0) {
        startsWith = "&";
    } else {
        startsWith = ""
    }
    window.location = rootPath + "/home.do?action=changeTabsByTabName&itemId=" + document.homeForm.itemId.value + "&selectedMenu=" + document.homeForm.selectedMenu.value + startsWith + path;
}
function showHome(homeScreenFileFlag, fileNumber, moduleId) {
    var index = jQuery("ul.htabs li.selected").find("a").attr("tabindex");
    if (homeScreenFileFlag !== "true") {
        if (undefined != index && document.getElementById("tab" + index)) {
            var tab = jQuery("#tab" + index).attr("title");
            if (undefined != tab && (tab == "Trading Partner" || tab == "Quotes" || tab == "IMPORT QUOTE" || tab == "FCL BL" || tab == "IMPORT FCL BL" || tab == "Bookings" || tab == "LCL BL")) {
                alert('Please Click Go Back');
                return;
            }
        }
    } else {
        document.homeForm.fileNumber.value = fileNumber;
        document.homeForm.moduleId.value = moduleId;
    }
    document.homeForm.action.value = "showHome";
    document.homeForm.itemId.value = "";
    document.homeForm.selectedMenu.value = "";
    document.homeForm.submit();
}

function logout() {
    window.location = rootPath + "/logout.do";
}
var links = document.getElementsByTagName("a");
for (var i = 0; i < links.length; i++) {
    links[i].onmouseover = function () {
        window.status = '';
        return true;
    }
}
var quoteCache = new Hashtable();
var bookingCache = new Hashtable();
var blCache = new Hashtable();
var dataStorage = new Hashtable();
function makeFormBorderless(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "text" || element.type == "textarea" || element.type == "select-one") {
            element.style.border = 0;
            if (element.type == "select-one") {
                element.disabled = true;
            }
            element.style.backgroundColor = "#CCEBFF";
            element.readOnly = true;
            element.tabIndex = -1;
            element.className = "textlabelsBoldForTextBox";
        } else if (element.type == "checkbox" || element.type == "radio") {
            element.style.border = 0;
            element.disabled = true;
            element.className = "textlabelsBoldForTextBox";
        } else if (element.type == "button") {
            if (element.value == "Save") {
                element.style.visibility = "hidden";
            }
        }
    }
    return false;
}

// Create a 'get' query string with the data from a given form
function gatherFormData(form, cacheFor) {
    var element;
    // For each form element, extract the name and value
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "text" || element.type == "password" || element.type == "textarea" || element.type == "hidden") {
            dataStorage.put(element.name, escape(element.value));
        } else if (element.type.indexOf("select") != -1) {
            for (var j = 0; j < element.options.length; j++) {
                if (element.options[j].selected == true) {
                    dataStorage.put(element.name, element.options[element.selectedIndex].value);
                }
            }
        } else if (element.type == "checkbox" && element.checked) {
            dataStorage.put(element.name, element.value);
        } else if (element.type == "radio" && element.checked == true) {
            dataStorage.put(element.name, element.value);
        }
    }

    if (cacheFor == 'QUOTE') {
        quoteCache = dataStorage;
    } else if (cacheFor == 'BOOKING') {
        bookingCache = dataStorage;
    } else if (cacheFor == 'BL') {
        blCache = dataStorage;
    }
    return false;
}

function restoreFormData(form, cacheFor) {
    var element;
    if (cacheFor == 'QUOTE') {
        dataStorage = quoteCache;
    } else if (cacheFor == 'BOOKING') {
        dataStorage = bookingCache;
    } else if (cacheFor == 'BL') {
        dataStorage = blCache;
    }
    if (dataStorage.size() == 0) {
        return false;
    }
    // For each form element, extract the name and value
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (unescape(dataStorage.get(element.name)) != "undefined") {
            if (element.type == "text" || element.type == "password" || element.type == "textarea" || element.type == "hidden") {
                element.value = unescape(dataStorage.get(element.name));
            } else if (element.type.indexOf("select") != -1) {
                for (var j = 0; j < element.options.length; j++) {
                    if (element.options[j].value == unescape(dataStorage.get(element.name))) {
                        element.selectedIndex = j;
                    }
                }
            } else if (element.type == "checkbox" && element.checked) {
                element.value = unescape(dataStorage.get(element.name));
            } else if (element.type == "radio" && element.checked == true) {
                element.value = unescape(dataStorage.get(element.name));
            }
        }
    }
    return false;
}

function disableFieldsWhileLocking(form) {
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
            element.className = "textlabelsBoldForTextBox";
        } else if (element.type == "button") {
            if (element.value != "Go Back") {
                element.style.visibility = "hidden";
            }
        }
    }
    return false;
}

jQuery(document).ready(function () {
    var initial = Number(jQuery("#selectedTab").val());
    jQuery("ul.htabs").tabs("> .pane", {
        effect: 'fade',
        current: 'selected',
        initialIndex: initial,
        onClick: function () {
            var index = jQuery("ul.htabs li.selected").find("a").attr("tabindex");
            var indexCount = new Array();
            $("ul.htabs li").find("a").each(function () {
                indexCount.push($(this).text());
            });
            if (indexCount.length > 1) {
                jQuery("ul.htabs li").css("background", "#C0C0C0");
                jQuery("ul.htabs li.selected").css("background", "#22AC4E");
            }
            var src = jQuery("#src" + index).val();
            if (jQuery("#tab" + index).attr("src") == '') {
                jQuery("#tab" + index).attr("src", src);
                showPreloading();
            }
            jQuery("#tab" + index).height(jQuery(document).height() - jQuery("#header-container").height() - jQuery("#footer-container").height() - 40);
        }
    });
    if (jQuery("#serverStatus").length > 0) {
        setInterval(function () {
            jQuery("#serverStatus").load(rootPath + "/home.do?action=showServerStatus");
        }, 1000);
    }
    if (jQuery(":radio[name='showNotes']").length > 0) {
        jQuery(":radio[name='showNotes']").click(function () {
            var url = rootPath + "/home.do?action=showFollowUpTasks&showNotes=" + jQuery(":radio[name='showNotes']:checked").val();
            jQuery("#followUpTasks").load(url);
        });
    }
    Lightbox.initialize({
        color: 'black',
        dir: rootPath + '/js/lightbox/images',
        moveDuration: 1,
        resizeDuration: 1
    });
});

function changeHeight() {
    jQuery("ul.htabs li").find("a").each(function () {
        var index = jQuery(this).attr("tabindex");
        jQuery("#tab" + index).height(jQuery(document).height() - jQuery("#header-container").height() - jQuery("#footer-container").height() - 40);
    });
}

function addNewCustomer(fromField, accountName, accountNo, callFrom) {
    var index = jQuery("ul.htabs li.selected").find("a").attr("tabindex");
    var frameRef = document.getElementById("tab" + index).contentWindow;
    // var frameRef = document.frames[index];
    if (callFrom != null && callFrom != "" && callFrom != undefined && callFrom.length > 3 && callFrom.substring(0, 3) == "LCL") {
        frameRef.setNewShipperInfoDetails(accountName, accountNo, callFrom);
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'shipperName') {
        frameRef.document.EditBookingsForm.shipperName.value = accountName;
        frameRef.document.EditBookingsForm.shipper.value = accountNo;
        frameRef.getShipperInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'fowardername') {
        frameRef.document.EditBookingsForm.fowardername.value = accountName;
        frameRef.document.EditBookingsForm.forwarder.value = accountNo;
        frameRef.getForwarderInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'consigneename') {
        frameRef.document.EditBookingsForm.consigneename.value = accountName;
        frameRef.document.EditBookingsForm.consignee.value = accountNo;
        frameRef.getConsigneeInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'spottingAccountName') {
        frameRef.document.EditBookingsForm.spottingAccountName.value = accountName;
        frameRef.document.EditBookingsForm.spottingAccountNo.value = accountNo;
        frameRef.getSpottingInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'truckerName') {
        frameRef.document.EditBookingsForm.truckerName.value = accountName;
        frameRef.document.EditBookingsForm.truckerCode.value = accountNo;
        frameRef.getTruckerInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'shipperNameBL') {
        frameRef.document.fclBillLaddingform.accountName.value = accountName;
        frameRef.document.getElementById("shipper").value = accountNo;
        frameRef.document.getElementById("houseShipperAddress").value = "";
        frameRef.getHouseShipperInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'consigneeNameBL') {
        frameRef.document.fclBillLaddingform.consigneeName.value = accountName;
        frameRef.document.getElementById("consignee").value = accountNo;
        frameRef.document.getElementById("streamShipConsignee").value = "";
        frameRef.getHouseConsigneeInForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'forwardingAgentNameBL') {
        frameRef.document.fclBillLaddingform.forwardingAgentName.value = accountName;
        frameRef.document.getElementById("forwardingAgent1").value = accountNo;
        frameRef.document.getElementById("forwardingAgentno").value = "";
        frameRef.getForwardingInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'notifyPartyNameBL') {
        frameRef.document.fclBillLaddingform.notifyPartyName.value = accountName;
        frameRef.document.getElementById("notifyParty").value = accountNo;
        frameRef.document.getElementById("streamshipNotifyParty").value = "";
        frameRef.getHouseNotifyPartyInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'shipperNameMasterBL') {
        frameRef.document.fclBillLaddingform.houseName.value = accountName;
        frameRef.document.fclBillLaddingform.houseShipper.value = accountNo;
        frameRef.document.fclBillLaddingform.houseShipper1.value = "";
        frameRef.getMasterShipperInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'houseConsigneeNameBL') {
        frameRef.document.fclBillLaddingform.houseConsigneeName.value = accountName;
        frameRef.document.getElementById("houseConsignee").value = accountNo;
        frameRef.document.getElementById("houseConsignee1").value = "";
        frameRef.getMasterConsigneeInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'houseNotifyPartyNameBL') {
        frameRef.document.fclBillLaddingform.houseNotifyPartyName.value = accountName;
        frameRef.document.getElementById("houseNotifyParty").value = accountNo;
        frameRef.document.getElementById("houseNotifyPartyaddress").value = "";
        frameRef.getMasterNotifyInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'blMasterShipper') {
        frameRef.document.getElementById("houseName").value = accountName;
        frameRef.document.getElementById("houseShipper").value = accountNo;
        frameRef.document.getElementById("houseShipper1").value = "";
        frameRef.getHouseShipperInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'blHouseShipper') {
        frameRef.document.getElementById("accountName").value = accountName;
        frameRef.document.getElementById("shipper").value = accountNo;
        frameRef.document.getElementById("streamShip").value = "";
        frameRef.getHouseConsigneeInForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'blHouseConsignee') {
        frameRef.document.getElementById("consigneeName").value = accountName;
        frameRef.document.getElementById("consignee").value = accountNo;
        frameRef.document.getElementById("streamShipConsignee").value = "";
        frameRef.getForwardingInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'blMasterConsignee') {
        frameRef.document.getElementById("houseConsigneeName").value = accountName;
        frameRef.document.getElementById("houseConsignee").value = accountNo;
        frameRef.document.getElementById("houseConsignee1").value = "";
        frameRef.getHouseNotifyPartyInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'blHouseNotify') {
        frameRef.document.getElementById("notifyPartyName").value = accountName;
        frameRef.document.getElementById("notifyParty").value = accountNo;
        frameRef.document.getElementById("streamshipNotifyParty").value = "";
        frameRef.getMasterShipperInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'blMasterNotify') {
        frameRef.document.getElementById("houseNotifyPartyName").value = accountName;
        frameRef.document.getElementById("houseNotifyParty").value = accountNo;
        frameRef.document.getElementById("houseNotifyPartyaddress").value = "";
        frameRef.getMasterConsigneeInfoForPopUp();
    } else if (fromField != undefined && fromField != null && fromField != '' && fromField == 'blForwarder') {
        frameRef.document.getElementById("forwardingAgentName").value = accountName;
        frameRef.document.getElementById("forwardingAgent1").value = accountNo;
        frameRef.document.getElementById("forwardingAgentno").value = "";
        frameRef.getMasterNotifyInfoForPopUp();
    }
    GB_hide();
}

function showArInquiry(customerNumber) {
    document.homeForm.action.value = "gotoArInquiry";
    document.homeForm.customerNumber.value = customerNumber;
    document.homeForm.submit();
}
function showArInquiryForLcl(fileNo, toScreenName) {
    document.homeForm.action.value = "gotoArInquiry";
    document.homeForm.toScreenName.value = toScreenName;
    document.homeForm.fileNumber.value = fileNo;
    document.homeForm.submit();
}

function removeFollowUpTask(followUpTaskId) {
    jQuery.prompt("Do you want to remove this followup?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v, m, f) {
            if (v) {
                var url = rootPath + "/home.do?action=removeFollowUpTask&showNotes=" + jQuery(":radio[name='showNotes']:checked").val();
                url += "&followUpTaskId=" + followUpTaskId;
                jQuery("#followUpTasks").load(url);
            }
        }
    });
}

function getFrameHeight() {
    var index = jQuery("ul.htabs li.selected").find("a").attr("tabindex");
    return jQuery("#tab" + index).height();
}

function showGreyBox(caption, path) {
    var height = jQuery(document).height() - 50;
    var width = jQuery(document).width() - 100;
    var version = jQuery.browser.version;
    if (jQuery.browser.msie && parseFloat(version) >= 9.0) {
        window.open(path, "mywindow", "menubar=1,resizable=1,width=" + width + ",height=" + height);
    } else {
        GB_show(caption, path, height, width);
    }
}

function showLightBox(title, url, height, width) {
    height = height || jQuery(document).height() - 50;
    width = width || jQuery(document).width() - 100;
    url += (url.indexOf("?") > -1 ? "&" : "?") + "TB_iframe&height=" + height + "&width=" + width;
    Lightbox.showPopUp(title, url, "sexylightbox", "", "");
}

function gotoTradingPartner(accountNumber, selectedTab) {
    window.location = rootPath + "/home.do?action=gotoTradingPartner&accountNumber=" + accountNumber + "&selectedTab=" + selectedTab;
}

function gotoFclOpsScreens(fileNumber, itemId, fclFileType) {
    showPreloading();
    var url = rootPath + "/home.do?action=gotoFclOpsScreens";
    url += "&itemId=" + (($.trim(itemId) != "") ? itemId : jQuery("#itemId").val());
    url += "&fileNumber=" + fileNumber;
    url += "&selectedMenu=" + jQuery("#selectedMenu").val();
    url += "&fclFileType=" + fclFileType;
    window.location = url;
}
function gotoDomestBookingScreens(bookingNo, itemId) {
    showPreloading();
    var url = rootPath + "/home.do?action=gotoDomestBookingScreens";
    url += "&itemId=" + (($.trim(itemId) != "") ? itemId : jQuery("#itemId").val());
    url += "&bookingNumber=" + bookingNo;
    url += "&selectedMenu=" + jQuery("#selectedMenu").val();
    window.location = url;
}

function gotoFclSearchScreen() {
    showPreloading();
    var url = rootPath + "/home.do?action=gotoFclSearchScreen";
    url += "&itemId=" + jQuery("#itemId").val();
    url += "&selectedMenu=" + jQuery("#selectedMenu").val();
    window.location = url;
}

function gotoNewFclScreen(tabName) {
    showPreloading();
    var url = rootPath + "/home.do?action=gotoNewFclScreen";
    url += "&itemId=" + jQuery("#itemId").val();
    url += "&tabName=" + tabName;
    url += "&selectedMenu=" + jQuery("#selectedMenu").val();
    window.location = url;
}

function getValueFromChild(childId, fieldId) {
    return jQuery(childId).contents().find(fieldId).val();
}

function goToBookingFromVoyage(path, fileId, filter, voyageId, detailId, unitSsId, fromScreen, toscreen, intransitDr, callBack) {
    showLoading();
    var startsWith = path.substring(0, 1);
    if (startsWith.indexOf("&") < 0) {
        startsWith = "&";
    } else {
        startsWith = "";
    }
    window.location = rootPath + "/home.do?action=changeTabs&itemId=" + document.homeForm.itemId.value
            + "&selectedMenu=Exports" + "&recordId=" + fileId + "&pickVoyId="
            + voyageId + "&detailId=" + detailId + "&filterByChanges=" + filter
            + "&unitSsId=" + unitSsId + "&toScreenName=" + toscreen + "&fromScreen="
            + fromScreen + "&inTransitDr=" + intransitDr + "&callBack=" + callBack;
}

function goToVoyageFromDisputed(path, headerId, filter, fromScreen, toscreen) {
    showLoading();
    var startsWith = path.substring(0, 1);
    if (startsWith.indexOf("&") < 0) {
        startsWith = "&";
    } else {
        startsWith = "";
    }
    window.location = rootPath + "/home.do?action=changeTabs&itemId=" + document.homeForm.itemId.value + "&selectedMenu=" + document.homeForm.selectedMenu.value + startsWith + path
            + "&pickVoyId=" + headerId + "&filterByChanges=" + filter + "&toScreenName=" + toscreen + "&fromScreen=" + fromScreen;
}

function navigateExportImportScreen(fileId, filter, voyageId, detailId,
        unitSsId, fromScreen, toscreen, callback) {
    showLoading();
    window.location = rootPath + "/home.do?action=changeTabs&itemId=" + document.homeForm.itemId.value
            + "&selectedMenu=Exports" + "&recordId=" + fileId + "&pickVoyId=" + voyageId
            + "&detailId=" + detailId + "&filterByChanges=" + filter + "&unitSsId=" + unitSsId
            + "&toScreenName=" + toscreen + "&fromScreen=" + fromScreen + "&callBack=" + callback;
}

function jump(module, reference) {
    document.homeForm.action.value = "jump";
    document.homeForm.module.value = module;
    document.homeForm.reference.value = reference;
    document.homeForm.submit();
}

function jump2view() {
    document.homeForm.action.value = "jump";
    document.homeForm.accessMode.value = "view";
    document.homeForm.submit();
}