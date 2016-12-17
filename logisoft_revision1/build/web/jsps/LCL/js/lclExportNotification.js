/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function addNew(path) {
    $("#voyageNo").val("");
    var href = path + "/exportNotification.do?methodName=display&buttonValue=notificationPopUp";
    $.colorbox({
        iframe: true,
        href: href,
        width: "50%",
        height: "75%",
        title: "Export Voyage Notification",
        onClosed: function () {
            searchNotificationList('displayNotificationList', '');
        }
    });
}

function getunitNo() {
    $("#voyageNoHiddenId").val($("#headerId").val());
}

function erasePopup() {
    parent.$.colorbox.close();
}

function viewReport(path, notificationId, methodName) {
    $("#notificationId").val(notificationId);
    $("#methodName").val(methodName);
    var params = $("#lclExportNotiFicationForm").serialize();
    $.post($("#lclExportNotiFicationForm").attr("action"), params, function (data) {
        if (data != null) {
            window.parent.showLightBox('Report', path + '/servlet/FileViewerServlet?fileName=' + data, 700, 900);
        }
    });
}


function sendDistribution(headerId, methodName) {
    if (headerId === '' || headerId === null) {
        $.prompt("Please Enter Voyage No");
        return false;
    }
    if ($("#voyageReasonId").val() === '') {
        $.prompt("Please Select Reason Code");
        $("#voyageReasonId").css("border", "1px solid red");
        return false;
    }
    showLoading();
    $("#methodName").val(methodName);
    $("#lclExportNotiFicationForm").submit();
    $("#voyageNo").val("");
    $("#unitNo").val("");

}
function sendNotification(headerId, methodName) {
    if (headerId === '' || headerId === null) {
        $.prompt("Please Enter Voyage No");
        return false;
    }
    if ($("#voyageReasonId").val() === '') {
        $.prompt("Please Select Reason Code");
        $("#voyageReasonId").css("border", "1px solid red");
        return false;
    }
    showLoading();
    $("#methodName").val(methodName);
    var params = $("#lclExportNotiFicationForm").serialize();
    $.post($("#lclExportNotiFicationForm").attr("action"), params, function (data) {
        if (data != null) {
            erasePopup();
            parent.$("#headerId").val($("#headerId").val());
            parent.$("#unitId").val($("#unitId").val());
        }
    });
}
function searchNotificationList(methodName, buttonValue) {
    var headerId = $("#voyageNo").val();
    if ((headerId === '' || headerId === null) && buttonValue === 'mainScreenSearch') {
        $.prompt("Please Enter Voyage No");
    } else {
        showLoading();
        $("#methodName").val(methodName);
        $("#lclExportNotiFicationForm").submit();
    }
}
function deleteNotification(methodName, notificationId, headerId, unitId) {
    var txt = "Are you sure you want to delete?";
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            NO: 2
        },
        submit: function (v) {
            if (v == 1) {
                showLoading();
                $("#headerId").val(headerId);
                $("#unitId").val(unitId);
                $("#notificationId").val(notificationId);
                $("#methodName").val(methodName);
                $("#lclExportNotiFicationForm").submit();
            } else {
                $.prompt.close();
            }
        }
    });

}