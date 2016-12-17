/*
 *  Document   : printDocument
 *  Author     : Rajesh
 */
var path = "/" + window.location.pathname.split('/')[1];
jQuery(document).ready(function () {
    jQuery("[title != '']").not("link").tooltip();
});
function openPrintStatusPopUp() {
    var href = path + '/emailSchedulers.do?methodName=display&fileName=' + jQuery('#fileNo').val() + "&isPrintPopUp=true";
    window.open(href, '_blank', 'width=1200,height=600,top=40,left=40,scrollbars=yes');
}
function openPopup() {
    jQuery("#printConfig").slideToggle();
    jQuery("#commentsPopup").slideToggle();
}

function doCancel() {
    jQuery("#printConfig").slideToggle();
    jQuery("#commentsPopup").slideToggle();
    jQuery(".voyageEmail").attr("checked", false);
    jQuery("#comments").val("");
}

function generatePDFReport() {
    var email = [];
    jQuery(".emailValues").each(function () {
        if (jQuery(this).is(":checked") === true) {
            email.push(jQuery(this).val());
        }
    });
    if (email.length === 0) {
        alert("Please select atleast one email");
    } else {
        var index = jQuery("#voyageIndex").val();
        var emailSubject = jQuery("#femailSubject").val();
        var docName = jQuery('#documentName' + index).val();
        var comments = jQuery('#comments').val();
        var unitSsId = jQuery('#fileId').val();
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.dwr.PrintUtil",
                methodName: "generateVoyNotiReport",
                param1: unitSsId,
                param2: docName,
                param3: email.toString(),
                param4: comments,
                param5: emailSubject,
                request: "true"
            },
            success: function (reportName) {
                jQuery(".voyageEmail").attr("checked", false);
                jQuery("#fileLocation" + index).val(reportName);
                jQuery("#printConfig").slideToggle();
                jQuery("#commentsPopup").slideToggle();
                jQuery('#displayMessage').text('Email Sent Successfully');
                setTimeout(function () {
                    jQuery("#displayMessage").fadeOut('slow', 'swing');
                }, 1000);
                setTimeout(function () {
                    jQuery("#displayMessage").fadeIn('slow', 'swing');
                }, 1500);
                setTimeout(function () {
                    jQuery("#displayMessage").fadeOut('slow', 'swing');
                }, 3000);
                return true;
            }
        });
    }
}
function openContactCodePdf(path) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.dwr.PrintUtil",
            methodName: "getPropertyPdf",
            param1: "application.contactCodeManual"
        },
        async: false,
        success: function (data) {
            var url = path + '/servlet/FileViewerServlet?fileName=' + data;
            window.open(url, 'Contact', 'width=1200,height=600,top=40,left=40,directories=no,location=no,linemenubar=no,toolbar=no,status=no');
        }
    });
}

function releaseOrdrEmailCheckedforCons() {
    var consigneeCustEmail = document.getElementById('houseConsigneeEmail1').value;
    var emailAddress = document.getElementById('toAddress').value;
    var defaultEmailAddress = document.getElementById('defaultEmailAddress').value;
    if (document.getElementById("releaseOrdrEmailCons").checked) {
        if (defaultEmailAddress == "Y") {
            emailAddress = "";
            document.getElementById('defaultEmailAddress').value = "";
        }
        if (emailAddress != "" && consigneeCustEmail != "" && consigneeCustEmail != null && consigneeCustEmail != undefined) {
            emailAddress = emailAddress + "," + consigneeCustEmail;
            document.getElementById('toAddress').value = emailAddress;
        } else {
            if (consigneeCustEmail != "" && consigneeCustEmail != null && consigneeCustEmail != undefined) {
                document.getElementById('toAddress').value = consigneeCustEmail;
            } else {
                document.getElementById('toAddress').value = emailAddress;
            }
        }
    } else {
        document.getElementById("releaseOrdrEmailCons").checked = false;
        document.getElementById('toAddress').value = "";
        if (document.getElementById("releaseOrdrEmailCfs").checked) {
            releaseOrdrEmailCheckedforCfs();
        }
        if (document.getElementById("releaseOrdrEmailIpi").checked) {
            releaseOrdrEmailCheckedforIpi();
        }
        if (document.getElementById("releaseOrdrEmailNotify").checked) {
            releaseOrdrEmailCheckedforNotify();
        }
        if (document.getElementById("releaseOrdrEmailNotify2").checked) {
            releaseOrdrEmailCheckedforNotify2();
        }
        if (!document.getElementById("releaseOrdrEmailCons").checked &&
                !document.getElementById("releaseOrdrEmailCfs").checked &&
                !document.getElementById("releaseOrdrEmailNotify").checked &&
                !document.getElementById("releaseOrdrEmailNotify2").checked &&
                !document.getElementById("releaseOrdrEmailIpi").checked) {
            defaultEmailAddressForReleaseOrder();
        }
    }
}
function releaseOrdrEmailCheckedforCfs() {
    var cFSDevWarhseManagerEmail = document.getElementById('cFSDevWarhseManagerEmail').value;
    var emailAddress = document.getElementById('toAddress').value;
    var defaultEmailAddress = document.getElementById('defaultEmailAddress').value;
    if (document.getElementById("releaseOrdrEmailCfs").checked) {
        if (defaultEmailAddress == "Y") {
            emailAddress = "";
            document.getElementById('defaultEmailAddress').value = "";
        }
        if (emailAddress != "" && cFSDevWarhseManagerEmail != "" && cFSDevWarhseManagerEmail != null && cFSDevWarhseManagerEmail != undefined) {
            emailAddress = emailAddress + "," + cFSDevWarhseManagerEmail;
            document.getElementById('toAddress').value = emailAddress;
        } else {
            if (cFSDevWarhseManagerEmail != "" && cFSDevWarhseManagerEmail != null && cFSDevWarhseManagerEmail != undefined) {
                document.getElementById('toAddress').value = cFSDevWarhseManagerEmail;
            } else {
                document.getElementById('toAddress').value = emailAddress;
            }
        }
    } else {
        document.getElementById("releaseOrdrEmailCfs").checked = false;
        document.getElementById('toAddress').value = "";
        if (document.getElementById("releaseOrdrEmailCons").checked) {
            releaseOrdrEmailCheckedforCons();
        }
        if (document.getElementById("releaseOrdrEmailIpi").checked) {
            releaseOrdrEmailCheckedforIpi();
        }
        if (document.getElementById("releaseOrdrEmailNotify").checked) {
            releaseOrdrEmailCheckedforNotify();
        }
        if (document.getElementById("releaseOrdrEmailNotify2").checked) {
            releaseOrdrEmailCheckedforNotify2();
        }
        if (!document.getElementById("releaseOrdrEmailCons").checked &&
                !document.getElementById("releaseOrdrEmailCfs").checked &&
                !document.getElementById("releaseOrdrEmailNotify").checked &&
                !document.getElementById("releaseOrdrEmailNotify2").checked &&
                !document.getElementById("releaseOrdrEmailIpi").checked) {
            defaultEmailAddressForReleaseOrder();
        }
    }
}
function releaseOrdrEmailCheckedforNotify() {
    var notifyCustEmail = document.getElementById('notifyPartyEmail1').value;
    var emailAddress = document.getElementById('toAddress').value;
    var defaultEmailAddress = document.getElementById('defaultEmailAddress').value;
    if (document.getElementById("releaseOrdrEmailNotify").checked) {
        if (defaultEmailAddress == "Y") {
            emailAddress = "";
            document.getElementById('defaultEmailAddress').value = "";
        }
        if (emailAddress != "" && notifyCustEmail != "" && notifyCustEmail != null && notifyCustEmail != undefined) {
            emailAddress = emailAddress + "," + notifyCustEmail;
            document.getElementById('toAddress').value = emailAddress;
        } else {
            if (notifyCustEmail != "" && notifyCustEmail != null && notifyCustEmail != undefined) {
                document.getElementById('toAddress').value = notifyCustEmail;
            } else {
                document.getElementById('toAddress').value = emailAddress;
            }
        }
    } else {
        document.getElementById("releaseOrdrEmailNotify").checked = false;
        document.getElementById('toAddress').value = "";
        if (document.getElementById("releaseOrdrEmailCons").checked) {
            releaseOrdrEmailCheckedforCons();
        }
        if (document.getElementById("releaseOrdrEmailIpi").checked) {
            releaseOrdrEmailCheckedforIpi();
        }
        if (document.getElementById("releaseOrdrEmailCfs").checked) {
            releaseOrdrEmailCheckedforCfs();
        }
        if (document.getElementById("releaseOrdrEmailNotify2").checked) {
            releaseOrdrEmailCheckedforNotify2();
        }
        if (!document.getElementById("releaseOrdrEmailCons").checked &&
                !document.getElementById("releaseOrdrEmailCfs").checked &&
                !document.getElementById("releaseOrdrEmailNotify").checked &&
                !document.getElementById("releaseOrdrEmailNotify2").checked &&
                !document.getElementById("releaseOrdrEmailIpi").checked) {
            defaultEmailAddressForReleaseOrder();
        }
    }
}
function releaseOrdrEmailCheckedforNotify2() {
    var notify2CustEmail = document.getElementById('notify2PartyEmail1').value;
    var emailAddress = document.getElementById('toAddress').value;
    var defaultEmailAddress = document.getElementById('defaultEmailAddress').value;
    if (document.getElementById("releaseOrdrEmailNotify2").checked) {
        if (defaultEmailAddress == "Y") {
            emailAddress = "";
            document.getElementById('defaultEmailAddress').value = "";
        }
        if (emailAddress != "" && notify2CustEmail != "" && notify2CustEmail != null && notify2CustEmail != undefined) {
            emailAddress = emailAddress + "," + notify2CustEmail;
            document.getElementById('toAddress').value = emailAddress;
        } else {
            if (notify2CustEmail != "" && notify2CustEmail != null && notify2CustEmail != undefined) {
                document.getElementById('toAddress').value = notify2CustEmail;
            } else {
                document.getElementById('toAddress').value = emailAddress;
            }
        }
    } else {
        document.getElementById("releaseOrdrEmailNotify2").checked = false;
        document.getElementById('toAddress').value = "";
        if (document.getElementById("releaseOrdrEmailCons").checked) {
            releaseOrdrEmailCheckedforCons();
        }
        if (document.getElementById("releaseOrdrEmailIpi").checked) {
            releaseOrdrEmailCheckedforIpi();
        }
        if (document.getElementById("releaseOrdrEmailCfs").checked) {
            releaseOrdrEmailCheckedforCfs();
        }
        if (document.getElementById("releaseOrdrEmailNotify").checked) {
            releaseOrdrEmailCheckedforNotify();
        }
        if (!document.getElementById("releaseOrdrEmailCons").checked &&
                !document.getElementById("releaseOrdrEmailCfs").checked &&
                !document.getElementById("releaseOrdrEmailNotify").checked &&
                !document.getElementById("releaseOrdrEmailNotify2").checked &&
                !document.getElementById("releaseOrdrEmailIpi").checked) {
            defaultEmailAddressForReleaseOrder();
        }
    }
}

function releaseOrdrEmailCheckedforIpi() {
    var ipiCfsEmail1 = parent.parent.jQuery("#ipiCfsEmail1").val();
    var emailAddress = document.getElementById('toAddress').value;
    var defaultEmailAddress = document.getElementById('defaultEmailAddress').value;
    if (document.getElementById("releaseOrdrEmailIpi").checked) {
        if (defaultEmailAddress == "Y") {
            emailAddress = "";
            document.getElementById('defaultEmailAddress').value = "";
        }
        if (emailAddress != "" && ipiCfsEmail1 != "" && ipiCfsEmail1 != null && ipiCfsEmail1 != undefined) {
            emailAddress = emailAddress + "," + ipiCfsEmail1;
            document.getElementById('toAddress').value = emailAddress;
        } else {
            if (ipiCfsEmail1 != "" && ipiCfsEmail1 != null && ipiCfsEmail1 != undefined) {
                document.getElementById('toAddress').value = ipiCfsEmail1;
            } else {
                document.getElementById('toAddress').value = emailAddress;
            }
        }
    } else {
        document.getElementById("releaseOrdrEmailIpi").checked = false;
        document.getElementById('toAddress').value = "";
        if (document.getElementById("releaseOrdrEmailCons").checked) {
            releaseOrdrEmailCheckedforCons();
        }
        if (document.getElementById("releaseOrdrEmailCfs").checked) {
            releaseOrdrEmailCheckedforCfs();
        }
        if (document.getElementById("releaseOrdrEmailNotify").checked) {
            releaseOrdrEmailCheckedforNotify();
        }
        if (document.getElementById("releaseOrdrEmailNotify2").checked) {
            releaseOrdrEmailCheckedforNotify2();
        }
        if (!document.getElementById("releaseOrdrEmailCons").checked &&
                !document.getElementById("releaseOrdrEmailCfs").checked &&
                !document.getElementById("releaseOrdrEmailNotify").checked &&
                !document.getElementById("releaseOrdrEmailNotify2").checked &&
                !document.getElementById("releaseOrdrEmailIpi").checked) {
            defaultEmailAddressForReleaseOrder();
        }
    }
}

function defaultEmailAddressForReleaseOrder() {
    var ipiCfsEmail1 = parent.parent.jQuery("#ipiCfsEmail1").val();
    var coloaderEmail1 = document.getElementById('coloaderEmail1').value;
    var cFSDevWarhseManagerEmail = document.getElementById('cFSDevWarhseManagerEmail').value;
    if (ipiCfsEmail1 != "" && ipiCfsEmail1 != null && ipiCfsEmail1 != undefined) {
        document.getElementById('toAddress').value = ipiCfsEmail1;
        document.getElementById('defaultEmailAddress').value = "Y";
    } else if (coloaderEmail1 != "" && coloaderEmail1 != null && coloaderEmail1 != undefined) {
        document.getElementById('toAddress').value = coloaderEmail1;
        document.getElementById('defaultEmailAddress').value = "Y";
    } else if (cFSDevWarhseManagerEmail != "" && cFSDevWarhseManagerEmail != null && cFSDevWarhseManagerEmail != undefined) {
        document.getElementById('toAddress').value = cFSDevWarhseManagerEmail;
        document.getElementById('defaultEmailAddress').value = "Y";
    }
}

function defaultFaxNumberForReleaseOrder() {
    var ipiCfsFax = parent.parent.jQuery("#ipiCfsSearchFax").val();
    var coloaderFax = document.getElementById('coloaderFax').value;
    var cFSDevFax = parent.parent.jQuery("#cfsWarehouseFax").val();
    if (ipiCfsFax != "" && ipiCfsFax != null && ipiCfsFax != undefined) {
        document.getElementById('toFaxNumber').value = ipiCfsFax;
    } else if (coloaderFax != "" && coloaderFax != null && coloaderFax != undefined) {
        document.getElementById('toFaxNumber').value = coloaderFax;
    } else if (cFSDevFax != "" && cFSDevFax != null && cFSDevFax != undefined) {
        document.getElementById('toFaxNumber').value = cFSDevFax;
    }
}

function releaseOrdrFaxCheckedforCons() {
    var houseConsigneeFax = document.getElementById('houseConsigneeFax').value;
    if (document.getElementById("releaseOrdrFaxCons").checked) {
        if (houseConsigneeFax != '' && houseConsigneeFax != null && houseConsigneeFax != undefined) {
            document.getElementById('toFaxNumber').value = houseConsigneeFax;
        } else {
            document.getElementById("releaseOrdrFaxCons").checked = false;
            defaultFaxNumberForReleaseOrder();
        }
        document.getElementById("releaseOrdrFaxCfs").checked = false;
        document.getElementById("releaseOrdrFaxNotify").checked = false;
        document.getElementById("releaseOrdrFaxNotify2").checked = false;
        document.getElementById("releaseOrdrFaxIpi").checked = false;
    } else {
        document.getElementById("releaseOrdrFaxCons").checked = false;
        defaultFaxNumberForReleaseOrder();
    }
}
function releaseOrdrFaxCheckedforCfs() {
    var cFSDevFax = parent.parent.jQuery("#cfsWarehouseFax").val();
    if (document.getElementById("releaseOrdrFaxCfs").checked) {
        if (cFSDevFax != '' && cFSDevFax != null && cFSDevFax != undefined) {
            document.getElementById('toFaxNumber').value = cFSDevFax;
        } else {
            document.getElementById("releaseOrdrFaxCfs").checked = false;
            defaultFaxNumberForReleaseOrder();
        }
        document.getElementById("releaseOrdrFaxCons").checked = false;
        document.getElementById("releaseOrdrFaxNotify").checked = false;
        document.getElementById("releaseOrdrFaxNotify2").checked = false;
        document.getElementById("releaseOrdrFaxIpi").checked = false;
    } else {
        document.getElementById("releaseOrdrFaxCfs").checked = false;
        defaultFaxNumberForReleaseOrder();
    }
}
function releaseOrdrFaxCheckedforNotify() {
    var notifyPartyFax = document.getElementById('notifyPartyFax').value;
    if (document.getElementById("releaseOrdrFaxNotify").checked) {
        if (notifyPartyFax != '' && notifyPartyFax != null && notifyPartyFax != undefined) {
            document.getElementById('toFaxNumber').value = notifyPartyFax;
        } else {
            document.getElementById("releaseOrdrFaxNotify").checked = false;
            defaultFaxNumberForReleaseOrder();
        }
        document.getElementById("releaseOrdrFaxCons").checked = false;
        document.getElementById("releaseOrdrFaxCfs").checked = false;
        document.getElementById("releaseOrdrFaxNotify2").checked = false;
        document.getElementById("releaseOrdrFaxIpi").checked = false;
    } else {
        document.getElementById("releaseOrdrFaxNotify").checked = false;
        defaultFaxNumberForReleaseOrder();
    }
}
function releaseOrdrFaxCheckedforNotify2() {
    var notify2PartyFax = document.getElementById('notify2PartyFax').value;
    if (document.getElementById("releaseOrdrFaxNotify2").checked) {
        if (notify2PartyFax != '' && notify2PartyFax != null && notify2PartyFax != undefined) {
            document.getElementById('toFaxNumber').value = notify2PartyFax;
        } else {
            document.getElementById("releaseOrdrFaxNotify2").checked = false;
            defaultFaxNumberForReleaseOrder();
        }
        document.getElementById("releaseOrdrFaxCons").checked = false;
        document.getElementById("releaseOrdrFaxCfs").checked = false;
        document.getElementById("releaseOrdrFaxNotify").checked = false;
        document.getElementById("releaseOrdrFaxIpi").checked = false;
    } else {
        document.getElementById("releaseOrdrFaxNotify2").checked = false;
        defaultFaxNumberForReleaseOrder();
    }
}
function releaseOrdrFaxCheckedforIpi() {
    var ipiCfsFax = parent.parent.jQuery("#ipiCfsSearchFax").val();
    if (document.getElementById("releaseOrdrFaxIpi").checked) {
        if (ipiCfsFax != '' && ipiCfsFax != null && ipiCfsFax != undefined) {
            document.getElementById('toFaxNumber').value = ipiCfsFax;
        } else {
            document.getElementById("releaseOrdrFaxIpi").checked = false;
            defaultFaxNumberForReleaseOrder();
        }
        document.getElementById("releaseOrdrFaxCons").checked = false;
        document.getElementById("releaseOrdrFaxCfs").checked = false;
        document.getElementById("releaseOrdrFaxNotify").checked = false;
        document.getElementById("releaseOrdrFaxNotify2").checked = false;
    } else {
        document.getElementById("releaseOrdrFaxIpi").checked = false;
        defaultFaxNumberForReleaseOrder();
    }
}
function getMyCCEmail() {
    if (document.getElementById("ccMyEmail").checked) {
        document.getElementById('ccAddress').value = document.getElementById('myEmail').value;
    } else {
        document.getElementById('ccAddress').value = '';
    }
}
function previewReport(screenName, documentName, index) {
    if (documentName === 'All House Bills of Lading' || documentName === 'Prepaid Invoices'
            || documentName === 'Collect Invoices') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclPrintUtil",
                methodName: "isBlFound",
                request: "true"
            },
            success: function (data) {
                if (data == 'failure') {
                    jQuery.prompt('No Posted Bill Of Lading available to print.');
                    return false;
                } else {
                    previewPDFReport(screenName, documentName, index);
                }
            }
        });
    } else if (screenName === 'LCLUnits' && documentName === 'Trucker OutBound Delivery') {
        var id = document.getElementById('fileId').value;
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclPrintUtil",
                methodName: "isBkgNoFound",
                param1: id
            },
            success: function (data) {
                if (data == 'failure') {
                    jQuery.prompt("Booking Number is Required");
                    return false;
                }
                else {
                    previewPDFReport(screenName, documentName, index);
                }
            }
        });
    }
    else {
        previewPDFReport(screenName, documentName, index);
    }
}
function previewPDFReport(screenName, documentName, index) {
    var fileLocation = jQuery('#previewLocation' + index).val();
    var fromAddress = jQuery('#fromEmailAddress').val();
    fromAddress = undefined != fromAddress && null != fromAddress ? fromAddress : "";
    var fromName = jQuery('#nameFromTerminal').val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.dwr.PrintUtil",
            methodName: "printReport",
            param1: screenName,
            param2: documentName,
            param3: "true",
            param4: fromAddress,
            param5: fromName,
            request: "true"
        },
        preloading: true,
        success: function (data) {
            jQuery('#previewLocation' + index).val(data);
            viewFile(data);
        }
    });
}
function resetLclBlCorrectionFile(correctedId, noticeNo, index) {//LCLBL Correction
    document.getElementById('fileLocation' + '1').value = "";
    document.getElementById('fileLocation' + '2').value = "";
    if (document.getElementById('fileLocation' + '3')) {
        document.getElementById('fileLocation' + '3').value = "";
    }
    if (document.getElementById('fileLocation' + '4')) {
        document.getElementById('fileLocation' + '4').value = "";
    }
    if (document.getElementById('fileLocation' + '5')) {
        document.getElementById('fileLocation' + '5').value = "";
    }
    if (document.getElementById('fileLocation' + '6')) {
        document.getElementById('fileLocation' + '6').value = "";
    }
    if (document.getElementById('fileLocation' + '7')) {
        document.getElementById('fileLocation' + '7').value = "";
    }
    if (document.getElementById('fileLocation' + '8')) {
        document.getElementById('fileLocation' + '8').value = "";
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.lcl.dwr.LclPrintUtil",
            methodName: "changeNoticeNo",
            param1: noticeNo,
            param2: index,
            request: "true"
        },
        success: function () {
            document.printConfigForm.blCorrectedId.value = correctedId;
            document.printConfigForm.exportFileCob.value = document.getElementById('exportFileCob').value;
            document.printConfigForm.pageAction.value = "doNothing";
            document.printConfigForm.submit();
        }
    });
}

function resetSessionForOriginalBl(ev) {//FCLBl Correction
    document.getElementById('fileLocation' + '1').value = "";
    document.getElementById('fileLocation' + '2').value = "";
    if (document.getElementById('fileLocation' + '3')) {
        document.getElementById('fileLocation' + '3').value = "";
    }
    if (document.getElementById('fileLocation' + '4')) {
        document.getElementById('fileLocation' + '4').value = "";
    }
    if (document.getElementById('fileLocation' + '5')) {
        document.getElementById('fileLocation' + '5').value = "";
    }
    if (document.getElementById('fileLocation' + '6')) {
        document.getElementById('fileLocation' + '6').value = "";
    }
    if (document.getElementById('fileLocation' + '7')) {
        document.getElementById('fileLocation' + '7').value = "";
    }
    if (document.getElementById('fileLocation' + '8')) {
        document.getElementById('fileLocation' + '8').value = "";
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.dwr.PrintUtil",
            methodName: "changeFileNo",
            param1: ev.substring(0, ev.indexOf('=')),
            request: "true"
        },
        success: function () {
            document.printConfigForm.pageAction.value = "doNothing";
            document.printConfigForm.submit();
        }
    });
}
function validateLabelPrint(obj, screenName, documentName) {
    if ((screenName === 'LCLIMPBooking' && documentName === 'Label Print')
            && (isNaN(obj.value) || Number(obj.value) < 1 || Number(obj.value) > 50)) {
        obj.value = "";
        obj.focus();
        jQuery.prompt("Invalid digit, Please Enter digit between 1-50", {
            callback: function () {
                return false;
            }
        });
    } else if (screenName === 'LCLBooking' && documentName === 'Label Print') {
        if (parseInt(obj.value) === 0) {
            jQuery.prompt("Label Copy cannot be 0");
            obj.value = "";
            obj.focus();
            return false
        }
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.logiware.common.dao.PropertyDAO",
                methodName: "getProperty",
                param1: "LabelPrintCount",
                dataType: "json"
            },
            success: function (data) {
                if (parseInt(obj.value) > data) {
                    jQuery.prompt("Labels Copy should be less than <span style=color:red>" + data + "</span>");
                    obj.value = "";
                    obj.focus();
                }
            }
        });
    }
}
function getToEmailAddressByFcl(bolNo, fileNo) {
    var toAddress = "";
    if (document.getElementById("contactsEmail") && document.getElementById("contactsEmail").checked) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getCodeCContactMail",
                param1: bolNo
            },
            async: false,
            success: function (data) {
                if (data != null && data != '') {
                    toAddress = data;
                    document.printConfigForm.toAddress.value = toAddress;
                }
            }
        });
    } else {
        document.printConfigForm.toAddress.value = toAddress;
    }
    if (document.getElementById("pullEmail") && document.getElementById("pullEmail").checked) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.dwr.PrintUtil",
                methodName: "pullClientEmail",
                param1: fileNo
            },
            async: false,
            success: function (data) {
                if (data != null && data != '') {
                    if (document.printConfigForm.toAddress.value != "") {
                        toAddress += ",";
                    }
                    toAddress += data;
                    document.printConfigForm.toAddress.value = toAddress;
                }
            }
        });
    } else {
        document.printConfigForm.toAddress.value = toAddress;
    }
}
function setLoginUserEmail() {
    clearFromValues();
    // clearPreviewLocation();
    jQuery("#pullEmailDocs").attr('checked', false);
    jQuery("#customerserviceemail").attr('checked', false);
    if (document.getElementById("pullEmailUser").checked) {
        jQuery('#fromEmailAddress').val(jQuery('#loginUserEmailId').val());
        jQuery('#nameFromTerminal').val(jQuery('#userFirstName').val());
        jQuery("#tempPhoneNo").val(jQuery("#phoneTerminalNo").val());
        jQuery("#tempFaxNo").val(jQuery("#faxTerminalNo").val());
        jQuery("#phoneTerminalNo").val(jQuery("#userTerminalPhoneNo").val());
        jQuery("#faxTerminalNo").val(jQuery("#userTerminalFaxNo").val());
    }
}
function setFromDocsEmail(billingTerminal) {
    clearFromValues();
    //clearPreviewLocation();
    jQuery("#pullEmailUser").attr('checked', false);
    jQuery("#customerserviceemail").attr('checked', false);
    if (jQuery("#pullEmailDocs").is(":checked")) {
        jQuery("#fromEmailAddress").val(jQuery('#docTerminalEmail').val());
        jQuery("#nameFromTerminal").val(jQuery('#docTerminalName').val());
        jQuery("#phoneTerminalNo").val(jQuery("#tempPhoneNo").val());
        jQuery("#faxTerminalNo").val(jQuery("#tempFaxNo").val());
    }
}
function setCustomerServiceEmail(billingTerminal) {
    clearFromValues();
    // clearPreviewLocation();
    jQuery("#pullEmailUser").attr('checked', false);
    jQuery("#pullEmailDocs").attr('checked', false);
    if (jQuery("#customerserviceemail").is(":checked")) {
        jQuery("#fromEmailAddress").val(jQuery('#terminalCustomerEmail').val());
        jQuery("#nameFromTerminal").val(jQuery('#terminalCustomerName').val());
        jQuery("#phoneTerminalNo").val(jQuery("#tempPhoneNo").val());
        jQuery("#faxTerminalNo").val(jQuery("#tempFaxNo").val());     
    }
}
function clearFromValues() {
    jQuery("#fromEmailAddress").val('');
    jQuery("#nameFromTerminal").val('');
}
function setEmailFormValues() {
    if (trim(document.getElementById('toAddress').value) == "") {
        document.getElementById('toAddress').focus();
        jQuery.prompt("Please enter 'TO Email Address'", {
            callback: function () {
                return false;
            }
        });
    }
    if (document.getElementById("emailSubject").value != emailSubject) {
        emailSubjectChanged = true;
    }
    emailCoverOk = true;
    Effect.SlideUp('emailTemplate');
    Effect.toggle('printConfig');
    document.getElementById("emailFormId").className = "buttonColor";
}
function clearPreviewLocation() {
    $('.previewLocation').each(function () {
        $(this).val('');
    });
}
function openVoyageAgentContact(path, accountName, accountNo, isEmailorFax) {
    var isVoyageContact = "true";
    if (accountNo === "") {
        jQuery.prompt('Please select Agent name.');
    } else {
        jQuery("#isEmailOrFax").val(isEmailorFax);
        var href = path + "/lclContactDetails.do?methodName=display&vendorNo=" + accountNo +
                "&vendorName=" + accountName + "&isVoyageContact=" + isVoyageContact;
        GB_show("Contact", href, 500, 800);
    }
}