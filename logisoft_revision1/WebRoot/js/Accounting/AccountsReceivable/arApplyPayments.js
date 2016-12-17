var batchId = $("#batchId").val();
var currentCheck = $("#checkNumber").val();
var originalCheckTotal = Number($("#checkTotal").val());
var batchAmount = Number($("#batchAmount").val());
var originalBatchBalance = Number($("#batchBalance").val());
var textClass = "textlabelsBoldForTextBox";
var textClassReadOnly = "textlabelsBoldForTextBoxDisabledLook";
var initialBalance = originalBatchBalance + originalCheckTotal;

var changes = $("#changes").val();
var autosaveId = 0;

function showLightBox(title, url, height, width) {
    height = height || $(document).height() - 50;
    width = width || $(document).width() - 100;
    url += (url.indexOf("?") > -1 ? "&" : "?") + "TB_iframe&height=" + height + "&width=" + width;
    Lightbox.showPopUp(title, url, "sexylightbox", "");
}
function setCustomerOptions() {
    var url = path + "/autocompleter/action/getAutocompleterResults.jsp?query=";
    if ($("#disabled1").is(":checked") && $("#showAllAccounts1").is(":checked")) {
        url += "CUSTOMER_DISABLED";
    } else if ($("#showAllAccounts1").is(":checked")) {
        url += "CUSTOMER";
    } else if ($("#disabled1").is(":checked")) {
        url += "OPEN_CUSTOMER_DISABLED";
    } else {
        url += "OPEN_CUSTOMER";
    }
    url += "&template=customer&fieldIndex=1,2&";
    $("#customerName").setOptions({
        url: url
    });
    $("#customerName").val("").callFocus();
    $("#customerNameCheck").val("");
    $("#customerNumber").val("");
}

function setOtherCustomerOptions() {
    var url = path + "/autocompleter/action/getAutocompleterResults.jsp?query=";
    if ($("#disabled2").is(":checked") && $("#showAllAccounts2").is(":checked")) {
        url += "CUSTOMER_DISABLED";
    } else if ($("#showAllAccounts2").is(":checked")) {
        url += "CUSTOMER";
    } else if ($("#disabled2").is(":checked")) {
        url += "OPEN_CUSTOMER_DISABLED";
    } else {
        url += "OPEN_CUSTOMER";
    }
    url += "&template=customer&fieldIndex=1,2&";
    $("#otherCustomerName").setOptions({
        url: url
    });
    $("#otherCustomerName").val("").callFocus();
    $("#otherCustomerNameCheck").val("");
    $("#otherCustomerNumber").val("");
}

function initAutocompleteFields() {
    $("#customerName").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=OPEN_CUSTOMER&template=customer&fieldIndex=1,2&",
        width: "480px",
        otherFields: "customerNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 300,
        callback: "search();"
    });
    $("#disabled1,#showAllAccounts1").click(function () {
        setCustomerOptions();
    });
    $("#otherCustomerName").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=OPEN_CUSTOMER&template=customer&fieldIndex=1,2&",
        width: "480px",
        otherFields: "otherCustomerNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        showLeft: true,
        scroll: true,
        scrollHeight: 250,
        callback: "searchResultsByFilter();"
    });
    $("#disabled2,#showAllAccounts2").click(function () {
        setOtherCustomerOptions();
    });
}

$(document).ready(function () {
    initAutocompleteFields();
    initAddAccrual();
    $("#file").fileInput();
    $("#import").click(function () {
        importTemplate();
    });
    onChangeSearchBy("#searchBy");
    calculateSubTotal();
    if ($.trim($("#customerNumber").val()) !== "" && $.trim($("#checkNumber").val()) !== "") {
        activateAutosave();
    }
    $("#customerName,#otherCustomerName").keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode === 13) {
            event.preventDefault();
            search();
        }
    });
    $("#searchValue").keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode === 13) {
            event.preventDefault();
            searchByValue();
        }
    });
    $("#checkNumber").change(function () {
        if ($.trim($("#customerNumber").val()) !== "" && $.trim($("#checkNumber").val()) !== "") {
            activateAutosave();
        } else {
            deactivateAutosave();
        }
    });

    $("#customerName").change(function () {
        if ($.trim($("#customerNumber").val()) !== "" && $.trim($("#checkNumber").val()) !== "") {
            activateAutosave();
        } else {
            deactivateAutosave();
        }
    });

    $("#checkNumber").blur(function () {
        if ($.trim($("#customerNumber").val()) !== "" && $.trim($("#checkNumber").val()) !== "") {
            activateAutosave();
        } else {
            deactivateAutosave();
        }
    });

    $("#customerName").blur(function () {
        if ($.trim($("#customerNumber").val()) !== "" && $.trim($("#checkNumber").val()) !== "") {
            activateAutosave();
        } else {
            deactivateAutosave();
        }
    });

    $("#currentPageSize").keyup(function () {
        $(this).val($(this).val().replace(/[^0-9]+/g, ''));
    });

    $("#currentPageSize").change(function () {
        onchangeCurrentPageSize(this);
    });

    $("#search").click(function () {
        search();
    });

    $("#refresh").click(function () {
        refresh();
    });

    $("#goBack").click(function () {
        goBack();
    });

    $("#noCheck").click(function () {
        onclickNoCheck(this);
    });

    $("#checkTotal").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    $("#checkTotal").keyup(function (event) {
        validateAmount(event, this);
    });
    $("#checkTotal").change(function () {
        onchangeCheckTotal(this);
    });

    $("#onAccountApplied").click(function () {
        onclickOnAccountApplied(this);
    });
    $("#onAccountAmount").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    $("#onAccountAmount").keyup(function (event) {
        validateAmount(event, this);
    });
    $("#onAccountAmount").change(function () {
        onchangeOnAccount(this);
    });

    $("#prepaymentApplied").click(function () {
        onclickPrepaymentApplied(this);
    });
    $(".removePP").click(function () {
        $(this).parent().parent().remove();
        calculateAmount();
    });
    $(".addPP").click(function () {
        addPP();
    });
    $(".prepaymentAmount").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    $(".prepaymentAmount").keyup(function (event) {
        validateAmount(event, this);
    });
    $(".prepaymentAmount").change(function () {
        onChangePrepayment(this);
    });

    $("#chargeCodeApplied").click(function () {
        onclickChargeCodeApplied(this);
    });
    $(".addGL").click(function () {
        addGL();
    });
    $(".removeGL").click(function () {
        $(this).parent().parent().remove();
        calculateAmount();
    });
    $(".glAccountAmount").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    $(".glAccountAmount").keyup(function (event) {
        validateAmount(event, this);
    });
    $(".glAccountAmount").change(function () {
        onChangeGlAccount(this);
    });

    $("#addAccrual").click(function () {
        showAddAccrual();
    });

    $("#searchBy").change(function () {
        onChangeSearchBy(this);
    });
    $("#searchByFilter").click(function () {
        searchByFilter();
    });

    $("#reset").click(function () {
        $("#otherCustomerName").val("");
        $("#otherCustomerNumber").val("");
        $("#otherCustomerNameValid").val("");
        $("#showOtherConsignee").attr("checked", false);
        $(":radio[value=false]").attr("checked", true);
        $("#searchBy").val("").change();
    });

    $(".selectedIds").click(function () {
        onclickFullPay($(this));
    });

    $(".paidAmount").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    $(".paidAmount").keyup(function (event) {
        validateAmount(event, this);
    });
    $(".paidAmount").change(function () {
        onchangePaidAmount(this);
    });

    $(".adjustAmount").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    $(".adjustAmount").keyup(function (event) {
        validateAmount(event, this);
    });
    $(".adjustAmount").change(function () {
        adjust(this);
    });

    $("#save").click(function () {
        save();
    });

    $(".appliedGlAccount").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            if (event.keyCode === 9 || event.keyCode === 13) {
                //event.preventDefault();
                //moveToGLAmount(this);
            }
        }
    });

    $(".glAccountAmount").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            moveToGLNote(this);
        }
    });

    $(".glAccountNote").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            moveToGLAccount(event, this);
        }
    });

    var rowIndex = 0;
    $(".appliedGlAccount").each(function () {
        if (rowIndex > 0) {
            $("#appliedGlAccount" + rowIndex).initautocomplete({
                url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=GL_ACCOUNT&template=glAccount&fieldIndex=1&",
                width: "350px",
                resultsClass: "ac_results z-index",
                resultPosition: "fixed",
                scroll: true,
                scrollHeight: 200,
                callback: "moveToGLAmount('#appliedGlAccount" + rowIndex + "');"
            });
        }
        rowIndex++;
    });

    var drRow = 0;
    $(".docReceipt").each(function () {
        if (drRow > 0) {
            $("#selectType" + drRow).change(function () {
                var rowIndex = $(this).attr("id").replace("selectType", "");
                if ($(this).val() !== "select") {
                    initDrAutocomplete(rowIndex);
                    $("#docReceipt" + rowIndex).keydown(function (event) {
                        onDrKeyDown(event, this);
                    });
                    setDocReceiptOptions(rowIndex);
                } else {
                    $("#docReceipt" + rowIndex).val("").callFocus();
                }
            });
        }
        drRow++;
    });

    rowIndex = 1;
    $(".glAccount").each(function () {
        $("#glAccount" + rowIndex).initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=GL_ACCOUNT&template=glAccount&fieldIndex=1&",
            width: "350px",
            resultsClass: "ac_results z-index",
            resultPosition: "fixed",
            showLeft: true,
            scroll: true,
            scrollHeight: 100
        });
        rowIndex++;
        $(this).change(function () {
            changes = true;
        });
    });

    $(".docReceipt").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            if (event.keyCode === 9 || event.keyCode === 13) {
                event.preventDefault();
                moveToPPAmount(this);
            }
        }
    });

    $(".prepaymentAmount").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            moveToPPNote(event, this);
        }
    });

    $(".prepaymentNote").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            moveToPPDockReceipt(event, this);
        }
    });
    Lightbox.initialize({
        color: 'black',
        dir: path + '/js/lightbox/images',
        moveDuration: 1,
        resizeDuration: 1
    });
    $("[title != '']").not("link").tooltip();
});

function importTemplate() {
    if ($.trim($("#file").val()) === "") {
        $.prompt("Please choose template to import");
    } else if ($.trim($("#customerNumber").val()) === "") {
        $.prompt("Please select the customer");
    } else {
        $("#action").val("upload");
        $("#className").val("com.logiware.dwr.ArDwr");
        $("#methodName").val("importTemplateAndFindInvoices");
        $("#forward").val("/jsps/AccountsRecievable/arBatchImportPreview.jsp");
        $("#setRequest").val("true");
        $("#arBatchForm").fileupload({
            preloading: true,
            success: function (data) {
                if (data) {
                    var height = ($(document).height() * 50) / 100;
                    var width = ($(document).width() * 60) / 100;
                    var url = path + "?TB_html&height=" + height + "&width=" + width;
                    Lightbox.showPopUp("Imported Invoices - " + $("#customerNumber").val(), url, "sexylightbox", "", "", data);
                }
            }
        });
    }
}

function saveImportedInvoices() {
    if ($.trim($("#customerNumber").val()) === "") {
        $.prompt("Please select the customer");
    } else if ($.trim($("#checkNumber").val()) === "") {
        $.prompt("Please enter the check number");
    } else {
        var errors = checkErrors();
        if (errors.length === 0) {
            $("#autosave").attr("checked", false);
            createPayments();
            var importIndex = 0;
            var discardIndex = 0;
            $("#importedDiv").appendTo("#arBatchForm");
            Lightbox.close();
            $(".importMatches").each(function () {
                var row = $(this).parent().parent();
                if ($(this).val() === "true") {
                    row.find("td .importPaidAmount").attr("name", "importedTransactions[" + importIndex + "].paidAmount");
                    row.find("td .importTransactionId").attr("name", "importedTransactions[" + importIndex + "].transactionId");
                    row.find("td .importBalanceInProcess").attr("name", "importedTransactions[" + importIndex + "].balanceInProcess").val("0.00");
                    importIndex++;
                } else {
                    row.find("td .importTransactionType").attr("name", "discardedTransactions[" + discardIndex + "].transactionType");
                    row.find("td .importInvoiceOrBl").attr("name", "discardedTransactions[" + discardIndex + "].invoiceOrBl");
                    row.find("td .importPaidAmount").attr("name", "discardedTransactions[" + discardIndex + "].paidAmount");
                    row.find("td .importBalanceInProcess").attr("name", "discardedTransactions[" + discardIndex + "].balanceInProcess");
                    row.find("td .importComments").attr("name", "discardedTransactions[" + discardIndex + "].comments");
                    discardIndex++;
                }
            });
            $("#changes").val(false);
            $("#action").val("saveImportedInvoices");
            $("#arBatchForm").submit();
        } else {
            var ul = "<ul style='list-style:disc'>";
            for (var i = 0; i < errors.length; i++) {
                ul += "<li>" + errors[i] + "</li>";
            }
            ul += "</ul>";
            $.prompt(ul);
        }
    }
}

function moveToGLAmount(account) {
    $(account).parent().parent().find(".glAccountAmount").select().fadeIn().fadeOut().fadeIn();
}

function moveToGLNote(event, amount) {
    if (event.keyCode === 9 || event.keyCode === 13) {
        event.preventDefault();
        $(amount).parent().parent().find(".glAccountNote").select().fadeIn().fadeOut().fadeIn();
        onChangeGlAccount(amount);
    }
}

function moveToGLAccount(event, note) {
    if (event.keyCode === 9 || event.keyCode === 13) {
        if ($(note).parent().parent().prevAll().length === $("#glTable tr").length - 1) {
            addGL();
        }
        event.preventDefault();
        $(note).parent().parent().next().find(".appliedGlAccount").select().fadeIn().fadeOut().fadeIn();
    }
}

function moveToPPAmount(account) {
    $(account).parent().parent().find(".prepaymentAmount").select().fadeIn().fadeOut().fadeIn();
}

function moveToPPNote(event, amount) {
    if (event.keyCode === 9 || event.keyCode === 13) {
        event.preventDefault();
        $(amount).parent().parent().find(".prepaymentNote").select().fadeIn().fadeOut().fadeIn();
        onChangeGlAccount(amount);
    }
}

function moveToPPDockReceipt(event, note) {
    if (event.keyCode === 9 || event.keyCode === 13) {
        if ($(note).parent().parent().prevAll().length === $("#ppTable tr").length - 1) {
            addPP();
        }
        event.preventDefault();
        $(note).parent().parent().next().find(".docReceipt").select().fadeIn().fadeOut().fadeIn();
    }
}

function onchangeCurrentPageSize(ele) {
    if (isNaN($(ele).val()) || Number($(ele).val()) <= 0) {
        $.prompt("Please enter valid number");
        $(ele).val("100").focus();
    } else {
        $(ele).val(Number($(ele).val()));
    }
}

function search() {
    if ($.trim($("#customerNumber").val()) === "") {
        $.prompt("Please select the customer", {
            callback: function () {
                $("#customerNumber").focus().fadeIn().fadeOut().fadeIn();
            }
        });
    } else {
        $("#pageNo").val("1");
        $("#sortBy").val("");
        $("#orderBy").val("");
        $("#action").val("searchApplyPayments");
        $("#changes").val(changes);
        createPayments();
        $("#arBatchForm").submit();
    }
}

function searchByValue() {
    if ($("#searchBy").val() !== "") {
        if ($("#searchBy").val() !== "" && $.trim($("#searchValue").val()) === "") {
            $.prompt("You have selected search by filter. Please enter search value");
            $("#searchValue").focus();
        } else {
            createPayments();
            $("#pageNo").val("1");
            $("#sortBy").val("");
            $("#orderBy").val("");
            $("#changes").val(changes);
            $("#action").val("searchApplyPayments");
            $("#arBatchForm").submit();
        }
    }
}

function refresh() {
    if (changes === "true") {
        $.prompt("Unsaved changes, do you want to save the changes?", {
            buttons: {
                Yes: true,
                No: false
            },
            callback: function (v, m, f) {
                if (v) {
                    if ($.trim($("#customerNumber").val()) === "") {
                        $.prompt("Please select the customer");
                    } else if ($.trim($("#checkNumber").val()) === "") {
                        $.prompt("Please enter the check number");
                    } else {
                        var errors = checkErrors();
                        if (errors.length === 0) {
                            comparePayments();
                        } else {
                            var ul = "<ul style='list-style:disc'>";
                            for (var i = 0; i < errors.length; i++) {
                                ul += "<li>" + errors[i] + "</li>";
                            }
                            ul += "</ul>";
                            $.prompt(ul);
                        }
                    }
                } else {
                    $("#selectedBatchId").val($("#batchId").val());
                    $("#action").val("applyPayments");
                    $("#arBatchForm").submit();
                }
            }
        });
    } else {
        $("#selectedBatchId").val($("#batchId").val());
        $("#action").val("applyPayments");
        $("#arBatchForm").submit();
    }
}

function goBack() {
    if (changes === "true") {
        $.prompt("Unsaved changes, do you want to save the changes?", {
            buttons: {
                Yes: true,
                No: false
            },
            callback: function (v, m, f) {
                if (v) {
                    if ($.trim($("#customerNumber").val()) === "") {
                        $.prompt("Please select the customer");
                    } else if ($.trim($("#checkNumber").val()) === "") {
                        $.prompt("Please enter the check number");
                    } else {
                        var errors = checkErrors();
                        if (errors.length === 0) {
                            comparePayments();
                        } else {
                            var ul = "<ul style='list-style:disc'>";
                            for (var i = 0; i < errors.length; i++) {
                                ul += "<li>" + errors[i] + "</li>";
                            }
                            ul += "</ul>";
                            $.prompt(ul);
                        }
                    }
                } else {
                    $("#applypayments").remove();
                    $("#pageNo").val("1");
                    $("#sortBy").val("");
                    $("#orderBy").val("");
                    $("#action").val("goBack");
                    $("#arBatchForm").submit();
                }
            }
        });
    } else {
        $("#applypayments").remove();
        $("#pageNo").val("1");
        $("#sortBy").val("");
        $("#orderBy").val("");
        $("#action").val("goBack");
        $("#arBatchForm").submit();
    }
}

function scanOrAttach(batchId) {
    var checkNumber = $("#checkNumber").val();
    if ($.trim(checkNumber) === "") {
        $.prompt("Please enter check number");
    } else {
        var documentId = batchId + "-" + checkNumber;
        GB_show("Scan/Attach", rootPath + "/scan.do?screenName=AR BATCH&documentId=" + encodeURIComponent(documentId), 375, 700);
    }
}

function onclickNoCheck(ele) {
    if ($(ele).is(":checked")) {
        $("#checkNumber").val("NoCheck" + batchId).addClass(textClassReadOnly).attr("readonly", true);
        $("#checkTotal").val("0.00").addClass(textClassReadOnly).attr("readonly", true);
        if ($.trim($("#customerNumber").val()) !== "" && $.trim($("#checkNumber").val()) !== "") {
            activateAutosave();
        } else {
            deactivateAutosave();
        }
    } else {
        deactivateAutosave();
        $("#checkNumber").val("").removeClass(textClassReadOnly).addClass(textClass).removeAttr("readonly");
        $("#checkTotal").val("0.00").removeClass(textClassReadOnly).addClass(textClass).removeAttr("readonly");
    }
    var checkTotal = Number($("#checkTotal").val());
    if ($("#batchType").val() === "D") {
        var balance = Number(initialBalance - checkTotal).toFixed(2);
        $("#batchBalance").val(balance);
    }
    $("#checkTotal").val(checkTotal.toFixed(2));
    calculateAmount();
}

function onclickOnAccountApplied(ele) {
    if ($(ele).is(":checked")) {
        $(".onAccountDiv").show();
    } else {
        $(".onAccountDiv").hide();
        $("#onAccountAmount").val("0.00");
        calculateAmount();
    }
}

function onclickPrepaymentApplied(ele) {
    if ($(ele).is(":checked")) {
        $("#prepayments").slideDown();
        addPP();
    } else {
        $("#ppTable tr:gt(1)").remove();
        $("#prepayments").slideUp();
    }
    calculateAmount();
}

function onclickChargeCodeApplied(ele) {
    if ($(ele).is(":checked")) {
        $("#glAccounts").slideDown();
        addGL();
    } else {
        $("#glTable tr:gt(1)").remove();
        $("#glAccounts").slideUp();
    }
    calculateAmount();
}

function onDrKeyDown(event, ele) {
    if (event.shiftKey) {
        // No need to prevent anything but allow shift+tab
    } else {
        if (event.keyCode === 9 || event.keyCode === 13) {
            event.preventDefault();
            moveToPPAmount(ele);
        }
    }
}

function initDrAutocomplete(rowIndex) {
    $("#docReceipt" + rowIndex).initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=DOC_RECEIPT_FCL&template=bl&fieldIndex=1&",
        width: "150px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 200,
        callback: "moveToPPAmount('#docReceipt" + rowIndex + "');fillCustomerBillToParty('" + rowIndex + "');"
    });
}

function addPP() {
    var rowIndex = $("#ppTable tr").length - 1;
    var row = $(".defaultPP").clone().removeClass("defaultPP").show();
    row.find(".docReceipt").attr("name", "docReceipt" + rowIndex).attr("id", "docReceipt" + rowIndex).val("");
    row.find(".docReceipt").keydown(function (event) {
        onDrKeyDown(event, this);
    });
    row.find(".docReceiptCheck").attr("id", "docReceipt" + rowIndex + "Check").val("");
    row.find(".dropdown").attr("id", "selectType" + rowIndex).val("").attr("onchange", "emptyDocReceipt(" + rowIndex + ")");
    row.find(".prepaymentAmount").val("");
    row.find(".prepaymentAmount").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    row.find(".prepaymentAmount").keyup(function (event) {
        validateAmount(event, this);
    });
    row.find(".prepaymentAmount").change(function () {
        onChangePrepayment(this);
    });
    row.find(".prepaymentAmount").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            moveToPPNote(event, this);
        }
    });
    row.find(".prepaymentNote").val("");
    row.find(".prepaymentNote").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            moveToPPDockReceipt(event, this);
        }
    });
    row.find(".removePP").click(function () {
        $(this).parent().parent().remove();
        calculateAmount();
    });
    row.appendTo("#ppTable");
    $("#selectType" + rowIndex).change(function () {
        $("#docReceipt" + rowIndex).unbind();
        if ($(this).val() !== "select") {
            initDrAutocomplete(rowIndex);
            $("#docReceipt" + rowIndex).keydown(function (event) {
                onDrKeyDown(event, this);
            });
            setDocReceiptOptions(rowIndex);
        } else {
            $("#docReceipt" + rowIndex).val("").callFocus();
        }
    });
    row.find("#docReceipt" + rowIndex).focus();
}

function setDocReceiptOptions(rowIndex) {
    var url = path + "/autocompleter/action/getAutocompleterResults.jsp?query=";
    if ($("#selectType" + rowIndex).val() === "LCLI") {
        url += "DOC_RECEIPT_LCL_I";
    } else if ($("#selectType" + rowIndex).val() === "LCLE") {
        url += "DOC_RECEIPT_LCL_E";
    } else {
        url += "DOC_RECEIPT_FCL";
    }
    url += "&template=bl&fieldIndex=1&";
    $("#docReceipt" + rowIndex).setOptions({
        url: url
    });
    $("#docReceipt" + rowIndex).val("").callFocus();
    $("#docReceiptCheck" + rowIndex).val("");
}
function fillCustomerBillToParty(rowIndex) {
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.logiware.dwr.ArDwr",
            methodName: "billToPartyName",
            param1: $("#docReceipt" + rowIndex).val(),
            param2: $("#selectType" + rowIndex).val(),
            dataType: "json"
        },
        success: function (data) {
            if (data) {
                if ($.trim($('#customerName').attr('class')) !== 'textbox readonly ac_input') {
                    $("#customerNumber").val(data[0]);
                    $("#customerName").val(data[1]);
                }
            }
        }
    });
}
function emptyDocReceipt(rowIndex) {
    $("#docReceipt" + rowIndex).val("").callFocus();
}
function addGL() {
    var rowIndex = $("#glTable tr").length - 1;
    var row = $(".defaultGL").clone().removeClass("defaultGL").show();
    row.find(".appliedGlAccount").attr("name", "appliedGlAccount" + rowIndex).attr("id", "appliedGlAccount" + rowIndex).val("");
    row.find(".appliedGlAccount").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            if (event.keyCode === 9 || event.keyCode === 13) {
                //event.preventDefault();
                //moveToGLAmount(this);
            }
        }
    });
    row.find(".appliedGlAccountCheck").attr("id", "appliedGlAccount" + rowIndex + "Check").val("");
    row.find(".glAccountAmount").val("");
    row.find(".glAccountAmount").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    row.find(".glAccountAmount").keyup(function (event) {
        validateAmount(event, this);
    });
    row.find(".glAccountAmount").change(function () {
        onChangeGlAccount(this);
    });
    row.find(".glAccountAmount").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            moveToGLNote(event, this);
        }
    });
    row.find(".glAccountNote").val("");
    row.find(".glAccountNote").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            moveToGLAccount(event, this);
        }
    });
    row.find(".removeGL").click(function () {
        $(this).parent().parent().remove();
        calculateAmount();
    });
    row.appendTo("#glTable");
    $("#appliedGlAccount" + rowIndex).initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=GL_ACCOUNT&template=glAccount&fieldIndex=1&",
        width: "350px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 200,
        callback: "moveToGLAmount('#appliedGlAccount" + rowIndex + "');"
    });
    $("#appliedGlAccount" + rowIndex).focus();
}

function toggleFilter() {
    $("#filter_options").slideToggle();
    if ($("#filterOpen").val() === "true") {
        $("#filterOpen").val("false");
    } else {
        $("#filterOpen").val("true");
    }
}

function onChangeSearchBy(ele) {
    if ($(ele).val() === "") {
        $("#searchValue").val("");
        $("#searchValue").addClass(textClassReadOnly).attr("readonly", true);
    } else {
        $("#searchValue").removeClass(textClassReadOnly).addClass(textClass).removeAttr("readonly").focus();
    }
}

function searchByFilter() {
    if ($("#searchBy").val() !== "") {
        if ($("#searchBy").val() !== "" && $.trim($("#searchValue").val()) === "") {
            $.prompt("You have selected search by filter. Please enter search value");
            $("#searchValue").focus();
        } else {
            createPayments();
            $("#pageNo").val("1");
            $("#sortBy").val("");
            $("#orderBy").val("");
            $("#changes").val(changes);
            $("#action").val("searchApplyPayments");
            $("#arBatchForm").submit();
        }
    } else if ($.trim($("#customerNumber").val()) === "") {
        $.prompt("Please select the customer");
    } else {
        createPayments();
        $("#pageNo").val("1");
        $("#sortBy").val("");
        $("#orderBy").val("");
        $("#changes").val(changes);
        $("#action").val("searchApplyPayments");
        $("#arBatchForm").submit();
    }
}

function searchResultsByFilter() {
    if (event.keyCode === 13 || event.keyCode === 9 || event.button === 0) {
        searchByFilter();
    }
}

function gotoPage(pageNo) {
    createPayments();
    $("#pageNo").val(pageNo);
    $("#changes").val(changes);
    $("#action").val("searchApplyPayments");
    $("#arBatchForm").submit();
}

function gotoSelectedPage() {
    createPayments();
    $("#pageNo").val($("#selectedPageNo").val());
    $("#changes").val(changes);
    $("#action").val("searchApplyPayments");
    $("#arBatchForm").submit();
}

function doSort(sortBy, orderBy) {
    createPayments();
    $("#sortBy").val(sortBy);
    $("#orderBy").val(orderBy);
    $("#changes").val(changes);
    $("#action").val("searchApplyPayments");
    $("#arBatchForm").submit();
}

var changed = true;
function onChangeInvoice(ele) {
    if (changed) {
        changed = false;
        var invoiceNumber = $.trim($(ele).val());
        if (invoiceNumber === "") {
            $.prompt("Please enter invoice number", {
                callback: function () {
                    $(ele).val("").focus();
                }
            });
            changed = true;
        } else {
            $.prompt("Do you want to change the invoice number?", {
                buttons: {
                    Yes: true,
                    No: false
                },
                callback: function (v, m, f) {
                    if (v) {
                        var transactionId = $(ele).attr("id").replace("invoiceOrBl", "");
                        $.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.bc.accounting.AccrualsBC",
                                methodName: "updateAccrualInvoiceNumber",
                                param1: transactionId,
                                param2: invoiceNumber
                            },
                            preloading: true,
                            success: function (data) {
                                if (data === "success") {
                                    $.prompt("Invoice number is updated successfully");
                                } else {
                                    $.prompt(data, {
                                        callback: function () {
                                            $(ele).val("").focus();
                                        }
                                    });
                                }
                                changed = true;
                            }
                        });
                    }
                }
            });
        }
    }
}

function getFreightInvoice(transactionId, arInvoiceId) {
    var invoiceOrBl = $.trim($("#invoiceOrBl" + transactionId).val());
    var noticeNumber = $.trim($("#correctionNotice" + transactionId).val());
    var manifestFlag = $.trim($("#manifestFlag" + transactionId).val());
    var cutomerNumber = $.trim($("#customerNumber" + transactionId).val());
    var url = "";
    if (null !== manifestFlag && manifestFlag === "R") {
        url = rootPath + "/printConfig.do?screenName=BL&arInvoice=" + invoiceOrBl + "&arInvoiceId=" + arInvoiceId;
        GB_show("Print/Fax/Email", url, 400, 800);
    } else if (invoiceOrBl.indexOf("04-") > -1) {
        var fileNo = invoiceOrBl.substr(invoiceOrBl.indexOf("04-") + 3);
        url = rootPath + "/printConfig.do?screenName=BL&fileNo=" + fileNo + "&noticeNumber=" + noticeNumber;
        url += "&cutomerNumber=" + cutomerNumber + "&blId=" + invoiceOrBl;
        GB_show("Print/Fax/Email", url, 400, 800);
    }
}

function showInvoiceOrBlDetails(transactionId) {
    var invoiceOrBl = $.trim($("#invoiceOrBl" + transactionId).val());
    var cutomerNumber = $.trim($("#customerNumber" + transactionId).val());
    $.ajaxx({
        data: {
            className: "com.logiware.dwr.ArDwr",
            methodName: "showInvoiceOrBlDetails",
            param1: cutomerNumber,
            param2: invoiceOrBl,
            request: "true",
            forward: "/jsps/AccountsRecievable/InvoicePopUpTemplate.jsp"
        },
        preloading: true,
        success: function (data) {
            if (data) {
                showAlternateMask();
                $("<div style='width:800px;height:300px'></div>").html(data).addClass("popup").appendTo("body").center();
            }
        }
    });
}

function showApInvoiceDetails(transactionId) {
    var invoiceNumber = $.trim($("#invoiceOrBl" + transactionId).val());
    var vendorNumber = $.trim($("#customerNumber" + transactionId).val());
    var url = rootPath + "/apInquiry.do?action=showInvoiceDetails&vendorNumber=" + vendorNumber + "&invoiceNumber=" + encodeURIComponent(invoiceNumber) + "&transactionType=AP";
    GB_show("Invoice Details", url, 300, 700);
}

function showScanOrAttach(transactionId) {
    var invoiceOrBl = $.trim($("#invoiceOrBl" + transactionId).val());
    var cutomerNumber = $.trim($("#customerNumber" + transactionId).val());
    var documentId = cutomerNumber + "-" + invoiceOrBl;
    GB_show("Scan/Attach", rootPath + "/scan.do?screenName=INVOICE&documentId=" + encodeURIComponent(documentId), 375, 700);
}

function showInvoiceNotes(noteModuleId, noteRefId) {
    GB_show("Notes", rootPath + "/notes.do?moduleId=" + noteModuleId + "&moduleRefId=" + encodeURIComponent(noteRefId), 375, 700);
}

function inactivateAccruals(accrualId) {
    $.prompt("Do you want to inactivate the accrual?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v, m, f) {
            if (v) {
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.accounting.AccrualsBC",
                        methodName: "updateAccrualStatus",
                        param1: accrualId,
                        param2: "inactive",
                        dataType: "json"
                    },
                    preloading: true,
                    success: function (data) {
                        if (data) {
                            $.prompt("Accrual is Inactivated");
                            var custNo = $("#customerNumber" + accrualId);
                            var row = $(custNo).parent().parent();
                            row.find(".lock").show();
                            row.find(".unlock").hide();
                        }
                    }
                });
            }
        }
    });
}

function activateAccruals(accrualId) {
    $.prompt("Do you want to activate the accrual?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v, m, f) {
            if (v) {
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.accounting.AccrualsBC",
                        methodName: "updateAccrualStatus",
                        param1: accrualId,
                        param2: "Open",
                        dataType: "json"
                    },
                    preloading: true,
                    success: function (data) {
                        if (data) {
                            $.prompt("Accrual is activated");
                            var custNo = $("#customerNumber" + accrualId);
                            var row = $(custNo).parent().parent();
                            row.find(".lock").hide();
                            row.find(".unlock").show();
                        }
                    }
                });
            }
        }
    });
}

function showTransactionHistory(transactionId) {
    $.ajaxx({
        data: {
            className: "com.logiware.dwr.ArDwr",
            methodName: "showArTransactionHistory",
            param1: transactionId,
            request: "true",
            forward: "/jsps/AccountsRecievable/arTransactionHistory.jsp"
        },
        preloading: true,
        success: function (data) {
            if (data) {
                showAlternateMask();
                $("<div style='width:700px;height:300px'></div>").html(data).addClass("popup").appendTo("body").center();
            }
        }
    });
}

function callServer(data, preloading, success, error, url) {
    url = url || rootPath + "/accruals.do";
    ajaxCall(url, {
        data: data,
        preloading: preloading,
        success: success,
        error: error,
        async: false
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
        $("#newTerminal").val("").callFocus();
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
    $("#newCostCode").setOptions({
        extraParams: {
            param1: $("#newShipmentType").val()
        }
    });
}
function onDrCallBefore() {
    var shipment_type = $("#newShipmentType").val();
    var param;
    if (shipment_type.indexOf("LCLE") !== -1) {
        param = "E";
    } else if (shipment_type.indexOf("LCLI") !== -1) {
        param = "I";
    }
    $("#lclDrNo").setOptions({
        extraParams: {
            param1: param
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
        resultPosition: "absolute",
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

    var voy = $("#lclVoyageNo");
    var dr = $("#lclDrNo");
    if (voy.length > 0 && !voy.hasClass("readonly")) {
        voy.initautocomplete({
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

    if (dr.length > 0 && !dr.hasClass("readonly")) {
        dr.initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=LCL_FILES&template=bl&fieldIndex=1,2&",
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
    var vendorNumber = $.trim($("#customerNumber").val());
    if (vendorNumber === "") {
        $.prompt("Please select the customer", {
            callback: function () {
                $("#customerNumber").callFocus();
            }
        });
    } else {
        showAlternateMask();
        $("#add-accrual-container").center().show(500, function () {
            $("#newVendorName").val($("#customerName").val());
            $("#newVendorNameCheck").val($("#customerName").val());
            $("#newVendorNumber").val($("#customerNumber").val());
            $("#newInvoiceNumber").callFocus();
        });
    }
}

function clearAccrual() {
    $("#add-accrual-container :input:not(:button)").val("");
    $("#newVendorName").callFocus();
    toggleByModule();
}

function toggleByModule() {
    var selected_type = $("#newShipmentType").val();
    var default_bl = $("#newBlNumber");
    var default_voy = $("#newVoyageNumber");
    var default_dr = $("#newDockReceipt");
    var lcl_bl = $("#lclBlNo");
    var lcl_voy = $("#lclVoyageNo");
    var lcl_dr = $("#lclDrNo");
    var air_bl = $("#airBlNo");
    var air_voy = $("#airVoyageNo");
    var air_dr = $("#airDrNo");
    var cost_code = $("#newCostCode");
    default_bl.val("");
    default_voy.val("");
    default_dr.val("");
    if ($("#newShipmentType").val() !== "") {
        default_bl.attr("readonly", false);
        default_bl.removeClass("readonly");
        default_voy.attr("readonly", false);
        default_voy.removeClass("readonly");
        default_dr.attr("readonly", false);
        default_dr.removeClass("readonly");
        cost_code.attr("readonly", false);
        cost_code.removeClass("readonly");
        if (selected_type.indexOf("LCL") !== -1) {
            lcl_bl.show();
            lcl_voy.show();
            lcl_dr.show();
            default_bl.hide();
            default_voy.hide();
            default_dr.hide();
            air_bl.hide();
            air_voy.hide();
            air_dr.hide();
            lcl_bl.attr("readonly", true);
            lcl_bl.addClass("readonly");
        } else if (selected_type.indexOf("FCL") !== -1) {
            default_bl.show();
            default_voy.show();
            default_dr.show();
            lcl_bl.hide();
            lcl_voy.hide();
            lcl_dr.hide();
            air_bl.hide();
            air_voy.hide();
            air_dr.hide();
        } else {
            air_bl.show();
            air_voy.show();
            air_dr.show();
            lcl_bl.hide();
            lcl_voy.hide();
            lcl_dr.hide();
            default_bl.hide();
            default_voy.hide();
            default_dr.hide();
        }
    } else {
        default_bl.attr("readonly", true);
        default_bl.addClass("readonly");
        default_voy.attr("readonly", true);
        default_voy.addClass("readonly");
        default_dr.attr("readonly", true);
        default_dr.addClass("readonly");
        cost_code.attr("readonly", true);
        cost_code.addClass("readonly");
        default_bl.show();
        default_voy.show();
        default_dr.show();
        lcl_bl.hide();
        lcl_voy.hide();
        lcl_dr.hide();
        air_bl.hide();
        air_voy.hide();
        air_dr.hide();
    }
}

function closeAccrual() {
    $("#add-accrual-container").hide(500, function () {
        hideAlternateMask();
        clearAccrual();
    });
}

function addAccrualSuccess(result) {
    closeAccrual();
    var styleClass = "odd";
    if ($("#applypayments>tbody>tr:eq(0)").hasClass("odd")) {
        styleClass = "even";
    }
    var row = $("<tr class='blue inserted-row'>" + result + "</tr>").addClass(styleClass);
    row.find("td .selectedIds").click(function () {
        onclickFullPay($(this));
    });
    row.prependTo("#applypayments>tbody");
    calculateAmount();
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
    } else {
        var params = "action=addAccrual";
        params += "&" + $("#add-accrual-container :input").serialize();
        params += "&from=arBatch";
        callServer(params, true, "addAccrualSuccess");
    }
}

function deriveUpdateGlAccountSuccess(result) {
    if (result.indexOf("Suffix") > 0) {
        $.prompt("For Cost Code - " + $("#updateCostCode").val() + ", " + result, {
            callback: function () {
                deriveUpdateGlAccountError();
            }
        });
    } else {
        $("#updateGlAccount").val(result);
        $("#updateAmount").callFocus();
        $("#updateTerminal").val(result.substr(result.lastIndexOf("-") + 1)).attr("readonly", true).addClass("readonly");
    }
}

function deriveUpdateGlAccountError() {
    var suffix = $.trim($("#updateSuffix").val());
    if ((suffix === "B" || suffix === "L" || suffix === "D")) {
        $("#updateTerminal").val("").callFocus();
    } else {
        $("#updateCostCode").val("").callFocus();
    }
}

function deriveUpdateGlAccount() {
    var suffix = $.trim($("#updateSuffix").val());
    var terminal = $.trim($("#updateTerminal").val());
    if ((suffix === "B" || suffix === "L" || suffix === "D") && terminal === "") {
        $.prompt("Please enter the Termial Number", {
            callback: function () {
                $("#updateTerminal").val("").callFocus();
            }
        });
    } else {
        var account = $("#updateAccount").val();
        var shipmentType = $("#updateShipmentType").val();
        callServer({
            action: "deriveGlAccount",
            account: account,
            suffix: suffix,
            shipmentType: shipmentType,
            terminal: terminal
        }, true, "deriveUpdateGlAccountSuccess", "deriveUpdateGlAccountError");
    }
}

function showUpdateTerminal() {
    var suffix = $.trim($("#updateSuffix").val());
    if (suffix === "B" || suffix === "L" || suffix === "D") {
        $("#updateTerminal").removeAttr("readonly").removeAttr("tabindex").removeClass("readonly").val("").callFocus();
    } else {
        $("#updateTerminal").attr("readonly", true).attr("tabindex", "-1").addClass("readonly");
        deriveUpdateGlAccount();
    }
}

function setUpdateShipmentType() {
    $("#updateCostCode").setOptions({
        extraParams: {
            param1: $("#updateShipmentType").val()
        }
    });
}

function initUpdateAccrual() {
    $("#updateCostCode").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=COST_CODE&template=costCode&fieldIndex=1,2,3,5,6&",
        width: "420px",
        otherFields: "updateBluescreenCostCode^updateShipmentType^updateSuffix^updateAccount",
        callBefore: "setUpdateShipmentType()",
        callback: "showUpdateTerminal()",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 200
    });
    $("#updateTerminal").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=TERMINALS&template=chargeCode&fieldIndex=1&",
        minChars: "1",
        width: "250px",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 200
    });
    $("#updateAmount").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    $("#updateAmount").keyup(function (event) {
        validateAmount(event, this);
    });
    $("#updateAmount").change(function () {
        if (isNaN($(this).val())) {
            $(this).val("0.00");
        } else {
            $(this).val(Number($(this).val()).toFixed(2));
        }
    });
}

function showUpdateAccrualSuccess(result) {
    showAlternateMask();
    $("#update-accrual-container").html(result).center().show(500, function () {
        $("#updateAmount").callFocus();
    });
    initUpdateAccrual();
}

function showUpdateAccrual(accrualId) {
    accrualId = accrualId.replace(/[^0-9.]+/g, "");
    $("#updateAccrualId").val(accrualId);
    callServer({
        action: "showUpdateAccrual",
        accrualId: accrualId
    }, true, "showUpdateAccrualSuccess");
}

function closeUpdateAccrual() {
    $("#update-accrual-container").hide(500, function () {
        hideAlternateMask();
        isAccrualsFullyMapped($("#updateAccrualId").val());
        $("#update-accrual-container").html("");
    });
}

function updateAccrualSuccess(newAccrualId) {
    var oldAccrualId = $("#updateAccrualId").val();
    $.ajaxx({
        data: {
            className: "com.logiware.dwr.ArDwr",
            methodName: "addAccrual",
            param1: oldAccrualId,
            request: "true",
            forward: "/jsps/AccountsRecievable/addAccrualsTemplate.jsp"
        },
        preloading: true,
        success: function (data) {
            if (data) {
                closeUpdateAccrual();
                var custNo = $("#customerNumber" + oldAccrualId + "AC");
                var oldRow = $(custNo).parent().parent();
                var isChecked = oldRow.find("td .selectedIds").is(":checked");
                oldRow.html(data);
                oldRow.find("td .selectedIds").click(function () {
                    onclickFullPay($(this));
                });
                if (isChecked) {
                    onclickFullPay(oldRow.find("td .selectedIds").attr("checked", true));
                }
                calculateAmount();
                if (null !== newAccrualId && $.trim(newAccrualId) !== "" && newAccrualId !== "0") {
                    $.ajaxx({
                        data: {
                            className: "com.logiware.dwr.ArDwr",
                            methodName: "addAccrual",
                            param1: newAccrualId,
                            request: "true",
                            forward: "/jsps/AccountsRecievable/addAccrualsTemplate.jsp"
                        },
                        preloading: true,
                        success: function (result) {
                            if (result) {
                                var newRow = $("<tr class='blue inserted-row'>" + result + "</tr>");
                                newRow.find("td .selectedIds").click(function () {
                                    onclickFullPay($(this));
                                });
                                oldRow.after(newRow);
                                calculateAmount();
                            }
                        }
                    });
                }
            }
        }
    });
}

function updateAccrual() {
    if ($.trim($("#updateAmount").val()) === "" || isNaN($("#updateAmount").val())) {
        $.prompt("Please enter Amount", {
            callback: function () {
                $("#updateAmount").val("").callFocus();
            }
        });
    } else if ($.trim($("#updateCostCode").val()) === "") {
        $.prompt("Please enter Cost Code", {
            callback: function () {
                $("#updateCostCode").val("").callFocus();
            }
        });
    } else if ($.trim($("#updateGlAccount").val()) === "") {
        $.prompt("Please derive GL Account", {
            callback: function () {
                $("#updateCostCode").val("").callFocus();
            }
        });
    } else {
        var params = "action=updateAccrual";
        params += "&" + $("#update-accrual-container :input").serialize();
        params += "&from=arBatch";
        params += "&accrualId=" + $("#updateAccrualId").val();
        callServer(params, true, "updateAccrualSuccess");
    }
}

function isAccrualsFullyMapped(id) {
    var batchId = $("#batchId").val();
    var ele = document.getElementById("customerNumber" + id + "AC");
    $.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.accounting.AccrualsBC",
            methodName: "validateGLAccountForAccruals",
            param1: id
        },
        preloading: true,
        success: function (data) {
            if (data === "inValid") {
                var row = $(ele).parent().parent();
                if (row.find(".selectedIds").is(":checked")) {
                    row.find(".paidAmount").val("");
                    row.find(".balanceInProcess").val(row.find(".originalPaidAmount").val());
                    row.find(".originalPaidAmount").val("0.00");
                    if (row.find(".lock").attr("display") === "block") {
                        row.find(".lock").show().removeAttr("display");
                    }
                    if (row.find(".unlock").attr("display") === "block") {
                        row.find(".unlock").show().removeAttr("display");
                    }
                }
                calculateAmount();
                row.find(".selectedIds").removeAttr("checked");
                $.prompt("This accrual did not map to a GL account - please update it.");
                $.ajaxx({
                    data: {
                        className: "com.logiware.dwr.ArDwr",
                        methodName: "unlockTransaction",
                        param1: batchId,
                        param2: id
                    }
                });
            }
        }
    });
}

function onchangeCheckTotal(ele) {
    if (isNaN($(ele).val())) {
        $.prompt("Please enter valid amount");
        $(ele).val("0.00").focus();
    }
    var checkTotal = Number($(ele).val());
    if ($("#batchType").val() === "D") {
        var balance = Number(initialBalance - checkTotal).toFixed(2);
        $("#batchBalance").val(balance);
    }
    $(ele).val(checkTotal.toFixed(2));
    calculateAmount();
}

function onchangeOnAccount(ele) {
    if (isNaN($(ele).val())) {
        $.prompt("Please enter valid amount");
        $(ele).val("").focus();
    } else {
        $(ele).val(Number($(ele).val()).toFixed(2));
    }
    calculateAmount();
}

function onChangePrepayment(ele) {
    var prepaymentAmount = $(ele).val();
    if (isNaN(prepaymentAmount)) {
        $.prompt("Please enter valid amount");
        $(ele).val("0.00").focus();
    } else {
        $(ele).val(Number($(ele).val()).toFixed(2));
    }
    calculateAmount();
}

function onChangeGlAccount(ele) {
    var glAccountAmount = $(ele).val();
    if (isNaN(glAccountAmount)) {
        $.prompt("Please enter valid amount");
        $(ele).val("0.00").focus();
    } else {
        $(ele).val(Number($(ele).val()).toFixed(2));
    }
    calculateAmount();
}

function onclickFullPay(ele) {
    var transactionId = $(ele).val();
    var batchId = $("#batchId").val();
    var row = $(ele).parent().parent();
    var transactionType = row.find(".transactionType").val();
    var balanceInProcess = Number(row.find(".balanceInProcess").val());
    var originalPaidAmount = row.find(".originalPaidAmount").val();
    originalPaidAmount = $.trim(originalPaidAmount) === "" ? Number(0) : Number(originalPaidAmount);
    var paidAmount = row.find(".paidAmount").val();
    paidAmount = $.trim(paidAmount) === "" ? Number(0) : Number(paidAmount);
    if ($(ele).is(":checked")) {
        $.ajaxx({
            data: {
                className: "com.logiware.dwr.ArDwr",
                methodName: "lockTransaction",
                param1: batchId,
                param2: transactionId,
                param3: transactionType,
                request: "true"
            },
            preloading: true,
            success: function (data) {
                if (data === "available") {
                    if (transactionType === "AC") {
                        var id = transactionId.replace(/[^0-9.]+/g, "");
                        $.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.bc.accounting.AccrualsBC",
                                methodName: "validateGLAccountForAccruals",
                                param1: id
                            },
                            preloading: true,
                            success: function (result) {
                                if (result === "inValid") {
                                    showUpdateAccrual(id);
                                } else {
                                    var amount = Number(balanceInProcess + originalPaidAmount).toFixed(2);
                                    row.find(".paidAmount").val(amount);
                                    row.find(".originalPaidAmount").val(amount);
                                    row.find(".balanceInProcess").val("0.00");
                                    if (row.find(".lock").css("display") === "block") {
                                        row.find(".lock").hide().attr("display", "block");
                                    }
                                    if (row.find(".unlock").css("display") === "block") {
                                        row.find(".unlock").hide().attr("display", "block");
                                    }
                                    calculateAmount();
                                    calculateSubTotal();
                                }
                            }
                        });
                    } else if (transactionType === "AP") {
                        amount = Number(balanceInProcess + originalPaidAmount).toFixed(2);
                        row.find(".paidAmount").val(amount);
                        row.find(".originalPaidAmount").val(amount);
                        row.find(".balanceInProcess").val("0.00");
                        calculateAmount();
                    } else {
                        amount = Number(balanceInProcess + originalPaidAmount).toFixed(2);
                        row.find(".paidAmount").val(amount).addClass(textClassReadOnly).attr("readonly", true).attr("tabindex", "-1");
                        row.find(".originalPaidAmount").val(amount);
                        row.find(".balanceInProcess").val("0.00");
                        calculateAmount();
                    }
                } else {
                    $.prompt(data);
                    $(ele).removeAttr("checked");
                    calculateAmount();
                }
            }
        });
    } else {
        $.ajaxx({
            data: {
                className: "com.logiware.dwr.ArDwr",
                methodName: "unlockTransaction",
                param1: batchId,
                param2: transactionId
            },
            preloading: true,
            success: function () {
                if (transactionType === "AC") {
                    row.find(".paidAmount").val("");
                    row.find(".balanceInProcess").val(row.find(".originalPaidAmount").val());
                    row.find(".originalPaidAmount").val("0.00");
                    if (row.find(".lock").attr("display") === "block") {
                        row.find(".lock").show().removeAttr("display");
                    }
                    if (row.find(".unlock").attr("display") === "block") {
                        row.find(".unlock").show().removeAttr("display");
                    }
                    calculateSubTotal();
                } else if (transactionType === "AP") {
                    row.find(".paidAmount").val("");
                    row.find(".balanceInProcess").val(row.find(".originalPaidAmount").val());
                    row.find(".originalPaidAmount").val("0.00");
                } else {
                    row.find(".paidAmount").val("").removeClass(textClassReadOnly).addClass(textClass).removeAttr("readonly").removeAttr("tabindex");
                    row.find(".balanceInProcess").val(row.find(".originalPaidAmount").val());
                    row.find(".originalPaidAmount").val("0.00");
                }
                calculateAmount();
            }
        });
    }
}

function onchangePaidAmount(ele) {
    var row = $(ele).parent().parent();
    var balanceInProcess = Number(row.find(".balanceInProcess").val());
    var originalPaidAmount = row.find(".originalPaidAmount").val();
    originalPaidAmount = $.trim(originalPaidAmount) === "" ? Number(0) : Number(originalPaidAmount);
    if (isNaN($(ele).val())) {
        $.prompt("Please enter valid amount");
        $(ele).val("0.00").focus();
        row.find(".balanceInProcess").val(Number(balanceInProcess + originalPaidAmount).toFixed(2));
        row.find(".originalPaidAmount").val("0.00");
        row.find(".selectedIds").removeAttr("readonly");
    } else {
        var paidAmount = $(ele).val();
        var transactionId = row.find(".selectedIds").val();
        var batchId = $("#batchId").val();
        var transactionType = row.find(".transactionType").val();
        $.ajaxx({
            data: {
                className: "com.logiware.dwr.ArDwr",
                methodName: "lockTransaction",
                param1: batchId,
                param2: transactionId,
                param3: transactionType,
                request: "true"
            },
            preloading: true,
            success: function (data) {
                if (data === "available") {
                    paidAmount = $.trim(paidAmount) === "" ? Number(0) : Number(paidAmount).toFixed(2);
                    var amount = Number(balanceInProcess + originalPaidAmount - paidAmount).toFixed(2);
                    row.find(".balanceInProcess").val(amount);
                    if (parseFloat(paidAmount) === 0.00) {
                        $.ajaxx({
                            data: {
                                className: "com.logiware.dwr.ArDwr",
                                methodName: "unlockTransaction",
                                param1: batchId,
                                param2: transactionId
                            },
                            preloading: true,
                            success: function () {
                                var originalAdjustAmount = row.find(".originalAdjustAmount").val();
                                originalAdjustAmount = $.trim(originalAdjustAmount) === "" ? Number(0) : Number(originalAdjustAmount);
                                amount = Number(balanceInProcess + originalPaidAmount + originalAdjustAmount - paidAmount).toFixed(2);
                                row.find(".balanceInProcess").val(amount);
                                row.find(".paidAmount").val("");
                                row.find(".originalPaidAmount").val("0.00");
                                row.find(".selectedIds").show();
                                row.find(".originalAdjustAmount").val("0.00");
                            }
                        });
                    } else if (parseFloat(amount) === 0.00) {
                        row.find(".paidAmount").val(paidAmount).addClass(textClassReadOnly).attr("readonly", true).attr("tabindex", "-1");
                        row.find(".originalPaidAmount").val(paidAmount);
                        row.find(".selectedIds").show().attr("checked", true);
                    } else {
                        row.find(".paidAmount").val(paidAmount);
                        row.find(".originalPaidAmount").val(paidAmount);
                        row.find(".selectedIds").hide();
                    }
                } else {
                    $.prompt(data);
                    row.find(".paidAmount").val("");
                }
                calculateAmount();
            }
        });
    }
}

function adjust(ele) {
    changes = true;
    var row = $(ele).parent().parent();
    var balanceInProcess = Number(row.find(".balanceInProcess").val());
    var originalAdjustAmount = row.find(".originalAdjustAmount").val();
    originalAdjustAmount = $.trim(originalAdjustAmount) === "" ? Number(0) : Number(originalAdjustAmount);
    if (isNaN($(ele).val())) {
        $.prompt("Please enter valid amount");
        $(ele).val("0.00").focus();
        row.find(".balanceInProcess").val(Number(balanceInProcess + originalAdjustAmount).toFixed(2));
        row.find(".originalAdjustAmount").val("0.00");
    } else {
        var adjustAmount = $(ele).val();
        adjustAmount = $.trim(adjustAmount) === "" ? Number(0).toFixed(2) : Number(adjustAmount).toFixed(2);
        var amount = Number(balanceInProcess + originalAdjustAmount - adjustAmount).toFixed(2);
        row.find(".adjustAmount").val(adjustAmount);
        if (parseFloat(adjustAmount) === 0.00) {
            row.find(".balanceInProcess").val(Number(balanceInProcess + originalAdjustAmount).toFixed(2));
            row.find(".originalAdjustAmount").val("0.00");
        } else {
            row.find(".balanceInProcess").val(amount);
            row.find(".originalAdjustAmount").val(adjustAmount);
        }
    }
}

function calculateAmount() {
    changes = "true";
    var checkAmount = Number($("#checkTotal").val());
    var totalPaidAmount = 0;
    var onAccountAmount = $("#onAccountAmount").val();
    totalPaidAmount += isNaN(onAccountAmount) ? 0 : Number(onAccountAmount);
    $(".prepaymentAmount").each(function () {
        var prepaymentAmount = $(this).val();
        totalPaidAmount += isNaN(prepaymentAmount) ? 0 : Number(prepaymentAmount);
    });
    $(".glAccountAmount").each(function () {
        var glAccountAmount = $(this).val();
        totalPaidAmount += isNaN(glAccountAmount) ? 0 : Number(glAccountAmount);
    });
    $(".paidAmount").each(function () {
        var paidAmount = $(this).val();
        totalPaidAmount += isNaN(paidAmount) ? 0 : Number(paidAmount);
    });
    $("#checkApplied").val(Number(totalPaidAmount).toFixed(2));
    if ($("#batchType").val() === "D") {
        $("#checkBalance").val(Number(checkAmount - totalPaidAmount).toFixed(2));
    }
}

function activateDeactivateAutosave() {
    if ($("#autosave").is(":checked")) {
        if ($.trim($("#customerNumber").val()) === "") {
            deactivateAutosave();
            $.prompt("Please enter the customer to activate auto save option.");
        } else if ($.trim($("#checkNumber").val()) === "") {
            deactivateAutosave();
            $.prompt("Please enter the check number to activate auto save option.");
        } else {
            activateAutosave();
        }
    } else {
        $.prompt("Do you want to deactivate the auto save option?", {
            buttons: {
                Yes: true,
                No: false
            },
            callback: function (v, m, f) {
                if (v) {
                    deactivateAutosave();
                } else {
                    activateAutosave();
                }
            }
        });
    }
}

function activateAutosave() {
    deactivateAutosave();
    $("#autosave").attr("checked", true);
    autosaveId = setTimeout("autosave()", 5 * 60 * 1000);
}

function deactivateAutosave() {
    $("#autosave").attr("checked", false);
    clearTimeout(autosaveId);
    autosaveId = 0;
}

function autosave() {
    if ($.trim($("#customerNumber").val()) === "") {
        $("#autosave").attr("checked", false);
    } else if ($.trim($("#checkNumber").val()) === "") {
        $("#autosave").attr("checked", false);
    } else {
        if ($("#autosave").is(":checked")) {
            createPayments();
            $("#action").val("autoSaveApplyPayments");
            var params = $("#arBatchForm").serialize();
            $.post($("#arBatchForm").attr("action"), params, function (checkId) {
                $("#paymentCheckId").val(checkId);
                var date = new Date();
                var MM = (date.getMonth() < 10) ? '0' + date.getMonth() : date.getMonth();
                var dd = (date.getDay() < 10) ? '0' + date.getDay() : date.getDay();
                var yyyy = date.getFullYear();
                var hh = (date.getHours() < 10) ? '0' + date.getHours() : date.getHours();
                var mm = (date.getMinutes() < 10) ? '0' + date.getMinutes() : date.getMinutes();
                var ss = (date.getSeconds() < 10) ? '0' + date.getSeconds() : date.getSeconds();
                var time = MM + "/" + dd + "/" + yyyy + " " + hh + ":" + mm + ":" + ss;
                $(".autosavenotification").html("Last auto saved on " + time);
                if ($("#autosave").is(":checked")) {
                    activateAutosave();
                } else {
                    deactivateAutoSave();
                }
            });
        }
    }
}

function save() {
    if ($.trim($("#customerNumber").val()) === "") {
        $.prompt("Please select the customer");
    } else if ($.trim($("#checkNumber").val()) === "") {
        $.prompt("Please enter the check number");
    } else {
        var hasPartialPayments = checkPartialPayment();
        if (hasPartialPayments.length !== 0) {
            var txt = "There are short/overpaid invoices in the check, do you want to adjust?";
            $.prompt(txt, {
                buttons: {
                    Yes: true,
                    No: false
                },
                callback: function (v, m, f) {
                    if (v) {
                        makeAdjustment();
                    } else {
                        savePayments();
                    }
                }
            });
        } else {
            savePayments();
        }
    }
}

function checkPartialPayment() {
    var hasParitialPayment = [];
    $(".paidAmount").each(function () {
        var paidAmount = Number($(this).val());
        if (!isNaN(paidAmount) && parseFloat(paidAmount) !== 0.00) {
            var row = $(this).parent().parent();
            var balance = Number(row.find("td .balanceInProcess").val());
            var adjustmentAmount = $.trim(row.find("td .adjustAmount").val());
            if (parseFloat(balance) !== 0.00 && (adjustmentAmount === "" || parseFloat(adjustmentAmount) === 0.00)) {
                hasParitialPayment[hasParitialPayment.length] = balance;
            }
        }
    });
    return hasParitialPayment;
}

function makeAdjustment() {
    $(".paidAmount").each(function () {
        var paidAmount = Number($(this).val());
        if (!isNaN(paidAmount) && parseFloat(paidAmount) !== 0.00) {
            var row = $(this).parent().parent();
            var balance = Number(row.find(".balanceInProcess").val());
            var adjustmentAmount = $.trim(row.find("td .adjustAmount").val());
            if (parseFloat(balance) !== 0.00
                    && (isNaN(adjustmentAmount) || parseFloat(adjustmentAmount) === 0.00 || adjustmentAmount === "")) {
                changes = true;
                var balanceInProcess = Number(row.find(".balanceInProcess").val());
                var originalAdjustAmount = row.find(".originalAdjustAmount").val();
                originalAdjustAmount = $.trim(originalAdjustAmount) === "" ? Number(0) : Number(originalAdjustAmount);
                row.find("td .adjustAmount").val(balance.toFixed(2));
                row.find(".originalAdjustAmount").val(balance.toFixed(2));
                var amount = Number(balanceInProcess + originalAdjustAmount - balance).toFixed(2);
                row.find(".balanceInProcess").val(amount);
            }
        }
    });
}

function savePayments() {
    var errors = checkErrors();
    if (errors.length === 0) {
        comparePayments();
    } else {
        var ul = "<ul style='list-style:disc'>";
        for (var i = 0; i < errors.length; i++) {
            ul += "<li>" + errors[i] + "</li>";
        }
        ul += "</ul>";
        $.prompt(ul);
    }
}

function checkErrors() {
    var error = [];
    if ($("#onAccountApplied").is(":checked")
            && (($.trim($("#onAccountAmount").val()) === "") || $.trim($("#onAccountAmount").val()) === "0.00")) {
        error[error.length] = "Apply On Account is selected. Please enter the on account amount";
    }
    if ($("#prepaymentApplied").is(":checked")) {
        var index = 0;
        $(".prepaymentAmount").each(function () {
            if (index > 0) {
                var prepaymentAmount = Number($(this).val()).toFixed(2);
                if (!isNaN(prepaymentAmount) && parseFloat(prepaymentAmount) !== 0.00) {
                    var row = $(this).parent().parent();
                    var docReceiptMissing = false;
                    var noteMissing = false;
                    var docReceipt = row.find("td .docReceipt").val();
                    var missing = "Please enter";
                    if ($.trim(docReceipt) === "") {
                        docReceiptMissing = true;
                        missing += " dock receipt";
                    }
                    var note = row.find("td .prepaymentNote").val();
                    if ($.trim(note) === "") {
                        noteMissing = true;
                        if (docReceiptMissing) {
                            missing += " and notes for amount=" + prepaymentAmount;
                        } else {
                            missing += " notes for dock receipt " + docReceipt + " and amount " + prepaymentAmount;
                        }
                    }
                    if (docReceiptMissing || noteMissing) {
                        error[error.length] = missing;
                    }
                }
            }
            index++;
        });
    }
    if ($("#chargeCodeApplied").is(":checked")) {
        index = 0;
        $(".glAccountAmount").each(function () {
            if (index > 0) {
                var glAccountAmount = Number($(this).val()).toFixed(2);
                if (!isNaN(glAccountAmount) && parseFloat(glAccountAmount) !== 0.00) {
                    var row = $(this).parent().parent();
                    var glAccountMissing = false;
                    var noteMissing = false;
                    var glAccount = row.find("td .appliedGlAccount").val();
                    var missing = "Please enter";
                    if ($.trim(glAccount) === "") {
                        glAccountMissing = true;
                        missing += " GL account";
                    }
                    var note = row.find("td .glAccountNote").val();
                    if ($.trim(note) === "") {
                        noteMissing = true;
                        if (glAccountMissing) {
                            missing += " and notes for amount=" + glAccountAmount;
                        } else {
                            missing += " notes for GL account " + glAccount + " and amount " + glAccountAmount;
                        }
                    }
                    if (glAccountMissing || noteMissing) {
                        error[error.length] = missing;
                    }
                }
            }
            index++;
        });
    }
    return error;
}

function comparePayments() {
    var params = new Object();

    if ($("#onAccountApplied").is(":checked")
            && $.trim($("#onAccountAmount").val()) !== "" && $.trim($("#onAccountAmount").val()) !== "0.00") {
        params.onAccount = formatNumber(Number($("#onAccountAmount").val()).toFixed(2));
    }

    if ($("#prepaymentApplied").is(":checked")) {
        var index = 0;
        $(".prepaymentAmount").each(function () {
            var prepaymentAmount = Number($(this).val()).toFixed(2);
            if (!isNaN(prepaymentAmount) && parseFloat(prepaymentAmount) !== 0.00) {
                var row = $(this).parent().parent();
                params["prepayments[" + index + "].paidAmount"] = formatNumber($(this).val());
                params["prepayments[" + index + "].docReceipt"] = row.find("td .docReceipt").val();
                params["prepayments[" + index + "].notes"] = row.find("td .prepaymentNote").val();
                index++;
            }
        });
    }

    if ($("#chargeCodeApplied").is(":checked")) {
        var index = 0;
        $(".glAccountAmount").each(function () {
            var glAccountAmount = Number($(this).val()).toFixed(2);
            if (!isNaN(glAccountAmount) && parseFloat(glAccountAmount) !== 0.00) {
                var row = $(this).parent().parent();
                params["chargeCodes[" + index + "].paidAmount"] = formatNumber($(this).val());
                params["chargeCodes[" + index + "].glAccount"] = row.find("td .appliedGlAccount").val();
                params["chargeCodes[" + index + "].notes"] = row.find("td .glAccountNote").val();
                index++;
            }
        });
    }

    var index = 0;
    $(".paidAmount").each(function () {
        var paidAmount = Number($(this).val());
        if (!isNaN(paidAmount) && parseFloat(paidAmount) !== 0.00) {
            var row = $(this).parent().parent();
            params["transactions[" + index + "].customerName"] = row.find("td .customerName").val();
            params["transactions[" + index + "].customerNumber"] = row.find("td .customerNumber").val();
            params["transactions[" + index + "].invoiceOrBl"] = row.find("td .invoiceOrBl").val();
            params["transactions[" + index + "].transactionAmount"] = formatNumber(row.find("td .transactionAmount").val());
            params["transactions[" + index + "].balanceInProcess"] = formatNumber(Number(row.find("td .balanceInProcess").val()).toFixed(2));
            params["transactions[" + index + "].paidAmount"] = formatNumber($(this).val());
            params["transactions[" + index + "].adjustAmount"] = formatNumber(row.find("td .adjustAmount").val());
            params["transactions[" + index + "].glAccount"] = row.find("td .glAccount").val();
            params["transactions[" + index + "].transactionType"] = row.find("td .transactionType").val();
            params["transactions[" + index + "].transactionId"] = row.find("td .transactionId").val();
            index++;
        }
    });
    params.batchId = $("#batchId").val();
    params.checkId = $("#paymentCheckId").val();
    params.checkNumber = $("#checkNumber").val();
    params.customerNumber = $("#customerNumber").val();
    params.checkTotalAmount = formatNumber($("#checkTotal").val());
    params.appliedAmount = formatNumber($("#checkApplied").val());
    params.checkBalance = formatNumber($("#checkBalance").val());
    params.className = "com.logiware.dwr.ArDwr";
    params.methodName = "comparePayments";
    params.formName = "com.logiware.bean.ComparePaymentsBean";
    params.request = "true";
    params.forward = "/jsps/AccountsRecievable/comparePayments.jsp";
    $.ajaxx({
        data: params,
        preloading: true,
        success: function (data) {
            if (data) {
                showAlternateMask();
                $("<div style='width:96%;height:60%'></div>").html(data).addClass("popup").appendTo("body").center();
            }
        }
    });
}

function saveAfterCompare() {
    $("#autosave").attr("checked", false);
    closePopUpDiv();
    createPayments();
    $("#changes").val(false);
    $("#action").val("saveApplyPayments");
    $("#arBatchForm").submit();
}

function createPayments() {
    var index = 0;
    $(".prepaymentAmount").each(function () {
        var row = $(this).parent().parent();
        var prepaymentAmount = Number($(this).val()).toFixed(2);
        var docReceipt = row.find("td .docReceipt").val();
        var prepaymentNote = row.find("td .prepaymentNote").val();
        if (!isNaN(prepaymentAmount) && parseFloat(prepaymentAmount) !== 0.00
                && $.trim(docReceipt) !== "" && $.trim(prepaymentNote) !== "") {
            $(this).attr("name", "appliedPrepayments[" + index + "].paidAmount");
            row.find("td .docReceipt").attr("name", "appliedPrepayments[" + index + "].docReceipt");
            row.find("td .prepaymentNote").attr("name", "appliedPrepayments[" + index + "].notes");
            index++;
        }
    });
    index = 0;
    $(".glAccountAmount").each(function () {
        var row = $(this).parent().parent();
        var glAccountAmount = Number($(this).val()).toFixed(2);
        var appliedGlAccount = row.find("td .appliedGlAccount").val();
        var glAccountNote = row.find("td .glAccountNote").val();
        if (!isNaN(glAccountAmount) && parseFloat(glAccountAmount) !== 0.00
                && $.trim(appliedGlAccount) !== "" && $.trim(glAccountNote) !== "") {
            $(this).attr("name", "appliedGLAccounts[" + index + "].paidAmount");
            row.find("td .appliedGlAccount").attr("name", "appliedGLAccounts[" + index + "].glAccount");
            row.find("td .glAccountNote").attr("name", "appliedGLAccounts[" + index + "].notes");
            index++;
        }
    });
    index = 0;
    $(".paidAmount").each(function () {
        var paidAmount = Number($(this).val());
        if (!isNaN(paidAmount) && parseFloat(paidAmount) !== 0.00) {
            $(this).attr("name", "appliedTransactions[" + index + "].paidAmount");
            var row = $(this).parent().parent();
            row.find("td .transactionId").attr("name", "appliedTransactions[" + index + "].transactionId");
            row.find("td .adjustAmount").attr("name", "appliedTransactions[" + index + "].adjustAmount");
            row.find("td .glAccount").attr("name", "appliedTransactions[" + index + "].glAccount");
            row.find("td .balanceInProcess").attr("name", "appliedTransactions[" + index + "].balanceInProcess");
            index++;
        }
    });
}

function formatNumber(string) {
    if (string.length > 0) {
        if (string.indexOf("(") > -1) {
            string = "-" + string;
        }
        string = string.replace(/[^-0-9.]+/g, "");
        var number = string.split(".");
        var beforeDot = number[0];
        var afterDot = '.' + (number.length > 1 ? number[1] : "00");
        var regex = /(\d+)(\d{3})/;
        while (regex.test(beforeDot)) {
            beforeDot = beforeDot.replace(regex, "$1" + "," + "$2");
        }
        return beforeDot + afterDot;
    } else {
        return "";
    }
}

function calculateSubTotal() {
    if ($("#subTotal").length > 0) {
        var subTotal = 0;
        var searchValue = $.trim($("#searchValue").val());
        $(".selectedIds").each(function () {
            var row = $(this).parent().parent();
            var transactionType = row.find(".transactionType").val();
            var invoiceOrBl = $.trim(row.find(".invoiceOrBl").val());
            if ($(this).is(":checked") && transactionType === "AC" && searchValue === invoiceOrBl) {
                subTotal += Number(row.find(".paidAmount").val());
            }
        });
        $("#subTotal").val(Number(subTotal).toFixed(2));
    }
}

function showConsignee() {
    var accountType = "NoConsignee";
    if (document.getElementById("showConsignee").checked) {
        accountType = "Consignee";
    }
    return "&accountType=" + accountType;
}

function showOtherConsignee() {
    var accountType = "NoConsignee";
    if (document.getElementById("showOtherConsignee").checked) {
        accountType = "Consignee";
    }
    return "&accountType=" + accountType;
}

var otherAutocompleterUrl = rootPath + "/servlet/AutoCompleterServlet?action=Vendor&textFieldId=otherCustomerName";

function changeDisputeIconColor(action, id, invoice) {
    if (action === "disputed") {
        $(".message").html("Invoice is disputed successfully").removeClass("message").addClass("error");
        $("#dispute" + id).attr({
            "src": rootPath + "/images/icons/alphabets/d_red.png"
        }).attr("onclick", "").unbind("click").click(function () {
            undisputeInvoice(id, invoice);
        }).data("tipText", "Undispute Invoice");
    } else {
        $(".error").html("Invoice is undisputed successfully").removeClass("error").addClass("message");
        $("#dispute" + id).attr({
            "src": rootPath + "/images/icons/alphabets/d_green.png"
        }).attr("onclick", "").unbind("click").click(function () {
            disputeInvoice(id, invoice);
        }).data("tipText", "Dispute Invoice");
    }
}

function disputeInvoice(id, invoice) {
    $.prompt("Do you want to dispute the invoice " + invoice + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v) {
            if (v) {
                var url = rootPath + "/jsps/common/email.jsp";
                url += "?subject=Disputed AR invoice " + encodeURIComponent(invoice);
                url += "&ccAddress=" + encodeURIComponent($("#userEmailAddress").val());
                url += "&id=" + id;
                url += "&invoice=" + encodeURIComponent(invoice);
                url += "&action=" + encodeURIComponent("arInquiry.do?action=disputeInvoice&id=" + id);
                showLightBox("Dispute Invoice " + invoice, url, 400, 800);
            }
        }
    });
}

function undisputeInvoice(id, invoice) {
    $.prompt("Do you want to get the invoice " + invoice + " undisputed?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v) {
            if (v) {
                $.ajaxx({
                    url: rootPath + "/arInquiry.do",
                    data: {
                        action: "undisputeInvoice",
                        id: id
                    },
                    preloading: true,
                    success: function (data) {
                        if (data === "undisputed") {
                            changeDisputeIconColor("undisputed", id, invoice);
                        }
                    }
                });
            }
        }
    });
}

function checkDisputeStatus(transactionId) {
    if ($(".selectedIds").is(":checked")) {
        var transId = transactionId.replace("AR", "");
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.logiware.dwr.ArDwr",
                methodName: "checkDisputeStatus",
                param1: transId,
                dataType: "json"
            },
            success: function (data) {
                if (data) {
                    $.prompt("This AR is in Dispute Status ");
                }
            }
        });
    }
}
