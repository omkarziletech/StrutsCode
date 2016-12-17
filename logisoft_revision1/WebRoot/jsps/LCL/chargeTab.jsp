<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<cong:javascript src="${path}/jsps/LCL/js/charge.js"></cong:javascript>
    <body>
    <cong:div style="width:100%; float:left;">
        <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
            Charges for File No:<span class="fileNo">${fileNumber}</span>
        </cong:div><br><br>
        <cong:form name="lclCostAndChargeForm" id="lclCostAndChargeForm" action="/lclCostAndCharge.do">
            <c:if test="${lclCostAndChargeForm.moduleName eq 'Imports'}">
                <jsp:useBean id="billToParty" scope="request" class="com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO"/>
                <c:set var="billToPartyList" value="${billToParty.allBillToPartyImp}"/>
            </c:if> 
            <cong:hidden name="manualEntry" id="manualEntry" value="${lclCostAndChargeForm.manualEntry}"/>
            <cong:hidden name="id" id="lclBookingAcId" value="${lclCostAndChargeForm.id}"/>
            <cong:hidden name="fileNumber" value="${fileNumber}"/>
            <cong:hidden name="fileNumberStatus" value="${lclCostAndChargeForm.fileNumberStatus}"/>
            <cong:hidden name="agentNo" id="agentNo" value="${lclCostAndChargeForm.agentNo}"/>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${fileNumberId}"/>
            <cong:hidden name="moduleName"  id="moduleName" value='${lclCostAndChargeForm.moduleName}' />
            <cong:hidden name="costStatus"  id="costStatus" value='${lclCostAndChargeForm.costStatus}' />
            <cong:hidden name="chargeInvoiceNumber"  id="chargeInvoiceNumber" value='${lclCostAndChargeForm.chargeInvoiceNumber}' />
            <cong:hidden name="billToPartyOldValue"  id="billToPartyOldValue" value="${lclBookingAc.arBillToParty}"/>
            <input type="hidden" id="disableCost" name="disableCost" value="${disableCost}"/>
            <input type="hidden" id="buttonval" value="${buttonValue}"/>
            <input type="hidden" id="cfAcctNo" value="${cfAcctNo}"/>
            <input type="hidden" id="postAr" value="${lclBookingAc.postAr}"/>
            <c:set var="shipmentType" value="${lclCostAndChargeForm.moduleName eq 'Imports' ? 'LCLI' :'LCLE'}"/>
            <c:set var="chargeQuery" value="${buttonValue ne 'addCost' ? 'CHARGE_CODE':'COST_CODE_UNIT'}"/>
            <table width="100%" border="0">
                <cong:tr>
                    <td class="textlabelsBoldforlcl">Code</td>
                    <td id="chg">
                        <cong:autocompletor name="hiddenChargeCode" id="hiddenChargeCode" template="two" query="${chargeQuery}" fields="null,chargesCodeId"
                                            shouldMatch="true" scrollHeight="150" params="${shipmentType}" 
                                            container="NULL" styleClass="mandatory textlabelsBoldForTextBoxWidth" width="400" callback="checkChargeCode('${shipmentType}');"/>
                        <input type="hidden" id="chargesCode" name="chargesCode" value="${lclCostAndChargeForm.chargesCode}"/>
                        <input type="hidden" id="existChargeCode" name="existChargeCode" value="${lclCostAndChargeForm.chargesCode}"/>
                        <input type="hidden" id="existChargeCodeId" name="existChargeCodeId" value="${lclCostAndChargeForm.chargesCodeId}"/>
                        <cong:hidden name="chargesCodeId" id="chargesCodeId"/>
                    </td>
                    <td class="textlabelsBoldforlcl">UOM</td>
                    <cong:td>
                        <cong:radio name="uom" value="M" id="metric" onclick="setUomFeilds();" container="NULL"/>M
                        <cong:radio name="uom" value="I" id="imperial" onclick="setUomFeilds();" container="NULL"/>I
                    </cong:td>
                    <c:if test="${shipmentType eq 'LCLE' && buttonValue ne 'addCost'}">     
                        <cong:td styleClass="bold" colspan="3">
                            <input type="radio" name="rateOption" value="FLAT" id="FL"  onclick="setSelect();"/>Flat Rate Only
                            <input type="radio" name="rateOption" value="WMRATE" id="WM" onclick="setSelect();"/>W/M Rate Only
                            <input type="radio" name="rateOption" value="BOTH" id="BO" checked onclick="setSelect();"/>Both
                        </cong:td>
                    </c:if>
                </cong:tr>
                <cong:tr><cong:td><br/></cong:td></cong:tr>
                <c:if test="${buttonValue ne 'addCost'}">
                    <cong:tr>
                        <c:choose>
                            <c:when test="${lclCostAndChargeForm.manualEntry}">
                                <td class="textlabelsBoldforlcl">Charge Amount(Sell)</td>
                                <td>
                                    <cong:text styleClass="textlabelsBoldForTextBox twoDigitDecFormat chargeAmt flatCharge" style="width:76px" name="flatRateAmount" 
                                               id="flatRateAmount" onkeyup="checkForNumberAndDecimal(this);"/>
                                    <span class="textlabelsBoldforlcl">(flat rate)</span>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td class="textlabelsBoldforlcl">Charge Amount</td>
                                <td>
                                    <cong:text styleClass="textlabelsBoldForTextBox twoDigitDecFormat chargeAmt flatCharge" style="width:76px" name="arAmount" id="arAmount"
                                               onkeyup="checkForNumberAndDecimal(this);rateChargeReadonly()"/>
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <td class="textlabelsBoldforlcl rate_or">${shipmentType eq 'LCLI' ? '(OR)' : ''}</td>
                        <td class="textlabelsBoldforlcl">W/M Rate</td>
                        <cong:td>
                            <cong:text name="measure" styleClass="textlabelsBoldForTextBox twoDigitDecFormat chargeAmt wmrateCharge"  style="width:76px" id="measure"
                                       onkeyup="checkForNumberAndDecimal(this);"/>
                            <cong:label id="msr" text=" /CBM" styleClass="textlabelsBoldforlcl"></cong:label>
                            <cong:text name="weight" styleClass="textlabelsBoldForTextBox twoDigitDecFormat chargeAmt wmrateCharge" style="width:76px" id="weight"
                                       onkeyup="checkForNumberAndDecimal(this);"/>
                            <cong:label id="wei" text="/1000 KGS" styleClass="textlabelsBoldforlcl"></cong:label>
                        </cong:td>
                        <td class="textlabelsBoldforlcl">Minimum</td>
                        <cong:td>
                            <cong:text name="minimum" styleClass="textlabelsBoldForTextBox twoDigitDecFormat chargeAmt wmrateCharge" style="width:76px" id="minimum"
                                       onkeyup="checkForNumberAndDecimal(this);" />
                        </cong:td>
                    </cong:tr>
                    <cong:tr><cong:td><br/></cong:td></cong:tr>
                    <cong:tr>
                        <td class="textlabelsBoldforlcl">Bill To Party</td>
                        <cong:td>
                            <html:select property="hiddenBillToParty" styleId="hiddenBillToParty" style="width:134px" styleClass="smallDropDown mandatory textlabelsBoldforlcl" 
                                         onchange="if(${shipmentType eq 'LCLI'}){changeBillToParty();}else{validateBillToParty();}">
                                <html:optionsCollection name="billToPartyList"/>
                            </html:select>
                            <input type="hidden" name="billToParty" value="${lclCostAndChargeForm.billToParty}" id="billToParty"/>
                            <input type="hidden" name="existBillToParty" value="${lclCostAndChargeForm.billToParty}" id="existBillToParty"/>
                        </cong:td>
                        <c:if test="${shipmentType ne 'LCLI'}">
                            <cong:td colspan="0" styleClass="td" valign="middle">Bill this Charge On</cong:td>
                            <cong:td>
                                <input type="radio" name="billCharge" id="billChargeInvoice" value="IV" container="NULL" 
                                       checked="${lclQuoteAc.billCharge=='IV'?'yes':''}"/>Invoice
                                <input type="radio" name="billCharge" id="billChargeBL" value="BL" container="NULL" 
                                       checked="${lclQuoteAc.billCharge=='IV'?'':'yes'}"/>B/L
                            </cong:td>
                        </c:if>
                    </cong:tr>
                    <cong:tr styleClass="tableHeadingNew">
                        <cong:td styleClass="setTopBorderForTable" colspan="8"></cong:td>
                    </cong:tr>
                </c:if>
                <cong:tr><cong:td><br/></cong:td></cong:tr>
                <cong:tr>
                    <td class="textlabelsBoldforlcl">Cost Amount</td>
                    <cong:td>
                        <cong:text styleClass="textlabelsBoldForTextBox twoDigitDecFormat costAmt flatCost" style="width:76px" name="costAmount" id="costAmount"
                                   onkeyup="allowNegativeNumbers(this);" onchange="costAmountValidate(this,'${buttonValue}');"/>
                        <span class="textlabelsBoldforlcl">(flat rate)</span>
                    </cong:td>
                    <td class="textlabelsBoldforlcl rate_or">${shipmentType eq 'LCLI' ? '(OR)' : ''}</td>
                    <td class="textlabelsBoldforlcl">W/M Cost</td>
                    <cong:td>
                        <cong:text name="measureForCost" styleClass="textlabelsBoldForTextBox twoDigitDecFormat costAmt wmrateCost" style="width:76px" id="measureForCost"
                                   onkeyup="checkForNumberAndDecimal(this);" onchange="costAmountValidate(this,'${buttonValue}');"/>
                        <cong:label id="msr1" text=" /CBM" styleClass="textlabelsBoldforlcl"></cong:label>
                        <cong:text name="weightForCost" styleClass="textlabelsBoldForTextBox twoDigitDecFormat costAmt wmrateCost" style="width:76px" id="weightForCost"
                                   onkeyup="checkForNumberAndDecimal(this);"/>
                        <cong:label id="wei1" text="/1000 KGS" styleClass="textlabelsBoldforlcl"></cong:label>
                    </cong:td>
                    <td class="textlabelsBoldforlcl">Minimum</td>
                    <cong:td>
                        <cong:text name="minimumForCost" styleClass="textlabelsBoldForTextBox twoDigitDecFormat costAmt wmrateCost" style="width:76px" id="minimumForCost"
                                   onkeyup="checkForNumberAndDecimal(this);" />
                    </cong:td>
                </cong:tr>
                <cong:tr><cong:td><br/></cong:td></cong:tr>
                <cong:tr>
                    <td class="textlabelsBoldforlcl">Vendor Name</td>
                    <cong:autocompletor  name="thirdPartyname"  id="thirdPartyname" fields="thirdpartyaccountNo,NULL,NULL,NULL,NULL,NULL,NULL,thirdpartyDisabled,NULL,NULL,NULL,NULL,NULL,thirdparyDisableAcct" styleClass="textlabelsBoldForTextBox"
                                         shouldMatch="true" callback="vendorTypeCheck();" width="600" query="VENDOR" template="tradingPartner" scrollHeight="150"
                                         paramFields="vendorSearchState,vendorSearchZip,vendorSearchSalesCode,vendorCountryUnLocCode">
                        <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" style="vertical-align: middle;"
                             title="Click here to edit House Consignee Search options" onclick="showClientSearchOption('${path}', 'Vendor');"/>
                        <c:if test="${shipmentType eq 'LCLI'}">
                            <input type="checkbox" name="selectAgentAccount" id="selectAgentAccount" style="vertical-align: middle;" onclick="selectAgentAccountNo();"/>
                            <span class="textlabelsBoldforlcl" style="vertical-align: middle;" id="agent">Agent</span>
                            <input type="checkbox" name="selectWareHouseAccount" id="selectWareHouseAccount" style="vertical-align: middle;"
                                   onclick="selectWareHouseAccountNo();"/>
                            <span class="textlabelsBoldforlcl" style="vertical-align: middle;" id="CFSDev">CFS Dev</span>
                        </c:if>
                    </cong:autocompletor>
                    <input type="hidden" name="thirdpartyDisabled" id="thirdpartyDisabled"/>
                    <input type="hidden" name="thirdparyDisableAcct" id="thirdparyDisableAcct"/>
                    <input type="hidden" name="vendorSearchState" id="vendorSearchState"/>
                    <input type="hidden" name="vendorSearchZip" id="vendorSearchZip"/>
                    <input type="hidden" name="vendorSearchSalesCode" id="vendorSearchSalesCode"/>
                    <input type="hidden" name="vendorSearchCountry" id="vendorSearchCountry"/>
                    <input type="hidden" name="vendorCountryUnLocCode" id="vendorCountryUnLocCode"/>
                    <cong:td></cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Vendor#</cong:td>
                        <td>
                        <cong:text  name="thirdpartyaccountNo" id="thirdpartyaccountNo" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"/>
                    </td>
                    <td class="textlabelsBoldforlcl">Invoice Number</td>
                    <td>
                        <cong:text styleClass="textlabelsBoldForTextBox"  maxlength="25" name="invoiceNumber" id="invoiceNumber"/>
                        <input type="checkbox" name="lastInvoice" id="lastInvoice" title="Repeat Last Invoice#" onclick="lastInvoiceNo();" style="vertical-align: middle;"/>
                        <input type="hidden" id="lastInvoiceNumber" value=""/>
                        <input type="hidden" id="lastVendorName" value=""/>
                        <input type="hidden" id="lastVendorNumber" value=""/>
                    </td>
                </cong:tr>
            </table><br>
            <table>
                <tr>
                    <td width="10%">
                        <c:choose>
                            <c:when test="${not empty lclCostAndChargeForm.id || not empty lclBookingAcId}">
                                <input type="button" class="button-style1" value="Save and Exit" onclick="saveCharge('SE', '${buttonValue}');" id="saveCode"
                                       onkeydown="tabFocusRestrictor(event);" />
                            </c:when>
                            <c:otherwise>
                                <input type="button" class="button-style1" value="Save and Exit" onclick="saveCharge('SE', '${buttonValue}');" id="saveCode" />
                                <input type="button" class="button-style1" style="position: relative;" value="Save and More" id="saveMore"
                                       onclick="saveCharge('SM', '${buttonValue}');" onkeydown="tabFocusRestrictor(event);" />
                                <c:if test="${lclCostAndChargeForm.moduleName eq 'Imports' && lclCostAndChargeForm.fileNumberStatus eq 'M'}">
                                    <input type="button" class="button-style1"  id="autoIpi" value="Auto IPI" onclick="getAutoIPICost();"/>  
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="destination" id="destination"/>
            <input type="hidden" name="buttonValue" id="buttonValue" value="${buttonValue}"/>
            <input type="hidden" name="bookingAcId" id="bookingAcId" value="${bookingAcId}"/>
            <input type="hidden" name="engmet" id="engmet" value="${engmet}"/>
        </cong:form>
    </cong:div>
</body>
