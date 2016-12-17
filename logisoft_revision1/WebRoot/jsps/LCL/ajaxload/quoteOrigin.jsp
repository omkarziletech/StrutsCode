<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:choose>
    <c:when test="${lclQuoteForm.moduleName!='Imports' && error=='false'}">
        <c:set var="originUnLocQuery" value="RELAYNAME"/>
        <c:set var="tempName" value="concatOrigin"/>
        <c:set var="requiredField" value="mandatory"/>
    </c:when>
    <c:when test="${lclQuoteForm.moduleName!='Imports' && error=='true'}">
        <c:set var="originUnLocQuery" value="${originQuery}"/>
        <c:set var="tempName" value="concatOrigin"/>
        <c:set var="requiredField" value="mandatory"/>
    </c:when>
    <c:otherwise>
        <c:set var="originUnLocQuery" value="ORIGIN_UNLOC"/>
        <c:set var="tempName" value="one"/>
        <c:set var="requiredField" value=""/>
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${originQuery=='RELAYORIGIN'}">
        <cong:td>

            <cong:autocompletor id="portOfOriginR" name="portOfOrigin" callback="quoteDeliverCargo();submitAjaxFormforOrigin('lclRelayFind','#lclQuoteForm','#polPod','')"
                                template="${tempName}" fields="NULL,NULL,originUnlocationCode,portOfOriginId" query="${originUnLocQuery}" styleClass="${requiredField} textlabelsBoldForTextBox"
                                width="250" container="NULL" paramFields="finalDestinationId" value="${lclQuoteForm.portOfOrigin}" shouldMatch="true"/>
        </cong:td>
    </c:when>
    <c:otherwise>
        <cong:td>
            <cong:autocompletor id="portOfOriginR" name="portOfOrigin" callback="quoteDeliverCargo();submitAjaxFormforOrigin('lclRelayFind','#lclQuoteForm','#polPod','')"
                                template="${tempName}" fields="NULL,NULL,originUnlocationCode,portOfOriginId" query="${originUnLocQuery}" styleClass="${requiredField} textlabelsBoldForTextBox"
                                width="250" container="NULL" value="${lclQuoteForm.portOfOrigin}" shouldMatch="true"/>
        </cong:td>
    </c:otherwise>
</c:choose>





