/* 
 *  Document   : accountRange
 *  Created on : Feb 25, 2014, 2:47:25 PM
 *  Author     : Lakshmi Narayanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function printTrialBalance(value) {
    var startingAccount = $("#startingAccount").val();
    var endingAccount = $("#endingAccount").val();
    var ecuReport = $("#ecuReport").is(":checked");
    parent.parent.GB_hide();
    parent.parent.printTrialBalance(value, startingAccount, endingAccount, ecuReport);
}

function exportTrialBalance(value) {
    var startingAccount = $("#startingAccount").val();
    var endingAccount = $("#endingAccount").val();
    var ecuReport = $("#ecuReport").is(":checked");
    parent.parent.GB_hide();
    parent.parent.exportTrialBalance(value, startingAccount, endingAccount, ecuReport);
}

function setStartingOptions() {
    var url = path + "/autocompleter/action/getAutocompleterResults.jsp?query=";
    if ($("#ecuReport").is(":checked")) {
        url += "ECU_ACCOUNT_MAPPING";
        url += "&template=ecuAccountMapping";
        url += "&fieldIndex=1&";
    } else {
        url += "GL_ACCOUNT";
        url += "&template=glAccount";
        url += "&fieldIndex=1&";
    }
    $("#startingAccount").setOptions({
        url: url
    });
}

function setEndingOptions() {
    var url = path + "/autocompleter/action/getAutocompleterResults.jsp?query=";
    if ($("#ecuReport").is(":checked")) {
        url += "ECU_ACCOUNT_MAPPING";
        url += "&template=ecuAccountMapping";
        url += "&fieldIndex=1&";
    } else {
        url += "GL_ACCOUNT";
        url += "&template=glAccount";
        url += "&fieldIndex=1&";
    }
    $("#endingAccount").setOptions({
        url: url
    });
}

function initAutocompleteFields() {
    var url = path + "/autocompleter/action/getAutocompleterResults.jsp?query=ECU_ACCOUNT_MAPPING&template=ecuAccountMapping&fieldIndex=1&";
    $("#startingAccount").initautocomplete({
        url: url,
        width: "300px",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 200,
        mustMatch: true,
        callBefore: "setStartingOptions()"
    });
    $("#endingAccount").initautocomplete({
        url: url,
        width: "300px",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 200,
        mustMatch: true,
        callBefore: "setEndingOptions()"
    });
}

$(document).ready(function() {
    initAutocompleteFields();
});