<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="fileNumberId" value="${param.fileNumberId}"></c:set>
<c:set var="fileNumber" value="${param.fileNumber}"></c:set>
<c:set var="status" value="${param.status}"></c:set>
<c:if test="${not empty lclCommodityList}">
    <cong:div style="width:100%;overflow-y:scroll">
        <c:set var="count" value="0"/>
        <display:table  name="${lclCommodityList}" id="commObj" class="dataTable" sort="list">
            <display:column title="Comm#">
                <input type="hidden" value="${empty commObj.commodityType.code 
                                              ? commObj.commNo : commObj.commodityType.code}" class="commodity_no"/>
                       ${commObj.commodityType.descEn}(${commObj.commodityType.code})
                </display:column>
                <display:column title="Comm Desc"  maxLength="40">
                    <span title="${fn:toUpperCase(commObj.pieceDesc)}">${fn:toUpperCase(fn:substring(commObj.pieceDesc,0,20))}</span>
                </display:column>
                <display:column title="Marks and Nums"  maxLength="40">
                    <span title="${fn:toUpperCase(commObj.markNoDesc)}">${fn:toUpperCase(fn:substring(commObj.markNoDesc,0,20))}</span>
                </display:column>
                <display:column title="Piece">
                    ${commObj.actualPieceCount}
                </display:column>
                <display:column title="Type">
                    <c:choose>
                        <c:when test="${commObj.bookedPieceCount<=1}">
                            <c:out value="${commObj.packageType.description}"/>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${commObj.packageType.description}${fn:toLowerCase(commObj.packageType.plural)}"/>
                        </c:otherwise>
                    </c:choose>
                </display:column>
                <display:column title="Msr(CFT)">
                    <fmt:formatNumber type="number" var="actualVolumeImperial"  pattern="0.00" value="${commObj.actualVolumeImperial}" />
                    ${(not empty commObj.actualVolumeImperial && commObj.actualVolumeImperial!='0.000')
                      ? actualVolumeImperial :''}
                </display:column>
                <display:column title="Msr(CBM)">
                    ${(not empty commObj.actualVolumeMetric && commObj.actualVolumeMetric!='0.000')
                      ? commObj.actualVolumeMetric :''}
                </display:column>
                <display:column title="Wgt(LBS)">
                    <fmt:formatNumber type="number" pattern="0.00" var="actualWeightImperial" value="${commObj.actualWeightImperial}" />
                    ${(not empty commObj.actualWeightImperial && commObj.actualWeightImperial!='0.000')
                      ? actualWeightImperial :''}
                </display:column>
                <display:column title="Wgt(KGS)">
                    ${(not empty commObj.actualWeightMetric && commObj.actualWeightMetric!='0.000')
                      ? commObj.actualWeightMetric :''}
                </display:column>
                <display:column title="Haz">
                    ${commObj.hazmat ? "Y" :"N"}
                </display:column>
                <display:column title="Action">
                    <img src="${path}/img/icons/UseCaseDiagram.gif" width="16" height="16" alt="info"
                         title="Entered BY:${commObj.enteredBy.firstName}&nbsp;${commObj.enteredBy.lastName}&nbsp; on ${commObj.enteredDatetime}<br/>
                         Modified BY:${commObj.modifiedBy.firstName}&nbsp;${commObj.modifiedBy.lastName}&nbsp; on ${commObj.modifiedDatetime}" />
                    <c:set var="count" value="${count+1}"/>
                    <img src="${path}/images/edit.png" id="bledit" class="addCommodity" style="cursor:pointer" width="16" height="16" alt="edit"
                         onclick="editBlCommodity('${path}', '${commObj.id}', 'true', '${fileNumberId}', '${fileNumber}');" title="Edit Commodity"/>
                    <c:if test="${roleDuty.deleteLclCommodity}">
                        <img src="${path}/images/error.png" class="hideImg" style="cursor:pointer" width="16" height="16" alt="delete"
                             onclick="deleteBlCommodity('${count-1}', '${commObj.id}');"
                             title="Delete Commodity" />
                    </c:if>
                    <c:if test="${commObj.hazmat}">
                        <input type="button" class="Blhazmat ${hazmatFlag ? 'green-background' :'button-style1'}"
                               value="Haz" title="Hazardous Information"
                               onclick="editBlHazmat('${path}', '${commObj.id}');"/>
                    </c:if>
                </display:column>
            </display:table>
            <cong:hidden id="commId" name="commId"/>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${commObj.lclFileNumber.id}"/>
        </cong:div>
    </c:if>
