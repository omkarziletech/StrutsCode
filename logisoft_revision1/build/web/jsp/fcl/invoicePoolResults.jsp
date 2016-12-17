<%-- 
    Document   : invoicePoolResults
    Created on : Mar 08, 2013, 5:48:19 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${not empty invoicePoolForm.results}">
	<div id="result-header" class="table-banner green">
	    <div class="float-right">
		<div class="float-left">
		    <c:choose>
			<c:when test="${invoicePoolForm.totalRows>invoicePoolForm.selectedRows}">
			    ${invoicePoolForm.selectedRows} invoices displayed. ${invoicePoolForm.totalRows} invoices found.
			</c:when>
			<c:when test="${invoicePoolForm.selectedRows>1}">${invoicePoolForm.selectedRows} invoices found.</c:when>
			<c:otherwise>1 invoice found.</c:otherwise>
		    </c:choose>
		</div>
		<c:if test="${invoicePoolForm.totalPages>1 && invoicePoolForm.selectedPage>1}">
		    <a title="First page" href="javascript: gotoPage('1')">
			<img alt="First page" title="First page" src="${path}/images/first.png"/>
		    </a>
		    <a title="Previous page" href="javascript: gotoPage('${invoicePoolForm.selectedPage-1}')">
			<img alt="Previous page" title="Previous page" src="${path}/images/prev.png"/>
		    </a>
		</c:if>
		<c:if test="${invoicePoolForm.totalPages>1}">
		    <select id="selectedPageNo" class="dropdown float-left">
			<c:forEach begin="1" end="${invoicePoolForm.totalPages}" var="selectedPage">
			    <c:choose>
				<c:when test="${invoicePoolForm.selectedPage!=selectedPage}">
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
		<c:if test="${invoicePoolForm.totalPages>invoicePoolForm.selectedPage}">
		    <a title="Next page" href="javascript: gotoPage('${invoicePoolForm.selectedPage+1}')">
			<img alt="Next Page" title="Next page" src="${path}/images/next.png"/>
		    </a>
		    <a title="Last page" href="javascript: gotoPage('${invoicePoolForm.totalPages}')">
			<img alt="Last Page" title="Last page" src="${path}/images/last.png"/>
		    </a>
		</c:if>
	    </div>
	</div>
	<div class="result-container">
	    <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
		<thead>
		    <tr>
			<th><a href="javascript:doSort('invoice_number')">Invoice Number</a></th>
			<th><a href="javascript:doSort('file_no')">File Number</a></th>
			<th><a href="javascript:doSort('date')">Created Date</a></th>
			<c:if test="${invoicePoolForm.status != 'Un Posted'}">
			    <th><a href="javascript:doSort('posted_date')">Posted Date</a></th>
			</c:if>
			<th><a href="javascript:doSort('invoice_amount')">Amount</a></th>
			<th><a href="javascript:doSort('customer_name')">Bill To Party</a></th>
			<th><a href="javascript:doSort('invoice_by')">User</a></th>
			<th class="width-400px"><a href="javascript:doSort('notes')">Description</a></th>
			<th><a href="javascript:doSort('status')">Status</a></th>
			<th>Action</th>
		    </tr>
		</thead>
		<tbody>
		    <c:set var="zebra" value="odd"/>
		    <c:forEach var="invoice" items="${invoicePoolForm.results}">
			<tr class="${zebra}">
			    <td>${invoice.invoiceNumber}</td>
			    <td>
				<a href="javascript:void(0);" onclick="openFile('${invoice.fileNumber}');">
				    ${invoice.fileNumber}
				</a>
			    </td>
			    <td>${invoice.createdDate}</td>
			    <c:if test="${invoicePoolForm.status != 'Un Posted'}">
				<td>${invoice.postedDate}</td>
			    </c:if>
			    <td class="align-right">${invoice.invoiceAmount}</td>
			    <td>${invoice.billToParty}</td>
			    <td>${invoice.user}</td>
			    <td class="pre-wrap width-400px">${str:blankSpaceWord(invoice.description,70,' ')}</td>
			    <td>${invoice.status}</td>
			    <td class="align-center">
				<img src="${path}/images/icons/preview.png" title="Preview" onclick="createPreview('${invoice.id}')"/>
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
