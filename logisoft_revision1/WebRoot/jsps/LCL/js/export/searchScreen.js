jQuery(document).ready(function () {
    $('#moduleNameId').val($('#moduleNameId').val() === "" ? "Exports" : $('#moduleNameId').val());
    $(document).keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode === 13) {
            event.preventDefault();
            searchValidation('search');
        }
    });

    if (($("#from-date").val() === '' || $("#from-date").val() === null) &&
            ($("#endDate").val() === '' || $("#endDate").val() === '')) {
        setEmptyDateValuesByField();
    }
    $("#orderBy").val('BDesc');

    var fileterBy = $('#filterBy').val();
    if (fileterBy === "IWB" || fileterBy === "BL" || fileterBy === "IPO") {
        $('#filterByInventory').attr('disabled', false);
        //$('#filterByInventory').attr('checked', true);
    } else {
        $('#filterByInventory').attr('disabled', true);
        $('#filterByInventory').attr('checked', false);
    }
    if (fileterBy !== "IWB") {
        $('#includeIntr').attr('checked', false);
    }
});

function setEmptyDateValuesByField() {
    if ($("#fileNumber").val() !== "" || $('#containerNo').val() !== "" || $('#inbondNo').val() !== ""
            || $('#masterBl').val() !== "" || $('#trackingNo').val() !== "" || $('#warehouseDocNo').val() !== ""
            || $('#customerPo').val() !== "" || $('#sslBookingNo').val() !== "") {
        $("#from-date").val('');
        $("#endDate").val('');
        $("#filterBy").val('All');
        $('#filterByInventory').attr('checked', false);
        $('#filterByInventory').attr('disabled', true);
    } else {
        // $('#filterByInventory').attr('checked', true);
        $('#filterByInventory').attr('disabled', false);
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
    }

}
// Search Filter by consignee & shipper
function searchFilter(path, searchByValue) {
    var href = path + "/lclBooking.do?methodName=clientSearch&searchByValue=" + searchByValue;
    $(".clientSearchEdit").attr("href", href);
    $(".clientSearchEdit").colorbox({
        iframe: true,
        width: "35%",
        height: "40%",
        title: searchByValue + " Search"
    });
}
//Create Quick Bkg
function createQuickBkg(path) {
    var href = path + "/lclBooking.do?methodName=createQuickBkg&moduleName=Exports";
    $('#quickdr').attr("href", href);
    $('#quickdr').colorbox({
        iframe: true,
        width: "75%",
        height: "75%",
        title: "<span  style=color:blue>Quick Booking</span>"
    });
}
//Apply Defaults
function applyUserDefaultValue(path) {
    window.parent.showLoading();
    var href = path + "/lclSearch.do?methodName=applyExportUserDefaultValues&lclDefaultId=" + $("#lclDefaultName").val();
    document.location.href = href;
}

//function searchAfterApply() {
//    var flag = $("#applyandsearch").attr("checked", true) ? true : false;
//    if ($("#lclDefaultName").val() === '') {
//        $.prompt("Please Select Atleast One Lcl Default Name");
//        $("#lclDefaultName").css("border-color","red");
//        $("#applyandsearch").attr("checked", false); 
//        return false;
//    } else if (flag) {
//        window.parent.showLoading();
//        $("#limit").val($("#limit").val());
//        $("#lclDefaultId").val($("#lclDefaultName").val());
//        $("#searchAndApply").val(flag);
//        $("#methodNameId").val('applyExportUserDefaultValues');
//        $("#lclSearchForm").submit();
//    }
//}
//Create a new Quote for Existing fileNo
function newcopyQuoteForm(path) {
    var qtFileNo = $("#copyQuote").val();
    if ((qtFileNo.length !== 0 && qtFileNo.length < 3)) {
        return;
    }
    if (qtFileNo != null && qtFileNo != "") {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO",
                methodName: "getFileState",
                param1: qtFileNo
            },
            async: false,
            success: function (data) {
                if (data === 'Q') {
                    window.parent.showLoading();
                    var href = path + "/lclQuote.do?methodName=copyQuote&fileNumber=" + qtFileNo + "&copyFnVal=Y&moduleName=Exports";
                    document.location.href = href;
                } else {
                    $.prompt("Please Select existing Quote FileNumber");
                }
            }
        });
    } else {
        $("#copyQuote").css("border-color", "red");
        $.prompt("Please Enter Quote File Number");
    }
}
//create a new bkg for existing fileNo
function newCopyBkgForm(path) {
    var bkgFileNo = $("#copyBooking").val();
    if ((bkgFileNo.length !== 0 && bkgFileNo.length < 3)) {
        return;
    }
    var moduleName = $('#moduleNameId').val();
    if (bkgFileNo != null && bkgFileNo != "") {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO",
                methodName: "getFileState",
                param1: bkgFileNo
            },
            async: false,
            success: function (data) {
                if (data === 'B') {
                    window.parent.showLoading();
                    var href = path + "/lclBooking.do?methodName=copyDr&fileNumber=" + bkgFileNo +
                            "&copyFnVal=Y&moduleName=" + moduleName + "&allowVoyageCopy=1";
                    document.location.href = href;
                } else {
                    $.prompt("Please Select existing Booking FileNumber");
                }
            }
        });
    } else {
        $("#copyBooking").css("border-color", "red");
        $.prompt("Please Enter Booking File Number");
    }
}
//printReport method changed due to change in FileName
function filterOptionReport(path) {
    jQuery.ajaxx({
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
//Filter by
function filterOptionEnable() {
    var fileterBy = $('#filterBy').val();
    if (fileterBy === "IWB" || fileterBy === "BL" || fileterBy === "IPO") {
        $('#filterByInventory').attr('disabled', false);
        //$('#filterByInventory').attr('checked', true);
    } else {
        $('#filterByInventory').attr('disabled', true);
        $('#filterByInventory').attr('checked', false);
    }
    if (fileterBy !== "IWB") {
        $('#includeIntr').attr('checked', false);
    }
}


//Create New File for quote,bkg
function createNewFile(path, moduleId) {
    var moduleName = $('#moduleNameId').val();
    window.parent.changeLclChilds(path, '', moduleId, moduleName);
}

function submitResetForm() {
    $('#fileNumber').val('');
    $('#client').val('');
    $('#sailDate').val('');
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
    $('#forwarder').val('');
    $('#createdBy').val('');
    $('#sslBookingNo').val('');
    $('#masterBl').val('');
    $('#limit').val('100');
    $('#sortBy').val('');
    $('#destinationRegion').val('');
    $('#originRegion').val('');
    $('#cfcl').val('0');
    $('#cfclAccountNo').val('');
    $('#foreignAgent').val('');
    $('#foreignAgentAccount').val('');
    $('#cfclAccount').val('');
    $('#portName').val('');
    $('#forwarderNo').val('');
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
    $('#copyQuote').val('');
    $('#copyBooking').val('');
    $('#currentLocation').val('');
    $('#blPoolOwner').val('');
    $('#currentLocName').val('');
    $('#currentLocCode').val('');
    $('#subHouse').val('');
    $('#amsHBL').val('');
    $('#blcreatedBy').attr('checked', false);
    $('#blOwner').attr('checked', false);
    $('#blbookedBy').attr('checked', false);
    $('#includeIntr').attr('checked', false);
    $('#metric').attr('checked', true);
    $('#filterBy').val('IWB');
    $('#imperial').attr('checked', true);
    $('#orderBy').val('BDesc');
    setEmptyDateValuesByField();
}

function searchValidation(methodName) {
    if ((methodName === 'search')
            && (($("#fileNumber").val().length !== 0 && $("#fileNumber").val().length < 3)
                    || ($("#cfclAccount").val().length !== 0 && $("#cfclAccount").val().length < 3)
                    || ($("#warehouseDocNo").val().length !== 0 && $("#warehouseDocNo").val().length < 3)
                    || ($("#masterBl").val().length !== 0 && $("#masterBl").val().length < 3)
                    || ($("#sslBookingNo").val().length !== 0 && $("#sslBookingNo").val().length < 3)
                    || ($("#inbondNo").val().length !== 0 && $("#inbondNo").val().length < 3)
                    || ($("#customerPo").val().length !== 0 && $("#customerPo").val().length < 3)
                    || ($("#containerNo").val().length !== 0 && $("#containerNo").val().length < 3)
                    || ($("#createdBy").val().length !== 0 && $("#createdBy").val().length < 3)
                    || ($("#bookedBy").val().length !== 0 && $("#bookedBy").val().length < 3)
                    || ($("#trackingNo").val().length !== 0 && $("#trackingNo").val().length < 3))) {
        return;
    }
    var voyNo = $('#bookedForVoyage').val();
    if (voyNo != undefined && voyNo != null && voyNo != "") {
        var bkVoyNo = voyNo.split('/');
        $('#bookedForVoyage').val(bkVoyNo[0]);
    }
    if (document.getElementById('filterBy').value === "IWB" || document.getElementById('filterBy').value === "IPO") {
        $("#methodNameId").val("getCompanyName");
        var params = $("#lclSearchForm").serialize();
        var foreignAgent = $("#foreignAgent").val();
        $.post($("#lclSearchForm").attr("action"), params, function (data) {
            if (data === 'OTI' && foreignAgent === "") {
                $.prompt("Foreign Agent is required ");
                $("#foreignAgent").css("border-color", "red");
                $("#warning").show();
            } else if (data === 'OTI' && foreignAgent != "") {
                search(methodName);
            } else if ($('#includeIntr').is(':checked') && $("#currentLocation").val() == '') {
                $.prompt("Please Enter Current Location");
                $("#currentLocation").css("border-color", "red");
                $("#warning").show();
            } else if ($('#fileNumber').val() === "" && $('#portName').val() == "" &&
                    $('#client').val() == "" && $('#polName').val() == "" &&
                    $('#issuingTerminal').val() == "" && $('#containerNo').val() == "" &&
                    $('#shipperNo').val() == "" && $('#podName').val() == "" &&
                    document.lclSearchForm.originRegion.value == "" && $('#ssl').val() == "" &&
                    document.lclSearchForm.destinationRegion.value == "" && $('#forwarderNo').val() == "" &&
                    $('#destinationName').val() == "" && $('#createdBy').val() == "" &&
                    $('#consigneeNo').val() == "" && $('#sslBookingNo').val() == "" &&
                    $('#bookedBy').val() == "" && $('#masterBl').val() == "" &&
                    $('#inbondNo').val() == "" && $('#foreignAgent').val() == "" && $("#currentLocation").val() === "") {
                $.prompt("Atleast One Search Criteria must be selected for this filter");
            } else {
                search(methodName);
            }
        });
    } else {
        search(methodName);
    }
}
function cfcl_AccttypeCheck() {
    if ($('#disabledAccount').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#forwardAccount').val() + "</span>");
        $('#cfclAccount').val('');
    }
}
function search(methodName) {
    if ($('#fileNumber').val() === "" && $('containerNo').val() === "") {
        var fromdate = $('#from-date').val();
        var todate = $('#endDate').val();
        if (fromdate === "" && todate === "") {
            $.prompt("Please Enter From and End Date");
            $("#from-date").css("border-color", "red");
            $("#endDate").css("border-color", "red");
            $("#warning").parent.show();
        }
        if (fromdate === "") {
            $.prompt("Please Enter From date");
            $("#from-date").css("border-color", "red");
            $("#warning").parent.show();
        }
        else if (todate === "") {
            $.prompt("Please fill the toDate");
            $("#endDate").css("border-color", "red");
            $("#warning").parent.show();
        }
    } else {
        window.parent.showLoading();
        $("#methodNameId").val(methodName);
        $("#lclSearchForm").submit();
    }
}
//Create New Template
function createTemplate(path) {
    var originalClose = $.colorbox.close;
    $.colorbox.close = function () {
        var $frame = $(".cboxIframe").contents();
        if ($frame.find("#lclTemplateForm").serialize() !== $frame.find("#loadtemplateForm").val()) {
            $frame.find("body").append(
                    $.prompt("Some fields have been modified, if you exit your changes will not be saved.", {
                        buttons: {
                            Yes: true,
                            No: false
                        },
                        submit: function (v) {
                            if (v) {
                                originalClose();
                            }
                        }
                    }));
        } else {
            originalClose();
        }
    }
    var href = path + "/lclTemplate.do?methodName=display&moduleName=Exports";
    $.colorbox({
        iframe: true,
        href: href,
        width: "70%",
        height: "90%",
        title: "<span style=color:blue>Template</span>",
        onClosed: function () {
            var href = path + "/lclSearch.do?methodName=display";
            document.location.href = href;
        }
    });
}

function defaultLoginNameCreatedBy() {
    var login = $('#loginNameId').val();
    if ($('#blcreatedBy').is(":checked")) {
        $('#createdBy').val(login);
        setdisableByDojo('createdBy');
        setFilterValuesByAll();
    } else {
        $('#createdBy').val('');
        if ($('#blbookedBy').is(":checked")) {
            $('#bookedBy').val(login);
            setdisableByDojo('bookedBy');
            setenableByDojo('createdBy');
            setFilterValuesByAll();
        } else {
            $('#bookedBy').val('');
            setFilterValuesByInvAll();
            setenableByDojo('createdBy');
        }
    }
}
function defaultLoginNameBookedBy() {
    var login = $('#loginNameId').val();
    if ($('#blbookedBy').is(":checked")) {
        setdisableByDojo('bookedBy');
        $('#bookedBy').val(login);
        setFilterValuesByAll();
    } else {
        $('#bookedBy').val('');
        if ($('#blcreatedBy').is(":checked")) {
            $('#createdBy').val(login);
            setenableByDojo('bookedBy');
            setdisableByDojo('createdBy');
            setFilterValuesByAll();
        } else {
            $('#bookedBy').val('');
            setFilterValuesByInvAll();
            setenableByDojo('bookedBy');
        }
    }
}
function defaultLoginNameBlOwner() {
    var login = $('#loginNameId').val();
    if ($('#blOwner').is(":checked")) {
        $('#blPoolOwner').val(login);
        setdisableByDojo('blPoolOwner');
        setFilterValuesByAll();
    } else {
        setenableByDojo('blPoolOwner');
        $('#blPoolOwner').val('');
        setFilterValuesByInvAll();
    }
}
function setdisableByDojo(id) {
    $('#' + id).attr('alt', ' ');
    $('#' + id).css("background", "#CCEBFF");
    $('#' + id).attr("readonly", true);
}
function setenableByDojo(id) {
    $('#' + id).attr('alt', 'SALES_PERSON');
    $('#' + id).css("background", "#FFFFFF");
    $('#' + id).attr("readonly", false);
}

function setFilterValuesByInvAll() {
    $("#filterBy").val("IWB");
    $('#filterByInventory').attr('disabled', false);
// $('#filterByInventory').attr('checked', true);
}
function setFilterValuesByAll() {
    if ($("#filterBy").val() !== 'BP') {
        $("#filterBy").val("All");
    }
    $('#filterByInventory').attr('disabled', true);
    $('#filterByInventory').attr('checked', false);
}


function combineValue(var1) {
    $('#' + var1).val($('#' + var1).val() + ',');
//    var originCode = $('#' + var1).val();
//    originCode = originCode.substring(0, originCode.lastIndexOf(",") + 1);
//    $('#' + var1).val(originCode += $('#' + var2).val() + ',');
//    $('#' + var2).val($('#' + var1).val());
}

jQuery(document).ready(function () {
    $('#origin').keyup(function () {
        var origin = $('#origin').val();
        if (origin === "") {
            $('#origin').val('');
            $('#portName').val('');
            $('#countrycode').val('');
        }
        if ($('#origin').val().length < $("#portName").val().length) {
            $("#portName").val($('#origin').val());
        }
    });
    $('#currentLocation').keyup(function () {
        var currentLocation = $('#currentLocation').val();
        if (currentLocation === "") {
            $('#currentLocation').val('');
            $('#currentLocName').val('');
            $('#currentLocCode').val('');
        }
    });
    $('#pol').keyup(function () {
        var pol = $('#pol').val();
        if (pol == "") {
            $('#pol').val('');
            $('#polName').val('');
            $('#polCountryCode').val('');
        }
        if ($('#pol').val().length < $("#polName").val().length) {
            $("#polName").val($('#pol').val());
        }
    });
    $('#pod').keyup(function () {
        var pod = $('#pod').val();
        if (pod == "") {
            $('#pod').val('');
            $('#podName').val('');
            $('#podCountryCode').val('');
        }
    });
    $('#destination').keyup(function () {
        var destination = $('#destination').val();
        if (destination == "") {
            $('#destination').val('');
            $('#destinationName').val('');
            $('#destCountryCode').val('');
        }
    });
    $('#client').keyup(function () {
        var client = $('#client').val();
        if (client == "") {
            $('#client').val('');
            $('#clientNo').val('');
            inventoryAllFilter();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#shipperName').keyup(function () {
        var shipperName = $('#shipperName').val();
        if (shipperName == "") {
            $('#shipperName').val('');
            $('#shipperNo').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#forwarder').keyup(function () {
        var forwarder = $('#forwarder').val();
        if (forwarder == "") {
            $('#forwarder').val('');
            $('#forwarderNo').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#consignee').keyup(function () {
        var consignee = $('#consignee').val();
        if (consignee == "") {
            $('#consignee').val('');
            $('#consigneeNo').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#fileNumber').keyup(function () {
        var fileNumber = $('#fileNumber').val();
        if (fileNumber == "") {
            $('#fileNumber').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#inbondNo').keyup(function () {
        var inbondNo = $('#inbondNo').val();
        if (inbondNo == "") {
            $('#inbondNo').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#customerPo').keyup(function () {
        var customerPo = $('#customerPo').val();
        if (customerPo == "") {
            $('#customerPo').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#trackingNo').keyup(function () {
        var trackingNo = $('#trackingNo').val();
        if (trackingNo == "") {
            $('#trackingNo').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#warehouseDocNo').keyup(function () {
        var warehouseDocNo = $('#warehouseDocNo').val();
        if (warehouseDocNo == "") {
            $('#warehouseDocNo').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#ssl').keyup(function () {
        var ssl = $('#ssl').val();
        if (ssl == "") {
            $('#ssl').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#sslBookingNo').keyup(function () {
        var sslBookingNo = $('#sslBookingNo').val();
        if (sslBookingNo == "") {
            $('#sslBookingNo').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#createdBy').keyup(function () {
        var createdBy = $('#createdBy').val();
        if (createdBy == "") {
            $('#createdBy').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#bookedBy').keyup(function () {
        var bookedBy = $('#bookedBy').val();
        if (bookedBy == "") {
            $('#bookedBy').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#blPoolOwner').keyup(function () {
        var blPoolOwner = $('#blPoolOwner').val();
        if (blPoolOwner == "") {
            $('#blPoolOwner').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#masterBl').keyup(function () {
        var masterBl = $('#masterBl').val();
        if (masterBl == "") {
            $('#masterBl').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#containerNo').keyup(function () {
        var containerNo = $('#containerNo').val();
        if (containerNo == "") {
            $('#containerNo').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#issuingTerminal').keyup(function () {
        var issuingTerminal = $('#issuingTerminal').val();
        if ($("#filterBy").val() !== 'BL') {
            if (issuingTerminal === "") {
                $('#issuingTerminal').val('');
                setFilterValuesByInvAll();
            } else {
                setFilterValuesByAll();
            }
        }
    });
    $('#subHouse').keyup(function () {
        var subHouse = $('#subHouse').val();
        if (subHouse == "") {
            $('#subHouse').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#amsHBL').keyup(function () {
        var subHouse = $('#amsHBL').val();
        if (subHouse == "") {
            $('#amsHBL').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#cfclAccount').keyup(function () {
        var cfclAccount = $('#cfclAccount').val();
        if (cfclAccount == "") {
            $('#cfclAccount').val('');
            $('#cfclAccountNo').val('');
            setFilterValuesByInvAll();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#bookedForVoyage').keyup(function () {
        var bookedForVoyage = $('#bookedForVoyage').val();
        if (bookedForVoyage == "") {
            $('#bookedForVoyage').val('');
        }
    });
    $('#foreignAgent').keyup(function () {
        var foreignAgent = $('#foreignAgent').val();
        if (foreignAgent == "") {
            $('#foreignAgent').val('');
            setFilterValuesByInvAll();
        }
    });
    $('#cfcl').change(function () {
        var cfcl = $('#cfcl').val();
        if (cfcl == "0") {
            inventoryAllFilter();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#originRegion').change(function () {
        var originRegion = $('#originRegion').val();
        if (originRegion == "") {
            inventoryAllFilter();
        } else {
            setFilterValuesByAll();
        }
    });
    $('#destinationRegion').change(function () {
        var destinationRegion = $('#destinationRegion').val();
        if (destinationRegion == "") {
            inventoryAllFilter();
        } else {
            setFilterValuesByAll();
        }
    });
});

function openApplyDefaults(path, methodName, userId, moduleName) {
    var originalClose = $.colorbox.close;
    $.colorbox.close = function () {
        var $frame = $(".cboxIframe").contents();
        if ($frame.find("#lclUserDefaultsForm").serialize() !== $frame.find("#loadForm").val()) {
            $frame.find("body").append(
                    $.prompt("Some fields have been modified, if you exit your changes will not be saved.", {
                        buttons: {
                            Yes: true,
                            No: false
                        },
                        submit: function (v) {
                            if (v) {
                                originalClose();
                            }
                        }
                    }));
        } else {
            originalClose();
        }
    }
    var href = path + "/lclExportUserDefaults.do?methodName=" + methodName + "&userId=" + userId + "&selectedMenu=" + moduleName;
    $.colorbox({
        iframe: true,
        href: href,
        width: "45%",
        height: "60%",
        title: "Edit Lcl Defaults",
        onClosed: function () {
            var href = path + "/lclSearch.do?methodName=display";
            document.location.href = href;
        }
    });
}

function addMorecites(path, buttonValue) {
    $.colorbox({
        iframe: true,
        href: path + "/lclSearch.do?methodName=addMultieCountry&buttonValue=" + buttonValue,
        width: "40%",
        height: "40%",
        title: "Add Cities"
    });
}
function openTerminatePopUp(path) {
    var href = path + "/lclTerminatePopup.do?methodName=displayBatchTerminate";
    $.colorbox({
        iframe: true,
        href: href,
        width: "75%",
        height: "90%",
        title: "Terminate"
    });
}

function lclBatchTerminationStatus() {
    var values = [];
    $("input[name='drNumber']").each(function () {
        if ($(this).val() !== '') {
            values.push($(this).val());
        }
    });
    var fileNumbers = values.toString();
    var terminateOption = $('input:radio[name=terminateOption]:checked').val();
    var consoTerminate = $('input:radio[name=consoTerminate]:checked').val();
    consoTerminate = (consoTerminate !== undefined) ? consoTerminate : "";
    var comment = $('#termComment').val();
    if (comment === null || comment === "" || comment === undefined) {
        $.prompt("Comment Is Required");
    } else if (terminateOption === null || terminateOption === "" || terminateOption === undefined) {
        $.prompt("Please Select atleast one Terminate Option");
    } else if (values === null || values.length === 0 || values === undefined) {
        $.prompt("Please enter atleast one file number");
    } else {
        $.prompt("Are you sure you want to terminate all of these bookings", {
            buttons: {
                Yes: 1,
                No: 2,
                Abort: 3
            },
            submit: function (v) {
                if (v === 1) {
                    showProgressBar();
                    var userId = $("#loginUserId").val();
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.lcl.dwr.LclDwr",
                            methodName: "lclBatchTermination",
                            param1: "X",
                            param2: terminateOption,
                            param3: comment,
                            param4: userId,
                            param5: consoTerminate,
                            param6: fileNumbers
                        },
                        async: false,
                        success: function (data) {
                            parent.$.colorbox.close();
                        }
                    });
                    hideProgressBar();
                    $.prompt.close();
                }
                else if (v === 2) {
                    $.prompt.close();
                } else if (v === 3) {
                    parent.$.colorbox.close();
                }
            }

        });
    }
}
function closepopUp() {
    parent.$.colorbox.close();
}