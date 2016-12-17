<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/lclCorrection.js"></cong:javascript>
<cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="lclCorrection.do">
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
                <input type="button" value="Submit" id="submitButton" class="buttonStyleNew"  onclick="validatePassword()"/>
            </td>
        </tr>
    </table>
</cong:form>
