<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<html>
    <body>
        <cong:form  action="/lclAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="headerId" name="headerId"/>
            <cong:hidden id="filterByChanges" name="filterByChanges"/>
            <cong:hidden id="spReferenceName" name="spReferenceName"/>
            <cong:hidden id="departurePier" name="departurePier"/>
            <cong:hidden id="lrdOverrideDays" name="lrdOverrideDays"/>
            <cong:hidden id="std" name="std"/>
            <cong:hidden id="etaPod" name="etaPod"/>
            <cong:hidden id="accountName" name="accountName"/>
            <cong:hidden id="spReferenceNo" name="spReferenceNo"/>


            <cong:hidden name="changedFields" id="changedFields" value="${changedFields}"/>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td width="20%">Lcl Voyage Notification</td>
                </tr>
            </table>
            <br>
            <div style="margin-left:20%; width:40%;">
                <table border="0" style="width:30%;">
                    <tr>
                        <td><cong:checkbox styleClass="selected" name="shipper" id="shipper"/><span class="textlabelsBoldforlcl">Shipper</span></td>
                        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<cong:checkbox styleClass="selected" id="consignee" name="consignee"/><span class="textlabelsBoldforlcl">Consignee</span></td>
                    </tr>
                    <tr>
                        <td><cong:checkbox styleClass="selected" name="forwarder" id="forwarder"/><span class="textlabelsBoldforlcl">Forwarder</span></td>
                        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<cong:checkbox styleClass="selected" name="notify" id="notify"/><span class="textlabelsBoldforlcl">Notify</span></td>
                    </tr>
                    <tr>
                        <td><cong:checkbox styleClass="selected" name="bookingContact" id="bookingContact"/><span class="textlabelsBoldforlcl">BookingContact</span></td>
                        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<cong:checkbox styleClass="selected" name="internalEmployees" id="internalEmployees"/><label class="textlabelsBoldforlcl">Econo Internal Employees</label></td>
                    </tr>
                    <tr>
                        <td><cong:checkbox styleClass="selected" name="portAgent" id="portAgent"/><span class="textlabelsBoldforlcl">Port_Agent</span></td>
                    </tr>
                    <tr><td colspan="4"><br/></td></tr>
                    <tr>
                        <td class="textlabelsBoldforlcl">Reason Code</td>
                        <td colspan="3">
                            <html:select property="voyageReasonId" styleId="voyageReasonId" style="width:275px" styleClass="smallDropDown textlabelsBoldforlcl mandatory">
                                <html:option value="">Select Reason Codes</html:option>
                                <html:optionsCollection name="reasonCodeList"/>
                            </html:select></td>

                    </tr>
                    <tr>
                        <td class="textlabelsBoldforlcl">Comment</td>
                        <td colspan="3"><cong:textarea styleClass="refusedTextarea textlabelsBoldForTextBox"  name="voyageChangeReason" id="voyageChangeReason" rows="3" cols="30"/></td>
                    </tr>
                </table>
            </div>
            <table align="center">
                <tr><td></td>
                    <td><input type="button" class="button-style1" id="save" value="Send" onclick="sendNotification();"></td>
                    <td><input type="button" class="button-style1" id="clear" value="Cancel" onClick="cancel();"></td>
                </tr>
            </table>
        </cong:form>
    </body>
    <script type="text/javascript">
        function cancel() {
            parent.$.colorbox.close();
        }
        function sendNotification() {
            var notifiers = new Array();
            $('.selected').each(function () {
                var flag = $(this).attr('checked') ? true : false;
                if (flag) {
                    notifiers.push(this.name);
                }
            });
            if($("#voyageReasonId").val()===''){
                $.prompt("Please Select Reason Code");
                $("#voyageReasonId").css("border", "1px solid red");
                return false;
            }
            $("#accountName").val();
            $("#spReferenceName").val();
            $("#departurePier").val();
            $("#lrdOverrideDays").val();
            $("#std").val();
            $("#etaPod").val();
            $("#spReferenceNo").val();
            $("#changedFields").val();
            $("#headerId").val(parent.parent.$("#headerId").val());
            $("#notifiers").val(notifiers);
            $("#methodName").val("sendVoyageNotification");
            $("#lclAddVoyageForm").submit();
            cancel();
        }
    </script>
</html>
