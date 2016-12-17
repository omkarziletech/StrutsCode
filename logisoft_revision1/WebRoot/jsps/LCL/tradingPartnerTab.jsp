<%@include file="/taglib.jsp" %>
<div>
    <cong:table width="100%">
        <cong:tr>
            <cong:td width="15%" valign="top">
                <cong:table width="100%">
                    <cong:tr>
                        <cong:td colspan="2" styleClass="report-header">${lclBookingForm.moduleName ne 'Imports' ? "Supplier":"Origin Agent"}</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Code</cong:td>
                        <cong:td>
                            <cong:text  id="supplierCode" name="supAcct" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclBooking.supAcct.accountno}"/>
                            <c:if test="${lclBookingForm.moduleName eq 'Imports'}">
                                <img src="${path}/jsps/LCL/images/folder_open.png" title="Address Details" class="tradingDetails" style="vertical-align: middle;"
                                     width="16" height="16" alt="details" onclick="displayTradingDetails('${path}', '#supplierCode', '#dupsupplierCode', '${lclBooking.lclFileNumber.id}', 'agent')"/>
                            </c:if>
                        </cong:td>
                    </cong:tr>
                    <c:choose>
                        <c:when  test="${lclBookingForm.moduleName ne 'Imports'}">
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Name</cong:td>
                                <cong:td>
                                    <div>
                                        <span id="dojoSupplier">
                                            <cong:autocompletor id="supplierName" name="supContact.companyName" template="tradingPartner"  styleClass="textlabelsBoldForTextBox textCap"
                                                                fields="supplierCode,NULL,NULL,NULL,NULL,NULL,NULL,supplierDisabled,supplierAddress,supplierCity,supplierState,supplierCountry,supplierZip,NULL,supplierPhone,supplierFax,supplierEmail,NULL,NULL,NULL,supplierPoa,NULL,NULL,NULL,NULL,NULL,NULL,NULL,supplierDisabledAcct"
                                                                query="CLIENT_WITH_CONSIGNEE"  width="600" container="NULL" value="${lclBooking.supContact.companyName}"
                                                                shouldMatch="true" callback="supplierAcctCheck()" scrollHeight="300"/>
                                        </span>
                                        <span id="manualSupplier" style="display:none">
                                            <cong:text name="supContactName" id="dupSupplierName" value="${lclBookingForm.supContactName}" styleClass="text textlabelsBoldForTextBox textCap"/></span>
                                            <cong:checkbox id="newSupplier" name="newSupplier" onclick="newSupplierName();" container="NULL"/>
                                            <cong:img src="${path}/jsps/LCL/images/display.gif" width="16" height="16" alt="display" styleClass="contactR" onclick="openLclContactInfo('${path}','Supplier')"/>
                                            <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" height="16" alt="display" styleClass="trading" id="supplierIcon"
                                                      onclick="newTPAccountFromBooking('${path}','dupSupplierName','supplierAddress','S','supplierCountry',
                                                      'supplierCity','supplierState','supplierZip','supplierPhone','supplierFax','supplierEmail',
                                                      'supplierCode','supplierCode','supplierUnLocCode','newSupplier');"/>
                                    </div>
                                </cong:td>
                            </cong:tr>
                        </c:when>
                        <c:otherwise>
                            <cong:tr id="originRefresh">
                                <jsp:include page="/jsps/LCL/ajaxload/refreshImpOriginAgent.jsp"/>
                            </cong:tr>
                        </c:otherwise>
                    </c:choose>

                    <c:if test="${lclBookingForm.moduleName ne 'Imports'}">
                        <cong:tr>
                            <input type="hidden" value="supplierDisabled" id="supplierDisabled"/>
                            <input type="hidden" value="supplierDisabledAcct" id="supplierDisabledAcct"/>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Address</cong:td>
                            <cong:td>
                                <cong:textarea cols="30" rows="6" id="supplierAddress" name="supplierAddress" 
                                               styleClass="textlabelsBoldForTextBox textup" value="${lclBooking.supContact.address}"/>
                                <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                            <cong:td>
                                <cong:autocompletor id="supplierCity" name="supContact.city" template="three"  
                                                    fields="supplierCity,supplierCountry,supplierUnLocCode,supplierState"
                                                    styleClass="textlabelsBoldForTextBox textCap"  query="PORT_CITY_STATE"  
                                                    width="600" container="NULL" value="${lclBooking.supContact.city}" 
                                                    shouldMatch="true"  scrollHeight="300">
                                </cong:autocompletor>
                                <input type="hidden" id="supplierUnLocCode" name="supplierUnLocCode"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl"> State </cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="supplierState" name="supContact.state" value="${lclBooking.supContact.state}" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Zip</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="supplierZip" name="supContact.zip" value="${lclBooking.supContact.zip}" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Country</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="supplierCountry" name="supContact.country" value="${lclBooking.supContact.country}" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Phone</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="supplierPhone" name="supContact.phone1" value="${lclBooking.supContact.phone1}" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Fax</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="supplierFax" name="supContact.fax1" value="${lclBooking.supContact.fax1}" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Email</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="supplierEmail" name="supContact.email1" value="${lclBooking.supContact.email1}" maxlength="100"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Client Ref</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1"  id="supplierClientRef" name="supReference" maxlength="100" value="${lclBooking.supReference}"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Client</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="supplierClient" name="supplierClient" maxlength="100"/>
                                <input type="checkbox" id="newSupplierClient" name="newSupplierClient"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="newSupplierClientCity" name="newSupplierClientCity" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">POA</cong:td>
                            <cong:td><span id="warning" name="warning"></span><cong:hidden name="supplierPoa" id="supplierPoa"/>
                            </cong:td>
                        </cong:tr>
                    </c:if>
                </cong:table>
            </cong:td><!-- Supplier ends here -->
            <cong:td width="15%" valign="top">
                <cong:table width="100%">
                    <cong:tr>
                        <cong:td colspan="2" styleClass="report-header">${lclBookingForm.moduleName ne 'Imports' ? "Shipper":"House Shipper"}</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Code</cong:td>
                        <cong:td>
                            <c:choose>
                                <c:when test="${lclBooking.bookingType eq 'T' && lclBookingForm.moduleName ne 'Imports'}">
                                    <cong:text  id="shipperCode" name="shipAcct" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclBooking.rtdAgentAcct.accountno}"/>
                                </c:when><c:otherwise>
                                    <cong:text  id="shipperCode" name="shipAcct" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclBooking.shipAcct.accountno}"/>
                                </c:otherwise></c:choose>
                            <c:if test="${lclBookingForm.moduleName eq 'Imports'}">
                                <img src="${path}/jsps/LCL/images/folder_open.png" width="16" height="16" alt="details"  style="vertical-align: middle;"
                                     title="Address Details" Class="tradingDetails" onclick="displayTradingDetails('${path}', '${lclBooking.lclFileNumber.id}', 'shipperCode', 'shipperName', 'dupShipperName', 'Shipper', 'shipperAddress', 'shipperCity', 'shipperState', 'shipperCountry', 'shipperPhone', 'shipperFax', 'shipperEmail', 'shipperZip', 'newShipper', 'shipperSalesPersonCode', 'shipperColoadRetailRadio')"/>
                                <span class="importCreditShipper green bold"  style="vertical-align: middle;">${ImportCreditForShipper}</span>
                                &nbsp;<span class="shipperCreditV red"  style="vertical-align: middle;">${CreditForShipper}</span>
                                &nbsp;<a class="shipeerScanSopV red" href="javascript:openSop('shipperCode')" style="vertical-align: middle;">${ScanSopForShipper}</a>
                            </c:if>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Name</cong:td>
                        <cong:td>
                            <div>
                                <span id="dojoShipper">
                                    <c:choose>
                                        <c:when test="${lclBooking.bookingType eq 'T' && lclBookingForm.moduleName ne 'Imports'}">
                                            <cong:autocompletor name="shipContact.companyName" template="tradingPartner" id="shipperName"
                                                                fields="shipperCode,shipper_acct_type,shipper_sub_type,shipBsEciAcctNo,shipBsEciFwNo,NULL,NULL,shipperDisabled,shipperAddress,shipperCity,shipperState,shipperCountry,shipperZip,NULL,shipperPhone,shipperFax,shipperEmail,NULL,NULL,shipperPoa,shipperCredit,NULL,shipDisableAcct,shipColoadNo,shipColoadDesc,shipRetailNo,shipRetailDesc"
                                                                styleClass="textlabelsBoldForTextBox textCap" paramFields="shipperSearchState,shipperSearchZip,shipperSearchSalesCode,shipperCountryUnLocCode"  query="SHIPPER"  width="600" container="NULL" shouldMatch="true" value="${lclBooking.rtdAgentAcct.accountName}"
                                                                callback="shipper_AccttypeCheck('${path}');ShippingDetails();checkEculine($('#shipperCode').val(), 'S');" scrollHeight="300"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${lclBookingForm.moduleName ne 'Imports'}">
                                                    <cong:autocompletor name="shipContact.companyName" template="tradingPartner" id="shipperName"
                                                                        fields="shipperCode,shipper_acct_type,shipper_sub_type,shipBsEciAcctNo,shipBsEciFwNo,NULL,shipperSalesPersonCode,shipperDisabled,shipperAddress,shipperCity,shipperState,shipperCountry,shipperZip,NULL,shipperPhone,shipperFax,shipperEmail,NULL,NULL,shipperPoa,shipperCredit,NULL,shipDisableAcct,shipColoadNo,shipColoadDesc,shipRetailNo,shipRetailDesc"
                                                                        styleClass="textlabelsBoldForTextBox textCap" paramFields="shipperSearchState,shipperSearchZip,shipperSearchSalesCode,shipperCountryUnLocCode"  query="SHIPPER"  width="600" container="NULL" shouldMatch="true" value="${lclBooking.shipContact.companyName}"
                                                                        callback="shipper_AccttypeCheck('${path}');ShippingDetails();notesCount('${path}','shpNotes',$('#shipperCode').val(),'S');checkEculine($('#shipperCode').val(), 'S');" scrollHeight="300"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <cong:autocompletor name="shipContact.companyName" template="tradingPartner" id="shipperName" 
                                                                        fields="shipperCode,shipper_acct_type,shipper_sub_type,NULL,NULL,NULL,shipperSalesPersonCode,shipperDisabled,shipperAddress,shipperCity,shipperState,shipperCountry,shipperZip,NULL,shipperPhone,shipperFax,shipperEmail,NULL,NULL,NULL,shipperPoa,shipperCredit,NULL,NULL,NULL,NULL,NULL,shipDisableAcct,NULL,NULL,NULL,NULL,shipScanSop,importCreditShipper"
                                                                        styleClass="textlabelsBoldForTextBox textCap" paramFields="shipperSearchState,shipperSearchZip,shipperSearchSalesCode,shipperCountryUnLocCode"  query="TP_IMPORTS_NOTE"  width="600" container="NULL" shouldMatch="true" value="${lclBooking.shipContact.companyName}"
                                                                        callback="shipper_AccttypeCheck('${path}');setToolTipTp('Shipper','shipperCode','shipperName','shipper');notesCount('${path}','shpNotes',$('#shipperCode').val(),'S');addClientHotCodes($('#shipperCode').val());" scrollHeight="300"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                                <input type="hidden" name="shipDisableAcct" id="shipDisableAcct"/>
                                <input type="hidden" name="shipperDisabled" id="shipperDisabled"/>
                                <input type="hidden" name="shipper_accttype" id="shipper_acct_type"/>
                                <input type="hidden" name="shipper_subtype" id="shipper_sub_type"/>
                                <input type="hidden" name="shipScanSop" id="shipScanSop" />
                                <input type="hidden" name="importCreditShipper" id="importCreditShipper"/>
                                <cong:hidden name="shipperCredit" id="shipperCredit" styleClass="textlabelsBoldForTextBox" value="${CreditForShipper}"/>
                                <span id="manualShipper" style="display:none">
                                    <cong:text name="shipContactName" id="dupShipperName" value="${lclBookingForm.shipContactName}" 
                                               styleClass="text textlabelsBoldForTextBox textCap" onchange="newShipperChange()" /></span>
                                <input type="checkbox" id="newShipper" name="newShipper" onclick="newShipperName();"  style="vertical-align: middle;" container="NULL"  title="New"/>
                                <c:if test="${lclBookingForm.moduleName eq 'Imports'}">
                                    <c:choose>
                                        <c:when test="${lclBookingForm.shipperIcon}">
                                            <img src="${path}/img/icons/e_contents_view1.gif"  style="vertical-align: middle;" width="14" height="14" title="Click here to see Shipper Notes"
                                                 class="shpNotes"  onclick="displayNotes('${path}', $('#shipperCode').val(), 'shpNotes', $('#fileId').val(), $('#fileNo').val());"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${path}/img/icons/e_contents_view.gif"  style="vertical-align: middle;" width="14" height="14" title="Click here to see Shipper Notes"
                                                 class="shpNotes" onclick="displayNotes('${path}', $('#shipperCode').val(), 'shpNotes', $('#fileId').val(), $('#fileNo').val());"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <input type="hidden" id="shipperAddress" name="shipContact.address" value="${lclBooking.shipContact.address}"/>
                                    <input type="hidden" id="shipperCity" name="shipContact.city" value="${lclBooking.shipContact.city}"/>
                                    <input type="hidden" id="shipperState" name="shipContact.state" value="${lclBooking.shipContact.state}"/>
                                    <input type="hidden" id="shipperZip" name="shipContact.zip" value="${lclBooking.shipContact.zip}"/>
                                    <input type="hidden" id="shipperCountry" name="shipContact.country" value="${lclBooking.shipContact.country}"/>
                                    <input type="hidden" id="shipperPhone" name="shipContact.phone1" value="${lclBooking.shipContact.phone1}"/>
                                    <input type="hidden" id="shipperFax" name="shipContact.fax1" value="${lclBooking.shipContact.fax1}"/>
                                    <input type="hidden" id="shipperEmail" name="shipContact.email1" value="${lclBooking.shipContact.email1}"/>
                                    <input type="hidden" id="shipperSalesPersonCode" name="shipContact.salesPersonCode" value="${lclBooking.shipContact.salesPersonCode}"/>
                                    <input type="hidden"   id="shipperColoadRetailRadio" name="shipperColoadRetailRadio" />
                                    <input type="hidden" id="shipUnlocCode" />
                                    <input type="hidden" name="shipperSearchState" id="shipperSearchState"/>
                                    <input type="hidden" name="shipperSearchZip" id="shipperSearchZip"/>
                                    <input type="hidden" name="shipperSearchSalesCode" id="shipperSearchSalesCode"/>
                                    <input type="hidden" name="shipperSearchCountry" id="shipperSearchCountry"/>
                                    <input type="hidden" name="shipperCountryUnLocCode" id="shipperCountryUnLocCode"/>
                                    <cong:img src="${path}/img/icons/iicon.png"  style="vertical-align: middle;" width="16" height="16" alt="Client" id="shipper"/>
                                </c:if>
                                <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"  style="vertical-align: middle;"
                                     title="Click here to edit House Shipper Search options" onclick="showClientSearchOption('${path}', 'Shipper')"/>
                                <cong:img src="${path}/jsps/LCL/images/display.gif"  style="vertical-align: middle;" width="16" height="16" alt="display" styleClass="contactR" onclick="openLclContactInfo('${path}','Shipper')"/>
                                <c:choose>
                                    <c:when test="${lclBookingForm.moduleName  ne 'Imports'}">
                                        <cong:img src="${path}/jsps/LCL/images/add2.gif" style="vertical-align: middle;" 
                                                  id="shipperIcon"  width="16" height="16" alt="display" styleClass="trading" 
                                                  onclick="newTPAccountFromBooking('${path}','dupShipperName','shipperAddress','S','shipperCountry','shipperCity',
                                                  'shipperState','shipperZip','shipperPhone','shipperFax','shipperEmail','shipperCode','shipperCodeClient','shipperUnLocCode','newShipper')">
                                        </cong:img>
                                    </c:when>
                                    <c:otherwise>
                                        <cong:img src="${path}/jsps/LCL/images/add2.gif" style="vertical-align: middle;" 
                                                  id="shipperIcon"  width="16" height="16" alt="display" styleClass="trading" 
                                                  onclick="createNewTPAcct('${path}','LCL_IMPORT_SHIPPER','S','newShipper','dupShipperName','shipperAddress','shipperCity',
                                                  'shipperState','shipUnlocCode','shipperPhone','shipperFax','shipperEmail','shipperZip','shipperSalesPersonCode',
                                                  'shipperColoadRetailRadio')">
                                        </cong:img>
                                    </c:otherwise>
                                </c:choose>                              
                            </div>
                        </cong:td>
                    </cong:tr>

                    <c:if test="${lclBookingForm.moduleName ne 'Imports'}">
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Address</cong:td>
                            <cong:td>
                                <cong:textarea cols="30" rows="6" id="shipperAddress" name="shipContact.address" value="${lclBooking.shipContact.address}" styleClass="textlabelsBoldForTextBox textup" onchange="changeShipDetails()" />
                                <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                            <cong:td>
                                <cong:autocompletor id="shipperCity" name="shipContact.city" template="three"  
                                                    fields="shipperCity,shipperCountry,shipperUnLocCode,shipperState"
                                                    styleClass="textlabelsBoldForTextBox textCap"  query="PORT_CITY_STATE"  
                                                    width="600" container="NULL" value="${lclBooking.shipContact.city}" 
                                                    shouldMatch="true" callback="changeShipDetails()" scrollHeight="300">
                                </cong:autocompletor>
                                <input type="hidden" id="shipperUnLocCode" name="shipperUnLocCode"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl"> 	 State </cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="shipperState" name="shipContact.state" value="${lclBooking.shipContact.state}" maxlength="50" onchange="changeShipDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Zip</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="shipperZip" name="shipContact.zip" value="${lclBooking.shipContact.zip}" maxlength="50" onchange="changeShipDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl"> Country</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="shipperCountry" name="shipContact.country" value="${lclBooking.shipContact.country}" maxlength="50" onchange="changeShipDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Phone</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="shipperPhone" name="shipContact.phone1" value="${lclBooking.shipContact.phone1}" maxlength="50" onchange="changeShipDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Fax</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="shipperFax" name="shipContact.fax1" value="${lclBooking.shipContact.fax1}" maxlength="50" onchange="changeShipDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Email</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="shipperEmail" name="shipContact.email1" value="${lclBooking.shipContact.email1}" maxlength="100" onchange="changeShipDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Client Ref</cong:td>
                            <cong:td>
                                <cong:text id="shipperClientRef" name="shipReference"  styleClass="text textlabelsBoldForTextBox textCap" maxlength="100" value="${lclBookingForm.shipReference}" onchange="changeShipDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Client</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="shipperClient" name="shipperClient" maxlength="50"/>
                                <input type="checkbox" id="newShipperClient" name="newShipperClient"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="newShipperClientCity" name="newShipperClientCity" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">POA</cong:td>
                            <cong:td>
                                <cong:hidden name="shipperPoa" id="shipperPoa" value="${lclBooking.shipAcct.generalInfo.poa}"/>
                                <c:choose>
                                    <c:when test="${lclBooking.shipAcct.generalInfo.poa eq 'Y'}">
                                        <span class="shipperPOAV green">YES</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="shipperPOAV red">${not empty lclBooking.shipAcct.generalInfo.poa ? 'NO' :''}</span></c:otherwise>
                                </c:choose>
                            </cong:td>
                        </cong:tr>
                    </c:if>
                </cong:table>
            </cong:td><!-- Shippers ends here -->
            <c:if test="${lclBookingForm.moduleName ne 'Imports'}">
                <cong:td valign="top">
                    <cong:table width="100%">
                        <cong:tr>
                            <cong:td colspan="2" styleClass="report-header"> Forwarder</cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Code</cong:td>
                            <cong:td>
                                <cong:text id="forwarderCode" name="fwdAcct" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclBooking.fwdAcct.accountno}"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Name</cong:td>
                            <cong:td>
                                <cong:autocompletor name="fwdContact.companyName" template="tradingPartner" id="forwarderName"
                                                    fields="forwarderCode,forwarder_acct_type,forwarder_sub_type,fwdBsEciAcctNo,fwdBsEciFwNo,NULL,NULL,forwardDisabled,forwarderAddress,forwarderCity,forwarderState,forwarderCountry,forwarderZip,NULL,forwarderPhone,forwarderFax,forwarderEmail,NULL,NULL,forwarderPoa,forwarderCredit,NULL,forwardDisabledAcct,fwdColoadNo,fwdColoadDesc,fwdRetailNo,fwdRetailDesc"
                                                    query="FORWARDER"  width="600" container="NULL" value="${lclBooking.fwdContact.companyName}" styleClass="textlabelsBoldForTextBox textCap"
                                                    shouldMatch="true" callback="forwarder_AccttypeCheck();forwarderDetails();checkEculine($('#forwarderCode').val(), 'F');" scrollHeight="300px"/>
                                <input type="hidden" name="forwarder_accttype" id="forwarder_acct_type"/>
                                <input type="hidden" name="forwarder_subtype" id="forwarder_sub_type"/>
                                <input type="hidden" name="forwardDisabled" id="forwardDisabled"/>
                                <input type="hidden" name="forwardDisabledAcct" id="forwardDisabledAcct"/>
                                <cong:hidden name="forwarderCredit" id="forwarderCredit" styleClass="textlabelsBoldForTextBox" value="${CreditForForwarder}"/>
                                <cong:img src="${path}/jsps/LCL/images/display.gif" width="16" height="16" alt="display" styleClass="contactR" onclick="openLclContactInfo('${path}','Consignee')"/>
                                <%-- <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" height="16" alt="display" 
                                        styleClass="trading" onclick="createNewTPAcct('${path}','LCL_IMPORT_FORWARDER','shipperName',
                                     'dupShipperName','shipperAddress','shipperCity','shipUnlocCode')"/> --%>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Address</cong:td>
                            <cong:td>
                                <cong:textarea cols="30" rows="6" id="forwarderAddress" name="fwdContact.address" value="${lclBooking.fwdContact.address}" styleClass="textlabelsBoldForTextBox textup" onchange="changeForwarderDetails()"/>
                                <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                            <cong:td>
                                <cong:autocompletor id="forwarderCity" name="fwdContact.city" template="three"  
                                                    fields="forwarderCity,forwarderCountry,fwdUnLocCode,forwarderState"
                                                    styleClass="textlabelsBoldForTextBox textCap"  query="PORT_CITY_STATE"  
                                                    width="600" container="NULL" value="${lclBooking.fwdContact.city}" 
                                                    shouldMatch="true" callback="changeForwarderDetails()" scrollHeight="300">
                                </cong:autocompletor>
                                <input type="hidden" id="fwdUnLocCode" name="fwdUnLocCode"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl"> 	 State </cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="forwarderState" name="fwdContact.state" value="${lclBooking.fwdContact.state}" maxlength="50" onchange="changeForwarderDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Zip</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="forwarderZip" name="fwdContact.zip" value="${lclBooking.fwdContact.zip}" maxlength="50" onchange="changeForwarderDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Country</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="forwarderCountry" name="fwdContact.country" value="${lclBooking.fwdContact.country}" maxlength="50" onchange="changeForwarderDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Phone</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap"  id="forwarderPhone" name="fwdContact.phone1" value="${lclBooking.fwdContact.phone1}" maxlength="50" onchange="changeForwarderDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Fax</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="forwarderFax" name="fwdContact.fax1" value="${lclBooking.fwdContact.fax1}" maxlength="50" onchange="changeForwarderDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Email</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="forwarderEmail" name="fwdContact.email1" value="${lclBooking.fwdContact.email1}" maxlength="100" onchange="changeForwarderDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Client Ref</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="forwarderClientRef" name="fwdReference" maxlength="50" value="${lclBookingForm.fwdReference}" onchange="changeForwarderDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Client</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="forwarderClient" name="forwarderClient" maxlength="50" />
                                <input type="checkbox" id="newForwarderClient" name="newForwarderClient"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="newForwarderClientCity" name="newForwarderClientCity" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">POA</cong:td>
                            <cong:td>
                                <span id="forwarder" name="warning"></span><cong:hidden name="forwarderPoa" id="forwarderPoa" value="${POAForForwarder}"/>
                            </cong:td>
                        </cong:tr>
                    </cong:table>
                </cong:td><!-- Forwarder ends here -->
            </c:if>               
            <cong:td valign="top" width="15%"><!-- Consignee starts here -->
                <cong:table width="100%" border="0">
                    <cong:tr>
                        <cong:td colspan="2" styleClass="report-header"> ${lclBookingForm.moduleName ne 'Imports' ? "Consignee":"House Consignee"}</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Code</cong:td>
                        <cong:td>
                            <cong:text id="consigneeCode" name="consAcct" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclBooking.consAcct.accountno}"/>
                            <c:if test="${lclBookingForm.moduleName eq 'Imports'}">
                                <img src="${path}/jsps/LCL/images/folder_open.png" width="16"  style="vertical-align: middle;" title="Address Details" height="16" alt="details"
                                     class="tradingDetails" onclick="displayTradingDetails('${path}', '${lclBooking.lclFileNumber.id}', 'consigneeCode', 'consigneeName', 'dupConsigneeName', 'Consignee', 'consigneeAddress', 'consigneeCity', 'consigneeState', 'consigneeCountry', 'consigneePhone', 'consigneeFax', 'consigneeEmail', 'consigneeZip', 'newConsignee', 'consigneeSalesPersonCode', 'consigneeColoadRetailRadio');"/>
                                <span class="importCreditConsignee green bold" style="vertical-align: middle;">${ImportCreditForConsignee}</span>
                                &nbsp;<span class="consigneeCreditV red" style="vertical-align: middle;">${CreditForConsignee}</span>
                                &nbsp;<a class="consigneeScanSopV red" href="javascript:openSop('consigneeCode')"  style="vertical-align: middle;">${ScanSopForConsignee}</a>
                            </c:if>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Name</cong:td>
                        <cong:td>
                            <span id="dojoConsignee">
                                <c:choose>
                                    <c:when test="${lclBookingForm.moduleName ne 'Imports'}">
                                        <cong:autocompletor name="consContact.companyName" template="tradingPartner" id="consigneeName" query="EXP_CONSIGNEE" paramFields="consigneeSearchState,consigneeSearchZip,consigneeSearchSalesCode,consigneeCountryUnLocCode"
                                                            fields="consigneeCode,consignee_acct_type,consignee_sub_type,consBsEciAcctNo,consBsEciFwNo,NULL,consigneeSalesPersonCode,consDisabled,consigneeAddress,consigneeCity,consigneeState,consigneeCountry,consigneeZip,NULL,consigneePhone,consigneeFax,consigneeEmail,NULL,NULL,consigneePoa,consigneeCredit,NULL,NULL,consDisableAcct,consColoadNo,consColoadDesc,consRetailNo,consRetailDesc"
                                                            width="600" container="NULL" value="${lclBooking.consContact.companyName}" styleClass="textlabelsBoldForTextBox textCap" shouldMatch="true"
                                                            callback="consignee_AccttypeCheck('${path}');copyConsigneeDetails();notesCount('${path}','conNotes',$('#consigneeCode').val(),'C');checkEculine($('#consigneeCode').val(), 'C');" scrollHeight="300"/>
                                    </c:when>
                                    <c:otherwise>
                                        <cong:autocompletor name="consContact.companyName" template="tradingPartner" id="consigneeName" query="IMPORTS_TP_NOTE" paramFields="consigneeSearchState,consigneeSearchZip,consigneeSearchSalesCode,consigneeCountryUnLocCode"
                                                            fields="consigneeCode,consignee_acct_type,consignee_sub_type,NULL,NULL,NULL,consigneeSalesPersonCode,consDisabled,consigneeAddress,consigneeCity,consigneeState,consigneeCountry,consigneeZip,NULL,consigneePhone,consigneeFax,consigneeEmail,coloadCommNo,NULL,NULL,consigneePoa,consigneeCredit,NULL,NULL,NULL,NULL,NULL,consDisableAcct,NULL,NULL,NULL,NULL,consigneeScanSop,importCreditConsignee"
                                                            width="600" container="NULL" value="${lclBooking.consContact.companyName}" styleClass="textlabelsBoldForTextBox textCap" shouldMatch="true"
                                                            callback="consignee_AccttypeCheck('${path}');setToolTipTp('Consignee','consigneeCode','consigneeName','consContact');checkErtAndRates('Consignee');notesCount('${path}','conNotes',$('#consigneeCode').val(),'C');addClientHotCodes($('#consigneeCode').val());" scrollHeight="300"/>
                                    </c:otherwise>
                                </c:choose>

                                <input type="hidden" name="consDisabled" id="consDisabled"/>
                                <input type="hidden" name="consDisableAcct" id="consDisableAcct"/>
                                <input type="hidden" name="consignee_accttype" id="consignee_acct_type"/>
                                <input type="hidden" name="consignee_subtype" id="consignee_sub_type"/>
                                <input type="hidden" name="consignee_contactName" id="consignee_contactName"/>
                                <input type="hidden" name="consigneeCredit" id="consigneeCredit"/>
                                <input type="hidden" name="consigneeScanSop" id="consigneeScanSop"/>
                                <input type="hidden" name="importCreditConsignee" id="importCreditConsignee" />
                                <input type="hidden" name="coloadCommNo" id="coloadCommNo" value="${lclBooking.consAcct.generalInfo.impCommodity.code}"/>
                                <input type="hidden" name="existinColoadCommon" id="existinColoadCommon" value="${lclBooking.consAcct.generalInfo.impCommodity.code}"/>
                                <input type="hidden" name="consigneeAcctName" id="consigneeAcctName" value="${lclBooking.consAcct.accountName}"/>
                                <input type="hidden" id="drErtFlag"/>
                            </span>
                            <span id="manualConsignee" style="display:none">
                                <cong:text name="consContactName" id="dupConsigneeName" value="${lclBookingForm.consContactName}" styleClass="text textlabelsBoldForTextBox textCap" onchange="newConsigneeChange()" /></span>
                            <c:if test="${lclBookingForm.moduleName ne 'Imports'}">
                                <input type="checkbox"  style="vertical-align: middle;" id="newConsignee" name="newConsignee" onclick="newConsigneeName();" container="NULL" title="New"/>
                            </c:if>  
                            <c:if test="${lclBookingForm.moduleName eq 'Imports'}">
                                <input type="hidden" id="consigneeAddress" name="consContact.address" value="${lclBooking.consContact.address}"/>
                                <input type="hidden" id="consigneeCity" name="consContact.city" value="${lclBooking.consContact.city}"/>
                                <input type="hidden" id="consigneeState" name="consContact.state" value="${lclBooking.consContact.state}"/>
                                <input type="hidden" id="consigneeZip" name="consContact.zip" value="${lclBooking.consContact.zip}"/>
                                <input type="hidden" id="consigneeCountry" name="consContact.country" value="${lclBooking.consContact.country}"/>
                                <input type="hidden" id="consigneePhone" name="consContact.phone1" value="${lclBooking.consContact.phone1}"/>
                                <input type="hidden" id="consigneeFax" name="consContact.fax1" value="${lclBooking.consContact.fax1}"/>
                                <input type="hidden" id="consigneeEmail" name="consContact.email1" value="${lclBooking.consContact.email1}"/>
                                <input type="hidden" id="consigneeCustEmail" name="consigneeCustEmail" value="${lclBooking.consAcct.custAddr.email1}"/>
                                <input type="hidden" id="consigneeSalesPersonCode" name="consContact.salesPersonCode" value="${lclBooking.consContact.salesPersonCode}"/>
                                <input type="hidden"   id="consigneeColoadRetailRadio" name="consigneeColoadRetailRadio" />
                                <input type="hidden" id="consigneeUnLocCode"/>
                                <input type="hidden" name="consigneeSearchState" id="consigneeSearchState"/>
                                <input type="hidden" name="consigneeSearchZip" id="consigneeSearchZip"/>
                                <input type="hidden" name="consigneeSearchSalesCode" id="consigneeSearchSalesCode"/>
                                <input type="hidden" name="consigneeSearchCountry" id="consigneeSearchCountry"/>
                                <input type="hidden" name="consigneeCountryUnLocCode" id="consigneeCountryUnLocCode"/>
                                <input type="checkbox"  style="vertical-align: middle;"  id="newConsignee" name="newConsignee" onclick="isValidateBillToCons();" container="NULL" title="New"/>
                                <input type="checkbox"  style="vertical-align: middle;"  id="editTpConsignee" name="editTpConsignee" onclick="isUncheckedEditTpCons();" container="NULL" title="Edit Consignee TP Details"/>
                                <c:choose>
                                    <c:when test="${lclBookingForm.consigneeIcon}">
                                        <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Consignee Notes" alt="green" style="vertical-align: middle;"
                                             class="conNotes" onclick="displayNotes('${path}', $('#consigneeCode').val(), 'conNotes', $('#fileId').val(), $('#fileNo').val());"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Consignee Notes" alt="normal" style="vertical-align: middle;"
                                             class="conNotes" onclick="displayNotes('${path}', $('#consigneeCode').val(), 'conNotes', $('#fileId').val(), $('#fileNo').val());"/>
                                    </c:otherwise>
                                </c:choose>
                                <cong:img src="${path}/img/icons/iicon.png" width="16" height="16" alt="Client" id="consContact" style="vertical-align: middle;"/>
                                <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" style="vertical-align: middle;"
                                     title="Click here to edit House Consignee Search options" onclick="showClientSearchOption('${path}', 'Consignee');"/>
                            </c:if>
                            <cong:img src="${path}/jsps/LCL/images/display.gif" style="vertical-align: middle;" width="16" height="16" alt="display" styleClass="contactR" onclick="openLclContactInfo('${path}','Consignee')"/>
                            <c:choose>
                                <c:when test="${lclBookingForm.moduleName ne 'Imports'}">
                                    <cong:img src="${path}/jsps/LCL/images/add2.gif" style="vertical-align: middle;" id="consigneeIcon"  
                                              width="16" height="16" alt="display" styleClass="trading" 
                                              onclick="newTPAccountFromBooking('${path}','dupConsigneeName','consigneeAddress','C','consigneeCountry','consigneeCity',
                                              'consigneeState','consigneeZip','consigneePhone','consigneeFax','consigneeEmail','consigneeCode','consigneeCodeClient','conisUnLocCode','newConsignee')">
                                    </cong:img>
                                </c:when>
                                <c:otherwise>
                                    <cong:img src="${path}/jsps/LCL/images/add2.gif" style="vertical-align: middle;" id="consigneeIcon"  
                                              width="16" height="16" alt="display" styleClass="trading" 
                                              onclick="createNewTPAcct('${path}','LCL_IMPORT_CONSIGNEE','C','newConsignee','dupConsigneeName','consigneeAddress','consigneeCity',
                                              'consigneeState','consigneeUnLocCode','consigneePhone','consigneeFax','consigneeEmail','consigneeZip','consigneeSalesPersonCode',
                                              'consigneeColoadRetailRadio')">
                                    </cong:img>
                                </c:otherwise>
                            </c:choose>                  
                        </cong:td>
                    </cong:tr>
                    <c:if test="${lclBookingForm.moduleName ne 'Imports'}">
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Address</cong:td>
                            <cong:td>
                                <cong:textarea cols="30" rows="6" id="consigneeAddress" name="consContact.address" value="${lclBooking.consContact.address}" styleClass="textlabelsBoldForTextBox textup" onchange="changeConsiDetails()"/>
                                <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                            </cong:td>
                        </cong:tr>

                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                            <cong:td>
                                <cong:autocompletor id="consigneeCity" name="consContact.city" template="three"  
                                                    fields="consigneeCity,consigneeCountry,conisUnLocCode,consigneeState"
                                                    styleClass="textlabelsBoldForTextBox textCap"  query="PORT_CITY_STATE"  
                                                    width="600" container="NULL" value="${lclBooking.consContact.city}" 
                                                    shouldMatch="true" callback="changeConsiDetails()" scrollHeight="300">
                                </cong:autocompletor>
                                <input type="hidden" id="conisUnLocCode" name="conisUnLocCode"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl"> 	 State </cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneeState" name="consContact.state" value="${lclBooking.consContact.state}" maxlength="50" onchange="changeConsiDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Zip</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneeZip" name="consContact.zip" value="${lclBooking.consContact.zip}" maxlength="50" onchange="changeConsiDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Country</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneeCountry" name="consContact.country" value="${lclBooking.consContact.country}" maxlength="50" onchange="changeConsiDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Phone</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneePhone" name="consContact.phone1" value="${lclBooking.consContact.phone1}" maxlength="50" onchange="changeConsiDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Fax</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneeFax" name="consContact.fax1" value="${lclBooking.consContact.fax1}" maxlength="50" onchange="changeConsiDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Email</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="consigneeEmail" name="consContact.email1" value="${lclBooking.consContact.email1}" maxlength="100" onchange="changeConsiDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Client Ref</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="consigneeClientRef" name="consReference" maxlength="50" value="${lclBookingForm.consReference}" onchange="changeConsiDetails()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Client</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="consigneeClient" name="consigneeClient" maxlength="50"/>
                                <input type="checkbox" id="newConsigneeClient" name="newConsigneeClient"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="newConsigneeClientCity" name="newConsigneeClientCity" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">POA</cong:td>
                            <cong:td>
                                <cong:hidden name="consigneePoa" id="consigneePoa" value="${lclBooking.consAcct.generalInfo.poa}"/>
                                <c:choose>
                                    <c:when test="${lclBooking.consAcct.generalInfo.poa eq 'Y'}">
                                        <label id="consigneePOAV" class="green consigneePoaValues">YES</label>
                                    </c:when>
                                    <c:otherwise>
                                        <label id="consigneePOAV" class="red consigneePoaValues">
                                            ${not empty lclBooking.consAcct.generalInfo.poa ? 'NO' :''}</label>
                                    </c:otherwise>
                                </c:choose>
                            </cong:td>
                        </cong:tr>
                    </c:if>
                </cong:table>
            </cong:td><!-- Consignee ends here -->
            <cong:td width="25%" valign="top">
                <c:choose>
                    <c:when test="${lclBookingForm.moduleName ne 'Imports'}">
                        <c:set var="notifyMethod" value=""/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="notifyMethod" value="setToolTipTp('Notify','notifyCode','notifyName','noty');checkErtAndRates('Notify');"/>
                    </c:otherwise>
                </c:choose>
                <cong:table width="100%">
                    <cong:tr>
                        <cong:td colspan="2" styleClass="report-header"> Notify</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Code</cong:td>
                        <cong:td>
                            <cong:text id="notifyCode" name="notyAcct" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclBooking.notyAcct.accountno}"/>
                            <c:if test="${lclBookingForm.moduleName eq 'Imports'}">
                                <img src="${path}/jsps/LCL/images/folder_open.png" width="16" height="16" style="vertical-align: middle;" alt="details" title="Address Details" class="tradingDetails" onclick="displayTradingDetails('${path}', '${lclBooking.lclFileNumber.id}', 'notifyCode', 'notifyName', 'dupNotifyName', 'Notify', 'notifyAddress', 'notifyCity', 'notifyState', 'notifyCountry', 'notifyPhone', 'notifyFax', 'notifyEmail', 'notifyZip', 'newNotify', 'notifySalesPersonCode', 'notifyColoadRetailRadio')"/>
                                <span class="importCreditForNotify green bold" style="vertical-align: middle;">${ImportCreditForNotify}</span>
                                &nbsp;<span class="notifyCreditV red" style="vertical-align: middle;">${CreditForNotify}</span>
                                &nbsp;<a class="notifyScanSopV red" href="javascript:openSop('notifyCode')" style="vertical-align: middle;">${ScanSopForNotify}</a>
                            </c:if>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td textlabelsBoldforlcl">Name</cong:td>
                        <cong:td>
                            <span id="dojoNotify">
                                <c:choose>
                                    <c:when test="${lclBookingForm.moduleName ne 'Imports'}">
                                        <cong:autocompletor name="notyContact.companyName" template="tradingPartner" id="notifyName"
                                                            fields="notifyCode,notify_acct_type,notify_sub_type,NULL,NULL,NULL,notifySalesPersonCode,notifyDisabled,notifyAddress,notifyCity,notifyState,notifyCountry,notifyZip,NULL,notifyPhone,notifyFax,notifyEmail,notifyCommNo,NULL,NULL,notifyPoa,notifyCredit,NULL,NULL,NULL,NULL,NULL,notyDisableAcct,NULL,NULL,NULL,NULL,notifyScanSop,importCreditNofify"
                                                            query="TP_IMPORTS_NOTE"  width="600" container="NULL" value="${lclBooking.notyContact.companyName}" position="left" styleClass="textlabelsBoldForTextBox textCap" shouldMatch="true"
                                                            paramFields="notifySearchState,notifySearchZip,notifySearchSalesCode,notifyCountryUnLocCode"  callback="notify_AccttypeCheck('${path}');${notifyMethod};notesCount('${path}','notNotes',$('#notifyCode').val(),'N');" scrollHeight="300"/>
                                    </c:when>
                                    <c:otherwise>
                                        <cong:autocompletor name="notyContact.companyName" template="tradingPartner" id="notifyName"
                                                            fields="notifyCode,notify_acct_type,notify_sub_type,NULL,NULL,NULL,notifySalesPersonCode,notifyDisabled,notifyAddress,notifyCity,notifyState,notifyCountry,notifyZip,NULL,notifyPhone,notifyFax,notifyEmail,notifyCommNo,NULL,NULL,notifyPoa,notifyCredit,NULL,NULL,NULL,NULL,NULL,notyDisableAcct,NULL,NULL,NULL,NULL,notifyScanSop,importCreditNofify"
                                                            query="TP_IMPORTS_NOTE"  width="600" container="NULL" value="${lclBooking.notyContact.companyName}" position="left" styleClass="textlabelsBoldForTextBox textCap" shouldMatch="true"
                                                            paramFields="notifySearchState,notifySearchZip,notifySearchSalesCode,notifyCountryUnLocCode"  callback="notify_AccttypeCheck('${path}');${notifyMethod};notesCount('${path}','notNotes',$('#notifyCode').val(),'N');addClientHotCodes($('#notifyCode').val());" scrollHeight="300"/>
                                    </c:otherwise>
                                </c:choose>
                                <input type="hidden" name="notifyDisabled" id="notifyDisabled"/>
                                <input type="hidden" name="notyDisableAcct" id="notyDisableAcct"/>
                                <input type="hidden" name="notify_accttype" id="notify_acct_type"/>
                                <input type="hidden" name="notify_subtype" id="notify_sub_type"/>
                                <input type="hidden" name="notifyCredit" id="notifyCredit" value="${CreditForNotify}"/>
                                <input type="hidden" name="notifyScanSop" id="notifyScanSop" value="${ScanSopForNotify}"/>
                                <input type="hidden" name="importCreditNofify" id="importCreditNofify" />
                                <input type="hidden" name="notifyCommNo" id="notifyCommNo"/>
                                <input type="hidden" name="notifyAcctName" id="notifyAcctName" value="${lclBooking.notyAcct.accountName}"/>
                            </span>
                            <span id="manualNotify" style="display:none">
                                <cong:text name="notifyContactName" id="dupNotifyName" value="${lclBookingForm.notifyContactName}" styleClass="text textlabelsBoldForTextBox textCap"/></span>
                            <input type="checkbox" style="vertical-align: middle;" id="newNotify" name="newNotify" onclick="isValidateBillToNoty();" container="NULL" title="New"/>
                            <input type="checkbox" id="editTpNoty" name="editTpNoty" onclick="isUncheckedEditTpNoty();"
                                   style="vertical-align: middle;" title="Edit Notify TP Details"/>
                            <c:if test="${lclBookingForm.moduleName eq 'Imports'}">
                                <c:choose>
                                    <c:when test="${lclBookingForm.notifyIcon}">
                                        <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Consignee Notes"style="vertical-align: middle;"
                                             class="notNotes" onclick="displayNotes('${path}', $('#notifyCode').val(), 'notNotes', $('#fileId').val(), $('#fileNo').val());"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Consignee Notes" style="vertical-align: middle;"
                                             class="notNotes" onclick="displayNotes('${path}', $('#notifyCode').val(), 'notNotes', $('#fileId').val(), $('#fileNo').val());"/>
                                    </c:otherwise>
                                </c:choose>
                                <cong:img src="${path}/img/icons/iicon.png" width="16" height="16" alt="Client" id="noty" style="vertical-align: middle;"/>
                                <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" style="vertical-align: middle;"
                                     title="Click here to edit Notify Search options" onclick="showClientSearchOption('${path}', 'Notify')"/>
                                <input type="hidden" id="notifyAddress" name="notyContact.address" value="${lclBooking.notyContact.address}"/>
                                <input type="hidden" id="notifyCity" name="notyContact.city" value="${lclBooking.notyContact.city}"/>
                                <input type="hidden" id="notifyState" name="notyContact.state" value="${lclBooking.notyContact.state}"/>
                                <input type="hidden" id="notifyZip" name="notyContact.zip" value="${lclBooking.notyContact.zip}"/>
                                <input type="hidden" id="notifyCountry" name="notyContact.country" value="${lclBooking.notyContact.country}"/>
                                <input type="hidden" id="notifyPhone" name="notyContact.phone1" value="${lclBooking.notyContact.phone1}"/>
                                <input type="hidden" id="notifyFax" name="notyContact.fax1" value="${lclBooking.notyContact.fax1}"/>
                                <input type="hidden" id="notifyEmail" name="notyContact.email1" value="${lclBooking.notyContact.email1}"/>
                                <input type="hidden" id="notifySalesPersonCode" name="notyContact.salesPersonCode" value="${lclBooking.notyContact.salesPersonCode}"/>
                                <input type="hidden"   id="notifyColoadRetailRadio" name="notifyColoadRetailRadio" />
                                <input type="hidden" id="notifyUnLocCode"/>
                                <input type="hidden" name="notifySearchState" id="notifySearchState"/>
                                <input type="hidden" name="notifySearchZip" id="notifySearchZip"/>
                                <input type="hidden" name="notifySearchSalesCode" id="notifySearchSalesCode"/>
                                <input type="hidden" name="notifySearchCountry" id="notifySearchCountry"/>
                                <input type="hidden" name="notifyCountryUnLocCode" id="notifyCountryUnLocCode"/>
                                <input type="hidden" name="notyAcctName" id="notyAcctName" value="${lclBooking.notyAcct.accountName}"/>
                            </c:if>
                            <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display" style="vertical-align: middle;"  styleClass="contactR" onclick="openLclContactInfo('${path}','Notify')"/>
                            <c:choose>
                                <c:when test="${lclBookingForm.moduleName ne 'Imports'}">
                                    <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" id="notifyIcon" height="16" style="vertical-align: middle;" alt="display" styleClass="trading" 
                                              onclick="newTPAccountFromBooking('${path}','dupNotifyName','notifyAddress','C','notifyCountry','notifyCity',
                                              'notifyState','notifyZip','notifyPhone','notifyFax','notifyEmail','notifyCode','notifyCode','notyUnLocCode','newNotify')">
                                    </cong:img>
                                </c:when>
                                <c:otherwise>
                                    <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" id="notifyIcon" height="16" style="vertical-align: middle;" alt="display" styleClass="trading" 
                                              onclick="createNewTPAcct('${path}','LCL_IMPORT_NOTIFY','C','newNotify','dupNotifyName','notifyAddress','notifyCity',
                                              'notifyState','notifyUnLocCode','notifyPhone','notifyFax','notifyEmail','notifyZip',
                                              'notifySalesPersonCode','notifyColoadRetailRadio')"/>
                                </c:otherwise>
                            </c:choose>                           
                        </cong:td>
                    </cong:tr>
                    <c:if test="${lclBookingForm.moduleName ne 'Imports'}">
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Address</cong:td>
                            <cong:td>
                                <cong:textarea cols="30" rows="6" id="notifyAddress" name="notifyAddress" value="${lclBooking.notyContact.address}" styleClass="textlabelsBoldForTextBox textup"/>
                                <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                            </cong:td>
                        </cong:tr>

                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                            <cong:td>
                                <cong:autocompletor id="notifyCity" name="notyContact.city" template="three"  
                                                    fields="notifyCity,notifyCountry,notyUnLocCode,notifyState"
                                                    styleClass="textlabelsBoldForTextBox textCap"  query="PORT_CITY_STATE"  
                                                    width="600" container="NULL" value="${lclBooking.notyContact.city}" 
                                                    shouldMatch="true" scrollHeight="300">
                                </cong:autocompletor>
                                <input type="hidden" id="notyUnLocCode" name="notyUnLocCode"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl"> 	 State </cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="notifyState" name="notyContact.state" value="${lclBooking.notyContact.state}" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Zip</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="notifyZip" name="notyContact.zip" value="${lclBooking.notyContact.zip}" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Country</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="notifyCountry" name="notyContact.country" value="${lclBooking.notyContact.country}" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Phone</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="notifyPhone" name="notyContact.phone1" value="${lclBooking.notyContact.phone1}" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Fax</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="notifyFax" name="notyContact.fax1" value="${lclBooking.notyContact.fax1}" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Email</cong:td>
                            <cong:td>
                                <cong:text styleClass="text textlabelsBoldForTextBox textCap" id="notifyEmail" name="notyContact.email1" value="${lclBooking.notyContact.email1}" maxlength="100"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="td textlabelsBoldforlcl">Client Ref</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="notifyClientRef" name="notyReference" maxlength="50" value="${lclBookingForm.notyReference}"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Client</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="notifyClient" name="notifyClient" maxlength="50"/>
                                <input type="checkbox" id="newNotifyClient" name="newNotifyClient"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                            <cong:td>
                                <input type="text" class="text textlabelsBoldForTextBox textCap" tabindex="-1" id="newNotifyClientCity" name="newNotifyClientCity" maxlength="50"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">POA</cong:td>
                            <cong:td>
                                <span id="notify" name="warning"></span><cong:hidden name="notifyPoa" id="notifyPoa"/>
                            </cong:td>
                        </cong:tr>
                    </c:if>
                </cong:table>
            </cong:td><!-- Notify ends here -->
            <c:if test="${lclBookingForm.moduleName eq 'Imports'}">
                <cong:td width="35%" valign="top">
                    <cong:table width="100%">
                        <cong:tr>
                            <cong:td colspan="2" styleClass="report-header">2nd Notify Party</cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Code</cong:td>
                            <cong:td>
                                <cong:text id="notify2Code" name="notify2AcctNo" styleClass="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclBooking.notify2Contact.tradingPartner.accountno}"/>
                                <img src="${path}/jsps/LCL/images/folder_open.png" width="16" height="16" style="vertical-align: middle;"
                                     alt="details" title="Address Details" class="tradingDetails" onclick="displayTradingDetails('${path}', '${lclBooking.lclFileNumber.id}', 'notify2Code', 'notify2Name', 'dupNotify2Name', 'Notify2', 'notify2Address', 'notify2City', 'notify2State', 'notify2Country', 'notify2Phone', 'notify2Fax', 'notify2Email', 'notify2Zip', 'newNotify2', 'notify2SalesPersonCode', 'notify2ColoadRetailRadio');"/>
                                <span  class ="importCreditForNotify2 green bold">${ImportCreditForNotify2}</span>
                                &nbsp;<label  id="notify2CreditValues"  style="color: red">${creditForNotify2}</label>
                                &nbsp;<a  id="notify2ScanSopValues"  style="color: red;vertical-align: middle;" href="javascript:openSop('notify2Code')" >${ScanSopForNotify2}</a>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td  styleClass="td textlabelsBoldforlcl">Name</cong:td>
                            <cong:td>
                                <span id="dojoNotify2">
                                    <cong:autocompletor name="notify2Contact.companyName" template="tradingPartner" id="notify2Name" query="TP_IMPORTS_NOTE"  width="600" container="NULL"
                                                        fields="notify2Code,notify2AcctType,NULL,NULL,NULL,NULL,notify2SalesPersonCode,noty2Disabled,notify2Address,notify2City,notify2State,notify2Country,notify2Zip,NULL,notify2Phone,notify2Fax,notify2Email,NULL,NULL,NULL,NULL,notify2CreditDetails,NULL,NULL,NULL,NULL,NULL,noty2DisableAcct,NULL,NULL,NULL,NULL,notify2ScanSopDetails,importCreditNofify2"
                                                        value="${lclBooking.notify2Contact.companyName}" position="left" styleClass="textlabelsBoldForTextBox textCap"
                                                        shouldMatch="true" paramFields="notify2SearchState,notify2SearchZip,notify2SearchSalesCode,notify2CountryUnLocCode"  callback="notify2AccttypeCheck('${path}');setToolTipTp('Notify2','notify2Code','notify2Name','notify2');notesCount('${path}','notify2Notes',$('#notifyCode').val(),'N2');addClientHotCodes($('#notifyCode').val());" scrollHeight="300"/>
                                    <input type="hidden" name="notify2AcctType" id="notify2AcctType"/>
                                    <input type="hidden" name="noty2Disabled" id="noty2Disabled"/>
                                    <input type="hidden" name="noty2DisableAcct" id="noty2DisableAcct"/>
                                    <input type="hidden" name="notify2CreditDetails" id="notify2CreditDetails"/>
                                    <input type="hidden" name="notify2ScanSopDetails" id="notify2ScanSopDetails" />
                                    <input type="hidden" name="importCreditNofify2" id="importCreditNofify2" />
                                </span>
                                <span id="manualNotify2" style="display:none">
                                    <cong:text name="notify2ContactName" id="dupNotify2Name" value="${lclBookingForm.notify2ContactName}" styleClass="text textlabelsBoldForTextBox textCap"/></span>
                                <input type="checkbox" id="newNotify2" name="newNotify2" onclick="newCheckNotify2();" style="vertical-align: middle;" title="New"/>
                                <c:choose>
                                    <c:when test="${lclBookingForm.notify2Icon}">
                                        <img src="${path}/img/icons/e_contents_view1.gif" width="14" height="14" title="Click here to see Notify2 Notes" alt="green" style="vertical-align: middle;"
                                             class="notify2Notes" onclick="displayNotes('${path}', $('#notify2Code').val(), 'notify2Notes', $('#fileId').val(), $('#fileNo').val());"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${path}/img/icons/e_contents_view.gif" id="" width="14" height="14" title="Click here to see Notify2 Notes" alt="normal" style="vertical-align: middle;"
                                             class="notify2Notes" onclick="displayNotes('${path}', $('#notify2Code').val(), 'notify2Notes', $('#fileId').val(), $('#fileNo').val());"/>
                                    </c:otherwise>
                                </c:choose>
                                <cong:img src="${path}/img/icons/iicon.png" width="16" height="16" alt="Notify2" id="notify2" style="vertical-align: middle;"/>
                                <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" style="vertical-align: middle;"
                                     title="Click here to edit 2nd Notify Search options" onclick="showClientSearchOption('${path}', '2nd Notify Party')"/>
                                <input type="hidden" id="notify2Address" name="notify2Contact.address" value="${lclBooking.notify2Contact.address}"/>
                                <input type="hidden" id="notify2City" name="notify2Contact.city" value="${lclBooking.notify2Contact.city}"/>
                                <input type="hidden" id="notify2State" name="notify2Contact.state" value="${lclBooking.notify2Contact.state}"/>
                                <input type="hidden" id="notify2Zip" name="notify2Contact.zip" value="${lclBooking.notify2Contact.zip}"/>
                                <input type="hidden" id="notify2Country" name="notify2Contact.country" value="${lclBooking.notify2Contact.country}"/>
                                <input type="hidden" id="notify2Phone" name="notify2Contact.phone1" value="${lclBooking.notify2Contact.phone1}"/>
                                <input type="hidden" id="notify2Fax" name="notify2Contact.fax1" value="${lclBooking.notify2Contact.fax1}"/>
                                <input type="hidden" id="notify2Email" name="notify2Contact.email1" value="${lclBooking.notify2Contact.email1}"/>
                                <input type="hidden" id="notify2SalesPersonCode" name="notify2Contact.salesPersonCode" value="${lclBooking.notify2Contact.salesPersonCode}"/>
                                <input type="hidden" id="notify2ColoadRetailRadio" name="notify2ColoadRetailRadio" />
                                <input type="hidden" id="notify2UnLocCode"/>
                                <input type="hidden" name="notify2SearchState" id="notify2SearchState"/>
                                <input type="hidden" name="notify2SearchZip" id="notify2SearchZip"/>
                                <input type="hidden" name="notify2SearchSalesCode" id="notify2SearchSalesCode"/>
                                <input type="hidden" name="notify2SearchCountry" id="notify2SearchCountry"/>
                                <input type="hidden" name="notify2CountryUnLocCode" id="notify2CountryUnLocCode"/>
                                <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"  styleClass="contactR" onclick="openLclContactInfo('${path}','2ndNotify')" style="vertical-align: middle;"/>
                                <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" id="nnn" height="16" alt="display" styleClass="trading"  style="vertical-align: middle;"
                                          onclick="createNewTPAcct('${path}','LCL_IMPORT_NOTIFY2','C','newNotify2','dupNotify2Name','notify2Address','notify2City','notify2State','notify2UnLocCode','notify2Phone','notify2Fax','notify2Email','notify2Zip','notify2SalesPersonCode','notify2ColoadRetailRadio')"/>
                            </cong:td>
                        </cong:tr>
                    </cong:table>
                </cong:td><!-- Notify ends here -->
            </c:if>
        </cong:tr>
    </cong:table>
</div>