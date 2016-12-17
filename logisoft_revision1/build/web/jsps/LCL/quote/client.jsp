<table width="100%" border="0">
    <cong:tr>
        <cong:td styleClass="caption" style="width:1%"></cong:td>
        <cong:td styleClass="caption" style="width:1%"></cong:td>
        <cong:td styleClass="caption" style="width:4%">New</cong:td>
        <cong:td styleClass="caption" style="width:1%">&nbsp;</cong:td>
        <cong:td styleClass="caption" style="width:6%">Acct No</cong:td>
        <cong:td styleClass="caption" style="width:1%">&nbsp;</cong:td>
        <cong:td styleClass="caption" style="width:2%">Booking Contact Name</cong:td>
        <cong:td styleClass="caption" style="width:2%">New</cong:td>
        <cong:td styleClass="caption" style="width:1%">&nbsp;</cong:td>
        <cong:td styleClass="caption" style="width:6%">Booking Email</cong:td>
        <cong:td styleClass="caption" style="width:6%">Fax</cong:td>
        <cong:td styleClass="caption" style="width:6%">Phone</cong:td>
        <cong:td styleClass="caption" style="width:6%">Client Ref</cong:td>
        <cong:td styleClass="caption" style="width:1%">POA&nbsp;&nbsp;</cong:td>
        <cong:td styleClass="caption" style="width:5%">Credit Status</cong:td>
        <cong:td styleClass="caption" style="width:4%">&nbsp;</cong:td>
        <cong:td styleClass="caption" style="width:4%">&nbsp;</cong:td>
    </cong:tr>
    <cong:tr>
        <cong:td styleClass="textlabelsBoldforlcl">Client</cong:td>
        <cong:td>
            <cong:div id="clientContainer">
                <cong:autocompletor name="clientCompany" template="tradingPartner" id="client"
                                    fields="client_no,acct_type,sub_type,clientBsEciAcctNo,clientBsEciFwNo,NULL,NULL,clientDisabled,address,city,state,NULL,zip,NULL,phone,fax,NULL,fmcNo,otiNumber,clientpoa,creditForClient,NULL,clientDisableAcct,clientColoadNo,clientColoadDesc,clientRetailNo,clientRetailDesc,clientBrand"
                                    query="EXP_CLIENT_NO_CONSIGNEE" value="${lclQuote.clientContact.companyName}"  width="600" container="NULL"
                                    shouldMatch="true" styleClass="textlabelsBoldForTextBox" paramFields="clientSearchState,clientSearchZip,clientSearchSalesCode,clientSearchCountryUnLocCode" 
                                    callback="acctTypeCheck();clientDetails();displayClientDetails();notesCount('${path}','clntNotes',$('#client_no').val(),$('#acct_type').val());checkEculineInQuote($('#client_no').val(),'CLN');" scrollHeight="300px"/>
            </cong:div>
            <cong:div id="clientConsContainer" style="display:none">
                <cong:autocompletor name="clientCompany" template="tradingPartner" id="clientCons"
                                    fields="client_no,acct_type,sub_type,clientBsEciAcctNo,clientBsEciFwNo,NULL,NULL,clientDisabled,address,city,state,NULL,zip,NULL,phone,fax,NULL,fmcNo,otiNumber,clientpoa,creditForClient,NULL,clientDisableAcct,clientColoadNo,clientColoadDesc,clientRetailNo,clientRetailDesc,clientBrand"
                                    query="EXP_CLIENT_WITH_CONSIGNEE" value="${lclQuote.clientContact.companyName}"  width="600" container="NULL"
                                    shouldMatch="true" styleClass="textlabelsBoldForTextBox" paramFields="clientSearchState,clientSearchZip,clientSearchSalesCode,clientSearchCountryUnLocCode" 
                                    callback="acctTypeCheck();clientDetails();showConsContainer();displayClientDetails();notesCount('${path}','clntNotes',$('#client_no').val(),$('#acct_type').val());checkEculineInQuote($('#client_no').val(),'CLN');" scrollHeight="300px"/>
           </cong:div>
            <cong:div id="clientText" style="display:none">
                <cong:text name="tempClientCompany" id="ManualClient" value="${lclQuoteForm.tempClientCompany}"
                           styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" maxlength="50"/>
            </cong:div>
            <cong:checkbox name="clientWithoutConsignee" id="clientManual" onclick="newClient();"/>
            <cong:checkbox name="clientWithConsignee" id="clientWithConsignee" container="NULL"
                           onclick="showConsigneeForClient();clientSearch();"
                           title="Checked=Consignee Listed<br/>unChecked=consignee not Listed"/>
            <td>
                <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                     title="Click here to edit Client Search options"onclick="showClientSearchOption('${path}', 'Client')"/>

            <c:choose>
                <c:when test="${lclQuoteForm.clientIcon}">
                    <img src="${path}/img/icons/e_contents_view1.gif" width="12" height="12" title="Click here to see Client Notes"
                         id="clntNotes" onclick="displayNotes('${path}', $('#client_no').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/e_contents_view.gif" width="12" height="12" title="Click here to see Client Notes"
                         id="clntNotes" onclick="displayNotes('${path}', $('#client_no').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                </c:otherwise>
            </c:choose>
            <c:if test="${empty lclQuote.lclFileNumber}">
                <img src="${path}/img/icons/copy.gif" alt="Look Up" width="16" height="16"
                     id="replicateClient" onclick="openClient('${path}', $('#client').val(), $('#client_no').val());"/>
            </c:if>
            </td>
            <input type="hidden" id="clientColoadNo" value="${lclQuote.clientAcct.generalInfo.genericCode.code}"/>
            <input type="hidden" id="clientColoadDesc" value="${lclQuote.clientAcct.generalInfo.genericCode.codedesc}"/>
            <input type="hidden" id="clientRetailNo" value="${lclQuote.clientAcct.generalInfo.retailCommodity.code}"/>
            <input type="hidden" id="clientRetailDesc" value="${lclQuote.clientAcct.generalInfo.retailCommodity.codedesc}"/>
            <input type="hidden" name="bookingContact" id="bookingContact"/>
            <input type="hidden" name="bookingContactEmail" id="bookingContactEmail"/>
            <input type="hidden" name="path" id="path" value="${path}"/>
            <input type="hidden" name="acctType" id="acct_type"/>
            <input type="hidden" name="subType" id="sub_type"/>
            <cong:hidden name="clientContact.address" id="address" value="${lclQuote.clientContact.address}"/>
            <cong:hidden name="clientContact.city" id="city"  value="${lclQuote.clientContact.city}"/>
            <cong:hidden name="clientContact.state" id="state" value="${lclQuote.clientContact.state}"/>
            <cong:hidden name="clientContact.zip" id="zip" value="${lclQuote.clientContact.zip}"/>
            <cong:hidden name="clientContact.country" id="country" value="${lclQuote.clientContact.country}"/>
            <input type="hidden" name="clientPoaClient" id="clientpoa" value="${lclQuote.clientAcct.generalInfo.poa}"/>
            <input type="hidden" id="creditForClient" name="creditForClient" value="${CreditForClient}"/>
            <input type="hidden" name="clientDisableAcct" id="clientDisableAcct"/>
            <input type="hidden" name="clientDisabled" id="clientDisabled"/>
            <input type="hidden" name="fmcNo" id="fmcNo" value="${lclQuote.clientAcct.generalInfo.fwFmcNo}"/>
            <cong:hidden name="otiNumber" id="otiNumber" value="${lclQuote.clientAcct.generalInfo.nvoOtiLicenseNo}"/>
            <input type="hidden" id="clientBsEciAcctNo" value="${lclQuote.clientAcct.eciAccountNo}"/>
            <input type="hidden" id="clientBsEciFwNo" value="${lclQuote.clientAcct.ECIFWNO}"/>

            <input type="hidden" name="clientSearchState" id="clientSearchState"/>
            <input type="hidden" name="clientSearchZip" id="clientSearchZip"/>
            <input type="hidden" name="clientSearchSalesCode" id="clientSearchSalesCode"/>
            <input type="hidden" name="clientSearchCountryUnLocCode" id="clientSearchCountryUnLocCode"/>
            <input type="hidden" name="clientSearchCountry" id="clientSearchCountry"/>
            <input type="hidden" name="searchClientBy" id="searchClientBy"/>
            <input type="hidden" id="clientBrand" name="clientBrand" value="${lclQuote.clientAcct.brandPreference}"/>
        </cong:td>
        <cong:td >
            <cong:text name="clientAcct" id="client_no" value="${lclQuote.clientAcct.accountno}" container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" style="width:70px"/>
        </cong:td>
        <cong:td>
            <span class="cons">
                <img src="${path}/img/icons/iicon.png" width="12" height="12" alt="Client" id="clientT"/>
            </span>
        </cong:td>
        <cong:td>
            <cong:div id="clientContactDojo">
                <cong:autocompletor  styleClass="textLCLuppercase textlabelsBoldForTextBox" paramFields="client_no" fields="email,dupFax,dupPhone"  container="NULL" scrollHeight="200px"
                                     id="contactName" shouldMatch="true" query="CONTACTFORACCT" template="four" width="450" value="${lclQuote.clientContact.contactName}"
                                     name="clientContact.contactName" callback="verifyContactDetails('client')"/>
            </cong:div>
            <input type="hidden" name="dupFax" id="dupFax" value="${lclQuote.clientAcct.custContact.fax}"/>
            <input type="hidden" name="dupPhone" id="dupPhone" value="${lclQuote.clientAcct.custContact.phone}"/>
            <cong:div id="clientContactManual" style="display:none">
                <cong:text id="clientContactManul" name="clientContactManual" style="text-transform: uppercase" onkeyup="verifyContactDetails('client')" value="${lclQuote.clientContact.contactName}" styleClass="textlabelsBoldForTextBox" maxlength="250"/>
            </cong:div>
        </cong:td>
        <cong:td>
            <cong:checkbox id="newClientContact" name="newClientContact" onclick="newClientContactName();" container="NULL"/>
        </cong:td>
        <cong:td>
            <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="contactR" onclick="openContact('${path}', $('#client').val(), $('#client_no').val(), $('#contactName').val(), 'C')"/>
        </cong:td>
        <cong:td>
            <cong:text name="clientContact.email1" id="email" styleClass="textlabelsBoldForTextBox" container="NULL" value="${lclQuote.clientContact.email1}" maxlength="100" style="width:160px"/>
        </cong:td>
        <cong:td>
            <cong:text name="clientContact.fax1" id="fax" value="${lclQuote.clientContact.fax1}" style="width:80px"  container="NULL" maxlength="50" styleClass="textlabelsBoldForTextBox"/>
        </cong:td>
        <cong:td>
            <cong:text name="clientContact.phone1" id="phone" style="width:80px"  container="NULL" styleClass="textlabelsBoldForTextBox" value="${lclQuote.clientContact.phone1}" maxlength="50"/>
        </cong:td>
        <cong:td>
            
        </cong:td>
        <cong:td>
            <label id="clientPoa" style="font-weight: bold"/>
        </cong:td>
        <cong:td>
            <label id="clientCreditClient" style="font-weight: bold"/>
        </cong:td>
        <cong:td valign="middle" styleClass="textlabelsBoldforlcl">  Default Agent</cong:td>
        <cong:td styleClass="textBoldforlcl">
            <cong:radio value="Y" name="defaultAgent"  container="NULL" onclick="lclQuotedefaultAgent()"/> Yes
            <cong:radio value="N" name="defaultAgent" container="NULL" onclick="lclQuoteDefaultValues()"/> No
        </cong:td>
    </cong:tr>
    <cong:tr>
        <cong:td styleClass="textlabelsBoldforlcl">Shipper</cong:td>
        <cong:td>
            <span id="dojoShipp">
                <cong:autocompletor name="shipContact.companyName" template="tradingPartner" id="shipperNameClient"
                                    fields="shipperCodeClient,shipper_acct_type,shipper_sub_type,shipBsEciAcctNo,shipBsEciFwNo,NULL,shipperSalesPersonCode,shipperDisabled,shipperAddressClient,shipperCityClient,shipperStateClient,shipperCountryClient,shipperZipClient,NULL,shipperPhoneClient,shipperFaxClient,NULL,sFmc,sotiNumber,shipperPoa,shipperCredit,NULL,shipDisableAcct,shipColoadNo,shipColoadDesc,shipRetailNo,shipRetailDesc,null,shipperBrand"
                                    styleClass="textlabelsBoldForTextBox textLCLuppercase" query="SHIPPER" paramFields="shipperSearchState,shipperSearchZip,shipperSearchSalesCode,CountryUnLocCode"  
                                    width="600" container="NULL" shouldMatch="true" value="${lclQuote.shipContact.companyName}"
                                    callback="shipperAcctType();changeShipperDetailsToParties();checkEculineInQuote($('#shipperCodeClient').val(),'S');" scrollHeight="300px"/>
            </span>
            <span id="manualShipp" style="display:none">
                <cong:text name="shipContactName" id="dupShipName" styleClass="textlabelsBoldForTextBox"
                           style="text-transform: uppercase" value="${lclQuoteForm.shipContactName}" onchange="newShippChange()"/>
            </span>
            <input type="hidden" name="shipperAddressClient" id="shipperAddressClient" value="${lclQuote.shipContact.address}"/>
            <input type="hidden" name="shipperStateClient" value="${lclQuote.shipContact.state}" id="shipperStateClient"/>
            <input type="hidden" name="shipperCityClient" value="${lclQuote.shipContact.city}" id="shipperCityClient"/>
            <input type="hidden" name="shipperCountryClient" value="${lclQuote.shipContact.country}" id="shipperCountryClient"/>
            <input type="hidden" name="shipperZipClient" value="${lclQuote.shipContact.zip}" id="shipperZipClient"/>
            <input type="hidden" name="shipperEmailClients" value="${lclQuote.shipContact.zip}" id="shipperEmailClients"/>
            <input type="hidden" name="sFmc" value="${lclQuote.shipAcct.generalInfo.fwFmcNo}" id="sFmc"/>
            <input type="hidden" name="sotiNumber" id="sotiNumber" value="${lclQuote.shipAcct.generalInfo.nvoOtiLicenseNo}"/>
            <input type="hidden" name="shipperPoa" id="shipperPoa" value="${lclQuote.shipAcct.generalInfo.poa}"/>
            <input type="hidden" name="shipperCredit" id="shipperCredit" value="${CreditForShipper}"/>
            <input type="hidden" id="shipColoadNo" value="${lclQuote.shipAcct.generalInfo.genericCode.code}"/>
            <input type="hidden" id="shipColoadDesc" value="${lclQuote.shipAcct.generalInfo.genericCode.codedesc}"/>
            <input type="hidden" id="shipRetailNo" value="${lclQuote.shipAcct.generalInfo.retailCommodity.code}"/>
            <input type="hidden" id="shipRetailDesc" value="${lclQuote.shipAcct.generalInfo.retailCommodity.codedesc}"/>
            <input type="hidden" id="shipBsEciAcctNo" value="${lclQuote.shipAcct.eciAccountNo}"/>
            <input type="hidden" id="shipBsEciFwNo" value="${lclQuote.shipAcct.ECIFWNO}"/>
            <input type="hidden" name="shipperSearchState" id="shipperSearchState"/>
            <input type="hidden" name="shipperSearchZip" id="shipperSearchZip"/>
            <input type="hidden" name="shipperSearchSalesCode" id="shipperSearchSalesCode"/>
            <input type="hidden" name="shipperSearchCountry" id="shipperSearchCountry"/>
            <input type="hidden" name="CountryUnLocCode" id="CountryUnLocCode"/>
            <input type="hidden" name="shipperBrand" id="shipperBrand" value="${lclQuote.shipAcct.brandPreference}"/>

        </cong:td>
        <cong:td> 
            <cong:checkbox id="newShipp" name="newShipper" onclick="newShippName();" container="NULL"/>
            <td>
                <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                     title="Click here to edit Shipper Search options" onclick="showClientSearchOption('${path}', 'Shipper')"/>
            <c:choose>
                <c:when test="${lclQuoteForm.shipperIcon}">
                    <img src="${path}/img/icons/e_contents_view1.gif" width="12" height="12" title="Click here to see Shipper Notes"
                         id="shpNotes" onclick="displayNotes('${path}', $('#shipperCodeClient').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/e_contents_view.gif" width="12" height="12" title="Click here to see Shipper Notes"
                         id="shpNotes" onclick="displayNotes('${path}', $('#shipperCodeClient').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                </c:otherwise>
            </c:choose>
            </td>
        </cong:td>
        <cong:td>
            <cong:text name="shipAcct" id="shipperCodeClient" value="${lclQuote.shipAcct.accountno}" container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" style="width:70px"/>
        </cong:td>
        <cong:td>
            <span class="cons">
                <img src="${path}/img/icons/iicon.png" width="12" height="12" alt="shipper" id="shipp"/>
            </span>
        </cong:td>
        <cong:td>
            <cong:div id="shipperDojo">
                <cong:autocompletor id="shipperContactClient" fields="shipperEmailClient,dupShipFax,dupShipPhone" paramFields="shipperCodeClient" value="${lclQuote.shipContact.contactName}" scrollHeight="200px" shouldMatch="true" query="CONTACTFORACCT" template="four" width="450"
                                    styleClass="textlabelsBoldForTextBox textLCLuppercase" name="shipContact.contactName" container="NULL" callback="verifyContactDetails('shipper')"/>
            </cong:div>
            <cong:div id="shippManual" style="display:none">
                <cong:text id="shipperManualContact" name="shipperManualContact" container="NULL" style="text-transform: uppercase" onkeyup="verifyContactDetails('shipper')" value="${lclQuote.shipContact.contactName}" styleClass="textlabelsBoldForTextBox"/>
            </cong:div>
        </cong:td>
        <input type="hidden" name="dupShipPhone" id="dupShipPhone" value="${lclQuote.shipAcct.custContact.phone}" />
        <input type="hidden" name="dupShipFax" id="dupShipFax" value="${lclQuote.shipAcct.custContact.fax}"/>
        <%--<cong:hidden name="shipperPoa" id="shipperPoaClient" value="${lclQuote.shipAcct.generalInfo.poa}"/>--%>
        <cong:td>
            <cong:checkbox id="newShipperContact" name="newShipperContact" onclick="newShipperContactName();" container="NULL"/>
        </cong:td>
        <cong:td>
            <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="shipcontact" onclick="openShipContact('${path}', $('#shipperNameClient').val(), $('#shipperCodeClient').val(), $('#shipperContactClient').val(), 'S')"/>
        </cong:td>
        <cong:td>
            <cong:text name="shipContact.email1" id="shipperEmailClient" container="NULL" value="${lclQuote.shipContact.email1}" maxlength="100" styleClass="text textlabelsBoldForTextBox" style="width:160px"/>
        </cong:td>
        <cong:td>
            <cong:text name="shipContact.fax1" id="shipperFaxClient" style="width:80px" 
                       container="NULL" maxlength="50" styleClass="textlabelsBoldForTextBox" value="${lclQuote.shipContact.fax1}"/>
        </cong:td>
        <cong:td>
            <cong:text name="shipContact.phone1" id="shipperPhoneClient" style="width:80px"  container="NULL"
                       maxlength="50" styleClass="textlabelsBoldForTextBox" value="${lclQuote.shipContact.phone1}"/>
        </cong:td>
         <cong:td>
          <input type="text" class="text textlabelsBoldForTextBox textCap" value="${lclQuote.shipReference}" tabindex="-1" id="dupShipref"  name="shipReference" maxlength="100" onchange="ShipReference()"/>
        </cong:td>
        <cong:td>
            <label id="shipperPoaClientId" style="font-weight: bold"/>
        </cong:td>
        <cong:td>
            <cong:label id="shipperCreditClient" style="font-weight: bold"/>
        </cong:td>
        <cong:td colspan="2">
            <cong:table id="m">
                <jsp:include page="/jsps/LCL/ajaxload/refreshQuoteAgentName.jsp"/></cong:table></cong:td>
    </cong:tr>
    <cong:tr>
        <cong:td styleClass="textlabelsBoldforlcl">Forwarder</cong:td>
        <cong:td>
            <cong:autocompletor name="fwdContact.companyName" template="tradingPartner" id="forwarderNameClient"
                                fields="forwarderCodeClient,forwarder_acct_type,forwarder_sub_type,fwdBsEciAcctNo,fwdBsEciFwNo,NULL,NULL,forwardDisabled,forwarderAddressClient,forwarderCityClient,forwarderStateClient,forwarderCountryClient,forwarderZipClient,NULL,forwarderPhoneClient,forwarderFaxClient,NULL,fFmc,fotiNumber,forwarderPoa,forwarderCredit,NULL,forwardDisabledAcct,fwdColoadNo,fwdColoadDesc,fwdRetailNo,fwdRetailDesc,fwdBrand"
                                query="FORWARDER"  width="600" container="NULL" value="${lclQuote.fwdContact.companyName}" styleClass="textlabelsBoldForTextBox textLCLuppercase"
                                shouldMatch="true" paramFields="frwdSearchState,frwdSearchZip,frwdSearchSalesCode,frwdSearchCountryUnLocCode"
                                callback="forwarder_AccttypeCheck();copyForwarderToParties();checkEculineInQuote($('#forwarderCodeClient').val(),'F');" scrollHeight="300px"/>
            <input type="hidden" name="forwarderaccttype" id="forwarder_acct_type"/>
            <input type="hidden" name="forwardersubtype" id="forwarder_sub_type"/>
            <input type="hidden" name="forwardDisabled" id="forwardDisabled"/>
            <input type="hidden" name="forwardDisabledAcct" id="forwardDisabledAcct"/>
            <input type="hidden" id="forwarderCredit" name="forwarderCredit" value="${CreditForForwarder}"/>
            <input type="hidden" name="forwarderPoa" id="forwarderPoa" value="${lclQuote.fwdAcct.generalInfo.poa}"/>
            <input type="hidden" name="forwarderCityClient" id="forwarderCityClient" value="${lclQuote.fwdContact.city}"/>
            <input type="hidden" name="forwarderCountryClient" id="forwarderCountryClient" value="${lclQuote.fwdContact.country}"/>
            <input type="hidden" name="forwarderStateClient" id="forwarderStateClient" value="${lclQuote.fwdContact.state}"/>
            <input type="hidden" name="forwarderZipClient" id="forwarderZipClient" value="${lclQuote.fwdContact.zip}"/>
            <input type="hidden" name="forwarderAddressClient" id="forwarderAddressClient" value="${lclQuote.fwdContact.address}"/>
            <input type="hidden" name="fFmc" id="fFmc" value="${lclQuote.fwdAcct.generalInfo.fwFmcNo}"/>
            <input type="hidden" name="fotiNumber" id="fotiNumber" value="${lclQuote.fwdAcct.generalInfo.nvoOtiLicenseNo}"/>
            <input type="hidden" name="frwdSearchState" id="frwdSearchState"/>
            <input type="hidden" name="frwdSearchZip" id="frwdSearchZip"/>
            <input type="hidden" name="frwdSearchSalesCode" id="frwdSearchSalesCode"/>
            <input type="hidden" name="frwdSearchCountryUnLocCode" id="frwdSearchCountryUnLocCode"/>
            <input type="hidden" name="frwdSearchCountry" id="frwdSearchCountry"/>
            <input type="hidden" id="fwdColoadNo" value="${lclQuote.fwdAcct.generalInfo.genericCode.code}"/>
            <input type="hidden" id="fwdColoadDesc" value="${lclQuote.fwdAcct.generalInfo.genericCode.codedesc}"/>
            <input type="hidden" id="fwdRetailNo" value="${lclQuote.fwdAcct.generalInfo.retailCommodity.code}"/>
            <input type="hidden" id="fwdRetailDesc" value="${lclQuote.fwdAcct.generalInfo.retailCommodity.codedesc}"/>
            <input type="hidden" id="fwdBsEciAcctNo" value="${lclQuote.fwdAcct.eciAccountNo}"/>
            <input type="hidden" id="fwdBsEciFwNo" value="${lclQuote.fwdAcct.ECIFWNO}"/>
            <input type="hidden" name="fwdBrand" id="fwdBrand" value="${lclQuote.fwdAcct.brandPreference}"/>
        </cong:td>
        <cong:td>
            <td>
                <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                     title="Click here to edit Forwarder Search options" onclick="showClientSearchOption('${path}', 'Forwarder')"/>
            <c:choose>
                <c:when test="${lclQuoteForm.forwaderIcon}">
                    <img src="${path}/img/icons/e_contents_view1.gif" width="12" height="12" title="Click here to see Forwarder Notes"
                         id="fwdNotes" onclick="displayNotes('${path}', $('#forwarderCodeClient').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/e_contents_view.gif" width="12" height="12" title="Click here to see Forwarder Notes"
                         id="fwdNotes" onclick="displayNotes('${path}', $('#forwarderCodeClient').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                </c:otherwise>
            </c:choose>
            </td>
        </cong:td>
        <cong:td>
            <cong:text name="fwdAcct" id="forwarderCodeClient" value="${lclQuote.fwdAcct.accountno}"
                       container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" style="width:70px"/>
        </cong:td>
        <cong:td>
            <span class="cons">
                <img src="${path}/img/icons/iicon.png" width="12" height="12" alt="shipper" id="Fwd"/>
            </span>
        </cong:td>
        <cong:td>
            <cong:div id="forwarederDojo">
                <cong:autocompletor id="forwardercontactClient" container="NULL" scrollHeight="200px" query="CONTACTFORACCT" 
                                    fields="forwarderEmailClient,dupFwdFax,dupFwdPhone" paramFields="forwarderCodeClient"
                                    template="four" shouldMatch="true" value="${lclQuote.fwdContact.contactName}" callback="verifyContactDetails('frwd')"
                                    styleClass="textlabelsBoldForTextBox textLCLuppercase" name="fwdContact.contactName" width="450" />
            </cong:div>
            <cong:div id="forwarederManual" style="display:none">
                <cong:text name="forwarederContactManual" id="forwarederContactManual" 
                           style="text-transform: uppercase" onkeyup="verifyContactDetails('frwd')"
                           value="${lclQuote.fwdContact.contactName}" styleClass="textlabelsBoldForTextBox" maxlength="250"/>
            </cong:div>
        </cong:td>
        <input type="hidden" name="dupFwdPhone" id="dupFwdPhone" value="${lclQuote.fwdAcct.custContact.phone}"/>
        <input type="hidden" name="dupFwdFax" id="dupFwdFax" value="${lclQuote.fwdAcct.custContact.fax}"/>
        <cong:td>
            <cong:checkbox id="newForwarderContact" name="newForwarderContact"
                           onclick="newForwarderContactName();" container="NULL"/>
        </cong:td>
        <cong:td>
            <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16"
                 id="fwdcontact" onclick="openQtFwdContact('${path}', $('#forwarderNameClient').val(), $('#forwarderCodeClient').val(), $('#forwardercontactClient').val(), 'F')"/>
        </cong:td>
        <cong:td>
            <cong:text name="fwdContact.email1" id="forwarderEmailClient" container="NULL" value="${lclQuote.fwdContact.email1}" maxlength="100" styleClass="text textlabelsBoldForTextBox" style="width:160px"/>
        </cong:td>
        <cong:td>
            <cong:text name="fwdContact.fax1" id="forwarderFaxClient" style="width:80px"  container="NULL" maxlength="19" styleClass="textlabelsBoldForTextBox" value="${lclQuote.fwdContact.fax1}"/>
        </cong:td>
        <cong:td>
            <cong:text name="fwdContact.phone1" id="forwarderPhoneClient" style="width:80px"  container="NULL" maxlength="19" styleClass="textlabelsBoldForTextBox" value="${lclQuote.fwdContact.phone1}"/>
        </cong:td>
        <cong:td>
                    <input type="text" class="text textlabelsBoldForTextBox textCap" value="${lclQuote.fwdReference}" tabindex="-1" id="forwarderClientReff" name="fwdReference"  maxlength="100" onchange="clientRefForwarder()"/>
        </cong:td>
        <cong:td>
            <span id="forwarderpoaClient" style="font-weight: bold"></span>
        </cong:td>
        <cong:td>
            <label id="forwarderCreditClient" style="font-weight: bold"/>
        </cong:td>
        <cong:td styleClass="textlabelsBoldforlcl">ERT Y/N</cong:td>
        <cong:td colspan="3">
            <html:select property="rtdTransaction" styleId="rtdTransaction" value="${lclQuoteForm.rtdTransaction}" styleClass="smallDropDown mandatory textlabelsBoldForTextBox" onchange="copyValuesofAgent()">
                <html:option value="">Select</html:option>
                <html:option value="Y">Yes</html:option>
                <html:option value="N">No</html:option>
            </html:select>
        </cong:td>
    </cong:tr>
    <cong:tr>
        <cong:td styleClass="textlabelsBoldforlcl">Consignee</cong:td>
        <cong:td>
            <span id="dojoConsign">
                <cong:autocompletor name="consContact.companyName" template="tradingPartner" id="consigneeNameClient"
                                    fields="consigneeCodeClient,consignee_acct_type,consignee_sub_type,consBsEciAcctNo,consBsEciFwNo,NULL,NULL,consDisabled,consigneeAddressClient,consigneeCityClient,consigneeStateClient,consigneeCountryClient,consigneeZipClient,NULL,consigneePhoneClient,consigneeFaxClient,NULL,cFmc,cotiNumber,consigneePoa,consigneeCredit,NULL,NULL,consDisableAcct,consColoadNo,consColoadDesc,consRetailNo,consRetailDesc"
                                    query="EXP_CONSIGNEE"  width="600" container="NULL" value="${lclQuote.consContact.companyName}" styleClass="textlabelsBoldForTextBox textLCLuppercase"
                                    shouldMatch="true" paramFields="consigneeSearchState,consigneeSearchZip,consigneeSearchSalesCode,consigneeUnLocCode" 
                                    callback="consigneeAccttype();copyConsigneeToParties();checkEculineInQuote($('#consigneeCodeClient').val(),'C');" scrollHeight="300px"/>
            </span>
            <span id="manualConsign" style="display:none">
                <cong:text name="consContactName" styleClass="textlabelsBoldForTextBox" 
                           style="text-transform: uppercase" id="dupConsName" value="${lclQuoteForm.consContactName}"
                           onchange="newConsignChange()"/>
            </span>
            <cong:hidden name="consContact.address" value="${lclQuote.consContact.address}" id="consigneeAddressClient"/>
            <cong:hidden name="consContact.city" value="${lclQuote.consContact.city}" id="consigneeCityClient"/>
            <cong:hidden name="consContact.country" value="${lclQuote.consContact.country}" id="consigneeCountryClient"/>
            <cong:hidden name="consContact.state" value="${lclQuote.consContact.state}" id="consigneeStateClient"/>
            <cong:hidden name="consContact.zip" value="${lclQuote.consContact.zip}" id="consigneeZipClient"/>
            <input type="hidden" name="cFmc" id="cFmc" value="${lclQuote.consAcct.generalInfo.fwFmcNo}"/>
            <input type="hidden" name="cotiNumber" id="cotiNumber" value="${lclQuote.consAcct.generalInfo.nvoOtiLicenseNo}"/>
            <input type="hidden" id="consColoadNo" value="${lclQuote.consAcct.generalInfo.consColoadCommodity.code}"/>
            <input type="hidden" id="consColoadDesc" value="${lclQuote.consAcct.generalInfo.consColoadCommodity.codedesc}"/>
            <input type="hidden" id="consRetailNo" value="${lclQuote.consAcct.generalInfo.consRetailCommodity.code}"/>
            <input type="hidden" id="consRetailDesc" value="${lclQuote.consAcct.generalInfo.consRetailCommodity.codedesc}"/>
            <input type="hidden" id="consBsEciAcctNo" value="${lclQuote.consAcct.eciAccountNo}"/>
            <input type="hidden" id="consBsEciFwNo" value="${lclQuote.consAcct.ECIFWNO}"/>
            <input type="hidden" name="consDisabled" id="consDisabled"/>
            <input type="hidden" name="consDisableAcct" id="consDisableAcct"/>
        </cong:td>
        <cong:td>
            <cong:checkbox id="newConsign" name="newConsignee" onclick="newConsigneeNameByClient();" container="NULL"/>
            <td>
                <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                     title="Click here to edit Consignee Search options" onclick="showClientSearchOption('${path}', 'Consignee')"/>
            <c:choose>
                <c:when test="${lclQuoteForm.consigneeIcon}">
                    <img src="${path}/img/icons/e_contents_view1.gif" width="12" height="12" title="Click here to see Consignee Notes"
                         id="conNotes" onclick="displayNotes('${path}', $('#consigneeCodeClient').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/e_contents_view.gif" width="12" height="12" title="Click here to see Consignee Notes"
                         id="conNotes" onclick="displayNotes('${path}', $('#consigneeCodeClient').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                </c:otherwise>
            </c:choose>
            </td>
        </cong:td>
        <cong:td>
            <cong:text id="consigneeCodeClient" name="consAcct" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook" readOnly="true" value="${lclQuote.consAcct.accountno}" style="width:70px"/>
        </cong:td>
        <cong:td>
            <span class="cons">
                <img src="${path}/img/icons/iicon.png" width="12" height="12" alt="shipper" id="ship"/>
            </span>
        </cong:td>
        <cong:td>
            <cong:div id="consigneeDojo">
                <cong:autocompletor id="consigneeContactName" scrollHeight="200px" value="${lclQuote.consContact.contactName}" container="NULL" query="CONTACTFORACCT"
                                    shouldMatch="true" fields="consigneeEmailClient,dupConsFax,dupConsPhone" paramFields="consigneeCodeClient"
                                    template="four" styleClass="textlabelsBoldForTextBox textLCLuppercase" name="consContact.contactName" width="450" callback="verifyContactDetails('consignee')"/>
            </cong:div>
            <cong:div id="consigneeManual" style="display:none">
                <cong:text id="consigneeManualContact" name="consigneeManualContact" style="text-transform: uppercase" value="${lclQuote.consContact.contactName}" styleClass="textlabelsBoldForTextBox" maxlength="250" onkeyup="verifyContactDetails('consignee')"/>
            </cong:div>
        </cong:td>
        <input type="hidden" name="dupConsPhone" id="dupConsPhone" value="${lclQuote.consAcct.custContact.phone}"/>
        <input type="hidden" name="dupConsFax" id="dupConsFax" value="${lclQuote.consAcct.custContact.fax}"/>
        <cong:td>
            <cong:checkbox id="newConsigneeContact" name="newConsigneeContact" onclick="newConsigneeContactName();" container="NULL"/>
        </cong:td>
        <cong:td>
            <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="conscontact" onclick="openConsContact('${path}', $('#consigneeNameClient').val(), $('#consigneeCodeClient').val(), $('#consigneeContactName').val(), 'Cons')"/>
        </cong:td>
        <cong:td>
            <cong:text name="consContact.email1" id="consigneeEmailClient"  container="NULL" value="${lclQuote.consContact.email1}" maxlength="100" styleClass="text textlabelsBoldForTextBox" style="width:160px"/>
        </cong:td>
        <cong:td>
            <cong:text name="consContact.fax1" id="consigneeFaxClient" style="width:80px" container="NULL" maxlength="50" styleClass="textlabelsBoldForTextBox" value="${lclQuote.consContact.fax1}"/>
        </cong:td>
        <cong:td>
            <cong:text name="consContact.phone1" id="consigneePhoneClient" style="width:80px" container="NULL" maxlength="50" styleClass="textlabelsBoldForTextBox" value="${lclQuote.consContact.phone1}"/>
        </cong:td>
         <cong:td>
                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="consigneeClientReff" name="consReference" value="${lclQuote.consReference}" maxlength="100" onchange="clientRefConsignee()"/>
         </cong:td>
        <cong:td>
            <label id="consigneePoaClientId" style="font-weight: bold"/>
        </cong:td>
        <cong:td>
            <label id="consigneeCreditClient" style="font-weight: bold"/>
        </cong:td>
        <cong:td styleClass="textlabelsBoldforlcl">Routed by Agent:</cong:td>
        <cong:td colspan="3">
            <cong:autocompletor id="agentInfo"  position="left" name="rtdAgentAcct" template="tradingPartner"
                                params="E" width="600" shouldMatch="true" query="AGENT" container="NULL"
                                styleClass="text textlabelsBoldForTextBox" fields="agentInfo"
                                value="${lclQuote.rtdAgentAcct.accountno}" scrollHeight="300px"/>
        </cong:td>
    </cong:tr>
</table>
