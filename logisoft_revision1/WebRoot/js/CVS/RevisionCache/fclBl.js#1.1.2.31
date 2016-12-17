function setTabName(tabName) {
    if (tabName && undefined != tabName && null != tabName && 'null' != tabName) {
        document.getElementById("selectedTab").value = tabName;
    }
    if (null != document.getElementById("collapsetable")) {
        document.getElementById("collapsetable").style.display = "block";
    }
    if (null != document.getElementById("chargestable")) {
        document.getElementById("chargestable").style.display = "none";
    }
    if (null != document.getElementById("expandcosttable")) {
        document.getElementById("expandcosttable").style.display = "none";
    }
    if (null != document.getElementById("collapseCosttable")) {
        document.getElementById("collapseCosttable").style.display = "block";
    }
}
function makeARInvoiceButtonGreen(fileNo,voyageInternal) {
    if (null != fileNo && fileNo != "") {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getARInvoiceForThisBL",
                param1: fileNo,
                param2: voyageInternal
            },
            success: function(data) {
                if (document.getElementById("arRedInvoice")) {
                    if (data == "ARINVOICE") {
                        document.getElementById("arRedInvoice").className = "buttonColor";
                        document.getElementById("arRedInvoiceDown").className = "buttonColor";
                    } else {
                        document.getElementById("arRedInvoice").className = "buttonStyleNew";
                        document.getElementById("arRedInvoiceDown").className = "buttonStyleNew";
                    }
                }
            }
        });
    }
}
function confirmUncheckClearNotifyBox(text) {
    confirmYesOrNo(text, "UncheckClearNotify");
}
function confirmUncheckClearConsigneeBox(text) {
    confirmYesOrNo(text, "UncheckClearConsignee");
}
function confirmUncheckClearMasterNotifyBox(text) {
    confirmYesOrNo(text, "UncheckClearMasterNotify");
}
function confirmUncheckClearMasterConsigneeBox(text) {
    confirmYesOrNo(text, "UncheckClearMasterConsignee");
}
function confirmUnCheckClearHouseShipperBox(text) {
    confirmYesOrNo(text, "UnCheckClearHouseShipper");
}
function postAccrual(fileNo) {
    if (document.fclBillLaddingform.readyToPost.checked && document.getElementById("manifestButtonDown") != null) {
        document.fclBillLaddingform.readyToPost.checked = false;
    }
    document.fclBillLaddingform.buttonValue.value = "postAccrualsBeforeManifest";
    if(checkAddChargeMappingWithGL(fileNo, "AC")) {
     confirmYesOrNo("Do you want to Post Accruals Before Manifesting?", "postAccrualsBeforeManifest");
    }
}
function getWareHouseAdd() {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "getWarehouseAddressById",
            param1: document.getElementById("importWareHouseId").value,
            dataType: "json"
        },
        success: function(data) {
            populateAddress(data);
        }
    });
}
function populateAddress(data) {
    if (null != data) {
        if (data.address != "" && data.address != null && data.zipCode != null && data.city != null && data.state != null) {
            document.fclBillLaddingform.importWareHouseAddress.value = data.warehouseName + "\n" + data.address + "\n" + data.city + "," + data.state + "," + data.zipCode;
        } else {
            document.fclBillLaddingform.importWareHouseAddress.value = "";
        }
        if (null != data.warehouseNo) {
            document.getElementById("importWareHouseCode").value = data.warehouseNo;
        }
    }
}
function fillOnwardInlandRouting() {
    closePopUp();
    document.getElementById('onwardInlandRouting').value = document.fclBillLaddingform.importWareHouseAddress.value;
    document.getElementById('importCargoLocationDiv').style.display = 'none';
}
function closeImportWareHousePopUp() {
    closePopUp();
    document.getElementById('importCargoLocationDiv').style.display = 'none';
}
function openImportWareHousePopUp() {
    showPopUp();
    document.getElementById('importCargoLocationDiv').style.display = 'block';
    document.getElementById("importCargoLocationDiv").style.left = "30%";
    document.getElementById("importCargoLocationDiv").style.top = "30%";
    document.getElementById("importCargoLocationDiv").style.width = "40%";
    document.getElementById("importCargoLocationDiv").style.height = "200px";
}

function checkAddChargeMappingWithGL(fileNumber, transactionType) {
    var flag = true;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkChargeMappingWithGL",
            param1: fileNumber,
            param2: transactionType,
            param3: 'true'
        },
        async: false,
        success: function (data) {
            if (data !== "") {
                alertNew("No gl account is mapped with these charge code.Please contact accounting -> <span style=color:red>" + data + "</span>.");
                flag = false;
            }
        }
    });
    return flag;
}