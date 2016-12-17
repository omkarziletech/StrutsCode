var changes = "false";
jQuery(document).ready(function () {
    window.parent.closePreloading();
    jQuery("#glBatchForm").submit(function () {
        window.parent.showPreloading();
    });
    jQuery("#importBatchFile").fileInput();
    jQuery("#importJournalEntryFile").fileInput();
    jQuery(".addLineItem").click(function () {
        addLineItems();
    });
    jQuery("input.debit,input.credit").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    jQuery("input.debit,input.credit").keyup(function (event) {
        validateAmount(event, this);
    });
    jQuery("input.debit,input.credit").blur(function () {
        onChangeAmount(this);
    });
    jQuery(".account").blur(function () {
        calculateAmount();
    });
    jQuery(".account").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            if (event.keyCode === 9 || event.keyCode === 13) {
                moveToDebit(this);
            }
        }
    });
    jQuery(".debit").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        } else {
            moveToCredit(event, this);
        }
    });
    jQuery(".credit").keydown(function (event) {
        if (event.shiftKey) {
            // No need to prevent anything but allow shift+tab
        }
        else {
            moveToAccount(event, this);
        }
    });
    jQuery("#save").click(function () {
        saveOrUpdate();
    });
    jQuery(".removeLineItem").click(function () {
        jQuery(this).parent().parent().remove();
        calculateAmount();
    });

    jQuery("#journalEntrySubledgerType").change(function () {
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.GeneralLedgerDwr",
                methodName: "getSubledgerDescription",
                param1: jQuery(this).val()
            },
            preloading: true,
            success: function (data) {
                if (data) {
                    jQuery("#journalEntrySubledgerDescription").val(data);
                }
            }
        });
    });
});

function addBatch() {
    jQuery("#action").val("addBatch");
    jQuery("#glBatchForm").submit();
}

function goBack() {
    if (changes === "true") {
        jQuery.prompt("Unsaved changes, do you want to save the changes?", {
            buttons: {
                Yes: true,
                No: false
            },
            callback: function (v, m, f) {
                if (v) {
                    saveOrUpdate();
                } else {
                    jQuery("#batchId").val(jQuery("#glBatchId").val());
                    jQuery("#action").val("searchBatch");
                    jQuery("#glBatchForm").submit();
                }
            }
        });
    } else {
        jQuery("#batchId").val(jQuery("#glBatchId").val());
        jQuery("#action").val("searchBatch");
        jQuery("#glBatchForm").submit();
    }
}

function goBackToReconcile() {
    if (changes === "true") {
        jQuery.prompt("Unsaved changes, do you want to save the changes?", {
            buttons: {
                Yes: true,
                No: false
            },
            callback: function (v, m, f) {
                if (v) {
                    saveOrUpdate();
                } else {
                    jQuery("#batchId").val(jQuery("#glBatchId").val());
                    jQuery("#action").val("goBackToReconcile");
                    jQuery("#glBatchForm").submit();
                }
            }
        });
    } else {
        jQuery("#batchId").val(jQuery("#glBatchId").val());
        jQuery("#action").val("goBackToReconcile");
        jQuery("#glBatchForm").submit();
    }
}

function autoReverse() {
    var errors = checkErrors();
    if (errors.length === 0) {
        createLineItems();
        jQuery("#action").val("autoReverseBatch");
        jQuery("#glBatchForm").submit();
    } else {
        var ul = "<ul style='list-style:disc'>";
        for (var i = 0; i < errors.length; i++) {
            ul += "<li>" + errors[i] + "</li>";
        }
        ul += "</ul>";
        jQuery.prompt(ul);
    }
}

function copyJournalEntry() {
    var errors = checkErrors();
    if (errors.length === 0) {
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.GeneralLedgerDwr",
                methodName: "showCopyBatchPopup",
                param1: jQuery("#glBatchId").val(),
                request: "true",
                forward: "/jsps/GeneralLedger/copyBatch.jsp"
            },
            preloading: true,
            success: function (data) {
                if (data) {
                    showAlternateMask();
                    jQuery("<div style='width:300px;height:100px'></div>").html(data).addClass("popup").appendTo("body").center();
                }
            }
        });
    } else {
        var ul = "<ul style='list-style:disc'>";
        for (var i = 0; i < errors.length; i++) {
            ul += "<li>" + errors[i] + "</li>";
        }
        ul += "</ul>";
        jQuery.prompt(ul);
    }
}

function copyToNewBatch() {
    createLineItems();
    jQuery("#newGlBatchId").val("");
    jQuery("#action").val("copyBatch");
    jQuery("#glBatchForm").submit();
}

function copyToOldBatch() {
    createLineItems();
    jQuery("#action").val("copyBatch");
    jQuery("#newGlBatchId").val(jQuery("#openBatchId").val());
    jQuery("#glBatchForm").submit();
}

function reverse() {
    var errors = checkErrors();
    if (errors.length === 0) {
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.GeneralLedgerDwr",
                methodName: "showReverseBatchPopup",
                param1: jQuery("#glBatchId").val(),
                request: "true",
                forward: "/jsps/GeneralLedger/reverseBatch.jsp"
            },
            preloading: true,
            success: function (data) {
                if (data) {
                    showAlternateMask();
                    jQuery("<div style='width:300px;height:100px'></div>").html(data).addClass("popup").appendTo("body").center();
                }
            }
        });
    } else {
        var ul = "<ul style='list-style:disc'>";
        for (var i = 0; i < errors.length; i++) {
            ul += "<li>" + errors[i] + "</li>";
        }
        ul += "</ul>";
        jQuery.prompt(ul);
    }
}

function reverseToNewBatch() {
    createLineItems();
    jQuery("#newGlBatchId").val("");
    jQuery("#action").val("reverseBatch");
    jQuery("#glBatchForm").submit();
}

function reverseToOldBatch() {
    createLineItems();
    jQuery("#action").val("reverseBatch");
    jQuery("#newGlBatchId").val(jQuery("#openBatchId").val());
    jQuery("#glBatchForm").submit();
}

function addJournalEntry() {
    var errors = checkErrors();
    if (errors.length === 0) {
        createLineItems();
        jQuery("#action").val("addJournalEntry");
        jQuery("#glBatchForm").submit();
    } else {
        var ul = "<ul style='list-style:disc'>";
        for (var i = 0; i < errors.length; i++) {
            ul += "<li>" + errors[i] + "</li>";
        }
        ul += "</ul>";
        jQuery.prompt(ul);
    }
}

function deleteJournalEntry() {
    jQuery("#action").val("deleteJournalEntry");
    jQuery("#glBatchForm").submit();
}

function addLineItems() {
    var lastId = jQuery("#lineItems tr:last").find(".lineItemId").val().split("-");
    var suffix = Number(lastId[2]);
    var styleClass = "odd";
    if (jQuery("#lineItems tr:last").hasClass("odd")) {
        styleClass = "even";
    }
    for (var count = 1; count < 11; count++) {
        var rowIndex = jQuery("#lineItems tr").length - 1;
        var row = jQuery(".defaultLineItem").clone().removeClass("defaultLineItem").addClass(styleClass).show();
        row.find(".account").attr("name", "account" + rowIndex).attr("id", "account" + rowIndex);
        row.find(".accountValid").attr("name", "accountValid" + rowIndex).attr("id", "accountValid" + rowIndex);
        row.find(".accountDescription").attr("name", "accountDescription" + rowIndex).attr("id", "accountDescription" + rowIndex);
        row.find(".accountChoices").attr("id", "accountChoices" + rowIndex);
        row.find("input.debit,input.credit").val("0.00");
        row.find(".lineItemId").val(lastId[0] + "-" + lastId[1] + "-" + pad(suffix + count, 3));
        row.find("input.debit,input.credit").keydown(function (event) {
            allowOnlyNumbers(event, this);
        });
        row.find("input.debit,input.credit").keyup(function (event) {
            validateAmount(event, this);
        });
        row.find("input.debit,input.credit").blur(function () {
            onChangeAmount(this);
        });
        row.find(".account").blur(function () {
            calculateAmount();
        });
        row.find(".removeLineItem").click(function () {
            jQuery(this).parent().parent().remove();
            calculateAmount();
        });
        row.find(".account").keydown(function (event) {
            if (event.shiftKey) {
                // No need to prevent anything but allow shift+tab
            } else {
                if (event.keyCode === 9 || event.keyCode === 13) {
                    moveToDebit(event, this);
                }
            }
        });
        row.find(".debit").keydown(function (event) {
            if (event.shiftKey) {
                // No need to prevent anything but allow shift+tab
            } else {
                moveToCredit(event, this);
            }
        });
        row.find(".credit").keydown(function (event) {
            if (event.shiftKey) {
                // No need to prevent anything but allow shift+tab
            } else {
                moveToAccount(event, this);
            }
        });
        row.appendTo("#lineItems");
        if (styleClass === "even") {
            styleClass = "odd";
        } else {
            styleClass = "even";
        }
        var autocompleterUrl = rootPath + "/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=account" + rowIndex + "&tabName=JournalEntry";
        var fn = "moveToDebit('#account" + rowIndex + "')";
        AjaxAutocompleter("account" + rowIndex, "accountChoices" + rowIndex, "accountDescription" + rowIndex, "accountValid" + rowIndex, autocompleterUrl, fn, "");
    }
}

function moveToMemo() {
    jQuery("#journalEntryMemo").select().fadeIn().fadeOut().fadeIn();
}

function moveToDebit(account) {
    jQuery(account).parent().parent().parent().find(".debit").select().fadeIn().fadeOut().fadeIn();
}

function moveToCredit(event, debit) {
    if (event.keyCode === 9 || event.keyCode === 13) {
        event.preventDefault();
        var ele = jQuery(debit).parent().parent().find(".credit");
        ele.val(jQuery.trim(ele.val()));
        ele.select().fadeIn().fadeOut().fadeIn();
        onChangeAmount(debit)
    }
}

function moveToAccount(event, credit) {
    if (event.keyCode === 9 || event.keyCode === 13) {
        if (jQuery(credit).parent().parent().prevAll().length === jQuery("#lineItems tr").length - 1) {
            addLineItems();
        }
        event.preventDefault();
        var ele = jQuery(credit).parent().parent().next().find(".account");
        ele.val(jQuery.trim(ele.val()));
        ele.select().fadeIn().fadeOut().fadeIn();
        onChangeAmount(credit);
    }
}

function onChangeAmount(obj) {
    if (isNaN(jQuery(obj).val())) {
        jQuery.prompt("Please enter valid amount");
        jQuery(obj).val("0.00").select().fadeIn().fadeOut().fadeIn();
    } else {
        jQuery(obj).val(Number(jQuery(obj).val()).toFixed(2));
    }
    calculateAmount();
}

function calculateAmount() {
    changes = "true";
    var totalDebit = 0.00;
    var totalCredit = 0.00;
    jQuery(".account").each(function () {
        var account = jQuery.trim(jQuery(this).val());
        if (account !== "") {
            var row = jQuery(this).parent().parent().parent();
            var debit = row.find(".debit").val();
            var credit = row.find(".credit").val();
            totalDebit += isNaN(debit) ? 0 : Number(debit);
            totalCredit += isNaN(credit) ? 0 : Number(credit);
        }
    });
    var batchDebit = Number(jQuery("#batchDebit").val());
    jQuery("#glBatchDebit").val(Number(totalDebit + batchDebit).toFixed(2));
    var batchCredit = Number(jQuery("#batchCredit").val());
    jQuery("#glBatchCredit").val(Number(batchCredit + totalCredit).toFixed(2));
    jQuery("#batchDifference").val(Number((batchDebit + totalDebit) - (batchCredit + totalCredit)).toFixed(2));
    jQuery("#journalEntryDebit").val(Number(totalDebit).toFixed(2));
    jQuery("#journalEntryCredit").val(Number(totalCredit).toFixed(2));
    jQuery("#journalEntryDifference").val(Number(totalDebit - totalCredit).toFixed(2));
}

function saveOrUpdate() {
    var errors = checkErrors();
    if (errors.length === 0) {
        createLineItems();
        jQuery("#action").val("saveOrUpdateBatch");
        jQuery("#glBatchForm").submit();
    } else {
        var ul = "<ul style='list-style:disc'>";
        for (var i = 0; i < errors.length; i++) {
            ul += "<li>" + errors[i] + "</li>";
        }
        ul += "</ul>";
        jQuery.prompt(ul);
    }
}


function deleteAndGoBackToFiscalPeriod() {
    jQuery.prompt("Do you want to delete the batch?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v, m, f) {
            if (v) {
                jQuery("#batchId").val(jQuery("#glBatchId").val());
                jQuery("#action").val("deleteAndGoBackToFiscalPeriod");
                jQuery("#glBatchForm").submit();
            }
        }
    });
}

function postAndCloseYear() {
    var errors = checkErrors();
    if (errors.length === 0) {
        createLineItems();
        jQuery("#action").val("postAndCloseYear");
        jQuery("#glBatchForm").submit();
    } else {
        var ul = "<ul style='list-style:disc'>";
        for (var i = 0; i < errors.length; i++) {
            ul += "<li>" + errors[i] + "</li>";
        }
        ul += "</ul>";
        jQuery.prompt(ul);
    }
}

function postAndGoBackToReconcile() {
    var errors = checkErrors();
    if (errors.length === 0) {
        createLineItems();
        jQuery("#action").val("postAndGoBackToReconcile");
        jQuery("#glBatchForm").submit();
    } else {
        var ul = "<ul style='list-style:disc'>";
        for (var i = 0; i < errors.length; i++) {
            ul += "<li>" + errors[i] + "</li>";
        }
        ul += "</ul>";
        jQuery.prompt(ul);
    }
}

function checkErrors() {
    var error = [];
    if (jQuery.trim(jQuery("#glBatchDescription").val()) === "") {
        error[error.length] = "Please enter description for Batch";
    }
    if (jQuery.trim(jQuery("#journalEntryDescription").val()) === "") {
        error[error.length] = "Please enter description for Journal Entry";
    }
    if (jQuery.trim(jQuery("#journalEntryPeriod").val()) === "") {
        error[error.length] = "Please select period";
    }
    var journalEntryDebit = jQuery.trim(jQuery("#journalEntryDebit").val());
    var journalEntryCredit = jQuery.trim(jQuery("#journalEntryCredit").val());
    if (parseFloat(journalEntryDebit) !== parseFloat(journalEntryCredit)) {
        error[error.length] = "Journal Entry out of balance. Please make sure both debit and credit are equal";
    }
    var count = 0;
    jQuery(".account").each(function () {
        var account = jQuery.trim(jQuery(this).val());
        if (account !== "") {
            var row = jQuery(this).parent().parent().parent();
            var debit = row.find(".debit").val();
            var credit = row.find(".credit").val();
            var lineItem = row.find(".lineItemId").val();
            if (parseFloat(debit) === 0.00 && parseFloat(credit) === 0.00) {
                error[error.length] = "Please enter debit or credit for account " + account + " and line item " + lineItem;
            }
            count++;
        }
    });
    if (count === 0) {
        error[error.length] = "Please add Line Items";
    }
    return error;
}

function createLineItems() {
    var index = 0;
    jQuery(".account").each(function () {
        var account = jQuery.trim(jQuery(this).val());
        if (account !== "") {
            var row = jQuery(this).parent().parent().parent();
            var debit = row.find(".debit").val();
            var credit = row.find(".credit").val();
            if ((!isNaN(debit) && parseFloat(debit) !== 0.00) || (!isNaN(credit) && parseFloat(credit) !== 0.00)) {
                jQuery(this).attr("name", "lineItems[" + index + "].account");
                row.find(".accountDescription").attr("name", "lineItems[" + index + "].accountDescription");
                row.find(".debit").attr("name", "lineItems[" + index + "].debit");
                row.find(".credit").attr("name", "lineItems[" + index + "].credit");
                row.find(".lineItemId").attr("name", "lineItems[" + index + "].id");
                index++;
            }
        }
    });
}

function gotoBatch(batchIndex) {
    jQuery("#batchIndex").val(batchIndex);
    jQuery("#journalEntryIndex").val("0");
    jQuery("#action").val("gotoBatch");
    jQuery("#glBatchForm").submit();
}

function gotoJournalEntry(journalEntryIndex) {
    jQuery("#journalEntryIndex").val(journalEntryIndex);
    jQuery("#action").val("gotoJournalEntry");
    jQuery("#glBatchForm").submit();
}

function printBatch() {
    var batchId = jQuery("#glBatchId").val();
    var url = rootPath + "/glBatch.do?action=printBatch&glBatch.id=" + batchId;
    window.parent.showGreyBox("GL Batch Report - " + batchId, url);
}

function exportBatch() {
    jQuery("#action").val("exportBatch");
    jQuery("#glBatchForm").submit();
    closePreloading();
}

function printJournalEntry() {
    var journalEntryId = jQuery("#journalEntryId").val();
    var url = rootPath + "/glBatch.do?action=printJournalEntry&journalEntry.id=" + journalEntryId;
    window.parent.showGreyBox("Journal Entry Report - " + journalEntryId, url);
}

function exportJournalEntry() {
    jQuery("#action").val("exportJournalEntry");
    jQuery("#glBatchForm").submit();
    closePreloading();
}

function printHistory() {
    var journalEntryId = jQuery("#journalEntryId").val();
    var url = rootPath + "/glBatch.do?action=printHistory&journalEntry.id=" + journalEntryId;
    window.parent.showGreyBox("Subledger History for JE - " + journalEntryId, url);
}

function exportHistory() {
    jQuery("#action").val("exportHistory");
    jQuery("#glBatchForm").submit();
    closePreloading();
}

function importBatch() {
    if (jQuery.trim(jQuery("#importBatchFile").val()) === "") {
        jQuery.prompt("Please attach batch excel spreadsheet to upload");
    } else {
        jQuery("#importBatchFile").attr("name", "file");
        jQuery("#action").val("upload");
        jQuery("#className").val("com.logiware.dwr.GeneralLedgerDwr");
        jQuery("#methodName").val("importBatchFile");
        jQuery("#glBatchForm").fileupload({
            preloading: true,
            success: function (data) {
                if (data) {
                    jQuery("#batchFileName").val(data);
                    jQuery("#importBatchFile").attr("name", "importBatchFile");
                    var errors = checkPreImportErrors();
                    if (errors.length === 0) {
                        createLineItems();
                        jQuery("#action").val("importBatch");
                        jQuery("#glBatchForm").submit();
                    } else {
                        var ul = "<ul style='list-style:disc'>";
                        for (var i = 0; i < errors.length; i++) {
                            ul += "<li>" + errors[i] + "</li>";
                        }
                        ul += "</ul>";
                        jQuery.prompt(ul);
                    }
                }
            }
        });
    }
}

function importJournalEntry() {
    if (jQuery.trim(jQuery("#importJournalEntryFile").val()) === "") {
        jQuery.prompt("Please attach journal entry excel spreadsheet to upload");
    } else {
        jQuery("#importJournalEntryFile").attr("name", "file");
        jQuery("#action").val("upload");
        jQuery("#className").val("com.logiware.dwr.GeneralLedgerDwr");
        jQuery("#methodName").val("importJournalEntryFile");
        jQuery("#glBatchForm").fileupload({
            preloading: true,
            success: function (data) {
                if (data) {
                    jQuery("#journalEntryFileName").val(data);
                    jQuery("#importJournalEntryFile").attr("name", "importJournalEntryFile");
                    var errors = checkPreImportErrors();
                    if (errors.length === 0) {
                        createLineItems();
                        jQuery("#action").val("importJournalEntry");
                        jQuery("#glBatchForm").submit();
                    } else {
                        var ul = "<ul style='list-style:disc'>";
                        for (var i = 0; i < errors.length; i++) {
                            ul += "<li>" + errors[i] + "</li>";
                        }
                        ul += "</ul>";
                        jQuery.prompt(ul);
                    }
                }
            }
        });
    }
}

function checkPreImportErrors() {
    var error = [];
    if (jQuery.trim(jQuery("#glBatchDescription").val()) === "") {
        error[error.length] = "Please enter description for Batch";
    }
    var count = 0;
    jQuery(".account").each(function () {
        var account = jQuery.trim(jQuery(this).val());
        if (account !== "") {
            var row = jQuery(this).parent().parent().parent();
            var debit = row.find(".debit").val();
            var credit = row.find(".credit").val();
            var lineItem = row.find(".lineItemId").val();
            if (parseFloat(debit) === 0.00 && parseFloat(credit) === 0.00) {
                error[error.length] = "Please enter debit or credit for account " + account + " and line item " + lineItem;
            }
            count++;
        }
    });

    if (count !== 0 || jQuery.trim(jQuery("#journalEntryId").val()) !== "") {
        if (jQuery.trim(jQuery("#journalEntryDescription").val()) === "") {
            error[error.length] = "Please enter description for Journal Entry";
        }
        if (jQuery.trim(jQuery("#journalEntryPeriod").val()) === "") {
            error[error.length] = "Please select period";
        }
        var journalEntryDebit = jQuery.trim(jQuery("#journalEntryDebit").val());
        var journalEntryCredit = jQuery.trim(jQuery("#journalEntryCredit").val());
        if (parseFloat(journalEntryDebit) !== parseFloat(journalEntryCredit)) {
            error[error.length] = "Journal Entry out of balance. Please make sure both debit and credit are equal";
        }
    }
    return error;
}

function drillDown(obj) {
    var row = jQuery(obj).parent().parent().parent();
    var lineItemId = row.find(".lineItemId").val();
    var html = row.find(".drillDownDiv").html();
    if (jQuery.trim(html) === "") {
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.GeneralLedgerDwr",
                methodName: "showDrillDownForLineItem",
                param1: lineItemId,
                request: "true",
                forward: "/jsps/GeneralLedger/drillDownTemplate.jsp"
            },
            preloading: true,
            success: function (data) {
                if (data) {
                    jQuery(".drillUp").hide();
                    jQuery(".drillDown").show();
                    jQuery(".drillDownDiv").hide();
                    jQuery(obj).hide();
                    row.find(".drillUp").show();
                    row.find(".drillDownDiv").html(data).slideDown("slow");
                }
            }
        });
    } else {
        jQuery(".drillUp").hide();
        jQuery(".drillDown").show();
        jQuery(".drillDownDiv").hide();
        jQuery(obj).hide();
        row.find(".drillUp").show();
        row.find(".drillDownDiv").slideDown("slow");
    }
}

function drillUp(obj) {
    var row = jQuery(obj).parent().parent();
    row.find(".drillDownDiv").slideUp("slow", function () {
        jQuery(".drillUp").hide();
        jQuery(".drillDown").show();
        jQuery(obj).hide();
    });
}

function showScanOrAttach(documentId) {
    GB_show('Upload', rootPath + '/scan.do?screenName=JOURNAL ENTRY&documentId=' + documentId, 350, 650);
}

if (document.getElementById("journalEntryPeriodChoices")) {
    var url = rootPath + "/servlet/AutoCompleterServlet?action=FiscalPeriod&textFieldId=journalEntryPeriod&from=JE";
    AjaxAutocompleter("journalEntryPeriod", "journalEntryPeriodChoices", "", "journalEntryPeriodValid", url, "moveToMemo()");
}
