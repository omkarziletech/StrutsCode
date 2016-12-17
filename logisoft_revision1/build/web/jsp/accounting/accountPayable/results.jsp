<%-- 
    Document   : results
    Created on : Apr 8, 2013, 6:07:57 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>   
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${not empty accountPayableForm.results}">
        <div id="result-header" class="table-banner green">
            <div class="float-right">
                <div class="float-left">
                    <c:choose>
                        <c:when test="${accountPayableForm.totalRows>accountPayableForm.selectedRows}">
                            ${accountPayableForm.selectedRows} records displayed. ${accountPayableForm.totalRows} records found.
                        </c:when>
                        <c:when test="${accountPayableForm.selectedRows>1}">${accountPayableForm.selectedRows} records found.</c:when>
                        <c:otherwise>1 record found.</c:otherwise>
                    </c:choose>
                </div>
                <c:if test="${accountPayableForm.totalPages>1 && accountPayableForm.selectedPage>1}">
                    <a title="First page" href="javascript: gotoPage('1')">
                        <img alt="First page" title="First page" src="${path}/images/first.png"/>
                    </a>
                    <a title="Previous page" href="javascript: gotoPage('${accountPayableForm.selectedPage-1}')">
                        <img alt="Previous page" title="Previous page" src="${path}/images/prev.png"/>
                    </a>
                </c:if>
                <c:if test="${accountPayableForm.totalPages>1}">
                    <select id="selectedPageNo" class="dropdown float-left">
                        <c:forEach begin="1" end="${accountPayableForm.totalPages}" var="selectedPage">
                            <c:choose>
                                <c:when test="${accountPayableForm.selectedPage!=selectedPage}">
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
                <c:if test="${accountPayableForm.totalPages>accountPayableForm.selectedPage}">
                    <a title="Next page" href="javascript: gotoPage('${accountPayableForm.selectedPage+1}')">
                        <img alt="Next Page" title="Next page" src="${path}/images/next.png"/>
                    </a>
                    <a title="Last page" href="javascript: gotoPage('${accountPayableForm.totalPages}')">
                        <img alt="Last Page" title="Last page" src="${path}/images/last.png"/>
                    </a>
                </c:if>
            </div>
        </div>
        <div class="result-container">
            <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
                <thead>
                    <tr>
                        <th><a href="javascript:doSort('vendor_name')">Vendor Name</a></th>
                        <th class="width-100px"><a href="javascript:doSort('vendor_number')">Vendor Number</a></th>
                        <th class="width-130px"><a href="javascript:doSort('invoice_number')">Invoice/BL</a></th>
                        <th class="width-80px"><a href="javascript:doSort('invoice_date')">Invoice Date</a></th>
                        <th class="width-115px"><a href="javascript:doSort('credit_terms')">Terms</a></th>
                        <th class="width-80px"><a href="javascript:doSort('due_date')">Due Date</a></th>
                        <th class="width-30px"><a href="javascript:doSort('age')">Age</a></th>
                        <th class="width-75px"><a href="javascript:doSort('credit_hold')">Credit Hold</a></th>
                        <th class="width-100px"><a href="javascript:doSort('invoice_amount')">Invoice Amount</a></th>
                        <th class="width-100px"><a href="javascript:doSort('check_number')">Check Number</a></th>
                        <th class="width-45px"><a href="javascript:doSort('transaction_type')">Type</a></th>
                            <c:if test="${writeMode}">
                            <th class="width-30px">Pay</th>
                            <th class="width-30px">Hold</th>
                            <th class="width-50px">Delete</th>
                            </c:if>
                        <th class="width-80px">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="zebra" value="odd"/>
                    <c:forEach var="result" items="${accountPayableForm.results}" varStatus="status">
                        <c:choose>
                            <c:when test="${result.status == 'In Progress' || result.status == 'EDI In Progress'}">
                                <c:set var="zebra" value="${zebra} red"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="zebra" value="${zebra} black"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="${zebra}">
                            <td>
                                <label title="${result.vendorName}">${str:abbreviate(result.vendorName,30)}</label>
                            </td>
                            <td>${result.vendorNumber}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${result.transactionType != 'AR' && not empty result.invoiceNumber}">
                                        <c:set var="vendorNumber" value="${result.vendorNumber}"/>
                                        <c:set var="invoiceNumber" value="${result.invoiceNumber}"/>
                                        <c:set var="blNumber" value="${result.blNumber}"/>
                                        <a href="javascript:showInvoiceDetails('${vendorNumber}','${invoiceNumber}','AC')">
                                            ${result.invoiceOrBl}
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        ${result.invoiceOrBl}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${result.invoiceDate}</td>
                            <td>${result.creditTerms}</td>
                            <td>${result.dueDate}</td>
                            <td class="align-right">${result.age}</td>
                            <td class="align-center">
                                <c:if test="${result.creditHold == 'Y'}">
                                    <img src="${path}/images/icon/ok.gif"/>
                                </c:if>
                            </td>
                            <c:choose>
                                <c:when test="${result.status == 'In Progress' || result.status == 'EDI In Progress'
                                                || fn:contains(result.invoiceAmount,'-')}">
                                    <c:set var="invoiceAmountClass" value="red"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="invoiceAmountClass" value="black"/>
                                </c:otherwise>
                            </c:choose>
                            <td class="${invoiceAmountClass} amount">
                                <c:choose>
                                    <c:when test="${fn:contains(result.invoiceAmount,'-')}">
                                        (${fn:replace(result.invoiceAmount,'-','')})
                                    </c:when>
                                    <c:otherwise>
                                        ${result.invoiceAmount}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${result.checkNumber}</td>
                            <td class="align-center">${result.transactionType}</td>
                            <c:if test="${writeMode}">
                                <c:choose>
                                    <c:when test="${result.status == 'In Progress' || result.status == 'EDI In Progress'}">
                                        <c:set var="payDisabled" value="disabled"/>
                                        <c:set var="holdDisabled" value="disabled"/>
                                    </c:when>
                                    <c:when test="${result.transactionType == 'AR'}">
                                        <c:set var="payDisabled" value=""/>
                                        <c:set var="holdDisabled" value="disabled"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="payDisabled" value=""/>
                                        <c:set var="holdDisabled" value=""/>
                                    </c:otherwise>
                                </c:choose>
                                <td class="align-center">
                                    <c:choose>
                                        <c:when test="${result.payCheck eq true}">
                                            <input type="checkbox" class="pay" onclick="setPay(this)" value="${result.id}" ${payDisabled} checked="true"/>
                                        </c:when>
                                        <c:when test="${result.age >= result.creditDays}">
                                            <input type="checkbox" class="pay" onclick="setPay(this)" value="${result.id}" ${payDisabled}/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" class="noDueUpon" onclick="setPay(this)" value="${result.id}" ${payDisabled}/>
                                        </c:otherwise>
                                    </c:choose>
                                    <input type="hidden" class="invoiceAmount" value="${fn:replace(result.invoiceAmount,',','')}"/>
                                    <input type="hidden" class="vendors" value="${result.vendorName}"/>
                                </td>
                                <td class="align-center">
                                    <c:choose>
                                        <c:when test="${result.status == 'H'}">
                                            <input type="checkbox" class="hold" onclick="setHold(this)" value="${result.id}" checked/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" class="hold" onclick="setHold(this)" value="${result.id}" ${holdDisabled}/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="align-center">
                                    <c:set var="deleteDisabled" value="${result.deletable ? '' : 'disabled'}"/>
                                    <input type="checkbox" class="delete" onclick="setDelete(this)" value="${result.id}" ${deleteDisabled}/>
                                </td>
                            </c:if>
                            <td>
                                <c:if test="${result.transactionType == 'AC' && not empty result.invoiceNumber}">
                                    <c:set var="invoiceRefId" value="${result.vendorNumber}-${result.invoiceNumber}"/>
                                </c:if>
                                <c:choose>
                                    <c:when test="${result.manualNotes}">
                                        <img title="Notes" src="${path}/images/notepad_green.png"
                                             onclick="showNotes('${result.noteModuleId}', '${result.noteRefId}')"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img title="Notes" src="${path}/images/notepad_yellow.png"
                                             onclick="showNotes('${result.noteModuleId}', '${result.noteRefId}')"/>
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
        <div class="table-banner green" style="background-color: #D1E6EE;">No payables found</div>
    </c:otherwise>
</c:choose>