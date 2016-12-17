/* 
 * Document   : apInquiry
 * Created on : Sep 28, 2012, 06:28:00 PM
 * Author     : Lakshmi Naryanan
 */

function searchAll(){
    if($.trim($("#fromAmount").val()) == "" && $.trim($("#toAmount").val()) == ""
	&& ($.trim($("#searchBy").val()) == "invoice_amount" || $.trim($("#searchBy").val()) == "check_amount")){
	$("#searchBy").val("");
	$("#fromAmount").val("");
	$("#toAmount").val("");
    }else if($.trim($("#searchBy").val()) != "" && $.trim($("#searchBy").val()) != "invoice_amount" 
	&& $.trim($("#searchBy").val()) != "check_amount" && $.trim($("#searchValue").val()) == ""){
	$("#searchBy").val("");
	$("#searchValue").val("")
    }
    if($.trim($("#showInvoices").val()) == "" && $.trim($("#showAccruals").val()) == ""){
	$("#showInvoices").val("Open");
    }
    $("#selectedPage").val("1");
    $("#sortBy").val("invoice_date");
    $("#orderBy").val("desc");
    $("#action").val("search");
    $("#apInquiryForm").submit();
}

function clearAll(){
    $("#action").val("clearAll");
    $("#apInquiryForm").submit();
}

function initVendor(){
    $("#vendorName").initautocomplete({
	url:path+"/autocompleter/action/getAutocompleterResults.jsp?query=VENDOR&template=vendor&fieldIndex=1,2&",
	width: "480px",
	otherFields:"vendorNumber",
	resultsClass:"ac_results z-index",
	resultPosition:"absolute",
	scroll:true,
	scrollHeight:300,
	callback:"searchAll()"
    });
}

function toggle(ele){
    $("."+ele).slideToggle(function(){
	if(ele == "filter-container"){
	    if($("#toggled").val() == "true"){
		$("#toggled").val("false");
	    }else{
		$("#toggled").val("true");
	    }
	}
	setResultHeight();
    });
}

function onchangeSearchBy(ele){
    if($.trim($(ele).val()) == "invoice_amount" || $.trim($(ele).val()) == "check_amount"){
	$("#searchByAmount").show();
	$("#fromAmount").removeAttr("readonly").removeClass("readonly").val("0.00").callFocus();
	$("#toAmount").removeAttr("readonly").removeClass("readonly").val("0.00");
	$("#searchByValue").hide();
	$("#searchValue").attr("readonly",true).addClass("readonly").val("");
    }else if($.trim($(ele).val())!=""){
	$("#searchByValue").show();
	$("#searchValue").removeAttr("readonly").removeClass("readonly").val("").callFocus();
	$("#searchByAmount").hide();
	$("#fromAmount").attr("readonly",true).addClass("readonly").val("0.00");
	$("#toAmount").attr("readonly",true).addClass("readonly").val("0.00");
    }else{
	$("#searchByAmount").hide();
	$("#fromAmount").attr("readonly",true).addClass("readonly").val("0.00");
	$("#toAmount").attr("readonly",true).addClass("readonly").val("0.00");
	$("#searchValue").attr("readonly",true).addClass("readonly").val("");
    }
}

function onchangeAmount(ele){
    if($.trim($(ele).val()) != ""){
	if(isNaN($(ele).val())){
	    $.prompt("Please enter valid amount",{
		callback: function(){
		    $(ele).val("").callFocus();
		}
	    });
	}else{
	    $(ele).val(Number($(ele).val()).toFixed(2));
	}
    }
}

function initAmounts(){
    $("#fromAmount,#toAmount").numeric();
}

function initDates(){
    $("#fromDateCalendar").insertFromCalendar({
	format : "%m/%d/%Y",
	inputField : "fromDate"
    });
    $("#toDateCalendar").insertFromCalendar({
	format : "%m/%d/%Y",
	inputField : "toDate"
    });
}

function onchangeDate(ele){
    if($.trim($(ele).val()) != ""){
	if(!isDate(ele)){
	    $.prompt("Please enter date in mm/dd/yyyy format",{
		callback: function(){
		    $(ele).val("").callFocus();
		}
	    });
	}else{
	    var date1 = "#"+$(ele).attr("id");
	    var date2 = (date1 == "#fromDate" ? "#toDate" : "#fromDate");
	    if($.trim($(date2).val()) != ""){
		if(date1 == "#fromDate" && Date.parse($(date1).val()) > Date.parse($(date2).val())){
		    $.prompt("From Date should not be greater than To Date",{
			callback: function(){
			    $(date1).val("").callFocus();
			}
		    });
		}else if(date1 == "#toDate" && Date.parse($(date1).val()) < Date.parse($(date2).val())){
		    $.prompt("To Date should not be lesser than From Date",{
			callback: function(){
			    $(date1).val("").callFocus();
			}
		    });
		}
	    }
	}
    }
}

function searchByFilters(){
    if($.trim($("#fromAmount").val()) == "" && $.trim($("#toAmount").val()) == ""
	&& ($.trim($("#searchBy").val()) == "invoice_amount" || $.trim($("#searchBy").val()) == "check_amount")){
	$.prompt("Please enter From or To Amount",{
	    callbak:function(){
		$("#fromAmount").val("").callFocus();
	    }
	});
    }else if($.trim($("#searchBy").val()) != "" && $.trim($("#searchBy").val()) != "invoice_amount" 
	&& $.trim($("#searchBy").val()) != "check_amount" && $.trim($("#searchValue").val()) == ""){
	$.prompt("Please enter search value",{
	    callbak:function(){
		$("#searchValue").val("").callFocus();
	    }
	});
    }else{
	if($.trim($("#showInvoices").val()) == "" && $.trim($("#showAccruals").val()) == ""){
	    $("#showInvoices").val("Open");
	}
	$("#action").val("search");
	$("#apInquiryForm").submit();
    }
}

function gotoPage(pageNo){
    $("#selectedPage").val(pageNo);
    searchByFilters();
}

function gotoSelectedPage(){
    $("#selectedPage").val($("#selectedPageNo").val());
    searchByFilters();
}

function doSort(sortBy){
    $("#sortBy").val(sortBy);
    if($("#orderBy").val() == "desc"){
	$("#orderBy").val("asc");
    }else{
	$("#orderBy").val("desc");
    }
    searchByFilters();
}

function clearFilters(){
    $("#action").val("clearFilters");
    $("#apInquiryForm").submit();
}

function setResultHeight(){
    if($(".result-container").length>0){
	var windowHeight = window.parent.getFrameHeight();
	var height = windowHeight;
	height -= $(".vendor-container").length>0 && $(".vendor-container").css("display") == "block" ? $(".vendor-container").height() : 0;
	height -= $(".filter-container").length>0 && $(".filter-container").css("display") == "block" ? $(".filter-container").height() : 0;
	height -= $(".table-banner").height();
	height -= 150;
	$(".result-container").height(height);
	$("body").css("overflow","hidden");
    }
}

function showInvoiceDetails(vendorNumber,invoiceNumber,blNumber,transactionType){
    var url = path+"/apInquiry.do";
    url += "?action=showInvoiceDetails";
    url += "&vendorNumber="+encodeURIComponent(vendorNumber);
    url += "&invoiceNumber="+encodeURIComponent(invoiceNumber);
    url += "&blNumber="+encodeURIComponent(blNumber);
    url += "&transactionType="+transactionType;
    window.parent.showLightBox("Invoice Details - Vendor : "+vendorNumber+" Invoice : "+invoiceNumber,url,400,900);
}

function showNotes(noteModuleId,noteRefId,costId,invoiceRefId){
    var url = path+"/notes.do?";
    url += "moduleId=" + encodeURIComponent(noteModuleId);
    url += "&moduleRefId=" + encodeURIComponent(noteRefId);
    if(noteModuleId === "ACCRUALS"){
	url += "&actions=showAccrualsNotes";
	url += "&accrualsRefId=" + encodeURIComponent(noteRefId);
	url += "&invoiceRefId=" + encodeURIComponent(invoiceRefId);
	url += "&costRefId=" + encodeURIComponent(costId);
    }
    window.parent.showLightBox("Notes",url,400,800);
}

function uploadInvoice(documentId){
    var title = "Upload Invoice";
    var url =  path+"/scan.do?screenName=INVOICE&documentId="+encodeURIComponent(documentId);
    window.parent.showLightBox(title,url,400,800);
}

function callServer(data,preloading,success){
    var url = $("#apInquiryForm").attr("action");
    ajaxCall(url,{
	data: data,
	preloading: preloading,
	success: success,
	async:false
    });
}

function printNSInvoice(fileName){
    var title = "NS Invoice";
    var url = path+"/servlet/FileViewerServlet?fileName="+fileName;
    window.parent.showLightBox(title,url);
}

function createNSInvoicePdf(batchId){
    var params = "action=createNSInvoicePdf";
    params += "&batchId=" + batchId;
    callServer(params,true,"printNSInvoice");
}

function exportNSInvoice(fileName){
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

function createNSInvoiceExcel(batchId){
    var params = "action=createNSInvoiceExcel";
    params += "&batchId=" + batchId;
    callServer(params,true,"exportNSInvoice");
}

function printPdf(fileName){
    var title = "Ap Inquiry";
    var url = path+"/servlet/FileViewerServlet?fileName="+fileName;
    window.parent.showLightBox(title,url);
}

function createPdf(){
    if($.trim($("#vendorNumber").val()) != "" || $.trim($("#searchBy").val())!=""){
	var params = "action=createPdf";
	params += "&" + $("#apInquiryForm").serialize();
	callServer(params,true,"printPdf");
    }else{
	$.prompt("Please enter Vendor or select Search By filter",{
	    callback:function(){
		$("#vendorName").val("").callFocus();
	    }
	});
    }
}

function exportExcel(fileName){
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

function createExcel(){
    if($.trim($("#vendorNumber").val()) != "" || $.trim($("#searchBy").val())!=""){
	var params = "action=createExcel";
	params += "&" + $("#apInquiryForm").serialize();
	callServer(params,true,"exportExcel");
    }else{
	$.prompt("Please enter Vendor or select Search By filter",{
	    callback:function(){
		$("#vendorName").val("").callFocus();
	    }
	});
    }
}

$(document).ready(function(){
    $("#apInquiryForm").submit(function(){
	showPreloading();
    });
    $("[title != '']").not("link").tooltip();
    if($.trim($("#vendorNumber").val()) == ""){
	$("#vendorName").callFocus();
    }
    initVendor();
    initAmounts();
    initDates();
    setResultHeight();
     $(window).resize(function() {
	window.parent.changeHeight();
	setResultHeight();
    });
    $(document).keypress(function(event){
	if($("#"+event.target.id).length<=0 
	    || (event.target.id!="jqi_state0_buttonOk" && event.target.id!="jqi_state0_buttonYes" && event.target.id!="jqi_state0_buttonNo")){
	    var keycode = (event.keyCode ? event.keyCode : event.which);
	    if(keycode == 13) {
		event.preventDefault();
		searchByFilters();
	    }
	}
    });
});
