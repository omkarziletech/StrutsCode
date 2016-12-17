var textClass = "textlabelsBoldForTextBox";
var textClassReadOnly = "textlabelsBoldForTextBoxDisabledLook";
var originalBatchAmount = jQuery("#batchAmount").val();
var originalBatchBalance = jQuery("#batchBalance").val();
jQuery(document).ready(function(){
    greyOutForDirectGlAccount(jQuery("#glAccount").val());
    greyOutForNetSett(jQuery("#glAccount").val(),jQuery("#description").val());
    jQuery("#netsettlement").click(function(){
	greyOutForNetSett("");
    });

    jQuery("#txtcal2").change(function(){
	if(jQuery.trim(jQuery(this).val())!="" && !isDate(jQuery(this))){
	    jQuery(this).val("").focus();
	    jQuery.prompt("Please enter date in mm/dd/yyyy format");
	}
    });

    jQuery("#directGlAccount").click(function(){
	greyOutForDirectGlAccount("");
    });
    
    jQuery("#batchAmount").keydown(function(event){
	allowOnlyNumbers(event,this);
    });
    jQuery("#batchAmount").keyup(function(event){
	validateAmount(event,this);
    });

    jQuery("#batchAmount").change(function(){
	onChangeBatchAmount();
    });

    jQuery("#bankAccount").change(function(){
	var bankAccount = jQuery(this).val();
	if(!jQuery("#netsettlement").is(":checked")){
	    if(bankAccount.indexOf("Zero batch")>-1 || bankAccount.indexOf("xxxxxxx")>-1){
		jQuery("#batchAmount").val("0.00").addClass(textClassReadOnly).attr("readonly",true);
	    }else{
		jQuery("#batchAmount").removeClass(textClassReadOnly).addClass(textClass).removeAttr("readonly");
	    }
	    onChangeBatchAmount();
	}
	if(bankAccount==""){
	    jQuery("#glAccount").val("");
	}else{
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.logiware.dwr.ArDwr",
                    methodName: "getGLAccountNumber",
                    param1: bankAccount,
                    dataType: "json"
                },
                preloading: true,
                success: function (data) {
                    if (data) {
                        jQuery("#glAccount").val(data);
                        jQuery("#glAccountValid").val(data);
                    }
                }
            });
	}
    });

    jQuery("#createOrUpdateBatch").click(function () {
        if (validateFields()) {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.logiware.dwr.ArDwr",
                    methodName: "isPeriodOpenForThisDate",
                    param1: jQuery("#txtcal1").val(),
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
                                param1: jQuery("#txtcal1").val(),
                                param2: jQuery("#bankAccount").val(),
                                param3: jQuery("#glAccount").val(),
                                dataType: "json"
                            },
                            preloading: true,
                            success: function (result) {
                                if (result == "available") {
                                    jQuery("#action").val("createOrUpdateArBatch");
                                    jQuery("#arBatchForm").submit();
                                } else {
                                    jQuery.prompt(result);
                                }
                            }
                        });
                    } else {
                        jQuery.prompt("The period is not open for this deposit date");
                    }
                }
            });
        }
    });

    jQuery("#applypayments").click(function () {
        if (validateFields()) {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.logiware.dwr.ArDwr",
                    methodName: "isPeriodOpenForThisDate",
                    param1: jQuery("#txtcal1").val(),
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
                                param1: jQuery("#txtcal1").val(),
                                param2: jQuery("#bankAccount").val(),
                                param3: jQuery("#glAccount").val(),
                                dataType: "json"
                            },
                            preloading: true,
                            success: function (result) {
                                if (result == "available") {
                                    jQuery("#action").val("createOrUpdateAndApplyPayments");
                                    jQuery("#arBatchForm").submit();
                                } else {
                                    jQuery.prompt(result);
                                }
                            }
                        });
                    } else {
                        jQuery.prompt("The period is not open for this deposit date");
                    }
                }
            });
        }
    });

    jQuery("#goBack").click(function () {
        jQuery("#action").val("goBack");
        jQuery("#arBatchForm").submit();
    });
});

function greyOutForNetSett(glAccount,description){
    if(jQuery("#netsettlement").is(":checked")){
	jQuery("#directGlAccount").attr("checked", false).hide();
	jQuery("#batchAmount").val("0.00").addClass(textClassReadOnly).attr("readonly",true);
	jQuery("#bankAccount").val("").addClass(textClassReadOnly).attr("disabled",true);
	jQuery("#glAccount").val(jQuery("#netSettGlAccount").val()).addClass(textClassReadOnly).attr("readonly",true);
	jQuery("#glAccountValid").val(jQuery("#netSettGlAccount").val()).addClass(textClassReadOnly).attr("readonly",true);
	jQuery("#description").val("").addClass(textClassReadOnly).attr("readonly",true);
    }else{
	jQuery("#directGlAccount").show();
	jQuery("#batchAmount").removeClass(textClassReadOnly).addClass(textClass).removeAttr("readonly");
	var bankAccount = jQuery("#bankAccount").val();
	if(bankAccount.indexOf("Zero batch")>-1 || bankAccount.indexOf("xxxxxxx")>-1){
	    jQuery("#batchAmount").val("0.00").addClass(textClassReadOnly).attr("readonly",true);
	}else{
	    jQuery("#batchAmount").removeClass(textClassReadOnly).addClass(textClass).removeAttr("readonly");
	}
	if(jQuery("#directGlAccount").is(":checked")){
	    jQuery("#glAccount").val(glAccount).removeClass(textClass).addClass(textClassReadOnly).attr("readonly",true);
	    jQuery("#description").val("").addClass(textClassReadOnly).attr("readonly",true);
	}else{
	    jQuery("#bankAccount").removeClass(textClassReadOnly).addClass(textClass).removeAttr("disabled");
	    jQuery("#description").removeClass(textClassReadOnly).addClass(textClass).removeAttr("readonly");
	}
	jQuery("#glAccount").val(glAccount);
	jQuery("#description").val(description);
    }
}

function greyOutForDirectGlAccount(glAccount){
    if(jQuery("#directGlAccount").is(":checked")){
	jQuery("#glAccount").val(glAccount).removeClass(textClassReadOnly).addClass(textClass).removeAttr("readonly");
	jQuery("#bankAccount").val("").addClass(textClassReadOnly).attr("disabled",true);
	jQuery("#description").val("").addClass(textClassReadOnly).attr("readonly",true);
    }else{
	jQuery("#glAccount").val(glAccount).removeClass(textClass).addClass(textClassReadOnly).attr("readonly",true);
	jQuery("#bankAccount").removeClass(textClassReadOnly).addClass(textClass).removeAttr("disabled");
	jQuery("#description").removeClass(textClassReadOnly).addClass(textClass).removeAttr("readonly");
    }
    var bankAccount = jQuery("#bankAccount").val();
    if(bankAccount.indexOf("Zero batch")>-1 || bankAccount.indexOf("xxxxxxx")>-1){
	jQuery("#batchAmount").val("0.00").addClass(textClassReadOnly).attr("readonly",true);
    }else{
	jQuery("#batchAmount").removeClass(textClassReadOnly).addClass(textClass).removeAttr("readonly");
    }
}

function onChangeBatchAmount(){
    var batchAmount = jQuery("#batchAmount").val();
    if(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test(batchAmount)){
	var balance = Number(batchAmount.replace(/[^-0-9.]+/g,''))-Number(originalBatchAmount.replace(/[^-0-9.]+/g,''));
	balance+=Number(originalBatchBalance.replace(/[^-0-9.]+/g,''));
	jQuery("#batchBalance").val(balance.toFixed(2));
    }else{
	jQuery.prompt("Please enter the valid number");
	jQuery("#batchAmount").val("").focus();
    }
}

function validateFields(){
    if(jQuery("#txtcal1").val()==""){
	jQuery.prompt("Please select deposit date");
	jQuery("txtcal1").focus();
	return false;
    }else if(jQuery("#batchAmount").val()==""){
	jQuery.prompt("Please enter batch amount");
	jQuery("#batchAmount").focus();
	return false;
    }else if(jQuery("#directGlAccount").is(":checked") && jQuery("#glAccount").val()==""){
	jQuery.prompt("Please enter GL account");
	jQuery("#glAccount").focus();
	return false;
    }else if(jQuery("#glAccount").val()==""){
	jQuery.prompt("Please select bank account to select GL account");
	jQuery("#bankAccount").focus();
	return false;
    }else if(jQuery("#description").val().length>200){
	jQuery.prompt("Bank account description should not be more than 200 characters");
	jQuery("#description").val("").focus();
	return false;
    }else if(jQuery("#notes").val().length>200){
	jQuery.prompt("Notes should not be more than 200 characters");
	jQuery("#notes").val("").focus();
	return false;
    }
    return true;
}

var autocompleterUrl = rootPath+"/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=glAccount";
AjaxAutocompleter("glAccount","glAccountChoices","","glAccountValid",autocompleterUrl,"","");
