<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/jsps/LCL/init.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<body style="background:#ffffff">
    <br>
    <cong:form id="lclRatesForm" name="lclRatesForm" action="/lclRates">
        <table width="99%" border="0" cellpadding="1" cellspacing="1" class="tableBorderNew" align="center">
            <tr class="tableHeadingNew"><td>LCL Rates</td></tr>
            <tr> <td>
                    <table width="99%" border="0" cellpadding="1" cellspacing="1" align="center">
                        <cong:tr>
                            <td class="textlabelsBoldforlcl">OriginTrm</td>
                            <td><cong:autocompletor name="orgTrm" id="originTrm" query="ORIGINTRM" template="one" width="400" fields="trmnum" scrollHeight="200px" shouldMatch="true"  container="NULL" callback="saveRates()"/></td>
                            <cong:hidden name="trmnum" id="trmnum"/>
                            <input type="hidden" name="methodName" id="methodName"/>
                            <td class="textlabelsBoldforlcl">DestinationTrm</td>
                            <cong:td ><cong:autocompletor name="destTrm" id="destTrm" query="DESTTRM" template="one"  width="400" scrollHeight="200px" shouldMatch="true" fields="eciportcode" container="NULL"  callback="saveRates()"/></cong:td>
                            <cong:hidden name="eciportcode" id="eciportcode"/>
                        </cong:tr>
                        <cong:tr>
                            <td class="textlabelsBoldforlcl">CommCode</td>
                            <cong:td ><cong:autocompletor name="comCode" id="comCode" template="two" fields="comDesc,hazmat,commodityTypeId" query="COMMODITY_TYPE_CODE" width="500" styleClass="text weight textlabelsBoldForTextBox"
                                                          container="NULL" scrollHeight="200px" shouldMatch="true"  callback="saveRates()"/></cong:td>
                                <td class="textlabelsBoldforlcl">Comm Desc</td>
                            <cong:td ><cong:text readOnly="true" styleClass="text-readonly" name="comDesc" id="comDesc" container="NULL"/></cong:td>
                        </cong:tr>
                        <cong:tr>
                            <td class="textlabelsBoldforlcl">UOM</td>
                            <cong:td >
                                <cong:radio name="uom" id="uomI" value="I" container="NULL" onclick="showValue()">I</cong:radio>
                                <cong:radio name="uom" id="uomM" value="M" container="NULL" onclick="showValue()">M</cong:radio>
                            </cong:td>
                            <td class="textlabelsBoldforlcl">W/M Rate</td>
                            <cong:td><cong:text name="measure" styleClass="text twoDigitDecFormat" style="width:76px" id="measure" onkeyup="checkForNumberAndDecimal(this);uomReadonly()"/>
                                <cong:label id="msr" text=" /CBM"></cong:label>
                                <cong:text name="weight" styleClass="text twoDigitDecFormat" style="width:76px" id="weight" onkeyup="checkForNumberAndDecimal(this);uomReadonly()"/>
                                <cong:label id="wei" text="/1000 KGS"></cong:label>
                                    Minimum
                                <cong:text name="minimum" styleClass="text twoDigitDecFormat" style="width:76px" id="minimum" onkeyup="checkForNumberAndDecimal(this);uomReadonly()" />
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <td colspan="3"></td>
                            <td><input type="button" class="button-style1" value="Save" onclick="save()"></td>
                            </cong:tr>


                    </table>
                </td></tr></table>
                <c:if test="${not empty ratesList}">
            <table width="99%" border="0" cellpadding="1" cellspacing="1" align="center">
                <tr><td>
                        <display:table  name="${ratesList}" id="ratesObj" class="dataTable" sort="list">
                            <c:choose>
                                <c:when test="${not empty ratesObj.engCft && ratesObj.engCft!='0.00'}">
                                    <display:column title="Weight(CFT)" >${ratesObj.engCft}</display:column>
                                    <display:column title="Measure(LBS)" >${ratesObj.engWgt}</display:column>
                                </c:when>
                                <c:otherwise>
                                    <display:column title="Weight(CBM)" >${ratesObj.metCft}</display:column>
                                    <display:column title="Measure(KGS)" >${ratesObj.metWgt}</display:column>
                                </c:otherwise>
                            </c:choose>
                            <display:column title="Minimum" >${ratesObj.ofMin}</display:column>
                        </display:table>
                    </td> </tr>
            </table>

            <table width="99%" border="0" cellpadding="1" cellspacing="1" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew"><td>Port Charge</td></tr>
                <tr> <td>
                        <table width="99%" border="0" cellpadding="1" cellspacing="1" align="center">
                            <cong:tr>
                                <td class="textlabelsBoldforlcl">Charge Code</td>
                                <td>
                                    <cong:autocompletor name="chargesCode" id="chargesCode" template="one" query="PORTCHARGECODE" fields="chgCode"
                                                        container="NULL" styleClass="text " width="500" callback="checkChargeCode()"/>
                                    <cong:hidden name="chgCode" id="chgCode" /></td>
                                <td class="textlabelsBoldforlcl">Flat Rate</td>
                                <td><cong:text name="flatRate" id="flatRate" styleClass="text twoDigitDecFormat" style="width:76px" onkeyup="checkForNumberAndDecimal(this);uomReadonly();"/></td>
                                <td class="textlabelsBoldforlcl">W/M Rate</td>
                                <cong:td><cong:text name="prtMeasure" styleClass="text twoDigitDecFormat" style="width:76px" id="prtMeasure" onkeyup="checkForNumberAndDecimal(this);uomReadonly();"/>
                                    <cong:label id="msr" text=" /CBM"></cong:label>
                                    <cong:text name="prtWeight" styleClass="text twoDigitDecFormat" style="width:76px" id="prtWeight" onkeyup="checkForNumberAndDecimal(this);uomReadonly();"/>
                                    <cong:label id="wei" text="/1000 KGS"></cong:label>
                                        Minimum
                                    <cong:text name="minChg" styleClass="text twoDigitDecFormat" style="width:76px" id="minChg" onkeyup="checkForNumberAndDecimal(this);uomReadonly();" />
                                </cong:td>
                            </cong:tr>

                            <cong:tr>
                                <td colspan="3"></td>
                                <td><input type="button" class="button-style1" value="Save" onclick="savePrtRates()"></td>
                                </cong:tr>


                        </table>
                    </td></tr></table>

            <table width="99%" border="0" cellpadding="1" cellspacing="1" align="center">
                <tr><td>
                        <display:table  name="${ratesPrtChgList}" id="rateObj" class="dataTable" sort="list">
                            <display:column title="ChgCode" >${rateObj.chgCode}</display:column>

                            <display:column title="FlatRate" >
                                ${rateObj.ofRate}
                            </display:column>
                            <display:column title="Measure(CFT)">
                                ${rateObj.cft}
                            </display:column>
                            <display:column title="Weight(LBS)">
                                ${rateObj.lbs}
                            </display:column>
                            <display:column title="Measure(CBM)">
                                ${rateObj.cbm}
                            </display:column>
                            <display:column title="Weight(KGS)">
                                ${rateObj.kgs}
                            </display:column>
                            <display:column title="Minimum">
                                ${rateObj.minChg}
                            </display:column>
                        </display:table>
                    </td> </tr>
            </table>

        </c:if>
    </cong:form>

    <script type="text/javascript">
        $(document).ready(function () {
            showUom();
            showValues();
        });

        function showValues() {
            $('#prtWeight').val('');
            $('#prtMeasure').val('');
            $('#minChg').val('');
            $('#flatRate').val('');
            $('#chargesCode').val('');
            $('#chgCode').val('');
        }
        function showValue()
        {
            if ($('#uomM').is(":checked")) {
                document.getElementById('wei').innerHTML = "/1000 KGS"
                document.getElementById('msr').innerHTML = "/CBM"
            }
            if ($('#uomI').is(":checked")) {
                document.getElementById('wei').innerHTML = "/100 LBS"
                document.getElementById('msr').innerHTML = "/CFT"
            }
        }

        function saveRates() {
            showUom();
            if ($('#trmnum').val() != null && $('#trmnum').val() != "" && $('#eciportcode').val() != null && $('#eciportcode').val() != "" && $('#comCode').val() != null && $('#comCode').val() != "") {
                $('#methodName').val("save");
                $('#lclRatesForm').submit();
            }
        }
        function showUom() {
            var unlocationCode = "";
            var destinationCode = "";
            var destinationObjR = document.getElementById('destTrm');
            if (destinationObjR != undefined && destinationObjR != null && destinationObjR.value != "") {
                destinationCode = document.getElementById('destTrm').value;
            }
            if (destinationCode != null && destinationCode != "") {
                if (destinationCode.lastIndexOf("(") > -1 && destinationCode.lastIndexOf(")") > -1) {
                    unlocationCode = destinationCode.substring(destinationCode.lastIndexOf("(") + 1, destinationCode.lastIndexOf(")"));
                }
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "fillUom",
                        param1: unlocationCode
                    },
                    success: function (data) {
                        if (data != null && data != "") {
                            if (data == 'E') {
                                $('#uomI').attr('checked', true);
                                $('#uomI').attr('disabled', false);
                                $('#uomM').attr('disabled', true);
                                showValue();
                            } else {
                                $('#uomM').attr('checked', true);
                                $('#uomI').attr('disabled', true);
                                $('#uomM').attr('disabled', false);
                                showValue();
                            }
                        }
                    }
                });
            }
        }

        function uomReadonly() {
            var weightCost = $('#prtWeight').val();
            var measureCost = $('#prtMeasure').val();
            var minimumCost = $('#minChg').val();
            var flatRate = $('#flatRate').val();
            if (weightCost != "" || measureCost != "" || minimumCost != "") {
                $('#flatRate').addClass('text-readonly');
                $('#flatRate').attr('readonly', true);
            }
            if (weightCost == "" && measureCost == "" && minimumCost == "") {
                $('#flatRate').removeClass('text-readonly');
                $('#flatRate').attr('readonly', false);
            }
            if (flatRate != "") {
                $('#prtWeight').addClass('text-readonly');
                $('#prtWeight').attr('readonly', true);
                $('#prtMeasure').addClass('text-readonly');
                $('#prtMeasure').attr('readonly', true);
                $('#minChg').addClass('text-readonly');
                $('#minChg').attr('readonly', true);
            }

            if (flatRate == "") {
                $('#prtWeight').removeClass('text-readonly');
                $('#prtWeight').attr('readonly', false);
                $('#prtMeasure').removeClass('text-readonly');
                $('#prtMeasure').attr('readonly', false);
                $('#minChg').removeClass('text-readonly');
                $('#minChg').attr('readonly', false);
            }

        }

        function save() {
            if ($('#originTrm').val() == null || $('#originTrm').val() == "") {
                congAlert("OriginTrm is Required");
                $("#originTrm").css("border-color", "red");
                return false;
            } else if ($('#destTrm').val() == null || $('#destTrm').val() == "") {
                $("#destTrm").css("border-color", "red");
                congAlert("DestinationTrm is Required");
                return false;
            } else if ($('#comCode').val() == null || $('#comCode').val() == "") {
                $("#comCode").css("border-color", "red");
                congAlert("CommCode is Required");
                return false;
            } else if ($('#weight').val() == null || $('#weight').val() == "") {
                $("#weight").css("border-color", "red");
                congAlert("W/M Rate is Required")
                return false;
            } else if ($('#measure').val() == null || $('#measure').val() == "") {
                $("#measure").css("border-color", "red");
                congAlert("W/M Rate is Required")
                return false;
            } else if ($('#minimum').val() == null || $('#minimum').val() == "") {
                $("#minimum").css("border-color", "red");
                congAlert("Minimum is Required")
                return false;
            } else {
                $('#methodName').val("saveRates");
                $('#lclRatesForm').submit();
            }
        }

        function congAlert(txt) {
            $.prompt(txt);
        }

        function savePrtRates() {
            if ($('#chargesCode').val() == null || $('#chargesCode').val() == "") {
                congAlert("ChargeCode is Required");
            } else if (($('#flatRate').val() == null || $('#flatRate').val() == "") &&
                    (($('#prtWeight').val() == null || $('#prtWeight').val() == "") || ($('#prtMeasure').val() == null || $('#prtMeasure').val() == "")
                            || ($('#minChg').val() == null || $('#minChg').val() == ""))) {
                congAlert("Flat Rate Amount is required or Combination of Measure,Weight and Minimum fields are required");
            } else {
                $('#methodName').val("savePrtRates");
                $('#lclRatesForm').submit();
            }
        }

        function checkChargeCode() {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "verifyPortChgRatesCombination",
                    param1: $('#trmnum').val(), 
                    param2: $('#eciportcode').val(), 
                    param3: $('#comCode').val(), 
                    param4: $('#chgCode').val(),
                    dataType: "json"
                },
                success: function (data) {
                    if (data) {
                        congAlert("Charge is already exists.Please Select different Charge Code");
                        $("#chargesCode").css("border-color", "red");
                        $('#chargesCode').val('');
                        return false;
                    }
                }
            });
        }

        function checkForNumberAndDecimal(obj) {
            var result;
            if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
                obj.value = "";
                sampleAlert("This field should be Numeric");
            }
        }
    </script>
</body>
