<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<script type="text/javascript" src="${path}/jsps/LCL/js/tooltip/jquery.qtip-1.0.0-rc3.min.js" ></script>
<link type="text/css" rel="stylesheet" media="screen" href="/logisoft/css/jquery/jquery.tooltip.css" />
<c:set var="fileNumberId" value="${param.fileNumberId}"></c:set>
<c:set var="fileNumber" value="${param.fileNumber}"></c:set>
<c:set var="status" value="${param.status}"></c:set>
<c:set var="copyFnVal" value="${param.copyFnVal}"></c:set>
<c:if test="${not empty lclCommodityList}">
    <c:set var="count" value="0"/>
    <c:set var="align1" value='width:auto;'/>
    <c:set var="align2" value='width:13%;'/>
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
            <c:choose>
                <c:when test="${rowStyle eq 'oddStyle'}">
                    <c:set var="rowStyle" value="evenStyle"/>
                </c:when>
                <c:otherwise>
                    <c:set var="rowStyle" value="oddStyle"/>
                </c:otherwise>
            </c:choose>
            <tr class="${rowStyle}">
                <td>
                    <input type="hidden" value="${empty commObj.commodityType.code ? commObj.commNo : commObj.commodityType.code}" class="commodity_no"/> <!--Filed Need For Lcl Exports to update Depand customer -->
                    <c:choose>
                        <c:when test="${not empty commObj.commodityType.descEn}">
                            <span title="${commObj.commodityType.descEn}">${fn:substring(commObj.commodityType.descEn,0,20)}</span>
                            (${commObj.commodityType.code})
                        </c:when>
                        <c:otherwise>
                            <c:if test="${not empty commObj.commName}">
                                <span title="${commObj.commName}">${fn:substring(commObj.commName,0,20)}(${commObj.commNo})</span>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td><span title="${fn:toUpperCase(commObj.pieceDesc)}">${fn:toUpperCase(fn:substring(commObj.pieceDesc,0,20))}</span></td>
                <td><span title="${fn:toUpperCase(commObj.markNoDesc)}">${fn:toUpperCase(fn:substring(commObj.markNoDesc,0,20))}</span></td>
                <td>
                    ${not empty commObj.actualPieceCount ? commObj.actualPieceCount : commObj.bookedPieceCount}
                </td>
                <td>
                    <c:choose>
                        <c:when test="${not empty commObj.packageType.description}">
                            <c:choose>
                                <c:when test="${commObj.bookedPieceCount<=1}">
                                    <c:out value="${fn:toUpperCase(commObj.packageType.description)}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${fn:toUpperCase(commObj.packageType.description)}${commObj.packageType.plural}"/>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${fn:toUpperCase(commObj.pkgName)}${fn:toLowerCase(commObj.packageType.plural)}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    ${not empty commObj.actualVolumeImperial ? commObj.actualVolumeImperial :commObj.bookedVolumeImperial}
                </td>
                <td>
                    ${not empty commObj.actualVolumeMetric ? commObj.actualVolumeMetric :commObj.bookedVolumeMetric}
                </td>
                <td>
                    ${not empty commObj.actualWeightImperial ? commObj.actualWeightImperial :commObj.bookedWeightImperial}
                </td>
                <td>
                    ${not empty commObj.actualWeightMetric ? commObj.actualWeightMetric :commObj.bookedWeightMetric}
                </td>
                <td>
                    ${commObj.weightVerified ? 'Y' : 'N'}
                </td>
                <td>${commObj.ofrateamount}
                    ${commObj.hazmat ? 'Y' : 'N'}
                </td>
                <c:if test="${not empty commObj.lclQuoteAcList}">
                    <c:forEach var="lclbookingac" items="${commObj.lclQuoteAcList}">
                        <c:if test="${lclbookingac.ratePerUnitUom == 'FRW' || lclbookingac.ratePerUnitUom == 'FRV' || lclbookingac.ratePerUnitUom == 'FRM' }">
                            <c:set var="ofrate" value=" $ ${lclbookingac.ratePerVolumeUnit}  / $  ${lclbookingac.ratePerWeightUnit}  / $  ${lclbookingac.rateFlatMinimum}"/>
                        </c:if>
                        <c:if test="${not empty lclbookingac.arglMapping && not empty lclbookingac.arglMapping.chargeCode &&
                                      lclbookingac.arglMapping.chargeCode == 'OFBARR' && lclbookingac.ratePerUnitUom == 'FL' }">
                            <c:set var="ofrate" value="$ ${lclbookingac.ratePerWeightUnit}"/>
                        </c:if>
                    </c:forEach>
                </c:if>
                <td style="${align2}">
                    <c:out value="${ofrate}"/>
                </td>
                <td>
                    <c:if test="${commObj.isBarrel}">
                        <span style="color: purple; font-weight: bolder">BAR</span>
                    </c:if>
                </td>
                <td style="${align1}">
                    <c:choose>
                        <c:when test="${not empty commObj.lclQuotePieceWhseList}">
                            <input type="button" class="green-background" value="Details" onclick="editQtWhseDetails('${path}', '${fileNumberId}', '${fileNumber}', '${commObj.id}');"/>
                            <c:set value="0" var="size"/>
                            <c:set var="wareLocationCode" value=""/>
                            <c:forEach items="${commObj.lclQuotePieceWhseList}" var="detail">
                                <c:set var="wareLocationCode" value="${detail.location}${not empty wareLocationCode ? '<>':''}${wareLocationCode}"/>
                                <c:set value="${detail.location}" var="location"/>
                                <c:set value="${detail.warehouse.warehouseNo}" var="warehouseNo"/>
                                <c:set value="${size+1}" var="size"/>
                            </c:forEach>
                            <c:out value="${warehouseNo}"/>
                            <span title="<c:import url="/jsps/LCL/wareHouseTooltip.jsp" >
                                      <c:param  name="locationCode" value="${wareLocationCode}" />
                                  </c:import>" >${fn:substring(fn:toUpperCase(location),0,10)}</span>
                            <c:if test="${size>1}">
                                <img alt="Warehouse" style="vertical-align: middle" src="${path}/img/icons/greenasteriskIcon.png" width="10" height="10" />
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <input type="button" class="button-style1 whseDetail" value="Details" onclick="editQtWhseDetails('${path}', '${fileNumberId}', '${fileNumber}', '${commObj.id}');"/>
                        </c:otherwise>
                    </c:choose>
                            
                            <span>
                                <img src="${path}/img/icons/iicon.png" width="16" height="16" alt="Client" id="dimsQuoteMainDetails${count}" name="dimsQuoteMainDetails"
                                     onclick="showDimsDetails('${count}', '${fileNumberId}');"/>
                            </span>
                </td>
                <td>
                    <img src="${path}/img/icons/UseCaseDiagram.gif" width="16" height="16" alt="info"
                         title="Entered BY:${commObj.enteredBy.firstName}&nbsp;${commObj.enteredBy.lastName}<br/>
                         on ${commObj.enteredDatetime}<br/>Modified BY:${commObj.modifiedBy.firstName}&nbsp;${commObj.modifiedBy.lastName}<br/>
                         on ${commObj.modifiedDatetime}"/>
                    <img src="${path}/images/edit.png" class="addCommodity" style="cursor: pointer" width="16" height="16" alt="edit"
                         onclick="editCommodity('${path}', '${count}', 'true', '${fileNumberId}', '${fileNumber}', '${copyFnVal}', '${commObj.id}');" title="Edit Commodity"/>
                    <c:if test="${roleDuty.deleteLclCommodity}">
                        <img src="${path}/images/error.png" style="cursor:pointer" width="16" height="16" alt="delete"
                             onclick="deleteQuoteCommodity('${count}', '${commObj.id}', '${fileNumberId}');"
                             title="Delete Commodity"/>
                    </c:if>
                    <c:if test="${commObj.hazmat}">
                        <c:set var="hazmatStyle" value="${not empty commObj.lclQuoteHazmatList ? 'green-background' : 'button-style1'}"/>
                        <input type="button" class="${hazmatStyle} Qhazmat" value="Haz" id="hazmatButton"
                               onclick="editQuoteHazmat('${path}', '${commObj.id}');"
                               title="Hazardous Information"/>
                    </c:if>
                </td>
            </tr>
        <input type="hidden" name="ofRate${count}" id="ofRate${count}" value="${ofrate}">
        <input type="hidden" id="commodityId" value="${commObj.id}"/>
        <input type="hidden" name="commodityNo" id="commodityNo" value="${commObj.commodityType.code}"/>
        <input type="hidden" name="commoditySpotNo" id="commoditySpotNo" value="${commObj.commodityType.code}"/>
        <input type="hidden" name="quotePieceId${count}" id="quotePieceId${count}" value="${commObj.id}"/>
        <input type="hidden" name="commodityNodims${count}" id="commodityNodims${count}" value="${empty commObj.commodityType.code ? commObj.commNo : commObj.commodityType.code}"/>
        <c:set var="count" value="${count+1}"/>
    </c:forEach>
    <input type="hidden" id="commodityCount" value="${count}"/>
</tbody>
</table>
</c:if>
<script type="text/javascript">
    jQuery(document).ready(function () {
     $("[title != '']").not("link").tooltip();
     });
     
     
    function showDimsDetails(count,fileNumberId) {
     var quotePieceId = $("#quotePieceId"+count).val();
     var commodityNo = $("#commodityNodims"+count).val();
    jQuery.ajaxx({
     
        dataType: "json",
        data: {
            className: "com.gp.cong.lcl.dwr.LclDwr",
            methodName: "displayQuoteDimsDetails",
            param1: quotePieceId,
            param2: fileNumberId,
            param3: commodityNo,
            request: "true",
            dataType: "json"
        },
        preloading: true,
        success: function (data) {
            JToolTipForLeft('#dimsQuoteMainDetails'+count, data[0], 450);
        }
    });
    }

</script>
