<%-- 
    Document   : faxTemplate
    Created on : Apr 19, 2016, 11:13:14 AM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div id="faxTemplate" style="display: none;width: 100%;">
            <div>
                <table align="center" id="mainTable" width="100%" border="0"
                       cellpadding="0" cellspacing="0" class="tableBorderNew">
                    <tr class="tableHeadingNew">
                        <td colspan="2">
                            Fax
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table align="center" id="subTable" width="60%" border="0"
                                   cellpadding="0" cellspacing="1">
                                <tr>
                                    <td class="textlabelsBold">
                                        To
                                    </td>
                                    <td>
                                <html:text property="toName" size="50" styleId="toName"
                                           styleClass="textlabelsBoldForTextBox"></html:text>
                                <c:if test="${screenName eq 'LCLUnits'}">
                                    <span class="textlabelsBold">
                                        Agent<img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="contactA"
                                                  onclick="openVoyageAgentContact('${path}', parent.parent.$('#exportAgentAcctName').val(), 
                                                              parent.parent.$('#exportAgentAcctNo').val(), 'FAX')"/>
                                    </span></c:if>
                        </td>
                        <td>
                            <span id="releaseOrdrFaxDivCons" class="textlabelsBold" style="display:none;">
                                <html:checkbox styleId="releaseOrdrFaxCons" property="releaseOrdrFaxCons"
                                               onclick="releaseOrdrFaxCheckedforCons();"/>
                                Get Fax number from House Consignee</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="textlabelsBold">
                            Fax Number
                        </td>
                        <td>
                    <html:text property="toFaxNumber" size="50" styleId="toFaxNumber"
                               styleClass="textlabelsBoldForTextBox" onchange="validateToFaxNumber(this);"></html:text>
                    </td>
                    <td><span id="releaseOrdrFaxDivCfs" class="textlabelsBold" style="display:none;">
                            <html:checkbox styleId="releaseOrdrFaxCfs" property="releaseOrdrFaxCfs"
                                           onclick="releaseOrdrFaxCheckedforCfs();"/>
                            Get Fax number from Devanning CFS</span></td>
                    </tr>
                    <tr>
                        <td class="textlabelsBold">
                            From
                        </td>
                        <td>
                    <html:text property="fromName" size="50" styleId="fromName"
                               styleClass="textlabelsBoldForTextBox" value="${concatedUserName}"></html:text>
                    </td>
                    <td><span id="releaseOrdrFaxDivNotify" class="textlabelsBold" style="display:none;">
                            <html:checkbox styleId="releaseOrdrFaxNotify" property="releaseOrdrFaxNotify"
                                           onclick="releaseOrdrFaxCheckedforNotify();"/>
                            Get Fax number from Notify Party</span></td>
                    </tr>
                    <tr>
                        <td class="textlabelsBold">
                            Fax Number
                        </td>
                        <td>
                    <html:text property="fromFaxNumber" size="50" value="${loginuser.fax}" styleId="fromFaxNumber"
                               styleClass="textlabelsBoldForTextBox" onchange="validateFromFaxNumber(this);"></html:text>
                    </td>
                    <td><span id="releaseOrdrFaxDivNotify2" class="textlabelsBold" style="display:none;">
                            <html:checkbox styleId="releaseOrdrFaxNotify2" property="releaseOrdrFaxNotify2"
                                           onclick="releaseOrdrFaxCheckedforNotify2();"/>
                            Get Fax number from 2nd Notify Party</span></td>
                    </tr>
                    <tr>
                        <td class="textlabelsBold">
                            Business Phone
                        </td>
                        <td>
                    <html:text property="businessPhone" size="50" value="${loginuser.telephone}" styleId="businessPhone"
                               styleClass="textlabelsBoldForTextBox" onchange="validatePhoneNumber(this);"></html:text>
                    </td>
                    <td><span id="releaseOrdrFaxDivIpi" class="textlabelsBold" style="display:none;">
                            <html:checkbox styleId="releaseOrdrFaxIpi" property="releaseOrdrFaxIpi"
                                           onclick="releaseOrdrFaxCheckedforIpi();"/>
                            Get Fax number from IPI CFS</span></td>
                    </tr>
                    <tr>
                        <td class="textlabelsBold">
                            Home Phone
                        </td>
                        <td>
                    <html:text property="homePhone" size="50" styleId="homePhone"
                               styleClass="textlabelsBoldForTextBox" onchange="validatePhoneNumber(this);"></html:text>
                    </td>
                    </tr>
                    <tr>
                        <td class="textlabelsBold">
                            Subject
                        </td>
                        <td>
                    <html:text property="subject" size="50" styleId="subject"
                               styleClass="textlabelsBoldForTextBox"></html:text>
                    </td>
                    </tr>
                    <tr>
                        <td class="textlabelsBold">
                            Message
                        </td>
                        <td>
                    <html:textarea property="message" cols="50" rows="7" styleId="message"></html:textarea>
                    </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <table width="100%" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td align="center">
                                        <input type="button" class="buttonStyleNew" value="OK" onClick="setFaxFormValues()"/>
                                        <input type="button" value="Cancel" onclick="cancelFax()"
                                               class="buttonStyleNew"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                </td>
                </tr>

                </table>
            </div>
        </div>
    </body>
</html>
