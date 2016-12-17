<%-- 
    Document   : lclInvoicePool
    Created on : Dec 25, 2013, 7:02:29 PM
    Author     : Meiyazhakan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Invoice Pool</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
        <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
        <%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
        <%@taglib tagdir="/WEB-INF/tags/cong/" prefix="cong"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/datepicker/cal.css" />
        <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lightbox.css"/>
        <script type="text/javascript" src="${path}/jsps/LCL/js/calendar/datetimepicker_css.js"></script>
        <link rel="stylesheet" type="text/css" href="${path}/jsps/LCL/js/autocomplete/jquery.autocomplete.css" />
        <link rel="stylesheet" type="text/css" href="${path}/jsps/LCL/js/autocomplete/main.css" />
        <script type='text/javascript' src='${path}/jsps/LCL/js/autocomplete/jquery.autocomplete.js'></script>
        <%@include file="/taglib.jsp"%>
        <%@include file="/jsps/preloader.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>  
        <script type="text/javascript" src="${path}/jsps/LCL/js/lclInvoicePool.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
    </head>
    <body>
        <html:form action="/lclInvoicePool.do" name="lclInvoicePoolForm" 
                   styleId="lclInvoicePoolForm" type="com.gp.cvst.logisoft.struts.form.lcl.LclInvoicePoolForm" scope="request" method="post">
            <input type="hidden" value="${path}" id="path"/>
            <html:hidden property="moduleName" styleId="moduleName" value="${lclInvoicePoolForm.moduleName}"/>
            <html:hidden property="methodName" styleId="methodName" value="${lclInvoicePoolForm.methodName}"/>
            <table class="table" style="margin: 0">
                <thead>
                    <tr>
                        <th colspan="8">Search Criteria</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="label amount">Show Invoices</td>
                        <td>
                            <html:select property="status" styleId="status" styleClass="dropdown" style="text-transform:none;min-width: 127px;">
                                <html:option value="">Select</html:option>
                                <html:option value="Posted">Posted</html:option>
                                <html:option value="Un Posted">Un Posted</html:option>
                            </html:select>
                        </td>
                        <td class="label amount">Order By</td>
                        <td>
                            <html:select property="orderByField" styleId="orderByField" 
                                         styleClass="dropdown" style="text-transform:none;min-width: 127px;">
                                <html:option value="date">Created Date</html:option>
                                <html:option value="invoice_number">Invoice Number</html:option>
                            </html:select>
                        </td>
                        <td class="label amount">From Date</td>
                        <td>
                            <cong:calendar container="NULL" styleClass="textbox" id="fromDate" name="fromDate"/>
                        </td>
                        <td class="label amount">To Date</td>
                        <td>
                            <cong:calendar container="NULL" styleClass="textbox" id="toDate" name="toDate"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label amount">Invoice Number</td>
                        <td>
                            <html:text property="invoiceNumber" styleId="invoiceNumber" styleClass="textbox"/>
                        </td>
                        <td class="label amount">File Number</td>
                        <td>
                            <html:text property="fileNumber" styleId="fileNumber" styleClass="textbox"/>
                        </td>
                        <td class="label amount">Customer Name</td>
                        <td>
                            <cong:autocompletor name="customerName" template="tradingPartner" id="customerName"
                                                fields="customerNumber,NULL,NULL,NULL,NULL,NULL,NULL,NULL,address,NULL,NULL,NULL,NULL,NULL,NULL"
                                                query="CLIENT_NO_CONSIGNEE" width="400" scrollHeight="300" container="NULL"
                                                shouldMatch="true" styleClass="textbox"/>
                            <html:hidden property="customerNumber" styleId="customerNumber" styleClass="textbox"/>
                        </td>
                        <td class="label"></td>
                        <td>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" align="right">
                            <input type="button" value="Search" class="button" onclick="searchInvoice()"/>
                            <input type="button" value="Reset" class="button" onclick="resetAllFeilds()"/>
                        </td>
                        <td colspan="4"></td>
                    </tr>
                </tbody>
            </table>
            <table class="table">
                <tr>
                    <th>Invoice Pool List</th>
                </tr>
                <tr>
                    <td>
                        <display:table name="${invoicePoolList}" class="display-table" pagesize="20"  id="invoicePool" sort="list" requestURI="/lclInvoicePool.do">
                            <display:setProperty name="paging.banner.some_items_found">
                                <span class="table-banner"><font color="blue">{0}</font>LCL Invocie Pool displayed,For more Records click on page numbers.</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.one_item_found">
                                <span class="table-banner">One {0} displayed. Page Number</span>
                            </display:setProperty>
                            <display:setProperty name="paging.banner.all_items_found">
                                <span class="table-banner">{0} {1} Displayed, Page Number</span>
                            </display:setProperty>
                            <display:setProperty name="basic.msg.empty_list">
                                <span class="table-banner">No Records Found.</span>
                            </display:setProperty>
                            <display:setProperty name="paging.banner.placement" value="bottom"/>
                            <display:setProperty name="paging.banner.item_name" value="LclInvoicePool"/>
                            <display:setProperty name="paging.banner.items_name" value="LclInvoicePool"/>
                            <display:column title="Invoice Number" headerClass="sortable" sortable="true">
                                ${invoicePool.invoiceNo}
                            </display:column>
                            <display:column title="File Number" headerClass="sortable" sortable="true">
                                <span id="link" onClick="openFile('${path}', '${invoicePool.fileId}', '${invoicePool.className}');" style="cursor: pointer;">
                                    ${invoicePool.fileNo}</span>
                                </display:column>
                                <display:column title="Created Date" headerClass="sortable" sortable="true">
                                    ${invoicePool.createdDate}
                                </display:column>
                                <display:column title="Posted Date" headerClass="sortable" sortable="true">
                                    ${invoicePool.postedDate}
                                </display:column>
                                <display:column title="Amount" headerClass="sortable" sortable="true">
                                    ${invoicePool.agentrelInv}
                                </display:column>
                                <display:column title="Bill To Party" headerClass="sortable" sortable="true">
                                <span title="${invoicePool.agentName}<br/>${invoicePool.agentNo}" >${invoicePool.agentNo}</span>
                            </display:column>
                            <display:column title="User" headerClass="sortable" sortable="true">
                                ${invoicePool.createdUser}
                            </display:column>
                            <display:column title="Description" headerClass="sortable" sortable="true">
                                <div class="pre-wrap width-400px">${invoicePool.description}</div>
                            </display:column>
                            <display:column title="Status" headerClass="sortable" sortable="true">
                                <c:choose>
                                    <c:when test="${invoicePool.invoiceStatus eq 'Posted'}">
                                        <span style="color: green;font-weight: bold;">Posted</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: red;font-weight: bold;">In Progress</span>
                                    </c:otherwise>
                                </c:choose>

                            </display:column>
                            <display:column title="Action" headerClass="sortable" sortable="true">
                                <img src="${path}/img/icons/search_over.gif" border="0"
                                     onClick="previewArInvoiceReport('${path}', '', '${invoicePool.invoiceId}', '${invoicePool.fileId}')"/> </span>
                            </display:column>
                        </display:table>
                    </td></tr>
            </table>
        </html:form>
    </body>
</html>

