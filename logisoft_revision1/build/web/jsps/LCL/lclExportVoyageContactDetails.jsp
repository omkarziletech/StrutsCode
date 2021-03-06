<%-- 
    Document   : lclExportVoyageContactDetails
    Created on : Oct 3, 2016, 2:34:35 PM
    Author     : Kuppu
--%>
<%@include file="init.jsp"%>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/taglib.jsp"%>
<%@include file="/jsps/preloader.jsp"%>
<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
<script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
<cong:javascript src="${path}/js/jquery/jquery.util.js"/>
<script type="text/javascript">
    var GB_ROOT_DIR = "${pageContext.request.contextPath}/js/greybox/";
</script>
<cong:form  id="lclContactDetailsForm" name="lclContactDetailsForm" action="lclContactDetails.do">
    <cong:div id="contactTable">
        <cong:table width="97%" border="0" cellpadding="1" cellspacing="0" >
            <cong:tr styleClass="tableHeadingNew">
                <cong:td colspan="3">Contact Search</cong:td>
                <cong:td align="right">
                    <input type="button" value="Add Contact" class="buttonStyleNew" id="addDetail"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Customer Name</cong:td>
                <cong:td>
                    <cong:text name="vendorName"  id="vendorName" styleClass="textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclContactDetailsForm.vendorName}"/>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Customer Number</cong:td>
                <cong:td>
                    <cong:text name="vendorNo" id="vendorNo" styleClass="textlabelsBoldForTextBoxDisabledLook textCap" readOnly="true" value="${lclContactDetailsForm.vendorNo}"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td>
                    <input type="button" value="Submit" class="button-style1"  style="width:50px;" onclick="submitEmailAddress()"/>
                </cong:td>
                <cong:td>&nbsp;</cong:td>
            </cong:tr>
            <cong:table width="100%" border="0" cellpadding="1" cellspacing="0">
                <cong:tr styleClass="tableHeadingNew">
                    <cong:td align="left"> List of Contacts</cong:td>
                </cong:tr>
            </cong:table>
        </cong:table>
    </cong:div>
    <cong:div id="saveTable" style="width:99.5%; float:left; height:250px; overflow-y:scroll; border:1px solid #dcdcdc">
        <cong:table border="1" width="100%" style="margin:5px 0; float:left;border:1px solid #dcdcdc">
            <cong:tr styleClass="tableHeading2">
                <cong:td width="5%">Select</cong:td>
                <cong:td width="5%">Customer No</cong:td>
                <cong:td width="8%">Contact Name</cong:td>
                <cong:td width="5%">Position</cong:td>
                <cong:td width="6%">Email</cong:td>
                <cong:td width="4%">Phone</cong:td>
                <cong:td width="4%">Fax</cong:td>
                <cong:td width="1%">A</cong:td>
                <cong:td width="1%">B</cong:td>
                <cong:td width="1%">C</cong:td>
                <cong:td width="1%">D</cong:td>
                <cong:td width="1%">E</cong:td>
                <cong:td width="1%">F</cong:td>
                <cong:td width="1%">G</cong:td>
                <cong:td width="1%">H</cong:td>
                <cong:td width="1%">I</cong:td>
                <cong:td width="1%">J</cong:td>
                <cong:td width="1%">K</cong:td>
                <cong:td width="4%">Action</cong:td>
            </cong:tr>
            <c:forEach items="${contactList}" var="contact" varStatus="count">
                <input type="hidden" id="contact${count.index}" value="${contact.firstName} ${contact.lastName}"/>
                <c:set var="zebra" value="${zebra=='odd' ? 'even' : 'odd'}"/>
                <cong:tr styleClass="${zebra}">  
                    <cong:td>
                        <input type="checkbox" name="selectContact${count.index}"  id="${count.index}" 
                               class="checkContact" value="${contact.email}#${contact.fax}"/>
                    </cong:td>
                    <cong:td>${contact.accountNo}</cong:td>
                    <cong:td>${contact.firstName}&nbsp;&nbsp;${contact.lastName}</cong:td>
                    <cong:td>${contact.position}</cong:td>
                    <cong:td>${contact.email}</cong:td>
                    <cong:td>${contact.phone}</cong:td>
                    <cong:td>${contact.fax}</cong:td>
                    <cong:td>${contact.codea.code}</cong:td>
                    <cong:td>${contact.codeb.code}</cong:td>
                    <cong:td>${contact.codec.code}</cong:td>
                    <cong:td>${contact.coded.code}</cong:td>
                    <cong:td>${contact.codee.code}</cong:td>
                    <cong:td>${contact.codef.code}</cong:td>
                    <cong:td>${contact.codeg.code}</cong:td>
                    <cong:td>${contact.codeh.code}</cong:td>
                    <cong:td>${contact.codei.code}</cong:td>
                    <cong:td>${contact.codej.code}</cong:td>
                    <cong:td>${contact.codek.code}</cong:td>
                    <cong:td>
                        <img src="${path}/images/edit.png" alt="edit" style="cursor: pointer" onclick="editContact('${contact.id}',
                                        '${contact.accountNo}', '${contact.firstName}', '${contact.lastName}', '${contact.position}', '${contact.email}', '${contact.phone}', '${contact.fax}', '${contact.codea.code}', '${contact.codeb.code}',
                                        '${contact.codec.code}', '${contact.coded.code}', '${contact.codee.code}', '${contact.codef.code}', '${contact.codeg.code}', '${contact.codeh.code}', '${contact.codei.code}', '${contact.codej.code}', '${contact.codek.code}')"/>
                        <img src="${path}/img/icons/trash.jpg" alt="delete" height="12px" width="12px" onclick="deleteContact('Are you sure you want to delete?', '${contact.id}');" style="cursor:pointer"/>
                    </cong:td>
                </cong:tr>
            </c:forEach>
        </cong:table>
    </cong:div>

    <cong:div id="contactDetails" style="display: none">
        <cong:table width="100%" styleClass="tableBorderNew" style="border: 10px;" border="0">
            <cong:tr styleClass="tableHeadingNew">
                <cong:td colspan="6">Enter Contact Details</cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">First Name</cong:td>
                <cong:td>
                    <cong:text name="firstName" id="firstName"  maxlength="40" styleClass="textuppercaseLetter"/>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Last Name</cong:td>
                <cong:td>
                    <cong:text name="lastName" id="lastName"  maxlength="40" styleClass="textuppercaseLetter"/>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Position</cong:td>
                <cong:td>
                    <cong:text name="position" id="position" styleClass="textuppercaseLetter" maxlength="50"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Email</cong:td>
                <cong:td>
                    <cong:text name="email" id="email" maxlength="50"/>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Phone
                    <cong:td><cong:text name="phone" id="phone" style="width:70px" maxlength="30"/>
                        <span class="textlabelsBoldforlcl">Ext</span>
                        <cong:text name="extension" id="extension" styleClass="textuppercaseLetter" style="width:40px" maxlength="4"/></cong:td>
                </cong:td>
                <cong:td styleClass="textlabelsBoldforlcl">Fax</cong:td>
                <cong:td>
                    <cong:text name="fax" id="fax" maxlength="40" styleClass="textuppercaseLetter"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBoldforlcl">Comment</cong:td>
                <cong:td  colspan="2"><cong:textarea name="comment" styleClass="textLCLuppercase" id="comment" cols="35" rows="3"/></cong:td>
                <cong:td colspan="3"></cong:td>
            </cong:tr></cong:table>
        <cong:table width="100%" styleClass="tableBorderNew" style="border: 10px;" border="0">
            <cong:tr styleClass="tableHeadingNew">
                <cong:hidden name="custContactId" id="custContactId"/>
                <cong:td valign="middle">Auto Notification Configuration&nbsp;&nbsp;&nbsp;
                </cong:td>
                <cong:td colspan="3">
                    <input type="button" value="Code Definitions" class="buttonStyleNew" onclick="openContactPdf('${path}');"/>
                </cong:td>
            </cong:tr>
            <cong:tr styleClass="textlabelsBold">
                <cong:td>CODE A   &nbsp; General Marketing</cong:td>
                <cong:td>
                    <cong:text name="codea" id="codea" style="width:60px" maxlength="5" styleClass="textuppercaseLetter"/>
                    <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="lookup" title="Lookup" onclick="openCode('A')"/>
                </cong:td>
                <cong:td>CODE F &nbsp; LCL Import Arrival Notice Status</cong:td>
                <cong:td>
                    <cong:text name="codef" id="codef" style="width:60px" maxlength="5" styleClass="textuppercaseLetter"/>
                    <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="lookup" title="Lookup" onclick="openCode('F')"/>
                </cong:td>
            </cong:tr>
            <cong:tr styleClass="textlabelsBold">
                <cong:td>CODE B  &nbsp; LCL D/R Whse Status Change</cong:td>
                <cong:td>
                    <cong:text name="codeb" id="codeb" style="width:60px" maxlength="5" styleClass="textuppercaseLetter"/>
                    <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="lookup" title="Lookup" onclick="openCode('B')"/>
                </cong:td>
                <cong:td>CODE G(&nbsp;Imports Only)Default Contact
                    Person For Routing Instructions</cong:td>
                <cong:td>
                    <cong:text name="codeg" id="codeg" style="width:60px" maxlength="5" styleClass="textuppercaseLetter"/>
                    <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="lookup" title="Lookup" onclick="openCode('G')"/>
                </cong:td>
            </cong:tr>
            <cong:tr styleClass="textlabelsBold">
                <cong:td>CODE C &nbsp;  FCL Notification</cong:td>
                <cong:td>
                    <cong:text name="codec" id="codec" style="width:60px" maxlength="5" styleClass="textuppercaseLetter"/>
                    <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="lookup" title="Lookup" onclick="openCode('C')"/>
                </cong:td>
                <cong:td>CODE H &nbsp;   Air Flight Schedule</cong:td>
                <cong:td>
                    <cong:text name="codeh" id="codeh" style="width:60px" maxlength="5" styleClass="textuppercaseLetter"/>
                    <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="lookup" title="Lookup" onclick="openCode('H')"/>
                </cong:td>
            </cong:tr>
            <cong:tr styleClass="textlabelsBold">
                <cong:td>CODE D  &nbsp; LCL B/L (Air/Ocean) Notice</cong:td>
                <cong:td>
                    <cong:text name="coded" id="coded"  style="width:60px" maxlength="5" styleClass="textuppercaseLetter"/>
                    <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="lookup" title="Lookup" onclick="openCode('D')"/>
                </cong:td>
                <cong:td>CODE I &nbsp;  Voyage Change Notice</cong:td>
                <cong:td>
                    <cong:text name="codei" id="codei" style="width:60px" maxlength="5" styleClass="textuppercaseLetter"/>
                    <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="lookup" title="Lookup" onclick="openCode('I')"/>
                </cong:td>
            </cong:tr>
            <cong:tr styleClass="textlabelsBold">
                <cong:td>CODE E &nbsp; Air Uplift Notice</cong:td>
                <cong:td>
                    <cong:text name="codee" id="codee" style="width:60px" maxlength="5" styleClass="textuppercaseLetter"/>
                    <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" height="16px" width="16px" title="Lookup" onclick="openCode('E')"/>
                </cong:td>
                <cong:td>CODE J<span style="padding-left:15px;">LCL Notification</span></cong:td>
                <cong:td>
                    <cong:text name="codej" id="codej" style="width:60px" maxlength="5" styleClass="textuppercaseLetter"/>
                    <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" height="16px" width="16px" title="Lookup" onclick="openCode('J')"/>
                </cong:td>
            </cong:tr>
            <cong:tr styleClass="textlabelsBold">
                <cong:td>CODE K&nbsp;Invoice Notification</cong:td>
                <cong:td>
                    <cong:text name="codek" id="codek" style="width:60px" maxlength="5" styleClass="textuppercaseLetter"/>
                    <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" height="16px" width="16px" title="Lookup" onclick="openCode('K')"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td></cong:td>
                <cong:td>&nbsp;</cong:td>
                <cong:td colspan="2" align="center">
                    <input type="button" class="button-style1" value="Save" onclick="submitForm('saveContact')"/>
                    <input type="button" value="Cancel" class="cancelBut button-style1"/>
                </cong:td>
            </cong:tr>
            <cong:hidden name="methodName" id="methodName"/>
        </cong:table>
    </cong:div>
    <input type="hidden" name="isVoyageContact" id="isVoyageContact" value="${lclContactDetailsForm.isVoyageContact}"/>
</cong:form>

<script >
    jQuery(document).ready(function () {
        $("[title != '']").not("link").tooltip();
    });
    $('#addDetail').click(function () {
        $("#contactDetails").show();
        $('#codea').val('');
        $('#firstName').val('');
        $('#lastName').val('');
        $('#email').val('');
        $('#position').val('');
        $('#extension').val('');
        $('#fax').val('');
        $('#phone').val('');
        $('#comment').val('');
        $('#codeb').val('');
        $('#codec').val('');
        $('#coded').val('');
        $('#codee').val('');
        $('#codef').val('');
        $('#codeg').val('');
        $('#codeh').val('');
        $('#codei').val('');
        $('#codej').val('');
        $('#codek').val('');
        $("#contactTable").hide();
        $("#saveTable").hide();
    });
    $('.cancelBut').click(function () {
        $("#contactDetails").hide();
        $("#contactTable").show();
        $("#saveTable").show();
    });
    function congAlert(txt) {
        $.prompt(txt);
    }
    function submitForm(methodName) {
        if (methodName === 'saveContact' && $('#firstName').val().trim() === '') {
            sampleAlert("Please Enter First Name");
        } else {
            $("#methodName").val(methodName);
            $("#lclContactDetailsForm").submit();
        }
    }
    function deleteContact(txt, id) {
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    showProgressBar();
                    $('#custContactId').val(id);
                    $('#methodName').val('deleteContact');
                    $("#lclContactDetailsForm").submit();
                    hideProgressBar();
                    $.prompt.close();
                }
                else if (v == 2) {
                    $.prompt.close();
                }
            }
        });
    }
    function editContact(id, accountNo, firstName, lastName, position, email, phone, fax,
            codea, codeb, codec, coded, codee, codef, codeg, codeh, codei, codej, codek) {
        $("#custContactId").val(id);
        $("#accountNo").val(accountNo);
        $("#firstName").val(firstName);
        $("#lastName").val(lastName);
        $("#position").val(position);
        $("#email").val(email);
        $("#phone").val(phone);
        $("#fax").val(fax);
        $("#codea").val(codea);
        $("#codeb").val(codeb);
        $("#codec").val(codec);
        $("#coded").val(coded);
        $("#codee").val(codee);
        $("#codef").val(codef);
        $("#codeg").val(codeg);
        $("#codeh").val(codeh);
        $("#codei").val(codei);
        $("#codej").val(codej);
        $("#codek").val(codek);
        $("#contactDetails").show();
        $("#contactTable").hide();
        $("#saveTable").hide();
    }
    function openCode(val) {
        var code = val;
        window.open('${path}/lclContactDetails.do?methodName=codeDetails&field1=' + code, '', 'width=600,height=500,scrollbars=yes');
    }

    function setContactRefreshIcon() {
        parent.parent.GB_hide();
    }

    function openContactPdf(path) {
        showLoading();
        jQuery.ajaxx({//printReport method changed due to change in FileName
            data: {
                className: "com.gp.cong.lcl.dwr.LclPrintUtil",
                methodName: "getPropertyPdf",
                param1: "application.contactCodeManual"
            },
            async: false,
            success: function (data) {
                var url = path + '/servlet/FileViewerServlet?fileName=' + data;
                window.open(url, 'Contact', 'width=1200,height=600,top=40,left=40,directories=no,location=no,linemenubar=no,toolbar=no,status=no');
            }
        });
        closePreloading();
    }

    function submitEmailAddress() {
        var emailArr = new Array();
        var faxArr = new Array();
        var contactNameArr = new Array();
        $(".checkContact").each(function () {
            if ($(this).is(":checked")) {
                var emailORFax = $(this).val().split("#");
                var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
                if (emailORFax[0] !== '' && emailReg.test(emailORFax[0])) {
                    emailArr.push(emailORFax[0]);
                }
                if (emailORFax[1] !== '') {
                    faxArr.push(emailORFax[1]);
                    var id = $(this).attr("id");
                    contactNameArr.push($("#contact" + id).val());
                }
            }
        });
        var isEmailorFax = window.parent.parent.jQuery("#isEmailOrFax").val();
        if (isEmailorFax === 'EMAIL') {
            window.parent.parent.jQuery("#toAddress").val($.unique(emailArr).toString());
        } else if (isEmailorFax === 'FAX') {
            window.parent.parent.jQuery("#toName").val($.unique(contactNameArr).toString());
            window.parent.parent.jQuery("#toFaxNumber").val($.unique(faxArr).toString());
        }
        parent.parent.GB_hide();
    }

</script>
