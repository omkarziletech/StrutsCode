/* 
 * Document   : dragAndDrop
 * Created on : Sep 25, 2015, 05:36:15 PM
 * Author     : Lakshmi Naryanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function changeButtonColor(screenName, documentName) {
    if (screenName.length > 3 && screenName.substring(0, 3) === 'LCL') {
        var button;
        if ("LCL SS MASTER BL" === screenName || "LCL EXPORTS UNIT" === screenName) {
            button = $("#scan-Attach" + $('#documentId').val(), window.parent.parent.document);
            var data = $("#receivedMaster" + $('#documentId').val(), window.parent.parent.document);
            var status = $('#status').val();
            data.text("Yes(" + status + ")");
        } else {
            button = $("#scan-attach, #scan-Attach, #scanAttach,#lclSsMasterScanAttach" + documentName, window.parent.parent.document);
        }
        if (button.length > 0) {
            button.addClass("green-background");
            if(documentName == 'CHECK COPY'){
                parent.parent.$('#paymentTypeCO').attr('disabled', false);
            }
        }
    } else if (screenName !== "INVOICE" && screenName !== "AR BATCH" && screenName !== "JOURNAL ENTRY" && screenName !== "ARCONFIG") {
        window.parent.parent.changeScanButtonColor(($("#status").length > 0 ? $("#status").val() : ""), documentName);
    } else {
        var button = $("#scanAttach, #arScan", window.parent.parent.document);
        if (button.length > 0) {
            button.addClass("green-background");
        }
    }
}
function goBack() {
    $("#action").val("search");
    $("#scanForm").submit();
}

var dropped = [];

function uploadFiles() {
    $.each(dropped, function (index) {
        var row = $("#drop #files table tr:eq(" + index + ")");
        var div = row.find("td:eq(1)").find("div");
        var data = new FormData();
        data.append("document", this);
        data.append("screenName", $("#screenName").val());
        data.append("documentId", $("#documentId").val());
        data.append("documentName", $("#documentName").val());
        data.append("documentType", $("#documentType").val());
        data.append("comment", $("#comment").val());
        data.append("status", $("#status").val());
        data.append("bookingType",$("#bookingType").val());
        data.append("hiddenScreenName",$("#hiddenScreenName").val());
        var url = path + "/scan.do?action=upload";
        $.ajax({
            url: url, //Server script to process data
            type: 'POST',
            cache: false,
            //Options to tell jQuery not to process data or worry about content-type.
            contentType: false,
            processData: false,
            // Form data
            data: data,
            xhr: function () {  // Custom XMLHttpRequest
                var xhr = $.ajaxSettings.xhr();
                if (xhr.upload) { // Check if upload property exists
                    //xhr.upload.onprogress
                    xhr.upload.addEventListener('progress', function (ev) {
                        var percentage = Math.floor(ev.loaded / ev.total * 100);
                        div.css("width", percentage + "%");
                        div.find("span").text(percentage + "%");
                    }, false); // For handling the progress of the upload
                }
                return xhr;
            },
            //Ajax events
            success: function () {
                row.find("td:eq(3)").html("<img src='" + path + "/images/fileUpload/success.png' title='Uploaded successfully'/>");
                if (index === dropped.length - 1) {
                    changeButtonColor($("#screenName").val(), $("#documentName").val());
                    goBack();
                }
            },
            error: function () {
                row.find("td:eq(3)").html("<img src='" + path + "/images/fileUpload/error.png' title='Upload failed'/>");
            }
        });
    });
}


$(document).ready(function () {
    Lightbox.initialize({
        color: 'black',
        dir: path + '/js/lightbox/images',
        moveDuration: 1,
        resizeDuration: 1,
        parent: "#scanForm"
    });
    $('#drop').bind(
            'dragover',
            function (e) {
                e.preventDefault();
                e.stopPropagation();
            }
    );
    $('#drop').bind(
            'dragenter',
            function (e) {
                e.preventDefault();
                e.stopPropagation();
            }
    );
    $('#drop').bind(
            'drop',
            function (e) {
                if (e.originalEvent.dataTransfer) {
                    if (e.originalEvent.dataTransfer.files.length) {
                        e.preventDefault();
                        e.stopPropagation();
                        var files = e.originalEvent.dataTransfer.files;
                        dropped = [];
                        $.each(files, function () {
                            dropped.push(this);
                        });
                        var rows = [];
                        $.each(dropped, function () {
                            var fileSize;
                            if (this.size > 1048576) {
                                fileSize = Math.round(this.size * 100 / 1048576) / 100 + " MB";
                            } else if (this.size > 1024) {
                                fileSize = Math.round(this.size * 100 / 1024) / 100 + " KB";
                            } else {
                                fileSize = this.size + " Bytes";
                            }
                            var row = "<tr>";
                            row += "<td>" + this.name + "</td>";
                            row += "<td class='width-200px align-right'><div style='width: 0px;background-color: lightgreen'><span></span></div></td>";
                            row += "<td class='width-100px align-right'>" + fileSize + "</td>";
                            row += "<td class='width-50px align-center'></td>";
                            row += "</tr>";
                            rows.push(row);
                        });
                        $("#files").html("<table style='width: 100%;'>" + rows.join("") + "</table>");
                        uploadFiles();
                    } else {
                        var url = path + "/?TB_html&height=150&width=600";
                        Lightbox.showPopUp("Download", url, "sexylightbox", "", "", $("#download").html(), "");
                    }
                }
            }
    );
});