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
        <cong:form name="lclQuoteCostAndChargeForm" id="lclQuoteCostAndChargeForm" action="lclQuoteCostAndCharge.do">
            <cong:hidden name="fileNumber" value="${lclQuoteCostAndChargeForm.fileNumber}"/>
            <cong:hidden name="id" value="${id}"/>
            <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
                Charges for File No: <span class="fileNo">${lclQuoteCostAndChargeForm.fileNumber}</span>&nbsp;
                <span id="refusedValidate"/>
            </cong:div>
            <br><br>
            <cong:table width="100%" border="0">
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Charge Amount(Sell)</cong:td>
                    <cong:td>
                        <cong:text styleClass="text twoDigitDecFormat" style="width:76px"
                                   name="dollarAmount" id="dollarAmount" onkeyup="checkForNumberAndDecimal(this);"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan='2'>
                        <input type="button" class="button-style1" value="Save"  onclick="submitCharge('addQuoteDocumCharge');"/>
                        <input type="button" class="button-style1" value="Cancel"  onclick="closeCharge();"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <cong:hidden name="methodName" id="methodName"/>
        </cong:form>
    </cong:div>
    <script type="text/javascript">
        function submitCharge(methodName){
            submitAjaxForm(methodName,'#lclQuoteCostAndChargeForm','#chargeDesc');
        }
        function submitAjaxForm(methodName,formName,selector){
            showProgressBar();
            $("#methodName").val(methodName);
            var params = $(formName).serialize();
            parent.$('#documSave').val('S');
            var fileNumberId = parent.document.getElementById('fileNumberId').value;
            var billingType=$("input:radio[name='pcBoth']:checked").val();
            params+="&fileNumberId="+fileNumberId+"&billingType="+billingType;
            $.post($(formName).attr("action"),params,
            function(data) {
                $(selector).html(data);
                $(selector,window.parent.document).html(data);
                hideProgressBar();
                parent.$.fn.colorbox.close();
            });
        }
        function checkForNumberAndDecimal(obj){
            var result;
            if(!/^\d*(\.\d{0,6})?$/.test(obj.value)){
                obj.value="";
                $('#refusedValidate').html('<span style="color:blue; font-size:12px">This should be Numeric</span>')
                $("#dollarAmount").css("border-color","red");
                $("#warning").show();
                return false;
            }else{
                $('#refusedValidate').html("");
            }
        }
        function closeCharge(){
            var documentation=parent.$('input:radio[name=documentation]:checked').val();
            if(documentation=="Y"){
                parent.$('#docNoQ').attr('checked',true);
            }
            parent.$.colorbox.close();

        }
    </script>
</body>

