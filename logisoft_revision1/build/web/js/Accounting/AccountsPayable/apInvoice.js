/* 
 * Document   : apInvoice
 * Created on : Jun 20, 2012, 01:34:00 PM
 * Author     : Lakshmi Naryanan
 */

function focusNextInput(){
    $("body").focusNextInput();
}

function initVendor(){
    if($("#vendorName").length>0 && !$("#vendorName").hasClass("readonly")){
	$("#vendorName").initautocomplete({
	    url:path+"/autocompleter/action/getAutocompleterResults.jsp?query=VENDOR&template=vendor&fieldIndex=1,2,10,11,12&",
	    width: "480px",
	    otherFields:"vendorNumber^creditDesc^creditTerm^creditId",
	    resultsClass:"ac_results z-index",
	    resultPosition:"fixed",
	    scroll:true,
	    scrollHeight:300,
	    focusField: $.trim($("#recurring").val())==="true" ? "for":"invoiceNumber"
	});
    }
}

function initInvoice(){
    $("#forComments").keydown(function(event){
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if(!event.shiftKey && keycode === 9){
	    $("#line-item-tbody tr").not("#defaultLineItem").eq(0).find(".glAccount").callFocus();
	    event.preventDefault();
	}
    });
    
    $("#invoiceDateCalendar").insertFromCalendar({
	inputField  : "invoiceDate",
	format	    : "%m/%d/%Y"
    });
}

function findDuplicatesSuccess(status){
    if(status === "Available"){
	$("#invoiceNumber").val($.trim($("#invoiceNumber").val()));
	$("#invoiceDate").callFocus();
    }else if(status === "Posted to AP"){
	$.prompt("Invoice is posted to AP already",{
	    callback: function(){
		$("#invoiceNumber").val("").callFocus();
	    }
	});
    }else if(status === "Posted to Negative AP"){
	$.prompt("Invoice is posted to Negative AP already",{
	    callback: function(){
		$("#invoiceNumber").val("").callFocus();
	    }
	});
    }else if(status === "In Progress" || status === "Dispute" || status === "Reject"){
	$.prompt("Invoice is already in "+status,{
	    callback: function(){
		$("#invoiceNumber").val("").callFocus();
	    }
	});
    }else{
	$.prompt(status,{
	    buttons:{
		Yes:true,
		No:false
	    },
	    callback: function(v,m,f){
		if(v){
		    $("#invoiceDate").callFocus();
		}else{
		    $("#invoiceNumber").val("").callFocus();
		}
	    }
	});
    }
}

function findDuplicatesError(){
    $("#invoiceNumber").val("").callFocus();
}

function findDuplicates(){
    if($.trim($("#invoiceNumber").val()) !== ""){
	if($.trim($("#vendorNumber").val()) === ""){
	    $.prompt("Please enter Vendor",{
		callback: function(){
		    $("#vendorName").callFocus();
		}
	    });
	}else{
	    var url = path+"/apInvoice.do";
	    ajaxCall(url,{
		data: {
		    action: "findDuplicates",
		    vendorNumber: $.trim($("#vendorNumber").val()),
		    invoiceNumber: $.trim($("#invoiceNumber").val())
		},
		preloading: true,
		success: "findDuplicatesSuccess",
		error: "findDuplicatesError"
	    });
	}
    }
}

var onDueDateSuccess = function(result){
    $("#dueDate").val(result);
    $("#forComments").callFocus();
};

var onDueDateError = function(){
    $("#invoiceDate").val("").callFocus();
    $("#dueDate").val("");
};

function calculateDueDate(){
    var url = $("#apInvoiceForm").attr("action");
    var params = "action=calculateDueDate&invoiceDate="+$("#invoiceDate").val()+"&creditTerm="+$("#creditTerm").val();
    ajaxCall(url,{
	data: params,
	preloading: false,
	success: "onDueDateSuccess",
	error: "onDueDateError"
    });
}

function validateDate(ele){
    if($.trim($(ele).val())!==""){
	if(!isDate(ele)){
	    $.prompt("Please enter date in mm/dd/yyyy format",{
		callback: function(){
		    $(ele).val("").callFocus();
		    $("#dueDate").val("");
		},
		loaded: function(){
		    $("#jqi_state0_buttonOk").callFocus();
		}
	    });
	}else{
	    if(Date.parse($(ele).val()) > Date.parse(Date())){
		$.prompt("Invoice date should not be greater than today's date",{
		    callback: function(){
			$(ele).val("").callFocus();
			$("#dueDate").val("");
		    },
		    loaded: function(){
			$("#jqi_state0_buttonOk").callFocus();
		    }
		});
	    }else{
		calculateDueDate();
	    }
	}
    }else{
	$("#dueDate").val("");
    }
}

function onchangeAmount(ele){
    if(isNaN($(ele).val())){
	$.prompt("Please enter valid amount");
	$(ele).val("").callFocus();
    }else{
	$(ele).val(Number($(ele).val()).toFixed(2));
    }
}

function saveInvoice(){
    if($.trim($("#vendorNumber").val())===""){
	$.prompt("Please enter vendor",{
	    callback: function(){
		$("#vendorName").callFocus();
	    }
	});
    }else if($.trim($("#recurring").val())==="false" && $.trim($("#invoiceNumber").val())===""){
	$.prompt("Please enter invoice number",{
	    callback: function(){
		$("#invoiceNumber").callFocus();
	    }
	});
    }else if($.trim($("#recurring").val())==="false" && $.trim($("#invoiceDate").val())===""){
	$.prompt("Please enter invoice date",{
	    callback: function(){
		$("#invoiceDate").callFocus();
	    }
	});
    }else {
	$("#action").val("save");
	$("#apInvoiceForm").submit();
    }
}

function addLineItem(){
    if($.trim($("#vendorNumber").val())===""){
	$.prompt("Please enter vendor",{
	    callback: function(){
		$("#vendorName").callFocus();
	    }
	});
    }else if($.trim($("#recurring").val())==="false" && $.trim($("#invoiceNumber").val())===""){
	$.prompt("Please enter invoice number",{
	    callback: function(){
		$("#invoiceNumber").callFocus();
	    }
	});
    }else if($.trim($("#recurring").val())==="false" && $.trim($("#invoiceDate").val())===""){
	$.prompt("Please enter invoice date",{
	    callback: function(){
		$("#invoiceDate").callFocus();
	    }
	});
    }else if($.trim($("#glAccount").val())===""){
	$.prompt("Please enter gl account",{
	    callback: function(){
		$("#glAccount").callFocus();
	    }
	});
    }else if($.trim($("#amount").val())===""){
	$.prompt("Please enter amount",{
	    callback: function(){
		$("#amount").callFocus();
	    }
	});
    }else {
	$("#action").val("addLineItem");
	$("#apInvoiceForm").submit();
    }
}

function initLineItem(){
    $("#glAccount").initautocomplete({
	url:path+"/autocompleter/action/getAutocompleterResults.jsp?query=GL_ACCOUNT&template=glAccount&fieldIndex=1&",
	width: "350px",
	resultsClass:"ac_results z-index",
	resultPosition:"fixed",
	scroll:true,
	scrollHeight:300,
	focusField:"#amount"
    });
    
    $("#amount").keydown(function(event){
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if (!event.shiftKey && (keycode === 9 || keycode === 13)) {
	    event.stopPropagation();
	    event.preventDefault();
	    addLineItem();
	}else{
	    allowOnlyNumbers(event,this);
	}
    });
    $("#amount").keyup(function(event){
	validateAmount(event,this);
    });
    $("#amount").change(function(){
	onchangeAmount(this);
    });
}

function removeLineItem(lineItemId){
    $.prompt("Do you want to remove this line item?",{
	buttons:{
	    Yes:true,
	    No:false
	},
	callback: function(v,m,f){
	    if(v){
		$("#lineItemId").val(lineItemId);
		$("#action").val("removeLineItem");
		$("#apInvoiceForm").submit();
	    }
	}
    });
}

function deleteInvoice(id){
    $.prompt("Do you want to delete this invoice?",{
	buttons:{
	    Yes:true,
	    No:false
	},
	callback: function(v,m,f){
	    if(v){
		$("#id").val(id);
		$("#action").val("delete");
		$("#apInvoiceForm").submit();
	    }
	}
    });
}

function initSearch(){
    initVendor();
    if($("#recurring").val() === "true"){
	$("#invoiceAmount").keydown(function(event){
	    allowOnlyNumbers(event,this);
	});
	$("#invoiceAmount").keyup(function(event){
	    validateAmount(event,this);
	});
    }
    var windowHeight = $(window.top).height();
    $(".search-results").height(windowHeight-$(".search-filters").height()-$(".page-banner").height()-100);
    $("#vendorName").callFocus();
}

function search(){
    $("#action").val("search");
    $("#apInvoiceForm").submit();
}

function gotoPage(pageNo){
    $("#selectedPage").val(pageNo);
    search();
}

function gotoSelectedPage(){
    $("#selectedPage").val($("#selectedPageNo").val());
    search();
}

function doSort(sortBy,orderBy){
    $("#sortBy").val(sortBy);
    $("#orderBy").val(orderBy);
    search();
}

function gotoSearch(recurring){
    $("#recurring").val(recurring);
    $("#action").val("gotoSearch");
    $("#apInvoiceForm").submit();
}

function editInvoice(id){
    $("#id").val(id);
    $("#action").val("apInvoice");
    $("#apInvoiceForm").submit();
}

function addInvoice(){
    $("#action").val("apInvoice");
    $("#apInvoiceForm").submit();
}

function post(){
    if($.trim($("#vendorNumber").val())===""){
	$.prompt("Please enter vendor",{
	    callback: function(){
		$("#vendorName").callFocus();
	    }
	});
    }else if($.trim($("#invoiceNumber").val())===""){
	$.prompt("Please enter invoice number",{
	    callback: function(){
		$("#invoiceNumber").callFocus();
	    }
	});
    }else if($.trim($("#invoiceDate").val())===""){
	$.prompt("Please enter invoice date",{
	    callback: function(){
		$("#invoiceDate").callFocus();
	    }
	});
    }else if($("#line-item-tbody tr").length<=0){
	$.prompt("Please add some line items",{
	    callback: function(){
		$("#glAccount").callFocus();
	    }
	});
    }else{
	$.prompt("Do you want to post this invoice to AP?",{
	    buttons:{
		Yes:true,
		No:false
	    },
	    callback: function(v,m,f){
		if(v){
		    $("#action").val("post");
		    $("#apInvoiceForm").submit();
		}
	    }
	});
    }
}

function upload(){
    if($.trim($("#vendorNumber").val())===""){
	$.prompt("Please enter vendor",{
	    callback: function(){
		$("#vendorName").callFocus();
	    }
	});
    }else if($.trim($("#invoiceNumber").val())===""){
	$.prompt("Please enter invoice number",{
	    callback: function(){
		$("#invoiceNumber").callFocus();
	    }
	});
    }else{
	var documentId = $.trim($("#vendorNumber").val())+"-"+$.trim($("#invoiceNumber").val());
	var title = "Upload Invoice Image";
	var url =  path+"/scan.do?screenName=INVOICE&documentId="+encodeURIComponent(documentId);
	window.parent.showLightBox(title,url,400,800);
    }
}

function notes(){
    if($.trim($("#vendorNumber").val())===""){
	$.prompt("Please enter vendor",{
	    callback: function(){
		$("#vendorName").callFocus();
	    }
	});
    }else if($.trim($("#invoiceNumber").val())===""){
	$.prompt("Please enter invoice number",{
	    callback: function(){
		$("#invoiceNumber").callFocus();
	    }
	});
    }else{
	var moduleRefId = $.trim($("#vendorNumber").val())+"-"+$.trim($("#invoiceNumber").val());
	var title = "Notes";
	var url =  path+"/notes.do?moduleId=AP_INVOICE&moduleRefId="+encodeURIComponent(moduleRefId);
	window.parent.showLightBox(title,url,400,800);
    }
}

function setResultHeight(){
    var windowHeight = window.parent.getFrameHeight();
    $(".search-results").height(windowHeight-$(".search-filters").height()-$(".table-banner").height()-150);
    $("body").css("overflow","hidden");
}

$(document).ready(function(){
    closePreloading();
    $("#apInvoiceForm").submit(function(){
	showPreloading();
    });
    $("[title != '']").not("link").tooltip();
    focusNextInput();
    if($.trim($("#action").val()) === "gotoSearch" || $.trim($("#action").val()) === "search"){
	initSearch();
    }else{
	if($.trim($("#vendorNumber").val()) === ""){
	    initVendor();
	}
	initInvoice();
	initLineItem();
	if($.trim($("#vendorNumber").val()) !== "" && $.trim($("#invoiceNumber").val()) !== "" && $.trim($("#invoiceDate").val()) !== ""){
	    $("#glAccount").callFocus();
	}else if($.trim($("#vendorNumber").val()) !== "" && $.trim($("#invoiceNumber").val()) !== ""){
	    $("#invoiceDate").callFocus();
	}else if($.trim($("#vendorNumber").val()) !== ""){
	    $("#invoiceNumber").callFocus();
	}else{
	    $("#vendorName").callFocus();
	}
    }
    if($(".search-results").length>0){
	setResultHeight();
	$(window).resize(function(){
	    window.parent.changeHeight();
	    setResultHeight();
	});
    }
});