<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        $('#agentNo').val(parent.$('#agentNumber').val());
        showValue();
        $('#chargesCode').focus();
        var id = $('#bookingAcId').val();
        var flatrate = $('#flatRateAmount').val();
        var weight = $('#weight').val();
        var measure = $('#measure').val();
        var minimum = $('#minimum').val();
        if (id != null && id != 0 && id != "") {
            document.getElementById('saveMore').style.position = "absolute";
            document.getElementById('saveMore').style.visibility = "hidden";
        } else {
            document.getElementById('saveMore').style.position = "relative";
            document.getElementById('saveMore').style.visibility = "visible";
        }
        if (flatrate == '0.00') {
            $('#flatRateAmount').addClass('text-readonly');
            $('#flatRateAmount').attr('readonly', true);
        }
        if (weight == '0.00' || measure == '0.00' || minimum == '0.00') {
            $('#weight').addClass('text-readonly');
            $('#weight').attr('readonly', true);
            $('#measure').addClass('text-readonly');
            $('#measure').attr('readonly', true);
            $('#minimum').addClass('text-readonly');
            $('#minimum').attr('readonly', true);
        }
    });

    function checkForNumberAndDecimal(obj) {
        var result;
        if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
            obj.value = "";
            sampleAlert("This field should be Numeric");
        }
    }

    function saveCharge(buttonVal) {
        var fileNumberId = $("#fileNumberId").val();
        var moduleName = parent.$('#moduleName').val();
        var count = new Array();
        parent.$(".chargeAmount").each(function () {
            count.push($(this).text().trim());
        });

        var chargesCode = $('#chargesCode').val();
        if (chargesCode == null || chargesCode == "")
        {
            sampleAlert('Code is required');
            $("#chargesCode").css("border-color", "red");
            return false;
        }
            if ("Imports" === moduleName) {
                if(count.length<25){
                var chargeCodeFlag=validateAgentInvoice(fileNumberId,chargesCode);
                if(chargeCodeFlag){
                    submitAjaxForm('addChargesInvoice','#lclCostAndChargeForm','#chargeDesc',buttonVal);
                }else{
                                $.prompt("<span style=color:red>" + chargesCode + '</span> Charge Code Already Exists and not yet posted');
                            }
                        } else {
                            $.prompt("More than 25 charges is not allowed");
                        }
                    } else {
                        if (count.length < 12) {
                            submitAjaxForm('addChargesInvoice', '#lclCostAndChargeForm', '#chargeDesc', buttonVal);
                        } else {
                            $.prompt("More than 12 charges is not allowed");
                        }
                    }
                }
    function validateAgentInvoice(fileId,chargeCode){
        var flag;
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO",
                methodName: "isValidateAgentCharges",
                param1: fileId,
                param2: 'LCLI',
                param3: 'AR',
                param4: chargeCode,
                dataType: "json"

            },
            async: false,
            success: function (data) {
                if(data==null){
                    flag=true;
                }else{
                    flag=data;
                }
            }
        });
        return flag;
    }
    function tabFocusRestrictor(e) {
        if (e.which == 9) {
            $('#chg').focus();
            return false;
        }
    }

</script>
<body style="background:#ffffff">
    <cong:div style="width:100%; float:left; ">
        <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
            Charges for File No:<span class="fileNo">${fileNumber}</span>
        </cong:div><br><br>
        <cong:form name="lclCostAndChargeForm" id="lclCostAndChargeForm" action="/lclCostAndCharge.do">
            <jsp:useBean id="billToParty" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO"/>
            <c:set var="billToPartyList" value="${billToParty.allBillToPartyImp}"/>
            <cong:hidden name="id" value="${id}"/>
            <cong:hidden name="fileNumber" value="${fileNumber}"/>
            <cong:hidden name="agentNo" id="agentNo" value="${lclCostAndChargeForm.agentNo}"/>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${fileNumberId}"/>
            <cong:hidden name="fileNumberStatus" value="${lclCostAndChargeForm.fileNumberStatus}"/>
            <c:set var="shipmentType" value="LCLI"/>
            <cong:table width="100%" border="0">
                <cong:tr>
                    <cong:td styleClass="td">Code</cong:td>
                    <cong:td id="chg"><cong:autocompletor name="chargesCode" id="chargesCode" template="one" query="CHARGE_CODE" fields="NULL,chargesCodeId"
                                                          params="${shipmentType}" shouldMatch="true" container="NULL" styleClass="text mandatory require" width="500"/>
                        <cong:hidden name="chargesCodeId" id="chargesCodeId"/>
                    </cong:td>
                    <cong:td></cong:td>
                    <cong:td styleClass="td" valign="middle">UOM</cong:td>
                    <cong:td>
                        <cong:div style="align:right">
                            <cong:radio name="uom" value="M" id="metric" disabled="true" styleClass="text-readonly" onclick="showValue();" container="NULL"/>M
                            <cong:radio name="uom" value="I" id="imperial" disabled="true" styleClass="text-readonly" onclick="showValue();" container="NULL"/>I</cong:div>
                    </cong:td>
                    <cong:td></cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td></cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td></cong:td>
                </cong:tr>
                <c:if test="${buttonValue!='addCost'}">
                    <cong:tr>
                        <cong:td styleClass="td">Charge Amount(Sell)</cong:td>
                        <cong:td><cong:text styleClass="text twoDigitDecFormat" style="width:76px" name="flatRateAmount" id="flatRateAmount" onkeyup="checkForNumberAndDecimal(this);rateReadonly()"/>
                            (flat rate)</cong:td>
                        <cong:td>(OR)</cong:td>
                        <cong:td align="right">W/M Rate</cong:td>
                        <cong:td><cong:text name="measure" styleClass="text twoDigitDecFormat text-readonly" disabled="true" value="" readOnly="true" style="width:76px" id="measure" onkeyup="checkForNumberAndDecimal(this);uomReadonly()"/>
                            <cong:label id="msr" text=" /CBM"></cong:label>
                            <cong:text name="weight" styleClass="text twoDigitDecFormat text-readonly" disabled="true" value="" readOnly="true"   style="width:76px" id="weight" onkeyup="checkForNumberAndDecimal(this);uomReadonly()"/>
                            <cong:label id="wei" text="/1000 KGS"></cong:label>
                        </cong:td>
                        <cong:td align="right">Minimum</cong:td>
                        <cong:td><cong:text name="minimum" styleClass="text twoDigitDecFormat text-readonly" disabled="true" value="" readOnly="true" style="width:76px" id="minimum" onkeyup="checkForNumberAndDecimal(this);uomReadonly()" />
                        </cong:td>
                    </cong:tr>
                </c:if>
                <cong:tr>
                    <cong:td></cong:td>
                </cong:tr>
            </cong:table><br>
            <cong:table>
                <cong:tr>
                    <cong:td width="10%">
                        <c:choose>
                            <c:when test="${not empty bookingAcId}">
                                <input type="button" class="button-style1" value="Save and Exit" onclick="saveCharge('SE')" id="saveCode" onkeydown="tabFocusRestrictor(event)" />
                            </c:when>
                            <c:otherwise>
                                <input type="button" class="button-style1" value="Save and Exit" onclick="saveCharge('SE')" id="saveCode" />
                            </c:otherwise></c:choose>
                    </cong:td>
                    <cong:td >
                        <input type="button" class="button-style1" style="position: relative;" value="Save and More" id="saveMore" onclick="saveCharge('SM')" onkeydown="tabFocusRestrictor(event)"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="destination" id="destination"/>
            <input type="hidden" name="buttonValue" id="buttonValue" value="${buttonValue}"/>
            <input type="hidden" name="bookingAcId" id="bookingAcId" value="${bookingAcId}"/>
            <input type="hidden" name="engmet" id="engmet" value="${engmet}"/>
        </cong:form>
    </cong:div>
    <script type="text/javascript">
        function submitAjaxForm(methodName, formName, selector, buttonVal) {
            $("#methodName").val(methodName);
            var params = $(formName).serialize();
            var fileNumberId = document.getElementById('fileNumberId').value;
            var billingType = parent.$("input:radio[name='pcBoth']:checked").val();
            var fileNumberStatus = document.getElementById('fileNumberStatus').value;
            params += "&fileNumberId=" + fileNumberId + "&billingType=" + billingType + "&fileNumberStatus=" + fileNumberStatus;
            params += "&moduleName=Imports"
            $.post($(formName).attr("action"), params,
            function (data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                if (buttonVal == "SE") {
                    parent.$.fn.colorbox.close();
                } else if (buttonVal == "SM") {
                    $('#flatRateAmount').val('');
                    $('#chargesCode').val('');
                    $('#measure').val('');
                    $('#weight').val('');
                    $('#minimum').val('');
                    $('#flatRateAmount').removeClass('text-readonly');
                    $('#flatRateAmount').attr('readonly', false);
                    $('#weight').removeClass('text-readonly');
                    $('#weight').attr('readonly', false);
                    $('#measure').removeClass('text-readonly');
                    $('#measure').attr('readonly', false);
                    $('#minimum').removeClass('text-readonly');
                    $('#minimum').attr('readonly', false);
                    $('#chargesCode').removeClass('text-readonly');
                    $('#chargesCode').attr('readonly', false);
                }
            });
        }
        function sampleAlert(txt) {
            $.prompt(txt);
        }

        function showValue()
        {
            var buttonValue = $('#buttonValue').val();
            if ($('#metric').is(":checked")) {
                if (buttonValue == 'addCharge') {
                    document.getElementById('wei').innerHTML = "/1000 KGS"
                    document.getElementById('msr').innerHTML = "/CBM"
                }
            }
            if ($('#imperial').is(":checked")) {
                if (buttonValue == 'addCharge') {
                    document.getElementById('wei').innerHTML = "/100 LBS"
                    document.getElementById('msr').innerHTML = "/CFT"
                }
            }
        }

        function rateReadonly() {
            var flatRate = $('#flatRateAmount').val();
            if (flatRate != "") {
                $('#weight').addClass('text-readonly');
                $('#weight').attr('readonly', true);
                $('#weight').attr('tabindex', -1);
                $('#measure').addClass('text-readonly');
                $('#measure').attr('readonly', true);
                $('#measure').attr('tabindex', -1);
                $('#minimum').addClass('text-readonly');
                $('#minimum').attr('readonly', true);
                $('#minimum').attr('tabindex', -1);
            }
            if (flatRate == "") {
                $('#weight').removeClass('text-readonly');
                $('#weight').attr('readonly', false);
                $('#weight').attr('tabindex', 0);
                $('#measure').removeClass('text-readonly');
                $('#measure').attr('readonly', false);
                $('#measure').attr('tabindex', 0);
                $('#minimum').removeClass('text-readonly');
                $('#minimum').attr('readonly', false);
                $('#minimum').attr('tabindex', 0);
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
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "chargeCodeValidate",
                        param1: chargeCode,
                        param2: fileID,
                        dataType: "json"
                    },
                    success: function (data) {
                        if (data == true) {
                            $("#chargesCode").val('');
                            $("#chargesCode").css("border-color", "red");
                            sampleAlert("Charge Code is already exists.Please Select different Charge Code");
                            return false;
                        }
                    }
                });
            }
        }

        $(document).ready(function () {
            $(document).keydown(function (e) {
                if ($(e.target).attr("readonly")) {
                    if (e.keyCode === 8) {
                        return false;
                    }
                }
            });
        });
    </script>
</body>
