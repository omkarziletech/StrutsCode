<%-- 
    Document   : spotRate
    Created on : Jan 22, 2016, 4:53:39 PM
    Author     : Wsadmin
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/jsps/LCL/init.jsp" %>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body style="background:#ffffff">
    <cong:div style="width:100%;" styleClass="tableHeadingNew">Spot Rate For <span class="redBold">${fileNumber}</span></cong:div>
    <cong:div style="width:100%; float:left; ">
        <cong:form name="lclBlCostAndChargeForm" id="lclBlCostAndChargeForm" action="/lclBlCostAndCharge.do">
            <cong:hidden name="fileNumberId" value="${fileNumberId}"/>
            <input type="hidden" name="updateTariff" id="updateTariff" value="N"/>
            <cong:table width="100%" border="0">
                &nbsp;  <cong:tr>
                    <cong:td styleClass="textlabelsBoldleftforlcl" colspan="2" width="20%">Rate
                        <cong:hidden name="measure" styleClass="text twoDigitDecFormat"  id="measure" />
                        <cong:text name="rate" id="rate" style="width:76px" onkeyup="checkForNumberAndDecimal(this);"/>
                        <cong:label id="msr"></cong:label>&nbsp;&nbsp;
                        <cong:hidden name="weight" styleClass="text twoDigitDecFormat"  id="weight" />
                        <cong:text name="rateN" id="rateN" style="width:76px" onkeyup="checkForNumberAndDecimal(this);"/>
                        <cong:label id="wei"></cong:label>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>&nbsp;</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="2" styleClass="textBoldforlcl">Comment</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="2">
                        <cong:textarea rows="4" cols="30" name="comment" id="comment" 
                                       styleClass="textLCLuppercase" style="width:259px" value="${comment}" >

                        </cong:textarea></cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>&nbsp;</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="2"  styleClass="textBoldforlcl">
                        <cong:checkbox name="spotCheckBottom" id="spotCheckBottom" 
                                       onclick="checkBottom();" container="NULL" >Is this a Bottom Line spot rate?
                        </cong:checkbox>&nbsp;&nbsp;
                        <cong:checkbox name="spotCheckOF" id="spotCheckOF" onclick="checkOcnFrt();" container="NULL" >
                            or an Ocean Freight only spot rate?
                        </cong:checkbox>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                    </cong:td>
                    <cong:td  styleClass="textBoldforlcl" align="left" style="color: #FF0000;" width="52%">
                        This rate includes any T&T
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>&nbsp;</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                        <input type="button" class="button-style1" value="Save" align="middle" 
                               id="saveCode" onclick="spotValidate();"/>
                        <input type="button" class="button-style1" value="Cancel" align="middle" id="cancelCode" 
                               onclick="closePrompt();"/>
                    </cong:td>
                </cong:tr>
                <cong:hidden name="methodName" id="methodName"/>
                <input type="hidden" name="engmet" id="engmet" value="${engmet}"/>
                <input type="hidden" name="buttonValue" id="buttonValue" value="${buttonValue}"/>
            </cong:table>
        </cong:form>
    </cong:div>
</body>
<script type="text/javascript">
    $(document).ready(function () {
        addWM();
    });
    function closePrompt() {
        var rate1 = $('#rate').val();
        var rate2 = $('#rateN').val();
        if (rate1 == '' && rate2 == '') {
            setSpotRateDisable();
        }
        parent.$.fn.colorbox.close();
    }
    function submitAjaxForm(methodName, formName, selector, submitCommodity) {
        showProgressBar();
        $("#methodName").val(methodName);
        var params = $(formName).serialize();
        var fileNumberId = parent.$('#fileNumberId').value;
        var billingType = parent.$("input:radio[name='pcBoth']:checked").val();
        params += "&fileNumberId=" + fileNumberId + "&billingType=" + billingType;
        $.post($(formName).attr("action"), params,
        function (data) {
            showProgressBar();
            $(selector).html(data);
            $(selector, window.parent.document).html(data);
            $("#methodName").val('refreshCommodity');
            var params1 = $(formName).serialize();
            params1 += "&fileNumberId=" + fileNumberId;
            $.post($(formName).attr("action"), params1,
            function (data) {
                $("#commodityDesc").html(data);
                $("#commodityDesc", window.parent.document).html(data);
                parent.$.fn.colorbox.close();
                hideProgressBar();
                spotDisplayT();
                setColor();
            });
        });
    }

    function validateCommondity() {
        flag = true;
        parent.$(".commodity_no").each(function () {
            if ($(this).val() === parent.$('#spotRateCommNo').val()) {
                flag = false;
            }
        });
        return flag;
    }
    function spotValidate() {
        var rate = $('#rate').val();
        var rateN = $('#rateN').val();
        if ((rate == null || rate == 0.00 || rate == "") && (rateN == null || rateN == 0.00 || rateN == "")) {
            $.prompt("Please add rate");
            return false;
        } else if (!$('#spotCheckBottom').is(":checked") && !$('#spotCheckOF').is(":checked")) {
            $.prompt("Please select checkbox");
            return false;
        } else if (parent.$("#commObj tr").length >= 2 && validateCommondity()) {
            $("#updateTariff").val('Y');
            showAlert("Tariff Number Will Be Changed to <span style=color:red>212600</span> and rates will be recalculated.");
        } else {
            submitAjaxForm('addSpotRate', '#lclBlCostAndChargeForm', '#chargeBlDesc', false);
        }
    }
    function showAlert(txt) {
        $.prompt(txt, {buttons: {OK: 1}, submit: function (v) {
                if (v === 1) {
                    submitAjaxForm('addSpotRate', '#lclBlCostAndChargeForm', '#chargeBlDesc', true);
                }
            }});
    }

    function checkForNumberAndDecimal(obj) {
        if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
            obj.value = "";
            sampleAlert("This field should be Numeric");
        }
    }

    function spotDisplayT() {
        var engmet = $('#engmet').val();
        var label = engmet === "E" ? $("#rate").val() + "/CFT" + "," + $('#rateN').val() + "/100 LBS"
        : $("#rate").val() + "/CBM" + "," + $('#rateN').val() + "/1000 KGS";
        parent.$("#spotratelabel").text(label);
    }
    function setColor() {
        parent.$('#lclSpotRate').addClass($('#rate').val() !== ""
            ? "green-background lclSpotRate" : "button-style1 lclSpotRate");
    }
</script>