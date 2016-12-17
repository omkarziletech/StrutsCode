/* 
 * Document   : reconcile
 * Created on : Mar 28, 2012, 3:14:35 PM
 * Author     : Lakshmi Naryanan
 */
$(document).ready(function () {
    closePreloading();
    $("#file").fileInput();
    if ($(".search-results").length > 0) {
        changeHeight();
//	$(".display-table").freezeHeader({
//	    container:".search-results"
//	});
    }
    $("form").submit(function () {
        showPreloading();
    });
    $(window).resize(function () {
        window.parent.changeHeight();
        if ($(".search-results").length > 0) {
            changeHeight();
        }
    });
    $("#glAccount").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=RECONCILE&template=reconcile&fieldIndex=1,2,3,5&",
        width: "400px",
        otherFields: "bankName^bankAccount^lastReconciledDate",
        callback: "searchResults()"
    });
    $("#reconcileDateCalendar").insertFromCalendar({
        inputField: "reconcileDate",
        format: "%m/%d/%Y"
    });

    $("#bankBalance").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    $("#bankBalance").keyup(function (event) {
        validateAmount(event, this);
    });
    $("#bankBalance").change(function () {
        if (isNaN($(this).val())) {
            $.prompt("Please enter valid amount", {
                callback: function () {
                    $("#bankBalance").val("0.00").callFocus();
                }
            });
        } else {
            $("#bankBalance").val(Number($("#bankBalance").val()).toFixed(2));
        }
        calculateDifference();
    });

    $(document).keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode === 13 && $(".jqicontainer").length === 0 && $(".ac_results").css("display") === "none") {
            search();
        }
    });
});

function changeHeight() {
    var frameHeight = window.parent.getFrameHeight();
    $(".search-results").height(frameHeight - $(".search-filters").height() - 103);
}

function validateDate() {
    if ($.trim($("#reconcileDate").val()) !== "" && !isDate("#reconcileDate")) {
        $.prompt("Please enter date in mm/dd/yyyy format", {
            callback: function () {
                $("#reconcileDate").val("").callFocus();
            }
        });
    }
}

function searchResults() {
    if ($.trim($("#glAccount").val()) === "") {
        $("#glAccount").callFocus();
    } else if ($.trim($("#reconcileDate").val()) === "") {
        $("#reconcileDate").callFocus();
    } else {
        validateDate();
        if ($.trim($("#reconcileDate").val()) === "") {
            $("#reconcileDate").callFocus();
        } else {
            search();
        }
    }
}

function search() {
    if ($.trim($("#glAccount").val()) === "") {
        $.prompt("Please enter gl account", {
            callback: function () {
                $("#glAccount").callFocus();
            }
        });
    } else if ($.trim($("#reconcileDate").val()) === "") {
        $.prompt("Please enter reconcile date", {
            callback: function () {
                $("#reconcileDate").callFocus();
            }
        });
    } else {
        createReconcileItems();
        $("#action").val("search");
        $("#reconcileForm").submit();
    }
}

function gotoPage(pageNo) {
    $("#selectedPage").val(pageNo);
    search();
}

function gotoSelectedPage() {
    $("#selectedPage").val($("#selectedPageNo").val());
    search();
}

function doSort(sortBy, orderBy) {
    $("#sortBy").val(sortBy);
    $("#orderBy").val(orderBy);
    search();
}

function refresh() {
    $("#action").val("refresh");
    $("#reconcileForm").submit();
}

function calculateTotals(obj) {
    var checksOpen = $("#checksOpen").val().replace(/[^-0-9.]+/g, '');
    var depositsOpen = $("#depositsOpen").val().replace(/[^-0-9.]+/g, '');
    checksOpen = $.trim(checksOpen) === "" ? Number(0) : Number(checksOpen);
    depositsOpen = $.trim(depositsOpen) === "" ? Number(0) : Number(depositsOpen);
    var row = $(obj).parent().parent();
    var debit = Number(row.find(".debit").val().replace(/[^-0-9.]+/g, ''));
    var credit = Number(row.find(".credit").val().replace(/[^-0-9.]+/g, ''));
    if ($(obj).is(":checked")) {
        depositsOpen -= debit;
        checksOpen -= credit;
    } else {
        depositsOpen += debit;
        checksOpen += credit;
    }
    $("#depositsOpen").val(depositsOpen).currencyFormat(2, "#,##0.00");
    $("#checksOpen").val(checksOpen).currencyFormat(2, "#,##0.00");
    calculateDifference();
}

function calculateDifference() {
    var bankBalance = $("#bankBalance").val().replace(/[^-0-9.]+/g, '');
    var glBalance = $("#glBalance").val().replace(/[^-0-9.]+/g, '');
    var checksOpen = $("#checksOpen").val().replace(/[^-0-9.]+/g, '');
    var depositsOpen = $("#depositsOpen").val().replace(/[^-0-9.]+/g, '');
    bankBalance = $.trim(bankBalance) === "" ? Number(0) : Number(bankBalance);
    glBalance = $.trim(glBalance) === "" ? Number(0) : Number(glBalance);
    checksOpen = $.trim(checksOpen) === "" ? Number(0) : Number(checksOpen);
    depositsOpen = $.trim(depositsOpen) === "" ? Number(0) : Number(depositsOpen);
    $("#difference").val(Number(bankBalance - glBalance + depositsOpen - checksOpen)).currencyFormat(2, "#,##0.00");
}

function reconcile() {
    if ($.trim($("#glAccount").val()) === "") {
        $.prompt("Please enter gl account", {
            callback: function () {
                $("#glAccount").callFocus();
            }
        });
    } else if ($.trim($("#reconcileDate").val()) === "") {
        $.prompt("Please enter reconcile date", {
            callback: function () {
                $("#reconcileDate").callFocus();
            }
        });
    } else if (parseFloat($.trim($("#difference").val())) !== 0.00) {
        $.prompt("There is a reconciling difference, do you want to create a j/e for the difference?", {
            buttons: {
                Yes: true,
                No: false
            },
            callback: function (v, m, f) {
                if (v) {
                    createReconcileItemsForJE();
                    $("#action").val("createJournalEntry");
                    $("#reconcileForm").submit();
                }
            }
        });
    } else {
        createReconcileItems();
        $("#action").val("reconcile");
        $("#reconcileForm").submit();
    }
}

function save() {
    if ($.trim($("#glAccount").val()) === "") {
        $.prompt("Please enter gl account", {
            callback: function () {
                $("#glAccount").callFocus();
            }
        });
    } else if ($.trim($("#reconcileDate").val()) === "") {
        $.prompt("Please enter reconcile date", {
            callback: function () {
                $("#reconcileDate").callFocus();
            }
        });
    } else if ($(".clear:checked").length <= 0) {
        $.prompt("Please select atleast one line item");
    } else {
        createReconcileItems();
        $("#action").val("save");
        $("#reconcileForm").submit();
    }
}

function createReconcileItemsForJE() {
    var index = 0;
    $(".clear").each(function () {
        var row = $(this).parent().parent();
        $(this).attr("name", "transactions[" + index + "].id");
        row.find("td .transactionType").attr("name", "transactions[" + index + "].transactionType");
        row.find("td .referenceNumber").attr("name", "transactions[" + index + "].referenceNumber");
        row.find("td .batchId").attr("name", "transactions[" + index + "].batchId");
        row.find("td .transactionDate").attr("name", "transactions[" + index + "].transactionDate");
        row.find("td .debit").attr("name", "transactions[" + index + "].debit");
        row.find("td .credit").attr("name", "transactions[" + index + "].credit");
        if ($(this).is(":checked")) {
            row.find("td .status").attr("name", "transactions[" + index + "].status").val("RIP");
        } else {
            row.find("td .status").attr("name", "transactions[" + index + "].status").val("");
        }
        index++;
    });
}

function createReconcileItems() {
    var index = 0;
    $(".clear").each(function () {
        var row = $(this).parent().parent();
        if ($(this).is(":checked")) {
            $(this).attr("name", "transactions[" + index + "].id");
            row.find("td .transactionType").attr("name", "transactions[" + index + "].transactionType");
            row.find("td .referenceNumber").attr("name", "transactions[" + index + "].referenceNumber");
            row.find("td .batchId").attr("name", "transactions[" + index + "].batchId");
            row.find("td .transactionDate").attr("name", "transactions[" + index + "].transactionDate");
            row.find("td .debit").attr("name", "transactions[" + index + "].debit");
            row.find("td .credit").attr("name", "transactions[" + index + "].credit");
            row.find("td .status").attr("name", "transactions[" + index + "].status").val("RIP");
            index++;
        }
    });
}

function importTemplate() {
    if ($.trim($("#file").val()) === "") {
        $.prompt("Please attach reconcile excel spreadsheet to upload");
    } else if ($.trim($("#glAccount").val()) === "") {
        $.prompt("Please enter gl account", {
            callback: function () {
                $("#glAccount").callFocus();
            }
        });
    } else {
        $("#action").val("upload");
        $("#className").val("com.logiware.dwr.GeneralLedgerDwr");
        $("#methodName").val("importReconcileTemplate");
        $("#reconcileForm").fileupload({
            preloading: true,
            success: function (data) {
                if (data) {
                    $("#importFileName").val(data);
                    $("#action").val("importTemplate");
                    $("#reconcileForm").submit();
                }
            }
        });
    }
}

function showNotes() {
    if ($.trim($("#glAccount").val()) === "") {
        $.prompt("Please enter gl account", {
            callback: function () {
                $("#glAccount").callFocus();
            }
        });
    } else {
        GB_show("Notes", path + "/notes.do?moduleId=RECONCILATION&moduleRefId=" + $.trim($("#glAccount").val()), 375, 700);
    }
}

function downloadFile(src) {
    $("<iframe />", {
        name: "iframe",
        id: "iframe",
        src: src
    }).appendTo("body").hide();
}