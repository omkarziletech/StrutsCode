
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <%@include file="init.jsp" %>
    <%@include file="/jsps/preloader.jsp" %>
    <%@include file="colorBox.jsp" %>
    <%@include file="../includes/baseResources.jsp" %>
    <%@include file="../includes/resources.jsp" %>
    <%@include file="/jsps/includes/jspVariables.jsp" %>
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
    <%@include file="../fragment/lclFormSerialize.jspf"  %>
    <cong:javascript src="${path}/jsps/LCL/js/common.js"/>
    <cong:javascript src="${path}/jsps/LCL/js/lcl.js"/>
    <cong:javascript src="${path}/jsps/LCL/js/lclExportBooking.js"/>
    <cong:javascript src="${path}/jsps/LCL/js/lclBooking.js"/>
    <cong:javascript src="${path}/jsps/LCL/js/common/lclExportCommon.js"/>
    <%@include file="/taglib.jsp" %>
    <body style="overflow:auto">
        <div  style="overflow:hidden" id="pane" >
            <c:if test="${lockMessage!='' && lockMessage!=null}">
                <center><font color="#FF4A4A" size="2"><b style="margin-left:150px;color: #000080;font-size: 15px;" >${lockMessage}</b></font></center>
                    </c:if>
                    <cong:form name="lclBookingForm" id="lclBookingForm" action="lclBooking.do">
                        <cong:hidden name="transShipMent" id="transShipMent" value="${lclBooking.bookingType eq 'T' ? 'Y' : ''}"/>
                        <cong:hidden name="bookingType" id="bookingType" value="${lclBooking.bookingType}"/>
                        <cong:hidden name="screenType" id="screenType"/>
                <input type="hidden" name="pickVoyId" id="pickVoyId" value="${lclBookingForm.pickVoyId}"/>
                <input type="hidden" name="detailId" id="detailId" value="${lclBookingForm.detailId}"/>
                <cong:hidden name="filterByChanges" id="filterByChanges" value="${lclBookingForm.filterByChanges}"/>
                <cong:hidden name="shortShipFileId" id="shortShipFileId" value="${lclBookingForm.shortShipFileId}"/>
                <cong:hidden name="shortShip" id="shortShip" value="${lclBookingForm.shortShip}"/><%-- Original ShortShip FileNo --%>
                <input type="hidden" name="hold" id="hold" value="${lclBooking.hold}"/>
                <cong:hidden name="unitSsId" id="unitSsId"/>
                <cong:hidden name="toScreenName" id="toScreenName"/>
                <cong:hidden name="consolidatedId" id="consolidatedId"/>
                <cong:hidden name="fromScreen" id="fromScreen"/>
                <cong:hidden name="pol" id="pol" value="${pol}"/>
                <cong:hidden name="pod" id="pod" value="${pod}"/>
                <input type="hidden" id="originId" value="${lclBooking.portOfLoading.id}"/>
                <input type="hidden" id="destinationId" value="${lclBooking.portOfDestination.id}"/>
                <input type="hidden" name="moduleId" id="moduleId" value="${lclBooking.lclFileNumber.fileNumber}"/>
                <input type="hidden" name="screenName" id="screenName" value="LCL FILE"/>
                <input type="hidden" name="moduleName" id="moduleName" value="${lclBookingForm.moduleName}"/>
                <input type="hidden" name="operationType" id="operationType" value="Scan or Attach"/>
                <input type="hidden" name="spotRateCommNo" id="spotRateCommNo" value="<bean:message key="application.spotRate.commodityCode"/>"/>
                       <input type="hidden" name="batchFileNoId" id="batchFileNoId" value="${lclBookingForm.lclFileNumber}" />
                <cong:hidden name="index" id="index" />
                <cong:hidden name="spotRateCommNo" id="spotRateCommNo"/>
                <cong:hidden name="cob" id="cob"/>
                <cong:hidden name="difflclBookedDimsActual" id="difflclBookedDimsActual" value="${lclBooking.enteredBy.difflclBookedDimsActual}"/>
                <input type="hidden" name="lockMessage" id="lockMessage" value="${lockMessage}"/>
                <cong:hidden name="ediData" id="ediData"/>
                <input type="hidden" name="fileState" id="fileState" value="${lclBooking.lclFileNumber.state}">
                <input type="hidden" id="originCityName" name="originCityName" value="${lclBooking.portOfOrigin.unLocationName},${lclBooking.portOfOrigin.stateId.code}"/>
                <!-- Setting Email Subject for Booking through File Number And Disposition Value only in LCL_Export -->
                <input type="hidden" id="fileNumberPrefix" name="fileNumberPrefix" value="${fn:substring(lclBooking.portOfOrigin.unLocationCode,2,5)}-${lclBooking.lclFileNumber.fileNumber}"/>
                <input type="hidden" id="exportDisposition" name="exportDisposition" value="${lclBookingForm.disposition}"/>
                <input type="hidden" id="aesRequiredForReleasingDRs" name="aesRequiredForReleasingDRs" value="${roleDuty.aesRequiredForReleasingDRs}"/>
                <input type="hidden" id="actualFieldsChangeAfterRelease" name="actualFieldsChangeAfterRelease" value="${roleDuty.weightChangeAfterRelease}"/>
                <input type="hidden" id="homeScreenDrFileFlag" name="homeScreenDrFileFlag" value="${lclBookingForm.homeScreenDrFileFlag}"/>
                <input type="hidden" id="removeDrHold" value="${roleDuty.removeDrHold}"/>
                <cong:hidden name="pooTrmNum" id="pooTrmNum" value="${lclBookingForm.pooTrmNum}"/>
                <cong:hidden name="polTrmNum" id="polTrmNum" value="${lclBookingForm.polTrmNum}"/>
                <cong:hidden name="podEciPortCode" id="podEciPortCode" value="${lclBookingForm.podEciPortCode}"/>
                <cong:hidden name="fdEciPortCode" id="fdEciPortCode"  value="${lclBookingForm.fdEciPortCode}"/>
                <cong:hidden name="fdEngmet" id="fdEngmet"  value="${lclBookingForm.fdEngmet}"/>
                <cong:hidden name="fileNumberStatus" id="fileNumberStatus" value="${lclBooking.lclFileNumber.status}"/>
                <input type="hidden" name="voyageClosedBy" id="voyageClosedBy"  value="${lclssheader.closedBy}"/>
                <input type="hidden" name="headerId" id="headerId"  value="${lclssheader.id}"/>
                <input type="hidden" name="blUnitCob" id="blUnitCob"  value="${blUnitCob}"/>
                <input type="hidden" name="isPicked" id="isPicked" value="${not empty pickOnVoyage}"/>
                <input type="hidden" name="hasBl" id="hasBl" value="${not empty lclBooking.lclFileNumber.lclBl}"/>
                <input type="hidden" name="calcBlRates" id="calcBlRates" value="false"/>
                <cong:hidden name="changeBlCharge" id="changeBlCharge" value="${lclBookingForm.changeBlCharge}"/>
                <cong:hidden name="previousSailing" id="previousSailing"/>
                <input type="hidden" id="isFormChanged" value="" />
                <input type="hidden" name="oldDestFee" id="oldDestFee" value="${bookingExport.includeDestfees}"/>
                <cong:hidden name="ups" id="ups" value="${lclBooking.lclFileNumber.lclBookingExport.ups}"/>
                <cong:hidden name="smallParcelRemarks" id="smallParcelRemarks"/>
                <input type="hidden" name="isMeasureImpChanged" id="isMeasureImpChanged" value="false"/>
                <input type="hidden" name="saveRemarks" id="saveRemarks" value="false"/>
                <input type="hidden" name="loginUser" id="loginUser" value="${loginuser.loginName}"/>
                <input type="hidden" name="loginUserId" id="loginUserId" value="${loginuser.userId}"/>
                <input type="hidden" name="unPickedFiles" id="unPickedFiles"/>
                <input type="hidden" id="insertInbondFlag" />
                <%@include file="booking/status.jsp" %>
                <%@include file="booking/button.jsp" %>
                <div class="table-block">
                    <cong:table>
                        <cong:tr>
                            <cong:td>
                                <cong:table>
                                    <cong:tr>
                                        <cong:td styleClass="black-border">
                                            <%@include file="booking/client.jsp" %>
                                        </cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr><!-- First Row ends here -->
                        <!--for Imports only-->
                        <cong:tr><cong:td></cong:td><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <%@include file="booking/tradeRoute.jsp" %>
                            </cong:td>
                        </cong:tr><!-- Second Row ends here -->
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <cong:table>
                                    <cong:tr styleClass="caption">
                                        <cong:td>
                                            <cong:table>
                                                <cong:tr>
                                                    <cong:td style="text-align:right; width:50%">
                                                        Commodity
                                                    </cong:td>
                                                    <cong:td style="text-align:left;width:50%">
                                                        <cong:div>
                                                            <span id="commodity1" class="button-style1 floatLeft addCommodity" onclick="newCommodity('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', 'false', '${lclBookingForm.moduleName}')"> Add New</span>
                                                        </cong:div>
                                                    </cong:td>
                                                </cong:tr>
                                            </cong:table>
                                        </cong:td>
                                    </cong:tr>
                                    <cong:tr>
                                        <cong:td>
                                            <cong:table  cellpadding="0" cellspacing="0" style="border-collapse: collapse" width="100%">

                                                <cong:tr>
                                                    <cong:td id="commodityDesc">
                                                        <!-- add the commodity descriptin list -->
                                                        <c:import url="/jsps/LCL/commodityDesc.jsp">
                                                            <c:param name="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
                                                            <c:param name="fileNumber" value="${lclBooking.lclFileNumber.fileNumber}"/>
                                                            <c:param name="copyFnVal" value="${lclBookingForm.copyFnVal}"/>
                                                        </c:import>
                                                    </cong:td>
                                                </cong:tr>
                                            </cong:table>
                                        </cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr><!-- third Row ends here -->
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td  width="100%" valign="top" id="upcomingSection" styleClass="black-border">
                                <cong:table border="0">
                                    <tr class="caption"  style="cursor: pointer">
                                        <cong:td onclick="toggleVoyageInfo()" align="left" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption">&nbsp;&nbsp;Upcoming Sailings&nbsp;&nbsp;</cong:td>
                                    <cong:td valign="middle"><input type="checkbox" name="showOlder" id="showOlder" title="Show Older" style="vertical-align: middle;" onclick="setRelayDetailsPrevious();"/>Show Older</cong:td>
                                    <cong:td onclick="toggleVoyageInfo()" align="left" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption" >&nbsp;&nbsp;Origin</cong:td>
                                    <cong:td onclick="toggleVoyageInfo()" width="12%" valign="middle"><cong:label text=""  style="font-weight:bold;color: green;font-size: 12px;text-transform: uppercase;border: 0px solid #DDDDDD;width:190px" id="pooSailing"></cong:label>
                                    </cong:td>
                                    <cong:td onclick="toggleVoyageInfo()" valign="middle">&nbsp;</cong:td>
                                    <cong:td onclick="toggleVoyageInfo()" align="left" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption" >POL:</cong:td>
                                    <cong:td onclick="toggleVoyageInfo()" width="12%" valign="middle"><cong:label text="" style="font-weight:bold;color: green;font-size: 12px;text-transform: uppercase;border: 0px solid #DDDDDD;width:190px" id="polSailing"/></cong:td>
                                    <cong:td onclick="toggleVoyageInfo()" valign="middle">&nbsp;</cong:td>
                                    <cong:td onclick="toggleVoyageInfo()" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption">POD:</cong:td>
                                    <cong:td onclick="toggleVoyageInfo()" width="12%" valign="middle"><cong:label text="" style="font-weight:bold;border:0px;color: green;font-size: 12px; text-transform: uppercase;border: 0px solid #DDDDDD;width:290px" id="podSailing"/></cong:td>
                                    <cong:td onclick="toggleVoyageInfo()" valign="middle">&nbsp;</cong:td>
                                    <cong:td onclick="toggleVoyageInfo()" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption">Destination:</cong:td>
                                    <cong:td onclick="toggleVoyageInfo()" width="12%" valign="middle"><cong:label text="" style="font-weight:bold;border:0px;color: green;font-size: 12px; text-transform: uppercase;border: 0px solid #DDDDDD;width:290px" id="fdSailing"/></cong:td>
                                        <td align="right" width="10%" onclick="toggleVoyageInfo()">
                                        <cong:label id="col">Click to Expand</cong:label>
                                        <cong:label id="exp">Click to Hide</cong:label>
                                            <a href="javascript: void()">
                                                <img alt="" src="${path}/img/icons/up.gif" border="0" style="margin: 0 0 0 0;float: right;"/>
                                        </a>
                                    </td>
                                    </tr>
                                </cong:table>
                                <cong:table>
                                    <cong:tr>
                                        <cong:td styleClass="text-readonly">
                                            <cong:div style="height:100px; overflow-y:auto;" id="upcomingSailing">
                                                <jsp:include page="/jsps/LCL/lclVoyage.jsp"/>
                                            </cong:div>
                                        </cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr>
                        <!-- fourth Row ends here -->
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="black-border" id="portremark">
                                <cong:table  width="100%">
                                    <cong:tr>
                                        <cong:td width="50%" valign="top">
                                            <table id="portSpecialRemarks" width="100%">
                                                <tr><td  class="caption" align="center">Port Special Remarks</td></tr>
                                                <tr><td>
                                                        <div  class="text-readonlytext8" style="width:auto; white-space: normal">
                                                            <span class="splRemarks">
                                                                <c:if test="${lclBookingForm.specialRemarksPod ne null && lclBookingForm.specialRemarksPod ne ''}">
                                                                    <span style="color:#800080;" >POD :</span>
                                                                    <span id="specialFd" style="color:red">${lclBookingForm.specialRemarksPod}</span><br/>
                                                                </c:if>
                                                                <c:if test="${lclBookingForm.specialRemarks ne null && lclBookingForm.specialRemarks ne ''}">
                                                                    <span style="color:#800080">FD :</span>
                                                                    <span  id="specialPod" style="color:red">${lclBookingForm.specialRemarks}</span>
                                                                </c:if>
                                                            </span>
                                                            <%-- Access only load from Script --%>
                                                            <span  class="splRemarks" id="specialPod" style="color:red"></span>
                                                            <span  class="splRemarks" id="specialFd" style="color:red"></span>
                                                        </div>
                                                        <div style="display:none;">
                                                            <cong:textarea id="specialRemarks" name="specialRemarks" value="${lclBookingForm.specialRemarks}"/>
                                                            <cong:textarea id="specialRemarksPod" name="specialRemarksPod" value="${lclBookingForm.specialRemarksPod}"/>
                                                        </div>
                                                    </td></tr>
                                            </table>                                            
                                        </cong:td>
                                        <cong:td width="50%">
                                            <table id="portInternalRemarks" width="100%">
                                                <tr><td class="caption" align="center">Port Internal Remarks</td></tr>
                                                <tr><td>
                                                        <div  class="text-readonlytext8"  style="width:auto; white-space: normal">
                                                            <span class="internRemarks">
                                                                <c:if test="${lclBookingForm.internalRemarksPod ne null && lclBookingForm.internalRemarksPod ne '' }">
                                                                    <span style="color:#800080" >POD :</span>
                                                                    <span id="internalFd" style="color:red">${lclBookingForm.internalRemarksPod}</span><br/>
                                                                </c:if>
                                                                <c:if test="${lclBookingForm.internalRemarks ne null && lclBookingForm.internalRemarks ne '' }">
                                                                    <span style="color:#800080">FD :</span>
                                                                    <span style="color:red;" id="internalPod">${lclBookingForm.internalRemarks}</span>
                                                                </c:if>
                                                            </span>
                                                            <%-- Access only load from Script --%>
                                                            <span  class="internRemarks" id="internalPod" style="color:red"></span>
                                                            <span  class="internRemarks" id="internalFd" style="color:red"></span>
                                                        </div>
                                                        <div style="display:none;">
                                                            <cong:textarea id="internalRemarks" name="internalRemarks" value="${lclBookingForm.internalRemarks}"/>
                                                            <cong:textarea id="internalRemarksPod" name="internalRemarksPod" value="${lclBookingForm.internalRemarksPod}"/>
                                                        </div>
                                                    </td></tr>
                                            </table>
                                        </cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr>
                        <cong:tr><cong:td></cong:td> </cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border" >
                                <table id="griRemarks" width="50%">
                                    <tr><td class="caption" align="center">Port GRI Remarks</td></tr>
                                    <tr><td>
                                            <div  class="text-readonlytext8" id="griRemarksDiv" style="width:auto; white-space: normal">
                                                <span class="griRemarks">
                                                    <c:if test="${lclBookingForm.portGriRemarksPod ne null && lclBookingForm.portGriRemarksPod ne '' }">
                                                        <span style="color:#800080" >POD :</span>
                                                        <span style="color:red" id="griRemarksFd">${lclBookingForm.portGriRemarksPod}</span><br/>
                                                    </c:if>
                                                    <c:if test="${lclBookingForm.portGriRemarks ne null && lclBookingForm.portGriRemarks ne '' }">
                                                        <span style="color:#800080">FD :</span>
                                                        <span style="color:red" id="griRemarksPod">${lclBookingForm.portGriRemarks}</span>
                                                    </c:if>
                                                </span>
                                                <%-- Access only load from Script --%>
                                                <span class="griRemarks" style="color:red" id="griRemarksPod"></span>
                                                <span class="griRemarks" style="color:red" id="griRemarksFd"></span>
                                            </div>
                                            <div style="display:none;">
                                                <cong:textarea id="portGriRemarks" name="portGriRemarks" value="${lclBookingForm.portGriRemarks}"/>
                                                <cong:textarea id="portGriRemarksPod" name="portGriRemarksPod" value="${lclBookingForm.portGriRemarksPod}"/>
                                            </div>
                                        </td></tr></table>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr><cong:td></cong:td></cong:tr>
                                <cong:tr><cong:td></cong:td></cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="black-border">
                                        <%@include file="booking/generalInformation.jsp" %>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr><cong:td></cong:td></cong:tr>
                                <cong:tr><cong:td></cong:td></cong:tr>
                            </cong:table>
                </div>
                <div class="table-block">
                    <cong:table>
                        <cong:tr>
                            <cong:td>
                                <div class="tab-block">
                                    <div class="tab-box">
                                        <a href="javascript:;" class="tabLink activeLink" id="cont-1"><span>Cost and Charges</span></a>
                                        <a href="javascript:;" class="tabLink isconsolidate" id="cont-2"><span>Consolidate With</span></a>
                                        <a href="javascript:;" class="tabLink asd" id="cont-3"><span>Parties</span></a>
                                        <a href="javascript:;" class="tabLink " id="cont-4"><span>AES Details</span></a>
                                    </div>
                                    <div class="tabcontent " id="cont-1-1">
                                        <cong:div id="chargeDesc">
                                            <c:import url="/jsps/LCL/ajaxload/chargeDesc.jsp">
                                                <c:param name="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
                                                <c:param name="fileNumber" value="${lclBooking.lclFileNumber.fileNumber}"/>
                                                <c:param name="fileNumberStatus" value="${lclBooking.lclFileNumber.status}"/>
                                            </c:import>
                                        </cong:div>
                                    </div>
                                    <div class="tabcontent hide" id="cont-2-1">
                                        <cong:table width="100%">                                            
                                            <cong:tr>
                                                <cong:td styleClass="tableHeadingNew">
                                                    Consolidate With
                                                    <input type="hidden" value="${enableBatchHsCode}" id="roleDutyForBatchCode"/>
                                                    <input type="hidden" value="${enableBatchHotCode}" id="roleDutyForHotCode"/>
                                                    <c:if test="${enableBatchHotCode eq 'true'}">
                                                        <div class="floatRight button-style1 consolidateHotCodes ${hideShow}" id="consolidateHotCodes" 
                                                             onclick="getBaseHotCodePopUp('${path}', '${lclBooking.lclFileNumber.id}');">Batch HOT Codes</div>
                                                    </c:if>
                                                    <c:if test="${enableBatchHsCode eq 'true'}">
                                                        <div class="floatRight button-style1 consolidateHsCodes ${hideShow}" id="consolidateHsCodes" 
                                                             onclick="getBaseHsCodePopUp('${path}', '${lclBooking.lclFileNumber.id}');">Batch HS Codes</div>

                                                    </c:if> 
                                                    <div class="floatRight button-style1 consolidate ${hideShow}" id="consolidate" 
                                                         onclick="showConsolidate('${path}', '#portOfDestinationId', '${lclBooking.lclFileNumber.fileNumber}', '#finalDestinationId',
                                                                         '${lclBooking.lclFileNumber.id}', '${lclBooking.portOfLoading.id}');">Consolidation View</div>

                                                </cong:td>
                                            </cong:tr>
                                            <cong:tr>
                                                <cong:td id="consolidateDesc">
                                                    <c:import url="/jsps/LCL/ajaxload/consolidateDesc.jsp">
                                                        <c:param name="fileIdA" value="${lclBooking.lclFileNumber.id}"/>
                                                    </c:import>
                                                </cong:td>
                                            </cong:tr>
                                        </cong:table>
                                    </div>
                                    <div class="tabcontent hide" id="cont-3-1">
                                        <%@include file="/jsps/LCL/tradingPartnerTab.jsp" %>
                                    </div>
                                    <div class="tabcontent hide" id="cont-4-1">
                                        <cong:table width="100%">
                                            <cong:tr>
                                                <cong:td styleClass="tableHeadingNew">AES DETAILS</cong:td>
                                                <cong:td styleClass="tableHeadingNew">
                                                    <div class="button-style1 floatRight ${hideShow}" id="aesdet" onclick="addAesDetails('${path}', '${lclBooking.lclFileNumber.fileNumber}', '${lclBooking.lclFileNumber.id}')">Add New</div>
                                                </cong:td>
                                            </cong:tr>
                                        </cong:table>
                                        <cong:div id="aesDesc">
                                            <c:import url="/jsps/LCL/ajaxload/aesDetailsDesc.jsp">
                                                <c:param name="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
                                                <c:param name="fileNumber" value="${lclBooking.lclFileNumber.fileNumber}"/>
                                            </c:import>
                                        </cong:div>
                                    </div>
                                </div>
                            </cong:td>
                        </cong:tr><!-- tab row ends here -->
                    </cong:table>
                </div>
                <cong:table border="0">
                    <cong:tr>
                        <td width="8%" style="padding-left: 9px;">
                            <table>
                                <tr class="textBoldforlcl">
                                    <td>File No :
                                        <c:if test="${not empty lclBooking.lclFileNumber.fileNumber}">
                                            <span class="fileNo" id="fileNumberBooking">
                                                <c:if test="${lclBooking.lclFileNumber.shortShip}">
                                                    ZZ${lclBooking.lclFileNumber.shortShipSequence}-${lclBooking.lclFileNumber.fileNumber}
                                                </c:if>
                                                <c:if test="${lclBooking.bookingType eq 'T' || lclBooking.bookingType eq 'I'}">
                                                    IMP-${lclBooking.lclFileNumber.fileNumber}
                                                </c:if>
                                                <c:if test="${lclBookingForm.moduleName eq 'Exports' && lclBooking.bookingType eq 'E' &&  lclBooking.lclFileNumber.shortShip eq 'false'}">
                                                    ${fn:substring(lclBooking.portOfOrigin.unLocationCode,2,5)}-${lclBooking.lclFileNumber.fileNumber}
                                                </c:if>
                                            </span>
                                        </c:if>
                                    </td></tr></table>
                        </td>
                    </cong:tr>
                </cong:table>
                <%@include file="booking/buttonBottom.jsp" %>
                <input type="hidden" name="methodName" id="methodName"/>
                <input type="hidden" name="clientToolTip" id="clientToolTip"/>
            </cong:form>
        </div>
    </body>
    <style>
        #lclEdiDataDiv {
            position: fixed;
            _position: absolute;
            z-index: 99;
            border-style:solid;
            border-width:2px;
            border-color:#808080;
            padding:0px 0px 0px 0px;
            background-color: #FFFFFF;
            left:10px;
            right:5px;
            top:0;
            margin:0 auto;
        }
    </style>
</html>
