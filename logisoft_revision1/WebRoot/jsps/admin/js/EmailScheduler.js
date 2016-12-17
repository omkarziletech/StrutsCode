$(document).ready(function () {
    if ($("#startDate").length > 0) {
        initDate();
    }
    $("[title != '']").not("link").tooltip();
});
function previewPdf(path, fileLocation) {
    if ($('#isPrintPopUp').val() === "true") {
        if (fileLocation.includes(";")) {//  for Lcl-Exports HouseBatch pdf
            var multiFile = fileLocation.split(";");
            for (i = 0; i < multiFile.length; i++) {
                window.open(path + '/servlet/FileViewerServlet?fileName=' + multiFile[i], '', 'width=800,height=500,scrollbars=yes');
            }
        } else {
            window.open(path + '/servlet/FileViewerServlet?fileName=' + fileLocation, '', 'width=800,height=500,scrollbars=yes');
        }
    } else if (fileLocation.includes(";")) {// for Lcl-Exports HouseBatch pdf
        var multiFile = fileLocation.split(";");
        for (i = 0; i < multiFile.length; i++) {
            window.open(path + '/servlet/FileViewerServlet?fileName=' + multiFile[i], '', 'width=800,height=500,scrollbars=yes');
        }
    }
    else {
        window.parent.showLightBox('Report', path + '/servlet/FileViewerServlet?fileName=' + fileLocation, 700, 900);
    }
}
function viewEmailDetails(path, id) {
    GB_show("View Cover Page", path + "/emailSchedulers.do?methodName=viewEmailTranscations&emailId=" + id, 400, 900);
}
function sendMail() {
    var emailCheckId = [];
    $(".emailChecks:checked").each(function () {
        emailCheckId.push($(this).val());
    });
    if (emailCheckId.length <= 0) {
        $.prompt("Please select atleast one Email");
        return false;
    } else {
        $('#emailCheck').val(emailCheckId);
        submitForm("sendEmail");
    }
}
function initDate() {
    $("#startDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + $('#path').val() + "/images/icons/calendar-blue.gif' class='trigger' title='From Date'/>",
        onClose: function () {
            fromDateChange();
        }
    }).change(function () {
        fromDateChange();
    });
    $("#endDate").datepick({
        showOnFocus: false,
        showTrigger: "<img src='" + $('#path').val() + "/images/icons/calendar-blue.gif' class='trigger' title='To Date'/>",
        onClose: function () {
            toDateChange();
        }
    }).change(function () {
        toDateChange();
    });
}

function fromDateChange() {
    if ($.trim($("#startDate").val()) !== "" && !validateDate($("#startDate"))) {
        $.prompt("Please enter valid from date", {
            callback: function () {
                $("#startDate").val("").callFocus();
            }
        });
    }
}

function toDateChange() {
    if ($.trim($("#endDate").val()) !== "" && !validateDate($("#endDate"))) {
        $.prompt("Please enter valid to date", {
            callback: function () {
                $("#endDate").val("").callFocus();
            }
        });
    }
}
function openNewMailWindow(path) {
    GB_show('Mail Window', path + '/jsps/admin/Mail.jsp', 500, 650);
}
function searchMail() {
    if ($.trim($("#endDate").val()) === "" && $('#fileName').val() === "" && $('#userName').val() === "" && $.trim($("#startDate").val()) !== "") {
        $.prompt("Please enter valid End Date");
        $("#endDate").addClass("error-indicator");
        $("#warning").show();
    } else {
        submitForm("searchMail");
    }
}

function submitForm(methodName) {
    if ($('#isPrintPopUp').val() !== "true") {
        window.parent.showLoading();
    }
    $("#methodName").val(methodName);
    $("#emailSchedulerForm").submit();
}
function clearAllValues() {
    $('#startDate').val('');
    $('#endDate').val('');
    $('#toEmailOrFax').val('');
    $('#status').val('Completed');
    $('#fileName').val('');
    $('#userName').val('');
    $('#sendMails').hide();
    $('#emails').remove();
    $('#filterByName').val('SELECT');
}