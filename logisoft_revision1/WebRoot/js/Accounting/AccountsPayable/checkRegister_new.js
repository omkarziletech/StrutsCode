/* 
 * Document   : checkRegister
 * Created on : July 09, 2015, 10:53:00 PM
 * Author     : Lakshmi Naryanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function search() {
    if ($.trim($("#glAccount").val()) === "" &&
            $.trim($("#fromCheckNumber").val()) === "" && $.trim($("#toCheckNumber").val()) === ""
            && $.trim($("#vendorNumber").val()) === "" && $.trim($("#paymentAmount").val()) === ""
            && $.trim($("#reconcileDate").val()) === "" && $.trim($("#batchId").val()) === "") {
        $.prompt("Please enter gl account or any one of the filters.");
    } else {
        $("#action").val("search");
        $("#checkRegisterForm").submit();
    }
}

function clearAll() {
    $("#action").val("clearAll");
    $("#checkRegisterForm").submit();
}

function save() {
    var voidCount = 0;
    $(".voided:enabled:checked").each(function () {
        $(this).attr("name", "paymentList[" + voidCount + "].voided").val("true");
        var row = $(this).parent().parent();
        row.find(".reprinted").attr("name", "paymentList[" + voidCount + "].reprinted").removeAttr("disabled").val("false");
        row.find(".batchId").attr("name", "paymentList[" + voidCount + "].batchId");
        row.find(".checkNumber").attr("name", "paymentList[" + voidCount + "].checkNumber");
        row.find(".vendorName").attr("name", "paymentList[" + voidCount + "].vendorName");
        row.find(".vendorNumber").attr("name", "paymentList[" + voidCount + "].vendorNumber");
        row.find(".paymentAmount").attr("name", "paymentList[" + voidCount + "].paymentAmount");
        row.find(".paymentMethod").attr("name", "paymentList[" + voidCount + "].paymentMethod");
        row.find(".paymentDate").attr("name", "paymentList[" + voidCount + "].paymentDate");
        row.find(".glAccount").attr("name", "paymentList[" + voidCount + "].glAccount");
        row.find(".bankName").attr("name", "paymentList[" + voidCount + "].bankName");
        row.find(".bankAccount").attr("name", "paymentList[" + voidCount + "].bankAccount");
        row.find(".ids").attr("name", "paymentList[" + voidCount + "].ids");
        voidCount++;
    });
    var reprintCount = 0;
    $(".reprinted:enabled:checked").each(function () {
        $(this).attr("name", "paymentList[" + reprintCount + "].reprinted").val("true");
        var row = $(this).parent().parent();
        row.find(".voided").attr("name", "paymentList[" + reprintCount + "].voided").removeAttr("disabled").val("false");
        row.find(".batchId").attr("name", "paymentList[" + reprintCount + "].batchId");
        row.find(".checkNumber").attr("name", "paymentList[" + reprintCount + "].checkNumber");
        row.find(".vendorName").attr("name", "paymentList[" + reprintCount + "].vendorName");
        row.find(".vendorNumber").attr("name", "paymentList[" + reprintCount + "].vendorNumber");
        row.find(".paymentAmount").attr("name", "paymentList[" + reprintCount + "].paymentAmount");
        row.find(".paymentMethod").attr("name", "paymentList[" + reprintCount + "].paymentMethod");
        row.find(".paymentDate").attr("name", "paymentList[" + reprintCount + "].paymentDate");
        row.find(".glAccount").attr("name", "paymentList[" + reprintCount + "].glAccount");
        row.find(".bankName").attr("name", "paymentList[" + reprintCount + "].bankName");
        row.find(".bankAccount").attr("name", "paymentList[" + reprintCount + "].bankAccount");
        row.find(".ids").attr("name", "paymentList[" + reprintCount + "].ids");
        reprintCount++;
    });
    if (voidCount === 0 && reprintCount === 0) {
        $.prompt("Please select atleast one transaction to void or reprint.");
    } else {
        $("#action").val("save");
        $("#checkRegisterForm").submit();
    }
}

function voided(ele) {
    if ($(ele).is(":checked")) {
        $(ele).parent().parent().find(".reprinted").removeAttr("checked");
    }
}

function reprinted(ele) {
    if ($(ele).is(":checked")) {
        $(ele).parent().parent().find(".voided").removeAttr("checked");
    }
}

function createReport(ids, vendorName, vendorNumber, paymentMethod, paymentDate, checkNumber) {
    $.ajaxx({
        url: path + "/checkRegister.do",
        data: {
            ids: ids,
            vendorName: vendorName,
            vendorNumber: vendorNumber,
            paymentMethod: paymentMethod,
            paymentDate: paymentDate,
            checkNumber: checkNumber,
            action: "createReport"
        },
        preloading: true,
        success: function (fileName) {
            var url = path + "/servlet/FileViewerServlet?fileName=" + fileName;
            window.parent.showLightBox("Payment Details Report", url);
        }
    });
}

function createExcel(ids, vendorName, vendorNumber, paymentMethod, paymentDate, checkNumber) {
    $.ajaxx({
        url: path + "/checkRegister.do",
        data: {
            ids: ids,
            vendorName: vendorName,
            vendorNumber: vendorNumber,
            paymentMethod: paymentMethod,
            paymentDate: paymentDate,
            checkNumber: checkNumber,
            action: "createExcel"
        },
        preloading: true,
        success: function (fileName) {
            var url = path + "/servlet/FileViewerServlet?fileName=" + fileName;
            var iframe = $("<iframe/>", {
                name: "iframe",
                id: "iframe",
                src: url
            }).appendTo("body").hide();
            iframe.load(function () {
                iframe.remove();
            });
        }
    });
}


function showInvoiceDetails(ids, vendorName, vendorNumber, paymentMethod, paymentDate, checkNumber, writeMode) {
    $.ajaxx({
        url: path + "/checkRegister.do",
        data: {
            ids: ids,
            vendorName: vendorName,
            vendorNumber: vendorNumber,
            paymentMethod: paymentMethod,
            paymentDate: paymentDate,
            checkNumber: checkNumber,
            writeMode: writeMode,
            action: "showInvoiceDetails"
        },
        preloading: true,
        success: function (data) {
            var url = path + "?TB_html&height=400&width=800";
            Lightbox.showPopUp("Payment Details", url, "sexylightbox", "", "", data);
        }
    });
}

function showNotes(moduleId, moduleRefId) {
    var url = path + "/notes.do?";
    url += "moduleId=" + encodeURIComponent(moduleId);
    url += "&moduleRefId=" + encodeURIComponent(moduleRefId);
    window.parent.showLightBox("Notes", url, 400, 800);
}

function setResultHeight() {
    if ($(".scrollable-table").length > 0) {
        var windowHeight = window.parent.getFrameHeight();
        var height = windowHeight;
        height -= 230;
        $(".scrollable-table").height(height);
        $("body").css("overflow-y", "hidden");
    }
}

function initAutocompleteFields() {
    $("#vendorName").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=VENDOR&template=vendor&fieldIndex=1,2&",
        width: "480px",
        otherFields: "vendorNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 300,
        callback: "search();"
    });
    $("#glAccount").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=RECONCILE&template=reconcile&fieldIndex=1,3&",
        width: "480px",
        otherFields: "bankAccount",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 300,
        callback: "search();"
    });
}

function onChangeDate(fieldId, fieldName) {
    if ($.trim($("#" + fieldId).val()) !== "" && !isDate($("#" + fieldId))) {
        $.prompt("Please enter valid " + fieldName, {
            callback: function () {
                $("#" + fieldId).val("").callFocus();
            }
        });
    }
}

function initDateFields() {
    $("#reconcileDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='Reconcile Date'/>",
        onClose: function () {
            onChangeDate("reconcileDate", "reconcile date");
        }
    }).change(function () {
        onChangeDate("reconcileDate", "reconcile date");
    });
    $("#fromDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='From Date'/>",
        onClose: function () {
            onChangeDate("fromDate", "from date");
        }
    }).change(function () {
        onChangeDate("fromDate", "from date");
    });
    $("#toDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='To Date'/>",
        onClose: function () {
            onChangeDate("toDate", "to date");
        }
    }).change(function () {
        onChangeDate("toDate", "to date");
    });
}

$(document).ready(function () {
    $("#checkRegisterForm").submit(function () {
        $.startPreloader();
    });
    initAutocompleteFields();
    initDateFields();
    $("[title != '']").not("link").tooltip();
    Lightbox.initialize({
        color: 'black',
        dir: path + '/js/lightbox/images',
        moveDuration: 1,
        resizeDuration: 1,
        parent: "#checkRegisterForm"
    });
    $(document).keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode === 13) {
            search();
        }
    });
    setResultHeight();
    $(window).resize(function () {
        window.parent.changeHeight();
        setResultHeight();
    });
});