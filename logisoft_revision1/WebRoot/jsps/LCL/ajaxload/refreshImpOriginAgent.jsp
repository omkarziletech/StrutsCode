<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<cong:td styleClass="td textlabelsBold">Name</cong:td>
<input type="hidden" id="agentFlagCountValue" value="${agentFlagCountValue}"/>
<c:choose>
    <c:when test="${agentFlag}">
        <c:set var="styleValue" value="textlabelsBoldForTextBox"/>
        <c:set var="readonly" value="false"/>
    </c:when>
    <c:otherwise>
        <c:set var="styleValue" value="text-readonly textlabelsBoldForTextBoxDisabledLook"/>
        <c:set var="readonly" value="true"/>
    </c:otherwise>
</c:choose>
<cong:td>
    <cong:autocompletor id="supplierNameOrg" name="supContact.companyName" template="tradingPartner" width="600" shouldMatch="true" query="IMPORTORIGINAGENT" styleClass="${styleValue} textCap"
                        readOnly="${readonly}"  container="NULL"
                        fields="supplierCode,acctType,NULL,NULL,NULL,NULL,NULL,agentDisabled,supplierAddress,supplierCity,supplierState,supplierCountry,supplierZip,NULL,supplierPhone,supplierFax,supplierEmail,NULL,NULL,NULL,supplierPoa,NULL,NULL,NULL,NULL,NULL,NULL,agCommodityNo"
                        params="${polUnlocationcode}" value="${lclBooking.supContact.companyName}" callback="checkErtAndRates('Agent');updateBussinessUnit();"/>
    <cong:img src="${path}/jsps/LCL/images/display.gif" width="16" height="16"  style="vertical-align: middle;"
              alt="display" styleClass="contactR" onclick="openLCLContactInfo('${path}','Supplier')"/>
    <input type="hidden" id="agentDisabled" name="agentDisabled"/>
    <input type="hidden" id="supplierAddress" name="supContact.address" value="${lclBooking.supContact.address}"/>
    <input type="hidden" id="supplierCity" name="supContact.city" value="${lclBooking.supContact.city}"/>
    <input type="hidden" id="supplierState" name="supContact.state" value="${lclBooking.supContact.state}"/>
    <input type="hidden" id="supplierZip" name="supContact.zip" value="${lclBooking.supContact.zip}"/>
    <input type="hidden" id="supplierCountry" name="supContact.country" value="${lclBooking.supContact.country}"/>
    <input type="hidden" id="supplierPhone" name="supContact.phone1" value="${lclBooking.supContact.phone1}"/>
    <input type="hidden" id="supplierFax" name="supContact.fax1" value="${lclBooking.supContact.fax1}"/>
    <input type="hidden" id="supplierEmail" name="supContact.email1" value="${lclBooking.supContact.email1}"/>
    <input type="hidden" id="agCommodityNo" name="agCommodityNo"/>
    <input type="hidden" id="acctType" name="acctType"/>
    <input type="hidden" id="supplierCode" name="supplierCode"/>
</cong:td>
