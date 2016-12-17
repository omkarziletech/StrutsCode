<%@include file="/taglib.jsp" %>
<div>
    <cong:table width="100%">
        <cong:tr>
            <cong:td valign="top">
                <cong:table width="100%">
                    <cong:tr>
                        <cong:td colspan="2" styleClass="report-header"> Supplier</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td">Name</cong:td>
                        <cong:td>
                            <cong:autocompletor id="supplierName" name="supContact.contactName" template="tradingPartner" 
                                                fields="supplierCode,NULL,NULL,NULL,NULL,NULL,NULL,NULL,supplierAddress,supplierCity,supplierState,supplierCountry,supplierZip,NULL,supplierPhone,supplierFax,supplierEmail,NULL,NULL,NULL,supplierPoa"
                                                query="CLIENT_WITH_CONSIGNEE"  width="600" container="NULL" value="${lclBl.supContact.contactName}"
                                                shouldMatch="true" callback="supplierAcctCheck()" scrollHeight="300px"/>
                            <input type="checkbox" id="newSupplier" name="newSupplier"/>
                            <cong:img src="${path}/jsps/LCL/images/display.gif" width="16" height="16" alt="display"/>
                            <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" height="16" alt="display"/>
                        </cong:td>

                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td">Code</cong:td>
                        <cong:td>
                            <cong:text  id="supplierCode" name="supAcct" styleClass="text-readonly" readOnly="true" value="${lclBl.supAcct.accountno}"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td">Address</cong:td>
                        <cong:td>
                            <cong:textarea cols="19" rows="5" id="supplierAddress" name="supContact.address" value="${lclBl.supContact.address}"/>
                            <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td">City</cong:td>
                        <cong:td>
                            <cong:text styleClass="text" id="supplierCity" name="supContact.city" value="${lclBl.supContact.city}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td"> State </cong:td>
                        <cong:td>
                            <cong:text styleClass="text" id="supplierState" name="supContact.state" value="${lclBl.supContact.state}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td">Zip</cong:td>
                        <cong:td>
                            <cong:text styleClass="text"  id="supplierZip" name="supContact.zip" value="${lclBl.supContact.zip}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td">Country</cong:td>
                        <cong:td>
                            <cong:text styleClass="text" id="supplierCountry" name="supContact.country" value="${lclBl.supContact.country}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td">Phone</cong:td>
                        <cong:td>
                            <cong:text styleClass="text" id="supplierPhone" name="supContact.phone1" value="${lclBl.supContact.phone1}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td">Fax</cong:td>
                        <cong:td>
                            <cong:text styleClass="text"  id="supplierFax" name="supContact.fax1" value="${lclBl.supContact.fax1}" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td">Email</cong:td>
                        <cong:td>
                            <cong:text styleClass="text" id="supplierEmail" name="supContact.email1" value="${lclBl.supContact.email1}" maxlength="100"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td">Client Ref</cong:td>
                        <cong:td>
                            <input type="text" class="text" tabindex="-1"  id="supplierClientRef" name="supplierClientRef" maxlength="100"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td">Client</cong:td>
                        <cong:td>
                            <input type="text" class="text" tabindex="-1" id="supplierClient" name="supplierClient" maxlength="100"/>
                            <input type="checkbox" id="newSupplierClient" name="newSupplierClient"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td">City</cong:td>
                        <cong:td>
                            <input type="text" class="text" tabindex="-1" id="newSupplierClientCity" name="newSupplierClientCity" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td  styleClass="td">POA</cong:td>
                        <cong:td><span id="warning" name="warning"/></span><cong:hidden name="supplierPoa" id="supplierPoa"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td><!-- Supplier ends here -->
        <cong:td valign="top">
            <cong:table width="100%">
                <cong:tr>
                    <cong:td colspan="2" styleClass="report-header"> Shippers</cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="td">Name</cong:td>
                    <cong:td>
                        <cong:autocompletor name="shipContact.contactName" template="tradingPartner" id="shipperName" fields="shipperCode,shipper_acct_type,shipper_sub_type,NULL,NULL,NULL,NULL,NULL,shipperAddress,shipperCity,shipperState,shipperCountry,shipperZip,shipperContactName,shipperPhone,shipperFax,shipperEmail,NULL,NULL,NULL,shipperPoa"
                                            query="CLIENT_WITH_CONSIGNEE"  width="600" container="NULL" shouldMatch="true" value="${lclBl.shipContact.contactName}"
                                            callback="shipper_AccttypeCheck()" scrollHeight="300px"/>
                        <input type="hidden" name="shipper_accttype" id="shipper_acct_type"/>
                        <input type="hidden" name="shipper_subtype" id="shipper_sub_type"/>
                        <cong:hidden name="shipper_contactName" id="shipperContactName"/>
                        <input type="checkbox" id="newShipper" name="newShipper"/>
                        <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                        <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" height="16" alt="display"/>
                    </cong:td>

                </cong:tr>
                <cong:tr>                        
                    <cong:td  styleClass="td">Code</cong:td>
                    <cong:td>
                        <cong:text  id="shipperCode" name="shipAcct" styleClass="text-readonly" readOnly="true" value="${lclBl.shipAcct.accountno}"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="td">Address</cong:td>
                    <cong:td>
                        <cong:textarea cols="19" rows="5" id="shipperAddress" name="shipContact.address" value="${lclBl.shipContact.address}"/>
                        <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="td">City</cong:td>
                    <cong:td>
                        <cong:text styleClass="text" id="shipperCity" name="shipContact.city" value="${lclBl.shipContact.city}" maxlength="50"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="td"> 	 State </cong:td>
                    <cong:td>
                        <cong:text styleClass="text"  id="shipperState" name="shipContact.state" value="${lclBl.shipContact.state}" maxlength="50"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="td">Zip</cong:td>
                    <cong:td>
                        <cong:text styleClass="text"  id="shipperZip" name="shipContact.zip" value="${lclBl.shipContact.zip}" maxlength="50"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="td"> Country</cong:td>
                    <cong:td>
                        <cong:text styleClass="text"  id="shipperCountry" name="shipContact.country" value="${lclBl.shipContact.country}" maxlength="50"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="td">Phone</cong:td>
                    <cong:td>
                        <cong:text styleClass="text" id="shipperPhone" name="shipContact.phone1" value="${lclBl.shipContact.phone1}" maxlength="50"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="td">Fax</cong:td>
                    <cong:td>
                        <cong:text styleClass="text" id="shipperFax" name="shipContact.fax1" value="${lclBl.shipContact.fax1}" maxlength="50"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="td">Email</cong:td>
                    <cong:td>
                        <cong:text styleClass="text" id="shipperEmail" name="shipContact.email1" value="${lclBl.shipContact.email1}" maxlength="100"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="td">Client Ref</cong:td>
                    <cong:td>
                        <input type="text" class="text" tabindex="-1" id="shipperClientRef" name="shipperClientRef" maxlength="100"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="td">Client</cong:td>
                    <cong:td>
                        <input type="text" class="text" tabindex="-1" id="shipperClient" name="shipperClient" maxlength="50"/>
                        <input type="checkbox" id="newShipperClient" name="newShipperClient"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="td">City</cong:td>
                    <cong:td>
                        <input type="text" class="text" tabindex="-1" id="newShipperClientCity" name="newShipperClientCity" maxlength="50"/>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td  styleClass="td">POA</cong:td>
                    <cong:td>
                        <span id="shipper" name="warning"/></span><cong:hidden name="shipperPoa" id="shipperPoa"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:td><!-- Shippers ends here -->
    <cong:td valign="top">
        <cong:table width="100%">
            <cong:tr>
                <cong:td colspan="2" styleClass="report-header"> Forwarder</cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td">Name</cong:td>
                <cong:td>
                    <cong:autocompletor name="fwdContact.contactName" template="tradingPartner" id="forwarderName" fields="forwarderCode,forwarder_acct_type,forwarder_sub_type,NULL,NULL,NULL,NULL,NULL,forwarderAddress,forwarderCity,forwarderState,forwarderCountry,forwarderZip,NULL,forwarderPhone,forwarderFax,forwarderEmail,NULL,NULL,NULL,forwarderPoa"
                                        query="CLIENT_WITH_CONSIGNEE"  width="600" container="NULL" value="${lclBl.fwdContact.contactName}"
                                        shouldMatch="true" callback="forwarder_AccttypeCheck()" scrollHeight="300px"/>
                    <input type="hidden" name="forwarder_accttype" id="forwarder_acct_type"/>
                    <input type="hidden" name="forwarder_subtype" id="forwarder_sub_type"/>
                    <input type="checkbox" id="newForwarder" name="newForwarder"/>
                    <cong:img src="${path}/jsps/LCL/images/display.gif" width="16" height="16" alt="display"/>
                    <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" height="16" alt="display"/>
                </cong:td>

            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td">Code</cong:td>
                <cong:td>
                    <cong:text id="forwarderCode" name="fwdAcct" styleClass="text-readonly" readOnly="true" value="${lclBl.fwdAcct.accountno}"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td">Address</cong:td>
                <cong:td>
                    <cong:textarea cols="19" rows="5" id="forwarderAddress" name="fwdContact.address" value="${lclBl.fwdContact.address}"/>
                    <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td">City</cong:td>
                <cong:td>
                    <cong:text styleClass="text" id="forwarderCity" name="fwdContact.city" value="${lclBl.fwdContact.city}" maxlength="50"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td"> 	 State </cong:td>
                <cong:td>
                    <cong:text styleClass="text" id="forwarderState" name="fwdContact.state" value="${lclBl.fwdContact.state}" maxlength="50"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td">Zip</cong:td>
                <cong:td>
                    <cong:text styleClass="text" id="forwarderZip" name="fwdContact.zip" value="${lclBl.fwdContact.zip}" maxlength="50"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td">Country</cong:td>
                <cong:td>
                    <cong:text styleClass="text"  id="forwarderCountry" name="fwdContact.country" value="${lclBl.fwdContact.country}" maxlength="50"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td">Phone</cong:td>
                <cong:td>
                    <cong:text styleClass="text"  id="forwarderPhone" name="fwdContact.phone1" value="${lclBl.fwdContact.phone1}" maxlength="50"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td">Fax</cong:td>
                <cong:td>
                    <cong:text styleClass="text" id="forwarderFax" name="fwdContact.fax1" value="${lclBl.fwdContact.fax1}" maxlength="50"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td">Email</cong:td>
                <cong:td>
                    <cong:text styleClass="text" id="forwarderEmail" name="fwdContact.email1" value="${lclBl.fwdContact.email1}" maxlength="100"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td">Client Ref</cong:td>
                <cong:td>
                    <input type="text" class="text" tabindex="-1" id="forwarderClientRef" name="forwarderClientRef" maxlength="50"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td">Client</cong:td>
                <cong:td>
                    <input type="text" class="text" tabindex="-1" id="forwarderClient" name="forwarderClient" maxlength="50"/>
                    <input type="checkbox" id="newForwarderClient" name="newForwarderClient"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td">City</cong:td>
                <cong:td>
                    <input type="text" class="text" tabindex="-1" id="newForwarderClientCity" name="newForwarderClientCity" maxlength="50"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td  styleClass="td">POA</cong:td>
                <cong:td>
                    <span id="forwarder" name="warning"/></span><cong:hidden name="forwarderPoa" id="forwarderPoa"/>
                </cong:td>
            </cong:tr>
        </cong:table>
    </cong:td><!-- Forwarder ends here -->
<cong:td valign="top">
    <cong:table width="100%">
        <cong:tr>
            <cong:td colspan="2" styleClass="report-header"> Consignee</cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Name</cong:td>
            <cong:td>
                <cong:autocompletor name="consContact.contactName" template="tradingPartner" id="consigneeName" fields="consigneeCode,consignee_acct_type,consignee_sub_type,NULL,NULL,NULL,NULL,NULL,consigneeAddress,consigneeCity,consigneeState,consigneeCountry,consigneeZip,NULL,consigneePhone,consigneeFax,consigneeEmail,NULL,NULL,NULL,consigneePoa"
                                    query="CLIENT_WITH_CONSIGNEE"  width="600" container="NULL" value="${lclBl.consContact.contactName}"
                                    shouldMatch="true" callback="consignee_AccttypeCheck()" scrollHeight="300px"/>
                <input type="hidden" name="consignee_accttype" id="consignee_acct_type"/>
                <input type="hidden" name="consignee_subtype" id="consignee_sub_type"/>
                <input type="checkbox" id="newConsignee" name="newConsignee"/>
                <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" height="16" alt="display"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Code</cong:td>
            <cong:td>
                <cong:text id="consigneeCode" name="consAcct" styleClass="text-readonly" readOnly="true" value="${lclBl.consAcct.accountno}"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Address</cong:td>
            <cong:td>
                <cong:textarea cols="19" rows="5" id="consigneeAddress" name="consContact.address" value="${lclBl.consContact.address}"/>
                <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
            </cong:td>
        </cong:tr>

        <cong:tr>
            <cong:td  styleClass="td">City</cong:td>
            <cong:td>
                <cong:text styleClass="text" id="consigneeCity" name="consContact.city" value="${lclBl.consContact.city}" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td"> 	 State </cong:td>
            <cong:td>
                <cong:text styleClass="text" id="consigneeState" name="consContact.state" value="${lclBl.consContact.state}" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Zip</cong:td>
            <cong:td>
                <cong:text styleClass="text" id="consigneeZip" name="consContact.zip" value="${lclBl.consContact.zip}" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Country</cong:td>
            <cong:td>
                <cong:text styleClass="text" id="consigneeCountry" name="consContact.country" value="${lclBl.consContact.country}" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Phone</cong:td>
            <cong:td>
                <cong:text styleClass="text" id="consigneePhone" name="consContact.phone1" value="${lclBl.consContact.phone1}" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Fax</cong:td>
            <cong:td>
                <cong:text styleClass="text" id="consigneeFax" name="consContact.fax1" value="${lclBl.consContact.fax1}" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Email</cong:td>
            <cong:td>
                <cong:text styleClass="text" id="consigneeEmail" name="consContact.email1" value="${lclBl.consContact.email1}" maxlength="100"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Client Ref</cong:td>
            <cong:td>
                <input type="text" class="text" tabindex="-1" id="consigneeClientRef" name="consigneeClientRef" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Client</cong:td>
            <cong:td>
                <input type="text" class="text" tabindex="-1" id="consigneeClient" name="consigneeClient" maxlength="50"/>
                <input type="checkbox" id="newConsigneeClient" name="newConsigneeClient"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">City</cong:td>
            <cong:td>
                <input type="text" class="text" tabindex="-1" id="newConsigneeClientCity" name="newConsigneeClientCity" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">POA</cong:td>
            <cong:td>
                <span id="consignee" name="warning"/></span><cong:hidden name="consigneePoa" id="consigneePoa"/>
            </cong:td>
        </cong:tr>
    </cong:table>
</cong:td><!-- Consignee ends here -->
<cong:td valign="top">
    <cong:table width="100%">
        <cong:tr>
            <cong:td colspan="2" styleClass="report-header"> Notify</cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Name</cong:td>
            <cong:td>
                <cong:autocompletor name="notyContact.contactName" template="tradingPartner" id="notifyName" fields="notifyCode,notify_acct_type,notify_sub_type,NULL,NULL,NULL,NULL,NULL,notifyAddress,notifyCity,notifyState,notifyCountry,notifyZip,NULL,notifyPhone,notifyFax,notifyEmail,NULL,NULL,NULL,notifyPoa"
                                    query="CLIENT_WITH_CONSIGNEE"  width="600" container="NULL" value="${lclBl.notyContact.contactName}"
                                    shouldMatch="true" callback="notify_AccttypeCheck()" scrollHeight="300px"/>
                <input type="hidden" name="notify_accttype" id="notify_acct_type"/>
                <input type="hidden" name="notify_subtype" id="notify_sub_type"/>
                <input type="checkbox" id="newNotify" name="newNotify"/>
                <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
                <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" height="16" alt="display"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Code</cong:td>
            <cong:td>
                <cong:text id="notifyCode" name="notyAcct" styleClass="text-readonly" readOnly="true" value="${lclBl.notyAcct.accountno}"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Address</cong:td>
            <cong:td>
                <cong:textarea cols="19" rows="5" id="notifyAddress" name="notyContact.address" value="${lclBl.notyContact.address}"/>
                <cong:img src="${path}/jsps/LCL/images/display.gif"  width="16" height="16" alt="display"/>
            </cong:td>
        </cong:tr>

        <cong:tr>
            <cong:td  styleClass="td">City</cong:td>
            <cong:td>
                <cong:text styleClass="text" id="notifyCity" name="notyContact.city" value="${lclBl.notyContact.city}" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td"> 	 State </cong:td>
            <cong:td>
                <cong:text styleClass="text" id="notifyState" name="notyContact.state" value="${lclBl.notyContact.state}" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Zip</cong:td>
            <cong:td>
                <cong:text styleClass="text" id="notifyZip" name="notyContact.zip" value="${lclBl.notyContact.zip}" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Country</cong:td>
            <cong:td>
                <cong:text styleClass="text" id="notifyCountry" name="notyContact.country" value="${lclBl.notyContact.country}" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Phone</cong:td>
            <cong:td>
                <cong:text styleClass="text" id="notifyPhone" name="notyContact.phone1" value="${lclBl.notyContact.phone1}" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Fax</cong:td>
            <cong:td>
                <cong:text styleClass="text" id="notifyFax" name="notyContact.fax1" value="${lclBl.notyContact.fax1}" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Email</cong:td>
            <cong:td>
                <cong:text styleClass="text" id="notifyEmail" name="notyContact.email1" value="${lclBl.notyContact.email1}" maxlength="100"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="td">Client Ref</cong:td>
            <cong:td>
                <input type="text" class="text" tabindex="-1" id="notifyClientRef" name="notifyClientRef" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">Client</cong:td>
            <cong:td>
                <input type="text" class="text" tabindex="-1" id="notifyClient" name="notifyClient" maxlength="50"/>
                <input type="checkbox" id="newNotifyClient" name="newNotifyClient"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">City</cong:td>
            <cong:td>
                <input type="text" class="text" tabindex="-1" id="newNotifyClientCity" name="newNotifyClientCity" maxlength="50"/>
            </cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td  styleClass="td">POA</cong:td>
            <cong:td>
                <span id="notify" name="warning"/></span><cong:hidden name="notifyPoa" id="notifyPoa"/>
            </cong:td>
        </cong:tr>
    </cong:table>

</cong:td><!-- Notify ends here -->

</cong:tr>
</cong:table>
</div>