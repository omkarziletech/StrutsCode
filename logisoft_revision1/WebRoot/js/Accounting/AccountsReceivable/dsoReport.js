
function refresh(){
    document.dsoReportForm.action.value="clear";
    document.dsoReportForm.submit();
}
if(document.getElementById("userName")){
    AjaxAutocompleter("userName", "userNameDiv","userId", "userNameValid", rootPath+"/servlet/AutoCompleterServlet?action=User&get=id&textFieldId=userName","","");
}
if(document.getElementById("fromPeriod")){
    AjaxAutocompleter("fromPeriod", "fromPeriodChoices","fromPeriodId", "fromPeriodValid", rootPath+"/servlet/AutoCompleterServlet?action=FiscalPeriod&textFieldId=fromPeriod&from=ApReports","getNumberOfDays()","","clearFromPeriodValues()");
}
if(document.getElementById("toPeriod")){
    AjaxAutocompleter("toPeriod", "toPeriodChoices","toPeriodId", "toPeriodValid", rootPath+"/servlet/AutoCompleterServlet?action=FiscalPeriod&textFieldId=toPeriod&from=ApReports","getNumberOfDays()","","clearToPeriodValues()");
}

function getNumberOfDays(){
    var fromDate = document.getElementById("fromPeriod");
    var toDate = document.getElementById("toPeriod");
    if(Date.parse(document.dsoReportForm.fromPeriod.value) > Date.parse(document.dsoReportForm.toPeriod.value)) {
        alert("From date should not be greater than To date");
        document.getElementById("fromPeriod").value = "";
        document.getElementById("toPeriod").value = "";
        document.getElementById("numberOfDays").value = "";
        document.dsoReportForm.fromPeriod.focus();
        return;
    }
    if(trim(fromDate.value)==""){
        document.getElementById("numberOfDays").value = "";
        return;
    }else if(trim(toDate.value)==""){
        document.getElementById("numberOfDays").value = "";
        return;
    }
    if(dwr.util.getValue("fromPeriodId")!="" && dwr.util.getValue("toPeriodId")!=""){
        FiscalPeriodBC.getNumberOfDays(dwr.util.getValue("fromPeriodId"),dwr.util.getValue("toPeriodId"),function(data){
            var json = ( function(){
                return eval( '(' + data + ')');
            })();
            if(json){
                dwr.util.setValue("numberOfDays",json.numberOfDays);
            }
        });
    }
}

function clearToPeriodValues(){
    document.getElementById("toPeriod").value = "";
    document.getElementById("numberOfDays").value = "";
}
function clearFromPeriodValues(){
    document.getElementById("fromPeriod").value = "";
    document.getElementById("numberOfDays").value = "";
}

function onchangeDso(){
    if(document.getElementById("searchDsoBy").value == 'AllAccountsReceivable'){
        document.getElementById("userName").value = "";
        document.getElementById("userName").className = "textlabelsBoldForTextBoxDisabledLook";
        document.getElementById("userName").disabled=true;
        document.getElementById("vendorName").value = "";
        document.getElementById("vendorName").className = "textlabelsBoldForTextBoxDisabledLook";
        document.getElementById("vendorName").disabled=true;
        document.getElementById("vendorName").value = "";
        document.getElementById("vendorNumber").value = "";
    }else if(document.getElementById("searchDsoBy").value == 'ByCollector'){
        document.getElementById("userName").className = "textlabelsBoldForTextBox";
        document.getElementById("userName").disabled=false;
        document.getElementById("vendorName").value = "";
        document.getElementById("vendorName").className = "textlabelsBoldForTextBoxDisabledLook";
        document.getElementById("vendorName").disabled=true;
        document.getElementById("vendorNumber").value = "";
    }else if(document.getElementById("searchDsoBy").value == 'ByCustomer'){
        document.getElementById("vendorName").className = "textlabelsBoldForTextBox";
        document.getElementById("vendorName").disabled=false;
        document.getElementById("userName").value = "";
        document.getElementById("userName").className = "textlabelsBoldForTextBoxDisabledLook";
        document.getElementById("userName").disabled=true;
    }
}

function gotoAction(action){
    if(trim(document.dsoReportForm.fromPeriod.value)==""){
        alert("Please Enter From Date");
        document.dsoReportForm.fromPeriod.focus();
    }else if(trim(document.dsoReportForm.toPeriod.value)==""){
        alert("Please Enter To Date");
        document.dsoReportForm.toPeriod.focus();
    }else if(document.getElementById("searchDsoBy").value == 'ByCollector' && trim(document.dsoReportForm.userName.value)==""){
        document.dsoReportForm.userName.focus();
        alert("Please Enter UserName");
    }else if(document.getElementById("searchDsoBy").value == 'ByCustomer' && trim(document.dsoReportForm.vendorName.value)==""){
        document.dsoReportForm.vendorName.focus();
        alert("Please Enter vendorName");
    }else{
        document.dsoReportForm.action.value=action;
        document.dsoReportForm.submit();
    }
}