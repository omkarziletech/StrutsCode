<table width="100%" border="0">
    <cong:tr>
        <cong:td styleClass="caption" style="width:1%"></cong:td>
        <cong:td styleClass="caption" style="width:1%"></cong:td>
        <cong:td styleClass="caption" style="width:4%">New</cong:td>
        <cong:td styleClass="caption" style="width:1%">&nbsp;</cong:td>
        <cong:td styleClass="caption" style="width:6%">Acct No</cong:td>
        <cong:td styleClass="caption" style="width:1%">&nbsp;</cong:td>
        <cong:td styleClass="caption" style="width:1%">Booking Contact Name</cong:td>
        <cong:td styleClass="caption" style="width:2%">New</cong:td>
        <cong:td styleClass="caption" style="width:1%">&nbsp;</cong:td>
        <cong:td styleClass="caption" style="width:6%">Booking Email</cong:td>
        <cong:td styleClass="caption" style="width:6%">Fax</cong:td>
        <cong:td styleClass="caption" style="width:7%">Phone</cong:td>
        <cong:td styleClass="caption" style="width:6%">Client Ref</cong:td>
        <cong:td styleClass="caption" style="width:5%">POA&nbsp;&nbsp;</cong:td>
        <cong:td styleClass="caption" style="width:4%">Credit Status</cong:td>
        <cong:td styleClass="caption" style="width:4%">&nbsp;</cong:td>
        <cong:td styleClass="caption" style="width:4%">&nbsp;</cong:td>
    </cong:tr>
    <cong:tr>
        <cong:td styleClass="textlabelsBoldforlcl">Client</cong:td>
        <cong:td>
            <cong:div id="clientContainer">
                <cong:autocompletor name="clientCompany" template="tradingPartner" id="client"
                                    fields="client_no,acct_type,sub_type,clientBsEciAcctNo,clientBsEciFwNo,NULL,NULL,clientDisabled,address,city,state,NULL,zip,NULL,phone,fax,NULL,fmcNo,otiNumber,clientpoa,creditForClient,NULL,clientDisableAcct,clientColoadNo,clientColoadDesc,clientRetailNo,clientRetailDesc,clientBrand"
                                    query="EXP_CLIENT_NO_CONSIGNEE" value="${lclBooking.clientContact.companyName}" width="600" container="NULL" shouldMatch="true"
                                    styleClass="textlabelsBoldForTextBox textLCLuppercase" paramFields="clientSearchState,clientSearchZip,clientSearchSalesCode,clientSearchCountryUnLocCode"
                                    scrollHeight="300px" callback="notesCount('${path}','clntNotes',$('#client_no').val(),$('#acct_type').val());acctTypeCheck();clientDetails();displayClientDetails();checkEculine($('#client_no').val(),'CLN');"/>
            </cong:div>
            <cong:div id="clientConsContainer" style="display:none">
                <cong:autocompletor name="clientCompany" template="tradingPartner" id="clientCons"
                                    fields="client_no,acct_type,sub_type,clientBsEciAcctNo,clientBsEciFwNo,NULL,NULL,clientDisabled,address,city,state,NULL,zip,NULL,phone,fax,NULL,fmcNo,otiNumber,clientpoa,creditForClient,NULL,clientDisableAcct,clientColoadNo,clientColoadDesc,clientRetailNo,clientRetailDesc,clientBrand"
                                    query="EXP_CLIENT_WITH_CONSIGNEE" value="${lclBooking.clientContact.companyName}" width="600" container="NULL" shouldMatch="true"
                                    styleClass="textlabelsBoldForTextBox textLCLuppercase"  paramFields="clientSearchState,clientSearchZip,clientSearchSalesCode,clientSearchCountryUnLocCode"
                                    callback=" acctTypeCheck();clientDetails();showConsContainer();displayClientDetails();notesCount('${path}','clntNotes',$('#client_no').val(),$('#acct_type').val());checkEculine($('#client_no').val(),'CLN');" scrollHeight="300px"/>
            </cong:div>
            <cong:div id="clientText" style="display:none">
                <cong:text name="tempClientCompany" id="ManualClient"
                           value="${lclBookingForm.tempClientCompany}" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" maxlength="50"/>
            </cong:div>
            <input type="hidden" id="clientColoadNo" value=" ${lclBooking.clientAcct.generalInfo.genericCode.code}"/>
            <input type="hidden" id="clientColoadDesc" value="${lclBooking.clientAcct.generalInfo.genericCode.codedesc}"/>
            <input type="hidden" id="clientRetailNo" value="${lclBooking.clientAcct.generalInfo.retailCommodity.code}"/>
            <input type="hidden" id="clientRetailDesc" value="${lclBooking.clientAcct.generalInfo.retailCommodity.codedesc}"/>
            <input type="hidden" id="clientBsEciAcctNo" value="${lclBooking.clientAcct.eciAccountNo}"/>
            <input type="hidden" id="clientBsEciFwNo" value="${lclBooking.clientAcct.ECIFWNO}"/>
            <input type="hidden" name="clientDisabled" id="clientDisabled"/>
            <input type="hidden" name="clientDisableAcct" id="clientDisableAcct"/>
            <input type="hidden" name="dupClientPhone" id="dupClientPhone" value="${lclBooking.clientAcct.custAddr.phone}"/>
            <input type="hidden" id="clientBrand" name="clientBrand" value="${lclBooking.clientAcct.brandPreference}"/>
            <cong:checkbox name="clientWithoutConsignee" id="clientManual" onclick="newClient();"  />
            <cong:checkbox name="clientWithConsignee" id="clientWithConsignee" container="NULL"
                           onclick="showConsigneeForClient();
                               clientSearch()" title="Checked=Consignee Listed <br/>unChecked=consignee not Listed"/>
            <td>
                <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                     title="Click here to edit Client Search options" onclick="showClientSearchOption('${path}', 'Client')"/>

            <c:choose>
                <c:when test="${lclBookingForm.clientIcon}">
                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Client Notes"
                         class="clntNotes"  onclick="displayNotes('${path}', $('#client_no').val(), 'clntNotes', $('#fileId').val(), $('#fileNo').val());"/>
                </c:when>
                <c:otherwise>
                    <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Client Notes"
                         class="clntNotes" onclick="displayNotes('${path}', $('#client_no').val(), 'clntNotes', $('#fileId').val(), $('#fileNo').val());"/>
                </c:otherwise>
            </c:choose>

            <c:if test="${empty lclBooking.lclFileNumber}">
                <img src="${path}/img/icons/copy.gif" alt="Look Up" width="16" height="16" title="Replicate Client Booking"
                     id="replicateClient" class="replicateBkgClient" onclick="openReplicateBkg('${path}', $('#client').val(), $('#client_no').val())"/>
            </c:if>
            </td>
            <cong:hidden name="acctType" id="acct_type"/>
            <cong:hidden name="subType" id="sub_type"/>
            <input type="hidden" name="clientSearchState" id="clientSearchState"/>
            <input type="hidden" name="clientSearchCountry" id="clientSearchCountry"/>
            <input type="hidden" name="clientSearchZip" id="clientSearchZip"/>
            <input type="hidden" name="clientSearchSalesCode" id="clientSearchSalesCode"/>
            <input type="hidden" name="clientSearchCountryUnLocCode" id="clientSearchCountryUnLocCode"/>
            <cong:hidden name="clientContact.address" id="address" value="${lclBooking.clientContact.address}"/>
            <cong:hidden name="city" id="city" value="${lclBooking.clientContact.city}"/>
            <cong:hidden name="state" id="state" value="${lclBooking.clientContact.state}"/>
            <input type="hidden" name="country" id="country" value="${lclBooking.clientContact.country}"/>
            <cong:hidden name="zip" id="zip" value="${lclBooking.clientContact.zip}"/>
            <cong:hidden name="fmcNumber" id="fmcNo" value="${lclBooking.clientAcct.generalInfo.fwFmcNo}"/>
            <cong:hidden name="otiNumber" id="otiNumber" value="${lclBooking.clientAcct.generalInfo.nvoOtiLicenseNo}"/>
        </cong:td>
        <cong:td>
            <cong:text name="clientAcct" id="client_no" value="${lclBooking.clientAcct.accountno}" style="width:70px;text-align: left" container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" />
        </cong:td>
        <cong:td>
            <span class="cons" onmouseover="" onmouseout="" >
                <img src="${path}/img/icons/iicon.png" width="12" height="12" alt="Client" id="clientT"/>
            </span>
        </cong:td>
        <cong:td>
            <span id="clientContactDojo">
                <cong:autocompletor id="contactName" value="${lclBooking.clientContact.contactName}" paramFields="client_no" fields="email,dupFax,dupPhone"
                                    scrollHeight="200px" container="NULL" shouldMatch="true" query="CONTACTFORACCT" template="four" name="clientContact.contactName"
                                    styleClass="textlabelsBoldForTextBox contactName" width="450" callback="verifyContactDetails('client')"/>
            </span>
            <span id="clientContactManual" style="display:none">
                <cong:text id="clientContactManul" name="clientContactManual"
                           style="text-transform: uppercase" onkeyup="verifyContactDetails('client')" 
                           value="${lclBooking.clientContact.contactName}" styleClass="textlabelsBoldForTextBox contactName" maxlength="250"/>
            </span>
            <input type="hidden" name="bookingContact" id="bookingContact"/>
            <input type="hidden" name="bookingContactEmail" id="bookingContactEmail"/>
            <input type="hidden" name="dupFax" id="dupFax" value="${lclBooking.clientAcct.custContact.fax}"/>
            <input type="hidden" name="dupPhone" id="dupPhone" value="${lclBooking.clientAcct.custContact.phone}"/>
        </cong:td>
        <cong:td>
            <cong:checkbox id="newClientContact" name="newClientContact" onclick="newClientContactName();" container="NULL"/> </cong:td>
        <cong:td>
            <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="contactR" onclick="openClientBkgContact('${path}', $('#client').val(), $('#client_no').val(), $('#contactName').val(), 'C')"/>
        </cong:td>
        <cong:td>
            <cong:text name="clientContact.email1" id="email" styleClass="textlabelsBoldForTextBox" style="width:160px" container="NULL" value="${lclBooking.clientContact.email1}" maxlength="100"/>
        </cong:td>
        <cong:td>
            <cong:text name="clientContact.fax1" id="fax" value="${lclBooking.clientContact.fax1}" style="width:80px" container="NULL" maxlength="50" styleClass="textlabelsBoldForTextBox"/>
        </cong:td>
        <cong:td>
            <cong:text name="clientContact.phone1" id="phone" style="width:80px"   container="NULL" maxlength="250" styleClass="textlabelsBoldForTextBox" value="${lclBooking.clientContact.phone1}"/>
        </cong:td>
        <cong:td>
            
        </cong:td>
        <cong:td>
            <span id="clientPoa"></span>
            <cong:hidden name="clientPoaClient" id="clientpoa" value="${lclBooking.clientAcct.generalInfo.poa}"/>
        </cong:td>
            <%--   <cong:label text=""  id="clientCreditClient" style="font-weight:bold;"/>--%>
            <input type="hidden" id="creditForClient" name="creditForClient" value="${CreditForClient}"/>
        <cong:td>
            <c:choose>
               <c:when test="${CreditForClient eq 'Suspended/See Accounting' || CreditForClient eq 'No Credit'}">
                            <label id="clientCreditClient" style="color: red;font-weight: bold">${CreditForClient}</label></c:when>
                <c:otherwise>
                    <label id="clientCreditClient" style="color: green;font-weight: bold">${not empty CreditForClient ? CreditForClient :''}</label>
                </c:otherwise>
            </c:choose>
        </cong:td>       
        <cong:td styleClass="textlabelsBoldforlcl" valign="middle">  Default Agent</cong:td>
        <cong:td  styleClass="textBoldforlcl">
            <cong:radio value="Y" name="defaultAgent" id="defaultAgent"  container="NULL" styleClass="defAgent" onclick="fillLCLdefaultAgent('${lclBookingForm.moduleName}')"/> Yes
            <cong:radio value="N" name="defaultAgent" id="defaultAgent" container="NULL" styleClass="defAgent" onclick="clearLCLDefaultValues()"/> No
        </cong:td>
    </cong:tr>
    <cong:tr>
        <cong:td styleClass="textlabelsBoldforlcl">Shipper</cong:td>
        <cong:td>
            <span id="dojoShipp">
                <c:choose> 
                    <c:when test="${lclBookingForm.moduleName=='Imports' && lclBooking.bookingType eq 'T'}">
                        <cong:autocompletor name="shipContact.companyName" template="tradingPartner" id="shipperNameClient"
                                            fields="shipperCodeClient,shipperaccttype,shippersubtype,shipBsEciAcctNo,shipBsEciFwNo,NULL,NULL,shipperDisabled,shipperAddressClient,shipperCityClient,shipperStateClient,shipperCountryClient,shipperZipClient,NULL,dupShipperPhone,dupShipperFax,NULL,sFmc,sotiNumber,shipperPoa,creditForShipper,NULL,shipDisableAcct,shipColoadNo,shipColoadDesc,shipRetailNo,shipRetailDesc,null,shipperBrand"
                                            styleClass="textlabelsBoldForTextBox textLCLuppercase" query="SHIPPER" paramFields="shipperSearchState,shipperSearchZip,shipperSearchSalesCode,shipperCountryUnLocCode"  width="600" container="NULL" shouldMatch="true" value="${lclBooking.rtdAgentAcct.accountName}"
                                            callback="notesCount('${path}','shpNotes',$('#shipperCodeClient').val(),'S');shipper_AccttypeCheck();shipDetails();displayShipperDetails();" scrollHeight="300px"/>
                    </c:when>
                    <c:otherwise>
                        <cong:autocompletor name="shipContact.companyName" template="tradingPartner" id="shipperNameClient"
                                            fields="shipperCodeClient,shipperaccttype,shippersubtype,shipBsEciAcctNo,shipBsEciFwNo,NULL,NULL,shipperDisabled,shipperAddressClient,shipperCityClient,shipperStateClient,shipperCountryClient,shipperZipClient,NULL,dupShipperPhone,dupShipperFax,NULL,sFmc,sotiNumber,shipperPoa,creditForShipper,NULL,shipDisableAcct,shipColoadNo,shipColoadDesc,shipRetailNo,shipRetailDesc,null,shipperBrand"
                                            styleClass="textlabelsBoldForTextBox textLCLuppercase" query="SHIPPER" paramFields="shipperSearchState,shipperSearchZip,shipperSearchSalesCode,shipperCountryUnLocCode"  width="600" container="NULL" shouldMatch="true" value="${lclBooking.shipContact.companyName}"
                                            callback="checkEculine($('#shipperCodeClient').val(),'S'); notesCount('${path}','shpNotes',$('#shipperCodeClient').val(),'S');shipper_AccttypeCheck();shipDetails();displayShipperDetails();" scrollHeight="300px"/>
                    </c:otherwise>
                </c:choose>
            </span>
            <span id="manualShipp" style="display:none">
                <cong:text name="shipContactName" id="dupShipName" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" value="${lclBookingForm.shipContactName}" onchange="newShippChange()"/></span>
            <input type="hidden" name="dupShipperFax" id="dupShipperFax" value="${lclBooking.shipAcct.custAddr.fax}"/>
            <input type="hidden" name="dupShipperPhone" id="dupShipperPhone" value="${lclBooking.shipAcct.custAddr.phone}"/>
            <input type="hidden" name="shipper_accttype" id="shipperaccttype"/>
            <input type="hidden" name="shipperSearchState" id="shipperSearchState"/>
            <input type="hidden" name="shipperSearchZip" id="shipperSearchZip"/>
            <input type="hidden" name="shipperSearchSalesCode" id="shipperSearchSalesCode"/>
            <input type="hidden" name="searchClientBy" id="searchClientBy" value=""/>
            <input type="hidden" name="shipper_subtype" id="shippersubtype"/>
            <input type="hidden" name="shipperSearchCountry" id="shipperSearchCountry"/>
            <input type="hidden" name="shipperCountryUnLocCode" id="shipperCountryUnLocCode"/>
            <cong:hidden name="shipContact.city" value="${lclBooking.shipContact.city}" id="shipperCityClient"/>
            <cong:hidden name="shipContact.country" value="${lclBooking.shipContact.country}" id="shipperCountryClient"/>
            <cong:hidden name="shipContact.state" value="${lclBooking.shipContact.state}" id="shipperStateClient"/>
            <cong:hidden name="shipContact.zip" value="${lclBooking.shipContact.zip}" id="shipperZipClient"/>
            <input type="hidden" name="sFmc" value="${lclBooking.shipAcct.generalInfo.fwFmcNo}" id="sFmc"/>
            <cong:hidden name="shipContact.address" id="shipperAddressClient"  styleClass="textlabelsBoldForTextBox" value="${lclBooking.shipContact.address}"/>
            <input type="hidden" id="shipColoadNo" value="${lclBooking.shipAcct.generalInfo.genericCode.code}"/>
            <input type="hidden" id="shipColoadDesc" value="${lclBooking.shipAcct.generalInfo.genericCode.codedesc}"/>
            <input type="hidden" id="shipRetailNo" value="${lclBooking.shipAcct.generalInfo.retailCommodity.code}"/>
            <input type="hidden" id="shipRetailDesc" value="${lclBooking.shipAcct.generalInfo.retailCommodity.codedesc}"/>
            <input type="hidden" id="shipBsEciAcctNo" value="${lclBooking.shipAcct.eciAccountNo}"/>
            <input type="hidden" id="shipBsEciFwNo" value="${lclBooking.shipAcct.ECIFWNO}"/>
            <input type="hidden" name="shipperDisabled" id="shipperDisabled"/>
            <input type="hidden" name="shipDisableAcct" id="shipDisableAcct"/>
            <input type="hidden" name="shipperBrand" id="shipperBrand" value="${lclBooking.shipAcct.brandPreference}"/>
        </cong:td>
        <cong:td> 
            <input type="hidden" name="fileId" id="fileId" value="${lclBooking.lclFileNumber.id}"/>
            <input type="hidden" name="fileNo" id="fileNo" value="${lclBooking.lclFileNumber.fileNumber}"/>
            <cong:checkbox id="newShipp" name="newShipper" onclick="newShippName();" container="NULL"/>
        </cong:td>
        <td>
            <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                 title="Click here to edit Shipper Search options" onclick="showClientSearchOption('${path}', 'Shipper')"/>

        <c:choose>
            <c:when test="${lclBookingForm.shipperIcon}">
                <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Shipper Notes"
                     class="shpNotes"  onclick="displayNotes('${path}', $('#shipperCodeClient').val(), 'shpNotes', $('#fileId').val(), $('#fileNo').val());"/>
            </c:when>
            <c:otherwise>
                <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Shipper Notes"
                     class="shpNotes" onclick="displayNotes('${path}', $('#shipperCodeClient').val(), 'shpNotes', $('#fileId').val(), $('#fileNo').val());"/>
            </c:otherwise>
        </c:choose>

        </td>
        <cong:td>
            <c:choose>
                <c:when test="${lclBookingForm.moduleName=='Imports' && lclBooking.bookingType eq 'T'}">
                    <cong:text name="shipAcct" id="shipperCodeClient" value="${lclBooking.rtdAgentAcct.accountno}" style="width:70px" container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"/>
                </c:when><c:otherwise>
                    <cong:text name="shipAcct" id="shipperCodeClient" value="${lclBooking.shipAcct.accountno}" style="width:70px" container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"/>
                </c:otherwise></c:choose>
        </cong:td>
        <cong:td>
            <span class="cons">
                <img src="${path}/img/icons/iicon.png" width="12" height="12" alt="shipper" id="shipp"/>
            </span>
        </cong:td>
        <cong:td>
            <cong:div id="shipperDojo">
                <cong:autocompletor id="shipperContactClient" value="${lclBooking.shipContact.contactName}" fields="shipperEmailClient,dupShipFax,dupShipPhone"
                                    paramFields="shipperCodeClient" scrollHeight="200px" container="NULL" width="450" shouldMatch="true"
                                    query="CONTACTFORACCT" template="four" styleClass="textlabelsBoldForTextBox textLCLuppercase contactName" 
                                    name="shipContact.contactName" callback="verifyContactDetails('shipper')"/>
            </cong:div>
            <cong:div id="shippManual" style="display:none">
                <cong:text id="shipperManualContact" name="shipperManualContact"
                           style="text-transform: uppercase" onkeyup="verifyContactDetails('shipper')" value="${lclBooking.shipContact.contactName}" 
                           styleClass="textlabelsBoldForTextBox contactName"/>
            </cong:div>
            <input type="hidden" name="dupShipPhone" id="dupShipPhone" value="${lclBooking.shipAcct.custContact.phone}" />
            <input type="hidden" name="dupShipFax" id="dupShipFax" value="${lclBooking.shipAcct.custContact.fax}"/>
        </cong:td>
        <cong:td>
            <cong:checkbox id="newShipperContact" name="newShipperContact" onclick="newShipperContactName();" container="NULL"/>
        </cong:td>
        <cong:td>
            <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="shipcontact" onclick="openShipContact('${path}', $('#shipperNameClient').val(), $('#shipperCodeClient').val(), $('#shipperContactClient').val(), 'S', $('#shipContact'))"/>
        </cong:td>
        <cong:td>
            <cong:text name="shipContact.email1" id="shipperEmailClient" container="NULL" value="${lclBooking.shipContact.email1}" maxlength="100" styleClass="text textlabelsBoldForTextBox" style="width:160px"/>
        </cong:td>
        <cong:td>
            <cong:text name="shipContact.fax1" id="shipperFaxClient" style="width:80px"  container="NULL" maxlength="50" styleClass="textlabelsBoldForTextBox" value="${lclBooking.shipContact.fax1}"/>
        </cong:td>
        <cong:td>
            <cong:text name="shipContact.phone1" id="shipperPhoneClient" style="width:80px" container="NULL" maxlength="50" styleClass="textlabelsBoldForTextBox" value="${lclBooking.shipContact.phone1}"/>
        </cong:td>
         <cong:td>
          <input type="text" class="text textlabelsBoldForTextBox textCap" value="${lclBooking.shipReference}" tabindex="-1" id="dupShipref"  name="shipReference" maxlength="100" onchange="ShipReference()"/>
        </cong:td>
        <cong:td>
            <span id="shipper1" name="warning"></span>
            <input type="hidden" id="creditForShipper" name="creditForShipper" value="${CreditForShipper}"/>
            <c:choose>
                <c:when test="${lclBooking.shipAcct.generalInfo.poa eq 'Y'}">
                    <label id="shipperPOAV" style="color: green;font-weight: bold">YES</label>
                </c:when>
                <c:otherwise>
                    <label id="shipperPOAV" style="color: red;font-weight: bold">
                        ${not empty lclBooking.shipAcct ? 'NO' :''}
                    </label>
                </c:otherwise>
            </c:choose>
        </cong:td>
        <cong:td>
            <c:choose>
               <c:when test="${CreditForShipper eq 'Suspended/See Accounting' || CreditForShipper eq 'No Credit'}">
                            <label id="shipperCreditStatus" style="color: red;font-weight: bold">${CreditForShipper}</label></c:when>
                <c:otherwise>
                    <label id="shipperCreditStatus" style="color: green;font-weight: bold">${not empty CreditForShipper ? CreditForShipper :''}</label>
                </c:otherwise>
            </c:choose> 
        </cong:td>
        <input type="hidden" name="sotiNumber" id="sotiNumber" value="${lclBooking.shipAcct.generalInfo.nvoOtiLicenseNo}"/>
        <cong:td colspan="2">
            <cong:table id="m" >
                <jsp:include page="/jsps/LCL/ajaxload/refreshAgent.jsp"/></cong:table></cong:td>
    </cong:tr>

    <cong:tr>
        <cong:td styleClass="textlabelsBoldforlcl">Forwarder</cong:td>
        <cong:td>
            <cong:autocompletor name="fwdContact.companyName" template="tradingPartner" id="forwarderNameClient"
                                fields="forwarderCodeClient,forwarderaccttype,forwardersubtype,fwdBsEciAcctNo,fwdBsEciFwNo,NULL,NULL,forwardDisabled,forwarderAddressClient,forwarderCityClint,forwarderStateClient,forwarderCountryClient,forwarderZipClient,NULL,dupForwarderPhone,dupForwarderFax,NULL,fFmc,fotiNumber,forwarderPoaClient,creditForForwarder,NULL,forwardDisabledAcct,fwdColoadNo,fwdColoadDesc,fwdRetailNo,fwdRetailDesc,fwdBrand"
                                query="FORWARDER"  width="600" container="NULL" value="${lclBooking.fwdContact.companyName}" styleClass="textlabelsBoldForTextBox textLCLuppercase"
                                shouldMatch="true" paramFields="frwdSearchState,frwdSearchZip,frwdSearchSalesCode,frwdSearchCountryUnLocCode" 
                                callback="if(forwarder_AccttypeCheck()){notesCount('${path}','fwdNotes',$('#forwarderCodeClient').val(),'V');frwdDetails();checkEculine($('#forwarderCodeClient').val(),'F');displayForwarderDetails();}" scrollHeight="300px"/>
            <input type="hidden" name="forwarder_accttype" id="forwarderaccttype"/>
            <input type="hidden" name="dupForwarderPhone" id="dupForwarderPhone" value="${lclBooking.fwdAcct.custAddr.phone}"/>
            <input type="hidden" name="dupForwarderFax" id="dupForwarderFax" value="${lclBooking.fwdAcct.custAddr.fax}"/>
            <input type="hidden" name="forwarder_subtype" id="forwardersubtype"/>
            <input type="hidden" name="frwdSearchState" id="frwdSearchState"/>
            <input type="hidden" name="frwdSearchZip" id="frwdSearchZip"/>
            <input type="hidden" name="frwdSearchSalesCode" id="frwdSearchSalesCode"/>
            <input type="hidden" name="frwdSearchCountryUnLocCode" id="frwdSearchCountryUnLocCode"/>
            <input type="hidden" name="frwdSearchCountry" id="frwdSearchCountry"/>
            <input type="hidden" name="forwardDisabled" id="forwardDisabled"/>
            <input type="hidden" name="forwardDisabledAcct" id="forwardDisabledAcct"/>
            <input type="hidden" id="fwdBsEciAcctNo" value="${lclBooking.fwdAcct.eciAccountNo}"/>
            <input type="hidden" id="fwdBsEciFwNo" value="${lclBooking.fwdAcct.ECIFWNO}"/>
            <cong:hidden  name="fwdContact.city" value="${lclBooking.fwdContact.city}"  id="forwarderCityClint"/>
            <cong:hidden name="fwdContact.country" value="${lclBooking.fwdContact.country}" id="forwarderCountryClient"/>
            <cong:hidden name="fwdContact.state" value="${lclBooking.fwdContact.state}" id="forwarderStateClient"/>
            <cong:hidden name="fwdContact.zip" value="${lclBooking.fwdContact.zip}" id="forwarderZipClient"/>
            <cong:hidden name="fwdContact.address" value="${lclBooking.fwdContact.address}" id="forwarderAddressClient"/>
            <input type="hidden" name="fFmc" id="fFmc" value="${lclBooking.fwdAcct.generalInfo.fwFmcNo}"/>
            <input type="hidden" name="fotiNumber" id="fotiNumber" value="${lclBooking.fwdAcct.generalInfo.nvoOtiLicenseNo}"/>
            <input type="hidden" id="fwdColoadNo" value="${lclBooking.fwdAcct.generalInfo.genericCode.code}"/>
            <input type="hidden" id="fwdColoadDesc" value="${lclBooking.fwdAcct.generalInfo.genericCode.codedesc}"/>
            <input type="hidden" id="fwdRetailNo" value="${lclBooking.fwdAcct.generalInfo.retailCommodity.code}"/>
            <input type="hidden" id="fwdRetailDesc" value="${lclBooking.fwdAcct.generalInfo.retailCommodity.codedesc}"/>
            <input type="hidden" name="fwdBrand" id="fwdBrand" value="${lclBooking.fwdAcct.brandPreference}"/>
        </cong:td>
        <cong:td>
            <input type="hidden" name="fileId" id="fileId" value="${lclBooking.lclFileNumber.id}"/>
            <input type="hidden" name="fileNo" id="fileNo" value="${lclBooking.lclFileNumber.fileNumber}"/>
        </cong:td>
        <td>
            <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                 title="Click here to edit Forwarder Search options" onclick="showClientSearchOption('${path}', 'Forwarder')"/>
        <c:choose>
            <c:when test="${lclBookingForm.forwaderIcon}">
                <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Forwarder Notes"
                     class="fwdNotes"  onclick="displayNotes('${path}', $('#forwarderCodeClient').val(), 'fwdNotes', $('#fileId').val(), $('#fileNo').val());"/>
            </c:when>
            <c:otherwise>
                <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Forwarder Notes"
                     class="fwdNotes" onclick="displayNotes('${path}', $('#forwarderCodeClient').val(), 'fwdNotes', $('#fileId').val(), $('#fileNo').val());"/>
            </c:otherwise>
        </c:choose>
        </td>
        <cong:td>
            <cong:text name="fwdAcct" id="forwarderCodeClient" value="${lclBooking.fwdAcct.accountno}" style="width:70px" container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"/>
        </cong:td>
        <cong:td>
            <span class="cons" onmouseover="" onmouseout="">
                <img src="${path}/img/icons/iicon.png" width="12" height="12" alt="shipper" id="Fwd"/>
            </span>
        </cong:td>
        <cong:td>

            <cong:div id="forwarederDojo">
                <cong:autocompletor id="forwardercontactClient" fields="forwarderEmailClient,dupFwdFax,dupFwdPhone"
                                    value="${lclBooking.fwdContact.contactName}" paramFields="forwarderCodeClient" container="NULL"  callback="verifyContactDetails('frwd')"
                                    template="four" scrollHeight="200px" styleClass="textlabelsBoldForTextBox textLCLuppercase contactName" 
                                    name="fwdContact.contactName" width="450" query="CONTACTFORACCT" shouldMatch="true"/>
            </cong:div>
            <cong:div id="forwarederManual" style="display:none">
                <cong:text name="forwarederContactManual" id="forwarederContactManual"
                           onkeyup="verifyContactDetails('frwd')" style="text-transform: uppercase"
                           value="${lclBooking.fwdContact.contactName}" styleClass="textlabelsBoldForTextBox contactName" maxlength="250"/>
            </cong:div>
            <input type="hidden" name="dupFwdPhone" id="dupFwdPhone" value="${lclBooking.fwdAcct.custContact.phone}"/>
            <input type="hidden" name="dupFwdFax" id="dupFwdFax" value="${lclBooking.fwdAcct.custContact.fax}"/>
        </cong:td>
        <cong:td>
            <cong:checkbox id="newForwarderContact" name="newForwarderContact" onclick="newForwarderContactName();" container="NULL"/>
        </cong:td>
        <cong:td>
            <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="fwdcontact" onclick="openFwdContact('${path}', $('#forwarderNameClient').val(), $('#forwarderCodeClient').val(), $('#forwardercontactClient').val(), 'F', $('#fwdContact'))"/>
        </cong:td>
        <cong:td>
            <cong:text name="fwdContact.email1" id="forwarderEmailClient" container="NULL" value="${lclBooking.fwdContact.email1}" maxlength="100" styleClass="text textlabelsBoldForTextBox" style="width:160px"/>
        </cong:td>
        <cong:td>
            <cong:text name="fwdContact.fax1" id="forwarderFaxClient"  style="width:80px" container="NULL" maxlength="19" styleClass="textlabelsBoldForTextBox" value="${lclBooking.fwdContact.fax1}"/>
        </cong:td>
        <cong:td>
            <cong:text name="fwdContact.phone1" id="forwarderPhoneClient"  style="width:80px"  container="NULL" maxlength="19" styleClass="textlabelsBoldForTextBox" value="${lclBooking.fwdContact.phone1}"/>
        </cong:td>
         <cong:td>
                    <input type="text" class="text textlabelsBoldForTextBox textCap" value="${lclBooking.fwdReference}" tabindex="-1" id="forwarderClientReff" name="fwdReference"  maxlength="100" onchange="clientRefForwarder()"/>
        </cong:td>
        <cong:td>
            <c:choose>
                <c:when test="${lclBooking.fwdAcct.generalInfo.poa eq 'Y'}">
                    <span id="forwarder1" style="color:green;font-weight: bold">YES</span>
                </c:when>
                <c:otherwise>
                    <span id="forwarder1" style="color:red;font-weight: bold">${not empty lclBooking.fwdAcct ? "NO":""}</span>
                </c:otherwise>
            </c:choose>
            <cong:hidden name="forwarderPoa" id="forwarderPoaClient" value="${lclBooking.fwdAcct.generalInfo.poa}"/>
        </cong:td>
        <cong:td>

            <c:choose>
                <c:when test="${CreditForForwarder eq 'Suspended/See Accounting' || CreditForForwarder eq 'No Credit'}">
                    <label id="forwarderCreditClient" style="color:red;font-weight: bold">${CreditForForwarder}</label></c:when>
                <c:otherwise>
                    <label id="forwarderCreditClient" style="color:green;font-weight: bold">${not empty CreditForForwarder ? CreditForForwarder :''}</label>
                </c:otherwise>
            </c:choose>
            <input type="hidden" id="creditForForwarder" name="creditForForwarder" value="${CreditForForwarder}"/>
        </cong:td>
        <cong:td styleClass="textlabelsBoldforlcl">ERT Y/N</cong:td>
        <cong:td colspan="3">


            <html:select property="rtdTransaction" styleId="rtdTransaction" value="${lclBookingForm.rtdTransaction}" styleClass="smallDropDown mandatory" onchange="setDefaultRouteAgent()">
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
                                    fields="consigneeCodeClient,consigneeaccttype,consigneesubtype,consBsEciAcctNo,consBsEciFwNo,NULL,NULL,consDisabled,consigneeAddressClient,consigneeCityClient,consigneeStateClient,consigneeCountryClient,consigneeZipClient,NULL,dupConsigneePhone,dupConsigneeFax,NULL,cFmc,cotiNumber,consigneePoa,creditForConsignee,NULL,NULL,consDisableAcct,consColoadNo,consColoadDesc,consRetailNo,consRetailDesc"
                                    query="EXP_CONSIGNEE"  width="600" container="NULL" value="${lclBooking.consContact.companyName}" styleClass="textlabelsBoldForTextBox textLCLuppercase"
                                    paramFields="consigneeSearchState,consigneeSearchZip,consigneeSearchSalesCode,consigneeCountryUnLocCode" shouldMatch="true" 
                                    callback="notesCount('${path}','conNotes',$('#consigneeCodeClient').val(),'C');consignee_AccttypeCheck();consDetails();displayConsigneeDetails();checkEculine($('#consigneeCodeClient').val(),'C');" scrollHeight="300px"/>
            </span>
            <input type="hidden" name="consDisabled" id="consDisabled"/>
            <input type="hidden" name="consDisableAcct" id="consDisableAcct"/>
            <input type="hidden" name="dupConsigneePhone" id="dupConsigneePhone" value="${lclBooking.consAcct.custAddr.phone}"/>
            <input type="hidden" name="dupConsigneeFax" id="dupConsigneeFax" value="${lclBooking.consAcct.custAddr.fax}"/>
            <span id="manualConsign" style="display:none">
                <cong:text name="consContactName" id="dupConsName" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" value="${lclBookingForm.consContactName}" onchange="newConsignChange()"/></span>
            <input type="hidden" name="consignee_accttype" id="consigneeaccttype"/>
            <input type="hidden" name="consignee_subtype" id="consigneesubtype"/>
            <input type="hidden" name="consigneeSearchState" id="consigneeSearchState"/>
            <input type="hidden" name="consigneeSearchZip" id="consigneeSearchZip"/>
            <input type="hidden" name="consigneeCountryUnLocCode" id="consigneeCountryUnLocCode"/>
            <input type="hidden" name="consigneeSearchCountry" id="consigneeSearchCountry"/>
            <input type="hidden" id="creditForConsignee" name="creditForConsignee" value="${CreditForConsignee}"/>
            <input type="hidden" name="consigneeSearchSalesCode" id="consigneeSearchSalesCode"/>
            <cong:hidden name="consContact.city" value="${lclBooking.consContact.city}" id="consigneeCityClient"/>
            <cong:hidden name="consContact.country" value="${lclBooking.consContact.country}" id="consigneeCountryClient"/>
            <cong:hidden name="consContact.state" value="${lclBooking.consContact.state}" id="consigneeStateClient"/>
            <cong:hidden name="consContact.zip" value="${lclBooking.consContact.zip}" id="consigneeZipClient"/>
            <cong:hidden name="consContact.address" value="${lclBooking.consContact.address}" id="consigneeAddressClient"/>
            <input type="hidden" name="cFmc" id="cFmc" value="${lclBooking.consAcct.generalInfo.fwFmcNo}"/>
            <input type="hidden" name="cotiNumber" id="cotiNumber" value="${lclBooking.consAcct.generalInfo.nvoOtiLicenseNo}"/>
            <input type="hidden" id="consColoadNo" value="${lclBooking.consAcct.generalInfo.consColoadCommodity.code}"/>
            <input type="hidden" id="consColoadDesc" value="${lclBooking.consAcct.generalInfo.consColoadCommodity.codedesc}"/>
            <input type="hidden" id="consRetailNo" value="${lclBooking.consAcct.generalInfo.consRetailCommodity.code}"/>
            <input type="hidden" id="consRetailDesc" value="${lclBooking.consAcct.generalInfo.consRetailCommodity.codedesc}"/>
            <input type="hidden" id="consBsEciAcctNo" value="${lclBooking.consAcct.eciAccountNo}"/>
            <input type="hidden" id="consBsEciFwNo" value="${lclBooking.consAcct.ECIFWNO}"/>
        </cong:td>
        <cong:td>
            <input type="hidden" name="fileId" id="fileId" value="${lclBooking.lclFileNumber.id}"/>
            <input type="hidden" name="fileNo" id="fileNo" value="${lclBooking.lclFileNumber.fileNumber}"/>
            <cong:checkbox id="newConsign" name="newConsignee" onclick="newConsignName();" container="NULL"/>
        </cong:td>
        <td>
            <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                 title="Click here to edit Consignee Search options" onclick="showClientSearchOption('${path}', 'Consignee')"/>

        <c:choose>
            <c:when test="${lclBookingForm.consigneeIcon}">
                <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Consignee Notes"
                     class="conNotes"  onclick="displayNotes('${path}', $('#consigneeCodeClient').val(), 'conNotes', $('#fileId').val(), $('#fileNo').val());"/>
            </c:when>
            <c:otherwise>
                <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Consignee Notes"
                     class="conNotes" onclick="displayNotes('${path}', $('#consigneeCodeClient').val(), 'conNotes', $('#fileId').val(), $('#fileNo').val());"/>
            </c:otherwise>
        </c:choose>
        </td>
        <cong:td>
            <cong:text id="consigneeCodeClient" name="consAcct" style="width:70px" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook" readOnly="true" value="${lclBooking.consAcct.accountno}"/>
        </cong:td>
        <cong:td>
            <span class="cons" onmouseover="" onmouseout="">
                <img src="${path}/img/icons/iicon.png" width="12" height="12" alt="shipper"  id="ship"/>
            </span>
        </cong:td>
        <cong:td>
            <span id="consigneeDojo">
                <cong:autocompletor id="consigneeContactName" fields="consigneeEmailClient,dupConsFax,dupConsPhone" value="${lclBooking.consContact.contactName}" shouldMatch="true" container="NULL"
                                    query="CONTACTFORACCT" template="four" paramFields="consigneeCodeClient" styleClass="textlabelsBoldForTextBox textLCLuppercase contactName"
                                    name="consContact.contactName" width="450" scrollHeight="200px" callback="verifyContactDetails('consignee')"/>
            </span>
            <input type="hidden" name="dupConsPhone" id="dupConsPhone" value="${lclBooking.consAcct.custContact.phone}"/>
            <input type="hidden" name="dupConsFax" id="dupConsFax" value="${lclBooking.consAcct.custContact.fax}"/>
            <span id="consigneeManual" style="display:none">
                <cong:text id="consigneeManualContact" name="consigneeManualContact" style="text-transform: uppercase" 
                           onkeyup="verifyContactDetails('consignee')" value="${lclBooking.consContact.contactName}"
                           styleClass="textlabelsBoldForTextBox contactName" maxlength="250"/>
            </span>
        </cong:td>
        <cong:td>
            <cong:checkbox id="newConsigneeContact" name="newConsigneeContact" onclick="newConsigneeContactName();" container="NULL"/>
        </cong:td>
        <cong:td>
            <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="conscontact" onclick="openConsContact('${path}', $('#consigneeNameClient').val(), $('#consigneeCodeClient').val(), $('#consigneeContactName').val(), 'Cons', $('#consigneeContact'))"/>
        </cong:td>
        <cong:td>
            <cong:text name="consContact.email1" id="consigneeEmailClient"  container="NULL" value="${lclBooking.consContact.email1}" maxlength="100" styleClass="text textlabelsBoldForTextBox" style="width:160px"/>
        </cong:td>
        <cong:td>
            <cong:text name="consContact.fax1" id="consigneeFaxClient"  style="width:80px" container="NULL" maxlength="50" styleClass="textlabelsBoldForTextBox" value="${lclBooking.consContact.fax1}"/>
        </cong:td>
        <cong:td>
            <cong:text name="consContact.phone1" id="consigneePhoneClient" style="width:80px"  container="NULL" maxlength="50" styleClass="textlabelsBoldForTextBox" value="${lclBooking.consContact.phone1}"/>
        </cong:td>
        <cong:td>
                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="consigneeClientReff" name="consReference" value="${lclBooking.consReference}" maxlength="100" onchange="clientRefConsignee()"/>
        </cong:td>
        <cong:td>
            <cong:hidden name="consigneePoa" id="consigneePoa" value="${lclBooking.consAcct.generalInfo.poa}"/>
            <c:choose>
                <c:when test="${lclBooking.consAcct.generalInfo.poa eq 'Y'}">
                    <label id="consigneePOAV" style="color: green;font-weight: bold">YES</label></c:when>
                <c:otherwise><label id="consigneePOAV" style="color: red;font-weight: bold">${not empty lclBooking.consAcct ? 'NO' :''}</label></c:otherwise>
            </c:choose>
        </cong:td>
        <cong:td>
            <c:choose>
                <c:when test="${CreditForConsignee eq 'Suspended/See Accounting' || CreditForConsignee eq 'No Credit'}">
                    <label id="consigneeCreditStatus" style="color: red;font-weight: bold">${CreditForConsignee}</label></c:when>
                <c:otherwise>
                    <label id="consigneeCreditStatus" style="color: green;font-weight: bold">${not empty CreditForConsignee ? CreditForConsignee :''}</label>
                </c:otherwise>
            </c:choose>
        </cong:td>
        <cong:td styleClass="textlabelsBoldforlcl">Routed by Agent:</cong:td>
        <cong:td>
            <cong:autocompletor id="agentInfo"  position="left" name="rtdAgentAcct" template="tradingPartner" params="E" width="600" shouldMatch="true" query="AGENT"
                                fields="agentInfo" styleClass="textlabelsBoldForTextBox"  container="NULL" value="${lclBooking.rtdAgentAcct.accountno}" scrollHeight="300px"/>
        </cong:td>
    </cong:tr>
    <cong:hidden id="replicateFileNumber" name="replicateFileNumber" value=""/>
</table>
