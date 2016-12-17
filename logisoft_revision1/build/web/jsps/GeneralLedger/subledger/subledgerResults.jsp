<%-- 
    Document   : subledgerResults
    Created on : Dec 20, 2012, 7:20:01 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${not empty subledgerForm.results}">
	<div id="result-header" class="table-banner green">
	    <div class="float-right">
		<div class="float-left">
		    <c:choose>
			<c:when test="${subledgerForm.totalRows>subledgerForm.selectedRows}">
			    ${subledgerForm.selectedRows} records displayed. ${subledgerForm.totalRows} records found.
			</c:when>
			<c:when test="${subledgerForm.selectedRows>1}">${subledgerForm.selectedRows} records found.</c:when>
			<c:otherwise>1 record found.</c:otherwise>
		    </c:choose>
		</div>
		<c:if test="${subledgerForm.totalPages>1 && subledgerForm.selectedPage>1}">
		    <a title="First page" href="javascript: paging('1')">
			<img alt="First page" title="First page" src="${path}/images/first.png"/>
		    </a>
		    <a title="Previous page" href="javascript: paging('${subledgerForm.selectedPage-1}')">
			<img alt="Previous page" title="Previous page" src="${path}/images/prev.png"/>
		    </a>
		</c:if>
		<c:if test="${subledgerForm.totalPages>1}">
		    <select id="selectedPageNo" class="dropdown float-left">
			<c:forEach begin="1" end="${subledgerForm.totalPages}" var="selectedPage">
			    <c:choose>
				<c:when test="${subledgerForm.selectedPage!=selectedPage}">
				    <option value="${selectedPage}">${selectedPage}</option>
				</c:when>
				<c:otherwise>
				    <option value="${selectedPage}" selected>${selectedPage}</option>
				</c:otherwise>
			    </c:choose>
			</c:forEach>
		    </select>
		    <a href="javascript: paging('')">
			<img alt="Goto Page" title="Goto Page" src="${path}/images/go.jpg"/>
		    </a>
		</c:if>
		<c:if test="${subledgerForm.totalPages>subledgerForm.selectedPage}">
		    <a title="Next page" href="javascript: paging('${subledgerForm.selectedPage+1}')">
			<img alt="Next Page" title="Next page" src="${path}/images/next.png"/>
		    </a>
		    <a title="Last page" href="javascript: paging('${subledgerForm.totalPages}')">
			<img alt="Last Page" title="Last page" src="${path}/images/last.png"/>
		    </a>
		</c:if>
	    </div>
	</div>
	<div class="result-container">
	    <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
		<thead>
		    <tr>
			<th><a href="javascript:sorting('subledger_source_code')">Subledger</a></th>
			<th><a href="javascript:sorting('ar_batch_id')">AR Batch</a></th>
			<th><a href="javascript:sorting('ap_batch_id')">AP Batch</a></th>
			<th><a href="javascript:sorting('bill_ladding_no')">BL</a></th>
			<th><a href="javascript:sorting('invoice_number')">Invoice</a></th>
			<th><a href="javascript:sorting('gl_account_number')">GL Account</a></th>
			<th><a href="javascript:sorting('charge_code')">Charge Code</a></th>
			<th><a href="javascript:sorting('voyage_no')">Voyage</a></th>
			<th><a href="javascript:sorting('transaction_date')">Transaction Date</a></th>
			<th><a href="javascript:sorting('sailing_date')">Reporting Date</a></th>
			<th><a href="javascript:sorting('posted_date')">Posted Date</a></th>
			<th><a href="javascript:sorting('transaction_amt')">Amount</a></th>
			<th><a href="javascript:sorting('debit')">Debit</a></th>
			<th><a href="javascript:sorting('credit')">Credit</a></th>
			<th><a href="javascript:sorting('transaction_type')">Type</a></th>
			<th><a href="javascript:sorting('journal_entry_number')">Journal Entry</a></th>
			<th><a href="javascript:sorting('line_item_number')">Line Item</a></th>
		    </tr>
		</thead>
		<tbody>
		    <c:set var="zebra" value="odd"/>
		    <c:forEach var="result" items="${subledgerForm.results}" varStatus="status">
			<tr class="${zebra}">
			    <td>${result.subledger}</td>
			    <td>${result.arBatchId}</td>
			    <td>${result.apBatchId}</td>
			    <td>${result.blNumber}</td>
			    <td>${result.invoiceNumber}</td>
			    <td>
				<c:choose>
				    <c:when test="${subledgerForm.subledger!='ACC' && subledgerForm.posted}">
					${result.glAccount}
				    </c:when>
				    <c:otherwise>
					<input type="text" id="glAccount${status.count-1}" value="${result.glAccount}" class="textbox glAccount"/>
					<img title="Update GL Account" src="${path}/images/icons/update.png"
					     style="width: 16px;height: 16px;vertical-align: bottom;" 
					     onclick="updateGlAccount('${result.id}','glAccount${status.count-1}')"/>
				    </c:otherwise>
				</c:choose>
			    </td>
			    <td>${result.chargeCode}</td>
			    <td>${result.voyage}</td>
			    <td>${result.transactionDate}</td>
			    <td>${result.reportingDate}</td>
			    <td>${result.postedDate}</td>
			    <c:choose>
				<c:when test="${fn:contains(result.amount,'-')}">
				    <td class="align-right red">(${fn:replace(result.amount,'-','')})</td>
				</c:when>
				<c:otherwise>
				    <td class="align-right black">${result.amount}</td>
				</c:otherwise>
			    </c:choose>
			    <td class="align-right">${result.debit}</td>
			    <td class="align-right">${result.credit}</td>
			    <td class="align-center">${result.transactionType}</td>
			    <td class="align-center">${result.journalEntryId}</td>
			    <td class="align-center">${result.lineItemId}</td>
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
	<div class="table-banner green" style="background-color: #D1E6EE;">No records found</div>
    </c:otherwise>
</c:choose>