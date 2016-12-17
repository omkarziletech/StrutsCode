<%-- 
    Document   : search
    Created on : Feb 7, 2013, 11:30:58 PM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FCL Search</title>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
        <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
            <script type="text/javascript">
                var path = "${path}";
        </script>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.datepick.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/default/style.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.datepick.js"></script>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="${path}/js/fcl/search.js"></script>
        <c:set var="accessMode" value="1"/>
        <c:set var="writeMode" value="true"/>
        <c:if test="${param.accessMode==0}">
            <c:set var="accessMode" value="0"/>
            <c:set var="writeMode" value="false"/>
        </c:if>
    </head>
    <body>
        <%@include file="../../jsps/preloader.jsp"%>
        <div id="body-container" style="overflow: auto;">
            <html:form action="/fcl/search?accessMode=${accessMode}" name="searchForm"
                       styleId="searchForm" type="com.logiware.fcl.form.SearchForm" scope="request" method="post">
                <html:hidden property="action" styleId="action"/>
                <html:hidden property="selectedPage" styleId="selectedPage"/>
                <html:hidden property="sortBy" styleId="sortBy"/>
                <html:hidden property="orderBy" styleId="orderBy"/>
                <c:if test="${not empty sessionScope.importNavigation}">
                    <html:hidden property="importFile" styleId="importFile" value="true"/>
                </c:if>
                <table class="table" style="margin: 0;overflow: hidden">
                    <tr>
                        <th colspan="8">
                            <c:import url="/jsp/fcl/resultsHeader.jsp"/>
                        </th>
                    </tr>
                    <c:if test="${searchForm.action != 'search'}">
                        <tr>
                            <td class="label">File No</td>
                            <td>
                                <html:text property="fileNumber" styleId="fileNumber" styleClass="textbox"/>
                            </td>
                            <td class="label">Origin</td>
                            <td>
                                <html:text property="origin" styleId="origin" styleClass="textbox"/>
                                <input type="hidden" name="originCheck" id="originCheck" value="${searchForm.origin}"/>
                            </td>
                            <td class="label">From Date</td>
                            <td>
                                <html:text property="fromDate" styleId="fromDate" styleClass="textbox" maxlength="10"/>
                                <html:checkbox property="sailingDate" styleId="sailingDate" value="true" title="Search Using Sail Date"/>
                            </td>
                            <td class="label">To Date</td>
                            <td>
                                <html:text property="toDate" styleId="toDate" styleClass="textbox" maxlength="10"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="label">Client</td>
                            <td>
                                <c:choose>
                                    <c:when test="${searchForm.disableClient}">
                                        <c:set var="autoClientStyle" value="display: none;"/>
                                        <c:set var="manualClientStyle" value="display: block;"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="autoClientStyle" value="display: block;"/>
                                        <c:set var="manualClientStyle" value="display: none;"/>
                                    </c:otherwise>
                                </c:choose>
                                <div>
                                    <div id="autoClient" style="${autoClientStyle}" class="float-left">
                                        <html:text property="clientName" styleId="clientName" styleClass="textbox"/>
                                        <html:hidden property="clientNumber" styleId="clientNumber"/>
                                        <input type="hidden" name="clientNameCheck" id="clientNameCheck" value="${searchForm.clientName}"/>
                                        <img src="${path}/images/icons/search_filter.png" id="clientSearchEdit" title="Click here to edit Client Search options"
                                             onclick="showCustomerSearchFilter('client')"/>
                                    </div>
                                    <div id="manualClient" style="${manualClientStyle}" class="float-left">
                                        <html:text property="manualClientName" styleId="manualClientName" styleClass="textbox"/>
                                    </div>
                                    <html:checkbox property="disableClient" styleClass="float-left"
                                                   styleId="disableClient" value="true" title="Disable Autocomplete" onclick="disableAutocomplete(this)"/>
                                    <input type="hidden" name="clientSearchState" id="clientSearchState"/>
                                    <input type="hidden" name="clientSearchCountry" id="clientSearchCountry"/>
                                    <input type="hidden" name="clientSearchCountryCode" id="clientSearchCountryCode"/>
                                    <input type="hidden" name="clientSearchZip" id="clientSearchZip"/>
                                    <input type="hidden" name="clientSearchSalesCode" id="clientSearchSalesCode"/>
                                </div>
                            </td>
                            <td class="label">POL</td>
                            <td>
                                <html:text property="pol" styleId="pol" styleClass="textbox"/>
                                <input type="hidden" name="polCheck" id="polCheck" value="${searchForm.pol}"/>
                            </td>
                            <td class="label">Issuing Terminal</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty sessionScope.importNavigation}">
                                        <html:text property="issuingTerminal" styleId="issuingTerminalimport" styleClass="textbox"/>
                                        <input type="hidden" name="issuingTerminalCheck" id="issuingTerminalCheck" value="${searchForm.issuingTerminal}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <html:text property="issuingTerminal" styleId="issuingTerminal" styleClass="textbox"/>
                                        <input type="hidden" name="issuingTerminalCheck" id="issuingTerminalCheck" value="${searchForm.issuingTerminal}"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="label">Container No</td>
                            <td>
                                <html:text property="containerNumber" styleId="containerNumber" styleClass="textbox"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="label">Shipper</td>
                            <td>
                                <html:text property="shipperName" styleId="shipperName" styleClass="textbox"/>
                                <html:hidden property="shipperNumber" styleId="shipperNumber"/>
                                <input type="hidden" name="shipperNameCheck" id="shipperNameCheck" value="${searchForm.shipperName}"/>
                                <input type="hidden" name="shipperSearchState" id="shipperSearchState"/>
                                <input type="hidden" name="shipperSearchCountry" id="shipperSearchCountry"/>
                                <input type="hidden" name="shipperSearchCountryCode" id="shipperSearchCountryCode"/>
                                <input type="hidden" name="shipperSearchZip" id="shipperSearchZip"/>
                                <input type="hidden" name="shipperSearchSalesCode" id="shipperSearchSalesCode"/>
                                <img src="${path}/images/icons/search_filter.png" id="clientSearchEdit" title="Click here to edit Shipper Search options"
                                     onclick="showCustomerSearchFilter('shipper')"/>
                            </td>
                            <td class="label">POD</td>
                            <td>
                                <html:text property="pod" styleId="pod" styleClass="textbox"/>
                                <input type="hidden" name="podCheck" id="podCheck" value="${searchForm.pod}"/>
                            </td>
                            <td class="label">Origin Region</td>
                            <td>
                                <html:select property="originRegion" styleId="originRegion" styleClass="dropdown" style="text-transform:none">
                                    <html:optionsCollection property="regions" label="label" value="value"/>
                                </html:select>
                            </td>
                            <td class="label">SSL</td>
                            <td>
                                <html:text property="sslName" styleId="sslName" styleClass="textbox"/>
                                <html:hidden property="sslNumber" styleId="sslNumber" styleClass="textbox"/>
                                <input type="hidden" name="sslNameCheck" id="sslNameCheck" value="${searchForm.sslName}"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="label">Forwarder</td>
                            <td>
                                <html:text property="forwarderName" styleId="forwarderName" styleClass="textbox"/>
                                <html:hidden property="forwarderNumber" styleId="forwarderNumber"/>
                                <input type="hidden" name="forwarderNameCheck" id="forwarderNameCheck" value="${searchForm.forwarderName}"/>
                                <input type="hidden" name="forwarderSearchState" id="forwarderSearchState"/>
                                <input type="hidden" name="forwarderSearchCountry" id="forwarderSearchCountry"/>
                                <input type="hidden" name="forwarderSearchCountryCode" id="forwarderSearchCountryCode"/>
                                <input type="hidden" name="forwarderSearchZip" id="forwarderSearchZip"/>
                                <input type="hidden" name="forwarderSearchSalesCode" id="forwarderSearchSalesCode"/>
                                <img src="${path}/images/icons/search_filter.png" id="clientSearchEdit" title="Click here to edit Forwarder Search options"
                                     onclick="showCustomerSearchFilter('forwarder')"/>
                            </td>
                            <td class="label">Destination</td>
                            <td>
                                <html:text property="destination" styleId="destination" styleClass="textbox"/>
                                <input type="hidden" name="destinationCheck" id="destinationCheck" value="${searchForm.destination}"/>
                            </td>
                            <td class="label">Destination Region</td>
                            <td>
                                <html:select property="destinationRegion" styleId="destinationRegion" styleClass="dropdown" style="text-transform:none">
                                    <html:optionsCollection property="regions" label="label" value="value"/>
                                </html:select>
                            </td>
                            <td class="label">Created By</td>
                            <td>
                                <html:text property="createdBy" styleId="createdBy" styleClass="textbox"/>
                                <input type="hidden" name="createdByCheck" id="createdByCheck" value="${searchForm.createdBy}"/>
                                <html:checkbox property="createdByMe" styleId="createdByMe"
                                               title="Created by Me" value="${loginuser.loginName}" onclick="setCreatedBy(this)"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="label">Consignee</td>
                            <td>
                                <html:text property="consigneeName" styleId="consigneeName" styleClass="textbox"/>
                                <html:hidden property="consigneeNumber" styleId="consigneeNumber"/>
                                <input type="hidden" name="consigneeNameCheck" id="consigneeNameCheck" value="${searchForm.consigneeName}"/>
                                <input type="hidden" name="consigneeSearchState" id="consigneeSearchState"/>
                                <input type="hidden" name="consigneeSearchCountry" id="consigneeSearchCountry"/>
                                <input type="hidden" name="consigneeSearchCountryCode" id="consigneeSearchCountryCode"/>
                                <input type="hidden" name="consigneeSearchZip" id="consigneeSearchZip"/>
                                <input type="hidden" name="consigneeSearchSalesCode" id="consigneeSearchSalesCode"/>
                                <img src="${path}/images/icons/search_filter.png" id="clientSearchEdit" title="Click here to edit Consignee Search options"
                                     onclick="showCustomerSearchFilter('consignee')"/>
                            </td>
                            <td class="label">Filter By</td>
                            <td>
                                <html:select property="filterBy" styleId="filterBy" styleClass="dropdown" style="text-transform:none">
                                    <html:optionsCollection property="filterByOptions" label="label" value="value"/>
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.importNavigation}">
                                            <html:option value="Imp Release">Imp Release</html:option>
                                            <html:option value="No Release">No Release</html:option>
                                            <html:option value="Doc Release">Doc Release</html:option>
                                            <html:option value="Pmt Release">Pmt Release</html:option>
                                            <html:option value="Over Paid">Over Paid</html:option>
                                            <html:option value="Closed">Closed</html:option>
                                            <html:option value="Audited">Audited</html:option>
                                            <html:option value="Voided">Voided</html:option>
                                        </c:when>
                                        <c:otherwise>
                                            <html:option value="No COB">No COB</html:option>
                                        </c:otherwise>
                                    </c:choose>
                                </html:select>
                                <c:if test="${empty sessionScope.importNavigation}">
                                    <html:checkbox property="olySpotRate" styleId="olySpotRate"
                                                   title="Only Display Spot/Bullet Files"/>
                                </c:if>
                            </td>
                            <td class="label">SSL Booking #</td>
                            <td>
                                <html:text property="sslBookingNumber" styleId="sslBookingNumber" styleClass="textbox"/>
                            </td>
                            <td class="label">Booked By</td>
                            <td>
                                <html:text property="bookedBy" styleId="bookedBy" styleClass="textbox"/>
                                <input type="hidden" name="bookedByCheck" id="bookedByCheck" value="${searchForm.bookedBy}"/>
                                <html:checkbox property="bookedByMe" styleId="bookedByMe"
                                               title="Booked by Me" value="${loginuser.loginName}" onclick="setBookedBy(this)"/>
                            </td>
                        </tr>
                        <tr>
                            <c:choose>
                                <c:when test="${not empty sessionScope.importNavigation}">
                                    <td class="label">Master Shipper</td>

                                    <td> <html:text property="masterShipper" styleId="masterShipper" styleClass="textbox"/>
                                        <html:hidden property="masterShipperNumber" styleId="masterShipperNumber"/>
                                        <input type="hidden" name="masterNameCheck" id="masterNameCheck" value="${searchForm.masterShipper}"/>
                                        <input type="hidden" name="masterSearchState" id="masterSearchState"/>
                                        <input type="hidden" name="masterSearchCountry" id="masterSearchCountry"/>
                                        <input type="hidden" name="masterSearchCountryCode" id="masterSearchCountryCode"/>
                                        <input type="hidden" name="masterSearchZip" id="masterSearchZip"/>
                                        <input type="hidden" name="masterSearchSalesCode" id="masterSearchSalesCode"/>

                                        <img src="${path}/images/icons/search_filter.png" id="clientSearchEdit" title="Click here to edit MasterShipper Search options"
                                             onclick="showCustomerSearchFilter('masterShipper')"/>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td class="label">Limit</td>
                                    <td>
                                        <html:select property="limit" styleId="limit" styleClass="dropdown">
                                            <html:option value="250">250</html:option>
                                            <html:option value="500">500</html:option>
                                            <html:option value="1000">1000</html:option>
                                        </html:select>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td class="label">Master BL</td>
                            <td>
                                <html:text property="masterBl" styleId="masterBl" styleClass="textbox"/>
                            </td>
                            <td class="label">Inbound Number</td>
                            <td>
                                <html:text property="inboundNumber" styleId="inboundNumber" styleClass="textbox"/>
                            </td>
                            <td class="label">Sort By</td>
                            <td>
                                <html:select property="sortByDate" styleId="sortByDate" styleClass="dropdown" style="text-transform:none">
                                    <html:optionsCollection property="sortByDateOptions" label="label" value="value"/>
                                    <c:if test="${not empty sessionScope.importNavigation}">
                                        <html:option value="ETA">ETA</html:option>
                                    </c:if>
                                </html:select>
                            </td>
                        </tr>
                        <tr><td colspan="8"></td></tr>
                        <tr>
                            <c:choose>
                                <c:when test="${not empty sessionScope.importNavigation}">
                                    <td class="label">AMS</td>
                                    <td>
                                        <html:text property="ams" styleId="ams" styleClass="textbox"/>
                                    </td>
                                    <td class="label">Sub House BL</td>
                                    <td>
                                        <html:text property="subHouseBl" styleId="subHouseBl" styleClass="textbox"/>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td class="label">AES ITN</td>
                                    <td>
                                        <html:text property="aesItn" styleId="aesItn" styleClass="textbox"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <td class="label">Vessel</td>
                            <td>
                                <html:text property="vessel" styleId="vessel" styleClass="textbox"/>
                                <input type="hidden" name="vesselCheck" id="vesselCheck" value="${searchForm.vessel}"/>
                            <td class="label">Sales Code</td>
                            <td>
                                <html:text property="salesCode" styleId="salesCode" styleClass="textbox" onchange="validateSalescode(this)"/>
                                <input type="hidden" name="salesCodeCheck" id="salesCodeCheck" value="${searchForm.salesCode}"/>
                            </td>
                        </tr>
                        <tr><td colspan="8"></td></tr>
                            <c:if test="${not empty sessionScope.importNavigation}">
                            <tr>
                                <td class="label">Limit</td>
                                <td>
                                    <html:select property="limit" styleId="limit" styleClass="dropdown">
                                        <html:option value="250">250</html:option>
                                        <html:option value="500">500</html:option>
                                        <html:option value="1000">1000</html:option>
                                    </html:select>
                                </td>
                            </tr>
                        </c:if>
                        <tr>
                            <td colspan="8" class="align-center">
                                <input type="button" value="Quick Rates" class="button" onclick="showQuickRates('${sessionScope.importNavigation}');"/>
                                <c:if test="${sessionScope.quoteAccessMode}">
                                    <input type="button" value="New Quote" class="button" onclick="newQuote();"/>
                                </c:if>
                                <c:if test="${sessionScope.bookingAccessMode}">
                                    <input type="button" value="New Booking" class="button" onclick="newBooking();"/>
                                </c:if>
                                <c:if test="${sessionScope.blAccessMode}">
                                    <input type="button" value="New Arrival Notice" class="button" onclick="newArrivalNotice();"/>
                                </c:if>
                                <input type="button" value="Search" class="button" onclick="search();"/>
                                <input type="button" value="Reset" class="button" onclick="resetAll();"/>
                                <c:if test="${empty sessionScope.importNavigation}">
                                    <input type="button" value="Multi Quote" class="button" onclick="multiQuote();"> 
                                </c:if>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td colspan="8">
                            <div id="results">
                                <c:import url="/jsp/fcl/results.jsp"/>
                            </div>
                        </td>
                    </tr>
                </table>
            </html:form>
            <div id="quickRates" class="static-popup" style="display: none;width: 600px;height: 104px;">
                <c:import url="/jsp/fcl/quickRates.jsp"/>
            </div>
            <div id="customerSearchFilters" class="static-popup" style="display: none;width: 350px;height: 140px;">
                <c:import url="/jsp/fcl/customerSearchFilters.jsp"/>
            </div>
            <div id="originsList" class="static-popup" style="display: none;width: 1000px;height: 350px;"></div>
            <div id="destinationsList" class="static-popup" style="display: none;width: 1000px;height: 350px;"></div>
        </div>
    </body>
    <span class="red bold" style="font-size: smaller">  * All Search Fields Should Be Minimum 3 Characters</span>
</html>