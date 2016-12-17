/* 
 * Document   : transactionHistory
 * Created on : Jun 02, 2014, 09:42:56 PM
 * Author     : Lakshmi Naryanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function search() {
    $("#action").val("searchTransactions");
    $("#chartOfAccountsForm").submit();
}

function toggle(ele) {
    $("." + ele).slideToggle(function() {
        if ($("#toggled").val() === "true") {
            $("#toggled").val("false");
        } else {
            $("#toggled").val("true");
        }
    });
}

function goback(){
    $("#action").val("sortingAndPaging");
    $("#chartOfAccountsForm").submit();
}

function initAutoCompleteFields(){
    $("#startAccount,#endAccount").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=GL_ACCOUNT&template=glAccount&fieldIndex=1&",
        width: "350px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300
    });
    $("#startPeriod,#endPeriod").initautocomplete({
        url: path + "/autocompleter/action/getAutocompleterResults.jsp?query=GL_PERIOD&template=glPeriod&fieldIndex=1&",
        width: "350px",
        resultsClass: "ac_results z-index",
        resultPosition: "fixed",
        scroll: true,
        scrollHeight: 300
    });
}

$(document).ready(function() {
    initAutoCompleteFields();
    $("[title != '']").not("link").tooltip();
    $("#chartOfAccountsForm").submit(function() {
        showPreloading();
    });
});