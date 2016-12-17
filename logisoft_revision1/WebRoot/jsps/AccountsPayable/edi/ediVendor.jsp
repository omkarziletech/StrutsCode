<%-- 
    Document   : ediVendor
    Created on : May 14, 2012, 2:53:38 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table width="100%" class="lightbox">
    <thead>
	<tr>
	    <th colspan="3" class="header">Change Vendor</th>
	    <th>
		<a href="javascript: hideStaticPopUp();" class="close">
		    <img alt="" src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
		</a>
	    </th>
	</tr>
    </thead>
</table>
<table width="100%">
    <tbody>
	<tr>
	    <td>Vendor</td>
	    <td>
		<input type="text" name="newVendorName" id="newVendorName" class="textbox" style="text-transform: uppercase"/>
		<input type="hidden" name="newVendorNameCheck" id="newVendorNameCheck"/>
	    </td>
	    <td>
		<input type="text" name="newVendorNumber" id="newVendorNumber" class="textbox readonly" readonly/>
	    </td>
	    <td>
		<input type="button" class="button" value="Update" style="width:80px" onclick="updateVendor()"/>
	    </td>
	</tr>
    </tbody>
</table>