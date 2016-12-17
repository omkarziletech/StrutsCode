<%--
    Document   : lclImportPayment
    Created on : 29 Apr, 2013, 3:56:26 PM
    Author     : Meiyazhakan
--%>
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript">
    function exitCharges() {
        parent.$.colorbox.close();
    }
    function saveCharges() {
        if ($('#oldAmt').val() !== '' && $('#oldAmt').val() === $('#amount').val() && $('#fileStatus').val() === 'M') {
            exitCharges();
        } else {
            showLoading();
            var fileNumberId = $('#fileNumberId').val();
            $("#methodName").val("saveCharges");
            var params = $("#lclImportPaymentForm").serialize();
            params += "&fileNumberId=" + fileNumberId + "&moduleName=Imports";
            $.post($("#lclImportPaymentForm").attr("action"), params,
                    function(data) {
                        parent.$('#correctionNotice').addClass('green-background');
                        parent.$('#correctionNoticeBottom').addClass('green-background');
                        $("#chargeDesc").html(data);
                        $("#chargeDesc", window.parent.document).html(data);
                        parent.$.fn.colorbox.close();
                    });
        }
    }
    function checkForNumberOnly(obj) {
        if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
            sampleAlert("This field should be Numeric");
            obj.value = "";
            obj.value = $('#hiddenStorageValues').val();
        }
    }
</script>
<body style="background:#ffffff">
    <cong:form id="lclImportPaymentForm" name="lclImportPaymentForm" action="/lclImpStorageCharge">
        <table style="width:99%;border: 1px solid #dcdcdc;" border="0" >
            <input type="hidden" id="fileNumberId" name="fileNumberId" value="${lclImportPaymentForm.fileNumberId}"/>
            <input type="hidden" id="fileStatus" name="fileStatus" value="${fileStatus}"/>
            <input type="hidden" id="oldAmt" name="oldAmt" value="${oldAmt}"/>
            <input type="hidden" id="methodName" name="methodName"/>
            <input type="hidden" id="fileNumber" name="fileNumber" value="${lclImportPaymentForm.fileNumber}"/>
            <tr class="tableHeadingNew">
                <td>Storage Charge:<span style="color:red">${lclImportPaymentForm.fileNumber}</span></td>
                <td>Number Of Weeks:<span style="color:red"><c:if test="${noOfWeeks ne 0}">${noOfWeeks}</c:if></span></td>
                </tr>
                <tr><td colspan="2">
                    <c:if test="${not empty storageChargelist}">
                        <display:table id="storageCharges" class="dataTable" name="${storageChargelist}">
                            <display:column title="">${storageCharges.label1}</display:column>
                            <display:column title="Charge Code">${storageCharges.arglMapping.chargeCode}</display:column>
                            <display:column title="Charge Desc">${storageCharges.arglMapping.chargeDescriptions}</display:column>
                            <display:column title="Charge Amount">
                                <input type="text" id="amount" name="amount" style="width:100Px;" onkeyup="checkForNumberOnly(this);"
                                       value="${fn:replace(storageCharges.arAmount,',', '')}"/>
                                <input type="hidden" id="hiddenStorageValues" value="${storageCharges.arAmount}"/>
                            </display:column>
                            <display:column title="Bill To Party">
                                <c:if test="${not empty storageCharges.arBillToParty}">
                                    <c:choose>
                                        <c:when test="${storageCharges.arBillToParty eq 'A'}">
                                            Agent
                                        </c:when>
                                        <c:when test="${storageCharges.arBillToParty eq 'T'}">
                                            Third Party
                                        </c:when>
                                        <c:when test="${storageCharges.arBillToParty eq 'C'}">
                                            Consignee
                                        </c:when>
                                        <c:when test="${chargesTable.arBillToParty eq 'N'}">
                                            Notify Party
                                        </c:when>
                                    </c:choose>
                                </c:if>
                            </display:column>
                            <display:column title="Rate">${storageCharges.label2}</display:column>
                        </display:table>
                    </td></tr>
                <tr><td align="center" colspan="2">
                        <div class="button-style1"  onclick="saveCharges();">Submit</div>
                        <div class="button-style1"  onclick="exitCharges();">Abort</div>
                    </td></tr>
                </c:if>
        </table>
    </cong:form></body>
