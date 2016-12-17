<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:form  action="/lclAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
    <cong:hidden id="unitNo" name="unitNo"/>
    <cong:hidden id="unitssId" name="unitssId"/>
    <cong:hidden id="methodName" name="methodName"/>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
        <tr class="tableHeadingNew">
            <td width="20%">
                View DRs
            </td>
            <td width="5%"><span class="blackBold"> UNIT# </span></td>
            <td width="15%"><span class="greenBold14px">${lclAddVoyageForm.unitNo}</span></td>
            <td width="5%"><span class="blackBold"> VOYAGE# </span></td>
            <td width="10%"><span class="greenBold14px">${lclAddVoyageForm.scheduleNo}</span></td>
            <td width="10%"><span class="blackBold"> BOOKING# : </span></td>
            <td width="20%"><span class="${not empty bookingNo ? 'greenBold14px':'redBold'}">
                    ${empty bookingNo ? 'Not Assigned' : bookingNo}</span></td>
            <td width="20%">
                <c:if test="${not empty pickedViewDrUnitList}">
                    <span class="button-style1" onclick="exportToExcel()">
                        ExportToExcel
                    </span>
                    <span class="button-style1" onclick="toggleViewDr()">
                        Toggle
                    </span>
                </c:if>

            </td>
        </tr>
    </table>
    <div style="overflow: auto;" align="center" id="view1">
        <c:set var="prepaidAmt" value="${0.00}"/>
        <c:set var="collectAmt" value="${0.00}"/>
        <c:set var="totalDrCount" value="${0}"/>
        <c:set var="piecesCount" value="${0}"/>
        <c:set var="cuftCount" value="${0.000}"/>
        <c:set var="poundsCount" value="${0.000}"/>
        <table border="0" id="manifestDr" width="100%"
               class="dataTable" style="border-collapse: collapse; border: 1px solid #dcdcdc;width:100%">
            <thead>
                <tr>
                    <th>File#</th>
                    <th>BL#</th>
                    <th>Type</th>
                    <th>BL Status</th>
                    <th>Dispo</th>
                    <th>CurLoc</th>
                    <th>Doc</th>
                    <th>Pieces</th>
                    <th>Cuft</th>
                    <th>Pounds</th>
                    <th>Origin</th>
                    <th>Pol</th>
                    <th>Pod</th>
                    <th>Destn</th>
                    <th>BookedVoy</th>
                    <th>Shipper</th>
                    <th>Forwarder</th>
                    <th>Consignee</th>
                    <th>Bill TM</th>
                    <th>HotCodes</th>
                    <th>COL</th>
                    <th>PPD</th>
                </tr>
            </thead>
            <c:if test='${not empty pickedViewDrUnitList}'>
                <tbody>
                    <c:forEach items="${pickedViewDrUnitList}" var="manifestBean">
                        <c:choose>
                            <c:when test="${zebra eq 'odd'}">
                                <c:set var="zebra" value="even"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="zebra" value="odd"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="${zebra}">
                            <td>
                                <a style="cursor: pointer;" onclick="editViewBlScreen('${path}', '${manifestBean.fileId}', '${manifestBean.state}')">
                                    <font color="blue">
                                        <c:set var="totalDrCount" value="${totalDrCount + 1}"/>
                                        <u> ${manifestBean.fileNo}</u>
                                    </font>
                                </a>
                                <c:if test="${manifestBean.hazmat eq 'true'}">
                                    <img src="${path}/images/icons/danger.png" width="12" height="12" alt="HazardousIcon" title="Hazardous"/>
                                </c:if>
                            <td>${manifestBean.blNo}</td>
                            <td>${manifestBean.rateType}</td>
                            <td class="${manifestBean.isCorrection ? 'redBold14Px' : manifestBean.className}">${manifestBean.statusLabel}</td>
                            <c:if test="${not empty manifestBean.disposition}">
                                <c:set var="disposition" value="${fn:substringBefore(manifestBean.disposition,'~~~')}" />
                                <c:set var="dispositionVal" value="${fn:substringAfter(manifestBean.disposition,'~~~')}" />
                                <c:set var="dispositionTooltip" value="${fn:substringBefore(dispositionVal,'~~~')}" />
                                <c:set var="Location" value="${fn:substringAfter(dispositionVal,'~~~')}" />
                                <c:set var="crtLocation" value="${fn:substringBefore(Location,'~~~')}" />
                                <c:set var="crtLocationTooltip" value="${fn:substringAfter(Location,'~~~')}" />
                                <td><span title="${dispositionTooltip}">${disposition}</span></td>
                                <td><c:if test="${disposition ne 'INTR'}"> 
                                        <span title="${crtLocationTooltip}">${crtLocation}</span> 
                                    </c:if>
                                </td>
                            </c:if>
                            <td>${manifestBean.doc}</td>
                            <td>
                                <c:set var="piecesCount" value="${piecesCount + manifestBean.totalPieceCount}" />
                                ${manifestBean.totalPieceCount}
                            </td>
                            <td>
                                <c:set var="cuftCount" value="${cuftCount + manifestBean.totalVolumeImperial}" />
                                ${manifestBean.totalVolumeImperial}
                            </td>
                            <td>
                                <c:set var="poundsCount" value="${poundsCount + manifestBean.totalWeightImperial}" />
                                ${manifestBean.totalWeightImperial}
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${manifestBean.pooPickup eq 1 && not empty manifestBean.pickupCity}">
                                        <span  style="color:red;" title="Origin=${manifestBean.originName}/${manifestBean.originState}/${manifestBean.origin}
                                               <br>Door Origin=${fn:substring(manifestBean.pickupCity,fn:indexOf(manifestBean.pickupCity,'-')+1,fn:length(manifestBean.pickupCity))}">
                                            ${manifestBean.origin}
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span title="${manifestBean.originName}/${manifestBean.originState}/${manifestBean.origin}">
                                            ${manifestBean.origin}
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <span title="${manifestBean.polName}/${manifestBean.polState}/${manifestBean.pol}">
                                    ${manifestBean.pol}
                                </span>
                            </td>
                            <td>
                                <span title="${manifestBean.podName}/${manifestBean.podCountry}/${manifestBean.pod}">
                                    ${manifestBean.pod}
                                </span>
                            </td>
                            <td>
                                <span title="${manifestBean.destinationName}/${manifestBean.destinationCountry}/${manifestBean.destination}">
                                    ${manifestBean.destination}
                                </span>
                            </td>
                            <td>${manifestBean.bookedVoyageNo}</td>
                            <td>
                                <span title="${manifestBean.shipperName}">
                                    ${fn:substring(manifestBean.shipperName,0,11)}
                                </span>
                            </td>
                            <td>
                                <span title="${manifestBean.forwarderName}">
                                    ${fn:substring(manifestBean.forwarderName,0,11)}
                                </span>
                            </td>
                            <td>
                                <span title="${manifestBean.consigneeName}">
                                    ${fn:substring(manifestBean.consigneeName,0,11)}
                                </span>
                            </td>
                            <td>
                                <span title="${manifestBean.terminalLocation}">
                                    ${fn:substring(manifestBean.terminalLocation,0,8)}
                                </span>
                            </td>
                            <td>
                                <c:if test="${not empty manifestBean.hotCodes}">
                                    <c:set var="hotcode" value="${fn:substringBefore(manifestBean.hotCodes,'~~~')}" />
                                    <c:set var="count" value="${fn:substringAfter(manifestBean.hotCodes,'~~~')}" />
                                    <c:set var="hotcount" value="${fn:substringBefore(count,'~~~')}" />
                                    <c:set var="hotCodeKey" value="${fn:substringAfter(count,'~~~')}" />
                                    <span id="hotCodeId" title="${hotcode}" ${hotcount > 3 ? 'style=color:#FF0000;':''}>
                                        ${hotCodeKey}
                                    </span>
                                </c:if>
                            </td>
                            <td>${manifestBean.colCharge}</td>
                            <c:set var="collectAmt" value="${collectAmt + manifestBean.colCharge}" />
                            <td>
                                <c:if test="${not empty manifestBean.ppdParties}">
                                    (${manifestBean.ppdParties})
                                </c:if>
                                ${manifestBean.ppdCharge}
                                <c:set var="prepaidAmt" value="${prepaidAmt + manifestBean.ppdCharge}" />
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>

                        <td style="color:green;font-size: 8px;font-family: Arial;">TOTAL</td>
                        <td style="color:green;font-size: 8px;font-family: Arial;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DRs: 
                            <b style="color:blue;font-size: 15px;">${totalDrCount}</b></td>

                        <td colspan="5"></td>
                        <td><b style="color:blue;font-size: 15px;">${piecesCount}</b></td>


                        <td><b style="color:blue;font-size: 15px;">${cuftCount}</b></td>
                        <td><b style="color:blue;font-size: 15px;">${poundsCount}</b></td>
                        <td colspan="10"></td>
                        <td><b style="color:blue;font-size: 15px;">${collectAmt ne 0.0 ? collectAmt : ''}</b></td>
                        <td><b style="color:red;font-size: 15px;">${prepaidAmt ne 0.0 ? prepaidAmt : ''}</b></td>
                    </tr>
                </tfoot>
            </c:if>
        </table>
    </div>
    <div style="overflow: auto;display: none" align="center" id="view2">
        <table border="0" id="manifestDr" width="100%"
               class="dataTable" style="border-collapse: collapse; border: 1px solid #dcdcdc;width:100%">
            <thead>
                <tr>
                    <th>File#</th>
                    <th>BL#</th>
                    <th>Type</th>
                    <th>BL Status</th>
                    <th>AR Invoice#</th>
                    <th>Doc</th>
                    <th>Pieces</th>
                    <th>Cuft</th>
                    <th>Pounds</th>
                    <th>Origin</th>
                    <th>Pol</th>
                    <th>Pod</th>
                    <th>Destn</th>
                    <th>FF Comm</th>
                    <th>FTF Fee</th>
                    <th>Incentive</th>
                    <th>PCB</th>
                    <th>BL Cft</th>
                    <th>BL Lbs</th>
                    <th>BL Cbm</th>
                    <th>BL Kgs</th>
                    <th>COL</th>
                    <th>PPD</th>
                </tr>
            </thead>
            <c:if test='${not empty pickedViewDrUnitList}'>
                <tbody>
                    <c:set var="prepaidAmt1" value="${0.00}"/>
                    <c:set var="collectAmt1" value="${0.00}"/>
                    <c:set var="totalDrCount1" value="${0}"/>
                    <c:set var="piecesCount1" value="${0}"/>
                    <c:set var="cuftCount1" value="${0.000}"/>
                    <c:set var="poundsCount1" value="${0.000}"/>
                    <c:set var="bLCftCount1" value="${0.000}"/>
                    <c:set var="bLLbsCount1" value="${0.000}"/>
                    <c:set var="bLCbmCount1" value="${0.000}"/>
                    <c:set var="bLKgsCount1" value="${0.000}"/>
                    <c:forEach items="${pickedViewDrUnitList}" var="manifestBean">
                        <c:choose>
                            <c:when test="${zebra eq 'odd'}">
                                <c:set var="zebra" value="even"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="zebra" value="odd"/>
                            </c:otherwise>
                        </c:choose>
                        <tr class="${zebra}">
                            <td>
                                <a style="cursor: pointer;" onclick="editViewBlScreen('${path}', '${manifestBean.fileId}', '${manifestBean.state}')">
                                    <font color="blue">
                                        <u><c:set var="totalDrCount1" value="${totalDrCount1+1}"/>
                                            ${manifestBean.fileNo}</u>
                                    </font>
                                </a>
                                <c:if test="${manifestBean.hazmat eq 'true'}">
                                    <img src="${path}/images/icons/danger.png" width="12" height="12" alt="HazardousIcon" title="Hazardous"/>
                                </c:if>
                            <td>${manifestBean.blNo}</td>
                            <td>${manifestBean.rateType}</td>
                            <td class="${manifestBean.className}">${manifestBean.statusLabel}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty manifestBean.arInvoiceNumber ? fn:substringAfter(manifestBean.arInvoiceNumber,'~~~') eq 'AR' : ''}">
                                        <span style="color:green;">${fn:substringBefore(manifestBean.arInvoiceNumber,'~~~')}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color:red;">${not empty manifestBean.arInvoiceNumber ? fn:substringBefore(manifestBean.arInvoiceNumber,'~~~') : ''}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${manifestBean.doc}</td>
                            <td>
                                <c:set var="piecesCount1" value="${piecesCount1 + manifestBean.totalPieceCount}" />
                                ${manifestBean.totalPieceCount}
                            </td>
                            <td>
                                <c:set var="cuftCount1" value="${cuftCount1 + manifestBean.totalVolumeImperial}" />
                                ${manifestBean.totalVolumeImperial}
                            </td>
                            <td>
                                <c:set var="poundsCount1" value="${poundsCount1 + manifestBean.totalWeightImperial}" />
                                ${manifestBean.totalWeightImperial}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${manifestBean.pooPickup eq 1 && not empty manifestBean.pickupCity}">
                                        <span  style="color:red;" title="Origin=${manifestBean.originName}/${manifestBean.originState}/${manifestBean.origin}
                                               <br>Door Origin=${fn:substring(manifestBean.pickupCity,fn:indexOf(manifestBean.pickupCity,'-')+1,fn:length(manifestBean.pickupCity))}">
                                            ${manifestBean.origin}
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span title="${manifestBean.originName}/${manifestBean.originState}/${manifestBean.origin}">
                                            ${manifestBean.origin}
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <span title="${manifestBean.polName}/${manifestBean.polState}/${manifestBean.pol}">
                                    ${manifestBean.pol}
                                </span>
                            </td>
                            <td>
                                <span title="${manifestBean.podName}/${manifestBean.podCountry}/${manifestBean.pod}">
                                    ${manifestBean.pod}
                                </span>
                            </td>
                            <td>
                                <span title="${manifestBean.destinationName}/${manifestBean.destinationCountry}/${manifestBean.destination}">
                                    ${manifestBean.destination}
                                </span>
                            </td>
                            <td>${manifestBean.ffComm}</td>
                            <td>${manifestBean.ftfFee}</td>
                            <td></td>
                            <td>${manifestBean.billingType}</td>
                            <td>
                                <c:set var="bLCftCount1" value="${bLCftCount1 + manifestBean.blCft}" />
                                ${manifestBean.blCft}
                            </td>
                            <td><c:set var="bLLbsCount1" value="${bLLbsCount1 + manifestBean.blLbs}" />
                                ${manifestBean.blLbs}
                            </td>
                            <td><c:set var="bLCbmCount1" value="${bLCbmCount1 + manifestBean.blCbm}" />
                                ${manifestBean.blCbm}
                            </td>
                            <td><c:set var="bLKgsCount1" value="${bLKgsCount1 + manifestBean.blKgs}" />
                                ${manifestBean.blKgs}
                            </td>
                            <td>${manifestBean.colCharge}</td>
                            <c:set var="collectAmt1" value="${collectAmt1 + manifestBean.colCharge}" />
                            <td>
                                <c:if test="${not empty manifestBean.ppdParties}">
                                    (${manifestBean.ppdParties})
                                </c:if>
                                ${manifestBean.ppdCharge}
                                <c:set var="prepaidAmt1" value="${prepaidAmt1 + manifestBean.ppdCharge}" />
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>

                        <td style="color:green;font-size: 8px;font-family: Arial;">TOTAL</td>
                        <td style="color:green;font-size: 8px;font-family: Arial;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DRs: 
                            <b style="color:blue;font-size: 15px;">${totalDrCount1}</b></td>
                        <td colspan="4"></td>
                        <td><b style="color:blue;font-size: 15px;">${piecesCount1}</b></td>
                        <td><b style="color:blue;font-size: 15px;">${cuftCount1}</b></td>
                        <td><b style="color:blue;font-size: 15px;">${poundsCount1}</b></td>
                        <td colspan="8"></td>
                        <td><b style="color:blue;font-size: 15px;">${bLCftCount1}</b></td>
                        <td><b style="color:blue;font-size: 15px;">${bLLbsCount1}</b></td>
                        <td><b style="color:blue;font-size: 15px;">${bLCbmCount1}</b></td>
                        <td><b style="color:blue;font-size: 15px;">${bLKgsCount1}</b></td>
                        <td><b style="color:blue;font-size: 15px;">${collectAmt1 ne 0.0 ? collectAmt1 : ''}</b></td>
                        <td><b style="color:red;font-size: 15px;">${prepaidAmt1 ne 0.0 ? prepaidAmt1 : ''}</b></td>
                    </tr>
                </tfoot>
            </c:if>
        </table>
    </div>
</cong:form>
<script type="text/javascript">
    function editViewBlScreen(path, fileId) {
        var headerId = parent.$('#headerId').val();
        var unitSsId = $('#unitssId').val();
        var filter = parent.$("#filterByChanges").val();
        var fromScreen = "UnitViewDrScreenToBooking";
        var toScreen = "EXP VOYAGE";
        window.parent.parent.goToBookingFromVoyage(path, fileId, filter, headerId, '', unitSsId, fromScreen, toScreen, '');
    }
    var view1 = true;
    function toggleViewDr() {
        if (view1) {
            $('#view1').hide();
            $('#view2').show();
            view1 = false;
        } else {
            $('#view2').hide();
            $('#view1').show();
            view1 = true;
        }
    }
    function exportToExcel() {
        $("#methodName").val("exportToExcel");
        $("#lclAddVoyageForm").submit();
    }
</script>