/* 
 * Document   : arReports
 * Created on : Jun 12, 2012, 12:34:35 PM
 * Author     : Lakshmi Naryanan
 */
function refresh(tabName) {
    showPreloading();
    var url = path + "/arReports.do";
    $.ajax({
        type: 'POST',
        url: url,
        data: {
            action: "show",
            tabName: tabName
        },
        cache: false,
        success: function(data) {
            $("#" + tabName).html(data);
            closePreloading();
            init(tabName);
        },
        error: function(jqXHR) {
            closePreloading();
            $.prompt(jqXHR.responseText, {
                top: "20%",
                left: "35%",
                width: "800px",
                height: "300px"
            });
        }
    });
}

function validateDate(obj) {
    if ($.trim($(obj).val()) !== "" && !isDate(obj)) {
        $.prompt("Please enter date in mm/dd/yyyy format", {
            callback: function() {
                $(obj).val("").callFocus();
            }
        });
    }
}

function initStatement() {
    $("#statementCustomerName").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=CUSTOMER&template=customer&fieldIndex=1,2&",
        width: "420px",
        otherFields: "statementCustomerNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300
    });
}

function setSalesManager() {
    if ($.trim($("#salesManagers").val()) !== "") {
        var salesManagers = $("#salesManagers").val().split("<<<>>>");
        $("#salesManagerName").val(salesManagers[0]);
        $("#salesManagerEmail").val(salesManagers[1]);
        $("#salesManagerSalesCode").val(salesManagers[2]);
        $("#salesManagerSalesId").val(salesManagers[3]);
        $("#agingReportTypeDetail").removeAttr("checked").attr("disabled", true);
        $("#agingReportTypeSummary").attr("checked", true);
        $("#terminalManagers").val("").change();
        $("#agingCustomerName,#agingCollector,#agingCustomerFromRange,#agingCustomerToRange").val("").attr("disabled", true).addClass("readonly");
        $("#agingCustomerNumber,#agingCustomerNameCheck").val("");
        $("#agingAllCustomers,#top10Customers").removeAttr("checked").attr("disabled", true);
        $("input[name='agents']").val("Y").attr("disabled", true);
        $("#agingPrintBtn").css("visibility", "hidden");
    } else {
        $("#salesManagerName").val("");
        $("#salesManagerEmail").val("");
        $("#salesManagerSalesCode").val("");
        $("#salesManagerSalesId").val("");
        $("#agingCustomerName,#agingCollector").removeAttr("disabled").removeClass("readonly");
        $("#agingReportTypeDetail,#agingAllCustomers,#top10Customers,input[name='agents']").removeAttr("disabled");
        $("#agingAllCustomers").attr("checked", true);
        $("#agingPrintBtn").css("visibility", "visible");
    }
}

function setTerminalManager() {
    if ($.trim($("#terminalManagers").val()) !== "") {
        var terminalManagers = $("#terminalManagers").val().split("<<<>>>");
        $("#terminalManagerName").val(terminalManagers[0]);
        $("#terminalManagerEmail").val(terminalManagers[1]);
        $("#terminalManagerTerminalNumber").val(terminalManagers[2]);
        $("#agingReportTypeSummary").removeAttr("checked").attr("disabled", true);
        $("#agingReportTypeDetail").attr("checked", true);
        $("#salesManagers").val("").change();
        $("#agingCustomerName,#agingCollector,#agingCustomerFromRange,#agingCustomerToRange").val("").attr("disabled", true).addClass("readonly");
        $("#agingCustomerNumber,#agingCustomerNameCheck").val("");
        $("#agingAllCustomers,#top10Customers").removeAttr("checked").attr("disabled", true);
        $("input[name='agents']").val("Y").attr("disabled", true);
        $("#agingPrintBtn").css("visibility", "hidden");
    } else {
        $("#terminalManagerName").val("");
        $("#terminalManagerEmail").val("");
        $("#terminalManagerTerminalNumber").val("");
        $("#agingCustomerName,#agingCollector").removeAttr("disabled").removeClass("readonly");
        $("#agingReportTypeSummary,#agingAllCustomers,#top10Customers,input[name='agents']").removeAttr("disabled");
        $("#agingAllCustomers").attr("checked", true);
        $("#agingPrintBtn").css("visibility", "visible");
    }
}

function initAging() {
    $("#agingCustomerName").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=CUSTOMER&template=customer&fieldIndex=1,2&",
        width: "420px",
        otherFields: "agingCustomerNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300
    });
    $("#agingCutOffDateCalendar").insertFromCalendar({
        inputField: "agingCutOffDate",
        format: "%m/%d/%Y"
    });
    setSalesManager();
    setTerminalManager();
}

function initDso() {
    $("#dsoCustomerName").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=CUSTOMER&template=customer&fieldIndex=1,2&",
        width: "420px",
        otherFields: "dsoCustomerNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300
    });
    $("#dsoFilter").change(function() {
        if ($.trim($("#dsoFilter").val()) === "By Collector") {
            $("#dsoCollector").callFocus();
        } else if ($.trim($("#dsoFilter").val()) === "By Customer") {
            $("#dsoCustomerName").callFocus();
        }
    })
}

function initNotes() {
    $("#notesCustomerName").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=CUSTOMER&template=customer&fieldIndex=1,2&",
        width: "420px",
        otherFields: "notesCustomerNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300
    });
    $("#notesFromDateCalendar").insertFromCalendar({
        inputField: "notesFromDate",
        format: "%m/%d/%Y"
    });
    $("#notesToDateCalendar").insertFromCalendar({
        inputField: "notesToDate",
        format: "%m/%d/%Y"
    });
}

function initNoCredit() {
    $("#noCreditFromDateCalendar").insertFromCalendar({
        inputField: "noCreditFromDate",
        format: "%m/%d/%Y"
    });
    $("#noCreditToDateCalendar").insertFromCalendar({
        inputField: "noCreditToDate",
        format: "%m/%d/%Y"
    });
}

function initActivity() {
    $("#activityCustomerName").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=CUSTOMER&template=customer&fieldIndex=1,2&",
        width: "420px",
        otherFields: "activityCustomerNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300
    });
    $("#activityFromDateCalendar").insertFromCalendar({
        inputField: "activityFromDate",
        format: "%m/%d/%Y"
    });
    $("#activityToDateCalendar").insertFromCalendar({
        inputField: "activityToDate",
        format: "%m/%d/%Y"
    });
}
function initDispute() {
    $("#disputeCustomerName").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=CUSTOMER&template=customer&fieldIndex=1,2&",
        width: "420px",
        otherFields: "disputeCustomerNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300
    });
    $("#disputeFromDateCalendar").insertFromCalendar({
        inputField: "activityFromDate",
        format: "%m/%d/%Y"
    });
    $("#disputeToDateCalendar").insertFromCalendar({
        inputField: "activityToDate",
        format: "%m/%d/%Y"
    });
}

function init(tabName) {
    if (tabName === "statement") {
        initStatement();
    } else if (tabName === "aging") {
        initAging();
    } else if (tabName === "dso") {
        initDso();
    } else if (tabName === "notes") {
        initNotes();
    } else if (tabName === "noCredit") {
        initNoCredit();
    } else if (tabName === "activity") {
        initActivity();
    } else if(tabName === "dispute") {
        initDispute();
    }
}

function changeFilter(obj) {
    if ((obj).is(":checked")) {
        $("#statementCreditStatement").removeAttr("checked");
        $("#statementCreditInvoice").removeAttr("checked");
        $("#statementNetsett[value='N']").attr("checked", "checked");
        $("#statementAgents[value='N']").attr("checked", "checked");
    }
}

function validateStatement() {
    if ($.trim($("#statementCustomerName").val()) === ""
            && !$("#statementAllCustomers").is(":checked") && $.trim($("#statementCollector").val()) === "") {
        $.prompt("Please select customer or all customer or collector.", {
            callback: function() {
                $("#customerName").callFocus();
            }
        });
        return false;
    } else {
        return true;
    }
}

function validateAging() {
    if ($.trim($("#agingCustomerName").val()) === ""
            && !$("#agingAllCustomers").is(":checked") && $.trim($("#agingCollector").val()) === ""
            && $.trim($("#salesManagers").val()) === "" && $.trim($("#terminalManagers").val()) === "") {
        $.prompt("Please select atleast one of customer or all customer or collector or sales manager or terminal manager.", {
            callback: function() {
                $("#agingCustomerName").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#agingCutOffDate").val()) === "") {
        $.prompt("Please enter cut-off date.", {
            callback: function() {
                $("#agingCutOffDate").callFocus();
            }
        });
        return false;
    } else {
        return true;
    }
}

function validateDso() {
    if ($.trim($("#dsoFilter").val()) === "By Collector" && $.trim($("#dsoCollector").val()) === "") {
        $.prompt("Please select collector.", {
            callback: function() {
                $("#dsoCollector").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#dsoFilter").val()) === "By Customer" && $.trim($("#dsoCustomerName").val()) === "") {
        $.prompt("Please select customer", {
            callback: function() {
                $("#dsoCustomerName").callFocus();
            }
        });
        return false;
    } else {
        return true;
    }
}

function validateNotes() {
    if ($.trim($("#notesCustomerName").val()) === "" && $.trim($("#accountAssignedTo").val()) === "" && $.trim($("#notesEnteredBy").val()) === "") {
        $.prompt("Please select customer or account assigned to or notes entered by.", {
            callback: function() {
                $("#notesCustomerName").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#notesFromDate").val()) === "") {
        $.prompt("Please enter from date.", {
            callback: function() {
                $("#notesFromDate").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#notesToDate").val()) === "") {
        $.prompt("Please enter to date.", {
            callback: function() {
                $("#notesToDate").callFocus();
            }
        });
        return false;
    } else {
        return true;
    }
}

function validateNoCredit() {
    if ($.trim($("#noCreditFromDate").val()) === "") {
        $.prompt("Please select From date.", {
            callback: function() {
                $("#noCreditFromDate").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#noCreditToDate").val()) === "") {
        $.prompt("Please select To date.", {
            callback: function() {
                $("#noCreditToDate").callFocus();
            }
        });
        return false;
    } else {
        return true;
    }
}

function validateActivity() {
    if ($.trim($("#activityCustomerName").val()) === "" && !$("#activityAllCustomers").is(":checked") && $.trim($("#activityCollector").val()) === "") {
        $.prompt("Please select customer or all customer or collector.", {
            callback: function() {
                $("#activityCustomerName").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#activityFromDate").val()) === "") {
        $.prompt("Please select From date.", {
            callback: function() {
                $("#activityFromDate").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#activityToDate").val()) === "") {
        $.prompt("Please select To date.", {
            callback: function() {
                $("#activityToDate").callFocus();
            }
        });
        return false;
    } else {
        return true;
    }
}

function validateDispute() {
    if ($.trim($("#disputeCustomerName").val()) === "" && !$("#disputeAllCustomers").is(":checked") && $.trim($("#disputeCollector").val()) === "") {
        $.prompt("Please select customer or all customer or collector.", {
            callback: function() {
                $("#disputeCustomerName").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#disputeFromDate").val()) === "") {
        $.prompt("Please select From date.", {
            callback: function() {
                $("#disputeFromDate").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#disputeToDate").val()) === "") {
        $.prompt("Please select To date.", {
            callback: function() {
                $("#disputeToDate").callFocus();
            }
        });
        return false;
    } else {
        return true;
    }
}
function validate(tabName) {
    if (tabName === "statement") {
        return validateStatement();
    } else if (tabName === "aging") {
        return validateAging();
    } else if (tabName === "notes") {
        return validateNotes();
    } else if (tabName === "dso") {
        return validateDso();
    } else if (tabName === "noCredit") {
        return validateNoCredit();
    } else if (tabName === "activity") {
        return validateActivity();
    } else if (tabName === "dispute"){
        return validateDispute();
    } else {
        return true;
    }
}

function createPdf(tabName) {
    if (validate(tabName)) {
        var title;
        if (tabName === "statement") {
            title = "Customer Statement";
        } else if (tabName === "aging") {
            title = "AR Aging";
        } else if (tabName === "dso") {
            title = "DSO";
        } else if (tabName === "notes") {
            title = "Account Notes";
        } else if (tabName === "noCredit") {
            title = "No Credit report";
        } else if (tabName === "dispute") {
            title = "AR Dispute Report";
        }
        var url = path + "/arReports.do";
        var params = "action=createPdf&tabName=" + tabName + "&" + $("#" + tabName + " :input").serialize();
        showPreloading();
        $.ajax({
            type: 'POST',
            url: url,
            data: params,
            cache: false,
            success: function(fileName) {
                closePreloading();
                if ($.trim(fileName) !== "") {
                    printPdf(title, url, fileName);
                }
            },
            error: function(jqXHR) {
                closePreloading();
                $.prompt(jqXHR.responseText, {
                    top: "20%",
                    left: "35%",
                    width: "800px",
                    height: "300px",
                    overflowy: "auto"
                });
            }
        });
    }
}

function sendEmail() {
    if (validateStatement()) {
        var url = path + "/arReports.do";
        var params = "action=createPdf&tabName=statement&" + $("#statement :input").serialize();
        showPreloading();
        $.ajax({
            type: 'POST',
            url: url,
            data: params,
            cache: false,
            success: function(fileName) {
                closePreloading();
                url = path + "/sendEmail.do?emailOption=ArStatement";
                url += "&moduleName=ArStatement&buttonValue=CreateMailForArStatement&fileLocation=" + fileName;
                if (window.parent.parent.parent.homeForm) {
                    window.parent.parent.parent.GB_showCenter("Email", url, 420, 800);
                } else if (window.parent.parent.homeForm) {
                    window.parent.parent.GB_showCenter("Email", url, 420, 800);
                } else if (window.parent.parent.parent.arCreditHoldForm) {
                    window.parent.parent.parent.parent.parent.GB_showCenter("Email", url, 420, 800);
                } else {
                    window.parent.GB_showCenter("Email", url, 420, 800);
                }
            },
            error: function(jqXHR) {
                closePreloading();
                $.prompt(jqXHR.responseText, {
                    top: "20%",
                    left: "35%",
                    width: "800px",
                    height: "300px",
                    overflowy: "auto"
                });
            }
        });
    }
}

function printPdf(title, url, fileName) {
    url += "?action=printPdf&fileName=" + fileName;
    showLightBox(title, url);
}

function createExcel(tabName) {
    if (validate(tabName)) {
        var url = path + "/arReports.do";
        var params = "action=createExcel&tabName=" + tabName + "&" + $("#" + tabName + " :input").serialize();
        showPreloading();
        $.ajax({
            type: 'POST',
            url: url,
            data: params,
            cache: false,
            success: function(fileName) {
                closePreloading();
                if ($.trim(fileName) !== "") {
                    exportExcel(url, fileName);
                }
            },
            error: function(jqXHR) {
                closePreloading();
                $.prompt(jqXHR.responseText, {
                    top: "20%",
                    left: "35%",
                    width: "800px",
                    height: "300px",
                    overflowy: "auto"
                });
            }
        });
    }
}

function createConfigurationExcel() {
    var url = path + "/arReports.do";
    showPreloading();
    $.ajax({
        type: 'POST',
        url: url,
        data: {
            action: "createConfigurationExcel"
        },
        cache: false,
        success: function(fileName) {
            closePreloading();
            exportExcel(url, fileName);
        },
        error: function(jqXHR) {
            closePreloading();
            $.prompt(jqXHR.responseText, {
                top: "20%",
                left: "35%",
                width: "800px",
                height: "300px",
                overflowy: "auto"
            });
        }
    });
}

function createExemptfromAutoHoldExcel() {
    var url = path + "/arReports.do";
    showPreloading();
    $.ajax({
        type: 'POST',
        url: url,
        data: {
            action: "createExemptfromAutoHoldExcel"
        },
        cache: false,
        success: function(fileName) {
            closePreloading();
            if ($.trim(fileName) !== "") {
                exportExcel(url, fileName);
            }
        },
        error: function(jqXHR) {
            closePreloading();
            $.prompt(jqXHR.responseText, {
                top: "20%",
                left: "35%",
                width: "800px",
                height: "300px",
                overflowy: "auto"
            });
        }
    });
}

function exportExcel(url, fileName) {
    var src = url + "?action=exportExcel&fileName=" + fileName;
    $("<iframe />", {
        name: "iframe",
        id: "iframe",
        src: src
    }).appendTo("body").hide();
}

function initTabs() {
    $("ul.htabs").tabs("> .pane", {
        effect: "fade",
        current: "selected",
        initialIndex: 0,
        onClick: function() {
            var index = $("ul.htabs li.selected").find("a").attr("tabindex");
            var tabName = $(".pane").eq(index).attr("id");
            if ($.trim($("#" + tabName).html()) === "") {
                showPreloading();
                var url = path + "/arReports.do";
                $.ajax({
                    type: 'POST',
                    url: url,
                    data: {
                        action: "show",
                        tabName: tabName
                    },
                    cache: false,
                    success: function(data) {
                        $("#" + tabName).html(data);
                        closePreloading();
                        init(tabName);
                    },
                    error: function(jqXHR) {
                        closePreloading();
                        $.prompt(jqXHR.responseText, {
                            top: "20%",
                            left: "35%",
                            width: "800px",
                            height: "300px"
                        });
                    }
                });
            }
        }
    });
}

$(document).ready(function() {
    closePreloading();
    initTabs();
});

function showLightBox(title, url) {
    if (window.parent.parent.parent.homeForm) {
        window.parent.parent.parent.showLightBox(title, url);
    } else if (window.parent.parent.homeForm) {
        window.parent.parent.showLightBox(title, url);
    } else if (window.parent.parent.parent.arCreditHoldForm) {
        window.parent.parent.parent.parent.parent.showLightBox(title, url);
    } else {
        window.parent.showLightBox(title, url);
    }
}