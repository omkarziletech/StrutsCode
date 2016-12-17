<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<jsp:useBean id="ssdetaildao" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO"/>
<jsp:useBean id="lclAddVoyageForm" scope="request" class="com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm"/>
<cong:javascript src="${path}/jsps/LCL/js/lclUnitsSchedule.js"></cong:javascript>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<%    request.setAttribute("transModeList", ssdetaildao.getAllTransModesForDisplay());
%>
<cong:form  action="/lclAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="headerId" name="headerId"/>
    <cong:hidden id="detailId" name="detailId" />
    <input type="hidden" name="delLocId" id="delLocId"/>
    <input type="hidden" name="buttonValue" id="buttonValue"/>
    <input type="hidden" name="stopOffIndex" id="stopOffIndex"/>
    <input type="hidden" name="unLocationId" id="unLocationId"/>
    <input type="hidden" name="warehouseName" id="warehouseName"/>
    <input type="hidden" name="warehouseNo" id="warehouseNo"/>
    <input type="hidden" name="warehouseId" id="warehouseId"/>
    <input type="hidden" name="stopOffRemarks" id="stopOffRemarks"/>
    <input type="hidden" name="stopOffETD" id="stopOffETD"/>
    <input type="hidden" name="stopOffETA" id="stopOffETA"/>
    <cong:hidden id="filterByChanges" name="filterByChanges" value="${lclAddVoyageForm.filterByChanges}" />
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
        <tr class="tableHeadingNew">
            <td width="20%">
                ${(not empty lclAddVoyageForm.detailId && lclAddVoyageForm.detailId ne 0) ? "Edit " : "Add "}
                Detail
            </td>
            <c:set var="headingValues" value="${lclAddVoyageForm.filterByChanges ne 'lclDomestic' ? '' :' Terminal'}"/>
            <td width="2%" style="font-weight:bold;font-size: 12px;">Origin${headingValues}:</td>
            <td width="30%" id="polName" style="font-weight:bold;color: green;font-size: 12px;text-transform: uppercase; " >
                ${lclUnitsScheduleForm.polName}</td>
            <td width="2%" style="font-weight:bold;font-size: 12px;">Destination${headingValues}:</td>
            <td id="podName" style="font-weight:bold;color: green;font-size: 12px;text-transform: uppercase;" >
                ${lclUnitsScheduleForm.podName}</td>
        </tr>
    </table>
    <br/>
    <c:choose>
        <c:when test="${lclAddVoyageForm.filterByChanges ne 'lclDomestic'}">
            <cong:table width="100%">
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Carrier</cong:td>
                    <cong:td>
                        <cong:autocompletor  name="accountName" styleClass="mandatory textWidth"  position="right" id="accountName" fields="accountNumber"
                                             shouldMatch="true" width="600" query="EXPORT_SSLINE" template="tradingPartner" container="null" scrollHeight="300px"/>
                        <cong:text id="accountNumber" name="accountNumber" style="width:70px" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Vessel</cong:td>
                    <cong:td>
                        <cong:autocompletor name="spReferenceName" id="spReferenceName" template="two" params="14" position="right" fields="ImoNumber" shouldMatch="true"
                                            query="GENERICCODE_BY_IMONUMBER"  width="250" container="NULL" styleClass="mandatory textlabelsLclBoldForTextBox" callback="getImoNumber()" scrollHeight="300px"/>
                        <input type="text" name="ImoNumber" readOnly="true" id="ImoNumber" style="width:60px" tabindex="-1"
                               class="textlabelsBoldForTextBoxDisabledLook readonly" style="width:30%" value="${ImoNumber.field3}"/>
                    </cong:td>

                    <cong:td styleClass="textlabelsBoldforlcl">SS Voyage#</cong:td>
                    <cong:td> <cong:text name="spReferenceNo" id="spReferenceNo" styleClass="mandatory textuppercaseLetter"/> </cong:td>
                </cong:tr>
                <cong:tr><cong:td colspan="8"></cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">ETD (Sailing Date)</cong:td>
                    <cong:td>
                        <cong:calendarNew styleClass="mandatory textWidth" id="std" name="std" value="" onchange="setETAPOD();validateETA();"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">ETA at POD</cong:td>
                    <cong:td>
                        <c:choose>
                            <c:when test="${lclAddVoyageForm.filterByChanges eq 'lclExport'}">
                                <cong:text id="etaPod" name="etaPod" styleClass="textlabelsBoldForTextBoxDisabledLook textWidth" readOnly="true" />
                            </c:when>
                            <c:otherwise>
                                <cong:calendarNew styleClass="mandatory textWidth" id="etaPod" name="etaPod" value="" onchange="validateETD()"/>
                            </c:otherwise>
                        </c:choose>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Transit Mode</cong:td>
                    <cong:td>
                        <html:select property="transMode" styleClass="smallDropDown" >
                            <html:optionsCollection name="transModeList"/>
                        </html:select>
                    </cong:td>
                </cong:tr>
                <cong:tr><cong:td colspan="8"></cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Departure Pier</cong:td>
                    <cong:td>
                        <cong:autocompletor id="departurePier" name="departurePier" template="one" fields="NULL,NULL,NULL,departureId" position="right"
                                            query="EXPORT_CONCAT_PORTS1"  styleClass="mandatory textWidth"  width="350" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden id="departureId" name="departureId"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Arrival Pier/POD</cong:td>
                    <cong:td>
                        <cong:autocompletor id="arrivalPier" name="arrivalPier" template="one" fields="NULL,NULL,NULL,arrivalId" position="left"
                                            query="CONCAT_PORT_NAME"  styleClass="mandatory textlabelsBoldForTextBox textWidth"  width="350" container="NULL"  shouldMatch="true" scrollHeight="200px"/>
                        <cong:hidden id="arrivalId" name="arrivalId"/>
                        <cong:checkbox  name="printViaMasterBl" id="printViaMasterBl"
                                        value="${lclAddVoyageForm.printViaMasterBl}" label="Via" container="null" title="Checked=Print via city on Master BL"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">LRD Override Days</cong:td>
                    <cong:td>
                        <cong:text name="lrdOverrideDays" id="lrdOverrideDays" onkeyup="checkForNumberAndDecimal(this);"/>
                    </cong:td>
                </cong:tr>
                <cong:tr><cong:td colspan="8"></cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Loading Deadline Date</cong:td>
                    <cong:td>
                        <cong:calendarNew id="generalLoadingDeadlineId" name="generalLoadingDeadline" value="" styleClass="textWidth"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">HazLoad Deadline Date</cong:td>
                    <cong:td>
                        <cong:calendarNew id="hazmatLoadingDeadlineId" name="hazmatLoadingDeadline" value="" styleClass="textWidth"/>
                    </cong:td>
                    <cong:hidden id="loadLrdt" name="loadLrdt"/>
                    <cong:hidden id="hazmatLrdt" name="hazmatLrdt"/>
                    <cong:td styleClass="textlabelsBoldforlcl">SSL Docs Cutoff Date</cong:td>
                    <cong:td><cong:calendarNew  id="sslDocsCutoffDate" name="sslDocsCutoffDate"/></cong:td>
                </cong:tr>
                <cong:tr><cong:td colspan="8"></cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Land Carrier</cong:td>
                    <cong:td>
                        <cong:autocompletor  name="landCarrier" styleClass="textWidth"  position="right" id="landCarrier" fields="landCarrierAcountNumber"
                                             shouldMatch="true" width="500"  query="EXPORT_LAND_CARRIER" template="tradingPartner" container="null" scrollHeight="150px"/>
                        <cong:text id="landCarrierAcountNumber" name="landCarrierAcountNumber" style="width:70px" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Land Exit City</cong:td>
                    <cong:td>
                        <cong:autocompletor  name="landExitCity" styleClass="textWidth"  position="left" id="landExitCity" fields="null,null,null,landExitCityUnlocCode"
                                             shouldMatch="true" width="200" query="COUNTRY_OF_US_CA" template="one" container="null" scrollHeight="150px"/>
                        <cong:hidden id="landExitCityUnlocCode" name="landExitCityUnlocCode"/>
                    </cong:td>

                    <cong:td styleClass="textlabelsBoldforlcl">Transit Days Override</cong:td>
                    <cong:td>
                        <cong:text name="ttOverrideDays" id="ttOverrideDays" onkeyup="checkForNumberAndDecimal(this);"
                                   onchange="if(${lclAddVoyageForm.filterByChanges eq 'lclExport'}){setEtaPodDate(this);}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:tr><cong:td colspan="6"></cong:td></cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Agent</cong:td>
                    <cong:td>
                        <c:choose>
                            <c:when test="${lclAddVoyageForm.filterByChanges eq 'lclExport'}">
                                <cong:autocompletor  name="exportAgentAcctName" styleClass="mandatory textWidth" position="right" id="exportAgentAcctName" fields="exportAgentAcctNo,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,agentBrandName"
                                                     callback="setVoyageLevelBrand();"    shouldMatch="true" width="500" params="${lclAddVoyageForm.fdUnlocationCode}" query="TRADING_AGENT" template="tradingPartner" container="null" scrollHeight="300px"/>
                            </c:when>
                            <c:otherwise>
                                <cong:autocompletor  name="exportAgentAcctName" styleClass="mandatory textlabelsLclBoldForTextBox" position="right" id="exportAgentAcctName" fields="exportAgentAcctNo,NULL,NULL,NULL,NULL,NULL,NULL,disabledAccount,forwardAccount,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,agentBrandName"
                                                     callback="cfclAccttypeCheck();setVoyageLevelBrand();"  shouldMatch="true" width="500" params="${lclAddVoyageForm.fdUnlocationCode}" query="TRADING_AGENT" template="tradingPartner" container="null" scrollHeight="300px"/>
                            </c:otherwise>
                        </c:choose>
                        <cong:text id="exportAgentAcctNo" readOnly="true"  name="exportAgentAcctNo"
                                   styleClass="textlabelsBoldForTextBoxDisabledLook readonly" style="width:70px"/>
                        <input type="hidden" name="disabledAccount" id="disabledAccount"/>
                        <input type="hidden" name="forwardAccount" id="forwardAccount"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Trucking Remarks</cong:td>
                    <cong:td>
                        <html:textarea cols="2" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox textAreaLimit30"
                                       onkeyup="limitText();" property="remarks" styleId="remarks"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Land Exit Date</cong:td>
                    <cong:td><cong:calendarNew  id="landExitDate" name="landExitDate"/></cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:tr><cong:td colspan="6"></cong:td></cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Brand</cong:td>
                    <cong:td>
                        <cong:hidden id="agentBrandName" name="agentBrandName"/>                        
                        <input type="hidden" id="companyCode" value="${companyCodeValue}"/>
                        <c:choose>
                            <c:when test="${companyCodeValue == '03'}">
                                <input type="radio" name="lclVoyageLevelBrand" value="Econo">Econo &nbsp;
                            </c:when>
                            <c:otherwise>
                                <input type="radio" name="lclVoyageLevelBrand" value="OTI">OTI &nbsp;
                            </c:otherwise>
                        </c:choose>                                   
                        <input type="radio" name="lclVoyageLevelBrand" value="ECU WW">ECU WW
                    </cong:td>                    
                </cong:tr>
            </cong:table>
        </c:when>
        <c:otherwise>
            <cong:table width="100%"  id="domesticVoyageDetail">
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Carrier</cong:td>
                    <cong:td>
                        <cong:autocompletor  name="accountName" styleClass="textWidth"  position="right" id="accountName" fields="accountNumber"
                                             shouldMatch="true" width="600" query="EXPORT_SSLINE" template="tradingPartner" container="null" scrollHeight="300px"/>
                        <cong:hidden id="accountNumber" name="accountNumber"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">ETD</cong:td>
                    <cong:td>
                        <cong:calendarNew styleClass="mandatory textWidth" id="std" name="std" value="" onchange="validateETA();"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">ETA</cong:td>
                    <cong:td>
                        <cong:calendarNew styleClass="mandatory textWidth" id="etaPod" name="etaPod" value="" onchange="validateETD()"/>
                    </cong:td>
                </cong:tr>
                <cong:tr><cong:td colspan="3"><br/></cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Transit Mode</cong:td>
                    <cong:td>
                        <html:select property="transMode" styleClass="smallDropDown" >
                            <html:optionsCollection name="transModeList"/>
                        </html:select>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Trucking Remarks</cong:td>
                    <cong:td>
                        <html:textarea cols="2" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox textAreaLimit30"
                                       onkeyup="limitText();" property="remarks" styleId="remarks"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </c:otherwise>
    </c:choose>
    <br/>
    <cong:table width="100%" border="0" align="center">
        <cong:tr>
            <cong:td width="45%"></cong:td>
            <cong:td colspan="0" align="center">
                <input type="button" value="Save Voyage Detail" align="center" class="button-style1" onclick="saveVoyageDetail();"/>
                <c:if test="${(lclAddVoyageForm.filterByChanges=='lclDomestic') && (empty lclAddVoyageForm.detailId || lclAddVoyageForm.detailId==0)}">
                    <div class="button-style1" id="stopoffs" onclick="addStopOff('${path}');">Stop-offs</div>
                    <div class="button-style1" id="stopoffs" onclick="openUnit('${path}');">Add Unit</div>
                </c:if>
            </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="8"></cong:td></cong:tr>
    </cong:table>
    <c:if test="${not empty stopAddList}">
        <table class="dataTable" border="0"  id="stopAddList" style="width:30%;">
            <thead>
                <tr>
                    <th>Stop-Off City</th>
                    <th>Warehouse</th>
                    <th>ETD</th>
                    <th>ETA</th>
                    <th>Remarks</th>
                    <th>Action</th>
                </tr>
            </thead>

            <c:forEach items="${stopAddList}" var='stopAdd' varStatus="stopOff">
                <c:choose>
                    <c:when test="${zebra=='odd'}">
                        <c:set var="zebra" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="zebra" value="odd"/>
                    </c:otherwise>
                </c:choose>
                <c:if test="${stopAdd.addOrRemove eq true}">
                    <tr class="${zebra}">
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="departureDate" value="${stopAdd.stdDate}"></fmt:formatDate>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="arrivalDate" value="${stopAdd.staDate}"></fmt:formatDate>
                        <td style="display:none; width:20%"><p class="unLocId">${stopAdd.unlocationId}</p></td>
                        <td>${stopAdd.countryValue}</td>
                        <td>${stopAdd.wareHouse}</td>
                        <td>${departureDate}</td>
                        <td>${arrivalDate}</td>
                        <td>${stopAdd.remarks}</td>
                        <td>
                            &nbsp;&nbsp;
                            <img src="${path}/jsps/LCL/images/close1.png"
                                 alt="delete" height="16" onclick="deleteStopOff('${stopAdd.unlocationId}', '${stopAdd.detailId}');"/>

                            &nbsp;&nbsp;&nbsp;<img src="${path}/jsps/LCL/images/add2.gif"
                                                   alt="add" height="16" onclick="addinternalstopOff('${path}', '${stopOff.index}');"/>
                        </td>
                        <td style="display:none;"><p class="detailId">${stopAdd.detailId}</p></td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </c:if>
    <%-- adding for lcl Voyage Notification in Exports--%>
</cong:form>
<script type="text/javascript">
    var vessel, pier, sailingDate, ss_line, ss_vog, lrd, arrivalDate;
    jQuery(document).ready(function () {
        vessel = $("#spReferenceName").val();
        pier = $("#departurePier").val();
        sailingDate = $("#std").val();
        arrivalDate = $("#etaPod").val();
        ss_line = $("#accountName").val();
        ss_vog = $("#spReferenceNo").val();
        lrd = $("#lrdOverrideDays").val();
        $("#polName").text(parent.$("#originalOriginName").val());
        $("#podName").text(parent.$("#originalDestinationName").val());
        var detailId = $("#detailId").val();
        var popUpFlag = parent.$("#openPopup").val();
        var filterBy = parent.$("#filterByChanges").val();
        if (detailId == null || detailId == "" || detailId == "0") {
            if (filterBy == "lclExport" || filterBy == "lclCfcl") {
                document.lclAddVoyageForm.transMode.value = "V";
            } else {
                document.lclAddVoyageForm.transMode.value = "T";
            }
            if (filterBy != 'lclDomestic') {
                $("#arrivalId").val(parent.$("#finalDestinationId").val());
                $("#arrivalPier").val(parent.$("#podDestination").val());
            }
            if (filterBy === 'lclCfcl') {
                $("#exportAgentAcctName").val(parent.$("#cfclAcctName").val());
                $("#exportAgentAcctNo").val(parent.$("#cfclAcctNo").val());
            }
        }
        if (filterBy == "lclDomestic") {
            $('#departurePier').removeClass("mandatory");
            $('#arrivalPier').removeClass("mandatory");
            $('#spReferenceName').removeClass("mandatory");
            $('#spReferenceNo').removeClass("mandatory");
        } else if (filterBy == "lclCfcl") {
            $("#exportAgentAcctName").attr("alt", "CFCL_ACCOUNT");
        }

        if (popUpFlag != null && popUpFlag == "openPopup") {
            $("#ttOverrideDays").val(parent.$("#ttOverrideDays").val());
            $("#lrdOverrideDays").val(parent.$("#lrdOverrideDays").val());
            $("#etaPod").val(parent.$("#etaPod").val());
            $("#arrivalId").val(parent.$("#arrivalId").val());
            $("#arrivalPier").val(parent.$("#arrivalPier").val());
            $("#departureId").val(parent.$("#departureId").val());
            $("#departurePier").val(parent.$("#departurePier").val());
            $("#accountNumber").val(parent.$("#accountNumber").val());
            $("#accountName").val(parent.$("#accountName").val());
            document.lclAddVoyageForm.transMode.value = parent.$("#transMode").val();
        }

        $("#spReferenceName").keyup(function () {
            if ($(this).val() === '') {
                $("#ImoNumber").val('');
            }
        });
        $("#landCarrier").keyup(function () {
            if ($(this).val() === '') {
                $("#landCarrierAcountNumber").val('');
            }
        });
        $("#landExitCity").keyup(function () {
            if ($(this).val() === '') {
                $("#landExitCityUnlocCode").val('');
            }
        });
        $("#exportAgentAcctName").keyup(function () {
            if ($(this).val() === '') {
                $("#exportAgentAcctNo").val('');
            }
        });
        $("#accountName").keyup(function () {
            if ($(this).val() === '') {
                $("#accountNumber").val('');
            }
        });
       setVoyageLevelBrand();
    });


    function showWaring(msg, mandatoryId) {
        $.prompt(msg);
        $("#" + mandatoryId).css("border-color", "red");
        $("#warning").parent.show();
    }
    function saveVoyageDetail() {
        var chkTransMode = "V";
        if (parent.$("#filterByChanges").val() === "lclExport" || parent.$("#filterByChanges").val() === "lclCfcl") {
            setETAPOD();
        }
        if ($("#std").val() === null || $("#std").val() === "") {
            showWaring("ETD Sailing Date is required", "std");
        }
        if (parent.$("#filterByChanges").val() === "lclExport" || parent.$("#filterByChanges").val() === "lclCfcl") {
            if ($("#accountNumber").val() === null || $("#accountNumber").val() === "") {
                showWaring("Carrier is required", "accountName");
            }
            if ($("#spReferenceName").val() === null || $("#spReferenceName").val() === "") {
                showWaring("Vessel is required", "spReferenceName");
            }
            if ($("#spReferenceNo").val() === null || $("#spReferenceNo").val() === "") {
                showWaring("SS Voyage# is required", "spReferenceNo");
            }
            if (parent.$("#filterByChanges").val() === 'lclCfcl') {
                if ($("#exportAgentAcctName").val() === null || $("#exportAgentAcctName").val() === "") {
                    showWaring("Agent is required", "exportAgentAcctName");
                }
            }
            if ($("#departureId").val() === null || $("#departureId").val() === "" || $("#departureId").val() === 0) {
                showWaring("Departure Pier is required", "departurePier");
            }
            if ($("#arrivalId").val() === null || $("#arrivalId").val() === "" || $("#arrivalId").val() === 0) {
                showWaring("Arrival Pier is required", "arrivalPier");
            }
            if ($("#exportAgentAcctName").val() === null || $("#exportAgentAcctName").val() === "") {
                showWaring("Agent is required", "exportAgentAcctName");
            }
        }
        else
        {
            if ($("#etaPod").val() === null || $("#etaPod").val() === "") {
                showWaring("ETA is required", "etaPod");
            }
            chkTransMode = "T";
        }
        var transMode = document.lclAddVoyageForm.transMode.value;
        var detailId = document.getElementById("detailId").value;
        var headerId = parent.document.getElementById("headerId").value;
        if (headerId != null && headerId != "" && headerId != "0" && transMode.toUpperCase() == chkTransMode)
        {
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.lcl.dwr.LclDwr",
                    methodName: "checkTransMode",
                    param1: headerId,
                    param2: detailId,
                    param3: chkTransMode,
                    dataType: "json"
                },
                async: false,
                success: function (data) {
                    if (data == true) {
                        sampleAlert("Cannot Add more than one detail for Trans Mode " + chkTransMode);
                        return false;
                    }
                    else
                    {
                        var changedFields = changedFieldsValue();
                        if ($("#filterByChanges").val() === "lclExport" && changedFields !== "") {
                            sendVoyageNotification(changedFields);
                        } else {
                            saveDetailValues(detailId, transMode);
                        }
                    }
                }
            });
        }
        else {
            saveDetailValues(detailId, transMode);
        }

    }
    function cfclAccttypeCheck() {
        if ($('#disabledAccount').val() === 'Y') {
            $.prompt("This Customer is disabled and merged with <span style=color:red>" + $('#forwardAccount').val() + "</span>");
            $('#exportAgentAcctName').val('');
            $('#exportAgentAcctNo').val('');
        }
    }
    function changedFieldsValue() {
        var description = "";
        if (vessel !== $("#spReferenceName").val()) {
            description = description + "@Vessel ->" + vessel + " To " + $('#spReferenceName').val();
        }
        if (pier !== $("#departurePier").val()) {
            description = description + "@Pier ->" + pier + " To " + $('#departurePier').val();
        }
        if (sailingDate !== $("#std").val()) {
            description = description + "@Sailing_Date ->" + sailingDate + " To " + $('#std').val();
        }
        if (arrivalDate !== $("#etaPod").val()) {
            description = description + "@ETA POD Date ->" + arrivalDate + " To " + $('#etaPod').val();
        }
        if (ss_vog !== $("#spReferenceNo").val()) {
            description = description + "@SS_Vogage ->" + ss_vog + " To " + $('#spReferenceNo').val();
        }
        if (ss_line !== $("#accountName").val()) {
            description = description + "@Line_Name ->" + ss_line + " To " + $('#accountName').val();
        }
        if (lrd !== $("#lrdOverrideDays").val()) {
            description = description + "@PortLrd->" + lrd + " To " + $('#lrdOverrideDays').val();
        }
        return description;
    }
    function sendVoyageNotification(changedFields) {
        var transMode = document.lclAddVoyageForm.transMode.value;
        var detailId = document.getElementById("detailId").value;
        var href = '${path}' + "/lclAddVoyage.do?methodName=sendVoyageNotification&changedFields=" + changedFields
                + "&spReferenceName=" + vessel + "&departurePier=" + pier + "&lrdOverrideDays=" + lrd + "&std=" + sailingDate + "&etaPod=" + arrivalDate
                + "&accountName=" + ss_line + "&spReferenceNo=" + ss_vog;
        $.colorbox({
            iframe: true,
            href: href,
            width: "70%",
            height: "90%",
            title: "Lcl Export Voyage Notification",
            onClosed: function () {
                saveDetailValues(detailId, transMode);
            }
        });
    }
    function saveDetailValues(detailId, transMode) {
        showLoading();
        if ($("filterByChanges").val() !== 'lclDomestic') {
            setVoyageDetailForExport(detailId, transMode);
        } else {
            setVoyageDetailForInland(detailId, transMode);
        }
        parent.$("#filterByChanges").val($("#filterByChanges").val());
        parent.$("#methodName").val('saveVoyageDetail');
        parent.$("#lclAddVoyageForm").submit();
    }

    function setVoyageDetailForExport(detailId, transMode) {
        parent.$("#accountNumber").val($("#accountNumber").val());
        parent.$("#spReferenceName").val($("#spReferenceName").val());
        parent.$("#spReferenceNo").val($("#spReferenceNo").val());
        parent.$("#std").val($("#std").val());
        parent.$("#etaPod").val($("#etaPod").val());
        parent.$("#departureId").val($("#departureId").val());
        parent.$("#arrivalId").val($("#arrivalId").val());
        parent.$("#ttOverrideDays").val($("#ttOverrideDays").val());
        parent.$("#lrdOverrideDays").val($("#lrdOverrideDays").val());
        parent.$("#loadLrdt").val($("#loadLrdt").val());
        parent.$("#hazmatLrdt").val($("#hazmatLrdt").val());
        parent.$("#transMode").val(transMode);
        parent.$("#detailId").val(detailId);
        parent.$("#arrivalPier").val($("#arrivalPier").val());
        parent.$("#departurePier").val($("#departurePier").val());
        parent.$("#remarks").val(document.lclAddVoyageForm.remarks.value);
        parent.$("#landCarrierAcountNumber").val($("#landCarrierAcountNumber").val());
        parent.$("#landExitCityUnlocCode").val($("#landExitCityUnlocCode").val());
        parent.$("#exportAgentAcctNo").val($("#exportAgentAcctNo").val());
        parent.$("#agentBrandName").val($('input:radio[name=lclVoyageLevelBrand]:checked').val());         
        parent.$("#landExitDate").val($("#landExitDate").val());
        parent.$("#sslDocsCutoffDate").val($("#sslDocsCutoffDate").val());
        parent.$("#hazmatLoadingDeadline").val($("#hazmatLoadingDeadlineId").val());
        parent.$("#generalLoadingDeadline").val($("#generalLoadingDeadlineId").val());
        parent.$("#printViaMasterBl").val($("#printViaMasterBl").is(":checked"));
    }

    function setVoyageDetailForInland(detailId, transMode) {
        parent.$("#accountNumber").val($("#accountNumber").val());
        parent.$("#std").val($("#std").val());
        parent.$("#etaPod").val($("#etaPod").val());
        parent.$("#transMode").val(transMode);
        parent.$("#detailId").val(detailId);
        parent.$("#remarks").val(document.lclAddVoyageForm.remarks.value);
    }

    function setETAPOD() {
        if (parent.$("#filterByChanges").val() === "lclExport" || parent.$("#filterByChanges").val() === "lclCfcl") {
            var polpodTT = parent.document.getElementById("polPodTT").value;
            var co_dbd = parent.document.getElementById("co_dbd").value;
            var co_tod = parent.document.getElementById("co_tod").value;
            var etdSailingDateStr = document.getElementById("std").value;
            if (etdSailingDateStr !== "")
            {
                if ($("#ttOverrideDays").val() !== "") {
                    var ttOverrideDays = new Object();
                    ttOverrideDays.value = $("#ttOverrideDays").val();
                    setEtaPodDate(ttOverrideDays);
                } else {
                    if (polpodTT !== "" && polpodTT !== "0")
                    {
                        var day = etdSailingDateStr.substring(0, 2);
                        var month = getMonthNumber(etdSailingDateStr.substring(3, 6));
                        var year = etdSailingDateStr.substring(7, etdSailingDateStr.length);
                        var etdSailingDate = new Date(month + "/" + day + "/" + year);
                        etdSailingDate.setDate(etdSailingDate.getDate() + parseInt(polpodTT));
                        day = etdSailingDate.getDate();
                        if (day <= 9)
                        {
                            day = "0" + day;
                        }
                        month = parseInt(etdSailingDate.getMonth()) + 1;
                        document.getElementById("etaPod").value = day + "-" + getMonthName(month) + "-" + etdSailingDate.getFullYear();
                    }
                }
                var lrdOverrideDays = document.getElementById("lrdOverrideDays").value;
                if (lrdOverrideDays != "" && lrdOverrideDays != "0")
                {
                    co_dbd = lrdOverrideDays;
                }
                if (co_dbd != "" && co_dbd != "0")
                {
                    var day = etdSailingDateStr.substring(0, 2);
                    var month = getMonthNumber(etdSailingDateStr.substring(3, 6));
                    var year = etdSailingDateStr.substring(7, etdSailingDateStr.length);
                    var hazmatDate = new Date(month + "/" + day + "/" + year);
                    var etdSailingDate = new Date(month + "/" + day + "/" + year);
                    etdSailingDate.setDate(etdSailingDate.getDate() - parseInt(co_dbd));
                    if (etdSailingDate.getDay() == 0)
                    {
                        etdSailingDate.setDate(etdSailingDate.getDate() - 2);
                    }
                    else if (etdSailingDate.getDay() == 6)
                    {
                        etdSailingDate.setDate(etdSailingDate.getDate() - 1);
                    }
                    day = etdSailingDate.getDate();
                    if (day <= 9)
                    {
                        day = "0" + day;
                    }
                    month = parseInt(etdSailingDate.getMonth()) + 1;
                    $("#loadLrdt").val(day + "-" + getMonthName(month) + "-" + etdSailingDate.getFullYear());
                    //Hazmat Date
                    hazmatDate.setDate(hazmatDate.getDate() + parseInt(1));
                    day = hazmatDate.getDate();
                    if (day <= 9) {
                        day = "0" + day;
                    }
                    month = parseInt(hazmatDate.getMonth()) + 1;
                    $("#hazmatLrdt").val(day + "-" + getMonthName(month) + "-" + hazmatDate.getFullYear());
                } else {
                    var day = etdSailingDateStr.substring(0, 2);
                    var month = getMonthNumber(etdSailingDateStr.substring(3, 6));
                    var year = etdSailingDateStr.substring(7, etdSailingDateStr.length);
                    $("#loadLrdt").val(day + "-" + getMonthName(month) + "-" + year);
                }

            }
        }
    }
    function getImoNumber() {
        if ($("#ImoNumber").val() === '') {
            $.prompt("Select Vessel Which Contain IMO Number", {
                buttons: {
                    Ok: 1
                },
                submit: function (v) {
                    if (v == 1) {
                        $.prompt.close();
                        $("#spReferenceName").val('');
                    }
                }
            });
        }
    }
    function limitText() {
        var text = document.getElementById("remarks");
        text.onkeypress = function () {
            var lines = text.value.split("\n");
            for (var i = 0; i < lines.length; i++) {
                if (lines[i].length <= 30)
                    continue;
                var j = 0;
                space = 30;
                while (j++ <= 30) {
                    if (lines[i].charAt(j) === " ")
                        space = j;
                }
                lines[i + 1] = lines[i].substring(space + 1) + (lines[i + 1] || "");
                lines[i] = lines[i].substring(0, space);
            }
            text.value = lines.slice(0, 5).join("\n");
        };
    }

    function addStopOff(path) {
        var href = path + "/stopOff.do?methodName=displayStopOff";
        $.colorbox({
            iframe: true,
            href: href,
            width: "90%",
            height: "90%",
            title: "Add Stop_Off"
        });
    }

    function deleteStopOff(id, detailId) {
        $('#delLocId').val(id);
        $('#detailId').val(detailId);
        $('#buttonValue').val('removeStopOff');
        $("#methodName").val('addStopOff');
        $("#lclAddVoyageForm").submit();
    }

    function addinternalstopOff(path, index) {
        var href = path + "/stopOff.do?methodName=displayStopOff&index=" + index;
        $.colorbox({
            iframe: true,
            href: href,
            width: "90%",
            height: "90%",
            title: "Add Stop_Off"
        });
    }

    function openUnit(path) {
        var unitNo = parent.$("#unitNo").val();
        var unitType = parent.$("#unitType").val();
        var haz = parent.$("#hazmatPermittedUnit").val();
        var ref = parent.$("#refrigerationPermittedUnit").val();
        var stopOff = parent.$("#stopoff").val();
        var chasisNo = parent.$("#chasisNo").val();
        var remarks = parent.$("#unitTruckRemarks").val();
        var href = path + "/lclAddVoyage.do?methodName=addUnitForDomesticVoyage&unitNo=" + unitNo + "&unitType=" + unitType +
                "&hazmat=" + haz + "&refrigeration=" + ref + "&stopoff=" + stopOff + "&chasisNo=" + chasisNo + "&unitTruckRemarks=" + remarks;
        $.colorbox({
            iframe: true,
            href: href,
            width: "100%",
            height: "90%",
            title: "Add Unit"
        });
    }
    function setEtaPodDate(ele) {
        if ('' != ele.value) {
            var etdSailingDateStr = $("#std").val().split("-");
            var day = etdSailingDateStr[0];
            var month = getMonthNumber(etdSailingDateStr[1]);
            var year = etdSailingDateStr[2];
            var etdSailingDate = new Date(month + "/" + day + "/" + year);
            etdSailingDate.setDate(etdSailingDate.getDate() + parseInt(ele.value));
            day = etdSailingDate.getDate();
            if (day <= 9) {
                day = "0" + day;
            }
            month = parseInt(etdSailingDate.getMonth()) + 1;
            document.getElementById("etaPod").value = day + "-" + getMonthName(month) + "-" + etdSailingDate.getFullYear();
        } else {
            setETAPOD();
        }
    }

    function setVoyageLevelBrand() {
        var brand = $("#agentBrandName").val();
        if (brand === "Econo" || brand === "OTI") {
            document.getElementsByName("lclVoyageLevelBrand")[0].checked = true;
            //                  document.getElementById("brandEcono").checked = true; same code
        } else if (brand === "ECU WW") {
            document.getElementsByName("lclVoyageLevelBrand")[1].checked = true;
        } else {
            document.getElementsByName("lclVoyageLevelBrand")[0].checked = false;
            document.getElementsByName("lclVoyageLevelBrand")[1].checked = false;
        }
    }

</script>
