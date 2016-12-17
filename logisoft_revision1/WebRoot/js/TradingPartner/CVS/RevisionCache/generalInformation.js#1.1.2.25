var path = "/" + window.location.pathname.split('/')[1];

function initSubsidiaryAccountAutocomplete() {
    jQuery("#subsidiaryAccountName").initautocomplete({
        url: path + "/autocompleter/common/action/getAutocompleterResults.jsp?query=SUBSIDIARY_ACCOUNT&template=tradingPartner&fieldIndex=1,2&",
        width: "525px",
        otherFields: "subsidiaryAccountNumber",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        scrollHeight: 200
    });
}

function addSubsidiary() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
            methodName: "addSubsidiaryAccount",
            forward: "/jsp/tradingPartner/generalInfo/subsidiaryAccounts.jsp",
            param1: jQuery("#subsidiaryAccountNumber").val(),
            param2: jQuery("#accountNo").val()
        },
        preloading: true,
        success: function (data) {
            jQuery("#subsidiaryAccountsDiv").html(data);
            initSubsidiaryAccountAutocomplete();
        }
    });
}

function removeSubsidiary(subsidiaryAccountNumber) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
            methodName: "removeSubsidiaryAccount",
            forward: "/jsp/tradingPartner/generalInfo/subsidiaryAccounts.jsp",
            param1: subsidiaryAccountNumber,
            param2: jQuery("#accountNo").val()
        },
        preloading: true,
        success: function (data) {
            jQuery("#subsidiaryAccountsDiv").html(data);
            initSubsidiaryAccountAutocomplete();
        }
    });
}

jQuery(document).ready(function () {
    if (jQuery("#subsidiaryAccountsDiv").length > 0) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                methodName: "showSubsidiaryAccounts",
                forward: "/jsp/tradingPartner/generalInfo/subsidiaryAccounts.jsp",
                param1: jQuery("#accountNo").val()
            },
            success: function (data) {
                jQuery("#subsidiaryAccountsDiv").html(data);
                initSubsidiaryAccountAutocomplete();
            }
        });
    }
    var array = new Array();
    var accountTypes = jQuery('#accountType').val();
    array = accountTypes.split(",");
    if (array.contains("C")) {
        document.tradingPartnerForm.accountType4.checked = true;
    }
    if (jQuery.trim(parent.jQuery("#eciAccountNo").val()) !== '') {
        jQuery("#accountType1").attr("checked", true);
    }
    if (jQuery.trim(parent.jQuery("#eciAccountNo2").val()) !== '') {
        jQuery("#accountType4").attr("checked", true);
    }
    if (jQuery.trim(parent.jQuery("#eciAccountNo3").val()) !== '') {
        jQuery("#accountType10").attr("checked", true);
    }
    jQuery("#tab" + jQuery("#selectedTab").val()).show();
    jQuery("#li" + jQuery("#selectedTab").val()).addClass("selected");
    jQuery("ul.htabs").each(function () {
        jQuery(this).find("li a").click(function () {
            jQuery("ul.htabs").find("li").removeClass("selected");
            jQuery(".pane").each(function () {
                jQuery(this).hide();
            });
            jQuery("#tab" + jQuery(this).attr("tabindex")).show();
            jQuery("#li" + jQuery(this).attr("tabindex")).addClass("selected");
        });
    });
    checkForVendor();
    setAccountType();
    getNotify();
    disableAutoFF();
    initCorporateAccountAutocomplete();
    setDisableMainAcct();
});
function setDisableMainAcct() {
    var corporateAcctTypeId = jQuery("#ecuReportingType").val();
    if (corporateAcctTypeId == '') {
        jQuery("#corporateAcctName").attr("readonly", true);
        jQuery('#corporateAcctName').addClass('BackgrndColorForTextBox');
        jQuery('#corporateAcctName').removeClass('textlabelsBoldForTextBox');
    } else {
        jQuery("#corporateAcctName").removeAttr("readonly");
        jQuery('#corporateAcctName').removeClass('BackgrndColorForTextBox');
        jQuery('#corporateAcctName').addClass('textlabelsBoldForTextBox');
    }
}

function initCorporateAccountAutocomplete() {
    jQuery("#corporateAcctName").initautocomplete({
        url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=CORPORATE_ACCOUNT_OPTIONS&template=one&fieldIndex=1,2&",
        width: "425px",
        otherFields: "corporateAcctId",
        resultsClass: "ac_results z-index",
        resultPosition: "absolute",
        scroll: true,
        shouldMatch :true,
        scrollHeight: 200,
        onblur: "setCorporateAcctOptionClear()"
    });
//    setCorporateAcctParam();
}
function setCorporateAcctOptionClear(){ 
    if(jQuery("#corporateAcctId").val() === ""){ 
        jQuery("#corporateAcctName").val('');
    }
}
function setCorporateAcctOption(){
    jQuery("#corporateAcctName").val('');
    jQuery("#corporateAcctId").val('');
    setCorporateAcctParam();
}
function setCorporateAcctParam() {
    var corporateAcctTypeId=jQuery("#ecuReportingType").val();
    if(corporateAcctTypeId==''){
        jQuery("#corporateAcctName").val('');
        jQuery("#corporateAcctId").val('');
        jQuery("#corporateAcctName").attr("readonly",true);
        jQuery('#corporateAcctName').addClass('BackgrndColorForTextBox');
        jQuery('#corporateAcctName').removeClass('textlabelsBoldForTextBox');
    }else{
        jQuery("#corporateAcctName").removeAttr("readonly");
        jQuery('#corporateAcctName').removeClass('BackgrndColorForTextBox');
        jQuery('#corporateAcctName').addClass('textlabelsBoldForTextBox');
        var url = path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=CORPORATE_ACCOUNT_OPTIONS&template=one&fieldIndex=1,2&";
        jQuery("#corporateAcctName").setOptions({
            url: url,
            extraParams: {
                param1: corporateAcctTypeId
            }
        });
    }
}

function clearData(){
    jQuery("#corporateAcctId").val('');
    setCorporateAcctParam();
}
function getSalesCode(ev) {
    if (event.keyCode === 9 || event.keyCode === 13 || event.keyCode === 0) {
        var code, codeDesc, id;
        id = ev.id;
        if (id === 'salesCode') {
            code = document.getElementById('salesCode').value;
            codeDesc = "";
        } else {
            codeDesc = document.getElementById('salesCodeName').value;
            code = "";
        }
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                methodName: "getSalesCodeInfoDWR",
                param1: code,
                param2: codeDesc
            },
            preloading: true,
            success: function (data) {
                if (id === 'salesCode') {
                    document.getElementById('salesCodeName').value = data;
                } else {
                    document.getElementById('salesCode').value = data;
                }
            }
        });
    }
}
function setFocus(id) {
    document.getElementById(id).focus();
}
function limitText(limitField, limitCount, limitNum) {
    if (limitField.value.length > limitNum) {
        limitField.value = limitField.value.substring(0, limitNum);
    } else {
        limitCount.value = limitNum - limitField.value.length;
    }
}
function limitTextchars(limitField, limitNum) {
    if (limitField.value.length > limitNum) {
        limitField.value = limitField.value.substring(0, limitNum).trim();
        return limitField.value;
    } else {
        return limitField.value;
    }
}
jQuery("#userName").blur(function () {
    var shipperUserName = jQuery.trim(jQuery("#userName").val());
    var consigneeUserName = jQuery.trim(jQuery("#consUserName").val());
    if (shipperUserName !== "" && shipperUserName === consigneeUserName) {
        jQuery.prompt("This username is already exist for this account in Website Cnee tab");
        jQuery("#userName").val("");
        jQuery("#userName").focus();
    }
});

jQuery("#consUserName").blur(function () {
    var shipperUserName = jQuery.trim(jQuery("#userName").val());
    var consigneeUserName = jQuery.trim(jQuery("#consUserName").val());
    if (consigneeUserName !== "" && shipperUserName === consigneeUserName) {
        jQuery.prompt("This username is already exist for this account in Website Ship/FF tab");
        jQuery("#consUserName").val("");
        jQuery("#consUserName").focus();
    }
});

function saveInfo() {
    if (trim(document.getElementById("accountName").value) === "") {
        jQuery.prompt("Please enter account name");
        return;
    }
    if (trim(jQuery("#ecuReportingType").val()) !== "" && (jQuery("#corporateAcctId").val()==='' || jQuery("#corporateAcctName").val()==='')) {
        jQuery("#corporateAcctName").css("border-color", "red");
        jQuery.prompt("Please enter main account name");
        return;
    }
    if (document.tradingPartnerForm.accountType10.checked) {
        parent.document.getElementById('subTypeContent').style.display = "block";
    } else {
        parent.document.getElementById('subTypeContent').style.display = "none";
    }
    if (document.getElementById("salesCode").value === '') {
        document.getElementById("salesCodeName").value = '';
    } else if (document.getElementById("salesCodeName").value === '') {
        document.getElementById("salesCode").value = '';
    }
    document.tradingPartnerForm.accountType.value = this.getAccountType();
    var accountName = trim(document.getElementById("accountName").value);
    document.tradingPartnerForm.accountName.value = accountName !== "" ? accountName.toString().toUpperCase() : accountName;
    if (parent.document.getElementById("accountName")) {
        parent.document.getElementById("accountName").value = document.tradingPartnerForm.accountName.value;
    }
    jQuery(":disabled").each(function () {
        jQuery(this).removeAttr("disabled");
    });
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
            methodName: "checkDuplicateAccountNumbers",
            param1: jQuery('#accountNo').val(),
            param2: jQuery('#eciAccountNo').val(),
            param3: jQuery('#eciAccountNo2').val(),
            param4: jQuery('#eciAccountNo3').val(),
            param5: jQuery('#sslineNumber').val()
        },
        preloading: true,
        success: function (data) {
            if (data !== '') {
                jQuery.prompt(data);
            } else {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                        methodName: "checkDuplicateUserName",
                        param1: jQuery("#accountNo").val(),
                        param2: jQuery("#userName").val(),
                        param3: jQuery("#consUserName").val()
                    },
                    preloading: true,
                    success: function (data) {
                        if (data !== '') {
                            jQuery.prompt(data);
                        } else {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                                    methodName: "checkDuplicateFirmsCode",
                                    param1: jQuery("#accountNo").val(),
                                    param2: jQuery("#firmsCode").val()
                                },
                                preloading: true,
                                success: function (data) {
                                    if (data !== '') {
                                        jQuery.prompt(data);
                                    } else {
                                        document.tradingPartnerForm.buttonValue.value = "saveGeneralInformation";
                                        document.tradingPartnerForm.submit();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    });
}

function getComCodeDesc(ev) {
    document.tradingPartnerForm.commDesc.value = "";
    if (event.keyCode === 9 || event.keyCode === 13) {
        if (document.tradingPartnerForm.commodity.value !== "000000"
                && document.tradingPartnerForm.commodity.value !== "" && document.tradingPartnerForm.commodity.value.length === 6) {
            var params = new Array();
            params['requestFor'] = "CommodityCodeDescription";
            params['commodityCode'] = document.tradingPartnerForm.commodity.value;

            var bindArgs = {
                url: path + "/actions/getChargeCodeDesc1.jsp",
                error: function (type, data, evt) {
                    jQuery.prompt("error");
                },
                mimetype: "text/json",
                content: params
            };

            var req = dojo.io.bind(bindArgs);
            dojo.event.connect(req, "load", this, "populateComCodeDescDojo");
        }
        if (document.tradingPartnerForm.commodity.value.length !== 6) {
            document.tradingPartnerForm.commodity.value = "";
            document.getElementById("commDesc").value = "";
        }
    }
}

function populateComCodeDescDojo(type, data, evt) {
    if (data) {
        if (data.commodityDescription === "") {
            document.tradingPartnerForm.commodity.value = "";
            document.getElementById("commDesc").value = "";
        }
        if (data.commodityDescription !== "COMMON COMMODITY") {
            document.getElementById("commDesc").value = data.commodityDescription;
        }
    } else {
        document.tradingPartnerForm.commodity.value = "";
        document.getElementById("commDesc").value = "";
    }
}

function getComCode(ev) {
    document.tradingPartnerForm.commodity.value = "";
    if (event.keyCode === 9 || event.keyCode === 13) {
        var params = new Array();
        params['requestFor'] = "CommodityCode";
        params['commodityCodeDescription'] = document.tradingPartnerForm.commDesc.value;

        var bindArgs = {
            url: path + "/actions/getChargeCodeDesc1.jsp",
            error: function (type, data, evt) {
                jQuery.prompt("error");
            },
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);
        dojo.event.connect(req, "load", this, "populateComCodeDojo");
    }
}

function populateComCodeDojo(type, data, evt) {
    if (data) {
        document.getElementById("commodity").value = data.commodityCode;
    } else {
        document.getElementById("commodity").value = "";
    }
}
function getComCodeDesc1(ev) {
    document.tradingPartnerForm.impCommodityDesc.value = "";

    if (event.keyCode === 9 || event.keyCode === 13) {
        if (document.tradingPartnerForm.impCommodity.value !== "000000"
                && document.tradingPartnerForm.impCommodity.value !== "") {
            var params = new Array();
            params['requestFor'] = "CommodityCodeDescription1";
            params['commodityCode1'] = document.tradingPartnerForm.impCommodity.value;
            var bindArgs = {
                url: path + "/actions/getChargeCodeDesc1.jsp",
                error: function (type, data, evt) {
                    jQuery.prompt("error");
                },
                mimetype: "text/json",
                content: params
            };
            var req = dojo.io.bind(bindArgs);
            dojo.event.connect(req, "load", this, "populateComCodeDescDojo1");
            if (document.tradingPartnerForm.impCommodity.value.length !== 6) {
                document.tradingPartnerForm.impCommodity.value = "";
                document.getElementById("impCommodityDesc").value = "";
            }
        }
    }
}

function populateComCodeDescDojo1(type, data, evt) {
    if (data) {
        if (data.commodityDescription1 === "") {
            document.tradingPartnerForm.impCommodity.value = "";
            document.getElementById("impCommodityDesc").value = "";
        }
        if (data.commodityDescription1 !== "COMMON COMMODITY") {
            document.getElementById("impCommodityDesc").value = data.commodityDescription1;
        }
    } else {
        document.tradingPartnerForm.impCommodity.value = "";
        document.getElementById("impCommodityDesc").value = "";
    }
}
function getComCode1(ev) {
    document.tradingPartnerForm.impCommodity.value = "";
    if (event.keyCode === 9 || event.keyCode === 13) {
        var params = new Array();
        params['requestFor'] = "CommodityCode1";
        params['commodityCodeDescription1'] = document.tradingPartnerForm.impCommodityDesc.value;
        var bindArgs = {
            url: path + "/actions/getChargeCodeDesc1.jsp",
            error: function (type, data, evt) {
                jQuery.prompt("error");
            },
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);
        dojo.event.connect(req, "load", this, "populateComCodeDojo1");
    }
}

function populateComCodeDojo1(type, data, evt) {
    if (data) {
        document.getElementById("impCommodity").value = data.commodityCode1;
    }
}
function selectsalescode(ev) {
    var params = new Array();
    params['requestFor'] = "SalesCodeDesc";
    params['salesCode'] = document.tradingPartnerForm.salesCode.value;
    var bindArgs = {
        url: path + "/actions/getChargeCodeDesc1.jsp",
        error: function (type, data, evt) {
            jQuery.prompt("error");
        },
        mimetype: "text/json",
        content: params
    };
    var req = dojo.io.bind(bindArgs);
    dojo.event.connect(req, "load", this, "populateSalesCodeDesc");
}
function populateSalesCodeDesc(type, data, evt) {
    if (data) {
        document.getElementById("salesCodeName").value = data.salesCodeDesc;
    }
}
function showSubType() {
    obj = document.getElementById('accountType10');
    if (obj.checked) {
        document.getElementById('subTypeContent').style.visibility = "visible";
        document.getElementById('subTypeContent').removeAttribute("disabled");
    } else {
        document.getElementById('subTypeContent').style.visibility = "hidden";
        document.getElementById('subTypeContent').setAttribute("disabled", "disabled");
    }
}
function getCommodityCodeLookUp() {
    var commodityDesc = document.tradingPartnerForm.commDesc.value;
    if (commodityDesc === '%') {
        commodityDesc = 'percent';
    }
    var href = path + '/commodityDescriptionLookUp.do?buttonValue=GeneralInfo&commDesc=' + commodityDesc;
    mywindow = window.open(href, '', 'width=900,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}
function setCommodityDesc(val1, val2) {
    document.tradingPartnerForm.commodity.value = val1;
    document.tradingPartnerForm.commDesc.value = val2;
}
function getImportCommodityCodeLookUp() {
    var commodityDesc = document.tradingPartnerForm.impCommodityDesc.value;
    if (commodityDesc === '%') {
        commodityDesc = 'percent';
    }
    var href = path + '/commodityDescriptionLookUp.do?buttonValue=GeneralImportInfo&commDesc=' + commodityDesc;
    mywindow = window.open(href, '', 'width=900,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}
function getRetailCommodityCodeLookUp() {
    var commodityDesc = document.tradingPartnerForm.retailCommodityDesc.value;
    if (commodityDesc === '%') {
        commodityDesc = 'percent';
    }
    var href = path + '/commodityDescriptionLookUp.do?buttonValue=retailCommodity&commDesc=' + commodityDesc;
    mywindow = window.open(href, '', 'width=900,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);

}
function getFclCommodityCodeLookUp() {
    var commodityDesc = document.tradingPartnerForm.fclCommodityDesc.value;
    if (commodityDesc === '%') {
        commodityDesc = 'percent';
    }
    var href = path + '/commodityDescriptionLookUp.do?buttonValue=fclCommodity&commDesc=' + commodityDesc;
    mywindow = window.open(href, '', 'width=900,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}
function getConsColoadCommodityCodeLookUp() {
    var commodityDesc = document.tradingPartnerForm.consColoadCommodityDesc.value;
    if (commodityDesc === '%') {
        commodityDesc = 'percent';
    }
    var href = path + '/commodityDescriptionLookUp.do?buttonValue=consColoadCommodity&commDesc=' + commodityDesc;
    mywindow = window.open(href, '', 'width=900,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}
function setConsColoadCommodity(val1, val2) {
    document.tradingPartnerForm.consColoadCommodity.value = val1;
    document.tradingPartnerForm.consColoadCommodityDesc.value = val2;
}
function getConsRetailCommodityCodeLookUp() {
    var commodityDesc = document.tradingPartnerForm.consRetailCommodityDesc.value;
    if (commodityDesc === '%') {
        commodityDesc = 'percent';
    }
    var href = path + '/commodityDescriptionLookUp.do?buttonValue=consRetailCommodity&commDesc=' + commodityDesc;
    mywindow = window.open(href, '', 'width=900,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}
function setConsRetailCommodity(val1, val2) {
    document.tradingPartnerForm.consRetailCommodity.value = val1;
    document.tradingPartnerForm.consRetailCommodityDesc.value = val2;
}
function getConsFclCommodityCodeLookUp() {
    var commodityDesc = document.tradingPartnerForm.consFclCommodityDesc.value;
    if (commodityDesc === '%') {
        commodityDesc = 'percent';
    }
    var href = path + '/commodityDescriptionLookUp.do?buttonValue=consFclCommodity&commDesc=' + commodityDesc;
    mywindow = window.open(href, '', 'width=900,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}
function setConsFclCommodity(val1, val2) {
    document.tradingPartnerForm.consFclCommodity.value = val1;
    document.tradingPartnerForm.consFclCommodityDesc.value = val2;
}
function setRetailCommodity(val1, val2) {
    document.tradingPartnerForm.retailCommodity.value = val1;
    document.tradingPartnerForm.retailCommodityDesc.value = val2;
}
function setFclCommodity(val1, val2) {
    document.tradingPartnerForm.fclCommodity.value = val1;
    document.tradingPartnerForm.fclCommodityDesc.value = val2;
}
function setImportCommodityDesc(val1, val2) {
    document.tradingPartnerForm.impCommodity.value = val1;
    document.tradingPartnerForm.impCommodityDesc.value = val2;
}
function getSalesCodeLookUp() {
    var salesCodeName = document.tradingPartnerForm.salesCodeName.value;
    if (salesCodeName === '%') {
        salesCodeName = 'percent';
    }
    var href = path + '/commodityDescriptionLookUp.do?buttonValue=SalesCode&commDesc=' + salesCodeName;
    mywindow = window.open(href, '', 'width=900,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}
function setSalesCode(val1, val2) {
    document.tradingPartnerForm.salesCode.value = val1;
    document.tradingPartnerForm.salesCodeName.value = val2;
}
function getConsSalesCodeLookUp() {
    var salesCodeName = document.tradingPartnerForm.consSalesCodeDesc.value;
    if (salesCodeName === '%') {
        salesCodeName = 'percent';
    }
    var href = path + '/commodityDescriptionLookUp.do?buttonValue=ConsSalesCode&commDesc=' + salesCodeName;
    mywindow = window.open(href, '', 'width=900,height=500,scrollbars=yes');
    mywindow.moveTo(200, 180);
}
function setConsSalesCode(val1, val2) {
    document.tradingPartnerForm.consSalesCode.value = val1;
    document.tradingPartnerForm.consSalesCodeDesc.value = val2;
}

function makeFormBorderless(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type === "button") {
            if (element.value === "Look Up") {
                element.style.visibility = "hidden";
            }
        }
    }
}
function load() {
    //            clearAccNo();
    getNotifyParty11();
}
function getNotifyParty11() {
    if (parent.parent.topFrame.tradingPartnerForm.accountType10.checked === true) {
        document.getElementById('vendorShipper').style.visibility = 'visible';
    } else {
        document.getElementById('vendorShipper').style.visibility = 'hidden';
    }
}
function maxLength(field, maxChars) {
    if (event.keyCode !== 8) {
        if (field.value.length >= maxChars) {
            event.returnValue = false;
            return false;
        }
    }
}
function TextAreaLimit(ev, length) {
    if (ev.value.length > length - 1 && event.keyCode !== 8 && event.keyCode !== 9) {
        jQuery.prompt('More than ' + length + ' characters are not allowed');
        var value = ev.value.substring(0, length);
        ev.value = value;
        event.returnValue = false;
        return false;
    }
}
function dontAllow1(ev) {
    if (ev.value === "000000") {
        ev.value = "";
    }
}
function dontAllow(ev) {
    if (ev.value === "000000") {
        ev.value = "";
    }
}
function getAccountType() {
    var acct_type = "";
    if (document.tradingPartnerForm.accountType1.checked) {
        acct_type = "S";
    }
    if (document.tradingPartnerForm.accountType3.checked) {
        acct_type += "," + "N";
    }
    if (document.tradingPartnerForm.accountType4.checked) {
        acct_type += "," + "C";
    }
    if (document.tradingPartnerForm.accountType8.checked) {
        acct_type += "," + "I";
    }
    if (document.tradingPartnerForm.accountType9.checked) {
        acct_type += "," + "E";
    }
    if (document.tradingPartnerForm.accountType10.checked) {
        acct_type += "," + "V";
    }
    if (document.tradingPartnerForm.accountType11.checked) {
        acct_type += "," + "O";
    }
    if (document.tradingPartnerForm.accountType13 && document.tradingPartnerForm.accountType13.checked) {
        acct_type += "," + "Z";
    }
    return acct_type;
}
function clearAccNo() {
    if (!document.tradingPartnerForm.accountType1.checked && !document.tradingPartnerForm.accountType10.checked) {
        //               &&  document.tradingPartnerForm.subType.value === ''){
        document.getElementById('eciAccountNo').value = "";
    }
    if (!document.tradingPartnerForm.accountType4.checked) {
        document.getElementById('eciAccountNo2').value = "";
    }
    if (!document.tradingPartnerForm.accountType10.checked) {
        document.getElementById('eciAccountNo3').value = "";
    }

}
function disableAutoCompleter(obj) {
    if (obj.checked) {
        if (obj.id === "shipperCheck") {
            if (document.getElementById("shipperName").value !== "") {
                confirmClearShipperCheckBox("Do you want to clear existing Shipper Details");
            } else {
                document.getElementById("shipperName").value = document.getElementById("shipperCopy").value;
                Event.stopObserving("shipperName", "blur");
            }
        } else if (obj.id === "consigneeCheck") {
            if (document.getElementById("consigneename").value !== "") {
                confirmClearConsigneeCheckBox("Do you want to clear existing Consignee Details");
            } else {
                document.getElementById("consigneename").value = document.getElementById("consigneeCopy").value;
                Event.stopObserving("consigneename", "blur");
            }
        }
    } else {
        if (obj.id === "shipperCheck") {
            if (document.getElementById("shipperName").value !== "") {
                confirmUncheckClearShipperCheckBox("Do you want to clear existing Shipper Details");

            } else {
                Event.observe("shipperName", "blur", function (event) {
                    var element = Event.element(event);
                    if (element.value !== $("shipperName_check").value) {
                        element.value = '';
                        $("shipperName_check").value = '';
                        $("shipperName").value = '';
                        $("shipperNo").value = '';
                    }
                }
                );

            }

        } else if (obj.id === "consigneeCheck") {
            if (document.getElementById("consigneename").value !== "") {
                confirmUncheckClearConsigneeBox("Do you want to clear existing Consignee Details");
            } else {
                Event.observe("consigneename", "blur", function (event) {
                    var element = Event.element(event);
                    if (element.value !== $("consigneename_check").value) {
                        element.value = '';
                        $("consigneename_check").value = '';
                        $("consigneename").value = '';
                        $("consigneeNo").value = '';
                    }
                }
                );
            }
        }
    }
}
function confirmUncheckClearShipperCheckBox(text) {
    confirmYesOrNo(text, "UncheckClearShipper");
}
function confirmUncheckClearConsigneeBox(text) {
    confirmYesOrNo(text, "UncheckClearConsignee");
}
function confirmClearShipperCheckBox(text) {
    confirmYesOrNo(text, "checkClearShipper");
}
function confirmClearConsigneeCheckBox(text) {
    confirmYesOrNo(text, "checkClearConsignee");
}
function confirmMessageFunction(id1, id2) {
    if (id1 === 'UncheckClearConsignee' && id2 === 'yes') {
        clearConsigneeName();
    } else if (id1 === 'UncheckClearConsignee' && id2 === 'no') {
        document.getElementById("consigneeCheck").checked = true;
    } else if (id1 === 'UncheckClearShipper' && id2 === 'yes') {
        clearShipperName();
    } else if (id1 === 'UncheckClearShipper' && id2 === 'no') {
        document.getElementById("shipperCheck").checked = true;
    } else if (id1 === 'checkClearShipper' && id2 === 'yes') {
        clearShipperName();
    } else if (id1 === 'checkClearShipper' && id2 === 'no') {
        document.getElementById("shipperCheck").checked = false;
    } else if (id1 === 'checkClearConsignee' && id2 === 'yes') {
        clearConsigneeName();
    } else if (id1 === 'checkClearConsignee' && id2 === 'no') {
        document.getElementById("consigneeCheck").checked = false;
    }

}
function clearShipperName() {
    if (document.getElementById('shipperCheck').checked) {
        document.getElementById("shipperName").value = "";
        document.getElementById("shipperNo").value = "";
        Event.stopObserving("shipperName", "blur");
    } else {
        document.getElementById("shipperName").value = "";
        document.getElementById("shipperNo").value = "";
        document.getElementById("shipperCopy").value = "";
        Event.observe("shipperName", "blur", function (event) {
            var element = Event.element(event);
            if (element.value !== $("shipperName_check").value) {
                element.value = '';
                $("shipperName_check").value = '';
                $("shipperName").value = '';
                $("shipperNo").value = '';
            }
        }
        );
    }
}
function clearConsigneeName() {
    if (document.getElementById('consigneeCheck').checked) {
        document.getElementById("consigneename").value = "";
        document.getElementById("consigneeNo").value = "";
        Event.stopObserving("consigneename", "blur");
    } else {
        document.getElementById("consigneename").value = "";
        document.getElementById("consigneeNo").value = "";
        document.getElementById("consigneeCopy").value = "";
        Event.observe("consigneename", "blur", function (event) {
            var element = Event.element(event);
            if (element.value !== $("consigneename_check").value) {
                element.value = '';
                $("consigneename_check").value = '';
                $("consigneename").value = '';
                $("consigneeNo").value = '';
            }
        }
        );
    }
}
function disableDojo(obj) {
    var path = "";
    var disable = 'disable';
    if (obj.id === "shipperName") {
        if (document.getElementById("shipperCheck").checked) {
            path = "&disableShipperDojo=" + disable;
            Event.stopObserving("shipperName", "blur");
        }
    } else if (obj.id === "consigneename") {
        if (document.getElementById("consigneeCheck").checked) {
            path = "&disableConsigneeDojo=" + disable;
            Event.stopObserving("consigneename", "blur");
        }
    }
    appendEncodeUrl(path);
}
function copyNotListedTp(from, to) {
    document.getElementById(to).value = from.value;
}
function onShipperBlur() {
    document.getElementById("shipperName").value = "";
    document.getElementById("shipperNo").value = "";
    document.getElementById("shipperCopy").value = "";
}
function onConsigneeBlur() {
    document.getElementById("consigneename").value = "";
    document.getElementById("consigneeNo").value = "";
    document.getElementById("consigneeCopy").value = "";
}
function checkForVendor() {
    if (document.tradingPartnerForm.accountType10.checked) {
        if (null !== parent.document.vendorframe && null !== parent.document.vendorframe.document.getElementById('subTypeValue')) {
            parent.document.vendorframe.document.getElementById('subTypeValue').style.display = "block";
            parent.document.vendorframe.document.getElementById('subTypeLabel').style.display = "block";
        }
        document.getElementById('subTypeValue').style.display = "block";
        document.getElementById('subTypeLabel').style.display = "block";
        document.getElementById('ssLineValue').style.display = "block";
        document.getElementById('ssLineLabel').style.display = "block";
    } else {
        document.getElementById('subTypeValue').style.display = "none";
        document.getElementById('subTypeLabel').style.display = "none";
        document.getElementById('ssLineValue').style.display = "none";
        document.getElementById('ssLineLabel').style.display = "none";
        if (null !== parent.document.vendorframe && null !== parent.document.vendorframe.document.getElementById('subTypeValue')) {
            parent.document.vendorframe.document.getElementById('subTypeValue').style.display = "none";
            parent.document.vendorframe.document.getElementById('subTypeLabel').style.display = "none";
        }
        document.getElementById('subType').value = "";
    }
    onChangeSubType();
}
function onChangeSubType(roleDuty) {
    var subType = document.getElementById('subType').value.toUpperCase();
    if (subType == "IMPORT CFS" && roleDuty == false) {
        jQuery.prompt("You do not have permission to mark this account as Imports CFS, please contact your manager");
        document.getElementById('subType').value = "";
        subType = "";
    }
    var sslineNumber = document.getElementById('sslineNumber').value;
    if (subType === "STEAMSHIP LINE" && trim(sslineNumber) !== "") {
        subType += " (" + sslineNumber.toUpperCase() + ")";
    }
    if (parent.document.getElementById('subType')) {
        parent.document.getElementById('subType').value = subType;
    }
    parent.document.vendorframe.document.getElementById('subType').value = subType;
}

function checkSubType() {
    var subType = document.getElementById('subType').value.toUpperCase();
    var firmsCode = jQuery.trim(jQuery("#firmsCode").val());
    if (subType === "IMPORT CFS" && firmsCode === "") {
        jQuery.prompt("Please enter a valid Firms Code before proceeding");
        jQuery("#subType").val("");
        subType = "";
    }
    var sslineNumber = document.getElementById('sslineNumber').value;
    if (subType === "STEAMSHIP LINE" && trim(sslineNumber) !== "") {
        subType += " (" + sslineNumber.toUpperCase() + ")";
    }
    if (parent.document.getElementById('subType')) {
        parent.document.getElementById('subType').value = subType;
    }
    parent.document.vendorframe.document.getElementById('subType').value = subType;
}

function checkForExistingRecord(obj) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
            methodName: "getCustomerOfAccountTypeZ"
        },
        preloading: true,
        success: function (data) {
            if (data !== "") {
                jQuery.prompt('Company with account type Z already exists');
                obj.checked = false;
            } else {
                obj.checked = true;
            }
        }
    });
}
function setAccountType() {
    parent.document.getElementById('accountType').value = document.getElementById('accountType').value;
    parent.document.getElementById('eciAccountNo').value = document.getElementById('eciAccountNo').value;
    parent.document.getElementById('eciAccountNo2').value = document.getElementById('eciAccountNo2').value;
    parent.document.getElementById('eciAccountNo3').value = document.getElementById('eciAccountNo3').value;
}
function hideUnhideDocCharge() {
    if (document.tradingPartnerForm.documentCharge[0].checked) {
        document.getElementById('documentChargeAmount').style.visibility = "visible";
    } else {
        document.getElementById('documentChargeAmount').style.visibility = "hidden";
        document.tradingPartnerForm.documentChargeAmount.value = "0.00";
    }
}
function setMaster() {
    //            parent.document.getElementById('master').value = document.getElementById('master').value;
    jQuery("select[name='master']", window.parent.document).val(jQuery("select[name='master'] option:selected").val());
}
var disabledMessage = "This Customer is Disabled";
function getConsigneeInfo() {
    jQuery(document).ready(function () {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "getCustAddressForNo",
                param1: document.tradingPartnerForm.consigneeNo.value,
                dataType: "json"
            },
            success: function (data) {
                populateConsigneeInfo(data);
            }
        });
    });
}
function populateConsigneeInfo(data) {
    if (data) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "checkForDisable",
                param1: data.acctNo
            },
            success: function (dataDup) {
                if (dataDup !== "") {
                    alertNew(dataDup);
                    document.getElementById("consigneename").value = "";
                    document.getElementById("consigneeNo").value = "";
                } else {
                    var type;
                    var coName = "";
                    var array1 = new Array();
                    if (data.acctType !== null) {
                        type = data.acctType;
                        array1 = type.split(",");
                    }
                    if (array1.contains("C")) {
                        document.getElementById("consigneename").value = data.acctName;
                        if (data.acctNo !== null) {
                            document.getElementById("consigneeNo").value = data.acctNo;
                        } else {
                            document.getElementById("consigneeNo").value = "";
                        }
                    } else {
                        alertNew("Select the Customers with Account Type C");
                        document.getElementById("consigneename").value = "";
                        document.getElementById("consigneeNo").value = "";
                    }
                }
            }
        });
    }
}
function disableTradingPartner(acctNo) {
    var forwardAcctNo = jQuery.trim(jQuery("#forwardAccount").val());
    if (forwardAcctNo === "") {
        jQuery.prompt("Please enter forward Account");
    } else {
        jQuery.prompt("Do you want to disable this account and merge it with " + forwardAcctNo + "?", {
            buttons: {
                Yes: true,
                No: false
            },
            callback: function (v) {
                if (v) {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                            methodName: "disableTradingPartner",
                            param1: acctNo,
                            param2: forwardAcctNo,
                            param3: "false",
                            request: true
                        },
                        preloading: true,
                        success: function (data) {
                            if (jQuery.trim(data).indexOf("Cannot disable the account due to following reasons") >= 0) {
                                jQuery.prompt(data);
                            } else if (jQuery.trim(data).indexOf("This account has") >= 0) {
                                jQuery.prompt(data, {
                                    buttons: {
                                        Yes: true,
                                        No: false
                                    },
                                    callback: function (v) {
                                        if (v) {
                                            jQuery.ajaxx({
                                                data: {
                                                    className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                                                    methodName: "disableTradingPartner",
                                                    param1: acctNo,
                                                    param2: forwardAcctNo,
                                                    param3: "true",
                                                    request: true
                                                },
                                                preloading: true,
                                                success: function (data) {
                                                    jQuery("#forwardAccount").attr('readonly', true);
                                                    jQuery("#forwardAccount").removeClass("textlabelsBoldForTextBox").addClass("BackgrndColorForTextBox");
                                                    jQuery("#disabledMessage", window.parent.document).html(data);
                                                    jQuery("#DisableImg").hide();
                                                    jQuery("#EnableImg").show();
                                                }
                                            });
                                        }
                                    }
                                });
                            } else {
                                jQuery("#forwardAccount").attr('readonly', true);
                                jQuery("#forwardAccount").removeClass("textlabelsBoldForTextBox").addClass("BackgrndColorForTextBox");
                                jQuery("#disabledMessage", window.parent.document).html(data);
                                jQuery("#DisableImg").hide();
                                jQuery("#EnableImg").show();
                            }
                        }
                    });
                }
            }
        });
    }
}

function enableTradingPartner(acctNo) {
    jQuery.prompt("Do you want to enable this account?", {
        buttons: {
            Yes: true,
            No: false
        },
        callback: function (v) {
            if (v) {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                        methodName: "enableTradingPartner",
                        param1: acctNo,
                        param2: jQuery('#eciAccountNo').val(),
                        param3: jQuery('#eciAccountNo2').val(),
                        param4: jQuery('#eciAccountNo3').val(),
                        param5: jQuery('#sslineNumber').val(),
                        param6: jQuery("#userName").val(),
                        param7: jQuery("#consUserName").val(),
                        param8: jQuery("#firmsCode").val(),
                        request: true
                    },
                    preloading: true,
                    success: function (data) {
                        if (jQuery.trim(data).indexOf("Account is enabled") < 0) {
                            jQuery.prompt(data);
                        } else {
                            jQuery("#forwardAccount").val("").attr('readonly', false);
                            jQuery("#forwardAccount").removeClass("BackgrndColorForTextBox").addClass("textlabelsBoldForTextBox");
                            jQuery("#disabledMessage", window.parent.document).html(data);
                            jQuery("#EnableImg").hide();
                            jQuery("#DisableImg").show();
                        }
                    }
                });
            }
        }
    });
}
function getForwarderInfo() {
    jQuery(document).ready(function () {
        if (document.tradingPartnerForm.billTo[0].checked && (document.getElementById("fowardername").value === "NO FF ASSIGNED"
                || document.getElementById("fowardername").value === "NO FF ASSIGNED / B/L PROVIDED"
                || document.getElementById("fowardername").value === "NO FRT. FORWARDER ASSIGNED")) {
            alertNew("Please change Bill To Party or change Forwarder, because Forwarder is NO FF ASSIGNED");
            document.getElementById("fowardername").value = "";
            document.getElementById("forwarderNo").value = "";
            return;
        }
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "getCustAddressForNo",
                param1: document.tradingPartnerForm.forwarderNo.value,
                dataType: "json"
            },
            success: function (data) {
                populateForwarderInfo(data);
            }
        });
    });
}
function populateForwarderInfo(data) {
    if (data) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "checkForDisable",
                param1: data.acctNo
            },
            success: function (dataDup) {
                if (dataDup !== "") {
                    alertNew(dataDup);
                    document.getElementById("fowardername").value = "";
                    document.getElementById("forwarderNo").value = "";
                } else {
                    fillForwarder(data);
                }
            }
        });
    }
}
function fillForwarder(data) {
    var array1 = new Array();
    var type;
    if (data.acctType !== null) {
        type = data.acctType;
    }
    if (((data.subType).toLowerCase()) === 'forwarder') {
        document.getElementById("fowardername").value = data.acctName;
        if (data.acctNo !== null) {
            document.getElementById("forwarderNo").value = data.acctNo;
        } else {
            document.getElementById("forwarderNo").value = "";
        }
    } else {
        alertNew("Select the Customers with Vendor (Sub type Forwarder)");
        document.getElementById("fowardername").value = "";
        document.getElementById("forwarderNo").value = "";
        return;
    }
}
function getShipperInfo() {
    jQuery(document).ready(function () {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "getCustAddressForNo",
                param1: document.tradingPartnerForm.shipperNo.value,
                dataType: "json"
            },
            success: function (data) {
                populateShipperInfo(data);
            }
        });
    });
}
function populateShipperInfo(data) {
    if (data) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "checkForDisable",
                param1: data.acctNo
            },
            success: function (dataDup) {
                if (dataDup !== "") {
                    alertNew(dataDup);
                    document.getElementById("shipperName").value = "";
                    document.getElementById("shipperNo").value = "";
                    return;
                } else {
                    var type;
                    var subtypes;
                    var array1 = new Array();
                    var array2 = new Array();
                    if (data.acctType !== null) {
                        type = data.acctType;
                        array1 = type.split(",");
                    }
                    if (data.subType !== null) {
                        subtypes = (data.subType).toLowerCase();
                        array2 = subtypes.split(",");
                    }
                    if (array1.contains("S") || array1.contains("F") || array1.contains("E") || array1.contains("I") || (array1.contains("V") && array2.contains("forwarder"))) {
                        if (data.acctNo !== null) {
                            document.tradingPartnerForm.shipperNo.value = data.acctNo;
                        } else {
                            document.tradingPartnerForm.shipperNo.value = "";
                        }
                    } else {
                        alertNew("Select the Customers with Account Type S,E,I and V with Sub Type Forwarder");
                        document.getElementById("shipperName").value = "";
                        document.getElementById("shipperNo").value = "";
                        return;
                    }
                }
            }
        });
    }
}
function checkForNumberAndDecimal(obj) {
    if (!/^([0-9]+(\.[0-9]{1,4})?)$/.test(obj.value)) {
        obj.value = "";
        alertNew("The amount you entered is not a valid");
        return;
    }
}
function changeBillTo() {
    if (document.tradingPartnerForm.prepaidOrCollect[0].checked) {
        document.tradingPartnerForm.billTo[0].checked = true;
        document.tradingPartnerForm.billTo[3].disabled = true;
        document.tradingPartnerForm.billTo[0].disabled = false;
        document.tradingPartnerForm.billTo[1].disabled = false;
        document.tradingPartnerForm.billTo[2].disabled = false;
    } else {
        document.tradingPartnerForm.billTo[3].checked = true;
        document.tradingPartnerForm.billTo[3].disabled = false;
        document.tradingPartnerForm.billTo[0].disabled = true;
        document.tradingPartnerForm.billTo[1].disabled = true;
        document.tradingPartnerForm.billTo[2].disabled = true;
    }
}
function validateBillTo() {
    if (document.tradingPartnerForm.billTo[0].checked && (document.getElementById("fowardername").value === "NO FF ASSIGNED"
            || document.getElementById("fowardername").value === "NO FF ASSIGNED / B/L PROVIDED"
            || document.getElementById("fowardername").value === "NO FRT. FORWARDER ASSIGNED")) {
        alertNew("Please change Bill To Party or change Forwarder, because Forwarder is NO FF ASSIGNED");
        document.tradingPartnerForm.billTo[1].checked = true;
        return;
    }
}
function validateDefaultAgent(obj) {
    if (obj.value === 'yes') {
        if (document.tradingPartnerForm.destination.value === '') {
            alertNew("Please Select Destination");
            obj.value = "";
        } else {
            var pod = document.tradingPartnerForm.destination.value;
            if (pod.indexOf("(") > -1 && pod.indexOf(")") > -1) {
                var podNew = pod.substring(pod.indexOf("(") + 1, pod.indexOf(")"));
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                        methodName: "getAgentForDestination",
                        param1: podNew,
                        dataType: "json"
                    },
                    success: function (data) {
                        if (data.accountNo !== undefined && data.accountNo !== "" && data.accountNo !== null) {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                    methodName: "checkForDisable",
                                    param1: data.accountNo
                                },
                                success: function (data) {
                                    if (data !== "") {
                                        alertNew(data);
                                        obj.value = "";
                                    }
                                }
                            });
                        } else {
                            alertNew("No Default Agent for this destination");
                            obj.value = "";
                        }
                    }
                });
            }
        }
    }
}
function setNVOmove() {
    document.getElementById("typeofMoveSelect").value = "DOOR TO PORT";
}
function disableAutoFF() {
    if (document.tradingPartnerForm.destination.value !== '') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "checkForTheRegion",
                param1: document.tradingPartnerForm.destination.value
            },
            success: function (data) {
                if (undefined !== data && null !== data) {
                    if (data === 'true') {
                        document.tradingPartnerForm.ffComm[0].disabled = false;
                        document.tradingPartnerForm.ffComm[1].disabled = false;
                    } else {
                        document.tradingPartnerForm.ffComm[1].checked = true;
                        document.tradingPartnerForm.ffComm[0].disabled = true;
                        document.tradingPartnerForm.ffComm[1].disabled = true;
                    }
                } else {
                    document.tradingPartnerForm.ffComm[0].disabled = false;
                    document.tradingPartnerForm.ffComm[1].disabled = false;
                }
            }
        });
    } else {
        document.tradingPartnerForm.ffComm[0].disabled = false;
        document.tradingPartnerForm.ffComm[1].disabled = false;
    }

}
function checkDoorMove() {
    var optionValue = document.getElementById("typeofMoveSelect").value;
    if (document.tradingPartnerForm.originZip.value !== "" && optionValue !== "DOOR TO DOOR" && optionValue !== "DOOR TO RAIL" && optionValue !== "DOOR TO PORT") {
        document.getElementById("typeofMoveSelect").value = "DOOR TO PORT";
        alertNew("Please select the option that starts from DOOR");
    }
}
function getNotify() {
    if (parent.document.geniframe !== undefined) {
        if (document.tradingPartnerForm.accountType4.checked) {
            parent.document.geniframe.getNotifyParty(parent.document.getElementById('notifyParty').value, true);
        } else {
            parent.document.geniframe.getNotifyParty(parent.document.getElementById('notifyParty').value, false);
        }
    }
}
function openRatesGrid(acctNo) {
    GB_show('Input Rates Manually', path + '/customerDefaultCharges.do?action=showHome&acctNo=' + acctNo, 400, 850);
}
function checkShipperOrForwarder() {
    if (!jQuery("#accountType1").is(":checked")) {
        if (confirm("Are you want to remove Account Type - S?")) {
            jQuery("#eciAccountNo").val("");
            parent.jQuery("#eciAccountNo").val("");
        } else {
            jQuery("#accountType1").attr("checked", true);
        }
    }
}
function checkConsigneeAccount() {
    if (!jQuery("#accountType4").is(":checked")) {
        if (confirm("Are you want to remove Account Type - C?")) {
            jQuery("#eciAccountNo2").val("");
            parent.jQuery("#eciAccountNo2").val("");
        } else {
            jQuery("#accountType4").attr("checked", true);
        }
    }
}
function checkVendorAccount() {
    if (!jQuery("#accountType10").is(":checked")) {
        if (confirm("Are you want to remove Account Type - V?")) {
            jQuery("#eciAccountNo3").val("");
            parent.jQuery("#eciAccountNo3").val("");
        } else {
            jQuery("#accountType10").attr("checked", true);
        }
    }
}
function validateShipper() {
    if (jQuery.trim(jQuery("#eciAccountNo").val()) === '') {
        jQuery("#accountType1").attr("checked", false);
    }
}
function validateConsignee() {
    if (jQuery.trim(jQuery("#eciAccountNo2").val()) === '') {
        jQuery("#accountType4").attr("checked", false);
    }
}
function validateVendor() {
    if (jQuery.trim(jQuery("#eciAccountNo3").val()) === '') {
        jQuery("#accountType10").attr("checked", false);
    }
}
function checkShipper() {
    jQuery("#accountType1").attr("checked", true);
}
function checkConsignee() {
    jQuery("#accountType4").attr("checked", true);
}
function checkVendor() {
    jQuery("#accountType10").attr("checked", true);
}

function blockSpecialChar(e) {
    var k = e.keyCode == 0 ? e.charCode : e.keyCode;
            return ((k > 64 && k < 91) || (k > 96 && k < 123) || k == 8   || (k >= 48 && k <= 57));
}
function onChangeCreditStatus(val) {
    var ecuDesignation = val.value;
    if (ecuDesignation === 'AG' || ecuDesignation === 'IC' || ecuDesignation === 'AA') {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO",
                methodName: "findIdByCodedesc",
                dataType: "json"
            },
            success: function (data) {
                parent.document.aciframe.document.getElementById('creditStatus').value = data[0];
                parent.document.aciframe.document.getElementById('exemptCreditProcess').checked = true;
                parent.document.aciframe.document.getElementById('creditLimit').value = '1,000,000.00';
                parent.document.aciframe.document.getElementById('creditRate').value = data[1];
            }
        });
    }
}
