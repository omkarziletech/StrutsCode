jQuery(document).ready(function(){
    jQuery("#clear").click(function(){
        jQuery("#action").val("clear");
        jQuery("#arAccountNotesReportForm").submit();
    });
});

if(document.getElementById("notesEnteredBy")){
    var url = rootPath+"/servlet/AutoCompleterServlet?action=User&get=id&textFieldId=notesEnteredBy";
    AjaxAutocompleter("notesEnteredBy", "notesEnteredByDiv", "", "notesEnteredByCheck", url, "", "");
}

if(document.getElementById("customerName")){
    var autocompleterUrl = rootPath+"/servlet/AutoCompleterServlet?action=Vendor&textFieldId=customerName";
    AjaxAutocompleter("customerName","customerNameChoices","customerNumber","customerNameCheck",autocompleterUrl,"","");
}

if(document.getElementById("accountAssignedTo")){
    var url = rootPath+"/servlet/AutoCompleterServlet?action=User&get=id&textFieldId=accountAssignedTo";
    AjaxAutocompleter("accountAssignedTo","accountAssignedToDiv","","accountAssignedToCheck",url,"","");
}

function gotoAction(action){
    var accountAssignedTo = jQuery.trim(jQuery("#accountAssignedTo").val());
    var notesEnteredBy = jQuery.trim(jQuery("#notesEnteredBy").val());
    var customerName = jQuery.trim(jQuery("#customerName").val());
    var fromDate = jQuery.trim(jQuery("#txtcal1").val());
    var toDate = jQuery.trim(jQuery("#txtcal2").val());
     if(accountAssignedTo == "" && notesEnteredBy == "" && customerName == ""){
         alert("Please enter either account assigned to or notes entered by or customer name");
     }
     else if(fromDate == ""){
        alert("Please enter from date");
        jQuery("#txtcal1").focus();
        return;
    }else if(toDate == ""){
        alert("Please enter to date");
        jQuery("#txtcal2").focus();
        return;
    }else{
        jQuery("#action").val(action);
        jQuery("#arAccountNotesReportForm").submit();
    }
}

function validateDate(date,dateCondition){
    if(date.value!=""){
        date.value = date.value.getValidDateTime("/","",false);
        if(date.value==""){
            alert("Please enter valid date");
            document.getElementById(date.id).focus();
        }
    }
    if(date.value!=""){
        var fromDate = jQuery.trim(jQuery("#txtcal1").val());
        var toDate = jQuery.trim(jQuery("#txtcal2").val());
        if(Date.parse(fromDate)> Date.parse(toDate)){
            if(dateCondition == "from"){
                alert("From date should not be greater than to date");
                jQuery("#txtcal1").val("");
                jQuery("#txtcal1").focus();
            }else{
                alert("To date should not be less than from date");
                jQuery("#txtcal2").val("");
                jQuery("#txtcal2").focus();
            }
        }
    }
}
