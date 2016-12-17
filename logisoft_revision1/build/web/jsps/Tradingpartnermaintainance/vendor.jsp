<%@include file="../includes/jspVariables.jsp"%>
<%@include file="../includes/resources.jsp"%>
<%@include file="../includes/baseResources.jsp"%>
<%@include file="../includes/baseResourcesForJS.jsp"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<%@include file="../Tradingpartnermaintainance/tradingPartnerSelectList.jsp"%>
<%@include file="../fragment/formSerialize.jspf" %>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <title>AP Config</title>
        <script type="text/javascript" src="${path}/js/isValidEmail.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.fileupload.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
        <style type="text/css">
            .popUpStyle {
                position: fixed;
                _position: absolute;
                z-index: 99;
                left:30% !important;
                width:40%;
                top:10px;
                height: 200px!important;
                overflow-x:auto; 
                border-style:solid solid solid solid;
                background-color: white;
            }
        </style>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <script type="text/javascript">
            start = function () {
                serializeForm();
            }
            window.onload = start;
        </script>
    </head>
    <body class="whitebackgrnd">
        <div id="cover"></div>
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <html:form action="/tradingPartner" name="tradingPartnerForm"
                   styleId="vendor" type="com.gp.cong.logisoft.struts.form.TradingPartnerForm" enctype="multipart/form-data" scope="request" method="post">
            <input type="hidden" name="action" id="action"/>
            <input type="hidden" name="className" id="className"/>
            <input type="hidden" name="methodName" id="methodName"/>
            <input type="hidden" name="customerNumber" id="customerNumber"/>
            <table width="100%" border="0" class="tableBorderNew" cellpadding="0"
                   cellspacing="0">
                <tr class="tableHeadingNew" >
                    <td>Vendor Information</td>
                    <td align="right" colspan="5">
                        <input type="button" style="width: 60px" class="buttonStyleNew" value="Save" onclick="saveAPConfig()"/>
                        <input type="button" class="buttonStyleNew" style="width: 60px" value='Notes'
                               onclick="window.parent.GB_show('Notes', '${path}/notes.do?moduleId=${notesConstants.APCONFIGURATION}&moduleRefId=${accountNo}', 300, 900);"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>TIN</td>
                    <td>Legal Name</td>
                    <td>AP Specialist</td>
                    <td>DBA</td>
                    <td>W-9 Field</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>
                        <html:text styleClass="textlabelsBoldForTextBox" onkeyup="toUppercase(this)" property="tin" value="${tradingPartnerForm.tin}"/>
                    </td>
                    <td>
                        <html:text styleClass="textlabelsBoldForTextBox" property="legalname" onkeyup="toUppercase(this)" value="${tradingPartnerForm.legalname}"/>
                    </td>
                    <td>
                        <input name="apSpecialistName" id="apSpecialistName" class="textlabelsBoldForTextBox" value="${tradingPartnerForm.apSpecialistName}">
                        <input type="hidden" name="apSpecialistId" id="apSpecialistId" value="${tradingPartnerForm.apSpecialistId}">
                        <input type="hidden" name="apSpecialistValid" id="apSpecialistValid" value="${tradingPartnerForm.apSpecialistName}"/>
                        <div id="apSpecialistDiv" class="newAutoComplete"></div>
                    </td>
                    <td>
                        <html:text styleClass="textlabelsBoldForTextBox" onkeyup="toUppercase(this)" property="dba" value="${tradingPartnerForm.dba}"/>
                    </td>
                    <td>
                        <html:checkbox property="wfile" styleId="wfile" name="tradingPartnerForm" onclick="fileUpload(this)"/>
                    </td>
                    <td>
                        <input type="button" value="View Image" class="buttonStyleNew" onclick="viewW9Image();"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Credit Limit</td>
                    <td>Credit Terms</td>
                    <td>EDI Code</td>
                    <td>Exempt from Inactivate</td>
                    <td id="subTypeLabel" colspan="2">SubType</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>
                        <html:text styleClass="textlabelsBoldForTextBox" property="climit" value="${tradingPartnerForm.climit}"
                                   onchange="validateLimit(this)" onblur="setcreditterms()" maxlength="13" size="13" style="width: 126px;"/>
                    </td>
                    <td><html:select property="cterms" styleClass="dropdown_accounting" value="${tradingPartnerForm.cterms}" style="width: 126px;" onchange="setcreditlimit()">
                            <html:optionsCollection name="creditTermList"/>
                        </html:select>
                    </td>
                    <td>
                        <html:text property="ediCode" styleId="ediCode" value="${tradingPartnerForm.ediCode}"
                                   styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"/>
                    </td>
                    <td>
                        <html:checkbox property="exemptInactivate" name="tradingPartnerForm" styleId="exemptInactivate" value="true"/>
                    </td>
                    <td colspan="2" id="subTypeValue">
                        <html:text property="subType" styleId="subType" value="${tradingPartnerForm.subType}"
                                   readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook" style="text-transform:uppercase;"/>
                    </td>
                </tr>
            </table>
            <br>
            <table width="100%" cellspacing="0" cellpadding="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td>AP Contact</td>
                    <td align="right" colspan="7">
                        <input type="button" class="buttonStyleNew" value="Select Contact"
                               style="width:80px" onClick="getAddressForAPConfig()"/>
                    </td>
                </tr>
                <tr class="textLabelsBold">
                    <td>C/o Name</td>
                    <td>
                        <input type="text" readonly="readonly" onkeyup="toUppercase(this)" class="textlabelsBoldForTextBoxDisabledLook"
                               value="${tradingPartnerForm.companyName}">
                        <html:hidden property="companyName" name="" value="${tradingPartnerForm.companyName}"/>
                    </td>
                    <td>Phone</td>
                    <td>
                        <input type="text" readonly="readonly" onkeyup="toUppercase(this)" class="textlabelsBoldForTextBoxDisabledLook"
                               value="${tradingPartnerForm.phone}">
                        <html:hidden property="phone" name="" value="${tradingPartnerForm.phone}"/>
                    </td>
                    <td>Fax</td>
                    <td>
                        <input type="text" readonly="readonly" onkeyup="toUppercase(this)" class="textlabelsBoldForTextBoxDisabledLook"
                               value="${tradingPartnerForm.fax}">
                        <html:hidden property="fax" name="" value="${tradingPartnerForm.fax}"/>
                    </td>
                    <td>Email</td>
                    <td>
                        <input type="text" readonly="readonly" onkeyup="toUppercase(this)" class="textlabelsBoldForTextBoxDisabledLook"
                               value="${tradingPartnerForm.email}">
                        <html:hidden property="email" name="" value="${tradingPartnerForm.email}"/>
                    </td>
                </tr>
            </table>
            <br>
            <table width="100%" class="tableBorderNew" cellpadding="0"
                   cellspacing="0">
                <tr class="tableHeadingNew">
                    <td>
                        Payment Information
                    </td>
                    <td align="right">
                        <c:if test="${empty tradingPartnerForm.paymentSet || fn:length(tradingPartnerForm.paymentSet)<5}">
                            <input type="button" class="buttonStyleNew" value="Add" style="width:65px" onclick="addPaymentInfo()"/>
                        </c:if>
                    </td>
                </tr>
                <tr>
                </tr>
                <tr>
                    <td colspan="2">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0"
                               class="displaytagstyleNew">
                            <display:table name="${tradingPartnerForm.paymentSet}"
                                           pagesize="${pageSize}" class="displaytagstyleNew"
                                           id="paymentInfoTable" sort="list" defaultsort="10">
                                <display:setProperty name="paging.banner.some_items_found">
                                    <span class="pagebanner"> <font color="blue">{0}</font>
                                    </span>
                                </display:setProperty>
                                <display:setProperty name="paging.banner.one_item_found">
                                    <span class="pagebanner"> One {0} displayed. Page
                                        Number </span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.all_items_found">
                                    <span class="pagebanner"> {0} {1} Displayed, Page
                                        Number </span>
                                    </display:setProperty>
                                    <display:setProperty name="basic.msg.empty_list">
                                    <span class="pagebanner"> No Records Found. </span>
                                </display:setProperty>
                                <display:column title="Payment<br>Method" property="paymethod"></display:column>
                                <display:column title="Bank<br>Name" property="bankname"/>
                                <display:column title="Bank<br>Address" property="baddr"/>
                                <display:column title="Account<br>Name" property="vacctname"></display:column>
                                <display:column title="Account<br>No" property="vacctno"></display:column>
                                <display:column title="<br>Aba" property="aba"></display:column>
                                <display:column title="<br>Swift" property="swift"></display:column>
                                <display:column title="Remit<br>Email" property="remail"></display:column>
                                <display:column title="Remit<br>Fax" property="rfax"></display:column>
                                <display:column title="<br>Primary" style="text-align:center;">
                                    <span> <c:choose>
                                            <c:when test="${paymentInfoTable.defaultpaymethod=='Y'}">
                                                <img alt="Default Payment Method" src="${path}/img/icons/ok.gif"/>
                                            </c:when>
                                        </c:choose> </span>
                                    </display:column>
                                    <display:column title="<br>Action">
                                    <span class="hotspot" onmouseout="tooltip.hide();"
                                          onmouseover="tooltip.show('<strong>Edit</strong>', null, event);">
                                        <img src="${path}/img/icons/edit.gif" alt="delte"
                                             onclick="editPaymentInfo(${paymentInfoTable.id})"/>
                                    </span>
                                    <span class="hotspot" onmouseout="tooltip.hide();"
                                          onmouseover="tooltip.show('<strong>Delete</strong>', null, event);">
                                        <img src="${path}/img/icons/delete.gif" alt="delte"
                                             onclick="deletePaymentMethod('${paymentInfoTable.paymethod}')"/>
                                    </span>
                                    <c:if test="${paymentInfoTable.paymethod=='ACH' && !empty paymentInfoTable.achDocumentName}">
                                        <span class="hotspot" onmouseout="tooltip.hide();"
                                              onmouseover="tooltip.show('<strong>View Ach Document</strong>', null, event);">
                                            <img src="${path}/img/icons/preview.gif" alt="view"
                                                 onclick="showAchDocument(${paymentInfoTable.id}, '${paymentInfoTable.achDocumentName}')"/>
                                        </span>
                                    </c:if>
                                </display:column>
                                <display:column style="display:none">
                                    <input name="paymentMethodAlreadyAdded" value="${paymentInfoTable.paymethod}"/>
                                </display:column>
                            </display:table>
                        </table>
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${!empty tradingPartnerForm.paymentSet}">
                        <input type="hidden" id="tempPaymemtSet" value="yes">
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" id="tempPaymemtSet" value="no">
                    </c:otherwise>
                </c:choose>
                <html:hidden property="tradingPartnerId" styleId="tradingPartnerId" value="${accountNo}"/>
                <html:hidden property="contactId" styleId="contactId" value="${accountNo}"/>
                <html:hidden property="custAddressId" styleId="custAddressId" value="${accountNo}"/>
                <html:hidden property="payMethodId" styleId="payMethodId" value=""/>
                <html:hidden property="buttonValue" styleId="buttonValue"/>
                <html:hidden property="fileLocation" styleId="fileLocation" value="${tradingPartnerForm.fileLocation}"/>
            </table>
            <%@include file="/jsps/Tradingpartnermaintainance/vendorPopUpDivs.jspf"%>
        </html:form>
    </body>
    <script type="text/javascript" src="${path}/js/TradingPartner/vendor.js"></script>
    <c:if test="${!empty apAddress}">
        <script type="text/javascript">
                                                     showPopUp();
                                                     this.setPopupAttributes('addressDiv');
        </script>
    </c:if>
    <!-- THIS CONDITION IS TO DISABLE THE PAGE BASED ON LOGIN USER ROLE -->
    <c:if test="${view == '3' || not empty disableTabBasedOnRole}">
        <script type="text/javascript">
            disablePage(document.getElementById("vendor"));
        </script>
    </c:if>
    <script type="text/javascript">
        showSubType();
    </script>
</html>




