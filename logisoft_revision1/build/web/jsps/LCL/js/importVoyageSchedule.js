jQuery(document).ready(function () {
    $(document).keydown(function (e) {
        if (e.keyCode === 9) {
            $("#origin").change(function () {
                $('#portOfOriginId').val('');
                searchVoyageDetails('searchVoyageResult');
            });
            $("#destination").change(function () {
                $('#finalDestinationId').val('');
                searchVoyageDetails('searchVoyageResult');
            });
            $("#billsTerminal").change(function () {
                $('#billsTerminalNo').val('');
                searchVoyageDetails('searchVoyageResult');
            });
        }
        $('#dispositionCode').keypress(function () {
            var dispositionCode = $(this).val() + 1;
            if (dispositionCode != "") {
                $('#loginName').val('');
                $('#loginId').val('');
            }
        });

        $('#billsTerminal').keypress(function () {
            var billsTerminal = $(this).val() + 1;
            if (billsTerminal != "") {
                $('#loginName').val('');
                $('#loginId').val('');
            }
        });

        $('#masterBl').keyup(function() {
            var masterBl=$(this).val();
            if(masterBl !=""){
                $('#loginName').val('');
                $('#loginId').val('');
            }
        });
        $('#agentNo').keyup(function() {
            if($.trim($(this).val()) !== ""){
                $('#loginName').val('');
                $('#loginId').val('');
            }
        });
    });
    $(document).keypress(function (e) {
        if (e.which == 13) {
            if ($("#unitNo").val() !== undefined && $("#unitNo").val() !== '') {
                searchUnitNo('searchVoyageResult');
            } else if ($("#masterBl").val() != undefined && $("#masterBl").val() !== '') {
                searchMasterBl('searchVoyageResult');

            } else if ($("#voyageNo").val() != undefined && $("#voyageNo").val() != '') {
                searchByVoyageNo('searchVoyageResult');
            }
            return false;
        }
    });
    $('#origin').keyup(function () {
        var origin = $('#origin').val();
        if (origin === "") {
            $('#origin').val('');
            $('#portOfOriginId').val('');
        }
    });
});
function searchVoyageDetails(methodName) {// search by origin,destination and billing Terminal
    var origin = $('#origin').val();
    var destination = $('#destination').val();
    if (destination === "" && origin !== "") {
        $('#loginName').val('');
        $('#loginId').val('');
    }
    if (destination !== "" && origin === "") {
        $('#loginName').val('');
        $('#loginId').val('');
    }
    submitForm(methodName);
}
function searchByVoyageNo(methodName) {//Search by Voygae No
    if ($('#voyageNo').val() === "") {
        $.prompt("Voyage No is required");
        $("#voyageNo").css("border-color", "red");
        $("#voyageNo").show();
    } else {
        submitForm(methodName);
    }
}
function searchByVoyOwner(methodName) {//search by Voyage Owner
    if ($('#loginName').val() === "") {
        $.prompt("Please Select Owner");
        $("#loginName").css("border-color", "red");
        $("#loginName").show();
    } else {
        submitForm(methodName);
    }
}
function searchByLoginUser(methodName) {//search by Voyage Owner
    var loginUserSearchFlag = $("#loginUserSearchFlag").val();
    if (loginUserSearchFlag === 'false') {
    } else if (loginUserSearchFlag === 'true') {
        $("#loginUserSearchFlag").val('true');
        submitForm(methodName);
    }
}
function searchUnitNo(methodName) {//Search by Unit No
    if ($('#unitNo').val() === "") {
        $.prompt(" Unit No is required");
        $("#unitNo").css("border-color", "red");
        $("#unitNo").show();
    } else {
        $('#loginName').val('');
        $('#loginId').val('');
        submitForm(methodName);
    }
}

$(document).ready(function(){
    $("#unitNo").keypress(function (e) {
        var keycode = (e.keyCode ? e.keyCode : e.which);
        if (keycode === 13) {
            e.preventDefault();
            autoFormatUnitNumber(this);
        }
    });
});

function autoFormatUnitNumber(obj) {
    var unitChecked = document.getElementById("unitNoCheck").checked;
    if(!unitChecked){
        var unitNo = obj.value;
        unitNo = unitNo.replace(/-/g, '');
        if (unitNo.length != 11 || !(/^[a-z]+$/i.test(unitNo.substring(0, 4))) || !( /^\d+$/.test(unitNo.substring(4,11))) ) {
            obj.value = "";
            $('#unitNo').val("");
            $.prompt('Unit number must be "AAAA-NNNNNN-N" in format');
        } else {
            if (unitNo.lastIndexOf("-") != 11 || unitNo.indexOf("-") != 4) {
                obj.value = unitNo.substring(0, 4) + "-" + unitNo.substring(4, 10) + "-" + unitNo.substring(10);
            }
        }
    }
    return true;
    if (unitNo !== '') {
        $('#loginName').val('');
        $('#loginId').val('');
        $('#voyageNo').val('');
    }
}
function searchMasterBl(methodName) {//Search by MasterBl No
    if ($('#masterBl').val() === "") {
        $.prompt("MasterBl value is required");
        $("#masterBl").css("border-color", "red");
        $("#masterBl").show();
    } else {
        submitForm(methodName);
    }
}
function searchByAgentNo(methodName) {//Search by Agent No
    if ($('#agentNo').val() === "") {
        $.prompt("Agent No is required");
        $("#agentNo").css("border-color", "red").focus();
    } else {
        submitForm(methodName);
    }
}
function searchByDisposition() {//Search By Disposition
    var disp_id = $("#dispositionId").val();
    var disp = $("#dispositionCode").val();
    if (disp_id !== undefined && disp_id !== '') {
        window.parent.showLoading();
        $("#methodName").val("searchVoyageResult");
        $("#lclUnitsScheduleForm").submit();
    } else if (disp !== undefined && disp !== '') {
        window.parent.showLoading();
        $("#methodName").val("searchVoyageResult");
        $("#lclUnitsScheduleForm").submit();
    } else {
        $.prompt("Please select disposition!");
    }
}
function submitForm(methodName) {
    window.parent.showLoading();
    $("#methodName").val(methodName);
    $("#lclUnitsScheduleForm").submit();
}
function createNewVoyage(methodName, path) {//Creating New Voayge
    var dest = $('#destination').val().substring($('#destination').val().lastIndexOf("(") + 1, $('#destination').val().lastIndexOf(")"));
    if ($('#origin').val() === "") {
        $.prompt("Origin is required");
        $("#origin").css("border-color", "red");
        $("#origin").show();
    } else if ($('#destination').val() === "") {
        $.prompt("Destination is required");
        $("#destination").css("border-color", "red");
        $("#destination").show();
    }
    else {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getDefaultAgentForLcl",
                param1: $("#polCode").val(),
                param2: "I",
                dataType: "json"
            },
            success: function (data) {
                if (data[0] === null || data[0] === "") {
                    $.prompt("No Agent exists for this origin");
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.lcl.dwr.LclDwr",
                            methodName: "checkImportVoyageTerminal",
                            param1: dest,
                            param2: "Y",
                            dataType: "json",
                            request: "true"
                        },
                        success: function (data) {
                            if (data[0] === '') {
                                $.prompt("No Terminal exists for this destination");
                            } else if (data[1] === '' && data[2] === 'true') {
                                $.prompt("POD does not have a 3 digit ECI port number, therefore you cannot open a voyage with this POD");
                            } else {
                                submitForm(methodName);
                            }
                        }
                    });
                }
            }
        });
    }
}



function editVoyage(path, headerId) {
    var originId = $('#portOfOriginId').val();
    var origin = $('#origin').val();
    var destinationId = $('#finalDestinationId').val();
    var destination = $('#destination').val();
    var billsTerminal = $('#billsTerminal').val();
    var billsTerminalNo = $('#billsTerminalNo').val();
    var dispositionCode = $('#dispositionCode').val();
    var dispoId = $('#dispositionId').val();
    var voyageNo = $('#voyageNo').val();
    var loginName = $('#loginName').val();
    var loginId = $('#loginId').val();
    var unitNo = $('#unitNo').val();
    var masterBl = $('#masterBl').val();
    var agentNo = $('#agentNo').val();
    var searchVoyageLimit = $('#limit').val();
    window.parent.showLoading();
    document.location.href = path + "/lclImportAddVoyage.do?methodName=editVoyage&headerId=" + headerId + "&searchOriginId="+originId
    +"&searchFdId="+destinationId+"&searchTerminalNo="+billsTerminalNo+"&searchLoginId="+loginId+"&searchUnitNo="+unitNo+"&searchVoyageNo="+voyageNo
    +"&searchMasterBl="+masterBl+"&searchAgentNo="+agentNo+"&searchDispoId="+dispoId+"&searchVoyageLimit="+searchVoyageLimit
    +"&searchOrigin="+origin+"&searchFd="+destination+"&searchTerminal="+billsTerminal + "&homeScreenVoyageFileFlag="+false;
//    document.location.href = path + "/lclImportAddVoyage.do?methodName=editVoyage&headerId=" + headerId + "&originalOriginId=" + originId +
//    +"&originalDestinationId=" + destinationId + "&originalOriginName=" + origin + "&originalDestinationName=" + destination
//    + "&billsTerminal=" + billsTerminal + "&billsTerminalNo=" + billsTerminalNo + "&voyageNo=" + voyageNo + "&loginName=" + loginName + "&loginId=" + loginId
//    + "&dispositionCode=" + dispositionCode + "&dispositionId=" + dispositionId + "&unitNo1=" + unitNo1 + "&masterBL1=" + masterBL1+ "&limit=" + limit +"&agentNo=" + agentNo;
}
function copyVoyage(path, headerId, detailId, unitId) {
    //var pol = $('#hiddenPolName').val();
    //var pod = $('#hiddenPodName').val();
    // var originId = $('#portOfOriginId').val();
    //var destinationId = $('#finalDestinationId').val();
    //var originName = $('#origin').val();
    //var destinationName = $('#destination').val();
    window.parent.showLoading();
    document.location.href = path + "/lclImportAddVoyage.do?methodName=copyVoyage&headerId=" + headerId + "&detailId=" + detailId + "&unitId=" + unitId;
//+ "&pol=" + pol + "&pod=" + pod + "&originalOriginId=" + originId + "&originalDestinationId=" + destinationId + "&originalOriginName=" + originName
//+ "&originalDestinationName=" + destinationName + "&unitId=" + unitId;
}
function deleteVoyageList(id) {
    var txt = 'Are you sure You want to delete?';
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                window.parent.showLoading();
                $('#voyageId').val(id);
                $("#methodName").val('deleteVoyage');
                $("#lclUnitsScheduleForm").submit();
                closePreloading();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function resetAllFields() {
    setClearFeilds();
    $('#voyageNo').val('');
    for (var i = document.getElementById("voyagetable").rows.length; i > 0; i--) {
        document.getElementById("voyagetable").deleteRow(i - 1);
    }
}
function clearValues() {
    setClearFeilds();
}
function setClearFeilds() {
    $('#origin').val('');
    $('#destination').val('');
    $('#portOfOriginId').val('');
    $('#finalDestinationId').val('');
    $('#billsTerminal').val('');
    $('#billsTerminalNo').val('');
    $('#loginName').val('');
    $('#masterBl').val('');
    $('#agentNo').val('');
    $('#loginId').val('');
    $('#dispositionCode').val('');
    $('#dispositionId').val('');
    $('#unitNo').val('');
    $('#unitNoCheck').removeAttr('checked');
    $('#polName').text('');
    $('#podName').text('');
    $('#voyagNoSearch').text('');
}
function checkForNumberOnly(obj) {
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        sampleAlert("This field should be Numeric");
        obj.value = "";
    }
}
function doSort(sortBy) {
    $('#columnName').val(sortBy);
    submitForm('searchVoyageResult');
}

function searchByLimit(methodName){
    submitForm(methodName);
}