<%@include file="/taglib.jsp" %>
<cong:table width="100%">
    <cong:tr>
        <cong:td width="15%" valign="top">
            <cong:table width="100%">
                <cong:tr>
                    <c:choose>
                        <c:when test="${lclQuoteForm.moduleName ne 'Imports'}">
                            <cong:td colspan="2"  styleClass="report-header"> Supplier</cong:td>
                                <c:set var="fieldCss" value="textlabelsBoldForTextBox textCap"/>
                                <c:set var="readonly" value="false"/>
                            </c:when>
                            <c:otherwise>
                            <cong:td colspan="2"  styleClass="report-header">Origin Agent</cong:td>
                                <c:set var="fieldCss" value="textlabelsBoldForTextBoxDisabledLook"/>
                                <c:set var="readonly" value="true"/>
                            </c:otherwise>
                        </c:choose>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="textlabelsBoldforlcl">Code</cong:td>
                    <cong:td>
                        <cong:text  id="supplierCode" name="supAcct"  styleClass="textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclQuote.supAcct.accountno}"/>
                        <c:if test="${lclQuoteForm.moduleName eq 'Imports'}">
                            <cong:img src="${path}/jsps/LCL/images/folder_open.png" style="vertical-align: middle"  styleClass="tradingDetails" width="16" height="16" alt="details" onclick="displayTradingDetails('${path}','#supplierCode','${lclQuote.lclFileNumber.id}','agent')"/>
                        </c:if>
                    </cong:td>
                </cong:tr>
                <c:choose>
                    <c:when test="${lclQuoteForm.moduleName ne 'Imports'}">
                        <cong:tr>
                            <cong:td  styleClass="textlabelsBoldforlcl">Name</cong:td>
                            <cong:td>
                                <span id="dojoSupplier">
                                    <cong:autocompletor id="supplierName" name="supContact.companyName" template="tradingPartner" readOnly="${readonly}"
                                                        fields="supplierCode,NULL,NULL,NULL,NULL,NULL,NULL,supplierDisabled,supplierAddress,supplierCity,supplierState,supplierCountry,supplierZip,NULL,supplierPhone,supplierFax,supplierEmail,NULL,NULL,NULL,supplierPoa,NULL,NULL,NULL,NULL,NULL,NULL,NULL,supplierDisabledAcct"
                                                        query="CLIENT_WITH_CONSIGNEE"  width="600" container="NULL" value="${lclQuote.supContact.companyName}"
                                                        shouldMatch="true" callback="supplierAcctCheck();" scrollHeight="300px"  styleClass="${fieldCss}"/>
                                </span>
                                <span id="manualSupplier"  style="display:none">
                                    <cong:text name="supContactName" id="dupSupplierName" value="${lclQuoteForm.supContactName}" styleClass="text textlabelsBoldForTextBox textCap"/></span>
                                <input type="hidden" name="supplierPoa" id="supplierPoa" value="${lclQuote.supAcct.generalInfo.poa}"/>
                                <c:if test="${lclQuoteForm.moduleName ne 'Imports'}">
                                    <cong:checkbox id="newSupplier" name="newSupplier" onclick="newSupplierName();" container="NULL"/></c:if>
                                    <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"  styleClass="contactR" onclick="openLclContactInfo('${path}','Supplier')"/>
                                    <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" height="16" alt="display" styleClass="trading" 
                                              onclick="newTPAccountFromQuote('${path}','dupSupplierName','supplierAddress','S','supplierCountry',
                                              'supplierCity','supplierState','supplierZip','supplierPhone','supplierFax','supplierEmail',
                                              'supplierCode','supplierCode','supplierUnLocCode','newSupplier');"/>
                                </cong:td>
                            </cong:tr>
                    </c:when><c:otherwise>
                        <cong:tr id="originRefresh">
                            <jsp:include page="/jsps/LCL/ajaxload/impOriginAgentQuote.jsp"/>
                        </cong:tr>
                    </c:otherwise>
                </c:choose>
                <c:if test="${lclQuoteForm.moduleName ne 'Imports'}">
                    <cong:tr>
                        <input type="hidden" name="supplierDisabled" id="supplierDisabled"/>
                        <input type="hidden" name="supplierDisabledAcct" id="supplierDisabledAcct"/>
                        <cong:td  styleClass="textlabelsBoldforlcl">Address</cong:td>
                        <cong:td>
                            <cong:textarea cols="30" rows="6" id="supplierAddress" name="supContact.address" value="${lclQuote.supContact.address}"  styleClass="textlabelsBoldForTextBox textup"/>
                            <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">City</cong:td>
                        <cong:td>
                            <cong:autocompletor id="supplierCity" name="supContact.city" template="three"  
                                                fields="supplierCity,supplierCountry,supplierUnLocCode,supplierState"
                                                styleClass="textlabelsBoldForTextBox textCap"  query="PORT_CITY_STATE"  
                                                width="600" container="NULL" value="${lclQuote.supContact.city}" 
                                                shouldMatch="true" scrollHeight="300">
                            </cong:autocompletor>
                            <input type="hidden" id="supplierUnLocCode" name="supplierUnLocCode"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl"> State </cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="supplierState" name="supContact.state" value="${lclQuote.supContact.state}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Zip</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="supplierZip" name="supContact.zip" value="${lclQuote.supContact.zip}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Country</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="supplierCountry" name="supContact.country" value="${lclQuote.supContact.country}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Phone</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="supplierPhone" name="supContact.phone1" value="${lclQuote.supContact.phone1}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Fax</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="supplierFax" name="supContact.fax1" value="${lclQuote.supContact.fax1}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Email</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="supplierEmail" name="supContact.email1" value="${lclQuote.supContact.email1}" maxlength="100"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Client Ref</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1"  id="supReference" name="supReference" value="${lclQuote.supReference}" maxlength="100"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Client</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="supplierClient" name="supplierClient" maxlength="100"/>
                            <input type="checkbox" id="newSupplierClient" name="newSupplierClient"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">City</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="newSupplierClientCity" name="newSupplierClientCity" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">POA</cong:td>
                        <cong:td><label id="supplierPoaId" style="font-weight: bold"/>
                        </cong:td>
                    </cong:tr>
                </c:if>
            </cong:table>
        </cong:td><!-- Supplier ends here -->
        <cong:td width="15%" valign="top">
            <cong:table width="100%">
                <cong:tr>
                    <cong:td colspan="2"  styleClass="report-header">${lclQuoteForm.moduleName ne 'Imports' ? "Shipper" :"House Shipper"}</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="textlabelsBoldforlcl">Code</cong:td>
                    <cong:td>
                        <cong:text  id="shipperCode" name="shipAcct"  styleClass="textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclQuote.shipAcct.accountno}"/>
                        <c:if test="${lclQuoteForm.moduleName eq 'Imports'}">
                            <img src="${path}/jsps/LCL/images/folder_open.png" style="vertical-align: middle"  width="16" height="16" alt="details"
                                 title="Address Details" Class="tradingDetails" onclick="displayTradingDetails('shipperCode', 'shipperName', 'dupShipperName', 'Shipper', 'shipperAddress', 'shipperCity', 'shipperState', 'shipperCountry', 'shipperPhone', 'shipperFax', 'shipperEmail', 'shipperZip', 'newShipper', 'shipperSalesPersonCode', 'shipperColoadRetailRadio')"/>
                            <label  id="shipperCreditClient"  style="color: red;font-weight: bold;">${CreditForShipper}</label>
                        </c:if>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="textlabelsBoldforlcl">Name</cong:td>
                    <cong:td>
                        <cong:div>
                            <span id="dojoShipper">
                                <c:choose>
                                    <c:when test="${lclQuoteForm.moduleName ne 'Imports'}">
                                        <cong:autocompletor name="shipContact.companyName" template="tradingPartner" id="shipperName"
                                                            fields="shipperCode,shipper_acct_type,shipper_sub_type,shipBsEciAcctNo,shipBsEciFwNo,NULL,shipperSalesPersonCode,shipperDisabled,shipperAddress,shipperCity,shipperState,shipperCountry,shipperZip,NULL,shipperPhone,shipperFax,shipperEmail,NULL,NULL,shipperPoa,shipperCredit,NULL,shipDisableAcct,shipColoadNo,shipColoadDesc,shipRetailNo,shipRetailDesc"
                                                            query="SHIPPER"  width="600" container="NULL" shouldMatch="true" value="${lclQuote.shipContact.companyName}"  styleClass="textlabelsBoldForTextBox textCap" paramFields="shipperSearchState,shipperSearchZip,shipperSearchSalesCode,CountryUnLocCode"
                                                            callback="shipperAcctType();changeShipperValuesToClient();checkRateTypeDetails($('#shipperCode').val(), 'S');" scrollHeight="300px"/>
                                    </c:when>
                                    <c:otherwise>
                                        <cong:autocompletor name="shipContact.companyName" template="tradingPartner" id="shipperName"
                                                            fields="shipperCode,shipper_acct_type,shipper_sub_type,NULL,NULL,NULL,shipperSalesPersonCode,shipperDisabled,shipperAddress,shipperCity,shipperState,shipperCountry,shipperZip,NULL,shipperPhone,shipperFax,shipperEmail,NULL,NULL,NULL,shipperPoa,shipperCredit,NULL,NULL,NULL,NULL,NULL,shipDisableAcct"
                                                            query="TP_IMPORTS_NOTE"  width="600" container="NULL" shouldMatch="true" value="${lclQuote.shipContact.companyName}"  styleClass="textlabelsBoldForTextBox textCap" paramFields="shipperSearchState,shipperSearchZip,shipperSearchSalesCode,CountryUnLocCode"
                                                            callback="shipper_AccttypeCheck();notesCount('${path}','shpNotes',$('#shipperCode').val(),'S');showShipDetails();addQuoteClientHotCodes($('#shipperCode').val());" scrollHeight="300px"/>
                                    </c:otherwise>
                                </c:choose>
                                <cong:hidden name="shipper_contactName" id="shipperContactName" value="${lclQuote.shipContact.contactName}"/>
                                <input type="hidden" name="shipperCredit" id="shipperCredit" value="${CreditForShipper}"/>
                            </span>
                            <span id="manualShipper"  style="display:none">
                                <cong:text name="shipContactName" id="dupShipperName"  value="${lclQuoteForm.shipContactName}" styleClass="text textlabelsBoldForTextBox textCap" onchange="newShipperChange()"/></span>
                            <input type="checkbox" id="newShipper" name="newShipper" onclick="newShipperName();" style="vertical-align: middle" title="New"/>
                            <c:if test="${lclQuoteForm.moduleName eq 'Imports'}">
                                <c:choose>
                                    <c:when test="${lclQuoteForm.shipperIcon}">
                                        <img src="${path}/img/icons/e_contents_view1.gif" style="vertical-align: middle" width="14" height="14" title="Click here to see Shipper Notes"
                                             id="shpNotes" class="greenShipIcon" onclick="displayNotes('${path}', $('#shipperCode').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${path}/img/icons/e_contents_view.gif" style="vertical-align: middle" width="14" height="14" title="Click here to see Shipper Notes"
                                             id="shpNotes" class="normalShipIcon" onclick="displayNotes('${path}', $('#shipperCode').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                                    </c:otherwise>
                                </c:choose>
                                <input type="hidden" id="shipperAddress" name="shipContact.address" value="${lclQuote.shipContact.address}"/>
                                <input type="hidden" id="shipperCity" name="shipContact.city" value="${lclQuote.shipContact.city}"/>
                                <input type="hidden" id="shipperState" name="shipContact.state" value="${lclQuote.shipContact.state}"/>
                                <input type="hidden" id="shipperZip" name="shipContact.zip" value="${lclQuote.shipContact.zip}"/>
                                <input type="hidden" id="shipperCountry" name="shipContact.country" value="${lclQuote.shipContact.country}"/>
                                <input type="hidden" id="shipperPhone" name="shipContact.phone1" value="${lclQuote.shipContact.phone1}"/>
                                <input type="hidden" id="shipperFax" name="shipContact.fax1" value="${lclQuote.shipContact.fax1}"/>
                                <input type="hidden" id="shipperEmail" name="shipContact.email1" value="${lclQuote.shipContact.email1}"/>
                                <input type="hidden" id="shipperSalesPersonCode" name="shipContact.salesPersonCode" value="${lclQuote.shipContact.salesPersonCode}"/>
                                <input type="hidden" id="shipUnlocCode" />
                                <input type="hidden" id="shipperColoadRetailRadio" name="shipperColoadRetailRadio" />
                                <cong:img src="${path}/img/icons/iicon.png" width="16" style="vertical-align: middle" height="16" alt="Client" id="shipper"/>
                                <img src="${path}/images/icons/search_filter.png" style="vertical-align: middle" class="clientSearchEdit" title="Click here to edit House Shipper Search options" onclick="showClientSearchOption('${path}', 'Shipper')"/>
                            </c:if>
                            <input type="hidden" name="shipper_accttype" id="shipper_acct_type"/>
                            <input type="hidden" name="shipper_subtype" id="shipper_sub_type"/>
                            <input type="hidden" name="shipDisableAcct" id="shipDisableAcct"/>
                            <input type="hidden" name="shipperDisabled" id="shipperDisabled"/>
                            <input type="hidden" name="shipperSearchState" id="shipperSearchState"/>
                            <input type="hidden" name="shipperSearchZip" id="shipperSearchZip"/>
                            <input type="hidden" name="shipperSearchSalesCode" id="shipperSearchSalesCode"/>
                            <input type="hidden" name="shipperSearchCountry" id="shipperSearchCountry"/>
                            <input type="hidden" name="CountryUnLocCode" id="CountryUnLocCode"/>
                            <cong:img src="${path}/jsps/LCL/images/display.gif" style="vertical-align: middle" width="16" height="16" alt="display"  styleClass="contactR" onclick="openLclContactInfo('${path}','Shipper')"/>
                            <c:choose>
                                <c:when test="${lclQuoteForm.moduleName ne 'Imports'}">
                                    <cong:img src="${path}/jsps/LCL/images/add2.gif" style="vertical-align: middle;" 
                                              id="shipperIcon"  width="16" height="16" alt="display" styleClass="trading" 
                                              onclick="newTPAccountFromQuote('${path}','dupShipperName','shipperAddress','S','shipperCountry','shipperCity',
                                              'shipperState','shipperZip','shipperPhone','shipperFax','shipperEmail','shipperCode','shipperCodeClient','shipperUnLocCode','newShipper')">
                                    </cong:img>
                                </c:when>
                                <c:otherwise>
                                    <cong:img src="${path}/jsps/LCL/images/add2.gif" style="vertical-align: middle"  width="16" height="16" 
                                              alt="display"  styleClass="trading" onclick="createNewTPAcct('${path}','LCL_IMP_QUOTE_SHIPPER','S','newShipper','dupShipperName','shipperAddress','shipperCity','shipperState','shipUnlocCode','shipperPhone','shipperFax','shipperEmail','shipperZip','shipperSalesPersonCode','shipperColoadRetailRadio')"/>
                                </c:otherwise>
                            </c:choose>

                        </cong:div> 
                    </cong:td>
                </cong:tr>
                <c:if test="${lclQuoteForm.moduleName ne 'Imports'}">
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Address</cong:td>
                        <cong:td>
                            <cong:textarea cols="30" rows="6" id="shipperAddress" name="shipContact.address"
                                           value="${lclQuote.shipContact.address}"  styleClass="textlabelsBoldForTextBox textup"
                                           onchange="changeShipperValuesToClient()"/>
                            <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">City</cong:td>
                        <cong:td>
                            <cong:autocompletor id="shipperCity" name="shipContact.city" template="three"  
                                                fields="shipperCity,shipperCountry,shipperUnLocCode,shipperState"
                                                styleClass="textlabelsBoldForTextBox textCap"  query="PORT_CITY_STATE"  
                                                width="600" container="NULL" value="${lclQuote.shipContact.city}" 
                                                shouldMatch="true" callback="changeShipperValuesToClient()" scrollHeight="300">
                            </cong:autocompletor>
                            <input type="hidden" id="shipperUnLocCode" name="shipperUnLocCode"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl"> 	 State </cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="shipperState" name="shipContact.state"
                                       value="${lclQuote.shipContact.state}" maxlength="50" onchange="changeShipperValuesToClient()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Zip</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="shipperZip" name="shipContact.zip"
                                       value="${lclQuote.shipContact.zip}" maxlength="50" onchange="changeShipperValuesToClient()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl"> Country</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="shipperCountry" name="shipContact.country"
                                       value="${lclQuote.shipContact.country}" maxlength="50" onchange="changeShipperValuesToClient()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Phone</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="shipperPhone" name="shipContact.phone1"
                                       value="${lclQuote.shipContact.phone1}" maxlength="50" onchange="changeShipperValuesToClient()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Fax</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="shipperFax" name="shipContact.fax1"
                                       value="${lclQuote.shipContact.fax1}" maxlength="50" onchange="changeShipperValuesToClient()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Email</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="shipperEmail"
                                       name="shipContact.email1" value="${lclQuote.shipContact.email1}" maxlength="100" onchange="changeShipperValuesToClient()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Client Ref</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="shipperClientRef"
                                   name="shipReference" value="${lclQuote.shipReference}" maxlength="100"  onchange="ShipReferences()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Client</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="shipperClient" name="shipperClient" maxlength="50"/>
                            <input type="checkbox" id="newShipperClient" name="newShipperClient"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">City</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="newShipperClientCity" name="newShipperClientCity" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">POA</cong:td>
                        <cong:td>
                            <label id="shipperPoaId" style="font-weight: bold"/>
                        </cong:td>
                    </cong:tr>
                </c:if>
            </cong:table>
        </cong:td><!-- Shippers ends here -->
        <c:if test="${lclQuoteForm.moduleName ne 'Imports'}">
            <cong:td valign="top">
                <cong:table width="100%">
                    <cong:tr>
                        <cong:td colspan="2"  styleClass="report-header"> Forwarder</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Code</cong:td>
                        <cong:td>
                            <cong:text id="forwarderCode" name="fwdAcct"  styleClass="textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclQuote.fwdAcct.accountno}"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Name</cong:td>
                        <cong:td>
                            <cong:autocompletor name="fwdContact.companyName" template="tradingPartner" id="forwarderName"
                                                fields="forwarderCode,forwarder_acct_type,forwarder_sub_type,fwdBsEciAcctNo,fwdBsEciFwNo,NULL,NULL,forwardDisabled,forwarderAddress,forwarderCity,forwarderState,forwarderCountry,forwarderZip,NULL,forwarderPhone,forwarderFax,forwarderEmail,NULL,NULL,forwarderPoa,forwarderCredit,NULL,forwardDisabledAcct,fwdColoadNo,fwdColoadDesc,fwdRetailNo,fwdRetailDesc"
                                                query="FORWARDER"  width="600" container="NULL" value="${lclQuote.fwdContact.companyName}"  styleClass="textlabelsBoldForTextBox textCap"
                                                shouldMatch="true" callback="forwarder_AccttypeCheck();copyForwarderDetailstoClient();checkRateTypeDetails($('#forwarderCode').val(), 'F');" scrollHeight="300px"/>
                            <cong:img src="${path}/jsps/LCL/images/display.gif" width="16" height="16" alt="display"  styleClass="supplierContact" onclick="getForwarder('${path}',this.value)"/>

                            <%-- <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" height="16" alt="display"  
                                       styleClass="trading" onclick="openTradingPartner('${path}','forwarderName','FR')"/>--%>

                            <input type="hidden" name="forwardDisabled" id="forwardDisabled"/>
                            <input type="hidden" name="forwardDisabledAcct" id="forwardDisabledAcct"/>
                        </cong:td>

                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Address</cong:td>
                        <cong:td>
                            <cong:textarea cols="30" rows="6" id="forwarderAddress" name="fwdContact.address"
                                           value="${lclQuote.fwdContact.address}"  styleClass="textlabelsBoldForTextBox textup" onchange="copyForwarderDetailstoClient()"/>
                            <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">City</cong:td>
                        <cong:td>
                            <cong:autocompletor id="forwarderCity" name="fwdContact.city" template="three"  
                                                fields="forwarderCity,forwarderCountry,fwdUnLocCode,forwarderState"
                                                styleClass="textlabelsBoldForTextBox textCap"  query="PORT_CITY_STATE"  
                                                width="600" container="NULL" value="${lclQuote.fwdContact.city}" 
                                                shouldMatch="true" callback="copyForwarderDetailstoClient()" scrollHeight="300">
                            </cong:autocompletor>
                            <input type="hidden" id="fwdUnLocCode" name="fwdUnLocCode"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl"> 	 State </cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="forwarderState"
                                       name="fwdContact.state" value="${lclQuote.fwdContact.state}" maxlength="50" onchange="copyForwarderDetailstoClient()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Zip</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="forwarderZip"
                                       name="fwdContact.zip" value="${lclQuote.fwdContact.zip}" maxlength="50" onchange="copyForwarderDetailstoClient()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Country</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="forwarderCountry"
                                       name="fwdContact.country" value="${lclQuote.fwdContact.country}" maxlength="50" onchange="copyForwarderDetailstoClient()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Phone</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="forwarderPhone"
                                       name="fwdContact.phone1" value="${lclQuote.fwdContact.phone1}" maxlength="50" onchange="copyForwarderDetailstoClient()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Fax</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="forwarderFax"
                                       name="fwdContact.fax1" value="${lclQuote.fwdContact.fax1}" maxlength="50" onchange="copyForwarderDetailstoClient()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Email</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="forwarderEmail"
                                       name="fwdContact.email1" value="${lclQuote.fwdContact.email1}" maxlength="100" onchange="copyForwarderDetailstoClient()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Client Ref</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1"
                                   id="forwarderClientRef" name="fwdReference" value="${lclQuote.fwdReference}" maxlength="50" onchange="ClientRefForwarders()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Client</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1"
                                   id="forwarderClient" name="forwarderClient" maxlength="50"/>
                            <input type="checkbox" id="newForwarderClient" name="newForwarderClient"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">City</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="newForwarderClientCity" name="newForwarderClientCity" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">POA</cong:td>
                        <cong:td>
                            <span id="forwarder" style="font-weight: bold"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td><!-- Forwarder ends here -->
        </c:if>
        <cong:td width="15%" valign="top">
            <cong:table width="100%">
                <cong:tr>
                    <cong:td colspan="2"  styleClass="report-header">${lclQuoteForm.moduleName ne 'Imports' ? "Consignee":"House Consignee"}</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="textlabelsBoldforlcl">Code</cong:td>
                    <cong:td>
                        <cong:text id="consigneeCode" name="consAcct"  styleClass="textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclQuote.consAcct.accountno}"/>
                        <c:if test="${lclQuoteForm.moduleName eq 'Imports'}">
                            <img src="${path}/jsps/LCL/images/folder_open.png" width="16" style="vertical-align: middle" title="Address Details"
                                 height="16" alt="details" class="tradingDetails" onclick="displayTradingDetails('consigneeCode', 'consigneeName', 'dupConsigneeName', 'Consignee', 'consigneeAddress', 'consigneeCity', 'consigneeState', 'consigneeCountry', 'consigneePhone', 'consigneeFax', 'consigneeEmail', 'consigneeZip', 'newConsignee', 'consigneeSalesPersonCode', 'consigneeColoadRetailRadio')"/>
                            <label  id="consigneeCreditClient"  style="color: red;vertical-align: middle;font-weight: bold;">${CreditForConsignee}</label>
                        </c:if>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="textlabelsBoldforlcl">Name</cong:td>
                    <cong:td>
                        <span id="dojoConsignee">
                            <c:choose>
                                <c:when test="${lclQuoteForm.moduleName ne 'Imports'}">
                                    <cong:autocompletor name="consContact.companyName" template="tradingPartner" id="consigneeName"
                                                        fields="consigneeCode,consignee_acct_type,consignee_sub_type,consBsEciAcctNo,consBsEciFwNo,NULL,consigneeSalesPersonCode,consDisabled,consigneeAddress,consigneeCity,consigneeState,consigneeCountry,consigneeZip,NULL,consigneePhone,consigneeFax,consigneeEmail,NULL,NULL,consigneePoa,consigneeCredit,NULL,NULL,consDisableAcct,consColoadNo,consColoadDesc,consRetailNo,consRetailDesc"
                                                        query="EXP_CONSIGNEE"  width="600" container="NULL" value="${lclQuote.consContact.companyName}"
                                                        shouldMatch="true" callback="consigneeAccttype();copyConsigneeToClientSection();checkRateTypeDetails($('#consigneeCode').val(), 'C');" scrollHeight="300px"  styleClass="textlabelsBoldForTextBox textCap"/>

                                </c:when>
                                <c:otherwise>
                                    <cong:autocompletor name="consContact.companyName" template="tradingPartner" id="consigneeName" fields="consigneeCode,consignee_acct_type,consignee_sub_type,NULL,NULL,NULL,consigneeSalesPersonCode,consDisabled,consigneeAddress,consigneeCity,consigneeState,consigneeCountry,consigneeZip,NULL,consigneePhone,consigneeFax,consigneeEmail,coloadCommNo,NULL,NULL,consigneePoa,consigneeCredit,NULL,NULL,NULL,NULL,NULL,consDisableAcct"
                                                        query="IMPORTS_TP_NOTE"  width="600" container="NULL" value="${lclQuote.consContact.companyName}"
                                                        shouldMatch="true" paramFields="consigneeSearchState,consigneeSearchZip,consigneeSearchSalesCode,consigneeUnLocCode"
                                                        callback="consignee_AccttypeCheck();notesCount('${path}','conNotes',$('#consigneeCode').val(),'C');showConsDetails();checkErtAndRates('Consignee');addQuoteClientHotCodes($('#consigneeCode').val());" scrollHeight="300px"  styleClass="textlabelsBoldForTextBox textCap"/>
                                </c:otherwise>
                            </c:choose>
                            <input type="hidden" name="coloadCommNo" id="coloadCommNo" value="${lclQuote.consAcct.generalInfo.genericCode.code}"/>
                            <input type="hidden" name="existinColoadCommon" id="existinColoadCommon" value="${lclQuote.consAcct.generalInfo.genericCode.code}"/>
                        </span>
                        <span id="manualConsignee"  style="display:none">
                            <cong:text name="consContactName" id="dupConsigneeName" value="${lclQuoteForm.consContactName}"
                                       styleClass="text textlabelsBoldForTextBox textCap" onchange="newConsigneeChange()"/></span>
                        <input type="checkbox" id="newConsignee" name="newConsignee" style="vertical-align: middle" onclick="newConsigneeName();" title="New"/>
                        <c:if test="${lclQuoteForm.moduleName eq 'Imports'}">
                            <c:choose>
                                <c:when test="${lclQuoteForm.consigneeIcon}">
                                    <img src="${path}/img/icons/e_contents_view1.gif" style="vertical-align: middle" width="14" height="14" title="Click here to see Consignee Notes"
                                         id="conNotes" onclick="displayNotes('${path}', $('#consigneeCode').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="${path}/img/icons/e_contents_view.gif" style="vertical-align: middle" width="14" height="14" title="Click here to see Consignee Notes"
                                         id="conNotes" onclick="displayNotes('${path}', $('#consigneeCode').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                                </c:otherwise>
                            </c:choose>
                            <input type="hidden" id="consigneeAddress" name="consContact.address" value="${lclQuote.consContact.address}"/>
                            <input type="hidden" id="consigneeCity" name="consContact.city" value="${lclQuote.consContact.city}"/>
                            <input type="hidden" id="consigneeState" name="consContact.state" value="${lclQuote.consContact.state}"/>
                            <input type="hidden" id="consigneeZip" name="consContact.zip" value="${lclQuote.consContact.zip}"/>
                            <input type="hidden" id="consigneeCountry" name="consContact.country" value="${lclQuote.consContact.country}"/>
                            <input type="hidden" id="consigneePhone" name="consContact.phone1" value="${lclQuote.consContact.phone1}"/>
                            <input type="hidden" id="consigneeFax" name="consContact.fax1" value="${lclQuote.consContact.fax1}"/>
                            <input type="hidden" id="consigneeEmail" name="consContact.email1" value="${lclQuote.consContact.email1}"/>
                            <input type="hidden" id="consigneeSalesPersonCode" name="consContact.salesPersonCode" value="${lclQuote.consContact.salesPersonCode}"/>
                            <input type="hidden"  id="consigneeColoadRetailRadio" name="consigneeColoadRetailRadio" />
                            <cong:img src="${path}/img/icons/iicon.png" width="16" style="vertical-align: middle"  height="16" alt="Client" id="consContact"/>
                            <img src="${path}/images/icons/search_filter.png" style="vertical-align: middle" class="clientSearchEdit" title="Click here to Edit House Consignee Search options" onclick="showClientSearchOption('${path}', 'Consignee')"/>
                        </c:if>
                        <cong:img src="${path}/jsps/LCL/images/display.gif" style="vertical-align: middle" width="16" height="16" alt="display"  styleClass="contactR" onclick="openLclContactInfo('${path}','Consignee')"/>
                        <c:choose>
                            <c:when test="${lclQuoteForm.moduleName eq 'Imports'}">
                                <cong:img src="${path}/jsps/LCL/images/add2.gif" id="consigneeIcon" style="vertical-align: middle"  width="16" height="16" alt="display"  styleClass="trading" 
                                          onclick="createNewTPAcct('${path}','LCL_IMP_QUOTE_CONSIGNEE','C','newConsignee','dupConsigneeName','consigneeAddress','consigneeCity',
                                          'consigneeState','consigneeUnLocCode','consigneePhone','consigneeFax','consigneeEmail','consigneeZip','consigneeSalesPersonCode',
                                          'consigneeColoadRetailRadio')"/>
                            </c:when>
                            <c:otherwise>
                                <cong:img src="${path}/jsps/LCL/images/add2.gif" id="consigneeIcon" style="vertical-align: middle"  width="16" height="16" alt="display"  styleClass="trading"
                                          onclick="newTPAccountFromQuote('${path}','dupConsigneeName','consigneeAddress','C','consigneeCountry','consigneeCity',
                                          'consigneeState','consigneeZip','consigneePhone','consigneeFax','consigneeEmail','consigneeCode','consigneeCodeClient','conisUnLocCode','newConsignee')">
                                </cong:img>
                            </c:otherwise>
                        </c:choose>
                        <input type="hidden" name="consDisabled" id="consDisabled"/>
                        <input type="hidden" name="consDisableAcct" id="consDisableAcct"/>
                        <input type="hidden" name="consignee_accttype" id="consignee_acct_type"/>
                        <input type="hidden" name="consignee_subtype" id="consignee_sub_type"/>
                        <input type="hidden" name="consigneeCredit" id="consigneeCredit"  value="${CreditForConsignee}"/>
                        <input type="hidden" name="consigneePoa" id="consigneePoa" value="${lclQuote.consAcct.generalInfo.poa}"/>
                        <input type="hidden" name="consigneeSearchState" id="consigneeSearchState"/>
                        <input type="hidden" name="consigneeSearchZip" id="consigneeSearchZip"/>
                        <input type="hidden" name="consigneeSearchSalesCode" id="consigneeSearchSalesCode"/>
                        <input type="hidden" id="consigneeUnLocCode"/>
                    </cong:td>
                </cong:tr>
                <c:if test="${lclQuoteForm.moduleName ne 'Imports'}">
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Address</cong:td>
                        <cong:td>
                            <cong:textarea cols="30" rows="6" id="consigneeAddress" name="consContact.address" value="${lclQuote.consContact.address}"
                                           styleClass="textlabelsBoldForTextBox textup" onchange="copyConsigneeToClientSection()"/>
                            <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                        </cong:td>
                    </cong:tr>

                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">City</cong:td>
                        <cong:td>
                            <cong:autocompletor id="consigneeCity" name="consContact.city" template="three"  
                                                fields="consigneeCity,consigneeCountry,conisUnLocCode,consigneeState"
                                                styleClass="textlabelsBoldForTextBox textCap"  query="PORT_CITY_STATE"  
                                                width="600" container="NULL" value="${lclQuote.consContact.city}" 
                                                shouldMatch="true" callback="copyConsigneeToClientSection()" scrollHeight="300">
                            </cong:autocompletor>
                            <input type="hidden" id="conisUnLocCode" name="conisUnLocCode"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl"> 	 State </cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneeState" name="consContact.state"
                                       value="${lclQuote.consContact.state}" maxlength="50" onchange="copyConsigneeToClientSection()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Zip</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneeZip" name="consContact.zip"
                                       value="${lclQuote.consContact.zip}" maxlength="50" onchange="copyConsigneeToClientSection()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Country</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneeCountry" name="consContact.country"
                                       value="${lclQuote.consContact.country}" maxlength="50" onchange="copyConsigneeToClientSection()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Phone</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneePhone" name="consContact.phone1"
                                       value="${lclQuote.consContact.phone1}" maxlength="50" onchange="copyConsigneeToClientSection()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Fax</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneeFax" name="consContact.fax1"
                                       value="${lclQuote.consContact.fax1}" maxlength="50" onchange="copyConsigneeToClientSection()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Email</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneeEmail" name="consContact.email1"
                                       value="${lclQuote.consContact.email1}" maxlength="100" onchange="copyConsigneeToClientSection()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Client Ref</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="consigneeClientRef"
                                   name="consReference" value="${lclQuote.consReference}" maxlength="50" onchange="clientRefConsignees()"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Client</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="consigneeClient" name="consigneeClient" maxlength="50"/>
                            <input type="checkbox" id="newConsigneeClient" name="newConsigneeClient"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">City</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="newConsigneeClientCity" name="newConsigneeClientCity" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">POA</cong:td>
                        <cong:td>
                            <label id="consigneePoaId" style="font-weight: bold"/>
                        </cong:td>
                    </cong:tr>
                </c:if>
            </cong:table>
        </cong:td><!-- Consignee ends here -->
        <c:choose>
            <c:when test="${lclQuoteForm.moduleName eq 'Imports'}">
                <c:set var="notyCustomerNotes" value="notesCount('${path}','notNotes',$('#notifyCode').val(),'N');showNotyDetails();checkErtAndRates('Notify');"/>
            </c:when>
            <c:otherwise>
                <c:set var="notyCustomerNotes" value=""/>
            </c:otherwise>
        </c:choose>
        <cong:td width="20%" valign="top">
            <cong:table width="100%">
                <cong:tr>
                    <cong:td colspan="2"  styleClass="report-header"> Notify</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="textlabelsBoldforlcl">Code</cong:td>
                    <cong:td>
                        <cong:text id="notifyCode" name="notyAcct"  styleClass="textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclQuote.notyAcct.accountno}"/>
                        <c:if test="${lclQuoteForm.moduleName eq 'Imports'}">
                            <img src="${path}/jsps/LCL/images/folder_open.png" style="vertical-align: middle"  width="16" height="16" alt="details" title="Address Details"
                                 class="tradingDetails" onclick="displayTradingDetails('notifyCode', 'notifyName', 'dupNotifyName', 'Notify', 'notifyAddress', 'notifyCity', 'notifyState', 'notifyCountry', 'notifyPhone', 'notifyFax', 'notifyEmail', 'notifyZip', 'newNotify', 'notifySalesPersonCode', 'notifyColoadRetailRadio')"/>
                            <label  id="notifyCreditClient"  style="color: red;vertical-align: middle;font-weight: bold;">${CreditForNotify}</label>
                        </c:if>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="textlabelsBoldforlcl">Name</cong:td>
                    <cong:td>
                        <span id="dojoNotify">
                            <c:choose>
                                <c:when test="${lclQuoteForm.moduleName ne 'Imports'}">
                                    <cong:autocompletor name="notyContact.companyName" template="tradingPartner" id="notifyName" fields="notifyCode,notify_acct_type,notify_sub_type,NULL,NULL,NULL,notifySalesPersonCode,notifyDisabled,notifyAddress,notifyCity,notifyState,notifyCountry,notifyZip,NULL,notifyPhone,notifyFax,notifyEmail,notifyCommNo,NULL,NULL,notifyPoa,notifyCredit,NULL,NULL,NULL,NULL,NULL,NULL,notyDisableAcct"
                                                        query="CONSIGNEE_NOTE"  width="600" container="NULL" value="${lclQuote.notyContact.companyName}" position="left" paramFields="notifySearchState,notifySearchZip,notifySearchSalesCode,notifyUnLocCode"
                                                        shouldMatch="true" callback="notifyAccttype();${notyCustomerNotes}" scrollHeight="300px"  styleClass="textlabelsBoldForTextBox textCap"/>

                                </c:when>
                                <c:otherwise>
                                    <cong:autocompletor name="notyContact.companyName" template="tradingPartner" id="notifyName" fields="notifyCode,notify_acct_type,notify_sub_type,NULL,NULL,NULL,notifySalesPersonCode,notifyDisabled,notifyAddress,notifyCity,notifyState,notifyCountry,notifyZip,NULL,notifyPhone,notifyFax,notifyEmail,notifyCommNo,NULL,NULL,notifyPoa,notifyCredit,NULL,NULL,NULL,NULL,NULL,NULL,notyDisableAcct"
                                                        query="CONSIGNEE_NOTE"  width="600" container="NULL" value="${lclQuote.notyContact.companyName}" position="left" paramFields="notifySearchState,notifySearchZip,notifySearchSalesCode,notifyUnLocCode"
                                                        shouldMatch="true" callback="notifyAccttype();addQuoteClientHotCodes($('#notifyCode').val());${notyCustomerNotes}" scrollHeight="300px"  styleClass="textlabelsBoldForTextBox textCap"/>
                                </c:otherwise>
                            </c:choose>
                            <input type="hidden" name="notifyDisabled" id="notifyDisabled"/>
                            <input type="hidden" name="notyDisableAcct" id="notyDisableAcct"/>
                            <input type="hidden" name="notify_accttype" id="notify_acct_type"/>
                            <input type="hidden" name="notify_subtype" id="notify_sub_type"/>
                            <input type="hidden" name="notifyCredit" id="notifyCredit" value="${CreditForNotify}"/>
                            <input type="hidden" name="notifyCommNo" id="notifyCommNo"/>
                        </span>
                        <span id="manualNotify"  style="display:none">
                            <cong:text name="notifyContactName" id="dupNotifyName" value="${lclQuoteForm.notifyContactName}"  styleClass="text textlabelsBoldForTextBox textCap"/></span>
                        <input type="checkbox" id="newNotify" name="newNotify" onclick="newNotifyName();"  style="vertical-align: middle" title="New"/>
                        <c:if test="${lclQuoteForm.moduleName eq 'Imports'}">
                            <c:choose>
                                <c:when test="${lclQuoteForm.notifyIcon}">
                                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" style="vertical-align: middle" height="14" title="Click here to see Consignee Notes"
                                         id="notNotes" onclick="displayNotes('${path}', $('#notifyCode').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="${path}/img/icons/e_contents_view.gif" style="vertical-align: middle" width="14" height="14" title="Click here to see Consignee Notes"
                                         id="notNotes" onclick="displayNotes('${path}', $('#notifyCode').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                                </c:otherwise>
                            </c:choose>
                            <input type="hidden" name="notifySearchState" id="notifySearchState"/>
                            <input type="hidden" name="notifySearchZip" id="notifySearchZip"/>
                            <input type="hidden" name="notifySearchSalesCode" id="notifySearchSalesCode"/>
                            <input type="hidden" id="notifyAddress" name="notyContact.address" value="${lclQuote.notyContact.address}"/>
                            <input type="hidden" id="notifyCity" name="notyContact.city" value="${lclQuote.notyContact.city}"/>
                            <input type="hidden" id="notifyState" name="notyContact.state" value="${lclQuote.notyContact.state}"/>
                            <input type="hidden" id="notifyZip" name="notyContact.zip" value="${lclQuote.notyContact.zip}"/>
                            <input type="hidden" id="notifyCountry" name="notyContact.country" value="${lclQuote.notyContact.country}"/>
                            <input type="hidden" id="notifyPhone" name="notyContact.phone1" value="${lclQuote.notyContact.phone1}"/>
                            <input type="hidden" id="notifyFax" name="notyContact.fax1" value="${lclQuote.notyContact.fax1}"/>
                            <input type="hidden" id="notifyEmail" name="notyContact.email1" value="${lclQuote.notyContact.email1}"/>
                            <input type="hidden" id="notifySalesPersonCode" name="notyContact.salesPersonCode" value="${lclQuote.notyContact.salesPersonCode}"/>
                            <input type="hidden" id="notifyUnLocCode"/>
                            <input type="hidden"  id="notifyColoadRetailRadio" name="notifyColoadRetailRadio" />
                            <cong:img src="${path}/img/icons/iicon.png" width="16" style="vertical-align: middle" height="16" alt="Client" id="noty"/>
                            <img src="${path}/images/icons/search_filter.png" style="vertical-align: middle" class="clientSearchEdit" title="Click here to edit Notify Search options" onclick="showClientSearchOption('${path}', 'Notify')"/>
                        </c:if>
                        <cong:img src="${path}/jsps/LCL/images/display.gif" style="vertical-align: middle" width="16" height="16" alt="display"  styleClass="contactR" onclick="openLclContactInfo('${path}','Notify')"/>
                        <c:choose>
                            <c:when test="${lclQuoteForm.moduleName eq 'Imports'}">
                                <cong:img src="${path}/jsps/LCL/images/add2.gif" style="vertical-align: middle"  
                                          width="16" id="notifyIcon" height="16" alt="display"  styleClass="trading" 
                                          onclick="createNewTPAcct('${path}','LCL_IMP_QUOTE_NOTIFY','C','newNotify','dupNotifyName','notifyAddress',
                                          'notifyCity','notifyState','notifyUnLocCode','notifyPhone','notifyFax','notifyEmail','notifyZip',
                                          'notifySalesPersonCode','notifyColoadRetailRadio')"/>
                            </c:when>
                            <c:otherwise>
                                <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" id="notifyIcon" height="16" style="vertical-align: middle;" alt="display" styleClass="trading" 
                                          onclick="newTPAccountFromQuote('${path}','dupNotifyName','notifyAddress','C','notifyCountry','notifyCity',
                                          'notifyState','notifyZip','notifyPhone','notifyFax','notifyEmail','notifyCode','notifyCode','notyUnLocCode','newNotify')">
                                </cong:img>
                            </c:otherwise>
                        </c:choose>
                        <input type="hidden" name="notifyPoa" id="notifyPoa" value="${lclQuote.notyAcct.generalInfo.poa}"/>
                    </cong:td>
                </cong:tr>
                <c:if test="${lclQuoteForm.moduleName ne 'Imports'}">
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Address</cong:td>
                        <cong:td>
                            <cong:textarea cols="30" rows="6" id="notifyAddress" name="notyContact.address" value="${lclQuote.notyContact.address}"  styleClass="textlabelsBoldForTextBox textup"/>
                            <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                        </cong:td>
                    </cong:tr>

                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">City</cong:td>
                        <cong:td>
                            <cong:autocompletor id="notifyCity" name="notyContact.city" template="three"  
                                                fields="notifyCity,notifyCountry,notyUnLocCode,notifyState"
                                                styleClass="textlabelsBoldForTextBox textCap"  query="PORT_CITY_STATE"  
                                                width="600" container="NULL" value="${lclQuote.notyContact.city}" 
                                                shouldMatch="true" scrollHeight="300">
                            </cong:autocompletor>
                            <input type="hidden" id="notyUnLocCode" name="notyUnLocCode"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl"> 	 State </cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="notifyState" name="notyContact.state" value="${lclQuote.notyContact.state}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Zip</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="notifyZip" name="notyContact.zip" value="${lclQuote.notyContact.zip}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Country</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="notifyCountry" name="notyContact.country" value="${lclQuote.notyContact.country}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Phone</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="notifyPhone" name="notyContact.phone1" value="${lclQuote.notyContact.phone1}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Fax</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="notifyFax" name="notyContact.fax1" value="${lclQuote.notyContact.fax1}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Email</cong:td>
                        <cong:td>
                            <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="notifyEmail" name="notyContact.email1" value="${lclQuote.notyContact.email1}" maxlength="100"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Client Ref</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="notifyClientRef" name="notyReference" value="${lclQuote.notyReference}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">Client</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="notifyClient" name="notifyClient" maxlength="50"/>
                            <input type="checkbox" id="newNotifyClient" name="newNotifyClient"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">City</cong:td>
                        <cong:td>
                            <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="newNotifyClientCity" name="newNotifyClientCity" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="textlabelsBoldforlcl">POA</cong:td>
                        <cong:td>
                            <label id="notifyPoaId" style="font-weight: bold"/>
                        </cong:td>
                    </cong:tr>
                </c:if>
            </cong:table>

        </cong:td><!-- Notify ends here -->
        <c:if test="${lclQuoteForm.moduleName eq 'Imports'}">
            <cong:td width="25%" valign="top">
                <cong:table width="100%">
                    <cong:tr>
                        <cong:td colspan="2" styleClass="report-header">2nd Notify Party</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Code</cong:td>
                        <cong:td>
                            <cong:text id="notify2Code" name="notify2AcctNo" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclQuote.notify2Contact.tradingPartner.accountno}"/>
                            <img src="${path}/jsps/LCL/images/folder_open.png" style="vertical-align: middle" width="16" height="16" alt="details" title="Address Details"
                                 class="tradingDetails" onclick="displayTradingDetails('notify2Code', 'notify2Name', 'dupNotify2Name', 'Notify2', 'notify2Address', 'notify2City', 'notify2State', 'notify2Country', 'notify2Phone', 'notify2Fax', 'notify2Email', 'notify2Zip', 'newNotify2', 'notify2SalesPersonCode', 'notify2ColoadRetailRadio');"/>
                            <label  id="notify2CreditValues"   style="color: red;font-weight: bold">${creditForNotify2}</label>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Name</cong:td>
                        <cong:td>
                            <span id="dojoNotify2">
                                <cong:autocompletor name="notify2Contact.companyName" template="tradingPartner" id="notify2Name" fields="notify2Code,notify2AcctType,NULL,NULL,NULL,NULL,notify2SalesPersonCode,noty2Disabled,notify2Address,notify2City,notify2State,notify2Country,notify2Zip,NULL,notify2Phone,notify2Fax,notify2Email,NULL,NULL,NULL,NULL,notify2CreditDetails,NULL,NULL,NULL,NULL,NULL,noty2DisableAcct"
                                                    query="TP_IMPORTS_NOTE"  width="600" container="NULL" value="${lclQuote.notify2Contact.companyName}" position="left" styleClass="textlabelsBoldForTextBox textCap"
                                                    shouldMatch="true" paramFields="notify2SearchState,notify2SearchZip,notify2SearchSalesCode"  callback="notify2AccttypeCheck();addQuoteClientHotCodes($('#notify2Code').val());" scrollHeight="300px"/>
                                <input type="hidden" name="notify2AcctType" id="notify2AcctType"/>
                                <input type="hidden" name="noty2Disabled" id="noty2Disabled"/>
                                <input type="hidden" name="noty2DisableAcct" id="noty2DisableAcct"/>
                                <input type="hidden" name="notify2CreditDetails" id="notify2CreditDetails"/>
                            </span>
                            <span id="manualNotify2" style="display:none">
                                <cong:text name="notify2ContactName" id="dupNotify2Name" value="${lclQuoteForm.notify2ContactName}" styleClass="text textlabelsBoldForTextBox textCap"/></span>
                            <input type="checkbox" id="newNotify2" name="newNotify2" style="vertical-align: middle" onclick="newCheckNotify2();" title="New"/>
                            <c:choose>
                                <c:when test="${lclQuoteForm.notify2Icon}">
                                    <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Notify2 Notes" style="vertical-align: middle"
                                         id="noty2Notes" onclick="displayNotes('${path}', $('#notify2Code').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                                </c:when>
                                <c:otherwise>
                                    <img src="${path}/img/icons/e_contents_view.gif" width="14" style="vertical-align: middle" height="14" title="Click here to see Notify2 Notes"
                                         id="noty2Notes" onclick="displayNotes('${path}', $('#notify2Code').val(), id, $('#fileId').val(), $('#fileNo').val());"/>
                                </c:otherwise>
                            </c:choose>
                            <cong:img src="${path}/img/icons/iicon.png" width="16" style="vertical-align: middle" height="16" alt="Notify2" id="notify2"/>
                            <img src="${path}/images/icons/search_filter.png" style="vertical-align: middle" class="clientSearchEdit" title="Click here to edit Notify2 Search options" onclick="showClientSearchOption('${path}', 'Notify2')"/>
                            <input type="hidden" id="notify2Address" name="notify2Contact.address" value="${lclQuote.notify2Contact.address}"/>
                            <input type="hidden" id="notify2City" name="notify2Contact.city" value="${lclQuote.notify2Contact.city}"/>
                            <input type="hidden" id="notify2State" name="notify2Contact.state" value="${lclQuote.notify2Contact.state}"/>
                            <input type="hidden" id="notify2Zip" name="notify2Contact.zip" value="${lclQuote.notify2Contact.zip}"/>
                            <input type="hidden" id="notify2Country" name="notify2Contact.country" value="${lclQuote.notify2Contact.country}"/>
                            <input type="hidden" id="notify2Phone" name="notify2Contact.phone1" value="${lclQuote.notify2Contact.phone1}"/>
                            <input type="hidden" id="notify2Fax" name="notify2Contact.fax1" value="${lclQuote.notify2Contact.fax1}"/>
                            <input type="hidden" id="notify2Email" name="notify2Contact.email1" value="${lclQuote.notify2Contact.email1}"/>
                            <input type="hidden" id="notify2SalesPersonCode" name="notify2Contact.salesPersonCode" value="${lclQuote.notify2Contact.salesPersonCode}"/>
                            <input type="hidden" id="notify2UnLocCode"/>
                            <input type="hidden"id="notify2ColoadRetailRadio" name="notify2ColoadRetailRadio" />
                            <input type="hidden" name="notify2SearchState" id="notify2SearchState"/>
                            <input type="hidden" name="notify2SearchZip" id="notify2SearchZip"/>
                            <input type="hidden" name="notify2SearchSalesCode" id="notify2SearchSalesCode"/>
                            <cong:img src="${path}/jsps/LCL/images/display.gif" style="vertical-align: middle" width="16" height="16" alt="display"  styleClass="contactR" onclick="openLclContactInfo('${path}','Notify2')"/>
                            <cong:img src="${path}/jsps/LCL/images/add2.gif" style="vertical-align: middle"   width="16" id="nnn" height="16" alt="display" styleClass="trading" onclick="createNewTPAcct('${path}','LCL_IMP_QUOTE_NOTIFY2','C','newNotify2','dupNotify2Name','notify2Address','notify2City','notify2State','notify2UnLocCode','notify2Phone','notify2Fax','notify2Email','notify2Zip','notify2SalesPersonCode','notify2ColoadRetailRadio')"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td><!-- Notify 2ends here -->
        </c:if>
    </cong:tr>
</cong:table>
