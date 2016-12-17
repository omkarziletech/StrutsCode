/* 
 *  Document   : accountPayable
 *  Created on : Dec 20, 2012, 3:28:08 PM
 *  Author     : Lakshmi Narayanan
 */

function search() {
    if ($.trim($("#fromDate").val()) != "" && $.trim($("#toDate").val()) != "" && isGreaterThan($("#fromDate"), $("#toDate"))) {
        $.prompt("From date cannot be greater than to date", {
            callback: function () {
                $("#fromDate").val("").callFocus();
            }
        });
    } else if ($.trim($("#vendorName").val()) == "" && $("#holdN").is(":checked") && $.trim($("#only").val()) == "") {
        $.prompt("Please enter vendor name", {
            callback: function () {
                $("#vendorName").val("").callFocus();
            }
        });
    } else {
        $("#selectedPage").val("1");
        $("#sortBy").val("vendor_number");
        $("#orderBy").val("asc");
        $("#action").val("search");
        $("#accountPayableForm").submit();
    }
}

function importExcel() {
    if ($.trim($("#file").val()) === "") {
        $.prompt("Please choose template to import");
    } else if ($.trim($("#vendorName").val()) === "") {
        $.prompt("Please enter vendor name", {
            callback: function () {
                $("#vendorName").val("").callFocus();
            }
        });
    }
    else {
        $("#selectedPage").val("1");              
        $("#action").val("importStatement");
        $("#accountPayableForm").submit();
    }
}
function gotoPage(pageNo) {
    $("#selectedPage").val(pageNo);
    $("#action").val("sortingAndPaging");
    $("#accountPayableForm").submit();
}

function gotoSelectedPage() {
    $("#selectedPage").val($("#selectedPageNo").val());
    $("#action").val("sortingAndPaging");
    $("#accountPayableForm").submit();
}

function doSort(sortBy) {
    $("#sortBy").val(sortBy);
    if ($("#orderBy").val() == "desc") {
        $("#orderBy").val("asc");
    } else {
        $("#orderBy").val("desc");
    }
    $("#action").val("sortingAndPaging");
    $("#accountPayableForm").submit();
}

function initVendor() {
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
}

function clearAll() {
    $("#action").val("clearAll");
    $("#accountPayableForm").submit();
}

function toggle(ele) {
    $("." + ele).slideToggle(function () {
        if (ele == "filter-container") {
            if ($("#toggled").val() == "true") {
                $("#toggled").val("false");
            } else {
                $("#toggled").val("true");
            }
        }
        setResultHeight();
    });
}

function fromDateChange() {
    if ($.trim($("#fromDate").val()) != "") {
        if (!isDate($("#fromDate"))) {
            $.prompt("Please enter valid from date", {
                callback: function () {
                    $("#fromDate").val("").callFocus();
                }
            });
        } else if (isGreaterThan($("#fromDate"), $("#toDate"))) {
            $.prompt("From date cannot be greater than to date", {
                callback: function () {
                    $("#fromDate").val("").callFocus();
                }
            });
        }
    }
}

function toDateChange() {
    if ($.trim($("#toDate").val()) != "") {
        if (!isDate($("#toDate"))) {
            $.prompt("Please enter valid to date", {
                callback: function () {
                    $("#toDate").val("").callFocus();
                }
            });
        } else if (isGreaterThan($("#fromDate"), $("#toDate"))) {
            $.prompt("To date cannot be lesser than from date", {
                callback: function () {
                    $("#toDate").val("").callFocus();
                }
            });
        }
    }
}

function initDate() {
    $("#fromDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='From Date'/>",
	onClose: function() {
            fromDateChange();
        }
    }).change(function() {
        fromDateChange();
    });
    $("#toDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='To Date'/>",
	onClose: function() {
            toDateChange();
        }
    }).change(function() {
        toDateChange();
    });
}

function clearFilters() {
    $("#action").val("clearFilters");
    $("#accountPayableForm").submit();
}

function setResultHeight() {
    if ($(".result-container").length > 0) {
        var windowHeight = $(window).height();
        var height = windowHeight;
        height -= $(".vendor-container").length > 0 && $(".vendor-container").css("display") == "block" ? $(".vendor-container").height() : 0;
        height -= $(".filter-container").length > 0 && $(".filter-container").css("display") == "block" ? $(".filter-container").height() : 0;
        height -= $(".table-banner").height();
        height -= 150;
        $(".result-container").height(height);
        $("body").css("overflow", "hidden");
    }
}

function setPayAll(ele) {
    if ($(ele).is(":checked")) {
        $(".pay:not(:disabled)").attr("checked", true);
        $(".hold").removeAttr("checked");
        $(".noDueUpon").removeAttr("checked");
        $(".delete").removeAttr("checked");
    } else {
        $(".pay").removeAttr("checked");
    }
}

function setPay(ele) {
    if ($(ele).is(":checked")) {
        var row = $(ele).parent().parent();
        row.find(".hold").removeAttr("checked");
        row.find(".delete").removeAttr("checked");
    }
}

function setHold(ele) {
    if ($(ele).is(":checked")) {
        var row = $(ele).parent().parent();
        row.find(".pay").removeAttr("checked");
        row.find(".noDueUpon").removeAttr("checked");
        row.find(".delete").removeAttr("checked");
    }
}

function setDelete(ele) {
    if ($(ele).is(":checked")) {
        var row = $(ele).parent().parent();
        row.find(".pay").removeAttr("checked");
        row.find(".noDueUpon").removeAttr("checked");
        row.find(".hold").removeAttr("checked");
    }
}

function showInvoiceDetails(vendorNumber, invoiceNumber, transactionType) {
    var url = path + "/apInquiry.do";
    url += "?action=showInvoiceDetails";
    url += "&vendorNumber=" + vendorNumber;
    url += "&invoiceNumber=" + encodeURIComponent(invoiceNumber);
    url += "&transactionType=" + transactionType;
    window.parent.showLightBox("Invoice Details - Vendor : " + vendorNumber + " Invoice : " + invoiceNumber, url, 400, 900);
}

function showNotes(noteModuleId, noteRefId) {
    var url = path + "/notes.do?";
    url += "moduleId=" + noteModuleId;
    url += "&moduleRefId=" + encodeURIComponent(noteRefId);
    window.parent.showLightBox("Notes", url, 400, 800);
}

function uploadInvoice(documentId) {
    var title = "Upload Invoice";
    var url = path + "/scan.do?screenName=INVOICE&documentId=" + encodeURIComponent(documentId);
    window.parent.showLightBox(title, url, 400, 800);
}

function showMessage(message) {
    $.prompt(message);
}

function save() {
    var payIds = new Array();
    var holdIds = new Array();
    var deleteIds = new Array();
    var vendorMap = {};
    $(".pay:checked,.noDueUpon:checked").each(function() {
        payIds.push($(this).val());
        var row = $(this).parent().parent();
        var amount = 0.00;
        var vendor = row.find(".vendors").val();
        amount += parseFloat(row.find(".invoiceAmount").val());
        if (vendor in vendorMap) {
            amount += parseFloat(vendorMap[vendor]);
        }
        vendorMap[vendor] = amount;
    });
    $(".hold:checked").each(function() {
        holdIds.push($(this).val());
    });
    $(".delete:checked").each(function() {
        deleteIds.push($(this).val());
    });
    if (payIds.length > 0) {
        var vendors = new Array();
	$.each(vendorMap, function(key, value) {
            if (value < 0.00) {
                vendors.push(key);
            }
        });
        if (vendors.length > 0) {
            var warning = "Balance on selected items must be positive.<br/>";
            warning += "<ul>";
	    $(vendors).each(function() {
                warning += "<li>" + this + "</li>";
            });
            warning += "</ul>";
            $.prompt(warning);
        } else {
            $("#action").val("save");
            $("#payIds").val(payIds);
            $("#holdIds").val(holdIds);
            $("#deleteIds").val(deleteIds);
            $("#accountPayableForm").submit();
        }
    } else if (holdIds.length > 0 || deleteIds.length > 0) {
        $("#action").val("save");
        $("#payIds").val(payIds);
        $("#holdIds").val(holdIds);
        $("#deleteIds").val(deleteIds);
        $("#accountPayableForm").submit();
    }
}

$(document).ready(function () {
    $("#accountPayableForm").submit(function () {
        showPreloading();
    });
    $("#file").fileInput();
    setResultHeight();
    $(window).resize(function () {
        window.parent.changeHeight();
        setResultHeight();
    });
    initDate();
    initVendor();
    $("[title != '']").not("link").tooltip();
    $(document).keypress(function (event) {
        if ($("#" + event.target.id).length <= 0
                || (event.target.id != "jqi_state0_buttonOk" && event.target.id != "jqi_state0_buttonYes" && event.target.id != "jqi_state0_buttonNo")) {
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode == 13) {
                event.preventDefault();
                search();
            }
        }
    });
});