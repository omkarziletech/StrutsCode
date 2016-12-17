/* 
 * Document   : scanOrAttach
 * Created on : Jan 16, 2015, 11:08:00 PM
 * Author     : Lakshmi Naryanan
 */
var path = "/" + window.location.pathname.split('/')[1];

function addDocument() {
    $.ajaxx({
        url: path + "/scan.do",
        data: {
            action: "addDocument"
        },
        preloading: true,
        success: function (data) {
            var url = path + "?TB_html&height=100&width=300";
            Lightbox.showPopUp("Add Document", url, "sexylightbox", "", "", data);
        }
    });
}

function editDocument(id) {
    $.ajaxx({
        url: path + "/scan.do",
        data: {
            action: "editDocument",
            'scanConfig.id': id
        },
        preloading: true,
        success: function (data) {
            var url = path + "?TB_html&height=100&width=300";
            Lightbox.showPopUp("Edit Document", url, "sexylightbox", "", "", data);
        }
    });
}

function removeDocument(documentName, id) {
    $.prompt("Do you want to delete the document - " + documentName + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v) {
            if (v) {
                $("input[name='scanConfig.id']").val(id);
                $("#action").val("removeDocument");
                $("#scanForm").submit();
            }
        }
    });
}

function saveDocument() {
    if ($.trim($("#addOrEditDocumentName").val()) === "") {
        $.prompt("Please enter document name", {
            callback: function () {
                $("#addOrEditDocumentName").val("").focuz();
            }
        });
    } else {
        $("input[name='scanConfig.id']").val($("#addOrEditId").val());
        $("input[name='scanConfig.screenName']").val($("#addOrEditScreenName").val().toUpperCase());
        $("input[name='scanConfig.documentName']").val($("#addOrEditDocumentName").val().toUpperCase());
        $("#action").val("saveDocument");
        $("#scanForm").submit();
    }
}

function resetButtonColor(screenName, documentName) {
    var allFilecount = getsslCount(screenName);
    if ($("#filelist tbody tr").length <= 0) {
        if (screenName.length > 3 && screenName.substring(0, 3) === 'LCL') {
            var button = $("#scan-attach, #scan-Attach, #scanAttach, #scanattach", window.parent.parent.document);
            if (button.length > 0) {
               button.removeClass("green-background").addClass("button-style1");
               if(documentName == 'CHECK COPY') {
               parent.parent.$('#paymentTypeCO').attr('disabled', true);
           }
        }
        } else if (screenName !== "INVOICE" && screenName !== "AR BATCH" && screenName !== "JOURNAL ENTRY" && screenName !== "ARCONFIG") {
            window.parent.parent.changeScanButtonColor(($("input[name='status']").length > 0 ? $("input[name='status']:checked").val() : ""), documentName, allFilecount);
        } else {
            var button = $("#scanAttach, #arScan", window.parent.parent.document);
            if (button.length > 0) {
                button.removeClass("green-background").addClass("button");
            }
        }
    } else {
        var sslinefileCount = getSSLineMasterCount(screenName);

        if (screenName !== "INVOICE" && screenName !== "AR BATCH" && screenName !== "JOURNAL ENTRY" && screenName !== "ARCONFIG") {
            if (sslinefileCount <= 0) {
                window.parent.parent.changeScanButtonColor(($("input[name='status']").length > 0 ? $("input[name='status']:checked").val() : ""), 'SS LINE MASTER BL', allFilecount, sslinefileCount);
            } else {
                window.parent.parent.changeScanButtonColor(($("input[name='status']").length > 0 ? $("input[name='status']:checked").val() : ""), documentName, allFilecount);
            }
        }
    }
}

function showDocuments(documentName) {
    var screenName = $("#screenName").val();
    var documentId = $("#documentId").val();
    $.ajaxx({
        url: path + "/scan.do",
        data: {
            action: "showDocuments",
            screenName: screenName,
            documentName: documentName,
            documentId: documentId
        },
        preloading: true,
        success: function (data) {
            var url = path + "?TB_html&height=400&width=800";
            var callback = "search();";
            var onload = "resetButtonColor('" + screenName + "', '" + documentName + "');";
            Lightbox.showPopUp("Documents", url, "sexylightbox", "", callback, data, onload);
        }
    });
}

function showSop(documentName) {
    var screenName = $("#screenName").val();
    var documentId = $("#documentId").val();
    $.ajaxx({
        url: path + "/scan.do",
        data: {
            action: "showDocuments",
            screenName: screenName,
            documentName: documentName,
            documentId: documentId
        },
        preloading: true,
        success: function (data) {
            var url = path + "?TB_html&height=400&width=800";
            var callback = "sopDocument();";
            var onload = "resetButtonColor('" + screenName + "', '" + documentName + "');";
            Lightbox.showPopUp("Documents", url, "sexylightbox", "", callback, data, onload);
        }
    });

}

function viewDocument(title, fileName) {
    var url = path + "/servlet/FileViewerServlet?fileName=" + fileName;
    if (fileName.indexOf(".xls") > 0 || fileName.indexOf(".doc") > 0
            || fileName.indexOf(".mht") > 0 || fileName.indexOf(".msg") > 0
            || fileName.indexOf(".csv") > 0 || fileName.indexOf(".ppt") > 0) {
        var iframe = $("<iframe/>", {
            name: "iframe",
            id: "iframe",
            src: url
        }).appendTo("body").hide();
        iframe.load(function () {
            iframe.remove();
        });
    } else {   // to open a new browser window
        window.open(url, '_blank', 'resizable=1,width=1000,height=600,top=40,left=40,directories=no,location=no,linemenubar=no,toolbar=no,status=no,scrollbars=yes');
    }
}

function resetButtonColorForExport(screenName, documentName, documentId) {
    var data = $("#receivedMaster" + documentId, window.parent.parent.document);
    if ($("#filelist tbody tr").length <= 0) {
        if (screenName.length > 3 && screenName.substring(0, 3) === 'LCL') {
            var button = $("#scan-Attach" + documentId, window.parent.parent.document);
            if (button.length > 0) {
                button.removeClass("green-background").addClass("button-style1");
                data.text("");
            }
        }
    } else {
        data.text("Yes(" + $(".receivedMasterStatus0").text() + ")");
    }
}

function deleteDocument(id, fileName, documentName) {
    var onload = "";
    $.prompt("Do you want to delete the file - " + fileName + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v) {
            if (v) {
                Lightbox.close();
                var screenName = $("#screenName").val();
                var documentId = $("#documentId").val();
                $.ajaxx({
                    url: path + "/scan.do",
                    data: {
                        action: "deleteDocument",
                        id: id,
                        screenName: screenName,
                        documentName: documentName,
                        documentId: documentId
                    },
                    preloading: true,
                    success: function (data) {
                        var url = path + "?TB_html&height=400&width=800";
                        var callback = "search();";
                        if ("LCL SS MASTER BL" === screenName || "LCL EXPORTS UNIT" === screenName) {
                            onload = "resetButtonColorForExport('" + screenName + "', '" + documentName + "','" + documentId + "');";
                        } else {
                            onload = "resetButtonColor('" + screenName + "', '" + documentName + "');";
                        }
                        Lightbox.showPopUp("Documents", url, "sexylightbox", "", callback, data, onload);
                    }
                });
            }
        }
    });
}
function deleteDocumentOlyView(id, fileName, documentName) {
    var onload = "";
    $.prompt("Do you want to delete the file - " + fileName + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v) {
            if (v) {
                Lightbox.close();
                var screenName = $("#screenName").val();
                var documentId = $("#documentId").val();
                $.ajaxx({
                    url: path + "/scan.do",
                    data: {
                        action: "deleteDocument",
                        id: id,
                        screenName: screenName,
                        documentName: documentName,
                        documentId: documentId
                    },
                    preloading: true,
                    success: function (data) {
                        var url = path + "?TB_html&height=400&width=800";
                        var callback = "search();";
                        if ("LCL SS MASTER BL" === screenName || "LCL EXPORTS UNIT" === screenName) {
                            onload = "resetButtonColorForExport('" + screenName + "', '" + documentName + "','" + documentId + "');";
                        } else {
                            onload = "resetButtonColor('" + screenName + "', '" + documentName + "');";
                        }
                        Lightbox.showPopUp("Documents", url, "sexylightbox", "", callback, data, onload);
                    }
                });
            }
        }
    });
}

function deleteDocumentSop(id, fileName, documentName) {
    $.prompt("Do you want to delete the file - " + fileName + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v) {
            if (v) {
                Lightbox.close();
                var screenName = $("#screenName").val();
                var documentId = $("#documentId").val();
                $.ajaxx({
                    url: path + "/scan.do",
                    data: {
                        action: "deleteDocument",
                        id: id,
                        screenName: screenName,
                        documentName: documentName,
                        documentId: documentId
                    },
                    preloading: true,
                    success: function (data) {
                        var url = path + "?TB_html&height=400&width=800";
                        var callback = " sopDocument();";
                        var onload = "resetButtonColor('" + screenName + "', '" + documentName + "');";
                        Lightbox.showPopUp("Documents", url, "sexylightbox", "", callback, data, onload);
                    }
                });
            }
        }
    });
}

function showSsMasterBl(documentName, action) {
    $.prompt({
        state: {
            html: "Enter ss master bl&nbsp;&nbsp;&nbsp;&nbsp;<input type='text' id='ssMasterBlNo' class='textbox'/>",
            buttons: {
                Save: true,
                Cancel: false
            },
            loaded: function () {
                $("#ssMasterBlNo").initautocomplete({
                    url: path + "/autocompleter/common/action/getAutocompleterResults.jsp?query=SS_BOOKING_NO&fieldIndex=1&",
                    width: "150px",
                    resultsClass: "ac_results z-index-more",
                    resultPosition: "absolute",
                    scroll: true,
                    scrollHeight: 100
                });
                $(".jqibuttons").css("text-align", "center");
            },
            submit: function (v) {
                if (v) {
                    var ssMasterBlNo = $("#ssMasterBlNo").val();
                    if (ssMasterBlNo !== "") {
                        $("#ssMasterBl").val(ssMasterBlNo);
                        $("#newMasterBL", window.parent.parent.document).val(ssMasterBlNo);
                        showComment(documentName, action);
                    } else {
                        $("#ssMasterBlNo").focuz();
                        e.preventDefault();
                    }
                }
            }
        }
    });
}
    
function showComment(documentName, action, screenName1) {
     var ssMasterBl = $("#ssMasterBl").val();
     if (screenName1 !== undefined) {
       if (documentName === "SS LINE MASTER BL" && $.trim(ssMasterBl) === "") {
            showSsMasterBl(documentName, action);
        } else {
            var screenName = $("#screenName").val();
            var hiddenScreenName = $('#hiddenScreenName').val();
            $.ajaxx({
                url: path + "/jsp/common/scan/comment.jsp",
                data: {
                    action: action,
                    screenName: screenName,
                    documentName: documentName,
                    hiddenScreenName: hiddenScreenName
                },
                preloading: true,
                success: function (data) {
                    var height = action === "Attach" ? (documentName === "SS LINE MASTER BL" ? 125 : 100) : 75;
                    var url = path + "?TB_html&height=" + height + "&width=375";
                    Lightbox.showPopUp("Comments", url, "sexylightbox", "", "", data);
                    if (action === "Attach") {
                        setTimeout(function () {
                            $("#document").fileInput();
                        }, 100);
                    }
                }
            });
        }
    } else {
        if (documentName === "SS LINE MASTER BL" && $.trim(ssMasterBl) === "") {
            showSsMasterBl(documentName, action);
        } else {
            var screenName = $("#screenName").val();
            $('#hiddenScreenName').val('');
            var hiddenScreenName = $('#hiddenScreenName').val();
            $.ajaxx({
                url: path + "/jsp/common/scan/comment.jsp",
                data: {
                    action: action,
                    screenName: screenName,
                    documentName: documentName,
                    hiddenScreenName: hiddenScreenName

                },
                preloading: true,
                success: function (data) {
                    var height = action === "Attach" ? (documentName === "SS LINE MASTER BL" ? 125 : 100) : 75;
                    var url = path + "?TB_html&height=" + height + "&width=375";
                    Lightbox.showPopUp("Comments", url, "sexylightbox", "", "", data);
                    if (action === "Attach") {
                        setTimeout(function () {
                            $("#document").fileInput();
                        }, 100);
                    }
                }
            });
        }
    }
}
    
function changeButtonGColor(screenName, documentName) {
    if (screenName.length > 3 && screenName.substring(0, 3) === 'LCL') {
        if ("LCL SS MASTER BL" === screenName || "LCL EXPORTS UNIT" === screenName) {
            var button = $("#scan-Attach" + $('#documentId').val(), window.parent.parent.document);
            var data = $("#receivedMaster" + $('#documentId').val(), window.parent.parent.document);
            var status = $("input:radio[name=status]:checked").val();
            data.text("Yes(" + status + ")");
        } else {
            var button = $("#scan-attach, #scan-Attach, #scanAttach, #scanattach, #lclSsMasterScanAttach" + documentName, window.parent.parent.document);
        }
         if (button.length > 0) {
              button.addClass("green-background");
              if(documentName == 'CHECK COPY'){
              parent.parent.$('#paymentTypeCO').attr('disabled', false);
          }
        }
    } else if (screenName !== "INVOICE" && screenName !== "AR BATCH" && screenName !== "JOURNAL ENTRY" && screenName !== "ARCONFIG") {
        window.parent.parent.changeScanButtonColor(($("input[name='status']").length > 0 ? $("input[name='status']:checked").val() : ""), documentName);
    } else {
        var button = $("#scanAttach, #arScan", window.parent.parent.document);
        if (button.length > 0) {
            button.addClass("green-background");
        }
    }
}

function attach(documentName) {
    var screenName = $("#screenName").val();
    if ($.trim($("#document").val()) === "") {
        $.prompt("Please choose a file to upload.", {
            callback: function () {
                $("#customFile").focuz();
            }
        });
    } else if ($.trim($("#comment").val()) === "" && (screenName !== "INVOICE" && screenName !== "AR BATCH" && screenName !== "JOURNAL ENTRY")) {
        $.prompt("Please enter a comment", {
            callback: function () {
                $("#comment").focuz();
            }
        });
    } else {
        $("#documentName").val(documentName);
        $("#action").val("attach");
        $("#scanForm").submit();
        changeButtonGColor(screenName, documentName);
    }
}

function scan(documentName) {
    var screenName = $("#screenName").val();
    if ($.trim($("#comment").val()) === "" && (screenName !== "INVOICE" && screenName !== "AR BATCH" && screenName !== "JOURNAL ENTRY")) {
        $.prompt("Please enter a comment", {
            callback: function () {
                $("#comment").focuz();
            }
        });
    } else {
        $("#documentName").val(documentName);
        $("#action").val("scan");
        $("#scanForm").submit();
        changeButtonGColor(screenName, documentName);
    }
}

function dragAndDrop(documentName) {
    var screenName = $("#screenName").val();
    if ($.trim($("#comment").val()) === "" && (screenName !== "INVOICE" && screenName !== "AR BATCH" && screenName !== "JOURNAL ENTRY")) {
        $.prompt("Please enter a comment", {
            callback: function () {
                $("#comment").focuz();
            }
        });
    } else {
        $("#documentName").val(documentName);
        $("#action").val("dragAndDrop");
        $("#scanForm").submit();
        changeButtonColor(screenName, documentName);
    }
}

function search() {
    $("#action").val("search");
    $("#scanForm").submit();
}

function sopDocument() {
    $("#action").val("sopDocument");
    $("#scanForm").submit();
}

$(document).ready(function () {
    $("#scanForm").submit(function () {
        Lightbox.close();
        $.startPreloader();
    });
    Lightbox.initialize({
        color: 'black',
        dir: path + '/js/lightbox/images',
        moveDuration: 1,
        resizeDuration: 1,
        parent: "#scanForm"
    });
    $("[title != '']").not("link").tooltip();
    $(".message").fadeOut(10000, function () {
        $(this).html("");
    });
});


function getsslCount(screenname) {
    var documentId = $("#documentId").val();
    var value;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO",
            methodName: "getSSMasterCount",
            param1: documentId,
            param2: screenname,
            dataType: "json"
        },
        async: false,
        success: function (data) {

            if (data !== '' || data !== '0') {
                value = data;
            }
        }

    });
    return value;
}
function getSSLineMasterCount(screenname){
    var documentId = $("#documentId").val();
    var value;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO",
            methodName: "getSSLineMasterCount",
            param1: documentId,
            param2: screenname,
            dataType: "json"
        },
        async: false,
        success: function(data){
            if(data !== '' || data !== '0'){
                value = data;
            }
        }
    });
    return value;
}

function showDocumentForTrans(documentName, screenName, screenName1) {
    var documentId = $("#documentId").val();
    var bookingType = $("#bookingType").val();
    $.ajaxx({
        url: path + "/scan.do",
        data: {
            action: "showDocumentsForTrans",
            screenName: screenName,
            documentName: documentName,
            documentId: documentId,
            hiddenScreenName: screenName1,
            bookingType: bookingType
        },
        preloading: true,
        success: function (data) {
            var url = path + "?TB_html&height=400&width=800";
            var callback = "search();";
            var onload = "resetButtonColor('" + screenName + "', '" + documentName + "');";
            Lightbox.showPopUp("Documents", url, "sexylightbox", "", callback, data, onload);
        }
    });
}
function deleteDocumentForTrans(id, fileName, documentName) {
    var onload = "";
    $.prompt("Do you want to delete the file - " + fileName + "?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v) {
            if (v) {
                Lightbox.close();
                var screenName = $("#screenName").val();
                var documentId = $("#documentId").val();
                var hiddenScreenName = $('#hiddenScreenName').val();
                $.ajaxx({
                    url: path + "/scan.do",
                    data: {
                        action: "deleteDocumentForTrans",
                        id: id,
                        screenName: screenName,
                        documentName: documentName,
                        documentId: documentId,
                        hiddenScreenName: hiddenScreenName
                    },
                    preloading: true,
                    success: function (data) {
                        var url = path + "?TB_html&height=400&width=800";
                        var callback = "search();";
                        if ("LCL SS MASTER BL" === screenName || "LCL EXPORTS UNIT" === screenName) {
                            onload = "resetButtonColorForExport('" + screenName + "', '" + documentName + "','" + documentId + "');";
                        } else {
                            onload = "resetButtonColor('" + screenName + "', '" + documentName + "');";
                        }
                        Lightbox.showPopUp("Documents", url, "sexylightbox", "", callback, data, onload);
                    }
                });
            }
        }
    });
}
function changeButtonColor(screenName, documentName) {
    if (screenName.length > 3 && screenName.substring(0, 3) === 'LCL') {
        var button;
        if ("LCL SS MASTER BL" === screenName || "LCL EXPORTS UNIT" === screenName) {
            button = $("#scan-Attach" + $('#documentId').val(), window.parent.parent.document);
        } else {
            button = $("#scan-attach, #scan-Attach, #scanAttach,#lclSsMasterScanAttach" + documentName, window.parent.parent.document);
        }
        if (button.length > 0) {
            button.addClass("green-background");
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

