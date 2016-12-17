<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:useBean id="unitTypeDAO" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.UnitTypeDAO"/>
<jsp:useBean id="lclSSMasterBlDAO" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO"/>
<%@include file="init.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<%@include file="/taglib.jsp" %>
<jsp:useBean id="lclAddUnitsForm" scope="request" class="com.gp.cvst.logisoft.struts.form.lcl.LclAddUnitsForm"/>
<%    request.setAttribute("unitTypeList", unitTypeDAO.getAllUnittypesForDisplay("0", "1"));
    request.setAttribute("bookingList", lclSSMasterBlDAO.getAllBookingNumbers(lclAddUnitsForm.getHeaderId()));
    lclAddUnitsForm.setWarehouseName("");
    lclAddUnitsForm.setDepartedDateTime("");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add LCL Units</title>
    </head>
    <body>
        <cong:form action="/lclAddUnits" name="lclAddUnitsForm" id="lclAddUnitsForm">
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="unLocationId" name="unLocationId"/>
            <cong:hidden name="unitId" id="unitId" />
            <cong:hidden name="unitssId" id="unitssId" />
            <cong:hidden name="cob" id="cob" value="${lclAddUnitsForm.cob}"/>
            <cong:hidden id="filterByChanges" name="filterByChanges" value="${lclAddVoyageForm.filterByChanges}" />
            <cong:hidden id="headerId" name="headerId" />
            <cong:hidden name="unitWarehouseId" id="unitWarehouseId" />
            <cong:hidden name="unitDispoId" id="unitDispoId" />
            <cong:hidden name="location" id="location" />
            <cong:hidden name="arrivedDateTime" id="arrivedDateTime" />
            <cong:hidden name="departedDateTime" id="departedDateTime" />
            <cong:hidden name="warehouseId" id="warehouseId" />
            <cong:hidden name="sealNoOut" id="sealNoOut"/>
            <cong:hidden id="unitsReopened" name="unitsReopened" value="${unitsReopened}"/>
            <cong:hidden id="oldUnitId" name="oldUnitId"  />
            <cong:hidden name="oldUnitNo" id="oldUnitNo" value="${lclAddUnitsForm.unitNo}"/>
            <input type="hidden" name="existUnitNo" id="existUnitNo" value="${lclAddUnitsForm.unitNo}"/>
            <input type="hidden" name="existUnitTypeId" id="existUnitTypeId" value="${lclAddUnitsForm.unitType}"/>
            <input type="hidden" name="existUnitId" id="existUnitId" value="${lclAddUnitsForm.unitId}"/>
            <input type="hidden" name="setLabel" id="setLabelId" value="${setLabel}"/>
            <table width="99%" border="0" cellpadding="1" cellspacing="1" class="tableBorderNew" align="center">
                <fmt:formatDate pattern="dd-MMM-yyyy hh:mm" var="modifiedDate" value="${modDate}"/>
                <tr class="tableHeadingNew">
                    <td>
                        <c:choose>
                            <c:when test="${not empty lclAddUnitsForm.unitId && lclAddVoyageForm.unitId!=0 && !setLabel}">
                                Edit Unit# <span class="fileNo">${lclAddUnitsForm.unitNo} Opened: {${modifiedDate}}</span>
                            </c:when>
                            <c:otherwise>
                                Add Unit
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table border="0" width="100%">
                            <tr>
                                <td class="textlabelsBoldforlcl" 
                                    style="padding-top:1em; padding-left:5em;" width="10%">
                                    Unit#
                                </td>
                                <td width="10%" style="padding-top:1em;">
                                    <c:choose>
                                        <c:when test="${not empty lclAddUnitsForm.unitId && lclAddVoyageForm.unitId ne 0 &&  !setLabel}">
                                            <cong:text name="unitNo" id="unitNo"  onchange="unitNumberExists();"
                                                       readOnly="true" styleClass="mandatory textlabelsBoldForTextBoxDisabledLook textuppercaseLetter"/>
                                        </c:when>
                                        <c:when test="${lclAddUnitsForm.filterByChanges eq 'unassignedContainers' && !setLabel}">
                                            <cong:text name="unitNo" id="unitNo" styleClass="mandatory  textuppercaseLetter" 
                                                       onchange="unitNumberValidate();"/>
                                        </c:when>
                                        <c:otherwise>
                                            <cong:text name="unitNo" id="unitNo"  onchange="unitNumberExists();unitNumberValidate();"
                                                       styleClass="mandatory  textuppercaseLetter" />
                                        </c:otherwise>
                                    </c:choose>
                                    <c:if test="${setLabel}">
                                        <html:select property="unitNo"  styleId="unassignUnit" styleClass="smallDropDown"
                                                     onchange="unitNumberExists();unitComments(this);" >
                                            <html:optionsCollection name="unAssignList"/>
                                        </html:select>
                                    </c:if>

                                    <input type="checkbox" name="allowfreetext" id="allowfreetext" style="vertical-align: middle;"
                                           title="Checked=Allow any Unit# Format<br> UnChecked=Allow only AAAA-NNNNNN-N Unit# Format')"/>
                                    <c:if test="${lclAddUnitsForm.filterByChanges ne 'unassignedContainers' }">
                                        <input type="checkbox" name="unassignedCheckBox" id="unassignedCheckBox"
                                               style="vertical-align: middle;" title="Click to find and select an unassigned container"/>
                                    </c:if>
                                    <c:if test="${not empty lclAddUnitsForm.unitId && lclAddVoyageForm.unitId!=0}">
                                        <img id="editUnit" title="Edit Unit#" style="cursor:pointer;vertical-align: middle;" alt="Edit"
                                             src="${path}/img/icons/edit.gif" width="16" height="16"/>
                                    </c:if>
                                    <c:if test="${lclAddUnitsForm.filterByChanges ne 'unassignedContainers'}">
                                        <cong:autocompletor name="wareHouseNo" id="wareHouseNo" template="two" position="right"
                                                            fields="wareHouseName,wareHouseId,wareHouseAdd,wareHouseCity,wareHouseState,wareHouseZip,wareHousePhone"
                                                            query="WAREHOUSE_WITH_WHSENUMBER"  width="250" container="NULL" styleClass="smallTextlabelsBoldForTextBox"
                                                            callback="changeUnitField()" scrollHeight="300px"/>
                                        <cong:hidden name="wareHouseId" id="wareHouseId"/>
                                        <input type="hidden" name="wareHouseName" id="wareHouseName"/>
                                        <input type="hidden" name="wareHouseAdd" id="wareHouseAdd"/>
                                        <input type="hidden" name="wareHouseCity" id="wareHouseCity"/>
                                        <input type="hidden" name="wareHouseState" id="wareHouseState"/>
                                        <input type="hidden" name="wareHouseZip" id="wareHouseZip"/>
                                        <input type="hidden" name="wareHousePhone" id="wareHousePhone"/>
                                        <span  id="wareHouseDetails" title="Please enter warehouse">
                                            <cong:img src="${path}/img/icons/iicon.png" width="12" height="12"/>
                                        </span>
                                    </c:if>
                                </td>
                                <td></td>                               
                                <td class="textlabelsBoldforlcl" style="padding-top:1em;">Seal#</td>
                                <td class="style2" align="left" style="padding-top:1em;">
                                    <cong:text id="sealNo" name="sealNo" maxlength="20" styleClass="textuppercaseLetter"
                                               style="width :150px"/>
                                </td>                                
                                <td class="textlabelsBoldforlcl"style="padding-top:1em;">Size</td>
                                <td style="padding-top:1em;">
                                    <html:select property="unitType" styleId="sizeId" onchange="unitComments();" styleClass="smallDropDown" >
                                        <html:optionsCollection name="unitTypeList"/>
                                    </html:select>
                                </td>
                                <c:if test="${lclAddUnitsForm.filterByChanges eq 'unassignedContainers' }">  
                                    <td></td>
                                </c:if>   
                                <td class="textlabelsBoldforlcl" style="padding-top:1em;">Hazardous</td>
                                <td class="textBoldforlcl" style="padding-top:1em;">
                                    <cong:radio value="Y" name="hazmatPermitted" id="hazmatPermitted" container="NULL" /> Yes
                                    <cong:radio value="N" name="hazmatPermitted" container="NULL" /> No
                                </td>

                                <td class="textlabelsBoldforlcl" style="padding-top:1em;">Refrigeration</td>
                                <td class="textBoldforlcl" style="padding-top:1em;">
                                    <cong:radio value="Y" name="refrigerationPermitted"   id="refrigerationPermitted" container="NULL" /> Yes
                                    <cong:radio value="N" name="refrigerationPermitted" container="NULL" /> No
                                </td>
                            </tr>                            
                            <tr>                                  
                                <td colspan="3" rowspan="3">                                  
                                    <table border="0" class="" >                                      
                                        <tr>
                                            <td colspan="2" class="bold" 
                                                style="padding-top:0.7em; padding-left:12em;text-decoration:underline;">
                                                SOLAS/VGM information
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="padding-top:0.7em; padding-left:4.8em;" 
                                                class="textlabelsBoldforlcl hideunassignedContainer">Cargo Weight&nbsp;&nbsp;</td>
                                            <td style="padding-top:0.7em; padding-right:2em;"  class="hideunassignedContainer">
                                                <cong:text id="dunnageWeightLbs" name="dunnageWeightLbs" style="width:70px;" styleClass="bold" 
                                                           onkeyup="checkForNumberAndDecimal(this);" onchange="convertToKGS('dunnageWeightLbs','dunnageWeightKgs');" />LBS
                                                <cong:text id="dunnageWeightKgs" name="dunnageWeightKgs" style="width:70px;" styleClass="bold" 
                                                           onkeyup="checkForNumberAndDecimal(this);" onchange="convertToLBS('dunnageWeightKgs','dunnageWeightLbs');"/>KGS
                                                <img src="./img/icons/calc.png" id="bookingWeightTotal" 
                                                     onclick="getPickedBookingWeightTotal('dunnageWeightKgs', 'dunnageWeightLbs')"
                                                     title="Calculate Booking Weight Total" height="14" width="16"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="padding-top:0.7em;" class="textlabelsBoldforlcl">Dunnage Weight&nbsp;&nbsp;</td>
                                            <td style="padding-top:0.7em;">
                                                <cong:text id="tareWeightLbs" name="tareWeightLbs" style="width:70px;" styleClass="bold" 
                                                           onkeyup="checkForNumberAndDecimal(this);" onchange="convertToKGS('tareWeightLbs','tareWeightKgs');"/>LBS
                                                <cong:text id="tareWeightKgs" name="tareWeightKgs" style="width:70px;" styleClass="bold" 
                                                           onkeyup="checkForNumberAndDecimal(this);" onchange="convertToLBS('tareWeightKgs','tareWeightLbs');"/>KGS
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="padding-top:0.7em;" class="textlabelsBoldforlcl">Tare Weight&nbsp;&nbsp;</td>
                                            <td style="padding-top:0.7em;">
                                                <cong:text id="cargoWeightLbs" name="cargoWeightLbs" style="width:70px;" styleClass="bold"
                                                           onkeyup="checkForNumberAndDecimal(this);" onchange="convertToKGS('cargoWeightLbs','cargoWeightKgs');"/>LBS
                                                <cong:text id="cargoWeightKgs" name="cargoWeightKgs" style="width:70px;" styleClass="bold"
                                                           onkeyup="checkForNumberAndDecimal(this);" onchange="convertToLBS('cargoWeightKgs','cargoWeightLbs');"/>KGS

                                            </td>      
                                        </tr>           
                                    </table>
                                </td>                                
                                <td colspan="8" class="">
                                    <table border="0" width="100%">
                                        <tr>
                                            <td class="textlabelsBoldforlcl">Booking#</td>
                                            <td >
                                                <html:select property="bookingNumber" styleId="bookingNumber"
                                                             styleClass="smallDropDown135" onchange="getMasterBlStatus(this);" >
                                                    <html:optionsCollection name="bookingList"/>
                                                </html:select>
                                            </td>  
                                            <td class="textlabelsBoldforlcl hideunassignedContainer">InterModal Provided</td>
                                            <td width="10%" class="textBoldforlcl hideunassignedContainer">
                                                <cong:radio value="Y" name="intermodalProvided" container="NULL" /> Yes
                                                <cong:radio value="N" name="intermodalProvided" container="NULL" /> No
                                            </td>
                                            
                                             <c:if test="${lclAddUnitsForm.filterByChanges eq 'unassignedContainers' }">  
                                                <td width="13%" class="textlabelsBoldforlcl ">Load By </td>
                                                <td>
                                                    <cong:autocompletor name="loadedBy" id="loadedBy" template="two" fields="NULL,loaddeByUserId" 
                                                                        query="WAREHOUSE_ACTIVATE_LOGINNAME" width="300"
                                                                        styleClass="text weight textlabelsBoldForTextBoxWidthShort" 
                                                                        container="NULL" scrollHeight="200" focusOnNext="true" shouldMatch="true" />
                                                    <cong:hidden id="loaddeByUserId" name="loaddeByUserId"/>
                                                </td>
                                                </c:if>
                                            <td class="textlabelsBoldforlcl hideunassignedContainer" >Stop Off</td>
                                            <td class="textBoldforlcl hideunassignedContainer">
                                                <cong:radio value="Y" name="stopoff" container="NULL" styleClass ="first"/> Yes
                                                <cong:radio value="N" name="stopoff" container="NULL" styleClass ="second" onclick="checkStopOff();"/> No
                                            </td>                                           
                                            <td class="textlabelsBoldforlcl">Trucking Information</td>
                                            <td class="textlabelsBoldleftforlcl" colspan="2">
                                                <html:textarea cols="5" rows="15" style="width:180px" styleClass="refusedTextarea textlabelsBoldForTextBox" styleId="remarks" property="remarks"/>
                                            </td>                                            
                                        </tr>
                                        <tr>
                                            <td class="textlabelsBoldforlcl">Comments</td>
                                            <td class="textBoldforlcl">
                                                <cong:text id="comments" name="comments" onchange="checkComments();" maxlength="25" styleClass="textuppercaseLetter"/>
                                            </td>
                                             <c:if test="${lclAddUnitsForm.filterByChanges ne 'unassignedContainers' }">  
                                            <td colspan=""></td>
                                             </c:if>
                                            <c:if test="${lclAddUnitsForm.filterByChanges eq 'unassignedContainers' }">                                                 
                                                <td class="textlabelsBoldforlcl">Chassis#</td>
                                                <td><cong:text id="chasisNo" name="chasisNo" maxlength="50" styleClass="textuppercaseLetter"/>
                                                
                                            </c:if>
                                        </tr>
                                    </table>
                                </td>

                            </tr>
                            <table width="100%" border="0">
                                <tr>
                                    <td width="10%" style="padding-top:0.7em;" class="textlabelsBoldforlcl hideunassignedContainer">Verification Signature</td>
                                    <td width="14.9%" style="padding-top:0.7em;" class="hideunassignedContainer">
                                        <cong:text id="verificationSignature" name="verificationSignature"
                                                   style="width:170px" maxlength="40" styleClass="textuppercaseLetter"/>
                                    </td>
                                    <td width="1%" class="textlabelsBoldforlcl hideunassignedContainer">Received Master</td>
                                    <td width="1%" class="textBoldforlcl hideunassignedContainer">
                                        <cong:radio value="N" id ="noMasterBl" name="receivedMaster" disabled="true" container="NULL"/>No
                                    </td>
                                    <td width="1%" class="textBoldforlcl hideunassignedContainer">
                                        <cong:radio value="Approved" id ="approved" name="receivedMaster" disabled="true" container="NULL"/>Approved
                                    </td>
                                    <td width="1%" class="textBoldforlcl hideunassignedContainer">
                                        <cong:radio value="Disputed" id ="disputed" name="receivedMaster" disabled="true" container="NULL"/>Disputed
                                    </td>

                                    <td width="13%" class="textlabelsBoldforlcl hideunassignedContainer ">Load By </td>
                                    <td class="hideunassignedContainer">
                                        <cong:autocompletor name="loadedBy" id="loadedBy" template="two" fields="NULL,loaddeByUserId" 
                                                            query="WAREHOUSE_ACTIVATE_LOGINNAME" width="300"
                                                            styleClass="text weight textlabelsBoldForTextBoxWidthShort" 
                                                            container="NULL" scrollHeight="200" focusOnNext="true" shouldMatch="true" />
                                        <cong:hidden id="loaddeByUserId" name="loaddeByUserId"/>
                                    </td>
                                    <td width="30%" class="textlabelsBoldforlcl hideunassignedContainer">Drayage Provided by SSline
                                        <cong:radio value="Y" name="drayageProvided" container="NULL" /> Yes
                                        <cong:radio value="N" name="drayageProvided" container="NULL" /> No
                                        <cong:radio value="I" name="drayageProvided" container="NULL" />Included
                                    </td>
                                    <td width="8%" align="right"></td>
                                </tr>
                            </table>
                            <table border="0" width="100%">
                                <tr>
                                    <td width="9.8%" style="padding-top:0.7em;" class="textlabelsBoldforlcl hideunassignedContainer">Verification Date</td>
                                    <td width="15%" style="padding-top:0.7em;" class="hideunassignedContainer">
                                        <cong:calendarNew id="verificationDate" name="verificationDate" 
                                                          value=""/>
                                    </td>
                                    <td width="3%" class="textlabelsBoldforlcl hideunassignedContainer">Chassis#</td>
                                    <td width="15%" class="hideunassignedContainer">
                                        <cong:text id="chasisNo" name="chasisNo" maxlength="50" styleClass="textuppercaseLetter"/>
                                    </td>
                                    <c:if test="${lclAddUnitsForm.filterByChanges eq 'unassignedContainers'}">
                                        <td width="9.8%" style="padding-top:0.7em;"></td>     
                                        <td width="15%" style="padding-top:0.7em;"></td>
                                        <td width="3%">
                                        <td width="15%"></td>
                                    </c:if>     
                                    <td>
                                        <c:choose>
                                            <c:when test="${lclAddUnitsForm.filterByChanges eq 'unassignedContainers'}">
                                                <div class="button-group">
                                                    <div class="button-style1" onclick="checkIfUnitExistsInWarehouse()">Save Unit</div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="button-group">
                                                    <div class="button-style1" onclick="saveUnits()">Save Units</div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </table>
                        </table>
                        <table border="0" width="100%">
                            <tr>
                                <td width="50%" style="vertical-align: top;">
                                    <%@include  file="/jsps/LCL/export/voyage/units/addOrEditWarehouse.jsp" %>
                                </td>
                                <td width="50%">
                                    <%@include  file="/jsps/LCL/export/voyage/units/addOrEditDisposition.jsp" %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </cong:form>
    </body>
</html>
<c:if test="${setLabel}" >
    <script>
        $("#unassignedCheckBox").attr("checked", true);
        $("#wareHouseNo").show();
        $("#wareHouseDetails").show();
        $("#unitNo").val('');
        $("#unitNo").hide();</script>
    </c:if>
<script type="text/javascript">
    jQuery(document).ready(function () {
        if ($('#filterByChanges').val() === "unassignedContainers") {
            $(".hideunassignedContainer").hide();
        }
        if ($("#unassignedCheckBox").is(":checked")) {
            $("#wareHouseNo").show();
            $("#wareHouseDetails").show();
        } else {
            $("#wareHouseNo").hide();
            $("#wareHouseDetails").hide();
        }
        $('#filterByChanges').val(parent.$('#filterByChanges').val());
        if ($("#unitId").val() != null && $("#unitId").val() != "0" && $("#unitId").val() != "" && $("#unitId").val() != undefined)
        {
            document.getElementById('lclWarehouseDiv').style.position = "relative";
            document.getElementById('lclWarehouseDiv').style.visibility = "visible";
            document.getElementById('lclDispositionDiv').style.position = "relative";
            document.getElementById('lclDispositionDiv').style.visibility = "visible";
            setCheckBoxOnload();
            if (parent.$('#hazStatus').val() === 'true') {
                $('#hazmatPermitted').attr('checked', 'true');
            }
        }
        if ($('#filterByChanges').val() === 'unassignedContainers') {
            $('#lclDispositionDiv').hide();
        }
        $("#editUnit").click(function () {
            var txt = "Are You sure, you want to edit Unit#";
            $.prompt(txt, {
                buttons: {
                    Ok: 1,
                    Cancel: 2
                },
                submit: function (v) {
                    if (v == 1) {
                        $("#unitNo").removeClass("textlabelsBoldForTextBoxDisabledLook");
                        $('#unitNo').attr("readonly", false);
                    } else {
                        $("#unitNo").addClass("textlabelsBoldForTextBoxDisabledLook");
                    }
                }
            }
            );
        });
        $("#loadedBy").keyup(function () {
            if ($(this).val() === "") {
                $('#loaddeByUserId').val('');
            }
        });
        $("#wareHouseNo").keyup(function () {
            if ($(this).val() === "") {
                var wareHouseDetail = setWareHouseDetrail("WareHouse Details", "", "", "", "", "", "");
                JToolTip('#wareHouseDetails', wareHouseDetail, 300);
                $("#unitNo").show();
                $("#unassignUnit").hide();
            }
        });
        $("#unassignedCheckBox").click(function () {
            if ($("#unassignedCheckBox").is(":checked")) {
                $("#wareHouseNo").show();
                $("#wareHouseDetails").show();
            } else {
                $("#setLabelId").val('');
                $("#wareHouseNo").val('');
                $("#wareHouseNo").hide();
                $("#wareHouseDetails").hide();
                $("#unassignUnit").hide();
                $("#unitNo").show();
            }
        });
    });
    function checkStopOff() {
        if (parent.$("#lclssDetail tr").length > 2) {
            $.prompt("You cannot change to NO unless you remove Stop Offs");
            $('.first').attr('checked', true);
        }
    }
    function backtoSearchScreen() {
        $("#methodName").val('goBack');
        $("#lclAddUnitsForm").submit();
    }
    function saveUnits() {
        //    alert("1.save the unit");
        var filterByChanges = parent.$('#filterByChanges').val();
        $('#filterByChanges').val(filterByChanges);
        var warehouseCon = $('#setLabelId').val();
        var unitNumber = "";
        if (warehouseCon === 'true') {
            unitNumber = $("#unassignUnit").val();
        } else {
            unitNumber = $("#unitNo").val();
        }
        var unitId = $("#unitId").val();
        if (unitNumber == null || unitNumber == "") {
            $.prompt("Unit Number is required");
            $("#unitNo").css("border-color", "red");
            return false;
        }
        if (!unitNumberValidate()) {
            return false;
        }
        submitForm(unitId, unitNumber);
    }
    function addWarehouse(path) {
        var headerId = $('#headerId').val();
        var href = path + "/lclEditWarehouse.do?methodName=editWarehouse&headerId=" + headerId
        $.colorbox({
            iframe: true,
            href: href,
            width: "70%",
            height: "50%",
            title: "<span style=color:blue>Warehouse</span>"
        });
    }
    function deleteWarehouse(unitWarehouseId) {
        var txt = 'Are you sure want to delete disposition?';
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    showProgressBar();
                    $("#methodName").val('deleteWareHouse');
                    document.getElementById("unitWarehouseId").value = unitWarehouseId;
                    $("#lclAddUnitsForm").submit();
                    hideProgressBar();
                } else if (v == 2) {
                    $.prompt.close();
                }
            }
        });
    }
    function deleteDisposition(dispositionId) {
        var txt = 'Are you sure want to delete warehouse?';
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    showProgressBar();
                    $("#methodName").val('deleteDisposition');
                    $("#unitDispoId").val(dispositionId);
                    $("#lclAddUnitsForm").submit();
                    hideProgressBar();
                } else if (v == 2) {
                    $.prompt.close();
                }
            }
        });
    }
    function submitForm(unitId, unitNumber) {
        showProgressBar();
        parent.$("#methodName").val('saveUnits');
        parent.document.getElementById("unitId").value = unitId;
        parent.document.getElementById("unitssId").value = document.getElementById("unitssId").value;
        parent.$("#unitNo").val(unitNumber);
        parent.document.getElementById("unitType").value = document.lclAddUnitsForm.unitType.value;
        parent.document.getElementById("hazmatPermittedUnit").value = $('input:radio[name=hazmatPermitted]:checked').val();
        parent.document.getElementById("refrigerationPermittedUnit").value = $('input:radio[name=refrigerationPermitted]:checked').val();
        parent.document.getElementById("drayageProvided").value = $('input:radio[name=drayageProvided]:checked').val();
        parent.document.getElementById("intermodalProvided").value = $('input:radio[name=intermodalProvided]:checked').val();
        parent.document.getElementById("stopoff").value = $('input:radio[name=stopoff]:checked').val();
        parent.document.getElementById("chasisNo").value = document.lclAddUnitsForm.chasisNo.value;
        parent.$("#receivedMaster").val($("input:radio[name=receivedMaster]:checked").val());
        parent.document.getElementById("sealNo").value = document.lclAddUnitsForm.sealNo.value;
        parent.document.getElementById("remarks").value = document.lclAddUnitsForm.remarks.value;
        parent.document.getElementById("unitsReopened").value = document.lclAddUnitsForm.unitsReopened.value;
        parent.document.getElementById("bookingNumber").value = document.lclAddUnitsForm.bookingNumber.value;
        parent.document.getElementById("oldUnitId").value = document.lclAddUnitsForm.oldUnitId.value;
        parent.document.getElementById("loadedBy").value = document.lclAddUnitsForm.loadedBy.value;
        parent.document.getElementById("loaddeByUserId").value = document.lclAddUnitsForm.loaddeByUserId.value;
        parent.$("#dunnageWeightLbs").val($("#dunnageWeightLbs").val());
        parent.$("#dunnageWeightKgs").val($("#dunnageWeightKgs").val());
        parent.$("#tareWeightLbs").val($("#tareWeightLbs").val());
        parent.$("#tareWeightKgs").val($("#tareWeightKgs").val());
        parent.$("#cargoWeightLbs").val($("#cargoWeightLbs").val());
        parent.$("#cargoWeightKgs").val($("#cargoWeightKgs").val());
        parent.$("#verificationSignature").val($("#verificationSignature").val());
        parent.$("#comments").val($("#comments").val());
        parent.$("#verificationDate").val($("#verificationDate").val());
        parent.$('#cob').val($('#cob').val());
        parent.$("#lclAddVoyageForm").submit();
    }
    function unitNumberValidate() {
        var warehouseCon = $('#setLabelId').val();
        var unitNo = $("#unitNo").val();
        if (warehouseCon == '') {
            var unitchecked = document.getElementById("allowfreetext").checked;
            if (!unitchecked && unitNo != '') {
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
        } else {
            return true;
        }
    }


    function setCheckBoxOnload() {
        var unitNo = $("#unitNo").val() === '' ? $("#unassignUnit").val() : $("#unitNo").val();
        unitNo = unitNo.replace(/-/g, '');
        if (unitNo.length < 11 || unitNo.length > 11 || isNotAlphabetic(unitNo.substring(0, 4)) || !isInteger(unitNo.substring(4, 12))) {
            document.getElementById("allowfreetext").checked = true;
        }
    }
    function unitNumberExists() {
        var filterByChanges = $('#filterByChanges').val();
        unitNumberValidate();
        if (filterByChanges !== "unassignedContainers") {

            var unitId = document.getElementById("unitId").value;
            var warehouseCon = $('#setLabelId').val();
            var unitNumber = "";
            if (warehouseCon === 'true') {
                unitNumber = $("#unassignUnit").val();
            } else {
                unitNumber = $("#unitNo").val();
            }
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
            //}
        }
    }
    function checkIfUnitExistsInWarehouse() {
        unitNumberValidate();
        var unitNumber = $("#unitNo").val();
        var warehouseno = parent.$("#warehouseNo").val();
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getUnitAndWarehouse",
                param1: unitNumber,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                unitNumber = "<span class='red'>" + unitNumber.toUpperCase() + "</span>";
                warehouseno = "<span class='red'>" + warehouseno + "</span>";
                if (data[1] == "" || data[1] == null || data[1] == undefined || $("#editUnit").val() == "") {
                    saveUnassignedContainer();
                }
                else if (warehouseno == data[1]) {
                    sampleAlert("Unit# " + unitNumber + " Already Exists in Warehouse " + warehouseno);
                    return false;
                }
                else {
                    var txt = "Unit# " + unitNumber + " Already Exists in Warehouse <span class='red'>" + data[1] + "</span> Do you want to open the same Unit?";
                    $.prompt(txt, {
                        buttons: {
                            Yes: 1,
                            No: 2
                        },
                        submit: function (v) {
                            if (v == 1) {
                                saveUnassignedContainer();
                            }
                            else if (v == 2) {
                                $.prompt.close();
                            }
                        }
                    });
                }
            }
        });
    }
    function unitCheckCurrentVoyage(data, unitNumber, unitId) {
        if (data[0] == "1") {
            $.prompt("Unit Number Already Exists in current voyage.");
            $("#unassignUnit").val('');
            $('#unitId').val($('#existUnitId').val());
            $('#unitType').val($('#existUnitTypeId').val());
            $('#unitNo').val($('#existUnitNo').val());
            $('#oldUnitId').val('');
            return false;
        } else if (data[3] == unitNumber) {
            $.prompt("Unit Number Already Exists.");
            $("#unassignUnit").val('');
            $('#unitId').val($('#existUnitId').val());
            $('#unitType').val($('#existUnitTypeId').val());
            $('#unitNo').val($('#existUnitNo').val());
            $('#oldUnitId').val('');
            return false;
        }
        else if (data[2] != null && data[2] != undefined && data[2] != "") {
            txt = 'Unit# <span style=color:red>' + unitNumber.toUpperCase() + '</span> Already Exists';
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
                        showProgressBar();
                        $("#unitId").val(data[2]);
                        $("#comments").val(data[5]);
                        $("#remarks").val(data[6]);
                        if (data[7] === 'true') {
                            $('#hazmatPermitted').attr('checked', 'true');
                        }
                        if (data[8] === 'true') {
                            $('#refrigerationPermitted').attr('checked', 'true');
                        }
                        $("#sizeId").val(data[9]);                        
                        $("#cargoWeightLbs").val(data[10]);
                        $("#cargoWeightKgs").val(data[11]);
                        $("#chasisNo").val(data[12]);
                        $("#sealNo").val(data[13]);
                        $("#loadedBy").val(data[14]);
                        $("#loaddeByUserId").val(data[15]);                        
                        $("#oldUnitId").val(unitId);
                        document.lclAddUnitsForm.unitsReopened.value = "Y";
                        hideProgressBar();
                        $.prompt.close();
                    }
                    else if (v == 2) {
                        var warehouseCon = $('#setLabelId').val();
                        if (warehouseCon === 'true') {
                            $("#unassignUnit").val('');
                        }
                        $('#unitId').val($('#existUnitId').val());
                        $('#unitType').val($('#existUnitTypeId').val());
                        $('#unitNo').val($('#existUnitNo').val());
                        $('#oldUnitId').val('');
                        $('#comments').val('');
                        $('#sizeId').val('');
                        $.prompt.close();
                    }
                }
            });
        } else {
            if ($('#existUnitId').val() != '') {
                $('#oldUnitId').val($('#existUnitId').val());
                document.lclAddUnitsForm.unitsReopened.value = "Y";
            }
            $("#unitId").val('');
        }
    }
    function unitCheckCurrentVoyageForSave(data, unitNumber, unitId)
    {
        if (data[0] == "1") {
            sampleAlert("Unit Number Already Exists in current voyage.");
            return false;
        }
        else if (data[2] != null && data[2] != undefined && data[2] != "") {
            txt = 'Unit# ' + unitNumber + ' Already Exists ';
            if (data[1] != null && data[1] != undefined && data[1] != "")
            {
                txt += 'in Voyage# ' + data[1];
            }
            txt += '.Do you want to open the same unit?';
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v == 1) {
                        showProgressBar();
                        document.getElementById("unitId").value = data[2];
                        document.getElementById("oldUnitId").value = unitId;
                        document.lclAddUnitsForm.unitsReopened.value = "Y";
                        $("#lclAddUnitsForm").submit();
                        hideProgressBar();
                        $.prompt.close();
                    }
                    else if (v == 2) {
                        $.prompt.close();
                    }
                }
            });
        }
        else
        {
            submitForm(unitId);
        }
    }
    function saveUnassignedContainer() {
        $("#methodName").val('saveUnassignedContainer');
        var parmas = $("#lclAddUnitsForm").serialize();
        $.post($("#lclAddUnitsForm").attr("action"), parmas,
                function (data) {
                    parent.$.fn.colorbox.close();
                    parent.$("#unitId").val('');
                    parent.$("#methodName").val('searchByUnAssignUnit');
                    parent.$("#lclUnitsScheduleForm").submit();
                });
    }
    function changeUnitField() {
        $("#unitNo").hide();
        $("#unitNo").val("");
        $("#unassignUnit").show();
        var name = $("#wareHouseName").val();
        var address = $("#wareHouseAdd").val();
        var city = $("#wareHouseCity").val();
        var state = $("#wareHouseState").val();
        var zip = $("#wareHouseZip").val();
        var phone = $("#wareHousePhone").val();
        var wareHouseDetail = setWareHouseDetrail("WareHouse Details", name, address, city, state, zip, phone);
        JToolTip('#wareHouseDetails', wareHouseDetail, 300);
        $("#methodName").val('unAssignedUnit');
        $("#lclAddUnitsForm").submit();
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
    function getMasterBlStatus(ele) {
        if ($("#" + ele.id).val() !== "") {
            $.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO",
                    methodName: "getMasterBlDocumentStatus",
                    param1: $("#" + ele.id).val(),
                    param2: $("#headerId").val()
                },
                success: function (data) {
                    if (data !== '') {
                        data === "Approved" ? $("#approved").attr("checked", true) : $("#disputed").attr("checked", true);
                    } else {
                        $("#noMasterBl").attr("checked", true);
                    }
                }
            });
        } else {
            $("#noMasterBl").attr("checked", true);
        }
    }
    function toggle(selector) {
        $(selector).fadeToggle('slow', 'swing');
    }
    function saveDisposition() {
        if ($("#disposition").val() == "" || $("#disposition").val() == "" || $('#dispositionId').val() == "") {
            $.prompt("Disposition is required");
            $("#disposition").css("border-color", "red");
            return false;
        }
        if ($("#dispositionDateTime").val() == null || $("#dispositionDateTime").val() == "") {
            $.prompt("Disposition Date Time is required");
            $("#dispositionDateTime").css("border-color", "red");
            return false;
        }
        if ($("#disposition").val() == 'RCVD' &&
                ($("#arrivallocation").val() == null || $("#arrivallocation").val() == '')) {
            $.prompt("City is required");
            $("#arrivallocation").css("border-color", "red");
            return false;
        }
        showLoading();
        $('#methodName').val('addDisposition');
        $("#lclAddUnitsForm").submit();
    }
    function checkForNumberAndDecimal(obj) {
        if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
            obj.value = "";
            sampleAlert("This field should be Numeric");
        }
    }
    function convertToKGS(lbs, kgs) {
        if ($("#" + lbs).val() !== '') {
            var value = parseFloat($("#" + lbs).val());
            $("#" + kgs).val((value / 2.2046).toFixed(3));
        } else {
            $("#" + kgs).val("");
        }
    }
    function convertToLBS(kgs, lbs) {
        if ($("#" + kgs).val() !== '') {
            var value = parseFloat($("#" + kgs).val());
            $("#" + lbs).val((value * 2.2046).toFixed(2));
        } else {
            $("#" + lbs).val("");
        }
    }
    function getPickedBookingWeightTotal(kgs, lbs) {
        if ($("#unitssId").val() !== '') {
            $.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO",
                    methodName: "getPickedDrWeightValue",
                    param1: $("#unitssId").val(),
                    param2: $("#headerId").val()
                },
                success: function (data) {
                    if (data !== null && data !== "") {
                        $("#" + kgs).val(data);
                        convertToLBS(kgs, lbs);
                    }
                }
            });
        }
    }
    function checkComments() {
        var comment = $('#comments').val();
        if (comment.match(/[^a-zA-Z0-9 ]/g)) {
            $.prompt(" Special Characters Not Allowed for Comments");
            $('#comments').val('');
        }
    }
    function unitComments() {
        var unitSize = $('#unassignUnit').val();
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO",
                methodName: "getunAssignedValue",
                param1: unitSize,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                $('#sizeId').val(data[0]);
                if (data[1] !== null)
                    $('#comments').val(data[1]);
            }
        });
    }
</script>

