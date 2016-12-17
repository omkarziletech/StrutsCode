<%-- 
    Document   : accountPayable
    Created on : Apr 7, 2013, 10:22:53 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account Payable</title>
        <script type="text/javascript">
            var path = "${path}";
        </script>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.datepick.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.datepick.js"></script>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/accountPayable.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.fileupload.js"></script>
        <script type="text/javascript" src="${path}/js/fileUpload/jquery.fileinput.js"></script>
        <link href="${path}/css/fileUpload/enhanced.css" type="text/css" rel="stylesheet"/>
        <c:set var="accessMode" value="1"/>
        <c:set var="writeMode" value="true" scope="request"/>
        <c:if test="${param.accessMode==0}">
            <c:set var="accessMode" value="0"/>
            <c:set var="writeMode" value="false" scope="request"/>
        </c:if>
    </head>
    <body>
        <%@include file="../../../jsps/preloader.jsp"%>
        <div id="body-container">
            <html:form action="/accounting/accountPayable?accessMode=${accessMode}" name="accountPayableForm" enctype="multipart/form-data"
                       styleId="accountPayableForm" type="com.logiware.accounting.form.AccountPayableForm" scope="request" method="post">
                <html:hidden property="action" styleId="action"/>
                <html:hidden property="limit" styleId="limit"/>
                <html:hidden property="selectedPage" styleId="selectedPage"/>
                <html:hidden property="sortBy" styleId="sortBy"/>
                <html:hidden property="orderBy" styleId="orderBy"/>
                <html:hidden property="toggled" styleId="toggled"/>
                <html:hidden property="payIds" styleId="payIds" value=""/>
                <html:hidden property="holdIds" styleId="holdIds" value=""/>
                <html:hidden property="deleteIds" styleId="deleteIds" value=""/>
                <c:if test="${not empty message}">
                    <div class="message">${message}</div>
                </c:if>
                <table class="table" style="margin: 0">
                    <tr>
                        <td class="label">
                            <label>Vendor Name</label>
                            <html:text property="vendorName" styleId="vendorName" styleClass="textbox"/>
                            <html:text property="vendorNumber" 
                                       styleId="vendorNumber" styleClass="textbox readonly" readonly="true" tabindex="-1"/>
                            <input type="hidden" name="vendorNameCheck" 
                                   id="vendorNameCheck" value="${accountPayableForm.vendorName}" class="hidden" maxlength="50"/>
                            <c:if test="${writeMode}">
                                <img src="${path}/images/icons/trading_partner.png" title="Go to Trading Partner"
                                     class="trading-partner" onclick="gotoTradingPartner('vendorName', 'vendorNumber');"/>
                            </c:if>
                            <input type="button" class="button" value="Search" onclick="search();" tabindex="-1"/>
                            <input type="button" class="button" value="Clear" onclick="clearAll();" tabindex="-1"/>
                            <input type="button" class="button" value="Print" onclick="createPdf();" tabindex="-1"/>
                            <input type="button" class="button" value="Export" onclick="createExcel();" tabindex="-1"/>
                            <div class="float-right">
                                <div class="float-left" style="padding: 4px 2px 0 0"><label>Choose Template</label></div>
                                <div class="float-left templateDiv"><input type="file"  name="importfile" id="file"/></div>
                                <div class="float-left" style="padding: 2px 0 0 2px">
                                    <input type="button" value="Import" class="button"   onclick="importExcel();" id="import"/></div>
                            </div>

                        </td>
                    </tr>
                    <c:if test="${not empty accountPayableForm.vendor}">
                        <tr>
                            <th class="toggle" onclick="toggle('vendor-container');">Vendor Details</th>
                        </tr>
                        <tr>
                            <td>
                                <div class="vendor-container">
                                    <table class="table" style="border: none;margin: 0">
                                        <tr>
                                            <td width="40%" style="vertical-align: top">
                                                <table class="label" cellspacing="2">
                                                    <tr>
                                                        <td class="align-right bold">Vendor Name :</td>
                                                        <td>${accountPayableForm.vendor.vendorName}</td>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">Address :</td>
                                                        <c:set var="newline" value="\n"/>
                                                        <td>${fn:replace(accountPayableForm.vendor.address,newline,'<br/>')}</td>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">Contact :</td>
                                                        <td>${accountPayableForm.vendor.contact}</td>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">Phone :</td>
                                                        <td>${accountPayableForm.vendor.phone}</td>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">Fax :</td>
                                                        <td>${accountPayableForm.vendor.fax}</td>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">Email :</td>
                                                        <td>${accountPayableForm.vendor.email}</td>
                                                    </tr>
                                                </table>
                                            </td>
                                            <td width="30%" style="vertical-align: top">
                                                <table class="label" cellspacing="2">
                                                    <tr>
                                                        <td class="align-right bold">Vendor Number :</td>
                                                        <td>${accountPayableForm.vendor.vendorNumber}</td>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">Credit Term :</td>
                                                        <td>${accountPayableForm.vendor.creditTerm}</td>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">Credit Limit :</td>
                                                        <c:choose>
                                                            <c:when test="${fn:contains(accountPayableForm.vendor.creditLimit,'-')}">
                                                                <td class="align-right red">
                                                                    (${fn:replace(accountPayableForm.vendor.creditLimit,'-','')})
                                                                </td>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td class="align-right black">${accountPayableForm.vendor.creditLimit}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">OutStanding Receivables :</td>
                                                        <c:choose>
                                                            <c:when test="${fn:contains(accountPayableForm.vendor.arAmount,'-')}">
                                                                <td class="align-right red">
                                                                    (${fn:replace(accountPayableForm.vendor.arAmount,'-','')})
                                                                </td>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td class="align-right black">${accountPayableForm.vendor.arAmount}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">Net Payable Amount :</td>
                                                        <c:choose>
                                                            <c:when test="${fn:contains(accountPayableForm.vendor.netAmount,'-')}">
                                                                <td class="align-right red">
                                                                    (${fn:replace(accountPayableForm.vendor.netAmount,'-','')})
                                                                </td>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td class="align-right black">${accountPayableForm.vendor.netAmount}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tr>
                                                </table>
                                            </td>
                                            <td width="30%" style="vertical-align: top">
                                                <table class="label" cellspacing="2">
                                                    <tr>
                                                        <td class="align-right bold">Sub Type :</td>
                                                        <td>${accountPayableForm.vendor.subType}</td>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">Current :</td>
                                                        <c:choose>
                                                            <c:when test="${fn:contains(accountPayableForm.vendor.age30Amount,'-')}">
                                                                <td class="align-right red">
                                                                    (${fn:replace(accountPayableForm.vendor.age30Amount,'-','')})
                                                                </td>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td class="align-right black">${accountPayableForm.vendor.age30Amount}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">30-60 Days :</td>
                                                        <c:choose>
                                                            <c:when test="${fn:contains(accountPayableForm.vendor.age60Amount,'-')}">
                                                                <td class="align-right red">
                                                                    (${fn:replace(accountPayableForm.vendor.age60Amount,'-','')})
                                                                </td>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td class="align-right black">${accountPayableForm.vendor.age60Amount}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">61-90 Days :</td>
                                                        <c:choose>
                                                            <c:when test="${fn:contains(accountPayableForm.vendor.age90Amount,'-')}">
                                                                <td class="align-right red">
                                                                    (${fn:replace(accountPayableForm.vendor.age90Amount,'-','')})
                                                                </td>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td class="align-right black">${accountPayableForm.vendor.age90Amount}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">&gt;90 Days :</td>
                                                        <c:choose>
                                                            <c:when test="${fn:contains(accountPayableForm.vendor.age91Amount,'-')}">
                                                                <td class="align-right red">
                                                                    (${fn:replace(accountPayableForm.vendor.age91Amount,'-','')})
                                                                </td>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td class="align-right black">${accountPayableForm.vendor.age91Amount}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tr>
                                                    <tr>
                                                        <td class="align-right bold">Total :</td>
                                                        <c:choose>
                                                            <c:when test="${fn:contains(accountPayableForm.vendor.apAmount,'-')}">
                                                                <td class="align-right red">
                                                                    (${fn:replace(accountPayableForm.vendor.apAmount,'-','')})
                                                                </td>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td class="align-right black">${accountPayableForm.vendor.apAmount}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <th class="toggle" onclick="toggle('filter-container');">Search Filters</th>
                    </tr>
                    <tr>
                        <td>
                            <c:set var="toggleStyle" value="none"/>
                            <c:if test="${accountPayableForm.toggled}">
                                <c:set var="toggleStyle" value="block"/>
                            </c:if>
                            <div style="display: ${toggleStyle}" class="filter-container">
                                <table class="table" style="border: none;margin: 0;">
                                    <tr>
                                        <td class="label width-90px">From Date</td>
                                        <td>
                                            <html:text property="fromDate" styleId="fromDate" styleClass="textbox" maxlength="10"/>
                                        </td>
                                        <td class="label width-75px">To Date</td>
                                        <td>
                                            <html:text property="toDate" styleId="toDate" styleClass="textbox" maxlength="10"/>
                                        </td>
                                        <td class="label width-90px">Show Only</td>
                                        <td>
                                            <html:select property="only" styleId="only" styleClass="dropdown ignore-case">
                                                <html:option value="">Choose One</html:option>
                                                <html:option value="My AP Accounts">My AP Accounts</html:option>
                                                <html:option value="My AP Entries">My AP Entries</html:option>
                                                <html:option value="In Progress">In Progress Invoices</html:option>
                                            </html:select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="label width-90px">Invoice Number</td>
                                        <td>
                                            <html:text property="invoiceNumber" styleId="invoiceNumber" styleClass="textbox"/>
                                        </td>
                                        <td class="label width-75px">Show AR</td>
                                        <td class="label">
                                            <html:radio property="ar" styleId="arY" value="Y"/>
                                            <label for="arY">Yes</label>
                                            <html:radio property="ar" styleId="arN" value="N"/>
                                            <label for="arN">No</label>
                                        </td>
                                        <td class="label width-90px">Show On Hold</td>
                                        <td class="label">
                                            <html:radio property="hold" styleId="holdY" value="Y"/>
                                            <label for="holdY">Yes</label>
                                            <html:radio property="hold" styleId="holdN" value="N"/>
                                            <label for="holdN">No</label>
                                            <html:radio property="hold" styleId="holdO" value="Only"/>
                                            <label for="holdO">Only</label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="align-center" colspan="6">
                                            <input type="button" value="Search" class="button" onclick="search();"/>
                                            <input type="button" value="Reset" class="button" onclick="clearFilters();"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <th colspan="8">
                            List of Payables
                            <div class="float-right">
                                <input type="checkbox" id="payAll" onclick="setPayAll(this)"/>
                                <label for="payAll">Pay All</label>
                                <input type="button" value="Save" class="button" onclick="save();" style="vertical-align: top;margin-top: 1px;"/>
                            </div>
                        </th>
                    </tr>
                    <tr>
                        <td colspan="8">
                            <div id="results">
                                <c:import url="/jsp/accounting/accountPayable/results.jsp"/>
                            </div>
                        </td>
                    </tr>
                </table>
            </html:form>
        </div>
        <c:if test="${not empty fileName}">
            <iframe src="${path}/servlet/FileViewerServlet?fileName=${fileName}" style="display:none;"></iframe>
        </c:if>
    </body>
</html>