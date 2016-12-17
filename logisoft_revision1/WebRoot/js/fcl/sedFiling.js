function openSchedDiv() {
    var schedSize = document.getElementById("schedSize").value;
    if (schedSize >= 10) {
        alertNew("Cannot add more than 10 Schedule B Details");
    } else {
        document.getElementById("ShowValues").style.display = "block";
        clearSchedBValues();
        document.getElementById("updateDiv").style.display = "none";
        document.getElementById("submitDiv").style.display = "block";
    }
}
function showMoreInfo(id) {
    removemandatory();
    document.sedFilingForm.buttonValue.value = "edit";
    document.sedFilingForm.schedId.value = id;
    document.sedFilingForm.submit();
}
function deleteInfo(id) {
    if (confirm("Are you sure do you want to delete the Schedule B")) {
        document.sedFilingForm.buttonValue.value = "delete";
        document.sedFilingForm.schedId.value = id;
        document.sedFilingForm.submit();
    }
}
function validateDate(data) {
    if (data.value != "") {
        data.value = data.value.getValidDateTime("/", "", false);
        if (data.value == "" || data.value.length > 10) {
            alertNew("Please enter valid date");
            data.value = "";
            document.getElementById(data.id).focus();
        }
    }
}
function closeDiv() {
    document.getElementById("ShowValues").style.display = "none";
}
function saveValues(action) {
    var mandotary = "";
    if (trim(document.getElementById("scheduleB_Number").value) == '' && document.sedFilingForm.exportInformationCode.value != 'HH') {
        mandotary = "--> Please Enter Schedule B Number<br>";
    }
    if (trim(document.getElementById("description1").value) == '') {
        mandotary = mandotary + "--> Please Enter Description1<br>";
    }
    if (document.sedFilingForm.uom1.value != 'X NO UNIT REQUIRED') {
        if (trim(document.getElementById("units1").value) != '' && trim(document.getElementById("quantities1").value) == '') {
            mandotary = mandotary + "--> Please Enter Quantities1<br>";
        }
    }
    if (trim(document.getElementById("weight").value) == '') {
        mandotary = mandotary + "--> Please Enter Weight<br>";
    }
    if (trim(document.getElementById("value").value) == '') {
        mandotary = mandotary + "--> Please Enter Value<br>";
    }
    if (trim(document.getElementById("exportInformationCode").value) == '') {
        mandotary = mandotary + "--> Please Select Export Information Code<br>";
    }
    if (trim(document.getElementById("licenseType").value) == '') {
        mandotary = mandotary + "--> Please Select License Code<br>";
    }
    if (mandotary != '') {
        alertNew(mandotary);
    } else {
        if (action == 'submit') {
            document.getElementById("SubmitSchB").disabled = true;
        } else if (action == 'update') {
            document.getElementById("updateSchB").disabled = true;
        }
        document.sedFilingForm.buttonValue.value = action;
        document.sedFilingForm.submit();
    }
}
function enableFtZone() {
    if (trim(document.getElementById("inbnd").value) == "") {
        document.getElementById("inbent").value = "";
        document.getElementById("ftzone").value = "";
    }
}
function fillSchedBInfo() {
  //  if (event.keyCode == 9 || event.keyCode == 13 || event.keyCode == 0) {
        document.getElementById("schedB_check").value = document.getElementById("scheduleB_Number").value;
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                methodName: "getSchedBDesc",
                param1: document.getElementById("scheduleB_Number").value,
                dataType: "json"
            },
            success: function (data) {
                if (null != data && undefined != data[0] && null != data[0]) {
                    document.getElementById("scheduleB_Name").value = data[0];
                    document.getElementById("schedBName_check").value = data[0];
                    if (undefined != data[1] && null != data[1] && data[1] != '') {
                        var uom1 = data[1].split("-->");
                        document.sedFilingForm.uom1.value = uom1[0];
                        document.sedFilingForm.units1.value = uom1[1];
                        if (uom1[0] != 'X NO UNIT REQUIRED') {
                            document.sedFilingForm.quantities1.className = "textlabelsBoldForTextBox mandatory";
                        } else {
                            document.sedFilingForm.quantities1.className = "textlabelsBoldForTextBox";
                        }
                    } else {
                        document.sedFilingForm.units1.value = "";
                        document.sedFilingForm.quantities1.className = "textlabelsBoldForTextBox";
                    }
                    if (undefined != data[2] && null != data[2] && data[2] != '') {
                        var uom2 = data[2].split("-->");
                        document.sedFilingForm.uom2.value = uom2[0];
                        document.sedFilingForm.units2.value = uom2[1];
                    } else {
                        document.sedFilingForm.uom2.value = "";
                        document.sedFilingForm.units2.value = "";
                    }
                }
            }
        });
   // }
}
function removemandatory() {
    if (document.sedFilingForm.exportInformationCode.value == 'HH') {
        document.getElementById("scheduleB_Name").className = "textlabelsBoldForTextBox";
        document.getElementById("scheduleB_Number").className = "textlabelsBoldForTextBox";

    } else {
        document.getElementById("scheduleB_Name").className = "textlabelsBoldForTextBox mandatory";
        document.getElementById("scheduleB_Number").className = "textlabelsBoldForTextBox mandatory";
    }
}
function changeSelectedTab(tabNumber) {
    document.sedFilingForm.selectedTab.value = tabNumber;
}

function gotoSelectedTab() {
    if (document.sedFilingForm.buttonValue.value == "edit") {
        document.getElementById("ShowValues").style.display = "block";
        document.getElementById("updateDiv").style.display = "block";
        document.getElementById("submitDiv").style.display = "none";
    } else {
        document.getElementById("updateDiv").style.display = "none";
        document.getElementById("submitDiv").style.display = "block";
    }
}
function showSchedB() {
    document.getElementById("status").disabled = false;
    document.sedFilingForm.buttonValue.value = "showSched";
    document.sedFilingForm.selectedTab.value = 3;
    document.sedFilingForm.submit();
}
function clearSchedBValues() {
    jQuery("#domesticOrForeign").val("D");
    jQuery("#scheduleB_Number").val("");
    jQuery("#scheduleB_Name").val("");
    jQuery("#description1").val("");
    jQuery("#description2").val("");
    jQuery("#quantities1").val("");
    jQuery("#units1").val("");
    jQuery("#units2").val("");
    jQuery("#quantities2").val("");
    jQuery("#weight").val("");
    jQuery("#weightType").val("K");
    jQuery("#value").val("");
    jQuery("#exportInformationCode").val("OS");
    jQuery("#licenseType").val("C33");
    jQuery("#exportLicense").val("");
    jQuery("#usedVehicle").val("");
    jQuery("#vehicleIdType").val("1");
    jQuery("#vehicleIdNumber").val("");
    jQuery("#vehicleTitleNumber").val("");
    jQuery("#vehicleState").val("AK");
    jQuery("#eccn").val("");
}
function validateIrs(obj) {
    var irsNo = obj.value.replace("-", "");
    if (irsNo.length != '9' && irsNo.length != '11' && irsNo.length != '13') {
        alertNew("IRS# Length Should be 9 or 11 or 13");
    }
}
function backToAesFiling() {
    window.location.href = rootPath + '/jsps/EdiTracking/AesTracking.jsp';
}
function makeSedEditMode(form) {
    window.parent.makeFormBorderless(form);
}
function closeSchedB(index) {
    var schedSize = document.getElementById("schedSize").value;
    parent.parent.changeSchedButtonColor(schedSize, index);
}

//This is for sed
function getSedShipper() {
   // if (event.keyCode == 9 || event.keyCode == 13 || event.keyCode == 0) {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                methodName: "getCustInfoForCustNo",
                param1: document.getElementById("expnum").value,
                dataType: "json"
            },
            success: function (data) {
                populateSedShipper(data);
            }
        });
//}
}
function populateSedShipper(data) {
    if (data && null !== data.acctNo) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: data.acctNo
            },
            success: function (result) {
                if (result !== "") {
                    alertNew(result);
                    document.getElementById("expnum").value = "";
                    document.getElementById("expnam").value = "";
                    document.getElementById("expadd").value = "";
                    document.getElementById("expcty").value = "";
                    document.getElementById("expsta").value = "";
                    document.getElementById("expzip").value = "";
                    document.getElementById("expcfn").value = "";
                    document.getElementById("expcpn").value = "";
                    document.getElementById("expirs").value = "";
                    document.getElementById("expirsTp").value = "";
                } else {
                    fillSedShipper(data);
                }
            }
        });
    }
}
function fillSedShipper(data) {
    var type;
    var array1 = new Array();
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (array1.contains("S") || array1.contains("F") || array1.contains("Z") || array1.contains("E") || array1.contains("I")) {
        if (data.acctNo != "") {
            document.getElementById("expnum").value = data.acctNo;
        } else {
            document.getElementById("expnum").value = "";
        }
        document.getElementById("expadd").value = data.address1.replace(/[\r\n]+/g, "");
        document.getElementById("expcty").value = data.city1;
        document.getElementById("expsta").value = data.state;
        document.getElementById("expzip").value = data.zip;
        document.getElementById("expcfn").value = data.coName;
        document.getElementById("expcpn").value = data.phone.replace(/[\(\)\.\-\s,]+/g, "");
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                methodName: "getGeneralInfoForCustNo",
                param1: data.acctNo,
                dataType: "json"
            },
            success: function (data) {
                if (null != data && undefined != data) {
                    if (null != data.idText && data.idText != '') {
                        document.getElementById("expirs").value = data.idText;
                        document.getElementById("expirs").className = "BackgrndColorForTextBox mandatory";
                        document.getElementById("expirs").readOnly = true;
                        document.getElementById("expirs").tabIndex = -1;
                        document.getElementById("expirsTp").value = "Y";

                    } else {
                        document.getElementById("expirs").value = "";
                        document.getElementById("expirs").className = "textlabelsBoldForTextBox mandatory";
                        document.getElementById("expirs").readOnly = false;
                        document.getElementById("expirs").tabIndex = 0;
                        document.getElementById("expirsTp").value = "N";
                    }
                    if (null != data.idType) {
                        document.getElementById("expicd").value = data.idType;
                    } else {
                        document.getElementById("expicd").value = "";
                    }
                    if (data.poa == 'Y') {
                        document.sedFilingForm.exppoa[0].checked = true;
                    } else {
                        document.sedFilingForm.exppoa[1].checked = true;
                    }
                }
            }
        });
    } else {
        alertNew("Select the Customers with Account Type S,F,E I,or Z");
        document.getElementById("expnum").value = "";
        document.getElementById("expnam").value = "";
        document.getElementById("expadd").value = "";
        document.getElementById("expcty").value = "";
        document.getElementById("expsta").value = "";
        document.getElementById("expzip").value = "";
        document.getElementById("expcfn").value = "";
        document.getElementById("expcpn").value = "";
        document.getElementById("expirs").value = "";
        document.getElementById("expirsTp").value = "";
    }
}
function getSedConsignee() {
   // if (event.keyCode == 9 || event.keyCode == 13 || event.keyCode == 0) {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                methodName: "getCustInfoForCustNo",
                param1: document.getElementById("connum").value,
                dataType: "json"
            },
            success: function (data) {
                populateSedConsignee(data);
            }
        });
   // }
}

function populateSedConsignee(data) {
    if (data && null !== data.acctNo) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: data.acctNo
            },
            success: function (result) {
                if (result !== "") {
                    alertNew(result);
                    document.getElementById("connum").value = "";
                    document.getElementById("connam").value = "";
                    document.getElementById("conadd").value = "";
                    document.getElementById("concty").value = "";
                    document.getElementById("consta").value = "";
                    document.getElementById("conpst").value = "";
                    document.getElementById("concfn").value = "";
                    document.getElementById("concpn").value = "";
                } else {
                    fillSedConsignee(data);
                }
            }
        });
    }
}

function fillSedConsignee(data) {
    var type;
    var array1 = new Array();
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (array1.contains("C")) {
        if (data.acctNo != "") {
            document.getElementById("connum").value = data.acctNo;
        } else {
            document.getElementById("connum").value = "";
        }
        document.getElementById("conadd").value = data.address1.replace(/[\r\n]+/g, "");
        document.getElementById("concty").value = data.city1;
        document.getElementById("consta").value = data.state;
        if (null != data.cuntry && null != data.cuntry.code) {
            document.getElementById("conctry").value = data.cuntry.code;
        }
        document.getElementById("conpst").value = data.zip;
        document.getElementById("concfn").value = data.coName;
        document.getElementById("concpn").value = data.phone;
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                methodName: "getGeneralInfoForCustNo",
                param1: data.acctNo,
                dataType: "json"
            },
            success: function (data) {
                if (null != data && undefined != data) {
                    if (data.poa == 'Y') {
                        document.sedFilingForm.conpoa[0].checked = true;
                    } else {
                        document.sedFilingForm.conpoa[1].checked = true;
                    }
                }
            }
        });
    } else {
        alertNew("Select the Customers with Account Type C");
        document.getElementById("connum").value = "";
        document.getElementById("connam").value = "";
        document.getElementById("conadd").value = "";
        document.getElementById("concty").value = "";
        document.getElementById("conctry").value = "";
        document.getElementById("consta").value = "";
        document.getElementById("conpst").value = "";
        document.getElementById("concfn").value = "";
        document.getElementById("concpn").value = "";
    }
}
function consigneeState() {
    var cntdes = document.getElementById("cntdes").value;
    if (cntdes == 'MX' || cntdes == 'CA' || cntdes == 'US') {
        document.getElementById("consigneeStateId").style.display = "block";
        document.getElementById("consta").value = cntdes;
    } else {
        document.getElementById("consta").value = "";
        document.getElementById("consigneeStateId").style.display = "none";
    }
}
function getSedForwarder() {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
            methodName: "getCustInfoForCustNo",
            param1: document.getElementById("frtnum").value,
            dataType: "json"
        },
        success: function (data) {
            populateSedForwarder(data);
        }
    });
}
function populateSedForwarder(data) {
    if (data && null !== data.acctNo) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: data.acctNo
            },
            success: function (result) {
                if (result !== "") {
                    alertNew(result);
                    document.getElementById("frtnum").value = "";
                    document.getElementById("frtnam").value = "";
                    document.getElementById("frtadd").value = "";
                    document.getElementById("frtcty").value = "";
                    document.getElementById("frtsta").value = "";
                    document.getElementById("frtzip").value = "";
                    document.getElementById("frtirs").value = "";
                    document.getElementById("frtirsTp").value = "";
                } else {
                    fillSedForwarder(data);
                }
            }
        });
    }
}

function fillSedForwarder(data) {
    var type;
    if (data.acctType != null) {
        type = data.acctType;
    }
    if (((data.subType).toLowerCase()) == 'forwarder') {
        if (data.acctNo != "") {
            document.getElementById("frtnum").value = data.acctNo;
        } else {
            document.getElementById("frtnum").value = "";
        }
        document.getElementById("frtadd").value = data.address1.replace(/[\r\n]+/g, "");
        document.getElementById("frtcty").value = data.city1;
        document.getElementById("frtsta").value = data.state;
        document.getElementById("frtzip").value = data.zip;
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                methodName: "getGeneralInfoForCustNo",
                param1: data.acctNo,
                dataType: "json"
            },
            success: function (data) {
                if (null != data && undefined != data) {
                    if (null != data.idText && data.idText != '') {
                        document.getElementById("frtirs").value = data.idText;
                        document.getElementById("frtirsTp").value = "Y";
                        document.getElementById("frtirs").className = "BackgrndColorForTextBox";
                        document.getElementById("frtirs").readOnly = true;
                        document.getElementById("frtirs").tabIndex = -1;
                    } else {
                        document.getElementById("frtirs").value = "";
                        document.getElementById("frtirsTp").value = "N";
                        document.getElementById("frtirs").className = "textlabelsBoldForTextBox";
                        document.getElementById("frtirs").readOnly = false;
                        document.getElementById("frtirs").tabIndex = 0;
                    }
                    if (null != data.idType) {
                        document.getElementById("frticd").value = data.idType;
                    } else {
                        document.getElementById("frticd").value = "";
                    }
                }
            }
        });
    } else {
        alertNew("Select the Customers with Vendor (Sub type Forwarder)");
        document.getElementById("frtnum").value = "";
        document.getElementById("frtnam").value = "";
        document.getElementById("frtadd").value = "";
        document.getElementById("frtcty").value = "";
        document.getElementById("frtsta").value = "";
        document.getElementById("frtzip").value = "";
        document.getElementById("frtirs").value = "";
        document.getElementById("frtirsTp").value = "";
    }
}

function checkInbondNo(obj) {
    if (event.keyCode != 9) {
        if (trim(document.getElementById("inbnd").value) == "") {
            alertNew('Please Enter Inbond Number');
            obj.value = "";
        }
    }
}
function validateAes() {
    var mandotary = "";
    var irs = document.sedFilingForm.expirs.value;
    if (null != irs && undefined != irs) {
        irs = irs.replace("-", "");
    }
    if (trim(document.sedFilingForm.trnref.value) == '') {
        mandotary = "--> Please Enter Transaction Reference Number<br>";
    }
    if (trim(document.sedFilingForm.cntdes.value) == '') {
        mandotary = mandotary + "--> Please Enter Destination<br>";
    }
    if (trim(document.sedFilingForm.orgsta.value) == '') {
        mandotary = mandotary + "--> Please Enter Origin<br>";
    }
    if (trim(document.sedFilingForm.exppnm.value) == '') {
        mandotary = mandotary + "--> Please Enter Port of Loading<br>";
    }
    if (trim(document.sedFilingForm.upptna.value) == '') {
        mandotary = mandotary + "--> Please Enter Port of Discharge<br>";
    }
    if (trim(document.sedFilingForm.scac.value) == '') {
        mandotary = mandotary + "--> Please Enter SCAC Code<br>";
    }
    if (trim(document.sedFilingForm.vesnam.value) == '') {
        mandotary = mandotary + "--> Please Select Vessel Name<br>";
    }
    if (trim(document.sedFilingForm.email.value) == '') {
        mandotary = mandotary + "--> Please Enter Email<br>";
    }
    if (trim(document.sedFilingForm.expnam.value) == '') {
        mandotary = mandotary + "--> Please Enter Shipper Name<br>";
    }
    if (trim(document.sedFilingForm.expadd.value) == '') {
        mandotary = mandotary + "--> Please Enter Shipper Address<br>";
    }
    if (trim(document.sedFilingForm.expcty.value) == '') {
        mandotary = mandotary + "--> Please Enter Shipper City<br>";
    }
    if (trim(document.sedFilingForm.expsta.value) == '') {
        mandotary = mandotary + "--> Please Enter Shipper State<br>";
    }
    if (trim(document.sedFilingForm.expzip.value) == '') {
        mandotary = mandotary + "--> Please Enter Shipper Zip<br>";
    }
    if (trim(document.sedFilingForm.expcfn.value) == '') {
        mandotary = mandotary + "--> Please Enter Shipper First Name<br>";
    }
    if (trim(document.sedFilingForm.expcpn.value) == '') {
        mandotary = mandotary + "--> Please Enter Shipper Phone Number<br>";
    }
    if (trim(document.sedFilingForm.expirs.value) == '') {
        mandotary = mandotary + "--> Please Enter Shipper IRS Number<br>";
    } else if (irs.length != '9' && irs.length != '11' && irs.length != '13') {
        mandotary = mandotary + "--> Shipper IRS# Length Should be 9 or 11 or 13<br>";
    }
    if (trim(document.sedFilingForm.expicd.value) == '') {
        mandotary = mandotary + "--> Please Enter Type in USPPI Section<br>";
    }
    if (trim(document.sedFilingForm.connam.value) == '') {
        mandotary = mandotary + "--> Please Enter Consignee Name<br>";
    }
    if (trim(document.sedFilingForm.conadd.value) == '') {
        mandotary = mandotary + "--> Please Enter Consignee Address<br>";
    }
    if (trim(document.sedFilingForm.concty.value) == '') {
        mandotary = mandotary + "--> Please Enter Consignee City<br>";
    }
    if (trim(document.sedFilingForm.inbnd.value) != '') {
        if (document.sedFilingForm.ftzone.value == '' && document.sedFilingForm.inbent.value == '') {
            mandotary = mandotary + "--> Please Enter either Ftzone or Entry# <br>";
        }
    }
    return mandotary;
}
function changeSedStatusColor(status) {
    if (status == 'E') {
        document.getElementById("filingSed").style.backgroundColor = "red";
        document.getElementById("filingSedDownButton").style.backgroundColor = "red";
    } else if (status == 'C') {
        document.getElementById("filingSed").style.backgroundColor = "#2AA900";
        document.getElementById("filingSedDownButton").style.backgroundColor = "#2AA900";
    } else if (status == 'S') {
        document.getElementById("filingSed").style.backgroundColor = "#FDF701";
        document.getElementById("filingSedDownButton").style.backgroundColor = "#FDF701";
    }
}
function checkSchedAvailability(fileNo) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkSchedBAvailability",
            param1: fileNo,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data) {
                alertNew("Please Enter Atleast one Sched B Information");
            }
        }
    });
}

function onlyNumbers(val) {
    val.value = val.value.replace(/\s/g, '');
    if (!isInteger(val.value)) {
        val.value = val.value.substring(0, val.value.length - 1);
    }
}
function formatUnitNo(val) {
    if (event.keyCode != 8 && event.keyCode != 46 && event.keyCode != 9) {
        val.value = val.value.replace(/\s/g, '');
        var inputValue = val.value;
        if (inputValue.length < 5 && isNotAlphabetic(inputValue)) {
            val.value = inputValue.substring(0, inputValue.length - 1);
            alertNew("Please enter alphabetic value");
            return;
        }

        if (inputValue.length > 4) {
            if (inputValue.length == 5) {
                if (!isInteger(inputValue.substring(4))) {
                    val.value = inputValue.substring(0, inputValue.length - 1);
                    alertNew("Please enter numeric value");
                    return;
                } else {
                    if (inputValue.length == 5) {
                        val.value = inputValue.substring(0, inputValue.length - 1) + "-" + inputValue.substring(inputValue.length - 1);
                    }
                }
            } else if (!isInteger(inputValue.substring(5, 11))) {
                val.value = inputValue.substring(0, inputValue.length - 1);
                alertNew("Please enter numeric value");
                return;
            } else {
                if (inputValue.length == 12) {
                    if (!isInteger(inputValue.substring(11))) {
                        val.value = inputValue.substring(0, inputValue.length - 1);
                        alertNew("Please enter numeric value");
                        return;
                    } else {
                        val.value = inputValue.substring(0, inputValue.length - 1) + "-" + inputValue.substring(inputValue.length - 1);
                    }
                }
                if (inputValue.length == 13) {
                    if (!isInteger(inputValue.substring(12))) {
                        val.value = inputValue.substring(0, inputValue.length - 1);
                        alertNew("Please enter numeric value");
                        return;
                    }
                }
            }
        }
    }
}
function disableAutoComplete(obj) {
    if (obj.checked) {
        if (obj.id == "consigneeCheck") {
            if (document.getElementById("connam").value != "") {
                confirmYesOrNo("Do you want to clear existing Consignee Details", "uncheckClearConsignee");
            } else {
                Event.observe("connam", "blur", function (event) {
                    var element = Event.element(event);
                    if (element.value != $("sedConsigneeName_check").value) {
                        element.value = '';
                        $("sedConsigneeName_check").value = '';
                        $("connam").value = '';
                        $("connum").value = '';
                        $("conadd").value = '';
                        $("concty").value = '';
                        $("consta").value = '';
                        $("conpst").value = '';
                    }
                }
                );
            }
        } else if (obj.id == "shipperCheck") {
            if (document.getElementById("expnam").value != "") {
                confirmYesOrNo("Do you want to clear existing Shipper Details", "uncheckClearShipper");
            } else {
                Event.observe("expnam", "blur", function (event) {
                    var element = Event.element(event);
                    if (element.value != $("sedhouseName_check").value) {
                        element.value = '';
                        $("sedhouseName_check").value = '';
                        $("expnam").value = '';
                        $("expnum").value = '';
                        $("expadd").value = '';
                        $("expcty").value = '';
                        $("expsta").value = '';
                        $("expzip").value = '';
                        $("expirs").value = '';
                        $("expicd").value = '';
                    }
                }
                );
            }
        }

    } else {
        if (obj.id == "consigneeCheck") {
            if (document.getElementById("connam").value != "") {
                confirmYesOrNo("Do you want to clear existing Consignee Details", "clearConsignee");
            } else {
                Event.stopObserving("connam", "blur");
            }
        } else if (obj.id == "shipperCheck") {
            if (document.getElementById("expnam").value != "") {
                confirmYesOrNo("Do you want to clear existing Shipper Details", "clearShipper");
            } else {
                Event.stopObserving("expnam", "blur");
            }
        }
    }
}
function disableDojo(obj) {
    var path = "";
    var disable = 'disable';
    if (obj.id == "connam") {
        if (!document.getElementById("consigneeCheck").checked) {
            path = "&disableConsigneeDojo=" + disable;
            Event.stopObserving("connam", "blur");
        }
    } else if (obj.id == "expnam") {
        if (!document.getElementById("shipperCheck").checked) {
            path = "&disableShipperDojo=" + disable;
            Event.stopObserving("expnam", "blur");
        }
    }
    appendEncodeUrl(path);
}
function setZeroDefault(val) {
    if (val.value === "") {
        val.value = "0";
    }
}
