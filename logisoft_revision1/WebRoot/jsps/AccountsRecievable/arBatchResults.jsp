<%-- 
    Document   : arBatchResults
    Created on : Jun 3, 2011, 7:32:18 PM
    Author     : lakshh
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<div align="right" id="pager" class="pagebanner" style="padding-right: 15px;">
    <div style="float:right">
        <c:if test="${arBatchForm.noOfPages>0}">
            <div style="float:left">
                <c:choose>
                    <c:when test="${arBatchForm.totalPageSize>arBatchForm.noOfRecords}">
                        ${arBatchForm.noOfRecords} out of ${arBatchForm.totalPageSize} batches displayed.
                    </c:when>
                    <c:when test="${arBatchForm.noOfRecords>1}">${arBatchForm.noOfRecords} batches displayed.</c:when>
                    <c:otherwise>1 batch displayed.</c:otherwise>
                </c:choose>
            </div>
            <c:if test="${arBatchForm.noOfPages>1 && arBatchForm.pageNo>1}">
                <a title="First page" href="javascript: gotoPage('1')">
                    <img alt="First" src="${path}/images/first.png"/>
                </a>
                <a title="Previous page" href="javascript: gotoPage('${arBatchForm.pageNo-1}')">
                    <img alt="Previous" src="${path}/images/prev.png"/>
                </a>
            </c:if>
            <c:if test="${arBatchForm.noOfPages>1}">
                <select id="selectedPageNo" class="textlabelsBoldForTextBox" style="float:left;">
                    <c:forEach begin="1" end="${arBatchForm.noOfPages}" var="pageNo">
                        <c:choose>
                            <c:when test="${arBatchForm.pageNo!=pageNo}">
                                <option value="${pageNo}">${pageNo}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${pageNo}" selected>${pageNo}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
                <a href="javascript: gotoSelectedPage()">
                    <img alt="Go" src="${path}/images/go.jpg"/>
                </a>
            </c:if>
            <c:if test="${arBatchForm.noOfPages>arBatchForm.pageNo}">
                <a title="Next page" href="javascript: gotoPage('${arBatchForm.pageNo+1}')">
                    <img alt="First" src="${path}/images/next.png"/>
                </a>
                <a title="Last page" href="javascript: gotoPage('${arBatchForm.noOfPages}')">
                    <img alt="Previous" src="${path}/images/last.png"/>
                </a>
            </c:if>
        </c:if>
    </div>
</div>
<div style="width:100%;float:left;">
    <c:choose>
        <c:when test="${not empty arBatchForm.arBatches}">
            <div class="scrolldisplaytable">
                <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" id="arBatch" style="border: 1px solid white">
                    <thead>
                        <c:choose>
                            <c:when test="${arBatchForm.orderBy=='desc'}"><c:set var="orderBy" value="asc"/></c:when>
                            <c:otherwise><c:set var="orderBy" value="desc"/></c:otherwise>
                        </c:choose>
                        <tr>
			    <th><a href="javascript:doSort('batch_id','${orderBy}')">Batch Number</a></th>
			    <th><a href="javascript:doSort('deposit_date','${orderBy}')">Deposit Date</a></th>
			    <th><a href="javascript:doSort('batch_type','${orderBy}')">Batch Type</a></th>
			    <th style="text-align: right"><a href="javascript:doSort('total_amount','${orderBy}')">Total Amount</a></th>
			    <th style="text-align: right"><a href="javascript:doSort('applied_amount','${orderBy}')">Applied Amount</a></th>
			    <th style="text-align: right"><a href="javascript:doSort('balance_amount','${orderBy}')">Balance Amount</a></th>
			    <th><a href="javascript:doSort('user','${orderBy}')">User</a></th>
			    <th><a href="javascript:doSort('bank_account','${orderBy}')">Bank Account</a></th>
			    <th><a href="javascript:doSort('gl_account','${orderBy}')">GL Account</a></th>
			    <th>Status</th>
                            <th>Notes</th>
			    <th>Cleared Date</th>
			    <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="zebra" value="odd"/>
                        <c:forEach var="arBatch" items="${arBatchForm.arBatches}" varStatus="status">
                            <c:choose>
                                <c:when test="${arBatch.status==commonConstants.STATUS_REVERSED}">
                                    <c:set var="zebra" value="${zebra} pink"/>
                                </c:when>
                                <c:when test="${arBatch.status==commonConstants.STATUS_CLOSED || arBatch.status==commonConstants.STATUS_VOID}">
                                    <c:set var="zebra" value="${zebra} red"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="zebra" value="${zebra} black"/>
                                </c:otherwise>
                            </c:choose>
                            <tr class="${zebra}">
				<td>${arBatch.batchId}</td>
				<td>${arBatch.depositDate}</td>
				<td>${arBatch.batchType}</td>
				<c:choose>
				    <c:when test="${fn:startsWith(arBatch.totalAmount,'-')}">
					<td style="text-align:right;" class="red">(${fn:replace(arBatch.totalAmount,'-','')})</td>
				    </c:when>
				    <c:otherwise>
					<td style="text-align:right;">${arBatch.totalAmount}</td>
				    </c:otherwise>
				</c:choose>
				<c:choose>
				    <c:when test="${fn:startsWith(arBatch.appliedAmount,'-')}">
					<td style="text-align:right;" class="red">(${fn:replace(arBatch.appliedAmount,'-','')})</td>
				    </c:when>
				    <c:otherwise>
					<td style="text-align:right;">${arBatch.appliedAmount}</td>
				    </c:otherwise>
				</c:choose>
				<c:choose>
				    <c:when test="${fn:startsWith(arBatch.balanceAmount,'-')}">
					<td style="text-align:right;" class="red">(${fn:replace(arBatch.balanceAmount,'-','')})</td>
				    </c:when>
				    <c:otherwise>
					<td style="text-align:right;">${arBatch.balanceAmount}</td>
				    </c:otherwise>
				</c:choose>
				<td class="uppercase">${arBatch.user}</td>
				<td>${arBatch.bankAccount}</td>
				<td>${arBatch.glAccount}</td>
				<td>${arBatch.status}</td>
                                <td class="uppercase">
                                    <label title="${arBatch.notes}">${str:abbreviate(arBatch.notes,25)}</label>
                                </td>
				<td>${arBatch.clearedDate}</td>
				<td>
				    <c:choose>
					<c:when test="${arBatch.status==commonConstants.STATUS_CLOSED 
							|| arBatch.status==commonConstants.STATUS_VOID 
							|| arBatch.status==commonConstants.STATUS_REVERSED}">
					    <c:if test="${arBatch.status==commonConstants.STATUS_CLOSED 
							  || arBatch.status==commonConstants.STATUS_REVERSED}">
						  <img src="${path}/img/icons/display.gif" alt="Show Batch Details"
						       title="Show Batch Details" onclick="showChecks('${arBatch.batchId}',false)"/>
					    </c:if>
					    <c:if test="${roleDuty.arBatchReversal && arBatch.status==commonConstants.STATUS_CLOSED}">
						  <img src="${path}/images/mail_reply.png" style="width:18px;height:18px;"
						       alt="Reverse Post" title="Reverse Post" onclick="reversePost('${arBatch.batchId}')"/>
					    </c:if>
					    <c:if test="${canEdit}">
						<img src="${path}/images/notepad_yellow.png"
						     alt="Notes" title="Notes" onclick="showArBatchNotes('${arBatch.batchId}')"/>
					    </c:if>
					    <img src="${path}/img/icons/print.gif" 
						 alt="Print" title="Print" onclick="printArBatch('${arBatch.batchId}')"/>
					</c:when>
					<c:otherwise>
					    <c:if test="${canEdit && (roleDuty.arBatchShowallUsersBatch || loginuser.loginName==arBatch.user)}">
						<img src="${path}/img/icons/edit.gif"
						     alt="Edit Batch" title="Edit Batch" onclick="editArBatch('${arBatch.batchId}')"/>
					    </c:if>
					    <img src="${path}/img/icons/display.gif" alt="Show Batch Details"
						 title="Show Checks" 
						 onclick="showChecks('${arBatch.batchId}',${canEdit && (roleDuty.arBatchShowallUsersBatch || loginuser.loginName==arBatch.user)})"/>
					    <c:if test="${canEdit && (roleDuty.arBatchShowallUsersBatch || loginuser.loginName==arBatch.user)}">
						<img src="${path}/img/icons/currency.gif"
						     alt="Apply Payments" title="Apply Payments" onclick="applyPayments('${arBatch.batchId}')"/>
						<img src="${path}/images/mail_send.png" style="width:18px;height:18px;"
						     alt="Post Batch" title="Post Batch" onclick="postArBatch('${arBatch.batchId}')"/>
						<img src="${path}/images/notepad_yellow.png"
						     alt="Show Notes" title="Show Notes" onclick="showArBatchNotes('${arBatch.batchId}')"/>
						<img src="${path}/img/icons/remove.gif"
						     alt="Void" title="Void" onclick="voidArBatch('${arBatch.batchId}')"/>
					    </c:if>
					    <img src="${path}/img/icons/print.gif" 
						 alt="Print" title="Print" onclick="printArBatch('${arBatch.batchId}')"/>
					</c:otherwise>
				    </c:choose>
				    <c:if test="${canEdit && (roleDuty.arBatchShowallUsersBatch || loginuser.loginName==arBatch.user)}">
					<c:choose>
					    <c:when test="${arBatch.hasDocuments}">
						<img alt="Scan/Attach" src="${path}/images/upload-green.gif"
						     title="Scan/Attach" onclick="showScanOrAttach('${arBatch.batchId}')"/>
					    </c:when>
					    <c:otherwise>
						<img alt="Scan/Attach" src="${path}/img/icons/upload.gif"
						     title="Scan/Attach" onclick="showScanOrAttach('${arBatch.batchId}')"/>
					    </c:otherwise>
					</c:choose>
				    </c:if>
				    <c:if test="${fn:toUpperCase(arBatch.batchType) == 'N' && arBatch.status==commonConstants.STATUS_CLOSED}">
					<img alt="Export NSInvoice" src="${path}/img/icons/excel.gif" title="Export NSInvoice"
					     onclick="exportNSInvoice('${arBatch.batchId}')"/>
					<img alt="Print NSInvoice" src="${path}/img/icons/pdf.gif" title="Print NSInvoice"
					     onclick="printNSInvoice('${arBatch.batchId}')"/>
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
        <c:otherwise>No batches found</c:otherwise>
    </c:choose>
</div>
