var path = "/" + window.location.pathname.split('/')[1];

function showLightBox(title, url, height, width) {
    height = height || $(document).height() - 50;
    width = width || $(document).width() - 100;
    url += (url.indexOf("?") > -1 ? "&" : "?") + "TB_iframe&height=" + height + "&width=" + width;
    Lightbox.showPopUp(title, url, "sexylightbox", "");
}

function showMessage(message) {
    if (message.indexOf("No") === 0 || message.indexOf("Please") === 0) {
        $(".message").html(message).addClass("error").removeClass("message");
    } else {
        $(".message").html(message).addClass("message").removeClass("error");
    }
    setResultHeight();
}

function setCustomerOptions() {
    var url = path + "/autocompleter/action/getAutocompleterResults.jsp?query=";
    if ($("#disabled").is(":checked") && $("#showAllAccounts").is(":checked")) {
        url += "CUSTOMER_DISABLED";
    } else if ($("#showAllAccounts").is(":checked")) {
        url += "CUSTOMER";
    } else if ($("#disabled").is(":checked")) {
        url += "OPEN_AR_CUSTOMER_DISABLED";
    } else {
        url += "OPEN_AR_CUSTOMER";
    }
    url += "&template=customer&fieldIndex=1,2&";
    $("#customerName").setOptions({
        url: url
    });
    $("#customerName").val("").callFocus();
    $("#customerNameCheck").val("");
    $("#customerNumber").val("");
}

function initAutocompleteFields() {
    $("#customerName").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=OPEN_AR_CUSTOMER&template=customer&fieldIndex=1,2&",
        width: "480px",
        otherFields: "customerNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 300,
        callback: "search();"
    });
    $("#disabled,#showAllAccounts").click(function () {
        setCustomerOptions();
    });
    $(".newCustomerName").each(function (index) {
        $(this).initautocomplete({
            url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=CUSTOMER_DISABLED&template=customer&fieldIndex=1,2&",
            width: "480px",
            otherFields: "newCustomerNumber",
            row: true,
            checkClass: "newCustomerNameCheck",
            resultsClass: "ac_results z-index",
            resultPosition: "absolute",
            scroll: true,
            scrollHeight: 300,
            callback: "updateCustomer(" + index + ");"
        });
    });
}

function toggle(ele) {
    $("." + ele).slideToggle(function () {
        if (ele === "filter-container") {
            if ($("#toggled").val() === "true") {
                $("#toggled").val("false");
            } else {
                $("#toggled").val("true");
            }
        }
        setResultHeight();
    });
}

function setApAndAcFilters() {
    if ($("#searchFilter4").is(":checked") || $("#searchFilter5").is(":checked")
            || $("#searchDate1").is(":checked") || $("#nsInvoiceOnly").is(":checked")) {
        $("#showFilters6").attr("disabled", true).addClass("disabled").removeAttr("checked");
        $(".show-filter-switch:eq(5)").addClass('false disabled').removeClass('true');
        $("#showFilters7").attr("disabled", true).addClass("disabled").removeAttr("checked");
        $(".show-filter-switch:eq(6)").addClass('false disabled').removeClass('true');
    } else {
        $("#showFilters6").removeAttr("disabled").removeClass("disabled");
        $(".show-filter-switch:eq(5)").removeClass('disabled');
        $("#showFilters7").removeAttr("disabled").removeClass("disabled");
        $(".show-filter-switch:eq(6)").removeClass('disabled');
    }
}

function searchFilterChange(ele) {
    var index = $(".search-filter").index(ele);
    if ($(ele).is(":checked")) {
        $(".search-filter:eq(" + index + ")").attr("checked", true);
        $(".search-filter").not(":eq(" + index + ")").removeAttr("checked");
        $(".search-value:eq(" + index + ")").css("visibility", "visible").find(".textbox:first").callFocus();
        $(".search-value").not(":eq(" + index + ")").css("visibility", "hidden");
        $(".search-filter-switch:eq(" + index + ")").addClass('true').removeClass('false');
        $(".search-filter-switch").not(":eq(" + index + ")").addClass('false').removeClass('true');
    } else {
        $(".search-filter").removeAttr("checked");
        $(".search-value").css("visibility", "hidden");
        $(".search-filter-switch").addClass('false').removeClass('true');
    }
    if ($(ele).val() === 'Check Number' || $(ele).val() === 'Check Amount') {
        setApAndAcFilters();
    }
}

function initSearchFilters() {
    $(".search-filter").jswitch({
        afterContent: "ON",
        beforeContent: "OFF",
        css: {
            width: "40px",
            margin: "0 10px 0 0"
        },
        styleClass: "search-filter-switch",
        callback: function ($input) {
            searchFilterChange($input);
        }
    });
    $(".fromAmount,.toAmount").keydown(function (event) {
        allowOnlyNumbers(event, this);
    });
    $(".fromAmount,.toAmount").keyup(function (event) {
        validateAmount(event, this);
    });
    $(".fromAmount,.toAmount").change(function () {
        var $ele = $(this);
        if (isNaN($ele.val())) {
            $.prompt("Please enter valid amount", {
                callback: function () {
                    $ele.val("0.00").callFocus();
                }
            });
        } else {
            $ele.val(Number($ele.val()).toFixed(2));
        }
    });
}

function dateChange(ele1, ele2) {
    if ($.trim($(ele1).val()) !== "") {
        if (!isDate($(ele1))) {
            $.prompt("Please enter valid from date", {
                callback: function () {
                    $(ele1).val("").callFocus();
                }
            });
        } else if (ele1 === "#fromDate" && isGreaterThan($(ele1), $(ele2))) {
            $.prompt("From date cannot be greater than to date", {
                callback: function () {
                    $(ele1).val("").callFocus();
                }
            });
        } else if (ele1 === "#toDate" && isGreaterThan($(ele2), $(ele1))) {
            $.prompt("To date cannot be lesser than from date", {
                callback: function () {
                    $(ele1).val("").callFocus();
                }
            });
        }
    }
}

function dateFilterChange(ele) {
    var index = $(".date-filter").index(ele);
    if ($(ele).is(":checked")) {
        $(".date-filter:eq(" + index + ")").attr("checked", true);
        $(".date-filter").not(":eq(" + index + ")").removeAttr("checked");
        $(".date-filter-switch:eq(" + index + ")").addClass('true').removeClass('false');
        $(".date-filter-switch").not(":eq(" + index + ")").addClass('false').removeClass('true');
    } else {
        $(".date-filter:eq(" + index + ")").removeAttr("checked");
        $(".date-filter").not(":eq(" + index + ")").attr("checked", true);
        $(".date-filter-switch:eq(" + index + ")").addClass('false').removeClass('true');
        $(".date-filter-switch").not(":eq(" + index + ")").addClass('true').removeClass('false');
    }
}

function initDateFields() {
    $(".date-filter").jswitch({
        afterContent: "ON",
        beforeContent: "OFF",
        css: {
            width: "40px",
            margin: "0 10px 0 0"
        },
        styleClass: "date-filter-switch",
        callback: function ($input) {
            dateFilterChange($input);
        }
    });
    $("#fromDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='From Date'/>",
        onClose: function () {
            dateChange("#fromDate", "#toDate");
        }
    }).change(function () {
        dateChange("#fromDate", "#toDate");
    });
    $("#toDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='To Date'/>",
        onClose: function () {
            dateChange("#toDate", "#fromDate");
        }
    }).change(function () {
        dateChange("#toDate", "#fromDate");
    });
    $("input[name='searchDate']").click(function () {
        setApAndAcFilters();
    });
}

function showFilterChange(ele) {
    var index = $(".show-filter").index(ele);
    if ($(ele).is(":checked")) {
        $(ele).attr("checked", true);
        if ($(ele).val() === 'NS Invoices') {
            $("#nsInvoiceOnly").removeAttr("disabled").removeClass("disabled");
            $(".ns-invoice-switch").removeClass('disabled');
        }
        $(".show-filter-switch:eq(" + index + ")").addClass('true').removeClass('false');
    } else {
        $(ele).removeAttr("checked");
        if ($(ele).val() === 'NS Invoices') {
            $("#nsInvoiceOnly").attr("disabled", true).addClass("disabled").removeAttr("checked");
            $(".ns-invoice-switch").addClass('false disabled').removeClass('true');
            setApAndAcFilters();
        }
        $(".show-filter-switch:eq(" + index + ")").addClass('false').removeClass('true');
        if ($(ele).val() === "Open Invoices" && !$("#showFilters2").is(":checked")) {
            $("#showFilters2").attr("checked", true);
            $(".show-filter-switch:eq(" + (index + 1) + ")").addClass('true').removeClass('false');
        } else if ($(ele).val() === "Paid Invoices" && !$("#showFilters1").is(":checked")) {
            $("#showFilters1").attr("checked", true);
            $(".show-filter-switch:eq(" + (index - 1) + ")").addClass('true').removeClass('false');
        } else if ($(ele).val() === "Credit Hold - Yes" && !$("#showFilters5").is(":checked")) {
            $("#showFilters5").attr("checked", true);
            $(".show-filter-switch:eq(" + (index + 1) + ")").addClass('true').removeClass('false');
        } else if ($(ele).val() === "Credit Hold - No" && !$("#showFilters4").is(":checked")) {
            $("#showFilters4").attr("checked", true);
            $(".show-filter-switch:eq(" + (index - 1) + ")").addClass('true').removeClass('false');
        }
    }
}

function initShowFilters() {
    $(".show-filter").jswitch({
        afterContent: "SHOW",
        beforeContent: "HIDE",
        css: {
            width: "50px",
            margin: "0 10px 0 0"
        },
        styleClass: "show-filter-switch",
        callback: function ($input) {
            showFilterChange($input);
        }
    });
    if (!$("#showFilters3").is(":checked")) {
        $("#nsInvoiceOnly").attr("disabled", true).addClass("disabled");
    }
    $("#nsInvoiceOnly").jswitch({
        afterContent: "ONLY",
        beforeContent: "YES",
        css: {
            width: "50px",
            margin: "0 10px 0 0"
        },
        styleClass: "ns-invoice-switch",
        callback: function ($input) {
            setApAndAcFilters($input);
        }
    });
}

function setResultHeight() {
    if ($(".result-container").length > 0) {
        var windowHeight = window.parent.getFrameHeight();
        var height = windowHeight;
        height -= $(".customer-container").length > 0 && $(".customer-container").css("display") === "block" ? $(".customer-container").height() : 0;
        height -= $(".filter-container").length > 0 && $(".filter-container").css("display") === "block" ? $(".filter-container").height() : 0;
        height -= $(".table-banner").height();
        height -= 150;
        $(".result-container").height(height);
        $("body").css("overflow-y", "hidden");
    }
}

function search() {
    if ($("#searchFilter1").is(":checked") && $.trim($("#searchValue1").val()) === "") {
        $.prompt("Please enter the value for Invoice/BL filter", {
            callback: function () {
                $("#searchValue1").callFocus();
            }
        });
    } else if ($("#searchFilter2").is(":checked") && $.trim($("#searchValue2").val()) === "") {
        $.prompt("Please enter the value for Dock Receipt filter", {
            callback: function () {
                $("#searchValue2").callFocus();
            }
        });
    } else if ($("#searchFilter3").is(":checked") && $.trim($("#searchValue3").val()) === "") {
        $.prompt("Please enter the value for Container Number filter", {
            callback: function () {
                $("#searchValue3").callFocus();
            }
        });
    } else if ($("#searchFilter4").is(":checked") && $.trim($("#searchValue4").val()) === "") {
        $.prompt("Please enter the value for Voyage Number filter", {
            callback: function () {
                $("#searchValue4").callFocus();
            }
        });
    } else if ($("#searchFilter5").is(":checked") && $.trim($("#searchValue5").val()) === "") {
        $.prompt("Please enter the value for Check Number filter", {
            callback: function () {
                $("#searchValue5").callFocus();
            }
        });
    } else if ($("#searchFilter6").is(":checked") && ($.trim($("#fromAmount6").val()) === "" || $.trim($("#toAmount6").val()) === "")) {
        if ($.trim($("#fromAmount6").val()) === "") {
            $.prompt("Please enter the from amount for Check Amount filter", {
                callback: function () {
                    $("#fromAmount6").callFocus();
                }
            });
        } else if ($.trim($("#toAmount6").val()) === "") {
            $.prompt("Please enter the to amount for Check Amount filter", {
                callback: function () {
                    $("#toAmount6").callFocus();
                }
            });
        }
    } else if ($("#searchFilter7").is(":checked") && ($.trim($("#fromAmount7").val()) === "" || $.trim($("#toAmount7").val()) === "")) {
        if ($.trim($("#fromAmount7").val()) === "") {
            $.prompt("Please enter value for Invoice Amount filter", {
                callback: function () {
                    $("#fromAmount7").callFocus();
                }
            });
        } else if ($.trim($("#toAmount7").val()) === "") {
            $.prompt("Please enter the value for Invoice Amount filter", {
                callback: function () {
                    $("#toAmount7").callFocus();
                }
            });
        }
    } else if ($("#searchFilter8").is(":checked") && ($.trim($("#fromAmount8").val()) === "" || $.trim($("#toAmount8").val()) === "")) {
        if ($.trim($("#fromAmount8").val()) === "") {
            $.prompt("Please enter the value for Invoice balance filter", {
                callback: function () {
                    $("#fromAmount8").callFocus();
                }
            });
        } else if ($.trim($("#toAmount8").val()) === "") {
            $.prompt("Please enter the value for Invoice balance filter", {
                callback: function () {
                    $("#toAmount8").callFocus();
                }
            });
        }
    } else if (!$("input[name='searchFilter']").is(":checked") && $.trim($("#customerNumber").val()) === "") {
        $.prompt("Please choose one of the search filter or enter the customer");
    } else {
        $("#selectedPage").val("1");
        $("#sortBy").val("invoice_date");
        $("#orderBy").val("asc");
        $("#action").val("search");
        $("#excludeIds").val("");
        $("#emailIds").val("");
        $("#arInquiryForm").submit();
    }
}

function changeView() {
    $("#action").val("changeView");
    $("#arInquiryForm").submit();
}

function gotoPage(pageNo) {
    $("#selectedPage").val(pageNo);
    $("#action").val("sortingAndPaging");
    $("#arInquiryForm").submit();
}

function gotoSelectedPage() {
    $("#selectedPage").val($("#selectedPageNo").val());
    $("#action").val("sortingAndPaging");
    $("#arInquiryForm").submit();
}

function doSort(sortBy) {
    $("#sortBy").val(sortBy);
    if ($("#orderBy").val() === "desc") {
        $("#orderBy").val("asc");
    } else {
        $("#orderBy").val("desc");
    }
    $("#action").val("sortingAndPaging");
    $("#arInquiryForm").submit();
}

function clearAll() {
    $("#action").val("clearAll");
    $("#arInquiryForm").submit();
}

function uploadArInvoices() {
    showLightBox("Upload AR Invoices", path + "/jsp/common/upload.jsp?action=uploadArInvoices", 100, 400);
}

function clearFilters() {
    $("#action").val("clearFilters");
    $("#arInquiryForm").submit();
}

function gotoTradingPartner() {
    var customerNumber = $.trim($("#customerNumber").val());
    if (customerNumber === "") {
        $.prompt("Please enter the customer", {
            callback: function () {
                $("#customerName").callFocus();
            }
        });
    } else if ($(document).parent().parent("homeForm").length) {
        window.parent.parent.gotoTradingPartner(customerNumber, "AR Config");
    } else {
        window.parent.gotoTradingPartner(customerNumber, "AR Config");
    }
}

function showAccountNotes() {
    var customerNumber = $.trim($("#customerNumber").val());
    if (customerNumber === "") {
        $.prompt("Please enter the customer", {
            callback: function () {
                $("#customerName").callFocus();
            }
        });
    } else {
        var url = path + "/notes.do";
        url += "?moduleId=AR_CONFIGURATION";
        url += "&moduleRefId=" + customerNumber;
        showLightBox("Notes", url, 400, 800);
    }
}

function sendStatement() {
    var customerNumber = $.trim($("#customerNumber").val());
    if (customerNumber === "") {
        $.prompt("Please enter the customer", {
            callback: function () {
                $("#customerName").callFocus();
            }
        });
    } else {
        setExcludeIds();
        var url = path + "/arInquiry.do";
        url += "?action=sendStatement";
        url += "&customerNumber=" + customerNumber;
        url += "&customerName=" + $("#customerName").val();
        url += "&excludeIds=" + $("#excludeIds").val();
        showLightBox("Send Statement", url, 400, 800);
    }
}

function sendEmail() {
    setEmailIds();
    var emailIds = $.trim($("#emailIds").val());
    if (emailIds === "") {
        $.prompt("Please select atlease one invoice");
    } else {
        var url = path + "/jsps/common/email.jsp";
        var params = encodeURIComponent("arInquiry.do?action=sendEmail&emailIds=" + emailIds);
        url += "?action=" + params;
        showLightBox("Send Email", url, 400, 800);
    }
}

function showAdjustmentMessage(message, id) {
    var number = parseInt(jQuery("#transactionList").val());
    $(".adjustedMessage").html(message).addClass("message");
    if (id !== "") {
        $("#adjustment" + id).remove();
        $("#balance" + id).html("0.00").removeClass("red").addClass("black");
    }
    jQuery("#transactionList").val(number - 1);
    if (jQuery("#transactionList").val() === "0") {
        $(".message").html(message).addClass("message");
        Lightbox.close();
    }
}

function saveAdjustment(id, ele) {
    var row = $(ele).parent().parent();
    var suffix = row.find(".suffix");
    if ($.trim(suffix.val()) === "") {
        $.prompt("Please enter suffix value", {
            callback: function () {
                suffix.callFocus();
            }
        });
    } else {
        $.ajaxx({
            url: path + "/arInquiry.do",
            data: {
                action: "saveAdjustment",
                id: id,
                suffix: $.trim(suffix.val())
            },
            preloading: true,
            success: function (message) {
                showAdjustmentMessage(message, id);
                row.remove();
            }
        });
    }
}

function showAdjustmentResult(result, id) {
    if (result.indexOf("adjusted successfully") > -1 || result.indexOf("invoices are adjusted") > -1) {
        showMessage(result);
        if (id !== "") {
            $("#adjustment" + id).remove();
            $("#balance" + id).html("0.00").removeClass("red").addClass("black");
        }
    } else {
        $("#hidden-div").html(result);
        var url = "?TB_inline&inlineId=hidden-div&height=200&width=600";
        Lightbox.showPopUp("Adjustment", url, "sexylightbox", "", "");
    }
}

function doMassAdjustment() {
    $.ajaxx({
        url: path + "/arInquiry.do",
        data: {
            action: "doMassAdjustment"
        },
        preloading: true,
        success: function (message) {
            showAdjustmentResult(message, "");
        }
    });
}

function doAdjustment(id) {
    $.ajaxx({
        url: path + "/arInquiry.do",
        data: {
            action: "doAdjustment",
            id: id
        },
        preloading: true,
        success: function (message) {
            showAdjustmentResult(message, id);
        }
    });
}

function massCustRefUpdate() {
    var customerNumber = $.trim($("#customerNumber").val());
    if (customerNumber === "") {
        $.prompt("Please enter the customer", {
            callback: function () {
                $("#customerName").callFocus();
            }
        });
    } else {
        var ul = "Customer Reference for LCL Imports shipments";
        ul += "<ul style='list-style: none; margin: 10px 0px;'>";
        $.each(["Master BL", "Sub House BL", "AMS House BL", "Container Number"], function (index, type) {
            ul += "<li>";
            ul += "<input type='radio' name='referenceType' value='" + type + "' style='vertical-align: middle; margin: 0 3px 0 0;'/>";
            ul += "<label>" + type + "</label>";
            ul += "</li>";
        });
        ul += "</ul>";
        $.prompt({
            state0: {
                html: ul,
                buttons: {
                    Update: true,
                    Cancel: false
                },
                focus: 1,
                submit: function (v) {
                    if (v) {
                        $.ajaxx({
                            url: path + "/arInquiry.do",
                            data: {
                                action: "updateCustomerReference",
                                customerNumber: customerNumber,
                                referenceType: $("input[name='referenceType']:checked").val()
                            },
                            preloading: true,
                            success: function (message) {
                                if ($(".message").length > 0) {
                                    $(".message").html(message);
                                } else {
                                    $(".error").html(message).removeClass("error").addClass("message");
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}

function showCustomerResult(result, customerNumber, customerName) {
    showMessage(result);
    customerNumber.addClass("readonly").attr("disabled", true);
    customerName.addClass("readonly").attr("disabled", true);
    var row = customerName.parent().parent();
    var noteRefId = row.find(".noteRefId");
    noteRefId.val(noteRefId.val().replace(noteRefId.val().split("-")[0], customerNumber.val()));
    var documentId = row.find(".documentId");
    documentId.val(documentId.val().replace(documentId.val().split("-")[0], customerNumber.val()));
}

function updateCustomer(index) {
    var customerNumber = $(".newCustomerNumber:eq(" + index + ")");
    var customerName = $(".newCustomerName:eq(" + index + ")");
    if (customerNumber.val() === "") {
        $.prompt("Please enter the customer", {
            callback: function () {
                customerName.callFocus();
            }
        });
    } else {
        var id = $(".id:eq(" + index + ")").val();
        $.ajaxx({
            url: path + "/arInquiry.do",
            data: {
                action: "updateInvoice",
                id: id,
                customerName: customerName.val(),
                customerNumber: customerNumber.val()
            },
            preloading: true,
            success: function (message) {
                showCustomerResult(message, customerNumber, customerName);
            }
        });
    }
}

function showFclBl(fileNo, blId, blNumber, importFlag) {
    var url = path + "/printConfig.do";
    url += "?screenName=BL";
    url += "&fileNo=" + fileNo;
    url += "&blId=" + blId;
    url += "&bolNo=" + blNumber;
    url += "&filesToPrint=all";
    url += "&importFlag=" + importFlag;
    showLightBox("Print/Fax/Email", url, 400, 800);
}
/* LCL BL and LCL Import PDF LIST for BL Column in searchResults.jsp*/
function showLcl(fileNo, fileId, bookingType) {
    var url = path + "/printConfig.do";
    if (bookingType === "E" || bookingType === "T") {
        //window.parent.goToConsolidatePage(path, fileId,"B","Exports","LCLBooking","ar_inquiry");
        url += "?screenName=LCLBL&fromScreen=AR_Inquiry&cob=true&postedLclBl=postBl";
    } else {
        url += "?screenName=LCLIMPBooking";
    }
    url += "&fileNo=" + fileNo;
    url += "&fileId=" + fileId;
    showLightBox("Print/Fax/Email", url, 400, 800);
}
/* LCL BL and LCL Import PDF LIST for Invoice Column in searchResults.jsp*/
function showStatusUpdate(fileNo, fileId, bookingType) {
    var screenName = bookingType === 'E'  ? "LCLBL" : "LCLIMPBooking";
    var url = path + "/printConfig.do";
    url += "?screenName=" + screenName;
    url += "&fileNo=" + fileNo;
    url += "&fileId=" + fileId;
    url += "&statusUpdateOnly=true&cob=true&postedLclBl=postBl";
    showLightBox("Print/Fax/Email", url, 400, 800);
}

function showFreightInvoice(fileNo, invoiceNumber, blNumber, id) {
    var url = path + "/printConfig.do";
    url += "?fileNo=" + fileNo;
    if (invoiceNumber.indexOf("CN") > 0) {
        url += "&screenName=CreditDebitNote";
        url += "&CreditDebitNotePrint=" + blNumber;
        url += "&noticeNo=" + invoiceNumber.slice(10);
    } else {
        url += "&screenName=BL";
        url += "&cutomerNumber=" + $.trim($("#customerNumber" + id).val());
        url += "&blId=" + blNumber;
        url += "&noticeNumber=" + $.trim($("#correctionNotice" + id).val());
    }
    showLightBox("Print/Fax/Email", url, 400, 800);
}

function showLclCorrectionInvoice(dockReceipt, correctionNo, sourceId, bookingType) {
    if (bookingType === 'E' || bookingType === 'T') {
        showStatusUpdate(dockReceipt, sourceId, "E")
    } else {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLCorrectionDAO",
                methodName: "getLclCorrectionId",
                param1: dockReceipt,
                param2: correctionNo,
                dataType: "json"
            },
            preloading: true,
            success: function (correctionId) {
                var url = path + "/printConfig.do";
                url += "?screenName=LclCreditDebitNote";
                url += "&CreditDebitNotePrint=" + dockReceipt;
                url += "&fileNo=" + dockReceipt;
                url += "&noticeNo=" + correctionNo;
                url += "&fileId=" + sourceId;
                url += "&correctionId=" + correctionId;
                url += "&selectedMenu=Imports";
                showLightBox("Print/Fax/Email", url, 400, 800);
            }
        });
    }
}

function showArInvoice(invoiceNumber, arInvoiceId) {
    var url = path + "/printConfig.do";
    url += "?screenName=BL";
    url += "&arInvoice=" + invoiceNumber;
    url += "&arInvoiceId=" + arInvoiceId;
    showLightBox("Print/Fax/Email", url, 400, 800);
}

function showApInvoiceDetails(vendorNumber, invoiceNumber) {
    var url = path + "/apInquiry.do";
    url += "?action=showInvoiceDetails";
    url += "&vendorNumber=" + vendorNumber;
    url += "&invoiceNumber=" + encodeURIComponent(invoiceNumber);
    url += "&transactionType=AP";
    showLightBox("Invoice Details - Vendor : " + vendorNumber + " Invoice : " + invoiceNumber, url, 400, 900);
}

function showCharges(source, id) {
    var url = path + "/arInquiry.do";
    url += "?action=showCharges";
    url += "&customerNumber=" + $.trim($("#customerNumber" + id).val());
    url += "&blNumber=" + $.trim($("#blNumber" + id).val());
    url += "&invoiceNumber=" + $.trim($("#invoiceNumber" + id).val());
    url += "&source=" + source;
    showLightBox("Charges", url, 400, 1200);
}

function setExcludeIds() {
    var excludedIds = [];
    $(".excludedIds:checked").each(function () {
        excludedIds.push($(this).val());
    });
    $("#excludeIds").val(excludedIds);
}

function setEmailIds() {
    var emailIds = [];
    $(".emailIds:checked").each(function () {
        emailIds.push($(this).val());
    });
    $("#emailIds").val(emailIds);
}

function showMoreInfo(source, id) {
    var url = path + "/arInquiry.do";
    url += "?action=showMoreInfo";
    url += "&source=" + source;
    url += "&id=" + id;
    showLightBox("More Info", url, 175, 425);
}

function showTransactions(source, id) {
    var url = path + "/arInquiry.do";
    url += "?action=showTransactions";
    url += "&customerNumber=" + $.trim($("#customerNumber" + id).val());
    url += "&blNumber=" + $.trim($("#blNumber" + id).val());
    url += "&invoiceNumber=" + $.trim($("#invoiceNumber" + id).val());
    url += "&source=" + source;
    url += "&id=" + id;
    showLightBox("Transactions", url, 400, 800);
}

function showInvoiceNotes(moduleId, id) {
    var url = path + "/notes.do";
    url += "?moduleId=" + moduleId;
    if (moduleId !== "Charge Code") {
        url += "&moduleRefId=" + encodeURIComponent($("#noteRefId" + id).val());
    } else {
        url += "&moduleRefId=" + id;
    }
    showLightBox("Notes", url, 400, 800);
}

function uploadInvoice(id) {
    var documentId = $("#documentId" + id).val();
    var url = path + "/scan.do";
    url += "?screenName=INVOICE";
    url += "&documentId=" + encodeURIComponent(documentId);
    showLightBox("Upload Invoice", url, 400, 800);
}

function displayMessage(message) {
    $(".message").html(message);
}

function changeDisputeIconColor(action, id, invoice) {
    if (action === "disputed") {
        $(".message").html("Invoice is disputed successfully").removeClass("message").addClass("error");
        $("#dispute" + id).attr({
            "src": path + "/images/icons/alphabets/d_red.png"
        }).attr("onclick", "").unbind("click").click(function () {
            undisputeInvoice(id, invoice);
        }).data("tipText", "Undispute Invoice");
    } else {
        $(".error").html("Invoice is undisputed successfully").removeClass("error").addClass("message");
        $("#dispute" + id).attr({
            "src": path + "/images/icons/alphabets/d_green.png"
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
                var url = path + "/jsps/common/email.jsp";
                url += "?subject=" + encodeURIComponent($("#customerName").val()) + " " + encodeURIComponent($("#customerNumber").val()) + " " + "disputes invoice" + " " + encodeURIComponent(invoice) + " " + "- action required.";
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
                    url: path + "/arInquiry.do",
                    data: {
                        action: "undisputeInvoice",
                        id: id
                    },
                    preloading: true,
                    success: function () {
                        changeDisputeIconColor("undisputed", id, invoice);
                    }
                });
            }
        }
    });
}

function printPdf(fileName) {
    var url = path + "/servlet/FileViewerServlet.do";
    url += "?fileName=" + fileName;
    window.parent.showLightBox("NS Invoice", url);
}

function exportExcel(fileName) {
    var url = path + "/servlet/FileViewerServlet.do";
    url += "?fileName=" + fileName;
    $("<iframe/>", {
        name: "iframe",
        id: "iframe",
        src: url
    }).appendTo("body").hide();
}

function createNsInvoice(id, type) {
    var batchId = $("#invoiceNumber" + id).val().replace("NET SETT", "");
    $.ajaxx({
        url: path + "/arInquiry.do",
        data: {
            action: "createNsInvoice",
            batchId: batchId,
            type: type
        },
        preloading: true,
        success: function (fileName) {
            if (type === "pdf") {
                printPdf(fileName);
            } else {
                exportExcel(fileName);
            }
        }
    });
}

$(document).ready(function () {
    $("#arInquiryForm").submit(function () {
        showPreloading();
    });
    setResultHeight();
    $(window).resize(function () {
        window.parent.changeHeight();
        setResultHeight();
    });
    initAutocompleteFields();
    initSearchFilters();
    initDateFields();
    initShowFilters();
    $("[title != '']").not("link").tooltip();
    if ($.trim($("#customerName").val()) === "") {
        $("#customerName").callFocus();
    }
    $(document).keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode === 13) {
            event.preventDefault();
            search();
        }
    });
    Lightbox.initialize({
        color: 'black',
        dir: path + '/js/lightbox/images',
        moveDuration: 1,
        resizeDuration: 1
    });
});

function showPayments(sourceId, id) {
    var url = path + "/arInquiry.do";
    url += "?action=showPayments";
    url += "&sourceId=" + sourceId;
    url += "&customerNumber=" + $.trim($("#customerNumber" + id).val());
    showLightBox("Payments", url, 400, 800);
}