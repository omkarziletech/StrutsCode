/* 
 * Document   : achPopup
 * Created on : Nov 2, 2013, 8:27:00 PM
 * Author     : Lakshmi Naryanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function dateChange(ele1, ele2) {
    if ($.trim($(ele1).val()) !== "") {
	if (!isDate($(ele1))) {
	    $.prompt("Please enter valid from date", {
		callback: function() {
		    $(ele1).val("").callFocus();
		}
	    });
	} else if (ele1 === "#fromDate" && isGreaterThan($(ele1), $(ele2))) {
	    $.prompt("From date cannot be greater than to date", {
		callback: function() {
		    $(ele1).val("").callFocus();
		}
	    });
	} else if (ele1 === "#toDate" && isGreaterThan($(ele2), $(ele1))) {
	    $.prompt("To date cannot be lesser than from date", {
		callback: function() {
		    $(ele1).val("").callFocus();
		}
	    });
	}
    }
}

function initDateFields() {
    $("#fromDate").datepick({
	showOnFocus: false,
	showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='From Date'/>",
	onClose: function() {
	    dateChange("#fromDate", "#toDate");
	}
    }).change(function() {
	dateChange("#fromDate", "#toDate");
    });
    $("#toDate").datepick({
	showOnFocus: false,
	showTrigger: "<img src='" + path + "/images/icons/calendar-blue.gif' class='trigger' title='To Date'/>",
	onClose: function() {
	    dateChange("#toDate", "#fromDate");
	}
    }).change(function() {
	dateChange("#toDate", "#fromDate");
    });
}

function doSearch() {
    $("#action").val("searchAch");
    $("#jobForm").submit();
}

function doClear() {
    $("#action").val("clearAch");
    $("#jobForm").submit();
}

function showLightBox(title, url, height, width, callback) {
    height = height || $(document).height() - 50;
    width = width || $(document).width() - 100;
    url += (url.indexOf("?") > -1 ? "&" : "?") + "TB_iframe&height=" + height + "&width=" + width;
    Lightbox.showPopUp(title, url, "sexylightbox", "", callback);
}

function showAchFile(achId, filename) {
    var type = filename.indexOf(".pgp") > 0 ? "pgp" : "txt";
    var url = path + "/job.do?action=showAchFile&achId=" + achId + "&type=" + type;
    showLightBox(filename, url, 375, 700, "");
}

var message;
var $ele;
function showResult(result) {
    message = result;
}

function showMessage() {
    $(".message").html(message);
    if (message === "Completed") {
	$ele.remove();
    }
}

function rerun(achId, ele) {
    $ele = $(ele);
    var url = $("#jobForm").attr("action");
    ajaxCall(url, {
	data: {
	    action: "rerun",
	    achId: achId
	},
	preloading: false,
	success: "showResult",
	async: true
    });
    url = path + "/job.do?action=showProgressBar";
    showLightBox("ACH Transaction", url, 300, 650, "showMessage()");
}

$(document).ready(function() {
    $("#jobForm").submit(function() {
	showPreloading();
    });
    $("[title != '']").not("link").tooltip();
    initDateFields();
    Lightbox.initialize({
	color: 'black',
	dir: path + '/js/lightbox/images',
	moveDuration: 1,
	resizeDuration: 1
    });
});