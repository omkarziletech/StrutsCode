<%-- 
    Document   : arCreditHoldResults
    Created on : Sep 19, 2011, 7:15:44 PM
    Author     : Lakshmi Narayanan
--%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <div align="right" id="pager" class="pagebanner" style="padding-right: 15px;">
	<div style="float:right">
	<c:if test="${arCreditHoldForm.noOfPages>0}">
	    <div style="float:left">
		<c:choose>
		    <c:when test="${arCreditHoldForm.totalPageSize>arCreditHoldForm.noOfRecords}">
			${arCreditHoldForm.noOfRecords} out of ${arCreditHoldForm.totalPageSize} receivables displayed.
		    </c:when>
		    <c:when test="${arCreditHoldForm.noOfRecords>1}">${arCreditHoldForm.noOfRecords} receivables displayed.</c:when>
		    <c:otherwise>1 receivable displayed.</c:otherwise>
		</c:choose>
	    </div>
	    <c:if test="${arCreditHoldForm.noOfPages>1 && arCreditHoldForm.pageNo>1}">
		<a title="First page" href="javascript: gotoPage('1')">
		    <img alt="First" src="${path}/images/first.png" border="0"/>
		</a>
		<a title="Previous page" href="javascript: gotoPage('${arCreditHoldForm.pageNo-1}')">
		    <img alt="Previous" src="${path}/images/prev.png" border="0"/>
		</a>
	    </c:if>
	    <c:if test="${arCreditHoldForm.noOfPages>1}">
		<select id="selectedPageNo" class="textlabelsBoldForTextBox" style="float:left;">
		    <c:forEach begin="1" end="${arCreditHoldForm.noOfPages}" var="pageNo">
			<c:choose>
			    <c:when test="${arCreditHoldForm.pageNo!=pageNo}">
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
	    <c:if test="${arCreditHoldForm.noOfPages>arCreditHoldForm.pageNo}">
		<a title="Next page" href="javascript: gotoPage('${arCreditHoldForm.pageNo+1}')">
		    <img alt="First" src="${path}/images/next.png" border="0"/>
		</a>
		<a title="Last page" href="javascript: gotoPage('${arCreditHoldForm.noOfPages}')">
		    <img alt="Previous" src="${path}/images/last.png" border="0"/>
		</a>
	    </c:if>
	</c:if>
    </div>
</div>
<div style="width:100%;float:left;">
    <c:choose>
	<c:when test="${not empty arCreditHoldForm.transactions}">
	    <div class="scrolldisplaytable">
		<table width="100%" cellpadding="0" cellspacing="1" class="displaytagstyleNew" id="transaction">
		    <thead>
			<c:choose>
			    <c:when test="${arCreditHoldForm.orderBy=='desc'}"><c:set var="orderBy" value="asc"/></c:when>
			    <c:otherwise><c:set var="orderBy" value="desc"/></c:otherwise>
			</c:choose>
			<tr>
			    <c:set var="colspan" value="15"/>
			    <c:if test="${roleDuty.creditHolder && canEdit}">
				<c:set var="colspan" value="16"/>
			    </c:if>
			    <th colspan="${colspan}" style="background-color: white"></th>
			    <th colspan="3" align="center" style="background-color: yellowgreen">Why shown on this list</th>
			    <th style="background-color: white">&nbsp;</th>
			</tr>
			<tr class="header">
			    <th><a href="javascript:doSort('c.acct_name','${orderBy}')">Customer Name</a></th>
			    <th><a href="javascript:doSort('c.acct_no','${orderBy}')">Customer Number</a></th>
			    <th><a href="javascript:doSort('c.collector','${orderBy}')">Collector</a></th>
			    <th><a href="javascript:doSort('c.credit_limit','${orderBy}')">Credit Limit</a></th>
			    <th><a href="javascript:doSort('c.credit_terms_value','${orderBy}')">Credit Terms</a></th>
			    <th><a href="javascript:doSort('c.bl_terms','${orderBy}')">BL Terms</a></th>
			    <th><a href="javascript:doSort('c.bill_to','${orderBy}')">Bill To</a></th>
			    <th><a href="javascript:doSort('c.bill_of_ladding','${orderBy}')">BL Number</a></th>
			    <th><a href="javascript:doSort('c.invoice_date','${orderBy}')">Invoice Date</a></th>
			    <th style="text-align: right"><a href="javascript:doSort('c.invoice_amount','${orderBy}')">Amount</a></th>
			    <th><a href="javascript:doSort('c.age','${orderBy}')">Age</a></th>
			    <th style="text-align: right"><a href="javascript:doSort('c.invoice_balance','${orderBy}')">Balance</a></th>
			    <th><a href="javascript:doSort('c.arrival_date','${orderBy}')">Arrival Date</a></th>
			    <th><a href="javascript:doSort('c.bl_created_by','${orderBy}')">BL Created By</a></th>
			    <c:if test="${roleDuty.creditHolder && canEdit}">
				<th>Remove From Hold</th>
			    </c:if>
			    <th><a href="javascript:doSort('c.credit_hold','${orderBy}')">On Hold</a></th>
			    <th><a href="javascript:doSort('c.no_credit','${orderBy}')">No Credit</a></th>
			    <th><a href="javascript:doSort('c.past_due','${orderBy}')">Past Due</a></th>
			    <th><a href="javascript:doSort('c.over_limit','${orderBy}')">Over Limit</a></th>
			    <th>Action</th>
			</tr>
		    </thead>
		    <tbody>
			<c:set var="zebra" value="odd"/>
			<c:forEach var="transaction" items="${arCreditHoldForm.transactions}" varStatus="status">
			    <tr class="${zebra}">
				<td title="${transaction.customerName}" class="uppercase">
				    <c:choose>
					<c:when test="${fn:length(transaction.customerName)>20}">
					    ${fn:substring(transaction.customerName,0,20)}...
					</c:when>
					<c:otherwise>${transaction.customerName}</c:otherwise>
				    </c:choose>
				</td>
				<td class="uppercase">
				    <c:set var="customerParam" value="customerNumber=${transaction.customerNumber}&customerName=${transaction.customerName}"/>
				    <a href="javascript:showArInquiry('${customerParam}')">
					${transaction.customerNumber}
				    </a>
				</td>
				<td class="uppercase">${transaction.collector}</td>
				<c:choose>
				    <c:when test="${fn:startsWith(transaction.formattedCredit,'-')}">
					<td style="text-align:right;" class="red">(${fn:replace(transaction.formattedCredit,'-','')})</td>
				    </c:when>
				    <c:otherwise>
					<td style="text-align:right;">${transaction.formattedCredit}</td>
				    </c:otherwise>
				</c:choose>
				<td class="uppercase">${transaction.creditTerms}</td>
				<td class="uppercase">${transaction.blTerms}</td>
				<td>
				    <c:if test="${transaction.billTo==commonConstants.YES}">
					<img alt="" src="${path}/img/icons/ok.gif"/>
				    </c:if>
				</td>
				<td class="uppercase">
				    <c:choose>
					<c:when test="${(fn:indexOf(transaction.billOfLadding,'04-')>-1
							|| (not empty transaction.manifestFlag && transaction.manifestFlag=='R'))}">
						<a href="javascript:getFreightInvoice('${transaction.transactionId}')">
						    ${fn:replace(fn:substring(transaction.billOfLadding,fn:indexOf(transaction.billOfLadding,'04-'),fn:length(transaction.billOfLadding)),'-','')}
						</a>
					</c:when>
					<c:otherwise>${transaction.billOfLadding}</c:otherwise>
				    </c:choose>
				</td>
				<td>${transaction.formattedDate}</td>
				<c:choose>
				    <c:when test="${fn:startsWith(transaction.formattedAmount,'-')}">
					<td style="text-align:right;" class="red">(${fn:replace(transaction.formattedAmount,'-','')})</td>
				    </c:when>
				    <c:otherwise>
					<td style="text-align:right;">${transaction.formattedAmount}</td>
				    </c:otherwise>
				</c:choose>
				<td>${transaction.age}</td>
				<c:choose>
				    <c:when test="${fn:startsWith(transaction.formattedBalance,'-')}">
					<td style="text-align:right;" class="red">(${fn:replace(transaction.formattedBalance,'-','')})</td>
				    </c:when>
				    <c:otherwise>
					<td style="text-align:right;">${transaction.formattedBalance}</td>
				    </c:otherwise>
				</c:choose>
				<td>${transaction.formattedArrivalDate}</td>
				<td>${transaction.blCreatedBy}</td>
				<c:if test="${roleDuty.creditHolder && canEdit}">
				    <td>
					<input type="checkbox" class="removeFromHoldIds" onclick="onclickRemoveFromHold(this,'${transaction.transactionId}')"/>
				    </td>
				</c:if>
				<td>
				    <c:choose>
					<c:when test="${transaction.creditHold!=commonConstants.YES && canEdit && roleDuty.creditHolder}">
					    <input type="checkbox" class="putOnHoldIds" onclick="onclickPutOnHold(this,'${transaction.transactionId}')"/>
					</c:when>
					<c:otherwise>
					    <img alt="" src="${path}/img/icons/ok.gif"/>
					</c:otherwise>
				    </c:choose>
				</td>
				<td>
				    <c:if test="${transaction.noCredit==commonConstants.YES}">
					<img alt="" src="${path}/img/icons/ok.gif"/>
				    </c:if>
				</td>
				<td>
				    <c:if test="${transaction.pastDue==commonConstants.ON}">
					<img alt="" src="${path}/img/icons/ok.gif"/>
				    </c:if>
				</td>
				<td>
				    <c:if test="${transaction.overLimit==commonConstants.ON}">
					<img alt="" src="${path}/img/icons/ok.gif"/>
				    </c:if>
				</td>
				<td>
				    <c:choose>
					<c:when test="${transaction.savedInBatches=='Y'}">
					    <img alt="Show Transactions" src="${path}/images/currency-red.gif" border="0"
						 title="Show Transactions" onclick="showTransactionHistory('${transaction.transactionId}')"/>
					</c:when>
					<c:otherwise>
					    <img alt="Show Transactions" src="${path}/img/icons/currency.gif" border="0"
						 title="Show Transactions" onclick="showTransactionHistory('${transaction.transactionId}')"/>
					</c:otherwise>
				    </c:choose>
				    <c:if test="${canEdit}">
					<c:if test="${not empty transaction.billOfLadding && fn:trim(transaction.billOfLadding)!='' 
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
					<img alt="Notes" src="${path}/img/icons/info1.gif" border="0"
					     title="Notes" onclick="showInvoiceNotes('${transaction.transactionId}')"/>
				    </c:if>
				    <input type="hidden" name="transactionIds" id="transactionIds${status.index}" value="${transaction.transactionId}"/>
				    <input type="hidden" id="customerNumber${transaction.transactionId}" value="${transaction.customerNumber}"/>
				    <input type="hidden" id="billOfLadding${transaction.transactionId}" value="${transaction.billOfLadding}"/>
				    <input type="hidden" id="correctionNotice${transaction.transactionId}" value="${transaction.correctionNotice}"/>
				    <input type="hidden" id="manifestFlag${transaction.transactionId}" value="${transaction.manifestFlag}"/>
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
	<c:otherwise>No receivables found</c:otherwise>
    </c:choose>
</div>