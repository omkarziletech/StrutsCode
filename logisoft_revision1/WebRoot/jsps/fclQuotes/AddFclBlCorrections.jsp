<%@page import="com.logiware.hibernate.dao.FclBlDAO"%>
<%@ page language="java" import="java.util.*,com.gp.cvst.logisoft.domain.*,
         com.gp.cong.logisoft.util.DBUtil"%>
<jsp:directive.page import="com.gp.cong.logisoft.bc.fcl.FclBlConstants"/>
<jsp:directive.page import="com.gp.cong.logisoft.bc.notes.NotesConstants"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@include file="../includes/jspVariables.jsp"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<bean:define id="defaultAgent" type="String">
    <bean:message key="defaultAgent"/>
</bean:define>
<%    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String message = "";
    String sendTo = "";
    String comment = "";
    String faeIncentFlag = "";
    Double latestamt = 0.0;
    if (request.getAttribute("msg") != null) {
        message = (String) request.getAttribute("msg");
    }
    if (request.getAttribute("fclBl") != null) {
        FclBl fclbl = (FclBl) request.getAttribute("fclBl");
        faeIncentFlag = new FclBlDAO().getBilltoForIncent(fclbl.getBol(), "INCENT");
    }
    request.setAttribute("faeIncentFlag", faeIncentFlag);
    if (request.getAttribute("FclBlCorrection") != null) {
        FclBlCorrections f = (FclBlCorrections) request.getAttribute("FclBlCorrection");

        if (f.getSendCopyTo() != null) {
            sendTo = f.getSendCopyTo();
            sendTo = sendTo.replaceAll("[\r\n]", "\t");
        }
        if (f.getComments() != null) {
            comment = f.getComments();
            comment = comment.replaceAll("[\r\n]", "\t");
        }
    }
%>
<html>
    <head>
        <title>Add FCL BL Corrections</title>
        <%@include file="../includes/baseResources.jsp"%>
        <script language="javascript" src="<%=path%>/js/common.js"></script>
        <script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/ajax.js"></script>
        <script language="javascript" type="text/javascript">
            var correctionCode = '${FclBlCorrection.correctionCode.id}';
            var sendToCopy = '<%=sendTo%>';
            var correctionType = '${FclBlCorrection.correctionType.id}';
            var view = '${FclBlCorrectionForm.viewMode}';
            var faeIncentFlag = '${faeIncentFlag}';

            function load() {
                var code = '';
                var choice = document.fclBlCorrectionsForm.correctionType.selectedIndex;
                var blNumber = jQuery("#blNumber").val();
                var noticeNo = document.fclBlCorrectionsForm.noticeNo.value;
                var cnType = "";
                var newAcctNo = "";
                var differnceAmt = "";
                if (document.fclBlCorrectionsForm.correctionType[choice] !== undefined) {
                    var codeType = document.fclBlCorrectionsForm.correctionType[choice].text;
                    var codetypeArray = codeType.split("-");
                    if (codetypeArray.length !== undefined) {
                        code = codetypeArray[0];
                    }
                }
                displayAccountBlock();
                if (code === 'A' || code === 'Y') {
                    document.getElementById("profitAfterCn").style.display = "block";
                    document.getElementById("displayForCTypeA").style.visibility = "visible";
                    if (null !== document.getElementById("chargesTable")) {
                        document.getElementById("chargesTable").style.visibility = "visible";
                    }

                } else {
                    document.getElementById("profitAfterCn").style.display = "none";
                    document.getElementById("displayForCTypeA").style.visibility = "hidden";
                    if (null !== document.getElementById("chargesTable")) {
                        document.getElementById("chargesTable").style.visibility = "hidden";
                    }

                }
                if (noticeNo !== undefined && noticeNo !== "") {
                    addMemoEmailSection(noticeNo, blNumber, cnType, newAcctNo, differnceAmt, view);
                } else {
                    addMemoEmailSection("", blNumber, cnType, newAcctNo, differnceAmt, view);
                }
            }
            function selectBlNumber() {
                if (document.fclBlCorrectionsForm.blNumber.value === "") {
                    alertNew("Please Select BlNumber First");
                    document.fclBlCorrectionsForm.blNumber.focus();
                    return;
                }
            }

            function postQuickCn() {
                var errorMessage = '';
                if (document.getElementById("accountBlock").style.display === "block" && document.fclBlCorrectionsForm.accountName !== undefined && document.fclBlCorrectionsForm.accountNumber !== undefined) {
                    if (document.fclBlCorrectionsForm.accountName.value === '' || document.fclBlCorrectionsForm.accountNumber.value === '') {
                        errorMessage += "-->Please Enter Account Name and Number<br>";
                    }
                }
                if (document.fclBlCorrectionsForm.correctionCode.value === "0") {
                    errorMessage += "-->Please enter Correction Code<br>";
                }
                if (document.fclBlCorrectionsForm.comments.value === "") {
                    errorMessage += "-->Please enter Comments<br>";
                }
                if (document.fclBlCorrectionsForm.correctionType.value === "0") {
                    errorMessage += "-->Please enter Correction Type<br>";
                    // document.fclBlCorrectionsForm.blNumber.focus();
                }
                if (errorMessage !== '') {
                    alertNew(errorMessage);
                    return false;
                } else {
                    if (document.getElementById("deleteImg") === null) {
                        alertNew('Plese Enter New Amount to Post Quick CN');
                    } else {
                        confirmNew("The Quick Correction Notice will be Posted Immediately, are you sure you want to continue?", "savePostQuickCn");
                    }
                }
            }

            function savePostQuickCn() {
                createNotesWhileSaving();
                document.fclBlCorrectionsForm.buttonValue.value = "postQuickCn";
                document.fclBlCorrectionsForm.submit();
            }

            function saveMainCorrections() {
                var errorMessage = '';
                var tempErrMsg = '';
                if (document.getElementById("importFlag").value === 'I') {
                    document.fclBlCorrectionsForm.quickCn.value = true;
                }
                if (document.getElementById("accountBlock").style.display === "block" && document.fclBlCorrectionsForm.accountName !== undefined && document.fclBlCorrectionsForm.accountNumber !== undefined) {
                    if (document.fclBlCorrectionsForm.accountName.value === '' || document.fclBlCorrectionsForm.accountNumber.value === '') {
                        errorMessage += "-->Please Enter Account Name and Number<br>";
                    }
                }
                if (document.fclBlCorrectionsForm.correctionCode.value === "0") {
                    errorMessage += "-->Please enter Correction Code<br>";
                }
                if (document.fclBlCorrectionsForm.comments.value === "") {
                    errorMessage += "-->Please enter Comments<br>";
                }
                if (document.fclBlCorrectionsForm.correctionType.value === "0") {
                    errorMessage += "-->Please enter Correction Type<br>";
                }
                if (document.fclBlCorrectionsForm.comments.value.length > 1000) {
                    errorMessage += "-->Please do not enter more than 500 characters in Comments<br>";
                }
                tempErrMsg = getMemoEmailForMainCorrection();
                if (tempErrMsg) {
                    errorMessage += tempErrMsg;
                }
                if (errorMessage !== '') {
                    alertNew(errorMessage);
                    return false;
                } else {
                    var choice = document.fclBlCorrectionsForm.correctionType.selectedIndex;
                    if (document.fclBlCorrectionsForm.correctionType[choice] !== undefined) {
                        var codeType = document.fclBlCorrectionsForm.correctionType[choice].text;
                        var codetypeArray = codeType.split("-");
                        if (codetypeArray.length !== undefined) {
                            var code = codetypeArray[0];
                            if (code === 'A') {
                                if (document.getElementById("deleteImg") === null) {
                                    alertNew('Plese Enter New Amount to Save Correction type A');

                                } else {
                                    if (document.getElementById("saveMainCorrection") && document.getElementById("saveMainCorrection").style.display !== "none") {
                                        document.getElementById("saveMainCorrection").style.display = "none";
                                        createNotesWhileSaving();
                                        document.fclBlCorrectionsForm.buttonValue.value = "saveMainCorrection";
                                        document.fclBlCorrectionsForm.submit();
                                    }
                                }
                            } else {
                                if (document.getElementById("saveMainCorrection") && document.getElementById("saveMainCorrection").style.display !== "none") {
                                    document.getElementById("saveMainCorrection").style.display = "none";
                                    createNotesWhileSaving();
                                    document.fclBlCorrectionsForm.buttonValue.value = "saveMainCorrection";
                                    document.fclBlCorrectionsForm.submit();
                                }
                            }
                        }
                    }
                }
            }

            function createNotesWhileSaving() {
                if (document.fclBlCorrectionsForm.noticeNo.value === '') {
                    document.getElementById('eventCode').value = '100016';
                    var blNumber = document.getElementById("blNumber").value;
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.fcl.FclBlCorrectionsBC",
                            methodName: "getNoticeNumber",
                            param1: blNumber
                        },
                        async: false,
                        success: function(data) {
                            if (data !== 0) {
                                blNumber = "(" + blNumber + "-" + data + ")  saved";
                                document.getElementById('eventDesc').value = blNumber;
                            }
                        }
                    });
                } else {
                    document.getElementById('eventCode').value = '';
                    document.getElementById('eventCode').value = '';
                }
            }

            function CalculateDifference(temp) {
                if (temp === 'save') {
                    var amount = document.fclBlCorrectionsForm.amount.value;
                    var newAmount = document.fclBlCorrectionsForm.newAmount.value;
                    newAmount = newAmount.replace(',', '');
                    amount = amount.replace(',', '');
                    newAmount = Number(newAmount).toFixed(2);
                    var difference = newAmount - amount;
                    if (difference === 0) {
                        alertNew("Old Amount and New Amount Cannot be same");
                        return false;
                    } else {
                        difference = Number(difference).toFixed(2);
                        fclBlCorrectionsForm.differenAmount.value = difference;
                        document.fclBlCorrectionsForm.newAmount.value = newAmount;
                    }
                }
            }
            function SaveCorrections() {
                var fileNo =document.fclBlCorrectionsForm.fileNo.value;
                var chargeCode =document.fclBlCorrectionsForm.chargeCode.value;
                if (document.fclBlCorrectionsForm.correctionType.value === "0") {
                    alertNew("Please enter CorrectionType");
                    document.fclBlCorrectionsForm.blNumber.focus();
                    return false;
                }
                if (document.fclBlCorrectionsForm.chargeCode.value === '') {
                    alertNew("Please enter ChargeCode ");
                    document.fclBlCorrectionsForm.chargeDescription.focus();
                    return false;
                }
                var rowCntr = document.getElementById('rowCnt').value;
                var i;
                var chargeCode;
                var billTo;
                var tableChgCode = document.fclBlCorrectionsForm.chargeCode.value.toString().trim();
                var tableBillToParty = document.fclBlCorrectionsForm.billTo.value.toString().trim();
                var correctionType = getCorrectionType();
                if (document.getElementById('chargesTable')) {
                    for (i = 0; i < document.getElementById('chargesTable').rows.length; i++) {
                        var rowElements = document.getElementById('chargesTable').rows[i].cells;
                        chargeCode = rowElements[1].innerHTML.toString().trim();
                        billTo = rowElements[3].innerHTML.toString().trim();
                        if ((tableChgCode === chargeCode && (correctionType === 'A' || correctionType === 'Y') && tableBillToParty === billTo && rowCntr != i) ||
                                (correctionType !== 'Y' && correctionType !== 'A' && tableChgCode === chargeCode)) {
                            if (correctionType === 'Y' || correctionType === 'A') {
                                alertNew('Charge Already Exists. Please choose another bill to party.');
                            } else {
                                alertNew('Charge Already Exists. Please edit the charge to make changes');
                            }
                            return false;
                        }

                    }
                }
                if (document.fclBlCorrectionsForm.billTo.disabled) {
                    document.fclBlCorrectionsForm.billTo.disabled = false;
                }
                var blNumber = document.getElementById("blNumber").value;
                var noticeNo = document.fclBlCorrectionsForm.noticeNo.value;
                var correctionType = document.fclBlCorrectionsForm.originalBillToPartyCorrectionTypeY.value;
                var billTo = document.fclBlCorrectionsForm.billTo.value;
                if (document.fclBlCorrectionsForm.newAmount.value === '') {
                    alertNew("Please enter New Amount");
                    document.fclBlCorrectionsForm.newAmount.focus();
                    return false;
                }
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlCorrectionsBC",
                        methodName: "checkBillToParty",
                        param1: blNumber,
                        param2: billTo,
                        dataType: "json"
                    },
                    success: function(data) {
                        if (!data) {
                            alertNew("There is no " + billTo + " Name  for updated BL ");
                            return false;
                        } else {
                            if (CalculateDifference('save') !== false) {
                                createNotesWhileSaving();
                                var cnType = getCorrectionType();
                                var differnceAmt = fclBlCorrectionsForm.differenAmount.value;
                                var newAcctNo = "";
                                if (differnceAmt > 0) {
                                    jQuery('#debitMemoEmail').val(jQuery('#hiddenEmailIds').val());
                                } else {
                                    jQuery('#creditMemoEmail').val(jQuery('#hiddenEmailIds').val());
                                }
                                addMemoEmailSection(noticeNo, blNumber, cnType, newAcctNo, differnceAmt, view);
                                if(checkAddCorrectionChargeMappingWithGL(chargeCode, fileNo)) {
                                document.fclBlCorrectionsForm.buttonValue.value = "save";
                                document.getElementById('saveCorrections').style.display = "none";
                                document.fclBlCorrectionsForm.submit();
                                }
                            }
                        }
                    }
                });
            }

            function addMemoEmailSection(noticeNo, blNumber, cnType, newAcctNo, differnceAmt, viewMode) {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlCorrectionsBC",
                        methodName: "getMemoEmailIds",
                        forward: "/jsps/fclQuotes/memoEmailSection.jsp",
                        param1: blNumber,
                        param2: noticeNo,
                        param3: cnType,
                        param4: newAcctNo,
                        param5: differnceAmt,
                        param6: viewMode
                    },
                    success: function(data) {
                        jQuery("#memoDivSection").html(data);
                        if (viewMode === "view") {
                            jQuery("#memoDivSection").find(".creditEmailId, .debitEmailId").attr("disabled", true);
                            jQuery("#creditMemoEmailDiv").attr("class", "divstyleThinDisabled");
                            jQuery("#debitMemoEmailDiv").attr("class", "divstyleThinDisabled");
                        }
                    }
                });
            }

            function getCorrectionType() {
                var correctionCodeAorY = "";
                var choice = document.fclBlCorrectionsForm.correctionType.selectedIndex;
                if (document.fclBlCorrectionsForm.correctionType[choice] !== undefined) {
                    var codeType = document.fclBlCorrectionsForm.correctionType[choice].text;
                    var codetypeArray = codeType.split("-");
                    if (codetypeArray.length !== undefined) {
                        correctionCodeAorY = codetypeArray[0];
                    }
                }
                return correctionCodeAorY;
            }
            function getEmailIds(acctNumber) {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlCorrectionsBC",
                        methodName: "getEmailIds",
                        param1: acctNumber
                    },
                    success: function(data) {
                        jQuery('#hiddenEmailIds').val(data);
                    }
                });

            }
            function getMemoEmailForMainCorrection() {
                var creditEmail = [];
                var debitEmail = [];
                var totalCreditEmailCount = 0;
                var totalDebitEmailCount = 0;
                var errMsg = '';
                jQuery(".creditEmailId").each(function() {
                    totalCreditEmailCount++;
                    if (jQuery(this).is(":checked") === true) {
                        creditEmail.push(jQuery(this).val());
                    }
                });
                jQuery(".debitEmailId").each(function() {
                    totalDebitEmailCount++;
                    if (jQuery(this).is(":checked") === true) {
                        debitEmail.push(jQuery(this).val());
                    }
                });
                jQuery('#creditMemoEmail').val(creditEmail);
                jQuery('#debitMemoEmail').val(debitEmail);
                if (totalCreditEmailCount > 0 && creditEmail.length <= 0) {
                    errMsg += "-->Please choose atleast one credit email<br>";
                }
                if (totalDebitEmailCount > 0 && debitEmail.length <= 0) {
                    errMsg += "-->Please choose atleast one debit email<br>";
                }
                return errMsg;
            }
            function goBackCall() {
                document.getElementById('eventCode').value = '';
                document.getElementById('eventDesc').value = '';
                if (!document.fclBlCorrectionsForm.temp.value === "") {
                    document.fclBlCorrectionsForm.buttonValue.value = "goBack";
                    document.fclBlCorrectionsForm.submit();
                } else {
                    document.fclBlCorrectionsForm.buttonValue.value = "goBack";
                    document.fclBlCorrectionsForm.submit();
                }
            }
            function confirmForSave() {
                document.getElementById("confirmNo").style.width = "88" + "px";
                document.getElementById("confirmNo").value = "Exit without Save";
                confirmNew("Do You want to save the Correction?", "goBack");
            }
            function GoBack() {
                if (document.fclBlCorrectionsForm.noticeNo.value === '') {
                    confirmForSave();
                } else {
                    var comments = document.fclBlCorrectionsForm.oldComment.value;
                    var booleabFlag = false;
                    var currentComments = document.fclBlCorrectionsForm.comments.value;
                    if (comments !== currentComments) {
                        booleabFlag = true;
                    }
                    if (correctionCode !== document.fclBlCorrectionsForm.correctionCode.value) {
                        booleabFlag = true;
                    }
                    if (correctionType !== document.fclBlCorrectionsForm.correctionType.value) {
                        booleabFlag = true;
                    }
                    if (booleabFlag && view !== 'view') {
                        confirmForSave();
                    } else {
                        goBackCall();
                    }
                }
                if (document.getElementById("importFlag").value === 'I') {
                    document.fclBlCorrectionsForm.quickCn.value = true;
                }
            }
            function manifestBlCorrections() {
                document.fclBlCorrectionsForm.buttonValue.value = "manifestCorrections";
                document.fclBlCorrectionsForm.submit();
            }
            function UpdateCorrections(val1) {
                document.fclBlCorrectionsForm.id.value = val1;
                document.fclBlCorrectionsForm.buttonValue.value = "update";
                document.fclBlCorrectionsForm.submit();
            }
            function getChargesOfBl(ev) {
                if (event.keyCode === 9 || event.keyCode === 13) {
                    document.fclBlCorrectionsForm.buttonValue.value = "displayingCharges";
                    document.fclBlCorrectionsForm.submit();
                }
            }
            function displayAccountBlock() {
                var choice = document.fclBlCorrectionsForm.correctionType.selectedIndex;
                if (document.fclBlCorrectionsForm.correctionType[choice] !== undefined) {
                    var codeType = document.fclBlCorrectionsForm.correctionType[choice].text;
                    var codetypeArray = codeType.split("-");
                    if (codetypeArray.length !== undefined) {
                        var code = codetypeArray[0];
                        if (code !== 'A' && code !== 'B' && code !== 'Y' && code !== 'F' && code !== 'C' & code !== 'D' && code !== 'G' & code !== 'I' && code !== 'H' && code !== 'Select Correction Type') {
                            document.getElementById("accountBlock").style.display = "block";
                        } else {
                            if (code === 'B' && parent.parent.document.getElementById("agentNo") &&
                                    parent.parent.document.getElementById("agentNo").value === "") {
                                alertNew("This BL doesn't have Agent please select different Correction Type");
                                document.fclBlCorrectionsForm.correctionType.selectedIndex = "0";
                            }
                            if ((code === 'F' || code === 'H' || code === 'D') && parent.parent.document.getElementById("forwardingAgent1")
                                    && parent.parent.document.getElementById("forwardingAgent1").value === "") {// forwarder
                                alertNew("This BL doesn't have Fowarder please select different Correction Type");
                                document.fclBlCorrectionsForm.correctionType.selectedIndex = "0";
                            }
                            if ((code === 'G' || code === 'I' || code === 'C') && parent.parent.document.getElementById("shipper")
                                    && parent.parent.document.getElementById("shipper").value === "") {// shipper
                                alertNew("This BL doesn't have Shipper please select different Correction Type");
                                document.fclBlCorrectionsForm.correctionType.selectedIndex = "0";
                            }
                            else {
                                document.getElementById("accountBlock").style.display = "none";
                            }
                        }
                    }
                }

            }
            function displaySubGrids() {
                var code = '';
                var choice = document.fclBlCorrectionsForm.correctionType.selectedIndex;
                var blNumber = jQuery("#blNumber").val();
                jQuery("#accountNumber").val("");
                jQuery("#accountName").val("");
                var differnceAmt = "";
                if (document.fclBlCorrectionsForm.correctionType[choice] !== undefined) {
                    var codeType = document.fclBlCorrectionsForm.correctionType[choice].text;
                    var codetypeArray = codeType.split("-");
                    if (codetypeArray.length !== undefined) {
                        code = codetypeArray[0];
                    }
                }
                displayAccountBlock();
                var customer = "";
                if (code === 'A' || code === 'Y') {
                    document.getElementById("profitAfterCn").style.display = "block";
                } else {
                    document.getElementById("profitAfterCn").style.display = "none";
                }
                if (code === 'B' && parent.parent.document.getElementById("agentNo")) {
                    customer = parent.parent.document.getElementById("agentNo").value;
                } else if ((code === 'F' || code === 'H' || code === 'D') && parent.parent.document.getElementById("forwardingAgent1")) {
                    customer = parent.parent.document.getElementById("forwardingAgent1").value;
                } else if ((code === 'G' || code === 'I' || code === 'C') && parent.parent.document.getElementById("shipper")) {
                    customer = parent.parent.document.getElementById("shipper").value;
                } else if (code === 'E' && parent.parent.document.getElementById("billTrePty")) {
                    customer = parent.parent.document.getElementById("billTrePty").value;
                } else {
                    addMemoEmailSection("", blNumber, "", customer, differnceAmt, view);
                }
                if (customer !== "") {
                    jQuery("#accountNumber").val(customer);
                    addMemoEmailSection("", blNumber, code, customer, differnceAmt, view);
                    jQuery.ajaxx({
                        data: {
                            className: "com.logiware.dwr.FclDwr",
                            methodName: "validateMasterAccount",
                            param1: customer
                        },
                        success: function(data) {
                            if (data !== '') {
                                alertNew(data + " cannot be billed as it is a Master Account");
                            } else {
                                document.getElementById("displayForCTypeA").style.visibility = "hidden";
                                if (null !== document.getElementById("chargesTable")) {
                                    document.getElementById("chargesTable").style.visibility = "hidden";
                                }
                            }
                        }
                    });
                } else {
                    addMemoEmailSection("", blNumber, "", customer, differnceAmt, view);
                    if (code === 'A' || code === 'Y') {
                        document.getElementById("displayForCTypeA").style.visibility = "visible";
                        if (null !== document.getElementById("chargesTable")) {
                            document.getElementById("chargesTable").style.visibility = "visible";
                        }
                    }
                    else {
                        document.getElementById("displayForCTypeA").style.visibility = "hidden";
                        if (null !== document.getElementById("chargesTable")) {
                            document.getElementById("chargesTable").style.visibility = "hidden";
                        }
                    }
                }
                if (faeIncentFlag !== '' && code !== 'Select Correction Type' && 'ABUTY'.indexOf(code) === -1) {
                    var result = confirm("Are you Sure Want to select Correction type '" + code + "' ?, it will delete INCENTIVE charge");
                    if (!result) {
                        document.fclBlCorrectionsForm.correctionType.selectedIndex = "0";
                        return;
                    }
                }
            }
            function getAccountNo(ev) {
                if (event.keyCode === 9 || event.keyCode === 13) {
                    var temp = ev;
                    var index = temp.indexOf("//");
                    if (index !== -1) {
                        var accountName = temp.substring(0, index);
                        document.fclBlCorrectionsForm.accountName.value = accountName;
                        var accountNo = temp.substring(index + 3, temp.length);
                        document.fclBlCorrectionsForm.accountNumber.value = accountNo;
                    }
                }
            }
            function getAccountNo1(ev) {
                var temp = ev;
                var index = temp.indexOf("//");
                if (index !== -1) {
                    var accountName = temp.substring(0, index);
                    document.fclBlCorrectionsForm.accountName.value = accountName;
                    var accountNo = temp.substring(index + 3, temp.length);
                    document.fclBlCorrectionsForm.accountNumber.value = accountNo;
                }
            }
            function AddNewCharges() {
                document.fclBlCorrectionsForm.buttonValue.value = "addNewCharges";
                document.fclBlCorrectionsForm.submit();
            }
            function callMemoEmailSection() {
                var blNumber = jQuery("#blNumber").val();
                var cnType = getCorrectionType();
                var newAcctNo = (jQuery("#accountNumber").val());
                var differnceAmt = "";
                addMemoEmailSection("", blNumber, cnType, newAcctNo, differnceAmt, view);
            }
            function deleteCharges(val1) {
                document.fclBlCorrectionsForm.buttonValue.value = "deleteCharges";
                var result = confirm("Are you sure you want to delete this Charge");
                if (result) {
                    document.fclBlCorrectionsForm.submit();
                }
            }
            function deleteCorrectionRecord(val1) {
                document.fclBlCorrectionsForm.id.value = val1;
                document.fclBlCorrectionsForm.buttonValue.value = "deleteCharges";
                var result = confirm("Are you sure you want to delete this record ");
                if (result) {
                    document.getElementById('deleteImg').style.display = 'none';
                    document.fclBlCorrectionsForm.submit();
                }
            }
            function hiddeButton(form) {
                var element;
                for (var i = 0; i < form.elements.length; i++) {
                    element = form.elements[i];
                    if (element.type === "button") {
                        if (element.value !== "Go Back" && element.value !== "Approve" && element.value !== "Manifest") {
                            element.style.visibility = "hidden";
                        }
                    }
                    var imgs = document.getElementsByTagName('img');
                    for (var k = 0; k < imgs.length; k++) {
                        imgs[k].style.visibility = 'hidden';
                    }
                    if (element.type === "select-one") {
                        element.style.border = 0;
                        element.disabled = true;
                    }
                    if (element.type === "text") {
                        element.style.backgroundColor = '#CCEBFF';
                    }
                    if (element.type === "textarea") {
                        element.style.backgroundColor = '#CCEBFF';
                    }
                }
                return false;
            }
            function openChargesPopUp(rowCount, originaBillTo, chargesId, chargeCode, chargeDesc
                    , billToParty, amount, newAmount, differenceAmount, fclBlChargeId, acctNo, chargesListSize) {
                if (chargesListSize === "" || chargesListSize < 12) {
                    var correctionCodeAorY = getCorrectionType();
                    if (document.fclBlCorrectionsForm.correctionCode.value === "0") {
                        alertNew("Please enter Correction Code");
                        return false;
                    }
                    if (document.fclBlCorrectionsForm.comments.value === "") {
                        alertNew("Please enter Comments");
                        return false;
                    }
                    if (document.fclBlCorrectionsForm.comments.value.length > 1000) {
                        alertNew("Please do not enter more than 500 characters in Comments<br>");
                        return false;
                    }
                    showPopUp();// from Common.js
                    document.getElementById('popUp').style.display = 'block';
                    if (rowCount !== -1) {
                        if (null !== originaBillTo && originaBillTo !== '') {
                            document.fclBlCorrectionsForm.originalBillToPartyCorrectionTypeY.value = originaBillTo;
                        } else {
                            document.fclBlCorrectionsForm.originalBillToPartyCorrectionTypeY.value = billToParty;
                        }
                        document.fclBlCorrectionsForm.chargeCode.value = chargeCode;
                        document.fclBlCorrectionsForm.chargeCode.readOnly = true;
                        document.fclBlCorrectionsForm.chargeCode.tabIndex = -1;
                        document.fclBlCorrectionsForm.chargeCode.className = 'BackgrndColorForTextBox';
                        document.fclBlCorrectionsForm.chargeDescription.value = chargeDesc;
                        document.fclBlCorrectionsForm.chargeDescription.readOnly = true;
                        document.fclBlCorrectionsForm.chargeDescription.tabIndex = -1;
                        document.fclBlCorrectionsForm.chargeDescription.className = 'BackgrndColorForTextBox';
                        document.fclBlCorrectionsForm.billTo.value = billToParty;
                        document.fclBlCorrectionsForm.billTo.disabled = true;
                        document.fclBlCorrectionsForm.billTo.className = 'BackgrndColorForTextBox';
                        document.fclBlCorrectionsForm.amount.value = amount;
                        document.fclBlCorrectionsForm.amount.readOnly = true;
                        document.fclBlCorrectionsForm.amount.tabIndex = -1;
                        document.fclBlCorrectionsForm.amount.className = 'BackgrndColorForTextBox';
                        document.fclBlCorrectionsForm.newAmount.value = newAmount;
                        document.fclBlCorrectionsForm.differenAmount.value = differenceAmount;
                        document.fclBlCorrectionsForm.differenAmount.readOnly = true;
                        document.fclBlCorrectionsForm.differenAmount.tabIndex = -1;
                        document.fclBlCorrectionsForm.differenAmount.className = 'BackgrndColorForTextBox';
                        document.fclBlCorrectionsForm.id.value = chargesId;
                        document.fclBlCorrectionsForm.chargeId.value = fclBlChargeId;
                        if (correctionCodeAorY === 'Y' || (faeIncentFlag !== '' && chargeCode !== 'INCENT')) {
                            document.fclBlCorrectionsForm.billTo.value = billToParty;
                            document.fclBlCorrectionsForm.billTo.disabled = false;
                            document.fclBlCorrectionsForm.billTo.className = 'dropdown_accounting';
                        }
                        if (correctionCodeAorY === 'A') {
                            getEmailIds(acctNo);
                        }
                        document.getElementById('addOrEditCharges').value = 'Edit';
                        document.getElementById('rowCnt').value = rowCount;
                    } else {
                        document.fclBlCorrectionsForm.originalBillToPartyCorrectionTypeY.value = '';
                        document.fclBlCorrectionsForm.chargeCode.readOnly = false;
                        document.fclBlCorrectionsForm.chargeCode.tabIndex = 0;
                        document.fclBlCorrectionsForm.chargeCode.className = 'textlabelsBoldForTextBox';
                        document.fclBlCorrectionsForm.chargeDescription.readOnly = false;
                        document.fclBlCorrectionsForm.chargeDescription.tabIndex = 0;
                        document.fclBlCorrectionsForm.chargeDescription.className = 'textlabelsBoldForTextBox';
                        var party = '${fclBl.billToCode}';
                        var houseBl = '${fclBl.houseBl}';
                        if (party === 'A') {
                            document.fclBlCorrectionsForm.billTo.value = "Agent";
                        } else if (party === 'S') {
                            document.fclBlCorrectionsForm.billTo.value = "Shipper";
                        } else if (party === 'T') {
                            document.fclBlCorrectionsForm.billTo.value = "ThirdParty";
                        } else if (party === 'F') {
                            document.fclBlCorrectionsForm.billTo.value = "Forwarder";
                        } else if (party === 'C') {
                            document.fclBlCorrectionsForm.billTo.value = "Consignee";
                        } else if (party === 'N') {
                            document.fclBlCorrectionsForm.billTo.value = "NotifyParty";
                        } else {
                            document.fclBlCorrectionsForm.billTo.value = "Forwarder";
                        }
                        if (billToParty !== null && billToParty !== undefined && billToParty !== "") {
                            document.fclBlCorrectionsForm.billTo.value = billToParty;
                        }
                        if (houseBl !== 'B-Both') {
                            document.fclBlCorrectionsForm.billTo.disabled = 'true';
                        }
                        if (null !== correctionCodeAorY && correctionCodeAorY === 'Y') {
                            document.fclBlCorrectionsForm.chargeId.value = fclBlChargeId;
                            document.fclBlCorrectionsForm.billTo.value = "Agent";
                            document.fclBlCorrectionsForm.billTo.disabled = false;
                        }
                        document.fclBlCorrectionsForm.chargeCode.value = "";
                        document.fclBlCorrectionsForm.chargeDescription.value = "";
                        document.fclBlCorrectionsForm.amount.value = '0.00';
                        document.fclBlCorrectionsForm.amount.readOnly = 'true';
                        document.fclBlCorrectionsForm.amount.tabIndex = -1;
                        document.fclBlCorrectionsForm.amount.className = 'BackgrndColorForTextBox';
                        document.fclBlCorrectionsForm.newAmount.value = "";
                        document.fclBlCorrectionsForm.differenAmount.value = '';
                        document.fclBlCorrectionsForm.differenAmount.readOnly = 'true';
                        document.fclBlCorrectionsForm.differenAmount.tabIndex = -1;
                        document.fclBlCorrectionsForm.differenAmount.className = 'BackgrndColorForTextBox';
                        document.getElementById('addOrEditCharges').value = 'Add';
                    }
                } else {
                    alertNew("Already reached maximum number of charges");
                }
            }

            function closeChargesPopup() {
                closePopUp();
                document.getElementById('popUp').style.display = 'none';
            }
            function   showContactsPopup() {
                if (document.fclBlCorrectionsForm.customerContact !== undefined && document.fclBlCorrectionsForm.customerContact.length !== undefined) {
                    for (var i = 0; i < document.fclBlCorrectionsForm.customerContact.length; i++) {
                        document.fclBlCorrectionsForm.customerContact[i].checked = false;
                    }
                } else if (document.fclBlCorrectionsForm.customerContact !== undefined && document.fclBlCorrectionsForm.customerContact.length === undefined) {
                    document.fclBlCorrectionsForm.customerContact.checked = false;
                } else {
                    alertNew('No contacts to display');
                    return false;
                }
                showPopUp();
                if (document.getElementById("customerContactDiv")) {
                    floatDiv("customerContactDiv", 100, document.body.offsetHeight / 4).floatIt();
                }
                document.getElementById('showAll').value = 'Show All Contacts';
                document.getElementById('showAll1').value = 'Show All Contacts';
                document.getElementById('customerCodeCContact').style.display = 'block';
                document.getElementById('customerAllContact').style.display = 'none';
                document.getElementById('customerContactDiv').style.display = 'block';
            }
            function showAllContact(obj) {
                if (obj.value === 'Show All Contacts') {
                    document.getElementById('showAll').value = 'Show CodeC Contacts';
                    document.getElementById('showAll1').value = 'Show CodeC Contacts';
                    document.getElementById('customerCodeCContact').style.display = 'none';
                    document.getElementById('customerAllContact').style.display = 'block';
                } else {
                    document.getElementById('showAll').value = 'Show All Contacts';
                    document.getElementById('showAll1').value = 'Show All Contacts';
                    document.getElementById('customerCodeCContact').style.display = 'block';
                    document.getElementById('customerAllContact').style.display = 'none';
                }
            }
            function closeContactsPopup() {
                closePopUp();
                document.getElementById('customerContactDiv').style.display = 'none';
            }
            function  selectContactsFromPopup() {
                var email = document.fclBlCorrectionsForm.sendCopyTo.value;
                var customerContacts = document.getElementsByName("customerContact");
                if (customerContacts) {
                    for (var i = 0; i < customerContacts.length; i++) {
                        if (customerContacts[i].checked) {
                            var emailFax = customerContacts[i].value;
                            var emailFaxArray = emailFax.split("==");
                            if (emailFaxArray[0].trim() !== '') {
                                if (trim(email) !== '') {
                                    var index = email.length;
                                    if (email.charAt(index - 1) !== ',' && email.charAt(index - 1) !== '\n') {
                                        email += ',\n' + emailFaxArray[0].trim() + ",\n";
                                    } else {
                                        email += emailFaxArray[0].trim() + ",\n";
                                    }
                                } else {
                                    email += emailFaxArray[0].trim() + ",\n";
                                }
                                //email = email.trim() != ''?email+",\n"+emailFaxArray[0]:emailFaxArray[0];
                            }
                        }
                    }
                }
                document.fclBlCorrectionsForm.sendCopyTo.value = email;
                closePopUp();
                document.getElementById('customerContactDiv').style.display = 'none';
            }
            function wordwrap(str, width, brk, cut) {
                brk = brk || '<br/>\n';
                width = width || 40;
                cut = cut || false;
                if (!str) {
                    return str;
                }
                var regex = '.{1,' + width + '}(\\s|$)' + (cut ? '|.{' + width + '}|.+$' : '|\\S+?(\\s|$)');
                return str.match(RegExp(regex, 'g')).join(brk);
            }
            function confirmMessageFunction(id1, id2) {
                if (id1 === 'goBack' && id2 === 'ok') {
                    saveMainCorrections();
                } else if (id1 === 'goBack' && id2 === 'cancel') {
                    goBackCall();
                } else if (id1 === 'savePostQuickCn' && id2 === 'ok') {
                    savePostQuickCn();
                }
            }
            function openContactPopup() {
                var href = "/logisoft/customerAddress.do?buttonValue=Quotation";
                mywindow = window.open(href, '', 'width=900,height=400,scrollbars=yes');
                mywindow.moveTo(200, 180);
                // GB_show("Contact Info", "/logisoft/customerAddress.do?buttonValue=Quotation", width = "400", height = "835");
            }
            function allowOnlyWholeNumbers(obj) {
                var result;
                if (!/^[1-9 . ]\d*$/.test(obj.value)) {
                    result = obj.value.replace(/[^0-9 . ]+/g, '');
                    obj.value = result;
                    return false;
                }
                return true;
            }
            function addCommaOnEnter(obj) {
                if (event.keyCode === 9 || event.keyCode === 13) {
                    if (obj.value !== '') {
                        var index = obj.value.length;
                        if (obj.value.charAt(index - 1) !== ',') {
                            obj.value += ',';
                        }
                        var str = obj.value;
                        var val = str.split(',');
                        for (var i = 0; i < val.length; i++) {
                            validate_email(val[i], val[i] + " Is Not A Valid Email");
                        }
                    }
                }
            }
            function approveUse(val1, val2, fileNo) {
                GB_show('Approval', '<%=path%>/jsps/fclQuotes/authanticatePassword.jsp?notice=' + val1 + '&blId=' + val2 + '&fileNo=' + fileNo, width = "200", height = "400");
            }
            
        function checkAddCorrectionChargeMappingWithGL(chargeCode,fileNo) {
        var flag = true;
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                methodName: "checkChargeAndCostMappingWithGL",
                param1: chargeCode,
                param2: fileNo,
                param3: 'AR',
                param3: 'BL'
            },
            async: false,
            success: function (data) {
                if (data !== "") {
                    alertNew("No gl account is mapped with these charge code.Please contact accounting -> <span style=color:red>" + data + "</span>.");
                    flag = false;
                }
            }
        });
        return flag;
    }
        </script>
        <style type="text/css">
            #popUp{
                position:fixed;
                _position:absolute;
                border-style: solid solid solid solid;
                background-color: white;
                left:10%;
                top:20%;
                width:700px !important;
                height:300px;
            }
            #customerContactDiv{
                position:fixed;
                _position:absolute;
                border-style: solid solid solid solid;
                background-color: white;
                z-index:99;
                _height:expression(document.body.offset+"px");
                overflow:scroll;
                left:0%;
                azimuth: left-side;
            }
        </style>
        <%@include file="../includes/resources.jsp" %>
    </head>
    <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>

    <body class="whitebackgrnd">
        <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert" style="width: 100%;padding-left: 3px;">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display = 'none';
                grayOut(false, '');">
            </form>
        </div>
        <div id="ConfirmBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Confirmation</b></p>
            <p id="innerText1" class="containerForAlert" style="width: 100%;padding-left: 3px;">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom:4px;padding-top:10px;">
                <input type="button"  class="buttonStyleForAlert" value="OK" id="confirmYes"
                       onclick="yes()">
                <input type="button"  class="buttonStyleForAlert" value="Cancel" id="confirmNo"
                       onclick="No()">
            </form>
        </div>

        <!--// ALERT BOX DESIGN ENDS -->

        <html:form action="/fclBlCorrections" styleId="fclBlCorrection" scope="request">

            <div id="cover"></div>
            <div id="customerContactDiv" style="display:none;width:730px;height:320px;azimuth:left-side;">
                <c:if test="${not empty fclBl.importFlag && fclBl.importFlag=='I'}">
                    <table width="100%">
                        <tr>
                            <td align="right">
                                <img src="<%=path%>/img/icons/comparison.gif" border="0" alt="Add Trading Partner Contacts" onclick="openContactPopup()"/>
                            </td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
                        </tr>
                    </table>
                </c:if>
                <%@include file="../print/customerList.jsp"%>
            </div>


            <div id="popUp" style="display:none;width:500px;height:200px;">
                <table width="100%" border="0" class="tableBorderNew">
                    <tr class="tableHeadingNew">
                        <td colspan="6">Add/Edit Corrections</td></tr>
                    <tr class="textlabelsBold">
                        <td>Charge Code Desc</td>
                        <td><input type="text" name="chargeDescription" maxlength="60"
                                   id= "chargeDescription" size="20" Class="textlabelsBoldForTextBox" >
                            <input name="chargeCodeCheck" id="chargeCodeCheck" type="hidden"/>
                            <div id="chargeCodeChoices" style="display: none"
                                 class="autocomplete"></div>
                            <script type="text/javascript">
                                initAutocomplete("chargeDescription", "chargeCodeChoices", "chargeCode", "chargeCodeCheck",
                                        "<%=path%>/actions/autoCompleterForChargeCode.jsp?tabName=FCL_BL_CHARGES&from=8&import=${importFlag}", "");
                            </script>
                        </td>
                        <td>Charge Code</td>
                        <td>
                            <input type="text" name="chargeCode" id = "chargeCode" Class="textlabelsBoldForTextBox"/>
                        </td>
                        <td>Bill To</td>
                        <td>
                            <html:select property="billTo" styleId="billTo" style="width:120px;" styleClass="dropdown_accounting">
                                <html:optionsCollection name="billToVendor"/>
                            </html:select>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Old Amount</td>
                        <td><html:text property="amount" styleClass="textlabelsBoldForTextBox" maxlength="8"/></td>
                        <td>New Amount</td>
                        <td><html:text  property="newAmount" styleClass="textlabelsBoldForTextBox" maxlength="8"
                                    onblur="allowOnlyWholeNumbers(this)"/></td>
                        <td>Difference</td>
                        <td><html:text property="differenAmount" size="10" styleClass="textlabelsBoldForTextBox" maxlength="13"/></td>
                    </tr>
                    <tr><td colspan="6">&nbsp;</td></tr>
                    <tr>
                        <td colspan="6" align="center">
                            <input class="buttonStyleNew" type="button" value="Cancel" onclick="closeChargesPopup()">
                            <input type="button" class="buttonStyleNew" id="saveCorrections" value="Save"  onclick="SaveCorrections()"/>
                        </td>
                    </tr>
                </table>
                <input type="hidden" name="hiddenEmailIds" id="hiddenEmailIds" />
                <input type="hidden" name="creditMemoEmail" id="creditMemoEmail" />
                <input type="hidden" name="debitMemoEmail" id="debitMemoEmail" />
            </div>

            <font color="Blue" size="3"><b><%=message%></b></font>
            <table>
                <tr>
                    <td>
                        <table class="tableBorderNew">
                            <tr class="textlabelsBold">
                                <td>BL NO:</td><td><b class="headerlabel" style="color:blue;">
                                        <c:out value="${companyCode}"/><c:choose><c:when test="${fclBl.importFlag!='I'}">-</c:when><c:otherwise> </c:otherwise></c:choose><c:out value="${FclBlCorrection.blNumber}"/></b></td>
                                    </tr></table></td><td>
                                <table class="tableBorderNew">
                                    <tr class="textlabelsBold">
                                <c:if test="${not  empty FclBlCorrection.noticeNo }">
                                    <td>CN #</td>
                                    <td><b class="headerlabel" style="color:blue;">  <c:out value="${FclBlCorrection.noticeNo}"/></b></td>
                                </c:if>
                            </tr></table></td><td>
                        <table class="tableBorderNew">
                            <tr class="textlabelsBold">
                                <c:if test="${not  empty FclBlCorrection.userName }">
                                    <td>Created BY:</td>
                                    <td><b class="headerlabel" style="color:blue;">  <c:out value="${FclBlCorrection.userName}"/></b></td>
                                </c:if>
                            </tr></table></td><td>
                        <table class="tableBorderNew">
                            <tr class="textlabelsBold">
                                <c:if test="${not  empty FclBlCorrection.date }">
                                    <td>On:</td>
                                    <td><fmt:formatDate pattern="dd-MMM-yyyy HH:mm" var="noticeDate" value="${FclBlCorrection.date}"/>
                                        <b class="headerlabel" style="color:blue;">  <c:out value="${noticeDate}"/></b></td>
                                    </c:if>

                            </tr>
                        </table>
                    </td><td>
                        <c:choose>
                            <c:when test="${FclBlCorrection.status=='Approved'}">
                            <td> <b> <FONT size="2" color="red"> Approved</FONT></td>
                                    <c:if test="${not  empty FclBlCorrection.manifest }">
                                <td>  <b> <FONT size="2" color="red"><c:out value=",Posted"/> </FONT>
                                    </c:if>	</b>
                                </c:when>
                            </c:choose>
                    </td>
                </tr>
            </table>

            <table width="100%" border="0" class="tableBorderNew" cellpadding="4" cellspacing="0">
                <tr class="tableHeadingNew">
                    <td colspan="2">Add/Edit Corrections<td>
                    <td  align="right" colspan="6">
                        <c:choose>
                            <c:when test="${FclBlCorrectionForm.quickCn}">
                                <input type="button" class="buttonStyleNew" value="Post Quick CN"  onclick="postQuickCn()"/>
                            </c:when>
                            <c:otherwise>
                                <% if (request.getAttribute("updateButton") == null) {%>
                                <input type="button" class="buttonStyleNew" id="saveMainCorrection" value="Save"  onclick="saveMainCorrections()"/>
                                <%} else {%>
                                <c:choose>
                                    <c:when test="${not empty FclBlCorrectionForm.approval}">
                                        <input type="button" class="buttonStyleNew" value="ApproveBL"
                                               onclick="approveCorrections('${FclBlCorrection.blNumber}', '${FclBlCorrection.noticeNo}')"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="button" class="buttonStyleNew" value="Update" onclick="UpdateCorrections('${FclBlCorrection.noticeNo}')"/>
                                    </c:otherwise>
                                </c:choose>
                                <%}%>
                                <c:if test="${empty FclBlCorrectionForm.temp && roleDuty.postCorrections && empty FclBlCorrection.status}">
                                    <input type="button" class="buttonStyleNew" id="approve" value="Approve"
                                           onclick="approveUse('${FclBlCorrection.noticeNo}', '${FclBlCorrection.blNumber}', '${FclBlCorrection.fileNo}')" />
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${not empty FclBlCorrection.status && empty FclBlCorrection.manifest }">
                            </c:when></c:choose>

                            <input type="button" class="buttonStyleNew" value="Go Back"  onclick="GoBack()"/>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Origin</td>
                        <td>
                            <span onmouseover="tooltip.show('<strong> ${fclBl.terminal}</strong>', null, event);" onmouseout="tooltip.hide();">
                            <input  class="BackgrndColorForTextBox" value="${fclBl.terminal}" readonly="readonly"/>
                        </span></td>
                    <td>Destination</td>
                    <td>
                        <span onmouseover="tooltip.show('<strong>${fclBl.finalDestination}</strong>', null, event);" onmouseout="tooltip.hide();">
                            <input class="BackgrndColorForTextBox"  value="${fclBl.finalDestination}" readonly="readonly"/>
                        </span></td>
                    <td>Sail Date</td>
                    <td><fmt:formatDate pattern="dd-MMM-yyyy" var="sailDate" value="${fclBl.sailDate}"/>
                        <input class="BackgrndColorForTextBox" value="${sailDate}" readonly="readonly"/></td>
                    <td valign="top">Voyage</td>
                    <td valign="top">
                        <input class="BackgrndColorForTextBox" value="${fclBl.voyages}" readonly="readonly"
                               <c:if test="${not empty fclBl.voyages}">
                                   onmouseover="tooltip.show('<strong>${fclBl.voyages}</strong>', null, event);" onmouseout="tooltip.hide();"
                               </c:if>
                               /></td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Agent</td>
                    <td>
                        <input class="BackgrndColorForTextBox"  value="${fclBl.agent}" readonly="readonly"
                               <c:if test="${not empty fclBl.agent}">
                                   onmouseover="tooltip.show('<strong>${fclBl.agent}</strong>', null, event);" onmouseout="tooltip.hide();"
                               </c:if>/>
                    </td>
                    <td>Shipper</td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty fclBl.importFlag && fclBl.importFlag=='I'}">
                                <input class="BackgrndColorForTextBox" value="${fclBl.houseShipperName}" readonly="readonly"
                                       <c:if test="${not empty fclBl.houseShipperName}">
                                           onmouseover="tooltip.show('<strong>${fclBl.houseShipperName}</strong>', null, event);" onmouseout="tooltip.hide();"
                                       </c:if>/>
                            </c:when>
                            <c:otherwise>
                                <input class="BackgrndColorForTextBox" value="${fclBl.shipperName}" readonly="readonly"
                                       <c:if test="${not empty fclBl.shipperName}">
                                           onmouseover="tooltip.show('<strong>${fclBl.shipperName}</strong>', null, event);" onmouseout="tooltip.hide();"
                                       </c:if>/>
                            </c:otherwise>

                        </c:choose>
                    </td>
                    <td>Third Party</td>
                    <td>
                        <input  class="BackgrndColorForTextBox" value="${fclBl.billTrdPrty}"
                                readonly="readonly"
                                <c:if test="${not empty fclBl.billTrdPrty}">
                                    onmouseover="tooltip.show('<strong>${fclBl.billTrdPrty}</strong>', null, event);" onmouseout="tooltip.hide();"
                                </c:if>/>
                    </td>
                    <td>F/F #</td>
                    <td><input type="text" class="BackgrndColorForTextBox" value="${fclBl.forwardingAgentName}"
                               readonly="readonly"/></td>
                </tr>
                <tr class="textlabelsBold">
                    <td colspan="1">Originally --></td>
                    <td valign="baseline" colspan="5">
                        <c:if test="${FclBlCorrectionForm.houseBl=='P'}">
                            <c:out value="Prepaid"></c:out>
                        </c:if>
                        <c:if test="${FclBlCorrectionForm.houseBl=='C'}">
                            <c:out value="Collect"></c:out>
                        </c:if>

                        <c:if test="${FclBlCorrectionForm.houseBl=='B'}">
                            <c:out value="Both"></c:out>
                        </c:if>
                        By
                        <font color="Blue">
                        <c:if test="${fclBl.billToCode=='S'}">
                            <c:out value="Shipper"></c:out>
                        </c:if>
                        <c:if test="${fclBl.billToCode=='F'}">
                            <c:out value="Forwarder"></c:out>
                        </c:if>
                        <c:if test="${fclBl.billToCode=='A'}">
                            <c:out value="Agent"></c:out>
                        </c:if>
                        <c:if test="${fclBl.billToCode=='T'}">
                            <c:out value="ThirdParty"></c:out>
                        </c:if>
                        <c:if test="${fclBl.billToCode=='N'}">
                            <c:out value="NotifyParty"></c:out>
                        </c:if>
                        </font>
                        <font color="red">
                        <c:out value="${FclBlCorrectionForm.tempCustomerName}"></c:out>
                        <c:out value="${FclBlCorrectionForm.tempCustomerNo}"></c:out></font></td>
                        <td>File No</td>
                        <td><html:text property="fileNo"  styleClass="BackgrndColorForTextBox"  value="${fclBl.fileNo}"
                               readonly="true"/></td>
                </tr>
                <tr class="textlabelsBold">
                    <c:if test="${! empty FclBlCorrectionForm.correctionType && FclBlCorrectionForm.correctionType != 'A' && FclBlCorrection.correctionType.code!='T' && FclBlCorrection.correctionType.code!='U'}">
                        <td colspan="1">New Bill To --></td>
                        <td colspan="5">
                            <font color="red">
                            <c:out value="${FclBlCorrectionForm.accountName}"></c:out>
                            <c:out value="${FclBlCorrectionForm.accountNumber}"></c:out></font></td>
                        </c:if>
                </tr>
                <tr>
                    <td colspan="8">
                        <div id="memoDivSection"></div>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Comments<b  class="mandatoryStarColor">*</b></td>
                    <td colspan="3">
                        <c:choose>
                            <c:when test="${FclBlCorrectionForm.quickCn && empty FclBlCorrection.comments}">
                                <html:textarea styleClass="textlabelsBoldForTextBox" property="comments" value="QUICK CN" styleId="comments"
                                               onclick="selectBlNumber()" rows="4" cols="65" style="text-transform: uppercase" onkeypress="return checkTextAreaLimit(this, 1000)"/>
                            </c:when>
                            <c:otherwise>
                                <html:textarea styleClass="textlabelsBoldForTextBox" property="comments" value="${FclBlCorrection.comments}" styleId="comments"
                                               onclick="selectBlNumber()" rows="4" cols="65" style="text-transform: uppercase" onkeypress="return checkTextAreaLimit(this, 1000)" />
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <html:hidden property="oldComment" styleId="oldComment" value="${FclBlCorrection.comments}"/>
                </tr>
                <tr class="textlabelsBold">
                    <td>C/N Type<b class="mandatoryStarColor">*</b></td>
                    <td colspan="5">
                        <c:choose>
                            <c:when test="${not empty FclBlCorrectionForm.viewMode && FclBlCorrectionForm.viewMode=='view'}">
                                <input  size="80"  class="BackgrndColorForTextBox" value="${FclBlCorrection.correctionType.code}-${FclBlCorrection.correctionType.codedesc}"/>
                                <html:select property="correctionType" style="display:none" value="${FclBlCorrection.correctionType.id}" >
                                    <html:option value="${FclBlCorrection.correctionType.id}">${FclBlCorrection.correctionType.code}</html:option>
                                    <html:option value="${FclBlCorrection.correctionType.id}">${FclBlCorrection.correctionType.code}</html:option>
                                </html:select>
                            </c:when>
                            <c:when test="${FclBlCorrectionForm.quickCn}">
                                <input  size="80"  class="BackgrndColorForTextBox" value="${correctionType.code}-${correctionType.codedesc}"/>
                                <html:select property="correctionType" style="display:none" value="${correctionType.id}" >
                                    <html:option value="${correctionType.id}">${correctionType.code}-${correctionType.codedesc}</html:option>
                                </html:select>
                            </c:when>
                            <c:otherwise>
                                <html:select property="correctionType" value="${FclBlCorrection.correctionType.id}"
                                             onchange="displaySubGrids()"  styleId="correctionTypeId"
                                             styleClass="dropdown_accounting" onclick="selectBlNumber()"
                                             style="width:481px;">
                                    <html:optionsCollection name="correctionTypeList"/></html:select>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${FclBlCorrectionForm.viewMode!='view'}">
                                <input type="button" class="buttonStyleNew" id="displayForCTypeA"
                                       value="Add Charges" style="width:80px;" onclick="openChargesPopUp(<%=-1%>, '', '', '', '', '${FclBlChargesList[0].billToParty}', '', '', '', '', '', '${fn:length(FclBlChargesList)}')"/>
                            </c:when>
                            <c:otherwise>
                                <input type="button" class="buttonStyleNew" id="displayForCTypeA"
                                       value="Add Charges" style="width:80px;display: none"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td id="currentProfit"><font class="green"> Current Profit     : </font><font class="red-90"> ${FclBlCorrectionForm.currentProfit}</font></td>
                </tr>
                <tr class="textlabelsBold">
                    <td>C/N Code<b class="mandatoryStarColor">*</b></td>
                    <td colspan="5">
                        <c:choose>
                            <c:when test="${not empty FclBlCorrectionForm.viewMode && FclBlCorrectionForm.viewMode=='view'}">
                                <input  size="80"  class="BackgrndColorForTextBox" value="${FclBlCorrection.correctionCode.codedesc}"/>
                                <html:hidden property="correctionCode"   value="${FclBlCorrection.correctionType.id}"/>
                            </c:when>
                            <c:when test="${FclBlCorrectionForm.quickCn && empty FclBlCorrection.correctionCode}">
                                <html:select property="correctionCode" styleClass="dropdown_accounting"
                                             styleId="correctionCodeId" value="${correctionCode.id}" onclick="selectBlNumber()" style="width:481px;">
                                    <html:optionsCollection name="correctionCodeList"/>
                                </html:select>
                            </c:when>
                            <c:otherwise>
                                <html:select property="correctionCode" styleClass="dropdown_accounting"
                                             styleId="correctionCodeId"  value="${FclBlCorrection.correctionCode.id}" onclick="selectBlNumber()" style="width:481px;">
                                    <html:optionsCollection name="correctionCodeList"/>
                                </html:select>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td id="profitAfterCn"><font class="green"> Profit after C/N : </font><font class="red-90">  ${FclBlCorrectionForm.profitAfterCn}</font></td>
                    <td align="center"></td>
                </tr>
            </table>
            <table style=display:none; id="accountBlock">
                <tr class="textlabelsBold">
                    <td>New Account Name</td>
                    <td>
                        <input class="textlabelsBoldForTextBox" name="accountName" id="accountName" onkeydown="getAccountNo(this.value);"
                               onchange="getAccountNo1(this.value);"  maxlength="50" size="50"
                               value="${FclBlCorrection.accountName}" onclick="selectBlNumber()" onblur="setFocusFromAccountName()"/>
                        <input name="accountNameCheck" id="accountNameCheck" type="hidden"/>
                        <div id="accountNameChoices" style="display: none" class="autocomplete"></div>
                        <script type="text/javascript">
                            initAutocomplete("accountName", "accountNameChoices", "accountNumber", "accountNameCheck",
                                    "<%=path%>/actions/tradingPartner.jsp?tabName=FCL_BL&from=12","callMemoEmailSection()");
                        </script>
                    </td></tr>
                <tr class="textlabelsBold">
                    <td>New Account Number</td>
                    <td>
                        <input name="accountNumber" class="textlabelsBoldForTextBox" id="accountNumber" maxlength="10" size="10"
                               value="${FclBlCorrection.accountNumber}" onclick="selectBlNumber()"/>
                    </td>
                </tr>
            </table>
            <%int i = 1;
            %>
            <display:table name="${FclBlChargesList}" id="chargesTable"
                           defaultorder="descending"  class="displaytagstyle">
                <display:setProperty name="basic.msg.empty_list">
                    <span style="display:none;" class="pagebanner"/>
                </display:setProperty>
                <display:column style="visibility:hidden;">${chargesTable.id}</display:column>
                <display:column title="Charge Code" property="chargeCode"/>
                <display:column title="ChargeCode Description" property="chargeCodeDescription"/>
                <display:column title="Bill To Party" property="billToParty"></display:column>
                <display:column title="Amount" >
                    <fmt:formatNumber  pattern="###,###,##0.00" value="${chargesTable.amount}" var="amt"/>
                    <c:out value="${amt}"/>
                </display:column>
                <display:column title="New Amount" >
                    <fmt:formatNumber  pattern="###,###,##0.00" value="${chargesTable.newAmount}" var="newAmt"/>
                    <c:out value="${newAmt}"/></display:column>
                <display:column title="Difference">
                    <fmt:formatNumber  pattern="###,###,##0.00" value="${chargesTable.differeceAmount}" var="differenceAmt"/>
                    <c:out value="${differenceAmt}"/>
                </display:column>
                <display:column title="Action">
                    <c:if test="${chargesTable.chargeCode!='PBASUR' && FclBlCorrectionForm.viewMode!='view'}">
                        <span class="hotspot">
                            <img src="<%=path%>/img/icons/edit.gif"  border="0" id="displayForEditIcon" title="Edit"
                                 onclick="openChargesPopUp('<%=i%>', '${chargesTable.originalBillToPartyCorrectionTypeY}',
                                                 '${chargesTable.id}', '${chargesTable.chargeCode}', '${chargesTable.chargeCodeDescription}',
                                                 '${chargesTable.billToParty}', '${amt}', '${newAmt}', '${differenceAmt}',
                                                 '${chargesTable.fclBlChargeId}', '${FclBlCorrectionForm.tempCustomerNo}', '')"/>
                        </span>
                        <c:if test="${chargesTable.newAmount!=null}">
                            <span class="hotspot" onmouseover="tooltip.show('Delete', null, event);" onmouseout="tooltip.hide();">
                                <img src="<%=path%>/img/icons/delete.gif" border="0"  id="deleteImg" onclick="deleteCorrectionRecord(${chargesTable.id})"/>
                            </span>
                        </c:if>
                    </c:if>
                </display:column>
                <%i++;%>
            </display:table>

            <c:choose>
                <c:when test="${empty FclBlCorrection.noticeNo}">
                    <input type="hidden" value="NEW"/>
                </c:when>
                <c:otherwise>
                    <input type="hidden" value="${FclBlCorrection.noticeNo}"/>
                </c:otherwise>
            </c:choose>
            <html:hidden property="buttonValue"/>
            <html:hidden property="originalBillToPartyCorrectionTypeY"/>
            <html:hidden property="eventCode" styleId="eventCode"/>
            <html:hidden property="eventDesc" styleId="eventDesc"/>
            <html:hidden property="moduleId" value="<%=NotesConstants.MODULE_ID_CORRECTION%>"/>
            <html:hidden property="moduleRefId" value="${fclBl.fileNo}"/>
            <html:hidden property="index1" value="${FclBlCorrection.id}"/>
            <html:hidden property="approval" value="${FclBlCorrectionForm.approval}"/>
            <html:hidden property="shipper" value="${fclBl.shipperName}"/>
            <html:hidden property="forwarder" value="${fclBl.forwardingAgentName}"/>
            <html:hidden property="agent" value="${fclBl.agent}"/>
            <html:hidden property="thirdParty" value="${fclBl.billTrdPrty}"/>
            <html:hidden property="voyages" value="${fclBl.voyages}"/>
            <html:hidden property="origin" value="${fclBl.terminal}"/>
            <html:hidden property="blNumber" styleId="blNumber" value="${FclBlCorrection.blNumber}"/>
            <html:hidden property="rampCity" value="${fclBl.rampCity}"/>
            <html:hidden property="pol" value="${fclBl.portOfLoading}"/>
            <html:hidden property="pod" value="${fclBl.portofDischarge}"/>
            <html:hidden property="destination" value="${fclBl.finalDestination}"/>
            <fmt:formatDate pattern="MM/dd/yyyy hh:mm a" var="sailDate" value="${fclBl.sailDate}"/>
            <html:hidden property="sailDate" value="${sailDate}"/>
            <html:hidden property="newTempCorrectionType" value="${FclBlCorrection.tempCrType}"/>
            <html:hidden property="noticeNo" value="${FclBlCorrection.noticeNo}"/>
            <html:hidden property="temp" value="${FclBlCorrectionForm.temp}"/>
            <html:hidden property="houseBl" value="${FclBlCorrectionForm.houseBl}"/>
            <html:hidden property="chargeId"/>
            <html:hidden property="quickCn" styleId="quickCn" value="${FclBlCorrectionForm.quickCn}"/>
            <html:hidden property="importFlag" styleId="importFlag" value="${fclBl.importFlag}"/>
            <input type="hidden" id="addOrEditCharges"/>
            <input type="hidden" id="rowCnt"/>
            <input type="hidden" id="correctionCodeAorY"/>
            <html:hidden property="id"/>

        </html:form>
        <script>load();</script>
    </body>

    <c:choose>
        <c:when test="${not empty FclBlCorrectionForm.viewMode && FclBlCorrectionForm.viewMode=='view'}">
            <c:choose>
                <c:when test="${not empty FclBlCorrectionForm.temp}">
                    <script>
                        parent.parent.callMakeFormBorderlessForPopupPage(document.getElementById("fclBlCorrection"));
                        hiddeButton(document.getElementById("fclBlCorrection"));
                    </script>
                </c:when>
                <c:otherwise>
                    <script>
                        parent.makeFormBorderless(document.getElementById("fclBlCorrection"));
                        hiddeButton(document.getElementById("fclBlCorrection"));
                    </script>
                </c:otherwise>
            </c:choose>
        </c:when>
    </c:choose>
</html>
