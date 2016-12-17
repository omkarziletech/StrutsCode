<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.CustomerContact,com.gp.cong.logisoft.bc.notes.NotesConstants"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../Tradingpartnermaintainance/tradingPartnerSelectList.jsp"%>
<%
            String path = request.getContextPath();
            String clientAcctNo = null, from = null, clientAcctName = null;
//----requests from customerAddress page of Quotes----
            if (request.getAttribute("customerAcctNoFromQuotes") != null) {
                clientAcctNo = (String) request.getAttribute("customerAcctNoFromQuotes");
                request.setAttribute("clientAcctNo", clientAcctNo);
                from = "QuotesContactLookUp";
            }
            if (request.getAttribute("customerAcctNameFromQuotes") != null) {
                clientAcctName = (String) request.getAttribute("customerAcctNameFromQuotes");
            }
//----ends------
%>
<html>
    <head>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../fragment/formSerialize.jspf" %>
        <script language="javascript" src="<%=path%>/js/common.js" ></script>
        <script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
        <link type="text/css" rel="stylesheet" media="screen" href="<%=path%>/jsps/LCL/js/colorbox/colorbox.css" />
        <script type="text/javascript" src="<%=path%>/jsps/LCL/js/colorbox/jquery.colorbox.js"></script>
         
        <script language="javascript">
            start = function() {
                serializeForm();
            }
            window.onload = start;
        </script>
        <script type="text/javascript">

            function deleteContactInfo(val1) {
                document.tradingPartnerForm.index.value = val1;
                document.tradingPartnerForm.buttonValue.value = "deleteContactConfiguration";
                var result = confirm("Are you sure you want to delete this Contact ");
                if (result) {
                    document.tradingPartnerForm.submit();
                }
            }
            function saveContactInfo(val1) {
                if (document.tradingPartnerForm.firstName.value == "")
                {
                    alert("Please enter the First Name");
                    document.tradingPartnerForm.firstName.value = "";
                    document.tradingPartnerForm.firstName.focus();
                    return;
                }
                var val = document.tradingPartnerForm.firstName.value
                var checkingSpaceChar= document.tradingPartnerForm.lastName.value
                if (val.match(" "))
                {
                    alert("WhiteSpace is not allowed for First Name");
                    return;
                }

                if(checkingSpaceChar.match(" "))
                {
                    alert("WhiteSpace is not allowed for Last Name");
                    return;
                }
                if (isSpecialExceptAsteric(document.tradingPartnerForm.firstName.value) == false)
                {
                    alert("Special Characters not allowed for First Name");
                    document.tradingPartnerForm.firstName.value = "";
                    document.tradingPartnerForm.firstName.focus();
                    return;
                }
                
                if (isSpecialExceptAsteric(document.tradingPartnerForm.lastName.value) == false)
                {
                    alert("Special Characters not allowed for Last Name");
                    document.tradingPartnerForm.lastName.value = "";
                    document.tradingPartnerForm.lastName.focus();
                    return;
                }
                var value = document.tradingPartnerForm.phone.value;
                for (var i = 0; i < value.length; i++)
                {
                    if (value.indexOf(" ") == 0)
                    {
                        alert("Please dont start with white space");
                        return;
                    }
                }
                if (IsNumeric(document.tradingPartnerForm.phone.value.replace(/ /g, '')) == false)
                {
                    alert("Telephone Number should be Numeric");
                    document.tradingPartnerForm.phone.value = "";
                    document.tradingPartnerForm.phone.focus();
                    return;
                }
                /* if(document.tradingPartnerForm.phone.value!="" && document.tradingPartnerForm.phone.value.length<13)
                 {
                 alert("Phone Number should be 13 Digits");
                 document.tradingPartnerForm.phone.value="";
                 document.tradingPartnerForm.phone.focus();
                 return;
                 } */
                if (IsNumeric(document.tradingPartnerForm.extension.value.replace(/ /g, '')) == false)
                {
                    alert("Extension should be Numeric.");
                    document.tradingPartnerForm.extension.value = "";
                    document.tradingPartnerForm.extension.focus();
                    return;
                }
                var value = document.tradingPartnerForm.fax.value;
                for (var i = 0; i < value.length; i++)
                {
                    if (value.indexOf(" ") == 0)
                    {
                        alert("Please dont start with white space");
                        return;
                    }
                }
                if (IsNumeric(document.tradingPartnerForm.fax.value.replace(/ /g, '')) == false)
                {
                    alert("Fax Number should be Numeric.");
                    document.tradingPartnerForm.fax.value = "";
                    document.tradingPartnerForm.fax.focus();
                    return;
                }
                /*if(document.tradingPartnerForm.fax.value!="" && document.tradingPartnerForm.fax.value.length<13)
                 {
                 alert("Fax Number should be 13 Digits");
                 document.tradingPartnerForm.fax.value="";
                 document.tradingPartnerForm.fax.focus();
                 return;
                 } */
                if (document.tradingPartnerForm.comment.value != "" && document.tradingPartnerForm.comment.value.length > 100)
                {
                    alert("Comment should be only 100 characters");
                    document.tradingPartnerForm.comment.value = "";
                    document.tradingPartnerForm.comment.focus();
                    return;
                }
                if (val1) {
                    document.tradingPartnerForm.buttonValue.value = "UpdateContactConfiguration";
                }
                else {
                    document.tradingPartnerForm.buttonValue.value = "saveContactConfiguration";
                }
                document.tradingPartnerForm.submit();
            }
            function editContactInfoForImageIcon(val1) {
                document.tradingPartnerForm.index.value = val1;
                document.tradingPartnerForm.buttonValue.value = "editContactForImageIcon";
                document.tradingPartnerForm.submit();
            }
            function Cancel(val1) {
                if (val1) {
                    document.tradingPartnerForm.buttonValue.value = "editcancel";
                } else {
                    document.tradingPartnerForm.buttonValue.value = "cancel";
                }
                document.tradingPartnerForm.submit();
            }
            function addNewContact() {
                document.tradingPartnerForm.buttonValue.value = "addNewContactDetails";
                document.tradingPartnerForm.submit();
            }
            function view(form) {
                var element;
                for (var i = 0; i < form.elements.length; i++) {
                    element = form.elements[i];
                    if (element.type == "button") {
                        if (element.value == "Add New Contact") {
                            element.style.visibility = "hidden";
                        }
                    }
                }
                var datatableobj = document.getElementById("contactConfigTable");

                if (datatableobj != null) {
                    for (i = 0; i < datatableobj.rows.length; i++) {
                        document.getElementById("delete" + i).style.visibility = "hidden";
                    }
                }
            }
            function getCodeLookUp(val) {
                var Code = val;
                var href ='<%=path%>/commodityDescriptionLookUp.do?buttonValue=ContactConfigCode&commDesc=' + Code;
                mywindow = window.open(href, '', 'width=600,height=800,scrollbars=yes');
                mywindow.moveTo(200, 180);
            }
            var popupWindow = null;
            function openCodeLookUp(val) {
                var Code = val;
                popupWindow = window.open('<%=path%>/commodityDescriptionLookUp.do?buttonValue=ContactConfigCode&FromPage=Quotes&commDesc=' + Code,
                'Codes', 'width=600,height=500,scrollbars=yes');
            }
            function openContactCodePdf() {
                popupWindow = window.open('<%=path%>/tradingPartner.do?buttonValue=contactCodeManualPdf',
                'Contact', 'width=1200,height=600,top=40,left=40,directories=no,location=no,linemenubar=no,toolbar=no,status=no');
            }
            function setContactCode(val1, val2, val3) {
                var code = val3;
                if (code == 'A') {
                    document.tradingPartnerForm.codea.value = val1;
                } else if (code == 'B') {
                    document.tradingPartnerForm.codeb.value = val1;
                } else if (code == 'C') {
                    document.tradingPartnerForm.codec.value = val1;
                } else if (code == 'D') {
                    document.tradingPartnerForm.coded.value = val1;
                } else if (code == 'E') {
                    document.tradingPartnerForm.codee.value = val1;
                } else if (code == 'F') {
                    document.tradingPartnerForm.codef.value = val1;
                } else if (code == 'G') {
                    document.tradingPartnerForm.codeg.value = val1;
                } else if (code == 'H') {
                    document.tradingPartnerForm.codeh.value = val1;
                } else if (code == 'I') {
                    document.tradingPartnerForm.codei.value = val1;
                } else if (code == 'J') {
                    document.tradingPartnerForm.codej.value = val1;
                }else if (code == 'K') {
                    document.tradingPartnerForm.codek.value = val1;
                }
            }
             function jCodeContact(custContactId, tradingPartnerId,path) {
                var href = path + "/JcodeContactDetailsAction.do?methodName=display&index=" + custContactId + "&tradingPartnerId=" + tradingPartnerId;
                $.colorbox({
                    iframe: true,
                    href: href,
                    width: "55%",
                    height: "65%",
                    title: "Code J Contact Details"
                });
            }
             function kCodeContact(custContactId, tradingPartnerId,path) {
                var href = path + "/KcodeContactDetailsAction.do?methodName=display&index=" + custContactId + "&tradingPartnerId=" + tradingPartnerId;
                $.colorbox({
                    iframe: true,
                    href: href,
                    width: "55%",
                    height: "40%",
                    title: "Code K Contact Details"
                });
            }
        </script>
        <%@include file="../includes/resources.jsp" %>
    </head>

    <body class="whitebackgrnd">
        <html:form action="/tradingPartner" name="tradingPartnerForm" styleId="contactConfig" type="com.gp.cong.logisoft.struts.form.TradingPartnerForm" scope="request">

            <c:if test="${addContactDetails!=null ||clientAcctNo!=null}">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                    <tr class="tableHeadingNew" colspan="2"><font style="font-weight: bold">Enter Contact Details</font></tr>
                <tr>
                    <td>
                        <table width="100%" cellpadding="3" cellspacing="0" border="0">
                            <tr class="textlabelsBold">
                                <td width="10%">First Name</td>
                                <td width="23%">
                                    <html:text styleClass="textlabelsBoldForTextBox"  property="firstName" size="24" style="text-transform: uppercase"
                                               value="${customerContactDetails.firstName}" maxlength="30"/></td>
                                <td width="10%">Last Name</td>
                                <td width="23%">
                                    <html:text styleClass="textlabelsBoldForTextBox"  property="lastName" maxlength="30" style="text-transform: uppercase"
                                               value="${customerContactDetails.lastName}" size="24"/></td>
                                <td width="10%">Position</td>
                                <td width="23%">
                                    <html:text styleClass="textlabelsBoldForTextBox"  property="position" maxlength="50" style="text-transform: uppercase"
                                               value="${customerContactDetails.position}"/></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Email</td>
                                <td><html:text styleClass="textlabelsBoldForTextBox" size="24" property="email" style="text-transform: uppercase"
                                           value="${customerContactDetails.email}" maxlength="50" /></td>
                                <td>Phone</td>
                                <td><html:text styleClass="textlabelsBoldForTextBox" property="phone" style="text-transform: uppercase" value="${customerContactDetails.phone}" maxlength="15" size="12" />
                                    Ext&nbsp;&nbsp;<html:text styleClass="textlabelsBoldForTextBox" property="extension" value="${customerContactDetails.extension}" maxlength="4" size="2"/>
                                </td>
                                <td>Fax</td>
                                <td><html:text styleClass="textlabelsBoldForTextBox" property="fax"  style="text-transform: uppercase" maxlength="15" value="${customerContactDetails.fax}" /></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Comment</td>
                                <td><html:textarea  property="comment" value="${customerContactDetails.comment}" style="text-transform: uppercase"
                                                styleClass="textlabelsBoldForTextBox" rows="2" cols="25" onkeypress="return checkTextAreaLimit(this, 200)"/></td>
                            </tr>
                            <tr>
                                <td colspan="6">
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                        <tr class="tableHeadingNew"><font style="font-weight: bold">Auto Notification Configuration&nbsp;&nbsp;
                                            <input type="button" value="Code Definitions" class="buttonStyleNew" onclick="openContactCodePdf();" style="vertical-align: middle"/>
                                        </font></tr>
                        </table>
                    </td>
                </tr>
                <tr>
                <table width="100%">
                    <tr><td width="50%">
                            <table width="100%">
                                <tr class="textlabelsBold">
                                    <td>CODE A<span style="padding-left:10px;">General Marketing</span></td>
                                    <td align="left">
                                        <html:text styleClass="textlabelsBoldForTextBox" property="codea" size="5" maxlength="5" style="text-transform: uppercase"
                                                   value="${tradingPartnerForm.codea}"></html:text>
                                        <%if (from == null) {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="getCodeLookUp('A')" />
                                        </span>

                                        <%} else {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="openCodeLookUp('A')" />
                                        </span>
                                        <%}%>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>CODE B<span style="padding-left:10px;"> LCL D/R Whse Status Change</span></td>
                                    <td>
                                        <html:text styleClass="textlabelsBoldForTextBox" property="codeb" size="5" maxlength="5" style="text-transform: uppercase"
                                                   value="${tradingPartnerForm.codeb}"></html:text>
                                        <%if (from == null) {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="getCodeLookUp('B')" />
                                        </span>
                                        <%} else {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="openCodeLookUp('B')" />
                                        </span>
                                        <%}%>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>CODE C<span style="padding-left:10px;">FCL Notification</span></td>
                                    <td>
                                        <html:text styleClass="textlabelsBoldForTextBox" property="codec" size="5" maxlength="5" style="text-transform: uppercase"
                                                   value="${tradingPartnerForm.codec}"></html:text>
                                        <%if (from == null) {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="getCodeLookUp('C')" />
                                        </span>
                                        <%} else {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="openCodeLookUp('C')" />
                                        </span>
                                        <%}%>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>CODE D<span style="padding-left:10px;">LCL B/L (Air/Ocean) Notice</span></td>
                                    <td>
                                        <html:text styleClass="textlabelsBoldForTextBox" property="coded" size="5" maxlength="5" style="text-transform: uppercase"
                                                   value="${tradingPartnerForm.coded}"></html:text>
                                        <%if (from == null) {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="getCodeLookUp('D')" />
                                        </span>
                                        <%} else {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="openCodeLookUp('D')" />
                                        </span>
                                        <%}%>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>CODE E<span style="padding-left:10px;">Air Uplift Notice</span></td>
                                    <td>
                                        <html:text styleClass="textlabelsBoldForTextBox" property="codee" size="5" maxlength="5" style="text-transform: uppercase"
                                                   value="${tradingPartnerForm.codee}"></html:text>
                                        <%if (from == null) {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="getCodeLookUp('E')" />
                                        </span>
                                        <%} else {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="openCodeLookUp('E')" />
                                        </span>
                                        <%}%>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%">
                            <table width="100%">
                                <tr class="textlabelsBold">
                                    <td>CODE F<span style="padding-left:12px;">LCL Import Arrival Notice Status</span></td>
                                    <td>
                                        <html:text styleClass="textlabelsBoldForTextBox" property="codef"  size="5" maxlength="5" style="text-transform: uppercase"
                                                   value="${tradingPartnerForm.codef}"></html:text>
                                        <%if (from == null) {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="getCodeLookUp('F')" />
                                        </span>
                                        <%} else {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="openCodeLookUp('F')" />
                                        </span>
                                        <%}%>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>CODE G<span style="padding-left:10px;">(Imports Only)Default Contact</span><br><span  style="padding-left:60px;">Person For Routing Instructions</span></td>
                                    <td>
                                        <html:text styleClass="textlabelsBoldForTextBox" property="codeg" size="5" maxlength="5" style="text-transform: uppercase"
                                                   value="${tradingPartnerForm.codeg}"></html:text>
                                        <%if (from == null) {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="getCodeLookUp('G')" />
                                        </span>
                                        <%} else {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="openCodeLookUp('G')" />
                                        </span>
                                        <%}%>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>CODE H<span style="padding-left:10px;">Air Flight Schedule</span></td>
                                    <td>
                                        <html:text styleClass="textlabelsBoldForTextBox" property="codeh" size="5" maxlength="5" style="text-transform: uppercase"
                                                   value="${tradingPartnerForm.codeh}"></html:text>
                                        <%if (from == null) {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="getCodeLookUp('H')" />
                                        </span>
                                        <%} else {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="openCodeLookUp('H')" />
                                        </span>
                                        <%}%>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>CODE I<span style="padding-left:15px;">Voyage Change Notice</span></td>
                                    <td>
                                        <html:text styleClass="textlabelsBoldForTextBox" property="codei" size="5" maxlength="5" style="text-transform: uppercase"
                                                   value="${tradingPartnerForm.codei}"></html:text>
                                        <%if (from == null) {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="getCodeLookUp('I')" />
                                        </span>
                                        <%} else {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="openCodeLookUp('I')" />
                                        </span>
                                        <%}%>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>CODE J<span style="padding-left:15px;">LCL Notification</span></td>
                                    <td>
                                        <html:text styleClass="textlabelsBoldForTextBox" property="codej" size="5" maxlength="5" style="text-transform: uppercase"
                                                   value="${tradingPartnerForm.codej}"></html:text>
                                        <%if (from == null) {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="getCodeLookUp('J')" />
                                        </span>
                                        <%} else {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="openCodeLookUp('J')" />
                                        </span>
                                        <%}%>
                                        <c:if test="${not empty tradingPartnerForm.codej}">
                                         <span class="hotspot" onmouseover="tooltip.show('<strong>Code J Contact Details</strong>', null, event);" onmouseout="tooltip.hide();">
                                                <img src="<%=path%>/images/icons/trading_partner.png" onclick="jCodeContact('${customerContactDetails.id}','${tradingPartnerId}','<%=path%>');" />
                                         </span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>CODE K<span style="padding-left:15px;"> Invoice Notification</span></td>
                                    <td>
                                        <html:text styleClass="textlabelsBoldForTextBox" property="codek" size="5" maxlength="5" style="text-transform: uppercase"
                                                   value="${tradingPartnerForm.codek}"></html:text>
                                        <%if (from == null) {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="getCodeLookUp('K')" />
                                        </span>
                                        <%} else {%>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Look Up</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/display.gif" onclick="openCodeLookUp('K')" />
                                        </span>
                                        <%}%>
                                         <c:if test="${tradingPartnerForm.codek eq 'E' || tradingPartnerForm.codek eq 'F'}">
                                         <span class="hotspot" onmouseover="tooltip.show('<strong>Code K Contact Details</strong>', null, event);" onmouseout="tooltip.hide();">
                                                <img src="<%=path%>/images/icons/trading_partner.png" onclick="kCodeContact('${customerContactDetails.id}','${tradingPartnerId}','<%=path%>');" />
                                         </span>
                                        </c:if>
                                    </td>                                    
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table></tr>
            <tr>
                <td  align="center" colspan="6" style="padding-top:7px;padding-bottom:7px;">
                    <input type="button" class="buttonStyleNew" value="Save" onclick="saveContactInfo(${customerContactDetails.id})" id="save"/>
                    <input type="button" class="buttonStyleNew" value="Cancel" onclick="Cancel(${customerContactDetails.id})" id="cancel"/>
                </td>
            </tr>

        </table></td>
</tr>
</table>
</c:if>
<br/>

<c:if test="${(addContactDetails==null) && (clientAcctNo==null)}">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
        <%-- <tr class="tableHeadingNew" colspan="2">--%>
        <%-- 	<td>--%>
        <%-- 		Contact Details--%>
        <%-- 	</td>--%>
        <%-- </tr>--%>
        <tr class="tableHeadingNew" colspan="2">
            <td>
                Contact Details
            </td>
            <td align="right">
                <input type="button" class="buttonStyleNew" value="Add New Contact"
                       style="width:110px" id="addNewContactToggle" onclick="addNewContact()"/>
                <input type="button" value="Code Definitions" class="buttonStyleNew" onclick="openContactCodePdf();" style="vertical-align: middle"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <div id="divtablesty1" class="scrolldisplaytable">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                        <%int i = 0;%>
                        <display:table name="${TRADINGPARTNER.customerContact}" class="displaytagstyleNew" id="contactConfigTable" pagesize="<%=pageSize%>" defaultsort="1" defaultorder="ascending">

                            <display:setProperty name="paging.banner.some_items_found">
                                <span class="pagebanner">
                                    <font color="blue">{0}</font> Contact Details displayed,For more Customer Contacts click on Page Numbers.

                                </span>
                            </display:setProperty>
                            <display:setProperty name="paging.banner.one_item_found">
                                <span class="pagebanner">
                                    One {0} displayed. Page Number
                                </span>
                            </display:setProperty>
                            <display:setProperty name="paging.banner.all_items_found">
                                <span class="pagebanner">
                                    {0} {1} Displayed, Page Number

                                </span>
                            </display:setProperty>

                            <display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
                                    No Records Found.
                                </span>
                            </display:setProperty>
                            <display:setProperty name="paging.banner.placement" value="bottom" />

                            <display:column title="FirstName" property="firstName"/>
                            <display:column title="LastName" property="lastName"/>
                            <display:column title="Position" property="position"/>
                            <display:column title="Phone Number" property="phone"/>
                            <display:column title="Extension" property="extension"/>
                            <display:column title="Fax" property="fax"/>
                            <display:column title="Email" property="email"/>
                            <display:column title="A" property="codea.code" style="width:30px"/>
                            <display:column title="B" property="codeb.code" style="width:30px"/>
                            <display:column title="C" property="codec.code" style="width:30px"/>
                            <display:column title="D" property="coded.code" style="width:30px"/>
                            <display:column title="E" property="codee.code" style="width:30px"/>
                            <display:column title="F" property="codef.code" style="width:30px"/>
                            <display:column title="G" property="codeg.code" style="width:30px"/>
                            <display:column title="H" property="codeh.code" style="width:30px"/>
                            <display:column title="I" property="codei.code" style="width:30px"/>
                            <display:column title="J" property="codej.code" style="width:30px"/>${contactConfigTable.codej.code}
                            <display:column title="K" property="codek.code" style="width:30px"/>
                            <display:column title="Actions">
                                <span class="hotspot" onmouseover="tooltip.show('Edit', null, event);" onmouseout="tooltip.hide();">
                                    <img src="<%=path%>/img/icons/edit.gif" border="0" onclick="editContactInfoForImageIcon(${contactConfigTable.id})" /></span>
                                <span class="hotspot" onmouseover="tooltip.show('Delete', null, event);" onmouseout="tooltip.hide();">
                                    <img src="<%=path%>/img/icons/delete.gif" border="0" id="delete<%=i%>" onclick="deleteContactInfo(${contactConfigTable.id})" /></span>
                                <span class="hotspot" onmouseover="tooltip.show('<strong>Notes</strong>', null, event);" onmouseout="tooltip.hide();">
                                    <img src="<%=path%>/img/icons/e_contents_view.gif" border="0" onclick="return GB_show('Notes', '<%=path%>/notes.do?moduleId=' + '<%=NotesConstants.CONTACTCONFIGURATION%>&moduleRefId=' + '${contactConfigTable.id}', 375, 700);" />
                                </span>
                            </display:column>
                            <%i++;%>
                        </display:table>
                    </table></div>
            </td>
        </tr>
    </table>
</c:if>
<!-- setting the hidden properties when contact is added from Quotes page -->
<c:choose>
    <c:when test="${clientAcctNo!=null}">
        <html:hidden property="tradingPartnerId" value="<%=clientAcctNo%>"/>
        <html:hidden property="fromMaster" value="<%=from%>"/>
        <html:hidden property="accountName" value="<%=clientAcctName%>"/>
    </c:when>
    <c:otherwise>
        <html:hidden property="tradingPartnerId" value="<%=accountNo%>"/>
    </c:otherwise>
</c:choose>

<!-- THIS CONDITION IS TO DISABLE THE PAGE BASED ON LOGIN USER ROLE -->
<c:if test="${view == '3' || not empty disableTabBasedOnRole}">
    <script type="text/javascript">
        disablePage(document.getElementById("contactConfig"));
    </script>
</c:if>
<html:hidden property="buttonValue" styleId="buttonValue"/>
<html:hidden property="name" styleId="name"/>
<html:hidden property="index" value="${customerContactDetails.id}"/>
</html:form> 

</body>
</html>

