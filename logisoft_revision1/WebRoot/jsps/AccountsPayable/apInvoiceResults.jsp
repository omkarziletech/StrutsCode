<%-- 
    Document   : apInvoiceResults
    Created on : May 9, 2012, 9:25:36 PM
    Author     : logiware
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>      
<div align="right" id="pager" class="pagebanner" style="padding-right: 15px;">
    <div style="float:right">
        <c:if test="${apInvoiceform.noOfPages>0}">
            <div style="float:left">
                <c:choose>
                    <c:when test="${apInvoiceform.totalPageSize>apInvoiceform.noOfRecords}">
                        ${apInvoiceform.noOfRecords} out of ${apInvoiceform.totalPageSize} invoices displayed.
                    </c:when>
                    <c:when test="${apInvoiceform.noOfRecords>1}">
                        ${apInvoiceform.noOfRecords} invoices displayed.
                    </c:when>
                    <c:otherwise>1 invoice displayed.</c:otherwise>
                </c:choose>
            </div>
            <c:if test="${apInvoiceform.noOfPages>1 && apInvoiceform.pageNo>1}">
                <a title="First page" href="javascript: gotoPage('1')">
                    <img alt="First" src="${path}/images/first.png" border="0"/>
                </a>
                <a title="Previous page" href="javascript: gotoPage('${apInvoiceform.pageNo-1}')">
                    <img alt="Previous" src="${path}/images/prev.png" border="0"/>
                </a>
            </c:if>
            <c:if test="${apInvoiceform.noOfPages>1}">
                <select id="selectedPageNo" class="textlabelsBoldForTextBox" style="float:left;">
                    <c:forEach begin="1" end="${apInvoiceform.noOfPages}" var="pageNo">
                        <c:choose>
                            <c:when test="${apInvoiceform.pageNo!=pageNo}">
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
            <c:if test="${apInvoiceform.noOfPages>apInvoiceform.pageNo}">
                <a title="Next page" href="javascript: gotoPage('${apInvoiceform.pageNo+1}')">
                    <img alt="First" src="${path}/images/next.png" border="0"/>
                </a>
                <a title="Last page" href="javascript: gotoPage('${apInvoiceform.noOfPages}')">
                    <img alt="Previous" src="${path}/images/last.png" border="0"/>
                </a>
            </c:if>
        </c:if>
    </div>
</div>

<div style="width:100%;float:left;">
    <c:choose>
        <c:when test="${not empty apInvoiceform.apInvoices}">
            <div class="scrolldisplaytable">
                <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" id="apInvoice" style="border: 1px solid white">
                    <thead>
                        <c:choose>
                            <c:when test="${apInvoiceform.orderBy=='desc'}"><c:set var="orderBy" value="asc"/></c:when>
                            <c:otherwise><c:set var="orderBy" value="desc"/></c:otherwise>
                        </c:choose>
                        <tr>
                        <th><a href="javascript:doSort('customer_name','${orderBy}')">Customer Name</a></th>
                        <th><a href="javascript:doSort('account_number','${orderBy}')">Account #</a></th>
                        <th><a href="javascript:doSort('invoice_number','${orderBy}')">Invoice #</a></th>
                        <th style="text-align: right"><a href="javascript:doSort('invoice_amount','${orderBy}')">Invoice Amount</a></th>
                        <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="zebra" value="odd"/>
                        <c:forEach var="apInvoice" items="${apInvoiceform.apInvoices}">
                            <tr class="${zebra}">
                                <td class="uppercase">${apInvoice.custname}</td>
                                <td class="uppercase">${apInvoice.custno}</td>
                                <td class="uppercase">${apInvoice.invoicenumber}</td>
                                <c:choose>
                                    <c:when test="${fn:startsWith(apInvoice.invoiceAmount,'-')}">
                                        <td style="text-align:right;" class="red">(${fn:replace(apInvoice.invoiceAmount,'-','')})</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td style="text-align:right;">${apInvoice.invoiceAmount}</td>
                                    </c:otherwise>
                                </c:choose>
                                <td>
                                    <c:if test="${canEdit}">
                                        <img src="${path}/img/icons/edit.gif" border="0" 
                                             alt="Edit Invoice" title="Edit Invoice" onclick="editApInvoice(${apInvoice.id})"/>
                                    </c:if>
                                    <img src="${path}/img/icons/pubserv.gif" border="0"
                                         alt="View Invoice" title="View Invoice" onclick="viewApInvoice(${apInvoice.id})"/>
                                </td>
                            </tr>
                            <c:choose>
                                <c:when test="${zebra eq 'odd'}">
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
        <c:otherwise>No invoices found</c:otherwise>
    </c:choose>
</div>
