var path = "/" + window.location.pathname.split('/')[1];
jQuery(document).ready(function () {
    $('#unitId').val('');
    $("[title != '']").not("link").tooltip();
    changefilter(false);
});
function showViewButton() {
    $('#viewAll').show();
}
function changefilter(callBackFlag) {
    var filterByValue = $('#filterByChanges').val();
    var serviceType="";
    $(".cfclAgenttextBox").hide();
    $(".cfclAgentlabel").hide();
    $("#refreshBtn").hide();
    $("#showUnCompleteUnits").show();
    $("#isLclContainerSize").hide();
    if(filterByValue==='currentProcess'){
        $("#refreshBtn").show();
        $("#isLclContainerSize").show();
        $("#showUnCompleteUnits").hide();
        if(callBackFlag){
            search("voyageSearch");
        }
    }
    if(filterByValue==='unassignedContainers'){
        $('#addunit').show();
        $('#UnassignedContainers').show();
        $("#dispositionCode").val("YARD");
        $("#dispositionCode").addClass("readonly");
        $("#showUnCompleteUnits").hide();
        $("#usaLclExports").hide();
    }
    else{
        if(filterByValue==="lclCfcl"){
            serviceType='C';
            $(".cfclAgenttextBox").show();
            $(".cfclAgentlabel").show();
            $('#origin').attr('alt',"RELAYNAME");
            $('#destination').attr('alt',"CONCAT_RELAY_NAME_FD");
        }else if(filterByValue==="lclExport"){
            serviceType='E';
            $('#origin').attr('alt',"RELAYNAME");
            $('#destination').attr('alt',"CONCAT_RELAY_NAME_FD");
        }else if(filterByValue==="lclDomestic"){
            serviceType='N';
            $('#origin').attr('alt',"RELAYNAME_DOMESTIC_INLAND");
            $('#destination').attr('alt',"RELAYNAME_DOMESTIC_INLAND");
        }
        $('#serviceType').val(serviceType);
        $('#UnassignedContainers').hide();
        $("#usaLclExports").show();
    }
    showBtn();
}
function showBtn(){
    var filterByValue = $('#filterByChanges').val();
    $('#addunit').hide();
    $('#viewAll').hide();
    $('#addnew').hide();
    var origin=$('#origin').val();
    var destination=$('#destination').val();
    if(filterByValue!=='unassignedContainers' && filterByValue!=='currentProcess'
        && origin!='' && destination!=''){
        $('#addnew').show();
    }else if(filterByValue=="lclCfcl" && (origin!='' || destination!='')){
        $('#viewAll').show();
    }
    else if(filterByValue==='unassignedContainers'){
        $('#addunit').show();
    }
}
function searchVoyage(methodName) {
    $("#filterByChanges").attr("tabindex", -1);
    var filterByValue = $('#filterByChanges').val();
    if(filterByValue === 'currentProcess'){
        if ($("#origin").val() === ""){
            $.prompt('Origin is Required');
            $("#origin").css("border-color", "red");
            return false;
        }
        search("voyageSearch");
    }else{
        $("#origin").attr("tabindex", -1);
        if ($("#origin").val() === "" || $("#destination").val() === "") {
            return false;
        }
        if ($('#showUnCompleteUnits').is(':checked')) {
            searchUnCompleteUnits();
        } else {
            search(methodName);
        }
    }
}
function search(methodName) {
    window.parent.showLoading();
    $("#methodName").val(methodName);
    $("#lclUnitsScheduleForm").submit();
}
function deleteVoyage(ssHeaderId) {
    var txt = 'Are you sure You want to delete?';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                window.parent.showLoading();
                $("#voyageId").val(ssHeaderId);
                $("#methodName").val('deleteLclVoyage');
                $("#lclUnitsScheduleForm").submit();
            }
            else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function searchUnAssingedUnit(methodName) {
    if ($("#warehouseName").val() === '' || $("#warehouseName").val() === null
        || $("#dispositionCode").val() === '' || $("#dispositionCode").val() === null) {
        return false;
    }
    window.parent.showLoading();
    $("#warehouseId").val();
    $("#methodName").val(methodName);
    $("#lclUnitsScheduleForm").submit();
}
function addUnits(path) {
    var warehouseName = $("#warehouseName").val();
    if (warehouseName === '' || warehouseName === null) {
        $("#warehouseName").css("border-color", "red");
        $.prompt('Warehouse Name is required');
        return  false;
    }
    var filterByValue = $('#filterByChanges').val();
    var href = path + "/lclAddUnits.do?methodName=addUnits&warehouseId=" + $("#warehouseId").val()
    + "&filterByChanges=" + filterByValue;
    $.colorbox({
        iframe: true,
        href: href,
        width: "100%",
        height: "90%",
        title: "Add Unit"
    });
}

function editUnits(path, unitId,unitssId) {
    var href = path + "/lclAddUnits.do?methodName=editUnits&unitId=" + unitId + "&filterByChanges=" +
    $("#filterByChanges").val() + "&warehouseId=" + $("#warehouseId").val()+"&unitssId="+unitssId;
    $.colorbox({
        iframe: true,
        href: href,
        width: "100%",
        height: "90%",
        title: "Edit Unit"
    });
}
function deleteUnit(unitId) {
    $.prompt('Are you sure You want to delete?', {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                window.parent.showLoading();
                $("#unitId").val(unitId);
                $("#methodName").val('searchByUnAssignUnit');
                $("#lclUnitsScheduleForm").submit();
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}
function resetAllFields(callBackFlag) {
    // only for unassigned Container
    if ($("#warehouseName").val() !== ''
        && $("#dispositionCode").val() !== '') {
        $("#warehouseName").val('');
        $("#warehouseId").val('');
        $("#warehouseNo").val('');
    }
    $('#polNameLabel').text('');
    $('#podNameLabel').text('');
    $('#uncompletedUnits').text('');
    $('#showUnCompleteUnits').attr('checked',false);
    $('#isLclContainerSize').attr('checked',false);
    $('#sortBy').val('')
    $('#unitId').val('')
    $('#columnName').val('')
    $('#unitMultiFlag').val('')
    $('#bookingMultiFlag').val('')
    $('#portOfOriginId').val('')
    $('#finalDestinationId').val('')
    $('#columnName').val('')
    $('#sortBy').val('')
    $('#polId').val('')
    $('#polName').val('')
    $('#podName').val('')
    $('#podId').val('')
    $('#cfclAcctName').val('');
    $('#cfclAcctNo').val('');
    $('#unitPooId').val('');
    $('#unitPoo').val('');
    $('#unitFdId').val('');
    $('#unitFd').val('');
    $('#unitVoyageNo').val('');
    $('#origin').val('');
    $('#destination').val('');
    $('#unitNo').val('');
    $('#voyageNo').val('');
    $('#bookingNo').val('');
    for (var i = document.getElementById("voyagetable").rows.length; i > 0; i--) {
        document.getElementById("voyagetable").deleteRow(i - 1);
    }
    changefilter(callBackFlag);
}

function releaseReports() {
    var originId = $("#portOfOriginId").val();
    if (originId !== "") {
        var href = path + "/lclUnitsSchedule.do?methodName=viewReleaseEmail&origin="+originId;
        $.colorbox({
            iframe: true,
            href: href,
            width: "32%",
            height: "70%",
            title: "Release Report"
        });
    } 
    else {
        $.prompt("Origin is Required");
    }
}

function editViewAll(path, headerId) {
    var originId = $('#portOfOriginId').val();
    var destinationId = $('#finalDestinationId').val();
    var origin=$('#origin').val();
    var destination=$('#destination').val();
    window.parent.showLoading();
    var filterByValue = $('#filterByChanges').val();
    var href=path+"/lclAddVoyage.do?methodName=editVoyage&headerId="+headerId+ "&searchOriginId="+originId
    +"&searchFdId="+destinationId+"&showUnCompleteUnits="+$('#showUnCompleteUnits').is(':checked')
    +"&filterByChanges="+ filterByValue +"&searchOrigin="+origin+"&searchFd="+destination + "&unitVoyageSearch=Y";
    document.location.href = href;
}
function searchByViewAll() {
    search("searchByViewAll");
}
function addNewVoyage(methodName) {
    var filterByChanges = $('#filterByChanges').val();
    if("lclExport"===filterByChanges){
        var relayFlag=isValidateRelay();
        var bypassRelayCheck = $('#bypassRelayCheck').val();
        if(relayFlag || bypassRelayCheck=== 'true'){
            search(methodName);
        }else{
            var origin=$('#origin').val();
            var destination=$('#destination').val();
            $.prompt("Relay Does Not Exist For  <span style='color:red;font-weight:bold;'>" + origin + "</span> to <span style='color:red;font-weight:bold;'>" +destination+"</span>");
        }
    }else{
        search(methodName);
    }
}
function isValidateRelay(){
    var pooId = $('#portOfOriginId').val();
    var fdId = $('#finalDestinationId').val();
    var flag;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO",
            methodName: "isValidateRelay",
            param1: pooId,
            param2: fdId,
            dataType: "json"
        },
        async: false,
        success: function (result) {
            flag=result;
        }
    });
    return flag;
}
function editVoyage(path, headerId,serviceType) {
    var filterByValue = $('#filterByChanges').val();
    var originId = $('#portOfOriginId').val();
    var destinationId = $('#finalDestinationId').val();
    var origin=$('#origin').val();
    var destination=$('#destination').val();
    var  voyageNo=$('#voyageNo').val();
    var unitNo=$('#unitNo').val();
    var cfclAcctName = $("#cfclAcctName").val();
    var cfclAcctNo = $("#cfclAcctNo").val();
    var filterByNewValue="";
    if(filterByValue==="currentProcess"){
        filterByNewValue=filterByValue;
        if(serviceType==='E'){
            filterByValue='lclExport';
        }else if(serviceType==='N'){
            filterByValue='lclDomestic';
        }
        else if(serviceType==='C'){
            filterByValue='lclCfcl';
        }
    }
    var isContainerSize=$('#isLclContainerSize').is(':checked');
    window.parent.showLoading();
    var href=path+"/lclAddVoyage.do?methodName=editVoyage&headerId="+headerId+ "&searchOriginId="+originId+"&searchFdId="+destinationId
    +"&searchUnitNo="+unitNo+"&searchVoyageNo="+voyageNo+"&showUnCompleteUnits="+$('#showUnCompleteUnits').is(':checked')
    +"&filterByChanges="+ filterByValue +"&cfclAcctName=" + cfclAcctName + "&cfclAcctNo=" + cfclAcctNo+"&filterByNewValue="+filterByNewValue
    +"&searchOrigin="+origin+"&searchFd="+destination+"&searchLclContainerSize="+isContainerSize;
    document.location.href = href;
}
function copyVoyage(path, detailId) {
    var polName = $('#hiddenPolName').val();
    var podName = $('#hiddenPodName').val();
    var polId = $('#polId').val();
    var podId = $('#podId').val();
    var originId = $('#portOfOriginId').val();
    var destinationId = $('#finalDestinationId').val();
    var originName = "", destinationName = "";
    var fileterByValue = document.lclUnitsScheduleForm.filterByChanges.value;
    if (fileterByValue == 'lclExport') {
        originName = $("#origin").val();
        destinationName = $("#destination").val();
    } else {
        originName = $("#originPOL").val();
        destinationName = $("#destinationPOD").val();
    }
    window.parent.showLoading();
    document.location.href = path + "/lclAddVoyage.do?methodName=copyVoyage&detailId=" + detailId + "&pol=" + polName + "&pod=" + podName + "&originalOriginId=" + originId + "&originalDestinationId=" + destinationId + "&originalOriginName=" + originName + "&originalDestinationName=" + destinationName + "&filterByChanges=" + fileterByValue + "&polId=" + polId + "&podId=" + podId;
}

function checkVoyage(path, moduleId) {
    window.parent.changeLclVoyage(path, moduleId);
}

function searchByCfclAcct(methodName) {
    $("#serviceType").val("C");
    search(methodName);
}
function searchUnCompleteUnits() {
    var flag = $('#showUnCompleteUnits').is(':checked') ? true : false;
    var filterByChanges = $('#filterByChanges').val();
    if (flag) {
        //$(".uncompletedUnits").text('Units not Completed');
        $("#serviceType").val(filterByChanges === 'lclExport' ? 'E' : filterByChanges === 'lclDomestic' ? "N" : "C");
        search("searchByUnCompleteUnit")
    } else {
        $(".uncompletedUnits").text('');
        searchVoyage('search');
    }
}

function searchByVoyage(){//Search By Voyage
    var filterByValue = $('#filterByChanges').val();
    var serviceType="";
    var unitPooId=$('#unitPooId').val();
    var unitPoo=$('#unitPoo').val();
    var unitFdId=$('#unitFdId').val();
    var unitFd=$('#unitFd').val();
    $('#portOfOriginId').val(unitPooId);
    $('#finalDestinationId').val(unitFdId);
    $('#origin').val(unitPoo);
    $('#destination').val(unitFd);
    //    if(filterByValue==="lclCfcl"){
    //        serviceType='C';
    //    }else if(filterByValue==="lclExport"){
    //        serviceType='E';
    //    }else if(filterByValue==="lclDomestic"){
    //        serviceType='N';
    //    }
    //  $('#serviceType').val(serviceType);
    search('search');
}
function searchByUnit(){//Search By Unit
    var filterByValue = $('#filterByChanges').val();
    var searchUnitNo=$('#unitNo').val();
    var serviceType="";
    //    if(filterByValue==="lclCfcl"){
    //        serviceType='C';
    //    }
    //    else if(filterByValue==="lclExport"){
    //        serviceType='E';
    //    }else if(filterByValue==="lclDomestic"){
    //        serviceType='N';
    //    }
    var multiUnitFlag=parseInt($('#unitMultiFlag').val());
    if(multiUnitFlag>=1){
        var unitId=$('#unitId').val();
        var href = path + "/lclUnitsSchedule.do?methodName=searchMutiUnit&unitId="+unitId+"&unitNo="+searchUnitNo+"&serviceType="+serviceType+"&filterByChanges="+filterByValue;
        $.colorbox({
            iframe: true,
            width: "60%",
            height: "50%",
            href: href,
            title: "Units",
            onLoad: function () {
                $('#cboxClose').hide();
            }
        });
    }else{
        var unitPooId=$('#unitPooId').val();
        var unitPoo=$('#unitPoo').val();
        var unitFdId=$('#unitFdId').val();
        var unitFd=$('#unitFd').val();
        $('#voyageNo').val($('#unitVoyageNo').val()); 
        $('#portOfOriginId').val(unitPooId);
        $('#finalDestinationId').val(unitFdId);
        $('#origin').val(unitPoo); 
        $('#destination').val(unitFd); 
        search('search');
    }
}

function searchByBookingNo(){//Search By BookingNo    
    var filterByValue = $('#filterByChanges').val();
    var searchbookingNo=$('#bookingNo').val();
    var serviceType="";
    //    if(filterByValue==="lclCfcl"){
    //        serviceType='C';
    //    }
    //    else if(filterByValue==="lclExport"){
    //        serviceType='E';
    //    }else if(filterByValue==="lclDomestic"){
    //        serviceType='N';
    //    }
      var bookingMultiFlag=parseInt($('#bookingMultiFlag').val());
       if(bookingMultiFlag>0){
        var href = path + "/lclUnitsSchedule.do?methodName=searchMutiSSLBooking&bookingNo="+searchbookingNo;
        $.colorbox({
            iframe: true,
            width: "60%",
            height: "50%",
            href: href,
            title: "SSL Booking",
            onLoad: function () {
                $('#cboxClose').hide();
            }
        });
    }else{
        var unitPooId=$('#unitPooId').val();
        var unitPoo=$('#unitPoo').val();
        var unitFdId=$('#unitFdId').val();
        var unitFd=$('#unitFd').val();
        $('#voyageNo').val($('#unitVoyageNo').val());
        $('#portOfOriginId').val(unitPooId);
        $('#finalDestinationId').val(unitFdId);
        $('#origin').val(unitPoo);
        $('#destination').val(unitFd);
        search('search');
    }
}
$(document).ready(function () {
    var filterByValue = $('#filterByChanges').val();
    if(filterByValue==="lclCfcl"){
        $('#cfclUnitNo').keyup(function () {
            if($('#cfclUnitNo').val()===""){
                resetAllFields();
            }
        });
    }
    $('#unitNo').keyup(function () {
        if($('#unitNo').val()===""){
            resetAllFields();
        }
    });
    $('#voyageNo').keyup(function () {
        if($('#voyageNo').val()===""){
            resetAllFields();
        }
    });
});

function doSort(sortByValue) {
    var sortBy = $("#sortBy").val();
    var toggleValue = sortBy === "up" ? "down" : "up";
    $("#" + sortByValue).removeClass(sortBy).addClass(toggleValue);
    $("#columnName").val(sortByValue);
    $("#sortBy").val(toggleValue);
    search('search');
}
function searchLimit(){
    var filterByValue = $('#filterByChanges').val();
    if(filterByValue==='currentProcess'){
        search('voyageSearch');
    }else{
        search('search');
    }
}

function doCurrentProcessSort(sortByValue) {
    var sortBy = $("#sortBy").val();
    var toggleValue = sortBy === "up" ? "down" : "up";
    $("#" + sortByValue).removeClass(sortBy).addClass(toggleValue);
    $("#columnName").val(sortByValue);
    $("#sortBy").val(toggleValue);
    search('voyageSearch');
}
function searchContainerSize(){
    searchVoyage('voyageSearch');
}