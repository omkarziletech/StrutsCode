<%--                            
Document   : searchResults
Created on : Jul 24, 2013, 4:21:09 PM
Author     : Rajesh
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
    <div class="table-banner green">
            <c:choose>
                <c:when test="${fn:length(bookings)>=1}">
                    ${fn:length(bookings)} files found.
                </c:when>
                <c:otherwise>0 file found.</c:otherwise>
            </c:choose>
        </div>
<table class="display-table" id="myTableID">
    <thead>
        <tr>
            <th id="bookingNumber" class="${searchForm.sortBy eq 'bookingNumber' ? searchForm.searchType : ''}">
                <a href="javascript:doSort('bookingNumber')" class="space">Booking #</a>
            </th>
            <th id="senderId" class="${searchForm.sortBy eq 'senderId' ? searchForm.searchType : ''}">
                <a href="javascript:doSort('senderId')" class="space">Sender ID</a>
            </th>
            <th id="senderMappingId" class="${searchForm.sortBy eq 'senderMappingId' ? searchForm.searchType : ''}">
                <a href="javascript:doSort('senderMappingId')" class="space">Sender Mapping Id</a>
            </th>
            <th id="customerControlCode" class="${searchForm.sortBy eq 'customerControlCode' ? searchForm.searchType : ''}">
                <a href="javascript:doSort('customerControlCode')" class="space">Customer Control Code</a>
            </th>
            <th id="origin" class="${searchForm.sortBy eq 'origin' ? searchForm.searchType : ''}">
                <a href="javascript:doSort('origin')" class="space">Org</a>
            </th>
            <th id="destination" class="${searchForm.sortBy eq 'destination' ? searchForm.searchType : ''}">
                <a href="javascript:doSort('destination')" class="space">Dest</a>
            </th>
            <th id="amsflag" class="${searchForm.sortBy eq 'amsflag' ? searchForm.searchType : ''}">
                <a  href="javascript:doSort('amsflag')" class="space">AMS</a>
            </th>
            <th id="aesflag" class="${searchForm.sortBy eq 'aesflag' ?  searchForm.searchType : ''}">
                <a href="javascript:doSort('aesflag')" class="space">AES</a>
            </th>
            <th id="bookingDate" class="${searchForm.sortBy eq 'bookingDate' ? searchForm.searchType:''}">
                <a href="javascript:doSort('bookingDate')" class="space">Booking Date</a>
            </th>
            <th id="pieces" class="${searchForm.sortBy eq 'pieces' ? searchForm.searchType:''}">
                <a href="javascript:doSort('pieces')" class="space">PCs</a>
            </th>
            <th id="pieces" class="${searchForm.sortBy eq 'weight' ? searchForm.searchType:''}">
                <a href="javascript:doSort('weight')" class="space">Wgt</a>
            </th>
            <th id="pieces" class="${searchForm.sortBy eq 'volume' ? searchForm.searchType:''}">
                <a href="javascript:doSort('volume')" class="space">Vol</a>
            </th>
            <th id="pieces" class="${searchForm.sortBy eq 'vesselVoyage' ? searchForm.searchType:''}">
                <a href="javascript:doSort('vesselVoyage')" class="space">Vessel Voyage ID</a>
            </th>
            <th id="pieces" class="${searchForm.sortBy eq 'vessel' ? searchForm.searchType:''}">
                <a href="javascript:doSort('vessel')" class="space">Vessel Name</a>
            </th>
            <th id="pieces" class="${searchForm.sortBy eq 'imoNumber' ? searchForm.searchType:''}">
                <a href="javascript:doSort('imoNumber')" class="space">IMO #</a>
            </th>
            <th  id="pieces" class="${searchForm.sortBy eq 'voyage' ? searchForm.searchType:''}">
                <a href="javascript:doSort('voyage')" class="space">Voyage</a>
            </th>
            <th  id="etd" class="${searchForm.sortBy eq 'etd' ? searchForm.searchType:''}">
                <a href="javascript:doSort('etd')" class="space">ETD</a>
            </th>
            <th  id="eta" class="${searchForm.sortBy eq 'eta' ? searchForm.searchType:''}">
                <a href="javascript:doSort('eta')" class="space">ETA</a>
            </th>
            <th id="pieces" class="${searchForm.sortBy eq 'createdOn' ? searchForm.searchType:''}">
                <a href="javascript:doSort('createdOn')" class="space">Created On</a>
            </th>
            <th>XML</th>
            <th>PDF</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach items="${bookings}" var="booking">
        <c:choose>
            <c:when test="${rowStyle eq 'odd'}">
                <c:set var="rowStyle" value="even"/>
            </c:when>
            <c:otherwise>
                <c:set var="rowStyle" value="odd"/>
            </c:otherwise>
        </c:choose>
        <tr class="${rowStyle}">
            <td>${booking.bookingNumber}</td>
            <td>${booking.senderId}</td>
            <td>${booking.senderMappingId}</td>
            <td>${booking.customerControlCode}</td>
            <td>${booking.cfsOrigin}</td>
            <td>${booking.cfsDestination}</td>
            <td>${booking.amsFlag}</td>
            <td>${booking.aesFlag}</td>
            <td>${booking.bookingDate}</td>
            <td>${booking.pieces}</td>
            <td>${booking.weight}</td>
            <td>${booking.volume}</td>
            <td>${booking.vesselVoyageId}</td>
            <td>${booking.vesselName}</td>
            <td>${booking.imoNumber}</td>
            <td>${booking.voyage}</td>
            <td>${booking.etd}</td>
            <td>${booking.eta}</td>
            <td>${booking.createdOn}</td>
            <td>
                <img alt="Xml" title="Booking Request file" src="${path}/jsps/LCL/images/xml_icon.gif"
                     class="xmlfile" onclick="showFile('${path}','br','${booking.bookingId}');"/>
        <c:if test="${not empty booking.bookingNumber}">
            <img alt="Xml" title="Booking Confirmation file" src="${path}/jsps/LCL/images/xml_icon.gif"
                 class="xmlfile" onclick="showFile('${path}','bc','${booking.bookingId}');"/>
        </c:if>
        </td>
        <td>
            <img alt="Pdf" title="PDF file" src="${path}/img/icons/pdf.gif"
                 class="xmlfile" onclick="showPDF('${path}', '${booking.bookingId}')"/>
        </td>
        <td>
        <c:choose>
            <c:when test="${not empty booking.bookingNumber}">
                <img alt="processed" title="Processed" src="${path}/jsps/LCL/images/approve.png"/>
            </c:when>
            <c:otherwise>
                <img alt="re-process" title="Error: ${booking.remarks} Click to Re-process"
                     src="${path}/images/icons/Kn_reload.jpg" width="16" height="16"
                     onclick="reProcess('${booking.bookingId}');"/>
            </c:otherwise>
        </c:choose>
        </td>
        </tr>
    </c:forEach>
</tbody>
</table>








