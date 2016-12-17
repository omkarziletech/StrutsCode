//Meiyazhakan
// agent flag is used---auto cost for imports in unit Charges

$(document).ready(function () {
    $(".printVoyageNoForExport").text(parent.$("#schedule").val());
});
function backToInvoiceList(path, fileId, voyageId, agentFlag) {
    var selectedMenu = $("#selectedMenu").val();
    var unitNo = $("#unitNo").val();
    var url = path + "/lclVoyageArInvoice.do?methodName=display&fileNumberId=" + fileId + "&voyageId=" + voyageId;
    url += "&selectedMenu=" + selectedMenu + "&unitNo=" + unitNo + "&agentFlag=" + agentFlag;
    window.location = url;
}
function unPostInvoice(path, fileId, fileNumber, invoiceId, agentFlag) {
    var selectedMenu = $("#selectedMenu").val();
    var unitNo = $("#unitNo").val();
    var url = path + "/lclVoyageArInvoice.do?methodName=unPost&fileNumberId=" + fileId + "&fileNumber=" + fileNumber + "&invoiceId=" + invoiceId;
    url = url + "&selectedMenu=" + selectedMenu + "&unitNo=" + unitNo + "&agentFlag=" + agentFlag;
    window.location = url;
}
function searchAR(path, agentFlag) {
    var selectedMenu = $("#selectedMenu").val();
    var fileNumberId = $("#fileNumberId").val();
    var voyageId = $("#voyageId").val();
    var unitNo = $("#unitNo").val();
    var url = path + "/lclVoyageArInvoice.do?methodName=searchAR&selectedMenu=" + selectedMenu + "&fileNumberId=" + fileNumberId + "&unitNo=" + unitNo + "&agentFlag=" + agentFlag;
    url = url + "&voyageId=" + voyageId;
    window.location = url;
}
function editArInvoice(path, fileId, voyageId, invoiceId, listFlag, newItemFlag, agentFlag) {
    var selectedMenu = $("#selectedMenu").val();
    var unitNo = $("#unitNo").val();
    var url = path + "/lclVoyageArInvoice.do?methodName=edit&fileNumberId=" + fileId + "&voyageId=" + voyageId + "&invoiceId=" + invoiceId;
    url = url + "&listFlag=" + listFlag + "&newItemFlag=" + newItemFlag + "&selectedMenu=" + selectedMenu + "&unitNo=" + unitNo + "&agentFlag=" + agentFlag;
    window.location = url;
}
function openAgentInvoicePopup(path, unitSSId, headerId) {
    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getUnitLevelAgentCharge",
            forward: "/jsps/LCL/lclUnitAutoCostTemplate.jsp",
            param1: unitSSId,
            param2: "UNIT_AGENT_CHR",
            request: true
        },
        success: function (data) {
            if ($.trim(data) !== "") {
                showMask();
                jQuery("<div style='top:50px; margin-left : 450px; width:500px;height:150px'></div>").html(data).addClass("popup").appendTo("body");
            } else {
                agentInvoice(path, unitSSId, headerId, "No");
            }
        }
    });
}
function agentInvoice(path, unitSSId, headerId, agentFlag) {
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkLinkedDrsCount",
            param1: unitSSId,
            dataType: "json"
        },
        success: function (data) {
            if (data[0] === 0) {
                $.prompt("None of the DRS linked to Current Unit.Please link atleast one DR.");
                return false;
            } else if (data[1] === 0 || data[1] === null || data[1] === "0") {
                $.prompt("Atleast one agent charge must be Released To Invoice to generate invoice.");
                return false;
            } else {
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "checkAgentsForInvoice",
                        param1: unitSSId
                    },
                    success: function (data) {
                        if (data.indexOf(',') > -1) {
                            $.prompt("Multiple agents are selected, Please only release charges for one agent at a time");
                            return false;
                        } else {
                            agentInvoicePopUp(path, unitSSId, headerId, agentFlag, data);
                        }
                    }
                });
            }
        }
    });
}
function agentInvoicePopUp(path, unitSSId, headerId, agentFlag, agentAcctNo) {
    var unitNo = $("#unitNo").val();
    var href = path + "/lclVoyageArInvoice.do?methodName=openAgentInvoicePopup&fileNumberId=" + unitSSId + "&unitNo=" + unitNo + "&voyageId=" + headerId + "&selectedMenu=Imports&agentFlag=" + agentFlag + "&agentAcctNo=" + agentAcctNo;
    $.colorbox({
        iframe: true,
        href: href,
        width: "70%",
        height: "60%",
        title: "Agent Invoice"
    });
}
function addAgentInvoicePopUp(path, unitSSId, headerId, agentFlag, arRedInvoiceId) {
    $.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkAgentsForInvoice",
            param1: unitSSId
        },
        success: function (data) {
            if (data.indexOf(',') > -1) {
                $.prompt("Multiple agents are selected, Please only release charges for one agent at a time");
                return false;
            } else {
                var unitNo = $("#unitNo").val();
                var href = path + "/lclVoyageArInvoice.do?methodName=openAgentInvoicePopup&fileNumberId=" + unitSSId + "&unitNo=" + unitNo + "&voyageId=" + headerId + "&selectedMenu=Imports&agentFlag=" + agentFlag + "&arRedInvoiceId=" + arRedInvoiceId;
                $.colorbox({
                    iframe: true,
                    href: href,
                    width: "70%",
                    height: "60%",
                    title: "Agent Invoice"
                });
            }
        }
    });
}
function previewArInvoice(buttonValue, invoiceId, path) {
    var unitSSId = $("#fileNumberId").val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclPrintUtil",
            methodName: "lclArInvoiceReport",
            param1: "",
            param2: buttonValue,
            param3: invoiceId,
            param4: unitSSId,
            param5: "No",
            request: "true"
        },
        preloading: true,
        success: function (data) {
            viewFile(data, path);
        }
    });
}
function viewFile(fileName, path) {
    if (fileName.indexOf(".xls") > 0 || fileName.indexOf(".doc") > 0
            || fileName.indexOf(".mht") > 0 || fileName.indexOf(".msg") > 0
            || fileName.indexOf(".csv") > 0 || fileName.indexOf(".ppt") > 0) {
        window.open(path + "/servlet/FileViewerServlet?fileName=" + fileName, "", "resizable=1,location=1,status=1,scrollbars=1, width=600,height=400");
    } else {
        window.open(path + "/servlet/FileViewerServlet?fileName=" + fileName, "_blank", "resizable=1,width=1200,height=600,top=40,left=40,directories=no,location=no,linemenubar=no,toolbar=no,status=no");
    }
}

function deleteArInvoice(path, fileId, fileNumber, invoiceId, listFlag, newItemFlag) {
    $.prompt('Are you sure you want to delete?', {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                var selectedMenu = $("#selectedMenu").val();
                var unitNo = $("#unitNo").val();
                var url = path + "/lclVoyageArInvoice.do?methodName=deleteInvoice&fileNumberId=" + fileId + "&fileNumber=" + fileNumber + "&invoiceId=" + invoiceId + "&listFlag=" + listFlag + "&newItemFlag=" + newItemFlag + "&selectedMenu=" + selectedMenu + "&unitNo=" + unitNo;
                window.location = url;
                hideProgressBar();
                $.prompt.close();
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function createInvoice(methodName) {
    var custName = $('#customerName').val();
    var date = $('#date').val();
    if (custName === "" || custName === null) {
        $.prompt("CustomerName is required");
        $("#customerName").css("border-color", "red");
    } else if ("" == date || date != "") {
        if (date == "") {
            $.prompt("Please Enter Date");
            $("#date").css("border-color", "red");
            $('#date').val('');
        } else {
            if (null != date && date != undefined) {
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "validDate",
                        param1: date,
                        dataType: "json"
                    },
                    success: function (data) {
                        if (!data) {
                            congAlert("Please Enter valid Date");
                            $('#date').val('');
                        } else {
                            calculateDueDate();
                        }
                    }
                });
            }
        }
    } else {
        checkAlertForCreatingInvoice(methodName);
    }
}

function calculateDueDate() {
    var day, month, year;
    var date = $('#date').val().split("-");
    day = date[0];
    month = getMonthNumber(date[1]) - 1;
    year = date[2];
    var invoiceDate = new Date(year, month, day);
    if (invoiceDate > new Date()) {
        $('#date').val("");
        $.prompt("Invoice Date should not be greater than Today's Date");
        $("#date").css("border-color", "red");
        return;
    } else {
        checkAlertForCreatingInvoice('save');
    }
}
function checkAlertForCreatingInvoice(methodName) {
    if ($('#invoiceNumber').val() === null || $('#invoiceNumber').val() === '') {
        var txt = 'Do you want to print a list of all DRs on this voyage, on the invoice?';
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    $('#printOnDrFlag').val("Yes");
                    saveArinvoice(methodName);
                } else {
                    saveArinvoice(methodName);
                }
            }
        });
    } else {
        saveArinvoice(methodName);
    }
}
function saveArinvoice(methodName) {
    showLoading();
    document.lclVoyageArInvoiceForm.methodName.value = methodName;
    document.lclVoyageArInvoiceForm.submit();
}
function showLineItem() {
    var m_names = new Array("JAN", "FEB", "MAR",
            "APR", "MAY", "JUN", "JUL", "AUG", "SEP",
            "OCT", "NOV", "DEC");
    var d = new Date();
    var curr_date = d.getDate();
    var curr_month = d.getMonth();
    var curr_year = d.getFullYear();
    $("#date").val(curr_date + "-" + m_names[curr_month]
            + "-" + curr_year);
    $("#addNew").show();
    clearValues();
    $("#lineItemList").hide();
    $("#arInvoiceList").hide();
    $("#success").hide();
    $("#agentInvoice").hide();
    $("#addnewButton").hide();
    $("#searchButton").hide();
// $("#date").val($("#newDate").val());
}
function showAddItem() {
    $("#chargeCode").val("");
    $("#chargeId").val("");
    $("#addLineItem").show();
    $("#chargeCode").focus();
    if ($("#voyageTerminal").val() !== "") {
        $("#terminal").val($("#voyageTerminal").val());
    } else {
        $("#terminal").val('Select');
    }
}
function hideAddItem() {
    $("#addLineItem").hide();
}
function submitAjaxForm(methodName, formName, fileNumber, selector) {
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&fileNumber=" + fileNumber;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                $("#addLineItem").hide();
                $("#chargeCode").val("");
                $("#terminal").val("");
                $("#amount").val("");
                $("#chargeDescription").val("");
            });
}

function postInvoice(path, fileId, fileNumber, invoiceId) {
    var acctNo = $('#customerNumber').val();
    var acctName = $('#customerName').val();
    var arRedInvoiceId = $("#arRedInvoiceId").val();
    if (acctNo !== '' && acctNo !== null && acctNo !== undefined) {
        if (checkInvoiceChargeAndCostMappingWithGLAccount(arRedInvoiceId)) {
            $("#methodName").val("checkCodeFCountByAcctNo");
            $("#selectedMenu").val($("#selectedMenu").val());
            var params = $("#lclVoyageArInvoiceForm").serialize();
            params += "&acctNo=" + acctNo + "&acctName=" + acctName;
            $.post($("#lclVoyageArInvoiceForm").attr("action"), params,
                    function (data) {
                        if (data === 'true') {
                            var unitNo = $("#unitNo").val();
                            var href = path + "/lclVoyageArInvoice.do?methodName=post&fileNumberId=" + fileId + "&fileNumber=" +
                                    fileNumber + "&invoiceId=" + invoiceId + "&unitNo=" + unitNo + "&acctNo=" + acctNo + "&selectedMenu=" + $("#selectedMenu").val();
                            $.colorbox({
                                iframe: true,
                                href: href,
                                width: "90%",
                                height: "90%",
                                title: "AR Invoice"
                            });
                        }
                        else {
                            $.prompt("<font color='red'>" + data + "</font>");
                        }
                    });
        }
    } else {
        $.prompt('Please select Customer');
        return false;
    }
}

function submitFormForCharge(methodName) {
    var amount = $("#amount").val();
    var chargeId = $("#chargeId").val();
    if (chargeId === null || chargeId === "" || chargeId === "0") {
        $.prompt('Code is required');
        $("#chargeCode").css("border-color", "red");
        return false;
    }
    if (amount === null || amount === "" || amount === "0.0") {
        $.prompt("Amount is required");
        $("#amount").css("border-color", "red");
        return false;
    } else if ($("#selectedMenu").val() === 'Exports' && !validateGlMapping($("#chargeCode").val())) {
        return false;
    } else {
        $("#methodName").val(methodName);
        if (parent.$("#voyageTerminal").val() !== "") {
            $("#voyageTerminal").val(parent.$("#voyageTerminal").val());
        }
        $("#lclVoyageArInvoiceForm").submit();
    }
}

function clearValues() {
    $('#customerName').val('');
    $('#customerNumber').val('');
    $('#dueDate').val('');
    $('#notes').val('');
    $('#invoiceNumber').val('');
    $('#customerType').val('');
    $('#contactName').val('');
    $('#address').val('');
    $('#contactName').val('');
    $('#phoneNumber').val('');
    $('#arRedInvoiceId').val('');
    // $('#date').val('');
    $('#invoiceDate').val('');
    $('#status').val('');
    $('#printOnDrFlag').val('');
}

function verifyAcctType() {
    var target = $('#customerType').val();
    if ($('#customerDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#customerDisableAcct').val() + "</span>");
        clearCustomerValues();
    } else {
        if (target !== "") {
            if (target === 'C') {
                $.prompt("Consignee Accounts are not allowed to be billed");
                clearCustomerValues();
            }
        }
    }
}
function clearCustomerValues() {
    jQuery("#customerName").val('');
    jQuery("#customerType").val('');
    jQuery("#customerNumber").val('');
    jQuery("#contactName").val('');
    jQuery("#phoneNumber").val('');
    jQuery("#fax").val('');
    jQuery("#address").val('');
}
function openArContact() {
    var customer = $('#customerNumber').val();
    var customerName = $('#customerName').val();
    var contactName = $('#contactName').val();
    var subtype = "AR";
    customerName = customerName.replace("&", "amp;");
    var href = "/logisoft/lclContactDetails.do?methodName=display&accountName=" + customerName + "&accountNo=" + customer + "&contactName=" + contactName
            + "&subtype=" + subtype + "&vendorName=" + customerName + "&vendorNo=" + customer;
    mywindow = window.open(href, '', 'width=900,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}
function deleteInvoice(invoiceId) {
    $.prompt('Are you sure you want to delete Invoice?', {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                showLoading();
                $("#invoiceId").val(invoiceId);
                $("#methodName").val("deleteEntireInvoice");
                $("#lclVoyageArInvoiceForm").submit();
                closePreloading();
                $.prompt.close();
            }
            else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function viewReports(path, invoiceNo, invoiceId) {
    var url = path + "/printConfig.do?screenName=BL&arInvoice=" + invoiceNo + "&arInvoiceId=" + invoiceId;
    mywindow = window.open(url, '', 'width=800,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}
function addDrLevelAgentInvoicePopUp(path, unitSSId) {
    $("#methodName").val("showDrLevelAgentInvoce");
    var params = $("#lclVoyageArInvoiceForm").serialize();
    params += "&moduleId=" + unitSSId;
    $.post($("#lclVoyageArInvoiceForm").attr("action"), params,
            function (data) {
                if (data !== '') {
                    var href = path + "/lclVoyageArInvoice.do?methodName=showDrLevelAgentInvoce&moduleId=" + unitSSId;
                    $.colorbox({
                        iframe: true,
                        href: href,
                        width: "80%",
                        height: "70%",
                        title: "Agent Charge Invoice List"
                    });
                } else {
                    $.prompt("Atleast one agent charge must be Released To Invoice to generate invoice.")
                }
            });
}

function addReleasedInvoice(methodName) {
    var invoiceId = parent.$("#arRedInvoiceId").val();
    var agentCharge = [];
    jQuery(".checkAgentCharge:checked").each(function () {
        agentCharge.push(jQuery(this).val())
    });
    if (agentCharge <= 0) {
        $.prompt("Atleast one agent charge must be Released To Invoice.");
    } else {
        $("#methodName").val("drLevelAgentCharges");
        var params = $("#lclVoyageArInvoiceForm").serialize();
        params += "&moduleId=" + agentCharge + "&invoiceId=" + invoiceId;
        $.post($("#lclVoyageArInvoiceForm").attr("action"), params,
                function (data) {
                    parent.$("#methodName").val(methodName);
                    parent.$("#lclVoyageArInvoiceForm").submit();
                    parent.$.colorbox.close();
                });
    }
}
function canelReleasedInvoice() {
    var flag = true;
    jQuery(".checkAgentCharge:checked").each(function () {
        flag = false;
    });
    if (flag) {
        parent.$.colorbox.close();
    } else {
        $.prompt("DR Is Selected Need To Close");
    }
}

function allowNegativeNumbers(obj) {
    if (!/^-?\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        $.prompt("This field should be Numeric");
    }
}

function checkInvoiceChargeAndCostMappingWithGLAccount(arRedInvoiceId) {
    var flag = true;
    if ($("#selectedMenu").val() !== 'Exports') {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkInvoiceChargeAndCostMappingWithGLAccount",
                param1: arRedInvoiceId,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data !== "") {
                    $.prompt("No gl account is mapped with these charge code.Please contact accounting - <span style=color:red>" + data + "</span>.");
                    flag = false;
                }
            }
        });
    }
    return flag;
}

function validateGlMapping(chargeCode) {
    var flag = true;
    var originId = parent.$("#originId").val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO",
            methodName: "validateLclExportGlAccount",
            param1: chargeCode,
            param2: "0",
            param3: "",
            param4: originId,
            param5: "AR",
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data !== '') {
                flag = false;
                $.prompt(data);
            }
        }
    });
    return flag;
}