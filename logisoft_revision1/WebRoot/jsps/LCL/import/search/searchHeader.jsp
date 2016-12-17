<%--
    Document   : searchHeader
    Created on : Feb 11, 2015, 3:22:47 PM
    Author     : mohana
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>LCL Search</title>
        <%@include file="../../init.jsp"%>
        <%@include file="/jsps/preloader.jsp" %>
        <%@include file="/jsps/includes/baseResources.jsp" %>
        <%@page import="com.gp.cong.common.Application"%>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <script type="text/javascript" src="${path}/jsps/LCL/js/checkLock.js"></script>
        <script type="text/javascript" src="${path}/jsps/LCL/js/importSearchResult.js"></script>
        <style type="text/css">
            .down {
                background: url("${path}/images/icons/Import_descending.gif") left no-repeat;
            }
            .up {
                background: url("${path}/images/icons/Import_ascending.gif") left no-repeat;
            }
            .filedSpace{
                padding-left: 10px;
            }
        </style>
    </head>
    <body>
        <%@include file="/jsps/preloader.jsp" %>
        <form id="lclSearchForm" name="lclSearchForm" action="lclImportSearch.do">
            <c:set var="moduleName" value="${lclSearchForm.moduleName}"/>
            <input type="hidden" id="highlightFileNo" name="highlightFileNo" value="${highlightFileNo}"/>
            <input type="hidden" id="uom" name="uom" value="${lclSearchForm.commodity}"/>
            <input type="hidden" id="userId" name="userId" value="${loginuser.userId}"/>
            <input type="hidden" id="isSingleFile" value="${isSingleFile}"/>
            <input type="hidden" id="filenumber" name="filenumber" value="${fileNumber}"/>
            <input type="hidden" id="searchFileNo" name="searchFileNo"/>
            <input type="hidden" id="filterBy" name="filterBy" value="${lclSearchForm.filterBy}"/>
            <input type="hidden" id="moduleName" name="moduleName" value="${lclSearchForm.moduleName}"/>
            <input type="hidden" id="searchFileNo" name="sortBy"/>
            <input type="hidden" id="searchType" name="searchType" value="${lclSearchForm.searchType}"/>
            <input type="hidden" id="sortByValue" name="sortByValue"/>
            <input type="hidden" name="methodName" id="methodName" value="search"/>
            <table class="table">
                <tr>
                    <th class="label" style="width: 40%">
                        <span>Filter By-></span>
                        <span class="blue">
                            <c:choose>
                                <c:when test="${lclSearchForm.filterBy eq 'Q'}">
                                    Quote
                                </c:when>
                                <c:when test="${lclSearchForm.filterBy eq 'B'}">
                                    Booking
                                </c:when>
                                <c:when test="${lclSearchForm.filterBy eq 'X'}">
                                    Terminated
                                </c:when>
                                <c:when test="${lclSearchForm.filterBy eq 'IWB'}">
                                    Inventory All
                                </c:when>
                                <c:when test="${lclSearchForm.filterBy eq 'INAVAL'}">
                                    Inventory AVAL
                                </c:when>
                                <c:when test="${lclSearchForm.filterBy eq 'COH'}">
                                    Cargo on Hold
                                </c:when>
                                <c:when test="${lclSearchForm.filterBy eq 'CGO'}">
                                    Cargo in General Order
                                </c:when>
                                <c:when test="${lclSearchForm.filterBy eq 'CGO'}">
                                    Cargo not Released
                                </c:when>
                                <c:otherwise>
                                    All
                                </c:otherwise>
                            </c:choose>
                        </span>,
                        <c:if test="${not empty lclSearchForm.fileNumber}">
                            <span>File Number-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.fileNumber)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.client}">
                            <span>Client-></span>
                            <span class="blue">${lclSearchForm.client}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.shipperName}">
                            <span>Shipper-></span>
                            <span class="blue">${lclSearchForm.shipperName}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.consignee}">
                            <span>Consignee-></span>
                            <span class="blue">${lclSearchForm.consignee}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.foreignAgent}">
                            <span>Foreign Agent-></span>
                            <span class="blue">${lclSearchForm.foreignAgent}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.origin}">
                            <span>Origin-></span>
                            <span class="${lclSearchForm.filterBy eq 'IWB' ? 'green' : 'blue'}">${lclSearchForm.origin}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.pol}">
                            <span>POL-></span>
                            <span class="${lclSearchForm.filterBy eq 'IWB' ? 'green' : 'blue'}">${lclSearchForm.pol}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.pod}">
                            <span>POD-></span>
                            <span class="${lclSearchForm.filterBy eq 'IWB' ? 'green' : 'blue'}">${lclSearchForm.pod}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.destination}">
                            <span>Destination-></span>
                            <span class="${lclSearchForm.filterBy eq 'IWB' ? 'green' : 'blue'}">${lclSearchForm.destination}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.originRegion && lclSearchForm.originRegion ne '0'}">
                            <span>Origin Region-></span>
                            <span class="${lclSearchForm.filterBy eq 'IWB' ? 'green' : 'blue'}">${lclSearchForm.originRegion}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.destinationRegion && lclSearchForm.destinationRegion ne '0'}">
                            <span>Destination Region-></span>
                            <span class="${lclSearchForm.filterBy eq 'IWB' ? 'green' : 'blue'}">${lclSearchForm.destinationRegion}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.issuingTerminal}">
                            <span>Issuing Terminal-></span>
                            <span class="blue">${lclSearchForm.issuingTerminal}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.containerNo}">
                            <span>Container No-></span>
                            <span class="blue">${lclSearchForm.containerNo}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.ssl}">
                            <span>SSL-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.ssl)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.masterBl}">
                            <span>Master BL-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.masterBl)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.warehouseDocNo}">
                            <span>Warehouse Doc#-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.warehouseDocNo)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.customerPo}">
                            <span>Customer PO-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.customerPo)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.trackingNo}">
                            <span>Tracking#-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.trackingNo)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.inbondNo}">
                            <span>Inbond#-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.inbondNo)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.subHouse}">
                            <span>Sub House-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.subHouse)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.amsHBL}">
                            <span>AMS House BL-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.amsHBL)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.ipiLoadNo}">
                            <span>IPI Load#-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.ipiLoadNo)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.createdBy}">
                            <span>Quote By-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.createdBy)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.bookedBy}">
                            <span>Booked By-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.bookedBy)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.startDate}">
                            <span>From Date-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.startDate)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.endDate}">
                            <span>To Date-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.endDate)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.limit && empty lclSearchForm.fileNumber && lclSearchForm.filterBy ne 'IWB'}">
                            <span>Limit-></span>
                            <span class="blue">${lclSearchForm.limit}</span>
                        </c:if>
                    </th>
                    <th style="width: 20%">
                        <input type="button" value="Back" class="button" onclick="backSearch();"/>
                        <input type="button" value="New Quote" class="button" onclick="checkFileNumber('${path}', '', 'Quotes', '${lclSearchForm.moduleName}');"/>
                        <input type="button" value="New DR" class="button" onclick="checkFileNumber('${path}', '', 'Bookings', '${lclSearchForm.moduleName}');"/>
                    </th>
                    <th style="width: 20%">
                        <div class="align-right label">
                            <span>File Search:&nbsp;</span>
                            <input type="text" id="fileNumber" name="fileNumber" size="8" class="textbox" onkeyup="searchEmptyDateValues()"/>
                            <input type="hidden" id="limit" name="limit" value="${lclSearchForm.limit}"/>
                            <img src="${path}/img/icons/magnifier.png" onclick="submitForm('search');"/>
                        </div>
                    </th>
                </tr>
                <tr>
                    <td colspan="3">
                        <%@include file="/jsps/LCL/import/search/searchResult.jsp" %>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>