<%-- 
    Document   : lineItems
    Created on : Jun 21, 2012, 3:33:54 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<table class="table">
    <thead>
	<tr>
	    <th colspan="7">Line Items</th>
	</tr>
    </thead>
    <tbody style="vertical-align: top">
	<c:if test="${apInvoiceForm.mode!='view'}">
	    <tr>
		<td class="label width-150px align-right">GL Account</td>
		<td>
		    <input type="text" name="lineItem.glAccount" id="glAccount" value="${apInvoiceForm.lineItem.glAccount}" class="textbox"/>
		    <input type="hidden" id="glAccountCheck"  value="${apInvoiceForm.lineItem.glAccount}" class="hidden"/>
		</td>
		<td class="label width-150px align-right">Description</td>
		<td>
		    <textarea name="lineItem.description"
			      id="description" rows="2" cols="30" tabindex="-1" class="textbox">${apInvoiceForm.lineItem.description}</textarea>
		</td>
		<td class="label width-150px align-right">Amount</td>
		<td>
		    <input type="text" name="lineItem.amount" id="amount" value="${apInvoiceForm.lineItem.amount}" class="textbox amount" maxlength="13"/>
		</td>
		<td>
		    <img src="${path}/img/icons/add2.gif" border="0" alt="Add Line Item" title="Add Line Item" onclick="addLineItem()"/>
		</td>
	    </tr>
	</c:if>
	<tr>
	    <td colspan="4">&nbsp;</td>
	    <td colspan="2">
		<div class="table-banner width-100pc float-left" style="background-color: rgb(224, 221, 228);">
		    <div style="font-size: 16px;font-weight: bold;color: rgb(29, 73, 75);" class="float-left align-right width-155px">
			Total Amount : 
		    </div> 
		    <c:choose>
			<c:when test="${fn:contains(apInvoiceForm.invoiceAmount,'(')}">
			    <div style="font-size: 13px;color: rgb(233, 47, 12);" class="float-left width-125px amount">
				$${apInvoiceForm.invoiceAmount}
			    </div>
			</c:when>
			<c:otherwise>
			    <div style="font-size: 13px;color: rgb(29, 73, 75);float:left;width:125px;" class="amount">
				$${apInvoiceForm.invoiceAmount}
			    </div>
			</c:otherwise>
		    </c:choose>
		</div>
	    </td>
	    <td>&nbsp;</td>
	</tr>
	<tr>
	    <td colspan="7">
		<div id="line-items">
		    <table width="100%" cellpadding="0" cellspacing="1" class="display-table" id="line-item-table">
			<thead>
			    <tr>
				<th>GL Account</th>
				<th>Description</th>
				<th>Amount</th>
				<c:if test="${apInvoiceForm.mode!='view'}">
				    <th>Actions</th>
				</c:if>
			    </tr>
			</thead>
			<tbody id="line-item-tbody">
			    <c:choose>
				<c:when test="${not empty apInvoiceForm.lineItems}">
				    <c:set var="zebra" value="odd"/>
				    <c:forEach var="lineItem" items="${apInvoiceForm.lineItems}">
					<tr class="${zebra}">
					    <td class="align-center">${lineItem.glAccount}</td>
					    <td class="uppercase">${lineItem.description}</td>
					    <c:choose>
						<c:when test="${fn:contains(lineItem.amount,'-')}">
						    <td class="red amount">(${fn:replace(lineItem.amount,'-','')})</td>
						</c:when>
						<c:otherwise>
						    <td class="black amount">${lineItem.amount}</td>
						</c:otherwise>
					    </c:choose>
					    <c:if test="${apInvoiceForm.mode!='view'}">
						<td class="align-center">
						    <img src="${path}/img/icons/remove.gif" alt="Remove Item" 
							 title="Remove Item" onclick="removeLineItem('${lineItem.id}')"/>
						</td>
					    </c:if>
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
				</c:when>
				<c:otherwise>
				    <tr>
					<c:set var="colspan" value="3"/>
					<c:if test="${apInvoiceForm.mode!='view'}">
					    <c:set var="colspan" value="4"/>
					</c:if>
					<td colspan="${colspan}">
					    <div class="table-banner green" style="background-color: #D1E6EE;">No Line Item found</div>
					</td> 
				    </tr>
				</c:otherwise>
			    </c:choose>
			</tbody>
		    </table>
		</div>
	    </td>
	</tr>
    </tbody>
</table>
