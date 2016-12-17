var path = "/" + window.location.pathname.split('/')[1];
$(document).ready(function () {
    if (($("#etaDate").val() === '' || $("etaDate").val() === null) &&
            ($("#etaToDate").val() === '' || $("#etaToDate").val() === '')) {
        setEmptyValues();
    }

    $(document).keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode === 13) {
            event.preventDefault();
            submitDoorDeliveryForm('search');
        }
    });
    });

function setEmptyValues() {
    if ($.trim($("#fileNumber").val()) !== "" || $.trim($('#voyageNo').val()) !== "" || $.trim($('#hblNo').val()) !== ""
            || $.trim($('#proNo').val()) !== "") {
        $("#etaDate").val('');
        $("#etaToDate").val('');
    } else {
        var m_names = new Array("Jan", "Feb", "Mar",
                "Apr", "May", "Jun", "Jul", "Aug", "Sep",
                "Oct", "Nov", "Dec");
        var d = new Date();
        var curr_date = d.getDate();
        var curr_month = d.getMonth();
        var curr_year = d.getFullYear();
        $("#etaToDate").val(curr_date + "-" + m_names[curr_month] + "-" + curr_year);
        d.setDate(d.getDate() - 30);
        curr_date = d.getDate();
        curr_month = d.getMonth();
        curr_year = d.getFullYear();
        $("#etaDate").val(curr_date + "-" + m_names[curr_month] + "-" + curr_year);
    }
}
function checkForNumberOnly(obj) {
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        sampleAlert("This field should be Numeric");
        obj.value = "";
    }
}
function submitDoorDeliveryForm(methodName) {
    var fromEtadate = $('#etaDate').val();
    var toEtadate = $('#etaToDate').val();
    if ($.trim($("#fileNumber").val()) !== "" || $.trim($('#voyageNo').val()) !== "" || $.trim($('#hblNo').val()) !== ""
            || $.trim($('#proNo').val()) !== "") {
        $("#methodName").val(methodName);
        $("#lclDoorDeliverySearchForm").submit();
        window.parent.showPreloading();

    } else {
        if ($('#dispositionCode').val() === "") {
            $('#dispositionId').val('');
        }
        if (fromEtadate !== "" && toEtadate === "") {
            $.prompt("Please fill the TO ETA Date");
            $("#etaToDate").css("border-color", "red");
        } else if (fromEtadate === "" && toEtadate !== "") {
            $.prompt("Please fill the FROM ETA Date");
            $("#etaDate").css("border-color", "red");
        } else {
            $("#methodName").val(methodName);
            $("#lclDoorDeliverySearchForm").submit();
            window.parent.showPreloading();
        }
    }
}

function exportToExcel() {
    $("#methodName").val('exportToExcel');
    var params = "action=createExcel";
    var params = $("#lclDoorDeliverySearchForm").serialize();
    window.parent.showPreloading();
    $.post($("#lclDoorDeliverySearchForm").attr("action"), params, function (fileName) {
        if (fileName != null) {
            var title = fileName.split("/").pop();
            window.parent.closePreloading();
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
    });
  
}


function clearValues() {
    setEmptyValues();
    $('#limit').val('100');
    for (var i = document.getElementById("result-table").rows.length; i > 0; i--) {
        document.getElementById("result-table").deleteRow(i - 1);
    }
    $("#voyageNo").val('');
    $("#pol").val('');
    $("#pod").val('');
    $("#dispositionCode").val('');
    $("#dispositionId").val('');
    $("#fileNumber").val('');
    $("#hblNo").val('');
    $("#customsClearanceReceived").val('');
    $("#scacCode").val('');
    $("#doorDeliveryStatus").val('');
    $("#proNo").val('');
    $("#lastFreeDate").val('');
    $("#estimatePickupDate").val('');
    $("#actualPickupDate").val('');
    $("#estimatedDeliveryDate").val('');
    $("#actualDeliveryDate").val('');
    $("#commercialDelivery").val('');
    $("#liftGate").val('');
    $("#hazmat").val('');
    $("#deliveryProof").val('');
    $("#polCountryCode").val('');
    $("#podCountryCode").val('');
    $("#dispositionId").val('');
    $("#result-header").val('');
    $("#deliveryOrder").val('');
    $("#methodName").val("clearValues");
    $("#lclDoorDeliverySearchForm").submit();
    window.parent.showPreloading();
}

function checkLock(path, fileNumber, fileId, moduleId, moduleName, transhipment, callBack) {
    $("#methodName").val("checkLocking");
    $('#fileNumber').val(fileNumber);
    var params = $("#lclDoorDeliverySearchForm").serialize();
    params += "&moduleId=" + moduleId;
    $.post($("#lclDoorDeliverySearchForm").attr("action"), params, function (data) {
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
                submit: function (v) {
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
    window.parent.changeLclChilds(path, fileId, moduleId, moduleName, callBack);
}
function doSort(val) {
    var sortBy = jQuery("#sortBy").val();
    var toggleValue = sortBy === "up" ? "down" : "up";
    jQuery("#" + val).removeClass(sortBy).addClass(toggleValue);
    jQuery("#orderBy").val(val);
    jQuery("#sortBy").val(toggleValue);
    $("#methodName").val("doSort");
    $("#lclDoorDeliverySearchForm").submit();
}

function displayNotesPopUp(path, fileNumberId, fileNumber, moduleName) {
    var href = path + "/lclRemarks.do?methodName=display&fileNumberId=" + fileNumberId +
            "&fileNumber=" + fileNumber + "&moduleName=" + moduleName + "&actions=manualNotes&moduleId=Booking";
    $(".notes").attr("href", href);
    $(".notes").colorbox({
        iframe: true,
        width: "65%",
        height: "75%",
        title: "Notes",
        scrolling: false
    });
}
