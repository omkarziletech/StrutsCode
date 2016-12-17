<%@include file="init.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>

<body style="background:#ffffff">
    <cong:div style="width:100%; float:left; ">
        <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
            Charges for File No:<span class="fileNo">${fileNumber}</span>
        </cong:div><br><br>
        <cong:form name="lclBlCostAndChargeForm" id="lclBlCostAndChargeForm" action="/lclBlCostAndCharge.do">
            <input type="hidden" id="noFrtFwd" name="noFrtFwd" value="${frtFwdAcct}"/>
            <cong:hidden name="id" value="${id}"/>
            <cong:hidden name="fileNumber" value="${fileNumber}"/>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${fileNumberId}"/>
            <cong:hidden name="manualEntry" id="manualEntry" value="${lclBlCostAndChargeForm.manualEntry}"/>
            <input type="hidden" id="existChargeCodeId" name="existChargeCodeId" value="${lclBlCostAndChargeForm.chargesCodeId}"/>
            <input type="hidden" id="existChargeCode" name="existChargeCode" value="${lclBlCostAndChargeForm.chargesCode}"/>
            <c:set var="shipmentType" value="LCLE"/>
            <c:set var="manual" value="${lclBlCostAndChargeForm.manualEntry ? true:false}"/>

            <cong:table width="100%" border="0">
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Code&nbsp;</cong:td>
                    <cong:td>
                        <c:choose>
                            <c:when test="${manual}">
                                <cong:autocompletor name="chargesCode" id="chargesCode" template="two" query="CHARGE_CODE" fields="NULL,chargesCodeId" shouldMatch="true" 
                                                    params="LCLE"  container="NULL" styleClass="text mandatory"  width="400"  scrollHeight="150" callback="checkChargeCode()"/>
                            </c:when>
                            <c:otherwise>
                                <cong:text name="chargesCode" id="chargesCode" styleClass="text twoDigitDecFormat mandatory ${!manual? 'text-readonly':''}" readOnly="${!manual}"
                                           style="width:76px"   container="NULL"/>
                            </c:otherwise>
                        </c:choose>
                        <cong:hidden name="chargesCodeId" id="chargesCodeId"/>
                        <cong:hidden name="chargeCode" id="chargeCode" value="${chargeCode}"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">UOM</cong:td>
                    <cong:td>
                        <cong:radio name="uom" value="M" id="metric" onclick="showValue();" container="NULL"/>M
                        <cong:radio name="uom" value="I" id="imperial" onclick="showValue();" container="NULL"/>I
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td><br/></cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Flat Rate Amount&nbsp;</cong:td>
                    <cong:td><cong:text styleClass="text twoDigitDecFormat ${!manual? 'text-readonly':''}" readOnly="${!manual}" style="width:76px" 
                                        name="flatRateAmount" id="flatRateAmount" container="NULL"  onkeyup="checkForNumberAndDecimal(this);rateReadonly();"/>
                        <span class="textlabelsBoldforlcl">(flat rate) </span>
                    </cong:td>
                    <cong:td  styleClass="textlabelsBoldforlcl">Measure&nbsp;</cong:td>
                    <cong:td>
                        <cong:text name="measure" styleClass="text twoDigitDecFormat ${!manual? 'text-readonly':''}" readOnly="${!manual}" style="width:76px" 
                                   id="measure" container="NULL" onkeyup="checkForNumberAndDecimal(this);uomReadonly();"/>
                        <cong:label id="msr" text=" /CBM" styleClass="textlabelsBoldforlcl"></cong:label>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Weight&nbsp;</cong:td>
                    <cong:td>
                        <cong:text name="weight" styleClass="text twoDigitDecFormat ${!manual? 'text-readonly':''}" readOnly="${!manual}" style="width:76px" 
                                   id="weight" container="NULL"  onkeyup="checkForNumberAndDecimal(this);uomReadonly();"/>
                        <cong:label id="wei" text="/1000 KGS" styleClass="textlabelsBoldforlcl"></cong:label>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Minimum&nbsp;</cong:td>
                    <cong:td>
                        <cong:text name="minimum" styleClass="text twoDigitDecFormat ${!manual? 'text-readonly':''}" readOnly="${!manual}" style="width:76px" 
                                   id="minimum" container="NULL"  onkeyup="checkForNumberAndDecimal(this);uomReadonly();" />
                    </cong:td>
                </cong:tr>
                <cong:tr><cong:td><br/></cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Bill To Party&nbsp;</cong:td>
                    <cong:td>
                        <html:select property="billingType" styleId="billToParty" style="width:134px" styleClass="smallDropDown mandatory textlabelsBoldforlcl" >
                            <html:optionsCollection name="billToPartyList"/>
                        </html:select>
                        <input type="hidden" name="existBillToParty" id="existBillingType" value="${lclBlCostAndChargeForm.billingType}"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Bill this Charge On&nbsp;</cong:td>
                    <cong:td>
                        <input type="radio" name="billCharge" id="billChargeInvoice" value="IV"/>Invoice
                        <input type="radio" name="billCharge" id="billChargeBL" value="BL" checked="yes"/>B/L
                    </cong:td>
                </cong:tr>
                <cong:tr><cong:td><br/></cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Cost Amount&nbsp;</cong:td>
                    <cong:td><cong:text container="NULL" styleClass="text-readonly" readOnly="true" style="width:76px" value="" name="costAmount" id="costAmount"/>
                        <span class="textlabelsBoldforlcl">(flat rate) </span>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Measure&nbsp;</cong:td>
                    <cong:td><cong:text container="NULL" name="measure" styleClass="text-readonly" readOnly="true" value="" style="width:76px" id="measureForCost" />
                        <cong:label styleClass="textlabelsBoldforlcl" id="msr" text=" /CBM"></cong:label>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Weight&nbsp;</cong:td>
                    <cong:td>
                        <cong:text name="weight" styleClass="text-readonly " readOnly="true" style="width:76px" value="" id="weightForCost" />
                        <cong:label styleClass="textlabelsBoldforlcl" id="wei" text="/1000 KGS"></cong:label>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Minimum&nbsp;</cong:td>
                    <cong:td>
                        <cong:text container="NULL" name="minimum" styleClass="text-readonly" readOnly="true" value="" style="width:76px" id="minimumForCost"  />
                    </cong:td>
                </cong:tr> 
                <cong:tr><cong:td><br/></cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Vendor Name</cong:td>
                    <cong:autocompletor  name="thirdPartyname" styleClass="text-readonly"  readOnly="true"  id="thirdPartyname" 
                                         fields="thirdpartyaccountNo" shouldMatch="true" width="600" query="VENDOR" template="tradingPartner" 
                                         scrollHeight="300px"/>
                    <cong:td styleClass="textlabelsBoldforlcl" align="right">Vendor#</cong:td>
                    <cong:td><cong:text container="NULL" name="thirdpartyaccountNo" readOnly="true" id="thirdpartyaccountNo" styleClass="text-readonly"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Invoice Number&nbsp;</cong:td>
                    <cong:td><cong:text container="NULL" styleClass="text text-readonly" readOnly="true"  maxlength="25" name="invoiceNumber" id="invoiceNumber" 
                                        onkeyup="checkForNumberAndDecimal(this)"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Adjustment&nbsp;</cong:td>
                    <cong:td><cong:text styleClass="text ${manual ? 'text-readonly':''}" readOnly="${manual}" name="adjustmentAmount" 
                                        id="adjustmentAmount" container="null" style="width:76px" onchange="allowOnlyWholeNumbers(this);"/></cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Comments&nbsp;</cong:td>
                    <cong:td colspan="3">
                        <cong:textarea rows="3" cols="30" styleClass="${manual ? 'text-readonly':''}" readOnly="${manual}" name="adjustmentComment" 
                                       id="adjustmentComment" style="margin-top: 4px;" value="${lclBlCostAndChargeForm.adjustmentComment}">

                        </cong:textarea>
                    </cong:td>
                </cong:tr>
            </cong:table><br>
            <cong:table>
                <cong:tr>
                    <cong:td >
                        <input type="button" class="button-style1" value="Save" id="saveCode" onclick="saveBLCharge();"/>
                        <input type="button" class="button-style1" value="Cancel" id="cancel" onclick="cancelBLCharge();"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="destination" id="destination"/>
            <input type="hidden" name="blAcId" id="blAcId" value="${blAcId}"/>
            <input type="hidden" name="engmet" id="engmet" value="${engmet}"/>
        </cong:form>
    </cong:div>
    <script type="text/javascript">
        $(document).ready(function () {
            var blAcId = $('#blAcId').val();
            if (blAcId == null || blAcId == "" || blAcId == "0") {
                if ($('#engmet').val() == "E") {
                    // $('#imperial').attr('checked', true);
                }
            }
        });

        function cancelBLCharge() {
            parent.$.colorbox.close();
        }
        function saveBLCharge() {
            var adjustmentResult = +$("#adjustmentAmount").val() + +$("#flatRateAmount").val();
            var chargesCode = $('#chargesCode').val();
            $(".required").each(function () {
                if ($(this).val().length == 0) {
                    sampleAlert('This field is required');
                    error = false;
                    $(this).css("border-color", "red");
                    $(this).focus();
                    return false;
                }
            });
            if ($("#billToParty").val() === '') {
                sampleAlert('Bill To Party is required');
                return false;
            }else{
             switch ($("#billToParty").val()) {
                case 'F':
                    if (parent.$("#forwarderName").val() === '') {
                        $.prompt("Please Select Forwarder Name And Number");
                        $("#billToParty").val($('#existBillingType').val());
                        return false;
                    }
                    else if (filterNoFreightForwarderAc()) {
                       $.prompt("Please Select Valid Forwarder Account");
                       $("#billToParty").val($('#existBillingType').val());
                       return false;
                    }
                    break;
                case 'S':
                    if (parent.$("#shipperCode").val() === '') {
                        $.prompt("Please Select Shipper Name And Number");
                        $("#billToParty").val($('#existBillingType').val());
                        return false;
                    }
                    break;
                case 'T':
                    if (parent.$("#thirdPartyname").val() === '') {
                        $.prompt("Please Select Thirty Party Name And Number");
                        $("#billToParty").val($('#existBillingType').val());
                        return false;
                    }
                    break;
                case 'A':
                    if (parent.$("#agentName").val() === '') {
                        $.prompt("Please Select Agent Name And Number");
                        $("#billToParty").val($('#existBillingType').val());
                        return false;
                    }
                    break;
            }}
            if (chargesCode === null || chargesCode === "") {
                sampleAlert('Code is required');
                $('#chargesCode').css("border-color", "red");
                return false;
            }
            if ($("#adjustmentAmount").val() !== "0.00" && $("#adjustmentAmount").val().trim() !== "" && $("#adjustmentComment").val() === '') {
                sampleAlert('Comments is required');
                $('#adjustmentComment').css("border-color", "red");
                return false;
            }
//            if (adjustmentResult < "0.00" ) {
//                sampleAlert("Adjustment Result Amount can't be a Negative Value");
//                $('#adjustmentAmount').css("border-color", "red");
//                return false;
//            }
            if (validateBlManualCharges()) {
                submitAjaxForm('addCharges', '#lclBlCostAndChargeForm', '#chargeBlDesc');
                if(chargesCode == 'INSURE'){
                        parent.$('#printInsuranceY').attr('disabled', false);
                        parent.$('#printInsuranceN').attr('disabled', false); 
                    }
            }
        }

        function validateBlManualCharges() {
            var flatrate = $('#flatRateAmount').val();
            var weight = $('#weight').val();
            var measure = $('#measure').val();
            var minimum = $('#minimum').val();
            var manualEntry = $("#manualEntry").val();
            if (manualEntry != 'false') {
                if (flatrate == "")
                {
                    if (weight == "" || measure == "" || minimum == "") {
                        sampleAlert('Combination of Flat Rate Amount field is required or Combination of Measure,Weight and Minimum fields are required');
                        if (weight != "" && measure == "" && minimum == "") {
                            $("#measure").css("border-color", "red");
                            $("#minimum").css("border-color", "red");
                            return false;
                        }
                        else if (weight != "" && measure != "" && minimum == "") {
                            $("#minimum").css("border-color", "red");
                            return false;
                        }
                        else if (weight == "" && measure != "" && minimum == "") {
                            $("#weight").css("border-color", "red");
                            $("#minimum").css("border-color", "red");
                            return false;
                        }
                        else if (weight == "" && measure != "" && minimum != "") {
                            $("#weight").css("border-color", "red");
                            return false;
                        }
                        else if (weight == "" && measure == "" && minimum != "") {
                            $("#weight").css("border-color", "red");
                            $("#measure").css("border-color", "red");
                            return false;
                        }
                        else if (weight != "" && measure == "" && minimum != "") {
                            $("#measure").css("border-color", "red");
                            return false;
                        }
                        return false;
                    }
                }
            }
            return true;
        }
        function submitAjaxForm(methodName, formName, selector) {
            showProgressBar();
            $("#methodName").val(methodName);
            var params = $(formName).serialize();
            var fileNumberId = document.getElementById('fileNumberId').value;
            params += "&fileNumberId=" + fileNumberId;
            $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                parent.$.fn.colorbox.close();
                hideProgressBar();
            });
        }
        function sampleAlert(txt) {
            $.prompt(txt);
        }
        function checkForNumberAndDecimal(obj) {
            var result;
            if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
                obj.value = "";
                sampleAlert("This field should be Numeric");

            }
        }
        function showValue() {
            if ($('#metric').is(":checked")) {
                document.getElementById('wei').innerHTML = "/1000 KGS"
                document.getElementById('msr').innerHTML = "/CBM"
            }
            if ($('#imperial').is(":checked")) {
                document.getElementById('wei').innerHTML = "/100 LBS"
                document.getElementById('msr').innerHTML = "/CFT"
            }
        }

        function rateReadonly() {
            var flatRate = $('#flatRateAmount').val();
            if (flatRate != "") {
                $('#weight').addClass('text-readonly');
                $('#weight').attr('readonly', true);
                $('#measure').addClass('text-readonly');
                $('#measure').attr('readonly', true);
                $('#minimum').addClass('text-readonly');
                $('#minimum').attr('readonly', true);
            }
            if (flatRate == "") {
                $('#weight').removeClass('text-readonly');
                $('#weight').attr('readonly', false);
                $('#measure').removeClass('text-readonly');
                $('#measure').attr('readonly', false);
                $('#minimum').removeClass('text-readonly');
                $('#minimum').attr('readonly', false);
            }
        }

        function uomReadonly() {
            var weight = $('#weight').val();
            var measure = $('#measure').val();
            var minimum = $('#minimum').val();
            if (minimum != "" || measure != "" || weight != "") {
                $('#flatRateAmount').addClass('text-readonly');
                $('#flatRateAmount').attr('readonly', true);
            }
            if (minimum == "" && measure == "" && weight == "") {
                $('#flatRateAmount').removeClass('text-readonly');
                $('#flatRateAmount').attr('readonly', false);
            }
        }

        function checkChargeCode() {
            var chargeCode = $('#chargesCode').val();
            var editchargeCode = $("#chargeCode").val();
            var fileID = $('#fileNumberId').val();
            if (chargeCode != '') {
                if($('#noFrtFwd').val()==='fwd' && chargeCode==='ADVFF'){
                    var forwarderCode = parent.$('#forwarderCode').val();
                    $.prompt("<span style=color:red>"+chargeCode+"</span> Charge Code will not allow for this Freight Forwader Account <span style=color:red>"+forwarderCode+"</span>");
                    $("#chargesCode").val($('#existChargeCode').val());
                    $("#chargesCodeId").val($('#existChargeCodeId').val());
                    return false;
                }
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "chargeCodeValidateForBl",
                        param1: chargeCode,
                        param2: editchargeCode,
                        param3: fileID,
                        dataType: "json"
                    },
                    success: function (data) {
                        if (data == true) {
                            $("#chargesCode").val('');
                            $("#chargesCode").css("border-color", "red");
                            $.prompt("Charge Code is already exists.Please Select different Charge Code");
                            return false;
                        }
                    }
                });
            }
        }
    
    function filterNoFreightForwarderAc() {
         var forwardAcctNo = parent.$("#forwarderCode").val();
         var flag;
         $.ajaxx({
         dataType: "json",
         data: {
            className: "com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO",
            methodName: "getFreightForwardAcctStatus",
            param1: forwardAcctNo,
            dataType: "json"
         },
         async: false,
         success: function (data) {
            flag = data;
         }
         });
        return flag;
    }
    </script>
</body>
