<%-- 
    Document   : inactivateAccruals
    Created on : Sep 7, 2012, 2:37:18 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table class="table" style="margin: 1px;width: 598px;height: 138px;">
    <tr>
	<th colspan="4">
	    <div class="float-left">Inactivate Accruals</div>
	    <div class="float-right">
		<a href="javascript: hideInactivateAccruals()">
		    <img alt="Close Inactivate Accruals" src="${path}/images/icons/close.png"/>
		</a>
	    </div>
	</th>
    </tr>
    <tr>
	<td class="label">Inactivate/Activate By</td>
	<td class="label">
	    <select name="inactivateBy" id="inactivateBy" class=" textbox dropdown" onchange="onchangeInactivateBy(this)">
		<option value="dateRange">Date Range</option>
		<option value="amountRange">Amount Range</option>
	    </select>
	</td>
    </tr>
    <tr>
	<td class="label">From Date</td>
	<td>
	    <input type="text" class="textbox" name="fromDate" id="fromDate" maxlength="10" onchange="validateDate(this)"/>
	    <img src="${path}/img/CalendarIco.gif" alt="From Date" title="From Date" align="top" id="fromDateCalendar" class="calendar-img"/>
	</td>
	<td class="label">To Date</td>
	<td>
	    <input type="text" class="textbox" name="toDate" id="toDate" maxlength="10" onchange="validateDate(this)"/>
	    <img src="${path}/img/CalendarIco.gif" alt="To Date" title="To Date" align="top" id="toDateCalendar" class="calendar-img"/>
	</td>
    </tr>
    <tr>
	<td class="label">From Amount</td>
	<td>
	    <input type="text" class="textbox readonly amount" name="fromAmount" id="fromAmount" maxlength="11" value="-5.00" readonly tabindex="-1"/>
	</td>
	<td class="label">To Amount</td>
	<td>
	    <input type="text" class="textbox readonly amount" name="toAmount" id="toAmount" maxlength="11" value="1.00" readonly tabindex="-1"/>
	</td>
    </tr>
    <tr>
	<td class="label">Vendor Name</td>
	<td>
	    <input type="text" class="textbox" name="inactivateVendorName" id="inactivateVendorName" maxlength="50"/>
	    <input type="hidden" name="inactivateVendorNameCheck" id="inactivateVendorNameCheck"/>
	</td>
	<td class="label">Vendor Number</td>
	<td>
	    <input type="text" class="textbox readonly" name="inactivateVendorNumber" id="inactivateVendorNumber" maxlength="10" readonly tabindex="-1"/>
	</td>
    </tr>
    <tr>
	<td colspan="4" class="align-center">
	    <input type="button" value="Inactivate" class="button" onclick="inactivateAccruals()"/>
	    <input type="button" value="Activate" class="button" onclick="activateAccruals()"/>
	    <input type="button" value="Cancel" class="button" onclick="hideInactivateAccruals()"/>
	</td>
    </tr>
</table>