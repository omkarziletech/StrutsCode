/*
 *  Document   : eculineInvoice
 *  Author     : Rajesh
 */

$(document).ready(function() {
    $("[title != '']").not("link").tooltip();
});

function showTabs() {
    $(".tab-content").hide();
    var rowIndex = $("#rowIndex").val();
    var tabToChoose = rowIndex === '' ? 1 : rowIndex;
    $("ul.tabs li:nth-child(" + tabToChoose + ")").addClass("active").show();
    $(".tab-content:nth-child(" + tabToChoose + ")").show();

    $("ul.tabs li").click(function() {
        $("ul.tabs li").removeClass("active");
        $(this).addClass("active");
        $(".tab-content").hide();

        var activeTab = $(this).find("a").attr("href");
        $(activeTab).fadeIn();
        return false;
    });
}

function getVoyageDetails(path, id, hblNo) {
    window.parent.showLoading();
    var url = path + "/lclEculineEdi.do?methodName=getVoyageDetails&id=" + id + "&hblNo=" + hblNo + "&isInvoiceReq=yes";
    document.location.href = url;
}
function dispute(id) {
    var $newAmt = $("#new-amt" + id);
    if ($("#dispute" + id).is(":checked")) {
        $newAmt.removeClass("readonly");
        $newAmt.attr("readonly", false);
        $newAmt.css({
            "border": "1px solid",
            "background": "white"
        });
        $newAmt.focus();
    } else {
        $newAmt.val("0.00");
        $newAmt.attr("readonly", true);
        $newAmt.css({
            "border": "0px none",
            "background": "transparent"
        });
    }
}

function approveCharge(chargeId, index) {
    window.parent.showLoading();
    $("#methodName").val("approveCharge");
    $("#chargeId").val(chargeId);
    $("#rowIndex").val(index);
    $("#invoiceForm").submit();
}

function validateUpdateCharge(index) {
    if ($("#chargeCode" + index).val() === '') {
        $.prompt("Please select Charge Code before save the charges.");
        $("#chargeCode" + index).addClass("error-indicator");
        $("#warning").parent.show();
        return false;
    } else if ($("#costCode" + index).val() === '') {
        $.prompt("Please select Cost Code before save the charges.");
        $("#costCode" + index).addClass("error-indicator");
        $("#warning").parent.show();
        return false;
    }
    return true;
}
function changeBillToParty(index) {
    var billToParty = $('#hiddenBillToParty'+index).val();
    $("#billToParty" + index).val(billToParty);
}
function updateCharge(chargeId, index, methodToInvoke, invoiceNo) {
    if (validateUpdateCharge(index)) {
        window.parent.showLoading();
        if ($('#dispute' + index).is(":checked")) {
            $('#dispute').val("yes");
        } else {
            $('#dispute').val('');
        }
        $("#arGlId").val($("#chargeArGlId" + index).val());
        $("#apGlId").val($("#chargeApGlId" + index).val());
        $("#eculineChargedesc").val($("#charge-desc" + index).val());
        $('#arAmount').val($("#arAmount" + index).val());
        $('#chargeAmount').val($("#arAmount" + index).val());
        $('#apAmount').val($("#apAmount" + index).val());
        $('#costAmount').val($("#apAmount" + index).val());
        $('#chargeBillToParty').val($("#billToParty" + index).val());
        $('#price').val($("#pricel" + index).val());
        $('#blueChargeCode').val($("#chargeBlueCode" + index).val());
        $('#blueCostCode').val($("#costBlueCode" + index).val());
        $('#isPosted').val($("#isPosted" + index).val());
        $("#chargeId").val(chargeId);
        $("#rowIndex").val(index.substring(0, 1));
        $("#methodName").val(methodToInvoke);
        $("#invoiceNo").val(invoiceNo);
        $("#invoiceForm").submit();
    }
}
//submit on charge
function submitCharge(chargeId, index, methodToInvoke,fileContainChargesFlag) {
    if (validateUpdateCharge(index)) {
        if ($('#dispute' + index).is(":checked")) {
            $('#dispute').val("yes");
        } else {
            $('#dispute').val('');
        }
        $("#arGlId").val($("#chargeArGlId" + index).val());
        $("#apGlId").val($("#chargeApGlId" + index).val());
        $("#eculineChargedesc").val($("#charge-desc" + index).val());
        $('#arAmount').val($("#arAmount" + index).val());
        $('#chargeAmount').val($("#arAmount" + index).val());
        $('#apAmount').val($("#apAmount" + index).val());
        $('#costAmount').val($("#apAmount" + index).val());
        $('#chargeBillToParty').val($("#billToParty" + index).val());
        $('#price').val($("#pricel" + index).val());
        $('#blueChargeCode').val($("#chargeBlueCode" + index).val());
        $('#blueCostCode').val($("#costBlueCode" + index).val());
        $("#chargeId").val(chargeId);
        $("#rowIndex").val(index.substring(0, 1));
        if(fileContainChargesFlag==='true'){
            var msg = "Charges or Costs already exists on DR ,Do you still want to add ?";
            $.prompt(msg, {
                buttons: {
                    Yes: true,
                    Cancel: false
                },
                submit: function(v) {
                    if (v) { //add
                          if(!checkacctType(index)){
                            return false;
                           }
                        showLoading();
                        $('#isPosted').val("P");
                        $("#chargeId").val(chargeId);
                        $("#rowIndex").val(index.substring(0, 1));
                        $("#methodName").val(methodToInvoke);
                        $("#invoiceForm").submit();
                    } else { //do nothing
                        $("#submit"+index).attr("checked", false);
                        return;
                    }
                }
            });
        }else{
            if(!checkacctType(index)){
                return false;
            }
            showLoading();
            $('#isPosted').val("P");
            $("#chargeId").val(chargeId);
            $("#rowIndex").val(index.substring(0, 1));
            $("#methodName").val(methodToInvoke);
            $("#invoiceForm").submit();
        }
    }
}
function checkacctType(index){
    var fileId = $("#fileNumberId").val();
    var acctType=$("#billToParty"+index).val();
    var flag=true;
        if(acctType!==""){
         jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "checkAccountTypeForEculine",
                        param1:fileId,
                        param2: acctType,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        if(!data){
                            $.prompt("Please Enter "+ $('#hiddenBillToParty'+index).find(":selected").text() +" Account No");
                          flag=false;
                        }
                    }
                    });
           return flag;
}else{
    $.prompt("Please Enter  Account Type");
}
}


//submit all charge in invoice 
function postInvoice(invoiceNo, index) {
    window.parent.showLoading();
    $("#methodName").val("postInvoice");
    $("#invoiceNo").val(invoiceNo);
    $("#rowIndex").val(index);
    $("#invoiceForm").submit();
}
function fillChargeCode(id) {
    var $desc = $("#charge-desc" + id);
    var $code = $("#chargeCode" + id);
    var charge_code = $code.val();
    var charge_desc = $desc.val();
    $desc.val(charge_desc);
    $code.val(charge_code);
}
function openNotes(path, invoiceNo) {
    var url = path + "/lclEculineInvoice.do?methodName=openNotes&invoiceNo=" + invoiceNo;
    var titleText = "<span class='font-trebuchet black'>Notes for Invoice #";
    titleText += "</span><span class='red'>" + invoiceNo + "</span>";
    $(".notes").attr("href", url);
    $(".notes").colorbox({
        iframe: true,
        width: "60%",
        height: "62%",
        title: titleText
    });
}
function changeChargeAmt(index){
    $("#arAmount" + index).removeAttr("readonly");
    $("#arAmount" + index).removeClass("text-readonly");
}
function changeCostAmt(index){
    $("#apAmount" + index).removeAttr("readonly");
    $("#apAmount" + index).removeClass("text-readonly");
}
function openCodeMapping(path, id) {
    var href = path + "/lclEculineInvoice.do?methodName=openCodeMapping";
    $.colorbox({
        iframe: true,
        href: href,
        width: "60%",
        height: "95%",
        title: "Open Code Mapping",
        onClosed: function () {
            var cntrId = $('#containerId').val();
            var bol = $('#blNo').val();
            var fileNumberId = $('#fileNumberId').val();
            showLoading();
            var url = path + "/lclEculineEdiBlInfo.do?methodName=openInvoice&bol=" + bol + "&id=" + cntrId + "&cntrId=" + cntrId + "&fileNumberId=" + fileNumberId;
            document.location.href = url;
        }
    });
}
function search(searchType) {
    showLoading();
    if (searchType === 'all') {
        $("#methodName").val('openCodeMapping');
        $("#eculineInvoiceForm").submit();
    } else {
        $("#methodName").val('searchCodeMapping');
        $("#eculineInvoiceForm").submit();
    }
}

function resetAll() {
    $("#srcChargeDesc").val("");
    $("#methodName").val('resetCodeMapping');
    $("#eculineInvoiceForm").submit();
}
function updateChargeCodeMapping( index, methodToInvoke) {
    if (validateUpdateCharge(index)) {
        showLoading();
        jQuery("#pageNo").val(jQuery("#selectedPageNo").val());
        $("#eculineChargedesc").val($("#charge-desc" + index).val());
        $('#blueChargeCode').val($("#chargeBlueCode" + index).val());
        $('#blueCostCode').val($("#costBlueCode" + index).val());
        $('#costCode').val($("#costCode" + index).val());
        $('#chargeCode').val($("#chargeCode" + index).val());
        $("#rowIndex").val(index.substring(0, 1));
        $("#methodName").val(methodToInvoke);
        $("#eculineInvoiceForm").submit();
    }
}
function updateChargeCodeMappingAll(methodToInvoke) {
    showLoading();
    $("#methodName").val(methodToInvoke);
    $("#mappingSaveAllFlag").val("true");
    $("#eculineInvoiceForm").submit();
}

function gotoPage(pageNo){
    showLoading();
    jQuery("#pageNo").val(pageNo);
    $("#methodName").val('openCodeMapping');
    $("#eculineInvoiceForm").submit();
}
function gotoSelectedPage(){
    showLoading();
    jQuery("#pageNo").val(jQuery("#selectedPageNo").val());
    $("#methodName").val('openCodeMapping');
    $("#eculineInvoiceForm").submit();
}

function validateTotalAmount(index){
    var arAmount = Number($("#arAmount"+index).val());
    var apAmount = Number($("#apAmount"+index).val());
    if(arAmount + apAmount < 0 || arAmount < 0){
        $("#arAmount"+index).val("0.00");
    }
}