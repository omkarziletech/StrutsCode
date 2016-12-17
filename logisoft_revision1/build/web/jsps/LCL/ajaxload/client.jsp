<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:choose>
    <c:when test="${lclBookingForm.moduleName=='Imports'}">
        <c:set var="mandField" value=""/>
        <c:set var="clientMethod" value="checkErtAndRates('Client');"/>
    </c:when>
    <c:otherwise>
        <c:set var="mandField" value="mandatory"/>
    </c:otherwise>
</c:choose>
<cong:tr>
    <cong:td>
        <c:choose>
            <c:when test="${lclBooking.clientAcct.accountno!=null or lclBooking.portOfOrigin==null}">
                <cong:div id="clientContainer">
                    <cong:autocompletor name="clientCompany" template="tradingPartner" paramFields="clientSearchState,clientSearchZip,clientSearchSalesCode,clientSearchCountryUnLocCode" id="client"
                                        fields="client_no,acct_type,sub_type,NULL,NULL,NULL,NULL,clientDisabled,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber,fmcNumber,otiNumber,NULL,NULL,retailCommodity,NULL,NULL,NULL,NULL,NULL,clientDisableAcct"
                                        query="CLIENT_NO_CONSIGNEE_IMP" value="${lclBooking.clientContact.companyName}" width="600" container="NULL" shouldMatch="true"
                                        styleClass="textlabelsBoldForTextBox" scrollHeight="300px" callback="acctTypeCheck();${clientMethod};"/>
                    <img alt="" src="${path}/images/icons/search_filter.png" style="vertical-align: middle;" class="clientSearchEdit" title="Click here to Client Search options" onclick="showClientSearchOption('${path}', 'Client')"/>
                </cong:div>
                <cong:div id="clientConsContainer" style="display:none">
                    <cong:autocompletor name="clientCompany" template="tradingPartner" id="clientCons" 
                                        fields="client_no,acct_type,sub_type,NULL,NULL,NULL,NULL,clientDisabled,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber,fmcNumber,otiNumber,NULL,NULL,retailCommodity,NULL,NULL,NULL,NULL,NULL,clientDisableAcct"
                                        query="CLIENT_WITH_CONSIGNEE_IMP" value="${lclBooking.clientContact.companyName}" width="600" container="NULL" shouldMatch="true"
                                        styleClass="textlabelsBoldForTextBox" callback="acctTypeCheck();showConsContainer();" scrollHeight="300px"/>
                </cong:div>
                <cong:div id="clientText" style="display:none">
                    <cong:text name="tempClientCompany" id="ManualClient"
                               value="${lclBookingForm.tempClientCompany}" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" maxlength="50"/>
                </cong:div>
            </c:when>
            <c:otherwise>
                <cong:div id="clientContainer" style="display:none">
                    <cong:autocompletor name="clientCompany" template="tradingPartner" id="client" 
                                        fields="client_no,acct_type,sub_type,NULL,NULL,NULL,NULL,clientDisabled,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber,fmcNumber,otiNumber,NULL,NULL,retailCommodity,NULL,NULL,NULL,NULL,NULL,clientDisableAcct"
                                        query="CLIENT_NO_CONSIGNEE_IMP" value="${lclBooking.clientContact.companyName}"  width="600" container="NULL" shouldMatch="true"
                                        styleClass="textlabelsBoldForTextBox" scrollHeight="300px" callback="acctTypeCheck();${clientMethod}"/>
                </cong:div>
                <cong:div id="clientConsContainer" style="display:none">
                    <cong:autocompletor name="clientCompany" template="tradingPartner" id="clientCons" 
                                        fields="client_no,acct_type,sub_type,NULL,NULL,NULL,NULL,clientDisabled,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber,fmcNumber,otiNumber,NULL,NULL,retailCommodity,NULL,NULL,NULL,NULL,NULL,clientDisableAcct"
                                        query="CLIENT_WITH_CONSIGNEE_IMP" value="${lclBooking.clientContact.companyName}"  width="600" container="NULL" shouldMatch="true"
                                        styleClass="textlabelsBoldForTextBox" scrollHeight="300px" callback="acctTypeCheck();showConsContainer();"/>
                </cong:div>
                <cong:div id="clientText">
                    <cong:text name="tempClientCompany" id="ManualClient"
                               value="${lclBookingForm.tempClientCompany}" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" maxlength="50"/>
                </cong:div>
            </c:otherwise>
        </c:choose>
        <input type="hidden" name="clientDisableAcct" id="clientDisableAcct"/>
        <input type="hidden" name="clientDisabled" id="clientDisabled"/>
        <input type="hidden" name="clientSearchState" id="clientSearchState"/>
        <input type="hidden" name="clientSearchZip" id="clientSearchZip"/>
        <input type="hidden" name="clientSearchSalesCode" id="clientSearchSalesCode"/>
        <input type="hidden" name="clientSearchCountry" id="clientSearchCountry"/>
        <input type="hidden" name="clientSearchCountryUnLocCode" id="clientSearchCountryUnLocCode"/>
        <input type="hidden" name="retailCommodity" id="retailCommodity"/>
        <input type="hidden" name="acct_type" id="acct_type"/>
    </cong:td>
</cong:tr>



