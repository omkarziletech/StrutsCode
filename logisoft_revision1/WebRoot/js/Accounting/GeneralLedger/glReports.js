/* 
 * Document   : glReports
 * Created on : Jun 12, 2012, 12:34:35 PM
 * Author     : Lakshmi Naryanan
 */

var currentTab = "chargeCode";

function callServer(data, preloading, success, error, url) {
    url = url || path + "/glReports.do";
    ajaxCall(url, {
        data: data,
        preloading: preloading,
        success: success,
        error: error,
        async: false
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

function initDates(tabName) {
    $("#fromDateCalendar" + tabName).insertFromCalendar({
        inputField: "fromDate" + tabName,
        format: "%m/%d/%Y"
    });
    $("#toDateCalendar" + tabName).insertFromCalendar({
        inputField: "toDate" + tabName,
        format: "%m/%d/%Y"
    });
}

function initChargeCode() {
    $("#chargeCode").find("#chargeCodeId").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=CHARGE_CODE&template=chargeCode&fieldIndex=1&",
        width: "420px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300,
        focusField: "fromDatechargeCode"
    });
    initDates("chargeCode");
}

function initGlCode() {
    $("#glCode").find("#glAccount").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=GL_ACCOUNT&template=glAccount&fieldIndex=1&tabName=GL_REPORT&",
        width: "350px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300,
        focusField: "fromDateglCode"
    });
    initDates("glCode");
}

function initCash() {
    $("#reportingDateCalendar").insertFromCalendar({
        inputField: "reportingDate",
        format: "%m/%d/%Y"
    });
}

function initFclPl() {
    initDates("fclPl");
    $("#fclPl").find("#reportType").change(function() {
        if ($(this).val() === "Show all items by gl period") {
            $("#fromDatefclPl").addClass("readonly").attr("readonly", true).val("");
            $("#toDatefclPl").addClass("readonly").attr("readonly", true).val("");
        } else {
            $("#fromDatefclPl").removeClass("readonly").removeAttr("readonly").val("");
            $("#toDatefclPl").removeClass("readonly").removeAttr("readonly").val("");
        }
        $("#fclPl").find("#glPeriod"), val("").callFocus();
    });
    $("#fclPl").find("#glPeriod").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=GL_PERIOD&template=glPeriod&fieldIndex=1,3,4&",
        width: "350px",
        otherFields: "fromDatefclPl^toDatefclPl",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300
    });
    $("#fclPl").find("#issuingTerminal").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=ISSUING_TERMINAL&fieldIndex=1&",
        width: "420px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300,
        focusField: "pod"
    });
    $("#fclPl").find("#pod").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=POD&fieldIndex=1&",
        width: "420px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300
    });
}

function initEcuMapping() {
    var url = path + "/autocompleter/action/getAutocompleterResults.jsp?query=ECU_ACCOUNT_MAPPING&template=ecuAccountMapping&fieldIndex=1&";
    $("#ecuMapping").find("#reportCategory").initautocomplete({
        url: url,
        width: "350px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300,
        focusField: "fromDateglCode"
    });
    initDates("ecuMapping");
}

function init(data) {
    $("#" + currentTab).html(data);
    if (currentTab === "chargeCode") {
        initChargeCode();
    } else if (currentTab === "glCode") {
        initGlCode();
    } else if (currentTab === "cash") {
        initCash();
    } else if (currentTab === "fclPl") {
        initFclPl();
    } else {
        initEcuMapping();
    }
}

function initTabs() {
    $("ul.htabs").tabs("> .pane", {
        effect: "fade",
        current: "selected",
        initialIndex: 0,
        onClick: function() {
            var index = $("ul.htabs li.selected").find("a").attr("tabindex");
            var tabName = $(".pane").eq(index).attr("id");
            currentTab = tabName;
            if ($.trim($("#" + tabName).html()) === "") {
                callServer({
                    action: "show",
                    tabName: tabName
                }, true, "init");
            }
        }
    });
}

function validateChargeCode() {
    if ($.trim($("#chargeCode").find("#chargeCodeId").val()) === "") {
        $.prompt("Please enter Charge Code.", {
            callback: function() {
                $("#chargeCode").find("#chargeCodeId").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#fromDatechargeCode").val()) === "") {
        $.prompt("Please enter From Date.", {
            callback: function() {
                $("#fromDatechargeCode").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#toDatechargeCode").val()) === "") {
        $.prompt("Please enter To Date.", {
            callback: function() {
                $("#toDatechargeCode").callFocus();
            }
        });
        return false;
    } else if (Date.parse($.trim($("#fromDatechargeCode").val())) > Date.parse($.trim($("#toDatechargeCode").val()))) {
        $.prompt("From Date should not be creater than To Date", {
            callback: function() {
                $("#fromDatechargeCode").callFocus();
            }
        });
        return false;
    } else {
        return true;
    }
}

function validateGlCode() {
    if ($.trim($("#glCode").find("#glAccount").val()) === "") {
        $.prompt("Please enter GL Account.", {
            callback: function() {
                $("#glCode").find("#glAccount").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#fromDateglCode").val()) === "") {
        $.prompt("Please enter From Date.", {
            callback: function() {
                $("#fromDateglCode").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#toDateglCode").val()) === "") {
        $.prompt("Please enter To Date.", {
            callback: function() {
                $("#toDateglCode").callFocus();
            }
        });
        return false;
    } else if (Date.parse($.trim($("#fromDateglCode").val())) > Date.parse($.trim($("#toDateglCode").val()))) {
        $.prompt("From Date should not be creater than To Date", {
            callback: function() {
                $("#fromDateglCode").callFocus();
            }
        });
        return false;
    } else {
        return true;
    }
}

function validateCash() {
    if ($.trim($("#reportingDate").val()) === "") {
        $.prompt("Please enter Reporting Date.", {
            callback: function() {
                $("#reportingDate").callFocus();
            }
        });
        return false;
    } else {
        return true;
    }
}

function validateFclPl() {
    if ($.trim($("#fromDatefclPl").val()) === "") {
        $.prompt("Please enter From Date.", {
            callback: function() {
                $("#fromDatefclPl").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#toDatefclPl").val()) === "") {
        $.prompt("Please enter To Date.", {
            callback: function() {
                $("#toDatefclPl").callFocus();
            }
        });
        return false;
    } else if (Date.parse($.trim($("#fromDatefclPl").val())) > Date.parse($.trim($("#toDatefclPl").val()))) {
        $.prompt("From Date should not be creater than To Date", {
            callback: function() {
                $("#fromDatefclPl").callFocus();
            }
        });
        return false;
    } else {
        return true;
    }
}

function validateEcuMapping() {
    if ($.trim($("#ecuMapping").find("#reportCategory").val()) === "") {
        $.prompt("Please enter ECU Account.", {
            callback: function() {
                $("#ecuMapping").find("#reportCategory").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#fromDateecuMapping").val()) === "") {
        $.prompt("Please enter From Date.", {
            callback: function() {
                $("#fromDateecuMapping").callFocus();
            }
        });
        return false;
    } else if ($.trim($("#toDateecuMapping").val()) === "") {
        $.prompt("Please enter To Date.", {
            callback: function() {
                $("#toDateecuMapping").callFocus();
            }
        });
        return false;
    } else if (Date.parse($.trim($("#fromDateecuMapping").val())) > Date.parse($.trim($("#toDateecuMapping").val()))) {
        $.prompt("From Date should not be creater than To Date", {
            callback: function() {
                $("#fromDateecuMapping").callFocus();
            }
        });
        return false;
    } else {
        return true;
    }
}
function validate(tabName) {
    if (tabName === "chargeCode") {
        return validateChargeCode();
    } else if (tabName === "glCode") {
        return validateGlCode();
    } else if (tabName === "cash") {
        return validateCash();
    } else if (tabName === "fclPl") {
        return validateFclPl();
    } else {
        return validateEcuMapping();
    }
}

function printPdf(fileName) {
    var title;
    if (currentTab === "chargeCode") {
        title = "Charge Code";
    } else if (currentTab === "glCode") {
        title = "GL Code";
    } else if (currentTab === "cash") {
        title = "CASH";
    } else if (currentTab === "fclPl") {
        title = "FCL P/L";
    } else {
        title = "ECU Account Mapping";
    }
    var url = path + "/servlet/FileViewerServlet?fileName=" + fileName;
    window.parent.showLightBox(title, url);
}

function createPdf(tabName) {
    if (validate(tabName)) {
        currentTab = tabName;
        var params = "action=createPdf&tabName=" + tabName + "&" + $("#" + tabName + " :input").serialize();
        callServer(params, true, "printPdf");
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

function createExcel(tabName) {
    if (validate(tabName)) {
        var params = "action=createExcel&tabName=" + tabName + "&" + $("#" + tabName + " :input").serialize();
        callServer(params, true, "exportExcel");
    }
}

function createMappingToExcel() {
    var params = "action=createExcel&tabName=ecuMapping&reportType=mapping";
    callServer(params, true, "exportExcel");
}

function refresh(tabName) {
    currentTab = tabName;
    callServer({
        action: "clear",
        tabName: tabName
    }, true, "init");
}

$(document).ready(function() {
    closePreloading();
    initTabs();
});