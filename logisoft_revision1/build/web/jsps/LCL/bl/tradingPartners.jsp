<%-- 
    Document   : tradingPartners
    Created on : Oct 24, 2012, 11:17:33 AM
    Author     : logiware
--%>
<%@include file="/taglib.jsp" %>
<table align="center" width="100%" >
    <tr>
        <td width="100%">
            <table width="100%" id="noteTable" border="1" style="border:1px solid #C4C5C4">
                <tr class="tableHeadingNew"><td width="50%">SHIPPER/EXPORTER</td><td width="20%">DOCUMENT NO:</td><td width="30%" align="center">TERMS</td></tr>
                <tr>
                    <td width="50%"><table>
                            <tr>
                                <td class="textlabelsBoldforlcl" width="10%" valign="middle">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Shipper Name</td>
                                <td class="textlabelsBoldleftforlcl">
                                    <span id="dojoShipper">
                                        <cong:autocompletor name="shipContact.companyName" template="tradingPartner" id="shipperName"
                                                            fields="shipperCode,shipper_acct_type,shipper_sub_type,NULL,NULL,NULL,NULL,shipperDisabledforbl,shipperAddress,shipperCity,shipperState,NULL,shipperZip,shipperContactName,shipperPhone,shipperFax,shipperEmail,shipperPoa,NULL,NULL,status,NULL,shipperDisabledforblAcct,NULL,NULL,NULL,NULL,limit"
                                                            query="SHIPPER" paramFields="shipperSearchState,shipperSearchZip,shipperSearchSalesCode,shipperCountryUnLocCode"  width="600" container="NULL" shouldMatch="true"
                                                            value="${lclBl.shipContact.companyName}" focusOnNext="true" callback="shipper_Accttype_Check();shipCheck();" 
                                                            scrollHeight="200px" styleClass="textlabelsBoldForTextBox  textCapsLetter shipperClass"/>
                                    </span>
                                    <input type="hidden" name="origShipAcctName" id="origShipAcctName" value="${lclBl.shipAcct.accountName}"/>
                                    <span id="manualShipper" style="display:none">
                                        <cong:text name="shipContactName" id="dupShipperName" value="${lclBl.shipContact.companyName}" 
                                                   styleClass="textlabelsBoldForTextBox  textCapsLetter shipperClass"/></span>
                                    <span id="editAcctDiv" style="display:none">
                                        <cong:text name="editShipAcctName" id="editShipAcctName" value="${lclBl.shipContact.companyName}" 
                                                   styleClass="textlabelsBoldForTextBox  textCapsLetter shipperClass"/></span>
                                        <cong:checkbox id="newShipper" name="newShipper"  styleClass="commonClass" onclick="newShipperName();" container="NULL" title="TP Not Listed"/>
                                        <cong:checkbox id="ediShipperCheck" name="ediShipperCheck" styleClass="commonClass" onclick="editAccountForEdi(this);" 
                                                       container="NULL" title="Edit Customer Name"/>
                                    <img src="${path}/images/icons/search_filter.png" class="clientSearchEditBl hideImg shipperClass" 
                                         id="clientSearchEditBlS" title="Click here to edit Shipper Search options" onclick="showClientSearchOption('${path}', 'Shipper')"/>
                                    <input type="hidden" name="shipper_accttype" id="shipper_acct_type"/>
                                    <input type="hidden" name="shipper_subtype" id="shipper_sub_type"/>
                                    <input type="hidden" name="status" id="status" value="${shipCreditStatus}"/>
                                    <input type="hidden" name="limit" id="limit" value="${shipCreditLimt}"/>
                                    <cong:hidden name="shipper_contactName" id="shipperContactName"/>
                                    <cong:hidden id="shipperState" name="shipContact.state" value="${lclBl.shipContact.state}" />
                                    <cong:hidden id="shipperCity" name="shipContact.city" value="${lclBl.shipContact.city}"/>
                                    <cong:hidden id="shipperZip" name="shipContact.zip" value="${lclBl.shipContact.zip}" />
                                    <cong:hidden  id="shipperPhone" name="shipContact.phone1" value="${lclBl.shipContact.phone1}"/>
                                    <cong:hidden  id="shipperFax" name="shipContact.fax1" value="${lclBl.shipContact.fax1}" />
                                    <cong:hidden id="shipperEmail" name="shipContact.email1" value="${lclBl.shipContact.email1}"/>
                                    <input type="hidden" name="shipperSearchState" id="shipperSearchState"/>
                                    <input type="hidden" name="shipperSearchZip" id="shipperSearchZip"/>
                                    <input type="hidden" name="shipperSearchSalesCode" id="shipperSearchSalesCode"/>
                                    <input type="hidden" name="shipperSearchCountry" id="shipperSearchCountry"/>
                                    <input type="hidden" name="shipperCountryUnLocCode" id="shipperCountryUnLocCode"/>
                                    <input type="hidden" name="shipperDisabledforbl" id="shipperDisabledforbl"/>
                                    <input type="hidden" name="shipperDisabledforblAcct" id="shipperDisabledforblAcct"/>
                                </td>
                                <td class="textlabelsBoldforlcl"  align="left">Shipper Number</td><td></td>
                                <td class="textlabelsBoldleftforlcl">
                                    <cong:text  id="shipperCode"  name="shipAcct" styleClass="textlabelsBoldForTextBoxDisabledLook textsmall" readOnly="true" value="${lclBl.shipAcct.accountno}"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="textlabelsBoldleftforlcl" rowspan="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Shipper Address</td>
                                <td class="textlabelsBoldleftforlcl" rowspan="4">
                                    <cong:textarea cols="47" rows="6" id="shipperAddress" styleClass="textlabelsBoldForTextBox textCapsLetter shipperClass" 
                                                     name="shipContact.address" value="${lclBl.shipContact.address}"/>
                                </td>
                                <td style="padding:0px" class="textlabelsBoldforlcl" align="left">Credit Status -</td><td style="padding:0px"></td>
                                <td>
                                    <cong:label id="shipperCreditStatusValue" text="" style="align:left;font-weight: bold"/>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding:0px" class="textlabelsBoldforlcl" align="left">Credit Limit -</td><td style="padding:0px"></td>
                                <td>
                                    <cong:label id="shipperCreditLimitValue" text="" style="text-align:left;font-weight: bold"/>
                                </td>
                            </tr>
                        </table></td>
                    <td width="20%">
                        <table width="100%" border="0">
                            <tr>
                                <td width="100%">
                                    <cong:textarea  style="border: none;" cols="55" rows="10" id="documentNo" name="documentNo" styleClass="textlabelsBoldForTextBox" 
                                                    value="${lclBl.lclFileNumber.fileNumber},${consolidatedDocuments}"/>
                                </td>
                            </tr>
                        </table></td>
                    <td rowspan="3" width="30%">
                        <table border="0" height="250px" width="100%">
                            <tr><td class="textlabelsBoldleftforlcl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;PPD/COL/BOTH</td>
                                <td class="textlabelsBoldleftforlcl">
                            <c:set var="billToParty" value="${empty lclBl.billToParty ? 'F' : lclBl.billToParty}"/>
                            <cong:radio value="P" name="pcBoth" id="radioP" container="NULL" onclick="updateBillToCodeFromBillType('F','P')"/> P
                            <cong:radio value="C" name="pcBoth" id="radioC" container="NULL" onclick="updateBillToCodeFromBillType('A','C')"/> C
                            <cong:radio value="B" name="pcBoth" id="radioB" container="NULL"
                                        onclick="updateBillToBoth('${lclBl.lclFileNumber.id}','B');"/> B
                            <input type="hidden" id="existBillTerms" value="${lclBl.billingType}"/>
                    </td></tr>
                <tr><td class="textlabelsBoldleftforlcl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bill To:</td><td class="textlabelsBoldleftforlcl">
                        <cong:radio value="F" name="billPPD" id="billF" container="NULL" onclick="updateBillingType('F');"/> F
                        <cong:radio value="S" name="billPPD" id="billS" container="NULL" onclick="updateBillingType('S');"/> S
                        <cong:radio value="T" name="billPPD" id="billT" container="NULL" onclick="updateBillingType('T');"/> T
                        <cong:radio value="A" name="billPPD" id="billA" container="NULL" onclick="updateBillingType('A');"/> A
                        <input type="hidden" id="hiddenBillToCode" value="${lclBl.billToParty}"/>
                    </td></tr>
                <tr><td class="textlabelsBoldleftforlcl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Third Party Name</td><td class="textlabelsBoldleftforlcl">
                        <cong:autocompletor  name="thirdPartyname" styleClass="textlabelsBoldForTextBox" position="left"  id="thirdPartyname"
                                             fields="thirdpartyaccountNo,NULL,NULL,NULL,NULL,NULL,NULL,thirdPartyDisabled,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,thirdPartyStatus,thirdPartyLimit,NULL,thirdPartyDisabledAcct"
                                             paramFields="thirdSearchState,thirdSearchZip,thirdPartySearchSalesCode,thirdPartySearchCoCode" shouldMatch="true" width="600" query="THIRD_PARTY_BL" template="tradingPartner"
                                             callback="thirdParty_Accttype_Check();"  scrollHeight="300px" container="NULL" value="${lclBl.thirdPartyAcct.accountName}"/>

                        <img src="${path}/images/icons/search_filter.png" class="clientSearchEditBl hideImg" style="vertical-align: middle" alt="search"
                             id="clientSearchEditBlT" title="Click here to edit Third party Search options" onclick="showClientSearchOption('${path}', 'ThirdParty')"/>

                        <input type="hidden" name="thirdPartyStatus" id="thirdPartyStatus" value="${thirdCreditStatus}"/>
                        <input type="hidden" name="thirdPartyLimit" id="thirdPartyLimit" value="${thirdCreditLimit}"/>
                        <input type="hidden" name="thirdSearchState" id="thirdSearchState"/>
                        <input type="hidden" name="thirdSearchZip" id="thirdSearchZip"/>
                        <input type="hidden" name="thirdPartySearchSalesCode" id="thirdPartySearchSalesCode"/>
                        <input type="hidden" name="thirdPartySearchCoCode" id="thirdPartySearchCoCode"/>
                        <input type="hidden" name="thirdPartyDisabled" id="thirdPartyDisabled"/>
                        <input type="hidden" name="thirdPartyDisabledAcct" id="thirdPartyDisabledAcct"/>
                    </td></tr>
                <tr><td class="textlabelsBoldleftforlcl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Third Party Acct#</td><td class="textlabelsBoldleftforlcl">
                        <cong:text name="thirdPartyAccount" styleClass="textlabelsBoldForTextBoxDisabledLook" id="thirdpartyaccountNo" container="NULL" readOnly="true"
                                   value="${lclBl.thirdPartyAcct.accountno}"/>
                    </td></tr>
                <tr><td class="textlabelsBoldleftforlcl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Credit Status -</td><td>
                        <cong:label style="align:left;font-weight: bold" id="thirdpartyCreditStatusValue" text=""/>
                    </td></tr>
                <tr><td class="textlabelsBoldleftforlcl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Credit Limit&nbsp;&nbsp;&nbsp; -</td><td>
                        <cong:label id="thirdpartyCreditLimitValue" text="" style="text-align:left;font-weight: bold"/>
                    </td></tr>
                <tr><td class="textlabelsBoldleftforlcl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Terms/Type 1</td><td>
                <html:select property="termsType1" value="${lclBlForm.termsType1}" styleClass="smallDropDown textlabelsBoldforlcl" styleId="termsType1" style="width:134px" onchange="roleDutyCheck();
                        saveTerms();">
                    <html:optionsCollection name="lclBlForm"  label="label" value="value" property="termsTypeList" />
                </html:select>

        </td></tr>
    <tr><td class="textlabelsBoldleftforlcl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Terms Type 2</td>
        <td>
    <html:select property="termsType2" value="${lclBlForm.termsType2}" styleClass="smallDropDown textlabelsBoldforlcl" styleId="termsType2" style="width:134px">
        <html:optionsCollection name="lclBlForm"  label="label" value="value" property="termsType2List" />
    </html:select>
</td></tr>
<c:set var="type2DateFmt" value="${not empty lclBl.type2Date ? lclBl.type2Date :''}"/>
<fmt:formatDate pattern="dd-MMM-yyyy" var="type2dte" value="${type2DateFmt}"/>
<tr><td class="textlabelsBoldleftforlcl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Type 2 Date</td>
    <td>
<c:set var="type2DateFmt" value="${not empty lclBl.type2Date ? lclBl.type2Date :''}"/>
<fmt:formatDate pattern="dd-MMM-yyyy" var="type2dte" value="${type2DateFmt}"/>
<cong:calendar container ="null" styleClass="textlabelsBoldForTextBox" id="type2Date" name="type2Date" value="${type2dte}"/>
</td>
</tr>
<tr><td class="textlabelsBoldleftforlcl">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Rates From term</td>
    <td class="textlabelsBoldleftforlcl">
        <cong:autocompletor id="ratesFromTerminal" name="ratesFromTerminal" template="commTempFormat" position="left"
                            styleClass="textlabelsBoldForTextBox" query="TERM_BL" fields="NULL,ratesFromTerminalNo"
                            width="200" container="NULL"   shouldMatch="true" scrollHeight="200px"/>
        <cong:hidden name="ratesFromTerminalNo" id="ratesFromTerminalNo" value="${lclBlForm.ratesFromTerminalNo}"/>
    </td></tr>
</table>
</td> </tr>   
<tr class="tableHeadingNew"><td width="50%">CONSIGNEE</td><td width="30%">EXPORT REFERENCE</td></tr>   
<tr>
    <td width="50%" height="30%"><table  width="100%">
            <tr>
                <td class="textlabelsBoldforlcl" width="10%" valign="middle">&nbsp;Consignee Name</td>
                <td class="textlabelsBoldleftforlcl">
                    <span id="dojoConsignee">
                        <cong:autocompletor name="consContact.companyName" template="tradingPartner" id="consigneeName" 
                                            fields="consigneeCode,consignee_acct_type,consignee_subtype,NULL,NULL,NULL,NULL,consigneeDisabledforbl,consigneeAddress,consigneeCity,consigneeState,consigneeCountry,consigneeZip,consigneeContactName,consigneePhone,consigneeFax,consigneeEmail,NULL,NULL,NULL,consStatus,NULL,consLimit,consigneeDisabledforblAcct,NULL,NULL,NULL"
                                            query="EXP_CONSIGNEE"  width="600" container="NULL" value="${lclBl.consContact.companyName}" paramFields="consigneeSearchState,consigneeSearchZip,consigneeSearchSalesCode,consigneeCountryUnLocCode" shouldMatch="true"
                                            callback="consignee_Accttype_Check();consCheck();setExportsReference();" focusOnNext="true" scrollHeight="250px" 
                                            styleClass="textlabelsBoldForTextBox  textCapsLetter commonClass"/>
                    </span>
                    <input type="hidden" name="origConsAcctName" id="origConsAcctName" value="${lclBl.consAcct.accountName}"/>
                    <span id="manualConsignee" style="display:none">
                        <cong:text name="consContactName" id="dupConsigneeName" value="${lclBl.consContact.companyName}" 
                                   styleClass="textlabelsBoldForTextBox  textCapsLetter commonClass"/></span>
                    <span id="editConsAcctDiv" style="display:none">
                        <cong:text name="editConsAcctName" id="editConsAcctName" value="${lclBl.consContact.companyName}" 
                                   styleClass="textlabelsBoldForTextBox  textCapsLetter commonClass"/></span>
                    <input type="hidden" name="consignee_accttype" id="consignee_acct_type"/>
                    <input type="hidden" name="consignee_subtype" id="consignee_sub_type"/>
                    <input type="hidden" name="consStatus" id="consStatus" value="${consCreditStatus}"/>
                    <input type="hidden" name="consLimit" id="consLimit" value="${consCreditLimt}"/>
                    <cong:hidden name="consignee_contactName" id="consigneeContactName"/>
                    <cong:hidden id="consigneeState" name="consContact.state" value="${lclBl.consContact.state}" />
                    <cong:hidden id="consigneeCity" name="consContact.city" value="${lclBl.consContact.city}"/>
                    <cong:hidden id="consigneeZip" name="consContact.zip" value="${lclBl.consContact.zip}" />
                    <cong:hidden  id="consigneeCountry" name="consContact.country" value="${lclBl.consContact.country}" />
                    <cong:hidden  id="consigneePhone" name="consContact.phone1" value="${lclBl.consContact.phone1}"/>
                    <cong:hidden  id="consigneeFax" name="consContact.fax1" value="${lclBl.consContact.fax1}" />
                    <cong:hidden id="consigneeEmail" name="consContact.email1" value="${lclBl.consContact.email1}"/>
                    <input type="hidden" name="consigneeSearchState" id="consigneeSearchState"/>
                    <input type="hidden" name="consigneeSearchZip" id="consigneeSearchZip"/>
                    <input type="hidden" name="consigneeCountryUnLocCode" id="consigneeCountryUnLocCode"/>
                    <input type="hidden" name="consigneeSearchSalesCode" id="consigneeSearchSalesCode"/>
                    <input type="hidden" name="consigneeCountryUnLocCode" id="consigneeCountryUnLocCode"/>
                    <input type="hidden" name="consigneeDisabledforbl" id="consigneeDisabledforbl"/>
                    <input type="hidden" name="consigneeDisabledforblAcct" id="consigneeDisabledforblAcct"/>
                    <cong:checkbox id="newConsignee" name="newConsignee" styleClass="commonClass" onclick="newConsigneeName();" 
                                   container="NULL" title="TP Not Listed" />
                    <cong:checkbox id="ediConsigneeCheck" styleClass="commonClass" name="ediConsigneeCheck" onclick="editAccountForEdi(this);" 
                                   container="NULL" title="Edit Customer Name"/>
                    <img src="${path}/images/icons/search_filter.png" class="clientSearchEditBl hideImg commonClass" id="clientSearchEditBlC" 
                         title="Click here to edit Consignee Search options" onclick="showClientSearchOption('${path}', 'Consignee')"/>
                </td>
                <td class="textlabelsBoldforlcl">Consignee Number</td><td></td>
                <td class="textlabelsBoldleftforlcl">
                    <cong:text id="consigneeCode" name="consAcct" styleClass="textlabelsBoldForTextBoxDisabledLook textsmall" 
                               readOnly="true" value="${lclBl.consAcct.accountno}"/>
                </td>
            </tr>
            <tr>
                <td class="textlabelsBoldleftforlcl"  rowspan="4">&nbsp;Consignee Address</td>
                <td class="textlabelsBoldleftforlcl"  rowspan="4">
                    <cong:textarea cols="47" rows="6" id="consigneeAddress" styleClass="textlabelsBoldForTextBox  textCapsLetter commonClass" 
                                       name="consContact.address" value="${lclBl.consContact.address}"/>
                </td>
                <td style="padding:0px" class="textlabelsBoldforlcl" align="left">Credit Status -</td><td></td>
                <td>
                    <cong:label style="align:left;font-weight: bold" id="consCreditStatusValue" text=""/>
                </td>
            </tr>
            <tr>
                <td style="padding:0px" class="textlabelsBoldforlcl" align="left">Credit Limit -</td><td></td>
                <td>
                    <cong:label style="align:left;font-weight: bold" id="consCreditLimitValue" text=""/>
                </td>
            </tr>
        </table>
    </td>
    <td width="30%" height="30%">
        <table width="100%">
            <tr>
                <td>
                    <cong:textarea name="eReference" id="eReference" cols="45" rows="3"
                                   styleClass="textlabelsBoldForTextBox  textCapsLetter commonClass"
                                   value="${lclBlForm.eReference}" style="text-align:left;margin-top:-55px;overflow:hidden;width:70%;"  onkeyup="limitTexts();"/>
                </td>    
            </tr>
        </table>
    </td>
</tr>   
<tr class="tableHeadingNew"><td width="50%">NOTIFY PARTY</td><td colspan="2" width="50%">FORWARDER</td></tr> 
<tr>
    <td width="50%" height="30%">
        <table  width="100%">
            <tr>
                <td class="textlabelsBoldforlcl" width="10%" valign="middle">Notify Party Name</td>
                <td class="textlabelsBoldleftforlcl">
                    <span id="dojoNotify">
                        <cong:autocompletor name="notyContact.companyName" template="tradingPartner" id="notifyName" fields="notifyCode,notify_acct_type,notify_sub_type,NULL,NULL,NULL,NULL,notifyforbl,notifyAddress,notifyCity,notifyState,notifyCountry,notifyZip,NULL,notifyPhone,notifyFax,notifyEmail,notifyPoa,NULL,NULL,NULL,notiStatus,NULL,NULL,NULL,NULL,NULL,notiLimit,notifyforblAcct"
                                            query="CONSIGNEE_BL"  width="600"  focusOnNext="true" callback="notify_Accttype_Check();notyCheck();"  container="NULL" value="${lclBl.notyContact.companyName}" 
                                            styleClass="mandatory textlabelsBoldForTextBox textCapsLetter commonClass" paramFields="notifySearchState,notifySearchZip,notifySearchSalesCode,notifyCountryUnLocCode,notifySearchCountry" 
                                            shouldMatch="true" scrollHeight="250px"/></span>
                    <input type="hidden" name="origNotyAcctName" id="origNotyAcctName" value="${lclBl.notyAcct.accountName}"/>
                    <span id="manualNotify" style="display:none">
                        <cong:text name="notifyContactName" id="dupNotifyName" styleClass="mandatory textlabelsBoldForTextBox textCapsLetter commonClass" value="${lclBl.notyContact.companyName}" /></span>
                    <span id="editNotyAcctDiv" style="display:none">
                        <cong:text name="editNotyAcctName" id="editNotyAcctName" value="${lclBl.notyContact.companyName}" styleClass="textlabelsBoldForTextBox  textCapsLetter"/></span>
                        <cong:checkbox id="newNotify" name="newNotify" onclick="newNotifyName();"  container="NULL" title="TP Not Listed" styleClass="commonClass"/>
                        <cong:checkbox id="ediNotifyCheck" name="ediNotifyCheck" onclick="editAccountForEdi(this);" container="NULL" styleClass="commonClass" title="Edit Customer Name" />
                        <cong:checkbox id="sameasConsignee" name="sameasConsignee" onclick="insertNotifyParty();"  container="NULL" styleClass="commonClass" title="Same as consignee" />
                    <img src="${path}/images/icons/search_filter.png" class="clientSearchEditBl commonClass" id="clientSearchEditBlN" title="Click here to edit Notify Search options" onclick="showClientSearchOption('${path}', 'Notify')"/>
                    <input type="hidden" name="notify_accttype" id="notify_acct_type"/>
                    <input type="hidden" name="notify_subtype" id="notify_sub_type"/>
                    <input type="hidden" name="notiStatus" id="notiStatus" value="${notyCreditStatus}"/>
                    <input type="hidden" name="notiLimit" id="notiLimit" value="${notyCreditLimit}"/>
                    <cong:hidden styleClass="text" id="notifyCity" name="notyContact.city" value="${lclBl.notyContact.city}"/>
                    <cong:hidden styleClass="text" id="notifyState" name="notyContact.state" value="${lclBl.notyContact.state}"/>
                    <cong:hidden styleClass="text" id="notifyZip" name="notyContact.zip" value="${lclBl.notyContact.zip}"/>
                    <cong:hidden styleClass="text" id="notifyCountry" name="notyContact.country" value="${lclBl.notyContact.country}"/>
                    <cong:hidden styleClass="text" id="notifyPhone" name="notyContact.phone1" value="${lclBl.notyContact.phone1}"/>
                    <cong:hidden styleClass="text" id="notifyFax" name="notyContact.fax1" value="${lclBl.notyContact.fax1}"/>
                    <cong:hidden styleClass="text" id="notifyEmail" name="notyContact.email1" value="${lclBl.notyContact.email1}"/>
                    <input type="hidden" name="notifySearchState" id="notifySearchState"/>
                    <input type="hidden" name="notifySearchZip" id="notifySearchZip"/>
                    <input type="hidden" name="notifySearchSalesCode" id="notifySearchSalesCode"/>
                    <input type="hidden" name="notifyCountryUnLocCode" id="notifyCountryUnLocCode"/>
                    <input type="hidden" name="notifySearchCountry" id="notifySearchCountry"/>
                    <input type="hidden" name="notifyforbl" id="notifyforbl"/>
                    <input type="hidden" name="notifyforblAcct" id="notifyforblAcct"/>
                    <input type="hidden" class="text" tabindex="-1" id="notifyClientRef" name="notifyClientRef" maxlength="50"/>
                </td>

                <td class="textlabelsBoldforlcl">Notify Party Number</td><td></td>
                <td class="textlabelsBoldleftforlcl">
                    <cong:text id="notifyCode" name="notyAcct" styleClass="textlabelsBoldForTextBoxDisabledLook textsmall" readOnly="true" value="${lclBl.notyAcct.accountno}"/>
                </td>
            </tr>
            <tr>
                <td class="textlabelsBoldleftforlcl" rowspan="4">Notify Party Address</td>
                <td class="textlabelsBoldleftforlcl" rowspan="4">
                    <cong:textarea cols="47" rows="6" id="notifyAddress" name="notyContact.address" 
                                   styleClass="textlabelsBoldForTextBox  textCapsLetter commonClass" value="${lclBl.notyContact.address}"/>
                </td>
                <td style="padding:0px" class="textlabelsBoldforlcl" align="left">Credit Status -</td><td></td>
                <td>
                    <cong:label style="align:left;font-weight: bold" id="notyCreditStatusValue" text=""/>
                </td>
            </tr>
            <tr>
                <td style="padding:0px" class="textlabelsBoldforlcl" align="left">Credit Limit -</td><td></td>
                <td>
                    <cong:label style="align:left;font-weight: bold" id="notyCreditLimitValue" text=""/>
                </td>
            </tr>
        </table>
    </td>
    <td width="50%" height="30%" colspan="2">
        <table  width="100%">
            <tr>
                <td class="textlabelsBoldforlcl" width="10%" valign="middle">Forwarder Name</td>
                <td class="textlabelsBoldleftforlcl">
                    <span id="fwdDojo">
                        <cong:autocompletor name="fwdContact.companyName" template="tradingPartner" id="forwarderName"
                                            fields="forwarderCode,forwarder_acct_type,forwarder_sub_type,NULL,NULL,NULL,NULL,frwddisabledforbl,forwarderAddressa,forwarderCity,forwarderState,NULL,forwarderZip,NULL,forwarderPhone,forwarderFax,forwarderEmail,fmc,fwdStatus,fwdLimit,frwddisabledforblAcct"
                                            query="FORWARDER_BL" focusOnNext="true" callback="forwarder_Accttype_Check();"  width="600" container="NULL" value="${lclBl.fwdContact.companyName}" styleClass="mandatory textlabelsBoldForTextBox textCapsLetter"
                                            paramFields="frwdSearchState,frwdSearchZip,frwdSearchSalesCode,frwdSearchCountryUnLocCode" shouldMatch="true" scrollHeight="250px" position="left"/></span>
                    <input type="hidden" name="origFwdAcctName" id="origFwdAcctName" value="${lclBl.fwdAcct.accountName}"/>
                    <input type="hidden" name="forwarder_accttype" id="forwarder_acct_type"/>
                    <input type="hidden" name="forwarder_subtype" id="forwarder_sub_type"/>
                    <input type="hidden" name="fwdStatus" id="fwdStatus" value="${fwdCreditStatus}"/>
                    <input type="hidden" name="fwdLimit" id="fwdLimit" value="${fwdCreditLimit}"/>



                    <span id="editFwdAcctDiv" style="display:none">
                        <cong:text name="editFwdAcctName" id="editFwdAcctName" value="${lclBl.fwdContact.companyName}" styleClass="textlabelsBoldForTextBox  textCapsLetter"/>
                    </span>
                    <cong:checkbox id="ediForwarderCheck" name="ediForwarderCheck" onclick="editAccountForEdi(this);" container="NULL" title="Edit Customer Name" />
                    <img src="${path}/images/icons/search_filter.png" class="clientSearchEditBl hideImg" id="clientSearchEditBlSF" title="Click here to edit Forwarder Search options" onclick="showClientSearchOption('${path}', 'Forwarder')"/>
                    <cong:hidden id="forwarderCity" name="fwdContact.city" value="${lclBl.fwdContact.city}"/>
                    <cong:hidden id="forwarderState" name="fwdContact.state" value="${lclBl.fwdContact.state}" />
                    <cong:hidden id="forwarderZip" name="fwdContact.zip" value="${lclBl.fwdContact.zip}"/>
                    <cong:hidden id="forwarderPhone" name="fwdContact.phone1" value="${lclBl.fwdContact.phone1}"/>
                    <cong:hidden id="forwarderFax" name="fwdContact.fax1" value="${lclBl.fwdContact.fax1}"/>
                    <cong:hidden id="forwarderEmail" name="fwdContact.email1" value="${lclBl.fwdContact.email1}"/>
                    <input type="hidden" name="frwdSearchState" id="frwdSearchState"/>
                    <input type="hidden" name="frwdSearchZip" id="frwdSearchZip"/>
                    <input type="hidden" name="frwdSearchSalesCode" id="frwdSearchSalesCode"/>
                    <input type="hidden" name="frwdSearchCountryUnLocCode" id="frwdSearchCountryUnLocCode"/>
                    <input type="hidden" name="frwdSearchCountry" id="frwdSearchCountry"/>
                    <input type="hidden" name="frwddisabledforbl" id="frwddisabledforbl"/>
                    <input type="hidden" name="frwddisabledforblAcct" id="frwddisabledforblAcct"/>
                </td>
                <td class="textlabelsBoldforlcl">Forwarder Number</td>
                <td class="textlabelsBoldleftforlcl">
                    <cong:text id="forwarderCode" name="fwdAcct" styleClass="textlabelsBoldForTextBoxDisabledLook textsmall" readOnly="true" value="${lclBl.fwdAcct.accountno}"/>
                </td>
            </tr>
            <tr>
                <td class="textlabelsBoldleftforlcl" rowspan="3">Forwarder Address</td>
                <td class="textlabelsBoldleftforlcl" rowspan="3">
                    <cong:textarea cols="47" rows="6" id="forwarderAddressa"  name="fwdContact.address" styleClass="textlabelsBoldForTextBox  textCapsLetter" value="${lclBl.fwdContact.address}"/>
                </td>
                <td class="textlabelsBoldforlcl">FMC#</td>
                <td class="textlabelsBoldleftforlcl">
                    <cong:text id="fmc" name="fmc" styleClass="textlabelsBoldForTextBoxDisabledLook textsmall" container="NULL" readOnly="true" value="${lclBl.fwdAcct.generalInfo.fwFmcNo}"/>
                </td>
            </tr>
            <tr>
                <td class="textlabelsBoldleftforlcl">Credit Status -</td>
                <td>
                    <cong:label style="align:left;font-weight: bold" id="fwdCreditStatusValue" text=""/>
                </td>
            </tr>
            <tr>
                <td  class="textlabelsBoldleftforlcl">Credit Limit -</td>
                <td valign="middle">
                    <cong:label style="align:left;font-weight: bold" id="fwdCreditLimitValue" text=""/>
                </td>
            </tr>
            <tr>
                <td colspan="11" class="tableHeadingNew">POINT AND COUNTRY OF ORIGIN</td>
            </tr>
            <tr><td colspan="11" class="textlabelsBoldleftforlcl">
            <c:choose>
                <c:when test="${lclBl.pointOfOrigin ne null}">
                    <cong:text id="" name="pointOfOrigin"  styleClass="text textuppercaseLetter" value="${lclBl.pointOfOrigin}"/>
                </c:when>
                <c:otherwise>
                    <cong:text id="" name="pointOfOrigin"  styleClass="text textuppercaseLetter" value="U.S.A"/>
                </c:otherwise>
            </c:choose>
    </td></tr>
</table>
</td>
</tr>
</table>
</td>
</tr>        
</table>
