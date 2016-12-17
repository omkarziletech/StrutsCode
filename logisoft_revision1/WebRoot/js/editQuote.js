var disableMessage = "This Customer is Disabled";
var junk = false;
var myArray = new Array();
var myArray1 = new Array();
var result = false;
var returnValue;
var importFlag = document.getElementById('fileType') && null !== document.getElementById('fileType').value && document.getElementById('fileType').value === 'I' ? true : false;
function loadData() {
    for (var i = 0; i < document.searchQuotationform.elements.length; i++) {
        if (document.searchQuotationform.elements[i].type == "radio" || document.searchQuotationform.elements[i].type == "checkbox") {
            if (document.searchQuotationform.elements[i].checked) {
                myArray[i] = document.searchQuotationform.elements[i].value;
            }
        } else {
            myArray[i] = document.searchQuotationform.elements[i].value;
        }
    }
}
function overrideComment() {
    if (document.getElementById("rampCheck").checked) {
        document.getElementById("inlandVal2").innerHTML = "INTERMODAL RAMP OVERRIDE";
    } else {
        document.getElementById("inlandVal2").innerHTML = "INLAND OVERRIDE";
    }
}
function showToolTipByChargeId(chargeType, w, e) {
    var comment;
    if (chargeType == 'intermodel') {
        comment = document.searchQuotationform.intermodelComments.value;
    } else {
        comment = document.searchQuotationform.deliveryChargeComments.value;
    }
    tooltip.showComments(comment, w, e);
}
function showToolTip(comment, chargeId, w, e) {
    tooltip.showComments(comment, w, e);
}
function displayToolTipDiv() {
    var quoteId = document.getElementById("quotationNo").value;
    if (null !== quoteId) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getIntermodelComments",
                param1: quoteId
            },
            success: function(data) {
                if (null !== data && jQuery.trim(data) !== "" && jQuery.trim(data) !== "null") {
                    document.searchQuotationform.intermodelComments.value = data;
                    document.getElementById('localDrayageCommentToolTipDiv').style.display = "block";
                    document.getElementById('override').style.display = "block";
                }
            }
        });
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getDeliveryChargeComments",
                param1: quoteId
            },
            success: function(data) {
                if (null !== data && jQuery.trim(data) !== "") {
                    document.searchQuotationform.deliveryChargeComments.value = data;
                    document.getElementById('deliveryChargeCommentToolTipDiv').style.display = "inline-block";
                }
            }
        });
    }
}
function closeCommentDiv(chargeType) {
    closePopUp();
    if (chargeType == 'delivery') {
        document.getElementById("deliveryChargeCommentDiv").style.display = 'none';
    }
    if (chargeType == 'OutOfGuage') {
        document.getElementById("outOfGaugeCommentDiv").style.display = 'none';
    } else {
        document.getElementById("localDrayageCommentDiv").style.display = 'none';
    }

}
function closeCommentsPopupByElementId(elementIdToClose, chargeId) {
    closePopUp();
    jQuery('#' + elementIdToClose).css({
        display: "none"
    });
    document.searchQuotationform.buttonValue.value = "adjustmentChargeComments";
    document.searchQuotationform.submit();
}
function getElelmentValueById(elementId) {
    return (jQuery('#' + elementId).val());
}
function submitComments(quoteId, userName, date, costType) {
    var comments = document.getElementById(costType).value;
    if (trim(comments) != "") {
        if (comments.length <= 460 && comments != '') {
            comments = comments + '(' + userName + '-' + date + ').';
        }
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "saveIntermodelComments",
                param1: quoteId,
                param2: comments,
                param3: costType
            },
            success: function(data) {
                if (null !== data) {
                    if (costType === 'localDrayageComments') {
                        document.searchQuotationform.intermodelComments.value = data;
                        document.getElementById('localDrayageCommentToolTipDiv').style.display = "block";
                        document.getElementById('override').style.display = "block";
                        closeCommentDiv('intermodel');
                    } else {
                        document.searchQuotationform.deliveryChargeComments.value = data;
                        closeCommentDiv('delivery');
                    }
                    var commentType = document.getElementById("commentType").value;
                    if (commentType === 'localdrayage') {
                        document.searchQuotationform.inland[1].checked = true;
                    }
                }
            }
        });
    } else {
        alertNew("Please Enter Comments!!!");
    }
}
function submitCommentsByChargeId(quoteId, chargeId, userName, date, costType, adjustmentAmtValue) {
    var comments = document.getElementById(costType).value;
    var isApplyToallContainerChecked = document.getElementById("applytoallcontainerchkbox").checked;
    if (trim(comments) != "") {
        if (comments.length <= 460 && comments != '') {
            comments = comments + '(' + userName + '-' + date + ').';
        }
        jQuery('#commentsPopupDiv').css({
            display: "none"
        });
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "saveCommentsAboutAdjustment",
                param1: quoteId,
                param2: chargeId,
                param3: comments,
                param4: adjustmentAmtValue,
                param5: isApplyToallContainerChecked
            },
            success: function(data) {
                if (null !== data) {
                    closePopUp();
                    document.searchQuotationform.buttonValue.value = "adjustmentChargeComments";
                    document.searchQuotationform.submit();
                }
            }
        });
    } else {
        alertNew("Please Enter Comments!!!");
    }
}
function saveRemarks() {
    var remarks = jQuery("#override-remarks").val();
    if (trim(remarks) !== "") {
        jQuery('#remarks-div').css("display", "none");
        closePopUp();
        jQuery("#buttonValue").val("onCarriageRemarks");
        jQuery("#onCarriage").prop("checked", true);
        document.searchQuotationform.submit();
    } else {
        alertNew("Please enter remarks");
    }
}
function cancelRemarks() {
    jQuery('#remarks-div').css("display", "none");
    closePopUp();
}
function onCarriageOverride() {
    //refer remarks-div in /jsp/common/popup.jsp included in /jsps/fclQuotes/fragment/tableQuoteRates.jspf
    showPopUp();
    jQuery("#override-remarks").val("");
    jQuery("#remarks-div").css({
        display: "block"
    });
    jQuery("#remarks-title").text("On-Carriage Remarks");
    jQuery("#remarks-title").prop("title", "On-Carriage remarks");
    jQuery("#remarks-msg").prop("title", "On-Carriage remarks");
    jQuery("#override-remarks").prop("title", "On-Carriage remarks goes here!");
}
function openRates() {
    var quoteId = jQuery("#quotationNo").val();
    var fileNo = jQuery("#moduleRefId").val();
    var hazmat = jQuery("#hazmatYes").is(":checked") === true ? "Y" : "N";
    var splEquip = document.searchQuotationform.specialequipment[0].checked === true ? "Y" : "N";
    var rateType = jQuery("#nonRated").is(":checked") === true ? "NonRated" : "rated";
    var url = "/logisoft/quoteCharge.do?markup=onCarriage&hazmat=" + hazmat + '&quoteNo=' + quoteId
            + '&spcleqpmt=' + splEquip + '&button=' + 'editquote' + '&fileNo=' + fileNo + '&ratedOption=' + rateType;
    GB_show('On-Carriage Rates', url, 500, 950);
}

function tabMoveDestination() {
    document.searchQuotationform.isTerminal.focus();
}
function tabMoveOrigin() {
    document.searchQuotationform.zip.focus();
}
function tabImpZip() {
    document.searchQuotationform.routedAgentCheck.focus();
}
function tabErt() {
    document.searchQuotationform.goodsdesc.focus();
}
function tabgoodsDesc() {
    document.searchQuotationform.costofgoods.focus();
}
function tabRemark() {
    document.getElementById("save1").focus();
}
function tabRemark1() {
    document.getElementById("cancel1").focus();
}
function tabMoveClient(importFlag) {
    if (importFlag == 'true') {
        document.searchQuotationform.isTerminal.focus();
    } else {
        document.searchQuotationform.portofDischarge.focus();
    }
}
function tabNvoMoveimp() {
    if (document.getElementById('fileType') && document.getElementById('fileType').value == 'I') {
        document.getElementById('doorDestination').focus();
    } else {
        document.getElementById('zip').focus();
    }
}
function tabNvoMove() {
    document.getElementById('fileTypeP').focus();
}
function tabMovErt() {
    if (document.getElementById('fileType') && document.getElementById('fileType').value == 'I') {
        document.getElementById('agentNo').focus();
    } else {
        document.getElementById('doorDestination').focus();
    }
}
function tabOrigin() {
    document.getElementById("getRates").focus();
}
function ratesFocus() {
    document.searchQuotationform.typeofMove.focus();
}
function copyValPol() {
    if (document.getElementById("nonRated").checked) {
        var pol = document.getElementById("isTerminal_check").value;
        document.getElementById("placeofReceipt").value = pol;

    } else {
        document.getElementById("placeofReceipt").value = "";
    }
}
function copyValPod() {
    if (document.getElementById("nonRated").checked) {
        var pod = document.getElementById("portofDischarge_check").value;
        document.getElementById("finalDestination").value = pod;

    } else {
        document.getElementById("finalDestination").value = "";
    }
}
function getTemp() {
    var path = "";
    if (!document.getElementById("nonRated").checked) {
        if (document.getElementById("isTerminal1City").checked) {
            path += "&typeOfmove=&destination=" + document.searchQuotationform.portofDischarge.value + "&radio=city";
        } else {
            path += "&typeOfmove=&destination=" + document.searchQuotationform.portofDischarge.value + "&radio=country";

        }
    } else {
        if (document.getElementById("isTerminal1City").checked) {
            path += "&typeOfmove=&nonRated=" + "yes" + "&radio=city";
        } else {
            path += "&nonRated=" + "yes" + "&radio=country";
        }
    }
    appendEncodeUrl(path);
    if (event.keyCode == 9 || event.keyCode == 13 || event.keyCode == 0) {
        setTimeout("getTypeOfMOve()", 150);
    }
}
function getTypeOfMOve() {
    if (jQuery.trim(document.searchQuotationform.isTerminal.value) !== "" && jQuery.trim(document.searchQuotationform.portofDischarge.value) !== "") {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getTypeOfMove",
                dataType: "json",
                param1: document.searchQuotationform.isTerminal.value,
                param2: document.searchQuotationform.portofDischarge.value
            },
            success: function(data) {
                typeOfMove(data);
            }
        });
    }
}
function compareWithOldArray(ev1, ev21) {
    if (ev1 == 3) {
        document.searchQuotationform.buttonValue.value = "viewprevious";
        document.searchQuotationform.submit();
    } else {
        document.searchQuotationform.buttonValue.value = "validate";
        document.searchQuotationform.submit();
    }
}
function compareWithOldArray1(ev1, ev21, fileNoParameter) {
    if (fileNoParameter != '') {
        if (document.searchQuotationform.isTerminal.value == "" || document.searchQuotationform.portofDischarge.value == ""
                || document.searchQuotationform.commcode.value == "") {
            if (document.searchQuotationform.isTerminal.value == "") {
                alertNew("Please Select Origin");
                jQuery("#isTerminal").css("border-color", "red");
                return;
            }
            if (document.searchQuotationform.commcode.value == "") {
                alertNew("Please Select Commodity Code");
                jQuery("#commcode").css("border-color", "red");
                return;

            }
            if (document.searchQuotationform.portofDischarge.value == "") {
                alertNew("Please Select Destination Port");
                jQuery("#portofDischarge").css("border-color", "red");
                return;
            }
            if (undefined != action && null != action && action == 'inland') {
                document.searchQuotationform.inland[1].checked = true;
            }
            return;
        } else {
            if (destinationDbValue == '' || originDbValue == '') {
                alertNew("Please Save New Origin and Destination");
                jQuery("#isTerminal").css("border-color", "red");
                jQuery("#portofDischarge").css("border-color", "red");
                return;
            }
            if (ev1 == 3) {
                document.searchQuotationform.buttonValue.value = "viewprevious";
                document.searchQuotationform.submit();
            } else {
                document.searchQuotationform.buttonValue.value = "validate";
                document.searchQuotationform.submit();
            }
        }
            } 
    else {
        if (ev1 == 3) {
            document.searchQuotationform.buttonValue.value = "viewprevious";
            document.searchQuotationform.submit();
        } else {
            document.searchQuotationform.buttonValue.value = "validate";
            document.searchQuotationform.submit();
        }
    }
        localStorage.removeItem('fclContactId');
        localStorage.removeItem('fclAccountNo');
}
function DisplayAlert(id, left, top, text, point) {
    document.getElementById(id).style.left = left + "px";
    document.getElementById(id).style.top = top + "px";
    document.getElementById("innerText").innerHTML = text;
    document.getElementById(id).style.display = "block";
    document.getElementById(id).style.left = point.x - 100 + "px";
    document.getElementById(id).style.top = point.y + "px";
    document.getElementById(id).style.zIndex = "1000";
    grayOut(true, "");
}
function alertNew(text) {
    var something = false;
    if (something) {
        // do this section
    } else {
        DisplayAlert("AlertBox", 100, 50, text, window.center({
            width: 100,
            height: 100
        }));
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
function okForReminderQuote() {
    document.getElementById("reminderBox").style.display = "none";
    grayOut(false, "");
    toBkg();
}
function yes() {
    document.getElementById("ConfirmBox").style.display = "none";
    grayOut(false, "");
    confirmMessageFunction(returnValue, "ok");
}
function No() {
    document.getElementById("ConfirmBox").style.display = "none";
    grayOut(false, "");
    confirmMessageFunction(returnValue, "cancel");
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
var collapsePrint = "";
var collapseid = "";
function call() {
    var pageWidth1 = "100%";
    var cvr = document.getElementById("cover");
    if (document.body && (document.body.scrollWidth || document.body.scrollHeight)) {
        pageWidth1 = document.body.scrollWidth + "px";
    } else {
        if (document.body.offsetWidth) {
            pageWidth1 = document.body.offsetWidth + "px";
        }
    }
    cvr.style.width = pageWidth1;
    cvr.style.height = "100%";
    cvr.style.display = "block";
    document.getElementById("newProgressBar").style.display = "block";
}
    function popupAddRates(windowname, val, copyQuote,importFlag) {
    if (!window.focus) {
        return true;
    }
    var portDestionation = document.searchQuotationform.portofDischarge.value;
    var haz = "";
    if (document.searchQuotationform.hazmat[0].checked) {
        haz = "Y";
    } else {
        haz = "N";
    }
        var fileType ="";
        if (importFlag == "true") {
            fileType = document.getElementById("fileType").value;
        }else{
            fileType = null;
        }
    var destinationPort = document.searchQuotationform.portofDischarge.value;
    var originPort = "";
    if (trim(document.searchQuotationform.isTerminal.value) != "") {
        originPort = document.searchQuotationform.isTerminal.value;
    } else {
        originPort = document.searchQuotationform.doorOrigin.value;
    }
    if (document.searchQuotationform.commcode.value == "") {
        alertNew("PLEASE SELECT COMMODITY CODE");
        jQuery("#commcode").css("border-color", "red");
        return;
    }
    if ((trim(document.searchQuotationform.portofDischarge.value) == "" || trim(document.searchQuotationform.isTerminal.value) == "")
            && document.getElementById('bulletRates').checked && document.getElementById("bulletRates")) {
        alertNew("Please Enter BOTH Origin and Destination when Looking up Bullet Rates");
        jQuery("#portofDischarge").css("border-color", "red");
        jQuery("#isTerminal").css("border-color", "red");
        return;
    }

    if ((trim(document.searchQuotationform.portofDischarge.value) == "" && trim(document.searchQuotationform.isTerminal.value) == "") || trim(document.searchQuotationform.customerName.value) == "") {
        if (document.getElementById("newClient").checked) {
            if (trim(document.searchQuotationform.customerName1.value) == "") {
                alertNew("PLEASE SELECT CLIENT");
                jQuery("#customerName1").css("border-color", "red");
                return;
            } else if (trim(document.searchQuotationform.portofDischarge.value) == "" && trim(document.searchQuotationform.isTerminal.value) == "") {
                alertNew("PLEASE SELECT DESTINATION PORT OR ORIGIN");
                return;
            }
        } else if (trim(document.searchQuotationform.customerName.value) == "") {
            alertNew("PLEASE SELECT CLIENT");
            jQuery("#customerName").css("border-color", "red");
            //document.searchQuotationform.portofDischarge.focus();
            return;
        } else {
            alertNew("PLEASE SELECT  DESTINATION PORT OR ORIGIN");
            //document.searchQuotationform.isTerminal.focus();
            return;
        }
    }
    if (document.searchQuotationform.commcode.value != "") {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "validateCommodityCode",
                param1: document.searchQuotationform.commcode.value
            },
            success: function(data) {
                if (data === "true") {
                    alertNew("Please Change Commodity Code for FCL");
                } else {
                    if (!document.getElementById('nonRated').checked && (trim(document.searchQuotationform.portofDischarge.value) == "" || trim(document.searchQuotationform.isTerminal.value) == "")) {
                        var searchBy = "city";
                        if (trim(document.searchQuotationform.portofDischarge.value) != "") {
                            if (!document.getElementById('destinationCity').checked) {
                                searchBy = "country";
                            }
                            var doorOrigin = document.getElementById("doorOrigin");
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                    methodName: "getOriginsForDestination",
                                    forward: "/jsps/fclQuotes/OriginAndDestination.jsp",
                                    param1: document.searchQuotationform.isTerminal.value,
                                    param2: document.searchQuotationform.portofDischarge.value,
                                    param3: searchBy,
                                    param4: document.searchQuotationform.zip.value,
                                    param5: document.searchQuotationform.commcode.value
                                },
                                preloading: true,
                                success: function(data) {
                                    if (jQuery.trim(data) === "") {
                                        alert("No Data Found!");
                                    } else if (data.length > 100) {
                                        showOriginDestinationList(data);
                                        if (null != doorOrigin && doorOrigin.value != '') {
                                            document.getElementById("doorOriginDisplay").innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;<b>' + doorOrigin.value + '</b>';
                                            document.getElementById("doorOriginDisplay").style.display = "block";
                                        } else {
                                            document.getElementById("top10Span").style.display = "none";
                                        }
                                    } else {
                                        document.searchQuotationform.isTerminal.value = data;
                                        document.searchQuotationform.isTerminal_check.value = data;
                                        openGetRates(haz, val, portDestionation, copyQuote);
                                    }
                                }
                            });
                        } else {
                            if (!document.getElementById('isTerminal1City').checked) {
                                searchBy = "country";
                            }
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                    methodName: "getDestinationsForOrigin",
                                    forward: "/jsps/fclQuotes/OriginAndDestination.jsp",
                                    param1: document.searchQuotationform.portofDischarge.value,
                                    param2: document.searchQuotationform.isTerminal.value,
                                    param3: searchBy,
                                    param4: document.searchQuotationform.zip.value,
                                    param5: document.searchQuotationform.commcode.value
                                },
                                preloading: true,
                                success: function(data) {
                                    if (data.length > 100) {
                                        showOriginDestinationList(data);
                                        var doorDestination = document.getElementById("doorDestination");
                                        if (null != doorDestination && doorDestination.value != '') {
                                            document.getElementById("doorOriginDisplay").innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;<b>' + doorDestination.value + '</b>';
                                            document.getElementById("doorOriginDisplay").style.display = "block";
                                        }
                                    } else {
                                        document.searchQuotationform.portofDischarge.value = jQuery.trim(data);
                                        document.searchQuotationform.portofDischarge_check.value = jQuery.trim(data);
                                        openGetRates(haz, val, portDestionation, copyQuote, fileType);
                                    }
                                }
                            });
                        }
                    } else if (document.getElementById("showAllCity") && document.getElementById("showAllCity").checked) {
                        //get list all the country cities
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                methodName: "getAllCountryPorts",
                                param1: document.searchQuotationform.portofDischarge.value
                            },
                            preloading: true,
                            success: function(data) {
                                var portOfDisch = document.searchQuotationform.portofDischarge.value;
                                var doorDestination = document.getElementById("doorDestination");
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
                                    if (null != doorDestination && doorDestination.value != '') {
                                        getDistancefromFDAndDestination(allCity, doorDestination.value, country, origin, haz, passedValue, document.searchQuotationform);
                                    }
                                    else {
                                        var destinationPort = document.searchQuotationform.portofDischarge.value;
                                        var originPort = "";
                                        if (trim(document.searchQuotationform.isTerminal.value) != "") {
                                            originPort = document.searchQuotationform.isTerminal.value;
                                        } else {
                                            originPort = document.searchQuotationform.doorOrigin.value;
                                        }
                                        var url = rootPath + '/rateGrid.do?action=Origin&origin=' + document.searchQuotationform.isTerminal.value;
                                        url += "&destination=" + origin + "&doorOrigin=" + doorDestination.value + "&commodity=" + document.searchQuotationform.commcode.value;
                                        url += '&hazardous=' + haz + "&destinationPort=" + destinationPort + "&originPort=" + originPort;
                                        url += "&bulletRates=" + jQuery("#bulletRates").is(":checked") + "&originalCommodity=" + document.searchQuotationform.commcode.value+"&fileType="+fileType;
                                        url += "&copyQuote=" + copyQuote;
                                        var height = jQuery(window).height() - 40;
                                        var width = jQuery(window).width() - 100;
                                        GB_show('FCL Rates Comparison Grid', url, height, width);
                                    }
                                } else {
                                    openGetRates(haz, val, portDestionation, copyQuote);
                                }
                            }
                        });
                    } else {
                        //get list all the country cities
                        if (document.getElementById("bulletRates") && jQuery("#bulletRates").is(":checked")) {
                            openGetRates(haz, val, portDestionation, copyQuote);
                        } else {
                            jQuery.ajaxx({
                                dataType: "json",
                                data: {
                                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                    methodName: "getAllSynonymousCity",
                                    param1: document.searchQuotationform.portofDischarge.value,
                                    param2: document.searchQuotationform.isTerminal.value,
                                    dataType: "json"
                                },
                                preloading: true,
                                success: function(data) {
                                    if (jQuery.trim(data) !== "") {
                                        if (null != data[0] && data[0] != '' && data[0].split(",").length > 1) {
                                            if (null != data[1] && data[1] != '' && data[1].split(",").length > 1) {
                                                var url = rootPath + '/rateGrid.do?action=Origin&origin=' + data[1];
                                                url += "&destination=" + data[0] + "&originName=" + document.searchQuotationform.isTerminal.value;
                                                url += "&doorOrigin=" + document.getElementById('doorOrigin').value;
                                                url += "&commodity=" + document.searchQuotationform.commcode.value + '&hazardous=' + haz;
                                                url += "&destinationPort=" + destinationPort + "&originPort=" + originPort;
                                                url += "&bulletRates=" + jQuery("#bulletRates").is(":checked") + "&originalCommodity=" + document.searchQuotationform.commcode.value;
                                                url += "&copyQuote=" + copyQuote+"&fileType="+fileType;
                                                var height = jQuery(window).height() - 40;
                                                var width = jQuery(window).width() - 100;
                                                GB_show('FCL Rates Comparison Grid', url, height, width);
                                            } else {
                                                var url = rootPath + '/rateGrid.do?action=Origin&origin=' + document.searchQuotationform.isTerminal.value;
                                                url += "&destination=" + data[0] + "&doorOrigin=" + document.getElementById('doorOrigin').value;
                                                url += "&commodity=" + document.searchQuotationform.commcode.value + '&hazardous=' + haz;
                                                url += "&destinationPort=" + destinationPort + "&originPort=" + originPort;
                                                url += "&bulletRates=" + jQuery("#bulletRates").is(":checked") + "&originalCommodity=" + document.searchQuotationform.commcode.value;
                                                url += "&copyQuote=" + copyQuote+"&fileType="+fileType;
                                                var height = jQuery(window).height() - 40;
                                                var width = jQuery(window).width() - 100;
                                                GB_show('FCL Rates Comparison Grid', url, height, width);
                                            }
                                        } else if (null != data[1] && data[0] != '' && data[1].split(",").length > 1) {
                                            var url = rootPath + '/rateGrid.do?action=Origin&origin=' + data[1];
                                            url += "&destination=" + document.searchQuotationform.portofDischarge.value;
                                            url += "&originName=" + document.searchQuotationform.isTerminal.value;
                                            url += "&doorOrigin=" + document.getElementById('doorOrigin').value;
                                            url += "&commodity=" + document.searchQuotationform.commcode.value + '&hazardous=' + haz;
                                            url += "&destinationPort=" + destinationPort + "&originPort=" + originPort;
                                            url += "&bulletRates=" + jQuery("#bulletRates").is(":checked") + "&originalCommodity=" + document.searchQuotationform.commcode.value;
                                            url += "&copyQuote=" + copyQuote+"&fileType="+fileType;
                                            var height = jQuery(window).height() - 40;
                                            var width = jQuery(window).width() - 100;
                                            GB_show('FCL Rates Comparison Grid', url, height, width);
                                        } else {
                                            openGetRates(haz, val, portDestionation, copyQuote);
                                        }
                                    } else {
                                        openGetRates(haz, val, portDestionation, copyQuote);
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
function openGetRates(haz, val, portDestionation, copyQuote, fileType) {
    var destinationPort = document.searchQuotationform.portofDischarge.value;
    var originPort = "";
    if (trim(document.searchQuotationform.isTerminal.value) != "") {
        originPort = document.searchQuotationform.isTerminal.value;
    } else {
        originPort = document.searchQuotationform.doorOrigin.value;
    }
    var url = "/logisoft/rateGrid.do?action=getrates&origin=" + document.searchQuotationform.isTerminal.value;
    url += "&destination=" + portDestionation + "&commodity=" + document.searchQuotationform.commcode.value;
    url += "&noOfDays=" + document.searchQuotationform.noOfDays.value + "&hazardous=" + haz;
    url += "&fileNo=" + val + "&doorOrigin=" + document.getElementById('doorOrigin').value + "&zip=" + document.searchQuotationform.zip.value + "&copyQuote=" + copyQuote;
    url += "&destinationPort=" + destinationPort + "&originPort=" + originPort + "&bulletRates=" + jQuery("#bulletRates").is(":checked") + "&originalCommodity=" + document.searchQuotationform.commcode.value+"&fileType="+fileType;
    GB_show("FCL Rates Comparison Grid", url, document.body.offsetHeight - 50, document.body.offsetWidth - 100);
}
function quotupdation(importFlag, action) {
    var length = "";
    var optionValue = "";
    var i = 0;
    if (collapseid == "") {
        getcollapse();
    }
    if (document.searchQuotationform.defaultAgent[0].checked) {
        document.searchQuotationform.defaultAgent[0].value = 'Y';
    } else {
        document.searchQuotationform.defaultAgent[1].value = 'N';
    }
    document.searchQuotationform.check1.value = collapsePrint;
    document.searchQuotationform.collapseid.value = collapseid;
    checkPrintInclude();
    if (document.searchQuotationform.isTerminal.value == "" || document.searchQuotationform.portofDischarge.value == "" || document.searchQuotationform.commcode.value == "") {
        alertNew("PLEASE SELECT ORIGIN, DESTINATION PORT, COMMODITY CODE AND CLIENT");
        jQuery("#portofDischarge").css("border-color", "red");
        jQuery("#isTerminal").css("border-color", "red");
        jQuery("#commcode").css("border-color", "red");
        jQuery("#customerName1").css("border-color", "red");
        jQuery("#customerName").css("border-color", "red");
        if (undefined != action && null != action && action == 'inland') {
            document.searchQuotationform.inland[1].checked = true;
        }
        return;
    }
    if (document.getElementById("newClient").checked) {
        if (document.searchQuotationform.customerName1.value == "") {
            alertNew("PLEASE SELECT CLIENT");
            jQuery("#customerName1").css("border-color", "red");
            if (undefined != action && null != action && action == 'inland') {
                document.searchQuotationform.inland[1].checked = true;
            }
            return;
        }
    } else {
        if (document.searchQuotationform.customerName.value == "") {
            alertNew("PLEASE SELECT CLIENT");
            jQuery("#customerName").css("border-color", "red");
            if (undefined != action && null != action && action == 'inland') {
                document.searchQuotationform.inland[1].checked = true;
            }
            return;
        }
    }
    if (document.searchQuotationform.issuingTerminal.value == "") {
        alertNew("PLEASE SELECT ISSUING TEMINAL");
        jQuery("#issuingTerminal").css("border-color", "red");
        if (undefined != action && null != action && action == 'inland') {
            document.searchQuotationform.inland[1].checked = true;
        }
        return;
    }
    if (document.searchQuotationform.goodsdesc.value == "" && importFlag != 'true' && action != 'inland') {
        alertNew("PLEASE ENTER GOODS DESCRIPTION");
        jQuery("#goodsDesc").css("border", " 1px solid red");
        if (undefined != action && null != action && action == 'inland') {
            document.searchQuotationform.inland[1].checked = true;
        }
        return;
    }
    if (document.searchQuotationform.goodsdesc.value.length > 500) {
        alertNew("Please donot enter more than 500 characters");
        return;
    }
    if (document.searchQuotationform.comment.value.length > 500) {
        alertNew("Please donot enter more than 500 characters");
        return;
    }
    if (document.searchQuotationform.isTerminal.value == document.searchQuotationform.doorOrigin.value) {
        alertNew("PLEASE CHANGE EITHER ORIGIN OR DOOR ORIGIN, BOTH CANNOT BE SAME");
        if (undefined != action && null != action && action == 'inland') {
            document.searchQuotationform.inland[1].checked = true;
        }
        return;
    }
    if (document.searchQuotationform.zip.value == "" && document.searchQuotationform.doorDestination.value == "" && document.getElementById("rampCheck").checked == false) {
        optionValue = document.getElementById("typeofMoveSelect").options[document.getElementById("typeofMoveSelect").selectedIndex].text;
        if (optionValue != "PORT TO PORT" && optionValue != "RAIL TO PORT" && optionValue != "PORT TO RAIL"
                && optionValue != "RAIL TO RAIL") {
            length = document.getElementById("typeofMoveSelect").length;
            for (var i = 0; i < length; i++) {
                if (document.getElementById("typeofMoveSelect").options[i].text == "PORT TO PORT") {
                    document.getElementById("typeofMoveSelect").selectedIndex = i;
                }
            }
        }
    }
    document.getElementById("save").disabled = true;
    document.getElementById("save1").disabled = true;
    if (undefined != action && null != action && action != '') {
        document.searchQuotationform.action.value = action;
    } else {
        document.searchQuotationform.action.value = "";
    }
    if (importFlag == "true") {
        var selectedUnitValues = [];
        jQuery(".unitSelect").each(function() {
            if (jQuery(this).is(":checked") == true) {
                selectedUnitValues.push(jQuery(this).val());
            }
        });
        document.searchQuotationform.unitSizeSelected.value = selectedUnitValues;
    }
    document.searchQuotationform.buttonValue.value = "quotupdation";
    window.parent.showPreloading();
    document.searchQuotationform.submit();
}
function selectradio() {
    if (special == "Y") {
        document.getElementById("y1").checked = true;
    } else {
        document.getElementById("n1").checked = true;
    }
    if (hazmat == "Y") {
        document.getElementById("y2").checked = true;
    } else {
        document.getElementById("n2").checked = true;
    }
    if (outgage == "Y") {
        document.getElementById("y3").checked = true;
    } else {
        document.getElementById("n3").checked = true;
    }
    if (outgage == "Y") {
        document.getElementById("y3").checked = true;
    } else {
        document.getElementById("n3").checked = true;
    }
    if (sed == "Y") {
        document.getElementById("y4").checked = true;
    } else {
        document.getElementById("n4").checked = true;
    }
    if (deductFfcomm == "Y") {
        document.getElementById("y5").checked = true;
    } else {
        document.getElementById("n5").checked = true;
    }
    if (insu == "Y") {
        document.getElementById("y8").checked = true;
    } else {
        document.getElementById("n8").checked = true;
    }
}
function disabled(val1, val2, val3) {
    load();
    if (val1 == 3 || val1 == 1 || val2 == "on") {
        var imgs = document.getElementsByTagName("img");
        for (var k = 0; k < imgs.length; k++) {
            if (val1 == 3) {
                if (imgs[k].id != "quoteCharges" && imgs[k].id != "previous") {
                    imgs[k].style.visibility = "hidden";
                }
            } else {
                if (imgs[k].id != "quoteCharges" && imgs[k].id != "previous" && imgs[k].id != "converttobook" && imgs[k].id != "copy" && imgs[k].id != "print") {
                    imgs[k].style.visibility = "hidden";
                }
            }
        }
        var input = document.getElementsByTagName("input");
        for (i = 0; i < input.length; i++) {
            if (input[i].id != "buttonValue" && input[i].name != "govSchCode" && input[i].name != "termNo" && input[i].name != "scheduleSuffix" && input[i].name != "state" && input[i].name != "country") {
                input[i].readOnly = true;
                input[i].tabIndex = -1;
                input[i].style.color = "blue";
            }
        }
        var textarea = document.getElementsByTagName("textarea");
        for (i = 0; i < textarea.length; i++) {
            textarea[i].readOnly = true;
            textarea[i].tabIndex = -1;
            textarea[i].style.color = "blue";
        }
        var select = document.getElementsByTagName("select");
        for (i = 0; i < select.length; i++) {
            select[i].disabled = true;
            select[i].style.backgroundColor = "blue";
        }
        document.getElementById("save").style.visibility = "hidden";
        document.getElementById("charge").style.visibility = "hidden";
        document.getElementById("insurance").style.visibility = "hidden";
        document.getElementById("conBL").style.visibility = "hidden";
        document.getElementById("addEquipment").style.visibility = "hidden";
        document.getElementById("updateEquipment").style.visibility = "hidden";
    }
    selectradio();
    if (val1 == 3 && val3 != "") {
        alertNew(val3);
    }
}
function searchReport(val) {
    if (val == "on") {
        document.searchQuotationform.buttonValue.value = "quoteReport";
    } else {
        if (collapseid == "") {
            getcollapse();
        }
        document.searchQuotationform.check1.value = collapsePrint;
        document.searchQuotationform.collapseid.value = collapseid;
        checkPrintInclude();
        if (document.searchQuotationform.quotationNo.value == "") {
            alertNew("Please Enter Quotation Number");
            return false;
        }
        document.searchQuotationform.buttonValue.value = "quoteDetails";
    }
    document.searchQuotationform.submit();
}
function deleteInland() {
    var message = "";
    var importFlag = document.getElementById("importFlag").value;        
    if (document.getElementById("rampCheck").checked) {
        message = "Intermodal Ramp Charges,Door Origin And Zip will be deleted, and all the Quotation changes will be saved<br><br> " +
                "Please ensure that NVO Move is Change from Ramp !   Are you sure?";
    } else if(importFlag === "true") {
        if (importFlag === 'true') {
            message = "Delivery Charges,Door Destination And Zip will be deleted, and all the Quotation changes will be saved<br><br> " +
                    "Please ensure that NVO Move is Change from Door !   Are you sure?";
        } else {
            message = "Inland Charges,Door Destination And Zip will be deleted, and all the Quotation changes will be saved<br><br> " +
                    "Please ensure that NVO Move is Change from Door !   Are you sure?";
        }
    } else {
        if (importFlag === 'true') {
            message = "Delivery Charges,Door Origin And Zip will be deleted, and all the Quotation changes will be saved<br><br> " +
                    "Please ensure that NVO Move is Change from Door !   Are you sure?";
        } else {
            message = "Inland Charges,Door Origin And Zip will be deleted, and all the Quotation changes will be saved<br><br> " +
                    "Please ensure that NVO Move is Change from Door !   Are you sure?";
        }
    }
    confirmNew(message, "inland");
}
function editArCharges(val1, val2, val3) {
    GB_show("get Input Rates", "/logisoft/quoteCharge.do?buttonValue=edit&id=" + val1 + "&fileNo=" + val2 + "&quoteNo=" + val3, 500, 950);
}
function editCollapseCharges(id, fileNumber, quotationNumber, totalValue, markupValue, isCollapsed) {
    GB_show("get Input Rates", "/logisoft/quoteCharge.do?buttonValue=edit&id=" + id + "&fileNo=" + fileNumber + "&quoteNo=" + quotationNumber + "&totalValue=" + totalValue + "&markupValue=" + markupValue + "&isCollapsed=" + isCollapsed, 500, 950);
}
function getinsurance() {
    document.searchQuotationform.costofgoods.readOnly = false;
    document.searchQuotationform.costofgoods.tabIndex = 0;
}
function getinsurance1() {
    confirmNew("Insurance charges will be deleted from the Grid and all the Quotation changes will be saved. Are you sure? ", "insuranceDelete");
}
var flag = "true";
var a;
var b;
function load(val, val1, val2, val3, rateListSize, rateFlag) {
    var stop = false;
    //--to make destination & origin readonly if rates are present---.
    if (rateListSize != "0" || (document.getElementById("bulletRates") && document.getElementById("bulletRates").checked)) {
        document.getElementById("portofDischarge").readOnly = true;
        document.getElementById("portofDischarge").style.backgroundColor = '#CCEBFF';
        document.getElementById("portofDischarge").tabIndex = -1;
        document.getElementById("isTerminal").tabIndex = -1;
        document.getElementById("isTerminal").readOnly = true;
        document.getElementById("isTerminal").style.backgroundColor = '#CCEBFF';
        document.getElementById("astarIcon").style.visibility = "visible";
        if ((document.getElementById("isTerminal").value != document.getElementById("placeofReceipt").value) && rateFlag == "true") {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName: "intRampCheck",
                    param1: val1
                },
                success: function(data) {
                    if (data === 'N') {
                        alertNew("Rate missing Intermodal portion.Please contact Pricing to confirm rate validity");
                    }
                }
            });
        }
    } else {
        document.getElementById("portofDischarge").readOnly = false;
        document.getElementById("isTerminal").readOnly = false;
        document.getElementById("portofDischarge").tabIndex = 0;
        document.getElementById("isTerminal").tabIndex = 0;
        document.getElementById("astarIcon").style.visibility = "hidden";
    }
    getspcleqpmt1();
    if (document.searchQuotationform.ratesNonRates[1].checked) {
        document.getElementById("getRates").style.visibility = "visible";
        if (document.getElementById("bulletRates")) {
            document.getElementById("bulletRates").style.visibility = "visible";
        }
        if (document.searchQuotationform.breakBulk != null) {
            document.getElementById("breakBulk1").disabled = true;
        }
        if (document.searchQuotationform.breakBulk != null) {
            document.getElementById("breakBulk2").disabled = true;
        }
    } else {
        document.getElementById("getRates").style.visibility = "hidden";
        if (document.getElementById("bulletRates")) {
            document.getElementById("bulletRates").style.visibility = "hidden";
        }
        if (document.searchQuotationform.breakBulk != null) {
            document.getElementById("breakBulk1").disabled = false;
        }
        if (document.searchQuotationform.breakBulk != null) {
            document.getElementById("breakBulk2").disabled = false;
        }
    }
    if (document.getElementById("newClient").checked && document.searchQuotationform.customerName.value == "") {
        document.getElementById("customerName").value = "";
        document.getElementById("clientNumber").value = "";
        document.getElementById("clienttype").value = "";
        document.getElementById("contactName").value = "";
        document.getElementById("email").value = "";
        document.getElementById("phone").value = "";
        document.getElementById("fax").value = "";
        document.getElementById("contactNameButton").style.visibility = "hidden";
    }
    if (document.getElementById("newClient").checked) {
        document.getElementById("newerClient").style.display = "block";
        document.getElementById("existingClient").style.display = "none";
    } else {
        document.getElementById("newerClient").style.display = "none";
        document.getElementById("existingClient").style.display = "block";
    }
    if (document.searchQuotationform.newClient.checked) {
        document.getElementById("contactNameButton").style.visibility = "hidden";
    }
    var element1 = document.getElementById("clienttype");
    element1.style.border = 0;
    element1.readOnly = true;
    element1.tabIndex = -1;
    element1.className = "whitebackgrnd";

    if (document.searchQuotationform.customerName.value == "") {
        document.getElementById("contactNameButton").style.visibility = "hidden";
    }
    if (document.searchQuotationform.sslDescription.value == "") {
        document.getElementById("contactNameButtonSecond").style.visibility = "hidden";
    }
    if (document.searchQuotationform.insurance[1].checked) {
        document.searchQuotationform.costofgoods.readOnly = true;
        document.searchQuotationform.costofgoods.tabIndex = -1;
    }
    document.getElementById("collapseRates").style.display = "block";
    document.getElementById("expandRates").style.display = "none";
    if (val == "sendEmail") {
        openMailPopup(val1, val2, val3);
    }
    if (document.searchQuotationform.commcode.value == "006100") {
        document.searchQuotationform.description.style.visibility = "hidden";
        document.getElementById("descriptionLabel").style.visibility = "hidden";
    } else {
        document.searchQuotationform.description.style.visibility = "visible";
        document.getElementById("descriptionLabel").style.visibility = "visible";
    }
    if (document.searchQuotationform.defaultAgent[0].checked) {
        document.getElementById("agent").tabIndex = -1;
        document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
    }
//        /* To make Doc Charge selected if rates from input rated manually*/
//          if(document.getElementById("expandRatesTable")!=null){
//                  var expandTable=document.getElementById("expandRatesTable");
//                  for(var i=0;i<expandTable.rows.length;i++){
//                      var rowObj=expandTable.rows[i];
//                      var chargecode=rowObj.cells[3].innerHTML;
//                      var redStarIndex=chargecode.indexOf("DOCUMENT CHARGE");
//                      if(redStarIndex!=-1){
//                        document.searchQuotationform.docCharge[0].checked=true;
//                        break;
//                      }else{
//                          document.searchQuotationform.docCharge[1].checked=true;
//                      }
//                  }
//          }
}
var booleanVar = true;
function getExpandRates() {
    if (booleanVar == true) {
        document.getElementById("expandRates").style.display = "block";
        document.getElementById("collapseRates").style.display = "none";
        booleanVar = false;
    } else {
        document.getElementById("collapseRates").style.display = "block";
        document.getElementById("expandRates").style.display = "none";
        booleanVar = true;
    }
}
function recalcfunction() {
    if (collapseid == "") {
        getcollapse();
    }
    document.searchQuotationform.check1.value = collapsePrint;
    document.searchQuotationform.collapseid.value = collapseid;
    checkPrintInclude();
    document.searchQuotationform.buttonValue.value = "recalc";
    document.searchQuotationform.submit();
}
function numberChanged(obj, val) {
    if (collapseid == "") {
        getcollapse();
    }
    document.searchQuotationform.check1.value = collapsePrint;
    document.searchQuotationform.collapseid.value = collapseid;
    checkPrintInclude();
    document.searchQuotationform.numbIdx.value = val;
    document.searchQuotationform.numbers1.value = obj.value;
    document.searchQuotationform.buttonValue.value = "numberChanged";
    document.searchQuotationform.submit();
}
function validateNumbers(obj, unitValue, quoteId) {
    var numberValue = obj.value;
    if (numberValue != "") { // not empty
        var trimmedValue = numberValue.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
        var intRegex = /^\d+$/;
        if (!intRegex.test(trimmedValue)) { // not numeric
            alertNew("Field must be numeric.<br/>");
            obj.value = "";
            return;
        }
    } else { // if empty
        alertNew("Field shouldn't blank.</br />");
        return;
    }
    //alert(unitValue+quoteId+numberValue);
    /*--Update number of container--*/
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "updateNoOfContainer",
            param1: unitValue,
            param2: quoteId,
            param3: numberValue
        }
    });
}
function routedAgentName() {
    if (document.searchQuotationform.routedAgentCheck.checked) {
        document.searchQuotationform.routedbymsg.value = document.searchQuotationform.agentNo.value;
        //                document.searchQuotationform.routedbymsg.className="textlabelsBoldForTextBoxDisabledLook";
        //                document.searchQuotationform.routedbymsg.readOnly=true;
        //                document.searchQuotationform.routedbymsg.tabIndex=-1;
    } else {
        document.searchQuotationform.routedbymsg.value = "";
        document.searchQuotationform.routedbymsg.className = "textlabelsBoldForTextBox";
        document.searchQuotationform.routedbymsg.readOnly = false;
        document.searchQuotationform.routedbymsg.tabIndex = -1;
    }
}
function printcheck() {
    if (document.searchQuotationform.include.length == null) {
        if (!document.searchQuotationform.include.checked) {
            document.searchQuotationform.print.checked = false;
        }
    }
    for (var i = 0; i < document.searchQuotationform.include.length; i++) {
        if (!document.searchQuotationform.include[i].checked) {
            document.searchQuotationform.print[i + 1].checked = false;
        }
    }
}
function printcheck1() {
    if (document.searchQuotationform.otherinclude.length == null) {
        if (!document.searchQuotationform.otherinclude.checked) {
            document.searchQuotationform.otherprint.checked = false;
        }
    }
    for (var i = 0; i < document.searchQuotationform.otherinclude.length; i++) {
        if (!document.searchQuotationform.otherinclude[i].checked) {
            document.searchQuotationform.otherprint[i].checked = false;
        }
    }
}
function titleLetter(ev) {
    if (event.keyCode == 9) {
        window.open("/logisoft/jsps/AccountsRecievable/customerSearch.jsp?button=editQuoteCust&customersearch=" + ev, "", "toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
    }
}
function getComcode(ev) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        document.searchQuotationform.description.value = "";
        document.searchQuotationform.buttonValue.value = "popupsearch";
        document.searchQuotationform.submit();
    }
}
function getComCodeName(ev) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        document.searchQuotationform.commcode.value = "";
        document.searchQuotationform.buttonValue.value = "popupsearch";
        document.searchQuotationform.submit();
    }
}
var no1;
var no2;
function cancel1(val, val1) {
    no1 = val;
    no2 = val1;
    //document.getElementById("confirmYes").value = "Yes";
    document.getElementById("confirmNo").style.width = "88" + "px";
    document.getElementById("confirmNo").value = "Exit without Save";
    confirmYesNoCancel("Do You want to save the Quote changes?", "goBack");
}
function yesFunction() {
    if (collapseid == "") {
        getcollapse();
    }
    document.searchQuotationform.check1.value = collapsePrint;
    document.searchQuotationform.collapseid.value = collapseid;
    checkPrintInclude();
    if (document.searchQuotationform.isTerminal.value == "" || document.searchQuotationform.portofDischarge.value == "" || document.searchQuotationform.commcode.value == "") {
        alertNew("PLEASE SELECT ORIGIN, DESTIONATION PORT, COMMODITY CODE AND CLIENT");
        //document.searchQuotationform.portofDischarge.focus();
        return;
    }
    if (document.getElementById("newClient").checked) {
        if (document.searchQuotationform.customerName1.value == "") {
            alertNew("PLEASE SELECT CLIENT");
            jQuery("#customerName1").css("border-color", "red");
            //document.searchQuotationform.portofDischarge.focus();
            return;
        }
    } else {
        if (document.searchQuotationform.customerName.value == "") {
            alertNew("PLEASE SELECT CLIENT");
            jQuery("#customerName").css("border-color", "red");
            //document.searchQuotationform.portofDischarge.focus();
            return;
        }
    }
    if (document.searchQuotationform.issuingTerminal.value == "") {
        alertNew("PLEASE SELECT ISSUING TEMINAL");
        jQuery("#issuingTerminal").css("border", " 1px solid red");
        return;
    }
    if (document.searchQuotationform.goodsdesc.value == "") {
        alertNew("PLEASE ENTER GOODS DESCRIPTION");
        jQuery("#goodsDesc").css("border", " 1px solid red");
        return;
    }
    if (document.searchQuotationform.isTerminal.value == document.searchQuotationform.doorOrigin.value) {
        alertNew("Please change either Origin or Door Origin, both cannot be same");
        return;
    }
    //parent.parent.call("fileSearch");
    document.getElementById("save").disabled = true;
    document.searchQuotationform.buttonValue.value = "confirmSave";
    document.searchQuotationform.submit();
}
function noFunction() {
    //parent.parent.call("fileSearch");
    document.searchQuotationform.buttonValue.value = "viewprevious";
    document.searchQuotationform.submit();
}
function getorigincheck() {
    if (document.searchQuotationform.polCheck.checked) {
        document.searchQuotationform.polCheck.checked = false;
    }
}
function getpolcheck() {
    if (document.searchQuotationform.originCheck.checked) {
        document.searchQuotationform.originCheck.checked = false;
    }
    if (document.searchQuotationform.polCheck.checked) {
        document.searchQuotationform.placeofReceipt.value = "";
        dojo.widget.byId("placeofReceipt").action = "/logisoft/actions/getUnlocationCode.jsp?tabName=QUOTE&from=104";
    } else {
        dojo.widget.byId("placeofReceipt").action = "/logisoft/actions/getUnlocationCode.jsp?tabName=QUOTE&from=3";
    }
}
function getpodcheck() {
    if (document.searchQuotationform.destinationCheck.checked) {
        document.searchQuotationform.destinationCheck.checked = false;
    }
    if (document.searchQuotationform.podCheck.checked) {
        document.searchQuotationform.finalDestination.value = "";
        dojo.widget.byId("finalDestination").action = "/logisoft/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=105";
    } else {
        dojo.widget.byId("finalDestination").action = "/logisoft/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=4";
    }
}
function getdestinationcheck() {
    if (document.searchQuotationform.podCheck.checked) {
        document.searchQuotationform.podCheck.checked = false;
    }
}
function getComCodeDesc(ev) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getCommodityCode",
                param1: document.searchQuotationform.commcode.value,
                dataType: "json"
            },
            sucess: function(data) {
                populateComCodeDesc(data);
            }
        });
    }
    if (document.searchQuotationform.commcode.value == "006100") {
        document.searchQuotationform.description.style.visibility = "hidden";
        document.getElementById("descriptionLabel").style.visibility = "hidden";
    } else {
        document.searchQuotationform.description.style.visibility = "visible";
        document.getElementById("descriptionLabel").style.visibility = "visible";
    }
}
function populateComCodeDesc(data) {
    if (data != "" || data != undefined) {
        document.searchQuotationform.description.value = data.codedesc;
    }
}
function makeFormBorderless(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "button") {
            if (element.value == "Rates" || element.value == "Recalc" || element.value == "Hazmat" || element.value == "Go"
                    || element.value == "ConvertToBooking" || element.value == "Insurance" || element.value == "Look Up" ||
                    element.value == "Input Rates Manually" || element.value == "Add to Quote" || element.value == "Update Existing"
                    || element.value == "IMS") {
                element.style.visibility = "hidden";
            }
        }
        if (element.type == "text") {
            element.style.backgroundColor = "#CCEBFF";
            if (element.id == "customerName" || element.id == "customerName1" || element.id == "isTerminal" || element.id == "portofDischarge" || element.id == "commcode" || element.id == "issuingTerminal") {
                element.style.borderLeft = "red 2px solid";
            }
        }
        if (element.type == "select-one") {
            element.style.border = 0;
            element.disabled = true;
        }
    }
    var imgs = document.getElementsByTagName("img");
    for (var k = 0; k < imgs.length; k++) {
        if (imgs[k].id != "quoteCharges" && imgs[k].id != "quoteCharges1" && imgs[k].id != "quoteCharges2" && imgs[k].id != "quoteCharges3" && imgs[k].id != "quoteCharges4") {
            imgs[k].style.visibility = "hidden";
        }
    }
    return false;
}
function makeFormBorderless1(form) {
    var element;
    var tableid;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "button") {
            if (element.value == "Rates" || element.value == "ConvertToBooking" || element.value == "Charges" ||
                    element.value == "Recalc" || element.value == "Hazmat" || element.value == "Go" || element.value == "Insurance"
                    || element.value == "Look Up" || element.value == "Input Rates Manually" || element.value == "quotesChargeUpdate" ||
                    element.value == "Add to Quote" || element.value == "Update Existing" || element.value == "IMS") {
                element.style.visibility = "hidden";
            }
        }
        if (element.type == "text" || element.type == "textarea") {
            element.style.backgroundColor = "#CCEBFF";
            if (element.id == "customerName" || element.id == "customerName1" || element.id == "isTerminal" || element.id == "portofDischarge" || element.id == "commcode" || element.id == "issuingTerminal") {
                element.style.borderLeft = "red 2px solid";
            }
            element.tabIndex = -1;
        }
        if (element.type == "select-one") {
            element.style.border = 0;
            element.disabled = true;
        }
    }
    var imgs = document.getElementsByTagName("img");
    for (var k = 0; k < imgs.length; k++) {
        /*if (imgs[k].id != "quoteCharges" || imgs[k].id != "quoteCharges1" || imgs[k].id != "quoteCharges2"
         || imgs[k].id != "quoteCharges3" || imgs[k].id != "quoteCharges4") {
         imgs[k].style.visibility = "hidden";
         }*/
        if (imgs[k].id == 'view1' || imgs[k].id == 'view2' || imgs[k].id == 'view3' || imgs[k].id == 'localDrayageCommentImg') {
            imgs[k].style.visibility = "visible";
        } else {
            imgs[k].style.visibility = "hidden";
        }
    }
    // don't hide Adjustment Remarks image on view mode
    jQuery(".adjustmentRemarks").each(function() {
        jQuery(this).css("visibility", "visible");
    });
    return false;
}
function disableImages() {
    var imgs = document.getElementsByTagName("img");
    for (var k = 0; k < imgs.length; k++) {
        imgs[k].style.visibility = "hidden";
    }
    // don't hide Adjustment Remarks image on view mode
    jQuery(".adjustmentRemarks").each(function() {
        jQuery(this).css("visibility", "visible");
    });
}
function refreshPage() {
    if (collapseid == "") {
        getcollapse();
    }
    document.searchQuotationform.check1.value = collapsePrint;
    document.searchQuotationform.collapseid.value = collapseid;
    checkPrintInclude();
    document.searchQuotationform.buttonValue.value = "addCharges";
    document.searchQuotationform.submit();
}
function getcollapse() {
    collapseid = "collapse";
    collapsePrint = "";
    if (document.searchQuotationform.collapseprint != undefined) {
        if (document.searchQuotationform.collapseprint.length != undefined) {
            for (var i = 0; i < document.searchQuotationform.collapseprint.length; i++) {
                if (document.searchQuotationform.collapseprint[i].checked) {
                    collapsePrint = collapsePrint + "1,";
                } else {
                    collapsePrint = collapsePrint + "0,";
                }
            }
        } else {
            if (document.searchQuotationform.collapseprint.checked) {
                collapsePrint = collapsePrint + "1,";
            } else {
                collapsePrint = collapsePrint + "0,";
            }
        }
    }
}
function getExpand() {
    collapseid = "expand";
    collapsePrint = "";
    var printArray = [];
    jQuery('.expandBundleToOfr').each(function() {
        printArray.push(jQuery(this).is(":checked") ? "1" : "0");
    });
    collapsePrint = printArray;
    /*    var check1 = "";
     if (document.searchQuotationform.print != undefined) {
     if (document.searchQuotationform.print.length != undefined) {
     for (var i = 1; i < document.searchQuotationform.print.length; i++) {
     if (document.searchQuotationform.print[i].checked) {
     collapsePrint = collapsePrint + "1,";
     }else {
     collapsePrint = collapsePrint + "0,";
     }
     }
     }else {
     if (document.searchQuotationform.print.checked) {
     collapsePrint = collapsePrint + "1,";
     } else {
     collapsePrint = collapsePrint + "0,";
     }
     }
     } */
}
function checkPrintInclude() {
    var check3 = "";
    if (document.searchQuotationform.otherprint != undefined) {
        if (document.searchQuotationform.otherprint.length != undefined) {
            for (var i = 0; i < document.searchQuotationform.otherprint.length; i++) {
                if (document.searchQuotationform.otherprint[i].checked) {
                    check3 = check3 + "1,";
                } else {
                    check3 = check3 + "0,";
                }
            }
        } else {
            if (document.searchQuotationform.otherprint.checked) {
                check3 = check3 + "1,";
            } else {
                check3 = check3 + "0,";
            }
        }
    }
    document.searchQuotationform.check3.value = check3;
}
var customerTemp = "";
function getCustomer(val1, val2, val3, val4, val5, val6, val7, val8, val9) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function(data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
            } else {
                document.getElementById("contactNameButton").style.visibility = "visible";
                val1 = val1.replace(":", "'");
                customerTemp = val1;
                document.searchQuotationform.customerName.value = val1;
                document.searchQuotationform.customerName.value = val3;
                document.searchQuotationform.clientNumber.value = val2;
                document.searchQuotationform.clienttype.value = val4;
                document.searchQuotationform.contactName.value = "";
                document.searchQuotationform.phone.value = val6;
                document.searchQuotationform.fax.value = val7;
                document.searchQuotationform.email.value = val8;
                setTimeout("setContactNameFocus()", 150);
            }
        }
    });
}
function setContactNameFocus() {
    document.getElementById("portofDischarge").focus();
    document.searchQuotationform.customerName.value = customerTemp;
}
function selectedMenu(val1, val2, val3, val4, val5, val6, val7) {
    document.searchQuotationform.unitSelect.value = val3;
    document.searchQuotationform.unitselected.value = val2;
    document.searchQuotationform.sslDescription.value = val1.replace(/&amp;/g, '&');
    document.searchQuotationform.selectedOrigin.value = val4;
    document.searchQuotationform.selectedDestination.value = val5;
    document.searchQuotationform.selectedComCode.value = val6;
    if (val7 == "newgetRatesBKG" || val7 == "oldgetRatesBKG" || val7 == "copyQuote") {
        document.getElementById("bkgAlert").value = val7;
        getConverttoBook(val7, val3);
    } else {
        document.searchQuotationform.inland[1].checked = true;
        document.searchQuotationform.buttonValue.value = "newgetRates";
        document.searchQuotationform.submit();
    }
}
function getSslCode() {
    var ssline = document.searchQuotationform.sslDescription.value;
    var customer = ssline.replace("&", "amp;");
    if (customer == "%") {
        customer = "percent";
    }
    GB_show("Carrier", "/logisoft/quoteCustomer.do?buttonValue=CarrierQuotation&clientName=" + customer, width = "400", height = "700");
}
function getCarrier(val1, val2, val3, val4, val5, val6, val7, val8) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function(data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
            } else {
                val1 = val1.replace(":", "'");
                document.getElementById("contactNameButtonSecond").style.visibility = "visible";
                document.searchQuotationform.sslDescription.value = val1;
                document.searchQuotationform.sslcode.value = val2;
                document.searchQuotationform.carrierContact.value = "";
                document.searchQuotationform.carrierPhone.value = val6;
                document.searchQuotationform.carrierFax.value = val7;
                if (val8 !== undefined) {
                    document.searchQuotationform.carrierEmail.value = val8;
                }
                setTimeout("setFocusToCarrier()", 150);
            }
        }
    });
}
function setFocusToCarrier() {
    document.getElementById("carrierContact").focus();
}
function getClientInfo() {
    var cust = document.getElementById("customerName").value;
    var customer = cust.replace("&", "amp;");
    if (customer == "%") {
        customer = "percent";
    }
    GB_show("Client Info", "/logisoft/quoteCustomer.do?buttonValue=Quotation&clientName=" + customer, width = "400", height = "870");
}
function getContactInfo() {
    var customerName = "";
    if (document.getElementById("customerName").value != undefined) {
        customerName = document.getElementById("customerName").value;
    } else {
        customerName = document.getElementById("customerName1").value;
    }
    customerName = customerName.replace("&", "amp;");
    var customer = document.getElementById("clientNumber").value;
    var contactName = document.searchQuotationform.contactName.value;
    if (contactName == "%") {
        contactName = "percent";
    }
    GB_show("Contact Info", "/logisoft/customerAddress.do?buttonValue=Quotation&custNo=" + customer + "&custName=" + customerName + "&contactName=" + contactName, width = "400", height = "1100");
}
function getContactNamefromPopup(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11) {
    val1 = val1.replace(":", "'");
    document.searchQuotationform.customerName.value = val1;
    document.searchQuotationform.clientNumber.value = val2;
    document.searchQuotationform.contactName.value = val4;
    if (val5 != "") {
        document.searchQuotationform.phone.value = val5;
    }
    if (val6 != "") {
        document.searchQuotationform.fax.value = val6;
    }
    if (val7 != "") {
        document.searchQuotationform.email.value = val7;
    }
    setTimeout("setFocusTypeofMove()", 150);
}
function setFocusTypeofMove() {
    document.getElementById("typeofMove").focus();
}
function getCarrierContactInfo() {
    var cust = document.searchQuotationform.sslDescription.value;
    var customerName = cust.replace("&", "amp;");
    var customer = document.searchQuotationform.sslcode.value;
    var carrierContact = document.searchQuotationform.carrierContact.value;
    if (carrierContact == "%") {
        carrierContact = "percent";
    }
    GB_show("Carrier Contact", "/logisoft/customerAddress.do?buttonValue=CarrierQuotation&custNo=" + customer + "&custName=" + customerName + "&contactName=" + carrierContact, width = "400", height = "700");
}
function getCarrierContactNamefromPopup(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11) {
    val1 = val1.replace(":", "'");
    document.searchQuotationform.sslDescription.value = val1;
    document.searchQuotationform.sslcode.value = val2;
    document.searchQuotationform.carrierContact.value = val4;
    if (val5 != "") {
        document.searchQuotationform.carrierPhone.value = val5;
    }
    if (val6 != "") {
        document.searchQuotationform.carrierFax.value = val6;
    }
    if (val7 != "") {
        document.searchQuotationform.carrierEmail.value = val7;
    }
    setTimeout("setFocusToComcode()", 150);
}
function setFocusToComcode() {
    document.getElementById("commcode").focus();
}
function getRoutedByAgent() {
    var customerName = document.searchQuotationform.routedbymsg.value;
    if (customerName == "%") {
        customerName = "percent";
    }
    GB_show("Routed By Agent", "/logisoft/quoteCustomer.do?buttonValue=RoutedQuotation&clientName=" + customerName, width = "400", height = "700");
}
function getRoutedByAgentFromPopup(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function(data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
            } else {
                val1 = val1.replace(":", "'");
                document.searchQuotationform.routedbymsg.value = val1;
            }
        }
    });
}
function focusTOSsl() {
    document.getElementById("sslDescription").focus();
}
function setDojoAction() {
    var path = "";
    if (!document.getElementById("nonRated").checked) {
        if (document.getElementById("destinationCity").checked) {
            path = "&origin=" + document.searchQuotationform.isTerminal.value + "&radio=city";
        } else {
            path = "&origin=" + document.searchQuotationform.isTerminal.value + "&radio=country";
        }
    } else {
        if (document.getElementById("destinationCity").checked) {
            path = "&nonRated=" + "yes" + "&radio=city";
        } else {
            path = "&nonRated=" + "yes" + "&radio=country";
        }
    }
    appendEncodeUrl(path);
}

function getAgentforDestinationonBlur() {
    var pod = document.searchQuotationform.portofDischarge.value;
    if (pod.lastIndexOf("(") > -1 && pod.lastIndexOf(")") > -1) {
        var podNew = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                methodName: "getDefaultAgent",
                param1: podNew,
                param2: "true" === document.getElementById("hiddenImportFlag").value ? "I" : "F"
            },
            success: function(data) {
                populateAgentDojo1(data);
            }
        });
    }
}
var cityNameForRemarks1;
var unlocationCode;
function typeOfMove(data) {
    document.searchQuotationform.typeofMove.value = data;
}
var cityNameForRemarks;
function getAgentforPod(ev) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        var pod = document.searchQuotationform.finalDestination.value;
        if (pod.lastIndexOf("(") > -1 && pod.lastIndexOf(")") > -1) {
            var podNew = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                    methodName: "getDefaultAgent",
                    param1: podNew,
                    param2: "true" === document.getElementById("hiddenImportFlag").value ? "I" : "F"
                },
                success: function(data) {
                    populateAgentDojo2(data);
                }
            });
        }
    }
    if (event.keyCode == 9 || event.keyCode == 13) {
        var city = document.searchQuotationform.finalDestination.value;
        var index = city.indexOf("/");
        var cityName = city.substring(0, index);
        cityNameForRemarks = cityName;
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                methodName: "getSpecialRemarks",
                param1: cityName
            },
            success: function(data) {
                showSpecialRemarks1(data);
            }
        });
    }
}
function populateAgentDojo2(data) {
    if (null != data && data.accountName != undefined && data.accountName != "" && data.accountName != null) {
        document.getElementById("agent").value = data.accountName;
    }
    if (null != data && data.accountno != undefined && data.accountno != "" && data.accountno != null) {
        document.getElementById("agentNo").value = data.accountno;
    }
}
var data1 = "";
function showSpecialRemarks(data) {
    data1 = jQuery.trim(data);
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
            methodName: "getCountryName",
            param1: unlocationCode
        },
        success: function(data) {
            getCountry(data);
        }
    });
}
function getCountry(data) {
    cityNameForRemarks1 = cityNameForRemarks1 + "/" + data;

    if (data1 != "") {
        GB_show("", "/logisoft/jsps/fclQuotes/remarksFromDojo.jsp?remarks=" + cityNameForRemarks1, width = "200", height = "600");
    }
}
function trim(stringToTrim) {
    return stringToTrim.replace(/^\s+|\s+$/g, "");
}
function showSpecialRemarks1(data) {
    if (data != "") {
        var data1 = trim(data);
        if (data1 != "") {
            GB_show("Remarks", "/logisoft/jsps/fclQuotes/remarksFromDojo.jsp?remarks=" + cityNameForRemarks, width = "200", height = "600");
        }
    }
}
function getAgent() {
    var portOfDischarge = document.searchQuotationform.portofDischarge.value;
    var destination = document.searchQuotationform.finalDestination.value;
    GB_show("Agent", "/logisoft/quoteAgent.do?buttonValue=Agent&portOfDischarge=" + portOfDischarge + "&destination=" + destination, width = "400", height = "700");
}
function setDojoPathForAgent() {
    var path = "";
    var portOfDischarge = document.searchQuotationform.finalDestination.value;
    var destination = document.searchQuotationform.portofDischarge.value;
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
        success: function(data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
            } else {
                document.searchQuotationform.agent.value = val1;
                document.searchQuotationform.agentNo.value = val2;
                setTimeout("setFocusToRoutedBy()", 150);
            }
        }
    });
}
function setFocusToRoutedBy() {
    document.getElementById("routedbymsg").focus();
}
function getInsurance(val) {
    //--CHECKING WHETHER RATES ARE COMING FROM RATES TABLE I.E. GETRATES POP UP---
    //    var val=checkIfRatesAreFromGetRates();
    //    if(val=='true'){
    //     	alertNew("Insurance Charges can be added only if rates are from getRates");
    //     	document.searchQuotationform.insurance[1].checked=true;
    //     	return;
    //  	}//--ends--
    if (jQuery("#spotRateY").is(":checked") && document.searchQuotationform.ratesNonRates[1].checked && isSpotRate === "No Spot Rate"){
            alertNew("On Spot Rate Files Spot Costs MUST be entered Manually For All Costs");
            return;
    }
    if (collapseid == "") {
        getcollapse();
    }
    document.searchQuotationform.check1.value = collapsePrint;
    document.searchQuotationform.collapseid.value = collapseid;
    checkPrintInclude();
    GB_show("Insurance Rate", "/logisoft/jsps/fclQuotes/calculateInsurance.jsp?costOfGoods=" + document.searchQuotationform.costofgoods.value + "&insuranceAmount=" + document.searchQuotationform.insurancamt.value, width = "200", height = "300");
}
function getDocCharge() {
    if (collapseid == "") {
        getcollapse();
    }
    document.searchQuotationform.check1.value = collapsePrint;
    document.searchQuotationform.collapseid.value = collapseid;
    checkPrintInclude();
    GB_show("Document Charge Rate", "/logisoft/jsps/fclQuotes/calculateDocCharge.jsp", width = "200", height = "300");
}
function calculateInsurance(val1, val2) {
    document.searchQuotationform.costofgoods.value = val1;
    document.searchQuotationform.insurancamt.value = val2;
    document.searchQuotationform.buttonValue.value = "insurance";
    document.searchQuotationform.focusValue.value = "soc";
    document.searchQuotationform.submit();
}
function calculateDocumentCharge(amount) {
    document.searchQuotationform.docChargeAmount.value = amount;
    document.searchQuotationform.buttonValue.value = "addDocumentCharge";
    document.searchQuotationform.submit();
}
function calculatePierPassCharge() { 
    document.searchQuotationform.buttonValue.value = "addPierPassCharge";
    document.searchQuotationform.submit();
}
function deleteDocCharge() {
    confirmNew("Document charges will be deleted from the Grid and all the Quotation changes will be saved. Are you sure? ", "deleteDocCharge");
}
function deletePierPassCharge() {
    confirmNew("Pier Pass charges will be deleted from the Grid and all the Quotation changes will be saved. Are you sure? ", "deletePierPassCharge");
}
function getDestination() {
    if (document.searchQuotationform.portofDischarge.value == "" && document.searchQuotationform.finalDestination.value != "") {
        var pod = document.searchQuotationform.finalDestination.value;
        var index = pod.indexOf("/");
        var podNew = pod.substring(0, index);
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getPortsForAgentInfo",
                param1: podNew
            },
            success: function(data) {
                populateAgentDojo(data);
            }
        });
    } else {
        if (document.searchQuotationform.finalDestination.value == "") {
            document.getElementById("agent").value = "";
            document.getElementById("agentNo").value = "";
        }
    }
}
function populateAgentDojo(data) {
    if (data == "empty" || document.searchQuotationform.routedAgentCheck.value == "yes") {
        document.getElementById("agent").value = "";
        document.getElementById("agentNo").value = "";
        document.getElementById("routedbymsg").value = "";
    }
}
function getPod() {
    if (document.searchQuotationform.finalDestination.value == "") {
        var pod = document.searchQuotationform.portofDischarge.value;
        var index = pod.indexOf("/");
        var podNew = pod.substring(0, index);
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getPortsForAgentInfo",
                param1: podNew
            },
            success: function(data) {
                populateAgent1Dojo(data);
            }
        });
    }
}
function populateAgent1Dojo(type, data, evt) {
    if (data == "empty") {
        document.getElementById("agent").value = "";
        document.getElementById("agentNo").value = "";
        document.getElementById("routedbymsg").value = "";
    }
}
function getEmptyClient() {
    if (document.getElementById("customerName1").value == "") {
        document.searchQuotationform.clientNumber.value = "";
        document.searchQuotationform.clienttype.value = "";
        document.searchQuotationform.contactName.value = "";
        document.searchQuotationform.phone.value = "";
        document.searchQuotationform.fax.value = "";
        document.searchQuotationform.email.value = "";
        document.getElementById("contactButton").style.visibility = "hidden";
    }
}
function getEmptySsline() {
    if (document.searchQuotationform.sslDescription.value == "") {
        document.searchQuotationform.sslcode.value = "";
        document.searchQuotationform.carrierContact.value = "";
        document.searchQuotationform.carrierPhone.value = "";
        document.searchQuotationform.carrierFax.value = "";
        document.searchQuotationform.carrierEmail.value = "";
        document.getElementById("contactNameButtonSecond").style.visibility = "hidden";
    }
}
function deleteArCharges(val1) {
    if (collapseid == "") {
        getcollapse();
    }
    document.searchQuotationform.check1.value = collapsePrint;
    document.searchQuotationform.collapseid.value = collapseid;
    checkPrintInclude();
    document.searchQuotationform.buttonValue.value = "deleteCharge";
    document.searchQuotationform.numbIdx.value = val1;
    confirmNew("Are you sure you want to delete this Charge", "deleteArCharges");
}
function deleteSpecialEquipmentUnit(val, index) {
    if (collapseid == "") {
        getcollapse();
    }
    document.searchQuotationform.check1.value = collapsePrint;
    document.searchQuotationform.collapseid.value = collapseid;
    checkPrintInclude();
    document.searchQuotationform.buttonValue.value = "deleteSpecialEquipmentUnit";
    document.searchQuotationform.numbIdx.value = val;
    document.searchQuotationform.standardChargeIndex.value = index;
    confirmNew("Are you sure you want to delete this Charge", "deleteSpecialEquipmentUnit");
}
function getAccountName(adjustmentAmt, chargeId) {
    if (collapseid == "") {
        getcollapse();
    }
    if (adjustmentAmt.match(/^-?[0-9]+(.[0-9]{1,2})?$/) == null) {
        adjustmentAmt = '';
        alertNew('The format is wrong');
        return false;
    } else {
        document.searchQuotationform.check1.value = collapsePrint;
        document.searchQuotationform.collapseid.value = collapseid;
        checkPrintInclude();
        showPopUp();
        jQuery("#commentsAboutAdjustment").val("");
        jQuery("#commentsPopupDiv").css({
            display: "block"
        });
        jQuery("#commentsTitleMessage").text("Reason for making adjustment to rate");
        jQuery("#hiddenChargeIdValue").val(chargeId);
        jQuery("#hiddenAdjustmentAmtValue").val(adjustmentAmt);
        var IpopTop = (screen.height - jQuery("#commentsPopupDiv").attr('offsetHeight')) / 2;
        var IpopLeft = (screen.width - jQuery("#commentsPopupDiv").attr('offsetWidth')) / 2;
        jQuery("#commentsPopupDiv").css({
            'left': IpopLeft + document.body.scrollLeft - 70
        });
        jQuery("#commentsPopupDiv").css({
            'top': IpopTop + document.body.scrollTop - 150
        });
    }
}
function getIssTerm() {
    var issuingTerm = document.searchQuotationform.issuingTerminal.value;
    var index = issuingTerm.indexOf("-");
    var newIssuingTerm = issuingTerm.substring(0, index);
    var issuTerm = newIssuingTerm.replace("&", "amp;");
    if (issuTerm == "%") {
        issuTerm = "percent";
    }
    GB_show("Issuing Termial Info", "/logisoft/issuingTerminal.do?buttonValue=Quotation&issuingTerminal=" + issuTerm, width = "400", height = "700");
}
function getIssuingTerminal(val1) {
    document.searchQuotationform.issuingTerminal.value = val1;
    setTimeout("setFocusTodefaultAgent()", 150);
}
function setFocusTodefaultAgent() {
    document.getElementById("defaultAgent").focus();
}
function addToBl(ev1, ev2) {
    document.searchQuotationform.amount.value = ev1;
    document.searchQuotationform.drayageMarkUp.value = ev2;
    document.searchQuotationform.focusValue.value = "n7";
    document.searchQuotationform.buttonValue.value = "addLocalDrayageToBl";
    document.searchQuotationform.submit();
}
function addIntermodelToBl() {
    if (event.keyCode == 9 || event.keyCode == 13) {
        document.searchQuotationform.buttonValue.value = "addIntermodelToBl";
        document.searchQuotationform.focusValue.value = "n8";
        document.searchQuotationform.submit();
    }
}
function addInsuranceToBl() {
    document.searchQuotationform.buttonValue.value = "addInsuranceToBl";
    document.searchQuotationform.submit();
}
function onCustomerBlur(clientType) {
    if (clientType == 'client') {
        document.getElementById('customerName1').value = document.getElementById('customerName').value;
    }
    document.getElementById('customerName').value = "";
    document.getElementById('customerName1').value = "";
    document.getElementById('clientNumber').value = "";
    document.getElementById('clienttype').value = "";
    document.getElementById('contactName').value = "";
    document.getElementById('email').value = "";
    document.getElementById('phone').value = "";
    document.getElementById('fax').value = "";
    jQuery('#clientIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
    document.getElementById("contactNameButton").style.visibility = 'hidden';
    getCreditStatus("alert");
}
function newClientEQ() {
    if (document.getElementById("newClient").checked) {
        if (document.getElementById("clientConsigneeCheck")) {
            document.getElementById("clientConsigneeCheck").checked = false;
        }
        document.getElementById("newerClient").style.display = "block";
        document.getElementById("existingClient").style.display = "none";
        onCustomerBlur('newClient');
    } else {
        document.getElementById("newerClient").style.display = "none";
        document.getElementById("existingClient").style.display = "block";
        document.getElementById("customerName").value = "";
    }
}
function newRampEQ() {
    if (document.getElementById("newRampCity").checked) {
        document.getElementById("rampCity").value = "";
        document.getElementById("rampLookUp").style.visibility = "hidden";
        dojo.widget.byId("rampAutoTextbox").action = "/logisoft/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=99";
    } else {
        document.getElementById("rampCity").value = "";
        document.getElementById("rampLookUp").style.visibility = "visible";
        dojo.widget.byId("rampAutoTextbox").action = "/logisoft/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=8";
    }
}

function popupQuoteCharge(windowname, val, val1, importFlag) {
    if (undefined == val1 || null == val1 || val1 == '') {
        quotupdation(importFlag, 'manualRates');
    } else {
        openInputRatesManually(val, val1, importFlag);
    }
}
function openInputRatesManually(val, val1, importFlag) {
    if (!window.focus) {
        return true;
    }
    var hazmat = "";
    if (document.searchQuotationform.hazmat[0].checked) {
        hazmat = "Y";
    } else {
        hazmat = "N";
    }
    var spcleqpt = "";
    if (document.searchQuotationform.specialequipment[0].checked) {
        spcleqpt = "Y";
    } else {
        spcleqpt = "N";
    }
    var breakBulk = "N";
    if (document.searchQuotationform.breakBulk != null) {
        if (document.searchQuotationform.breakBulk[0].checked) {
            breakBulk = "Y";
        } else {
            breakBulk = "N";
        }
    }
    var ratedOption = '';
    if (undefined != document.getElementById('nonRated') && true == document.getElementById('nonRated').checked) {
        ratedOption = 'NonRated';
    } else {
        ratedOption = 'rated';
    }

    document.getElementById("scrollUp").scrollIntoView(true);
    GB_show("get Input Rates", rootPath+"/quoteCharge.do?hazmat=" + hazmat + "&quoteNo=" + val + "&breakBulk=" + breakBulk +
            "&spcleqpmt=" + spcleqpt + "&button=" + "editquote" + "&fileNo=" + val1 + '&ratedOption=' + ratedOption + '&importFlag=' + importFlag, 500, 950);
}
function getspcleqpmt(obj) {
    if (obj.value == "Y") {
        document.searchQuotationform.specialEqpmt.disabled = false;
        document.searchQuotationform.specialEqpmtUnit.disabled = false;
    }
}

function getspcleqpmt1() {
    if (document.searchQuotationform.specialequipment[0].checked) {
        document.searchQuotationform.specialEqpmt.disabled = false;
        document.searchQuotationform.specialEqpmtUnit.disabled = false;
    } else {
        document.searchQuotationform.specialEqpmt.disabled = true;
        document.searchQuotationform.specialEqpmtUnit.disabled = true;
    }
}
function addSpecialEquipment(id) {
    if (document.searchQuotationform.ratesNonRates[1].checked) {
        document.searchQuotationform.specialEqpmt.disabled = false;
        document.searchQuotationform.specialEqpmtUnit.disabled = false;
    } else {
        if (document.searchQuotationform.breakBulk[1].checked) {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName: "validateSpecialEquipment",
                    param1: id,
                    dataType: "json"
                },
                success: function(data) {
                    if (data) {
                        document.searchQuotationform.specialEqpmt.disabled = false;
                        document.searchQuotationform.specialEqpmtUnit.disabled = false;
                    } else {
                        document.searchQuotationform.specialequipment[1].checked = true;
                        alertNew("Please Select atleast one charges from Input Rates Manually");
                    }
                }
            });
        } else {
            document.searchQuotationform.specialequipment[1].checked = true;
            alertNew("Special Equipment cannot be added if Break bulk Yes");
        }
    }
}

function deleteSpecialEquipment() {
    confirmNew("Special Equipment Charges will be deleted and all the Quotation changes will be saved. Are you sure?", "deleteSpecialEquipment");
}
function scan(fileNo, importFlag) {
    if (null != fileNo && fileNo != '') {
        var screenName = "FCLFILE";
        if (null != importFlag && importFlag == 'true') {
            screenName = "IMPORT FILE";
        }
        document.getElementById("scrollUp").scrollIntoView(true);
        GB_show("Scan", "/logisoft/scan.do?screenName=" + screenName + "&documentId=" + fileNo + "&ignoreDocumentName=SS LINE MASTER BL" + "&", 420, 1150);
    } else {
        alert("Please save the file before Scan/Attach");
    }
}
function getFFCommission() {
     if (jQuery("#spotRateY").is(":checked") && document.searchQuotationform.ratesNonRates[1].checked && isSpotRate === "No Spot Rate"){
            alertNew("On Spot Rate Files Spot Costs MUST be entered Manually For All Costs");
            return;
    }
    confirmNew("Commission Amount will be subtracted and all the Quotation changes will be saved. Are you sure?", "getFFCommission");
}
function deleteFFCommission() {
    confirmNew("Commission Amount will be deleted and all the Quotation changes will be saved. Are you sure?", "deleteFFCommission");
}
function sendEmail(val, val1, val2, val3) {
    if (val == "on") {
        openMailPopup(val1, val2, val3);
    } else {
        if (collapseid == "") {
            getcollapse();
        }
        document.searchQuotationform.check1.value = collapsePrint;
        document.searchQuotationform.collapseid.value = collapseid;
        checkPrintInclude();
        document.searchQuotationform.buttonValue.value = "sendEmail";
        document.searchQuotationform.submit();
    }
}
function openMailPopup(val, val1, val3) {
    if (document.getElementById("cc").checked) {
        GB_show("Email", "/logisoft/sendEmail.do?id=" + val + "&moduleName=Quotes&ccAddress=" + val3 + "&toAddress=" + document.getElementById("email").value + "&subject=FCL Quote " + val1 + "&fileNo=" + val1, 455, 650);
    } else {
        GB_show("Email", "/logisoft/sendEmail.do?id=" + val + "&moduleName=Quotes&toAddress=" + document.getElementById("email").value + "&subject=FCL Quote " + val1 + "&fileNo=" + val1, 455, 650);
    }
}
function sendToDojoJsp() {
    var selectBoxValue = document.searchQuotationform.typeofMove.selectedIndex;
    if (selectBoxValue == "0" || selectBoxValue == "1" || selectBoxValue == "2") {
        //		document.getElementById("originZip").style.visibility = "visible";
        //		document.getElementById("zipLookUp").style.visibility = "visible";
    } else {
//		if (selectBoxValue == "3" || selectBoxValue == "4" || selectBoxValue == "5") {
//			document.getElementById("originZip").style.visibility = "hidden";
//			document.getElementById("zipLookUp").style.visibility = "hidden";
//		} else {
//			document.getElementById("originZip").style.visibility = "hidden";
//			document.getElementById("zipLookUp").style.visibility = "hidden";
//		}
    }
}
function displayOrigins() {
    var selectBoxValue = document.searchQuotationform.typeofMove.selectedIndex;
    GB_show("", "/logisoft/searchquotation.do?buttonValue=searchPort&setFocus=commcode&textName=isTerminal&from=terminal&typeOfmove=" + selectBoxValue + "&fclDestination=" + document.searchQuotationform.portofDischarge.value + "&NonRated=" + document.getElementById("nonRated").checked, width = "250", height = "670");
}
function displayRampCity() {
    var selectBoxValue = document.searchQuotationform.typeofMove.selectedIndex;
    GB_show("RampCity Search", "/logisoft/searchquotation.do?buttonValue=searchPort&setFocus=placeofReceipt&textName=rampCity&from=pol&typeOfmove=" + selectBoxValue, width = "250", height = "600");
}
function setZipCode(val) {
    document.searchQuotationform.zip.value = val;
    setTimeout("setFocusToTRansit()", 150);
}
function setFocusToTRansit() {
    document.getElementById("noOfDays").focus();
}
var fileNumberDup = '';
var checkVal = '';
function quotationPrint(ev, val) {
    fileNumberDup = ev;
    checkVal = val;
    if (jQuery("#spotRateY").is(":checked") && document.searchQuotationform.ratesNonRates[1].checked && isSpotRate === "No Spot Rate"){
            alertNew("On Spot Rate Files Spot Costs MUST be entered Manually For All Costs");
            return;
    }
    if (document.searchQuotationform.typeofMove) {
        var nvoMove = document.searchQuotationform.typeofMove.value;
        if (nvoMove == "DOOR TO DOOR" || nvoMove == "DOOR TO PORT" || nvoMove == "DOOR TO RAIL") {
            var originZip = document.searchQuotationform.zip.value;
            if (undefined == originZip || originZip == '') {
                alertNew("Please Enter Origin Zip");
                return;
            }
        }
    }
    // Delivery remarks OR On-Carriage remarks
    var navigationType = jQuery("#hiddenImportFlag").val();
    var doorDest = (navigationType === "true") ? jQuery('#doorOrigin').val() : jQuery('#doorDestination').val();
    if (doorDest !== "") {
        if (jQuery('#onCarriage').is(':checked')) {
            if (jQuery("#hiddenRemarks").val() === "" && checkOnCarriage() === false) {
                confirmYesOrNo("Oncarriage charge is required. Click Yes to add Oncarriage charge or No to skip this requirement and provide Oncarriage remarks", "onCarriage");
                return;
            }
        } else if (navigationType === "false" && jQuery('#deliveryChargeComments').val() === "" && checkDeliveryRates() === 'false') {
            confirmYesOrNo("Delivery Charge Cost is required when doing door to door move. Choose Yes to continue or choose No to acknowledge and skip past this requirement", "deliveryCharge");
            return;
        }
    }
    if (trim(document.searchQuotationform.intermodelComments.value) == "") {
        if ((document.searchQuotationform.inland[1].checked &&
                (document.getElementById("typeofMoveSelect").value == "DOOR TO DOOR" || document.getElementById("typeofMoveSelect").value == "DOOR TO PORT" || document.getElementById("typeofMoveSelect").value == "DOOR TO RAIL"
                        || document.getElementById("typeofMoveSelect").value == "RAMP TO DOOR" || document.getElementById("typeofMoveSelect").value == "RAMP TO PORT" || document.getElementById("typeofMoveSelect").value == "RAMP TO RAIL"))
                || (document.getElementById("zip") != null && !document.getElementById("zip").readOnly && document.searchQuotationform.zip.value != '')) {
            if (document.searchQuotationform.inland[1].checked) {
                if (document.getElementById("rampCheck").checked) {
                    confirmYesOrNo("Intermodal Ramp Cost is required when doing ramp move. Choose Yes to continue or choose No to acknowledge and skip past this requirement", "printFaxEmail");
                } else {
                    confirmYesOrNo("Inland Cost is required when doing door move. Choose Yes to continue or choose No to acknowledge and skip past this requirement", "printFaxEmail");
                }
                return;
            } else {
                quotationPrintSubmit();
            }
        } else {
            quotationPrintSubmit();
        }
    } else {
        quotationPrintSubmit();
    }
}
function quotationPrintSubmit() {
    if (collapseid == "") {
        getcollapse();
    }
    document.searchQuotationform.check1.value = collapsePrint;
    document.searchQuotationform.collapseid.value = collapseid;
    checkPrintInclude();
    if (document.searchQuotationform.zip.value == "" && document.searchQuotationform.doorDestination.value == "" && document.getElementById("rampCheck").checked == false) {
        var length = document.getElementById("typeofMoveSelect").length;
        for (var i = 0; i < length; i++) {
            if (document.getElementById("typeofMoveSelect").options[i].text == "PORT TO PORT") {
                document.getElementById("typeofMoveSelect").selectedIndex = i;
            }
        }
    }
    if (document.searchQuotationform.zip.value != "" && document.searchQuotationform.doorDestination.value == "" && document.getElementById("rampCheck").checked == false) {
        length = document.getElementById("typeofMoveSelect").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("typeofMoveSelect").options[i].text == "DOOR TO PORT") {
                document.getElementById("typeofMoveSelect").selectedIndex = i;
            }
        }
    }
    revertPageToEditMode(document.getElementById("editQuote"));
    if (checkVal == "on") {
        document.getElementById("print").disabled = true;
        document.searchQuotationform.buttonValue.value = "quotePrintwithoutsave";
    } else {
        document.getElementById("print").disabled = true;
        document.searchQuotationform.buttonValue.value = "quotePrint";
    }
    document.searchQuotationform.fileNumber.value = fileNumberDup;
    document.searchQuotationform.submit();
}
function tabMoveAfterDeleteRates(importflag) {
    if (document.getElementById("customerName").value != '' && document.getElementById("customerName1").value != '') {
        if (importflag == 'true') {
            document.getElementById("isTerminal").focus();
        } else {
            document.getElementById('portofDischarge').focus();
        }
    } else {
        if (document.getElementById("newClient").checked && document.getElementById("customerName1").value == '') {
            document.getElementById("customerName1").focus();
        } else {
            document.getElementById("customerName").focus();
        }
    }
}
function setFocuss() {
    if (stat == "") {
        if (button == "assignRemarks") {
            if (undefined != document.getElementById("isTerminal")
                    && document.getElementById("isTerminal").style.visibility != 'hidden') {
                document.getElementById("isTerminal").focus();
                document.getElementById("isTerminal").select();
            }
        }
    } else {
        if (document.getElementById(stat) != null) {
            if (undefined != document.getElementById(stat)
                    && document.getElementById(stat).style.visibility != 'hidden') {
                document.getElementById(stat).focus();
                document.getElementById(stat).select();
            }
        }
    }
}
function test() {
    if (event.keyCode == 9 || event.keyCode == 13) {
        setTimeout("setF()", 150);
    }
}
function test1() {
    if (event.keyCode == 9 || event.keyCode == 13) {
        setTimeout("setFocusTOAmt1()", 50);
    }
}
function setFocusTOAmt1() {
    document.getElementById("amt1").focus();
}
function setF() {
//document.getElementById('HazmatButton').select();
}
function setFoc() {
    if (event.keyCode == 9 || event.keyCode == 13) {
        setTimeout("setFocusTOAmt2()", 50);
    }
}
function setFocusTOAmt2() {
    document.getElementById("amt2").focus();
}
function setFR() {
    if (event.keyCode == 9 || event.keyCode == 13) {
        setTimeout("setFocusTOcosty()", 50);
    }
}
function setFocusTOcosty() {
    document.getElementById("costofgoods").focus();
}
function setFocY() {
    if (event.keyCode == 8) {
        return false;
    }
}
function getUppercase(val) {
    var text;
    text = val;
    if (document.getElementById("newClient").checked) {
        document.getElementById("customerName1").value = text.toUpperCase();
    } else {
        document.getElementById("customerName").value = text.toUpperCase();
    }
}
function focusSetting(isContact) {
        if (document.getElementById("newClient").checked) {
           setFocusByElementId('customerName1');
        } else {
            testing(isContact);
        }
}
function focusSettingForSSl() {
    if (event.keyCode == 9 || event.keyCode == 13 || event.keyCode == 0) {
        setTimeout("testingForSSl()", 200);
    }
}
var temp3 = "", temp4 = "";
function testingForSSl() {
    document.searchQuotationform.carrierContact.value = "";
    document.searchQuotationform.carrierPhone.value = "";
    document.searchQuotationform.carrierFax.value = "";
    document.searchQuotationform.carrierEmail.value = "";
    var account = document.getElementById("sslDescription").value;
    temp3 = document.getElementById("sslDescription").value;
    var accountNumber = document.getElementById("sslcode").value;
    temp4 = document.getElementById("sslcode").value;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: accountNumber
        },
        success: function(data) {
            if (undefined !== data && data !== "") {
                document.searchQuotationform.carrierContact.value = "";
                document.searchQuotationform.carrierPhone.value = "";
                document.searchQuotationform.carrierFax.value = "";
                document.searchQuotationform.carrierEmail.value = "";
                document.getElementById("sslDescription").value = "";
                document.getElementById("sslDescription").value = "";
                document.getElementById("sslcode").value = "";
                alertNew(data);
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
                    success: function(result) {
                        populateClientDetails1(result);
                    }
                });
            }
        }
    });
}
function populateClientDetails1(data) {
    if (document.searchQuotationform.ratesNonRates[0].checked) {
        if (null != data && (null == data.sslineNumber || data.sslineNumber == '' || data.sslineNumber == '00000')) {
            alertNew("Please select Steamship Line with 5 digit Ssline Number");
            document.searchQuotationform.carrierContact.value = "";
            document.searchQuotationform.carrierPhone.value = "";
            document.searchQuotationform.carrierFax.value = "";
            document.searchQuotationform.carrierEmail.value = "";
            document.getElementById("sslDescription").value = "";
            document.getElementById("sslDescription").value = "";
            document.getElementById("sslcode").value = "";
            return;
        }
    }
    document.getElementById("contactNameButtonSecond").style.visibility = "visible";
    document.searchQuotationform.sslDescription.value = data.acctName;
    temp3 = data.acctName;
    document.searchQuotationform.sslcode.value = data.acctNo;
    temp4 = data.acctNo;
    document.searchQuotationform.carrierContact.value = "";
    if (data.phone != undefined && data.phone != null) {
        document.searchQuotationform.carrierPhone.value = data.phone;
    } else {
        document.searchQuotationform.carrierPhone.value = "";
    }
    if (data.fax != undefined && data.fax != null) {
        document.searchQuotationform.carrierFax.value = data.fax;
    } else {
        document.searchQuotationform.carrierFax.value = "";
    }
    if (data.email1 != undefined && data.email1 != null) {
        document.searchQuotationform.carrierEmail.value = data.email1;
    } else {
        document.searchQuotationform.carrierEmail.value = "";
    }
    document.getElementById("carrierContact").focus();
    setTimeout("set1()", 20);
}
function set1() {
    document.searchQuotationform.sslDescription.value = temp3;
    document.searchQuotationform.sslcode.value = temp4;
}
var temp1 = "", temp2 = "";
function testing(isContact) {
    document.searchQuotationform.clienttype.value = "";
    if (!isContact) {
        document.searchQuotationform.contactName.value = "";
        document.searchQuotationform.email.value = "";
    }
    document.searchQuotationform.phone.value = "";
    document.searchQuotationform.fax.value = "";
    var account = document.getElementById("customerName").value;
    temp1 = document.getElementById("customerName").value;
    var accountNumber = document.getElementById("clientNumber").value;
    temp2 = document.getElementById("clientNumber").value;
    var importFlag = document.getElementById("importFlag").value;
    if (importFlag === 'false') {
        addBrandvalueForanAccount(temp2);
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: accountNumber
        },
        success: function(data) {
            if (undefined !== data && data !== "" && data != "null") {
                document.getElementById("customerName").value = "";
                document.getElementById("clientNumber").value = "";
                document.searchQuotationform.clienttype.value = "";
                document.searchQuotationform.contactName.value = "";
                document.searchQuotationform.phone.value = "";
                document.searchQuotationform.fax.value = "";
                document.searchQuotationform.email.value = "";
                alertNew(data);
            } else {
                if (data != "null") {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                        methodName: "getClientDetails",
                        param1: account,
                        param2: accountNumber,
                        dataType: "json"
                    },
                    success: function(result) {
                        populateClientDetails(result, isContact);
                    }
                });
            }
        }
            }
    });
}
function populateClientDetails(data, isContact) {
    if (data != null && data.subType != null && (data.acctType == 'V' || data.acctType == 'V,SS' || data.acctType == 'V,O' || data.acctType == 'O,V') && data.subType && ((data.subType).toLowerCase()) != 'forwarder') {
        alertNew("Please select the Customers  with Account Type S,C,O and V with Sub Type Forwarder");
        document.searchQuotationform.customerName.value = "";
        document.searchQuotationform.clienttype.value = "";
        document.searchQuotationform.phone.value = "";
        document.searchQuotationform.fax.value = "";
        document.searchQuotationform.email.value = "";
        document.getElementById("clientNumber").value = "";
        jQuery('#clientIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
        document.getElementById("customerName").focus();
    } else {
        document.getElementById("contactNameButton").style.visibility = "visible";
        document.searchQuotationform.customerName.value = data.acctName;
        temp1 = data.acctName;
        document.searchQuotationform.clientNumber.value = data.acctNo;
        document.getElementById("clientNumber").value = data.acctNo;
        temp2 = data.acctNo;
        if (data.clientTypeForDwr != undefined && data.clientTypeForDwr != null) {
            document.searchQuotationform.clienttype.value = data.clientTypeForDwr;
        } else {
            document.searchQuotationform.clienttype.value = "";
        }
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "isCommodityChangeApplyForThisCustomer",
                param1: data.acctNo
            },
            success: function(data) {
                if (null !== data && jQuery.trim(data) !== "") {
                    var codIndx = data.indexOf(',');
                    var comCod = data.substring(0, codIndx);
                    var comCodDesc = data.substring(codIndx + 1);
                    document.searchQuotationform.commcode_check.value = comCod;
                    document.searchQuotationform.commcode.value = comCod;
                    document.searchQuotationform.description.value = comCodDesc;
                }else {
                    var importFlag = document.getElementById("importFlag").value;
                    if (importFlag == 'true') {
                        document.searchQuotationform.commcode_check.value = '006205';
                        document.searchQuotationform.commcode.value = '006205';
                    } else {
                        document.searchQuotationform.commcode_check.value = '006100';
                        document.searchQuotationform.commcode.value = '006100';
                    }
                }
            }
        });
        if (!isContact) {
            document.searchQuotationform.contactName.value = "";
        }
        if (data.phone != undefined && data.phone != null) {
            document.searchQuotationform.phone.value = data.phone;
        } else {
            document.searchQuotationform.phone.value = "";
        }
        if (data.fax != undefined && data.fax != null) {
            document.searchQuotationform.fax.value = data.fax;
        } else {
            document.searchQuotationform.fax.value = "";
        }
        if (!isContact) {
            if (data.email1 != undefined && data.email1 != null) {
                document.searchQuotationform.email.value = data.email1;
            } else {
                document.searchQuotationform.email.value = "";
            }
        }
        if (null != data.acctNo && data.acctNo != undefined) {
            isCustomerNotes(data.acctNo);
        }
        if (document.getElementById('fileType') && document.getElementById('fileType').value == 'I') {
            document.getElementById('isTerminal').focus();
        } else {
            document.getElementById('portofDischarge').focus();
        }
        if (document.getElementById('fileTypeS') && null != data.acctNo && data.acctNo != undefined) {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName: "getDefaultDetails",
                    param1: data.acctNo,
                    dataType: "json"
                },
                success: function(result) {
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
                            success: function(data) {
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
            setTimeout("set()", 20);
        }
    }
}

function fillDefaultCustomerValues(importFlag) {
    document.getElementById('portofDischarge').value = defaultCustomerData.destination;
    document.getElementById('portofDischarge_check').value = defaultCustomerData.destination;
    document.getElementById('isTerminal').value = defaultCustomerData.origin;
    document.getElementById('isTerminal_check').value = defaultCustomerData.origin;
    if (null !== defaultCustomerData.commodityCode && defaultCustomerData.commodityCode !== defaultCustomerData.commodityCode) {
        document.getElementById('commcode').value = defaultCustomerData.commodityCode;
        document.getElementById('commcode_check').value = defaultCustomerData.commodityCode;
    }
    if (defaultCustomerData.issuingTerminal !== '' && null !== defaultCustomerData.issuingTerminal) {
        document.getElementById('issuingTerminal_check').value = defaultCustomerData.issuingTerminal;
        document.getElementById('issuingTerminal').value = defaultCustomerData.issuingTerminal;
    }
    document.getElementById('placeofReceipt').value = defaultCustomerData.pol;
    document.getElementById('placeofReceipt_check').value = defaultCustomerData.pol;
    document.getElementById('finalDestination').value = defaultCustomerData.pod;
    document.getElementById('finalDestination_check').value = defaultCustomerData.pod;
    document.getElementById('typeofMoveSelect').value = defaultCustomerData.nvoMove;
    document.getElementById('zip').value = defaultCustomerData.originZip;
    document.getElementById('zip_check').value = defaultCustomerData.originZip;
    document.getElementById('doorOrigin').value = defaultCustomerData.doorOrigin;
    document.getElementById('doorOrigin_check').value = defaultCustomerData.doorOrigin;
    document.searchQuotationform.goodsdesc.value = defaultCustomerData.goodsDesc;
    document.searchQuotationform.docChargeAmount.value = defaultCustomerData.documentAmount;
    if (defaultCustomerData.ffComm == 'Y') {
        document.searchQuotationform.deductFFcomm[0].checked = true;
    } else {
        document.searchQuotationform.deductFFcomm[1].checked = true;
    }
    if (defaultCustomerData.documentCharge == 'Y') {
        document.searchQuotationform.docCharge[0].checked = true;
    } else {
        document.searchQuotationform.docCharge[1].checked = true;
    }
    document.getElementById('routedAgentCheck').value = defaultCustomerData.ert;
    if (null != defaultCustomerData.destination && defaultCustomerData.destination != '') {
        defaultFromCustgeneral(defaultCustomerData.ert, defaultCustomerData.importantNotes, importFlag);
    } else {
        document.getElementById('routedbymsg').value = "";
        document.getElementById('routedbymsg_check').value = "";
        document.getElementById('agent').value = "";
        document.getElementById('agent_check').value = "";
        document.getElementById('agentNo').value = "";
        document.searchQuotationform.importantNotes.value = defaultCustomerData.importantNotes;
        document.searchQuotationform.buttonValue.value = "applyDefaultValues";
        document.searchQuotationform.submit();
    }
}

function onclickAlertOk(importFlag) {
    document.getElementById('AlertBoxDefaultValues').style.display = 'none';
    grayOut(false, '');
    fillDefaultCustomerValues(importFlag);
}

var defaultCustomerData;
function fillDefaultCustomerData(importFlag) {
    defaultCustomerData = null;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "getDefaultDetails",
            param1: document.getElementById("clientNumber").value,
            dataType: "json"
        },
        success: function(data) {
            if (null !== data) {
                defaultCustomerData = data;
                if (defaultCustomerData.importantNotes !== "" && defaultCustomerData.importantNotes !== undefined && defaultCustomerData.importantNotes !== null) {
                    alertNewForDefaultValue(defaultCustomerData.importantNotes.replace(/\n/g, "<br/>"));
                } else {
                    fillDefaultCustomerValues(importFlag);
                }
            }
        }
    });
}
function defaultFromCustgeneral(ert, importantNotes) {
    var pod = document.searchQuotationform.portofDischarge.value;
    if (pod.lastIndexOf("(") > -1 && pod.lastIndexOf(")") > -1) {
        var podNew = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                methodName: "getDefaultAgent",
                param1: podNew,
                param2: importFlag === "true" ? "I" : "F",
                dataType: "json"
            },
            success: function(data) {
                if (null !== data && null !== data.accountno && jQuery.type(data.accountno) !== "undefined" && jQuery.trim(data.accountno) !== "") {
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                            methodName: "checkForDisable",
                            param1: data.accountno
                        },
                        success: function(dataNew) {
                            if (jQuery.trim(dataNew) !== "") {
                                document.getElementById("agentNo").value = "";
                                document.getElementById("agent").value = "";
                                document.getElementById("routedAgentCheck").value = "";
                                document.searchQuotationform.defaultAgent[1].checked = true;
                                document.searchQuotationform.importantNotes.value = importantNotes;
                                document.searchQuotationform.buttonValue.value = "applyDefaultValues";
                                document.searchQuotationform.submit();
                            } else {
                                if (data.accountName !== undefined && data.accountName !== "" && data.accountName != null) {
                                    document.getElementById("agent").value = data.accountName;
                                    document.getElementById("agent_check").value = data.accountName;
                                    document.searchQuotationform.defaultAgent[0].checked = true;
                                } else {
                                    document.getElementById("agent").value = "";
                                    document.searchQuotationform.defaultAgent[0].checked = false;
                                }
                                if (data.accountno != undefined && data.accountno != "" && data.accountno != null) {
                                    document.getElementById("agentNo").value = data.accountno;
                                } else {
                                    document.getElementById("agentNo").value = "";
                                }
                                if (ert == 'yes' && data.accountno != undefined && data.accountno != "" && data.accountno != null) {
                                    document.getElementById('routedbymsg').value = data.accountno;
                                    document.getElementById('routedbymsg_check').value = data.accountno;
                                } else {
                                    document.getElementById('routedbymsg').value = "";
                                    document.getElementById('routedbymsg_check').value = "";
                                }
                                document.searchQuotationform.importantNotes.value = importantNotes;
                                document.searchQuotationform.buttonValue.value = "applyDefaultValues";
                                document.searchQuotationform.submit();
                            }
                        }
                    });
                    document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
                    document.getElementById("agent").readOnly = true;
                } else {
                    if (document.getElementById("nonRated").checked) {
                        document.searchQuotationform.buttonValue.value = "applyDefaultValues";
                        document.searchQuotationform.submit();
                    }
                }
            }
        });
    } else {
        document.searchQuotationform.importantNotes.value = importantNotes;
        document.searchQuotationform.buttonValue.value = "applyDefaultValues";
        document.searchQuotationform.submit();
    }
}
function set() {
    document.searchQuotationform.customerName.value = temp1;
    document.searchQuotationform.clientNumber.value = temp2;
    getCreditStatus("alert");
}
//function setFocussing() {
//   if (document.getElementById('fileType') && document.getElementById('fileType').value == 'I') {
//       document.getElementById('isTerminal').focus();
//    } else {
//        document.getElementById('portofDischarge').focus();
//   }
//}
function findRate() {
    document.searchQuotationform.rateId.value = "expand";
}
function findRateForCollapse() {
    document.searchQuotationform.rateId.value = "collapse";
}
function getContactNameAndEmailfromPopup(val1, val2, val3, val4, fclContactId, fclAccountNo) {
    localStorage.setItem("fclContactId",fclContactId);
    localStorage.setItem("fclAccountNo",fclAccountNo);
    document.searchQuotationform.contactName.value = val1;
    document.searchQuotationform.email.value = val2;
    if (val3 != "" && val3 != undefined && val3 != null) {
        document.searchQuotationform.phone.value = val3;
    }
    if (val4 != "" && val4 != undefined && val4 != null) {
        document.searchQuotationform.fax.value = val4;
    }
}
function getMultipleCarrierContactNameAndEmailfromPopup(val1, val2, val3, val4) {
    document.searchQuotationform.carrierContact.value = val1;
    document.searchQuotationform.carrierEmail.value = val2;
    if (val3 != "" && val3 != undefined && val3 != null) {
        document.searchQuotationform.carrierPhone.value = val3;
    }
    if (val4 != "" && val4 != undefined && val4 != null) {
        document.searchQuotationform.carrierFax.value = val4;
    }
}
function assignRemarksValue(ev) {
    document.searchQuotationform.remarks.value = ev;
    document.searchQuotationform.buttonValue.value = "assignRemarks";
    document.searchQuotationform.submit();
}
function getHazmatForRates() {
    if (document.searchQuotationform.numbers == undefined && null == document.getElementById('otherCharges')) {
        alertNew("Please Select Rates before selecting Hazmat");
        jQuery("#charge").css("border-color", "red");
        document.getElementById('hazmatNo').checked = true;
        return;
    } else if (document.searchQuotationform.inland[0].checked) {
        document.searchQuotationform.ssline.value = document.searchQuotationform.sslDescription.value + "//" + document.searchQuotationform.sslcode.value;
        document.searchQuotationform.selectedOrigin.value = document.searchQuotationform.isTerminal.value;
        document.searchQuotationform.selectedDestination.value = document.searchQuotationform.portofDischarge.value;
        document.searchQuotationform.selectedComCode.value = document.searchQuotationform.commcode.value;
        if (document.getElementById('rampCheck').checked) {
            confirmNew("This option may add a Hazardous Surcharge to the Rates below and Saves all the Quotation changes.INTERMODAL RAMP charges will be removed and MUST be recalculated. Are you sure?", "hazmatWithIntrModel");
        } else {
            confirmNew("This option may add a Hazardous Surcharge to the Rates below and Saves all the Quotation changes.INLAND charges will be removed and MUST be recalculated. Are you sure?", "hazmatWithInland");
        }
    } else {
        //if (document.searchQuotationform.ratesNonRates[1].checked) {
        document.searchQuotationform.ssline.value = document.searchQuotationform.sslDescription.value + "//" + document.searchQuotationform.sslcode.value;
        document.searchQuotationform.selectedOrigin.value = document.searchQuotationform.isTerminal.value;
        document.searchQuotationform.selectedDestination.value = document.searchQuotationform.portofDischarge.value;
        document.searchQuotationform.selectedComCode.value = document.searchQuotationform.commcode.value;
        document.getElementById("commentRemark").value = "Hazardous Cargo";
        confirmNew("This option may add a Hazardous Surcharge to the Rates below and Saves all the Quotation changes. Are you sure?", "getHazmatForRates");
    }
}

function getHazmatForRatesN() {
    if (document.searchQuotationform.numbers == undefined && null == document.getElementById('otherCharges')) {
        alertNew("Please Select Rates before selecting Hazmat");
        document.getElementById('hazmatYes').checked = true;
        return;
    } else {
        //if (document.searchQuotationform.ratesNonRates[1].checked) {
        document.searchQuotationform.ssline.value = document.searchQuotationform.sslDescription.value + "//" + document.searchQuotationform.sslcode.value;
        document.searchQuotationform.selectedOrigin.value = document.searchQuotationform.isTerminal.value;
        document.searchQuotationform.selectedDestination.value = document.searchQuotationform.portofDischarge.value;
        document.searchQuotationform.selectedComCode.value = document.searchQuotationform.commcode.value;
        document.getElementById("commentRemark").value = "";
        confirmNew("This option will delete any Hazardous descriptions you might have entered and Saves all the Quotation changes. Are you sure?", "getHazmatForRatesN");
        //}
    }
}
function getDesc(ev) {
    if (event.keyCode == 13 || event.keyCode == 9 || event.keyCode == 0) {
        var ind = ev.indexOf(":");
        if (ind != -1) {
            var newCode = ev.substring(0, ind);
            var newDesc = ev.substring(ind + 1, ev.length);
            document.getElementById("goodsdescTemp").value = newCode;
            document.getElementById("goodsDesc").value = newDesc;
        }
    }
}
function getRemark(ev) {
    if (event.keyCode == 13 || event.keyCode == 9 || event.keyCode == 0) {
        var ind = ev.indexOf(":");
        if (ind != -1) {
            var newCode = ev.substring(0, ind);
            var newDesc = ev.substring(ind + 1, ev.length);
            document.getElementById("commentTemp").value = newCode;
            document.getElementById("commentRemark").value = newDesc;
        }
    }
}
function hideOrgZip() {
//   document.getElementById("originZip").style.visibility = "hidden";
//   document.searchQuotationform.zip.value="";
//   document.getElementById("zipLookUp").style.visibility = "hidden";
}
function getTypeOfMove() {
    if (event.keyCode == 13 || event.keyCode == 9 || event.keyCode == 0) {
        if (document.searchQuotationform.doorOrigin.value != "") {
            //document.getElementById("originZip").style.visibility = "visible";
            //document.searchQuotationform.zip.value="";
            //document.getElementById("zipLookUp").style.visibility = "visible";
        } else {
            hideOrgZip();
        }
        var length = document.getElementById("typeofMoveSelect").length;
        for (var i = 0; i < length; i++) {
            if (document.getElementById("typeofMoveSelect").options[i].text == "DOOR TO PORT") {
                document.getElementById("typeofMoveSelect").selectedIndex = i;
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

function goToRemarksLookUp(importFlag) {
    document.getElementById("scrollUp").scrollIntoView(true);
    GB_show("Pre-defined Remarks", "/logisoft/remarksLookUp.do?buttonValue=Quotation&importFlag=" + importFlag, 400, 820);
}
function getAllRemarksFromPopUp(val) {
    var commentVal = document.searchQuotationform.comment.value;
    var totalLength = commentVal.length + val.length;
    if (totalLength > 500) {
        alertNew('More than 500 characters are not allowed');
        return;
    }
    var oldarray = document.searchQuotationform.comment.value;
    var splittedArray;
    if (oldarray.length == 0) {
        splittedArray = oldarray;
    } else {
        splittedArray = oldarray.split("\n");
    }
    var newarray = val.split(">>");
    var resultarray = new Array();
    var flag = false;
    for (var k = 0; k < newarray.length; k++) {
        flag = false;
        for (var l = 0; l < splittedArray.length; l++) {
            if (newarray[k].replace(/^[\s]+/, "").replace(/[\s]+$/, "").replace(/[\s]{2,}/, " ") == splittedArray[l].replace(/^[\s]+/, "").replace(/[\s]+$/, "").replace(/[\s]{2,}/, " ")) {
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
    document.searchQuotationform.comment.value = oldarray.replace(/>/g, "");
}
function getRemarks() {
    if (document.searchQuotationform.comment.value.length > 500) {
        alertNew("Please enter only 500 Characters");
        return;
    }
}
//---part of Confirm function of New Alertbox -----
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
            success: function(data) {
                populateSSlineAcctNo(data);
            }
        });
    }
}
function getConverttoBook(val, val1) {
    document.searchQuotationform.unitSelect.value = val1;
    if (collapseid == "") {
        getcollapse();
    }
    document.searchQuotationform.check1.value = collapsePrint;
    document.searchQuotationform.collapseid.value = collapseid;
    checkPrintInclude();
    if (val == "copyQuote") {
        document.searchQuotationform.buttonValue.value = "copyQuoteWithRate";
    } else if (val == "oldgetRatesBKG") {
        document.searchQuotationform.buttonValue.value = "converttobook";
    } else {
        document.searchQuotationform.buttonValue.value = "converttobooknew";
    }
    getCreditStatus('warning');
}
function populateSSlineAcctNo(data) {
    document.searchQuotationform.carrierContact.value = "";
    document.getElementById("contactNameButtonSecond").style.visibility = "visible";
    document.searchQuotationform.sslcode.value = data.acctNo != null ? data.acctNo : "";
    document.searchQuotationform.carrierPhone.value = data.phone != null ? data.phone : "";
    document.searchQuotationform.carrierFax.value = data.fax != null ? data.fax : "";
    document.searchQuotationform.carrierEmail.value = data.email1 != null ? data.email1 : "";
    setTimeout("setFocusToCarrier()", 150);
}
function checkEmail() {
    if (document.searchQuotationform.email.value == "") {
        document.searchQuotationform.ccEmail.checked = false;
        alertNew("Please Enter Email Id");
        //document.searchQuotationform.email.focus();
        return;
    }
}
function getGoogleMap() {
    if (document.searchQuotationform.zip.value == "") {
        alertNew("Please Enter Zip");
        return;
    }
    var addr = document.searchQuotationform.zip.value;
    array1 = addr.split("-");
    if (array1[1] == undefined) {
        array1[1] = array1[0];
    }
    GB_show("GoogleMap", "/logisoft/jsps/fclQuotes/GoogleMap.jsp?address=" + array1[1], 600, 600);
}
function setNvoMove() {
    var optionValue = "";
    var length = "";
    var i = 0;
    if (document.searchQuotationform.zip.value != "" && document.getElementById("doorDestination").value == "") {
        optionValue = document.getElementById("typeofMoveSelect").options[document.getElementById("typeofMoveSelect").selectedIndex].text;
        if (optionValue != "DOOR TO DOOR" && optionValue != "DOOR TO RAIL" && optionValue != "DOOR TO PORT"
                && optionValue != "RAMP TO DOOR" && optionValue != "RAMP TO RAIL" && optionValue != "RAMP TO PORT") {
            length = document.getElementById("typeofMoveSelect").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("typeofMoveSelect").options[i].text == "DOOR TO PORT") {
                    document.getElementById("typeofMoveSelect").selectedIndex = i;
                }
            }
            alertNew("Please select the option that starts from DOOR");
        } else {
            tabNvoMoveimp();
        }
    } else if (document.searchQuotationform.zip.value == "" && document.getElementById("doorDestination").value != "") {
        optionValue = document.getElementById("typeofMoveSelect").options[document.getElementById("typeofMoveSelect").selectedIndex].text;
        if (optionValue != "PORT TO DOOR" && optionValue != "RAIL TO DOOR" && optionValue != "DOOR TO DOOR"
                && optionValue != "RAMP TO DOOR" && optionValue != "RAMP TO RAIL" && optionValue != "RAMP TO PORT") {
            length = document.getElementById("typeofMoveSelect").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("typeofMoveSelect").options[i].text == "PORT TO DOOR") {
                    document.getElementById("typeofMoveSelect").selectedIndex = i;
                }
            }
            alertNew("Please select the option that ends from DOOR");
        } else {
            tabNvoMoveimp();
        }
    } else if (document.searchQuotationform.zip.value != "" && document.getElementById("doorDestination").value != "") {
        optionValue = document.getElementById("typeofMoveSelect").options[document.getElementById("typeofMoveSelect").selectedIndex].text;
        if (optionValue != "DOOR TO DOOR" && optionValue != "RAMP TO DOOR" && optionValue != "RAMP TO RAIL" && optionValue != "RAMP TO PORT") {
            length = document.getElementById("typeofMoveSelect").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("typeofMoveSelect").options[i].text == "DOOR TO DOOR") {
                    document.getElementById("typeofMoveSelect").selectedIndex = i;
                }
            }
            alertNew("Please select the option that start and ends from DOOR");
        } else {
            tabNvoMoveimp();
        }
    } else {
        tabNvoMoveimp();
    }

}
function selectNVOmove() {
    var length = 0;
    var i = 0;
    if (document.getElementById("doorDestination").value == "" && document.getElementById("zip").value == "") {
        length = document.getElementById("typeofMoveSelect").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("typeofMoveSelect").options[i].text == "PORT TO PORT") {
                document.getElementById("typeofMoveSelect").selectedIndex = i;
            }
        }
    } else if (document.getElementById("zip").value != "" && document.getElementById("rampCheck").checked) {
        length = document.getElementById("typeofMoveSelect").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("typeofMoveSelect").options[i].text == "RAMP TO PORT") {
                document.getElementById("typeofMoveSelect").selectedIndex = i;
            }
        }
    } else if (document.getElementById("doorDestination").value != "" && document.getElementById("zip").value == "") {
        length = document.getElementById("typeofMoveSelect").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("typeofMoveSelect").options[i].text == "PORT TO DOOR") {
                document.getElementById("typeofMoveSelect").selectedIndex = i;
            }
        }
    } else if (document.getElementById("doorDestination").value != "" && document.getElementById("zip").value != "") {
        length = document.getElementById("typeofMoveSelect").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("typeofMoveSelect").options[i].text == "DOOR TO DOOR") {
                document.getElementById("typeofMoveSelect").selectedIndex = i;
            }
        }

    } else if (document.getElementById("doorDestination").value == "") {
        length = document.getElementById("typeofMoveSelect").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("typeofMoveSelect").options[i].text == "DOOR TO PORT") {
                document.getElementById("typeofMoveSelect").selectedIndex = i;
            }
        }
    }
}
var bolId;
function setDefaultAgent(ev2) {

    if (document.searchQuotationform.hazmat[0].checked) {
        document.getElementById("HazmatButton").style.visibility = 'visible'
        document.getElementById("HazmatButtonDown").style.visibility = 'visible'
    } else {
        document.getElementById("HazmatButton").style.visibility = 'hidden'
        document.getElementById("HazmatButtonDown").style.visibility = 'hidden'
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkHazmat",
            param1: ev2
        },
        success: function(data) {
            if (data === "true") {
                document.getElementById("HazmatButton").className = "buttonColor";
                document.getElementById("HazmatButtonDown").className = "buttonColor";
            } else {
                document.getElementById("HazmatButton").className = "buttonStyleNew";
                document.getElementById("HazmatButtonDown").className = "buttonStyleNew";
            }
        }
    });
    bolId = ev2;
}
function checkHazmat() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkHazmat",
            param1: bolId
        },
        success: function(data) {
            if (data === "true") {
                document.getElementById("HazmatButton").className = "buttonColor";
                document.getElementById("HazmatButtonDown").className = "buttonColor";
            } else {
                document.getElementById("HazmatButton").className = "buttonStyleNew";
                document.getElementById("HazmatButtonDown").className = "buttonStyleNew";
            }
        }
    });
}
function fillDefaultAgent() {
    document.getElementById("agent").className = "textlabelsBoldForTextBox";
    document.getElementById("routedAgentCheck").className = "dropdown_accounting";
    document.getElementById("routedbymsg").className = "textlabelsBoldForTextBox";
    jQuery("#directConsignmentN").attr("checked",true);
    jQuery("#directConsignmentY").attr("checked",false);
    jQuery("#agent").attr("disabled", false);
    jQuery("#agentNo").attr("disabled", false);
    jQuery("#routedAgentCheck").attr("disabled", false);
    jQuery("#routedbymsg").attr("disabled", false);
    var pod = document.searchQuotationform.portofDischarge.value;
    if (pod.lastIndexOf("(") > -1 && pod.lastIndexOf(")") > -1) {
        var podNew = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                methodName: "getDefaultAgent",
                param1: podNew,
                param2: importFlag === "true" ? "I" : "F",
                dataType: "json"
            },
            success: function(data) {
                defaultAgentFill(data);
            }
        });
    }
}
function directConsignmnt() {
        document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";        
        document.getElementById("routedAgentCheck").className = "dropdown_accountingDisabled";
        document.getElementById("routedbymsg").className = "textlabelsBoldForTextBoxDisabledLook";
        jQuery("#defaultAgentN").attr("checked", true);
        jQuery("#routedAgentCheck").val("");
        jQuery("#routedbymsg").val("");
        jQuery("#agent").val("");
        jQuery("#agentNo").val("");
        jQuery("#agent").attr("disabled", true);
        jQuery("#agentNo").attr("disabled", true);
        jQuery("#routedAgentCheck").attr("disabled", true);
        jQuery("#routedbymsg").attr("disabled", true);
    }
    function directConsignmntNo() {
        document.getElementById("agent").className = "textlabelsBoldForTextBox";        
        document.getElementById("routedAgentCheck").className = "dropdown_accounting";
        document.getElementById("routedbymsg").className = "textlabelsBoldForTextBox";
        jQuery("#defaultAgentY").attr("checked", true);
        jQuery("#directConsignmentN").attr("checked", true);
        fillDefaultAgent();
    }
function defaultAgentFill(data) {
    if (data.accountno != undefined && data.accountno != "" && data.accountno != null) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "checkForDisable",
                param1: data.accountno
            },
            success: function(dataNew) {
                if (dataNew != "") {
                        alertNew(dataNew.replace('This','Default Agent'));
                        document.QuotesForm.defaultAgent[1].checked = true;
                } else {
                    if (data.accountName != undefined && data.accountName != "" && data.accountName != null) {
                        document.getElementById("agent").value = data.accountName;
                        document.getElementById("agent_check").value = data.accountName;
                    } else {
                        document.getElementById("agent").value = "";
                    }
                    if (data.accountno != undefined && data.accountno != "" && data.accountno != null) {
                        document.getElementById("agentNo").value = data.accountno;
                    } else {
                        document.getElementById("agentNo").value = "";
                    }
                }
            }
        });
        document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
        document.getElementById("agent").readOnly = true;
        document.getElementById("agent").tabIndex = -1;
        var selectedErt = document.searchQuotationform.routedAgentCheck.value;
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getDefaultDetails",
                param1: document.getElementById("clientNumber").value,
                dataType: "json"
            },
            success: function(obj) {
                if ((document.searchQuotationform.routedAgentCheck.value == "yes" || document.searchQuotationform.routedAgentCheck.value == "no")
                        && !(null != obj && obj.ert != '' && selectedErt == obj.ert)) {
                    document.getElementById("routedbymsg").value = "";
                    document.getElementById("routedbymsg").className = "textlabelsBoldForTextBox";
                    document.searchQuotationform.routedAgentCheck.value = "";
                }
            }
        });
    }
}
function clearValues() {
    document.getElementById("agent").value = "";
    document.getElementById("agent").readOnly = false;
    document.getElementById("agent").tabIndex = 0;
    document.getElementById("agentNo").value = "";
    document.getElementById("agent").className = "textlabelsBoldForTextBox";
    document.getElementById("routedbymsg").readOnly = false;
    document.getElementById("routedbymsg").tabIndex = 0;
    var selectedErt = document.searchQuotationform.routedAgentCheck.value;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "getDefaultDetails",
            param1: document.getElementById("clientNumber").value,
            dataType: "json"
        },
        success: function(obj) {
            if (null != obj && obj.ert != '' && selectedErt == obj.ert) {
                document.getElementById("routedbymsg").value = "";
                document.getElementById("routedbymsg").className = "textlabelsBoldForTextBox";
            } else if (document.searchQuotationform.routedAgentCheck.value == "yes" || document.searchQuotationform.routedAgentCheck.value == "no") {
                document.getElementById("agent").readOnly = false;
                document.getElementById("routedbymsg").value = "";
                document.getElementById("routedbymsg").className = "textlabelsBoldForTextBox";
                document.searchQuotationform.routedAgentCheck.value = "";
            }
        }
    });
}
function updateContactInfoAfterDeletion(contName, contEmail) {
    var contactName = document.getElementById('contactName').value;
    var contactEmail = document.getElementById('email').value;
    var contactNameArray = contactName.split(",");
    var newContactName = '';
    for (var i = 0; i < contactNameArray.length; i++) {
        var temp = contactNameArray[i];
        if (temp == contName) {

        } else {
            if (newContactName == '') {
                newContactName = temp;
            } else {
                newContactName = newContactName + "," + temp;
            }
        }
    }
    document.getElementById('contactName').value = newContactName;
    //--UPDATING EMAIL---
    var contactEmailArray = contactEmail.split(",");
    var newContactEmail = '';
    for (var i = 0; i < contactEmailArray.length; i++) {
        var temp = contactEmailArray[i];
        if (temp == contEmail) {

        } else {
            if (newContactEmail == '') {
                newContactEmail = temp;
            } else {
                newContactEmail = newContactEmail + "," + temp;
            }
        }
    }
    document.getElementById('email').value = newContactEmail;
}
function updateCarrierContactAfterDeletion(contName, contEmail) {
    var carrierContact = document.getElementById('carrierContact').value;
    var carrierEmail = document.getElementById('carrierEmail').value;

    var carrierContactArray = carrierContact.split(",");
    var newContactName = '';
    for (var i = 0; i < carrierContactArray.length; i++) {
        var temp = carrierContactArray[i];
        if (temp == contName) {

        } else {
            if (newContactName == '') {
                newContactName = temp;
            } else {
                newContactName = newContactName + "," + temp;
            }
        }
    }
    document.getElementById('carrierContact').value = newContactName;

    //--UPDATING CARRIER EMAIL---
    var carrierEmailArray = carrierEmail.split(",");
    var newContactEmail = '';
    for (var i = 0; i < carrierEmailArray.length; i++) {
        var temp = carrierEmailArray[i];
        if (temp == contEmail) {

        } else {
            if (newContactEmail == '') {
                newContactEmail = temp;
            } else {
                newContactEmail = newContactEmail + "," + temp;
            }
        }
    }
    document.getElementById('carrierEmail').value = newContactEmail;
}
function disableAutoFF() {
    if (undefined != document.getElementById('portofDischarge') && null != document.getElementById('portofDischarge')
            && '' != document.getElementById('portofDischarge').value && importFlag == 'false') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "checkForTheRegion",
                param1: document.getElementById('portofDischarge').value
            },
            success: function(data) {
                if (jQuery.trim(data) === "true") {
                    checkforCommodity();
                } else {
                    document.getElementById('n5').checked = true;
                    document.getElementById('n5').disabled = true;
                    document.getElementById('y5').disabled = true;
                }
            }
        });
    }
}
function checkforCommodity() {
    var commcode = document.getElementById("commcode").value;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForCommodity",
            param1: commcode
        },
        success: function(data) {
            if (jQuery.trim(data) === "true") {
                document.getElementById('n5').disabled = false;
                document.getElementById('y5').disabled = false;
            } else {
                document.getElementById('n5').checked = true;
                document.getElementById('n5').disabled = true;
                document.getElementById('y5').disabled = true;
            }
        }
    });
}
function changeOriginAndDestination() {
    confirmNew("This will allow to change the Origin & Destination,but will remove all the existing rates.Continue?", "changeOriginDestination");
}
function checkDisable() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: document.getElementById('routedNo').value
        },
        success: function(data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
                document.getElementById('routedbymsg').value = "";
            } else {
                document.getElementById('routedbymsg').value = document.getElementById('routedNo').value;
                document.searchQuotationform.goodsdesc.focus();
            }
        }
    });
}
function checkDisableForAgent(focusTo) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: document.getElementById('agentNo').value
        },
        success: function(data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
                document.getElementById('agentNo').value = "";
                document.getElementById('agent').value = "";
            }
        }
    });
    setFocusFromDojo(focusTo);
}
function checkIfRatesAreFromGetRates() {
    var val;
    if (document.getElementById("expandRatesTable") != null) {
        var expandTable = document.getElementById("expandRatesTable");
        var rowObj = expandTable.rows[1];
        var chargecode = rowObj.cells[3].innerHTML;
        var redStarIndex = chargecode.indexOf("*");
        if (redStarIndex != -1) {
            val = 'true';
            return val;
        }
    } else {
        val = 'true';
        return val;
    }
}
function checkDeliveryRates() {
    var result = 'false';
    if (document.getElementById("expandRatesTable") != null) {
        var expandTable = document.getElementById("expandRatesTable");
        for (var i = 0; i < expandTable.rows.length; i++) {
            var rowObj = expandTable.rows[i];
            var chargecode = rowObj.cells[3].innerHTML;
            var deliveryIndex = chargecode.indexOf("DELIVERY");
            if (deliveryIndex != -1) {
                result = 'true';
                break;
            }
        }
    }
    return result;
}
function checkOnCarriage() {
    var found = false;
    jQuery('#expandRatesTable').find('tr').each(function() {
        var chargeCode = jQuery(this.cells[3]).html();
        if (chargeCode.indexOf("ONCARRIAGE") != -1) {
            found = true;
        }
    });
    return found;
}
function checkCharges(charge) {
    var found = false;
    jQuery('#expandRatesTable').find('.expandRatesTableChargeCode').each(function() {
        var chargeCode = jQuery(this).val();
        if (chargeCode == charge) {
            found = true;
            return false;
        }
    });
    return found;
}

function showRateGrid(route, path) {
    var origin = "";
    var destination = "";
    var selectedList = "";
    var distance = "";
    var distanceList = "";
    var fileType = "";
    var doorOrigin = document.searchQuotationform.doorOrigin;
    var checkBoxes = document.getElementsByName("originDestination");
    if (importFlag == "true") {
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
                if (route == "Destination" && null != doorOrigin && trim(doorOrigin.value) != "") {
                    distanceList = "" != trim(distance) && null != trim(distance) ? checkBoxes[i].value + "=" + distance : checkBoxes[i].value;
                }
            } else {
                selectedList = selectedList + "," + checkBoxes[i].value;
                if (route == "Destination" && null != doorOrigin && trim(doorOrigin.value) != "") {
                    distanceList = "" != trim(distance) && null != trim(distance) ? distanceList + "," + checkBoxes[i].value + "=" + distance : distanceList + "," + checkBoxes[i].value;
                }
            }
        }
    }
    if (selectedList == "") {
        alert("Please Select atleast One");
        return false;
    }
    if ("Origin" == route) {
        origin = document.searchQuotationform.isTerminal.value;
        destination = selectedList;
    } else {
        destination = document.searchQuotationform.portofDischarge.value;
        origin = selectedList;
    }
    var haz = "N";
    if (document.searchQuotationform.hazmat[0].checked) {
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
    var destinationPort = document.searchQuotationform.portofDischarge.value;
    var originPort = "";
    if (trim(document.searchQuotationform.isTerminal.value) != "") {
        originPort = document.searchQuotationform.isTerminal.value;
    } else {
        originPort = document.searchQuotationform.doorOrigin.value;
    }
    url = path + '/rateGrid.do?action=' + route + '&origin=' + origin +
            "&destination=" + destination + "&commodity=" + document.searchQuotationform.commcode.value + '&hazardous=' + haz + "&zip="
            + document.searchQuotationform.zip.value + "&doorOrigin=" + document.searchQuotationform.doorOrigin.value + "&region"
            + selectedRegion + "&distances=" + distanceList + "&imsChecked=" + imsChecked + "&destinationPort=" + destinationPort + "&originPort=" + originPort + "&fileType=" + fileType;
    GB_show('FCL Rates Comparison Grid', url, document.body.offsetHeight - 50, document.body.offsetWidth - 100);
}

function closeOriginDestinationList() {
    if (null != document.getElementById("originAndDestinationDiv")) {
        document.body.removeChild(document.getElementById("originAndDestinationDiv"));
        document.searchQuotationform.portofDischarge.focus();
    }
    closePopUp();
}

function checkShowAllCity() {
    if (document.getElementById("showAllCity") && document.getElementById("showAllCity").checked
            && (trim(document.searchQuotationform.portofDischarge.value) == "" || trim(document.searchQuotationform.isTerminal.value) == "")) {
        alertNew("PLEASE SELECT DESTINATION PORT AND ORIGIN");
        document.getElementById("showAllCity").checked = false;
        return;
    }
}
function setFocusFromDojo(focusTo) {
    if (focusTo == 'isTerminal') {
        fillDefaultAgent();
    }
    if (document.getElementById(focusTo)) {
        document.getElementById(focusTo).focus();
    }
    allowInsurance();
}
function allowOnlyWholeNumbers(obj) {
    if (obj.value.match(/^-?[0-9]+(.[0-9]{1,2})?$/) == null) {
        obj.value = '';
        alertNew('The format is wrong');
        return false;
    }
    if (obj.value.indexOf(' ') >= 0) {
        obj.value = '';
        alertNew('The format is wrong');
        return false;
    }
}
function allowNagWholeNumbers(obj) {
    var result;
    if (!/^[1-9 . ]\d*$/.test(obj.value)) {
        result = obj.value.replace(/[^-\0-9 . ]+/g, '');
        obj.value = result;
        return false;
    }
    return true;
}
function changeNVOMove() {
    if (document.searchQuotationform.doorDestination.value == "" && !document.getElementById("rampCheck").checked) {
        var length = document.getElementById("typeofMoveSelect").length;
        for (var i = 0; i < length; i++) {
            if (document.searchQuotationform.zip.value != "") {
                if (document.getElementById("typeofMoveSelect").options[i].text == "DOOR TO PORT") {
                    document.getElementById("typeofMoveSelect").selectedIndex = i;
                }
            } else {
                if (document.getElementById("typeofMoveSelect").options[i].text == "PORT TO DOOR") {
                    document.getElementById("typeofMoveSelect").selectedIndex = i;
                }
            }
        }
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
        //        if(document.getElementById('customerName')){
        //            document.getElementById('customerName').value ="";
        //        }
        document.getElementById('customerName1').value ="";
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
function addOrUpdateSpecialEquipment(quoteId, action) {
    if (document.searchQuotationform.specialequipment[1].checked == true) {
        alertNew("Please Check Special Equipment");
        return;
    } else if (document.searchQuotationform.specialEqpmt.value == '') {
        alertNew("Please Select Special Equipment");
        return;
    } else if (document.searchQuotationform.specialEqpmtUnit.value == '') {
        alertNew("Please Select Unit Type");
        return;
    } else {
        if (action == 'add') {
            document.getElementById('addEquipment').disabled = true;
            document.searchQuotationform.buttonValue.value = "addSpecialEquipment";
        } else {
            document.getElementById('updateEquipment').disabled = true;
            document.searchQuotationform.buttonValue.value = "updateSpecialEquipment";
        }
        document.searchQuotationform.submit();
    }
}
function openOutOfGuageComments(val, index, comment) {
    document.searchQuotationform.numbIdx.value = val;
    document.searchQuotationform.standardChargeIndex.value = index;
    showPopUp();
    window.scrollTo(0, 0);
    var isIE5Min = getIEVersionNumber() <= 8;
    if (isIE5Min) {
        document.getElementById("outOfGaugeCommentDiv").style.position = 'absolute';
        document.getElementById("outOfGaugeCommentDiv").style.left = '40%';
        document.getElementById("outOfGaugeCommentDiv").style.top = '30px'
    } else {
        document.getElementById("outOfGaugeCommentDiv").style.position = 'fixed';
        document.getElementById("outOfGaugeCommentDiv").style.left = '40%';
        document.getElementById("outOfGaugeCommentDiv").style.top = '30px';
    }
    document.getElementById("outOfGaugeCommentDiv").style.display = 'block';
    //    var IpopTop = (screen.height - document.getElementById("outOfGaugeCommentDiv").offsetHeight)/2;
    //    var IpopLeft = (screen.width - document.getElementById("outOfGaugeCommentDiv").offsetWidth)/2;
    //    document.getElementById("outOfGaugeCommentDiv").style.left=IpopLeft + document.body.scrollLeft-70;
    //    document.getElementById("outOfGaugeCommentDiv").style.top=IpopTop + document.body.scrollTop-100;
    document.getElementById("outOfGaugeComment").value = comment.replace(/<br>/g, "\n");
}
function checkGaugeComment(mode, unit, obj) {
    if (obj.value == 'Y') {
        document.getElementById(mode + unit).style.visibility = "visible";
    } else {
        document.getElementById(mode + unit).style.visibility = "hidden";
    }

}
function submitOutOfGuageComments() {
    var comment = document.getElementById("outOfGaugeComment").value;
    document.searchQuotationform.outOfGuageComment.value = comment;
    document.searchQuotationform.buttonValue.value = "addSpecialEquipmentComment";
    document.searchQuotationform.submit();

}
function watchTextarea(id) {
    document.getElementById(id).onkeyup()
}
function limitTextarea(textarea, maxLines, maxChar) {
    var lines = textarea.value.replace(/\r/g, '').split('\n'),
            lines_removed,
            char_removed,
            i;
    if (maxLines && lines.length > maxLines) {
        alertNew('You can not enter\nmore than ' + maxLines + ' lines');
        lines = lines.slice(0, maxLines);
        lines_removed = 1
    }
    if (maxChar) {
        i = lines.length;
        while (i-- > 0)
            if (lines[i].length > maxChar) {
                lines[i] = lines[i].slice(0, maxChar);
                char_removed = 1;
            }
        if (char_removed)
            alertNew('You can not enter more\nthan ' + maxChar + ' characters per line');
    }
    if (char_removed || lines_removed)
        textarea.value = lines.join('\n');
}
function revertPageToEditMode(form) {
    var element;
    for (var i = 0; i < form.elements.length; i++) {
        element = form.elements[i];
        if (element.type == "checkbox" || element.type == "radio") {
            element.disabled = false;
            // element.style.border='1px solid #C4C5C4';
        }

    }
}
function hideArInvoice(fileNo) {
    GB_hide();
    makeARInvoiceButtonGreen(fileNo,"");

}
function makeARInvoiceButtonGreen(fileNo) {
    if (null !== fileNo && fileNo !== "" && document.getElementById("arRedInvoice")) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "getARInvoiceForThisBL",
                param1: fileNo,
                param2: ""
            },
            success: function(data) {
                if (data === "ARINVOICE") {
                    document.getElementById("arRedInvoice").className = "buttonColor";
                    document.getElementById("arRedInvoiceDown").className = "buttonColor";
                } else {
                    document.getElementById("arRedInvoice").className = "buttonStyleNew";
                    document.getElementById("arRedInvoiceDown").className = "buttonStyleNew";
                }
            }
        });
    }
}
function getEmptyPickUp(quoteId, hazmat, fileNo, emptyPickUp) {
        GB_show('Inland Quote', rootPath + '/rateGrid.do?action=ImsQuote&originZip=' + document.searchQuotationform.zip.value + '&doorOrigin='
                + document.searchQuotationform.doorOrigin.value + '&quoteId=' + quoteId + '&hazardous=' + hazmat + '&screenName=quotation&fileNo=' + fileNo + '&emptyPickUp=' + emptyPickUp, 350, 800);
    }

function getCreditStatus(warningType) {
    var accountNumber = document.getElementById("clientNumber").value;
    if (trim(accountNumber) != '') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getCreditStatus",
                param1: accountNumber
            },
            success: function(data) {
                if (data === "Suspended/See Accounting") {
                    if (warningType === "alert") {
                        alertNew("SUSPENDED CREDIT");
                    } else if (warningType === "warning") {
                        confirmSuspendedCreditConvertToBookings("Client has Suspended Credit.  Please review file before proceeding.");
                    }
                    document.getElementById("creditStatusCol").style.display = "block";
                    document.getElementById("creditStatus").innerHTML = "SUSPENDED CREDIT";
                } else {
                    document.getElementById("creditStatusCol").style.display = "none";
                    document.getElementById("creditStatus").innerHTML = "";
                    if (warningType === "warning") {
                        document.searchQuotationform.submit();
                    }
                }
            }
        });
    } else {
        document.getElementById("creditStatusCol").style.display = "none";
        document.getElementById("creditStatus").innerHTML = "";
        if (warningType == "warning") {
            document.searchQuotationform.submit();
        }
    }
}

function confirmSuspendedCreditConvertToBookings(text) {
    var id = "ConfirmBoxForSuspendedCredit";
    var left = 100;
    var top = 50;
    var point = window.center({
        width: 100,
        height: 100
    });
    document.getElementById(id).style.left = left + "px";
    document.getElementById(id).style.top = top + "px";
    document.getElementById("confirmBoxForSuspendedCreditText").innerHTML = text;
    document.getElementById(id).style.display = "block";
    document.getElementById(id).style.left = point.x - 100 + "px";
    document.getElementById(id).style.top = point.y + "px";
    document.getElementById(id).style.zIndex = "1000";
    grayOut(true, "");
}

function proceedConvertToBookings() {
    document.getElementById("ConfirmBoxForSuspendedCredit").style.display = "none";
    grayOut(false, "");
    showCommentsBoxForSuspendedCredit();
}

function cancelConvertToBookings() {
    document.getElementById("ConfirmBoxForSuspendedCredit").style.display = "none";
    grayOut(false, "");
}

function showCommentsBoxForSuspendedCredit() {
    var id = "CommentsBoxForSuspendedCredit";
    var left = 100;
    var top = 50;
    var point = window.center({
        width: 100,
        height: 100
    });
    document.getElementById(id).style.left = left + "px";
    document.getElementById(id).style.top = top + "px";
    document.getElementById(id).style.display = "block";
    document.getElementById(id).style.left = point.x - 100 + "px";
    document.getElementById(id).style.top = point.y + "px";
    document.getElementById(id).style.zIndex = "1000";
    grayOut(true, "");
}

function saveCommentsAndConvertToBookings() {
    document.getElementById("CommentsBoxForSuspendedCredit").style.display = "none";
    grayOut(false, "");
    var fileNo = document.getElementById("moduleRefId").value;
    var comments = document.getElementById("suspendedCreditComments").value;
    if (trim(comments) != "") {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "createSuspendedCreditNotes",
                param1: fileNo,
                param2: comments,
                request: "true"
            },
            success: function(data) {
                document.searchQuotationform.submit();
            }
        });
    } else {
        var id = "AlertBoxForSuspendedCredit";
        var left = 100;
        var top = 50;
        var point = window.center({
            width: 100,
            height: 100
        });
        document.getElementById(id).style.left = left + "px";
        document.getElementById(id).style.top = top + "px";
        document.getElementById("alertBoxForSuspendedCreditText").innerHTML = "Please enter comments";
        document.getElementById(id).style.display = "block";
        document.getElementById(id).style.left = point.x - 100 + "px";
        document.getElementById(id).style.top = point.y + "px";
        document.getElementById(id).style.zIndex = "1000";
        grayOut(true, "");
    }
}
function numericTextbox(text) {
    var dateval = text.value
    if (dateval != "") {
        if ((!isNaN(parseFloat(dateval)) && isFinite(dateval)) == false) {
            alertNew("Please Enter Numeric Value For Transit Days");
            document.getElementById("noOfDays").value = "";
            document.getElementById("noOfDays").focus();
            return;
        }
    }
}
function cancelCommentsAndConvertToBookings() {
    document.getElementById("CommentsBoxForSuspendedCredit").style.display = "none";
    grayOut(false, "");
}
function convertToArrivalNotice(quoteId, quoteBy, quoteDate) {
    if (validateConvertToArrivalNotice() == true) {
        confirmNew("Are you sure you want to convert this Quote to Arrival notice?\n" +
                "Created By--> " + quoteBy + " on " + quoteDate, "convertToArrivalNotice");
    }
}
function validateConvertToArrivalNotice() {
    var selectedUnitValues = [];
    if (!jQuery("#placeofReceipt").val()) {
        alertNew("Please Enter POL");
        return false;
    }
    if (!jQuery("#finalDestination").val()) {
        alertNew("Please Enter POD");
        return false;
    }
    if ((!jQuery('#routedAgentCheck').val()) && ((jQuery("#nonRated").is(":checked") == false)
            && (jQuery("#directConsignmentN").attr("checked", true)))) {
        alertNew("Please Select ERT");
        return false;
    }
    if (jQuery("#deliveryChargeComments").val() == "" && checkDeliveryRates() == 'false'
            && jQuery("#doorDestination").val() != "") {
        var msg = "Delivery Charge Cost is required when doing door to door move. Choose Yes to continue or choose No to acknowledge and skip past this requirement";
        confirmYesOrNo(msg, "deliveryCharge");
        return false;
    }
    var nvoMove = jQuery("#typeofMove").val();
    if (nvoMove == "DOOR TO DOOR" || nvoMove == "DOOR TO PORT" || nvoMove == "DOOR TO RAIL" || nvoMove == "RAMP TO PORT") {
        var originZip = jQuery("#zip").val();
        if (undefined == originZip || originZip == '') {
            alertNew("Please Enter Origin Zip");
            return false;
        } else if ((jQuery("#inland").is(":checked") &&
                jQuery("#intermodelComments").val())) {
            if (jQuery("#rampCheck").is(":checked")) {
                confirmYesOrNo("Intermodal Ramp Cost is required when doing door move. Choose Yes to continue or choose No to acknowledge and skip past this requirement", "inland");
            } else {
                confirmYesOrNo("Inland Cost is required when doing door move. Choose Yes to continue or choose No to acknowledge and skip past this requirement", "inland");
            }
            return false;
        }
    }
    if (!jQuery("#sslDescription").val()) {
        alertNew("Please Enter SSL Name");
        return false;
    }
    jQuery(".unitSelect").each(function() {
        if (jQuery(this).is(":checked") == true) {
            selectedUnitValues.push(jQuery(this).val());
        }
    });
    document.searchQuotationform.unitSizeSelected.value = selectedUnitValues;
    if (selectedUnitValues == "" && jQuery("#breakBulk1").is(":checked") == false) {
        alertNew("Please select at least one Unit Size before converting to Arrival Notice");
        return false;
    }
    if (jQuery('#hazmatYes').is(":checked") && checkCharges(HAZARDOUS) === false) {
        confirmYesOrNo("File is hazardous but does not have hazardous surcharges," +
                " Do you want to continue to convert to arrival notice?", "hazChargeArrivalNotice");
        return;
    }
    return true;
}
function proceedConvertToArrivalNotice() {
    document.searchQuotationform.buttonValue.value = "convertToArrivalNotice";
    document.searchQuotationform.submit();
}

jQuery(document).ready(function() {
    window.parent.closePreloading();
});
function checkCarrier() {
    if (carrierPrint == "on") {
        document.searchQuotationform.carrierPrint[0].checked = true;
    } else {
        document.searchQuotationform.carrierPrint[1].checked = true;
    }
}
function checkDiscloser(ratesStatus, importantDisclosures) {
    var ratsStatus = ratesStatus;
    var importantDisclosurs = importantDisclosures;
    if (ratsStatus == 'N') {
        if (importantDisclosurs == "on") {
            document.searchQuotationform.importantDisclosures[0].checked = true;
        } else {
            document.searchQuotationform.importantDisclosures[1].checked = true;
        }
    }
}
function copyNewQuote() {
    confirmYesOrNo("Do you want to copy the quote", "copyQuote");
}
function getTypeofMoveList(typeofMoveSelect, quoteId) {
    if (document.getElementById("rampCheck").checked) {
        document.getElementById("inlandVal").innerHTML = "Intermodal Ramp";
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getrampNvoMoveList",
                dataType: "json"
            },
            success: function(data) {
                jQuery(typeofMoveSelect).empty();
                jQuery.each(data, function(index, item) {
                    jQuery(typeofMoveSelect).append("<option value='" + item.value + "'>" + item.label + "</option>");
                });
                selectNVOmove();
            }
        });
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "intrmodRampCheck",
                param1: quoteId
            },
            success: function(data) {
                if (data === "Y") {
                    document.searchQuotationform.inland[1].checked = false;
                    document.searchQuotationform.inland[0].checked = true;
                } else {
                    document.searchQuotationform.inland[0].checked = false;
                    document.searchQuotationform.inland[1].checked = true;
                }
            }
        });
    } else {
        var importFlag = document.getElementById("importFlag").value;
        if (importFlag === 'true') {
            document.getElementById("inlandVal").innerHTML = "Delivery";
        } else {
            document.getElementById("inlandVal").innerHTML = "Inland";
        }
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getNvoMoveList",
                dataType: "json"
            },
            success: function(data) {
                jQuery(typeofMoveSelect).empty();
                jQuery.each(data, function(index, item) {
                    jQuery(typeofMoveSelect).append("<option value='" + item.value + "'>" + item.label + "</option>");
                });
                selectNVOmove();
            }
        });
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "inlandCheck",
                param1: quoteId
            },
            success: function(data) {
                if (data === "Y") {
                    document.searchQuotationform.inland[1].checked = false;
                    document.searchQuotationform.inland[0].checked = true;
                } else {
                    document.searchQuotationform.inland[0].checked = false;
                    document.searchQuotationform.inland[1].checked = true;
                }
            }
        });
    }
}
function newOriginEQ() {
    var path = "";
    var doorOrigin = document.getElementById('doorOrigin');
    if (document.getElementById('newOrigin').checked) {
        doorOrigin.value = "";
        hideOrgZip();
        path = "&from=5&isDojo=false&check=false";
        Event.stopObserving("doorOrigin", "blur");
    } else {
        doorOrigin.value = "";
        hideOrgZip();
        path = "&from=5&isDojo=false&check=true";
        Event.observe("doorOrigin", "blur", function(event) {
            var element = Event.element(event);
            if (element.value != $("doorOrigin_check").value) {
                element.value = '';
                $("doorOrigin_check").value = '';
                hideOrgZip();
            }
        }
        );
    }
    appendEncodeUrl(path);
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
        Event.observe("doorDestination", "blur", function(event) {
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
function checkTheDojoURL() {
    var path = "";
    if (document.searchQuotationform.zip.value != "" && !document.getElementById("rampCheck").checked) {
        var length = document.getElementById("typeofMoveSelect").length;
        for (var i = 0; i < length; i++) {
            if (document.getElementById("typeofMoveSelect").options[i].text == "DOOR TO DOOR") {
                document.getElementById("typeofMoveSelect").selectedIndex = i;
            }
        }

    } else if (document.searchQuotationform.zip.value == "") {
        length = document.getElementById("typeofMoveSelect").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("typeofMoveSelect").options[i].text == "PORT TO DOOR") {
                document.getElementById("typeofMoveSelect").selectedIndex = i;
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
function updateDoorOrigin(elementId) {
    document.getElementById('doorOrigin_check').value = getValue(elementId);
    document.getElementById('doorOrigin').value = getValue(elementId)
    if (document.getElementById('fileType') && document.getElementById('fileType').value == 'I') {
        document.getElementById('routedAgentCheck').focus();
    } else {
        document.getElementById("rampCheck").style.visibility = 'visible';
        document.getElementById('doorDestination').focus();
    }
    if (jQuery('#enableIMS').attr("checked")) {
        document.getElementById("imsQuoteImg").style.visibility = "visible";
    }
}
function hideImsQuotes(quoteId) {
    document.getElementById("imsQuoteImg").style.visibility = "hidden";
    document.getElementById("rampCheck").checked = false;
    document.getElementById("inlandVal").innerHTML = "Inland";
    document.getElementById("rampCheck").style.visibility = "hidden";
    if (document.getElementById("rampCheck").checked) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "intrmodRampCheck",
                param1: quoteId
            },
            success: function(data) {
                if (data === "Y") {
                    document.searchQuotationform.inland[1].checked = false;
                    document.searchQuotationform.inland[0].checked = true;
                } else {
                    document.searchQuotationform.inland[0].checked = false;
                    document.searchQuotationform.inland[1].checked = true;
                }
            }
        });
    } else {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "inlandCheck",
                param1: quoteId
            },
            success: function(data) {
                if (data === "Y") {
                    document.searchQuotationform.inland[1].checked = false;
                    document.searchQuotationform.inland[0].checked = true;
                } else {
                    document.searchQuotationform.inland[0].checked = false;
                    document.searchQuotationform.inland[1].checked = true;
                }
            }
        });
    }
}
function hideImsQuote() {
    document.getElementById("imsQuoteImg").style.visibility = "hidden";
    document.getElementById("rampCheck").checked = false;
    document.getElementById("inlandVal").innerHTML = "Delivery";
    document.getElementById("rampCheck").style.visibility = "hidden";
}
function displayShortCutCharges() {
    if (document.searchQuotationform.routedAgentCheck.value == "yes") {
        document.getElementById("shortCutCharge").style.visibility = "visible";
        document.getElementById("greenDollarUseTrueCostN").checked = true;
        document.getElementById("greenDollarUseTrueCostN").value = "N";
    } else {
        document.getElementById("shortCutCharge").style.visibility = "hidden";
    }
}
function quoteOptions() {
    showPopUp();
    document.getElementById("QuoteOPtions").style.display = "block";
    floatDiv2("QuoteOPtions", document.body.offsetWidth / 3, document.body.offsetHeight / 3).floatIt();
}
function modifyVisibilityDocumentCharge(ChosenValue) {
    if (ChosenValue == "Y") {
        document.getElementById("documentCharge").style.visibility = "visible";
    }
    else {
        document.getElementById("documentCharge").style.visibility = "hidden";
        document.getElementById("documentCharge").value = "";
    }
}
function toggleReduceOFR(ChosenValue) {
    if (ChosenValue == "Y") {
        document.getElementById("reducedOceanFreight").style.backgroundColor = '#CCEBFF';
        document.getElementById("reducedOceanFreight").disabled = true;
    } else {
        document.getElementById("reducedOceanFreight").disabled = false;
        document.getElementById("reducedOceanFreight").style.backgroundColor = '#FFFFFF';
    }

}
function shortCutCharges() {
    var val = checkIfRatesAreFromGetRates();
    if (val == 'true') {
        alertNew("only if rates are from getRates");
        return;
    } else {
        showPopUp();
        document.getElementById("chargesOptions").style.display = "block";
        document.getElementById("reducedOceanFreight").value = "0.00";
        document.getElementById("adminCharge").value = "0.00";
        if (document.getElementsByName("greenDollarDocCharge")[0].checked == true) {
            modifyVisibilityDocumentCharge("Y");
        }
        else {
            modifyVisibilityDocumentCharge("N");
        }
        floatDiv2("chargesOptions", document.body.offsetWidth / 3, document.body.offsetHeight / 3).floatIt();
    }
}
function submitQuoteOPtions() {
    closePopUp();
    document.getElementById("QuoteOPtions").style.display = "none";
}
function submitChargesOPtions() {
    if (document.getElementById("greenDollarDocChargeY").checked) {
        if (document.getElementById("documentCharge").value == "") {
            alertNew("Please Enter Document Charge");
            return;
        } else if (document.getElementById("documentCharge").value.match(/^-?[0-9]+(.[0-9]{1,2})?$/) == null) {
            document.getElementById("documentCharge").value = '';
            alertNew('The format is wrong');
            return;
        }
    }
    if (document.getElementById("documentCharge").value == "") {
        document.getElementById("documentCharge").value = "0.00";
    }
    if (document.getElementById("reducedOceanFreight").value == "") {
        document.getElementById("reducedOceanFreight").value = "0.00";
    }
    if (document.getElementById("adminCharge").value == "") {
        document.getElementById("adminCharge").value = "0.00";
    }
    document.searchQuotationform.buttonValue.value = "shortCutCharge";
    document.searchQuotationform.submit();
    closePopUp();
    document.getElementById("chargesOptions").style.display = "none";
}
function carrierOPition(obj) {
    document.searchQuotationform.carrierPrint.value = obj.value;
}
function disclosureOPition(obj) {
    document.searchQuotationform.importantDisclosures.value = obj.value;
}
function docsInquiriesOPition(obj) {
    document.searchQuotationform.docsInquiries.value = obj.value;
}
function printPortRemarksOPition(obj) {
    document.searchQuotationform.printPortRemarks.value = obj.value;
}
function closeDivs() {
    closePopUp();
    document.getElementById("QuoteOPtions").style.display = "none";
}
function closeChargesDivs() {
    closePopUp();
    document.getElementById("chargesOptions").style.display = "none";
}
function changeScanButtonColor(masterStatus, documentName,docList) {
    if(docList===0){
        document.getElementById("scanButton").className = "buttonStyleNew";
        document.getElementById("scanButtonDown").className = "buttonStyleNew";
    } else {
        document.getElementById("scanButton").className = "buttoncolor";
        document.getElementById("scanButtonDown").className = "buttoncolor";
    }
}
function onBlurForSSL() {
    document.getElementById('sslDescription').value = "";
    document.getElementById('sslcode').value = "";
    document.editQuote.carrierPhone.value = "";
    document.editQuote.carrierFax.value = "";
    document.getElementById('carrierContact').value = "";
    document.getElementById('carrierEmail').value = "";
    document.getElementById("contactNameButtonSecond").style.visibility = 'hidden';
}
function setFocusForBlurToWork() {
    if (event.keyCode == 9 || event.keyCode == 13 || event.keyCode == 0) {
        setTimeout("secondFocus()", 150);

    }
}

function secondFocus() {
    document.getElementById('isTerminal').focus();
    document.getElementById('isTerminal').select();
}
var value;
var obj;
var value1;
function setBlur(ev) {
    if (ev.readOnly) {
        value = ev.value;
        value1 = document.getElementById('sslcode').value;
        obj = ev;
        document.getElementById('carrierContact').select();
        document.getElementById('carrierContact').focus();
        setTimeout("setValue()", 150);
    }
}
function setValue() {
    obj.value = value;
    document.getElementById('sslcode').value = value1;
}

function checkprintPortRemark(ratesStatus) {
    var ratesStats = ratesStatus;
    if (ratesStats === 'N') {
        if (null != printPortRemarks && printPortRemarks == "on" && document.searchQuotationform.printPortRemarks[0]) {
            document.searchQuotationform.printPortRemarks[0].checked = true;
        } else if (document.searchQuotationform.printPortRemarks[1]) {
            document.searchQuotationform.printPortRemarks[1].checked = true;
        }
    }
}
function setDefaultRouteAgent(importFlag) {
    var importFlg = importFlag;
    var agentNo = document.searchQuotationform.agentNo.value;
    if (document.searchQuotationform.routedAgentCheck.value == "yes") {
        if (null != agentNo && agentNo != '') {
            document.searchQuotationform.routedbymsg.value = agentNo;
            document.getElementById("routedbymsg_check").value = agentNo;
            document.searchQuotationform.routedbymsg.className = "textlabelsBoldForTextBox";
            document.searchQuotationform.routedbymsg.readOnly = false;
            document.searchQuotationform.routedbymsg.tabIndex = 0;
            tabErt();
        } else {
            if (importFlg == false) {
                document.getElementById("routedAgentCheck").selectedIndex = 0;
                alertNew("You must first select an agent");
            }
        }
    } else {
        document.searchQuotationform.routedbymsg.value = "";
        document.searchQuotationform.routedbymsg.className = "textlabelsBoldForTextBox";
        document.searchQuotationform.routedbymsg.readOnly = false;
        tabErt();
    }
    displayShortCutCharges();
}
function getAdjustValue(fileNo) {
    var file = fileNo;
    var finalAdjustedValue = "";
    if (file != "") {
        var splitFileNo = file.split('-')[1];
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getAdjustValue",
                param1: splitFileNo
            },
            success: function(data) {
                if (null !== data && data !== "" && data !== "null") {
                    document.searchQuotationform.inland[1].checked = false;
                    finalAdjustedValue = "Adjusted value<br/>" + data;
                    document.searchQuotationform.ertAdjustedValue.value = finalAdjustedValue;
                } else {
                    document.searchQuotationform.ertAdjustedValue.value = "No Adjustment";
                }
            }
        });
    }
}
function goBackCall(view, finalValue, buttonVal) {
    var viewVal = view;
    var finalVal = finalValue;
    var buttonValue = buttonVal;
    if (document.searchQuotationform.buttonValue.value == "") {
        document.searchQuotationform.buttonValue.value = buttonValue;
    }
    if (document.searchQuotationform.buttonValue.value == 'newgetRates' || document.searchQuotationform.buttonValue.value == 'hazmat'
            || document.searchQuotationform.buttonValue.value == 'addCharges') {
        cancel1(viewVal, finalVal);
    } else {
        window.parent.showProgressBar();
        document.searchQuotationform.buttonValue.value = "previous";
        if (document.searchQuotationform.defaultAgent[0].checked) {
            document.searchQuotationform.defaultAgent[0].value = 'Y';
        } else {
            document.searchQuotationform.defaultAgent[1].value = 'N';
        }
        document.searchQuotationform.submit();
    }
}
function bulletRatesClick() {
    document.getElementById("commcode").value = "";
    document.getElementById("commcode").focus();
}
function bulletRatesStauts() {
    var path = "";
    if (document.getElementById("bulletRates") && document.getElementById("bulletRates").checked) {
        path = "&bulletRates=true";
    } else {
        path = "&bulletRates=false";
    }
    appendEncodeUrl(path);
}
function retainHazmat() {
    var quoteId = jQuery('#quotationNo').val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "isHazmat",
            param1: quoteId
        },
        success: function(data) {
            isHazmat(data);
        }
    });
}
function isHazmat(data) {
    if (data === "Y") {
        jQuery('#hazmatYes').attr("checked", true);
    } else {
        jQuery('#hazmatNo').attr("checked", true);
    }
}
function saveHazmat() {
    jQuery('#buttonValue').val('hazmat');
    document.searchQuotationform.submit();
}
function convertToBkg() {
    var containsJapan = jQuery('#portofDischarge').val();
    var contains = (containsJapan.indexOf('JAPAN') > 1);
    if (contains && importFlag == 'false') {
        reminderBox("Japan AFR must be filed on all Japan shipments");
    } else {
        toBkg();
    }
}
function toBkg() {
    var quoteBy = jQuery('#quoteBy').val();
    var quoteDate = jQuery('#quoteDate').val();
    confirmNew("Are you sure you want to convert this Quote to Booking?\n" +
            "Created By--> " + quoteBy + " on " + quoteDate, "convertToBookings");
}
function dontAddInsureToCharges() {
    if (collapseid == "") {
        getcollapse();
    }
    document.searchQuotationform.check1.value = collapsePrint;
    document.searchQuotationform.collapseid.value = collapseid;
    checkPrintInclude();
    document.searchQuotationform.buttonValue.value = "addChargesWithoutInsure";
    document.searchQuotationform.submit();
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
        success: function(data) {
            if (data) {
                jQuery('#clientIcon').attr("src", rootPath + "/img/icons/e_contents_view1.gif");
            } else {
                jQuery('#clientIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
            }
        }
    });
}
function allowInsurance() {
    var portname = document.getElementById('portofDischarge').value;
    var unloc = portname.substring(portname.lastIndexOf("(") + 1, portname.lastIndexOf(")"));
    var contains = (insuranceAllowed.indexOf(unloc) != -1);
    if (portname !== null && portname !== '') {
        if (contains) {
            document.getElementById('y8').disabled = true;
            document.getElementById('n8').checked = true;
            document.getElementById('n8').disabled = true;
        } else {
            document.getElementById('y8').disabled = false;
            document.getElementById('n8').disabled = false;
        }
    }
}
function checkBulkBreak() {
    var quoteId = jQuery("#quotationNo").val();
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkUnitType",
            param1: quoteId,
            dataType: "json"
        },
        success: function(data) {
            if (data) {
                document.getElementById("breakBulk1").disabled = true;
                document.getElementById("breakBulk2").disabled = true;
                document.getElementById("breakBulk2").checked = true;
            } else {
                document.getElementById("breakBulk1").disabled = false;
                document.getElementById("breakBulk2").disabled = false;
            }
        }
    });
}

function confirmVid(status, userName){
    if(jQuery("#moduleRefId").val()===''){
        alertNew("Please Save the File to Enable Spot/Bullet Rate Feature");
        jQuery("#spotRateN").attr('checked', 'checked');
        return;
    }
    if (status === "Y") {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName: "isContainDeferral",
                    param1: jQuery("#quotationNo").val(),
                    dataType: "json"
                },
                success: function (data) {
                    if (data === 'DEFERRAL') {
                        confirmYesOrNo("Did you confirm if VID's were included in the rate you secured from the Steamship Line ?","acceptVid");
                    } else {
                        spotMsg(status,userName);
                    }
                }
            });

        }else{
            spotMsg(status,userName);
        }
}
function spotMsg(status, userName) {
    if (importFlag == 'false') {
        if (status === "Y") {
            document.getElementById("spotRateMsgDiv").style.display = "block";
            document.getElementById("spotRateMsgStatus").innerHTML = "**SPOT/BULLET RATE**";
            document.getElementById("spotRateMsgDiv2").style.display = "block";
            document.getElementById("spotRateMsgStatus2").innerHTML = "**SPOT/BULLET RATE**";
        } else {
            document.getElementById("spotRateMsgDiv").style.display = "none";
            document.getElementById("spotRateMsgStatus").innerHTML = "";
            document.getElementById("spotRateMsgDiv2").style.display = "none";
            document.getElementById("spotRateMsgStatus2").innerHTML = "";
        }
        if (userName !== "") {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName: "notesForSpotRates",
                    param1: jQuery("#moduleRefId").val(),
                    param2: userName,
                    param3: status
                }
            });
        }
    }
}
function brandValueLogic(){
  var pod = document.QuotesForm.portofDischarge.value;
        
        if (pod.lastIndexOf("(") > -1 && pod.lastIndexOf(")") > -1) {
            var podNew = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
            if (importFlag === false) {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
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
          
}
}
    function setbrandvalueBasedONDestination(companyCode) {
    var clientname = document.getElementById('clientNumber').value;
    
    if (undefined !== clientname && null !== clientname && '' !== clientname) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "checkBrandForClient",
                param1: clientname
            },
            success: function (data) {
                
                if (data === "None") {
                    brandValueLogic();
                }
            }
        });

    } else if (undefined !== document.getElementById('portofDischarge') && null !== document.getElementById('portofDischarge')
            && '' !== document.getElementById('portofDischarge').value) {
        brandValueLogic();
    }
    }

function validateBrandFields(data) {
    if (data === "Ecu Worldwide") {
        
        document.getElementById('brandEcuworldwide').checked = true;
        document.getElementById('brandEcono').checked = false;
    } else if (data === "Econo") {
        document.getElementById('brandEcono').checked = true;
        document.getElementById('brandEcuworldwide').checked = false;
    } else if(data === "OTI"){
       document.getElementById('brandOti').checked = true;
        document.getElementById('brandEcuworldwide').checked = false; 
    } 
    else if (data === "None") {
       var companyCode = document.getElementById('companyCode').value;
        setbrandvalueBasedONDestination(companyCode);
    }
}
function addBrandvalueForanAccount(acctno) {
   
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "checkBrandForClient",
                param1: acctno
            },
            success: function (data) {
             validateBrandFields(data);
            }
        });

    
}
function checkBrand(val, quoteid, companyCode) {
  

    if (val === "Econo" && companyCode === '03') {
      
        checkpreviousBrandValueEco03(quoteid, val);
    } else if (val === "OTI" && companyCode == '02') {
        checkpreviousBrandValueOti02(quoteid, val);
    } else if (val === "Ecu Worldwide" && companyCode === '03') {
        
        checkpreviousBrandValueEcu03(quoteid, val);
    } else if (val === "Ecu Worldwide" && companyCode === '02') {
        checkpreviousBrandValueEcu03(quoteid, val);
    }

}
function checkpreviousBrandValueEcu03(quoteid, val) {

    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkBrandForQuote",
            param1: quoteid
        },
        success: function (data) {
            if (null !== data && data !== val) {
              
                confirmYesOrNo("Please note that the Brand is changing from " + data + " to " + val + "", "Ecu Worldwide/"+data);

            }
        }
    });

}
function checkpreviousBrandValueOti02(quoteid, val) {

    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkBrandForQuote",
            param1: quoteid
        },
        success: function (data) {
            if (null !== data && data !== val) {
                
                confirmYesOrNo("Please note that the Brand is changing from " + data + " to " + val + "", "OTI/"+data);

            }
        }
    });

}
function checkpreviousBrandValueEco03(quoteid, val) {

    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkBrandForQuote",
            param1: quoteid
        },
        success: function (data) {
            if (null !== data && data !== val) {
                
                confirmYesOrNo("Please note that the Brand is changing from " + data + " to " + val + "", "Econo/"+data);

            }
        }
    });

}

function showbrandValue(quoteid) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkBrandForQuote",
            param1: quoteid
        },
        success: function (data) {
           
            if (data === 'Ecu Worldwide') {
                document.getElementById('brandEcuworldwide').checked = true;
                document.getElementById('brandEcono').checked = false;
            } else if (data === 'Econo') {
                document.getElementById('brandEcono').checked = true;
                document.getElementById('brandEcuworldwide').checked = false;
            } else if (data === 'OTI') {
                document.getElementById('brandOti').checked = true;
                document.getElementById('brandEcuworldwide').checked = false;
            }
        }
    });
}
function getChasisCharge(quoteId) {
    var value;
    if (collapseid == "") {
        getcollapse();
    }
    document.searchQuotationform.check1.value = collapsePrint;
    document.searchQuotationform.collapseid.value = collapseid;
    checkPrintInclude();
    GB_show("Chassis Charge Rate", "/logisoft/jsps/fclQuotes/calculateChassisCharge.jsp?vendorName=" + document.searchQuotationform.vendorName + "&vendorNumber" + document.searchQuotationform.accountNo + "&amount=" + document.searchQuotationform.amount + "&markup=" + document.searchQuotationform.markUp, width = "200", height = "700");
}

function calculateChassisCharge(cost, sell, vendorName, VendorNo) {
  
    document.getElementById("vendorName").value = vendorName;
    document.getElementById("accountNo").value = VendorNo;
    document.getElementById("amount").value = cost;
    document.getElementById("markUp").value = sell;
 
   document.searchQuotationform.buttonValue.value = "addChassisCharge";
   document.searchQuotationform.submit();
    
}
function deleteChassis(){
 confirmYesOrNo("Are you sure you want to remove CHASSIS from this file?", "deleteChassisCharge");   
}