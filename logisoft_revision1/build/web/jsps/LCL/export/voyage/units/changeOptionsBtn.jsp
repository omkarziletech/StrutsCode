<%-- 
    Document   : /jsps/LCL/export/voyage/units/changeOptionsBtn.jsp
    Created on : Apr 21, 2016, 4:53:12 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../../../init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="../../../../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change Option</title>
    </head>
    <body>
        <cong:form  action="/lclAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm">
            <cong:hidden id="methodName" name="methodName"/>
            <input type="hidden" name="msg" id="msgId" value="${sucessMsg}"/>
            <cong:hidden id="scheduleNo" name="scheduleNo" value="${unitSs.lclSsHeader.scheduleNo}"/>
            <cong:hidden id="unitssId" name="unitssId" value="${unitSs.id}"/>
            <cong:hidden id="headerId" name="headerId" value="${unitSs.lclSsHeader.id}"/>
            <cong:hidden id="originalOriginId" name="originalOriginId" value="${unitSs.lclSsHeader.origin.id}"/>
            <cong:hidden id="originalDestinationId" name="originalDestinationId" value="${unitSs.lclSsHeader.destination.id}"/>
            <cong:hidden id="schServiceType" name="schServiceType" value="${unitSs.lclSsHeader.serviceType}"/>

            <cong:hidden id="oldUnitId" name="oldUnitId"  value="${unitSs.lclUnit.id}" />
            <cong:hidden name="oldUnitNo" id="oldUnitNo" value="${unitSs.lclUnit.unitNo}"/>
            <input type="hidden" name="existUnitNo" id="existUnitNo" value="${unitSs.lclUnit.unitNo}"/>
            <input type="hidden" name="existUnitId" id="existUnitId" value="${unitSs.lclUnit.id}"/>
            <cong:hidden name="unitId" id="unitId"  value="${unitSs.lclUnit.id}"  />
            <cong:hidden id="unPickDrOpt" name="unPickDrOpt"/>
            <cong:hidden id="originId" name="originId" />
            <cong:hidden id="finalDestinationId" name="finalDestinationId" />
            <cong:hidden id="filterByChanges" name="filterByChanges" />

            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td width="20%">Change Option</td>
                    <td width="2%">UnitNo#</td>
                    <td width="30%" style="color: blue;" >
                        ${unitSs.lclUnit.unitNo}</td>
                    <td width="2%">VoyageNo#</td>
                    <td style="color: blue;" >
                        ${unitSs.lclSsHeader.scheduleNo}</td>
                </tr>
            </table>
            <table width="100%" align="center">
                <tr><td colspan="2"></td></tr>
                <tr>
                    <td class="textlabelsBoldforlcl">Change Option</td>
                    <td>
                        <html:select property="changeVoyOpt" styleId="changeVoyOpt" style="width:240px;"
                                     styleClass="unfixedtextfiledstyle" onchange="changeOption()">
                            <html:option value="">Select</html:option>
                            <c:if test="${empty flagOption}">
                                <c:if test="${voyageFlag}">
                                    <html:option value="CV">Move Unit/BLs to a new voyage</html:option>
                                </c:if>
                                <html:option value="UC">Change the Unit#</html:option>
                            </c:if>
                            <c:if test="${unitSs.status eq 'M'}">
                                <html:option value="ABL">Add a BL to this unit’s manifest</html:option>
                                <html:option value="RBL"> Remove a BL from this unit’s manifest</html:option>
                            </c:if>
                        </html:select>
                    </td>
                </tr>
                <tr><td colspan="2"></td></tr>
                <tr id="changeVoyage">
                    <td class="textlabelsBoldforlcl">Voyage No#</td>
                    <td>
                        <cong:autocompletor name="schedule" id="schedule" template="one" paramFields="originalOriginId,originalDestinationId,scheduleNo,schServiceType"
                                            position="right" scrollHeight="100px" query="LCL_SS_VOYAGE" shouldMatch="true"
                                            fields="changeVoyageNo,changeVoyHeaderId"
                                            width="160" container="NULL" styleClass="mandatory textlabelsLclBoldForTextBox"/>
                        <cong:hidden name="changeVoyageNo" id="changeVoyageNo"/>
                        <cong:hidden name="changeVoyHeaderId" id="changeVoyHeaderId"/>
                    </td>
                </tr>
                <tr id="changeUnit">
                    <td class="textlabelsBoldforlcl">Unit No#</td>
                    <td>
                        <%--  <cong:autocompletor name="schedule" id="unitNos" template="one" paramFields="oldUnitNo"
                                              position="right" scrollHeight="100px" query="UNIT_NO" shouldMatch="true"
                                              fields="unitNo,unitId" width="300" container="NULL" styleClass="mandatory textlabelsLclBoldForTextBox"/>
                          <cong:hidden id="unitId" name="unitId"/>
                          <cong:hidden id="unitNo" name="unitNo"/>--%>
                        <cong:text name="unitNo" id="unitNo" onchange="unitNumberExists();" styleClass="mandatory  textuppercaseLetter" /> 
                    </td>
                </tr>
                <tr id="changeAddBl">
                    <td class="textlabelsBoldforlcl">DR#</td>
                    <td>
                        <cong:autocompletor name="fileNumber" id="fileNumber" template="one" paramFields="originalOriginId,originalDestinationId,scheduleNo"
                                            position="right" scrollHeight="100px" query="BL_POSTED_FILES" shouldMatch="true"
                                            fields="fileNumberId" width="300" container="NULL" styleClass="mandatory textlabelsLclBoldForTextBox"/>
                    </td>
                    <cong:hidden id="fileNumberId" name="fileNumberId"/>
                </tr>
                <tr id="changeRemoveBl">
                    <td class="textlabelsBoldforlcl">DR#</td>
                    <td>
                        <cong:autocompletor name="fileNumber" id="fileNumbers" template="one" paramFields="unitssId"
                                            position="right" scrollHeight="100px" query="BL_PICKED_UNIT" shouldMatch="true"
                                            fields="fileNumberId" width="300" container="NULL" styleClass="mandatory textlabelsLclBoldForTextBox"/>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input type="button" class="button-style3" value="Submit" onclick="submitOpt()"/>
                        <input type="button" class="button-style3" value="Cancel" onclick="closePopupBox()"/>
                    </td>
                </tr>
            </table>
            <div id="add-voyage-container" class="static-popup" style="display: none;width: 600px;height: 150px;">
                <%@include file="/jsps/LCL/export/voyage/units/changeByOptions.jsp" %>
            </div>
        </cong:form>
    </body>
</html>
<script type="text/javascript">
    $(document).ready(function () {
        if ($('#changeVoyOpt').val() === '') {
            $('#changeVoyage').hide();
            $('#changeUnit').hide();
            $('#changeAddBl').hide();
            $('#changeRemoveBl').hide();
        }
        var msg = $('#msgId').val();
        if (msg != '') {
            $.prompt(msg, {
                buttons: {
                    Ok: 1
                },
                submit: function (v) {
                    if (v === 1) {
                        parent.$.fn.colorbox.close();
                        window.parent.showLoading();
                        parent.$("#methodName").val('editVoyage');
                        parent.$("#lclAddVoyageForm").submit();
                    }
                }
            });
        }
    });
    function changeOption() {
        var changeOpt = $('#changeVoyOpt').val();
        $('#changeVoyage').hide();
        $('#changeUnit').hide();
        $('#changeAddBl').hide();
        $('#changeRemoveBl').hide();
        if (changeOpt === 'CV') {
            $('#changeVoyage').show();
        }
        if (changeOpt === 'UC') {
            $('#changeUnit').show();
        }
        if (changeOpt === 'ABL') {
            $('#changeAddBl').show();
        }
        if (changeOpt === 'RBL') {
            $('#changeRemoveBl').show();
        }
    }
    function closePopupBox() {
        parent.$.fn.colorbox.close();
    }
    function submitOpt() {
        var changeOpt = $('#changeVoyOpt').val();
        if (changeOpt === '') {
            $.prompt("Please select any option");
            return false;
        }
        if (changeOpt === 'CV') {
            changeVoyage();
        }
        if (changeOpt === 'UC') {
            changeUnit();
        }
        if (changeOpt === 'ABL') {
            validateFileNo();
        }
        if (changeOpt === 'RBL') {
            removeFileNo();
        }
    }

    function changeUnit() {
        var unitNo = $('#unitNo').val();
        if (unitNo === null || unitNo === '') {
            $.prompt("UnitNo is Required");
            return false;
        }
        var unitFlag = isValidateByUnitSs(unitNo);
        if (unitFlag) {
            showAlternateMask();
            $("#add-voyage-container").center().show(500, function () {
                $('#voyageChangeReason').val('');
            });
        }
    }
    function validateOption() {
        var emails = false;
        $('.selected').each(function () {
            var flag = $(this).attr('checked') ? true : false;
            if (flag) {
                emails = true;
            }
        });
        var ispdf = false;
        $('.selectedDoc').each(function () {
            var flag = $(this).attr('checked') ? true : false;
            if (flag) {
                ispdf = true;
            }
        });
        if (!emails) {
            $.prompt("Please select atleast one Send Auto notifications to both customers and internal and to billing terminal");
            return false;
        }
        if (!ispdf) {
            $.prompt("Please select atleast one Document(booking or non-rated B/L)");
            return false;
        }
        var comments = $('#voyageChangeReason').val();
        if (comments === null || comments === '') {
            $.prompt("Comment is Required");
            return false;
        }

        submitVoyage();
    }
    function submitVoyage() {
        showLoading();
        $("#methodName").val('changeOptionSubmitBtn');
        $("#lclAddVoyageForm").submit();
    }
    function changeVoyage() {
        var scheduleNo = $('#changeVoyageNo').val();
        if (scheduleNo === null || scheduleNo === '') {
            $.prompt("UnitNo is Required");
            return false;
        }
        var unitFlag = unitNumberExist();
        if (unitFlag) {
            showAlternateMask();
            $("#add-voyage-container").center().show(500, function () {
                $('#voyageChangeReason').val('');
            });
        }
    }
    function isValidateByUnitSs(unitNo) {
        var unitId = $('#unitNo').val();// This is given unitNo
        var scheduleNo = $('#scheduleNo').val();
        var flag = true;
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO",
                methodName: "isValidateByUnitSs",
                param1: $('#headerId').val(),
                param2: unitId,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data) {
                    $.prompt("Unit Number  <span style=color:red>" + unitNo + " </span> Already Exists in the Voyage#" + "<span style=color:red>" + scheduleNo);
                    $("#unitId").val('');
                    $("#unitNo").val('');
                    flag = false;
                }

            }
        });
        return flag;
    }
    function unitNumberExist() {
        var scheduleNo = $('#changeVoyageNo').val()
        var unitNumber = $("#unitNo").val();
        var flag = true;
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkUnitNumberExists",
                param1: $('#changeVoyHeaderId').val(),
                param2: unitNumber,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data[0] == "1") {
                    $.prompt("Unit Number  <span style=color:red>" + unitNumber + " </span> Already Exists in the Voyage#" + "<span style=color:red>" + scheduleNo);
                    $("#schedule").val('');
                    flag = false;
                }

            }
        });
        return flag;
    }

    function unitNumberValidate() {
        var unitNo = $("#unitNo").val();
        if (unitNo != '') {
            unitNo = unitNo.replace(/-/g, '');
            if (unitNo.length < 11 || unitNo.length > 11 || isNotAlphabetic(unitNo.substring(0, 4))
                    || !isInteger(unitNo.substring(4, 12))) {
                $.prompt('Unit number must be "AAAA-NNNNNN-N" in format');
                return false;
            } else {
                if (unitNo.lastIndexOf("-") != 11 || unitNo.indexOf("-") != 4) {
                    unitNo = unitNo.substring(0, 4) + "-" + unitNo.substring(4, 10) + "-" + unitNo.substring(10);
                    $("#unitNo").val(unitNo);
                }
            }
        }
        return true;
    }

    function unitNumberExists() {
//        if(unitNumberValidate()){
        var unitId = $("#unitId").val();
        var unitNumber = $("#unitNo").val();
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkUnitNumberExists",
                param1: $("#headerId").val(),
                param2: unitNumber,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                unitCheckCurrentVoyage(data, unitNumber, unitId);
            }
        });
//        }
    }
    function unitCheckCurrentVoyage(data, unitNumber, unitId) {
        if (data[0] == "1") {
            $.prompt("Unit Number Already Exists in current voyage.");
            $('#oldUnitId').val('');
            $("#unitId").val('');
            $("#unitNo").val('');
            return false;
        } else if (data[3] == unitNumber) {
            $.prompt("Unit Number Already Exists.");
            $('#oldUnitId').val('');
            $("#unitId").val('');
            $("#unitNo").val('');
            return false;
        }
        else if (data[2] != null && data[2] != undefined && data[2] != "") {
            txt = 'Unit# <span style=color:red>' + unitNumber + '</span> Already Exists';
            if (data[1] != null && data[1] != undefined && data[1] != "")
            {
                txt += ' in Voyage# <span style=color:red>' + data[1] + "</span>";
            }
            txt += '.Do you want to open the same unit?';
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v == 1) {
                        $("#unitId").val(data[2]);
                        $("#oldUnitId").val(unitId);
                        $.prompt.close();
                    }
                    else if (v == 2) {
                        $('#oldUnitId').val('');
                        $("#unitId").val('');
                        $("#unitNo").val('');
                        $.prompt.close();
                    }
                }
            });
        } else {
            $('#oldUnitId').val($('#existUnitId').val());
        }
    }
    function validateFileNo() {
        var fileNo = $('#fileNumber').val();
        if (fileNo === null || fileNo === '') {
            $.prompt("BL FileNo is Required");
            return false;
        }
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.common.constant.ExportVoyageUtils",
                methodName: "validatePickFileNo",
                param1: $('#scheduleNo').val(),
                param2: $('#existUnitNo').val(),
                param3: $('#fileNumberId').val(),
                param4: $('#fileNumber').val(),
                param5: $('#schServiceType').val(),
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data[1] === 'chargeFlag') {
                    $.prompt(data[0]);
                    return false;
                } else {
                    pickOnFileNo(data[0]);
                }
            }
        });
    }
    function pickOnFileNo(txt) {
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    submitVoyage();
                    $.prompt.close();
                } else {
                    $.prompt.close();
                }
            }
        });
    }
    function unPickOnFileNoUnManifest(txt) {
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    $("#unPickDrOpt").val(true);
                    $("#originId").val(parent.$("#originId").val());
                    $("#finalDestinationId").val(parent.$("#finalDestinationId").val());
                    $("#filterByChanges").val(parent.$("#filterByChanges").val());
                    
                    submitVoyage();
                    $.prompt.close();
                } else {
                    pickOnFileNo("Are you sure want to UnManifest a BL from this Unit?");
                }
            }
        });
    }
    
    function removeFileNo() {
        unPickOnFileNoUnManifest("Do you also want to Un-pick the DR from the Unit?");
    }
</script>