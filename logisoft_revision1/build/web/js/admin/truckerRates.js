/* 
 * Document   : truckerRates
 * Created on : 10 Sep, 2012, 6:07:45 PM
 * Author     : Lakshmi Narayanan
 */

function uploadRates() {
    var ext = $("#uploadFile").val().split(/\\/).pop().split('.').pop().toLowerCase();
    if ($.trim($("#uploadFile").val()) !== "" && (ext === "csv")) {
	$("#action").val("upload");
	$("#truckerRatesForm").submit();
    } else {
	$.prompt("Please upload csv filetype only");
    }
}

function showErrorRates(){
    if($("#errorRates").is(":checked")){
	$("#truckerName").val("").addClass("readonly").attr("readonly", true);
	$("#truckerNameCheck").val("");
	$("#truckerNumber").val("");
	$("#fromZip").val("").addClass("readonly").attr("readonly", true);
	$("#fromZipCheck").val("");
	$("#toPortCode").val("").addClass("readonly").attr("readonly", true);
	$("#toPortCodeCheck").val("");
	$("#toPort").val("");
    }else{
	$("#truckerName").val("").removeClass("readonly").removeAttr("readonly", true);
	$("#fromZip").val("").removeClass("readonly").removeAttr("readonly", true);
	$("#toPortCode").val("").removeClass("readonly").removeAttr("readonly", true);
    }
}

function search() {
    $("#sortBy").val("tr.id");
    $("#orderBy").val("asc");
    $("#selectedPage").val("1");
    $("#action").val("search");
    $("#truckerRatesForm").submit();
}

function gotoPage(pageNo) {
    $("#selectedPage").val(pageNo);
    $("#action").val("gotoPage");
    $("#truckerRatesForm").submit();
}

function gotoSelectedPage() {
    $("#selectedPage").val($("#selectedPageNo").val());
    $("#action").val("gotoPage");
    $("#truckerRatesForm").submit();
}

function doSort(sortBy, orderBy) {
    $("#sortBy").val(sortBy);
    $("#orderBy").val(orderBy);
    $("#action").val("doSort");
    $("#truckerRatesForm").submit();
}

function goBack() {
    $("#action").val("goBack");
    $("#truckerRatesForm").submit();
}

function success(result) {
    $.prompt(result);
}

function update(data) {
    var url = $("#truckerRatesForm").attr("action");
    ajaxCall(url, {
	data: data,
	preloading: true,
	success: "success",
	async: false
    });
}

function updateTrucker(id) {
    if ($.trim($("#truckerNumber" + id).val()) !== "") {
	var data = {
	    action: "update",
	    "truckerRates.id": id,
	    "truckerRates.truckerName": $.trim($("#truckerName" + id).val()),
	    "truckerRates.truckerNumber": $.trim($("#truckerNumber" + id).val())
	};
	update(data);
    }
}

function updateFromZip(id) {
    if ($.trim($("#fromZip" + id).val()) !== "") {
	var data = {
	    action: "update",
	    "truckerRates.id": id,
	    "truckerRates.fromZip": $.trim($("#fromZip" + id).val()),
	    "truckerRates.fromCity": $.trim($("#fromCity" + id).val()),
	    "truckerRates.fromState": $.trim($("#fromState" + id).val())
	};
	update(data);
    }
}

function updateToPort(id) {
    if ($.trim($("#toPort" + id).val()) !== "") {
	var data = {
	    action: "update",
	    "truckerRates.id": id,
	    "truckerRates.toPort": $.trim($("#toPort" + id).val()),
	    "truckerRates.toPortCode": $.trim($("#toPortCode" + id).val())
	};
	update(data);
    }
}

function initAutoCompleteFields() {
    $("#truckerName").initautocomplete({
	url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=VENDOR&template=tradingPartner&fieldIndex=1,2&",
	width: "480px",
	otherFields: "truckerNumber",
	resultsClass: "ac_results z-index",
	resultPosition: "fixed",
	scroll: true,
	scrollHeight: 200
    });
    $("#fromZip").initautocomplete({
	url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=ZIP&template=zip&fieldIndex=1&",
	width: "420px",
	resultsClass: "ac_results z-index",
	resultPosition: "fixed",
	scroll: true,
	scrollHeight: 200
    });
    $("#toPortCode").initautocomplete({
	url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=UN_LOC_CODE&template=unloccode&fieldIndex=1,4&",
	width: "420px",
	otherFields: "toPort",
	resultsClass: "ac_results z-index",
	resultPosition: "fixed",
	scroll: true,
	scrollHeight: 200
    });
    $(".truckerName").each(function() {
	var row = $(this).parent().parent();
	var id = row.find(".id").val();
	$(this).initautocomplete({
	    url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=VENDOR&template=tradingPartner&fieldIndex=1,2&",
	    width: "480px",
	    otherFields: "truckerNumber",
	    row: true,
	    checkClass: "truckerNameCheck",
	    callback: "updateTrucker(" + id + ")",
	    resultsClass: "ac_results z-index",
	    resultPosition: "fixed",
	    scroll: true,
	    scrollHeight: 200
	});
    });
    $(".fromZip").each(function() {
	var row = $(this).parent().parent();
	var id = row.find(".id").val();
	$(this).initautocomplete({
	    url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=ZIP&template=zip&fieldIndex=1,2,3&",
	    width: "420px",
	    otherFields: "fromCity^fromState",
	    row: true,
	    checkClass: "fromZipCheck",
	    callback: "updateFromZip(" + id + ")",
	    resultsClass: "ac_results z-index",
	    resultPosition: "fixed",
	    scroll: true,
	    scrollHeight: 200
	});
    });
    $(".toPortCode").each(function() {
	var row = $(this).parent().parent();
	var id = row.find(".id").val();
	$(this).initautocomplete({
	    url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=UN_LOC_CODE&template=unloccode&fieldIndex=1,4&",
	    width: "420px",
	    otherFields: "toPort",
	    row: true,
	    checkClass: "toPortCodeCheck",
	    callback: "updateToPort(" + id + ")",
	    resultsClass: "ac_results z-index",
	    resultPosition: "fixed",
	    scroll: true,
	    scrollHeight: 200
	});
    });
}

function setResultHeight() {
    var windowHeight = window.parent.getFrameHeight();
    $(".search-results").height(windowHeight - $(".table-banner").height() - 150);
    $("body").css("overflow", "hidden");
}

$(document).ready(function() {
    $("#uploadFile").fileInput();
    if ($(".search-results").length > 0) {
	setResultHeight();
	$(window).resize(function() {
	    window.parent.changeHeight();
	    setResultHeight();
	});
	$("[title != '']").not("link").tooltip();
    }
    initAutoCompleteFields();
});
