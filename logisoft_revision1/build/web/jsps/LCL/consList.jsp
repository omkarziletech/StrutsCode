<%@include file="init.jsp"%>
<%@taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<body style="background:#ffffff">
    <cong:div style="width:100%; float:left;">
        <cong:form name="lclConsolidateForm" id="lclConsolidateForm" action="/lclConsolidate.do">
            <cong:div styleClass="floatLeft tableHeadingNew" style="width:100%">
                Consolidate for File No:<span class="fileNo" style="margin-right: 100px">${fileNumber}</span>
            </cong:div>
            <cong:table width="100%" border="1" style="border-collapse: collapse; border: 1px solid #dcdcdc">
                <cong:tr styleClass="tableHeading2">
                    <cong:td>File No</cong:td>
                    <cong:td>Origin</cong:td>
                    <cong:td>Client</cong:td>
                    <cong:td>Status</cong:td>
                    <cong:td>Sail Date</cong:td>
                    <cong:td>Piece</cong:td>
                    <cong:td>Weight</cong:td>
                    <cong:td>Measure</cong:td>
                </cong:tr>
                <c:set var="count" value="0"/>
                <c:forEach items="${consDescList}" var="lclConsolidate">
                    <c:if test="${lclConsolidate.lclFileNumberB.state!='Q'}">
                        <c:choose>
                            <c:when test="${zebra=='odd'}">
                                <c:set var="zebra" value="even"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="zebra" value="odd"/>
                            </c:otherwise>
                        </c:choose>
                        <cong:tr styleClass="${zebra}">
                            <fmt:formatDate pattern="dd-MMM-yyyy" var="polEtd" value="${lclConsolidate.lclFileNumberB.lclBooking.masterSchedule.polEtd}"/>
                            <cong:td>
                                <span style="cursor: pointer" onclick="submitForm('${path}','${file.id}')">
                                    <u>${lclConsolidate.lclFileNumberB.fileNumber}</u>
                                </span>
                            </cong:td>
                            <cong:td>${lclConsolidate.lclFileNumberB.lclBooking.portOfOrigin.unLocationCode}/${lclConsolidate.lclFileNumberB.lclBooking.portOfOrigin.stateId.codedesc}</cong:td>
                            <cong:td>${lclConsolidate.lclFileNumberB.lclBooking.clientContact.companyName}</cong:td>
                            <cong:td>
                                <c:choose>
                                    <c:when test="${lclConsolidate.lclFileNumberB.status=='PR' || lclConsolidate.lclFileNumberB.status=='R' ||
                                                    lclConsolidate.lclFileNumberB.status=='WV' || lclConsolidate.lclFileNumberB.status=='WU' ||
                                                    lclConsolidate.lclFileNumberB.status=='WNO'}">
                                        <c:if test="${lclConsolidate.lclFileNumberB.status=='PR'}">
                                            <c:out value="WAREHOUSE (Pre Release)"/>
                                        </c:if>
                                        <c:if test="${lclConsolidate.lclFileNumberB.status=='R'}">
                                            <c:out value="WAREHOUSE (Release)"/>
                                        </c:if>
                                        <c:if test="${lclConsolidate.lclFileNumberB.status=='WV'}">
                                            <c:out value="WAREHOUSE (Verified)"/>
                                        </c:if>
                                        <c:if test="${lclConsolidate.lclFileNumberB.status=='WU'}">
                                            <c:out value="WAREHOUSE (Un-verified)"/>
                                        </c:if>
                                        <c:if test="${lclConsolidate.lclFileNumberB.status=='WNO'}">
                                            <c:out value="WAREHOUSE (Refused)"/>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${lclConsolidate.lclFileNumberB.state=='B'}">
                                            <c:out value="Booking"/>
                                        </c:if>
                                        <c:if test="${lclConsolidate.lclFileNumberB.state=='Q'}">
                                            <c:out value="Quote"/>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </cong:td>
                            <cong:td>${polEtd}</cong:td>
                            <c:choose>
                                <c:when test="${not empty lclConsolidate.lclFileNumberB.lclBookingPieceList[fn:length(lclConsolidate.lclFileNumberB.lclBookingPieceList)-1].actualPieceCount}">
                                    <cong:td>${lclConsolidate.lclFileNumberB.lclBookingPieceList[fn:length(lclConsolidate.lclFileNumberB.lclBookingPieceList)-1].actualPieceCount}</cong:td>
                                </c:when>
                                <c:otherwise>
                                    <cong:td>${lclConsolidate.lclFileNumberB.lclBookingPieceList[fn:length(lclConsolidate.lclFileNumberB.lclBookingPieceList)-1].bookedPieceCount}</cong:td>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${not empty lclConsolidate.lclFileNumberB.lclBookingPieceList[fn:length(lclConsolidate.lclFileNumberB.lclBookingPieceList)-1].actualWeightImperial}">
                                    <cong:td>${lclConsolidate.lclFileNumberB.lclBookingPieceList[fn:length(lclConsolidate.lclFileNumberB.lclBookingPieceList)-1].actualWeightImperial}</cong:td>
                                </c:when>
                                <c:otherwise>
                                    <cong:td>${lclConsolidate.lclFileNumberB.lclBookingPieceList[fn:length(lclConsolidate.lclFileNumberB.lclBookingPieceList)-1].bookedWeightImperial}</cong:td>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${not empty lclConsolidate.lclFileNumberB.lclBookingPieceList[fn:length(lclConsolidate.lclFileNumberB.lclBookingPieceList)-1].actualVolumeImperial}">
                                    <cong:td>${lclConsolidate.lclFileNumberB.lclBookingPieceList[fn:length(lclConsolidate.lclFileNumberB.lclBookingPieceList)-1].actualVolumeImperial}</cong:td>
                                </c:when>
                                <c:otherwise>
                                    <cong:td>${lclConsolidate.lclFileNumberB.lclBookingPieceList[fn:length(lclConsolidate.lclFileNumberB.lclBookingPieceList)-1].bookedVolumeImperial}</cong:td>
                                </c:otherwise>
                            </c:choose>
                        </cong:tr>
                        <c:set var="count" value="${count+1}"/>
                    </c:if>
                </c:forEach>
            </cong:table>
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="fileNumberA" id="fileNumberA" value="${lclConsolidateForm.fileNumberA}"/>
        </cong:form>
    </cong:div>
    <script type="text/javascript">
        function congAlert(txt){
            $.prompt(txt);
        }
        function submitForm(path,fleNumber){
            $("#fileNumberA").val(fleNumber);
            $("#methodName").val("consolidatedFiles");
            $("#lclConsolidateForm").submit();
        }
        function submitAjaxForm(methodName,formName,selector){
            showProgressBar();
            $("#methodName").val(methodName);
            var params = $(formName).serialize();
            var fileNumberId=$('#fileNumberId').val();
            params+="&fileNumberId="+fileNumberId;
            $.post($(formName).attr("action"),params,
            function(data) {
                $(selector).html(data);
                $(selector,window.parent.document).html(data);
                parent.$.fn.colorbox.close();
                hideProgressBar();
            });
        }
    </script>
</body>
