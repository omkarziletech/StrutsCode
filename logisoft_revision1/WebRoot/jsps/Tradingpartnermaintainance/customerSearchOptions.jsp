<%-- 
    Document   : customerSearchOptions
    Created on : Jun 23, 2014, 3:33:30 PM
    Author     : venugopal.s
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div id="customerSearchOptions" class="customerSearchOptions" align="center">
    <table class="tableBorderNew" width="100%">
        <tr class="tableHeadingNew">
            <td colspan="2">Customer Search Options</td>
        </tr>
        <tr class="textlabelsBold">
            <td>State</td>
            <td>
        <html:text property="customerState" styleId="customerState" styleClass="textlabelsBoldForTextBox uppercase"/>
        </td>
        </tr>
        <tr class="textlabelsBold">
            <td>Country</td>
            <td>
        <html:text property="customerCountry" styleId="customerCountry" styleClass="textlabelsBoldForTextBox uppercase"/>
        <input id="country_check" type="hidden"/>
        <div id="country_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
        </td>
        </tr>
        <tr class="textlabelsBold">
            <td colspan="2" align="center">
                <input type="button" value="Submit" onclick="submitCustomerSearchOption();" class="buttonStyleNew"/>
                <input type="button" value="Clear" onclick="clearCustomerSearchOption();" class="buttonStyleNew"/>
            </td>
        </tr>
    </table>
</div>


