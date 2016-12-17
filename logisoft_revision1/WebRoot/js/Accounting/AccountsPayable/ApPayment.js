dwr.engine.setTextHtmlHandler(dwrSessionError);
var messageLoginError = document.getElementById("messageLoginError").value;
var messageSuccess = document.getElementById("messageSuccess").value;
useLogisoftLodingMessageNew();
jQuery(document).ready(function(){
    jQuery(document).keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == 13) {
            showAll();
        }
    });
})
function searchResults(){
    if(navigator.appName == "Netscape"){
	showAll();
    }else if(event.keyCode==13 || event.keyCode==9 || event.button==0){
	showAll();
    }
}
function showAll(){
    window.parent.showPreloading();
    document.aPPaymentForm.button.value="showAll";
    document.aPPaymentForm.submit();
}

function clearAll(){
    window.parent.showPreloading();
    document.aPPaymentForm.button.value="clearAll";
    document.aPPaymentForm.submit();
}

function selectAllCheckBoxes(){
    var payCheckBox = document.getElementsByName('payCheckBox');
    var approveCheckBox = document.getElementsByName("approveCheckBox");
    if(document.aPPaymentForm.checkAll.checked){
	for(var i=0; i<payCheckBox.length;i++){
	    payCheckBox[i].checked=true;
	    if(document.aPPaymentForm.approveAll.checked){
		approveCheckBox[i].checked=true;
	    }
	}
    }else{
	for(var i=0; i<payCheckBox.length;i++){
	    payCheckBox[i].checked=false;
	    if(null!=approveCheckBox[i] && typeof approveCheckBox[i] != undefined
		&& !approveCheckBox[i].disabled && approveCheckBox[i].checked){
		approveCheckBox[i].checked=false;
	    }
	}
	document.aPPaymentForm.approveAll.checked=false;
    }
}

function approvedAllPay(){
    var payCheckBox = document.getElementsByName('payCheckBox');
    var approveCheckBox = document.getElementsByName("approveCheckBox");
    if(document.aPPaymentForm.approveAll.checked){
	for(var i=0; i<payCheckBox.length;i++){
	    if(payCheckBox[i].checked){
		approveCheckBox[i].checked=true;
	    }
	}
    }else{
	for(var i=0; i<payCheckBox.length;i++){
	    if(payCheckBox[i].checked){
		approveCheckBox[i].checked=false;
	    }
	}
    }
}

function approveOrUnApprove(obj){
    var payCheckBox = document.getElementsByName('payCheckBox');
    var rowIndex = obj.parentNode.parentNode.parentNode.rowIndex;
    rowIndex--;
    if(!payCheckBox[rowIndex].checked){
	obj.checked = false;
    }
}
function payOrUnPay(obj){
    var approveCheckBox = document.getElementsByName('approveCheckBox');
    var payMethod = document.getElementsByName('paymethod');
    var rowIndex = obj.parentNode.parentNode.rowIndex;
    rowIndex--;
    if(!obj.checked){
	approveCheckBox[rowIndex].checked = false;
	document.getElementById("approveCheckBoxdiv"+rowIndex).style.display="none";
    }else{
	if(trim(payMethod[rowIndex].value)!='CHECK'){
	    document.getElementById("approveCheckBoxdiv"+rowIndex).style.display="block";
	}
    }
}

function showDetails(transactionId){
    dwr.util.setValue("printerMessage","");
    var canEdit = dwr.util.getValue("canEdit");
    APPaymentBC.getPaymentsByTransactionId(transactionId,canEdit,function(data){
	if(data!=null && data!=""){
	    showPopUp();
	    var payListDiv = createHTMLElement("div","payListDiv","60%","224px",document.body);
	    dwr.util.setValue("payListDiv", data, {
		escapeHtml:false
	    });
	    jQuery("#vendorDetails").tablesorter({
		widgets: ['zebra']
	    });
	    floatDiv("payListDiv", document.body.offsetWidth/5,document.body.offsetHeight/4).floatIt();
	}else if(data!=null && data==messageLoginError){
	    dwr.util.setValue("errorMessage",data);
	    setTimeout(redirectToLoginPage(),5000);
	}
    });
}

function redirectToLoginPage(){
    window.location = path+"/jsps/login.jsp";
}

function closeDetails(){
    showAll();
}
function removePayment(removedId,transactionIds){
    dwr.util.setValue("printerMessage","");
    APPaymentBC.removePayment(removedId,transactionIds,function(data){
	if(data!=null && data!=messageSuccess && data!=messageLoginError){
	    dwr.util.setValue("payListDiv",data, {
		escapeHtml:false
	    });
	    showPopUp();
	}else if(data!=null && data==messageLoginError){
	    document.body.removeChild(document.getElementById("payListDiv"));
	    closePopUp();
	    dwr.util.setValue("errorMessage",data);
	    setTimeout(redirectToLoginPage(),5000);
	}else if(data!=null && data==messageSuccess){
	    closeDetails();
	}
    });
}


function getBankAccounts(){
    var bankName = jQuery("#bankName").val();
    jQuery("#startingNumber").val("");
    jQuery("#startingNumberLabel").html("");
    jQuery("#bankAccountNumber").empty();
    if(jQuery.trim(bankName)!=""){
	GeneralLedgerDwr.getBankAccounts(bankName,function(bankAccounts){
	    if(bankAccounts.length>1){
		var option = "<option value=''>Select Bank Account</option>";
		jQuery("#bankAccountNumber").append(option);
	    }
	    jQuery.each(bankAccounts, function(index){
		var option = "<option value='" + bankAccounts[index].bankAcctNo + "'>";
		option+=(jQuery.trim(bankAccounts[index].acctName)!=""?bankAccounts[index].acctName:bankAccounts[index].bankAcctNo)+ "</option>";
		jQuery("#bankAccountNumber").append(option);
	    });
	    setStartingNumber();
	});
    }
}

function setStartingNumber() {
    var bankName = jQuery("#bankName").val();
    var bankAcctNo = jQuery("#bankAccountNumber").val();
    jQuery("#startingNumber").val("");
    jQuery("#startingNumberLabel").html("");
    if(jQuery.trim(bankName)!="" && jQuery.trim(bankAcctNo)!=""){
        GeneralLedgerDwr.getStartingNumber(bankName,bankAcctNo,function(startingNumber){
	    jQuery("#startingNumber").val(startingNumber);
	    jQuery("#startingNumberLabel").html(startingNumber);
	});
    }
}

function changeAllDateFields(obj){
    if(trim(obj.value)!=""){
	GeneralLedgerDwr.isPeriodOpenForThisDate(obj.value, function(data){
	    if(data==false){
		alert("Please select another period, this period is not yet open or closed");
		obj.value="";
	    }else{
		var bankAccount = document.aPPaymentForm.bankAccountNumber.value;
		ArDwr.isNotReconciledDate(obj.value,bankAccount,function(result){
		    if(result=="available"){
			var paymentDate = document.getElementsByName('paymentDate');
			for(var j=0;j<paymentDate.length;j++){
			    paymentDate[j].value=obj.value;
			}
		    }else{
			alert(result);
			obj.value="";
		    }
		});
	    }
	});
    }
}

function makePayments(){
    dwr.util.setValue("printerMessage","");
    var payCheckBox = document.getElementsByName('payCheckBox');
    var approveCheckBox = document.getElementsByName("approveCheckBox");
    var payMethod = document.getElementsByName('paymethod');
    var idsForPayment = "";
    var idsForApproval = "";
    var idsForUnCheckPay = "";
    var paymentMethod = "";
    var txtCommonPaymentDate = document.getElementById('txtCommonPaymentDate');
    var paymentDate = document.getElementsByName('paymentDate');
    var makePayment = false;
    var noCheckPay = true;
    var otherPaymentMethod = false;
    for(var j=0;j<payCheckBox.length;j++) {
	if(payCheckBox[j].checked && !payCheckBox[j].disabled) {
	    selectedDaymentDate=paymentDate
	    if(trim(idsForPayment)!=""){
		idsForPayment=idsForPayment+"<-->"+payCheckBox[j].value;
	    }else{
		idsForPayment=payCheckBox[j].value;
	    }
	    if(trim(payMethod[j].value)=='CHECK'){
		noCheckPay = false;
	    }else if(trim(payMethod[j].value)=='ACH DEBIT' || trim(payMethod[j].value)=='CREDIT CARD'){
		otherPaymentMethod  =true;
	    }
	    if(trim(paymentMethod)!=""){
		paymentMethod=paymentMethod+"<-->"+payMethod[j].value;
	    }else{
		paymentMethod=payMethod[j].value;
	    }
	    if(txtCommonPaymentDate.value=="" || txtCommonPaymentDate.value==null){
		txtCommonPaymentDate.value = paymentDate[j].value;
	    }
	}else if((!payCheckBox[j].checked && !payCheckBox[j].disabled) && payMethod[j].value!='CHECK'){
	    if(trim(idsForUnCheckPay)!=""){
		idsForUnCheckPay = idsForUnCheckPay+","+payCheckBox[j].value;
	    }else{
		idsForUnCheckPay = payCheckBox[j].value;
	    }
	}
    }
    document.aPPaymentForm.idsForPayment.value=idsForPayment;
    var hasApproved = false;
    if(document.aPPaymentForm.approveAll){
	for(var j=0;j<approveCheckBox.length;j++) {
	    if(payCheckBox[j].checked && approveCheckBox[j].checked) {
		if(trim(idsForApproval)!=""){
		    idsForApproval=idsForApproval+"<-->"+approveCheckBox[j].value;
		}else{
		    idsForApproval=approveCheckBox[j].value;
		}
		hasApproved = true;
	    }else if(payCheckBox[j].checked){
		if(trim(idsForApproval)!=""){
		    idsForApproval=idsForApproval+"<-->"+"notApproved";
		}
		else{
		    idsForApproval="notApproved";
		}
	    }
	}
	document.aPPaymentForm.idsForApproval.value=idsForApproval;
    }
    if(trim(idsForPayment)!="" || hasApproved){
	makePayment = true;
    }
    document.aPPaymentForm.paymentMethods.value=paymentMethod;
    var bankName = document.aPPaymentForm.bankName;
    var bankAccount = document.aPPaymentForm.bankAccountNumber;
    document.aPPaymentForm.idsForUnCheckPay.value = idsForUnCheckPay;
    if(!makePayment && trim(idsForUnCheckPay)!=""){
        window.parent.showPreloading();
	document.aPPaymentForm.button.value="undoPayment";
	document.aPPaymentForm.submit();
    }else if(!makePayment){
	alert("Please select atleast one Transaction, To Make Payment");
	return false;
    }else{
	if(null==bankName || undefined == bankName || null==bankAccount || undefined == bankAccount){
	    alert("You did not having any bank account. Please add bank account");
	    return false;
	}
	if(bankName.value == "") {
	    alert("Please Select Bank");
	    bankName.focus();
	    return false;
	}else if(bankAccount.value == "") {
	    alert("Please Select Bank Account");
	    bankAccount.focus();
	    return false;
	}else{
	    ArDwr.isNotReconciledDate(txtCommonPaymentDate.value,bankAccount.value,function(result){
		if(result=="available"){
		    if(noCheckPay && !otherPaymentMethod && (trim(idsForApproval)=="" || !hasApproved)){
                        window.parent.showPreloading();
			document.aPPaymentForm.button.value='makePayment';
			document.aPPaymentForm.submit();
		    }else if(noCheckPay && hasApproved){
			this.doPayments();
		    }else if(noCheckPay && otherPaymentMethod){
			this.doPayments();
		    }else {
			APPaymentBC.validatePrinters(bankName.value,bankAccount.value,function(data){
			    if(data!=null && data=="success"){
				this.doPayments();
			    }else if(data!=null && data=="checkPrinterError"){
				alert("There is no Check Printer available for this Bank Account");
				return false;
			    }else if(data!=null && data=="overflowPrinterError"){
				if(confirm("There is no overflow Printer available for this Bank Account. It won't print overflow checks. Do you want to Continue?")){
				    this.doPayments();
				}else{
				    return false;
				}
			    }
			});
		    }
		}else{
		    alert(result);
		    txtCommonPaymentDate.focus();
		}
	    });
	}
    }
}

function doPayments(){
    APPaymentBC.setBatchIdForPayment(function(data){
	showPopUp();
	var batchDiv = createHTMLElement("div","batchDiv","380px","150px",document.body);
	dwr.util.setValue("batchDiv", data, {
	    escapeHtml:false
	});
	floatDiv("batchDiv", document.body.offsetWidth/4,document.body.offsetHeight/4).floatIt();
    });
}

function closeBatch(apBatchId){
    APPaymentBC.removeBatchIdFromPayment(apBatchId,function(data){
	document.body.removeChild(document.getElementById("batchDiv"));
	closePopUp();
    });
}

function saveBatchAndMakePayments(batchId){
    document.getElementById("searchButton").disabled= true;
    document.getElementById("clearButton").disabled= true;
    document.getElementById("makePaymentButton").disabled= true;
    dwr.util.setValue("printerMessage","");
    var batchDescription = document.getElementById('Description').value;
    document.aPPaymentForm.batchId.value=batchId;
    document.aPPaymentForm.batchDescription.value=batchDescription;
    document.aPPaymentForm.button.value='makePayment';
    window.parent.showPreloading();
    document.aPPaymentForm.submit();
    document.body.removeChild(document.getElementById("batchDiv"));
    closePopUp();
}

if(document.getElementById("vendor")){
    AjaxAutocompleter("vendor", "custname_choices","vendorNumber", "custname_check", rootPath+"/servlet/AutoCompleterServlet?action=Vendor&tabName=APPAYMENT&textFieldId=vendor","searchResults()","");
}