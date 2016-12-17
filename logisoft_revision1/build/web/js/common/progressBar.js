/* 
 * Document   : progressBar
 * Created on : Nov 1, 2013, 9:50:00 PM
 * Author     : Lakshmi Naryanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function updateProgressBar(progressBar) {
    if ($.trim(progressBar) !== "") {
	progressBar = progressBar.split("===");
	var message = progressBar[0];
	var percentage = progressBar[1];
	$('.progress .bar').stop(true).animate({
	    width: percentage + '%'
	}, {
	    step: function(now) {
		$(this).text(Math.round(now) + '%');
	    },
	    duration: 1000
	});
	$(".status ul").html(message);
	if (percentage === '100') {
	    window.parent.$("#SLB-CloseButton").show();
	} else {
	    setTimeout("getProgressBar()", 1000);
	}
    } else {
	$('.progress .bar').stop(true).animate({
	    width: '100%'
	}, {
	    step: function(now) {
		$(this).text(Math.round(now) + '%');
	    },
	    duration: 1000
	});
	window.parent.$("#SLB-CloseButton").show();
    }
}

function getProgressBar() {
    var url = path + "/job.do";
    ajaxCall(url, {
	data: {
	    action: "updateProgressBar"
	},
	preloading: false,
	success: "updateProgressBar",
	async: false
    });
}
$(document).ready(function() {
    window.parent.$("#SLB-CloseButton").hide();
    getProgressBar();
});