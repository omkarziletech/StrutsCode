<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<cong:form  id="lclClientDetailsForm"  name="lclClientDetailsForm" action="lclClientDetails.do" >
    <cong:table width="100%" style="margin:5px 0; float:left">
        <cong:tr>
            <cong:td styleClass="tableHeadingNew">
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
                    <input type="button" class="button-style1" value="Replicate" onclick="submitFormQ('${path}')"/>
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
                    <th>Status</th>
                    <th>Origin CFS</th>
                    <th>POL</th>
                    <th>POD</th>
                    <th>Destination</th>
                    <th>Sail Date</th>
                    <th>Commodity</th>
                    <th>Piece</th>
                    <th width='7%'>Weight</th>
                    <th width='7%'>Measure</th>
                    <th>Haz</th>
                </tr>
            </thead>
        </tbody>
        <c:forEach items="${quoteList}" var="quote">
            <c:choose>
                <c:when test="${zebra=='odd'}">
                    <c:set var="zebra" value="even"/>
                </c:when>
                <c:otherwise>
                    <c:set var="zebra" value="odd"/>
                </c:otherwise>
            </c:choose>
            <tr class="${zebra}">
                <fmt:formatDate pattern="dd-MMM-yyyy" var="polEtd" value="${quote.masterSchedule.polEtd}"/>
                <td>
                    <input type="radio" name="fileNumberId" id="quoteRadio" value="${quote.fileNumberId}" >
                </td>
                <td>${quote.lclFileNumber.fileNumber}</td>
                <td>
                    ${quote.lclFileNumber.state eq 'Q' ? 'Quote':'Booking'}
                </td>
                <td>${quote.portOfOrigin.unLocationName}/${quote.portOfOrigin.stateId.code}(${quote.portOfOrigin.unLocationCode})</td>
                <td>${quote.portOfLoading.unLocationName}</td>
                <td>${quote.portOfDestination.unLocationName}</td>
                <td>${quote.finalDestination.unLocationName}/${quote.finalDestination.stateId.code}(${quote.finalDestination.unLocationCode})</td>
                <td><c:out value="${polEtd}"></c:out></td>
                <c:choose>
                    <c:when test="${not empty quote.lclFileNumber && not empty quote.lclFileNumber.lclQuotePieceList}">
                        <c:set var="exitLoop" value="true"/>
                        <c:forEach items="${quote.lclFileNumber.lclQuotePieceList}" var="lclQuotePieceList">
                            <c:if test="${exitLoop=='true'}">
                                <td>${lclQuotePieceList.commodityType.descEn}(${lclQuotePieceList.commodityType.code})</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty lclQuotePieceList.actualPieceCount}">
                                            ${lclQuotePieceList.actualPieceCount}
                                        </c:when>
                                        <c:otherwise>${lclQuotePieceList.bookedPieceCount}</c:otherwise>
                                    </c:choose>
                                    <c:if test="${1 <  lclQuotePieceList.actualPieceCount  && 1 < lclQuotePieceList.bookedPieceCount}">
                                        <c:if test="${(lclQuotePieceList.actualPieceCount-lclQuotePieceList.bookedPieceCount)*100 ne 0}">
                                            <c:choose>
                                                <c:when test="${(lclQuotePieceList.actualPieceCount-lclQuotePieceList.bookedPieceCount)*100/lclQuotePieceList.bookedPieceCount >=10}">
                                                    <span style="color: red;font-size: 25px" title='${lclQuotePieceList.bookedPieceCount}'>*</span>
                                                </c:when>
                                                <c:when test="${(lclQuotePieceList.actualPieceCount-lclQuotePieceList.bookedPieceCount)*100/lclQuotePieceList.bookedPieceCount < 10}">
                                                    <span style="color: green;font-size: 25px" title='${lclQuotePieceList.bookedPieceCount}'>*</span>
                                                </c:when><c:otherwise></c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </c:if>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty lclQuotePieceList.actualWeightImperial}">
                                            ${lclQuotePieceList.actualWeightImperial}
                                        </c:when>
                                        <c:otherwise>${lclQuotePieceList.bookedWeightImperial}</c:otherwise>
                                    </c:choose>
                                    <c:if test="${1 <  lclQuotePieceList.actualWeightImperial && 1 < lclQuotePieceList.bookedWeightImperial}">
                                        <c:if test="${(lclQuotePieceList.actualWeightImperial-lclQuotePieceList.bookedWeightImperial)*100 ne 0}">
                                            <c:choose>
                                                <c:when test="${(lclQuotePieceList.actualWeightImperial-lclQuotePieceList.bookedWeightImperial)*100/lclQuotePieceList.bookedWeightImperial >=10}">
                                                    <span style="color: red;font-size: 25px" title='${lclQuotePieceList.bookedWeightImperial}'>*</span>
                                                </c:when>
                                                <c:when test="${(lclQuotePieceList.actualWeightImperial-lclQuotePieceList.bookedWeightImperial)*100/lclQuotePieceList.bookedWeightImperial < 10}">
                                                    <span style="color: green;font-size: 25px" title='${lclQuotePieceList.bookedWeightImperial}'>*</span>
                                                </c:when><c:otherwise></c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </c:if>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty lclQuotePieceList.actualVolumeImperial}">
                                            ${lclQuotePieceList.actualVolumeImperial}
                                        </c:when>
                                        <c:otherwise>${lclQuotePieceList.bookedVolumeImperial}</c:otherwise>
                                    </c:choose>
                                    <c:if test="${1 < lclQuotePieceList.actualVolumeImperial && 1 < lclQuotePieceList.bookedVolumeImperial}">
                                        <c:if test="${(lclQuotePieceList.actualVolumeImperial-lclQuotePieceList.bookedVolumeImperial)*100 ne 0}">
                                            <c:choose>
                                                <c:when test="${(lclQuotePieceList.actualVolumeImperial-lclQuotePieceList.bookedVolumeImperial)*100/lclQuotePieceList.bookedVolumeImperial >=10}">
                                                    <span style="color: red;font-size: 25px" title='${lclQuotePieceList.bookedVolumeImperial}'>*</span>
                                                </c:when>
                                                <c:when test="${(lclQuotePieceList.actualVolumeImperial-lclQuotePieceList.bookedVolumeImperial)*100/lclQuotePieceList.bookedVolumeImperial < 10}">
                                                    <span style="color: green;font-size: 25px" title='${lclQuotePieceList.bookedVolumeImperial}'>*</span>
                                                </c:when><c:otherwise></c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </c:if>
                                </td>
                                <td>
                                    ${lclQuotePieceList.hazmat ? 'Y':'N'}
                                </td>
                            </c:if>
                            <c:set var="exitLoop" value="false"/>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <cong:td></cong:td>
                        <cong:td></cong:td>
                        <cong:td></cong:td>
                        <cong:td></cong:td>
                        <cong:td></cong:td>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </tr>
        </c:forEach>
    </table>
</cong:div>


</cong:form>

<script>
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
    function submitFormQ(path)
    {
        //var fileNumberId = document.lclClientDetailsForm.fileNumberId;
        var fileNumberId = $('input:radio[name=fileNumberId]:checked').val();
        if (fileNumberId != undefined)
        {
            parent.location.href = path + "/lclQuote.do?methodName=replicateQuote&fileNumberId=" + fileNumberId;
        }
        else
        {
            clientAlert("Please select one quote to replicate ");
        }
        //$("#methodName").val(methodName);
        //$("#lclClientDetailsForm").submit();
    }
    function closeClient(id) {
        $("#id").val(id);
    }
    function clientAlert(txt) {
        $.prompt(txt);
    }
</script>
