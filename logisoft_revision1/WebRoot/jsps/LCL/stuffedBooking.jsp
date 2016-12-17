<%--
    Document   : stuffedBooking
    Created on : Oct 5, 2012, 12:44:18 PM
    Author     : Ram
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="/taglib.jsp" %>
<%@include file="init.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp"%>
<%@taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <body>
        <input type="hidden" name="currentFile" value="0"/>
        <input type="hidden" name="path" id="path" value="${path}"/>
    <cong:hidden id="serviceType" name="schServiceType" value="${lclAddVoyageForm.schServiceType}"/>
    <cong:hidden name="originId" id="originId" value="${originId}"/>
    <cong:hidden name="finalDestinationId" id="finalDestinationId" value="${finalDestinationId}"/>
    <cong:hidden name="headerId" id="headerId" value="${headerId}"/>
    <cong:hidden name="unitssId" id="unitssId" value="${lclunitss.id}"/>
    <cong:hidden name="detailId" id="detailId" value="${lclAddVoyageForm.detailId}"/>
    <cong:hidden name="pickedVoyNo" id="pickedVoyNo" value="${lclunitss.lclSsHeader.scheduleNo}"/>
    <input type="hidden" name="userId" id="userId" value="${user.userId}"/>
    <input type="hidden" id="userRoleId" name="userRoleId" value="${loginuser.role.id}">
    <!-- declare for furture enchancement--!>
    <table width="100%" class="tableBorderNew" border="0">
        <tr><td>
    <!-- ------------------------------------  Top Header Row starts Here -------------------------------- -->
    <div style="float: left;position: relative;width:49.9%;height:35px">
        <table class="tableBorderNew">
            <tr class="tableHeadingNew">
                <c:choose>
                    <c:when test="${lclAddVoyageForm.filterByChanges eq 'lclDomestic'}">
                        <td>
                            <span title="Reset">
                                <img src="${path}/img/icons/Button-Refresh-icon.png" width="12" height="12"
                                     onclick="resetUnit('${path}', '${unit.id}', '${originId}', '${finalDestinationId}', '${lclunitss.id}', '${headerId}');" />
                            </span>
                        </td>
                        <td>
                            <input type="checkbox" name="showAllPol" id="showAllPol" ${lclAddVoyageForm.showAllDr ? "checked" :""}
                                   onclick="showAllDr('${path}', '${unit.id}', '${originId}', '${finalDestinationId}', '${lclunitss.id}', '${headerId}');">Show All Pol
                            <input type="checkbox" name="showBooking" id="showBooking"  ${lclAddVoyageForm.showBooking ? "checked" :""}
                                   onclick="showOBKGBooking('${path}', '${unit.id}', '${originId}', '${finalDestinationId}', '${lclunitss.id}', '${headerId}');">Show OBKG
                            <input type="checkbox" name="showPreReleased" id="showPreReleased"  ${lclAddVoyageForm.showPreReleased ? "checked" :""}
                                   onclick="preReleasedDR('${path}', '${unit.id}', '${originId}', '${finalDestinationId}', '${lclunitss.id}', '${headerId}');">Show Pre-Release Only
                        </td>
                    </c:when>
                    <c:otherwise>
                        <td colspan="2">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="checkbox" name="bookScheduleNo" ${lclAddVoyageForm.bookScheduleNo eq 'Y' ? 'checked':''}
                                   value="${lclAddVoyageForm.bookScheduleNo}" id="bookScheduleNo"
                                   onclick="showCurrentVoyage('${path}', '${unit.id}', '${originId}', '${finalDestinationId}', '${lclunitss.id}', '${headerId}');"/>Current Voyage
                        </td>
                        <td>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="checkbox" name="isReleasedDr" id="isReleasedDr" ${lclAddVoyageForm.isReleasedDr eq 'Y' ? 'checked':''}
                                   value="${lclAddVoyageForm.isReleasedDr}"   onclick="showAllReleased('${path}', '${unit.id}', '${originId}', '${finalDestinationId}', '${lclunitss.id}', '${headerId}');"/>All Released
                        </td>
                        <td>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="checkbox" name="checkAllRealeaseWithCurrLoc" id="checkAllRealeaseWithCurrLoc"
                                   ${lclAddVoyageForm.checkAllRealeaseWithCurrLoc ? 'checked':''}
                                   value="${lclAddVoyageForm.checkAllRealeaseWithCurrLoc}"   
                                   onclick="showAllReleasedIncludeCurrloc('${path}', '${unit.id}', '${originId}', '${finalDestinationId}', '${lclunitss.id}', '${headerId}');"/>
                            All Rels Cur Loc 
                        </td>
                        <%-- <input type="checkbox" name="showINTR" id="showINTR" ${intransitDr ? 'checked' :''}
                               onclick="showINTR('${path}', '${unit.id}', '${originId}', '${finalDestinationId}', '${lclunitss.id}', '${headerId}');">INTR
                        --%>
                    </c:otherwise>
                </c:choose>
                <c:if test="${lclAddVoyageForm.filterByChanges != 'lclDomestic'}">
                    <td colspan="6" align="center" width="95%">RELEASED</td>
                </c:if>
                <td align="right" width="95%">
                    <img src="${path}/images/arrowforward.png" width="30" height="30"
                         <c:choose>
                             <c:when test="${lclAddVoyageForm.filterByChanges eq 'lclExport' || lclAddVoyageForm.filterByChanges eq 'lclCfcl'}">
                                 onclick="save('${path}', '${unit.id}', '${originId}', '${finalDestinationId}');"
                             </c:when>
                             <c:otherwise>
                                 onclick="save('${path}', '${unit.id}', '${lclSsdetail.departure.id}', '${lclSsdetail.arrival.id}');"
                             </c:otherwise>
                         </c:choose>
                         <c:if test="${lclunitss.status=='C'}">
                             style="visibility: hidden;"
                         </c:if>
                         />
                </td>
            </tr>
        </table>
    </div>
    <!-- ------------------------------------  separation section for header  -------------------------------- -->
    <div style="float: right;position: relative;width:49.9%;height:35px;">
        <table class="tableBorderNew">
            <tr class="tableHeadingNew" >
                <td align="left" width="5%">
                    <img src="${path}/images/arrowprevious.png" width="30" height="30"  onclick="deleteStuffedList('${path}', '${unit.id}', '${lclunitss.lclSsHeader.origin.id}', '${lclunitss.lclSsHeader.destination.id}');"
                         <c:if test="${lclunitss.status=='C'}">
                             style="visibility: hidden;"
                         </c:if>
                         />
                </td>
                <td  width="18%">PICKED ON UNIT </td>
                <td  width="2%">Unit#  </td>
                <td width="15%"  style="font-weight:bold;color: green;font-size: 12px;">${unit.unitNo}</td>
                <td  width="4%">DR'S</td>
                <td width="4%"  style="font-weight:bold;color: green;font-size: 12px;">${bookingUnitsBean.count}</td>
                <td  width="4%">Pcs</td>
                <td width="7%"  style="font-weight:bold;color: green;font-size: 12px;">${bookingUnitsBean.totalPieceCount}</td>
                <td  width="4%">Cuft</td>
                <td width="7%"  style="font-weight:bold;color: green;font-size: 12px;">${bookingUnitsBean.totalVolumeImperial}</td>
                <td  width="4%">Pounds</td>
                <td width="7%"  style="font-weight:bold;color: green;font-size: 12px;">${bookingUnitsBean.totalWeightImperial}</td>
            </tr>
        </table>
    </div>
    <!-- ------------------------------------  Top Header Row Ends Here -------------------------------- -->
</td></tr>
</table>
<table width="100%" border="0">
    <tr>
        <td width="50%">
            <!-- ------------------------------------  Destuffed List Section starts Here -------------------------------- -->
            <div class="scrollable-table stuffed" style="float:left;"> 
                <div>
                    <div  style="padding-bottom: 20px">
                        <table id="released_booking" style="border:1px solid #dcdcdc;border-collapse: collapse"> 
                            <thead>
                                <tr>
                                    <th>
                                        <c:choose>
                                            <c:when test="${lclAddVoyageForm.filterByChanges=='lclExport'  || lclAddVoyageForm.filterByChanges=='lclCfcl'}">
                                                <c:if test="${lclunitss.status=='E' && bookingUnitsBean.status!='PICKED' && bookingUnitsBean.status!='PRE' &&
                                                              bookingUnitsBean.status!='HAZ,PICKED' && bookingUnitsBean.status!='HAZ,PRE' 
                                                              && ( not empty bookingUnitsBean.curLocName || intransitDr)}">
                                                      &nbsp;
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <c:if test="${lclunitss.status=='E' && bookingUnitsBean.status!='PICKED' && bookingUnitsBean.status!='PRE' &&
                                                              bookingUnitsBean.status!='HAZ,PICKED' && bookingUnitsBean.status!='HAZ,PRE' }">
                                                      &nbsp;
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                    </th>
                                    <th></th>
                                    <th><div label="File#"/></th>
                                    <th><div label="Status"></th>
                                    <th><div label="Pieces"></th>
                                    <th><div label="Cuft"></th>
                                    <th><div label="Pounds"></th>
                                    <th><div label="Curr Loc"></th>
                                    <th><div label="Dispo"></th>
                                    <th><div label="Pol"></th>
                                    <th><div label="Remarks"></th>
                                    <th><div label="Hot Code"></th>
                                    <th><div label="Whsloc"></th>
                                </tr>
                            </thead>
                            <tbody> 
                                <c:forEach items="${destuffedList}" var="bookingUnitsBean">
                                    <c:set var="canDisplayConsolidateIcon" value="false"/>
                                    <c:set var="zebra" value="${zebra=='odd' ? 'even' : 'odd'}"/>

                                    <!-- ------------------ List For Export AND CFCL  -------------------------------- -->
                                    <c:choose>
                                        <c:when  test="${lclAddVoyageForm.filterByChanges=='lclExport' || lclAddVoyageForm.filterByChanges=='lclCfcl'}">
                                            <c:if test="${not empty  bookingUnitsBean.curLoc || (bookingUnitsBean.dispo eq 'INTR' && intransitDr)}">
                                                <tr class="${zebra}">
                                                    <td>

                                                        <span class="dimensionsRemarks${bookingUnitsBean.fileId}" style="display:none;"></span>
                                                        <span class="destuffed${bookingUnitsBean.fileId}" style="display:none;">${bookingUnitsBean.bookVoyageNo}</span>
                                                        <c:if test="${lclunitss.status=='E' && bookingUnitsBean.status!='PICKED' && bookingUnitsBean.status!='PRE' &&
                                                                      bookingUnitsBean.status!='HAZ,PICKED' && bookingUnitsBean.status!='HAZ,PRE' 
                                                                      && (not empty  bookingUnitsBean.curLocName || intransitDr)}">
                                                            <c:set var="canDisplayConsolidateIcon" value="true"/>
                                                            <input type="hidden" class="fileNo${bookingUnitsBean.fileId}" value="${bookingUnitsBean.fileNo}"/>
                                                            <input type="hidden" class="${bookingUnitsBean.fileId}" value="${bookingUnitsBean.consolidatesFileId}"/>
                                                            <input type="checkbox"  class="chkSaveDeStuff" id="${bookingUnitsBean.fileId}" 
                                                                   value="${bookingUnitsBean.fileId}" 
                                                                   onclick="checkDimensions('${bookingUnitsBean.fileId}', '${bookingUnitsBean.dimsLength}',
                                                                                   '${bookingUnitsBean.dimsHeight}', '${bookingUnitsBean.dimsWidth}', '${bookingUnitsBean.dimsWeight}');"/>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:if test="${not empty bookingUnitsBean.consolidatesFiles && canDisplayConsolidateIcon}">
                                                            <span title="${bookingUnitsBean.consolidatesFiles} <b class='red'>Click to pick all</b>">
                                                                <img src="${path}/jsps/LCL/images/consolidate.png"  height="12" width="12"
                                                                     id="consolidateIcon${bookingUnitsBean.fileId}" onClick="pickORUnPickConsolidateDr('${bookingUnitsBean.fileId}', true);"/>
                                                            </span>
                                                        </c:if>
                                                        <c:choose>
                                                            <c:when test="${bookingUnitsBean.status=='PICKED' || bookingUnitsBean.status=='HAZ,PICKED' 
                                                                            || bookingUnitsBean.status!='HAZ,PRE' || bookingUnitsBean.status=='PRE' || bookingUnitsBean.status==''}">
                                                                    <span style="height: 10px" title="${bookingUnitsBean.dimensionsToolTip}">
                                                                        <img src="${path}/img/icons/iicon.png" width="12" height="12" alt="info"  id="${bookingUnitsBean.fileNo}"  />
                                                                    </span>
                                                            </c:when>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <a style="cursor: pointer;" onclick="checkFileNumberLock('${path}', '${bookingUnitsBean.fileId}', '${bookingUnitsBean.fileNo}', '${user.userId}')">
                                                            <font color="blue">
                                                                <u>
                                                                    <c:choose>
                                                                        <c:when test="${bookingUnitsBean.shortShipSequence eq 0}">   
                                                                            ${fn:substring(bookingUnitsBean.poo,2,5)}-${bookingUnitsBean.fileNo}
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            ZZ${bookingUnitsBean.shortShipSequence}-${bookingUnitsBean.fileNo}
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </u>
                                                            </font>
                                                        </a>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test ="${bookingUnitsBean.haz}">
                                                                <img src="${path}/img/icons/danger..png"  style="vertical-align: middle" width="12" height="12" alt="Haz" 
                                                                     title="${not empty bookingUnitsBean.bookingHazmatDetails ? bookingUnitsBean.bookingHazmatDetails : 'No Haz Info entered'}"/>${bookingUnitsBean.status}
                                                            </c:when>
                                                            <c:otherwise>${bookingUnitsBean.status}</c:otherwise>
                                                        </c:choose> 
                                                    </td>
                                                    <td class="drPieceCount${bookingUnitsBean.status}">${bookingUnitsBean.totalPieceCount}</td>
                                                    <td class="drPieceVolume${bookingUnitsBean.status} volume${bookingUnitsBean.fileId}">${bookingUnitsBean.totalVolumeImperial}</td>
                                                    <td class="drPieceWeight${bookingUnitsBean.status} weight${bookingUnitsBean.fileId}">${bookingUnitsBean.totalWeightImperial}</td>
                                                    <c:choose>
                                                        <c:when test ="${bookingUnitsBean.dispo ne 'INTR' &&  bookingUnitsBean.dispo ne 'VSAL'}">
                                                            <td>${bookingUnitsBean.curLoc}</td>
                                                        </c:when>
                                                        <c:otherwise> <td></td></c:otherwise>
                                                    </c:choose> 
                                                    <td>${bookingUnitsBean.dispo}</td>
                                                    <td>${bookingUnitsBean.polUnCode}</td>
                                                    <td>
                                                        <a style=" cursor:pointer" title="${fn:toUpperCase(bookingUnitsBean.remarks)}">${fn:substring(bookingUnitsBean.remarks,0,10)}</a>
                                                    </td>
                                                    <td>
                                                        <span id="hotCodeId" title="${bookingUnitsBean.hotCodes}">
                                                            ${bookingUnitsBean.hotCodeKey}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <a style=" cursor:pointer" title="${bookingUnitsBean.warehouseLineToolTip}">${bookingUnitsBean.warehouseLine}</a>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- ------------------ List For DOMESTIC -------------------------------- -->
                                            <tr class="${zebra}">
                                                <td>
                                                    <span class="dimensionsRemarks${bookingUnitsBean.fileId}"  style="display:none;"></span>
                                                    <span class="destuffed${bookingUnitsBean.fileId}" style="display:none;">${bookingUnitsBean.bookVoyageNo}</span>
                                                    <c:if test="${lclunitss.status=='E'  && bookingUnitsBean.status!='PRE' && (bookingUnitsBean.status!='PICKED' 
                                                                  || bookingUnitsBean.dispo=='INTR') && bookingUnitsBean.status!='HAZ,PICKED' 
                                                                  && bookingUnitsBean.status!='HAZ,PRE'}">
                                                        <c:set var="canDisplayConsolidateIcon" value="true"/>
                                                        <input type="hidden" class="fileNo${bookingUnitsBean.fileId}" value="${bookingUnitsBean.fileNo}"/>
                                                        <input type="hidden" class="${bookingUnitsBean.fileId}" value="${bookingUnitsBean.consolidatesFileId}"/> 
                                                        <input type="checkbox"  class="chkSaveDeStuff ${bookingUnitsBean.fileId}" id="${bookingUnitsBean.fileId}" value="${bookingUnitsBean.fileId}" 
                                                               onclick="checkDimensions('${bookingUnitsBean.fileId}', '${bookingUnitsBean.dimsLength}',
                                                                               '${bookingUnitsBean.dimsHeight}', '${bookingUnitsBean.dimsWidth}',
                                                                               '${bookingUnitsBean.dimsWeight}');"/>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty bookingUnitsBean.consolidatesFiles && canDisplayConsolidateIcon}">
                                                        <span title="${bookingUnitsBean.consolidatesFiles} <b class='red'>Click to pick all</b>">
                                                            <img src="${path}/jsps/LCL/images/consolidate.png"  height="12" width="12"
                                                                 id="consolidateIcon${bookingUnitsBean.fileId}" onClick="pickORUnPickConsolidateDr('${bookingUnitsBean.fileId}', true);"/>
                                                        </span>
                                                    </c:if>
                                                    <c:choose>
                                                        <c:when test="${bookingUnitsBean.status=='PICKED' || bookingUnitsBean.status=='HAZ,PICKED' || bookingUnitsBean.status!='HAZ,PRE' || bookingUnitsBean.status=='PRE' || bookingUnitsBean.status==''}">
                                                            <span title="${bookingUnitsBean.dimensionsToolTip}">
                                                                <img src="${path}/img/icons/iicon.png" width="12" height="12" alt="info"  id="${bookingUnitsBean.fileNo}"  />
                                                            </span>
                                                        </c:when>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <a style="cursor: pointer;" onclick="checkFileNumberLock('${path}', '${bookingUnitsBean.fileId}', '${bookingUnitsBean.fileNo}', '${user.userId}')">
                                                        <font color="blue">
                                                            <u>
                                                                <c:choose>
                                                                    <c:when test="${bookingUnitsBean.shortShipSequence eq 0}">   
                                                                        ${fn:substring(bookingUnitsBean.poo,2,5)}-${bookingUnitsBean.fileNo}
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        ZZ${bookingUnitsBean.shortShipSequence}-${bookingUnitsBean.fileNo}
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </u>
                                                        </font>
                                                    </a>
                                                </td>
                                                <td> 
                                                    <c:choose>
                                                        <c:when test ="${bookingUnitsBean.haz}">
                                                            <img src="${path}/img/icons/danger..png"  style="cursor:pointer" width="12" height="12" alt="Haz" 
                                                                 title="${not empty bookingUnitsBean.bookingHazmatDetails ? bookingUnitsBean.bookingHazmatDetails : 'No Haz Info entered'}"/>${bookingUnitsBean.status}
                                                        </c:when>
                                                        <c:otherwise>${bookingUnitsBean.status}</c:otherwise>
                                                    </c:choose> 
                                                </td>
                                                <td class="drPieceCount${bookingUnitsBean.status}">${bookingUnitsBean.totalPieceCount}</td>
                                                <td class="drPieceVolume${bookingUnitsBean.status} volume${bookingUnitsBean.fileId}">${bookingUnitsBean.totalVolumeImperial}</td>
                                                <td class="drPieceWeight${bookingUnitsBean.status} weight${bookingUnitsBean.fileId}">${bookingUnitsBean.totalWeightImperial}</td>
                                                <c:choose>
                                                    <c:when test ="${bookingUnitsBean.dispo ne 'INTR' &&  bookingUnitsBean.dispo ne 'VSAL'}">
                                                        <td>${bookingUnitsBean.curLoc}</td>
                                                    </c:when>
                                                    <c:otherwise> <td></td></c:otherwise>
                                                </c:choose> 
                                                <td>${bookingUnitsBean.dispo}</td>
                                                <td>${bookingUnitsBean.polUnCode}</td>
                                                <td>
                                                    <a style=" cursor:pointer" title="${fn:toUpperCase(bookingUnitsBean.remarks)}">${fn:substring(bookingUnitsBean.remarks,0,10)}</a>
                                                </td>
                                                <td>
                                                    <span id="hotCodeId" title="${bookingUnitsBean.hotCodes}">
                                                        ${bookingUnitsBean.hotCodeKey}
                                                    </span>
                                                </td>
                                                <td>
                                                    <a style=" cursor:pointer" title="${bookingUnitsBean.warehouseLineToolTip}">${bookingUnitsBean.warehouseLine}</a>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </tbody>
                        </table> 
                    </div>
                </div>
            </div>
            <!-- ------------------------------------  Destuffed List Section ends here -------------------------------- -->
        </td>
        <td width="50%">
            <!-- ------------------------------------  Stuffed List Section starts here  -------------------------------- -->
            <div class="scrollable-table stuffed" style="float:left;">
                <div>
                    <div  style="padding-bottom: 20px">
                        <table  style="border:1px solid #dcdcdc;border-collapse: collapse"> 
                            <thead><tr>
                                    <c:if test="${lclunitss.status=='E'}">
                                        <th>&nbsp;</th>
                                        </c:if>
                                    <th></th>
                                    <th><div label="File#"/></th>
                                    <th><div label="Status"></th>
                                    <th><div label="Pieces"></th>
                                    <th><div label="Cuft"></th>
                                    <th><div label="Pounds"></th>
                                    <th><div label="Remarks"></th>
                                    <th><div label="Hot Code"></th>
                                    <th><div label="Whsloc"></th>
                                </tr></thead>
                            <tbody>
                                <c:forEach items="${stuffedList}" var="bookingUnitsBean">
                                    <c:choose>
                                        <c:when test="${zebra=='odd'}">
                                            <c:set var="zebra" value="even"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="zebra" value="odd"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <tr class="${zebra}">
                                        <c:if test="${lclunitss.status=='E'}">
                                            <td width="2%">
                                                <input type="hidden" class="pickedFileNo${bookingUnitsBean.fileId}" value="${bookingUnitsBean.fileNo}"/>
                                                <input type="hidden" class="consolidate${bookingUnitsBean.fileId}" value="${bookingUnitsBean.consolidatesFileId}"/> 
                                                <input type="checkbox"  class="chkDeleteStuff" id="pickedFileId${bookingUnitsBean.fileId}" 
                                                       value="${bookingUnitsBean.fileId}" onclick="checkConsolidation('${bookingUnitsBean.fileId}');"/>
                                            </td>
                                        </c:if>
                                        <td>
                                            <c:if test="${not empty bookingUnitsBean.consolidatesFiles}">
                                                <span title="${bookingUnitsBean.consolidatesFiles} <b class='red'>Click to Unpick all</b>">
                                                    <img src="${path}/jsps/LCL/images/consolidate.png"  height="12" width="12"
                                                         id="consolidateIcon${bookingUnitsBean.fileId}" onclick="checkConsoPickedIcon('${bookingUnitsBean.fileId}');"/>
                                                </span>
                                            </c:if>
                                            <span title="<font size='2' color=#008000><b>Dimensions Details</b></font></br><table>${bookingUnitsBean.dimensionData}</table>">
                                                <img src="${path}/img/icons/iicon.png" width="12" height="12" alt="info"  id="${bookingUnitsBean.fileNo}"/>
                                            </span>            
                                        </td>
                                        <td>
                                            <a style="cursor: pointer;" onclick="checkFileNumberLock('${path}', '${bookingUnitsBean.fileId}', '${bookingUnitsBean.fileNo}', '${user.userId}')">
                                                <font color="blue">
                                                    <u>${bookingUnitsBean.fileNo}</u>
                                                </font>
                                            </a> 
                                        </td>
                                        <td>
                                            <c:choose><c:when test ="${bookingUnitsBean.haz}">
                                                    <img src="${path}/img/icons/danger..png"  style="cursor:pointer" width="12" height="12" alt="Haz" 
                                                         title="${not empty bookingUnitsBean.bookingHazmatDetails ? bookingUnitsBean.bookingHazmatDetails : 'No Haz Info entered'}"/>${bookingUnitsBean.status}
                                                </c:when>
                                                <c:otherwise>
                                                    ${bookingUnitsBean.status}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${bookingUnitsBean.totalPieceCount}</td>
                                        <td class="pickedVolume">${bookingUnitsBean.totalVolumeImperial}</td>
                                        <td>${bookingUnitsBean.totalWeightImperial}</td>
                                        <td>
                                            <a style=" cursor:pointer" title="${fn:toUpperCase(bookingUnitsBean.remarks)}">${fn:substring(bookingUnitsBean.remarks,0,10)}</a>
                                        </td>
                                        <td>
                                            <span id="hotCodeId" title="${bookingUnitsBean.hotCodes}">
                                                ${bookingUnitsBean.hotCodeKey}
                                            </span>
                                        </td>
                                        <td>
                                            <a style=" cursor:pointer" title="${bookingUnitsBean.warehouseLineToolTip}">${bookingUnitsBean.warehouseLine}</a>
                                        </td>
                                    </tr>
                                </c:forEach>   
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <!-- ------------------------------------  Stuffed List Section Ends here -------------------------------- -->
        </td>
    </tr>
</table>                    
</body>
