<%-- 
    Document   : exportNotificationPopUp
    Created on : Mar 18, 2015, 11:02:12 AM
    Author     : aravindhan.v
--%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="init.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/lclExportNotification.js"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Export Notification</title>
    </head>
    <body>
        <html:form action="/exportNotification" styleId="lclExportNotiFicationForm">
            <input type="hidden" id="buttonValue" name="buttonValue"/>
            <input type="hidden" id="methodName" name="methodName"/>
            <table width="99%" border="0" cellpadding="1" cellspacing="1"  align="center">
                <tr class="tableHeadingNew"><td>Voyage Notification Details</td></tr>
            </table>
            <br/>
            <span id="blueBold" style="font-size: 14px">${confirmMessage}</span>
            <br/><br/>
            <div style="height:40%; margin-left:10%;">
                <table width="99%" border="0" cellpadding="1" cellspacing="1">
                    <tr>
                        <td class="style2">Voyage No&nbsp;&nbsp;&nbsp;&nbsp;
                            <cong:autocompletor id="voyageNo" name="voyageNo" template="one" fields="headerId" query="VOYAGE_SCHEDULE_NO"
                                                styleClass="mandatory" width="100" container="NULL" params='E' shouldMatch="true"
                                                callback="getunitNo()" value="${exportNotiFicationForm.voyageNo}" />
                            <input type="hidden" id="headerId" name="headerId"/></td>
                        <td class="style2">Unit No
                            <cong:autocompletor id="unitNo" name="unitNo" template="one"  fields="unitId"  query="UNIT_NO" paramFields="voyageNoHiddenId"
                                                callback=""   width="100" container="NULL" shouldMatch="true"
                                                value="${exportNotiFicationForm.unitNo}" /></td>
                        <td><input type="hidden" id="unitId" name="unitId"/>
                            <input type="hidden" id="voyageNoHiddenId" name="voyageNoHiddenId"/>
                        </td>
                    </tr>
                    <tr><td><br/></td></tr>
                    <tr>
                        <td class="style2">Reason Code
                            <html:select property="voyageReasonId" styleId="voyageReasonId" style="width:250px" styleClass="smallDropDown textlabelsBoldforlcl mandatory">
                                <html:option value="">Select Reason Codes</html:option>
                                <html:optionsCollection name="reasonCodeList"/>
                            </html:select></td>
                    </tr>
                    <tr><td><br/></td></tr>
                    <tr><td class="style2">Message</td><td class="style2">Title</td></tr>
                    <tr><td>
                            <cong:textarea name="remarks" id="remarks" cols="5" rows="20" style="height:100px;"
                                           styleClass="refusedTextarea textlabelsBoldForTextBox" />
                        </td>
                        <td>
                            <cong:textarea name="voyageReason" id="voyageReason" cols="5" rows="20" style="height:100px;"
                                           styleClass="refusedTextarea textlabelsBoldForTextBox" />
                        </td>
                    </tr>
                    <tr><td><br/></td></tr>
                    <tr>
                        <td><input type="button" style="margin-left:35%;" class="button-style1" id="send" value="Send" onclick="sendNotification($('#headerId').val(), 'sendNotificationReport');"/>
                            <input type="button"  style="margin-left:5%;" class="button-style1" id="preview" value="Preview" onclick="sendDistribution($('#headerId').val(), 'sendDistributionList');"/>
                            <input type="button"  style="margin-left:5%;" class="button-style1" id="cancel" value="Cancel" onclick="erasePopup();"/>
                        </td>
                    </tr>
                </table>
            </div>
        </html:form>
    </body>
</html>
