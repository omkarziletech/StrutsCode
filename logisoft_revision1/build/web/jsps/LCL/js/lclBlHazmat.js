$(document).ready(function(){
    var dispo = parent.$("#exportDisposition").val();
     if ($("#freeFormValues").val() !== "") {
        $("#freeForm").removeClass("button-style1");
        $("#freeForm").addClass("green-background");
    }
     if (dispo === 'B') {
        $("#outerPkgNoPieces").removeClass("mandatory");
        $("#outerPkgType").removeClass("mandatory");
        $("#outerPkgComposition").removeClass("mandatory");
        $("#totalGrossWeight").removeClass("mandatory");
    }
    $('#saveHazmat').click(function(){
        var error = true;
        $(".required").each(function(){
            if($(this).val().length == 0)
            {
                sampleAlert('This field is required');
                error = false;
                $(this).css("border-color","red");
                $(this).focus();
                return false;
            }
        });
        if(error){
            if($("#unHazmatNo").val()== null || $("#unHazmatNo").val()== "") {
                sampleAlert('UN# is required');
                return false;
            }
            if($("#properShippingName").val()== null || $("#properShippingName").val()== "") {
                sampleAlert('Proper Shipping Name is required');
                return false;
            }
            if($("#hazClass").val()== null || $("#hazClass").val()== "") {
                sampleAlert('Please select Class');
                return false;
            }
           /* if($("#outerPkgNoPieces").val()== null || $("#outerPkgNoPieces").val()== "") {
                $.prompt('Please select Outer Packaging Pieces');
                return false;
            }
             if($("#outerPkgType").val()== null || $("#outerPkgType").val()== "") {
                $.prompt('Please select Outer Packaging Type');
                return false;
            }
            if($("#outerPkgComposition").val()== null || $("#outerPkgComposition").val()== "") {
                $.prompt('Please select Outer Pkg Composition');
                return false;
            }
             if($("#totalGrossWeight").val()== null || $("#totalGrossWeight").val()== "") {
                $.prompt('Please select Total Gross Weight/Kgs');
                return false;
            }*/
            else{
                submitHazmatForm('addBlHazmat','#lclBlHazmatForm','#commodityDesc');
            }
        }
    });
});

$(document).ready(function () {
    if (($('#methodName').val() === 'display') || ($('#methodName').val() === 'addBlHazmat') || ($('#methodName').val() === 'delete')){ 
        $('#addHazmat').show();
        $('#hazmatsection').hide();
        $('#saveHazmat').hide();
    } else {  //open hazmat window
        $('#addHazmat').hide();
        $('#saveHazmat').show();
        $('#hazmatsection').show();
    }
});
function submitHazmatForm(methodName,formName,selector){
    $("#methodName").val(methodName);
    $(formName).submit();
}

function submitForm(methodName){
    $("#methodName").val(methodName);
    alert(methodName)
    var params = $('#lclHazmatForm').serialize();
    $.post($('#lclHazmatForm').attr("action"),params,
        function(data) {
            alert(data)
            $('#commodityDesc').html(data);
            $('#commodityDesc',window.parent.document).html(data);
            parent.$.fn.colorbox.close();
        });
}

/* script for alert */
function sampleAlert(txt){
    $.prompt(txt);
}
function addBlHazmat(){
    showBlock('#hazmatTable');
}
function showBlock(tar){
    $(tar).show(300);
}
function checkForNumberAndDecimal(obj){
    var result;
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value="";
        sampleAlert("This should be Numeric");
    }
}
function closeNotes(id,methodName){
    $("#methodName").val(methodName);
    $("#id").val(id);
    $("#lclBlHazmatForm").submit();
}
function editBlHazmat(id,unHazmatNo,innerPkgNwtPiece,outerPkgNoPieces,liquidVolume,exceptedQuantity,emsCode){
    $("#id").val(id);
    $("#unHazmatNo").val(unHazmatNo);
    $("#innerPkgNwtPiece").val(innerPkgNwtPiece);
    $("#outerPkgNoPieces").val(outerPkgNoPieces);
    $("#liquidVolume").val(liquidVolume);
    $("#exceptedQuantity").val(exceptedQuantity);
    $("#emsCode").val(emsCode);
    showBlock('#hazmatTable');
}

function editHazmatRecord(path,id,blPieceId,fileNumber,commodityNo){
    var fileNumberId = $('#fileNumberId').val();
    var fileNumber= $('#fileNumber').val();
    var commodityId = $('#commodityId').val();
    var commodityNo = $('#commodityNo').val();
    var href = path+"/lclBlHazmat.do?methodName=edit&id="+id+"&blPieceId="+blPieceId+"&fileNumberId="+fileNumberId+"&commodityId="+commodityId+"&fileNumber="+fileNumber+"&commodityNo="+commodityNo;
    window.location =href;
}

//function addHazmatRecord(path,blPieceId,fileNumber,commodityNo){
//
//    var fileNumberId = $('#fileNumberId').val();
//
//    var fileNumber= $('#fileNumber').val();
//    var commodityId = $('#commodityId').val();
//    var commodityNo = $('#commodityNo').val();
//    var href = path+"/lclBlHazmat.do?methodName=add&blPieceId="+blPieceId+"&fileNumberId="+fileNumberId+"&commodityId="+commodityId+"&fileNumber="+fileNumber+"&commodityNo="+commodityNo;
//    window.location =href;
//}
function addHazmatRecord() {
    $('#hazmatId').val('');
    $('#unHazmatNo').val('');
    $('#properShippingName').val('');
    $('#hazClass').val('');
    $('#packingGroupCode').val('');
    $('#flashPoint').val('');
    $('#outerPkgNoPieces').val('');
    $('#outerPkgType').val('');
    $('#outerPkgComposition').val('');
    $('#totalNetWeight').val('');
    $('#liquidVolume').val('');
    $('#totalGrossWeight').val('');
    $('#technicalName').val('');
    $('#imoPriSubClassCode').val('');
    $('#imoSecSubClassCode').val('');
    $('#innerPkgNoPieces').val('');
    $('#innerPkgType').val('');
    $('#innerPkgComposition').val('');
    $('#innerPkgUom').val('');
    $('#innerPkgNwtPiece').val('');
    $('#emergencyContact').val('');
    $('#emergencyPhone').val('');
    $('#emsCode').val('');
    $('#reportableQuantityY').val('');
    $('#marinePollutantY').val('');
    $('#exceptedQuantityY').val('');   
    $('#limitedQuantityY').val('');   
    $('#residueY').val('');   
    $('#inhalationHazardY').val('');   
 
    $('#hazmatsection').show();
    $('#addHazmat').hide();
    $('#saveHazmat').show();
}

function deleteHazmat(path,id,blPieceId,fileNumber,commodityNo){
    var txt = 'Are you sure you want to delete?';
    $.prompt(txt,{
        buttons:{
            Yes:1,
            No:2
        },
        submit:function(v){
            if(v==1){
                showProgressBar();
                var fileNumberId = $('#fileNumberId').val();
                var fileNumber = $('#fileNumber').val();
                var commodityId = $('#commodityId').val();
                var commodityNo = $('#commodityNo').val();
                var href = path+"/lclBlHazmat.do?methodName=delete&id="+id+"&blPieceId="+blPieceId+"&fileNumberId="+fileNumberId+"&commodityId="+commodityId+"&fileNumber="+fileNumber+"&commodityNo="+commodityNo;
                window.location =href;
            }
            else if(v==2){
                $.prompt.close();
            }
        }
    });
}

function closeHazmat(id,bookingPieceId){
    parent.$.fn.colorbox.close();
    // showProgressBar();
//    $("#methodName").val("closeHazmat");
//    var params = $('#lclBlHazmatForm').serialize();
//    params+="&id="+id+"&bookingPieceId="+bookingPieceId;
//    $.post($('#lclBlHazmatForm').attr("action"),params,
//        function(data) {
//            $('#commodityDesc').html(data);
//            $('#commodityDesc',window.parent.document).html(data);
//            hideProgressBar();
//        });
}