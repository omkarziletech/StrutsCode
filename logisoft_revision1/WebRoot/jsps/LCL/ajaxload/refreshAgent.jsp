<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:choose>
    <c:when test="${lclBookingForm.moduleName ne 'Imports'}">
        <cong:tr>
            <cong:td styleClass="td textlabelsBold">Agent
                <c:choose>
                    <c:when test="${lclBooking.bookingType eq 'T'}">
                        <span title="${lclBookingImport.exportAgentAcctNo.accountName}" style="cursor: pointer">
                            <cong:autocompletor id="agentName" name="exportAgentAcctName" template="tradingPartner" fields="agentNumber" width="500" shouldMatch="true" position="left"  callback="updateAgentFromBkg();"
                                                query="TRADING_AGENT" styleClass="textlabelsBoldForTextBoxDisabledLook autoCompletortextBoxWidth" readOnly="true" params="${foreignunlocationCode}"
                                                container="NULL" value="${lclBookingImport.exportAgentAcctNo.accountName}"/></span>
                            <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook" style="width:75px" id="agentNumber" name="exportAgentAcctNo" value="${lclBookingImport.exportAgentAcctNo.accountno}" readOnly="true"/>
                        </c:when><c:otherwise>
                        <span title="${lclBooking.agentAcct.accountName}" style="cursor: pointer">
                            <cong:autocompletor id="agentName" name="accountName" template="tradingPartner" width="500" shouldMatch="true"
                                                query="TRADING_AGENT" styleClass="textlabelsBoldForTextBoxDisabledLook autoCompletortextBoxWidth" readOnly="true" callback="updateAgentFromBkg();checkEculine($('#agentNumber').val(), 'A');"
                                                container="NULL" params="${podUnlocationcode}" position="left" 

                                                fields="agentNumber,NULL,NULL,NULL,NULL,NULL,NULL,NULL,agentAddressConcat,agentCity,agentState,NULL,agentZip,NULL,agentPhone,agentFax,agentEmail,agentBrand" value="${lclBooking.agentAcct.accountName}"/></span>
                            <cong:text title="" styleClass="textlabelsBoldForTextBoxDisabledLook" style="width:75px" id="agentNumber" name="agentAcct" value="${lclBooking.agentAcct.accountno}" readOnly="true"/>
                        <input type="hidden" id="agentAddressConcat" name="agentAddressConcat" />
                        <input type="hidden" id="agentPhone" name="agentPhone" />
                        <input type="hidden" id="agentFax" name="agentFax" />
                        <input type="hidden" id="agentEmail" name="agentEmail"/>
                        <input type="hidden" id="agentCity" name="agentCity"/>
                        <input type="hidden" id="agentState" name="agentState"/>
                        <input type="hidden" id="agentZip" name="agentZip"/>
                        <input type="hidden" id="agentCompName" name="agentCompName"/>
                        <input type="hidden" id="agentBrand" name="agentBrand" value="${lclBookingForm.agentBrand}"/>
                    </c:otherwise>
                </c:choose>
            </cong:td>
        </cong:tr>
    </c:when>
    <c:otherwise>
        <cong:td styleClass="td textlabelsBold">  Agent Name</cong:td>
        <cong:td>
            <cong:autocompletor id="agentName" name="exportAgentAcctName" template="tradingPartner" width="600" fields="agentNumber,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL"
                                shouldMatch="true" query="TRADING_AGENT" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"
                                container="NULL" params="${foreignunlocationCode}" position="left" value="${lclBookingImport.exportAgentAcctNo.accountName}" />
        </cong:td>
    </c:otherwise>
</c:choose>
