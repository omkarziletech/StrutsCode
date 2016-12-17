<%--
    Document   : emailTemplate
    Created on : Apr 19, 2016, 11:13:36 AM
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
        <div id="emailTemplate" style="display: none">
            <div>
                <font color="red" size="4"><b><c:out
                            value="${emailForm.fileNo}"/>
                    </b>
                </font>
                <table width="100%" class="tableBorderNew" style="border: 10px;"
                       border="0">
                    <tr class="tableHeadingNew">
                        <td colspan="3">
                            Compose Mail
                        </td>
                    </tr>
                    <c:if test="${module eq 'FCL' && importflag eq false &&
                                  (screenName eq 'BL' || screenName eq 'Booking' || screenName eq 'Quotation')}">
                        <tr>
                            <td class="textlabelsBold">FROM</td>
                            <td colspan="2">
                                <input type="text" name="fromEmailAddress" id="fromEmailAddress" size="79"
                                       class="textlabelsBoldForTextBox" />
                                <input type="checkbox" name="pullEmailUser" id="pullEmailUser"
                                       checked="checked"  title="Me" onclick="setLoginUserEmail()"/>
                                <input type="checkbox" name="pullEmailDocs" id="pullEmailDocs"
                                       title="From Docs" onclick="setFromDocsEmail('${billingTerminal}')"/>
                                <input type="checkbox" name="customerserviceemail" id="customerserviceemail"
                                       title="from customer service" onclick="setCustomerServiceEmail('${billingTerminal}')"/>
                            </td>
                        </tr>
                    </c:if>
                     <c:if test="${screenName eq 'LCLQuotation' || screenName eq 'LCLBooking' || screenName eq 'LCLBL' || 
                                   screenName eq 'LCLSSMaster' || screenName eq 'LCLUnits' || screenName eq 'LclCreditDebitNote'}">
                        <tr>
                            <td class="textlabelsBold">FROM</td>
                            <td colspan="2">
                                <input type="text" name="fromEmailAddress" id="fromEmailAddress" size="79"
                                       class="textlabelsBoldForTextBox" />
                                <input type="checkbox" name="pullEmailUser" id="pullEmailUser"
                                       checked="checked"  title="Me" onclick="setLoginUserEmail()"/>                          
                                <input type="checkbox" name="pullEmailDocs" id="pullEmailDocs"
                                       title="From Docs" onclick="setFromDocsEmail('${billingTerminal}')"/>
                                <input type="checkbox" name="customerserviceemail" id="customerserviceemail"
                                       title="from customer service" onclick="setCustomerServiceEmail('${billingTerminal}')"/>
                            </td>
                        </tr>
                    </c:if>        
                    <tr>
                        <td class="textlabelsBold" width="10%">
                            TO
                        </td>
                        <td>
                            <input type="text" name="toAddress" id="toAddress" size="79"
                                   class="textlabelsBoldForTextBox" value="${clientEmail}"/>
                    <c:if test="${screenName eq 'LCLUnits'}">
                        <span class="textlabelsBold">
                            Agent<img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="contactA"
                                      onclick="openVoyageAgentContact('${path}', parent.parent.$('#exportAgentAcctName').val(),
                                                      parent.parent.$('#exportAgentAcctNo').val(), 'EMAIL')"/>
                        </span>
                    </c:if>
                    <c:if test="${(not empty bookingId and bookingId!=null) or (not empty bolNo and bolNo!=null)}">
                        <span class="hotspot">
                            <html:checkbox property="pullEmail"  styleId="pullEmail" title="Pull email address from booking contact"
                                           onclick="getToEmailAddressByFcl('${bolNo}', '${fileNo}')"/></span>
                    </c:if>
                    <c:if test="${not empty bolNo and bolNo!=null}">
                        <span class="hotspot">
                            <html:checkbox property="contactsEmail"  styleId="contactsEmail" title="Code C Foreign Agents"
                                           onclick="getToEmailAddressByFcl('${bolNo}', '${fileNo}')"/></span>
                    </c:if>
                    </td>                   
                    <td>
                        <span id="releaseOrdrEmailDivCons" class="textlabelsBold" style="display:none;">
                            <html:checkbox styleId="releaseOrdrEmailCons" property="releaseOrdrEmailCons"
                                           onclick="releaseOrdrEmailCheckedforCons();"/>
                            Get Email address from House Consignee</span>
                    </td>                   
                    </tr>
                    <tr>
                        <td class="textlabelsBold">
                            CC
                        </td>
                        <td>
                            <input type="text" name="ccAddress" id="ccAddress" size="79"
                                   class="textlabelsBoldForTextBox"/>
                            <input type="checkbox"  id="ccMyEmail" title="me" onclick="getMyCCEmail();"/>
                        </td>
                        <td>
                            <span id="releaseOrdrEmailDivCfs" class="textlabelsBold" style="display:none;">
                                <html:checkbox styleId="releaseOrdrEmailCfs" property="releaseOrdrEmailCfs"
                                               onclick="releaseOrdrEmailCheckedforCfs();"/>
                                Get Email address from CFS Devanning</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="textlabelsBold">
                            BCC
                        </td>
                        <td>
                            <input type="text" name="bccAddress" id="bccAddress" size="79"
                                   class="textlabelsBoldForTextBox"/>
                    <c:if test="${param.filesToPrint eq 'confirmOnBoard' and not empty param.billingTerminal}">
                        <c:set var="query" value="select doc_dept_email as docDeptEmail"/>
                        <c:set var="query" value="${query} from terminal"/>
                        <c:set var="query" value="${query} where trmnum = '${fn:substringAfter(param.billingTerminal, '-')}'"/>
                        <c:set var="docDeptEmail" value="${dao:getUniqueResult(query)}"></c:set>
                        <input type="checkbox" id="docDeptEmail" value="${docDeptEmail}" onclick="populateDocDeptEmail(this)"
                               title="Get Email address from Booking Terminal's Document Dept"/>
                    </c:if>
                    </td>
                    <td>
                        <span id="releaseOrdrEmailDivNotify"  class="textlabelsBold" style="display:none;">
                            <html:checkbox styleId="releaseOrdrEmailNotify" property="releaseOrdrEmailNotify"
                                           onclick="releaseOrdrEmailCheckedforNotify();"/>
                            Get Email address from Notify Party</span>
                    </td>
                    </tr>
                    <tr>
                        <td class="textlabelsBold">
                            Subject
                        </td>
                        <td>
                            <input type="text" name="emailSubject" id="emailSubject" size="79"
                                   class="textlabelsBoldForTextBox" />
                        </td>
                        <td><span id="releaseOrdrEmailDiv2ndNotify"  class="textlabelsBold" style="display:none;">
                                <html:checkbox styleId="releaseOrdrEmailNotify2" property="releaseOrdrEmailNotify2"
                                               onclick="releaseOrdrEmailCheckedforNotify2();"/>
                                Get Email address from 2nd Notify Party</span></td>
                    </tr>
                    <tr>
                        <td class="textlabelsBold" valign="top">
                            Message
                        </td>
                        <td>

                            <script language="JavaScript" type="text/javascript">
                                var rte1 = new richTextEditor('emailComment');
                                rte1.toggleSrc = false;
                                rte1.build();
                            </script>
                            <%--
                            <html:textarea property="emailComment" cols="50" rows="7" styleId="emailComment"></html:textarea>
                            --%>
                        </td>
                        <td class="textlabelsBold" id="releaseOrdrEmailDivIpi" valign="top" style="display:none;">
                    <html:checkbox styleId="releaseOrdrEmailIpi" property="releaseOrdrEmailIpi"
                                   onclick="releaseOrdrEmailCheckedforIpi();"/>
                    Get Email address from IPI CFS
                    </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <input type="button" class="buttonStyleNew" value="OK" onClick="setEmailFormValues()"/>
                            <input type="button" value="Cancel" onclick="cancelEmail()"
                                   class="buttonStyleNew"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
