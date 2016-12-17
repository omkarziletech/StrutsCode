<%-- 
    Document   : changeByOptions
    Created on : Apr 25, 2016, 11:29:49 AM
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
        <table width="99%" border="0" cellpadding="1" cellspacing="1"  align="center">
            <tr class="tableHeadingNew"><td>Voyage Notification Details</td></tr>
        </table>
        <div style="margin-left:10%; width:40%;">
            <table border="0" style="width:30%;">
                <tr>
                    <td class="textlabelsBoldforlcl">Comment</td>
                    <td colspan="3"><cong:textarea styleClass="refusedTextarea textlabelsBoldForTextBox"
                                               name="voyageChangeReason" id="voyageChangeReason" rows="3" cols="30"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><cong:checkbox styleClass="selected" name="customerEmployee" id="customerEmployee" container="NULL"/>
                <span class="textlabelsBoldforlcl">Customers</span></td>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<cong:checkbox styleClass="selected" id="internalEmployees"  container="NULL" name="internalEmployees" disabled="true"/>
                <span class="textlabelsBoldforlcl">Econo Internal Employees</span></td>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<cong:checkbox styleClass="selected"  container="NULL" id="billingTerminal" name="billingTerminal"/>
                <span class="textlabelsBoldforlcl">Billing Terminal</span></td>
                </tr>
                <tr>
                    <td class="textlabelsBoldforlcl">Choose Documents</td>
                    <td><cong:checkbox styleClass="selectedDoc" name="bookingPdf" id="bookingPdf"  container="NULL"/>
                <span class="textlabelsBoldforlcl">Booking</span></td>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<cong:checkbox styleClass="selectedDoc" id="nonRatedBl" name="nonRatedBl"  container="NULL"/>
                <span class="textlabelsBoldforlcl">Non-Rated B/L</span></td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input type="button" class="button-style3" value="Submit" onclick="validateOption()"/>
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>
