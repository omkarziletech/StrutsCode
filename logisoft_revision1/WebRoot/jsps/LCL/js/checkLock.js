function checkLock(path, fileNumber, fileId, moduleId, moduleName, transhipment, callBack) {
    $("#methodName").val("checkLocking");
    $('#fileNumber').val(fileNumber);
    $('#userId').val($("#userId").val());
    var params = $("#lclSearchForm").serialize();
    params += "&moduleId=" + moduleId;
    $.post($("#lclSearchForm").attr("action"), params, function(data) {
        if (data === 'available') {
            callBackMethod(path, fileId, moduleId, moduleName, transhipment, callBack);
        } else if (data.indexOf("is already opened in another window") > -1) {
            $.prompt(data);
        } else {
            $.prompt(data + ". Do you want to view the file?", {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function(v) {
                    if (v === 1) {
                        callBackMethod(path, fileId, moduleId, moduleName, transhipment, callBack);
                    } else if (v === 2) {
                        $.prompt.close();
                    }
                }
            });
        }
    });
}
function callBackMethod(path, fileId, moduleId, moduleName, transhipment, callBack) {
    callBack = transhipment === "false" ? "" : callBack;
    window.parent.changeLclChilds(path, fileId, moduleId, moduleName, callBack);
}
function checkFileNumber(path, fileId, moduleId, moduleName, callBack) {
    window.parent.changeLclChilds(path, fileId, moduleId, moduleName, callBack);
}