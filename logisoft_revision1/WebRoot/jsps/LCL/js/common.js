/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    $(document).keydown(function (e) {
        if ($(e.target).attr("readonly")) {
            if (e.keyCode === 8) {
                return false;
            }
        }
    });
});
function scan(documentId, screenName, moduleName, importFlag, bookingType) {
    if (null !== documentId && documentId !== '') {
        //document.getElementById("scrollUp").scrollIntoView(true);
        GB_show("Scan", "/logisoft/scan.do?screenName=" + screenName + "&documentId=" + documentId + "&importFlag=" + importFlag + "&quoteBookingName=" + moduleName + "&bookingType=" + bookingType + "&", 450, 700);
    } else {
        alert("Please save the file before Scan/Attach");
    }
}
function printreport(path, fileId, fileNo, screenName, transhipment, subject) {
    if (null !== fileId && fileId !== '') {
        var comment = "";
        if ($('#externalComment').val() !== undefined && $('#externalComment').val() !== '') {
            comment = $('#externalComment').val();
        }
        var issuTerm=$("#terminalLocation").val();
        issuTerm = screenName === 'LCLQuotation'? issuTerm :'' ; 
        var url = path + "/printConfig.do?screenName=" + screenName + "&fileId=" + fileId;
        url += "&fileNo=" + fileNo + "&comment=" + comment + "&subject=" + subject;
        url += "&emailSubject=" + subject + "&transhipment=" + transhipment +"&issuingTerminal=" + issuTerm;
        GB_show("Print", url, 350, 1000);
    } else {
        alert("Please save the file before print/fax");
    }
}
function printExportBLReport(path, fileId, fileNo, screenName, transhipment, subject) {
    var portText = $('#portText').val();
    var freightPickupText = $('#freightPickupText').val();
    var hblPierText = $('#hblPierText').val();
    var hblPolText = $('#hblPolText').val();
    var deliveryText = $('#deliveryText').val();
    if ($("#portY").is(':checked') && (portText == '')) {
        $.prompt('Print Alt Port lower right is required in print option');
        return false;
    }
    if ($("#freightPickupY").is(':checked') && (freightPickupText == '')) {
        $.prompt('Freight Pickup account is required in print option ');
        return false;
    }
    if ($("#hblPierY").is(':checked') && (hblPierText == '')) {
        $.prompt('HBL Pier Override is required in print option ');
        return false;
    }
    if ($("#hblPolY").is(':checked') && (hblPolText == '')) {
        $.prompt('HBL Pol Override is required in print option ');
        return false;
    }
    if ($("#deliveryOverrideY").is(':checked') && (deliveryText == '')) {
        $.prompt('Final Delivery To override is required in print option ');
        return false;
    }

    if (null !== fileId && fileId !== '') {
        var postedLclBl = $("#postButton").attr("class").indexOf('green-background') != -1 ? "postBl" : "unPostBl";
        var billToParty = $("input:radio[name=billPPD]:checked").val();
        billToParty = billToParty === 'A' ? "Agent" : billToParty === 'S' ? "Shipper" : billToParty === 'F'
                ? "Forwarder" : billToParty === 'T' ? "ThirdParty" : "";
        var comment = "";
        if ($('#externalComment').val() !== undefined && $('#externalComment').val() !== '') {
            comment = $('#externalComment').val();
        }
        var cob = $('#blUnitCob').val() === '' ? false : $('#blUnitCob').val();
        var url = path + "/printConfig.do?screenName=" + screenName + "&fileId=" + fileId + "&cob=" + cob;
        url += "&fileNo=" + fileNo + "&comment=" + comment + "&subject=" + subject + "&postedLclBl=" + postedLclBl;
        url += "&emailSubject=" + subject + "&transhipment=" + transhipment + "&billToParty=" + billToParty;
        GB_show("Print", url, 350, 1000);
    } else {
        alert("Please save the file before print/fax");
    }
   
}
function openWarehouse() {
    GB_show("Add Warehouse", "/logisoft/jsps/fclQuotes/AddFclBlWareHouse.jsp?wareHouseType=LCLE&field=pickUp", 325, 730);
}

function changeScanButtonColor(masterStatus, documentName) {
    document.getElementById("scan-attach").className = "green-background";
    document.getElementById("scan-attach1").className = "green-background";
}

function displayClientNotes(moduleName) {
    var companyName = "";
    if ($('#client').val() != "") {
        companyName = $('#client').val();
    } else if ($('#clientCons').val() == "") {
        companyName = $('#clientCons').val();
    } else {
        companyName = $('#ManualClient').val();
    }
    addorUpdateNotes("Client",
            companyName, $('#city').val(), $('#state').val(), $('#zip').val(),
            $('#client_no').val(), $('#address').val(), $('#fax').val(),
            $('#phone').val(), moduleName)

}
function addorUpdateNotes(vendorName, companyName, city, state, zip, acctNo, address, fax, phone, moduleName) {
    var fileId = $('#fileNumberId').val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "showClientSectionNotes",
            param1: $.type(vendorName) !== "undefined" ? vendorName : "",
            param2: $.type(fileId) !== "undefined" ? fileId : "",
            param3: $.type(companyName) !== "undefined" ? companyName : "",
            param4: $.type(city) !== "undefined" ? city : "",
            param5: $.type(state) !== "undefined" ? state : "",
            param6: $.type(zip) !== "undefined" ? zip : "",
            param7: $.type(acctNo) !== "undefined" ? acctNo : "",
            param8: $.type(address) !== "undefined" ? address : "",
            param9: $.type(fax) !== "undefined" ? fax : "",
            param10: $.type(phone) !== "undefined" ? phone : "",
            param11: $.type(moduleName) !== "undefined" ? moduleName : "",
            param12: $('#loginUserId').length > 0 ? $('#loginUserId').val() : ""
        },
        async: false,
        success: function (data) {

        }
    });
}

function displayShipperNotes(moduleName) {
    var companyName = "";
    if ($('#shipperNameClient').val != "") {
        companyName = $('#shipperNameClient').val();
    }
    else if ($('#dupShipName').val != "") {
        companyName = $('#dupShipName').val();
    }
    addorUpdateNotes("Shipper", companyName,
            $('#shipperCity').val(), $('#shipperState').val(), $('#shipperZip').val(), $('#shipperCodeClient').val(),
            $('#shipperAddress').val(), $('#shipperFaxClient ').val(), $('#shipperPhoneClient').val(), moduleName)
}

function displayForwarderNotes(moduleName) {
    var companyName = "";
    if ($('#forwarderNameClient').val() != "") {
        companyName = $('#forwarderNameClient').val();
    }
    addorUpdateNotes("Forwarder",
            companyName, $('#forwarderCity').val(), $('#forwarderState').val(),
            $('#forwarderZip').val(), $('#forwarderCodeClient').val(), $('#forwarderAddress').val(),
            $('#forwarderFaxClient').val(), $('#forwarderPhoneClient').val(), moduleName);
}

function displayConsigneeNotes(moduleName) {
    var companyName = "";
    if ($('#consigneeNameClient').val() != "") {
        companyName = $('#consigneeNameClient').val();
    }
    else if ($('#dupConsName').val() != "") {
        companyName = $('#dupConsName').val();
    }
    addorUpdateNotes("Consignee",
            companyName, $('#consigneeCity').val(), $('#consigneeState').val(), $('#consigneeZip').val(),
            $('#consigneeCodeClient').val(), $('#consigneeAddress').val(), $('#consigneeFaxClient').val(),
            $('#consigneePhoneClient').val(), moduleName);
}
function displayClientDetails() {//set ToolTip for Client Section in Export Booking and Quote
    setContactDojoForQuery($('#client_no').val(), '#contactName');
    var contactName = "";
    if ($('#client').val() !== "") {
        contactName = $('#client').val();
    } else if ($('#clientCons').val() !== "") {
        contactName = $('#clientCons').val();
    } else {
        contactName = $('#ManualClient').val();
    }
    var acctNo = $('#client_no').val();
    var address = $('#address').val();
    var city = $('#city').val();
    var state = $('#state').val();
    var zip = $('#zip').val();
    var fmcNo = $('#fmcNo').val();
    var otiNo = $('#otiNumber').val();
    var bsShipFwNo = $('#clientBsEciAcctNo').val();
    var bsConsNo = $('#clientBsEciFwNo').val();
    var coloadCommodity = $('#clientColoadNo').val().trim() === "000000" ? "" : $('#clientColoadNo').val() + "  " + $('#clientColoadDesc').val();
    var retailCommodity = $('#clientRetailNo').val() === "000000" ? "" : $('#clientRetailNo').val() + "  " + $('#clientRetailDesc').val();
    var clientToolTip = setToolTipDesignforLclExp("Client Details", contactName, acctNo,
            address, city, state, zip, fmcNo, otiNo, coloadCommodity, retailCommodity, bsShipFwNo, bsConsNo);
    JToolTip('#clientT', clientToolTip, 400);
}

function setToolTipDesignforLclExp(headingName, acctName, acctNo, address, city, state,
        zip, fmcNo, otiNo, commodityNo, retailCommodity, bsShipFwNo, bsConsNo) {
    var toolTip = "<table><tr>";
    toolTip += "<td colspan='2' class='textBoldforlcl' align='left'><FONT size='2' COLOR=#008000><b>" + headingName + "</b></FONT></td></tr>";
    toolTip += "<tr><td class='textlabelsBoldforlcl'><FONT COLOR=red>ACCOUNT NAME:</FONT> </td><td>" + acctName + "</td></tr>";
    toolTip += "<tr><td class='textlabelsBoldforlcl'><FONT COLOR=red>ACCOUNT NO:</FONT> </td><td>" + acctNo + "</td></tr>";
    toolTip += "<tr><td class='textlabelsBoldforlcl'><FONT COLOR=red>ADDRESS:</FONT> </td><td>" + address + "</td></tr>";
    toolTip += "<tr><td class='textlabelsBoldforlcl'><FONT COLOR=red>CITY:</FONT> </td><td>" + city + "</td></tr>";
    toolTip += "<tr><td class='textlabelsBoldforlcl'><FONT COLOR=red>STATE:</FONT> </td><td>" + state + "</td></tr>";
    toolTip += "<tr><td class='textlabelsBoldforlcl'><FONT COLOR=red>ZIP:</FONT> </td><td>" + zip + "</td></tr>";
    toolTip += "<tr><td class='textlabelsBoldforlcl'><FONT COLOR=red>FMC:</FONT> </td><td>" + fmcNo + "</td></tr>";
    toolTip += "<tr><td class='textlabelsBoldforlcl'><FONT COLOR=red>OTI NUMBER:</FONT> </td><td>" + otiNo + "</td></tr>";
    toolTip += "<tr><td class='textlabelsBoldforlcl'><FONT COLOR=red>COLOAD COMMODITY:</FONT> </td><td>" + commodityNo + "</td></tr>";
    toolTip += "<tr><td class='textlabelsBoldforlcl'><FONT COLOR=red>RETAIL COMMODITY:</FONT> </td><td>" + retailCommodity + "</td></tr>";
    toolTip += "<tr><td class='textlabelsBoldforlcl'><FONT COLOR=red>Blue Scr Shipper/FF:</FONT> </td><td>" + bsShipFwNo + "</td></tr>";
    toolTip += "<tr><td class='textlabelsBoldforlcl'><FONT COLOR=red>Blue Scr Consignee:</FONT> </td><td>" + bsConsNo + "</td></tr>";
    toolTip += "</table>";
    return toolTip;
}
function displayShipperDetails() {//set ToolTip for Shipper Section in Export Booking and Quote
    setContactDojoForQuery($('#shipperCodeClient').val(), '#shipperContactClient');
    var shipperName = "";
    if ($('#shipperNameClient').val !== "") {
        shipperName = $('#shipperNameClient').val();
    } else if ($('#dupShipName').val !== "") {
        shipperName = $('#dupShipName').val();
    }
    var acctNo = $('#shipperCodeClient').val();
    var address = $('#shipperAddress').val();
    var city = $('#shipperCity').val();
    var state = $('#shipperState').val();
    var zip = $('#shipperZip').val();
    var fmcNo = $('#sFmc').val();
    var otiNo = $('#sotiNumber').val();
    var bsShipFwNo = $('#shipBsEciAcctNo').val();
    var bsConsNo = $('#shipBsEciFwNo').val();
    var coloadCommodity = $('#shipColoadNo').val() === "000000" ? "" : $('#shipColoadNo').val() + "  " + $('#shipColoadDesc').val();
    var retailCommodity = $('#shipRetailNo').val() === "000000" ? "" : $('#shipRetailNo').val() + "  " + $('#shipRetailDesc').val();
    var shipperToolTip = setToolTipDesignforLclExp("Shipper Details", shipperName, acctNo, address, city,
            state, zip, fmcNo, otiNo, coloadCommodity, retailCommodity, bsShipFwNo, bsConsNo);
    JToolTip('#shipp', shipperToolTip, 400);
}
function displayConsigneeDetails() {//set ToolTip for Consignee Section in Export Booking and Quote
    setContactDojoForQuery($('#consigneeCodeClient').val(), '#consigneeContactName');
    var consigneeName = "";
    if ($('#consigneeNameClient').val() !== "") {
        consigneeName = $('#consigneeNameClient').val();
    }
    else if ($('#dupConsName').val() !== "") {
        consigneeName = $('#dupConsName').val();
    }
    var acctNo = $('#consigneeCodeClient').val();
    var address = $('#consigneeAddress').val();
    var city = $('#consigneeCity').val();
    var state = $('#consigneeState').val();
    var zip = $('#consigneeZip').val();
    var fmcNo = $('#cFmc').val();
    var otiNo = $('#cotiNumber').val();
    var bsShipFwNo = $('#consBsEciAcctNo').val();
    var bsConsNo = $('#consBsEciFwNo').val();
    var coloadCommodity = $('#consColoadNo').val() === "000000" ? "" : $('#consColoadNo').val() + "  " + $('#consColoadDesc').val();
    var retailCommodity = $('#consRetailNo').val() === "000000" ? "" : $('#consRetailNo').val() + "  " + $('#consRetailDesc').val();
    var consigneeToolTip = setToolTipDesignforLclExp("Consignee Details", consigneeName, acctNo, address, city,
            state, zip, fmcNo, otiNo, coloadCommodity, retailCommodity, bsShipFwNo, bsConsNo);
    JToolTip('#ship', consigneeToolTip, 400);
}
function displayForwarderDetails() {//set ToolTip for Forwarder Section in Export Booking and Quote
    setContactDojoForQuery($('#forwarderCodeClient').val(), '#forwardercontactClient');
    var acctName = $('#forwarderNameClient').val();
    var acctNo = $('#forwarderCodeClient').val();
    var address = $('#forwarderAddress').val();
    var city = $('#forwarderCity').val();
    var state = $('#forwarderState').val();
    var zip = $('#forwarderZip').val();
    var fmcNo = $('#fFmc').val();
    var otiNo = $('#fotiNumber').val();
    var bsShipFwdNo = $('#fwdBsEciAcctNo').val();
    var bsConsNo = $('#fwdBsEciFwNo').val();
    var coloadCommodity = $('#fwdColoadNo').val() === "000000" ? "" : $('#fwdColoadNo').val() + "  " + $('#fwdColoadDesc').val();
    var retailCommodity = $('#fwdRetailNo').val() === "000000" ? "" : $('#fwdRetailNo').val() + "  " + $('#fwdRetailDesc').val();
    var forwarderToolTip = setToolTipDesignforLclExp("Forwarder Details", acctName, acctNo, address,
            city, state, zip, fmcNo, otiNo, coloadCommodity, retailCommodity, bsShipFwdNo, bsConsNo);
    JToolTip('#Fwd', forwarderToolTip, 400);
}

function setContactDojoForQuery(acctNo, idValue) {
    if (acctNo !== "") {
        $(idValue).attr('alt', 'CONTACTFORACCT');
    }
}


function verifyContactDetails(refName) {
    if (refName == 'frwd') {
        if ($('#consigneeContactName').val().trim() != "" || $('#consigneeManualContact').val().trim() != "") {
            verifyConsDetails('');
        } else if ($('#shipperContactClient').val().trim() != "" || $('#shipperManualContact').val().trim() != "") {
            verifyshipDetails('');
        } else if ($('#contactName').val().trim() != "" || $('#clientContactManul').val().trim() != "") {
            verifyClientDetails('');
        } else {
            verifyFrwdDetails(refName);
        }
    }
    if (refName == 'consignee') {
        if ($('#forwardercontactClient').val().trim() != "" || $('#forwarederContactManual').val().trim() != "") {
            verifyFrwdDetails('');
        } else if ($('#shipperContactClient').val().trim() != "" || $('#shipperManualContact').val().trim() != "") {
            verifyshipDetails('');
        } else if ($('#contactName').val().trim() != "" || $('#clientContactManul').val().trim() != "") {
            verifyClientDetails('');
        } else {
            verifyConsDetails(refName);
        }
    }
    if (refName == 'shipper') {
        if ($('#forwardercontactClient').val().trim() != "" || $('#forwarederContactManual').val().trim() != "") {
            verifyFrwdDetails('');
        } else if ($('#consigneeContactName').val().trim() != "" || $('#consigneeManualContact').val().trim() != "") {
            verifyConsDetails('');
        } else if ($('#contactName').val().trim() != "" || $('#clientContactManul').val().trim() != "") {
            verifyClientDetails('');
        } else {
            verifyshipDetails(refName);
        }
    }
    if (refName == 'client') {
        if ($('#forwardercontactClient').val().trim() != "" || $('#forwarederContactManual').val().trim() != "") {
            verifyFrwdDetails('');
        } else if ($('#shipperContactClient').val().trim() != "" || $('#shipperManualContact').val().trim() != "") {
            verifyshipDetails('');
        } else if ($('#consigneeContactName').val().trim() != "" || $('#consigneeManualContact').val().trim() != "") {
            verifyConsDetails('');
        } else {
            verifyClientDetails(refName);
        }
    }
}

function verifyFrwdDetails(partyName) {
    if (partyName != 'frwd') {
        $.prompt('Only one Booking Contact is allowed, Please remove the existing one and try.');
        $('#newClientContact').attr('checked', false);
        $('#newShipperContact').attr('checked', false);
        $('#newConsigneeContact').attr('checked', false);
    }
    $('#contactName').val('')
    $('#email').val('');
    $('#clientContactManul').val('')
    $('#consigneeManualContact').val('')
    $('#consigneeContactName').val('')
    $('#consigneeEmailClient').val('');
    $('#shipperEmailClient').val('');
    $('#shipperContactClient').val('')
    $('#shipperManualContact').val('')

    if ($('#newForwarderContact').is(":checked")) {
        $('#forwarderFaxClient').val('');
    } else {
        $('#forwarderFaxClient').val($('#dupFwdFax').val());
    }
    if ($('#newForwarderContact').is(":checked")) {
        $('#forwarderPhoneClient').val('');
    } else {
        $('#forwarderPhoneClient').val($('#dupFwdPhone').val());
    }
}

function verifyConsDetails(partyName) {
    if (partyName != 'consignee') {
        $.prompt('Only one Booking Contact is allowed, Please remove the existing one and try.');
        $('#newClientContact').attr('checked', false);
        $('#newShipperContact').attr('checked', false);
        $('#newForwarderContact').attr('checked', false);
    }
    $('#shipperContactClient').val('')
    $('#shipperManualContact').val('')
    $('#shipperEmailClient').val('');
    $('#email').val('');
    $('#contactName').val('')
    $('#clientContactManul').val('')
    $('#forwarederContactManual').val('')
    $('#forwardercontactClient').val('')
    $('#forwarderEmailClient').val('');

    if ($('#newConsigneeContact').is(":checked")) {
        $('#consigneeFaxClient').val('');
    } else {
        $('#consigneeFaxClient').val($('#dupConsFax').val());
    }
    if ($('#newConsigneeContact').is(":checked")) {
        $('#consigneePhoneClient').val('');
    } else {
        $('#consigneePhoneClient').val($('#dupConsPhone').val());
    }
}

function verifyClientDetails(partyName) {
    if (partyName != 'client') {
        $.prompt('Only one Booking Contact is allowed, Please remove the existing one and try.');
        $('#newShipperContact').attr('checked', false);
        $('#newForwarderContact').attr('checked', false);
        $('#newConsigneeContact').attr('checked', false);
    }
    $('#forwarederContactManual').val('')
    $('#forwardercontactClient').val('')
    $('#forwarderEmailClient').val('');
    $('#shipperManualContact').val('')
    $('#shipperContactClient').val('')
    $('#shipperEmailClient').val('');
    $('#consigneeEmailClient').val('');
    $('#consigneeContactName').val('');
    $('#consigneeManualContact').val('');

    if ($('#newClientContact').is(":checked")) {
        $('#fax').val('');
    } else {
        $('#fax').val($('#dupFax').val());
    }
    if ($('#newClientContact').is(":checked")) {
        $('#phone').val('');
    } else {
        $('#phone').val($('#dupPhone').val());
    }
}

function verifyshipDetails(partyName) {
    if (partyName != 'shipper') {
        $.prompt('Only one Booking Contact is allowed, Please remove the existing one and try.');
        $('#newClientContact').attr('checked', false);
        $('#newForwarderContact').attr('checked', false);
        $('#newConsigneeContact').attr('checked', false);
    }
    $('#email').val('');
    $('#contactName').val('')
    $('#clientContactManul').val('')
    $('#forwardercontactClient').val('')
    $('#forwarderEmailClient').val('');
    $('#forwarederContactManual').val('')
    $('#consigneeContactName').val('')
    $('#consigneeEmailClient').val('');
    $('#consigneeManualContact').val('')

    if ($('#newShipperContact').is(":checked")) {
        $('#shipperFaxClient').val('');
    } else {
        $('#shipperFaxClient').val($('#dupShipFax').val());
    }
    if ($('#newShipperContact').is(":checked")) {
        $('#shipperPhoneClient').val('');
    } else {
        $('#shipperPhoneClient').val($('#dupShipPhone').val());
    }
}

function setSpotRateDisable() {
    parent.$('input:radio[name=spotRate]').val(["N"]);
    parent.$("#lclSpotRate").hide();
    parent.$("#spotratelabel").text('');
}
function addWM() {
    var engmet = $('#engmet').val();
    var WM = $('input:radio[name=checkWM]:checked').val();
    if (engmet == "E") {
        document.getElementById('wei').innerHTML = "/100 LBS";
        document.getElementById('msr').innerHTML = "/CFT"
    } else {
        document.getElementById('wei').innerHTML = "/1000 KGS";
        document.getElementById('msr').innerHTML = "/CBM"
    }
}

function checkBottom() {
    document.getElementById('spotCheckOF').checked = !document.getElementById('spotCheckBottom').checked;

}
function checkOcnFrt() {
    document.getElementById('spotCheckBottom').checked = !document.getElementById('spotCheckOF').checked;

}
function checkForNumberAndDecimal(obj) {
    var result;
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        sampleAlert("This field should be Numeric");
    }
}
function getBookingContact() {
    if ($('#contactName').val() != null && $('#contactName').val() != "") {
        $('#bookingContact').val($('#contactName').val());
    } else if ($('#shipperContactClient').val() != null && $('#shipperContactClient').val() != "") {
        $('#bookingContact').val($('#shipperContactClient').val());
    } else if ($('#forwardercontactClient').val() != null && $('#forwardercontactClient').val() != "") {
        $('#bookingContact').val($('#forwardercontactClient').val());
    } else if ($('#consigneeContactName').val() != null && $('#consigneeContactName').val() != "") {
        $('#bookingContact').val($('#consigneeContactName').val());
    }

    if ($('#email').val() != null && $('#email').val() != "") {
        $('#bookingContactEmail').val($('#email').val());
    } else if ($('#shipperEmailClient').val() != null && $('#shipperEmailClient').val() != "") {
        $('#bookingContactEmail').val($('#shipperEmailClient').val());
    } else if ($('#forwarderEmailClient').val() != null && $('#forwarderEmailClient').val() != "") {
        $('#bookingContactEmail').val($('#forwarderEmailClient').val());
    } else if ($('#consigneeEmailClient').val() != null && $('#consigneeEmailClient').val() != "") {
        $('#bookingContactEmail').val($('#consigneeEmailClient').val());
    }
}

function refreshComm() {
    var spotRate = $('input:radio[name=spotRate]:checked').val();
    var datatableobj = document.getElementById("commObj");
    var comment = $("#externalComment").val();
    if (spotRate === 'Y') {
        $("#externalComment").val("Spot Rate is " + $("#spotLabel").val());
    } else {
        if (comment !== "" && comment.substr(0, 9) === 'SPOT RATE') {
            $("#externalComment").val('');
        }
    }
    if (datatableobj != null) {
        var row = datatableobj.rows.item(0);
        for (var i = 1; i <= datatableobj.rows.length - 1; i++) {
            for (var j = 0; j < row.cells.length; j++) {
                var col = row.cells.item(j);
                if (col.innerHTML.toString().trim().indexOf("O/F rate") != -1)
                {
                    if (spotRate == 'Y') {
                        if (datatableobj.rows[i].cells[j].innerHTML != null && datatableobj.rows[i].cells[j].innerHTML != '')
                        {
                            datatableobj.rows[i].cells[j].innerHTML = "";
                        }
                    }
                    else {
                        var ofrateobj = "document.getElementById('ofRate" + i + "')";
                        datatableobj.rows[i].cells[j].innerHTML = "$" + eval(ofrateobj).value;
                    }
                }//end of j loop
            }//end of i loop
        }
    }

}
function allowOnlyWholeNumbers(obj) {
    if (obj.value.match(/^-?[0-9]+(.[0-9]{1,2})?$/) == null) {
        obj.value = '';
        obj.focus();
        sampleAlert("This field should be Numeric");
        return false;
    }
    return true;
}
//import contact
function openImportContact(path, accountName, accountNo, contactName, subtype) {
    var href = path + "/lclContactDetails.do?methodName=display&vendorName=" + accountName + "&vendorNo=" + accountNo + "&contactName=" + contactName + "&vendorType=" + subtype;
    $("#contactR").attr("href", href);
    $("#contactR").colorbox({
        iframe: true,
        width: "90%",
        height: "90%",
        title: "Contact"
    });
}
function getCurrentDate(obj) {
    var m_names = new Array("Jan", "Feb", "Mar",
            "Apr", "May", "Jun", "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec");
    var d = new Date();
    var curr_date = d.getDate();
    var curr_month = d.getMonth();
    var curr_year = d.getFullYear();
    var curr_hour = d.getHours();
    var curr_min = d.getMinutes();
    obj.value = curr_date + "-" + m_names[curr_month] + "-" + curr_year + " " + curr_hour + ":" + curr_min;
}
//function shipper_AccttypeCheck(){
//    target=jQuery("#accttype").val();
//    subtype=jQuery("#subtype").val();
//    var type;
//    var subTypes;
//    var array1 = new Array();
//    var array2 = new Array();
//    if (target!= null) {
//        type = target;
//        array1 = type.split(",");
//    }
//    if ( subtype != null) {
//        subTypes = ( subtype).toLowerCase();
//        array2 = subTypes.split(",");
//    }
//    if(target!=""){
//        if(((!array1.contains('S') && target!='E' && target!='I' && array1.contains('V')) || (target=='C')) && !array2.contains('forwarder')){
//            congAlert("Please select the customers with account type S,E,I and V with subtype forwarder");
//            jQuery("#accttype").val('');
//            jQuery("#subtype").val('');
//            jQuery("#shipperNo").val('');
//            jQuery("#shipperName").val('');
//        }
//    }
//}
function setDefaultLoginName(checkBoxValue, name, id) {
    var login = $('#loginName').val();
    var userId = $('#userId').val();
    var textObject = "document.getElementById('" + name + "')";
    var hiddenObject = "document.getElementById('" + id + "')";
    if (checkBoxValue.checked) {
        eval(textObject).value = login;
        eval(hiddenObject).value = userId;
    } else {
        eval(textObject).value = "";
        eval(hiddenObject).value = "";
    }
}
function addDate(date, days) {
    var months = new Array("Jan", "Feb", "Mar",
            "Apr", "May", "Jun", "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec");
    var convertedDate = toDateFormat(date);
    var time = getTime(date);
    convertedDate.setDate(convertedDate.getDate() + days);
    var cDate = convertedDate.getDate();
    var cMonth = convertedDate.getMonth();
    var cYear = convertedDate.getFullYear();
    var formattedDate = cDate + '-' + months[cMonth] + '-' + cYear + ' ' + time;
    return $.trim(formattedDate);
}

function toDateFormat(dateString) {
    var dateTime = dateString.split(' ');
    var date = dateTime[0].split('-');
    var time = dateTime[1];
    var convertedDate;
    if (time !== undefined) {
        convertedDate = new Date(date[1] + ' ' + date[0] + ',' + date[2] + ' ' + time + ':00');
    } else {
        convertedDate = new Date(date[1] + ' ' + date[0] + ',' + date[2]);
    }
    return convertedDate;
}

function getTime(dateString) {
    var dateTime = dateString.split(' ');
    return dateTime[1] === undefined ? '' : dateTime[1] + ' ' + dateTime[2];
}

function getDifference(date) {
    var today = new Date();
    var convertedDate = toDateFormat(date);
    var diff = (today - convertedDate);
    return (Math.round(diff / (1000 * 60 * 60 * 24)));
}

function getWeekday(dateString) {
    var date = toDateFormat(dateString);
    var day = date.getDay();
    if (day === 0) { //if sunday then return previous friday
        return addDate(dateString, -2);
    } else if (day === 6) { //if saturday then return previous friday
        return addDate(dateString, -1);
    } else {
        return dateString;
    }
}
function checkAlphaNumeric(obj) {
    var result;
    if (!/[*|\":<>[\]{}`\\()';@&$#]/.test(obj.value)) {
        obj.value = "";
        $('#scac').html('<span style="color:red; font-size:12px">This should be AlphaNumeric</span>')
        $("#scac").css("border-color", "red");
        $("#warning").parent.show();
        return false;
    }
    else {
        $('#refusedValidate').html("");

    }
}
function openReplicateBkg(path, accountName, accountNo) {
    var address = document.getElementById('address').value;
    var city = document.getElementById('city').value;
    var state = document.getElementById('state').value;
    var zip = document.getElementById('zip').value;
    var href = path + "/lclClientDetails.do?methodName=display&accountName=" + accountName + "&accountNo=" + accountNo + "&address=" + address + "&city=" + city + "&state=" + state + "&zip=" + zip;
    $(".replicateBkgClient").attr("href", href);
    $(".replicateBkgClient").colorbox({
        iframe: true,
        width: "90%",
        height: "90%",
        title: "Client"
    });
}

function get_priority_value(v1, v2, v3, v4) {
    var value = v1 !== '' ? v1 : v2 !== '' ? v2 : v3 !== '' ? v3 : v4 !== '' ? v4 : '';
    return value;
}

function omit_Null_Data(val) {
    var data = val.trim() !== '000000' && val.trim() !== '' && val !== undefined ? val : '';
    return data;
}

function omit_undefined_data(val) {
    var data = val !== '' && val !== undefined ? val : '';
    return data;
}

function get_previous_commodity(commodity_no) {
    var flag = false;
    $(".commodity_no").each(function () {
        if ($(this).val() === commodity_no) {
            flag = true;
        }
    });
    return flag;
}

function is_commodity_available() {
    var flag = false;
    $(".commodity_no").each(function () {
        flag = true;
    });
    return flag;
}
function isValidateSpotRate() {
    var spotRate = $('input:radio[name=spotRate]:checked').val();
    if (spotRate === 'Y' && $("#fileNumberId").val() != '') {
        //$.prompt('Cannot change commodity as Spot Rate is set to Yes');
        return false;
    }
    return true;
}
function checkRateTypeDetails(acctNo, acctType) { // using for Export to check RateType

    var previousRateType = omit_undefined_data($('input:radio[name=rateType]:checked').val());
    var account_name = '';
    var retail = "", coload = "";
    var isClientRetail = '', isShipRetail = '', isFwdRetail = '', isConsRetail = '';
    var isClientCoload = '', isShipCoload = '', isFwdCoload = '', isConsCoload = '';
    var retailCommNo = '', coloadCommNo = '', commodity_no = '';

    // First fetching  the valid data in from all customer with separation on coload and retail.
    var client_Retail_No = omit_Null_Data($('#clientRetailNo').val());
    var ship_Retail_No = omit_Null_Data($('#shipRetailNo').val());
    var fwd_Retail_No = omit_Null_Data($('#fwdRetailNo').val());
    var cons_Retail_No = omit_Null_Data($('#consRetailNo').val());

    var client_Coload_No = omit_Null_Data($('#clientColoadNo').val());
    var ship_Coload_No = omit_Null_Data($('#shipColoadNo').val());
    var fwd_Coload_No = omit_Null_Data($('#fwdColoadNo').val())
    var cons_Coload_No = omit_Null_Data($('#consColoadNo').val());

    // second given unique name for contain retail and coload commodity.
    isClientRetail = client_Retail_No !== '' ? 'CR' : '';
    isShipRetail = ship_Retail_No !== '' ? 'SR' : '';
    isFwdRetail = fwd_Retail_No !== '' ? 'FR' : '';
    isConsRetail = cons_Retail_No !== '' ? 'COR' : '';

    isClientCoload = client_Coload_No !== '' ? 'CC' : '';
    isShipCoload = ship_Coload_No !== '' ? 'SC' : '';
    isFwdCoload = fwd_Coload_No !== '' ? 'FC' : '';
    isConsCoload = cons_Coload_No !== '' ? 'COC' : '';

    // checking  the priority of retail and coload.
    retail = isClientRetail !== '' ? isClientRetail : isFwdRetail !== '' ? isFwdRetail :
            isShipRetail !== '' ? isShipRetail : isConsRetail !== '' ? isConsRetail : 'NoRetail';

    coload = isClientCoload !== '' ? isClientCoload : isFwdCoload !== '' ? isFwdCoload :
            isShipCoload !== '' ? isShipCoload : isConsCoload !== '' ? isConsCoload : 'NoCoload';

    // checking  the priority commodity no of retail and coload.
    retailCommNo = get_priority_value(client_Retail_No, fwd_Retail_No, ship_Retail_No, cons_Retail_No);
    coloadCommNo = get_priority_value(client_Coload_No, fwd_Coload_No, ship_Coload_No, cons_Coload_No);

    // condition check for to assign the rate type.
    var flag = false;
    if (isValidateSpotRate()) {
        if ((retail === 'CR' && coload === 'CC') || (retail === 'FR' && coload === 'FC')
                || (retail === 'SR' && coload === 'SC') || (retail === 'COR' && coload === 'COC')) {
            showWaringMessage(acctType, retail, coload, retailCommNo, coloadCommNo, previousRateType);
            flag = true;
        } else if (retail === 'CR' || (coload === 'CC' && coload !== 'SC' && coload !== 'FC' && coload !== 'COC')) {
            $(retail === 'CR' ? "#rateR" : "#ctc").attr("checked", true);
            account_name = 'Client';
        } else if (retail === 'FR' || (coload === 'FC' && coload !== 'CC' && coload !== 'SC' && coload !== 'COC')) {
            $(retail === 'FR' ? "#rateR" : "#ctc").attr("checked", true);
            account_name = 'Forwarder';
        } else if (retail === 'SR' || (coload === 'SC' && coload !== 'CC' && coload !== 'FC' && coload !== 'COC')) {
            $(retail === 'SR' ? "#rateR" : "#ctc").attr("checked", true);
            account_name = 'Shipper';
        } else if (retail === 'COR' || (coload === 'COC' && coload !== 'CC' && coload !== 'SC' && coload !== 'FC')) {
            $(retail === 'COR' ? "#rateR" : "#ctc").attr("checked", true);
            account_name = 'Consignee';
        }
        var ratetype = omit_undefined_data($('input:radio[name=rateType]:checked').val());
        var txtmsg = ratetype === 'R' ? "has rate type set up as Retail. Current selection will change to Retail."
                : "has rate type set up as Coload. Current selection will change to Coload.";
        commodity_no = ratetype === 'C' ? coloadCommNo : retailCommNo;
        txtmsg = account_name + " " + txtmsg;
        var file_flag = $("#fileNumberId").val() !== '' ? true : false;
        if (previousRateType !== ratetype && ratetype !== '' && previousRateType !== '' && is_commodity_available()) {
            $.prompt(txtmsg, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1 && file_flag) {
                        calculates_Rates_When_Select_Customer('AutoRates will be removed.Manual Rates will be recalculated.Are you sure you want to delete?', commodity_no);
                    } else {
                        $.prompt.close();
                        if (file_flag) {
                            $(previousRateType === 'R' ? "#rateR" : "#ctc").attr("checked", true);
                        }
                    }
                }
            });
        } else if ($("#fileNumberId").val() !== '' && is_commodity_available()
                && !flag && commodity_no !== '' && !get_previous_commodity(commodity_no)) {
            calculates_Rates_When_Select_Customer('AutoRates will be removed.Manual Rates will be recalculated.Are you sure you want to delete?', commodity_no);
        }
    }
}

function showWaringMessage(acctType, retail, coload, retailCommNo, coloadCommNo, previousRateType) {
    var coloadDesc = '', retailDesc = '', acctNo = '';
    if (retail === 'CR' && coload === 'CC' && acctType === 'CLN') {
        coloadDesc = coloadCommNo + " " + $('#clientColoadDesc').val();
        retailDesc = retailCommNo + " " + $('#clientRetailDesc').val();
        acctNo = $("#client_no").val();
    }
    if (retail === 'SR' && coload === 'SC' && acctType === 'S') {
        coloadDesc = coloadCommNo + " " + $('#shipColoadDesc').val();
        retailDesc = retailCommNo + " " + $('#shipRetailDesc').val();
        acctNo = $('#shipperCodeClient').val();
    }
    if (retail === 'FR' && coload === 'FC' && acctType === 'F') {
        coloadDesc = coloadCommNo + " " + $('#fwdColoadDesc').val();
        retailDesc = retailCommNo + " " + $('#fwdRetailDesc').val();
        acctNo = $('#forwarderCodeClient').val();
    }
    if (retail === 'COR' && coload === 'COC' && acctType === 'C') {
        coloadDesc = coloadCommNo + " " + $('#consColoadDesc').val();
        retailDesc = retailCommNo + " " + $('#consRetailDesc').val();
        acctNo = $('#consigneeCodeClient').val();
    }
    if (retailDesc !== '' && coloadDesc !== '') {
        $("#ctc").attr("checked", false);
        $("#rateR").attr("checked", false);
        var txtMsg = get_message(acctNo, retailDesc, coloadDesc, coloadCommNo, retailCommNo);
        $.prompt(txtMsg, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    var get_value = $('input:radio[name=common_rateType]:checked').val();
                    var get_selected_rateType = omit_undefined_data($('input:radio[name=common_rateType]:checked').attr('id'));
                    if (get_selected_rateType === '') {
                        showWaringMessage(acctType, retail, coload, retailCommNo, coloadCommNo, previousRateType);
                        $(".retailCoload").css("outline", "3px solid red");
                    } else {
                        submit_rate_type(previousRateType, get_selected_rateType, get_value);
                    }
                } else {
                    if ($("#fileNumberId").val() !== '') {
                        $.prompt.close();
                        $(previousRateType === 'R' ? "#rateR" : "#ctc").attr("checked", true);
                    }
                }
            }
        });
    }
}

function get_message(acctNo, retailDesc, coloadDesc, coloadCommNo, retailCommNo) {
    var txtMsg = "Account No <b style='color:red'>" + acctNo + "</b> has both Coload (CTC) and  Retail Commodities <br/> Coload : <b style='color:red'>" +
            coloadDesc + "</b><br/>Retail : <b style='color:red'>" + retailDesc + "</b><br/>" +
            "<input type='radio' name='common_rateType'  class='retailCoload' value=" + coloadCommNo + " id='C'/>Coload" +
            "<input type='radio' name='common_rateType' class='retailCoload' value=" + retailCommNo + " id='R'/>Retail";
    return txtMsg;
}
function submit_rate_type(previousRateType, get_selected_rateType, commodity_no) {
    if ($("#fileNumberId").val() !== '') {
        if (is_commodity_available() && (previousRateType !== get_selected_rateType || !get_previous_commodity(commodity_no))) {
            calculates_Rates_When_Select_Customer('AutoRates will be removed.Manual Rates will be recalculated.Are you sure you want to delete?', commodity_no);
            $(get_selected_rateType === 'C' ? "#ctc" : "#rateR").attr("checked", true);
        } else {
            $(previousRateType === 'C' ? "#ctc" : "#rateR").attr("checked", true);
        }
    } else {
        $(get_selected_rateType === 'C' ? "#ctc" : "#rateR").attr("checked", true);
    }
}

function closeHsCodeBox(ele) {
    $("#" + ele).hide();
}

function updateEconoOrEculine(businessUnit, fileId, userId, remarksType) {
    showProgressBar();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "updateEconoOREculine",
            param1: businessUnit,
            param2: fileId,
            param3: userId,
            param4: remarksType,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            hideProgressBar();
        }
    });
}

function getPreviousBusinessUnit(currentData, fileId) {
    var flag = false;
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO",
            methodName: "getBusinessUnit",
            param1: fileId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (null !== data && data !== currentData) {
                flag = true;
            }
            hideProgressBar();
        }
    });
    return flag;
}

function  getTradingPartnerPriority() {
    var agentBrand = $("#agentBrand").val();
    agentBrand = agentBrand === "ECU WW" ? "Ecu Worldwide" : agentBrand === "Econo" ? "Econo" : agentBrand;
    var clientBrand = $("#clientBrand").val();
    var shipperBrand = $("#shipperBrand").val();
    var forwarderBrand = $("#fwdBrand").val();
    var brand = agentBrand !== "" && agentBrand !== "None" ? agentBrand : clientBrand !== ""
            && clientBrand !== "None" ? clientBrand : forwarderBrand !== ""
            && forwarderBrand !== "None" ? forwarderBrand : shipperBrand !== ""
            && shipperBrand !== "None" ? shipperBrand : "None";
    return brand;
}

function limitText(field, max) {
    if (((event.keyCode === 86 || 17 || 67) && (field.value.length > max - 1)) || (field.value.length > max - 1)) {
        field.value = field.value.substr(0, max);
        return false;
    }
}

function notAllowSpecialChar(obj) {
    if (!/^[a-zA-Z0-9]*$/.test(obj.value)) {
        obj.value = "";
        sampleAlert("This field should be Alphanumeric");
    }
}

function  gotoExportDrFromImpDrScreen(path, fileId, module) {
    var filter = "", fromScreen = "", toScreen = "";
    if (module === "Imports") {
        filter = "LCL_IMP_DR";
        fromScreen = "ExportDrScreen";
        toScreen = "EXPORT_TO_IMPORT";
        window.parent.navigateExportImportScreen(fileId, filter, 0, 0, 0, fromScreen, toScreen, "");
    } else {
        fromScreen = "ImportDrScreen";
        toScreen = "IMPORT_TO_EXPORT";
        window.parent.changeLclChilds(path, fileId, $("#fileType").val(), "Imports", "LCLI DR");
    }

}

function setEnableDisableCost(flag, shipmentType) {
    var chargeCode = $("#chargesCode option:selected").val();
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "chargeCostValidate",
            param1: chargeCode,
            param2: shipmentType,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data[0] === 'N' && ($("#flatRateAmount").val() !== "" && $("#flatRateAmount").val() !== "0.00" ||
                    $("#measure").val() !== "" && $("#measure").val() !== "0.00")) {
                $.prompt('No Gl Mapping found for ChargeCode --->' + chargeCode);
                $('#flatRateAmount').val('');
                $('#measure').val('');
                $('#weight').val('');
                flag = false;
            } else if (data[1] === 'N' && ($("#costAmount").val() !== "" && $("#costAmount").val() !== "0.00" ||
                    $("#measureForCost").val() !== "" && $("#measureForCost").val() !== "0.00")) {
                $.prompt('No Gl Mapping found for CostCode --->' + chargeCode);
                $('#costAmount').val('');
                $('#measureForCost').val('');
                $('#weightForCost').val('');
                flag = false;
            }
        }
    });
    return flag;
}

function validateDate(obj) {
    if (Date.parse(obj.value) < Date.parse(Date())) {
        obj.value = "";
        $.prompt("FollowUp Date should be greater than Today's Date");
    }
}

function isDestinationCharge(code) {
    var flag = true;
    if (code === "ONCARR" || code === "DELIV" || code === "DTHC PREPAID"
            || code === "DAP" || code === "DDP") {
        flag = false;
    }
    return flag;
}

function getVoyageOriginId(scheduleNo) {
    var origin = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO",
            methodName: "getPickedOrBookedVoyOriginID",
            param1: scheduleNo,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            origin = data;
        }
    });
    return origin;
}
function getCurrentTime() {
    var currentTime = "";
    var a_p = "";
    var d = new Date();
    var curr_hour = d.getHours();
    if (curr_hour < 12) {
        a_p = "AM";
    } else {
        a_p = "PM";
    }
    if (curr_hour == 0) {
        curr_hour = 12;
    }
    if (curr_hour > 12) {
        curr_hour = curr_hour - 12;
    }

    var curr_min = d.getMinutes();
    curr_min = curr_min + "";

    if (curr_min.length == 1) {
        curr_min = "0" + curr_min;
    }

    currentTime = curr_hour + ":" + curr_min + " " + a_p;
    return currentTime;
}

// mantis item: 14601
function isLockPort(FileState) {
    var flag = true;
    var podUnlocCode = $('#unlocationCode').val();
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.LCLPortConfigurationDAO",
            methodName: "getLockport",
            param1: podUnlocCode,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data === true) {
                $.prompt("This market is currently locked. Quotes and Bookings are not allowed");
                $("#finalDestinationR").val('');
                $("#finalDestinationId").val('');
                $("#unlocationCode").val('');
                $("#unlocationName").val('');
                $("#portOfDestination").val('');
                $("#foreignDischargeId").val('');
                $("#podUnlocationcode").val('');
                $('#agentName').val('');
                $('#agentNumber').val('');
                $('#agentBrand').val('');
                if (FileState == 'Booking') {
                    submitAjaxFormforRates('displayemptyVoyage', '#lclBookingForm', '#upcomingSailing', '');
                } else {
                    submitAjaxFormforRates('displayemptyVoyage', '#lclQuoteForm', '#upcomingSailing', '');
                }
                flag = false;
            }
        }
    });
    return flag;
}

