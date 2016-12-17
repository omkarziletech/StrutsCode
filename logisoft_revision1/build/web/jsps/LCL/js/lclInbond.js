$(document).ready(function () {
    $('#saveInbnd').click(function () {
        var error = true;
        $(".mandatory").each(function () {
              if ($(this).val().length === 0)
            {
                sampleAlert("This field is required");
                error = false;
                $(this).css("border-color", "red");
                $(this).focus();
                return false;
            }

        });
        if($("#moduleName").val() === 'Exports'){
        var eciBondYes = document.getElementById("eciBondYes").checked;
        var eciBondNo = document.getElementById("eciBondNo").checked; 

        if (!eciBondYes && !eciBondNo  ) {
            sampleAlert("Select ECI's Bond");
            return false;
        }
        } else if ($("#inbondNo").val() !== '' && $("#inbondType").val() === "IT" && parent.$("#fileState").val() !== 'Q'
                && parent.$("#moduleName").val() === 'Imports') {
            error = false;
            $("#methodName").val('addInbonds');
            var headerId = parent.$("#headerId").val();
            var unitId = parent.$('#unitId').val();
            $("#headerId").val(headerId);
            $("#unitId").val(unitId);
            var parmas = $("#lclInbondForm").serialize();
            $.post($("#lclInbondForm").attr("action"), parmas,
                    function (data) {
                        if (data === "true") {
                            sendInboundStatusMail();
                        } else {
                            display();
                        }
                    });
        }
        if (error) {
            submitForm('addInbonds');
        }
    });

});

function submitForm(methodName){
    showLoading();
    $("#fileState").val(parent.$("#fileState").val());
    $("#methodName").val(methodName) ;
    $("#lclInbondForm").submit();
}
function addInbond(){
    $("#id").val('');
    $("#inbondPort").val(''); 
    $("#openInbond").attr("checked",true);
    $("#closedInbond").attr("checked",false);
    $("#eciBondYes").attr("checked",false);
    $("#eciBondNo").attr("checked",false);
    $('#inbondTable').show();
}
function hideInbond(){
    $("#inbondNo").val("")
    $("#inbondType").val("")
    $("#inbondPort").val("")
    $("#inbondDatetime").val("")
    $("#inbondNo").css("border-color","");
    $("#inbondType").css("border-color","");
    $("#inbondPort").css("border-color","");
    $("#inbondDatetime").css("border-color","");
    $('#inbondTable').hide();
}
function sampleAlert(txt){
    $.prompt(txt);
}
function closeInbond(id, inbondNo){
    $("#id").val(id);
    $("#inbondNo").val(inbondNo);
}
function editInbondExport(id,inbondNo,inbondType,inbondDate,inbondPort,inbondPortId,inbondOpenClose,eciBond){
    if((inbondNo.length <= 9 ) && isNumber(inbondNo)){
        $("#inbondNo").attr('maxlength','9');
        $("#inbondType").addClass("mandatory");
        document.getElementById("allowfreetext").checked= false;
    }else{
        $("#inbondNo").attr('maxlength','25');
        $("#inbondType").removeClass("mandatory");
        document.getElementById("allowfreetext").checked= true;
    }
    $("#id").val(id);
    $("#inbondNo").val(inbondNo);
    $("#inbondType").val(inbondType);
    $("#inbondDatetime").val(inbondDate);
    $("#inbondPort").val(inbondPort);
    $("#inbondPortId").val(inbondPortId);   
    if (inbondOpenClose == 'true') {
        $("#openInbond").attr("checked",true);
    } else {
        $("#closedInbond").attr("checked",true);
    }
    if (eciBond == 'true') {
        $("#eciBondYes").attr("checked",true);
    } else {
        $("#eciBondNo").attr("checked",true);
    }     
    $('#inbondTable').show();
}
    

function editInbond(id,inbondNo,inbondType,inbondDate,inbondPort,inbondPortId){
    if((inbondNo.length <= 9 ) && isNumber(inbondNo)){
        $("#inbondNo").attr('maxlength','9');
        $("#inbondType").addClass("mandatory");
        document.getElementById("allowfreetext").checked= false;
    }else{
        $("#inbondNo").attr('maxlength','25');
        $("#inbondType").removeClass("mandatory");
        document.getElementById("allowfreetext").checked= true;
    }
    $("#id").val(id);
    $("#inbondNo").val(inbondNo);
    $("#inbondType").val(inbondType);
    $("#inbondDatetime").val(inbondDate);
    $("#inbondPort").val(inbondPort);
    $("#inbondPortId").val(inbondPortId);       
    $('#inbondTable').show();
}

function deleteInbond(txt)
{
    $.prompt(txt,{
        buttons:{
            Yes:1,
            No:2
        },
        submit:function(v){
            if(v==1){
                showProgressBar();
                $('#methodName').val('closeInbond');
                $("#lclInbondForm").submit();
                hideProgressBar();
                $.prompt.close();
            }
            else if(v==2){
                $.prompt.close();
            }
        }
    });
}

function toggle(selector) {
    $(selector).fadeToggle('slow','swing');
    $("#img-down").toggle();
    $("#img-right").toggle();
    var $msg = $("#click-msg");
    if(($msg.text().indexOf("hide")) != -1) {
        $msg.text("Click this bar to show");
    } else {
        $msg.text("Click this bar to hide");
    }
}

function isNumeric(obj){
    if(!/^\d*(\.\d{0,6})?$/.test(obj.value)){
        obj.value="";
        sampleAlert("This field should be Numeric");
    }
}

function display(status){
    $("#methodName").val('display') ;
    $("#status").val(status);
    $("#lclInbondForm").submit();
}

function sendInboundStatusMail(){
    var consigneeCode=parent.$("#consigneeCode").val();
    var shipperCode=parent.$("#shipperCode").val();
    var notifyCode=parent.$("#notifyCode").val();
    var notify2Code=parent.$('#notify2Code').val();
    var moduleName =parent.$('#moduleName').val();
    var fileNumberId=parent.$("#fileNumberId").val();
    var fileNumber=parent.$("#fileNumber").val();
    var headerId=parent.$("#headerId").val();
    var txt="Send Status Update To Customer";
    if($("#inbondNo").val()!=='' && $("#inbondType").val()==="IT"){
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit:function(av){
                if(av===1){
                    $.prompt.close();
                    if(consigneeCode==="" && shipperCode==="" && notifyCode==="" && notify2Code===""){
                        $.prompt("Select Atleast One Customer Type",{
                            butons:{
                                Ok:true
                            },
                            submit:function(v){if(v){display();}}
                        });
                    }else{
                        $("#methodName").val("validateCodeFList");
                        var params=$("#lclInbondForm").serialize();
                        params += "&consigneeCode="+consigneeCode+"&shipperCode="+shipperCode+
                        "&notifyCode="+notifyCode+"&notify2Code="+notify2Code+"&headerId="+headerId;
                        $.post($("#lclInbondForm").attr("action"), params,
                            function(data) {
                                if(data===''){
                                    var href="/logisoft/lclBooking.do?methodName=EftModifyDetails&consigneeCode="+consigneeCode+"&shipperCode="+shipperCode+
                                    "&notifyCode="+notifyCode+"&notify2Code="+notify2Code+"&headerId="+headerId+"&eftDate="+''+"&fileNumberId="+fileNumberId;
                                    $.colorbox({
                                        href: href,
                                        iframe: true,
                                        width: "95%",
                                        height: "85%",
                                        title: "INBOUND MAIL DETAILS",
                                        onClosed:function(){
                                            display("");
                                        }
                                    });
                                }else{
                                    $.prompt(data,{
                                         butons:{
                                              Ok:true
                                          },
                                        submit:function(v){if(v){display("");}}
                                         });
                                }
                            });
                    }
                }else{
                    $.prompt.close();
                    display("No");
                }
            }
        });
    }
}

