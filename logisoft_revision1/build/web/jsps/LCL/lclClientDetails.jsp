<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<cong:form  id="lclClientDetailsForm"  name="lclClientDetailsForm" action="lclClientDetails.do" >
    <cong:table width="100%" style="margin:5px 0; float:left">
        <cong:tr>
            <cong:td styleClass="tableHeadingNew" colspan="5">
                <cong:div styleClass="floatLeft">Client Name: <span class="fileNo">${lclClientDetailsForm.accountName}</span>
                </cong:div>
            </cong:td>
            <cong:td styleClass="tableHeadingNew">
                <cong:div styleClass="floatLeft">Address: <span class="fileNo">${lclClientDetailsForm.address}</span>
                </cong:div>
            </cong:td>
            <cong:td styleClass="tableHeadingNew">
                <cong:div styleClass="floatLeft">City: <span class="fileNo">${lclClientDetailsForm.city}</span>
                </cong:div>
            </cong:td>
            <cong:td styleClass="tableHeadingNew">
                <cong:div styleClass="floatLeft">State: <span class="fileNo">${lclClientDetailsForm.state}</span>
                </cong:div>
            </cong:td>
            <cong:td styleClass="tableHeadingNew">
                <cong:div styleClass="floatLeft">Zip: <span class="fileNo">${lclClientDetailsForm.zip}</span>
                </cong:div>
            </cong:td>
        </cong:tr>
    </cong:table>
    <cong:div id="clientDetails" >
        <cong:table>
            <cong:tr>
                <cong:td></cong:td>
                <cong:td>&nbsp;</cong:td>
                <cong:td colspan="4" align="center">
                    <input type="button" class="button-style1" value="Replicate" onclick="submitFormB('${path}')"/>
                </cong:td>
            </cong:tr>
            <cong:hidden name="methodName" id="methodName"/>
        </cong:table>
    </cong:div>
    <cong:div id="saveTable">

        <table border="0" width="100%" class="dataTable">
            <thead>
                <tr>
                    <th>&nbsp;</th>
                    <th>File Number</th>
                    <th>Origin CFS</th>
                    <th>POL</th>
                    <th>POD</th>
                    <th>Destination</th>
                    <th>Sail Date</th>
                    <th>Commodity</th>
                    <th>Piece</th>
                    <th width='7%'>Weight</th>
                    <th width='7%'>Measure</th>
                    <th>Haz(Booked)</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${bookingList}" var="booking">
                    <c:choose>
                        <c:when test="${zebra=='odd'}">
                            <c:set var="zebra" value="even"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="zebra" value="odd"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${zebra}">
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="polEtd" value="${booking.masterSchedule.polEtd}"/>
                        <td>
                            <input type="radio" name="fileNumberId" id="bookingRadio" value="${booking.fileNumberId}" >
                        </td>
                        <td>${booking.lclFileNumber.fileNumber}</td>
                        <td>${booking.portOfOrigin.unLocationName}/${booking.portOfOrigin.stateId.code}(${booking.portOfOrigin.unLocationCode})</td>
                        <td>${booking.portOfLoading.unLocationName}</td>
                        <td>${booking.portOfDestination.unLocationName}</td>
                        <td>${booking.finalDestination.unLocationName}/${booking.finalDestination.stateId.code}(${booking.finalDestination.unLocationCode})</td>
                        <td><c:out value="${polEtd}"></c:out></td>
                        <c:choose>
                            <c:when test="${not empty booking.lclFileNumber && not empty booking.lclFileNumber.lclBookingPieceList}">
                                <c:set var="exitLoop" value="true"/>
                                <c:forEach items="${booking.lclFileNumber.lclBookingPieceList}" var="lclBookingPieceList">
                                    <c:if test="${exitLoop=='true'}">
                                        <td>${lclBookingPieceList.commodityType.descEn}(${lclBookingPieceList.commodityType.code})</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty lclBookingPieceList.actualPieceCount && 1< lclBookingPieceList.actualPieceCount}">
                                                    ${lclBookingPieceList.actualPieceCount}
                                                </c:when>
                                                <c:otherwise>${lclBookingPieceList.bookedPieceCount}</c:otherwise>
                                            </c:choose>
                                            <c:if test="${1 <  lclBookingPieceList.actualPieceCount  && 1 < lclBookingPieceList.bookedPieceCount}">
                                                <c:if test="${(lclBookingPieceList.actualPieceCount-lclBookingPieceList.bookedPieceCount)*100 ne 0}">
                                                    <c:choose>
                                                        <c:when test="${(lclBookingPieceList.actualPieceCount-lclBookingPieceList.bookedPieceCount)*100/lclBookingPieceList.bookedPieceCount >=10}">
                                                            <span style="color: red;font-size: 25px" title='${lclBookingPieceList.bookedPieceCount}'>*</span>
                                                        </c:when>
                                                        <c:when test="${(lclBookingPieceList.actualPieceCount-lclBookingPieceList.bookedPieceCount)*100/lclBookingPieceList.bookedPieceCount < 10}">
                                                            <span style="color: green;font-size: 25px" title='${lclBookingPieceList.bookedPieceCount}'>*</span>
                                                        </c:when><c:otherwise></c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty lclBookingPieceList.actualWeightImperial}">
                                                    ${lclBookingPieceList.actualWeightImperial}
                                                </c:when>
                                                <c:otherwise>${lclBookingPieceList.bookedWeightImperial}</c:otherwise>
                                            </c:choose>
                                            <c:if test="${1 <  lclBookingPieceList.actualWeightImperial && 1 < lclBookingPieceList.bookedWeightImperial}">
                                                <c:if test="${(lclBookingPieceList.actualWeightImperial-lclBookingPieceList.bookedWeightImperial)*100 ne 0}">
                                                    <c:choose>
                                                        <c:when test="${(lclBookingPieceList.actualWeightImperial-lclBookingPieceList.bookedWeightImperial)*100/lclBookingPieceList.bookedWeightImperial >=10}">
                                                            <span style="color: red;font-size: 25px" title='${lclBookingPieceList.bookedWeightImperial}'>*</span>
                                                        </c:when>
                                                        <c:when test="${(lclBookingPieceList.actualWeightImperial-lclBookingPieceList.bookedWeightImperial)*100/lclBookingPieceList.bookedWeightImperial < 10}">
                                                            <span style="color: green;font-size: 25px" title='${lclBookingPieceList.bookedWeightImperial}'>*</span>
                                                        </c:when><c:otherwise></c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty lclBookingPieceList.actualVolumeImperial}">
                                                    ${lclBookingPieceList.actualVolumeImperial}
                                                </c:when>
                                                <c:otherwise>${lclBookingPieceList.bookedVolumeImperial}</c:otherwise>
                                            </c:choose>
                                             <c:if test="${1 < lclBookingPieceList.actualVolumeImperial && 1 < lclBookingPieceList.bookedVolumeImperial}">
                                                <c:if test="${(lclBookingPieceList.actualVolumeImperial-lclBookingPieceList.bookedVolumeImperial)*100 ne 0}">
                                                    <c:choose>
                                                        <c:when test="${(lclBookingPieceList.actualVolumeImperial-lclBookingPieceList.bookedVolumeImperial)*100/lclBookingPieceList.bookedVolumeImperial >=10}">
                                                            <span style="color: red;font-size: 25px" title='${lclBookingPieceList.bookedVolumeImperial}'>*</span>
                                                        </c:when>
                                                        <c:when test="${(lclBookingPieceList.actualVolumeImperial-lclBookingPieceList.bookedVolumeImperial)*100/lclBookingPieceList.bookedVolumeImperial < 10}">
                                                            <span style="color: green;font-size: 25px" title='${lclBookingPieceList.bookedVolumeImperial}'>*</span>
                                                        </c:when><c:otherwise></c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <td>
                                           ${lclBookingPieceList.hazmat ? 'Y':'N'}
                                        </td>
                                    </c:if>
                                    <c:set var="exitLoop" value="false"/>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </cong:div>


</cong:form>

<script type="text/javascript">
    $('#addDetail').click(function () {
        $("#clientDetails").show();
        $("#clientTable").hide();
        $("#saveTable").hide();
    });
    $('.cancelBut').click(function () {
        $("#clientDetails").hide();
        $("#clientTable").show();
        $("#saveTable").show();
    });
    function closePopup()
    {
        parent.$.fn.colorbox.close();
    }
    function submitFormB(path)
    {
        var fileNumberId = $('input:radio[name=fileNumberId]:checked').val();
        if (fileNumberId !== undefined)
        {
            parent.location.href = path + "/lclBooking.do?methodName=replicateBooking&fileNumberId=" + fileNumberId;
        }
        else
        {
            $.prompt("Please select one booking to replicate ");
        }
    }
    function closeClient(id) {
        $("#id").val(id);
    }
</script>
