<%-- 
    Document   : miscellaneous
    Created on : Dec 2, 2014, 9:36:58 AM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="table no-margin-border">
    <tr>
        <c:choose>
            <c:when test="${roleDuty.changeTpType}">
                <td class="label" colspan="2">Account Type:&nbsp;
                    <html:checkbox property="accountType1" styleId="accountType1" name="tradingPartnerForm" onclick="checkShipperOrForwarder()" title="Shipper"/>S&nbsp;
                    <html:checkbox property="accountType3" styleId="accountType3" name="tradingPartnerForm" disabled="true" title="NVOCC"/>N&nbsp;
                    <html:checkbox property="accountType4" styleId="accountType4" name="tradingPartnerForm" onclick="getNotify();checkConsigneeAccount();" title="Consignee"/>C&nbsp;
                    <html:checkbox property="accountType8" styleId="accountType8" name="tradingPartnerForm" title="Import Agent"/>AI&nbsp;
                    <html:checkbox property="accountType9" styleId="accountType9" name="tradingPartnerForm" title="Export Agent"/>AE&nbsp;
                    <html:checkbox property="accountType10" styleId="accountType10" name="tradingPartnerForm" onclick="checkForVendor();checkVendorAccount();" title="Vendor"/>V&nbsp;
                    <html:checkbox property="accountType11" styleId="accountType11" name="tradingPartnerForm" disabled="true" title="Others"/>O&nbsp;
                    <c:if test="${loginuser.role.roleDesc == commonConstants.ROLE_NAME_ADMIN}">
                        <html:checkbox property="accountType13" styleId="accountType13" name="tradingPartnerForm" title="Company"/>Z&nbsp;
                    </c:if>
                </td>
                <td class="label">Sub Type</td>
                <td>
                    <html:select property="subType" styleId="subType" styleClass="dropdown" onchange="onChangeSubType();">
                        <html:optionsCollection name="subTypeList"/>
                    </html:select>
                </td>
            </c:when>
            <c:otherwise>
                <td class="label" colspan="2">Account Type:&nbsp;
                    <html:checkbox property="accountType1" styleId="accountType1" name="tradingPartnerForm"  disabled="true" title="Shipper"/>S&nbsp;
                    <html:checkbox property="accountType3" styleId="accountType3" name="tradingPartnerForm" disabled="true" title="NVOCC"/>N&nbsp;
                    <html:checkbox property="accountType4" styleId="accountType4" name="tradingPartnerForm" disabled="true" title="Consignee"/>C&nbsp;
                    <html:checkbox property="accountType8" styleId="accountType8" name="tradingPartnerForm" disabled="true" title="Import Agent"/>AI&nbsp;
                    <html:checkbox property="accountType9" styleId="accountType9" name="tradingPartnerForm" disabled="true" title="Export Agent"/>AE&nbsp;
                    <html:checkbox property="accountType10" styleId="accountType10" name="tradingPartnerForm" disabled="true" title="Vendor"/>V&nbsp;
                    <html:checkbox property="accountType11" styleId="accountType11" name="tradingPartnerForm" disabled="true" title="Others"/>O&nbsp;
                    <c:if test="${loginuser.role.roleDesc == commonConstants.ROLE_NAME_ADMIN}">
                        <html:checkbox property="accountType13" styleId="accountType13" name="tradingPartnerForm" disabled="true" title="Company"/>Z&nbsp;
                    </c:if>
                </td>
                <td class="label">Sub Type</td>
                <td>
                    <html:select property="subType" styleId="subType" styleClass="dropdown" onchange="onChangeSubType();" disabled="true">
                        <html:optionsCollection name="subTypeList"/>
                    </html:select>
                </td>
            </c:otherwise>
        </c:choose>
        <c:if test="${not master}">
            <td>Master</td>
            <td>
                <c:choose>
                    <c:when test="${roleDuty.changeMaster}">
                        <html:select property="master" styleId="master" styleClass="dropdown" onchange = "setMaster();">
                            <html:optionsCollection name="mastertypelist"/>
                        </html:select>
                    </c:when>
                    <c:otherwise>
                        <html:text property="master" styleId="master" styleClass="textbox readonly" readonly="true"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </c:if> 
    </tr>
    <tr>
        <c:choose>
            <c:when test="${roleDuty.editEciAcct}">
                <td class="label">ECI Shpr/FF#</td>
                <td>
                    <html:text styleId="eciAccountNo" property="eciAccountNo" size="5" maxlength="5" styleClass="textbox" onblur="validateShipper()" onchange="checkShipper()"/>
                </td>
                <td class="label">ECI Consignee</td>
                <td>
                    <html:text styleId="eciAccountNo2" property="eciAccountNo2" size="5" maxlength="5" styleClass="textbox" onblur="validateConsignee()" onchange="checkConsignee()"/>
                </td>
                <td class="label">ECI Vendor</td>
                <td>
                    <html:text  styleId="eciAccountNo3" property="eciAccountNo3" size="5" maxlength="5" styleClass="textbox" onblur="validateVendor()" onchange="checkVendor()"/>
                </td>
                <td class="label">SSLine Number</td>
                <td>
                    <html:text  styleId="sslineNumber" property="sslineNumber" size="5" maxlength="5" styleClass="textbox"/>
                </td>
            </c:when>
            <c:otherwise>
                <td class="label">ECI Shpr/FF#</td>
                <td>
                    <html:text styleId="eciAccountNo" property="eciAccountNo" size="5" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                </td>
                <td class="label">ECI Consignee</td>
                <td>
                    <html:text styleId="eciAccountNo2" property="eciAccountNo2" size="5" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                </td>
                <td class="label">ECI Vendor</td>
                <td>
                    <html:text  styleId="eciAccountNo3" property="eciAccountNo3" size="5" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                </td>
                <td class="label">SSLine Number</td>
                <td>
                    <html:text  styleId="sslineNumber" property="sslineNumber" size="5" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                </td>
            </c:otherwise>
        </c:choose>
    </tr>
    <tr>
        <td class="label">Account Name</td>
        <td>
            <html:text property="accountName" styleId="accountName" styleClass="textbox" maxlength="50"/>
        </td>
        <td class="label">Forward Account</td>
        <td>
            <c:choose>
                <c:when test="${tradingPartnerForm.disabled eq 'Y' || not roleDuty.disableOrEnableTp}">
                    <html:text property="forwardAccount" styleId="forwardAccount" styleClass="textbox readonly" readonly="true"/>
                    <html:hidden property="forwardAccountName" styleId="forwardAccountName"/>
                </c:when>
                <c:otherwise>
                    <html:text property="forwardAccount" styleId="forwardAccount" styleClass="textbox" maxlength="50"/>
                    <html:hidden property="forwardAccountName" styleId="forwardAccountName"/>
                    <input type="hidden" id="forwardAccountCheck" value="${tradingPartnerForm.forwardAccount}"/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${roleDuty.disableOrEnableTp and tradingPartnerForm.disabled eq 'Y'}">
                    <img id="EnableImg" title="Enable" src="${path}/images/Unlock.png" onclick="enableTradingPartner('${accountNo}')"/>
                    <img id="DisableImg" title="Disable" src="${path}/images/Lock.png" onclick="disableTradingPartner('${accountNo}')" style="display: none;"/>
                </c:when>
                <c:when test="${roleDuty.disableOrEnableTp}">
                    <img id="EnableImg" title="Enable" src="${path}/images/Unlock.png" onclick="enableTradingPartner('${accountNo}')" style="display: none;"/>
                    <img id="DisableImg" title="Disable" src="${path}/images/Lock.png" onclick="disableTradingPartner('${accountNo}')"/>
                </c:when>
            </c:choose>
        </td>
    </tr>
    <tr>
        <td class="label">Additional Phone#</td>
        <td>
            <html:text property="phone1" styleId="phone1" styleClass="textbox" size="15" maxlength="13"/>
        </td>
        <td class="label">Additional Fax#</td>
        <td>
            <html:text property="fax1" styleId="fax1" styleClass="textbox" size="15" maxlength="13"/>
        </td>
        <td class="label">Merge Note info</td>
        <td>
            <html:textarea property="mergeNoteInfo" styleId="mergeNoteInfo" styleClass="textbox" cols="40" rows="3" onkeydown="TextAreaLimit(this,100)"/>
        </td>
    </tr>
    <tr>
        <td class="label">ECU Designation</td>
        <td>
            <html:select property="ecuDesignation" styleId="ecuDesignation" styleClass="dropdown" disabled="${not roleDuty.ecuDesignation}">
                <html:option value="LO">LOCAL (LO)</html:option>
                <html:option value="IC">INTERCOMPANY (IC)</html:option>
                <html:option value="AG">AGENTS (AG)</html:option>
                <html:option value="AA">AFFILIATED AGENTS (AA)</html:option>
            </html:select>
        </td>
        <td class="label">ECU Reporting Type</td>
        <td>
            <html:select property="ecuReportingType" styleId="ecuReportingType" styleClass="dropdown">
                <html:option value="CA">CORPORATE ACCOUNT (CA)</html:option>
                <html:option value="JA">JAPANESE ACCOUNT (JA)</html:option>
                <html:option value="PA">PRIORITY ACCOUNT (PA)</html:option>
                <html:option value="SA">STANDARD ACCOUNT (SA)</html:option>
            </html:select>
        </td>
    </tr>
    <c:if test="${master}">
        <tr>
            <th colspan="4" class="table-head-background">
                Subsidiary Accounts
            </th>
        </tr>
        <tr>
            <td colspan="4">
                <div class="result-container" id="subsidiaryAccountsDiv" style="height: 300px"></div>
            </td>
        </tr>
    </c:if>
</table>