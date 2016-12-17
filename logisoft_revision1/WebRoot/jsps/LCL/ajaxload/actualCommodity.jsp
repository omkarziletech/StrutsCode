<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%-- Existing Values --%>
<input type="hidden" name="weightMetric" id="existActualWeightMetric" value="${lclBookingPiece.actualWeightMetric}"/>
<input type="hidden" name="volumeMetric" id="existActualVolumeMetric" value="${lclBookingPiece.actualVolumeMetric}"/>
<input type="hidden" name="weightImperial" id="existActualWeightImperial" value="${lclBookingPiece.actualWeightImperial}"/>
<input type="hidden" name="volumeImperial" id="existActualVolumeImperial" value="${lclBookingPiece.actualVolumeImperial}"/>
<cong:table>
    <cong:tr>
        <cong:td styleClass="td textlabelsBoldforlcl">Actual Weight Metric</cong:td>
        <cong:td>
            <input type="text" name="actualWeightMetric" id="actualWeightMetric" tabindex="-1" value="${actualWeightMet}"
                   class="text1 weight textlabelsBoldForTextBox" onkeyup="checkForNumberAndDecimal(this);
                           validateActualWeightMetric(this)"/>
            <span class="textBoldforlcl" align="center">KGS</span>
            <c:if test="${param.CommodityScreen=='blcommodity'}">
                 <cong:checkbox name='autoConvertMetric' id='autoConvertMetric' title="Disable auto-convert" alt="false" container="NULL" onclick="disableAutoConvert()"/>
            </c:if>
        </cong:td>
    </cong:tr>
    <cong:tr>
        <cong:td styleClass="td textlabelsBoldforlcl">Actual Volume Metric</cong:td>
        <cong:td>
            <input type="text" name="actualVolumeMetric" id="actualVolumeMetric"
                   tabindex="-1" value="${actualMeasureMet}" class="text1 weight actualBarrel textlabelsBoldForTextBox"
                   onkeyup="checkForNumberAndDecimal(this);
                           validateActualVolumeMetric(this)"/>
            <span class="textBoldforlcl" align="center">CBM</span>

        </cong:td>
    </cong:tr>
    <cong:tr>
        <cong:td styleClass="td textlabelsBoldforlcl">Actual Weight Imperial</cong:td>
        <cong:td>
            <cong:text name="actualWeightImperial" id="actualWeightImperial"
                       styleClass="text1 twoDigitDecFormat textlabelsBoldForTextBox" value="${actualWeightImp}"
                       container="NULL" onkeyup="checkForNumberAndDecimal(this);validateActualWeightImperial(this)"/>
            <span class="textBoldforlcl">LBS</span>
        </cong:td>
    </cong:tr>

    <cong:tr>
        <cong:td styleClass="td textlabelsBoldforlcl">Actual Volume Imperial</cong:td>
        <cong:td>
            <cong:text name="actualVolumeImperial" id="actualVolumeImperial" value="${actualMeasureImp}"
                       styleClass="text1 twoDigitDecFormat actualBarrel textlabelsBoldForTextBox" container="NULL"
                       onkeyup="checkForNumberAndDecimal(this);validateActualVolumeImperial(this)"/>
            <span class="textBoldforlcl">CFT</span>
        </cong:td>
    </cong:tr>
</cong:table>
