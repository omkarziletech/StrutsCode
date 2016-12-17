<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../fragment/lclFormSerialize.jspf"  %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/includes/resources.jsp" %>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/lclUnitsSchedule.js"></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/exportVoyageHblBatch.js"></cong:javascript>
<link type="text/css" rel="stylesheet" href="${path}/css/default/style.css"/>
<%@include file="/taglib.jsp" %>
<jsp:useBean id="lclAddVoyageForm" scope="request" class="com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm"/>
<%    lclAddVoyageForm.setHazmatPermitted("Y");
    lclAddVoyageForm.setRefrigerationPermitted("N");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add LCL Voyage</title>
    </head>
    <body>
        <div id="pane" style="overflow: auto;">
            <cong:form action="/lclAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm">
                <cong:hidden id="methodName" name="methodName"/>
                <cong:hidden id="headerId" name="headerId" value="${lclssheader.id}" />
                <input type="hidden" id="schedule" name="schedule" value="${lclssheader.scheduleNo}" />
                <cong:hidden id="detailId" name="detailId" value="${lclAddVoyageForm.detailId}" />
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
                <cong:hidden id="unitId" name="unitId"  />
                <cong:hidden id="unitssId" name="unitssId"  />
                <cong:hidden id="unitNo" name="unitNo"  />
                <cong:hidden id="unitType" name="unitType"  />
                <cong:hidden id="hazmatPermittedUnit" name="hazmatPermittedUnit"  />
                <cong:hidden id="hazmatPermitted" name="hazmatPermitted"  />
                <cong:hidden id="drayageProvided" name="drayageProvided"  />
                <cong:hidden id="intermodalProvided" name="intermodalProvided"  />
                <cong:hidden id="stopoff" name="stopoff"  />
                <cong:hidden id="chasisNo" name="chasisNo"  />
                <cong:hidden id="receivedMaster" name="receivedMaster"  />
                <cong:hidden id="sealNo" name="sealNo"  />
                <cong:hidden id="remarks" name="remarks"  />
                <cong:hidden id="cob" name="cob"/>
                <cong:hidden id="cobRemarks" name="cobRemarks"/>
                <cong:hidden id="verifiedEta" name="verifiedEta"  />
                <cong:hidden id="path" name="path" value="${path}" />
                <cong:hidden id="openPopup" name="openPopup" value="${openPopup}" />
                <cong:hidden id="refrigerationPermittedUnit" name="refrigerationPermittedUnit"  />
                <cong:hidden id="hiddenRefrigerationPermitted" name="hiddenRefrigerationPermitted"/>
                <cong:hidden id="loadLrdt" name="loadLrdt"  />
                <cong:hidden id="hazmatLrdt" name="hazmatLrdt"/>
                <cong:hidden id="hazmatLoadingDeadline" name="hazmatLoadingDeadline"  />
                <cong:hidden id="generalLoadingDeadline" name="generalLoadingDeadline"/>
                <cong:hidden id="loadLrdt" name="loadLrdt"  />
                <cong:hidden id="hazmatLrdt" name="hazmatLrdt"/>
                <cong:hidden id="closedRemarks" name="closedRemarks" value="${lclssheader.closedRemarks}"/>
                <cong:hidden id="reopenedRemarks" name="reopenedRemarks" value="${lclssheader.reopenedRemarks}"/>
                <cong:hidden id="auditedRemarks" name="auditedRemarks" value="${lclssheader.auditedRemarks}"/>
                <cong:hidden id="originId" name="originId" value="${polId}" /><%-- polId --%>
                <cong:hidden id="pooOrigin" name="pooOrigin" value="${originValue}" /><%-- polName --%>
                <cong:hidden id="finalDestinationId" name="finalDestinationId" value="${podId}" /><%-- podId --%>
                <cong:hidden id="podDestination" name="podDestination" value="${destinationValue}"/><%-- podName --%>
                <cong:hidden id="originalOriginId" name="originalOriginId" value="${pooId}" /><%-- pooId --%>
                <cong:hidden id="originalOriginName" name="originalOriginName" value="${originalOriginName}" /><%-- pooName --%>
                <cong:hidden id="originalDestinationId" name="originalDestinationId" value="${fdId}" /><%-- fdId --%>
                <cong:hidden id="originalDestinationName" name="originalDestinationName" value="${originalDestinationName}"/><%-- fdName --%>
                <cong:hidden id="filterByChanges" name="filterByChanges" value="${lclAddVoyageForm.filterByChanges}" />
                <cong:hidden id="polPodTT" name="polPodTT" value="${polPodTT}" />
                <cong:hidden id="co_dbd" name="co_dbd" value="${co_dbd}" />
                <cong:hidden id="co_tod" name="co_tod" value="${co_tod}" />
                <cong:hidden id="unitsReopened" name="unitsReopened" value="${unitsReopened}"/>
                <cong:hidden id="contractNumber" name="contractNumber"  />
                <cong:hidden id="prepaidCollect" name="prepaidCollect"  />
                <cong:hidden id="destPrepaidCollect" name="destPrepaidCollect"  />
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
                <cong:hidden id="releaseClause" name="releaseClause"  />
                <cong:hidden id="ssMasterBl" name="ssMasterBl"  />
                <cong:hidden id="changeVoyageNo" name="changeVoyageNo"  />
                <cong:hidden id="changeVoyHeaderId" name="changeVoyHeaderId"  />
                <cong:hidden id="oldUnitId" name="oldUnitId"  />
                <cong:hidden id="landCarrierAcountNumber" name="landCarrierAcountNumber"/>
                <cong:hidden id="landExitCityUnlocCode" name="landExitCityUnlocCode"/>
                <cong:hidden id="exportAgentAcctName" name="exportAgentAcctName"
                            value="${lclssheader.lclSsExports.exportAgentAcctoNo.accountName}"/>
                <cong:hidden id="exportAgentAcctNo" name="exportAgentAcctNo" 
                             value="${lclssheader.lclSsExports.exportAgentAcctoNo.accountno}"/>
                <cong:hidden id="agentBrandName" name="agentBrandName"/>
                
                <%-- <cong:hidden id="goBackVoyage" name="goBackVoyage" value="${goBackVoyage}"/>
                <cong:hidden id="goBackInland" name="goBackInland" value="${goBackInland}"/>
                <cong:hidden id="goBackCfcl" name="goBackCfcl" value="${goBackCfcl}"/> --%>

                <cong:hidden id="landExitDate" name="landExitDate"/>
                <cong:hidden id="searchLoadDisplay" name="searchLoadDisplay" value="${searchLoadDisplay}" />
                <cong:hidden id="unitTruckRemarks" name="unitTruckRemarks"/>
                <cong:hidden id="showUnCompleteUnits" name="showUnCompleteUnits"/>
                <cong:hidden id="searchLclContainerSize" name="searchLclContainerSize"/>
                <cong:hidden id="toScreenName" name="toScreenName"/>
                <cong:hidden id="intransitDr" name="intransitDr"/>
                <cong:hidden id="sslDocsCutoffDate" name="sslDocsCutoffDate"/>
                <cong:hidden id="filterByNewValue" name="filterByNewValue"/>
                <cong:hidden id="loadedBy" name="loadedBy"/>
                <cong:hidden id="loaddeByUserId" name="loaddeByUserId"/>
                <cong:hidden id="hazFlag" name="hazFlag"/>
                <cong:hidden id="unitSsIds" name="unitSsIds"/>
                <cong:hidden id="totalPieces" name="totalPieces"/>
                <cong:hidden id="volumeMetric" name="volumeMetric"/>
                <cong:hidden id="weightMetric" name="weightMetric" />
                <cong:hidden id="volumeImperial" name="volumeImperial"/>
                <cong:hidden id="weightImperial" name="weightImperial" />
                <input type="hidden" id="filterVal" value="${filterValues}"/>
                <input type="hidden" id="currentUnit" name="currentUnit"/>
                <input type="hidden" id="orgnId" name="orgnId" value="${lclssheader.origin.id}"/>
                <input type="hidden" id="fdId" name="fdId" value="${lclssheader.destination.id}"/>
                <input type="hidden" name="buttonValue" id="buttonValue"/>
                <input type="hidden" id="releaseLock" name="releaseLock" value="false"/>
                <input type="hidden" id="deleteMoveAction"  name="deleteMoveAction"/>
                <input type="hidden" id="wareHouseNo"  name="wareHouseNo"/>
                <cong:hidden id="schServiceType" name="schServiceType" value="${lclAddVoyageForm.schServiceType}"/>
                <cong:hidden id="searchOriginId" name="searchOriginId" value="${lclAddVoyageForm.searchOriginId}"/>
                <cong:hidden id="searchFdId" name="searchFdId" value="${lclAddVoyageForm.searchFdId}"/>
                <cong:hidden id="searchUnitNo" name="searchUnitNo" value="${lclAddVoyageForm.searchUnitNo}"/>
                <cong:hidden id="searchVoyageNo" name="searchVoyageNo" value="${lclAddVoyageForm.searchVoyageNo}"/>
                <cong:hidden id="searchOrigin" name="searchOrigin" value="${lclAddVoyageForm.searchOrigin}"/>
                <cong:hidden id="searchFd" name="searchFd" value="${lclAddVoyageForm.searchFd}"/>
                <cong:hidden id="cfclAcctName" name="cfclAcctName" value="${cfclAcctName}"/>
                <cong:hidden id="cfclAcctNo" name="cfclAcctNo" value="${cfclAcctNo}"/>
                <cong:hidden id="serviceType" name="serviceType" value="${serviceType}"/>
                <cong:hidden id="unitVoyageSearch" name="unitVoyageSearch" value="${lclAddVoyageForm.unitVoyageSearch}" />
                <cong:hidden id="printSsDockReceipt" name="printSsDockReceipt"/>
                <cong:hidden id="masterBlInvoiceValue" name="masterBlInvoiceValue"/>
                <cong:hidden id="comments" name="comments"/>
                <%-- LCL Unit SOLAS/VGM Info --%>
                <cong:hidden id="dunnageWeightLbs" name="dunnageWeightLbs"/>
                <cong:hidden id="dunnageWeightKgs" name="dunnageWeightKgs"/>
                <cong:hidden id="tareWeightLbs" name="tareWeightLbs"/>
                <cong:hidden id="tareWeightKgs" name="tareWeightKgs"/>
                <cong:hidden id="cargoWeightLbs" name="cargoWeightLbs"/>
                <cong:hidden id="cargoWeightKgs" name="cargoWeightKgs"/>
                <cong:hidden id="verificationSignature" name="verificationSignature"/>
                <cong:hidden id="verificationDate" name="verificationDate"/>
                <cong:hidden id="printViaMasterBl" name="printViaMasterBl"/>

                <table width="100%" border="0" cellpadding="1" cellspacing="1"  align="center">
                    <tr>
                        <td>
                            <center>
                                <span style="font-weight: bold; color: red;font-size: 16px;">
                                    ${(lclAddVoyageForm.filterByChanges eq 'lclExport' || filterValues eq 'lclExport') ? "USA LCL EXPORTS"
                                      :(lclAddVoyageForm.filterByChanges eq 'lclDomestic' || filterValues eq 'lclDomestic')
                                      ? "USA DOMESTIC INLAND":"CFCL"}
                                    <c:if test="${lclAddVoyageForm.filterByChanges eq 'lclCfcl' && not empty lclssheader && not empty lclssheader.lclSsExports &&
                                                  not empty lclssheader.lclSsExports.exportAgentAcctoNo}">
                                          &nbsp;
                                          <span style="color: green">
                                              ${lclssheader.lclSsExports.exportAgentAcctoNo.accountName}
                                              (${lclssheader.lclSsExports.exportAgentAcctoNo.accountno})</span>
                                          </c:if>
                                </span>
                            </center>
                        </td>
                    </tr>
                    <tr align="center">
                    <input type="hidden" id="voyageOwnerFlag" name="voyageOwnerFlag"/>
                    <td style="color:blue;font-weight: bold;font-size: 12px" width="5%">Voyage Owner :<b  class="headerlabel">
                            <c:choose>
                                <c:when test="${roleDuty.lclExpVoyageOwner}">
                                    <cong:autocompletor name="voyageOwner" fields="voyenteredById" id="voyageOwner" template="one" query="BL_PERSON" width="200" container="NULL" scrollHeight="200px" styleClass="textlabelsLclBoldForBl"
                                                        value="${fn:toUpperCase(lclssheader.ownerUserId.loginName)}" shouldMatch="true" callback="changeVoyageOwner()"/>
                                    <cong:hidden id="voyenteredById" name="voyenteredById" value="${fn:toUpperCase(lclssheader.ownerUserId.id)}"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="text" readonly="true" name="voyageOwner" class="text-readonly textlabelsBoldForTextBoxDisabledLook" id="voyageOwner" value="${fn:toUpperCase(lclssheader.ownerUserId.loginName)}"/>
                                    <cong:hidden id="voyenteredById" name="voyenteredById" value="${fn:toUpperCase(lclssheader.ownerUserId.id)}"/>
                                </c:otherwise>
                            </c:choose>
                           <cong:hidden id="voyOwnerTerminalName" name="voyOwnerTerminalName" value="${fn:toUpperCase(lclssheader.ownerUserId.terminal.terminalLocation)}"/>
                           <cong:hidden id="voyOwnerTerminalId" name="voyOwnerTerminalId" value="${fn:toUpperCase(lclssheader.ownerUserId.terminal.trmnum)}"/>
                        </b>
                    </td>
                    </tr>
                    <tr>
                        <td width="3%"> <div class="button-style1" 
                                             onclick="if (${lclAddVoyageForm.toScreenName eq 'DISPUTED_MASTER_BL'}) {
                                                         goToDisputedMasterBLScreen('${path}')
                                                     }
                                                     else {
                                                         goBackToVoyageScreen('${path}')
                                                     }">GO Back</div></td>
                    </tr>
                    <tr><td></td></tr>
                </table>
                    <table width="100%" border="0" cellpadding="1" cellspacing="1" align="center">
                        <tr>
                            <td>
                                <%@include file="/jsps/LCL/export/voyage/voyageDetails/voyageInfoResult.jsp"%>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <%@include file="/jsps/LCL/export/voyage/voyageDetails/voyageDetailsResult.jsp"%>
                            </td>
                        </tr>
                        <tr><td></td></tr>
                        <tr>
                            <td>
                                <%@include file="/jsps/LCL/export/voyage/voyageDetails/ssMasterResult.jsp"%>
                            </td>
                        </tr>
                        <tr><td></td></tr>
                        <tr>
                            <td>
                                <%@include file="/jsps/LCL/export/voyage/voyageDetails/unitsResult.jsp"%>
                            </td>
                        </tr>


                    </table>
            </cong:form>
        </div>
        <div id="error-container" class="static-popup" style="display: none;width: 500px;height: auto;">
            <div id="glAccountErrorMessage"></div>
        </div>
        <script type="text/javascript">
            jQuery(document).ready(function () {
                showStyleArinvoiceForVoyage();
                var path = document.getElementById("path").value;
                if (document.getElementById("openPopup").value != null && (document.getElementById("openPopup").value == "openPopup") ||
                        (document.getElementById("openPopup").value == "openPopupOnly"))
                {
                    addDetailsOnload(path);
                }
//                var datatableobj = document.getElementById('lclssDetail');
//                if (datatableobj != null && datatableobj.rows.length >= 2)
//                {
//                    document.getElementById('lclUnitHeadingDiv').style.position = "relative";
//                    document.getElementById('lclUnitHeadingDiv').style.visibility = "visible";
//                }
//                if (document.getElementById("closedRemarks").value != null && document.getElementById("closedRemarks").value != "")
//                {
//                    document.getElementById('closedRemarksSpan').style.position = "relative";
//                    document.getElementById('closedRemarksSpan').style.visibility = "visible";
//                }
//                if (document.getElementById("auditedRemarks").value != null && document.getElementById("auditedRemarks").value != "")
//                {
//                    document.getElementById('auditedRemarksSpan').style.position = "relative";
//                    document.getElementById('auditedRemarksSpan').style.visibility = "visible";
//                }

                var searchLoadDisplay = $('#searchLoadDisplay').val();
                var toScreenName = $('#toScreenName').val();
                if (searchLoadDisplay == 'Y') {
                    addUnits(path);
                }
                if (toScreenName === 'BookingToUnitViewDrScreen') {
                    viewDRSPopup(path, $("#unitssId").val(), '', $("#schedule").val());
                } else if (toScreenName === 'BookingToUnitLoadScreen') {
                    var originId = $("#originId").val();
                    var destnIn = $("#finalDestinationId").val();
                    var scheduleNo = $("#schedule").val();
                    var detailId = $("#detailId").val();
                    var href = path + "/lclAddVoyage.do?methodName=openUnits&originId=" + originId + "&finalDestinationId=" + destnIn + "&headerId="
                            + $("#headerId").val() + "&displayLoadComplete=N" + "&filterByChanges=" + $("#filterByChanges").val() + "&scheduleNo=" + scheduleNo
                            + "&detailId=" + detailId
                    if ($("#filterByChanges").val() === 'lclDomestic') {
                        href += "&intransitDr=" + $("#intransitDr").val()
                    }
                    if ($("#unitssId").val() !== '') {
                        href += "&unitssId=" + $("#unitssId").val() + "&unitSsIdFlag=true"
                    }
                    ;
                    openDetailPopup(href, $("#filterByChanges").val(), scheduleNo);
                }
                $("[title != '']").not("link").tooltip();
            });

            function addStopOff(path) {
                var href = path + "/lclAddVoyage.do?methodName=addStopOff&headerId=" + $("#headerId").val() + "&buttonValue=openStopOff";
                $.colorbox({
                    iframe: true,
                    href: href,
                    width: "80%",
                    height: "80%",
                    title: "Add Stop_Off",
                    onClosed: function () {
                        showLoading();
                        $("#buttonValue").val("closeStopOff");
                        $("#methodName").val('editVoyage');
                        $("#lclAddVoyageForm").submit();
                    }
                });
            }

        </script>
    </body>
</html>
