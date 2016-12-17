/* 
 *  Document   : ecuAccountMapping
 *  Created on : Feb 24, 2014, 4:13:56 PM
 *  Author     : Lakshmi Narayanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function setResultHeight() {
    if ($(".result-container").length > 0) {
        var windowHeight = window.parent.getFrameHeight();
        var height = windowHeight;
        height -= 50;
        $(".result-container").height(height);
        $("body").css("overflow", "hidden");
    }
}

function save(ele) {
    var parent = $(ele).parent().parent();
    if ($.trim(parent.find(".report-category").val()) === "") {
        $.prompt("Please enter report category", {
            callback: function() {
                parent.find(".report-category").val("").callFocus();
            }
        });
//    } else if ($.trim(parent.find(".account-type").val()) === "") {
//        $.prompt("Please enter account type", {
//            callback: function() {
//                parent.find(".account-type").val("").callFocus();
//            }
//        });
    } else {
        $("#reportCategory").val(parent.find(".report-category").val());
        $("#accountType").val(parent.find(".account-type").val());
        $("#id").val(parent.find(".id").val());
        $("#action").val("save");
        $("#ecuAccountMappingForm").submit();
    }
}

function removeMapping(ele) {
    var parent = $(ele).parent().parent();
    $.prompt("Do you want to delete the report category?", {
        callback: function(v) {
            if (v) {
                $("#reportCategory").val(parent.find(".report-category").val());
                $("#accountType").val(parent.find(".account-type").val());
                $("#id").val(parent.find(".id").val());
                $("#action").val("delete");
                $("#ecuAccountMappingForm").submit();
            }
        }
    });
}

$(document).ready(function() {
    $("#ecuAccountMappingForm").submit(function() {
        showPreloading();
    });
    setResultHeight();
    $(window).resize(function() {
        window.parent.changeHeight();
        setResultHeight();
    });
    $("[title != '']").not("link").tooltip();
    Lightbox.initialize({
        color: 'black',
        dir: path + '/js/lightbox/images',
        moveDuration: 1,
        resizeDuration: 1
    });
});