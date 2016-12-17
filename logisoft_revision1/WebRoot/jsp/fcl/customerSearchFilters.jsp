<%-- 
    Document   : customerSearchFilters
    Created on : Nov 4, 2014, 5:16:05 PM
    Author     : venugopal.s
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table class="table" style="margin: 1px;width: 350px;">
    <tr>
        <th colspan="2">
    <div class="float-left" id="searchValue"></div>
    <div class="float-right">
        <a href="javascript: hideSearchFilter()">
            <img alt="Close Search Options" src="${path}/images/icons/close.png"/>
        </a>
    </div>
</th>
</tr>
<tr class="textlabelsBold">
    <td>State</td>
    <td>
        <input type="text" name="customerState" id="customerState" class="textbox uppercase"/>
    </td>
</tr>
<tr class="textlabelsBold">
    <td>Country</td>
    <td>
        <input type="text" name="customerCountry" id="customerCountry" class="textbox uppercase"/>
        <input type="hidden" name="customerCountryCode" id="customerCountryCode"/>
        <input type="hidden" name="customerCountryCheck" id="customerCountryCheck"/>
    </td>
</tr>
<tr class="textlabelsBold">
    <td>Zip Code</td>
    <td>
        <input name="customerZipCode" id="customerZipCode" class="textbox uppercase"/>
    </td>
</tr>
<tr class="textlabelsBold">
    <td>Sales Code</td>
    <td>
        <input type="text" name="customerSalesCode" id="customerSalesCode" class="textbox uppercase"/>
    </td>
</tr>
<tr class="textlabelsBold">
    <td colspan="2" align="center">
        <input type="button" value="Submit" onclick="submitCustomerSearchFilter()" class="button"/>
        <input type="button" value="Clear" onclick="closeCustomerSearchFilter();" class="button"/>
        <input type="hidden"  id="searchValueBy" var="searchValueBy"/>               
    </td>
</tr>
</table>