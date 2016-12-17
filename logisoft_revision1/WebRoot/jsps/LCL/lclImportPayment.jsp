<%-- 
    Document   : lclImportPayment
    Created on : 29 Apr, 2013, 3:56:26 PM
    Author     : Meiyazhakan
--%>
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
<link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
<link type="text/css" rel="stylesheet" href="${path}/css/fileUpload/enhanced.css"/>
<script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
<script type="text/javascript">
    jQuery(document).ready(function () {
        if ($('#importCreditStatus').val() === 'In Good Standing' || $('#importCreditStatus').val() === 'Credit Hold') {
            $('#paymentTypeIC').attr('disabled', false);
        } else {
            $('#paymentTypeIC').attr('disabled', true);
        }
        detectFormChanged();
        var isDisableCheckCopy = $('#isDisableCheckCopy').val();
        if (isDisableCheckCopy == 'false') {
            $('#paymentTypeCO').attr('disabled', true);
        } else {
            $('#paymentTypeCO').attr('disabled', false);
        }
    });
    function detectFormChanged() {
        var changeSave = $('#formChangeFlag').val();
        parent.$('#formChangeFlag').val(changeSave);
    }
    function payAllAmt() {
        $('.chargesChecked').attr('checked', true);
        $('.cAmt').each(function () {
            var cAmt = +($(this).html().replace(/^\s\s*/, '').replace(/\s\s*$/, ''));
            var pAmt = +($(this).next().html().replace(/^\s\s*/, '').replace(/\s\s*$/, ''));
            var balance = (cAmt - pAmt).toFixed(2);
            $(this).next().next().html('0.00');
            $(this).next().next().next().find('.manualAmt').val(balance);
        });
        // var totalAmt= +($("#amount").val());
        var totalAmt = 0;
        $('.editAmt').show();
        $('.manualAmt').each(function () {
            totalAmt += +($(this).val());
        });
        var chequeAmount = (+$("#amount").val()).toFixed(2);
        if (Number(chequeAmount) < Number(totalAmt.toFixed(2))) {
            $('.chargesChecked').attr('checked', false);
            $('.cAmt').each(function () {
                $(this).next().next().next().find('.manualAmt').val('');
            });
            $('.editAmt').hide();
            sampleAlert("Payment Amount should not be Less than Paid Amount");
        }
    }
    function savePayment() {
        var bkgchargesId = [];
        var bkgchargesAmt = [];
        var amount = (+$("#amount").val()).toFixed(2);
        var manualAmt = $("#totalchrgeAmount").val();
        jQuery(".chargesChecked:checked").each(function () {
            var chargesId = jQuery(this).val();
            var amt = $('#chargeAmount' + chargesId).val();
            manualAmt = Number(manualAmt) + Number(amt);
            bkgchargesAmt.push(amt);
            bkgchargesId.push(chargesId);
        });
        if (($('#paymentTypeCH').is(":checked") === false && $('#paymentTypeAC').is(":checked") === false && $('#paymentTypeCC').is(":checked") === false && $('#paymentTypeIC').is(":checked") === false && $('#paymentTypeCO').is(":checked") === false)) {
            sampleAlert("Please select the Payment type");
            return false;
        } else if ($('#checkNumber').val() === null || $('#checkNumber').val() === "") {
            sampleAlert("Check Number is Required");
            setWarning("checkNumber");
            $("#checkNumber").focus();
            return false;
        } else if ($('#amount').val() === null || $('#amount').val() === "") {
            setWarning("amount");
            sampleAlert("Payment Amount is Required");
            return false;
        } else if (bkgchargesId.length <= 0) {
            sampleAlert("Please select atleast one Charge");
            return false;
        } else if (Number(amount) < Number(manualAmt.toFixed(2))) {
            $("#amount").val('');
            $("#amount").focus();
            sampleAlert("Payment Amount should not be Less than Paid Amount");
            return false;
        } else {
            showLoading();
            jQuery("#bookingAcId").val(bkgchargesId);
            jQuery("#chargesAmount").val(bkgchargesAmt);
            $("#totalchrgeAmount").val(manualAmt);
            $("#methodName").val('savePayment');
            $("#lclImportPaymentForm").submit();
        }
    }

    function deletePayment(txt, bookingactId) {
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    showLoading();
                    $("#bkgTransid").val(bookingactId);
                    $('#methodName').val("deletePayment");
                    $("#lclImportPaymentForm").submit();
                    $.prompt.close();
                }
                else if (v === 2) {
                    $.prompt.close();
                }
            }
        });
    }
    function totalCharges(id, totalAmt, paidAmt) {
        var chequeAmt = +($('#amount').val());
        if (chequeAmt === '' || chequeAmt === null || chequeAmt === 0) {
            $('#chargesCheck' + id).attr('checked', false);
            sampleAlert("Please Enter Payment Amount");
            setWarning("amount");
        } else {
            var manualTotal = 0;
            $('.manualAmt').each(function () {
                var amtPaid = +($(this).val().replace(/^\s\s*/, '').replace(/\s\s*$/, ''));
                manualTotal += amtPaid;
            });
            var balanceAmt = +(totalAmt - paidAmt);
            if ($('#chargesCheck' + id).is(":checked") === true) {
                if (chequeAmt !== null && chequeAmt !== '') {
                    if (manualTotal !== null && manualTotal !== '') {
                        var totalManual = +(chequeAmt - manualTotal);
                        if (totalManual < balanceAmt) {
                            $("#chargeAmount" + id).val(totalManual.toFixed(2));
                        } else {
                            $("#chargeAmount" + id).val(balanceAmt.toFixed(2));
                        }
                    } else {
                        if (balanceAmt > chequeAmt) {
                            $("#chargeAmount" + id).val(chequeAmt.toFixed(2));
                        } else {
                            $("#chargeAmount" + id).val(balanceAmt.toFixed(2));
                        }
                    }
                } else {
                    if (paidAmt !== null && paidAmt !== '') {
                        $("#chargeAmount" + id).val(balanceAmt.toFixed(2));
                    } else {
                        $("#chargeAmount" + id).val(totalAmt);
                    }
                }
                $('#editImgCharge' + id).show();
            } else {
                $('#editImgCharge' + id).hide();
                $("#chargeAmount" + id).val('');
                $("#chargeAmount" + id).focus();
            }
        }
    }
    function validateAmt(id, totalAmt, paidAmt) {
        var manualAmt = $("#chargeAmount" + id).val();
        if (paidAmt !== null && paidAmt !== "") {
            if ((Number(totalAmt) - Number(paidAmt)) < Number(manualAmt)) {
                sampleAlert("This field should be less than Paid Amout");
                $("#chargeAmount" + id).val('');
                $("#chargeAmount" + id).focus();
            }
        } else {
            if (Number(totalAmt) < Number(manualAmt)) {
                sampleAlert("This field should be less than Charge Amout");
                $("#chargeAmount" + id).val('');
                $("#chargeAmount" + id).focus();
            }
        }
    }

    function viewPayment(path, count, bkgTransid) {
        var href = path + "/lclImportPayment.do?methodName=viewPayment&bkgTransid=" + bkgTransid;
        $("#payment" + count).attr("href", href);
        $("#payment" + count).colorbox({
            iframe: true,
            width: "70%",
            height: "80%",
            title: "Payments"
        });
    }
    function checkForNumberOnly(obj) {
        if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
            sampleAlert("This field should be Numeric");
            obj.value = "";
        } else {
            $(obj).val(Number($(obj).val()).toFixed(2))
        }
    }
    function getBalanceAmt(chargeId) {
        var id = 'chargeAmount' + chargeId;
        // var balanceAmt = 0.00;
        var manualTotal = 0;
        var manualAmt = +($('#' + id).val());
        //  var chargeAmt = +($('#'+id).parent().prev().prev().prev().html().replace(/^\s\s*/, '').replace(/\s\s*$/, ''));
        // var paidAmt = +($('#'+id).parent().prev().prev().html().replace(/^\s\s*/, '').replace(/\s\s*$/, ''));

        //        if(paidAmt != null && paidAmt != ""){
        //            balanceAmt = +(chargeAmt - paidAmt - manualAmt);
        //        } else {
        //            balanceAmt = +(chargeAmt - manualAmt);
        //        }
        //  $('#'+id).parent().prev().html(balanceAmt.toFixed(2));
        if (manualAmt !== 0.00) {
            $('#chargesCheck' + chargeId).attr('checked', 'checked');
        } else {
            $('#chargesCheck' + chargeId).removeAttr('checked');
        }
        var cheQueAmt = $("#amount").val();
        if (cheQueAmt === null && cheQueAmt === '') {
            $('.manualAmt').each(function () {
                var amtPaid = +($(this).val().replace(/^\s\s*/, '').replace(/\s\s*$/, ''));
                manualTotal += amtPaid;
            });
            $("#amount").val(manualTotal.toFixed(2));
        }
        $('#chargeAmount' + id).removeClass("textlabelsBoldForTextBox ");
        $("#chargeAmount" + chargeId).addClass("textlabelsBoldForTextBoxDisabledLook readOnly");
        $('#chargeAmount' + chargeId).attr("readOnly", true);
    }
    function editimgCharges(id) {
        $("#chargeAmount" + id).removeClass("textlabelsBoldForTextBoxDisabledLook readOnly");
        $('#chargeAmount' + id).removeAttr("readOnly");
        $('#chargeAmount' + id).addClass("textlabelsBoldForTextBox");
    }
    function setWarning(id) {
        $('#' + id).css("border-color", "red");
    }
    function fillCheckNumberAndAmount(obj) {
        var importCreditAccount = $('#importCreditAccount').val();
        if (obj.value === 'Credit Account') {
            if (importCreditAccount !== '') {
                $('#checkNumber').val(importCreditAccount);
                $('#amount').val($('#totalBalanceAmt').val().replace(",", ""));
            }
        } else if ($('#checkNumber').val() === importCreditAccount) {
            $('#checkNumber').val("");
            $('#amount').val("");
        }
    }
    function updatePaymentType(val, id) {
        var fileNumberId = $("#fileNumberId").val();
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingAcTransDAO",
                methodName: "updatePaymentType",
                param1: val.value,
                param2: id,
                param3: fileNumberId,
                request: "true",
                dataType: "json"
            },
            async: false,
            success: function (data) {
                $('#formChangeFlag').val("true");
                parent.$('#formChangeFlag').val("true");
            }
        });
    }
    function scan(documentId, screenName, moduleName, importFlag) {
            GB_show("Scan", "/logisoft/scan.do?screenName=" + screenName + "&documentId=" + documentId + "&importFlag=" + importFlag + "&quoteBookingName=" + moduleName + "&", 250, 850);
    }
</script>
<body style="background:#ffffff">
    <cong:form id="lclImportPaymentForm" name="lclImportPaymentForm" action="/lclImportPayment">
        <cong:table style="width:99%;border: 1px solid #dcdcdc;" border="0" >
            <input type="hidden" id="fileNumberId" name="fileNumberId" value="${lclImportPaymentForm.fileNumberId}"/>
            <input type="hidden" id="methodName" name="methodName"/>
            <input type="hidden" id="bookingAcId" name="bookingAcId"/>
            <input type="hidden" id="chargesAmount" name="chargesAmount"/>
            <input type="hidden" id="bkgTransid" name="bkgTransid"/>
            <input type="hidden" id="totalchrgeAmount" name="totalchrgeAmount"/>
            <input type="hidden" id="formChangeFlag" name="formChangeFlag" value="${lclImportPaymentForm.formChangeFlag}"/>
            <input type="hidden" id="fileNumber" name="fileNumber" value="${lclImportPaymentForm.fileNumber}"/>
            <input type="hidden" id="importCreditStatus" name="importCreditStatus" value="${importCreditStatus}"/>
            <input type="hidden" id="importCreditAccount" name="importCreditAccount" value="${importCreditAccount}"/>
            <input type="hidden" id="totalBalanceAmt" name="totalBalanceAmt" value="${totalBalanceAmt}"/>
            <input type="hidden" id="isDisableCheckCopy" name="isDisableCheckCopy" value="${isDisableCheckCopy}"/>
            <input type="hidden" name="screenName" id="screenName" value="LCL FILE"/>
            <input type="hidden" name="moduleId" id="moduleId" value="${lclImportPaymentForm.fileNumber}"/>
            <input type="hidden" name="moduleName" id="moduleName" value="Imports"/>
            <cong:tr styleClass="tableHeadingNew">
                <cong:td width="70%" colspan="5">Payments:<span style="color: red">${lclImportPaymentForm.fileNumber}</span></cong:td>
                <cong:td style="float:right; padding-right:25px">
                    <div  class=" ${scanAttachFlag ? "green-background":"button-style1"} disabledButton" id="scanattach"
                          onclick="scan('${lclImportPaymentForm.fileNumber}', 'LCL IMPORTS DR', 'BOOKING', 'false');">
                        Scan/Attach
                    </div>
                    <div class="button-style1" onclick="payAllAmt();">Pay All</div>
                    <div class="button-style1" onclick="savePayment();">Save</div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:tr><cong:td colspan="6"></cong:td></cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Check/Reference Number</cong:td>
                <cong:td><cong:text name="checkNumber" id="checkNumber" styleClass="text mandatory textuppercaseLetter"/></cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Payment Amount</cong:td>
                <cong:td>
                    <cong:text name="amount" id="amount" styleClass="textlabelsBoldForTextBox mandatory" onchange="checkForNumberOnly(this)"/>
                </cong:td>
                <cong:td rowspan="2" valign="top" styleClass="textlabelsBoldforlcl">Comments</cong:td>
                <cong:td rowspan="2" valign="top" styleClass="textlabelsBoldforlcl" style="float:right; padding-right:25px">
                    <html:textarea cols="10" rows="30" styleClass="refusedTextarea textlabelsBoldForTextBox"
                                   property="remarks" style="resize:none;" />
                </cong:td>
            </cong:tr> <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Paid By</cong:td>
                <cong:td>
                    <cong:text name="paidBy" id="paidBy" styleClass="textuppercaseLetter"/>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Paid Date</cong:td>
                <cong:td>
                    <cong:calendarNew name="paidDate" id="paidDate" styleClass="text"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Payment Type
                    <cong:radio name="paymentType" id="paymentTypeCH" styleClass="textlabelsBoldforlcl" value="Check/Cash" onclick="fillCheckNumberAndAmount(this)">Check/Cash</cong:radio>
                    <cong:radio name="paymentType" id="paymentTypeAC" styleClass="textlabelsBoldforlcl" value="ACH/Wire/Electronic Payments" onclick="fillCheckNumberAndAmount(this)">ACH/Wire/Electronic Payments</cong:radio>
                    <cong:radio name="paymentType" id="paymentTypeCC" styleClass="textlabelsBoldforlcl" value="Credit Card" onclick="fillCheckNumberAndAmount(this)">Credit Card</cong:radio>
                    <cong:radio name="paymentType" id="paymentTypeIC" styleClass="textlabelsBoldforlcl" value="Credit Account" onclick="fillCheckNumberAndAmount(this)">Import Credit Account</cong:radio>
                    <cong:radio name="paymentType" id="paymentTypeCO" styleClass="textlabelsBoldforlcl" value="Check Copy" onclick="fillCheckNumberAndAmount(this)">Check Copy</cong:radio>
                </cong:td>

            </cong:tr>
            <cong:tr><cong:td colspan="6"></cong:td></cong:tr>
        </cong:table>
        <c:if test="${not empty lclBookingAcTransesList}">
            <c:set var="count" value="0"></c:set>
            <display:table id="trans" class="dataTable" name="${lclBookingAcTransesList}">
                <display:column title="Check Number" property="referenceNo"></display:column>
                <fmt:formatDate value="${trans.transDatetime}" pattern="dd-MMM-yyyy" var="transDate"/>
                <display:column title="Paid By / Paid Date">${fn:toUpperCase(trans.referenceName)} / ${transDate}</display:column>
                <display:column title="Amount" property="amount"></display:column>
                <fmt:formatDate value="${trans.modifiedDatetime}" pattern="dd-MMM-yyyy" var="enteredDate"/>
                <display:column title="Trans Date">${fn:toUpperCase(trans.modifiedBy.loginName)} / ${enteredDate}</display:column>
                <display:column title="Payment Type">
                    <c:choose>
                        <c:when test="${trans.paymentType eq 'Check Copy'}" >
                            <c:choose>
                                <c:when test="${importCreditStatus eq 'In Good Standing' || importCreditStatus eq 'Credit Hold'}">
                                    <html:select property="paymentType" styleClass="smallDropDown textlabelsBoldforlcl"
                                                 styleId="payTypes" style="width:100px" value="${trans.paymentType}" onchange="updatePaymentType(this,'${trans.id}')">
                                        <html:option value="Check/Cash">Check/Cash</html:option>
                                        <html:option value="ACH/Wire/Electronic Payments">ACH/Wire/Electronic Payments</html:option>
                                        <html:option value="Credit Card" >Credit Card</html:option>
                                        <html:option value="Credit Account">Import Credit Account</html:option>
                                        <html:option value="Check Copy">Check Copy</html:option>
                                    </html:select>   
                                </c:when>  
                                <c:otherwise>
                                    <html:select property="paymentType" styleClass="smallDropDown textlabelsBoldforlcl"
                                                 styleId="payTypes" style="width:100px" value="${trans.paymentType}" onchange="updatePaymentType(this,'${trans.id}')">
                                        <html:option value="Check/Cash">Check/Cash</html:option>
                                        <html:option value="ACH/Wire/Electronic Payments">ACH/Wire/Electronic Payments</html:option>
                                        <html:option value="Credit Card" >Credit Card</html:option>
                                        <html:option value="Check Copy">Check Copy</html:option>
                                    </html:select>  
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            ${trans.paymentType}
                        </c:otherwise>
                    </c:choose>
                </display:column>
                <display:column title="Action" style="width:80Px;">
                    <c:if test="${not empty trans.remarks}">
                        <img src="${path}/img/icons/iicon.png" width="16" height="16" alt="remarks" title="${trans.remarks}" id="payremarks"/></c:if>
                    <img alt="More Information" src="${path}/img/icons/pubserv.gif" border="0" id="payment${count}"
                         title="More Information" onclick="viewPayment('${path}', '${count}', '${trans.id}');"/>
                    <img src="${path}/images/error.png" style="cursor:pointer" width="13" height="13" alt="delete" title="Delete Payment"
                         onclick="deletePayment('Are you sure you want to delete?', '${trans.id}');"/>
                </display:column>
                <c:set var="count" value="${count+1}"></c:set>
            </display:table>
        </c:if>
        <c:if test="${not empty lclBookingAcList}">
            <display:table id="charges" class="dataTable" name="${lclBookingAcList}">
                <display:column style="width:1Px;">
                    <c:if test="${charges.balanceAmt ne '0.00'}">
                        <input type="checkbox" class="chargesChecked" id="chargesCheck${charges.id}" value="${charges.id}"
                               onclick="totalCharges('${charges.id}', '${charges.totalAmt}', '${charges.paidAmt}');"/>
                    </c:if>
                </display:column>
                <display:column title="Charge Code" style="width:100Px;">${charges.chargeCode}
                    <input type="hidden" id="hiddenChargeId${charges.id}" name="hiddenChargeId${charges.id}" value="${charges.id}"/>
                </display:column>
                <display:column title="Charge Amount" style="width:100Px;" class="cAmt">${charges.totalAmt}</display:column>
                <display:column title="Amount Paid" style="width:100Px;" class="pAmt">${charges.paidAmt}</display:column>
                <display:column title="Balance Amt" style="width:100Px;" class="bAmt">${charges.balanceAmt}</display:column>
                <display:column title="" style="width:100Px;">
                    <c:if test="${charges.balanceAmt ne '0.00'}">
                        <input type="text" class="manualAmt textlabelsBoldForTextBoxDisabledLook"  readOnly="true" id="chargeAmount${charges.id}" onkeyup="checkForNumberOnly(this);"
                               onblur="validateAmt('${charges.id}', '${charges.totalAmt}', '${charges.paidAmt}');
                                       getBalanceAmt('${charges.id}');"/>
                        <img src="${path}/images/edit.png" class="editAmt" style="display: none" width="16" height="16" id="editImgCharge${charges.id}" onclick="editimgCharges('${charges.id}');"/>
                    </c:if>
                </display:column>
            </display:table>
            <table width="100%"><tr>
                    <td width="11%"></td>
                    <td width="10%"></td>
                    <td  width="18%">--------------</td>
                    <td  width="18%">--------------</td>
                    <td  width="10%">--------------</td>
                    <td ></td></tr>
                <tr>
                    <td colspan="2" align="center">
                        <b style="color: green;font-size: medium;"  class="headerlabel">TOTAL($-USD)</b></td>
                    <td style="color: red;font-size: medium;font-weight:bolder">${totalAmt}</td>
                    <td style="color: red;font-size: medium;font-weight:bolder">
                        <c:set var="totalPaidAmt" value="0"/>
                        <c:forEach var="lbac" items="${lclBookingAcList}">
                            <c:set var="totalPaidAmt" value="${totalPaidAmt+lbac.paidAmt}"/>
                        </c:forEach>
                        ${totalPaidAmt}
                    </td>
                    <td style="color: red;font-size: medium;font-weight:bolder">
                        ${totalBalanceAmt}
                    </td>
                    <td ></td></tr></table>
                </c:if>
            </cong:form></body>
