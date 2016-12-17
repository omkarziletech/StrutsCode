<%-- 
    Document   : ACHSetUp
    Created on : Feb 11, 2010, 8:37:44 PM
    Author     : Lakshminarayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ include file="../includes/baseResources.jsp"%>
<%@ include file="../includes/resources.jsp"%>
<%@ include file="../includes/jspVariables.jsp"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ACH SetUp</title>
	<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery.preloader.js"></script>
	<script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
    </head>
    <body class="whitebackgrnd" onload="validateHostAddress()">
        <html:form name="achSetUpForm" type="com.logiware.form.AchSetUpForm" action="/achSetUp" scope="request" method="post" enctype="multipart/form-data">
            <c:if test="${!empty message && message=='setupCompleted'}">
                <script type="text/javascript">
                    parent.parent.GB_hide();
                </script>
            </c:if>
            <html:hidden property="bankId"/>
            <table width="100%" cellspacing="3" cellpadding="3" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td colspan="6">File Header</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Record Type</td>
                    <td><html:text property="fileHeaderRecordTypeCode" styleClass="textlabelsBoldForTextBox" maxlength="1" style="width:10px"/></td>
                    <td>Priority Code</td>
                    <td><html:text property="priorityCode" styleClass="textlabelsBoldForTextBox" maxlength="2" style="width:15px"/></td>
                    <td>Immediate Destination <br><font color="brown"> (Routing No from Bank setup)</font></td>
                    <td>
                        <html:hidden property="immediateDestination" styleClass="textlabelsBoldForTextBox"/>
                        <c:out value="${achSetUpForm.immediateDestination}"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Immediate Origin</td>
                    <td><html:text property="immediateOrigin" styleClass="textlabelsBoldForTextBox" maxlength="10" style="width:60px"/></td>
                    <td>File Id Modifier</td>
                    <td><html:text property="fileIdModifier" styleClass="textlabelsBoldForTextBox" maxlength="1" style="width:10px"/></td>
                    <td>Record Size</td>
                    <td><html:text property="recordSize" styleClass="textlabelsBoldForTextBox" maxlength="3" style="width:20px"/></td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Blocking Factor</td>
                    <td><html:text property="blockingFactor" styleClass="textlabelsBoldForTextBox" maxlength="2" style="width:15px"/></td>
                    <td>Format Code</td>
                    <td><html:text property="formatCode" styleClass="textlabelsBoldForTextBox" maxlength="1" style="width:10px"/></td>
                    <td>Immediate Destination name</td>
                    <td><html:text property="immediateDestinationName" styleClass="textlabelsBoldForTextBox" maxlength="23" style="width:100px"/></td>
                </tr>
                <tr class="tableHeadingNew">
                    <td colspan="6">Batch Header</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Record Type</td>
                    <td><html:text property="batchHeaderRecordTypeCode" styleClass="textlabelsBoldForTextBox" maxlength="1" style="width:10px"/></td>
                    <td>Service Class Code</td>
                    <td><html:text property="serviceClassCode" styleClass="textlabelsBoldForTextBox" maxlength="3" style="width:20px"/></td>
                    <td>Company Name</td>
                    <td><html:text property="companyName" styleClass="textlabelsBoldForTextBox" maxlength="16" style="width:100px"/></td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Company Identification</td>
                    <td><html:text property="companyIdentification" styleClass="textlabelsBoldForTextBox" maxlength="10" style="width:60px"/></td>
                    <td>Standard Entry Class</td>
                    <td><html:text property="standardEntryClass" styleClass="textlabelsBoldForTextBox" maxlength="3" style="width:20px"/></td>
                    <td>Company Entry Description</td>
                    <td><html:text property="companyEntryDescription" styleClass="textlabelsBoldForTextBox" maxlength="10" style="width:65px"/></td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Originator Status Code</td>
                    <td><html:text property="originatorStatusCode" styleClass="textlabelsBoldForTextBox" maxlength="1" style="width:10px"/></td>
                </tr>
                <tr class="tableHeadingNew">
                    <td colspan="6">Entry Detail</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Record Type</td>
                    <td><html:text property="entryDetailRecordTypeCode" styleClass="textlabelsBoldForTextBox" maxlength="1" style="width:10px"/></td>
                    <td>Transaction Code</td>
                    <td><html:text property="transactionCode" styleClass="textlabelsBoldForTextBox" maxlength="2" style="width:15px"/></td>
                    <td>Addenda Record Indicator</td>
                    <td><c:out value="${achSetUpForm.addendaRecordIndicator}"/></td>
                </tr>
                <tr class="tableHeadingNew">
                    <td colspan="6">FTP</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>
                        Host Address
                    </td>
                    <td>
                        <html:text property="ftpHost" styleClass="textlabelsBoldForTextBox" maxlength="30" style="width:100px" onblur="validateHostAddress()"/>
                    </td>
                    <td colspan="4">
                        <label id="hostFound" style="font: bold 12px Arial, Verdana, sans-serif;color: blue;"></label>
                        <label id="hostNotFound" style="font: bold 12px Arial, Verdana, sans-serif;color: red;"></label>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>UserName</td>
                    <td><html:text property="ftpUserName" styleClass="textlabelsBoldForTextBox" maxlength="30" style="width:100px"/></td>
                    <td>Password</td>
                    <td colspan="3"><html:password property="ftpPassword" styleClass="textlabelsBoldForTextBox" maxlength="30" style="width:100px"/></td>
                </tr>
                <tr class="textlabelsBold">
                    <td>Port</td>
                    <td><html:text property="ftpPort" styleClass="textlabelsBoldForTextBox" maxlength="4" style="width:100px"/></td>
                    <td>Directory</td>
                    <td colspan="3"><html:text property="ftpDirectory" styleClass="textlabelsBoldForTextBox" maxlength="150" style="width:100px"/></td>
                </tr>
                <tr class="tableHeadingNew">
                    <td colspan="6">Encryption</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>
                        Public Key
                    </td>
                    <td colspan="5">
                        <html:file property="publicKeyFile" style="width: 175px;"/>
                        <html:hidden property="havePublicKey"/>
                        <c:choose>
                            <c:when test="${!empty achSetUpForm && achSetUpForm.havePublicKey}">
                                <span style="font: bold 12px Arial, Verdana, sans-serif;color: blue;">Public Key uploaded already</span>
                            </c:when>
                            <c:otherwise>
                                <span style="font: bold 12px Arial, Verdana, sans-serif;color: red;">Public Key not yet uploaded</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr class="tableHeadingNew">
                    <td colspan="6">SSH</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>
                        Private Key
                    </td>
                    <td colspan="5">
                        <html:file property="sshPrivateKeyFile" style="width: 175px;"/>
                        <html:hidden property="hasSshPrivateKey"/>
                        <c:choose>
                            <c:when test="${!empty achSetUpForm && achSetUpForm.hasSshPrivateKey}">
                                <span style="font: bold 12px Arial, Verdana, sans-serif;color: blue;">SSH Private Key uploaded already</span>
                            </c:when>
                            <c:otherwise>
                                <span style="font: bold 12px Arial, Verdana, sans-serif;color: red;">SSH Private Key not yet uploaded</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td>
                        Passphrase
                    </td>
                    <td>
                        <html:password property="sshPassphrase" styleClass="textlabelsBoldForTextBox" maxlength="50" style="width:100px"/>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td colspan="6" align="center">
                        <input type="button" value="Save" class="buttonStyleNew" onclick="save()">
                        <span class="required">* </span>All Fields are required
                        <html:hidden property="buttonAction"/>
                    </td>
                </tr>
            </table>
        </html:form>
        <script type="text/javascript">
            function validateHostAddress() {
                ftpHost = document.achSetUpForm.ftpHost;
                if (ftpHost.value !== "") {
                    $.ajaxx({
                        data: {
                            className: "com.logiware.dwr.AchDwr",
                            methodName: "validateHostAddress",
                            param1: ftpHost.value
                        },
                        preloading: true,
                        success: function (data) {
                            if (data.indexOf("Unknown Host - ") > -1) {
                                $("#hostFound").html("");
                                $("#hostNotFound").html(data);
                                ftpHost.value = "";
                                ftpHost.focus();
                            } else {
                                $("#hostFound").html(data);
                                $("#hostNotFound").html("");
                            }
                        }
                    });
                }
            }

            function save() {
                if (trim(document.achSetUpForm.ftpHost.value) === "") {
                    alert("Please Enter Host Address");
                    document.achSetUpForm.ftpHost.focus();
                } else if (trim(document.achSetUpForm.ftpUserName.value) === "") {
                    alert("Please Enter User Name");
                    document.achSetUpForm.ftpUserName.focus();
                } else if (trim(document.achSetUpForm.ftpPassword.value) === "") {
                    alert("Please Enter Password");
                    document.achSetUpForm.ftpPassword.focus();
                } else {
                    document.achSetUpForm.buttonAction.value = "Save";
                    document.achSetUpForm.submit();
                }
            }
        </script>
    </body>
</html>
