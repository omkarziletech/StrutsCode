<%--
    Document   : commodityDetails
    Created on : Jan 29, 2012, 1:19:34 PM
    Author     : lakshh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/taglib.jsp" %>
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
            $('#saveDetail').click(function () {
                var error = true;
                $(".mandatory").each(function () {
                    if ($(this).val().length == 0) {
                        $.prompt('This field is required');
                        error = false;
                        $(this).css("border-color", "red");
                        $(this).focus();
                        return false;
                    }
                });
                if (error) {
                    parent.parent.$("#isMeasureImpChanged").val("true");
                    submitForm('addDetails', '#quoteCommodityDetailsForm');
                }
            });
            var uom = $('#uom').val();
            if (uom == 'M') {
                var rowlen = document.getElementById("dimTable").rows.length;
                if (rowlen > 1) {
                    changeUom();
                    $('.measure-imp').hide();
                    $('.measure-met').show();
                    $('#impDetailList').hide();
                    $('#metDetailList').show();
                    $('#actualM').show();
                    $('#actualI').hide();
                }
            }
            if (uom == 'I') {
                var rowlen = document.getElementById("dimTable").rows.length;
                if (rowlen > 1) {
                    changeUom();
                }
                $('.measure-imp').show();
                $('.measure-met').hide();
                $('#impDetailList').show();
                $('#metDetailList').hide();
                $('#actualM').hide();
                $('#actualI').show();
            }

            if ($("#actualUomI").is(":checked")) {
                var rowslen = document.getElementById("dimsTable").rows.length;
                if (rowslen > 1) {
                    changeUom();
                }
            }
            if ($("#actualUomM").is(":checked")) {
                var rowlen = document.getElementById("dimTable").rows.length;
                if (rowlen > 1) {
                    changeUom();
                }
            }
        });
    </script>
    <body>
        <cong:form name="quoteCommodityDetailsForm" id="quoteCommodityDetailsForm" action="/quoteCommodityDetails.do">
            <input type="hidden" name="bookingPieceId" id="bookingPieceId" value="${quoteCommodityDetailsForm.bookingPieceId}"/>
            <input type="hidden" name="fileNumberId" id="fileNumberId" value="${quoteCommodityDetailsForm.fileNumberId}"/>
            <input type="hidden" name="commodityNo" id="commodityNo" value="${quoteCommodityDetailsForm.commodityNo}"/>
            <input type="hidden" name="countVal" id="countVal" value="${countVal}"/>
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="fileNumber" id="fileNumber" value="${quoteCommodityDetailsForm.fileNumber}"/>
            <cong:hidden name="qtPieceDetailId" id="qtPieceDetailId"/>
            <cong:hidden name="editDetailId" id="editDetailId" value="${editDetailId}"/>
            <cong:hidden name="uom" id="uom" value="${quoteCommodityDetailsForm.uom}"/>
            <cong:hidden name="moduleName" id="moduleName" value="${quoteCommodityDetailsForm.moduleName}"/>
            <cong:hidden name="ups" id ="ups" />
            <input type="hidden" name="id" id="id" value="${id}"/>
            <cong:hidden name="dimFlag" id="dimFlag" value="${dimFlag}"/>
            <cong:div styleClass="tableHeadingNew" style="width:97%">Commodity Details <span style="color:red">${quoteCommodityDetailsForm.fileNumber}</span>
                <cong:div styleClass="floatRight" style="width:20%">UOM
                    <cong:radio name="actualUom" value="M" id="actualUomM" onclick="showHideMeasureLabel(this);showValue();"/>M
                    <cong:radio name="actualUom" value="I" id="actualUomI" onclick="showHideMeasureLabel(this);showValue();"/>I
                    <input type="hidden" name="duptotalMeasureImp" id="duptotalMeasureImp" value="${quoteCommodityDetailsForm.duptotalMeasureImp}"/>
                    <input type="hidden" name="duptotalMeasureMet" id="duptotalMeasureMet" value="${quoteCommodityDetailsForm.duptotalMeasureMet}"/>
                    <input type="hidden" name="duptotalWeightImp" id="duptotalWeightImp" value="${quoteCommodityDetailsForm.duptotalWeightImp}"/>
                    <input type="hidden" name="duptotalWeightMet" id="duptotalWeightMet" value="${quoteCommodityDetailsForm.duptotalWeightMet}"/>
                </cong:div>
            </cong:div>
            <cong:table  width="100%" border="0">
                <cong:tr id="actualI" styleClass="textBoldforlcl">
                    <cong:td width="12%">Length(IN)</cong:td>
                    <cong:td width="12%">Width(IN)</cong:td>
                    <cong:td width="12%">Height(IN)</cong:td>
                    <cong:td width="12%">Pieces</cong:td>
                    <cong:td width="12%">Weight/PC(LBS)</cong:td>
                    <cong:td  width="12%">WarehouseLine</cong:td>
                    <cong:td width="8%"></cong:td>
                    <cong:td width="8%" id="hideIdImports1"></cong:td>
                </cong:tr>
                <cong:tr id="actualM" style="display:none" styleClass="textBoldforlcl">
                    <cong:td width="12%">Length(CMS)</cong:td>
                    <cong:td width="12%">Width(CMS)</cong:td>
                    <cong:td width="12%">Height(CMS)</cong:td>
                    <cong:td width="12%">Pieces</cong:td>
                    <cong:td width="12%">Weight/PC(KGS)</cong:td>
                    <cong:td width="12%">WarehouseLine</cong:td>
                    <cong:td width="8%"></cong:td>
                    <cong:td width="8%" id="hideIdImports2"></cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td><cong:text styleClass="textsmall mandatory weight" name="actualLength" id="actualLength" value="${quotePieceDetail.actualLength}" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall mandatory weight" name="actualWidth" id="actualWidth" value="${quotePieceDetail.actualWidth}" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall mandatory weight" name="actualHeight" id="actualHeight" value="${quotePieceDetail.actualHeight}" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall mandatory" name="pieceCount" id="pieceCount" value="${quotePieceDetail.pieceCount}" container="NULL" onkeyup="checkForNumber(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall weight" name="actualWeight" id="actualWeight" value="${quotePieceDetail.actualWeight}" container="NULL" onkeyup="checkForNumberAndDecimal(this)"/></cong:td>
                    <cong:td><cong:text styleClass="textsmall" style="text-transform: uppercase" name="warehouse" id="warehouse" value="${quotePieceDetail.stowedLocation}" container="NULL"/></cong:td>
                    <cong:td><input type="button"  value="Add" class="button-style1" id="saveDetail"/>  </cong:td>
                    <cong:td id="hideIdImports3"><input type="button"  value="Multiple" class="button-style1 multipleOpt" id="multipleDetail" onclick="MultipleDims('${path}');"/>  </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="8">
                            <cong:table  width="100%" border='0'>
                                <cong:tr styleClass="textBoldforlcl">
                                    <cong:td styleClass="measure-imp" width="14%">Total Measure(I):</cong:td>
                                    <cong:td styleClass="measure-imp" colspan="1" width="22%">
                                        <cong:text name="totalMeasureImp" styleClass="textsmall text-border"
                                                   readOnly="true" container="NULL"  id="totalMeasureImp" value="${quoteCommodityDetailsForm.totalMeasureImp}"/>(CFT)
                                    </cong:td>
                                    <cong:td styleClass="measure-met" width="14%">Total Measure(M):</cong:td>
                                    <cong:td styleClass="measure-met" colspan="1" width="22%">
                                        <cong:text name="totalMeasureMet" styleClass="textsmall text-border" readOnly="true"
                                                   container="NULL"  id="totalMeasureMet" value="${quoteCommodityDetailsForm.totalMeasureMet}"/>(CBM)
                                        <!--                                    Measure(M)=Measure(I)/35.3146-->
                                    </cong:td>
                                    <cong:td styleClass="measure-imp" width="16%">Total Weight(I):</cong:td>
                                    <cong:td styleClass="measure-imp" width="22%">
                                        <cong:text name="totalWeightImp" styleClass="textsmall" container="NULL"  id="totalWeightImp" value="${quoteCommodityDetailsForm.totalWeightImp}"/>(LBS)
                                    </cong:td>
                                    <cong:td styleClass="measure-met" width="38%">Total Weight(M):
                                        <cong:text name="totalWeightMet" styleClass="textsmall text-border" readOnly="true" container="NULL"
                                                   id="totalWeightMet" value="${quoteCommodityDetailsForm.totalWeightMet}"/>(KGS)
                                        <!--                                    Weight(M)=Measure(I)/2.2046-->
                                    </cong:td>
                                    <cong:td width="10%">Total Pieces:</cong:td>
                                    <cong:td>
                                        <cong:text name="totalPieces" styleClass="textsmall text-border"
                                                   readOnly="true" container="NULL"  id="totalPieces" value="${quoteCommodityDetailsForm.totalPieces}"/>
                                    </cong:td>
                                    <cong:td width="6%">
                                        <input type="hidden" name="packageTypeId" id="packageTypeId" value="${pack}"/>
                                        <input type="button"  class="button-style1" value="Save"
                                               onclick="submitAjaxForm('updateBookedComm', '#quoteCommodityDetailsForm', '#bookedField', '#bookingPieceId')"/>
                                    </cong:td>
                                    <cong:td>
                                        <c:set var="moduleName"  value="${quoteCommodityDetailsForm.moduleName}"/> 
                                        <c:if test="${moduleName ne 'Imports'}">
                                            <c:forEach items="${detailListMet}" varStatus="row">
                                                <c:choose>
                                                    <c:when test="${row.count==1}">
                                                        <input type="button" class="button-style1" value="Del" onclick="quoteDeleteAll();"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </c:if>
                                    </cong:td>

                                </cong:tr>
                            </cong:table>
                        </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="8">
                            <cong:div id="impDetailList"  style="width:100%">
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
                                                              onclick="editDetails('${path}','${count-1}','${details.quotePiece.id}','${dimFlag}','${details.actualUom}','${details.id}');"/>
                                                    <cong:img src="${path}/images/error.png" style="cursor:pointer" width="16" height="16" alt="delete"
                                                              onclick="deleteDetail('Are you sure you want to delete?','${dimFlag}','${details.id}','${details.quotePiece.id}','${details.actualUom}');deleteDetails('${count-1}')"/>
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
                                                              onclick="editDetails('${path}','${count-1}','${details.quotePiece.id}','${dimFlag}','${details.actualUom}','${details.id}');"/>
                                                    <cong:img src="${path}/images/error.png" style="cursor:pointer" width="16" height="16" alt="delete"
                                                              onclick="deleteDetail('Are you sure you want to delete?','${dimFlag}','${details.id}','${details.quotePiece.id}','${details.actualUom}');deleteDetails('${count-1}')"/>
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
            <cong:hidden name="lengthDims" id="lengthDims"/>
            <cong:hidden name="acWidthDims" id="acWidthDims"/>
            <cong:hidden name="heightDims" id="heightDims"/>
            <cong:hidden name="piecesDims" id="piecesDims"/>
            <cong:hidden name="weightDims" id="weightDims"/>
            <cong:hidden name="warehouseTabDims" id="warehouseTabDims"/>
        </cong:form>
    </body>
    <script type="text/javascript">

        $(document).ready(function () {
            var moduleName = parent.parent.$('#moduleName').val();
            if (moduleName === "Imports") {
                $("#hideIdImports1").hide();
                $("#hideIdImports2").hide();
                $("#hideIdImports3").hide();
            }
            $('#bkgWeightMetric').val(parent.$('#bookedWeightMetric').val());
            $('#bkgVolumeMetric').val(parent.$('#bookedVolumeMetric').val());
            $('#bkgWeightImperial').val(parent.$('#bookedWeightImperial').val());
            $('#bkgVolumeImperial').val(parent.$('#bookedVolumeImperial').val());
            parent.$("#smalParcelMeasureImp").val($('#totalMeasureImp').val());
            $('input:radio[name=actualUom]:checked').val($('#uom').val());
        });
        function submitAjaxForm(methodName, formName, selector, id) {
            var bkgWeightMetric = $("#bkgWeightMetric").val();
            var bkgVolumeMetric = $("#bkgVolumeMetric").val();
            var bkgWeightImperial = $("#bkgWeightImperial").val();
            var bkgVolumeImperial = $("#bkgVolumeImperial").val();
            var upsParent = parent.$("input:radio[name=ups]:checked").val();
            var smallparcel = parent.$('input:radio[name=ups]:checked').val();
            var totalMeasureImp = $('#totalMeasureImp').val();
            $('#ups').val(upsParent);
            if (smallparcel === "true") {
                parent.$("#smallParcelFlag").val("true");
                parent.$("#smalParcelMeasureImp").val(totalMeasureImp);
            }
            if ((bkgWeightMetric == null || bkgWeightMetric == "") && (bkgVolumeMetric == null || bkgVolumeMetric == "")
                    && (bkgWeightImperial == null || bkgWeightImperial == "") && (bkgVolumeImperial == null || bkgVolumeImperial == "")) {
                parent.parent.$("#saveRemarks").val("true");
                var totalMeasureImp = $('#totalMeasureImp').val();
                var totalWeightImp = $('#totalWeightImp').val();
                var totalMeasureMet = $('#totalMeasureMet').val();
                var totalWeightMet = $('#totalWeightMet').val();
                parent.$('#bookedPieceCount').val($('#totalPieces').val());
                if ((Number(totalMeasureImp) >= 100000) || (Number(totalMeasureMet) >= 100000)) {
                    $.prompt('Shipment Size must be Less than 100,000 CFT');
                    return false;
                }
                else if ((Number(totalWeightImp) >= 100000) || (Number(totalWeightMet) >= 100000)) {
                    $.prompt('Shipment Size must be Less than 100,000 LBS');
                    return false;
                } else {
                    var actualUom = $('input:radio[name=actualUom]:checked').val();
                    var rowlen = "";
                    if (actualUom == 'I') {
                        rowlen = document.getElementById("dimsTable").rows.length;
                    } else {
                        rowlen = document.getElementById("dimTable").rows.length;
                    }
                    if (rowlen != "" && rowlen > 1) {
                        $("#methodName").val(methodName);
                        var params = $(formName).serialize();
                        params += "&id=" + id;
                        $.post($(formName).attr("action"), params,
                                function (data) {
                                    $(selector).html(data);
                                    $(selector, window.parent.document).html(data);
                                    parent.$.fn.colorbox.close();
                                });
                    } else {
                        $.prompt('Please add atleast one Commodity Details');
                    }
                }
            } else {
                var totalMeasureImp = $('#totalMeasureImp').val();
                var totalWeightImp = $('#totalWeightImp').val();
                var totalMeasureMet = $('#totalMeasureMet').val();
                var totalWeightMet = $('#totalWeightMet').val();
                parent.$('#bookedPieceCount').val($('#totalPieces').val());
                $('#ups').val(upsParent);
                if ((Number(totalMeasureImp) >= 100000) || (Number(totalMeasureMet) >= 100000)) {
                    $.prompt('Shipment Size must be Less than 100,000 CFT');
                    return false;
                }
                else if ((Number(totalWeightImp) >= 100000) || (Number(totalWeightMet) >= 100000)) {
                    $.prompt('Shipment Size must be Less than 100,000 LBS');
                    return false;
                }
                else {
                    var actualUom = $('input:radio[name=actualUom]:checked').val();
                    var rowlen = "";
                    if (actualUom == 'I') {
                        rowlen = document.getElementById("dimsTable").rows.length;
                    } else {
                        rowlen = document.getElementById("dimTable").rows.length;
                    }
                    if (rowlen != "" && rowlen > 1) {
                        $.prompt('Do you want to Override Existing Values?', {
                            buttons: {
                                Yes: 1,
                                No: 2
                            },
                            submit: function (v) {
                                if (v == 1) {
                                    parent.parent.$("#saveRemarks").val("true");
                                    showProgressBar();
                                    $("#methodName").val(methodName);
                                    var params = $(formName).serialize();
                                    params += "&id=" + id;
                                    $.post($(formName).attr("action"), params,
                                            function (data) {
                                                $(selector).html(data);
                                                $(selector, window.parent.document).html(data);
                                                parent.$.fn.colorbox.close();
                                            });
                                    hideProgressBar();
                                    $.prompt.close();
                                }
                                else if (v == 2) {
                                    $('#actualLength').focus().val();
                                    $.prompt.close();
                                }
                            }
                        });
                    } else {
                        $('#actualLength').focus().val();
                        $.prompt('Please add atleast one commodity Details');
                    }
                }
            }
            parent.showDimsDetails();
        }
        function submitForm(methodName, formName) {
            document.quoteCommodityDetailsForm.fileNumberId.value = parent.parent.$("#fileNumberId").val();
            document.quoteCommodityDetailsForm.qtPieceDetailId.value = parent.$("#quotePieceId").val();
            document.quoteCommodityDetailsForm.bookingPieceId.value = parent.$("#quotePieceId").val();
            document.quoteCommodityDetailsForm.uom.value = $('input:radio[name=actualUom]:checked').val();
            document.quoteCommodityDetailsForm.editDetailId.value = $("#editDetailId").val();
            $("#uom").val($('input:radio[name=actualUom]:checked').val());
            document.quoteCommodityDetailsForm.uom.value = $('input:radio[name=actualUom]:checked').val();
            showLoading();
            $("#methodName").val(methodName);
            $("#quoteCommodityDetailsForm").submit();
        }

        function editDetails(path, id, bookingPieceId, dimFlag, uom, detailId) {
            if (detailId != null && detailId != "") {
                id = detailId;
            }
            document.quoteCommodityDetailsForm.fileNumberId.value = parent.parent.$("#fileNumberId").val();
            document.quoteCommodityDetailsForm.qtPieceDetailId.value = parent.$("#quotePieceId").val();
            document.quoteCommodityDetailsForm.bookingPieceId.value = parent.$("#quotePieceId").val();
            document.quoteCommodityDetailsForm.uom.value = $('input:radio[name=actualUom]:checked').val();
            $("#uom").val($('input:radio[name=actualUom]:checked').val());
            var countVal = $("#countVal").val();
            showLoading();
            var commodityNo = $('#commodityNo').val();
            var fileNumberId = $('#fileNumberId').val();
            var moduleName = $('#moduleName').val();
            var fileNumber = $('#fileNumber').val();
            var url = path + "/quoteCommodityDetails.do?methodName=editDetails&id=" + id + "&bookingPieceId=" + bookingPieceId +
                    "&dimFlag=" + dimFlag + "&uom=" + uom + "&commodityNo=" + commodityNo + "&fileNumberId=" + fileNumberId +
                    "&qtPieceDetailId=" + detailId + "&moduleName=" + moduleName + "&fileNumber=" + fileNumber + "&countVal=" + countVal;
            window.location = url;
            changeUom();
        }
        function deleteDetails(id) {
            $('#id').val(id);
        }
        function deleteDetail(txt, dimFlag, detailid, pieceId, uom) {
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v == 1) {
                        parent.parent.$("#saveRemarks").val("true");
                        var countVal = parent.$("#countVal").val();
                        $("#countVal").val(countVal);
                        showLoading();
                        $('#qtPieceDetailId').val(detailid);
                        $("#methodName").val('deleteDetails');
                        $("#totalWeightImp").val(''); 
                        $("#totalMeasureImp").val(''); 
                        $("#totalPieces").val('');
                        $("#quoteCommodityDetailsForm").submit();
                    }
                    else if (v == 2) {
                        $('#id').val('');
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

        function quoteDeleteAll() {
//            var quoteDeleteAll = $('#bookingPieceId').val();
            var countVal = parent.$("#countVal").val();
            $("#countVal").val(countVal);
            var bookingPieceId = parent.$("#quotePieceId").val();
            var fileNumberId = parent.parent.$("#fileNumberId").val();
            var fileNumber = parent.parent.$("#fileNumber").val();
            var uom = $('input:radio[name=actualUom]:checked').val();
            var dimValue = $("#dimFlag").val();
            $.prompt('This will delete all dimension line items, are you sure?', {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        showLoading();
                        var url = "quoteCommodityDetails.do?methodName=quoteDeleteAllPiece&dimFlag=" + dimValue + "&countVal=" + countVal +
                                "&bookingPieceId=" + bookingPieceId + "&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&uom=" + uom;
                        window.location = url;
                        hideProgressBar();
                        $.prompt.close();
                    }
                    else if (v === 2)
                    {
                        $.prompt.close();
                    }
                }
            });
        }
        function MultipleDims(path) {
            var error = true;
            var fileNumberId = $("#fileNumberId").val();
            var countVal = parent.$("#countVal").val();
            if (error) {
                var bookingPieceId = $('#bookingPieceId').val();
                var fileNumber = $('#fileNumber').val();
                var uom = $('input:radio[name=actualUom]:checked').val();
                var rowLength = (uom === "I") ? document.getElementById("dimsTable").rows.length : document.getElementById("dimTable").rows.length;
                var href = path + "/quoteCommodityDetails.do?methodName=displayMultipleDims&qtPieceDetailId=" + bookingPieceId +
                        "&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&uom=" + uom + "&rowLength=" + rowLength + "&countVal=" + countVal;
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
            var countVal = parent.$("#countVal").val();
            $("#uom").val(uom);
            $("#countVal").val(countVal);
            document.quoteCommodityDetailsForm.uom.value = uom;
            document.quoteCommodityDetailsForm.qtPieceDetailId.value = parent.$("#quotePieceId").val();
            showLoading();
            $("#methodName").val("saveMultiDims");
            $("#quoteCommodityDetailsForm").submit();
        }
    </script>
</html>



