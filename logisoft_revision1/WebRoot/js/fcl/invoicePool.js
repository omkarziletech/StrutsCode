/* 
 *  Document   : invoicePool
 *  Created on : Mar 08, 2013, 5:57:00 PM
 *  Author     : Lakshmi Narayanan
 */

function fromDateChange() {
    if ($.trim($("#fromDate").val()) != "" && !validateDate($("#fromDate"))) {
	$.prompt("Please enter valid from date", {
	    callback: function() {
		$("#fromDate").val("").callFocus();
	    }
	});
    }
}

function toDateChange() {
    if ($.trim($("#toDate").val()) != "" && !validateDate($("#toDate"))) {
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
	showTrigger: "<img src='"+path+"/images/icons/calendar-blue.gif' class='trigger' title='From Date'/>",
	onClose: function() {
	    fromDateChange();
	}
    }).change(function() {
	fromDateChange();
    });
    $("#toDate").datepick({
	showOnFocus: false, 
	showTrigger: "<img src='"+path+"/images/icons/calendar-blue.gif' class='trigger' title='To Date'/>",
	onClose: function() {
	    toDateChange();
	}
    }).change(function() {
	toDateChange();
    });
}

function initAutoCompleteFields() {
    $("#customerName").initautocomplete({
	url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=CLIENT&template=tradingPartner&fieldIndex=1,2&",
	width: "480px",
	otherFields: "customerNumber",
	resultsClass: "ac_results z-index",
	resultPosition: "absolute",
	scroll: true,
	scrollHeight: 300
    });
}

function search() {
    $("#action").val("search");
    $("#invoicePoolForm").submit();
}

function gotoPage(pageNo){
    $("#selectedPage").val(pageNo);
    $("#action").val("gotoPage");
    $("#invoicePoolForm").submit();
}

function gotoSelectedPage(){
    $("#selectedPage").val($("#selectedPageNo").val());
    $("#action").val("gotoPage");
    $("#invoicePoolForm").submit();
}

function doSort(sortBy){
    $("#sortBy").val(sortBy);
    if($("#orderBy").val() == "desc"){
	$("#orderBy").val("asc");
    }else{
	$("#orderBy").val("desc");
    }
    $("#action").val("doSort");
    $("#invoicePoolForm").submit();
}

function resetAll() {
    $("#action").val("reset");
    $("#invoicePoolForm").submit();
}

function setResultHeight() {
    if ($(".result-container").length > 0) {
	//var windowHeight = window.parent.getFrameHeight();
	var windowHeight = $(window).height();
	var height = windowHeight;
	height -= $(".table-banner").height();
	height -= 150;
	$(".result-container").height(height);
	$("body").css("overflow", "hidden");
    }
}

var selectedFileNumber;
function showMessage(result) {
    if(result.indexOf("available========") >- 1){
	var itemId = result.replace("available========","");
	window.parent.gotoFclOpsScreens(selectedFileNumber,itemId);
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
    var url = $("#invoicePoolForm").attr("action");
    var params = "action=checkLocking";
    params += "&fileNumber=" + fileNumber;
    ajaxCall(url, {
	data: params,
	preloading: true,
	success: "showMessage",
	async: false
    });
}

function showPreview(fileName){
    var title = "Misc Invoice";
    var url = path+"/servlet/FileViewerServlet?fileName="+fileName;
    window.parent.showLightBox(title,url);
}

function createPreview(id){
    var url = $("#invoicePoolForm").attr("action");
    var params = "action=preview";
    params += "&id=" + id;
    ajaxCall(url, {
	data: params,
	preloading: true,
	success: "showPreview",
	async: false
    });
}

$(document).ready(function() {
    $("#invoicePoolForm").submit(function() {
	window.parent.showPreloading();
    });
    window.parent.closePreloading();
    initDate();
    initAutoCompleteFields();
    setResultHeight();
    $("[title != '']").not("link").tooltip();
});