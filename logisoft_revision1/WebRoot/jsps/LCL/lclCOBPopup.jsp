<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<html>
    <body>
        <cong:form  action="/lclAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
            <input type="hidden" name="filterByChanges" id="filterByChanges" value="${lclAddVoyageForm.filterByChanges}"/>
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="unitssId" name="unitssId"/>
            <cong:hidden id="headerId" name="headerId"/>
            <cong:hidden id="unitId" name="unitId"/>
            <input type="hidden" name="existCobValue" id="existCobValue" value="${lclUnitSs.cob}"/>
            <input type="hidden" name="reverseRoleDuty" id="reverseRoleDuty" value="${reverseRoleDuty}"/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="border: 1px solid #dcdcdc;" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td width="25%">
                        Confirm On Board
                    </td>
                    <td width="5%"><span class="blackBold"> UNIT# </span></td>
                    <td width="25%"><span class="redBold14Px">${lclAddVoyageForm.unitNo}</span></td>
                    <td width="5%"><span class="blackBold"> VOYAGE# </span></td>
                    <td width="25%"><span class="redBold14Px">${lclAddVoyageForm.scheduleNo}</span></td>
                </tr>

            </table>
            <cong:table width="100%" border="0" style="border: 1px solid #dcdcdc;">
                <cong:tr>
                    <cong:td colspan="6"></cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" style="border-left: red 2px solid;">Confirm on Board </cong:td>
                    <cong:td styleClass="textBoldforlcl">
                        <html:radio property="cob" name="lclAddVoyageForm" value="Y"
                                    styleId="cobY"  onclick="submitFormForManifest()"/>Y&nbsp;&nbsp;
                        <html:radio property="cob" name="lclAddVoyageForm" value="N"
                                    styleId="cobN" onclick="submitFormForUnManifest()"/>N</cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">ETD</cong:td>
                    <fmt:formatDate pattern="dd-MMM-yyyy" var="etdDate" value="${lclSsDetail.std}"/>
                    <cong:td>
                        <input size="20" class="BackgrndColorForTextBox"
                               value="${etdDate}" id="etdValue" readonly="readonly"  tabindex="-1"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">ETA</cong:td>
                    <cong:td>
                        <fmt:formatDate pattern="dd-MMM-yyyy 00:00:00" var="etaDate" value="${lclSsDetail.sta}"/>
                        <input size="20" class="BackgrndColorForTextBox"
                               value="${etaDate}" id="etaValue" readonly="readonly"  tabindex="-1"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="6"></cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Verified ETA</cong:td>
                    <cong:td>
                        <fmt:formatDate pattern="dd-MMM-yyyy hh:mm:ss" var="confirmedEtaDate" value="${lclSsDetail.confirmedEta}"/>
                       
                        <cong:calendarNew styleClass="mandatory textWidth" id="verifiedEta" showTime="true" is12HrFormat="true" 
                                          name="verifiedEta" value="${confirmedEtaDate}"/>
                        <input type="checkbox" name="verifiedEtaCheck" style="vertical-align: middle;"
                               id="verifiedEtaCheck"  onclick="copyEtaDate(this)" title="Same as ETA"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Vessel Name</cong:td>
                    <cong:td >
                        <input size="20" class="BackgrndColorForTextBox" value="${lclSsDetail.spReferenceName}"
                               readonly="readonly"  tabindex="-1"/></cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Voyage</cong:td>
                    <cong:td>
                        <input size="20" class="BackgrndColorForTextBox" value="${lclSsDetail.spReferenceNo}"
                               readonly="readonly"  tabindex="-1"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="6"></cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">COB Remarks</cong:td>
                    <cong:td>
                        <html:textarea cols="2" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox textAreaLimit30" styleId="cobRemarks"
                                       property="cobRemarks" value="${lclAddVoyageForm.cobRemarks}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="6"></cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="6" align="center">
                        <input type="button" class="buttonStyleNew" id="savecob" value="Save COB"
                               onclick="saveCob()" />
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:form>
        <script type="text/javascript">
            function closePopupBox() {
                parent.$.fn.colorbox.close();
            }

            function copyEtaDate(obj) {
                if (obj.checked) {
                    $("#verifiedEta").val($("#etaValue").val());
                }
            }
            function checkCob() {
                $("#verifiedEta").addClass('mandatory textWidth');
                $('#verifiedEtaCheck').show();
            }
            function saveCob() {
                var validateFwdAcct = validateForwardAcct($("#unitssId").val());
                var validateTerms = validateLclBlTerms($("#unitssId").val());
                var cobRadio = $('input:radio[name=cob]:checked').val();
                var verifiedETA = $("#verifiedEta").val();
                if (cobRadio === 'Y' && verifiedETA === '') {
                    $.prompt("Please Enter Verified ETA");
                    $('#verifiedEta').css("border-color", "red");
                    return false;
                } else if (validateFwdAcct !== null && validateFwdAcct !== "" && cobRadio === 'Y') {
                    $.prompt(validateFwdAcct);
                    return false;
                } else if (validateTerms !== null && validateTerms !== "" && cobRadio === 'Y') {
                    $.prompt(validateTerms);
                    return false;
                }
                if (cobRadio === 'Y') {
                    var termToDoBLRateType = checkTermToDoBLAndRateTypes($("#unitssId").val());
                    if (!termToDoBLRateType) {
                        $("#cobN").attr("checked", true);
                        return false;
                    }
                }
                parent.$("#cob").val(cobRadio);
                parent.$("#verifiedEta").val($("#verifiedEta").val());
                parent.$("#cobRemarks").val($("#cobRemarks").val());
                parent.$("#remarks").val($("#remarks").val());
                parent.$("#headerId").val($("#headerId").val());
                parent.$("#unitssId").val($("#unitssId").val());
                parent.$("#unitId").val($("#unitId").val());
                parent.$("#filterByChanges").val($("#filterByChanges").val());
                showProgressBar();
                parent.$("#methodName").val('saveCob');
                parent.$("#lclAddVoyageForm").submit();
            }
            jQuery(document).ready(function () {
                var cobRadio = $('input:radio[name=cob]:checked').val();
                if (jQuery("#etaValue").val() === jQuery("#verifiedEta").val()) {
                    jQuery('#verifiedEtaCheck').attr('checked', true);
                }
                if (cobRadio !== 'Y') {
                    $("#verifiedEta").removeClass("mandatory").addClass('textWidth');
                    $('#verifiedEtaCheck').attr('disabled', true);
                }
                var existCobValue = jQuery("#existCobValue").val() === 'false' ? false : true;
                var reverseRoleDuty = jQuery("#reverseRoleDuty").val() === 'false' ? false : true;
                if (!reverseRoleDuty && existCobValue) {
                    jQuery("#cobY").attr("disabled", true);
                    jQuery("#cobN").attr("disabled", true);
                    jQuery("#cobRemarks").attr("disabled", true);
                    jQuery("#verifiedEtaCheck").attr("disabled", true);
                    jQuery("#verifiedEta").attr("disabled", true);
                    jQuery("#savecob").attr("disabled", true);
                }

            });

            function checkTermToDoBLAndRateTypes(unitssId) {
                var flag = true;
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
                        methodName: "isValidateTermsToDoBLAndRateType",
                        param1: unitssId,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        if (data[1] === 'alert') {
                            flag = false;
                        }
                        if (!flag) {
                            $.prompt("For File Numbers <span style=color:red>" + data[0] + "</span> Term To Do BL does not belong to the Rate Type selected (Retail/Coload/FTF).  Please correct this problem.");
                        }
                    }
                });
                return flag;
            }

            function submitFormForManifest() {
                var unitId = $("#unitId").val();
                var unitssId = $("#unitssId").val();
                var filterByChanges = $("#filterByChanges").val();
                $("#methodName").val("validateChargesBillToParty");
                $.post($("#lclAddVoyageForm").attr("action"), $("#lclAddVoyageForm").serialize()
                        + "&unitId=" + unitId + "&unitssId=" + unitssId + "&filterByChanges=" + filterByChanges,
                        function (data) {
                            if (data !== "") {
                                $.prompt(data);
                                $("#cobN").attr("checked", true);
                                jQuery("#verifiedEtaCheck").attr('disabled', true);
                                hideProgressBar();
                            } else {
                                hideProgressBar();
                                var existCobValue = jQuery("#existCobValue").val() === 'false' ? false : true;
                                var cobRadio = $('input:radio[name=cob]:checked').val() === 'N' ? false : true;
                                if (existCobValue && cobRadio) {
                                    return false;
                                }
                                $.prompt("Charges will move to Accounting.Are you sure want to Continue?", {
                                    buttons: {
                                        Yes: 1,
                                        No: 2
                                    },
                                    submit: function (v) {
                                        if (v === 1) {
                                            jQuery("#verifiedEtaCheck").attr('disabled', false);
                                            $.prompt.close();
                                        } else if (v === 2) {
                                            $("#cobN").attr("checked", true);
                                            jQuery("#verifiedEtaCheck").attr('disabled', true);
                                            $.prompt.close();
                                        }
                                    }
                                });
                            }
                        });
            }
            function submitFormForUnManifest() {
                var existCobValue = jQuery("#existCobValue").val() === 'false' ? false : true;
                var cobRadio = $('input:radio[name=cob]:checked').val() === 'N' ? false : true;
                if (!existCobValue && !cobRadio) {
                    return false;
                }
                jQuery("#verifiedEtaCheck").attr('disabled', true);
                jQuery("#verifiedEtaCheck").attr('checked', false);
                jQuery("#verifiedEta").val("");
                $.prompt("Do You want to Reverse Charges from Accounting?", {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v === 1) {
                            $("#cobN").attr("checked", true);
                            $.prompt.close();
                        } else if (v === 2) {
                            $("#cobY").attr("checked", true);
                            jQuery("#verifiedEtaCheck").attr('disabled', false);
                            $.prompt.close();
                        }
                    }
                });
            }
            //  Mantis item:14545
            function validateForwardAcct(unitssId) {
                var txt = "";
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
                        methodName: "validateForwardAccount",
                        param1: unitssId,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        if (data !== null && data !== "") {
                            txt = "Forwarder Account is required for the file numbers<span style=color:red>" + data + "</span>.";
                        }
                    }
                });
                return txt;
            }
            // Mantis Item: 14117
            function validateLclBlTerms(unitssId) {
                var txt = "";
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
                        methodName: "isValidateLclBlTerms",
                        param1: unitssId,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        if (data[0] !== null && data[0] !== "" && (data[1] === null || data[1] === "")) {
                            txt = "BL can not be Collect for file numbers  <span style=color:red>" + data[0] + "</span>."
                        } else if (data[1] !== null && data[1] !== "" && (data[0] !== null && data[0] !== "")) {
                            txt = "BL can not be Collect .Please ensure that charges are not bill to Agent for file numbers <span style=color:red>" + data[0] + data[1] + "</span> .";
                        } else if (data[1] !== null && data[1] !== "" && (data[0] === null || data[0] === "")) {
                            txt = "BL can not be Collect .Please ensure that charges are not bill to Agent for file numbers <span style=color:red>" + data[1] + "</span> .";
                        }
                    }
                });
                return txt;
            }
        </script>
    </body>
</html>
