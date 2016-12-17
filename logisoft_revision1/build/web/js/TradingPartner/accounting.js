function setcreditterms() {
    document.tradingPartnerForm.creditLimit.value == "0.00" ? document.tradingPartnerForm.creditRate.selectedIndex = 1
    : document.tradingPartnerForm.creditLimit.value !="0.00" ? document.tradingPartnerForm.creditRate.selectedIndex = 3
    : document.tradingPartnerForm.creditLimit.value =="" ? document.tradingPartnerForm.creditRate.selectedIndex = 0
    : document.tradingPartnerForm.creditRate.selectedIndex = 0;
}
function setCreditStatus1(){
    document.accountingForm.suspendCredit.checked? document.accountingForm.creditStatus.selectedIndex=4: document.accountingForm.legal.checked?document.accountingForm.creditStatus.selectedIndex=5:document.accountingForm.creditStatus.selectedIndex=0;
}
function setCreditStatus2(){
    document.accountingForm.legal.checked? document.accountingForm.creditStatus.selectedIndex=5: document.accountingForm.suspendCredit.checked?document.accountingForm.creditStatus.selectedIndex=4:document.accountingForm.creditStatus.selectedIndex=0;
}
function loadContactDetails(type) {
    document.tradingPartnerForm.buttonValue.value = type;
    document.tradingPartnerForm.creditLimit.disabled=false;
    document.tradingPartnerForm.creditRate.disabled=false;
    document.tradingPartnerForm.submit();
}

function addContactDetails(type,contactId) {
    document.tradingPartnerForm.contactId.value = contactId;
    document.tradingPartnerForm.buttonValue.value = type;
    document.tradingPartnerForm.creditLimit.disabled=false;
    document.tradingPartnerForm.creditRate.disabled=false;
    document.tradingPartnerForm.submit();
}

function saveARConfig() {
    if(document.tradingPartnerForm.creditLimit.value==''){
        alert("Please Enter the Credit Limit");
        document.tradingPartnerForm.creditLimit.focus();
        return;
    }
    if(document.tradingPartnerForm.pastDueBuffer.value==""){
        alert("Please enter the past due buffer");
        document.tradingPartnerForm.pastDueBuffer.focus();
        return;
    }
    document.tradingPartnerForm.buttonValue.value ="saveARConfiguration";
    document.tradingPartnerForm.creditLimit.disabled=false;
    document.tradingPartnerForm.creditRate.disabled=false;
    document.tradingPartnerForm.submit();
}
function getMasterAddress(){
    document.tradingPartnerForm.buttonValue.value="getAddressFromMaster";
    document.tradingPartnerForm.creditLimit.disabled=false;
    document.tradingPartnerForm.creditRate.disabled=false;
    document.tradingPartnerForm.submit();
}
function view(form){
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if(element.type=="button"){
            if(element.value=="Add" || element.value=="Use From Master"){
                element.style.visibility="hidden";
            }
        }
    }
}
function ActiveCreditStatus(){
    if(document.tradingPartnerForm.extendCredit.checked==true){
        document.tradingPartnerForm.creditStatus.disabled=false;
    }else{
        document.tradingPartnerForm.creditStatus.disabled=true;
    }
}
function setCreditStatus(){
    if(document.tradingPartnerForm.extendCredit.checked==true){
        document.tradingPartnerForm.creditStatus.disabled=false;
    }else{
        document.tradingPartnerForm.creditStatus.disabled=true;
    }

}

function grayOutFields(action){
    if(action=="statements"){
        var statements = document.tradingPartnerForm.statements;
        //alert(statements);
        if(statements[statements.selectedIndex].text=="No"){
            document.tradingPartnerForm.creditbalance.disabled=true;
            document.tradingPartnerForm.creditinvoice.disabled=true;
            document.tradingPartnerForm.schedulestmt.disabled=true;
        }else{
            document.tradingPartnerForm.creditbalance.disabled=false;
            document.tradingPartnerForm.creditinvoice.disabled=false;
            document.tradingPartnerForm.schedulestmt.disabled=false;
        }
    }
}

function validateCredit(action){
    creditStatus = document.tradingPartnerForm.creditStatus;
    creditLimit = document.tradingPartnerForm.creditLimit;
    creditRate = document.tradingPartnerForm.creditRate;
    if(action=="creditStatus"){
        if(creditStatus[creditStatus.selectedIndex].text=="No Credit" 
            || creditStatus[creditStatus.selectedIndex].text=="Suspended/See Accounting"
            || creditStatus[creditStatus.selectedIndex].text=="Legal/See Accounting"){
            creditLimit.value="0.00";
            for (i=0; i<creditRate.options.length; i++){
                if (creditRate.options[i].text=="Due Upon Receipt"){
                    creditRate.options[i].selected = true;
                    break;
                }
            }
        }else{
            if(creditRate[creditRate.selectedIndex].text=="Due Upon Receipt"){
                for (i=0; i<creditRate.options.length; i++){
                    if (creditRate.options[i].text=="Net 7 Days"){
                        creditRate.options[i].selected = true;
                        break;
                    }
                }
            }
            if(trim(creditLimit.value)=="" || Number(creditLimit.value.replace(/[^0-9.]+/g,'')).toFixed(2)==0.00){
                creditLimit.value="";
                creditLimit.focus();
                alert("Please Enter the Credit Limit");
            }
        }
    }else if(action=="creditTerms"){
        if(creditRate[creditRate.selectedIndex].text=="Due Upon Receipt"){
            creditLimit.value="0.00";
            for (i=0; i<creditStatus.options.length; i++){
                if (creditStatus.options[i].text=="No Credit"){
                    creditStatus.options[i].selected = true;
                    break;
                }
            }
        }else{
            if(creditStatus[creditStatus.selectedIndex].text=="No Credit"
                || creditStatus[creditStatus.selectedIndex].text=="Suspended/See Accounting"
                || creditStatus[creditStatus.selectedIndex].text=="Legal/See Accounting"){
                for (i=0; i<creditStatus.options.length; i++){
                    if (creditStatus.options[i].text=="In Good Standing"){
                        creditStatus.options[i].selected = true;
                        break;
                    }
                }
            }
            if(trim(creditLimit.value)=="" || Number(creditLimit.value.replace(/[^0-9.]+/g,'')).toFixed(2)==0.00){
                creditLimit.value="";
                creditLimit.focus();
                alert("Please Enter the Credit Limit");
            }
        }
    }else if(action=="creditLimit"){
        if(!isNaN(creditLimit .value)){
            if(creditLimit .value.indexOf(".")>=0){
                var creditLimitValue = creditLimit .value.split(".");
                if(creditLimitValue.length==2){
                    if(creditLimitValue[0].length>8){
                        creditLimit.value="0.00";
                    }else{
                        creditLimit.value=addCommas(Number(creditLimit .value).toFixed(2));
                    }
                }
            }else if(creditLimit .value.length>8){
                creditLimit .value=addCommas(Number(creditLimit .value.substring(0,8)).toFixed(2));
            }else{
                creditLimit .value=addCommas(Number(creditLimit .value).toFixed(2));
            }
        }else{
            creditLimit.value="";
        }
        if(trim(creditLimit.value)==''){
            document.tradingPartnerForm.creditLimit.focus();
            alert("Please Enter the Credit Limit");
        }else if(Number(creditLimit.value.replace(/[^0-9.]+/g,'')).toFixed(2)==0.00){
            if(creditStatus[creditStatus.selectedIndex].text!="No Credit"
                && creditStatus[creditStatus.selectedIndex].text!="Suspended/See Accounting"
                && creditStatus[creditStatus.selectedIndex].text!="Legal/See Accounting"){
                for (i=0; i<creditStatus.options.length; i++){
                    if (creditStatus.options[i].text=="No Credit"){
                        creditStatus.options[i].selected = true;
                        break;
                    }
                }
                for (i=0; i<creditRate.options.length; i++){
                    if (creditRate.options[i].text=="Due Upon Receipt"){
                        creditRate.options[i].selected = true;
                        break;
                    }
                }
            }
        }else{
            if(creditStatus[creditStatus.selectedIndex].text=="No Credit"
                || creditStatus[creditStatus.selectedIndex].text=="Suspended/See Accounting"
                || creditStatus[creditStatus.selectedIndex].text=="Legal/See Accounting"){
                for (i=0; i<creditStatus.options.length; i++){
                    if (creditStatus.options[i].text=="In Good Standing"){
                        creditStatus.options[i].selected = true;
                        break;
                    }
                }
            }
            if(creditRate[creditRate.selectedIndex].text=="Due Upon Receipt"){
                for (i=0; i<creditRate.options.length; i++){
                    if (creditRate.options[i].text=="Net 7 Days"){
                        creditRate.options[i].selected = true;
                        break;
                    }
                }
            }
        }
    }
}

function validatePastDueBuffer(){
    var pastDueBuffer = document.tradingPartnerForm.pastDueBuffer;
    if(isNaN(pastDueBuffer.value)){
        alert("Please enter valid number");
        document.getElementById("pastDueBuffer").value = "";
        document.getElementById("pastDueBuffer").focus();
        return;
    }
}


function addCommas(nStr)
{
    nStr += '';
    x = nStr.split('.');
    x1 = x[0];
    x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    return x1 + x2;
}
function setPopupAttributes(id){
    document.getElementById(id).style.display = 'block';
    floatDiv(id, document.body.offsetWidth/5,document.body.offsetTop+40).floatIt();
}
function closePopup(id){
    document.getElementById(id).style.display = 'none';
    document.getElementById('cover').style.display = 'none';
}
if(document.getElementById("arCodeName")){
    AjaxAutocompleter("arCodeName", "arCodeNameDiv","arCode", "arCodeNameValid", rootPath+"/servlet/AutoCompleterServlet?action=User&get=id&textFieldId=arCodeName","","");
}

function arscanAttach(documentId){
    GB_show("Scan", rootPath+"/scan.do?screenName=ARCONFIG&documentId=" +documentId, 300, 650);
}