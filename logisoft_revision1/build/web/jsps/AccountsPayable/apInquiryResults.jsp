<%--
    Document   : apInquiryResults
    Created on : July 18, 2011, 2:20:58 AM
    Author     : Lakshmi Naryanan
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div align="right" id="pager" class="pagebanner" style="padding-right: 15px;">
    <div style="float:right">
        <c:if test="${apInquiryForm.noOfPages>0}">
            <div style="float: left;padding: 0 20px 0 0">
                <input type="button" class="buttonStyleNew" value="Print" style="width: 40px" onclick="printReport()"/>
                <input type="button" class="buttonStyleNew" value="Export to Excel" style="width: 100px" onclick="exportToExcel()"/>
            </div>
            <div style="float:left">
                <c:choose>
                    <c:when test="${apInquiryForm.totalPageSize>apInquiryForm.noOfRecords}">
                        ${apInquiryForm.noOfRecords} out of ${apInquiryForm.totalPageSize} payables displayed.
                    </c:when>
                    <c:when test="${apInquiryForm.noOfRecords>1}">
                        ${apInquiryForm.noOfRecords} payables displayed.
                    </c:when>
                    <c:otherwise>
                        1 payable displayed.
                    </c:otherwise>
                </c:choose>
            </div>
            <c:if test="${apInquiryForm.noOfPages>1 && apInquiryForm.pageNo>1}">
                <a title="First page" href="javascript: gotoPage('1')">
                    <img alt="First" src="${path}/images/first.png" border="0"/>
                </a>
                <a title="Previous page" href="javascript: gotoPage('${apInquiryForm.pageNo-1}')">
                    <img alt="Previous" src="${path}/images/prev.png" border="0"/>
                </a>
            </c:if>
            <c:if test="${apInquiryForm.noOfPages>1}">
                <select id="selectedPageNo" class="textlabelsBoldForTextBox" style="float:left;">
                    <c:forEach begin="1" end="${apInquiryForm.noOfPages}" var="pageNo">
                        <c:choose>
                            <c:when test="${apInquiryForm.pageNo!=pageNo}">
                                <option value="${pageNo}">${pageNo}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${pageNo}" selected>${pageNo}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
                <a href="javascript: gotoSelectedPage()">
                    <img alt="Go" src="${path}/images/go.jpg" border="0"/>
                </a>
            </c:if>
            <c:if test="${apInquiryForm.noOfPages>apInquiryForm.pageNo}">
                <a title="Next page" href="javascript: gotoPage('${apInquiryForm.pageNo+1}')">
                    <img alt="First" src="${path}/images/next.png" border="0"/>
                </a>
                <a title="Last page" href="javascript: gotoPage('${apInquiryForm.noOfPages}')">
                    <img alt="Previous" src="${path}/images/last.png" border="0"/>
                </a>
            </c:if>
        </c:if>
    </div>
</div>
<div style="width:100%;float:left;">
    <c:choose>
        <c:when test="${not empty apInquiryForm.apInquiryList}">
            <div class="scrolldisplaytable">
                <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" id="transaction" style="border: 1px solid white">
                    <thead>
                        <c:choose>
                            <c:when test="${apInquiryForm.orderBy=='desc'}">
                                <c:set var="orderBy" value="asc"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="orderBy" value="desc"/>
                            </c:otherwise>
                        </c:choose>
                        <tr>
			    <th><a href="javascript:doSort('tbl.acctNo','${orderBy}')">Vendor#</a></th>
			    <th><a href="javascript:doSort('tbl.acctName','${orderBy}')">Vendor Name</a></th>
			    <th><a href="javascript:doSort('tbl.invoiceOrBl','${orderBy}')">Invoice/BL</a></th>
			    <th><a href="javascript:doSort('tbl.invoiceDate','${orderBy}')">Invoice Date</a></th>
			    <th><a href="javascript:doSort('tbl.dueDate','${orderBy}')">Due Date</a></th>
			    <th><a href="javascript:doSort('tbl.invoiceAmount','${orderBy}')">Invoice Amount</a></th>
			    <th><a href="javascript:doSort('tbl.balance','${orderBy}')">Balance</a></th>
			    <th><a href="javascript:doSort('tbl.checkNumber','${orderBy}')">Check Number</a></th>
			    <th><a href="javascript:doSort('tbl.paymentDate','${orderBy}')">Payment Date</a></th>
			    <th><a href="javascript:doSort('tbl.clearedDate','${orderBy}')">Cleared Date</a></th>
			    <th><a href="javascript:doSort('tbl.dockReceipt','${orderBy}')">Dock Receipt</a></th>
			    <th><a href="javascript:doSort('tbl.voyage','${orderBy}')">Voyage</a></th>
			    <th><a href="javascript:doSort('tbl.costCode','${orderBy}')">Cost Code</a></th>
			    <th><a href="javascript:doSort('tbl.transactionType','${orderBy}')">Type</a></th>
			    <th><a href="javascript:doSort('tbl.customerReference','${orderBy}')">Cust Ref#</a></th>
			    <th><a href="javascript:doSort('tbl.status','${orderBy}')">Status</a></th>
			    <th>Hold</th>
			    <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="zebra" value="odd"/>
                        <c:forEach var="transaction" items="${apInquiryForm.apInquiryList}">
                            <c:set var="color" value="black"/>
                            <c:choose>
                                <c:when test="${transaction.status==commonConstants.STATUS_IN_PROGRESS 
						|| transaction.status==commonConstants.STATUS_EDI_IN_PROGRESS}">
                                    <c:set var="color" value="red"/>
                                </c:when>
                                <c:when test="${transaction.transactionType==commonConstants.TRANSACTION_TYPE_ACCRUALS}">
                                    <c:set var="color" value="blue"/>
                                </c:when>
                            </c:choose>
                            <tr class="${zebra} ${color}">
				<td class="uppercase">
				    <c:if test="${not empty transaction.correctionNotice && transaction.correctionNotice!='0-CNA'}">
					<label style="color:red; font-size:medium">*</label>
				    </c:if>
				    ${transaction.customerNumber}
				</td>
				<td class="uppercase">
				    <label title="${transaction.customerName}">
					<c:choose>
					    <c:when test="${fn:length(transaction.customerName)>20}">
						${fn:substring(transaction.customerName,0,20)}...
					    </c:when>
					    <c:otherwise>${transaction.customerName}</c:otherwise>
					</c:choose>
				    </label>
				</td>
				<td class="uppercase">
				    <a href="javascript:showInvoiceOrBlDetails('${transaction.transactionId}')">
					${transaction.invoiceOrBl}
				    </a>
				    <input type="hidden" id="vendorNumber${transaction.transactionId}" value="${transaction.customerNumber}"/>
				    <input type="hidden" id="invoiceOrBl${transaction.transactionId}" value="${transaction.invoiceOrBl}"/>
				    <input type="hidden" id="invoiceNumber${transaction.transactionId}" value="${transaction.invoiceNumber}"/>
				    <input type="hidden" id="billOfLadding${transaction.transactionId}" value="${transaction.billOfLadding}"/>
				    <input type="hidden" id="transactionType${transaction.transactionId}" value="${transaction.transactionType}"/>
				    <input type="hidden" id="status${transaction.transactionId}" value="${transaction.status}"/>
				</td>
				<td>${transaction.formattedDate}</td>
				<td>${transaction.formattedDueDate}</td>
				<c:choose>
				    <c:when test="${transaction.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE
						    && fn:startsWith(transaction.formattedAmount,'-')}">
					<c:set var="invoiceAmount" value="${fn:replace(transaction.formattedAmount,'-','')}"/>
					<c:set var="balanceColor" value="black"/>
				    </c:when>
				    <c:when test="${transaction.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE}">
					<c:set var="invoiceAmount" value="(${transaction.formattedAmount})"/>
					<c:set var="amountColor" value="red"/>
				    </c:when>
				    <c:when test="${transaction.transactionType==commonConstants.TRANSACTION_TYPE_ACCRUALS
						    && fn:startsWith(transaction.formattedAmount,'-')}">
					<c:set var="invoiceAmount" value="(${fn:replace(transaction.formattedAmount,'-','')})"/>
					<c:set var="amountColor" value="red"/>
				    </c:when>
				    <c:when test="${transaction.transactionType==commonConstants.TRANSACTION_TYPE_ACCRUALS}">
					<c:set var="invoiceAmount" value="${transaction.formattedAmount}"/>
					<c:set var="amountColor" value="blue"/>
				    </c:when>
				    <c:when test="${fn:startsWith(transaction.formattedAmount,'-')}">
					<c:set var="invoiceAmount" value="(${fn:replace(transaction.formattedAmount,'-','')})"/>
					<c:set var="amountColor" value="red"/>
				    </c:when>
				    <c:otherwise>
					<c:set var="invoiceAmount" value="${transaction.formattedAmount}"/>
					<c:set var="amountColor" value="black"/>
				    </c:otherwise>
				</c:choose>
				<c:if test="${transaction.status==commonConstants.STATUS_IN_PROGRESS 
						|| transaction.status==commonConstants.STATUS_EDI_IN_PROGRESS}">
				    <c:set var="amountColor" value="red"/>
				</c:if>
				<td class="${amountColor}" style="text-align:right;">
				    <c:choose>
					<c:when test="${transaction.transactionType == commonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE}">
					    <a href="javascript:showInvoiceOrBlDetails('${transaction.transactionId}')" class="${amountColor}">
						${invoiceAmount}
					    </a>
					</c:when>
					<c:otherwise>
					    ${invoiceAmount}
					</c:otherwise>
				    </c:choose>
				</td>
				<c:choose>
				    <c:when test="${transaction.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE
						    && fn:startsWith(transaction.formattedBalance,'-')}">
					<c:set var="invoiceBalance" value="${fn:replace(transaction.formattedBalance,'-','')}"/>
					<c:set var="balanceColor" value="black"/>
				    </c:when>
				    <c:when test="${transaction.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE}">
					<c:set var="invoiceBalance" value="(${transaction.formattedBalance})"/>
					<c:set var="balanceColor" value="red"/>
				    </c:when>
				    <c:when test="${transaction.transactionType==commonConstants.TRANSACTION_TYPE_ACCRUALS
						    && fn:startsWith(transaction.formattedBalance,'-')}">
					<c:set var="invoiceBalance" value="(${fn:replace(transaction.formattedBalance,'-','')})"/>
					<c:set var="amountColor" value="red"/>
				    </c:when>
				    <c:when test="${transaction.transactionType==commonConstants.TRANSACTION_TYPE_ACCRUALS}">
					<c:set var="invoiceBalance" value="${transaction.formattedBalance}"/>
					<c:set var="balanceColor" value="blue"/>
				    </c:when>
				    <c:when test="${fn:startsWith(transaction.formattedBalance,'-')}">
					<c:set var="invoiceBalance" value="(${fn:replace(transaction.formattedBalance,'-','')})"/>
					<c:set var="balanceColor" value="red"/>
				    </c:when>
				    <c:otherwise>
					<c:set var="invoiceBalance" value="${transaction.formattedBalance}"/>
					<c:set var="balanceColor" value="black"/>
				    </c:otherwise>
				</c:choose>
				<c:if test="${transaction.status==commonConstants.STATUS_IN_PROGRESS 
						|| transaction.status==commonConstants.STATUS_EDI_IN_PROGRESS}">
				    <c:set var="balanceColor" value="red"/>
				</c:if>
				<td class="${balanceColor}" style="text-align:right;">${invoiceBalance}</td>
				<td class="uppercase">${transaction.checkNumber}</td>
				<td>
				    <c:if test="${transaction.status==commonConstants.STATUS_PAID}">
					${transaction.formattedPaymentDate}
				    </c:if>
				</td>
				<td>${transaction.clearedDate}</td>
				<td class="uppercase">${transaction.dockReceipt}</td>
				<td class="uppercase">${transaction.voyage}</td>
				<td class="uppercase">${transaction.chargeCode}</td>
				<td class="uppercase">${transaction.transactionType}</td>
				<td class="uppercase">
				    <c:choose>
					<c:when test="${fn:length(transaction.customerReference)>20}">
					    <label title="${transaction.customerReference}">
						${fn:substring(transaction.customerReference,0,20)}...
					    </label>
					</c:when>
					<c:otherwise>${transaction.customerReference}</c:otherwise>
				    </c:choose>
				</td>
				<td>
				    <c:choose>
					<c:when test="${transaction.status==commonConstants.STATUS_PAID}">Paid</c:when>
					<c:when test="${transaction.status==commonConstants.STATUS_READY_TO_PAY}">Ready To Pay</c:when>
					<c:when test="${transaction.status==commonConstants.STATUS_ASSIGN}">Assigned</c:when>
					<c:when test="${transaction.status==commonConstants.STATUS_HOLD}">Hold</c:when>
					<c:when test="${transaction.status==commonConstants.STATUS_REJECT}">Reject</c:when>
					<c:when test="${transaction.status==commonConstants.STATUS_IN_PROGRESS}">In Progress</c:when>
					<c:otherwise>${transaction.status}</c:otherwise>
				    </c:choose>
				</td>
				<td>
				    <c:if test="${transaction.status==commonConstants.STATUS_HOLD}">
					<img src="${path}/img/icons/ok.gif" alt="Hold"/>
				    </c:if>
				</td>
				<td>
				    <img alt="More Information" src="${path}/img/icons/pubserv.gif" border="0"
					 title="More Information" onclick="showMoreInfo('${transaction.transactionId}')"/>
				    <c:if test="${canEdit}">
					<c:if test="${(transaction.status==commonConstants.STATUS_PAID
						      && transaction.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE)
						      || transaction.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE}">
					      <img src="${path}/img/icons/currency.gif" border="0" alt="Show Payments"
						   onclick="showPaymentTemplate('${transaction.transactionId}')"/>
					</c:if>
					<c:if test="${not empty transaction.invoiceOrBl && fn:trim(transaction.invoiceOrBl)!='' 
						      && (!roleDuty.viewAccountingScanAttach || (roleDuty.viewAccountingScanAttach && transaction.subType != 'Y'))}">
					    <c:choose>
						<c:when test="${transaction.hasDocuments}">
						    <img alt="Scan/Attach" src="${path}/images/upload-green.gif" border="0"
							 title="Scan/Attach" onclick="showScanOrAttach('${transaction.transactionId}')"/>
						</c:when>
						<c:otherwise>
						    <img alt="Scan/Attach" src="${path}/img/icons/upload.gif" border="0"
							 title="Scan/Attach" onclick="showScanOrAttach('${transaction.transactionId}')"/>
						</c:otherwise>
					    </c:choose>
					</c:if>
					<c:choose>
					    <c:when test="${transaction.manualNotes}">
						<img alt="Notes" src="${path}/images/notepad_green.png" border="0" width="16px" height="16px"
						    title="Notes" onclick="showInvoiceNotes('${transaction.costId}','${transaction.transactionId}')"/>
					    </c:when>
					    <c:otherwise>
						<img alt="Notes" src="${path}/images/notepad_yellow.png" border="0" width="16px" height="16px"
						    title="Notes" onclick="showInvoiceNotes('${transaction.costId}','${transaction.transactionId}')"/>
					    </c:otherwise>
					</c:choose>
					<c:if test="${fn:indexOf(transaction.invoiceOrBl,'04-')>-1}">
					    <img alt="File Notes" src="${path}/img/icons/notes1.png" border="0"
						 title="File Notes" onclick="showFileNotes('${transaction.dockReceipt}')"/>
					</c:if>
				    </c:if>
				    <c:if test="${fn:containsIgnoreCase(transaction.invoiceOrBl,commonConstants.SUBLEDGER_CODE_NETSETT)}">
					<img alt="exportNSInvoice" src="${path}/img/icons/excel.gif" border="0" title="exportNSInvoice"
					     onclick="exportNSInvoice('${fn:replace(fn:toUpperCase(transaction.invoiceOrBl),commonConstants.SUBLEDGER_CODE_NETSETT,'')}')"/>
					<img alt="printNSInvoice" src="${path}/img/icons/pdf.gif" border="0" title="printNSInvoice"
					     onclick="printNSInvoice('${fn:replace(fn:toUpperCase(transaction.invoiceOrBl),commonConstants.SUBLEDGER_CODE_NETSETT,'')}')"/>
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
            No payables found
        </c:otherwise>
    </c:choose>
</div>
