/* 
 *  Document   : accountMaintenance
 *  Created on : Feb 24, 2014, 8:12:56 PM
 *  Author     : Lakshmi Narayanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function onReportCategoryCallBefore() {
    if ($.trim($("#accttype").val()) === "") {
        $.prompt("Please select account type", {
            callback: function() {
                return false;
            }
        });
    }else{
        $("#reportCategory").setOptions({
            extraParams: {
                param1: $("#accttype").val()
            }
        });
    }
}
function initAutocompleteFields() {
    var url = path + "/autocompleter/action/getAutocompleterResults.jsp?query=ECU_ACCOUNT_MAPPING_ACCOUNT_TYPE&template=ecuAccountMapping&fieldIndex=1&";
    $("#reportCategory").initautocomplete({
        url: url,
        width: "300px",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 200,
        mustMatch: false,
        callBefore: "onReportCategoryCallBefore()"
    });
}
$(document).ready(function() {
    initAutocompleteFields();
});