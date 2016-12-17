<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:useBean id="unitTypeDAO" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.UnitTypeDAO"/>
<%--<jsp:useBean id="lclSSMasterBlDAO" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO"/>--%>
<%@include file="init.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<script type="text/javascript" src="${path}/js/tooltip/tooltip.js" ></script>
<%@include file="/taglib.jsp" %>
<jsp:useBean id="lclAddUnitsForm" scope="request" class="com.gp.cvst.logisoft.struts.form.lcl.LclAddUnitsForm"/>
<%    request.setAttribute("unitTypeList", unitTypeDAO.getAllUnittypesForDisplay("0", "1"));
    // request.setAttribute("bookingList", lclSSMasterBlDAO.getAllBookingNumbers(lclAddUnitsForm.getHeaderId()));
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add LCL Units</title>
    </head>
    <body>
        <cong:form  name="lclAddVoyageForm" id="lclAddVoyageForm" action="/lclAddVoyage.do">
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="unitId" name="unitId"/>
            <cong:hidden id="filterByChanges" name="filterByChanges" value="${lclAddVoyageForm.filterByChanges}" />
            <table width="99%" border="0" cellpadding="1" cellspacing="1" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew"><td colspan="8"> Add Unit </td></tr>
            </table>
            <table border="0">
                <tr>
                    <td class="style2">Unit#</td>
                    <td>
                        <cong:text name="unitNo" id="unitNo"  onchange="unitNumberValidate(this);unitNumberExists(this,'${path}');" 
                                   styleClass="mandatory  textuppercaseLetter" value="${lclAddVoyageForm.unitNo}"/>
                        <cong:autocompletor name="unitNo" id="unassignUnit" template="two" position="right" fields=""
                                            query="Un_Assign_Unit_No"  width="130" paramFields="warehouseId" container="NULL" 
                                            styleClass="mandatory textuppercaseLetter"  scrollHeight="300px"/>
                    </td>
                    <td>
                        <input type="checkbox" name="allowfreetext" id="allowfreetext" style="align:floatLeft" onmouseover="tooltip.showSmall('Checked=Allow any Unit# Format<br> UnChecked=Allow only AAAA-NNNNNN-N Unit# Format')" onmouseout="tooltip.hide();"/>
                        <%--     </cong:td>
                             <cong:td width="2%" align="left">--%>
                        <c:choose>
                            <c:when test="${not empty lclAddUnitsForm.unitId && lclAddVoyageForm.unitId!=0}">
                                <span style="cursor:pointer">
                                    <img id="editUnit" src="${path}/img/icons/edit.gif" width="16" height="16" onclick=""/>
                                </span></c:when>
                            <c:otherwise></c:otherwise>
                        </c:choose>
                        <c:if test="${lclAddUnitsForm.filterByChanges ne 'unassignedContainers'}">
                            <cong:autocompletor name="warehouseNo" id="warehouseNo" template="two" position="right" 
                                                fields="wareHouseName,warehouseId,wareHouseAdd,wareHouseCity,wareHouseState,wareHouseZip,wareHousePhone"
                                                query="WAREHOUSE_WITH_WHSENUMBER"  width="250" container="NULL" styleClass="smallTextlabelsBoldForTextBox" 
                                                callback="changeUnitField()" scrollHeight="300px"/>
                            <cong:hidden name="warehouseId" id="warehouseId"/>
                            <input type="hidden" name="wareHouseName" id="wareHouseName"/>
                            <input type="hidden" name="wareHouseAdd" id="wareHouseAdd"/>
                            <input type="hidden" name="wareHouseCity" id="wareHouseCity"/>
                            <input type="hidden" name="wareHouseState" id="wareHouseState"/>
                            <input type="hidden" name="wareHouseZip" id="wareHouseZip"/>
                            <input type="hidden" name="wareHousePhone" id="wareHousePhone"/>
                            <span id="wareHouseDetails">
                                <cong:img src="${path}/img/icons/iicon.png" width="12" height="12"/>
                            </span>
                            <span title="Pick Units from Unassigned Containers<br/>Using Warehouse">
                                <img src="${path}/img/icons/help-icon.gif" width="12" height="12"/>
                            </span>
                        </c:if>
                    </td>

                    <td class="style2" align="right">Size</td><td>
                        <html:select property="unitType" styleId="unitType" styleClass="smallDropDown" >
                            <html:optionsCollection name="unitTypeList"/>
                        </html:select></td>
                    <td class="style2" align="right"> Hazardous </td>
                    <td>
                        <cong:radio value="Y" name="hazmatPermitted" container="NULL" /> Yes
                        <cong:radio value="N" name="hazmatPermitted" container="NULL" /> No
                    </td>

                    <td class="style2" align="right">Refrigeration</td>
                    <td>
                        <cong:radio value="Y" name="refrigerationPermitted" container="NULL" /> Yes
                        <cong:radio value="N" name="refrigerationPermitted" container="NULL" /> No
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td class="style2">Chasis#</td>
                    <td>
                        <cong:text id="chasisNo" name="chasisNo" maxlength="50" styleClass="textuppercaseLetter"/>
                    </td>
                    <td class="style2" align="right"> Stop Off </td>
                    <td class="style2">
                        <cong:radio value="Y" name="stopoff" styleClass="first" container="NULL" /> Yes
                        <cong:radio value="N" name="stopoff" styleClass="second" container="NULL"  onclick="checkStopOff()"/> No
                    </td><td class="style2" align="right">Trucking Information</td>
                    <td class="textlabelsBoldleftforlcl" colspan="2">
                        <html:textarea cols="5" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox" 
                                       styleId="unitTruckRemarks"  property="unitTruckRemarks"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr><td colspan="5">
                        <div style="margin-left:70%;" class="button-style1" onclick="saveUnits()">Save Units</div>
                    </td>
                </tr>
            </table>

        </cong:form>
    </body>
</html>

<script type="text/javascript">
    jQuery(document).ready(function () {
        $("#unassignUnit").hide();
        $("#wareHouseNo").keyup(function () {
            if ($(this).val() === "") {
                var wareHouseDetail = setWareHouseDetrail("WareHouse Details", "", "", "", "", "", "");
                JToolTip('#wareHouseDetails', wareHouseDetail, 300);
                $("#unitNo").show();
                $("#unassignUnit").hide();
            }
        });
    });
    function checkStopOff() {
        if (parent.$("#stopAddList tr").length > 1) {
            $.prompt("You cannot change to NO unless you remove Stop Offs");
            $('.first').attr('checked',true);
        }
    }
    
    function checkSpecialChar(obj) {
        var myString = new String("*&^%$#@{}[]\/!)(");
        for (var i = 0; i < obj.length; i++) {
            if (myString.indexOf(obj.charAt(i)) !== -1 && obj !== '') {
                return true;
                break;
            }
        }
    }

    function unitNumberValidate(obj) {
        var unitchecked = document.getElementById("allowfreetext").checked;
        if (checkSpecialChar(obj.value)) {
            $.prompt("Special Character Not Allowed");
            obj.value = '';
            return false;
        } else {
            if (!unitchecked)
            {
                var unitNo = obj.value;
                unitNo = unitNo.replace(/-/g, '');
                if (unitNo.length < 11 || unitNo.length > 11 || isNotAlphabetic(unitNo.substring(0, 4)) || !isInteger(unitNo.substring(4, 12))) {
                    sampleAlert('Unit number must be "AAAA-NNNNNN-N" in format');
                    return false;
                } else {
                    if (unitNo.lastIndexOf("-") != 11 || unitNo.indexOf("-") != 4) {
                        obj.value = unitNo.substring(0, 4) + "-" + unitNo.substring(4, 10) + "-" + unitNo.substring(10);
                    }
                }
            }
            return true;
        }
    }
    function setCheckBoxOnload(obj) {
        var unitNo = obj.value;
        unitNo = unitNo.replace(/-/g, '');
        if (unitNo.length < 11 || unitNo.length > 11 || isNotAlphabetic(unitNo.substring(0, 4)) || !isInteger(unitNo.substring(4, 12))) {
            document.getElementById("allowfreetext").checked = true;
        }
    }

    function unitNumberExists(obj, path) {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "checkUnitNumberExists",
                param1: "",
                param2: obj.value,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data[1] !== "" && data[1] !== null) {
                    $.prompt("Unit# " + obj.value + ' Already Exists in Voyage# ' + data[1] + '.Do you want to open the same unit?', {
                        buttons: {
                            Yes: 1,
                            No: 2
                        },
                        submit: function (v) {
                            if (v == 1) {
                                window.location.href = path + "/lclAddVoyage.do?methodName=addUnitForDomesticVoyage&unitId=" + data[2];
                            }
                        }
                    });
                }
            }
        });

    }
    function changeUnitField() {
        $("#unitNo").hide();
        $("#unassignUnit").show();
        var name = $("#wareHouseName").val();
        var address = $("#wareHouseAdd").val();
        var city = $("#wareHouseCity").val();
        var state = $("#wareHouseState").val();
        var zip = $("#wareHouseZip").val();
        var phone = $("#wareHousePhone").val();
        var wareHouseDetail = setWareHouseDetrail("WareHouse Details", name, address, city, state, zip, phone);
        JToolTip('#wareHouseDetails', wareHouseDetail, 300);
    }

    function setWareHouseDetrail(heading, name, address, city, state, zip, phone) {
        var information = "<table>";
        information += "<tr><td colspan='2' align='left'><FONT size='2' COLOR=#008000><b>" + heading + "</b></FONT></td></tr>";
        information += "<tr><td align='right'><FONT COLOR=red>NAME:</FONT> </td><td>" + name + "</td></tr>";
        information += "<tr><td align='right'><FONT COLOR=red>ADDRESS:</FONT> </td><td>" + address + "</td></tr>";
        information += "<tr><td align='right'><FONT COLOR=red>CITY:</FONT> </td><td>" + city + "</td></tr>";
        information += "<tr><td align='right'><FONT COLOR=red>STATE:</FONT> </td><td>" + state + "</td></tr>";
        information += "<tr><td align='right'><FONT COLOR=red>ZIP:</FONT> </td><td>" + zip + "</td></tr>";
        information += "<tr><td align='right'><FONT COLOR=red>PHONE:</FONT> </td><td>" + phone + "</td></tr>";
        information += "</table>";
        return information;
    }
    function saveUnits() {
        var unitNumber = $("#unitNo").val() === '' ? $("#unassignUnit").val() : $("#unitNo").val();
        if (unitNumber === null || unitNumber === "") {
            sampleAlert("Unit Number is required");
            $("#unitNo").css("border-color", "red");
            $("#warning").parent.show();
            return false;
        }
        showProgressBar();
        parent.parent.$("#unitId").val($("#unitId").val());
        parent.parent.$("#unitNo").val($("#unitNo").val() === '' ? $("#unassignUnit").val() : $("#unitNo").val());
        parent.parent.$("#unitType").val($("#unitType").val());
        parent.parent.$("#hazmatPermittedUnit").val($('input:radio[name=hazmatPermitted]:checked').val());
        parent.parent.$("#refrigerationPermittedUnit").val($('input:radio[name=refrigerationPermitted]:checked').val());
        parent.parent.$("#stopoff").val($('input:radio[name=stopoff]:checked').val());
        parent.parent.$("#chasisNo").val($("#chasisNo").val());
        parent.parent.$("#unitTruckRemarks").val($("#unitTruckRemarks").val());
        parent.$.fn.colorbox.close();
    }

</script>