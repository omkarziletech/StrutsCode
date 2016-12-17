<%-- 
    Document   : disputeEmailLog
    Created on : Apr 18, 2012, 3:59:44 PM
    Author     : logiware
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="/WEB-INF/tlds/string.tld" prefix="string"%>

<div align="right" id="pager" class="pagebanner" style="padding-right: 15px;">
    <div style="float:right">
        <c:if test="${apReportsForm.noOfPages>0}">
            <div style="float:left">
                <c:choose>
                    <c:when test="${apReportsForm.totalPageSize>apReportsForm.noOfRecords}">
                        ${apReportsForm.noOfRecords} out of ${apReportsForm.totalPageSize} disputed emails displayed.
                    </c:when>
                    <c:when test="${apReportsForm.noOfRecords>1}">${apReportsForm.noOfRecords} disputed emails displayed.</c:when>
                    <c:otherwise>1 disputed email displayed.</c:otherwise>
                </c:choose>
            </div>
            <c:if test="${apReportsForm.noOfPages>1 && apReportsForm.pageNo>1}">
                <a title="First page" href="javascript: gotoPage('1')">
                    <img alt="First" src="${path}/images/first.png" border="0"/>
                </a>
                <a title="Previous page" href="javascript: gotoPage('${apReportsForm.pageNo-1}')">
                    <img alt="Previous" src="${path}/images/prev.png" border="0"/>
                </a>
            </c:if>
            <c:if test="${apReportsForm.noOfPages>1}">
                <select id="selectedPageNo" class="textlabelsBoldForTextBox" style="float:left;">
                    <c:forEach begin="1" end="${apReportsForm.noOfPages}" var="pageNo">
                        <c:choose>
                            <c:when test="${apReportsForm.pageNo!=pageNo}">
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
            <c:if test="${apReportsForm.noOfPages>apReportsForm.pageNo}">
                <a title="Next page" href="javascript: gotoPage('${apReportsForm.pageNo+1}')">
                    <img alt="First" src="${path}/images/next.png" border="0"/>
                </a>
                <a title="Last page" href="javascript: gotoPage('${apReportsForm.noOfPages}')">
                    <img alt="Previous" src="${path}/images/last.png" border="0"/>
                </a>
            </c:if>
        </c:if>
    </div>
</div>

<div style="width:100%;float:left;">
    <c:choose>
        <c:when test="${not empty apReportsForm.disputedEmailLogList}">
            <div class="scrolldisplaytable">
                <table width="100%" cellpadding="0" cellspacing="3" class="displaytagstyleNew" id="disputedEmail" style="border: 1px solid white">
                    <thead>
                        <c:choose>
                            <c:when test="${apReportsForm.orderBy=='desc'}"><c:set var="orderBy" value="asc"/></c:when>
                            <c:otherwise><c:set var="orderBy" value="desc"/></c:otherwise>
                        </c:choose>
                        <tr>
                        <th><a href="javascript:doSort('tp.acct_name','${orderBy}')">Vendor#</a></th>
                        <th><a href="javascript:doSort('tp.acct_no','${orderBy}')">Vendor Name</a></th>
                        <th><a href="javascript:doSort('mail.module_id','${orderBy}')">Invoice Number</a></th>
                        <th><a href="javascript:doSort('mail.from_name','${orderBy}')">From Address</a></th>
                        <th><a href="javascript:doSort('mail.to_address','${orderBy}')">To Address</a></th>
                        <th><a href="javascript:doSort('mail.cc_address','${orderBy}')">Cc Address</a></th>
                        <th><a href="javascript:doSort('mail.bcc_address','${orderBy}')">Bcc Address</a></th>
                        <th><a href="javascript:doSort('mail.subject','${orderBy}')">Subject</a></th>
                        <th><a href="javascript:doSort('mail.html_message','${orderBy}')">Html Message</a></th>
                        <th><a href="javascript:doSort('mail.status','${orderBy}')">Status</a></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="zebra" value="odd"/>
                        <c:forEach var="disputedEmail" items="${apReportsForm.disputedEmailLogList}">
                            <tr class="${zebra}">
                            <td class="uppercase">${disputedEmail.vendorNumber}</td>
                            <td class="uppercase">
                                <label title="${disputedEmail.vendorName}">
                                    <c:choose>
                                        <c:when test="${fn:length(disputedEmail.vendorName)>20}">
                                            ${fn:substring(disputedEmail.vendorName,0,20)}...
                                        </c:when>
                                        <c:otherwise>${disputedEmail.vendorName}</c:otherwise>
                                     </c:choose>    
                                </label>
                            </td>
                            <td class="uppercase">${disputedEmail.invoiceNumber}</td>
                            <td>${string:split(disputedEmail.fromAddress,15,'<br>')}</td>
                            <td>${string:split(disputedEmail.toAddress,15,'<br>')}</td>
                            <td>${string:split(disputedEmail.ccAddress,15,'<br>')}</td>
                            <td>${string:split(disputedEmail.bccAddress,15,'<br>')}</td>
                            <td>${string:split(disputedEmail.subject,15,'<br>')}</td>
                            <td>${string:split(fn:replace(disputedEmail.htmlMessage,'<br />',''),10,'<br>')}</td>
                            <td>${disputedEmail.status}</td>
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
        <c:otherwise>No disputed email found</c:otherwise>
    </c:choose>
</div>