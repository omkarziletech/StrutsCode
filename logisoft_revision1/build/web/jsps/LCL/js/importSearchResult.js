/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function setResultHeight() {
    if ($(".scrollable-table").length > 0) {
        var windowHeight = window.parent.getFrameHeight();
        var height = windowHeight;
        height -= 50;
        $(".scrollable-table").height(height);
        $("body").css("overflow-y", "hidden");
    }
}
function showAllcities(){
    var chec= $("#destinationAllcities").is(':checked');
    if (chec){
        $("#destination").attr("alt", "IMPORT_MAIN_SCREEN_DESTINATION_AllCITIES");
    }else{
        $("#destination").attr("alt", "IMPORT_MAIN_SCREEN_DESTINATION");
    }
 
}

$(document).ready(function () {
    if (($("#from-date").val() === '' || $("#from-date").val() === null) &&
            ($("#endDate").val() === '' || $("#endDate").val() === '')) {
        searchEmptyDateValues();
    }

    $(document).keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode === 13) {
            event.preventDefault();
            submitForm('search');
        }
    });

    if ($("#isSingleFile").val() === "true") {
        $('.file-no').trigger('click');
    }
    setResultHeight();
    $(window).resize(function () {
        window.parent.changeHeight();
        setResultHeight();
    });
});

function submitBookingForm(methodName, fileNumber) {
    window.location = 'lclBooking.do?methodName=' + methodName + '&fileNumber=' + fileNumber;
    return false;
}

function openTrackingPopUp(path, methodName, filenumber, filenumberId) {
    var href = path + '/lclTracking.do?methodName=' + methodName + '&fileNumber=' + filenumber + '&fileId=' + filenumberId;
    $(".track").attr("href", href);
    $(".track").colorbox({
        iframe: true,
        width: "65%",
        height: "65%",
        title: "Tracking"
    });
}

function backSearch() {
    $("#methodName").val("goBackToMainScreen");
    $("#lclSearchForm").submit();
}

function submitResetForm() {
    $('#fileNumber').val('');
    $('#client').val('');
    $('#issuingTerminal').val('');
    $('#shipperName').val('');
    $('#destination').val('');
    $('#consignee').val('');
    $('#bookedBy').val('');
    $('#inbondNo').val('');
    $('#origin').val('');
    $('#pol').val('');
    $('#containerNo').val('');
    $('#pod').val('');
    $('#ssl').val('');
    $('#createdBy').val('');
    $('#masterBl').val('');
    $('#limit').val('100');
    $('#destinationRegion').val('');
    $('#originRegion').val('');
    $('#foreignAgent').val('');
    $('#foreignAgentAccount').val('');
    $('#portName').val('');
    $('#destinationName').val('');
    $('#consigneeNo').val('');
    $('#clientNo').val('');
    $('#polName').val('');
    $('#shipperNo').val('');
    $('#podName').val('');
    $('#customerPo').val('');
    $('#trackingNo').val('');
    $('#warehouseDocNo').val('');
    $('#countrycode').val('');
    $('#polCountryCode').val('');
    $('#podCountryCode').val('');
    $('#destCountryCode').val('');
    $('#sortBy').val('');
    $('#subHouse').val('');
    $('#amsHBL').val('');
    $('#blcreatedBy').attr('checked', false);
    $('#blbookedBy').attr('checked', false);
    $('#includeIntr').attr('checked', false);
    $('#metric').attr('checked', true);
    $('#filterBy').val('All');
    $('#metric').attr('checked', true);
    $('#salesCode').val('');
    $('#ipiLoadNo').val('');
    searchEmptyDateValues();
    hideProgressBar();
}

function submitForm(methodName) {
    if ((methodName === 'search')
            && (($("#fileNumber").val().length !== 0 && $("#fileNumber").val().length < 3)
                    || (typeof $("#warehouseDocNo").val() !== 'undefined' && $("#warehouseDocNo").val().length !== 0 && $("#warehouseDocNo").val().length < 3)
                    || (typeof $("#subHouse").val() !== 'undefined' && $("#subHouse").val().length !== 0 && $("#subHouse").val().length < 3)
                    || (typeof $("#customerPo").val() !== 'undefined' && $("#customerPo").val().length !== 0 && $("#customerPo").val().length < 3)
                    || (typeof $("#amsHBL").val() !== 'undefined' && $("#amsHBL").val().length !== 0 && $("#amsHBL").val().length < 3)
                    || (typeof $("#masterBl").val() !== 'undefined' && $("#masterBl").val().length !== 0 && $("#masterBl").val().length < 3)
                    || (typeof $("#trackingNo").val() !== 'undefined' && $("#trackingNo").val().length !== 0 && $("#trackingNo").val().length < 3)
                    || (typeof $("#containerNo").val() !== 'undefined' && $("#containerNo").val().length !== 0 && $("#containerNo").val().length < 3)
                    || (typeof $("#createdBy").val() !== 'undefined' && $("#createdBy").val().length !== 0 && $("#createdBy").val().length < 3)
                    || (typeof $("#bookedBy").val() !== 'undefined' && $("#bookedBy").val().length !== 0 && $("#bookedBy").val().length < 3)
                    || (typeof $("#inbondNo").val() !== 'undefined' && $("#inbondNo").val().length !== 0 && $("#inbondNo").val().length < 3)
                    || (typeof $("#ipiLoadNo").val() !== 'undefined' && $("#ipiLoadNo").val().length !== 0 && $("#ipiLoadNo").val().length < 3))) {
        return;
    } else if ($.trim($("#fileNumber").val()) === "" && $.trim($('#warehouseDocNo').val()) === "" && $.trim($('#subHouse').val()) === ""
            && $.trim($('#trackingNo').val()) === "" && $.trim($('#inbondNo').val()) === "" && $.trim($('#customerPo').val()) === ""
            && $.trim($('#amsHBL').val()) === "" && $.trim($('#masterBl').val()) === "" && $.trim($('#containerNo').val()) === ""
            && $.trim($('#sslineNo').val()) === "" && $.trim($('#ipiLoadNo').val()) === "") {
        var fromdate = $('#from-date').val();
        var todate = $('#endDate').val();
        if (fromdate === "" && todate === "") {
            hideProgressBar();
            $.prompt("Please Enter From and End Date");
            $("#from-date").css("border-color", "red");
            $("#endDate").css("border-color", "red");
            $("#warning").parent.show();
            return false;
        } else if (fromdate === "") {
            hideProgressBar();
            $.prompt("Please Enter From date");
            $("#from-date").css("border-color", "red");
            $("#warning").parent.show();
            return false;
        } else if (todate === "") {
            hideProgressBar();
            $.prompt("Please fill the toDate");
            $("#endDate").css("border-color", "red");
            $("#warning").parent.show();
            return false;
        } else {
            window.parent.showLoading();
            $("#methodName").val(methodName);
            $("#lclSearchForm").submit();
        }
    } else {
        searchEmptyDateValues();
        window.parent.showLoading();
        $("#methodName").val(methodName);
        $("#lclSearchForm").submit();
    }
}
function enableInventoryCheckbox() {
    if ($('#filterBy').val() === "IWB") {
        $('#filterByInventory').attr('disabled', false);
        $('#filterByInventory').attr('checked', true);
    } else {
        $('#filterByInventory').attr('disabled', true);
        $('#filterByInventory').attr('checked', false);
    }
}

function searchEmptyDateValues() {
    if ($.trim($("#fileNumber").val()) !== "" || $.trim($('#warehouseDocNo').val()) !== "" || $.trim($('#subHouse').val()) !== ""
            || $.trim($('#trackingNo').val()) !== "" || $.trim($('#inbondNo').val()) !== "" || $.trim($('#customerPo').val()) !== ""
            || $.trim($('#amsHBL').val()) !== "" || $.trim($('#masterBl').val()) !== "" || $.trim($('#containerNo').val()) !== ""
            || $.trim($('#sslineNo').val()) !== "" || $.trim($('#ipiLoadNo').val()) !== "") {
        $("#from-date").val('');
        $("#endDate").val('');
        document.getElementById("filterBy").value = "";
        $('#filterByInventory').attr('disabled', true);
        $('#filterByInventory').attr('checked', false);
    } else {
        var m_names = new Array("Jan", "Feb", "Mar",
                "Apr", "May", "Jun", "Jul", "Aug", "Sep",
                "Oct", "Nov", "Dec");
        var d = new Date();
        var curr_date = d.getDate();
        var curr_month = d.getMonth();
        var curr_year = d.getFullYear();
        $("#endDate").val(curr_date + "-" + m_names[curr_month] + "-" + curr_year);
        d.setDate(d.getDate() - 30);
        curr_date = d.getDate();
        curr_month = d.getMonth();
        curr_year = d.getFullYear();
        $("#from-date").val(curr_date + "-" + m_names[curr_month] + "-" + curr_year);
        document.getElementById("filterBy").value = "All";
    }
}

function checkFileNumber(path, fileNumberId, moduleId) {
    var moduleName = $('#moduleName').val();
    window.parent.changeLclChilds(path, fileNumberId, moduleId, moduleName);
}

function openLclUserDefaults(path) {
    var href = path + "/lclImportSearch.do?methodName=displayLclSearchScreen";
    document.location.href = href;
}

function openTemplate(path) {
    var href = path + "/lclTemplate.do?methodName=display";
    $(".template").attr("href", href);
    $(".template").colorbox({
        iframe: true,
        width: "80%",
        height: "80%",
        title: "Template"
    });
}

function doSortAscDesc(sortByValue) {
    var searchType = $("#searchType").val();
    var toggleValue = searchType === "up" ? "down" : "up";
    $("#" + sortByValue).removeClass(searchType).addClass(toggleValue);
    $("#sortByValue").val(sortByValue);
    $("#searchType").val(toggleValue);
    $("#methodName").val("doSortAscDesc");
    $("#lclSearchForm").submit();
}

function showClientSearchOption(path, searchByValue) {
    var href = path + "/lclBooking.do?methodName=clientSearch&searchByValue=" + searchByValue;
    $(".clientSearchEdit").attr("href", href);
    $(".clientSearchEdit").colorbox({
        iframe: true,
        width: "35%",
        height: "40%",
        title: searchByValue + " Search"
    });
}

function defaultLoginNameCreatedBy() {
    var login = $('#login').val();
    if ($('#blcreatedBy').is(":checked")) {
        $('#createdBy').val(login);
        allFilter();
    } else {
        $('#createdBy').val('');
        if ($('#blbookedBy').is(":checked")) {
            $('#bookedBy').val(login);
            allFilter();
        } else {
            $('#bookedBy').val('');
            inventoryAllFilter();
        }
    }
}
function defaultLoginNameBookedBy() {
    var login = $('#login').val();
    if ($('#blbookedBy').is(":checked")) {
        $('#bookedBy').val(login);
        allFilter();
    } else {
        $('#bookedBy').val('');
        if ($('#blcreatedBy').is(":checked")) {
            $('#createdBy').val(login);
            allFilter();
        } else {
            $('#bookedBy').val('');
            inventoryAllFilter();
        }
    }
}

function inventoryAllFilter() {
    document.getElementById("filterBy").value = "IWB";
    $('#filterByInventory').attr('disabled', false);
    $('#filterByInventory').attr('checked', true);
}
function allFilter() {
    document.getElementById("filterBy").value = "All";
    $('#filterByInventory').attr('disabled', true);
    $('#filterByInventory').attr('checked', false);
}

function enableInventoryCheckbox() {
    if ($('#filterBy').val() === 'IWB') {
        $('#filterByInventory').attr('disabled', false);
        $('#filterByInventory').attr('checked', true);
    } else {
        $('#filterByInventory').attr('disabled', true);
        $('#filterByInventory').attr('checked', false);
    }
}

function showFilterOptions(path) {
    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclPrintUtil",
            methodName: "lclFilterOptionReport",
            request: "true"
        },
        success: function (data) {
            window.parent.showLightBox('Filter Details', path + '/servlet/FileViewerServlet?fileName=' + data, 700, 800);
        }
    });
}


function validateSalescode(code) {
    var codeValue = code.value.replace(/,/g, '');
    code.value = codeValue.match(new RegExp('.{1,' + 2 + '}', 'g'));
}