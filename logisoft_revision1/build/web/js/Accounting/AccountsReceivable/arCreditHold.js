var removeFromHoldIds = new Array();
var putOnHoldIds = new Array();

jQuery(document).ready(function(){
    jQuery(document).keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == 13) {
            search();
        }
    });
});
function search(){
    jQuery("#pageNo").val("1");
    jQuery("#sortBy").val("c.invoice_date");
    jQuery("#orderBy").val("asc");
    jQuery("#action").val("search")
    jQuery("#results").html("");
    jQuery("#arCreditHoldForm").submit();
}

function gotoPage(pageNo){
    jQuery("#pageNo").val(pageNo);
    jQuery("#results").html("");
    jQuery("#action").val("search");
    jQuery("#arCreditHoldForm").submit();
}
function gotoSelectedPage(){
    jQuery("#pageNo").val(jQuery("#selectedPageNo").val());
    jQuery("#results").html("");
    jQuery("#action").val("search");
    jQuery("#arCreditHoldForm").submit();
}
function doSort(sortBy,orderBy){
    jQuery("#sortBy").val(sortBy);
    jQuery("#orderBy").val(orderBy);
    jQuery("#results").html("");
    jQuery("#action").val("search");
    jQuery("#arCreditHoldForm").submit();
}

function save(){
    if(removeFromHoldIds.length==0 && putOnHoldIds.length==0){
        jQuery.prompt("Please select atleast one invoice to release from hold or put hold");
    }else{
        jQuery("#removeFromHoldIds").val(removeFromHoldIds);
        jQuery("#putOnHoldIds").val(putOnHoldIds);
        jQuery("#pageNo").val("1");
        jQuery("#sortBy").val("c.invoice_date");
        jQuery("#orderBy").val("asc");
        jQuery("#action").val("save")
        jQuery("#arCreditHoldForm").submit();
    }
}
function refresh(){
    jQuery("#results").html("");
    jQuery("#action").val("refresh")
    jQuery("#arCreditHoldForm").submit();
}

function showTransactionHistory(transactionId){
    jQuery.ajaxx({
        data: {
            className: "com.logiware.dwr.ArDwr",
            methodName: "showArTransactionHistory",
            param1: transactionId,
            request: "true",
            forward: "/jsps/AccountsRecievable/arTransactionHistory.jsp"
        },
        preloading: true,
        success: function (data) {
            if (data) {
                showAlternateMask();
                jQuery("<div style='width:700px;height:300px'></div>").html(data).addClass("popup").appendTo("body").center();
            }
        }
    });
}

function gotoTradingPartner(){
    var custNo = jQuery.trim(jQuery("#customerNumber").val());
    if(custNo==""){
        jQuery.prompt("Please select the customer");
    }else{
        window.parent.gotoTradingPartner(custNo,"AR Config")
    }
}

function showArInquiry(param) {
    var caption = "AR Inquiry";
    var path = rootPath+"/arInquiry.do?action=search&"+param;
    var height = jQuery(document).height()-50;
    var width = jQuery(document).width()-100;
    var version = jQuery.browser.version;
    if(jQuery.browser.msie && parseFloat(version)>=9.0){
        window.open(path,"mywindow","menubar=1,resizable=1,width="+width+",height="+height);
    }else{
        GB_show(caption,path,height,width);
    }
}

function getFreightInvoice(transactionId){
    var billOfLadding = jQuery.trim(jQuery("#billOfLadding"+transactionId).val());
    var noticeNumber = jQuery.trim(jQuery("#correctionNotice"+transactionId).val());
    var manifestFlag = jQuery.trim(jQuery("#manifestFlag"+transactionId).val());
    var cutomerNumber = jQuery.trim(jQuery("#customerNumber"+transactionId).val());
    var url = "";
    if(null != manifestFlag && manifestFlag =="R"){
        url = rootPath+"/printConfig.do?screenName=BL&arInvoice="+billOfLadding;
    }else if(billOfLadding.indexOf("04-")>-1){
        var fileNo=billOfLadding.substring(billOfLadding.indexOf("04-")+3);
        url = rootPath+"/printConfig.do?screenName=BL&fileNo="+fileNo+"&noticeNumber="+noticeNumber;
        url+="&cutomerNumber="+cutomerNumber+"&blId="+billOfLadding;
    }
    GB_show("Print/Fax/Email",url,400,800);
}


function onclickRemoveFromHold(obj,transactionId){
    if(jQuery(obj).is(":checked")){
        removeFromHoldIds.push(transactionId);
        index = jQuery.inArray(transactionId, putOnHoldIds);
        if(index>-1){
            putOnHoldIds.splice(index, 1);
        }
        var row = jQuery(obj).parent().parent();
        row.find(".putOnHoldIds").attr("checked",false);
    }else{
        index = jQuery.inArray(transactionId, removeFromHoldIds);
        if(index>-1){
            removeFromHoldIds.splice(index, 1);
        }
    }
}

function onclickPutOnHold(obj,transactionId){
    if(jQuery(obj).is(":checked")){
        putOnHoldIds.push(transactionId);
        index = jQuery.inArray(transactionId, removeFromHoldIds);
        if(index>-1){
            removeFromHoldIds.splice(index, 1);
        }
        var row = jQuery(obj).parent().parent();
        row.find(".removeFromHoldIds").attr("checked",false);
    }else{
        index = jQuery.inArray(transactionId, putOnHoldIds);
        if(index>-1){
            putOnHoldIds.splice(index, 1);
        }
    }
}

function showScanOrAttach(transactionId){
    var billOfLadding = jQuery.trim(jQuery("#billOfLadding"+transactionId).val());
    var cutomerNumber = jQuery.trim(jQuery("#customerNumber"+transactionId).val());
    var documentId = cutomerNumber+"-"+billOfLadding;
    GB_show("Scan/Attach", rootPath+"/scan.do?screenName=INVOICE&documentId="+documentId,375,700);
}

function showInvoiceNotes(transactionId){
    var billOfLadding = jQuery.trim(jQuery("#billOfLadding"+transactionId).val());
    var cutomerNumber = jQuery.trim(jQuery("#customerNumber"+transactionId).val());
    var moduleId = jQuery.trim(jQuery("#notesConstantArInvoice").val());
    var moduleRefId = cutomerNumber+"-"+billOfLadding;
    GB_show("Notes", rootPath+"/notes.do?moduleId="+moduleId+"&moduleRefId="+moduleRefId,375,700);
}

var customerUrl = rootPath+"/servlet/AutoCompleterServlet?action=Vendor&textFieldId=customerName";
AjaxAutocompleter("customerName","customerNameChoices","customerNumber","customerNameCheck",customerUrl,"","");

var billingTerminalUrl = rootPath+"/servlet/AutoCompleterServlet?action=BillingTerminal&textFieldId=billingTerminal";
AjaxAutocompleter("billingTerminal","billingTerminalChoices","","billingTerminalCheck",billingTerminalUrl,"","");

var destinationUrl = rootPath+"/servlet/AutoCompleterServlet?action=Destination&textFieldId=destination";
AjaxAutocompleter("destination","destinationChoices","","destinationCheck",destinationUrl,"","");

var collectorUrl = rootPath+"/servlet/AutoCompleterServlet?action=Collector&textFieldId=collectorName";
AjaxAutocompleter("collectorName","collectorNameChoices","","collectorNameCheck",collectorUrl,"","");

