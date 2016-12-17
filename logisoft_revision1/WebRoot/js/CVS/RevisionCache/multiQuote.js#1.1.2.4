
/* 
 * Document   : multiQuote
 * Created on : Feb 1, 2016, 11:08:00 AM
 * Author     : NambuRajasekar
 */
var path = "/" + window.location.pathname.split("/")[1];

var junk = false;
var myArray = new Array();
var myArray1 = new Array();
var result = false;
var importFlag = document.getElementById('fileType') && null !== document.getElementById('fileType').value && document.getElementById('fileType').value === 'I' ? true : false;
function loadData() {
    if (junk == false) {
        for (var i = 0; i < document.multiQuotesForm.elements.length; i++) {
            if (document.multiQuotesForm.elements[i].type == 'radio' ||
                    document.multiQuotesForm.elements[i].type == 'checkbox') {
                if (document.multiQuotesForm.elements[i].checked) {
                    myArray[i] = document.multiQuotesForm.elements[i].value;
                }
            } else {
                myArray[i] = document.multiQuotesForm.elements[i].value;
            }
        }
        junk = true;
    }
}

function checkClientConsignee() {
    var path = "";
    if (document.getElementById("clientConsigneeCheck")) {
        if (document.getElementById("clientConsigneeCheck").checked) {
            path = "&consigneeCheck=true";
        } else {
            path = "&consigneeCheck=false";
        }
    }
    appendEncodeUrl(path);
}

function clearNewClient() {
    if (document.getElementById("clientConsigneeCheck").checked) {
        document.getElementById('newClient').checked = false;
        document.getElementById('clientNumber').value = "";
        document.getElementById('clienttype').value = "";
        document.getElementById('contactName').value = "";
        document.getElementById('email').value = "";
        document.getElementById('phone').value = "";
        document.getElementById('fax').value = "";
        document.getElementById("contactButton").style.visibility = 'hidden';
        document.getElementById('newerClient').style.display = "none";
        document.getElementById('existingClient').style.display = "block";
    } else {
        document.getElementById('customerName').value = "";
    }
}

function getCreditStatus() {
    var accountNumber = document.getElementById("clientNumber").value;
    if (trim(accountNumber) != '') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getCreditStatus",
                param1: accountNumber
            },
            success: function (data) {
                if (data === "Suspended/See Accounting") {
                    alertNew("SUSPENDED CREDIT");
                    document.getElementById("creditStatusCol").style.display = "block";
                    document.getElementById("creditStatus").innerHTML = "SUSPENDED CREDIT";
                } else {
                    document.getElementById("creditStatusCol").style.display = "none";
                    document.getElementById("creditStatus").innerHTML = "";
                }
            }
        });
    } else {
        document.getElementById("creditStatusCol").style.display = "none";
        document.getElementById("creditStatus").innerHTML = "";
    }
}

function alertNewForDefaultValues(text) {
    DisplayAlerts("AlertBoxDefaultValues", 200, 300, text, window.center({
        width: 100,
        height: 100
    }));
}
function DisplayAlerts(id, left, top, text, point) {
    document.getElementById(id).style.left = left + "px";
    document.getElementById(id).style.top = top + "px";
    jQuery("#AlertBoxDefaultValues").find("#innerText").html(text);
    document.getElementById(id).style.display = "block";
    document.getElementById(id).style.left = point.x - 100 + "px";
    document.getElementById(id).style.top = point.y + "px";
    document.getElementById(id).style.zIndex = "1000";
    grayOut(true, "");
}
function getEmptyClient() {
    if (document.getElementById('customerName1').value == "") {
        document.multiQuotesForm.clientNumber.value = "";
        document.multiQuotesForm.clienttype.value = "";
        document.multiQuotesForm.contactName.value = "";
        document.multiQuotesForm.phone.value = "";
        document.multiQuotesForm.fax.value = "";
        document.multiQuotesForm.email.value = "";
        document.getElementById("contactButton").style.visibility = 'hidden';
    }
}
function newClientEQ() {
    if (document.getElementById('newClient').checked) {
        if (document.getElementById("clientConsigneeCheck")) {
            document.getElementById("clientConsigneeCheck").checked = false;
        }
        document.getElementById('newerClient').style.display = "block";
        document.getElementById('existingClient').style.display = "none";
        onCustomerBlur("newClient");
    } else {
        document.getElementById('newerClient').style.display = "none";
        document.getElementById('existingClient').style.display = "block";
        document.getElementById('customerName1').value = "";
    }
}
jQuery(document).ready(function () {
    window.parent.closePreloading();
});


function load() {
    loadData();
    if (document.getElementById('newClient').checked) {
        document.getElementById('newerClient').style.display = "block";
        document.getElementById('existingClient').style.display = "none";
    } else {
        document.getElementById('newerClient').style.display = "none";
        document.getElementById('existingClient').style.display = "block";
    }
}
function setDojoAction() {
    var path = "";
    if (document.getElementById('destinationCity').checked) {
        path = "&origin="
                + document.multiQuotesForm.isTerminal.value + "&radio=city";

    } else {
        path = "&origin="
                + document.multiQuotesForm.isTerminal.value + "&radio=country";

        ;
    }
    appendEncodeUrl(path);
}
function getAgentforDestinationonBlur() {
    var pod = document.multiQuotesForm.portofDischarge.value;
    if (pod.lastIndexOf("(") > -1 && pod.lastIndexOf(")") > -1) {
        var podNew = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                methodName: "getDefaultAgent",
                param1: podNew,
                param2: "true" === importFlag ? "I" : "F"
            },
            success: function (data) {
                populateAgentDojo1(data);
            }
        });
    }
}
function getOriginUrl() {
    var path = "";
    if (document.getElementById('originCountry').checked) {
        path = "&destination="
                + document.multiQuotesForm.portofDischarge.value + "&radio=city";
    } else {
        path = "&destination="
                + document.multiQuotesForm.portofDischarge.value + "&radio=country";
    }
    appendEncodeUrl(path);
}

function trim(stringToTrim) {
    return stringToTrim.replace(/^\s+|\s+$/g, "");
}
function getContactNamefromPopup(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11) {
    val1 = val1.replace(":", "'");
    document.multiQuotesForm.customerName.value = val1;
    document.multiQuotesForm.clientNumber.value = val2;
    document.multiQuotesForm.contactName.value = val4;
    if (val5 != "") {
        document.multiQuotesForm.phone.value = val5;
    }
    if (val6 != "") {
        document.multiQuotesForm.fax.value = val6;
    }
    if (val7 != "") {
        document.multiQuotesForm.email.value = val7;
    }
    setTimeout("setFocus1()", 150);
}
function setFocus1() {
    document.getElementById('move').focus();
}
function setFC() {
    document.getElementById('commcode').focus();
}
function getCarrier(val1, val2, val3, val4, val5, val6, val7, val8) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function (data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
            }
            else {
                setTimeout("setF()", 150);
            }
        }
    });
}
function setF() {
    document.getElementById('carrierContact').focus();
}

function refreshPage(val1) {
    checkbox();
    document.multiQuotesForm.quoteId.value = val1;
    document.multiQuotesForm.buttonValue.value = "addCharges";
    document.multiQuotesForm.submit();
}
function setDojoPathForAgent() {
    var path = "";
    var portOfDischarge = document.multiQuotesForm.finalDestination.value;
    var destination = document.multiQuotesForm.portofDischarge.value;
    path = "&portOfDischarge=" + portOfDischarge + "&destination=" + destination;
    appendEncodeUrl(path);
}
function getAgentInfo(val1, val2) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function (data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
            } else {
                document.multiQuotesForm.agent.value = val1;
                document.multiQuotesForm.agentNo.value = val2;
                document.getElementById('routedbymsg').focus();
            }
        }
    });
}

function makeClientTypeBorderLess() {
    var element = document.getElementById("clienttype");
    element.style.border = 0;
    element.readOnly = true;
    element.className = "whitebackgrnd";
}
function getEmptySsline() {
    if (document.multiQuotesForm.sslDescription.value == "") {
        document.multiQuotesForm.sslcode.value = "";
        document.multiQuotesForm.carrierContact.value = "";
        document.multiQuotesForm.carrierPhone.value = "";
        document.multiQuotesForm.carrierFax.value = "";
        document.multiQuotesForm.carrierEmail.value = "";
        document.getElementById("contactNameButton").style.visibility = 'hidden';
    }
}


function getAccountName(ev) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        checkbox();
        document.multiQuotesForm.buttonValue.value = "recalc";
        document.multiQuotesForm.focusValue.value = "accountNumber";
        document.multiQuotesForm.submit();
    }
}

function onCustomerBlur(clear) {
    if (clear == 'client') {
        document.getElementById('customerName1').value = document.getElementById('customerName').value;
    }
    document.getElementById('customerName').value = "";
    document.getElementById('clientNumber').value = "";
    document.getElementById('clienttype').value = "";
    document.getElementById('contactName').value = "";
    document.getElementById('email').value = "";
    document.getElementById('phone').value = "";
    document.getElementById('fax').value = "";
    document.getElementById("contactButton").style.visibility = 'hidden';
    jQuery('#clientIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
    getCreditStatus();
}

function focusOut() {
    if (document.getElementById("quoteId").value != "") {
        jQuery('#customerName').blur();
    }
}
function newDestinationEQ() {
    var path = "";
    if (document.getElementById('newDestination').checked) {
        document.getElementById('doorDestination').value = "";
        path = "&from=10&isDojo=false&checkDoor=true";
        Event.stopObserving("doorDestination", "blur");
    } else {
        document.getElementById('doorDestination').value = "";
        path = "&from=10&isDojo=false&checkDoor=false";
        Event.observe("doorDestination", "blur", function (event) {
            var element = Event.element(event);
            if (element.value != $("doorDestination_check").value) {
                element.value = '';
                $("doorDestination_check").value = '';
            }
        }
        );
    }
    appendEncodeUrl(path);
}

var stat;
var button;
function setFocus(ev1, ev2) {
    stat = ev1;
    button = ev2;
    setTimeout("setFocuss()", 800);
}
function setFocuss() {
    if (stat == "") {
        if (button == 'assignRemarks') {
            document.getElementById("isTerminal").focus();
            document.getElementById("isTerminal").select();
        }
    } else if (document.getElementById(stat) != null) {
        document.getElementById(stat).focus();
        document.getElementById(stat).select();
    }
}

function getUppercase(val) {
    var text;
    text = val;
    if (document.getElementById('newClient').checked) {
        document.getElementById('customerName1').value = text.toUpperCase();
    } else {
        document.getElementById('customerName').value = text.toUpperCase();
    }
}
function focusSetting(isContact) {
    if (document.getElementById('newClient').checked) {
        setFocusByElementId('customerName1');
    } else {
        testing(isContact);
    }
}
var temp1 = "", temp2 = "";
function testing(isContact) {
    document.getElementById('clienttype').value = "";
    document.getElementById('phone').value = "";
    document.getElementById('fax').value = "";
    if (!isContact) {
        document.getElementById('contactName').value = "";
        document.getElementById('email').value = "";
    }
    var account = document.getElementById('customerName').value;
    temp1 = document.getElementById('customerName').value;
    var accountNumber = document.getElementById('clientNumber').value;
    temp2 = document.getElementById('clientNumber').value;
    var importFlag = document.getElementById("importFlag").value;

//    if (importFlag === 'false') {
//        addBrandvalueForanAccount(temp2);
//    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: accountNumber
        },
        success: function (data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
                document.getElementById('clienttype').value = "";
                document.getElementById('contactName').value = "";
                document.getElementById('phone').value = "";
                document.getElementById('fax').value = "";
                document.getElementById('email').value = "";
                document.getElementById('customerName').value = "";
                document.getElementById('clientNumber').value = "";
            } else {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                        methodName: "getClientDetails",
                        param1: account,
                        param2: accountNumber,
                        dataType: "json"
                    },
                    success: function (result) {
                        populateClientDetails(result, isContact);
                    }
                });
            }
        }
    });
}

function populateClientDetails(data, isContact) {
    if (data != null && data.acctType != null && data.subType != null && (data.acctType == 'V' || data.acctType == 'V,SS' || data.acctType == 'V,O' || data.acctType == 'O,V') && ((data.subType).toLowerCase()) != 'forwarder') {
        alertNew("Please select the Customers  with Account Type S,C,O and V with Sub Type Forwarder");
        document.getElementById('customerName').value = "";
        document.getElementById('clienttype').value = "";
        document.getElementById('phone').value = "";
        document.getElementById('fax').value = "";
        document.getElementById('email').value = "";
        document.getElementById("clientNumber").value = "";
        jQuery('#clientIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
        document.getElementById("customerName").focus();
    } else if (data != null && data.acctNo != null && data.acctNo != "") {
//        document.getElementById("contactNameButton").style.visibility = 'visible';
        document.getElementById('customerName').value = data.acctName;
        temp1 = data.acctName;
        document.getElementById("clientNumber").value = data.acctNo;
        temp2 = data.acctNo;
        if (data.clientTypeForDwr != undefined && data.clientTypeForDwr != null) {
            document.getElementById('clienttype').value = data.clientTypeForDwr;
        } else {
            document.getElementById('clienttype').value = "";
        }
        if (!isContact) {
            document.multiQuotesForm.contactName.value = "";
        }
        if (data.phone != undefined && data.phone != null) {
            document.getElementById('phone').value = data.phone;
        } else {
            document.getElementById('phone').value = "";
        }
        if (data.fax != undefined && data.fax != null) {
            document.getElementById('fax').value = data.fax;
        } else {
            document.getElementById('fax').value = "";
        }
        if (!isContact) {
            if (data.email1 != undefined && data.email1 != null) {
                document.getElementById('email').value = data.email1;
            } else {
                document.getElementById('email').value = "";
            }
        }
        isCustomerNotes(data.acctNo);
        document.getElementById('contactButton').style.visibility = "visible";
        if (document.getElementById('fileType') && document.getElementById('fileType').value == 'I') {
            document.getElementById('isTerminal').focus();
        } else {
            document.getElementById('portofDischarge').focus();
        }
        if (document.getElementById('fileTypeS')) {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName: "getDefaultDetails",
                    param1: data.acctNo,
                    dataType: "json"
                },
                success: function (result) {
                    if (null !== result) {
                        fillDefaultCustomerData();
                    } else {
                        jQuery.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                methodName: "getDefaultDetailsAlert",
                                param1: data.acctNo,
                                dataType: "json"
                            },
                            success: function (data) {
                                if (jQuery.trim(data) !== "") {
                                    alertNewForDefaultValue(data.replace(/\n/g, "<br/>"));
                                }
                            }
                        });
                        setTimeout("setI()", 20);
                    }
                }
            });
        } else {
            setTimeout("setI()", 20);
        }
    }
}

function onclickAlertOk() {
    document.getElementById('AlertBoxDefaultValues').style.display = 'none';
    grayOut(false, '');
    fillDefaultCustomerValues();
}
var defaultCustomerData;
function fillDefaultCustomerData() {
    defaultCustomerData = null;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "getDefaultDetails",
            param1: document.getElementById("clientNumber").value,
            dataType: "json"
        },
        success: function (data) {
            if (null !== data) {
                defaultCustomerData = data;
                if (defaultCustomerData.importantNotes !== "" && defaultCustomerData.importantNotes !== undefined && defaultCustomerData.importantNotes !== null) {
                    alertNewForDefaultValue(defaultCustomerData.importantNotes.replace(/\n/g, "<br/>"));
                } else {
                    fillDefaultCustomerValues();
                }
            }
        }
    });
}
function changePolPodForNonRated() {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "getDefaultDetails",
            param1: document.getElementById("clientNumber").value,
            dataType: "json"
        },
        success: function (data) {
            if (null !== data) {
                if (document.getElementById("nonRated").checked) {
                    document.getElementById('placeofReceipt').value = data.origin;
                    document.getElementById('finalDestination').value = data.destination;
                } else {
                    document.getElementById('placeofReceipt').value = data.pol;
                    document.getElementById('finalDestination').value = data.pod;
                }
            }
        }
    });
}

function setI() {
    document.getElementById('customerName').value = temp1;
    document.getElementById("clientNumber").value = temp2;
    document.getElementById('clientNumberHidden').value = temp2;
    getCreditStatus();
}

function getContactNameAndEmailfromPopup(val1, val2, val3, val4) {
    document.multiQuotesForm.contactName.value = val1;
    document.multiQuotesForm.email.value = val2;
    if (val3 != "" && val3 != undefined && val3 != null) {
        document.multiQuotesForm.phone.value = val3;
    }
    if (val4 != "" && val4 != undefined && val4 != null) {
        document.multiQuotesForm.fax.value = val4;
    }
}
function assignRemarksValue(ev) {
    document.multiQuotesForm.remarks.value = ev;
    document.multiQuotesForm.buttonValue.value = "assignRemarks";
    document.multiQuotesForm.submit();
}
function getRemark(ev) {
    if (event.keyCode == 13 || event.keyCode == 9 || event.keyCode == 0) {
        var ind = ev.indexOf(":");
        if (ind != -1) {
            var newCode = ev.substring(0, ind);
            var newDesc = ev.substring(ind + 1, ev.length);
            document.getElementById('commentTemp').value = newCode;
            document.getElementById('commentRemark').value = newDesc;
        }
    }
}

function getTypeOfMove() {
    if (event.keyCode == 13 || event.keyCode == 9 || event.keyCode == 0) {

        var length = document.getElementById('move').length;
        for (var i = 0; i < length; i++) {
            if (document.getElementById('move').options[i].text == 'DOOR TO PORT') {
                document.getElementById('move').selectedIndex = i;
            }
        }
    } else {
        var path = "";
        if (document.getElementById('newOrigin').checked) {
            path = "&from=5&isDojo=false&check=false";
        } else {
            path = "&from=5&isDojo=false&check=true";
        }
        appendEncodeUrl(path);
    }
}
function getTypeOfMove1() {
    var path = "";
    if (document.multiQuotesForm.zip.value != "" && !document.getElementById('rampCheck').checked) {
        var length = document.getElementById('move').length;
        for (var i = 0; i < length; i++) {
            if (document.getElementById('move').options[i].text == 'DOOR TO DOOR') {
                document.getElementById('move').selectedIndex = i;
            }
        }

    } else if (document.multiQuotesForm.zip.value == "") {
        length = document.getElementById('move').length;
        for (i = 0; i < length; i++) {
            if (document.getElementById('move').options[i].text == 'PORT TO DOOR') {
                document.getElementById('move').selectedIndex = i;
            }
        }

    }
    if (document.getElementById('newDestination').checked) {
        path = "&checkDoor=true";
    } else {
        path = "&checkDoor=false";
    }
    appendEncodeUrl(path);
}
function getAllRemarksFromPopUp(val) {
    var oldarray = document.multiQuotesForm.comment.value;
    var splittedArray;
    if (oldarray.length == 0) {
        splittedArray = oldarray;
    } else {
        splittedArray = oldarray.split("\n");
    }
    var newarray = val.split(">>");
    var flag = false;
    for (var k = 0; k < newarray.length; k++) {
        flag = false;
        for (var l = 0; l < splittedArray.length; l++) {
            if (newarray[k].replace(/^[\s]+/, '').replace(/[\s]+$/, '').replace(/[\s]{2,}/, ' ') ==
                    splittedArray[l].replace(/^[\s]+/, '').replace(/[\s]+$/, '').replace(/[\s]{2,}/, ' ')) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            if (oldarray == "") {
                oldarray = newarray[k];
            } else {
                oldarray += "\n" + newarray[k];
            }
        }
    }
    document.multiQuotesForm.comment.value = oldarray;
}
function getSSlineAcctNo(ev) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getSSlineAcctNo",
                param1: ev,
                dataType: "json"
            },
            success: function (data) {
                populateSSlineAcctNo(data);
            }
        });
    }
}
function populateSSlineAcctNo(data) {
    document.multiQuotesForm.carrierContact.value = "";
    document.getElementById("contactNameButton").style.visibility = 'visible';
    document.multiQuotesForm.sslcode.value = data.acctNo != null ? data.acctNo : '';
    document.multiQuotesForm.carrierPhone.value = data.phone != null ? data.phone : '';
    document.multiQuotesForm.carrierFax.value = data.fax != null ? data.fax : '';
    document.multiQuotesForm.carrierEmail.value = data.email1 != null ? data.email1 : '';
    setTimeout("setF()", 150);
}


function focusSettingForSSl() {
    if (event.keyCode == 9 || event.keyCode == 13 || event.keyCode == 0) {
        setTimeout("testingForSSl()", 200);
    }
}

var temp3 = "", temp4 = "";
function testingForSSl() {
    document.multiQuotesForm.carrierContact.value = "";
    document.multiQuotesForm.carrierPhone.value = "";
    document.multiQuotesForm.carrierFax.value = "";
    document.multiQuotesForm.carrierEmail.value = "";
    var account = document.getElementById('sslDescription').value;
    temp3 = document.getElementById('sslDescription').value;
    var accountNumber = document.getElementById('sslcode').value;
    temp4 = document.getElementById('sslcode').value;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: accountNumber
        },
        success: function (data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
                document.multiQuotesForm.carrierContact.value = "";
                document.multiQuotesForm.carrierPhone.value = "";
                document.multiQuotesForm.carrierFax.value = "";
                document.multiQuotesForm.carrierEmail.value = "";
                document.getElementById('sslDescription').value = "";
                document.getElementById('sslcode').value = "";
            } else {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                        methodName: "getClientDetails",
                        param1: account,
                        param2: accountNumber,
                        dataType: "json"
                    },
                    success: function (data) {
                        populateClientDetails1(data);
                    }
                });
            }
        }
    });
}
function populateClientDetails1(data) {
    if (document.multiQuotesForm.ratesNonRates[0].checked) {
        if (null != data && (null == data.sslineNumber || data.sslineNumber == '' || data.sslineNumber == '00000')) {
            alertNew("Please select Steamship Line with 5 digit Ssline Number");
            document.multiQuotesForm.carrierContact.value = "";
            document.multiQuotesForm.carrierPhone.value = "";
            document.multiQuotesForm.carrierFax.value = "";
            document.multiQuotesForm.carrierEmail.value = "";
            document.getElementById('sslDescription').value = "";
            document.getElementById('sslcode').value = "";
            return;
        }
    }
    document.getElementById("contactNameButton").style.visibility = 'visible';
    document.multiQuotesForm.sslDescription.value = data.acctName;
    temp3 = data.acctName;
    document.multiQuotesForm.sslcode.value = data.acctNo;
    temp4 = data.acctNo;

    document.multiQuotesForm.carrierContact.value = "";
    if (data.phone != undefined && data.phone != null) {
        document.multiQuotesForm.carrierPhone.value = data.phone;
    } else {
        document.multiQuotesForm.carrierPhone.value = "";
    }
    if (data.fax != undefined && data.fax != null) {
        document.multiQuotesForm.carrierFax.value = data.fax;
    } else {
        document.multiQuotesForm.carrierFax.value = "";
    }
    if (data.email1 != undefined && data.email1 != null) {
        document.multiQuotesForm.carrierEmail.value = data.email1;
    } else {
        document.multiQuotesForm.carrierEmail.value = "";
    }
    document.getElementById('carrierContact').focus();
    setTimeout("set1()", 20);
}
function set1() {
    document.multiQuotesForm.sslDescription.value = temp3;
    document.multiQuotesForm.sslcode.value = temp4;
}
function checkEmail() {
    if (document.multiQuotesForm.email.value == "") {
        document.multiQuotesForm.ccEmail.checked = false;
        alertNew("Please Enter Email Id");
        return;
    }
}


function setFocusFromDojo(focusTo) {
    if (document.getElementById(focusTo)) {
        document.getElementById(focusTo).focus();
    }
}
function setFocusFromorigin() {
    if (document.getElementById('getRates')) {
        document.getElementById('getRates').focus();
    }
}



function openBlueScreenNotesInfo(customerNo, customerName) {
    GB_show("Client Notes", rootPath + "/bluescreenCustomerNotes.do?methodName=displayNotes&customerNo=" + customerNo + "&customerName=" + customerName, 400, 1000);
}
function isCustomerNotes(acctNo) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkCustomerNotes",
            param1: acctNo,
            dataType: "json"
        },
        success: function (data) {
            if (data) {
                jQuery('#clientIcon').attr("src", rootPath + "/img/icons/e_contents_view1.gif");
            } else {
                jQuery('#clientIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
            }
        }
    });
}

function showRateGrid(route, path) {
    var origin = "";
    var destination = "";
    var selectedList = "";
    var distance = "";
    var distanceList = "";
    var fileType = "";
    var doorOrigin = "";
    var checkBoxes = document.getElementsByName("originDestination");
    var importFlag = document.getElementById('fileType') && null !== document.getElementById('fileType').value && document.getElementById('fileType').value === 'I' ? true : false;
    if (importFlag === true) {
        fileType = document.getElementById("fileType").value;
    } else {
        fileType = null;
    }
    for (i = 0; i < checkBoxes.length; i++) {
        if (checkBoxes[i].checked) {
            distance = "";
            distance = null != checkBoxes[i].parentNode.childNodes[1] ? checkBoxes[i].parentNode.childNodes[1].id : "";
            if (selectedList == "") {
                selectedList = checkBoxes[i].value;
                if (route == "Destination") {
                    distanceList = "" != (distance) && null != (distance) ? checkBoxes[i].value + "=" + distance : checkBoxes[i].value;
                }
            } else {
                selectedList = selectedList + "," + checkBoxes[i].value;
                if (route == "Destination") {
                    distanceList = "" != (distance) && null != (distance) ? distanceList + "," + checkBoxes[i].value + "=" + distance : distanceList + "," + checkBoxes[i].value;
                }
            }
        }
    }
    if (selectedList == "") {
        alertNew("Please Select atleast One");
        return;
    }
    if ("Origin" == route) {
        origin = document.multiQuotesForm.isTerminal.value;
        destination = selectedList;
    } else {
        destination = document.multiQuotesForm.portofDischarge.value;
        origin = selectedList;
    }
    var haz = "N";
    if (document.multiQuotesForm.hazmat[0].checked) {
        haz = "Y";
    }
    var region = document.getElementsByName("region");
    var selectedRegion = "";
    for (i = 0; i < region.length; i++) {
        if (region[i].checked) {
            selectedRegion = selectedRegion != "" ? selectedRegion + ', ' + region[i].id : region[i].id;
        }
    }
    var imsChecked = "";
    if (jQuery('#enableIMS').attr("checked")) {
        imsChecked = "checked";
    }
    var destinationPort = document.multiQuotesForm.portofDischarge.value;
    var originPort = "";
    if (trim(document.multiQuotesForm.isTerminal.value) != "") {
        originPort = document.multiQuotesForm.isTerminal.value;
    }
    url = path + '/rateGrid.do?action=' + route + "&origin=" + origin + "&ratesFrom=" + "multiRates" +
            "&destination=" + destination + "&doorOrigin=" + "" + "&commodity="
            + document.multiQuotesForm.commcode.value + '&hazardous=' + haz
            + "&region=" + selectedRegion + "&distances=" + distanceList + "&imsChecked=" + imsChecked
            + "&destinationPort=" + destinationPort + "&originPort=" + originPort + "&fileType=" + fileType;
    GB_show('FCL Rates Comparison Grid', url, document.body.offsetHeight - 20, document.body.offsetWidth - 100);

}


function getMultiRates(importFlag, val) {
    var haz = "";
    if (document.multiQuotesForm.hazmat[0].checked) {
        haz = "Y";
    } else {
        haz = "N";
    }

    var fileType = "";
    if (importFlag === true) {
        fileType = document.getElementById("fileType").value;
    } else {
        fileType = null;
    }
    if (document.multiQuotesForm.commcode.value == "") {
        alertNew("PLEASE SELECT COMMODITY CODE");
        jQuery("#commcode").css("border-color", "red");
        //document.multiQuotesForm.commcode.focus();
        return;
    }
    if ((trim(document.multiQuotesForm.portofDischarge.value) == "" || trim(document.multiQuotesForm.isTerminal.value) == "")
            && document.getElementById("bulletRates") && document.getElementById('bulletRates').checked) {
        alertNew("Please Enter BOTH Origin and Destination when looking up Bullet Rates");
        jQuery("#portofDischarge").css("border-color", "red");
        jQuery("#isTerminal").css("border-color", "red");
        return;
    }

    if (trim(document.multiQuotesForm.portofDischarge.value) == "" && trim(document.multiQuotesForm.isTerminal.value) == "") {
        alertNew("PLEASE SELECT DESTINATION PORT OR ORIGIN");
        jQuery("#portofDischarge").css("border-color", "red");
        jQuery("#isTerminal").css("border-color", "red");
        return;
    }

    if (document.multiQuotesForm.commcode.value != "") {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "validateCommodityCode",
                param1: document.multiQuotesForm.commcode.value
            },
            success: function (data) {
                if (data === "true") {
                    alertNew("Please Change Commodity Code for FCL");
                } else {
                    //get all the origin for given destination (or) get all destination for given origin
                    if ((trim(document.multiQuotesForm.portofDischarge.value) == "" || trim(document.multiQuotesForm.isTerminal.value) == "")) {
                        var searchBy = "city";
                        if (trim(document.multiQuotesForm.portofDischarge.value) != "") { 
                            if (!document.getElementById('destinationCity').checked) {
                                searchBy = "country";
                            }
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                    methodName: "getOriginsForDestination",
                                    forward: "/jsps/fclQuotes/OriginAndDestination.jsp",
                                    param1: document.multiQuotesForm.isTerminal.value,
                                    param2: document.multiQuotesForm.portofDischarge.value,
                                    param3: searchBy,
                                    param4: "",
                                    param5: document.multiQuotesForm.commcode.value
                                },
                                preloading: true,
                                success: function (data) {
                                    if (jQuery.trim(data) === "") {
                                        alert("No Data Found!");
                                    } else if (data.length > 100) {
                                        showOriginDestinationList(data);
                                        document.getElementById("top10Span").style.display = "none";
                                    }
                                    else {
                                        openGetRates(haz, val, fileType);
                                    }
                                }
                            });
                        } else {
                            if (!document.getElementById('originCountry').checked) {
                                searchBy = "country";
                            }
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                    methodName: "getDestinationsForOrigin",
                                    forward: "/jsps/fclQuotes/OriginAndDestination.jsp",
                                    param1: document.multiQuotesForm.portofDischarge.value,
                                    param2: document.multiQuotesForm.isTerminal.value,
                                    param3: searchBy,
                                    param4: "",
                                    param5: document.multiQuotesForm.commcode.value
                                },
                                preloading: true,
                                success: function (data) {
                                    if (data.length > 100) {
                                        showOriginDestinationList(data);
                                    } else {
                                        document.multiQuotesForm.portofDischarge.value = jQuery.trim(data);
                                        document.multiQuotesForm.portofDischarge_check.value = jQuery.trim(data);
                                        openGetRates(haz, val, fileType);
                                    }
                                }
                            });
                        }
                    } else if (document.getElementById("showAllCity") && document.getElementById("showAllCity").checked) {
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                methodName: "getAllCountryPorts",
                                param1: document.multiQuotesForm.portofDischarge.value
                            },
                            preloading: true,
                            success: function (data) {
                                var portOfDisch = document.multiQuotesForm.portofDischarge.value;
                                var country = portOfDisch.split("/")[1];
                                var origin = null;
                                var arry = null;
                                var passedValue = null;
                                if (null != data && data.length > 1) {
                                    passedValue = data.split('||');
                                    var allCity = new Array();
                                    for (i = 0; i < passedValue.length; i++) {
                                        arry = passedValue[i].split('==');
                                        if (null == origin) {
                                            origin = arry[0];
                                            allCity[i] = arry[1];
                                        } else {
                                            origin = origin + ',' + arry[0];
                                            allCity[i] = arry[1];
                                        }
                                    }
                                    window.parent.hideProgressBar();
                                    var destinationPort = document.multiQuotesForm.portofDischarge.value;
                                    var originPort = "";
                                    if (trim(document.multiQuotesForm.isTerminal.value) != "") {
                                        originPort = document.multiQuotesForm.isTerminal.value;
                                    }
                                    var url = path + '/rateGrid.do?action=Origin&origin=' + document.multiQuotesForm.isTerminal.value;
                                    url += "&destination=" + origin + "&commodity=" + document.multiQuotesForm.commcode.value;
                                    url += "&ratesFrom=" + "multiRates";
                                    url += '&hazardous=' + haz + "&destinationPort=" + destinationPort + "&originPort=" + originPort;
                                    url += "&bulletRates=" + jQuery("#bulletRates").is(":checked") + "&originalCommodity=" + document.multiQuotesForm.commcode.value + "&fileType=" + fileType;
                                    var height = jQuery(window).height() - 40;
                                    var width = jQuery(window).width() - 100;
                                    GB_show('FCL Rates Comparison Grid', url, height, width);
                                } else {
                                    openGetRates(haz, val, fileType);
                                }
                            }
                        });
                    } else {

                        var destinationPort = document.multiQuotesForm.portofDischarge.value;
                        var originPort = "";
                        if (trim(document.multiQuotesForm.isTerminal.value) != "") {
                            originPort = document.multiQuotesForm.isTerminal.value;
                        }

                        if (document.getElementById("bulletRates") && jQuery("#bulletRates").is(":checked")) {
                            openGetRates(haz, val, fileType);
                        } else {
                            jQuery.ajaxx({
                                dataType: "json",
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                    methodName: "getAllSynonymousCity",
                                    param1: document.multiQuotesForm.portofDischarge.value,
                                    param2: document.multiQuotesForm.isTerminal.value,
                                    dataType: "json"
                                },
                                preloading: true,
                                success: function (data) {
                                    if (jQuery.trim(data) !== "") {
                                        if (null != data[0] && data[0] != '' && data[0].split(",").length > 1) {
                                            if (null != data[1] && data[1] != '' && data[1].split(",").length > 1) {
                                                var url = path + '/rateGrid.do?action=Origin&origin=' + data[1];
                                                url += "&destination=" + data[0] + "&originName=" + document.multiQuotesForm.isTerminal.value;
                                                url += "&ratesFrom=" + "multiRates";
                                                url += "&commodity=" + document.multiQuotesForm.commcode.value + '&hazardous=' + haz;
                                                url += "&destinationPort=" + destinationPort + "&originPort=" + originPort;
                                                url += "&bulletRates=" + jQuery("#bulletRates").is(":checked") + "&originalCommodity=" + document.multiQuotesForm.commcode.value + "&fileType=" + fileType;
                                                var height = jQuery(window).height() - 40;
                                                var width = jQuery(window).width() - 100;
                                                GB_show('FCL Rates Comparison Grid', url, height, width);
                                            } else {
                                                var url = path + '/rateGrid.do?action=Origin&origin=' + document.multiQuotesForm.isTerminal.value;
                                                url += "&destination=" + data[0] + "&ratesFrom=" + "multiRates";
                                                url += "&commodity=" + document.multiQuotesForm.commcode.value + '&hazardous=' + haz;
                                                url += "&destinationPort=" + destinationPort + "&originPort=" + originPort;
                                                url += "&bulletRates=" + jQuery("#bulletRates").is(":checked") + "&originalCommodity=" + document.multiQuotesForm.commcode.value + "&fileType=" + fileType;
                                                var height = jQuery(window).height() - 40;
                                                var width = jQuery(window).width() - 100;
                                                GB_show('FCL Rates Comparison Grid', url, height, width);
                                            }
                                        } else if (null != data[1] && data[0] != '' && data[1].split(",").length > 1) {
                                            var url = path + '/rateGrid.do?action=Origin&origin=' + data[1];
                                            url += "&destination=" + document.multiQuotesForm.portofDischarge.value;
                                            url += "&originName=" + document.multiQuotesForm.isTerminal.value;
                                            url += "&ratesFrom=" + "multiRates";
                                            url += "&commodity=" + document.multiQuotesForm.commcode.value + '&hazardous=' + haz;
                                            url += "&destinationPort=" + destinationPort + "&originPort=" + originPort;
                                            url += "&bulletRates=" + jQuery("#bulletRates").is(":checked") + "&originalCommodity=" + document.multiQuotesForm.commcode.value + "&fileType=" + fileType;
                                            var height = jQuery(window).height() - 40;
                                            var width = jQuery(window).width() - 100;
                                            GB_show('FCL Rates Comparison Grid', url, height, width);
                                        }
                                        else {
                                            openGetRates(haz, val, fileType);
                                        }
                                    }
                                    else {
                                        openGetRates(haz, val, fileType);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}
function openGetRates(haz, val, fileType) {
    var destinationPort = document.multiQuotesForm.portofDischarge.value;
    var originPort = "";
    if (trim(document.multiQuotesForm.isTerminal.value) != "") {
        originPort = document.multiQuotesForm.isTerminal.value;
    }
    var url = path + '/rateGrid.do?action=getrates&origin=' + document.multiQuotesForm.isTerminal.value;
    url += "&destination=" + document.multiQuotesForm.portofDischarge.value + "&commodity=" + document.multiQuotesForm.commcode.value;
    url += "&noOfDays=" + "" + "&hazardous=" + haz + "&fileNo=" + val;
    url += "&ratesFrom=" + "multiRates";
    url += "&destinationPort=" + destinationPort + "&originPort=" + originPort;
    url += "&bulletRates=" + jQuery("#bulletRates").is(":checked") + "&originalCommodity=" + document.multiQuotesForm.commcode.value + "&fileType=" + fileType;
    var height = jQuery(window).height() - 40;
    var width = jQuery(window).width() - 100;
    GB_show('FCL Rates Comparison Grid', url, height, width);
}

function selectedMenuMulti(ssline, selectionInsert, selectedUnits) {

    var ssline1 = ssline.toString();//14575;48810;ANTMAR0008;1703,38239;50824;SEASTA0006;1703
    var selectionInsert1 = selectionInsert.toString();//ANTILLEAN MARINE-;ANTMAR0008-;Puerto Plata/DOMINICAN REPUBLIC(DOPOP)-;MIAMI, FL/FL/UNITED STATES(USMIA)-;006100-;N&,
    var unitType = selectedUnits.toString();//A=20,B=40,C=40HC,D=45,E=48
    document.multiQuotesForm.ssLine.value = ssline1;
    document.multiQuotesForm.selectionInsert.value = selectionInsert1;
    document.multiQuotesForm.unitTypes.value = unitType;
    document.multiQuotesForm.methodName.value = "getRates";
    window.parent.showPreloading();
    document.multiQuotesForm.submit();
}


function addRatesDiv(importFlag) {
    if (document.multiQuotesForm.customerName.value == "") {
        if (document.multiQuotesForm.newClient.checked) {
            if (trim(document.multiQuotesForm.customerName1.value) == "") {
                alertNew("PLEASE ENTER CLIENT");
                jQuery("#customerName1").css("border-color", "red");
                // document.multiQuotesForm.customerName1.focus();
                return;
            }
        }
        else if (trim(document.multiQuotesForm.customerName.value) == "") {
            alertNew("PLEASE ENTER CLIENT");
            jQuery("#customerName").css("border-color", "red");
            // document.multiQuotesForm.customerName.focus();
            return;
        }
    }
    showAlternateMask();
    jQuery("#addRatesDivId").center().show(500);
    jQuery("#portofDischarge").val("").focus();
    jQuery("#isTerminal").val("")
    if (importFlag == true) {
        jQuery("#commcode").val("006205");
    } else {
        jQuery("#commcode").val("006100");
    }
    document.getElementById("bulletRates").checked = false;
    jQuery('input:radio[name=hazmat]')[1].checked = true;
}

function closeDivs() {
    hideAlternateMask();
    document.getElementById("addRatesDivId").style.display = "none";
}

function ConvertToQuote() {
    if (document.getElementById("quoteCompleteY").checked == true) {
        if (jQuery('input[name="multiQuoteRadioId"]:checked').length == 0) {
            alertNew("Please Choose One Carrier");
            return;
        }
        else {
            var quoteBy = jQuery('#quoteBy').val();
            var quoteDate = jQuery('#quoteDate').val();
            confirmNew("Are you sure you want to convert this Quote to Booking?\n" +
                    "Created By--> " + quoteBy + " on " + quoteDate, "convertToQuote");
        }
    }
    else {
        alertNew("Quote is Not Complete");
        return;
    }
}

function convertToQuote2() {
    var fileNoSplit = document.getElementById("fileNo").value.split("-", 2);//Ex:04-489027
    var fileNo = fileNoSplit[1];
    var d = new Date();
    var multiQuoteDate = document.getElementById("quoteDate").value;
    var mm = (d.getMonth() < 9 ? "0" : "") + (d.getMonth() + 1);
    var dd = (d.getDate() < 10 ? "0" : "") + d.getDate();
    var yyyy = d.getFullYear();
    var quoteDate = mm + "/" + dd + "/" + yyyy;
    var multiQuoteId = document.getElementById("multiQuoteRadioId").value;

    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "rateChangeAlertForMultiQuote",
            param1: fileNo,
            param2: multiQuoteId,
            request: "true",
            dataType: "json"
        },
        preloading: true,
        success: function (data) {
            if (data) {
                convertToQuote3();
            }
            else {
                var url = path + "/fclQuotes.do?buttonValue=getRatesBooking&multiQuoteFlag=multiQuote&fileNo=" + fileNo + "&multiQuoteId=" + multiQuoteId
                        + "&multiQuoteDate=" + multiQuoteDate + "&quoteDate=" + quoteDate;
                GB_show("Rate Change Alert", url, 500, 950);
            }
        }
    });
}

function convertToQuote3() {
    document.multiQuotesForm.methodName.value = "convertToQuote"
    window.parent.showPreloading();
    document.multiQuotesForm.submit();
}

function printFaxEmail() {
    if (document.getElementById("quoteCompleteY").checked == true) {
        var quotationNo = document.getElementById("quoteId").value;
        var fileNoSplit = document.getElementById("fileNo").value.split("-", 2);//Ex:04-489027
        var fileNo = fileNoSplit[1];
        GB_show("MultiQuote Report", path + "/printConfig.do?screenName=" + "MultiQuote" + "&fileNo=" + fileNo + "&quotationNo=" + quotationNo, 350, 900);
    }
    else {
        alertNew("Quote is Not Complete");
        return;
    }
}


function saveMethod() {
    if (document.multiQuotesForm.customerName.value == "") {
        if (document.multiQuotesForm.newClient.checked) {
            if (trim(document.multiQuotesForm.customerName1.value) == "") {
                alertNew("PLEASE ENTER CLIENT");
                jQuery("#customerName1").css("border-color", "red");
                // document.multiQuotesForm.customerName1.focus();
                return;
            }
        }
        else if (trim(document.multiQuotesForm.customerName.value) == "") {
            alertNew("PLEASE ENTER CLIENT");
            jQuery("#customerName").css("border-color", "red");
            // document.multiQuotesForm.customerName.focus();
            return;
        }
    }
    if (document.getElementById("quoteId").value != "") {
        document.getElementById("save").disabled = true;
        document.multiQuotesForm.methodName.value = "save";
        window.parent.showPreloading();
        document.multiQuotesForm.submit();
    }
    else {
        alertNew("Please Choose Rates");
        return false;
    }
}

function goBackMethod() {
    if (document.getElementById("quoteId").value != "") {
        goback();
    }
    else {
        document.multiQuotesForm.methodName.value = "goBack";
        window.parent.showPreloading();
        document.multiQuotesForm.submit();
    }
}

function goback() {
    document.getElementById("confirmNo").style.width = "88" + "px";
    document.getElementById("confirmNo").value = "Exit without Save";
    confirmNew("Do you want to save the Quote changes?", "goBack");
}

var returnValue;
var formChanged = false;

function goBackMain() {

    if (document.getElementById("quoteId").value != "" && document.getElementById("quoteCompleteY").checked) {
        document.multiQuotesForm.methodName.value = "goBack";
//       document.multiQuotesForm.focusValue.value ="inoutComplete";
        window.parent.showPreloading();
        document.multiQuotesForm.submit();
    }
    else if (document.getElementById("quoteId").value != "") {
//      alert("changed");
        if (isMultiFormChanged()) {
            document.getElementById("confirmNo").style.width = "88" + "px";
            document.getElementById("confirmNo").value = "Exit without Save";
            confirmYesNoCancel("Do You want to save the Quote changes?", "goBack");
        }
    }
    else {
        document.multiQuotesForm.methodName.value = "goBack";
        window.parent.showPreloading();
        document.multiQuotesForm.submit();
    }

}



function isMultiFormChanged() {
    var form = "#multiQuotesForm";
    var selector = jQuery(form + " input[type=text], " + form + " textarea");
    jQuery(selector).each(function () {
        if (jQuery(this).val() !== jQuery(this).data('initial_value')) {
            formChanged = true;
        }
    });
    return formChanged;
}


function confirmYesNoCancel(confirmMessage, confirmId) {
    returnValue = confirmId;
    DisplayConfirmYesNoCancel("ConfirmYesNoCancelDiv", 100, 50, confirmMessage, window.center({
        width: 100,
        height: 100
    }));
}

function confirmOptionYes() {
    document.getElementById("ConfirmYesNoCancelDiv").style.display = "none";
    grayOut(false, "");
    confirmMessageFunction(returnValue, "yes");
}

function confirmOptionNo() {
    document.getElementById("ConfirmYesNoCancelDiv").style.display = "none";
    grayOut(false, "");
    confirmMessageFunction(returnValue, "no");
}
function confirmOptionCancel() {
    document.getElementById("ConfirmYesNoCancelDiv").style.display = "none";
    grayOut(false, "");
    confirmMessageFunction(returnValue, "cancel");
}

function DisplayConfirmYesNoCancel(divId, left, top, confirmMessage, point) {
    document.getElementById(divId).style.left = left + "px";
    document.getElementById(divId).style.top = top + "px";
    document.getElementById("confirmMessagePara").innerHTML = confirmMessage;
    document.getElementById(divId).style.display = "block";
    document.getElementById(divId).style.left = point.x - 100 + "px";
    document.getElementById(divId).style.top = point.y + "px";
    document.getElementById(divId).style.zIndex = "1000";
    grayOut(true, "");
}

function confirmMessageFunction(id1, id2) {
    if (id1 == "goBack" && id2 == "yes") {
        yesFunction();
    } else if (id1 == "goBack" && id2 == "no") {
        noFunction();
    } else if (id1 == "goBack" && id2 == "cancel") {
        cancelFunction();
    } else if (id1 == "confirm" && id2 == "cancel") {
        cancelQuoteCompleteFunction();
    } else if (id1 == "confirm" && id2 == "ok") {
        quoteComplete();
    } else if (id1 == "convertToQuote" && id2 == "cancel") {
        cancelFunction();
    } else if (id1 == "convertToQuote" && id2 == "ok") {
        convertToQuote2();
    }
}

function yesFunction() {
    if (document.getElementById('newClient').checked) {
        if (document.multiQuotesForm.customerName1.value == "") {
            alertNew("PLEASE ENTER CLIENT");
            jQuery("#customerName").css("border-color", "red");
            return;
        }
    } else {
        if (document.multiQuotesForm.customerName.value == "") {
            alertNew("PLEASE ENTER CLIENT");
            jQuery("#customerName").css("border-color", "red");
            return;
        }
    }
    document.multiQuotesForm.focusValue.value = "saveAndExit";
    saveMethod();
}

function noFunction() {
    document.multiQuotesForm.methodName.value = "goBack";
    window.parent.showPreloading();
    document.multiQuotesForm.submit();
}

function cancelFunction() {
    return;
}

function cancelQuoteCompleteFunction() {
    document.getElementById("quoteCompleteN").checked = true;
    return;
}

function quoteCompleteM() {
    if (document.getElementById("fileNo").value != "NEW") {
        var confirmationMsg = "Once you make this quote complete, you will no longer be able to make any changes to it. ARE YOU SURE?";
        confirmNew(confirmationMsg, "confirm");
    }
    else if (document.getElementById("quoteId").value != "") {
        document.getElementById("quoteCompleteN").checked = true;
        alertNew("Please Save Quote");
        return;
    }
    else {
        document.getElementById("quoteCompleteN").checked = true;
        alertNew("Please Choose Rates");
        return;
    }
}


function quoteCompleteOnLoad() {
    if (document.getElementById("convertButtonFlag").value == "true") {
        document.getElementById("quoteCompleteY").checked = true;
        document.getElementById("quoteCompleteY").disabled = true;
        document.getElementById("quoteCompleteN").disabled = true;
        document.getElementById("Options").disabled = true;
        document.getElementById("save").disabled = true;
        document.getElementById("contactButton").disabled = true;
        var element;
        var form = document.getElementById('multiQuotesForm');
        for (var i = 0; i < form.elements.length; i++) {
            element = form.elements[i];
            if (element.type === "text" || element.type === "textarea") {
                element.style.backgroundColor = "#CCEBFF";
                element.readOnly = true;
                element.style.borderTop = "0px";
                element.style.borderBottom = "0px";
                element.style.borderRight = "0px";
                element.style.borderLeft = "0px solid";
            }
            if (element.type === "checkbox") {
                element.disabled = true;
            }
        }

    }
}

function quoteComplete() {
    document.multiQuotesForm.focusValue.value = "quoteComplete";
    saveMethod();
}


function getMultiMode(val) {
    if (val == 'expand') {
        document.getElementById("collapse").style.visibility = "visible";
        document.getElementById("expand").style.visibility = "hidden";
        document.getElementById('expandData').style.display = "table";
        document.getElementById('collapseData').style.display = "none";
    }
    else {

        document.getElementById("expand").style.visibility = "visible";
        document.getElementById("collapse").style.visibility = "hidden";
        document.getElementById('collapseData').style.display = "table";
        document.getElementById('expandData').style.display = "none";
    }
}

function selectedMenu(val1, val2) {
    document.multiQuotesForm.multiQuoteRadioId.value = val2;
    if (val1 == "oldgetRatesBKG") {
        convertToQuote3();
    }
    else if (val1 == "newgetRatesBKG") {
        document.multiQuotesForm.methodName.value = "convertQuoteNewRates"
        window.parent.showPreloading();
        document.multiQuotesForm.submit();
    }
}

 function getContactInfo() {
                var cust = "";
                if (document.getElementById('customerName').value != undefined) {
                    cust = document.getElementById('customerName').value;
                } else {
                    cust = document.getElementById('customerName1').value;
                }
                var customerName = cust.replace("&", "amp;");
                var customer = document.getElementById("clientNumber").value;
                var contactName = document.multiQuotesForm.contactName.value;
                if (contactName == '%') {
                    contactName = 'percent';
                }
                GB_show('Contact', '${path}/customerAddress.do?buttonValue=Quotation&custNo=' + customer + '&custName=' + customerName + '&contactName=' + contactName,
                        width = "400", height = "1100");
            }
          

function getDestination() {
    if (document.multiQuotesForm.portofDischarge.value == "") {
        var pod = document.multiQuotesForm.finalDestination.value;
        var index = pod.indexOf("/");
        var podNew = pod.substring(0, index);
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getPortsForAgentInfo",
                param1: podNew
            },
            success: function (data) {
                populateAgentDojo(data);
            }
        });
    }

}
function populateAgentDojo(data) {
    if (data == "empty" || document.multiQuotesForm.routedAgentCheck.value == "yes") {
        document.getElementById("agent").value = "";
        document.getElementById("agentNo").value = "";
        document.getElementById("routedbymsg").value = "";
    }
}
