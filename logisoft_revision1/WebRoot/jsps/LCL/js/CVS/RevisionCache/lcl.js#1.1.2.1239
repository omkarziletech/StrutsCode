var formChanged = false;
jQuery(document).ready(function () {
    etaFdStatus();
    acctTypeCheck();
    //forwarder_AccttypeCheck();
    supplierAcctCheck();
    showClient();
    //Set checkBox value Manually check r unCheck
    showConsManualCheck();
    showShipManualCheck();
    showNotifyManualCheck();
    showConsContainer();
    displayOsdBox(); //dispaly osd Box
    lockAgentInfo();
    checkNonRated('L');
    showPickupButton();
    cfclDisable();
    changePort();
    notify();
    if ($('#lockMessage').val() !== '') {
        showBookingReadOnly();
    }
    //  showShipper();
    //showConsignee();
    changerelay();
    rateVerified();
    checkHazmatBoth();
    var moduleName = $('#moduleName').val();
    if (moduleName !== 'Imports') {
        showSupplier();
        getBookingContact();
        //        if ($("#bookingType").val() === 'T' && $("#dispoId").text() === 'OBKG') {
        //            showBookingReadOnly();  // checking only for transhipment file in exports
        //        }
        var trmnum = $('#trmnum').val();
        var rateType = $('input:radio[name=rateType]:checked').val();
        if ($("#fileNumberId").val() == '' && (trmnum === '59' || (rateType !== undefined && rateType === 'C'))) {
            $('#aesByN').attr('checked', true);
        }
    } else {
        if ($("#fileNumberId").val() == '') {
            $('#clientWithConsignee').attr("checked", true);
            setConsigneeForClient();
        } else {
            if ($("#client_no").val() != "") {
                checkConsigneeAcctType();
            }
        }
    }
    if ($('#checkDRChange').val() === 'true') {
        $('#checkDRChange').val("false");
        openPrintFaxPopUp();
    }
    showTerminateBtn();
    detectFormChange();
    $("#client").change(function () {
        clearClientValues();
    });
    $("#shipperNameClient").change(function () {
        clearShipperValuesInExp();
    });
    $("#forwarderNameClient").change(function () {
        clearForwarder();
    });
    $("#consigneeNameClient").change(function () {
        clearConsigneeValuesExp();
    });
});
function detectFormChange() {
    var form = "#lclBookingForm";
    var $selector = $(form + " input[type=text], " + form + " textarea");
    $($selector).each(function () {
        $(this).data('initial_value', $(this).val());
    });
    $($selector).keyup(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            formChanged = true;
        }
    });
    $($selector).change(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            formChanged = true;
        }
    });
    $(form).bind('change paste', function () {
        formChanged = true;
    });
}

function isFormChanged() {
    var form = "#lclBookingForm";
    var $selector = $(form + " input[type=text], " + form + " textarea");
    $($selector).each(function () {
        if ($(this).val() !== $(this).data('initial_value')) {
            formChanged = true;
        }
    });
    return formChanged;
}
function acctTypeCheck() {
    target = jQuery("#acct_type").val();
    subtype = jQuery("#sub_type").val();
    moduleName = $('#moduleName').val();
    var type;
    var subTypes;
    var array1 = new Array();
    var array2 = new Array();
    if ($('#clientDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#clientDisableAcct').val() + "</span>");
        clearClientValues();
    } else {
        if (target != null) {
            type = target;
            array1 = type.split(",");
        }
        if (subtype != null) {
            subTypes = (subtype).toLowerCase();
            array2 = subTypes.split(",");
        }
        if (moduleName != 'Imports') {
            credit = jQuery("#creditForClient").val();
            if (credit.indexOf('-') === -1) {
                document.getElementById('clientCreditClient').innerHTML = credit;
            }
            var client = credit.substring(0, credit.indexOf('-'));
            if (client != "") {
                document.getElementById('clientCreditClient').innerHTML = client;
            }
            if (client == 'Suspended/See Accounting' || client == 'No Credit') {
                document.getElementById('clientCreditClient').className = "red";
            } else {
                document.getElementById('clientCreditClient').className = "green";
            }
        }
        if (target != "") {
            if ((!array1.contains('S') && target != 'C' && array1.contains('V') && target != 'O') && !array2.contains('forwarder')) {
                $.prompt("Please select the customers with account type S,O,C and V with subtype forwarder");
                clearClientValues();
            }
        }
        poa = jQuery("#clientpoa").val();
        if (poa == 'Y') {
            $('#clientPoa').html("<span style='color:green;font-weight:bold;'>YES</span>");
        }
        if (poa == 'N') {
            $('#clientPoa').html("<span style='color:red;font-weight:bold;'>NO</span>");
        }
    }
}
function clearClientValues() {
    jQuery("#contactName").val('');
    jQuery("#email").val('');
    jQuery("#address").val('');
    jQuery("#phone").val('');
    jQuery("#fax").val('');
    jQuery("#client_no").val('');
    jQuery("#client").val('');
    jQuery("#dupClientPhone").val('');
}
function supplierAcctCheck() {
    if ($('#supplierDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#supplierDisabledAcct').val() + "</span>");
        clearSupplierFeilds();
    } else {
        poa = jQuery("#supplierPoa").val();
        if (poa == 'Y') {
            $('#warning').html("<span style='color:green;font-weight:bold;'>YES</span>");
        }
        if (poa == 'N') {
            $('#warning').html("<span style='color:red;font-weight:bold;'>NO</span>");
        }
    }
}
function clearSupplierFeilds() {
    $("#supplierName").val("");
    $("#supplierCode").val("");
    $("#supplierPhone").val("");
    $("#supplierFax").val("");
    $("#supplierEmail").val("");
    $("#supplierAddress").val("");
    $("#supplierCity").val("");
    $("#supplierCountry").val("");
    $("#supplierState").val("");
    $("#supplierZip").val("");
    $("#supplierPoa").val("");
    $("#supReference").val("");
    $("#supplierClient").val("");
    $("#newSupplierClientCity").val("");
    $("#supplierPoa").val("");
    $("#supplierPoaId").text("");
}

function clientSearch() {
    if (($('#clientSearchState').val() != "" && $('#clientSearchState').val() != null) &&
            ($('#clientSearchZip').val() != "" && $('#clientSearchZip').val() != null) &&
            ($('#clientSearchSalesCode').val() != "" && $('#clientSearchSalesCode').val() != null)) {
        if ($('#clientWithConsignee').is(":checked")) {
            $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_STATE_ZIP_SP');
        } else {
            $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_STATE_ZIP_SP');
        }
    } else {
        if (($('#clientSearchState').val() != "" && $('#clientSearchState').val() != null) &&
                ($('#clientSearchZip').val() != "" && $('#clientSearchZip').val() != null)) {
            if ($('#clientWithConsignee').is(":checked")) {
                $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_STATE_ZIP');
            } else {
                $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_STATE_ZIP');
            }
        } else if (($('#clientSearchZip').val() != "" && $('#clientSearchZip').val() != null) &&
                ($('#clientSearchSalesCode').val() != "" && $('#clientSearchSalesCode').val() != null)) {
            if ($('#clientWithConsignee').is(":checked")) {
                $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_ZIP_SP');
            } else {
                $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_ZIP_SP');
            }
        } else if (($('#clientSearchSalesCode').val() != "" && $('#clientSearchSalesCode').val() != null) &&
                ($('#clientSearchState').val() != "" && $('#clientSearchState').val() != null)) {
            if ($('#clientWithConsignee').is(":checked")) {
                $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_STATE_SP');
            } else {
                $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_STATE_SP');
            }
        } else {
            if ($('#clientSearchState').val() != "" && $('#clientSearchState').val() != null) {
                if ($('#clientWithConsignee').is(":checked")) {
                    $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_STATE');
                } else {
                    $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_STATE');
                }
            } else if ($('#clientSearchSalesCode').val() != "" && $('#clientSearchSalesCode').val() != null) {
                if ($('#clientWithConsignee').is(":checked")) {
                    $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_SP');
                } else {
                    $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_SP');
                }
            } else if ($('#clientSearchZip').val() != "" && $('#clientSearchZip').val() != null) {
                if ($('#clientWithConsignee').is(":checked")) {
                    $('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_ZIP');
                } else {
                    $('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_ZIP');
                }
            }
        }
    }
}

function forwarder_AccttypeCheck() {
    target = jQuery("#forwarder_acct_type").val();
    sub_type = jQuery("#forwarder_sub_type").val();
    moduleName = $('#moduleName').val();
    var type;
    var subTypes;
    var array1 = new Array();
    var array2 = new Array();
    if ($('#forwardDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#forwardDisabledAcct').val() + "</span>");
        clearForwarder();
        return false;
    }
    poa = jQuery("#forwarderPoa").val();
    if (target != null) {
        type = target;
        array1 = type.split(",");
    }
    if (sub_type != null) {
        subTypes = (sub_type).toLowerCase();
        array2 = subTypes.split(",");
    }

    if (moduleName != 'Imports') {
        target1 = jQuery("#forwarderaccttype").val();
        sub_type1 = jQuery("#forwardersubtype").val();
        poa1 = jQuery("#forwarderPoaClient").val();
        var array3 = new Array();
        var array4 = new Array();
        if (target1 != null) {
            type = target1;
            array3 = type.split(",");
        }
        if (sub_type1 != null) {
            subTypes = (sub_type1).toLowerCase();
            array4 = subTypes.split(",");
        }

        if (poa == 'Y' || poa1 == 'Y') {
            $('#forwarder').html("<span style='color:green;font-weight:bold;'>YES</span>");
            $('#forwarder1').html("<span style='color:green;font-weight:bold;'>YES</span>");
        }
        if (poa == 'N' || poa1 == 'N') {
            $('#forwarder').html("<span style='color:red;font-weight:bold;'>NO</span>");
            $('#forwarder1').html("<span style='color:red;font-weight:bold;'>NO</span>");
        }

        credit = jQuery("#creditForForwarder").val();
        credit1 = jQuery("#forwarderCredit").val();
        if (credit.indexOf('-') === -1 && credit1.indexOf('-') == -1) {
            document.getElementById('forwarderCreditClient').innerHTML = credit;
            document.getElementById('forwarderCredit').value = credit1;
        }
        var client1 = credit1.substring(0, credit1.indexOf('-'));
        var client = credit.substring(0, credit.indexOf('-'));
        if (client != "") {
            document.getElementById('forwarderCreditClient').innerHTML = client;
            document.getElementById('forwarderCredit').value = client;
        }
        if (client1 != "") {
            document.getElementById('forwarderCreditClient').innerHTML = client1;
            document.getElementById('forwarderCredit').value = client1;
        }
        if (client == 'Suspended/See Accounting' || client1 == 'Suspended/See Accounting' || client == 'No Credit' || client1 == 'No Credit') {
            document.getElementById('forwarderCreditClient').className = "red";
        } else {
            document.getElementById('forwarderCreditClient').className = "green";
        }
        if (target1 != "" && target1 != undefined) {
            if ((!array3.contains("V")) || (array3.contains("V") && !array4.contains('forwarder'))) {
                clearForwarder();
                return false;
            }

        }
    }
    if (target != "" && target != undefined) {
        if (!array1.contains("V") || (array1.contains("V") && !array2.contains('forwarder'))) {
            clearForwarder();
            return false;
        }
    }
    return true;
}
function clearForwarder() {
    //  congAlert("Please select the customers with account type V with subtype forwarder"); no need in dojo all entry show with V type.
    jQuery("#forwardercontactClient").val('');
    jQuery("#forwarderCode").val('');
    jQuery("#forwarderAddress").val('');
    jQuery("#forwarderCity").val('');
    jQuery("#forwarderState").val('');
    jQuery("#forwarderZip").val('');
    jQuery("#forwarderCountry").val('');
    jQuery("#forwarderPhone").val('');
    jQuery("#forwarderFax").val('');
    jQuery("#forwarderEmail").val('');
    jQuery("#forwarderName").val('');
    jQuery("#forwarderCodeClient").val('');
    jQuery("#forwarderAddressClient").val('');
    jQuery("#forwarderCityClient").val('');
    jQuery("#forwarderStateClient").val('');
    jQuery("#forwarderZipClient").val('');
    jQuery("#forwarderCountryClient").val('');
    jQuery("#forwarderPhoneClient").val('');
    jQuery("#forwarderFaxClient").val('');
    jQuery("#forwarderEmailClient").val('');
    jQuery("#forwarderNameClient").val('');
    jQuery("#dupForwarderPhone").val('');
    jQuery("#dupForwarderFax").val('');
    jQuery("#forwarderPoaClient").val('');
    jQuery("#forwarderPoa").val('');
    jQuery("#creditForForwarder").val('');
    jQuery("#forwarder1").text('');
    jQuery("#forwarderCreditClient").text('');
}
function shipDetails() {
    $('#shipperFaxClient').val($('#dupShipperFax').val());
    $('#shipperPhoneClient').val($('#dupShipperPhone').val());
    $('#shipperName').val($('#shipperNameClient').val());
    $('#dupShipperName').val($('#shipperNameClient').val());
    $('#shipperCode').val($('#shipperCodeClient').val());
    $('#shipper_acct_type').val($('#shipperaccttype').val());
    $('#shipper_sub_type').val($('#shippersubtype').val());
    $('#shipperFax').val($('#shipperFaxClient').val());
    $('#shipperCity').val($('#shipperCityClient').val());
    $('#shipperCountry').val($('#shipperCountryClient').val());
    $('#shipperState').val($('#shipperStateClient').val());
    $('#shipperZip').val($('#shipperZipClient').val());
    //  $('#shipperPoa').text($('#shipperPoaClient').text());
    $('#shipperAddress').val($('#shipperAddressClient').val().replace(/\,/g, " "));
    $('#shipperPhone').val($('#shipperPhoneClient').val());
    $('#shipperEmail').val($('#shipperEmailClient').val());
    $('#shipperCredit').val($('#creditForShipper').val());
    if ($('#radioP').attr('checked')) {
        if (($('#shipperNameClient').val() !== "" && $('#thirdpartyaccountNo').val() === "" &&
                $('#forwarderNameClient').val() === "" && $("#fileNumberId").val() !== '')) {
            document.getElementById('billS').checked = true;
            $("#billS").click();
        }
    }
    setShipperPoaCredit('shipperPoa', 'creditForShipper');
}
function ShippingDetails() {
    $('#shipperNameClient').val($('#shipperName').val());
    $('#dupShipName').val($('#shipperName').val());
    $('#shipperCodeClient').val($('#shipperCode').val());
    $('#shipperaccttype').val($('#shipper_acct_type').val());
    $('#shippersubtype').val($('#shipper_sub_type').val());
    $('#shipperFaxClient').val($('#shipperFax').val());
    $('#shipperCityClient').val($('#shipperCity').val());
    $('#shipperCountryClient').val($('#shipperCountry').val());
    $('#shipperStateClient').val($('#shipperState').val());
    $('#shipperZipClient').val($('#shipperZip').val());
    $('#shipperAddressClient').val($('#shipperAddress').val());
    $('#shipperEmailClient').val($('#shipperEmail').val());
    $('#shipperPhoneClient').val($('#shipperPhone').val());
    $('#creditForShipper').val($('#shipperCredit').val());
    // $('#shipperPoaClient').val($('#shipperPoa').val());
    if ($('#radioP').attr('checked')) {
        if (($('#shipperName').val() !== "" && $('#forwarderName').val() === ""
                && $('#thirdpartyaccountNo').val() === "" && $("#fileNumberId").val() !== '')) {
            document.getElementById('billS').checked = true;
            $("#billS").click();
        }
    }
    setShipperPoaCredit('shipperPoa', 'shipperCredit');
    displayShipperDetails();
}
function setShipperPoaCredit(poa, credit) {
    if ($('#' + poa).val() === 'Y') {
        $('#shipperPOAV').text('YES');
        $('#shipperPOAV').addClass('green');
        $('.shipperPOAV').text('YES');
        $('.shipperPOAV').addClass('green');
    } else if ($('#' + poa).val() === '') {
        $('#shipperPOAV').text('');
        $('.shipperPOAV').text('');
    } else {
        $('#shipperPOAV').text('NO');
        $('#shipperPOAV').addClass('red');
        $('.shipperPOAV').text('NO');
        $('.shipperPOAV').addClass('red');
    }
    var creditStatus = $('#' + credit).val();
    if (creditStatus === "Suspended/See Accounting" || creditStatus === 'No Credit') {
        $('#shipperCreditStatus').text(creditStatus);
        $('#shipperCreditStatus').addClass('red');
    } else {
        $('#shipperCreditStatus').text(creditStatus);
        $('#shipperCreditStatus').addClass('green');
    }
}

function shipper_AccttypeCheck(path) {
    var acctType = jQuery("#shipper_acct_type").val();
    var shipperCode = ($("#shipperCode").val());
    var array1 = new Array();
    var moduleName = $('#moduleName').val();
    if ($('#shipperDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#shipDisableAcct').val() + "</span>");
        clearShipperValues(path);
        clearShipperValuesInExp(path);
    } else {
        if (acctType != null) {
            array1 = acctType.split(",");
        }
        if (acctType != "" && !array1.contains('S')) {
            createNewConsignee(path, 'S', 'S');
            if (moduleName !== 'Imports') {
                clearShipperValuesInExp(path);
            } else {
                clearShipperValues(path);
            }
        } else if (moduleName === 'Imports') {
            $('.shipperCreditV').addClass('red');
            $('.shipperCreditV').text($('#shipperCredit').val());
            $('.shipeerScanSopV').addClass('red');
            $('.shipeerScanSopV').text($('#shipScanSop').val());
            $('.importCreditShipper').addClass('green');
            if ($('#importCreditShipper').val() === 'Y') {
                $('.importCreditShipper').text('$');
            }
        }
    }
}
function clearShipperValuesInExp(path) {
    jQuery("#shipperContactClient").val('');
    jQuery("#shipperCodeClient").val('');
    jQuery("#shipperAddressClient").val('');
    jQuery("#shipperCityClient").val('');
    jQuery("#shipperStateClient").val('');
    jQuery("#shipperZipClient").val('');
    jQuery("#shipperCountryClient").val('');
    jQuery("#shipperPhoneClient").val('');
    jQuery("#shipperFaxClient").val('');
    jQuery("#shipperEmailClient").val('');
    jQuery("#shipperNameClient").val('');
    jQuery("#shipperPoa").val('');
    jQuery("#dupShipperPhone").val('');
    jQuery("#dupShipperFax").val('');
    jQuery("#creditForShipper").val('');
    jQuery('#shipperPOAV').text('');
    jQuery("#shipperCreditStatus").text('');
    //  $('.shpNotes').attr("src", path + "/img/icons/e_contents_view.gif");
}
function clearShipperValues(path) {
    jQuery("#shipperName").val('');
    jQuery("#shipperCode").val('');
    jQuery("#shipperAddress").val('');
    jQuery("#shipperCity").val('');
    jQuery("#shipperState").val('');
    jQuery("#shipperZip").val('');
    jQuery("#shipperCountry").val('');
    jQuery("#shipperPhone").val('');
    jQuery("#shipperFax").val('');
    jQuery("#shipperEmail").val('');
    jQuery(".shipperCreditV").text('');
    jQuery(".shipeerScanSopV").text('');
    jQuery("#shipDisableAcct").val('');
    jQuery("#shipperDisabled").val('');
    jQuery("#shipperNameClient").val('');
    jQuery("#shipperCodeClient").val('');
    jQuery("#shipperPoa").val('');
    $('.shipperPOAV').val('');
    //$('.shpNotes').attr("src", path + "/img/icons/e_contents_view.gif");
}
function copyConsigneeDetails() {
    $('#consigneeNameClient').val($('#consigneeName').val());
    $('#dupConsName').val($('#consigneeName').val());
    $('#consigneeCodeClient').val($('#consigneeCode').val());
    $('#consigneeaccttype').val($('#consignee_acct_type').val());
    $('#consigneesubtype').val($('#consignee_sub_type').val());
    $('#consigneeFaxClient').val($('#consigneeFax').val());
    $('#consigneeCityClient').val($('#consigneeCity').val());
    $('#consigneeCountryClient').val($('#consigneeCountry').val());
    $('#consigneeStateClient').val($('#consigneeState').val());
    $('#consigneeZipClient').val($('#consigneeZip').val());
    $('#consigneeAddressClient').val($('#consigneeAddress').val());
    $('#consigneeEmailClient').val($('#consigneeEmail').val());
    $('#consigneePhoneClient').val($('#consigneePhone').val());
    $('#creditForConsignee').val($('#consigneeCredit').val());
    displayConsigneeDetails();
    setConsPoaCredit("consigneePoa", "consigneeCredit");
}
function setConsPoaCredit(poa, credit) {
    if ($('#' + poa).val() === 'Y') {
        $('#consigneePOAV').text('YES');
        $('#consigneePOAV').addClass('green');
        $('.consigneePoaValues').text('YES');
        $('.consigneePoaValues').addClass('green');
    } else if ($('#' + poa).val() === '') {
        $('#consigneePOAV').text('');
        $('.consigneePoaValues').text('');
    } else {
        $('#consigneePOAV').text('NO');
        $('#consigneePOAV').addClass('red');
        $('.consigneePoaValues').text('NO');
        $('.consigneePoaValues').addClass('red');
    }
    var creditStatus = $('#' + credit).val();
    if (creditStatus === "Suspended/See Accounting" || creditStatus === 'No Credit') {
        $('#consigneeCreditStatus').text(creditStatus);
        $('#consigneeCreditStatus').addClass('red');
    } else {
        $('#consigneeCreditStatus').text(creditStatus);
        $('#consigneeCreditStatus').addClass('green');
    }
}
function consDetails() {
    $('#consigneeFaxClient').val($('#dupConsigneeFax').val());
    $('#consigneePhoneClient').val($('#dupConsigneePhone').val());
    var client = $('#consigneeNameClient').val();
    var code = $('#consigneeCodeClient').val();
    var acctType = $('#consigneeaccttype').val();
    var subType = $('#consigneesubtype').val();
    var fax = $('#consigneeFaxClient').val();
    var city = $('#consigneeCityClient').val();
    var country = $('#consigneeCountryClient').val();
    var state = $('#consigneeStateClient').val();
    var zip = $('#consigneeZipClient').val();
    //var poa = document.getElementById('consigneePoaClient').innerHTML;
    var address = $('#consigneeAddressClient').val().replace(/\,/g, " ");
    var email = $('#consigneeEmailClient').val();
    var phone = $('#consigneePhoneClient').val();
    var credit = $('#creditForConsignee').val();
    $('#consigneeName').val(client);
    $('#dupConsigneeName').val(client);
    $('#consigneeCode').val(code);
    $('#consignee_acct_type').val(acctType);
    $('#consignee_sub_type').val(subType);
    $('#consigneeFax').val(fax);
    $('#consigneeCity').val(city);
    $('#consigneeCountry').val(country);
    $('#consigneeState').val(state);
    $('#consigneeZip').val(zip);
    //   document.getElementById('consigneePoa').innerHTML = poa;
    $('#consigneeAddress').val(address);
    $('#consigneePhone').val(phone);
    $('#consigneeEmail').val(email);
    $('#consigneeCredit').val(credit);
    setConsPoaCredit("consigneePoa", "creditForConsignee");
}
function consignee_AccttypeCheck(path) {
    var acctType = jQuery("#consignee_acct_type").val();
    var moduleName = $('#moduleName').val();
    var consDisabled = $('#consDisabled').val();
    if (consDisabled === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#consDisableAcct').val() + "</span>");
        clearConsigneeValues(path);
        clearConsigneeValuesExp(path);
    } else {
        if (acctType !== "" && acctType.indexOf("C") === -1) {
            if (moduleName !== 'Imports') {
                $.prompt("Please select the customers with account type C");
                clearConsigneeValuesExp(path);
            } else {
                createNewConsignee(path, 'C', 'C');
            }
        } else if (moduleName === 'Imports') {
            $('.consigneeCreditV').addClass('red');
            $('.consigneeCreditV').text($('#consigneeCredit').val());
            $('.consigneeScanSopV').addClass('red');
            $('.consigneeScanSopV').text($('#consigneeScanSop').val());
            $('.importCreditConsignee').addClass('green');
            if ($('#importCreditConsignee').val() === 'Y') {
                $('.importCreditConsignee').text('$');
            }
        }
    }
}
function clearConsigneeValuesExp(path) {
    jQuery("#consigneeContactName").val('');
    jQuery("#consigneeCodeClient").val('');
    jQuery("#dupConsName").val('');
    jQuery("#consigneeNameClient").val('');
    jQuery("#consigneeAddressClient").val('');
    jQuery("#consigneeCityClient").val('');
    jQuery("#consigneeStateClient").val('');
    jQuery("#consigneeZipClient").val('');
    jQuery("#consigneeCountryClient").val('');
    jQuery("#consigneePhoneClient").val('');
    jQuery("#consigneeFaxClient").val('');
    jQuery("#consigneeEmailClient").val('');
    jQuery("#consigneePoa").val('');
    jQuery("#consigneeCredit").val('');
    jQuery("#consigneePOAV").text('');
    $('#creditForConsignee').val('');
    $('#consBsEciAcctNo').val('');
    $('#consBsEciFwNo').val('');
    $('#dupConsigneePhone').val('');
    $('#dupConsigneeFax').val('');
    jQuery("#consigneeCreditStatus").text('');
    $('.conNotes').attr("src", path + "/img/icons/e_contents_view.gif");
}
function clearConsigneeValues(path) {
    jQuery("#consigneeName").val('');
    jQuery("#consigneeCode").val('');
    jQuery("#consigneeAddress").val('');
    jQuery("#consigneeCity").val('');
    jQuery("#consigneeState").val('');
    jQuery("#consigneeZip").val('');
    jQuery("#consigneeCountry").val('');
    jQuery("#consigneePhone").val('');
    jQuery("#consigneeFax").val('');
    jQuery("#consigneeEmail").val('');
    jQuery("#consigneeCredit").val('');
    jQuery("#consDisableAcct").val('');
    $(".consigneeCreditV").text('');
    $(".consigneeScanSopV").text('');
    $('.consigneePoaValues').text('');
    jQuery("#consigneePoa").val('');
    $('.conNotes').attr("src", path + "/img/icons/e_contents_view.gif");
}
function createNewConsignee(path, type, acctType) {
    var txt = 'This account is not a Consignee, would you like to make it one?';
    var code = "";
    var vendorName = "";
    var city = "";
    var tpFlag = "";
    if (type === 'C') {
        code = jQuery("#consigneeCode").val();
        vendorName = jQuery("#consigneeName").val();
        city = jQuery("#consigneeCity").val();
        tpFlag = 'LCL_IMPORT_CONSIGNEE';
    } else if (type === 'S') {
        txt = 'This account is not a Shipper, would you like to make it one?';
        code = jQuery("#shipperCode").val();
        vendorName = jQuery("#shipperName").val();
        city = jQuery("#shipperCity").val();
        tpFlag = 'LCL_IMPORT_SHIPPER';
    } else if (type === 'N') {
        code = jQuery("#notifyCode").val();
        vendorName = jQuery("#notifyName").val();
        city = jQuery("#notifyCity").val();
        tpFlag = 'LCL_IMPORT_NOTIFY';
    } else if (type === 'N2') {
        code = jQuery("#notify2Code").val();
        vendorName = jQuery("#notify2Name").val();
        city = jQuery("#notify2City").val();
        tpFlag = 'LCL_IMPORT_NOTIFY2';
    }
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                        methodName: "existingImpCustomerList",
                        forward: "/jsps/Tradingpartnermaintainance/tradingPartnerExistsCustomerList.jsp",
                        param1: vendorName,
                        param2: city
                    },
                    preloading: true,
                    success: function (data) {
                        if (null !== data && data !== "") {
                            showPopUp();
                            createHTMLElement("div", "docListDiv", "60%", "50%", document.body);
                            $("#docListDiv").html(data);
                            $('#tpFlag').val(tpFlag);
                            $('#tpacctNo').val(code);
                        } else {
                            createConsigneeAcctShipperAcct(code, type, acctType)
                        }
                    }
                });
            } else {
                if (type === 'C') {
                    clearConsigneeValues(path);
                } else if (type === 'N') {
                    clearNotifyDetails(path);
                } else if (type === 'N2') {
                    clearNotify2Values(path);
                } else if (type === 'S') {
                    clearShipperValues(path);
                }
                $.prompt.close();
            }
        }
    });
}
function createConsigneeAcctShipperAcct(code, type, acctType) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "createConsigneeAcctShipperAcct",
            param1: code,
            param2: acctType,
            param3: $("#loginName").val(),
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (type === 'C') {
                jQuery("#consigneeName").val(data[0]);
                jQuery("#consigneeCode").val(data[1]);
                $('#consigneeName').attr('alt', 'IMPORTS_TP_NOTE');
            } else if (type === 'N') {
                jQuery("#notifyName").val(data[0]);
                jQuery("#notifyCode").val(data[1]);
                $('#notifyName').attr('alt', 'IMPORTS_TP_NOTE');
            } else if (type === 'N2') {
                jQuery("#notify2Name").val(data[0]);
                jQuery("#notify2Code").val(data[1]);
                $('#notify2Name').attr('alt', 'IMPORTS_TP_NOTE');
            } else if (type === 'S') {
                jQuery("#shipperName").val(data[0]);
                jQuery("#shipperCode").val(data[1]);
            }
        }
    });
}
function notify_AccttypeCheck(path) {
    var acctType = $("#notify_acct_type").val();
    var moduleName = $('#moduleName').val();
    var notifyDisabled = $('#notifyDisabled').val();
    if (notifyDisabled === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#notyDisableAcct').val() + "</span>");
        clearNotifyDetails(path);
    } else {
        if (acctType !== "" && acctType.indexOf("C") === -1) {
            if (moduleName !== 'Imports') {
                $.prompt("Please select the customers with account type C");
                clearNotifyDetails(path);
            } else {
                createNewConsignee(path, 'N', 'C');
            }
        } else if (moduleName === 'Imports') {
            $('.notifyCreditV').addClass('red');
            $('.notifyScanSopV').addClass('red');
            $('.notifyCreditV').text($('#notifyCredit').val());
            $('.notifyScanSopV').text($('#notifyScanSop').val());
            $('.importCreditNotify').addClass('green');
            if ($('#importCreditNotify').val() === 'Y') {
                $('.importCreditNotify').text('$');
            }
        }
        var notifyAddress = jQuery("#notifyAddress").val();
        if ((notifyAddress !== undefined && notifyAddress !== "")) {
            jQuery("#notifyAddress").val(jQuery("#notifyAddress").val().replace(/\,/g, " "));
        }
    }
}
function clearNotifyDetails(path) {
    jQuery("#notifyCode").val('');
    jQuery("#notifyAddress").val('');
    jQuery("#notifyCity").val('');
    jQuery("#notifyState").val('');
    jQuery("#notifyZip").val('');
    jQuery("#notifyCountry").val('');
    jQuery("#notifyPhone").val('');
    jQuery("#notifyFax").val('');
    jQuery("#notifyEmail").val('');
    jQuery("#notifyName").val('');
    jQuery("#notifyCredit").val('');
    //    jQuery("#notifyDisabled").val('');
    jQuery("#notyDisableAcct").val('');
    jQuery(".notifyCreditV").text('');
    jQuery(".notifyScanSopV").text('');
    $('.notNotes').attr("src", path + "/img/icons/e_contents_view.gif");
}

function congAlert(txt) {
    $.prompt(txt);
}

function showRelayCities() {
    var relayorigin = $('#originRelay').val();
    var relayDestination = $('#countryNameRelay').val();
    if (relayorigin != "") {
        $('#portOfOriginR').val(relayorigin);
    }
    if (relayDestination != "") {
        $('#finalDestinationR').val(relayDestination);
    }
}

function showBlock(tar) {
    $('#setSacacValue').attr('checked', false);
    $('.smallInputPopupBox').hide();
    $("#amsHblNo").val("");
    $("#amsHblPiece").val("");
    $("#amsHblScac").val("");
    $(tar).show("slow");
}
function hideBlock(tar) {
    $(tar).hide("slow");
}

function showHsCodeBlock(tar) {//HS Code based on destination
    var unLocationCode = getDestination();
    var fileNumberId = $('#fileNumberId').val();
    $('#hsCode').val('');
    $('#bookingHsCode').val('');
    $('#bookingHsCodeId').val('');
    $('#packageType').val('');
    $('#packageTypeId').val('');
    $('#hsCodePiece').val('');
    $('#hsCodeWeightMetric').val('');
    if (null != unLocationCode && "" != unLocationCode) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getPortsHsCode",
                param1: unLocationCode,
                param2: fileNumberId
            },
            async: false,
            success: function (data) {
                if (data === "true") {
                    tar = "#hsCodeBoxForOther";
                    $(tar).show("slow");
                } else {
                    tar = "#hsCodeBox";
                    $(tar).show("slow");
                }
            }
        });
    } else {
        $.prompt("Please Enter Destination");
    }
}

function getCommodityPcs() {
    var commSum = 0;
    $("#commObj tr td:nth-child(4)").each(function () {
        commSum += +$(this).text();
    });
    return commSum;
}

function submitForm(methodName) {


    window.parent.showLoading();
    var moduleName = $('#moduleName').val();
    var fileNumberId = $('#fileNumberId').val();
    if (moduleName != 'Imports' && fileNumberId != null && fileNumberId != "" && fileNumberId != "0") {
        displayClientNotes('B');
        displayShipperNotes('B');
        displayForwarderNotes('B');
        displayConsigneeNotes('B');
    }
    $("#methodName").val(methodName);
    $("#lclBookingForm").submit();
}

function submitAjaxForm(methodName, formName, selector, id) {//no need this method
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&id=" + id;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                $(selector).find("[title != '']").not("link").tooltip();
                if (methodName == "lclRelayFind") {
                    submitAjaxFormForVoyage('displayVoyage', '#lclBookingForm', '#upcomingSailing', '');
                }
                hideProgressBar();
            });
}

function submitAjaxFormForAgent(methodName, formName, selector, id) {//same for import and exports
    var transShipMent = $('input:radio[name=transShipMent]:checked').val();
    var moduleName = $('#moduleName').val();
    var foreignunlocationCode = "";
    if (moduleName === 'Exports') {
        foreignunlocationCode = $('#podUnlocationcode').val();
    } else if (moduleName === 'Imports') {
        foreignunlocationCode = $('#impForeignPod').val();
    }
    if ((transShipMent === 'Y' && transShipMent !== '') || moduleName !== "Imports") {
        showProgressBar();
        $("#methodName").val(methodName);
        var params = $(formName).serialize();
        params += "&id=" + id + "&foreignunlocationCode=" + foreignunlocationCode;
        $.post($(formName).attr("action"), params,
                function (data) {
                    $(selector).html(data);
                    $(selector, window.parent.document).html(data);
                    if (methodName === "refreshAgent") {
                        fillLCLdefaultAgent();
                    }
                    hideProgressBar();
                });
    }
}

function updateAgentFromBkg() {
    if ($('#fileType').val() === "BL" && $("#fileNumberId").val() !== "" && $('#moduleName').val() === 'Exports') {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "updateDefaultAgentFromBkg",
                param1: $("#fileNumberId").val(),
                param2: $("#agentNumber").val(),
                param3: $('input:radio[name=defaultAgent]:checked').val(),
                param4: $("#portOfDestinationId").val(),
                request: true,
                dataType: "json"
            },
            async: false,
            close: function (data) {
            }
        });
    }
}

function fillLCLdefaultAgent() {//Default Agent Values based on Exports and Imports
    var defaultagentValues = $('input:radio[name=defaultAgent]:checked').val();
    if (defaultagentValues === 'Y') {
        var moduleName = $('#moduleName').val();
        var fdUnlocationcode = "";
        if (moduleName === 'Exports') {
            fdUnlocationcode = $('#podUnlocationcode').val();
            if (fdUnlocationcode === ''){
                fdUnlocationcode=getPortOfDestination();
            }
        } else if (moduleName === 'Imports') {
            fdUnlocationcode = $('#impForeignPod').val();
        }
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getDefaultAgentForLcl",
                param1: fdUnlocationcode,
                param2: 'L',
                dataType: "json"
            },
            success: function (data) {
                if (data[3] === 'Y') {
                    $.prompt("This customer is disabled and merged with " + data[4] !== null ? data[4] : "");
                    $("#agentName").val('');
                    $("#agentNumber").val('');
                } else {
                    if (data[0] !== undefined && data[0] !== "" && data[0] !== null) {
                        $('#agentName').val(data[1]);
                        $('#agentNumber').val(data[0]);
                        $('#agentBrand').val(data[4]);
                        checkEculine($('#agentNumber').val(), 'A');
                        $("#agentName").addClass("text-readonly");
                        $("#agentName").addClass("textlabelsBoldForTextBoxDisabledLook");
                        $("#agentNumber").addClass("textlabelsBoldForTextBoxDisabledLook");
                        $("#agentName").attr("readonly", true);
                        $("#agentNumber").attr("readonly", true);
                        $("#agentInfo").val('');
                        parent.$("#agentAcctNoOnBL").val(data[0]);
                        updateAgentFromBkg();
                    } else {
                        $("#agentName").val('');
                        $("#agentNumber").val('');
                        $('#agentBrand').val('');
                    }
                }
            }
        });
    } else {
        $("#agentName").val("");
        $("#agentNumber").val("");
        $('#agentBrand').val('');
        $("#agentName").removeClass("textlabelsBoldForTextBoxDisabledLook");
        $("#agentName").addClass("textlabelsBoldForTextBox");
        $("#agentName").removeAttr("readonly");
    }
}
function clearLCLDefaultValues() {//Agent N
    var defaultAgentValues = $('input:radio[name=defaultAgent]:checked').val();
    var cob = $('#cob').val() === undefined ? "false" : $('#cob').val();
    if (defaultAgentValues === 'N' && cob === 'false') {
        $("#rtdTransaction").val("");
        $("#agentName").val("");
        $("#agentNumber").val("");
        $('#agentBrand').val('');
        $("#agentInfo").val("");
        $("#agentName").removeAttr("readonly");
        $("#agentName").removeClass("text-readonly textlabelsBoldForTextBoxDisabledLook");
        $("#agentName").addClass("textlabelsBoldForTextBox");
        $("#agentInfo").removeClass("textlabelsBoldForTextBoxDisabledLook");
        $("#agentInfo").addClass("textlabelsBoldForTextBox");
        $("#agentInfo").removeAttr("readonly");
    } else {
        $("#agentName").removeClass("textlabelsBoldForTextBox");
        $("#agentName").addClass("text-readonly textlabelsBoldForTextBoxDisabledLook");
        $("#agentInfo").removeClass("textlabelsBoldForTextBox");
        $("#agentInfo").addClass("textlabelsBoldForTextBoxDisabledLook");
        $("#agentName").attr('readonly', true);
        $("#agentInfo").attr('readonly', true);
    }
}
function lockAgentInfo() {//enable agent box
    var defaultAgentValues = $('input:radio[name=defaultAgent]:checked').val();
    var cob = $('#cob').val();
    if (defaultAgentValues === 'N' && cob === 'false') {
        $("#agentName").removeClass("textlabelsBoldForTextBoxDisabledLook").addClass("textlabelsBoldForTextBox");
        $("#agentInfo").removeClass("textlabelsBoldForTextBoxDisabledLook").addClass("textlabelsBoldForTextBox");
        $("#agentName").removeAttr("readonly");
        $("#agentInfo").removeAttr("readonly");
    }
}
function setDefaultRouteAgent() {//ERT Changes
    var transShipment = $('input:radio[name=transShipMent]:checked').val();
    var moduleName = $('#moduleName').val();
    if (transShipment === undefined || (moduleName === 'Imports' && transShipment === 'Y')) {
        var agentNo = $("#agentNumber").val();
        if ($("#rtdTransaction").val() === "Y") {
            if (null !== agentNo && agentNo !== '') {
                $("#agentInfo").val(agentNo);
            } else {
                document.getElementById("rtdTransaction").selectedIndex = 1;
                document.getElementById("rtdTransaction").value = "";
                $.prompt("You must first select an agent");
                $("#agentInfo").val('');
            }
        } else {
            $("#agentInfo").val('');
        }
    }
}


function submitAjaxFormforRates(methodName, formName, selector, id) {
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&id=" + id;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                fillPolPod();
                clearBookedForVoyage();
            });
}
function submitAjaxForm(methodName, formName, selector, origin, destination, pol, pod,
        radioValue, doorOriginCityZip, pickupReadyDate) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&origin=" + origin + "&destination=" + destination + "&pol=" + pol + "&pod=" + pod + "&radioValue=" + radioValue + "&doorOriginCityZip=" + doorOriginCityZip + "&pickupReadyDate=" + pickupReadyDate;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                if ($("#validateImpRates").val() !== "") {
                    $.prompt($("#validateImpRates").val());
                } else if ($("#newIpiChargeStatus").val() !== "") {
                    $.prompt($("#newIpiChargeStatus").val());
                }
                if ($('#moduleName').val() === 'Imports') {
                    disablecfsChargeBtn();
                }
                hideProgressBar();
            });
}

function validateform(module, checkDRChange) {
    var fileId = $('#fileNumberId').val();
    if (isFormChanged() === false && module === "Imports" && fileId !== "") {
        $.prompt("DR not saved - no changes detected");
        return false;
    }
    var result = true;
    var ratetype = $('input:radio[name=rateType]:checked').val();
    var thirdParty = $('input:radio[name=billForm]:checked').val();
    var CFCL = $('input:radio[name=cfcl]:checked').val();
    var haz = $('.hazmat').val();
    var ert = $('#rtdTransaction').val();
    var scheduleNo = $('#masterScheduleNo').val();
    var insurance = $('input:radio[name=insurance]:checked').val();
    var pcBoth = "";
    var agentAcct = $("#agentNumber").val();
    if (module !== "Imports") {
        pcBoth = $('input:radio[name=pcBoth]:checked').val();
    } else {
        pcBoth = $('input:radio[name=pcBothImports]:checked').val();
    }
    var bookingType = $('#bookingType').val();
    if (CFCL === "Y" && bookingType !== "T") {
        if ($("#portOfLoading").val() === "") {
            $("#portOfLoading").val($("#portOfOriginR").val());
            $("#polUnlocationcode").val($("#originUnlocationCode").val());
            $("#portOfLoadingId").val($("#portOfOriginId").val());
        }
        if ($("#portOfDestination").val() === "") {
            $("#portOfDestination").val($("#finalDestinationR").val());
            $("#podUnlocationcode").val($("#unlocationCode").val());
            $("#portOfDestinationId").val($("#finalDestinationId").val());
        }
    }
    if (checkDRChange === "true") {
        $('#checkDRChange').val('true');
    } else {
        $('#checkDRChange').val('false');
    }
    var pickupYesNo = $('input:radio[name=pooDoor]:checked').val();
    var datatableobj = document.getElementById('commObj');
    var headerId = $("#headerId").val();
    var shipment = $('input:radio[name=transShipMent]:checked').val();
    var unknownDest = $('input:radio[name=nonRated]:checked').val();
    var consigneeChargeFlag = "";
    var amsFlag = false;
    var podUnlocationcode = $('#podUnlocationcode').val();
    var fdUnlocation = $('#finalDestinationR').val();
    var fdUnlocationcode = fdUnlocation.substring(fdUnlocation.indexOf("(") + 1, fdUnlocation.indexOf(")"));
    var fdCode = fdUnlocationcode.substring(0, 2);
    var orginAgentStatus = sessionStorage.getItem("orginAgentStatus");
    var rowCount = $("#chargesTable tbody tr").length;
    if (orginAgentStatus === "Clear" && ($('#supplierNameOrg').val() === "" || $('#supplierCode').val() === "")) {
        $.prompt("Origin Agent is Required");
        return false;
    } else {
        sessionStorage.removeItem("orginAgentStatus");
    }
    for (var i = 1; i <= rowCount; i++) {
        var value = $("#chargesTable").find('tr:nth-child(' + i + ') td:nth-child(6)').html();
        if (value !== null && value.indexOf("Consignee") !== -1) {
            consigneeChargeFlag = "true";
            break;
        }
    }
    if (module !== "Imports" && (document.getElementById("portOfOriginR") === null || document.getElementById("portOfOriginR").value === "")) {
        $.prompt("Origin CFS is required");
        $("#portOfOriginR").css("border-color", "red");
        return false;
    } else if (module !== 'Imports' && agentAcct === "") {
        $.prompt("Agent is Required");
        return false;
    } else if ($("#finalDestinationR") === null || $("#finalDestinationR").val() === "") {
        $.prompt(module !== "Imports" ? "Destination is Required" : "Final Destination is Required");
        return false;
    } else if (module !== 'Imports' && (ratetype === null || ratetype === "" || ratetype === undefined)) {
        $.prompt("CTC/Retail/FTF  is required");
        return false;
    } else if (insurance === "Y" && ($("#valueOfGoods").val() === null || $("#valueOfGoods").val() === "")) {
        $.prompt("Please enter the Value of Goods");
        $("#valueOfGoods").css("border-color", "red");
        return false;
    }
//    else if (thirdParty === "T" && ($('#thirdPartyname').val() === '' || $('#thirdPartyname').val() === null)) {
//        $.prompt("Third Party Account Name is required");
//        $('#thirdPartyname').css("border-color", "red");
//        return false;
//    } 
    else if (CFCL === "Y" && ($('#cfclAccount').val() === '' || $('cfclAccount').val() === null)) {
        $.prompt("CFCL Account is required");
        $('#cfclAccount').css("border-color", "red");
        return false;
    } else if ($("terminal") === null || $("#terminal").val() === "") {
        $.prompt("Term to do BL is Required");
        $("#terminal").css("border-color", "red");
        return false;
    } else if (pcBoth === "" || pcBoth === null) {
        $.prompt(module !== "Imports" ? "P/C/Both  is required" : "P/C  is required");
        return false;
    } else if (module !== 'Imports' && ert === "" || ert === null) {
        $.prompt("ERT field is required");
        $("#rtdTransaction").css("border-color", "red");
        return false;
    } else if (pickupYesNo !== null && pickupYesNo !== undefined && pickupYesNo === "Y") {
        if (module === 'Exports' && $("#doorOriginCityZip").val() === "" && $("#manualDoorOriginCityZip").val() === "") {
            $.prompt("Door Origin/City/Zip is required");
            $("#doorOriginCityZip").css("border-color", "red");
            $("#manualDoorOriginCityZip").css("border-color", "red");
            return false;
        } else if (module === 'Imports' && $("#doorOriginCityZip").val() === "") {
            $.prompt("Door Dest/City/Zip is required");
            $("#doorOriginCityZip").css("border-color", "red");
            return false;
        } else if (module !== 'Imports' && datatableobj === null) {
            $.prompt("Please add atleast one commodity/tariff#");
            return false;
        }
    } else if (module !== 'Imports' && datatableobj === null) {
        $.prompt("Please add atleast one commodity/tariff#");
        return false;
    } else if ((module === 'Imports' && headerId !== null && headerId !== "") && datatableobj === null) {
        $.prompt("Please select atleast one Commodity");
        return false;
    } else if (module === 'Imports' && shipment === 'Y' && fdCode === 'US') {
        $.prompt("Final destination should not be in USA, Please change the Final destination.");
        $("#finalDestinationR").css("border-color", "red");
        return false;
    } else if (module === 'Imports' && shipment === 'Y' && ($("#portExit").val() === "" || $("#portExit").val() === null)) {
        $.prompt("USA Port of Exit is required");
        $("#portExit").css("border-color", "red");
        return false;
    } else if (module === 'Imports' && shipment === 'Y' && ($("#foreignDischarge").val() === "" || $("#foreignDischarge").val() === null)) {
        $.prompt("Foreign Port of Discharge is required");
        $("#foreignDischarge").css("border-color", "red");
        return false;
    } else if (module === 'Imports' && validateBilltoPartyImports()) {
        return false;
    } else if (module === 'Imports' && (getAmsPcs() < getCommodityPcs())) {
        amsFlag = true;
    }
    if (fileId !== "" && module !== 'Imports' && pickupYesNo === "Y") {
        if (!isValidateRates(fileId)) {
            $.prompt('Please Enter Inland Charge/Sell Amount and Inland Cost is Required');
            return false;
        }
    }
    if ((fileId !== "" && scheduleNo === "") || (fileId === "" && scheduleNo === "")) {
        var loginUserRoleId = $('#userRoleId').val();
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkBookingVoyage",
                param1: module,
                param2: loginUserRoleId,
                param3: "",
                dataType: "json"
            },
            success: function (data) {
                if (data === true && unknownDest === 'N' && scheduleNo === "") {
                    $.prompt("Booked for Voyage is Required");
                } else if (fileId !== "" && (haz === 'Haz' && haz != undefined)) {
                    hazValidateSaveButton(consigneeChargeFlag, amsFlag, result, fileId, module);
                } else {
                    saveBooking(consigneeChargeFlag, amsFlag, result);
                }
            }
        });
    } else if (fileId !== "" && (haz === 'Haz' && haz != undefined)) {
        hazValidateSaveButton(consigneeChargeFlag, amsFlag, result, fileId, module);
    } else {
        saveBooking(consigneeChargeFlag, amsFlag, result);
    }
}
function isValidateRates(fileId) {
    var flag = false;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO",
            methodName: "isValidateRates",
            param1: fileId,
            param2: "INLAND",
            dataType: "json"
        },
        preloading: true,
        async: false,
        success: function (data) {
            if (data) {
                flag = true;
            }
        }
    });
    return flag;
}
function saveBooking(consigneeChargeFlag, amsFlag, result) {
    $("#isFormchangeFlag").val(formChanged ? "true" : "false");
    var amsTable = document.getElementById("amsHbl");
    var row = amsTable ? amsTable.rows.length : 0;
    var module = $('#moduleName').val();
    if (module === 'Imports' && ($('#scac').val() === "" && $('#defaultAms').val() !== "") && $("#fileNumberId").val() !== "") {
        $.prompt('SCAC is required');
        $('#scac').css("border-color", "red");
    } else if (($('#defaultAms').val() === "" && $('#scac').val() !== "") && $("#fileNumberId").val() !== "") {
        $.prompt('AMS No is required');
        $("#defaultAms").css("border-color", "red");
    } else if (module === 'Imports') {
        var defaultAmsScac = $("#defaultAms").val().toUpperCase() + $("#scac").val().toUpperCase();
        var flag = false;
        $('.hblAmsNoHblScac').each(function () {
            if ($(this).text().trim().trim().toUpperCase() === defaultAmsScac) {
                flag = true;
            }
        });
        if (flag && module === 'Imports') {
            $.prompt("This DR - " + $('#fileNumber').val() + " already has same scac - <span style=color:red>" + $('#scac').val().toUpperCase() + "</span>"
                    + " and ams# - <span style=color:red>" + $('#defaultAms').val().toUpperCase() + "</span>");
            $('#scac').focus();
        } else {
            if ($('#segId').val() === '' || $('#segId').val() === null || $('#segId').val() === undefined) {
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO",
                        methodName: "validateScacAndAms",
                        param1: $('#scac').val(),
                        param2: $('#defaultAms').val(),
                        param3: $("#fileNumberId").val()
                    },
                    preloading: true,
                    success: function (data) {
                        if (data === "available") {
                            if (result) {
                                if (module === 'Imports' && (amsFlag && row > 1)) {
                                    $.prompt("AMS piece count does not match total piece count", {
                                        buttons: {
                                            Ok: 1
                                        },
                                        submit: function (v) {
                                            if (v === 1 && ($('#newConsignee').attr('checked') && consigneeChargeFlag === 'true')) {
                                                var txt = 'Consignee does not exist in Trading Partners . Cannot bill charges to Consignee .Please correct.';
                                                showAlertMsg(txt);
                                            } else {
                                                submitForm('saveBooking');
                                            }
                                        }
                                    });
                                } else {
                                    submitForm('saveBooking');
                                }
                            }
                        } else {
                            $.prompt(data, {
                                callback: function () {
                                    $('#scac').focus();
                                }
                            });
                        }
                    }
                });
            } else {
                submitForm('saveBooking');
            }
        }
    } else {
        submitForm('saveBooking');
    }
}
function showAlertMsg(txt) {
    $.prompt(txt, {
        buttons: {
            Ok: 1
        },
        submit: function (v) {
            if (v === 1) {
                submitForm('saveBooking');
            }

        }
    });
}

function validateBilltoPartyImports() {
    var billtoCodeImports = $('input:radio[name=billtoCodeImports]:checked').val();
    if (billtoCodeImports == "T") {
        if ($('#thirdpartyaccountNo').val() == "") {
            congAlert("Please select Third Party Account");
            $("#thirdpartyaccountNo").css("border-color", "red");
            $("#warning").parent.show();
            result = false;
        }
    } else if (billtoCodeImports == "C") {
        if ($('#consigneeName').val() == "" && $('#dupConsigneeName').val() == "") {
            congAlert("Please select Consignee Account ");
            $("#consigneeName").css("border-color", "red");
            $("#warning").parent.show();
            result = false;
        }
    } else if (billtoCodeImports == "N") {
        if ($('#notifyName').val() == "" && $('#dupNotifyName').val() == "") {
            congAlert("Please select Notify Account");
            $("#notifyName").css("border-color", "red");
            $("#warning").parent.show();
            result = false;
        }
    }
//    else if (billtoCodeImports == "A") {
//        if ($('#agentName').val() == "" && $('#agentNumber').val() == "") {
//            congAlert("Please select Agent Account");
//            $("#agentName").css("border-color", "red");
//            $("#warning").parent.show();
//            result = false;
//        }
//    }
}

function changePort() {
    var relay = $('input:radio[name=relayOverride]:checked').val();
    var finalDestinationR = $('#finalDestinationR').val();
    var unlocationCode = $('#unlocationCode').val();
    var portOfDestination = $('#portOfDestination').val();
    var portDest = portOfDestination.substring(0, portOfDestination.indexOf('/'));
    var unCode = portOfDestination.substring(portOfDestination.indexOf('(') + 1, portOfDestination.indexOf(')'));
    var destination = "";
    if (finalDestinationR != "") {
        destination = finalDestinationR.substring(0, finalDestinationR.indexOf('/'));
    }
    if (relay != undefined && relay == "Y") {
        $('#portOfLoading').removeClass().addClass("text");
        $('#portOfDestination').removeClass().addClass("text");
        $('#portOfLoading').removeAttr("readonly");
        $('#portOfDestination').removeAttr("readonly");
        $('#showFullRelay').removeAttr('disabled');
        $('#showFullRelayFd').removeAttr('disabled');
        //        if (destination != "" && portOfDestination != "") {
        //            if (destination.toString().toUpperCase() != portDest.toString().toUpperCase() && unCode.toString().toUpperCase() != unlocationCode.toString().toUpperCase()) {
        //                $('#podfd').show();
        //                $('#podfdtt').show();
        //                var podfdtt = $('#podfdtt').val();
        //                if (podfdtt != '') {
        //                    $('#podfdtt').val();
        //                }
        //                else {
        //                    $('#podfdtt').val('');
        //                }
        //            }
        //            else {
        //                $('#podfd').hide();
        //                $('#podfdtt').hide();
        //            }
        //        }
    }
    if (relay != undefined && relay == "N") {

        // loadingVoyage();
        $('#portOfLoading').addClass("text-readonly");
        $('#portOfDestination').addClass("text-readonly");
        $('#portOfLoading').attr("readonly", true);
        $('#portOfDestination').attr("readonly", true);
        $('input[name=showFullRelay]').attr('checked', false);
        $('input[name=showFullRelayFd]').attr('checked', false);
        $('#showFullRelay').attr('disabled', true);
        $('#showFullRelayFd').attr('disabled', true);
        $('#podFdTransTime').hide();
        $('#podfdtrans').val('');
//        $('#portOfDestination').val($('#finalDestinationR').val())
//        $('#portOfDestinationId').val($('#finalDestinationId').val())
//        $('#podUnlocationcode').val($('#unlocationCode').val())
//        //        $('#podfd').hide();
        //        $('#podfdtt').hide();
        //        $('#podfdtt').val('');
    }
}

function podDestination() {
    var relay = $('input:radio[name=relayOverride]:checked').val();
    var finalDestinationR = $('#finalDestinationR').val();
    var finalDestinationId = $('#finalDestinationId').val();
    var portOfOriginId = $('#portOfOriginId').val();
    var unlocationCode = $('#unlocationCode').val();
    var portOfDestination = $('#portOfDestination').val();
    var portDest = portOfDestination.substring(0, portOfDestination.lastIndexOf('/'));
    var unCode = portOfDestination.substring(portOfDestination.lastIndexOf('(') + 1, portOfDestination.lastIndexOf(')'));
    var destination = "";
    if (finalDestinationR != "") {
        destination = finalDestinationR.substring(0, finalDestinationR.indexOf('/'));
    }
    if (relay != undefined && relay == "Y") {
        $('#portOfLoading').removeClass().addClass("text");
        $('#portOfDestination').removeClass().addClass("text");
        $('#portOfLoading').removeAttr("readonly");
        $('#portOfDestination').removeAttr("readonly");
        $('#showFullRelay').removeAttr('disabled');
        $('#showFullRelayFd').removeAttr('disabled');
        //        if (destination != "" && portOfDestination != "") {
        //            if (destination.toString().toUpperCase() != portDest.toString().toUpperCase() && unCode.toString().toUpperCase() != unlocationCode.toString().toUpperCase()) {
        //                $('#podfd').show();
        //                $('#podfdtt').show();
        //                var podfdtt = $('#podfdtt').val();
        //                if (podfdtt != '') {
        //                    $('#podfdtt').val();
        //                }
        //                else {
        //                    $('#podfdtt').val('');
        //                }
        //            }
        //            else {
        //                $('#podfd').hide();
        //                $('#podfdtt').hide();
        //            }
        //        }

    }

}
function changerelay() {
    var relay = $('input:radio[name=relayOverride]:checked').val();
    var finalDestinationR = $('#finalDestinationR').val();
    var unlocationCode = $('#unlocationCode').val();
    var portOfDestination = $('#portOfDestination').val();
    var portDest = portOfDestination.substring(0, portOfDestination.lastIndexOf('/'));
    var unCode = portOfDestination.substring(portOfDestination.lastIndexOf('(') + 1, portOfDestination.lastIndexOf(')'));
    var destination = "";
    if (finalDestinationR != "") {
        destination = finalDestinationR.substring(0, finalDestinationR.indexOf('/'));
    }
    if (relay != undefined && relay == "Y") {
        /* var originlrd=$("#originLrd").val();
         if(originlrd!==""){
         $("#originLrd").val('');
         }*/
        $('#portOfLoading').removeClass().addClass("text");
        $('#portOfDestination').removeClass().addClass("text");
        $('#portOfLoading').removeAttr("readonly");
        $('#portOfDestination').removeAttr("readonly");
        $('#showFullRelay').removeAttr('disabled');
        $('#showFullRelayFd').removeAttr('disabled');
        if (destination != "" && portOfDestination != "") {
            if (destination.toString().toUpperCase() != portDest.toString().toUpperCase() && unCode.toString().toUpperCase() != unlocationCode.toString().toUpperCase()) {
                $('#podfd').show();
                $('#podfdtt').show();
            } else {
                $('#podfd').hide();
                $('#podfdtt').hide();
            }
        }
    }
    if (relay != undefined && relay == "N") {
        //var portofOriginR = $('#portOfOriginR').val();
        // var finalDestinationR = $('#finalDestinationR').val();
        // var portOfOriginIdR=$("#portOfOriginId").val();
        // var finalDestinationIdR=$("#finalDestinationId").val();
        // document.getElementById('portOfLoading').value = portofOriginR;
        // document.getElementById('portOfDestination').value = finalDestinationR;
        // $("#portOfLoadingId").val(portOfOriginIdR);
        // $("#portOfDestinationId").val(finalDestinationIdR);
        //loadingVoyage();
        $('#portOfLoading').addClass("text-readonly");
        $('#portOfDestination').addClass("text-readonly");
        $('#portOfLoading').attr("readonly", true);
        $('#portOfDestination').attr("readonly", true);
        $('input[name=showFullRelay]').attr('checked', false);
        $('input[name=showFullRelayFd]').attr('checked', false);
        $('#showFullRelay').attr('disabled', true);
        $('#showFullRelayFd').attr('disabled', true);
        $('#podfd').hide();
        $('#podfdtt').hide();
    }
}
function checkForNumberAndDecimal(obj) {
    if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
        obj.value = "";
        sampleAlert("This field should be Numeric");
    }
}
function showPickupButton() {
    var pooDoor = $('input:radio[name=pooDoor]:checked').val();
    var module = $('#moduleName').val();
    var fileId = $('#fileNumberId').val();
    var status = $('#fileStatus').val();
    var transType = $('#transType').val();
    var spReferenceNo = $('#spReferenceNo').val();
    var doorOriginCityZip = $('#doorOriginCityZip').val();
    var pickupFlag = $('#pickupFlag').val();
    $("#manualDoorOriginCityZip").hide();
    if (pooDoor === 'Y') {
        if (module === "Exports") {
            $('#doorOriginCityZip').removeClass("textlabelsBoldForTextBoxDisabledLook");
            $('#doorOriginCityZip').addClass("textlabelsBoldForTextBox");
            $('#doorOriginCityZip').removeAttr("readonly");
            $("#checkDoorOriginCityZip").show();
            if ($('#doorOriginCityZip').val() === '') {
                $("#checkDoorOriginCityZip").attr('checked', false);
            }
            $("#doorOriginCityZip").show();
        } else if (module === "Imports") {
            if (fileId !== "" && doorOriginCityZip !== '' && pickupFlag === 'true' && (status === 'M' || transType !== 'AC' || spReferenceNo !== '')) {
                $('#doorOriginCityZip').removeClass("textlabelsBoldForTextBoxWidth");
                $('#doorOriginCityZip').addClass("textlabelsBoldForTextBoxDisabledLookWidth");
                $('#doorOriginCityZip').attr("readonly", true);
            } else {
                $('#doorOriginCityZip').removeClass("textlabelsBoldForTextBoxDisabledLookWidth");
                $('#doorOriginCityZip').addClass("textlabelsBoldForTextBoxWidth");
                $('#doorOriginCityZip').removeAttr("readonly");
            }
        }
    } else {
        $('#doorOriginCityZip').val('');
        $('#duplicateDoorOrigin').val('');
        if (module === "Exports") {
            $('#doorOriginCityZip').removeClass("textlabelsBoldForTextBox");
            $('#doorOriginCityZip').addClass("textlabelsBoldForTextBoxDisabledLook");
            $('#doorOriginCityZip').attr("readonly", true);
            $("#checkDoorOriginCityZip").hide();
        } else {
            $('#doorOriginCityZip').removeClass("textlabelsBoldForTextBoxWidth");
            $('#doorOriginCityZip').addClass("textlabelsBoldForTextBoxDisabledLookWidth");
            $('#doorOriginCityZip').attr("readonly", true);
        }
    }
    var doorOrigin = $('#doorOriginCityZip').val();
    if (doorOrigin !== null && doorOrigin !== undefined && doorOrigin !== "") {
        $('#pickupInfo').show();
        if (module === "Exports" && doorOrigin.indexOf("/") < 0) {
            $('#doorOriginCityZip').hide();
            $("#manualDoorOriginCityZip").show();
        }
    } else {
        $('#pickupInfo').hide();
    }
}

function changeDoor(fileNumberStatus) {
    var fileId = $('#fileNumberId').val();
    var pooDoor = $('input:radio[name=pooDoor]:checked').val();
    var module = $('#moduleName').val();
    var status = $('#fileStatus').val();
    var transType = $('#transType').val();
    var spReferenceNo = $('#spReferenceNo').val();
    var doorOriginCityZip = $('#doorOriginCityZip').val();
    var pickupFlag = $('#pickupFlag').val();
    alertMsg = module == "Exports" ? 'Inland' : 'Door Delivery';
    var txt = alertMsg + " Rates will be deleted.Are you sure want to continue?";
    if (pooDoor === "N") {
        if (fileId !== "" && doorOriginCityZip !== '' && (status === 'M' || (transType !== '' && transType !== 'AC') || spReferenceNo !== '')) {
            txt = alertMsg + " Rates will get delete.Please use delete icon in Cost and Charges Tab to delete";
        } else {
            txt = alertMsg + " Rates will be deleted.Are you sure want to continue?";
        }
        if (fileId !== "") {
            if (isValidateRates(fileId) && module === 'Exports') {
                deletePickupRates(txt, fileNumberStatus);
            } else if (module === 'Imports') {
                deletePickupRates(txt, fileNumberStatus);
            }
        } else {
            $('#doorOriginCityZip').val('');
        }
        if (module === "Exports") {
            $("#checkDoorOriginCityZip").hide();
            $("#manualDoorOriginCityZip").val('');
            $("#manualDoorOriginCityZip").addClass("textlabelsBoldForTextBoxDisabledLook");
            $('#doorOriginCityZip').addClass("textlabelsBoldForTextBoxDisabledLook");
        } else {
            $('#doorOriginCityZip').addClass("textlabelsBoldForTextBoxDisabledLookWidth");
        }
        $('#doorOriginCityZip').val('');
        $('#doorOriginCityZip').attr("readonly", true);
        $('#pickupInfo').hide();
        //        }
    }
}

function newCommodity(path, fileNumberId, fileNumber, tabFlag, moduleId) {
    var fileNo = $("#fileNumber").val();
    var agentAcct = $("#agentNumber").val();
    var datatableobj = document.getElementById('commObj');
    $('#isFormChanged').val(isFormChanged());
    var rateType = $("input:radio[name='rateType']:checked").val();
    var spotRate = $('input:radio[name=spotRate]:checked').val();
    var destination = getDestination();
    if (destination === '007UN') {
        destination = 'PRSJU';
    }
    var portOfDestination = $('#portOfDestination').val();
    if (moduleId === 'Exports' && fileNo === '' && agentAcct === '' && portOfDestination !=='') {
        $.prompt("Agent is Required");
    } else if (destination === null || destination === "") {
        $.prompt("Destination is Required");
    } else if (moduleId === 'Imports' && ($('#supplierCode').val() === null || $('#supplierCode').val() === '')) {
        $.prompt("Origin Agent is Required");
        $("#supplierNameOrg").css("border-color", "red");
    } else if (moduleId !== 'Imports' && (rateType === null || rateType === "" || rateType === undefined)) {
        $.prompt("RateType is required");
    } else if ($('#rtdTransaction').val() === "" || $('#rtdTransaction').val() === null) {
        $.prompt("ERT field is required");
        $("#rtdTransaction").css("border-color", "red");
    } else if (datatableobj !== null && datatableobj.rows.length >= 1 && moduleId != 'Imports') {
        if (spotRate === "Y") {
            $.prompt("Only one Commodity details is allowed for Spot Rate");
        } else {
            commodityValidate("A commodity detail line already exists. Are you sure you want to add a 2nd commodity detail line?",
                    path, fileNumberId, fileNumber, tabFlag, moduleId, destination);
        }
        // $("span").removeClass("addCommodity");
        // commodityValidate("You have already selected a commodity/tariff#,  are you sure you want to add a new one?", path, fileNumberId, fileNumber, tabFlag, moduleId, destination);
    } else {
        newCommodityPopUp(path, fileNumberId, fileNumber, tabFlag, moduleId, "flase", destination);
    }
}
function commodityValidate(txt, path, fileNumberId, fileNumber, tabFlag, moduleId, destination) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                newCommodityPopUp(path, fileNumberId, fileNumber, tabFlag, moduleId, "flase", destination);
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function newCommodityPopUp(path, fileNumberId, fileNumber, tabFlag, moduleId, editDimFlag, destination) {
    var transhipment = "";
    var coloadCommo = "";
    var notifyNo = "";
    var consNo = "";
    var agentNo = "";
    var clientNo = "";
    var billingType = "";
    var origin = getOrigin();
    var pol = $('#polUnlocationcode').val();
    var pod = $('#podUnlocationcode').val();
    var rateType = $("input:radio[name='rateType']:checked").val();
    var spotRate = $('input:radio[name=spotRate]:checked').val();
    var cfcl = $('input:radio[name=cfcl]:checked').val();
    var unitSsId = $("#unitSsId").val();
    if (moduleId === 'Imports') {
        transhipment = $("input:radio[name='transShipMent']:checked").val();
        coloadCommo = $('#coloadCommNo').val();
        notifyNo = $('#notifyCode').val();
        consNo = $('#consigneeCode').val();
        agentNo = $('#supplierCode').val();
        clientNo = $('#client_no').val();
        billingType = $('input:radio[name=pcBothImports]:checked').val();
    } else {
        if (spotRate === 'Y') {
            clientNo = $('#spotRateCommNo').val();
        } else {
            clientNo = setTraffiDetails(); //return client or fwd or shipper or consignee (retail or coload commodity Number)
        }
    }
    var href = path + "/lclCommodity.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&tabFlag=" + tabFlag
            + "&editDimFlag=" + editDimFlag + "&origin=" + origin + "&destination=" + destination + "&rateType=" + rateType + "&pol=" + pol
            + "&pod=" + pod + "&unitSsId=" + unitSsId + "&transhipment=" + transhipment + "&moduleName=" + moduleId
            + "&coloadCommNo=" + coloadCommo + "&billingType=" + billingType + "&notifyNo=" + notifyNo + "&consNo=" + consNo + "&agentNo=" + agentNo + "&clientNo=" + clientNo
            + "&cfcl=" + cfcl;
    $.colorbox({
        iframe: true,
        width: "90%",
        height: "90%",
        href: href,
        scrolling: false,
        title: "Commodity"
    });
}
function getOrigin() {
    var originCode = "";
    var originObjR = document.getElementById('portOfOriginR');
    if (originObjR != undefined && originObjR != null && originObjR.value != "") {
        originCode = document.getElementById('portOfOriginR').value;
    }
    if (originCode.lastIndexOf("(") > -1 && originCode.lastIndexOf(")") > -1) {
        return originCode.substring(originCode.lastIndexOf("(") + 1, originCode.lastIndexOf(")"));
    }
    return "";
}
function getDestination() {
    var destinationCode = "";
    var destinationObjR = document.getElementById('finalDestinationR');
    if (destinationObjR != undefined && destinationObjR != null && destinationObjR.value != "") {
        destinationCode = document.getElementById('finalDestinationR').value;
    }
    if (destinationCode.lastIndexOf("(") > -1 && destinationCode.lastIndexOf(")") > -1) {
        return destinationCode.substring(destinationCode.lastIndexOf("(") + 1, destinationCode.lastIndexOf(")"));
    }
    return "";
}
function getPortOfDestination() {
    var portOfDestinationObjR = document.getElementById('portOfDestination').value;
    if (portOfDestinationObjR != undefined && portOfDestinationObjR != null && portOfDestinationObjR.value != "") {
        if (portOfDestinationObjR.lastIndexOf("(") > -1 && portOfDestinationObjR.lastIndexOf(")") > -1) {
            return portOfDestinationObjR.substring(portOfDestinationObjR.lastIndexOf("(") + 1, portOfDestinationObjR.lastIndexOf(")"));
        }
    }
    return "";
}

function openAes(path, fileNumberId, fileNumber) {
    var href = path + "/lclBooking.do?methodName=displayAES&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber;
    $.colorbox({
        iframe: true,
        href: href,
        width: "65%",
        height: "65%",
        title: "AES/ITN Details"
    });
}



//commodity descritpin js code
function submitAjaxFormCrossUrl(id) {
    $('#commId').val(id);
}

function deleteCommodity(count, commodityId, fileNumberId, status) {
    var filenumberId = $('#fileNumberId').val();
    if (filenumberId != "") {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkRates",
                param1: filenumberId,
                dataType: "json"
            },
            success: function (data) {
                if (data == true) {
                    var txt = 'AutoRates will be removed. Manual rates will be recalculated. Are you sure you want to delete?';
                    $.prompt(txt, {
                        buttons: {
                            Yes: 1,
                            No: 2
                        },
                        submit: function (v) {
                            if (v == 1) {
                                showProgressBar();
                                $("#methodName").val("deleteLclCommodity");
                                var params = $("#lclBookingForm").serialize();
                                params += "&count=" + count + "&commodityId=" + commodityId + "&fileNumberId=" + fileNumberId + "&status=" + status;
                                $.post($("#lclBookingForm").attr("action"), params,
                                        function (data) {
                                            $("#commodityDesc").html(data);
                                            $("#commodityDesc", window.parent.document).html(data);
                                            var destination = $('#unlocationCode').val();
                                            $("#methodName").val("modifyCommodityAndCharges");
                                            var params1 = $("#lclBookingForm").serialize();
                                            params1 += "&id=" + fileNumberId + "&destination=" + destination + "&recalculate=true";
                                            $.post($("#lclBookingForm").attr("action"), params1,
                                                    function (data) {
                                                        $("#chargeDesc").html(data);
                                                        $("#chargeDesc", window.parent.document).html(data);
                                                        $("#chargeDesc").find("[title != '']").not("link").tooltip();
                                                        parent.$.fn.colorbox.close();
                                                        hideProgressBar();
                                                    });
                                            setOriginLrd();
                                        });
                                hideProgressBar();
                                $.prompt.close();
                            } else if (v == 2) {
                                $.prompt.close();
                            }
                        }
                    });
                } else {
                    var txt = 'Are you sure You want to delete?';
                    $.prompt(txt, {
                        buttons: {
                            Yes: 1,
                            No: 2
                        },
                        submit: function (v) {
                            if (v == 1) {
                                showProgressBar();
                                $.prompt.close();
                                submitAjaxFormDelete('deleteLclCommodity', '#lclBookingForm', '#commodityDesc', count, commodityId, fileNumberId, status);
                                setOriginLrd();
                                hideProgressBar();
                            } else if (v == 2) {
                                $.prompt.close();
                            }
                        }
                    });
                }
            }
        });
    } else {
        var txt = "Are you sure you want to delete?";
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    showProgressBar();
                    $.prompt.close();
                    submitAjaxFormDelete('deleteLclCommodity', '#lclBookingForm', '#commodityDesc', count, commodityId, fileNumberId, status);
                } else if (v == 2) {
                    $.prompt.close();
                }
            }
        });
    }
}

function submitAjaxFormDelete(methodName, formName, selector, count, commodityId, fileNumberId, status) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&id=" + count + "&commodityId=" + commodityId + "&fileNumberId=" + fileNumberId + "&status=" + status;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}

function submitAjaxFormForCost(methodName, formName, selector, id, fileNumberId) {
    showProgressBar();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&id=" + id + "&fileNumberId=" + fileNumberId;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                hideProgressBar();
            });
}

function deleteAesDesc(txt, id, shpdr) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v == 1) {
                showProgressBar();
                $.ajax({
                    url: "lclAesDetails.do?methodName=closeAes&id=" + id + "&shpdr=" + shpdr,
                    success: function (data) {
                        $('#aesDesc').html(data);
                        hideProgressBar();
                    }
                });
                $.prompt.close();
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}

function closeAes(trnref) {
    $('#trn').val(trnref);
}

function editBkgHazmat(path, bookingPieceId) {
    var fileId = $('#fileNumberId').val();
    if (fileId != null && fileId != "" && fileId != '0') {
        var fileNumber = $('#fileNumber').val();
        var moduleName = $('#moduleName').val();
        var href = path + "/lclHazmat.do?methodName=display&fileNumberId=" + fileId + "&fileNumber="
                + fileNumber + "&bookingPieceId=" + bookingPieceId + "&moduleName=" + moduleName;
        $(".hazmat").attr("href", href);
        $(".hazmat").colorbox({
            iframe: true,
            width: "84%",
            height: "98%",
            title: "Haz.Mat",
            onClosed: function () {
                showProgressBar();
                $.post($('#lclBookingForm').attr("action"), "methodName=getLcl3pReference&thirdPName=hotCodes&fileNumberId=" + fileId,
                        function (data) {
                            $('#hotCodesList').html(data);
                            $('#hotCodesList', window.parent.document).html(data);
                        });
                $("#methodName").val("closeHazmat");
                var params = $('#lclBookingForm').serialize();
                params += "&fileNumberId=" + fileId;
                $.post($('#lclBookingForm').attr("action"), params,
                        function (data) {
                            $('#commodityDesc').html(data);
                            $('#commodityDesc', window.parent.document).html(data);
                            $.fn.colorbox.close();
                            hideProgressBar();
                        });
            }
        });
    } else {
        $.prompt("Please Save Booking");
    }
}
function openDetails(path, bookingPieceId) {//remove this method
    var href = path + "/commodityDetails.do?methodName=display&bookingPieceId=" + bookingPieceId;
    $(".details").attr("href", href);
    $(".details").colorbox({
        iframe: true,
        width: "62%",
        height: "85%",
        title: "Commodity Details"
    });
}

function editWhseDetails(path, fileNumberId, fileNumber, bookingPieceId) {
    if (fileNumberId !== null && fileNumberId !== "" && fileNumberId !== '0') {
        var bookingType = $('#bookingType').val();
        var moduleName = $('#moduleName').val();
        var warhsNo = $('#whsCode').val();
        var href = path + "/lclCommodity.do?methodName=displayWhse&fileNumber=" + fileNumber + "&fileNumberId=" + fileNumberId + "&warhsNo=" + warhsNo
                + "&pieceId=" + bookingPieceId + "&bookingType=" + bookingType + "&moduleName=" + moduleName + "&unitId=" + $('#unitId').val() + "&status=" + $('#fileStatus').val();
        $(".whseDetail").attr("href", href);
        $(".whseDetail").colorbox({
            iframe: true,
            width: "70%",
            height: "80%",
            title: "Warehouse Details",
            onClosed: function () {
//                if ((bookingType !== 'T' && moduleName === 'Exports') || moduleName === "Imports") {
                var fileNumberId = $('#fileNumberId').val();
                $("#methodName").val("closeDetail");
                var bookingPieceId = $('#bookPieceId').val();
                var params = $("#lclBookingForm").serialize();
                params += "&bookingPieceId=" + bookingPieceId + "&fileNumberId=" + fileNumberId;
                $.post($("#lclBookingForm").attr("action"), params,
                        function (data) {
                            $("#commodityDesc").html(data);
                            $("#commodityDesc", window.parent.document).html(data);
                            $("#commodityDesc").find("[title != '']").not("link").tooltip();
                        });
            }
//            }
        });
    } else {
        congAlert("Please Save Booking");
    }
}
function editBookingCommodity(path, id, editDimFlag, fileNumberId, fileNumber, copyVal) {
    var originalClose = $.colorbox.close;
    var moduleName = $('#moduleName').val();
    $('#isFormChanged').val(isFormChanged());
    $.colorbox.close = function () {
        var $frame = $(".cboxIframe").contents();
        if (($frame.find("#lclCommodityForm").serialize() !== $frame.find("#loadForm").val()) && ($frame.find("#methodName").val() === "editLclCommodity")) {
            if ($frame.find("#moduleName").val() === 'Exports') {
                $frame.find("body").append(
                        $.prompt("Some fields have been modified, if you exit your changes will not be saved.", {
                            buttons: {
                                SaveAndExit: true,
                                ExitWithoutSaving: false
                            },
                            submit: function (v) {
                                if (v) {
                                    parent.showLoading();
                                    $frame.find("#methodName").val('addLclCommodity');
                                    var params = $frame.find("#lclCommodityForm").serialize();
                                    $.post($frame.find("#lclCommodityForm").attr('action'), params,
                                            function (data) {
                                                $("#commodityDesc").html(data);
                                                $("#commodityDesc", window.parent.document).html(data);
                                                $frame.find("#methodName").val("modifyMinimumRates");
                                                var params1 = $frame.find("#lclCommodityForm").serialize();
                                                $.post($frame.find("#lclCommodityForm").attr("action"), params1,
                                                        function (data) {
                                                            $("#chargeDesc").html(data);
                                                            $("#chargeDesc", window.parent.document).html(data);
                                                            $("#chargeDesc").find("[title != '']").not("link").tooltip();
                                                            originalClose();
                                                        });
                                            });
                                } else {
                                    originalClose();
                                }
                            }
                        }));
                $frame.find("body").clear();
            } else {
                $frame.find("body").append(
                        $.prompt("Some fields have been modified, Use Save option"));
            }
        } else {
            originalClose();
        }
    }


    var origin = getOrigin();
    var transhipment = "";
    var destination = getDestination();
    if (destination === '007UN') {
        destination = 'PRSJU';
    }
    var rateType = $("input:radio[name='rateType']:checked").val();
    var moduleName = $('#moduleName').val();
    var unitSsId = $("#unitSsId").val();
    var shortShipFileNo = $('#shortShipFlag').val() === "true" ? fileNumber : "";
    var cfcl = $('input:radio[name=cfcl]:checked').val();
    var flag = true;
    if (moduleName === 'Imports') {
        transhipment = $("input:radio[name='transShipMent']:checked").val();
        if (isFormChanged()) {
            $.prompt("Some fields have been modified, Please save Booking");
            return false;
        }
    }
    if (flag) {
        var href = path + "/lclCommodity.do?methodName=editLclCommodity&id=" + id + "&editDimFlag=" + editDimFlag + "&fileNumber=" + fileNumber;
        href = href + "&origin=" + origin + "&destination=" + destination + "&rateType=" + rateType + "&fileNumberId=" + fileNumberId + "&copyVal=" + copyVal;
        href = href + "&transhipment=" + transhipment + "&moduleName=" + moduleName + "&status=" + $("#fileStatus").val();
        href = href + "&disposition=" + $("#exportDisposition").val() + "&unitSsId=" + unitSsId + "&shortShipFileNo=" + shortShipFileNo + "&agentNo=" + $("#agentNumber").val() + "&cfcl=" + cfcl;
        $.colorbox({
            iframe: true,
            href: href,
            width: "90%",
            height: "90%",
            scrolling: false,
            title: "Commodity",
            onClosed: function () {
                if ((fileNumberId !== '' && fileNumberId !== '0' && fileNumberId !== null && fileNumberId !== undefined)) {
                    window.parent.showLoading();
                    var method = formChanged ? "saveBooking" : "editBooking";
                    $("#methodName").val(method);
                    $("#lclBookingForm").submit();
                }
            }
        });
    }
}


$(document).ready(function () {
    var moduleName = $('#moduleName').val();
    $('#consigneeContactName').keyup(function () {
        if (moduleName != 'Imports') {
            if ($('#consigneeContactName').val() == "") {
                $('#consigneeFaxClient').val($('#dupConsigneeFax').val());
                $('#consigneePhoneClient').val($('#dupConsigneePhone').val());
                $('#consigneeContactName').val('');
                $('#consigneeManualContact').val('');
                $('#consigneeEmailClient').val('');
            }
        }
    });
    $('#consigneeManualContact').keyup(function () {
        if (moduleName != 'Imports') {
            if ($('#consigneeManualContact').val() == "") {
                $('#consigneeFaxClient').val($('#dupConsigneeFax').val());
                $('#consigneePhoneClient').val($('#dupConsigneePhone').val());
                $('#consigneeContactName').val('');
                $('#consigneeEmailClient').val('');
            }
        }
    });
    $('#finalDestinationR').keyup(function () {
        if (moduleName == 'Imports') {
            if ($('#finalDestinationR').val() == "") {
                $('#specialRemarks').val('');
                $('#internalRemarks').val('');
                $('#portGriRemarks').val('');
            }
        }
    });
    $('#forwardercontactClient').keyup(function () {
        if (moduleName != 'Imports') {
            if ($('#forwardercontactClient').val() == "") {
                $('#forwarederContactManual').val('');
                $('#forwarderEmailClient').val('');
                $('#forwarderFaxClient').val($('#dupForwarderFax').val());
                $('#forwarderPhoneClient').val($('#dupForwarderPhone').val());
                $('#forwarderFax').val('');
                $('#forwarderPhone').val('');
                $('#forwarderEmail').val('');
            }
        }
    });
    $('#forwarederContactManual').keyup(function () {
        if (moduleName != 'Imports') {
            if ($('#forwarederContactManual').val() == "") {
                $('#forwardercontactClient').val('');
                $('#forwarderEmailClient').val('');
                $('#forwarderFaxClient').val($('#dupForwarderFax').val());
                $('#forwarderPhoneClient').val($('#dupForwarderPhone').val());
                $('#forwarderFax').val('');
                $('#forwarderPhone').val('');
                $('#forwarderEmail').val('');
            }
        }
    });
    $('#contactName').keyup(function () {
        if (moduleName != 'Imports') {
            if ($('#contactName').val() == "") {
                $('#clientContactManul').val('');
                $('#fax').val($('#dupClientFax').val());
                $('#phone').val($('#dupClientPhone').val());
                $('#email').val('');
            }
        }
    });
    $('#clientContactManul').keyup(function () {
        if (moduleName != 'Imports') {
            if ($('#clientContactManul').val() == "") {
                $('#contactName').val('');
                $('#fax').val($('#dupClientFax').val());
                $('#phone').val($('#dupClientPhone').val());
                $('#email').val('');
            }
        }
    });
    $('#shipperContactClient').keyup(function () {
        if (moduleName != 'Imports') {
            if ($('#shipperContactClient').val() == "") {
                $('#shipperManualContact').val('');
                $('#shipperFaxClient').val($('#dupShipperFax').val());
                $('#shipperPhoneClient').val($('#dupShipperPhone').val());
                $('#shipperEmailClient').val('');
                $('#shipperPhone').val('');
                $('#shipperFax').val('');
                $('#shipperEmail').val('');
            }
        }
    });
    $('#shipperManualContact').keyup(function () {
        if (moduleName != 'Imports') {
            if ($('#shipperManualContact').val() == "") {
                $('#shipperContactClient').val('');
                $('#shipperFaxClient').val($('#dupShipperFax').val());
                $('#shipperPhoneClient').val($('#dupShipperPhone').val());
                $('#shipperEmailClient').val('');
                $('#shipperPhone').val('');
                $('#shipperFax').val('');
                $('#shipperEmail').val('');
            }
        }
    });
    $('#doorOriginCityZip').keyup(function () {
        var destination = $('#doorOriginCityZip').val();
        if (destination == "") {
            $('#pickupInfo').hide();
        }
    });
    $('#portOfLoading').keyup(function () {
        if (moduleName == 'Imports') {
            var portOfLoading = $('#portOfLoading').val();
            if (portOfLoading == "") {
                $('#agentName').val('');
                $('#agentNumber').val('');
                $('#supplierNameOrg').val('');
                $('#supplierCode').val('');
                $('#specialRemarks').val('');
                $('#internalRemarks').val('');
                $('#portGriRemarks').val('');
            }
        }
    });
});
var mouse_is_inside = false;
$(document).ready(function ()
{
    $('#aesBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#inbondBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#hotCodesBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#hsCodeBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#customerPoBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#warehouseDocBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#ncmNoBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#trackingBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#osdCodeBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $('#amsHblBox').hover(function () {
        mouse_is_inside = true;
    }, function () {
        mouse_is_inside = false;
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#amsHblBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#aesBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#inbondBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#hsCodeBox').hide();
    });
    //    $("body").mouseup(function(){
    //        var dupPhone=$("#dupClientPhone").val();
    //        var dupFax=$("#dupClientFax").val();
    //        if($('#clientCons').val()==''){
    //            $('#client').val('');
    //        }
    //        if(dupPhone==null || dupPhone==''){
    //            if($('#clientManual').is(":checked")  || $('#newClientContact').is(":checked")){
    //            }else{
    //                $('#phone').val('');
    //            }
    //        }
    //        if(dupFax==null || dupFax==''){
    //            if($('#clientManual').is(":checked") || $('#newClientContact').is(":checked")){
    //            }else{
    //                $('#fax').val('');
    //            }
    //        }
    //        if($('#dupShipperPhone').val()==null || $('#dupShipperPhone').val()==''){
    //            if($('#newShipper').is(":checked")  || $('#newShipperContact').is(":checked") ){
    //            }else{
    //                $('#shipperPhoneClient').val('');
    //            }
    //        }
    //        if($('#dupShipperFax').val()==null || $('#dupShipperFax').val()==''){
    //            if($('#newShipper').is(":checked") || $('#newShipperContact').is(":checked")){
    //            }else{
    //                $('#shipperFaxClient').val('');
    //            }
    //        }
    //        if($('#dupConsigneePhone').val()==null || $('#dupConsigneePhone').val()==''){
    //            if($('#newConsignee').is(":checked")  || $('#newConsigneeContact').is(":checked")){
    //            }else{
    //                $('#consigneePhoneClient').val('');
    //            }
    //        }
    //        if($('#dupConsigneeFax').val()==null || $('#dupConsigneeFax').val()==''){
    //            if($('#newConsignee').is(":checked")  || $('#newConsigneeContact').is(":checked")){
    //            }else{
    //                $('#consigneeFaxClient').val('');
    //            }
    //        }
    //    });
    $("table").mouseup(function () {
        if (!mouse_is_inside) {
            var hotCode = $("#hotCodes").val();
            if (hotCode == null || hotCode == '') {
                $('#hotCodesBox').hide();
            }
        }
    });
    //    $('#hsCodeBoxForOther').hover(function(){
    //        mouse_is_inside=true;
    //    },function(){
    //        mouse_is_inside=false;
    //    });
    //    $("body").mouseup(function(){
    //        if(! mouse_is_inside){
    //            var hsCode=$("#bookingHsCode").val();
    //            if(hsCode==null || hsCode==''){
    //                $('#hsCodeBoxForOther').hide();
    //            }
    //        }
    //    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#customerPoBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#warehouseDocBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#ncmNoBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#trackingBox').hide();
    });
    $("body").mouseup(function () {
        if (!mouse_is_inside)
            $('#osdCodeBox').hide();
    });
});
function showClient() {
    var acctno = $('#client_no').val();
    var newClient = $('#ManualClient').val();
    if (newClient != '' && acctno === '') {
        $('#clientManual').attr("checked", true);
        $('#clientContainer').hide();
        $('#clientConsContainer').hide();
        $('#clientContactManual').show();
        $('#clientContactDojo').hide();
        $('#clientText').show();
    } else {
        $('#clientManual').attr('checked', false);
        $('#clientContainer').show();
        $('#clientConsContainer').hide();
        $('#clientContactManual').hide();
        $('#clientContactDojo').show();
        $('#clientText').hide();
    }
}
function showConsContainer() {
    var clientCons = $('#clientCons').val();
    $('#client').val(clientCons);
}
function setConsigneeForClient() {
    $('#clientManual').attr("checked", false);
    $("#clientConsContainer").show();
    $("#clientText").hide();
    $("#clientContainer").hide();
}
function checkConsigneeAcctType() {
    var clientNo = $('#client_no').val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO",
            methodName: "getTradingpatnerAccType",
            param1: clientNo
        },
        async: false,
        success: function (data) {
            var array = data.split(",");
            for (var i = 0; i <= array.length - 1; i++) {
                if (array[i] == "C") {
                    $('#clientWithConsignee').attr("checked", true);
                    break;
                }
            }
        }
    });
}
function showConsigneeForClient() {
    if ($('#clientWithConsignee').is(":checked")) {
        $('#clientManual').attr("checked", false);
        $("#clientText").hide();
        $("#clientContainer").hide();
        $("#clientConsContainer").show();
        $("#client").val('');
        $('#clientCons').val('');
        $('#contactName').val('');
        $('#phone').val('');
        $('#email').val('');
        $('#fmcNo').val('');
        $('#retailCommodity').val('');
        $('#commodityNumber').val('');
        $('#otiNumber').val('');
        $('#address').val('');
        $('#fax').val('');
        $('#commodityNumber').val('');
        $("#client_no").val("");
        $("#ManualClient").val('');
        $("#clientPoaClient").val('');
        $('#clientPoa').html("");
        $('#clntNotes').css("border", "");
    } else {
        $('#clientManual').attr("checked", false);
        $("#clientText").hide();
        $("#clientContainer").show();
        $("#clientConsContainer").hide();
        $('#fmcNo').val('');
        $('#retailCommodity').val('');
        $('#commodityNumber').val('');
        $('#otiNumber').val('');
        $("#client").val('');
        $('#clientCons').val('');
        $('#contactName').val('');
        $('#phone').val('');
        $('#email').val('');
        $('#address').val('');
        $('#fax').val('');
        $('#commodityNumber').val('');
        $("#client_no").val("");
        $("#ManualClient").val('');
        $("#clientPoaClient").val('');
        $('#clientPoa').html("");
        $('#clntNotes').css("border", "");
    }
}
function newClient() {
    if ($('#clientManual').is(":checked")) {
        $('#clientWithConsignee').attr("checked", false);
        $('#newClientContact').attr("checked", true);
        $("#clientContainer").hide();
        $("#clientConsContainer").hide();
        $("#clientContactDojo").hide();
        $("#clientContactManual").show();
        $('#fmcNo').val('');
        $('#retailCommodity').val('');
        $('#commodityNumber').val('');
        $('#otiNumber').val('');
        $('#dupFax').val('');
        $('#dupClientFax').val('');
        $('#dupPhone').val('');
        $('#dupClientPhone').val('');
        $("#clientText").show();
        $("#client").val('');
        $('#clientContactManul').val('');
        $('#clientCons').val('');
        $('#contactName').val('');
        $('#phone').val('');
        $('#email').val('');
        $('#address').val('');
        $('#fax').val('');
        $('#city').val('');
        $('#state').val('');
        $('#zip').val('');
        $('#commodityNumber').val('');
        $("#client_no").val("");
        $("#ManualClient").val('');
        $("#clientPoaClient").val('');
        $('#clientPoa').html("");
        $('#creditForClient').val("");
        $('#clientCreditClient').html("");
        displayClientDetails();
    } else {
        $('#clientWithConsignee').attr("checked", false);
        $('#newClientContact').attr("checked", false);
        $("#clientText").hide();
        $('#dupFax').val('');
        $('#dupClientFax').val('');
        $('#fmcNo').val('');
        $('#retailCommodity').val('');
        $('#commodityNumber').val('');
        $('#otiNumber').val('');
        $('#dupPhone').val('');
        $('#dupClientPhone').val('');
        $("#clientContainer").show();
        $("#clientConsContainer").hide();
        $("#clientContactDojo").show();
        $("#clientContactManual").hide();
        $('#clientCons').val('');
        $("#client").val('');
        $('#clientContactManul').val('');
        $('#contactName').val('');
        $('#phone').val('');
        $('#email').val('');
        $('#address').val('');
        $('#fax').val('');
        $('#commodityNumber').val('');
        $("#ManualClient").val('');
        $("#clientPoaClient").val('');
        $('#clientPoa').html("");
        $('#creditForClient').val("");
        $('#clientCreditClient').html("");
    }
}

function showSupplier() {
    var supManual = $('#dupSupplierName').val();
    if (supManual !== '') {
        $("#dojoSupplier").hide();
        $("#manualSupplier").show();
        $("#newSupplier").attr('checked', true);
        $("#supplierName").val(supManual);
    }
}

function newSupplierName() {
    if ($('#newSupplier').is(":checked")) {
        $("#dojoSupplier").hide();
        $("#manualSupplier").show();
        $("#supplierName").val("");
        $("#dupSupplierName").val("");
        $("#supplierCode").val("");
        $("#supplierPhone").val("");
        $("#supplierFax").val("");
        $("#supplierEmail").val("");
        $("#supplierAddress").val("");
        $("#supplierCity").val("");
        $("#supplierCountry").val("");
        $("#supplierState").val("");
        $("#supplierZip").val("");
        $("#supplierPoa").val("");
        $("#supplierClientRef").val("");
        $("#supplierClient").val("");
        $("#newSupplierClientCity").val("");
    } else {
        $("#dojoSupplier").show();
        $("#manualSupplier").hide();
        $("#supplierName").val("");
        $("#dupSupplierName").val("");
        $("#supplierCode").val("");
        $("#supplierPhone").val("");
        $("#supplierFax").val("");
        $("#supplierEmail").val("");
        $("#supplierAddress").val("");
        $("#supplierCity").val("");
        $("#supplierCountry").val("");
        $("#supplierState").val("");
        $("#supplierZip").val("");
        $("#supplierPoa").val("");
        $("#supplierClientRef").val("");
        $("#supplierClient").val("");
        $("#newSupplierClientCity").val("");
    }
}

function showShipper() {
    var shipManual = $('#dupShipperName').val();
    var shippManual = $('#dupShipName').val();
    var shipper = $("#shipperName").val();
    var shipperClient = $("#shipperNameClient").val();
    if (shipManual != '' && shipper == '') {
        $("#shipperName").val(shipManual);
        $("#shipperNameClient").val(shipManual);
        $("#shipContactName").val(shipManual);
        $("#dupShipName").val(shipManual);
    }
    if (shippManual != '' && shipperClient == '') {
        $("#shipperName").val(shippManual);
        $("#shipperNameClient").val(shippManual);
        $("#shipContactName").val(shippManual);
        $("#shipContactName").val(shippManual);
        $("#dupShipName").val(shippManual);
    }
}

function newShipperName() {
    if ($('#newShipper').is(":checked")) {
        $("#dojoShipper").hide();
        $("#dojoShipp").hide();
        $("#shipperDojo").hide();
        $("#shippManual").show();
        $("#manualShipper").show();
        $("#manualShipp").show();
        $("#shipperName").val("");
        $("#dupShipName").val("");
        $("#shipperNameClient").val("");
        $('#dupShipPhone').val('');
        $('#dupShipFax').val('');
        $('#dupShipperPhone').val('');
        $('#dupShipperFax').val('');
        $("#shipperContactClient").val("");
        $("#shipperManualContact").val("");
        $("#dupShipperName").val("");
        $("#dupShipName").val("");
        $("#shipperCode").val("");
        $("#shipperContactClient").val("");
        $("#shipperPhone").val("");
        $("#shipperFax").val("");
        $("#shipperEmail").val("");
        $("#shipperAddress").val("");
        $("#shipperCity").val("");
        $("#shipperCountry").val("");
        $("#shipperState").val("");
        $("#shipperZip").val("");
        $("#shipperPoa").val("");
        $('#shipperNameClient').val("");
        $('#shipperCodeClient').val("");
        $('#shipperaccttype').val("");
        $('#shippersubtype').val("");
        $('#shipperFaxClient').val("");
        $('#shipperCityClient').val("");
        $('#shipperCountryClient').val("");
        $('#shipperStateClient').val("");
        $('#shipperZipClient').val("");
        // $('#shipperPoaClient').val("");
        $('#shipperAddressClient').val("");
        $('#shipperEmailClient').val("");
        $('#shipperPhoneClient').val("");
        $('#shipperCreditClient').html("");
        $('#shipperPoa').val("");
        $('#shipper1').html("");
        $('#shipper').html("");
        $('#shipperClientRef').val("");
        $('#shipperClient').val("");
        $('#newShipperClientCity').val("");
        $("#newShipp").attr('checked', true);
        $("#shipperSalesPersonCode").val("");
        displayShipperDetails();
    } else {
        $("#dojoShipper").show();
        $("#dojoShipp").show();
        $("#manualShipper").hide();
        $("#manualShipp").hide();
        $("#shipperDojo").show();
        $("#shippManual").hide();
        $("#shipperName").val("");
        $("#dupShipName").val("");
        $("#shipperNameClient").val("");
        $('#dupShipPhone').val('');
        $('#dupShipFax').val('');
        $('#dupShipperPhone').val('');
        $('#dupShipperFax').val('');
        $("#shipperContactClient").val("");
        $("#shipperManualContact").val("");
        $("#dupShipperName").val("");
        $("#dupShipName").val("");
        $("#shipperCode").val("");
        $("#shipperContactClient").val("");
        $("#shipperPhone").val("");
        $("#shipperFax").val("");
        $("#shipperEmail").val("");
        $("#shipperAddress").val("");
        $("#shipperCity").val("");
        $("#shipperCountry").val("");
        $("#shipperState").val("");
        $("#shipperZip").val("");
        $("#shipperPoa").val("");
        $('#shipperNameClient').val("");
        $('#shipperCodeClient').val("");
        $('#shipperaccttype').val("");
        $('#shippersubtype').val("");
        $('#shipperFaxClient').val("");
        $('#shipperCityClient').val("");
        $('#shipperCountryClient').val("");
        $('#shipperStateClient').val("");
        $('#shipperZipClient').val("");
        //$('#shipperPoaClient').val("");
        $('#shipperAddressClient').val("");
        $('#shipperEmailClient').val("");
        $('#shipperPhoneClient').val("");
        $('#shipperCreditClient').html("");
        $('#shipperPoa').val("");
        $('#shipper1').html("");
        $('#shipper').html("");
        $("#shipperName").val("");
        $("#shipperPhoneClient").val("");
        $("#shipperFaxClient").val("");
        $('#shipperClientRef').val("");
        $('#shipperClient').val("");
        $('#newShipperClientCity').val("");
        $("#newShipp").attr('checked', false);
        $("#shipperSalesPersonCode").val("");
    }
}

function newShippName() {
    if ($('#newShipp').is(":checked")) {
        $("#dojoShipper").hide();
        $("#dojoShipp").hide();
        $("#shipperDojo").hide();
        $("#shippManual").show();
        $("#manualShipper").show();
        $("#manualShipp").show();
        $("#shipperName").val("");
        $("#dupShipName").val("");
        $("#shipperNameClient").val("");
        $('#dupShipPhone').val('');
        $('#dupShipFax').val('');
        $('#dupShipperPhone').val('');
        $('#dupShipperFax').val('');
        $("#shipperContactClient").val("");
        $("#shipperManualContact").val("");
        $("#dupShipperName").val("");
        $("#dupShipName").val("");
        $("#shipperCode").val("");
        $("#shipperContactClient").val("");
        $("#shipperPhone").val("");
        $("#shipperFax").val("");
        $("#shipperEmail").val("");
        $("#shipperAddress").val("");
        $("#shipperCity").val("");
        $("#shipperCountry").val("");
        $("#shipperState").val("");
        $("#shipperZip").val("");
        $("#shipperPoa").val("");
        $('#shipperNameClient').val("");
        $('#shipperCodeClient').val("");
        $('#shipperaccttype').val("");
        $('#shippersubtype').val("");
        $('#shipperFaxClient').val("");
        $('#shipperCityClient').val("");
        $('#shipperCountryClient').val("");
        $('#shipperStateClient').val("");
        $('#shipperZipClient').val("");
        //  $('#shipperPoaClient').val("");
        $('#shipperAddressClient').val("");
        $('#shipperEmailClient').val("");
        $('#shipperPhoneClient').val("");
        $('#shipperCreditClient').html("");
        $('#shipperPoa').val("");
        $('#shipper1').html("");
        $('#shipper').html("");
        $('#shipperClientRef').val("");
        $('#shipperClient').val("");
        $('#newShipperClientCity').val("");
        $('#newShipper').attr('checked', true);
        displayShipperDetails();
    } else {
        $("#dojoShipper").show();
        $("#dojoShipp").show();
        $("#manualShipper").hide();
        $("#manualShipp").hide();
        $("#shipperDojo").show();
        $("#shippManual").hide();
        $("#shipperName").val("");
        $("#dupShipName").val("");
        $("#shipperNameClient").val("");
        $('#dupShipPhone').val('');
        $('#dupShipFax').val('');
        $('#dupShipperPhone').val('');
        $('#dupShipperFax').val('');
        $("#shipperContactClient").val("");
        $("#shipperManualContact").val("");
        $("#dupShipperName").val("");
        $("#dupShipName").val("");
        $("#shipperCode").val("");
        $("#shipperContactClient").val("");
        $("#shipperPhone").val("");
        $("#shipperFax").val("");
        $("#shipperEmail").val("");
        $("#shipperAddress").val("");
        $("#shipperCity").val("");
        $("#shipperCountry").val("");
        $("#shipperState").val("");
        $("#shipperZip").val("");
        $("#shipperPoa").val("");
        $('#shipperNameClient').val("");
        $('#shipperCodeClient').val("");
        $('#shipperaccttype').val("");
        $('#shippersubtype').val("");
        $('#shipperFaxClient').val("");
        $('#shipperCityClient').val("");
        $('#shipperCountryClient').val("");
        $('#shipperStateClient').val("");
        $('#shipperZipClient').val("");
        //   $('#shipperPoaClient').val("");
        $('#shipperAddressClient').val("");
        $('#shipperEmailClient').val("");
        $('#shipperPhoneClient').val("");
        $('#shipperCreditClient').html("");
        $('#shipperPoa').val("");
        $('#shipper1').html("");
        $('#shipper').html("");
        $("#shipperName").val("");
        $("#shipperPhoneClient").val("");
        $("#shipperFaxClient").val("");
        $('#shipperClientRef').val("");
        $('#shipperClient').val("");
        $('#newShipperClientCity').val("");
        $('#newShipper').attr('checked', false);
    }
}
function newShipperChange() {
    var shipName = $('#dupShipperName').val();
    $('#dupShipName').val(shipName);
}
function newShippChange() {
    var shipName = $('#dupShipName').val();
    $('#dupShipperName').val(shipName);
}
function showConsignee() {
    var consManual = $('#dupConsigneeName').val();
    var conssManual = $('#dupConsName').val();
    var consignee = $("#consigneeName").val();
    var consigneeClient = $("#consigneeNameClient").val();
    if (consManual != '' && consignee == '') {
        $("#consigneeName").val(consManual);
        $("#consigneeNameClient").val(consManual);
    }
    if (conssManual != '' && consigneeClient == '') {
        $("#consigneeName").val(conssManual);
        $("#consigneeNameClient").val(conssManual);
    }
}
//set checkBox Value Manual check or unCheck for Shipper party
function showShipManualCheck() {
    var acctno = $('#shipperCode').val();
    var shipperManual = $('#dupShipperName').val();
    if (shipperManual !== '' && acctno === '') {
        $('#newShipper').attr("checked", true);
        $('#dojoShipper').hide();
        $('#dojoShipp').hide();
        $('#manualShipper').show();
        $('#manualShipp').show();
        $('#shipperDojo').hide();
        $('#shippManual').show();
        $('#newShipp').attr('checked', true);
    } else {
        $('#dupShipperName').val('');
        $('#newShipper').attr("checked", false);
        $('#dojoShipper').show();
        $('#manualShipper').hide();
        $('#shipperDojo').show();
        $('#shippManual').hide();
    }
}
//set checkBox Value Manual check or unCheck for Consignee party
function showConsManualCheck() {
    var acctno = $('#consigneeCode').val();
    var consigneeManual = $('#dupConsigneeName').val();
    if (consigneeManual !== '' && acctno === '') {
        $('#newConsignee').attr("checked", true);
        $("#dojoConsignee").hide();
        $("#manualConsignee").show();
    } else {
        $('#dupConsigneeName').val('');
        $('#newConsignee').attr("checked", false);
        $("#manualConsignee").hide();
        $("#dojoConsignee").show();
    }
}
//set checkBox Value Manual check or unCheck for notify party
function showNotifyManualCheck() {
    var notifyManual = $('#dupNotifyName').val();
    if (notifyManual !== '' && $('#notifyCode').val() === '') {
        $("#dojoNotify").hide();
        $("#manualNotify").show();
        $("#newNotify").attr('checked', true);
    } else {
        $("#dupNotifyName").val('');
        $("#newNotify").attr('checked', false);
        $("#manualNotify").hide();
        $("#dojoNotify").show();
    }
}
function newConsigneeName() {
    if ($('#newConsignee').is(":checked")) {
        $("#dojoConsignee").hide();
        $("#dojoConsign").hide();
        $("#consigneeDojo").hide();
        $("#consigneeManual").show();
        $("#manualConsignee").show();
        $("#manualConsign").show();
        $("#consigneeName").val("");
        $('#dupConsFax').val('');
        $('#dupConsName').val('');
        $('#dupConsPhone').val('');
        $('#dupConsigneeFax').val('');
        $('#dupConsigneePhone').val('');
        $("#consigneeNameClient").val("");
        $("#dupConsigneeName").val("");
        $("#dupConsName").val("");
        $("#consigneeCode").val("");
        $("#consigneeContactName").val("");
        $("#consigneePhone").val("");
        $("#consigneeFax").val("");
        $("#consigneeEmail").val("");
        $("#consigneeAddress").val("");
        $("#consigneeCity").val("");
        $("#consigneeCountry").val("");
        $("#consigneeState").val("");
        $("#consigneeZip").val("");
        $("#consigneePoa").val("");
        $('#consigneeNameClient').val("");
        $('#consigneeCodeClient').val("");
        $('#consigneeaccttype').val("");
        $('#consigneesubtype').val("");
        $('#consigneeFaxClient').val("");
        $('#consigneeCityClient').val("");
        $('#consigneeCountryClient').val("");
        $('#consigneeStateClient').val("");
        $('#consigneeZipClient').val("");
        $('#consigneePoa').val("");
        $('#consigneeAddressClient').val("");
        $('#consigneeEmailClient').val("");
        $('#consigneePhoneClient').val("");
        $('#consigneeCreditClient').html("");
        //$('#consigneePoaClient').val("");
        $('#consigneeContactName').val("");
        $('#consigneeManualContact').val("");
        $('#consignee1').html("");
        $('#consignee').html("");
        $('#consigneeClientRef').val("");
        $('#consigneeClient').val("");
        $('#newConsigneeClientCity').val("");
        $("#newConsign").attr('checked', true);
        $('#consigneeSalesPersonCode').val("");
        displayConsigneeDetails();
    } else {
        $("#dojoConsignee").show();
        $("#dojoConsign").show();
        $("#manualConsignee").hide();
        $("#manualConsign").hide();
        $("#consigneeDojo").hide();
        $("#consigneeManual").show();
        $("#consigneeName").val("");
        $('#dupConsFax').val('');
        $('#dupConsName').val('');
        $('#dupConsPhone').val('');
        $('#dupConsigneeFax').val('');
        $('#dupConsigneePhone').val('');
        $("#consigneeNameClient").val("");
        $("#dupConsigneeName").val("");
        $("#dupConsName").val("");
        $("#consigneeCode").val("");
        $("#consigneeContactName").val("");
        $("#consigneePhone").val("");
        $("#consigneeFax").val("");
        $("#consigneeEmail").val("");
        $("#consigneeAddress").val("");
        $("#consigneeCity").val("");
        $("#consigneeCountry").val("");
        $("#consigneeState").val("");
        $("#consigneeZip").val("");
        $("#consigneePoa").val("");
        $('#consigneeNameClient').val("");
        $('#consigneeCodeClient').val("");
        $('#consigneeaccttype').val("");
        $('#consigneesubtype').val("");
        $('#consigneeFaxClient').val("");
        $('#consigneeCityClient').val("");
        $('#consigneeCountryClient').val("");
        $('#consigneeStateClient').val("");
        $('#consigneeZipClient').val("");
        // $('#consigneePoaClient').val("");
        $('#consigneeAddressClient').val("");
        $('#consigneeEmailClient').val("");
        $('#consigneePhoneClient').val("");
        $('#consigneeCreditClient').html("");
        $('#consigneePoa').val("");
        $('#consigneeContactName').val("");
        $('#consigneeManualContact').val("");
        $('#consignee1').html("");
        $('#consignee').html("");
        $('#consigneeClientRef').val("");
        $('#consigneeClient').val("");
        $('#newConsigneeClientCity').val("");
        $("#newConsign").attr('checked', false);
        $('#consigneeSalesPersonCode').val("");
    }
}

function newConsignName() {
    if ($('#newConsign').is(":checked")) {
        $("#dojoConsignee").hide();
        $("#dojoConsign").hide();
        $("#consigneeDojo").hide();
        $("#consigneeManual").show();
        $("#manualConsignee").show();
        $("#manualConsign").show();
        $("#consigneeName").val("");
        $('#dupConsFax').val('');
        $('#dupConsName').val('');
        $('#dupConsPhone').val('');
        $('#dupConsigneeFax').val('');
        $('#dupConsigneePhone').val('');
        $("#consigneeNameClient").val("");
        $("#dupConsigneeName").val("");
        $("#dupConsName").val("");
        $("#consigneeCode").val("");
        $("#consigneeContactName").val("");
        $("#consigneePhone").val("");
        $("#consigneeFax").val("");
        $("#consigneeEmail").val("");
        $("#consigneeAddress").val("");
        $("#consigneeCity").val("");
        $("#consigneeCountry").val("");
        $("#consigneeState").val("");
        $("#consigneeZip").val("");
        $("#consigneePoa").val("");
        $('#consigneeNameClient').val("");
        $('#consigneeCodeClient').val("");
        $('#consigneeaccttype').val("");
        $('#consigneesubtype').val("");
        $('#consigneeFaxClient').val("");
        $('#consigneeCityClient').val("");
        $('#consigneeCountryClient').val("");
        $('#consigneeStateClient').val("");
        $('#consigneeZipClient').val("");
        // $('#consigneePoaClient').val("");
        $('#consigneeAddressClient').val("");
        $('#consigneeEmailClient').val("");
        $('#consigneePhoneClient').val("");
        $('#consigneeCreditClient').html("");
        $('#consigneePoa').val("");
        $('#consigneeContactName').val("");
        $('#consigneeManualContact').val("");
        $('#consignee1').html("");
        $('#consignee').html("");
        $('#consigneeClientRef').val("");
        $('#consigneeClient').val("");
        $('#newConsigneeClientCity').val("");
        $('#newConsignee').attr('checked', true);
        displayConsigneeDetails();
    } else {
        $("#dojoConsignee").show();
        $("#dojoConsign").show();
        $("#manualConsignee").hide();
        $("#manualConsign").hide();
        $("#consigneeDojo").hide();
        $("#consigneeManual").show();
        $("#consigneeName").val("");
        $('#dupConsFax').val('');
        $('#dupConsName').val('');
        $('#dupConsPhone').val('');
        $('#dupConsigneeFax').val('');
        $('#dupConsigneePhone').val('');
        $("#consigneeNameClient").val("");
        $("#dupConsigneeName").val("");
        $("#dupConsName").val("");
        $("#consigneeCode").val("");
        $("#consigneeContactName").val("");
        $("#consigneePhone").val("");
        $("#consigneeFax").val("");
        $("#consigneeEmail").val("");
        $("#consigneeAddress").val("");
        $("#consigneeCity").val("");
        $("#consigneeCountry").val("");
        $("#consigneeState").val("");
        $("#consigneeZip").val("");
        $("#consigneePoa").val("");
        $('#consigneeNameClient').val("");
        $('#consigneeCodeClient').val("");
        $('#consigneeaccttype').val("");
        $('#consigneesubtype').val("");
        $('#consigneeFaxClient').val("");
        $('#consigneeCityClient').val("");
        $('#consigneeCountryClient').val("");
        $('#consigneeStateClient').val("");
        $('#consigneeZipClient').val("");
        // $('#consigneePoaClient').val("");
        $('#consigneeAddressClient').val("");
        $('#consigneeEmailClient').val("");
        $('#consigneePhoneClient').val("");
        $('#consigneeCreditClient').html("");
        $('#consigneePoa').val("");
        $('#consigneeContactName').val("");
        $('#consigneeManualContact').val("");
        $('#consignee1').html("");
        $('#consignee').html("");
        $('#consigneeClientRef').val("");
        $('#consigneeClient').val("");
        $('#newConsigneeClientCity').val("");
        $('#newConsignee').attr('checked', false);
    }
}
function newConsigneeChange() {
    var newConsignee = $("#dupConsigneeName").val();
    $("#dupConsName").val(newConsignee);
}
function newConsignChange() {
    var newConsignee = $("#dupConsName").val();
    $("#dupConsigneeName").val(newConsignee);
}
function newNotifyName() {
    if ($('#newNotify').is(":checked")) {
        $("#dojoNotify").hide();
        $("#manualNotify").show();
        $("#notifyName").val("");
        $("#dupNotifyName").val("");
        $("#notifyCode").val("");
        $("#notifyContactName").val("");
        $("#notifyPhone").val("");
        $("#notifyFax").val("");
        $("#notifyEmail").val("");
        $("#notifyAddress").val("");
        $("#notifyCity").val("");
        $("#notifyCountry").val("");
        $("#notifyState").val("");
        $("#notifyZip").val("");
        $("#notifyPoa").val("");
        $("#notifyClientRef").val("");
        $("#notifyClient").val("");
        $("#newNotifyClientCity").val("");
        $('#notifySalesPersonCode').val("");
    } else {
        $("#dojoNotify").show();
        $("#manualNotify").hide();
        $("#notifyName").val("");
        $("#dupNotifyName").val("");
        $("#notifyCode").val("");
        $("#notifyContactName").val("");
        $("#notifyPhone").val("");
        $("#notifyFax").val("");
        $("#notifyEmail").val("");
        $("#notifyAddress").val("");
        $("#notifyCity").val("");
        $("#notifyCountry").val("");
        $("#notifyState").val("");
        $("#notifyZip").val("");
        $("#notifyPoa").val("");
        $("#notifyClientRef").val("");
        $("#notifyClient").val("");
        $("#newNotifyClientCity").val("");
        $('#notifySalesPersonCode').val("");
    }
}
function addHotCode3pRef(fileId) {//add hot code
    var hotcodeArray = $('#hotCodes').val().split("/");
    var moduleName = $('#moduleName').val();
    if (hotcodeArray[0] === "XXX" && moduleName === "Exports") {
        showAlternateMask();
        $("#add-hotCodeComments-container").center().show(500, function () {
            $('#headingComments').text('Enter a required explanation for adding the value "XXX" Hot Code');
            $('#hotCodeComments').val('');
            $('#hiddenDeleteHotCodeFlag').val('');
            $('#3pRefId').val('');
        });
    } else {
        addGeneralInfOn3pRef("hotCodesBox", "hotCodesList", "hotCodes", "hotCodes", fileId, "");
    }
}
function addHotCodeXXX3pRef() {
    var hotCodeXXXComments = $('#hotCodeComments').val();
    if (hotCodeXXXComments === null || hotCodeXXXComments === "") {
        $.prompt("Hot Code XXX Comments is required");
        return false;
    }
    var deleteFlag = $('#hiddenDeleteHotCodeFlag').val();
    var fileId = $("#fileNumberId").val();
    $("#add-hotCodeComments-container").center().hide(500, function () {
        hideAlternateMask();
    });
    if (deleteFlag === "deleteFlag") {
        submitAjaxFormforGeneral("deleteLcl3pReference", "hotCodes", $('#3pRefId').val(), "hotCodesList", hotCodeXXXComments);
    } else {
        addGeneralInfOn3pRef("hotCodesBox", "hotCodesList", "hotCodes", "hotCodes", fileId, hotCodeXXXComments);
    }
}
function cancelHotCodeXXXComments() {
    $("#add-hotCodeComments-container").center().hide(500, function () {
        $('#hotCodes').val('');
        $('#hotCodesBox').hide();
        hideAlternateMask();
    });
}
function addGeneralInfOn3pRef(hideSelectorText, selector, refType, refValue, fileId, comments) {
    var brand = getTradingPartnerPriority();
    showProgressBar();
    $('#' + hideSelectorText).hide();
    $('#methodName').val('addLcl3pReference');
    var params = $("#lclBookingForm").serialize();
    var releaseClass = $("#lclReleaseButton1").attr('class') === 'green-background' ? true : false;
    params += "&thirdPName=" + refType + "&fileNumberId=" + fileId;
    params += "&refValue=" + $('#' + refValue).val() + "&hotCodeXXXComments=" + comments;
    $.post($("#lclBookingForm").attr("action"), params, function (data) {
        $("#" + selector).html(data);
        $("#" + selector, window.parent.document).html(data);
        hideProgressBar();
        if ($('#moduleName').val() === 'Exports' && refType == "hotCodes") {
            isValidateNotesColor();
        }
        if (comments !== '' && !releaseClass) {
            $("#holdButton1").removeClass("button-style1").addClass("red-background");
            $("#holdButton2").removeClass("button-style1").addClass("red-background");
            $("#holdButton1").text("UnHold");
            $("#holdButton2").text("UnHold");
            $("#hold").val("Y");
        }
        $('#' + refValue).val('');
    });
}
function isValidateNotesColor() {//Mantis#11329
    if ($('#hotCodeFlagId').val() === 'true') {
        $('.notes').removeClass('button-style1');
        $('.notes').addClass('green-background');
    } else {
        $('.notes').removeClass('green-background');
        $('.notes').addClass('button-style1');
    }
}
function submitPopupBox(tar, methodName, fileNumberId, selector, hsCodeFlag) {//hs code
    if (methodName === 'addLclBookingHsCode' && (hsCodeFlag === 'hsCode1' || hsCodeFlag === 'hsCode')) {
        $.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHsCodeDAO",
                methodName: "hsCodeAlreadyExist",
                param1: fileNumberId,
                param2: $("#hsCode").val().trim(),
                param3: $("#bookingHsCode").val().trim()
            },
            success: function (data) {
                if (data === 'true' && $('#bookingHsCodeId').val().trim() === '') {
                    $.prompt("This DR - " + $('#fileNumber').val() + " already has same HS code");
                    return false;
                } else {
                    showProgressBar();
                    //xxxx.xx
                    var regexp = /^\d{4}\.\d{2}$/i;
                    //xxxx.xx.xxxx
                    var regexp1 = /^\d{4}\.\d{2}\.\d{4}$/i;
                    if (hsCodeFlag !== 'hsCode') {
                        if ($('#hsCode').val() === null || $('#hsCode').val() === "") {
                            $.prompt('Code is required');
                            $("#hsCode").css("border-color", "red");
                            hideProgressBar();
                        } else if (!regexp.exec($('#hsCode').val()) && !regexp1.exec($('#hsCode').val())) {
                            $.prompt('HS Code should be in format NNNN.NN or NNNN.NN.NNNN');
                            $("#hsCode").css("border-color", "red");
                            hideProgressBar();
                        } else {
                            $(tar).hide("");
                            $('#methodName').val(methodName);
                            var params = $("#lclBookingForm").serialize();
                            params += "&thirdPName=" + hsCodeFlag + "&fileNumberId=" + fileNumberId;
                            $.post($("#lclBookingForm").attr("action"), params, function (data) {
                                $(selector).html(data);
                                $(selector, window.parent.document).html(data);
                                if (hsCodeFlag !== 'hsCode') {
                                    $('#' + hsCodeFlag).val('');
                                }
                                hideProgressBar();
                            });
                        }
                    } else {
                        if ($('#bookingHsCode').val() === null || $('#bookingHsCode').val() === "") {
                            $.prompt('Code is required');
                            $("#bookingHsCode").css("border-color", "red");
                            hideProgressBar();
                        } else if (!regexp.exec($('#bookingHsCode').val()) && !regexp1.exec($('#bookingHsCode').val())) {
                            $.prompt('HS Code should be in format NNNN.NN or NNNN.NN.NNNN');
                            $("#bookingHsCode").css("border-color", "red");
                            hideProgressBar();
                        } else if ($('#hsCodePiece').val() === null || $('#hsCodePiece').val() === "") {
                            $.prompt('Piece is required');
                            $("#hsCodePiece").css("border-color", "red");
                            hideProgressBar();
                        } else if ($('#hsCodeWeightMetric').val() === null || $('#hsCodeWeightMetric').val() === "") {
                            $.prompt('Weight is required');
                            $("#hsCodeWeightMetric").css("border-color", "red");
                            hideProgressBar();
                        } else if ($('#packageType').val() === null || $('#packageType').val() === "") {
                            $.prompt('packageType is required');
                            $("#packageType").css("border-color", "red");
                            hideProgressBar();
                        } else {
                            $(tar).hide("");
                            $("#bookingHsCode").css("border-color", "");
                            $("#hsCodePiece").css("border-color", "");
                            $("#hsCodeWeightMetric").css("border-color", "");
                            $("#packageType").css("border-color", "");
                            $('#methodName').val(methodName);
                            var params = $("#lclBookingForm").serialize();
                            params += "&thirdPName=" + hsCodeFlag + "&fileNumberId=" + fileNumberId;
                            $.post($("#lclBookingForm").attr("action"), params, function (data) {
                                $(selector).html(data);
                                $(selector, window.parent.document).html(data);
                                if (hsCodeFlag != 'osdRemarks') {
                                    $('#' + hsCodeFlag).val("");
                                }
                                if (hsCodeFlag == 'hsCode') {
                                    var unLocationCode = getDestination();
                                    var fileNumberId = $('#fileNumberId').val();
                                    jQuery.ajaxx({
                                        dataType: "json",
                                        data: {
                                            className: "com.gp.cong.lcl.dwr.LclDwr",
                                            methodName: "checkEmptyForPortsHsCode",
                                            param1: unLocationCode,
                                            param2: fileNumberId,
                                            dataType: "json"
                                        },
                                        async: false,
                                        success: function (data) {
                                            if (data != null && data != undefined && data != "") {
                                                var pieces = data.split("\n"), part;
                                                for (var i = 0; i < (pieces.length - 1); i++) {
                                                    var data1 = pieces[i];
                                                    $('#bookingHsCode').val(data1.substring(0, data1.indexOf('/')));
                                                    $('#bookingHsCodeId').val(data1.substring(data1.indexOf('/') + 1));
                                                    $('#hsCodePiece').val('');
                                                    $('#hsCodeWeightMetric').val('');
                                                    $('#packageType').val('');
                                                    $('#packageTypeId').val('');
                                                    $('#hsCodeBoxForOther').show();
                                                }
                                            }
                                        }
                                    });
                                }
                                hideProgressBar();
                            });
                        }
                    }
                }
            }
        });
    } else {
        showProgressBar();
        if (hsCodeFlag !== 'hsCode') {
            $(tar).hide("");
            $('#methodName').val(methodName);
            var params = $("#lclBookingForm").serialize();
            params += "&thirdPName=" + hsCodeFlag + "&fileNumberId=" + fileNumberId;
            $.post($("#lclBookingForm").attr("action"), params, function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                if (hsCodeFlag === 'hotCodes') {
                    $('#methodName').val("refreshCommodity");
                    var params = $("#lclBookingForm").serialize();
                    params += "fileNumberId=" + fileNumberId;
                    $.post($("#lclBookingForm").attr("action"), params, function (data) {
                        $("#commodityDesc").html(data);
                        $("#commodityDesc", window.parent.document).html(data);
                    });
                }
                if (hsCodeFlag !== 'hsCode') {
                    $('#' + hsCodeFlag).val('');
                }
                hideProgressBar();
            });
        }
    }
}
function deleteHotCode(txt, id) {
    var refValue = $("#hotCodesRef" + id).val();
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                var hotcodeArray = refValue.split("/");
                var moduleName = $('#moduleName').val();
                if (hotcodeArray[0] === "XXX" && moduleName === "Exports") {
                    showAlternateMask();
                    $("#add-hotCodeComments-container").center().show(500, function () {
                        $('#headingComments').text('Enter a required explanation for removing the value "XXX" Hot Code');
                        $('#hotCodeComments').val('');
                        $('#hiddenDeleteHotCodeFlag').val('deleteFlag');
                        $('#3pRefId').val(id);
                    });
                } else {
                    submitAjaxFormforGeneral("deleteLcl3pReference", "hotCodes", id, "hotCodesList", "");
                }
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function deleteCustPo(txt, id) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                submitAjaxFormforGeneral("deleteLcl3pReference", "customerPo", id, "customerPoList", "");
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function deleteNcm(txt, id) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                submitAjaxFormforGeneral("deleteLcl3pReference", "ncmNo", id, "ncmNoList", "");
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function deleteWarehouseDoc(txt, id) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                submitAjaxFormforGeneral("deleteLcl3pReference", "wareHouseDoc", id, "warehouseList", "");
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function deleteHsCode(txt, id) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                submitAjaxFormforGeneral("deleteLclBookingHsCode", "hsCode", id, "hsCodeList", "");
                $('#hsCode').val('');
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function deleteTracking(txt, id) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                submitAjaxFormforGeneral("deleteLcl3pReference", "tracking", id, "trackingList", "");
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function submitAjaxFormforGeneral(methodName, thirdPName, thirdpRefId, refreshDivId, comments) {
    showProgressBar();
    $('#methodName').val(methodName);
    var params = $("#lclBookingForm").serialize();
    var fileNumberId = $("#fileNumberId").val();
    params += "&thirdPName=" + thirdPName + "&fileNumberId=" + fileNumberId;
    params += "&lcl3pRefId=" + thirdpRefId + "&hotCodecomments=" + comments;
    $.post($("#lclBookingForm").attr("action"), params, function (data) {
        $('#' + refreshDivId).html(data);
        $("#" + refreshDivId, window.parent.document).html(data);
        hideProgressBar();
        if ($('#moduleName').val() === 'Exports' && thirdPName === "hotCodes") {
            isValidateNotesColor();
        }
    });
}
function deleteAmsHbl(txt, id) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                submitAjaxFormforGeneral("deleteImpAmsHBL", "Ams", id, "amsHblList", "");
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}

function setBkgVoyageDetails() {//set booked voyage Details
    var masterSchdNo = $("#masterScheduleNo").val();
    var existMasterSchd = $("#existMasterSchd").val();
    if (existMasterSchd === "" || masterSchdNo === "" || masterSchdNo === existMasterSchd) {
        setMasterSchdValues();
    } else {
        differMasterSchd('Are you sure you want to change voyage?');
    }
}
function differMasterSchd(txt) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                setMasterSchdValues();
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}

//for ETA FD
function etaFdStatus() {
    var pickedIdValue = $('#pickedId').val();
    var fdEtaValue = $('#fdEta').val();
    var podFdTT = $("#podfdtrans").val();
    if (podFdTT != null && podFdTT != "" && podFdTT != undefined) {
        if (pickedIdValue != null && pickedIdValue != "" && pickedIdValue != undefined) {
            var addPickedId = addPodFdTTandEtaPod(podFdTT, pickedIdValue);
            $('#pickedId').val(addPickedId);
        } else if (fdEtaValue != null && fdEtaValue != "" && fdEtaValue != undefined) {
            var addFdEta = addPodFdTTandEtaPod(podFdTT, fdEtaValue);
            $('#fdEta').val(addFdEta);
        }
    }
}

function addPodFdTTandEtaPod(podFdTT, date) {
    var etaFd = new Date(new Date(date).getTime() + (podFdTT * 24 * 60 * 60 * 1000));
    var monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var addEtaFd = etaFd.getDate() + "-" + monthNames[etaFd.getMonth()] + "-" + etaFd.getFullYear();
    return addEtaFd;
}

function setMasterSchdValues() {
    var voyage = $('input:radio[name=voyageRadio]:checked').val();
    var voy = voyage.split('==');
    var haz = $('.hazmat').val();
    var originLrd = voy[7];
    var date = voy[12];
    //for ETA FD
    var podFdTT = $("#podfdtrans").val();
    if (podFdTT != null && podFdTT != "" && podFdTT != undefined) {
        var etaFd1 = addPodFdTTandEtaPod(podFdTT, date);
        $('#fdEta').val(etaFd1);
    } else {
        $('#fdEta').val(voy[6]);
    }
    $('#selectedLrd').val(originLrd);
    $('#masterScheduleNo').val(voy[0]);
    $('#eciVoyage').val(voy[1]);
    $('#sailDate').val(voy[2]);
    $('#ssLine').val(voy[3]);
    $('#vesselName').val(voy[4]);
    $('#ssVoyage').val(voy[5]);
    $('#originLrd').val(originLrd);
    $('#pier').val(voy[11]);
    $('#pierName').attr("title", voy[10]);
    if ($('#moduleName').val() === "Imports") {
        $('#transSailing').text(voy[1] + " / " + voy[2]);
    }
}

function setLoad() {
    $('#originLrd').val('');
}

function editInvoiceCharge(path, fileNumberId, fileNumber, fileNumberStatus, buttonValue, id) {///remove this method
    var destination = getDestination();
    var href = path + "/lclCostAndCharge.do?methodName=editInvoiceCharge&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber;
    href = href + "&destination=" + destination + "&buttonValue=" + buttonValue + "&fileNumberStatus=" + fileNumberStatus + "&id=" + id;
    $(".costAndCharge").attr("href", href);
    $(".costAndCharge").colorbox({
        iframe: true,
        width: "70%",
        height: "70%",
        title: "Charges"
    });
}
/* START:Add,Edit & Delete Charges and Cost */
function addCharge(path, fileNumberId, fileNumber, fileNumberStatus, buttonValue, id) {
    var moduleName = $('#moduleName').val();
    var count = new Array();
    var bkgBillToParty = $('input:radio[name=pcBoth]:checked').val();
    var bkgBillingType = $('input:radio[name=billForm]:checked').val();
    $(".chargeAmount").each(function () {
        count.push($(this).text().trim());
    });
    if (count.length >= 25 && buttonValue === 'addCharge' && moduleName === 'Imports') {
        $.prompt("More than 25 charges is not allowed");
    } else if (count.length >= 12 && buttonValue === 'addCharge' && moduleName === 'Exports') {
        $.prompt("More than 12 charges is not allowed");
    } else {
        var title = buttonValue === "addCost" ? "Add Cost" : "Add Charge";
        var destination = getDestination();
        var href = path + "/lclCostAndCharge.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&destination=" + destination;
        href = href + "&buttonValue=" + buttonValue + "&fileNumberStatus=" + fileNumberStatus + "&moduleName=" + moduleName + "&manualEntry=true";
        href = href + "&bkgBillToParty=" + bkgBillToParty + "&bkgBillingType=" + bkgBillingType;
        $("#" + id).attr("href", href);
        $("#" + id).colorbox({
            iframe: true,
            width: "70%",
            height: "70%",
            title: title,
            onClosed: function () {
                $("#chargeDesc").find("[title != '']").not("link").tooltip();
            }
        });
    }
}

function editCharge(path, id, fileNumberId, fileNumber, fileNumberStatus, manualEntry, costStatus, chargeInvoiceNumber) {
    var moduleName = $('#moduleName').val();
    var bkgBillToParty = $('input:radio[name=pcBoth]:checked').val();
    var bkgBillingType = $('input:radio[name=billForm]:checked').val();
    var cfsWarehouseNo = $('#cfsWarehouseNo').val();
    var href = path + "/lclCostAndCharge.do?methodName=editCharge&fileNumberId=" + fileNumberId;
    href = href + "&fileNumber=" + fileNumber + "&id=" + id + "&moduleName=" + moduleName + "&manualEntry=" + manualEntry;
    href = href + "&fileNumberStatus=" + fileNumberStatus + "&costStatus=" + costStatus + "&chargeInvoiceNumber=" + chargeInvoiceNumber;
    href = href + "&cfsWarehouseNo=" + cfsWarehouseNo + "&bkgBillToParty=" + bkgBillToParty + "&bkgBillingType=" + bkgBillingType;
    $(".costAndCharge").attr("href", href);
    $(".costAndCharge").colorbox({
        iframe: true,
        width: "70%",
        height: "70%",
        title: "Edit Charge",
        onClosed: function () {
            $("#chargeDesc").find("[title != '']").not("link").tooltip();
        }
    });
}
function deleteCharge(txt, id, fileNumberId, costStatus, fileNumberStatus) {
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                $("#methodName").val("deleteCharge");
                var params = $("#lclBookingForm").serialize();
                var moduleName = $('#moduleName').val();
                params += "&cid=" + id + "&fileNumberId=" + fileNumberId;
                params += "&moduleName=" + moduleName + "&costStatus=" + costStatus + "&fileNumberStatus=" + fileNumberStatus;
                $.post($("#lclBookingForm").attr("action"), params,
                        function (data) {
                            $("#chargeDesc").html(data);
                            $("#chargeDesc", window.parent.document).html(data);
                            $("#chargeDesc").find("[title != '']").not("link").tooltip();
                            hideProgressBar();
                        });
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function getUnlocationCode(selectorName) {
    var unlocCode = "";
    var obj = document.getElementById(selectorName);
    if (obj !== undefined && obj !== null && obj.value !== "") {
        unlocCode = document.getElementById(selectorName).value;
    }
    if (unlocCode.lastIndexOf("(") > -1 && unlocCode.lastIndexOf(")") > -1) {
        return unlocCode.substring(unlocCode.lastIndexOf("(") + 1, unlocCode.lastIndexOf(")"));
    }
    return "";
}
/* END:Add,Edit & Delete Charges and Cost */
function calculateCharge(radioValue, doorOriginCityZip, pickupReadyDate) {//calculate rates
    var fileId = $("#fileNumberId").val();
    if (fileId !== "") {
        var module = $('#moduleName').val();
        var origin = getUnlocationCode('portOfOriginR');
        var pol = getUnlocationCode('portOfLoading');
        var pod = getUnlocationCode('portOfDestination');
        var destination = getUnlocationCode('finalDestinationR');

        var methodName = "calculateCharges";
        if (module === "Imports") {
            methodName = "calculateImportCharges";
        }
        var spotRate = $('input:radio[name=spotRate]:checked').val();
        if ("Exports" === module && spotRate === 'Y') {
            $.prompt('Cannot change rates as Spot Rate is set to Yes');
            return false;
        }
        var insurance = $('input:radio[name=insurance]:checked').val();
        if (module !== "Imports" && insurance === "Y" && (document.getElementById('valueOfGoods') === null || document.getElementById('valueOfGoods').value === "")) {
            $.prompt("Please enter the Value of Goods");
            $("#valueOfGoods").css("border-color", "red");
            $("#warning").show();
        } else if (document.getElementById("commObj") === null) {
            $.prompt("Please add Weight and Measure from Commodity");
        } else {
            $.prompt("Are you sure want to recalculate Rates?", {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        var origin = getUnlocationCode('portOfOriginR');
                        var destination = getUnlocationCode('finalDestinationR');
                        submitAjaxForm(methodName, '#lclBookingForm', '#chargeDesc', origin, destination, pol, pod, radioValue, doorOriginCityZip, pickupReadyDate);
                        $('#showAstar').show();
                        $('#showAstarDestn').show();
                        if ($('#portOfOriginR').val() !== "") {
                            $('#portOfOriginR').addClass("text-readonly");
                            $('#portOfOriginR').attr("readonly", true);
                            var origin = $("#portOfOriginR").val();
                            $(".ChargeOrigin").text(origin.substring(0, origin.indexOf("/")));
                        }
                        if (module !== "Imports") {
//                            $('#finalDestinationR').addClass("text-readonly");
//                            $('#finalDestinationR').attr("readonly", true);
                            var destination = $("#finalDestinationR").val();
                            $(".ChargeDest").text(destination.substring(0, destination.indexOf("/")));
                            var deliveryMetro = $('input:radio[name=deliveryMetro]:checked').val();
                            $('#deliveryMetroField').val(deliveryMetro);
                        }
                        $.prompt.close();
                    } else {
                        $.prompt.close();
                    }
                }
            });
        }
    }
}

function copyDr(txt, path, fileNumberId) {
    var moduleName = $('#moduleName').val();
    var $impVoyageFlag = $('#impSearchFlag').val();
    var $voyageHeaderId = $("#headerId").val();
    var $voyageUnitId = $("#unitId").val();
    if (moduleName === 'Imports') {
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 0
            },
            submit: function (v) {
                showProgressBar();
                var href = path + "/lclBooking.do?methodName=copyDr" + "&fileNumberId=" + fileNumberId +
                        "&moduleName=" + moduleName + "&allowVoyageCopy=" + v + "&impSearchFlag=" + $impVoyageFlag
                href += "&headerId=" + $voyageHeaderId + "&unitId=" + $voyageUnitId;
                window.location = href;
                $.prompt.close();
            }
        });
    } else {
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 0
            },
            submit: function (v) {
                $.prompt("Do you also want to copy the rates?", {
                    buttons: {
                        Yes: 1,
                        No: 0
                    },
                    submit: function (v) {
                        showProgressBar();
                        var href = path + "/lclBooking.do?methodName=copyDr" + "&fileNumberId=" + fileNumberId +
                                "&moduleName=" + moduleName + "&copyRates=copyDrRates" + "&allowVoyageCopy=" + v;
                        window.location = href;
                        $.prompt.close();
                    }
                });
            }
        });
    }
}

function changeAgentStyle() {
    var agentRadioVal = $('input:radio[name=defaultAgent]:checked').val();
    if (agentRadioVal == "N") {
        document.getElementById('agentName').className = "text";
        document.getElementById('agentName').readOnly = false;
    }
}
function displayOsdBox() {//display osd box hide or show
    var osdValues = $('input:radio[name=overShortdamaged]:checked').val();
    if (osdValues === "Y") {
        $('#osdRemarksId').show();
    } else {
        $('#osdRemarksId').hide();
    }
}
function deleteOsdRemarks(txt, id) {//delete OSD Remarks
    var user = $("#loginUserId").val();
    var fileState = $("#fileState").val();
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "deleteRemarks",
                        param1: id,
                        param2: "OSD",
                        param3: user.toString(),
                        param4: fileState,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        var osdValues = $('input:radio[name=overShortdamaged]:checked').val();
                        if (osdValues === "N" && data === true) {
                            $('#osdRemarksId').hide();
                            $('#osdRemarks').val('');
                        }
                    }
                });
            } else if (v === 2) {
                $('input:radio[name=overShortdamaged]').val(['Y']);
            }
        }
    });
}
function showPickupInfo(path, fileNumberId, fileNumber, popupName, closedBy) {//show pickUpInfo PopUp
    var unitId = "";
    var headerId = "";
    var cityZip = "";
    var moduleName = $('#moduleName').val();
    var commodity = $("#commObj").val();
    if (moduleName === "Imports") {
        headerId = $('#headerId').val();
        unitId = $('#unitId').val();
        cityZip = $('#doorOriginCityZip').val();
    } else {
        cityZip = $("#checkDoorOriginCityZip").is(":checked") ? $('#manualDoorOriginCityZip').val() : $('#doorOriginCityZip').val();
    }
    if (fileNumberId !== null && fileNumberId !== undefined && fileNumberId !== "") {
        if (commodity === null || commodity === undefined) {
            $.prompt("Please select atleast one commodity.");
            $("#commodity1").css({
                "border": "2px solid rgb(177, 57, 57)"
            });
        } else {
            var href = path + "/lclPickupInfo.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber="
                    + fileNumber + "&cityStateZip=" + cityZip.toUpperCase() + "&headerId=" + headerId + "&unitId=" + unitId +
                    "&moduleName=" + moduleName + "&closedBy=" + closedBy;
            $(".pickupInfo").attr("href", href);
            $(".pickupInfo").colorbox({
                iframe: true,
                width: "90%",
                height: "95%",
                title: popupName
            });
        }
    } else {
        $.prompt("Please save Booking");
    }
}
function showSpotRateInfo(path, fileNumberId, fileNumber) {
    var destination = getDestination();
    var href = path + "/lclBooking.do?methodName=displaySpotRate&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&destination=" + destination;
    $(".lclSpotRate").attr("href", href);
    $(".lclSpotRate").colorbox({
        iframe: true,
        width: "37%",
        height: "60%",
        title: "SpotRate"
    });
}
function showBarrel(path, fileNumberId, fileNumber) {
    var href = path + "/lclBarrel.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber;
    $(".barrel").attr("href", href);
    $(".barrel").colorbox({
        iframe: true,
        width: "85%",
        height: "85 %",
        title: "Barrel"
    });
}

function outsourceUserEmail(path, fileNumberId, fileNumber, userId) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "outsourceUserEmailaddress",
            param1: userId
        },
        success: function (data) {
            if (data === null || data === "") {
                $.prompt("Outsource Email Id is Required");
            } else {
                var href = path + "/lclOutsource.do?methodName=display&fileId=" + fileNumberId + "&fileNumber=" + fileNumber + "&emailId=" + data;
                $.colorbox({
                    iframe: true,
                    href: href,
                    width: "60%",
                    height: "70%",
                    title: "Outsource"
                });
            }
        }
    });
}
function showTerminateBtn() {//Terminate btn show or hide
    if ($('#fileNumberId').val() !== "" && $('#fileNumberId').val() !== null) {
        var moduleName = $('#moduleName').val();
        var status = $('#fileStatus').val();
        if (status === 'X') {
            if (moduleName === 'Exports') {
                showBookingReadOnly();
                $('#lclShortShip').hide();
                $('#lclShortShip1').hide();
                $('#lclReleaseButton1').hide();
                $('#lclReleaseButton2').hide();
            }
            $("#lclDomTermination").hide();
            $("#lclDomTermination1").hide();
            $("#lclUnTermination").show();
            $("#lclUnTermination1").show();
        } else {
            $("#lclDomTermination").show();
            $("#lclDomTermination1").show();
            $("#lclUnTermination").hide();
            $("#lclUnTermination1").hide();
            if (moduleName === 'Exports') {
                $('#lclReleaseButton1').show();
                $('#lclReleaseButton2').show();
            }
        }
    }
}

function lclUnTerminationStatus(txt, fileId) {//Restore Bkg
    $.prompt(txt, {
        buttons: {
            Yes: 1,
            No: 2
        },
        submit: function (v) {
            if (v === 1) {
                var userId = $("#loginUserId").val();
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "lclUnTerminationStatus",
                        param1: fileId,
                        param2: userId,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        window.parent.showLoading();
                        $("#methodName").val("editBooking");
                        $("#lclBookingForm").submit();
                    }
                });
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}
function openTerminatepopup(path, fileNumberId, fileNumber) {
    var moduleName = $('#moduleName').val();
    if (moduleName === 'Imports') {
        importTerminate(path, fileNumberId, fileNumber);
    } else {
        exportTerminate(path, fileNumberId, fileNumber);
    }
}
function importTerminate(path, fileNumberId, fileNumber) {
    var unitNo = $('#impUnitNo').val();
    if (unitNo !== null && unitNo !== "") {
        var voyNo = $('#impEciVoyage').val();
        $.prompt("Cannot perform Terminate Operation.DR is already linked to Unit#<span style=color:red>" + unitNo + "</span> of Voyage#<span style=color:red>" + voyNo + "</span>");
    } else {
        var userRoleId = $('#userRoleId').val();
        var totCostAmt = $('#totalCostAmt').val();
        if (totCostAmt !== null && totCostAmt !== '' && totCostAmt !== '0.00') {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "isbookingTerminate",
                    param1: userRoleId,
                    dataType: "json"
                },
                async: false,
                success: function (data) {
                    if (data) {
                        var txt = "Are you sure you want to terminate as there are costs on DR?";
                        $.prompt(txt, {
                            buttons: {
                                Yes: 1,
                                No: 2
                            },
                            submit: function (v) {
                                if (v === 1) {
                                    $.prompt.close();
                                    terminatePopUp(path, fileNumberId, fileNumber);
                                } else if (v === 2) {
                                    $.prompt.close();
                                }
                            }
                        });
                    } else {
                        $.prompt("You are not able to terminate a DR that has costs.");
                    }
                }
            });
        } else {
            terminatePopUp(path, fileNumberId, fileNumber);
        }
    }
}
function isCheckedReleaseStatus(fileNumberId) {
    var flag = false;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO",
            methodName: "getReleasedDateTime",
            param1: fileNumberId

        },
        async: false,
        success: function (data) {
            flag = data;
        }
    });
    return flag;
}
function exportTerminate(path, fileNumberId, fileNumber) {
    var isConsolidate = $("#conosolidateId tr").length > 1 ? true : false;
    var fileNos = getreleasedConsolidatedDR(fileNumberId);
    if (isCheckedReleaseStatus(fileNumberId)) {
        $.prompt("Please unrelease and unpick the DR inorder to terminate");
        return false;
    } else if (isConsolidate &&
            fileNos !== '' && isCheckedReleaseStatus(fileNos)) {
        $.prompt("Please unrelease and unpick all consolidated DR's inorder to terminate");
        return false;
    }
    terminatePopUp(path, fileNumberId, fileNumber);
}
function terminatePopUp(path, fileNumberId, fileNumber) {
    var destination = getDestination();
    var originName = $('#originCityName').val();
    var href = path + "/lclTerminatePopup.do?methodName=display&fileId=" + fileNumberId + "&fileNumber=" + fileNumber + "&originCityName=" + originName + "&destination=" + destination + "&moduleName=" + $('#moduleName').val();
    $.colorbox({
        iframe: true,
        href: href,
        width: "55%",
        height: "65%",
        title: "Terminate"
    });
}

function openLclArInvoice(path, fileNumberId, fileNumber, listFlag, moduleName, headerId) {
    var href = path + "/lclArInvoice.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber
            + "&listFlag=" + listFlag + "&moduleName=" + moduleName + "&headerId=" + headerId;
    $(".invoice").attr("href", href);
    $(".invoice").colorbox({
        iframe: true,
        width: "85%",
        height: "95 %",
        title: "AR Invoice",
        onClosed: function () {
            window.parent.showLoading();
            $("#methodName").val("saveBooking");
            $("#lclBookingForm").submit();
        }
    });
}


function revertToQuote(path, fileNumberId, moduleId) {
    var flag = false;
    $('.costStatus').each(function () {
        var costStatus = $(this).html().replace(/^\s\s*/, '').replace(/\s\s*$/, '');
        if (costStatus === 'AP') {
            flag = true;
        }
    });
    var containerNo = $('#impUnitNo').val();
    if (flag) {
        $.prompt("Cannot reverse to Booking because some of the Costs are paid in Accounting.");
    } else if (containerNo !== null && containerNo !== "" && containerNo !== undefined) {
        var voyNo = $('#impEciVoyage').val();
        $.prompt("Cannot perform Reverse Operation.DR is already linked to Unit#<span style=color:red>" + containerNo +
                "</span> of Voyage#<span style=color:red>" + voyNo + "</span>");
    } else {
        $.prompt("Are you sure? You want to reverse this Booking to Quote.", {
            buttons: {
                Ok: 1,
                Cancel: 2
            },
            submit: function (v) {
                if (v === 1) {
                    window.parent.showLoading();
                    $("#methodName").val("revertToQuote");
                    var params = $("#lclBookingForm").serialize();
                    $.post($("#lclBookingForm").attr("action"), params, function (data) {
                        if (data === 'revertToQuote') {
                            window.parent.changeLclChilds(path, fileNumberId, moduleId, moduleName);
                        } else {
                            $.prompt(data);
                        }
                    });
                    $.prompt.close();
                } else if (v === 2) {
                    $.prompt.close();
                }
            }
        });
    }
}

function clearBookedForVoyage() {
    var eciVoyage = $('#eciVoyage');
    if (eciVoyage !== undefined && eciVoyage !== null) {
        $('#eciVoyage').val('');
        $('#masterScheduleNo').val('');
        $('#sailDate').val('');
        $('#ssLine').val('');
        $('#vesselName').val('');
        $('#ssVoyage').val('');
        $('#originLrd').val('');
        $('#fdEta').val('');
        $('#pier').val('');
        $('#existMasterSchd').val('');
    }
}

function clearorgDestValues(showAstarType) {
    var moduleName = $('#moduleName').val();
    if (moduleName === 'Imports') {
        document.getElementById('clearRates').style.visibility = "hidden";
        $('#portOfOriginR').removeClass("text-readonly");
        $('#portOfOriginR').removeAttr("readonly");
        $('#portOfOriginR').removeClass("textlabelsBoldForTextBoxDisabledLookWidth");
        $('#portOfOriginR').removeClass("textlabelsBoldForTextBoxDisabledLook");
        $('#portOfOriginR').addClass("textlabelsBoldForTextBoxWidth");
        $('#portOfLoading').removeClass("text-readonly");
        $('#portOfLoading').removeClass("textlabelsBoldForTextBoxDisabledLookWidth");
        $('#portOfLoading').removeClass("textlabelsBoldForTextBoxDisabledLook");
        $('#portOfLoading').addClass("textlabelsBoldForTextBoxWidth");
        $('#portOfLoading').removeAttr("readonly");
        $('#portOfDestination').removeClass("text-readonly");
        $('#portOfDestination').removeClass("textlabelsBoldForTextBoxDisabledLookWidth");
        $('#portOfDestination').removeClass("textlabelsBoldForTextBoxDisabledLook");
        $('#portOfDestination').addClass("textlabelsBoldForTextBoxWidth");
        $('#portOfDestination').removeAttr("readonly");
        $('#finalDestinationR').removeClass("textlabelsBoldForTextBoxDisabledLookWidth");
        $('#finalDestinationR').removeClass("textlabelsBoldForTextBoxDisabledLook");
        $('#finalDestinationR').addClass("textlabelsBoldForTextBoxWidth");
        $('#finalDestinationR').removeClass("text-readonly");
        $('#finalDestinationR').removeAttr("readonly");
        $('#supplierNameOrg').val('');
        $('#supplierCode').val('');
        $('#finalDestinationR').val('');
        $('#finalDestinationId').val('');
        $('#portOfOriginR').val('');
        $('#portOfOriginId').val('');
        $('#portOfLoading').val('');
        $('#portOfLoadingId').val('');
        $('#portOfDestination').val('');
        $('#portOfDestinationId').val('');
        $('#terminal').val('');
        sessionStorage.setItem("orginAgentStatus", "Clear");
    } else {
        $('#showAstarVoy').hide();
        $('#upcomingSection').show();
        clearBookedForVoyage();
        fillPolPod();
        $('#finalDestinationR').removeClass("textlabelsBoldForTextBoxDisabledLook");
        $('#finalDestinationR').addClass("textlabelsBoldForTextBox");
        $('#portOfOriginR').removeClass("textlabelsBoldForTextBoxDisabledLook");
        $('#portOfOriginR').addClass("textlabelsBoldForTextBox");
        $('#nonRatedY').attr('checked', false);
        $('#nonRatedN').attr('checked', true);
        if (showAstarType == 'showAstarOrigin') {
            clearDeliverCargoFields();
            $('#portOfOriginR').removeClass("text-readonly");
            $('#portOfOriginR').removeAttr("readonly");
            $('#portOfOriginR').val('');
            $('#portOfOriginId').val('');
            $('#finalDestinationR').removeClass("text-readonly");
            $('#finalDestinationR').removeAttr("readonly");
            $('#portOfLoading').val('');
            $('#portOfLoadingId').val('');
            $('#portGriRemarksPod').val('');
            $('#internalRemarksPod').val('');
            $('#specialRemarksPod').val('');
            $(".splRemarks").text('');
            $(".internRemarks").text('');
            $(".griRemarks").text('');
        }
        if (showAstarType == 'showAstarDestn') {
            $('#finalDestinationR').removeClass("text-readonly");
            $('#finalDestinationR').removeAttr("readonly");
            $('#finalDestinationR').val('');
            $('#specialRemarks').val('');
            $('#portGriRemarks').val('');
            $('#internalRemarks').val('');
            $('#portGriRemarksPod').val('');
            $('#internalRemarksPod').val('');
            $('#specialRemarksPod').val('');
            $('#finalDestinationId').val('');
            $(".splRemarks").text('');
            $(".internRemarks").text('');
            $(".griRemarks").text('');
            $('#portOfOriginR').removeClass("text-readonly");
            $('#portOfOriginR').removeAttr("readonly");
            $('#originCode').val('');
            $('#portOfDestination').val('');
            $('#portOfDestinationId').val('');
            $('#agentName').val('');
            $('#agentNumber').val('');
            $('#agentBrand').val('');
            $('#rtdTransaction').val('');
            $('#agentInfo').val('');
        }
    }
    $('#destinationCode').val('');
    $('#originCode').val('');
    $('#polCode').val('');
    $('#podCode').val('');
//    $('#portOfLoading').val('');
//    $('#portOfLoadingId').val('');
//    $('#portOfDestination').val('');
//    $('#portOfDestinationId').val('');
}

function clearAllValues(txt, fileId, showAstarType) {
    var moduleName = $('#moduleName').val();
    if (moduleName === 'Imports' && $('#fileStatus').val() === 'M') {
        txt = "This will allow to change the Origin & Destination and the charges have already been posted. You must issue a CN in order to remove any charges to the customer";
    }
    $.prompt(txt, {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                if ($('#fileStatus').val() !== 'M') {
                    showProgressBar();
                    $("#methodName").val("deleteAutoCharges");
                    var params = $("#lclBookingForm").serialize();
                    params += "&fileNumberId=" + $('#fileNumberId').val();
                    $.post($("#lclBookingForm").attr("action"), params,
                            function (data) {
                                $('#chargeDesc').html(data);
                                $('#chargeDesc', window.parent.document).html(data);
                                hideProgressBar();
                            });
                }
                clearorgDestValues(showAstarType);
                $.prompt.close();
                if (moduleName === "Exports") {
                    validateInlandRates();
                }
            } else if (v == 2) {
                $.prompt.close();
            }
        }
    });
}

function clearAllRates(txt, fileId) {
    var hiddenRateType = $('#rateTypes').val();
    var filenumberId = $('#fileNumberId').val();
    if (filenumberId != "") {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkRates",
                param1: filenumberId,
                dataType: "json"
            },
            success: function (data) {
                if (data == true) {
                    $.prompt(txt, {
                        buttons: {
                            Yes: 1,
                            No: 2
                        },
                        submit: function (v) {
                            if (v == 1) {
                                $.prompt.close();
                                $.ajaxx({
                                    dataType: "json",
                                    data: {
                                        className: "com.gp.cong.lcl.dwr.LclDwr",
                                        methodName: "deleteRates",
                                        param1: fileId,
                                        request: "true",
                                        dataType: "json"
                                    },
                                    preloading: true,
                                    success: function (data) {
                                        var ratetype = $('input:radio[name=rateType]:checked').val();
                                        $('#rateTypes').val(ratetype);
                                        var rowCount = $("#chargesTable tbody tr").length;
                                        if (data[0] === 'true') {
                                            for (var i = rowCount; i > 0; i--)
                                            {
                                                var value = $("#chargesTable").find('tr:nth-child(' + i + ') td:nth-child(2)').html();
                                                if (trim(value) === '' || trim(value) == null) {
                                                    document.getElementById("chargesTable").deleteRow(i);
                                                }
                                            }
                                            if (data[1] !== undefined && data[1] !== "" && data[1] !== null) {
                                                document.getElementById('totalchargestd').innerHTML = data[1];
                                            }
                                        }
                                    }
                                });
                            } else if (v == 2) {
                                $('#rateTypes').val(hiddenRateType);
                                if (hiddenRateType == "C") {
                                    $('#ctc').attr('checked', true);
                                    fillaesbyRateType();
                                } else if (hiddenRateType == "R") {
                                    $('#rateR').attr('checked', true);
                                    fillaesbyRateType();
                                } else if (hiddenRateType == "F") {
                                    $('#rateF').attr('checked', true);
                                    fillaesbyRateType();
                                }
                                $.prompt.close();
                            }
                        }
                    });
                }
            }
        });
    }
}

function rateVerified() {
    var fileNumberId = $('#fileNumberId').val();
    var portOfOrigin = $('#portOfOriginR').val();
    var moduleName = $('#moduleName').val();
    if (fileNumberId != "") {
        if (moduleName != 'Imports') {
            document.getElementById('showAstar').style.position = "relative"
            document.getElementById('showAstar').style.visibility = "visible";
            document.getElementById('showAstarDestn').style.position = "relative"
            document.getElementById('showAstarDestn').style.visibility = "visible";
            $('#finalDestinationR').addClass("text-readonly");
            $('#finalDestinationR').removeClass("textlabelsBoldForTextBox");
            $('#finalDestinationR').attr("readonly", true);
            if (portOfOrigin != '') {
                $('#portOfOriginR').addClass("text-readonly");
                $('#portOfOriginR').removeClass("textlabelsBoldForTextBox");
                $('#portOfOriginR').attr("readonly", true);
            }
        }
        if (moduleName == 'Imports') {
            document.getElementById('showAstar').style.position = "relative"
            document.getElementById('showAstar').style.visibility = "visible";
            var pol = $('#portOfLoading').val();
            var pod = $('#portOfDestination').val();
            if (pol != "") {
                $('#portOfLoading').addClass("text-readonly");
                $('#portOfLoading').addClass("textlabelsBoldForTextBoxDisabledLookWidth");
                $('#portOfLoading').attr("readonly", true);
            } else {
                $('#portOfLoading').removeClass("text-readonly");
                $('#portOfLoading').attr("readonly", false);
            }
            if (pod != "") {
                $('#portOfDestination').addClass("text-readonly");
                $('#portOfDestination').addClass("textlabelsBoldForTextBoxDisabledLookWidth");
                $('#portOfDestination').attr("readonly", true);
            } else {
                $('#portOfDestination').removeClass("text-readonly");
                $('#portOfDestination').attr("readonly", false);
            }
            $('#portOfOriginR').addClass("text-readonly");
            $('#portOfOriginR').addClass("textlabelsBoldForTextBoxDisabledLookWidth");
            $('#portOfOriginR').attr("readonly", true);
        }
    } else {
        //  $('#showAstar').hide();
        if (moduleName != 'Imports') {
            document.getElementById('showAstar').style.visibility = "hidden";
            document.getElementById('showAstarDestn').style.visibility = "hidden";
        } else {
            document.getElementById('showAstar').style.visibility = "hidden";
        }
        $('#portOfOriginR').removeClass("text-readonly");
        $('#finalDestinationR').removeClass("text-readonly");
        $('#portOfOriginR').addClass("textlabelsBoldForTextBox");
        $('#finalDestinationR').addClass("textlabelsBoldForTextBox");
        $('#portOfOriginR').attr("readonly", false);
        $('#finalDestinationR').attr("readonly", false);
    }
//        });
//    }
}



function changeDoorOrigin(fileNumberStatus) {
    var doorOrigin = $('#doorOriginCityZip').val();
    var duplicateDoorOrigin = $('#duplicateDoorOrigin').val();
    var fileId = $('#fileNumberId').val();
    $('#duplicateDoorOrigin').val(doorOrigin);
    if (doorOrigin !== null && doorOrigin !== undefined && doorOrigin !== "") {
        $('#pickupInfo').show();
    }
//    if (fileId !== "" && duplicateDoorOrigin !== null && duplicateDoorOrigin !== '' && doorOrigin !== duplicateDoorOrigin) {
//        $('#duplicateDoorOrigin').val(doorOrigin);
//        $('#pickupInfo').show();
//        $.ajaxx({
//            data: {
//                className: "com.gp.cong.lcl.dwr.LclDwr",
//                methodName: "removePickupInfo",
//                param1: fileId
//            },
//            success: function (data) {
//
//            }
//        });
//        $.ajaxx({
//            dataType: "json",
//            data: {
//                className: "com.gp.cong.lcl.dwr.LclDwr",
//                methodName: "checkPickupRate",
//                param1: fileId,
//                request: "true",
//                dataType: "json"
//            },
//            success: function (data) {
//                if (data == true) {
//                    deletePickupRates('Pickup Rates will be deleted.Are you sure You want to continue?', fileNumberStatus);
//                    return false;
//                }
//            }
//        });
//    }
}

function deletePickupRates(txt, fileNumberStatus) {
    $.prompt(txt, {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                showProgressBar();
                submitAjaxFormForPickupCharge('deletePickUp', '#lclBookingForm', '#chargeDesc', fileNumberStatus);
                var pooDoor = $('input:radio[name=pooDoor]:checked').val();
                if (pooDoor === "N") {
                    $('#doorOriginCityZip').val('');
                }
                if ($('#moduleName').val() === 'Exports') {
                    $("#checkDoorOriginCityZip").hide();
                    $("#checkDoorOriginCityZip").attr('checked', false);
                }
                hideProgressBar();
                $.prompt.close();
            } else if (v === 2) {
                $('#pickupInfo').show();
                $('#doorOriginY').attr('checked', true);
                $('#duplicateDoorOrigin').val($('#doorOriginCityZip').val());
                $('#doorOriginCityZip').removeClass().addClass("text");
                $('#doorOriginCityZip').removeAttr("readonly");
                if ($('#moduleName').val() === 'Exports') {
                    $("#checkDoorOriginCityZip").show();
                    $("#checkDoorOriginCityZip").attr('checked', false);
                    $("#manualDoorOriginCityZip").hide();
                    $("#manualDoorOriginCityZip").removeClass("textlabelsBoldForTextBoxDisabledLook");
                    $("#doorOriginCityZip").show();
                    $("#doorOriginCityZip").val('');
                }
                $.prompt.close();
            }
        }
    });
}

function submitAjaxFormForPickupCharge(methodName, formName, selector, fileNumberStatus) {
    var fileId = $('#fileNumberId').val();
    var moduleName = $('#moduleName').val();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&fileId=" + fileId + "&moduleName=" + moduleName + "&fileNumberStatus=" + fileNumberStatus;
    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                $.fn.colorbox.close();
            });
}

function showHideRelay() {
    $('#portOfOriginR').val('');
    $('#portOfOriginId').val('');
    var destination = $('#finalDestinationR').val();
    var relay = $('input:radio[name=relayOverride]:checked').val();
    var nonRated = $('input:radio[name=nonRated]:checked').val();
    if (relay == 'Y') {
        if ($('#showFullRelay').is(":checked")) {
            $('#portOfOriginR').attr('alt', 'PORTNAME');
        } else {
            $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
        }
    } else {
        if (nonRated == 'N') {
            if (destination != "") {
                $('#portOfOriginR').attr('alt', 'RELAYORIGIN');
            } else {
                if ($('#showFullRelay').is(":checked")) {
                    $('#portOfOriginR').attr('alt', 'PORTNAME');
                } else {
                    $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
                }
            }
        } else {
            if ($('#showFullRelay').is(":checked")) {
                $('#portOfOriginR').attr('alt', 'PORTNAME');
            } else {
                if (destination != "") {
                    $('#portOfOriginR').attr('alt', 'RELAYORIGIN');
                } else {
                    $('#portOfOriginR').attr('alt', 'RELAY_ORIGIN');
                }
            }
        }
    }
}

function showBookingReadOnly() {
    var form = document.getElementById('lclBookingForm');
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "text" || element.type == "textarea") {
            if (element.id == "stdchgratebasis" || element.id == "msrForM" || element.id == "ofratebasis"
                    || element.id == "wgtForM" || element.id == "wgtForI" || element.id == "msrForI") {
            } else {
                element.style.backgroundColor = "#CCEBFF";
                element.readOnly = true;
                element.style.borderTop = "0px";
                element.style.borderBottom = "0px";
                element.style.borderRight = "0px";
                element.style.borderLeft = "0px solid";
            }
        }
        if (element.type == "select-one" || element.type == "radio" || element.type == "checkbox") {
            element.style.border = 0;
            element.disabled = true;
        }
    }
    $('.disabledButton').hide();
    $("#scanAttach").show();
    $('#lcloutsourceButton').hide();
    $('#lclImpRreleasebot').hide();
    $('#belowrouting').hide();
    $('#lclDomTermination').hide();
    $('#lclImpRrelease').hide();
    $('#lclUnTermination').hide();
    $('#arInvoice').hide();
    $('#copy').hide();
    $('#remarks').hide();
    $('#tracking1').hide();
    $('#save').hide();
    $('#revert').hide();
    $('#convertBl').hide();
    $('.bottomConvertBl').hide();
    $('#convertBook').hide();
    $('#inbond').hide();
    $('#correctionNotice').hide();
    $('#inbonds').hide();
    $('#freightRelease').hide();
    $('#copy').hide();
    $('#Baes').hide();
    $('.whseDetail').show();
    $('#aesB').hide();
    $('#tracking1').hide();
    $('#releaseId1').hide();
    $('#cargoReceivedId').hide();
    $('#movePlan').hide();
    $('#routing').hide();
    $('#commodity1').hide();
    $('#addhot').hide();
    $('#addcust').hide();
    $('#addncm').hide();
    $('#addWhse').hide();
    $('#addHs').hide();
    $('#addtrack').hide();
    $('#addaes').hide();
    $('#consolidate').hide();
    $('#podfdtt').hide();
    $('#podfd').hide();
    $('#routeOption').hide();
    $('#lclSpotRate').hide();
    $('#calculate').hide();
    $('#costCharge').hide();
    $('#costandCharge').hide();
    $('#aesdet').hide();
    $('.hazmat').hide();
    $('#pickupInfo').hide();
    $('#correctionNotice').hide();
    $('#correctionNoticeBottom').hide();
    $('#impPayments').hide();
    $('#link-voy').hide();
    $('#link-voyage').hide();
    $('#clearImpVoyage').hide();
    $('#invoiceCharge').hide();
    $('#quickCN').hide();
    $('#lclCargoButton').hide();
    $('#lclCargoButtonBottom').hide();
    $('#destinationServices').hide();
    $('#pickupDate').attr('disabled', true);
    $('#goDate').attr('disabled', true);
    $('#itDate').attr('disabled', true);
    $('#lastFd').attr('disabled', true);
    $('#storageDate').attr('disabled', true);
    var imgs = document.getElementsByTagName("img");
    for (var k = 0; k < imgs.length; k++) {
        if (imgs[k].id != "trianleicon" && imgs[k].id != "collpaseicon") {
            imgs[k].style.visibility = "hidden";
        }
        if (imgs[k].id == "viewgif" || imgs[k].id == "viewgif1" || imgs[k].id == "viewgif2") {
            imgs[k].style.visibility = "visible";
        }
    }
}
function openInbond(path, fileNumberId, fileNumber) {
    if (fileNumberId != null && fileNumberId != "" && fileNumberId != '0') {
        var moduleName = $('#moduleName').val();
        var href = path + "/lclInbonds.do?methodName=display&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&moduleName=" + moduleName;
        $(".inbondButton").attr("href", href);
        $(".inbondButton").colorbox({
            iframe: true,
            href: href,
            width: "45%",
            height: "80%",
            title: "Inbonds",
            onClosed: function () {
                var moduleName = $('#moduleName').val();
                if (moduleName == 'Imports') {
                    var fileNumberId = $('#fileNumberId').val();
                    $("#methodName").val("closeInbond");
                    var params = $('#lclBookingForm').serialize();
                    params += "&fileNumberId=" + fileNumberId;
                    $.post($('#lclBookingForm').attr("action"), params,
                            function (data) {
                                $('#inbondNumersList').html(data);
                                $('#inbondNumersList', window.parent.document).html(data);
                                closeInbondNoHotcode();
                                $.fn.colorbox.close();
                            });
                } else {
                    showProgressBar();
                    closeInbondNoHotcode();
                }
            }
        });
    } else {
        congAlert("Please Save Booking");
    }
}
function closeInbondNoHotcode() {
    var fileNumberId = $('#fileNumberId').val();
    var thirdPName = "hotCodes";
    var insertInbondFlag = $("#insertInbondFlag").val();
    var inbDesc = "INB/INBOND CARGO";
    $("#methodName").val("addLcl3pReference");
    var params = $('#lclBookingForm').serialize();
    params += "&thirdPName=" + thirdPName + "&fileNumberId=" + fileNumberId + "&inbDesc=" + inbDesc + "&insertInbondFlag=" + insertInbondFlag;
    $.post($('#lclBookingForm').attr("action"), params,
            function (data) {
                hideProgressBar();
                $('#hotCodesList').html(data);
                $('#hotCodesList', window.parent.document).html(data);
            });
}
function getThirdParties(val0, val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12) {
    $('#supplierName').val(val0);
    $('#supplierCode').val(val1);
    $('#supplierAddress').val(val8);
    $('#supplierCity').val(val10);
    $('#supplierState').val(val11);
    $('#supplierZip').val(val12);
    $('#supplierPhone').val(val5);
    $('#supplierFax').val(val6);
    $('#supplierEmail').val(val7);
}
function getThirdPartiesShipper(val0, val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12) {
    $('#shipperName').val(val0);
    $('#shipperCode').val(val1);
    $('#shipperAddress').val(val8);
    $('#shipperCity').val(val10);
    $('#shipperState').val(val11);
    $('#shipperZip').val(val12);
    $('#shipperPhone').val(val5);
    $('#shipperFax').val(val6);
    $('#shipperEmail').val(val7);
}
function getThirdPartiesForwarder(val0, val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12) {
    $('#forwarderName').val(val0);
    $('#forwarderCode').val(val1);
    $('#forwarderAddress').val(val8);
    $('#forwarderCity').val(val10);
    $('#forwarderState').val(val11);
    $('#forwarderZip').val(val12);
    $('#forwarderPhone').val(val5);
    $('#forwarderFax').val(val6);
    $('#forwarderEmail').val(val7);
}
function getThirdPartiesConsignee(val0, val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12) {
    $('#consigneeName').val(val0);
    $('#consigneeCode').val(val1);
    $('#consigneeAddress').val(val8);
    $('#consigneeCity').val(val10);
    $('#consigneeState').val(val11);
    $('#consigneeZip').val(val12);
    $('#consigneePhone').val(val5);
    $('#consigneeFax').val(val6);
    $('#consigneeEmail').val(val7);
}
function getThirdPartiesNotify(val0, val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12) {
    $('#notifyName').val(val0);
    $('#notifyCode').val(val1);
    $('#notifyAddress').val(val8);
    $('#notifyCity').val(val10);
    $('#notifyState').val(val11);
    $('#notifyZip').val(val12);
    $('#notifyPhone').val(val5);
    $('#notifyFax').val(val6);
    $('#notifyEmail').val(val7);
}

function checkNonRated(val) {
    var nonRated = $('input:radio[name=nonRated]:checked').val();
    var destination = $('#finalDestinationR').val();
    var unknownDest = $('#unknownDest').val();
    if (nonRated == 'Y') {
        $('#showFullRelayFd').attr('disabled', false);
        $('#showFullRelay').attr('disabled', false);
        if (destination == "") {
            $('#finalDestinationId').val($('#unknownDestId').val());
            $('#finalDestinationR').val(unknownDest);
            $('#finalDestinationR').addClass('textlabelsBoldForTextBoxDisabledLook');
            $('#portOfLoading').val($('#portOfOriginR').val());
            $('#portOfLoadingId').val($('#portOfOriginId').val());
            $('#portOfDestination').val($('#finalDestinationR').val());
            $('#portOfDestinationId').val($('#finalDestinationId').val());
        } else {
            if (val != 'L') {
                var txt = "Are You Sure you want to change Destination?";
                $.prompt(txt, {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v == 1) {

                            $("#methodName").val("deleteAutoCharges");
                            var params = $("#lclBookingForm").serialize();
                            params += "&fileNumberId=" + $('#fileNumberId').val();
                            $.post($("#lclBookingForm").attr("action"), params,
                                    function (data) {
                                        $('#chargeDesc').html(data);
                                        $('#chargeDesc', window.parent.document).html(data);
                                        var origin = $("#portOfOriginR").val();
                                        var destination = $("#finalDestinationR").val();
                                        $(".ChargeOrigin").text(origin.substring(0, origin.indexOf("/")));
                                        $(".ChargeDest").text(destination.substring(0, destination.indexOf("/")));
                                    });
                            $('#finalDestinationId').val($('#unknownDestId').val());
                            $('#finalDestinationR').val(unknownDest);
                            $('#finalDestinationR').addClass('textlabelsBoldForTextBoxDisabledLook');
                            $('#portOfLoading').val($('#portOfOriginR').val());
                            $('#portOfLoadingId').val($('#portOfOriginId').val());
                            $('#portOfDestination').val($('#finalDestinationR').val());
                            $('#portOfDestinationId').val($('#finalDestinationId').val());
                            $('#specialRemarks').val('');
                            $('#portGriRemarks').val('');
                            $('#internalRemarks').val('');
                            $('#portGriRemarksPod').val('');
                            $('#internalRemarksPod').val('');
                            $('#specialRemarksPod').val('');
                            $(".splRemarks").text('');
                            $(".internRemarks").text('');
                            $(".griRemarks").text('');
                            $('#upcomingSection').hide();
                            $('#showAstarVoy').hide();
                            clearBookedForVoyage();
                        } else if (v == 2) {
                            $('#nonRatedN').attr('checked', true);
                            $('#finalDestinationR').val();
                        }
                    }
                });
            } else {
                if ($('#portOfLoading').val != "" && $('#portOfLoadingId').val != "") {
                    $('#portOfLoading').val($('#portOfOriginR').val());
                    $('#portOfLoadingId').val($('#portOfOriginId').val());
                }
                if ($('#portOfDestination').val != "" && $('#portOfDestinationId').val != "") {
                    $('#portOfDestination').val($('#finalDestinationR').val());
                    $('#portOfDestinationId').val($('#finalDestinationId').val());
                }
            }
        }
    } else {
        $('#showFullRelayFd').attr('disabled', true);
        $('#showFullRelay').attr('disabled', true);
        $('#showFullRelayFd').attr('checked', false);
        $('#showFullRelay').attr('checked', false);
        if (destination != "" && destination.indexOf('007UN') != -1) {
            $('#finalDestinationR').removeClass("textlabelsBoldForTextBoxDisabledLook");
            document.getElementById('finalDestinationR').value = "";
            $('#finalDestinationR').removeAttr("readonly");
            $('#finalDestinationR').removeClass("text-readonly");
            $('#portOfLoading').val();
            $('#portOfLoadingId').val();
            $('#portOfDestination').val();
            $('#portOfDestinationId').val();
            $('#portOfLoading').addClass("text-readonly");
            $('#portOfLoading').addClass("textlabelsBoldForTextBoxDisabledLook");
            $('#portOfDestination').addClass("text-readonly");
            $('#portOfDestination').addClass("textlabelsBoldForTextBoxDisabledLook");
        }
    }
}
function clearBookedVoyage(txt) {
    $.prompt(txt, {
        buttons: {
            Ok: 1,
            Cancel: 2
        },
        submit: function (v) {
            if (v === 1) {
                $('#showAstarVoy').hide();
                $('#upcomingSection').show();
                clearBookedForVoyage();
                $.prompt.close();
            } else if (v === 2) {
                $.prompt.close();
            }
        }
    });
}




function clientDetails() {
    $('#fax').val($('#dupClientFax').val());
    $('#phone').val($('#dupClientPhone').val());
    if ($('#clientWithConsignee').is(":checked")) {
    } else {
        $('#clientCons').val($('#client').val());
    }
}



function changeShipDetails() {
    //  var client =  $('#shipperName').val();
    var code = $('#shipperCode').val();
    var acctType = $('#shipper_acct_type').val();
    var subType = $('#shipper_sub_type').val();
    var fax = $('#shipperFax').val();
    var city = $('#shipperCity').val();
    var country = $('#shipperCountry').val();
    var state = $('#shipperState').val();
    var zip = $('#shipperZip').val();
    var address = $('#shipperAddress').val().replace(/\,/g, " ");
    var email = $('#shipperEmail').val();
    var phone = $('#shipperPhone').val();
    var credit = $('#shipperCredit').val();
    var shipref = $('#shipperClientRef').val();
    $('#dupShipref').val(shipref);
    // $('#shipperNameClient').val(client);
    // $('#dupShipName').val(client);
    $('#shipperCodeClient').val(code);
    $('#shipperaccttype').val(acctType);
    $('#shippersubtype').val(subType);
    $('#shipperFaxClient').val(fax);
    $('#shipperCityClient').val(city);
    $('#shipperCountryClient').val(country);
    $('#shipperStateClient').val(state);
    $('#shipperZipClient').val(zip);
    $('#shipperAddressClient').val(address);
    $('#shipperEmailClient').val(email);
    $('#shipperPhoneClient').val(phone);
    $('#creditForShipper').val(credit);
}

function changeConsiDetails() {
    //  var client =  $('#consigneeName').val();
    var code = $('#consigneeCode').val();
    var acctType = $('#consignee_acct_type').val();
    var subType = $('#consignee_sub_type').val();
    var fax = $('#consigneeFax').val();
    var city = $('#consigneeCity').val();
    var country = $('#consigneeCountry').val();
    var state = $('#consigneeState').val();
    var zip = $('#consigneeZip').val();
    var address = $('#consigneeAddress').val().replace(/\,/g, " ");
    var email = $('#consigneeEmail').val();
    var phone = $('#consigneePhone').val();
    var credit = $('#consigneeCredit').val();
    var consigneeClientRef = $('#consigneeClientRef').val();
    $('#consigneeClientReff').val(consigneeClientRef);
    // $('#consigneeNameClient').val(client);
    // $('#dupConsName').val(client);
    $('#consigneeCodeClient').val(code);
    $('#consigneeaccttype').val(acctType);
    $('#consigneesubtype').val(subType);
    $('#consigneeFaxClient').val(fax);
    $('#consigneeCityClient').val(city);
    $('#consigneeCountryClient').val(country);
    $('#consigneeStateClient').val(state);
    $('#consigneeZipClient').val(zip);
    $('#consigneeAddressClient').val(address);
    $('#consigneeEmailClient').val(email);
    $('#consigneePhoneClient').val(phone);
    $('#creditForConsignee').val(credit);
}
function frwdDetails() {
    $('#forwarderFaxClient').val($('#dupForwarderFax').val());
    $('#forwarderPhoneClient').val($('#dupForwarderPhone').val());
    var client = $('#forwarderNameClient').val();
    var shipper = $('#shipperNameClient').val();
    var code = $('#forwarderCodeClient').val();
    var acctType = $('#forwarderaccttype').val();
    var subType = $('#forwardersubtype').val();
    var fax = $('#forwarderFaxClient').val();
    var city = $('#forwarderCityClint').val();
    var country = $('#forwarderCountryClient').val();
    var state = $('#forwarderStateClient').val();
    var zip = $('#forwarderZipClient').val();
    var poa = document.getElementById('forwarderPoaClient').innerHTML;
    var address = $('#forwarderAddressClient').val().replace(/\,/g, " ");
    var email = $('#forwarderEmailClient').val();
    var phone = $('#forwarderPhoneClient').val();
    var credit = $('#creditForForwarder').val();
    $('#forwarderName').val(client);
    $('#forwarderCode').val(code);
    // $('#forwarder_acct_type').val(acctType);
    // $('#forwarder_sub_type').val(subType);
    $('#forwarderFax').val(fax);
    $('#forwarderCity').val(city);
    $('#forwarderCountry').val(country);
    $('#forwarderState').val(state);
    $('#forwarderZip').val(zip);
    document.getElementById('forwarderPoa').innerHTML = poa;
    $('#forwarderAddress').val(address);
    $('#forwarderPhone').val(phone);
    $('#forwarderEmail').val(email);
    $('#forwarderCredit').val(credit);
    if ($('#radioP').attr('checked'))
    {
        if (shipper === "" && $('#thirdpartyaccountNo').val() === "" && $("#fileNumberId").val() !== '') {
            document.getElementById('billF').checked = true;
            $("#billF").click();
        }
    }
}


function forwarderDetails() {
    var client = $('#forwarderName').val();
    var code = $('#forwarderCode').val();
    var acctType = $('#forwarder_acct_type').val();
    var subType = $('#forwarder_sub_type').val();
    var fax = $('#forwarderFax').val();
    var city = $('#forwarderCity').val();
    var country = $('#forwarderCountry').val();
    var state = $('#forwarderState').val();
    var zip = $('#forwarderZip').val();
    var poa = document.getElementById('forwarderPoa').innerHTML;
    var address = $('#forwarderAddress').val().replace(/\,/g, " ");
    var email = $('#forwarderEmail').val();
    var phone = $('#forwarderPhone').val();
    var credit = $('#forwarderCredit').val();
    var partyship = $('#shipperName').val();
    var partyfwd = $('#forwarderName').val();
    var credit1 = credit.substring(0, credit.indexOf('-'))
    $('#forwarderNameClient').val(client);
    $('#forwarderCodeClient').val(code);
    $('#forwarderaccttype').val(acctType);
    $('#forwardersubtype').val(subType);
    $('#forwarderFaxClient').val(fax);
    $('#forwarderCityClint').val(city);
    $('#forwarderCountryClient').val(country);
    $('#forwarderStateClient').val(state);
    $('#forwarderZipClient').val(zip);
    document.getElementById('forwarderPoaClient').innerHTML = poa;
    $('#forwarderAddressClient').val(address);
    $('#forwarderEmailClient').val(email);
    $('#forwarderPhoneClient').val(phone);
    $('#creditForForwarder').val(client);
    displayForwarderDetails();
    if ($('#radioP').attr('checked'))
    {
        if (partyship === "" && $('#thirdpartyaccountNo').val() === "" && $("#fileNumberId").val() !== '') {
            document.getElementById('billF').checked = true;
            $("#billF").click();
        }
    }
}

function changeForwarderDetails() {
    var client = $('#forwarderName').val();
    var code = $('#forwarderCode').val();
    var acctType = $('#forwarder_acct_type').val();
    var subType = $('#forwarder_sub_type').val();
    var fax = $('#forwarderFax').val();
    var city = $('#forwarderCity').val();
    var country = $('#forwarderCountry').val();
    var state = $('#forwarderState').val();
    var zip = $('#forwarderZip').val();
    var poa = document.getElementById('forwarderPoa').innerHTML;
    var address = $('#forwarderAddress').val().replace(/\,/g, " ");
    var email = $('#forwarderEmail').val();
    var phone = $('#forwarderPhone').val();
    var credit = $('#forwarderCredit').val();
    var credit1 = credit.substring(0, credit.indexOf('-'))
    var forwarderClientRef = $('#forwarderClientRef').val();
    $('#forwarderClientReff').val(forwarderClientRef);
    $('#forwarderNameClient').val(client);
    $('#forwarderCodeClient').val(code);
    $('#forwarderaccttype').val(acctType);
    $('#forwardersubtype').val(subType);
    $('#forwarderFaxClient').val(fax);
    $('#forwarderCityClint').val(city);
    $('#forwarderCountryClient').val(country);
    $('#forwarderStateClient').val(state);
    $('#forwarderZipClient').val(zip);
    document.getElementById('forwarderPoaClient').innerHTML = poa;
    $('#forwarderAddressClient').val(address);
    $('#forwarderEmailClient').val(email);
    $('#forwarderPhoneClient').val(phone);
    $('#creditForForwarder').val(client);
}

$(document).ready(function () {
    var moduleName = $('#moduleName').val();
    $('#shipperNameClient').keyup(function () {
        var moduleName = $('#moduleName').val();
        if (moduleName != 'Imports') {
            var shipper = $('#shipperNameClient').val();
            if (shipper == "") {
                $('#shipperName').val('');
                $('#shipperCode').val('');
                $('#dupShipperPhone').val('');
                $('#dupShipperFax').val('');
                $('#shipper_acct_type').val('');
                $('#shipperContactClient').val('');
                $('#shipper_sub_type').val('');
                $('#shipperFax').val('');
                $('#shipperCity').val('');
                $('#shipperCountry').val('');
                $('#shipperState').val('');
                $('#shipperZip').val('');
                document.getElementById('shipper1').innerHTML = "";
                $('#shipperAddress').val('');
                $('#shipperEmail').val('');
                $('#shipperPhone').val('');
                $('#shipperCredit').val('');
                $('#shipperNameClient').val('');
                $('#shipperCodeClient').val('');
                $('#shipperaccttype').val('');
                $('#shippersubtype').val('');
                $('#sFmc').val('');
                $('#sotiNumber').val('');
                $('#scommodityNumber').val('');
                $('#sretailCommodity').val('');
                $('#shipperFaxClient').val('');
                $('#shipperCityClient').val('');
                $('#shipperCountryClient').val('');
                $('#shipperStateClient').val('');
                $('#shipperZipClient').val('');
                $('#shipperCreditStatus').text('');
                $('#shipperPOAV').text('');
                $('.shipperPOAV').text('');
                $('#shipBsEciAcctNo').val('');
                $('#shipBsEciFwNo').val('');
                // document.getElementById('shipper').innerHTML = "";
                //document.getElementById('shipperCreditClient').innerHTML = '';
                $('#shipperAddressClient').val('');
                $('#shipperEmailClient').val('');
                $('#shipperPhoneClient').val('');
                $('#creditForShipper').val('');
                $('#newShipperContact').attr('checked', false);
                var path = $('#path').val();
                //   $('.shpNotes').attr("src", path + "/img/icons/e_contents_view.gif");
                displayShipperDetails();
                var partyship = $('#shipperName').val();
                var partyfwd = $('#forwarderName').val();
                var client = $('#shipperNameClient').val();
                var fwdclient = $('#forwarderNameClient').val();
                if ($('#radioP').attr('checked'))
                {
                    if ((client != "" && fwdclient == "") && (partyship != "" && partyfwd == "")) {
                        document.getElementById('billS').checked = true;
                    }
                    if ((client == "" && fwdclient == "") && (partyship == "" && partyfwd == "")) {
                        document.getElementById('billS').checked = false;
                        document.getElementById('billF').checked = true;
                        checkBilltoCode();
                        ChangeBillToCode('F', this);
                    }
                }
            }
        }
    })
    $('#dupShipName').keyup(function () {
        var moduleName = $('#moduleName').val();
        if (moduleName != 'Imports') {
            var shipper = $('#dupShipName').val();
            if (shipper == "") {
                $('#shipperName').val('');
                $('#shipperCode').val('');
                $('#shipperNameClient').val('');
                $('#shipper_acct_type').val('');
                $('#dupShipperPhone').val('');
                $('#dupShipperFax').val('');
                $('#shipperContactClient').val('');
                $('#shipper_sub_type').val('');
                $('#shipperFax').val('');
                $('#shipperCity').val('');
                $('#shipperCountry').val('');
                $('#shipperState').val('');
                $('#shipperZip').val('');
                document.getElementById('shipper1').innerHTML = "";
                $('#shipperAddress').val('');
                $('#shipperEmail').val('');
                $('#shipperPhone').val('');
                $('#shipperCredit').val('');
                $('#shipperNameClient').val('');
                $('#shipperCodeClient').val('');
                $('#shipperaccttype').val('');
                $('#shippersubtype').val('');
                $('#shipperFaxClient').val('');
                $('#shipperCityClient').val('');
                $('#shipperCountryClient').val('');
                $('#shipperStateClient').val('');
                $('#shipperZipClient').val('');
                document.getElementById('shipper').innerHTML = "";
                document.getElementById('shipperCreditClient').innerHTML = '';
                $('#shipperAddressClient').val('');
                $('#shipperEmailClient').val('');
                $('#shipperPhoneClient').val('');
                $('#creditForShipper').val('');
                $('#newShipperContact').attr('checked', false);
                displayShipperDetails();
            }
        }
    })
    $('#shipperName').keyup(function () {
        var shipperName = $('#shipperName').val();
        if (shipperName == "") {
            $('#shipperName').val();
            $('#shipperCode').val();
            var path = $('#path').val();
            $('.shpNotess').attr("src", path + "/img/icons/e_contents_view.gif");
        }
    });
    $('#client').keyup(function () {
        var moduleName = $('#moduleName').val();
        if (moduleName != 'Imports') {
            var shipper = $('#client').val();
            if (shipper == "") {
                $('#client').val('');
                $('#clientCons').val('');
                $('#ManualClient').val('');
                $('#client_no').val('');
                $('#contactName').val('');
                $('#otiNumber').val('');
                $('#clientContactManul').val('');
                $('#email').val('');
                $('#fmcNo').val('');
                $('#retailCommodity').val('');
                $('#commodityNumber').val('');
                $('#fax').val('');
                $('#city').val('');
                $('#state').val('');
                $('#phone').val('');
                $('#address').val('');
                $('#zip').val('');
                $('#clientPoaClient').val('');
                $('#creditForClient').val('');
                $('#clientBsEciAcctNo').val('');
                $('#clientBsEciFwNo').val('');
                document.getElementById('clientCreditClient').innerHTML = "";
                document.getElementById('clientPoa').innerHTML = "";
                $('#newClientContact').attr('checked', false);
                var path = $('#path').val();
                //  $('#clntNotes').attr("src", path + "/img/icons/e_contents_view.gif");
                displayClientDetails();
            }
        }
    })
    $('#ManualClient').keyup(function () {
        var moduleName = $('#moduleName').val();
        if (moduleName != 'Imports') {
            var shipper = $('#ManualClient').val();
            if (shipper == "") {
                $('#client').val('');
                $('#clientCons').val('');
                $('#ManualClient').val('');
                $('#client_no').val('');
                $('#contactName').val('');
                $('#clientContactManul').val('');
                $('#email').val('');
                $('#fax').val('');
                $('#city').val('');
                $('#state').val('');
                $('#phone').val('');
                $('#address').val('');
                $('#fmcNo').val('');
                $('#retailCommodity').val('');
                $('#commodityNumber').val('');
                $('#otiNumber').val('');
                $('#zip').val('');
                $('#clientPoaClient').val('');
                $('#creditForClient').val('');
                document.getElementById('clientCreditClient').innerHTML = "";
                document.getElementById('clientPoa').innerHTML = "";
                $('#newClientContact').attr('checked', true);
                displayClientDetails();
            }
        }
    })
    $('#clientCons').keyup(function () {
        var moduleName = $('#moduleName').val();
        if (moduleName != 'Imports') {
            var shipper = $('#clientCons').val();
            if (shipper == "") {
                $('#client').val('');
                $('#clientCons').val('');
                $('#otiNumber').val('');
                $('#ManualClient').val('');
                $('#client_no').val('');
                $('#contactName').val('');
                $('#clientContactManul').val('');
                $('#email').val('');
                $('#fax').val('');
                $('#phone').val('');
                $('#city').val('');
                $('#state').val('');
                $('#zip').val('');
                $('#fmcNo').val('');
                $('#otiNumber').val('');
                $('#retailCommodity').val('');
                $('#commodityNumber').val('');
                $('#address').val('');
                $('#clientPoaClient').val('');
                $('#creditForClient').val('');
                $('#clientBsEciAcctNo').val('');
                $('#clientBsEciFwNo').val('');
                document.getElementById('clientCreditClient').innerHTML = "";
                document.getElementById('clientPoa').innerHTML = "";
                $('#newClientContact').attr('checked', false);
                var path = $('#path').val();
                //   $('#clntNotes').attr("src", path + "/img/icons/e_contents_view.gif");
                displayClientDetails();
            }
        }
    })
    $('#forwarderNameClient').keyup(function () {
        var moduleName = $('#moduleName').val();
        if (moduleName != 'Imports') {
            var Forwarder = $('#forwarderNameClient').val();
            if (Forwarder == "") {
                $('#forwarderName').val('');
                $('#forwarderCode').val('');
                $('#fFmc').val('');
                $('#fotiNumber').val('');
                $('#fcommodityNumber').val('');
                $('#fretailCommodity').val('');
                $('#dupForwarderPhone').val('');
                $('#dupForwarderFax').val('');
                $('#forwarder_acct_type').val('');
                $('#forwarder_sub_type').val('');
                $('#forwarderFax').val('');
                $('#forwarderCity').val('');
                $('#forwarderCountry').val('');
                $('#forwarderState').val('');
                $('#forwarderZip').val('');
                document.getElementById('forwarder').innerHTML = "";
                $('#forwarderAddress').val('');
                $('#forwardercontactClient').val('');
                $('#forwarderEmail').val('');
                $('#forwarderPhone').val('');
                $('#forwarderCredit').val('');
                $('#forwarderNameClient').val('');
                $('#forwarderCodeClient').val('');
                $('#forwarderaccttype').val('');
                $('#forwardersubtype').val('');
                $('#forwarderFaxClient').val('');
                $('#forwarderCityClint').val('');
                $('#forwarderCountryClient').val('');
                $('#forwarderStateClient').val('');
                $('#forwarderZipClient').val('');
                document.getElementById('forwarder1').innerHTML = "";
                document.getElementById('forwarderCreditClient').innerHTML = '';
                $('#forwarderAddressClient').val('');
                $('#forwarderEmailClient').val('');
                $('#forwarderPhoneClient').val('');
                $('#creditForForwarder').val('');
                $('#fwdBsEciFwNo').val('');
                $('#fwdBsEciAcctNo').val('');
                $('#newForwarderContact').attr('checked', false);
                var path = $('#path').val();
                $('#fwdNotes').attr("src", path + "/img/icons/e_contents_view.gif");
                displayForwarderDetails();
                var client = $('#forwarderNameClient').val();
                var shipper = $('#shipperNameClient').val();
                var partyship = $('#shipperName').val();
                var partyfwd = $('#forwarderName').val();
                var thirdParty = $('input:radio[name=billForm]:checked').val();
                if ($('#radioP').attr('checked'))
                {
                    if (thirdParty != 'T' && thirdParty != 'A') {
                        if ((shipper != "" && client != "") && (partyship != "" && partyfwd != "")) {
                            document.getElementById('billF').checked = true;
                            checkBilltoCode();
                            validateBillToCode('F', this);
                        }
                        if ((shipper != "" && client == "") && (partyship != "" && partyfwd == "")) {
                            document.getElementById('billS').checked = true;
                            checkBilltoCode();
                            validateBillToCode('S', this);
                        }
                        if ((shipper == "" && client == "") && (partyship == "" && partyfwd == "")) {
                            document.getElementById('billF').checked = true;
                            checkBilltoCode();
                            validateBillToCode('F', this);
                        }
                    }
                }
            }
        }
    })
    $('#forwarderName').keyup(function () {
        var moduleName = $('#moduleName').val();
        var partyship = $('#shipperName').val();
        if (moduleName != 'Imports') {
            var Forwarder = $('#forwarderName').val();
            if (Forwarder == "") {
                $('#forwarderNameClient').val('');
                $('#forwarderCodeClient').val('');
                $('#forwarderaccttype').val('');
                $('#forwardersubtype').val('');
                $('#forwarderFaxClient').val('');
                $('#forwarderCityClint').val('');
                $('#forwarderCountryClient').val('');
                $('#forwarderStateClient').val('');
                $('#forwardercontactClient').val('');
                $('#forwarderZipClient').val('');
                document.getElementById('forwarder1').innerHTML = "";
                $('#forwarderAddressClient').val('');
                $('#forwarderEmailClient').val('');
                $('#forwarderPhoneClient').val('');
                $('#creditForForwarder').val('');
                $('#forwarderName').val('');
                $('#forwarderCode').val('');
                $('#forwarder_acct_type').val('');
                $('#forwarder_sub_type').val('');
                $('#forwarderFax').val('');
                $('#forwarderCity').val('');
                $('#forwarderCountry').val('');
                $('#forwarderState').val('');
                $('#forwarderZip').val('');
                document.getElementById('forwarder').innerHTML = "";
                document.getElementById('forwarder').innerHTML = "";
                document.getElementById('forwarderCreditClient').innerHTML = '';
                $('#forwarderAddress').val('');
                $('#forwarderEmail').val('');
                $('#forwarderPhone').val('');
                $('#forwarderCredit').val('');
                $('#fwdBsEciFwNo').val('');
                $('#fwdBsEciAcctNo').val('');
                displayForwarderDetails();
                if ($('#radioP').attr('checked'))
                {
                    if (partyship != "" && Forwarder != "") {
                        document.getElementById('billF').checked = true;
                    }
                    if (partyship != "" && Forwarder == "") {
                        document.getElementById('billS').checked = true;
                    }
                    if (partyship == "" && Forwarder == "") {
                        document.getElementById('billF').checked = true;
                    }
                }
            }
        }
    })
    $('#shipperName').keyup(function () {
        var moduleName = $('#moduleName').val();
        var shipper = $('#shipperName').val();
        if (shipper == "") {
            if (moduleName !== 'Imports') {
                $('#shipperNameClient').val('');
                $('#shipperCodeClient').val('');
                $('#shipperaccttype').val('');
                $('#shippersubtype').val('');
                $('#shipperContactClient').val('');
                $('#shipperFaxClient').val('');
                $('#shipperCityClient').val('');
                $('#shipperCountryClient').val('');
                $('#shipperStateClient').val('');
                $('#shipperZipClient').val('');
                $('#shipperAddressClient').val('');
                $('#shipperEmailClient').val('');
                $('#shipperPhoneClient').val('');
                $('#creditForShipper').val('');
                $('#shipperName').val('');
                $('#shipperCode').val('');
                $('#shipper_acct_type').val('');
                $('#shipper_sub_type').val('');
                $('#shipperFax').val('');
                $('#shipperCity').val('');
                $('#shipperCountry').val('');
                $('#shipperState').val('');
                $('#shipperZip').val('');
                $('#shipperPOAV').text('');
                $('.shipperPOAV').text('');
                $('#shipperCreditStatus').text('');
                document.getElementById('shipper1').innerHTML = "";
                //   document.getElementById('shipper').innerHTML = "";
                // document.getElementById('shipperCreditClient').innerHTML = '';
                $('#shipperAddress').val('');
                $('#shipperEmail').val('');
                $('#shipperPhone').val('');
                $('#shipperCredit').val('');
                $('#shipBsEciAcctNo').val('');
                $('#shipBsEciFwNo').val('');
                displayShipperDetails();
            } else {
                clearShipperValues(path);
            }
        }
    });
    var path = $('#path').val();
    $('#consigneeName').keyup(function () {
        var consigneeName = $('#consigneeName').val();
        if (consigneeName === "") {
            if (moduleName !== 'Imports') {
                clearConsigneeValuesExp(path);
                clearConsigneeValues(path);
                displayConsigneeDetails();
            } else {
                clearConsigneeValues(path);
                //showConsDetails();
            }
        }
    });
    $('#notifyName').keyup(function () {
        var moduleName = $('#moduleName').val();
        if (moduleName === 'Imports') {
            var Forwarder = $('#notifyName').val();
            if (Forwarder === "") {
                clearNotifyDetails(path);
            }
        }
    });
    $('#consigneeNameClient').keyup(function () {
        var moduleName = $('#moduleName').val();
        if (moduleName != 'Imports') {
            var Forwarder = $('#consigneeNameClient').val();
            if (Forwarder == "") {
                $('#consigneeName').val('');
                $('#dupConsigneePhone').val('');
                $('#dupConsigneeFax').val('');
                $('#cFmc').val('');
                $('#cotiNumber').val('');
                $('#ccommodityNumber').val('');
                $('#cretailCommodity').val('');
                $('#dupConsigneeName').val('');
                $('#consigneeCode').val('');
                $('#consignee_acct_type').val('');
                $('#consignee_sub_type').val('');
                $('#consigneeFax').val('');
                $('#consigneeCity').val('');
                $('#consigneeCountry').val('');
                $('#consigneeState').val('');
                $('#consigneeZip').val('');
                $('#consigneeAddress').val('');
                $('#consigneePhone').val('');
                $('#consigneeEmail').val('');
                $('#consigneeCredit').val('');
                $('#consigneeNameClient').val('');
                $('#consigneeContactName').val('');
                $('#dupConsName').val('');
                $('#consigneeCodeClient').val('');
                $('#consigneeaccttype').val('');
                $('#consigneesubtype').val('');
                $('#consigneeFaxClient').val('');
                $('#consigneeCityClient').val('');
                $('#consigneeCountryClient').val('');
                $('#consigneeStateClient').val('');
                $('#consigneeZipClient').val('');
                // document.getElementById('consignee1').innerHTML = "";
                // document.getElementById('consignee').innerHTML = "";
                $('#consigneeCreditStatus').text('');
                $('#consigneePOAV').text('');
                $('.consigneePoaValues').text('');
                $('#consigneeAddressClient').val('');
                $('#consigneeEmailClient').val('');
                $('#consigneePhoneClient').val('');
                $('#creditForConsignee').val('');
                $('#consBsEciAcctNo').val('');
                $('#consBsEciFwNo').val('');
                $('#newConsigneeContact').attr('checked', false);
                var path = $('#path').val();
                //  $('.conNotes').attr("src", path + "/img/icons/e_contents_view.gif");
                displayConsigneeDetails()
            }
        }
    })
    $('#dupConsName').keyup(function () {
        var moduleName = $('#moduleName').val();
        if (moduleName != 'Imports') {
            var Forwarder = $('#dupConsName').val();
            if (Forwarder == "") {
                $('#consigneeNameClient').val('');
                $('#consigneeName').val('');
                $('#dupConsigneeName').val('');
                $('#dupConsigneePhone').val('');
                $('#dupConsigneeFax').val('');
                $('#consigneeCode').val('');
                $('#consignee_acct_type').val('');
                $('#consignee_sub_type').val('');
                $('#consigneeFax').val('');
                $('#consigneeCity').val('');
                $('#consigneeCountry').val('');
                $('#consigneeState').val('');
                $('#consigneeZip').val('');
                $('#consigneeAddress').val('');
                $('#consigneePhone').val('');
                $('#consigneeEmail').val('');
                $('#consigneeCredit').val('');
                $('#consigneeNameClient').val('');
                $('#consigneeContactName').val('');
                $('#dupConsName').val('');
                $('#consigneeCodeClient').val('');
                $('#consigneeaccttype').val('');
                $('#consigneesubtype').val('');
                $('#consigneeFaxClient').val('');
                $('#consigneeCityClient').val('');
                $('#consigneeCountryClient').val('');
                $('#consigneeStateClient').val('');
                $('#consigneeZipClient').val('');
                document.getElementById('consignee1').innerHTML = "";
                document.getElementById('consignee').innerHTML = "";
                document.getElementById('consigneeCreditClient').innerHTML = "";
                $('#consigneeAddressClient').val('');
                $('#consigneeEmailClient').val('');
                $('#consigneePhoneClient').val('');
                $('#creditForConsignee').val('');
                $('#newConsigneeContact').attr('checked', false);
                displayConsigneeDetails()
            }
        }
    })

    $('#portOfLoading').keyup(function () {
        var moduleName = $('#moduleName').val();
        if (moduleName != 'Imports') {
            var pol = $('#portOfLoading').val();
            if (pol == "") {
                clearBookedForVoyage();
                $('#showAstarVoy').hide();
                $('#upcomingSection').show();
                submitAjaxFormforRates('displayemptyVoyage', '#lclBookingForm', '#upcomingSailing', '');
            }
        }
    });
    $('#supplierNameOrg').keyup(function () {
        var supplier = $('#supplierNameOrg').val();
        if (supplier == "") {
            $('#supplierNameOrg').val('');
            $('#supplierCode').val('');
        }
    });
    $('#portOfDestination').keyup(function () {
        var moduleName = $('#moduleName').val();
        if (moduleName != 'Imports') {
            var pol = $('#portOfDestination').val();
            if (pol == "") {
                clearBookedForVoyage();
                $('#showAstarVoy').hide();
                $('#upcomingSection').show();
                submitAjaxFormforRates('displayemptyVoyage', '#lclBookingForm', '#upcomingSailing', '');
            }
        }
    });
});
function hideQuickDr() {
    var fileId = $('#fileNumberId').val();
    var moduleName = $('#moduleName').val();
    if (moduleName != 'Imports') {
        if (fileId != "") {
            $('#quickDr').hide();
            $('#quickdr').hide();
        } else {
            $('#quickDr').show();
            $('#quickdr').show();
        }
    }
}

function getDoorOriginCityGoogleMap(path) {
    var doorOriginCity = $("#doorOriginCityZip").val();
    if (doorOriginCity == null || doorOriginCity == "") {
        $.prompt("Please Select Door Origin City");
    } else {
        if (doorOriginCity.indexOf("-") > -1 && doorOriginCity.indexOf("/") > -1) {
            var doorCityName = doorOriginCity.substring(doorOriginCity.indexOf("-") + 1, doorOriginCity.indexOf("/"));
            var doorZipCode = doorOriginCity.substring(0, doorOriginCity.indexOf("-"));
        }
        var href = path + "/lclServiceMap.do?methodName=displayDoorOriginCityMap&countryName=" + doorCityName + "&zipCode=" + doorZipCode;
        $("#lclDoorOriginCityMap").attr("href", href);
        $("#lclDoorOriginCityMap").colorbox({
            iframe: true,
            width: "100%",
            height: "90%",
            title: "Door Origin City Map"
        });
    }
}

function getOriginGoogleMap(path, methodName, flagName) {
    var portOfOrigin = $("#portOfOriginR").val();
    if (portOfOrigin == null || portOfOrigin == "") {
        $.prompt("Origin CFS is required");
        $("#portOfOriginR").css("border-color", "red");
    } else {
        if (flagName == 'I') {
            originUnloc = portOfOrigin.substring(portOfOrigin.lastIndexOf("(") + 1, portOfOrigin.lastIndexOf(")"));
        } else {
            originUnloc = $("#originUnlocationCode").val();
        }
        var href = path + "/lclServiceMap.do?methodName=" + methodName + "&countryName=" + originUnloc;
        $("#lclOriginMap").attr("href", href);
        $("#lclOriginMap").colorbox({
            iframe: true,
            width: "100%",
            height: "90%",
            title: "Origin Map"
        });
    }
}

function getDestinationGoogleMap(path, methodName, flagName) {
    var finalDestination = $("#finalDestinationR").val();
    if (finalDestination == null || finalDestination == "") {
        $.prompt("Destination is required");
        $("#finalDestinationR").css("border-color", "red");
    } else {
        if (flagName == 'I') {
            countryName = $("#unlocationCode").val();
        } else {
            if (finalDestination.indexOf("/") > -1 && finalDestination.indexOf("(") > -1) {
                countryName = finalDestination.substring(finalDestination.indexOf("/") + 1, finalDestination.indexOf("("));
            }
        }
        var href = path + "/lclServiceMap.do?methodName=" + methodName + "&countryName=" + countryName;
        $("#lclDestinationMap").attr("href", href);
        $("#lclDestinationMap").colorbox({
            iframe: true,
            width: "100%",
            height: "90%",
            title: "Destination Map"
        });
    }
}
function checkHazmatBoth() {
    var fileNumberId = $('#fileNumberId').val();
    if (fileNumberId != null && fileNumberId != "" && fileNumberId != '0') {
        var haz = $('.hazmat').val();
        if (haz == 'Haz') {
            $('#hazmatFound').html("<span style='color:red;font-weight:bold;'>** HAZ **</span>");
            $('#hazmatFound1').html("<span style='color:red;font-weight:bold;'>** HAZ **</span>");
        }
    }
}

function notesCount(path, className, acctNo, type) {//BlueScreen notes icon color change
    var moduleName = $('#moduleName').val();
    if (acctNo !== "") {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkNotesCount",
                param1: acctNo,
                param2: moduleName,
                param3: type
            },
            success: function (data) {
                if (data === "true") {
                    $('.' + className).attr("src", path + "/img/icons/e_contents_view1.gif");
                } else {
                    $('.' + className).attr("src", path + "/img/icons/e_contents_view.gif");
                }
            }
        });
    }
}

function displayNotes(path, acctNo, id, fileId, fileNo) {//dispaly blueScreen Customer Notes
    var href = "";
    var refpath = path + "/lclRemarks.do?methodName=display&actions=specialNotes&moduleId=Booking&fileNumberId=" + fileId + "&fileNumber=" + fileNo;
    if (id === 'clntNotes') {
        href = refpath + "&clntAcctNo=" + acctNo + "&clntId=" + id;
    }
    if (id === 'shpNotes') {
        href = refpath + "&shpAcctNo=" + acctNo + "&shpId=" + id;
    }
    if (id === 'fwdNotes') {
        href = refpath + "&frwdAcctNo=" + acctNo + "&fwdId=" + id;
    }
    if (id === 'conNotes' || id === 'notNotes' || id === 'notify2Notes') {
        href = refpath + "&consAcctNo=" + acctNo + "&consId=conNotes";
    }


    //var href=path + "/bluescreenCustomerNotes.do?methodName=displayNotes&customerNo=" + acctNo + "&customerName=" + fileNo
    $('.' + id).attr("href", href);
    $('.' + id).colorbox({
        iframe: true,
        width: "80%",
        height: "80%",
        title: "Notes"
    });
}

function updateCharges(id, index) {//Update bundle and release inv charges by checkbox
    var checkValues = "";
    if (index === 'B') {
        checkValues = $('#bundleToOf' + id).is(":checked");
    } else if (index === 'R') {
        checkValues = $('#relsToInv' + id).is(":checked");
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "updateBookingCharges",
            param1: id,
            param2: checkValues,
            param3: index
        },
        success: function (data) {
        }
    });
}
function displayNotesPopUp(path, fileNumberId, fileNumber, moduleName) {
    var href = path + "/lclRemarks.do?methodName=display&fileNumberId=" + fileNumberId +
            "&fileNumber=" + fileNumber + "&moduleName=" + moduleName + "&actions=manualNotes&moduleId=Booking";
    $(".notes").attr("href", href);
    $(".notes").colorbox({
        iframe: true,
        width: "65%",
        height: "75%",
        title: "Notes",
        scrolling: false
    });
}
function bkgPrint() {
    var fileId = $("#fileNumberId").val();
    var moduleName = $('#moduleName').val();
    if ($('#lockMessage').val() == '') {
        if (null !== fileId && fileId !== '') {
            if (isFormChanged()) {
                validateform(moduleName, 'true');
            } else {
                openPrintFaxPopUp();
            }
        } else {
            $.prompt("Please save the file before print/fax");
        }
    } else {
        openPrintFaxPopUp();
    }

}

function openPrintFaxPopUp() {
    var fileId = $("#fileNumberId").val();
    var fileNo = $("#fileNumber").val();
    var unitSsId = $("#unitSsId").val();
    var unitNo = $("#impUnitNo").val();
    var path = $('#path').val();
    var subject = "";
    var screenName;
    var trans = false;
    var cob = false;
    var moduleName = $('#moduleName').val();
    if (moduleName === 'Imports') {
        var transhipment = $("input:radio[name='transShipMent']:checked").val();
        if (transhipment === 'Y') {
            trans = true;
        }
        subject = "IMP-" + fileNo;
        screenName = 'LCLIMPBooking';
    } else {
        cob = $('#cob').val() === '' ? false : $('#cob').val();
        screenName = 'LCLBooking';
        var issuTerm = $("#terminal").val();
    }
    issuTerm = screenName === 'LCLBooking' ? issuTerm : '';
    var url = path + "/printConfig.do?screenName=" + screenName + "&fileId=" + fileId;
    url += "&fileNo=" + fileNo + "&comment=" + encodeURIComponent($('#externalComment').val());
    url += "&subject=" + subject + "&emailSubject=" + subject + "&transhipment=" + trans + "&cob=" + cob;
    url += "&pdfDocumentName=" + $('#pdfDocumentName').val() + "&unitSsId=" + unitSsId + "&unitNo=" + unitNo + "&issuingTerminal=" + issuTerm;
    GB_show("Print", url, 350, 1000);
}
function thirdPartyAcct() {
    if ($('#thirdPartyDisabled').val() === 'Y') {
        $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#thirdpartyDisableAcct').val() + "</span>");
        $('#thirdPartyname').val('');
        $('#thirdPartyDisabled').val('');
        $('#thirdpartyaccountNo').val('');
    }
}
function openTrackingPopUp(path, fileId, filenumber) {
    var href = path + '/lclTracking.do?methodName=display&fileNumber=' + filenumber + '&fileId=' + fileId;
    $.colorbox({
        iframe: true,
        width: "65%",
        height: "65%",
        href: href,
        title: "Tracking"
    });
}
function getRemarks(path, multipleSelect, selector) {
    var moduleName = $('#moduleName').val();
    var url = path + '/lclBooking.do?methodName=getPredefinedRemarks&multipleSelect=' + multipleSelect + "&moduleName=" + moduleName;
    $(selector).attr("href", url);
    $(selector).colorbox({
        iframe: true,
        width: "50%",
        height: "70%",
        title: "Predefined remarks"
    });
}

function clearRemarksSession()
{
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "clearRemarksSession",
            request: true
        },
        success: function (data) {
        }
    });
}
function okAlert(consigneeChargeFlag, amsFlag, result) {
    $.prompt("Hazmat info will be required prior to releasing this shipment", {
        buttons: {
            Ok: 0
        },
        submit: function (v) {
            if (v === 0) {
                saveBooking(consigneeChargeFlag, amsFlag, result);
            }
        }
    });
}

function hazValidateSaveButton(consigneeChargeFlag, amsFlag, result, fileNumberId, module) {
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "checkHaz",
            param1: fileNumberId,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            if (data) {
                if (module === 'Imports') {
                    $.prompt('UnHazmat No is required');
                } else {
                    okAlert(consigneeChargeFlag, amsFlag, result);
                }
            } else if (($("#lcl3p-container tr").length) === 0) {
                $.prompt('Please Enter Atleast One Hot Code');
            } else if (($("#lcl3p-container tr").length) !== 0) {
                $("#methodName").val('isCheckHazmatCode');
                var params = $("#lclBookingForm").serialize();
                params += "&fileId=" + $('#fileNumberId').val();
                $.post($("#lclBookingForm").attr("action"), params,
                        function (data) {
                            if (!data) {
                                $.prompt('Please Enter Atleast One Hazmat Hot Code');
                            } else {
                                saveBooking(consigneeChargeFlag, amsFlag, result);
                            }
                        });
            } else {
                saveBooking(consigneeChargeFlag, amsFlag, result);
            }
        }
    });
}
function checkValidHotCode(id1, id2) {
    $("." + id2).each(function () {
        if ($(this).val() === $("#" + id1).val()) {
            $.prompt("Hot code already exists.");
            $("#hotCodes").val('');
            return false;
        }
    });
}


function RecalculateRelayCharge(radioValue, doorOriginCityZip, pickupReadyDate) {//calculate rates
    var fileId = $("#fileNumberId").val();
    if (fileId !== "") {
        var origin = getOrigin();
        var module = $('#moduleName').val();
        var destination = getDestination();
        var pol = $('#polCode').val();
        var pod = $('#podCode').val();
        var methodName = "calculateCharges";
        var insurance = $('input:radio[name=insurance]:checked').val();
        $.prompt("Since the POL has changed, T&T Charges will be recalculated", {
            buttons: {
                Ok: 1
            },
            submit: function (v) {
                if (v === 1) {
                    showProgressBar();
                    submitAjaxForm(methodName, '#lclBookingForm', '#chargeDesc', origin, destination, pol, pod, radioValue, doorOriginCityZip, pickupReadyDate);
                    $('#showAstar').show();
                    $('#showAstarDestn').show();
                    if ($('#portOfOriginR').val() !== "") {
                        $('#portOfOriginR').addClass("text-readonly");
                        $('#portOfOriginR').attr("readonly", true);
                    }
                    if (module !== 'Exports') {
                        $('#finalDestinationR').addClass("text-readonly");
                        $('#finalDestinationR').attr("readonly", true);
                    }
                    $.prompt.close();
                }
            }
        });
    }
}

function coloadOrTerminal() {
    var trmnum = $('#trmnum').val();
    var rateType = $('input:radio[name=rateType]:checked').val();
    if ($("#fileNumberId").val() == '' && (trmnum === '59' || (rateType !== undefined && rateType === 'C'))) {
        $('#aesByN').attr('checked', true);
    }
    if ($("#terminal").val() == "QUEENS, NY/59")
    {
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "updateCommodityPersonalEffects",
                param1: $("#fileNumberId").val(),
                request: "true",
                dataType: "json"
            },
            success: function () {
            }
        });
    }
}


function getreleasedConsolidatedDR(fileId) {
    var status = "";
    $.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "getreleasedConsolidatedDR",
            param1: fileId,
            dataType: "json",
            request: "true"
        },
        async: false,
        success: function (data) {
            status = data;
        }
    });
    return status;
}
// for Exports #mantis :13725
function validateInlandRates() {
    var fileId = $('#fileNumberId').val();
    var pooDoor = $('input:radio[name=pooDoor]:checked').val();
    if (fileId !== "" && pooDoor === 'Y') {
        if (isValidateRates(fileId)) {
            var txt = 'This will cause the Inland rate to be removed. You must enter/calculate it again.';
            $.prompt(txt, {
                buttons: {
                    Ok: 1
                },
                submit: function (v) {
                    if (v === 1) {
                        showProgressBar();
                        submitAjaxFormForDeletingInland('deleteInlandForExports', '#lclBookingForm', '#chargeDesc');
                        hideProgressBar();
                        $.prompt.close();
                    }
                }
            });
        } else {
            submitAjaxFormForDeletingInland('deleteInlandForExports', '#lclBookingForm', '#chargeDesc');
        }
    }
}

function submitAjaxFormForDeletingInland(methodName, formName, selector) {
    var fileId = $('#fileNumberId').val();
    var moduleName = $('#moduleName').val();
    var deliveryCargoToCode = $('#deliveryCargoToCode').val();
    var deliverCargoToName = $('#deliverCargoToName').val();
    var deliverCargoToAddress = $('#deliverCargoToAddress').val();
    var deliverCargoToCity = $('#deliverCargoToCity').val();
    var deliverCargoToState = $('#deliverCargoToState').val();
    var deliverCargoToZip = $('#deliverCargoToZip').val();
    var deliverCargoToPhone = $('#deliverCargoToPhone').val();
    var deliverCargoToFax = $('#deliverCargoToFax').val();
    $("#methodName").val(methodName);
    var params = $(formName).serialize();
    params += "&fileId=" + fileId + "&moduleName=" + moduleName;
    params += "&deliverCargoToName=" + deliverCargoToName + "&deliverCargoToAddress=" + deliverCargoToAddress;
    params += "&deliverCargoToCity=" + deliverCargoToCity + "&deliverCargoToState=" + deliverCargoToState;
    params += "&deliverCargoToZip=" + deliverCargoToZip + "&deliverCargoToPhone=" + deliverCargoToPhone;
    params += "&deliveryCargoToCode=" + deliveryCargoToCode;

    $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                $.fn.colorbox.close();
            });
}

function ChangeBillToCode(billCode, ele) {
    var existBillTo = $('#previousbillToParty').val();
    var txt = validateBillToAcct(billCode);
    if (txt !== "") {
        var $billTo = $('input:radio[name=billForm]');
        $billTo.filter('[value=' + existBillTo + ']').attr('checked', true);
        $("#" + ele.id).attr("checked", false);
    } else {
        updateBillToCode(billCode);
        $('#previousbillToParty').val(billCode);
    }
}
function cfclDisable() {
    var pickedOnVoyageNo = $("#pickedOnVoyageNo").val();
    if (pickedOnVoyageNo !== '') {
        var cfcl = $('input:radio[name=cfcl]:checked').val();
        $('input:radio[name=cfcl]').attr('disabled', true);
        $('input:radio[name=cfcl]').val(cfcl);
    }
}
function addClientHotCodes(acctNo) {
    if ($('#fileNumberId').val() !== '' && $('#fileNumberId').val() !== null) {
        $("#methodName").val("addingClientHotCodes");
        var params = $("#lclBookingForm").serialize();
        params += "&accountNo=" + acctNo;
        $.post($("#lclBookingForm").attr("action"), params, function (data) {
            $("#hotCodesList").html(data);
            $("#hotCodesList", window.parent.document).html(data);
        });
    }
}
function notify(){
    var notifyCode = $('#notifyCode').val();
        if (notifyCode===''){
            $('#notifyAddress').val('');
            $('#notifyName').val('');
        }
}
