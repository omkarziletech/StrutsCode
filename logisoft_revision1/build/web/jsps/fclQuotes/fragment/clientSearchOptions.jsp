<%-- 
    Document   : clientSearchOptions
    Created on : Jul 23, 2012, 1:23:57 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div id="clientSearchOptions" class="clientSearchOptions" align="center">
    <table class="tableBorderNew" width="100%">
	<tr class="tableHeadingNew">
	    <td colspan="2">Client Search Options</td>
	</tr>
	<tr class="textlabelsBold">
	    <td>Search By</td>
	    <td>
		<html:select property="searchClientBy" styleId="searchClientBy" styleClass="dropdown_accounting" style="width: 125px;">
		    <html:option value="Client">Client</html:option>
		    <html:option value="Contact">Contact</html:option>
		    <html:option value="Email">Email</html:option>
		</html:select>
	    </td>
	</tr>
	<tr class="textlabelsBold">
	    <td>State</td>
	    <td>
		<html:text property="clientState" styleId="clientState" styleClass="textlabelsBoldForTextBox uppercase"/>
	    </td>
	</tr>
	<tr class="textlabelsBold">
	    <td>Zip Code</td>
	    <td>
		<html:text property="clientZipCode" styleId="clientZipCode" styleClass="textlabelsBoldForTextBox uppercase"/>
	    </td>
	</tr>
	<tr class="textlabelsBold">
	    <td>Sales Code</td>
	    <td>
		<html:text property="clientSalesCode" styleId="clientSalesCode" styleClass="textlabelsBoldForTextBox uppercase"/>
	    </td>
	</tr>
	<tr class="textlabelsBold">
	    <td colspan="2">
		<html:checkbox property="displayClientOneLine" styleId="displayClientOneLine" value="true"/>
		Display Name and Address in one line
	    </td>
	</tr>
	<tr class="textlabelsBold">
	    <td colspan="2" align="center">
		<input type="button" value="Submit" onclick="closeClientSearchOption()" class="buttonStyleNew"/>
	    </td>
	</tr>
    </table>
</div>