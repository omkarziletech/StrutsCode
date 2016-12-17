
function openLoad(path) {
    var headerId = $('#headerId').val();
    var detailId = $('#detailId').val();
    var filterByChanges = $('#filterByChanges').val();
    var orgnId = $('#orgnId').val();
    var fdId = $('#fdId').val();
    var href = path + "/lclAddVoyage.do?methodName=openUnits&originId=" + orgnId + "&finalDestinationId=" + fdId
            + "&headerId=" + headerId + "&detailId=" + detailId + "&displayLoadComplete=N&filterByChanges=" + filterByChanges + "";
    $.colorbox({
        iframe: true,
        href: href,
        width: "100%",
        height: "90%",
        title: "Stuffing"
    });
}
function showStyleArinvoiceForVoyage() {
    var fileId = $('#headerId').val();
    if (fileId != "") {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "verifyArinvoice",
                param1: fileId,
                dataType: "json"
            },
            success: function (data) {
                if (data === 'posted') {
                    $("#arInvoice").addClass('green-background');
                } else if (data === 'unPosted') {
                    $("#arInvoice").addClass('red-background');
                }
            }
        });
    }
}
function addDetails(path) {
    var href = path + "/lclAddVoyage.do?methodName=openSSDetailsPopup&filterByChanges=" + document.getElementById('filterByChanges').value + "&podDestination=" + $("#podDestination").val();
    $.colorbox({
        iframe: true,
        href: href,
        width: "80%",
        height: "80%",
        title: "Add Detail"
    });
}


function validateVoyage() {
    var originalClose = $.colorbox.close;
    $.colorbox.close = function () {
        var $frame = $(".cboxIframe").contents();
        if ($("#filterByChanges").val() == 'lclDomestic' && $("#unitNo").val() !== '') {
            $frame.find("body").append(
                    $.prompt("Unit will not be Saved , if you discard the Voyage. Do you want to continue?", {
                        buttons: {
                            Yes: 1,
                            No: 2
                        },
                        submit: function (v) {
                            if (v == 1) {
                                originalClose();
                            }
                        }
                    }));
        } else {
            originalClose();
        }
    }
}
function addDetailsOnload(path) {
    validateVoyage();
    var podName = $("#podDestination").val();
    var href = path + "/lclAddVoyage.do?methodName=openSSDetailsPopup&filterByChanges=" + document.getElementById('filterByChanges').value + "&podDestination=" + podName;
    $.colorbox({
        iframe: true,
        href: href,
        width: "80%",
        height: "80%",
        title: "Add Detail",
        onClosed: function () {
            goBackToVoyageScreen(path);
        }
    });
}
function editDetails(path, detailId) {
    var href = path + "/lclAddVoyage.do?methodName=editVoyageDetail&detailId=" + detailId + "&filterByChanges=" + $('#filterByChanges').val();
    $.colorbox({
        iframe: true,
        href: href,
        width: "80%",
        height: "80%",
        title: "Edit Detail"
    });
}
function addSSMasterDetails(path) {
    var headerId = document.getElementById('headerId').value;
    var finalDestinationId = $("#finalDestinationId").val();
    var originUnlocCode = getUnlocationCode("originalOriginName");
    var fdUnlocCode = getUnlocationCode("originalDestinationName");
    var href = path + "/lclAddVoyage.do?methodName=openSSMasterPopup&headerId=" + headerId + "&finalDestinationId=" + finalDestinationId + "&polUnlocationCode=" + originUnlocCode + "&fdUnlocationCode=" + fdUnlocCode;
    $.colorbox({
        iframe: true,
        href: href,
        width: "90%",
        height: "80%",
        title: "SS Master Details"
    });
}
function addUnits(path) {
    var headerId = document.getElementById('headerId').value;
    var href = path + "/lclAddUnits.do?methodName=editUnits&headerId=" + headerId;
    $.colorbox({
        iframe: true,
        href: href,
        width: "100%",
        height: "90%",
        title: "<span style=color:blue>Add Unit</span>"
    });
}
function editUnits(path, unitId) {
    var href = path + "/lclAddUnits.do?methodName=editUnits&unitId=" + unitId;
    $("#editUnits").attr("href", href);
    $("#editUnits").colorbox({
        iframe: true,
        width: "100%",
        height: "90%",
        title: "Edit Unit"
    });
}
function editVoyage(path, headerId) {
    var podId = document.getElementById("finalDestinationId").value;
    var href = path + "/lclAddVoyage.do?methodName=editVoyageHeader&headerId=" + headerId + "&finalDestinationId=" + podId;
    $("#imgeditheader").attr("href", href);
    $("#imgeditheader").colorbox({
        iframe: true,
        width: "80%",
        height: "40%",
        title: "Edit Header"
    });
}
function goBackToVoyageScreen(path) {
    var filterByChanges = $('#filterByChanges').val();
    var cfclAcctName = $("#cfclAcctName").val();
    var cfclAcctNo = $("#cfclAcctNo").val();
    var searchLoadDisplay = $('#searchLoadDisplay').val();
    var unitVoyageSearch = $('#unitVoyageSearch').val();
    var unCompleteUnitCheck = $("#showUnCompleteUnits").val() === 'true' ? "on" : "off";
    var isContainerFlag = $("#searchLclContainerSize").val() === 'true' ? true : false;
    var searchOriginId = "";
    var searchFdId = "";
    var searchOrigin = "";
    var searchFd = "";
    var finalURL = "";
    var filterByNewValue = $('#filterByNewValue').val();
    if (filterByNewValue === "currentProcess") {
        filterByChanges = "currentProcess";
        searchOriginId = $('#searchOriginId').val();
        searchFdId = $('#searchFdId').val();
        searchOrigin = $('#searchOrigin').val();
        searchFd = $('#searchFd').val();
    } else {
        searchOriginId = $('#searchOriginId').val() !== '' ? $('#searchOriginId').val() : $('#originalOriginId').val();
        searchFdId = $('#searchFdId').val() !== '' ? $('#searchFdId').val() : $('#originalDestinationId').val();
        searchOrigin = $('#searchOrigin').val() !== '' ? $('#searchOrigin').val() : $('#originalOriginName').val();
        searchFd = $('#searchFd').val() !== '' ? $('#searchFd').val() : $('#originalDestinationName').val();
    }
    if (searchLoadDisplay == 'Y' || searchLoadDisplay == 'N' || searchLoadDisplay == "empty") {
        finalURL = path + "/lclSearch.do?methodName=goBackToSearchList";
    } else {
        var searchVoyageNo = $('#searchVoyageNo').val();
        var searchUnitNo = $('#searchUnitNo').val();
        var voyageNo = $('#schedule').val();
        finalURL = path + "/lclUnitsSchedule.do?methodName=goBack&portOfOriginId=" + searchOriginId + "&finalDestinationId=" + searchFdId + "&filterByChanges=" + filterByChanges
                + "&origin=" + searchOrigin + "&destination=" + searchFd + "&voyageNo=" + searchVoyageNo + "&showUnCompleteUnits=" + unCompleteUnitCheck
                + "&cfclAcctName=" + cfclAcctName + "&cfclAcctNo=" + cfclAcctNo + "&unitVoyageSearch=" + unitVoyageSearch + "&unitNo=" + searchUnitNo
                + "&goBackVoyNo=" + voyageNo + "&serviceType=" + $('#serviceType').val() + "&isLclContainerSize=" + isContainerFlag;
    }
    document.location.href = finalURL;
}

function goToDisputedMasterBLScreen(path) {
    var fromScreen = "EXP VOYAGE";
    var toScreenName = "DISPUTED_MASTER_BL";
    window.parent.goToVoyageFromDisputed(path, "", "", fromScreen, toScreenName);
}

function enableVoyage(obj) {
    if (obj.checked)
    {
        document.getElementById("scheduleNo").readOnly = true;
        document.getElementById("scheduleNo").className = "text mandatory readOnly";
        document.getElementById("scheduleNo").value = document.getElementById("hiddenScheduleNo").value;
    }
    else
    {
        document.getElementById("scheduleNo").value = "";
        document.getElementById("scheduleNo").className = "text mandatory";
        $('#scheduleNo').removeAttr('readonly');
    }
}

function congAlert(txt) {
    $.prompt(txt);
}
function closeVoyage(path) {
    var status = $("#closevoyagebutton").val();
    var headerId = document.getElementById("headerId").value;
    if (getCompleteValidation(headerId, status)) {
        var href = path + "/lclAddVoyage.do?methodName=displayCloseRemarks&status=" + status + "&headerId=" + headerId;
        $("#closevoyagebutton").attr("href", href);
        $("#closevoyagebutton").colorbox({
            iframe: true,
            width: "30%",
            height: "45%",
            title: "Remarks"
        });
    }
}

function auditVoyage(path) {
    var status = $("#auditvoyagebutton").val();
    var headerId = $("#headerId").val();
    var unitId = $("#unitId").val();
    if (getCompleteValidation(headerId, status)) {
        if (status === 'Audit') {
            var href = path + "/lclAddVoyage.do?methodName=displayAuditedRemarks&status=" + status + "&headerId=" + headerId + "&unitId=" + unitId;
            $("#auditvoyagebutton").attr("href", href);
            $("#auditvoyagebutton").colorbox({
                iframe: true,
                width: "30%",
                height: "45%",
                title: "Audit Remarks",
                onClosed: function () {
                    var remarks = document.getElementById("remarks").value;
                    document.getElementById("headerId").value = headerId;
                    document.getElementById("unitId").value = unitId;
                    document.getElementById("auditedRemarks").value = remarks;
                    document.getElementById("methodName").value = "updateClosedAuditedRemarks";
                    $("#lclAddVoyageForm").submit();
                }
            });
        } else {
            var txt = "Are you sure Y/N?"
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v == 1) {
                        $.prompt.close();
                        var remarks = document.getElementById("remarks").value;
                        document.getElementById("headerId").value = headerId;
                        document.getElementById("unitId").value = unitId;
                        document.getElementById("auditedRemarks").value = remarks;
                        document.getElementById("buttonValue").value = status;
                        document.getElementById("methodName").value = "updateClosedAuditedRemarks";
                        $("#lclAddVoyageForm").submit();
                    }
                    else if (v == 2) {
                        $.prompt.close();
                    }
                }
            });
        }
    }
}

function deleteUnits(unitId) {
    var txt = 'Are you sure You want to delete?';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                $("#methodName").val('deleteUnits');
                document.getElementById("unitId").value = unitId;
                $("#lclAddVoyageForm").submit();
                hideProgressBar();
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function deleteSSDetail(detailId) {
    var datatableobj = document.getElementById('lclunitSS');
    if (datatableobj != null && datatableobj.rows.length >= 2)
    {
        $.prompt("Cannot delete details record when units is associated with voyage.");
        return false;
    }
    var txt = 'Are you sure You want to delete?';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                $("#methodName").val('deleteVoyageDetail');
                document.getElementById("detailId").value = detailId;
                $("#lclAddVoyageForm").submit();
                hideProgressBar();
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function deleteSSMasterDetail(masterId) {
    var txt = 'Are you sure You want to delete?';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                $("#methodName").val('deleteSSMasterDetail');
                document.getElementById("masterId").value = masterId;
                $("#lclAddVoyageForm").submit();
                hideProgressBar();
            }
            else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function showCloseRemarksOnMouseOver() {
    var headerId = document.getElementById("headerId").value;
    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "showCloseRemarks",
            param1: headerId
        },
        async: false,
        success: function (data) {
            jQuery("#closedRemarks").html(data);
        }
    });
}
function showAuditedRemarksOnMouseOver() {
    var headerId = document.getElementById("headerId").value;
    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "showAuditRemarks",
            param1: headerId
        },
        async: false,
        success: function (data) {
            jQuery("#auditedRemarks").html(data);
        }
    });
}
function openUnits(path, unitId, unitssId) {
    var originId = document.getElementById("originId").value;
    var finalDestinationId = document.getElementById("finalDestinationId").value;
    var href = path + "/lclAddVoyage.do?methodName=openUnits&originId=" + originId + "&finalDestinationId=" + finalDestinationId + "&unitId=" + unitId + "&unitssId=" + unitssId;
    $("#stuffing_dr").attr("href", href);
    $("#stuffing_dr").colorbox({
        iframe: true,
        width: "100%",
        height: "96%",
        title: "Stuffing"
    });
}
//Action Performed after clicking on send EDI button.
function sendEDI(ssMasterId, spBookingNo) {
    var carrierspAcctNo = $("#carrierSpAccountno").val();
    if (carrierspAcctNo !== "" && carrierspAcctNo !== null && carrierspAcctNo !== 'undefined') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkIntraForCarrier",
                param1: carrierspAcctNo
            },
            async: false,
            success: function (data) {
                if (data === null || data === "" || data === "N") {
                    $.prompt("Selected Carrier is not valid to send EDI to either Inttra or GT Nexus");
                } else {
                    var table = document.getElementById("lclunitSS");
                    var tbody = table.getElementsByTagName("tbody")[0];
                    var rows = tbody.getElementsByTagName("tr");
                    sendEDI.counter = 0;
                    for (var j = 1; j <= rows.length; j++) {
                        var value = $("#lclunitSS").find('tr:nth-child(' + j + ') td:nth-child(13)').html();
                        if (value == spBookingNo) {
                            sendEDI.counter += 1;
                        }
                    }
                    if (sendEDI.counter == 0) {
                        $.prompt("Sorry ,you can not send EDI ,No Unit has been assigned to SS Master");
                    }
                    else {
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.lcl.dwr.LclDwr",
                                methodName: "sendEDI",
                                param1: ssMasterId,
                                request: true
                            },
                            async: false,
                            preloading: true,
                            success: function (data) {
                                if (data == "EDI Submited Successfully") {
                                    $.prompt(data, {
                                        buttons: {
                                            Ok: 1
                                        },
                                        submit: function (v) {
                                            if (v === 1) {
                                                window.parent.showLoading();
                                                $("#methodName").val('editVoyage');
                                                $("#lclAddVoyageForm").submit();
                                                $.prompt.close();
                                            }
                                        }
                                    });
                                } else {
                                    $.prompt(data);
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
function viewEdiPopUp(path, masterId) {
    var headerId = $("#headerId").val();
    var href = path + "/lclAddVoyage.do?methodName=voyageEdiTracking&headerId=" + headerId + "&masterId=" + masterId;
    $.colorbox({
        iframe: true,
        width: "70%",
        height: "90%",
        href: href,
        title: "EDI Tracking"
    });
}
function openLclVoyageArInvoice(path, headerId, voyNo, listFlag) {
    var href = path + "/lclVoyageArInvoice.do?methodName=display&fileNumberId=" + headerId + "&fileNumber=" + voyNo;
    href = href + "&listFlag=" + listFlag + "&voyageId=" + headerId + "&selectedMenu=Exports";
    $(".invoice").attr("href", href);
    $(".invoice").colorbox({
        iframe: true,
        width: "85%",
        height: "95 %",
        title: "AR Invoice",
        onClosed: function () {
            $("#filterByChanges").val($("#filterByChanges").val());
            $("#methodName").val('editVoyage');
            $("#lclAddVoyageForm").submit();
        }
    });
}
function lclPrintReport(path, unitsId, index) {
    var href = path + "/printfaxemail.do?methodName=print&moduleId=LCLUnits&fileNumberId=" + unitsId;
    $("#fax").attr("href", href);
    $("#fax").colorbox({
        iframe: true,
        width: "75%",
        height: "90%",
        title: "Print/Fax/Email"
    });
}
function validateBillToCodeByBL(unitssId) {
    var txt = "";
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
            methodName: "isValidateManifestFiles",
            param1: unitssId,
            dataType: "json"
        },
        async: false,
        success: function (data) {

        }
    });
    var text = "";
    return text;
}

function openCOBPopUp(path, unitId, unitssId, unitNo, headerId,
        scheduleNo, status, filterByChanges, bkgHazFlag, unitHazFlag) {
    if (status !== "M") {
        $.prompt("Please Manifest this Unit#<span style='color:red'>" + unitNo + "</span> before COB")
        return false;
    } else if (!validateGlAccount(unitssId)) {
        return false;
    }
    else if (!getCobValueAlert(unitssId)) {
        return false;
    }

    else {
        var hazFlag = '';
        if (bkgHazFlag === 'true' || unitHazFlag === 'true') {
            hazFlag = 'haz';
        }
        $('#hazFlag').val(hazFlag)
        var href = path + "/lclAddVoyage.do?methodName=openCOBPopup&unitId=" + unitId +
                "&unitssId=" + unitssId + "&unitNo=" + unitNo + "&headerId=" + headerId + "&scheduleNo="
                + scheduleNo + "&status=" + status + "&filterByChanges=" + filterByChanges + "&hazFlag=" + hazFlag;
        $.colorbox({
            iframe: true,
            href: href,
            width: "70%",
            height: "70%",
            title: "COB"
        });
    }
}
function unmanifest(unitId, unitssId, unitNo, confirmOnBoard, filterByChanges) {
    if (confirmOnBoard === "true") {
        $.prompt("Unit#<span style='color:red'>" + unitNo + "</span>  has been COB, you cannot unmanifest")
        return false;
    }
    $.prompt("Are you sure you want to Unmanifest this unit# <span style='color:red'>" + unitNo + "</span> ?", {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                $("#methodName").val("isCorrectionFound");
                $("#unitssId").val(unitssId);
                $.post($("#lclAddVoyageForm").attr("action"), $("#lclAddVoyageForm").serialize() + "&filterByChanges=" + filterByChanges,
                        function (data) {
                            if (data !== "") {
                                $.prompt(data);
                            }
                            else {
                                window.parent.showLoading();
                                $("#methodName").val('unManifest');
                                $("#unitssId").val(unitssId);
                                $("#unitId").val(unitId);
                                $("#lclAddVoyageForm").submit();
                            }
                        });
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}

function printreport(fileId, fileNo, screenName, subject) {
    var voyOwnerTerminalName = $("#voyOwnerTerminalName").val();
    var voyOwnerTerminalID = $("#voyOwnerTerminalId").val();
    var issuTerm = voyOwnerTerminalName + "/" + voyOwnerTerminalID;
    if (null != fileId && fileId != '') {
        GB_show("Print", "/logisoft/printConfig.do?screenName=" + screenName + "&fileId=" + fileId + "&fileNo=" + fileNo
                + "&subject=" + subject + "&emailSubject=" + subject + "&issuingTerminal=" + issuTerm, 350, 900);
    } else {
        alert("Please save the file before print/fax");
    }
}
function scanUnit(documentId, screenName, moduleName, importFlag, unitNo) {
    if (null != documentId && documentId != '') {
        $("#currentUnit").val(documentId);
        GB_show("Scan", "/logisoft/scan.do?screenName=" + screenName + "&documentId=" + documentId + "&importFlag=" + importFlag + "&unitNo=" + unitNo + "&quoteBookingName=" + moduleName + "&", 350, 900);
    } else {
        alert("Please save the file before Scan/Attach");
    }
}
function changeScanButtonColor(masterStatus, documentName) {
    document.getElementById("scan-attach_" + $("#currentUnit").val()).className = "green-background";
}
function loadComplete(path, unitId, unitSsId, headerId, index, originId, destnId) {
    var filterByChanges = $('#filterByChanges').val();
    var podDestination = $('#podDestination').val();
    var unitVoyageSearch = $('#unitVoyageSearch').val();
    var pooOrigin = $('#pooOrigin').val();
    var filterByNewValue = $('#filterByNewValue').val();
    if (filterByNewValue === "currentProcess") {
        filterByChanges = "currentProcess";
        var searchOriginId = $('#searchOriginId').val();
        var searchFdId = $('#searchFdId').val();
        var searchOrigin = $('#searchOrigin').val();
        var searchFd = $('#searchFd').val();
    }
    var href = '';
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
            methodName: "isUnitContainFile",
            param1: unitSsId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data) {
                if (filterByChanges == 'lclDomestic' && $('#lclssDetail tr').length > 2) {
                    href = path + "/lclAddVoyage.do?methodName=displayArrivalLocation&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId="
                            + destnId + "&unitssId=" + unitSsId + "&index=" + index + "&headerId=" + headerId + "&filterByChanges=" + filterByChanges
                            + "&unitVoyageSearch=" + unitVoyageSearch + "&pol=" + pooOrigin + "&pod=" + podDestination + "&filterByNewValue=" + $('#filterByNewValue').val();
                    $.colorbox({
                        iframe: true,
                        width: "30%",
                        height: "35%",
                        href: href,
                        title: "Arrival Location"
                    });
                } else {
                    window.parent.showLoading();
                    href = path + "/lclAddVoyage.do?methodName=loadComplete&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId=" + destnId + "&unitssId=" + unitSsId + "&index=" + index + "&headerId=" + headerId + "&filterByChanges=" + filterByChanges;
                    if (unitVoyageSearch == 'Y') {
                        href += "&unitVoyageSearch=" + unitVoyageSearch;
                    }
                    if (filterByChanges == 'lclExport') {
                        href += "&pol=" + pooOrigin + "&pod=" + podDestination;
                    } else {
                        href += "&originalOriginName=" + pooOrigin + "&originalDestinationName=" + podDestination;
                    }
                    if (filterByChanges == 'currentProcess') {
                        href += "&searchOriginId=" + searchOriginId + "&searchFdId=" + searchFdId
                                + "&searchOrigin=" + searchOrigin + "&searchFd=" + searchFd;
                    }
                    href += "&filterByNewValue=" + $('#filterByNewValue').val();
                    document.location.href = href;
                }
            } else {
                $.prompt("Atleast one booking must be picked");
            }
        }
    });
}

function unLoad(path, unitId, unitSsId, headerId, index, originId, destnId) {
    var txt = 'Are you sure you want to re-open this unit for loading';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                window.parent.showLoading();
                var filterByChanges = $('#filterByChanges').val();
                var pooOrigin = $('#pooOrigin').val();
                var podDestination = $('#podDestination').val();
                var unitVoyageSearch = $('#unitVoyageSearch').val();
                var filterByNewValue = $('#filterByNewValue').val();
                if (filterByNewValue === "currentProcess") {
                    filterByChanges = "currentProcess";
                    var searchOriginId = $('#searchOriginId').val();
                    var searchFdId = $('#searchFdId').val();
                    var searchOrigin = $('#searchOrigin').val();
                    var searchFd = $('#searchFd').val();
                }
                var href = path + "/lclAddVoyage.do?methodName=unLoad&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId=" + destnId + "&unitssId=" + unitSsId + "&index=" + index + "&headerId=" + headerId + "&filterByChanges=" + filterByChanges;
                if (unitVoyageSearch == 'Y') {
                    href += "&unitVoyageSearch=" + unitVoyageSearch;
                }
                if (filterByChanges == 'lclExport') {
                    href += "&pol=" + pooOrigin + "&pod=" + podDestination;
                } else {
                    href += "&originalOriginName=" + pooOrigin + "&originalDestinationName=" + podDestination;
                }
                if (filterByChanges == 'currentProcess') {
                    href += "&searchOriginId=" + searchOriginId + "&searchFdId=" + searchFdId
                            + "&searchOrigin=" + searchOrigin + "&searchFd=" + searchFd;
                }
                href += "&filterByNewValue=" + filterByNewValue;
                document.location.href = href;
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}

function addBLToManifest(path, unitId, unitssId, unitNo, scheduleNo) {
    var polId = document.getElementById("originId").value;
    var podId = document.getElementById("finalDestinationId").value;
    var filterByChanges = $('#filterByChanges').val();
    var href = path + "/lclAddVoyage.do?methodName=addBLToManifest&unitId=" + unitId + "&unitssId=" + unitssId + "&filterByChanges=" + filterByChanges;
    href = href + "&unitNo=" + unitNo + "&scheduleNo=" + scheduleNo + "&polId=" + polId + "&podId=" + podId;
    $.colorbox({
        iframe: true,
        href: href,
        width: "60%",
        height: "40%",
        title: "Add B/L to Manifest"
    });
}

function openManifestPopup(path, unitId, unitssId, unitNo, scheduleNo, filterByChanges) {

    var href = path + "/lclAddVoyage.do?methodName=openManifestPopup&unitId=" + unitId +
            "&unitssId=" + unitssId + "&unitNo=" + unitNo + "&scheduleNo=" + scheduleNo + "&filterByChanges=" + filterByChanges;
    $.colorbox({
        iframe: true,
        href: href,
        width: "50%",
        height: "80%",
        title: "Manifest"
    });
}

function viewManifestPopup(path, unitssId, unitNo, scheduleNo) {
    var href = path + "/lclAddVoyage.do?methodName=viewManifestPopup&unitssId=" + unitssId + "&unitNo=" + unitNo + "&scheduleNo=" + scheduleNo;
    $.colorbox({
        iframe: true,
        href: href,
        width: "90%",
        height: "90%",
        title: "View Manifest"

    });
}

function viewDRSPopup(path, unitssId, unitNo, scheduleNo) {
    parent.showPreloading();
    var href = path + "/lclAddVoyage.do?methodName=viewDRSPopup&unitssId=" + unitssId + "&unitNo=" + unitNo + "&scheduleNo=" + scheduleNo;
    $.colorbox({
        iframe: true,
        href: href,
        width: "100%",
        height: "90%",
        title: "View DRs",
        onClosed: function () {
            parent.showLoading();
            $('#toScreenName').val('');
            $("#filterByChanges").val($("#filterByChanges").val());
            $("#methodName").val('editVoyage');
            $("#lclAddVoyageForm").submit();
        }
    });
    setTimeout(function () {
        parent.closePreloading();
    }, 1000);
}
function addExceptionPopup(path, unitssId, unitNo, scheduleNo, unitId, headerId) {
    var href = path + "/lclAddVoyage.do?methodName=addExceptionDRsPopup&unitssId=" + unitssId + "&unitNo=" + unitNo + "&scheduleNo=" + scheduleNo + "&unitId=" + unitId + "&headerId=" + headerId + "&unitExceptionFlag=false&addExcepPoppUpFlag=true";
    $.colorbox({
        iframe: true,
        href: href,
        width: "70%",
        height: "90%",
        title: "Add Exception"

    });
}
function changeVoyagePopUp(path, scheduleNo, originId, destinationId, unitssId, headerId, unitId, unitNo) {

    var href = path + "/lclAddVoyage.do?methodName=changeVoyagePopup&unitssId=" + unitssId + "&scheduleNo=" +
            scheduleNo + "&originalOriginId=" + originId + "&originalDestinationId=" + destinationId +
            "&headerId=" + headerId + "&unitId=" + unitId + "&unitNo=" + unitNo + "&schServiceType=" + $('#schServiceType').val();
    $.colorbox({
        iframe: true,
        href: href,
        width: "50%",
        height: "40%",
        title: "<span style=color:blue>Change Voyage</span>"
    });
}
function showWholeBooking(path) {
    var unitssId = $("#unitssId").val();
    var unitNo = $("#unitNo").val();
    var scheduleNo = $("#scheduleNo").val();
    var unitId = $("#unitId").val();
    var headerId = $("#headerId").val();
    var href = path + "/lclAddVoyage.do?methodName=addExceptionDRsPopup&unitssId=" + unitssId + "&unitNo=" + unitNo + "&scheduleNo=" + scheduleNo + "&unitId=" + unitId + "&headerId=" + headerId + "&unitExceptionFlag=true";
    document.location.href = href;
}
function addExceptionToDrs(path) {
    var dr = false;
    $('[name=drException]').each(function () {
        if (this.checked) {
            dr = true;
        }
    });
    if (dr) {
        $("#unitExceptionId").val("");
        $('#exceptionTable').show();
    } else {
        $('#exceptionTable').hide();
        $.prompt("Please check D/R to add Exception");
        $("#warning").parent.show();
    }
}
function editExceptionToDrs(id) {
    $("#exceptionFileNumbers").val(id);
    $("#methodName").val('editUnitException');
    $("#lclAddVoyageForm").submit();
}
function checkUncheckExceptionToDrs() {
    var dr = false;
    $('[name=drException]').each(function () {
        if (this.checked) {
            dr = true;
        }
    });
    if (!dr) {
        $('#exceptionTable').hide();
    }
}
function saveExceptionToDrs(obj) {
    var dr = "";
    $('[name=drException]').each(function () {
        if (this.checked) {
            dr = dr + "," + this.value;
        }
    });
    if (obj.value == 'Update') {
        dr = $("#exceptionFileNumbers").val();
    }
    if (dr != '') {
        if (document.getElementById("unitExceptionId").value == "") {
            $.prompt("Please enter Exception");
            $("#warning").parent.show();
        }
        $("#exceptionFileNumbers").val(dr);
        $("#methodName").val('saveUnitException');
        $("#lclAddVoyageForm").submit();
    }
}
function showToolTip(comment, w, e) {
    tooltip.showComments(comment, w, e);
}
function showBLStatus() {
    if ($("#fileNumber").val() == "") {
        $.prompt("D/R# is required");
        $("#fileNumber").css("border-color", "red");
        $("#warning").show();
    } else if (isSpecial(document.getElementById("fileNumber").value)) {
        $("#methodName").val('showBLStatus');
        $("#lclAddVoyageForm").submit();
    } else {
        $.prompt("Special Characters Not Allowed in D/R#");
        $("#fileNumber").css("border-color", "red");
        $("#warning").show();
    }

}
function manifestDr() {
    $("#methodName").val('manifestDr');
    $("#lclAddVoyageForm").submit();
}
function goBackAddManifestBL() {
    $("#methodName").val('addBLToManifest');
    $("#lclAddVoyageForm").submit();
}

function unitsEdit(path, unitId, headerId, unitssId, hazStatus) {
    var filterByChanges = $('#filterByChanges').val();
    var href = path + "/lclAddUnits.do?methodName=editUnits&unitId=" + unitId + "&headerId=" + headerId + "&unitssId=" + unitssId + "&hazStatus=" + hazStatus + "&filterByChanges=" + filterByChanges;
    $.colorbox({
        iframe: true,
        href: href,
        width: "100%",
        height: "90%",
        title: "<span style=color:blue>Edit Unit</span>",
        onClosed: function () {
            parent.showLoading();
            $("#methodName").val('editVoyage');
            $("#lclAddVoyageForm").submit();
        }
    });
}

function load(path, originId, destnIn, headerId, filterByChanges, scheduleNo, detailId, serviceType) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "unitStuffingAvailable",
            param1: scheduleNo,
            param2: 'LCLEXPORT-VOYAGE',
            param3: 'UNIT-STUFFING',
            dataType: "json"
        },
        success: function (data) {
            if (data == 'unlocked') {
                var href = path + "/lclAddVoyage.do?methodName=openUnits&originId=" + originId + "&finalDestinationId=" + destnIn + "&headerId="
                        + headerId + "&displayLoadComplete=N" + "&filterByChanges=" + filterByChanges + "&scheduleNo=" + scheduleNo + "&detailId=" + detailId + "&schServiceType=" + serviceType;
                $.colorbox({
                    iframe: true,
                    href: href,
                    width: "100%",
                    height: "90%",
                    title: "Stuffing",
                    onClosed: function () {
                        window.parent.showLoading();
                        $("#filterByChanges").val(filterByChanges);
                        $("#releaseLock").val(true);
                        $("#methodName").val('editVoyage');
                        $("#lclAddVoyageForm").submit();
                    }
                });
            } else {
                $.prompt(data);
            }
        }
    });
}

function unitNotes(path, unitId, headerId, voyageNo) {
    var href = path + "/lclUnitSsRemarks.do?methodName=displayNotes&unitId=" + unitId + "&headerId=" + headerId + "&voyageNo=" + voyageNo;
    $.colorbox({
        iframe: true,
        href: href,
        width: "70%",
        height: "370px",
        title: "Remarks"
    });
}
function customDltMove(path, unitId, confirmOnBoard, status, suHeadingNote, chassisNo, loadedByUserId) {
    if (status === "C" || confirmOnBoard === "true" || status === "M") {
        $.prompt("Please UnComplete the Unit and Delete")
        return false;
    }
    var href = path + "/lclAddUnits.do?methodName=customDltMoveToYard&unitId=" + unitId + "&sealNo=" + suHeadingNote + "&chassisNo=" + chassisNo + "&loadedByUserId=" + loadedByUserId;
    $.colorbox({
        iframe: true,
        href: href,
        width: "40%",
        height: "300px",
        title: "lclUnitDeleteAndMoveUnitToYARD"
    });
}
function getUnlocationCode(id) {
    var originCode = "";
    var originObjR = document.getElementById(id);
    if (originObjR != undefined && originObjR != null && originObjR.value != "") {
        originCode = document.getElementById(id).value;
    }
    if (originCode.indexOf("(") > -1 && originCode.indexOf(")") > -1) {
        return originCode.substring(originCode.indexOf("(") + 1, originCode.indexOf(")"));
    }
    return "";
}
function changeVoyageOwner() {//change voyage owner
    showLoading();
    $('#voyageOwnerFlag').val("true");
    $("#methodName").val('editVoyage');
    $("#lclAddVoyageForm").submit();
}

function openButton(path, index, detailId) {
    var buttonStatus = "OpenToClose", flag = false;
    $(".openbutton").each(function () {
        if ($(this).text() === "Close" && $(this).text() !== "Closed") {
            buttonStatus = "CloseToClosed";
            flag = true;
        } else if ($("#detailOpenButton" + index).text() === "Close") {
            buttonStatus = "CloseToClosed";
            flag = true;
        } else if ($("#detailOpenButton" + index).text() === "Closed") {
            buttonStatus = "alreadyClosed";
            flag = true;
        } else {
            buttonStatus = "";
        }
    });
    if (buttonStatus === "OpenToClose" || !flag) {
        openToClose(index, detailId);
    } else if (buttonStatus === "CloseToClosed") {
        closeToClosed(index, detailId);
    } else if (buttonStatus.trim() === "alreadyClosed") {
        $.prompt("The voyage Detail Already Closed. Do You Want To ReOpen", {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    closedToOpen(index, detailId);
                } else {
                    $.prompt.close();
                }
            }
        });
    } else {
        $.prompt("Should open one voyage at a time.");
    }
}

function closeToClosed(index, detailId) {
    if ($("#detailOpenButton" + index).text() === "Close") {
        $("#detailLoadButton" + index).hide();
        $("#detailOpenButton" + index).text("Closed");
        $("#detailOpenButton" + index).css("background", "green");
        changeButtonStatus(detailId, "Closed");
    }
}
function closedToOpen(index, detailId) {
    if ($("#detailOpenButton" + index).text() === "Closed") {
        $("#detailLoadButton" + index).hide();
        $("#detailOpenButton" + index).text("Open");
        $("#detailOpenButton" + index).css("background", "#8DB7D6");
        changeButtonStatus(detailId, "Open");
    }
}
function openToClose(index, detailId) {
    if ($("#detailOpenButton" + index).text() === "Open") {
        $("#detailLoadButton" + index).show();
        $("#detailOpenButton" + index).text("Close");
        $("#detailOpenButton" + index).css("background", "red");
        changeButtonStatus(detailId, "Close");
    }
}
function changeButtonStatus(detailId, value) {
    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "updateLclDetailStatus",
            param1: detailId,
            param2: value
        },
        async: false,
        success: function (s) {
        }
    });
}

function detailLoad(path, originId, destnIn, headerId, filterByChanges, scheduleNo, index, detailId) {
    var msg = "Include Intransit DRs:";
    var href = "";
    $.prompt(msg, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                href = path + "/lclAddVoyage.do?methodName=openUnits&originId=" + originId + "&finalDestinationId=" + destnIn + "&headerId="
                        + headerId + "&displayLoadComplete=N" + "&filterByChanges=" + filterByChanges + "&scheduleNo=" + scheduleNo + "&detailId=" + detailId + "&intransitDr=true";
                openDetailPopup(href, filterByChanges, scheduleNo);
            }
            else {
                href = path + "/lclAddVoyage.do?methodName=openUnits&originId=" + originId + "&finalDestinationId=" + destnIn + "&headerId="
                        + headerId + "&displayLoadComplete=N" + "&filterByChanges=" + filterByChanges + "&scheduleNo=" + scheduleNo + "&detailId=" + detailId + "&intransitDr=false";
                openDetailPopup(href, filterByChanges, scheduleNo);
            }
        }
    });
}

function openDetailPopup(href, filterByChanges, scheduleNo) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "unitStuffingAvailable",
            param1: scheduleNo,
            param2: 'LCLEXPORT-VOYAGE',
            param3: 'UNIT-STUFFING',
            dataType: "json"
        },
        success: function (data) {
            if (data == 'unlocked') {
                $.colorbox({
                    iframe: true,
                    href: href,
                    width: "100%",
                    height: "90%",
                    title: "Stuffing",
                    onClosed: function () {
                        $('#toScreenName').val('');
                        $("#filterByChanges").val(filterByChanges);
                        $("#releaseLock").val('true');
                        $("#methodName").val('editVoyage');
                        $("#lclAddVoyageForm").submit();
                    }
                });
            } else {
                $.prompt(data);
            }
        }
    });
}

function validateETA() {
    var ETA = $('#std').val().split("-");
    var ETD = $('#etaPod').val().split("-");
    var days = getDaysBetween(ETA, ETD);
    if (days >= 0) {
        $.prompt("ETD should be less than ETA");
        $("#std").val("");
    }
}
function validateETD() {
    var ETA = $('#std').val().split("-");
    var ETD = $('#etaPod').val().split("-");
    var days = getDaysBetween(ETA, ETD);
    if (days >= 0) {
        $.prompt("ETA should be greater than ETD");
        $('#etaPod').val("");
    }
}
function getDaysBetween(etdArray, etaArray) {
    var date1 = new Date(etdArray[2], getMonthNumber(etdArray[1]) - 1, etdArray[0]);
    var date2 = new Date(etaArray[2], getMonthNumber(etaArray[1]) - 1, etaArray[0]);
    var distance = date1.getTime() - date2.getTime();
    distance = Math.ceil(distance / 1000 / 60 / 60 / 24);
    return distance;
}

function openHeaderNotes(path, headerId, voyageNo) {
    var href = path + "/lclSSHeaderRemarks.do?methodName=displayNotes&headerId=" + headerId + "&actions=show All&voyageNumber=" + voyageNo;
    $.colorbox({
        iframe: true,
        href: href,
        width: "70%",
        height: "370px",
        title: "Remarks"
    });
}
function costPopUp(path, unitssId, unitId, unitNo, cob, closedTime, auditedTime, bkgHazFlag, unitHazFlag) {
    var headerId = $('#headerId').val();
    var hazFlag = '';
    if (bkgHazFlag === 'true' || unitHazFlag === 'true') {
        hazFlag = 'haz';
    }
    var href = path + "/lclImpUnitCostCharge.do?methodName=displayCost&unitSsId=" + unitssId + "&cobStatus=" + cob
            + "&closedTime=" + closedTime + "&auditedTime=" + auditedTime + "&hazFlag=" + hazFlag
            + "&headerId=" + headerId + "&unitId=" + unitId + "&unitNo=" + unitNo + "&moduleName=Exports";
    $.colorbox({
        iframe: true,
        href: href,
        width: "70%",
        height: "100%",
        title: "Cost",
        onClosed: function () {
            window.parent.showLoading();
            $("#methodName").val('editVoyage');
            $("#lclAddVoyageForm").submit();
        }
    });
}
function scanAttach(documentId, screenName, ssMasterId, masterBl) {
    if (null !== documentId && documentId !== '') {
        GB_show("Scan", "/logisoft/scan.do?screenName=" + screenName + "&documentId=" + documentId + '-' + ssMasterId
                + "&ssMasterBl=" + masterBl, 350, 900);
    } else {
        alert("Please save the file before Scan/Attach");
    }
}

function editSsMasterDetails(path, masterId, destinationId) {
    var href = path + "/lclAddVoyage.do?methodName=editSSMasterDetail&masterId="
            + masterId + "&finalDestinationId=" + destinationId;
    $.colorbox({
        iframe: true,
        href: href,
        width: "90%",
        height: "90%",
        title: "Edit SS Master Detail"
    });
}
function changeOptionPopUp(path, unitSsId, unitId) {
    var href = path + "/lclAddVoyage.do?methodName=changeOptionPopUp&unitssId=" + unitSsId + "&unitId=" + unitId;
    $.colorbox({
        iframe: true,
        href: href,
        width: "55%",
        height: "50%",
        title: "<span style=color:blue>Change Option</span>"
    });
}

function validateGlAccount(unitSsId) {
    var flag = true;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.common.constant.ExportUnitQueryUtils",
            methodName: "validateGlAccount",
            param1: unitSsId,
            param2: $("#originId").val(),
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data !== '') {
                flag = false;
                showAlternateMask();
                $("#error-container").center().show(500, function () {
                    var txt = "<table border='0' class='bold' height='auto' width='100%'>" +
                            "<tr class='tableHeadingNew'><td>FILE NO</td><td>CHARGE CODE</td>" +
                            "<td>ERROR MESSAGE</td></tr>" + data +
                            "<tr><td colspan='3'>" +
                            "<input type='button' value='OK' class='button-style1'" +
                            " style='float:right;' onclick='cancelAlert();'></td></tr></table>";
                    $("#error-container").html(txt);
                });
            }
        }
    });
    return flag;
}

function cancelAlert() {
    $("#error-container").center().hide(500, function () {
        hideAlternateMask();
    });
}

function getCobValueAlert(unitssId) {
    var fileFlag = true;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO",
            methodName: "isCobValidation",
            param1: unitssId,
            dataType: "json"

        },
        async: false,
        success: function (data) {
            if (data !== null && ($(".cob" + unitssId).val() !== "true")) {
                $.prompt("Please check Piece,CFT,CBM,LBS and KGS for the following files<span class='red'> " + data + "</span> it should greater than zero");
                fileFlag = false;
            }
        }
    });
    return fileFlag;
}
function getCompleteValidation(headerId, status) {
    var flag = true;
    var close = $("#closevoyagebutton").attr('class') === 'green-background cursor' ? 'green-background' : 'button-style';
    var audit = $("#auditvoyagebutton").attr('class') === 'green-background cursor' ? 'green-background' : 'button-style';

    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
            methodName: "getCompleteValidation",
            param1: headerId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (status === "Close") {
                if (data !== null && close !== 'green-background') {
                    $.prompt("Audit/Close is not allowed unless unit is completed");
                    flag = false;
                }
            }
            else if (status === "Audit") {
                if (data !== null && audit !== 'green-background') {
                    $.prompt("Audit/Close is not allowed unless unit is completed");
                    flag = false;
                }
            }
        }
    });
    return flag;
}


