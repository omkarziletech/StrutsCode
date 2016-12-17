function showSubType() {
    if (parent.document.getElementById('accountType').value.indexOf("V") >= 0) {
        document.getElementById('subTypeValue').style.display = "block";
        document.getElementById('subTypeLabel').style.display = "block";
    } else {
        document.getElementById('subTypeValue').style.display = "none";
        document.getElementById('subTypeLabel').style.display = "none";
    }
}

function limitText(limitField, limitCount, limitNum) {
    limitField.value = limitField.value.toUpperCase();
    if (limitField.value.length > limitNum) {
        limitField.value = limitField.value.substring(0, limitNum);
    } else {
        limitCount.value = limitNum - limitField.value.length;
    }
}
function setcreditterms() {
    if (document.tradingPartnerForm.climit.value == "0.00") {
        document.tradingPartnerForm.cterms.selectedIndex = 1;
    } else if (document.tradingPartnerForm.cterms.selectedIndex == 1) {
        document.tradingPartnerForm.cterms.selectedIndex = 2;
    }
}

function setcreditlimit() {
    if (document.tradingPartnerForm.cterms.selectedIndex == 1) {
        document.tradingPartnerForm.climit.value = "0.00";
    } else if (document.tradingPartnerForm.climit.value == "0.00") {
        alert("Please Enter the Credit Limit");
        document.tradingPartnerForm.climit.value = "";
        document.tradingPartnerForm.climit.focus();
    }
}

function addOrUpdatePayementMethod() {
    if (jQuery("#paymethod").val() == "CHECK") {
        valid = validateCHECK();
        if (!valid) {
            return;
        }
    } else if (jQuery("#paymethod").val() == "ACH") {
        valid = validateACH();
        if (!valid) {
            return;
        }
    }
    if (jQuery("#remail").val() != "") {
        var email = jQuery("#remail").val().replace(",", ";");
        var emails = email.split(";");
        for (var i = 0; i < emails.length; i++) {
            if (!isEmail(emails[i])) {
                alert("Please Enter valid Remit Email");
                return;
            }
        }
    }
    var value = document.tradingPartnerForm.rfax.value;
    for (var i = 0; i < value.length; i++) {
        if (value.indexOf(" ") == 0) {
            alert("Please dont start with white space");
            return;
        }
    }
    document.tradingPartnerForm.buttonValue.value = "addOrUpdatePayementMethod";
    document.tradingPartnerForm.submit();
}

function trim(str) {
    return str.replace(/^\s+|\s+$/g, "");
}
function validateCHECK() {
    if (trim(jQuery("#rfax").val()) == "" && trim(jQuery("#remail").val()) == "") {
        alert("Please Enter either Remit Fax or Remit Email");
        return false;
    }
    return true;
}
function validateACH() {
    if (jQuery("#bankname").val() == "") {
        alert("Please Enter Bank name");
        return false;
    } else if (jQuery("#baddr").val() == "") {
        alert("Please Enter Bank Address");
        return false;
    } else if (jQuery("#vacctno").val() == "") {
        alert("Please Enter Account Number");
        return false;
    } else if (jQuery("#vacctname").val() == "") {
        alert("Please Enter Account Name");
        return false;
    } else if (jQuery("#rfax").val() == "" && jQuery("#remail").val() == "") {
        alert("Please Enter either Remit Fax or Remit Email");
        return false;
    } else if (jQuery("#aba").val() == "" && jQuery("#swift").val() == "") {
        alert("Please Enter either ABA Routing or Swift");
        return false;
    } else if (jQuery("#aba").val() != "" && (trim(jQuery("#aba").val()).length != 9 || isNaN(trim(jQuery("#aba").val())))) {
        alert("ABA Routing should be a nine digit number");
        return false;
    }
    return true;
}

function setDefaultPaymentMethod() {
    var defaultPaymentMethod = document.getElementById("defaultPaymentMethod");
    if (defaultPaymentMethod.checked) {
        defaultPaymentMethod.value = "Y";
    } else {
        defaultPaymentMethod.value = "N";
    }

}
function getAddressForAPConfig() {
    document.tradingPartnerForm.buttonValue.value = "getAllAPAddress";
    document.tradingPartnerForm.submit();
}
function addAddress(val1) {
    document.tradingPartnerForm.contactId.value = val1
    document.tradingPartnerForm.buttonValue.value = "addAddressToApConfiguration";
    document.tradingPartnerForm.submit();
//parent.parent.GB_hide();
}
function saveAPConfig() {
    if (jQuery("#tempPaymemtSet").val() == "no") {
        alert("Please add Payment Method");
        return false;
    }
    if (jQuery('#fileLocation').val() == "" && document.tradingPartnerForm.wfile.checked) {
        //alert(dwr.util.getValue('fileLocation'));
        alert("Please Upload Image");
        return false;
    } else {
//        document.tradingPartnerForm.subType.value=document.getElementById('subType').value;

        document.tradingPartnerForm.buttonValue.value = "saveAPConfig";
        document.tradingPartnerForm.submit();
    }
}
function deletePaymentMethod(payMethod) {
    document.tradingPartnerForm.payMethodId.value = payMethod;
    document.tradingPartnerForm.buttonValue.value = "deletePaymentMethod";
    document.tradingPartnerForm.submit();
}
function view(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "button") {
            if (element.value == "Add" || element.value == "Add Address") {
                element.style.visibility = "hidden";
            }
        }
    }
}
function fileUpload(obj) {
    if (obj.checked) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.dwr.DwrUtil",
                methodName: "getCommentsForScanOrAttach",
                forward: "/jsps/Scan/ScanCommentsTemplate.jsp",
                param1: "uploadFile",
                param2: "0",
                param3: "",
                param4: "",
                param5: "",
                request: true
            },
            success: function (data) {
                if (data) {
                    showPopUp();
                    var div = jQuery("<div id='attachListDiv'></div>").html(data).addClass("popup").appendTo("#vendor");
                    var win = jQuery(window);
                    div.css({
                        width: "400px",
                        height: "100px",
                        position: "absolute",
                        top: (Math.max(0, ((win.height() - div.outerHeight()) / 2) + win.scrollTop()) + "px"),
                        left: (Math.max(0, ((win.width() - div.outerWidth()) / 2) + win.scrollLeft()) + "px")
                    });
                }
            }
        });
    }
}

function copyFile() {
    if (jQuery.trim(jQuery("#theFile").val()) === "") {
        jQuery.prompt("Please attach file.");
    } else {
        jQuery("#theFile").attr("name", "file");
        jQuery("#customerNumber").val(jQuery("#tradingPartnerId").val());
        jQuery("#action").val("upload");
        jQuery("#className").val("com.gp.cong.logisoft.dwr.UploadDownloaderDWR");
        jQuery("#methodName").val("uploadImageDocument");
        jQuery("#vendor").fileupload({
            preloading: true,
            success: function (data) {
                jQuery("#theFile").attr("name", "theFile");
                jQuery('#fileLocation').val(data);
                closeAttachList();
                document.tradingPartnerForm.wfile.checked = true;
            }
        });
    }
}

function closeAttachList() {
    jQuery("#attachListDiv").remove();
    closePopUp();
    document.tradingPartnerForm.wfile.checked = false;
}

function validateLimit(obj) {
    if (!isNaN(obj.value)) {
        if (obj.value.indexOf(".") >= 0) {
            var creditLimit = obj.value.split(".");
            if (creditLimit.length == 2) {
                if (creditLimit[0].length > 8) {
                    obj.value = "";
                    obj.focus();
                } else {
                    obj.value = addCommas(Number(obj.value).toFixed(2));
                }
            }
        } else if (obj.value.length > 8) {
            obj.value = addCommas(Number(obj.value.substring(0, 8)).toFixed(2));
            obj.focus();
        } else {
            obj.value = addCommas(Number(obj.value).toFixed(2));
        }
    } else {
        obj.value = "";
        obj.focus();
    }
}
function addCommas(nStr)
{
    nStr += '';
    x = nStr.split('.');
    x1 = x[0];
    x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    return x1 + x2;
}

function isEmail(s) {
    return String(s).search(/^\s*[\w\-\+_]+(\.[\w\-\+_]+)*\@[\w\-\+_]+\.[\w\-\+_]+(\.[\w\-\+_]+)*\s*$/) != -1;
}

function viewW9Image() {
    var fileName = jQuery("#fileLocation").val();
    if (fileName == "") {
        alert('There is no Image');
    } else {
        var url = rootPath + "/servlet/FileViewerServlet?fileName=" + fileName;
        if (fileName.indexOf(".xls") > 0 || fileName.indexOf(".doc") > 0
                || fileName.indexOf(".mht") > 0 || fileName.indexOf(".msg") > 0
                || fileName.indexOf(".csv") > 0 || fileName.indexOf(".ppt") > 0) {
            var iframe = jQuery("<iframe/>", {
                name: "iframe",
                id: "iframe",
                src: url
            }).appendTo("body").hide();
            iframe.load(function () {
                iframe.remove();
            });
        } else {
            if (window.parent.parent.parent.homeForm) {
                window.parent.parent.parent.showGreyBox("W-9 Image", url);
            } else if (window.parent.parent.homeForm) {
                window.parent.parent.showGreyBox("W-9 Image", url);
            } else {
                window.parent.showGreyBox("W-9 Image", url);
            }
        }
    }
}

if (document.getElementById("apSpecialistName")) {
    AjaxAutocompleter("apSpecialistName", "apSpecialistDiv", "apSpecialistId", "apSpecialistValid", rootPath + "/servlet/AutoCompleterServlet?action=User&get=id&textFieldId=apSpecialistName", "", "");
}

function setPopupAttributes(id) {
    document.getElementById(id).style.display = 'block';
    var IpopTop = (screen.height - document.getElementById(id).offsetHeight) / 2;
    var IpopLeft = (screen.width - document.getElementById(id).offsetWidth) / 2;
    document.getElementById(id).style.left = IpopLeft + document.body.scrollLeft - 50;
    document.getElementById(id).style.top = IpopTop + document.body.scrollTop - 200;
}
function closePopUpDiv(id) {
    document.getElementById(id).style.display = 'none';
    closePopUp();
}
function enableAddPopup() {
    showPopUp();
    this.setPopupAttributes('paymentDiv');
}

function editPaymentInfo(paymentMethodId) {
    var tradingPartnerId = document.tradingPartnerForm.tradingPartnerId.value;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.dwr.DwrUtil",
            methodName: "getPaymentMethod",
            param1: paymentMethodId,
            param2: tradingPartnerId,
            dataType: "json"
        },
        success: function (data) {
            if (data) {
                enableAddPopup();
                document.getElementById("allPaymentMethodDiv").style.display = 'none';
                document.getElementById("selectedPaymentMethodDiv").style.display = 'block';
                if (null != data.defaultpaymethod && data.defaultpaymethod == "Y") {
                    document.getElementById("defaultPaymentMethod").checked = true;
                } else {
                    document.getElementById("defaultPaymentMethod").checked = false;
                }
                if (data.paymethod == 'ACH') {
                    document.getElementById("forAchDocument").style.display = "block";
                } else {
                    document.getElementById("forAchDocument").style.display = "none";
                }
                var payMethod = document.getElementById("paymethod");
                var addOption = true;
                for (j = 0; j < payMethod.length; j++) {
                    if (payMethod[j].value == data.paymethod) {
                        payMethod[j].selected = true;
                        addOption = false;
                    }
                }
                if (addOption) {
                    var option = document.createElement('option');
                    option.text = data.paymethod;
                    option.value = data.paymethod;
                    option.selected = true;
                    payMethod.add(option);
                }
                jQuery("#addOrUpdate").val("Update");
                jQuery("#tempPaymentMethod").val(null != data.paymethod ? data.paymethod : "");
                jQuery("#bankname").val(null != data.bankname ? data.bankname : "");
                jQuery("#vacctno").val(null != data.vacctno ? data.vacctno : "");
                jQuery("#vacctname").val(null != data.vacctname ? data.vacctname : "");
                jQuery("#baddr").val(null != data.baddr ? data.baddr : "");
                jQuery("#rfax").val(null != data.rfax ? data.rfax : "");
                jQuery("#remail").val(null != data.remail ? data.remail : "");
                jQuery("#aba").val(null != data.aba ? data.aba : "");
                jQuery("#swift").val(null != data.swift ? data.swift : "");
            }
        }
    });
}

function showAchDocumentUpload() {
    if (jQuery("#paymethod").val() == 'ACH') {
        document.getElementById("forAchDocument").style.display = "block";
    } else {
        document.getElementById("forAchDocument").style.display = "none";
    }
}
function showAchDocument(id, achDocumentName) {
    window.parent.parent.showGreyBox(achDocumentName, rootPath + "/servlet/FileViewerServlet?domain=PaymentMethod&id=" + id + "&fileName=" + achDocumentName);
}
function addPaymentInfo() {
    enableAddPopup();
    document.getElementById("paymentDiv").style.width = '850px';
    document.getElementById("allPaymentMethodDiv").style.display = 'block';
    document.getElementById("selectedPaymentMethodDiv").style.display = 'none';
    document.getElementById("defaultPaymentMethod").checked = false;
    jQuery("#addOrUpdate").val("Add");
    jQuery("#bankname").val("");
    jQuery("#vacctno").val("");
    jQuery("#vacctname").val("");
    jQuery("#baddr").val("");
    jQuery("#rfax").val("");
    jQuery("#remail").val("");
    jQuery("#aba").val("");
    jQuery("#swift").val("");
    var paymentMethodAlreadyAdded = document.getElementsByName("paymentMethodAlreadyAdded");
    var payMethod = document.getElementById("paymethod");
    for (i = 0; i < paymentMethodAlreadyAdded.length; i++) {
        for (j = 0; j < payMethod.length; j++) {
            if (paymentMethodAlreadyAdded[i].value == payMethod[j].value) {
                payMethod.remove(j);
            }
        }
    }
    if (payMethod[0].value == 'ACH') {
        document.getElementById("forAchDocument").style.display = "block";
    } else {
        document.getElementById("forAchDocument").style.display = "none";
    }
}