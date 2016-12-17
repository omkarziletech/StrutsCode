<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/tlds/dao.tld" prefix="dao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="../fragment/lclFormSerialize.jspf"  %>
<%@include file="/jsps/includes/resources.jsp" %>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<cong:javascript src="${path}/jsps/LCL/js/lclImportsUnitsSchedule.js"></cong:javascript>
<link type="text/css" rel="stylesheet" href="${path}/css/default/style.css">
<%@include file="/taglib.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lcl Import Voyage Details</title>
    </head>
    <body id="pane">
        <cong:form action="/lclImportAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm">
            <cong:hidden id="methodName" name="methodName"/>
            <input type="hidden" name="loginUserId" id="loginUserId" value="${loginuser.userId}"/>
            <input type="hidden" name="reportPreviewLocation" id="reportPreviewLocation" value="${reportPreviewLocation}"/>
            <cong:hidden id="headerId" name="headerId" value="${lclssheader.id}" />
            <cong:hidden id="schedule" name="schedule" value="${lclssheader.scheduleNo}" />
            <cong:hidden id="detailId" name="detailId" />
            <cong:hidden id="masterId" name="masterId" />
            <cong:hidden id="accountName" name="accountName"  />
            <cong:hidden id="accountNumber" name="accountNumber"  />
            <cong:hidden id="spReferenceName" name="spReferenceName"  />
            <cong:hidden id="spReferenceNo" name="spReferenceNo"  />
            <cong:hidden id="std" name="std"  />
            <cong:hidden id="etaPod" name="etaPod"  />
            <cong:hidden id="departureId" name="departureId"  />
            <cong:hidden id="arrivalId" name="arrivalId"  />
            <cong:hidden id="arrivalPier" name="arrivalPier"  />
            <cong:hidden id="departurePier" name="departurePier"  />
            <cong:hidden id="ttOverrideDays" name="ttOverrideDays"  />
            <cong:hidden id="lrdOverrideDays" name="lrdOverrideDays"  />
            <cong:hidden id="bookingNumber" name="bookingNumber"  />
            <cong:hidden id="transMode" name="transMode"  />
            <cong:hidden id="unitId" name="unitId" value="${lclunitSS.lclUnit.id}" />
            <cong:hidden id="unitssId" name="unitssId" value="${lclUnitSS.id}" />
            <cong:hidden id="unitNo" name="unitNo"  />
            <cong:hidden id="SUHeadingNote" name="SUHeadingNote"  />
            <cong:hidden id="unitType" name="unitType"  />
            <cong:hidden id="hazmatPermittedUnit" name="hazmatPermittedUnit"  />
            <cong:hidden id="hazmatPermitted" name="hazmatPermitted"  />
            <cong:hidden id="drayageProvided" name="drayageProvided"  />
            <cong:hidden id="intermodalProvided" name="intermodalProvided"  />
            <cong:hidden id="stopoff" name="stopoff"  />
            <cong:hidden id="chasisNo" name="chasisNo"  />
            <cong:hidden id="remarks" name="remarks"  />
            <cong:hidden id="billTerminalNo" name="billTerminalNo"  />
            <cong:hidden id="billTerminal" name="billTerminal"  />
            <cong:hidden  id="voyageTerminal" name="voyageTerminal"  value="${lclssheader.billingTerminal.trmnum}"/>
            <cong:hidden id="path" name="path" value="${path}" />
            <cong:hidden id="openPopup" name="openPopup" value="${openPopup}" />
            <cong:hidden id="refrigerationPermittedUnit" name="refrigerationPermittedUnit"  />
            <cong:hidden id="hiddenRefrigerationPermitted" name="hiddenRefrigerationPermitted"/>
            <cong:hidden id="loadLrdt" name="loadLrdt"  />
            <cong:hidden id="approxDueDate" name="approxDueDate"/>
            <cong:hidden id="masterBL" name="masterBL"/>
            <cong:hidden id="hazmatLrdt" name="hazmatLrdt"/>
            <cong:hidden id="closedRemarks" name="closedRemarks" value="${lclssheader.closedRemarks}"/>
            <cong:hidden id="reopenedRemarks" name="reopenedRemarks" value="${lclssheader.reopenedRemarks}"/>
            <cong:hidden id="auditedRemarks" name="auditedRemarks" value="${lclssheader.auditedRemarks}"/>
            <cong:hidden id="originId" name="originId" value="${originId}" />
            <cong:hidden id="finalDestinationId" name="finalDestinationId" value="${destinationId}" />
            <cong:hidden id="pooOrigin" name="pooOrigin" value="${originValue}" />
            <cong:hidden id="podDestination" name="podDestination" value="${destinationValue}"/>
            <cong:hidden id="originalOriginId" name="originalOriginId" value="${originalOriginId}" />
            <cong:hidden id="originalDestinationId" name="originalDestinationId" value="${originalDestinationId}" />
            <cong:hidden id="originalOriginName" name="originalOriginName" value="${originalOriginName}" />
            <cong:hidden id="originalDestinationName" name="originalDestinationName" value="${originalDestinationName}"/>
            <cong:hidden id="filterByChanges" name="filterByChanges" value="${lclAddVoyageForm.filterByChanges}" />
            <cong:hidden id="polPodTT" name="polPodTT" value="${polPodTT}" />
            <cong:hidden id="co_dbd" name="co_dbd" value="${co_dbd}" />
            <cong:hidden id="co_tod" name="co_tod" value="${co_tod}" />
            <cong:hidden id="unitsReopened" name="unitsReopened" value="${unitsReopened}"/>
            <cong:hidden id="contractNumber" name="contractNumber"  />
            <cong:hidden id="prepaidCollect" name="prepaidCollect"  />
            <cong:hidden id="shipperAccountNumber" name="shipperAccountNumber"  />
            <cong:hidden id="shipperAccountNo" name="shipperAccountNo"  />
            <cong:hidden id="shipperEdi" name="shipperEdi"  />
            <cong:hidden id="consigneeAccountNumber" name="consigneeAccountNumber"  />
            <cong:hidden id="consigneeAccountNo" name="consigneeAccountNo"  />
            <cong:hidden id="consigneeEdi" name="consigneeEdi"  />
            <cong:hidden id="notifyAccountNumber" name="notifyAccountNumber"  />
            <cong:hidden id="notifyAccountNo" name="notifyAccountNo"  />
            <cong:hidden id="manualNotyName" name="manualNotyName"  />
            <cong:hidden id="notifyEdi" name="notifyEdi"  />
            <cong:hidden id="exportReferenceEdi" name="exportReferenceEdi"  />
            <cong:hidden id="blbody" name="blbody"  />
            <cong:hidden id="moveType" name="moveType"  />
            <cong:hidden id="itDatetime" name="itDatetime"  />
            <cong:hidden id="originAcctNo" name="originAcctNo"  />
            <cong:hidden id="originAcct" name="originAcct"  />
            <cong:hidden id="unitsWarehouseId" name="unitsWarehouseId"  />
            <cong:hidden id="unitWarehouse" name="unitWarehouse"  />
            <cong:hidden id="goDate" name="goDate"  />
            <cong:hidden id="lastFreeDate" name="lastFreeDate"  />
            <cong:hidden id="coloaderAcctNo" name="coloaderAcctNo"  />
            <cong:hidden id="coloaderAcct" name="coloaderAcct"  />
            <cong:hidden id="cfsWarehouseId" name="cfsWarehouseId"  />
            <cong:hidden id="cfsWarehouse" name="cfsWarehouse"  />
            <cong:hidden id="itNo" name="itNo"  />
            <cong:hidden id="itPortId" name="itPortId"  />
            <cong:hidden id="sealNoIn" name="sealNoIn"  />
            <cong:hidden id="sealNoOut" name="sealNoOut"  />
            <cong:hidden id="strippedDate" name="strippedDate"  />
            <cong:hidden id="unitAutoCostFlag" name="unitAutoCostFlag"  />
            <cong:hidden id="voyageNo" name="voyageNo" value="${voyageNo}" />
            <cong:hidden id="loginId" name="loginId" value="${loginId}" />
            <cong:hidden id="loginName" name="loginName" value="${loginName}" />
            <cong:hidden id="billsTerminal" name="billsTerminal" value="${billsTerminal}" />
            <cong:hidden id="billsTerminalNo" name="billsTerminalNo" value="${billsTerminalNo}" />
            <cong:hidden id="polUnlocationCode" name="polUnlocationCode" value="${polUnlocationCode}" />
            <cong:hidden id="stuffedByUserId" name="stuffedByUserId"/>
            <cong:hidden id="destuffedByUserId" name="destuffedByUserId"/>
            <cong:hidden id="postFlag" name="postFlag"/>
            <cong:hidden id="buttonValue" name="buttonValue"/>
            <cong:hidden id="locked" name="locked"/>
            <cong:hidden id="doorNumber" name="doorNumber"/>
            <cong:hidden id="docReceived" name="docReceived"/>
            <cong:hidden id="coloaderDevngAcctNo" name="coloaderDevngAcctNo"/>
            <cong:hidden id="dispositionCode" name="dispositionCode" value="${dispositionCode}" />
            <cong:hidden id="dispositionId" name="dispositionId" value="${dispositionId}" />
            <input type="hidden" id="goBackVoyageNo" name="goBackVoyageNo" value="${lclssheader.scheduleNo}"/>
            <input type="hidden" id="currentDispo" name="currentDispo" value="${lclUnitSSList.dispoCode}"/>


            <input type="hidden" name="limt" id="requestLimit" value="${limit}"/>
            <cong:hidden id="limit" name="searchVoyageLimit" value="${lclAddVoyageForm.searchVoyageLimit}"/>
            <cong:hidden id="searchOriginId" name="searchOriginId" value="${lclAddVoyageForm.searchOriginId}"/>
            <cong:hidden id="searchFdId" name="searchFdId" value="${lclAddVoyageForm.searchFdId}"/>
            <cong:hidden id="searchTerminalNo" name="searchTerminalNo" value="${lclAddVoyageForm.searchTerminalNo}"/>
            <cong:hidden id="searchLoginId" name="searchLoginId" value="${lclAddVoyageForm.searchLoginId}"/>
            <cong:hidden id="searchUnitNo" name="searchUnitNo" value="${lclAddVoyageForm.searchUnitNo}"/>
            <cong:hidden id="searchMasterBl" name="searchMasterBl" value="${lclAddVoyageForm.searchMasterBl}"/>
            <cong:hidden id="searchAgentNo" name="searchAgentNo" value="${lclAddVoyageForm.searchAgentNo}"/>
            <cong:hidden id="searchVoyageNo" name="searchVoyageNo" value="${lclAddVoyageForm.searchVoyageNo}"/>
            <cong:hidden id="searchDispoId" name="searchDispoId" value="${lclAddVoyageForm.searchDispoId}"/>
            <cong:hidden id="searchOrigin" name="searchOrigin" value="${lclAddVoyageForm.searchOrigin}"/>
            <cong:hidden id="searchFd" name="searchFd" value="${lclAddVoyageForm.searchFd}"/>
            <cong:hidden id="searchTerminal" name="searchTerminal" value="${lclAddVoyageForm.searchTerminal}"/>
            <cong:hidden id="homeScreenVoyageFileFlag" name="homeScreenVoyageFileFlag" value="${lclAddVoyageForm.homeScreenVoyageFileFlag}"/>

            <%--
            <input type="hidden" id="searchOriginName" name="searchOriginName" value="${searchOriginName}"/>
            <input type="hidden" id="searchOriginId" name="searchOriginId" value="${searchOriginId}"/>
            <input type="hidden" id="searchDestinationName" name="searchDestinationName" value="${searchDestinationName}"/>
            <input type="hidden" id="searchDestinationId" name="searchDestinationId" value="${searchDestinationId}"/>
            <input type="hidden" id="unitNo1" name="unitNo1" value="${unitNo1}"/>
            <input type="hidden" id="masterBL1" name="masterBL" value="${masterBL1}"/>
            <input type="hidden" id="agentNo" name="agentNo" value="${agentNo}"/>
            --%>
            <table width="99%" border="0" cellpadding="1" cellspacing="1"  align="center">
                <tr align="center">
                <input type="hidden" id="voyageOwnerFlag" name="voyageOwnerFlag"/>
                <td style="color:blue;font-weight: bold;font-size: 12px" width="5%">Voyage Owner :<b  class="headerlabel">
                        <c:choose>
                            <c:when test="${roleDuty.lclVoyageOwner}">
                                <cong:autocompletor name="voyageOwner" fields="voyenteredById" id="voyageOwner" template="one" query="BL_PERSON" width="200" container="NULL" scrollHeight="200px" styleClass="textlabelsLclBoldForBl"
                                                    value="${fn:toUpperCase(lclssheader.ownerUserId.loginName)}" shouldMatch="true" callback="changeVoyageOwner()"/>
                                <cong:hidden id="voyenteredById" name="voyenteredById" value="${fn:toUpperCase(lclssheader.ownerUserId.id)}"/>
                            </c:when>
                            <c:otherwise>
                                <input type="text" readonly="true" name="voyageOwner" class="text-readonly textlabelsBoldForTextBoxDisabledLook" id="voyageOwner" value="${fn:toUpperCase(lclssheader.ownerUserId.loginName)}"/>
                            </c:otherwise>
                        </c:choose>
                    </b>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="button" class="button-style1" id="Go Back" value='Go Back' onclick="backtoSearchScreen('${path}');"/>
                </td>
            </tr>
        </table>
        <table width="100%" border="0" cellpadding="1" cellspacing="1" align="center">
            <tr>
                <td>
                    <%@include  file="/jsps/LCL/import/voyage/unit/voyageInfoResults.jsp" %>
                    <br/>
                    <%@include  file="/jsps/LCL/import/voyage/unit/ssVoyageDetailsResults.jsp" %>
                    <br/>
                    <%@include  file="/jsps/LCL/import/voyage/unit/unitResults.jsp" %>
                    <br/>
                    <c:if test="${not empty pickedDrList}">
                        <%@include  file="/jsps/LCL/import/voyage/unit/pickedDrList.jsp" %>
                    </c:if>
                </td>
            </tr>
        </table>
    </cong:form>
    <script type="text/javascript">
        jQuery(document).ready(function () {
            $("[title != '']").not("link").tooltip();
            addNewVoyagePopUp();
            setUnitDiv();
            remarksVisible();
        });
        function addNewVoyagePopUp() {
            var polName = $("#pooOrigin").val();
            var polCode = polName.substring(polName.indexOf("(") + 1, polName.indexOf(")"));
            $("#polUnlocationCode").val(polCode);
            document.lclAddVoyageForm.polUnlocationCode.value = polCode;
            if ($("#openPopup").val() !== null && ($("#openPopup").val() === "openPopup") ||
                ($("#openPopup").val() === "openPopupOnly")) {
                addDetailsPopUp($('#path').val());
            }
        }
        function setUnitDiv() {
            var datatableobj = document.getElementById('lclssDetail');
            if (datatableobj !== null && datatableobj.rows.length >= 2) {
                document.getElementById('lclUnitHeadingDiv').style.position = "relative";
                document.getElementById('lclUnitHeadingDiv').style.visibility = "visible";
            }
        }
        function remarksVisible() {
            if ($("#closedRemarks").val() !== null && $("#closedRemarks").val() !== "") {
                document.getElementById('closedRemarksSpan').style.position = "relative";
                document.getElementById('closedRemarksSpan').style.visibility = "visible";
            }
            if ($("#auditedRemarks").val() !== null && $("#auditedRemarks").val() !== "") {
                document.getElementById('auditedRemarksSpan').style.position = "relative";
                document.getElementById('auditedRemarksSpan').style.visibility = "visible";
            }
        }

        function addDetailsPopUp(path) {//Create New Voygae
            var polName = $("#pooOrigin").val();
            var polCode = polName.substring(polName.indexOf("(") + 1, polName.indexOf(")"));
            var limit = $('#limit').val();
            var href = path + "/lclImportAddVoyage.do?methodName=openDetailsPopup&polUnlocationCode=" + polCode + "&searchVoyageLimit=" + $('#requestLimit').val();
            $.colorbox({
                iframe: true,
                href: href,
                width: "100%",
                height: "80%",
                title: "Voyage Details",
                onClosed: function () {
                    backtoSearchScreen(path);
                }
            });
        }
        function congAlert(txt) {
            $.prompt(txt);
        }

    </script>
</body>
</html>
