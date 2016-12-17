<%-- 
    Document   : updateAccrual
    Created on : Sep 18, 2012, 7:26:41 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table class="table" style="margin: 1px;width: 598px;height: 198px;">
    <tr>
	<th colspan="4">
	    <div class="float-left">Update Accrual</div>
	    <div class="float-right">
		<a href="javascript: closeUpdateAccrual()">
		    <img alt="Close Update Accrual" src="${path}/images/icons/close.png"/>
		</a>
	    </div>
	</th>
    </tr>
    <tr>
	<td class="label">Vendor Name</td>
	<td>
	    <input type="text" class="textbox readonly" name="updateVendorName"
		   id="updateVendorName" readonly tabindex="-1" value="${accrualsForm.updateVendorName}"/>
	</td>
	<td class="label">Vendor Number</td>
	<td>
	    <input type="text" class="textbox readonly" name="updateVendorNumber"
		   id="updateVendorNumber" readonly tabindex="-1" value="${accrualsForm.updateVendorNumber}"/>
	</td>
    </tr>
    <tr>
	<td class="label">Cost Code</td>
	<td>
	    <input type="text" class="textbox" name="updateCostCode" id="updateCostCode" maxlength="20" value="${accrualsForm.updateCostCode}"/>
	    <input type="hidden" name="updateCostCodeCheck" id="updateCostCodeCheck" value="${accrualsForm.updateCostCode}"/>
	</td>
	<td class="label">GL Account</td>
	<td>
	    <input type="text" class="textbox readonly" name="updateGlAccount"
		   id="updateGlAccount" readonly tabindex="-1" value="${accrualsForm.updateGlAccount}"/>
	</td>
    </tr>
    <tr>
	<td class="label">Shipment Type</td>
	<td>
	    <input type="text" class="textbox readonly" name="updateShipmentType"
		   id="updateShipmentType" readonly tabindex="-1" value="${accrualsForm.updateShipmentType}"/>
	</td>
	<td class="label">Terminal</td>
	<td>
	    <input type="text" class="textbox readonly" name="updateTerminal" id="updateTerminal"
		   maxlength="3" readonly tabindex="-1" onchange="deriveUpdateGlAccount()" value="${accrualsForm.updateTerminal}"/>
	    <input type="hidden" name="updateBluescreenCostCode" id="updateBluescreenCostCode" value="${accrualsForm.updateBluescreenCostCode}"/>
	    <input type="hidden" name="updateSuffix" id="updateSuffix"/>
	    <input type="hidden" name="updateAccount" id="updateAccount"/>
	</td>
    </tr>
    <tr>
	<td class="label">Accrual Amount</td>
	<td>
	    <input type="text" class="textbox amount" name="updateAmount" id="updateAmount" maxlength="11" value="${accrualsForm.updateAmount}"/>
	</td>
	<td class="label">Leave Balance</td>
	<td>
	    <input type="checkbox"  name="leaveBalance" id="leaveBalance" title="Leave Balance"/>
	    <input type="hidden"  name="updateInvoiceNumber" id="updateInvoiceNumber" value="${accrualsForm.updateInvoiceNumber}"/>
	</td>
    </tr>
    <tr>
	<td colspan="4" class="align-center">
	    <input type="button" value="Update" class="button" onclick="updateAccrual()"/>
	</td>
    </tr>
</table>