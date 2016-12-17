var disableMessage = "This Customer is Disabled";
var importFlag1 = document.getElementById('importFlag1') && null !== document.getElementById('importFlag1').value && document.getElementById('importFlag1').value === 'I' ? true : false;

function openContactInfo(party) {
    var customerName = "";
    var customer = "";
    if (party == 'MC') {
        customerName = jQuery("#houseConsigneeName").val();
        customer = jQuery("#houseConsignee").val();
    } else if (party == 'MS') {
        customerName = jQuery("#houseName").val();
        customer = jQuery("#houseShipper").val();
    } else if (party == 'HC') {
        customerName = jQuery("#consigneeName").val();
        customer = jQuery("#consignee").val();
    } else if (party == 'HS') {
        customerName = jQuery("#accountName").val();
        customer = jQuery("#shipper").val();
    } else if (party == 'F') {
        customerName = jQuery("#forwardingAgentName").val();
        customer = jQuery("#forwardingAgent1").val();
    } else if (party == 'T') {
        customerName = jQuery("#billThirdPartyName").val();
        customer = jQuery("#billTrePty").val();
    } else if (party == 'CF') {
        customerName = jQuery("#correctedForwarderName").val();
        customer = jQuery("#correctedForwarderNo").val();
    } else if (party == 'CS') {
        customerName = jQuery("#correctedShipperName").val();
        customer = jQuery("#correctedShipperNo").val();
    }
    customerName = customerName.replace("&", "amp;");
    GB_show("Contact Info", "/logisoft/customerAddress.do?buttonValue=Quotation&custNo=" + customer + "&custName=" + customerName, width = "400", height = "1100");
}
function hideMasterShipper() {
    if (document.getElementById("houseName").value == "") {
        document.getElementById("ediShipperCheck").checked = false;
        document.getElementById("ediShipperCheck").style.visibility = 'hidden';
        document.getElementById("houseShipper").value = "";
        document.getElementById("houseShipper1").value = "";
    } else {
        document.getElementById("ediShipperCheck").style.visibility = 'visible';
    }
}
function hideMasterShipperblur() {
    if (document.getElementById('ediShipperCheck').checked && document.getElementById("houseName").value == "") {
        document.getElementById("houseName").value = "";
        Event.observe("houseName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseName_check").value) {
                element.value = '';
                $("houseName_check").value = '';
                $("houseName").value = '';
                $("houseShipper").value = '';
                $("houseShipper1").value = '';
            }
            hideMasterShipper();
        }
        );
        hideMasterShipper();
    }
}
function hideMasterConsignee() {
    if (document.getElementById("houseConsigneeName").value == "") {
        document.getElementById("ediConsigneeCheck").checked = false;
        document.getElementById("ediConsigneeCheck").style.visibility = 'hidden';
        document.getElementById("houseConsignee").value = "";
        document.getElementById("houseConsignee1").value = "";
    }
    else {
        document.getElementById("ediConsigneeCheck").style.visibility = 'visible';
    }
}
function hideMasterConsigneeblur() {
    if (document.getElementById("ediConsigneeCheck").checked && document.getElementById("houseConsigneeName").value == "") {
        document.getElementById("houseConsigneeName").value = "";
        Event.observe("houseConsigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseConsigneeName_check").value) {
                element.value = '';
                $("houseConsigneeName_check").value = '';
                $("houseConsigneeName").value = '';
                $("houseConsignee").value = '';
                $("houseConsignee1").value = '';
            }
            hideMasterConsignee();
        }
        );
        hideMasterConsignee();
    }
}
function hideMasterNotify() {
    if (document.getElementById("houseNotifyPartyName").value == "") {
        document.getElementById("ediNotifyPartyCheck").checked = false;
        document.getElementById("ediNotifyPartyCheck").style.visibility = 'hidden';
        document.getElementById("houseNotifyParty").value = "";
        document.getElementById("houseNotifyPartyaddress").value = "";
    } else {
        document.getElementById("ediNotifyPartyCheck").style.visibility = 'visible';
    }
}
function hideMasterNotifyblur() {
    if (document.getElementById("ediNotifyPartyCheck").checked && document.getElementById("houseNotifyPartyName").value == "") {
        document.getElementById("houseNotifyPartyName").value = "";
        Event.observe("houseNotifyPartyName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseNotifyPartyName_check").value) {
                element.value = '';
                $("houseNotifyPartyName_check").value = '';
                $("houseNotifyPartyName").value = '';
                $("houseNotifyParty").value = '';
                $("houseNotifyPartyaddress").value = '';
            }
            hideMasterNotify();
        }
        );
        hideMasterNotify();
    }
}
function hideHouseShipper() {
    if (document.getElementById("accountName").value == "") {
        document.getElementById("editHouseShipperCheck").checked = false;
        document.getElementById("editHouseShipperCheck").style.visibility = 'hidden';
        document.getElementById("shipper").value = "";
        document.getElementById("streamShip").value = "";
    } else {
        document.getElementById("editHouseShipperCheck").style.visibility = 'visible';
    }
}
function hideHouseShipperblur() {
    if (document.getElementById("editHouseShipperCheck").checked && document.getElementById("accountName").value == "") {
        document.getElementById("accountName").value = "";
        document.getElementById("shipper").value = "";
        document.getElementById("streamShip").value = "";
        Event.observe("accountName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("accountName_check").value) {
                element.value = '';
                $("accountName_check").value = '';
                $("shipper").value = '';
                $("accountName").value = '';
                $("streamShip").value = '';
            }
            hideHouseShipper();
        }
        );
        hideHouseShipper();
    }
}
function hideHouseConsignee() {
    if (document.getElementById("consigneeName").value == "") {
        document.getElementById("editHouseConsigneeCheck").checked = false;
        document.getElementById("editHouseConsigneeCheck").style.visibility = 'hidden';
        document.getElementById("consignee").value = "";
        document.getElementById("streamShipConsignee").value = "";
    } else {
        document.getElementById("editHouseConsigneeCheck").style.visibility = 'visible';
    }
}
function hideHouseConsigneeblur() {
    if (document.getElementById("editHouseConsigneeCheck").checked && document.getElementById("consigneeName").value == "") {
        document.getElementById("consigneeName").value = "";
        document.getElementById("consignee").value = "";
        document.getElementById("streamShipConsignee").value = "";
        Event.observe("consigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("consigneeName_check").value) {
                element.value = '';
                $("consigneeName_check").value = '';
                $("consignee").value = '';
                $("consigneeName").value = '';
                $("streamShipConsignee").value = '';
            }
            hideHouseConsignee();
        }
        );
        hideHouseConsignee();
    }
}
function hideHouseNotify() {
    if (document.getElementById("notifyPartyName").value == "") {
        document.getElementById("editHouseNotifyCheck").checked = false;
        document.getElementById("editHouseNotifyCheck").style.visibility = 'hidden';
        document.getElementById("notifyParty").value = "";
        document.getElementById("streamshipNotifyParty").value = "";
    } else {
        document.getElementById("editHouseNotifyCheck").style.visibility = 'visible';
    }
}
function hideHouseNotifyblur() {
    if (document.getElementById("editHouseNotifyCheck").checked && document.getElementById("notifyPartyName").value == "") {
        document.getElementById("notifyPartyName").value = "";
        document.getElementById("notifyParty").value = "";
        document.getElementById("streamshipNotifyParty").value = "";
        Event.observe("consigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("notifyPartyName_check").value) {
                element.value = '';
                $("notifyPartyName_check").value = '';
                $("notifyParty").value = '';
                $("notifyPartyName").value = '';
                $("streamshipNotifyParty").value = '';
            }
            hideHouseNotify();
        }
        );
        hideHouseNotify();
    }

}
function hideAgentCheck() {
    if (document.getElementById("forwardingAgentName").value == "") {
        document.getElementById("editAgentNameCheck").checked = false;
        document.getElementById("editAgentNameCheck").style.visibility = 'hidden';
        document.getElementById("forwardingAgent1").value = "";
        document.getElementById("forwardingAgentno").value = "";
    } else {
        document.getElementById("editAgentNameCheck").style.visibility = 'visible';
    }
}
function hideAgentCheckblur() {
    if (document.getElementById("editAgentNameCheck").checked && document.getElementById("forwardingAgentName").value == "") {
        document.getElementById("forwardingAgentName").value = "";
        document.getElementById("forwardingAgent1").value = "";
        document.getElementById("forwardingAgentno").value = "";
        Event.observe("forwardingAgentName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("forwardingAgentName_check").value) {
                element.value = '';
                $("forwardingAgentName_check").value = '';
                $("forwardingAgentName").value = '';
                $("forwardingAgent1").value = '';
                $("forwardingAgentno").value = '';
            }
            hideAgentCheck();
        }
        );
        hideAgentCheck();
    }

}
function getPartner(partner, buttonValue) {
    var cust = jQuery('#' + partner).val();
    var customer = cust.replace("&", "amp;");
    GB_show("Customer Search", "/logisoft/quoteCustomer.do?buttonValue=" + buttonValue + "&clientName=" + customer, width = "630", height = "900");
}
function getCustomer(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function(dataDup) {
            if (dataDup != "") {
                alertNew(dataDup);
            } else {
                fillPartnerDetails(val1, val2, val9, val4, val10);
            }
        }
    });
}
function openTradingPartner(ev) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "clearTradingPartnerSession",
            request: "true"
        },
        success: function() {
            window.parent.GB_showFullScreen("Trading Partner", "/logisoft/jsps/Tradingpartnermaintainance/SearchCustomer.jsp?callFrom=fclBillLadding&programid=156&field=" + ev);
        }
    });
}
function fillPartnerDetails(val1, val2, val3, val4, val5) {
    var array1 = new Array();
    array1 = val4.split(",");
    if (val5 == "Shipper") {
        if (array1.contains("S") || array1.contains("F") || array1.contains("E") || array1.contains("I")) {
            jQuery("#accountName").val(val1);
            jQuery("#shipper").val(val2);
            jQuery("#streamShip").val(val3);
        } else {
            alertNew("Select the customers with Account Type S,F,E or I");
            jQuery("#accountName").val("");
            jQuery("#shipper").val("");
            jQuery("#streamShip").val("");
        }
    } else if (val5 == "MasterShipper") {
        if (array1.contains("S") || array1.contains("F") || array1.contains("E") || array1.contains("I")) {
            jQuery("#houseName").val(val1);
            jQuery("#houseShipper").val(val2);
            jQuery("#houseShipper1").val(val3);
        } else {
            alertNew("Select the customers with Account Type S,F,E or I");
            jQuery("#houseName").val("");
            jQuery("#houseShipper").val("");
            jQuery("#houseShipper1").val("");
        }
    } else if (val5 == "Consignee") {
        if (array1.contains("C")) {
            jQuery("#consigneeName").val(val1);
            jQuery("#consignee").val(val2);
            jQuery("#streamShipConsignee").val(val3);
        } else {
            alertNew("Select the customers with Account Type C");
            jQuery("#consignee").val("");
            jQuery("#consigneeName").val("");
            jQuery("#streamShipConsignee").val("");
        }
    } else if (val5 == "MasterConsignee") {
        if (array1.contains("C")) {
            jQuery("#houseConsigneeName").val(val1);
            jQuery("#houseConsignee").val(val2);
            jQuery("#houseConsignee1").val(val3);
        } else {
            alertNew("Select the customers with Account Type C");
            jQuery("#houseConsigneeName").val("");
            jQuery("#houseConsignee").val("");
            jQuery("#houseConsignee1").val("");
        }
    } else if (val5 == "NotifyParty") {
        if (array1.contains("C")) {
            jQuery("#notifyPartyName").val(val1);
            jQuery("#notifyParty").val(val2);
            jQuery("#streamshipNotifyParty").val(val3);
        } else {
            alertNew("Select the customers with Account Type C");
            jQuery("#notifyPartyName").val("");
            jQuery("#notifyParty").val("");
            jQuery("#streamshipNotifyParty").val("");
        }
    } else if (val5 == "MasterNotifyParty") {
        if (array1.contains("C")) {
            jQuery("#houseNotifyPartyName").val(val1);
            jQuery("#houseNotifyParty").val(val2);
            jQuery("#houseNotifyPartyaddress").val(val3);
        } else {
            alertNew("Select the customers with Account Type C");
            jQuery("#houseNotifyPartyName").val("");
            jQuery("#houseNotifyParty").val("");
            jQuery("#houseNotifyPartyaddress").val("");
        }
    } else if (val5 == "Forwarder") {
        if (array1.contains("F")) {
            jQuery("#forwardingAgentName").val(val1);
            jQuery("#forwardingAgent1").val(val2);
            jQuery("#forwardingAgentno").val(val3);
        } else {
            alertNew("Select the customers with Account Type F");
            jQuery("#forwardingAgentName").val("");
            jQuery("#forwardingAgent1").val("");
            jQuery("#forwardingAgentno").val("");
        }
    }
}
function getThirdParty() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForDisable",
            param1: jQuery("#billTrePty").val()
        },
        success: function(dataDup) {
            if (dataDup != "") {
                alertNew(dataDup);
                jQuery("#billThirdPartyName").val("")
                jQuery("#billTrePty").val("");
                jQuery("#billThirdPartyAddress").val("");
            } else {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                        methodName: "getCustInfoForCustNo",
                        param1: jQuery("#billTrePty").val(),
                        dataType: "json"
                    },
                    success: function(data) {
                        if (null != data) {
                            fillThirdParty(data);
                        }
                    }
                });
            }
        }
    });
}
function fillThirdParty(data) {
    if ((data.acctType == 'V' && (null == data.subType || data.subType == '')) || (data.acctType == 'C' && (null == data.subType || data.subType == ''))
            || ((null != data.subType && (data.subType).toLowerCase()) != 'forwarder' && data.acctType == 'V')) {
        alertNew("Please select the Customers only with Account Type S,O and V with Sub Type Forwarder");
        jQuery("#billThirdPartyName").val("")
        jQuery("#billTrePty").val("");
        jQuery("#billThirdPartyAddress").val("");
    } else {
        var bol = jQuery("#bol").val();
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "IsFaeCommisionSetupOrAdvSurCharge",
                param1: bol,
                param2: "yes"
            },
            async: false,
            success: function(data) {
                if (data !== '') {
                    faeCommisionSetupOrAdvSurCharge = data.toString();
                }
            }
        });
        if (faeCommisionSetupOrAdvSurCharge == 'INCENTADDED') {
            confirmYesOrNo("--->Please note that all Bill to party will be changed for all Charges,<br>--->INCENT chargecode will be deleted <br> ---> - Yes to continue and Cancel to abort operation", "thirdparty");
        } else {
            confirmYesOrNo("Please note that all Bill to party will be changed for all Charges - Yes to continue and Cancel to abort operation", "thirdparty");
        }
    }
}
function getMasterNotifyData() {
    jQuery(document).ready(function() {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: jQuery("#houseNotifyParty").val()
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    jQuery("#houseNotifyPartyName").val("");
                    jQuery("#houseNotifyParty").val("");
                    jQuery("#houseNotifyPartyaddress").val("");
                    jQuery('#mNotifyIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: jQuery("#houseNotifyParty").val(),
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data && null != data.acctNo) {
                                fillMasterNotifyData(data);
                            }
                        }
                    });
                }
            }
        });
        document.getElementById("houseNotifyParty_check").value = document.getElementById("houseNotifyParty").value;
    });
    setFocusTo('accountName');
    hideMasterNotify();
}
function fillMasterNotifyData(data) {
    var type;
    var array1 = new Array();
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (array1.contains("C")) {
        if (data.acctNo != "") {
            jQuery("#houseNotifyParty").val(data.acctNo);
        } else {
            jQuery("#houseNotifyParty").val("");
        }
        jQuery("#houseNotifyPartyaddress").val(concatenateAddress(data));
        isCustomerNotes("mNotifyIcon", data.acctNo);
    } else {
        alertNew("Select the customers with Account Type C");
        jQuery("#houseNotifyPartyName").val(jQuery("#houseNotifyPartyName").val());
        jQuery("#houseNotifyParty").val("");
        jQuery("#houseNotifyPartyaddress").val("");
        jQuery('#mNotifyIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
    }
    hideMasterNotify();
}
function getMasterConsigneeData() {
    jQuery(document).ready(function() {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: jQuery("#houseConsignee").val()
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    jQuery("#houseConsigneeName").val("");
                    jQuery("#houseConsignee").val("");
                    jQuery("#houseConsignee1").val("");
                    jQuery('#consigneeIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: jQuery("#houseConsignee").val(),
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data && null != data.acctNo) {
                                fillMasterConsigneeData(data);
                            }
                        }
                    });
                }
            }
        });

        jQuery("houseConsignee_check").val(jQuery("#houseConsignee").val());
    });
    setFocusTo('houseNotifyPartyName');
    hideMasterConsignee();
}

function fillMasterConsigneeData(data) {
    if (data != null) {
        if (data.acctNo != "") {
            jQuery("#houseConsignee").val(data.acctNo);
            if (document.getElementById("houseNotifyPartyName").value == '') {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                        methodName: "getTradingPartner",
                        param1: data.acctNo,
                        dataType: "json"
                    },
                    success: function(data) {
                        if (null != data) {
                            if (null != data.notifyParty && data.notifyParty != '') {
                                jQuery("#houseNotifyPartyName").val(data.notifyParty);
                                jQuery("#houseNotifyPartyName_check").val(data.notifyParty);
                                jQuery("#masterNotifyCheck").attr("checked", true);
                                jQuery("#houseNotifyPartyaddress").val(concatenateNotifyAddress(data));
                            }
                        }
                    }
                });
            }
            isCustomerNotes("consigneeIcon", data.acctNo);
        } else {
            jQuery("#houseConsignee").val("");
        }
        jQuery("#houseConsignee1").val(concatenateAddress(data));
    } else {
        jQuery("#houseConsigneeName").val("");
        jQuery("#houseConsignee").val("");
        jQuery("#houseConsignee1").val("");
        jQuery('#consigneeIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
    }
    hideMasterConsignee();
}
function getMasterShipperData() {
    jQuery(document).ready(function() {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: jQuery("#houseShipper").val()
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    jQuery("#houseName").val("");
                    jQuery("#houseShipper").val("");
                    jQuery("#houseShipper1").val("");
                    jQuery('#shipperIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: jQuery("#houseShipper").val(),
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data && null != data.acctNo) {
                                populateMasterData(data);
                            }
                        }
                    });
                }
            }
        });

        jQuery("houseShipper_check").val(jQuery("#houseShipper").val());
    });
    setFocusTo('houseConsigneeName');
    hideMasterShipper();
}

function populateMasterData(data) {
    var type;
    var subTypes;
    var array1 = new Array();
    var array2 = new Array();
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (data.subType != null) {
        subTypes = (data.subType).toLowerCase();
        array2 = subTypes.split(",");
    }
    if (array1.contains("S") || array1.contains("F") || array1.contains("Z") || array1.contains("E") || array1.contains("I") || (array1.contains("V") && array2.contains("forwarder"))) {
        if (data.acctNo != "") {
            jQuery("#houseShipper").val(data.acctNo);
        } else {
            jQuery("#houseShipper").val("");
        }
        jQuery("#houseShipper1").val(concatenateAddress(data));
        isCustomerNotes('shipperIcon', data.acctNo);
    } else {
        alertNew("Select the Customers with Account Type S,E,I,Z and V with Sub Type Forwarder");
        jQuery("#houseName").val(jQuery("#houseName").val());
        jQuery("#houseShipper").val("");
        jQuery("#houseShipper1").val("");
        jQuery('#shipperIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
    }
    hideMasterShipper();
}
function getHouseNotifyPartyData() {
    jQuery(document).ready(function() {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: jQuery("#notifyParty").val()
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    jQuery("#notifyPartyName").val("");
                    jQuery("#notifyParty").val("");
                    jQuery("#streamshipNotifyParty").val("");
                    jQuery('#hNotifyIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: jQuery("#notifyParty").val(),
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data && null != data.acctNo) {
                                fillHouseNotifyData(data);
                            }
                        }
                    });
                }
            }
        });
        document.getElementById("notifyParty_check").value = document.getElementById("notifyParty").value;
    });
    setFocusTo('forwardingAgentName');
    hideHouseNotify();
}
function fillHouseNotifyData(data) {
    var type;
    var array1 = new Array();
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (array1.contains("C")) {
        if (data.acctNo != "") {
            jQuery("#notifyParty").val(data.acctNo);
        } else {
            jQuery("#notifyParty").val("");
        }
        jQuery("#streamshipNotifyParty").val(concatenateAddress(data));
        isCustomerNotes("hNotifyIcon", data.acctNo);
    } else {
        alertNew("Select the customers with Account Type C");
        jQuery("#notifyPartyName").val(jQuery("#notifyPartyName").val());
        jQuery("#notifyParty").val("");
        jQuery("#streamshipNotifyParty").val("");
        jQuery('#hNotifyIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
    }
    hideHouseNotify();
}
function getForwardingData() {
    jQuery(document).ready(function() {
        if (jQuery("#billToCodeF").attr("checked") && (jQuery("#forwardingAgentName").val() == 'NO FF ASSIGNED' ||
                jQuery("#forwardingAgentName").val() == 'NO FF ASSIGNED / B/L PROVIDED' ||
                jQuery("#forwardingAgentName").val() == 'NO FRT. FORWARDER ASSIGNED')) {
            alertNew("Please change Bill To Party or change Forwarder, because Forwarder is NO FF ASSIGNED");
            jQuery("#forwardingAgentName").val("");
            jQuery("#forwardingAgent1").val("");
            jQuery("#forwardingAgentno").val("");
            jQuery('#freightIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
            return;
        } else {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "checkForDisable",
                    param1: jQuery("#forwardingAgent1").val()
                },
                success: function(dataDup) {
                    if (dataDup != "") {
                        alertNew(dataDup);
                        jQuery("#forwardingAgentName").val("");
                        jQuery("#forwardingAgent1").val("");
                        jQuery("#forwardingAgentno").val("");
                        jQuery('#freightIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
                    } else {
                        jQuery.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                                methodName: "getCustInfoForCustNo",
                                param1: jQuery("#forwardingAgent1").val(),
                                dataType: "json"
                            },
                            success: function(data) {
                                if (data && null != data.acctNo) {
                                    fillForwarderData(data);
                                }
                            }
                        });
                    }
                    hideAgentCheck();
                }
            });
        }
        jQuery("#forwardingAgent1_check").val(jQuery("#forwardingAgent1").val());
    });
   
    var forwardAccnt = jQuery("#forwardingAgent1").val();
   if(importFlag1 === false) {
        addBrandvalueForForwardAccount(forwardAccnt);
    }
    hideAgentCheck();
}
function fillForwarderData(data) {
    var array1 = new Array();
    var type;
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (data.subType != null && (((data.subType).toLowerCase()) == 'forwarder')) {
        if (data.acctNo != "") {
            jQuery("#forwardingAgent1").val(data.acctNo);
        } else {
            jQuery("#forwardingAgent1").val("");
        }
        jQuery("#forwardingAgentno").val(concatenateAddress(data));
        isCustomerNotes("freightIcon", data.acctNo);
        var forwarderName = jQuery("#forwardingAgentName").val();
        var bol = jQuery("#bol").val();
        if ((forwarderName == 'NO FF ASSIGNED' || forwarderName == 'NO FF ASSIGNED / B/L PROVIDED'
                || forwarderName == 'NO FRT. FORWARDER ASSIGNED')) {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.dwr.DwrUtil",
                    methodName: "deleteOrAddFFCommission",
                    param1: bol,
                    param2: "delete",
                    param3: "",
                    param4: "",
                    request: "true"
                }
            });
            if (document.getElementById("contactNameButtonForFF")) {
                document.getElementById("contactNameButtonForFF").style.display = "none";
            }
        } else {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.dwr.DwrUtil",
                    methodName: "deleteOrAddFFCommission",
                    param1: bol,
                    param2: "add",
                    param3: forwarderName,
                    param4: data.acctNo,
                    request: "true"
                }
            });
        }
    } else {
        alertNew("Select the Customers with Vendor (Sub type Forwarder)");
        jQuery("#forwardingAgentName").val(jQuery("#forwardingAgentName").val());
        jQuery("#forwardingAgent1").val("");
        jQuery("#forwardingAgentno").val("");
        jQuery('#freightIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
    }
    hideAgentCheck();
}
function getHouseConsigneeData() {
    jQuery(document).ready(function() {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: jQuery("#consignee").val()
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    jQuery("#consignee").val("");
                    jQuery("#consigneeName").val("");
                    jQuery("#streamShipConsignee").val("");
                    jQuery('#hConsigneeIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: jQuery("#consignee").val(),
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data && null != data.acctNo) {
                                fillHouseConsigneeData(data);
                            }
                        }
                    });
                }
            }
        });
        document.getElementById("consigneeName_check").value = document.getElementById("consigneeName").value;
    });
    setFocusTo('notifyPartyName');
    hideHouseConsignee();
}

function fillHouseConsigneeData(data) {
    var type;
    var array1 = new Array();
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (array1.contains("C")) {
        if (data.acctNo != "") {
            jQuery("#consignee").val(data.acctNo);
            isCustomerNotes("hConsigneeIcon", data.acctNo);
            if (jQuery("#notifyPartyName").val() == '') {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                        methodName: "getTradingPartner",
                        param1: data.acctNo,
                        dataType: "json"
                    },
                    success: function(data) {
                        if (null != data) {
                            if (null != data.notifyParty && data.notifyParty != '') {
                                jQuery("#notifyPartyName").val(data.notifyParty);
                                jQuery("#notifyPartyName_check").val(data.notifyParty);
                                jQuery("#notifyCheck").attr("checked", true);
                                jQuery("#streamshipNotifyParty").val(concatenateNotifyAddress(data));
                            }
                        }
                    }
                });
            }
        } else {
            jQuery("#consignee").val("")
        }
        jQuery("#streamShipConsignee").val(concatenateAddress(data));
    } else {
        alertNew("Select the Customers with Account Type C");
        jQuery("#consignee").val("");
        jQuery("#consigneeName").val(jQuery("#consigneeName").val());
        jQuery("#streamShipConsignee").val("");
        jQuery('#hConsigneeIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
    }
    hideHouseConsignee();
}
function getHouseShipperData() {
    jQuery(document).ready(function() {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkForDisable",
                param1: jQuery("#shipper").val()
            },
            success: function(dataDup) {
                if (dataDup != "") {
                    alertNew(dataDup);
                    jQuery("#accountName").val("");
                    jQuery("#shipper").val("");
                    jQuery("#streamShip").val("");
                    jQuery('#hShipperIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
                } else {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                            methodName: "getCustInfoForCustNo",
                            param1: jQuery("#shipper").val(),
                            dataType: "json"
                        },
                        success: function(data) {
                            if (data && null != data.acctNo) {
                                fillHouseShipperData(data);
                            }
                        }
                    });
                }
            }
        });

        document.getElementById("accountName_check").value = document.getElementById("accountName").value;
    });
    
    var shipperAccnt = jQuery("#shipper").val();
   if (importFlag1 === false) {
        addBrandvalueForShippereAccount(shipperAccnt);
    }
    setFocusTo('consigneeName');
    hideHouseShipper();
}

function fillHouseShipperData(data) {
    var type;
    var subTypes;
    var array1 = new Array();
    var array2 = new Array();
    if (data.acctType != null) {
        type = data.acctType;
        array1 = type.split(",");
    }
    if (data.subType != null) {
        subTypes = (data.subType).toLowerCase();
        array2 = subTypes.split(",");
    }
    if (null != data.acctType && array1.length > 0 && (array1.contains("S") || array1.contains("F") || array1.contains("E") || array1.contains("I") || (array1.contains("V") && array2.contains("forwarder")))) {
        if (data.acctNo != "") {
            jQuery("#shipper").val(data.acctNo);
        } else {
            jQuery("#shipper").val("");
        }
        jQuery("#streamShip").val(concatenateHouseShipperAddress(data));
        isCustomerNotes("hShipperIcon", data.acctNo);
    } else {
        alertNew("Select the customers with Account Type S,E,I and V with Sub Type Forwarder");
        jQuery("#accountName").val(jQuery("#accountName").val());
        jQuery("#shipper").val("");
        jQuery("#streamShip").val("");
        jQuery('#hShipperIcon').attr("src", "/logisoft/img/icons/e_contents_view.gif");
    }
    hideHouseShipper();
}
function editAccountForEdi(obj) {
    if (obj.checked) {
        if (obj.id == "ediShipperCheck") {
            if (jQuery("#houseName").val() != "") {
                confirmNew("Do you want to edit Shipper for EDI", "clearShipperForEdi");
            } else {
                Event.stopObserving("houseName", "blur");
            }
            hideMasterShipper();
        } else if (obj.id == "ediConsigneeCheck") {
            if (jQuery("#houseConsigneeName").val() != "") {
                confirmNew("Do you want to edit Consignee for EDI", "clearConsigneeForEdi");
            } else {
                Event.stopObserving("houseConsigneeName", "blur");
            }
            hideMasterConsignee();
        } else if (obj.id == "ediNotifyPartyCheck") {
            if (jQuery("#houseNotifyPartyName").val() != "") {
                confirmNew("Do you want to edit NotifyParty for EDI", "clearNotifyForEdi");
            } else {
                Event.stopObserving("houseNotifyPartyName", "blur");
            }
            hideMasterNotify();
        }
    } else {
        if (obj.id == "ediShipperCheck") {
            clearShipperDataForEdi();
        } else if (obj.id == "ediConsigneeCheck") {
            clearConsigneeDataForEdi();
        } else if (obj.id == "ediNotifyPartyCheck") {
            clearNotifyDataForEdi();
        }
    }
}
function clearShipperDataForEdi() {
    if (document.getElementById('ediShipperCheck').checked) {
        Event.stopObserving("houseName", "blur");
    } else {
        document.getElementById("houseName").value = "";
        Event.observe("houseName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseName_check").value) {
                element.value = '';
                $("houseName_check").value = '';
                $("houseName").value = '';
                $("houseShipper").value = '';
                $("houseShipper1").value = '';
            }
            hideMasterShipper();
        }
        );
        hideMasterShipper();
    }
    hideMasterShipper();
}
function clearNotifyDataForEdi() {
    if (document.getElementById('ediNotifyPartyCheck').checked) {
        Event.stopObserving("houseNotifyPartyName", "blur");
    } else {
        document.getElementById("houseNotifyPartyName").value = "";
        Event.observe("houseNotifyPartyName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseNotifyPartyName_check").value) {
                element.value = '';
                $("houseNotifyPartyName_check").value = '';
                $("houseNotifyPartyName").value = '';
                $("houseNotifyParty").value = '';
                $("houseNotifyPartyaddress").value = '';
            }
            hideMasterNotify();
        }
        );
        hideMasterNotify();
    }
    hideMasterNotify();
}
function clearConsigneeDataForEdi() {
    if (document.getElementById('ediConsigneeCheck').checked) {
        Event.stopObserving("houseConsigneeName", "blur");
    } else {
        document.getElementById("houseConsigneeName").value = "";
        Event.observe("houseConsigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseConsigneeName_check").value) {
                element.value = '';
                $("houseConsigneeName_check").value = '';
                $("houseConsigneeName").value = '';
                $("houseConsignee").value = '';
                $("houseConsignee1").value = '';
            }
            hideMasterConsignee();
        }
        );
        hideMasterConsignee();
    }
    hideMasterConsignee();
}

function disableAutoCompleter(obj) {
    if (obj.checked) {
        if (obj.id == "notifyCheck") {
            if (document.getElementById("notifyPartyName").value != "") {
                confirmNew("Do you want to clear existing NotifyParty Details", "clearNotifyParty");
            } else {
                document.getElementById("notifyPartyName").value = document.getElementById("notifyCopy").value;
                Event.stopObserving("notifyPartyName", "blur");
            }
            hideHouseNotify();
        } else if (obj.id == "consigneeCheck") {
            if (document.getElementById("consigneeName").value != "") {
                confirmNew("Do you want to clear existing Consignee Details", "clearConsignee");
            } else {
                document.getElementById("consigneeName").value = document.getElementById("consigneeCopy").value;
                Event.stopObserving("consigneeName", "blur");
            }
            hideHouseConsignee();
        } else if (obj.id == "masterNotifyCheck") {
            if (document.getElementById("houseNotifyPartyName").value != "") {
                confirmNew("Do you want to clear existing NotifyParty Details", "clearMasterNotifyParty");
            } else {
                document.getElementById("houseNotifyPartyName").value = document.getElementById("masterNotifyCopy").value;
                Event.stopObserving("houseNotifyPartyName", "blur");
            }
            hideMasterNotify();
        } else if (obj.id == "masterConsigneeCheck") {
            if (document.getElementById("houseConsigneeName").value != "") {
                confirmNew("Do you want to clear existing Consignee Details", "clearMasterConsignee");
            } else {
                document.getElementById('houseConsignee1').value = "";
                document.getElementById("houseConsigneeName").value = document.getElementById("masterConsigneeCopy").value;
                Event.stopObserving("houseConsigneeName", "blur");
            }
            hideMasterConsignee();
        } else if (obj.id == "houseShipperCheck") {
            if (document.getElementById("accountName").value != "") {
                confirmNew("Do you want to clear existing Shipper Details", "clearHouseShipper");
            } else {
                document.getElementById('streamShip').value = "";
                Event.stopObserving("accountName", "blur");
            }
        }
    } else {
        if (obj.id == "notifyCheck") {
            if (document.getElementById("notifyPartyName").value != "") {
                confirmUncheckClearNotifyBox("Do you want to clear existing NotifyParty Details");

            } else {
                document.getElementById("notifyParty").disabled = false;
                Event.observe("notifyPartyName", "blur", function(event) {
                    var element = Event.element(event);
                    if (element.value != $("notifyPartyName_check").value) {
                        element.value = '';
                        $("notifyPartyName_check").value = '';
                        $("notifyPartyName").value = '';
                        $("notifyParty").value = '';
                        $("streamshipNotifyParty").value = '';
                    }
                }
                );

            }
        } else if (obj.id == "consigneeCheck") {
            if (document.getElementById("consigneeName").value != "") {
                confirmUncheckClearConsigneeBox("Do you want to clear existing Consignee Details");
            } else {
                document.getElementById("consignee").disabled = false;
                Event.observe("consigneeName", "blur", function(event) {
                    var element = Event.element(event);
                    if (element.value != $("consigneeName_check").value) {
                        element.value = '';
                        $("consigneeName_check").value = '';
                        $("consigneeName").value = '';
                        $("consignee").value = '';
                        $("streamShipConsignee").value = '';
                    }
                }
                );
            }
        } else if (obj.id == "masterNotifyCheck") {
            if (document.getElementById("houseNotifyPartyName").value != "") {
                confirmUncheckClearMasterNotifyBox("Do you want to clear existing NotifyParty Details");
            } else {
                document.getElementById("houseNotifyParty").disabled = false;
                Event.observe("houseNotifyPartyName", "blur", function(event) {
                    var element = Event.element(event);
                    if (element.value != $("houseNotifyPartyName_check").value) {
                        element.value = '';
                        $("houseNotifyPartyName_check").value = '';
                        $("houseNotifyPartyName").value = '';
                        $("houseNotifyParty").value = '';
                        $("houseNotifyPartyaddress").value = '';
                    }
                    hideMasterNotify();
                }
                );
                hideMasterNotify();
            }
            hideMasterNotify();
        } else if (obj.id == "masterConsigneeCheck") {
            if (document.getElementById("houseConsigneeName").value != "") {
                confirmUncheckClearMasterConsigneeBox("Do you want to clear existing Consignee Details");
            } else {
                document.getElementById("houseConsignee").disabled = false;
                Event.observe("houseConsigneeName", "blur", function(event) {
                    var element = Event.element(event);
                    if (element.value != $("houseConsigneeName_check").value) {
                        element.value = '';
                        $("houseConsigneeName_check").value = '';
                        $("houseConsigneeName").value = '';
                        $("houseConsignee").value = '';
                        $("houseConsignee1").value = '';
                    }
                    hideMasterConsignee();
                }
                );
                hideMasterConsignee();
            }
            hideMasterConsignee();
        } else if (obj.id == "houseShipperCheck") {
            if (document.getElementById("accountName").value != "") {
                confirmUnCheckClearHouseShipperBox("Do you want to clear existing Shipper Details");
            } else {
                document.getElementById("shipper").disabled = false;
                Event.observe("accountName", "blur", function(event) {
                    var element = Event.element(event);
                    if (element.value != $("accountName_check").value) {
                        element.value = '';
                        $("accountName_check").value = '';
                        $("accountName").value = '';
                        $("shipper").value = '';
                        $("streamShip").value = '';
                    }
                }
                );
            }
        }
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
function editHouseShipper(obj) {
    if (obj.checked) {
        Event.stopObserving("accountName", "blur");
    } else {
        document.getElementById("accountName").value = "";
        document.getElementById("shipper").value = "";
        document.getElementById("streamShip").value = "";
        Event.observe("accountName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("accountName_check").value) {
                element.value = '';
                $("accountName_check").value = '';
                $("shipper").value = '';
                $("accountName").value = '';
                $("streamShip").value = '';
            }
            hideHouseShipper();
        }
        );
        hideHouseShipper();
    }
    hideHouseShipper();
}
function editHouseConsignee(obj) {
    if (obj.checked) {
        Event.stopObserving("consigneeName", "blur");
    } else {
        document.getElementById("consigneeName").value = "";
        document.getElementById("consignee").value = "";
        document.getElementById("streamShipConsignee").value = "";
        Event.observe("consigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("consigneeName_check").value) {
                element.value = '';
                $("consigneeName_check").value = '';
                $("consignee").value = '';
                $("consigneeName").value = '';
                $("streamShipConsignee").value = '';
            }
            hideHouseConsignee();
        }
        );
        hideHouseConsignee();
    }
    hideHouseConsignee();
}
function editHouseNotifyParty(obj) {
    if (obj.checked) {
        Event.stopObserving("notifyPartyName", "blur");
    } else {
        document.getElementById("notifyPartyName").value = "";
        document.getElementById("notifyParty").value = "";
        document.getElementById("streamshipNotifyParty").value = "";
        Event.observe("consigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("notifyPartyName_check").value) {
                element.value = '';
                $("notifyPartyName_check").value = '';
                $("notifyParty").value = '';
                $("notifyPartyName").value = '';
                $("streamshipNotifyParty").value = '';
            }
            hideHouseNotify();
        }
        );
        hideHouseNotify();
    }
    hideHouseNotify();
}
function editAgentName(obj) {
    if (obj.checked) {
        Event.stopObserving("forwardingAgentName", "blur");
    } else {
        document.getElementById("forwardingAgentName").value = "";
        document.getElementById("forwardingAgent1").value = "";
        document.getElementById("forwardingAgentno").value = "";
        Event.observe("forwardingAgentName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("forwardingAgentName_check").value) {
                element.value = '';
                $("forwardingAgentName").value = '';
                $("forwardingAgentName_check").value = '';
                $("forwardagentNo").value = '';
                $("forwardingAgentno").value = '';
            }
            hideAgentCheck();
        }
        );
        hideAgentCheck();
    }
    hideAgentCheck();
}
function copyNotListedPartner(from, to) {
    document.getElementById(to).value = from.value;
}
function concatenateNotifyAddress(data) {
    var address = "";

    if (null != data.notifyPartyAddress && data.notifyPartyAddress != "") {
        address += wordwrap(data.notifyPartyAddress.replace(/[\r\n]+/g, ""), 35);
        address = address + " \n";
    }
    var address1 = "";
    if (null != data.notifyPartyCity && data.notifyPartyCity != "") {
        address1 += data.notifyPartyCity + ", ";
    }
    if (null != data.notifyPartyState && data.notifyPartyState != "") {
        address1 += data.notifyPartyState + " ";
    }
    if (null != data.notifyPartyPostalCode && data.notifyPartyPostalCode != "") {
        address1 += data.notifyPartyPostalCode + " ";
    }
    address += wordwrap(address1, 35) + "\n";
    if (null != data.notifyPartyCountry && data.notifyPartyCountry != "" && data.notifyPartyCountry != 'UNITED STATES') {
        address += wordwrap(data.notifyPartyCountry, 35) + "\n";
    }
    return address;
}
function disableDojos(obj) {
    var path = "";
    var disable = 'disable';
    if (obj.id == "consigneeName") {
        if (jQuery("#consigneeCheck").attr("checked") || jQuery("#editHouseConsigneeCheck").attr("checked")) {
            path = "&disableDojo=" + disable;
            Event.stopObserving("consigneeName", "blur");
        }
    } else if (obj.id == "notifyPartyName") {
        if (jQuery("#notifyCheck").attr("checked") || jQuery("#editHouseNotifyCheck").attr("checked")) {
            path = "&disableAutocomplete=" + disable;
            Event.stopObserving("notifyPartyName", "blur");
        }
    } else if (obj.id == "houseConsigneeName") {
        if (jQuery("#masterConsigneeCheck").attr("checked")) {
            path = "&disableMasterConsigneeDojo=" + disable;
            Event.stopObserving("houseConsigneeName", "blur");
        } else if (jQuery("#ediConsigneeCheck").attr("checked")) {
            path = "&disableMasterConsigneeDojo=" + disable;
            Event.stopObserving("houseConsigneeName", "blur");
        }
    } else if (obj.id == "houseNotifyPartyName") {
        if (jQuery("#masterNotifyCheck").attr("checked")) {
            path = "&disableMasterNotifyDojo=" + disable;
            Event.stopObserving("houseNotifyPartyName", "blur");
        } else if (jQuery("#ediNotifyPartyCheck").attr("checked")) {
            path = "&disableMasterNotifyDojo=" + disable;
            Event.stopObserving("houseNotifyPartyName", "blur");
        }
    } else if (obj.id == "accountName") {
        if (jQuery("#houseShipperCheck") && jQuery("#houseShipperCheck").attr("checked")) {
            path = "&disableHouseShipperDojo=" + disable;
            Event.stopObserving("accountName", "blur");
        } else if (jQuery("#editHouseShipperCheck") && jQuery("#editHouseShipperCheck").attr("checked")) {
            path = "&disableHouseShipperDojo=" + disable;
            Event.stopObserving("accountName", "blur");
        }
    } else if (obj.id == "houseName") {
        if (jQuery("#ediShipperCheck") && jQuery("#ediShipperCheck").attr("checked")) {
            path = "&disableHouseShipperDojo=" + disable;
            Event.stopObserving("houseName", "blur");
        }
    } else if (obj.id == "forwardingAgentName") {
        if (jQuery("#editAgentNameCheck") && jQuery("#editAgentNameCheck").attr("checked")) {
            path = "&disableForwarderDojo=" + disable;
            Event.stopObserving("forwardingAgentName", "blur");
        }
    }
    appendEncodeUrl(path);
}

function concatenateAddress(data) {
    var address = "";
    if (null != data.coName && data.coName != "") {
        address += data.coName + " \n";
    }
    if (null != data.address1 && data.address1 != "") {
        //		address += wordwrap(data.address1.replace(/[\r\n]+/g, ""),35);
        address += data.address1;
        address = address + " \n";
    }
    var address1 = "";
    if (null != data.city1 && data.city1 != "") {
        address1 += data.city1 + ", ";
    }
    if (null != data.state && data.state != "") {
        address1 += data.state + " ";
    }
    if (null != data.zip && data.zip != "") {
        address1 += data.zip + " ";
    }
    address += wordwrap(address1, 35) + "\n";
    if (null != data.cuntry && data.cuntry != "" && data.cuntry.codedesc != 'UNITED STATES') {
        address += wordwrap(data.cuntry.codedesc, 35) + "\n";
    }
    // change compnay name in FClBlConstant
    if (null != data.phone && data.phone != "") {
        address += "PHONE " + data.phone;// + ",\n";
    }
    return address;
}
function concatenateHouseShipperAddress(data) {
    var address = "";
    if (null != data.coName && data.coName != "") {
        address += data.coName + " \n";
    }
    if (null != data.address1 && data.address1 != "") {
        //		address += wordwrap(data.address1.replace(/[\r\n]+/g, ""),35);
        address += data.address1;
        address = address + " \n";
    }
    var address1 = "";
    if (null != data.city1 && data.city1 != "") {
        address1 += data.city1 + ", ";
    }
    if (null != data.state && data.state != "") {
        address1 += data.state + " ";
    }
    if (null != data.zip && data.zip != "") {
        address1 += data.zip + " ";
    }
    address += wordwrap(address1, 35) + "\n";
    if (null != data.cuntry && data.cuntry != "" && data.cuntry.codedesc != 'UNITED STATES') {
        address += wordwrap(data.cuntry.codedesc, 35) + "\n";
    }
    return address;
}
function wordwrap(str, width, brk, cut) {
    brk = brk || '\n';
    width = width || 35;
    cut = cut || false;
    if (!str) {
        return str;
    }
    var regex = '.{1,35}(\\s|$)|\\S+?(\\s|$)' + (cut ? '|.{' + width + '}|.+$' : '|\\S+?(\\s|$)');
    return str.match(RegExp(regex, 'g')).join(brk);
}

function setFocusTo(focusTo) {
    if (document.getElementById(focusTo)) {
        document.getElementById(focusTo).focus();
    }
}
function clearNotifyPartyData() {
    if (document.getElementById('notifyCheck').checked) {
        document.getElementById("notifyPartyName").value = "";
        document.getElementById("notifyParty").value = "";
        document.getElementById("streamshipNotifyParty").value = "";
        Event.stopObserving("notifyPartyName", "blur");
    } else {
        document.getElementById("notifyParty").disabled = false;
        document.getElementById("notifyPartyName").value = "";
        document.getElementById("notifyParty").value = "";
        document.getElementById("streamshipNotifyParty").value = "";
        document.getElementById("notifyCopy").value = "";
        Event.observe("notifyPartyName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("notifyPartyName_check").value) {
                element.value = '';
                $("notifyPartyName_check").value = '';
                $("notifyPartyName").value = '';
                $("notifyParty").value = '';
                $("streamshipNotifyParty").value = '';
            }
            hideHouseNotify();
        }
        );
        hideHouseNotify();
    }
    hideHouseNotify();
}
function clearConsigneeData() {
    if (document.getElementById('consigneeCheck').checked) {
        document.getElementById("consigneeName").value = "";
        document.getElementById("consignee").value = "";
        document.getElementById("streamShipConsignee").value = "";
        Event.stopObserving("consigneeName", "blur");
    } else {
        document.getElementById("consignee").disabled = false;
        document.getElementById("consigneeName").value = "";
        document.getElementById("consignee").value = "";
        document.getElementById("streamShipConsignee").value = "";
        document.getElementById("consigneeCopy").value = "";
        Event.observe("consigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("consigneeName_check").value) {
                element.value = '';
                $("consigneeName_check").value = '';
                $("consigneeName").value = '';
                $("consignee").value = '';
                $("streamShipConsignee").value = '';
            }
            hideHouseConsignee();
        }
        );
        hideHouseConsignee();
    }
    hideHouseConsignee();
}
function clearMasterNotifyData() {
    if (document.getElementById('masterNotifyCheck').checked) {
        document.getElementById("houseNotifyPartyName").value = "";
        document.getElementById("houseNotifyParty").value = "";
        document.getElementById("houseNotifyPartyaddress").value = "";
        Event.stopObserving("houseNotifyPartyName", "blur");
    } else {
        document.getElementById("houseNotifyParty").disabled = false;
        document.getElementById("houseNotifyPartyName").value = "";
        document.getElementById("houseNotifyParty").value = "";
        document.getElementById("houseNotifyPartyaddress").value = "";
        document.getElementById("masterNotifyCopy").value = "";
        Event.observe("houseNotifyPartyName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseNotifyPartyName_check").value) {
                element.value = '';
                $("houseNotifyPartyName_check").value = '';
                $("houseNotifyPartyName").value = '';
                $("houseNotifyParty").value = '';
                $("houseNotifyPartyaddress").value = '';
            }
            hideMasterNotify();
        }
        );
        hideMasterNotify();
    }
    hideMasterNotify();
}
function clearMasterConsigneeData() {
    if (document.getElementById('masterConsigneeCheck').checked) {
        document.getElementById("houseConsigneeName").value = "";
        document.getElementById("houseConsignee").value = "";
        document.getElementById("houseConsignee1").value = "";
        Event.stopObserving("houseConsigneeName", "blur");
    } else {
        document.getElementById("houseConsignee").disabled = false;
        document.getElementById("houseConsigneeName").value = "";
        document.getElementById("houseConsignee").value = "";
        document.getElementById("houseConsignee1").value = "";
        document.getElementById("masterConsigneeCopy").value = "";
        Event.observe("houseConsigneeName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("houseConsigneeName_check").value) {
                element.value = '';
                $("houseConsigneeName_check").value = '';
                $("houseConsigneeName").value = '';
                $("houseConsignee").value = '';
                $("houseConsignee1").value = '';
            }
            hideMasterConsignee();
        }
        );
        hideMasterConsignee();
    }
    hideMasterConsignee();
}
function clearHouseShipper() {
    if (document.getElementById('houseShipperCheck') && document.getElementById('houseShipperCheck').checked) {
        document.getElementById("accountName").value = "";
        document.getElementById("shipper").value = "";
        document.getElementById("streamShip").value = "";
        Event.stopObserving("accountName", "blur");
    } else {
        document.getElementById("shipper").disabled = false;
        document.getElementById("accountName").value = "";
        document.getElementById("shipper").value = "";
        document.getElementById("streamShip").value = "";
        Event.observe("accountName", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("accountName_check").value) {
                element.value = '';
                $("accountName_check").value = '';
                $("accountName").value = '';
                $("shipper").value = '';
                $("streamShip").value = '';
            }
        }
        );
    }
}
function concatSlashForDateValue(dateValue) {
    return (dateValue.substring(0, 2) + "/" + dateValue.substring(2, 4) + "/" + dateValue.substring(4));
}
function AlertMessage(data1) {
    if (data1.value != "") {
        if (data1.value.length < 11) {
            if (data1.value.length == 8 && !(isNaN(data1.value))) {
                data1.value = concatSlashForDateValue(data1.value);
            }
            data1.value = data1.value.getValidDateTime("/", "", false);
            data1.value = data1.value + " 12:00 PM";
        } else if (data1.value.toUpperCase().indexOf("PM") > -1 && data1.value.toUpperCase().indexOf(" PM") == -1) {
            data1.value = data1.value.toUpperCase().replace("PM", " PM");
        } else if (data1.value.toUpperCase().indexOf("AM") > -1 && data1.value.toUpperCase().indexOf(" AM") == -1) {
            data1.value = data1.value.toUpperCase().replace("AM", " AM");
        }
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "dateTimeValidation",
                param1: data1.value,
                param2: "MM/dd/yyyy HH:mm a",
                dataType: "json"
            },
            success: function(data) {
                if (null != data) {
                    if (data1.value != "") {
                        var date = new Date();
                        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear();
                        var previousTime = new Date();
                        previousTime.setDate(previousTime.getDate() - 60);
                        var previousDate = previousTime.getMonth() + 1 + "/" + previousTime.getDate() + "/" + previousTime.getFullYear();
                        var containerDate = document.getElementById("txtcal313").value;
                        var docDate = document.getElementById("txtcal71").value;
                        var etd = document.getElementById("txtetdCal").value;
                        var dateInYard = document.getElementById("dateInYard").value;
                        var earliestDate = document.getElementById("earlierPickUpDate").value;
                        if (containerDate != "" && containerDate != null) {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                    methodName: "dateValidation",
                                    param1: containerDate,
                                    param2: previousDate
                                },
                                success: function(data) {
                                    if (data == "lesser") {
                                        alertNew("Container Cut off Cannot be less than 60 days");
                                        document.getElementById("txtcal313").value = "";
                                        document.getElementById("txtcal313").focus();
                                        return;
                                    }
                                }
                            });
                        }
                        if (docDate != "" && docDate != null) {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                    methodName: "dateValidation",
                                    param1: docDate,
                                    param2: previousDate
                                },
                                success: function(data) {
                                    if (data == "lesser") {
                                        alertNew("Doc Cut off Cannot be less than 60 days");
                                        document.getElementById("txtcal71").value = "";
                                        document.getElementById("txtcal71").focus();
                                        return;
                                    }
                                }
                            });
                        }
                        if (etd != "" && etd != null) {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                    methodName: "dateValidation",
                                    param1: containerDate,
                                    param2: etd
                                },
                                success: function(data) {
                                    if (data == "greater") {
                                        alertNew("Container Cut off Cannot be greater than ETD");
                                        document.getElementById("txtcal313").value = "";
                                        document.getElementById("txtcal313").focus();
                                        return;
                                    }
                                }
                            });
                        }
                        if (etd != "" && etd != null) {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                    methodName: "dateValidation",
                                    param1: docDate,
                                    param2: etd
                                },
                                success: function(data) {
                                    if (data == "greater") {
                                        alertNew("Doc Cut off Cannot be greater than ETD");
                                        document.getElementById("txtcal71").value = "";
                                        document.getElementById("txtcal71").focus();
                                        return;
                                    }
                                }
                            });
                        }
                        if (dateInYard != "") {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                    methodName: "dateValidation",
                                    param1: containerDate,
                                    param2: dateInYard
                                },
                                success: function(data) {
                                    if (data == "lesser") {
                                        alertNew("Container Cut off Cannot be lesser than DateBackIntoYard");
                                        document.getElementById("txtcal313").value = "";
                                        document.getElementById("txtcal313").focus();
                                        return;
                                    }
                                }
                            });
                        }
                        if (earliestDate != "" && earliestDate != null) {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                    methodName: "dateValidation",
                                    param1: containerDate,
                                    param2: earliestDate
                                },
                                success: function(data) {
                                    if (data == "lesser") {
                                        alertNew("Container Cut off Cannot be lesser than Earliest Date");
                                        document.getElementById("txtcal313").value = "";
                                        document.getElementById("txtcal313").focus();
                                        return;
                                    }
                                }
                            });
                        }
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                methodName: "dateValidationforSixMonth",
                                param1: currentDate,
                                param2: containerDate
                            },
                            success: function(data) {
                                if (data == "greater") {
                                    alertNew("Container Cut off should not be greater than six month from Today's date");
                                    jQuery("#txtcal313").val('');
                                    setFocusTo("txtcal313");
                                    return;
                                }
                            }
                        });
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                                methodName: "dateValidationforSixMonth",
                                param1: currentDate,
                                param2: docDate
                            },
                            success: function(data) {
                                if (data == "greater") {
                                    alertNew("Doc Cut off should not be greater than six month from Today's date");
                                    jQuery("#txtcal71").val('');
                                    setFocusTo("txtcal71");
                                    return;
                                }
                            }
                        });
                        yearValidation(data1);
                    } else {
                        alertNew("Please enter valid date");
                        data1.value = "";
                        document.getElementById(data1.id).focus();
                    }
                } else {
                    alertNew("Please enter valid date");
                    data1.value = "";
                    document.getElementById(data1.id).focus();
                }
            }
        });
        if (data1.value != "") {
            if (data1.id == "txtcal313") {
                document.getElementById("txtcal71").focus();
            } else if (data1.id == "txtcal71") {
                document.getElementById("txtetdCal").focus();
            }
        }
    }
}
function validateEtd(data) {
    if (data.value != "") {
        data.value = data.value.getValidDateTime("/", "", false);
        if (data.value == "" || data.value.length > 10) {
            alertNew("Please enter valid date");
            data.value = "";
            document.getElementById(data.id).focus();
        }
    }
    if (data.value != "") {
        var date = new Date();
        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear();
        var previousTime = new Date();
        previousTime.setDate(previousTime.getDate() - 60);
        var previousDate = previousTime.getMonth() + 1 + "/" + previousTime.getDate() + "/" + previousTime.getFullYear();
        var etdDate = jQuery("#txtetdCal").val();
        var etaDate = jQuery("#txtetaCal").val();
        var originalDate = jQuery("#etdDate").val();
        var d = etdDate.split("/");
        var simpleFormat = d[0] + "/" + d[1] + "/" + d[2];
        var etd = new Date(simpleFormat);
        var containerDate = document.getElementById("txtcal313").value;
        var docDate = document.getElementById("txtcal71").value;
        var e = etaDate.split("/");
        var r = e[0] + "/" + e[1] + "/" + e[2];
        var eta = new Date(r);
        var now = new Date();
        now.setDate(now.getDate() - 60);
        if (etd < now) {
            alertNew("ETD Cannot be less than " + previousDate);
            jQuery("#txtetdCal").val('');
            setFocusTo("txtetdCal");
            return;
        } else if (etd >= eta) {
            alertNew("ETD Cannot be greater than or equal to ETA");
            jQuery("#txtetdCal").val('');
            setFocusTo("txtetdCal");
            return;
        } else {
            jQuery("#etdDate").val(jQuery("#txtetdCal").val());
        }
        if (containerDate != "" && simpleFormat != "") {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateValidation",
                    param1: containerDate,
                    param2: simpleFormat
                },
                success: function(data) {
                    if (data == "greater") {
                        alertNew("ETD should be greater than or equal to Container Cut Off");
                        jQuery("#txtetdCal").val('');
                        setFocusTo("txtetdCal");
                        return;
                    }
                }
            });
        }
        if (docDate != "" && simpleFormat != "") {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateValidation",
                    param1: docDate,
                    param2: simpleFormat
                },
                success: function(data) {
                    if (data == "greater") {
                        alertNew("ETD should be greater than or equal to Doc Cut Off");
                        jQuery("#txtetdCal").val('');
                        setFocusTo("txtetdCal");
                        return;
                    }
                }
            });
        }
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "dateValidationforSixMonth",
                param1: currentDate,
                param2: etdDate
            },
            success: function(data) {
                if (data == "greater") {
                    alertNew("ETD should not be greater than six month from Today's date");
                    jQuery("#txtetdCal").val('');
                    setFocusTo("txtetdCal");
                    return;
                }
            }
        });
        yearValidation(data);
    }
}
function validateETA(data) {
    if (data.value != "") {
        data.value = data.value.getValidDateTime("/", "", false);
        if (data.value == "" || data.value.length > 10) {
            alertNew("Please enter valid date");
            data.value = "";
            document.getElementById(data.id).focus();
        }
    }
    if (data.value != "") {
        var verifiedEta = jQuery("#txtcal4").val();
        var etd = jQuery("#etdValue").val();
        if (etd != "" && etd != null) {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateValidation",
                    param1: verifiedEta,
                    param2: etd
                },
                success: function(data) {
                    if (data == "lesser") {
                        alertNew("Verified ETA cannot be less than ETD");
                        jQuery("#txtcal4").val('');
                        return;
                    }
                }
            });
        }
        yearValidation(data);
    }
}
function validateBlETA(data) {
    if (data.value != "") {
        data.value = data.value.getValidDateTime("/", "", false);
        if (data.value == "" || data.value.length > 10) {
            alertNew("Please enter valid date");
            data.value = "";
            document.getElementById(data.id).focus();
        }
    }
    if (data.value != "") {
        var etdDate = jQuery("#txtetdCal").val();
        var etaDate = jQuery("#txtetaCal").val();
        var originalDate = jQuery("#etaDate").val();
        var d = etdDate.split("/");
        var s = d[0] + "/" + d[1] + "/" + d[2];
        var etd = new Date(s);
        var e = etaDate.split("/");
        var r = e[0] + "/" + e[1] + "/" + e[2];
        var eta = new Date(r);
        var date = new Date();
        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear();
        if (etaDate != "" && etaDate != null) {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateValidation",
                    param1: etaDate,
                    param2: currentDate
                },
                success: function(data) {
                    if (data == "lesser") {
                        alertNew("ETA Cannot be less than today date");
                        jQuery("#txtetaCal").val('');
                        setFocusTo("txtetaCal");
                        return;
                    }
                }
            });
        }
        if (eta <= etd) {
            alertNew("ETA  Cannot be less than or equal to ETD");
            jQuery("#txtetaCal").val('');
            setFocusTo("txtetaCal");
            return;
        } else {
            jQuery("#etaDate").val(jQuery("#txtetaCal").val());
            if (null != etaDate && etaDate != '' && etdDate != '') {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                        methodName: "getTransitDays",
                        param1: etdDate,
                        param2: etaDate
                    },
                    success: function(data) {
                        if (null != data) {
                            jQuery("#noOfDays").val(data);
                        }
                    }
                });
            }
        }
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "dateValidationforSixMonth",
                param1: etdDate,
                param2: etaDate
            },
            success: function(data) {
                if (data == "greater") {
                    alertNew("ETA date can never be more than six months from the ETD");
                    jQuery("#txtetaCal").val('');
                    setFocusTo("txtetaCal");
                    return;
                }
            }
        });
        yearValidation(data);
    }
}
function validateDateInYard(data) {
    if (data.value != "") {
        data.value = data.value.getValidDateTime("/", "", false);
        if (data.value == "" || data.value.length > 10) {
            data.value = "";
            alertNew("Please enter valid date");
            document.getElementById(data.id).focus();
        }
    }
    if (data.value != "") {
        var date = new Date();
        var currentDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getYear();
        var dateOutOfYard = jQuery("#txtcaldateOutYard").val();
        var dateIntoYard = jQuery("#txtcaldateIntoYard").val();

        if (dateIntoYard != "" && dateIntoYard != null) {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateValidation",
                    param1: dateIntoYard,
                    param2: currentDate
                },
                success: function(data) {
                    if (data == "lesser") {
                        alertNew("Date Back into Yard Cannot be less than today");
                        jQuery("#txtcaldateIntoYard").val("");
                        setFocusTo("txtcaldateIntoYard");
                        return;
                    }
                }
            });
        }

        if (dateOutOfYard != "" && dateOutOfYard != null) {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateValidation",
                    param1: dateIntoYard,
                    param2: dateOutOfYard
                },
                success: function(data) {
                    if (data == "lesser" || data == "equal") {
                        alertNew("Date Back into Yard Cannot be less than or equal to Date out of Yard");
                        jQuery("#txtcaldateIntoYard").val("");
                        setFocusTo("txtcaldateIntoYard");
                        return;
                    }
                }
            });
        }
        yearValidation(data);
    }
}
function validateDateOutYard(data) {
    if (data.value != "") {
        data.value = data.value.getValidDateTime("/", "", false);
        if (data.value == "" || data.value.length > 10) {
            alertNew("Please enter valid date");
            data.value = "";
            document.getElementById(data.id).focus();
        }
    }
    if (data.value != "") {
        var dateOutOfYard = jQuery("#txtcaldateOutYard").val();
        var dateIntoYard = jQuery("#txtcaldateIntoYard").val();
        if (dateIntoYard != "" && dateIntoYard != null) {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                    methodName: "dateValidation",
                    param1: dateOutOfYard,
                    param2: dateIntoYard
                },
                success: function(data) {
                    if (data == "greater" || data == "equal") {
                        alertNew("Date Out Of Yard  Cannot be greater than or equal to Date Back into Yard");
                        jQuery("#txtcaldateOutYard").val("");
                        setFocusTo("txtcaldateOutYard");
                        return;
                    }
                }
            });
        }
        yearValidation(data);
    }
}
function setSslBookingNo() {
    if (jQuery("#newMasterBLCheckBox").attr("checked")) {
        jQuery("#newMasterBL").val(jQuery("#booking").val());
    } else {
        jQuery("#newMasterBL").val("");
    }
}
function setAgentDojoAction() {
    var path = "";
    var portOfDischarge = jQuery("#portofdischarge").val();
    var destination = jQuery("#finalDestination").val();
    path = "&portOfDischarge=" + portOfDischarge + "&destination=" + destination;
    appendEncodeUrl(path);
}
function checkDisableAgent() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForDisable",
            param1: jQuery("#agentNo").val()
        },
        success: function(dataDup) {
            if (dataDup != "") {
                alertNew(dataDup);
                jQuery("#agentNo").val("");
                jQuery("#agent").val("");
            }
        }
    });
    setFocusTo('routedByAgent');
}
function checkDisabledRouted() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForDisable",
            param1: jQuery("#RoutedDup").val()
        },
        success: function(data) {
            if (data != "") {
                alertNew(data);
                jQuery("#routedByAgent").val("");
                jQuery("#RoutedDup").val("");
            } else {
                jQuery("#routedByAgent").val(jQuery("#RoutedDup").val());
            }
        }
    });
}
function autoFillDefaultAgent() {
    jQuery("#directConsignmentN").attr("checked",true);
    jQuery("#directConsignmentY").attr("checked",false);
    jQuery("#agent").attr("disabled", false);
    jQuery("#routedAgentCheck").attr("disabled", false);
    document.getElementById("routedAgentCheck").className = "dropdown_accounting"
    jQuery("#routedByAgent").attr("readonly", false);
    jQuery("#faeAdd").show();
    var destination = jQuery("#finalDestination").val();
    if (destination.indexOf("(") > -1 && destination.indexOf(")") > -1) {
        var destiNew = destination.substring(destination.indexOf("(") + 1, destination.indexOf(")"));
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                methodName: "getDefaultAgent",
                param1: destiNew,
                param2: "F",
                dataType: "json"
            },
            success: function(data) {
                if (data.accountno != undefined && data.accountno != "" && data.accountno != null) {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                            methodName: "checkForDisable",
                            param1: data.accountno
                        },
                        success: function(dataDup) {
                            if (dataDup != "") {
                                alertNew(dataDup);
                                jQuery("#agentNo").val("");
                                jQuery("#agent").val("");
                            } else {
                                if (data.accountName != undefined && data.accountName != "" && data.accountName != null) {
                                    jQuery("#agent").val(data.accountName);
                                    jQuery("#houseConsigneeName").val(data.accountName);
                                } else {
                                    jQuery("#agent").val("");
                                }
                                if (data.accountno != undefined && data.accountno != "" && data.accountno != null) {
                                    jQuery("#agentNo").val(data.accountno);
                                    jQuery("#houseConsignee").val(data.accountno);
                                } else {
                                    jQuery("#agentNo").val("");
                                }
                                if (data.address1 != undefined && data.address1 != "" && data.address1 != null) {
                                    jQuery("#houseConsignee1").val(data.address1);
                                }

                            }
                        }
                    });
                    document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
                    document.getElementById("agent").readOnly = true;
                    document.getElementById("agentNo").className = "textlabelsBoldForTextBoxDisabledLook";
                    document.getElementById("agentNo").readOnly = true;
                    document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
                    jQuery("#routedByAgent").attr("readonly", false);
                    if (jQuery("#routedAgentCheck").val() == "yes" || jQuery("#routedAgentCheck").val() == "no") {
                        document.getElementById("routedByAgent").value = "";
                        jQuery("#routedAgentCheck").val("");
                    }
                }

            }
        });
    }
}
function directConsign() {
        document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
        document.getElementById("routedAgentCheck").className = "dropdown_accountingDisabled";
        document.getElementById("routedByAgent").className = "textlabelsBoldForTextBoxDisabledLook";
        jQuery("#defaultAgentN").attr("checked", true);
        jQuery("#routedAgentCheck").val("");
        jQuery("#routedByAgent").val("");
        jQuery("#agent").val("");
        jQuery("#agentNo").val("");
        jQuery("#agent").attr("disabled", true);
        jQuery("#routedAgentCheck").attr("disabled", true);
        jQuery("#routedByAgent").attr("readonly", true);
        jQuery("#faeAdd").hide();
    }
   function directConsignNo() {
        document.getElementById("agent").className = "textlabelsBoldForTextBox";
        document.getElementById("routedAgentCheck").className = "dropdown_accounting";
        document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
        jQuery("#defaultAgentY").attr("checked", true);
        jQuery("#directConsignmentN").attr("checked", true);
        autoFillDefaultAgent();
    }
function setDefaultRouteAgent() {
    var agentNo = jQuery("#agentNo").val();
    if (jQuery("#routedAgentCheck").val() == "yes") {
        if (null != agentNo && agentNo != '') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                    methodName: "getUnlocDetail",
                    param1: agentNo
                },
                success: function(data) {
                    if (data == "false") {
                        jQuery("#routedAgentCheck").val("");
                        jQuery("#routedByAgent").val("");
                        alertNew("Please Enter UnLocation Code for selected Routed Agent");
                    } else {
                        jQuery("#routedByAgent").val(agentNo);
                        jQuery("#routedByAgent_action").val(agentNo);
                        document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
                    }
                }
            });
        } else {
            jQuery("#routedAgentCheck").val("");
            alertNew("You must first select an agent");
        }
    } else {
        jQuery("#routedByAgent").val("");
        document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
        jQuery("#routedByAgent").attr("readOnly", false);
        document.getElementById("routedByAgent").tabIndex = -1;
    }
}
function clearAgentValues() {
    document.getElementById("agent").value = "";
    document.getElementById("agentNo").value = "";
    document.getElementById("routedByAgent").value = "";
    document.getElementById("agent").readOnly = false;
    document.getElementById("agent").tabIndex = 0;
    document.getElementById("agent").className = "textlabelsBoldForTextBox";
    document.getElementById("routedByAgent").tabIndex = 0;
    document.getElementById("faeAdd").hide();
    if (jQuery("#routedAgentCheck").val() == "yes" || jQuery("#routedAgentCheck").val() == "no") {
        document.getElementById("routedByAgent").value = "";
        document.getElementById("routedByAgent").className = "textlabelsBoldForTextBox";
        jQuery("#routedAgentCheck").val("");
        jQuery("#routedByAgent").attr("readonly", false);
    }
    //document.fclBillLaddingform.routedAgentCheck.checked = false;
    var code = "";
    var dest = jQuery("#finalDestination").val();
    if (dest.lastIndexOf("(") != -1) {
        code = dest.substring(dest.lastIndexOf("(") + 1, dest.lastIndexOf(")"));
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "getDestCodeforHBL",
            param1: code
        },
        async: false,
        success: function(data) {
            if (data != null && data != '') {
                if (data == 'Y') {//pripaid
                    if (jQuery("#houseBlC").attr("checked")) {
                        jQuery("#houseBlP").attr("checked", true);
                        jQuery("#houseBlC").attr("checked", false);
                        jQuery("#billToCodeF").attr("checked", true);
                        jQuery("#billToCodeF").attr("disabled", false);
                        jQuery("#billToCodeS").attr("disabled", false);
                        jQuery("#billToCodeT").attr("disabled", false);
                        jQuery("#billToCodeT").attr("checked", false);
                        jQuery("#billToCodeA").attr("disabled", true);
                    }
                }
            }
        }
    });
}
function fillDomesticRouting() {
    var domesticRoute = "";
    var issuingTerm = "";
    var codeNo = "";
    var code = "";
    var codeVal = "";
    var temp;
    var temp1;
    if (jQuery("#billingTerminal").val() != "") {
        issuingTerm = jQuery("#billingTerminal").val();
        domesticRoute += "Issued By ";
        domesticRoute += "(";
        temp = issuingTerm.split(",");
        if (temp != null && temp.length == 2) {
            for (var i = 0; i < temp.length; i++) {
                if (i == 0) {
                    codeVal = temp[i];
                } else {
                    var codeandNo = temp[i];
                    temp1 = codeandNo.split("-");
                    for (var j = 0; j < temp1.length; j++) {
                        if (j == 0) {
                            code = temp1[j];
                        } else {
                            codeNo = temp1[j];
                        }
                    }
                }
            }
        }
        domesticRoute += codeNo;
        domesticRoute += ") ";
        domesticRoute += codeVal;
        domesticRoute += " -";
        domesticRoute += code;
    }
    jQuery("#domesticRouting").val(domesticRoute);
}
function isCustomerNotes(id, acctNo) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkCustomerNotes",
            param1: acctNo,
            dataType: "json"
        },
        success: function(data) {
            if (data) {
                jQuery('#' + id).attr("src", "/logisoft/img/icons/e_contents_view1.gif");
            } else {
                jQuery('#' + id).attr("src", "/logisoft/img/icons/e_contents_view.gif");
            }
        }
    });
}
function openBlueScreenNotesInfo(path, customerNo, customerName) {
    GB_show("Notes", path + "/bluescreenCustomerNotes.do?methodName=displayNotes&customerNo=" + customerNo + "&customerName=" + customerName, 400, 1000);
}
function setbrandvalueBasedONDestination(companyCode) {
   
    if (undefined !== document.getElementById('portofdischarge').value && null !== document.getElementById('portofdischarge').value && '' !== document.getElementById('portofdischarge').value) {
        
        var pod = document.getElementById('portofdischarge').value;
        if (pod.lastIndexOf("(") > -1 && pod.lastIndexOf(")") > -1) {
        var podNew = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
        
            jQuery.ajaxx({
                data: {
                    className: "com.logiware.dwr.FclDwr",
                    methodName: "checkBrandForDestination",
                    param1: podNew
                },
                success: function (data) {
                    
                    if (data === "Ecu Worldwide") {
                        document.getElementById('brandEcuworldwide').checked = true;
                        document.getElementById('brandEcono').checked = false;
                    } else if (data === "OTI") {
                        document.getElementById('brandOti').checked = true;
                        document.getElementById('brandEcuworldwide').checked = false;
                    }
                    else if (data === "Econocaribe") {
                        document.getElementById('brandEcono').checked = true;
                        document.getElementById('brandEcuworldwide').checked = false;
                    }
                }
            });

        
        }
    }else{
        
        if (companyCode === '03') {
            document.getElementById('brandEcono').checked = false;
            document.getElementById('brandEcuworldwide').checked = true;
        } else if(companyCode === '02'){
            document.getElementById('brandOti').checked = true;
            document.getElementById('brandEcuworldwide').checked = false;
        }
    }
    }

function validateBrandFieldsForShipper(data) {
    
    if (data === "Ecu Worldwide") {
        document.getElementById('brandEcuworldwide').checked = true;
        document.getElementById('brandEcono').checked = false;
    } else if (data === "Econo") {
        document.getElementById('brandEcono').checked = true;
        document.getElementById('brandEcuworldwide').checked = false;
    } else if (data === "OTI") {
        document.getElementById('brandOti').checked = true;
        document.getElementById('brandEcuworldwide').checked = false;
    } else if (data === "None") {
        var companyCode = document.getElementById('companyCode').value;
        setbrandvalueBasedONDestination(companyCode);
    }
}
function addBrandvalueForShippereAccount(shipperAccnt) {
  
   if (undefined !== shipperAccnt && null !== shipperAccnt && '' !== shipperAccnt) {
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "checkBrandForClient",
                param1: shipperAccnt
            },
            success: function (data) {
             validateBrandFieldsForShipper(data);
            }
        });

    }
}

function showBrandValuesFromBooking(brand){

    if (brand === "Econo"){
        document.getElementById('brandEcono').checked = true;
        document.getElementById('brandEcuworldwide').checked = false;
    } else if(brand === "Ecu Worldwide"){
        document.getElementById('brandEcuworldwide').checked = true;
        document.getElementById('brandEcono').checked == false
    }  else if (brand === "OTI") {
        document.getElementById('brandOti').checked = true;
        document.getElementById('brandEcuworldwide').checked == false
    }
}

function addBrandvalueForForwardAccount(forwardAccnt){
  
    if (undefined !== forwardAccnt && null !== forwardAccnt && '' !== forwardAccnt) {
        jQuery.ajaxx({
            data: {
                className: "com.logiware.dwr.FclDwr",
                methodName: "checkBrandForClient",
                param1: forwardAccnt
            },
            success: function (data) {
             validateBrandFieldsForwardAcct(data);
            }
        });

    }
    
}
function validateBrandFieldsForwardAcct(data) {
    
    if (data === "Ecu Worldwide") {
        document.getElementById('brandEcuworldwide').checked = true;
        document.getElementById('brandEcono').checked = false;
    } else if (data === "Econo") {
        document.getElementById('brandEcono').checked = true;
        document.getElementById('brandEcuworldwide').checked = false;
    } else if (data === "None") {
        var accountNo = jQuery("#shipper").val();
        if (accountNo !== undefined && accountNo !== null && accountNo !== '') {
            addBrandvalueForShippereAccount(accountNo);
        } else {
            var companyCode = document.getElementById('companyCode').value;
            setbrandvalueBasedONDestination(companyCode);
        }
    }
}

