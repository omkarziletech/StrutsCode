<%@include file="/taglib.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<cong:tr>
    <cong:td>
        <c:choose>
            <c:when test="${lclBl.clientAcct.accountno!=null or lclBl.portOfOrigin==null}">
                <cong:div id="clientContainer">
                    <cong:autocompletor name="clientCompany" template="tradingPartner" id="client" 
                                        fields="client_no,acct_type,sub_type,NULL,NULL,NULL,NULL,NULL,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber"
                                        query="CLIENT_NO_CONSIGNEE" value="${lclBl.clientContact.companyName}" width="600px" container="NULL" shouldMatch="true" 
                                        styleClass="mandatory" scrollHeight="300px" callback="acctTypeCheck();"/>
                </cong:div>
                <cong:div id="clientConsContainer" style="display:none">
                    <cong:autocompletor name="clientCompany" template="tradingPartner" id="clientCons" 
                                        fields="client_no,acct_type,sub_type,NULL,NULL,NULL,NULL,NULL,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber"
                                        query="CLIENT_WITH_CONSIGNEE" value="${lclBl.clientContact.companyName}" width="600px" container="NULL" shouldMatch="true"
                                        styleClass="mandatory" callback="acctTypeCheck();showConsContainer();" scrollHeight="300px"/>
                </cong:div>
                <cong:div id="clientText" style="display:none">
                    <cong:text name="tempClientCompany" id="ManualClient"
                               value="${lclBlForm.tempClientCompany}" styleClass="mandatory" style="text-transform: uppercase" maxlength="50"/>
                </cong:div>
            </c:when>
            <c:otherwise>
                <cong:div id="clientContainer" style="display:none">
                    <cong:autocompletor name="clientCompany" template="tradingPartner" id="client" 
                                        fields="client_no,acct_type,sub_type,NULL,NULL,NULL,NULL,NULL,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber"
                                        query="CLIENT_NO_CONSIGNEE" value="${lclBl.clientContact.companyName}"  width="600px" container="NULL" shouldMatch="true"
                                        styleClass="mandatory" scrollHeight="300px" callback="acctTypeCheck();"/>
                </cong:div>
                <cong:div id="clientConsContainer" style="display:none">
                    <cong:autocompletor name="clientCompany" template="tradingPartner" id="clientCons" 
                                        fields="client_no,acct_type,sub_type,NULL,NULL,NULL,NULL,NULL,address,city,state,NULL,zip,contactName,phone,fax,email,commodityNumber"
                                        query="CLIENT_WITH_CONSIGNEE" value="${lclBl.clientContact.companyName}"  width="600px" container="NULL" shouldMatch="true"
                                        styleClass="mandatory" scrollHeight="300px" callback="acctTypeCheck();showConsContainer();"/>
                </cong:div>
                <cong:div id="clientText">
                    <cong:text name="tempClientCompany" id="ManualClient"
                               value="${lclBlForm.tempClientCompany}" styleClass="mandatory" style="text-transform: uppercase" maxlength="50"/>
                </cong:div>
            </c:otherwise>
        </c:choose>
    </cong:td>
</cong:tr>



