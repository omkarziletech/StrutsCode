jQuery(document).ready(function(){
    jQuery(document).keypress(function(event) {
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if(keycode == 13) {
	    search();
	}
    });
    jQuery("#search").click(function(){
	search();
    });

    jQuery("#clear").click(function(){
	jQuery("#action").val("clear");
	jQuery("#ssMastersApprovedForm").submit();
    });
    changeLable(document.getElementById("moduleName"));
});
function search(){
    var module = jQuery("#moduleName").val();
    jQuery("#sortBy").val("")
    if(module === 'LCLE'){
        jQuery("#action").val("searchLCLSSMastersApproved");
        jQuery("#ssMastersApprovedForm").submit();
    }else if(module === 'ALL'){
        jQuery("#action").val("searchALLSSMastersApproved");
        jQuery("#ssMastersApprovedForm").submit();
    }else{
        jQuery("#action").val("searchSSMastersApproved");
        jQuery("#ssMastersApprovedForm").submit();
    }
}
function doSort(sortBy,orderBy){
    jQuery("#sortBy").val(sortBy);
    jQuery("#orderBy").val(orderBy);
    search();
}
function gotoPage(pageNo){
    jQuery("#pageNo").val(pageNo);
    search();
}

function gotoSelectedPage(){
    jQuery("#pageNo").val(jQuery("#selectedPageNo").val());
    search();
}
function convertToAp(fileNo,from){
    showPreloading();
    var url = rootPath+"/accruals.do?action=fromSsMasters&fileNo="+fileNo+"&from="+from;
    url += "&fromParams=";
    url += escape("&moduleName=")+jQuery("#moduleName").val();
    url += escape("&fileNo=")+jQuery("#fileNo").val();
    url += escape("&sslineName=")+jQuery("#sslineName").val();
    url += escape("&sslineNo=")+jQuery("#sslineNo").val();
    url += escape("&bookingNo=")+jQuery("#bookingNo").val();
    url += escape("&masterNo=")+jQuery("#masterNo").val();
    url += escape("&containerNo=")+jQuery("#containerNo").val();
    url += escape("&prepaidOrCollect=")+jQuery("#prepaidOrCollect").val();
    url += escape("&sortBy=")+jQuery("#sortBy").val();
    url += escape("&orderBy=")+jQuery("#orderBy").val();
    url += escape("&pageNo=")+jQuery("#pageNo").val();
    window.location = url;
}
function convertLclSsMasterToAp(headerId,bookingNo,ssLineNo,fileNo,newInvoiceDate,from,ssId,ssMasterBl){
    showPreloading();
    var url = rootPath+"/accruals.do?action=fromLclSsMasters&unitId="+headerId+"&newDockReceipt="+bookingNo+"&newVendorNumber="+ssLineNo+"&fileNo="+fileNo+"&newInvoiceDate="+newInvoiceDate+"&from="+from+"&ssMasterId="+ssId+"&ssMasterBl="+ssMasterBl;
    url += "&fromParams=";
    url += escape("&moduleName=")+jQuery("#moduleName").val();
    url += escape("&fileNo=")+jQuery("#fileNo").val();
    url += escape("&sslineName=")+jQuery("#sslineName").val();
    url += escape("&sslineNo=")+jQuery("#sslineNo").val();
    url += escape("&bookingNo=")+jQuery("#bookingNo").val();
    url += escape("&masterNo=")+jQuery("#masterNo").val();
    url += escape("&containerNo=")+jQuery("#containerNo").val();
    url += escape("&prepaidOrCollect=")+jQuery("#prepaidOrCollect").val();
    url += escape("&sortBy=")+jQuery("#sortBy").val();
    url += escape("&orderBy=")+jQuery("#orderBy").val();
    url += escape("&pageNo=")+jQuery("#pageNo").val();
    window.location = url;
}
function showNotes(moduleId,selectedFileNo){
    window.parent.GB_showCenter("Notes", rootPath+"/notes.do?moduleId="+moduleId+"&moduleRefId="+selectedFileNo, 350, 700);
}

function removeFile(selectedFileNo,obj){
    jQuery.prompt("Do you want to remove this master file - "+selectedFileNo+"?",{
	buttons:{
	    Yes:true,
	    No:false
	},
	callback: function(v,m,f){
	    if(v){
		showPreloading();
		jQuery.post(jQuery("#ssMastersApprovedForm").attr("action"), {
		    selectedFileNo: selectedFileNo, 
		    action: "remove"
		}, function(data) {
		    closePreloading();
		    if(data=="success"){
			jQuery(obj).parent().parent().remove();
			jQuery.prompt("The master file is removed");
		    }else{
			jQuery.prompt("Oops.. Something  went wrong");
		    }
		});
	    }
	}
    });
}
function removeLclSsFile(headerId,bookingNo,obj){
    jQuery.prompt("Do you want to remove this master file - "+bookingNo+"?",{
	buttons:{
	    Yes:true,
	    No:false
	},
	callback: function(v,m,f){
	    if(v){
		showPreloading();
		jQuery.post(jQuery("#ssMastersApprovedForm").attr("action"), {
		    masterNo: headerId, 
		    selectedFileNo: bookingNo, 
		    action: "removeLclSsMasterApproved"
		}, function(data) {
		    closePreloading();
		    if(data=="success"){
			jQuery(obj).parent().parent().remove();
			jQuery.prompt("The master file is removed");
		    }else{
			jQuery.prompt("Oops.. Something  went wrong");
		    }
		});
	    }
	}
    });
}
function changeLable(obj){
    if(obj.value === 'LCLE'){
        jQuery("#fileLable").text("Voyage No");
    }else if(obj.value === 'ALL'){
        jQuery("#fileLable").text("File/Voyage #");
    }else{
        jQuery("#fileLable").text("File No");
    }
}
function openHeaderNotes(path, headerId, voyageNo) {
    window.parent.GB_showCenter("Notes", path + "/lclSSHeaderRemarks.do?methodName=displayNotes&headerId=" + headerId + "&actions=show All&voyageNumber=" + voyageNo, 350, 700);
}
var autocompleterUrl = rootPath+"/servlet/AutoCompleterServlet?action=Vendor&textFieldId=sslineName&accountType=SteamShipline";
AjaxAutocompleter("sslineName","sslineNameDiv","sslineNo","sslineNameValid",autocompleterUrl,"","");
