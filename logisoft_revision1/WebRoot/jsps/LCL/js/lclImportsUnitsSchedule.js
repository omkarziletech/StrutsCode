//Disposition Port PDF
function viewDispPdf(fileId, fileNumber, path) {
    var reportPreviewLocation = $('#reportPreviewLocation').val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclPrintUtil",
            methodName: "printUnitImpBkg",
            param1: fileId,
            param2: fileNumber,
            param3: "Arrival Notice",
            request: "true"
        },
        async: false,
        success: function (data) {
            var url = path + '/servlet/FileViewerServlet?fileName=' + data;
            window.open(url, 'mywin', 'left=200,top=200,width=1000,height=1000,toolbar=0,resizable=1');
        }
    });
}
//Agent Invoice Report
function viewFile(file, path) {
    var win = window.open(path + '/servlet/PdfServlet?fileName=' + file, '_new', 'width=1000,height=650,toolbar=no,directories=no,status=no,linemenubar=no,scrollbars=no,resizable=no,modal=yes');
    window.onblur = function () {
        win.focus();
    }
}
//Multiple DR Reports
function viewPreviewReport(unitSsId, fileNo, path) {
    if (document.getElementById('bkgTable') === null) {
        sampleAlert("No DRs are linked to this Unit");
    } else {
        showLoading();
        jQuery.ajaxx({//printReport method changed due to change in FileName
            data: {
                className: "com.gp.cong.lcl.dwr.LclPrintUtil",
                methodName: "printTotalBKGUnit",
                param1: unitSsId,
                param2: fileNo,
                param3: "Pre Advice/Arrival Notice/Status Update",
                request: "true"
            },
            success: function (data) {
                closePreloading();
                window.parent.showLightBox('Status Update', path + '/servlet/FileViewerServlet?fileName=' + data, 700, 1000);
            }
        });
    }
}
//Single DR Report
function viewDrReport(fileId, fileNo, path) {
    var reportPreviewLocation = $('#reportPreviewLocation').val();
    showLoading();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclPrintUtil",
            methodName: "printUnitImpBkg",
            param1: fileId,
            param2: fileNo,
            param3: "Pre Advice/Arrival Notice/Status Update",
            request: "true"
        },
        success: function (data) {
            closePreloading();
            window.parent.showLightBox('Status Update', path + '/servlet/FileViewerServlet?fileName=' + data, 700, 1000);
        }
    });
}

function lclImpCharge(path, unitssId, headerId, unitId, unitNo, podUnlocCode) {
    var href = path + "/lclImpUnitCostCharge.do?methodName=displayCharge&unitSsId=" + unitssId + "&headerId=" + headerId + "&unitId=" + unitId + "&unitNo=" + unitNo + "&podUnlocCode=" + podUnlocCode;
    $.colorbox({
        iframe: true,
        href: href,
        width: "80%",
        height: "90%",
        title: "Charges/Dist"
    });
}
function lclImpCost(path, unitssId, headerId, unitId, unitNo, closedTime, auditedTime) {
    var href = path + "/lclImpUnitCostCharge.do?methodName=displayCost&unitSsId=" + unitssId + "&headerId=" + headerId + "&closedTime=" + closedTime + "&auditedTime=" + auditedTime + "&unitId=" + unitId + "&unitNo=" + unitNo;
    $.colorbox({
        iframe: true,
        href: href,
        width: "70%",
        height: "100%",
        title: "Cost",
        onClosed: function () {
            showLoading();
            $("#methodName").val('editVoyage');
            $("#lclAddVoyageForm").submit();
        }
    });
}

function openLclVoyageArInvoice(path, unitSSId, unitNo, listFlag, voyageId) {
    var href = path + "/lclVoyageArInvoice.do?methodName=display&fileNumberId=" + unitSSId + "&fileNumber=" + unitNo;
    href = href + "&listFlag=" + listFlag + "&voyageId=" + voyageId + "&unitNo=" + unitNo + "&selectedMenu=Imports";
    $.colorbox({
        iframe: true,
        href: href,
        width: "75%",
        height: "95%",
        title: "AR Invoice",
        onClosed: function () {
            $("#methodName").val('editVoyage');
            $("#lclAddVoyageForm").submit();
        }
    });
}

function abortDispDrsPopup() {
    $.colorbox.close();
    parent.$.colorbox.close();
}

function abortCurrentPopup() {
    parent.$.colorbox.close();
}

function validateDistributeDr(path, methodName, lclSsAcId) {
    var chargesCode = $('#chargesCode').val();
    var orginalCostAmount = $('#orginalCostAmount').val();
    var weightMeasure = $("input:radio[name='weightMeasure']:checked").val();
    var terminalNo = parent.$("#terminalNo").val();
    if (jQuery(".chkDistribute:checked").length === 0) {
        $.prompt("Please select atleast one Charge to Distribute");
        $('#finalAmount').val('0.00');
        $("#tdTotal").text('0.00');
        return false;
    }
    if (chargesCode === null || chargesCode === "") {
        $.prompt('Code is required');
        $("#chargesCode").css("border-color", "red");
        return false;
    }
    var finalamount = $('#finalAmount').val();
    if (finalamount === null || finalamount === "" || finalamount === '0.00') {
        $.prompt('Final Amount is required and must be greater than 0.00');
        $('#finalAmount').css("border-color", "red");
        return false;
    }
    var unitSSId = $('#unitSsId').val();
    var headerId = $('#headerId').val();
    var unitId = $('#unitId').val();
    var invoiceNumber = $('#invoiceNumber').val();
    var unitNo = $('#unitNo').val();
    var percentage = $('#percentage').val();
    var unitStatus = $("#unitStatus").val();
    if (checkDistChargeAndCostMappingWithGL(chargesCode, terminalNo, unitStatus)) {
        var href = path + "/lclImpUnitCostCharge.do?methodName=" + methodName + "&unitSsId=" + unitSSId + "&finalAmount=" + finalamount + "&chargesCode=" + chargesCode;
        href = href + "&headerId=" + headerId + "&unitId=" + unitId + "&invoiceNumber=" + invoiceNumber + "&unitNo=" + unitNo + "&weightMeasure=" + weightMeasure + "&podUnlocCode=" + $("#podUnlocCode").val();
        href = href + "&percentage=" + percentage + "&lclSsAcId=" + lclSsAcId + "&orginalCostAmount=" + orginalCostAmount;
        $.colorbox({
            iframe: true,
            href: href,
            width: "100%",
            height: "95%",
            title: "Distribute DRs"
        });
    }
}

function distributeAllDrs(lclSsAcId) {
    var fileId = [];
    var chargesAmt = [];
    var minimumAmt = [];
    var nonzerofound = false, emptyfound = false;
    jQuery(".hiddenFileId").each(function () {
        fileId.push($(this).val());
    });
    jQuery(".chgAmt").each(function () {
        var amt = $(this).val();
        chargesAmt.push(amt);
        if (amt > 0.00) {
            nonzerofound = true;
        }
        if (amt == null || amt == "") {
            $(this).css("border-color", "red");
            emptyfound = true;
        }
    });
    if (emptyfound) {
        $.prompt('None of the fields must be empty.Atleast 0.00 must be filled');
        return false;
    }
    if (!nonzerofound) {
        $.prompt('Atleast one field must be greater than 0.00');
        return false;
    }
    var unitStatus = $("#unitStatus").val();
    if (unitStatus === 'M') {
        var unitSSId = $("#unitSsId").val();
        $.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "validateDrHasConsigneeOrNot",
                param1: unitSSId
            },
            async: false,
            success: function (data) {
                if (data != "" && data != "null" && data != null) {
                    sampleAlert("There is no consignee on DR# - " + data);
                    return false;
                } else {
                    saveDistribution(fileId, chargesAmt, lclSsAcId);
                }
            }
        });
    } else {
        saveDistribution(fileId, chargesAmt, lclSsAcId);
    }
}


function saveDistribution(fileId, chargesAmt, lclSsAcId) {
    showLoading();
    $("#fileId").val(fileId);
    $("#lclSsAcId").val(lclSsAcId);
    $("#chargesAmount").val(chargesAmt);
    $("#methodName").val('saveDistributedDr');
    $("#lclUnitCostChargeForm").submit();
}
function previewArInvoice(path, agentFlag) {
    var unitSSId = $("#fileNumberId").val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclPrintUtil",
            methodName: "lclArInvoiceReport",
            param1: "AgentInvoice",
            param2: "0",
            param3: unitSSId,
            param4: agentFlag,
            request: "true"
        },
        success: function (data) {
            viewFile(data, path);
        }
    });
}

function createAgentInvoice(path, unitNo, agentFlag) {
    var txt = 'Do you want to print a list of all DRs on this voyage, on the invoice?';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                createAgentArInvoice(path, unitNo, agentFlag, "Yes");
            } else {
                createAgentArInvoice(path, unitNo, agentFlag, "");
            }
        }
    });
}
function createAgentArInvoice(path, unitNo, agentFlag, drPrintFlag) {
    if ($("#totalRelsAmt").val() > 0) {
        var unitSSId = $("#fileNumberId").val();
        var voyageId = parent.$("#voyageId").val();
        var invoiceId = $("#arRedInvoiceId").val();
        var selectedMenu = parent.$("#selectedMenu").val();
        parent.$.colorbox.close();
        var url = path + "/lclVoyageArInvoice.do?methodName=saveAgentInvoice&fileNumberId=" + unitSSId + "&arRedInvoiceId=" + invoiceId + "&voyageId=" + voyageId;
        url += "&selectedMenu=" + selectedMenu + "&unitNo=" + unitNo + "&listFlag=false&newItemFlag=true" + "&agentFlag=" + agentFlag + "&printOnDrFlag=" + drPrintFlag;
        parent.window.location = url;
        $.prompt.close();
    }
    else {
        $.prompt("Atleast one agent charge must be Released To Invoice to generate invoice.");
    }
}
function unitsEdit(path, unitId, headerId, unitssId, unLocationCode, etaPod) {
    var originalClose = $.colorbox.close;
    $.colorbox.close = function () {
        var $frame = $(".cboxIframe").contents();
        if ($frame.find("#lclAddUnitsForm").serialize() != $frame.find("#loadForm").val()) {
            $frame.find("body").append(
                    $.prompt("Some fields have been modified. Are you sure you want to close?", {
                        buttons: {
                            Yes: 1,
                            No: 2
                        },
                        submit: function (v) {
                            if (v == 1) {
                                originalClose();
                            } else if (v == 2) {/*do nothing*/
                            }
                        }
                    }));
        } else {
            originalClose();
        }
    }
    var href = path + "/lclImportAddUnits.do?methodName=editUnits&unitId=" + unitId + "&headerId=" + headerId + "&unitssId=" + unitssId;
    href += "&polUnlocationCode=" + unLocationCode + "&etaPodDate=" + etaPod;
    $.colorbox({
        iframe: true,
        href: href,
        width: "100%",
        height: "100%",
        title: "Edit Unit",
        onClosed: function () {
            $("#methodName").val('editVoyage');
            $("#lclAddVoyageForm").submit();
        }
    });
}

function showAddItem() {
    $("#chargeCode").val("");
    $("#chargeId").val("");
    $("#addLineItem").show();
}
function hideAddItem() {
    $("#addLineItem").hide();
}

function submitFormForCharge(methodName) {
    var amount = $("#amount").val();
    var chargeId = $("#chargeId").val();
    if (chargeId == null || chargeId == "" || chargeId == "0")
    {
        sampleAlert('Code is required');
        $("#chargeCode").css("border-color", "red");
        return false;
    }
    if (amount == null || amount == "" || amount == "0.0") {
        sampleAlert("Amount is required");
        $("#amount").css("border-color", "red");
        return false;
    } else {
        $("#methodName").val(methodName);
        $("#lclVoyageArInvoiceForm").submit();
    }
}

function deleteInvoiceCharge(methodName, index) {
    $.prompt('Are you sure you want to delete?', {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                $("#methodName").val(methodName);
                $("#arRedInvoiceId").val(index);
                $.prompt.close();
                $("#lclVoyageArInvoiceForm").submit();
            }
            else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}

function openReversePostPopup(path) {
    var unitssId = $("#unitssId").val();
    var unitId = $("#unitId").val();
    $("#methodName").val("isCorrectionFound");
    $.post($("#lclAddUnitsForm").attr("action"), $("#lclAddUnitsForm").serialize(), function (data) {
        if (data !== "") {
            sampleAlert(data);
            return false;
        } else {
            var href = path + "/lclImportAddUnits.do?methodName=openAccountingReversePopup&unitssId=" + unitssId;
            href = href + "&unitId=" + unitId;
            $.colorbox({
                iframe: true,
                href: href,
                width: "60%",
                height: "60%",
                title: "Reverse Accounting"
            });
        }
    });

}
function unManifest(unitssId, unitId) {
    var txt = 'Do you want to reverse accounting?';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                showLoading();
                $("#methodName").val("validateChargesBillToParty");
                $("#unitssId").val(unitssId);
                $("#postFlag").val("N");
                $.post($("#lclAddVoyageForm").attr("action"), $("#lclAddVoyageForm").serialize(), function (data) {
                    if (data !== "") {
                        closePreloading();
                        sampleAlert(data);
                        return false;
                    }
                    else {
                        $("#methodName").val('unmanifest');
                        $("#unitId").val(unitId);
                        $("#lclAddVoyageForm").submit();
                        $.prompt.close();
                    }
                });
            }
            else if (v === 2) {
                $.prompt.close();
                return false;
            }
        }
    });
}

//Click Import New DR Button
function importBooking(path, headerId, unitSsId, unitId) {
    var limit = $("#limit").val();
    var searchOriginId = $('#searchOriginId').val();
    var searchFdId = $('#searchFdId').val();
    var searchTerminalNo = $('#searchTerminalNo').val();
    var searchLoginId = $('#searchLoginId').val();
    var searchMasterBl = $('#searchMasterBl').val();
    var searchUnitNo = $('#searchUnitNo').val();
    var searchAgentNo = $('#searchAgentNo').val();
    var searchVoyageNo = $('#searchVoyageNo').val();
    var searchDispoId = $('#searchDispoId').val();
    var searchOrigin = $('#searchOrigin').val();
    var searchFd = $('#searchFd').val();
    var searchTerminal = $('#searchTerminal').val();
    window.parent.showLoading();
    var href = path + "/lclBooking.do?methodName=importNewBooking&impSearchFlag=true&headerId=" + headerId
            + "&unitSsId=" + unitSsId + "&unitId=" + unitId
            + "&searchOriginId=" + searchOriginId + "&searchFdId=" + searchFdId + "&searchTerminalNo=" + searchTerminalNo
            + "&searchLoginId=" + searchLoginId + "&searchUnitNo=" + searchUnitNo + "&searchMasterBl=" + searchMasterBl
            + "&searchAgentNo=" + searchAgentNo + "&searchDispoId=" + searchDispoId + "&searchVoyageNo=" + searchVoyageNo + "&limit=" + limit
            + "&searchOrigin=" + searchOrigin + "&searchFd=" + searchFd + "&searchTerminal=" + searchTerminal;
    window.location.href = href;
}
//click Import Exisiting DR
function clickImportBooking(path, fileId, moduleName, fileNumber) {
    $("#methodName").val("checkLocking");
    var params = $("#lclAddVoyageForm").serialize();
    params += "&fileNo=" + fileNumber + "&loginUserId=" + $('#loginUserId').val();
    $.post($("#lclAddVoyageForm").attr("action"), params, function (data) {
        if (data === 'available') {
            if (moduleName === "Exports") {
                gotoExportDrFromImpVoyageScreen(path, fileId);
            } else {
                callBackImpDR(path, fileId, moduleName, fileNumber);
            }
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
                        if (moduleName === "Exports") {
                            gotoExportDrFromImpVoyageScreen(path, fileId);
                        } else {
                            callBackImpDR(path, fileId, moduleName, fileNumber);
                        }
                    } else if (v === 2) {
                        $.prompt.close();
                    }
                }
            });
        }
    });
}

function callBackImpDR(path, fileId, moduleName, fileNumber) {
    window.parent.showLoading();
    var headerId = $('#headerId').val();
    var unitId = $('#unitId').val();
    var unitSsId = $('#unitssId').val();
    var limit = $("#limit").val();
    var searchOriginId = $('#searchOriginId').val();
    var searchFdId = $('#searchFdId').val();
    var searchTerminalNo = $('#searchTerminalNo').val();
    var searchLoginId = $('#searchLoginId').val();
    var searchMasterBl = $('#searchMasterBl').val();
    var searchUnitNo = $('#searchUnitNo').val();
    var searchAgentNo = $('#searchAgentNo').val();
    var searchVoyageNo = $('#searchVoyageNo').val();
    var searchDispoId = $('#searchDispoId').val();
    var searchOrigin = $('#searchOrigin').val();
    var searchFd = $('#searchFd').val();
    var searchTerminal = $('#searchTerminal').val();
    var href = path + "/lclBooking.do?methodName=editBooking&impSearchFlag=true&fileNumberId=" + fileId
            + "&headerId=" + headerId + "&unitId=" + unitId + "&moduleName=" + moduleName + "&moduleId=" + fileNumber
            + "&unitSsId=" + unitSsId + "&screenName=LCL FILE&operationType=Scan or Attach"
            + "&searchOriginId=" + searchOriginId + "&searchFdId=" + searchFdId + "&searchTerminalNo=" + searchTerminalNo
            + "&searchLoginId=" + searchLoginId + "&searchUnitNo=" + searchUnitNo + "&searchMasterBl=" + searchMasterBl
            + "&searchAgentNo=" + searchAgentNo + "&searchDispoId=" + searchDispoId + "&searchVoyageNo=" + searchVoyageNo + "&limit=" + limit
            + "&searchOrigin=" + searchOrigin + "&searchFd=" + searchFd + "&searchTerminal=" + searchTerminal;
    //+"&voyagesOriginIdSearch=" + voyagesOriginIdSearch + "&voyagesDestinationIdSearch=" + voyagesDestinationIdSearch
    //+ "&voyagesVoyageNoSearch=" + voyagesVoyageNoSearch +  "&voyagesBillinTerminalNoSearch=" + voyagesBillinTerminalNoSearch
    //+ "&voyagesDispositionIdSearch=" + voyagesDispositionIdSearch + "&voyagesUnitNoSearch=" + voyagesUnitNoSearch + "&voyagesMasterBlSearch=" + voyagesMasterBlSearch + "&limit=" + limit
    //+ "&voyagesAgentNoSearch=" + voyagesAgentNoSearch
    window.location.href = href;
}

// Lcl Export Dr from Import voyage 
function  gotoExportDrFromImpVoyageScreen(path, fileId) {
    var headerId = $('#headerId').val();
    var unitSsId = $('#unitssId').val();
    var filter = "lclImports";
    var detailId = $("#detailId").val();
    var fromScreen = "ImportVoyageScreen";
    var toScreen = "EXPORT_TO_IMPORT";
    window.parent.navigateExportImportScreen(fileId, filter,
            headerId, detailId, unitSsId, fromScreen, toScreen, "");
}

//Link DR
function linkDR(path, unitId, unitssId, unitNo, scheduleNo, headerId) {
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkDispositionStatus",
            param1: unitId,
            request: true,
            dataType: "json"
        },
        success: function (data) {
            if (data) {
                var polId = document.getElementById("originId").value;
                var podId = document.getElementById("finalDestinationId").value;
                var href = path + "/lclImportAddVoyage.do?methodName=linkDRDisplay&unitId=" + unitId + "&unitssId=" + unitssId + "&unitNo=" + unitNo + "&scheduleNo=" + scheduleNo + "&polId=" + polId + "&podId=" + podId + "&headerId=" + headerId;
                $.colorbox({
                    iframe: true,
                    href: href,
                    width: "45%",
                    height: "60%",
                    title: "Link DR"
                });
            } else {
                sampleAlert("This option is only available for the Disposition in DATA or WATR or PORT");
            }
        }
    });
}
function osdReceived(path, unitssId, unitId, headerId, unitNo) {
    var href = path + "/lclImportAddVoyage.do?methodName=displayOSDDetails&unitId=" + unitId + "&unitssId=" + unitssId + "&unitNo=" + unitNo + "&headerId=" + headerId;
    $.colorbox({
        iframe: true,
        href: href,
        width: "40%",
        height: "40%",
        title: "OSD Screen"
    });
}
//Check IT Number Validation
function checkForNumberOnly(obj) {
    var inbondChecked = document.getElementById("allowfreetextIT").checked;
    if (!inbondChecked) {
        if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
            obj.value = "";
            sampleAlert("This field should be Numeric");
        }
    }
}
//set check box  for IT Number
function checkBoxMaxLengthForIt() {
    var inbondChecked = document.getElementById("allowfreetextIT").checked;
    if (inbondChecked) {
        $("#itNo").attr('maxlength', '25');
    } else {
        $("#itNo").attr('maxlength', '9');
    }
}
function setCheckBoxOnLoadForIT() {
    var inbondNo = $('#itNo').val();
    if ((inbondNo.length <= 9) && isNumber(inbondNo)) {
        $("#itNo").attr('maxlength', '9');
        document.getElementById("allowfreetextIT").checked = false;
    } else {
        $("#itNo").attr('maxlength', '25');
        $("#inbondType").removeClass("mandatory");
        document.getElementById("allowfreetextIT").checked = true;
    }
}
function dateCheck(obj) {
    var months = {
        Jan: 1,
        Feb: 2,
        Mar: 3,
        Apr: 4,
        May: 5,
        Jun: 6,
        Jul: 7,
        Aug: 8,
        Sep: 9,
        Oct: 10,
        Nov: 11,
        Dec: 12
    };
    var dateArray = obj.value.split("-");
    var enteredDate = new Date(eval("months." + dateArray[1]) + "-" + dateArray[0] + "-" + dateArray[2]);
    var pDate = new Date();
    pDate.setDate(new Date().getDate() - 60);
    pDate.setHours(0, 0, 0, 0);
    var fDate = new Date();
    fDate.setDate(new Date().getDate() + 60);
    fDate.setHours(0, 0, 0, 0);
    if (enteredDate < pDate) {
        obj.value = "";
        congAlert("Only past 60 days are allowed from today's date");
    } else if (enteredDate > fDate) {
        obj.value = "";
        congAlert("Greater than 60 days from today's date is not allowed");
    }
}
function congAlert(txt) {
    $.prompt(txt);
}
function etdValidate() {
    var day, month, year;
    var etdArray = $('#std').val().split("-");
    var etaArray = $('#etaPod').val().split("-");
    day = etdArray[0];
    month = getMonthNumber(etdArray[1]) - 1;
    year = etdArray[2];
    var SailingDate = new Date(year, month, day);
    day = etaArray[0];
    month = getMonthNumber(etaArray[1]) - 1;
    year = etaArray[2];
    var ArrivalDate = new Date(year, month, day);
    var pDate = new Date();
    pDate.setDate(new Date().getDate() - 180);
    pDate.setHours(0, 0, 0, 0);
    var fDate = new Date();
    fDate.setDate(new Date().getDate() + 180);
    fDate.setHours(0, 0, 0, 0);
    if (SailingDate >= ArrivalDate) {
        congAlert("ETD should be less than ETA");
        $('#std').val('');
    } else if (SailingDate < pDate) {
        congAlert("ETD should not be less than six month from today's date");
        $('#std').val('');
    } else if (SailingDate > fDate) {
        congAlert("ETD should not be greater than six month from today's date");
        $('#std').val('');
    }
}

function etaValidate() {
    var day, month, year;
    var etdArray = $('#std').val().split("-");
    var etaArray = $('#etaPod').val().split("-");
    day = etdArray[0];
    month = getMonthNumber(etdArray[1]) - 1;
    year = etdArray[2];
    var SailingDate = new Date(year, month, day);
    day = etaArray[0];
    month = getMonthNumber(etaArray[1]) - 1;
    year = etaArray[2];
    var ArrivalDate = new Date(year, month, day);
    var pDate = new Date();
    pDate.setDate(new Date().getDate() - 180);
    pDate.setHours(0, 0, 0, 0);
    var fDate = new Date();
    fDate.setDate(new Date().getDate() + 180);
    fDate.setHours(0, 0, 0, 0);
    if (SailingDate >= ArrivalDate) {
        congAlert("ETA should be greater than ETD");
        $('#etaPod').val('');
    } else if (ArrivalDate < pDate) {
        congAlert("ETA should not be less than six month from today's date");
        $('#etaPod').val('');
    } else if (ArrivalDate > fDate) {
        congAlert("ETA should not be greater than six month from today's date");
        $('#etaPod').val('');
    }
}

function strippedValidate() {
    var months = {
        Jan: 1,
        Feb: 2,
        Mar: 3,
        Apr: 4,
        May: 5,
        Jun: 6,
        Jul: 7,
        Aug: 8,
        Sep: 9,
        Oct: 10,
        Nov: 11,
        Dec: 12
    };
    var stripped = $('#strippedDate').val().split("-");
    var etaPod = $('#etaPodDate').val().split("-");
    var strippedDate = new Date(eval("months." + stripped[1]) + "-" + stripped[0] + "-" + stripped[2]);
    var etaPodDate = new Date(eval("months." + etaPod[1]) + "-" + etaPod[0] + "-" + etaPod[2]);
    if (strippedDate < etaPodDate) {
        congAlert("Stripped date should not be less than ETA");
        $('#strippedDate').val('');
    }
}

function calculateFinalAmount() {
    var total = 0.00;
    var percentage = $('#percentage').val();
    if (jQuery(".chkDistribute:checked").length === 0) {
        sampleAlert("Please select atleast one Charge to Distribute");
        $('#finalAmount').val('0.00');
        $("#tdTotal").text('0.00');
        $("#orginalCostAmount").val('0.00');
        $('#chargesCode').val('');
        $('#chargesCodeId').val('');
        return false;
    }
    else {
        var ssId = [];
        jQuery(".chkDistribute:checked").each(function () {
            ssId.push("'" + jQuery(this).val() + "'");
            $('#hiddenChargeId').val(ssId);
            total += +($(this).closest(".distribute").next().next().next().next().next().html().replace(/^\s\s*/, '').replace(/\s\s*$/, ''));
            /*  var distributetable = document.getElementById("lclUnitSSAc");
             var rowslen = distributetable.rows.length - 1;
             var row = distributetable.rows.item(0);
             for (var i = 1; i <= rowslen; i++) {
             if (jQuery(this).val() == i)
             {
             for (var j = 0; j < row.cells.length; j++) {
             var col = row.cells.item(j);
             if (col.innerHTML.toString().trim().toUpperCase() == 'COST AMOUNT') {
             total += Number(distributetable.rows[i].cells[j].innerHTML);
             }
             }
             }
             }*/
        });
        $("#tdTotal").text(total.toFixed(2));
        $("#orginalCostAmount").val(total.toFixed(2));
    }
    if (percentage != "" && percentage > 0.00) {
        var finalAmount = (percentage / 100) * total.toFixed(2);
        finalAmount = Number(finalAmount) + Number(total.toFixed(2));
        $('#finalAmount').val(finalAmount.toFixed(2));
    }
}
function setChargeCode() {
    var i = 0;
    jQuery(".chkDistribute:checked").each(function () {
        i++;
        if (i === 1) {
            $('#chargesCode').val($(this).closest(".distribute").next().next().html().trim());
        } else {
            $('#chargesCode').val('');
            $('#chargesCodeId').val('');
        }
    });
}

function openOutsourceEmail(path, unit_id, header_id, userId) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "outsourceUserEmailaddress",
            param1: userId
        },
        success: function (data) {
            if (data === null || data === "") {
                $.prompt("Outsource Email Id is Required");
            } else {
                var href = path + "/lclOutsource.do?methodName=openOutsourceEmail&unitId=" + unit_id + "&ssHeaderId=" + header_id + "&emailId=" + data;
                $.colorbox({
                    iframe: true,
                    href: href,
                    width: "60%",
                    height: "70%",
                    title: "Outsource"
                });
            }
        }
    });
}
function deleteUnits(unitId) {//Delete Units
    var txt = 'Are you sure You want to delete?';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                $('#unitId').val(unitId);
                $("#methodName").val('deleteUnits');
                $("#lclAddVoyageForm").submit();
                hideProgressBar();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function addUnits(path, etaPod) {//ADD Units
    var polName = $("#pooOrigin").val();
    var polCode = polName.substring(polName.indexOf("(") + 1, polName.indexOf(")"));
    var headerId = $('#headerId').val();
    var href = path + "/lclImportAddUnits.do?methodName=addUnits&headerId=" + headerId + "&polUnlocationCode=" + polCode + "&etaPodDate=" + etaPod;
    $("#addunits").attr("href", href);
    $("#addunits").colorbox({
        iframe: true,
        width: "100%",
        height: "90%",
        title: "Add Unit"
    });
}
function editVoyage(path, headerId) {
    var href = path + "/lclImportAddVoyage.do?methodName=editVoyageHeader&headerId=" + headerId;
    $("#imgeditheader").attr("href", href);
    $("#imgeditheader").colorbox({
        iframe: true,
        width: "65%",
        height: "40%",
        title: "Voyage Information"
    });
}
function editDetails(path, detailId) {
    var href = path + "/lclImportAddVoyage.do?methodName=editVoyageDetail&detailId=" + detailId;
    $.colorbox({
        iframe: true,
        href: href,
        width: "80%",
        height: "50%",
        title: "Voyage Details"
    });
}

function goBackEculine(path) {
    var unitId = $("#unitId").val();
    var url = path + "/lclEculineEdi.do?methodName=display&unitId=" + unitId;
    document.location.href = url;
}

function goBackSearch(path) {
    //    var polId = $("#originId").val();
    //    //var searchPolName = $("#searchPolName").val();
    //    var podId = $("#finalDestinationId").val();
    //    //var searchPodName = $("#searchPodName").val();
    //    var originId = $("#searchOriginId").val();
    //    var destinationId = $("#searchDestinationId").val();
    //    var polName = $("#pooOrigin").val();
    //    var podName = $("#podDestination").val();
    //    var originName = $("#searchOriginName").val();
    //    var destinationName = $("#searchDestinationName").val();
    //    var billsTerminal = $("#billsTerminal").val();
    //    var billsTerminalNo = $("#billsTerminalNo").val();
    //    var polCode = $("#polUnlocationCode").val();
    //    var voyageNo = $("#voyageNo").val();
    //    var unitNo = $("#unitNo1").val();
    //    var masterBl = $("#masterBL1").val();
    //    var loginName = $("#loginName").val();
    //    var loginId = $("#loginId").val();
    //    var dispositionCode = $("#dispositionCode").val();
    //    var dispositionId = $("#dispositionId").val();
    //    var agentNo = $("#agentNo").val();
    //    var pooId=$('#searchOriginId').val();
    //    var fdId=$('#searchFdId').val();

    var limit = $("#limit").val() != '' ? $("#limit").val() : '50';
    var goBackVoyageNo = $("#goBackVoyageNo").val();
    var searchOriginId = $('#searchOriginId').val();
    var searchFdId = $('#searchFdId').val();
    var searchTerminalNo = $('#searchTerminalNo').val();
    var searchLoginId = $('#searchLoginId').val();
    var searchMasterBl = $('#searchMasterBl').val();
    var searchUnitNo = $('#searchUnitNo').val();
    var searchAgentNo = $('#searchAgentNo').val();
    var searchVoyageNo = $('#searchVoyageNo').val();
    var searchDispoId = $('#searchDispoId').val();
    var searchOrigin = $('#searchOrigin').val();
    var searchFd = $('#searchFd').val();
    var searchTerminal = $('#searchTerminal').val();
    var searchTerminalLoc = $("#billsTerminal").val();
    if (searchDispoId == '' && searchVoyageNo == '' && searchUnitNo == '' && searchAgentNo == '' && searchTerminal == ''
            && searchFd == '' && searchOrigin == '' && searchMasterBl == '' && searchLoginId == ''
            && searchTerminalNo == '' && searchFdId == '' && searchOriginId == '') {
        searchLoginId = $('#loginUserId').val();
    }
    var finalURL = path + "/lclImportVoyage.do?methodName=goBack&portOfOriginId=" + searchOriginId + "&finalDestinationId=" + searchFdId
            + "&voyageNumber=" + goBackVoyageNo + "&billsTerminal=" + searchTerminalLoc + "&billsTerminalNo=" + searchTerminalNo
            + "&voyageNo=" + searchVoyageNo + "&loginId=" + searchLoginId
            + "&dispositionId=" + searchDispoId + "&unitNo=" + searchUnitNo + "&masterBl=" + searchMasterBl
            + "&limit=" + limit + "&agentNo=" + searchAgentNo
            + "&origin=" + searchOrigin + "&destination=" + searchFd + "&billsTerminal=" + searchTerminal;
    //+ "&dispositionCode=" + dispositionCode ++ "&polId=" + polId + "&podId=" + podId+"&polName=" + polName + "&podName=" + podName+"&origin=" + originName + "&destination=" + destinationName + "&polCode=" + polCode

    document.location.href = finalURL;
}
function backtoSearchScreen(path) {
    var homeScreenVoyageFileFlag = $("#homeScreenVoyageFileFlag").val();
    var fileNumber = $("#goBackVoyageNo").val();
    window.parent.showLoading();
    if (parent.$("#container .selected").text().indexOf("Eculine") != -1) { //goback Eculine
        goBackEculine(path);
    } else {
        if (homeScreenVoyageFileFlag === "true") {
            window.parent.showHome(homeScreenVoyageFileFlag, fileNumber);
        } else {
            goBackSearch(path);
        }
    }
}
function changeVoyageOwner() {//change voyage owner
    showLoading();
    $('#voyageOwnerFlag').val("true");
    $("#methodName").val('editVoyage');
    $("#lclAddVoyageForm").submit();
}

function onStatusClose(path, headerId, unitId) {
    var status = $("#closevoyagebutton").val();
    var href = path + "/lclImportAddVoyage.do?methodName=displayCloseRemarks&status=" + status + "&headerId=" + headerId + "&unitId=" + unitId;
    $.colorbox({
        href: href,
        iframe: true,
        width: "30%",
        height: "45%",
        title: "Close Remarks",
        onClosed: function () {
            var remarks = document.getElementById("remarks").value;
            document.getElementById("headerId").value = headerId;
            document.getElementById("unitId").value = unitId;
            document.getElementById("closedRemarks").value = remarks;
            document.getElementById('reopenedRemarks').value = remarks;
            document.getElementById("methodName").value = "updateClosedAuditedRemarks";
            $("#lclAddVoyageForm").submit();
        }
    });
}

//function onStatusNotClose() {
//    var status = $("#closevoyagebutton").val();
//    var headerId = $("#headerId").val();
//    var unitId = $("#unitId").val();
//    var txt = "Are you sure Y/N?not"
//    $.prompt(txt, {
//        buttons: {
//            Yes: 1,
//            No: 2
//        },
//        submit: function(v) {
//            if (v == 1) {
//                $.prompt.close();
//                var remarks = document.getElementById("remarks").value;
//                document.getElementById("headerId").value = headerId;
//                document.getElementById("unitId").value = unitId;
//                document.getElementById("closedRemarks").value = remarks;
//                document.getElementById("buttonValue").value = status;
//                document.getElementById("methodName").value = "updateClosedAuditedRemarks";
//                $("#lclAddVoyageForm").submit();
//            }
//            else if (v == 2) {
//                $.prompt.close();
//            }
//        }
//    });
//}

function closeVoyage(path) {
    var status = $("#closevoyagebutton").val();
    var headerId = $("#headerId").val();
    var unitId = $("#unitId").val();
    var currentDispo = $("#currentDispo").val();
    var strippedDate = $("#strippedDate").val();
    if (status === 'Close') {
        if (currentDispo == 'AVAL' && (strippedDate == "" || strippedDate == null)) {
            $.prompt("Strip Date Missing");
            return false;
        }
        $("#methodName").val("getNonInvoicedCharges");
        var $addVoyageForm = $("#lclAddVoyageForm");
        var url = $addVoyageForm.attr("action");
        var params = $addVoyageForm.serialize();
        $.post(url, params, function (data) {
            if (data !== '') {
                $.prompt("Warning - DR# <span style=color:red>" + data + "</span> has agent charges which are yet to be invoiced.");
            } else {
                onStatusClose(path, headerId, unitId);
            }
        });
    } else {
        onStatusClose(path, headerId, unitId);
    }
}

function auditVoyage(path) {
    var status = $("#auditvoyagebutton").val();
    var headerId = $("#headerId").val();
    var unitId = $("#unitId").val();
    if (status === 'Audit') {
        var href = path + "/lclImportAddVoyage.do?methodName=displayAuditedRemarks&status=" + status + "&headerId=" + headerId + "&unitId=" + unitId;
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

function copyToClipboard(str) {
    window.clipboardData.clearData("Text");
    window.clipboardData.setData('Text', str);
}

function fillLastFreeDate() {
    var strippedDate = $('#strippedDate').val();
    var dateArray = strippedDate.split("-");
    var months = {
        Jan: 1,
        Feb: 2,
        Mar: 3,
        Apr: 4,
        May: 5,
        Jun: 6,
        Jul: 7,
        Aug: 8,
        Sep: 9,
        Oct: 10,
        Nov: 11,
        Dec: 12
    };
    var enteredDate = new Date(eval("months." + dateArray[1]) + "-" + dateArray[0] + "-" + dateArray[2]);
    var pDate = new Date();
    pDate.setDate(enteredDate.getDate() + 6);
    var m_names = new Array("Jan", "Feb", "Mar",
            "Apr", "May", "Jun", "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec");
    var curr_date = pDate.getDate();
    var curr_month = pDate.getMonth();
    var curr_year = pDate.getFullYear();
    if (strippedDate != "") {
        if (curr_date.toString().length == '1') {
            $("#lastFreeDate").val("0" + curr_date + "-" + m_names[curr_month] + "-" + curr_year);
        } else {
            $("#lastFreeDate").val(curr_date + "-" + m_names[curr_month] + "-" + curr_year);
        }
    }
}

function toggle(selector) {
    $(selector).fadeToggle('slow', 'swing');
}

function addNewDisposition(path) {
    var thisDisposition = $("#disposition").val();
    var existingDisposition = $("#lclDisposition").find('tr:nth-child(1) td:nth-child(1)').text();
    if (existingDisposition === thisDisposition) {
        $.prompt("Disposition already exists ,please select another disposition");
        return;
    } else if (thisDisposition === "") {
        $.prompt("Disposition is required");
        $("#disposition").css("border-color", "red");
    } else {
        showLoading();
        if (thisDisposition === "PORT") {
            $("#methodName").val("checkChargeAndCostMappingWithGL");
            $.post($("#lclAddUnitsForm").attr("action"), $("#lclAddUnitsForm").serialize(),
                    function (data) {
                        if (data !== "null" && data !== "") {
                            $.prompt("No gl account is mapped with these charge code.Please contact accounting - <span style=color:red>" + data + "</span>.");
                            closePreloading();
                        } else {
                            addNewDispositionSubMethod(path);
                        }
                    });
        } else {
            addNewDispositionSubMethod(path);
        }
    }
}
function addNewDispositionSubMethod(path) {
    var thisDisposition = $("#disposition").val();
    var existingDisposition = $("#lclDisposition").find('tr:nth-child(1) td:nth-child(1)').text();
    $("#methodName").val("checkTerminalForPodFd");
    $.post($("#lclAddUnitsForm").attr("action"), $("#lclAddUnitsForm").serialize(),
            function (data) {
                if (data !== "null" && data !== "") {
                    $.prompt("Warning - FD and POD missing Import Contact Email for the DR# - <span style=color:red>" + data + "</span>.Please configure it for FD or POD to send the Dispostion Notification.");
                    closePreloading();
                } else {
                    $("#methodName").val("getNoOFContactFromDrs");
                    $.post($("#lclAddUnitsForm").attr("action"), $("#lclAddUnitsForm").serialize(),
                            function (data) {
                                closePreloading();
                                if (data !== "null" && data !== "") {
                                    var txt = "Warning - Accounts of <span style=color:red>" + data + " </span>are not set up to receive status updates ,Are you sure you want to continue  Y/N?";
                                    $.prompt(txt, {
                                        buttons: {
                                            Yes: 1,
                                            No: 2
                                        },
                                        submit: function (v) {
                                            if (v === 1) {
                                                checkDrCount(path, thisDisposition);
                                                $.prompt.close();
                                            }
                                            else if (v === 2) {
                                                var unitNo = $("#unitNo").val();
                                                var scheduleNo = parent.$("#schedule").val();
                                                var unitssId = $("#unitssId").val();
                                                var href = path + "/lclImportAddVoyage.do?methodName=viewDispDRSEditContactPopup&unitssId=" + unitssId;
                                                href = href + "&unitNo=" + unitNo + "&scheduleNo=" + scheduleNo + "&noalertFlag=true";
                                                $.colorbox({
                                                    iframe: true,
                                                    href: href,
                                                    width: "80%",
                                                    height: "75%",
                                                    title: "Edit Contacts"
                                                });
                                            }
                                        }
                                    });
                                } else {
                                    checkDrCount(path, thisDisposition);
                                }
                            });
                }
            });
}
function checkDrCount(path, thisDisposition) {
    if (thisDisposition === "PORT") {
        $("#methodName").val("getDrCount");
        $.post($("#lclAddUnitsForm").attr("action"), $("#lclAddUnitsForm").serialize(),
                function (drCount) {
                    if (drCount > 0) {
                        var unitNo = $("#unitNo").val();
                        var scheduleNo = parent.$("#schedule").val();
                        var unitssId = $("#unitssId").val();
                        listofPostedDRDetails(path, unitNo, scheduleNo, unitssId, "");
                    } else {
                        showLoading();
                        $("#methodName").val('addDisposition');
                        $("#lclAddUnitsForm").submit();
                    }
                });
    } else {
        showLoading();
        $("#methodName").val('addDisposition');
        $("#lclAddUnitsForm").submit();
    }
}
function addNewDispositionFromEditContactScreen(path, noAlertFlag) {
    var thisDisposition = parent.$("#disposition").val();
    if (thisDisposition === 'PORT') {
        parent.$("#methodName").val("getDrCount");
        $.post(parent.$("#lclAddUnitsForm").attr("action"), parent.$("#lclAddUnitsForm").serialize(),
                function (drCount) {
                    if (drCount > 0) {
                        var unitNo = parent.$("#unitNo").val();
                        var scheduleNo = parent.parent.$("#schedule").val();
                        var unitssId = parent.$("#unitssId").val();
                        listofPostedDRDetails(path, unitNo, scheduleNo, unitssId, noAlertFlag);
                    } else {
                        $("#methodName").val('addDisposition');
                        $("#lclAddUnitsForm").submit();
                    }
                });
    } else {
        window.parent.showLoading();
        parent.$("#methodName").val('addDisposition');
        parent.$("#lclAddUnitsForm").submit();
    }
}
function listofPostedDRDetails(path, unitNo, scheduleNo, unitssId, noAlertFlag) {
    var href = path + "/lclImportAddVoyage.do?methodName=viewDispDRSPopup&unitssId=" + unitssId;
    href = href + "&unitNo=" + unitNo + "&scheduleNo=" + scheduleNo + "&noAlertFlag=" + noAlertFlag;
    $.colorbox({
        iframe: true,
        href: href,
        width: "90%",
        height: "80%",
        title: "View DRs"
    });
}
function validateDispo() {
    var thisDisposition = $("#disposition").val();
    var strippedDate = $("#strippedDate").val();
    var existingDisposition = $("#lclDisposition").find('tr:nth-child(1) td:nth-child(1)').html();
    if (thisDisposition === existingDisposition) {
        $('#disposition').val('');
        $('#dispositionId').val('');
        $.prompt("Disposition already exists! Please select another disposition");
        return;
    } else if (existingDisposition === "DATA" && thisDisposition !== "WATR") {
        $('#disposition').val('');
        $('#dispositionId').val('');
        $.prompt("Only disposition WATR is allowed");
        return;
    } else if (existingDisposition === "WATR" && thisDisposition !== "PORT" && thisDisposition !== "DATA") {
        $('#disposition').val('');
        $('#dispositionId').val('');
        $.prompt("Only disposition PORT and DATA are allowed");
        return;
    }else if(thisDisposition == 'AVAL' && (strippedDate == "" || strippedDate == null)){
        $('#disposition').val('');
        $('#dispositionId').val('');
        $.prompt("Strip Date Missing");
        return;  
    }
}

function manifest(buttonValue, noAlertFlag) {
    $("#methodName").val("validateChargesBillToParty");
    $("#postFlag").val("Y");
    $.post($("#lclAddVoyageForm").attr("action"), $("#lclAddVoyageForm").serialize(), function (data) {
        if (data !== "") {
            sampleAlert(data);
            return false;
        }
        else {
            showProgressBar();
            var txt = "Are you sure Y/N?";
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        $.prompt.close();
                        if (noAlertFlag === 'true') {
                            parent.parent.$("#methodName").val('manifest');
                            parent.parent.$("#buttonValue").val(buttonValue);
                            parent.parent.parent.$("#buttonValue").val(buttonValue);
                            parent.parent.$("#lclAddUnitsForm").submit();
                        } else {
                            parent.$("#methodName").val('manifest');
                            parent.$("#buttonValue").val(buttonValue);
                            parent.parent.$("#buttonValue").val(buttonValue);
                            parent.$("#lclAddUnitsForm").submit();
                        }
                    }
                    else if (v === 2) {
                        hideProgressBar();
                        $.prompt.close();
                        return false;
                    }
                }
            });
        }
    });
}
function calculateDistributeChargeamt() {
    var total = 0.00;
    $(".chgAmt").each(function () {
        total += +($(this).val());
    });
    $("#tdChargeTotal").text(total.toFixed(2));
}
function vendorTypeCheck() {
    if ($('#thirdpartyDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#thirdparyDisableAcct').val() + "</span>");
        $('#thirdpartyaccountNo').val('');
        $('#thirdPartyname').val('');
    }
}
function showClientSearchOption(path, searchByValue) {
    var href = path + "/lclBooking.do?methodName=clientSearch&searchByValue=" + searchByValue;
    $(".clientSearchEdit").attr("href", href);
    $(".clientSearchEdit").colorbox({
        iframe: true,
        width: "45%",
        height: "48%",
        title: searchByValue + " Search"
    });
}

function resendEmails(unitSsId) {
    var txt = "Are you sure you want to resend all SU/AN?";
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO",
                        methodName: "insertVoyageNotification",
                        param1: unitSsId
                    },
                    async: false,
                    success: function (data) {
                        $.prompt("Emails sent successfully");
                        return false;
                    }
                });
            } else {
                $.prompt.close();
            }
        }
    });

}
function checkDistChargeAndCostMappingWithGL(chargesCode, terminalNo, unitStatus) {
    var flag = true;
    // if (unitStatus === "M") {
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkChargeAndCostMappingWithGLForDistribute",
            param1: chargesCode,
            param2: terminalNo,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data === "") {
                $.prompt("No gl account is mapped with these charge code." +
                        "Please contact accounting -> <span style=color:red>"
                        + chargesCode + "</span>.");
                flag = false;
            } else {
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
                            $.prompt("No gl account is mapped with these charge code." +
                                    "Please contact accounting -> <span style=color:red>"
                                    + data + "</span>.");
                            flag = false;
                        }

                    }
                });
            }
        }
    });
    // }
    return flag;
}
                 
                
                
