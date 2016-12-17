/*
 *  Document   : eculineEdi
 *  Author     : Rajesh
 */
var formChanged = false;
$(document).keypress(function (e) {
    if (e.which === 13) {
        search('byUnitNo');
    }
});

$(document).ready(function () {
    fillSsline();
    $("[title != '']").not("link").tooltip();
    detectFormChange();
});
function detectFormChange() {
    var form = "#lclEculineEdiForm";
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
function goBackSearch(path, containerNo, voyNo) {
    if (isFormChanged()) {
        var msg = "Do you want to save the Container changes?";
        $.prompt(msg, {
            buttons: {
                Yes: 1,
                No: 2,
                Cancel: 3
            },
            submit: function (v) {
                if (v === 1) { //save and go back
                    showLoading();
                    var params = $("#lclEculineEdiForm").serialize();
                    $.post(path + "/lclEculineEdi.do?methodName=updateContainer", params, function () {
                        url = path + "/lclEculineEdi.do?methodName=goBackToMainScrn&unitNumber=" + containerNo + "&voyNumber=" + voyNo;
                        document.location.href = url;
                    });
                } else if (v === 2) { //go back
                    showLoading();
                    url = path + "/lclEculineEdi.do?methodName=goBackToMainScrn&unitNumber=" + containerNo + "&voyNumber=" + voyNo;
                    document.location.href = url;
                } else { //do nothing
                    return;
                }
            }
        });
    } else {
        showLoading();
        var url = path + "/lclEculineEdi.do?methodName=goBackToMainScrn&unitNumber=" + containerNo + "&voyNumber=" + voyNo;
        document.location.href = url;
    }
}
function isFormChanged() {
    var form = "#lclEculineEdiForm";
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
    //    $($selector).each(function() {
    //        if ($(this).val() !== $(this).data('initial_value')) {
    //            formChanged = true;
    //        }
    //    });
    return formChanged;
}
function loadNewAgentValues() {
    showLoading();
    $("#methodName").val("updateAgentValue");
    $("#setAgentValFlag").val("true");
    var params = $("#lclEculineEdiForm").serialize();
    $.post($("#lclEculineEdiForm").attr("action"), params,
            function (data) {
                $("#loadDataEculine").html(data);
                closePreloading();
            });
}
function search(searchType) {
    showLoading();
    if (searchType === 'all') {
        $("#methodName").val('display');
        $("#lclEculineEdiForm").submit();
    } else {
        $("#methodName").val('search');
        $("#lclEculineEdiForm").submit();
    }
}

function resetAll() {
    $("#containerNo").val("");
    $("#polUncode").val("");
    $("#podUncode").val("");
    $("#methodName").val('reset');
    $("#lclEculineEdiForm").submit();
}

function getVoyageDetails(path, id, hblNo) {
    showLoading();
    var url = path + "/lclEculineEdi.do?methodName=getVoyageDetails&id=" + id + "&hblNo=" + hblNo;
    document.location.href = url;
}



function viewXmlFile(path, id) {
    var url = path + "/lclEculineEdi.do?methodName=viewXmlFile&id=" + id;
    var xmlWindow = window.open(url, "", "menubar=1,resizable=1,scrollbars=1,left=250,top=30,width=700,height=600");
    xmlWindow.focus();
}

function openBol(path, id, bol, ecuId, readyToApproveFlag) {
    if (isFormChanged()) {
        var msg = "Do you want to save the Container changes?";
        $.prompt(msg, {
            buttons: {
                Yes: 1,
                No: 2,
                Cancel: 3
            },
            submit: function (v) {
                if (v === 1) {
                    showLoading();
                    var params = $("#lclEculineEdiForm").serialize();
                    $.post(path + "/lclEculineEdi.do?methodName=updateContainer", params, function () {
                        var url = path + "/lclEculineEdiBlInfo.do?methodName=openBol&id=" + id + "&ecuId=" + ecuId + "&readyToApproveFlag=" + readyToApproveFlag;
                        document.location.href = url;
                    });
                } else if (v === 2) {
                    showLoading();
                    var url = path + "/lclEculineEdiBlInfo.do?methodName=openBol&id=" + id + "&ecuId=" + ecuId + "&readyToApproveFlag=" + readyToApproveFlag;
                    document.location.href = url;
                }
            }
        });
    } else {
        showLoading();
        var url = path + "/lclEculineEdiBlInfo.do?methodName=openBol&id=" + id + "&ecuId=" + ecuId + "&readyToApproveFlag=" + readyToApproveFlag;
        document.location.href = url;
    }
}

function openInvoice(path, id, bol, fileNumberId, cntrId) {
    if (isFormChanged()) {
        var msg = "Do you want to save the Container changes?";
        $.prompt(msg, {
            buttons: {
                Yes: 1,
                No: 2,
                Cancel: 3
            },
            submit: function (v) {
                if (v === 1) {
                    showLoading();
                    var params = $("#lclEculineEdiForm").serialize();
                    $.post(path + "/lclEculineEdi.do?methodName=updateContainer", params, function () {
                        var url = path + "/lclEculineEdiBlInfo.do?methodName=openInvoice&bol=" + bol + "&id=" + id + "&cntrId=" + cntrId + "&fileNumberId=" + fileNumberId;
                        document.location.href = url;
                    });
                } else if (v === 2) {
                    showLoading();
                    var url = path + "/lclEculineEdiBlInfo.do?methodName=openInvoice&bol=" + bol + "&id=" + id + "&cntrId=" + cntrId + "&fileNumberId=" + fileNumberId;
                    document.location.href = url;
                }
            }
        });
    } else {
        showLoading();
        var url = path + "/lclEculineEdiBlInfo.do?methodName=openInvoice&bol=" + bol + "&id=" + id + "&cntrId=" + cntrId + "&fileNumberId=" + fileNumberId;
        document.location.href = url;
    }
}

function updateBol() {
    showLoading();
    $("#methodName").val("updateBol");
    $("#lclEculineEdiBlInfoForm").submit();
}

function updateContainer(path, containerNo, voyNo) {
    var docReceived = $("#docReceived").val();
    if (docReceived == null || docReceived == "") {
        $.prompt("Doc Received is mandatory");
        return false;
    }
    showLoading();
    $("#methodName").val('updateContainer');
    $("#lclEculineEdiForm").submit();
}
function validateApproveVoy(path, index) {
    var $agentNo = $("#agentNo" + index);
    var $billingTerminal = $("#billingTerminal" + index);
    var $vesselCode = $("#vesselCode" + index);
    var $vesselErrorCheck = $("#vesselErrorCheck" + index);
    var $sslineNo = $("#sslineNo" + index);
    var $warehouseNo = $("#warehouseNo" + index);
    var $masterBl = $("#masterBl" + index);
    var $unitTypeId = $("#unitTypeId" + index);
    var $origin = $("#origin" + index);
    var $destn = $("#destn" + index);
    var $docReceived = $("#docReceived" +index);
    if ($origin.text() === '') {
        confirmVoy(path, "POL is mandatory to approve container.", index);
        $("#warning").parent.show();
        return;
    } else if ($destn.text() === '') {
        confirmVoy(path, "POD is mandatory to approve container.", index);
        $("#warning").parent.show();
        return;
    } else if ($agentNo.val() === '') {
        confirmVoy(path, "Agent number is mandatory to approve container.", index);
        $("#warning").parent.show();
        return;
    } else if ($billingTerminal.val() === '') {
        confirmVoy(path, "Billing terminal is mandatory to approve container.", index);
        $("#warning").parent.show();
        return;
    } else if ($vesselCode.val() === '' && $vesselErrorCheck.val() === 'false') {
        confirmVoy(path, "Vessel code is not mapped to approve container.", index);
        $("#warning").parent.show();
        return;
    } else if ($sslineNo.val() === '') {
        confirmVoy(path, "SS Line # is not mapped to approve container.", index);
        $("#warning").parent.show();
        return;
    } else if ($warehouseNo.val() === '') {
        confirmVoy(path, "Warehouse # is mandatory to approve container.", index);
        $("#warning").parent.show();
        return;
    } else if ($masterBl.val() === '') {
        confirmVoy(path, "Master BL # is mandatory to approve container.", index);
        $("#warning").parent.show();
        return;
    } else if ($unitTypeId.val() === '') {
        confirmVoy(path, "Container Size is not mapped to approve container.", index);
        $("#warning").parent.show();
        return;
    } else if ($docReceived.val() == null || $docReceived.val() == "" || $docReceived.val() == 0) {
        confirmVoy(path, "Doc Received is mandatory to approve container.", index);
        $("#warning").parent.show();
        return;
    }else {
        confirmVoy(path, "All House B/L are not ready to Approve .", index);
        $("#warning").parent.show();
        return;
    }
}
function validateApproveVoyFromContainer(agentNo, billingTerminal, vesselCode, vesselErrorCheck, sslineNo, warehouseNo, masterBl, unitTypeId, origin, destn, polCount, podCount) {
    var docReceived = $("#docReceived").val();
    $("#voy-detail").removeAttr("onclick");
    if (origin === '' && $('#polUncode').val().length === 0) {
        $('#polUncode').css("border-color", "red");
        $.prompt("POL is mandatory to approve container.");
        return;
    } else if (destn === '' && $('#podUncode').val().length === 0) {
        $('#podUncode').css("border-color", "red");
        $.prompt("POD is mandatory to approve container.");
        return;
    } else if (masterBl === '' && $('#masterBl').val().length === 0) {
        $('#masterBl').css("border-color", "red");
        $.prompt("Master BL # is mandatory to approve container.");
        return;
    } else if (agentNo === '' && $('#agentName').val().length === 0) {
        $('#agentName').css("border-color", "red");
        $.prompt("Agent number is mandatory to approve container.");
        return;
    } else if (warehouseNo === '' && $('#warehouseName').val().length === 0) {
        $('#warehouseName').css("border-color", "red");
        $.prompt("Warehouse # is mandatory to approve container.");
        return;
    } else if (billingTerminal === '' && $('#terminal').val().length === 0) {
        $('#terminal').css("border-color", "red");
        $.prompt("Billing terminal is mandatory to approve container.");
        return;
    } else if (unitTypeId === '' && $('#contSize').hasClass('error-indicator')) {
        $.prompt("Container Size is not mapped to approve container.");
        return;
    } else if (vesselCode === '' && vesselErrorCheck === 'false' && $('#vesselName').hasClass('error-indicator')) {
        $.prompt("Vessel code is not mapped to approve container.");
        return;
    } else if (sslineNo === '' && $('#ssline').hasClass('error-indicator')) {
        $.prompt("SS Line # is not mapped to approve container.");
        return;
    } else if (polCount === '0') {
        $.prompt("POL does not exist in the system.");
        return;
    } else if (podCount === '0') {
        $.prompt("POD does not exist in the system.");
        return;
    } else if (docReceived == null || docReceived == "" || docReceived == 0) {
        $.prompt("Doc Received is mandatory to approve container.");
        $("#docReceived").css("border-color", "red");
        return;
    } else if (origin === '' || destn === '' || agentNo === '' || billingTerminal === '' || warehouseNo === '' || masterBl === '' || (vesselCode === '' && vesselErrorCheck === 'false') || sslineNo === '' || unitTypeId === '') {
        $.prompt("Please Save Container.");
        return;
    } else {
        $.prompt(" All House B/L are not ready to Approve .");
        return;
    }
}
function removeErrCheckVessel() {
    if ($("#vesselErrorCheck").attr("checked") === true) {
        $("#vesselName").removeClass("error-indicator");
    } else {
        $("#vesselName").addClass("error-indicator");
    }
}
function removeErrorBorder(id) {
    $("#" + id).removeClass("error-indicator");
}
function removeBorderRedCol(id) {
    $("#" + id).css("border-color", "");
}
function validateApproveBkg(path, index) {
    var contPol = $('#polUncode').val();
    var contPod = $('#podUncode').val();
    var $packageId = $("#packageId" + index);
    var $pod = $("#pod" + index);
    var $pol = $("#pol" + index);
    var $unPolCount = $("#unPolCount" + index);
    var $unPodCount = $("#unPodCount" + index);
    var $unDelCount = $("#unDelCount" + index);
    var $delivery = $("#delivery" + index);
    var polVal, podVal, delivery, pachageType, polpodVal, unLoc = true, polflag = false, podflag = false, delflag = false;
    var str = '', str1 = '', str2 = '', str3 = '', count = 0;
    if ($pod.val() === '') {
        podVal = false;
        podflag = true;
        str = "POD";
        count += 1;
    } else {
        podVal = true;
    }
    if ($pol.val() === '') {
        polVal = false;
        polflag = true;
        if (count > 0) {
            str += " ,";
        }
        str = str + "POL";
        count += 1;
    } else {
        polVal = true;
    }
    if ($delivery.val() === '') {
        delivery = false;
        delflag = true;
        if (count > 0) {
            str += " ,";
        }
        str = str + "Delivery";
        count += 1;
    } else {
        delivery = true;
    }
    if ($packageId.val() === "") {
        pachageType = false;
        str1 = "Package Type should be mapped.<br> ";
        count += 1;
    } else {
        pachageType = true;

    }
    if (contPol !== $pol.val() && contPod !== $pod.val()) {
        polpodVal = false;
        str2 = "POL,POD should match Container POL,POD.<br>";
    } else if (contPol !== $pol.val() && contPod === $pod.val()) {
        polpodVal = false;
        str2 = "POL should match Container POL.<br>";
    } else if (contPod !== $pod.val() && contPol === $pol.val()) {
        polpodVal = false;
        str2 = "POD should match Container POD.<br>";
    } else {
        polpodVal = true;
    }
    if ($unPolCount.val() === '0' && polflag === false) {
        unLoc = false;
        str3 = "POL does not exist in the system.<br>";
    }
    if ($unPodCount.val() === '0' && podflag === false) {
        unLoc = false;
        str3 = str3 + "POD does not exist in the system.<br>";
    }
    if ($unDelCount.val() === '0' && delflag === false) {
        unLoc = false;
        str3 = str3 + "Place Of Delivery does not exist in the system.";
    }
    if (polVal && podVal && delivery && pachageType && polpodVal && unLoc) {
    } else {
        if (str === '') {
        } else {
            str = str + " should not be empty.<br> ";
        }
        confirmation(path, str + str1 + str2 + str3, index);
        $("#warning").parent.show();
        return;
    }
}
function validateApproveBkgFromBLScreen(pol, pod, packageId, delivery, readyToApproveFlag) {
    if (pod === '' && $('#podUncode').val().length === 0) {
        $('#podUncode').css("border-color", "red");
        $.prompt("POD value should not be empty.");
        return;
    } else if (delivery === '' && $('#poddeliveryUncode').val().length === 0) {
        $('#poddeliveryUncode').css("border-color", "red");
        $.prompt("Place of Delivery value should not be empty.");
        return;
    } else if (pol === '' && $('#polUncode').val().length === 0) {
        $('#polUncode').css("border-color", "red");
        $.prompt("POL value should not be empty.");
        return;
    } else if (readyToApproveFlag === false && $('[name="packageDesc"]').hasClass('error-indicator')) {
        $.prompt("Package type not mapped.");
        return;
    } else if (pod === '' || pol === '' || delivery === '' || readyToApproveFlag === false) {
        $.prompt("Please Save BL.");
        return;
    }
}
function validateApproveBol() {
    var $ecuApproved = $("#unitApproved");
    var isPackageMapped = true;
    var $pod = $("#podUncode");
    var $pol = $("#polUncode");
    $(".pId").each(function () {
        if ($(this).val() === '' && isPackageMapped) {
            isPackageMapped = false;
        }
    });
    if ($ecuApproved.val() === '' || $ecuApproved.val() !== '1') {
        $.prompt("Approve container before approving House BL.");
        $("#warning").parent.show();
        return;
    } else if (!isPackageMapped) {
        $.prompt("Package type not mapped.");
        $("#warning").parent.show();
        return;
    } else if ($pod.val() === '') {
        $.prompt("POD value should not be empty.");
        $pod.addClass("error-indicator");
        $("#warning").parent.show();
        return;
    } else if ($pol.val() === '') {
        $.prompt("POL value should not be empty.");
        $pol.addClass("error-indicator");
        $("#warning").parent.show();
        return;
    }
}
function approveVoy(voyNo, index, originId, destinationId, warehouseId, unitTypeId, path) {
    var eculineId = $("#eculineId" + index).val();
    var containerNo = $("#containerNO" + index).val();
    var $docReceived = $("#docReceived" +index);

     if ($docReceived.val() == null || $docReceived.val() == "" || $docReceived.val() == 0) {
        confirmVoy(path, "Doc Received is mandatory to approve container.", index);
        $("#warning").parent.show();
        return;
    }
    if (eculineId !== "" && eculineId !== null && eculineId !== undefined) {

        $.ajaxx({
            dataType: "json",
            data: {
                className : "com.gp.cong.logisoft.struts.action.lcl.LclEculineEdiAction",
                methodName : "scacHbl",
                param1 : eculineId,
                dataType : "json"
            },
            success: function (data) {
                if(data != ""){            //check Dublicate in lclBooking imports Dr#
                    var newData = data;
                    $.prompt(newData+"");
                }
                else{
                    var txt = "Are you sure you want to approve?";
                    $.prompt(txt, {
                        buttons: {
                            Yes: 1,
                            Cancel: 2
                        },
                        submit: function (v) {
                            if (v === 1) {
                                checkUnitAutoCost(originId, destinationId, warehouseId, unitTypeId, voyNo, eculineId, containerNo, "search");
                            } else if (v === 2) {
                                $.prompt.close();
                            }
                        }
                    });
                }
            }
        })
    }
}
function approvingVoy(voyNo, id, cntrNo, screenFlag) {
    showLoading();
    $("#voyNo").val(voyNo);
    if (screenFlag === 'search') {
        $("#id").val(id);
    } else {
        $("#ecuId").val(id);
    }
    $("#containerNo").val(cntrNo);
    $("#methodName").val('approveVoy');
    $("#lclEculineEdiForm").submit();
}

function approveVoyFromContainer(path, voyNo, eculineId, containerNo) {
    var originId = $('#originId').val();
    var destinationId = $('#destinationId').val();
    var unitTypeId = $('#unitTypeId').val();
    var warehouseId = $('#warehouseNo').val();
    var ecuId = $('#ecuId').val();
    var docReceived = $("#docReceived").val();
    if (docReceived == null || docReceived == "") {
        $.prompt("Doc Received is mandatory to approve container.");
        return false;
    } 
    if (ecuId !== "" && ecuId !== null && ecuId !== undefined) {
        $.ajaxx({
            dataType: "json",
            data: {
                className : "com.gp.cong.logisoft.struts.action.lcl.LclEculineEdiAction",
                methodName : "scacHbl",
                param1 : ecuId,
                dataType : "json"
            },
            success: function (data) {
                if(data != ""){               //check Dublicate in lclBooking imports Dr#
                    var newData = data;
                    $.prompt(newData+"");
                }
                else if(isFormChanged()) {
                    var msg = "Do you want to save the Container changes?";
                    $.prompt(msg, {
                        buttons: {
                            Yes: 1,
                            No: 2,
                            Cancel: 3
                        },
                        submit: function (v) {
                            if (v === 1) { //save and approve
                                showLoading();
                                var params = $("#lclEculineEdiForm").serialize();
                                $.post(path + "/lclEculineEdi.do?methodName=updateContainer", params, function () {
                                    $("#voy-detail").removeAttr("onclick");
                                    var txt = "Are you sure you want to approve?";
                                    $.prompt(txt, {
                                        buttons: {
                                            Yes: 1,
                                            Cancel: 2
                                        },
                                        submit: function (v) {
                                            if (v === 1) {
                                                checkUnitAutoCost(originId, destinationId, warehouseId, unitTypeId, voyNo, eculineId, containerNo, "container");
                                            } else {
                                                $.prompt.close();
                                                closePreloading();
                                            }
                                        }
                                    });
                                });
                            } else if (v === 2) { //go back
                                $("#voy-detail").removeAttr("onclick");
                                var txt = "Are you sure you want to approve?";
                                $.prompt(txt, {
                                    buttons: {
                                        Yes: 1,
                                        Cancel: 2
                                    },
                                    submit: function (v) {
                                        if (v === 1) {
                                            checkUnitAutoCost(originId, destinationId, warehouseId, unitTypeId, voyNo, eculineId, containerNo, "container");
                                        } else {
                                            $.prompt.close();
                                            closePreloading();
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    $("#voy-detail").removeAttr("onclick");
                    var txt = "Are you sure you want to approve?";
                    $.prompt(txt, {
                        buttons: {
                            Yes: 1,
                            Cancel: 2
                        },
                        submit: function (v) {
                            if (v === 1) {
                                checkUnitAutoCost(originId, destinationId, warehouseId, unitTypeId, voyNo, eculineId, containerNo, "container");
                            } else {
                                $.prompt.close();
                                closePreloading();
                            }
                        }
                    });
                }
            }
        })
    }
}
function checkUnitAutoCost(originId, destinationId, warehouseId, unitTypeId, voyNo, eculineId, containerNo, screenFlag) {
    showLoading();
    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getEculineAutoCost",
            forward: "/jsps/LCL/lclImpUnitCostEculine.jsp",
            request: true,
            param1: originId,
            param2: destinationId,
            param3: warehouseId,
            param4: unitTypeId,
            param5: voyNo,
            param6: eculineId,
            param7: containerNo,
            param8: screenFlag
        },
        async: false,
        success: function (data) {
            if ($.trim(data) !== "") {
                $("<div style='top:50px; margin-left : 250px; width:500px;height:150px'></div>").html(data).addClass("popup").appendTo("body");
                closePreloading();
            } else {
                approvingVoy(voyNo, eculineId, containerNo, screenFlag);
            }
        }
    });
}

function approveCheckVoy(path, voyNo, index) {
    validateApproveVoy(path, index);
}

function approveBkg(path, id, voyNo, index) {
    if (isFormChanged()) {
        var msg = "Do you want to save the Container changes?";
        $.prompt(msg, {
            buttons: {
                Yes: 1,
                No: 2,
                Cancel: 3
            },
            submit: function (v) {
                if (v === 1) {
                    showLoading();
                    var params = $("#lclEculineEdiForm").serialize();
                    $.post(path + "/lclEculineEdi.do?methodName=updateContainer", params, function () {
                        closePreloading();
                        validateApproveBkg(path, index);
                    });
                } else if (v === 2) {
                    validateApproveBkg(path, index);
                }
            }
        });
    } else {
        validateApproveBkg(path, index);
    }
}
function readyToApproveBl() {
    $.prompt("Approve container for approving House BL.");
}

function approveBol() {
    validateApproveBol();
    showLoading();
    $("#methodName").val("approveBol");
    $("#lclEculineEdiBlInfoForm").submit();
}

function confirmation(path, message, index) {
    var $ecuId = $("#ecuId");
    var $blId = $("#blId" + index);
    var $adjudicated = $("#adjudicated" + index);
    $.prompt(message + "<br/>Do you want to open this HBL to edit?", {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                openBol(path, $blId.val(), "", $ecuId.val(), $adjudicated.val());
            } else {
                $.prompt.close();
            }
        }
    });
}

function confirmVoy(path, message, index) {
    var $ecuId = $("#eculineId" + index);
    $.prompt(message + "<br/>Do you want to open this Container to edit?", {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                getVoyageDetails(path, $ecuId.val(), "");
            } else {
                $.prompt.close();
            }
        }
    });
}

function toggle(selector) {
    $(selector).fadeToggle('slow', 'swing');
}

function fillShipAddress() {
    var address = $('#shipperAddress').val();
    address += ', ' + $('#shipperCity').val();
    address += ', ' + $('#shipperState').val();
    address += ', ' + $('#shipperCountry').val();
    address += ', ' + $('#shipperZip').val();
    $('#shipperNad').val(address);
    $('#shipperAcctName').val($('#shipperContact').val());
    $('#shipperContact').val($('#shipperCode').val());
}

function fillConsAddress() {
    var address = $('#consigneeAddress').val();
    address += ', ' + $('#consigneeCity').val();
    address += ', ' + $('#consigneeState').val();
    address += ', ' + $('#consigneeCountry').val();
    address += ', ' + $('#consigneeZip').val();
    $('#consNad').val(address);
    $('#consAcctName').val($('#consContact').val());
    $('#consContact').val($('#consigneeCode').val());
}

function fillNotify1Address() {
    var address = $('#notify1Address').val();
    address += ', ' + $('#notify1City').val();
    address += ', ' + $('#notify1State').val();
    address += ', ' + $('#notify1Country').val();
    address += ', ' + $('#notify1Zip').val();
    $('#notify1Nad').val(address);
    $('#notify1AcctName').val($('#notify1Contact').val());
    $('#notify1Contact').val($('#notify1Code').val());
}

function fillNotify2Address() {
    var address = $('#notify2Address').val();
    address += ', ' + $('#notify2City').val();
    address += ', ' + $('#notify2State').val();
    address += ', ' + $('#notify2Country').val();
    address += ', ' + $('#notify2Zip').val();
    $('#notify2Nad').val(address);
    $('#notify2AcctName').val($('#notify2Contact').val());
    $('#notify2Contact').val($('#notify2Code').val());
}

function fillSsline() {
    $('#sslineNo').val($('#ssline').val());
    if (document.getElementById('ssLineBluescreenNo') !== null && document.getElementById('ssLineBluescreenNo') !== undefined) {
        document.getElementById('ssLineBluescreenNo').innerHTML = $('#ssLineNumber').val();
    }
}

function getPackageAbbr(id) {
    $('#packageDesc' + id).val($('#packageAbbr' + id).val());
}

function getUnloc(citySelector, descSelector) {
    var unLocCode = $(citySelector).val();
    $(citySelector).val((unLocCode.substring((unLocCode.lastIndexOf("(") + 1), (unLocCode.lastIndexOf(")")))));
    if (descSelector !== '') {
        $(descSelector).val(unLocCode.substring(0, unLocCode.lastIndexOf("(")));
    }
}

function toggleAcct(type) {
    if (type === 'Sh') {
        $('#manualShipper').toggle();
        $('#shipperContact').toggle();
        $('#shipperNad').val('');
        $('#shipperContact').val('');
        $('#manualShipper').val('');
    } else if (type === 'Co') {
        $('#manualCons').toggle();
        $('#consContact').toggle();
        $('#consNad').val('');
        $('#consContact').val('');
        $('#manualCons').val('');
    } else if (type === 'No1') {
        $('#manualNotify1').toggle();
        $('#notify1Contact').toggle();
        $('#notify1Nad').val('');
        $('#notify1Contact').val('');
        $('#manualNotify1').val('');
    } else if (type === 'No2') {
        $('#manualNotify2').toggle();
        $('#notify2Contact').toggle();
        $('#notify2Nad').val('');
        $('#notify2Contact').val('');
        $('#manualNotify2').val('');
    }
}
function toggleAccount() {
    if ($('#chkManualSh').is(':checked')) {
        $('#manualShipper').show();
        $('#shipperContact').hide();
    } else {
        $('#manualShipper').hide();
        $('#shipperContact').show();
    }

    if ($('#chkManualCo').is(':checked')) {
        $('#manualCons').show();
        $('#consContact').hide();
    } else {
        $('#manualCons').hide();
        $('#consContact').show();
    }

    if ($('#chkManualNo1').is(':checked')) {
        $('#manualNotify1').show();
        $('#notify1Contact').hide();
    } else {
        $('#manualNotify1').hide();
        $('#notify1Contact').show();
    }

    if ($('#chkManualNo2').is(':checked')) {
        $('#manualNotify2').show();
        $('#notify2Contact').hide();
    } else {
        $('#manualNotify2').hide();
        $('#notify2Contact').show();
    }
}

function openTradingPartner(path, type) {
    var accountNumber = '';
    var url;
    if (type === 'SH' && $('#chkManualSh').is(':not(:checked)')) {
        accountNumber = $('#shipperContact').val();
    } else if (type === 'CO' && $('#chkManualCo').is(':not(:checked)')) {
        accountNumber = $('#consContact').val();
    } else if (type === 'NO1' && $('#chkManualNo1').is(':not(:checked)')) {
        accountNumber = $('#notify1Contact').val();
    } else if (type === 'NO2' && $('#chkManualNo2').is(':not(:checked)')) {
        accountNumber = $('#notify2Contact').val();
    }

    if (accountNumber === '') {
        url = path + '/tradingPartner.do?buttonValue=removesession';
    } else {
        url = path + '/tradingPartner.do?buttonValue=SearchMachList&account=' + accountNumber;
    }
    $(".tp").attr("href", url);
    $(".tp").colorbox({
        iframe: true,
        width: "100%",
        height: "100 %",
        title: "Trading Partner"
    });
}

function setApprovedMsg() {
    if ($('#approved-msg').text().replace(/\s+/g, '') !== '') {
        setTimeout(function () {
            $("#approved-msg").fadeOut('slow', 'swing');
        }, 1000);
        setTimeout(function () {
            $("#approved-msg").fadeIn('slow', 'swing');
        }, 1100);
    } else {
        $('#approved-msg').hide();
        $('#approved-img').hide();
    }
}
function openVoyage(path, ecuId, headerId, unitId) {
    showLoading();
    var url = path + "/lclEculineEdi.do?methodName=openEciVoyage&id=" + ecuId + "&headerId=" + headerId + "&unitId=" + unitId;
    document.location.href = url;
}

function openFile(path, fileId, fileNumber) {
    showLoading();
    var url = path + "/lclBooking.do?methodName=editBooking&moduleId=" + fileNumber + "&moduleName=Imports&screenName=LCL FILE&fileNumberId=" + fileId;
    document.location.href = url;
}
function openNotes(path, voyNo, number, type) {
    var url = path + "/lclEculineEdi.do?methodName=openNotes&voyNo=" + voyNo;
    var titleText = "<span class='font-trebuchet black'>Notes for ";
    if (type === 'bl') {
        url += "&refNo=" + number;
        titleText += "HBL #"
    } else {
        url += "&containerNo=" + number;
        titleText += "Container #"
    }
    titleText += "</span><span class='red'>" + number + "</span>";
    $(".notes").attr("href", url);
    $(".notes").colorbox({
        iframe: true,
        width: "60%",
        height: "62%",
        title: titleText
    });
}
function cfsAddress() {
    var phone = $('#phone').val();
    var fax = $('#fax').val();
    var cfsaddress = "";
    cfsaddress += $("#cfswarehsAddress").val() + "\n";
    //    cfsaddress += $("#city").val() + " ";
    //  cfsaddress += $("#state").val() + " ";
    //  cfsaddress += $("#zipCode").val() + "\n";
    if (null !== phone && phone !== "") {
        cfsaddress += "Phone:" + $("#phone").val() + "\n";
    }
    if (null !== fax && fax !== "") {
        cfsaddress += "Fax:" + $("#fax").val();
    }
    $("#warehsAddress").val(cfsaddress);
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

function linkDR(path, BlId, houseBlNo, pol, pod, voyNo, contnNo) {
    if (isFormChanged()) {
        var msg = "Do you want to save the Container changes?";
        $.prompt(msg, {
            buttons: {
                Yes: 1,
                No: 2,
                Cancel: 3
            },
            submit: function (v) {
                if (v === 1) {
                    showLoading();
                    var params = $("#lclEculineEdiForm").serialize();
                    $.post(path + "/lclEculineEdi.do?methodName=updateContainer", params, function () {
                        closePreloading();
                        var href = path + "/lclEculineEdiBlInfo.do?methodName=linkDRDisplay&id=" + BlId + "&houseBlNo=" + houseBlNo + "&polUncode=" + pol + "&podUncode=" + pod + "&voyNo=" + voyNo + "&containerNo=" + contnNo;
                        $.colorbox({
                            iframe: true,
                            href: href,
                            width: "45%",
                            height: "60%",
                            title: "Link DR"
                        });
                    });
                } else if (v === 2) {
                    var href = path + "/lclEculineEdiBlInfo.do?methodName=linkDRDisplay&id=" + BlId + "&houseBlNo=" + houseBlNo + "&polUncode=" + pol + "&podUncode=" + pod + "&voyNo=" + voyNo + "&containerNo=" + contnNo;
                    $.colorbox({
                        iframe: true,
                        href: href,
                        width: "45%",
                        height: "60%",
                        title: "Link DR"
                    });
                }
            }
        });
    } else {
        var href = path + "/lclEculineEdiBlInfo.do?methodName=linkDRDisplay&id=" + BlId + "&houseBlNo=" + houseBlNo + "&polUncode=" + pol + "&podUncode=" + pod + "&voyNo=" + voyNo + "&containerNo=" + contnNo;
        $.colorbox({
            iframe: true,
            href: href,
            width: "45%",
            height: "60%",
            title: "Link DR"
        });
    }
}
function shipper_AccttypeCheck() {
    if ($('#shipperDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#shipForwardAcct').val() + "</span>");
        clearShipperDetails();
    } else {
        target = jQuery("#shipper_acct_type").val();
        var type;
        var array1 = new Array();
        if (target !== null) {
            type = target;
            array1 = type.split(",");
        }
        if (target !== "") {
            if (!array1.contains('S')) {
                $.prompt("Please select the customers with account type S");
                clearShipperDetails();
            }
        }
    }
}
function consignee_AccttypeCheck() {
    if ($('#consigneeDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#consigneeForwardAcct').val() + "</span>");
        clearConsigneeDetails();
    } else {
        target = jQuery("#consignee_acct_type").val();
        var type;
        var array1 = new Array();
        if (target !== null) {
            type = target;
            array1 = type.split(",");
        }
        if (target !== "") {
            if (!array1.contains('C')) {
                $.prompt("Please select the customers with account type C");
                clearConsigneeDetails();
            }
        }
    }
}
function notify1_AccttypeCheck() {
    if ($('#notifyDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#notifyForwardAcct').val() + "</span>");
        clearNotify1Details();
    } else {
        target = jQuery("#notify1_acct_type").val();
        var type;
        var array1 = new Array();
        if (target !== null) {
            type = target;
            array1 = type.split(",");
        }
        if (target !== "") {
            if (!array1.contains('C')) {
                $.prompt("Please select the customers with account type C");
                clearNotify1Details();
            }
        }
    }
}
function notify2_AccttypeCheck() {
    if ($('#notify2Disabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#notify2ForwardAcct').val() + "</span>");
        clearNotify2Details();
    } else {
        target = jQuery("#notify2_acct_type").val();
        var type;
        var array1 = new Array();
        if (target !== null) {
            type = target;
            array1 = type.split(",");
        }
        if (target !== "") {
            if (!array1.contains('C')) {
                $.prompt("Please select the customers with account type C");
                clearNotify2Details();
            }
        }
    }
}
//Clear hidden values
function clearShipperDetails() {
    jQuery("#shipperContact").val('');
    jQuery("#shipperNad").val('');
    jQuery("#shipperAcctName").val('');
    jQuery("#shipperCode").val('');
    jQuery("#shipperAddress").val('');
    jQuery("#shipper_acct_type").val('');
    jQuery("#shipperCity").val('');
    jQuery("#shipperState").val('');
    jQuery("#shipperCountry").val('');
    jQuery("#shipperZip").val('');
}
function clearConsigneeDetails() {
    jQuery("#consContact").val('');
    jQuery("#consNad").val('');
    jQuery("#consAcctName").val('');
    jQuery("#consigneeCode").val('');
    jQuery("#consigneeAddress").val('');
    jQuery("#consignee_acct_type").val('');
    jQuery("#consigneeCity").val('');
    jQuery("#consigneeState").val('');
    jQuery("#consigneeCountry").val('');
    jQuery("#consigneeZip").val('');
}
function clearNotify1Details() {
    jQuery("#notify1Contact").val('');
    jQuery("#notify1AcctName").val('');
    jQuery("#notify1Nad").val('');
    jQuery("#notify1Code").val('');
    jQuery("#notify1Address").val('');
    jQuery("#notify1_acct_type").val('');
    jQuery("#notify1City").val('');
    jQuery("#notify1State").val('');
    jQuery("#notify1Country").val('');
    jQuery("#notify1Zip").val('');
}
function clearNotify2Details() {
    jQuery("#notify2Contact").val('');
    jQuery("#notify2AcctName").val('');
    jQuery("#notify2Nad").val('');
    jQuery("#notify2Code").val('');
    jQuery("#notify2Address").val('');
    jQuery("#notify2City").val('');
    jQuery("#notify2State").val('');
    jQuery("#notify2Country").val('');
    jQuery("#notify2_acct_type").val('');
    jQuery("#notify2Zip").val('');
}

function unLinkDR(path, BlId, voyNo, contnNo) {
    if (isFormChanged()) {
        var msg = "Do you want to save the Container changes?";
        $.prompt(msg, {
            buttons: {
                Yes: 1,
                No: 2,
                Cancel: 3
            },
            submit: function (v) {
                if (v === 1) {
                    showLoading();
                    var params = $("#lclEculineEdiForm").serialize();
                    $.post(path + "/lclEculineEdi.do?methodName=updateContainer", params, function () {
                        url = path + "/lclEculineEdi.do?methodName=unLinkDR&blId=" + BlId + "&voyNo=" + voyNo + "&containerNo=" + contnNo;
                        document.location.href = url;
                    });
                } else if (v === 2) {
                    showLoading();
                    url = path + "/lclEculineEdi.do?methodName=unLinkDR&blId=" + BlId + "&voyNo=" + voyNo + "&containerNo=" + contnNo;
                    document.location.href = url;
                }
            }
        });
    } else {
        showLoading();
        url = path + "/lclEculineEdi.do?methodName=unLinkDR&blId=" + BlId + "&voyNo=" + voyNo + "&containerNo=" + contnNo;
        document.location.href = url;
    }
}
