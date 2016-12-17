/* 
 * Document   : accruals
 * Created on : Jul 11, 2012, 11:25:00 PM
 * Author     : Lakshmi Naryanan
 */

var lastSearchAction;

function clearNotification() {
    $("#message").html("");
    $("#error").html("");
}

function showLightBox(title, url, height, width, callback) {
    height = height || $(document).height() - 50;
    width = width || $(document).width() - 100;
    url += (url.indexOf("?") > -1 ? "&" : "?") + "TB_iframe&height=" + height + "&width=" + width;
    Lightbox.showPopUp(title, url, "sexylightbox", "", callback);
}

function setResultHeight() {
    if ($(".result-container").length > 0) {
        var windowHeight = window.parent.getFrameHeight();
        if ($("#toggled").val() === "true") {
            $(".result-container").height(windowHeight - $(".invoice-container").height() - $(".filter-container").height() - $(".table-banner").height() - 150);
        } else {
            $(".result-container").height(windowHeight - $(".invoice-container").height() - $(".table-banner").height() - 150);
        }
        $("body").css("overflow", "hidden");
    }
}

function callServer(data, preloading, success, error, url) {
    url = url || $("#accrualsForm").attr("action")
    ajaxCall(url, {
        data: data,
        preloading: preloading,
        success: success,
        error: error,
        async: false
    });
}

var updateIndex;
function deriveGlAccountSuccess(result) {
    var row = $("#accruals tbody tr").eq(updateIndex);
    if (result.indexOf("Suffix") > 0) {
        $.prompt("For Cost Code - " + row.find(".costCode").val() + ", " + result, {
            callback: function () {
                deriveGlAccountError();
            }
        });
    } else {
        row.find(".glAccount").val(result);
        row.find(".terminal").val(result.substr(result.lastIndexOf("-") + 1)).attr("readonly", true).addClass("readonly");
        row.find(".costCode").css("border", "1px solid #C4C5C4");
        updateAccrual(row);
    }
}

function deriveGlAccountError() {
    var row = $("#accruals tbody tr").eq(updateIndex);
    var suffix = $.trim(row.find(".suffix").val());
    if ((suffix === "B" || suffix === "L" || suffix === "D")) {
        row.find(".terminal").val("").callFocus();
    } else {
        row.find(".costCode").val("").callFocus();
    }
}

function deriveGlAccount(index) {
    updateIndex = index;
    var row = $("#accruals tbody tr").eq(index);
    var suffix = $.trim(row.find(".suffix").val());
    var terminal = $.trim(row.find(".terminal").val());
    if ((suffix === "B" || suffix === "L" || suffix === "D") && terminal === "") {
        $.prompt("Please enter the Termial Number", {
            callback: function () {
                row.find(".terminal").val("").callFocus();
            }
        });
    } else {
        var account = row.find(".account").val();
        var shipmentType = row.find(".shipmentType").val();
        callServer({
            action: "deriveGlAccount",
            account: account,
            suffix: suffix,
            shipmentType: shipmentType,
            terminal: terminal
        }, true, "deriveGlAccountSuccess", "deriveGlAccountError");
    }
}

function showTerminal(index) {
    var row = $("#accruals tbody tr").eq(index);
    var suffix = $.trim(row.find(".suffix").val());
    if ((suffix === "B" || suffix === "L" || suffix === "D")) {
        row.find(".terminal").val("").removeAttr("readonly").removeClass("readonly").callFocus();
        row.find(".terminal").change(function () {
            deriveGlAccount(index);
        });
    } else {
        row.find(".terminal").attr("readonly", true).addClass("readonly");
        deriveGlAccount(index);
    }
}

function validateLimit(ele) {
    if (isNaN($(ele).val()) || Number($(ele).val()) <= 0) {
        $.prompt("Please enter valid number", {
            callback: function () {
                $(ele).val("200").callFocus();
            }
        });
    } else {
        $(ele).val(Number($(ele).val()));
    }
}

function setShipmentType(index) {
    var row = $("#accruals tbody tr").eq(index);
    row.find(".account").val("");
    row.find(".glAccount").val("");
    row.find(".terminal").val("").attr("readonly", true).addClass("readonly");
    $(row.find(".costCode")).setOptions({
        extraParams: {
            param1: row.find(".shipmentType").val()
        }
    });
}

function initRows() {
    $("[title != '']").not("link").tooltip();
    var zebra = "odd";
    var index = 0;
    $(".id").each(function () {
        var row = $(this).parent().parent();
        if (row.find(".costCode").length > 0) {
            row.find(".costCode").initautocomplete({
                url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=COST_CODE&template=costCode&fieldIndex=1,2,3,5,6&",
                width: "420px",
                otherFields: "bluescreenCostCode^shipmentType^suffix^account",
                row: true,
                checkClass: "costCodeCheck",
                callBefore: "setShipmentType(" + index + ")",
                callback: "showTerminal(" + index + ")",
                resultsClass: "ac_results z-index",
                resultPosition: "fixed",
                scroll: true,
                scrollHeight: 300
            });
            row.find(".amount").keydown(function (event) {
                allowOnlyNumbers(event, this);
            });
            row.find(".amount").keyup(function (event) {
                validateAmount(event, this);
                if (event.keyCode === 13) {
                    var row = $(this).parent().parent();
                    if (row.find(".old-amount").val() !== $.trim($(this).val())) {
                        if (row.find(".transactionType").val() === "AR") {
                            onchangeArAmount(this);
                        } else {
                            onchangeAmount(this);
                        }
                    }
                }
            });
            index++;
        }
        row.removeClass("odd").removeClass("even");
        row.addClass(zebra);
        if (zebra === "odd") {
            zebra = "even";
        } else {
            zebra = "odd";
        }
        row.find(".terminal").initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=TERMINALS&template=chargeCode&fieldIndex=1&",
            minChars: "1",
            width: "250px",
            resultsClass: "ac_results z-index",
            resultPosition: "fixed",
            scroll: true,
            scrollHeight: 200
        });
        row.find(".terminal").attr("readonly", true).addClass("readonly");
    });
    calculateAmount();

}

function setSelectedIds() {
    clearHiddenIds();
    var arIds = new Array();
    var accrualIds = new Array();
    $(".assign:checked").not(":disabled").each(function () {
        var row = $(this).parent().parent();
        if (row.find(".transactionType").val() === "AR") {
            arIds.push($(this).val());
        } else {
            accrualIds.push($(this).val());
        }
    });
    $(".dispute:checked").not(":disabled").each(function () {
        var row = $(this).parent().parent();
        if (row.find(".transactionType").val() === "AR") {
            arIds.push($(this).val());
        } else {
            accrualIds.push($(this).val());
        }
    });
    $(".inactive:checked").not(":disabled").each(function () {
        var row = $(this).parent().parent();
        if (row.find(".transactionType").val() === "AR") {
            arIds.push($(this).val());
        } else {
            accrualIds.push($(this).val());
        }
    });
    $("#arIds").val(arIds);
    $("#accrualIds").val(accrualIds);
}

function createParams(action) {
    $("#error").html("");
    var isSortingOrPaging = false;
    if ($.trim(action) === "") {
        action = lastSearchAction || "searchByVendor";
        isSortingOrPaging = true;
    }
    var params = "action=" + action;
    params += "&from=" + $("#from").val();
    params += "&limit=" + $("#limit").val();
    params += "&ediInvoiceNumber=" + $("#ediInvoiceNumber").val();
    if (action === "searchByVendor") {
        params += "&vendorNumber=" + $.trim($("#vendorNumber").val());
        lastSearchAction = "searchByVendor";
    } else if (action === "searchByInvoice") {
        params += "&vendorNumber=" + $.trim($("#vendorNumber").val());
        params += "&invoiceNumber=" + $.trim($("#invoiceNumber").val());
    } else if (action === "searchByFilter") {
        params += "&" + $("#toggleDiv :input").serialize();
        lastSearchAction = "searchByFilter";
    }
    if ($("#accruals>tbody").length > 0) {
        var invoiceNumber = $.trim($("#invoiceNumber").val());
        var removed = 0;
        if (action === "searchByVendor") {
            $(".assign").each(function () {
                var row = $(this).parent().parent();
                if ($(this).is(":disabled") || row.find(".dispute").is(":disabled")
                        || (row.find(".transactionType").val() === "AC" && row.find(".inactive").is(":disabled"))
                        || row.find(".status").val() === "IN PROGRESS" || row.find(".status").val() === "DISPUTE"
                        || row.find(".status").val() === "EDI IN PROGRESS" || row.find(".status").val() === "EDI DISPUTE"
                        || (!$(this).is(":checked") && !row.find(".dispute").is(":checked") && !row.find(".inactive").is(":checked"))) {
                    row.remove();
                    removed++;
                }
            });
        } else if (action === "searchByInvoice") {
            $(".status[value='IN PROGRESS'], .status[value='DISPUTE'], .status[value='EDI IN PROGRESS'], .status[value='EDI DISPUTE']").each(function () {
                var row = $(this).parent().parent();
                if (invoiceNumber.toUpperCase() !== $.trim(row.find(".invoiceNumber").val().toUpperCase())) {
                    row.remove();
                    removed++;
                }
            });
        } else if (action === "searchByFilter") {
            $(".assign").each(function () {
                var row = $(this).parent().parent();
                if ($(this).is(":disabled") || row.find(".dispute").is(":disabled")
                        || (row.find(".transactionType").val() === "AC" && row.find(".inactive").is(":disabled"))
                        || (!$(this).is(":checked") && !row.find(".dispute").is(":checked") && !row.find(".inactive").is(":checked"))) {
                    row.remove();
                    removed++;
                }
            });
        }
        $("#remainingRows").val(Number($("#remainingRows").val()) - removed);
        setSelectedIds();
        params += "&arIds=" + $("#arIds").val();
        params += "&accrualIds=" + $("#accrualIds").val();
        params += "&rowsOnly=true";
        params += "&remainingRows=" + $("#remainingRows").val();
        if (action === "searchByVendor" && !isSortingOrPaging) {
            params += "&sortBy=reportingDate";
            params += "&orderBy=desc";
            params += "&selectedPage=1";
        } else {
            params += "&sortBy=" + $("#sortBy").val();
            params += "&orderBy=" + $("#orderBy").val();
            if (action === "searchByInvoice") {
                params += "&removedRows=" + removed;
                params += "&totalRows=" + $("#totalRows").val();
                params += "&selectedRows=" + $("#selectedRows").val();
                params += "&totalPages=" + $("#totalPages").val();
            }
            params += "&selectedPage=" + $("#selectedPage").val();
        }
    } else {
        params += "&rowsOnly=false";
        params += "&sortBy=reportingDate";
        params += "&orderBy=desc";
        params += "&selectedPage=1";
    }
    return params;
}

function loadResults(results) {
    if ($.trim(results) !== "") {
        if ($("#accruals>tbody").length > 0) {
            $(results).prependTo("#accruals>tbody");
        } else {
            $("#results").html(results);
        }
        if ($(".result-container").length > 0) {
            $("#result-header").html($("#result-banner").html()).show();
            $("#result-banner-row").remove();
            setResultHeight();
            initRows();
        }
    }
}

function searchByVendorSuccess(results) {
    loadResults(results);
}

function searchByVendorError() {
    $("#vendorName").val("").callFocus();
}

function searchByVendor() {
    var vendorNumber = $.trim($("#vendorNumber").val());
    if (vendorNumber !== "") {
        var params = createParams("searchByVendor");
        callServer(params, true, "searchByVendorSuccess", "searchByVendorError");
    }
}

function onAutocompleteVendor() {
    if ($.trim($("#vendorNumberOld").val()) !== $.trim($("#vendorNumber").val())) {
        $("#vendorNumberOld").val($.trim($("#vendorNumber").val()));
        $("#searchVendorName").val($.trim($("#vendorName").val()));
        $("#searchVendorNameCheck").val($.trim($("#vendorName").val()));
        $("#searchVendorNumber").val($.trim($("#vendorNumber").val()));
        $("#invoiceNumber").val("");
        $("#status").val("");
        $("#invoiceAmount").val("0.00");
        $("#invoiceDate").val("");
        $("#dueDate").val("");
        $("#dispute").removeAttr("checked");
        $("#reject").removeAttr("checked");
        searchByVendor();
    }
}

function initVendor() {
    if ($("#vendorName").length > 0 && !$("#vendorName").hasClass("readonly")) {
        $("#vendorName").initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=VENDOR&template=vendor&fieldIndex=1,2,10,11,12&",
            width: "480px",
            otherFields: "vendorNumber^citDesc^creditTerm^creditId",
            resultsClass: "ac_results z-index",
            resultPosition: "absolute",
            scroll: true,
            scrollHeight: 300,
            focusField: "invoiceNumber",
            callback: "onAutocompleteVendor()"
        });
    }

    if ($("#searchVendorName").length > 0 && !$("#searchVendorName").hasClass("readonly")) {
        $("#searchVendorName").initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=VENDOR&template=vendor&fieldIndex=1,2&",
            width: "480px",
            otherFields: "searchVendorNumber",
            resultsClass: "ac_results z-index",
            resultPosition: "absolute",
            scroll: true,
            scrollHeight: 300,
            callback: "searchByFilter()"
        });
    }
}

function gotoTradingPartner(nameField, numberField) {
    var vendorNumber = $.trim($("#" + numberField).val());
    if (vendorNumber === "") {
        $.prompt("Please enter the Vendor", {
            callback: function () {
                $("#" + nameField).callFocus();
            }
        });
    }
    else {
        window.parent.gotoTradingPartner(vendorNumber, "AP Config")
    }
}

function onInvoiceCallBefore() {
    $("#status").val("");
    $("#dispute").removeAttr("checked");
    $("#reject").removeAttr("checked");
    $("#auto").removeAttr("checked");
    $("#invoiceNumber").setOptions({
        extraParams: {
            param1: $("#vendorNumber").val()
        }
    });
}

function searchByInvoiceSuccess(results) {
    loadResults(results);
}

function searchByInvoiceError() {
    $("#invoiceNumber").val("").callFocus();
}

function searchByInvoice() {
    var invoiceNumber = $.trim($("#invoiceNumber").val());
    var vendorNumber = $.trim($("#vendorNumber").val());
    if (vendorNumber !== "" && invoiceNumber !== "") {
        var params = createParams("searchByInvoice");
        callServer(params, true, "searchByInvoiceSuccess", "searchByInvoiceError");
    }
}

function overwriteDocuments(oldDocumentId, newDocumentId) {
    var params = "action=overwriteDocuments";
    params += "&oldDocumentId=" + oldDocumentId;
    params += "&newDocumentId=" + newDocumentId;
    callServer(params, false);
}

function onAutocompleteInvoice() {
    var status = $("#status").val();
    if (status === "Dispute" || ($.trim($("#from").val()) === "EDI" && status === "EDI Dispute")) {
        $("#dispute").attr("checked", "checked");
    }
    if ($.trim($("#invoiceNumberOld").val()) !== $.trim($("#invoiceNumber").val())) {
        if ($.trim($("#from").val()) === "SSMasters") {
            var vendorNumber = $.trim($("#vendorNumber").val());
            var invoiceNumberOld = $.trim($("#invoiceNumberOld").val());
            var invoiceNumber = $.trim($("#invoiceNumber").val());
            var oldDocumentId = vendorNumber + "-" + invoiceNumberOld;
            var newDocumentId = vendorNumber + "-" + invoiceNumber;
            overwriteDocuments(oldDocumentId, newDocumentId);
        }
        $("#invoiceNumberOld").val($.trim($("#invoiceNumber").val()));
        if (status === "In Progress"
                || ($.trim($("#from").val()) === "EDI"
                        && (status === "EDI In Progress" || status === "EDI Ready To Post" || status === "EDI Ready To Post / Fully Mapped"))) {
            searchByInvoice();
        } else if (status === "Dispute" || ($.trim($("#from").val()) === "EDI" && status === "EDI Dispute")) {
            $("#dispute").attr("checked", "checked");
            searchByInvoice();
        } else if (status === "Reject") {
            $("#reject").attr("checked", "checked");
        }
    }
}

function updateOldInvoiceNumber() {
    if ($.trim($("#from").val()) === "SSMasters") {
        var vendorNumber = $.trim($("#vendorNumber").val());
        var invoiceNumberOld = $.trim($("#invoiceNumberOld").val());
        var invoiceNumber = $.trim($("#invoiceNumber").val());
        var oldDocumentId = vendorNumber + "-" + invoiceNumberOld;
        var newDocumentId = vendorNumber + "-" + invoiceNumber;
        overwriteDocuments(oldDocumentId, newDocumentId);
    }
    $("#invoiceNumberOld").val($.trim($("#invoiceNumber").val()));
    searchByInvoice();
}

function findDuplicatesSuccess(status) {
    if (status === "Available") {
        $("#invoiceNumber").val($.trim($("#invoiceNumber").val()));
        if ($.trim($("#from").val()) === "EDI") {
            var vendorNumber = $.trim($("#vendorNumber").val());
            var invoiceNumberOld = $.trim($("#invoiceNumberOld").val());
            var invoiceNumber = $.trim($("#invoiceNumber").val());
            var oldDocumentId = vendorNumber + "-" + invoiceNumberOld;
            var newDocumentId = vendorNumber + "-" + invoiceNumber;
            overwriteDocuments(oldDocumentId, newDocumentId);
            $("#invoiceNumberOld").val($.trim($("#invoiceNumber").val()));
        } else {
            updateOldInvoiceNumber();
        }
    } else if (status === "Posted to AP") {
        $.prompt("Invoice is posted to AP already", {
            callback: function () {
                $("#invoiceNumber").val("").callFocus();
            }
        });
    } else if (status === "Posted to Negative AP") {
        $.prompt("Invoice is posted to Negative AP already", {
            callback: function () {
                $("#invoiceNumber").val("").callFocus();
            }
        });
    } else if (status === "In Progress" || status === "Dispute" || status === "Reject") {
        if (status === "In Progress") {
            updateOldInvoiceNumber();
        } else if (status === "Dispute") {
            $("#dispute").attr("checked", "checked");
            updateOldInvoiceNumber();
        } else if (status === "Reject") {
            $("#reject").attr("checked", "checked");
        }
    } else if ($.trim($("#from").val()) === "EDI") {
        if (status === "EDI In Progress" || status === "EDI Ready To Post" || status === "EDI Ready To Post / Fully Mapped") {
            updateOldInvoiceNumber();
        } else if (status === "EDI Dispute") {
            $("#dispute").attr("checked", "checked");
            updateOldInvoiceNumber();
        }
    } else {
        $.prompt(status, {
            buttons: {
                Yes: true,
                No: false
            },
            callback: function (v, m, f) {
                if (v) {
                    if ($.trim($("#from").val()) !== "EDI") {
                        updateOldInvoiceNumber();
                    }
                    $("#invoiceAmount").callFocus();
                } else {
                    $("#invoiceNumber").val("").callFocus();
                    $("#invoiceNumberOld").val($.trim($("#invoiceNumber").val()));
                }
            }
        });
    }
}

function findDuplicatesError() {
    $("#invoiceNumber").val("").callFocus();
}

function findDuplicates() {
    if ($.trim($("#invoiceNumber").val()) !== "") {
        if ($.trim($("#vendorNumber").val()) === "") {
            $.prompt("Please enter Vendor", {
                callback: function () {
                    $("#vendorName").callFocus();
                }
            });
        } else {
            var url = path + "/apInvoice.do";
            callServer({
                action: "findDuplicates",
                vendorNumber: $.trim($("#vendorNumber").val()),
                invoiceNumber: $.trim($("#invoiceNumber").val())
            }, true, "findDuplicatesSuccess", "findDuplicatesError", url);
        }
    }
}

function onInvoiceBlur() {
    $("#invoiceAmount").callFocus();
    if ($.trim($("#invoiceNumber").val()) !== "" && $.trim($("#invoiceNumber").val()) !== $.trim($("#invoiceNumberOld").val())) {
        findDuplicates();
    }
}

function initInvoice() {
    $("#invoiceNumberOld").val($.trim($("#invoiceNumber").val()));
    if ($("#vendorName").length > 0 && !$("#vendorName").hasClass("readonly")
            && $("#invoiceNumber").length > 0 && !$("#invoiceNumber").hasClass("readonly")) {
        var url = path + "/autocompleter/action/getAutocompleterResults.jsp?query=AP_INVOICE&template=apInvoice&fieldIndex=1,2,3,4,5,6,7,8&";
        if ($.trim($("#from").val()) === "EDI") {
            url = path + "/autocompleter/action/getAutocompleterResults.jsp?query=EDI_INVOICE&template=ediInvoice&fieldIndex=1,2,3,4,5,6,7,8&";
        }
        $("#invoiceNumber").initautocomplete({
            url: url,
            width: "300px",
            otherFields: "invoiceAmount^status^invoiceDate^creditId^creditTerm^creditDesc^dueDate",
            resultsClass: "ac_results z-index",
            resultPosition: "absolute",
            scroll: true,
            scrollHeight: 300,
            mustMatch: true,
            focusField: "invoiceAmount",
            callBefore: "onInvoiceCallBefore()",
            callback: "onAutocompleteInvoice()",
            onblur: "onInvoiceBlur()"
        });
    }
    $("#invoiceNumber").keyup(function (event) {
        if (event.keyCode === 13) {
            if ($.trim($("#invoiceNumber").val()) !== "") {
                $("#invoiceAmount").callFocus();
            }
        }
    });
    $("#invoiceAmount").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    $("#invoiceAmount").keyup(function (event) {
        validateAmount(event, this);
    });
    $("#invoiceAmount").change(function () {
        if (isNaN($(this).val())) {
            $(this).val("0.00");
        } else {
            $(this).val(Number($(this).val()).toFixed(2));
        }
        calculateAmount();
    });
    $("#invoiceDateCalendar").insertFromCalendar({
        inputField: "invoiceDate",
        format: "%m/%d/%Y"
    });
}

function onDueDateSuccess(result) {
    $("#dueDate").val(result);
}

function onDueDateError() {
    $("#invoiceDate").val("").callFocus();
    $("#dueDate").val("");
}

function calculateDueDate() {
    var url = path + "/apInvoice.do";
    var params = "action=calculateDueDate&invoiceDate=" + $("#invoiceDate").val() + "&creditTerm=" + $("#creditTerm").val();
    callServer(params, false, "onDueDateSuccess", "onDueDateError", url);
}

function validateDate(ele) {
    if ($(ele).attr("id") === "invoiceDate") {
        if ($.trim($("#invoiceDate").val()) !== "") {
            if (!isDate("#invoiceDate")) {
                $.prompt("Please enter Invoice Date in mm/dd/yyyy format", {
                    callback: function () {
                        $("#invoiceDate").val("").callFocus();
                        $("#dueDate").val("");
                    },
                    loaded: function () {
                        $("#jqi_state0_buttonOk").callFocus();
                    }
                });
            } else {
                if (Date.parse($("#invoiceDate").val()) > Date.parse(Date())) {
                    $.prompt("Invoice date should not be greater than today's date", {
                        callback: function () {
                            $("#invoiceDate").val("").callFocus();
                            $("#dueDate").val("");
                        },
                        loaded: function () {
                            $("#jqi_state0_buttonOk").callFocus();
                        }
                    });
                } else {
                    calculateDueDate();
                }
            }
        } else {
            $("#dueDate").val("");
        }
    } else if ($.trim($(ele).val()) !== "" && !isDate(ele)) {
        $.prompt("Please enter date in mm/dd/yyyy format", {
            callback: function () {
                $(ele).val("").callFocus();
            }
        });
    }
}

function clearAll() {
    $("#action").val("clear");
    $("#accrualsForm").submit();
}

function uploadInvoice(callback) {
    callback = callback || "";
    var vendorNumber = $.trim($("#vendorNumber").val());
    var invoiceNumber = $.trim($("#invoiceNumber").val());
    if (vendorNumber === "") {
        $.prompt("Please enter Vendor", {
            callback: function () {
                $("#vendorName").val("").callFocus();
            }
        });
    } else if (invoiceNumber === "") {
        $.prompt("Please enter Invoice Number", {
            callback: function () {
                $("#invoiceNumber").val("").callFocus();
            }
        });
    } else {
        var documentId = vendorNumber + "-" + invoiceNumber;
        var title = "Upload Invoice";
        var url = path + "/scan.do?screenName=INVOICE&documentId=" + encodeURIComponent(documentId);
        showLightBox(title, url, 400, 800, callback);
    }
}

function initInactivateAccruals() {
    if ($("#inactivateVendorName").length > 0 && !$("#inactivateVendorName").hasClass("readonly")) {
        $("#inactivateVendorName").initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=VENDOR&template=vendor&fieldIndex=1,2&",
            width: "480px",
            otherFields: "inactivateVendorNumber",
            resultsClass: "ac_results z-index",
            resultPosition: "absolute",
            scroll: true,
            scrollHeight: 150
        });
    }
    $("#fromDateCalendar").insertFromCalendar({
        inputField: "fromDate",
        format: "%m/%d/%Y"
    });
    $("#toDateCalendar").insertFromCalendar({
        inputField: "toDate",
        format: "%m/%d/%Y"
    });
}

function onchangeDateRange(ele) {
    if ($.trim($(ele).val()) !== "") {
        if (!isDate(ele)) {
            $.prompt("Please enter Date in mm/dd/yyyy format", {
                callback: function () {
                    $(ele).val("").callFocus();
                }
            });
        }
    }
}

function onchangeInactivateBy(ele) {
    if ($(ele).val() === "dateRange") {
        $("#fromDate, #toDate").removeClass("readonly").removeAttr("readonly").val("");
        $("#fromDate").callFocus();
    } else {
        $("#fromDate, #toDate").addClass("readonly").attr("readonly", true).val("");
        $("#inactivateVendorName").callFocus();
    }
}

function validateInactivateAccruals() {
    if ($("#inactivateBy").val() === "dateRange") {
        if ($.trim($("#fromDate").val()) === "") {
            $.prompt("Please enter From Date", {
                callback: function () {
                    $("#fromDate").val("").callFocus();
                }
            });
            return false;
        } else if ($.trim($("#toDate").val()) === "") {
            $.prompt("Please enter To Date", {
                callback: function () {
                    $("#toDate").val("").callFocus();
                }
            });
            return false;
        } else if (Date.parse($("#fromDate").val()) > Date.parse($("#toDate").val())) {
            $.prompt("From Date should not be greater than To Date", {
                callback: function () {
                    $("#fromDate").val("").callFocus();
                }
            });
            return false;
        } else {
            return true;
        }
    } else {
        if ($.trim($("#fromAmount").val()) === "") {
            $.prompt("Please enter From Amount", {
                callback: function () {
                    $("#fromAmount").val("").callFocus();
                }
            });
            return false;
        } else if ($.trim($("#toAmount").val()) === "") {
            $.prompt("Please enter To Amount", {
                callback: function () {
                    $("#toAmount").val("").callFocus();
                }
            });
            return false;
        } else if (parseFloat($("#fromAmount").val()) > parseFloat($("#toAmount").val())) {
            $.prompt("From Amount should not be greater than To Amount", {
                callback: function () {
                    $("#fromAmount").val("").callFocus();
                }
            });
            return false;
        } else {
            return true;
        }
    }
}

function inactivateAccrualsSuccess(result) {
    $("#message").html(result);
    hideInactivateAccruals();
}

function inactivateAccrualsError() {
    showAlternateMask();
    $("#inactivate-container").show();
}

function inactivateAccruals() {
    if (validateInactivateAccruals()) {
        deactivateAutosave();
        hideAlternateMask();
        $("#inactivate-container").hide();
        var params = "action=inactivateAccruals";
        params += "&" + $("#inactivate-container :input:not(:button)").serialize();
        callServer(params, true, "inactivateAccrualsSuccess", "inactivateAccrualsError");
    }
}

function activateAccrualsSuccess(result) {
    $("#message").html(result);
    hideInactivateAccruals();
}

function activateAccrualsError() {
    showAlternateMask();
    $("#inactivate-container").show();
}

function activateAccruals() {
    if (validateInactivateAccruals()) {
        deactivateAutosave();
        hideAlternateMask();
        $("#inactivate-container").hide();
        var params = "action=activateAccruals";
        params += "&" + $("#inactivate-container :input:not(:button)").serialize();
        callServer(params, true, "activateAccrualsSuccess", "activateAccrualsError");
    }
}

function showInactivateAccruals() {
    showAlternateMask();
    $("#inactivate-container").center().show(500, function () {
        $("#inactivateBy").callFocus();
    });
}

function hideInactivateAccruals() {
    $("#inactivate-container :input:not(:button)").val("");
    $("#inactivateBy").val("dateRange");
    $("#fromAmount").val("-5.00");
    $("#toAmount").val("+1.00");
    $("#inactivate-container").hide(500, function () {
        hideAlternateMask();
    });
}

function deriveNewGlAccountSuccess(result) {
    if (result.indexOf("Suffix") > 0) {
        $.prompt("For Cost Code - " + $("#newCostCode").val() + ", " + result, {
            callback: function () {
                deriveNewGlAccountError();
            }
        });
    } else {
        $("#newGlAccount").val(result);
        $("#newAmount").callFocus();
        $("#newTerminal").val(result.substr(result.lastIndexOf("-") + 1)).attr("readonly", true).addClass("readonly");
    }
}

function deriveNewGlAccountError() {
    var suffix = $.trim($("#newSuffix").val());
    if ((suffix === "B" || suffix === "L" || suffix === "D")) {
        $("#newTerminal").val("").callFocus()
    } else {
        $("#newCostCode").val("").callFocus();
    }
}

function deriveNewGlAccount() {
    var suffix = $.trim($("#newSuffix").val());
    var terminal = $.trim($("#newTerminal").val());
    if ((suffix === "B" || suffix === "L" || suffix === "D") && terminal === "") {
        $.prompt("Please enter the Termial Number", {
            callback: function () {
                $("#newTerminal").val("").callFocus();
            }
        });
    } else {
        var account = $("#newAccount").val();
        var shipmentType = $("#newShipmentType").val();
        callServer({
            action: "deriveGlAccount",
            account: account,
            suffix: suffix,
            shipmentType: shipmentType,
            terminal: terminal
        }, true, "deriveNewGlAccountSuccess", "deriveNewGlAccountError");
    }
}

function showNewTerminal() {
    var suffix = $.trim($("#newSuffix").val());
    if (suffix === "B" || suffix === "L" || suffix === "D") {
        $("#newTerminal").removeAttr("readonly").removeAttr("tabindex").removeClass("readonly").val("").callFocus();
    } else {
        $("#newTerminal").attr("readonly", true).attr("tabindex", "-1").addClass("readonly");
        deriveNewGlAccount();
    }
}

function onCostCodeCallBefore() {
    $("#newAccount").val("");
    $("#newGlAccount").val("");
    $("#newTerminal").val("").attr("readonly", true).attr("tabindex", "-1").addClass("readonly");
    $("#newCostCode").setOptions({
        extraParams: {
            param1: $("#newShipmentType").val()
        }
    });
}

function onDrCallBefore() {
    var shipment_type = $("#newShipmentType").val();
    var param1;
    var param2;
    if (shipment_type.indexOf("LCLE") !== -1) {
        param1 = "E";
        param2 = "E";
    } else if (shipment_type.indexOf("LCLI") !== -1) {
        param1 = "I";
        param2 = "T";
    }
    $("#lclDrNo").setOptions({
        extraParams: {
            param1: param1,
            param2: param2
        }
    });
}

function fillVoy() {
    $("#newVoyageNumber").val($("#lclVoyageNo").val());
}

function fillDr() {
    $("#newDockReceipt").val($("#lclDrNo").val());
}

function initAddAccrual() {
    if ($("#newVendorName").length > 0 && !$("#newVendorName").hasClass("readonly")) {
        $("#newVendorName").initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=VENDOR&template=vendor&fieldIndex=1,2&",
            width: "480px",
            otherFields: "newVendorNumber",
            resultsClass: "ac_results z-index",
            resultPosition: "absolute",
            scroll: true,
            scrollHeight: 250,
            focusField: "newInvoiceNumber"
        });
    }

    if ($("#newBlNumber").length > 0) {
        $("#newBlNumber").initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=BL&template=bl&fieldIndex=1,2,3&",
            width: "480px",
            otherFields: "newVoyageNumber^newDockReceipt",
            resultsClass: "ac_results z-index",
            resultPosition: "absolute",
            scroll: true,
            scrollHeight: 250,
            focusField: "newCostCode"
        });
    }

    $("#newCostCode").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=COST_CODE&template=costCode&fieldIndex=1,2,3,5,6&",
        width: "420px",
        otherFields: "newBluescreenCostCode^newShipmentType^newSuffix^newAccount",
        callback: "showNewTerminal()",
        callBefore: "onCostCodeCallBefore()",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 200
    });

    $("#newTerminal").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=TERMINALS&template=chargeCode&fieldIndex=1&",
        minChars: "1",
        width: "250px",
        callback: "deriveNewGlAccount()",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 200
    });


    var $voy = $("#lclVoyageNo");
    var $dr = $("#lclDrNo");
    if ($voy.length > 0 && !$voy.hasClass("readonly")) {
        $voy.initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=VOYAGE&template=glPeriod&fieldIndex=1,3&",
            width: "420px",
            otherFields: "unitId",
            callback: "fillVoy()",
            resultsClass: "ac_results z-index",
            resultPosition: "absolute",
            scroll: true,
            scrollHeight: 250
        });
    }

    if ($dr.length > 0 && !$dr.hasClass("readonly")) {
        $dr.initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=DOC_RECEIPT_LCL_I_E_T&template=bl&fieldIndex=1,2&",
            width: "420px",
            otherFields: "fileId",
            callBefore: "onDrCallBefore()",
            callback: "fillDr()",
            resultsClass: "ac_results z-index",
            resultPosition: "absolute",
            scroll: true,
            scrollHeight: 250
        });
    }

    $("#newAmount").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    $("#newAmount").keyup(function (event) {
        validateAmount(event, this);
    });
    $("#newAmount").change(function () {
        if (isNaN($(this).val())) {
            $(this).val("0.00");
        } else {
            $(this).val(Number($(this).val()).toFixed(2));
        }
    });
}

function showAddAccrual() {
    showAlternateMask();
    $("#add-accrual-container").center().show(500, function () {
        $("#newVendorName").val($("#vendorName").val());
        $("#newVendorNameCheck").val($("#vendorName").val());
        $("#newVendorNumber").val($("#vendorNumber").val());
        $("#newInvoiceNumber").val($("#invoiceNumber").val());
        if ($.trim($("#newVendorNumber").val()) !== "" && $.trim($("#newInvoiceNumber").val()) !== "") {
            $("#newBlNumber").callFocus();
        } else if ($.trim($("#newVendorNumber").val()) !== "") {
            $("#newInvoiceNumber").callFocus();
        } else {
            $("#newVendorName").callFocus();
        }
    });
}

function clearAccrual() {
    $("#add-accrual-container :input:not(:button)").val("");
    $("#newVendorName").callFocus();
    toggleByModule();
}

function toggleByModule() {
    var selected_type = $("#newShipmentType").val();
    var $default_bl = $("#newBlNumber");
    var $default_voy = $("#newVoyageNumber");
    var $default_dr = $("#newDockReceipt");
    var $lcl_bl = $("#lclBlNo");
    var $lcl_voy = $("#lclVoyageNo");
    var $lcl_dr = $("#lclDrNo");
    var $air_bl = $("#airBlNo");
    var $air_voy = $("#airVoyageNo");
    var $air_dr = $("#airDrNo");
    var $cost_code = $("#newCostCode");
    $default_bl.val("");
    $default_voy.val("");
    $default_dr.val("");
    $("#newAccount").val("");
    $("#newCostCode").val("");
    $("#newGlAccount").val("");
    $("#newTerminal").val("").attr("readonly", true).attr("tabindex", "-1").addClass("readonly");
    if ($("#newShipmentType").val() !== "") {
        $default_bl.attr("readonly", false);
        $default_bl.removeClass("readonly");
        $default_voy.attr("readonly", false);
        $default_voy.removeClass("readonly");
        $default_dr.attr("readonly", false);
        $default_dr.removeClass("readonly");
        $cost_code.attr("readonly", false);
        $cost_code.removeClass("readonly");
        if (selected_type.indexOf("LCL") !== -1) {
            $lcl_bl.show();
            $lcl_voy.show();
            $lcl_dr.show();
            $default_bl.hide();
            $default_voy.hide();
            $default_dr.hide();
            $air_bl.hide();
            $air_voy.hide();
            $air_dr.hide();
            $lcl_bl.attr("readonly", true);
            $lcl_bl.addClass("readonly");
        } else if (selected_type.indexOf("FCL") !== -1) {
            $default_bl.show();
            $default_voy.show();
            $default_dr.show();
            $lcl_bl.hide();
            $lcl_voy.hide();
            $lcl_dr.hide();
            $air_bl.hide();
            $air_voy.hide();
            $air_dr.hide();
        } else {
            $air_bl.show();
            $air_voy.show();
            $air_dr.show();
            $lcl_bl.hide();
            $lcl_voy.hide();
            $lcl_dr.hide();
            $default_bl.hide();
            $default_voy.hide();
            $default_dr.hide();
        }
    } else {
        $default_bl.attr("readonly", true);
        $default_bl.addClass("readonly");
        $default_voy.attr("readonly", true);
        $default_voy.addClass("readonly");
        $default_dr.attr("readonly", true);
        $default_dr.addClass("readonly");
        $cost_code.attr("readonly", true);
        $cost_code.addClass("readonly");
        $default_bl.show();
        $default_voy.show();
        $default_dr.show();
        $lcl_bl.hide();
        $lcl_voy.hide();
        $lcl_dr.hide();
        $air_bl.hide();
        $air_voy.hide();
        $air_dr.hide();
    }
}

function closeAccrual() {
    $("#add-accrual-container").hide(500, function () {
        hideAlternateMask();
        clearAccrual();
    });
}

function addAccrualSuccess(results) {
    if ($.trim(results) !== "") {
        loadResults(results)
        clearAccrual();
    }
}

function addAccrual() {
    if ($.trim($("#newVendorNumber").val()) === "") {
        $.prompt("Please enter Vendor", {
            callback: function () {
                $("#newVendorName").val("").callFocus();
            }
        });
    } else if ($.trim($("#newShipmentType").val()) === "") {
        $.prompt("Please select Shipment Type", {
            callback: function () {
                $("#newShipmentType").val("").callFocus();
            }
        });
    } else if ($.trim($("#newBlNumber").val()) === "" && $.trim($("#newVoyageNumber").val()) === "" && $.trim($("#newDockReceipt").val()) === "") {
        $.prompt("Please enter either BL Number or Voyage Number or Dock Receipt", {
            callback: function () {
                $("#newBlNumber").val("").callFocus();
                $("#lclBlNo").hide();
            }
        });
    } else if ($.trim($("#newAmount").val()) === "" || isNaN($("#newAmount").val())) {
        $.prompt("Please enter Amount", {
            callback: function () {
                $("#newAmount").val("").callFocus();
            }
        });
    } else if ($.trim($("#newCostCode").val()) === "") {
        $.prompt("Please enter Cost Code", {
            callback: function () {
                $("#newCostCode").val("").callFocus();
            }
        });
    } else if ($.trim($("#newGlAccount").val()) === "") {
        $.prompt("Please derive GL Account", {
            callback: function () {
                $("#newCostCode").val("").callFocus();
            }
        });
    }
    else {
        var params = "action=addAccrual";
        params += "&" + $("#add-accrual-container :input").serialize();
        params += "&rowsOnly=" + ($("#accruals>tbody").length > 0);
        params += "&totalRows=" + $("#totalRows").val();
        params += "&selectedRows=" + $("#selectedRows").val();
        params += "&totalPages=" + $("#totalPages").val();
        params += "&selectedPage=" + $("#selectedPage").val();
        params += "&from=" + $("#from").val();
        params += "&ediInvoiceNumber=" + $("#ediInvoiceNumber").val();
        params += "&newTerminal=" + $("#newTerminal").val();
        callServer(params, true, "addAccrualSuccess");
    }
}

function toggle() {
    $("#toggleDiv").slideToggle(function () {
        if ($("#toggled").val() === "true") {
            $("#toggled").val("false");
        } else {
            $("#toggled").val("true");
        }
        setResultHeight();
    });
}

function onchangeSearchBy() {
    if ($.trim($("#searchBy").val()) !== "") {
        $("#searchValue").removeAttr("readonly").removeClass("readonly").val("").callFocus();
    } else {
        $("#searchValue").attr("readonly", true).addClass("readonly").val("");
    }
}

function searchByFilterSuccess(results) {
    loadResults(results);
}

function searchByFilterError() {
    $("#searchVendorName").val("").callFocus();
}

function searchByFilter() {
    var vendorNumber = $.trim($("#searchVendorNumber").val());
    if (vendorNumber !== "" || $.trim($("#searchBy").val()) !== "") {
        var params = createParams("searchByFilter");
        callServer(params, true, "searchByFilterSuccess", "searchByFilterError");
    } else {
        $.prompt("Please enter Vendor or select Search By filter", {
            callback: function () {
                if ($("#toggleDiv").css("display") === "none") {
                    toggle();
                }
                $("#searchVendorName").val("").callFocus();
            }
        });
    }
}

function clearFilter() {
    $("#searchVendorName").val("");
    $("#searchVendorNameCheck").val("");
    $("#searchVendorNumber").val("");
    $("#openAccruals[value='true']").attr("checked", true);
    $("#ar").removeAttr("checked");
    $("#searchBy").val("");
    $("#hideAccruals").val("");
    onchangeSearchBy();
    $("#searchVendorName").callFocus();
}

function searchSuccess(results) {
    loadResults(results);
}

function search() {
    var params = createParams("");
    callServer(params, true, "searchSuccess");
}

function doSort(sortBy) {
    $("#sortBy").val(sortBy);
    if ($("#orderBy").val() === "desc") {
        $("#orderBy").val("asc");
    } else {
        $("#orderBy").val("desc");
    }
    search();
}

function gotoPage(selectedPage) {
    $("#selectedPage").val(selectedPage);
    search();
}

function gotoSelectedPage() {
    $("#selectedPage").val($("#selectedPageNo").val());
    search();
}

var updatedRow;
function updateAccrualSuccess(results) {
    if ($.trim(results) === "success") {
        $.prompt("Accrual is updated successfully", {
            callback: function () {
                initRows();
            }
        });
    } else if ($.trim(results) !== "") {
        if (updatedRow) {
            updatedRow.after(results);
        } else {
            $(results).prependTo("#accruals>tbody");
        }
        $("#result-header").html($("#result-banner").html()).show();
        $("#result-banner-row").remove();
        setResultHeight();
        $.prompt("Accrual is updated successfully", {
            callback: function () {
                initRows();
            }
        });
    }
}

function updateAccrual(row) {
    updatedRow = row;
    var params = "action=updateAccrual";
    params += "&rowsOnly=true";
    params += "&totalRows=" + $("#totalRows").val();
    params += "&selectedRows=" + $("#selectedRows").val();
    params += "&totalPages=" + $("#totalPages").val();
    params += "&selectedPage=" + $("#selectedPage").val();
    params += "&updateInvoiceNumber=" + row.find(".invoiceNumber").val();
    params += "&updateAmount=" + row.find(".amount").val();
    params += "&leaveBalance=" + row.find(".leavebalance").is(":checked");
    params += "&updateCostCode=" + row.find(".costCode").val();
    params += "&updateGlAccount=" + row.find(".glAccount").val();
    params += "&updateShipmentType=" + row.find(".shipmentType").val();
    params += "&updateBluescreenCostCode=" + row.find(".bluescreenCostCode").val();
    params += "&accrualId=" + row.find(".id").val();
    params += "&from=" + $("#from").val();
    params += "&ediInvoiceNumber=" + $("#ediInvoiceNumber").val();
    params += "&updateTerminal=" + row.find(".terminal").val();
    callServer(params, true, "updateAccrualSuccess");
}

function onchangeInvoice(ele) {
    $(ele).val($.trim($(ele).val()));
    var row = $(ele).parent().parent();
    if ($(ele).val() !== $.trim($("#invoiceNumber").val())) {
        if (row.find(".assign").is(":checked") && row.find(".status") === "IN PROGRESS") {
            row.find(".assign").removeAttr("checked");
        } else if (row.find(".dispute").is(":checked") && row.find(".status") === "DISPUTE") {
            row.find(".dispute").removeAttr("checked");
        }
    }
    updateAccrual(row);
}

function onchangeAmount(ele) {
    if ($.trim($(ele).val()) === "" || isNaN($(ele).val())) {
        $.prompt("Please enter Valid Amount", {
            callback: function () {
                $(ele).val("0.00").callFocus();
                var row = $(ele).parent().parent();
                row.find(".old-amount").val($(ele).val());
            }
        })
    } else {
        $(ele).val(Number($(ele).val()).toFixed(2));
        var row = $(ele).parent().parent();
        row.find(".old-amount").val($(ele).val());
        updateAccrual(row);
    }
}

function onchangeArAmount(ele) {
    if ($.trim($(ele).val()) === "" || isNaN($(ele).val())) {
        $.prompt("Please enter Valid Amount", {
            callback: function () {
                $(ele).val("0.00").callFocus();
                var row = $(ele).parent().parent();
                row.find(".old-amount").val($(ele).val());
                if (row.find(".assign").is(":checked") && !row.find(".assign").is(":disabled")) {
                    calculateAmount();
                }
            }
        });
    } else {
        $(ele).val(Number($(ele).val()).toFixed(2));
        var row = $(ele).parent().parent();
        row.find(".old-amount").val($(ele).val());
        if (row.find(".assign").is(":checked") && !row.find(".assign").is(":disabled")) {
            calculateAmount();
        }
    }
}

function onclickUpdateAccrual(ele) {
    var row = $(ele).parent().parent();
    updateAccrual(row);
}

function onclickAssign(ele) {
    if ($(ele).is(":checked")) {
        var row = $(ele).parent().parent();
        if (row.find(".transactionType").val() === "AC" && $.trim(row.find(".glAccount").val()) === "") {
            $.prompt("This accrual did not map to a GL account. Please update it", {
                callback: function () {
                    row.find(".costCode").callFocus();
                    row.find(".costCode").css("border-color", "red !important");
                    $(ele).removeAttr("checked");
                }
            });
        }
        row.find(".dispute").removeAttr("checked");
        row.find(".inactive").removeAttr("checked");
        row.find(".delete").removeAttr("checked");
    }
    calculateAmount();
}

function onclickDispute(ele) {
    if ($(ele).is(":checked")) {
        var row = $(ele).parent().parent();
        if (row.find(".assign").is(":checked")) {
            row.find(".assign").removeAttr("checked");
            calculateAmount();
        }
        row.find(".inactive").removeAttr("checked");
        row.find(".delete").removeAttr("checked");
    }
}

function onclickInactive(ele) {
    if ($(ele).is(":checked")) {
        var row = $(ele).parent().parent();
        if (row.find(".assign").is(":checked")) {
            row.find(".assign").removeAttr("checked");
            calculateAmount();
        }
        row.find(".dispute").removeAttr("checked");
        row.find(".delete").removeAttr("checked");
    }
}

function onclickDelete(ele) {
    if ($(ele).is(":checked")) {
        var row = $(ele).parent().parent();
        if (row.find(".assign").is(":checked")) {
            row.find(".assign").removeAttr("checked");
            calculateAmount();
        }
        row.find(".dispute").removeAttr("checked");
        row.find(".inactive").removeAttr("checked");
    }
}

function calculateAmount() {
    var assignedAmount = 0.00;
    $(".id").each(function () {
        var row = $(this).parent().parent();
        if (row.find(".assign").is(":checked") && !row.find(".assign").is(":disabled")) {
            assignedAmount += Number(row.find(".amount").val());
        }
    });
    $("#allocatedAmount").val(Number(assignedAmount).toFixed(2));
    $("#remainingAmount").val(Number($("#invoiceAmount").val() - assignedAmount).toFixed(2));
}

function showNotes(ele, dockReceipt) {
    var url = path + "/notes.do?";
    if ($.trim(dockReceipt) !== "") {
        url += "&moduleId=FILE";
        url += "&moduleRefId=" + dockReceipt.substr(2, dockReceipt.length);
    } else {
        var row = $(ele).parent().parent();
        if ($.trim(row.find(".transactionType").val()) === "AR") {
            url += "&moduleId=AR_INVOICE";
            var blNumber = row.find(".blNumber").val();
            var invoiceNumber = row.find(".invoiceNumber").val();
            url += "&moduleRefId=" + encodeURIComponent(row.find(".vendorNumber").val() + "-" + ($.trim(blNumber) !== "" ? blNumber : invoiceNumber));
        } else {
            url += "actions=showAccrualsNotes";
            url += "&moduleId=ACCRUALS";
            url += "&moduleRefId=" + row.find(".id").val();
            url += "&accrualsRefId=" + row.find(".id").val();
            url += "&costRefId=" + row.find(".costId").val();
            url += "&shipmentType=" + row.find(".shipmentType").val();
            if ($.trim(row.find(".status")) === "ASSIGNED" || $.trim(row.find(".status")) === "EDI ASSIGNED") {
                url += "&invoiceRefId=" + encodeURIComponent(row.find(".vendorNumber").val() + "-" + row.find(".invoiceNumber").val());
            }
        }
    }
    window.parent.showLightBox("Notes", url, 400, 800);
}

function showCostComment(comment) {
    $.prompt(comment)
}

function initEmail() {
    $("#body").jqte();
    $(".jqte").css("width", "700px");
    $(".jqte_Content").css("max-height", "160px");
    $(".jqte_Content").css("overflow-y", "auto");
    if ($("#loginName").length > 0 && !$("#loginName").hasClass("readonly")) {
        $("#loginName").initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=EMAIL&template=email&fieldIndex=1,2&",
            width: "480px",
            otherFields: "toAddress",
            resultsClass: "ac_results z-index",
            resultPosition: "absolute",
            scroll: true,
            scrollHeight: 250,
            append: false
        });
    }
}

function validateSave() {
    if ($.trim($("#vendorNumber").val()) === "") {
        $.prompt("Please enter Vendor", {
            callback: function () {
                $("#vendorName").val("").callFocus();
                $("#dispute").removeAttr("checked");
                $("#reject").removeAttr("checked");
            }
        });
        return false;
    } else if ($.trim($("#invoiceNumber").val()) === "") {
        $.prompt("Please enter Invoice Number", {
            callback: function () {
                $("#invoiceNumber").val("").callFocus();
                $("#dispute").removeAttr("checked");
                $("#reject").removeAttr("checked");
            }
        });
        return false;
    } else if ($.trim($("#invoiceDate").val()) === "") {
        $.prompt("Please enter Invoice Date", {
            callback: function () {
                $("#invoiceDate").val("").callFocus();
                $("#dispute").removeAttr("checked");
                $("#reject").removeAttr("checked");
            }
        });
        return false;
    } else {
        return true;
    }
}

function clearHiddenIds() {
    $("#hiddenIds :input").val("");
}

function setAssignedIds() {
    var assignedArIds = new Array();
    var assignedAccrualIds = new Array();
    $(".assign:checked").not(":disabled").each(function () {
        var row = $(this).parent().parent();
        if (row.find(".transactionType").val() === "AR") {
            assignedArIds.push($(this).val() + ":" + row.find(".amount").val());
        } else {
            assignedAccrualIds.push($(this).val());
        }
    });
    $("#assignedArIds").val(assignedArIds);
    $("#assignedAccrualIds").val(assignedAccrualIds);
}

function setDisputedIds() {
    var disputedArIds = new Array();
    var disputedAccrualIds = new Array();
    $(".dispute:checked").not(":disabled").each(function () {
        var row = $(this).parent().parent();
        if (row.find(".transactionType").val() === "AR") {
            disputedArIds.push($(this).val());
        } else {
            disputedAccrualIds.push($(this).val());
        }
    });
    $("#disputedArIds").val(disputedArIds);
    $("#disputedAccrualIds").val(disputedAccrualIds);
}

function setInactiveIds() {
    var inactiveAccrualIds = new Array();
    $(".inactive:checked").not(":disabled").each(function () {
        inactiveAccrualIds.push($(this).val());
    });
    $("#inactiveAccrualIds").val(inactiveAccrualIds);
}

function setDeleteIds() {
    var deleteAccrualIds = new Array();
    $(".delete:checked").not(":disabled").each(function () {
        deleteAccrualIds.push($(this).val());
    });
    $("#deleteAccrualIds").val(deleteAccrualIds);
}

function setActiveIds() {
    var activeAccrualIds = new Array();
    $(".status[value='INACTIVE']").each(function () {
        var row = $(this).parent().parent();
        if (!row.find(".inactive").is(":checked")
                && !row.find(".inactive").is(":disabled")
                && !row.find(".assign").is(":checked")
                && !row.find(".delete").is(":checked")) {
            activeAccrualIds.push(row.find(".inactive").val());
        }
    });
    $("#activeAccrualIds").val(activeAccrualIds);
}

function setUnDeleteActiveIds() {
    var undeleteAccrualIds = new Array();
    $(".status[value='DELETED']").each(function () {
        var row = $(this).parent().parent();
        if (!row.find(".delete").is(":checked")
                && !row.find(".delete").is(":disabled")
                && !row.find(".inactive").is(":checked")
                && !row.find(".assign").is(":checked")) {
            undeleteAccrualIds.push(row.find(".delete").val());
        }
    });
    $("#undeleteAccrualIds").val(undeleteAccrualIds);
}

function saveDisputeInvoiceSuccess(result) {
    if (result === "success") {
        if ($.trim($("#from").val()) === "EDI") {
            $("#message").html("Invoice is disputed successfully");
            goBack();
        } else {
            $("#invoiceNumber").val("");
            $("#invoiceNumberOld").val("");
            $("#status").val("");
            $("#invoiceAmount").val("0.00");
            $("#invoiceDate").val("");
            $("#dueDate").val("");
            $("#dispute").removeAttr("checked");
            $("#reject").removeAttr("checked");
            $("#results").html("");
            $("#email :input:not(:button)").val("");
            $("#message").html("Invoice is disputed successfully");
            searchByVendor();
        }
    }
}

function saveDisputeInvoiceError() {
    $("#dispute").removeAttr("checked");
}

function saveDisputeInvoice() {
    deactivateAutosave();
    clearHiddenIds();
    setAssignedIds();
    setDisputedIds();
    setInactiveIds();
    setDeleteIds();
    setActiveIds();
    setUnDeleteActiveIds();
    var params = "action=disputeInvoice";
    params += "&from=" + $("#from").val();
    params += "&ediInvoiceNumber=" + $("#ediInvoiceNumber").val();
    params += "&" + $("#invoice-container :input").serialize();
    params += "&" + $("#email :input:not(:button)").serialize();
    params += "&" + $("#hiddenIds :input").serialize();
    hideAlternateMask();
    $("#email").hide(500);
    callServer(params, true, "saveDisputeInvoiceSuccess", "saveDisputeInvoiceError");
}

function sendEmail() {
    if ($("#email").validate()) {
        saveDisputeInvoice();
    }
}

function showEmail() {
    showAlternateMask();
    $("#email").center().show(500);
}

function closeEmail() {
    hideAlternateMask();
    $("#email").hide(500);
    $("#dispute").removeAttr("checked");
}

function sendDisputeEmail() {
    var vendorName = $.trim($("#vendorName").val());
    var vendorNumber = $.trim($("#vendorNumber").val());
    var invoiceNumber = $.trim($("#invoiceNumber").val());
    var subject = "Invoice discrepancy:";
    subject += " Vendor: " + vendorName + "(" + vendorNumber + ")";
    subject += " - Invoice Number: " + invoiceNumber;
    subject += " - action required";
    var body = "Hi, <br/><br/> Attached invoice did not match our accounting records, please review and take corrective action.";
    body += " A copy of the invoice is attached.";
    body += "<br/><br/> Vendor : " + vendorName + "(" + vendorNumber + ")";
    body += "<br/> Invoice : " + invoiceNumber + "<br/><br/>";
    var items = "";
    $(".dispute:checked").not(":disabled").each(function () {
        var row = $(this).parent().parent();
        items += "<tr>";
        var dockReceipt = $.trim(row.find(".dockReceipt").val());
        var voyage = $.trim(row.find(".voyage").val());
        var amount = $.trim(row.find(".amount").val());
        items += "<td>" + dockReceipt + "</td>";
        items += "<td>" + voyage + "</td>";
        items += "<td>" + formatNumber(amount, "#,###.00") + "</td>";
        items += "</tr>";
    });
    if (items.length > 0) {
        body += " Following accruals do not match the billing of our vendor:<br/><br/>";
        body += "<div style='padding-left: 20px;'>";
        body += "<table cellpadding='0' cellspacing='0' width='60%' border='1'>";
        body += "<thead>";
        body += "<tr>";
        body += "<th>D/R</th>";
        body += "<th>Voyage</th>";
        body += "<th>Amount</th>";
        body += "</tr>";
        body += "</thead>";
        body += "<tbody>";
        body += items;
        body += "</tbody>";
        body += "</table>";
        body += "</div>";
        body += "<br/><br/>";
    }
    body += "Thank you for your assistance in resolving this matter.<br/>";
    $("#subject").val(subject);
    $("#ccAddress").val($("#userEmailAddress").val());
    $("#body").val(body);
    $(".jqte_Content").html(body);
    showEmail();
}

var invoiceUploadCount = 0;
function isInvoiceUploaded(success, error) {
    callServer({
        action: "isInvoiceUploaded",
        vendorNumber: $("#vendorNumber").val(),
        invoiceNumber: $.trim($("#invoiceNumber").val())
    }, true, success, error);
}

function disputeInvoiceUploadSuccess(result) {
    if (result === "true") {
        sendDisputeEmail();
    } else {
        if (invoiceUploadCount === 0) {
            uploadInvoice("isInvoiceUploaded('disputeInvoiceUploadSuccess','disputeInvoiceUploadError');this.close()")
            invoiceUploadCount++;
        } else {
            invoiceUploadCount--;
            $("#dispute").removeAttr("checked");
        }
    }
}

function disputeInvoiceUploadError() {
    $("#dispute").removeAttr("checked");
}

function disputeInvoice() {
    if ($("#dispute").is(":checked")) {
        if (validateSave()) {
            if (parseFloat($("#invoiceAmount").val()) !== 0.00 && parseFloat($("#remainingAmount").val()) === 0.00) {
                $.prompt("This invoice is fully applied, are you sure you want to dispute?", {
                    buttons: {
                        Yes: true,
                        No: false
                    },
                    callback: function (v, m, f) {
                        if (v) {
                            isInvoiceUploaded("disputeInvoiceUploadSuccess", "disputeInvoiceUploadError");
                        } else {
                            $("#dispute").removeAttr("checked");
                        }
                    }
                });
            } else {
                isInvoiceUploaded("disputeInvoiceUploadSuccess", "disputeInvoiceUploadError");
            }
        }
    }
}

function unDisputeInvoiceSuccess(result) {
    if (result === "success") {
        $("#message").html("Invoice is undisputed successfully");
        $("#ediInvoiceNumber").val($.trim($("#invoiceNumber").val()));
    }
}

function unDisputeInvoiceError() {
    $("#dispute").removeAttr("checked");
}

function unDisputeInvoice() {
    if ($.trim($("#vendorNumber").val()) === "") {
        $.prompt("Please enter Vendor", {
            callback: function () {
                $("#vendorName").val("").callFocus();
            }
        });
    }
    else if ($.trim($("#invoiceNumber").val()) === "") {
        $.prompt("Please enter Invoice Number", {
            callback: function () {
                $("#invoiceNumber").val("").callFocus();
            }
        });
    } else {
        if ($(".dispute:checked").not(":disabled").length > 0) {
            $("#dispute").attr("checked", true);
            $.prompt("One or more accruals marked as dispute. Please uncheck them.");
        } else {
            clearHiddenIds();
            setAssignedIds();
            setDisputedIds();
            setInactiveIds();
            setDeleteIds();
            setActiveIds();
            setUnDeleteActiveIds();
            var params = "action=unDisputeInvoice";
            params += "&from=" + $("#from").val();
            params += "&ediInvoiceNumber=" + $("#ediInvoiceNumber").val();
            params += "&" + $("#invoice-container :input").serialize();
            params += "&" + $("#email :input:not(:button)").serialize();
            params += "&" + $("#hiddenIds :input").serialize();
            callServer(params, true, "unDisputeInvoiceSuccess", "unDisputeInvoiceError");
        }
    }
}

function doDispute() {
    if ($("#dispute").is(":checked")) {
        deactivateAutosave();
    } else {
        unDisputeInvoice();
    }
}

function rejectInvoiceSuccess(result) {
    if (result === "success") {
        $("#message").html("Invoice is rejected successfully");
        if ($.trim($("#from").val()) === "EDI") {
            goBack();
        } else {
            $("#invoiceNumber").val("");
            $("#invoiceNumberOld").val("");
            $("#status").val("");
            $("#invoiceAmount").val("0.00");
            $("#invoiceDate").val("");
            $("#dueDate").val("");
            $("#dispute").removeAttr("checked");
            $("#reject").removeAttr("checked");
            $("#results").html("");
            $("#email :input:not(:button)").val("");
            searchByVendor();
        }
    }
}

function rejectInvoiceError() {
    showAlternateMask();
    $("#dispute").removeAttr("checked");
    deactivateAutosave();
    $("#comments").callFocus();
    $("#reject-comments").center().show(500);
}

function rejectInvoice() {
    if ($.trim($("#comments").val()) === "") {
        $.prompt("Please enter comments to reject the invoice", {
            callback: function () {
                $("#comments").val("").callFocus();
            }
        });
    } else if ($("#reject").is(":checked") && validateSave()) {
        var params = "action=rejectInvoice";
        params += "&" + $("#invoice-container :input").serialize();
        params += "&comments=" + $("#comments").val();
        hideAlternateMask();
        $("#reject-comments").hide(500);
        callServer(params, true, "rejectInvoiceSuccess", "rejectInvoiceError");
    } else {
        closeRejectComments();
    }
}

function unRejectInvoiceSuccess(result) {
    if (result === "success") {
        $("#message").html("Invoice is unrejected successfully");
    }
}

function unRejectInvoiceError() {
    $("#reject").attr("checked", true);
}

function unRejectInvoice() {
    var params = "action=unRejectInvoice";
    params += "&" + $("#invoice-container :input").serialize();
    hideAlternateMask();
    $("#reject-comments").hide(500);
    callServer(params, true, "unRejectInvoiceSuccess", "unRejectInvoiceError");
}

function showRejectComments() {
    showAlternateMask();
    $("#dispute").removeAttr("checked");
    deactivateAutosave();
    $("#comments").val("").callFocus();
    $("#reject-comments").center().show(500);
}

function closeRejectComments() {
    hideAlternateMask();
    $("#reject-comments").hide(500);
    $("#reject").removeAttr("checked");
    $("#dispute").removeAttr("checked");
}

function rejectInvoiceUploadSuccess(result) {
    if (result === "true") {
        showRejectComments();
    } else {
        if (invoiceUploadCount === 0) {
            uploadInvoice("isInvoiceUploaded('rejectInvoiceUploadSuccess','rejectInvoiceUploadError');this.close()")
            invoiceUploadCount++;
        } else {
            invoiceUploadCount--;
            $("#reject").removeAttr("checked");
        }
    }
}

function rejectInvoiceUploadError() {
    $("#reject").removeAttr("checked");
}

function doReject() {
    if (validateSave()) {
        if ($("#reject").is(":checked")) {
            $.prompt("Do you want to reject this invoice?", {
                buttons: {
                    Yes: true,
                    No: false
                },
                callback: function (v, m, f) {
                    if (v) {
                        $("#dispute").removeAttr("checked");
                        isInvoiceUploaded("rejectInvoiceUploadSuccess", "rejectInvoiceUploadError");
                    } else {
                        $("#reject").removeAttr("checked");
                    }
                }
            });
        } else {
            unRejectInvoice();
        }
    }
}

function saveInprogressInvoiceSuccess(result) {
    if (result === "success") {
        $("#message").html("Invoice is saved as In Progress successfully");
        $("#ediInvoiceNumber").val($.trim($("#invoiceNumber").val()));
    }
}

function saveInprogressInvoice() {
    if (validateSave()) {
        deactivateAutosave();
        clearHiddenIds();
        setAssignedIds();
        setDisputedIds();
        setInactiveIds();
        setDeleteIds();
        setActiveIds();
        setUnDeleteActiveIds();
        var params = "action=inprogressInvoice";
        params += "&from=" + $("#from").val();
        params += "&ediInvoiceNumber=" + $("#ediInvoiceNumber").val();
        params += "&" + $("#invoice-container :input").serialize();
        params += "&" + $("#email :input:not(:button)").serialize();
        params += "&" + $("#hiddenIds :input").serialize();
        callServer(params, true, "saveInprogressInvoiceSuccess");
    }
}

function postInvoiceSuccess(result) {
    if (result === "success") {
        $("#message").html("Invoice is posted to AP successfully");
        if ($.trim($("#from").val()) === "EDI" || $.trim($("#from").val()) === "SSMasters") {
            goBack();
        } else {
            $("#invoiceNumber").val("").callFocus();
            $("#invoiceNumberOld").val("");
            $("#status").val("");
            $("#invoiceAmount").val("0.00");
            $("#invoiceDate").val("");
            $("#dueDate").val("");
            $("#dispute").removeAttr("checked");
            $("#reject").removeAttr("checked");
            $("#results").html("");
            $("#email :input:not(:button)").val("");
            clearHiddenIds();
            searchByVendor();
        }
    }else{
       $.prompt("Invoice is posted to AP already. Please enter another invoice number");   
    }
}

function isAccrualsFullyMappedSuccess(result) {
    if (result === "") {
        var params = "action=postInvoice";
        params += "&from=" + $("#from").val();
        params += "&ediInvoiceNumber=" + $("#ediInvoiceNumber").val();
        params += "&" + $("#invoice-container :input").serialize();
        params += "&" + $("#email :input:not(:button)").serialize();
        params += "&" + $("#hiddenIds :input").serialize();
        callServer(params, true, "postInvoiceSuccess");
    } else {
        $.prompt("One or more accruals does not mapped to a GL account. Please update it", {
            callback: function () {
                var ids = result.split(",");
                $(ids).each(function (index, id) {
                    var row = $("#id-" + id).parent().parent();
                    if (index === 0) {
                        row.find(".costCode").callFocus();
                        row.find(".costCode").css("border-color", "red !important");
                    }
                });
            }
        });
    }
}

function isAccrualsFullyMapped() {
    var params = "action=isAccrualsFullyMapped";
    params += "&assignedAccrualIds=" + $("#assignedAccrualIds").val();
    callServer(params, true, "isAccrualsFullyMappedSuccess");
}

function postInvoice() {
    if (validateSave()) {
        deactivateAutosave();
        clearHiddenIds();
        setAssignedIds();
        setDeleteIds();
        setActiveIds();
        setUnDeleteActiveIds();
        if ($.trim($("#assignedAccrualIds").val()) !== "") {
            isAccrualsFullyMapped();
        } else {
            var params = "action=postInvoice";
            params += "&from=" + $("#from").val();
            params += "&ediInvoiceNumber=" + $("#ediInvoiceNumber").val();
            params += "&" + $("#invoice-container :input").serialize();
            params += "&" + $("#email :input:not(:button)").serialize();
            params += "&" + $("#hiddenIds :input").serialize();
            callServer(params, true, "postInvoiceSuccess");
        }
    }
}

function deleteOrInactivateAccrualsSuccess(result) {
    $("#message").html(result + " successfully");
    if ($.trim($("#from").val()) === "EDI" || $.trim($("#from").val()) === "SSMasters") {
        goBack();
    } else {
        $("#invoiceNumber").val("").callFocus();
        $("#invoiceNumberOld").val("");
        $("#status").val("");
        $("#invoiceAmount").val("0.00");
        $("#invoiceDate").val("");
        $("#dueDate").val("");
        $("#dispute").removeAttr("checked");
        $("#reject").removeAttr("checked");
        $("#results").html("");
        $("#email :input:not(:button)").val("");
        clearHiddenIds();
        searchByVendor();
    }
}

function save() {
    if ($("#reject").is(":checked")) {
        doReject();
    } else if ($("#dispute").is(":checked")) {
        disputeInvoice();
    } else if ($(".dispute:checked").not(":disabled").length > 0) {
        $("#dispute").attr("checked", true);
        disputeInvoice();
    } else {
        var activeCount = 0;
        var undeleteCount = 0;
        $(".status[value='INACTIVE']").each(function () {
            var row = $(this).parent().parent();
            if (!row.find(".inactive").is(":checked") && !row.find(".inactive").is(":disabled")) {
                activeCount = 1;
                return false;
            }
        });
        $(".status[value='DELETED']").each(function () {
            var row = $(this).parent().parent();
            if (!row.find(".delete").is(":checked") && !row.find(".delete").is(":disabled")) {
                undeleteCount = 1;
                return false;
            }
        });
        if ($.trim($("#invoiceNumber").val()) === ""
                && $(".assign:checked").not(":disabled").length <= 0 && $(".dispute:checked").not(":disabled").length <= 0
                && (activeCount > 0 || undeleteCount > 0
                        || $(".inactive:checked").not(":disabled").length > 0 || $(".delete:checked").not(":disabled").length > 0)) {
            clearHiddenIds();
            setInactiveIds();
            setDeleteIds();
            setActiveIds();
            setUnDeleteActiveIds();
            var params = "action=deleteOrInactivateAccruals";
            params += "&" + $("#hiddenIds :input").serialize();
            callServer(params, true, "deleteOrInactivateAccrualsSuccess");
        } else if (validateSave()) {
            if ($(".assign:checked").not(":disabled").length > 0) {
                if (parseFloat($("#remainingAmount").val()) !== 0.00) {
                    $.prompt("Invoice amount does not equals to amount of assigned accruals. Do you want to save anyway?", {
                        buttons: {
                            Yes: true,
                            No: false
                        },
                        callback: function (v, m, f) {
                            if (v) {
                                saveInprogressInvoice();
                            }
                        }
                    });
                } else {
                    postInvoice();
                }
            } else {
                saveInprogressInvoice();
            }
        }
    }
}

var autosaveId = 0;

function initAutosave() {
    if ($("#auto").is(":checked")) {
        if ($.trim($("#vendorNumber").val()) === "") {
            $.prompt("Please enter Vendor to turn on auto save option.", {
                callback: function () {
                    deactivateAutosave();
                    $("#vendorName").callFocus();
                }
            });
        } else if ($.trim($("#invoiceNumber").val()) === "") {
            $.prompt("Please enter Invoice Number to turn on auto save option.", {
                callback: function () {
                    deactivateAutosave();
                    $("#invoiceNumber").callFocus();
                }
            });
        } else if ($.trim($("#invoiceDate").val()) === "") {
            $.prompt("Please enter Invoice Date to turn on auto save option.", {
                callback: function () {
                    deactivateAutosave();
                    $("#invoiceDate").callFocus();
                }
            });
        } else if ($("#dispute").is(":checked")) {
            $.prompt("Cannot do auto save, Dispute feature is turned on.", {
                callback: function () {
                    deactivateAutosave();
                }
            });
        } else if ($(".dispute:checked").not(":disabled").length > 0) {
            $.prompt("Cannot do auto save, one or more dispute accrual is selected.", {
                callback: function () {
                    deactivateAutosave();
                }
            });
        } else {
            activateAutosave();
        }
    } else {
        $.prompt("Do you want to turn off the auto save option?", {
            buttons: {
                Yes: true,
                No: false
            },
            callback: function (v, m, f) {
                if (v) {
                    deactivateAutosave();
                } else {
                    $("#auto").attr("checked", true);
                }
            }
        });
    }
}

function activateAutosave() {
    deactivateAutosave();
    if ($.trim($("#vendorNumber").val()) !== "" && $.trim($("#invoiceNumber").val()) !== ""
            && $.trim($("#invoiceDate").val()) !== "" && $.trim($("#invoiceAmount").val()) !== "") {
        $("#auto").attr("checked", true);
        autosaveId = setTimeout("autosave()", 5 * 60 * 1000);
    }
}

function deactivateAutosave() {
    $("#auto").attr("checked", false);
    clearTimeout(autosaveId);
    autosaveId = 0;
}

function autosaveSuccess(result) {
    if (result === "success") {
        var date = new Date();
        var MM = (date.getMonth() < 10) ? '0' + date.getMonth() : date.getMonth();
        var dd = (date.getDay() < 10) ? '0' + date.getDay() : date.getDay();
        var yyyy = date.getFullYear();
        var hh = (date.getHours() < 10) ? '0' + date.getHours() : date.getHours();
        var mm = (date.getMinutes() < 10) ? '0' + date.getMinutes() : date.getMinutes();
        var ss = (date.getSeconds() < 10) ? '0' + date.getSeconds() : date.getSeconds();
        var time = MM + "/" + dd + "/" + yyyy + " " + hh + ":" + mm + ":" + ss;
        $("#autosave-notification").html("Last auto saved on " + time);
        $(".delete:checked").not(":disabled").each(function () {
            var row = $(this).parent().parent();
            row.find(".amount").val("0.00");
        });
    }
}

function autosave() {
    if (!$("#auto").is(":checked") || $.trim($("#vendorNumber").val()) === ""
            || $.trim($("#invoiceNumber").val()) === "" || $.trim($("#invoiceDate").val()) === ""
            || $.trim($("#invoiceAmount").val()) === "" || $("#dispute").is(":checked") || $(".dispute:checked").not(":disabled").length > 0) {
        deactivateAutosave();
    } else {
        clearHiddenIds();
        setAssignedIds();
        setDisputedIds();
        setInactiveIds();
        setDeleteIds();
        setActiveIds();
        setUnDeleteActiveIds();
        var params = "action=inprogressInvoice";
        params += "&from=" + $("#from").val();
        params += "&ediInvoiceNumber=" + $("#ediInvoiceNumber").val();
        params += "&" + $("#invoice-container :input").serialize();
        params += "&" + $("#email :input:not(:button)").serialize();
        params += "&" + $("#hiddenIds :input").serialize();
        callServer(params, true, "autosaveSuccess");
    }
}

function goBack() {
    showPreloading();
    if ($.trim($("#from").val()) === "EDI") {
        window.location = path + "/ediInvoice.do?action=search&message=" + $("#message").html() + $("#fromParams").val();
    } else if ($.trim($("#from").val()) === "SSMasters") {
        window.location = path + "/ssMastersApproved.do?action=searchSSMastersApproved&message=" + $("#message").html() + $("#fromParams").val();
    }else if ($.trim($("#from").val()) === "SSMastersLcl") {
        window.location = path + "/ssMastersApproved.do?action=searchLCLSSMastersApproved&message=" + $("#message").html() + $("#fromParams").val();
    }else if ($.trim($("#from").val()) === "SSMastersAll") {
        window.location = path + "/ssMastersApproved.do?action=searchALLSSMastersApproved&message=" + $("#message").html() + $("#fromParams").val();
    }
}

function showScanOrAttach(ele) {
    var row = $(ele).parent().parent();
    var vendorNumber = $.trim(row.find(".vendorNumber").val());
    var shipmetType = $.trim(row.find(".shipmentType").val());
    var invoiceNumber = "";
    if (row.find(".transactionType").val() === "AR") {
        invoiceNumber = ($.trim(row.find(".blNumber").val()) ? $.trim(row.find(".blNumber").val()) : $.trim(row.find(".invoiceNumber").val()))
    } else {
        invoiceNumber = $.trim(row.find(".invoiceNumber").val());
    }
    if (invoiceNumber === "") {
        $.prompt("Please enter Invoice Number", {
            callback: function () {
                row.find(".invoiceNumber").val("").callFocus();
            }
        });
    } else {
        var documentId = "";
        if(shipmetType === 'LCLE'){
            var voyage = $.trim(row.find(".voyage").val());
            voyage = voyage.split(/[- ]+/).pop();
            documentId = vendorNumber + "-" + voyage;
        }else{
            documentId = vendorNumber + "-" + invoiceNumber;
        }
        
        var title = "Upload Invoice";
        var url = path + "/scan.do?screenName=INVOICE&documentId=" + encodeURIComponent(documentId);
        showLightBox(title, url, 400, 800);
    }
}

function printPdf(fileName) {
    var title = "Accruals";
    var url = path + "/servlet/FileViewerServlet?fileName=" + fileName;
    window.parent.showLightBox(title, url);
}

function createPdf() {
    var vendorNumber = $.trim($("#searchVendorNumber").val());
    if (vendorNumber !== "" || $.trim($("#searchBy").val()) !== "") {
        $("#error").html("");
        setSelectedIds();
        var params = "action=createPdf";
        params += "&from=" + $("#from").val();
        params += "&vendorNumber=" + $.trim($("#vendorNumber").val());
        params += "&invoiceNumber=" + $.trim($("#invoiceNumber").val());
        params += "&ediInvoiceNumber=" + $("#ediInvoiceNumber").val();
        params += "&" + $("#toggleDiv :input").serialize();
        params += "&" + $("#hiddenIds :input").serialize();
        callServer(params, true, "printPdf");
    } else {
        $.prompt("Please enter Vendor or select Search By filter", {
            callback: function () {
                if ($("#toggleDiv").css("display") === "none") {
                    toggle();
                }
                $("#searchVendorName").val("").callFocus();
            }
        });
    }
}

function exportExcel(fileName) {
    var src = path + "/servlet/FileViewerServlet?fileName=" + fileName;
    if ($("#iframe").length > 0) {
        $("#iframe").attr("src", src);
    } else {
        $("<iframe/>", {
            name: "iframe",
            id: "iframe",
            src: src
        }).appendTo("body").hide();
    }
}

function createExcel() {
    var vendorNumber = $.trim($("#searchVendorNumber").val());
    if (vendorNumber !== "" || $.trim($("#searchBy").val()) !== "") {
        $("#error").html("");
        setSelectedIds();
        var params = "action=createExcel";
        params += "&from=" + $("#from").val();
        params += "&vendorNumber=" + $.trim($("#vendorNumber").val());
        params += "&invoiceNumber=" + $.trim($("#invoiceNumber").val());
        params += "&ediInvoiceNumber=" + $("#ediInvoiceNumber").val();
        params += "&" + $("#toggleDiv :input").serialize();
        params += "&" + $("#hiddenIds :input").serialize();
        callServer(params, true, "exportExcel");
    } else {
        $.prompt("Please enter Vendor or select Search By filter", {
            callback: function () {
                if ($("#toggleDiv").css("display") === "none") {
                    toggle();
                }
                $("#searchVendorName").val("").callFocus();
            }
        });
    }
}

$(document).ready(function () {
    closePreloading();
    $("[title != '']").not("link").tooltip();
    if ($.trim($("#vendorNumber").val()) !== "" && $.trim($("#invoiceNumber").val()) !== "") {
        $("#invoiceAmount").callFocus();
    } else if ($.trim($("#vendorNumber").val()) !== "") {
        $("#invoiceNumber").callFocus();
    } else {
        $("#vendorName").callFocus();
    }
    initVendor();
    initInvoice();
    initAddAccrual();
    initEmail();
    initInactivateAccruals();
    if ($(".result-container").length > 0) {
        $("#result-header").html($("#result-banner").html()).show();
        $("#result-banner-row").remove();
        setResultHeight();
        initRows();
    }
    $(window).resize(function () {
        window.parent.changeHeight();
        if ($(".result-container").length > 0) {
            setResultHeight();
        }
    });
    $("#accrualsForm").submit(function () {
        showPreloading();
    });
    Lightbox.initialize({
        color: 'black',
        dir: path + '/js/lightbox/images',
        moveDuration: 1,
        resizeDuration: 1
    });
    $(document).keypress(function (event) {
        if (event.target.id !== "jqi_state0_buttonOk"
                && event.target.id !== "jqi_state0_buttonYes" && event.target.id !== "jqi_state0_buttonNo"
                && event.target.id !== "invoiceNumber" && !$(event.target).hasClass("amount")) {
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode === 13) {
                event.preventDefault();
                searchByFilter();
            }
        }
    });
});