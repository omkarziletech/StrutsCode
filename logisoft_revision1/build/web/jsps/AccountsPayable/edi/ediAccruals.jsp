<%-- 
    Document   : ediAccruals
    Created on : May 17, 2012, 12:22:09 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<table width="100%" class="lightbox">
    <thead>
	<tr>
	    <th class="header">Accruals for EDI Invoice - ${invoice}</th>
	    <th>
		<a href="javascript: closePopUpDiv();" class="close">
		    <img alt="" src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
		</a>
	    </th>
	</tr>
    </thead>
</table>
<c:choose>
    <c:when test="${not empty accruals}">
	<div align="right" class="table-banner" style="padding-right: 15px;">
	    <div style="float:right">
		<div style="float:left">
		    <c:choose>
			<c:when test="${fn:length(accruals)>1}">
			    ${fn:length(accruals)} accruals displayed.
			</c:when>
			<c:otherwise>1 accrual displayed.</c:otherwise>
		    </c:choose>
		</div>
	    </div>
	</div>
	<div class="accruals-results">
	    <table width="100%" cellpadding="0" cellspacing="1" class="display-table" id="accruals">
		<thead>
		    <tr>
			<th>Bill of Lading</th>
			<th>Container Number</th>
			<th>Voyage Number</th>
			<th>Dock Receipt</th>
			<th>Reporting Date</th>
			<th>Amount</th>
			<th>Blue Screen Cost Code</th>
			<th>Cost Code</th>
			<th>Shipment Type</th>
			<th>Terminal</th>
			<th>GL Account</th>
			<th>Action</th>

		    </tr>
		</thead>
		<tbody>
		    <c:set var="zebra" value="odd"/>
		    <c:forEach var="accrual" items="${accruals}" varStatus="index">
			<tr class="${zebra}">
			    <td>
				<input type="text" class="textbox blNumber" value="${accrual.blNumber}" style="width: 100px" maxlength="40"/>
			    </td>
			    <td>
				<input type="text" class="textbox containerNumber" value="${accrual.containerNumber}" style="width: 100px" maxlength="1000"/>
			    </td>
			    <td>
				<input type="text" class="textbox voyageNumber" value="${accrual.voyageNumber}" style="width: 100px" maxlength="30"/>
			    </td>
			    <td>
				<input type="text" class="textbox dockReceipt" value="${accrual.dockReceipt}" style="width: 100px" maxlength="10"/>
			    </td>
			    <td>${accrual.reportingDate}</td>
			    <td>
				<input type="text" class="textbox amount" value="${fn:replace(accrual.amount,',','')}" style="width: 100px" maxlength="11"/>
			    </td>
			    <td>
				<input type="text" class="textbox readonly bluescreenCostCode" value="${accrual.bluescreenCostCode}" readonly/>
			    </td>
			    <td>
				<input type="text" class="textbox costCode" value="${accrual.costCode}" style="width: 100px" maxlength="20"/>
				<input type="hidden" class="costCodeCheck" value="${accrual.costCode}"/>
			    </td>
			    <td>
				<input type="text" class="textbox readonly shipmentType" value="${accrual.shipmentType}" readonly style="width: 50px"/>
			    </td>
			    <td>
				<input type="text" class="textbox readonly terminal" value="${str:lastSubString(accrual.glAccount,'-')}" readonly style="width: 30px" maxlength="2"/>
			    </td>
			    <td>
				<input type="text" class="textbox readonly glAccount" value="${accrual.glAccount}" readonly style="width: 120px"/>
			    </td>
			    <td>
				<input type="hidden" class="suffix" value=""/>
				<input type="hidden" class="account" value=""/>
				<img alt="Update Accrual" title="Update Accrual"
				     src="${path}/images/icons/update.png" onclick="updateAccrual(this,'${accrual.id}')"/>
				<img alt="Remove Accrual" title="Remove Accrual"
				     src="${path}/img/icons/remove.gif" onclick="removeAccrual(this,'${accrual.id}')"/>
			    </td>
			</tr>
			<c:choose>
			    <c:when test="${zebra=='odd'}">
				<c:set var="zebra" value="even"/>
			    </c:when>
			    <c:otherwise>
				<c:set var="zebra" value="odd"/>
			    </c:otherwise>
			</c:choose>
		    </c:forEach>
		</tbody>
	    </table>
	</div>
    </c:when>
    <c:otherwise>
	<div class="table-banner green" style="background-color: #D1E6EE;">No accruals found</div>
    </c:otherwise>
</c:choose>