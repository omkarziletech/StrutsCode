<%-- 
    Document   : lclDocumCharge
    Created on : Nov 2, 2012, 5:09:44 PM
    Author     : mohana
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="colorBox.jsp" %>
<body style="background:#ffffff">
    <cong:div style="width:100%; float:left; ">
        <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
            Charges for File No: <span class="fileNo">${lclBlCostAndChargeForm.fileNumber} </span><span id="refusedValidate"/>
        </cong:div>
        <br><br>
        <cong:form name="lclBlCostAndChargeForm" id="lclBlCostAndChargeForm" action="lclBlCostAndCharge.do">
            <cong:hidden name="id" value="${id}"/>
            <cong:hidden name="fileNumber" id="fileNumber" value="${lclBlCostAndChargeForm.fileNumber}"/>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclBlCostAndChargeForm.fileNumberId}"/>
            <cong:hidden name="billingType" id="billingType" value="${lclBlCostAndChargeForm.billingType}"/>
            <cong:hidden name="billToParty" id="billToParty" value="${lclBlCostAndChargeForm.billToParty}"/>
            <cong:table width="100%" border="0">
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Charge Amount(Sell)</cong:td>
                    <cong:td>
                        <cong:text styleClass="text twoDigitDecFormat" style="width:76px"
                                   name="docChargeAmount" id="docChargeAmount" onkeyup="checkForNumberAndDecimal(this);"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                    </cong:td>
                    <cong:td>
                        <input type="button" class="button-style1" value="Save" onclick="submitCharge('addDocumCharge')"/>
                        <input type="button" class="button-style1" value="Cancel"  onclick="closeCharge();"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <cong:hidden name="methodName" id="methodName"/>
        </cong:form>
    </cong:div>
    <script type="text/javascript">
        function submitCharge(methodName) {
            submitAjaxForm(methodName, '#lclBlCostAndChargeForm', '#chargeBlDesc');
        }
        function submitAjaxForm(methodName, formName, selector) {
            showProgressBar();
            $("#methodName").val(methodName);
            var params = $(formName).serialize();
            params += "&moduleName=Exports";
            parent.$('#documSave').val('S');
            $.post($(formName).attr("action"), params,
            function(data) {
                hideProgressBar();
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                parent.$.fn.colorbox.close();
            });
        }
        function checkForNumberAndDecimal(obj) {
            if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
                obj.value = "";
                $('#refusedValidate').html('<span style="color:red; font-size:12px">This should be Numeric</span>')
                $("#labelsCount").css("border-color", "red");
                $("#warning").parent.show();
                return false;
            }
            else {
                $('#refusedValidate').html("");
            }
        }
        function closeCharge() {
            var documentation = parent.$('input:radio[name=documentation]:checked').val();
            if (documentation === "Y") {
                parent.$('#docNo').attr('checked', true);
            }
            parent.$.colorbox.close();
        }
    </script>
</body>

