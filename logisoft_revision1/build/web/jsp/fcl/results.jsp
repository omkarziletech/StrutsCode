<%-- 
    Document   : results
    Created on : Feb 13, 2013, 6:36:19 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:if test="${not empty searchForm.results}">
    <div id="result-header" class="table-banner green">
        <div class="float-left">
            <c:choose>
                <c:when test="${fn:length(searchForm.results)>1}">
                    ${fn:length(searchForm.results)} files found.
                </c:when>
                <c:otherwise>1 file found.</c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="result-container" style="overflow-x: hidden;">
        <table style="min-width: 1270px; overflow: hidden;" cellpadding="0" cellspacing="1" class="display-table" >
            <thead>
                <tr>
                    <th>Q</th>
                    <th>BK</th>
                    <th>BL</th>
                    <th>HZ</th>
                    <th>EDI</th>
                    <th class="width-75px"><a href="javascript:doSort('f.file_no')">File No</a></th>
                    <th>Status</th>
                    <th>
                        <c:choose>
                            <c:when test="${not empty sessionScope.importNavigation}">
                                Rel
                            </c:when>
                            <c:otherwise>
                                Doc
                            </c:otherwise>
                        </c:choose>
                    </th>
                    <th class="width-115px">
                        <c:choose>
                            <c:when test="${not empty sessionScope.importNavigation}">
                                Container #
                            </c:when>
                            <c:otherwise>
                                SSL Booking #
                            </c:otherwise>
                        </c:choose>
                    </th>
                    <th><a href="javascript:doSort('f.start_date')">Start Date</a></th>
                    <th class="width-75px"><a href="javascript:doSort('origin')">Origin</a></th>
                    <th class="width-75px"><a href="javascript:doSort('pol')">POL</a></th>
                    <th class="width-75px"><a href="javascript:doSort('pod')">POD</a></th>
                    <th class="width-75px">
                        <a href="javascript:doSort('destination')">
                            <c:choose>
                                <c:when test="${not empty sessionScope.importNavigation}">
                                    FD
                                </c:when>
                                <c:otherwise>
                                    Destination
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </th>
                    <th><a href="javascript:doSort('f.sail_date')">Sail Date</a></th>
                        <c:if test="${not empty sessionScope.importNavigation}">
                        <th><a href="javascript:doSort('f.eta_date')">ETA</a></th>
                        </c:if>
                    <th><a href="javascript:doSort('clientName')">Client</a></th>
                    <th><a href="javascript:doSort('sslineName')">SSL</a></th>
                    <th>ISS</th>
                    <th>TR</th>
                        <c:if test="${empty sessionScope.importNavigation}">
                        <th>AES</th>
                        </c:if>
                    <th>Created By</th>
                    <th>Booked By</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="zebra" value="odd"/>
                <c:forEach var="file" items="${searchForm.results}" varStatus="status">
                    <tr class="${zebra}">
                        <td>
                            <c:choose>
                                <c:when test="${file.multiStatus=='M'}">
                                    <img title="MultiQuote" src="${path}/images/icons/dots/yellow.gif" />
                                </c:when>
                                <c:when test="${file.quoteStatus=='Rated'}">
                                    <img title="${file.quoteStatus}" src="${path}/images/icons/dots/light-blue.gif" />
                                </c:when>
                                <c:when test="${file.quoteStatus=='Non Rated'}">
                                    <img title="${file.quoteStatus}" src="${path}/images/icons/dots/black.gif"/>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${file.bookingStatus=='Booking Complete'}">
                                    <img title="${file.bookingStatus}" src="${path}/images/icons/dots/dark-green.gif"/>
                                </c:when>
                                <c:when test="${file.bookingStatus=='Rated In Process'}">
                                    <img title="${file.bookingStatus}" src="${path}/images/icons/dots/light-blue.gif"/>
                                </c:when>
                                <c:when test="${file.bookingStatus=='Non Rated'}">
                                    <img title="${file.bookingStatus}" src="${path}/images/icons/dots/black.gif"/>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${file.blStatus=='BL not Manifested'}">
                                    <img title="${file.blStatus}" src="${path}/images/icons/dots/orange.png"/>
                                </c:when>
                                <c:when test="${file.blStatus=='Void BL'}">
                                    <img title="${file.blStatus}" src="${path}/images/icons/dots/red-cross.png" class="image-12x12"/>
                                </c:when>
                                <c:when test="${file.blStatus=='Sailing Date Past'}">
                                    <img title="${file.blStatus}" src="${path}/images/icons/dots/yellow.gif"/>
                                </c:when>
                                <c:when test="${file.blStatus=='Corrected'}">
                                    <img title="${file.blStatus}" src="${path}/images/icons/dots/red.gif"/>
                                </c:when>
                                <c:when test="${file.blStatus=='Manifested, Closed, Audited'
                                                || file.blStatus=='Manifested, Closed' || file.blStatus=='Manifested'}">
                                        <img title="${file.blStatus}" src="${path}/images/icons/dots/dark-green.gif"/>
                                </c:when>
                                <c:when test="${file.blStatus=='BL In Process'}">
                                    <img title="${file.blStatus}" src="${path}/images/icons/dots/light-blue.gif"/>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${file.hazmat}">
                                <img title="Hazmat" src="${path}/images/icons/danger.png" class="image-12x12"/>
                            </c:if>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${file.bookingEdiStatus eq 'Cancel' and file.ediStatus eq '304 success'}">
                                    <img title="EDI Booking Confirmation Cancelled Successfully" src="${path}/images/icons/arrow/blackBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/yellow.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${file.bookingEdiStatus eq 'Cancel' and file.ediStatus eq '997 success'}">
                                    <img title="EDI Booking Confirmation Cancelled Successfully" src="${path}/images/icons/arrow/blackBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/green.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${file.bookingEdiStatus eq 'Cancel' and file.ediStatus eq '304 failure'}">
                                    <img title="EDI Booking Confirmation Cancelled Successfully" src="${path}/images/icons/arrow/blackBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/red.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${file.bookingEdiStatus eq 'Cancel'}">
                                    <img title="EDI Booking Confirmation Cancelled Successfully" src="${path}/images/icons/arrow/blackBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                </c:when>
                                <c:when test="${file.ediStatus eq '304 success' and file.bookingEdiStatus eq '997 Booking Success'}">
                                    <img title="${file.bookingEdiStatus}" src="${path}/images/icons/arrow/yellowBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/yellow.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${file.ediStatus eq '997 success' and file.bookingEdiStatus eq '997 Booking Success'}">
                                    <img title="${file.bookingEdiStatus}" src="${path}/images/icons/arrow/yellowBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/green.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${file.ediStatus eq '997 success'}">
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/green.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${file.bookingEdiStatus eq '997 Booking Success'}">
                                    <img title="${file.bookingEdiStatus}" src="${path}/images/icons/arrow/yellowBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                </c:when>
                                <c:when test="${file.bookingEdiStatus eq '300 Success' and file.ediStatus eq '304 success'}">
                                    <img title="${file.bookingEdiStatus}" src="${path}/images/icons/arrow/greyBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/yellow.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${(file.bookingEdiStatus eq '301 Success' or file.bookingEdiStatus eq '301 Pending' or file.bookingEdiStatus eq '301 Conditionally Accepted') and file.ediStatus eq '304 success'}">
                                    <img title="${file.bookingEdiStatus}" src="${path}/images/icons/arrow/greenBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/yellow.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${file.bookingEdiStatus eq '300 Success' and file.ediStatus eq '304 failure'}">
                                    <img title="${file.bookingEdiStatus}" src="${path}/images/icons/arrow/greyBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/red.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${(file.bookingEdiStatus eq '301 Success'  or file.bookingEdiStatus eq '301 Pending' or file.bookingEdiStatus eq '301 Conditionally Accepted') and file.ediStatus eq '304 failure'}">
                                    <img title="${file.bookingEdiStatus}" src="${path}/images/icons/arrow/greenBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/red.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${file.bookingEdiStatus eq '301 Success' or file.bookingEdiStatus eq '301 Pending' or file.bookingEdiStatus eq '301 Conditionally Accepted'}">
                                    <img title="${file.bookingEdiStatus}" src="${path}/images/icons/arrow/greenBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                </c:when>
                                <c:when test="${(file.bookingEdiStatus eq '997 Booking Failure' or file.bookingEdiStatus eq '301 Failure' or file.bookingEdiStatus eq '300 Failure'  or file.bookingEdiStatus eq '301 Replaced' or file.bookingEdiStatus eq '301 Declined')
                                                and file.ediStatus eq '304 failure'}">
                                    <img title="${file.bookingEdiStatus}" src="${path}/images/icons/arrow/redBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/red.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${(file.bookingEdiStatus eq '997 Booking Failure' or file.bookingEdiStatus eq '301 Failure' or file.bookingEdiStatus eq '300 Failure'  or file.bookingEdiStatus eq '301 Replaced' or file.bookingEdiStatus eq '301 Declined')
                                                and file.ediStatus eq '304 success'}">
                                    <img title="${file.bookingEdiStatus}" src="${path}/images/icons/arrow/redBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/yellow.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${file.bookingEdiStatus eq '300 Success' or file.bookingEdiStatus eq '300 Change'}">
                                    <img title="${file.bookingEdiStatus}" src="${path}/images/icons/arrow/greyBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                </c:when>
                                <c:when test="${file.ediStatus eq '304 success'}">
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/yellow.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${file.ediStatus eq '304 failure'}">
                                    <img title="${file.ediStatus}" src="${path}/images/icons/arrow/red.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','bl')"/>
                                </c:when>
                                <c:when test="${file.bookingEdiStatus eq '997 Booking Failure' or file.bookingEdiStatus eq '301 Failure' or file.bookingEdiStatus eq '300 Failure'  or file.bookingEdiStatus eq '301 Replaced' or file.bookingEdiStatus eq '301 Declined'}">
                                    <img title="${file.bookingEdiStatus}" src="${path}/images/icons/arrow/redBooking.png" class="image-15x15" onclick="showEdi('04${file.fileNumber}','booking')"/>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${sessionScope.selectedFileNumber == file.fileNumber}">
                                    <c:remove var="selectedFileNumber" scope="session"/>
                                    <c:set var="fileColor" value="font-weight: bold;color: red;background: #00CCFF;"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="fileColor" value="color: black;"/>
                                </c:otherwise>
                            </c:choose>
                            <a href="javascript:void(0);" 
                               onclick="openFile('${file.fileNumber}');" style="${fileColor}">
                                04-${file.fileNumber}
                            </a>
                        </td>
                        <td>
                            <c:if test="${not empty file.fileStatus}">
                                <c:set var="fileStatus" value="<ul>"/>
                                <c:forTokens var="status" items="${file.fileStatus}" delims=",">
                                    <c:choose>
                                        <c:when test="${status=='I'}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>I=INTTRA</li>"/>
                                        </c:when>
                                        <c:when test="${status=='G'}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>G=GT NEXUS</li>"/>
                                        </c:when>
                                        <c:when test="${status=='E'}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>E=Ready To EDI</li>"/>
                                        </c:when>
                                        <c:when test="${status=='A'}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>A=BL Audited</li>"/>
                                        </c:when>
                                        <c:when test="${status=='RM'}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>RM=Received Master</li>"/>
                                        </c:when>
                                        <c:when test="${status=='CL'}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>CL=BL Closed</li>"/>
                                        </c:when>
                                        <c:when test="${status=='V'}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>V=BL Voided</li>"/>
                                        </c:when>
                                        <c:when test="${fn:contains(status, 'C') && not fn:contains(status, 'CN')}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>C=Container Cut Off</li>"/>
                                        </c:when>
                                        <c:when test="${fn:contains(status, 'D')}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>D=Doc Cut Off</li>"/>
                                        </c:when>
                                        <c:when test="${fn:contains(status, 'SP')}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>SP = Spot/Bullet Rate</li>"/>
                                        </c:when>
                                        <c:when test="${fn:length(status) eq 1 and fn:contains(status, 'S')}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>S=Vessel Sailing Date</li>"/>
                                        </c:when>
                                        <c:when test="${fn:contains(status, 'U')}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>U=Number of Units</li>"/>
                                        </c:when>
                                        <c:when test="${fn:contains(status, 'NR')}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>NR=Non Rated</li>"/>
                                        </c:when>
                                        <c:when test="${fn:contains(status, 'M')}">
                                            <c:set var="fileStatus" value="${fileStatus}<li>M=Manifested</li>"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="fileStatus" value="${fileStatus}<li>CN=Corrected BL</li>"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forTokens>
                                <c:set var="fileStatus" value="${fileStatus}<ul>"/>
                                <span title="${fileStatus}">${file.fileStatus}</span>
                            </c:if>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty sessionScope.importNavigation}">
                                    <c:if test="${not empty file.releaseType}">
                                        <c:choose>
                                            <c:when test="${file.releaseType == 'YY'}">
                                                <img src="${path}/images/icons/green-check.png" class="image-12x12"/>
                                            </c:when>
                                            <c:when test="${file.releaseType == 'YN'}">
                                                <font color="red" style="font-weight: bold">DR</font>
                                            </c:when>
                                            <c:when test="${file.releaseType == 'NY'}">
                                                <font color="red" style="font-weight: bold">PR</font>
                                            </c:when>
                                        </c:choose>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${file.documentReceived}">
                                            <font color="red" style="font-weight: bold;">Y</font>
                                        </c:when>
                                        <c:otherwise>
                                            N
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${file.documentStatus=='Approved'}">
                                            ,<font color="green" style="font-weight: bold"> A</font>
                                        </c:when>
                                        <c:when test="${file.documentStatus=='Disputed'}">
                                            ,<font color="red" style="font-weight: bold"> D</font>
                                        </c:when>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty sessionScope.importNavigation}">
                                    <span title="${file.containerNumber}">${str:abbreviate(file.containerNumber,20)}</span>
                                </c:when>
                                <c:otherwise>
                                    ${file.sslBookingNumber}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${file.startDate}</td>
                        <td>
                            <c:set var="origin" value="${fn:substring(file.origin, str:lastIndexOf(file.origin, '(')+1, str:lastIndexOf(file.origin, ')'))}"/>
                            <c:choose>
                                <c:when test="${not empty file.doorOrigin}">
                                    <c:set var="tooltip" value="Origin="/>
                                    <c:set var="tooltip" value="${tooltip}${fn:substringBefore(file.origin, '/')}(${origin})<br/>"/>
                                    <c:set var="tooltip" value="${tooltip}Door Origin="/>
                                    <c:choose>
                                        <c:when test="${fn:contains(file.doorOrigin,'/')}">
                                            <c:set var="tooltip" value="${tooltip}${fn:substringBefore(file.doorOrigin, '/')}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="tooltip" value="${tooltip}${file.doorOrigin}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:set var="tooltipClass" value="red bold"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="tooltip" value="${file.origin}"/>
                                    <c:set var="tooltipClass" value="black"/>
                                </c:otherwise>
                            </c:choose>
                            <span title="${tooltip}" class="${tooltipClass}">${origin}</span>
                        </td>
                        <td>
                            <c:set var="pol" value="${fn:substring(file.pol, str:lastIndexOf(file.pol, '(')+1, str:lastIndexOf(file.pol, ')'))}"/>
                            <span title="${file.pol}">${pol}</span>
                        </td>
                        <td>
                            <c:set var="pod" value="${fn:substring(file.pod, str:lastIndexOf(file.pod, '(')+1, str:lastIndexOf(file.pod, ')'))}"/>
                            <span title="${file.pod}">${pod}</span>
                        </td>
                        <td>
                            <c:set var="destination" 
                                   value="${fn:substring(file.destination, str:lastIndexOf(file.destination, '(')+1, str:lastIndexOf(file.destination, ')'))}"/>
                            <c:choose>
                                <c:when test="${not empty file.doorDestination}">
                                    <c:set var="tooltip" value="Destination="/>
                                    <c:set var="tooltip" value="${tooltip}${fn:substringBefore(file.destination, '/')}(${destination})<br/>"/>
                                    <c:set var="tooltip" value="${tooltip}Door Destination="/>
                                    <c:choose>
                                        <c:when test="${fn:contains(file.doorDestination,'/')}">
                                            <c:set var="tooltip" value="${tooltip}${fn:substringBefore(file.doorDestination, '/')}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="tooltip" value="${tooltip}${file.doorDestination}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:set var="tooltipClass" value="red bold"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="tooltip" value="${file.destination}"/>
                                    <c:set var="tooltipClass" value="black"/>
                                </c:otherwise>
                            </c:choose>
                            <span title="${tooltip}" class="${tooltipClass}">${destination}</span>
                        </td>
                        <td><span title="${file.containers}">${file.sailDate}</span></td>
                            <c:if test="${not empty sessionScope.importNavigation}">
                            <td>${file.eta}</td>
                        </c:if>
                        <td>
                            <span title="${file.clientName}">${str:abbreviate(file.clientName,20)}</span>
                        </td>
                        <td>
                            <span title="${file.sslineName}">${str:abbreviate(file.sslineName,20)}</span>
                        </td>
                        <td>
                            <span title="${file.issuingTerminal}">${fn:substringAfter(file.issuingTerminal, '-')}</span>
                        </td>
                        <td>
                            <c:if test="${not empty file.trackingStatus}">
                                <img title="${file.trackingStatus}" class="image-12x12" 
                                     src="${path}/images/icons/contents-view.gif" onclick="showTrackingStatus('${file.fileNumber}')"/>
                            </c:if>
                        </td>
                        <c:if test="${empty sessionScope.importNavigation}">
                            <td>
                                <c:choose>
                                    <c:when test="${not empty file.aesStatus}">
                                        <c:choose>
                                            <c:when test="${fn:toLowerCase(file.aesStatus)=='shipment added' 
                                                            || fn:toLowerCase(file.aesStatus)=='shipment replaced'
                                                            || fn:startsWith(file.aesStatus, 'X')}">
                                                <c:set var="aesStyle" value="background: #00FF00;"/>
                                            </c:when>
                                            <c:when test="${fn:containsIgnoreCase(file.aesStatus,'verify')}">
                                                <c:set var="aesStyle" value="background: #00FFFF;"/>
                                            </c:when>
                                            <c:when test="${fn:containsIgnoreCase(file.aesStatus,'shipment rejected') 
                                                            || !fn:containsIgnoreCase(file.aesStatus,'successfully processed')}">
                                                <c:set var="aesStyle" value="background: #FF0000;"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="aesStyle" value="background: yellow;"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:set var="aesStatus" value="${fn:replace(file.aesStatus, 'ITN#', '')}"/>
                                        <c:choose>
                                            <c:when test="${file.fileType == 'BL'}">
                                                <a href="javascript:void(0);" title="${aesStatus}" style="${aesStyle}"
                                                   onclick="showAes('${file.fileNumber}')">AES</a>
                                            </c:when>
                                            <c:otherwise>
                                                <span title="${aesStatus}" style="${aesStyle}">AES</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:when test="${file.aesCount!=0}">
                                        <span title="AES Sent" style="background: yellow;text-transform: none;">AES</span>
                                    </c:when>
                                </c:choose>
                            </td>
                        </c:if>
                        <td>${file.createdBy}</td>
                        <td>${file.bookedBy}</td>
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
</c:if>
