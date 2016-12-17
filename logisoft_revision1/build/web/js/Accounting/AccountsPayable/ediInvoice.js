/* 
 * Document   : ediInvoice
 * Created on : May 10, 2012, 06:38:35 PM
 * Author     : Lakshmi Naryanan
 */
$(document).ready(function(){
    closePreloading();
    $("[title != '']").not("link").tooltip();
    $("form").submit(function(){
        showPreloading();
    });
    if($(".search-results").length>0){
        changeHeight();
    //$(".display-table").fixedHeaderTable({fixedColumn: true});
    }
    $(window).resize(function(){
        window.parent.changeHeight();
        if($(".search-results").length>0){
            changeHeight();
        }
    });
    $("#vendorName").initautocomplete({
        url:path+"/autocompleter/action/getAutocompleterResults.jsp?query=VENDOR&template=vendor&fieldIndex=1,2&",
        width: "420px",
        otherFields:"vendorNumber",
        callback:"search()",
        resultsClass:"ac_results z-index",
        resultPosition:"fixed",
        scroll:true,
        scrollHeight:300
    });
    
    $(document).keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == 13 && $(".jqicontainer").length==0 && $(".ac_results").css("display")=="none") {
            search();
        }
    });
});

function changeHeight(){
    var frameHeight = window.parent.getFrameHeight();
    $(".search-results").height(frameHeight-$(".search-filters").height()-110);
}

function search(){
    $("#action").val("search");
    $("#ediInvoiceForm").submit();
}

function gotoPage(pageNo){
    $("#selectedPage").val(pageNo);
    search();
}

function gotoSelectedPage(){
    $("#selectedPage").val($("#selectedPageNo").val());
    search();
}

function doSort(sortBy,orderBy){
    $("#sortBy").val(sortBy);
    $("#orderBy").val(orderBy);
    search();
}

function refresh(){
    $("#action").val("clear");
    $("#ediInvoiceForm").submit();
}

function showLogs(){
    showPreloading();
    $.post($("#ediInvoiceForm").attr("action"), {
        action: "showLogs"
    }, function(data) {
        closePreloading();
        showMask();
        $("<div style='width:70%;height:250px'></div>").html(data).addClass("popup").appendTo("body").center();
    //$("[title != '']").tooltip();
    });
}

function viewEdiFile(id){
    var height = $(document,window.parent).height();
    var width = $(document,window.parent).width()-100;
    var url = path+"/ediInvoice.do?action=viewEdiFile&id="+id;
    window.open(url,"mywindow","menubar=1,resizable=1,scrollbars=1,width="+width+",height="+height);
}

function removeLog(obj,id){
    $.prompt("Do you want to remove this file from the log?",{
        buttons:{
            Yes:true,
            No:false
        },
        callback: function(v,m,f){
            if(v){
                showPreloading();
                $.post($("#ediInvoiceForm").attr("action"), {
                    action: "removeLog",
                    id: id
                }, function(data) {
                    closePreloading();
                    showMask();
                    $(obj).parent().parent().remove();
                    $.prompt(data);
                });
            }
        }
    });
}

function hideStaticPopUp(){
    jQuery('.static-popup').hide();
    hideMask();
    hideAlternateMask();
}

function changeVendor(obj,id){
    showMask();
    $("#newVendorName").val("");
    $("#newVendorNameCheck").val("");
    $("#newVendorNumber").val("");
    $("#invoiceId").val(id);
    var pos = findPosition(obj);
    var height = obj.offsetHeight;
    $("#changeVendor").css({
        top: (pos.y + height) + "px",
        left: pos.x + "px"
    }).show();
    $("#newVendorName").unbind().initautocomplete({
        url: path+"/autocompleter/action/getAutocompleterResults.jsp?query=VENDOR&template=vendor&fieldIndex=1,2&",
        width: "450px",
        otherFields:"newVendorNumber",
        resultsClass:"ac_results z-index",
        resultPosition:"fixed",
        scroll:true,
        scrollHeight:300
    });
}

function updateVendor(){
    var id = $("#invoiceId").val();
    var vendorNumber = $("#newVendorNumber").val();
    var vendorName = $("#newVendorName").val();
    if($.trim(vendorNumber)==""){
        $.prompt("Please enter the new vendor");
    }else{
        $.prompt("Do you want to update the vendor information?",{
            buttons:{
                Yes:true,
                No:false
            },
            callback: function(v,m,f){
                if(v){
                    hideStaticPopUp();
                    showPreloading();
                    $.post($("#ediInvoiceForm").attr("action"), {
                        action: "updateVendor",
                        id: id,
                        vendorNumber: vendorNumber,
                        vendorName: vendorName
                    }, function(data) {
                        closePreloading();
                        $("#vendorName"+id).html(vendorName.length >20 ? (vendorName.substring(0,20) + "...") :vendorName);
                        $("#vendorName"+id).attr("title",vendorName);
                        $("#vendorNumber"+id).html(vendorNumber);
                        $.prompt(data);
                    });
                }
            }
        });
    }
}

function printInvoice(id){
    var title = "Edi Invoice";
    var url = path+"/ediInvoice.do?action=printInvoice&id="+id;
    window.parent.showLightBox(title, url);
}

function setShipmentType(index) {
    var row = $("#accruals tbody tr").eq(index);
    $(row.find(".costCode")).setOptions({
        extraParams: {
            param1:row.find(".shipmentType").val()
        }
    });
}

function showAccruals(id){
    showPreloading();
    $.post($("#ediInvoiceForm").attr("action"), {
        action: "showAccruals",
        id: id
    }, function(data) {
        closePreloading();
        showMask();
        $("#invoiceId").val(id);
        $("<div style='width:90%;height:250px'></div>").html(data).addClass("popup").appendTo("body").center();
        $("[title != '']").tooltip();
        $(".costCode").each(function(index){
            $(this).initautocomplete({
                url:path+"/autocompleter/action/getAutocompleterResults.jsp?query=COST_CODE&template=costCode&fieldIndex=1,2,3,5,6&",
                width: "420px",
                otherFields:"bluescreenCostCode^shipmentType^suffix^account",
                row:true,
                checkClass:"costCodeCheck",
                callBefore: "setShipmentType("+index+")",
                callback:"showOrHideTerminal("+index+")",
                resultsClass:"ac_results z-index",
                resultPosition:"fixed",
                scroll:true,
                scrollHeight:200
            });

            var row = $(this).parent().parent();
            row.find(".terminal").initautocomplete({
                url:path+"/autocompleter/action/getAutocompleterResults.jsp?query=TERMINALS&template=chargeCode&fieldIndex=1&",
                width: "250px",
                resultsClass:"ac_results z-index",
                resultPosition:"fixed",
                scroll:true,
                scrollHeight:200
            });
            row.find(".amount").keydown(function(event){
                allowOnlyNumbers(event,this);
            });
            row.find(".amount").keyup(function(event){
                validateAmount(event,this);
            });
        });
    });
}

function showOrHideTerminal(index){
    var row = $("#accruals tbody tr").eq(index);
    var suffix = $.trim(row.find(".suffix").val());
    if((suffix=="B") || (suffix=="L") || (suffix=="D")){
        row.find(".terminal").val("").removeAttr("readonly").removeClass("readonly").callFocus();
        row.find(".terminal").blur(function(){
            deriveGlAccount(index);
        });
    }else{
        row.find(".terminal").val("").removeAttr("readonly").removeClass("readonly").callFocus();
        deriveGlAccount(index);
    }
}

function deriveGlAccount(index){
    var row = $("#accruals tbody tr").eq(index);
    var suffix = $.trim(row.find(".suffix").val());
    var terminal = $.trim(row.find(".terminal").val());
    if(((suffix=="B") || (suffix=="L") || (suffix=="D")) && terminal==""){
        $.prompt("Please enter the termial number");
    }else{
        var account = row.find(".account").val();
        var shipmentType = row.find(".shipmentType").val();
        showPreloading();
        $.post($("#ediInvoiceForm").attr("action"), {
            action: "deriveGlAccount",
            account: account,
            suffix: suffix,
            shipmentType: shipmentType,
            terminal: terminal
        }, function(result) {
            closePreloading();
            showMask();
            if(result.indexOf("and suffix - ")>0){
                $.prompt(result,{
                    callback: function(){
                        row.find(".costCode").show().callFocus();
                    }
                });
            }else{
                row.find(".glAccount").val(result);
                row.find(".terminal").val(result.substr(result.lastIndexOf("-")+1)).attr("readonly",true).addClass("readonly");
            }
        });
    }
}

function updateAccrual(obj,id){
    var row = $(obj).parent().parent();
    var blNumber = $.trim(row.find(".blNumber").val());
    var containerNumber = $.trim(row.find(".containerNumber").val());
    var voyageNumber = $.trim(row.find(".voyageNumber").val());
    var dockReceipt = $.trim(row.find(".dockReceipt").val());
    var bluescreenCostCode = $.trim(row.find(".bluescreenCostCode").val());
    var costCode = $.trim(row.find(".costCode").val());
    var glAccount = $.trim(row.find(".glAccount").val());
    var shipmentType = $.trim(row.find(".shipmentType").val());
    var amount = $.trim(row.find(".amount").val());
    var suffix = $.trim(row.find(".suffix").val());
    var terminal = $.trim(row.find(".terminal").val());
    if(((suffix=="B") || (suffix=="L") || (suffix=="D")) && terminal==""){
        $.prompt("Please enter the termial number",{
            callback: function(){
                row.find(".terminal").show().callFocus();
            }
        });
    }else if(bluescreenCostCode=="" || costCode=="" || glAccount=="" || shipmentType==""){
        $.prompt("Please enter the cost code and gl account",{
            callback: function(){
                row.find(".costCode").show().callFocus();
            }
        });
    }else if(blNumber=="" && voyageNumber=="" && dockReceipt==""){
        $.prompt("Please enter either bill of ladding or voyage number or dock receipt",{
            callback: function(){
                row.find(".blNumber").show().callFocus();
            }
        });
    }else if(amount=="" || isNaN(amount)){
        $.prompt("Please enter valid amount",{
            callback: function(){
                row.find(".amount").show().callFocus();
            }
        });
    }else{
        showPreloading();
        var invoiceId = $("#invoiceId").val();
        $.post($("#ediInvoiceForm").attr("action"), {
            action: "updateAccrual",
            id: id,
            blNumber: blNumber,
            containerNumber: containerNumber,
            voyageNumber: voyageNumber,
            dockReceipt: dockReceipt,
            bluescreenCostCode: bluescreenCostCode,
            costCode: costCode,
            glAccount: glAccount,
            shipmentType: shipmentType,
            amount: amount,
            invoiceId:invoiceId,
            terminal:terminal
        }, function(data) {
            closePreloading();
            showMask();
            $.prompt(data);
        });
    }
}

function removeAccrual(obj,id){
    $.prompt("Do you want to remove this accrual from the invoice?",{
        buttons:{
            Yes:true,
            No:false
        },
        callback: function(v,m,f){
            if(v){
                showPreloading();
                var invoiceId = $("#invoiceId").val();
                $.post($("#ediInvoiceForm").attr("action"), {
                    action: "removeAccrual",
                    id: id,
                    invoiceId: invoiceId
                }, function(data) {
                    closePreloading();
                    showMask();
                    $(obj).parent().parent().remove();
                    $("#postToAp"+invoiceId).remove();
                    $("#status"+invoiceId).html("In Progress");
                    $.prompt(data);
                });
            }
        }
    });
}

function postToAp(obj,id){
    $.prompt("Do you want to post this invoice?",{
        buttons:{
            Yes:true,
            No:false
        },
        callback: function(v,m,f){
            if(v){
                showPreloading();
                $.post($("#ediInvoiceForm").attr("action"), {
                    action: "postToAp",
                    id: id
                }, function(data) {
                    closePreloading();
                    if(data=="success"){
                        $(obj).parent().parent().remove();
                        $.prompt("Invoice is Posted to AP successfully");
                    }else if(data=="EDI In Progress"){
                        $(obj).remove();
                        $.prompt("Invoice is not ready to post. Total accrual amount is not equal to invoice amount");
                        $("#status"+id).html("In Progress");
                    }else{
                        showMask();
                        $("<div style='width:90%;height:250px'></div>").html(data).addClass("popup").appendTo("body").center();
                        $("[title != '']").tooltip();
                        $("#invoiceId").val(id);
                        $(".costCode").each(function(index){
                            $(this).initautocomplete({
                                url:path+"/autocompleter/action/getAutocompleterResults.jsp?query=COST_CODE&template=costCode&fieldIndex=1,2,3,5,6&",
                                width: "420px",
                                otherFields:"bluescreenCostCode^shipmentType^suffix^account",
                                row:true,
                                checkClass:"costCodeCheck",
                                callback:"showOrHideTerminal("+index+")",
                                resultsClass:"ac_results z-index",
                                resultPosition:"fixed",
                                scroll:true,
                                scrollHeight:300
                            });
                            var row = $(this).parent().parent();
                            row.find(".amount").keydown(function(event){
                                allowOnlyNumbers(event,this);
                            });
                            row.find(".amount").keyup(function(event){
                                validateAmount(event,this);
                            });
                        });
                    }
                });
            }
        }
    });
}

function gotoAccruals(id){
    showPreloading();
    var url = path+"/accruals.do?action=fromEdiInvoice&ediInvoiceId="+id;
    url += "&fromParams=";
    url += escape("&vendorName=")+jQuery("#vendorName").val();
    url += escape("&vendorNumber=")+jQuery("#vendorNumber").val();
    url += escape("&invoiceNumber=")+encodeURIComponent(jQuery("#invoiceNumber").val());
    url += escape("&status=")+jQuery("#status").val();
    window.location = url;
}

function uploadDocument(id,invoiceNumber){
    var vendorNumber = $.trim($("#vendorNumber"+id).html());
    var documentId = vendorNumber+"-"+invoiceNumber;
    var title = "Scan/Attach";
    var url =  path+"/scan.do?screenName=INVOICE&documentId="+encodeURIComponent(documentId);
    window.parent.GB_showCenter(title,url,400,800);
}

function showNotes(id,invoiceNumber){
    var vendorNumber = $.trim($("#vendorNumber"+id).html());
    var moduleRefId = vendorNumber+"-"+invoiceNumber;
    var title = "Notes";
    var url =  path+"/notes.do?moduleId=AP_INVOICE&moduleRefId="+encodeURIComponent(moduleRefId);
    window.parent.GB_showCenter(title,url,400,800);
}

function archiveInvoice(obj,id){
    $.prompt("Do you want to archive this invoice?",{
        buttons:{
            Yes:true,
            No:false
        },
        callback: function(v,m,f){
            if(v){
                showPreloading();
                $.post($("#ediInvoiceForm").attr("action"), {
                    action: "archiveInvoice",
                    id: id
                }, function(data) {
                    closePreloading();
                    $(obj).parent().parent().remove();
                    $.prompt(data);
                });
            }
        }
    });
}

function updateEdiCode(){
    $.prompt("Do you want to update missing edi codes?",{
        buttons:{
            Yes:true,
            No:false
        },
        callback: function(v,m,f){
            if(v){
                showPreloading();
                $.post($("#ediInvoiceForm").attr("action"), {
                    action: "updateEdiCode"
                }, function() {
                    closePreloading();
                    $.prompt("EDI Codes are updated successfully");
                });
            }
        }
    });
}

function attachInvoices(){
    $.prompt("Do you want to delete the invoice attachments and reattach them again?",{
        buttons:{
            Yes:true,
            No:false
        },
        callback: function(v,m,f){
            if(v){
                showPreloading();
                $.post($("#ediInvoiceForm").attr("action"), {
                    action: "attachInvoices"
                }, function() {
                    closePreloading();
                    $.prompt("Invoice attachments reattached successfully");
                });
            }
        }
    });
}