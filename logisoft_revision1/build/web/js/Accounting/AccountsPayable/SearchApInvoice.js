jQuery(document).ready(function(){
    jQuery(document).keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == 13) {
            if(jQuery.trim(jQuery("#cusName").val())==""){
                alert("Please select the customer");
                jQuery("#cusName").focus();
            }else{
                SearchApInvoice();
            }
        }
    });
});

function searchResults(){
    if(event.keyCode==13 || (event.keyCode==9) || (event.button==0)){
        SearchApInvoice();
    }
}
function SearchApInvoice(){
    document.apInvoiceForm.buttonValue.value="searchApInvoice";
    document.apInvoiceForm.submit();
}

function clearApInvoice(){
    document.apInvoiceForm.buttonValue.value="clearInvoice";
    document.apInvoiceForm.submit();
}
function editApInvoice(val1){
    document.apInvoiceForm.buttonValue.value="editInvoice";
    document.apInvoiceForm.apInvoiceId.value = val1;
    document.apInvoiceForm.submit();
}
function viewApInvoice(val1){
    document.apInvoiceForm.buttonValue.value="viewInvoice";
    document.apInvoiceForm.apInvoiceId.value = val1;
    document.apInvoiceForm.submit();
}
function addNewApInvoice(){
    document.apInvoiceForm.buttonValue.value="addAPInvoice";
    document.apInvoiceForm.submit();
}
function gotoPage(pageNo){
    document.getElementById("pageNo").value = pageNo;
    document.getElementById("buttonValue").value="searchApInvoice";
    document.apInvoiceForm.submit();
}
function gotoSelectedPage(){
    document.getElementById("pageNo").value =  document.getElementById("selectedPageNo").value;
    document.getElementById("buttonValue").value="searchApInvoice";
    document.apInvoiceForm.submit();
}
function doSort(sortBy,orderBy){
    document.getElementById("sortBy").value = sortBy;
    document.getElementById("orderBy").value = orderBy;
    document.getElementById("buttonValue").value="searchApInvoice";
    document.apInvoiceForm.submit();
}
if(document.getElementById("cusName")){
    AjaxAutocompleter("cusName", "custname_choices","accountNumber", "custname_check", rootPath+"/servlet/AutoCompleterServlet?action=Vendor&textFieldId=cusName&accountType=V","searchResults()","");
}
