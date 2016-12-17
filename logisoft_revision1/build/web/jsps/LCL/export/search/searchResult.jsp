<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%@ taglib prefix="cong" tagdir="/WEB-INF/tags/cong"%>
        <%@include file="../../init.jsp" %>
        <%@include file="../../../../jsps/includes/baseResources.jsp" %>
        <%@include file="../../../includes/resources.jsp" %>
        <%@include file="../../../../jsps/preloader.jsp"%>
        <cong:javascript  src="${path}/jsps/LCL/js/export/searchResult.js"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <cong:javascript src="${path}/jsps/LCL/js/common/lclExportCommon.js"/>
        <script type="text/javascript" src='<c:url value="/jsps/LCL/js/export/searchScreen.js"></c:url>'></script>
            <title>Search Result</title>
        </head>
        <body>
        <cong:form id="lclSearchForm" name="lclSearchForm" method="post" action="lclSearch.do">
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="filterBy" name="filterBy"/>
            <input type="hidden" id="userRoleId" name="userRoleId" value="${loginuser.role.id}">
            <cong:hidden id="moduleName" name="moduleName" value='${lclSearchForm.moduleName}'/>
            <cong:hidden id="commodity" name="commodity" value='${commodity}'/>
            <input type="hidden" id="userId" name="userId" value="${loginuser.userId}"/>
            <cong:hidden name="filenumber" id="filenumber" value="${fileNumber}"/>
            <input type="hidden" id="uom" name="uom" value="${lclSearchForm.commodity}"/>
            <input type="hidden" id="conoslidateFiles" name="conoslidateFiles" value=""/>
            <input type="hidden" id="filterBy" value="${lclSearchForm.filterBy}"/>
            <input type="hidden" id="searchType" name="searchType" value="${lclSearchForm.searchType}"/>
            <input type="hidden" id="orderBy" name="orderBy" value="${lclSearchForm.orderBy}"/>
            <input type="hidden" id="sortByValue" name="sortByValue" value="${lclSearchForm.sortByValue}"/>
            <input type="hidden" id="recordCount" name="recordCount" value="${lclSearchForm.recordCount}"/>
            <input type="hidden" id="voyageorigin" name="voyageorigin" value="${lclSearchForm.polName}"/>
            <input type="hidden" id="voyagedestination" name="voyagedestination" value="${lclSearchForm.pod}"/>
            <table  class="table" border="0" width="100%">
                <tr>
                    <th class="label" width=35%">
                        <c:set var="inventoryFlag" value="${lclSearchForm.filterBy eq 'IWB' || lclSearchForm.filterBy eq 'BP'
                                                            || lclSearchForm.filterBy eq 'IPO' || lclSearchForm.filterBy eq 'BL'}"/>
                        <span>Filter</span>
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
                                <c:when test="${lclSearchForm.filterBy eq 'BP'}">
                                    BL Pool
                                </c:when>
                                <c:when test="${lclSearchForm.filterBy eq 'IPO'}">
                                    Inventory Pickups Only
                                </c:when>
                                <c:when test="${lclSearchForm.filterBy eq 'CGO'}">
                                    Cargo not Released
                                </c:when>
                                <c:when test="${lclSearchForm.filterBy eq 'ONBK'}">
                                    Online Booking
                                </c:when>
                                <c:when test="${lclSearchForm.filterBy eq 'BNR'}">
                                    Bookings NOT Received
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
                        <c:if test="${not empty lclSearchForm.forwarder}">
                            <span>Forwarder-></span>
                            <span class="blue">${lclSearchForm.forwarder}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.foreignAgent}">
                            <span>Foreign Agent-></span>
                            <span class="blue">${lclSearchForm.foreignAgent}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.origin}">
                            <span>Origin-></span>
                            <span class="${inventoryFlag ? 'green' : 'blue'}">${lclSearchForm.origin}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.pol}">
                            <span>POL-></span>
                            <span class="${inventoryFlag ? 'green' : 'blue'}">${lclSearchForm.pol}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.pod}">
                            <span>POD-></span>
                            <span class="${inventoryFlag ? 'green' : 'blue'}">${lclSearchForm.pod}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.destination}">
                            <span>Destination-></span>
                            <span class="${inventoryFlag ? 'green' : 'blue'}">${lclSearchForm.destination}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.originRegion && lclSearchForm.originRegion ne '0'}">
                            <span>Origin Region-></span>
                            <span class="${inventoryFlag ? 'green' : 'blue'}">${lclSearchForm.originRegion}</span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.destinationRegion && lclSearchForm.destinationRegion ne '0'}">
                            <span>Destination Region-></span>
                            <span class="${inventoryFlag ? 'green' : 'blue'}">${lclSearchForm.destinationRegion}</span>
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
                        <c:if test="${not empty lclSearchForm.createdBy}">
                            <span>Quote By-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.createdBy)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.bookedBy}">
                            <span>Booked By-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.bookedBy)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.bookedForVoyage && lclSearchForm.bookedForVoyage ne '0'}">
                            <span>Booked for Voyage-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.bookedForVoyage)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.startDate && empty lclSearchForm.fileNumber && empty lclSearchForm.containerNo && !inventoryFlag}">
                            <span>From Date-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.startDate)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.endDate && empty lclSearchForm.fileNumber && empty lclSearchForm.containerNo && !inventoryFlag}">
                            <span>To Date-></span>
                            <span class="blue">${fn:toUpperCase(lclSearchForm.endDate)}</span>,
                        </c:if>
                        <c:if test="${not empty lclSearchForm.limit && empty lclSearchForm.fileNumber && !inventoryFlag}">
                            <span>Limit-></span>
                            <span class="blue">${lclSearchForm.limit}</span>
                        </c:if>
                        <c:choose>
                            <c:when test="${not empty lclSearchForm.sortBy && lclSearchForm.sortBy ne '0'}">
                                <span>Sort By-></span>
                                <c:if test="${fn:contains(lclSearchForm.sortBy, 'C')}">
                                    <span class="blue">Container Cut off</span>,
                                </c:if>
                                <c:if test="${fn:contains(lclSearchForm.sortBy, 'D')}">
                                    <span class="blue">Doc Cut off</span>,
                                </c:if>
                                <c:if test="${fn:contains(lclSearchForm.sortBy, 'DNR')}">
                                    <span class="blue">Doc's Not Received</span>,
                                </c:if>
                                <c:if test="${fn:contains(lclSearchForm.sortBy, 'E')}">
                                    <span class="blue">ETD</span>
                                </c:if>
                            </c:when>
                            <c:otherwise></c:otherwise>
                        </c:choose>
                        <c:if test="${consolidate}">
                            <b><span style="color:red;"><c:out value="Consolidation"></span></c:out></b>
                            </c:if>
                    </th>
                    <th width=25%">
                        <input type="button" class="button-style1" onclick="searchBack();" value="Back"/>
                        <input type="button" value="New Quote" class="button-style1" onclick="checkFileNumber('${path}', '', 'Quotes', '${lclSearchForm.moduleName}');"/>
                        <input type="button" value="New Bkg" class="button-style1" onclick="checkFileNumber('${path}', '', 'Bookings', '${lclSearchForm.moduleName}');"/>
                        <c:if test="${lclSearchForm.filterBy == 'IWB'}">
                            <c:if test="${not empty lclSearchForm.pol &&  not empty lclSearchForm.pod}">
                                <input type="button" id="unitVoyage" class="button" onclick="openLoadUnits('${path}')" value="Load"/>
                            </c:if>
                            <c:if test="${not empty lclSearchForm.destination}">
                                <input type="button" id="rollDr" class="button" value="Roll DRs"/>
                            </c:if>
                        </c:if> 
                        <c:if test="${roleQuickBkg}">
                            <input type="button" id="quickdr"  value="Quick Bkg" align="middle" class="button-style1" onclick="createQuickBkg('${path}');"/>
                        </c:if>
                    </th>
                    <th class="label" width="18%" style="font-weight: normal; ">
                        <c:if test="${inventoryFlag}">
                            <c:choose>
                                <c:when test="${lclSearchForm.commodity eq 'I'}">
                                    Totals:
                                    <cong:label id="cftlbs" text=""></cong:label><br/>
                                    Released: <cong:label id="cftlbsForR" text=""></cong:label>

                                </c:when>
                                <c:otherwise>
                                    Totals:<cong:label id="cbmkgs" text=""></cong:label><br/>
                                    &nbsp;&nbsp;&nbsp;Released: <cong:label id="cbmkgsForR" text=""></cong:label>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </th>
                    <th class="label"  width="5%">
                        <c:if test="${lclSearchForm.cfcl=='0'}">
                            CFCL-><span style="color:blue;font-weight: bold"><c:out value="Exclude"></c:out></span>
                        </c:if>
                        <c:if test="${lclSearchForm.cfcl=='1'}">
                            CFCL-><span style="color:blue;font-weight: bold"><c:out value="Only"></c:out></span>
                        </c:if>
                        <c:if test="${empty lclSearchForm.cfcl}">
                            CFCL-><span style="color:blue;font-weight: bold"><c:out value="Include"></c:out></span>
                        </c:if>
                        <c:if test="${not empty lclSearchForm.cfclAcct}">
                            ,CFCL Account#->
                            <span style="color:blue;font-weight: bold"><c:out value="${lclSearchForm.cfclAcct}"></c:out></span>
                        </c:if>
                    </th>
                    <th width="15%">
                        <b><span style="color:black;font-weight: bold">
                                &nbsp;&nbsp;&nbsp;&nbsp;File Search:&nbsp;</span></b>
                        <input type="text" id="fileNumber" name="fileNumber" size="8" class="textlabelsBoldForTextBox" style="text-transform: uppercase"/>
                        <input type="hidden" id="limit" name="limit" size="8" class="textlabelsBoldForTextBox" value="100"/>
                        <img src="${path}/img/icons/magnifier.png" border="0" alt="S" onclick="submitForm('search');"/>
                    </th>
                    <th width="10%">
                        <c:if test="${lclSearchForm.filterBy eq 'BP' || lclSearchForm.blPoolOwner ne ''}">
                            <input type="button" value="Print BL Pool" class="button-style1" onclick="openPrintFaxPopUp('${path}');"/>
                        </c:if>
                    </th>
                </tr>

                <tr>
                    <td colspan="6">
                        <div class="table-banner green">
                            <table>
                                <tr><td>
                                        <c:choose>
                                            <c:when test="${fn:length(fileSearchList)>=1}">
                                                ${fn:length(fileSearchList)}   files found.
                                            </c:when>
                                            <c:otherwise>No file found.</c:otherwise>
                                        </c:choose>
                                    </td></tr>
                            </table>
                        </div>
                        <c:choose>
                            <c:when test="${empty orderedTemplateList}">
                                <%@include file="/jsps/LCL/export/search/results.jsp" %>
                            </c:when>
                            <c:otherwise>
                                <%@include file="/jsps/LCL/export/search/templateSearch.jsp" %>
                            </c:otherwise>
                        </c:choose>
                        <div id="labelCopy" class="static-popup" style="display: none;width: 300px;height: 100px;">
                            <table class="table"  style="margin: 1px;width: 300px;height: 100px;">
                                <input type="hidden" id="fileNo"/>
                                <input type="hidden" id="fileNumberId"/>
                                <tr>
                                    <th colspan="2">
                                        <div class="float-left">Booking#<span id="currentFileNo"></span></div>
                                        <div class="float-right">
                                            <a href="javascript: closeLabelPopup()">
                                                <img title="Close" src="${path}/images/icons/close.png"/>
                                            </a>
                                        </div>
                                    </th>
                                </tr>
                                <tr>
                                    <td>
                                        <span id="update-msg" class="message"></span>
                                        <span id="update-filedName" class="update-filedName"></span>
                                    </td>
                                    <td>
                                        <span id="update-val" class="update-value"></span>   
                                    </td>
                                </tr>
                                <tr>
                                    <td class="textlabelsBoldleftforlcl">How many Labels
                                        <input type="text" id="labelsCount" style="width:35px" maxlength="4" onkeyup="validateLabelsValues()"/></td>
                                    <td align="center"><input type="button" class="button-style1" value="Submit" onclick="saveLabelCount()"/></td>
                                </tr>
                            </table>
                        </div> 
                    </td>
                </tr>
            </table>
        </cong:form>
        <script type="text/javascript">
            $(document).ready(function () {
                var searchFlag = $("#recordCount").val();
                var filterBy = $("#filterBy").val();
                if (searchFlag === "true" && searchFlag !== "" && filterBy !== "IWB") {
                    $.prompt("Please increase limit to view more results.");
                    $("#recordCount").val('');
                }
                document.getElementById('fileNumber').focus();
                $("#fileNumber").keypress(function (e) {
                    var keycode = (e.keyCode ? e.keyCode : e.which);
                    if (keycode === 13) {
                        e.preventDefault();
                        submitForm('search');
                    }
                });
            });

            function submitForm(methodName) {
                $("#methodName").val(methodName);
                $('#filterBy').val("All");
                $("#searchFileNo").val("Y");
                $("#lclSearchForm").submit();
            }

        </script>
    </body>
</html>
