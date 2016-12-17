var path = "/" + window.location.pathname.split('/')[1];

function emailAction() {
    if (!checkDrAvaiableInVoyage()) {
        $.prompt("No Bills of Lading in this Unit");
        return false;
    } else if (!$('#frieghtEmail').is(':checked') && !$('#negotiableEmail').is(':checked')
            && ($('#frieghtEmailCollect').is(':checked') && !validateCollectCharges())) {
        $.prompt("No Collect Bills of Lading in this Unit");
        return false;
    } else if ($('#frieghtEmail').is(':checked') || $('#negotiableEmail').is(':checked')
            || $('#frieghtEmailCollect').is(':checked')) {
        if ($('input:radio[name=billOfLading]:checked').val() === undefined) {
            $.prompt("Choose option for bill of lading");
            return false;
        }
        $("#email-container").center().show(600, function () {
            showAlternateMask();
        });
        return false;
    } else if($('#unitManifestEmail').is(':checked') || $('#unitLargePrintManifestEmail').is(':checked')
            || $('#unitMiniConsolidationManifestEmail').is(':checked')) {
        $("#email-container").center().show(600, function () {
            showAlternateMask();
        });
        return false; 
        
    } else if ($('#unitUnratedDockReceiptEmail').is(':checked'))
    {
        $('#unitManifestEmail').attr('checked',false);
        $('#frieghtEmail').attr('checked',false);
        $('#negotiableEmail').attr('checked',false);
        $('#frieghtEmailCollect').attr('checked',false);
        $('#unitLargePrintManifestEmail').attr('checked',false);
         $('#unitMiniConsolidationManifestEmail').attr('checked',false);
        $("#email-container").center().show(600, function () {
            showAlternateMask();
        });
        return false;
    }
    else {
        $.prompt("Choose either non-negotiable or freight invoice or freight collect invoice.");
        return false;
    }
}

function cancelEmail() {
    $("#email-container").center().hide(600, function () {
        $("#toEmailAddress").val("");
        $("#ccEmailAddress").val("");
        $("#bccEmailAddress").val("");
        $("#emailSubject").val("");
        $("#emailMessage").val("");
        $("#agentContact").attr("checked", false);
        hideAlternateMask();
    });
}

function submitEmailMe(ele) {
    if (!checkDrAvaiableInVoyage()) {
        $.prompt("No Bills of Lading in this Unit");
        return false;
    } else if (!$('#frieghtEmail').is(':checked') && !$('#negotiableEmail').is(':checked')
            && ($('#frieghtEmailCollect').is(':checked') && !validateCollectCharges())) {
        $.prompt("No Collect Bills of Lading in this Unit");
        return false;
    } else if ($('#frieghtEmail').is(':checked') || $('#negotiableEmail').is(':checked')
            || $('#frieghtEmailCollect').is(':checked')) {
        if ($('input:radio[name=billOfLading]:checked').val() === undefined) {
            $.prompt("Choose option for bill of lading");
            $("#" + ele.id).attr("checked", false);
            return false;
        } else {
            var checkedValue = $('input:radio[name=billOfLading]:checked').val();
            showProgressBar();
            var subject = $("#negotiableEmail").is(':checked') ? ", Non-Negotiable" : "";
            subject += $('#frieghtEmail').is(':checked') ? ", Freight Invoice" : "";
            subject += $('#frieghtEmailCollect').is(':checked') ? ",Collect,Freight Invoice" : "";
            $("#methodName").val("setEmail");
            var params = $("#exportVoyageHblBatchForm").serialize();
            params += "&emailMe=true&emailSubject=House BLs Voy# "
                    + $("#voyageNumber").val() + ", " + checkedValue + "" + subject;
            $.post($("#exportVoyageHblBatchForm").attr("action"), params, function (data) {
                $("#emailMe").attr("checked", false);
                $("#status").addClass("green-background");
                if ($("#fileNumber").val() !== "") {
                    $("#fileNumber").val('');
                    $("#fileNumberId").val('');
                } else {
                    $("#exportVoyageHblBatchForm").closest('form').find("input[type=text]").val("");
                    $("#exportVoyageHblBatchForm").closest('form').find("input[type=radio]").attr("checked", false);
                    $("#exportVoyageHblBatchForm").closest('form').find("input[type=checkbox]").attr("checked", false);
                }
                hideProgressBar();
            });
        }
    }else if ($('#unitManifestEmail').is(':checked') || $('#unitLargePrintManifestEmail').is(':checked')
            || $('#unitMiniConsolidationManifestEmail').is(':checked') || $('#unitUnratedDockReceiptEmail').is(':checked')) {
        var checkedValue = $('input:radio[name=billOfLading]:checked').val();
        showProgressBar();
        var subject = $("#unitManifestEmail").is(':checked') ? ", Manifetst" : "";
        subject += $('#unitLargePrintManifestEmail').is(':checked') ? ", LargePrintManifest" : "";
        subject += $('#unitMiniConsolidationManifestEmail').is(':checked') ? ",MiniConsolidationManifest" : "";
        subject += $('#unitUnratedDockReceiptEmail').is(':checked') ? ",UnratedDockReceipt" : "";
        $("#methodName").val("setEmail");
        var params = $("#exportVoyageHblBatchForm").serialize();
        params += "&emailMe=true&emailSubject=House BLs Voy# "
                + $("#voyageNumber").val() + ", " + checkedValue + "" + subject;
        $.post($("#exportVoyageHblBatchForm").attr("action"), params, function (data) {
            $("#emailMe").attr("checked", false);
            $("#status").addClass("green-background");
            $("#exportVoyageHblBatchForm").closest('form').find("input[type=text]").val("");
            $("#exportVoyageHblBatchForm").closest('form').find("input[type=radio]").attr("checked", false);
            $("#exportVoyageHblBatchForm").closest('form').find("input[type=checkbox]").attr("checked", false);
            hideProgressBar();
        });
    }
    else {
        $.prompt("Choose either non-negotiable or freight invoice or freight collect invoice.");
        $("#" + ele.id).attr("checked", false);
        return false;
    }
}

function submitEmail() {
    if ($("#toEmailAddress").val() === "") {
        $.prompt("Please Enter To Address");
        return false;
    } else {
        showProgressBar();
        cancelEmail();
        $("#methodName").val("setEmail");
        var params = $("#exportVoyageHblBatchForm").serialize();
        params += "&toEmailAddress=" + $("#toEmailAddress").val() + "&ccEmailAddress="
                + $("#ccEmailAddress").val() + "&bccEmailAddress=" + $("#bccEmailAddress").val()
                + "&emailSubject=" + $("#emailSubject").val() + "&emailMessage=" + $("#emailMessage").val();
        $.post($("#exportVoyageHblBatchForm").attr("action"), params, function (data) {
            $("#status").addClass("green-background");
            if ($("#fileNumber").val() !== "") {
                $("#fileNumber").val('');
                $("#fileNumberId").val('');
            } else {
                $("#exportVoyageHblBatchForm").closest('form').find("input[type=text]").val("");
                $("#exportVoyageHblBatchForm").closest('form').find("input[type=radio]").attr("checked", false);
                $("#exportVoyageHblBatchForm").closest('form').find("input[type=checkbox]").attr("checked", false);
            }
            hideProgressBar();
        });
    }
}

function checkNumberAndDecimal(obj) {
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This field should be numeric");
    }
}

function printEmailVoyage(path, headerId, voyageNo) {
    var href = path + "/exportVoyageHblBatch.do?methodName=display&headerId="
            + headerId + "&voyageNumber=" + voyageNo + "&destinationId=" + $("#finalDestinationId").val();
    $.colorbox({
        iframe: true,
        href: href,
        width: "50%",
        height: "90%",
        title: "House BL Batch"
    });
}
function appendTextAlert(class1, class2) {
    var txt = "";
    if ($("." + class2).val() !== "") {
        txt += "<tr><td>" + $("." + class1).text() + "</td><td>-</td><td> <font color='red'>"
                + $("." + class2).val() + "</font></td></tr>";
    }
    return txt;
}

function validateDocumentName(documentName) {
    var flag = true;
    $(".documentName").each(function () {
        if ($(this).text().trim().trim() === documentName) {
            flag = false;
        }
    });
    return flag;
}

function printAction() {
    var maxRadio = $('input:radio[name=billOfLading]:checked').val();
    if (!checkDrAvaiableInVoyage()) {
        $.prompt("No Bills of Lading in this Unit");
        return false;
    }
    if ($("#printer").val() === "false") {
        $.prompt("Please choose printer");
        return false;
    }
    
    if (($('#unitManifest').val() === "" && $("#unitLargePrintManifest").val() === "" && $("#unitMiniConsolidationManifest").val() === ""
            && $("#unitUnratedDockReceipt").val() === "") && maxRadio === undefined) {
        $.prompt("Choose option for bill of lading");
        return false;
    } else if (maxRadio === undefined && ($('#unitManifest').val !== "" ||
            $("#unitMiniConsolidationManifest").val() !== "" || $("#unitLargePrintManifest").val() !== "" ||
            $("#unitUnratedDockReceipt").val() !== "")) {
    } else if (maxRadio !== undefined && ($('#unitManifest').val !== "" ||
            $("#unitMiniConsolidationManifest").val() !== "" || $("#unitLargePrintManifest").val() !== "" ||
            $("#unitUnratedDockReceipt").val() !== "")) {

    } 
    if ($('#frieghtInvoice').val() === "" && $('#nonNegotiable').val() === ""
            && ($('#frieghtInvoiceCollect').val() !== "" && !validateCollectCharges())) {
        $.prompt("No Collect Bills of Lading in this Unit");
        return false;
    }
    if ($("#unsignedOriginal").val() !== "" && validateDocumentName("Bill of Lading (Original UNSIGNED)")) {
        $.prompt("Please set up printer for <span class='red'>Original (Unsigned)</span> document.");
        return false;
    }
    if ($("#original").val() !== "" && validateDocumentName("Bill of Lading (Original)")) {
        $.prompt("Please set up printer for <span class='red'>Original (Signed)</span> document.");
        return false;
    }
    if (($("#nonNegotiable").val() !== "" || $("#signedNonNegotiable").val() !== "") && validateDocumentName("Quick Print")) {
        $.prompt("Please set up printer for <span class='red'>Quick Print</span> document.");
        return false;
    }
    if ($('#frieghtInvoice').val() !== "" || $('#frieghtInvoiceCollect').val() !== "") {
        maxRadio = "RATED";
        $("#rated").attr('checked', true);
    }
    if (validateDocumentName("Quick Print") && ($('#unitManifest').val !== "" || $("#unitMiniConsolidationManifest").val() !== "" || $("#unitLargePrintManifest").val() !== "" ||
            $("#unitUnratedDockReceipt").val() !== "")) {
        $.prompt("Choose Quick Print Printer");
        return false;
    }

    var rad = "";
    if (maxRadio !== undefined) {
        rad = "<font color='blue'>" + maxRadio + "</font>";
    }
    var txt = appendTextAlert('unsigned', 'unsignedCount');
    txt += appendTextAlert('original', 'originalCount');
    txt += appendTextAlert('nonNeg', 'nonNegotiableCount');
    txt += appendTextAlert('signed', 'signedCount');
    txt += appendTextAlert('frieght', 'frieghtCount');
    txt += appendTextAlert('MiniConsolidationManifestCol', 'unitMiniConsolidationManifestCount');
    txt += appendTextAlert('frieghtCol', 'frieghtColCount');
    txt += appendTextAlert('manifestCol', 'manifestCount');
    txt += appendTextAlert('LargePrintManifestCol', 'unitLargePrintManifestCount');
    txt += appendTextAlert('UnratedDockReceiptCol', 'unitUnratedDockReceiptCount');
    if (txt === "" || txt === undefined) {
        $.prompt("Please enter number of copies");
        return false;
    }
    if ($("#negotiableEmail").is(':checked') || $("#frieghtEmail").is(':checked') || $("#frieghtEmailCollect").is(':checked')) {
        $("#negotiableEmail").attr('checked', false);
        $("#frieghtEmail").attr('checked', false);
        $("#frieghtEmailCollect").attr('checked', false);
    }

    $.prompt("<html><body><table><tr><td>" + rad + "</td></tr>" + txt
            + "<tr><td><font color='red'> Are you sure you want to print?</font></td></tr></table></body></html>", {
                buttons: {Yes: 1, No: 2}, submit: function (v) {
                    if (v === 1) {
                        submitPrinter();
                    } else {
                        $.prompt.close();
                    }
                }
            });
}
function submitPrinter() {
    showLoading();
    $("#methodName").val("setPrint");
    var params = $("#exportVoyageHblBatchForm").serialize();
    $.post($("#exportVoyageHblBatchForm").attr("action"), params, function (data) {
        if ($("#fileNumber").val() === "") {
            parent.$.colorbox.close();
        } else {
            closePreloading();
        }
        $("#fileNumberId").val('');
        $("#fileNumber").val('');
    });
}
function openSchedulerPopUp() {
    var href = path + '/emailSchedulers.do?methodName=display&fileName=' + $("#voyageNumber").val() + "&isPrintPopUp=true";
    window.open(href, '_blank', 'width=1200,height=600,top=40,left=40,scrollbars=yes');
}
function setAgentContact(ele) {
    if ($("#" + ele.id).is(":checked")) {
        $("#toEmailAddress").val($("#agentEmailAddress").val());
    } else {
        $("#toEmailAddress").val("");
    }
}

function setFreigtCheckBox(ele, eValue) {
    if (($("#" + ele.id).is(":checked") || ($("#" + eValue).is(":checked"))) &&
            ($('input:radio[name=billOfLading]:checked').val() === undefined) || (!$("#negotiableEmail").is(":checked"))) {
        $("input[name=billOfLading]").val(["RATED"]);
    } else if (($("#frieghtInvoice").val() === "") && ($("#frieghtColInvoice").val() === "")) {
        $("input[name=billOfLading]").val([""]);
    }
}

function setFreight(ele, eValue) {
    if (ele.value !== "" || $("#" + eValue).val() !== "") {
        $("input[name=billOfLading]").val(["RATED"]);
    } else if ((!$("#frieghtEmail").is(":checked")) && (!$("#frieghtEmailCollect").is(":checked"))) {
        $("input[name=billOfLading]").val([""]);
    }
}
function setSelectOption() {
    if (($("#frieghtInvoice").val() !== "" || $("#frieghtEmail").is(":checked")
            || $("#frieghtInvoiceCollect").val() !== "" || $("#frieghtEmailCollect").is(":checked")) && (!$("#negotiableEmail").is(":checked"))) {
        $.prompt("Freight invoice is selected");
        $("input[name=billOfLading]").val(["RATED"]);
        return false;
    }
}
function selectRadioOption() {
    if ((!$("#negotiableEmail").is(":checked") && ($('#frieghtEmail').is(':checked') || $("#frieghtEmailCollect").is(":checked")))) {
        $("input[name=billOfLading]").val(["RATED"]);
        return false;
    }
}
function checkDrAvaiableInVoyage() {
    var flag = true;
    var headerId = $("#headerId").val();
    var unitSsid = $("#unitSSId").val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.common.constant.ExportUnitQueryUtils",
            methodName: "isDrAvailableInVoyage",
            param1: headerId,
            param2: unitSsid,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (!data) {
                flag = false;
            }
        }
    });
    return flag;
}

function validateCollectCharges() {
    var flag = true;
    var headerId = $("#headerId").val();
    var unitSsid = $("#unitSSId").val();
    var fileId = $("#fileNumberId").val() === (undefined || null) ? "" : $("#fileNumberId").val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.common.constant.ExportUnitQueryUtils",
            methodName: "validateCollectCharges",
            param1: headerId,
            param2: unitSsid,
            param3: fileId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (!data) {
                flag = false;
            }
        }
    });
    return flag;
}