<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:choose>
    <c:when test="${not empty lclQuoteForm.moduleName}">
        <c:set var="moduleName" value="${lclQuoteForm.moduleName}"></c:set>
        <c:set var="agentRefresh" value="submitAjaxForm('refreshAgent','#lclQuoteForm','#m','');"></c:set>
    </c:when>
    <c:otherwise>
        <c:set var="moduleName" value="${lclBookingForm.moduleName}"></c:set>
        <c:set var="agentRefresh" value="submitAjaxFormForAgent('refreshAgent','#lclBookingForm','#m','');updatePOD();"></c:set>
        <c:set var="impBkg" value="true"/>
    </c:otherwise>
</c:choose>
<cong:tr>
    <cong:td id="polLabel" width="45%" valign="bottom" styleClass="textlabelsBoldforlcl">POL</cong:td>
    <cong:td id="polDojo" width="20%">
        <c:choose>
            <c:when test="${lclBooking.bookingType eq 'T' && moduleName eq 'Exports'}">
                <cong:autocompletor name="portExit" id="portOfLoading" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" template="one" shouldMatch="true"
                                    fields="NULL,NULL,polUnlocationcode,portOfLoadingId" query="RELAY_ORIGIN"  width="500" labeltitle="Port Of Loading" container="NULL"
                                    value="${lclBookingForm.portExit}" scrollHeight="200" callback="setRelayOverRideYes();setRelayDetails('relayOverride');"/>
                <cong:hidden name="portExitId" id="portOfLoadingId" value="${lclBookingForm.portExitId}"/>
            </c:when>
            <c:otherwise>
                <cong:autocompletor id="portOfLoading" name="portOfLoading" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" template="one" shouldMatch="true"
                                    callback="setRelayOverRideYes();calculateRelayTTREVCharge();setRelayDetails('relayOverride');"
                                    fields="NULL,NULL,polUnlocationcode,portOfLoadingId" query="RELAY_ORIGIN" 
                                    width="500" container="NULL"  value="${pol}"  scrollHeight="200px"/>
                <cong:hidden name="portOfLoadingId" id="portOfLoadingId" value="${portOfLoadingId}"/>
                <cong:hidden name="polUnlocationcode" id="polUnlocationcode" value="${polUnlocationcode}"/>
                <%--           <cong:hidden name="originUnlocationCode" id="originUnlocationCode" value="${lclBookingForm.originUnlocationCode}"/>
                           <cong:hidden name="portOfOriginId" id="portOfOriginId" value="${lclBookingForm.portOfOriginId}"/>
                           <input type="hidden" id="warehsterminalNo" name="warehsterminalNo" value="${lclBooking.portOfOrigin.terminal.trmnum}"/>
            <cong:autocompletor name="portOfLoading" id="portOfLoading" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" template="one" shouldMatch="true"
                               fields="NULL,NULL,polUnlocationcode,portOfLoadingId" query="RELAYNAME" width="500" labeltitle="Port Of Loading" container="NULL"
                               value="${pol}" scrollHeight="200" callback="setRelayOverRideYes();setRelayDetails();calculateRelayTTREVCharge();"/>
           <cong:hidden name="portOfLoadingId" id="portOfLoadingId" value="${portOfLoadingId}"/>
           <cong:hidden name="polUnlocationcode" id="polUnlocationcode" value="${polUnlocationcode}"/> --%>
            </c:otherwise>
        </c:choose>
        <cong:hidden name="relaySearch" id="relaySearch" value="${relaySearch}"/>
        <input type="hidden" id="isRelay" value="${relayOverRide}"/>
    </cong:td>
</cong:tr>
<cong:tr>
    <cong:td width="10%" styleClass="textlabelsBoldforlcl">POD</cong:td>
    <cong:td  width="20%">
        <c:choose>
            <c:when test="${lclBooking.bookingType eq 'T' && moduleName eq 'Exports'}">
                <cong:autocompletor name="foreignDischarge" id="portOfDestination" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"  query="CONCAT_RELAY_NAME_FD"
                                    shouldMatch="true" fields="NULL,NULL,podUnlocationcode,portOfDestinationId" template="one" width="500"
                                    container="NULL" scrollHeight="200" value="${lclBookingForm.foreignDischarge}"
                                    callback="calculateRates();setRelayOverRideYes();podDestination();setRelayDetails('relayOverride');${agentRefresh}" />
                <cong:hidden name="foreignDischargeId" id="portOfDestinationId" value="${lclBookingForm.foreignDischargeId}"/>
                <cong:hidden name="podUnlocationcode" id="podUnlocationcode"  value="${lclBookingImport.foreignPortOfDischarge.unLocationCode}"/>
            </c:when>
            <c:otherwise>
                <cong:autocompletor name="portOfDestination" id="portOfDestination" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"  query="CONCAT_RELAY_NAME_FD"
                                    fields="NULL,NULL,podUnlocationcode,portOfDestinationId" template="one" width="500" container="NULL"
                                    scrollHeight="200" value="${pod}" shouldMatch="true"
                                    callback="if(isLockPort()){calculateRates();setRelayOverRideYes();podDestination();setRelayDetails('relayOverride');${agentRefresh}}" />
                <cong:hidden name="portOfDestinationId" id="portOfDestinationId" value="${portOfDestinationId}"/>
                <cong:hidden name="podUnlocationcode" id="podUnlocationcode"  value="${podUnlocationcode}"/>
            </c:otherwise></c:choose>
    </cong:td>
    <cong:hidden name="polCode" id="polCode" value="${polCode}"/>
    <cong:hidden name="podCode" id="podCode" value="${podCode}"/>
</cong:tr>
<script type="text/javascript" >
    jQuery(document).ready(function () {
        var relay = $('input:radio[name=relayOverride]:checked').val();
        if (relay != undefined && relay == "Y") {
            $('#portOfLoading').removeClass().addClass("text");
            $('#portOfDestination').removeClass().addClass("text");
            $('#portOfLoading').removeAttr("readonly");
            $('#portOfDestination').removeAttr("readonly");

        }
        if (relay != undefined && relay == "N") {
            $('#portOfLoading').addClass("text-readonly");
            $('#portOfDestination').addClass("text-readonly");
            $('#portOfLoading').attr("readonly", true);
            $('#portOfDestination').attr("readonly", true);
        }
    });
</script>
