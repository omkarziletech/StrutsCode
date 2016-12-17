<%@include file="init.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>

<html>
    <style type="text/css">
        .text-border{
            border: none;
        }
        .measure-met{
            display: none;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#actualLength').focus().val();
            var fileStatus = parent.$('#status').val();
            var fileNumberId = $("#fileNumberId").val();

            var bookedPieceCount = parent.$("#bookedPieceCount").val();
            var packageType = parent.$("#packageType").val();
            var commodityType = parent.$("#commodityType").val();
            var commodityNo = parent.$("#commodityNo").val();
            // alert("readonly "+parent.$("#bookedPieceCount").is('[readonly]'));
            if (bookedPieceCount !== "" && packageType !== "" && parent.$("#bookedPieceCount").is('[readonly]')) {
                // alert(bookedPieceCount + " " + packageType + "," + commodityType + "(" + commodityNo + ")");
                var message = "Booked values: " + bookedPieceCount + " " + packageType.toUpperCase() + "," + commodityType + " (" + commodityNo + ")";
                document.getElementById("bookedValues").innerHTML = message;
            }

            $('#saveDetail').click(function () {
                var error = true;
                var moduleName = parent.parent.$('#moduleName').val();
                var role = parent.parent.$("#actualFieldsChangeAfterRelease").val();
                if ("Exports" === moduleName && role === 'false' && fileStatus !== 'WV' && (isCheckedReleaseStatus(fileNumberId) || fileStatus === 'L' || fileStatus === 'M')) {
                    $.prompt('Cannot update actual fields after release,Check in roleduty to update these fields!');
                    error = false;
                    return false;
                }
                $(".mandatory").each(function () {
                    if ($(this).val().length == 0) {
                        error = false;
                        $(this).css("border-color", "red");
                        $(this).focus();
                        $.prompt('This field is required');
                        return false;
                    }
                });

                if (error) {
                    parent.parent.$("#isMeasureImpChanged").val("true");
                    submitForm('addDetails', '#commodityDetailsForm');
                }
            });
            //  parent.showDimsDetails();
            // parent.setBookedCommodity();
            if ($("#actualUomI").is(":checked")) {
                var rowslen = document.getElementById("dimsTable").rows.length;
                if (rowslen > 1) {
                    //$('#actualUomM').attr('disabled', true);
                    changeUom();
                }
            }
            if ($("#actualUomM").is(":checked")) {
                var rowlen = document.getElementById("dimTable").rows.length;
                if (rowlen > 1) {
                    changeUom();
                }
            }
            var uom = $('#uom').val();
            if (uom == 'M') {
                var rowlen = document.getElementById("dimTable").rows.length;
                if (rowlen > 1) {
                    $('.measure-imp').hide();
                    $('.measure-met').show();
                    $('#impDetailList').hide();
                    $('#metDetailList').show();
                    $('#actualM').show();
                    $('#actualI').hide();
                }
            }
            if (uom == 'I') {
                $('.measure-imp').show();
                $('.measure-met').hide();
                $('#impDetailList').show();
                $('#metDetailList').hide();
                $('#actualM').hide();
                $('#actualI').show();
            }

        });
    </script>
    <body>
        <cong:form name="commodityDetailsForm" id="commodityDetailsForm" action="/commodityDetails.do">
            <input type="hidden" name="bookingPieceId" id="bookingPieceId" value="${commodityDetailsForm.bookingPieceId}"/>
            <input type="hidden" name="packageTypeId" id="packageTypeId" value="${packageTypeId}"/>
            <input type="hidden" name="packageType" id="packageType" value="${packageType}"/>
            <cong:hidden name="fileNumber" id="fileNumber" value="${commodityDetailsForm.fileNumber}"/>
            <cong:hidden name="fileNumberId" id="fileNumberId" value="${commodityDetailsForm.fileNumberId}"/>
            <cong:hidden name="methodName" id="methodName"/>
            <input type="hidden" name="id" id="id" value="${id}"/>
            <cong:hidden name="dimFlag" id="dimFlag" value="${dimFlag}"/>
            <table width="97%">
                <thead>
                    <tr class="tableHeadingNew">
                        <td>Commodity Details <span style="color:red">${commodityDetailsForm.fileNumber}</span></td>
                        <td align="right">
                            UOM
                            <cong:radio name="actualUom" value="M" id="actualUomM" onclick="showHideMeasureLabel(this);showValue();" container="NULL"/>M
                            <cong:radio name="actualUom" value="I" id="actualUomI" onclick="showHideMeasureLabel(this);showValue();" container="NULL"/>I
                            &nbsp;&nbsp;
                            <cong:hidden name="uom" id="uom" value="${uom}"/>
                        </td>
                    </tr>
                </thead>
            </table>
            <input type="hidden" name="duptotalMeasureImp" id="duptotalMeasureImp" value="${commodityDetailsForm.duptotalMeasureImp}"/>
            <input type="hidden" name="duptotalMeasureMet" id="duptotalMeasureMet" value="${commodityDetailsForm.duptotalMeasureMet}"/>
            <input type="hidden" name="duptotalWeightImp" id="duptotalWeightImp" value="${commodityDetailsForm.duptotalWeightImp}"/>
            <input type="hidden" name="duptotalWeightMet" id="duptotalWeightMet" value="${commodityDetailsForm.duptotalWeightMet}"/>
            <input type="hidden" name="commodityNo" id="commodityNo" value="${commodityNo}"/>
            <b><span style="color:red;" id="bookedValues"></span></b>
                <cong:table width="100%">
                    <cong:tr id="actualI" styleClass="textBoldforlcl">
                        <cong:td width="12%">Length(IN)</cong:td>
                    <cong:td width="12%">Width(IN)</cong:td>
                    <cong:td width="12%">Height(IN)</cong:td>
                    <cong:td width="12%">Pieces</cong:td>
                    <cong:td width="12%">Weight/PC(LBS)</cong:td>
                    <cong:td width="12%">WarehouseLine</cong:td>
                    <cong:td width="8%"></cong:td>
                    <cong:td width="8%" id="hideIdImports1"></cong:td>
                </cong:tr>
                <cong:tr id="actualM" style="display:none" styleClass="textBoldforlcl">
                    <cong:td width="14%">Length(CMS)</cong:td>
                    <cong:td width="14%">Width(CMS)</cong:td>
                    <cong:td width="14%">Height(CMS)</cong:td>
                    <cong:td width="14%">Pieces</cong:td>
                    <cong:td width="14%">Weight/PC(KGS)</cong:td>
                    <cong:td width="14%">WarehouseLine</cong:td>
                    <cong:td width="8%"></cong:td>
                    <cong:td width="8%" id="hideIdImports2"></cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td><cong:text styleClass="textsmall mandatory weight" name="actualLength" id="actualLength" value="${bookingPieceDetail.actualLength}" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall mandatory weight" name="actualWidth" id="actualWidth" value="${bookingPieceDetail.actualWidth}" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall mandatory weight" name="actualHeight" id="actualHeight" value="${bookingPieceDetail.actualHeight}" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall mandatory" name="pieceCount" id="pieceCount" value="${bookingPieceDetail.pieceCount}" container="NULL" onkeyup="checkForNumber(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall weight" name="actualWeight" id="actualWeight" value="${bookingPieceDetail.actualWeight}" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall" style="text-transform: uppercase" name="warehouse" id="warehouse" value="${bookingPieceDetail.stowedLocation}" container="NULL"/></cong:td>
                    <cong:td><input type="button"  value="Add" class="button-style1" id="saveDetail"/>  </cong:td>
                    <cong:td id="hideIdImports3"><input type="button"  value="Multiple" class="button-style1 multipleOpt" id="multipleDetail" onclick="MultipleDims('${path}');"/>  </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="8">
                            <cong:table width="100%" border="0">
                                <cong:tr styleClass="textBoldforlcl">
                                    <cong:td styleClass="measure-imp" width="15%">Total Measure(I):</cong:td>
                                    <cong:td styleClass="measure-imp" colspan="1" width="22%">
                                        <cong:text name="totalMeasureImp" styleClass="textsmall text-border" readOnly="true" container="NULL"  id="totalMeasureImp" value="${commodityDetailsForm.totalMeasureImp} "/>(CFT)
                                    </cong:td>
                                    <cong:td styleClass="measure-met" width="16%">Total Measure(M):</cong:td>
                                    <cong:td styleClass="measure-met" colspan="1" width="22%">
                                        <cong:text name="totalMeasureMet" styleClass="textsmall text-border" readOnly="true" container="NULL"  id="totalMeasureMet" value="${commodityDetailsForm.totalMeasureMet}"/>(CBM)
                                        <!--                                    Measure(M)=Measure(I)/35.3146-->
                                    </cong:td>
                                    <cong:td styleClass="measure-imp" width="14%">Total Weight(I):</cong:td>
                                    <cong:td styleClass="measure-imp" width="18%">
                                        <cong:text name="totalWeightImp" styleClass="textsmall"  container="NULL"  id="totalWeightImp" value="${commodityDetailsForm.totalWeightImp} "/>(LBS)
                                    </cong:td>
                                    <cong:td styleClass="measure-met" width="38%">Total Weight(M):
                                        <cong:text name="totalWeightMet"  styleClass="textsmall"  container="NULL"  id="totalWeightMet" value="${commodityDetailsForm.totalWeightMet} "/>(KGS)
                                        <!--                                    Weight(M)=Measure(I)/2.2046-->
                                    </cong:td>
                                    <cong:td width="10%">Total Pieces:</cong:td>
                                    <cong:td>
                                        <cong:text name="totalPieces" styleClass="textsmall text-border" readOnly="true" container="NULL"  id="totalPieces" value="${commodityDetailsForm.totalPieces}"/>
                                    </cong:td>

                                    <cong:td width="6%">
                                        <c:choose>
                                            <c:when test="${dimFlag=='booked'}">
                                                <cong:div id="bookedAction">
                                                    <input type="button"  class="button-style1" value="Save" onclick="submitAjaxForm('${path}', 'updateBookedComm')"/>
                                                </cong:div>
                                            </c:when>
                                            <c:when test="${dimFlag=='actual'}">
                                                <cong:div id="actualAction">
                                                    <input type="button"  class="button-style1" value="Save" onclick="submitAjaxForm('${path}', 'updateActualComm')"/>
                                                </cong:div>
                                            </c:when>
                                        </c:choose>
                                    </cong:td>
                                    <cong:td>
                                        <input type="hidden" name="delModuleName" id="delModuleName" />
                                        <c:forEach items="${detailListMet}"  varStatus="row">
                                            <c:choose>
                                                <c:when test="${row.count == 1}">
                                                    <input type="button" class="button-style1" value="Del" id="delButton" onclick="deleteAll();"/>
                                                </c:when>
                                                <c:otherwise>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </cong:td>
                                </cong:tr>
                            </cong:table>
                        </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="8">
                            <cong:div id="impDetailList" style="width:100%">
                                <table width="100%" border="0" id="dimsTable"  class="dataTable">
                                    <thead>
                                        <tr>
                                            <th>Length(IN)</th>
                                            <th>Width(IN)</th>
                                            <th>Height(IN)</th>
                                            <th>Weight Per Piece(LBS)</th>
                                            <th>Piece</th>
                                            <th>Warehouse Line</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:set var="count" value="0"/>
                                        <c:forEach items="${detailListImp}" var="details">
                                        <input type="hidden" id="deleteAllBooking" value="${details.lclBookingPiece.id}">
                                        <c:set var="count" value="${count+1}"/>
                                        <c:choose>
                                            <c:when test="${rowStyle eq 'oddStyle'}">
                                                <c:set var="rowStyle" value="evenStyle"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="rowStyle" value="oddStyle"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <tr class="${rowStyle}">
                                            <td>${details.actualLength}</td>
                                            <td>${details.actualWidth}</td>
                                            <td>${details.actualHeight}</td>
                                            <td>${details.actualWeight}</td>
                                            <td>${details.pieceCount}</td>
                                            <td style="text-transform: uppercase">${details.stowedLocation}</td>
                                            <td>
                                                <cong:img src="${path}/images/edit.png" styleClass="addCommodity" style="cursor:pointer" width="16" height="16" alt="edit"
                                                          onclick="editDetails('${path}','${count-1}','${details.lclBookingPiece.id}','${dimFlag}','${details.actualUom}',$('#commodityNo').val(),'${details.id}');"/>
                                                <cong:img src="${path}/images/error.png" style="cursor:pointer" width="16" height="16" alt="delete"
                                                          onclick="deleteDetail('Are you sure you want to delete?','${dimFlag}','${details.id}','${details.lclBookingPiece.id}','${details.actualUom}',$('#commodityNo').val());deleteDetails('${count-1}')"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </cong:div>
                        <cong:div id="metDetailList" style="display:none;width:100%;">
                            <table width="100%" border="0"  class="dataTable" id="dimTable">
                                <thead>
                                    <tr>
                                        <th>Length(CM)</th>
                                        <th>Width(CM)</th>
                                        <th>Height(CM)</th>
                                        <th>Weight Per Piece(KGS)</th>
                                        <th>Piece</th>
                                        <th>Warehouse Line</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <c:set var="count" value="0"/>
                                <tbody>
                                    <c:forEach items="${detailListMet}" var="details">
                                        <c:set var="count" value="${count+1}"/>
                                        <c:choose>
                                            <c:when test="${rowStyle eq 'oddStyle'}">
                                                <c:set var="rowStyle" value="evenStyle"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="rowStyle" value="oddStyle"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <tr class="${rowStyle}">
                                            <td>${details.actualLength}</td>
                                            <td>${details.actualWidth}</td>
                                            <td>${details.actualHeight}</td>
                                            <td>${details.actualWeight}</td>
                                            <td>${details.pieceCount}</td>
                                            <td style="text-transform: uppercase">${details.stowedLocation}</td>
                                            <td>
                                                <cong:img src="${path}/images/edit.png" styleClass="addCommodity" style="cursor:pointer" width="16" height="16" alt="edit"
                                                          onclick="editDetails('${path}','${count-1}','${details.lclBookingPiece.id}','${dimFlag}','${details.actualUom}',$('#commodityNo').val(),'${details.id}');"/>
                                                <cong:img src="${path}/images/error.png" style="cursor:pointer" width="16" height="16" alt="delete"
                                                          onclick="deleteDetail('Are you sure you want to delete?','${dimFlag}','${details.id}','${details.lclBookingPiece.id}','${details.actualUom}');deleteDetails('${count-1}')"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </cong:div>
                    </cong:td>
            </cong:tr>
        </cong:table>
        <input type="hidden" name="bkgWeightMetric" id="bkgWeightMetric"/>
        <input type="hidden" name="bkgVolumeMetric" id="bkgVolumeMetric"/>
        <input type="hidden" name="bkgWeightVolume" id="bkgWeightImperial"/>
        <input type="hidden" name="bkgVolumeVolume" id="bkgVolumeImperial"/>
        <input type="hidden" name="oldDestFee" id="oldDestFee" value="${bookingExport.includeDestfees}"/>
        <cong:hidden name="lengthDims" id="lengthDims"/>
        <cong:hidden name="acWidthDims" id="acWidthDims"/>
        <cong:hidden name="heightDims" id="heightDims"/>
        <cong:hidden name="piecesDims" id="piecesDims"/>
        <cong:hidden name="weightDims" id="weightDims"/>
        <cong:hidden name="warehouseTabDims" id="warehouseTabDims"/>
    </cong:form>
</body>
<script type="text/javascript">
    function saveDims() {
        // alert($('#totalMeasureImp').val())
        //alert(document.getElementById("bookedVolumeImperial"));
        $('#bookedVolumeImperial').val($('#totalMeasureImp').val());
        parent.$.fn.colorbox.close();
    }

    $(document).ready(function () {
        var moduleName = parent.parent.$('#moduleName').val();
        if (moduleName === "Imports") {
            $("#hideIdImports1").hide();
            $("#hideIdImports2").hide();
            $("#hideIdImports3").hide();
        } 
        $('#bkgWeightMetric').val(parent.$('#bookedWeightMetric').val())
        $('#bkgVolumeMetric').val(parent.$('#bookedVolumeMetric').val())
        $('#bkgWeightImperial').val(parent.$('#bookedWeightImperial').val())
        $('#bkgVolumeImperial').val(parent.$('#bookedVolumeImperial').val())
        $("#delModuleName").val(parent.parent.$('#moduleName').val());
        if ($("#delModuleName").val() === 'Imports') {
            $("#delButton").hide();
        }
        var ups = parent.$('input:radio[name=ups]:checked').val();
        parent.parent.$("#ups").val(ups);
        parent.$("#methodFlag").val($("#dimFlag").val());
    });

    function submitAjaxForm(path, methodName) {
        var actualUom = $('input:radio[name=actualUom]:checked').val();
        var rowlen = "";
        var fileStatus = parent.$('#status').val();
        var fileNumberId = $("#fileNumberId").val();
        var totalMeasureImp = $("#totalMeasureImp").val();
        var ups = parent.$('input:radio[name=ups]:checked').val();
        parent.parent.$("#ups").val(ups);
        var smalParcel = parent.$("input:radio[name='ups']:checked").val();
        if (smalParcel === "true") {
            parent.$("#smallParcelFlag").val("true");
        }
        if (actualUom == 'I') {
            rowlen = document.getElementById("dimsTable").rows.length;
        } else {
            rowlen = document.getElementById("dimTable").rows.length;
        }
        if (rowlen != "" && rowlen > 1) {
            var totalMeasureImp = $('#totalMeasureImp').val();
            var totalWeightImp = $('#totalWeightImp').val();
            var totalMeasureMet = $('#totalMeasureMet').val();
            var totalWeightMet = $('#totalWeightMet').val();
            if (methodName == 'updateActualComm') {
                parent.$('#actualPieceCount').val($('#totalPieces').val());
                var moduleName = parent.parent.$('#moduleName').val();
                var role = parent.parent.$("#actualFieldsChangeAfterRelease").val();
                if ("Exports" === moduleName && role === 'false' && fileStatus !== 'WV' && (isCheckedReleaseStatus(fileNumberId) || fileStatus === 'L' || fileStatus === 'M')) {
                    $.prompt('Cannot update actual fields after release,Check in roleduty to update these fields!');
                    return false;
                }
            } else {
                parent.$('#bookedPieceCount').val($('#totalPieces').val());
            }
            if ((Number(totalMeasureImp) >= 100000) || (Number(totalMeasureMet) >= 100000)) {
                $.prompt('Shipment Size must be Less than 100,000 CFT');
                return false;
            }
            if ((Number(totalWeightImp) >= 100000) || (Number(totalWeightMet) >= 100000)) {
                $.prompt('Shipment Size must be Less than 100,000 LBS');
                return false;
            }
            var bkgWeightMetric = $("#bkgWeightMetric").val();
            var bkgVolumeMetric = $("#bkgVolumeMetric").val();
            var bkgWeightImperial = $("#bkgWeightImperial").val();
            var bkgVolumeImperial = $("#bkgVolumeImperial").val();
            if ((bkgWeightMetric == null || bkgWeightMetric == "") && (bkgVolumeMetric == null || bkgVolumeMetric == "")
                    && (bkgWeightImperial == null || bkgWeightImperial == "") && (bkgVolumeImperial == null || bkgVolumeImperial == "")) {
                parent.parent.$("#saveRemarks").val("true");
                showLoading();
                parent.$("#methodName").val(methodName);
                parent.$('#totalMeasureImp').val($('#totalMeasureImp').val());
                parent.$('#totalWeightImp').val($('#totalWeightImp').val());
                parent.$('#totalMeasureMet').val($('#totalMeasureMet').val());
                parent.$('#totalWeightMet').val($('#totalWeightMet').val());
                parent.$('#totalPieces').val($('#totalPieces').val());
                parent.$('#actualUom').val(actualUom);
                parent.$("#lclCommodityForm").submit();
            } else {
                var fileNumberId = $('#fileNumberId').val();
                var fileNumber = parent.parent.$('#fileNumber').val();
                var bookingPieceId = $('#bookingPieceId').val();
                var editDimFlag = parent.$('#editDimFlag').val();
                var cargoReceived = parent.$('#cargoReceived').val();
                var copyFnVal = parent.parent.$('#copyFnVal').val();
                var origin = parent.parent.getOrigin();
                var status = parent.$('#status').val();
                var transhipment = "";
                var destination = parent.parent.getDestination();
                if (destination === '007UN') {
                    destination = 'PRSJU';
                }
                var rateType = parent.parent.$("input:radio[name='rateType']:checked").val();
                var moduleName = parent.parent.$('#moduleName').val();
                if (moduleName === 'Imports') {
                    transhipment = parent.parent.$("input:radio[name='transShipMent']:checked").val();
                }
                $.prompt('Do you want to Override Existing Values?', {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        var userId = parent.$("#loginUserId").val();
                        if (v === 1) {
                            if (bookingPieceId == null && bookingPieceId == undefined && bookingPieceId == "") {
                                parent.parent.$("#saveRemarks").val("true");
                                showLoading();
                                var href = path + "/lclCommodity.do?methodName=editLclCommodity&id=" + bookingPieceId + "&editDimFlag=" + editDimFlag + "&fileNumber=" + fileNumber;
                                href = href + "&origin=" + origin + "&destination=" + destination + "&rateType=" + rateType + "&fileNumberId=" + fileNumberId + "&copyVal=" + copyFnVal + "&cargoReceived=" + cargoReceived;
                                href = href + "&transhipment=" + transhipment + "&moduleName=" + moduleName + "&status=" + status + "&shortShipFileNo=" + parent.$('#shortShipFileNo').val();
                                parent.document.location.href = href;
                            } else {
                                parent.parent.$("#saveRemarks").val("true");
                                showLoading();
                                parent.$("#methodName").val(methodName);
                                parent.$('#totalMeasureImp').val($('#totalMeasureImp').val());
                                parent.$('#totalWeightImp').val($('#totalWeightImp').val());
                                parent.$('#totalMeasureMet').val($('#totalMeasureMet').val());
                                parent.$('#totalWeightMet').val($('#totalWeightMet').val());

                                parent.$('#totalPieces').val($('#totalPieces').val());
                                parent.$('#actualUom').val(actualUom);
                                parent.$("#lclCommodityForm").submit();
                            }
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.lcl.dwr.LclDwr",
                                    methodName: "addDimsOverideRemarks",
                                    param1: fileNumberId,
                                    param2: userId,
                                    param3: "Override Existing Values-Yes"

                                },
                                async: false,
                                success: function (data) {

                                }
                            });

                        }
                        else if (v == 2) {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.lcl.dwr.LclDwr",
                                    methodName: "addDimsOverideRemarks",
                                    param1: fileNumberId,
                                    param2: userId,
                                    param3: "Override Existing Values-No"

                                },
                                async: false,
                                success: function (data) {
                                }
                            });
                            $('#actualLength').focus().val();
                            $.prompt.close();
                        }
                    }
                });
            }
        } else {
            $('#actualLength').focus().val();
            $.prompt('Please add atleast one commodity Details');
        }


    }
    function submitForm(methodName, formName) {
        showLoading();
        var bookingPieceId = $('#bookingPieceId').val();
        var id = $('#id').val();
        var actualLength = $('#actualLength').val();
        var dimFlag = $('#dimFlag').val();
        var actualWidth = $('#actualWidth').val();
        var actualHeight = $('#actualHeight').val();
        var actualWeight = $('#actualWeight').val();
        var pieceCount = $('#pieceCount').val();
        var warehouse = $('#warehouse').val();
        var commodityNo = $('#commodityNo').val();
        var fileNumberId = $('#fileNumberId').val();
        var fileNumber = $('#fileNumber').val();
        var packageTypeId = $('#packageTypeId').val();
        var packageType = $('#packageType').val();
        var uom = $('input:radio[name=actualUom]:checked').val();
        var url = "${path}/commodityDetails.do?methodName=" + methodName + "&bookingPieceId=" + bookingPieceId + "&id=" + id + "&fileNumberId=" + fileNumberId + "&uom=" + uom + "&warehouse=" + warehouse + "&actualLength=" + actualLength + "&actualWeight=" + actualWeight + "&actualWidth=" + actualWidth + "&actualHeight=" + actualHeight + "&pieceCount=" + pieceCount + "&commodityNo=" + commodityNo + "&dimFlag=" + dimFlag + "&packageTypeId=" + packageTypeId + "&packageType=" + packageType + "&fileNumber=" + fileNumber;
        window.location = url;
    }
    function editDetails(path, id, bookingPieceId, dimFlag, uom, commodityNo, detailId) {
        showLoading();
        var fileNumberId = $('#fileNumberId').val();
        var fileNumber = $('#fileNumber').val();
        var packageTypeId = $('#packageTypeId').val();
        var packageType = $('#packageType').val();
        if (dimFlag == "" || dimFlag == null) {
            var dimFlag = $('#dimFlag').val();
        }
        var url = path + "/commodityDetails.do?methodName=editDetails&id=" + id + "&bookingPieceId=" + bookingPieceId + "&dimFlag=" + dimFlag + "&uom=" + uom + "&commodityNo=" + commodityNo + "&fileNumberId=" + fileNumberId + "&detailId=" + detailId + "&packageTypeId=" + packageTypeId + "&packageType=" + packageType + "&fileNumber=" + fileNumber;
        window.location = url;
        changeUom();
    }
    function deleteDetails(id) {
        $('#id').val(id);
    }
    function deleteDetail(txt, dimFlag, detailid, pieceId, uom, commodityNo) {
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v == 1) {
                    parent.parent.$("#isMeasureImpChanged").val("true");
                    showLoading();
                    var id = $('#id').val();
                    var fileNumberId = $('#fileNumberId').val();
                    var fileNumber = $('#fileNumber').val();
                    var packageTypeId = $('#packageTypeId').val();
                    var packageType = $('#packageType').val();
                    if (dimFlag == "" || dimFlag == null) {
                        var dimFlag = $('#dimFlag').val();
                    }
                    //var bookingPieceId=$('#bookingPieceId').val();
                    var url = "commodityDetails.do?methodName=deleteDetails&id=" + id + "&dimFlag=" + dimFlag + "&uom=" + uom + "&commodityNo=" + commodityNo + "&detailid=" + detailid + "&bookingPieceId=" + pieceId + "&fileNumberId=" + fileNumberId + "&packageTypeId=" + packageTypeId + "&packageType=" + packageType + "&fileNumber=" + fileNumber;
                    window.location = url;
                    hideProgressBar();
                    $.prompt.close();
                    var rowLength = $("#actualUomM").is(":checked") ? document.getElementById("dimTable").rows.length : document.getElementById("dimsTable").rows.length;
                    if (rowLength === 2) {
                        parent.emptyDims(); //change DIMS Button color
                    }
                }
                else if (v == 2) {
                    $('#id').val('')
                    $.prompt.close();
                }
            }
        });
    }
    function showHideMeasureLabel(obj) {
        var val = obj.value;
        if (val == 'M') {
            $('.measure-imp').hide();
            $('.measure-met').show();
            $('#impDetailList').hide();
            $('#metDetailList').show();
            $('#uom').val(val);
        }
        if (val == 'I') {
            $('.measure-imp').show();
            $('.measure-met').hide();
            $('#impDetailList').show();
            $('#metDetailList').hide();
            $('#uom').val(val);
        }
    }
    function sampleAlert(txt) {
        $.prompt(txt);
    }
    function checkForNumberAndDecimal(obj) {
        var result;
        if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
            obj.value = "";
            sampleAlert("This field should be Numeric");

        }
    }
    function checkForNumber(evt) {
        var charCode = (evt.which) ? evt.which : event.keyCode
        if ((charCode < 48 || charCode > 57) && ((charCode < 95 || charCode > 105))) {
            if (charCode != 9 && charCode != 8) {
                evt.value = "";
                sampleAlert("This field should be Numeric");
            }
        }
    }
    function showValue() {
        if ($("#actualUomM").is(":checked")) {
            $('#actualM').show();
            $('#actualI').hide();
        }
        if ($("#actualUomI").is(":checked")) {
            $('#actualI').show();
            $('#actualM').hide();
        }
    }

    function changeUom() {
        var uom = $('#uom').val();
        if (uom == 'M') {
            $('#actualUomM').attr('checked', true);
            $('#actualUomI').attr('disabled', true);
        }
        if (uom == 'I') {
            $('#actualUomI').attr('checked', true);
            $('#actualUomM').attr('disabled', true);
        }
    }

    function isCheckedReleaseStatus(fileNumberId) {
        var flag = false;
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingExportDAO",
                methodName: "getReleasedDateTime",
                param1: fileNumberId

            },
            async: false,
            success: function (data) {
                flag = data;
            }
        });
        return flag;
    }

    document.onkeydown = mykeyhandler;

    function mykeyhandler() {
        var activeElType = document.activeElement.type;
        var activeNodeName = document.activeElement.nodeName;
        if (window.event && window.event.keyCode == 8 && activeElType != 'text') {
            // try to cancel the backspace
            window.event.cancelBubble = true;
            window.event.returnValue = false;
            return false;
        }
    }
    function deleteAll() {
//        var deleteAllpiece = $('#deleteAllBooking').val();
        var bookingPieceId = $("#bookingPieceId").val();
        var fileNumber = $("#fileNumber").val();
        var fileNumberId = $("#fileNumberId").val();
        var dimValue = $("#dimFlag").val();
        $.prompt('This will delete all dimension line items, are you sure?', {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function (v) {
                if (v === 1) {
                    showLoading();
                    var url = "commodityDetails.do?methodName=deleteAllPiece&bookingPieceId=" + bookingPieceId + "&dimFlag=" + dimValue
                            + "&fileNumber=" + fileNumber + "&fileNumberId=" + fileNumberId;
                    window.location = url;
                    hideProgressBar();
                    $.prompt.close();
                    parent.emptyDims();
                }
                else if (v === 2) {
                    $.prompt.close();
                }
            }
        });
    }
    function MultipleDims(path) {
        var error = true;
        var fileNumberId = $("#fileNumberId").val();
        var fileStatus = parent.$('#status').val();
        var moduleName = parent.parent.$('#moduleName').val();
        var role = parent.parent.$("#actualFieldsChangeAfterRelease").val();
        if ("Exports" === moduleName && role === 'false' && fileStatus !== 'WV' && (isCheckedReleaseStatus(fileNumberId) || fileStatus === 'L' || fileStatus === 'M')) {
            $.prompt('Cannot update actual fields after release,Check in roleduty to update these fields!');
            error = false;
            return false;
        }
        if (error) {
            var bookingPieceId = $('#bookingPieceId').val();
            var packageTypeId = $('#packageTypeId').val();
            var packageType = $('#packageType').val();
            var fileNumber = $('#fileNumber').val();
            var uom = $('input:radio[name=actualUom]:checked').val();
            var rowLength = (uom === "I") ? document.getElementById("dimsTable").rows.length : document.getElementById("dimTable").rows.length;
            var href = path + "/commodityDetails.do?methodName=displayMultipleDims&bookingPieceId=" + bookingPieceId +
                    "&fileNumberId=" + fileNumberId + "&packageTypeId=" + packageTypeId +
                    "&packageType=" + packageType + "&fileNumber=" + fileNumber + "&uom=" + uom + "&rowLength=" + rowLength;
            $.colorbox({
                iframe: true,
                href: href,
                width: "100%",
                height: "90%",
                title: "Multiple Commodity Details",
            });
        }

    }

    function selectedMenultiDims(uom) {
        $("#uom").val(uom);
        document.commodityDetailsForm.uom.value = uom;
        showLoading();
        $("#methodName").val("saveMultiDims");
        $("#commodityDetailsForm").submit();
    }

</script>
</html>



