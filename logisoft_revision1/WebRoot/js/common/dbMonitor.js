/* 
 * Document   : dbMonitor
 * Created on : Nov 29, 2012, 05:04:00 PM
 * Author     : Lakshmi Naryanan
 */
function refresh(){
    $("#action").val("refresh");
    $("#dbMonitorForm").submit();
}

function gotoPage(pageNo){
    $("#selectedPage").val(pageNo);
    refresh();
}

function gotoSelectedPage(){
    $("#selectedPage").val($("#selectedPageNo").val());
    refresh();
}

function doSort(sortBy){
    $("#sortBy").val(sortBy);
    if($("#orderBy").val() == "desc"){
	$("#orderBy").val("asc");
    }else{
	$("#orderBy").val("desc");
    }
    refresh();
}

function setResultHeight(){
    if($(".result-container").length>0){
	var windowHeight = window.parent.getFrameHeight();
	$(".result-container").height(windowHeight-$(".table-banner").height()-100);
	$("body").css("overflow","hidden");
    }
}

function killProcess(id){
    $.prompt("Are you sure want to kill this process - "+id+"?",{
	buttons:{
	    Yes:true,
	    No:false
	},
	callback: function(v,m,f){
	    if(v){
		$("#id").val(id);
		$("#add-container").html("");
		$("#action").val("killProcess");
		$("#dbMonitorForm").submit();
	    }
	}
    });
}

function showInfo(id){
    showAlternateMask();
    $("#info-container"+id).center().show(500);
}

function closeInfo(id){
    hideAlternateMask();
    $("#info-container"+id).center().hide(500);
}

$(document).ready(function(){
    $("#dbMonitorForm").submit(function(){
	showPreloading();
    });
    $("[title != '']").not("link").tooltip();
    $(window).resize(function(){
	window.parent.changeHeight();
	if($(".result-container").length>0){
	    setResultHeight();
	}
    });
});