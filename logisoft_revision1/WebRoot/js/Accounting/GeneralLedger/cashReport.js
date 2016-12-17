jQuery(document).ready(function(){
    jQuery("#cashDateCalendar").insertFromCalendar({
        inputField  : "reportingDate",
        format	    : "%m/%d/%Y"
    });
});

function validateDate(){
    if(jQuery.trim(jQuery("#reportingDate").val())!="" && !isDate("#reportingDate")){
        jQuery.prompt("Please enter reporting date in mm/dd/yyyy format",{
            callback: function(){
                jQuery("#reportingDate").val("").callFocus();
            }
        });
    }
}

function gotoAction(action){
    jQuery("#action").val(action);
    if(jQuery.trim(jQuery("#reportingDate").val())==""){
        jQuery.prompt("Please enter reporting date");
        jQuery("#reportingDate").select().fadeIn().fadeOut().fadeIn();
    }else{
        jQuery("#glReportsForm").submit();
        if(action=='exportCashAccounts'){
           window.parent.closePreloading(); 
        }
    }
}

function refresh(action){
    jQuery("#action").val(action);
    jQuery("#glReportsForm").submit();
}