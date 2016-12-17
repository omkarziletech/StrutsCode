/* 
 * Document   : chartOfAccounts
 * Created on : Jun 02, 2014, 07:46:10 PM
 * Author     : Lakshmi Naryanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function search() {
    $("#sortBy").val("account");
    $("#orderBy").val("asc");
    $("#selectedPage").val("1");
    $("#action").val("search");
    $("#chartOfAccountsForm").submit();
}

function sorting(sortBy) {
    $("#sortBy").val(sortBy);
    if ($("#orderBy").val() === "desc") {
        $("#orderBy").val("asc");
    } else {
        $("#orderBy").val("desc");
    }
    $("#action").val("sortingAndPaging");
    $("#chartOfAccountsForm").submit();
}

function paging(pageNo) {
    if (pageNo === "") {
        $("#selectedPage").val($("#selectedPageNo").val());
    } else {
        $("#selectedPage").val(pageNo);
    }
    $("#action").val("sortingAndPaging");
    $("#chartOfAccountsForm").submit();
}

function searchTransactions(account) {
    $("#startAccount").val(account);
    $("#endAccount").val(account);
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