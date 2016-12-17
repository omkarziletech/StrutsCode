$(document).ready(function() {
    $("[title != '']").not("link").tooltip();
});
function searchInvoice() {
    $('#methodName').val('search');
    $('#lclInvoicePoolForm').submit();
}
function openFile(path, fileId, screenName) {
    var moduleName = "";
    var moduleId = "";
    if (screenName === "LCLI DR") {
        moduleName = "Imports";
        moduleId='B';
    }
    window.parent.changeLclChilds(path, fileId, moduleId, moduleName,screenName);
}

function previewArInvoiceReport(path, buttonValue, invoiceId, unitSsId) {
    showLoading();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclPrintUtil",
            methodName: "lclArInvoiceReport",
            param1: buttonValue,
            param2: invoiceId,
            param3: unitSsId,
            param4: "No",
            request: "true"
        },
        success: function(data) {
            closePreloading();
            // viewFile(path, data);
            window.parent.showLightBox('Invoice', path + '/servlet/FileViewerServlet?fileName=' + data, 700, 1000);
        }
    });
}
function viewFile(path, file) {
    var win = window.open(path + '/servlet/PdfServlet?fileName=' + file, '_new', 'width=1000,height=650,toolbar=no,directories=no,status=no,linemenubar=no,scrollbars=no,resizable=no,modal=yes');
    window.onblur = function() {
        win.focus();
    }
}
function resetAllFeilds() {
    $('#status').val('');
    $('#orderByField').val('date');
    $('#fromDate').val('');
    $('#toDate').val('');
    $('#invoiceNumber').val('');
    $('#fileNumber').val('');
    $('#customerName').val('');
    $('#customerNumber').val('');
    for (var i = document.getElementById("invoicePool").rows.length; i > 0; i--) {
        document.getElementById("invoicePool").deleteRow(i - 1);
    }
}