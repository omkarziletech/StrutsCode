/* 
 *  Document   : incomeStatement
 *  Created on : Mar 10, 2014, 4:08:25 PM
 *  Author     : Lakshmi Narayanan
 */
var path = "/" + window.location.pathname.split('/')[1];


function printIncomeStatement(fileName) {
    var title = "Income Statement";
    var url = path + "/servlet/FileViewerServlet?fileName=" + fileName;
    window.parent.parent.showLightBox(title, url);
}

function exportIncomeStatement(fileName) {
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

function createIncomeStatement(fileType) {
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
                action: "createIncomeStatement",
                fileType: fileType,
                year: $("#year").val(),
                startPeriod: $("#startPeriod").val(),
                endPeriod: $("#endPeriod").val()
            },
            preloading: true,
            success: function(data) {
                if("pdf" === fileType){
                    printIncomeStatement(data);
                }else{
                    exportIncomeStatement(data);
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