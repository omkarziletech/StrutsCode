<c:if test="${not empty fileSearchList}">
    <div class="scrollable-table">
        <input type="hidden" id="templateCubeFlag" value="true"/>
        <input type="hidden" id="templateWeightFlag" value="true"/>
        <div>
            <div>
                <table id="file">
                    <thead>
                        <tr> 
                            <th><div label="QU"/></th>
                    <th><div label="BK"/></th>
                    <th><div label="BL"/></th>
                    <th><div label="HZ"/></th>
                    <th><div label="EDI"/></th>
                    <th><div label="S"/></th>
                    <th><div label="File No"/></th>
                    <th><div label="PN"/></th>
                    <th><div label="Status" onclick="doSortAscDesc('fileStatus')" class="underline"/></th>
                    <th><div label="Doc"/></th>
                    <th><div label="ERT"/></th>
                    <th><div label="Dispo" onclick="doSortAscDesc('dispoCode')" class="underline"/></th>
                    <th style="min-width: 60px"><div label="CurLoc"></div>
                    <img src="${path}/images/icons/filter.png" class="filterable" data-field="span"/></th>
                    <th><div label="PCS"/></th>
                    <th><div label="${lclSearchForm.commodity eq 'M' ? 'CBM' :'CFT'}" onclick="doSortAscDesc('${lclSearchForm.commodity eq 'M' ? 'totalVolumeMetric' :'totalVolumeImperial'}')" class="underline" /></th>
                    <th><div label="${lclSearchForm.commodity eq 'M' ? 'KGS' :'LBS'}" onclick="doSortAscDesc('${lclSearchForm.commodity eq 'M' ? 'totalWeightMetric' :'totalWeightImperial'}')" class="underline" /></th>
                    <th style="min-width: 55px"><div label="Origin" onclick="doSortAscDesc('originUncode')" class="underline"></div><img src="${path}/images/icons/filter.png" class="filterable" data-field="span"/></th>
                    <th style="min-width: 45px"><div label="Pol" onclick="doSortAscDesc('polUncode')" class="underline"></div><img src="${path}/images/icons/filter.png" class="filterable" data-field="span"/></th>
                    <th style="min-width: 45px"><div label="Pod" onclick="doSortAscDesc('podUncode')" class="underline"></div><img src="${path}/images/icons/filter.png" class="filterable" data-field="span"/></th>
                    <th style="min-width: 55px"><div label="Destn" onclick="doSortAscDesc('destinationUncode')" class="underline"></div><img src="${path}/images/icons/filter.png" class="filterable" data-field="span"/></th>
                    <th><div label="BkgVoy"onclick="doSortAscDesc('scheduleNo')" class="underline"/></th>
                    <th><div label="Date Rec"/></th>
                    <th><div label="Origin LRD" onclick="doSortAscDesc('pooLrdDate')" class="underline"/></th>
                    <th><div label="Loading RMKS" onclick="doSortAscDesc('polLrdDate')" class="underline"/></th>
                    <th><div label="Sailing Date" onclick="doSortAscDesc('sailingDate')" class="underline"/></th>
                    <th><div label="Shipper" onclick="doSortAscDesc('shipName')" class="underline"/></th>
                    <th><div label="FWD" onclick="doSortAscDesc('fwdName')" class="underline"/></th>
                    <th><div label="Consig" onclick="doSortAscDesc('consName')" class="underline"/></th>
                    <th><div label="Bill TM" onclick="doSortAscDesc('billingTerminal')" class="underline"/></th>
                    <th><div label="AES"/></th>
                    <th><div label="Bkg By"/></th>
                    <th><div label="Cons"/></th>
                    <th><div label="Hot Codes"/></th>
                    <th><div label="Action"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="result" items="${fileSearchList}"  varStatus="count">
                        <c:set var="index" value='0'/>
                        <c:set var="zebra" value="${zebra eq 'odd' ? 'even' : 'odd'}"/>
                        <tr class="${zebra}" style="border-color:white;">
                            <%-- Quote disc --%>
                            <td>
                        <c:choose>
                            <c:when test="${not empty result.bookedBy && not empty result.quoteBy}">
                                <img src="${path}/img/icons/orange_dot.png" title="Quote By:${fn:toUpperCase(result.quoteBy)}"
                                     alt="orange disc"/>
                            </c:when>
                            <c:when test="${result.fileState eq 'Q' && empty result.bookedBy && not empty result.quoteBy}">
                                <c:choose>
                                    <c:when test="${result.quoteComplete ==1}">
                                        <img src="${path}/img/icons/darkGreenDot.gif" title="Quote By:${fn:toUpperCase(result.quoteBy)}"
                                             alt="green disc"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${path}/img/icons/lightBlue2.gif" alt="blue disc"
                                             title="Quote By:${fn:toUpperCase(result.quoteBy)}"/>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                        </c:choose>
                        </td>
                        <%-- Booking disc --%>
                        <td>
                        <c:if test="${result.fileState eq 'B' && (result.fileStatus eq 'M' || result.fileState eq 'B')}">
                            <img src="${path}/img/icons/darkGreenDot.gif" alt="green disc" title="Booking"/>
                        </c:if>
                        </td>
                        <%-- BL disc --%>
                        <td>
                        <c:choose>
                            <c:when test="${not empty result.correctionIds}">
                                <span title="Corrected"><img src="${path}/images/icons/dots/red.gif" alt="red disc"/></span>
                            </c:when>
                            <c:when test="${result.fileState eq 'BL' && result.currentFileStatus eq 'M'}">
                                <span title="Manifested"><img src="${path}/img/icons/darkGreenDot.gif" alt="green disc"/></span>
                            </c:when>
                            <c:when test="${result.fileState eq 'BL' && (result.postedUser!='' && result.postedUser!=null)
                                            && result.currentFileStatus ne 'M'}">
                                <span title="Posted"><img src="${path}/img/icons/lightBlue2.gif" alt="blue disc"/></span>
                            </c:when>
                            <c:when test="${result.fileState eq 'BL' && (empty result.postedUser && result.postedUser eq null)
                                            && result.currentFileStatus ne 'M'}">
                                <span title="Pool"><img src="${path}/img/icons/orange_dot.png" alt="orange disc"/></span>
                            </c:when>
                        </c:choose>
                        </td>
                        <%-- Hazmat  disc --%>
                        <td>
                        <c:if test="${result.hazmat}">
                            <c:set var="hazImagePath" value="${not empty result.hazmatInfo ? '/img/icons/danger..png' : '/img/dangerBlack.png'}"/>
                            <img src="${path}${hazImagePath}"  style="cursor:pointer"
                                 width="12" height="12" alt="Haz" title="Hazardous Information"/>
                        </c:if>
                        </td>
                        <td> <%-- EDI   block --%></td>
                        <td>
                            ${result.dataSource}
                        </td>
                        <td>
                            <span class="link ${result.fileNumberId eq fileNumberId ? 'highlight-saffron' : ''}" 
                                  onclick="checkLock('${path}', '${result.fileNumber}', '${result.fileNumberId}',
                                                  '${result.fileState}', '${lclSearchForm.moduleName}', '', 'LCLIMPTRANS');">
                                <c:choose>
                                    <c:when test="${result.transshipment ==1}">
                                        IMP -${result.fileNumber}
                                    </c:when>
                                    <c:when test="${result.shortShip ==1}">
                                        ZZ${result.shortShipSequence}-${result.fileNumber}
                                    </c:when>
                                    <c:otherwise>
                                        ${fn:substring(result.originUncode,2,5)}-${result.fileNumber}
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </td>
                        <td>
                        <c:if test="${result.priorityNotes}">
                            <span class="priority" title="${fn:replace(result.priorityNoteValues,',','<br/>')}"  onmouseout="tooltip.hide()">
                                <img src="${path}/img/icons/priorityNote.png" align="middle" width="15" height="15" alt="PN"/>
                            </span>
                        </c:if>
                        </td>
                        <td>
                            <label id="Release" class="statusClass${result.fileNumberId}">
                                <c:choose>
                                    <c:when test="${result.fileState eq 'Q'}">
                                        <c:if test="${result.fileState eq 'Q'&& result.cfcl eq '1'}">
                                            <span style="vertical-align: top;height: 10px" id="statusC" title="CFCL Account">CF,</span>
                                        </c:if>
                                        <span style="vertical-align: top;height: 10px" id="statusQ" title="Quote">${result.fileState}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${(result.fileState eq 'B'|| result.fileState eq 'BL') && (result.cfcl eq'1')}">
                                            <span style="vertical-align: top;height: 10px" id="statusC" title="CFCL Account">CF,</span>
                                        </c:if>
                                        <c:if test="${result.fileState eq 'B' || result.fileState eq 'BL'}">
                                            <c:if test="${result.fileStatus eq 'B'}">
                                                <span style="height: 10px" title="Booking">${result.fileStatus}</span>
                                            </c:if>
                                            <c:if test="${result.fileStatus eq 'X'}">
                                                <span style="height: 10px" title="Cargo Terminated">${result.fileStatus}</span>
                                            </c:if>
                                            <c:if test="${result.fileStatus eq 'RF' }">
                                                <span style="height: 10px" title="Cargo Refused">${result.fileStatus}</span>
                                            </c:if>
                                            <c:if test="${(result.fileStatus eq 'W' || result.fileStatus eq 'WV') && (result.dispoCode eq 'RCVD' || result.dispoCode eq 'INTR')}">
                                                <span style="height: 10px" title="Warehouse Verified">${result.fileStatus}</span>
                                            </c:if>
                                            <c:if test="${(result.fileStatus eq 'W' || result.fileStatus eq 'WU') && result.dispoCode eq 'RUNV'}">
                                                <span style="height: 10px" title="Warehouse UN-Verified">${result.fileStatus}</span>
                                            </c:if>
                                            <c:if test="${result.fileStatus eq 'PR'}">
                                                <span style="height: 10px" title="PreReleased for Export">
                                                    ${result.originalFileStatus},<span style="background-color: lightskyblue">PRE </span>
                                                </span>
                                            </c:if> 
                                            <c:if test="${result.fileStatus eq 'R'}">
                                                <c:choose>
                                                    <c:when test="${result.pieceUnit ne null && not empty result.pieceUnit
                                                                    && (result.voyageServiceType eq 'E' || result.voyageServiceType eq 'C')}">
                                                        <span style="height: 10px;color:red;background-color:yellowgreen;" title="Released and Picked">${result.originalFileStatus},RP</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="height: 10px;color:red;background-color:yellowgreen;" title="Released for Export">${result.originalFileStatus},${result.fileStatus}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                            <c:if test="${result.fileStatus eq 'L'}">
                                                <span style="height: 10px" title="Load Completed">${result.fileStatus}</span>
                                            </c:if>
                                            <c:if test="${result.fileStatus eq 'M'}">
                                                <span style="height: 10px" title="Manifested">${result.fileStatus}</span>
                                            </c:if>
                                            <c:if test="${result.fileStatus eq 'PU'}">
                                                <span style="height: 10px" title="Picked Up">${result.fileStatus}</span>
                                            </c:if>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${(result.billToParty=='S' && result.creditS=='11343') || (result.billToParty=='T' && result.creditT=='11343')
                                              || (result.billToParty=='F' && result.creditF=='11343')}">
                                    <c:out value=","/>
                                    <label class="onHoldClass${result.fileNumberId}"><span  style="color:red;font-weight: bold" title="Credit Hold" >CH</span></label>
                                </c:if>
                                <c:if test="${result.shipmentHoldFlag}">
                                    <c:out value=","/>   
                                    <label><span style="color:red;font-weight: bold" title="Shipment On Hold" >OH</span></label>   
                                </c:if>
                            </label>
                        </td>
                        <td>
                            <span class="${result.clientPwk == 1 ? '' : 'red'}">${result.clientPwk == 1 ? 'Y' : 'N'}</span>
                        </td>
                        <td>
                            <span class="${result.ert == 1 ? 'red' : ''}">${result.ert == 1 ? 'Y' : 'N'}</span>
                        </td>
                        <td>
                            <span title="${result.dispoDesc}">${result.dispoCode}</span></td>
                        <td>
                        <c:if test="${result.dispoCode ne 'INTR' && result.dispoCode ne 'VSAL'}">
                            <span title="${result.currentLocName}">${result.currentLocCode}</span>
                        </c:if> 
                        </td>
                        <td><c:out value="${result.totalPiece}"/>
                        <c:if test="${not empty result.totalActualPiece && result.totalActualPiece!=0}">
                            <c:if test="${result.totalActualPiece!=result.totalBookedPiece}">
                                <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle" title="Booked As ${result.totalBookedPiece}">*</span>
                            </c:if>
                        </c:if>
                        </td>

                        <c:choose>
                            <c:when test="${lclSearchForm.commodity=='I'}">
                                <td>
                                <c:out value="${result.totalVolumeImperial}"/>
                                <c:if test="${not empty result.actualVolume}">
                                    <c:set var ="AVOL" value="${result.actualVolume}"/>
                                    <c:set var ="BVOL" value="${empty result.bookedVolume ? 0:result.bookedVolume}"/>
                                    <c:set var ="volumeDifference" value="${((AVOL-BVOL) / ((AVOL + BVOL)/2))*100}"/>
                                    <c:if test="${not empty volumeDifference &&(volumeDifference le -9 || volumeDifference ge 9)}">
                                        <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle"
                                              title="Booked As ${result.bookedVolume}">*</span>
                                    </c:if>
                                </c:if>
                                </td>
                                <td>
                                <c:out value="${result.totalWeightImperial}"/>
                                <c:if test="${not empty result.actualWeight}">
                                    <c:set var="AWEI" value="${result.actualWeight}"/>
                                    <c:set var="BWEI" value="${empty result.bookedWeight ? 0:result.bookedWeight}"/>
                                    <c:set var ="weightDifference"  value="${((AWEI - BWEI) / ((AWEI + BWEI)/2))*100}"/>
                                    <c:if test="${not empty weightDifference && (weightDifference le -9 || weightDifference ge 9)}">
                                        <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle"
                                              title="Booked As ${result.bookedWeight}">*</span>
                                    </c:if>
                                </c:if>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>
                                <c:out value="${result.totalVolumeMetric}"/>
                                <c:if test="${not empty result.actualVolumeMetric}">
                                    <c:set var="AVOLM" value="${result.actualVolumeMetric}"/>
                                    <c:set var="BVOLM" value="${empty result.bookedVolumeMetric ? 0:result.bookedVolumeMetric}"/>
                                    <c:set var ="volMetricDifference" value="${((AVOLM - BVOLM) / ((AVOLM + BVOLM)/2))*100}"/>
                                    <c:if test="${not empty volMetricDifference && (volMetricDifference le -9 || volMetricDifference ge 9)}">
                                        <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle"
                                              title="Booked As ${result.bookedVolumeMetric}">*</span>
                                    </c:if>
                                </c:if>
                                </td>
                                <td>
                                <c:out value="${result.totalWeightMetric}"/>
                                <c:if test="${not empty result.actualWeightMetric}">
                                    <c:set var="AWEIM" value="${result.actualWeightMetric}"/>
                                    <c:set var="BWEIM" value="${empty result.bookedWeightMetric ? 0:result.bookedWeightMetric}"/>
                                    <c:set var ="weiMetricDifference" value="${((AWEIM - BWEIM) / ((AWEIM + BWEIM)/2))*100}"/>
                                    <c:if test="${not empty weiMetricDifference && (weiMetricDifference le -9 || weiMetricDifference ge 9)}">
                                        <span style="color: red;font-size: 12px;font-weight: bold;vertical-align: middle"
                                              title="Booked As ${result.bookedWeightMetric}">*</span>
                                    </c:if>
                                </c:if>
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <td>    
                        <c:choose>
                            <c:when test="${result.pooPickup==1  && not empty result.pickupCity && result.bookingType ne 'T'}">
                                <span  style="color:red;" title="Origin=${result.origin}
                                       <br>Door Origin=${fn:substring(result.pickupCity,fn:indexOf(result.pickupCity,'-')+1,fn:length(result.pickupCity))}">${result.originUncode}</span>
                                <c:set var="unCode" value="${result.originUncode}"></c:set>
                            </c:when>
                            <c:when test="${result.pooPickup==1  && not empty result.pickupCity && result.bookingType eq 'T'}">
                                <span  style="color:red;" title="Origin=${result.pod}
                                       <br>Cargo Originated=${result.pol}
                                       <br>Door Origin=${fn:substring(result.pickupCity,fn:indexOf(result.pickupCity,'-')+1,fn:length(result.pickupCity))}">${result.podUncode}</span>
                                <c:set var="unCode" value="${result.podUncode}"></c:set>
                            </c:when>
                            <c:when test="${result.pooPickup==0 &&  empty result.pickupCity && result.bookingType eq 'T'}">
                                <span  style="color:brown;" title="Origin=${result.pod}
                                       <br>Cargo Originated=${result.pol}">${result.podUncode}</span>
                                <c:set var="unCode" value="${result.podUncode}"></c:set>
                            </c:when>
                            <c:otherwise>
                                <span title="${result.origin}">${result.originUncode}</span>
                                <c:set var="unCode" value="${result.originUncode}"></c:set>
                            </c:otherwise>
                        </c:choose>
                        <%-- <span title="${result.origin}">${result.originUncode}</span> --%>
                        </td>
                        <td>
                        <c:choose>
                            <c:when test="${result.relayOverride ==1 && result.bookingType eq 'T'}">
                                <span title="${result.transPolCode}/${result.transPolName}/${result.transPolState}" style="color:#800080"><b>${result.transPolCode}</b></span>
                            </c:when>
                            <c:when test="${result.relayOverride == 1 && result.bookingType ne 'T'}">
                                <span title="${result.pol}" style="color:#800080"><b>${result.polUncode}</b></span>
                            </c:when>
                            <c:otherwise>
                                <span title="${result.pol}">${result.polUncode}</span>
                            </c:otherwise>
                        </c:choose>

                        </td>
                        <td>
                        <c:choose>
                            <c:when test="${result.relayOverride ==1 && result.bookingType eq 'T'}">
                                <span title="${result.transPodCode}/${result.transPodName}/${result.transPodCountry}" style="color:#800080">
                                    <b>${result.transPodCode}</b></span>
                            </c:when>
                            <c:when test="${result.relayOverride == 1 && result.bookingType ne 'T'}">
                                <span title="${result.pod}" style="color:#800080"><b>${result.podUncode}</b></span>
                            </c:when>
                            <c:otherwise>
                                <span title="${result.pod}">${result.podUncode}</span>
                            </c:otherwise>
                        </c:choose>
                        </td>
                        <td><span title="${result.destination}">${result.destinationUncode}</span></td>
                        <td>${result.scheduleNo}</td>
                        <td>    
                        <c:if  test="${not empty result.cargoRecDate}">
                            <span>${result.cargoRecDate}</span>   
                        </c:if>  
                        </td>
                        <td>${result.pooLrd}</td>
                        <td><span title="${fn:substring(result.loadingRemarks,6,fn:length(result.loadingRemarks))}"
                                  <c:choose>
                                    <c:when test="${fn:length(fn:trim(fn:substring(result.loadingRemarks,6,fn:length(result.loadingRemarks))))>10}">
                                        style="color:red;font-weight: bold"
                                    </c:when>
                                    <c:otherwise>
                                    </c:otherwise>
                                </c:choose>
                                >${fn:substring(result.loadingRemarks,6,19)}</span> 
                        </td>
                        <td>${result.sailDate}</td>

                        <td><span title="${result.shipName}">${fn:substring(result.shipName,0,15)}</span></td>
                        <td><span title="${result.fwdName}">${fn:substring(result.fwdName,0,5)}</span></td>
                        <td><span title="${result.consName}">${fn:substring(result.consName,0,15)}</span></td>
                        <td>
                        <c:set var="billingTerminal" value="${fn:split(result.billingTerminal,'-')}"></c:set>
                        <span title="${billingTerminal[1]}-${billingTerminal[0]}">${fn:substring(result.billingTerminal,0,2)}</span>
                        </td>
                        <td>
                        <c:choose>
                            <c:when test="${not empty result.aesStatus}">
                                <c:choose>
                                    <c:when test="${fn:toLowerCase(result.aesStatus)=='shipment added'
                                                    || fn:toLowerCase(result.aesStatus)=='shipment replaced'
                                                    || fn:startsWith(result.aesStatus, 'X')}">
                                        <c:set var="aesStyle" value="background: #00FF00;"/>
                                    </c:when>
                                    <c:when test="${fn:containsIgnoreCase(result.aesStatus,'verify')}">
                                        <c:set var="aesStyle" value="background: #00FFFF;"/>
                                    </c:when>
                                    <c:when test="${fn:containsIgnoreCase(result.aesStatus,'shipment rejected')
                                                    || !fn:containsIgnoreCase(result.aesStatus,'successfully processed')}">
                                        <c:set var="aesStyle" value="background: #FF0000;"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="aesStyle" value="background: yellow;"/>
                                    </c:otherwise>
                                </c:choose>
                                <span class="linkSpan"  style="${aesStyle}" title="${result.aesStatus}"
                                      onclick="submitAesHistory('${path}', '${result.fileNumber}');">AES</span>
                            </c:when>
                            <c:when test="${not empty result.sedCount && result.sedCount!= 0}">
                                <span  style="font-weight: bold;background:yellow;cursor: pointer" title="Aes Sent">AES</span>
                            </c:when>
                        </c:choose>
                        </td>
                        <%--  <c:if test="${template==null || template.quoteBy}"><td>
                        <span title="${fn:toUpperCase(result.quoteBy)}">${fn:toUpperCase(fn:substring(result.quoteBy,0,6))}</span>
                    </td></c:if>--%>
                        <td>
                            <span title="${fn:toUpperCase(result.bookedBy)}">${fn:toUpperCase(fn:substring(result.bookedBy,0,6))}</span>
                        </td>
                        <td> <c:if test="${not empty result.relatedConoslidted && consIconSearchScreen!=false}">

                            <span class="cons" title="Consolidate with <br> ${fn:replace(result.relatedConoslidted,',','<br>')}"
                                  onclick="showConsolidate('${path}', 'search', '${result.fileNumber}', '${result.fileNumberId}', '${result.fileNumber},${result.relatedConoslidted}', '${lclSearchForm.commodity}')" onmouseout="tooltip.hide()">
                                <img src="${path}/jsps/LCL/images/consolidate.png" alt="cons" height="10" width="10" />
                            </span>

                        </c:if>
                        </td>
                        <td><span title="${result.hotCodes} <br> ${result.loadingRemarks}" ${result.hotCodeCount > 3 ? 'style=color:#FF0000;':''}>${result.hotCodeKey}</span>
                        </td>
                        <td>
                        <c:if test="${result.fileState ne 'Q'}">
                            <img src="${path}/img/icons/print_purpleIcon.png" alt="print" width="10" height="10" title="Print"
                                 align="middle" onclick="printBkgReport('${path}', '${result.fileNumberId}', '${result.fileNumber}')"/>
                        </c:if>
                        
                        <c:set var="isPreRelease" value="${result.fileStatus eq 'PR' ? ',PRE' : ''}"/>
                        <input type="hidden" name="preReleaseStatus" id="preReleaseStatus${result.fileNumberId}" 
                               value="${result.originalFileStatus}${isPreRelease}"/>
                        <input type="hidden" name="originalFileStatus" id="originalFileStatus${result.fileNumberId}" value="${result.originalFileStatus}"/>
                        
                        <c:if test="${result.fileState ne 'Q' && (result.fileStatus  eq 'B'  || result.fileStatus  eq 'W' || result.fileStatus eq 'PR')}">
                            <span style="cursor:pointer" title="Release" id="release${result.fileNumberId}">
                                <img src="${path}/img/icons/releaseicon.png" alt="" width="10" height="10" 
                                     align="middle" id="Release${result.fileNumberId}"   class="buttonStyleNew"
                                     onclick="clickSearchReleaseIcon('${path}', '${unCode}', '${result.podUncode}', '${result.destinationUncode}',
                                                     '${result.fileNumberId}', this, '${result.totalVolumeImperial}', '${result.totalWeightImperial}', '${result.scheduleNo}', '${result.cfcl}', '${result.relatedConoslidted}', '${result.hold}','${result.hazmat}')">
                            </span>
                        </c:if>
                        <c:if test="${result.fileStatus  eq 'R'}">
                            <span style="cursor:pointer" title="UnRelease" id="release${result.fileNumberId}">
                                <img src="${path}/img/icons/unreleaseIcon.png" alt="" width="10" height="10" 
                                     align="middle" id="Release${result.fileNumberId}"   class="buttonStyleNew"
                                     onclick="clickSearchReleaseIcon('${path}', '${unCode}', '${result.podUncode}', '${result.destinationUncode}',
                                                     '${result.fileNumberId}', this, '${result.totalVolumeImperial}', '${result.totalWeightImperial}', '${result.scheduleNo}', '${result.cfcl}', '${result.relatedConoslidted}', '${result.hold}')">
                            </span>
                        </c:if>
                        <c:if test="${result.fileState ne 'Q'}">
                            <img src="${path}/img/icons/label_icone.png" width="10" height="10" align="middle" title="label print" 
                                 onclick="displayLabelPopup('${result.fileNumber}', '${result.fileNumberId}')"/>    
                        </c:if>     
                        </td>
                        <td class="hidden">
                            <span class="nonReleasedCFT">${result.totalVolumeImperial}</span>
                            <span class="nonReleasedLBS">${result.totalWeightImperial}</span>
                            <span class="nonReleasedCBM">${result.totalVolumeMetric}</span>
                            <span class="nonReleasedKGS">${result.totalWeightMetric}</span>
                        <c:if test="${result.drReleaseFlag eq 'true'}">
                            <span class="releaseStatusFlag">${result.drReleaseFlag}</span>
                            <span class="CFT">${result.totalVolumeImperial}</span>
                            <span class="LBS">${result.totalWeightImperial}</span>
                            <span class="CBM">${result.totalVolumeMetric}</span>
                            <span class="KGS">${result.totalWeightMetric}</span>
                        </c:if>
                        </td>
                        </tr>
                        <c:set var="index" value="${index+1}"></c:set>
                    </c:forEach>
                    <tr>
                        <td colspan="32" style="height:20px">&nbsp;&nbsp;</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</c:if>

