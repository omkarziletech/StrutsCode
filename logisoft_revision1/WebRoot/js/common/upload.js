/* 
 * Document   : upload
 * Created on : Mar 05, 2014, 05:54:00 PM
 * Author     : Lakshmi Naryanan
 */
$(document).ready(function() {
    $("#file").fileInput();
    $("#uploadForm").submit(function() {
        var ext = $("#file").val().split('.').pop().toLowerCase();
        var allow = new Array("xls", "xlsx");
        if ($.inArray(ext, allow) === -1) {
            $(".error").html("Please choose only xls or xlsx type file to upload");
            return false;
        } else {
            showPreloading();
        }
    });
});