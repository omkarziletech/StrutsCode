<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@taglib uri="/WEB-INF/tlds/date.tld" prefix="date"%>
<jsp:useBean id="ssdetaildao" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO"/>
<jsp:useBean id="lclAddVoyageForm" scope="request" class="com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm"/>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/lclImportsUnitsSchedule.js"></cong:javascript>
<%    request.setAttribute("transModeList", ssdetaildao.getAllTransModesForDisplay());
%>
<cong:form  action="/lclImportAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="headerId" name="headerId" />
    <cong:hidden id="detailId" name="detailId" />
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr class="tableHeadingNew">
            <td >Edit Detail</td>
            <td colspan="2" style="font-weight:bold;font-size: 12px;text-transform: uppercase;">POL:<span id="polName" style="font-weight:bold;color: green;font-size: 12px;text-transform: uppercase; " >${lclUnitsScheduleForm.polName}</span></td>
            <td colspan="2" style="font-weight:bold;font-size: 12px;text-transform: uppercase;">POD:<span  id="podName" style="font-weight:bold;color: green;font-size: 12px;text-transform: uppercase;" >${lclUnitsScheduleForm.podName}</span></td>
            <td></td>
        </tr>
        <cong:tr><cong:td colspan="6"></cong:td></cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Carrier</cong:td>
            <cong:td>
                <cong:autocompletor  name="accountName" styleClass="mandatory textWidth textlabelsLclBoldForTextBox"  position="right" id="accountName"
                                     fields="accountNumber,NULL,NULL,NULL,NULL,blueScreenCarrier" callback="checkBlueScreenAcct();"
                                     shouldMatch="true" width="600" query="IMPORT_SSLINE" template="tradingPartner" container="null" scrollHeight="300px"/>
                <cong:text id="accountNumber" name="accountNumber" style="width:70px" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                <input type="hidden" name="blueScreenCarrier" id="blueScreenCarrier"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Vessel</cong:td>
            <cong:td>
                <cong:autocompletor name="spReferenceName" id="spReferenceName" template="one" params="14" position="right" scrollHeight="150"
                                    query="GENERICCODE_BY_CODETYPEID"  width="250" container="NULL" styleClass="mandatory textWidth textlabelsLclBoldForTextBox"/>
            </cong:td>

            <cong:td styleClass="textlabelsBoldforlcl">SS Voyage#</cong:td>
            <cong:td> <cong:text name="spReferenceNo" id="spReferenceNo" styleClass="mandatory textuppercaseLetter"/> </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="6"></cong:td></cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">ETD (Sailing Date)</cong:td>
            <cong:td>
                <cong:calendarNew styleClass="mandatory textWidth" id="std" name="std" onchange="calculateDaysinTT();etdValidate()"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">ETA at POD</cong:td>
            <cong:td>
                <cong:calendarNew id="etaPod" name="etaPod" styleClass="mandatory textWidth" onchange="calculateDaysinTT();etaValidate()"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Transit Mode</cong:td>
            <cong:td>
                <html:select property="transMode" disabled="true" styleClass="smallDropDown textlabelsBoldForTextBoxDisabledLook" >
                    <html:optionsCollection name="transModeList"/>
                </html:select>
            </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="6"></cong:td></cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Departure Pier</cong:td>
            <cong:td>
                <cong:autocompletor id="departurePier" name="departurePier" template="one" fields="NULL,NULL,NULL,departureId" position="right"
                                    query="CONCAT_PORT_NAME"  styleClass="mandatory textWidth"  width="350" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                <cong:hidden id="departureId" name="departureId"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Arrival Pier/POD</cong:td>
            <cong:td>
                <cong:autocompletor id="arrivalPier" name="arrivalPier" template="one" fields="NULL,NULL,NULL,arrivalId" position="left"
                                    query="CONCAT_PORT_NAME"  styleClass="mandatory textlabelsBoldForTextBox textWidth"  width="350" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                <cong:hidden id="arrivalId" name="arrivalId"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Transit Days</cong:td>
            <cong:td>
                <input type="label" id="ttOverrideDays" readOnly="true" class="textlabelsBoldForTextBoxDisabledLook" tabindex="-1"/>
            </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="6"></cong:td></cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Trucking Remarks</cong:td>
            <cong:td>
                <html:textarea cols="2" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox textAreaLimit30"
                               onkeyup="limitTextarea(this,15,30)" property="remarks" />
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">Doc Received</cong:td>
            <cong:td>
                <cong:calendarNew id="docReceived" name="docReceived" styleClass="mandatory textWidth"/>
            </cong:td>
            <cong:td styleClass="style2" align="right"></cong:td>
            <cong:td></cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td colspan="3" align="center"></cong:td>
            <cong:td colspan="3">
                <input type="button" value="Save Voyage Detail" align="center" class="button-style1" onclick="saveVoyageDetail();"/>
            </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="6"></cong:td></cong:tr>
    </table>
</cong:form>
<script type="text/javascript">
    jQuery(document).ready(function () {
        document.getElementById("polName").innerHTML = parent.document.getElementById("pooOrigin").value;
        document.getElementById("podName").innerHTML = parent.document.getElementById("podDestination").value;
        document.lclAddVoyageForm.transMode.value = "V";
        calculateDaysinTT();
        if (parent.document.getElementById("openPopup").value != null && parent.document.getElementById("openPopup").value == "openPopup") {
            document.getElementById("etaPod").value = parent.document.getElementById("etaPod").value;
            document.getElementById("departureId").value = parent.document.getElementById("departureId").value;
            document.getElementById("arrivalId").value = parent.document.getElementById("arrivalId").value;
            document.getElementById("arrivalPier").value = parent.document.getElementById("arrivalPier").value;
            document.getElementById("departurePier").value = parent.document.getElementById("departurePier").value;
            document.getElementById("accountNumber").value = parent.document.getElementById("accountNumber").value;
            document.getElementById("accountName").value = parent.document.getElementById("accountName").value;
            document.lclAddVoyageForm.transMode.value = parent.document.getElementById("transMode").value;
            document.getElementById("docReceived").value = parent.document.getElementById("docReceived").value;
        }
    });

    function saveVoyageDetail() {

        if (document.getElementById("accountNumber") == null || document.getElementById("accountNumber").value == "") {
            sampleAlert("Line is required");
            $("#accountName").css("border-color", "red");
            $("#warning").show();
        }
        else if (document.getElementById("spReferenceName") == null || document.getElementById("spReferenceName").value == "") {
            sampleAlert("Vessel is required");
            $("#spReferenceName").css("border-color", "red");
            $("#warning").show();
        }
        else if (document.getElementById("spReferenceNo") == null || document.getElementById("spReferenceNo").value == "") {
            sampleAlert("SS Voyage# is required");
            $("#spReferenceNo").css("border-color", "red");
            $("#warning").show();
        }
        else if (document.getElementById("etaPod") == null || document.getElementById("etaPod").value == "") {
            sampleAlert("ETA is required");
            $("#etaPod").css("border-color", "red");
            $("#warning").show();
        }
        else if (document.getElementById("std") == null || document.getElementById("std").value == "") {
            sampleAlert("ETD Sailing Date is required");
            $("#std").css("border-color", "red");
            $("#warning").show();
        } else if (document.getElementById("departureId") == null || document.getElementById("departureId").value == "" ||
            document.getElementById("departureId").value == 0) {
            sampleAlert("Departure Pier is required");
            $("#departurePier").css("border-color", "red");
            $("#warning").show();
        } else if (document.getElementById("arrivalId") == null || document.getElementById("arrivalId").value == "" ||
            document.getElementById("arrivalId").value == 0) {
            sampleAlert("Arrival Pier is required");
            $("#arrivalPier").css("border-color", "red");
            $("#warning").show();
        } else if ($("#ttOverrideDays").val() === '' || $("#ttOverrideDays").val() <= 0) {
            sampleAlert("ETA should be greater than ETD");
            $("#departurePier").css("border-color", "red");
            $("#arrivalPier").css("border-color", "red");
            $("#warning").show();
        }else if (document.getElementById("docReceived") == null || document.getElementById("docReceived").value == "" ||
            document.getElementById("docReceived").value == 0) {
            sampleAlert("Doc Received is required");
            $("#docReceived").css("border-color", "red");
            $("#warning").show();
        } else {
            var transMode = document.lclAddVoyageForm.transMode.value;
            var detailId = document.getElementById("detailId").value;
            var headerId = parent.document.getElementById("headerId").value;
            if (headerId != null && headerId != "" && headerId != "0" && transMode.toUpperCase() == "V") {
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "checkTransMode",
                        param1: headerId,
                        param2: detailId,
                        param3: transMode,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        if (data == true) {
                            sampleAlert("Cannot Add more than one detail for Trans Mode V");
                            return false;
                        } else {
                            saveDetailValues(detailId, transMode);
                        }
                    }
                });
            }
            else {
                saveDetailValues(detailId, transMode);
            }
        }
    }
    function calculateDaysinTT() {
        if ($("#std").val() != "" && $("#etaPod").val() != "") {
            var one_day = 1000 * 60 * 60 * 24;
            var stdDate = $("#std").val();
            var etdDay = stdDate.substring(0, 2);
            var etdMonth = getMonthNumber(stdDate.substring(3, 6));
            var etdYear = stdDate.substring(7, stdDate.length);
            var etdSailingDate = new Date(etdYear, etdMonth, etdDay);
            var etaPod = $("#etaPod").val();
            var etaDay = etaPod.substring(0, 2);
            var etaMonth = getMonthNumber(etaPod.substring(3, 6));
            var etaYear = etaPod.substring(7, etaPod.length);
            var etaDate = new Date(etaYear, etaMonth, etaDay);
            var totaldays = Math.ceil((etaDate.getTime() - etdSailingDate.getTime()) / (one_day));
            $("#ttOverrideDays").val(totaldays);
        }
    }
    function saveDetailValues(detailId, transMode) {
        parent.document.getElementById("accountNumber").value = document.getElementById("accountNumber").value;
        parent.document.getElementById("spReferenceName").value = document.getElementById("spReferenceName").value;
        parent.document.getElementById("spReferenceNo").value = document.getElementById("spReferenceNo").value;
        parent.document.getElementById("std").value = document.getElementById("std").value;
        parent.document.getElementById("etaPod").value = document.getElementById("etaPod").value;
        parent.document.getElementById("departureId").value = document.getElementById("departureId").value;
        parent.document.getElementById("arrivalId").value = document.getElementById("arrivalId").value;
        parent.document.getElementById("docReceived").value = document.getElementById("docReceived").value;
        parent.document.getElementById("transMode").value = transMode;
        parent.document.getElementById("detailId").value = detailId;
        parent.document.getElementById("remarks").value = document.lclAddVoyageForm.remarks.value;
        parent.$("#methodName").val('saveVoyageDetails');
        parent.$("#lclAddVoyageForm").submit();
    }
    function checkBlueScreenAcct() {
        var blueScreenAcct = $('#blueScreenCarrier').val();
        if (blueScreenAcct === null || blueScreenAcct === "" || blueScreenAcct === "00000") {
            $.prompt('Please Select Steam ship line Account');
            $("#accountName").css("border-color", "red");
            $("#accountName").show();
            $("#accountName").val('');
            $("#accountNumber").val('');
        }
    }
</script>
