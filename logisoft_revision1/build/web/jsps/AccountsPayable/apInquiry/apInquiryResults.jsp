<%-- 
    Document   : apInquiryResults
    Created on : Oct 1, 2012, 3:52:01 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>   
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${not empty apInquiryForm.results}">
	<div id="result-header" class="table-banner green">
	    <div class="float-right">
		<div class="float-left">
		    <c:choose>
			<c:when test="${apInquiryForm.totalRows>apInquiryForm.selectedRows}">
			    ${apInquiryForm.selectedRows} records displayed. ${apInquiryForm.totalRows} records found.
			</c:when>
			<c:when test="${apInquiryForm.selectedRows>1}">${apInquiryForm.selectedRows} records found.</c:when>
			<c:otherwise>1 record found.</c:otherwise>
		    </c:choose>
		</div>
		<c:if test="${apInquiryForm.totalPages>1 && apInquiryForm.selectedPage>1}">
		    <a title="First page" href="javascript: gotoPage('1')">
			<img alt="First page" title="First page" src="${path}/images/first.png"/>
		    </a>
		    <a title="Previous page" href="javascript: gotoPage('${apInquiryForm.selectedPage-1}')">
			<img alt="Previous page" title="Previous page" src="${path}/images/prev.png"/>
		    </a>
		</c:if>
		<c:if test="${apInquiryForm.totalPages>1}">
		    <select id="selectedPageNo" class="dropdown float-left">
			<c:forEach begin="1" end="${apInquiryForm.totalPages}" var="selectedPage">
			    <c:choose>
				<c:when test="${apInquiryForm.selectedPage!=selectedPage}">
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
		<c:if test="${apInquiryForm.totalPages>apInquiryForm.selectedPage}">
		    <a title="Next page" href="javascript: gotoPage('${apInquiryForm.selectedPage+1}')">
			<img alt="Next Page" title="Next page" src="${path}/images/next.png"/>
		    </a>
		    <a title="Last page" href="javascript: gotoPage('${apInquiryForm.totalPages}')">
			<img alt="Last Page" title="Last page" src="${path}/images/last.png"/>
		    </a>
		</c:if>
	    </div>
	</div>
	<div class="result-container">
	    <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
		<thead>
		    <tr>
			<th class="width-130px"><a href="javascript:doSort('vendor_name')">Vendor Name</a></th>
			<th><a href="javascript:doSort('vendor_number')">Vendor Number</a></th>
			<th class="width-100px"><a href="javascript:doSort('invoice_number')">Invoice/BL</a></th>
			<th><a href="javascript:doSort('invoice_date')">Invoice Date</a></th>
			<th><a href="javascript:doSort('due_date')">Due Date</a></th>
			<th><a href="javascript:doSort('invoice_amount')">Invoice Amount</a></th>
			<th><a href="javascript:doSort('invoice_balance')">Invoice Balance</a></th>
			<th class="width-100px"><a href="javascript:doSort('check_number')">Check Number</a></th>
			<th><a href="javascript:doSort('payment_date')">Payment Date</a></th>
			<th><a href="javascript:doSort('cleared_date')">Cleared Date</a></th>
			<th><a href="javascript:doSort('dock_receipt')">Dock Receipt</a></th>
			<th><a href="javascript:doSort('voyage')">Voyage</a></th>
			<th><a href="javascript:doSort('cost_code')">Cost Code</a></th>
			<th><a href="javascript:doSort('transaction_type')">Type</a></th>
			<th class="width-85px"><a href="javascript:doSort('status')">Status</a></th>
			<th class="width-115px">Action</th>
		    </tr>
		</thead>
		<tbody>
		    <c:set var="zebra" value="odd"/>
		    <c:forEach var="result" items="${apInquiryForm.results}" varStatus="status">
			<c:choose>
			    <c:when test="${result.status == 'In Progress' || result.status == 'EDI In Progress'}">
				<c:set var="zebra" value="${zebra} red"/>
			    </c:when>
			    <c:when test="${result.transactionType == 'AC'}">
				<c:set var="zebra" value="${zebra} blue"/>
			    </c:when>
			    <c:otherwise>
				<c:set var="zebra" value="${zebra} black"/>
			    </c:otherwise>
			</c:choose>
			<tr class="${zebra}">
			    <td>
				<label title="${result.vendorName}">${str:abbreviate(result.vendorName,20)}</label>
			    </td>
			    <td>${result.vendorNumber}</td>
			    <td>
				<c:choose>
				    <c:when test="${result.transactionType != 'AR' && not empty result.invoiceNumber}">
					<c:set var="vendorNumber" value="${result.vendorNumber}"/>
					<c:set var="invoiceNumber" value="${result.invoiceNumber}"/>
					<c:set var="blNumber" value="${result.blNumber}"/>
					<c:choose>
					    <c:when test="${result.transactionType == 'AC' 
							    && (result.status == 'Assigned' || result.status == 'EDI Assigned')}">
						<c:set var="transactionType" value="AP"/>
					    </c:when>
					    <c:otherwise>
						<c:set var="transactionType" value="${result.transactionType}"/>
					    </c:otherwise>
					</c:choose>
					<a href="javascript:showInvoiceDetails('${vendorNumber}','${invoiceNumber}','${blNumber}','${transactionType}')">
					    ${result.invoiceOrBl}
					</a>
				    </c:when>
				    <c:otherwise>
					${result.invoiceOrBl}
				    </c:otherwise>
				</c:choose>
			    </td>
			    <td>${result.invoiceDate}</td>
			    <td>${result.dueDate}</td>
			    <c:choose>
				<c:when test="${result.status == 'In Progress' || result.status == 'EDI In Progress'}">
				    <c:set var="invoiceAmount" value="red"/>
				</c:when>
				<c:when test="${fn:contains(result.invoiceAmount,'-')}">
				    <c:set var="invoiceAmount" value="red"/>
				</c:when>
				<c:when test="${result.transactionType == 'AC'}">
				    <c:set var="invoiceAmount" value="blue"/>
				</c:when>
				<c:otherwise>
				    <c:set var="invoiceAmount" value="black"/>
				</c:otherwise>
			    </c:choose>
			    <td class="${invoiceAmount} amount">
				<c:choose>
				    <c:when test="${fn:contains(result.invoiceAmount,'-')}">
					(${fn:replace(result.invoiceAmount,'-','')})
				    </c:when>
				    <c:otherwise>
					${result.invoiceAmount}
				    </c:otherwise>
				</c:choose>
			    </td>
			    <c:choose>
				<c:when test="${result.status == 'In Progress' || result.status == 'EDI In Progress'}">
				    <c:set var="invoiceBalance" value="red"/>
				</c:when>
				<c:when test="${fn:contains(result.invoiceBalance,'-')}">
				    <c:set var="invoiceBalance" value="red"/>
				</c:when>
				<c:when test="${result.transactionType == 'AC'}">
				    <c:set var="invoiceBalance" value="blue"/>
				</c:when>
				<c:otherwise>
				    <c:set var="invoiceBalance" value="black"/>
				</c:otherwise>
			    </c:choose>
			    <td class="${invoiceBalance} amount">
				<c:choose>
				    <c:when test="${fn:contains(result.invoiceBalance,'-')}">
					(${fn:replace(result.invoiceBalance,'-','')})
				    </c:when>
				    <c:otherwise>
					${result.invoiceBalance}
				    </c:otherwise>
				</c:choose>
			    </td>
			    <td>${result.checkNumber}</td>
			    <td>${result.paymentDate}</td>
			    <td>${result.clearedDate}</td>
			    <td>${result.dockReceipt}</td>
			    <td>${result.voyage}</td>
			    <td>${result.costCode}</td>
			    <td>${result.transactionType}</td>
			    <td>${result.status}</td>
			    <td>
				<c:if test="${result.transactionType == 'AC' && not empty result.invoiceNumber}">
				    <c:set var="invoiceRefId" value="${result.vendorNumber}-${result.invoiceNumber}"/>
				</c:if>
				<c:choose>
				    <c:when test="${result.manualNotes}">
					<img title="Notes" src="${path}/images/notepad_green.png"
					     onclick="showNotes('${result.noteModuleId}','${result.noteRefId}','${result.costId}','${invoiceRefId}')"/>
				    </c:when>
				    <c:otherwise>
					<img title="Notes" src="${path}/images/notepad_yellow.png"
					     onclick="showNotes('${result.noteModuleId}','${result.noteRefId}','${result.costId}','${invoiceRefId}')"/>
				    </c:otherwise>
				</c:choose>
				<c:if test="${fn:trim(result.invoiceOrBl)!='' 
					      && (not roleDuty.viewAccountingScanAttach || (roleDuty.viewAccountingScanAttach && not result.overhead))}">
				    <c:choose>
					<c:when test="${result.uploaded}">
					    <img title="Upload Invoice"
						 src="${path}/images/upload-green.gif" onclick="uploadInvoice('${result.documentId}')"/>
					</c:when>
					<c:otherwise>
					    <img title="Upload Invoice" 
						 src="${path}/img/icons/upload.gif" onclick="uploadInvoice('${result.documentId}')"/>
					</c:otherwise>
				    </c:choose>
				</c:if>
				<c:if test="${result.transactionType == 'AC' && fn:indexOf(result.invoiceOrBl,'04-') >- 1}">
				    <img title="File Notes" src="${path}/img/icons/notes1.png" onclick="showFileNotes('${result.dockReceipt}')"/>
				</c:if>
				<c:if test="${result.transactionType == 'AR' && fn:containsIgnoreCase(result.invoiceOrBl,'NET SETT')}">
				    <img title="Print NS Invoice" src="${path}/img/icons/pdf.gif"
					 onclick="createNSInvoicePdf('${fn:replace(fn:toUpperCase(result.invoiceOrBl),'NET SETT','')}')"/>
				    <img title="Export NS Invoice" src="${path}/img/icons/excel.gif"
					 onclick="createNSInvoiceExcel('${fn:replace(fn:toUpperCase(result.invoiceOrBl),'NET SETT','')}')"/>
				</c:if>
			    </td>
			</tr>
			<c:choose>
			    <c:when test="${status.index % 2 eq 0}">
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
	<div class="table-banner green" style="background-color: #D1E6EE;">No records found</div>
    </c:otherwise>
</c:choose>