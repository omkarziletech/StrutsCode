<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<cong:javascript  src="${path}/jsps/LCL/js/lclPickUp.js"/>
<body style="background:#ffffff">
    <cong:form name="lclPickupInfoForm" id="lclPickupInfoForm" action="/lclPickupInfo.do">
        <cong:hidden name="moduleName" id="moduleName" value="${lclPickupInfoForm.moduleName}"/>
        <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclPickupInfoForm.fileNumberId}"/>
        <cong:hidden name="cityStateZip" id="cityStateZip" value="${lclPickupInfoForm.cityStateZip}"/>
        <cong:hidden name="fileNumber" id="fileNumber" value="${lclPickupInfoForm.fileNumber}"/>
        <cong:hidden name="lclBookingPadId" value="${lclBookingPad.id}"/>
        <cong:hidden id="pickUpInfo" name="pickUpInfo" value="false"/>
        <input type="hidden" name="arGlMappingFlag" id="arGlMappingFlagId" value="${arGlMappingFlag}"/>
        <input type="hidden" name="apGlMappingFlag" id="apGlMappingFlagId" value="${apGlMappingFlag}"/>
        <cong:table style="width:100%" border="0">
            <cong:tr>
                <cong:td styleClass="tableHeadingNew" colspan="3">
                    <cong:div styleClass="floatLeft">Pickup Info for File No:
                        <span class="fileNo">${lclPickupInfoForm.fileNumber}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                        DoorOriginCity:<span class="fileNo originZip"> ${lclPickupInfoForm.cityStateZip}</span>
                    </cong:div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="25%">
                    <cong:table>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Issued By:</cong:td>
                            <cong:td>
                                <%--<cong:autocompletor name="issuingTerminal"  id="issuingTerminal" template="commTempFormat" styleClass="text mandatory textlabelsBoldForTextBox textuppercaseLetter" query="ISSUING_TERMINAL" fields="NULL,trmnum" width="250" value="${lclPickupInfoForm.issuingTerminal}" container="NULL" shouldMatch="true"/>--%>
                                <cong:text name="issuingTerminal" id="issuingTerminal" styleClass="textlabelsBoldForTextBoxDisabledLook textLCLuppercase" readOnly="true" value="${lclPickupInfoForm.issuingTerminal}"/>
                                <cong:hidden name="trmnum" value="${lclPickupInfoForm.trmnum}"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">To Trucker</cong:td>
                            <cong:td>
                                <span id="dojoVendor">
                                    <cong:autocompletor name="toAccountName" template="tradingPartner" id="toDojo" fields="toAccountNo" scrollHeight="300"
                                                        styleClass="textlabelsBoldForTextBox textLCLuppercase"  query="VENDOR" width="600" container="NULL"
                                                        shouldMatch="true" value="${lclPickupInfoForm.toAccountName}"/>
                                </span>
                                <span id="manualVendor">
                                    <cong:text name="manualCompanyName" id="manualCompanyName"  styleClass="text textLCLuppercase" container="NULL" value="${lclPickupInfoForm.manualCompanyName}"/>
                                </span>
                                <cong:checkbox name="manualShipper" id="manualShipper" onclick="newmanualVendor();" container="NULL"/>New
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Ship/Supplier</cong:td>
                            <cong:td>
                                <span id="dojoPickUpShipper">
                                    <cong:autocompletor name="companyName" template="tradingPartner" id="shipSupplier" fields="shipperAccountNo,shipper_acct_type,NULL,NULL,NULL,NULL,NULL,shipSupDisabled,address,city,state,NULL,zip,NULL,phone1,fax1,NULL,NULL,NULL,NULL,NULL,NULL,shipSupDisableAcct"
                                                        styleClass="textlabelsBoldForTextBox textLCLuppercase"  query="SHIPPER" paramFields="shipSupSearchState,shipSupSearchZip,shipSupSearchSalesCode,shipCountryUnLocCode" width="500"   scrollHeight="300px" container="NULL" shouldMatch="true"
                                                        value="${lclPickupInfoForm.companyName}" callback="shipSupplierAcctType();"/>
                                </span>
                                <input type="hidden" name="shipSupSearchState" id="shipSupSearchState" value=""/>
                                <input type="hidden" name="shipSupSearchZip" id="shipSupSearchZip" value=""/>
                                <input type="hidden" name="shipSupSearchSalesCode" id="shipSupSearchSalesCode"/>
                                <input type="hidden" name="shipSupSearchCountry" id="shipSupSearchCountry"/>
                                <input type="hidden" name="shipCountryUnLocCode" id="shipCountryUnLocCode"/>
                                <input type="hidden" name="shipper_acct_type" id="shipper_acct_type"/>
                                <input type="hidden" name="shipSupDisabled" id="shipSupDisabled"/>
                                <input type="hidden" name="shipSupDisableAcct" id="shipSupDisableAcct"/>
                                <span id="manualShipp">
                                    <cong:text name="companyNameDup" id="dupShipName" styleClass="textlabelsBoldForTextBox"
                                               style="text-transform: uppercase" value="${lclPickupInfoForm.companyNameDup}"/>
                                </span>
                                <cong:text name="shipperAccountNo" id="shipperAccountNo"  style="width:70px" readOnly="true"
                                           styleClass="textlabelsBoldForTextBoxDisabledLook"/>
                                <cong:checkbox id="newCompanyName" name="newCompanyName" title="New" onclick="newcompanyName();" container="NULL"/>
                                <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
                                     title="Click here to edit Ship/Supplier Search options" onclick="showShipSupplierSearchOption('${path}', 'Ship/Supplier')"/>
                                <cong:img src="${path}/jsps/LCL/images/add2.gif" id="greenPlusIcon" width="16" height="16" alt="display" styleClass="trading" onclick="openWarehouse()"/>
                                <span title="Checked=Copy from Shipper">
                                    <cong:checkbox name="dupShipper" id="dupShipper" onclick="copyShipper();" container="NULL"/>Copy
                                </span>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Address</cong:td>
                            <cong:td>
                                <cong:textarea cols="40" rows="3" name="address" id="address" styleClass="textlabelsBoldForTextBox"
                                               style="text-transform: uppercase" value="${lclPickupInfoForm.address}" container="NULL"/>
                            </cong:td>
                        </cong:tr>
                    <tr>
                        <cong:td styleClass="textlabelsBoldforlcl">City</cong:td>
                            <td>
                            <cong:text name="shipperCity" id="city" styleClass="textlabelsBoldForTextBox" style="width:100px"
                                       value="${lclPickupInfoForm.shipperCity}" container="NULL"/>
                            <span class="textlabelsBoldforlcl">&nbsp;&nbsp;State</span>
                            <cong:text name="shipperState" id="state" styleClass="textlabelsBoldForTextBox" style="width:100px"
                                       value="${lclPickupInfoForm.shipperState}" container="NULL"/>
                        </td>
                    </tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Zip</cong:td>
                        <cong:td>
                            <cong:text  name="shipperZip" id="zip" styleClass="textlabelsBoldForTextBox"
                                        value="${lclPickupInfoForm.shipperZip}" container="NULL"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Ready Date</cong:td>
                        <cong:td>
                            <cong:calendarNew name="pickupReadyDate" id="pickupReadyDate" value="${lclPickupInfoForm.pickupReadyDate}" styleClass="textlabelsBoldForTextBox"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">CutOff Date</cong:td>
                        <cong:td>
                            <cong:calendarNew name="pickupCutoffDate" id="pickupCutoffDate" value="${lclPickupInfoForm.pickupCutoffDate}"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td>
            <cong:td width="40%" align="left">
                <cong:table>
                    <cong:tr>
                        <cong:td width="40%" id="pickupCharge" colspan="2">
                            <c:import url="/jsps/LCL/ajaxload/pickupcharge.jsp"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td>
            <cong:td width="30%" align="left"></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table width="99%" border="0">
        <cong:tr>
            <cong:td width="8%" styleClass="textlabelsBoldforlcl">Ref#/PRO#</cong:td>
            <cong:td width="92%"><cong:textarea name="pickupReferenceNo" id="pickupReferenceNo" styleClass="textlabelsBoldForTextBox" cols="50" rows="1" style="width:41%;text-transform: uppercase;"  value="${lclPickupInfoForm.pickupReferenceNo}" onkeyup="limitText(this,50)"/></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table>
        <cong:tr>
            <cong:td width="8%" styleClass="textlabelsBoldforlcl"></cong:td>
            <cong:td styleClass="tableHeadingNew" colspan="3">
                <cong:div styleClass="floatLeft">Deliver To Whse</cong:div>
            </cong:td>
            <cong:td width="15%" styleClass="td"></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table border="0" width="85%">
        <cong:tr>
            <cong:td width="10%">    </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl" width="4%">WhseName</cong:td>
            <cong:td><cong:autocompletor name="whsecompanyName" id="whsecompanyName" width="500" styleClass="textLclWidth textuppercaseLetter" query="DELWHSE" fields="NULL,NULL,whseState,whseZip,whseAddress,whsePhone,whsecity," template="delwhse" value="${lclPickupInfoForm.whsecompanyName}" shouldMatch="true" scrollHeight="200px"/></cong:td>
            <cong:td styleClass="textlabelsBoldforlcl" style="align:left">Address</cong:td>
            <cong:td width="3%"><cong:text name="whseAddress" id="whseAddress" styleClass="textlabelsBoldForTextBox textuppercaseLetter" style="width:200px" value="${lclPickupInfoForm.whseAddress}"/></cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">City</cong:td>
            <cong:td width="3%">
                <cong:text name="whseCity" id="whsecity" styleClass="textlabelsBoldForTextBox" value="${lclPickupInfoForm.whseCity}" container="NULL" style="width:66px;text-transform: uppercase" maxlength="50"/>
                <cong:text name="whseState" id="whseState" styleClass="textlabelsBoldForTextBox" value="${lclPickupInfoForm.whseState}" container="NULL" style="width:19px;text-transform: uppercase" maxlength="50"/>
                <cong:text name="whseZip" id="whseZip" styleClass="textlabelsBoldForTextBox" value="${lclPickupInfoForm.whseZip}" container="NULL" style="width:40px;text-transform: uppercase" maxlength="50"/>
            </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl">phone</cong:td>
            <cong:td align="left"><cong:text  name="whsePhone" id="whsePhone" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase" value="${lclPickupInfoForm.whsePhone}" maxlength="50"/></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table border="0" width="100%">
        <cong:tr>
            <cong:td  width="8%" styleClass="textlabelsBoldforlcl">Instructions
            </cong:td>
            <cong:td  width="92%"> <cong:textarea  name="pickupInstructions" id="pickupInstructions" styleClass="textlabelsBoldForTextBox" style="width:84%;text-transform: uppercase" rows="3" value="${lclPickupInfoForm.pickupInstructions}" container="NULL"></cong:textarea></cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Commodity
            </cong:td>
            <cong:td><cong:text  name="commodityDesc"  id="commodityDesc" styleClass="textlabelsBoldForTextBox textuppercaseLetter" style="width:84%;text-transform: uppercase" value="${lclPickupInfoForm.commodityDesc}" onkeyup="limitText(this,255)" container="NULL"/></cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">TOS</cong:td>
            <cong:td><cong:textarea  name="termsOfService" id="termsOfService" styleClass="textlabelsBoldForTextBox textuppercaseLetter" style="width:84%;text-transform: uppercase" value="${lclPickupInfoForm.termsOfService}" container="NULL"/></cong:td>
        </cong:tr>
    </cong:table>
    <cong:tr>
        <cong:td>
            <input type="button" value="Save" class="button-style1"  id="savePickup" onclick="submitPickUpinfo('save', 'N', '${lclPickupInfoForm.moduleName}');"/>
            <input type="button" value="Carrier Rates" class="button-style1 carrier" id="carrierRates" onclick="submitPickUpinfo('callCts', '', '${lclPickupInfoForm.moduleName}');"/>
            <c:choose>
                <c:when test="${not empty ediStatus}">
                    <c:if test="${ediStatus eq 'green'}"><c:set var="ediStatusStyle" value="green-background"/></c:if>
                    <c:if test="${ediStatus eq 'yellow'}"><c:set var="ediStatusStyle" value="button-style1yellow"/></c:if>
                    <c:if test="${ediStatus eq 'red'}"><c:set var="ediStatusStyle" value="red-background"/></c:if>
                </c:when>
                <c:otherwise>
                    <c:set var="ediStatusStyle" value="button-style1"/>
                </c:otherwise>
            </c:choose>
            <input type="button" value="Send EDI to CTS" class="${ediStatusStyle}"  onclick="submitPickUpinfo('sendEdi', '', '${lclPickupInfoForm.moduleName}');" id="SendEDI"/>
            <span title="Click here to see CTS EDI History">
                <img src="${path}/img/icons/e_contents_view.gif" width="18" height="18" id="histEdi" onclick="displayEdiHistory(id, $('#fileNumberId').val(), '${path}');" />
            </span>
        </cong:td>
    </cong:tr>
    <cong:hidden name="methodName" id="methodName"/>
    <cong:hidden name="sendEdiToCtsFlag" id="sendEdiToCtsFlag"/>
</cong:form>
</body>
