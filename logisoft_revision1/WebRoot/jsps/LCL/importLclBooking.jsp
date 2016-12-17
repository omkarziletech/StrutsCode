<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <%@include file="init.jsp" %>
    <%@include file="/jsps/preloader.jsp" %>
    <%@include file="colorBox.jsp" %>
    <%@include file="../includes/baseResources.jsp" %>
    <%@include file="../includes/resources.jsp" %>
    <%@include file="/jsps/includes/jspVariables.jsp" %>
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
    <%@include file="../fragment/lclFormSerialize.jspf"  %>
    <cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
    <script type="text/javascript" src="${path}/jsps/LCL/js/lclImportBooking.js"></script>
    <cong:javascript  src="${path}/jsps/LCL/js/lclBooking.js"/>
    <cong:javascript  src="${path}/jsps/LCL/js/lcl.js"/>
    <%@include file="/taglib.jsp" %>
  <style type="text/css">
            #docListDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: #FFFFFF;
                left: 20%;
                right: 0;
                top: 20%;
            }
        </style>
    <body style="overflow:auto">
        <div  style="overflow:hidden" id="pane">
            <c:if test="${lockMessage!='' && lockMessage!=null}">
                <center><font color="#FF4A4A" size="2"><b style="margin-left:150px;color: #000080;font-size: 15px;">${lockMessage}</b></font></center>
            </c:if>
            <cong:form name="lclBookingForm" id="lclBookingForm" action="lclBooking.do">
                    <div id="cover"></div>  
                <cong:hidden name="screenType" id="screenType"/>
                <cong:hidden name="isFormchangeFlag" id="isFormchangeFlag"/>
                <cong:hidden name="unitSsId" id="unitSsId" value="${lclBookingForm.unitSsId}"/>
                <cong:hidden name="pdfDocumentName" id="pdfDocumentName" value="${lclBookingForm.pdfDocumentName}"/>
                <input type="hidden" name="unitId" id="unitId" value="${lclBookingForm.unitId}"/>
                <input type="hidden" name="segId" id="segId" value="${lclBookingSegregation.id}"/>
                <input type="hidden" name="parentFileNumberId" id="parentFileNumberId" value="${lclBookingSegregation.parentLclFileNumber.id}"/>
                <cong:hidden name="disposition" id="disposition" value="${lclBookingForm.disposition}"/>
                <cong:hidden name="unitStatus" id="unitStatus" value="${lclBookingForm.unitStatus}"/>
                <input type="hidden" name="moduleId" id="moduleId" value="${lclBooking.lclFileNumber.fileNumber}"/>
                <input type="hidden" name="fileNumberId" id="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
                <input type="hidden" name="screenName" id="screenName" value="LCL FILE"/>
                <input type="hidden" name="operationType" id="operationType" value="Scan or Attach"/>
                <input type="hidden" id ="cfclNo" name="cfcl" value="N"  />
                <input type="hidden" id ="aesBy" name="aesBy" value="N"  />
                <input type="hidden" id ="ups" name="ups" value="N"  />
                <input type="hidden" id="impCfsWarehsId" name="impCfsWarehsId" value="${lclBookingForm.impCfsWarehsId}"/>
                <input type="hidden" id="impCFSWareName" name="impCFSWareName" value="${lclBookingForm.impCFSWareName}"/>
                <input type="hidden" id="cfsWarehouseNo" name="cfsWarehouseNo" value="${lclBookingForm.cfsWarehouseNo}"/>
                <input type="hidden" id="cfsWarehouseAddress" name="cfsWarehouseAddress" value="${lclBookingForm.cfsWarehouseAddress}"/>
                <input type="hidden" id="cfsWarehouseCity" name="cfsWarehouseCity" value="${lclBookingForm.cfsWarehouseCity}"/>
                <input type="hidden" id="cfsWarehouseState" name="cfsWarehouseState" value="${lclBookingForm.cfsWarehouseState}"/>
                <input type="hidden" id="cfsWarehouseZip" name="cfsWarehouseZip" value="${lclBookingForm.cfsWarehouseZip}"/>
                <input type="hidden" id="cfsWarehousePhone" name="cfsWarehousePhone" value="${lclBookingForm.cfsWarehousePhone}"/>
                <input type="hidden" id="cfsWarehouseFax" name="cfsWarehouseFax" value="${lclBookingForm.cfsWarehouseFax}"/>
                <input type="hidden" id="unitCollect" name="unitCollect" value="${lclUnitSs.prepaidCollect}"/>
                <input type="hidden" id="homeScreenDrFileFlag" name="homeScreenDrFileFlag" value="${lclBookingForm.homeScreenDrFileFlag}"/>
                <input type="hidden" id="tabName" name="tabName" value="${lclBookingForm.tabName}"/>
                <%--  <input type="hidden" id="voyagesOriginIdSearch" name="voyagesOriginIdSearch" value="${lclBookingForm.voyagesOriginIdSearch}"/>
                <input type="hidden" id="voyagesDestinationIdSearch" name="voyagesDestinationIdSearch" value="${lclBookingForm.voyagesDestinationIdSearch}"/>
                <input type="hidden" id="voyagesOriginNameSearch" name="voyagesOriginNameSearch" value="${lclBookingForm.voyagesOriginNameSearch}"/>
                <input type="hidden" id="voyagesDestinationNameSearch" name="voyagesDestinationNameSearch" value="${lclBookingForm.voyagesDestinationNameSearch}"/>
                <input type="hidden" id="voyagesVoyageNoSearch" name="voyagesVoyageNoSearch" value="${lclBookingForm.voyagesVoyageNoSearch}"/>
                <input type="hidden" id="voyagesBillinTerminalSearch" name="voyagesBillinTerminalSearch" value="${lclBookingForm.voyagesBillinTerminalSearch}"/>
                <input type="hidden" id="voyagesBillinTerminalNoSearch" name="voyagesBillinTerminalNoSearch" value="${lclBookingForm.voyagesBillinTerminalNoSearch}"/>
                <input type="hidden" id="voyagesDispositionSearch" name="voyagesDispositionSearch" value="${lclBookingForm.voyagesDispositionSearch}"/>
                <input type="hidden" id="voyagesDispositionIdSearch" name="voyagesDispositionIdSearch" value="${lclBookingForm.voyagesDispositionIdSearch}"/>
                <input type="hidden" id="voyagesUnitNoSearch" name="voyagesUnitNoSearch" value="${lclBookingForm.voyagesUnitNoSearch}"/>
                <input type="hidden" id="voyagesMasterBlSearch" name="voyagesMasterBlSearch" value="${lclBookingForm.voyagesMasterBlSearch}"/>
                <input type="hidden" id="voyagesAgentNoSearch" name="voyagesAgentNoSearch" value="${lclBookingForm.voyagesAgentNoSearch}"/>--%>
                <input type="hidden" name="spotRateCommNo" id="spotRateCommNo" value="<bean:message key="application.spotRate.commodityCode"/>"/>
                <cong:hidden name="index" id="index" />
                <cong:hidden name="spotRateCommNo" id="spotRateCommNo"/>
                <cong:hidden name="difflclBookedDimsActual" id="difflclBookedDimsActual" value="${lclBooking.enteredBy.difflclBookedDimsActual}"/>
                <cong:hidden name="limit" id="limit" value="${lclBookingForm.limit}"/>
                <input type="hidden" name="lockMessage" id="lockMessage" value="${lockMessage}"/>
                <cong:hidden name="allowVoyageCopy" id="allowVoyageCopy"/>
                <input type="hidden" id="originCityName" name="originCityName" value="${lclBooking.finalDestination.unLocationName},${lclBooking.finalDestination.stateId.code}"/>
                <input type="hidden" name="allowTransshipment" id="allowTransshipment" value="${roleDuty.lclImportAllowTransshipment}"/>
                <input type="hidden" name="VoyageClosedBy" id="VoyageClosedBy" value="${lclssheader.closedBy}"/>
                <%@include file="booking/status.jsp" %>
                <%@include file="booking/button.jsp" %>
                <div class="table-block">
                    <cong:table>
                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <%@include file="booking/importClient.jsp" %>
                            </cong:td>
                        </cong:tr><!-- First Row ends here -->
                        <!--for Imports only-->
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <%@include file="/jsps/LCL/tradingPartnerTab.jsp" %>
                            </cong:td>
                        </cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <%@include file="booking/importTradeRoute.jsp" %>
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
                                                    <cong:td style="text-align:right; width:51%">
                                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Commodity
                                                    </cong:td>
                                                    <cong:td style="text-align:left;width:47.5%">
                                                        <cong:div>
                                                            <span id="commodity1" class="button-style1 floatLeft addCommodity" onclick="newCommodity('${path}','${lclBooking.lclFileNumber.id}','${lclBooking.lclFileNumber.fileNumber}','false','${lclBookingForm.moduleName}')"> Add New</span>
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
                            <cong:td  width="100%" valign="top" styleClass="black-border"  id="upcomingImpSection">
                                <cong:table border="0">
                                    <tr class="caption" onclick="toggleUpcomingSailings()" style="cursor: pointer">
                                        <cong:td align="left" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption">&nbsp;&nbsp;Upcoming Sailings&nbsp;&nbsp;</cong:td>
                                        <cong:td valign="middle">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</cong:td>
                                        <cong:td align="left" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption" >&nbsp;&nbsp;Origin:</cong:td>
                                        <cong:td width="12%" valign="middle"><cong:label id="originSailing"  styleClass="greenBold" style="width:100px"></cong:label>
                                        </cong:td>
                                        <cong:td valign="middle">&nbsp;</cong:td>
                                        <cong:td align="left" width="2%" style="font-size: 12px;" valign="middle" styleClass="caption" >POL:</cong:td>
                                        <cong:td width="12%" valign="middle"><cong:label styleClass="greenBold" style="width:190px" id="polSailing"/></cong:td>
                                        <cong:td valign="middle">&nbsp;</cong:td>
                                        <cong:td width="2%" style="font-size: 12px;" valign="middle" styleClass="caption">POD:</cong:td>
                                        <cong:td width="12%" valign="middle"><cong:label text="" styleClass="greenBold" style="width:290px" id="podSailing"/></cong:td>
                                        <cong:td valign="middle">&nbsp;</cong:td>
                                        <cong:td width="2%" style="font-size: 12px;" valign="middle" styleClass="caption">Destination:</cong:td>
                                        <cong:td width="12%" valign="middle"><cong:label text="" styleClass="greenBold" style="width:290px" id="destinationSailing"/></cong:td>
                                        <td align="right" width="10%">
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
                                        <cong:td>
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
                            <cong:td styleClass="black-border" id="remarksSection">
                                <cong:table>
                                    <cong:tr>
                                        <cong:td  width="50%" valign="top">
                                            <cong:table caption="Port Special Remarks" id="portSpecialRemarks">
                                                <cong:tr>
                                                    <cong:td>
                                                        <cong:textarea cols="78" rows="8" id="specialRemarks" name="specialRemarks" styleClass="text-readonlytext8" readOnly="true" value="${lclBookingForm.specialRemarks}">
                                                        </cong:textarea>
                                                    </cong:td>
                                                </cong:tr>
                                            </cong:table>
                                        </cong:td>
                                        <cong:td width="50%">
                                            <cong:table caption="Port Internal Remarks" id="portInternalRemarks">
                                                <cong:tr>
                                                    <cong:td>
                                                        <cong:textarea cols="78" rows="8" id="internalRemarks" name="internalRemarks" styleClass="text-readonlytext8" readOnly="true" value="${lclBookingForm.internalRemarks}">
                                                        </cong:textarea>
                                                    </cong:td>
                                                </cong:tr>
                                            </cong:table>
                                        </cong:td>
                                        <cong:td></cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr>
                        <cong:tr><cong:td></cong:td> </cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr id="regionalRemarkhide">
                            <cong:td styleClass="black-border" id="regionalRemark">
                                <cong:table caption="GRI and Regional Remarks" width="50%" id="griRemarks">
                                    <cong:textarea cols="90" rows="8" id="portGriRemarks" name="portGriRemarks" styleClass="text-readonlytext8" readOnly="true" value="${lclBookingForm.portGriRemarks}">
                                    </cong:textarea>
                                </cong:table>
                            </cong:td>
                        </cong:tr>
                        <cong:tr><cong:td></cong:td> </cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <%@include file="booking/importGeneralInformation.jsp" %>
                            </cong:td>
                        </cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr><cong:td></cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="black-border">
                                <%@include file="/jsps/LCL/lclImport.jsp" %>
                            </cong:td>
                        </cong:tr>
                    </cong:table>
                </div>
                <div class="table-block">
                    <cong:table>
                        <cong:tr>
                            <cong:td>
                                <div class="tab-block">
                                    <div class="tab-box">
                                        <a href="javascript:;" class="tabLink activeLink" id="cont-2"><span>Cost and Charges</span></a>
                                    </div>
                                    <div class="tabcontent activeLink" id="cont-2-1">
                                        <cong:div id="chargeDesc">
                                            <c:import url="/jsps/LCL/ajaxload/chargeDesc.jsp">
                                                <c:param name="fileNumberId" value="${lclBooking.lclFileNumber.id}"/>
                                                <c:param name="fileNumber" value="${lclBooking.lclFileNumber.fileNumber}"/>
                                                <c:param name="fileNumberStatus" value="${lclBooking.lclFileNumber.status}"/>
                                                <c:param name="bookingStatus" value="${lclBooking.bookingType}"/>
                                            </c:import>
                                        </cong:div>
                                    </div>
                                </div>
                            </cong:td>
                        </cong:tr><!-- tab row ends here -->
                    </cong:table>
                </div>
                <table border="0">
                    <cong:tr>
                        <td width="8%" style="padding-left: 9px;">
                            <table>
                                <tr class="textBoldforlcl">
                                    <td>File No :
                                        <c:if test="${not empty lclBooking.lclFileNumber.fileNumber}">
                                            <span class="fileNo">IMP-${lclBooking.lclFileNumber.fileNumber}</span>
                                        </c:if>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </cong:tr>
                </table>
                <%@include file="booking/buttonBottom.jsp" %>
                <input type="hidden" name="methodName" id="methodName"/>
                <input type="hidden" name="clientToolTip" id="clientToolTip"/>
            </cong:form>
        </div>
    </body>
</html>
