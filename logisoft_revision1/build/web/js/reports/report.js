/* 
 * Document   : report
 * Created on : Nov 11, 2012, 10:12:00 PM
 * Author     : Lakshmi Naryanan
 */

var emailPattern = "^[A-Za-z0-9\._%-]+@[A-Za-z0-9\.-]+\.[A-Za-z]{2,4}(?:[;][A-Za-z0-9\._%-]+@[A-Za-z0-9\.-]+\.[A-Za-z]{2,4}?)*";

function search(){
    $("#action").val("search");
    $("#reportForm").submit();
}

function gotoPage(pageNo){
    $("#selectedPage").val(pageNo);
    search();
}

function gotoSelectedPage(){
    $("#selectedPage").val($("#selectedPageNo").val());
    search();
}

function doSort(sortBy){
    $("#sortBy").val(sortBy);
    if($("#orderBy").val() == "desc"){
	$("#orderBy").val("asc");
    }else{
	$("#orderBy").val("desc");
    }
    search();
}

function clearScreen(){
    $("#action").val("clear");
    $("#reportForm").submit();
}

function closeAddReport(){
    $("#add-container").hide(500);
    hideAlternateMask();
}

function addReport(){
    showAlternateMask();
    $("#add-container").center().show(500);
}

function onchangeScheduleFrequency(ele,index){
    if(index){
	if($(ele).val() == "TWICE A MONTH"){
	    $("#scheduleDay1"+index).removeClass("readonly").removeAttr("disabled");
	    $("#scheduleDay2"+index).removeClass("readonly").removeAttr("disabled");
	    $("#scheduleDay1"+index).empty();
	    for(day=1; day<=31; day++){
		$("#scheduleDay1"+index).append("<option value='"+day+"'>"+day+"</option>");
	    }
	}else if($(ele).val() == "MONTHLY"){
	    $("#scheduleDay1"+index).removeClass("readonly").removeAttr("disabled");
	    $("#scheduleDay2"+index).val(1).addClass("readonly").attr("disabled",true);
	    $("#scheduleDay1"+index).empty();
	    for(day=1; day<=31; day++){
		$("#scheduleDay1"+index).append("<option value='"+day+"'>"+day+"</option>");
	    }
	}else if($(ele).val() == "WEEKLY"){
	    $("#scheduleDay1"+index).removeClass("readonly").removeAttr("disabled");
	    $("#scheduleDay2"+index).val(1).addClass("readonly").attr("disabled",true);
	    $("#scheduleDay1"+index).empty();
	    for(day=1; day<=7; day++){
		$("#scheduleDay1"+index).append("<option value='"+day+"'>"+day+"</option>");
	    }
	}else{
	    $("#scheduleDay1"+index).val(1).addClass("readonly").attr("disabled",true);
	    $("#scheduleDay2"+index).val(1).addClass("readonly").attr("disabled",true);
	}
    }else{
	if($(ele).val() == "TWICE A MONTH"){
	    $("#scheduleDay1").removeClass("readonly").removeAttr("disabled");
	    $("#scheduleDay2").removeClass("readonly").removeAttr("disabled");
	    $("#scheduleDay1").empty();
	    for(day=1; day<=31; day++){
		$("#scheduleDay1").append("<option value='"+day+"'>"+day+"</option>");
	    }
	}else if($(ele).val() == "MONTHLY"){
	    $("#scheduleDay1").removeClass("readonly").removeAttr("disabled");
	    $("#scheduleDay2").val(1).addClass("readonly").attr("disabled",true);
	    $("#scheduleDay1").empty();
	    for(day=1; day<=31; day++){
		$("#scheduleDay1").append("<option value='"+day+"'>"+day+"</option>");
	    }
	}else if($(ele).val() == "WEEKLY"){
	    $("#scheduleDay1").removeClass("readonly").removeAttr("disabled");
	    $("#scheduleDay2").val(1).addClass("readonly").attr("disabled",true);
	    $("#scheduleDay1").empty();
	    for(day=1; day<=7; day++){
		$("#scheduleDay1").append("<option value='"+day+"'>"+day+"</option>");
	    }
	}else{
	    $("#scheduleDay1").val(1).addClass("readonly").attr("disabled",true);
	    $("#scheduleDay2").val(1).addClass("readonly").attr("disabled",true);
	}
    }
}

function validateReport(){
    $("#scheduleTime").val($("#scheduleHr").val()+":"+$("#scheduleMin").val()+":"+$("#scheduleSec").val())
    if($.trim($("#reportName").val()) == ""){
	$.prompt("Please enter Report Name",{
	    callback:function(){
		$("#reportName").callFocus();
	    }
	});
	return false;
    }else if($.trim($("#sender").val()) == ""){
	$.prompt("Please enter Report Sender",{
	    callback:function(){
		$("#senderName").callFocus();
	    }
	});
	return false;
    }else if($.trim($("#emailBody").val()) == ""){
	$.prompt("Please enter Email Body",{
	    callback:function(){
		$("#emailBody").callFocus();
	    }
	});
	return false;
    }else if($.trim($("#query1").val()) == ""){
	$.prompt("Please enter Query 1",{
	    callback:function(){
		$("#query1").callFocus();
	    }
	});
	return false;
    }else if($.trim($("#query1").val()).indexOf(" as 'Email'")<0 && $.trim($("#query1").val()).indexOf(" as email")<0){
	$.prompt("Email column must be required in Query 1.",{
	    callback:function(){
		$("#query1").callFocus();
	    }
	});
	return false;
    }else if($.trim($("#query2").val()) == "" && $.trim($("#emailId").val()) == ""){
	$.prompt("To/Cc Email Id must be entered since Query 1 only entered.",{
	    callback:function(){
		$("#emailId").callFocus();
	    }
	});
	return false;
    }else if($.trim($("#emailId").val()) != "" && String($.trim($("#emailId").val())).search(emailPattern) == -1){
	$.prompt("Please enter valid To/Cc Email Id.",{
	    callback:function(){
		$("#emailId").callFocus();
	    }
	});
	return false;
    }else{
	return true;
    }
}

function onValidateSave(result){
    if(result=="valid"){
	$("#action").val("saveOrUpdate");
	$("#reportForm").submit();
    }else{
	$.prompt(result);
    }
}

function saveReport(){
    if(validateReport()){
	if($.trim($("#query2").val()) == ""){
	    $("#query2").removeAttr("name");
	}
	var url = $("#reportForm").attr("action");
	ajaxCall(url,{
	    data: {
		query1:$.trim($("#query1").val()),
		query2:$.trim($("#query2").val()),
		action:"validateQueries"
	    },
	    preloading: true,
	    success: "onValidateSave",
	    async:false
	});
    }
}

function closeQueries(index){
    $("#query-container"+index).html($("#edit-container").html());
    $("#edit-container").center().hide(500);
    $("#edit-container"+index).html("");
    hideAlternateMask();
}

function showQueries(index){
    showAlternateMask();
    $("#edit-container").html($("#query-container"+index).html());
    $("#query-container"+index).html("");
    $("#edit-container").center().show(500);
}

var rowIndex;

function onValidateUpdate(result){
    if(result=="valid"){
	var index = rowIndex;
	$("#add-container").html("");
	$("#reportName"+index).attr("name","report.reportName");
	$("#reportTypeValue"+index).attr("name","report.reportTypeValue");
	$("#scheduleFrequencyValue"+index).attr("name","report.scheduleFrequencyValue");
	$("#scheduleDay1"+index).attr("name","report.scheduleDay1");
	$("#scheduleDay2"+index).attr("name","report.scheduleDay2");
	$("#scheduleTime"+index).attr("name","report.scheduleTime");
	$("#header"+index).attr("name","report.header").val($("#header"+index).is(":checked"));
	$("#enabled"+index).attr("name","report.enabled").val($("#enabled"+index).is(":checked"));
	$("#sender"+index).attr("name","report.sender").val();
	$("#senderName"+index).attr("name","report.senderName").val();
	$("#emailId"+index).attr("name","report.emailId").val();
	$("#emailBody"+index).attr("name","report.emailBody").val();
	$("#createdBy"+index).attr("name","report.createdBy");
	$("#createdDateValue"+index).attr("name","report.createdDateValue");
	$("#query1"+index).attr("name","report.query1");
	if($.trim($("#query2"+index).val()) == ""){
	    $("#query2"+index).val("");
	}
	$("#query2"+index).attr("name","report.query2");
	$("#id"+index).attr("name","report.id");
	$("#action").val("saveOrUpdate");
	$("#reportForm").submit();
    }else{
	$.prompt(result);
    }
}

function updateReport(index){
    $("#scheduleTime"+index).val($("#scheduleHr"+index).val()+":"+$("#scheduleMin"+index).val()+":"+$("#scheduleSec"+index).val())
    if($.trim($("#reportName"+index).val()) == ""){
	$.prompt("Please enter Report Name",{
	    callback:function(){
		$("#reportName"+index).callFocus();
	    }
	});
    }else if($.trim($("#sender"+index).val()) == ""){
	$.prompt("Please enter Report Sender",{
	    callback:function(){
		$("#senderName"+index).callFocus();
	    }
	});
    }else if($.trim($("#emailBody"+index).val()) == ""){
	$.prompt("Please enter Email Body",{
	    callback:function(){
		$("#emailBody"+index).callFocus();
	    }
	});
    }else if($.trim($("#query1"+index).val()) == ""){
	$.prompt("Please enter Query 1",{
	    callback:function(){
		$("#query1"+index).callFocus();
	    }
	});
    }else if($.trim($("#query1"+index).val()).indexOf(" as 'Email'")<0 && $.trim($("#query1"+index).val()).indexOf(" as email")<0){
	$.prompt("Email column must be required in Query 1.",{
	    callback:function(){
		$("#query1"+index).callFocus();
	    }
	});
    }else if($.trim($("#query2"+index).val()) == "" && $.trim($("#emailId"+index).val()) == ""){
	$.prompt("To/Cc Email Id must be entered since Query 1 only entered.",{
	    callback:function(){
		$("#emailId"+index).callFocus();
	    }
	});
    }else if($.trim($("#emailId"+index).val()) != "" && String($.trim($("#emailId"+index).val())).search(emailPattern) == -1){
	$.prompt("Please enter valid To/Cc Email Id.",{
	    callback:function(){
		$("#emailId"+index).callFocus();
	    }
	});
    }else{
	rowIndex = index;
	var url = $("#reportForm").attr("action");
	ajaxCall(url,{
	    data: {
		query1:$.trim($("#query1"+index).val()),
		query2:$.trim($("#query2"+index).val()),
		action:"validateQueries"
	    },
	    preloading: true,
	    success: "onValidateUpdate",
	    async:false
	});
    }
}

function deleteReport(id,report){
    $.prompt("Are you sure want to delete this report - "+report+"?",{
	buttons:{
	    Yes:true,
	    No:false
	},
	callback: function(v,m,f){
	    if(v){
		$("#reportId").val(id).attr("name","report.id");
		$("#add-container").html("");
		$("#action").val("delete");
		$("#reportForm").submit();
	    }
	}
    });
}

function downloadFile(fileName){
    var src = path+"/servlet/FileViewerServlet?fileName="+fileName;
    if($("#iframe").length>0){
	$("#iframe").attr("src",src);
    }else{
	$("<iframe/>", {
	    name	:   "iframe",
	    id	:   "iframe",
	    src	:   src
	}).appendTo("body").hide();
    }
}

function previewFile(fileName){
    var title = fileName.split("/").pop();
    var url = path+"/servlet/FileViewerServlet?fileName="+fileName;
    window.parent.showLightBox(title,url);
}

function showReport(fileName){
    var extension = fileName.split(".").pop();
    if(extension.indexOf("xls")>-1){
	downloadFile(fileName);
    }else{
	previewFile(fileName);
    }
}

function previewReport(id){
    var url = $("#reportForm").attr("action");
    ajaxCall(url,{
	data: {
	    id:id,
	    action:"previewReport"
	},
	preloading: true,
	success: "showReport",
	async:false
    });
}

function showNotes(id){
    var url = path+"/notes.do?";
    url += "moduleId=Report";
    url += "&moduleRefId=" + id;
    window.parent.showLightBox("Notes",url,400,800);
}

function initReport(){
    $("#senderName").initautocomplete({
	url:path+"/autocompleter/action/getAutocompleterResults.jsp?query=USER&template=user&fieldIndex=1,3&",
	width: "400px",
	otherFields:"sender",
	resultsClass:"ac_results z-index",
	resultPosition:"absolute",
	scroll:true,
	scrollHeight:250,
	focusField:"emailId"
    });
    $(".display-table>tbody>tr").each(function(index){
	var rowIndex = index+1;
	$("#senderName"+rowIndex).initautocomplete({
	    url:path+"/autocompleter/action/getAutocompleterResults.jsp?query=USER&template=user&fieldIndex=1,3&",
	    width: "400px",
	    otherFields:"sender"+rowIndex,
	    resultsClass:"ac_results z-index",
	    resultPosition:"absolute",
	    scroll:true,
	    scrollHeight:250,
	    focusField:"emailId"+rowIndex
	});
    })
}

function setResultHeight(){
    if($(".result-container").length>0){
	var windowHeight = window.parent.getFrameHeight();
	$(".result-container").height(windowHeight-$(".table-banner").height()-100);
	$("body").css("overflow","hidden");
    }
}

$(document).ready(function(){
    $("#reportForm").submit(function(){
	showPreloading();
    });
    $("[title != '']").not("link").tooltip();
    initReport();
    $(window).resize(function(){
	window.parent.changeHeight();
	if($(".result-container").length>0){
	    setResultHeight();
	}
    });
});