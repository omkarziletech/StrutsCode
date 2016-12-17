/* currency format -------------------------------------------------------------------*/
// mini jQuery plugin that formats to two decimal places
(function($) {
    $.fn.currencyFormat = function(fixedTo) {
        this.each( function( i ) {
            //format the innerHTML instead of value.
            if(! isNaN( parseFloat( this.value ) ) ){
                this.value = parseFloat(this.value).toFixed(fixedTo);
            }else if(! isNaN( parseFloat( this.innerHTML ) ) ){
                this.innerHTML= parseFloat(this.innerHTML).toFixed(fixedTo);
            }
        //----------------------------------------------
        });
        return this; //for chaining
    }
    $.fn.convertToBookedWeightMet=function(selector){
        if($('#autoConvert').is(":checked")){
            if(!isNaN(parseFloat(selector.val())/2.2046)){
                $('#bookedWeightMetric').val((parseFloat(selector.val())/2.2046).toFixed(3));
            }
        }
    }
    $.fn.convertToBookedWeightImp=function(selector){
        if($('#autoConvert').is(":checked")){
            if(!isNaN(parseFloat(selector.val())*2.2046)){
                if(parent.$("#moduleName").val()=='Imports'){
                $('#bookedWeightImperial').val((parseFloat(selector.val())*2.2046).toFixed(3));
                }else{
                   $('#bookedWeightImperial').val((parseFloat(selector.val())*2.2046).toFixed(2));  
            }
            }
        }
    }
    $.fn.convertToActualWeightMet=function(selector){
        if ($('#autoConvert').is(":checked") && !$('#autoConvertMetric').is(':checked')) {
            if(!isNaN(parseFloat(selector.val())/2.2046)){
                $('#actualWeightMetric').val((parseFloat(selector.val())/2.2046).toFixed(3));
            }
        }
    }
    $.fn.convertToActulWeightImp=function(selector){
         if ($('#autoConvert').is(":checked") && !$('#autoConvertMetric').is(':checked')) {
            if(!isNaN(parseFloat(selector.val())*2.2046)){
                $('#actualWeightImperial').val((parseFloat(selector.val())*2.2046).toFixed(2));
            }
        }
    }
    $.fn.convertToBookedVolumeMet=function(selector){
        if($('#autoConvert').is(":checked")){
            if(!isNaN(parseFloat(selector.val())/35.3146)){
                $('#bookedVolumeMetric').val((parseFloat(selector.val())/35.314).toFixed(3));
            }
        }
    }
    $.fn.convertToBookedVolumeImp=function(selector){
        if($('#autoConvert').is(":checked")){
            if(!isNaN(parseFloat(selector.val())*35.3146)){
                if(parent.$("#moduleName").val()=='Imports'){
                 $('#bookedVolumeImperial').val((parseFloat(selector.val())*35.314).toFixed(3));   
                }else{
                $('#bookedVolumeImperial').val((parseFloat(selector.val())*35.314).toFixed(2));
            }
            }
        }
    }
    $.fn.convertToActualVolumeMet=function(selector){
         if ($('#autoConvert').is(":checked") && !$('#autoConvertMetric').is(':checked')) {
            if(!isNaN(parseFloat(selector.val())/35.3146)){
                $('#actualVolumeMetric').val((parseFloat(selector.val())/35.314).toFixed(3));
            }
        }
    }
    $.fn.convertToActulVolumeImp=function(selector){
        if ($('#autoConvert').is(":checked") && !$('#autoConvertMetric').is(':checked')) {
            if(!isNaN(parseFloat(selector.val())*35.3146)){
                $('#actualVolumeImperial').val((parseFloat(selector.val())*35.314).toFixed(2));
            }
        }
    }
})( jQuery );

// apply the currencyFormat behaviour to elements with 'currency' as their class
$(function() {
    $("body").delegate(".weight","change",function(){
        $(this).currencyFormat(3)
    });
// $('.weight').currencyFormat(3);
});
$(function() {
    $("body").delegate(".twoDigitDecFormat","change",function(){
        $(this).currencyFormat(2)
    });
// $('.weight').currencyFormat(3);
});
/*-------------------------------------------------------------------------------------------*/

jQuery(document).ready(function(){
    $("body").delegate("#bookedWeightImperial","change",function(){
        $(this).convertToBookedWeightMet($('#bookedWeightImperial'))
    });
    $("body").delegate("#bookedWeightMetric","change",function(){
        $(this).convertToBookedWeightImp($('#bookedWeightMetric'))
    });
    $("body").delegate("#actualWeightImperial","change",function(){
        $(this).convertToActualWeightMet($('#actualWeightImperial'))
    });
    $("body").delegate("#actualWeightMetric","change",function(){
        $(this).convertToActulWeightImp($('#actualWeightMetric'))
    });

    $("body").delegate("#bookedVolumeImperial","change",function(){
        $(this).convertToBookedVolumeMet($('#bookedVolumeImperial'))
    });
    $("body").delegate("#bookedVolumeMetric","change",function(){
        $(this).convertToBookedVolumeImp($('#bookedVolumeMetric'))
    });
    $("body").delegate("#actualVolumeImperial","change",function(){
        $(this).convertToActualVolumeMet($('#actualVolumeImperial'))
    });
    $("body").delegate("#actualVolumeMetric","change",function(){
        $(this).convertToActulVolumeImp($('#actualVolumeMetric'))
    });
});
// -- common for Qt/Bkg/Bl
//Validation for Commodity Booked Values 
function validationBookedWeightMetric(obj) {
    if (obj.value === "" && $("#autoConvert").is(':checked')) {
        $("#bookedWeightImperial").val("");
    }
    if (obj.value >= 45359.249) {
        sampleAlert('Shipment Size must be Less than 100,000 LBS');
        $("#bookedWeightImperial").val("");
        $("#bookedWeightMetric").val("");
        $("#bookedWeightMetric").css("border-color", "red");
        $(this).focus();
    }
}

function validationBookedVolumeMetric(obj) {
    if (obj.value === "" && $("#autoConvert").is(':checked')) {
        $("#bookedVolumeImperial").val("");
    }
    if (obj.value >= 2831.710) {
        sampleAlert('Shipment Size must be Less than 100,000 CFT');
        $("#bookedVolumeImperial").val("");
        $("#bookedVolumeMetric").val("");
        $("#bookedVolumeMetric").css("border-color", "red");
        $(this).focus();
    }
}
function validationBookedWeightImperial(obj) {
    if (obj.value === "" && $("#autoConvert").is(':checked')) {
        $("#bookedWeightMetric").val("");
    }
    if (obj.value >= 100000) {
        sampleAlert('Shipment Size must be Less than 100,000 LBS');
        $("#bookedWeightImperial").val("");
        $("#bookedWeightMetric").val("");
        $("#bookedWeightImperial").css("border-color", "red");
        $(this).focus();
    }
}

function validationBookedVolumeImperial(obj) {
    if (obj.value === "" && $("#autoConvert").is(':checked')) {
        $("#bookedVolumeMetric").val("");
    }
    if (obj.value >= 100000) {
        sampleAlert('Shipment Size must be Less than 100,000 CFT');
        $("#bookedVolumeImperial").val("");
        $("#bookedVolumeMetric").val("");
        $("#bookedVolumeImperial").css("border-color", "red");
        $(this).focus();
    }
}
//Validation for Commodity Actual Values
function validateActualWeightMetric(obj) {
    if (obj.value === "" && $("#autoConvert").is(':checked')) {
        $("#actualWeightImperial").val("");
    }
    if (obj.value >= 45359.249) {
        sampleAlert('Shipment Size must be Less than 100,000 LBS');
        $("#actualWeightImperial").val("");
        $("#actualWeightMetric").val("");
        $("#actualWeightMetric").css("border-color", "red");
        $(this).focus();
    }
}
function validateActualVolumeMetric(obj) {
    if (obj.value === "" && $("#autoConvert").is(':checked')) {
        $("#actualVolumeImperial").val("");
    }
    if (obj.value >= 2831.710) {
        $.prompt('Shipment Size must be Less than 100,000 CFT');
        $("#actualVolumeImperial").val("");
        $("#actualVolumeMetric").val("");
        $("#actualVolumeMetric").css("border-color", "red");
        $(this).focus();
    }
}

function validateActualWeightImperial(obj) {
    if (obj.value === "" && $("#autoConvert").is(':checked')) {
        $("#actualWeightMetric").val("");
    }
    if (obj.value >= 100000) {
        $.prompt('Shipment Size must be Less than 100,000 LBS');
        $("#actualWeightImperial").val("");
        $("#actualWeightMetric").val("");
        $("#actualWeightImperial").css("border-color", "red");
        $(this).focus();
    }
}
function validateActualVolumeImperial(obj) {
    if (obj.value === "" && $("#autoConvert").is(':checked')) {
        $("#actualVolumeMetric").val("");
    }
    if (obj.value >= 100000) {
        $.prompt('Shipment Size must be Less than 100,000 CFT');
        $("#actualVolumeImperial").val("");
        $("#actualVolumeMetric").val("");
        $("#actualVolumeImperial").css("border-color", "red");
        $(this).focus();
    }
}

