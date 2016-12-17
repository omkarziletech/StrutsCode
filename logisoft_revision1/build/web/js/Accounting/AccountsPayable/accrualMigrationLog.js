$(document).ready(function(){
    $(".scrolldisplaytable").height($("body").height()-$("#searchFilters").height()-110)
    $(document).keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == 13) {
            search();
        }
    });
});
function onChangeDate(obj){
    if($.trim($(obj).val())!="" && !isDate($(obj))){
	$(obj).val("").select().fadeIn().fadeOut().fadeIn();
	$.prompt("Please enter date in mm/dd/yyyy format");
    }
}

function onChangeSearchBy(obj){
    if($.trim($(obj).val())!=""){
	$("#searchValue").removeClass("textlabelsBoldForTextBoxDisabledLook").addClass("textlabelsBoldForTextBox").removeAttr("readonly");
	$("#searchValue").val("").select().fadeIn().fadeOut().fadeIn();
    }else{
	$("#searchValue").val("");
	$("#searchValue").addClass("textlabelsBoldForTextBoxDisabledLook").attr("readonly",true);
    }
}

function search(){
    $("#action").val("search");
    $("#currentPageNo").val("1");
    $("#sortBy").val("reportedDate");
    $("#orderBy").val("desc");
    $("#accrualMigrationLogForm").submit();
}

function refresh(){
    $("#action").val("clear");
    $("#accrualMigrationLogForm").submit();
}

function gotoPage(pageNo){
    $("#currentPageNo").val(pageNo);
    $("#action").val("search");
    $("#accrualMigrationLogForm").submit();
}

function gotoSelectedPage(){
    $("#currentPageNo").val($("#selectedPageNo").val());
    $("#action").val("search");
    $("#accrualMigrationLogForm").submit();
}

function doSort(sortBy,orderBy){
    $("#sortBy").val(sortBy);
    $("#orderBy").val(orderBy);
    $("#action").val("search");
    $("#accrualMigrationLogForm").submit();
}

function showErrorFile(id){
    showPreloading();
    $.post($("#accrualMigrationLogForm").attr("action"), {
	id: id, 
	action: "showErrorFile"
    },
    function(data) {
	closePreloading();
	showMask();
	$("<div style='width:90%;height:230px'></div>").html(data).addClass("popup").appendTo("#accrualMigrationLogForm").center();
    });
}

function updateErrorFile(id){
    $.prompt("Do you want to save the changes?",{
	buttons:{
	    Yes:true,
	    No:false
	},
	callback: function(v,m,f){
	    if(v){
		showPreloading();
		var params = $(".input-text").serialize();
		params+="&id="+id+"&action=updateErrorFile";
		$.post($("#accrualMigrationLogForm").attr("action"), params,
		    function(data) {
			closePreloading();
			if(data=="success"){
			    closePopUpDiv();
			    $.prompt("The csv file is updated and ready to reprocess..");
			}else{
			    $.prompt(data);
			}
		    }
		    );
	    }
	}
    });
}

function showReprocessLogs(id){
    showPreloading();
    $.post($("#accrualMigrationLogForm").attr("action"), {
	id: id, 
	action: "showReprocessLogs"
    },
    function(data) {
	closePreloading();
	showMask();
	$("<div style='width:600px;height:220px'></div>").html(data).addClass("popup").appendTo("#accrualMigrationLogForm").center();
    });
}

function archiveLog(id,obj){
    $.prompt("Do you want to archive this error log?",{
	buttons:{
	    Yes:true,
	    No:false
	},
	callback: function(v,m,f){
	    if(v){
		showPreloading();
		$.post($("#accrualMigrationLogForm").attr("action"), {
		    id: id, 
		    action: "archive"
		}, function(data) {
		    closePreloading();
		    if(data=="success"){
			$(obj).parent().parent().remove();
			$.prompt("The log is archived");
		    }else{
			$.prompt(data);
		    }
		});
	    }
	}
    });
}

function reprocessAllErrors(){
    $.prompt("Do you want to reprocess all the errors?",{
	buttons:{
	    Yes:true,
	    No:false
	},
	callback: function(v,m,f){
	    if(v){
		$("#log tbody").empty();
		showPreloading();
		$.post($("#accrualMigrationLogForm").attr("action"), {
		    action: "reprocessAllErrors"
		}, function(data) {
		    closePreloading();
		    if(data=="success"){
			$.prompt("All the error files got reprocessed");
		    }else{
			showMask();
			$("<div style='width:900px;height:220px'></div>").html(data).addClass("popup").appendTo("#accrualMigrationLogForm").center();
		    }
		});
	    }
	}
    });
}

function loadMissingAccruals(){
    $.prompt("Do you want to load all the missing accruals?",{
	buttons:{
	    Yes:true,
	    No:false
	},
	callback: function(v,m,f){
	    if(v){
		$("#log tbody").empty();
		showPreloading();
		$.post($("#accrualMigrationLogForm").attr("action"), {
		    action: "loadMissingAccruals"
		}, function(data) {
		    closePreloading();
		    if(!isNaN(data)){
			var result = data==0?"No accrual is missed":data==1?"1 missed accrual is loaded":data+" missed accruals are loaded";
			$.prompt(result);
		    }else{
			showMask();
			$("<div style='width:900px;height:220px'></div>").html(data).addClass("popup").appendTo("#accrualMigrationLogForm").center();
		    }
		});
	    }
	}
    });
}

function reprocessSingleError(id,obj){
    $.prompt("Do you want to reprocess this error?",{
	buttons:{
	    Yes:true,
	    No:false
	},
	callback: function(v,m,f){
	    if(v){
		showPreloading();
		$.post($("#accrualMigrationLogForm").attr("action"), {
		    id: id, 
		    action: "reprocessSingleError"
		}, function(data) {
		    closePreloading();
		    if(data=="success"){
			$(obj).parent().parent().remove();
			$.prompt("The file got reprocessed");
		    }else if(data=="warning"){
			$(obj).parent().parent().remove();
			$.prompt("The file got reprocessed but with some warnings");
		    }else{
			showMask();
			$("<div style='width:600px;height:220px'></div>").html(data).addClass("popup").appendTo("#accrualMigrationLogForm").center();
		    }
		});
	    }
	}
    });
}

function printControlReport(){
    if($.trim($("#txtcal1").val())=="" || !isDate($("#txtcal1"))){
	$("#txtcal1").val("").select().fadeIn().fadeOut().fadeIn();
	$.prompt("Please enter from date in mm/dd/yyyy format");
    }else if($.trim($("#txtcal2").val())=="" || !isDate($("#txtcal2"))){
	$("#txtcal2").val("").select().fadeIn().fadeOut().fadeIn();
	$.prompt("Please enter to date in mm/dd/yyyy format");
    }else{
	$("#action").val("printControlReport");
	$("#accrualMigrationLogForm").submit();
    }
}

function exportControlReport(){
    if($.trim($("#txtcal1").val())=="" || !isDate($("#txtcal1"))){
	$("#txtcal1").val("").select().fadeIn().fadeOut().fadeIn();
	$.prompt("Please enter from date in mm/dd/yyyy format");
    }else if($.trim($("#txtcal2").val())=="" || !isDate($("#txtcal2"))){
	$("#txtcal2").val("").select().fadeIn().fadeOut().fadeIn();
	$.prompt("Please enter to date in mm/dd/yyyy format");
    }else{
	$("#action").val("exportControlReport");
	$("#accrualMigrationLogForm").submit();
	closePreloading();
    }
}
