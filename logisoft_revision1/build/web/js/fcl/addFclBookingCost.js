

function fillInvoiceNumber(invoiceNumber) {
    document.EditBookingsForm.invoiceNumber.value = invoiceNumber;
    var costcode = document.EditBookingsForm.costCode.value;
    if (costcode === 'DEFER') {
        var vendor = parent.parent.document.getElementById("sslDescription").value;
        var index = vendor.indexOf("//");
        var vendorName = "";
        var vendorNumber = "";
        if (index != -1) {
            vendorName = vendor.substring(0, index);
            vendorNumber = vendor.substring(index + 2, vendor.length);
        } else {
            vendorName = parent.parent.document.getElementById("sslDescription").value;
        }
        document.getElementById('accountName').value = vendorName;
        document.getElementById('accountNo').value = vendorNumber;
        document.getElementById('accountNameCheck').value = vendorName;
        document.getElementById('accountName').readOnly = true;
        document.getElementById('accountName').tabIndex = -1;
        document.getElementById("defaultCarrierY").checked = false;
    }else{
        document.getElementById('accountName').value = '';
        document.getElementById('accountNo').value = '';
    }
}
function addBookingAccruals(username, toDaysdate, fileNo) {
    if (document.EditBookingsForm.costCode.value === "") {
        jQuery("#costCodeDesc").css("border-color", "red");
        alertNew("Please Enter the cost code ");
        return;
    }
    var chargeCode = document.EditBookingsForm.costCode.value;
    if ((chargeCode.trim() === "ADVFF" || chargeCode.trim() === "PBA") && window.parent.parent.document.getElementById('fowardername').value === "") {
        jQuery("#costCodeDesc").css("border-color", "red");
        alertNew("Please Select Forwarder to Add this cost Code");
        return;
    }
    if (document.EditBookingsForm.costAmount.value === "" || document.EditBookingsForm.costAmount.value === "0" ||
            document.EditBookingsForm.costAmount.value === "0.0") {
        jQuery("#costAmount").css("border-color", "red");
        alertNew("Amount Cannot be Empty or Zero");
        return;
    }
    if (document.EditBookingsForm.vendorAccountName.value === "" && chargeCode.trim() !== "ADVFF" && chargeCode.trim() !== "ADVSHP") {
        jQuery("#accountName").css("border-color", "red");
        alertNew("Please Enter the VendorName");
        return;
    }
    appendUserInfoForComments(document.EditBookingsForm.costComments, username, toDaysdate);
    //    document.EditBookingsForm.costComments.value=document.getElementById('previousComments').value+document.EditBookingsForm.costComments.value.trim();
    if (checkAddCostMappingWithGL(chargeCode,fileNo)) {
        document.EditBookingsForm.buttonValue.value = "addBookingCost";
        document.getElementById("addCost").disabled = true;
        document.EditBookingsForm.submit();
    }
}
function checkForNumberAndDecimal(obj) {
    if (!/^([0-9]+(\.[0-9]{1,4})?)$/.test(obj.value)) {
        obj.value = "";
        alertNew("The Amount you Entered is Not a Valid");
        jQuery("#costAmount").css("border-color", "red");
        return;
    } else {
        costSellValidation(obj);
    }
}
function costSellValidation(obj) {
    var str = obj.value;
    var n = str.substr(str.indexOf("."));
    if (n.length > 3 && str.indexOf(".") !== -1) {
        return obj.value = obj.value.substring(0, str.indexOf(".") + 3).trim();
    } else {
        return obj.value;
    }
}
function checkDisabledPopUP() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForDisable",
            param1: document.EditBookingsForm.vendorAccountNo.value
        },
        success: function(data) {
            if (data !== "") {
                alertNew(data);
                document.EditBookingsForm.vendorAccountNo.value = "";
                document.EditBookingsForm.accountName.value = "";
            } else {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                        methodName: "getCustInfoForCustNo",
                        param1: document.EditBookingsForm.vendorAccountNo.value,
                        dataType: "json"
                    },
                    success: function(data) {
                        fillVendorData(data);
                    }
                });
            }
        }
    });
}
function  fillVendorData(data) {
    var type;
    var array1 = new Array();
    if (data.acctType !== null) {
        type = data.acctType;
        array1 = type.split(",");
        if (null !== data.acctType && array1.length > 0 && array1.contains("V")) {
            document.EditBookingsForm.vendorAccountNo.value = data.acctNo;
        } else {
            alertNew("Select the customers with Account Type V");
            jQuery("#accountName").css("border-color", "red");
            document.EditBookingsForm.vendorAccountNo.value = "";
            document.EditBookingsForm.accountName.value = "";
        }
    }
}
function updateBookingAccruals(username, toDaysdate) {
    if (document.EditBookingsForm.costCode.value === "") {
        jQuery("#costCodeDesc").css("border-color", "red");
        alertNew("Please Enter the cost code");
        return;
    }
    if (document.EditBookingsForm.accountName.value === "") {
        jQuery("#accountName").css("border-color", "red");
        alertNew("Please Enter the VendorName and Vendor No");

        return;
    }
    if (document.EditBookingsForm.costAmount.value === "" || document.EditBookingsForm.costAmount.value === "0" ||
        document.EditBookingsForm.costAmount.value === "0.0") {
        jQuery("#costAmount").css("border-color", "red");
        alertNew("Amount Cannot be Empty or Zero");
        return;
    }
    appendUserInfoForComments(document.EditBookingsForm.costComments, username, toDaysdate);
    //        document.EditBookingsForm.costComments.value=document.getElementById('previousComments').value+document.EditBookingsForm.costComments.value.trim();//"\r"
    document.getElementById("updateCost").disabled = true;
    document.EditBookingsForm.buttonValue.value = "updateBookingCost";
    document.EditBookingsForm.submit();
}
function validateCommentsLength(value, obj, length) {
    if (parseInt(value.length) + obj.value.length < length) {
    } else {
        alertNew('More than 500 characters are not allowed');
        return (parseInt(value.length) + obj.value.length < length);
    }
}

function checkAddCostMappingWithGL(costCode,fileNo) {
    var flag = true;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkChargeAndCostMappingWithGL",
            param1: costCode,
            param2: fileNo,
            param3: 'AC',
            param4: 'BOOKING'
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