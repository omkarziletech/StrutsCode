<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="/taglib.jsp" %>
<body style="background:#ffffff">
    <cong:div style="width:100%; float:left;">
        <cong:table width="100%"  border="1" style="border:1px solid #c4c5c4">
            <cong:tr>
                <cong:td  styleClass="tableHeadingNew">SUPPLIER</cong:td>
                <cong:td  styleClass="tableHeadingNew">GENERAL</cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td width="50%">
                    <cong:table>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl" width="10%" valign="middle">Supplier Name</cong:td>
                            <cong:td styleClass="textlabelsBoldleftforlcl">
                                <cong:autocompletor name="supContact.companyName" template="tradingPartner" id="supplierName" fields="supplierCode,NULL,NULL,NULL,NULL,NULL,NULL,NULL,supplierAddress,supplierCity,supplierState,supplierCountry,supplierZip,NULL,supplierPhone,supplierFax,supplierEmail,NULL,NULL,NULL,NULL"
                                                    query="CLIENT_WITH_CONSIGNEE"  width="600" container="NULL" value="${lclBl.supContact.companyName}" styleClass="mandatory textlabelsBoldForTextBox textCapitalLetter"
                                                    shouldMatch="true" scrollHeight="300px"/>
                                <cong:hidden id="supplierCity" name="supContact.city" value="${lclBl.supContact.city}"/>
                                <cong:hidden id="supplierState" name="supContact.state" value="${lclBl.supContact.state}"/>
                                <cong:hidden id="supplierZip" name="supContact.zip" value="${lclBl.supContact.zip}"/>
                                <cong:hidden id="supplierCountry" name="supContact.country" value="${lclBl.supContact.country}"/>
                                <cong:hidden id="supplierPhone" name="supContact.phone1" value="${lclBl.supContact.phone1}"/>
                                <cong:hidden id="supplierFax" name="supContact.fax1" value="${lclBl.supContact.fax1}"/>
                                <cong:hidden  id="supplierEmail" name="supContact.email1" value="${lclBl.supContact.email1}"/>
                            </cong:td>

                            <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Supplier Number</cong:td>
                            <cong:td styleClass="textlabelsBoldleftforlcl">
                                <cong:text id="supplierCode" name="supAcct" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" value="${lclBl.supAcct.accountno}"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldleftforlcl" valign="middle">Supplier Address</cong:td>
                            <cong:td styleClass="textlabelsBoldleftforlcl" colspan="3">
                                <cong:textarea cols="40" rows="6" id="supplierAddress" name="supContact.address" styleClass="textlabelsBoldForTextBox textCapitalLetter" value="${lclBl.supContact.address}"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr styleClass="tableHeadingNew">
                            <cong:td styleClass="setTopBorderForTable" colspan="8"></cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="tableHeadingNew"  colspan="4">TO PICK UP FREIGHT, PLEASE CONTACT:</cong:td>
                        </cong:tr>

                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl" valign="middle" >Agent Name</cong:td>
                            <cong:td >
                                <cong:text id="agentName" name="agentName" styleClass="textlabelsBoldForTextBoxDisabledLook textCapitalLetter"  readOnly="true"
                                           container="NULL" value="${lclBl.agentAcct.accountName}" />

                            </cong:td>

                            <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Agent Number</cong:td>
                            <cong:td styleClass="textlabelsBoldleftforlcl">
                                <cong:text name="agentAccount" id="agentacctno" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true" container="NULL" value="${lclBl.agentAcct.accountno}"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr><cong:td>&nbsp;</cong:td></cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldleftforlcl" valign="left">Agent Address</cong:td>
                            <cong:td styleClass="textlabelsBoldleftforlcl">
                                <cong:textarea cols="40" rows="6" id="agentAddress" readOnly="true" style="height:auto;" name="AgentAddressConcat" styleClass="text-readonlytext8 textlabelsBoldForTextBoxDisabledLook textCapitalLetter" value="${lclBlForm.agentAddressConcat}"/>
                            </cong:td>
                            <cong:td styleClass="textlabelsBoldforlcl" >Phone</cong:td>
                            <cong:td styleClass="textlabelsBoldleftforlcl">
                                <cong:text name="agentContact.phone1" id="agentphone" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook " container="NULL" value="${lclBl.agentContact.phone1}"/>
                            </cong:td></cong:tr><cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Email</cong:td>
                            <cong:td styleClass="textlabelsBoldleftforlcl">
                                <cong:text name="agentContact.email1" id="agentemail" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook  textCapitalLetter" container="NULL" value="${lclBl.agentContact.email1}"/>
                            </cong:td>
                            <cong:td styleClass="textlabelsBoldforlcl" valign="middle" >Fax</cong:td>
                            <cong:td styleClass="textlabelsBoldleftforlcl">
                                <cong:text name="agentContact.fax1" id="agentfax" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook " container="NULL" value="${lclBl.agentContact.fax1}"/>
                            </cong:td>
                        </cong:tr>

                    </cong:table>
                </cong:td>
                <cong:td valign="top">
                    <cong:table>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Docs</cong:td>
                            <cong:td  styleClass="textBoldforlcl">
                                <cong:checkbox name="docsBl" id="docsBl" container="NULL" value="${lclBl.docsBl}"/>BL
                                <cong:checkbox name="docsCaricom" id="docsCaricom" container="NULL" value="${lclBl.docsCaricom}"/>Caricom
                                <cong:checkbox name="docsAes" id="docsAes" container="NULL" value="${lclBl.docsCaricom}"/>AES
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td valign="middle" styleClass="textlabelsBoldforlcl">Spot Rate</cong:td>
                            <cong:td>
                                <cong:table border="0" width="10%">
                                    <cong:tr>
                                        <cong:td styleClass="textBoldforlcl">
                                            <cong:radio value="Y" name="spotRate" container="NULL" onclick="spotRate_yes(this)"/> Yes
                                            <cong:radio value="N" name="spotRate" container="NULL" onclick="spotRateByNo(this)"/>No
                                            <input type="hidden" name="existSpotRate" id="existSpotRate" value="${lclBl.spotRate}"/>
                                        </cong:td>
                                        <cong:td>
                                            <input type='button' ${lclBl.spotRate ? "":"style='display:none;'"} id="lclSpotRate" value="Spot Rate"
                                                   class="${lclBl.spotWmRate!=null && lclBl.spotWmRate!='' ? 'green-background' : 'button-style1'} lclSpotRate" 
                                                   onClick ="showSpotRateInfo('${path}', '${lclBl.lclFileNumber.id}', '${lclBl.lclFileNumber.fileNumber}');"/>
                                        </cong:td>
                                    </cong:tr>
                                    <cong:tr>
                                        <cong:td colspan="2">
                                            <label id="spotratelabel">${lclBl.spotRate ? spotratelabel:''}</label>
                                            <input type="hidden" name="spotLabel" id="spotLabel" value="${spotratelabel}"/>
                                            <input type="hidden" name="weight" id="weight" />
                                            <input type="hidden" name="measure" id="measure" />
                                        </cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr>

                        <cong:tr>
                            <cong:td styleClass="textlabelsBoldforlcl" valign="middle">High Volume Discount</cong:td>
                            <c:choose>
                                <c:when test="${highVolumeDis eq 'true'}">
                                <cong:td styleClass="textlabelsBoldleftforlcl" valign="middle" style="color:green">
                                    &nbsp;&nbsp;Yes
                                </cong:td>
                            </c:when><c:otherwise>
                                <cong:td styleClass="textlabelsBoldleftforlcl" valign="middle" style="color:green">
                                    &nbsp;&nbsp;No
                                </cong:td>
                            </c:otherwise>
                        </c:choose>


                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle">CTC/Retail/FTF</cong:td>
                        <cong:td styleClass="textlabelsBoldleftforlcl">
                            <cong:radio value="C" name="rateType" id="ctc" onclick="ratesTypeBasedToRecalculateAutoRates('C')"  container="NULL"/> C
                            <cong:radio value="R" name="rateType" id="rateR" onclick="ratesTypeBasedToRecalculateAutoRates('R')" container="NULL"/> R
                            <cong:radio value="F" name="rateType" id="rateF" onclick="ratesTypeBasedToRecalculateAutoRates('F')" container="NULL"/>FTF
                            <cong:hidden name="editRateType" id="editRateType"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Free B/L</cong:td>
                        <cong:td styleClass="textlabelsBoldleftforlcl" valign="middle">
                            <cong:radio value="Y" name="freeBl"  container="NULL" onclick="deleteChargesByFreeBlYes()"/> Yes
                            <cong:radio value="N" name="freeBl" container="NULL" onclick="calculateChargesByFreeBlNo()"/> No</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Incentive</cong:td>
                        <cong:td styleClass="textlabelsBoldleftforlcl" valign="middle">
                            <cong:radio value="Y" name="clientPwkRecvd"  container="NULL" disabled="true"/> Yes
                            <cong:radio value="N" name="clientPwkRecvd"  container="NULL" disabled="true"/> No
                        </cong:td>
                    </cong:tr>
                    <cong:tr styleClass="tableHeadingNew">
                        <cong:td styleClass="setTopBorderForTable" colspan="4"></cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Insurance</cong:td>
                        <cong:td  styleClass="textBoldforlcl">
                            <c:set var="insuranceFlag" value="${lclBl.insurance}"/>
                            <input type="radio" name="insurance" id="insuranceY" value="Y" ${insuranceFlag ? 'checked':''} onclick="addInsurance();"/> Yes
                            <input type="radio" name="insurance" id="insuranceN" value="N" ${!insuranceFlag ? 'checked':''} onclick="removeInsuranceCharge();"/> No
                        </cong:td>
                        <cong:td></cong:td>
                        <cong:td></cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Value of Goods</cong:td>
                        <cong:td>
                            <input type="text" title="" class="text textlabelsBoldForTextBox" id="valueOfGoods"
                                   name="valueOfGoods" value="${lclBl.valueOfGoods}" 
                                   onchange="calculateInsuranceCharge();" onkeyup="checkForNumberAndDecimal(this);"/>
                       CIF
                        <input type="text"  class="textlabelsBoldForTextBoxDisabledLook" id="cifValue"
                               name="cifValue" style="width:50px;" maxlength="7" readonly="true" value="${lclBl.cifValue}"/>
                      </cong:td>
                      </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Documentation Yes/No</cong:td>
                        <cong:td  styleClass="textBoldforlcl">
                            <c:choose>
                                <c:when test="${lclBl.documentation==true}">
                                    <cong:radio name="documentation" id="docYes" value="Y" checked="yes" container="NULL" onclick="addDocumentCharge('${path}');"/>Yes
                                    <cong:radio name="documentation" id="docNo" value="N" container="NULL" onclick="removeDocumCharge();"/> No
                                </c:when>
                                <c:otherwise>
                                    <cong:radio name="documentation" id="docYes" value="Y" container="NULL" onclick="addDocumentCharge('${path}');"/>Yes
                                    <cong:radio name="documentation" id="docNo" value="N" checked="yes" container="NULL" onclick="removeDocumCharge();"/> No
                                </c:otherwise>
                            </c:choose>
                            <input type="hidden" name="documSave" id="documSave"/>
                        </cong:td>
                    </cong:tr>
                            <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Delivery Metro PRSJU</cong:td>
                    <cong:td  styleClass="textBoldforlcl">
                        <cong:radio value="I" name="deliveryMetro" id="deliveryMetroI" container="NULL" onclick="calculateDeliveryMetroCharge()"/>I
                        <cong:radio value="O" name="deliveryMetro" id="deliveryMetroO" container="NULL" onclick="calculateDeliveryMetroCharge()"/>O
                        <cong:radio value="N" name="deliveryMetro" id="deliveryMetroN" container="NULL" onclick="calculateDeliveryMetroCharge()"/>N</cong:td>
                    <cong:hidden name="deliveryMetroField" value="${lclBl.deliveryMetro}" id="deliveryMetroField"/>
                </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl" valign="middle">Invoice Value</cong:td>
                        <cong:td  styleClass="textBoldforlcl">
                            <cong:text styleClass="textlabelsBoldForTextBox" id="invoiceValue"
                                   name="invoiceValue" value="${lclBl.invoiceValue}" onkeyup="checkForNumberAndDecimal(this);"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </cong:td>

        </cong:tr>
    </cong:table>
</cong:div>
</body>
