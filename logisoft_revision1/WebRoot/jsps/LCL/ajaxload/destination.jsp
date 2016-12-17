<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:choose>
    <c:when test="${lclBookingForm.moduleName!='Imports' && error=='false'}">
        <c:set var="destUnLocQuery" value="CONCAT_RELAY_NAME_FD"/>
        <c:set var="destTempName" value="concatOrigin"/>
        <c:set var="methodName" value="fillLCLdefaultAgent('${lclBookingForm.moduleName}')"/>
        <c:set var="refreshAgent" value="submitAjaxForm('refreshAgent','#lclBookingForm','#m','')"/>
    </c:when>
    <c:when test="${lclBookingForm.moduleName!='Imports' && error=='true'}">
        <c:set var="destUnLocQuery" value="${query}"/>
        <c:set var="destTempName" value="concatOrigin"/>
        <c:set var="methodName" value="fillLCLdefaultAgent('${lclBookingForm.moduleName}')"/>
        <c:set var="refreshAgent" value="submitAjaxForm('refreshAgent','#lclBookingForm','#m','')"/>
    </c:when>
    <c:otherwise>
        <c:set var="destUnLocQuery" value="DEST_UNLOC"/>
        <c:set var="destTempName" value="one"/>
        <c:set var="methodName" value=""/>
        <c:set var="refreshAgent" value=""/>
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${query=='RELAYDESTINATION'}">
        <cong:td>
            <cong:autocompletor id="finalDestinationR" name="finalDestination" template="${destTempName}" fields="unlocationName,NULL,unlocationCode,finalDestinationId"
                                query="${destUnLocQuery}" paramFields="portOfOriginId" callback="submitAjaxForm1('lclRelayFind','#lclBookingForm','#polPod','','${lclBookingForm.moduleName}');${methodName};${refreshAgent};lockInsurance();"
                                styleClass="mandatory textlabelsBoldForTextBox"  width="350" container="NULL" value="${lclBookingForm.finalDestination}" shouldMatch="true"/>
        </cong:td>
    </c:when>
    <c:otherwise>
        <cong:td>
            <cong:autocompletor id="finalDestinationR" name="finalDestination" template="${destTempName}" fields="unlocationName,NULL,unlocationCode,finalDestinationId"
                                query="${destUnLocQuery}" callback="submitAjaxForm1('lclRelayFind','#lclBookingForm','#polPod','','${lclBookingForm.moduleName}');${methodName};${refreshAgent};lockInsurance();"
                                styleClass="mandatory textlabelsBoldForTextBox"  width="350" container="NULL" value="${lclBookingForm.finalDestination}" shouldMatch="true"/>
        </cong:td>
    </c:otherwise>
</c:choose>






