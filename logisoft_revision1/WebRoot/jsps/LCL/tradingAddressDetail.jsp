<%@include file="init.jsp" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/baseResources.jsp" %>
<body style="background:#ffffff">
    <cong:form id="tradingContactForm" name="tradingContactForm" action="tradingContact.do">
        <cong:hidden name="fileId" id="fileId" value="${tradingContactForm.fileId}"/>
        <cong:hidden name="fileType" id="fileType" value="${tradingContactForm.fileType}"/>
        <cong:hidden name="vendorType" id="vendorType" value="${tradingContactForm.vendorType}"/>
        <cong:hidden name="checkValue" id="newCheckValue" value="${tradingContactForm.checkValue}"/>
        <cong:hidden name="methodName" id="methodName"/>
        <cong:div style="width:100%;">
            <cong:table style="width:100%">
                <cong:tr styleClass="tableHeadingNew" >
                    <cong:td width="90%">Address Details</cong:td>
                    <cong:td>
                        <c:if test="${not empty tradingContactForm.fileId}">
                            <cong:div styleClass="button-style1" style="float:left"
                                      onclick="submitForm('save')">Save</cong:div></c:if> </cong:td>
                </cong:tr></cong:table>
            <cong:table width="100%">
                <cong:tr><cong:td></cong:td></cong:tr>
                <cong:tr>
                    <cong:td width="33%">
                        <cong:table >
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Address</cong:td>
                                <cong:td>
                                    <cong:textarea cols="30" rows="6" name="address" id="address" styleClass="textlabelsBoldForTextBox textup" value="${tradingContactForm.address}"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td>&nbsp&nbsp;</cong:td>
                                <cong:td></cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td>&nbsp&nbsp;</cong:td>
                                <cong:td></cong:td>
                            </cong:tr>
                            <c:if test="${tradingContactForm.checkValue && tradingContactForm.vendorType ne 'Shipper'}">
                                <cong:tr>
                                    <cong:td></cong:td>
                                    <cong:td styleClass="textlabelsBoldforlcl">Imports Rating Coload/Retail
                                        <input type="radio" name="importQuoteColoadRetail" ${tradingContactForm.importQuoteColoadRetail eq 'R' ? "checked":""} id="Retail"  value="R"/>R
                                        <input type="radio" name="importQuoteColoadRetail" ${tradingContactForm.importQuoteColoadRetail eq 'C' ? "checked":""} id="Coload"  value="C"/>C
                                    </cong:td>
                                </cong:tr>
                            </c:if>
                        </cong:table>
                    </cong:td>
                    <cong:td width="33%" valign="top">
                        <cong:table>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">City</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${tradingContactForm.checkValue}">
                                            <cong:autocompletor id="city" name="city" template="one" fields="city,country,unlocCode,state" query="PORT_CITY_STATE" styleClass="textlabelsBoldForTextBox"
                                                                width="300" container="NULL" value="${tradingContactForm.city}" shouldMatch="true" scrollHeight="200px"/>
                                        </c:when>
                                        <c:otherwise>
                                            <cong:text styleClass="textlabelsBoldForTextBox textCap" name="city" id="city" value="${tradingContactForm.city}" maxlength="50"/>
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                                <cong:hidden id="unlocCode" name="unlocCode"/>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">State</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${tradingContactForm.checkValue}">
                                            <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook textCap" name="state" readOnly="true" id="state" value="${tradingContactForm.state}" maxlength="50"/>
                                        </c:when>
                                        <c:otherwise>
                                            <cong:text styleClass="textlabelsBoldForTextBox textCap" name="state" id="state" value="${tradingContactForm.state}" maxlength="50"/>
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                            </cong:tr> <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Zip</cong:td>
                                <cong:td>
                                    <cong:text styleClass="textlabelsBoldForTextBox" name="zip" id="zip" value="${tradingContactForm.zip}" maxlength="10"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Country</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${tradingContactForm.checkValue}">
                                            <cong:text styleClass="textlabelsBoldForTextBoxDisabledLook textCap" name="country" readOnly="true" id="country" value="${tradingContactForm.country}" maxlength="50"/>
                                        </c:when>
                                        <c:otherwise>
                                            <cong:text styleClass="textlabelsBoldForTextBox textCap" name="country" id="country" value="${tradingContactForm.country}" maxlength="50"/>
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                            </cong:tr> <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Phone</cong:td>
                                <cong:td>
                                    <cong:text styleClass="textlabelsBoldForTextBox" name="phone" id="phone" value="${tradingContactForm.phone}" maxlength="50"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Fax</cong:td>
                                <cong:td>
                                    <cong:text styleClass="textlabelsBoldForTextBox" name="fax" id="fax" value="${tradingContactForm.fax}" maxlength="50"/>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                    <cong:td width="25%" valign="top">
                        <cong:table>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Email</cong:td>
                                <cong:td>
                                    <cong:text styleClass="textlabelsBoldForTextBox" name="email" id="email" value="${tradingContactForm.email}" maxlength="50"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Sales Person Code</cong:td>
                                <cong:td>
                                    <cong:autocompletor id="salesPersonCode" name="salesPersonCode" template="two" query="SALES_PERSON_CODE" styleClass="textlabelsBoldForTextBox"
                                                        width="250" container="NULL" value="${tradingContactForm.salesPersonCode}" shouldMatch="true" scrollHeight="150px" maxlength="2" position="left"/>
                                </cong:td>
                            </cong:tr>
                            <c:if test="${tradingContactForm.checkValue && tradingContactForm.vendorType eq 'Consignee'}">
                                <cong:tr>
                                    <cong:td colspan="2" styleClass="textlabelsBoldforlcl">
                                        <html:checkbox property="sendNotification" styleId="sendNotification" tabindex="-1"/>Send Notifications by Email</cong:td>
                                </cong:tr>
                            </c:if>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">Client Ref</cong:td>
                                <cong:td>
                                    <cong:text styleClass="textlabelsBoldForTextBox textCap" name="clientReference" id="clientReference" value="${tradingContactForm.clientReference}" maxlength="50"/>
                                </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td  styleClass="td textlabelsBoldforlcl">POA</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${poa eq 'N'}">
                                            <span style="color:red;font: bold;font-size: 11px;font-family:Arial;font-weight: bold">
                                                <c:out value="NO"/>
                                            </span>
                                        </c:when>
                                        <c:when test="${poa eq 'Y'}">
                                            <span style="color:green;font: bold;font-size: 11px;font-family:Arial;font-weight: bold">
                                                <c:out value="YES"/>
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </cong:div>
    </cong:form>
    <script type="text/javascript">
        var vendorType = $('#vendorType').val();
        var newCheckValue = $('#newCheckValue').val();
        function submitForm() {
            var  coloadRetails = $('input:radio[name=importQuoteColoadRetail]:checked').val();
            if(vendorType !== 'Shipper' && newCheckValue==='true' &&(coloadRetails==null || coloadRetails=="")){
                $.prompt("Please select Imports Rating Coload/Retail");
                return false;
            }
            if (vendorType === 'Shipper') {
                setValues('shipperAddress', 'shipperCity', 'shipperState', 'shipperZip', 'shipperCountry', 'shipperPhone', 'shipperFax', 'shipperEmail', 'shipperSalesPersonCode', 'shipUnlocCode','');
            }
            else if (vendorType === 'Consignee') {
                setValues('consigneeAddress', 'consigneeCity', 'consigneeState', 'consigneeZip', 'consigneeCountry', 'consigneePhone', 'consigneeFax', 'consigneeEmail', 'consigneeSalesPersonCode', 'consigneeUnLocCode','consigneeColoadRetailRadio');
            }
            else if (vendorType === 'Notify') {
                setValues('notifyAddress', 'notifyCity', 'notifyState', 'notifyZip', 'notifyCountry', 'notifyPhone', 'notifyFax', 'notifyEmail', 'notifySalesPersonCode', 'notifyUnLocCode','notifyColoadRetailRadio');
            }
            else if (vendorType === 'Notify2') {
                setValues('notify2Address', 'notify2City', 'notify2State', 'notify2Zip', 'notify2Country', 'notify2Phone', 'notify2Fax', 'notify2Email', 'notify2SalesPersonCode', 'notify2UnLocCode','notify2ColoadRetailRadio');
            }
            if ($('#sendNotification').is(":checked") && ($("#email").val() === null || $("#email").val() === '')) {
                $.prompt("Email is Required");
                $("#email").css("border-color", "red");
                $("#warning").show();
            } else {
                $("#methodName").val('save');
                $("#tradingContactForm").submit();
                parent.$.colorbox.close();
            }
        }
        function setValues(address, city, state, zip, country, phone, fax, email, salesPersonCode, unlocCode,coloadRetail) {
            if(newCheckValue==='true' && coloadRetail!=''){
                var coloadRetailValue = $('input:radio[name=importQuoteColoadRetail]:checked').val();
                parent.$('#' +coloadRetail).val(coloadRetailValue);
            }
            parent.$('#' + address).val($('#address').val());
            parent.$('#' + city).val($('#city').val());
            parent.$('#' + state).val($('#state').val());
            parent.$('#' + zip).val($('#zip').val());
            parent.$('#' + country).val($('#country').val());
            parent.$('#' + phone).val($('#phone').val());
            parent.$('#' + fax).val($('#fax').val());
            parent.$('#' + email).val($('#email').val());
            parent.$('#' + salesPersonCode).val($('#salesPersonCode').val());
            parent.$('#' + unlocCode).val($('#unlocCode').val());
        }
    </script>
</body>