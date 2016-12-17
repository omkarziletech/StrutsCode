/* 
 * Document   : job
 * Created on : Jun 27, 2013, 03:58:00 PM
 * Author     : Lakshmi Naryanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function refresh() {
    showPreloading();
    window.location = path + "/job.do";
}

function showLightBox(title, url, height, width, callback) {
    height = height || $(document).height() - 50;
    width = width || $(document).width() - 100;
    url += (url.indexOf("?") > -1 ? "&" : "?") + "TB_iframe&height=" + height + "&width=" + width;
    Lightbox.showPopUp(title, url, "sexylightbox", "", callback);
}

function onchangeFrequency(ele, id) {
    if ($(ele).val() === "TWICE A MONTH") {
	$("#day1" + id).removeClass("readonly").removeAttr("disabled");
	$("#day1" + id).empty();
	for (day = 1; day <= 31; day++) {
	    $("#day1" + id).append("<option value='" + day + "'>" + day + "</option>");
	}
	$("#day2" + id).removeClass("readonly").removeAttr("disabled");
	$("#day2" + id).empty();
	for (day = 1; day <= 31; day++) {
	    $("#day2" + id).append("<option value='" + day + "'>" + day + "</option>");
	}
	$("#hour" + id).removeClass("readonly").removeAttr("disabled");
	$("#hour" + id).empty();
	for (hour = 0; hour <= 23; hour++) {
	    $("#hour" + id).append("<option value='" + hour + "'>" + hour + "</option>");
	}
	$("#minute" + id).removeClass("readonly").removeAttr("disabled");
	$("#minute" + id).empty();
	for (minute = 0; minute <= 23; minute++) {
	    $("#minute" + id).append("<option value='" + minute + "'>" + minute + "</option>");
	}
    } else if ($(ele).val() === "MONTHLY") {
	$("#day1" + id).removeClass("readonly").removeAttr("disabled");
	$("#day1" + id).empty();
	for (day = 1; day <= 31; day++) {
	    $("#day1" + id).append("<option value='" + day + "'>" + day + "</option>");
	}
	$("#day2" + id).empty();
	$("#day2" + id).append("<option value='0'>0</option>");
	$("#day2" + id).addClass("readonly").attr("disabled", true);
	$("#hour" + id).removeClass("readonly").removeAttr("disabled");
	$("#hour" + id).empty();
	for (hour = 0; hour <= 23; hour++) {
	    $("#hour" + id).append("<option value='" + hour + "'>" + hour + "</option>");
	}
	$("#minute" + id).removeClass("readonly").removeAttr("disabled");
	$("#minute" + id).empty();
	for (minute = 0; minute <= 23; minute++) {
	    $("#minute" + id).append("<option value='" + minute + "'>" + minute + "</option>");
	}
    } else if ($(ele).val() === "WEEKLY") {
	$("#day1" + id).removeClass("readonly").removeAttr("disabled");
	$("#day1" + id).empty();
	for (day = 1; day <= 7; day++) {
	    $("#day1" + id).append("<option value='" + day + "'>" + day + "</option>");
	}
	$("#day2" + id).empty();
	$("#day2" + id).append("<option value='0'>0</option>");
	$("#day2" + id).addClass("readonly").attr("disabled", true);
	$("#hour" + id).removeClass("readonly").removeAttr("disabled");
	$("#hour" + id).empty();
	for (hour = 0; hour <= 23; hour++) {
	    $("#hour" + id).append("<option value='" + hour + "'>" + hour + "</option>");
	}
	$("#minute" + id).removeClass("readonly").removeAttr("disabled");
	$("#minute" + id).empty();
	for (minute = 0; minute <= 23; minute++) {
	    $("#minute" + id).append("<option value='" + minute + "'>" + minute + "</option>");
	}
    } else if ($(ele).val() === "DAILY") {
	$("#day1" + id).empty();
	$("#day1" + id).append("<option value='0'>0</option>");
	$("#day1" + id).addClass("readonly").attr("disabled", true);
	$("#day2" + id).empty();
	$("#day2" + id).append("<option value='0'>0</option>");
	$("#day2" + id).addClass("readonly").attr("disabled", true);
	$("#hour" + id).removeClass("readonly").removeAttr("disabled");
	$("#hour" + id).empty();
	for (hour = 0; hour <= 23; hour++) {
	    $("#hour" + id).append("<option value='" + hour + "'>" + hour + "</option>");
	}
	$("#minute" + id).removeClass("readonly").removeAttr("disabled");
	$("#minute" + id).empty();
	for (minute = 0; minute <= 23; minute++) {
	    $("#minute" + id).append("<option value='" + minute + "'>" + minute + "</option>");
	}
    } else if ($(ele).val() === "HOURLY") {
	$("#day1" + id).empty();
	$("#day1" + id).append("<option value='0'>0</option>");
	$("#day1" + id).addClass("readonly").attr("disabled", true);
	$("#day2" + id).empty();
	$("#day2" + id).append("<option value='0'>0</option>");
	$("#day2" + id).addClass("readonly").attr("disabled", true);
	$("#hour" + id).removeClass("readonly").removeAttr("disabled");
	$("#hour" + id).empty();
	for (hour = 1; hour <= 24; hour++) {
	    $("#hour" + id).append("<option value='" + hour + "'>" + hour + "</option>");
	}
	$("#minute" + id).val(0).addClass("readonly").attr("disabled", true);
    } else {
	$("#day1" + id).empty();
	$("#day1" + id).append("<option value='0'>0</option>");
	$("#day1" + id).addClass("readonly").attr("disabled", true);
	$("#day2" + id).empty();
	$("#day2" + id).append("<option value='0'>0</option>");
	$("#day2" + id).addClass("readonly").attr("disabled", true);
	$("#hour" + id).val(0).addClass("readonly").attr("disabled", true);
	$("#minute" + id).removeClass("readonly").removeAttr("disabled");
	$("#minute" + id).empty();
	for (minute = 1; minute <= 60; minute++) {
	    $("#minute" + id).append("<option value='" + minute + "'>" + minute + "</option>");
	}
    }
}

function save(id) {
    $("#frequencyValue" + id).attr("name", "frequency");
    $("#day1" + id).attr("name", "day1");
    $("#day2" + id).attr("name", "day2");
    $("#hour" + id).attr("name", "hour");
    $("#minute" + id).attr("name", "minute");
    $("#enabled" + id).attr("name", "enabled").val($("#enabled" + id).is(":checked"));
    $("#id" + id).attr("name", "id");
    $("#action").val("save");
    $("#jobForm").submit();
}

var data;
var $row;
function showResult(result) {
    result = result.split("===");
    var startTime = result[0];
    var endTime = result[1];
    var message = result[2];
    $(".message").html(message);
    $row.find("td:eq(9)").text(startTime);
    $row.find("td:eq(10)").html(endTime);
}

function storeData(result) {
    data = result;
}
function showResultForACH() {
    showResult(data);
}

function run(id, name, ele) {
    $row = $(ele).parent().parent();
    var url = $("#jobForm").attr("action");
    ajaxCall(url, {
	data: {
	    action: "run",
	    id: $("#id" + id).val()
	},
	preloading: $.trim(name) === "ACH Transaction" ? false : true,
	success: $.trim(name) === "ACH Transaction" ? "storeData" : "showResult",
	async: $.trim(name) === "ACH Transaction" ? true : false
    });
    if ($.trim(name) === "ACH Transaction") {
	var url = path + "/job.do?action=showProgressBar";
	showLightBox("ACH Transaction", url, 300, 650, "showResultForACH()");
    }
}

function preview() {
    var url = path + "/job.do?action=searchAch";
    showLightBox("ACH Transactions", url, 400, 800, "");
}

$(document).ready(function() {
    $("#jobForm").submit(function() {
	showPreloading();
    });
    $("[title != '']").not("link").tooltip();
    $(".switch").jswitch();
    Lightbox.initialize({
	color: 'black',
	dir: path + '/js/lightbox/images',
	moveDuration: 1,
	resizeDuration: 1
    });
});