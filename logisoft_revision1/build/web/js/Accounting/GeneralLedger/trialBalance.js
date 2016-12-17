/* 
 *  Document   : trialBalance
 *  Created on : Feb 28, 2014, 12:54:25 PM
 *  Author     : Lakshmi Narayanan
 */
var path = "/" + window.location.pathname.split('/')[1];


function printTrialBalance(fileName) {
    var title = "Trial Balance";
    var url = path + "/servlet/FileViewerServlet?fileName=" + fileName;
    window.parent.parent.showLightBox(title, url);
}

function exportTrialBalance(fileName) {
    var src = path + "/servlet/FileViewerServlet?fileName=" + fileName;
    if ($("#iframe").length > 0) {
        $("#iframe").attr("src", src);
    } else {
        $("<iframe/>", {
            name: "iframe",
            id: "iframe",
            src: src
        }).appendTo("body").hide();
    }
}

function createTrialBalance(fileType) {
    if ($.trim($("#startPeriod").val()) === "") {
        $.prompt("Please enter starting period", {
            callback: function() {
                $("#startPeriod").val("").focuz();
            }
        });
    }else if ($.trim($("#endPeriod").val()) === "") {
        $.prompt("Please enter ending period", {
            callback: function() {
                $("#endPeriod").val("").focuz();
            }
        });
    }else{
        $.ajaxx({
            url: path + "/fiscalPeriod.do",
            data: {
                action: "createTrialBalance",
                fileType: fileType,
                startPeriod: $("#startPeriod").val(),
                endPeriod: $("#endPeriod").val(),
                ecuFormat: $("#ecuFormat").is(":checked")
            },
            preloading: true,
            success: function(data) {
                if("pdf" === fileType){
                    printTrialBalance(data);
                }else{
                    exportTrialBalance(data);
                }
            }
        });
    }
}

function initAutocompleteFields() {
    var url = path + "/autocompleter/action/getAutocompleterResults.jsp?query=GL_PERIOD&template=glPeriod&fieldIndex=1&";
    $("#startPeriod").initautocomplete({
        url: url,
        width: "200px",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 100,
        mustMatch: false
    });
    $("#endPeriod").initautocomplete({
        url: url,
        width: "200px",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 100,
        mustMatch: false
    });
}

$(document).ready(function() {
    initAutocompleteFields();
});