<%@ page language="java" pageEncoding="ISO-8859-1" import="
         com.gp.cong.logisoft.bc.notes.NotesConstants,
         com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.GenericCode"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../includes/jspVariables.jsp" %>
<%    String path = request.getContextPath();

    DBUtil dbUtil = new DBUtil();
    List userList = new ArrayList();
    com.gp.cong.logisoft.domain.User user = new com.gp.cong.logisoft.domain.User();
    GenericCode genericCode = null;
    String countryId = "";
    String cityId = "";
    String state = "";
    String role = "0";
    String status = "";
    String terminalId = "0";
    String importterminalId = "0";
    String billingTerminalId = "0";
    String templateId = "0";
    if (session.getAttribute("view") != null) {
        session.removeAttribute("view");
    }
    if (session.getAttribute("user") != null) {
        user = (com.gp.cong.logisoft.domain.User) session.getAttribute("user");
        genericCode = user.getGenericCode();
        if (user != null && user.getTerminal() != null && user.getTerminal().getTrmnum() != null) {
            terminalId = user.getTerminal().getTrmnum();
        }
        if (user != null && user.getImportTerminal() != null && user.getImportTerminal().getTrmnum() != null) {
            importterminalId = user.getImportTerminal().getTrmnum();
        }
        if (user != null && user.getBillingTerminal() != null && user.getBillingTerminal().getTrmnum() != null) {
            billingTerminalId = user.getBillingTerminal().getTrmnum();
        }
        if (user != null && user.getUnLocation() != null && user.getUnLocation().getCountryId() != null && user.getUnLocation().getCountryId().getCodedesc() != null) {
            countryId = user.getUnLocation().getCountryId().getCodedesc();
        }
        if (user != null && user.getUnLocation() != null && user.getUnLocation().getStateId() != null && user.getUnLocation().getStateId().getCode() != null) {
            state = user.getUnLocation().getStateId().getCode();
        }
        if (user.getRole() != null && user.getRole().getRoleId() != null) {
            role = user.getRole().getRoleId().toString();
        }
        if (user.getStatus() != null) {
            status = user.getStatus();
        }
        if (user.getCity() != null && user.getCity() != "") {
            cityId = user.getCity();
        }
    }
    if (request.getAttribute("userList") != null) {
        userList = (List) request.getAttribute("userList");
    }

    List allTerminalList = dbUtil.getTerminalList();
    request.setAttribute("terminallist", allTerminalList);
    request.setAttribute("importterminallist", allTerminalList);
    request.setAttribute("billingTerminallist", allTerminalList);

    request.setAttribute("rolelist", dbUtil.getRoleList());
    request.setAttribute("statuslist", dbUtil.getStatusList());
    List templateList = dbUtil.getTemplateList();
    request.setAttribute("templateList", templateList);

    String msg = "";
    String messg = "";
    List cities = new ArrayList();
    if (request.getAttribute("message") != null) {
        msg = (String) request.getAttribute("message");

    }
    String modify = "1";
    session.setAttribute("printermodify", modify);
    if (genericCode != null) {
        cities = dbUtil.getCityList(genericCode);
    }
    request.setAttribute("citylist", cities);
%>

<html>
    <head>
        <title>New User Login Page</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <%@include file="../includes/resources.jsp" %>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/default/style.css">
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lable-fields.css"/>
        <%@include file="../../jsps/preloader.jsp"%>
        <script type="text/javascript" src="${path}/js/common.js" ></script>
        <script type="text/javascript" src="${path}/js/isValidEmail.js" ></script>
        <script type="text/javascript">
            jQuery(document).ready(function () {
                jQuery("[title != '']").not("link").tooltip();
            });
            function alertMsg(txtMsg, selector) {
                $.prompt(txtMsg, {
                    callback: function () {
                        jQuery("#" + selector).val("").callFocus();
                    }
                });
            }
            function saveUser() {
                var loginName = jQuery('#loginName').val();
                if (loginName === "") {
                    alertMsg("Please enter the Login Name", "loginName");
                    return;
                }
                if (loginName.match(" ")) {
                    $.prompt("Space is not allowed for Login Name");
                    return;
                }
                if (isSpecialExceptPeriod(loginName) == false) {
                    alertMsg("Special Characters not allowed for Login Name", "loginName");
                    return;
                }
                var firstName = jQuery('#firstName').val();
                if (firstName === "") {
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
                if (jQuery('#terminal').val() === "0") {
                    alertMsg("Please select the Terminal", "terminal");
                    return;
                }
                if (jQuery('#importTerminal').val() === "0") {
                    alertMsg("Please select the ImportTerminal", "importTerminal");
                    return;
                }
                if (jQuery('#billingTerminal').val() === "0") {
                    alertMsg("Please select the BillingTerminal", "billingTerminal");
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

                var password = jQuery('#password').val();
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
                if ("" != email && allValidChars(email) == false) {
                    alertMsg("Please enter proper Email", "email");
                    return;
                }
                var outsourceEmail = jQuery('#outsourceEmail').val();
                if ("" != outsourceEmail && allValidChars(outsourceEmail) == false) {
                    alertMsg("Please enter proper Outsource Email", "outsourceEmail");
                    return;
                }
                showLoading();
                document.newUserForm.buttonValue.value = "save";
                document.newUserForm.submit();
            }
            function selectcity() {
                if (document.newUserForm.country.value == "0") {
                    $.prompt("Please enter the country");
                    return;
                }
                showLoading();
                document.newUserForm.buttonValue.value = "selectcity";
                document.newUserForm.submit();
            }
            function selectstate()
            {
                if (document.newUserForm.city.value == "0")
                {
                    alert("Please enter the city");
                    return;
                }
                document.newUserForm.buttonValue.value = "selectstate";
                document.newUserForm.submit();
            }
            function submit1() {
                if (document.newUserForm.terminal.value == "0") {
                    $.prompt("Please select the Terminal");
                    return;
                }
                showLoading();
                document.newUserForm.buttonValue.value = "terminalselected";
                document.newUserForm.submit();
            }
            function submit2() {
                if (document.newUserForm.importTerminal.value == "0") {
                    $.prompt("Please select the ImportTerminal");
                    return;
                }
                showLoading();
                document.newUserForm.buttonValue.value = "importterminalselected";
                document.newUserForm.submit();
            }
            function submit3() {
                if (document.newUserForm.billingTerminal.value == "0") {
                    $.prompt("Please select the BillingTerminal");
                    return;
                }
                showLoading();
                document.newUserForm.buttonValue.value = "billingterminalselected";
                document.newUserForm.submit();
            }
            function cancel() {
                document.newUserForm.buttonValue.value = "cancel";
                document.newUserForm.submit();
            }
            var newwindow = '';
            function openPrinterList() {
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
                document.newUserForm.submit();
                return false;
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

                document.newUserForm.submit();
                return false;
            }


            function searchcity() {
                document.newUserForm.buttonValue.value = "searchcity";
                document.newUserForm.submit();
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
    <body class="whitebackgrnd"  >
        <div class="error">
            <c:if test="${not empty error}">
                <c:out value="${error}"/>
            </c:if>
        </div>
        <html:form action="/newUser" name="newUserForm" enctype="multipart/form-data"
                   type="com.gp.cong.logisoft.struts.form.NewUserForm" scope="request">
            <font color="blue"><h4><%=msg%></h4></font>
            <div align="right">
                <input type="button" class="buttonStyleNew" value="Save" id="search" onclick="saveUser()">
                <input type="button" class="buttonStyleNew" value="Add Printer" id="printer"
                       onclick="return GB_show('Printer', '<%=path%>/printer.do?printerList=' + 'add', 200, 600)"/>
                <input type="button" class="buttonStyleNew" value="Go Back" id="search" onclick="cancel()">
                <input type="button" class="buttonStyleNew" id="note" onclick="return GB_show('Notes', '<%=path%>/notes.do?moduleId=' + '<%=NotesConstants.USER%>', 300, 700);" id="note" name="search" value="Note"/>
            </div>

            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorder">
                <tr>
                    <td class="tableHeadingNew">
                        <bean:message key="form.newUserForm.enteruser" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%"  border="0">
                            <tr><td colspan="4" style="padding-top: 2px"></td></tr>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.loginName" />&nbsp;</td>
                                <td>
                                    <html:text property="loginName" readonly="true" styleId="loginName"
                                               value="${user.loginName}" styleClass="varysizeareahighlightgrey textlabelsBoldForTextBox width-200px"/></td>
                                <td>
                                    <html:checkbox property="achApprover" name="newUserForm" style="vertical-align: middle"/>
                                    <label id="labelText" class="align-left">&nbsp;ACH Approver</label>
                                </td>
                                <td>
                                    <input type="checkbox" name="searchScreenReset" title="If this is checked it will automatically  <br>reset the search screen for users"
                                           value="true" checked="true" style="vertical-align: middle"/>
                                    <label id="labelText" class="align-left">&nbsp;Search Screen Reset</label>
                                </td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 2px"></td></tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                                <td class="tableHeadingNew" colspan="7">
                                    <bean:message key="form.newUserForm.personal" />
                                </td>
                            </tr>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.firstName" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;
                                </td>
                                <td>
                                    <html:text property="firstName" styleId="firstName" styleClass="areahighlightyellow1 textlabelsBoldForTextBox width-200px"
                                    value="<%=user.getFirstName()%>"/>
                                </td>
                                <td id="labelText">
                                    <bean:message key="form.newUserForm.lastName" />
                                </td>
                                <td>
                                    <html:text property="lastName" styleId="lastName"
                                    styleClass="textlabelsBoldForTextBox width-145px" value="<%=user.getLastName()%>"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0"  >
                            <tr>
                                <td class="tableHeadingNew" colspan="5">
                                    <bean:message key="form.newUserForm.contact" />
                                </td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 2px"></td>
                                <td rowspan="16">
                                    <table class="table margin-none border-none">
                                        <tr>
                                            <td>
                                                <img src="data:image/png;base64,${user.signaturImg}"
                                                     width="200px" height="28px" id="images" alt="ImageIcon"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="label">
                                                    <input type="file" name="signatureImageOutput" onchange="readURL(this)"
                                                           accept="image/x-png, image/gif, image/jpeg" value="SignatureImage"/>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td id="labelText">
                                    <bean:message key="form.newUserForm.extension" />&nbsp;
                                </td>
                                <td>
                                    <html:text property="extension" styleId="extension"
                                    styleClass="textlabelsBoldForTextBox width-200px" value="<%=user.getExtension()%>"/>
                                </td>
                                <td id="labelText">
                                    <bean:message key="form.newUserForm.email" />&nbsp;
                                </td>
                                <td>
                                    <html:text property="email" styleId="email"
                                    styleClass="textlabelsBoldForTextBox width-200px" value="<%=user.getEmail()%>"/>
                                </td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 2px"></td></tr>
                            <tr>
                                <td  id="labelText"><bean:message key="form.newUserForm.terminal" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;
                                </td>
                                <td>
                                    <html:select property="terminal" styleId="terminal" styleClass="dropdown_accounting unfixedtextfiledstyle"
                                    onchange="submit1();" value="<%=terminalId%>">
                                        <html:option value="0">Select ExportTerminal</html:option>
                                        <html:optionsCollection name="terminallist"/>
                                    </html:select>
                                </td>
                                <td  id="labelText">
                                    <bean:message key="form.newUserForm.officeCityLocation" />&nbsp;
                                </td>
                                <td>
                                    <html:text property="officeCityLocation" styleClass="textlabelsBoldForTextBox width-200px"
                                    value="<%=user.getOfficeCityLOcation()%>" maxlength="5"/>
                                </td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 2px"></td></tr>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.importterminal" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;</td>
                                <td>
                                    <html:select property="importTerminal" styleId="importTerminal" styleClass="dropdown_accounting unfixedtextfiledstyle"
                                    onchange="submit2()" value="<%=importterminalId%>">
                                        <html:option value="0">Select ImportTerminal</html:option>
                                        <html:optionsCollection name="importterminallist"/>
                                    </html:select>
                                <td id="labelText">Outsource Email&nbsp;</td>
                                <td>
                                    <html:text property="outsourceEmail" styleId="outsourceEmail"
                                    styleClass="textlabelsBoldForTextBox width-200px" value="<%=user.getOutsourceEmail()%>"/>
                                </td>
                            </tr>

                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.billingTerminal" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;</td>
                                <td>
                                    <html:select property="billingTerminal" styleId="billingTerminal" styleClass="dropdown_accounting unfixedtextfiledstyle"
                                    onchange="submit3()" value="<%=billingTerminalId%>">
                                        <html:option value="0">Select BillingTerminal</html:option>
                                        <html:optionsCollection name="billingTerminallist"/>
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
                                <%
                                    String telephone = "";
                                    String fax = "";
                                    if (user.getTelephone() != null) {
                                        telephone = user.getTelephone();
                                    }
                                    if (user.getFax() != null) {
                                        fax = user.getFax();
                                    }
                                %>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.telephone" />&nbsp;</td>
                                <td><html:text property="telephone" value="<%=telephone%>" styleClass="textlabelsBoldForTextBox width-200px" /></td>
                                <td id="labelText"><bean:message key="form.newUserForm.fax" />&nbsp;</td>
                                <td><html:text property="fax" value="<%=fax%>" styleClass="textlabelsBoldForTextBox width-200px" /></td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 3px"></td></tr>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.address1" />&nbsp;</td>
                                <td><html:text property="address1" value="<%=user.getAddress1()%>" styleClass="textlabelsBoldForTextBox width-200px" /></td>
                                <td id="labelText"><bean:message key="form.newUserForm.address2" />&nbsp;</td>
                                <td><html:text property="address2" value="<%=user.getAddress2()%>" styleClass="textlabelsBoldForTextBox width-200px" /></td>
                            </tr>
                            <tr><td colspan="4" style="padding-top: 3px"></td></tr>
                            <tr>
                                <td id="labelText">City &nbsp;
                                    <img border="0" src="${path}/img/search1.gif" alt="citySearchIcon"
                                         onclick="return popup1('<%=path%>/jsps/datareference/searchpopup.jsp?button=' + 'searchusercity', 'windows')">
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
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                            <tr>
                                <td class="tableHeadingNew" colspan="6">
                                    <bean:message key="form.newUserForm.loginInfo" />
                                </td>
                            </tr>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.role" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;</td>
                                <td>
                                    <html:select property="role" styleId="roleId"
                                    styleClass="dropdown_accounting unfixedtextfiledstyle" value="<%=role%>">
                                        <html:optionsCollection name="rolelist"/>
                                    </html:select>
                                </td>
                                <td id="labelText"><bean:message key="form.newUserForm.status" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;</td>
                                <td><html:select property="status" styleId="statusId"
                                    styleClass="dropdown_accounting unfixedtextfiledstyle" value="<%=status%>">
                                        <html:optionsCollection name="statuslist"/>
                                    </html:select>
                                </td>
                                <td id="labelText">Warehouse</td>
                                <td id="labelText" class="align-left">
                                    <input type="radio" id="warehouseY" name="warehouse" value="true"/>Yes
                                    <input type="radio" id="warehouseN" name="warehouse" value="false" checked/>No
                                <td>
                            </tr>
                            <tr><td colspan="6" style="padding-top: 3px"></td></tr>
                            <tr>
                                <td id="labelText"><bean:message key="form.newUserForm.password" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;
                                </td>
                                <td>
                                    <html:password property="password" styleId="password"
                                                   styleClass="areahighlightyellow1 textlabelsBoldForTextBox width-145px"
                                    value="<%=user.getPassword()%>"/></td>
                                <td id="labelText"><bean:message key="form.newUserForm.repassword" />
                                    <label style="color: red;font-size: 20px;font-weight: bold">*</label>
                                    &nbsp;
                                </td>
                                <td>
                                    <html:password property="retypePassword" styleId="retypePassword"
                                                   styleClass="areahighlightyellow1 textlabelsBoldForTextBox width-145px"
                                    value="<%=user.getPassword()%>"/></td>
                                <td id="labelText">WarehouseNo</td>
                                <td>
                                    <html:text property="warehouseNo" styleId="warehouseNo"
                                               styleClass="textlabelsBoldForTextBox width-145px" maxlength="6"
                                    value="<%=user.getWarehouseNo()%>"/>   
                                </td>    

                            </tr>
                            <tr><td colspan="6" style="padding-top: 3px"></td></tr>
                            <tr>
                                <td id="labelText" colspan="2">
                                    Auto notify when Booked dims are different from Actual
                                </td>
                                <td id="labelText" class="align-left">
                                    <html:radio property="difflclBookedDimsActual" name="newUserForm" value="1"/>Yes
                                    <html:radio property="difflclBookedDimsActual" name="newUserForm" value="0"/>No
                                </td>
                                <td colspan="3"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue"/>
        </html:form>
    </body>
    <%--
    <%@include file="../includes/baseResourcesForJS.jsp" %>
    --%>
</html>
