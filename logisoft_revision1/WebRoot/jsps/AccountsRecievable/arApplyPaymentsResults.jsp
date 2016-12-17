<%--
    Document   : arApplyPaymentsResults
    Created on : May 26, 2011, 2:20:58 AM
    Author     : Lakshmi Naryanan
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div align="right" id="pager" class="pagebanner" style="padding-right: 15px;margin: 4px 0 0 0">
    <div style="float:right">
        <div style="float:left">
            <c:choose>
                <c:when test="${arBatchForm.totalPageSize>arBatchForm.noOfRecords}">
                    ${arBatchForm.noOfRecords} out of ${arBatchForm.totalPageSize} invoices displayed.
                </c:when>
                <c:when test="${arBatchForm.noOfRecords>1}">${arBatchForm.noOfRecords} invoices displayed.</c:when>
                <c:otherwise>${arBatchForm.noOfRecords} invoice displayed.</c:otherwise>
            </c:choose>
        </div>
        <c:if test="${arBatchForm.noOfPages>1 && arBatchForm.pageNo>1}">
            <a title="First page" href="javascript: gotoPage('1')" tabindex="-1">
                <img alt="First" src="${path}/images/first.png" border="0"/>
            </a>
            <a title="Previous page" href="javascript: gotoPage('${arBatchForm.pageNo-1}')" tabindex="-1">
                <img alt="Previous" src="${path}/images/prev.png" border="0"/>
            </a>
        </c:if>
        <c:if test="${arBatchForm.noOfPages>1}">
            <select id="selectedPageNo" class="dropdown_accounting" style="float:left;" tabindex="-1">
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
            <a href="javascript: gotoSelectedPage()" tabindex="-1">
                <img alt="Go" src="${path}/images/go.jpg" border="0"/>
            </a>
        </c:if>
        <c:if test="${arBatchForm.noOfPages>arBatchForm.pageNo}">
            <a title="Next page" href="javascript: gotoPage('${arBatchForm.pageNo+1}')" tabindex="-1">
                <img alt="First" src="${path}/images/next.png" border="0"/>
            </a>
            <a title="Last page" href="javascript: gotoPage('${arBatchForm.noOfPages}')" tabindex="-1">
                <img alt="Previous" src="${path}/images/last.png" border="0"/>
            </a>
        </c:if>
    </div>
</div>
<div style="width:100%;float:left;">
    <div class="scrolldisplaytable">
        <table width="100%" cellpadding="0" cellspacing="2" class="displaytagstyleNew" id="applypayments" style="border: 1px solid white">
            <thead>
                <c:choose>
                    <c:when test="${arBatchForm.orderBy=='desc'}">
                        <c:set var="orderBy" value="asc"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="orderBy" value="desc"/>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <th><a href="javascript:doSort('tbl.acct_no','${orderBy}')" tabindex="-1">Customer#</a></th>
                    <th><a href="javascript:doSort('tbl.acct_name','${orderBy}')" tabindex="-1">Customer Name</a></th>
                    <th><a href="javascript:doSort('tbl.ecu_designation','${orderBy}')" tabindex="-1">ECU designation</a></th>
                    <th><a href="javascript:doSort('tbl.Customer_Reference_No','${orderBy}')" tabindex="-1">Cust Ref#</a></th>
                    <th><a href="javascript:doSort('tbl.Charge_Code','${orderBy}')" tabindex="-1">Charge/Cost code</a></th>
                    <th><a href="javascript:doSort('tbl.invoiceOrBl','${orderBy}')" tabindex="-1">Invoice/BL</a></th>
                    <th><a href="javascript:doSort('tbl.Voyage_No','${orderBy}')" tabindex="-1">Voyage</a></th>
                    <th><a href="javascript:doSort('tbl.dockReceipt','${orderBy}')" tabindex="-1">Dock Receipt</a></th>
                    <th><a href="javascript:doSort('tbl.transactionAmount','${orderBy}')" tabindex="-1">Amount</a></th>
                    <th><a href="javascript:doSort('tbl.balanceInProcess','${orderBy}')" tabindex="-1">Balance In Process</a></th>
                    <th><a href="javascript:doSort('tbl.Transaction_date','${orderBy}')" tabindex="-1">Date</a></th>
                    <th><a href="javascript:doSort('tbl.Transaction_type','${orderBy}')" tabindex="-1">Type</a></th>
                    <th>Pay Full</th>
                    <th>Paid Amount</th>
                    <th>Adjust</th>
                    <th>GL Account</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty arBatchForm.applypayments}">
                        <c:set var="zebra" value="odd"/>
                        <c:forEach var="invoice" items="${arBatchForm.applypayments}" varStatus="status">
                            <c:if test="${invoice.selected || not empty invoice.paidAmount}">
                                <c:set var="zebra" value="selected-row"/>
                            </c:if>
                            <c:if test="${invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCRUALS
                                          && fn:toLowerCase(invoice.status)==fn:toLowerCase(commonConstants.STATUS_DISPUTE)}">
                                <c:set var="zebra" value="disputed-row"/>
                            </c:if>
                            <c:choose>
                                <c:when test="${invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE 
                                                && fn:toLowerCase(invoice.status)==fn:toLowerCase(commonConstants.STATUS_READY_TO_PAY)}">
                                    <c:set var="zebra" value="${zebra} pink"/>
                                </c:when>
                                <c:when test="${invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE}">
                                    <c:set var="zebra" value="${zebra} red"/>
                                </c:when>
                                <c:when test="${invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCRUALS}">
                                    <c:set var="zebra" value="${zebra} blue"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="zebra" value="${zebra} black"/>
                                </c:otherwise>
                            </c:choose>
                            <tr class="${zebra}">
                                <td class="uppercase">
                                    ${invoice.customerNumber}
                                    <input type="hidden" class="customerNumber"
                                           id="customerNumber${invoice.transactionId}" value="${invoice.customerNumber}"/>
                                </td>
                                <td class="uppercase">
                                    <input type="hidden" class="customerName" value="${invoice.customerName}"/>
                                    <c:choose>
                                        <c:when test="${fn:length(invoice.customerName)>20}">
                                            <label title="${invoice.customerName}">
                                                ${fn:substring(invoice.customerName,0,20)}..
                                            </label>
                                        </c:when>
                                        <c:otherwise>${invoice.customerName}</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="uppercase">
                                  ${invoice.ecuDesignation}
                                </td>
                                <td class="uppercase">
                                    <c:choose>
                                        <c:when test="${fn:length(invoice.customerReference)>15}">
                                            <label title="${invoice.customerReference}">
                                                ${fn:substring(invoice.customerReference,0,15)}..
                                            </label>
                                        </c:when>
                                        <c:otherwise>${invoice.customerReference}</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="uppercase">${invoice.chargeCode}</td>
                                <td class="uppercase">
                                    <c:choose>
                                        <c:when test="${(fn:indexOf(invoice.invoiceOrBl,'04-')>-1
                                                        || (not empty invoice.manifestFlag && invoice.manifestFlag=='R'))
                                                        && invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE}">
                                                <a href="javascript:getFreightInvoice('${invoice.transactionId}', '${invoice.arInvoiceId}')" tabindex="-1">
                                                    ${invoice.invoiceOrBl}
                                                </a>
                                                <input type="hidden" class="invoiceOrBl"
                                                       id="invoiceOrBl${invoice.transactionId}" value="${invoice.invoiceOrBl}"/>
                                        </c:when>
                                        <c:when test="${invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCRUALS}">
                                            <c:choose>
                                                <c:when test="${fn:toLowerCase(invoice.status)!=fn:toLowerCase(commonConstants.STATUS_ASSIGN)
                                                                && fn:toLowerCase(invoice.status)!=fn:toLowerCase(commonConstants.STATUS_EDI_ASSIGNED)
                                                                && fn:toLowerCase(invoice.status)!=fn:toLowerCase(commonConstants.STATUS_DISPUTE)}">
                                                        <input type="text" id="invoiceOrBl${invoice.transactionId}" value="${invoice.invoiceOrBl}"
                                                               style="width: 100px" class="textlabelsBoldForTextBox invoiceOrBl" maxlength="100" tabindex="-1" onchange="onChangeInvoice(this)"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="text" id="invoiceOrBl${invoice.transactionId}" value="${invoice.invoiceOrBl}" readonly
                                                           style="width: 100px" class="textlabelsBoldForTextBoxDisabledLook invoiceOrBl" maxlength="100" tabindex="-1"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:when test="${invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE}">
                                            <a href="javascript:showApInvoiceDetails('${invoice.transactionId}')">
                                                ${invoice.invoiceOrBl}
                                            </a>
                                            <input type="hidden" class="invoiceOrBl"
                                                   id="invoiceOrBl${invoice.transactionId}" value="${invoice.invoiceOrBl}"/>
                                        </c:when>
                                        <c:otherwise>
                                            ${invoice.invoiceOrBl}
                                            <input type="hidden" class="invoiceOrBl"
                                                   id="invoiceOrBl${invoice.transactionId}" value="${invoice.invoiceOrBl}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <input type="hidden" id="correctionNotice${invoice.transactionId}" value="${invoice.correctionNotice}"/>
                                    <input type="hidden" id="manifestFlag${invoice.transactionId}" value="${invoice.manifestFlag}"/>
                                </td>
                                <td class="uppercase">${invoice.voyage}</td>
                                        <td class="uppercase">${invoice.dockReceipt}</td>
                                <c:choose>
                                    <c:when test="${fn:startsWith(invoice.formattedAmount,'-')}">
                                        <c:set var="invoiceAmount" value="(${fn:replace(invoice.formattedAmount,'-','')})"/>
                                        <c:set var="amountColor" value="red"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="invoiceAmount" value="${invoice.formattedAmount}"/>
                                        <c:choose>
                                            <c:when test="${invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE}">
                                                <c:set var="amountColor" value="blue"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="amountColor" value="black"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                                <td class="${amountColor}" style="text-align:right;">
                                    <c:choose>
                                        <c:when test="${invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE}">
                                            <a href="javascript:showInvoiceOrBlDetails('${invoice.transactionId}')"
                                               class="${amountColor}" tabindex="-1">
                                                ${invoiceAmount}
                                            </a>
                                        </c:when>
                                        <c:otherwise>${invoiceAmount}</c:otherwise>
                                    </c:choose>
                                    <input type="hidden" class="transactionAmount" value="${invoiceAmount}"/>
                                </td>
                                <c:choose>
                                    <c:when test="${fn:startsWith(invoice.formattedBalanceInProcess,'-')}">
                                        <c:set var="balanceInProcess" value="(${fn:replace(invoice.formattedBalanceInProcess,'-','')})"/>
                                        <c:set var="balanceInProcessColor" value="red"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="balanceInProcess" value="${invoice.formattedBalanceInProcess}"/>
                                        <c:set var="balanceInProcessColor" value="black"/>
                                    </c:otherwise>
                                </c:choose>
                                <td class="${balanceInProcessColor}" style="text-align:right;">
                                    <label class="balanceInProcessLbl">${balanceInProcess}</label>
                                    <input type="hidden" class="balanceInProcess"
                                           id="balanceInProcess${invoice.transactionId}" value="${invoice.balanceInProcess}"/>
                                </td>
                                <td>${invoice.formattedDate}</td>
                                <td>
                                    ${invoice.transactionType}
                                    <input type="hidden" class="transactionType" id="transactionType${invoice.transactionId}" 
                                           value="${invoice.transactionType}"/>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${invoice.selected}">
                                            <input type="checkbox" value="${invoice.transactionId}" onclick="checkDisputeStatus('${invoice.transactionId}')"  class="selectedIds" checked/>
                                        </c:when>
                                        <c:when test="${not empty invoice.paidAmount
                                                        || (invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCRUALS
                                                        && (fn:toLowerCase(invoice.status)==fn:toLowerCase(commonConstants.STATUS_ASSIGN)
                                                        || fn:toLowerCase(invoice.status)==fn:toLowerCase(commonConstants.STATUS_EDI_ASSIGNED)
                                                        || fn:toLowerCase(invoice.status)==fn:toLowerCase(commonConstants.STATUS_DISPUTE)))
                                                        || (invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE 
                                                        && fn:toLowerCase(invoice.status)==fn:toLowerCase(commonConstants.STATUS_READY_TO_PAY))}">
                                                <input type="checkbox" value="${invoice.transactionId}" class="selectedIds" style="display: none" onclick="checkDisputeStatus('${invoice.transactionId}')"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" value="${invoice.transactionId}" class="selectedIds" onclick="checkDisputeStatus('${invoice.transactionId}')"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <input type="hidden" value="${invoice.transactionId}" class="transactionId"/>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${invoice.selected
                                                        || invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCRUALS
                                                        || invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE}">
                                                <input type="text" style="width: 70px" value="${invoice.paidAmount}"
                                                       class="textlabelsBoldForTextBoxDisabledLook paidAmount" readonly tabindex="-1"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="text" maxlength="11" style="width: 70px"
                                                   value="${invoice.paidAmount}" class="textlabelsBoldForTextBox paidAmount"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <input type="hidden" class="originalPaidAmount" value="${invoice.paidAmount}"/>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE}">
                                            <input type="text" maxlength="11" value="${invoice.adjustAmount}"
                                                   style="width: 70px" class="textlabelsBoldForTextBox adjustAmount"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="text" style="width: 70px"
                                                   class="textlabelsBoldForTextBoxDisabledLook adjustAmount" readonly tabindex="-1"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <input type="hidden" class="originalAdjustAmount" value="${invoice.adjustAmount}"/>
                                <td>
                                    <c:choose>
                                        <c:when test="${invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE}">
                                            <input type="text" name="glAccount${status.count}" id="glAccount${status.count}" maxlength="10"
                                                   value="${invoice.glAccount}" style="width: 70px" class="textlabelsBoldForTextBox glAccount"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="text" name="glAccount${status.count}" id="glAccount${status.count}"
                                                   style="width: 70px" class="textlabelsBoldForTextBoxDisabledLook glAccount" readonly tabindex="-1"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <input type="hidden" id="glAccount${status.count}Check" value="${invoice.glAccount}"/>
                                </td>
                                <td class="image-td">
                                    <div style="width: 90px;float: left">
                                        <c:if test="${invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCRUALS
                                                      && fn:toLowerCase(invoice.status)!=fn:toLowerCase(commonConstants.STATUS_ASSIGN)
                                                      && fn:toLowerCase(invoice.status)!=fn:toLowerCase(commonConstants.STATUS_EDI_ASSIGNED)
                                                      && fn:toLowerCase(invoice.status)!=fn:toLowerCase(commonConstants.STATUS_DISPUTE)}">
                                            <c:choose>
                                                <c:when test="${fn:toLowerCase(invoice.status)==fn:toLowerCase(commonConstants.STATUS_INACTIVE)}">
                                                    <img alt="" title="Inactive Accruals" src="${path}/images/locked.png"
                                                         border="0" onclick="activateAccruals('${invoice.transactionId}')"
                                                         style="display: block;" class="lock"/>
                                                    <img alt="" title="Active Accruals" src="${path}/images/unlocked.png"
                                                         border="0" onclick="inActivateAccruals('${invoice.transactionId}')"
                                                         style="display: none;" class="unlock"/>
                                                </c:when>
                                                <c:when test="${fn:toLowerCase(invoice.status)!=fn:toLowerCase(commonConstants.STATUS_PENDING)}">
                                                    <img alt="" title="Inactive Accruals" src="${path}/images/locked.png"
                                                         border="0" onclick="activateAccruals('${invoice.transactionId}')"
                                                         style="display: none;" class="lock"/>
                                                    <img alt="" title="Active Accruals" src="${path}/images/unlocked.png"
                                                         border="0" onclick="inactivateAccruals('${invoice.transactionId}')"
                                                         style="display: block;" class="unlock"/>
                                                </c:when>
                                            </c:choose>
                                            <img alt="" src="${path}/img/icons/edit.gif" border="0"
                                                 onclick="showUpdateAccrual('${invoice.transactionId}')"/>
                                        </c:if>
                                        <c:if test="${invoice.transactionType==commonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE}">
                                            <c:set var="arId" value="${fn:replace(invoice.transactionId,commonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE,'')}"/>
                                            <img alt="Show Transactions" src="${path}/img/icons/currency.gif" border="0" title="Show Transactions"
                                                 onclick="showTransactionHistory('${arId}')"/>
                                        </c:if>
                                        <c:if test="${not empty invoice.invoiceOrBl && fn:trim(invoice.invoiceOrBl)!='' 
                                                      && (!roleDuty.viewAccountingScanAttach || (roleDuty.viewAccountingScanAttach && invoice.subType != 'Y'))}">
                                            <c:choose>
                                                <c:when test="${invoice.hasDocuments}">
                                                    <img alt="Scan/Attach" src="${path}/images/upload-green.gif" border="0"
                                                         title="Scan/Attach" onclick="showScanOrAttach('${invoice.transactionId}')"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img alt="Scan/Attach" src="${path}/img/icons/upload.gif" border="0"
                                                         title="Scan/Attach" onclick="showScanOrAttach('${invoice.transactionId}')"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                        <c:choose>
                                            <c:when test="${invoice.manualNotes}">
                                                <img alt="Notes" src="${path}/images/notepad_green.png" border="0" width="16px" height="16px"
                                                     title="Notes" onclick="showInvoiceNotes('${invoice.noteModuleId}', '${invoice.noteRefId}')"/>
                                            </c:when>
                                            <c:otherwise>
                                                <img alt="Notes" src="${path}/images/notepad_yellow.png" border="0" width="16px" height="16px"
                                                     title="Notes" onclick="showInvoiceNotes('${invoice.noteModuleId}', '${invoice.noteRefId}')"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:if test="${invoice.transactionType eq 'AR'}">
                                            <c:choose>
                                                <c:when test="${invoice.status eq 'Dispute'}">
                                                    <img src="${path}/images/icons/alphabets/d_red.png" id="dispute${arId}"
                                                         title="Undispute Invoice" onclick="undisputeInvoice('${arId}', '${invoice.invoiceOrBl}');"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${path}/images/icons/alphabets/d_green.png" id="dispute${arId}" 
                                                         title="Dispute Invoice" onclick="disputeInvoice('${arId}', '${invoice.invoiceOrBl}');"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </div>
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
                    </c:when>
                    <c:otherwise>
                        <tr><td style="font-weight: bold;">No invoices found</td></tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>