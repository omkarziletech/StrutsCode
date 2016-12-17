<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<cong:td styleClass="td textlabelsBold">Name</cong:td>
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
                        readOnly="${readonly}" container="NULL" fields="supplierCode,acctType,NULL,NULL,NULL,NULL,NULL,agentDisabled,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,agCommodityNo" params="${polUnlocationcode}" value="${lclQuote.supContact.companyName}" callback="checkErtAndRates('Agent');updateBussinessUnit();"/>
    <cong:img src="${path}/jsps/LCL/images/display.gif" style="vertical-align: middle" width="16" height="16" alt="display" styleClass="contactR" onclick="openLCLContactInfo('${path}','Supplier')"/>
    <input type="hidden" id="agentCount" name="agentCount" value="${agentFlag}"/>
    <input type="hidden" id="agCommodityNo" name="agCommodityNo"/>
    <input type="hidden" id="acctType" name="acctType"/>
    <input type="hidden" id="agentDisabled" name="agentDisabled"/>
    <input type="hidden" id="supplierCode" name="supplierCode"/>
</cong:td>

