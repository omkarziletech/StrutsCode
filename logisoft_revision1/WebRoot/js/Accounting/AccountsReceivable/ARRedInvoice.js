function addLineItem() {
    var valid = checkFormFields();
    var screenName= document.arRedInvoiceForm.screenName.value;
    var fileNo =document.arRedInvoiceForm.fileNo.value;
    var chargeCode =document.arRedInvoiceForm.chargeCode.value;
    if (valid) {
        if (document.arRedInvoiceForm.bl_drNumber.value === "") {
            alertNew("Please select BL/DR Number Type ");
            document.arRedInvoiceForm.shipmentType.focus();
            return;
        } else if (document.arRedInvoiceForm.shipmentType.value === "") {
            alertNew("Please select Shipment Type ");
            document.arRedInvoiceForm.shipmentType.focus();
            return;
        } else if (document.arRedInvoiceForm.chargeCode.value === "") {
            alertNew("Please Select Charge Code");
            document.arRedInvoiceForm.amount.focus();
            return;
        } else if (document.arRedInvoiceForm.terminalCode.value === "") {
            alertNew("Please Select Terminal Code ");
            document.arRedInvoiceForm.amount.focus();
            return;
        } else if (document.arRedInvoiceForm.amount.value === "" || document.arRedInvoiceForm.amount.value === "0.0" || document.arRedInvoiceForm.amount.value === "0.00") {
            alertNew("Please Enter Invoice Amount ");
            document.arRedInvoiceForm.amount.focus();
            return;
        } else {
            document.getElementById("saveAddLineitem").disabled = true;
            var totalAmount = 0;
            if (document.getElementById("lineItems")) {
                var itemTable = document.getElementById("lineItems");
                for (i = 0; i < itemTable.rows.length; i++) {
                    var tablerowobj = itemTable.rows[i + 1];
                    if (undefined !== tablerowobj || null !== tablerowobj) {
                        var amount = tablerowobj.cells[5].innerHTML;
                        if (amount.indexOf(",") !== -1) {
                            totalAmount = Number(totalAmount) + Number(amount.replace(",", ""));
                        } else {
                            totalAmount = amount;
                        }
                    }
                }
            }
            if (document.arRedInvoiceForm.amount.value.indexOf(",") !== -1) {
                totalAmount = Number(totalAmount) + Number((document.arRedInvoiceForm.amount.value).replace(",", ""));
            } else {
                totalAmount = document.arRedInvoiceForm.amount.value;
            }
            document.arRedInvoiceForm.totalAmount.value = totalAmount;
            if(checkAddInvoiceChargeMappingWithGL(chargeCode, fileNo,screenName)) {
            document.arRedInvoiceForm.action.value = "addLineItem";
            document.arRedInvoiceForm.submit();
            }
        }
    }
}
function getCustomerContact() {
    var cust = document.arRedInvoiceForm.cusName.value;
    var customer = document.arRedInvoiceForm.accountNumber.value;
    var customerName = cust.replace("&", "amp;");
    GB_show("Contact Info", "/logisoft/customerAddress.do?buttonValue=Quotation&custNo=" + customer + "&custName=" + customerName, width = "400", height = "835");
}
function call() {
    var cvr = document.getElementById("cover");
    if (document.body && (document.body.scrollWidth || document.body.scrollHeight)) {
        var pageWidth1 = document.body.scrollWidth + 'px';
    } else if (document.body.offsetWidth) {
        var pageWidth1 = document.body.offsetWidth + 'px';
    } else {
        var pageWidth1 = '100%';
    }
    cvr.style.width = pageWidth1;
    cvr.style.height = '100%';
    cvr.style.display = "block";
    document.getElementById('newProgressBar').style.display = "block";
}
function invoicePoolSearch() {
    if (document.getElementById("txtcalfrom").value === "" && document.getElementById("txtcalto").value !== "") {
        alertNew("Please Enter From Date");
        return;
    } else if (document.getElementById("txtcalfrom").value !== "" && document.getElementById("txtcalto").value === "") {
        alertNew("Please Enter To Date");
        return;
    } else {
        call();
        document.arRedInvoiceForm.action.value = "searchInvoice";
        document.arRedInvoiceForm.submit();
    }
}
function checkFormFields() {
    if (document.arRedInvoiceForm.cusName.value === "") {
        alertNew("Please Enter Customer Name ");
        document.arRedInvoiceForm.cusName.focus();
        return false;
    } else if (document.arRedInvoiceForm.accountNumber.value === "") {
        alertNew("Please Enter Customer Number ");
        document.arRedInvoiceForm.cusName.focus();
        return false;
    } else if (document.arRedInvoiceForm.date.value === "") {
        alertNew("Please Enter Invoice Date ");
        document.arRedInvoiceForm.date.focus();
        return false;
    } else if (document.arRedInvoiceForm.dueDate.value === "") {
        alertN("Please Enter Due Date ");
        document.arRedInvoiceForm.dueDate.focus();
        return false;
    } else {
        return true;
    }
}
function updateArRedInvoice() {
    var customerName = document.arRedInvoiceForm.cusName;
    var customerNumber = document.arRedInvoiceForm.accountNumber;
    if (customerName.value === "") {
        alertNew("Please Enter Customer Name ");
        customerName.focus();
        return;
    } else if (customerNumber.value === "") {
        alertNew("Please Enter Customer Number ");
        customerNumber.focus();
        return;
    } else if (document.arRedInvoiceForm.date.value === "") {
        alertNew("Please Enter Invoice Date ");
        document.arRedInvoiceForm.date.focus();
        return;
    } else if (document.arRedInvoiceForm.dueDate.value === "") {
        alertNew("Please Enter Due Date ");
        document.arRedInvoiceForm.dueDate.focus();
        return;
    }
    document.arRedInvoiceForm.action.value = "saveOrupdateArRedInvoice";
    document.arRedInvoiceForm.submit();
}
function confirmation(message) {
    var c = confirm(message);
    if (c === true)
    {
        return true;
    }
    else {
        return false;
    }
}
function setCustomerDetails(importFlag) {
    var accountNumber = document.arRedInvoiceForm.accountNumber;
    if (accountNumber && accountNumber.value !== "") {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                methodName: "checkBlueScreenAccount",
                param1: accountNumber.value,
                dataType: "json"
            },
            success: function(data) {
                if (data) {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.accounting.AccrualsBC",
                            methodName: "getCustomerDetails",
                            param1: accountNumber.value
                        },
                        success: function(data) {
                            var json = (function() {
                                return eval('(' + data + ')');
                            })();
                            if (json) {
                                if (json.accountType === "C" && (importFlag === false)) {
                                    alertNew("Consignee Accounts are not allowed to be billed");
                                    jQuery("#cusName").val("");
                                    jQuery("#accountNumber").val("");
                                    jQuery("#arCustomertype").val("");
                                    jQuery("#contactName").val("");
                                    jQuery("#address").val("");
                                    jQuery("#phoneNumber").val("");
                                    jQuery("#term").val("");
                                } else {
                                    jQuery("#arCustomertype").val(json.vendorType);
                                    jQuery("#contactName").val(json.contactName);
                                    jQuery("#address").val(json.address);
                                    jQuery("#phoneNumber").val(json.phoneNumber);
                                    jQuery("#term").val(json.term);
                                    jQuery("#termDesc").val(json.termDesc);
                                }
                            }
                        }
                    });
                } else {
                    alertNew("The selected account has no valid account numbers linked to it.Please select a valid account");
                    jQuery("#cusName").val("");
                    jQuery("#accountNumber").val("");
                    jQuery("#arCustomertype").val("");
                    jQuery("#contactName").val("");
                    jQuery("#address").val("");
                    jQuery("#phoneNumber").val("");
                    jQuery("#term").val("");
                }
            }
        });
    }
}

function getCustomerDetails(importFlag) {
    if (navigator.appName === "Netscape") {
        setCustomerDetails(importFlag);
    } else if (event.keyCode === 13 || event.keyCode === 9 || event.button === 0) {
        setCustomerDetails(importFlag);
    }
}
function calculateDueDate() {
    var invoiceDate = document.getElementById("txtcal1");
    var dueDate = document.getElementById("txtcal2");
    if (invoiceDate && invoiceDate !== null && trim(invoiceDate.value) !== '') {
        if (Date.parse(invoiceDate.value) > Date.parse(Date()))
        {
            alertNew("Invoice Date should not be greater than Today's Date");
            invoiceDate.value = "";
            //dueDate.value="";
            invoiceDate.focus();
            return;
        }
        if (document.getElementById("term").value === 0) {
            dueDate.value = "";
        }
        else {
            var term = document.getElementById("termDesc").value;
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.dwr.DwrUtil",
                    methodName: "dueDateCalculation",
                    param1: term,
                    param2: invoiceDate.value
                },
                success: function(data) {
                    var json = (function() {
                        return eval('(' + data + ')');
                    })();
                    if (json) {
                        dueDate.value = json.newDate;
                    }
                }
            });
        }
    }
    else {
        alertNew("Please Enter Invoice Date");
        invoiceDate.focus();
    }
}
function validateInvoiceDate(date) {
    if (null !== date && date !== undefined) {
        date.value = date.value.getValidDateTime("/", ":", true);
        if (date.value === "") {
            alertNew("Please Enter valid Date");
            document.getElementById(date.id).focus();
        } else {
            this.calculateDueDate();
        }
    }
    if (date.value !== "") {
        yearValidation(date);
    }
}
function validateDueDate(date) {
    if (null !== date && date !== undefined) {
        date.value = date.value.getValidDateTime("/", ":", true);
        if (date.value === "") {
            alertNew("Please Enter valid Date");
            document.getElementById(date.id).focus();
        }
    }
    if (date.value !== "") {
        yearValidation(date);
    }
}
function showPost(obj) {
    if (obj.checked) {
        document.getElementById("postbutton").style.visibility = 'hidden';
    } else {
        document.getElementById("postbutton").style.visibility = 'visible';
    }
}

function formatAmount() {
    document.arRedInvoiceForm.amount.value = Number(document.arRedInvoiceForm.amount.value).toFixed(2);
}
function validateInvoice() {
    var customer = document.arRedInvoiceForm.accountNumber;
    var invoice = document.arRedInvoiceForm.invoiceNumber;
    if (trim(customer.value) !== '' && trim(invoice.value) !== '') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.dwr.DwrUtil",
                methodName: "validateARRedInvoiceForCustomer",
                param1: trim(customer.value),
                param2: trim(invoice.value)
            },
            success: function(data) {
                if (null !== data && data === 'AR') {
                    alertNew("This invoice " + invoice.value + " is already posted to AR");
                    invoice.value = '';
                    invoice.focus();
                }
            }
        });
    }
}
function SearchArRedInvoice(fileNo) {
    if (undefined !== fileNo && null !== fileNo && fileNo !== '') {
        document.arRedInvoiceForm.fileNo.value = fileNo;
    }
    document.arRedInvoiceForm.action.value = "searchArRedInvoice";
    document.arRedInvoiceForm.submit();
}
function listArInvoice(fileNo) {
    document.arRedInvoiceForm.fileNo.value = fileNo;
    document.arRedInvoiceForm.action.value = "listArInvoice";
    document.arRedInvoiceForm.submit();
}
function editArRedInvoice(id, fileNo) {
    document.arRedInvoiceForm.fileNo.value = fileNo;
    document.arRedInvoiceForm.action.value = "editArRedInvoice";
    document.arRedInvoiceForm.arRedInvoiceId.value = id;
    document.arRedInvoiceForm.submit();
}
function deleteArRedInvoice() {
    confirmYesOrNo("Do u want to Delete this AR Red Invoice", "deleteInvoice");
    return;
}
function getChargeCode(obj) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.dwr.DwrUtil",
            methodName: "getChargeCode",
            param1: obj.value,
            param2: 'AR',
            dataType: "json"
        },
        success: function(data) {
            ;
            jQuery("#chargeCode").empty();
            jQuery.each(data, function(index, item) {
                jQuery("#chargeCode").append("<option value='" + item.value + "'>" + item.label + "</option>");
            });
        }
    });
}
function validateBl(obj) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkBlDrNumber",
            param1: obj.value,
            dataType: "json"
        },
        async: false,
        success: function(data) {
            if (data) {
                obj.value = '';
                alertNew("BL/DR Number does not exist");
            }
        }
    });
}
function deleteAccruals(val1) {
    if (confirm("Do u want to Delete this Line Item")) {
        document.arRedInvoiceForm.accrualsId.value = val1;
        document.arRedInvoiceForm.action.value = "deleteAccruals";
        document.arRedInvoiceForm.submit();
    }
}
function addArRedInvoice(fileNo) {
    if (undefined !== fileNo && null !== fileNo && fileNo !== '') {
        document.arRedInvoiceForm.fileNo.value = fileNo;
        document.arRedInvoiceForm.action.value = "addFromBl";
        document.arRedInvoiceForm.submit();
    } else {
        document.arRedInvoiceForm.action.value = "showHome";
        document.arRedInvoiceForm.submit();
    }
}
function closeArRedInvoice(fileNo) {
    parent.parent.hideArInvoice(fileNo);
}
function contactNotification(importFlag) {
    var item = document.getElementById("index").value;
    var valid = checkFormFields();
    var acctNo = document.arRedInvoiceForm.accountNumber.value;
    if (valid) {
        if (null !== item && item === '0') {
            alertNew("No Line Items to manifest, Please Add Line Items");
        } else {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "redInvoiceValidateCustomer",
                    param1: acctNo,
                    param2:importFlag
                },
                success: function(data) {
                    if (null !== data && data !== '') {
                        alertNew("<font color='red'>" + data + "</font>");
                    } else {
                        document.getElementById("postbutton").disabled = true;
                        document.arRedInvoiceForm.action.value = "contactNotification";
                        document.arRedInvoiceForm.submit();
                    }
                }
            });
        }
    }
}
function redInvoiceMailNotification() {
    showPopUp();
    document.getElementById('contactConfigDetails').style.display = 'block';
}
function closeContactConfigDivPopUp() {
    closePopUp();
    document.getElementById('contactConfigDetails').style.display = 'none';
}
function postArRedInvoice(obj) {
    closeContactConfigDivPopUp();
    var invoiceDate = document.getElementById("txtcal1");
    if (invoiceDate && invoiceDate !== null && trim(invoiceDate.value) !== '') {
        document.arRedInvoiceForm.action.value = "postArRedInvoice";
        document.arRedInvoiceForm.notification.value = "Send";
        document.getElementById("postbutton").disabled = true;
        document.arRedInvoiceForm.submit();
    } else {
        alertNew("Please Enter Invoice Date");
        invoiceDate.focus();
    }
}
function postArRedInvoiceWithoutMail(obj){
     var invoiceDate = document.getElementById("txtcal1");
       if (invoiceDate && invoiceDate !== null && trim(invoiceDate.value) !== '') {
        document.arRedInvoiceForm.action.value = "postArRedInvoiceWithoutMail";
        document.arRedInvoiceForm.notification.value = "Send";
        document.getElementById("postbutton").disabled = true;
        document.arRedInvoiceForm.submit();
    } else {
        alertNew("Please Enter Invoice Date");
        invoiceDate.focus();
    }
     closeContactConfigDivPopUp();
}
function reverseToPost(arRedInvoiceId) {
    document.arRedInvoiceForm.action.value = "reversePostArRedInvoice";
    document.arRedInvoiceForm.arRedInvoiceId.value = arRedInvoiceId;
    document.arRedInvoiceForm.submit();
}
function previewArInvoice(id) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "previewArInvoice",
            param1: id,
            param2: "id",
            request: "true"
        },
        success: function(data) {
            if (null !== data && data !== '') {
                viewFile(data);
            }
        }
    });
}
function viewFile(file) {
    var win = window.open(rootPath + '/servlet/PdfServlet?fileName=' + file, '_new', 'width=1000,height=650,toolbar=no,directories=no,status=no,linemenubar=no,scrollbars=no,resizable=no,modal=yes');
    window.onblur = function() {
        win.focus();
    }
}
function disableDojo() {
    var path = "";
    if (document.getElementById("drNumberCheck").checked) {
        path = "&disableDojo=disable";
        Event.stopObserving("bl_drNumber", "blur");
    }
}
function checkUncheckDrNumber(obj) {
    document.getElementById('bl_drNumber').value = '';
    if (obj.checked) {
        Event.stopObserving("bl_drNumber", "blur");
    } else {
        Event.observe("bl_drNumber", "blur", function(event) {
            var element = Event.element(event);
            if (element.value !== $("bl_drNumber_check").value) {
                element.value = '';
                $("bl_drNumber_check").value = '';
                $("bl_drNumber").value = '';
            }
        }
        );
    }
}
function closeLineItem() {
    document.getElementById('addLineItems').style.display = 'none';
}
function openLineItem() {
    document.getElementById('addLineItems').style.display = 'block';
}
function PrintReports(invoiceOrBl, id) {
    var url = rootPath + "/printConfig.do?screenName=BL&arInvoice=" + invoiceOrBl + "&arInvoiceId=" + id;
    mywindow = window.open(url, '', 'width=800,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}
function openArContact() {
    var customer = document.arRedInvoiceForm.accountNumber.value;
    var customerName = document.arRedInvoiceForm.cusName.value;
    customerName = customerName.replace("&", "amp;");
    var href = "/logisoft/customerAddress.do?buttonValue=Quotation&custNo=" + customer + "&custName=" + customerName;
    mywindow = window.open(href, '', 'width=900,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}
function confirmMessageFunction(id1, id2) {
    if (id1 === 'deleteInvoice' && id2 === 'yes') {
        document.arRedInvoiceForm.action.value = "deleteArRedInvoice";
        document.arRedInvoiceForm.submit();
    } else if (id1 === 'deleteInvoice' && id2 === 'no') {
    }
}
function DisplayConfirm(id, left, top, text, point) {
    document.getElementById(id).style.left = left + "px";
    document.getElementById(id).style.top = top + "px";
    document.getElementById("innerText1").innerHTML = text;
    document.getElementById(id).style.display = "block";
    document.getElementById(id).style.left = point.x - 100 + "px";
    document.getElementById(id).style.top = point.y + "px";
    document.getElementById(id).style.zIndex = "1000";
    grayOut(true, "");
}
function confirmNew(text, jam) {
    returnValue = jam;
    DisplayConfirm("ConfirmBox", 100, 50, text, window.center({
        width: 100,
        height: 100
    }));
}
function yes() {
    document.getElementById("ConfirmBox").style.display = "none";
    //document.getElementById('cover').style.display='none';
    grayOut(false, "");
    confirmMessageFunction(returnValue, "ok");
}
function No() {
    document.getElementById("ConfirmBox").style.display = "none";
    //document.getElementById('cover').style.display='none';
    grayOut(false, "");
    confirmMessageFunction(returnValue, "cancel");
}
//setCustomerDetails();
function costSellValidation(obj) {
    var str = obj.value;
    var n = str.substr(str.indexOf("."));
    if (n.length > 3 && str.indexOf(".") !== -1) {
        obj.value = obj.value.substring(0, str.indexOf(".") + 3).trim();
        return obj.value;
    } else {
        return obj.value;
    }
}
function checkForNumberAndDecimals(obj) {
    if (!/^(-?[0-9]+(\.[0-9]{1,4})?)$/.test(obj.value)) {
        obj.value = "";
        alertNew("The Amount You Entered is Not a Valid");
        return;
    } else {
        costSellValidation(obj);
    }
}

function checkAddInvoiceChargeMappingWithGL(chargeCode,fileNo,screenName) {
        var flag = true;
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkChargeAndCostMappingWithGL",
                param1: chargeCode,
                param2: fileNo,
                param3: 'AR',
                param4: screenName
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