<%@ taglib prefix="c" uri="/WEB-INF/c-1_0-rt.tld" %>
<%@include file="/taglib.jsp"%>
<cong:table>
    <cong:tr>
        <cong:td styleClass="td">SCAC Code</cong:td>
        <cong:td><cong:text name="scacCode" id="scacCode" styleClass="text" value="${scac}" maxlength="4" style="text-transform:uppercase"/></cong:td>
    </cong:tr>
    <cong:tr>
        <cong:td styleClass="td">Pickup Charge</cong:td>
        <cong:td><cong:text name="chargeAmount" id="chargeAmount" value="${pickupCharge}" styleClass="floatLeft" container="NULL"/>
            <c:if test="${not empty lclBlAc && lclBlAc.manualEntry!=true}">
                <cong:div id="chargeImage" style="color: #EAE02F;font-size:30px">*</cong:div>
            </c:if>
        </cong:td>
    </cong:tr>
</cong:table>