
jQuery(document).ready(function(){
    jQuery("[title != '']").not("link").tooltip();
    jQuery(document).keypress(function(event) {
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if(keycode == 13) {
	    event.preventDefault();
	    search();
	}
    });
    jQuery("#txtcal1").change(function(){
	if(jQuery.trim(jQuery(this).val())!="" && !isDate(jQuery(this))){
	    jQuery(this).val("").focus();
	    jQuery.prompt("Please enter date in mm/dd/yyyy format");
	}
    });
    jQuery("#txtcal2").change(function(){
	if(jQuery.trim(jQuery(this).val())!="" && !isDate(jQuery(this))){
	    jQuery(this).val("").focus();
	    jQuery.prompt("Please enter date in mm/dd/yyyy format");
	}
    });
    jQuery("#search").click(function(){
	var fromDate = new Date(jQuery("#txtcal1").val()).getTime();
	var toDate = new Date(jQuery("#txtcal2").val()).getTime();
	if(isNaN(jQuery("#batchId").val())) {
	    jQuery.prompt('Please enter valid batch number');
	    jQuery("#batchId").focus();
	}else if(!isNaN(fromDate) && !isNaN(toDate) && fromDate>toDate){
	    jQuery.prompt("Invalid date range!\nFrom Date cannot be greater than To Date!")
	}else{
	    jQuery("#pageNo").val("1");
	    jQuery("#sortBy").val("");
	    jQuery("#orderBy").val("");
	    jQuery("#action").val("searchArBatch")
	    jQuery("#arBatchForm").submit();
	}
    });

    jQuery("#addArBatch").click(function(){
	jQuery("#action").val("addArBatch");
	jQuery("#arBatchForm").submit();
    });

    jQuery("#reset").click(function(){
	jQuery("#action").val("refreshArBatch");
	jQuery("#arBatchForm").submit();
    });

    jQuery("#batchAmount").change(function(){
	if(isNaN(jQuery(this).val())){
	    jQuery.prompt("Please enter valid amount");
	    jQuery(this).val("").focus();
	}
    });

    jQuery("#searchByUser").click(function(){
	jQuery("#user").val("");
	if(jQuery(this).is(":checked")){
	    jQuery("#user").show();
	}else{
	    jQuery("#user").hide();
	}
    });

    jQuery("#batchAmount").keydown(function(event){
	allowOnlyNumbers(event,this);
    });
    jQuery("#batchAmount").keyup(function(event){
	validateAmount(event,this);
    });
});

function search(){
    var fromDate = new Date(jQuery("#txtcal1").val()).getTime();
    var toDate = new Date(jQuery("#txtcal2").val()).getTime();
    if(isNaN(jQuery("#batchId").val())) {
	jQuery.prompt('Please enter valid batch number',{  
	    callback: function(){  
		jQuery("#batchId").focus().fadeIn().fadeOut().fadeIn();
	    } 
	});
    }else if(!isNaN(fromDate) && !isNaN(toDate) && fromDate>toDate){
	jQuery.prompt("Invalid date range!\nFrom Date cannot be greater than To Date!",{  
	    callback: function(){
		jQuery("#txtcal1").focus().fadeIn().fadeOut().fadeIn();
	    } 
	});
    }else{
	jQuery("#pageNo").val("1");
	jQuery("#sortBy").val("");
	jQuery("#orderBy").val("");
	jQuery("#action").val("searchArBatch");
	jQuery("#arBatchForm").submit();
    }
}
function gotoPage(pageNo){
    jQuery("#pageNo").val(pageNo);
    jQuery("#action").val("searchArBatch");
    jQuery("#arBatchForm").submit();
}
function gotoSelectedPage(){
    jQuery("#pageNo").val(jQuery("#selectedPageNo").val());
    jQuery("#action").val("searchArBatch");
    jQuery("#arBatchForm").submit();
}
function doSort(sortBy,orderBy){
    jQuery("#sortBy").val(sortBy);
    jQuery("#orderBy").val(orderBy);
    jQuery("#action").val("searchArBatch");
    jQuery("#arBatchForm").submit();
}

function editArBatch(batchId){
    jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.logiware.dwr.ArDwr",
                methodName: "lockArBatch",
                param1: batchId,
                request: "true",
                dataType: "json"
            },
            preloading: true,
            success: function (data) {
                if (data == "success") {
                    jQuery("#selectedBatchId").val(batchId);
                    jQuery("#action").val("editArBatch");
                    jQuery("#arBatchForm").submit();
                } else {
                    jQuery.prompt(data);
                }
            }
        });
}

function printArBatch(batchId){
    jQuery("#selectedBatchId").val(batchId);
    jQuery("#action").val("printArBatch");
    jQuery("#arBatchForm").submit();
}

function voidArBatch(batchId){
    jQuery.prompt("Do you want to void this batch - "+batchId+"?",{
	buttons:{
	    Yes:true,
	    No:false
	},
	callback: function(v,m,f){
	    if(v){
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.logiware.dwr.ArDwr",
                        methodName: "lockArBatch",
                        param1: batchId,
                        request: "true",
                        dataType: "json"
                    },
                    preloading: true,
                    success: function (data) {
                        if (data == "success") {
                            jQuery("#selectedBatchId").val(batchId);
                            jQuery("#action").val("voidArBatch");
                            jQuery("#arBatchForm").submit();
                        } else {
                            jQuery.prompt(data);
                        }
                    }
                });
	    }
	}
    });
}

function showArBatchNotes(batchId){
    var moduleId = jQuery.trim(jQuery("#notesConstantArBatch").val());
    GB_show("Notes", rootPath+"/notes.do?moduleId="+moduleId+"&moduleRefId="+batchId,375,700);
}

function postArBatch(batchId) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.logiware.dwr.ArDwr",
            methodName: "lockArBatch",
            param1: batchId,
            request: "true",
            dataType: "json"
        },
        preloading: true,
        success: function (data) {
            if (data == "success") {
                jQuery.ajaxx({
                    data: {
                        className: "com.logiware.dwr.ArDwr",
                        methodName: "showPostBatch",
                        forward: "/jsps/AccountsRecievable/postArBatch.jsp",
                        param1: batchId,
                        request: "true",
                        dataType: "json"
                    },
                    preloading: true,
                    success: function (result) {
                        if (result) {
                            showAlternateMask();
                            jQuery("<div style='width:850px;height:200px'></div>").html(result).addClass("popup").appendTo("#arBatchForm").center();
                            if (document.getElementById("otherCustomerName")) {
                                var otherAutocompleterUrl = rootPath + "/servlet/AutoCompleterServlet?action=Vendor&textFieldId=otherCustomerName";
                                AjaxAutocompleter("otherCustomerName", "otherCustomerNameChoices", "otherCustomerNumber", "otherCustomerNameValid", otherAutocompleterUrl);
                                jQuery("#otherCustomer").click(function () {
                                    if (jQuery(this).is(":checked")) {
                                        jQuery(".otherCustomer").select().focus().show();
                                    } else {
                                        jQuery(".otherCustomer").hide();
                                    }
                                });
                            }
                        } else {
                            jQuery.prompt(result);
                        }
                    }
                });
            } else {
                jQuery.prompt(data);
            }
        }
    });
}

function closePostDiv(batchId){
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.logiware.dwr.ArDwr",
            methodName: "unLockArBatch",
            param1: batchId,
            request: "true",
            dataType: "json"
        },
        preloading: true,
        success: function (data) {
            if (data == "success") {
                closePopUpDiv();
            } else {
                jQuery.prompt(data);
            }
        }
    });
}

function post(batchId, depositDate, bankAccount, glAccount) {
    jQuery('.popup').hide();
    jQuery('.mask').hide();
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.logiware.dwr.ArDwr",
            methodName: "isPeriodOpenForThisDate",
            param1: depositDate,
            dataType: "json"
        },
        preloading: true,
        success: function (data) {
            if (data) {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.logiware.dwr.ArDwr",
                        methodName: "isNotReconciledDate",
                        param1: depositDate,
                        param2: bankAccount,
                        param3: glAccount,
                        dataType: "json"
                    },
                    preloading: true,
                    success: function (result) {
                        if (result == "available") {
                            if (jQuery("#otherCustomer").is(":checked") && jQuery("#otherCustomerNumber").val() == "") {
                                jQuery.prompt("Other Customer option selected. Please enter Other Customer");
                            } else {
                                jQuery.ajaxx({
                                    dataType: "json",
                                    data: {
                                        className: "com.logiware.dwr.ArDwr",
                                        methodName: "validateGLAccountForAccruals",
                                        param1: batchId,
                                        dataType: "json"
                                    },
                                    preloading: true,
                                    success: function (data) {
                                        if (data == "valid") {
                                            jQuery("#selectedBatchId").val(batchId);
                                            jQuery("#action").val("postArBatch");
                                            jQuery("#arBatchForm").submit();
                                        } else {
                                            var url = rootPath + "/accruals.do?buttonValue=addValidGLAccountTemplate&transactionIdsForValidation=" + data;
                                            window.parent.parent.showGreyBox("Update GL Accounts", url);
                                        }
                                    }
                                });
                            }
                        } else {
                            jQuery('.mask').show();
                            jQuery('.popup').show();
                            jQuery.prompt(result);
                        }
                    }
                });
            } else {
                jQuery('.mask').show();
                jQuery('.popup').show();
                jQuery.prompt("Please select another period, this period is not yet open or closed");
            }
        }
    });
}

function applyPayments(batchId) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.logiware.dwr.ArDwr",
            methodName: "lockArBatch",
            param1: batchId,
            request: "true",
            dataType: "json"
        },
        preloading: true,
        success: function (data) {
            if (data == "success") {
                jQuery("#selectedBatchId").val(batchId);
                jQuery("#action").val("applyPayments");
                jQuery("#arBatchForm").submit();
            } else {
                jQuery.prompt(data);
            }
        }
    });
}

function showChecks(batchId,canEdit){
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.logiware.dwr.ArDwr",
            methodName: "lockArBatch",
            param1: batchId,
            request: "true",
            dataType: "json"
        },
        preloading: true,
        success: function(data) {
            if (data === "success") {
                jQuery.ajaxx({
                    data: {
                        className: "com.logiware.dwr.ArDwr",
                        methodName: "showChecks",
                        forward: "/jsps/AccountsRecievable/arBatchChecks.jsp",
                        param1: batchId,
                        param2: canEdit,
                        request: "true",
                        dataType: "json"
                    },
                    preloading: true,
                    success: function (result) {
                        if (result) {
                            showAlternateMask();
                            jQuery("<div style='width:800px;height:200px'></div>").html(result).addClass("popup").appendTo("body").center();
                        }
                    }
                });
            } else {
                jQuery.prompt(data);
            }
        }
    });
}

function closeCheckDiv(batchId) {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.logiware.dwr.ArDwr",
                methodName: "unLockArBatch",
                param1: batchId,
                request: "true",
                dataType: "json"
            },
            preloading: true,
            success: function (data) {
                if (data == "success") {
                    closePopUpDiv();
                    jQuery("#action").val("searchArBatch");
                    jQuery("#arBatchForm").submit();
                } else {
                    jQuery.prompt(data);
                }
            }
        });
}
function editCheck(batchId,checkId,subType){
    closePopUpDiv();
    jQuery("#selectedBatchId").val(batchId);
    jQuery("#paymentCheckId").val(checkId);
    jQuery("#selectedSubType").val(subType);
    jQuery("#action").val("editCheck");
    jQuery("#arBatchForm").submit();
}

function deleteCheck(batchId, checkId, checkNumber) {
    jQuery.prompt("Do you want to remove this check - " + checkNumber + " from batch - " + batchId + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v, m, f) {
            if (v) {
                jQuery('.popup').hide();
                jQuery('.mask').hide();
                jQuery(".checkMessage").html("");
                jQuery(".checkError").html("");
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.logiware.dwr.ArDwr",
                        methodName: "voidCheck",
                        param1: batchId,
                        param2: checkId,
                        request: "true",
                        dataType: "json"
                    },
                    preloading: true,
                    success: function (data) {
                        if (data) {
                            closePopUpDiv();
                            jQuery.ajaxx({
                                data: {
                                    className: "com.logiware.dwr.ArDwr",
                                    methodName: "showChecks",
                                    forward: "/jsps/AccountsRecievable/arBatchChecks.jsp",
                                    param1: batchId,
                                    param2: "true",
                                    request: "true",
                                    dataType: "json"
                                },
                                preloading: true,
                                success: function (result) {
                                    if (result) {
                                        showAlternateMask();
                                        jQuery("<div style='width:800px;height:200px'></div>").html(result).addClass("popup").appendTo("body").center();
                                        jQuery(".checkMessage").html("Check - " + checkNumber + " is removed from batch - " + batchId + " successfully");
                                    }
                                }
                            });
                        } else {
                            jQuery(".checkError").html("Error in removing check - " + checkNumber + " from batch - " + batchId);
                        }
                    }
                });
            }
        }
    });
}

function showScanOrAttach(documentId){
    GB_show("Scan/Attach", rootPath+"/scan.do?screenName=AR BATCH&documentId="+documentId,375,700);
}
function exportNSInvoice(batchId){
    jQuery("#selectedBatchId").val(batchId);
    jQuery("#action").val("exportNSInvoice");
    jQuery("#arBatchForm").submit();
    closePreloading();
}
function printNSInvoice(batchId){
    jQuery("#selectedBatchId").val(batchId);
    jQuery("#action").val("printNSInvoice");
    jQuery("#arBatchForm").submit();
}

function reversePost(batchId) {
    jQuery('.popup').hide();
    jQuery('.mask').hide();
    jQuery.prompt("Do you want to reverse this batch - " + batchId + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v, m, f) {
            if (v) {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.logiware.dwr.ArDwr",
                        methodName: "lockArBatch",
                        param1: batchId,
                        request: "true",
                        dataType: "json"
                    },
                    preloading: true,
                    success: function (data) {
                        if (data == "success") {
                            jQuery("#selectedBatchId").val(batchId);
                            jQuery("#action").val("reversePost");
                            jQuery("#arBatchForm").submit();
                        } else {
                            jQuery.prompt(data);
                        }
                    }
                });
            }
        }
    });
}
