
function initPickaDay() {
    var sDate = $("#sDateHidden").val();
    var eDate = $("#eDateHidden").val();
    var now = new Date();
    now.setHours(0, 0, 0, 0);
    var date = new Date(now);
    sDate = date.setMonth(date.getMonth() - 1);
    sDate = date.toString('dd-MMM-yyyy HH:mm:ss');
    eDate = now.toString('dd-MMM-yyyy 23:59:59');
    $("#startDate").pikaday({
        defaultDate: now
    }).change(function () {
        var date = Date.parse($(this).val());
        if (date !== null) {
            $(this).val(date.toString('dd-MMM-yyyy HH:mm:ss'));
        } else {
            $(this).val('');
        }
    });
    $("#endDate").pikaday({
        defaultDate: now
    }).change(function () {
        var endDate = $(this).val() + " 23:59:59";
        var date = Date.parse(endDate);
        if (date !== null) {
            $(this).val(date.toString('dd-MMM-yyyy HH:mm:ss'));
        } else {
            $(this).val('');
        }
    });
    if ($("#action").val() !== "search") {
        $("#startDate").val(sDate !== '' ? sDate : formattedDate);
        $("#endDate").val(eDate !== '' ? eDate : formattedDate);
    }
}
$(document).ready(function () {
    initPickaDay();
});

function doClear() {
    $("#bkgNo").val("");
    $("#startDate").val("");
    $("#endDate").val("");
    $("#sortBy").val("");
    $("#bookingId").val("");
    $("#searchBy").val("");
    $("#action").val("reset");
    $("#searchForm").submit();
    window.parent.showPreloading();
}
$(document).keypress(function (e) {
    if (e.which === 13) {
        search();
        return false;
    }
});

function search() {
    $("#searchBy").val("value");
    $("#action").val('search');
    $("#searchForm").submit();
    window.parent.showPreloading();
}

function exportToExcel() {
    $("#action").val('exportToExcel');
    $("#searchForm").submit();
}

function showAll() {
    $("#bkgNo").val("");
    $("#startDate").val("");
    $("#endDate").val("");
    $("#sortBy").val("");
    $("#bookingId").val("");
    $("#searchBy").val("all");
    $("#action").val('display');
    $("#searchForm").submit();
    window.parent.showPreloading();
}
function showFile(path, type, id) {
    var url = path + "/kn/search.do?action=showFile&id=" + id + "&type=" + type;
    var xmlWindow = window.open(url, "", "left=250,top=30,width=700,height=600");
    xmlWindow.focus();
}

function reProcess(id) {
    $("#bookingEnvelopeId").val(id);
    $("#action").val('reProcess');
    document.searchForm.submit();
    window.parent.showPreloading();
}
function showPDF(path, id) {
    window.parent.showLightBox('', path + "/kn/search.do?action=showPDF&id=" + id, 800, 1000);
    closePreloading();
}
function doSort(sortBy) {
    var searchType = $("#searchType").val();
    var toggleValue = searchType === "up" ? "down" : "up";
    $("#" + sortBy).removeClass(searchType).addClass(toggleValue);
    $("#sortBy").val(sortBy);
    $("#searchType").val(toggleValue);
    $("#action").val('search');
    $("#searchForm").submit();
}

function exportToExcel() {
    var path = "/" + window.location.pathname.split('/')[1];
    $.ajaxx({
        url: path + "/kn/search.do",
        data: {
            action: "exportToExcel"
        },
        preloading: true,
        success: function (data) {
            window.open(path + "/servlet/FileViewerServlet?fileName=" + data, "", "resizable=1,location=1,status=1,scrollbars=1, width=600,height=400");
        }
    });
}
