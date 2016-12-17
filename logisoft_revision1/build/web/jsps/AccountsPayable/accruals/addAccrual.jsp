<%-- 
    Document   : addAccrual
    Created on : Aug 8, 2012, 7:26:41 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table class="table" style="margin: 1px;width: 598px;height: 198px;">
    <tr>
        <th colspan="4">
            <div class="float-left">Add Accrual</div>
            <div class="float-right">
                <a href="javascript: closeAccrual()">
                    <img alt="Close Accrual" src="${path}/images/icons/close.png"/>
                </a>
            </div>
        </th>
    </tr>
    <tr>
        <td class="label">Vendor Name</td>
        <td>
            <input type="text" class="textbox" name="newVendorName" id="newVendorName" maxlength="50"/>
            <input type="hidden" name="newVendorNameCheck" id="newVendorNameCheck"/>
        </td>
        <td class="label">Vendor Number</td>
        <td>
            <input type="text" class="textbox readonly" name="newVendorNumber" id="newVendorNumber" maxlength="10" readonly tabindex="-1"/>
        </td>
    </tr>
    <tr>
        <td class="label">Invoice Number</td>
        <td>
            <input type="text" class="textbox" name="newInvoiceNumber" id="newInvoiceNumber" maxlength="100"/>
        </td>
        <td class="label">B/L</td>
        <td>
            <input type="text" class="textbox readonly" name="newBlNumber" id="newBlNumber" readonly maxlength="100"/>
	    <input type="text" style="display: none" class="textbox" name="lclBlNo" id="lclBlNo" maxlength="100"/>
	    <input type="text" style="display: none" class="textbox" name="airBlNo" id="airBlNo" maxlength="100"/>
        </td>
    </tr>
    <tr>
        <td class="label">Voyage</td>
        <td>
            <input type="text" class="textbox readonly" name="newVoyageNumber" id="newVoyageNumber" readonly maxlength="100"/>
            <input type="text" style="display: none" class="textbox" name="lclVoyageNo" id="lclVoyageNo" maxlength="100" onchange="fillVoy();"/>
            <input type="text" style="display: none" class="textbox" name="airVoyageNo" id="airVoyageNo" maxlength="100"/>
            <input type="hidden" class="textbox" name="unitId" id="unitId" maxlength="100"/>
        </td>
        <td class="label">D/R</td>
        <td>
            <input type="text" class="textbox readonly" name="newDockReceipt" id="newDockReceipt" readonly maxlength="100"/>
            <input type="text" style="display: none" class="textbox" name="lclDrNo" id="lclDrNo" maxlength="100" onchange="fillDr();"/>
            <input type="text" style="display: none" class="textbox" name="airDrNo" id="airDrNo" maxlength="100"/>
            <input type="hidden" class="textbox" name="fileId" id="fileId" maxlength="100"/>
        </td>
    </tr>
    <tr>
        <td class="label">Cost Code</td>
        <td>
            <input type="text" class="textbox readonly" name="newCostCode" id="newCostCode" readonly maxlength="20"/>
            <input type="text" style="display: none" class="textbox" name="lclCost" id="lclCost" maxlength="20"/>
            <input type="hidden" name="newCostCodeCheck" id="newCostCodeCheck"/>
        </td>
        <td class="label">GL Account</td>
        <td>
            <input type="text" class="textbox readonly" name="newGlAccount" id="newGlAccount" readonly tabindex="-1"/>
        </td>
    </tr>
    <tr>
        <td class="label">Shipment Type</td>
        <td>
            <select name="newShipmentType" id="newShipmentType" class="dropdown" style="width: 125px" onchange="toggleByModule();">
                <option value="">Select</option>
		<option value="AIRE">AIRE</option>
		<option value="AIRI">AIRI</option>
		<option value="FCLE">FCLE</option>
		<option value="FCLI">FCLI</option>
		<option value="INLE">INLE</option>
		<option value="LCLE">LCLE</option>
		<option value="LCLI">LCLI</option>
            </select>
        </td>
        <td class="label">Terminal</td>
        <td>
            <input type="text" class="textbox readonly" name="newTerminal" id="newTerminal"
		   maxlength="3" readonly tabindex="-1" />
            <input type="hidden" name="newBluescreenCostCode" id="newBluescreenCostCode"/>
            <input type="hidden" name="newSuffix" id="newSuffix"/>
            <input type="hidden" name="newAccount" id="newAccount"/>
        </td>
    </tr>
    <tr>
        <td class="label">Accrual Amount</td>
        <td>
            <input type="text" class="textbox amount" name="newAmount" id="newAmount" maxlength="11"/>
        </td>
    </tr>
    <tr>
        <td colspan="4" class="align-center">
            <input type="button" value="Add" class="button" onclick="addAccrual()"/>
            <input type="button" value="Clear" class="button" onclick="clearAccrual()"/>
        </td>
    </tr>
</table>