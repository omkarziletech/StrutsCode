<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.bc.notes.NotesConstants,
         com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.hibernate.dao.UserDAO,com.gp.cong.logisoft.domain.User,
         com.gp.cong.logisoft.domain.Role,com.gp.cong.logisoft.domain.RefTerminal,
         com.gp.cong.logisoft.domain.GenericCode,java.util.*,com.gp.cong.logisoft.domain.lcl.LclSearchTemplate"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../includes/resources.jsp" %>
<jsp:useBean id="editUserForm" scope="request" class="com.gp.cong.logisoft.struts.form.EditUserForm"/>
<jsp:useBean id="userDao" scope="request" class="com.gp.cong.logisoft.hibernate.dao.UserDAO"/>
<c:set var="packageList" value="${userDao.packageList}" scope="request"/>
<c:set var="palletList" value="${userDao.palletList}" scope="request"/>
<%    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    DBUtil dbUtil = new DBUtil();
    com.gp.cong.logisoft.domain.User user = new com.gp.cong.logisoft.domain.User();
    String countryId = "";
    String cityId = "";
    String state = "";
    String roleId = "0";
    String terminalId = "";
    String importterminalId = "";
    String billingTerminalId = "";
    String templateId = "";

    if (session.getAttribute("user") != null) {
        user = (com.gp.cong.logisoft.domain.User) session.getAttribute("user");
        if (user.getDifflclBookedDimsActual() != null && !user.getDifflclBookedDimsActual().equals("") && user.getDifflclBookedDimsActual().equals("1")) {
            editUserForm.setDifflclBookedDimsActual(user.getDifflclBookedDimsActual());
        } else {
            editUserForm.setDifflclBookedDimsActual("0");
        }
        editUserForm.setDifflclBookedDimsActual(user.getDifflclBookedDimsActual());
        if (user != null && user.getUnLocation() != null && user.getUnLocation().getCountryId() != null && user.getUnLocation().getCountryId().getCodedesc() != null) {
            countryId = user.getUnLocation().getCountryId().getCodedesc();
        }
        if (user != null && user.getUnLocation() != null && user.getUnLocation().getUnLocationName() != null) {
            cityId = user.getUnLocation().getUnLocationName();
        }
        if (user != null && user.getUnLocation() != null && user.getUnLocation().getStateId() != null && user.getUnLocation().getStateId().getCode() != null) {
            state = user.getUnLocation().getStateId().getCode();
        }
        if (user.getCity() != null && user.getCity() != "") {
            cityId = user.getCity();
        }
    }
    String userName = user.getLoginName();
    User user1 = null;
    if (session.getAttribute("loginuser") != null) {
        user1 = (User) session.getAttribute("loginuser");
    }
    String login = user1.getLoginName();
    List allTerminalList = dbUtil.getTerminalList();
    
    request.setAttribute("terminallist", allTerminalList);
    request.setAttribute("ImportTerminalList", allTerminalList);
    request.setAttribute("BillingTerminalList", allTerminalList);
    request.setAttribute("rolelist", dbUtil.getRoleList());
    request.setAttribute("statuslist", dbUtil.getStatusList());
    List templateList = dbUtil.getTemplateList();
    request.setAttribute("templateList", templateList);
    String msg = "";
    String modify = null;
    session.setAttribute("editpage", "editpage");
    if (user.getRole() != null && user.getRole().getRoleId() != null) {
        roleId = user.getRole().getRoleId().toString();
    }
    RefTerminal terminal = user.getTerminal();
    if (terminal != null && terminal.getTrmnum() != null) {
        terminalId = terminal.getTrmnum();
    }
    RefTerminal importterminal = user.getImportTerminal();
    if (importterminal != null && importterminal.getTrmnum() != null) {
        importterminalId = importterminal.getTrmnum();
    }
    RefTerminal billingTerminal = user.getBillingTerminal();
    if (billingTerminal != null && billingTerminal.getTrmnum() != null) {
        billingTerminalId = billingTerminal.getTrmnum();
    }
    LclSearchTemplate template = user.getUserTemplate();
    if (template != null) {
        templateId = template.getId().toString();
    }
    modify = (String) session.getAttribute("modifyforuser");
    session.setAttribute("printermodify", modify);
    if (request.getAttribute("message") != null) {
        msg = (String) request.getAttribute("message");
    }
// Name:Yogesh Date:11/30/2007  ----> Is view only when page is locked
    if (request.getAttribute("view") != null) {
        modify = (String) request.getAttribute("view");
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">
        <title>Edit User Information</title>

        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/default/style.css">
        <link type="text/css" rel="stylesheet" href="${path}/css/fileUpload/enhanced.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/fileUpload/jquery.fileinput.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lable-fields.css"/>

        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <%@include file="../../jsps/preloader.jsp"%>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <%--    <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
         <script type="text/javascript" src="${path}/js/common.js"></script>
            <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
            <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
            <script type="text/javascript" src="${path}/js/script.aculo.us/script-aculo-us.tabs.js"></script>
            <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
            <script type="text/javascript" src="${path}/js/isValidEmail.js" ></script>--%>
        <script type="text/javascript">
            var path = "${path}";
            jQuery(document).ready(function () {
                jQuery("[title != '']").not("link").tooltip();
                $("#signatureImagesId").fileInput();
                initAutoCompleteFields();
            });
            function initAutoCompleteFields() {
                $("#pod").initautocomplete({
                    url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=PORT&template=port&fieldIndex=1&",
                    width: "420px",
                    resultsClass: "ac_results z-index",
                    resultPosition: "absolute",
                    scroll: true,
                    scrollHeight: 200
                            //otherFields: "clientNumber"
                });
                $("#ctsAccount").initautocomplete({
                    url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=CLIENT&template=tradingPartner&fieldIndex=1,2&",
                    width: "525px",
                    otherFields: "ctsAccountNo",
                    resultsClass: "ac_results z-index",
                    resultPosition: "absolute",
                    scroll: true,
                    scrollHeight: 200
                });
            }
            var newwindow = '';
            function openEditList() {
                if (!newwindow.closed && newwindow.location)
                {
                    newwindow.location.href = "<%=path%>/jsps/admin/Printer.jsp";
                }
                else
                {
                    newwindow = window.open("<%=path%>/jsps/admin/Printer.jsp", "", "width=500,height=200");
                    if (!newwindow.opener)
                        newwindow.opener = self;
                }
                if (window.focus) {
                    newwindow.focus()
                }
                document.editUser.submit();
                return false;
            }
            function alertMsg(txtMsg, selector) {
                $.prompt(txtMsg, {
                    callback: function () {
                        jQuery("#" + selector).val("").callFocus();
                    }
                });
            }
            function saveUser() {
                var firstName = jQuery('#firstName').val();
                if (firstName == "") {
                    alertMsg("Please enter the First Name", "firstName");
                    return;
                }
                if (firstName.match(" ")) {
                    alertMsg("WhiteSpace is not allowed for First Name", "firstName");
                    return;
                }
                if (isSpecial(firstName) == false) {
                    alertMsg("Special Characters not allowed for First Name", "firstName");
                    return;
                }
                var password = jQuery('#password').val();
                var loginName = jQuery('#loginName').val();
                if (password == "") {
                    alertMsg("Please enter the Password", "password");
                    return;
                }
                if (password.match(" ")) {
                    alertMsg("Space is not allowed for Password", "password");
                    return;
                }
                if (password.length < 6) {
                    alertMsg("Password must be more than five characters", "password");
                    return;
                }
                if (IsPassword(password) === false) {
                    alertMsg("Please enter the proper Password", "password");
                    return;
                }
                if (loginName === password) {
                    alertMsg("Login name and Password should not be same", "password");
                    return;
                }
                var retypePassword = jQuery('#retypePassword').val();
                if (retypePassword === "") {
                    alertMsg("Please enter the retype Password", "retypePassword");
                    return;
                }
                if (password !== retypePassword) {
                    alertMsg("Password and Retype Password must be same", "retypePassword");
                    return;
                }
                if (jQuery('#terminal').val() === "0") {
                    alertMsg("Please select the Terminal", "terminal");
                    return;
                }
                if (jQuery('#importTerminal').val() === "0") {
                    alertMsg("Please select the ImportTerminal", "importTerminal");
                    return;
                }
                if (jQuery('#roleId').val() === "0") {
                    alertMsg("Please select the Role", "roleId");
                    return;
                }

                var lastName = jQuery('#lastName').val();
                if ("" != lastName && isSpecial(lastName) == false) {
                    alertMsg("Special Characters not allowed for Last Name", "lastName");
                    return;
                }

                var zip = jQuery('#zipId').val();
                if (zip != null && zip != "") {
                    if (IsNumeric(zip) == false) {
                        alertMsg("Zipcode should be Numeric", "zipId");
                        return;
                    }
                    if (zip.length < 5) {
                        alertMsg("Zipcode should be 5 Digits", "zipId");
                        return;
                    }
                    if (zip.length > 5 && zip.length < 9) {
                        alertMsg("Zipcode should be 9 Digits", "zipId");
                        return;
                    }
                }
                var extension = jQuery('#extension').val();
                if ("" != extension && IsNumeric(extension) == false) {
                    alertMsg("Extension should be Numeric", "extension");
                    return;
                }
                var email = jQuery('#email').val();
                if ("" != email && validateEmails(email) == false) {
                    alertMsg("Please enter proper Email", "email");
                    return;
                }
                var outsourceEmail = jQuery('#outsourceEmail').val();
                if ("" != outsourceEmail && validateEmails(outsourceEmail) == false) {
                    alertMsg("Please enter proper Outsource Email", "outsourceEmail");
                    return;
                }
                //var value=document.editUser.telephone.value;

                //for(var i=0;i< value.length;i++)
                //{
                //if(value.indexOf(" ") != -1)
                //{
                //alert("Please dont start with white space");
                //return;
                //}
                //}

                //if(IsNumeric(document.editUser.telephone.value.replace(/ /g,''))==false)
                // {
                // alert("Telephone Number should be Numeric.");
                //document.editUser.telephone.value="";
                //document.editUser.telephone.focus();
                //return;
                // }
                //if(document.editUser.telephone.value!="" && document.editUser.telephone.value.length<13)
                //{
                //alert("Phone Number should be 13 Digits");
                // document.editUser.telephone.value="";
                //document.editUser.telephone.focus();
                //return;
                // }
                // var val=document.editUser.fax.value;

                //for(var i=0;i< val.length;i++)
                //{
                //if(val.indexOf(" ") != -1)
                //{
                //alert("Please dont start with white space");
                //return;
                //}
                //}

                //if(IsNumeric(document.editUser.fax.value.replace(/ /g,''))==false)
                // {
                //alert("Fax Number should be Numeric.");
                //document.editUser.fax.value="";
                //document.editUser.fax.focus();
                //return;
                // }
                //if(document.editUser.fax.value!="" && document.editUser.fax.value.length<13)
                // {
                // alert("Fax Number should be 13 Digits");
                // document.editUser.fax.value="";
                //document.editUser.fax.focus();
                //return;
                //}
                showLoading();
                document.editUser.buttonValue.value = "save";
                document.editUser.submit();
            }
            function validateEmails(field) {
                var regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,5}$/;
                return (regex.test(field)) ? true : false;
            }
            function selectcity()
            {
                if (document.editUser.country.value == "0")
                {
                    alert("Please enter the country");
                    document.editUser.city.value = "0";
                    document.editUser.state.value = "";
                    return;
                }
                document.editUser.buttonValue.value = "selectcity";
                document.editUser.submit();
            }
            function selectstate()
            {
                if (document.editUser.city.value == "0")
                {
                    alert("Please enter the city");
                    document.editUser.state.value = "";
                    return;
                }
                document.editUser.buttonValue.value = "selectstate";
                document.editUser.submit();
            }
            function updateTerminal() {
                if (jQuery('#terminal').val() === "0") {
                    alertMsg("Please select the Terminal", "terminal");
                    return;
                }
                showLoading();
                document.editUser.buttonValue.value = "terminalselected";
                document.editUser.submit();
            }
            function updateImpTerminal() {
                if (jQuery('#importTerminal').val() === "0") {
                    alertMsg("Please select the Import Terminal", "importTerminal");
                    return;
                }
                showLoading();
                document.editUser.buttonValue.value = "importterminalselected";
                document.editUser.submit();
            }
            function updateBillingTerminal() {
                if (jQuery('#billingTerminal').val() === "0") {
                    alertMsg("Please select the Billing Terminal", "billingTerminal");
                    return;
                }
                showLoading();
                document.editUser.buttonValue.value = "importterminalselected";
                document.editUser.submit();
            }
            function cancelbtn(val) {
                if (val == 0 || val == 3) {
                    document.editUser.buttonValue.value = "cancelview";
                } else {
                    document.editUser.buttonValue.value = "cancel";
                }
                showLoading();
                document.editUser.submit();
            }
            function disabled(val1, val2) {
                if (val1 == 0 || val1 == 3) {
                    var imgs = document.getElementsByTagName('img');
                    for (var k = 0; k < imgs.length; k++) {
                        if (imgs[k].id != "cancel" && imgs[k].id != "addprinter"
                                && imgs[k].id != "note" && imgs[k].id != "images") {
                            imgs[k].style.visibility = 'hidden';
                        }
                    }
                    var input = document.getElementsByTagName("input");
                    for (i = 0; i < input.length; i++) {
                        if (input[i].id != "buttonValue") {
                            input[i].readOnly = true;
                            input[i].style.color = "blue";
                        }
                    }
                    var select = document.getElementsByTagName("select");
                    for (i = 0; i < select.length; i++) {
                        select[i].disabled = true;
                        //select[i].style.backgroundColor = "blue";
                    }
                    document.getElementById("delete").style.visibility = 'hidden';
                    document.getElementById("save").style.visibility = 'hidden';
                    jQuery("#signatureImagesId").hide();
                    jQuery("#addDestination").hide();
                }
                if (val1 == 1) {
                    document.getElementById("delete").style.visibility = 'hidden';
                }
                if (val1 == 3 && val2 != "") {
                    alert(val2);
                }
            }

            function deleteUser() {
                var loginUser = jQuery('#loginUser').val();
                var userName = jQuery('#userName').val();
                if (loginUser == userName) {
                    $.prompt("This user cannot be Delete");
                    return;
                }
                $.prompt("Are you sure you want to delete this user", {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v === 1) {
                            showLoading();
                            document.editUser.buttonValue.value = "delete";
                            document.editUser.submit();
                        } else if (v === 2) {
                            $.prompt.close();
                        }
                    }
                });
            }
            function confirmnote() {
                document.editUser.buttonValue.value = "note";
                document.editUser.submit();
            }
            function deleteAgent(userId, agentId) {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.dwr.DwrUtil",
                        methodName: "deleteAgentInformation",
                        forward: "/jsps/admin/AgentInformation.jsp",
                        param1: agentId,
                        param2: userId,
                        request: true
                    },
                    success: function (data) {
                        if (data) {
                            jQuery("#agentInformation").html(data);
                            jQuery("#pod").val('');
                        }
                    }
                });
            }
            function addAgent(userId) {
                var destination = jQuery("#pod").val();
                if (destination != "") {
                    jQuery.ajaxx({
                        dataType: "json",
                        data: {
                            className: "com.gp.cong.logisoft.dwr.DwrUtil",
                            methodName: "checkAgentDestinationExist",
                            param1: destination,
                            param2: userId,
                            dataType: "json"
                        },
                        success: function (data) {
                            if (data) {
                                alertMsg("Unloc code already exist for the user", 'pod');
                            } else {
                                jQuery.ajaxx({
                                    data: {
                                        className: "com.gp.cong.logisoft.dwr.DwrUtil",
                                        methodName: "addAgentInformation",
                                        forward: "/jsps/admin/AgentInformation.jsp",
                                        param1: destination,
                                        param2: userId,
                                        request: true
                                    },
                                    success: function (data) {
                                        if (data) {
                                            jQuery("#agentInformation").html(data);
                                            jQuery("#pod").val('');
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    alertMsg("Please Enter Destination is Required", 'pod');
                }
            }
            function allowOnlyWholeNumbers(obj) {
                if (!/^\d*(\.\d{0,2})?$/.test(obj.value)) {
                    obj.value = "";
                    return false;
                }
            }
            function showAgentInformation(obj) {
                var role = jQuery("#roleId option:selected").text().toLowerCase();
                if (role == 'AGENT' || role == 'agent') {
                    document.getElementById("userAgent").style.display = 'block';
                } else {
                    document.getElementById("userAgent").style.display = 'none';
                }
            }
            function popup1(mylink, windowname)
            {
                if (!window.focus)
                    return true;
                var href;
                if (typeof (mylink) == 'string')
                    href = mylink;
                else
                    href = mylink.href;
                mywindow = window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
                mywindow.moveTo(200, 180);

                return false;
            }

            function searchcity() {

                document.editUser.buttonValue.value = "searchcity";
                document.editUser.submit();
            }
            function print() {
                document.editUser.buttonValue.value = "print";
                document.editUser.submit();
            }
            function readURL(input) {
                if (input.files && input.files[0]) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        jQuery('#images')
                                .attr('src', e.target.result)
                                .width(200)
                                .height(28);
                    };
                    reader.readAsDataURL(input.files[0]);
                }
            }
        </script>
    </head>
    <body class="whitebackgrnd" onLoad="disabled('<%=modify%>', '<%=msg%>')" >
        <div class="error">
            <c:if test="${not empty error}">
                <c:out value="${error}"/>
            </c:if>
        </div>
        <html:form action="/editUser" name="editUser" enctype="multipart/form-data"
                   type="com.gp.cong.logisoft.struts.form.EditUserForm" scope="request">


            <div align="right">

                <!input type="button" class="buttonStyleNew" value="LCL Defaults" id="lclDefaults" onclick="return GB_show('Lcl User Defaults', '<%=path%>/lclUserDefaults.do?methodName=display',350,750)">
                <input type="button" class="buttonStyleNew" value="Save" id="save"  onclick="saveUser()">

                <input type="button" class="buttonStyleNew" value="Add Printer" id="addprinter"
                       onclick="return GB_show('User', '<%=path%>/printer.do?printerList=' + 'add', 450, 800)"
                       />

                <input type="button" class="buttonStyleNew" value="Go Back" id="cancel" onclick="cancelbtn(<%=modify%>)"/>
                <input type="button" class="buttonStyleNew" value="Delete" id="delete" onclick="deleteUser()">
                <input type="button" class="buttonStyleNew" id="note" onclick="return GB_show('Notes', '<%=path%>/notes.do?moduleId=' + '<%=NotesConstants.EDITUSER%>', 300, 700);" id="note" name="search" value="Note"/>

            </div>
            <table width="100%" border="0" style="margin-top: 2px"
                   cellpadding="0" cellspacing="0" class="tableBorderNew" >
                <tr>
                    <td class="tableHeadingNew">
                        <bean:message key="form.newUserForm.edituser" />
                    </td>
                </tr>

                <tr>
                    <td>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr><td colspan="4" style="padding-top: 2px"></td></tr>
                            <tr>
                                <td id="labelText">Login Name&nbsp;
                                    <%--                                    <bean:message key="form.newUserForm.loginName"/>--%>
                                </td>
                                <td>
                                    <html:text property="loginName" readonly="true" value="${user.loginName}" styleId="loginName"
                                               styleClass="varysizeareahighlightgrey textlabelsBoldForTextBox width-200px"/>
                                </td>
                                <td>
                                    <%if (user.isAchApprover()) {%>
                                    <input type="checkbox" name="achApprover" value="true" checked style="vertical-align: middle"/>
                                    <%	} else {%>
                                    <html:checkbox property="achApprover" name="editUser"
                                                   style="vertical-align: middle"/>
                                    <%}%>
                                    <label id="labelText" class="align-left">&nbsp;ACH Approver</label>
                                </td>
                                <td>
                                    <%if (user.isSearchScreenReset()) {%>
                                    <input type="checkbox" title="If this is checked it will automatically <br>reset the search screen for users"
                                           name="searchScreenReset" value="true" checked style="vertical-align: middle"/>
                                    <%	} else {%>
                                    <html:checkbox property="searchScreenReset" style="vertical-align: middle"
                                                   title="If this is checked it will automatically <br>reset the search screen for users" name="editUser"/>
                                    <%}%>
                                    <label id="labelText" class="align-left">&nbsp;Search Screen Reset</label>
                                </td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 2px"></td></tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" >
                            <tr> 
                                <td class="tableHeadingNew" colspan="4">
                                    <bean:message key="form.newUserForm.personal" />
                                </td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 2px"></td></tr>
                            <tr>
                                <td id="labelText">
                                    <bean:message key="form.newUserForm.firstName" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;</td>
                                <td><html:text property="firstName" value="${user.firstName}" styleId="firstName"
                                           styleClass="areahighlightyellow1 textlabelsBoldForTextBox width-200px" />
                                </td>

                                <td id="labelText"><bean:message key="form.newUserForm.lastName" />&nbsp;</td>

                                <td>
                                    <html:text property="lastName" styleId="lastName" styleClass="textlabelsBoldForTextBox width-200px"
                                               value="${user.lastName}"/>
                                </td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 2px"></td></tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                            <tr>
                                <td colspan="7" class="tableHeadingNew">
                                    <bean:message key="form.newUserForm.contact" />
                                </td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 4px"></td></tr>
                            <tr>
                                <td colspan="4"></td>
                                <td rowspan="16">
                                    <table class="table margin-none border-none">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty user.signaturImg}">
                                                        <img src="data:image/png;base64,${user.signaturImg}"
                                                             width="200px" height="28px" id="images" alt="ImageIcon"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src=""
                                                             width="200px" height="28px" id="images" alt="Signature Image Icon"/>
                                                    </c:otherwise>
                                                </c:choose>

                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="label">
                                                    <input type="file" id="signatureImagesId" name="signatureImageOutput"
                                                           onchange="readURL(this)"
                                                           accept="image/x-png, image/gif, image/jpeg" value="SignatureImage"/>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td id="labelText" class="align-left">Upload Signature Image</td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td id="labelText">
                                    <bean:message key="form.newUserForm.extension" />&nbsp;
                                </td>
                                <td>
                                    <html:text property="extension" styleClass="textlabelsBoldForTextBox width-200px"
                                               styleId="extension"  value="${user.extension}"/>
                                </td>
                                <td id="labelText"><bean:message key="form.newUserForm.email" />&nbsp;
                                </td>
                                <td>
                                    <html:text property="email" value="${user.email}"
                                               styleId="email" styleClass="textlabelsBoldForTextBox width-200px" />
                                </td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 3px"></td></tr>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.terminal" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;</td>
                                <td>
                                    <html:select property="terminal" styleClass="dropdown_accounting unfixedtextfiledstyle"
                                    onchange="updateTerminal()" value="<%=terminalId%>" styleId="terminal" >
                                        <html:option value="0">Select ExportTerminal</html:option>
                                        <html:optionsCollection name="terminallist" styleClass="areahighlightyellow"/>
                                    </html:select>
                                </td>
                                <td id="labelText"><bean:message key="form.newUserForm.officeCityLocation" />&nbsp;</td>

                                <td>
                                    <html:text property="officeCityLocation" styleClass="textlabelsBoldForTextBox width-200px"
                                               value="${user.officeCityLOcation}" maxlength="5"/>
                                </td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 3px"></td></tr>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.importterminal" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;</td>
                                <td>
                                    <html:select property="importTerminal" styleClass="dropdown_accounting unfixedtextfiledstyle"
                                    onchange="updateImpTerminal()" styleId="importTerminal" value="<%=importterminalId%>">
                                        <html:option value="0">Select ImportTerminal</html:option>
                                        <html:optionsCollection name="ImportTerminalList" styleClass="areahighlightyellow"/>
                                    </html:select></td>
                                <td id="labelText">Outsource Email&nbsp;</td>
                                <td>
                                    <html:text property="outsourceEmail" styleClass="textlabelsBoldForTextBox width-200px"
                                               styleId="outsourceEmail" value="${user.outsourceEmail}"/>
                                </td>
                            </tr>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.billingTerminal" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;
                                </td>
                                <td>
                                    <html:select property="billingTerminal" styleClass="dropdown_accounting unfixedtextfiledstyle"
                                    onchange="updateBillingTerminal()" styleId="billingTerminal" value="<%=billingTerminalId%>">
                                        <html:option value="0">Select BillingTerminal</html:option>
                                        <html:optionsCollection name="BillingTerminalList" styleClass="areahighlightyellow"/>
                                    </html:select>
                                </td>
                                <td id="labelText">LCL Export Default Template</td>
                                <td><html:select property="templateId" styleClass="dropdown_accounting unfixedtextfiledstyle"
                                     styleId="templateId" value="<%=templateId%>">
                                        <html:option value="0">Select Template</html:option>
                                        <html:optionsCollection name="templateList" styleClass="areahighlightyellow"/>
                                    </html:select>
                                </td>
                            </tr>

                            <tr><td colspan="4" style="padding-top: 3px"></td></tr>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.telephone" />&nbsp;</td>
                                <td><html:text property="telephone" value="${user.telephone}" styleClass="textlabelsBoldForTextBox width-200px" /></td>
                                <td id="labelText"><bean:message key="form.newUserForm.fax" />&nbsp;</td>
                                <td><html:text property="fax" value="${user.fax}" styleClass="textlabelsBoldForTextBox width-200px" /></td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 3px"></td></tr>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.address1" />&nbsp;</td>
                                <td><html:text property="address1" value="${user.address1}" styleClass="textlabelsBoldForTextBox width-200px" /></td>
                                <td id="labelText"><bean:message key="form.newUserForm.address2" />&nbsp;</td>
                                <td><html:text property="address2" value="${user.address2}" styleClass="textlabelsBoldForTextBox width-200px" /></td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 3px"></td></tr>
                            <tr>
                                <td id="labelText">City &nbsp;
                                    <img border="0" src="${path}/img/search1.gif" alt="citySearchIcon"
                                         onclick="return popup1('<%=path%>/jsps/datareference/searchpopup.jsp?button=' + 'editusercity', 'windows')">
                                </td>
                                <td><html:text property="city" value="<%=cityId%>"
                                           onkeyup="searchcity()" styleClass="textlabelsBoldForTextBox width-200px" />
                                </td>
                                <td id="labelText"><bean:message key="form.newUserForm.country" />&nbsp;</td>
                                <td>
                                    <html:text property="country" value="<%=countryId%>"
                                               readonly="true" styleClass="varysizeareahighlightgrey textlabelsBoldForTextBox width-200px"/>
                                </td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 3px"></td></tr>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.state" />&nbsp;</td>
                                <td>
                                    <html:text property="state" value="<%=state%>" readonly="true"
                                               styleClass="varysizeareahighlightgrey textlabelsBoldForTextBox width-200px"/>
                                </td>
                                <td id="labelText"><bean:message key="form.newUserForm.zip" />&nbsp;</td>
                                <td><html:text property="zipCode" value="${user.zipCode}" styleId="zipId"
                                           onkeypress="getzip(this)" styleClass="textlabelsBoldForTextBox width-200px"  maxlength="10"/>
                                </td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 3px"></td></tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td colspan="6" class="tableHeadingNew">
                                    <bean:message key="form.newUserForm.loginInfo" />
                                </td>
                            </tr>
                            <tr><td colspan="6" style="padding-top: 3px"></td></tr>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.role" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;</td>
                                <td>
                                    <html:select property="role" styleClass="dropdown_accounting unfixedtextfiledstyle"
                                    value="<%=String.valueOf(roleId)%>" onchange="showAgentInformation(this)" styleId="roleId">
                                        <html:optionsCollection name="rolelist" styleClass="areahighlightyellow"/>
                                    </html:select>
                                </td>
                                <td id="labelText"><bean:message key="form.newUserForm.status" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;</td>
                                <td>
                                    <html:select property="status" styleClass="dropdown_accounting unfixedtextfiledstyle"
                                                 value="${user.status}">
                                        <html:optionsCollection name="statuslist"/>
                                    </html:select>
                                </td>
                                <td id="labelText">Warehouse</td>
                                <td id="labelText" class="align-left">
                                    <c:set var="warehouseValue" value="${user.warehouse}"/>
                                    <input type="radio" id="warehouseY" name="warehouse" value="true" <c:if test="${warehouseValue}">checked</c:if>/>Yes
                                    <input type="radio" id="warehouseN" name="warehouse" value="false" <c:if test="${!warehouseValue}">checked</c:if>/>No
                                    <td>
                                </tr>
                                <tr><td colspan="6" style="padding-top: 3px"></td></tr>
                                <tr>
                                    <td id="labelText"><bean:message key="form.newUserForm.password" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;
                                </td>
                                <td>
                                    <html:password property="password" value="${user.password}" styleId="password"
                                                   styleClass="areahighlightyellow1 textlabelsBoldForTextBox width-145px"/></td>
                                <td id="labelText"><bean:message key="form.newUserForm.repassword" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;
                                </td>
                                <td>
                                    <html:password property="retypePassword" value="${user.password}" styleId="retypePassword"
                                                   styleClass="areahighlightyellow1 textlabelsBoldForTextBox width-145px"/>
                                </td>
                                <td id="labelText">WarehouseNo</td>
                                <td>
                                    <html:text property="warehouseNo" value="${user.warehouseNo}" styleId="warehouseNo"
                                               styleClass="textlabelsBoldForTextBox width-145px" maxlength="6"/>   
                                </td>
                            </tr>
                            <tr><td colspan="6" style="padding-top: 3px"></td></tr>
                            <tr>
                                <td id="labelText" colspan="2">
                                    Auto notify when Booked dims are different from Actual</td>
                                <td id="labelText" class="align-left">
                                    <html:radio property="difflclBookedDimsActual" name="editUserForm" value="1" />Yes
                                    <html:radio property="difflclBookedDimsActual" name="editUserForm" value="0"/>No<td>
                                <td colspan="3"></td>
                            </tr>
                        </table>

                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%"  border="0">
                            <tr>
                                <td class="tableHeadingNew" colspan="8">CTS Information</td>
                            </tr>
                            <tr>
                                <td style="padding-top: 2px" colspan="8"></td>
                            </tr>
                            <tr>
                                <td id="labelText">Account</td>
                                <td>
                                    <html:text styleClass="textlabelsBoldForTextBox" property="ctsAccount"
                                               styleId="ctsAccount" value="${user.ctsAccount.accountName}"/>
                                    <html:hidden  property="ctsAccountNo" styleId="ctsAccountNo" value="${user.ctsAccount.accountno}"/>
                                    <%-- <script type="text/javascript">
                                    <div id="ctsaccount_choices"  style="display:none;" class="autocomplete"></div>
                                        initAutocompleteWithFormClear("ctsAccount", "ctsaccount_choices", "ctsAccountNo", "ctsAccountCheck",
                                        "${path}/actions/tradingPartner.jsp?tabName=USER", "getCtsAccount()", "");
                                    </script>--%>
                                </td>
                                <td id="labelText">Package Type</td>
                                <td>
                                    <html:select property="ctsPackageType" styleId="ctsPackageType"
                                                 styleClass="dropdown_accounting unfixedtextfiledstyle" value="${user.ctsPackageType}">
                                        <html:optionsCollection  name="packageList" />
                                    </html:select>
                                </td>
                                <td id="labelText">Pallet Type</td>
                                <td>
                                    <html:select property="ctsPalletType" styleId="ctsPalletType"
                                                 styleClass="dropdown_accounting unfixedtextfiledstyle"  value="${user.ctsPalletType}">
                                        <html:optionsCollection  name="palletList" />
                                    </html:select>
                                </td>
                                <td colspan="2"></td>
                            </tr>
                            <tr>
                                <td style="padding-top: 2px" colspan="8"></td>
                            </tr>
                            <tr>
                                <td id="labelText">Line MarkUp</td>
                                <td>
                                    <html:text styleClass="textlabelsBoldForTextBox" property="lineMarkUp"
                                               styleId="lineMarkUp"   size="20" value="${user.lineMarkUp}"
                                               onkeyup="allowOnlyWholeNumbers(this);"/>
                                </td>
                                <td id="labelText">Fuel MarkUp</td>
                                <td>
                                    <html:text styleClass="textlabelsBoldForTextBox" property="fuelMarkUp"
                                               styleId="fuelMarkUp"   size="20" value="${user.fuelMarkUp}"
                                               onkeyup="allowOnlyWholeNumbers(this);"/>
                                </td>
                                <td id="labelText">Flat Fee</td>
                                <td>
                                    <html:text styleClass="textlabelsBoldForTextBox" property="flatFee"
                                               styleId="flatFee"   size="20" value="${user.flatFee}"
                                               onkeyup="allowOnlyWholeNumbers(this);"/>
                                </td>
                                <td id="labelText">Min Amount</td>
                                <td>
                                    <html:text styleClass="textlabelsBoldForTextBox" property="minAmount"
                                               styleId="minAmount"   size="20" value="${user.minAmount}"
                                               onkeyup="allowOnlyWholeNumbers(this);"/>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding-top: 2px" colspan="8"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <c:set var="display" value="none"/>
                <c:if test="${user.role.roleDesc eq 'AGENT' || user.role.roleDesc eq 'agent'}">
                    <c:set var="display" value="block"/>
                </c:if>
                <tr id="userAgent" style="display: ${display}">
                    <td>
                        <table width="100%" border="0">
                            <tr>
                                <td colspan="2" class="tableHeadingNew">Agent Information</td>
                            </tr>
                            <tr>
                                <td id="labelText">Destination</td>
                                <td>
                                    <input name="pod" id="pod" class="textlabelsBoldForTextBox width-145px"/>
                                    <%--  <input name="pod_check" id="pod_check_id"  type="hidden">
                                    <div id="pod_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                    <script type="text/javascript">
                                        initAutocompleteWithFormClear("pod", "pod_choices", "", "pod_check",
                                        "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=1&isDojo=false", "");
                                    </script> --%>
                                    <input type="button" class="buttonStyleNew" value="Add" id="addDestination" onclick="addAgent('${user.userId}')"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <div id="agentInformation">
                                        <c:import url="../admin/AgentInformation.jsp"/>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

            <html:hidden property="modifyTest" styleId="modify"/>
            <html:hidden property="buttonValue" styleId="buttonValue" />
            <html:hidden property="login" styleId="loginUser" value="<%=login%>"/>
            <html:hidden property="userName" styleId="userName" value="<%=userName%>"/>
            <input type="hidden" name="match" value="${match}"/>
        </html:form>
    </body>

</html>



