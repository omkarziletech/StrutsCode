function printReport(action) {
    if (action == 'printAging') {
        if (document.apReportsForm.cutOffDate.value == "") {
            alert("Please Enter Cut Off Date");
            return;
        }
    } else if (action == "printDisputedItems") {
        if (document.apReportsForm.fromDate.value == "") {
            alert("Please Enter From Date");
            return;
        } else if (document.apReportsForm.toDate.value == "") {
            alert("Please Enter To Date");
            return;
        }
    } else if (action == "printActivity") { //for Activity Report
        var vendorName = trim(document.apReportsForm.vendorName.value);
        var userName = trim(document.apReportsForm.userName.value);
        var fromDate = trim(document.apReportsForm.fromDate.value);
        var toDate = trim(document.apReportsForm.toDate.value);
        if (vendorName == "" && userName == "" && fromDate == "" && toDate == "") {
            alert("Please Enter atleast one Filter");
            return;
        } else if (fromDate != "" && toDate == "") {
            alert("Please Enter To Date");
            return;
        } else if (toDate != "" && fromDate == "") {
            alert("Please Enter From Date");
            return;
        }
    } else if (action == "printAdjustedAccruals") { //for AdjustedAccruals Report
        var vendorName = trim(document.apReportsForm.vendorName.value);
        var fromDate = trim(document.apReportsForm.fromDate.value);
        var toDate = trim(document.apReportsForm.toDate.value);
        if (vendorName == "" && fromDate == "" && toDate == "") {
            alert("Please Enter atleast one Filter");
            return;
        } else if (fromDate != "" && toDate == "") {
            alert("Please Enter To Date");
            return;
        } else if (toDate != "" && fromDate == "") {
            alert("Please Enter From Date");
            return;
        }
    } else if (action == "printDPO") {   //for DPO Report
        if (document.getElementById("searchDpoBy").value == 'AllAccountsPayable') {
            if (trim(document.apReportsForm.fromPeriod.value) == "") {
                alert("Please Enter From Date");
                document.apReportsForm.fromPeriod.focus();
                return;
            } else if (trim(document.apReportsForm.toPeriod.value) == "") {
                alert("Please Enter To Date");
                document.apReportsForm.toPeriod.focus();
                return;
            }
        } else if (document.getElementById("searchDpoBy").value == 'ByUser') {
            if (trim(document.apReportsForm.fromPeriod.value) == "") {
                document.apReportsForm.fromPeriod.focus();
                alert("Please Enter From Date");
                return;
            } else if (trim(document.apReportsForm.toPeriod.value) == "") {
                document.apReportsForm.toPeriod.focus();
                alert("Please Enter To Date");
                return;
            } else if (trim(document.apReportsForm.userName.value) == "") {
                document.apReportsForm.userName.focus();
                alert("Please Enter UserName");
                return;
            }
        } else if (document.getElementById("searchDpoBy").value == 'ByVendor') {
            if (trim(document.apReportsForm.fromPeriod.value) == "") {
                document.apReportsForm.fromPeriod.focus();
                alert("Please Enter From Date");
                return;
            } else if (trim(document.apReportsForm.toPeriod.value) == "") {
                document.apReportsForm.toPeriod.focus();
                alert("Please Enter To Date");
                return;
            } else if (trim(document.apReportsForm.vendorName.value) == "") {
                document.apReportsForm.vendorName.focus();
                alert("Please Enter vendorName");
                return;
            }
        }
    } else if (action == "printStatement") {
        var vendorName = trim(document.apReportsForm.vendorName.value);
        var apSpecialist = document.apReportsForm.apSpecialist.value;
        if (vendorName == "" && apSpecialist == "" && !document.apReportsForm.allCustomers.checked) {
            alert("Please enter either Customer or select All customers or select AP Specialist");
            document.apReportsForm.vendorName.focus();
            return;
        }
    }
    document.apReportsForm.action.value = action;
    document.apReportsForm.submit();
}
function exportToExcel(action) {
    if (action == 'exportAgingToExcel') {
        if (document.apReportsForm.cutOffDate.value == "") {
            alert("Please Enter Cut Off Date");
            return;
        }
    } else if (action == "exportDisputedItemsToExcel") {
        if (document.apReportsForm.fromDate.value == "") {
            alert("Please Enter From Date");
            return;
        } else if (document.apReportsForm.toDate.value == "") {
            alert("Please Enter To Date");
            return;
        }
    } else if (action == "exportUserToExcel") {
        var vendorName = trim(document.apReportsForm.vendorName.value);
        var userName = trim(document.apReportsForm.userName.value);
        var fromDate = trim(document.apReportsForm.fromDate.value);
        var toDate = trim(document.apReportsForm.toDate.value);
        if (vendorName == "" && userName == "" && fromDate == "" && toDate == "") {
            alert("Please Enter atleast one Filter");
            return;
        } else if (fromDate != "" && toDate == "") {
            alert("Please Enter To Date");
            return;
        } else if (toDate != "" && fromDate == "") {
            alert("Please Enter From Date");
            return;
        }
    } else if (action == "exportAdjustedAccrualsToExcel") {
        var vendorName = trim(document.apReportsForm.vendorName.value);
        var fromDate = trim(document.apReportsForm.fromDate.value);
        var toDate = trim(document.apReportsForm.toDate.value);
        if (vendorName == "" && fromDate == "" && toDate == "") {
            alert("Please Enter atleast one Filter");
            return;
        } else if (fromDate != "" && toDate == "") {
            alert("Please Enter To Date");
            return;
        } else if (toDate != "" && fromDate == "") {
            alert("Please Enter From Date");
            return;
        }
    } else if (action == "exportDPOToExcel") { //For DPO Report
        if (document.getElementById("searchDpoBy").value == 'AllAccountsPayable') {
            if (trim(document.apReportsForm.fromPeriod.value) == "") {
                alert("Please Enter From Date");
                document.apReportsForm.fromPeriod.focus();
                return;
            } else if (trim(document.apReportsForm.toPeriod.value) == "") {
                alert("Please Enter To Date");
                document.apReportsForm.toPeriod.focus();
                return;
            }
        } else if (document.getElementById("searchDpoBy").value == 'ByUser') {
            if (trim(document.apReportsForm.fromPeriod.value) == "") {
                document.apReportsForm.fromPeriod.focus();
                alert("Please Enter From Date");
                return;
            } else if (trim(document.apReportsForm.toPeriod.value) == "") {
                document.apReportsForm.toPeriod.focus();
                alert("Please Enter To Date");
                return;
            } else if (trim(document.apReportsForm.userName.value) == "") {
                document.apReportsForm.userName.focus();
                alert("Please Enter UserName");
                return;
            }
        } else if (document.getElementById("searchDpoBy").value == 'ByVendor') {
            if (trim(document.apReportsForm.fromPeriod.value) == "") {
                document.apReportsForm.fromPeriod.focus();
                alert("Please Enter From Date");
                return;
            } else if (trim(document.apReportsForm.toPeriod.value) == "") {
                document.apReportsForm.toPeriod.focus();
                alert("Please Enter To Date");
                return;
            } else if (trim(document.apReportsForm.vendorName.value) == "") {
                document.apReportsForm.vendorName.focus();
                alert("Please Enter vendorName");
                return;
            }
        }
    } else if (action == "exportStatementToExcel") {
        var vendorName = trim(document.apReportsForm.vendorName.value);
        var apSpecialist = document.apReportsForm.apSpecialist.value;
        if (vendorName == "" && apSpecialist == "" && !document.apReportsForm.allCustomers.checked) {
            alert("Please enter either Customer or select All customers or select AP Specialist");
            document.apReportsForm.vendorName.focus();
            return;
        }
    }
    document.apReportsForm.action.value = action;
    document.apReportsForm.submit();
}
function clearValues(action) {
    document.apReportsForm.action.value = action;
    document.apReportsForm.submit();
}


function grayOutFields(allCustomer) {
    if (allCustomer.checked) {
        document.apReportsForm.vendorName.value = '';
        document.apReportsForm.vendorNumber.value = '';
        document.apReportsForm.vendorName.disabled = true;
        document.apReportsForm.vendorNumber.disabled = true;
    } else {
        document.apReportsForm.vendorName.disabled = false;
        document.apReportsForm.vendorNumber.disabled = false;
    }
}

function validateDate(date) {
    if (date.value != "") {
        date.value = date.value.getValidDateTime("/", "", false);
        if (date.value == "") {
            alert("Please Enter valid Date");
            document.getElementById(date.id).focus();
        }
    }
}

function validateDate(date, dateCondition) {
    if (date.value != "") {
        date.value = date.value.getValidDateTime("/", "", false);
        if (date.value == "") {
            alert("Please Enter valid Date");
            document.getElementById(date.id).focus();
        }
    }
    if (date.value != "") {
        var fromDate = document.getElementById("txtcal1").value;
        var toDate = document.getElementById("txtcal2").value;
        if (Date.parse(fromDate) > Date.parse(toDate)) {
            if (dateCondition == 'from') {
                alert("From date should not be greater than to date");
                document.getElementById("txtcal1").value = "";
                document.getElementById("txtcal1").focus();
            } else {
                alert("To date should not be less than from date");
                document.getElementById("txtcal2").value = "";
                document.getElementById("txtcal2").focus();
            }
        }
    }
}

function validate(date) {
    if (date.value != '') {
        date.value = date.value.getValidDateTime("/", "", false);
        if (date.value == "") {
            alert("Please Enter valid Date");
            document.getElementById(date.id).focus();
        }
    }
}

function getNumberOfDays(ele) {
    var fromDate = document.getElementById("fromPeriod");
    var toDate = document.getElementById("toPeriod");
    if (Date.parse(document.apReportsForm.fromPeriod.value) > Date.parse(document.apReportsForm.toPeriod.value)) {
        alert("From date should not be greater than To date");
        if (ele == "fromDate") {
            document.getElementById("fromPeriod").value = "";
            document.apReportsForm.fromPeriod.focus();
        } else {
            document.getElementById("toPeriod").value = "";
            document.apReportsForm.toPeriod.focus();
        }
        document.getElementById("numberOfDays").value = "";
        return;
    }
    if (trim(fromDate.value) == "") {
        document.getElementById("numberOfDays").value = "";
        return;
    } else if (trim(toDate.value) == "") {
        document.getElementById("numberOfDays").value = "";
        return;
    }
    if (document.getElementById("fromPeriodId").value !== ""
            && document.getElementById("toPeriodId").value !== "") {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.accounting.FiscalPeriodBC",
                methodName: "getNumberOfDays",
                param1: document.getElementById("fromPeriodId").value,
                param2: document.getElementById("toPeriodId").value
            },
            success: function (data) {
                document.getElementById("numberOfDays").value = data;
            }
        });
    }
}

function clearToPeriodValues() {
    document.getElementById("toPeriod").value = "";
    document.getElementById("numberOfDays").value = "";
}
function clearFromPeriodValues() {
    document.getElementById("fromPeriod").value = "";
    document.getElementById("numberOfDays").value = "";
}

if (document.getElementById("userName")) {
    AjaxAutocompleter("userName", "userNameDiv", "userId", "userNameValid", rootPath + "/servlet/AutoCompleterServlet?action=User&get=id&textFieldId=userName", "", "");
}
if (document.getElementById("fromPeriod")) {
    initAutocompleteWithFormClear("fromPeriod", "fromPeriodChoices", "fromPeriodId", "fromPeriodValid", rootPath + "/servlet/AutoCompleterServlet?action=FiscalPeriod&textFieldId=fromPeriod&from=ApReports", "getNumberOfDays('fromDate')", "clearFromPeriodValues()");
}
if (document.getElementById("toPeriod")) {
    initAutocompleteWithFormClear("toPeriod", "toPeriodChoices", "toPeriodId", "toPeriodValid", rootPath + "/servlet/AutoCompleterServlet?action=FiscalPeriod&textFieldId=toPeriod&from=ApReports", "getNumberOfDays('toDate')", "clearToPeriodValues()");
}
if (document.getElementById("vendorName")) {
    var vendorUrl = rootPath + "/servlet/AutoCompleterServlet?action=Vendor&textFieldId=vendorName&accountType=V";
    AjaxAutocompleter("vendorName", "vendorNameChoices", "vendorNumber", "vendorNameCheck", vendorUrl, "");
}
function onchangeDpo() {
    if (document.getElementById("searchDpoBy").value == 'AllAccountsPayable') {
        document.getElementById("userName").value = "";
        document.getElementById("userName").className = "textlabelsBoldForTextBoxDisabledLook";
        document.getElementById("userName").disabled = true;
        document.getElementById("vendorName").value = "";
        document.getElementById("vendorName").className = "textlabelsBoldForTextBoxDisabledLook";
        document.getElementById("vendorName").disabled = true;
        document.getElementById("vendorName").value = "";
        document.getElementById("vendorNumber").value = "";
    } else if (document.getElementById("searchDpoBy").value == 'ByUser') {
        document.getElementById("userName").className = "textlabelsBoldForTextBox";
        document.getElementById("userName").disabled = false;
        document.getElementById("vendorName").value = "";
        document.getElementById("vendorName").className = "textlabelsBoldForTextBoxDisabledLook";
        document.getElementById("vendorName").disabled = true;
        document.getElementById("vendorNumber").value = "";
    } else if (document.getElementById("searchDpoBy").value == 'ByVendor') {
        document.getElementById("vendorName").className = "textlabelsBoldForTextBox";
        document.getElementById("vendorName").disabled = false;
        document.getElementById("userName").value = "";
        document.getElementById("userName").className = "textlabelsBoldForTextBoxDisabledLook";
        document.getElementById("userName").disabled = true;
    }
}

function gotoAction(action) {
    var fromDate = document.getElementById("txtcal1").value;
    var toDate = document.getElementById("txtcal2").value;
    if (fromDate != "" && toDate == "") {
        alert("Please enter To date");
        document.getElementById("txtcal2").focus();
    } else if (toDate != "" && fromDate == "") {
        alert("Please enter from date");
        document.getElementById("txtcal1").focus();
    } else {
        document.getElementById("action").value = action;
        document.apReportsForm.submit();
    }
}

function gotoPage(pageNo) {
    document.getElementById("pageNo").value = pageNo;
    document.getElementById("action").value = "getForDisputedEmailLog";
    document.apReportsForm.submit();
}

function gotoSelectedPage() {
    document.getElementById("pageNo").value = document.getElementById("selectedPageNo").value;
    document.getElementById("action").value = "getForDisputedEmailLog";
    document.apReportsForm.submit();
}

function doSort(sortBy, orderBy) {
    document.getElementById("sortBy").value = sortBy;
    document.getElementById("orderBy").value = orderBy;
    document.getElementById("action").value = "getForDisputedEmailLog";
    document.apReportsForm.submit();
}
