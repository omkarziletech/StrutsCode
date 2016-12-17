<%-- 
    Document   : glBatchResults
    Created on : Jul 25, 2011, 5:55:18 PM
    Author     : lakshh
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div align="right" id="pager" class="pagebanner" style="padding-right: 15px;">
    <div style="float:right">
        <c:if test="${glBatchForm.noOfPages>0}">
            <div style="float:left">
                <c:choose>
                    <c:when test="${glBatchForm.totalPageSize>glBatchForm.noOfRecords}">
                        ${glBatchForm.noOfRecords} out of ${glBatchForm.totalPageSize} batches displayed.
                    </c:when>
                    <c:when test="${glBatchForm.noOfRecords>1}">${glBatchForm.noOfRecords} batches displayed.</c:when>
                    <c:otherwise>1 batch displayed.</c:otherwise>
                </c:choose>
            </div>
            <c:if test="${glBatchForm.noOfPages>1 && glBatchForm.pageNo>1}">
                <a title="First page" href="javascript: gotoPage('1')">
                    <img alt="First" src="${path}/images/first.png" border="0"/>
                </a>
                <a title="Previous page" href="javascript: gotoPage('${glBatchForm.pageNo-1}')">
                    <img alt="Previous" src="${path}/images/prev.png" border="0"/>
                </a>
            </c:if>
            <c:if test="${glBatchForm.noOfPages>1}">
                <select id="selectedPageNo" class="textlabelsBoldForTextBox" style="float:left;">
                    <c:forEach begin="1" end="${glBatchForm.noOfPages}" var="pageNo">
                        <c:choose>
                            <c:when test="${glBatchForm.pageNo!=pageNo}">
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
            <c:if test="${glBatchForm.noOfPages>glBatchForm.pageNo}">
                <a title="Next page" href="javascript: gotoPage('${glBatchForm.pageNo+1}')">
                    <img alt="First" src="${path}/images/next.png" border="0"/>
                </a>
                <a title="Last page" href="javascript: gotoPage('${glBatchForm.noOfPages}')">
                    <img alt="Previous" src="${path}/images/last.png" border="0"/>
                </a>
            </c:if>
        </c:if>
    </div>
</div>
<div style="width:100%;float:left;">
    <c:choose>
        <c:when test="${not empty glBatchForm.glBatches}">
            <div class="scrolldisplaytable">
                <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" id="glBatch" style="border: 1px solid white">
                    <thead>
                        <c:choose>
                            <c:when test="${glBatchForm.orderBy=='desc'}"><c:set var="orderBy" value="asc"/></c:when>
                            <c:otherwise><c:set var="orderBy" value="desc"/></c:otherwise>
                        </c:choose>
                        <tr>
                        <th><a href="javascript:doSort('batchId','${orderBy}')">Batch Number</a></th>
                        <th><a href="javascript:doSort('description','${orderBy}')">Description</a></th>
                        <th><a href="javascript:doSort('subledgerType','${orderBy}')">Subledger Type</a></th>
                        <th><a href="javascript:doSort('batchType','${orderBy}')">Type</a></th>
                        <th><a href="javascript:doSort('status','${orderBy}')">Status</a></th>
                        <th><a href="javascript:doSort('period','${orderBy}')">Period</a></th>
                        <th style="text-align: right"><a href="javascript:doSort('b.total_debit','${orderBy}')">Debit</a></th>
                        <th style="text-align: right"><a href="javascript:doSort('b.total_credit','${orderBy}')">Credit</a></th>
                        <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="zebra" value="odd"/>
                        <c:forEach var="glBatch" items="${glBatchForm.glBatches}" varStatus="status">
                            <c:choose>
                                <c:when test="${glBatch.status==commonConstants.STATUS_DELETED}">
                                    <c:set var="zebra" value="${zebra} red"/>
                                </c:when>
                                <c:when test="${glBatch.status==commonConstants.STATUS_POSTED}">
                                    <c:set var="zebra" value="${zebra} blue"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="zebra" value="${zebra} black"/>
                                </c:otherwise>
                            </c:choose>
                            <tr class="${zebra}">
                            <td>${glBatch.id}</td>
                            <td class="uppercase">${glBatch.description}</td>
                            <td>${glBatch.subledgerType}</td>
                            <td>${glBatch.type}</td>
                            <td>${glBatch.status}</td>
                            <td>${glBatch.period}</td>
                            <c:choose>
                                <c:when test="${fn:startsWith(glBatch.debit,'-')}">
                                    <td style="text-align:right;" class="red">(${fn:replace(glBatch.debit,'-','')})</td>
                                </c:when>
                                <c:otherwise>
                                    <td style="text-align:right;">${glBatch.debit}</td>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${fn:startsWith(glBatch.credit,'-')}">
                                    <td style="text-align:right;" class="red">(${fn:replace(glBatch.credit,'-','')})</td>
                                </c:when>
                                <c:otherwise>
                                    <td style="text-align:right;">${glBatch.credit}</td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:if test="${glBatch.status!=commonConstants.STATUS_DELETED}">
                                    <c:choose>
                                        <c:when test="${glBatch.status==commonConstants.STATUS_POSTED}">
                                            <img alt="View Batch" title="View Batch"
                                                 src="${path}/images/more_details.png" border="0" onclick="viewBatch('${glBatch.id}')"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${path}/img/icons/edit.gif" border="0"
                                                 alt="Edit Batch" title="Edit Batch" onclick="editBatch('${glBatch.id}')"/>
                                            <img src="${path}/img/icons/send.gif" border="0"
                                                 alt="Post Batch" title="Post Batch" onclick="postBatch('${glBatch.id}')"/>
                                            <img src="${path}/img/icons/remove.gif" border="0"
                                                 alt="Void Batch" title="Void Batch" onclick="voidBatch('${glBatch.id}')"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <img alt="Print" title="Print"
                                         src="${path}/img/icons/printer.gif" border="0" onclick="printBatch('${glBatch.id}');"/>
                                    <img alt="Export to Excel" title="Export to Excel"
                                         src="${path}/images/excel.gif" border="0" onclick="exportBatch('${glBatch.id}');"/>
                                </c:if>
                                <c:if test="${canEdit}">
                                    <img alt="Show Notes" title="Show Notes"
                                         src="${path}/img/icons/info1.gif" border="0" onclick="showBatchNotes('${glBatch.id}')"/>
                                    <c:choose>
                                        <c:when test="${glBatch.uploaded}">
                                            <img alt="Scan/Attach" src="${path}/images/upload-green.gif"
                                                 title="Scan/Attach" onclick="showScanOrAttach('${glBatch.id}')"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img alt="Scan/Attach" src="${path}/img/icons/upload.gif"
                                                 title="Scan/Attach" onclick="showScanOrAttach('${glBatch.id}')"/>
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
        <c:otherwise>No batches found</c:otherwise>
    </c:choose>
</div>
