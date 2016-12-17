<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<cong:td styleClass="td textlabelsBoldforlcl">Agent ${lclQuoteForm.moduleName eq 'Imports'? "Name":""}</cong:td>
<cong:td>
    <c:choose>
        <c:when test="${lclQuoteImport.transShipment}">--
            <cong:autocompletor id="agentName" name="accountName" template="tradingPartner" width="600"
                                fields="agentNumber,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,agentBrand"
                                shouldMatch="true" query="TRADING_AGENT" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"
                                container="NULL" params="${lclQuoteForm.unlocationCode}" position="left"
                                value="${lclQuoteImport.exportAgentAcctNo.accountName}"  callback="checkEculineInQuote($('#agentNumber').val(), 'A')"/>
        </c:when>
        <c:otherwise>
            <cong:autocompletor id="agentName" name="accountName" template="tradingPartner" width="600" fields="agentNumber,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,agentBrand"
                                shouldMatch="true" query="TRADING_AGENT" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"
                                container="NULL" params="${podUnlocationcode}" position="left"
                                callback="checkEculineInQuote($('#agentNumber').val(), 'A')" value="${lclQuote.agentAcct.accountName}" />
        </c:otherwise>
    </c:choose>
    <input type="hidden" id="agentBrand" name="agentBrand" value="${lclQuoteForm.agentBrand}"/>
    <c:if test="${lclQuoteForm.moduleName ne 'Imports'}">
        <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook" style="width:75px" id="agentNumber" name="agentAcct" value="${lclQuote.agentAcct.accountno}" readOnly="true"/>
    </c:if>
</cong:td>
