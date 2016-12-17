function openPaymentRelease(){
    clearPaymentRelease();
    document.getElementById("paymentReleaseDiv").style.display ="block";
}
function closePaymentRelease(){
   var overPaid = document.paymentReleaseForm.overPaid.value;
   parent.parent.GB_hide();
   parent.parent.closeImportReleasePopup(overPaid);
}
function savePaymentRelease(){
    var paymentReleaseOn = document.paymentReleaseForm.releasedOn.value;
    var paymentReleaseComment = document.paymentReleaseForm.comment.value;
    var bolId = document.paymentReleaseForm.bolId.value;
    var importReleaseOn = document.paymentReleaseForm.importReleaseOn.value;
    var importReleaseComment = document.paymentReleaseForm.importReleaseComments.value;
    var expressReleaseOn = document.paymentReleaseForm.expressReleasedOn.value;
    var expressReleaseComment = document.paymentReleaseForm.expressReleaseComment.value;
    var deliveryOrderOn = document.paymentReleaseForm.deliveryOrderOn.value;
    var customsClearanceOn = document.paymentReleaseForm.customsClearanceOn.value;
    var customsClearanceComment = document.paymentReleaseForm.customsClearanceComment.value;
    var deliveryOrderComment = document.paymentReleaseForm.deliveryOrderComment.value;
    var paymentRelease = '';
    var importRelease = '';
    var expressRelease = '';
    var deliveryOrder = '';
    var customsClearance = '';
    if(document.paymentReleaseForm.importRelease[0].checked){
        importRelease = 'Y';
    }else{
        importRelease = 'N';
    }
    if(document.paymentReleaseForm.paymentRelease[0].checked){
        paymentRelease = 'Y';
    }else{
        paymentRelease = 'N';
    }
    if(document.paymentReleaseForm.expressRelease[0].checked){
        expressRelease = 'Y';
    }else {
        expressRelease = 'N';
    }
    if(document.paymentReleaseForm.deliveryOrder[0].checked){
        deliveryOrder = 'Y';
    } else {
        deliveryOrder = 'N';
    }
    if(document.paymentReleaseForm.customsClearance[0].checked){
        customsClearance = 'Y';
    }else {
        customsClearance = 'N';
    }
    var date = new Date();
    var month=getMonthName(date.getMonth());
    var time=getCurrentTime();
    var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear();
    var overPaid = document.paymentReleaseForm.overPaid.value;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "updateImportRelease",       
            param1: bolId,
            param2: importRelease,
            param3: importReleaseOn,
            param4: importReleaseComment,
            param5: paymentRelease,
            param6: paymentReleaseOn,
            param7: paymentReleaseComment,
            param8: expressRelease,
            param9: expressReleaseOn,
            param10: expressReleaseComment,
            param11: deliveryOrder,
            param12: deliveryOrderOn,
            param13: deliveryOrderComment,
            param14: customsClearance,
            param15: customsClearanceOn,
            param16: customsClearanceComment,
            request: true
        },
        success: function (data) {
            if(null != data){
                parent.parent.changeImportReleaseColor(paymentRelease,importRelease,data,currentDate,overPaid);
                parent.parent.GB_hide();
            }
        }
    });
}
function cancelPaymentRelease(){
    document.getElementById("paymentReleaseDiv").style.display ="none";
    disablePayment();
}
function checkImportRelease(obj){
    if(obj.id == 'importReleaseYes'){
        var date = new Date();
        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear()+" "+getCurrentTime();
        document.paymentReleaseForm.importReleaseOn.value=currentDate;
        document.getElementById("importCommentsId").className ="textlabelsBoldForTextBox";
        document.getElementById("importCommentsId").readOnly = false;
        document.getElementById("importCommentsId").tabIndex = 0;
    }else{
        document.paymentReleaseForm.importReleaseOn.value='';
        document.paymentReleaseForm.importReleaseComments.value='';
        document.getElementById("importCommentsId").className ="BackgrndColorForTextBox";
        document.getElementById("importCommentsId").readOnly = true;
        document.getElementById("importCommentsId").tabIndex = -1;
    }
}
function checkPaymentRelease(obj){
    if(obj.id == 'paymentReleaseYes'){
        var date = new Date();
        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear()+" "+getCurrentTime();
        document.paymentReleaseForm.releasedOn.value=currentDate;
        enablePayment();
    }else{
        document.paymentReleaseForm.releasedOn.value='';
        document.paymentReleaseForm.comment.value='';
        disablePayment();
}
}
function disablePayment(){
    document.getElementById("paymentReleaseComments").className ="BackgrndColorForTextBox";
    document.getElementById("paymentReleaseComments").readOnly = true;
    document.getElementById("paymentReleaseComments").tabIndex = -1;
}
function enablePayment(){
    document.getElementById("paymentReleaseComments").className ="textlabelsBoldForTextBox";
    document.getElementById("paymentReleaseComments").readOnly = false;
    document.getElementById("paymentReleaseComments").tabIndex = 0;
}
function addPaymentRelease(obj){
    var amount = document.paymentReleaseForm.amount.value;
    if(amount == '' || amount == '0.00' || amount == '0.0'){
        alertNew("Please Enter Amout");
        return;
    }
    document.paymentReleaseForm.action.value = obj.value;
    document.paymentReleaseForm.submit();
}
function deletePaymentRelease(id){
    document.paymentReleaseForm.id.value = id;
    confirmYesOrNo("Are you sure you want to delete the Payment","deletePayment");
}
function confirmMessageFunction(id1,id2){
    if(id1=='deletePayment' && id2=='yes'){
        document.paymentReleaseForm.action.value = "Delete";
        document.paymentReleaseForm.submit();
    }
}
function editPaymentRelease(id,checkNumber,amount,paidDate,paidBy){
    enablePayment();
    document.getElementById("paymentReleaseDiv").style.display ="block";
    document.getElementById("checkNumber").value =checkNumber;
    document.getElementById("paymentAmount").value =amount;
    document.getElementById("paidBy").value =paidBy;
    document.getElementById("txtpaidDateCal").value =paidDate;
    document.getElementById("id").value =id;
    document.getElementById("savePayment").value ="Update";
}
function clearPaymentRelease(){
    document.getElementById("checkNumber").value ='';
    document.getElementById("paymentAmount").value ='';
    document.getElementById("paidBy").value ='';
    document.getElementById("txtpaidDateCal").value ='';
    document.getElementById("id").value ='';
    document.getElementById("savePayment").value ="Save";
}
function checkExpressRelease(obj){
    if(obj.id == 'expressReleaseYes'){
        var date = new Date();
        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" +date.getFullYear() + " "+getCurrentTime();
        document.paymentReleaseForm.expressReleasedOn.value=currentDate;
        document.getElementById("expressReleaseComment").className ="textlabelsBoldForTextBox";
        document.getElementById("expressReleaseComment").readOnly = false;
        document.getElementById("expressReleaseComment").tabIndex = 0;
    } else {
        document.paymentReleaseForm.expressReleasedOn.value='';
        document.paymentReleaseForm.expressReleaseComment.value='';
        document.getElementById("expressReleaseComment").className ="BackgrndColorForTextBox";
        document.getElementById("expressReleaseComment").readOnly = true;
        document.getElementById("expressReleaseComment").tabIndex = -1;
    }
}
function checkdeliveryOrder(obj) {
   if (obj.id == 'deliveryOrderYes') {
        var date = new Date();
        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear() + " " + getCurrentTime();
        document.paymentReleaseForm.deliveryOrderOn.value = currentDate;
        document.getElementById("deliveryOrderComment").className = "textlabelsBoldForTextBox";
        document.getElementById("deliveryOrderComment").readOnly = false;
        document.getElementById("deliveryOrderComment").tabIndex = 0;
    } else {
        document.paymentReleaseForm.deliveryOrderOn.value = '';
        document.paymentReleaseForm.deliveryOrderComment.value = '';
        document.getElementById("deliveryOrderComment").className = "BackgrndColorForTextBox";
        document.getElementById("deliveryOrderComment").readOnly = true;
        document.getElementById("deliveryOrderComment").tabIndex = -1;
    }
}
function checkcustomsClearance(obj) {
    if (obj.id == 'customsClearanceYes') {
        var date = new Date();
        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear() + " " + getCurrentTime();
        document.paymentReleaseForm.customsClearanceOn.value = currentDate;
        document.getElementById("customsClearanceComment").className = "textlabelsBoldForTextBox";
        document.getElementById("customsClearanceComment").readOnly = false;
        document.getElementById("customsClearanceComment").tabIndex = 0;
    } else {
        document.paymentReleaseForm.customsClearanceOn.value = '';
        document.paymentReleaseForm.customsClearanceComment.value = '';
        document.getElementById("customsClearanceComment").className = "BackgrndColorForTextBox";
        document.getElementById("customsClearanceComment").readOnly = true;
        document.getElementById("customsClearanceComment").tabIndex = -1;
    }
}