<%@page import="com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<%@ include file="../includes/jspVariables.jsp"%>
<%@ include file="../Tradingpartnermaintainance/tradingPartnerSelectList.jsp"%>
<%@ include file="../includes/baseResources.jsp"%>
<%@ include file="../includes/resources.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<%@include file="../fragment/formSerialize.jspf" %>
<%    DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
    if (accountNo != null) {
        request.setAttribute("arScan", documentStoreLogDAO.getNoOfFilesScannedOrAttached(accountNo, "ARCONFIG", "", "Scan or Attach"));
    }
%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <title>AR Config</title>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/isValidEmail.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <script language="javascript">
            start = function() {
                serializeForm();
            }
            window.onload = start;
    </script>
        <style type="text/css">
            .popUpStyle {
                position: fixed;
                _position: absolute;
                z-index: 99;
                width:60%;
                height: 220px!important;
                overflow:auto; 
                border-style:solid solid solid solid;
                background-color: white;
            }
        </style>
    </head>
    <body class="whitebackgrnd">
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <html:form action="/tradingPartner" name="tradingPartnerForm"
                   type="com.gp.cong.logisoft.struts.form.TradingPartnerForm" styleId="accounting" scope="request">
            <div id="cover"></div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td colspan="3">Accounts Receivable</td>
                    <td align="right">
                        <input id="save" type="button" class="buttonStyleNew"
                               style="width: 60px" value='Save' onclick="saveARConfig()" />
                        <input id="arScan" type="button" class="${null!=arScan && arScan!='0' ? 'buttonColor' : 'buttonStyleNew'}"
                               value="Scan/Attach" onclick="arscanAttach('${accountNo}')"/>
                        <input type="button" class="buttonStyleNew" style="width:60px" value='Notes'
                               onclick="window.parent.GB_show('Notes', '${path}/notes.do?moduleId=${notesConstants.ARCONFIGURATION}&moduleRefId=${accountNo}', 300, 900);" />
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="2">
                            <tr class="textlabelsBold">
                                <td>Statements</td>
                                <td>Schedule For Statements</td>
                            </tr>
                            <tr>
                                <td>
                                    <html:select property="statements" styleClass="dropdown_accounting" styleId="statements"
                                                 value="${tradingPartnerForm.statements}" onchange="grayOutFields('statements')">
                                        <html:optionsCollection name="statements" />
                                    </html:select>
                                </td>
                                <td>
                                    <html:select property="schedulestmt" styleClass="dropdown_accounting"
                                                 value="${tradingPartnerForm.schedulestmt}">
                                        <html:optionsCollection name="Schedulelist" />
                                    </html:select>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Statement Type</td>
                                <td>Credit Status</td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>
                                    <html:select property="statementType" styleClass="dropdown_accounting" value="${tradingPartnerForm.statementType}">
                                        <html:option value="PDF">PDF</html:option>
                                        <html:option value="XLS">XLS</html:option>
                                    </html:select>
                                </td>
                                <td>
                                    <html:select property="creditStatus" styleClass="dropdown_accounting" styleId="creditStatus"
                                                 value="${tradingPartnerForm.creditStatus}" onchange="validateCredit('creditStatus')">
                                        <html:optionsCollection name="creditStatusList" />
                                    </html:select>
                                    Import Credit&nbsp;<html:checkbox property="importCredit" title="Import Credit" name="tradingPartnerForm"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold"> 
                                <td>Collector Code <br> 
                                    <html:text property="arCodeName" styleId="arCodeName" styleClass="textlabelsBoldforTextBox" value="${tradingPartnerForm.arCodeName}"/>
                                    <html:hidden property="arCode" styleId="arCode" value="${tradingPartnerForm.arCode}"/>
                                    <input type="hidden" name="arCodeNameValid" id="arCodeNameValid" value="${tradingPartnerForm.arCodeName}">
                                    <div id="arCodeNameDiv" class="autocomplete"></div>
                                </td>
                                <td>
                                    Exempt from Auto Hold&nbsp;<html:checkbox property="exemptCreditProcess" name="tradingPartnerForm" styleId="exemptCreditProcess"/>
                                    <br>HHG/PE/AUTOS CREDIT<html:checkbox property="hhgPeAutosCredit" name="tradingPartnerForm"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" style="padding-top:5px;padding-left: 10px;">
                                    <table border="0">
                                        <tr align="center">
                                            <td colspan="4" class="textlabelsBold">
                                                <font style="text-align: center" size="2">Send Statements With</font>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="textlabelsBold">Credit balance</td>
                                            <td>
                                                <html:checkbox property="creditbalance" name="tradingPartnerForm"/>
                                            </td>
                                            <td class="textlabelsBold">Credit Invoice</td>
                                            <td>
                                                <html:checkbox property="creditinvoice" name="tradingPartnerForm"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td></td>
                    <td valign="top">
                        <table border="0">
                            <tr class="textlabelsBold">
                                <td>Credit Terms</td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>
                                    <html:select property="creditRate" styleClass="dropdown_accounting" styleId="creditRate"
                                                 value="${tradingPartnerForm.creditRate}" onchange="validateCredit('creditTerms')">
                                        <html:optionsCollection name="creditRateList"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold">
                                    <bean:message key="form.customerForm.creditlimit" />
                                </td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold">
                                    <html:text property="creditLimit" maxlength="13" size="19" styleId="creditLimit"
                                               styleClass="textlabelsBoldForTextBox"
                                               onchange="validateCredit('creditLimit')" name="tradingPartnerForm" />
                                </td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold" colspan="2">
                                    Over Limit
                                    <html:checkbox property="overLimitCheck" value="on"
                                                   name="tradingPartnerForm" disabled="true"/>
                                    Past due
                                    <html:checkbox property="pastDueCheck" value="on"
                                                   name="tradingPartnerForm" disabled="true"/>
                                    <br>
                                </td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold" colspan="2">
                                    FCL Apply Late Fee
                                    <html:checkbox property="fclApplyLateFee" value="on" name="tradingPartnerForm"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold" colspan="2">
                                    LCL Apply Late Fee
                                    <html:checkbox property="lclApplyLateFee" value="on" name="tradingPartnerForm"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold">
                                    Past Due Buffer
                                    <html:text property="pastDueBuffer" styleId="pastDueBuffer" styleClass="textlabelsBoldforTextBox" 
                                               value="${tradingPartnerForm.pastDueBuffer}" maxlength="6" size="6" onblur="validatePastDueBuffer()"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td valign="top" style="padding-right: 6px;padding-bottom: 4px;">
                        <table  cellspacing="0" border="0" cellpadding="0">
                            <tr>
                                <td valign="top">
                                    <span class="textlabelsBold">Comment Line</span>
                                </td>

                            </tr>
                            <tr>
                                <td class="textlabelsBold" valign="top">
                                    <html:textarea property="acctReceive" styleId="acctReceive" onkeyup="toUppercase(this)"
                                                   styleClass="textlabelsBoldForTextBox" cols="70" rows="4"
                                                   value="${tradingPartnerForm.acctReceive}" />
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">
                                    <span class="textlabelsBold">Credit Status Comments</span>
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">
                                    <html:textarea property="holdComment" styleId="holdComment" onkeyup="toUppercase(this)"
                                                   styleClass="textlabelsBoldForTextBox" cols="70" rows="4"
                                                   value="${tradingPartnerForm.holdComment}" />
                                </td>
                            </tr>
                            <tr>
                                <td class="textlabelsBold" valign="top">
                                    Send Debit/Credit Notes
                                    <html:checkbox property="sendDebitCreditNotes" name="tradingPartnerForm"/> 
                                </td>
                            </tr>   
                        </table>
                    </td>
                </tr>
            </table>
            <br/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr><td>
                        <table border="0" width="100%" cellpadding="2" cellspacing="2" class="tableBorderNew">
                            <tr class="tableHeadingNew"><td colspan="3">Contact Details</td></tr>
                            <tr class="textlabelsBold"><td>Contact Name</td>
                                <td><input type="text" readonly="readonly" size="29" onkeyup="toUppercase(this)"  class="textlabelsBoldForTextBoxDisabledLook"
                                           value="${tradingPartnerForm.contact}">
                                </td>
                                <td align="justify">
                                    <span class="hotspot" onmouseover="tooltip.show('<strong>Select</strong>', null, event);"
                                          onmouseout="tooltip.hide();">
                                        <img src="${path}/img/icons/display.gif" id="contactDetailsToggle" onclick="loadContactDetails('loadContactDetails')" />
                                    </span>
                                </td>

                            </tr>
                            <tr class="textlabelsBold">
                                <td>Phone</td>
                                <td><input type="text" readonly="readonly" size="29" onkeyup="toUppercase(this)"  class="textlabelsBoldForTextBoxDisabledLook"
                                           value="${tradingPartnerForm.arPhone}">
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Fax</td>
                                <td><input type="text" readonly="readonly" size="29" onkeyup="toUppercase(this)" class="textlabelsBoldForTextBoxDisabledLook"
                                           value="${tradingPartnerForm.arFax}">
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Email</td>
                                <td><input type="text" readonly="readonly" size="29"  onkeyup="toUppercase(this)" class="textlabelsBoldForTextBoxDisabledLook"
                                           value="${tradingPartnerForm.acctRecEmail}" maxlength="50">
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td>
                        <table border="0" width="100%" cellpadding="2" cellspacing="2" class="tableBorderNew" style="border-left-width: 0px;">
                            <tr class="tableHeadingNew"><td colspan="3">Customer Invoice Address</td></tr>
                            <tr class="textlabelsBold">
                                <td>C/O Name</td>
                                <td><input type="text" size="29" id="coName" onkeyup="toUppercase(this)"  readonly="readonly" class="textlabelsBoldForTextBoxDisabledLook"
                                           value="${tradingPartnerForm.coName}"/>
                                </td>
                                <td align="justify">
                                    <span class="hotspot" onmouseover="tooltip.show('<strong>Select</strong>', null, event);"
                                          onmouseout="tooltip.hide();">
                                        <img src="${path}/img/icons/display.gif" id="contactDetailsToggle" onclick="loadContactDetails('loadInvoiceAddressDetails')" />
                                    </span>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Address</td>
                                <td><input type="text" readonly="readonly" size="29" id="address1" onkeyup="toUppercase(this)"  class="textlabelsBoldForTextBoxDisabledLook"
                                           value="${tradingPartnerForm.address1}"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>City</td>
                                <td><input type="text" size="29" id="city" readonly="readonly" onkeyup="toUppercase(this)" class="textlabelsBoldForTextBoxDisabledLook"
                                           value="${tradingPartnerForm.city1}"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>State</td>
                                <td colspan="2"><input type="text" size="4" onkeyup="toUppercase(this)"  class="textlabelsBoldForTextBoxDisabledLook"
                                                       id="state" value="${tradingPartnerForm.state}"/>
                                    Zip
                                    <input type="text" size="16" readonly="readonly" onkeyup="toUppercase(this)" class="textlabelsBoldForTextBoxDisabledLook"
                                           id="zip" value="${tradingPartnerForm.zip}"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td>
                        <table border="0" width="100%" cellpadding="2" cellspacing="2" class="tableBorderNew" style="border-left-width: 0px;">
                            <tr class="tableHeadingNew"><td colspan="3">F.F Payment Address</td></tr>
                            <tr class="textlabelsBold">
                                <td>C/O Name</td>
                                <td><input type="text" size="29" onkeyup="toUppercase(this)"  readonly="readonly" class="textlabelsBoldForTextBoxDisabledLook"
                                           value="${tradingPartnerForm.payCompany}">
                                </td>
                                <td align="justify"><%int stringIndex = accountNo.lastIndexOf("0");
                                    if (stringIndex < 9) {
                                    %>

                                    <span class="hotspot" onmouseover="tooltip.show('<strong>Use From Master</strong>', '100', event);"
                                          onmouseout="tooltip.hide();">
                                        <img src="${path}/img/icons/display.gif" id="contactDetailsToggle"
                                             onclick="loadContactDetails('loadMasterAddress')" />
                                    </span>

                                    <%
                                        }
                                    %>
                                    <span class="hotspot" onmouseover="tooltip.show('<strong>Select</strong>', '100', event);"
                                          onmouseout="tooltip.hide();">
                                        <img src="${path}/img/icons/display.gif" id="contactDetailsToggle"
                                             onclick="loadContactDetails('loadPaymentAddressDetails')" />
                                    </span>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Address</td>
                                <td><input type="text" readonly="readonly" size="29" onkeyup="toUppercase(this)"  class="textlabelsBoldForTextBoxDisabledLook"
                                           value="${tradingPartnerForm.payAddress1}">
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>City</td>
                                <td><input type="text" size="29" onkeyup="toUppercase(this)"  class="textlabelsBoldForTextBoxDisabledLook"
                                           value="${tradingPartnerForm.paycity2}">
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>State</td>
                                <td colspan="2"><input type="text" readonly="readonly" onkeyup="toUppercase(this)"  size="4" class="textlabelsBoldForTextBoxDisabledLook"
                                                       value="${tradingPartnerForm.payState}"/>
                                    Zip <input type="text" readonly="readonly" size="16" onkeyup="toUppercase(this)" class="textlabelsBoldForTextBoxDisabledLook"
                                               value="${tradingPartnerForm.payZip}"/>
                                </td>
                            </tr>

                        </table>
                    </td>
                </tr>
            </table>
            <%@include file="AccountingPopupDivs.jspf"%>
            <html:hidden property="buttonValue" />
            <html:hidden property="contactId" />
            <html:hidden property="arConfigId" value="${tradingPartnerForm.arConfigId}" />
            <html:hidden property="contact" value="${tradingPartnerForm.contact}" />
            <html:hidden property="arPhone" value="${tradingPartnerForm.arPhone}" />
            <html:hidden property="arFax" value="${tradingPartnerForm.arFax}" />
            <html:hidden property="acctRecEmail" value="${tradingPartnerForm.acctRecEmail}" />
            <html:hidden property="coName" value="${tradingPartnerForm.coName}" />
            <html:hidden property="address1" value="${tradingPartnerForm.address1}" />
            <html:hidden property="city" value="${tradingPartnerForm.city}" />
            <html:hidden property="state" value="${tradingPartnerForm.state}" />
            <html:hidden property="zip" value="${tradingPartnerForm.zip}" />
            <html:hidden property="payCompany" value="${tradingPartnerForm.payCompany}" />
            <html:hidden property="payAddress1" value="${tradingPartnerForm.payAddress1}" />
            <html:hidden property="paycity2" value="${tradingPartnerForm.paycity2}" />
            <html:hidden property="payState" value="${tradingPartnerForm.payState}" />
            <html:hidden property="payZip" value="${tradingPartnerForm.payZip}" />
            <html:hidden property="city1" value="${tradingPartnerForm.city1}" />
            <html:hidden property="tradingPartnerId" value="<%=accountNo%>" />
            <html:hidden property="custAddressId" value="${tradingPartnerForm.custAddressId}"/>
        </html:form>
    </body>
    <script type="text/javascript" src="${path}/js/TradingPartner/accounting.js"></script>
    <c:if test="${loadContactDetails}">
        <script type="text/javascript">
                                        document.getElementById('cover').style.display = 'block';
                                        setPopupAttributes('contactDetailsSlide');
        </script>
    </c:if>
    <c:if test="${loadInvoiceAddressDetails}">
        <script type="text/javascript">
            document.getElementById('cover').style.display = 'block';
            setPopupAttributes('invoiceAddressDetailsSlide');
        </script>
    </c:if>
    <c:if test="${loadMasterAddress}">
        <script type="text/javascript">
            document.getElementById('cover').style.display = 'block';
            setPopupAttributes('paymentAddressDetailsSlide');
        </script>
    </c:if>
    <c:if test="${loadPaymentAddressDetails}">
        <script type="text/javascript">
            document.getElementById('cover').style.display = 'block';
            setPopupAttributes('paymentAddressDetailsSlide1');
        </script>
    </c:if>
    <!-- THIS CONDITION IS TO DISABLE THE PAGE BASED ON LOGIN USER ROLE -->
    <c:if test="${view == '3' || not empty disableTabBasedOnRole}">
        <script type="text/javascript">
            disablePage(document.getElementById("accounting"));
        </script>
    </c:if>
    <!-- THIS CONDITION IS TO DISABLE THE PAGE BASED ON LOGIN USER ROLE DUTY -->
    <c:if test="${roleDuty.arConfigTabReadOnly eq true}">
        <script type="text/javascript">
            disablePage(document.getElementById("accounting"));
        </script>
    </c:if>
</html>
