/* 
 * Document   : payment
 * Created on : Jun 24, 2015, 01:10:00 AM
 * Author     : Lakshmi Naryanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function setBankAccount() {
    $("#bankAccount").empty();
    $("#startingNumber").val("");
    if ($("#bankName").val() !== "") {
        $.ajaxx({
            url: path + "/apPayment.do",
            dataType: "json",
            data: {
                action: "getBankAccount",
                dataType: "json",
                bankName: $("#bankName").val()
            },
            preloading: true,
            success: function (data) {
                var options = [];
                if (data.length > 1) {
                    options.push("<option value=''>SELECT BANK ACCOUNT</option>");
                }
                $.each(data, function (index) {
                    options.push("<option value='" + data[index].value + "'>" + ($.trim(data[index].label) !== "" ? data[index].label : data[index].value) + "</option>");
                });
                $("#bankAccount").html(options.join(''));
                setStartingNumber();
            }
        });
    } else {
        $("#bankAccount").html("<option value=''>SELECT BANK ACCOUNT</option>");
    }
}

function setStartingNumber() {
    $("#startingNumber").val("");
    if ($("#bankAccount").val() !== "") {
        $.ajaxx({
            url: path + "/apPayment.do",
            data: {
                action: "getStartingNumber",
                bankName: $("#bankName").val(),
                bankAccount: $("#bankAccount").val()
            },
            preloading: true,
            success: function (data) {
                $("#startingNumber").val(data);
            }
        });
    }
}

function search() {
    $("#payment-list").html("");
    $("#action").val("search");
    $("#apPaymentForm").submit();
}

function clearAll() {
    $("#action").val("clearAll");
    $("#apPaymentForm").submit();
}

function showInvoices(ids, writeMode, vendorNumber) {
    $.ajaxx({
        url: path + "/apPayment.do",
        data: {
            ids: ids,
            writeMode: writeMode,
            action: "showInvoices"
        },
        preloading: true,
        success: function (data) {
            if ($.trim(data) !== "noresult") {
                var url = path + "?TB_html&height=400&width=800";
                Lightbox.showPopUp("Invoices for Vendor : " + vendorNumber, url, "sexylightbox", "", "Lightbox.close(); search();", data);
            }
        }
    });
}

function showNotes(moduleId, moduleRefId) {
    var url = path + "/notes.do?";
    url += "moduleId=" + encodeURIComponent(moduleId);
    url += "&moduleRefId=" + encodeURIComponent(moduleRefId);
    window.parent.showLightBox("Notes", url, 400, 800);
}

function removeInvoice(id, ids, writeMode) {
    $.ajaxx({
        url: path + "/apPayment.do",
        data: {
            id: id,
            ids: ids,
            writeMode: writeMode,
            action: "removeInvoice"
        },
        preloading: true,
        success: function (data) {
            if ($.trim(data) !== "noresult") {
                $("#invoice-list").parent().html(data);
            } else {
                Lightbox.close();
                search();
            }
        }
    });
}

function setApSpecialist() {
    if ($("#role").val() === "APSpecialist") {
        $("#vendor").setOptions({
            extraParams: {
                param1: $("#userId").val()
            }
        });
    }
}

function onChangePaymentDate() {
    if ($.trim($("#paymentDate").val()) !== "") {
        if ($("#bankAccount").val() === "") {
            $.prompt("Please select bank account", {
                callback: function () {
                    $("#paymentDate").val("");
                    $("#bankAccount").val("").callFocus();
                }
            });
        } else if (!isDate($("#paymentDate"))) {
            $.prompt("Please enter valid payment date", {
                callback: function () {
                    $("#paymentDate").val("").callFocus();
                }
            });
        } else {
            var paymentDate = $("#paymentDate").val();
            $.ajaxx({
                url: path + "/apPayment.do",
                data: {
                    action: "validatePaymentDate",
                    paymentDate: paymentDate,
                    bankName: $("#bankName").val(),
                    bankAccount: $("#bankAccount").val()
                },
                preloading: true,
                success: function (data) {
                    if (data === "Open") {
                        $(".paymentDate").each(function () {
                            $(this).val(paymentDate);
                        });
                    } else {
                        $.prompt(data, {
                            callback: function () {
                                $("#paymentDate").val("").callFocus();
                            }
                        });
                    }
                }
            });
        }
    }
}

function payAll(ele) {
    if ($(ele).is(":checked")) {
        $(".pay:enabled").each(function () {
            $(this).attr("checked", true);
            var row = $(this).parent().parent();
            var approve = row.find(".approve");
            if ($("#achApprover").val() === "true" && (row.find(".paymentMethod").val() === "ACH" || row.find(".paymentMethod").val() === "WIRE")) {
                approve.removeAttr("disabled");
                if ($(".approveAll").is(":checked")) {
                    approve.attr("checked", true);
                }
            } else {
                approve.removeAttr("checked").attr("disabled", true);
            }
        });
    } else {
        $(".pay:enabled").each(function () {
            var approve = $(this).parent().parent().find(".approve");
            $(this).removeAttr("checked");
            if (!approve.is(":disabled")) {
                approve.removeAttr("checked").attr("disabled", true);
            }
        });
        $(".approveAll").removeAttr("checked");
    }
}

function approveAll(ele) {
    if ($(ele).is(":checked")) {
        $(".approve:enabled").each(function () {
            $(this).attr("checked", true);
        });
    } else {
        $(".approve:enabled").each(function () {
            $(this).removeAttr("checked");
        });
    }
}

function validatePay(ele) {
    if ($("#achApprover").val() === "true" && ($(ele).val() === "ACH" || $(ele).val() === "WIRE")) {
        $(ele).parent().parent().find(".approve").removeAttr("disabled");
    } else {
        $(ele).parent().parent().find(".approve").removeAttr("checked").attr("disabled", true);
    }
}

function pay(ele) {
    if ($(ele).is(":checked") && $("#achApprover").val() === "true") {
        if ($(ele).parent().parent().find(".paymentMethod").val() === "ACH" || $(ele).parent().parent().find(".paymentMethod").val() === "WIRE") {
            $(ele).parent().parent().find(".approve").removeAttr("disabled");
        }
    } else {
        if ($(ele).parent().parent().find(".paymentMethod").val() === "ACH" || $(ele).parent().parent().find(".paymentMethod").val() === "WIRE") {
            $(ele).parent().parent().find(".approve").removeAttr("checked").attr("disabled", true);
        }
    }
}

function cancelBatch() {
    $(".button").removeAttr("disabled");
    Lightbox.close();
    var fields = ".vendorNumber,.vendorName,.paymentAmount,.paymentMethod,.paymentDate,.pay,.approve,.status,.ids";
    $("#payment-list").find(fields).removeAttr("name");
}

function savePayment() {
    $(".button").attr("disabled", true);
    $("#action").val("makePayment");
    $("#apPaymentForm").submit();
}

function showBatch() {
    $.ajaxx({
        url: path + "/apPayment.do",
        data: {
            action: "showBatch"
        },
        preloading: true,
        success: function (data) {
            var url = path + "?TB_html&height=100&width=400";
            Lightbox.showPopUp("Batch Details", url, "sexylightbox", "", "cancelBatch();", data);
        }
    });
}

function validatePayment() {
    $(".button").attr("disabled", true);
    if ($("#bankName").length <= 0 || ($("#bankName").val() === "" && $("#bankAccount").val() === "")) {
        $(".button").removeAttr("disabled");
        $.prompt("You do not have any bank account. Please add one.");
    } else if ($("#bankName").val() === "") {
        $(".button").removeAttr("disabled");
        $.prompt("Please select bank name", {
            callback: function () {
                $("#bankName").val("").callFocus();
            }
        });
    } else if ($("#bankAccount").val() === "") {
        $(".button").removeAttr("disabled");
        $.prompt("Please select bank account", {
            callback: function () {
                $("#bankAccount").val("").callFocus();
            }
        });
    } else {
        $.ajaxx({
            url: path + "/apPayment.do",
            data: {
                action: "validatePaymentDate",
                paymentDate: $("#paymentDate").val(),
                bankName: $("#bankName").val(),
                bankAccount: $("#bankAccount").val()
            },
            preloading: true,
            success: function (data) {
                if (data !== "Open") {
                    $(".button").removeAttr("disabled");
                    $.prompt(data, {
                        callback: function () {
                            $("#paymentDate").val("").callFocus();
                        }
                    });
                } else {
                    var index = 0;
                    var payCount = 0;
                    var unpayCount = 0;
                    var checkCount = 0;
                    var approveCount = 0;
                    var achCount = 0;
                    $(".pay:enabled").each(function () {
                        var row = $(this).parent().parent();
                        if ($(this).is(":checked") || (!$(this).is(":checked") && row.find(".status").val() === "WFA")) {
                            row.find(".vendorNumber").attr("name", "paymentList[" + index + "].vendorNumber");
                            row.find(".vendorName").attr("name", "paymentList[" + index + "].vendorName");
                            row.find(".paymentAmount").attr("name", "paymentList[" + index + "].paymentAmount");
                            row.find(".paymentMethod").attr("name", "paymentList[" + index + "].paymentMethod");
                            row.find(".paymentDate").attr("name", "paymentList[" + index + "].paymentDate");
                            row.find(".pay").attr("name", "paymentList[" + index + "].pay");
                            row.find(".approve").attr("name", "paymentList[" + index + "].approve");
                            row.find(".status").attr("name", "paymentList[" + index + "].status");
                            row.find(".ids").attr("name", "paymentList[" + index + "].ids");
                            if ($(this).is(":checked")) {
                                if (row.find(".paymentMethod").val() === "CHECK") {
                                    payCount++;
                                    checkCount++;
                                } else if (row.find(".paymentMethod").val() === "ACH" || row.find(".paymentMethod").val() === "WIRE") {
                                    if (row.find(".status").val() === "WFA"
                                            && !row.find(".approve").is(":disabled") && row.find(".approve").is(":checked")) {
                                        payCount++;
                                        approveCount++;
                                    } else if (row.find(".status").val() === "RP") {
                                        payCount++;
                                        if (row.find(".approve").is(":checked")) {
                                            approveCount++;
                                        } else {
                                            achCount++;
                                        }
                                    }
                                } else {
                                    payCount++;
                                }
                            } else if (!$(this).is(":checked") && row.find(".status").val() === "WFA") {
                                unpayCount++;
                            }
                            index++;
                        }
                    });
                    if (payCount === 0 && unpayCount === 0) {
                        $(".button").removeAttr("disabled");
                        $.prompt("Please select atleast one transaction to make payment.");
                    } else if (payCount === 0 && unpayCount > 0) {
                        savePayment();
                    } else if (payCount > 0) {
                        if (checkCount > 0) {
                            $.ajaxx({
                                url: path + "/apPayment.do",
                                data: {
                                    action: "validatePrinters",
                                    bankName: $("#bankName").val(),
                                    bankAccount: $("#bankAccount").val()
                                },
                                preloading: true,
                                success: function (data) {
                                    if (data.indexOf("No Check Printer") >= 0) {
                                        $(".button").removeAttr("disabled");
                                        $.prompt(data);
                                    } else if (data.indexOf("No Overflow Printer") >= 0) {
                                        $.prompt(data, {
                                            buttons: {
                                                Continue: true,
                                                Cancel: false
                                            },
                                            callback: function (v) {
                                                if (v) {
                                                    showBatch();
                                                } else {
                                                    $(".button").removeAttr("disabled");
                                                    var fields = ".vendorNumber,.vendorName,.paymentAmount,.paymentMethod,.paymentDate,.pay,.approve,.status,.ids";
                                                    $("#payment-list").find(fields).removeAttr("name");
                                                }
                                            }
                                        });
                                    } else {
                                        showBatch();
                                    }
                                }
                            });
                        } else if (approveCount > 0 || achCount === 0) {
                            showBatch();
                        } else if (achCount > 0) {
                            savePayment();
                        }
                    }
                }
            }
        });
    }
}

function setResultHeight() {
    if ($(".scrollable-table").length > 0) {
        var windowHeight = window.parent.getFrameHeight();
        var height = windowHeight;
        height -= 130;
        $(".scrollable-table").height(height);
        $("body").css("overflow-y", "hidden");
    }
}

$(document).ready(function () {
    $("#apPaymentForm").submit(function () {
        $.startPreloader();
    });
    if ($.inArray($("#role").val(), ["APSpecialist", "Admin", "Supervisor"]) >= 0) {
        var query = $("#role").val() === "APSpecialist" ? "VENDOR_FOR_AP_SPECIALIST" : "VENDOR_FOR_ADMIN_SUPERVISOR";
        $("#vendorName").initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=" + query + "&template=vendor&fieldIndex=1,2&",
            width: "480px",
            otherFields: "vendorNumber",
            resultsClass: "ac_results z-index",
            resultPosition: "absolute",
            scroll: true,
            scrollHeight: 300,
            callBefore: "setApSpecialist()",
            callback: "search();"
        });
    } else {
        $("#vendorName").blur(function () {
            $(this).val("");
            $("#vendorCheck").val("");
            $("#vendorNumber").val("");
        });
    }
    $("#paymentDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='Payment Date'/>",
        onClose: function () {
            onChangePaymentDate();
        }
    }).change(function () {
        onChangePaymentDate();
    });
    $("[title != '']").not("link").tooltip();
    Lightbox.initialize({
        color: 'black',
        dir: path + '/js/lightbox/images',
        moveDuration: 1,
        resizeDuration: 1,
        parent: "#apPaymentForm"
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