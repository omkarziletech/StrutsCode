/* 
 *  Document   : fiscalPeriod
 *  Created on : Feb 27, 2014, 9:00:00 PM
 *  Author     : Lakshmi Narayanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function showLightBox(title, url, height, width, callback) {
    url += (url.indexOf("?") > -1 ? "&" : "?") + "TB_iframe&height=" + height + "&width=" + width;
    Lightbox.showPopUp(title, url, "sexylightbox", "", callback);
}

function displayMessage(message) {
    $(".error").html("");
    $(".message").html(message);
}

function displayError(error) {
    $(".message").html("");
    $(".error").html(error);
}

function searchYear() {
    $("#action").val("searchYear");
    $("#fiscalPeriodForm").submit();
}

function createYear() {
    var years = [];
    $('#year option').each(function() {
        years.push($(this).val());
    });
    var maxYear = Math.max.apply(Math, years);
    var newYear = maxYear + 1;
    $.prompt("Do you want to create a new year - " + newYear + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function(v) {
            if (v) {
                $("#year").append($("<option></option>").attr("value", newYear).text(newYear)).val(newYear);
                $("#startPeriod, #startPeriodCheck").val(maxYear + "04");
                $("#endPeriod, #endPeriodCheck").val(newYear + "03");
                $("#action").val("createYear");
                $("#fiscalPeriodForm").submit();
            }
        }
    });
}

function createJournalEntry(year) {
    $.ajaxx({
        url: path + "/fiscalPeriod.do",
        data: {
            action: "createJournalEntry",
            year: year
        },
        preloading: true,
        success: function(data) {
            window.parent.showPreloading();
            window.location = path + "/glBatch.do?action=editBatch&from=FiscalPeriod&batchId=" + data + "&year=" + year;
        }
    });
}

function closeYear() {
    var year = $("#year").val();
    $.prompt("Do you want to close the year - " + year + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function(v) {
            if (v) {
                $.ajaxx({
                    url: path + "/fiscalPeriod.do",
                    data: {
                        action: "validateClosingYear",
                        year: year
                    },
                    preloading: true,
                    success: function(data) {
                        if(data === "success"){
                            createJournalEntry(year);
                        }else{
                            displayError(data);
                        }
                    }
                });
            }
        }
    });
}

function createBudget() {
    var budgetSet = $("#budgetSet").val();
    var year = $("#year").val();
    $.prompt("Do you want to create the budget set - " + budgetSet + " for the year - " + year + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function(v) {
            if (v) {
                $.ajaxx({
                    url: path + "/fiscalPeriod.do",
                    data: {
                        action: "createBudget",
                        year: year,
                        budgetSet: budgetSet
                    },
                    preloading: true,
                    success: function(data) {
                        displayMessage(data);
                    }
                });
            }
        }
    });
}

function importBudget() {
    showLightBox("Upload Budget", path + "/jsp/common/upload.jsp?action=uploadBudget", 100, 400);
}

function createTrialBalance() {
    var url = path + "/jsp/accounting/generalLedger/fiscalPeriod/trialBalance.jsp";
    showLightBox("Trial Balance", url, 200, 500);
}

function createIncomeStatement() {
    var url = path + "/jsp/accounting/generalLedger/fiscalPeriod/incomeStatement.jsp";
    showLightBox("Income Statement", url, 200, 500);
}

function closePeriod(ele) {
    var index = $(".switch").index(ele);
    $.prompt("Do you want to close the period - " + $(ele).val() + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function(v) {
            if (v) {
                $.ajaxx({
                    url: path + "/fiscalPeriod.do",
                    data: {
                        action: "closePeriod",
                        period: $(ele).val()
                    },
                    preloading: true,
                    success: function(data) {
                        displayMessage(data);
                    }
                });
            } else {
                $(ele).attr("checked", true);
                $(".status-switch:eq(" + index + ")").addClass('true').removeClass('false');
            }
        }
    });
}

function openPeriod(ele) {
    var index = $(".switch").index(ele);
    $.prompt("Do you want to reopen the period - " + $(ele).val() + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function(v) {
            if (v) {
                $.ajaxx({
                    url: path + "/fiscalPeriod.do",
                    data: {
                        action: "openPeriod",
                        period: $(ele).val()
                    },
                    preloading: true,
                    success: function(data) {
                        displayMessage(data);
                    }
                });
            } else {
                $(ele).removeAttr("checked");
                $(".status-switch:eq(" + index + ")").addClass('false').removeClass('true');
            }
        }
    });
}

function onSwitched(ele) {
    if ($(ele).is(":checked")) {
        $(ele).attr("checked", true);
        openPeriod(ele);
    } else {
        $(ele).removeAttr("checked");
        closePeriod(ele);
    }
}

function initSwitch() {
    $(".switch").jswitch({
        afterContent: "OPEN",
        beforeContent: "CLOSE",
        css: {
            width: "55px",
            margin: "0 0 0 40%"
        },
        styleClass: "status-switch",
        callback: function($input) {
            onSwitched($input);
        }
    });
}

function showNotes(moduleRefId) {
    var url = path + "/notes.do";
    url += "?moduleId=FISCAL_PERIOD";
    url += "&moduleRefId=" + encodeURIComponent(moduleRefId);
    showLightBox("Notes", url, 300, 800);
}

$(document).ready(function() {
    $("#fiscalPeriodForm").submit(function() {
        showPreloading();
    });
    initSwitch();
    $("[title != '']").not("link").tooltip();
    Lightbox.initialize({
        color: 'black',
        dir: path + '/js/lightbox/images',
        moveDuration: 1,
        resizeDuration: 1
    });
});