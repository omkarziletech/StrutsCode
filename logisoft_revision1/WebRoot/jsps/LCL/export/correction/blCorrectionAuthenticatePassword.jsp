<%-- 
    Document   : blCorrectionAuthenticatePassword
    Created on : Jan 11, 2016, 11:13:06 PM
    Author     : Wsware
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../../init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="../../colorBox.jsp" %>
<%@include file="../../../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="/blCorrection.do">
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="userPassword" name="userPassword"/>
            <cong:hidden id="correctionId" name="correctionId"/>
            <cong:hidden id="fileId" name="fileId"/>
            <cong:hidden id="blNo" name="blNo"/>
            <cong:hidden id="noticeNo" name="noticeNo" />
            <table cellpadding="2" cellspacing="2" border="0" width="100%" align="center">
                <tr class="tableHeadingNew">
                    <td colspan="4">
                        Correction Approved# <span class="fileNo" style="color:#0000FF">${lclCorrectionForm.blNo}</span>
                    </td>
                </tr>
                <tr>
                    <td class="caution display-hide" id="invalidPasswordLabel">
                        &nbsp; Invalid Password!
                    </td>
                </tr>
                <tr><td class="blueFontWithBold11px">
                        <c:out value="${lclCorrectionForm.blNo}"/>&nbsp; Correction Notice will be posted
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
                <tr class="textlabels" align="center">
                    <td>Enter Password</td>
                </tr>
                <tr>
                    <td align="center">
                        <input type="password" name="password" id="password"/>
                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <input type="button" value="Submit" id="submitButton" class="buttonStyleNew"  onclick="approveCorrection()"/>
                    </td>
                </tr>
            </table>
        </cong:form>
    </body>
</html>
<script type="text/javascript">
    function approveCorrection() {
        document.getElementById("submitButton").disabled = true;
        var password = document.lclCorrectionForm.password.value;
        if (password === "" || password === null) {
            document.getElementById("submitButton").disabled = false;
            sampleAlert("Please enter password");
            document.lclCorrectionForm.password.style.borderColor = "red";
            $("#warning").parent.show();
        } else if ($("#userPassword").val() !== password) {
            document.getElementById("submitButton").disabled = false;
            document.lclCorrectionForm.password.value = "";
            $("#invalidPasswordLabel").removeClass("display-hide");
            $("#invalidPasswordLabel").addClass("display-show");
        } else {
            showLoading();
            parent.$("#methodName").val('approveCorrections');
            parent.$("#correctionId").val($("#correctionId").val());
            parent.$("#fileId").val($("#fileId").val());
            parent.$("#buttonValue").val('SA');
            parent.document.getElementById("concatenatedBlNo").value = document.getElementById("blNo").value + "-" + document.getElementById("noticeNo").value;
            parent.document.getElementById("notesBlNo").value = "(" + document.getElementById("blNo").value + "-C-" + document.getElementById("noticeNo").value + ")";
            parent.$("#lclCorrectionForm").submit();
        }

    }
</script>