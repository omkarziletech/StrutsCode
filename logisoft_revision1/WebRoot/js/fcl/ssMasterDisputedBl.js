/* 
 *  Document   : ssMasterDisputedBl
 *  Created on : Mar 11, 2013, 4:38:00 PM
 *  Author     : Lakshmi Narayanan
 */

function etaChange() {
    if ($.trim($("#eta").val()) != "" && !validateDate($("#eta"))) {
	$.prompt("Please enter valid eta", {
	    callback: function() {
		$("#eta").val("").callFocus();
	    }
	});
    }
}

function etdChange() {
    if ($.trim($("#etd").val()) != "" && !validateDate($("#etd"))) {
	$.prompt("Please enter valid etd", {
	    callback: function() {
		$("#etd").val("").callFocus();
	    }
	});
    }
}

function initDate() {
    $("#eta").datepick({
	showOnFocus: false, 
	showTrigger: "<img src='"+path+"/images/icons/calendar-blue.gif' class='trigger' title='ETA'/>",
	onClose: function() {
	    etaChange();
	}
    }).change(function() {
	etaChange();
    });
    $("#etd").datepick({
	showOnFocus: false, 
	showTrigger: "<img src='"+path+"/images/icons/calendar-blue.gif' class='trigger' title='ETD'/>",
	onClose: function() {
	    etdChange();
	}
    }).change(function() {
	etdChange();
    });
}

function initAutoCompleteFields() {
    $("#origin,#pol,#pod,#destination").initautocomplete({
	url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=PORT&template=port&fieldIndex=1&",
	width: "420px",
	resultsClass: "ac_results z-index",
	resultPosition: "fixed",
	scroll: true,
	scrollHeight: 300
    });
    $("#sslineName").initautocomplete({
	url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=SSL&template=tradingPartner&fieldIndex=1,2&",
	width: "480px",
	otherFields: "sslineNumber",
	resultsClass: "ac_results z-index",
	resultPosition: "absolute",
	scroll: true,
	scrollHeight: 300
    });
}

function search() {
    $("#action").val("search");
    $("#ssMasterDisputedBlForm").submit();
}

function doSort(sortBy){
    $("#sortBy").val(sortBy);
    if($("#orderBy").val() == "desc"){
	$("#orderBy").val("asc");
    }else{
	$("#orderBy").val("desc");
    }
    $("#action").val("doSort");
    $("#ssMasterDisputedBlForm").submit();
}

function resetAll() {
    $("#action").val("reset");
    $("#ssMasterDisputedBlForm").submit();
}

function setResultHeight() {
    if ($(".scrollable-table").length > 0) {
	//var windowHeight = window.parent.getFrameHeight();
	var windowHeight = $(window).height();
	var height = windowHeight;
	height -= $(".table-banner").height();
	height -= 150;
	$(".scrollable-table").height(height);
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
    var url = $("#ssMasterDisputedBlForm").attr("action");
    var params = "action=checkLocking";
    params += "&fileNumber=" + fileNumber;
    ajaxCall(url, {
	data: params,
	preloading: true,
	success: "showMessage",
	async: false
    });
}

function updateAckComments(result){
    $("#ackComments"+selectedFileNumber).attr({
	src: path+"/images/icons/dots/dark-green.gif",
	title: result
    }).tooltip().removeAttr("onclick");
}

function acknowledge(fileNumber){
    $.prompt("Are you sure you want to take ownership of this disputed file?", {
	buttons: {
	    "Yes": true,
	    "No": false
	},
	callback: function(v, m, f) {
	    if (v) {
		selectedFileNumber = fileNumber;
		var url = $("#ssMasterDisputedBlForm").attr("action");
		var params = "action=acknowledge";
		params += "&fileNumber=" + fileNumber;
		ajaxCall(url, {
		    data: params,
		    preloading: true,
		    success: "updateAckComments",
		    async: false
		});
	    }
	}
    });
}

function showLightBox(title, url, height, width) {
    height = height || $(document).height() - 50;
    width = width || $(document).width() - 100;
    url += (url.indexOf("?") > -1 ? "&" : "?") + "TB_iframe&height=" + height + "&width=" + width;
    Lightbox.showPopUp(title, url, "sexylightbox", "", "");
}

function showNotes(url){
    var title = "Notes";
    showLightBox(title,url,400,800);
}

function showDocuments(url){
    var title = "Documents List";
    showLightBox(title,url,400,800);
}

$(document).ready(function() {
    window.parent.closePreloading();
    $("#ssMasterDisputedBlForm").submit(function() {
	window.parent.showPreloading();
    });
    initDate();
    initAutoCompleteFields();
    setResultHeight();
    $("[title != '']").not("link").tooltip();
    Lightbox.initialize({
	color: 'black',
	dir: path + '/js/lightbox/images',
	moveDuration: 1,
	resizeDuration: 1
    });
});