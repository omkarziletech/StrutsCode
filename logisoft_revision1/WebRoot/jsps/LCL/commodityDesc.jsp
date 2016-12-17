<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="fileNumberId" value="${param.fileNumberId}"></c:set>
<c:set var="fileNumber" value="${param.fileNumber}"></c:set>
<c:set var="copyFnVal" value="${param.copyFnVal}"></c:set>
<c:set var="status" value="${param.status}"></c:set>
<input type="hidden" name="moduleName" id="moduleName" value="${lclBookingForm.moduleName}"/>
<c:set var="index" value="0"/>
<c:if test="${not empty lclCommodityList}">
    <c:set var="align1" value='width:auto;'/>
    <c:set var="align2" value='width:11%;'/>
    <table class="dataTable" id="commObj">
        <thead>
        <th>Comm#</th>
        <th>Comm Desc</th>
        <th>Marks And Nums</th>
        <th>Piece</th>
        <th>Type</th>
        <th>Msr(CFT)</th>
        <th>Msr(CBM)</th>
        <th>Wgt(LBS)</th>
        <th>Wgt(KGS)</th>
        <th>Wt Vrfd	</th>
        <th>Haz</th>
        <th style="${align2}">O/F rate</th>
        <th>P/E</th>
        <th style='${align1}'>Whse Line</th>
        <th>Action</th>
    </thead>
    <tbody>
        <c:forEach items="${lclCommodityList}" var="commObj">
            <c:set var="rowStyle" value="${rowStyle eq 'odd' ? 'even' :'odd'}"/>
            <tr class="${rowStyle}">
                <td>
                    <input type="hidden" value="${empty commObj.commodityType.code ? commObj.commNo : commObj.commodityType.code}" class="commodity_no"/><!--Filed Need For Lcl Exports to update Depand customer -->
                    <c:choose>
                        <c:when test="${not empty commObj.commodityType.descEn}">
                            <span title="${commObj.commodityType.descEn}">${fn:substring(commObj.commodityType.descEn,0,20)}</span>(${commObj.commodityType.code})
                        </c:when>
                        <c:otherwise>
                            <c:if test="${not empty commObj.commName}">
                                ${commObj.commName}(${commObj.commNo})
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td style="text-transform: uppercase;"><span title="${commObj.pieceDesc}">${fn:substring(commObj.pieceDesc,0,20)}</span></td>
                <td style="text-transform: uppercase;"><span title="${commObj.markNoDesc}">${fn:substring(commObj.markNoDesc,0,20)}</span></td>
                <td>
                    ${not empty commObj.actualPieceCount ? commObj.actualPieceCount : commObj.bookedPieceCount}
                    <c:if test="${not empty commObj.actualPieceCount && commObj.actualPieceCount ne commObj.bookedPieceCount}">
                        <span style="color: red;font-size: 19px;font-weight: bold;vertical-align: middle"
                              title="Booked As ${commObj.bookedPieceCount}">*</span>
                    </c:if>
                </td>
                <td style="text-transform: uppercase;">
                    <c:choose>
                        <c:when test="${not empty commObj.packageType.description}">
                            <c:choose>
                                <c:when test="${not empty commObj.actualPackageType}">
                                    ${commObj.actualPackageType.description}${commObj.actualPieceCount>=2 ? commObj.actualPackageType.plural : ''}
                                </c:when>
                                <c:otherwise>
                                    ${commObj.packageType.description}${commObj.bookedPieceCount>=2 ? commObj.packageType.plural : ''}
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${commObj.pkgName}${commObj.packageType.plural}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${lclBookingForm.moduleName eq 'Exports'}">
                            <fmt:formatNumber type="number" var="actualVolumeImperial"  pattern="0.00" value="${commObj.actualVolumeImperial}" />
                            <fmt:formatNumber type="number" pattern="0.00" var="bookedVolumeImperial" value="${commObj.bookedVolumeImperial}" />
                            ${not empty commObj.actualVolumeImperial ? actualVolumeImperial : bookedVolumeImperial}
                        </c:when>
                        <c:otherwise>
                            ${not empty commObj.actualVolumeImperial ? commObj.actualVolumeImperial : commObj.bookedVolumeImperial}   
                        </c:otherwise>
                    </c:choose> 
                    <!--setting value for measureImp calculation-->
                    <c:if test="${not empty commObj.actualVolumeImperial && commObj.actualVolumeImperial ne '0.000'
                                  && not empty commObj.bookedVolumeImperial && commObj.bookedVolumeImperial ne '0.00'
                                  && commObj.bookedVolumeImperial ne '0.000'}">
                        <c:set var ="AVOL" value="${commObj.actualVolumeImperial}"/>
                        <c:set var ="BVOL" value="${empty commObj.bookedVolumeImperial ? 0:commObj.bookedVolumeImperial}"/>
                        <c:set var ="volumeDifference" value="${((AVOL-BVOL) / ((AVOL + BVOL)/2))*100}"/>
                        <c:if test="${not empty volumeDifference &&(volumeDifference le -9 || volumeDifference ge 9)}">
                            <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle"
                                  title="Booked As ${commObj.bookedVolumeImperial}">*</span>
                        </c:if>
                    </c:if>
                </td>
                <td>
                    ${not empty commObj.actualVolumeMetric ? commObj.actualVolumeMetric :commObj.bookedVolumeMetric}
                    <c:if test="${not empty commObj.actualVolumeMetric && commObj.actualVolumeMetric ne '0.000'
                                  && not empty commObj.bookedVolumeMetric && commObj.bookedVolumeMetric ne '0.000'}">
                        <c:set var="AVOLM" value="${commObj.actualVolumeMetric}"/>
                        <c:set var="BVOLM" value="${empty commObj.bookedVolumeMetric ? 0:commObj.bookedVolumeMetric}"/>
                        <c:set var ="volMetricDifference" value="${((AVOLM - BVOLM) / ((AVOLM + BVOLM)/2))*100}"/>
                        <c:if test="${not empty volMetricDifference && (volMetricDifference le -9 || volMetricDifference ge 9)}">
                            <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle"
                                  title="Booked As ${commObj.bookedVolumeMetric}">*</span>
                        </c:if>
                    </c:if>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${lclBookingForm.moduleName eq 'Exports'}">
                            <fmt:formatNumber type="number" var="actualWeightImperial"  pattern="0.00" value="${commObj.actualWeightImperial}" />
                            <fmt:formatNumber type="number" pattern="0.00" var="bookedWeightImperial" value="${commObj.bookedWeightImperial}" />
                            ${not empty commObj.actualWeightImperial ? actualWeightImperial : bookedWeightImperial}
                        </c:when><c:otherwise>
                            ${not empty commObj.actualWeightImperial ? commObj.actualWeightImperial : commObj.bookedWeightImperial}       
                        </c:otherwise>
                    </c:choose>
                    <!--setting value for weightImp calculation-->
                    <c:if test="${not empty commObj.actualWeightImperial && commObj.actualWeightImperial ne '0.000'
                                  && not empty commObj.bookedWeightImperial && commObj.bookedWeightImperial ne '0.000'}">
                        <c:set var="AWEI" value="${commObj.actualWeightImperial}"/>
                        <c:set var="BWEI" value="${empty commObj.bookedWeightImperial ? 0:commObj.bookedWeightImperial}"/>
                        <c:set var ="weightDifference" value="${((AWEI - BWEI) / ((AWEI + BWEI)/2))*100}"/>
                        <c:if test="${not empty weightDifference && (weightDifference le -9 || weightDifference ge 9)}">
                            <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle"
                                  title="Booked As ${commObj.bookedWeightImperial}">*</span>
                        </c:if>
                    </c:if>
                </td>
                <td>
                    ${not empty commObj.actualWeightMetric ? commObj.actualWeightMetric :commObj.bookedWeightMetric}
                    <!--setting value for weightMet calculation-->
                    <c:if test="${not empty commObj.actualWeightMetric && commObj.actualWeightMetric ne '0.000'
                                  && not empty commObj.bookedWeightMetric && commObj.bookedWeightMetric ne '0.000'}">
                        <c:set var="AWEIM" value="${commObj.actualWeightMetric}"/>
                        <c:set var="BWEIM" value="${empty commObj.bookedWeightMetric ? 0:commObj.bookedWeightMetric}"/>
                        <c:set var ="weiMetricDifference" value="${((AWEIM - BWEIM) / ((AWEIM + BWEIM)/2))*100}"/>
                        <c:if test="${not empty weiMetricDifference && (weiMetricDifference le -9 || weiMetricDifference ge 9)}">
                            <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle"
                                  title="Booked As ${commObj.bookedWeightMetric}">*</span>
                        </c:if>
                    </c:if>
                </td>
                <td>
                    ${commObj.weightVerified ? 'Y' : 'N'}
                </td>
                <td>
                    ${commObj.hazmat ? 'Y' : 'N'}
                </td>
                <c:if test="${not empty commObj.lclBookingAcList}">
                    <c:forEach var="lclbookingac" items="${commObj.lclBookingAcList}">
                        <c:if test="${lclbookingac.ratePerUnitUom == 'FRW' || lclbookingac.ratePerUnitUom == 'FRV' || lclbookingac.ratePerUnitUom == 'FRM' }">
                            <c:set var="ofrate" value="$ ${lclbookingac.ratePerVolumeUnit}  / $  ${lclbookingac.ratePerWeightUnit}  / $  ${lclbookingac.rateFlatMinimum}"/>
                        </c:if>
                        <c:if test="${not empty lclbookingac.arglMapping && not empty lclbookingac.arglMapping.chargeCode &&
                                      lclbookingac.arglMapping.chargeCode == 'OFBARR' && lclbookingac.ratePerUnitUom == 'FL' }">
                            <c:set var="ofrate" value="$ ${lclbookingac.ratePerWeightUnit}"/>
                        </c:if>
                    </c:forEach>
                </c:if>
                <td style="${align2}">
                    <c:out value="$ ${ofrate}"/>
                </td>
                <td>
                    <c:if test="${commObj.isBarrel}">
                        <span style="color: purple; font-weight: bolder">BAR</span>
                    </c:if>
                </td>
                <td style="${align1};text-transform: uppercase;" >
                    <input type="button" class="${not empty commObj.lclBookingPieceWhseList ? 'green-background' :'button-style1'} whseDetail"
                           value="Details" onclick="editWhseDetails('${path}', '${fileNumberId}', '${fileNumber}', '${commObj.id}');"/>
                    <c:set value="" var="wareLocationCode"/>
                    <c:if test="${not empty commObj.lclBookingPieceWhseList}">
                        <c:forEach items="${commObj.lclBookingPieceWhseList}" var="detail" varStatus="whse">
                            <c:set var="wareLocationCode" value="${detail.location}${not empty wareLocationCode ? '<>':''}${wareLocationCode}"/>
                            <c:if test="${whse.last}">
                                <c:set value="${detail.location}" var="location"/>
                                <c:set value="${detail.warehouse.warehouseNo}" var="warehouseNo"/>
                            </c:if>
                        </c:forEach>
                    </c:if>
                    ${warehouseNo} 
                    <span title="<c:import url="/jsps/LCL/wareHouseTooltip.jsp" >
                              <c:param  name="locationCode" value="${wareLocationCode}" />
                              </c:import>" >${fn:substring(location,0,10)}</span>
                    <input type="hidden" id="detailWarhouseNo" name="detailWarhouseNo" value="${warehouseNo}">
                    <c:if test="${fn:length(commObj.lclBookingPieceWhseList) > 1}">
                        <img alt="Warehouse" style="vertical-align: middle" src="${path}/img/icons/greenasteriskIcon.png" 
                             width="10" height="10" />
                    </c:if>
                </td>
                <td>
                     <c:if test="${lclBooking.lclFileNumber.status != 'X' && commObj.lclFileNumber.status != 'X'}">
                    <img src="${path}/img/icons/UseCaseDiagram.gif" width="16" height="16" alt="info"
                         title="Entered BY:${commObj.enteredBy.firstName}&nbsp;${commObj.enteredBy.lastName}<br/>
                         on ${commObj.enteredDatetime}<br/>Modified BY:${commObj.modifiedBy.firstName}&nbsp;${commObj.modifiedBy.lastName}<br/>
                         on ${commObj.modifiedDatetime}"/>
                     </c:if>
                    <c:set var="count" value="${count+1}"/>
                    <c:choose>
                        <c:when test="${not empty commObj.id && not empty fileNumberId}">
                            <img src="${path}/images/edit.png" class="addCommodity" style="cursor:pointer" width="16" height="16" alt="edit Commodity" id="viewgif"
                                 onclick="editBookingCommodity('${path}', '${commObj.id}', 'true', '${fileNumberId}', '${fileNumber}', '${copyFnVal}');" title="Edit"/>
                        </c:when>
                        <c:otherwise>
                            <img src="${path}/images/edit.png" class="addCommodity" style="cursor:pointer" width="16" height="16" alt="edit Commodity" id="viewgif"
                                 onclick="editBookingCommodity('${path}', '${count-1}', 'true', '${fileNumberId}', '${fileNumber}', '${copyFnVal}');" title="Edit"/>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${(roleDuty.deleteLclCommodity && (lclBooking.lclFileNumber.status != 'M' && commObj.lclFileNumber.status != 'M')) &&
                          (lclBooking.lclFileNumber.status != 'X' && commObj.lclFileNumber.status != 'X')}">
                        <img src="${path}/images/error.png" style="cursor:pointer" width="16" height="16" alt="delete" id="deleteIcon"
                             onclick="deleteCommodity('${count-1}', '${commObj.id}', '${fileNumberId}');"
                             title="Delete Commodity"/>
                    </c:if>
                    <c:if test="${commObj.hazmat}">
                        <c:set var="hazmatStyle" value="${not empty commObj.lclBookingHazmatList ? 'green-background' : 'button-style1'}"/>
                        <input type="button" class="${hazmatStyle} hazmat" value="Haz" id="hazmatButton"
                               onclick="editBkgHazmat('${path}', '${commObj.id}');"
                               title="Hazardous Information"/>
                    </c:if>
                </td>
            </tr>
            <cong:hidden name="comno" id="comno" value="${commObj.commodityType.code}"/>
        <input type="hidden" name="ofRate${count}" id="ofRate${count}" value="${ofrate}">
        <c:set var="index" value="${index+1}" />
    </c:forEach>
    <input type="hidden" id="commodityCount" value="${index}"/>
    <input type="hidden" id="commodityId" value="${commObj.id}"/>
    <cong:hidden id="commId" name="commId"/>
    <cong:hidden name="fileNumberId" id="fileNumberId" value="${commObj.lclFileNumber.id}"/>
</tbody>
</table>
</c:if>
