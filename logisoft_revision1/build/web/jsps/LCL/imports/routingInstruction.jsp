<%-- 
    Document   : routingInstruction
    Created on : May 9, 2014, 1:04:50 PM
    Author     : meiyazhakan.r
--%>

<%@include file="/jsps/LCL/init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@include file="/jsps/includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <script type="text/javascript">
        function savePPP() {
            showLoading();
            if($("#shipperHours").val()==='' && $("#cutOff").val()==='' &&
                $("#readyDate").val()==='' && $("#specialInstructions").val()==='' &&
                $("#termsOfSale").val()==='' && $("#readyNote").val()===''){
                parent.$("#routing").css('background','#8DB7D6');
                 parent.$("#routing").css('color','black');
                parent.$("#belowrouting").css('background','#8DB7D6');
                 parent.$("#belowrouting").css('color','black');
                }else{
                    parent.$("#routing").css('background','green');
                    parent.$("#routing").css('color','white');
                    parent.$("#belowrouting").css('background','green');
                    parent.$("#belowrouting").css('color','white');
                }
            $("#methodName").val("savePickUp");
            $("#importRoutingForm").submit();
            parent.$.colorbox.close();
        }
    </script>
    <body style="background:#ffffff">
        <cong:form id="importRoutingForm" name="importRoutingForm" action="importRouting.do">
            <input type="hidden" id="fileNo" name="fileNo"value="${importRoutingForm.fileNo}"/>
            <input type="hidden" id="fileId" name="fileId" value="${importRoutingForm.fileId}"/>
            <input type="hidden" id="methodName" name="methodName"/>
            <table style="width:99%;border: 1px solid #dcdcdc; height:79%;" border="0" >
                <tr class="tableHeadingNew">
                    <td colspan="4">
                        Routing Instruction# <span class="fileNo" style="color:#0000FF">${importRoutingForm.fileNo}</span>
                    </td>
                </tr>
                <tr>
                    <td class="textlabelsBoldforlcl">Shipper Hours</td>
                    <td><input type="text" id="shipperHours" name="shipperHours" class="textlabelsBoldForTextBox" value="${lclBookingPad.deliveryHours}"/></td>
                    <td class="textlabelsBoldforlcl">CutOff Date</td>
                    <td>
                        <cong:calendarNew styleClass="textlabelsBoldForTextBox" id="cutOff" name="cutOff"/>
                    </td>
                </tr>
                <tr>
                    <td class="textlabelsBoldforlcl">Ready Note</td>
                    <td><input type="text" id="readyNote" name="readyNote" class="textlabelsBoldForTextBox" value="${lclBookingPad.pickupReadyNote}"/></td>
                    <td class="textlabelsBoldforlcl">Ready Date</td>
                    <td>
                        <cong:calendarNew styleClass="textlabelsBoldForTextBox" id="readyDate" name="readyDate"/>
                    </td>
                </tr>
                <tr>
                    <td class="textlabelsBoldforlcl">Term of Sale</td>
                    <td><input type="text" id="termsOfSale" name="termsOfSale" class="textlabelsBoldForTextBox" value="${lclBookingPad.termsOfService}"/></td>
                </tr>
                <tr>
                    <td class="textlabelsBoldforlcl">Special Instructions</td>
                    <td colspan="3">
                        <cong:textarea rows="3" name="specialInstructions" id="specialInstructions" styleClass="textlabelsBoldForTextBox" 
                                       style="width:85%;text-transform: uppercase" value="${lclBookingPad.pickUpTo}"/>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td colspan="3">
                        <input type="button" value="Save" class="button-style1" id="savePickup" onclick="savePPP();"/>
                    </td>
                </tr>
            </table>
        </cong:form>
    </body>

</html>
