/* 
 * Document   : clientSearch
 * Created on : Jul 23, 2012, 08:25:00 PM
 * Author     : Lakshmi Naryanan
 * Description: Javascript for Client Search
 */

function initAutocompleteWithOptions(txtName,choiceName,txtIds,txtCheck,action,update,clear,addMoreParams) {
    new Ajax.Autocompleter(txtName, choiceName, action, {
	callback: function(element, entry){
	    if(addMoreParams != null && trim(addMoreParams) != "") {
		var params = eval(addMoreParams);
		if(null!=params && trim(params)!=""){
		    entry = entry + params;
		}
	    }
	    return entry;
	},
	paramName: txtName,
	tokens:"<-->",
	afterUpdateElement : function (text, li) {
	    if(txtIds != ""){
		var values = li.id.split("^");
		var fieldIds = txtIds.split("^");
		for(var index = 0; index < fieldIds.length; index++){
		    var fields = fieldIds[index].split(":");
		    $(fields[0]).value = values[index];
		    if(fields.length==2){
			$(fields[1]).value = values[index];//For Check field
		    }
		}
	    }
	    
	    $(txtName).blur();
	    if(update != null && trim(update) != "") {
		eval(update);
	    }
	}
    });

    Event.observe(txtName, "blur", function (event){
	var element = Event.element(event);
	if($(txtCheck)!=undefined && element.value!=$(txtCheck).value){
	    if(clear != null && trim(clear) != "") {
		eval(clear);
	    }
	    element.value = '';
	}
    });
}

function showClientSearchOption(){
    showPopUp();
    jQuery("#clientSearchOptions").show();
}

function closeClientSearchOption(){
    closePopUp();
    jQuery("#clientSearchOptions").hide();
    if(jQuery("#searchClientBy").val()=="Client"){
	jQuery("#customerName").focus();
    }else if(jQuery("#searchClientBy").val()=="Email"){
	jQuery("#email").focus();
    }else{
	jQuery("#contactName").focus();
    }
}

function addClientParams() {
    var params="&consignee="+jQuery("#clientConsigneeCheck").is(":checked");
    params+="&state="+jQuery.trim(jQuery("#clientState").val());
    params+="&zipCode="+jQuery.trim(jQuery("#clientZipCode").val());
    params+="&salesCode="+jQuery.trim(jQuery("#clientSalesCode").val());
    params+="&displayOneLine="+jQuery("#displayClientOneLine").is(":checked");
    if(jQuery("#searchClientBy").val()=="Client"){
	params+="&enabled=Client";
    }else if(jQuery("#searchClientBy").val()=="Email"){
	params+="&enabled=Email";
    }else{
	params+="&enabled=Contact";
    }
    return params;
}

if(jQuery("#customerName").length>0){
    url = rootPath+"/actions/getClientResults.jsp?input=Client&field=customerName";
    initAutocompleteWithOptions("customerName","custname_choices","customerName:custname_check^clientNumber","custname_check",url,"focusSetting(false);","","addClientParams()");
}

if(jQuery("#contactName").length>0){
    url = rootPath+"/actions/getClientResults.jsp?input=Contact&field=contactName";
    initAutocompleteWithOptions("contactName","contactName_choices","customerName:custname_check^clientNumber^contactName^email","",url,"focusSetting(true);","","addClientParams()");
}

if(jQuery("#email").length>0){
    url = rootPath+"/actions/getClientResults.jsp?input=Email&field=email";
    initAutocompleteWithOptions("email","email_choices","customerName:custname_check^clientNumber^contactName^email","",url,"focusSetting(true);","","addClientParams()");
}

function showCustomerSearchOption(){
    showPopUp();
    jQuery("#customerSearchOptions").show();
}

function submitCustomerSearchOption(){
    jQuery("#customerSearchOptions").hide();
    closePopUp();
}

function addCustomerParams() {
    var country=jQuery.trim(jQuery("#customerCountry").val()).split("/");
    var param="&customerStateId="+jQuery.trim(jQuery("#customerState").val());
    param+="&customerCountryId="+country[0];
    return param;
}

if(jQuery("#name").length>0){
    url = rootPath+"/actions/tradingPartner.jsp?tabName=TRADING_PARTNER&from=0";
    initAutocompleteWithOptions("name","name_choices","account","name_check",url,"searchform()","","addCustomerParams()");
}

function clearCustomerSearchOption(){
    closePopUp();
    jQuery("#customerState").val("");
    jQuery("#customerCountry").val("");
    jQuery("#customerSearchOptions").hide();
}

function addCountryParams() {
    var param ="&customerCountry="+jQuery.trim(jQuery("#customerCountry").val());
    return param;
}
// Autocompleter for country field
initAutocomplete("customerCountry","country_choices","","country_check",
    rootPath+"/actions/getUnlocationCodeDesc.jsp?tabName=TRADING_PARTNER&from=11&isDojo=false","addCountryParams()");

