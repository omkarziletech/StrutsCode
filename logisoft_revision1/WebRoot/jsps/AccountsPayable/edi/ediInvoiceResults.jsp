<%-- 
    Document   : ediInvoiceResults
    Created on : May 10, 2012, 6:34:09 PM
    Author     : Lakshmi Naryanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:choose>
    <c:when test="${not empty ediInvoiceForm.invoices}">
	<div align="right" class="table-banner" style="padding-right: 15px;">
	    <div style="float:right">
		<div style="float:left">
		    <c:choose>
			<c:when test="${ediInvoiceForm.totalRows>ediInvoiceForm.selectedRows}">
			    ${ediInvoiceForm.selectedRows} out of ${ediInvoiceForm.totalRows} invoices displayed.
			</c:when>
			<c:when test="${ediInvoiceForm.selectedRows>1}">${ediInvoiceForm.selectedRows} invoices displayed.</c:when>
			<c:otherwise>1 invoice displayed.</c:otherwise>
		    </c:choose>
		</div>
		<c:if test="${ediInvoiceForm.totalPages>1 && ediInvoiceForm.selectedPage>1}">
		    <a title="First page" href="javascript: gotoPage('1')">
			<img alt="First page" title="First page" src="${path}/images/first.png"/>
		    </a>
		    <a title="Previous page" href="javascript: gotoPage('${ediInvoiceForm.selectedPage-1}')">
			<img alt="Previous page" title="Previous page" src="${path}/images/prev.png"/>
		    </a>
		</c:if>
		<c:if test="${ediInvoiceForm.totalPages>1}">
		    <select id="selectedPageNo" class="dropdown" style="float:left;">
			<c:forEach begin="1" end="${ediInvoiceForm.totalPages}" var="selectedPage">
			    <c:choose>
				<c:when test="${ediInvoiceForm.selectedPage!=selectedPage}">
				    <option value="${selectedPage}">${selectedPage}</option>
				</c:when>
				<c:otherwise>
				    <option value="${selectedPage}" selected>${selectedPage}</option>
				</c:otherwise>
			    </c:choose>
			</c:forEach>
		    </select>
		    <a href="javascript: gotoSelectedPage()">
			<img alt="Goto Page" title="Goto Page" src="${path}/images/go.jpg"/>
		    </a>
		</c:if>
		<c:if test="${ediInvoiceForm.totalPages>ediInvoiceForm.selectedPage}">
		    <a title="Next page" href="javascript: gotoPage('${ediInvoiceForm.selectedPage+1}')">
			<img alt="Next page" title="Next page" src="${path}/images/next.png"/>
		    </a>
		    <a title="Last page" href="javascript: gotoPage('${ediInvoiceForm.totalPages}')">
			<img alt="Last page" title="Last page" src="${path}/images/last.png"/>
		    </a>
		</c:if>
	    </div>
	</div>
	<div class="search-results">
	    <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
		<thead>
		    <tr>
			<c:choose>
			    <c:when test="${ediInvoiceForm.orderBy=='desc'}"><c:set var="orderBy" value="asc"/></c:when>
			    <c:otherwise><c:set var="orderBy" value="desc"/></c:otherwise>
			</c:choose>
			<th><a href="javascript:doSort('vendor_name','${orderBy}')">Vendor Name</a></th>
			<th><a href="javascript:doSort('vendor_number','${orderBy}')">Vendor Number</a></th>
			<th><a href="javascript:doSort('invoice_number','${orderBy}')">Invoice Number</a></th>
			<th><a href="javascript:doSort('invoice_date','${orderBy}')">Invoice Date</a></th>
			<th><a href="javascript:doSort('invoice_amount','${orderBy}')">Invoice Amount</a></th>
			<th><a href="javascript:doSort('status','${orderBy}')">Status</a></th>
			<th>Action</th>
		    </tr>
		</thead>
		<tbody>
		    <c:set var="zebra" value="odd"/>
		    <c:forEach var="invoice" items="${ediInvoiceForm.invoices}">
			<tr class="${zebra}">
			    <td class="uppercase">
				<label id="vendorName${invoice.id}" title="${invoice.vendorName}">
				    <c:choose>
					<c:when test="${fn:length(invoice.vendorName)>20}">
					    ${fn:substring(invoice.vendorName,0,20)}...
					</c:when>
					<c:otherwise>${invoice.vendorName}</c:otherwise>
				    </c:choose>
				</label>
			    </td>
			    <td class="uppercase">
				<label id="vendorNumber${invoice.id}">${invoice.vendorNumber}</label>
				<img src="${path}/img/icons/edit.gif" alt="Change Vendor" onclick="changeVendor(this,'${invoice.id}')" align="top"/>
			    </td>
			    <td class="uppercase">${invoice.invoiceNumber}</td>
			    <td>${invoice.invoiceDate}</td>
			    <c:choose>
				<c:when test="${fn:startsWith(invoice.invoiceAmount,'-')}">
				    <td style="text-align:right;" class="red">(${fn:replace(invoice.invoiceAmount,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td style="text-align:right;">${invoice.invoiceAmount}</td>
				</c:otherwise>
			    </c:choose>
			    <td>
				<label id="status${invoice.id}">${fn:replace(invoice.status,'EDI ','')}</label>
			    </td>
			    <td>
				<img alt="View Edi File" title="View Edi File" src="${path}/images/xml.png" onclick="viewEdiFile('${invoice.logId}')"/>
				<img alt="Print Invoice" title="Print Invoice" src="${path}/img/icons/pdf.gif" onclick="printInvoice('${invoice.id}')"/>
				<img alt="Scan/Attach" title="Scan/Attach"
				     src="${path}/img/icons/upload.gif" onclick="uploadDocument('${invoice.id}','${invoice.invoiceNumber}')"/>
				<img alt="Notes" title="Notes" width="16px" height="16px"
				     src="${path}/images/notepad_yellow.png" onclick="showNotes('${invoice.id}','${invoice.invoiceNumber}')"/>
				<c:if test="${invoice.status!='EDI Posted To AP' && invoice.status!='EDI Duplicate' && invoice.status!='EDI Archive'}">
				    <img alt="Show Accruals" title="Show Accruals"
					 src="${path}/images/more_details.png" onclick="showAccruals('${invoice.id}')"/>
				    <img alt="Goto Accruals" title="Goto Accruals"
					 src="${path}/images/icons/forward.png" onclick="gotoAccruals('${invoice.id}')"/>
				</c:if>
				<c:if test="${invoice.status=='EDI Ready To Post' || invoice.status=='EDI Ready To Post / Fully Mapped'}">
				    <img alt="Post to AP" title="Post to AP" id="postToAp${invoice.id}"
					 src="${path}/images/mail_send.png" onclick="postToAp(this,'${invoice.id}')" style="width:18px;height:18px;"/>
				</c:if>
				<c:if test="${invoice.status!='EDI Posted To AP' && invoice.status!='EDI Archive'}">
				    <img alt="Archive the Invoice" title="Archive the Invoice"
					 src="${path}/images/trash.png" onclick="archiveInvoice(this,'${invoice.id}')"/>
				</c:if>
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
	<div class="table-banner green" style="background-color: #D1E6EE;">No Invoices found</div>
    </c:otherwise>
</c:choose>