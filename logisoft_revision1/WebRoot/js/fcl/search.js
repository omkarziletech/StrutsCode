/* 
 *  Document   : search
 *  Created on : Feb 12, 2013, 6:43:00 PM
 *  Author     : Lakshmi Narayanan
 */

function showLightBox(title, url, height, width) {
    height = height || $(document).height() - 50;
    width = width || $(document).width() - 100;
    url += (url.indexOf("?") > -1 ? "&" : "?") + "TB_iframe&height=" + height + "&width=" + width;
    Lightbox.showPopUp(title, url, "sexylightbox", "", "");
}

function closeLightBox() {
    Lightbox.close();
}

function fromDateChange() {
    if ($.trim($("#fromDate").val()) !== "" && !validateDate($("#fromDate"))) {
        $.prompt("Please enter valid from date", {
            callback: function() {
                $("#fromDate").val("").callFocus();
            }
        });
    }
}

function toDateChange() {
    if ($.trim($("#toDate").val()) !== "" && !validateDate($("#toDate"))) {
        $.prompt("Please enter valid to date", {
            callback: function() {
                $("#toDate").val("").callFocus();
            }
        });
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

function initAutoCompleteFields() {
    $("#issuingTerminal").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=TERMINAL&template=terminal&fieldIndex=1&",
        width: "420px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 250,
        focusField: "pod"
    });
    $("#issuingTerminalimport").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=IMPORTTERMINAL&template=terminal&fieldIndex=1&",
        width: "420px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 250,
        focusField: "pod"
    });
    $("#origin,#pol,#pod,#destination").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=PORT&template=port&fieldIndex=1&",
        width: "420px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 250
    });
    $("#vessel").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=VESSEL&template=vessel&fieldIndex=1&",
        width: "250px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 250
    });
    $("#clientName").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=CLIENT&template=tradingPartner&fieldIndex=1,2&",
        width: "525px",
        otherFields: "clientNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 250
    });
    $("#shipperName").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=SHIPPER&template=tradingPartner&fieldIndex=1,2&",
        width: "525px",
        otherFields: "shipperNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 250
    });
    $("#masterShipper").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=MASTERSHIPPER&template=tradingPartner&fieldIndex=1,2&",
        width: "525px",
        otherFields: "masterShipperNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 250

    });
    $("#sslName").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=SSL&template=tradingPartner&fieldIndex=1,2&",
        width: "525px",
        otherFields: "sslNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 250,
        showLeft: true
    });
    $("#forwarderName").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=FORWARDER&template=tradingPartner&fieldIndex=1,2&",
        width: "525px",
        otherFields: "forwarderNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 250
    });
    $("#consigneeName").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=CONSIGNEE&template=tradingPartner&fieldIndex=1,2&",
        width: "525px",
        otherFields: "consigneeNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 250
    });
    $("#createdBy,#bookedBy").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=USER&template=user&fieldIndex=1&",
        width: "250px",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 250,
        showLeft: true
    });
    $("#customerCountry").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=COUNTRY&template=user&fieldIndex=1,2&",
        width: "350px",
        otherFields: "customerCountryCode",
        resultsClass: "ac_results  z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 250
    });
}

function initQuickRates() {
    $("#quickRateDestination").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=DESTINATION&template=port&fieldIndex=1,6&",
        width: "420px",
        otherFields: "quickRateDestinationId",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 200,
        onblur: "setOriginOptions()"
    });
    $("#quickRateOrigin").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=ORIGIN&template=port&fieldIndex=1,6&",
        width: "420px",
        otherFields: "quickRateOriginId",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 200,
        onblur: "setDestinationOptions()"
    });
    $("#quickRateCommodity").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=COMMODITY&template=commodity&fieldIndex=1&",
        width: "420px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 200
    });
}

function disableAutocomplete(ele) {
    if ($(ele).is(":checked")) {
        $("#autoClient").hide();
        $("#manualClient").show();
    } else {
        $("#autoClient").show();
        $("#manualClient").hide();
    }
}

function setCreatedBy(ele) {
    if ($(ele).is(":checked")) {
        $("#createdBy").val($(ele).val()).addClass("readonly").attr("readonly", true);
        $("#createdByCheck").val($(ele).val());
    } else {
        $("#createdBy").val("").removeClass("readonly").removeAttr("readonly");
        $("#createdByCheck").val("");
    }
}

function setBookedBy(ele) {
    if ($(ele).is(":checked")) {
        $("#bookedBy").val($(ele).val()).addClass("readonly").attr("readonly", true);
        $("#bookedByCheck").val($(ele).val());
    } else {
        $("#bookedBy").val("").removeClass("readonly").removeAttr("readonly");
        $("#bookedByCheck").val("");
    }
}

function setOriginOptions() {
    var url = path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=";
    if ($.trim($("#quickRateDestination").val()) !== "") {
        if ($("#quickRateOriginByCity").is(":checked")) {
            url += "ORIGIN_BY_DESTINATION";
        } else {
            url += "ORIGIN_BY_DESTINATION_AND_COUNTRY";
        }
        url += "&template=port&fieldIndex=1,6&";
        var quickRateDestination = $("#quickRateDestination").val();
        $("#quickRateOrigin").setOptions({
            url: url,
            extraParams: {
                param1: quickRateDestination.substring(quickRateDestination.indexOf("(") + 1, quickRateDestination.indexOf(")"))
            }
        });
    } else {
        if ($("#quickRateOriginByCity").is(":checked")) {
            url += "ORIGIN";
        } else {
            url += "ORIGIN_BY_COUNTRY";
        }
        url += "&template=port&fieldIndex=1,6&";
        $("#quickRateOrigin").setOptions({
            url: url
        });
    }
}

function setDestinationOptions() {
    var url = path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=";
    if ($.trim($("#quickRateOrigin").val()) !== "") {
        if ($("#quickRateDestinationByCity").is(":checked")) {
            url += "DESTINATION_BY_ORIGIN";
        } else {
            url += "DESTINATION_BY_ORIGIN_AND_COUNTRY";
        }
        url += "&template=port&fieldIndex=1,6&";
        var quickRateOrigin = $("#quickRateOrigin").val();
        $("#quickRateDestination").setOptions({
            url: url,
            extraParams: {
                param1: quickRateOrigin.substring(quickRateOrigin.indexOf("(") + 1, quickRateOrigin.indexOf(")"))
            }
        });
    } else {
        if ($("#quickRateDestinationByCity").is(":checked")) {
            url += "DESTINATION";
        } else {
            url += "DESTINATION_BY_COUNTRY";
        }
        url += "&template=port&fieldIndex=1,6&";
        $("#quickRateDestination").setOptions({
            url: url
        });
    }
}

function setCommodityOptions() {
    $("#quickRateCommodity").val("");
    $("#quickRateCommodityCheck").val("");
    var url = path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=";
    if ($("#quickRateCommodityBulletRate").is(":checked")) {
        url += "BULLET_COMMODITY";
    } else {
        url += "COMMODITY";
    }
    url += "&template=commodity&fieldIndex=1&";
    $("#quickRateCommodity").setOptions({
        url: url
    });
}

function showQuickRates(importNavigation) {
    showAlternateMask();
    $("#quickRates").center().show(500);
    $("#quickRateDestination").val("").callFocus();
    $("#quickRateOrigin").val("");
    if (importNavigation !== "") {
        $("#quickRateCommodity").val("006205");
        $("#quickRateCommodityCheck").val("006205");
    } else {
        $("#quickRateCommodity").val("006100");
        $("#quickRateCommodityCheck").val("006100");
    }
    $("#quickRateCommodityBulletRate").removeAttr("checked");
}

function hideQuickRates() {
    $("#quickRates").hide(500);
    hideAlternateMask();
}

function showOrigins(result) {
    if (result === "No Data") {
        $.prompt(result);
    } else if (result.indexOf("========") > -1) {
        var url = path + "/rateGrid.do?action=Destination";
        url += "&originPort=" + $.trim($("#quickRateOrigin").val());
        url += "&origin=" + result.replace("========", "");
        url += "&destinationPort=" + $.trim($("#quickRateDestination").val());
        url += "&destination=" + $.trim($("#quickRateDestination").val());
        url += "&ratesFrom=quickRates";
        url += "&hazardous=" + $("input[name='quickRateHazmat']:checked").val();
        url += "&commodity=" + $("#quickRateCommodity").val();
        url += "&originalCommodity=" + $("#quickRateCommodity").val();
        url += "&bulletRates=" + $("#quickRateCommodityBulletRate").is(":checked");
        showLightBox("FCL Rates Comparison Grid", url, $(window).height() - 20, $(window).width(-100), "");
    } else {
        $("#originsList").html(result).center().show(500);
    }
}

function checkAllOrigins(ele) {
    if ($(ele).is(":checked")) {
        $("input[name='regionCheck']").attr("checked", true);
        $("input[name='originCheck']").attr("checked", true);
    } else {
        $("input[name='regionCheck']").removeAttr("checked");
        $("input[name='originCheck']").removeAttr("checked");
    }
}

function checkRegion(ele) {
    if ($(ele).is(":checked")) {
        $("input[region='" + $(ele).val() + "']").attr("checked", true);
    } else {
        $("input[region='" + $(ele).val() + "']").removeAttr("checked");
    }
}

function showRatesForDestination() {
    var origins = [];
    $("input[name='originCheck']:checked").each(function() {
        if ($(this).val() !== "") {
            origins.push($(this).val());
        }
    });
    if (origins.length === 0) {
        $.prompt("Please select atleast one");
    } else {
        var url = path + "/rateGrid.do?action=Destination";
        url += "&originPort=" + $.trim($("#quickRateOrigin").val());
        url += "&origin=" + origins.toString();
        url += "&destinationPort=" + $.trim($("#quickRateDestination").val());
        url += "&destination=" + $.trim($("#quickRateDestination").val());
        url += "&ratesFrom=quickRates";
        url += "&hazardous=" + $("input[name='quickRateHazmat']:checked").val();
        url += "&commodity=" + $("#quickRateCommodity").val();
        url += "&originalCommodity=" + $("#quickRateCommodity").val();
        url += "&bulletRates=" + $("#quickRateCommodityBulletRate").is(":checked");
        showLightBox("FCL Rates Comparison Grid", url, $(window).height() - 20, $(window).width(-100), "");
    }
}

function closeOrigins() {
    $("#originsList").html("").hide(500);
}

function showDestinations(result) {
    if (result === "No Data") {
        $.prompt(result);
    } else if (result.indexOf("========") > -1) {
        var url = path + "/rateGrid.do?action=Origin";
        url += "&destinationPort=" + $.trim($("#quickRateDestination").val());
        url += "&destination=" + result.replace("========", "");
        url += "&originPort=" + $.trim($("#quickRateOrigin").val());
        url += "&origin=" + $.trim($("#quickRateOrigin").val());
        url += "&ratesFrom=quickRates";
        url += "&hazardous=" + $("input[name='quickRateHazmat']:checked").val();
        url += "&commodity=" + $("#quickRateCommodity").val();
        url += "&originalCommodity=" + $("#quickRateCommodity").val();
        url += "&bulletRates=" + $("#quickRateCommodityBulletRate").is(":checked");
        showLightBox("FCL Rates Comparison Grid", url, $(window).height() - 20, $(window).width(-100), "");
    } else {
        $("#destinationsList").html(result).center().show(500);
    }
}

function checkAllDestinations(ele) {
    if ($(ele).is(":checked")) {
        $("input[name='destinationCheck']").attr("checked", true);
    } else {
        $("input[name='destinationCheck']").removeAttr("checked");
    }
}

function showRatesForOrigin() {
    var destinations = [];
    $("input[name='destinationCheck']:checked").each(function() {
        if ($(this).val() !== "") {
            destinations.push($(this).val());
        }
    });
    if (destinations.length === 0) {
        $.prompt("Please select atleast one");
    } else {
        var url = path + "/rateGrid.do?action=Origin";
        url += "&destinationPort=" + $.trim($("#quickRateDestination").val());
        url += "&destination=" + destinations.toString();
        url += "&originPort=" + $.trim($("#quickRateOrigin").val());
        url += "&origin=" + $.trim($("#quickRateOrigin").val());
        url += "&ratesFrom=quickRates";
        url += "&hazardous=" + $("input[name='quickRateHazmat']:checked").val();
        url += "&commodity=" + $("#quickRateCommodity").val();
        url += "&originalCommodity=" + $("#quickRateCommodity").val();
        url += "&bulletRates=" + $("#quickRateCommodityBulletRate").is(":checked");
        showLightBox("FCL Rates Comparison Grid", url, $(window).height() - 20, $(window).width(-100), "");
    }
}

function closeDestinations() {
    $("#destinationsList").html("").hide(500);
}

function showDestinationsForEntireCountry(result) {
    var url = path + "/rateGrid.do?action=Origin";
    url += "&destinationPort=" + $.trim($("#quickRateDestination").val());
    url += "&destination=" + ($.trim(result) !== "" ? result : $.trim($("#quickRateDestination").val()));
    url += "&originPort=" + $.trim($("#quickRateOrigin").val());
    url += "&origin=" + $.trim($("#quickRateOrigin").val());
    url += "&ratesFrom=quickRates";
    url += "&hazardous=" + $("input[name='quickRateHazmat']:checked").val();
    url += "&commodity=" + $("#quickRateCommodity").val();
    url += "&originalCommodity=" + $("#quickRateCommodity").val();
    url += "&bulletRates=" + $("#quickRateCommodityBulletRate").is(":checked");
    showLightBox("FCL Rates Comparison Grid", url, $(window).height() - 20, $(window).width(-100), "");
}

function getQuickRates(importNavigation) {
    var fileType = "";
    if (importNavigation !== "") {
        fileType = "I";
    } else {
        fileType = null;
    }
    var destination = $.trim($("#quickRateDestination").val());
    var origin = $.trim($("#quickRateOrigin").val());
    var commodity = $.trim($("#quickRateCommodity").val());
    var url = $("#searchForm").attr("action");
    var params = "";
    if ((destination === "" || origin === "") && $("#quickRateCommodityBulletRate").is(":checked")) {
        $.prompt("Please select Destination Port and Origin");
    } else if (destination === "" && origin === "") {
        $.prompt("Please select Destination Port or Origin");
    } else if (commodity === "") {
        $.prompt("Please Select Commodity Code");
    } else if (origin === "") {
        params += "action=getOrigins";
        params += "&destination=" + destination;
        params += "&commodity=" + commodity;
        ajaxCall(url, {
            data: params,
            preloading: true,
            success: "showOrigins",
            async: false
        });
    } else if (destination === "") {
        params += "action=getDestinations";
        params += "&origin=" + origin;
        params += "&commodity=" + commodity;
        ajaxCall(url, {
            data: params,
            preloading: true,
            success: "showDestinations",
            async: false
        });
    } else if ($("#ratesForEntireCountry").is(":checked")) {
        params += "action=getDestinationsForCountry";
        params += "&destination=" + destination;
        ajaxCall(url, {
            data: params,
            preloading: true,
            success: "showDestinationsForEntireCountry",
            async: false
        });
    } else {
        var gridUrl = path + "/rateGrid.do?action=Origin";
        gridUrl += "&originPort=" + $("#quickRateOriginId").val();
        gridUrl += "&origin=" + origin;
        gridUrl += "&originName=" + origin;
        gridUrl += "&destinationPort=" + destination;
        gridUrl += "&destination=" + destination;
        gridUrl += "&ratesFrom=quickRates";
        gridUrl += "&doorOrigin=";
        gridUrl += "&hazardous=" + $("input[name='quickRateHazmat']:checked").val();
        gridUrl += "&commodity=" + commodity;
        gridUrl += "&originalCommodity=" + $("#quickRateCommodity").val();
        gridUrl += "&bulletRates=" + $("#quickRateCommodityBulletRate").is(":checked");
        gridUrl += "&fileType=" + fileType;
        showLightBox("FCL Rates Comparison Grid", gridUrl, $(window).height() - 20, $(window).width(-100), "");
    }
}

function gotoSearch() {
    $("#action").val("gotoSearch");
    $("#searchForm").submit();
}

function search() {
    if ((typeof $("#aesItn").val()!=='undefined' && $("#aesItn").val().length !== 0 && $("#aesItn").val().length < 3)
        ||(typeof $("#ams").val()!=='undefined' && $("#ams").val().length !== 0 && $("#ams").val().length < 3)
        || ($("#fileNumber").val().length !== 0 && $("#fileNumber").val().length < 3)
        || ($("#containerNumber").val().length !== 0 && $("#containerNumber").val().length < 3)
        || ($("#masterBl").val().length !== 0 && $("#masterBl").val().length < 3)
        || ($("#inboundNumber").val().length !== 0 && $("#inboundNumber").val().length < 3)
        || ($("#sslBookingNumber").val().length !== 0 && $("#sslBookingNumber").val().length < 3)){
        return;
    }else if ($.trim($("#fileNumber").val()) !== ""
        || $.trim($("#containerNumber").val()) !== ""
        || $.trim($("#sslBookingNumber").val()) !== ""
        || $.trim($("#masterBl").val()) !== ""
        || $.trim($("#inboundNumber").val()) !== ""
        || (typeof $("#aesItn").val()!=='undefined' && $("#aesItn").val().length > 3 && $.trim($("#aesItn").val()) !== "")
        || $.trim($("#filterBy").val()) === "Closed"
        || $.trim($("#filterBy").val()) === "Audited") {
        $("#fromDate").val("");
        $("#toDate").val("");
        $("#action").val("search");
        $("#searchForm").submit();
    } else if ($.trim($("#fromDate").val()) !== "" && $.trim($("#toDate").val()) !== "") {
        $("#action").val("search");
        $("#searchForm").submit();
    } else if ($.trim($("#fromDate").val()) === "") {
        $.prompt("Please enter From Date", {
            callback: function() {
                $("#fromDate").val("").callFocus();
            }
        });
    } else if ($.trim($("#toDate").val()) === "") {
        $.prompt("Please enter To Date", {
            callback: function() {
                $("#toDate").val("").callFocus();
            }
        });
    }
}

function searchByFileNumber() {
    if ($.trim($("#fileNumber").val()) !== "" || $.trim($("#sslBookingNumber").val()) !== "" || $.trim($("#containerNumber").val()) !== "") {
        $("#fromDate").val("");
        $("#toDate").val("");
        $("#action").val("search");
        $("#searchForm").submit();
    }
}

function doSort(sortBy) {
    $("#sortBy").val(sortBy);
    if ($("#orderBy").val() === "desc") {
        $("#orderBy").val("asc");
    } else {
        $("#orderBy").val("desc");
    }
    $("#action").val("doSort");
    $("#searchForm").submit();
}

function resetAll() {
    $("#action").val("reset");
    $("#searchForm").submit();
}

function showEdi(fileNumber, docTyp) {
    var title = "Edi Tracking";
    var url = path + "/ediTracking.do?docTyp=" + docTyp + "&fileNumber=" + fileNumber;
    showLightBox(title, url, 400, 1000, "");
}

function showTrackingStatus(fileNumber) {
    var title = "Notes";
    var url = path + "/notes.do?moduleId=File&itemName=100018&moduleRefId=" + fileNumber;
    showLightBox(title, url, 400, 800, "");
}

function showAes(fileNumber) {
    var title = "AES Tracking";
    var url = path + "/aesHistory.do?fileNumber=" + fileNumber;
    showLightBox(title, url, 400, 800, "");
}

var selectedFileNumber;
function showMessage(result) {
    if (result === 'available') {
        window.parent.gotoFclOpsScreens(selectedFileNumber);
    } else if (result.indexOf("is already opened in another window") > -1) {
        $.prompt(result);
    } else {
        $.prompt(result + ". Do you want to view the file?", {
            buttons: {
                "Yes": true,
                "No": false
            },
            callback: function(v, m, f) {
                if (v) {
                    window.parent.gotoFclOpsScreens(selectedFileNumber);
                }
            }
        });
    }
}

function openFile(fileNumber) {
    selectedFileNumber = fileNumber;
    var url = $("#searchForm").attr("action");
    var params = "action=checkLocking";
    params += "&fileNumber=" + fileNumber;
    ajaxCall(url, {
        data: params,
        preloading: true,
        success: "showMessage",
        async: false
    });
}

function newQuote() {
    window.parent.gotoNewFclScreen(($("#importFile").length > 0) ? "IMPORT QUOTE" : "Quotes");
}

function newBooking() {
    window.parent.gotoNewFclScreen("IMPORT BOOKING");
}

function newArrivalNotice() {
    window.parent.gotoNewFclScreen("IMPORT FCL BL");
}
function multiQuote(){
    window.parent.gotoNewFclScreen(($("#importFile").length > 0) ? "IMPORT MultiQuote" : "MultiQuote");
}

function setResultHeight() {
    if ($(".result-container").length > 0) {
        var height = window.parent.getFrameHeight() - 100;
        $(".result-container").height(height);
        $("body").css("overflow", "hidden");
    }
}

function validateSalescode(code) {
    var codeValue = code.value.replace(/,/g, '');
    code.value = codeValue.match(new RegExp('.{1,' + 2 + '}', 'g'));
}

$(document).ready(function() {
    $("#searchForm").submit(function() {
        window.parent.showPreloading();
    });
    window.parent.closePreloading();
    if ($("#fromDate").length > 0) {
        initDate();
        initAutoCompleteFields();
    } else {
        setResultHeight();
        $(window).resize(function() {
            window.parent.changeHeight();
            setResultHeight();
        });
    }
    initQuickRates();
    $("[title != '']").not("link").tooltip();
    Lightbox.initialize({
        color: 'black',
        dir: path + '/js/lightbox/images',
        moveDuration: 1,
        resizeDuration: 1
    });
    $("#fileNumber,#sslBookingNumber,#containerNumber").keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode === 13) {
            event.preventDefault();
            searchByFileNumber();
        }
    });
});

function showCustomerSearchFilter(data) {
    jQuery("#searchValueBy").val(data);
    if (data === "client") {
        jQuery("#customerState").val(jQuery("#clientSearchState").val());
        jQuery("#customerCountry").val(jQuery("#clientSearchCountry").val());
        jQuery("#customerCountryCode").val(jQuery("#clientSearchCountryCode").val());
        jQuery("#customerZipCode").val(jQuery("#clientSearchZip").val());
        jQuery("#customerSalesCode").val(jQuery("#clientSearchSalesCode").val());
        jQuery("#searchValue").html("Client Search Options");
    }
    else if (data === "shipper") {
        jQuery("#customerState").val(jQuery("#shipperSearchState").val());
        jQuery("#customerCountry").val(jQuery("#shipperSearchCountry").val());
        jQuery("#customerCountryCode").val(jQuery("#shipperSearchCountryCode").val());
        jQuery("#customerZipCode").val(jQuery("#shipperSearchZip").val());
        jQuery("#customerSalesCode").val(jQuery("#shipperSearchSalesCode").val());
        jQuery("#searchValue").html("Shipper Search Options");
    }
    else if (data === "masterShipper") {
        jQuery("#masterState").val(jQuery("#masterSearchState").val());
        jQuery("#masterCountry").val(jQuery("#masterSearchCountry").val());
        jQuery("#masterCountryCode").val(jQuery("#masterSearchCountryCode").val());
        jQuery("#masterZipCode").val(jQuery("#masterSearchZip").val());
        jQuery("#masterSalesCode").val(jQuery("#masterSearchSalesCode").val());
        jQuery("#masterValue").html("masterShipper Search Options");
    }
    else if (data === "forwarder") {
        jQuery("#customerState").val(jQuery("#forwarderSearchState").val());
        jQuery("#customerCountry").val(jQuery("#forwarderSearchCountry").val());
        jQuery("#customerCountryCode").val(jQuery("#forwarderSearchCountryCode").val());
        jQuery("#customerZipCode").val(jQuery("#forwarderSearchZip").val());
        jQuery("#customerSalesCode").val(jQuery("#forwarderSearchSalesCode").val());
        jQuery("#searchValue").html("Forwarder Search Options");
    } else if (data === "consignee") {
        jQuery("#customerState").val(jQuery("#consigneeSearchState").val());
        jQuery("#customerCountry").val(jQuery("#consigneeSearchCountry").val());
        jQuery("#customerCountryCode").val(jQuery("#consigneeSearchCountryCode").val());
        jQuery("#customerZipCode").val(jQuery("#consigneeSearchZip").val());
        jQuery("#customerSalesCode").val(jQuery("#consigneeSearchSalesCode").val());
        jQuery("#searchValue").html("Consignee Search Options");
    }
    showAlternateMask();
    $("#customerSearchFilters").center().show(500);
}

function hideSearchFilter() {
    $("#customerSearchFilters").hide(500);
    hideAlternateMask();
}

function submitCustomerSearchFilter() {
    var searchValueBy = jQuery("#searchValueBy").val();
    if (searchValueBy === "client") {
        jQuery("#clientSearchState").val(jQuery("#customerState").val());
        jQuery("#clientSearchCountry").val(jQuery("#customerCountry").val());
        jQuery("#clientSearchCountryCode").val(jQuery("#customerCountryCode").val());
        jQuery("#clientSearchZip").val(jQuery("#customerZipCode").val());
        jQuery("#clientSearchSalesCode").val(jQuery("#customerSalesCode").val());
        setClientOptions();
    } else if (searchValueBy === "shipper") {
        jQuery("#shipperSearchState").val(jQuery("#customerState").val());
        jQuery("#shipperSearchCountry").val(jQuery("#customerCountry").val());
        jQuery("#shipperSearchCountryCode").val(jQuery("#customerCountryCode").val());
        jQuery("#shipperSearchZip").val(jQuery("#customerZipCode").val());
        jQuery("#shipperSearchSalesCode").val(jQuery("#customerSalesCode").val());
        setShipperOptions();
    }
    else if (searchValueBy === "masterShipper") {
        jQuery("#masterSearchState").val(jQuery("#customerState").val());
        jQuery("#masterSearchCountry").val(jQuery("#customerCountry").val());
        jQuery("#masterSearchCountryCode").val(jQuery("#customerCountryCode").val());
        jQuery("#masterSearchZip").val(jQuery("#customerZipCode").val());
        jQuery("#masterSearchSalesCode").val(jQuery("#customerSalesCode").val());
        setMasterShipperOptions();
    }
    else if (searchValueBy === "forwarder") {
        jQuery("#forwarderSearchState").val(jQuery("#customerState").val());
        jQuery("#forwarderSearchCountry").val(jQuery("#customerCountry").val());
        jQuery("#forwarderSearchCountryCode").val(jQuery("#customerCountryCode").val());
        jQuery("#forwarderSearchZip").val(jQuery("#customerZipCode").val());
        jQuery("#forwarderSearchSalesCode").val(jQuery("#customerSalesCode").val());
        setForwarderOptions();
    } else if (searchValueBy === "consignee") {
        jQuery("#consigneeSearchState").val(jQuery("#customerState").val());
        jQuery("#consigneeSearchCountry").val(jQuery("#customerCountry").val());
        jQuery("#consigneeSearchCountryCode").val(jQuery("#customerCountryCode").val());
        jQuery("#consigneeSearchZip").val(jQuery("#customerZipCode").val());
        jQuery("#consigneeSearchSalesCode").val(jQuery("#customerSalesCode").val());
        setConsigneeOptions();
    }
    $("#customerSearchFilters").hide(500);
    hideAlternateMask();
}
function closeCustomerSearchFilter() {
    jQuery("#customerState").val("");
    jQuery("#customerCountry").val("");
    jQuery("#customerZipCode").val("");
    jQuery("#customerSalesCode").val("");
    submitCustomerSearchFilter();
}

function setClientOptions() {
    var url = path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=";
    if ($("#clientSearchState").val() !== "" || $("#clientSearchCountry").val() !== "" || $("#clientSearchZip").val() !== "" || $("#clientSearchSalesCode").val() !== "") {
        url += "CLIENT_WITH_OPTIONS&template=tradingPartner&fieldIndex=1,2&";
        $("#clientName").setOptions({
            url: url,
            extraParams: {
                param1: jQuery("#clientSearchState").val(),
                param2: jQuery("#clientSearchCountryCode").val(),
                param3: jQuery("#clientSearchZip").val(),
                param4: jQuery("#clientSearchSalesCode").val()
            }
        });
    } else {
        url += "CLIENT&template=tradingPartner&fieldIndex=1,2&";
        $("#clientName").setOptions({
            url: url
        });
    }
}
function setShipperOptions() {
    var url = path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=";
    if ($("#shipperSearchState").val() !== "" || $("#shipperSearchCountry").val() !== "" || $("#shipperSearchZip").val() !== "" || $("#shipperSearchSalesCode").val() !== "") {
        url += "SHIPPER_WITH_OPTIONS&template=tradingPartner&fieldIndex=1,2&";
        $("#shipperName").setOptions({
            url: url,
            extraParams: {
                param1: jQuery("#shipperSearchState").val(),
                param2: jQuery("#shipperSearchCountryCode").val(),
                param3: jQuery("#shipperSearchZip").val(),
                param4: jQuery("#shipperSearchSalesCode").val()
            }
        });
    } else {
        url += "SHIPPER&template=tradingPartner&fieldIndex=1,2&";
        $("#shipperName").setOptions({
            url: url
        });
    }
}
function setMasterShipperOptions() {
    var url = path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=";

    if ($("#masterSearchState").val() !== "" || $("#masterSearchCountry").val() !== "" || $("#masterSearchZip").val() !== "" || $("#masterSearchSalesCode").val() !== "") {
     url += "MASTER_SHIPPER_WITH_OPTIONS&template=tradingPartner&fieldIndex=1,2&";
        $("#masterShipper").setOptions({
            url: url,
            extraParams: {
                param1: jQuery("#masterSearchState").val(),
                param2: jQuery("#masterSearchCountryCode").val(),
                param3: jQuery("#masterSearchZip").val(),
                param4: jQuery("#masterSearchSalesCode").val()
            }
        });
    } else {
        url += "MASTERSHIPPER&template=tradingPartner&fieldIndex=1,2&";
        $("#masterShipper").setOptions({
            url: url
        });
    }
}
function setForwarderOptions() {
    var url = path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=";
    if ($("#forwarderSearchState").val() !== "" || $("#forwarderSearchCountry").val() !== "" || $("#forwarderSearchZip").val() !== "" || $("#forwarderSearchSalesCode").val() !== "") {
        url += "FORWARDER_WITH_OPTIONS&template=tradingPartner&fieldIndex=1,2&";
        $("#forwarderName").setOptions({
            url: url,
            extraParams: {
                param1: jQuery("#forwarderSearchState").val(),
                param2: jQuery("#forwarderSearchCountryCode").val(),
                param3: jQuery("#forwarderSearchZip").val(),
                param4: jQuery("#forwarderSearchSalesCode").val()
            }
        });
    } else {
        url += "FORWARDER&template=tradingPartner&fieldIndex=1,2&";
        $("#forwarderName").setOptions({
            url: url
        });
    }
}
function setConsigneeOptions() {
    var url = path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=";
    if ($("#consigneeSearchState").val() !== "" || $("#consigneeSearchCountry").val() !== "" || $("#consigneeSearchZip").val() !== "" || $("#consigneeSearchSalesCode").val() !== "") {
        url += "CONSIGNEE_WITH_OPTIONS&template=tradingPartner&fieldIndex=1,2&";
        $("#consigneeName").setOptions({
            url: url,
            extraParams: {
                param1: jQuery("#consigneeSearchState").val(),
                param2: jQuery("#consigneeSearchCountryCode").val(),
                param3: jQuery("#consigneeSearchZip").val(),
                param4: jQuery("#consigneeSearchSalesCode").val()
            }
        });
    } else {
        url += "CONSIGNEE&template=tradingPartner&fieldIndex=1,2&";
        $("#consigneeName").setOptions({
            url: url
        });
    }
}
