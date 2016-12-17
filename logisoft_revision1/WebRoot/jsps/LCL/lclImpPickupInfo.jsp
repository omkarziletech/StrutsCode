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
        <cong:hidden name="headerId" id="headerId" value="${lclPickupInfoForm.headerId}"/>
        <input type="hidden" name="unitId" id="unitId" value="${lclPickupInfoForm.unitId}"/>
        <input type="hidden" name="apTransType" id="apTransType" value="${transType}"/>
        <input type="hidden" name="invoiceStatus" id="invoiceStatus" value="${invoiceStatus}"/>
        <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclPickupInfoForm.fileNumberId}"/>
        <cong:hidden name="fileNumber" id="fileNumber" value="${lclPickupInfoForm.fileNumber}"/>
        <cong:hidden name="cityStateZip" id="cityStateZip" value="${lclPickupInfoForm.cityStateZip}"/>
        <input type="hidden" name="arGlMappingFlag" id="arGlMappingFlagId" value="${arGlMappingFlag}"/>
        <input type="hidden" name="apGlMappingFlag" id="apGlMappingFlagId" value="${apGlMappingFlag}"/>
        <cong:hidden name="lclBookingPadId" value="${lclBookingPad.id}"/>
        <cong:table style="width:100%" border="0">
            <cong:tr>
                <cong:td styleClass="tableHeadingNew" colspan="6">
                    <cong:div styleClass="floatLeft">Door Delivery Info for File No:
                        <span class="fileNo">${lclPickupInfoForm.fileNumber}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                        DoorOriginCity:<span class="fileNo">${lclPickupInfoForm.cityStateZip}</span>
                    </cong:div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="25%">
                    <cong:table border="0">
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Issued By:</cong:td>
                            <cong:td>
                                <cong:text name="issuingTerminal" id="issuingTerminal" styleClass="textlabelsBoldForTextBoxDisabledLook textLCLuppercase" readOnly="true" value="${lclPickupInfoForm.issuingTerminal}"/>
                                <cong:hidden name="trmnum" value="${lclPickupInfoForm.trmnum}"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Ship To</cong:td>
                            <cong:td>
                                <span id="dojoVendor">
                                    <cong:autocompletor name="companyName" template="tradingPartner" id="shipSupplier" fields="accountNo,shipperaccttype,NULL,NULL,NULL,NULL,NULL,shipToDissable,address,city,state,country,zipCode,contactName,phone1,fax1,email1,shipToUnlocationCode,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL"
                                                        styleClass="textlabelsBoldForTextBox textLCLuppercase"  query="SHIP_TO" width="500" scrollHeight="200" container="NULL" value="${lclPickupInfoForm.companyName}"/>
                                    <!--                                    <input type="hidden" name="city" id="city" value=""/>
                                                                        <input type="hidden" name="state" id="state" value=""/>
                                                                        <input type="hidden" name="zipCode" id="zipCode" value=""/>-->
                                </span>
                                <span id="manualVendor">
                                    <cong:text name="manualCompanyName" id="manualCompanyName" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase"  value="${lclPickupInfoForm.manualCompanyName}"/></span>
                                <input type="text" id="accountNo" name="accountNo" style="width:80px;" class="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclPickupInfoForm.accountNo}"/>
                                <cong:checkbox name="dupShipper" id="dupShipper"  title="New" onclick="newmanualVendor()" container="NULL"/>
                                <cong:img src="${path}/jsps/LCL/images/add2.gif"  width="16" height="16" alt="display" styleClass="trading" onclick="openTradingPartner('${path}','shipperName')"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Address</cong:td>
                            <cong:td>
                                <cong:textarea cols="41" rows="3" name="address" id="address" 
                                               styleClass="textlabelsBoldForTextBox textlabelsBoldForTextBoxDisabledLook "
                                               style="text-transform: uppercase" value="${lclPickupInfoForm.address}" container="NULL" readOnly="true"/>
                            </cong:td>
                        </cong:tr>
                    <tr>
                        <cong:td styleClass="textlabelsBoldforlcl">City</cong:td>
                            <td>
                            <cong:text name="shipperCity" id="city" styleClass="textlabelsBoldForTextBox textlabelsBoldForTextBoxDisabledLook" style="width:100px"
                                       value="${lclPickupInfoForm.shipperCity}" container="NULL"/>
                            <span class="textlabelsBoldforlcl">&nbsp;&nbsp;State</span>
                            <cong:text name="shipperState" id="state" styleClass="textlabelsBoldForTextBox textlabelsBoldForTextBoxDisabledLook" style="width:100px"
                                       value="${lclPickupInfoForm.shipperState}" container="NULL"/>
                        </td>
                    </tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Zip</cong:td>
                        <cong:td>
                            <cong:text  name="shipperZip" id="zipCode" styleClass="textlabelsBoldForTextBox textlabelsBoldForTextBoxDisabledLook"
                                        value="${lclPickupInfoForm.shipperZip}" container="NULL"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Ready Date</cong:td>
                        <cong:td>
                            <cong:calendarNew name="deliveryReadyDate" id="pickupReadyDate" value="${lclPickupInfoForm.deliveryReadyDate}"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td width="8%" styleClass="textlabelsBoldforlcl">Last Free Day</cong:td>
                        <cong:td>
                            <cong:calendarNew name="lastFreeDate" id="lastFreeDate" value="${lclPickupInfoForm.lastFreeDate}"/>
                        </cong:td>
                    </cong:tr>
                        <c:if test="${lclPickupInfoForm.moduleName eq 'Imports'}">
                            <cong:tr>
                                <cong:td width="8%" styleClass="textlabelsBoldforlcl">Estimated Pickup Date</cong:td>
                                <cong:td>
                                    <cong:calendarNew name="estPickupDate" id="estPickupDate" value="${lclPickupInfoForm.estPickupDate}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td width="8%" styleClass="textlabelsBoldforlcl">Estimated Delivery Date</cong:td>
                                <cong:td>
                                    <cong:calendarNew name="estimatedDeliveryDate" id="estimatedDeliveryDate" value="${lclPickupInfoForm.estimatedDeliveryDate}"/>
                                </cong:td>
                            </cong:tr>    
                        </c:if>    
                </cong:table>
            </cong:td>
            <cong:td width="25%" align="left">
                <cong:table>
                    <cong:tr>
                        <cong:td width="40%" id="pickupCharge" colspan="2">
                            <c:import url="/jsps/LCL/ajaxload/pickupcharge.jsp"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
                <cong:table>
                     <c:if test="${lclPickupInfoForm.moduleName eq 'Imports'}">
                        <cong:tr>
                            <cong:td width="33%" styleClass="textlabelsBoldforlcl">Actual Pickup Date</cong:td>
                            <cong:td>
                                <cong:calendarNew name="pickedupDatetime" id="pickedupDatetime" showTime="true" value="${lclPickupInfoForm.pickedupDatetime}"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td width="33%" styleClass="textlabelsBoldforlcl">Actual Delivery Date</cong:td>
                            <cong:td>
                                <cong:calendarNew name="deliveredDatetime" id="deliveredDatetime" showTime="true" value="${lclPickupInfoForm.deliveredDatetime}"/>
                            </cong:td>
                        </cong:tr>
                    </c:if>
                </cong:table>
            </cong:td>
            <cong:td width="30%" align="left">
                <cong:table align="left">
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Door Delivery Status</cong:td>
                        <cong:td>
                            <html:select property="doorDeliveryStatus" styleClass="smallDropDown textlabelsBoldforlcl" styleId="doorDeliveryStatus" style="width:150px" onchange="enableDoorDeliveryETA();">
                                <html:option value="P">Pending(Cargo at CFS )</html:option>
                                <html:option value="O">Out For Delivery</html:option>
                                <html:option value="D">Delivered</html:option>
                                <html:option value="F" >Final/Closed</html:option>
                                <html:option value="PC">Pending contact</html:option>
                                <html:option value="DR">Docs requested</html:option>
                                <html:option value="PD">Pending docs</html:option>
                                <html:option value="PA">Pending Appt</html:option>
                                <html:option value="PB">Pending balance</html:option>
                                <html:option value="OH">Cargo on HOLD</html:option>
                                <html:option value="DP">Dispatched</html:option>
                            </html:select>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">POD Signed</cong:td>
                        <cong:td>
                            <cong:text name="podSigned" styleClass="textlabelsBoldForTextBox" id="podSigned" style="width: 143px;text-transform:uppercase;" maxlength="50"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">POD Date</cong:td>
                        <cong:td>
                            <cong:calendarNew name="podDate" id="podDate" showTime="true" value="${lclPickupInfoForm.pickupCutoffDate}"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Pickup#</cong:td>
                        <cong:td>
                            <cong:text name="deliveryReferenceNo" styleClass="textlabelsBoldForTextBox" id="deliveryReferenceNo" style="width: 143px;text-transform:uppercase;" maxlength="20"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Vendor</cong:td>
                        <cong:td>
                            <cong:autocompletor name="costVendorAcct" template="tradingPartner" id="costVendorAcct" styleClass="textlabelsBoldForTextBox textLCLuppercase"  query="TRADING_PARTNER_IMPORTS"
                                                fields="costVendorNo,acctType,NULL,NULL,NULL,NULL,NULL,costDisabled,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,forwarderAcct"
                                                width="500" scrollHeight="200" container="NULL" position="left" callback="acctTypeCheck()"/>
                            <input type="text" name="costVendorNo"  id="costVendorNo" style="width:80px;" value="${lclPickupInfoForm.costVendorNo}"class="text-readonly textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true"/>
                            <input type="hidden" name="acctType" id="acctType"/>
                            <input type="hidden" name="costDisabled" id="costDisabled"/>
                            <input type="hidden" name="forwarderAcct" id="forwarderAcct"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">PRO#</cong:td>
                        <cong:td>
                            <cong:text name="pickupReferenceNo" styleClass="textlabelsBoldForTextBox" id="pickupReferenceNo" style="width: 180px;text-transform:uppercase;" maxlength="20"/>
                        </cong:td>
                    </cong:tr>
                    <c:if test="${lclPickupInfoForm.moduleName eq 'Imports'}">
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Commercial Delivery</cong:td>
                            <cong:td>
                                <html:select property="deliveryCommercial" styleId="deliveryCommercial" value="${lclPickupInfoForm.deliveryCommercial}" styleClass="textlabelsLclBoldForMainScreenTextBox">
                                    <html:option value="">Select</html:option>
                                    <html:option value="Y">Yes</html:option>
                                    <html:option value="N">No</html:option>
                                </html:select>
                            </cong:td>

                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Lift Gate</cong:td>
                            <cong:td>
                                <html:select property="liftGate" styleId="liftGate" value="${lclPickupInfoForm.liftGate}" styleClass="textlabelsLclBoldForMainScreenTextBox">
                                    <html:option value="">Select</html:option>
                                    <html:option value="Y">Yes</html:option>
                                    <html:option value="N">No</html:option>
                                </html:select>
                            </cong:td>
                        </cong:tr>        
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl">Need POD</cong:td>
                            <cong:td>
                                <html:select property="needPOD" styleId="needPOD" value="${lclPickupInfoForm.needPOD}" styleClass="textlabelsLclBoldForMainScreenTextBox">
                                    <html:option value="">Select</html:option>
                                    <html:option value="Y">Yes</html:option>
                                    <html:option value="N">No</html:option>
                                </html:select>
                            </cong:td>
                        </cong:tr>
                                    <cong:tr>
                                        <cong:td width="33%" styleClass="textlabelsBoldforlcl">Door Delivery Notes</cong:td>
                                        <cong:td> <cong:textarea  name="deliveryNotes" id="deliveryNotes" styleClass="textlabelsBoldForTextBox" style="width:84%;text-transform: uppercase" rows="2" cols="10" value="${lclPickupInfoForm.deliveryNotes}" onchange="limitText(this, 200)" onkeypress="limitText(this, 200)" container="NULL"></cong:textarea></cong:td>
                                    </cong:tr>           
                    </c:if>    
                    <c:choose>
                        <c:when test="${lclPickupInfoForm.moduleName ne 'Exports'}">
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl" id="doorDelivery">Door Delivery ETA</cong:td>
                                <cong:td>
                                    <cong:calendarNew name="doorDeliveryEta" id="doorDeliveryEta" styleClass="mandatory" value="${lclPickupInfoForm.doorDeliveryEta}"/>
                                </cong:td>
                            </cong:tr>
                        </c:when>
                    </c:choose>
                </cong:table>
            </cong:td>
            <cong:td width="10%">
            </cong:td>
        </cong:tr>
    </cong:table>
    <cong:table width="99%" border="0">
        <cong:tr>
            <cong:td width="8%" styleClass="textlabelsBoldforlcl">Special Instructions</cong:td>
            <cong:td width="91%"><cong:textarea rows="2" name="deliveryInstructions" id="deliveryInstructions" styleClass="textlabelsBoldForTextBox" style="width:85%;text-transform: uppercase" value="${lclPickupInfoForm.deliveryInstructions}"/></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table>
        <cong:tr>
            <cong:td width="8%" styleClass="textlabelsBoldforlcl"></cong:td>
            <cong:td styleClass="tableHeadingNew">
                <cong:div styleClass="textlabelsBoldforlcl floatLeft">DELIVER FROM WHSE</cong:div>
            </cong:td>
            <cong:td width="15%" styleClass="td"></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table border="0" width="85%">
        <cong:tr>
            <cong:td width="10%">    </cong:td>
            <cong:td styleClass="textlabelsBoldforlcl" width="4%">WhseName</cong:td>
            <cong:td>
                <cong:autocompletor name="whsecompanyName" id="whsecompanyName" width="550" styleClass="textLclWidth textuppercaseLetter" query="IMPORTWAREHOUSE" fields="NULL,NULL,whseAddress,whsecity,whseState,whseZip,whsePhone,whseFax,whseId" template="delwhse"
                                    value="${lclPickupInfoForm.whsecompanyName}" shouldMatch="true" scrollHeight="200"/>
                <cong:hidden name="whseId" id="whseId"/><cong:hidden name="whseNo" id="whseNo"/></cong:td>
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
            <cong:td align="left"><cong:hidden name="whseFax" id="whseFax" value="${lclPickupInfoForm.whseFax}"/></cong:td>
        </cong:tr>
    </cong:table>
    <cong:table border="0" width="100%">
        <cong:tr>
            <cong:td width="8%" styleClass="textlabelsBoldforlcl">Bill To</cong:td>
            <cong:td  width="92%"> <cong:textarea  name="pickupInstructions" id="pickupInstructions" styleClass="textlabelsBoldForTextBox" style="width:84%;text-transform: uppercase" rows="3" value="${lclPickupInfoForm.pickupInstructions}" container="NULL"></cong:textarea></cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">Commodity
            </cong:td>
            <cong:td><cong:text  name="commodityDesc"  id="commodityDesc" styleClass="textlabelsBoldForTextBox textuppercaseLetter" style="width:84%;text-transform: uppercase" value="${lclPickupInfoForm.commodityDesc}" container="NULL"/></cong:td>
        </cong:tr>
        <cong:tr>
            <cong:td styleClass="textlabelsBoldforlcl">TOS</cong:td>
            <cong:td><cong:textarea  name="termsOfService" id="termsOfService" styleClass="textlabelsBoldForTextBox textuppercaseLetter" style="width:84%;text-transform: uppercase" value="${lclPickupInfoForm.termsOfService}" container="NULL"/></cong:td>
        </cong:tr>
    </cong:table>
    <br/>
    <cong:tr>
        <cong:td>
            <input type="button" value="Save" class="button-style1 disabledButton" id="savePickup" onclick="submitForm('save', 'N');"/>
            <input type="hidden" name="unitId" id="unitId" value="${lclPickupInfoForm.unitId}"/>
            <c:choose>
                <c:when test="${closedBy eq ''}">
                    <input type="button" value="Carrier Rates"
                           class="button-style1 carrier disabledButton"
                           onclick="callCarrierRates($('#whseZip').val(), $('#pickupReadyDate').val(), $('#fileNumberId').val(), '${lclPickupInfoForm.moduleName}', '${fileNumberStatus}', '${invoiceStatus}', '${transType}', '${billToParty}');"/>
                </c:when>
                <c:otherwise>
                    <input type="button" value="Carrier Rates" class="gray-background carrier disabledButton"/>
                </c:otherwise>
            </c:choose>
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
            <input type="button" value="Send EDI to CTS" class="${ediStatusStyle} disabledButton" onclick="conformboxEdi('${lclPickupInfoForm.moduleName}');" id="SendEDI"/>
            <input type="button" value="Email Me" class="button-style1 disabledButton"  onclick="submitForm('save', 'Y');" id="SendEDI"/>
            <img src="${path}/img/icons/e_contents_view.gif" width="18" height="18" id="histEdi" onclick="displayEdiHistory(id, $('#fileNumberId').val(), '${path}');" title="Click here to see CTS EDI History"/>
        </cong:td>
    </cong:tr>
    <cong:hidden name="methodName" id="methodName"/>
    <cong:hidden name="sendEdiToCtsFlag" id="sendEdiToCtsFlag"/>
</cong:form>
</body>
