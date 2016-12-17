<%-- 
    Document   : searchResults
    Created on : Sep 23, 2013, 11:21:49 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${not empty arInquiryForm.results}">
        <c:if test="${not empty arInquiryForm.emailIds}">
            <c:set var="emailIds" value="${fn:split(arInquiryForm.emailIds, ',')}"/>
        </c:if>
        <div id="result-header" class="table-banner green">
            <div class="float-left">
                <span>Change View</span>
                <span>
                    <html:select property="accountType" styleId="accountType" styleClass="dropdown" onchange="changeView()">
                        <html:option value="">Select View</html:option>
                        <html:option value="shipper">Shipper</html:option>
                        <html:option value="consignee">Consignee</html:option>
                        <html:option value="forwarder">Forwarder</html:option>
                        <html:option value="agent">Agent</html:option>
                        <html:option value="third party">Third Party</html:option>
                    </html:select>
                </span>
            </div>
            <div class="float-right">
                <div class="float-left">
                    <c:choose>
                        <c:when test="${arInquiryForm.totalRows gt arInquiryForm.selectedRows}">
                            ${arInquiryForm.selectedRows} records displayed. ${arInquiryForm.totalRows} records found.
                        </c:when>
                        <c:when test="${arInquiryForm.selectedRows gt 1}">${arInquiryForm.selectedRows} records found.</c:when>
                        <c:otherwise>1 record found.</c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${arInquiryForm.totalPages gt 1 and arInquiryForm.selectedPage gt 1}">
                    <a title="First page" href="javascript: gotoPage('1')">
                        <img alt="First page" title="First page" src="${path}/images/first.png"/>
                    </a>
                    <a title="Previous page" href="javascript: gotoPage('${arInquiryForm.selectedPage-1}')">
                        <img alt="Previous page" title="Previous page" src="${path}/images/prev.png"/>
                    </a>
                </c:if>
                <c:if test="${arInquiryForm.totalPages gt 1}">
                    <select id="selectedPageNo" class="dropdown float-left">
                        <c:forEach begin="1" end="${arInquiryForm.totalPages}" var="selectedPage">
                            <c:choose>
                                <c:when test="${arInquiryForm.selectedPage eq selectedPage}">
                                    <option value="${selectedPage}" selected>${selectedPage}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${selectedPage}">${selectedPage}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                    <a href="javascript: gotoSelectedPage()">
                        <img alt="Goto Page" title="Goto Page" src="${path}/images/go.jpg"/>
                    </a>
                </c:if>
                <c:if test="${arInquiryForm.totalPages gt arInquiryForm.selectedPage}">
                    <a title="Next page" href="javascript: gotoPage('${arInquiryForm.selectedPage + 1}')">
                        <img alt="Next Page" title="Next page" src="${path}/images/next.png"/>
                    </a>
                    <a title="Last page" href="javascript: gotoPage('${arInquiryForm.totalPages}')">
                        <img alt="Last Page" title="Last page" src="${path}/images/last.png"/>
                    </a>
                </c:if>
            </div>
        </div>
        <div class="result-container">
            <table width="100%" cellpadding="0" cellspacing="1" class="display-table" style="table-layout: fixed;">
                <colgroup>
                    <col span="1" style="width: 80px">
                    <col span="1" style="width: 150px">
                    <col span="1" style="width: 20px">
                    <c:if test="${not empty arInquiryForm.accountType}">
                        <col span="1" style="width: 80px">
                        <col span="1" style="width: 120px">
                    </c:if>
                    <col span="1" style="width: 25px">
                    <col span="1" style="width: 55px">
                    <col span="1" style="width: 125px">
                    <col span="1" style="width: 100px">
                    <col span="1" style="width: 125px">
                    <col span="1" style="width: 110px">
                    <col span="1" style="width: 60px">
                    <col span="1" style="width: 30px">
                    <col span="1" style="width: 40px">
                    <col span="1" style="width: 60px">
                    <c:if test="${writeMode}">
                        <col span="1" style="width: 45px">
                        <col span="1" style="width: 35px">
                    </c:if>
                    <col span="1" style="width: 125px">
                </colgroup>
                <thead>
                    <tr>
                        <th>
                            <a href="javascript:doSort('customer_number')">Customer #</a>
                        </th>
                        <th>
                            <a href="javascript:doSort('customer_name')">Customer Name</a>
                        </th>
                        <th>
                            <a href="javascript:doSort('bill_to')">Bill To</a>
                        </th>
                        <c:if test="${not empty arInquiryForm.accountType}">
                            <c:choose>
                                <c:when test="${arInquiryForm.accountType eq 'shipper'}">
                                    <c:set var="tpNumber" value="Shipper #"/>
                                    <c:set var="tpName" value="Shipper Name"/>
                                </c:when>
                                <c:when test="${arInquiryForm.accountType eq 'consignee'}">
                                    <c:set var="tpNumber" value="Consignee #"/>
                                    <c:set var="tpName" value="Consignee Name"/>
                                </c:when>
                                <c:when test="${arInquiryForm.accountType eq 'forwarder'}">
                                    <c:set var="tpNumber" value="Forwarder #"/>
                                    <c:set var="tpName" value="Forwarder Name"/>
                                </c:when>
                                <c:when test="${arInquiryForm.accountType eq 'agent'}">
                                    <c:set var="tpNumber" value="Agent #"/>
                                    <c:set var="tpName" value="Agent Name"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="tpNumber" value="Third Party #"/>
                                    <c:set var="tpName" value="Third Party Name"/>
                                </c:otherwise>
                            </c:choose>
                            <th>
                                <a href="javascript:doSort('tp_number')">${tpNumber}</a>
                            </th>
                            <th>
                                <a href="javascript:doSort('tp_name')">${tpName}</a>
                            </th>
                        </c:if>
                        <th>
                            <a href="javascript:doSort('type')">Type</a>
                        </th>
                        <th>
                            <a href="javascript:doSort('invoice_date')">Date</a>
                        </th>
                        <th>
                            <a href="javascript:doSort('bl_number')">BL #</a>
                        </th>
                        <th>
                            <a href="javascript:doSort('invoice_number')">Invoice #</a>
                        </th>
                        <th>
                            <a href="javascript:doSort('voyage')">Voyage #</a>
                        </th>
                        <th>
                            <a href="javascript:doSort('reference')">Cust Ref #</a>
                        </th>
                        <th>
                            <a href="javascript:doSort('invoice_amount')">Amount</a>
                        </th>
                        <th>
                            <a href="javascript:doSort('invoice_date')">Age</a>
                        </th>
                        <th>
                            <a href="javascript:doSort('credit_hold')">Credit Hold</a>
                        </th>
                        <th>
                            <a href="javascript:doSort('invoice_balance')">Balance</a>
                        </th>
                        <c:if test="${writeMode}">
                            <th>
                                Exclude
                            </th>
                            <th>
                                Email
                            </th>
                        </c:if>
                        <th>
                            Action
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="zebra" value="odd"/>
                    <c:forEach var="row" items="${arInquiryForm.results}" varStatus="status">
                        <c:choose>
                            <c:when test="${row.type eq 'AP'}">
                                <c:set var="zebra" value="${zebra} red"/>
                            </c:when>
                            <c:when test="${row.type eq 'AC'}">
                                <c:set var="zebra" value="${zebra} blue"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="zebra" value="${zebra} black"/>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${fn:startsWith(row.invoiceNumber, 'NET SETT')}">
                                <c:set var="nsInvoice" value="true"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="nsInvoice" value="false"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="${zebra}">
                            <td>
                                <c:choose>
                                    <c:when test="${not empty row.correctionNotice and row.type eq 'AR'}">
                                        <span class="red font-14px" title="Correction Notice #${row.correctionNotice}">*</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span>&nbsp;</span>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${writeMode and roleDuty.arInquiryChangeCustomer and row.type eq 'AR'}">
                                        <input type="text" id="customerNumber${row.id}"
                                               class="newCustomerNumber textbox readonly width-70px" value="${row.customerNumber}" readonly/>
                                    </c:when>
                                    <c:otherwise>
                                        <span>${row.customerNumber}</span>
                                        <input type="hidden" id="customerNumber${row.id}" value="${row.customerNumber}"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${writeMode and roleDuty.arInquiryChangeCustomer and row.type eq 'AR'}">
                                        <input type="text" id="customerName${row.id}" class="newCustomerName textbox" value="${row.customerName}"/>
                                        <input type="hidden" class="newCustomerNameCheck" value="${row.customerName}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <span title="${row.customerName}">${str:abbreviate(row.customerName, 25)}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="align-center">${row.billTo}</td>
                            <c:if test="${not empty arInquiryForm.accountType}">
                                <td>${row.tpNumber}</td>
                                <td>
                                    <span title="${row.tpName}">${str:abbreviate(row.tpName, 15)}</span>
                                </td>
                            </c:if>
                            <td class="align-center">${row.type}</td>
                            <td>${row.invoiceDate}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${row.type eq 'AR' and fn:containsIgnoreCase(row.source, 'FCL')}">
                                        <c:set var="drcpt" value="${row.dockReceipt}"/>
                                        <c:set var="fileNo" value="${row.dockReceipt}${fn:substringAfter(row.blNumber, drcpt)}"/>
                                        <c:set var="blNumber" value="${row.blNumber}"/>
                                        <c:set var="blId" value="${row.sourceId}"/>
                                        <c:set var="importFlag" value="${row.sourceType eq 'I'}"/>
                                        <a href="javascript: showFclBl('${fileNo}', '${blId}', '${blNumber}', '${importFlag}')">
                                            ${blNumber}
                                        </a>
                                    </c:when>
                                    <c:when test="${row.type eq 'AR' and fn:containsIgnoreCase(row.source, 'LCL')}">
                                        <c:set var="blNumber" value="${fn:substringBefore(row.blNumber, row.dockReceipt)}${row.dockReceipt}"/>
                                        <a href="javascript: showLcl('${row.dockReceipt}', '${row.sourceId}','${row.sourceType}')">${blNumber}</a>
                                    </c:when>
                                    <c:otherwise>
                                        ${row.blNumber}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${row.type eq 'AR' and row.source eq 'FCL' and (fn:contains(row.blNumber, '04-') or !fn:startsWith(fileNo, '04'))}">
                                        <a href="javascript: showFreightInvoice('${fileNo}', '${fileNo}', '${blNumber}', '${row.id}')">
                                            04${fileNo}
                                        </a>
                                    </c:when>
                                    <c:when test="${row.type eq 'AR' and row.source eq 'FCL'}">
                                        <a href="javascript: showFreightInvoice('${fileNo}', '${fileNo}', '${blNumber}', '${row.id}')">
                                            ${fileNo}
                                        </a>
                                    </c:when>
                                    <c:when test="${row.type eq 'AR' and row.source eq 'LCL' and fn:containsIgnoreCase(row.blNumber, 'CN')}">
                                        <c:set var="invoiceNumber" value="${row.dockReceipt}${fn:substringAfter(row.blNumber, row.dockReceipt)}"/>
                                        <c:set var="correctionNo" value="${fn:substringAfter(row.blNumber, 'CN')}"/>
                                        <a href="javascript: showLclCorrectionInvoice('${row.dockReceipt}', '${correctionNo}', '${row.sourceId}','${row.sourceType}')">
                                            ${invoiceNumber}
                                        </a>
                                    </c:when>
                                    <c:when test="${row.type eq 'AR' and row.source eq 'LCL'}">
                                        <a href="javascript: showStatusUpdate('${row.dockReceipt}', '${row.sourceId}', '${row.sourceType}')">
                                            ${row.dockReceipt}
                                        </a>
                                    </c:when>
                                    <c:when test="${row.type eq 'AR' and fn:containsIgnoreCase(row.source, 'INV')}">
                                        <a href="javascript: showArInvoice('${row.invoiceNumber}', '${row.arInvoiceId}')">${row.invoiceNumber}</a>
                                    </c:when>
                                    <c:when test="${row.type eq 'AP'}">
                                        <a href="javascript: showApInvoiceDetails('${row.customerNumber}', '${row.invoiceNumber}')">
                                            ${row.invoiceNumber}
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        ${row.invoiceNumber}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${row.voyage}</td>
                            <td>
                                <span title="${row.reference}">${str:abbreviate(row.reference, 20)}</span>
                            </td>
                            <c:choose>
                                <c:when test="${row.type eq 'AP' or row.type eq 'AC' or fn:startsWith(row.invoiceAmount, '-')}">
                                    <c:set var="amountColor" value="red"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="amountColor" value="blue"/>
                                </c:otherwise>
                            </c:choose>
                            <td class="${amountColor} amount">
                                <c:choose>
                                    <c:when test="${fn:startsWith(row.invoiceAmount, '-')}">
                                        <c:set var="amount" value="(${fn:replace(row.invoiceAmount, '-', '')})"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="amount" value="${row.invoiceAmount}"/>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${row.type eq 'AR' and !nsInvoice}">
                                        <a href="javascript: showCharges('${row.source}', '${row.id}')" class="${amountColor}">${amount}</a>
                                    </c:when>
                                    <c:otherwise>
                                        ${amount}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="align-right">${row.age}</td>
                            <td class="align-center">${row.creditHold}</td>
                            <c:choose>
                                <c:when test="${row.status eq 'AP' or row.status eq 'AC' or fn:startsWith(row.invoiceBalance, '-')}">
                                    <c:set var="balanceColor" value="red"/>
                                </c:when>
                                <c:when test="${row.status eq 'AC'}">
                                    <c:set var="balanceColor" value="blue"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="balanceColor" value="black"/>
                                </c:otherwise>
                            </c:choose>
                            <td class="${balanceColor} amount" id="balance${row.id}">
                                <c:choose>
                                    <c:when test="${fn:startsWith(row.invoiceBalance, '-')}">
                                        (${fn:replace(row.invoiceBalance, '-', '')})
                                    </c:when>
                                    <c:otherwise>
                                        ${row.invoiceBalance}
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${row.includedInBatch}">
                                    <span class="red">*</span>
                                </c:if>
                            </td>
                            <c:if test="${writeMode}">
                                <c:choose>
                                    <c:when test="${row.type eq 'AR'}">
                                        <c:set var="excludeDisabled" value=""/>
                                        <c:set var="emailDisabled" value=""/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="excludeDisabled" value="disabled"/>
                                        <c:set var="emailDisabled" value="disabled"/>
                                    </c:otherwise>
                                </c:choose>
                                <td class="align-center">
                                    <c:choose>
                                        <c:when test="${row.type eq 'AR' and not empty excludedIds and str:in(row.id, excludedIds)}">
                                            <input type="checkbox" class="excludedIds" value="${row.id}" checked/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" class="excludedIds" value="${row.id}" ${excludeDisabled}/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="align-center">
                                    <c:choose>
                                        <c:when test="${row.type eq 'AR' and not empty emailIds and str:in(row.id, emailIds)}">
                                            <input type="checkbox" class="emailIds" value="${row.id}" checked/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" class="emailIds" value="${row.id}" ${emailDisabled}/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:if>
                            <td>
                                <c:if test="${row.type eq 'AR'}">
                                    <img src="${path}/images/icons/info.png"
                                         title="More Information" onclick="showMoreInfo('${row.source}', '${row.id}');"/>
                                    <c:choose>
                                        <c:when test="${row.includedInBatch}">
                                            <img src="${path}/images/icons/currency_red.png"
                                                 title="Show Transactions" onclick="showTransactions('${row.source}', '${row.id}');"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${path}/images/icons/currency_blue.png"
                                                 title="Show Transactions" onclick="showTransactions('${row.source}', '${row.id}');"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${writeMode}">
                                    <c:choose>
                                        <c:when test="${row.manualNotes}">
                                            <img src="${path}/images/icons/notepad_green.png"
                                                 title="Notes" onclick="showInvoiceNotes('${row.noteModuleId}', '${row.id}');"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${path}/images/icons/notepad_yellow.png"
                                                 title="Notes" onclick="showInvoiceNotes('${row.noteModuleId}', '${row.id}');"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:if test="${row.type ne 'GL' and (not roleDuty.viewAccountingScanAttach 
                                                  or (roleDuty.viewAccountingScanAttach and not row.overhead))}">
                                        <c:choose>
                                            <c:when test="${row.uploaded}">
                                                <img src="${path}/images/icons/upload-green.png" 
                                                     title="Upload Invoice" onclick="uploadInvoice('${row.id}');"/>
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${path}/images/icons/upload-yellow.png" 
                                                     title="Upload Invoice" onclick="uploadInvoice('${row.id}');"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                    <c:if test="${roleDuty.arInquiryMakeAdjustments 
                                                  and row.type eq 'AR'
                                                  and row.invoiceBalance ne '0.00'
                                                  and not row.includedInBatch
                                                  and (not fn:startsWith(row.invoiceNumber, 'NET SETT')
                                                  and not fn:containsIgnoreCase(row.blNumber, 'ON ACCOUNT')
                                                  and not fn:containsIgnoreCase(row.invoiceNumber, 'PRE PAYMENT'))}">
                                        <fmt:parseNumber var="balance" type="number" value="${fn:replace(row.invoiceBalance, ',', '')}"/>
                                        <c:if test="${balance ge -adjustmentThreshold and balance le adjustmentThreshold}">
                                            <img src="${path}/images/icons/adjustment.gif" 
                                                 title="Adjust" onclick="doAdjustment('${row.id}');" id="adjustment${row.id}"/>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${row.type eq 'AR'}">
                                        <c:choose>
                                            <c:when test="${fn:containsIgnoreCase(row.source, 'INV')}">
                                                <c:set var="invoice" value="${row.invoiceNumber}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="invoice" value="${row.blNumber}"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${row.status eq 'DISPUTE'}">
                                                <img src="${path}/images/icons/alphabets/d_red.png" id="dispute${row.id}"
                                                     title="Undispute Invoice" onclick="undisputeInvoice('${row.id}', '${invoice}');"/>
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${path}/images/icons/alphabets/d_green.png" id="dispute${row.id}" 
                                                     title="Dispute Invoice" onclick="disputeInvoice('${row.id}', '${invoice}');"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </c:if>
                                <c:if test="${nsInvoice}">
                                    <img src="${path}/images/icons/excel.png" title="Export NS Invoice" onclick="createNsInvoice('${row.id}', 'xlsx');"/>
                                    <img src="${path}/images/icons/pdf.png" title="Print NS Invoice" onclick="createNsInvoice('${row.id}', 'pdf');"/>
                                </c:if>
                                <c:choose>
                                    <c:when test="${row.balanceMatches eq true}">
                                        <img src="${path}/images/icons/alphabets/p_green.png" title="Payments" onclick="showPayments('${row.sourceId}', '${row.id}');"/>
                                    </c:when>
                                    <c:when test="${row.balanceMatches eq false}">
                                        <img src="${path}/images/icons/alphabets/p_red.png" title="Payments" onclick="showPayments('${row.sourceId}', '${row.id}');"/>
                                    </c:when>
                                </c:choose>
                                <input type="hidden" id="blNumber${row.id}" value="${row.blNumber}"/>
                                <input type="hidden" id="invoiceNumber${row.id}" value="${row.invoiceNumber}"/>
                                <input type="hidden" id="dockReceipt${row.id}" value="${row.dockReceipt}"/>
                                <input type="hidden" id="id${row.id}" class="id" value="${row.id}"/>
                                <input type="hidden" id="noteRefId${row.id}" class="noteRefId" value="${row.noteRefId}"/>
                                <input type="hidden" id="documentId${row.id}" class="documentId" value="${row.documentId}"/>
                                <input type="hidden" id="correctionNotice${row.id}" value="${row.correctionNotice}"/>
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
        <div class="table-banner green">No records found</div>
    </c:otherwise>
</c:choose>