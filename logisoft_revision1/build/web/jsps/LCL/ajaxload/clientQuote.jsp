<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:if test="${lclQuoteForm.moduleName eq 'Imports'}">
    <c:set var="clientMethod" value="checkErtAndRates('Client');"/>
</c:if>
<cong:tr>
    <cong:td>
        <input type="hidden" name="importCommodity" id="importCommodity" value="${lclQuoteForm.importCommodity}"/>
        <cong:div id="clientContainer">
            <cong:autocompletor name="clientCompany" template="tradingPartner" paramFields="clientSearchState,clientSearchZip,clientSearchSalesCode,clientSearchCountryUnLocCode" id="client"
                                fields="client_no,acct_type,sub_type,NULL,NULL,NULL,NULL,clientDisabled,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber,fwdFmcNo,otiNumber,NULL,NULL,retailCmmodity,importCommodity,NULL,NULL,NULL,NULL,clientDisableAcct"
                                query="CLIENT_NO_CONSIGNEE_IMP" value="${lclQuote.clientContact.companyName}"  width="600" container="NULL"
                                shouldMatch="true" styleClass="textlabelsBoldForTextBox" callback="acctTypeCheck();notesCount('${path}','clntNotes',$('#client_no').val(),'CL');${clientMethod};" scrollHeight="300px"/>
        </cong:div>
        <cong:div id="clientConsContainer" style="display:none">
            <cong:autocompletor name="clientCompany" template="tradingPartner" id="clientCons"
                                fields="client_no,acct_type,sub_type,NULL,NULL,NULL,NULL,clientDisabled,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber,fwdFmcNo,otiNumber,NULL,NULL,NULL,importCommodity,NULL,NULL,NULL,NULL,clientDisableAcct"
                                query="CLIENT_WITH_CONSIGNEE_IMP" value="${lclQuote.clientContact.companyName}"  width="600" container="NULL"
                                shouldMatch="true" styleClass="textlabelsBoldForTextBox" callback="acctTypeCheck();showConsContainer();" scrollHeight="300px"/>
        </cong:div>
        <cong:div id="clientText" style="display:none">
            <cong:text name="tempClientCompany" id="ManualClient"
                       value="${lclQuoteForm.tempClientCompany}" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" maxlength="50"/>
        </cong:div>
        <input type="hidden" name="clientDisableAcct" id="clientDisableAcct"/>
        <input type="hidden" name="clientDisabled" id="clientDisabled"/>
        <input type="hidden" name="clientSearchState" id="clientSearchState"/>
        <input type="hidden" name="clientSearchZip" id="clientSearchZip"/>
        <input type="hidden" name="clientSearchSalesCode" id="clientSearchSalesCode"/>
        <input type="hidden" name="clientSearchCountry" id="clientSearchCountry"/>
        <input type="hidden" name="clientSearchCountryUnLocCode" id="clientSearchCountryUnLocCode"/>
        <input type="hidden" name="retailCmmodity" id="retailCmmodity"/>
        <input type="hidden" name="acct_type" id="acct_type"/>
        <input type="hidden" name="sub_type" id="sub_type"/>
    </cong:td>
</cong:tr>



