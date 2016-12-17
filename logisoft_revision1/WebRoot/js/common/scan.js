/* 
 * Document   : scan
 * Created on : Sep 25, 2015, 05:32:27 PM
 * Author     : Lakshmi Naryanan
 */
var path = "/" + window.location.pathname.split('/')[1];

var DWObject;
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

function goBack() {
    $("#action").val("search");
    $("#scanForm").submit();
}

function OnHttpUploadSuccess() {
    changeButtonColor($("#screenName").val(), $("#documentName").val());
    goBack();
}

function OnHttpUploadFailure() {
    if (DWObject.HTTPPostResponseString === "success") {
        changeButtonColor($("#screenName").val(), $("#documentName").val());
        goBack();
    } else {
        $.prompt(DWObject.ErrorString + " : " + DWObject.HTTPPostResponseString);
    }
}

function uploadFile() {
    var strHTTPServer = location.hostname;
    DWObject.HTTPPort = location.port === "" ? 80 : location.port;
    var currentPathName = unescape(location.pathname);
    var currentPath = currentPathName.substring(0, currentPathName.lastIndexOf("/") + 1);
    var strActionPage = currentPath + "scan.do?action=upload";
    var fileName = "document.pdf";
    DWObject.ClearAllHTTPFormField();
    DWObject.HttpFieldNameOfUploadedImage = "document";
    DWObject.SetHTTPFormField("screenName", $("#screenName").val());
    DWObject.SetHTTPFormField("documentId", $("#documentId").val());
    DWObject.SetHTTPFormField("documentName", $("#documentName").val());
    DWObject.SetHTTPFormField("documentType", $("#documentType").val());
    DWObject.SetHTTPFormField("comment", $("#comment").val());
    DWObject.SetHTTPFormField("status", $("#status").val());
    DWObject.SetHTTPFormField("bookingType", $("#bookingType").val());
    DWObject.SetHTTPFormField("hiddenScreenName", $("#hiddenScreenName").val());
    DWObject.HTTPUploadAllThroughPostAsPDF(strHTTPServer, strActionPage, fileName, OnHttpUploadSuccess, OnHttpUploadFailure);
}

function acquireImage() {
    DWObject = Dynamsoft.WebTwainEnv.GetWebTwain('dwtcontrolContainer');
    if (DWObject) {
        if (DWObject.SourceCount > 0) {
            DWObject.OpenSource();
            DWObject.RemoveAllImages();
            DWObject.IfShowUI = false;
            DWObject.IfDisableSourceAfterAcquire = true;
            DWObject.AcquireImage();
            DWObject.RegisterEvent('OnPostAllTransfers', function () {
                if (DWObject.HowManyImagesInBuffer > 0) {
                    uploadFile();
                } else {
                    $.prompt("Please load paper in the scanner and try again.");
                    DWObject.CloseSource();
                }
            });
        } else {
            $.prompt("No Scanner detected. Please check scanner connection and try again.");
        }
    }
}

Dynamsoft.WebTwainEnv.RegisterEvent('OnWebTwainReady', function () {
    acquireImage();
});

