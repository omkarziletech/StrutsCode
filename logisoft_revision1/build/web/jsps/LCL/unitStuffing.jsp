<%--
    Document   : UnitStuffing.jsp
    Created on : Oct 4, 2012, 9:02:07 PM
    Author     : Ram
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/taglib.jsp" %>
<%@include file="init.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:javascript src="${path}/jsps/LCL/js/lclExportUnitStuffing.js"></cong:javascript>
    <body>
        <div id="stuffingProgressBar" class="progressBar" style="position: absolute;left:30% ;top: 20%;right: 20%;bottom: 20%;visibility: hidden;">
            <p class="progressBarHeader"><b style="width: 100%;padding-left: 60px;">Processing......Please Wait</b></p>
            <div style="padding-left: 60px;">
                <input type="image" src="${path}/img/icons/newprogress_bar.gif"/>
        </div>
    </div>
    <cong:div style="width:99%; float:left;">
        <cong:hidden id="unitID" name="unitId" value="${unit.unitNo}"/>
        <cong:hidden id="unitNo" name="unitNo" value="${unit.unitNo}"/>
        <cong:hidden id="unitVolume" name="unitVolume" value="${unit.unitType.volumeImperial}"/>
        <cong:hidden id="volumeAllocated" name="volumeAllocated" value="0"/>
        <cong:hidden id="unitWeight" name="unitWeight" value="${unit.unitType.volumeImperial}"/>
        <cong:hidden id="unitssId" name="unitssId" value="${empty lclunitss.id ? unitssId :lclunitss.id}"/>
        <cong:hidden id="weightAllocated" name="weightAllocated" value="0"/>
        <cong:hidden id="index" name="index" value="${index}"/>
        <cong:hidden id="filterByChanges" name="filterByChanges" value="${lclAddVoyageForm.filterByChanges}"/>
        <input type="hidden" id="containerSize" name="containerSize" value="${unit.unitType.description}"/>
        <input type="hidden" id="filterVal" value="${filterValues}"/>
        <input type="hidden" id="detailId" value="${lclSsdetail.id}"/>
        <input type="hidden" id="hiddenSaveDestuff" name="hiddenSaveDestuff" />
        <input type="hidden" id="hiddenUnitId" name="hiddenUnitId" value="${unit.id}"/>
        <input type="hidden" id="unitSsIdFlag" name="unitSsIdFlag" value="${unitSsIdFlag}"/>
        <input type="hidden" id="loadedfileIds" name="loadedfileIds"/>
        <input type="hidden" id="hiddenDeletestuff" name="hiddenDeletestuff"/>
        <cong:hidden id="intransitDr" name="intransitDr" value="${lclAddVoyageForm.intransitDr}"/>
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="40%">
                    <c:if test="${lclAddVoyageForm.filterByChanges eq 'lclExport' || filterValues eq 'lclExport'}">
                        <span  style="font-weight: bold; color: black;font-size: 16px;">Agent:
                            <span style="color: blue">
                                <c:if test="${not empty lclAddVoyageForm.cfclAcctNo}">
                                    ${lclAddVoyageForm.cfclAcctName}(${lclAddVoyageForm.cfclAcctNo})</span>
                                </c:if>
                        </span>
                    </c:if>
                </td>
                <td width="60%">
                    <span style="font-weight: bold; color: red;font-size: 16px;">
                        <c:choose>
                            <c:when test="${lclAddVoyageForm.filterByChanges=='lclExport' || filterValues=='lclExport'}">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;USA LCL EXPORTS
                            </c:when>
                            <c:when test="${lclAddVoyageForm.filterByChanges=='lclDomestic' || filterValues=='lclDomestic'}">
                                USA DOMESTIC INLAND
                            </c:when>

                            <c:otherwise>
                                CFCL&nbsp;<span style="color: green">
                                    <c:if test="${not empty lclAddVoyageForm.cfclAcctNo}">
                                        ${lclAddVoyageForm.cfclAcctName}(${lclAddVoyageForm.cfclAcctNo})</span>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                    </span>
                </td>
            </tr>
            <tr><td colspan="2">
                    <center><font size="2" style="font-weight: bold;">Unit Stuffing</font></center>
                </td>
            </tr>
        </table>
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="71%">
                    <table width="100%" border="0"  align="left">
                        <c:set var="exportOrigin" value="${lclSsdetail.lclSsHeader.origin.unLocationName}/${lclSsdetail.lclSsHeader.origin.stateId.code}(${lclSsdetail.lclSsHeader.origin.unLocationCode})"/>
                        <c:set var="exportDestination" value="${lclSsdetail.lclSsHeader.destination.unLocationName}/${lclSsdetail.lclSsHeader.destination.countryId.codedesc}(${lclSsdetail.lclSsHeader.destination.unLocationCode})"/>
                        <c:set var="origin" value="${lclSsdetail.departure.unLocationName}/${lclSsdetail.departure.stateId.code}(${lclSsdetail.departure.unLocationCode})"/>
                        <c:set var="destination" value="${lclSsdetail.arrival.unLocationName}/${lclSsdetail.arrival.stateId.code}(${lclSsdetail.arrival.unLocationCode})"/>
                        <c:choose>
                            <c:when test="${(lclAddVoyageForm.filterByChanges!='lclDomestic' || filterValues!='lclDomestic') && (lclSsdetail.spReferenceName ne '' || lclSsdetail.spReferenceNo ne '')}">
                                <c:set var="vessel" value="${lclSsdetail.spReferenceName} v. ${lclSsdetail.spReferenceNo}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="vessel" value="${lclSsdetail.spReferenceName}"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="tableHeadingNew">

                            <c:choose>
                                <c:when test="${lclAddVoyageForm.filterByChanges ne 'lclDomestic' || filterValues ne 'lclDomestic'}">
                                    <td width="1px" style="font-weight:bold;font-size: 12px;"align="right">POL:</td>
                                    <td width="33%"class="boldGreen" align="left">${lclSsdetail ne null ? exportOrigin : null}</td>
                                    <td width="1px" style="font-weight:bold;font-size: 12px;"align="right">POD:</td>
                                    <td width="33%"class="boldGreen" align="left">${lclSsdetail ne null ? exportDestination : null}</td>
                                </c:when>
                                <c:otherwise>
                                    <td width="1px" style="font-weight:bold;font-size: 12px;" align="right">POL:</td>
                                    <td width="33%"class="boldGreen"align="left">${lclSsdetail ne null ? origin : null}</td>
                                    <td width="1px" style="font-weight:bold;font-size: 12px;"align="right">POD:</td>
                                    <td width="33%"class="boldGreen" align="left">${lclSsdetail ne null ? destination : null}</td>
                                </c:otherwise>
                            </c:choose>
                            <td width="1px" style="font-weight:bold;font-size: 12px;" align="right">Voyage:</td>
                            <td width="33%" class="boldGreen"align="left">${lclSsdetail ne null ? lclSsdetail.lclSsHeader.scheduleNo : null}</td>
                        </tr>
                        <tr  style="border-color:white;">
                            <td></td>
                        </tr>
                        <tr class="tableHeadingNew" >
                            <td width="1px" style="font-weight:bold;font-size: 12px;" align="right">Pier:</td>
                            <td width="33%" class="boldGreen" align="left">${lclSsdetail ne null ? origin : null} </td>
                            <td width="1px" style="font-weight:bold;font-size: 12px;"align="right">Sailing Date:</td>
                            <fmt:formatDate pattern="dd-MMM-yyyy" var="std" value="${lclSsdetail!=null ? lclSsdetail.std : null}"></fmt:formatDate>
                            <td width="33%" class="boldGreen"align="left">${std}</td>
                            <td width="1px" style="font-weight:bold;font-size: 12px;"align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Booking#:</td>
                            <td width="33%" class="boldGreen"align="left">${lclunitss.spBookingNo}</td>
                        </tr>
                        <tr style="border-color:white;">
                            <td></td>
                        </tr>
                        <tr class="tableHeadingNew" >
                            <td width="1px" style="font-weight:bold;font-size: 12px;"align="right">SSLine:</td>
                            <td width="33%" class="boldGreen"align="left">${lclSsdetail.spAcctNo != null ? lclSsdetail.spAcctNo.accountName : null}</td>
                            <td width="1px" style="font-weight:bold;font-size: 12px;" align="right">Vessel:</td>
                            <td width="33%" class="boldGreen"align="left">${vessel}</td>
                            <fmt:formatDate pattern="dd-MMM-yyyy" var="loadDeadlineDate" value="${lclSsdetail.generalLrdt}"></fmt:formatDate>
                                <td width="1px" style="font-weight:bold;font-size: 12px;"align="right">Loading Deadline:</td>
                                <td width="33%" class="boldGreen"align="left">${loadDeadlineDate}</td>
                        </tr>
                        <tr style="border-color:white;">
                            <td></td>
                        </tr>
                    </table>
                    <br/><br/>
                    <table>
                        <tr><td>
                                <div id="printSelection" class="button-style1"
                                     onclick="printLoadReport('${path}')">Print Selection&nbsp;</div>
                            </td>
                        <div >
                            <td id="showPicked" style="font-weight:bold;font-size: 12px;color: red; display: none;">
                                To Pick: 
                                <span  id="cuft" style="color: blue;"></span>
                                <span id="pounds" style="color: blue;"></span>
                            <br>
                                Container Total after Picking:
                                <span  id="totalCft" style="color: blue;"></span>
                                <span id="totalWgt" style="color: blue;"></span>
                            </td>
                        </div>  
            </tr>
        </table>

    </td>
<td width="1%">
<td width="28%">
    <div style="overflow: auto;height: 100px;width:auto;">
        <table border="1" id="booking" style="border-collapse: collapse; border: 1px solid #dcdcdc;width:100%">
            <tr class="tableHeading2">
                <td></td>
                <td>DR'S</td>
                <td>Pcs</td>
                <td>Cuft</td>
                <td>Pounds</td>
                <td>Size</td>
                <td>Capacity</td>
                <td>Load By</td>
            </tr>
            <tr>
                <td class="">Released</td>
                <td class="drCount">0</td>
                <td class="pieceCount"></td>
                <td class="pieceVolume"></td>
                <td class="pieceWeight"></td>
                <td class="size"></td>
                <td class="capacity"></td>
                <td class="loadBy"></td>
            </tr>
            <c:forEach items="${drList}" var="bookingUnitsBean" varStatus="status">
                <c:choose>
                    <c:when test="${zebra=='odd'}">
                        <c:set var="zebra" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="zebra" value="odd"/>
                    </c:otherwise>
                </c:choose>
                <c:if test="${bookingUnitsBean.label1 ne 'Released'}">
                    <tr class="${zebra}">
                        <td>
                            <c:if test="${bookingUnitsBean.label1 ne 'Released'}">
                                <c:choose>
                                    <c:when test="${!((fn:contains(unitStatusList[status.count-1] , 'C')) || (fn:contains(unitStatusList[status.count-1] , 'M')))}">
                                        <c:choose>
                                            <c:when test="${not empty bookScheduleNo  && not empty polEtd}">
                                                <a style="cursor: pointer;"
                                                   class="link ${bookingUnitsBean.unitId eq unit.id ? "highlight-saffron" :''}"
                                                   onclick="showCurrentUnit('${path}', '${bookingUnitsBean.unitId}', '${originId}', '${finalDestinationId}', '${bookingUnitsBean.unitssId}', '${headerId}', '${bookScheduleNo}', '${polEtd}')"/>
                                            </c:when>
                                            <c:otherwise>
                                                <a style="cursor: pointer;" 
                                                   class="link ${bookingUnitsBean.unitId eq unit.id ? "highlight-saffron" :''} releasedUnit${bookingUnitsBean.unitssId}"
                                                   onclick="resetUnit('${path}', '${bookingUnitsBean.unitId}', '${originId}', '${finalDestinationId}', '${bookingUnitsBean.unitssId}', '${headerId}')" >
                                                </c:otherwise>
                                            </c:choose>
                                            <font color="blue">
                                                <u>
                                                </c:when>
                                                <c:otherwise>
                                                    <font color="gray">
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                            ${bookingUnitsBean.label1}</font>
                                            <c:choose>
                                                <c:when test="${!((fn:contains(unitStatusList[status.count-1] , 'C')) || (fn:contains(unitStatusList[status.count-1] , 'M')))}">
                                            </u>
                                        </font>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>

                        </td>
                        <td>${bookingUnitsBean.count}</td>
                        <td>${bookingUnitsBean.totalPieceCount}</td>
                        <td class="totalvolume${bookingUnitsBean.unitId}">${bookingUnitsBean.totalVolumeImperial}</td>
                        <td  class="totalWeight${bookingUnitsBean.unitId}">${bookingUnitsBean.totalWeightImperial}</td>
                        <c:choose> 
                            <c:when test="${bookingUnitsBean.unitDesc ne null && bookingUnitsBean.unitDesc ne ''}">
                                <td title="${bookingUnitsBean.unitType}">${bookingUnitsBean.unitDesc}</td>
                            </c:when>
                            <c:otherwise>
                                <td title="${bookingUnitsBean.unitType}">${fn:substring(bookingUnitsBean.unitType,0,4)} ..</td>
                            </c:otherwise>
                        </c:choose>
                        <td>${bookingUnitsBean.unitCapacity - bookingUnitsBean.totalVolumeImperial}</td>
                        <td>${bookingUnitsBean.loadBy}</td>
                    </tr>
                </c:if>
            </c:forEach>
            <tr></tr>
        </table>
    </div>
</td>
</tr>
</table>
<c:import url="/jsps/LCL/stuffedBooking.jsp"/>
</cong:div>
</body>
