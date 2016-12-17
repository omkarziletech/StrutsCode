var totalReleasedlbs = 0.000;
var totalReleasedcft = 0.000;
var totaldrsForR = 0;
var lbsValues = 0.000;
var cftValues = 0.000;
function checkLock(path, fileNumber, fileId, moduleId, moduleName, transhipment, callBack) {
    $("#methodName").val("checkLocking");
    $('#fileNumber').val(fileNumber);
    $('#userId').val($("#userId").val());
    var params = $("#lclSearchForm").serialize();
    params += "&moduleId=" + moduleId;
    $.post($("#lclSearchForm").attr("action"), params, function (data) {
        if (data === 'available') {
            callBackMethod(path, fileId, moduleId, moduleName, transhipment, callBack);
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
                        callBackMethod(path, fileId, moduleId, moduleName, transhipment, callBack);
                    } else if (v === 2) {
                        $.prompt.close();
                    }
                }
            });
        }
    });
}
function callBackMethod(path, fileId, moduleId, moduleName, transhipment, callBack) {
    callBack = transhipment === "false" ? "" : callBack;
    window.parent.changeLclChilds(path, fileId, moduleId, moduleName, callBack);
}

function checkFileNumber(path, fileId, moduleId, moduleName, callBack) {
    window.parent.changeLclChilds(path, fileId, moduleId, moduleName, callBack);
}

function searchBack()
{
    $("#methodName").val("goBackScreen");
    $("#lclSearchForm").submit();
}

function showConsolidate(path, methodName, filenumber, filenumberId, conoslidatedFiles, commodity) {
    var url = path + '/lclSearch.do?methodName=' + methodName + '&fileNo=' + filenumber + '&fileNumberA=' + filenumberId
            + "&conoslidateFiles=" + conoslidatedFiles + "&commodity=" + commodity + "&moduleName=Exports";
    window.location = url;
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

function submitForm(methodName) {
    if ($("#fileNumber").val() !== "") {
        $("#methodName").val(methodName);
        $('#filterBy').val("All");
        $("#searchFileNo").val("Y");
        $("#lclSearchForm").submit();
    }
}


function setResultHeight() {
    if ($(".scrollable-table").length > 0) {
        var windowHeight = window.parent.getFrameHeight();
        var height = windowHeight;
        height -= 50;
        $(".scrollable-table").height(height);
        $("body").css("overflow-y", "hidden");
    }
}

$(document).ready(function () {
    totalCftLbs();
    $(window).resize(function () {
        window.parent.changeHeight();
        setResultHeight();
    });
    $(".filterable").tablefilter({
        class: "table-filter",
        container: "#file"
    });
});
function totalCftLbs() {
    var totalcft = 0.000;
    var totallbs = 0.000;
    var uom = $('#uom').val();
    if (uom === 'I') {
        $(".CFT").each(function () {
            totalReleasedcft = Number(totalReleasedcft) + Number($(this).text().trim());
            totalReleasedcft = totalReleasedcft.toFixed(2);
        });
        $(".nonReleasedCFT").each(function () {
            totalcft = Number(totalcft) + Number($(this).text().trim());
            totalcft = totalcft.toFixed(2);
        });
        $(".LBS").each(function () {
            totalReleasedlbs = Number(totalReleasedlbs) + Number($(this).text().trim());
            totalReleasedlbs = totalReleasedlbs.toFixed(2);
        });
        $(".nonReleasedLBS").each(function () {
            totallbs = Number(totallbs) + Number($(this).text().trim());
            totallbs = totallbs.toFixed(2);
        });
    }
    if (uom === 'M') {
        $(".CBM").each(function () {
            totalReleasedcft = Number(totalReleasedcft) + Number($(this).text().trim());
            totalReleasedcft = totalReleasedcft.toFixed(2);
        });
        $(".nonReleasedCBM").each(function () {
            totalcft = Number(totalcft) + Number($(this).text().trim());
            totalcft = totalcft.toFixed(2);
        });
        $(".KGS").each(function () {
            totalReleasedlbs = Number(totalReleasedlbs) + Number($(this).text().trim());
            totalReleasedlbs = totalReleasedlbs.toFixed(2);
        });
        $(".nonReleasedKGS").each(function () {
            totallbs = Number(totallbs) + Number($(this).text().trim());
            totallbs = totallbs.toFixed(2);
        });
    }
    $('.releaseStatusFlag').each(function () {
        totaldrsForR += 1;
    });
    var filter = $('#filterBy').val();
    if (filter === 'IWB' || filter === 'IPO') {
        if (uom === 'I') {
            document.getElementById('cftlbs').innerHTML = "<span style='color:blue;font-weight: bold'>" + totalcft
                    + "</span>" + " CFT /" + "<span style='color:blue;font-weight: bold'>" + totallbs + "</span>" + " LBS";
            document.getElementById('cftlbsForR').innerHTML = "<span style='color:#800080;font-weight: bold'>"
                    + totalReleasedcft + "</span>" + " CFT /" + "<span style='color:#800080;font-weight: bold'>" + totalReleasedlbs + "</span>" + " LBS ," + "<span style='color:#800080;font-weight: bold'>" + totaldrsForR + "</span>" + " DRs";
        }
        if (uom === 'M') {
            document.getElementById('cbmkgs').innerHTML = "<span style='color:blue;font-weight: bold'>"
                    + totalcft + "</span>" + " CBM /" + "<span style='color:blue;font-weight: bold'>" + totallbs + "</span>" + " KGS";
            document.getElementById('cbmkgsForR').innerHTML = "<span style='color:#800080;font-weight: bold'>"
                    + totalReleasedcft + "</span>" + " CBM /" + "<span style='color:#800080;font-weight: bold'>"
                    + totalReleasedlbs + "</span>" + " KGS";
        }
    }
}

function doSortAscDesc(sortByValue) {
    var searchType = $("#searchType").val();
    var toggleValue = searchType === "up" ? "down" : "up";
    $("#" + sortByValue).removeClass(searchType).addClass(toggleValue);
    $("#sortByValue").val(sortByValue);
    $("#searchType").val(toggleValue);
    window.parent.showLoading();
    $("#methodName").val("doSortAscDesc");
    $("#lclSearchForm").submit();
}

function checkingCode(fileId, destination, portOfDestination) {
    var codeValue = "";
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkHsCode",
            param1: fileId,
            param2: destination,
            param3: portOfDestination,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            codeValue = data;
        }
    });
    return codeValue;
}

function openTrackingPopUp(path, methodName, filenumber, filenumberId) {
    var href = path + '/lclTracking.do?methodName=' + methodName + '&fileNumber=' + filenumber + '&fileId=' + filenumberId;
    $(".track").attr("href", href);
    $(".track").colorbox({
        iframe: true,
        width: "65%",
        height: "65%",
        title: "Tracking"
    });
}
function openPriorityNotesPopUp(path, methodName, filenumber, filenumberId) {
    var href = path + '/lclRemarks.do?methodName=' + methodName + '&fileNumber=' + filenumber + '&fileNumberId=' + filenumberId;
    $(".priority").attr("href", href);
    $(".priority").colorbox({
        iframe: true,
        width: "65%",
        height: "65%",
        title: "Priority Notes"
    });
}
function isChecked3pRef(fileId, hotCodeType, hotCodeValue) {
    var flag;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO",
            methodName: "isHotCodeExistForThreeDigit",
            param1: fileId,
            param2: hotCodeType,
            param3: hotCodeValue,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}
function isInbondExists(fileId) {
    var flag;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO",
            methodName: "isInbondExists",
            param1: fileId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}
function isHotCodePoa(fileId) {
    var flag;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO",
            methodName: "getHotCodePoa",
            param1: fileId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}
function poaRestrication(fileId) {
    var flag = true;
    var poa = isHotCodePoa(fileId);
    if (poa[0] == 'true') {
        if (poa[1] != "" || poa[2] != "") {
            var poaValues = isValidatePoa(poa[1], poa[2]);
            if (null !== poaValues && (poaValues[0] == 'Y' || poaValues[1] == 'Y')) {
                flag = false;
            } else {
                flag = true;
            }
        } else {
            flag = true;
        }
    } else {
        flag = false;
    }
    return flag;
}
function isValidatePoa(consacctNo, shipAcctNo) {
    var flag;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO",
            methodName: "isValidatePoa",
            param1: "",
            param2: consacctNo,
            param3: shipAcctNo,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}
function openPrintFaxPopUp(path) {
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO",
            methodName: "getBlPoolFiles",
            param1: $('#userId').val(),
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data != null && data != "") {
                GB_show("Print", path + "/printConfig.do?screenName=EditLCLBLPool&fileId=" + data, 350, 900);
            } else {
                $.prompt('No BL Pool for this login user');
            }
        }
    });
}



function isLinkedDr(fileId) {
    var flag = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
            methodName: "getBkgLinkedByDetails",
            param1: fileId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data != null && data != "" && data[0] != null && data[0] != "" && data[1] != null) {
                flag = "Cannot perform Un-release Operation.DR is already linked to Unit#<span style=color:red>" + data[1] + "</span> of Voyage#<span style=color:red>" + data[0] + "</span>"
            } else {
                flag = "";
            }
        }
    });
    return flag;
}

function openLoadUnits(path) {
    var polvalue = $("#voyageorigin").val();
    var podValue = $("#voyagedestination").val();
    var href = path + "/lclSearch.do?methodName=openVoyage&voyageorigin=" + polvalue + "&voyagedestination=" + podValue;
    $("#unitVoyage").attr("href", href);
    $("#unitVoyage").colorbox({
        iframe: true,
        width: "40%",
        height: "70%",
        title: "Load Units"
    });
}

function hazValidate(fileNumberId) {
    var flag = "";
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
            flag = data;
            // $.prompt('Hazmat info will be required prior to releasing this shipment.');
        }
    });
    return flag;
}

function getDispoCode(fileId) {
    var flag = false;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO",
            methodName: "getDispoCode",
            param1: fileId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data === 'INTX' || data === 'INTR') {
                flag = true;
            }
        }
    });
    return flag;
}
function clickSearchReleaseIcon(path, OriginCode, DestinationCode, fdCode, fileId, obj,
        cftValues, lbsValues, bkgScheduleNo, cfcl, consolidatedId, hold, isHazmat) {
    if ($(obj).attr("src").indexOf("unreleaseIcon") >= 0) {
        var buttonName = "UnReleaseFromMainScreen";
        Unrelease(path, buttonName, OriginCode, fileId, obj, cftValues, lbsValues);
    } else if (consolidatedId !== '') {
        var consolidateData = getConsolidateFileNumber(fileId, "R", "true");
        if (null !== consolidateData[1] && consolidateData[1] !== '') {
            validateConsolidatedDR(path, OriginCode, DestinationCode, fdCode,
                    fileId, obj, cftValues, lbsValues, cfcl, consolidateData[1], hold, bkgScheduleNo, consolidateData[0], isHazmat);
        } else {
            $.prompt("Are you sure you want to release?", {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        releaseDR(path, OriginCode, DestinationCode, fdCode,
                                fileId, obj, cftValues, lbsValues, bkgScheduleNo, cfcl, hold, isHazmat);
                    }
                    else {

                    }
                }
            });
        }
    } else {
        releaseDR(path, OriginCode, DestinationCode, fdCode,
                fileId, obj, cftValues, lbsValues, bkgScheduleNo, cfcl, hold, isHazmat);
    }
}
function validateConsolidatedDR(path, OriginCode, DestinationCode, fdCode,
        fileId, obj, cftValues, lbsValues, cfcl, consolidatedFiles, hold, bkgScheduleNo, fileIds, isHazmat) {
    var errorMessage = searchReleaseValidate(fileId, DestinationCode, fdCode, bkgScheduleNo, cfcl, hold, isHazmat);
    if (errorMessage !== "") {
        $.prompt(errorMessage);
        return false;
    }
    var txt = "This Dock Receipt is consolidated with(<span class='red'>" + consolidatedFiles + "</span>). Do you want to release these DRs as well?";
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2,
            Cancel: 3
        },
        submit: function (v) {
            if (v === 1) {
                releaseConsolidatedDR(path, OriginCode, fileId, obj, cftValues, lbsValues, fileIds);
            } else if (v === 2) {
                releaseParentDR(path, OriginCode, fileId, obj, cftValues, lbsValues);
            }
        }
    });
}
function releaseConsolidatedDR(path, OriginCode, fileId, obj, cftValues, lbsValues, consolidatedId) {
    var errorMessage = releaseValidateConsolidatedDR(fileId);
    if (errorMessage !== "") {
        $.prompt("Cannot release the following DRs, please check: \n<br>" + errorMessage);
        return false;
    }
    var uom = $('#uom').val();
    var filter = $('#filterBy').val();
    var buttonName = "ReleaseFromMainScreen";
    var originalFileStatus = $("#originalFileStatus" + fileId).val();
    var release = "<span style='color:red' onmouseover=\"tooltip.showSmall('<strong>Released for Export</strong>')\" onmouseout='tooltip.hide();'>"
            + originalFileStatus + " R" + "</span>";
    var params = "&fileNumberId=" + fileId + "&unLocCode=" + OriginCode + "&buttonName=" + buttonName;
    $.ajax({
        url: path + "/lclBooking.do?methodName=updateReleaseConsolidatedDR" + params,
        success: function (result) {
            if (result === 'R') {
                var classValue = $(".onHoldClass" + fileId).text();
                if (classValue === "OH") {
                    $(".statusClass" + fileId).html(release + "<span  style='color:red;font-weight: bold' title='On Hold' >," + "  OH  " + "</span>");
                } else {
                    $(".statusClass" + fileId).html(release);
                }
                if (filter === 'IWB' || filter === 'IPO') {
                    cftAndLabsCalculation(cftValues, lbsValues);
                    setCFTLBSValues(uom);
                }
                $("#release" + fileId).data('tipText', 'UnRelease');
                $(obj).attr("src", path + "/img/icons/unreleaseIcon.png");
                var fileno_array = consolidatedId.split(',');
                for (var i = 0; i < fileno_array.length; i++) {
                    var fileno = fileno_array[i];
                    $("#release" + fileno).data('tipText', 'UnRelease');
                    $("#Release" + fileno).attr("src", path + "/img/icons/unreleaseIcon.png");
                    classValue = $(".onHoldClass" + fileno).text();
                    originalFileStatus = $("#originalFileStatus" + fileno).val();
                    release = "<span style='color:red' onmouseover=\"tooltip.showSmall('<strong>Released for Export</strong>')\" onmouseout='tooltip.hide();'>"
                            + originalFileStatus + " R" + "</span>";
                    if (classValue === "OH") {
                        $(".statusClass" + fileno).html(release + "<span  style='color:red;font-weight: bold' title='On Hold' >," + "  OH  " + "</span>");
                    } else {
                        $(".statusClass" + fileno).html(release);
                    }
                }
            }
        }
    });
}
function releaseValidateConsolidatedDR(fileId) {
    var status = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "releaseValidateConsolidateDR",
            param1: fileId,
            param2: "R",
            dataType: "json",
            request: "true"
        },
        async: false,
        success: function (data) {
            status = data;
        }
    });
    return status;
}
function releaseParentDR(path, OriginCode, fileId, obj, cftValues, lbsValues) {
    var txt = "You will need to release the other Dock Receipts manually";
    var uom = $('#uom').val();
    var filter = $('#filterBy').val();
    var buttonName = "ReleaseFromMainScreen";
    $.prompt(txt, {
        buttons: {
            Ok: 1
        },
        submit: function (v) {
            if (v === 1) {
                var originalFileStatus = $("#originalFileStatus" + fileId).val();
                var release = "<span style='color:red' onmouseover=\"tooltip.showSmall('<strong>Released for Export</strong>')\" onmouseout='tooltip.hide();'>"
                        + originalFileStatus + " R" + "</span>";
                var params = "&fileNumberId=" + fileId + "&unLocCode=" + OriginCode + "&buttonName=" + buttonName;
                $.ajax({
                    url: path + "/lclBooking.do?methodName=updateRelease" + params,
                    success: function (result) {
                        if (result === 'R') {
                            var classValue = $(".onHoldClass" + fileId).text();
                            if (classValue === "OH") {
                                $(".statusClass" + fileId).html(release + "<span  style='color:red;font-weight: bold' title='On Hold' >,"
                                        + "  OH  " + "</span>");
                            } else {
                                $(".statusClass" + fileId).html(release);
                            }
                            if (filter === 'IWB' || filter === 'IPO') {
                                cftAndLabsCalculation(cftValues, lbsValues);
                                setCFTLBSValues(uom);
                            }
                            $("#release" + fileId).data('tipText', 'UnRelease');
                            $(obj).attr("src", path + "/img/icons/unreleaseIcon.png");
                        }
                    }
                });
            }
        }
    });
}

function getConsolidateFileNumber(fileId, buttonValue, isRelease) {
    var status = ""
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO",
            methodName: "getReleaseORPreReleaseConsolidate",
            param1: fileId,
            param2: buttonValue,
            param3: isRelease,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            status = data;
        }
    });
    return status;
}

function releaseDR(path, OriginCode, DestinationCode, fdCode,
        fileId, obj, cftValues, lbsValues, bkgScheduleNo, cfcl, hold, isHazmat) {
    var uom = $('#uom').val();
    var filter = $('#filterBy').val();
    var buttonName = "ReleaseFromMainScreen";
    var errorMessage = searchReleaseValidate(fileId, DestinationCode, fdCode, bkgScheduleNo, cfcl, hold, isHazmat);
    if (errorMessage !== "") {
        $.prompt(errorMessage);
        return false;
    }
    var originalFileStatus = $("#originalFileStatus" + fileId).val();
    var release = "<span style='color:red' onmouseover=\"tooltip.showSmall('<strong>Released for Export</strong>')\" onmouseout='tooltip.hide();'>"
            + originalFileStatus + " R" + "</span>";

    var params = "&fileNumberId=" + fileId + "&unLocCode=" + OriginCode + "&buttonName=" + buttonName;
    $.ajax({
        url: path + "/lclBooking.do?methodName=updateRelease" + params,
        success: function (result) {
            if (result === 'R') {
                var classValue = $(".onHoldClass" + fileId).text();
                if (classValue === "OH") {
                    $(".statusClass" + fileId).html(release + "<span  style='color:red;font-weight: bold' title='On Hold' >," + "  OH  " + "</span>");
                } else {
                    $(".statusClass" + fileId).html(release);
                }
                if (filter === 'IWB' || filter === 'IPO') {
                    cftAndLabsCalculation(cftValues, lbsValues);
                    setCFTLBSValues(uom);
                }
                $("#release" + fileId).data('tipText', 'UnRelease');
                $(obj).attr("src", path + "/img/icons/unreleaseIcon.png");
            }
        }
    });
}

function searchReleaseValidate(fileId, fd, pod, bkgScheduleNo, cfcl, hold, isHazmat) {
    var hazStatus = "";
    var flag = false;
    if (cfcl === "0") {
        if (releaseRoleDuty() && bkgScheduleNo === '') {
            flag = true;
        }
    }
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "validateReleaseBySearch",
            param1: fileId,
            param2: fd,
            param3: pod,
            param4: isHazmat,
            param5: hold,
            param6: flag,
            dataType: "json",
            request: "true"
        },
        async: false,
        success: function (data) {
            hazStatus = data;
        }
    });
    return hazStatus;
}
function printBkgReport(path, fileId, fileNo) {
    window.parent.showLoading();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclPrintUtil",
            methodName: "printBookingReport",
            param1: "",
            param2: fileId,
            param3: fileNo,
            param4: "Booking Confirmation Without Rate",
            request: "true"
        },
        success: function (data) {
            window.parent.closePreloading();
            window.open(path + '/servlet/FileViewerServlet?fileName=' + data, 'Booking', 'resizable=1,width=1200,height=700,top=40,left=40,directories=no,location=no,linemenubar=no,toolbar=no,status=no');
        }
    });
}

function displayLabelPopup(fileNo, fileNumberId) {
    showAlternateMask();
    $("#labelCopy").center().show(500, function () {
        document.getElementById('currentFileNo').innerHTML = fileNo;
        document.getElementById('fileNo').value = fileNo;
        document.getElementById('fileNumberId').value = fileNumberId;
    });
}
function closeLabelPopup() {
    $("#labelCopy").center().hide(500, function () {
        hideAlternateMask();
        $("#labelsCount").val('');
    });
}

function saveLabelCount() {
    var labelsCount = $("#labelsCount").val();
    var fileId = $("#fileNumberId").val();
    var fileNo = $("#fileNo").val();
    if ($("#labelsCount").val() === "") {
        $.prompt("Labels Filed is Required");
        return false;
    }
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "insertLabelCountFromSearchScreen",
            param1: fileId,
            param3: fileNo,
            param4: labelsCount,
            dataType: "json",
            request: "true"
        },
        async: false,
        success: function (data) {
            var $msg = $("#update-msg");
            var $filedName = $("#update-filedName");
            var $val = $("#update-val");
            $msg.text(data);
            $filedName.text("How many Labels :");
            $val.text(labelsCount);
            setTimeout(function () {
                $msg.text("");
                $val.text("");
                $filedName.text("");
                closeLabelPopup();
            }, 1500);
        }
    });
}

function validateLabelsValues() {
    if (parseInt($('#labelsCount').val()) === 0) {
        $.prompt("Label Copy cannot be 0");
        $('#labelsCount').val('');
        return false;
    }
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.logiware.common.dao.PropertyDAO",
            methodName: "getProperty",
            param1: "LabelPrintCount",
            dataType: "json"
        },
        success: function (data) {
            if (parseInt($('#labelsCount').val()) > data) {
                $.prompt("Labels Copy should be less than <span style=color:red>" + data + "</span>");
                $('#labelsCount').val('');
            }
        }
    });
}

function cftAndLabsCalculation(cftValues, lbsValues) {
    if ($("#templateCubeFlag").val().trim() !== "false" && $("#templateWeightFlag").val().trim() !== "false") {
        totalReleasedcft = Number(totalReleasedcft) + Number(cftValues);
        totalReleasedcft = totalReleasedcft.toFixed(2);
        totalReleasedlbs = Number(totalReleasedlbs) + Number(lbsValues);
        totalReleasedlbs = totalReleasedlbs.toFixed(2);
    } else if ($("#templateCubeFlag").val().trim() !== "false") {
        totalReleasedcft = Number(totalReleasedcft) + Number(cftValues);
        totalReleasedcft = totalReleasedcft.toFixed(2);
    } else if ($("#templateWeightFlag").val().trim() !== "false") {
        totalReleasedlbs = Number(totalReleasedlbs) + Number(lbsValues);
        totalReleasedlbs = totalReleasedlbs.toFixed(2);
    }
    totaldrsForR += 1;
}


function setCFTLBSValues(UOM) {
    if (UOM === 'I') {
        document.getElementById('cftlbsForR').innerHTML = "<span style='color:#800080;font-weight: bold'>"
                + totalReleasedcft + "</span>" + " CFT /" + "<span style='color:#800080;font-weight: bold'>"
                + totalReleasedlbs + "</span>" + " LBS ," + "<span style='color:#800080;font-weight: bold'>"
                + totaldrsForR + "</span>" + "DRs";
    }
    if (UOM === 'M') {
        document.getElementById('cbmkgsForR').innerHTML = "<span style='color:#800080;font-weight: bold'>"
                + totalReleasedcft + "</span>" + " CBM /" + "<span style='color:#800080;font-weight: bold'>"
                + totalReleasedlbs + "</span>" + " KGS";
    }
}

function Unrelease(path, buttonName, OriginCode, fileId, obj, cftValues, lbsValues) {
    var message = isLinkedDr(fileId);
    var consolidateData = getConsolidateFileNumber(fileId, "R", "false");
    if (message !== "") {
        $.prompt(message);
        return false;
    }
    else {
        var consolidateFile = consolidateFiles(fileId, "R", "false");
        var pickedConsolidate = getPickedFiles(consolidateFile[0]);
        var fileNumbers = "";
        var fId = "";
        consolidateFile = (consolidateFile == ',') ? "" : consolidateFile;
        pickedConsolidate = (pickedConsolidate == ',') ? "" : pickedConsolidate;
        pickedConsolidate = pickedConsolidate !== null ? pickedConsolidate : null;
        if (pickedConsolidate !== null && pickedConsolidate !== "") {
            var consId = consolidateFile[0];
            var PickId = pickedConsolidate[0];
            var consFileNo = consolidateFile[1];
            var PickFileNo = pickedConsolidate[1];
            var consolidateFileNo = new Array();
            var consolidateFileId = new Array();
            var pickedConsolidateFileNo = new Array();
            var pickedConsolidateFileId = new Array();
            consolidateFileNo = consFileNo.split(",");
            consolidateFileId = consId.split(",");
            pickedConsolidateFileId = PickId.split(",");
            pickedConsolidateFileNo = PickFileNo.split(",");
            for (i = 0; i < pickedConsolidateFileNo.length; i++) {
                consolidateFileNo.indexOf(pickedConsolidateFileNo[i]);
                consolidateFileNo.splice(consolidateFileNo.indexOf(pickedConsolidateFileNo[i]), 1);
            }
            for (i = 0; i < pickedConsolidateFileId.length; i++) {
                consolidateFileId.indexOf(pickedConsolidateFileId[i]);
                consolidateFileId.splice(consolidateFileId.indexOf(pickedConsolidateFileId[i]), 1);
            }
            consolidateFileId + "" + $("#fileNumberId").val();
        }
        fileNumbers = pickedConsolidate !== null && pickedConsolidate !== "" ? consolidateFileNo : consolidateFile[1];
        fId = pickedConsolidate !== null && pickedConsolidate !== "" ? pickedConsolidateFileId : fileId;
        var txt = "This Dock Receipt is consolidated with(<span class='red'>"
                + fileNumbers + "</span>). Do you want to Un-Release these DRs as well?";
        if (fileNumbers != "" && fileNumbers !== undefined && fileNumbers !== null) {
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2,
                    Cancel: 3
                },
                submit: function (v) {
                    if (v === 1) {
                        unReleaseConsolidateDr(path, buttonName, OriginCode, fId, obj, cftValues, lbsValues, consolidateData[0], consolidateFileId);
                    } else if (v === 2) {
                        unReleaseSingleDr(path, buttonName, OriginCode, fileId, obj, cftValues, lbsValues);
                    }
                }
            });
        }
        else {
            $.prompt("Are you sure you want to Un-release?", {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        unReleaseSingleDr(path, buttonName, OriginCode, fileId, obj, cftValues, lbsValues);
                    }
                }
            });
        }
    }
}
function unReleaseSingleDr(path, buttonName, OriginCode, fileId, obj, cftValues, lbsValues) {
    var uom = $('#uom').val();
    var filter = $('#filterBy').val();
    var releaseTool = "<span style='color:black' onmouseover=\"tooltip.showSmall('<strong>WareHouse Verified</strong>')\" onmouseout='tooltip.hide();'>";
    releaseTool += $("#originalFileStatus" + fileId).val();
    var params = "&fileNumberId=" + fileId + "&unLocCode=" + OriginCode + "&buttonName=" + buttonName;
    $.ajax({
        url: path + "/lclBooking.do?methodName=updateRelease" + params,
        success: function (result) {
            if (result === 'URPR' || result === 'UR') {
                releaseTool += result === "UR" ? " " : ",PRE";
                releaseTool += "</span>";
                var classValue = $(".onHoldClass" + fileId).text();
                if (classValue === "OH") {
                    $(".statusClass" + fileId).html(releaseTool + "<spanstyle='color:red;font-weight: bold' title='On Hold' >,"
                            + "  OH  " + "</span>");
                } else {
                    $(".statusClass" + fileId).html(releaseTool);
                }
                if (filter === 'IWB' || filter === 'IPO') {
                    cftAndLbsCalculationONUnRelease(cftValues, lbsValues);
                    setCFTLBSValues(uom);
                }
                $(obj).attr("src", path + "/img/icons/releaseicon.png");
                $("#release" + fileId).data('tipText', 'Release');
            }
        }
    });
}


function unReleaseConsolidateDr(path, buttonName, OriginCode, fileId, obj,
        cftValues, lbsValues, consolilatefileNo, consolidateFileId) {
    var uom = $('#uom').val();
    var filter = $('#filterBy').val();
    var originalFileStatus = $("#originalFileStatus" + fileId).val();
    var releaseTool = "<span style='color:black' onmouseover=\"tooltip.showSmall('<strong>WareHouse Verified</strong>')\" onmouseout='tooltip.hide();'>";
    releaseTool += $("#originalFileStatus" + fileId).val();
    var params = "&fileNumberId=" + fileId + "&unLocCode=" + OriginCode + "&buttonName=" + buttonName + "&consolidateFileId=" + consolidateFileId;
    $.ajax({
        url: path + "/lclBooking.do?methodName=updateReleaseConsolidatedDR" + params,
        success: function (result) {
            if (result === 'URPR' || result === 'UR') {
                releaseTool += result === "UR" ? " " : ",PRE";
                releaseTool += "</span>";
                var classValue = $(".onHoldClass" + fileId).text();
                if (classValue === "OH") {
                    $(".statusClass" + fileId).html(releaseTool + "<spanstyle='color:red;font-weight: bold' title='On Hold' >,"
                            + "  OH  " + "</span>");
                } else {
                    $(".statusClass" + fileId).html(releaseTool);
                }
                if (filter === 'IWB' || filter === 'IPO') {
                    cftAndLbsCalculationONUnRelease(cftValues, lbsValues);
                    setCFTLBSValues(uom);
                }
                $(obj).attr("src", path + "/img/icons/releaseicon.png");
                $("#release" + fileId).data('tipText', 'Release');
                var fileno_array = consolilatefileNo.split(',');
                for (var i = 0; i < fileno_array.length; i++) {
                    var fileno = fileno_array[i];
                    $("#release" + fileno).data('tipText', 'Release');
                    $("#Release" + fileno).attr("src", path + "/img/icons/releaseicon.png");
                    classValue = $(".onHoldClass" + fileno).text();
                    originalFileStatus = $("#preReleaseStatus" + fileno).val();
                    releaseTool = "<span style='color:black' onmouseover=\"tooltip.showSmall('<strong>WareHouse Verified</strong>')\" onmouseout='tooltip.hide();'>"
                            + originalFileStatus + "</span>";
                    if (classValue === "OH") {
                        $(".statusClass" + fileno).html(releaseTool + "<span  style='color:red;font-weight: bold' title='On Hold' >," + "  OH  " + "</span>");
                    } else {
                        $(".statusClass" + fileno).html(releaseTool);
                    }
                }
            }
        }
    });
}

function cftAndLbsCalculationONUnRelease(cftValues, lbsValues) {
    if ($("#templateCubeFlag").val().trim() !== "false" && $("#templateWeightFlag").val().trim() !== "false") {
        totalReleasedcft = Number(totalReleasedcft) - Number(cftValues);
        totalReleasedcft = totalReleasedcft.toFixed(2);
        totalReleasedlbs = Number(totalReleasedlbs) - Number(lbsValues);
        totalReleasedlbs = totalReleasedlbs.toFixed(2);
    } else if ($("#templateCubeFlag").val().trim() !== "false") {
        totalReleasedcft = Number(totalReleasedcft) - Number(cftValues);
        totalReleasedcft = totalReleasedcft.toFixed(2);
    } else if ($("#templateWeightFlag").val().trim() !== "false") {
        totalReleasedlbs = Number(totalReleasedlbs) - Number(lbsValues);
        totalReleasedlbs = totalReleasedlbs.toFixed(2);
    }
    totaldrsForR -= 1;
}
function consolidateFiles(fileId, buttonValue, isRelease) {
    var status = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO",
            methodName: "getReleaseORPreReleaseConsolidate",
            param1: fileId,
            param2: buttonValue,
            param3: isRelease,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            status = data;
        }
    });
    return status;
}
function getPickedFiles(consolidateFileNumber) {
    var cons = consolidateFileNumber;
    var pickedFile;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
            methodName: "getPickedVoyage",
            param1: cons,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            pickedFile = data;
        }
    });
    return pickedFile;
}