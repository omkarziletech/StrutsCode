<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="editDimFlag" value="${param.editDimFlag}"></c:set>
<c:set var="commObjId" value="${param.quotePieceId}"></c:set>
<c:set var="commCode" value="${param.commCode}"></c:set>
<c:set var="labelName" value="${param.labelName}"/>
<c:choose>
    <c:when test="${not empty lclQuoteCommodityForm.moduleName}">
        <c:set var="moduleName" value="${lclQuoteCommodityForm.moduleName}"></c:set>
    </c:when>
    <c:otherwise>
        <c:set var="moduleName" value="${lclCommodityForm.moduleName}"></c:set>
    </c:otherwise>
</c:choose>
<c:if test="${empty moduleName}">
    <c:set var="moduleName" value="${moduleNames}"></c:set>
</c:if>
<cong:table>
    <c:choose>
        <c:when test="${moduleName eq 'Imports'}">
            <c:set var="textBoxCssStyle" value="textlabelsBoldForTextBoxDisabledLook"/>
            <c:set var="readonly" value="readonly='true'"/>
            <c:set var="tabindexVal" value="-1"/>
            <c:set var="readonlyval" value="true"/>
            <c:set var="labelName" value="Actual"/>
            <c:set var="toggleTitle" value="Switch to Imperial input"/>
        </c:when>
        <c:otherwise>
            <c:set var="toggleTitle" value="Switch to Metric input"/>
            <c:set var="textBoxCssStyle" value=""/>
            <c:set var="tabindexVal" value="0"/>
            <c:set var="readonlyval" value=""/>
            <c:set var="labelName" value="Booked"/>
        </c:otherwise>
    </c:choose>


    <%-- Existing Values --%>
    <input type="hidden" name="weightMetric" id="bkgWeightMetric" value="${lclBookingPiece.bookedWeightMetric}"/>
    <input type="hidden" name="volumeMetric" id="bkgVolumeMetric" value="${lclBookingPiece.bookedVolumeMetric}"/>
    <input type="hidden" name="weightImperial" id="bkgWeightImperial" value="${lclBookingPiece.bookedWeightImperial}"/>
    <input type="hidden" name="volumeImperial" id="bkgVolumeImperial" value="${lclBookingPiece.bookedVolumeImperial}"/>
    <input type="hidden" name="piece" id="piece" value="${bookedPieceCount}"/>
    <c:choose>
        <c:when test="${moduleName eq 'Imports'}">
            <cong:tr>
                <cong:td styleClass="td textlabelsBoldforlcl">Actual Weight Metric</cong:td>
                <cong:td>
                    <cong:text name="bookedWeightMetric" id="bookedWeightMetric" style="width: 65px" styleClass="text1 weight booked textlabelsBoldForTextBox"
                               value="${totalWeightMet}" container="NULL" onkeyup="checkForNumberAndDecimal(this);validationBookedWeightMetric(this)"
                               onchange="correctBookedWeightMetric(this);changePostDRcommodity();"/>
                    <span class="textlabelsBoldforlcl">KGS</span>
                </cong:td>
            </cong:tr>
        </c:when>
        <c:otherwise>
            <cong:tr>
                <cong:td styleClass="td textlabelsBoldforlcl">Booked Weight Metric</cong:td>
                <cong:td>
                    <input type="text" name="bookedWeightMetric"  id="bookedWeightMetric" style="width: 65px"
                           class="text1 weight booked textlabelsBoldForTextBox textlabelsBoldForTextBoxDisabledLook" readonly="true"
                           tabindex="-1" value="${totalWeightMet}"  onkeyup="checkForNumberAndDecimal(this);validationBookedWeightMetric(this);"
                           onchange="correctBookedVolumeMetric(this);"/>
                    <span class="textlabelsBoldforlcl">KGS</span>
                </cong:td>
            </cong:tr>
        </c:otherwise>
    </c:choose>
    <cong:tr>
        <cong:td styleClass="td textlabelsBoldforlcl">${labelName} Volume Metric</cong:td>
        <cong:td >
            <c:choose>
                <c:when test="${moduleName eq 'Imports'}">
                    <input type="text" name="bookedVolumeMetric" id="bookedVolumeMetric" value="${totalMeasureMet}"
                           style="width: 65px" class="text1 weight booked bookedBarrel textlabelsBoldForTextBox"
                           onkeyup="checkForNumberAndDecimal(this);validationBookedVolumeMetric(this);"
                           onchange="correctBookedVolumeMetric(this);changePostDRcommodity();"/>
                    <span class="textlabelsBoldforlcl"> CBM</span>
<!--                    <img src="${path}/img/icons/astar.gif" width="12" height="12" alt="star" id="toggle-measure" title="Switch to Imperial input"
                         onclick="toggleMeasure('${moduleName}');"/>-->
                    <span title="${toggleTitle}" id="toggle-Imp-met">
                        <img src="${path}/images/icons/arrow/green_down.png" width="18" height="18" alt="star" id="toggle-measure"
                             style="vertical-align: middle"
                             onclick="toggleMeasure('${moduleName}');"/>
                    </span>
                </c:when><c:otherwise>
                    <input type="text" name="bookedVolumeMetric" id="bookedVolumeMetric"  style="width: 65px"
                           value="${totalMeasureMet}"  class="text1 weight booked textlabelsBoldForTextBox  textlabelsBoldForTextBoxDisabledLook"
                           readonly="true"  tabindex="-1"
                           onkeyup="checkForNumberAndDecimal(this);validationBookedVolumeMetric(this);"
                           onchange="correctBookedVolumeMetric(this);"/>
                    <span class="textlabelsBoldforlcl"> CBM</span>
                    <span title="${toggleTitle}" id="toggle-Imp-met"> <img src="${path}/images/icons/arrow/green_Up.png" width="18" height="18" alt="star" id="toggle-measure"
                                                                           style="vertical-align: middle"
                                                                           onclick="toggleMeasure('${moduleName}');"/>
                    </span>
                </c:otherwise></c:choose>
        </cong:td >
    </cong:tr>

    <cong:tr>
        <cong:td styleClass="td textlabelsBoldforlcl">${labelName} Weight Imperial</cong:td>
        <cong:td>
             <c:choose>
                 <c:when test="${moduleName eq 'Imports'}">
            <input type="text" name="bookedWeightImperial" id="bookedWeightImperial" style="width: 65px" ${readonlyval}
                   class="weight textlabelsBoldForTextBox ${textBoxCssStyle} booked" tabindex="${tabindexVal}"
                   value="${totalWeightImp}" onkeyup="checkForNumberAndDecimal(this);validationBookedWeightImperial(this);"/>
                 </c:when><c:otherwise>
                      <input type="text" name="bookedWeightImperial" id="bookedWeightImperial" style="width: 65px" ${readonlyval}
                   class="twoDigitDecFormat textlabelsBoldForTextBox ${textBoxCssStyle} booked" tabindex="${tabindexVal}"
                   value="${totalWeightImp}" onkeyup="checkForNumberAndDecimal(this);validationBookedWeightImperial(this);"/>
                 </c:otherwise></c:choose>
            <span class="textlabelsBoldforlcl">LBS</span>
        </cong:td>
    </cong:tr>

    <cong:tr>
        <cong:td styleClass="td textlabelsBoldforlcl">${labelName} Volume Imperial</cong:td>
        <cong:td>
             <c:choose>
                 <c:when test="${moduleName eq 'Imports'}"> 
            <input type="text" name="bookedVolumeImperial" id="bookedVolumeImperial" ${readonlyval}
                   style="width: 65px" class="weight textlabelsBoldForTextBox ${textBoxCssStyle} booked"
                   tabindex="${tabindexVal}"  value="${totalMeasureImp}" onkeyup="checkForNumberAndDecimal(this);
                       validationBookedVolumeImperial(this);"/>
                 </c:when><c:otherwise>
                      <input type="text" name="bookedVolumeImperial" id="bookedVolumeImperial" ${readonlyval}
                   style="width: 65px" class="twoDigitDecFormat textlabelsBoldForTextBox ${textBoxCssStyle} booked bookedBarrel"
                   tabindex="${tabindexVal}"  value="${totalMeasureImp}" onkeyup="checkForNumberAndDecimal(this);
                       validationBookedVolumeImperial(this);"/>
                 </c:otherwise></c:choose>
            <span class="textlabelsBoldforlcl"> CFT</span>
        </cong:td>
    </cong:tr>

</cong:table>