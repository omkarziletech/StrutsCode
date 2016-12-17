var containerSize = "";
$(document).ready(function () {
    showProgressBar(); // progress bar need for some date process dont't remove.
    setContainerType($("#containerSize").val());
    if ($("#unitSsIdFlag").val() === 'true') {
        $(".releasedUnit" + $("#unitssId").val()).click();
    }
    calculatePieceCount();
    parent.$(".cboxIframe").attr('scrolling', 'no');
    var height = parent.$(window).height() - 170 + "px";
    $(".stuffed").css({
        "height": height
    });
    hideProgressBar();
});

function setContainerType(cType) {
    containerSize = cType === "20ft dry freight container" ? "20FT"
            : cType === "40ft dry freight container" ? "40FT"
            : cType === "40ft high cube dry freight container" ? "40HC"
            : cType === "40ft open top container" ? "40OT"
            : cType === "40ft flat rack container" ? "40FR"
            : cType === "40ft reefer container" ? "40RE"
            : cType === "45ft dry freight container" ? "45FT"
            : cType === "45ft wide dry freight container" ? "45WD"
            : cType === "48ft high cube dry freight container" ? "48HC"
            : cType === "53ft dry freight semi-trailer" ? "53FT"
            : cType === "LCL" ? "LCL" : "";
}
function showCurrentUnit(path, unitId, originId, finalDestinationId, unitssId, headerId, bookScheduleNo, polEtd) {
    var filterByChanges = parent.document.getElementById("filterByChanges").value;
    var methodName = filterByChanges === 'lclDomestic' ? "viewCurrentUnitForDomestic" : "viewCurrentUnit";
    var appendFlag = "";
    if (filterByChanges !== 'lclDomestic') {
        var isReleasedDr = $('#isReleasedDr').val();
        bookScheduleNo = $('#bookScheduleNo').val();
        var isReleaseWithCurLoc = $("#checkAllRealeaseWithCurrLoc").attr("checked") ? true : false;
        appendFlag = "&isReleasedDr=" + isReleasedDr + "&bookScheduleNo=" + bookScheduleNo + "&checkAllRealeaseWithCurrLoc=" + isReleaseWithCurLoc;
    }
    var serviceType = $('#serviceType').val();
    var flag = $("#showAllPol").attr("checked") ? true : false;
    var showOBKG = $("#showBooking").attr("checked") ? true : false;
    showProgressBar();
    var href = path + "/lclAddVoyage.do?methodName=" + methodName + "&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId=" + finalDestinationId + "&unitssId=" + unitssId + "&headerId="
            + headerId + "&filterByChanges=" + filterByChanges + "&displayLoadComplete=N&bookScheduleNo=" + bookScheduleNo + "&polEtd=" + polEtd + "&detailId=" + $("#detailId").val()
            + "&showAllDr=" + flag + "&showBooking=" + showOBKG + "&schServiceType=" + serviceType + appendFlag;
    document.location.href = href;
}

function deleteStuffedList(path, unitId, originId, finalDestinationId) {
    var unitssId = $("#unitssId").val();
    var chkDeleteStuff = [];
    $(".chkDeleteStuff:checked").each(function () {
        chkDeleteStuff.push($(this).val());
    });
    if (chkDeleteStuff.length <= 0) {
        sampleAlert("Please select atleast one stuffed record");
        return false;
    } else {
        $("#hiddenDeletestuff").val(chkDeleteStuff);
    }
    showProgressBar();
    var flag = $("#showAllPol").attr("checked") ? true : false;
    var showOBKG = $("#showBooking").attr("checked") ? true : false;
    var filterByChanges = parent.$("#filterByChanges").val();
    var index = $("#index").value;
    var scheduleNo = parent.$("#schedule").val();
    var unitNo = $('#unitNo').val();
    var headerId = $('#headerId').val();
    var serviceType = $('#serviceType').val();
    var appendFlag = "";
    if (filterByChanges !== 'lclDomestic') {
        var isReleasedDr = $('#isReleasedDr').val();
        var bookScheduleNo = $('#bookScheduleNo').val();
        var isReleaseWithCurLoc = $("#checkAllRealeaseWithCurrLoc").attr("checked") ? true : false;
        appendFlag = "&isReleasedDr=" + isReleasedDr + "&bookScheduleNo=" + bookScheduleNo + "&checkAllRealeaseWithCurrLoc=" + isReleaseWithCurLoc;
    }
    var href = path + "/lclAddVoyage.do?methodName=deleteStuffedUnits&unitId=" + unitId + "&unitssId=" + unitssId + "&stuffedFileNumbers=" + $("#hiddenDeletestuff").val() + "&originId=" + originId + "&finalDestinationId="
            + finalDestinationId + "&headerId=" + headerId + "&index=" + index + "&unitNo=" + unitNo + "&scheduleNo=" + scheduleNo + "&filterByChanges=" + filterByChanges + "&detailId=" + $("#detailId").val() + "&intransitDr=" + $("#intransitDr").val()
            + "&showAllDr=" + flag + "&showBooking=" + showOBKG + "&schServiceType=" + serviceType + appendFlag;
    document.location.href = href;
}


function printLoadReport(path) {
    var unitSsId = $('#unitssId').val();
    if (unitSsId === '' || unitSsId === null) {
        $.prompt('Please Select Unit');
        return false;
    }
    var chkSaveDeStuff = [];
    $(".chkSaveDeStuff:checked").each(function () {
        chkSaveDeStuff.push(jQuery(this).val());
    });
    $("#loadedfileIds").val(chkSaveDeStuff);
    var selectedDr = $("#loadedfileIds").val();
    if (selectedDr === '' || selectedDr === null) {
        $.prompt('Please Select atleast one Booking');
        return false;
    }
    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclPrintUtil",
            methodName: "createLclELoadPdf",
            param1: unitSsId,
            param2: unitSsId,
            param3: $('#unitNo').val(),
            param4: selectedDr,
            request: "true"
        },
        preloading: true,
        success: function (data) {
            viewFile(data, path);
        }
    });
}

function viewFile(file, path) {
    window.open(path + '/servlet/FileViewerServlet?fileName=' + file, '_blank', 'width=1200,height=600,top=40,left=40,directories=no,location=no,linemenubar=no,toolbar=no,status=no');
}

function resetUnit(path, unitId, originId, finalDestinationId, unitssId, headerId) {
    var filterByChanges = parent.document.getElementById("filterByChanges").value;
    var methodName = filterByChanges === 'lclDomestic' ? "viewCurrentUnitForDomestic" : "viewCurrentUnit";
    var serviceType = $('#serviceType').val();
    showProgressBar();
    var flag = $("#showAllPol").attr("checked") ? true : false;
    var showOBKG = $("#showBooking").attr("checked") ? true : false;
    var showPreReleased = $("#showPreReleased").attr("checked") ? true : false;
    var appendFlag = "";
    if (filterByChanges !== 'lclDomestic') {
        var isReleasedDr = $('#isReleasedDr').val();
        var bookScheduleNo = $('#bookScheduleNo').val();
        var isReleaseWithCurLoc = $("#checkAllRealeaseWithCurrLoc").attr("checked") ? true : false;
        appendFlag = "&isReleasedDr=" + isReleasedDr + "&bookScheduleNo=" + bookScheduleNo + "&checkAllRealeaseWithCurrLoc=" + isReleaseWithCurLoc;
    }
    var href = path + "/lclAddVoyage.do?methodName=" + methodName + "&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId="
            + finalDestinationId + "&unitssId=" + unitssId + "&headerId=" + headerId + "&filterByChanges=" +
            filterByChanges + "&displayLoadComplete=N&detailId=" + $("#detailId").val() + "&intransitDr=" + $("#intransitDr").val()
            + "&showAllDr=" + flag + "&showBooking=" + showOBKG + "&schServiceType=" + serviceType+ "&showPreReleased=" + showPreReleased + appendFlag;
    document.location.href = href;
}

function showAllDr(path, unitId, originId, finalDestinationId, unitssId, headerId) {
    var filterByChanges = parent.document.getElementById("filterByChanges").value;
    var serviceType = $('#serviceType').val();
    showProgressBar();
    var href;
    var flag = $("#showAllPol").attr("checked") ? true : false;
    var showOBKG = $("#showBooking").attr("checked") ? true : false;
    var showPreReleased = $("#showPreReleased").attr("checked") ? true : false;
    if (!flag) {
        href = path + "/lclAddVoyage.do?methodName=viewCurrentUnitForDomestic&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId="
                + finalDestinationId + "&unitssId=" + unitssId + "&headerId=" + headerId + "&filterByChanges="
                + filterByChanges + "&displayLoadComplete=N&detailId=" + $("#detailId").val()
                + "&intransitDr=" + $("#intransitDr").val() + "&showAllDr=" + flag + "&showBooking=" + showOBKG + "&schServiceType=" + serviceType+ "&showPreReleased=" + showPreReleased;
    } else {
        href = path + "/lclAddVoyage.do?methodName=viewCurrentUnitForDomestic&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId="
                + finalDestinationId + "&unitssId=" + unitssId + "&headerId=" + headerId + "&filterByChanges="
                + filterByChanges + "&displayLoadComplete=N&detailId=" + $("#detailId").val()
                + "&intransitDr=" + $("#intransitDr").val() + "&showAllDr=" + flag + "&showBooking=" + showOBKG + "&schServiceType=" + serviceType+ "&showPreReleased=" + showPreReleased;
    }
    document.location.href = href;
}
function showINTR(path, unitId, originId, finalDestinationId, unitssId, headerId) {
    var filterByChanges = parent.document.getElementById("filterByChanges").value;
    var serviceType = $('#serviceType').val();
    var flag = $("#showINTR").attr("checked") ? true : false;
    var href = path + "/lclAddVoyage.do?methodName=viewCurrentUnit&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId="
            + finalDestinationId + "&unitssId=" + unitssId + "&headerId=" + headerId + "&filterByChanges=" +
            filterByChanges + "&displayLoadComplete=N&detailId=" + $("#detailId").val() + "&intransitDr=" + flag + "&schServiceType=" + serviceType;
    document.location.href = href;
}

function showOBKGBooking(path, unitId, originId, finalDestinationId, unitssId, headerId) {
    showProgressBar();
    var filterByChanges = parent.document.getElementById("filterByChanges").value;
    var serviceType = $('#serviceType').val();
    var flag = $("#showBooking").attr("checked") ? true : false;
    var showAllPol = $("#showAllPol").attr("checked") ? true : false;
     var showPreReleased = $("#showPreReleased").attr("checked") ? true : false;
    var href;
    if (!flag) {
        href = path + "/lclAddVoyage.do?methodName=viewCurrentUnitForDomestic&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId="
                + finalDestinationId + "&unitssId=" + unitssId + "&headerId=" + headerId + "&filterByChanges="
                + filterByChanges + "&displayLoadComplete=N&detailId=" + $("#detailId").val() + "&schServiceType=" + serviceType
                + "&intransitDr=" + $("#intransitDr").val() + "&showAllDr=" + showAllPol + "&showBooking=" + flag+ "&showPreReleased=" + showPreReleased;
    } else {
        href = path + "/lclAddVoyage.do?methodName=viewCurrentUnitForDomestic&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId="
                + finalDestinationId + "&unitssId=" + unitssId + "&headerId=" + headerId + "&filterByChanges="
                + filterByChanges + "&displayLoadComplete=N&detailId=" + $("#detailId").val() + "&schServiceType=" + serviceType
                + "&intransitDr=" + $("#intransitDr").val() + "&showAllDr=" + showAllPol + "&showBooking=" + flag+ "&showPreReleased=" + showPreReleased;
    }
    document.location.href = href;
}
function preReleasedDR(path, unitId, originId, finalDestinationId, unitssId, headerId) {
    showProgressBar();
    var filterByChanges = parent.document.getElementById("filterByChanges").value;
    var serviceType = $('#serviceType').val();
    var flag = $("#showBooking").attr("checked") ? true : false;
    var showAllPol = $("#showAllPol").attr("checked") ? true : false;
    var showPreReleased = $("#showPreReleased").attr("checked") ? true : false;
    var href = path + "/lclAddVoyage.do?methodName=viewCurrentUnitForDomestic&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId="
                + finalDestinationId + "&unitssId=" + unitssId + "&headerId=" + headerId + "&filterByChanges="
                + filterByChanges + "&displayLoadComplete=N&detailId=" + $("#detailId").val() + "&schServiceType=" + serviceType
                + "&intransitDr=" + $("#intransitDr").val() + "&showAllDr=" + showAllPol + "&showBooking=" + flag+ "&showPreReleased=" + showPreReleased;
    document.location.href = href;
}

function checkFileNumberLock(path, fileId, fileNo, userId) {
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO",
            methodName: "checkFileNumberLocking",
            param1: fileNo,
            param2: userId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data === null || data === "") {
                gotoDrFromUnitLoadScreen(path, fileId);
            } else if (data.indexOf("is already opened in another window") > -1) {
                $.prompt(data);
            } else {
                $.prompt(data + ". Do you want to view the file?", {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v === 1) {
                            gotoDrFromUnitLoadScreen(path, fileId);
                        } else {
                            $.prompt.close();
                        }
                    }
                });
            }
        }
    });
}

function  gotoDrFromUnitLoadScreen(path, fileId) {
    var headerId = $('#headerId').val();
    var unitSsId = $('#unitssId').val();
    var filter = parent.$("#filterByChanges").val();
    var detailId = $("#detailId").val();
    var intransitDr = $("#intransitDr").val();
    var fromScreen = "UnitLoadScreenToBooking";
    var toScreen = "EXP VOYAGE";
    window.parent.parent.goToBookingFromVoyage(path, fileId, filter, headerId, detailId, unitSsId, fromScreen, toScreen, intransitDr);
}

function calculatePieceCount() {
    var pieceCount = 0.000;
    var pieceVolume = 0.000;
    var pieceweight = 0.000;
    var drCount = 0;
    var filter = parent.$("#filterByChanges").val();
    if (filter !== 'lclDomestic') {
        $(".drPieceCount").each(function () {
            pieceCount = Number(pieceCount) + Number($(this).text().trim());
            pieceCount = pieceCount.toFixed(0);
            drCount = drCount + 1;
        });
        $(".drPieceVolume").each(function () {
            pieceVolume = Number(pieceVolume) + Number($(this).text().trim());
            pieceVolume = pieceVolume.toFixed(0);
        });
        $(".drPieceWeight").each(function () {
            pieceweight = Number(pieceweight) + Number($(this).text().trim());
            pieceweight = pieceweight.toFixed(0);
        });
    }
    $(".drCount").text(drCount);
    $(".pieceCount").text(pieceCount);
    $(".pieceVolume").text(pieceVolume);
    $(".pieceWeight").text(pieceweight);
}


function checkCFtisValid(currentcft) {
    var cft = Number(currentcft);
    var flag = true;
    if (containerSize === "40RE") {
        flag = cft > 1800 ? false : true;
    } else if (containerSize === "40FT") {
        flag = cft > 2000 ? false : true;
    } else if (containerSize === "40HC") {
        flag = cft > 2200 ? false : true;
    } else if (containerSize === "20FT") {
        flag = cft > 1000 ? false : true;
    } else if (containerSize === "45FT") {
        flag = cft > 2500 ? false : true;
    } else if (containerSize === "53FT") {
        flag = cft > 3000 ? false : true;
    } else if (containerSize === "LCL") {
        flag = true;
    }
    return flag;
}

function checkBookVoyAndPickVoy(fileId) {
    var pickedVoyNo = $('#pickedVoyNo').val();
    var filterByChanges = parent.$("#filterByChanges").val();
    var bookVoyNo = $(".destuffed" + fileId).text();
    var pickedDrRoleDuty = getPickedDrWarnings();
    if (null !== bookVoyNo && bookVoyNo !== '' && filterByChanges === 'lclExport') {
        if (bookVoyNo !== pickedVoyNo && pickedDrRoleDuty) {
            var txt = "File# " + $(".fileNo" + fileId).val() + " is not Booked for this voyage,Are you Sure?";
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        $("#" + fileId).attr('checked', true);
                        showConsolidationData(fileId);
                    } else {
                        $("#" + fileId).attr('checked', false);
                        $.prompt.close();
                        showCalcultedCftLbsPopUp();
                    }
                }
            });
        } else if ($("#bookScheduleNo").is(":checked")) {
            validateConsolidateFileVoyageLogic(fileId);
        } else {
            showConsolidationData(fileId);
        }
    } else {
        showConsolidationData(fileId);
    }
}

function validateConsolidateFileVoyageLogic(fileId) {
    var data = getConsolidatePickOnAnotherVoyage(fileId);
    if (data !== '') {
        $.prompt("DR <span class='red'>" + $(".fileNo" + fileId).val() + "</span> in this consolidation is booked for a different voyage," +
                " you will need to pick that one separately.", {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v === 1) {
                            showConsolidationData(fileId);
                        } else {
                            $("#" + fileId).attr("checked", false);
                            $.prompt.close();
                            showCalcultedCftLbsPopUp();
                        }
                    }
                });
    } else {
        showConsolidationData(fileId);
    }
}

function confirmTopick(txt, fileId, measure) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                $(".dimensionsRemarks" + fileId).text("$" + measure + "*" + $(".fileNo" + fileId).val());
                checkBookVoyAndPickVoy(fileId);
            } else {
                $("#" + fileId).attr("checked", false);
            }
        }
    });
}

function checkDimensions(fileId, length, height, width, weight) {
    var fileNo = $(".fileNo" + fileId).val();
    var flag = $("#" + fileId).is(":checked");
    if (flag) {
        var flag2 = false;
        var pickedDrRoleDuty = getPickedDrWarnings();
        if (containerSize === '20FT' || containerSize === '40FT') {
            if (height > 89 && pickedDrRoleDuty) {
                var txt = "DR# <font style='color:red;'>" + fileNo + "</font> may not fit in the container: " +
                        " Height is  <font style='color:red;'>" + height + "</font>  inches (greater than 89 inches). Do you want to continue?";
                confirmTopick(txt, fileId, "Height");
                flag2 = true;
            } else if (length > 232 && containerSize !== '40FT' && pickedDrRoleDuty) {
                var txt = "DR# <font style='color:red;'>" + fileNo + "</font> may not fit in the container: " +
                        " Length is  <font style='color:red;'>" + length + "</font>  inches (greater than 232 inches). Do you want to continue?";
                confirmTopick(txt, fileId, "Length");
                flag2 = true;
            } else if (length > 92 && width > 92 && pickedDrRoleDuty) {
                var txt = "The piece may not fit in the container: Width/Length for Dr# <font style='color:red;'>" + fileNo +
                        "</font> are both greater than 92 inches so it may not fit in the container. Do you want to continue?";
                confirmTopick(txt, fileId, "Width/Length");
                flag2 = true;
            }
        } else if (containerSize === '40RE') {
            if (height > 86 && pickedDrRoleDuty) {
                var txt = "DR# <font style='color:red;'>" + fileNo + "</font> may not fit in the container: " +
                        " Height is  <font style='color:red;'>" + height + "</font>  inches (greater than 86 inches). Do you want to continue?";
                confirmTopick(txt, fileId, "Height");
                flag2 = true;
            } else if (length > 86 && width > 86 && pickedDrRoleDuty) {
                var txt = "The piece may not fit in the container: Width/Length for Dr# <font style='color:red;'>" + fileNo +
                        "</font> are both greater than 86 inches so it may not fit in the container. Do you want to continue?";
                confirmTopick(txt, fileId, "Width/Length");
                flag2 = true;
            }
        } else if (containerSize === '40HC' || containerSize === '45FT') {
            if (height > 102 && pickedDrRoleDuty) {
                var txt = "DR# <font style='color:red;'>" + fileNo + "</font> may not fit in the container: " +
                        " Height is  <font style='color:red;'>" + height + "</font>  inches (greater than 102 inches). Do you want to continue?";
                confirmTopick(txt, fileId, "Height");
                flag2 = true;
            } else if (length > 92 && width > 92 && pickedDrRoleDuty) {
                var txt = "The piece may not fit in the container: Width/Length for Dr# <font style='color:red;'>" + fileNo +
                        "</font> are both greater than 92 inches so it may not fit in the container. Do you want to continue?";
                confirmTopick(txt, fileNo, "Width/Length");
                flag2 = true;
            }
        } else if (containerSize === '45WD') {
            if (length > 92 && width > 92 && pickedDrRoleDuty) {
                // condition 3;
            }
        } else if (containerSize === '53FT') {
            if (length > 92 && width > 92 && pickedDrRoleDuty) {
                var txt = "The piece may not fit in the container: Width/Length for Dr# <font style='color:red;'>" + fileNo +
                        "</font> are both greater than 92 inches so it may not fit in the container. Do you want to continue?";
                confirmTopick(txt, fileId, "Width/Length");
                flag2 = true;
            }
        }
        if (!flag2) {
            checkBookVoyAndPickVoy(fileId);
        }
    } else {
        pickORUnPickConsolidateDr(fileId, false);
        removeDimenisionsNotes(fileId);
    }
    showCalcultedCftLbsPopUp();
}

function showCalcultedCftLbsPopUp() {
    var flagT = false;
    var checkedCft = "", checkedWgt = "";
    $(".chkSaveDeStuff").each(function () {
        if ($(this).is(":checked")) {
            $("#showPicked").show();
            flagT = true;
            checkedCft = Number(checkedCft) + Number($(".volume" + $(this).val()).text());
            checkedWgt = Number(checkedWgt) + Number($(".weight" + $(this).val()).text());
        }
        else if (!flagT) {
            $("#showPicked").hide();
        }
    });

    document.getElementById('cuft').innerHTML = checkedCft + " CFT ,";
    document.getElementById('pounds').innerHTML = checkedWgt + " LBS";

    var totalCft = Number($(".totalvolume" + $("#hiddenUnitId").val()).text()) + Number(checkedCft);
    var totalWgt = Number($(".totalWeight" + $("#hiddenUnitId").val()).text()) + Number(checkedWgt);

    document.getElementById('totalCft').innerHTML = totalCft + " CFT ,";
    document.getElementById('totalWgt').innerHTML = totalWgt + " LBS";
}

function removeDimenisionsNotes(fileNo) {
    $(".dimensionsRemarks" + fileNo).text("");
}

function save(path, unitId, originId, finalDestinationId) {
    var chkSaveDeStuff = [];
    var checkedCft = "", checkedWgt = "";
    $(".chkSaveDeStuff:checked").each(function () {
        chkSaveDeStuff.push($(this).val());
        checkedCft = Number(checkedCft) + Number($(".volume" + $(this).val()).text());
        checkedWgt = Number(checkedWgt) + Number($(".weight" + $(this).val()).text());
    });
    if (chkSaveDeStuff.length <= 0) {
        $.prompt("Please select atleast one Destuffed record");
        return false;
    }
    var totalCft = Number($(".totalvolume" + $("#hiddenUnitId").val()).text()) + Number(checkedCft);
    var totalWgt = Number($(".totalWeight" + $("#hiddenUnitId").val()).text()) + Number(checkedWgt);
    var volumeflag = checkCFtisValid(totalCft);
    var volumetxt = "", weighttxt = "";
    var pickedDrRoleDuty = getPickedDrWarnings();
    if (!volumeflag && pickedDrRoleDuty) {
        volumetxt = "Total CFT ( <font style='color:red;'>" + totalCft + " </font> CFT) may exceed limits of a <font style='color:red;'>" +
                $("#containerSize").val().replace("container", "") + " </font> container. Please check. Do you want to continue";
    }
    if (totalWgt >= 38000 && $("#containerSize").val().substring(0, 4) !== 'LCL' && pickedDrRoleDuty) {
        weighttxt = "Total unit weight is exceeding <font style='color:red;'> 38,000 LBS.</font> Please check. Do you want to continue";
    }
    // the below condition is checked for append the alert for if Both VOLUME & WEIGHT exists Please refer 7934.
    if (volumetxt !== "" && weighttxt !== "") {
        $.prompt(volumetxt, {
            button: {
                Ok: 1
            },
            submit: function (v) {
                if (v) {
                    volumeWightAlert(weighttxt, path, unitId, originId, finalDestinationId, chkSaveDeStuff);
                }
            }
        });
    } else if (volumetxt !== "" && weighttxt === "") {
        volumeWightAlert(volumetxt, path, unitId, originId, finalDestinationId, chkSaveDeStuff);
    } else if (volumetxt === "" && weighttxt !== "") {
        volumeWightAlert(weighttxt, path, unitId, originId, finalDestinationId, chkSaveDeStuff);
    } else {
        submitForm(path, unitId, originId, finalDestinationId, chkSaveDeStuff);
    }
// ------------------------End Here -----------------------------//
}
function volumeWightAlert(txt, path, unitId, originId, finalDestinationId, chkSaveDeStuff) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                submitForm(path, unitId, originId, finalDestinationId, chkSaveDeStuff);
            } else {
                $.prompt.close();
            }
        }
    });
}
function submitForm(path, unitId, originId, finalDestinationId, chkSaveDeStuff) {
    showProgressBar();
    var unitssId = $("#unitssId").val();
    var flag = $("#showAllPol").attr("checked") ? true : false;
    var showOBKG = $("#showBooking").attr("checked") ? true : false;
    var filterByChanges = parent.$("#filterByChanges").val();
    var index = $("#index").val();
    var scheduleNo = parent.$("#schedule").val();
    var unitNo = $('#unitNo').val();
    var headerId = $('#headerId').val();
    $("#hiddenSaveDestuff").val(chkSaveDeStuff);
    var dimensions = getDimensionsRemarks();
    var serviceType = $('#serviceType').val();
    var appendFlag = "";
    if (filterByChanges !== 'lclDomestic') {
        var isReleasedDr = $('#isReleasedDr').val();
        var bookScheduleNo = $('#bookScheduleNo').val();
        var isReleaseWithCurLoc = $("#checkAllRealeaseWithCurrLoc").attr("checked") ? true : false;
        appendFlag = "&isReleasedDr=" + isReleasedDr + "&bookScheduleNo=" + bookScheduleNo
                + "&checkAllRealeaseWithCurrLoc=" + isReleaseWithCurLoc;
    }
    var href = path + "/lclAddVoyage.do?methodName=saveDestuffedUnits&unitId=" + unitId + "&unitssId=" + unitssId + "&headerId=" + headerId
            + "&destuffedFileNumbers=" + $("#hiddenSaveDestuff").val() + "&originId=" + originId + "&finalDestinationId="
            + finalDestinationId + "&index=" + index + "&unitNo=" + unitNo + "&scheduleNo=" + scheduleNo + "&filterByChanges="
            + filterByChanges + "&detailId=" + $("#detailId").val() + "&intransitDr=" + $("#intransitDr").val()
            + "&showAllDr=" + flag + "&showBooking=" + showOBKG + "&dimensionsRemarks=" + dimensions + "&schServiceType=" + serviceType + appendFlag;
    document.location.href = href;
}

function getDimensionsRemarks() {
    var dimensionsRemarks = [];
    $(".chkSaveDeStuff:checked").each(function () {
        dimensionsRemarks.push($(".dimensionsRemarks" + $(this).attr("id")).text());
    });
    return dimensionsRemarks;
}

jQuery(document).ready(function () {
    $("[title != '']").not("link").tooltip();
});

function bookedVoyageNo(bookForVoy, path, unitId) {
    var bkVoyNo = bookForVoy.value;
    var originId = $('#originId').val();
    var polEtd = $('#polEtd').val();
    var finalDestinationId = $('#finalDestinationId').val();
    var headerId = $('#headerId').val();
    var unitssId = $('#unitssId').val();
    var serviceType = $('#serviceType').val();
    var filterByChanges = parent.document.getElementById("filterByChanges").value;
    showProgressBar();
    var href = path + "/lclAddVoyage.do?methodName=viewCurrentUnit&bookScheduleNo=" + bkVoyNo + "&originId=" + originId + "&finalDestinationId=" + finalDestinationId + "&unitId=" + unitId + "&unitssId=" + unitssId + "&headerId="
            + headerId + "&filterByChanges=" + filterByChanges + "&polEtd=" + polEtd + "&displayLoadComplete=N&detailId=" + $("#detailId").val()
            + "&schServiceType=" + serviceType;
    document.location.href = href;
}
function editBooking(path, fileId) {
    var voyageHeaderId = $('#headerId').val();
    var voyageDetailId = $("#detailId").val();
    var voyageFilter = parent.document.getElementById("filterByChanges").value;
    window.parent.parent.changeExportLclUnitsVoyages(path, fileId, 'B', voyageHeaderId,
            voyageDetailId, voyageFilter, '', 'loadScreen');
}

function showCurrentVoyage(path, unitId, originId, finalDestinationId, unitssId, headerId) {
    showProgressBar();
    $('#isReleasedDr').attr("checked", false);
    $('#checkAllRealeaseWithCurrLoc').attr("checked", false);
    var filterByChanges = parent.$("#filterByChanges").val();
    var serviceType = $('#serviceType').val();
    var href = path + "/lclAddVoyage.do?methodName=viewCurrentUnit&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId="
            + finalDestinationId + "&unitssId=" + unitssId + "&headerId=" + headerId + "&filterByChanges=" +
            filterByChanges + "&displayLoadComplete=N&isReleasedDr=N&detailId=" + $("#detailId").val() + "&bookScheduleNo=Y&schServiceType=" + serviceType;
    document.location.href = href;
}
function showAllReleased(path, unitId, originId, finalDestinationId, unitssId, headerId) {
    showProgressBar();
    $('#bookScheduleNo').attr("checked", false);
    $('#checkAllRealeaseWithCurrLoc').attr("checked", false);
    var filterByChanges = parent.$("#filterByChanges").val();
    var serviceType = $('#serviceType').val();
    var href = path + "/lclAddVoyage.do?methodName=viewCurrentUnit&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId="
            + finalDestinationId + "&unitssId=" + unitssId + "&headerId=" + headerId + "&filterByChanges=" +
            filterByChanges + "&displayLoadComplete=N&bookScheduleNo=N&detailId=" + $("#detailId").val() + "&isReleasedDr=Y&schServiceType=" + serviceType;
    document.location.href = href;
}
function showAllReleasedIncludeCurrloc(path, unitId, originId, finalDestinationId, unitssId, headerId) {
    showProgressBar();
    $('#bookScheduleNo').attr("checked", false);
    $('#isReleasedDr').attr("checked", false);
    var filterByChanges = parent.$("#filterByChanges").val();
    var serviceType = $('#serviceType').val();
    var href = path + "/lclAddVoyage.do?methodName=viewCurrentUnit&unitId=" + unitId + "&originId=" + originId + "&finalDestinationId="
            + finalDestinationId + "&unitssId=" + unitssId + "&headerId=" + headerId + "&filterByChanges=" +
            filterByChanges + "&displayLoadComplete=N&bookScheduleNo=N&detailId=" + $("#detailId").val()
            + "&isReleasedDr=N&schServiceType=" + serviceType + "&checkAllRealeaseWithCurrLoc=true";
    document.location.href = href;
}

function showConsolidationData(fileId) {
    var consoliatedNumbers = $("." + fileId).val();
    if (consoliatedNumbers !== '') {
        var value = validateConsolidateDr(consoliatedNumbers);
        if (value !== "") {
            var txt = "<span class='red'>" + $(".fileNo" + fileId).val() + "</span> is consolidated with <span class='red'>"
                    + value + "</span>, but being picked on a different unit. Are you sure you want to proceed?";
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                async: false,
                submit: function (v) {
                    if (v === 1) {
                        openConsolidateDrPage(fileId, consoliatedNumbers);
                    } else {
                        $("#" + fileId).attr("checked", false);
                        $("#" + fileId).attr("disabled", false);
                        showCalcultedCftLbsPopUp();
                    }
                }
            });
        } else {
            openConsolidateDrPage(fileId, consoliatedNumbers);
        }
    }
}

function openConsolidateDrPage(fileId, consoliatedNumbers) {
    var href = $("#path").val() + "/lclAddVoyage.do?methodName=openConsolidatePage"
            + "&fileNumberId=" + fileId + "&fileNumber=" + $(".fileNo" + fileId).val() + "&consolidateFiles=" + consoliatedNumbers
            + "&filterByChanges=" + parent.$("#filterByChanges").val()
            + "&headerId=" + parent.$("#headerId").val();
    $.colorbox({
        iframe: true,
        href: href,
        height: "75%",
        width: "50%",
        title: "Consolidated List"
    });
}

function pickORUnPickConsolidateDr(fileId, flag) {
    var consolidatedIds = $("." + fileId).val();
    if (flag) {
        $("#" + fileId).attr("checked", flag);
        var pickedVoyNo = $('#pickedVoyNo').val();
        var filterByChanges = parent.$("#filterByChanges").val();
        var bookVoyNo = $(".destuffed" + fileId).text();
        if (null !== bookVoyNo && bookVoyNo !== '' && filterByChanges === 'lclExport') {
            if ((bookVoyNo === pickedVoyNo) && $("#bookScheduleNo").is(":checked")) {
                validateConsolidateFileVoyageLogic(fileId);
            } else {
                showConsolidationData(fileId);
            }
        } else {
            showConsolidationData(fileId);
        }
    }
    if (!flag && consolidatedIds !== '') {
        var val = consolidatedIds.split(",");
        for (var i = 0; i < val.length; i++) {
            $("#" + val[i]).attr("checked", flag);
            $("#" + val[i]).attr("disabled", flag);
            $("#consolidateIcon" + val[i]).show();
        }
    }
    showCalcultedCftLbsPopUp();
}

function validateConsolidateDr(consoliatedNumbers) {
    var value = "";
    var unitssId = $("#unitssId").val();
    if (consoliatedNumbers !== '') {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO",
                methodName: "getPickedDrInDiffferentUnit",
                param1: consoliatedNumbers,
                param2: unitssId,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data !== null && data !== "") {
                    value = data;
                }
            }
        });
    }
    return value;
}

function getConsolidatePickOnAnotherVoyage(fileId) {
    var value = "";
    var consoliatedNumbers = $("." + fileId).val();
    if (consoliatedNumbers !== '') {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO",
                methodName: "getBookedOnAnotherVoyageDRList",
                param1: consoliatedNumbers,
                param2: parent.$("#headerId").val(),
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data !== null && data !== "") {
                    value = data;
                }
            }
        });
    }
    return value;
}
function getPickedDrWarnings() {
    var pickedDr = true;
    var roleId = $('#userRoleId').val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO",
            methodName: "getRoleDetails",
            param1: "pick_dr_warnings",
            param2: roleId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            pickedDr = data;
        }
    });
    return pickedDr;
}

function checkConsolidation(fileId) {
    var consolidateIds = $(".consolidate" + fileId).val();
    if (consolidateIds !== '') {
        var consolidateIdArr = consolidateIds.split(",");
        var fileNo = new Array();
        for (var i = 0; i < consolidateIdArr.length; i++) {
            if ($(".pickedFileNo" + consolidateIdArr[i]).val() !== undefined) {
                fileNo.push($(".pickedFileNo" + consolidateIdArr[i]).val());
            }
        }
        if (fileNo.length > 0) {
            if ($("#pickedFileId" + fileId).is(":checked")) {
                setConsolidateDR(consolidateIdArr, fileId, fileNo);
            } else {
                for (var i = 0; i < consolidateIdArr.length; i++) {
                    $("#pickedFileId" + consolidateIdArr[i]).attr("checked", false);
                    $("#pickedFileId" + consolidateIdArr[i]).attr("disabled", false);
                    $("#consolidateIcon" + consolidateIdArr[i]).show();
                }
            }
        }
    }
}

function checkConsoPickedIcon(fileId) {
    var consolidateIds = $(".consolidate" + fileId).val();
    if (consolidateIds !== '') {
        var consolidateIdArr = consolidateIds.split(",");
        var fileNo = new Array();
        for (var i = 0; i < consolidateIdArr.length; i++) {
            if ($(".pickedFileNo" + consolidateIdArr[i]).val() !== undefined) {
                fileNo.push($(".pickedFileNo" + consolidateIdArr[i]).val());
            }
        }
        if (fileNo.length > 0) {
            setConsolidateDR(consolidateIdArr, fileId, fileNo);
        }
    }
}

function setConsolidateDR(consolidateIdArr, fileId, fileNo) {
    var txt = "The file is Consolidated with (<span class='red'>" + fileNo
            + "</span>). Do you want to Un-Pick these DRs as well?";
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2,
            Cancel: 3
        },
        submit: function (v) {
            if (v === 1) {
                $("#pickedFileId" + fileId).attr("checked", true);
                for (var i = 0; i < consolidateIdArr.length; i++) {
                    $("#pickedFileId" + consolidateIdArr[i]).attr("checked", true);
                    $("#pickedFileId" + consolidateIdArr[i]).attr("disabled", true);
                    $("#consolidateIcon" + consolidateIdArr[i]).hide();
                }
            } else if (v === 2) {
                $("#pickedFileId" + fileId).attr("checked", true);
            } else if (v === 3) {
                $("#pickedFileId" + fileId).attr("checked", false);
            }
        }
    });
}
