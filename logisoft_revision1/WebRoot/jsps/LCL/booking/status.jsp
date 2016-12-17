<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:hidden name="callBackFlag" id="callBackFlag" value="${lclBookingForm.callBackFlag}"/><%--Transhipmentflag--%>
<%--These two hidden fields using imports and Exports--%>
<cong:hidden name="moduleName" id="moduleName" value="${lclBookingForm.moduleName}"/>
<input type="hidden" name="path" id="path" value="${path}"/>
<input type="hidden" name="copyFnVal" id="copyFnVal" value="${lclBookingForm.copyFnVal}"/>
<input type="hidden" name="fileType" id="fileType" value="${lclBooking.lclFileNumber.state}"/>
<cong:hidden name="flag" id="flag" value="${flag}"/>
<cong:hidden name="fileNumberId" id="fileNumberId" value="${lclBooking.fileNumberId}"/>
<cong:hidden name="fileNumber" id="fileNumber" value="${lclBooking.lclFileNumber.fileNumber}"/>
<cong:hidden name="bookingType" id="bookingType" value="${lclBooking.bookingType}"/>
<input type="hidden" id="fileStatus" name="fileStatus" value="${lclBooking.lclFileNumber.status}"/><%--file Status--%>
<input type="hidden" id="shortShipFlag" name="shortShipFlag" value="${lclBooking.lclFileNumber.shortShip}"/><%--file Status--%>
<input type="hidden" name="oldCurrentLocationName" id="oldCurrentLocationName" value="${currentLocationName}"/>
<input type="hidden" id="userRoleId" name="userRoleId" value="${loginuser.role.id}"><%-- Login User Role Id--%>
<input type="hidden" id="loginUserId" name="loginUserId" value="${loginuser.userId}"><%-- Login User Id--%>
<input type="hidden" id="loginName" name="loginName" value="${loginuser.loginName}"><%-- Login Name--%>
<input type="hidden" id="bookedForVoyage" name="bookedForVoyage" value="${roleDuty.byPassVoyage}"><%-- Booked for Voyage Role Duty--%>
<input type="hidden" id="releasedDatetime" name="releasedDatetime" value="${lclBookingExport.releasedDatetime}"/>
<%--These two hidden fields using imports --%>
<cong:hidden name="headerId" id="headerId" value="${lclBookingForm.headerId}"/>
<cong:hidden name="impSearchFlag" id="impSearchFlag" value="${lclBookingForm.impSearchFlag}"/>
<cong:hidden name="shipperToolTip" id="shipperToolTip" value="${lclBookingForm.shipperToolTip}"/>
<cong:hidden name="consigneeToolTip" id="consigneeToolTip" value="${lclBookingForm.consigneeToolTip}"/>
<cong:hidden name="notifyToolTip" id="notifyToolTip" value="${lclBookingForm.notifyToolTip}"/>
<cong:hidden name="notify2ToolTip" id="notify2ToolTip" value="${lclBookingForm.notify2ToolTip}"/>
<cong:hidden name="dispositionToolTip" id="dispositionToolTip" value="${lclBookingForm.dispositionToolTip}"/>
<input type="hidden" name="allowDisposition" id="allowDisposition" value="${allowDisposition}"/>
<cong:hidden name="eculineCommodity" id="eculineCommodity"/>
<div id="booking-info" style="padding-top: 4px;padding-bottom: 15px;">
    <table border="0">
        <tr>
            <td width="8%">
                <table>
                    <tr class="textBoldforlcl">
                        <td>File No :
                    <c:if test="${not empty lclBooking.lclFileNumber.fileNumber}">
                        <span class="fileNo" id="fileNumberBooking">
                            <c:choose>
                                <c:when test="${lclBookingForm.moduleName eq 'Exports' && lclBooking.bookingType eq 'E' &&  lclBooking.lclFileNumber.shortShip eq 'false'}">
                                    ${fn:substring(lclBooking.portOfOrigin.unLocationCode,2,5)}-${lclBooking.lclFileNumber.fileNumber}
                                </c:when>
                                <c:when test="${lclBooking.bookingType eq 'T' || lclBooking.bookingType eq 'I'}">
                                    IMP-${lclBooking.lclFileNumber.fileNumber}
                                </c:when>
                                <c:when test="${lclBooking.lclFileNumber.shortShip}">
                                    ZZ${lclBooking.lclFileNumber.shortShipSequence}-${lclBooking.lclFileNumber.fileNumber}
                                </c:when>
                            </c:choose>
                        </span>
                        <c:if test="${lclBookingForm.moduleName eq 'Exports' && lclBooking.lclFileNumber.state ne 'BL'}">
                            <img src="${path}/images/edit.png"  style="cursor:pointer" width="15" height="15" alt="Edit DR Number" 
                                 onclick="changeDrNumber('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}')"
                                 title="Allow to change the D/R number"/>
                        </c:if>
                    </c:if>
            </td>
        </tr>
        <c:choose>
            <c:when test="${lclBookingForm.moduleName ne 'Imports'}">
                <tr>
                    <td class="textBoldforlcl">
                <c:set var="ECI_OTI_label" value ="${lclBookingForm.companyCode eq 'ECU' ? 'Econo' : 'OTI'}"/>
                <c:set var="ECI_OTI_value" value ="${lclBookingForm.companyCode eq 'ECU' ? 'ECI' : 'OTI'}"/>
                Brand: <cong:radio container="null" name="businessUnit" value='${ECI_OTI_value}' id="econo" 
                                   onclick="confirmBussinessUnit(this, 'eculine');" styleClass="businessUnit"/><span class="econo">${ECI_OTI_label}</span>
                <cong:radio container="null" name="businessUnit" value='ECU' id="eculine"  
                            onclick="confirmBussinessUnit(this, 'econo');" styleClass="businessUnit"/><span class="eculine">ECU Worldwide</span>
                </td>
                </tr>
            </c:when>
            <c:otherwise>
                <tr>
                    <td class="textBoldforlcl">
                <c:set var="ECU_OTI_Label" value ="${lclBookingForm.companyCode eq '03' ? 'ECI' : 'OTI'}"/>
                Brand: <cong:radio container="null" name="businessUnit" value='${lclBookingForm.businessUnit}' id="${fn:trim(ECU_OTI_Label)}" 
                                   onclick="confirmBussinessUnitInImport('${lclBooking.lclFileNumber.id}', '${ECU_OTI_Label}');"/> 
                ${ECU_OTI_Label eq 'ECI' ? 'Econo' : 'OTI'}
                <cong:radio container="null" name="businessUnit" value='ECU' id="ECU"  
                            onclick="confirmBussinessUnitInImport('${lclBooking.lclFileNumber.id}', 'ECU');"/>Ecu Worldwide
                <input hidden="oldBrand" id="oldBrand" value="${lclBookingForm.businessUnit}"/>
                </td>
                </tr>      
            </c:otherwise>     
        </c:choose>
    </table>
</td>
<td>
<c:if test="${not empty lclBooking.lclFileNumber.lclQuote}">
    <div class="info-box" style="padding-top: 4px; margin-left: 20px;">
        <b class="textlabelsBold">&nbsp;Quote By :</b>&nbsp;
        <b class="headerlabel" style="color:blue;text-transform: uppercase">${lclBooking.lclFileNumber.lclQuote.enteredBy.loginName}</b>&nbsp
        <b class="textlabelsBold">On :</b>&nbsp;
        <fmt:formatDate pattern="dd-MMM-yyyy hh:mm" var="qCreatedTime" value="${lclBooking.lclFileNumber.lclQuote.enteredDatetime}"/>
        <b class="headerlabel" style="color:blue">${qCreatedTime}</b>&nbsp
    </div>
</c:if>
<div class="info-box" style="padding-top: 4px;${empty lclBooking.lclFileNumber.lclQuote ? 'margin-left: 20px;' : ''}">
    <b class="textlabelsBold">&nbsp;Booking By :</b>&nbsp;
    <b class="headerlabel" style="color:blue;text-transform: uppercase">${lclBooking.enteredBy.loginName}</b>&nbsp
    <b class="textlabelsBold">On :</b>&nbsp;
    <fmt:formatDate pattern="dd-MMM-yyyy hh:mm" var="bkgCreatedTime" value="${lclBooking.enteredDatetime}"/>
    <b class="headerlabel" style="color:blue">${bkgCreatedTime}</b>&nbsp
</div>
<c:if test="${not empty lclBooking.lclFileNumber.lclBl && lclBookingForm.moduleName ne 'Imports'}">
    <div class="info-box" style="padding-top: 4px;">
        <b class="textlabelsBold">&nbsp;BL By :</b>&nbsp;
        <b class="headerlabel" style="color:blue;text-transform: uppercase">${lclBooking.lclFileNumber.lclBl.enteredBy.loginName}</b>&nbsp
        <b class="textlabelsBold">On :</b>&nbsp;
        <fmt:formatDate pattern="dd-MMM-yyyy hh:mm" var="blCreatedTime" value="${lclBooking.lclFileNumber.lclBl.enteredDatetime}"/>
        <b class="headerlabel" style="color:blue">${blCreatedTime}</b>&nbsp
    </div>
</c:if>
</td>
<c:if test="${lclBookingForm.moduleName ne 'Imports'}">
    </tr>
    </table>
    <table border="0" style="text-align: right; width: 75%; float: right"><tr>
            </c:if>
            <td id="fileNo" class="textBoldforlcl" style="padding-top: 2px; left:25px;" >
        <c:choose>
            <c:when test="${lclBookingForm.moduleName eq 'Exports'}">
                <c:choose>
                    <c:when test="${not empty lclBooking.lclFileNumber.fileNumber && allowDisposition eq 'true'}"> 
                        <a href="#" onclick="changeDisposition('${path}');" style="color: blue">Disposition:</a>
                        <b style="color: #FF0000;font-size:15px;" class="headerlabel"><label id="descId">${lclBookingForm.description}, </label>
                        <c style="color: #FF0000;font-size:15px;" class="headerlabel"><label id="dispoId">${lclBookingForm.disposition}</label>
                            <d:if test="${not empty lclBooking.lclFileNumber}"> 
                                <span id="dispImp"><img src="${path}/img/icons/iicon.png" style="vertical-align: middle"  width="12" height="12" alt="disposition" id="disposition"/></span>
                            </d:if></c>
                        </b>
                    </c:when>
                    <c:otherwise>
                        Disposition:
                        <b style="color: #FF0000;font-size:15px;" class="headerlabel"><label id="dispoId">${lclBookingForm.disposition}</label>
                            <c:if test="${not empty lclBooking.lclFileNumber}"> 
                                <span id="dispImp"><img src="${path}/img/icons/iicon.png" style="vertical-align: middle"  width="12" height="12" alt="disposition" id="disposition"/></span>
                            </c:if>
                        </b>
                    </c:otherwise>
                </c:choose>
            </c:when> 
            <c:otherwise>
                Disposition:
                <c:if test="${not empty lclBooking.lclFileNumber}">
                    <b style="color: #FF0000;font-size:15px;" class="headerlabel"><label id="dispoId">${lclBookingForm.disposition}</label>
                        <c:if test="${lclBookingForm.moduleName eq 'Imports' && not empty lclBookingForm.impEciVoyage}">
                            <span id="dispImp"><img src="${path}/img/icons/iicon.png" style="vertical-align: middle"  width="16" height="16" alt="disposition" id="impDisposition"/></span>
                        </c:if>
                    </b>
                </c:if>  
            </c:otherwise>
        </c:choose>

        &nbsp;&nbsp;&nbsp; Inventory Status:<b style="color: #FF0000;" class="headerlabel">
            <label id="statuslabel" style="font-size: 15px;">
                <c:if test="${not empty lclBooking.lclFileNumber.fileNumber}">
                    <c:choose>
                        <c:when test="${not empty lclBookingImport.pickupDateTime && lclBookingForm.moduleName eq 'Imports'}">
                            <span style="color: blue"><c:out value="Picked Up"/></span>
                        </c:when>
                        <c:when test="${lclBooking.lclFileNumber.status eq 'X'}">
                            <c:out value="Terminated"/>
                            <c:if test="${not empty lclBooking.terminateDesc}">
                                <c:out value="/"/><c:out value="${lclBooking.terminateDesc}"/>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${(lclBooking.lclFileNumber.status=='B' ||  lclBooking.lclFileNumber.status=='W' || 
                                                lclBooking.lclFileNumber.status=='RF' ||  lclBooking.lclFileNumber.status=='L' ||
                                                lclBooking.lclFileNumber.status eq 'M') && lclBookingForm.moduleName ne 'Imports'}">

                                    <c:choose>
                                        <c:when test="${lclBooking.bookingType=='E'}">
                                            <c:if test="${lclBooking.lclFileNumber.status eq 'L' || lclBooking.lclFileNumber.status eq 'M'}">
                                                <c:out value="Loaded"/>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${lclBooking.lclFileNumber.status eq 'L'}">
                                                <c:out value="Loaded"/>
                                            </c:if>

                                            <c:if test="${lclBooking.lclFileNumber.status eq 'M' && lclBookingExport.prereleaseDatetime eq null && lclBookingExport.releasedDatetime eq null}">
                                                <c:out value="Posted"/>
                                            </c:if>
                                            <c:if test="${lclBooking.lclFileNumber.status eq 'M' && lclBookingExport.releasedDatetime ne null}">
                                                <c:out value="Loaded"/>
                                            </c:if>    
                                        </c:otherwise>
                                    </c:choose>
                                    <c:if test="${lclBooking.lclFileNumber.status eq 'WV' || lclBooking.lclFileNumber.status eq 'W' 
                                                  && (lclBookingForm.disposition  eq 'RCVD' || lclBookingForm.disposition  eq 'INTR')
                                                  && lclBookingExport.prereleaseDatetime eq null && lclBookingExport.releasedDatetime eq null}">
                                        <c:out value="WAREHOUSE(Verified)"/>
                                    </c:if>
                                    <c:if test="${lclBooking.lclFileNumber.status eq 'WU' ||  lclBooking.lclFileNumber.status eq 'W' 
                                                  && lclBookingForm.disposition  eq 'RUNV' && lclBookingExport.prereleaseDatetime eq null 
                                                  && lclBookingExport.releasedDatetime eq null}">
                                        <c:out value="WAREHOUSE(Un-verified)"/>
                                    </c:if>
                                    <c:if test="${lclBookingExport.prereleaseDatetime ne null && lclBookingExport.releasedDatetime eq null}">
                                        <c:out value="WAREHOUSE(Pre Released)"/>
                                    </c:if>
                                    <c:if test="${lclBookingExport.releasedDatetime ne null && lclBooking.lclFileNumber.status ne 'L' 
                                                  && lclBooking.lclFileNumber.status ne 'M' && lclBooking.lclFileNumber.status ne 'RF'}">
                                        <c:out value="Released"/>
                                    </c:if>

                                    <c:if test="${lclBooking.lclFileNumber.status eq 'RF'}">
                                        <c:out value="Refused"/>
                                    </c:if>                                            
                                    <c:if test="${lclBooking.lclFileNumber.status eq 'B' && lclBookingExport.releasedDatetime eq null && lclBookingExport.prereleaseDatetime eq null}">
                                        <c:out value="Booking"/>
                                    </c:if>   
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${lclBooking.lclFileNumber.state eq 'B' && lclBookingForm.moduleName ne 'Imports'}">
                                        <c:out value="Booking"/></c:if>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </label>
        </b>
        </td>
        <c:if test="${lclBooking.bookingType eq 'T'}">
            <td>
                <span class="osdValuesDisplay">
                    <c:choose>
                        <c:when test="${lclBookingForm.moduleName eq 'Imports'}">
                            Transhipment Import View
                            <c:set var="viewMessage" value="Export Transshipment View"/>
                        </c:when>
                        <c:otherwise>Transhipment Export View
                            <c:set var="viewMessage" value="Import Transshipment View"/>
                        </c:otherwise>
                    </c:choose></span>
                <span title="${viewMessage}">
                    <img src="${path}/img/icons/trigger.gif" align="middle" height="18" width="18" alt="${viewMessage}"  id="triggerImg" 
                         onclick="gotoExportDrFromImpDrScreen('${path}', '${lclBooking.lclFileNumber.id}', '${lclBookingForm.moduleName}');"/>
                </span>
            </td>
        </c:if>
        <td class="textBoldforlcl" style="padding-top: 6px">
        <c:choose><c:when test="${lclBookingForm.moduleName eq 'Imports'}">
                <span class="textBoldforlcl">Accounting Status:</span>
                <c:if test="${lclBooking.lclFileNumber.status eq 'M'}">
                    <span style='color:green;font-weight:bold;font-size: 13px;'>POSTED</span></c:if>
            </c:when><c:otherwise>
                <c:choose>
                    <c:when test="${not empty lclBooking.lclFileNumber.status && roleDuty.lclCurrentLocation && lclBooking.lclFileNumber.status ne 'B'
                                    && lclBooking.lclFileNumber.status ne 'L' && lclBookingForm.disposition ne 'INTR' &&  lclBookingForm.disposition ne 'VSAL'}">
                        <a href="#" onclick="changeCurrentLocation('${path}');" style="color: blue">Current Location:</a>
                    </c:when>
                    <c:otherwise>Current Location:</c:otherwise>
                </c:choose>
            </c:otherwise></c:choose>
        <c:if test="${lclBookingForm.disposition ne 'INTR' &&  lclBookingForm.disposition ne 'VSAL'}">
            <b style="color: #FF0000;" class="headerlabel"><label id="currentLocation"/>${currentLocationName}</b>
        </c:if>
        </td>
        <td style="float:right" class="textlabelsBoldforlcl">
            <div class="textlabelsBoldforlcl"> Unknown Dest
                <cong:radio container="null" name="nonRated" id="nonRatedY" value="Y" onclick="checkNonRated('NL')"/>Yes
                <cong:radio container="null" name="nonRated" id="nonRatedN" value="N" onclick="checkNonRated('NL')"/>No&nbsp;&nbsp;
            </div>
        </td>
        </tr>
    </table>
</div>
<%-- voyage Search Flag --%>
<cong:hidden id="searchOriginId" name="searchOriginId" value="${lclBookingForm.searchOriginId}"/>
<cong:hidden id="searchFdId" name="searchFdId" value="${lclBookingForm.searchFdId}"/>
<cong:hidden id="searchTerminalNo" name="searchTerminalNo" value="${lclBookingForm.searchTerminalNo}"/>
<cong:hidden id="searchLoginId" name="searchLoginId" value="${lclBookingForm.searchLoginId}"/>
<cong:hidden id="searchUnitNo" name="searchUnitNo" value="${lclBookingForm.searchUnitNo}"/>
<cong:hidden id="searchMasterBl" name="searchMasterBl" value="${lclBookingForm.searchMasterBl}"/>
<cong:hidden id="searchAgentNo" name="searchAgentNo" value="${lclBookingForm.searchAgentNo}"/>
<cong:hidden id="searchVoyageNo" name="searchVoyageNo" value="${lclBookingForm.searchVoyageNo}"/>
<cong:hidden id="searchDispoId" name="searchDispoId" value="${lclBookingForm.searchDispoId}"/>
<cong:hidden id="limit" name="limit" value="${lclBookingForm.limit}"/>
<cong:hidden id="searchOrigin" name="searchOrigin" value="${lclBookingForm.searchOrigin}"/>
<cong:hidden id="searchFd" name="searchFd" value="${lclBookingForm.searchFd}"/>
<cong:hidden id="searchTerminal" name="searchTerminal" value="${lclBookingForm.searchTerminal}"/>
